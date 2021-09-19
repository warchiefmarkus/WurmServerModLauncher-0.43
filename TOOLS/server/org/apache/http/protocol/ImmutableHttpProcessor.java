/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.ThreadSafe;
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
/*     */ @ThreadSafe
/*     */ public final class ImmutableHttpProcessor
/*     */   implements HttpProcessor
/*     */ {
/*     */   private final HttpRequestInterceptor[] requestInterceptors;
/*     */   private final HttpResponseInterceptor[] responseInterceptors;
/*     */   
/*     */   public ImmutableHttpProcessor(HttpRequestInterceptor[] requestInterceptors, HttpResponseInterceptor[] responseInterceptors) {
/*  53 */     if (requestInterceptors != null) {
/*  54 */       int count = requestInterceptors.length;
/*  55 */       this.requestInterceptors = new HttpRequestInterceptor[count];
/*  56 */       for (int i = 0; i < count; i++) {
/*  57 */         this.requestInterceptors[i] = requestInterceptors[i];
/*     */       }
/*     */     } else {
/*  60 */       this.requestInterceptors = new HttpRequestInterceptor[0];
/*     */     } 
/*  62 */     if (responseInterceptors != null) {
/*  63 */       int count = responseInterceptors.length;
/*  64 */       this.responseInterceptors = new HttpResponseInterceptor[count];
/*  65 */       for (int i = 0; i < count; i++) {
/*  66 */         this.responseInterceptors[i] = responseInterceptors[i];
/*     */       }
/*     */     } else {
/*  69 */       this.responseInterceptors = new HttpResponseInterceptor[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableHttpProcessor(HttpRequestInterceptorList requestInterceptors, HttpResponseInterceptorList responseInterceptors) {
/*  77 */     if (requestInterceptors != null) {
/*  78 */       int count = requestInterceptors.getRequestInterceptorCount();
/*  79 */       this.requestInterceptors = new HttpRequestInterceptor[count];
/*  80 */       for (int i = 0; i < count; i++) {
/*  81 */         this.requestInterceptors[i] = requestInterceptors.getRequestInterceptor(i);
/*     */       }
/*     */     } else {
/*  84 */       this.requestInterceptors = new HttpRequestInterceptor[0];
/*     */     } 
/*  86 */     if (responseInterceptors != null) {
/*  87 */       int count = responseInterceptors.getResponseInterceptorCount();
/*  88 */       this.responseInterceptors = new HttpResponseInterceptor[count];
/*  89 */       for (int i = 0; i < count; i++) {
/*  90 */         this.responseInterceptors[i] = responseInterceptors.getResponseInterceptor(i);
/*     */       }
/*     */     } else {
/*  93 */       this.responseInterceptors = new HttpResponseInterceptor[0];
/*     */     } 
/*     */   }
/*     */   
/*     */   public ImmutableHttpProcessor(HttpRequestInterceptor[] requestInterceptors) {
/*  98 */     this(requestInterceptors, (HttpResponseInterceptor[])null);
/*     */   }
/*     */   
/*     */   public ImmutableHttpProcessor(HttpResponseInterceptor[] responseInterceptors) {
/* 102 */     this((HttpRequestInterceptor[])null, responseInterceptors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
/* 108 */     for (int i = 0; i < this.requestInterceptors.length; i++) {
/* 109 */       this.requestInterceptors[i].process(request, context);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
/* 116 */     for (int i = 0; i < this.responseInterceptors.length; i++)
/* 117 */       this.responseInterceptors[i].process(response, context); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\ImmutableHttpProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */