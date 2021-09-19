/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLStreamHandler;
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
/*    */ public final class Handler
/*    */   extends URLStreamHandler
/*    */ {
/*    */   private static final String JAVA_PROTOCOL_HANDLER_PACKAGES = "java.protocol.handler.pkgs";
/*    */   
/*    */   static void register() {
/* 50 */     register((Class)Handler.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void register(Class<? extends URLStreamHandler> handlerClass) {
/* 57 */     Preconditions.checkArgument("Handler".equals(handlerClass.getSimpleName()));
/*    */     
/* 59 */     String pkg = handlerClass.getPackage().getName();
/* 60 */     int lastDot = pkg.lastIndexOf('.');
/* 61 */     Preconditions.checkArgument((lastDot > 0), "package for Handler (%s) must have a parent package", new Object[] { pkg });
/*    */     
/* 63 */     String parentPackage = pkg.substring(0, lastDot);
/*    */     
/* 65 */     String packages = System.getProperty("java.protocol.handler.pkgs");
/* 66 */     if (packages == null) {
/* 67 */       packages = parentPackage;
/*    */     } else {
/* 69 */       packages = packages + "|" + parentPackage;
/*    */     } 
/* 71 */     System.setProperty("java.protocol.handler.pkgs", packages);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected URLConnection openConnection(URL url) throws IOException {
/* 82 */     return new PathURLConnection(url);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\Handler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */