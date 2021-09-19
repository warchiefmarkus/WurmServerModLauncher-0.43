/*    */ package com.sun.xml.bind;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Util
/*    */ {
/*    */   public static Logger getClassLogger() {
/*    */     try {
/* 55 */       StackTraceElement[] trace = (new Exception()).getStackTrace();
/* 56 */       return Logger.getLogger(trace[1].getClassName());
/* 57 */     } catch (SecurityException _) {
/* 58 */       return Logger.getLogger("com.sun.xml.bind");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getSystemProperty(String name) {
/*    */     try {
/* 67 */       return System.getProperty(name);
/* 68 */     } catch (SecurityException e) {
/* 69 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */