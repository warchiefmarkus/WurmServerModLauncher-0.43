/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class BasicLineFormatter
/*     */   implements LineFormatter
/*     */ {
/*  59 */   public static final BasicLineFormatter DEFAULT = new BasicLineFormatter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CharArrayBuffer initBuffer(CharArrayBuffer buffer) {
/*  75 */     if (buffer != null) {
/*  76 */       buffer.clear();
/*     */     } else {
/*  78 */       buffer = new CharArrayBuffer(64);
/*     */     } 
/*  80 */     return buffer;
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
/*     */   public static final String formatProtocolVersion(ProtocolVersion version, LineFormatter formatter) {
/*  97 */     if (formatter == null)
/*  98 */       formatter = DEFAULT; 
/*  99 */     return formatter.appendProtocolVersion(null, version).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer appendProtocolVersion(CharArrayBuffer buffer, ProtocolVersion version) {
/* 106 */     if (version == null) {
/* 107 */       throw new IllegalArgumentException("Protocol version may not be null");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 112 */     CharArrayBuffer result = buffer;
/* 113 */     int len = estimateProtocolVersionLen(version);
/* 114 */     if (result == null) {
/* 115 */       result = new CharArrayBuffer(len);
/*     */     } else {
/* 117 */       result.ensureCapacity(len);
/*     */     } 
/*     */     
/* 120 */     result.append(version.getProtocol());
/* 121 */     result.append('/');
/* 122 */     result.append(Integer.toString(version.getMajor()));
/* 123 */     result.append('.');
/* 124 */     result.append(Integer.toString(version.getMinor()));
/*     */     
/* 126 */     return result;
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
/*     */   protected int estimateProtocolVersionLen(ProtocolVersion version) {
/* 140 */     return version.getProtocol().length() + 4;
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
/*     */   public static final String formatRequestLine(RequestLine reqline, LineFormatter formatter) {
/* 156 */     if (formatter == null)
/* 157 */       formatter = DEFAULT; 
/* 158 */     return formatter.formatRequestLine(null, reqline).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatRequestLine(CharArrayBuffer buffer, RequestLine reqline) {
/* 165 */     if (reqline == null) {
/* 166 */       throw new IllegalArgumentException("Request line may not be null");
/*     */     }
/*     */ 
/*     */     
/* 170 */     CharArrayBuffer result = initBuffer(buffer);
/* 171 */     doFormatRequestLine(result, reqline);
/*     */     
/* 173 */     return result;
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
/*     */   protected void doFormatRequestLine(CharArrayBuffer buffer, RequestLine reqline) {
/* 187 */     String method = reqline.getMethod();
/* 188 */     String uri = reqline.getUri();
/*     */ 
/*     */     
/* 191 */     int len = method.length() + 1 + uri.length() + 1 + estimateProtocolVersionLen(reqline.getProtocolVersion());
/*     */     
/* 193 */     buffer.ensureCapacity(len);
/*     */     
/* 195 */     buffer.append(method);
/* 196 */     buffer.append(' ');
/* 197 */     buffer.append(uri);
/* 198 */     buffer.append(' ');
/* 199 */     appendProtocolVersion(buffer, reqline.getProtocolVersion());
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
/*     */   public static final String formatStatusLine(StatusLine statline, LineFormatter formatter) {
/* 216 */     if (formatter == null)
/* 217 */       formatter = DEFAULT; 
/* 218 */     return formatter.formatStatusLine(null, statline).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatStatusLine(CharArrayBuffer buffer, StatusLine statline) {
/* 225 */     if (statline == null) {
/* 226 */       throw new IllegalArgumentException("Status line may not be null");
/*     */     }
/*     */ 
/*     */     
/* 230 */     CharArrayBuffer result = initBuffer(buffer);
/* 231 */     doFormatStatusLine(result, statline);
/*     */     
/* 233 */     return result;
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
/*     */   protected void doFormatStatusLine(CharArrayBuffer buffer, StatusLine statline) {
/* 248 */     int len = estimateProtocolVersionLen(statline.getProtocolVersion()) + 1 + 3 + 1;
/*     */     
/* 250 */     String reason = statline.getReasonPhrase();
/* 251 */     if (reason != null) {
/* 252 */       len += reason.length();
/*     */     }
/* 254 */     buffer.ensureCapacity(len);
/*     */     
/* 256 */     appendProtocolVersion(buffer, statline.getProtocolVersion());
/* 257 */     buffer.append(' ');
/* 258 */     buffer.append(Integer.toString(statline.getStatusCode()));
/* 259 */     buffer.append(' ');
/* 260 */     if (reason != null) {
/* 261 */       buffer.append(reason);
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
/*     */   public static final String formatHeader(Header header, LineFormatter formatter) {
/* 278 */     if (formatter == null)
/* 279 */       formatter = DEFAULT; 
/* 280 */     return formatter.formatHeader(null, header).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatHeader(CharArrayBuffer buffer, Header header) {
/* 287 */     if (header == null) {
/* 288 */       throw new IllegalArgumentException("Header may not be null");
/*     */     }
/*     */     
/* 291 */     CharArrayBuffer result = null;
/*     */     
/* 293 */     if (header instanceof FormattedHeader) {
/*     */       
/* 295 */       result = ((FormattedHeader)header).getBuffer();
/*     */     } else {
/* 297 */       result = initBuffer(buffer);
/* 298 */       doFormatHeader(result, header);
/*     */     } 
/* 300 */     return result;
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
/*     */   protected void doFormatHeader(CharArrayBuffer buffer, Header header) {
/* 315 */     String name = header.getName();
/* 316 */     String value = header.getValue();
/*     */     
/* 318 */     int len = name.length() + 2;
/* 319 */     if (value != null) {
/* 320 */       len += value.length();
/*     */     }
/* 322 */     buffer.ensureCapacity(len);
/*     */     
/* 324 */     buffer.append(name);
/* 325 */     buffer.append(": ");
/* 326 */     if (value != null)
/* 327 */       buffer.append(value); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicLineFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */