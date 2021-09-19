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
/*     */ public class KerberosScheme
/*     */   extends GGSSchemeBase
/*     */ {
/*     */   private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
/*     */   
/*     */   public KerberosScheme(boolean stripPort) {
/*  46 */     super(stripPort);
/*     */   }
/*     */   
/*     */   public KerberosScheme() {
/*  50 */     super(false);
/*     */   }
/*     */   
/*     */   public String getSchemeName() {
/*  54 */     return "Kerberos";
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
/*  74 */     return super.authenticate(credentials, request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateToken(byte[] input, String authServer) throws GSSException {
/*  79 */     return generateGSSToken(input, new Oid("1.2.840.113554.1.2.2"), authServer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/*  89 */     if (name == null) {
/*  90 */       throw new IllegalArgumentException("Parameter name may not be null");
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 111 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\KerberosScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */