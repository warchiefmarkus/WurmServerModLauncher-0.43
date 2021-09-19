/*    */ package com.sun.javaws.util;
/*    */ 
/*    */ import java.security.Policy;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public class JavawsConsoleController14
/*    */   extends JavawsConsoleController
/*    */ {
/* 23 */   private static Logger logger = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogger(Logger paramLogger) {
/* 30 */     if (logger == null) {
/* 31 */       logger = paramLogger;
/*    */     }
/*    */   }
/*    */   
/*    */   public Logger getLogger() {
/* 36 */     return logger;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSecurityPolicyReloadSupported() {
/* 43 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reloadSecurityPolicy() {
/* 50 */     Policy policy = Policy.getPolicy();
/* 51 */     policy.refresh();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isLoggingSupported() {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean toggleLogging() {
/* 67 */     if (logger != null) {
/* 68 */       Level level = logger.getLevel();
/*    */       
/* 70 */       if (level == Level.OFF) {
/* 71 */         level = Level.ALL;
/*    */       } else {
/* 73 */         level = Level.OFF;
/*    */       } 
/* 75 */       logger.setLevel(level);
/*    */       
/* 77 */       return (level == Level.ALL);
/*    */     } 
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\util\JavawsConsoleController14.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */