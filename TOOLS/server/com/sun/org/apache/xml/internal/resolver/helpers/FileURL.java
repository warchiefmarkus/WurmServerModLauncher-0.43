/*    */ package com.sun.org.apache.xml.internal.resolver.helpers;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
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
/*    */ public abstract class FileURL
/*    */ {
/*    */   public static URL makeURL(String pathname) throws MalformedURLException {
/* 72 */     if (pathname.startsWith("/")) {
/* 73 */       return new URL("file://" + pathname);
/*    */     }
/*    */     
/* 76 */     String userdir = System.getProperty("user.dir");
/* 77 */     userdir.replace('\\', '/');
/*    */     
/* 79 */     if (userdir.endsWith("/")) {
/* 80 */       return new URL("file:///" + userdir + pathname);
/*    */     }
/* 82 */     return new URL("file:///" + userdir + "/" + pathname);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\helpers\FileURL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */