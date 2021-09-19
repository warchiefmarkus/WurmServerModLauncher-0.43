/*    */ package com.wurmonline.server.webinterface;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
/*    */ import java.net.InetAddress;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import java.rmi.server.RMISocketFactory;
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
/*    */ final class AnchorSocketFactory
/*    */   extends RMISocketFactory
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 720394327635467676L;
/*    */   private final InetAddress ipInterface;
/*    */   
/*    */   AnchorSocketFactory(InetAddress aIpInterface) {
/* 39 */     this.ipInterface = aIpInterface;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServerSocket createServerSocket(int port) {
/* 50 */     ServerSocket serverSocket = null;
/*    */     
/*    */     try {
/* 53 */       serverSocket = new ServerSocket(port, 50, this.ipInterface);
/*    */     }
/* 55 */     catch (Exception e) {
/*    */       
/* 57 */       System.out.println(e);
/*    */     } 
/* 59 */     return serverSocket;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket createSocket(String dummy, int port) throws IOException {
/* 70 */     return new Socket(this.ipInterface, port);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object that) {
/* 81 */     return (that != null && that.getClass() == getClass());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\AnchorSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */