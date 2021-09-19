/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BestMatchSpec
/*     */   implements CookieSpec
/*     */ {
/*     */   private final String[] datepatterns;
/*     */   private final boolean oneHeader;
/*     */   private RFC2965Spec strict;
/*     */   private RFC2109Spec obsoleteStrict;
/*     */   private BrowserCompatSpec compat;
/*     */   
/*     */   public BestMatchSpec(String[] datepatterns, boolean oneHeader) {
/*  65 */     this.datepatterns = (datepatterns == null) ? null : (String[])datepatterns.clone();
/*  66 */     this.oneHeader = oneHeader;
/*     */   }
/*     */   
/*     */   public BestMatchSpec() {
/*  70 */     this(null, false);
/*     */   }
/*     */   
/*     */   private RFC2965Spec getStrict() {
/*  74 */     if (this.strict == null) {
/*  75 */       this.strict = new RFC2965Spec(this.datepatterns, this.oneHeader);
/*     */     }
/*  77 */     return this.strict;
/*     */   }
/*     */   
/*     */   private RFC2109Spec getObsoleteStrict() {
/*  81 */     if (this.obsoleteStrict == null) {
/*  82 */       this.obsoleteStrict = new RFC2109Spec(this.datepatterns, this.oneHeader);
/*     */     }
/*  84 */     return this.obsoleteStrict;
/*     */   }
/*     */   
/*     */   private BrowserCompatSpec getCompat() {
/*  88 */     if (this.compat == null) {
/*  89 */       this.compat = new BrowserCompatSpec(this.datepatterns);
/*     */     }
/*  91 */     return this.compat;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*  97 */     if (header == null) {
/*  98 */       throw new IllegalArgumentException("Header may not be null");
/*     */     }
/* 100 */     if (origin == null) {
/* 101 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 103 */     HeaderElement[] helems = header.getElements();
/* 104 */     boolean versioned = false;
/* 105 */     boolean netscape = false;
/* 106 */     for (HeaderElement helem : helems) {
/* 107 */       if (helem.getParameterByName("version") != null) {
/* 108 */         versioned = true;
/*     */       }
/* 110 */       if (helem.getParameterByName("expires") != null) {
/* 111 */         netscape = true;
/*     */       }
/*     */     } 
/* 114 */     if (netscape || !versioned) {
/*     */       CharArrayBuffer buffer;
/*     */       ParserCursor cursor;
/* 117 */       NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
/*     */ 
/*     */       
/* 120 */       if (header instanceof FormattedHeader) {
/* 121 */         buffer = ((FormattedHeader)header).getBuffer();
/* 122 */         cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */       }
/*     */       else {
/*     */         
/* 126 */         String s = header.getValue();
/* 127 */         if (s == null) {
/* 128 */           throw new MalformedCookieException("Header value is null");
/*     */         }
/* 130 */         buffer = new CharArrayBuffer(s.length());
/* 131 */         buffer.append(s);
/* 132 */         cursor = new ParserCursor(0, buffer.length());
/*     */       } 
/* 134 */       helems = new HeaderElement[] { parser.parseHeader(buffer, cursor) };
/* 135 */       return getCompat().parse(helems, origin);
/*     */     } 
/* 137 */     if ("Set-Cookie2".equals(header.getName())) {
/* 138 */       return getStrict().parse(helems, origin);
/*     */     }
/* 140 */     return getObsoleteStrict().parse(helems, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 148 */     if (cookie == null) {
/* 149 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 151 */     if (origin == null) {
/* 152 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 154 */     if (cookie.getVersion() > 0) {
/* 155 */       if (cookie instanceof org.apache.http.cookie.SetCookie2) {
/* 156 */         getStrict().validate(cookie, origin);
/*     */       } else {
/* 158 */         getObsoleteStrict().validate(cookie, origin);
/*     */       } 
/*     */     } else {
/* 161 */       getCompat().validate(cookie, origin);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 166 */     if (cookie == null) {
/* 167 */       throw new IllegalArgumentException("Cookie may not be null");
/*     */     }
/* 169 */     if (origin == null) {
/* 170 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 172 */     if (cookie.getVersion() > 0) {
/* 173 */       if (cookie instanceof org.apache.http.cookie.SetCookie2) {
/* 174 */         return getStrict().match(cookie, origin);
/*     */       }
/* 176 */       return getObsoleteStrict().match(cookie, origin);
/*     */     } 
/*     */     
/* 179 */     return getCompat().match(cookie, origin);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 184 */     if (cookies == null) {
/* 185 */       throw new IllegalArgumentException("List of cookies may not be null");
/*     */     }
/* 187 */     int version = Integer.MAX_VALUE;
/* 188 */     boolean isSetCookie2 = true;
/* 189 */     for (Cookie cookie : cookies) {
/* 190 */       if (!(cookie instanceof org.apache.http.cookie.SetCookie2)) {
/* 191 */         isSetCookie2 = false;
/*     */       }
/* 193 */       if (cookie.getVersion() < version) {
/* 194 */         version = cookie.getVersion();
/*     */       }
/*     */     } 
/* 197 */     if (version > 0) {
/* 198 */       if (isSetCookie2) {
/* 199 */         return getStrict().formatCookies(cookies);
/*     */       }
/* 201 */       return getObsoleteStrict().formatCookies(cookies);
/*     */     } 
/*     */     
/* 204 */     return getCompat().formatCookies(cookies);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 209 */     return getStrict().getVersion();
/*     */   }
/*     */   
/*     */   public Header getVersionHeader() {
/* 213 */     return getStrict().getVersionHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 218 */     return "best-match";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BestMatchSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */