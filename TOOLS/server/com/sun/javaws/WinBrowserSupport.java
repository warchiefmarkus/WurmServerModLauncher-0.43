/*    */ package com.sun.javaws;
/*    */ 
/*    */ import com.sun.deploy.config.Config;
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
/*    */ public class WinBrowserSupport
/*    */   extends BrowserSupport
/*    */ {
/*    */   public String getNS6MailCapInfo() {
/* 18 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OperaSupport getOperaSupport() {
/* 28 */     return new WinOperaSupport(Config.getBooleanProperty("deployment.mime.types.use.default"));
/*    */   }
/*    */   public boolean isWebBrowserSupportedImpl() {
/* 31 */     return true;
/*    */   } public boolean showDocumentImpl(URL paramURL) {
/* 33 */     if (paramURL == null) return false; 
/* 34 */     return showDocument(paramURL.toString());
/*    */   }
/*    */   
/*    */   public String getDefaultHandler(URL paramURL) {
/* 38 */     return Config.getInstance().getBrowserPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean showDocument(String paramString) {
/* 43 */     return Config.getInstance().showDocument(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\WinBrowserSupport.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */