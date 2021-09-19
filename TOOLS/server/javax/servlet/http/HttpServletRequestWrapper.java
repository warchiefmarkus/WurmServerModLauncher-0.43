/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequestWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpServletRequestWrapper
/*     */   extends ServletRequestWrapper
/*     */   implements HttpServletRequest
/*     */ {
/*     */   public HttpServletRequestWrapper(HttpServletRequest request) {
/*  86 */     super(request);
/*     */   }
/*     */   
/*     */   private HttpServletRequest _getHttpServletRequest() {
/*  90 */     return (HttpServletRequest)getRequest();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthType() {
/*  99 */     return _getHttpServletRequest().getAuthType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cookie[] getCookies() {
/* 107 */     return _getHttpServletRequest().getCookies();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDateHeader(String name) {
/* 115 */     return _getHttpServletRequest().getDateHeader(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHeader(String name) {
/* 123 */     return _getHttpServletRequest().getHeader(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getHeaders(String name) {
/* 131 */     return _getHttpServletRequest().getHeaders(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getHeaderNames() {
/* 140 */     return _getHttpServletRequest().getHeaderNames();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIntHeader(String name) {
/* 149 */     return _getHttpServletRequest().getIntHeader(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethod() {
/* 157 */     return _getHttpServletRequest().getMethod();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPathInfo() {
/* 165 */     return _getHttpServletRequest().getPathInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPathTranslated() {
/* 174 */     return _getHttpServletRequest().getPathTranslated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContextPath() {
/* 182 */     return _getHttpServletRequest().getContextPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQueryString() {
/* 190 */     return _getHttpServletRequest().getQueryString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoteUser() {
/* 198 */     return _getHttpServletRequest().getRemoteUser();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUserInRole(String role) {
/* 207 */     return _getHttpServletRequest().isUserInRole(role);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/* 217 */     return _getHttpServletRequest().getUserPrincipal();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequestedSessionId() {
/* 226 */     return _getHttpServletRequest().getRequestedSessionId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequestURI() {
/* 234 */     return _getHttpServletRequest().getRequestURI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringBuffer getRequestURL() {
/* 241 */     return _getHttpServletRequest().getRequestURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServletPath() {
/* 250 */     return _getHttpServletRequest().getServletPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpSession getSession(boolean create) {
/* 259 */     return _getHttpServletRequest().getSession(create);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpSession getSession() {
/* 267 */     return _getHttpServletRequest().getSession();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestedSessionIdValid() {
/* 276 */     return _getHttpServletRequest().isRequestedSessionIdValid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestedSessionIdFromCookie() {
/* 285 */     return _getHttpServletRequest().isRequestedSessionIdFromCookie();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestedSessionIdFromURL() {
/* 294 */     return _getHttpServletRequest().isRequestedSessionIdFromURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestedSessionIdFromUrl() {
/* 303 */     return _getHttpServletRequest().isRequestedSessionIdFromUrl();
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
/*     */   public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
/* 315 */     return _getHttpServletRequest().authenticate(response);
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
/*     */   public void login(String username, String password) throws ServletException {
/* 327 */     _getHttpServletRequest().login(username, password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logout() throws ServletException {
/* 338 */     _getHttpServletRequest().logout();
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
/*     */   public Collection<Part> getParts() throws IOException, ServletException {
/* 351 */     return _getHttpServletRequest().getParts();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Part getPart(String name) throws IOException, ServletException {
/* 361 */     return _getHttpServletRequest().getPart(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpServletRequestWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */