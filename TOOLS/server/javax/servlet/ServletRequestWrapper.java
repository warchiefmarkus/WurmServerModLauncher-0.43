/*     */ package javax.servlet;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletRequestWrapper
/*     */   implements ServletRequest
/*     */ {
/*     */   private ServletRequest request;
/*     */   
/*     */   public ServletRequestWrapper(ServletRequest request) {
/*  88 */     if (request == null) {
/*  89 */       throw new IllegalArgumentException("Request cannot be null");
/*     */     }
/*  91 */     this.request = request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletRequest getRequest() {
/*  99 */     return this.request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequest(ServletRequest request) {
/* 108 */     if (request == null) {
/* 109 */       throw new IllegalArgumentException("Request cannot be null");
/*     */     }
/* 111 */     this.request = request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name) {
/* 120 */     return this.request.getAttribute(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getAttributeNames() {
/* 129 */     return this.request.getAttributeNames();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharacterEncoding() {
/* 138 */     return this.request.getCharacterEncoding();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
/* 148 */     this.request.setCharacterEncoding(enc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getContentLength() {
/* 157 */     return this.request.getContentLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 166 */     return this.request.getContentType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletInputStream getInputStream() throws IOException {
/* 175 */     return this.request.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 184 */     return this.request.getParameter(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String[]> getParameterMap() {
/* 193 */     return this.request.getParameterMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/* 202 */     return this.request.getParameterNames();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getParameterValues(String name) {
/* 211 */     return this.request.getParameterValues(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProtocol() {
/* 220 */     return this.request.getProtocol();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScheme() {
/* 229 */     return this.request.getScheme();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerName() {
/* 238 */     return this.request.getServerName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getServerPort() {
/* 247 */     return this.request.getServerPort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedReader getReader() throws IOException {
/* 256 */     return this.request.getReader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoteAddr() {
/* 265 */     return this.request.getRemoteAddr();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoteHost() {
/* 274 */     return this.request.getRemoteHost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object o) {
/* 283 */     this.request.setAttribute(name, o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAttribute(String name) {
/* 292 */     this.request.removeAttribute(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 301 */     return this.request.getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<Locale> getLocales() {
/* 310 */     return this.request.getLocales();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 319 */     return this.request.isSecure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestDispatcher getRequestDispatcher(String path) {
/* 328 */     return this.request.getRequestDispatcher(path);
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
/*     */   public String getRealPath(String path) {
/* 340 */     return this.request.getRealPath(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 351 */     return this.request.getRemotePort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocalName() {
/* 362 */     return this.request.getLocalName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocalAddr() {
/* 373 */     return this.request.getLocalAddr();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 384 */     return this.request.getLocalPort();
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
/*     */   public ServletContext getServletContext() {
/* 398 */     return this.request.getServletContext();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncContext startAsync() throws IllegalStateException {
/* 422 */     return this.request.startAsync();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
/* 454 */     return this.request.startAsync(servletRequest, servletResponse);
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
/*     */   public boolean isAsyncStarted() {
/* 469 */     return this.request.isAsyncStarted();
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
/*     */   public boolean isAsyncSupported() {
/* 484 */     return this.request.isAsyncSupported();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncContext getAsyncContext() {
/* 508 */     return this.request.getAsyncContext();
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
/*     */   public boolean isWrapperFor(ServletRequest wrapped) {
/* 524 */     if (this.request == wrapped)
/* 525 */       return true; 
/* 526 */     if (this.request instanceof ServletRequestWrapper) {
/* 527 */       return ((ServletRequestWrapper)this.request).isWrapperFor(wrapped);
/*     */     }
/* 529 */     return false;
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
/* 550 */     if (!ServletRequest.class.isAssignableFrom(wrappedType)) {
/* 551 */       throw new IllegalArgumentException("Given class " + wrappedType.getName() + " not a subinterface of " + ServletRequest.class.getName());
/*     */     }
/*     */ 
/*     */     
/* 555 */     if (wrappedType.isAssignableFrom(this.request.getClass()))
/* 556 */       return true; 
/* 557 */     if (this.request instanceof ServletRequestWrapper) {
/* 558 */       return ((ServletRequestWrapper)this.request).isWrapperFor(wrappedType);
/*     */     }
/* 560 */     return false;
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
/*     */   public DispatcherType getDispatcherType() {
/* 575 */     return this.request.getDispatcherType();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletRequestWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */