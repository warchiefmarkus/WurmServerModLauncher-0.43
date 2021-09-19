/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
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
/*     */ @Immutable
/*     */ public class BasicHeaderValueFormatter
/*     */   implements HeaderValueFormatter
/*     */ {
/*  52 */   public static final BasicHeaderValueFormatter DEFAULT = new BasicHeaderValueFormatter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String UNSAFE_CHARS = "\"\\";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String formatElements(HeaderElement[] elems, boolean quote, HeaderValueFormatter formatter) {
/*  90 */     if (formatter == null)
/*  91 */       formatter = DEFAULT; 
/*  92 */     return formatter.formatElements(null, elems, quote).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatElements(CharArrayBuffer buffer, HeaderElement[] elems, boolean quote) {
/* 100 */     if (elems == null) {
/* 101 */       throw new IllegalArgumentException("Header element array must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 105 */     int len = estimateElementsLen(elems);
/* 106 */     if (buffer == null) {
/* 107 */       buffer = new CharArrayBuffer(len);
/*     */     } else {
/* 109 */       buffer.ensureCapacity(len);
/*     */     } 
/*     */     
/* 112 */     for (int i = 0; i < elems.length; i++) {
/* 113 */       if (i > 0) {
/* 114 */         buffer.append(", ");
/*     */       }
/* 116 */       formatHeaderElement(buffer, elems[i], quote);
/*     */     } 
/*     */     
/* 119 */     return buffer;
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
/*     */   protected int estimateElementsLen(HeaderElement[] elems) {
/* 131 */     if (elems == null || elems.length < 1) {
/* 132 */       return 0;
/*     */     }
/* 134 */     int result = (elems.length - 1) * 2;
/* 135 */     for (int i = 0; i < elems.length; i++) {
/* 136 */       result += estimateHeaderElementLen(elems[i]);
/*     */     }
/*     */     
/* 139 */     return result;
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
/*     */   public static final String formatHeaderElement(HeaderElement elem, boolean quote, HeaderValueFormatter formatter) {
/* 159 */     if (formatter == null)
/* 160 */       formatter = DEFAULT; 
/* 161 */     return formatter.formatHeaderElement(null, elem, quote).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatHeaderElement(CharArrayBuffer buffer, HeaderElement elem, boolean quote) {
/* 169 */     if (elem == null) {
/* 170 */       throw new IllegalArgumentException("Header element must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 174 */     int len = estimateHeaderElementLen(elem);
/* 175 */     if (buffer == null) {
/* 176 */       buffer = new CharArrayBuffer(len);
/*     */     } else {
/* 178 */       buffer.ensureCapacity(len);
/*     */     } 
/*     */     
/* 181 */     buffer.append(elem.getName());
/* 182 */     String value = elem.getValue();
/* 183 */     if (value != null) {
/* 184 */       buffer.append('=');
/* 185 */       doFormatValue(buffer, value, quote);
/*     */     } 
/*     */     
/* 188 */     int parcnt = elem.getParameterCount();
/* 189 */     if (parcnt > 0) {
/* 190 */       for (int i = 0; i < parcnt; i++) {
/* 191 */         buffer.append("; ");
/* 192 */         formatNameValuePair(buffer, elem.getParameter(i), quote);
/*     */       } 
/*     */     }
/*     */     
/* 196 */     return buffer;
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
/*     */   protected int estimateHeaderElementLen(HeaderElement elem) {
/* 208 */     if (elem == null) {
/* 209 */       return 0;
/*     */     }
/* 211 */     int result = elem.getName().length();
/* 212 */     String value = elem.getValue();
/* 213 */     if (value != null)
/*     */     {
/* 215 */       result += 3 + value.length();
/*     */     }
/*     */     
/* 218 */     int parcnt = elem.getParameterCount();
/* 219 */     if (parcnt > 0) {
/* 220 */       for (int i = 0; i < parcnt; i++) {
/* 221 */         result += 2 + estimateNameValuePairLen(elem.getParameter(i));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 226 */     return result;
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
/*     */   public static final String formatParameters(NameValuePair[] nvps, boolean quote, HeaderValueFormatter formatter) {
/* 247 */     if (formatter == null)
/* 248 */       formatter = DEFAULT; 
/* 249 */     return formatter.formatParameters(null, nvps, quote).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatParameters(CharArrayBuffer buffer, NameValuePair[] nvps, boolean quote) {
/* 257 */     if (nvps == null) {
/* 258 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 262 */     int len = estimateParametersLen(nvps);
/* 263 */     if (buffer == null) {
/* 264 */       buffer = new CharArrayBuffer(len);
/*     */     } else {
/* 266 */       buffer.ensureCapacity(len);
/*     */     } 
/*     */     
/* 269 */     for (int i = 0; i < nvps.length; i++) {
/* 270 */       if (i > 0) {
/* 271 */         buffer.append("; ");
/*     */       }
/* 273 */       formatNameValuePair(buffer, nvps[i], quote);
/*     */     } 
/*     */     
/* 276 */     return buffer;
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
/*     */   protected int estimateParametersLen(NameValuePair[] nvps) {
/* 288 */     if (nvps == null || nvps.length < 1) {
/* 289 */       return 0;
/*     */     }
/* 291 */     int result = (nvps.length - 1) * 2;
/* 292 */     for (int i = 0; i < nvps.length; i++) {
/* 293 */       result += estimateNameValuePairLen(nvps[i]);
/*     */     }
/*     */     
/* 296 */     return result;
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
/*     */   public static final String formatNameValuePair(NameValuePair nvp, boolean quote, HeaderValueFormatter formatter) {
/* 315 */     if (formatter == null)
/* 316 */       formatter = DEFAULT; 
/* 317 */     return formatter.formatNameValuePair(null, nvp, quote).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatNameValuePair(CharArrayBuffer buffer, NameValuePair nvp, boolean quote) {
/* 325 */     if (nvp == null) {
/* 326 */       throw new IllegalArgumentException("NameValuePair must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 330 */     int len = estimateNameValuePairLen(nvp);
/* 331 */     if (buffer == null) {
/* 332 */       buffer = new CharArrayBuffer(len);
/*     */     } else {
/* 334 */       buffer.ensureCapacity(len);
/*     */     } 
/*     */     
/* 337 */     buffer.append(nvp.getName());
/* 338 */     String value = nvp.getValue();
/* 339 */     if (value != null) {
/* 340 */       buffer.append('=');
/* 341 */       doFormatValue(buffer, value, quote);
/*     */     } 
/*     */     
/* 344 */     return buffer;
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
/*     */   protected int estimateNameValuePairLen(NameValuePair nvp) {
/* 356 */     if (nvp == null) {
/* 357 */       return 0;
/*     */     }
/* 359 */     int result = nvp.getName().length();
/* 360 */     String value = nvp.getValue();
/* 361 */     if (value != null)
/*     */     {
/* 363 */       result += 3 + value.length();
/*     */     }
/* 365 */     return result;
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
/*     */   protected void doFormatValue(CharArrayBuffer buffer, String value, boolean quote) {
/* 383 */     if (!quote) {
/* 384 */       for (int j = 0; j < value.length() && !quote; j++) {
/* 385 */         quote = isSeparator(value.charAt(j));
/*     */       }
/*     */     }
/*     */     
/* 389 */     if (quote) {
/* 390 */       buffer.append('"');
/*     */     }
/* 392 */     for (int i = 0; i < value.length(); i++) {
/* 393 */       char ch = value.charAt(i);
/* 394 */       if (isUnsafe(ch)) {
/* 395 */         buffer.append('\\');
/*     */       }
/* 397 */       buffer.append(ch);
/*     */     } 
/* 399 */     if (quote) {
/* 400 */       buffer.append('"');
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
/*     */   protected boolean isSeparator(char ch) {
/* 414 */     return (" ;,:@()<>\\\"/[]?={}\t".indexOf(ch) >= 0);
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
/*     */   protected boolean isUnsafe(char ch) {
/* 427 */     return ("\"\\".indexOf(ch) >= 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHeaderValueFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */