/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import javax.servlet.ServletResponseWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpServletResponseWrapper
/*     */   extends ServletResponseWrapper
/*     */   implements HttpServletResponse
/*     */ {
/*     */   public HttpServletResponseWrapper(HttpServletResponse response) {
/*  86 */     super(response);
/*     */   }
/*     */   
/*     */   private HttpServletResponse _getHttpServletResponse() {
/*  90 */     return (HttpServletResponse)getResponse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCookie(Cookie cookie) {
/*  98 */     _getHttpServletResponse().addCookie(cookie);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsHeader(String name) {
/* 108 */     return _getHttpServletResponse().containsHeader(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encodeURL(String url) {
/* 116 */     return _getHttpServletResponse().encodeURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encodeRedirectURL(String url) {
/* 124 */     return _getHttpServletResponse().encodeRedirectURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encodeUrl(String url) {
/* 135 */     return _getHttpServletResponse().encodeUrl(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encodeRedirectUrl(String url) {
/* 146 */     return _getHttpServletResponse().encodeRedirectUrl(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendError(int sc, String msg) throws IOException {
/* 154 */     _getHttpServletResponse().sendError(sc, msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendError(int sc) throws IOException {
/* 164 */     _getHttpServletResponse().sendError(sc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRedirect(String location) throws IOException {
/* 172 */     _getHttpServletResponse().sendRedirect(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateHeader(String name, long date) {
/* 180 */     _getHttpServletResponse().setDateHeader(name, date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDateHeader(String name, long date) {
/* 188 */     _getHttpServletResponse().addDateHeader(name, date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeader(String name, String value) {
/* 196 */     _getHttpServletResponse().setHeader(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(String name, String value) {
/* 204 */     _getHttpServletResponse().addHeader(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIntHeader(String name, int value) {
/* 212 */     _getHttpServletResponse().setIntHeader(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIntHeader(String name, int value) {
/* 220 */     _getHttpServletResponse().addIntHeader(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(int sc) {
/* 228 */     _getHttpServletResponse().setStatus(sc);
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
/*     */   public void setStatus(int sc, String sm) {
/* 241 */     _getHttpServletResponse().setStatus(sc, sm);
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
/*     */   public int getStatus() {
/* 253 */     return _getHttpServletResponse().getStatus();
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
/*     */   public String getHeader(String name) {
/* 271 */     return _getHttpServletResponse().getHeader(name);
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
/*     */   public Collection<String> getHeaders(String name) {
/* 291 */     return _getHttpServletResponse().getHeaders(name);
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
/*     */   public Collection<String> getHeaderNames() {
/* 309 */     return _getHttpServletResponse().getHeaderNames();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpServletResponseWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */