/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.message.BasicHeader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractHttpEntity
/*     */   implements HttpEntity
/*     */ {
/*     */   protected Header contentType;
/*     */   protected Header contentEncoding;
/*     */   protected boolean chunked;
/*     */   
/*     */   public Header getContentType() {
/*  71 */     return this.contentType;
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
/*     */   public Header getContentEncoding() {
/*  83 */     return this.contentEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  94 */     return this.chunked;
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
/*     */   public void setContentType(Header contentType) {
/* 107 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(String ctString) {
/*     */     BasicHeader basicHeader;
/* 119 */     Header h = null;
/* 120 */     if (ctString != null) {
/* 121 */       basicHeader = new BasicHeader("Content-Type", ctString);
/*     */     }
/* 123 */     setContentType((Header)basicHeader);
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
/*     */   public void setContentEncoding(Header contentEncoding) {
/* 136 */     this.contentEncoding = contentEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentEncoding(String ceString) {
/*     */     BasicHeader basicHeader;
/* 148 */     Header h = null;
/* 149 */     if (ceString != null) {
/* 150 */       basicHeader = new BasicHeader("Content-Encoding", ceString);
/*     */     }
/* 152 */     setContentEncoding((Header)basicHeader);
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
/*     */   public void setChunked(boolean b) {
/* 171 */     this.chunked = b;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\AbstractHttpEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */