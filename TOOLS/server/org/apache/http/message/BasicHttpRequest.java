/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.params.HttpProtocolParams;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BasicHttpRequest
/*     */   extends AbstractHttpMessage
/*     */   implements HttpRequest
/*     */ {
/*     */   private final String method;
/*     */   private final String uri;
/*     */   private RequestLine requestline;
/*     */   
/*     */   public BasicHttpRequest(String method, String uri) {
/*  67 */     if (method == null) {
/*  68 */       throw new IllegalArgumentException("Method name may not be null");
/*     */     }
/*  70 */     if (uri == null) {
/*  71 */       throw new IllegalArgumentException("Request URI may not be null");
/*     */     }
/*  73 */     this.method = method;
/*  74 */     this.uri = uri;
/*  75 */     this.requestline = null;
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
/*     */   public BasicHttpRequest(String method, String uri, ProtocolVersion ver) {
/*  87 */     this(new BasicRequestLine(method, uri, ver));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpRequest(RequestLine requestline) {
/*  97 */     if (requestline == null) {
/*  98 */       throw new IllegalArgumentException("Request line may not be null");
/*     */     }
/* 100 */     this.requestline = requestline;
/* 101 */     this.method = requestline.getMethod();
/* 102 */     this.uri = requestline.getUri();
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
/*     */   public ProtocolVersion getProtocolVersion() {
/* 114 */     return getRequestLine().getProtocolVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestLine getRequestLine() {
/* 125 */     if (this.requestline == null) {
/* 126 */       ProtocolVersion ver = HttpProtocolParams.getVersion(getParams());
/* 127 */       this.requestline = new BasicRequestLine(this.method, this.uri, ver);
/*     */     } 
/* 129 */     return this.requestline;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return this.method + " " + this.uri + " " + this.headergroup;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHttpRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */