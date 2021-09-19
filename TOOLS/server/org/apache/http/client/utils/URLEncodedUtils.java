/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.message.BasicHeaderValueParser;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.message.ParserCursor;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class URLEncodedUtils
/*     */ {
/*     */   public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
/*     */   private static final String PARAMETER_SEPARATOR = "&";
/*     */   private static final String NAME_VALUE_SEPARATOR = "=";
/*     */   
/*     */   public static List<NameValuePair> parse(URI uri, String encoding) {
/*  82 */     String query = uri.getRawQuery();
/*  83 */     if (query != null && query.length() > 0) {
/*  84 */       List<NameValuePair> result = new ArrayList<NameValuePair>();
/*  85 */       Scanner scanner = new Scanner(query);
/*  86 */       parse(result, scanner, encoding);
/*  87 */       return result;
/*     */     } 
/*  89 */     return Collections.emptyList();
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
/*     */   public static List<NameValuePair> parse(HttpEntity entity) throws IOException {
/* 107 */     ContentType contentType = ContentType.get(entity);
/* 108 */     if (contentType != null && contentType.getMimeType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
/* 109 */       String content = EntityUtils.toString(entity, Consts.ASCII);
/* 110 */       if (content != null && content.length() > 0) {
/* 111 */         Charset charset = contentType.getCharset();
/* 112 */         if (charset == null) {
/* 113 */           charset = HTTP.DEF_CONTENT_CHARSET;
/*     */         }
/* 115 */         return parse(content, charset);
/*     */       } 
/*     */     } 
/* 118 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEncoded(HttpEntity entity) {
/* 126 */     Header h = entity.getContentType();
/* 127 */     if (h != null) {
/* 128 */       HeaderElement[] elems = h.getElements();
/* 129 */       if (elems.length > 0) {
/* 130 */         String contentType = elems[0].getName();
/* 131 */         return contentType.equalsIgnoreCase("application/x-www-form-urlencoded");
/*     */       } 
/* 133 */       return false;
/*     */     } 
/*     */     
/* 136 */     return false;
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
/*     */   public static void parse(List<NameValuePair> parameters, Scanner scanner, String charset) {
/* 158 */     scanner.useDelimiter("&");
/* 159 */     while (scanner.hasNext()) {
/* 160 */       String name = null;
/* 161 */       String value = null;
/* 162 */       String token = scanner.next();
/* 163 */       int i = token.indexOf("=");
/* 164 */       if (i != -1) {
/* 165 */         name = decodeFormFields(token.substring(0, i).trim(), charset);
/* 166 */         value = decodeFormFields(token.substring(i + 1).trim(), charset);
/*     */       } else {
/* 168 */         name = decodeFormFields(token.trim(), charset);
/*     */       } 
/* 170 */       parameters.add(new BasicNameValuePair(name, value));
/*     */     } 
/*     */   }
/*     */   
/* 174 */   private static final char[] DELIM = new char[] { '&' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<NameValuePair> parse(String s, Charset charset) {
/* 188 */     if (s == null) {
/* 189 */       return Collections.emptyList();
/*     */     }
/* 191 */     BasicHeaderValueParser parser = BasicHeaderValueParser.DEFAULT;
/* 192 */     CharArrayBuffer buffer = new CharArrayBuffer(s.length());
/* 193 */     buffer.append(s);
/* 194 */     ParserCursor cursor = new ParserCursor(0, buffer.length());
/* 195 */     List<NameValuePair> list = new ArrayList<NameValuePair>();
/* 196 */     while (!cursor.atEnd()) {
/* 197 */       NameValuePair nvp = parser.parseNameValuePair(buffer, cursor, DELIM);
/* 198 */       if (nvp.getName().length() > 0) {
/* 199 */         list.add(new BasicNameValuePair(decodeFormFields(nvp.getName(), charset), decodeFormFields(nvp.getValue(), charset)));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 204 */     return list;
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
/*     */   public static String format(List<? extends NameValuePair> parameters, String encoding) {
/* 217 */     StringBuilder result = new StringBuilder();
/* 218 */     for (NameValuePair parameter : parameters) {
/* 219 */       String encodedName = encodeFormFields(parameter.getName(), encoding);
/* 220 */       String encodedValue = encodeFormFields(parameter.getValue(), encoding);
/* 221 */       if (result.length() > 0) {
/* 222 */         result.append("&");
/*     */       }
/* 224 */       result.append(encodedName);
/* 225 */       if (encodedValue != null) {
/* 226 */         result.append("=");
/* 227 */         result.append(encodedValue);
/*     */       } 
/*     */     } 
/* 230 */     return result.toString();
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
/*     */   public static String format(Iterable<? extends NameValuePair> parameters, Charset charset) {
/* 245 */     StringBuilder result = new StringBuilder();
/* 246 */     for (NameValuePair parameter : parameters) {
/* 247 */       String encodedName = encodeFormFields(parameter.getName(), charset);
/* 248 */       String encodedValue = encodeFormFields(parameter.getValue(), charset);
/* 249 */       if (result.length() > 0) {
/* 250 */         result.append("&");
/*     */       }
/* 252 */       result.append(encodedName);
/* 253 */       if (encodedValue != null) {
/* 254 */         result.append("=");
/* 255 */         result.append(encodedValue);
/*     */       } 
/*     */     } 
/* 258 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 267 */   private static final BitSet UNRESERVED = new BitSet(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   private static final BitSet PUNCT = new BitSet(256);
/*     */   
/* 275 */   private static final BitSet USERINFO = new BitSet(256);
/*     */   
/* 277 */   private static final BitSet PATHSAFE = new BitSet(256);
/*     */   
/* 279 */   private static final BitSet FRAGMENT = new BitSet(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   private static final BitSet RESERVED = new BitSet(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 296 */   private static final BitSet URLENCODER = new BitSet(256);
/*     */   private static final int RADIX = 16;
/*     */   
/*     */   static {
/*     */     int i;
/* 301 */     for (i = 97; i <= 122; i++) {
/* 302 */       UNRESERVED.set(i);
/*     */     }
/* 304 */     for (i = 65; i <= 90; i++) {
/* 305 */       UNRESERVED.set(i);
/*     */     }
/*     */     
/* 308 */     for (i = 48; i <= 57; i++) {
/* 309 */       UNRESERVED.set(i);
/*     */     }
/* 311 */     UNRESERVED.set(95);
/* 312 */     UNRESERVED.set(45);
/* 313 */     UNRESERVED.set(46);
/* 314 */     UNRESERVED.set(42);
/* 315 */     URLENCODER.or(UNRESERVED);
/* 316 */     UNRESERVED.set(33);
/* 317 */     UNRESERVED.set(126);
/* 318 */     UNRESERVED.set(39);
/* 319 */     UNRESERVED.set(40);
/* 320 */     UNRESERVED.set(41);
/*     */     
/* 322 */     PUNCT.set(44);
/* 323 */     PUNCT.set(59);
/* 324 */     PUNCT.set(58);
/* 325 */     PUNCT.set(36);
/* 326 */     PUNCT.set(38);
/* 327 */     PUNCT.set(43);
/* 328 */     PUNCT.set(61);
/*     */     
/* 330 */     USERINFO.or(UNRESERVED);
/* 331 */     USERINFO.or(PUNCT);
/*     */ 
/*     */     
/* 334 */     PATHSAFE.or(UNRESERVED);
/* 335 */     PATHSAFE.set(47);
/* 336 */     PATHSAFE.set(59);
/* 337 */     PATHSAFE.set(58);
/* 338 */     PATHSAFE.set(64);
/* 339 */     PATHSAFE.set(38);
/* 340 */     PATHSAFE.set(61);
/* 341 */     PATHSAFE.set(43);
/* 342 */     PATHSAFE.set(36);
/* 343 */     PATHSAFE.set(44);
/*     */     
/* 345 */     RESERVED.set(59);
/* 346 */     RESERVED.set(47);
/* 347 */     RESERVED.set(63);
/* 348 */     RESERVED.set(58);
/* 349 */     RESERVED.set(64);
/* 350 */     RESERVED.set(38);
/* 351 */     RESERVED.set(61);
/* 352 */     RESERVED.set(43);
/* 353 */     RESERVED.set(36);
/* 354 */     RESERVED.set(44);
/* 355 */     RESERVED.set(91);
/* 356 */     RESERVED.set(93);
/*     */     
/* 358 */     FRAGMENT.or(RESERVED);
/* 359 */     FRAGMENT.or(UNRESERVED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String urlencode(String content, Charset charset, BitSet safechars, boolean blankAsPlus) {
/* 369 */     if (content == null) {
/* 370 */       return null;
/*     */     }
/* 372 */     StringBuilder buf = new StringBuilder();
/* 373 */     ByteBuffer bb = charset.encode(content);
/* 374 */     while (bb.hasRemaining()) {
/* 375 */       int b = bb.get() & 0xFF;
/* 376 */       if (safechars.get(b)) {
/* 377 */         buf.append((char)b); continue;
/* 378 */       }  if (blankAsPlus && b == 32) {
/* 379 */         buf.append('+'); continue;
/*     */       } 
/* 381 */       buf.append("%");
/* 382 */       char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 383 */       char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 384 */       buf.append(hex1);
/* 385 */       buf.append(hex2);
/*     */     } 
/*     */     
/* 388 */     return buf.toString();
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
/*     */   private static String urldecode(String content, Charset charset, boolean plusAsBlank) {
/* 403 */     if (content == null) {
/* 404 */       return null;
/*     */     }
/* 406 */     ByteBuffer bb = ByteBuffer.allocate(content.length());
/* 407 */     CharBuffer cb = CharBuffer.wrap(content);
/* 408 */     while (cb.hasRemaining()) {
/* 409 */       char c = cb.get();
/* 410 */       if (c == '%' && cb.remaining() >= 2) {
/* 411 */         char uc = cb.get();
/* 412 */         char lc = cb.get();
/* 413 */         int u = Character.digit(uc, 16);
/* 414 */         int l = Character.digit(lc, 16);
/* 415 */         if (u != -1 && l != -1) {
/* 416 */           bb.put((byte)((u << 4) + l)); continue;
/*     */         } 
/* 418 */         bb.put((byte)37);
/* 419 */         bb.put((byte)uc);
/* 420 */         bb.put((byte)lc); continue;
/*     */       } 
/* 422 */       if (plusAsBlank && c == '+') {
/* 423 */         bb.put((byte)32); continue;
/*     */       } 
/* 425 */       bb.put((byte)c);
/*     */     } 
/*     */     
/* 428 */     bb.flip();
/* 429 */     return charset.decode(bb).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String decodeFormFields(String content, String charset) {
/* 440 */     if (content == null) {
/* 441 */       return null;
/*     */     }
/* 443 */     return urldecode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String decodeFormFields(String content, Charset charset) {
/* 454 */     if (content == null) {
/* 455 */       return null;
/*     */     }
/* 457 */     return urldecode(content, (charset != null) ? charset : Consts.UTF_8, true);
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
/*     */   private static String encodeFormFields(String content, String charset) {
/* 472 */     if (content == null) {
/* 473 */       return null;
/*     */     }
/* 475 */     return urlencode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, URLENCODER, true);
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
/*     */   private static String encodeFormFields(String content, Charset charset) {
/* 491 */     if (content == null) {
/* 492 */       return null;
/*     */     }
/* 494 */     return urlencode(content, (charset != null) ? charset : Consts.UTF_8, URLENCODER, true);
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
/*     */   static String encUserInfo(String content, Charset charset) {
/* 507 */     return urlencode(content, charset, USERINFO, false);
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
/*     */   static String encFragment(String content, Charset charset) {
/* 520 */     return urlencode(content, charset, FRAGMENT, false);
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
/*     */   static String encPath(String content, Charset charset) {
/* 533 */     return urlencode(content, charset, PATHSAFE, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\clien\\utils\URLEncodedUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */