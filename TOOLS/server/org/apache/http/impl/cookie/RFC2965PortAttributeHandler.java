/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.cookie.SetCookie;
/*     */ import org.apache.http.cookie.SetCookie2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class RFC2965PortAttributeHandler
/*     */   implements CookieAttributeHandler
/*     */ {
/*     */   private static int[] parsePortAttribute(String portValue) throws MalformedCookieException {
/*  66 */     StringTokenizer st = new StringTokenizer(portValue, ",");
/*  67 */     int[] ports = new int[st.countTokens()];
/*     */     try {
/*  69 */       int i = 0;
/*  70 */       while (st.hasMoreTokens()) {
/*  71 */         ports[i] = Integer.parseInt(st.nextToken().trim());
/*  72 */         if (ports[i] < 0) {
/*  73 */           throw new MalformedCookieException("Invalid Port attribute.");
/*     */         }
/*  75 */         i++;
/*     */       } 
/*  77 */     } catch (NumberFormatException e) {
/*  78 */       throw new MalformedCookieException("Invalid Port attribute: " + e.getMessage());
/*     */     } 
/*     */     
/*  81 */     return ports;
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
/*     */   private static boolean portMatch(int port, int[] ports) {
/*  94 */     boolean portInList = false;
/*  95 */     for (int i = 0, len = ports.length; i < len; i++) {
/*  96 */       if (port == ports[i]) {
/*  97 */         portInList = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 101 */     return portInList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(SetCookie cookie, String portValue) throws MalformedCookieException {
/* 109 */     if (cookie == null) {
/* 110 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 112 */     if (cookie instanceof SetCookie2) {
/* 113 */       SetCookie2 cookie2 = (SetCookie2)cookie;
/* 114 */       if (portValue != null && portValue.trim().length() > 0) {
/* 115 */         int[] ports = parsePortAttribute(portValue);
/* 116 */         cookie2.setPorts(ports);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 127 */     if (cookie == null) {
/* 128 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 130 */     if (origin == null) {
/* 131 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 133 */     int port = origin.getPort();
/* 134 */     if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("port"))
/*     */     {
/* 136 */       if (!portMatch(port, cookie.getPorts())) {
/* 137 */         throw new CookieRestrictionViolationException("Port attribute violates RFC 2965: Request port not found in cookie's port list.");
/*     */       }
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
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 150 */     if (cookie == null) {
/* 151 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 153 */     if (origin == null) {
/* 154 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 156 */     int port = origin.getPort();
/* 157 */     if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("port")) {
/*     */       
/* 159 */       if (cookie.getPorts() == null)
/*     */       {
/* 161 */         return false;
/*     */       }
/* 163 */       if (!portMatch(port, cookie.getPorts())) {
/* 164 */         return false;
/*     */       }
/*     */     } 
/* 167 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2965PortAttributeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */