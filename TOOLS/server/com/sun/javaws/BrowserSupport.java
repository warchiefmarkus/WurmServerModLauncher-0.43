/*    */ package com.sun.javaws;
/*    */ 
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
/*    */ public abstract class BrowserSupport
/*    */ {
/* 20 */   private static BrowserSupport _browserSupportImplementation = null;
/*    */   
/*    */   public static synchronized BrowserSupport getInstance() {
/* 23 */     if (_browserSupportImplementation == null)
/*    */     {
/* 25 */       _browserSupportImplementation = BrowserSupportFactory.newInstance();
/*    */     }
/* 27 */     return _browserSupportImplementation;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isWebBrowserSupported() {
/* 32 */     return getInstance().isWebBrowserSupportedImpl();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean showDocument(URL paramURL) {
/* 40 */     return getInstance().showDocumentImpl(paramURL);
/*    */   }
/*    */   
/*    */   public abstract boolean isWebBrowserSupportedImpl();
/*    */   
/*    */   public abstract boolean showDocumentImpl(URL paramURL);
/*    */   
/*    */   public abstract String getNS6MailCapInfo();
/*    */   
/*    */   public abstract OperaSupport getOperaSupport();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\BrowserSupport.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */