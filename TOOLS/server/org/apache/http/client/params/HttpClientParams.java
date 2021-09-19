/*     */ package org.apache.http.client.params;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class HttpClientParams
/*     */ {
/*     */   public static boolean isRedirecting(HttpParams params) {
/*  47 */     if (params == null) {
/*  48 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  50 */     return params.getBooleanParameter("http.protocol.handle-redirects", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setRedirecting(HttpParams params, boolean value) {
/*  55 */     if (params == null) {
/*  56 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  58 */     params.setBooleanParameter("http.protocol.handle-redirects", value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAuthenticating(HttpParams params) {
/*  63 */     if (params == null) {
/*  64 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  66 */     return params.getBooleanParameter("http.protocol.handle-authentication", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setAuthenticating(HttpParams params, boolean value) {
/*  71 */     if (params == null) {
/*  72 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  74 */     params.setBooleanParameter("http.protocol.handle-authentication", value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getCookiePolicy(HttpParams params) {
/*  79 */     if (params == null) {
/*  80 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  82 */     String cookiePolicy = (String)params.getParameter("http.protocol.cookie-policy");
/*     */     
/*  84 */     if (cookiePolicy == null) {
/*  85 */       return "best-match";
/*     */     }
/*  87 */     return cookiePolicy;
/*     */   }
/*     */   
/*     */   public static void setCookiePolicy(HttpParams params, String cookiePolicy) {
/*  91 */     if (params == null) {
/*  92 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  94 */     params.setParameter("http.protocol.cookie-policy", cookiePolicy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setConnectionManagerTimeout(HttpParams params, long timeout) {
/* 103 */     if (params == null) {
/* 104 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 106 */     params.setLongParameter("http.conn-manager.timeout", timeout);
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
/*     */   public static long getConnectionManagerTimeout(HttpParams params) {
/* 119 */     if (params == null) {
/* 120 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 122 */     Long timeout = (Long)params.getParameter("http.conn-manager.timeout");
/* 123 */     if (timeout != null) {
/* 124 */       return timeout.longValue();
/*     */     }
/* 126 */     return HttpConnectionParams.getConnectionTimeout(params);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\params\HttpClientParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */