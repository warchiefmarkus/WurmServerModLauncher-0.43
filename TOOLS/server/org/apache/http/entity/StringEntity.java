/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class StringEntity
/*     */   extends AbstractHttpEntity
/*     */   implements Cloneable
/*     */ {
/*     */   protected final byte[] content;
/*     */   
/*     */   public StringEntity(String string, ContentType contentType) {
/*  65 */     if (string == null) {
/*  66 */       throw new IllegalArgumentException("Source string may not be null");
/*     */     }
/*  68 */     Charset charset = (contentType != null) ? contentType.getCharset() : null;
/*  69 */     if (charset == null) {
/*  70 */       charset = HTTP.DEF_CONTENT_CHARSET;
/*     */     }
/*     */     try {
/*  73 */       this.content = string.getBytes(charset.name());
/*  74 */     } catch (UnsupportedEncodingException ex) {
/*     */       
/*  76 */       throw new UnsupportedCharsetException(charset.name());
/*     */     } 
/*  78 */     if (contentType != null) {
/*  79 */       setContentType(contentType.toString());
/*     */     }
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
/*     */   @Deprecated
/*     */   public StringEntity(String string, String mimeType, String charset) throws UnsupportedEncodingException {
/* 101 */     if (string == null) {
/* 102 */       throw new IllegalArgumentException("Source string may not be null");
/*     */     }
/* 104 */     if (mimeType == null) {
/* 105 */       mimeType = "text/plain";
/*     */     }
/* 107 */     if (charset == null) {
/* 108 */       charset = "ISO-8859-1";
/*     */     }
/* 110 */     this.content = string.getBytes(charset);
/* 111 */     setContentType(mimeType + "; charset=" + charset);
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
/*     */   public StringEntity(String string, String charset) throws UnsupportedEncodingException {
/* 127 */     this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
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
/*     */   public StringEntity(String string, Charset charset) {
/* 143 */     this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
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
/*     */   public StringEntity(String string) throws UnsupportedEncodingException {
/* 157 */     this(string, ContentType.DEFAULT_TEXT);
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   public long getContentLength() {
/* 165 */     return this.content.length;
/*     */   }
/*     */   
/*     */   public InputStream getContent() throws IOException {
/* 169 */     return new ByteArrayInputStream(this.content);
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 173 */     if (outstream == null) {
/* 174 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/* 176 */     outstream.write(this.content);
/* 177 */     outstream.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 191 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\StringEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */