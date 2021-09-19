/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.BasicHeaderElement;
/*     */ import org.apache.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.http.message.BufferedHeader;
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
/*     */ @NotThreadSafe
/*     */ public class BrowserCompatSpec
/*     */   extends CookieSpecBase
/*     */ {
/*  60 */   private static final String[] DEFAULT_DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String[] datepatterns;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrowserCompatSpec(String[] datepatterns) {
/*  82 */     if (datepatterns != null) {
/*  83 */       this.datepatterns = (String[])datepatterns.clone();
/*     */     } else {
/*  85 */       this.datepatterns = DEFAULT_DATE_PATTERNS;
/*     */     } 
/*  87 */     registerAttribHandler("path", new BasicPathHandler());
/*  88 */     registerAttribHandler("domain", new BasicDomainHandler());
/*  89 */     registerAttribHandler("max-age", new BasicMaxAgeHandler());
/*  90 */     registerAttribHandler("secure", new BasicSecureHandler());
/*  91 */     registerAttribHandler("comment", new BasicCommentHandler());
/*  92 */     registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
/*     */     
/*  94 */     registerAttribHandler("version", new BrowserCompatVersionAttributeHandler());
/*     */   }
/*     */ 
/*     */   
/*     */   public BrowserCompatSpec() {
/*  99 */     this((String[])null);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/* 104 */     if (header == null) {
/* 105 */       throw new IllegalArgumentException("Header may not be null");
/*     */     }
/* 107 */     if (origin == null) {
/* 108 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 110 */     String headername = header.getName();
/* 111 */     if (!headername.equalsIgnoreCase("Set-Cookie")) {
/* 112 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 115 */     HeaderElement[] helems = header.getElements();
/* 116 */     boolean versioned = false;
/* 117 */     boolean netscape = false;
/* 118 */     for (HeaderElement helem : helems) {
/* 119 */       if (helem.getParameterByName("version") != null) {
/* 120 */         versioned = true;
/*     */       }
/* 122 */       if (helem.getParameterByName("expires") != null) {
/* 123 */         netscape = true;
/*     */       }
/*     */     } 
/* 126 */     if (netscape || !versioned) {
/*     */       CharArrayBuffer buffer;
/*     */       ParserCursor cursor;
/* 129 */       NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
/*     */ 
/*     */       
/* 132 */       if (header instanceof FormattedHeader) {
/* 133 */         buffer = ((FormattedHeader)header).getBuffer();
/* 134 */         cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */       }
/*     */       else {
/*     */         
/* 138 */         String s = header.getValue();
/* 139 */         if (s == null) {
/* 140 */           throw new MalformedCookieException("Header value is null");
/*     */         }
/* 142 */         buffer = new CharArrayBuffer(s.length());
/* 143 */         buffer.append(s);
/* 144 */         cursor = new ParserCursor(0, buffer.length());
/*     */       } 
/* 146 */       helems = new HeaderElement[] { parser.parseHeader(buffer, cursor) };
/*     */     } 
/* 148 */     return parse(helems, origin);
/*     */   }
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 152 */     if (cookies == null) {
/* 153 */       throw new IllegalArgumentException("List of cookies may not be null");
/*     */     }
/* 155 */     if (cookies.isEmpty()) {
/* 156 */       throw new IllegalArgumentException("List of cookies may not be empty");
/*     */     }
/* 158 */     CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
/* 159 */     buffer.append("Cookie");
/* 160 */     buffer.append(": ");
/* 161 */     for (int i = 0; i < cookies.size(); i++) {
/* 162 */       Cookie cookie = cookies.get(i);
/* 163 */       if (i > 0) {
/* 164 */         buffer.append("; ");
/*     */       }
/* 166 */       String cookieName = cookie.getName();
/* 167 */       String cookieValue = cookie.getValue();
/* 168 */       if (cookie.getVersion() > 0 && (!cookieValue.startsWith("\"") || !cookieValue.endsWith("\""))) {
/*     */         
/* 170 */         BasicHeaderValueFormatter.DEFAULT.formatHeaderElement(buffer, (HeaderElement)new BasicHeaderElement(cookieName, cookieValue), false);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 176 */         buffer.append(cookieName);
/* 177 */         buffer.append("=");
/* 178 */         if (cookieValue != null) {
/* 179 */           buffer.append(cookieValue);
/*     */         }
/*     */       } 
/*     */     } 
/* 183 */     List<Header> headers = new ArrayList<Header>(1);
/* 184 */     headers.add(new BufferedHeader(buffer));
/* 185 */     return headers;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/* 189 */     return 0;
/*     */   }
/*     */   
/*     */   public Header getVersionHeader() {
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     return "compatibility";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BrowserCompatSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */