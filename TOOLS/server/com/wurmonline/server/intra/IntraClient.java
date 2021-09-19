/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.communication.SimpleConnectionListener;
/*     */ import com.wurmonline.communication.SocketConnection;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntraClient
/*     */   implements SimpleConnectionListener, MiscConstants
/*     */ {
/*     */   private SocketConnection connection;
/*     */   private IntraServerConnectionListener serverConnectionListener;
/*     */   private boolean disconnected = true;
/*     */   public boolean loggedIn = false;
/*  40 */   private String disconnectReason = "Lost link.";
/*  41 */   int retryInSeconds = 0;
/*  42 */   long timeDifference = 0L;
/*     */   
/*     */   private static final int DATABUFSIZE = 16384;
/*     */   private static final int TRANSFERSIZE = 16366;
/*  46 */   private static Logger logger = Logger.getLogger(IntraClient.class.getName());
/*     */ 
/*     */   
/*     */   public boolean isConnecting = false;
/*     */ 
/*     */   
/*     */   public boolean hasFailedConnection = false;
/*     */ 
/*     */   
/*     */   protected static final String CHARSET_ENCODING_FOR_COMMS = "UTF-8";
/*     */ 
/*     */ 
/*     */   
/*     */   public IntraClient(String serverIp, int serverPort, IntraServerConnectionListener aServerConnectionListener) throws IOException {
/*  60 */     reconnect(serverIp, serverPort, aServerConnectionListener);
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
/*     */   private void reconnect(String serverIp, int serverPort, IntraServerConnectionListener aServerConnectionListener) throws IOException {
/*  82 */     this.serverConnectionListener = aServerConnectionListener;
/*  83 */     this.connection = new SocketConnection(serverIp, serverPort, 20000);
/*     */     
/*  85 */     this.connection.setMaxBlocksPerIteration(1000000);
/*  86 */     this.connection.setConnectionListener(this);
/*  87 */     this.disconnected = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reconnectAsynch(final String serverIp, final int serverPort, IntraServerConnectionListener aServerConnectionListener) {
/*  93 */     this.isConnecting = true;
/*  94 */     this.serverConnectionListener = aServerConnectionListener;
/*  95 */     final IntraClient c = this;
/*  96 */     (new Thread()
/*     */       {
/*     */ 
/*     */         
/*     */         public void run()
/*     */         {
/*     */           try {
/* 103 */             IntraClient.this.connection = new SocketConnection(serverIp, serverPort, 20000);
/*     */             
/* 105 */             IntraClient.this.connection.setMaxBlocksPerIteration(1000000);
/* 106 */             IntraClient.this.connection.setConnectionListener(c);
/* 107 */             IntraClient.this.disconnected = false;
/* 108 */             IntraClient.this.isConnecting = false;
/*     */           }
/* 110 */           catch (IOException iox) {
/*     */             
/* 112 */             IntraClient.this.hasFailedConnection = true;
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 117 */       }).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void login(String password, boolean dev) {
/* 122 */     if (this.retryInSeconds <= 0) {
/*     */ 
/*     */       
/*     */       try {
/* 126 */         byte[] passwordBytes = password.getBytes("UTF-8");
/* 127 */         ByteBuffer buf = this.connection.getBuffer();
/* 128 */         buf.put((byte)1);
/* 129 */         buf.putInt(1);
/* 130 */         buf.put((byte)passwordBytes.length);
/* 131 */         buf.put(passwordBytes);
/* 132 */         buf.put((byte)(dev ? 1 : 0));
/* 133 */         this.connection.flush();
/* 134 */         this.retryInSeconds = 0;
/* 135 */         if (logger.isLoggable(Level.FINE))
/*     */         {
/* 137 */           logger.fine("Client sent login");
/*     */         }
/*     */       }
/* 140 */       catch (IOException e) {
/*     */         
/* 142 */         logger.log(Level.WARNING, "Failed to login", e);
/* 143 */         this.serverConnectionListener.commandFailed(this);
/*     */       } 
/*     */     } else {
/*     */       
/* 147 */       this.retryInSeconds--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void update() throws IOException {
/* 152 */     if (this.disconnected) {
/* 153 */       throw new IOException(this.disconnectReason);
/*     */     }
/*     */     try {
/* 156 */       this.connection.tick();
/*     */     }
/* 158 */     catch (Exception e) {
/*     */       
/* 160 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 162 */         logger.log(Level.FINE, "Failed to update on connection: " + this.connection, e);
/*     */       }
/* 164 */       this.serverConnectionListener.commandFailed(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void disconnect(String reason) {
/* 170 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/*     */       
/* 173 */       if (reason != null && reason.equals("Done"))
/*     */       {
/* 175 */         logger.log(Level.FINE, "Disconnecting connection: " + this.connection + ", reason: " + reason);
/*     */       }
/*     */     }
/* 178 */     if (this.connection != null && this.connection.isConnected()) {
/*     */ 
/*     */       
/*     */       try {
/* 182 */         sendDisconnect();
/*     */       }
/* 184 */       catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 190 */         this.connection.sendShutdown();
/* 191 */         this.connection.disconnect();
/* 192 */         this.connection.closeChannel();
/*     */       }
/* 194 */       catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 199 */     this.disconnectReason = reason;
/* 200 */     this.disconnected = true;
/* 201 */     this.loggedIn = false;
/* 202 */     if (this.serverConnectionListener != null)
/*     */     {
/* 204 */       this.serverConnectionListener.remove(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reallyHandle(int num, ByteBuffer bb) {
/*     */     try {
/* 216 */       byte cmd = bb.get();
/* 217 */       if (logger.isLoggable(Level.FINER))
/*     */       {
/* 219 */         logger.finer("Received cmd " + cmd);
/*     */       }
/* 221 */       if (cmd == 2) {
/*     */         
/* 223 */         this.loggedIn = (bb.get() != 0);
/* 224 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/* 226 */           logger.finest("This client is loggedin=" + this.loggedIn);
/*     */         }
/* 228 */         byte[] bytes = new byte[bb.getShort() & 0xFFFF];
/* 229 */         bb.get(bytes);
/* 230 */         String message = new String(bytes, "UTF-8");
/* 231 */         this.retryInSeconds = bb.getShort() & 0xFFFF;
/* 232 */         long targetNow = bb.getLong();
/* 233 */         this.timeDifference = targetNow - System.currentTimeMillis();
/*     */         
/* 235 */         if (!this.loggedIn)
/*     */         {
/* 237 */           logger.log(Level.WARNING, "Login Failed: " + message);
/* 238 */           this.serverConnectionListener.commandFailed(this);
/*     */         }
/* 240 */         else if (logger.isLoggable(Level.FINER))
/*     */         {
/* 242 */           logger.finer("Client logged in - message: " + message + ", " + this);
/*     */         }
/*     */       
/* 245 */       } else if (cmd == 6) {
/*     */         
/* 247 */         boolean ok = (bb.get() != 0);
/* 248 */         byte[] bytes = new byte[bb.getShort() & 0xFFFF];
/* 249 */         bb.get(bytes);
/*     */ 
/*     */         
/* 252 */         String sessionKey = new String(bytes, "UTF-8");
/* 253 */         this.retryInSeconds = bb.getShort() & 0xFFFF;
/* 254 */         long targetNow = bb.getLong();
/* 255 */         this.timeDifference = targetNow - System.currentTimeMillis();
/* 256 */         if (this.retryInSeconds > 0)
/*     */         {
/* 258 */           this.serverConnectionListener.commandFailed(this);
/*     */         }
/* 260 */         else if (!ok)
/*     */         {
/* 262 */           this.serverConnectionListener.commandFailed(this);
/*     */         }
/* 264 */         else if (logger.isLoggable(Level.FINE))
/*     */         {
/* 266 */           logger.fine("Client received transferrequest ok - " + this);
/*     */         }
/*     */       
/* 269 */       } else if (cmd == 10) {
/*     */         
/* 271 */         long oldTime = WurmCalendar.currentTime;
/* 272 */         long wurmTime = bb.getLong();
/* 273 */         WurmCalendar.currentTime = wurmTime;
/* 274 */         logger.log(Level.INFO, "The server just synched wurm clock. New wurm time=" + wurmTime + ". Difference was " + (oldTime - wurmTime) + " wurm seconds.");
/*     */         
/* 276 */         this.serverConnectionListener.commandExecuted(this);
/*     */       }
/* 278 */       else if (cmd == 4) {
/*     */         
/* 280 */         this.serverConnectionListener.commandExecuted(this);
/*     */       }
/* 282 */       else if (cmd == 5) {
/*     */         
/* 284 */         this.serverConnectionListener.commandFailed(this);
/*     */       }
/* 286 */       else if (cmd == 8) {
/*     */         
/* 288 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/* 290 */           logger.finest("Client received data received.");
/*     */         }
/* 292 */         this.serverConnectionListener.dataReceived(this);
/*     */       }
/* 294 */       else if (cmd == 9) {
/*     */         
/* 296 */         this.serverConnectionListener.receivingData(bb);
/*     */       }
/* 298 */       else if (cmd == 11) {
/*     */         
/* 300 */         this.serverConnectionListener.receivingData(bb);
/*     */       }
/* 302 */       else if (cmd == 14) {
/*     */         
/* 304 */         this.serverConnectionListener.reschedule(this);
/*     */       }
/* 306 */       else if (cmd == 13) {
/*     */         
/* 308 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/* 310 */           logger.finest("IntraClient received PONG - " + this);
/*     */         }
/* 312 */         this.serverConnectionListener.receivingData(bb);
/*     */       }
/*     */       else {
/*     */         
/* 316 */         logger.warning("Ignoring unknown cmd " + cmd);
/* 317 */         System.out.println("Ignoring unknown cmd " + cmd);
/*     */       }
/*     */     
/* 320 */     } catch (Exception e) {
/*     */       
/* 322 */       logger.log(Level.WARNING, "Problem handling Block: " + bb, e);
/* 323 */       e.printStackTrace();
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
/*     */   int sendNextDataPart(byte[] data, int index) throws IOException {
/* 337 */     ByteBuffer buf = this.connection.getBuffer();
/* 338 */     int length = Math.min(data.length - index, 16366);
/*     */     
/* 340 */     int nextindex = index + length;
/* 341 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 343 */       logger.finest("Sending " + length + " out of " + data.length + " up to " + nextindex + " max size is " + '䀀');
/*     */     }
/*     */     
/* 346 */     buf.put((byte)7);
/* 347 */     buf.putInt(length);
/* 348 */     buf.put(data, index, length);
/* 349 */     buf.put((byte)((nextindex == data.length) ? 1 : 0));
/* 350 */     this.connection.flush();
/* 351 */     return nextindex;
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
/*     */   void executePlayerTransferRequest(int posx, int posy, boolean surfaced) throws IOException {
/* 364 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 366 */       logger.finest("Requesting player transfer for coordinates: " + posx + ", " + posy + ", surfaced: " + surfaced + " - " + this);
/*     */     }
/*     */     
/* 369 */     ByteBuffer buf = this.connection.getBuffer();
/* 370 */     buf.put((byte)3);
/* 371 */     buf.putInt(posx);
/* 372 */     buf.putInt(posy);
/* 373 */     buf.put((byte)(surfaced ? 1 : 0));
/* 374 */     this.connection.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void executeSyncCommand() throws IOException {
/* 384 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 386 */       logger.finest("Synchronising the time - " + this);
/*     */     }
/* 388 */     ByteBuffer buf = this.connection.getBuffer();
/* 389 */     buf.put((byte)10);
/*     */     
/* 391 */     this.connection.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void executeRequestPlayerVersion(long playerId) throws IOException {
/* 402 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 404 */       logger.finest("Requesting player version for player id: " + playerId + " - " + this);
/*     */     }
/* 406 */     ByteBuffer buf = this.connection.getBuffer();
/* 407 */     buf.put((byte)9);
/* 408 */     buf.putLong(playerId);
/* 409 */     this.connection.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void executeRequestPlayerPaymentExpire(long playerId) throws IOException {
/* 420 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 422 */       logger.finest("Requesting player payment expire for player id: " + playerId + " - " + this);
/*     */     }
/* 424 */     ByteBuffer buf = this.connection.getBuffer();
/* 425 */     buf.put((byte)11);
/* 426 */     buf.putLong(playerId);
/* 427 */     this.connection.flush();
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
/*     */   void executeMoneyUpdate(long playerId, long currentMoney, long moneyAdded, String detail) throws IOException {
/* 447 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 449 */       logger.finest("Updating money update for player id: " + playerId + " - " + this);
/*     */     }
/* 451 */     ByteBuffer buf = this.connection.getBuffer();
/* 452 */     buf.put((byte)16);
/* 453 */     buf.putLong(playerId);
/* 454 */     buf.putLong(currentMoney);
/* 455 */     buf.putLong(moneyAdded);
/* 456 */     byte[] det = detail.getBytes("UTF-8");
/* 457 */     buf.putInt(det.length);
/* 458 */     buf.put(det);
/* 459 */     this.connection.flush();
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
/*     */   void executeExpireUpdate(long playerId, long currentExpire, String detail, int days, int months, boolean dealItems) throws IOException {
/* 475 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 477 */       logger.finest("Updating expire update for player id: " + playerId + " - " + this);
/*     */     }
/* 479 */     ByteBuffer buf = this.connection.getBuffer();
/* 480 */     buf.put((byte)17);
/* 481 */     buf.putLong(playerId);
/* 482 */     buf.putLong(currentExpire);
/* 483 */     buf.putInt(days);
/* 484 */     buf.putInt(months);
/* 485 */     buf.put((byte)(dealItems ? 1 : 0));
/* 486 */     byte[] det = detail.getBytes("UTF-8");
/* 487 */     buf.putInt(det.length);
/* 488 */     buf.put(det);
/* 489 */     this.connection.flush();
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
/*     */   void executePasswordUpdate(long playerId, String currentHashedPassword, long timestamp) throws IOException {
/* 503 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 505 */       logger.finest("Updating password for player id: " + playerId + " at timestamp " + timestamp + " - " + this);
/*     */     }
/* 507 */     ByteBuffer buf = this.connection.getBuffer();
/* 508 */     buf.put((byte)18);
/* 509 */     buf.putLong(playerId);
/* 510 */     byte[] pw = currentHashedPassword.getBytes("UTF-8");
/* 511 */     buf.putInt(pw.length);
/* 512 */     buf.put(pw);
/* 513 */     buf.putLong(timestamp);
/* 514 */     this.connection.flush();
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
/*     */   public void executePingCommand() throws IOException {
/* 528 */     ByteBuffer buf = this.connection.getBuffer();
/* 529 */     buf.put((byte)13);
/* 530 */     this.connection.flush();
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
/*     */   static final void sendShortStringLength(String toSend, ByteBuffer bb) throws Exception {
/* 542 */     byte[] toSendStringArr = toSend.getBytes("UTF-8");
/* 543 */     bb.putShort((short)toSendStringArr.length);
/* 544 */     bb.put(toSendStringArr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendDisconnect() throws IOException {
/* 554 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 556 */       logger.finest("Client sending disconnect - " + this);
/*     */     }
/* 558 */     ByteBuffer buf = this.connection.getBuffer();
/* 559 */     buf.put((byte)15);
/* 560 */     this.connection.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 571 */     return "IntraClient [SocketConnection: " + this.connection + ", disconnected: " + this.disconnected + ", loggedIn: " + this.loggedIn + ", disconnectReason: " + this.disconnectReason + ']';
/*     */   }
/*     */   
/*     */   public IntraClient() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\IntraClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */