/*     */ package javax.servlet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenericServlet
/*     */   implements Servlet, ServletConfig, Serializable
/*     */ {
/*     */   private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
/*  95 */   private static ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.LocalStrings");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient ServletConfig config;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInitParameter(String name) {
/* 137 */     ServletConfig sc = getServletConfig();
/* 138 */     if (sc == null) {
/* 139 */       throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
/*     */     }
/*     */ 
/*     */     
/* 143 */     return sc.getInitParameter(name);
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
/*     */   public Enumeration<String> getInitParameterNames() {
/* 163 */     ServletConfig sc = getServletConfig();
/* 164 */     if (sc == null) {
/* 165 */       throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
/*     */     }
/*     */ 
/*     */     
/* 169 */     return sc.getInitParameterNames();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletConfig getServletConfig() {
/* 180 */     return this.config;
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
/*     */   public ServletContext getServletContext() {
/* 197 */     ServletConfig sc = getServletConfig();
/* 198 */     if (sc == null) {
/* 199 */       throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
/*     */     }
/*     */ 
/*     */     
/* 203 */     return sc.getServletContext();
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
/*     */   public String getServletInfo() {
/* 219 */     return "";
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
/*     */   public void init(ServletConfig config) throws ServletException {
/* 243 */     this.config = config;
/* 244 */     init();
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
/*     */   public void init() throws ServletException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String msg) {
/* 275 */     getServletContext().log(getServletName() + ": " + msg);
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
/*     */   public void log(String message, Throwable t) {
/* 293 */     getServletContext().log(getServletName() + ": " + message, t);
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
/*     */   public abstract void service(ServletRequest paramServletRequest, ServletResponse paramServletResponse) throws ServletException, IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServletName() {
/* 329 */     ServletConfig sc = getServletConfig();
/* 330 */     if (sc == null) {
/* 331 */       throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
/*     */     }
/*     */ 
/*     */     
/* 335 */     return sc.getServletName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\GenericServlet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */