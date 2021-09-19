/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookiePathComparator;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
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
/*     */ public class RFC2109Spec
/*     */   extends CookieSpecBase
/*     */ {
/*  61 */   private static final CookiePathComparator PATH_COMPARATOR = new CookiePathComparator();
/*     */   
/*  63 */   private static final String[] DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
/*     */ 
/*     */ 
/*     */   
/*     */   private final String[] datepatterns;
/*     */ 
/*     */   
/*     */   private final boolean oneHeader;
/*     */ 
/*     */ 
/*     */   
/*     */   public RFC2109Spec(String[] datepatterns, boolean oneHeader) {
/*  75 */     if (datepatterns != null) {
/*  76 */       this.datepatterns = (String[])datepatterns.clone();
/*     */     } else {
/*  78 */       this.datepatterns = DATE_PATTERNS;
/*     */     } 
/*  80 */     this.oneHeader = oneHeader;
/*  81 */     registerAttribHandler("version", new RFC2109VersionHandler());
/*  82 */     registerAttribHandler("path", new BasicPathHandler());
/*  83 */     registerAttribHandler("domain", new RFC2109DomainHandler());
/*  84 */     registerAttribHandler("max-age", new BasicMaxAgeHandler());
/*  85 */     registerAttribHandler("secure", new BasicSecureHandler());
/*  86 */     registerAttribHandler("comment", new BasicCommentHandler());
/*  87 */     registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RFC2109Spec() {
/*  93 */     this((String[])null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*  98 */     if (header == null) {
/*  99 */       throw new IllegalArgumentException("Header may not be null");
/*     */     }
/* 101 */     if (origin == null) {
/* 102 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 104 */     if (!header.getName().equalsIgnoreCase("Set-Cookie")) {
/* 105 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 108 */     HeaderElement[] elems = header.getElements();
/* 109 */     return parse(elems, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 115 */     if (cookie == null) {
/* 116 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 118 */     String name = cookie.getName();
/* 119 */     if (name.indexOf(' ') != -1) {
/* 120 */       throw new CookieRestrictionViolationException("Cookie name may not contain blanks");
/*     */     }
/* 122 */     if (name.startsWith("$")) {
/* 123 */       throw new CookieRestrictionViolationException("Cookie name may not start with $");
/*     */     }
/* 125 */     super.validate(cookie, origin);
/*     */   }
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 129 */     if (cookies == null) {
/* 130 */       throw new IllegalArgumentException("List of cookies may not be null");
/*     */     }
/* 132 */     if (cookies.isEmpty()) {
/* 133 */       throw new IllegalArgumentException("List of cookies may not be empty");
/*     */     }
/* 135 */     if (cookies.size() > 1) {
/*     */       
/* 137 */       cookies = new ArrayList<Cookie>(cookies);
/* 138 */       Collections.sort(cookies, (Comparator<? super Cookie>)PATH_COMPARATOR);
/*     */     } 
/* 140 */     if (this.oneHeader) {
/* 141 */       return doFormatOneHeader(cookies);
/*     */     }
/* 143 */     return doFormatManyHeaders(cookies);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Header> doFormatOneHeader(List<Cookie> cookies) {
/* 148 */     int version = Integer.MAX_VALUE;
/*     */     
/* 150 */     for (Cookie cookie : cookies) {
/* 151 */       if (cookie.getVersion() < version) {
/* 152 */         version = cookie.getVersion();
/*     */       }
/*     */     } 
/* 155 */     CharArrayBuffer buffer = new CharArrayBuffer(40 * cookies.size());
/* 156 */     buffer.append("Cookie");
/* 157 */     buffer.append(": ");
/* 158 */     buffer.append("$Version=");
/* 159 */     buffer.append(Integer.toString(version));
/* 160 */     for (Cookie cooky : cookies) {
/* 161 */       buffer.append("; ");
/* 162 */       Cookie cookie = cooky;
/* 163 */       formatCookieAsVer(buffer, cookie, version);
/*     */     } 
/* 165 */     List<Header> headers = new ArrayList<Header>(1);
/* 166 */     headers.add(new BufferedHeader(buffer));
/* 167 */     return headers;
/*     */   }
/*     */   
/*     */   private List<Header> doFormatManyHeaders(List<Cookie> cookies) {
/* 171 */     List<Header> headers = new ArrayList<Header>(cookies.size());
/* 172 */     for (Cookie cookie : cookies) {
/* 173 */       int version = cookie.getVersion();
/* 174 */       CharArrayBuffer buffer = new CharArrayBuffer(40);
/* 175 */       buffer.append("Cookie: ");
/* 176 */       buffer.append("$Version=");
/* 177 */       buffer.append(Integer.toString(version));
/* 178 */       buffer.append("; ");
/* 179 */       formatCookieAsVer(buffer, cookie, version);
/* 180 */       headers.add(new BufferedHeader(buffer));
/*     */     } 
/* 182 */     return headers;
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
/*     */   protected void formatParamAsVer(CharArrayBuffer buffer, String name, String value, int version) {
/* 196 */     buffer.append(name);
/* 197 */     buffer.append("=");
/* 198 */     if (value != null) {
/* 199 */       if (version > 0) {
/* 200 */         buffer.append('"');
/* 201 */         buffer.append(value);
/* 202 */         buffer.append('"');
/*     */       } else {
/* 204 */         buffer.append(value);
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
/*     */   protected void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
/* 218 */     formatParamAsVer(buffer, cookie.getName(), cookie.getValue(), version);
/* 219 */     if (cookie.getPath() != null && 
/* 220 */       cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("path")) {
/*     */       
/* 222 */       buffer.append("; ");
/* 223 */       formatParamAsVer(buffer, "$Path", cookie.getPath(), version);
/*     */     } 
/*     */     
/* 226 */     if (cookie.getDomain() != null && 
/* 227 */       cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("domain")) {
/*     */       
/* 229 */       buffer.append("; ");
/* 230 */       formatParamAsVer(buffer, "$Domain", cookie.getDomain(), version);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 236 */     return 1;
/*     */   }
/*     */   
/*     */   public Header getVersionHeader() {
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 245 */     return "rfc2109";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2109Spec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */