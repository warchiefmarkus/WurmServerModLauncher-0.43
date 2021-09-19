/*    */ package org.fourthline.cling.transport.impl;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import org.fourthline.cling.transport.spi.MulticastReceiverConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MulticastReceiverConfigurationImpl
/*    */   implements MulticastReceiverConfiguration
/*    */ {
/*    */   private InetAddress group;
/*    */   private int port;
/*    */   private int maxDatagramBytes;
/*    */   
/*    */   public MulticastReceiverConfigurationImpl(InetAddress group, int port, int maxDatagramBytes) {
/* 35 */     this.group = group;
/* 36 */     this.port = port;
/* 37 */     this.maxDatagramBytes = maxDatagramBytes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MulticastReceiverConfigurationImpl(InetAddress group, int port) {
/* 44 */     this(group, port, 640);
/*    */   }
/*    */   
/*    */   public MulticastReceiverConfigurationImpl(String group, int port, int maxDatagramBytes) throws UnknownHostException {
/* 48 */     this(InetAddress.getByName(group), port, maxDatagramBytes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MulticastReceiverConfigurationImpl(String group, int port) throws UnknownHostException {
/* 55 */     this(InetAddress.getByName(group), port, 640);
/*    */   }
/*    */   
/*    */   public InetAddress getGroup() {
/* 59 */     return this.group;
/*    */   }
/*    */   
/*    */   public void setGroup(InetAddress group) {
/* 63 */     this.group = group;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 67 */     return this.port;
/*    */   }
/*    */   
/*    */   public void setPort(int port) {
/* 71 */     this.port = port;
/*    */   }
/*    */   
/*    */   public int getMaxDatagramBytes() {
/* 75 */     return this.maxDatagramBytes;
/*    */   }
/*    */   
/*    */   public void setMaxDatagramBytes(int maxDatagramBytes) {
/* 79 */     this.maxDatagramBytes = maxDatagramBytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\MulticastReceiverConfigurationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */