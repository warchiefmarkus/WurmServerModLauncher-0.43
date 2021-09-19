/*     */ package com.sun.xml.bind;
/*     */ 
/*     */ import com.sun.xml.bind.v2.TODO;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.DatatypeConverterInterface;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DatatypeConverterImpl
/*     */   implements DatatypeConverterInterface
/*     */ {
/*  74 */   public static final DatatypeConverterInterface theInstance = new DatatypeConverterImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String parseString(String lexicalXSDString) {
/*  80 */     return lexicalXSDString;
/*     */   }
/*     */   
/*     */   public BigInteger parseInteger(String lexicalXSDInteger) {
/*  84 */     return _parseInteger(lexicalXSDInteger);
/*     */   }
/*     */   
/*     */   public static BigInteger _parseInteger(CharSequence s) {
/*  88 */     return new BigInteger(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString());
/*     */   }
/*     */   
/*     */   public String printInteger(BigInteger val) {
/*  92 */     return _printInteger(val);
/*     */   }
/*     */   
/*     */   public static String _printInteger(BigInteger val) {
/*  96 */     return val.toString();
/*     */   }
/*     */   
/*     */   public int parseInt(String s) {
/* 100 */     return _parseInt(s);
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
/*     */   public static int _parseInt(CharSequence s) {
/* 114 */     int len = s.length();
/* 115 */     int sign = 1;
/*     */     
/* 117 */     int r = 0;
/*     */     
/* 119 */     for (int i = 0; i < len; i++) {
/* 120 */       char ch = s.charAt(i);
/* 121 */       if (!WhiteSpaceProcessor.isWhiteSpace(ch))
/*     */       {
/*     */         
/* 124 */         if ('0' <= ch && ch <= '9') {
/* 125 */           r = r * 10 + ch - 48;
/*     */         }
/* 127 */         else if (ch == '-') {
/* 128 */           sign = -1;
/*     */         }
/* 130 */         else if (ch != '+') {
/*     */ 
/*     */           
/* 133 */           throw new NumberFormatException("Not a number: " + s);
/*     */         }  } 
/*     */     } 
/* 136 */     return r * sign;
/*     */   }
/*     */   
/*     */   public long parseLong(String lexicalXSLong) {
/* 140 */     return _parseLong(lexicalXSLong);
/*     */   }
/*     */   
/*     */   public static long _parseLong(CharSequence s) {
/* 144 */     return Long.valueOf(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString()).longValue();
/*     */   }
/*     */   
/*     */   public short parseShort(String lexicalXSDShort) {
/* 148 */     return _parseShort(lexicalXSDShort);
/*     */   }
/*     */   
/*     */   public static short _parseShort(CharSequence s) {
/* 152 */     return (short)_parseInt(s);
/*     */   }
/*     */   
/*     */   public String printShort(short val) {
/* 156 */     return _printShort(val);
/*     */   }
/*     */   
/*     */   public static String _printShort(short val) {
/* 160 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public BigDecimal parseDecimal(String content) {
/* 164 */     return _parseDecimal(content);
/*     */   }
/*     */   public static BigDecimal _parseDecimal(CharSequence content) {
/* 167 */     content = WhiteSpaceProcessor.trim(content);
/*     */     
/* 169 */     return new BigDecimal(content.toString());
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
/*     */   public float parseFloat(String lexicalXSDFloat) {
/* 185 */     return _parseFloat(lexicalXSDFloat);
/*     */   }
/*     */   
/*     */   public static float _parseFloat(CharSequence _val) {
/* 189 */     String s = WhiteSpaceProcessor.trim(_val).toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     if (s.equals("NaN")) return Float.NaN; 
/* 206 */     if (s.equals("INF")) return Float.POSITIVE_INFINITY; 
/* 207 */     if (s.equals("-INF")) return Float.NEGATIVE_INFINITY;
/*     */     
/* 209 */     if (s.length() == 0 || !isDigitOrPeriodOrSign(s.charAt(0)) || !isDigitOrPeriodOrSign(s.charAt(s.length() - 1)))
/*     */     {
/*     */       
/* 212 */       throw new NumberFormatException();
/*     */     }
/*     */     
/* 215 */     return Float.parseFloat(s);
/*     */   }
/*     */   
/*     */   public String printFloat(float v) {
/* 219 */     return _printFloat(v);
/*     */   }
/*     */   
/*     */   public static String _printFloat(float v) {
/* 223 */     if (Float.isNaN(v)) return "NaN"; 
/* 224 */     if (v == Float.POSITIVE_INFINITY) return "INF"; 
/* 225 */     if (v == Float.NEGATIVE_INFINITY) return "-INF"; 
/* 226 */     return String.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double parseDouble(String lexicalXSDDouble) {
/* 232 */     return _parseDouble(lexicalXSDDouble);
/*     */   }
/*     */   
/*     */   public static double _parseDouble(CharSequence _val) {
/* 236 */     String val = WhiteSpaceProcessor.trim(_val).toString();
/*     */     
/* 238 */     if (val.equals("NaN")) return Double.NaN; 
/* 239 */     if (val.equals("INF")) return Double.POSITIVE_INFINITY; 
/* 240 */     if (val.equals("-INF")) return Double.NEGATIVE_INFINITY;
/*     */     
/* 242 */     if (val.length() == 0 || !isDigitOrPeriodOrSign(val.charAt(0)) || !isDigitOrPeriodOrSign(val.charAt(val.length() - 1)))
/*     */     {
/*     */       
/* 245 */       throw new NumberFormatException(val);
/*     */     }
/*     */ 
/*     */     
/* 249 */     return Double.parseDouble(val);
/*     */   }
/*     */   
/*     */   public boolean parseBoolean(String lexicalXSDBoolean) {
/* 253 */     return _parseBoolean(lexicalXSDBoolean);
/*     */   }
/*     */   public static boolean _parseBoolean(CharSequence literal) {
/*     */     char ch;
/* 257 */     int i = 0;
/* 258 */     int len = literal.length();
/*     */     
/*     */     do {
/* 261 */       ch = literal.charAt(i++);
/* 262 */     } while (WhiteSpaceProcessor.isWhiteSpace(ch) && i < len);
/*     */ 
/*     */ 
/*     */     
/* 266 */     if (ch == 't' || ch == '1') return true; 
/* 267 */     if (ch == 'f' || ch == '0') return false; 
/* 268 */     TODO.checkSpec("issue #42");
/* 269 */     return false;
/*     */   }
/*     */   
/*     */   public String printBoolean(boolean val) {
/* 273 */     return val ? "true" : "false";
/*     */   }
/*     */   public static String _printBoolean(boolean val) {
/* 276 */     return val ? "true" : "false";
/*     */   }
/*     */   
/*     */   public byte parseByte(String lexicalXSDByte) {
/* 280 */     return _parseByte(lexicalXSDByte);
/*     */   }
/*     */   
/*     */   public static byte _parseByte(CharSequence literal) {
/* 284 */     return (byte)_parseInt(literal);
/*     */   }
/*     */   
/*     */   public String printByte(byte val) {
/* 288 */     return _printByte(val);
/*     */   }
/*     */   
/*     */   public static String _printByte(byte val) {
/* 292 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public QName parseQName(String lexicalXSDQName, NamespaceContext nsc) {
/* 296 */     return _parseQName(lexicalXSDQName, nsc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static QName _parseQName(CharSequence text, NamespaceContext nsc) {
/*     */     String uri, localPart, prefix;
/* 303 */     int length = text.length();
/*     */ 
/*     */     
/* 306 */     int start = 0;
/* 307 */     while (start < length && WhiteSpaceProcessor.isWhiteSpace(text.charAt(start))) {
/* 308 */       start++;
/*     */     }
/* 310 */     int end = length;
/* 311 */     while (end > start && WhiteSpaceProcessor.isWhiteSpace(text.charAt(end - 1))) {
/* 312 */       end--;
/*     */     }
/* 314 */     if (end == start) {
/* 315 */       throw new IllegalArgumentException("input is empty");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     int idx = start + 1;
/* 324 */     while (idx < end && text.charAt(idx) != ':') {
/* 325 */       idx++;
/*     */     }
/* 327 */     if (idx == end) {
/* 328 */       uri = nsc.getNamespaceURI("");
/* 329 */       localPart = text.subSequence(start, end).toString();
/* 330 */       prefix = "";
/*     */     } else {
/*     */       
/* 333 */       prefix = text.subSequence(start, idx).toString();
/* 334 */       localPart = text.subSequence(idx + 1, end).toString();
/* 335 */       uri = nsc.getNamespaceURI(prefix);
/*     */ 
/*     */       
/* 338 */       if (uri == null || uri.length() == 0)
/*     */       {
/* 340 */         throw new IllegalArgumentException("prefix " + prefix + " is not bound to a namespace");
/*     */       }
/*     */     } 
/* 343 */     return new QName(uri, localPart, prefix);
/*     */   }
/*     */   
/*     */   public Calendar parseDateTime(String lexicalXSDDateTime) {
/* 347 */     return _parseDateTime(lexicalXSDDateTime);
/*     */   }
/*     */   
/*     */   public static GregorianCalendar _parseDateTime(CharSequence s) {
/* 351 */     String val = WhiteSpaceProcessor.trim(s).toString();
/* 352 */     return datatypeFactory.newXMLGregorianCalendar(val).toGregorianCalendar();
/*     */   }
/*     */   
/*     */   public String printDateTime(Calendar val) {
/* 356 */     return _printDateTime(val);
/*     */   }
/*     */   
/*     */   public static String _printDateTime(Calendar val) {
/* 360 */     return CalendarFormatter.doFormat("%Y-%M-%DT%h:%m:%s%z", val);
/*     */   }
/*     */   
/*     */   public byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
/* 364 */     return _parseBase64Binary(lexicalXSDBase64Binary);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] parseHexBinary(String s) {
/* 369 */     int len = s.length();
/*     */ 
/*     */     
/* 372 */     if (len % 2 != 0) {
/* 373 */       throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
/*     */     }
/* 375 */     byte[] out = new byte[len / 2];
/*     */     
/* 377 */     for (int i = 0; i < len; i += 2) {
/* 378 */       int h = hexToBin(s.charAt(i));
/* 379 */       int l = hexToBin(s.charAt(i + 1));
/* 380 */       if (h == -1 || l == -1) {
/* 381 */         throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
/*     */       }
/* 383 */       out[i / 2] = (byte)(h * 16 + l);
/*     */     } 
/*     */     
/* 386 */     return out;
/*     */   }
/*     */   
/*     */   private static int hexToBin(char ch) {
/* 390 */     if ('0' <= ch && ch <= '9') return ch - 48; 
/* 391 */     if ('A' <= ch && ch <= 'F') return ch - 65 + 10; 
/* 392 */     if ('a' <= ch && ch <= 'f') return ch - 97 + 10; 
/* 393 */     return -1;
/*     */   }
/*     */   
/* 396 */   private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
/*     */   
/*     */   public String printHexBinary(byte[] data) {
/* 399 */     StringBuilder r = new StringBuilder(data.length * 2);
/* 400 */     for (byte b : data) {
/* 401 */       r.append(hexCode[b >> 4 & 0xF]);
/* 402 */       r.append(hexCode[b & 0xF]);
/*     */     } 
/* 404 */     return r.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public long parseUnsignedInt(String lexicalXSDUnsignedInt) {
/* 409 */     return _parseLong(lexicalXSDUnsignedInt);
/*     */   }
/*     */   
/*     */   public String printUnsignedInt(long val) {
/* 413 */     return _printLong(val);
/*     */   }
/*     */   
/*     */   public int parseUnsignedShort(String lexicalXSDUnsignedShort) {
/* 417 */     return _parseInt(lexicalXSDUnsignedShort);
/*     */   }
/*     */   
/*     */   public Calendar parseTime(String lexicalXSDTime) {
/* 421 */     return datatypeFactory.newXMLGregorianCalendar(lexicalXSDTime).toGregorianCalendar();
/*     */   }
/*     */   
/*     */   public String printTime(Calendar val) {
/* 425 */     return CalendarFormatter.doFormat("%h:%m:%s%z", val);
/*     */   }
/*     */   
/*     */   public Calendar parseDate(String lexicalXSDDate) {
/* 429 */     return datatypeFactory.newXMLGregorianCalendar(lexicalXSDDate).toGregorianCalendar();
/*     */   }
/*     */ 
/*     */   
/*     */   public String printDate(Calendar val) {
/* 434 */     return CalendarFormatter.doFormat("%Y-%M-%D" + "%z", val);
/*     */   }
/*     */   
/*     */   public String parseAnySimpleType(String lexicalXSDAnySimpleType) {
/* 438 */     return lexicalXSDAnySimpleType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String printString(String val) {
/* 444 */     return val;
/*     */   }
/*     */ 
/*     */   
/*     */   public String printInt(int val) {
/* 449 */     return _printInt(val);
/*     */   }
/*     */   
/*     */   public static String _printInt(int val) {
/* 453 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public String printLong(long val) {
/* 457 */     return _printLong(val);
/*     */   }
/*     */   
/*     */   public static String _printLong(long val) {
/* 461 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public String printDecimal(BigDecimal val) {
/* 465 */     return _printDecimal(val);
/*     */   }
/*     */   
/*     */   public static String _printDecimal(BigDecimal val) {
/* 469 */     return val.toPlainString();
/*     */   }
/*     */   
/*     */   public String printDouble(double v) {
/* 473 */     return _printDouble(v);
/*     */   }
/*     */   
/*     */   public static String _printDouble(double v) {
/* 477 */     if (Double.isNaN(v)) return "NaN"; 
/* 478 */     if (v == Double.POSITIVE_INFINITY) return "INF"; 
/* 479 */     if (v == Double.NEGATIVE_INFINITY) return "-INF"; 
/* 480 */     return String.valueOf(v);
/*     */   }
/*     */   
/*     */   public String printQName(QName val, NamespaceContext nsc) {
/* 484 */     return _printQName(val, nsc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String _printQName(QName val, NamespaceContext nsc) {
/* 490 */     String qname, prefix = nsc.getPrefix(val.getNamespaceURI());
/* 491 */     String localPart = val.getLocalPart();
/*     */     
/* 493 */     if (prefix == null || prefix.length() == 0) {
/* 494 */       qname = localPart;
/*     */     } else {
/* 496 */       qname = prefix + ':' + localPart;
/*     */     } 
/*     */     
/* 499 */     return qname;
/*     */   }
/*     */   
/*     */   public String printBase64Binary(byte[] val) {
/* 503 */     return _printBase64Binary(val);
/*     */   }
/*     */   
/*     */   public String printUnsignedShort(int val) {
/* 507 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public String printAnySimpleType(String val) {
/* 511 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String installHook(String s) {
/* 521 */     DatatypeConverter.setDatatypeConverter(theInstance);
/* 522 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 531 */   private static final byte[] decodeMap = initDecodeMap();
/*     */   private static final byte PADDING = 127;
/*     */   
/*     */   private static byte[] initDecodeMap() {
/* 535 */     byte[] map = new byte[128];
/*     */     int i;
/* 537 */     for (i = 0; i < 128; ) { map[i] = -1; i++; }
/*     */     
/* 539 */     for (i = 65; i <= 90; ) { map[i] = (byte)(i - 65); i++; }
/* 540 */      for (i = 97; i <= 122; ) { map[i] = (byte)(i - 97 + 26); i++; }
/* 541 */      for (i = 48; i <= 57; ) { map[i] = (byte)(i - 48 + 52); i++; }
/* 542 */      map[43] = 62;
/* 543 */     map[47] = 63;
/* 544 */     map[61] = Byte.MAX_VALUE;
/*     */     
/* 546 */     return map;
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
/*     */   private static int guessLength(String text) {
/* 570 */     int len = text.length();
/*     */ 
/*     */     
/* 573 */     int j = len - 1;
/* 574 */     while (j >= 0) {
/* 575 */       byte code = decodeMap[text.charAt(j)];
/* 576 */       if (code == Byte.MAX_VALUE) {
/*     */         j--; continue;
/* 578 */       }  if (code == -1)
/*     */       {
/* 580 */         return text.length() / 4 * 3;
/*     */       }
/*     */     } 
/*     */     
/* 584 */     j++;
/* 585 */     int padSize = len - j;
/* 586 */     if (padSize > 2) {
/* 587 */       return text.length() / 4 * 3;
/*     */     }
/*     */ 
/*     */     
/* 591 */     return text.length() / 4 * 3 - padSize;
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
/*     */   public static byte[] _parseBase64Binary(String text) {
/* 604 */     int buflen = guessLength(text);
/* 605 */     byte[] out = new byte[buflen];
/* 606 */     int o = 0;
/*     */     
/* 608 */     int len = text.length();
/*     */ 
/*     */     
/* 611 */     byte[] quadruplet = new byte[4];
/* 612 */     int q = 0;
/*     */ 
/*     */     
/* 615 */     for (int i = 0; i < len; i++) {
/* 616 */       char ch = text.charAt(i);
/* 617 */       byte v = decodeMap[ch];
/*     */       
/* 619 */       if (v != -1) {
/* 620 */         quadruplet[q++] = v;
/*     */       }
/* 622 */       if (q == 4) {
/*     */         
/* 624 */         out[o++] = (byte)(quadruplet[0] << 2 | quadruplet[1] >> 4);
/* 625 */         if (quadruplet[2] != Byte.MAX_VALUE)
/* 626 */           out[o++] = (byte)(quadruplet[1] << 4 | quadruplet[2] >> 2); 
/* 627 */         if (quadruplet[3] != Byte.MAX_VALUE)
/* 628 */           out[o++] = (byte)(quadruplet[2] << 6 | quadruplet[3]); 
/* 629 */         q = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 633 */     if (buflen == o) {
/* 634 */       return out;
/*     */     }
/*     */     
/* 637 */     byte[] nb = new byte[o];
/* 638 */     System.arraycopy(out, 0, nb, 0, o);
/* 639 */     return nb;
/*     */   }
/*     */   
/* 642 */   private static final char[] encodeMap = initEncodeMap(); private static final DatatypeFactory datatypeFactory;
/*     */   
/*     */   private static char[] initEncodeMap() {
/* 645 */     char[] map = new char[64];
/*     */     int i;
/* 647 */     for (i = 0; i < 26; ) { map[i] = (char)(65 + i); i++; }
/* 648 */      for (i = 26; i < 52; ) { map[i] = (char)(97 + i - 26); i++; }
/* 649 */      for (i = 52; i < 62; ) { map[i] = (char)(48 + i - 52); i++; }
/* 650 */      map[62] = '+';
/* 651 */     map[63] = '/';
/*     */     
/* 653 */     return map;
/*     */   }
/*     */   
/*     */   public static char encode(int i) {
/* 657 */     return encodeMap[i & 0x3F];
/*     */   }
/*     */   
/*     */   public static byte encodeByte(int i) {
/* 661 */     return (byte)encodeMap[i & 0x3F];
/*     */   }
/*     */   
/*     */   public static String _printBase64Binary(byte[] input) {
/* 665 */     return _printBase64Binary(input, 0, input.length);
/*     */   }
/*     */   public static String _printBase64Binary(byte[] input, int offset, int len) {
/* 668 */     char[] buf = new char[(len + 2) / 3 * 4];
/* 669 */     int ptr = _printBase64Binary(input, offset, len, buf, 0);
/* 670 */     assert ptr == buf.length;
/* 671 */     return new String(buf);
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
/*     */   public static int _printBase64Binary(byte[] input, int offset, int len, char[] buf, int ptr) {
/* 684 */     for (int i = offset; i < len; i += 3) {
/* 685 */       switch (len - i) {
/*     */         case 1:
/* 687 */           buf[ptr++] = encode(input[i] >> 2);
/* 688 */           buf[ptr++] = encode((input[i] & 0x3) << 4);
/* 689 */           buf[ptr++] = '=';
/* 690 */           buf[ptr++] = '=';
/*     */           break;
/*     */         case 2:
/* 693 */           buf[ptr++] = encode(input[i] >> 2);
/* 694 */           buf[ptr++] = encode((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 697 */           buf[ptr++] = encode((input[i + 1] & 0xF) << 2);
/* 698 */           buf[ptr++] = '=';
/*     */           break;
/*     */         default:
/* 701 */           buf[ptr++] = encode(input[i] >> 2);
/* 702 */           buf[ptr++] = encode((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 705 */           buf[ptr++] = encode((input[i + 1] & 0xF) << 2 | input[i + 2] >> 6 & 0x3);
/*     */ 
/*     */           
/* 708 */           buf[ptr++] = encode(input[i + 2] & 0x3F);
/*     */           break;
/*     */       } 
/*     */     } 
/* 712 */     return ptr;
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
/*     */   public static int _printBase64Binary(byte[] input, int offset, int len, byte[] out, int ptr) {
/* 726 */     byte[] buf = out;
/* 727 */     int max = len + offset;
/* 728 */     for (int i = offset; i < max; i += 3) {
/* 729 */       switch (max - i) {
/*     */         case 1:
/* 731 */           buf[ptr++] = encodeByte(input[i] >> 2);
/* 732 */           buf[ptr++] = encodeByte((input[i] & 0x3) << 4);
/* 733 */           buf[ptr++] = 61;
/* 734 */           buf[ptr++] = 61;
/*     */           break;
/*     */         case 2:
/* 737 */           buf[ptr++] = encodeByte(input[i] >> 2);
/* 738 */           buf[ptr++] = encodeByte((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 741 */           buf[ptr++] = encodeByte((input[i + 1] & 0xF) << 2);
/* 742 */           buf[ptr++] = 61;
/*     */           break;
/*     */         default:
/* 745 */           buf[ptr++] = encodeByte(input[i] >> 2);
/* 746 */           buf[ptr++] = encodeByte((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 749 */           buf[ptr++] = encodeByte((input[i + 1] & 0xF) << 2 | input[i + 2] >> 6 & 0x3);
/*     */ 
/*     */           
/* 752 */           buf[ptr++] = encodeByte(input[i + 2] & 0x3F);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 757 */     return ptr;
/*     */   }
/*     */   
/*     */   private static CharSequence removeOptionalPlus(CharSequence s) {
/* 761 */     int len = s.length();
/*     */     
/* 763 */     if (len <= 1 || s.charAt(0) != '+') return s;
/*     */     
/* 765 */     s = s.subSequence(1, len);
/* 766 */     char ch = s.charAt(0);
/* 767 */     if ('0' <= ch && ch <= '9') return s; 
/* 768 */     if ('.' == ch) return s;
/*     */     
/* 770 */     throw new NumberFormatException();
/*     */   }
/*     */   
/*     */   private static boolean isDigitOrPeriodOrSign(char ch) {
/* 774 */     if ('0' <= ch && ch <= '9') return true; 
/* 775 */     if (ch == '+' || ch == '-' || ch == '.') return true; 
/* 776 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 783 */       datatypeFactory = DatatypeFactory.newInstance();
/* 784 */     } catch (DatatypeConfigurationException e) {
/* 785 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class CalendarFormatter
/*     */   {
/*     */     public static String doFormat(String format, Calendar cal) throws IllegalArgumentException {
/* 792 */       int fidx = 0;
/* 793 */       int flen = format.length();
/* 794 */       StringBuilder buf = new StringBuilder();
/*     */       
/* 796 */       while (fidx < flen) {
/* 797 */         char fch = format.charAt(fidx++);
/*     */         
/* 799 */         if (fch != '%') {
/* 800 */           buf.append(fch);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 805 */         switch (format.charAt(fidx++)) {
/*     */           case 'Y':
/* 807 */             formatYear(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'M':
/* 811 */             formatMonth(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'D':
/* 815 */             formatDays(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'h':
/* 819 */             formatHours(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'm':
/* 823 */             formatMinutes(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 's':
/* 827 */             formatSeconds(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'z':
/* 831 */             formatTimeZone(cal, buf);
/*     */             continue;
/*     */         } 
/*     */ 
/*     */         
/* 836 */         throw new InternalError();
/*     */       } 
/*     */ 
/*     */       
/* 840 */       return buf.toString();
/*     */     }
/*     */     
/*     */     private static void formatYear(Calendar cal, StringBuilder buf) {
/*     */       String s;
/* 845 */       int year = cal.get(1);
/*     */ 
/*     */       
/* 848 */       if (year <= 0) {
/* 849 */         s = Integer.toString(1 - year);
/*     */       } else {
/* 851 */         s = Integer.toString(year);
/*     */       } 
/* 853 */       while (s.length() < 4)
/* 854 */         s = '0' + s; 
/* 855 */       if (year <= 0) {
/* 856 */         s = '-' + s;
/*     */       }
/* 858 */       buf.append(s);
/*     */     }
/*     */     
/*     */     private static void formatMonth(Calendar cal, StringBuilder buf) {
/* 862 */       formatTwoDigits(cal.get(2) + 1, buf);
/*     */     }
/*     */     
/*     */     private static void formatDays(Calendar cal, StringBuilder buf) {
/* 866 */       formatTwoDigits(cal.get(5), buf);
/*     */     }
/*     */     
/*     */     private static void formatHours(Calendar cal, StringBuilder buf) {
/* 870 */       formatTwoDigits(cal.get(11), buf);
/*     */     }
/*     */     
/*     */     private static void formatMinutes(Calendar cal, StringBuilder buf) {
/* 874 */       formatTwoDigits(cal.get(12), buf);
/*     */     }
/*     */     
/*     */     private static void formatSeconds(Calendar cal, StringBuilder buf) {
/* 878 */       formatTwoDigits(cal.get(13), buf);
/* 879 */       if (cal.isSet(14)) {
/* 880 */         int n = cal.get(14);
/* 881 */         if (n != 0) {
/* 882 */           String ms = Integer.toString(n);
/* 883 */           while (ms.length() < 3) {
/* 884 */             ms = '0' + ms;
/*     */           }
/* 886 */           buf.append('.');
/* 887 */           buf.append(ms);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void formatTimeZone(Calendar cal, StringBuilder buf) {
/*     */       int offset;
/* 894 */       TimeZone tz = cal.getTimeZone();
/*     */       
/* 896 */       if (tz == null) {
/*     */         return;
/*     */       }
/*     */       
/* 900 */       if (tz.inDaylightTime(cal.getTime())) {
/* 901 */         offset = tz.getRawOffset() + (tz.useDaylightTime() ? 3600000 : 0);
/*     */       } else {
/* 903 */         offset = tz.getRawOffset();
/*     */       } 
/*     */       
/* 906 */       if (offset == 0) {
/* 907 */         buf.append('Z');
/*     */         
/*     */         return;
/*     */       } 
/* 911 */       if (offset >= 0) {
/* 912 */         buf.append('+');
/*     */       } else {
/* 914 */         buf.append('-');
/* 915 */         offset *= -1;
/*     */       } 
/*     */       
/* 918 */       offset /= 60000;
/*     */       
/* 920 */       formatTwoDigits(offset / 60, buf);
/* 921 */       buf.append(':');
/* 922 */       formatTwoDigits(offset % 60, buf);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static void formatTwoDigits(int n, StringBuilder buf) {
/* 928 */       if (n < 10) buf.append('0'); 
/* 929 */       buf.append(n);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\DatatypeConverterImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */