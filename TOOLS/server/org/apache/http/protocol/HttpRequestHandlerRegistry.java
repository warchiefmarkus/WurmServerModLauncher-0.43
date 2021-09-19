/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HttpRequestHandlerRegistry
/*     */   implements HttpRequestHandlerResolver
/*     */ {
/*  57 */   private final UriPatternMatcher<HttpRequestHandler> matcher = new UriPatternMatcher<HttpRequestHandler>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String pattern, HttpRequestHandler handler) {
/*  68 */     if (pattern == null) {
/*  69 */       throw new IllegalArgumentException("URI request pattern may not be null");
/*     */     }
/*  71 */     if (handler == null) {
/*  72 */       throw new IllegalArgumentException("Request handler may not be null");
/*     */     }
/*  74 */     this.matcher.register(pattern, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(String pattern) {
/*  83 */     this.matcher.unregister(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlers(Map<String, HttpRequestHandler> map) {
/*  91 */     this.matcher.setObjects(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, HttpRequestHandler> getHandlers() {
/* 101 */     return this.matcher.getObjects();
/*     */   }
/*     */   
/*     */   public HttpRequestHandler lookup(String requestURI) {
/* 105 */     return this.matcher.lookup(requestURI);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\HttpRequestHandlerRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */