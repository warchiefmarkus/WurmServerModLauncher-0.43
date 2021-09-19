/*    */ package com.wurmonline.server.utils;
/*    */ 
/*    */ import java.util.IllegalFormatException;
/*    */ import java.util.Locale;
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
/*    */ public class StringUtil
/*    */ {
/* 30 */   private static final Logger logger = Logger.getLogger(StringUtil.class.getName());
/* 31 */   private static Locale locale = Locale.ENGLISH;
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
/*    */   public static String format(String format, Object... args) {
/*    */     try {
/* 45 */       return String.format(locale, format, args);
/*    */     }
/* 47 */     catch (IllegalFormatException ife) {
/*    */       
/* 49 */       logger.log(Level.WARNING, format, ife);
/* 50 */       return "";
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String toLowerCase(String original) {
/* 56 */     return original.toLowerCase(locale);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toLowerCase(Object obj) {
/* 67 */     if (obj == null)
/* 68 */       return ""; 
/* 69 */     return toLowerCase(obj.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public static String toUpperCase(String original) {
/* 74 */     return original.toUpperCase(locale);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toUpperCase(Object obj) {
/* 85 */     if (obj == null)
/* 86 */       return ""; 
/* 87 */     return toUpperCase(obj.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\StringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */