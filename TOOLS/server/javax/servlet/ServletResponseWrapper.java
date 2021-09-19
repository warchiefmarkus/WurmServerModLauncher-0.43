/*     */ package javax.servlet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletResponseWrapper
/*     */   implements ServletResponse
/*     */ {
/*     */   private ServletResponse response;
/*     */   
/*     */   public ServletResponseWrapper(ServletResponse response) {
/*  88 */     if (response == null) {
/*  89 */       throw new IllegalArgumentException("Response cannot be null");
/*     */     }
/*  91 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletResponse getResponse() {
/*  99 */     return this.response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResponse(ServletResponse response) {
/* 109 */     if (response == null) {
/* 110 */       throw new IllegalArgumentException("Response cannot be null");
/*     */     }
/* 112 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterEncoding(String charset) {
/* 123 */     this.response.setCharacterEncoding(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharacterEncoding() {
/* 132 */     return this.response.getCharacterEncoding();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletOutputStream getOutputStream() throws IOException {
/* 142 */     return this.response.getOutputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getWriter() throws IOException {
/* 152 */     return this.response.getWriter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentLength(int len) {
/* 161 */     this.response.setContentLength(len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(String type) {
/* 170 */     this.response.setContentType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 181 */     return this.response.getContentType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferSize(int size) {
/* 189 */     this.response.setBufferSize(size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 197 */     return this.response.getBufferSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushBuffer() throws IOException {
/* 206 */     this.response.flushBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCommitted() {
/* 214 */     return this.response.isCommitted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 223 */     this.response.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetBuffer() {
/* 232 */     this.response.resetBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocale(Locale loc) {
/* 241 */     this.response.setLocale(loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 249 */     return this.response.getLocale();
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
/*     */   public boolean isWrapperFor(ServletResponse wrapped) {
/* 265 */     if (this.response == wrapped)
/* 266 */       return true; 
/* 267 */     if (this.response instanceof ServletResponseWrapper) {
/* 268 */       return ((ServletResponseWrapper)this.response).isWrapperFor(wrapped);
/*     */     }
/* 270 */     return false;
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
/*     */   
/*     */   public boolean isWrapperFor(Class<?> wrappedType) {
/* 291 */     if (!ServletResponse.class.isAssignableFrom(wrappedType)) {
/* 292 */       throw new IllegalArgumentException("Given class " + wrappedType.getName() + " not a subinterface of " + ServletResponse.class.getName());
/*     */     }
/*     */ 
/*     */     
/* 296 */     if (wrappedType.isAssignableFrom(this.response.getClass()))
/* 297 */       return true; 
/* 298 */     if (this.response instanceof ServletResponseWrapper) {
/* 299 */       return ((ServletResponseWrapper)this.response).isWrapperFor(wrappedType);
/*     */     }
/* 301 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletResponseWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */