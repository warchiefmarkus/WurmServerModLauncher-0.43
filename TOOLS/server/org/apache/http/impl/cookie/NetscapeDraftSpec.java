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
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class NetscapeDraftSpec
/*     */   extends CookieSpecBase
/*     */ {
/*     */   protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";
/*     */   private final String[] datepatterns;
/*     */   
/*     */   public NetscapeDraftSpec(String[] datepatterns) {
/*  65 */     if (datepatterns != null) {
/*  66 */       this.datepatterns = (String[])datepatterns.clone();
/*     */     } else {
/*  68 */       this.datepatterns = new String[] { "EEE, dd-MMM-yy HH:mm:ss z" };
/*     */     } 
/*  70 */     registerAttribHandler("path", new BasicPathHandler());
/*  71 */     registerAttribHandler("domain", new NetscapeDomainHandler());
/*  72 */     registerAttribHandler("max-age", new BasicMaxAgeHandler());
/*  73 */     registerAttribHandler("secure", new BasicSecureHandler());
/*  74 */     registerAttribHandler("comment", new BasicCommentHandler());
/*  75 */     registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NetscapeDraftSpec() {
/*  81 */     this((String[])null);
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
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*     */     CharArrayBuffer buffer;
/*     */     ParserCursor cursor;
/* 110 */     if (header == null) {
/* 111 */       throw new IllegalArgumentException("Header may not be null");
/*     */     }
/* 113 */     if (origin == null) {
/* 114 */       throw new IllegalArgumentException("Cookie origin may not be null");
/*     */     }
/* 116 */     if (!header.getName().equalsIgnoreCase("Set-Cookie")) {
/* 117 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 120 */     NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
/*     */ 
/*     */     
/* 123 */     if (header instanceof FormattedHeader) {
/* 124 */       buffer = ((FormattedHeader)header).getBuffer();
/* 125 */       cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */     }
/*     */     else {
/*     */       
/* 129 */       String s = header.getValue();
/* 130 */       if (s == null) {
/* 131 */         throw new MalformedCookieException("Header value is null");
/*     */       }
/* 133 */       buffer = new CharArrayBuffer(s.length());
/* 134 */       buffer.append(s);
/* 135 */       cursor = new ParserCursor(0, buffer.length());
/*     */     } 
/* 137 */     return parse(new HeaderElement[] { parser.parseHeader(buffer, cursor) }origin);
/*     */   }
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 141 */     if (cookies == null) {
/* 142 */       throw new IllegalArgumentException("List of cookies may not be null");
/*     */     }
/* 144 */     if (cookies.isEmpty()) {
/* 145 */       throw new IllegalArgumentException("List of cookies may not be empty");
/*     */     }
/* 147 */     CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
/* 148 */     buffer.append("Cookie");
/* 149 */     buffer.append(": ");
/* 150 */     for (int i = 0; i < cookies.size(); i++) {
/* 151 */       Cookie cookie = cookies.get(i);
/* 152 */       if (i > 0) {
/* 153 */         buffer.append("; ");
/*     */       }
/* 155 */       buffer.append(cookie.getName());
/* 156 */       String s = cookie.getValue();
/* 157 */       if (s != null) {
/* 158 */         buffer.append("=");
/* 159 */         buffer.append(s);
/*     */       } 
/*     */     } 
/* 162 */     List<Header> headers = new ArrayList<Header>(1);
/* 163 */     headers.add(new BufferedHeader(buffer));
/* 164 */     return headers;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/* 168 */     return 0;
/*     */   }
/*     */   
/*     */   public Header getVersionHeader() {
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     return "netscape";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\NetscapeDraftSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */