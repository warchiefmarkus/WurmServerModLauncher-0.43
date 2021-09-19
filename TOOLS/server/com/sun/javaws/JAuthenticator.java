/*    */ package com.sun.javaws;
/*    */ 
/*    */ import com.sun.deploy.config.Config;
/*    */ import com.sun.deploy.security.DeployAuthenticator;
/*    */ import java.awt.Frame;
/*    */ import java.net.PasswordAuthentication;
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
/*    */ public class JAuthenticator
/*    */   extends DeployAuthenticator
/*    */ {
/*    */   private static JAuthenticator _instance;
/*    */   private boolean _challanging = false;
/*    */   private boolean _cancel = false;
/*    */   
/*    */   public static synchronized JAuthenticator getInstance(Frame paramFrame) {
/* 34 */     if (_instance == null) {
/* 35 */       _instance = new JAuthenticator();
/*    */     }
/* 37 */     _instance.setParentFrame(paramFrame);
/* 38 */     return _instance;
/*    */   }
/*    */ 
/*    */   
/*    */   protected synchronized PasswordAuthentication getPasswordAuthentication() {
/* 43 */     PasswordAuthentication passwordAuthentication = null;
/*    */     
/* 45 */     if (Config.getBooleanProperty("deployment.security.authenticator")) {
/*    */       
/* 47 */       this._challanging = true;
/*    */       
/* 49 */       passwordAuthentication = super.getPasswordAuthentication();
/*    */       
/* 51 */       this._challanging = false;
/*    */     } 
/*    */     
/* 54 */     return passwordAuthentication;
/*    */   }
/*    */   
/*    */   boolean isChallanging() {
/* 58 */     return this._challanging;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\JAuthenticator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */