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
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class NTLMSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/*    */   public AuthScheme newInstance(HttpParams params) {
/* 47 */     return (AuthScheme)new NTLMScheme(new NTLMEngineImpl());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\NTLMSchemeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */