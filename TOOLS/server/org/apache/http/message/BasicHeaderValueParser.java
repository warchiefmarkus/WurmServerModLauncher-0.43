/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ParseException;
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
/*     */ @Immutable
/*     */ public class BasicHeaderValueParser
/*     */   implements HeaderValueParser
/*     */ {
/*  57 */   public static final BasicHeaderValueParser DEFAULT = new BasicHeaderValueParser();
/*     */   
/*     */   private static final char PARAM_DELIMITER = ';';
/*     */   private static final char ELEM_DELIMITER = ',';
/*  61 */   private static final char[] ALL_DELIMITERS = new char[] { ';', ',' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final HeaderElement[] parseElements(String value, HeaderValueParser parser) throws ParseException {
/*  82 */     if (value == null) {
/*  83 */       throw new IllegalArgumentException("Value to parse may not be null");
/*     */     }
/*     */ 
/*     */     
/*  87 */     if (parser == null) {
/*  88 */       parser = DEFAULT;
/*     */     }
/*  90 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/*  91 */     buffer.append(value);
/*  92 */     ParserCursor cursor = new ParserCursor(0, value.length());
/*  93 */     return parser.parseElements(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderElement[] parseElements(CharArrayBuffer buffer, ParserCursor cursor) {
/* 101 */     if (buffer == null) {
/* 102 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 104 */     if (cursor == null) {
/* 105 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*     */     
/* 108 */     List<HeaderElement> elements = new ArrayList<HeaderElement>();
/* 109 */     while (!cursor.atEnd()) {
/* 110 */       HeaderElement element = parseHeaderElement(buffer, cursor);
/* 111 */       if (element.getName().length() != 0 || element.getValue() != null) {
/* 112 */         elements.add(element);
/*     */       }
/*     */     } 
/* 115 */     return elements.<HeaderElement>toArray(new HeaderElement[elements.size()]);
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
/*     */   public static final HeaderElement parseHeaderElement(String value, HeaderValueParser parser) throws ParseException {
/* 132 */     if (value == null) {
/* 133 */       throw new IllegalArgumentException("Value to parse may not be null");
/*     */     }
/*     */ 
/*     */     
/* 137 */     if (parser == null) {
/* 138 */       parser = DEFAULT;
/*     */     }
/* 140 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 141 */     buffer.append(value);
/* 142 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 143 */     return parser.parseHeaderElement(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderElement parseHeaderElement(CharArrayBuffer buffer, ParserCursor cursor) {
/* 151 */     if (buffer == null) {
/* 152 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 154 */     if (cursor == null) {
/* 155 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*     */     
/* 158 */     NameValuePair nvp = parseNameValuePair(buffer, cursor);
/* 159 */     NameValuePair[] params = null;
/* 160 */     if (!cursor.atEnd()) {
/* 161 */       char ch = buffer.charAt(cursor.getPos() - 1);
/* 162 */       if (ch != ',') {
/* 163 */         params = parseParameters(buffer, cursor);
/*     */       }
/*     */     } 
/* 166 */     return createHeaderElement(nvp.getName(), nvp.getValue(), params);
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
/*     */   protected HeaderElement createHeaderElement(String name, String value, NameValuePair[] params) {
/* 180 */     return new BasicHeaderElement(name, value, params);
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
/*     */   public static final NameValuePair[] parseParameters(String value, HeaderValueParser parser) throws ParseException {
/* 197 */     if (value == null) {
/* 198 */       throw new IllegalArgumentException("Value to parse may not be null");
/*     */     }
/*     */ 
/*     */     
/* 202 */     if (parser == null) {
/* 203 */       parser = DEFAULT;
/*     */     }
/* 205 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 206 */     buffer.append(value);
/* 207 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 208 */     return parser.parseParameters(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameValuePair[] parseParameters(CharArrayBuffer buffer, ParserCursor cursor) {
/* 217 */     if (buffer == null) {
/* 218 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 220 */     if (cursor == null) {
/* 221 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*     */     
/* 224 */     int pos = cursor.getPos();
/* 225 */     int indexTo = cursor.getUpperBound();
/*     */     
/* 227 */     while (pos < indexTo) {
/* 228 */       char ch = buffer.charAt(pos);
/* 229 */       if (HTTP.isWhitespace(ch)) {
/* 230 */         pos++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 235 */     cursor.updatePos(pos);
/* 236 */     if (cursor.atEnd()) {
/* 237 */       return new NameValuePair[0];
/*     */     }
/*     */     
/* 240 */     List<NameValuePair> params = new ArrayList<NameValuePair>();
/* 241 */     while (!cursor.atEnd()) {
/* 242 */       NameValuePair param = parseNameValuePair(buffer, cursor);
/* 243 */       params.add(param);
/* 244 */       char ch = buffer.charAt(cursor.getPos() - 1);
/* 245 */       if (ch == ',') {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 250 */     return params.<NameValuePair>toArray(new NameValuePair[params.size()]);
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
/*     */   public static final NameValuePair parseNameValuePair(String value, HeaderValueParser parser) throws ParseException {
/* 266 */     if (value == null) {
/* 267 */       throw new IllegalArgumentException("Value to parse may not be null");
/*     */     }
/*     */ 
/*     */     
/* 271 */     if (parser == null) {
/* 272 */       parser = DEFAULT;
/*     */     }
/* 274 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 275 */     buffer.append(value);
/* 276 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 277 */     return parser.parseNameValuePair(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
/* 284 */     return parseNameValuePair(buffer, cursor, ALL_DELIMITERS);
/*     */   }
/*     */   
/*     */   private static boolean isOneOf(char ch, char[] chs) {
/* 288 */     if (chs != null) {
/* 289 */       for (int i = 0; i < chs.length; i++) {
/* 290 */         if (ch == chs[i]) {
/* 291 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 295 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor, char[] delimiters) {
/* 302 */     if (buffer == null) {
/* 303 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/* 305 */     if (cursor == null) {
/* 306 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*     */     
/* 309 */     boolean terminated = false;
/*     */     
/* 311 */     int pos = cursor.getPos();
/* 312 */     int indexFrom = cursor.getPos();
/* 313 */     int indexTo = cursor.getUpperBound();
/*     */ 
/*     */     
/* 316 */     String name = null;
/* 317 */     while (pos < indexTo) {
/* 318 */       char ch = buffer.charAt(pos);
/* 319 */       if (ch == '=') {
/*     */         break;
/*     */       }
/* 322 */       if (isOneOf(ch, delimiters)) {
/* 323 */         terminated = true;
/*     */         break;
/*     */       } 
/* 326 */       pos++;
/*     */     } 
/*     */     
/* 329 */     if (pos == indexTo) {
/* 330 */       terminated = true;
/* 331 */       name = buffer.substringTrimmed(indexFrom, indexTo);
/*     */     } else {
/* 333 */       name = buffer.substringTrimmed(indexFrom, pos);
/* 334 */       pos++;
/*     */     } 
/*     */     
/* 337 */     if (terminated) {
/* 338 */       cursor.updatePos(pos);
/* 339 */       return createNameValuePair(name, null);
/*     */     } 
/*     */ 
/*     */     
/* 343 */     String value = null;
/* 344 */     int i1 = pos;
/*     */     
/* 346 */     boolean qouted = false;
/* 347 */     boolean escaped = false;
/* 348 */     while (pos < indexTo) {
/* 349 */       char ch = buffer.charAt(pos);
/* 350 */       if (ch == '"' && !escaped) {
/* 351 */         qouted = !qouted;
/*     */       }
/* 353 */       if (!qouted && !escaped && isOneOf(ch, delimiters)) {
/* 354 */         terminated = true;
/*     */         break;
/*     */       } 
/* 357 */       if (escaped) {
/* 358 */         escaped = false;
/*     */       } else {
/* 360 */         escaped = (qouted && ch == '\\');
/*     */       } 
/* 362 */       pos++;
/*     */     } 
/*     */     
/* 365 */     int i2 = pos;
/*     */     
/* 367 */     while (i1 < i2 && HTTP.isWhitespace(buffer.charAt(i1))) {
/* 368 */       i1++;
/*     */     }
/*     */     
/* 371 */     while (i2 > i1 && HTTP.isWhitespace(buffer.charAt(i2 - 1))) {
/* 372 */       i2--;
/*     */     }
/*     */     
/* 375 */     if (i2 - i1 >= 2 && buffer.charAt(i1) == '"' && buffer.charAt(i2 - 1) == '"') {
/*     */ 
/*     */       
/* 378 */       i1++;
/* 379 */       i2--;
/*     */     } 
/* 381 */     value = buffer.substring(i1, i2);
/* 382 */     if (terminated) {
/* 383 */       pos++;
/*     */     }
/* 385 */     cursor.updatePos(pos);
/* 386 */     return createNameValuePair(name, value);
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
/*     */   protected NameValuePair createNameValuePair(String name, String value) {
/* 399 */     return new BasicNameValuePair(name, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHeaderValueParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */