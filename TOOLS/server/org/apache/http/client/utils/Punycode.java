/*    */ package org.apache.http.client.utils;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class Punycode
/*    */ {
/*    */   private static final Idn impl;
/*    */   
/*    */   static {
/*    */     Idn idn;
/*    */     try {
/* 47 */       idn = new JdkIdn();
/* 48 */     } catch (Exception e) {
/* 49 */       idn = new Rfc3492Idn();
/*    */     } 
/* 51 */     impl = idn;
/*    */   }
/*    */   
/*    */   public static String toUnicode(String punycode) {
/* 55 */     return impl.toUnicode(punycode);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\clien\\utils\Punycode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */