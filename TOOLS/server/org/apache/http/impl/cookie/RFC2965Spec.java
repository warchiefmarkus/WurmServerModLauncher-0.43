/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class RFC2965Spec
/*     */   extends RFC2109Spec
/*     */ {
/*     */   public RFC2965Spec() {
/*  63 */     this((String[])null, false);
/*     */   }
/*     */   
/*     */   public RFC2965Spec(String[] datepatterns, boolean oneHeader) {
/*  67 */     super(datepatterns, oneHeader);
/*  68 */     registerAttribHandler("domain", new RFC2965DomainAttributeHandler());
/*  69 */     registerAttribHandler("port", new RFC2965PortAttributeHandler());
/*  70 */     registerAttribHandler("commenturl", new RFC2965CommentUrlAttributeHandler());
/*  71 */     registerAttribHandler("discard", new RFC2965DiscardAttributeHandler());
/*  72 */     registerAttribHandler("version", new RFC2965VersionAttributeHandler());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*  79 */     if (header == null) {
/*  80 */       throw new IllegalArgumentException("Header may not be null");
/*     */     }
/*  82 */     if (origin == null) {
/*  83 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/*  85 */     if (!header.getName().equalsIgnoreCase("Set-Cookie2")) {
/*  86 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/*  89 */     origin = adjustEffectiveHost(origin);
/*  90 */     HeaderElement[] elems = header.getElements();
/*  91 */     return createCookies(elems, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Cookie> parse(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
/*  98 */     origin = adjustEffectiveHost(origin);
/*  99 */     return createCookies(elems, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Cookie> createCookies(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
/* 105 */     List<Cookie> cookies = new ArrayList<Cookie>(elems.length);
/* 106 */     for (HeaderElement headerelement : elems) {
/* 107 */       String name = headerelement.getName();
/* 108 */       String value = headerelement.getValue();
/* 109 */       if (name == null || name.length() == 0) {
/* 110 */         throw new MalformedCookieException("Cookie name may not be empty");
/*     */       }
/*     */       
/* 113 */       BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
/* 114 */       cookie.setPath(getDefaultPath(origin));
/* 115 */       cookie.setDomain(getDefaultDomain(origin));
/* 116 */       cookie.setPorts(new int[] { origin.getPort() });
/*     */       
/* 118 */       NameValuePair[] attribs = headerelement.getParameters();
/*     */ 
/*     */ 
/*     */       
/* 122 */       Map<String, NameValuePair> attribmap = new HashMap<String, NameValuePair>(attribs.length);
/*     */       
/* 124 */       for (int j = attribs.length - 1; j >= 0; j--) {
/* 125 */         NameValuePair param = attribs[j];
/* 126 */         attribmap.put(param.getName().toLowerCase(Locale.ENGLISH), param);
/*     */       } 
/* 128 */       for (Map.Entry<String, NameValuePair> entry : attribmap.entrySet()) {
/* 129 */         NameValuePair attrib = entry.getValue();
/* 130 */         String s = attrib.getName().toLowerCase(Locale.ENGLISH);
/*     */         
/* 132 */         cookie.setAttribute(s, attrib.getValue());
/*     */         
/* 134 */         CookieAttributeHandler handler = findAttribHandler(s);
/* 135 */         if (handler != null) {
/* 136 */           handler.parse(cookie, attrib.getValue());
/*     */         }
/*     */       } 
/* 139 */       cookies.add(cookie);
/*     */     } 
/* 141 */     return cookies;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 147 */     if (cookie == null) {
/* 148 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 150 */     if (origin == null) {
/* 151 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 153 */     origin = adjustEffectiveHost(origin);
/* 154 */     super.validate(cookie, origin);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 159 */     if (cookie == null) {
/* 160 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 162 */     if (origin == null) {
/* 163 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 165 */     origin = adjustEffectiveHost(origin);
/* 166 */     return super.match(cookie, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
/* 175 */     super.formatCookieAsVer(buffer, cookie, version);
/*     */     
/* 177 */     if (cookie instanceof ClientCookie) {
/*     */       
/* 179 */       String s = ((ClientCookie)cookie).getAttribute("port");
/* 180 */       if (s != null) {
/* 181 */         buffer.append("; $Port");
/* 182 */         buffer.append("=\"");
/* 183 */         if (s.trim().length() > 0) {
/* 184 */           int[] ports = cookie.getPorts();
/* 185 */           if (ports != null) {
/* 186 */             for (int i = 0, len = ports.length; i < len; i++) {
/* 187 */               if (i > 0) {
/* 188 */                 buffer.append(",");
/*     */               }
/* 190 */               buffer.append(Integer.toString(ports[i]));
/*     */             } 
/*     */           }
/*     */         } 
/* 194 */         buffer.append("\"");
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static CookieOrigin adjustEffectiveHost(CookieOrigin origin) {
/* 210 */     String host = origin.getHost();
/*     */ 
/*     */ 
/*     */     
/* 214 */     boolean isLocalHost = true;
/* 215 */     for (int i = 0; i < host.length(); i++) {
/* 216 */       char ch = host.charAt(i);
/* 217 */       if (ch == '.' || ch == ':') {
/* 218 */         isLocalHost = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 222 */     if (isLocalHost) {
/* 223 */       host = host + ".local";
/* 224 */       return new CookieOrigin(host, origin.getPort(), origin.getPath(), origin.isSecure());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     return origin;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 236 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getVersionHeader() {
/* 241 */     CharArrayBuffer buffer = new CharArrayBuffer(40);
/* 242 */     buffer.append("Cookie2");
/* 243 */     buffer.append(": ");
/* 244 */     buffer.append("$Version=");
/* 245 */     buffer.append(Integer.toString(getVersion()));
/* 246 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 251 */     return "rfc2965";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2965Spec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */