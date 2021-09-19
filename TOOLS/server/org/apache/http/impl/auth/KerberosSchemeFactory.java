/*    */ package org.apache.http.impl.auth;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthScheme;
/*    */ import org.apache.http.auth.AuthSchemeFactory;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ public class KerberosSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/*    */   private final boolean stripPort;
/*    */   
/*    */   public KerberosSchemeFactory(boolean stripPort) {
/* 46 */     this.stripPort = stripPort;
/*    */   }
/*    */   
/*    */   public KerberosSchemeFactory() {
/* 50 */     this(false);
/*    */   }
/*    */   
/*    */   public AuthScheme newInstance(HttpParams params) {
/* 54 */     return (AuthScheme)new KerberosScheme(this.stripPort);
/*    */   }
/*    */   
/*    */   public boolean isStripPort() {
/* 58 */     return this.stripPort;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\KerberosSchemeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */