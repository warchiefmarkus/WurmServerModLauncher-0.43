/*    */ package org.fourthline.cling.transport.impl;
/*    */ 
/*    */ import org.fourthline.cling.transport.spi.StreamServerConfiguration;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StreamServerConfigurationImpl
/*    */   implements StreamServerConfiguration
/*    */ {
/*    */   private int listenPort;
/*    */   private int tcpConnectionBacklog;
/*    */   
/*    */   public StreamServerConfigurationImpl() {}
/*    */   
/*    */   public StreamServerConfigurationImpl(int listenPort) {
/* 37 */     this.listenPort = listenPort;
/*    */   }
/*    */   
/*    */   public int getListenPort() {
/* 41 */     return this.listenPort;
/*    */   }
/*    */   
/*    */   public void setListenPort(int listenPort) {
/* 45 */     this.listenPort = listenPort;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTcpConnectionBacklog() {
/* 53 */     return this.tcpConnectionBacklog;
/*    */   }
/*    */   
/*    */   public void setTcpConnectionBacklog(int tcpConnectionBacklog) {
/* 57 */     this.tcpConnectionBacklog = tcpConnectionBacklog;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\StreamServerConfigurationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */