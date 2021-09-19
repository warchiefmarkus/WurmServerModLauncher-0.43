/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.SocketException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.spi.DatagramIO;
/*     */ import org.fourthline.cling.transport.spi.DatagramIOConfiguration;
/*     */ import org.fourthline.cling.transport.spi.DatagramProcessor;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatagramIOImpl
/*     */   implements DatagramIO<DatagramIOConfigurationImpl>
/*     */ {
/*  47 */   private static Logger log = Logger.getLogger(DatagramIO.class.getName());
/*     */ 
/*     */   
/*     */   protected final DatagramIOConfigurationImpl configuration;
/*     */ 
/*     */   
/*     */   protected Router router;
/*     */ 
/*     */   
/*     */   protected DatagramProcessor datagramProcessor;
/*     */ 
/*     */   
/*     */   protected InetSocketAddress localAddress;
/*     */ 
/*     */   
/*     */   protected MulticastSocket socket;
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramIOImpl(DatagramIOConfigurationImpl configuration) {
/*  67 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */   public DatagramIOConfigurationImpl getConfiguration() {
/*  71 */     return this.configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void init(InetAddress bindAddress, Router router, DatagramProcessor datagramProcessor) throws InitializationException {
/*  76 */     this.router = router;
/*  77 */     this.datagramProcessor = datagramProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  84 */       log.info("Creating bound socket (for datagram input/output) on: " + bindAddress);
/*  85 */       this.localAddress = new InetSocketAddress(bindAddress, 0);
/*  86 */       this.socket = new MulticastSocket(this.localAddress);
/*  87 */       this.socket.setTimeToLive(this.configuration.getTimeToLive());
/*  88 */       this.socket.setReceiveBufferSize(262144);
/*  89 */     } catch (Exception ex) {
/*  90 */       throw new InitializationException("Could not initialize " + getClass().getSimpleName() + ": " + ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void stop() {
/*  95 */     if (this.socket != null && !this.socket.isClosed()) {
/*  96 */       this.socket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/* 101 */     log.fine("Entering blocking receiving loop, listening for UDP datagrams on: " + this.socket.getLocalAddress());
/*     */ 
/*     */     
/*     */     while (true) {
/*     */       try {
/* 106 */         byte[] buf = new byte[getConfiguration().getMaxDatagramBytes()];
/* 107 */         DatagramPacket datagram = new DatagramPacket(buf, buf.length);
/*     */         
/* 109 */         this.socket.receive(datagram);
/*     */         
/* 111 */         log.fine("UDP datagram received from: " + datagram
/*     */             
/* 113 */             .getAddress().getHostAddress() + ":" + datagram
/* 114 */             .getPort() + " on: " + this.localAddress);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 119 */         this.router.received(this.datagramProcessor.read(this.localAddress.getAddress(), datagram));
/*     */       }
/* 121 */       catch (SocketException ex) {
/* 122 */         log.fine("Socket closed");
/*     */         break;
/* 124 */       } catch (UnsupportedDataException ex) {
/* 125 */         log.info("Could not read datagram: " + ex.getMessage());
/* 126 */       } catch (Exception ex) {
/* 127 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/*     */     try {
/* 131 */       if (!this.socket.isClosed()) {
/* 132 */         log.fine("Closing unicast socket");
/* 133 */         this.socket.close();
/*     */       } 
/* 135 */     } catch (Exception ex) {
/* 136 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void send(OutgoingDatagramMessage message) {
/* 141 */     if (log.isLoggable(Level.FINE)) {
/* 142 */       log.fine("Sending message from address: " + this.localAddress);
/*     */     }
/* 144 */     DatagramPacket packet = this.datagramProcessor.write(message);
/*     */     
/* 146 */     if (log.isLoggable(Level.FINE)) {
/* 147 */       log.fine("Sending UDP datagram packet to: " + message.getDestinationAddress() + ":" + message.getDestinationPort());
/*     */     }
/*     */     
/* 150 */     send(packet);
/*     */   }
/*     */   
/*     */   public synchronized void send(DatagramPacket datagram) {
/* 154 */     if (log.isLoggable(Level.FINE)) {
/* 155 */       log.fine("Sending message from address: " + this.localAddress);
/*     */     }
/*     */     
/*     */     try {
/* 159 */       this.socket.send(datagram);
/* 160 */     } catch (SocketException ex) {
/* 161 */       log.fine("Socket closed, aborting datagram send to: " + datagram.getAddress());
/* 162 */     } catch (RuntimeException ex) {
/* 163 */       throw ex;
/* 164 */     } catch (Exception ex) {
/* 165 */       log.log(Level.SEVERE, "Exception sending datagram to: " + datagram.getAddress() + ": " + ex, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\DatagramIOImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */