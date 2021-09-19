/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class NegotiateScheme
/*     */   extends GGSSchemeBase
/*     */ {
/*  51 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
/*     */ 
/*     */   
/*     */   private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
/*     */   
/*     */   private final SpnegoTokenGenerator spengoGenerator;
/*     */ 
/*     */   
/*     */   public NegotiateScheme(SpnegoTokenGenerator spengoGenerator, boolean stripPort) {
/*  63 */     super(stripPort);
/*  64 */     this.spengoGenerator = spengoGenerator;
/*     */   }
/*     */   
/*     */   public NegotiateScheme(SpnegoTokenGenerator spengoGenerator) {
/*  68 */     this(spengoGenerator, false);
/*     */   }
/*     */   
/*     */   public NegotiateScheme() {
/*  72 */     this(null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/*  81 */     return "Negotiate";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/*  87 */     return authenticate(credentials, request, null);
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
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 108 */     return super.authenticate(credentials, request, context);
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
/*     */   protected byte[] generateToken(byte[] input, String authServer) throws GSSException {
/* 128 */     Oid negotiationOid = new Oid("1.3.6.1.5.5.2");
/*     */     
/* 130 */     byte[] token = input;
/* 131 */     boolean tryKerberos = false;
/*     */     try {
/* 133 */       token = generateGSSToken(token, negotiationOid, authServer);
/* 134 */     } catch (GSSException ex) {
/*     */ 
/*     */       
/* 137 */       if (ex.getMajor() == 2) {
/* 138 */         this.log.debug("GSSException BAD_MECH, retry with Kerberos MECH");
/* 139 */         tryKerberos = true;
/*     */       } else {
/* 141 */         throw ex;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     if (tryKerberos) {
/*     */       
/* 147 */       this.log.debug("Using Kerberos MECH 1.2.840.113554.1.2.2");
/* 148 */       negotiationOid = new Oid("1.2.840.113554.1.2.2");
/* 149 */       token = generateGSSToken(token, negotiationOid, authServer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 155 */       if (token != null && this.spengoGenerator != null) {
/*     */         try {
/* 157 */           token = this.spengoGenerator.generateSpnegoDERObject(token);
/* 158 */         } catch (IOException ex) {
/* 159 */           this.log.error(ex.getMessage(), ex);
/*     */         } 
/*     */       }
/*     */     } 
/* 163 */     return token;
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
/*     */   public String getParameter(String name) {
/* 177 */     if (name == null) {
/* 178 */       throw new IllegalArgumentException("Parameter name may not be null");
/*     */     }
/* 180 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 200 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\NegotiateScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */