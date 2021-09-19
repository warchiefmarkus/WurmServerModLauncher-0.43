/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.servlet.GenericServlet;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpServlet
/*     */   extends GenericServlet
/*     */ {
/*     */   private static final String METHOD_DELETE = "DELETE";
/*     */   private static final String METHOD_HEAD = "HEAD";
/*     */   private static final String METHOD_GET = "GET";
/*     */   private static final String METHOD_OPTIONS = "OPTIONS";
/*     */   private static final String METHOD_POST = "POST";
/*     */   private static final String METHOD_PUT = "PUT";
/*     */   private static final String METHOD_TRACE = "TRACE";
/*     */   private static final String HEADER_IFMODSINCE = "If-Modified-Since";
/*     */   private static final String HEADER_LASTMOD = "Last-Modified";
/*     */   private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
/* 135 */   private static ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 216 */     String protocol = req.getProtocol();
/* 217 */     String msg = lStrings.getString("http.method_get_not_supported");
/* 218 */     if (protocol.endsWith("1.1")) {
/* 219 */       resp.sendError(405, msg);
/*     */     } else {
/* 221 */       resp.sendError(400, msg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getLastModified(HttpServletRequest req) {
/* 250 */     return -1L;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 290 */     NoBodyResponse response = new NoBodyResponse(resp);
/*     */     
/* 292 */     doGet(req, response);
/* 293 */     response.setContentLength();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 358 */     String protocol = req.getProtocol();
/* 359 */     String msg = lStrings.getString("http.method_post_not_supported");
/* 360 */     if (protocol.endsWith("1.1")) {
/* 361 */       resp.sendError(405, msg);
/*     */     } else {
/* 363 */       resp.sendError(400, msg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 413 */     String protocol = req.getProtocol();
/* 414 */     String msg = lStrings.getString("http.method_put_not_supported");
/* 415 */     if (protocol.endsWith("1.1")) {
/* 416 */       resp.sendError(405, msg);
/*     */     } else {
/* 418 */       resp.sendError(400, msg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 460 */     String protocol = req.getProtocol();
/* 461 */     String msg = lStrings.getString("http.method_delete_not_supported");
/* 462 */     if (protocol.endsWith("1.1")) {
/* 463 */       resp.sendError(405, msg);
/*     */     } else {
/* 465 */       resp.sendError(400, msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Method[] getAllDeclaredMethods(Class<?> c) {
/* 472 */     if (c.equals(HttpServlet.class)) {
/* 473 */       return null;
/*     */     }
/*     */     
/* 476 */     Method[] parentMethods = getAllDeclaredMethods(c.getSuperclass());
/* 477 */     Method[] thisMethods = c.getDeclaredMethods();
/*     */     
/* 479 */     if (parentMethods != null && parentMethods.length > 0) {
/* 480 */       Method[] allMethods = new Method[parentMethods.length + thisMethods.length];
/*     */       
/* 482 */       System.arraycopy(parentMethods, 0, allMethods, 0, parentMethods.length);
/*     */       
/* 484 */       System.arraycopy(thisMethods, 0, allMethods, parentMethods.length, thisMethods.length);
/*     */ 
/*     */       
/* 487 */       thisMethods = allMethods;
/*     */     } 
/*     */     
/* 490 */     return thisMethods;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 528 */     Method[] methods = getAllDeclaredMethods(getClass());
/*     */     
/* 530 */     boolean ALLOW_GET = false;
/* 531 */     boolean ALLOW_HEAD = false;
/* 532 */     boolean ALLOW_POST = false;
/* 533 */     boolean ALLOW_PUT = false;
/* 534 */     boolean ALLOW_DELETE = false;
/* 535 */     boolean ALLOW_TRACE = true;
/* 536 */     boolean ALLOW_OPTIONS = true;
/*     */     
/* 538 */     for (int i = 0; i < methods.length; i++) {
/* 539 */       Method m = methods[i];
/*     */       
/* 541 */       if (m.getName().equals("doGet")) {
/* 542 */         ALLOW_GET = true;
/* 543 */         ALLOW_HEAD = true;
/*     */       } 
/* 545 */       if (m.getName().equals("doPost"))
/* 546 */         ALLOW_POST = true; 
/* 547 */       if (m.getName().equals("doPut"))
/* 548 */         ALLOW_PUT = true; 
/* 549 */       if (m.getName().equals("doDelete")) {
/* 550 */         ALLOW_DELETE = true;
/*     */       }
/*     */     } 
/*     */     
/* 554 */     String allow = null;
/* 555 */     if (ALLOW_GET)
/* 556 */       allow = "GET"; 
/* 557 */     if (ALLOW_HEAD)
/* 558 */       if (allow == null) { allow = "HEAD"; }
/* 559 */       else { allow = allow + ", HEAD"; }
/* 560 */         if (ALLOW_POST)
/* 561 */       if (allow == null) { allow = "POST"; }
/* 562 */       else { allow = allow + ", POST"; }
/* 563 */         if (ALLOW_PUT)
/* 564 */       if (allow == null) { allow = "PUT"; }
/* 565 */       else { allow = allow + ", PUT"; }
/* 566 */         if (ALLOW_DELETE)
/* 567 */       if (allow == null) { allow = "DELETE"; }
/* 568 */       else { allow = allow + ", DELETE"; }
/* 569 */         if (ALLOW_TRACE)
/* 570 */       if (allow == null) { allow = "TRACE"; }
/* 571 */       else { allow = allow + ", TRACE"; }
/* 572 */         if (ALLOW_OPTIONS)
/* 573 */       if (allow == null) { allow = "OPTIONS"; }
/* 574 */       else { allow = allow + ", OPTIONS"; }
/*     */        
/* 576 */     resp.setHeader("Allow", allow);
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
/*     */ 
/*     */   
/*     */   protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 610 */     String CRLF = "\r\n";
/* 611 */     StringBuilder buffer = (new StringBuilder("TRACE ")).append(req.getRequestURI()).append(" ").append(req.getProtocol());
/*     */ 
/*     */     
/* 614 */     Enumeration<String> reqHeaderEnum = req.getHeaderNames();
/*     */     
/* 616 */     while (reqHeaderEnum.hasMoreElements()) {
/* 617 */       String headerName = reqHeaderEnum.nextElement();
/* 618 */       buffer.append(CRLF).append(headerName).append(": ").append(req.getHeader(headerName));
/*     */     } 
/*     */ 
/*     */     
/* 622 */     buffer.append(CRLF);
/*     */     
/* 624 */     int responseLength = buffer.length();
/*     */     
/* 626 */     resp.setContentType("message/http");
/* 627 */     resp.setContentLength(responseLength);
/* 628 */     ServletOutputStream out = resp.getOutputStream();
/* 629 */     out.print(buffer.toString());
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
/*     */   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/* 661 */     String method = req.getMethod();
/*     */     
/* 663 */     if (method.equals("GET")) {
/* 664 */       long lastModified = getLastModified(req);
/* 665 */       if (lastModified == -1L) {
/*     */ 
/*     */         
/* 668 */         doGet(req, resp);
/*     */       } else {
/* 670 */         long ifModifiedSince = req.getDateHeader("If-Modified-Since");
/* 671 */         if (ifModifiedSince < lastModified) {
/*     */ 
/*     */ 
/*     */           
/* 675 */           maybeSetLastModified(resp, lastModified);
/* 676 */           doGet(req, resp);
/*     */         } else {
/* 678 */           resp.setStatus(304);
/*     */         }
/*     */       
/*     */       } 
/* 682 */     } else if (method.equals("HEAD")) {
/* 683 */       long lastModified = getLastModified(req);
/* 684 */       maybeSetLastModified(resp, lastModified);
/* 685 */       doHead(req, resp);
/*     */     }
/* 687 */     else if (method.equals("POST")) {
/* 688 */       doPost(req, resp);
/*     */     }
/* 690 */     else if (method.equals("PUT")) {
/* 691 */       doPut(req, resp);
/*     */     }
/* 693 */     else if (method.equals("DELETE")) {
/* 694 */       doDelete(req, resp);
/*     */     }
/* 696 */     else if (method.equals("OPTIONS")) {
/* 697 */       doOptions(req, resp);
/*     */     }
/* 699 */     else if (method.equals("TRACE")) {
/* 700 */       doTrace(req, resp);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 708 */       String errMsg = lStrings.getString("http.method_not_implemented");
/* 709 */       Object[] errArgs = new Object[1];
/* 710 */       errArgs[0] = method;
/* 711 */       errMsg = MessageFormat.format(errMsg, errArgs);
/*     */       
/* 713 */       resp.sendError(501, errMsg);
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
/*     */   private void maybeSetLastModified(HttpServletResponse resp, long lastModified) {
/* 727 */     if (resp.containsHeader("Last-Modified"))
/*     */       return; 
/* 729 */     if (lastModified >= 0L) {
/* 730 */       resp.setDateHeader("Last-Modified", lastModified);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
/* 762 */     if (!(req instanceof HttpServletRequest) || !(res instanceof HttpServletResponse))
/*     */     {
/* 764 */       throw new ServletException("non-HTTP request or response");
/*     */     }
/*     */     
/* 767 */     HttpServletRequest request = (HttpServletRequest)req;
/* 768 */     HttpServletResponse response = (HttpServletResponse)res;
/*     */     
/* 770 */     service(request, response);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpServlet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */