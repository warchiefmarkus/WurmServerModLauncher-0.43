/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.Oid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SPNegoScheme
/*     */   extends GGSSchemeBase
/*     */ {
/*     */   private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
/*     */   
/*     */   public SPNegoScheme(boolean stripPort) {
/*  47 */     super(stripPort);
/*     */   }
/*     */   
/*     */   public SPNegoScheme() {
/*  51 */     super(false);
/*     */   }
/*     */   
/*     */   public String getSchemeName() {
/*  55 */     return "Negotiate";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/*  75 */     return super.authenticate(credentials, request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateToken(byte[] input, String authServer) throws GSSException {
/*  80 */     return generateGSSToken(input, new Oid("1.3.6.1.5.5.2"), authServer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/*  90 */     if (name == null) {
/*  91 */       throw new IllegalArgumentException("Parameter name may not be null");
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 112 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\SPNegoScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */