/*    */ package org.apache.http.auth.params;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.protocol.HTTP;
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
/*    */ @Immutable
/*    */ public final class AuthParams
/*    */ {
/*    */   public static String getCredentialCharset(HttpParams params) {
/* 58 */     if (params == null) {
/* 59 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*    */     }
/* 61 */     String charset = (String)params.getParameter("http.auth.credential-charset");
/*    */     
/* 63 */     if (charset == null) {
/* 64 */       charset = HTTP.DEF_PROTOCOL_CHARSET.name();
/*    */     }
/* 66 */     return charset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setCredentialCharset(HttpParams params, String charset) {
/* 77 */     if (params == null) {
/* 78 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*    */     }
/* 80 */     params.setParameter("http.auth.credential-charset", charset);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\params\AuthParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */