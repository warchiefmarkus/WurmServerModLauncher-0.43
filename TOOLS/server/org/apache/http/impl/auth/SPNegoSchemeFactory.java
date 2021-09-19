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
/*    */ 
/*    */ @Immutable
/*    */ public class SPNegoSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/*    */   private final boolean stripPort;
/*    */   
/*    */   public SPNegoSchemeFactory(boolean stripPort) {
/* 47 */     this.stripPort = stripPort;
/*    */   }
/*    */   
/*    */   public SPNegoSchemeFactory() {
/* 51 */     this(false);
/*    */   }
/*    */   
/*    */   public AuthScheme newInstance(HttpParams params) {
/* 55 */     return (AuthScheme)new SPNegoScheme(this.stripPort);
/*    */   }
/*    */   
/*    */   public boolean isStripPort() {
/* 59 */     return this.stripPort;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\SPNegoSchemeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */