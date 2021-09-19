/*    */ package com.sun.tools.xjc.util;
/*    */ 
/*    */ import org.xml.sax.Locator;
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
/*    */ public final class Util
/*    */ {
/*    */   public static String getSystemProperty(String name) {
/*    */     try {
/* 57 */       return System.getProperty(name);
/* 58 */     } catch (SecurityException e) {
/* 59 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean equals(Locator lhs, Locator rhs) {
/* 67 */     return (lhs.getLineNumber() == rhs.getLineNumber() && lhs.getColumnNumber() == rhs.getColumnNumber() && equals(lhs.getSystemId(), rhs.getSystemId()) && equals(lhs.getPublicId(), rhs.getPublicId()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean equals(String lhs, String rhs) {
/* 74 */     if (lhs == null && rhs == null) return true; 
/* 75 */     if (lhs == null || rhs == null) return false; 
/* 76 */     return lhs.equals(rhs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getSystemProperty(Class clazz, String name) {
/* 84 */     return getSystemProperty(clazz.getName() + '.' + name);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */