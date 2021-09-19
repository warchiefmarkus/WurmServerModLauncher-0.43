/*    */ package org.apache.http.impl.auth;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class NegotiateSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/*    */   private final SpnegoTokenGenerator spengoGenerator;
/*    */   private final boolean stripPort;
/*    */   
/*    */   public NegotiateSchemeFactory(SpnegoTokenGenerator spengoGenerator, boolean stripPort) {
/* 49 */     this.spengoGenerator = spengoGenerator;
/* 50 */     this.stripPort = stripPort;
/*    */   }
/*    */   
/*    */   public NegotiateSchemeFactory(SpnegoTokenGenerator spengoGenerator) {
/* 54 */     this(spengoGenerator, false);
/*    */   }
/*    */   
/*    */   public NegotiateSchemeFactory() {
/* 58 */     this(null, false);
/*    */   }
/*    */   
/*    */   public AuthScheme newInstance(HttpParams params) {
/* 62 */     return (AuthScheme)new NegotiateScheme(this.spengoGenerator, this.stripPort);
/*    */   }
/*    */   
/*    */   public boolean isStripPort() {
/* 66 */     return this.stripPort;
/*    */   }
/*    */   
/*    */   public SpnegoTokenGenerator getSpengoGenerator() {
/* 70 */     return this.spengoGenerator;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\NegotiateSchemeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */