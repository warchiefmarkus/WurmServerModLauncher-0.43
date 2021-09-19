/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.spi.DatagramProcessor;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.fourthline.cling.transport.spi.MulticastReceiver;
/*     */ import org.fourthline.cling.transport.spi.MulticastReceiverConfiguration;
/*     */ import org.fourthline.cling.transport.spi.NetworkAddressFactory;
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
/*     */ public class MulticastReceiverImpl
/*     */   implements MulticastReceiver<MulticastReceiverConfigurationImpl>
/*     */ {
/*  44 */   private static Logger log = Logger.getLogger(MulticastReceiver.class.getName());
/*     */   
/*     */   protected final MulticastReceiverConfigurationImpl configuration;
/*     */   
/*     */   protected Router router;
/*     */   
/*     */   protected NetworkAddressFactory networkAddressFactory;
/*     */   protected DatagramProcessor datagramProcessor;
/*     */   protected NetworkInterface multicastInterface;
/*     */   protected InetSocketAddress multicastAddress;
/*     */   protected MulticastSocket socket;
/*     */   
/*     */   public MulticastReceiverImpl(MulticastReceiverConfigurationImpl configuration) {
/*  57 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */   public MulticastReceiverConfigurationImpl getConfiguration() {
/*  61 */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void init(NetworkInterface networkInterface, Router router, NetworkAddressFactory networkAddressFactory, DatagramProcessor datagramProcessor) throws InitializationException {
/*  69 */     this.router = router;
/*  70 */     this.networkAddressFactory = networkAddressFactory;
/*  71 */     this.datagramProcessor = datagramProcessor;
/*  72 */     this.multicastInterface = networkInterface;
/*     */ 
/*     */     
/*     */     try {
/*  76 */       log.info("Creating wildcard socket (for receiving multicast datagrams) on port: " + this.configuration.getPort());
/*  77 */       this.multicastAddress = new InetSocketAddress(this.configuration.getGroup(), this.configuration.getPort());
/*     */       
/*  79 */       this.socket = new MulticastSocket(this.configuration.getPort());
/*  80 */       this.socket.setReuseAddress(true);
/*  81 */       this.socket.setReceiveBufferSize(32768);
/*     */       
/*  83 */       log.info("Joining multicast group: " + this.multicastAddress + " on network interface: " + this.multicastInterface.getDisplayName());
/*  84 */       this.socket.joinGroup(this.multicastAddress, this.multicastInterface);
/*     */     }
/*  86 */     catch (Exception ex) {
/*  87 */       throw new InitializationException("Could not initialize " + getClass().getSimpleName() + ": " + ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void stop() {
/*  92 */     if (this.socket != null && !this.socket.isClosed()) {
/*     */       try {
/*  94 */         log.fine("Leaving multicast group");
/*  95 */         this.socket.leaveGroup(this.multicastAddress, this.multicastInterface);
/*     */       }
/*  97 */       catch (Exception ex) {
/*  98 */         log.fine("Could not leave multicast group: " + ex);
/*     */       } 
/*     */       
/* 101 */       this.socket.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 107 */     log.fine("Entering blocking receiving loop, listening for UDP datagrams on: " + this.socket.getLocalAddress());
/*     */     
/*     */     while (true) {
/*     */       try {
/* 111 */         byte[] buf = new byte[getConfiguration().getMaxDatagramBytes()];
/* 112 */         DatagramPacket datagram = new DatagramPacket(buf, buf.length);
/*     */         
/* 114 */         this.socket.receive(datagram);
/*     */ 
/*     */         
/* 117 */         InetAddress receivedOnLocalAddress = this.networkAddressFactory.getLocalAddress(this.multicastInterface, this.multicastAddress
/*     */             
/* 119 */             .getAddress() instanceof java.net.Inet6Address, datagram
/* 120 */             .getAddress());
/*     */ 
/*     */         
/* 123 */         log.fine("UDP datagram received from: " + datagram
/* 124 */             .getAddress().getHostAddress() + ":" + datagram
/* 125 */             .getPort() + " on local interface: " + this.multicastInterface
/* 126 */             .getDisplayName() + " and address: " + receivedOnLocalAddress
/* 127 */             .getHostAddress());
/*     */ 
/*     */         
/* 130 */         this.router.received(this.datagramProcessor.read(receivedOnLocalAddress, datagram));
/*     */       }
/* 132 */       catch (SocketException ex) {
/* 133 */         log.fine("Socket closed");
/*     */         break;
/* 135 */       } catch (UnsupportedDataException ex) {
/* 136 */         log.info("Could not read datagram: " + ex.getMessage());
/* 137 */       } catch (Exception ex) {
/* 138 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/*     */     try {
/* 142 */       if (!this.socket.isClosed()) {
/* 143 */         log.fine("Closing multicast socket");
/* 144 */         this.socket.close();
/*     */       } 
/* 146 */     } catch (Exception ex) {
/* 147 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\MulticastReceiverImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */