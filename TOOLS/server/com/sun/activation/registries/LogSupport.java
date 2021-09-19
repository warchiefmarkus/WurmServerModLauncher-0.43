/*    */ package com.sun.activation.registries;
/*    */ 
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
/*    */ public class LogSupport
/*    */ {
/*    */   private static boolean debug = false;
/*    */   private static Logger logger;
/* 52 */   private static final Level level = Level.FINE;
/*    */   
/*    */   static {
/*    */     try {
/* 56 */       debug = Boolean.getBoolean("javax.activation.debug");
/* 57 */     } catch (Throwable t) {}
/*    */ 
/*    */     
/* 60 */     logger = Logger.getLogger("javax.activation");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void log(String msg) {
/* 71 */     if (debug)
/* 72 */       System.out.println(msg); 
/* 73 */     logger.log(level, msg);
/*    */   }
/*    */   
/*    */   public static void log(String msg, Throwable t) {
/* 77 */     if (debug)
/* 78 */       System.out.println(msg + "; Exception: " + t); 
/* 79 */     logger.log(level, msg, t);
/*    */   }
/*    */   
/*    */   public static boolean isLoggable() {
/* 83 */     return (debug || logger.isLoggable(level));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\registries\LogSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */