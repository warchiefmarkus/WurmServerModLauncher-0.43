/*    */ package org.kohsuke.rngom.xml.util;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ 
/*    */ public abstract class EncodingMap {
/*  6 */   private static final String[] aliases = new String[] { "UTF-8", "UTF8", "UTF-16", "Unicode", "UTF-16BE", "UnicodeBigUnmarked", "UTF-16LE", "UnicodeLittleUnmarked", "US-ASCII", "ASCII", "TIS-620", "TIS620" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getJavaName(String enc) {
/*    */     try {
/* 17 */       "x".getBytes(enc);
/*    */     }
/* 19 */     catch (UnsupportedEncodingException e) {
/* 20 */       for (int i = 0; i < aliases.length; i += 2) {
/* 21 */         if (enc.equalsIgnoreCase(aliases[i])) {
/*    */           try {
/* 23 */             "x".getBytes(aliases[i + 1]);
/* 24 */             return aliases[i + 1];
/*    */           }
/* 26 */           catch (UnsupportedEncodingException e2) {}
/*    */         }
/*    */       } 
/*    */     } 
/* 30 */     return enc;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 34 */     System.err.println(getJavaName(args[0]));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\xm\\util\EncodingMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */