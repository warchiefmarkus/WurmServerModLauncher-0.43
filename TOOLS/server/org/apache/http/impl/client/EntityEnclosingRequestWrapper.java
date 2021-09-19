/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EntityEnclosingRequestWrapper
/*     */   extends RequestWrapper
/*     */   implements HttpEntityEnclosingRequest
/*     */ {
/*     */   private HttpEntity entity;
/*     */   private boolean consumed;
/*     */   
/*     */   public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request) throws ProtocolException {
/*  63 */     super((HttpRequest)request);
/*  64 */     setEntity(request.getEntity());
/*     */   }
/*     */   
/*     */   public HttpEntity getEntity() {
/*  68 */     return this.entity;
/*     */   }
/*     */   
/*     */   public void setEntity(HttpEntity entity) {
/*  72 */     this.entity = (entity != null) ? (HttpEntity)new EntityWrapper(entity) : null;
/*  73 */     this.consumed = false;
/*     */   }
/*     */   
/*     */   public boolean expectContinue() {
/*  77 */     Header expect = getFirstHeader("Expect");
/*  78 */     return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  83 */     return (this.entity == null || this.entity.isRepeatable() || !this.consumed);
/*     */   }
/*     */   
/*     */   class EntityWrapper
/*     */     extends HttpEntityWrapper {
/*     */     EntityWrapper(HttpEntity entity) {
/*  89 */       super(entity);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void consumeContent() throws IOException {
/*  95 */       EntityEnclosingRequestWrapper.this.consumed = true;
/*  96 */       super.consumeContent();
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getContent() throws IOException {
/* 101 */       EntityEnclosingRequestWrapper.this.consumed = true;
/* 102 */       return super.getContent();
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeTo(OutputStream outstream) throws IOException {
/* 107 */       EntityEnclosingRequestWrapper.this.consumed = true;
/* 108 */       super.writeTo(outstream);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\EntityEnclosingRequestWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */