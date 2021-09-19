/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.ReasonPhraseCatalog;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHttpResponse;
/*     */ import org.apache.http.message.BasicStatusLine;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class DefaultHttpResponseFactory
/*     */   implements HttpResponseFactory
/*     */ {
/*     */   protected final ReasonPhraseCatalog reasonCatalog;
/*     */   
/*     */   public DefaultHttpResponseFactory(ReasonPhraseCatalog catalog) {
/*  61 */     if (catalog == null) {
/*  62 */       throw new IllegalArgumentException("Reason phrase catalog must not be null.");
/*     */     }
/*     */     
/*  65 */     this.reasonCatalog = catalog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpResponseFactory() {
/*  73 */     this(EnglishReasonPhraseCatalog.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse newHttpResponse(ProtocolVersion ver, int status, HttpContext context) {
/*  81 */     if (ver == null) {
/*  82 */       throw new IllegalArgumentException("HTTP version may not be null");
/*     */     }
/*  84 */     Locale loc = determineLocale(context);
/*  85 */     String reason = this.reasonCatalog.getReason(status, loc);
/*  86 */     BasicStatusLine basicStatusLine = new BasicStatusLine(ver, status, reason);
/*  87 */     return (HttpResponse)new BasicHttpResponse((StatusLine)basicStatusLine, this.reasonCatalog, loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponse newHttpResponse(StatusLine statusline, HttpContext context) {
/*  94 */     if (statusline == null) {
/*  95 */       throw new IllegalArgumentException("Status line may not be null");
/*     */     }
/*  97 */     Locale loc = determineLocale(context);
/*  98 */     return (HttpResponse)new BasicHttpResponse(statusline, this.reasonCatalog, loc);
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
/*     */   protected Locale determineLocale(HttpContext context) {
/* 112 */     return Locale.getDefault();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\DefaultHttpResponseFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */