/*    */ package org.apache.http.conn.ssl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class AllowAllHostnameVerifier
/*    */   extends AbstractVerifier
/*    */ {
/*    */   public final void verify(String host, String[] cns, String[] subjectAlts) {}
/*    */   
/*    */   public final String toString() {
/* 51 */     return "ALLOW_ALL";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\ssl\AllowAllHostnameVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */