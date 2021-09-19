/*    */ package com.sun.mail.util;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.Socket;
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
/*    */ class SocksSupport
/*    */ {
/*    */   public static Socket getSocket(String host, int port) {
/* 57 */     if (host == null || host.length() == 0) {
/* 58 */       return new Socket(Proxy.NO_PROXY);
/*    */     }
/* 60 */     return new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port)));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\mai\\util\SocksSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */