/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.MethodNotSupportedException;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHttpEntityEnclosingRequest;
/*     */ import org.apache.http.message.BasicHttpRequest;
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
/*     */ public class DefaultHttpRequestFactory
/*     */   implements HttpRequestFactory
/*     */ {
/*  46 */   private static final String[] RFC2616_COMMON_METHODS = new String[] { "GET" };
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final String[] RFC2616_ENTITY_ENC_METHODS = new String[] { "POST", "PUT" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final String[] RFC2616_SPECIAL_METHODS = new String[] { "HEAD", "OPTIONS", "DELETE", "TRACE", "CONNECT" };
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
/*     */   private static boolean isOneOf(String[] methods, String method) {
/*  69 */     for (int i = 0; i < methods.length; i++) {
/*  70 */       if (methods[i].equalsIgnoreCase(method)) {
/*  71 */         return true;
/*     */       }
/*     */     } 
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequest newHttpRequest(RequestLine requestline) throws MethodNotSupportedException {
/*  79 */     if (requestline == null) {
/*  80 */       throw new IllegalArgumentException("Request line may not be null");
/*     */     }
/*  82 */     String method = requestline.getMethod();
/*  83 */     if (isOneOf(RFC2616_COMMON_METHODS, method))
/*  84 */       return (HttpRequest)new BasicHttpRequest(requestline); 
/*  85 */     if (isOneOf(RFC2616_ENTITY_ENC_METHODS, method))
/*  86 */       return (HttpRequest)new BasicHttpEntityEnclosingRequest(requestline); 
/*  87 */     if (isOneOf(RFC2616_SPECIAL_METHODS, method)) {
/*  88 */       return (HttpRequest)new BasicHttpRequest(requestline);
/*     */     }
/*  90 */     throw new MethodNotSupportedException(method + " method not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequest newHttpRequest(String method, String uri) throws MethodNotSupportedException {
/*  96 */     if (isOneOf(RFC2616_COMMON_METHODS, method))
/*  97 */       return (HttpRequest)new BasicHttpRequest(method, uri); 
/*  98 */     if (isOneOf(RFC2616_ENTITY_ENC_METHODS, method))
/*  99 */       return (HttpRequest)new BasicHttpEntityEnclosingRequest(method, uri); 
/* 100 */     if (isOneOf(RFC2616_SPECIAL_METHODS, method)) {
/* 101 */       return (HttpRequest)new BasicHttpRequest(method, uri);
/*     */     }
/* 103 */     throw new MethodNotSupportedException(method + " method not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\DefaultHttpRequestFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */