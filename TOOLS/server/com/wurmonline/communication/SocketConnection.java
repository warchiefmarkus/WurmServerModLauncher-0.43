/*     */ package com.wurmonline.communication;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketConnection
/*     */ {
/*  32 */   private static final Logger logger = Logger.getLogger(SocketConnection.class.getName());
/*     */   
/*  34 */   private static final String CLASS_NAME = SocketConnection.class.getName();
/*     */   
/*     */   public static final int BUFFER_SIZE = 262136;
/*     */   
/*  38 */   private final ByteBuffer writeBufferTmp = ByteBuffer.allocate(65534);
/*     */ 
/*     */ 
/*     */   
/*  42 */   private ByteBuffer readBuffer = ByteBuffer.allocate(262136);
/*  43 */   private ByteBuffer writeBuffer_w = null;
/*  44 */   private ByteBuffer writeBuffer_r = null;
/*     */ 
/*     */   
/*     */   public static final long timeOutTime = 300000L;
/*     */ 
/*     */   
/*     */   public static final long disconTime = 5000L;
/*     */ 
/*     */   
/*     */   private boolean connected;
/*     */   
/*     */   private boolean playerServerConnection = false;
/*     */   
/*     */   private SocketChannel socketChannel;
/*     */   
/*  59 */   private long lastRead = System.currentTimeMillis();
/*     */   private SimpleConnectionListener connectionListener;
/*  61 */   private int toRead = -1;
/*     */   
/*     */   private volatile boolean writing;
/*     */   private int bytesRead;
/*     */   private int totalBytesWritten;
/*  66 */   private int maxBlocksPerIteration = 3;
/*     */   
/*     */   private boolean isLoggedIn = true;
/*  69 */   public int ticksToDisconnect = -1;
/*     */   private Socket socket;
/*     */   private BufferedInputStream in;
/*     */   private BufferedOutputStream out;
/*  73 */   public Random encryptRandom = new Random(105773331L);
/*     */   
/*  75 */   private int remainingEncryptBytes = 0;
/*  76 */   private int encryptByte = 0;
/*  77 */   private int encryptAddByte = 0;
/*     */   
/*  79 */   public Random decryptRandom = new Random(105773331L);
/*     */ 
/*     */   
/*  82 */   private int remainingDencryptBytes = 0;
/*  83 */   private int dencryptByte = 0;
/*  84 */   private int decryptAddByte = 0;
/*     */   
/*  86 */   private static final ReentrantReadWriteLock RW_LOCK = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean callTickWritingFromTick = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SocketConnection(SocketChannel socketChannel, boolean enableNagles, boolean intraServer) throws IOException {
/* 103 */     this.socketChannel = socketChannel;
/* 104 */     socketChannel.configureBlocking(false);
/* 105 */     this.socket = socketChannel.socket();
/* 106 */     this.playerServerConnection = !intraServer;
/* 107 */     if (this.playerServerConnection) {
/*     */       
/* 109 */       this.readBuffer = ByteBuffer.allocate(262136);
/*     */       
/* 111 */       this.writeBuffer_w = ByteBuffer.allocate(32767);
/* 112 */       this.writeBuffer_r = ByteBuffer.allocate(32767);
/*     */     }
/*     */     else {
/*     */       
/* 116 */       this.readBuffer = ByteBuffer.allocate(262136);
/* 117 */       this.writeBuffer_w = ByteBuffer.allocate(262136);
/* 118 */       this.writeBuffer_r = ByteBuffer.allocate(262136);
/*     */     } 
/*     */     
/* 121 */     if (!enableNagles) {
/*     */       
/* 123 */       System.out.println("Disabling Nagles");
/* 124 */       socketChannel.socket().setTcpNoDelay(true);
/*     */     } 
/*     */ 
/*     */     
/* 128 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 130 */       logger.fine("SocketChannel validOps: " + socketChannel.validOps() + ", isConnected: " + socketChannel.isConnected() + ", isOpen: " + socketChannel
/* 131 */           .isOpen() + ", isRegistered: " + socketChannel.isRegistered() + ", socket: " + socketChannel
/* 132 */           .socket());
/*     */     }
/*     */     
/* 135 */     this.connected = true;
/*     */     
/* 137 */     this.readBuffer.clear();
/* 138 */     this.readBuffer.limit(2);
/* 139 */     this.writing = false;
/* 140 */     this.writeBuffer_w.clear();
/* 141 */     this.writeBuffer_r.flip();
/* 142 */     this.isLoggedIn = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SocketConnection(String ip, int port, boolean enableNagles) throws UnknownHostException, IOException {
/* 153 */     this.readBuffer = ByteBuffer.allocate(262136);
/* 154 */     this.writeBuffer_w = ByteBuffer.allocate(262136);
/* 155 */     this.writeBuffer_r = ByteBuffer.allocate(262136);
/* 156 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 158 */       logger.entering(CLASS_NAME, "SocketConnection", new Object[] { ip, 
/* 159 */             Integer.valueOf(port) });
/*     */     }
/*     */     
/* 162 */     this.socketChannel = SocketChannel.open();
/*     */ 
/*     */ 
/*     */     
/* 166 */     this.socketChannel.connect(new InetSocketAddress(ip, port));
/*     */ 
/*     */     
/* 169 */     if (!enableNagles) {
/*     */       
/* 171 */       System.out.println("Disabling Nagles");
/* 172 */       this.socketChannel.socket().setTcpNoDelay(true);
/*     */     } 
/*     */     
/* 175 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 177 */       logger.fine("SocketChannel validOps: " + this.socketChannel.validOps() + ", isConnected: " + this.socketChannel.isConnected() + ", isOpen: " + this.socketChannel
/* 178 */           .isOpen() + ", isRegistered: " + this.socketChannel.isRegistered() + ", socket: " + this.socketChannel
/* 179 */           .socket());
/*     */     }
/*     */     
/* 182 */     this.socketChannel.configureBlocking(false);
/*     */     
/* 184 */     this.connected = true;
/*     */     
/* 186 */     this.readBuffer.clear();
/* 187 */     this.readBuffer.limit(2);
/* 188 */     this.writing = false;
/* 189 */     this.writeBuffer_w.clear();
/* 190 */     this.writeBuffer_r.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketConnection(String ip, int port, int timeout) throws UnknownHostException, IOException {
/* 195 */     this(ip, port, timeout, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   SocketConnection(String ip, int port, int timeout, boolean enableNagles) throws UnknownHostException, IOException {
/* 201 */     this.readBuffer = ByteBuffer.allocate(262136);
/* 202 */     this.writeBuffer_w = ByteBuffer.allocate(262136);
/* 203 */     this.writeBuffer_r = ByteBuffer.allocate(262136);
/* 204 */     this.socketChannel = SocketChannel.open();
/*     */ 
/*     */ 
/*     */     
/* 208 */     this.socketChannel.socket().setSoTimeout(timeout);
/* 209 */     this.socketChannel.connect(new InetSocketAddress(ip, port));
/*     */ 
/*     */     
/* 212 */     if (!enableNagles) {
/*     */       
/* 214 */       System.out.println("Disabling Nagles");
/* 215 */       this.socketChannel.socket().setTcpNoDelay(true);
/*     */     } 
/*     */     
/* 218 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 220 */       logger.fine("SocketChannel validOps: " + this.socketChannel.validOps() + ", isConnected: " + this.socketChannel.isConnected() + ", isOpen: " + this.socketChannel
/* 221 */           .isOpen() + ", isRegistered: " + this.socketChannel.isRegistered() + ", socket: " + this.socketChannel
/* 222 */           .socket());
/*     */     }
/*     */     
/* 225 */     this.socketChannel.configureBlocking(false);
/*     */     
/* 227 */     this.connected = true;
/*     */     
/* 229 */     this.readBuffer.clear();
/* 230 */     this.readBuffer.limit(2);
/* 231 */     this.writing = false;
/* 232 */     this.writeBuffer_w.clear();
/* 233 */     this.writeBuffer_r.flip();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxBlocksPerIteration(int aMaxBlocksPerIteration) {
/* 245 */     this.maxBlocksPerIteration = aMaxBlocksPerIteration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIp() {
/* 256 */     return this.socket.getInetAddress().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getBuffer() {
/* 263 */     if (this.writing) {
/* 264 */       throw new IllegalStateException("getBuffer() called twice in a row. You probably forgot to flush()");
/*     */     }
/* 266 */     this.writing = true;
/*     */     
/* 268 */     this.writeBufferTmp.clear();
/* 269 */     return this.writeBufferTmp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearBuffer() {
/* 274 */     if (this.writing) {
/*     */       
/* 276 */       this.writing = false;
/* 277 */       this.writeBufferTmp.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnflushed() {
/* 283 */     return this.writeBuffer_w.position() + this.writeBuffer_r.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 297 */     if (!this.writing) {
/* 298 */       throw new IllegalStateException("flush() called twice in a row.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 306 */     this.writing = false;
/*     */     
/* 308 */     this.writeBufferTmp.flip();
/* 309 */     int bytesWritten = this.writeBufferTmp.limit();
/* 310 */     this.totalBytesWritten += bytesWritten;
/* 311 */     if (bytesWritten > 65524)
/* 312 */       logger.log(Level.WARNING, "WARNING Written " + bytesWritten, new Exception()); 
/* 313 */     if (this.writeBuffer_w.remaining() < bytesWritten + 2) {
/*     */       
/* 315 */       if (!tickWriting(0L)) {
/* 316 */         throw new IOException("BufferOverflow: Tried to write " + (bytesWritten + 2) + " bytes, but only " + this.writeBuffer_w
/* 317 */             .remaining() + " bytes remained. Written=" + this.totalBytesWritten + ", BufferTmp: " + this.writeBufferTmp + ", Buffer_w: " + this.writeBuffer_w + ", Buffer_r: " + this.writeBuffer_r);
/*     */       }
/*     */       
/* 320 */       logger.log(Level.INFO, "Possibly saved client crash by forcing a write of the writeBuffer_w.");
/*     */     } 
/*     */     
/* 323 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*     */       
/* 326 */       if (bytesWritten > 1 || logger.isLoggable(Level.FINEST))
/*     */       {
/* 328 */         logger.finer("Number of bytes in the write buffer: " + bytesWritten);
/*     */       }
/*     */     }
/*     */     
/* 332 */     int startPos = this.writeBuffer_w.position();
/* 333 */     this.writeBuffer_w.putShort((short)bytesWritten);
/* 334 */     this.writeBuffer_w.put(this.writeBufferTmp);
/* 335 */     int endPos = this.writeBuffer_w.position();
/*     */     
/* 337 */     encrypt(this.writeBuffer_w, startPos, endPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectionListener(SimpleConnectionListener connectionListener) {
/* 363 */     this.connectionListener = connectionListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/* 372 */     if (this.playerServerConnection)
/*     */     {
/* 374 */       if (this.isLoggedIn) {
/*     */         
/* 376 */         if (this.lastRead < System.currentTimeMillis() - 300000L) {
/* 377 */           return false;
/*     */         }
/* 379 */       } else if (this.lastRead < System.currentTimeMillis() - 5000L) {
/* 380 */         return false;
/*     */       }  } 
/* 382 */     return this.connected;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogin(boolean li) {
/* 387 */     if (!this.isLoggedIn && li)
/*     */     {
/* 389 */       if (this.playerServerConnection) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 394 */         this.writeBuffer_w = ByteBuffer.allocate(786408);
/*     */         
/* 396 */         this.writeBuffer_r = ByteBuffer.allocate(786408);
/* 397 */         this.writeBuffer_w.clear();
/* 398 */         this.writeBuffer_r.flip();
/*     */       } 
/*     */     }
/* 401 */     this.isLoggedIn = li;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 407 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 409 */       logger.entering(CLASS_NAME, "disconnect");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 420 */     this.connected = false;
/*     */     
/*     */     try {
/* 423 */       if (this.in != null)
/* 424 */         this.in.close(); 
/* 425 */       this.in = null;
/*     */     }
/* 427 */     catch (Exception e) {
/*     */       
/* 429 */       e.printStackTrace();
/*     */     } 
/*     */     
/*     */     try {
/* 433 */       if (this.out != null)
/* 434 */         this.out.close(); 
/* 435 */       this.out = null;
/*     */     }
/* 437 */     catch (Exception e) {
/*     */       
/* 439 */       e.printStackTrace();
/*     */     } 
/*     */     
/*     */     try {
/* 443 */       if (this.socket != null)
/* 444 */         this.socket.close(); 
/* 445 */       this.socket = null;
/*     */     }
/* 447 */     catch (Exception e) {
/*     */       
/* 449 */       e.printStackTrace();
/*     */     } 
/* 451 */     this.readBuffer.clear();
/* 452 */     this.writeBuffer_w.clear();
/* 453 */     this.writeBuffer_r.clear();
/* 454 */     this.isLoggedIn = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendShutdown() {
/* 459 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 461 */       logger.entering(CLASS_NAME, "sendShutdown");
/*     */     }
/*     */     
/* 464 */     if (this.socketChannel != null) {
/*     */       
/*     */       try {
/*     */         
/* 468 */         this.socketChannel.socket().shutdownOutput();
/*     */       }
/* 470 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 475 */     if (this.socketChannel != null) {
/*     */       
/*     */       try {
/*     */         
/* 479 */         this.socketChannel.socket().shutdownInput();
/*     */       }
/* 481 */       catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeChannel() {
/* 490 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 492 */       logger.entering(CLASS_NAME, "closeChannel");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 504 */     if (this.socketChannel != null && this.socketChannel.socket() != null) {
/*     */       
/*     */       try {
/*     */         
/* 508 */         this.socketChannel.socket().close();
/*     */       }
/* 510 */       catch (IOException iox) {
/*     */         
/* 512 */         iox.printStackTrace();
/*     */       } 
/*     */     }
/* 515 */     if (this.socketChannel != null) {
/*     */       
/*     */       try {
/*     */         
/* 519 */         this.socketChannel.close();
/*     */       }
/* 521 */       catch (IOException iox) {
/*     */         
/* 523 */         iox.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/* 528 */   static long maxRead = 0L;
/*     */   
/* 530 */   static int maxTotalRead = 0;
/* 531 */   static int maxTotalReadAllowed = 20000;
/* 532 */   static int maxReadAllowed = 20000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() throws IOException {
/* 541 */     if (this.callTickWritingFromTick)
/*     */     {
/*     */ 
/*     */       
/* 545 */       tickWriting(0L);
/*     */     }
/*     */     
/* 548 */     if (this.ticksToDisconnect >= 0)
/*     */     {
/* 550 */       if (--this.ticksToDisconnect <= 0)
/*     */       {
/* 552 */         throw new IOException("Disconnecting by timeout.");
/*     */       }
/*     */     }
/*     */     
/* 556 */     int preRead = this.bytesRead;
/* 557 */     int totalRead = 0;
/*     */ 
/*     */     
/* 560 */     long maxNanosPerIteration = 3000000000L;
/* 561 */     long startTime = System.nanoTime();
/* 562 */     int readBlocks = 0;
/* 563 */     while (readBlocks < this.maxBlocksPerIteration && System.nanoTime() - startTime < 3000000000L && (
/* 564 */       totalRead = this.socketChannel.read(this.readBuffer)) > 0) {
/*     */       
/* 566 */       if (this.playerServerConnection) {
/*     */         
/* 568 */         if (totalRead > maxTotalRead)
/*     */         {
/* 570 */           maxTotalRead = totalRead;
/*     */         }
/* 572 */         if (totalRead > maxTotalReadAllowed)
/*     */         {
/* 574 */           throw new IOException(getIp() + " disconnected in SocketConnection. Maxtotalread not allowed: " + totalRead);
/*     */         }
/*     */       } 
/*     */       
/* 578 */       this.lastRead = System.currentTimeMillis();
/*     */       
/* 580 */       if (this.toRead < 0) {
/*     */         
/* 582 */         if (this.readBuffer.position() == 2) {
/*     */           
/* 584 */           this.bytesRead += this.readBuffer.position();
/* 585 */           this.readBuffer.flip();
/* 586 */           decrypt(this.readBuffer);
/* 587 */           this.toRead = this.readBuffer.getShort() & 0xFFFF;
/*     */           
/* 589 */           this.readBuffer.clear();
/* 590 */           this.readBuffer.limit(this.toRead);
/*     */           
/* 592 */           if (this.playerServerConnection) {
/*     */             
/* 594 */             if (this.toRead > maxRead)
/*     */             {
/* 596 */               maxRead = (this.toRead & 0xFFFF);
/*     */             }
/* 598 */             if (this.toRead > maxReadAllowed)
/*     */             {
/* 600 */               throw new IOException(getIp() + " disconnected in SocketConnection. Maxread not allowed: " + this.toRead);
/*     */             }
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 611 */       if (this.readBuffer.position() == this.toRead) {
/*     */         
/* 613 */         this.bytesRead += this.readBuffer.position();
/* 614 */         readBlocks++;
/*     */         
/* 616 */         this.readBuffer.flip();
/* 617 */         decrypt(this.readBuffer);
/* 618 */         this.connectionListener.reallyHandle(0, this.readBuffer);
/* 619 */         this.readBuffer.clear();
/* 620 */         this.readBuffer.limit(2);
/* 621 */         if (this.playerServerConnection)
/*     */         {
/* 623 */           if (this.toRead > maxReadAllowed)
/*     */           {
/* 625 */             throw new IOException(getIp() + " disconnected in SocketConnection. Maxread not allowed: " + this.toRead);
/*     */           }
/*     */         }
/*     */         
/* 629 */         this.toRead = -1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tickWriting(long aNanosToWaitForLock) throws IOException {
/*     */     try {
/* 645 */       if ((aNanosToWaitForLock <= 0L && RW_LOCK.writeLock().tryLock()) || (aNanosToWaitForLock > 0L && RW_LOCK
/* 646 */         .writeLock().tryLock(aNanosToWaitForLock, TimeUnit.NANOSECONDS))) {
/*     */         
/* 648 */         if (this.socketChannel == null || !this.socketChannel.isConnected()) {
/*     */           
/* 650 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 652 */             logger.fine("Cannot write message to socketChannel: " + this.socketChannel);
/*     */           }
/* 654 */           return false;
/*     */         } 
/*     */         
/*     */         try {
/* 658 */           if (this.writing)
/* 659 */             throw new IllegalStateException("update called between a getBuffer() and a flush(). Don't do that."); 
/* 660 */           if (getUnflushed() > 1048576)
/*     */           {
/* 662 */             throw new IOException("Buffer overflow (1 mb unsent)");
/*     */           }
/* 664 */           int preWrite = this.writeBuffer_r.remaining();
/* 665 */           this.socketChannel.write(this.writeBuffer_r);
/* 666 */           if (this.writeBuffer_r.remaining() == 0) {
/*     */             
/* 668 */             ByteBuffer tmp = this.writeBuffer_w;
/* 669 */             this.writeBuffer_w = this.writeBuffer_r;
/* 670 */             this.writeBuffer_r = tmp;
/* 671 */             this.writeBuffer_w.clear();
/* 672 */             this.writeBuffer_r.flip();
/*     */           } 
/* 674 */           if (logger.isLoggable(Level.FINER)) {
/*     */             
/* 676 */             int lBytesWritten = preWrite - this.writeBuffer_r.remaining();
/* 677 */             if (lBytesWritten > 0)
/*     */             {
/* 679 */               logger.finer("Number of bytes wriiten to the socketChannel: " + lBytesWritten + ", channel: " + this.socketChannel);
/*     */             }
/*     */           } 
/*     */           
/* 683 */           return true;
/*     */         }
/* 685 */         catch (IOException e) {
/*     */           
/* 687 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/*     */             
/* 690 */             logger.log(Level.FINE, "IOException while writing to channel: " + this.socketChannel + ", only " + this.writeBuffer_w
/* 691 */                 .remaining() + " bytes remained. Written=" + this.totalBytesWritten + ", BufferTmp: " + this.writeBufferTmp + ", Buffer_w: " + this.writeBuffer_w + ", Buffer_r: " + this.writeBuffer_r, e);
/*     */           }
/*     */           
/* 694 */           throw e;
/*     */         }
/*     */         finally {
/*     */           
/* 698 */           RW_LOCK.writeLock().unlock();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 703 */       if (logger.isLoggable(Level.FINEST));
/*     */ 
/*     */ 
/*     */       
/* 707 */       return false;
/*     */     
/*     */     }
/* 710 */     catch (InterruptedException e) {
/*     */       
/* 712 */       logger.log(Level.WARNING, "Lock was interrupted", e);
/* 713 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeProtocol(long newSeed) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void encrypt(ByteBuffer bb, int start, int end) {
/* 740 */     byte[] bytes = bb.array();
/*     */     
/* 742 */     for (int i = start; i < end; i++) {
/*     */       
/* 744 */       if (--this.remainingEncryptBytes < 0) {
/*     */         
/* 746 */         this.remainingEncryptBytes = this.encryptRandom.nextInt(100) + 1;
/* 747 */         this.encryptByte = (byte)this.encryptRandom.nextInt(254);
/* 748 */         this.encryptAddByte = (byte)this.encryptRandom.nextInt(254);
/*     */       } 
/* 750 */       bytes[i] = (byte)(bytes[i] - this.encryptAddByte);
/* 751 */       bytes[i] = (byte)(bytes[i] ^ this.encryptByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decrypt(ByteBuffer bb) {
/* 764 */     byte[] bytes = bb.array();
/* 765 */     int start = bb.position();
/* 766 */     int end = bb.limit();
/*     */     
/* 768 */     for (int i = start; i < end; i++) {
/*     */       
/* 770 */       if (--this.remainingDencryptBytes < 0) {
/*     */         
/* 772 */         this.remainingDencryptBytes = this.decryptRandom.nextInt(100) + 1;
/* 773 */         this.dencryptByte = (byte)this.decryptRandom.nextInt(254);
/* 774 */         this.decryptAddByte = (byte)this.decryptRandom.nextInt(254);
/*     */       } 
/* 776 */       bytes[i] = (byte)(bytes[i] ^ this.dencryptByte);
/* 777 */       bytes[i] = (byte)(bytes[i] + this.decryptAddByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryptSeed(long seed) {
/* 790 */     this.encryptRandom.setSeed(seed);
/* 791 */     this.remainingEncryptBytes = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDecryptSeed(long seed) {
/* 796 */     this.decryptRandom.setSeed(seed);
/* 797 */     this.remainingDencryptBytes = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSentBytes() {
/* 802 */     return this.totalBytesWritten;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReadBytes() {
/* 807 */     return this.bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearSentBytes() {
/* 812 */     this.totalBytesWritten = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearReadBytes() {
/* 817 */     this.bytesRead = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCallTickWritingFromTick() {
/* 831 */     return this.callTickWritingFromTick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCallTickWritingFromTick(boolean newCallTickWritingFromTick) {
/* 842 */     this.callTickWritingFromTick = newCallTickWritingFromTick;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWriting() {
/* 847 */     return this.writing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 856 */     return "SocketConnection [IrcChannel: " + this.socketChannel + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\communication\SocketConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */