/*     */ package org.seamless.http;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.logging.Logger;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestInfo
/*     */ {
/*  28 */   private static final Logger log = Logger.getLogger(RequestInfo.class.getName());
/*     */   
/*     */   public static void reportRequest(StringBuilder builder, HttpServletRequest req) {
/*  31 */     builder.append("Request: ");
/*  32 */     builder.append(req.getMethod());
/*  33 */     builder.append(' ');
/*  34 */     builder.append(req.getRequestURL());
/*  35 */     String queryString = req.getQueryString();
/*  36 */     if (queryString != null) {
/*  37 */       builder.append('?');
/*  38 */       builder.append(queryString);
/*     */     } 
/*     */     
/*  41 */     builder.append(" - ");
/*     */     
/*  43 */     String sessionId = req.getRequestedSessionId();
/*  44 */     if (sessionId != null)
/*  45 */       builder.append("\nSession ID: "); 
/*  46 */     if (sessionId == null) {
/*  47 */       builder.append("No Session");
/*  48 */     } else if (req.isRequestedSessionIdValid()) {
/*  49 */       builder.append(sessionId);
/*  50 */       builder.append(" (from ");
/*  51 */       if (req.isRequestedSessionIdFromCookie()) {
/*  52 */         builder.append("cookie)\n");
/*  53 */       } else if (req.isRequestedSessionIdFromURL()) {
/*  54 */         builder.append("url)\n");
/*     */       } else {
/*  56 */         builder.append("unknown)\n");
/*     */       } 
/*     */     } else {
/*  59 */       builder.append("Invalid Session ID\n");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void reportParameters(StringBuilder builder, HttpServletRequest req) {
/*  64 */     Enumeration<String> names = req.getParameterNames();
/*  65 */     if (names == null)
/*     */       return; 
/*  67 */     if (names.hasMoreElements()) {
/*  68 */       builder.append("Parameters:\n");
/*  69 */       while (names.hasMoreElements()) {
/*  70 */         String name = names.nextElement();
/*  71 */         String[] values = req.getParameterValues(name);
/*  72 */         if (values != null) {
/*  73 */           for (String value : values) {
/*  74 */             builder.append("    ").append(name).append(" = ").append(value).append('\n');
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void reportHeaders(StringBuilder builder, HttpServletRequest req) {
/*  82 */     Enumeration<String> names = req.getHeaderNames();
/*  83 */     if (names == null)
/*  84 */       return;  if (names.hasMoreElements()) {
/*  85 */       builder.append("Headers:\n");
/*  86 */       while (names.hasMoreElements()) {
/*  87 */         String name = names.nextElement();
/*  88 */         String value = req.getHeader(name);
/*  89 */         builder.append("    ").append(name).append(": ").append(value).append('\n');
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void reportCookies(StringBuilder builder, HttpServletRequest req) {
/*  95 */     Cookie[] cookies = req.getCookies();
/*  96 */     if (cookies == null)
/*  97 */       return;  int l = cookies.length;
/*  98 */     if (l > 0) {
/*  99 */       builder.append("Cookies:\n");
/* 100 */       for (int i = 0; i < l; i++) {
/* 101 */         Cookie cookie = cookies[i];
/* 102 */         builder.append("    ").append(cookie.getName()).append(" = ").append(cookie.getValue()).append('\n');
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void reportClient(StringBuilder builder, HttpServletRequest req) {
/* 108 */     builder.append("Remote Address: ").append(req.getRemoteAddr()).append("\n");
/* 109 */     if (!req.getRemoteAddr().equals(req.getRemoteHost()))
/* 110 */       builder.append("Remote Host: ").append(req.getRemoteHost()).append("\n"); 
/* 111 */     builder.append("Remote Port: ").append(req.getRemotePort()).append("\n");
/* 112 */     if (req.getRemoteUser() != null)
/* 113 */       builder.append("Remote User: ").append(req.getRemoteUser()).append("\n"); 
/*     */   }
/*     */   
/*     */   public static boolean isPS3Request(String userAgent, String avClientInfo) {
/* 117 */     return ((userAgent != null && userAgent.contains("PLAYSTATION 3")) || (avClientInfo != null && avClientInfo.contains("PLAYSTATION 3")));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAndroidBubbleUPnPRequest(String userAgent) {
/* 122 */     return (userAgent != null && userAgent.contains("BubbleUPnP"));
/*     */   }
/*     */   
/*     */   public static boolean isPS3Request(HttpServletRequest request) {
/* 126 */     return isPS3Request(request.getHeader("User-Agent"), request.getHeader("X-AV-Client-Info"));
/*     */   }
/*     */   
/*     */   public static boolean isJRiverRequest(HttpServletRequest request) {
/* 130 */     return isJRiverRequest(request.getHeader("User-Agent"));
/*     */   }
/*     */   
/*     */   public static boolean isJRiverRequest(String userAgent) {
/* 134 */     return (userAgent != null && (userAgent.contains("J-River") || userAgent.contains("J. River")));
/*     */   }
/*     */   
/*     */   public static boolean isWMPRequest(String userAgent) {
/* 138 */     return (userAgent != null && userAgent.contains("Windows-Media-Player") && !isJRiverRequest(userAgent));
/*     */   }
/*     */   
/*     */   public static boolean isXbox360Request(HttpServletRequest request) {
/* 142 */     return isXbox360Request(request.getHeader("User-Agent"), request.getHeader("Server"));
/*     */   }
/*     */   
/*     */   public static boolean isXbox360Request(String userAgent, String server) {
/* 146 */     return ((userAgent != null && (userAgent.contains("Xbox") || userAgent.contains("Xenon"))) || (server != null && server.contains("Xbox")));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isXbox360AlbumArtRequest(HttpServletRequest request) {
/* 151 */     return ("true".equals(request.getParameter("albumArt")) && isXbox360Request(request));
/*     */   }
/*     */   
/*     */   public static void dumpRequestHeaders(long timestamp, HttpServletRequest request) {
/* 155 */     dumpRequestHeaders(timestamp, "REQUEST HEADERS", request);
/*     */   }
/*     */   
/*     */   public static void dumpRequestString(long timestamp, HttpServletRequest request) {
/* 159 */     log.info(getRequestInfoString(timestamp, request));
/*     */   }
/*     */   
/*     */   public static void dumpRequestHeaders(long timestamp, String text, HttpServletRequest request) {
/* 163 */     log.info(text);
/* 164 */     dumpRequestString(timestamp, request);
/* 165 */     Enumeration<String> headers = request.getHeaderNames();
/* 166 */     if (headers != null) {
/* 167 */       while (headers.hasMoreElements()) {
/* 168 */         String headerName = headers.nextElement();
/* 169 */         log.info(String.format("%s: %s", new Object[] { headerName, request.getHeader(headerName) }));
/*     */       } 
/*     */     }
/* 172 */     log.info("----------------------------------------");
/*     */   }
/*     */   
/*     */   public static String getRequestInfoString(long timestamp, HttpServletRequest request) {
/* 176 */     return String.format("%s %s %s %s %s %d", new Object[] { request.getMethod(), request.getRequestURI(), request.getProtocol(), request.getParameterMap(), request.getRemoteAddr(), Long.valueOf(timestamp) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRequestFullURL(HttpServletRequest req) {
/* 187 */     String scheme = req.getScheme();
/* 188 */     String serverName = req.getServerName();
/* 189 */     int serverPort = req.getServerPort();
/* 190 */     String contextPath = req.getContextPath();
/* 191 */     String servletPath = req.getServletPath();
/* 192 */     String pathInfo = req.getPathInfo();
/* 193 */     String queryString = req.getQueryString();
/*     */ 
/*     */     
/* 196 */     StringBuffer url = new StringBuffer();
/* 197 */     url.append(scheme).append("://").append(serverName);
/*     */     
/* 199 */     if (serverPort != 80 && serverPort != 443) {
/* 200 */       url.append(":").append(serverPort);
/*     */     }
/*     */     
/* 203 */     url.append(contextPath).append(servletPath);
/*     */     
/* 205 */     if (pathInfo != null) {
/* 206 */       url.append(pathInfo);
/*     */     }
/* 208 */     if (queryString != null) {
/* 209 */       url.append("?").append(queryString);
/*     */     }
/* 211 */     return url.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\http\RequestInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */