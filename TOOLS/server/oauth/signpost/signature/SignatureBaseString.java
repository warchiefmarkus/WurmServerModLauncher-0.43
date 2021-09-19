/*     */ package oauth.signpost.signature;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Iterator;
/*     */ import oauth.signpost.OAuth;
/*     */ import oauth.signpost.exception.OAuthMessageSignerException;
/*     */ import oauth.signpost.http.HttpParameters;
/*     */ import oauth.signpost.http.HttpRequest;
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
/*     */ public class SignatureBaseString
/*     */ {
/*     */   private HttpRequest request;
/*     */   private HttpParameters requestParameters;
/*     */   
/*     */   public SignatureBaseString(HttpRequest request, HttpParameters requestParameters) {
/*  40 */     this.request = request;
/*  41 */     this.requestParameters = requestParameters;
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
/*     */   public String generate() throws OAuthMessageSignerException {
/*     */     try {
/*  54 */       String normalizedUrl = normalizeRequestUrl();
/*  55 */       String normalizedParams = normalizeRequestParameters();
/*     */       
/*  57 */       return this.request.getMethod() + '&' + OAuth.percentEncode(normalizedUrl) + '&' + OAuth.percentEncode(normalizedParams);
/*     */     }
/*  59 */     catch (Exception e) {
/*  60 */       throw new OAuthMessageSignerException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String normalizeRequestUrl() throws URISyntaxException {
/*  65 */     URI uri = new URI(this.request.getRequestUrl());
/*  66 */     String scheme = uri.getScheme().toLowerCase();
/*  67 */     String authority = uri.getAuthority().toLowerCase();
/*  68 */     boolean dropPort = ((scheme.equals("http") && uri.getPort() == 80) || (scheme.equals("https") && uri.getPort() == 443));
/*     */     
/*  70 */     if (dropPort) {
/*     */       
/*  72 */       int index = authority.lastIndexOf(":");
/*  73 */       if (index >= 0) {
/*  74 */         authority = authority.substring(0, index);
/*     */       }
/*     */     } 
/*  77 */     String path = uri.getRawPath();
/*  78 */     if (path == null || path.length() <= 0) {
/*  79 */       path = "/";
/*     */     }
/*     */     
/*  82 */     return scheme + "://" + authority + path;
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
/*     */   public String normalizeRequestParameters() throws IOException {
/*  95 */     if (this.requestParameters == null) {
/*  96 */       return "";
/*     */     }
/*     */     
/*  99 */     StringBuilder sb = new StringBuilder();
/* 100 */     Iterator<String> iter = this.requestParameters.keySet().iterator();
/*     */     
/* 102 */     for (int i = 0; iter.hasNext(); i++) {
/* 103 */       String param = iter.next();
/*     */       
/* 105 */       if (!"oauth_signature".equals(param) && !"realm".equals(param)) {
/*     */ 
/*     */ 
/*     */         
/* 109 */         if (i > 0) {
/* 110 */           sb.append("&");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 115 */         sb.append(this.requestParameters.getAsQueryString(param, false));
/*     */       } 
/* 117 */     }  return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\signature\SignatureBaseString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */