/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class BasicLineParser
/*     */   implements LineParser
/*     */ {
/*  67 */   public static final BasicLineParser DEFAULT = new BasicLineParser();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ProtocolVersion protocol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicLineParser(ProtocolVersion proto) {
/*     */     HttpVersion httpVersion;
/*  85 */     if (proto == null) {
/*  86 */       httpVersion = HttpVersion.HTTP_1_1;
/*     */     }
/*  88 */     this.protocol = (ProtocolVersion)httpVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicLineParser() {
/*  96 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ProtocolVersion parseProtocolVersion(String value, LineParser parser) throws ParseException {
/* 105 */     if (value == null) {
/* 106 */       throw new IllegalArgumentException("Value to parse may not be null.");
/*     */     }
/*     */ 
/*     */     
/* 110 */     if (parser == null) {
/* 111 */       parser = DEFAULT;
/*     */     }
/* 113 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 114 */     buffer.append(value);
/* 115 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 116 */     return parser.parseProtocolVersion(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion parseProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/*     */     int major, minor;
/* 125 */     if (buffer == null) {
/* 126 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 128 */     if (cursor == null) {
/* 129 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*     */     
/* 132 */     String protoname = this.protocol.getProtocol();
/* 133 */     int protolength = protoname.length();
/*     */     
/* 135 */     int indexFrom = cursor.getPos();
/* 136 */     int indexTo = cursor.getUpperBound();
/*     */     
/* 138 */     skipWhitespace(buffer, cursor);
/*     */     
/* 140 */     int i = cursor.getPos();
/*     */ 
/*     */     
/* 143 */     if (i + protolength + 4 > indexTo) {
/* 144 */       throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     boolean ok = true;
/* 151 */     for (int j = 0; ok && j < protolength; j++) {
/* 152 */       ok = (buffer.charAt(i + j) == protoname.charAt(j));
/*     */     }
/* 154 */     if (ok) {
/* 155 */       ok = (buffer.charAt(i + protolength) == '/');
/*     */     }
/* 157 */     if (!ok) {
/* 158 */       throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 163 */     i += protolength + 1;
/*     */     
/* 165 */     int period = buffer.indexOf(46, i, indexTo);
/* 166 */     if (period == -1) {
/* 167 */       throw new ParseException("Invalid protocol version number: " + buffer.substring(indexFrom, indexTo));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 173 */       major = Integer.parseInt(buffer.substringTrimmed(i, period));
/* 174 */     } catch (NumberFormatException e) {
/* 175 */       throw new ParseException("Invalid protocol major version number: " + buffer.substring(indexFrom, indexTo));
/*     */     } 
/*     */ 
/*     */     
/* 179 */     i = period + 1;
/*     */     
/* 181 */     int blank = buffer.indexOf(32, i, indexTo);
/* 182 */     if (blank == -1) {
/* 183 */       blank = indexTo;
/*     */     }
/*     */     
/*     */     try {
/* 187 */       minor = Integer.parseInt(buffer.substringTrimmed(i, blank));
/* 188 */     } catch (NumberFormatException e) {
/* 189 */       throw new ParseException("Invalid protocol minor version number: " + buffer.substring(indexFrom, indexTo));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 194 */     cursor.updatePos(blank);
/*     */     
/* 196 */     return createProtocolVersion(major, minor);
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
/*     */   protected ProtocolVersion createProtocolVersion(int major, int minor) {
/* 211 */     return this.protocol.forVersion(major, minor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) {
/* 220 */     if (buffer == null) {
/* 221 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 223 */     if (cursor == null) {
/* 224 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/* 226 */     int index = cursor.getPos();
/*     */     
/* 228 */     String protoname = this.protocol.getProtocol();
/* 229 */     int protolength = protoname.length();
/*     */     
/* 231 */     if (buffer.length() < protolength + 4) {
/* 232 */       return false;
/*     */     }
/* 234 */     if (index < 0) {
/*     */ 
/*     */       
/* 237 */       index = buffer.length() - 4 - protolength;
/* 238 */     } else if (index == 0) {
/*     */       
/* 240 */       while (index < buffer.length() && HTTP.isWhitespace(buffer.charAt(index)))
/*     */       {
/* 242 */         index++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 247 */     if (index + protolength + 4 > buffer.length()) {
/* 248 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 252 */     boolean ok = true;
/* 253 */     for (int j = 0; ok && j < protolength; j++) {
/* 254 */       ok = (buffer.charAt(index + j) == protoname.charAt(j));
/*     */     }
/* 256 */     if (ok) {
/* 257 */       ok = (buffer.charAt(index + protolength) == '/');
/*     */     }
/*     */     
/* 260 */     return ok;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final RequestLine parseRequestLine(String value, LineParser parser) throws ParseException {
/* 270 */     if (value == null) {
/* 271 */       throw new IllegalArgumentException("Value to parse may not be null.");
/*     */     }
/*     */ 
/*     */     
/* 275 */     if (parser == null) {
/* 276 */       parser = DEFAULT;
/*     */     }
/* 278 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 279 */     buffer.append(value);
/* 280 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 281 */     return parser.parseRequestLine(buffer, cursor);
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
/*     */   public RequestLine parseRequestLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/* 298 */     if (buffer == null) {
/* 299 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 301 */     if (cursor == null) {
/* 302 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*     */     
/* 305 */     int indexFrom = cursor.getPos();
/* 306 */     int indexTo = cursor.getUpperBound();
/*     */     
/*     */     try {
/* 309 */       skipWhitespace(buffer, cursor);
/* 310 */       int i = cursor.getPos();
/*     */       
/* 312 */       int blank = buffer.indexOf(32, i, indexTo);
/* 313 */       if (blank < 0) {
/* 314 */         throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
/*     */       }
/*     */       
/* 317 */       String method = buffer.substringTrimmed(i, blank);
/* 318 */       cursor.updatePos(blank);
/*     */       
/* 320 */       skipWhitespace(buffer, cursor);
/* 321 */       i = cursor.getPos();
/*     */       
/* 323 */       blank = buffer.indexOf(32, i, indexTo);
/* 324 */       if (blank < 0) {
/* 325 */         throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
/*     */       }
/*     */       
/* 328 */       String uri = buffer.substringTrimmed(i, blank);
/* 329 */       cursor.updatePos(blank);
/*     */       
/* 331 */       ProtocolVersion ver = parseProtocolVersion(buffer, cursor);
/*     */       
/* 333 */       skipWhitespace(buffer, cursor);
/* 334 */       if (!cursor.atEnd()) {
/* 335 */         throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
/*     */       }
/*     */ 
/*     */       
/* 339 */       return createRequestLine(method, uri, ver);
/* 340 */     } catch (IndexOutOfBoundsException e) {
/* 341 */       throw new ParseException("Invalid request line: " + buffer.substring(indexFrom, indexTo));
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
/*     */   protected RequestLine createRequestLine(String method, String uri, ProtocolVersion ver) {
/* 360 */     return new BasicRequestLine(method, uri, ver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final StatusLine parseStatusLine(String value, LineParser parser) throws ParseException {
/* 370 */     if (value == null) {
/* 371 */       throw new IllegalArgumentException("Value to parse may not be null.");
/*     */     }
/*     */ 
/*     */     
/* 375 */     if (parser == null) {
/* 376 */       parser = DEFAULT;
/*     */     }
/* 378 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 379 */     buffer.append(value);
/* 380 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 381 */     return parser.parseStatusLine(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusLine parseStatusLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/* 390 */     if (buffer == null) {
/* 391 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 393 */     if (cursor == null) {
/* 394 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*     */     
/* 397 */     int indexFrom = cursor.getPos();
/* 398 */     int indexTo = cursor.getUpperBound();
/*     */ 
/*     */     
/*     */     try {
/* 402 */       ProtocolVersion ver = parseProtocolVersion(buffer, cursor);
/*     */ 
/*     */       
/* 405 */       skipWhitespace(buffer, cursor);
/* 406 */       int i = cursor.getPos();
/*     */       
/* 408 */       int blank = buffer.indexOf(32, i, indexTo);
/* 409 */       if (blank < 0) {
/* 410 */         blank = indexTo;
/*     */       }
/* 412 */       int statusCode = 0;
/* 413 */       String s = buffer.substringTrimmed(i, blank);
/* 414 */       for (int j = 0; j < s.length(); j++) {
/* 415 */         if (!Character.isDigit(s.charAt(j))) {
/* 416 */           throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 422 */         statusCode = Integer.parseInt(s);
/* 423 */       } catch (NumberFormatException e) {
/* 424 */         throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 429 */       i = blank;
/* 430 */       String reasonPhrase = null;
/* 431 */       if (i < indexTo) {
/* 432 */         reasonPhrase = buffer.substringTrimmed(i, indexTo);
/*     */       } else {
/* 434 */         reasonPhrase = "";
/*     */       } 
/* 436 */       return createStatusLine(ver, statusCode, reasonPhrase);
/*     */     }
/* 438 */     catch (IndexOutOfBoundsException e) {
/* 439 */       throw new ParseException("Invalid status line: " + buffer.substring(indexFrom, indexTo));
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
/*     */   protected StatusLine createStatusLine(ProtocolVersion ver, int status, String reason) {
/* 458 */     return new BasicStatusLine(ver, status, reason);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Header parseHeader(String value, LineParser parser) throws ParseException {
/* 468 */     if (value == null) {
/* 469 */       throw new IllegalArgumentException("Value to parse may not be null");
/*     */     }
/*     */ 
/*     */     
/* 473 */     if (parser == null) {
/* 474 */       parser = DEFAULT;
/*     */     }
/* 476 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 477 */     buffer.append(value);
/* 478 */     return parser.parseHeader(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
/* 487 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void skipWhitespace(CharArrayBuffer buffer, ParserCursor cursor) {
/* 495 */     int pos = cursor.getPos();
/* 496 */     int indexTo = cursor.getUpperBound();
/* 497 */     while (pos < indexTo && HTTP.isWhitespace(buffer.charAt(pos)))
/*     */     {
/* 499 */       pos++;
/*     */     }
/* 501 */     cursor.updatePos(pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicLineParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */