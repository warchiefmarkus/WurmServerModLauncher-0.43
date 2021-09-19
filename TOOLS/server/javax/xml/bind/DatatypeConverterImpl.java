/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
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
/*     */ final class DatatypeConverterImpl
/*     */   implements DatatypeConverterInterface
/*     */ {
/*  34 */   public static final DatatypeConverterInterface theInstance = new DatatypeConverterImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String parseString(String lexicalXSDString) {
/*  40 */     return lexicalXSDString;
/*     */   }
/*     */   
/*     */   public BigInteger parseInteger(String lexicalXSDInteger) {
/*  44 */     return _parseInteger(lexicalXSDInteger);
/*     */   }
/*     */   
/*     */   public static BigInteger _parseInteger(CharSequence s) {
/*  48 */     return new BigInteger(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString());
/*     */   }
/*     */   
/*     */   public String printInteger(BigInteger val) {
/*  52 */     return _printInteger(val);
/*     */   }
/*     */   
/*     */   public static String _printInteger(BigInteger val) {
/*  56 */     return val.toString();
/*     */   }
/*     */   
/*     */   public int parseInt(String s) {
/*  60 */     return _parseInt(s);
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
/*  74 */     int len = s.length();
/*  75 */     int sign = 1;
/*     */     
/*  77 */     int r = 0;
/*     */     
/*  79 */     for (int i = 0; i < len; i++) {
/*  80 */       char ch = s.charAt(i);
/*  81 */       if (!WhiteSpaceProcessor.isWhiteSpace(ch))
/*     */       {
/*     */         
/*  84 */         if ('0' <= ch && ch <= '9') {
/*  85 */           r = r * 10 + ch - 48;
/*     */         }
/*  87 */         else if (ch == '-') {
/*  88 */           sign = -1;
/*     */         }
/*  90 */         else if (ch != '+') {
/*     */ 
/*     */           
/*  93 */           throw new NumberFormatException("Not a number: " + s);
/*     */         }  } 
/*     */     } 
/*  96 */     return r * sign;
/*     */   }
/*     */   
/*     */   public long parseLong(String lexicalXSLong) {
/* 100 */     return _parseLong(lexicalXSLong);
/*     */   }
/*     */   
/*     */   public static long _parseLong(CharSequence s) {
/* 104 */     return Long.valueOf(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString()).longValue();
/*     */   }
/*     */   
/*     */   public short parseShort(String lexicalXSDShort) {
/* 108 */     return _parseShort(lexicalXSDShort);
/*     */   }
/*     */   
/*     */   public static short _parseShort(CharSequence s) {
/* 112 */     return (short)_parseInt(s);
/*     */   }
/*     */   
/*     */   public String printShort(short val) {
/* 116 */     return _printShort(val);
/*     */   }
/*     */   
/*     */   public static String _printShort(short val) {
/* 120 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public BigDecimal parseDecimal(String content) {
/* 124 */     return _parseDecimal(content);
/*     */   }
/*     */   public static BigDecimal _parseDecimal(CharSequence content) {
/* 127 */     content = WhiteSpaceProcessor.trim(content);
/*     */     
/* 129 */     return new BigDecimal(content.toString());
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
/* 145 */     return _parseFloat(lexicalXSDFloat);
/*     */   }
/*     */   
/*     */   public static float _parseFloat(CharSequence _val) {
/* 149 */     String s = WhiteSpaceProcessor.trim(_val).toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     if (s.equals("NaN")) return Float.NaN; 
/* 166 */     if (s.equals("INF")) return Float.POSITIVE_INFINITY; 
/* 167 */     if (s.equals("-INF")) return Float.NEGATIVE_INFINITY;
/*     */     
/* 169 */     if (s.length() == 0 || !isDigitOrPeriodOrSign(s.charAt(0)) || !isDigitOrPeriodOrSign(s.charAt(s.length() - 1)))
/*     */     {
/*     */       
/* 172 */       throw new NumberFormatException();
/*     */     }
/*     */     
/* 175 */     return Float.parseFloat(s);
/*     */   }
/*     */   
/*     */   public String printFloat(float v) {
/* 179 */     return _printFloat(v);
/*     */   }
/*     */   
/*     */   public static String _printFloat(float v) {
/* 183 */     if (v == Float.NaN) return "NaN"; 
/* 184 */     if (v == Float.POSITIVE_INFINITY) return "INF"; 
/* 185 */     if (v == Float.NEGATIVE_INFINITY) return "-INF"; 
/* 186 */     return String.valueOf(v);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double parseDouble(String lexicalXSDDouble) {
/* 192 */     return _parseDouble(lexicalXSDDouble);
/*     */   }
/*     */   
/*     */   public static double _parseDouble(CharSequence _val) {
/* 196 */     String val = WhiteSpaceProcessor.trim(_val).toString();
/*     */     
/* 198 */     if (val.equals("NaN")) return Double.NaN; 
/* 199 */     if (val.equals("INF")) return Double.POSITIVE_INFINITY; 
/* 200 */     if (val.equals("-INF")) return Double.NEGATIVE_INFINITY;
/*     */     
/* 202 */     if (val.length() == 0 || !isDigitOrPeriodOrSign(val.charAt(0)) || !isDigitOrPeriodOrSign(val.charAt(val.length() - 1)))
/*     */     {
/*     */       
/* 205 */       throw new NumberFormatException(val);
/*     */     }
/*     */ 
/*     */     
/* 209 */     return Double.parseDouble(val);
/*     */   }
/*     */   
/*     */   public boolean parseBoolean(String lexicalXSDBoolean) {
/* 213 */     return _parseBoolean(lexicalXSDBoolean);
/*     */   }
/*     */   public static boolean _parseBoolean(CharSequence literal) {
/*     */     char ch;
/* 217 */     int i = 0;
/* 218 */     int len = literal.length();
/*     */     
/*     */     do {
/* 221 */       ch = literal.charAt(i++);
/* 222 */     } while (WhiteSpaceProcessor.isWhiteSpace(ch) && i < len);
/*     */ 
/*     */ 
/*     */     
/* 226 */     if (ch == 't' || ch == '1') return true; 
/* 227 */     if (ch == 'f' || ch == '0') return false; 
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   public String printBoolean(boolean val) {
/* 232 */     return val ? "true" : "false";
/*     */   }
/*     */   public static String _printBoolean(boolean val) {
/* 235 */     return val ? "true" : "false";
/*     */   }
/*     */   
/*     */   public byte parseByte(String lexicalXSDByte) {
/* 239 */     return _parseByte(lexicalXSDByte);
/*     */   }
/*     */   
/*     */   public static byte _parseByte(CharSequence literal) {
/* 243 */     return (byte)_parseInt(literal);
/*     */   }
/*     */   
/*     */   public String printByte(byte val) {
/* 247 */     return _printByte(val);
/*     */   }
/*     */   
/*     */   public static String _printByte(byte val) {
/* 251 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public QName parseQName(String lexicalXSDQName, NamespaceContext nsc) {
/* 255 */     return _parseQName(lexicalXSDQName, nsc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static QName _parseQName(CharSequence text, NamespaceContext nsc) {
/*     */     String uri, localPart, prefix;
/* 262 */     int length = text.length();
/*     */ 
/*     */     
/* 265 */     int start = 0;
/* 266 */     while (start < length && WhiteSpaceProcessor.isWhiteSpace(text.charAt(start))) {
/* 267 */       start++;
/*     */     }
/* 269 */     int end = length;
/* 270 */     while (end > start && WhiteSpaceProcessor.isWhiteSpace(text.charAt(end - 1))) {
/* 271 */       end--;
/*     */     }
/* 273 */     if (end == start) {
/* 274 */       throw new IllegalArgumentException("input is empty");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     int idx = start + 1;
/* 283 */     while (idx < end && text.charAt(idx) != ':') {
/* 284 */       idx++;
/*     */     }
/* 286 */     if (idx == end) {
/* 287 */       uri = nsc.getNamespaceURI("");
/* 288 */       localPart = text.subSequence(start, end).toString();
/* 289 */       prefix = "";
/*     */     } else {
/*     */       
/* 292 */       prefix = text.subSequence(start, idx).toString();
/* 293 */       localPart = text.subSequence(idx + 1, end).toString();
/* 294 */       uri = nsc.getNamespaceURI(prefix);
/*     */ 
/*     */       
/* 297 */       if (uri == null || uri.length() == 0)
/*     */       {
/* 299 */         throw new IllegalArgumentException("prefix " + prefix + " is not bound to a namespace");
/*     */       }
/*     */     } 
/* 302 */     return new QName(uri, localPart, prefix);
/*     */   }
/*     */   
/*     */   public Calendar parseDateTime(String lexicalXSDDateTime) {
/* 306 */     return _parseDateTime(lexicalXSDDateTime);
/*     */   }
/*     */   
/*     */   public static GregorianCalendar _parseDateTime(CharSequence s) {
/* 310 */     String val = WhiteSpaceProcessor.trim(s).toString();
/* 311 */     return datatypeFactory.newXMLGregorianCalendar(val).toGregorianCalendar();
/*     */   }
/*     */   
/*     */   public String printDateTime(Calendar val) {
/* 315 */     return _printDateTime(val);
/*     */   }
/*     */   
/*     */   public static String _printDateTime(Calendar val) {
/* 319 */     return CalendarFormatter.doFormat("%Y-%M-%DT%h:%m:%s%z", val);
/*     */   }
/*     */   
/*     */   public byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
/* 323 */     return _parseBase64Binary(lexicalXSDBase64Binary);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] parseHexBinary(String s) {
/* 328 */     int len = s.length();
/*     */ 
/*     */     
/* 331 */     if (len % 2 != 0) {
/* 332 */       throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
/*     */     }
/* 334 */     byte[] out = new byte[len / 2];
/*     */     
/* 336 */     for (int i = 0; i < len; i += 2) {
/* 337 */       int h = hexToBin(s.charAt(i));
/* 338 */       int l = hexToBin(s.charAt(i + 1));
/* 339 */       if (h == -1 || l == -1) {
/* 340 */         throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
/*     */       }
/* 342 */       out[i / 2] = (byte)(h * 16 + l);
/*     */     } 
/*     */     
/* 345 */     return out;
/*     */   }
/*     */   
/*     */   private static int hexToBin(char ch) {
/* 349 */     if ('0' <= ch && ch <= '9') return ch - 48; 
/* 350 */     if ('A' <= ch && ch <= 'F') return ch - 65 + 10; 
/* 351 */     if ('a' <= ch && ch <= 'f') return ch - 97 + 10; 
/* 352 */     return -1;
/*     */   }
/*     */   
/* 355 */   private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
/*     */   
/*     */   public String printHexBinary(byte[] data) {
/* 358 */     StringBuilder r = new StringBuilder(data.length * 2);
/* 359 */     for (byte b : data) {
/* 360 */       r.append(hexCode[b >> 4 & 0xF]);
/* 361 */       r.append(hexCode[b & 0xF]);
/*     */     } 
/* 363 */     return r.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public long parseUnsignedInt(String lexicalXSDUnsignedInt) {
/* 368 */     return _parseLong(lexicalXSDUnsignedInt);
/*     */   }
/*     */   
/*     */   public String printUnsignedInt(long val) {
/* 372 */     return _printLong(val);
/*     */   }
/*     */   
/*     */   public int parseUnsignedShort(String lexicalXSDUnsignedShort) {
/* 376 */     return _parseInt(lexicalXSDUnsignedShort);
/*     */   }
/*     */   
/*     */   public Calendar parseTime(String lexicalXSDTime) {
/* 380 */     return datatypeFactory.newXMLGregorianCalendar(lexicalXSDTime).toGregorianCalendar();
/*     */   }
/*     */   
/*     */   public String printTime(Calendar val) {
/* 384 */     return CalendarFormatter.doFormat("%h:%m:%s%z", val);
/*     */   }
/*     */   
/*     */   public Calendar parseDate(String lexicalXSDDate) {
/* 388 */     return datatypeFactory.newXMLGregorianCalendar(lexicalXSDDate).toGregorianCalendar();
/*     */   }
/*     */ 
/*     */   
/*     */   public String printDate(Calendar val) {
/* 393 */     return CalendarFormatter.doFormat("%Y-%M-%D" + "%z", val);
/*     */   }
/*     */   
/*     */   public String parseAnySimpleType(String lexicalXSDAnySimpleType) {
/* 397 */     return lexicalXSDAnySimpleType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String printString(String val) {
/* 403 */     return val;
/*     */   }
/*     */ 
/*     */   
/*     */   public String printInt(int val) {
/* 408 */     return _printInt(val);
/*     */   }
/*     */   
/*     */   public static String _printInt(int val) {
/* 412 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public String printLong(long val) {
/* 416 */     return _printLong(val);
/*     */   }
/*     */   
/*     */   public static String _printLong(long val) {
/* 420 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public String printDecimal(BigDecimal val) {
/* 424 */     return _printDecimal(val);
/*     */   }
/*     */   
/*     */   public static String _printDecimal(BigDecimal val) {
/* 428 */     return val.toPlainString();
/*     */   }
/*     */   
/*     */   public String printDouble(double v) {
/* 432 */     return _printDouble(v);
/*     */   }
/*     */   
/*     */   public static String _printDouble(double v) {
/* 436 */     if (v == Double.NaN) return "NaN"; 
/* 437 */     if (v == Double.POSITIVE_INFINITY) return "INF"; 
/* 438 */     if (v == Double.NEGATIVE_INFINITY) return "-INF"; 
/* 439 */     return String.valueOf(v);
/*     */   }
/*     */   
/*     */   public String printQName(QName val, NamespaceContext nsc) {
/* 443 */     return _printQName(val, nsc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String _printQName(QName val, NamespaceContext nsc) {
/* 449 */     String qname, prefix = nsc.getPrefix(val.getNamespaceURI());
/* 450 */     String localPart = val.getLocalPart();
/*     */     
/* 452 */     if (prefix == null || prefix.length() == 0) {
/* 453 */       qname = localPart;
/*     */     } else {
/* 455 */       qname = prefix + ':' + localPart;
/*     */     } 
/*     */     
/* 458 */     return qname;
/*     */   }
/*     */   
/*     */   public String printBase64Binary(byte[] val) {
/* 462 */     return _printBase64Binary(val);
/*     */   }
/*     */   
/*     */   public String printUnsignedShort(int val) {
/* 466 */     return String.valueOf(val);
/*     */   }
/*     */   
/*     */   public String printAnySimpleType(String val) {
/* 470 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String installHook(String s) {
/* 480 */     DatatypeConverter.setDatatypeConverter(theInstance);
/* 481 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 490 */   private static final byte[] decodeMap = initDecodeMap();
/*     */   private static final byte PADDING = 127;
/*     */   
/*     */   private static byte[] initDecodeMap() {
/* 494 */     byte[] map = new byte[128];
/*     */     int i;
/* 496 */     for (i = 0; i < 128; ) { map[i] = -1; i++; }
/*     */     
/* 498 */     for (i = 65; i <= 90; ) { map[i] = (byte)(i - 65); i++; }
/* 499 */      for (i = 97; i <= 122; ) { map[i] = (byte)(i - 97 + 26); i++; }
/* 500 */      for (i = 48; i <= 57; ) { map[i] = (byte)(i - 48 + 52); i++; }
/* 501 */      map[43] = 62;
/* 502 */     map[47] = 63;
/* 503 */     map[61] = Byte.MAX_VALUE;
/*     */     
/* 505 */     return map;
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
/* 529 */     int len = text.length();
/*     */ 
/*     */     
/* 532 */     int j = len - 1;
/* 533 */     while (j >= 0) {
/* 534 */       byte code = decodeMap[text.charAt(j)];
/* 535 */       if (code == Byte.MAX_VALUE) {
/*     */         j--; continue;
/* 537 */       }  if (code == -1)
/*     */       {
/* 539 */         return text.length() / 4 * 3;
/*     */       }
/*     */     } 
/*     */     
/* 543 */     j++;
/* 544 */     int padSize = len - j;
/* 545 */     if (padSize > 2) {
/* 546 */       return text.length() / 4 * 3;
/*     */     }
/*     */ 
/*     */     
/* 550 */     return text.length() / 4 * 3 - padSize;
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
/* 563 */     int buflen = guessLength(text);
/* 564 */     byte[] out = new byte[buflen];
/* 565 */     int o = 0;
/*     */     
/* 567 */     int len = text.length();
/*     */ 
/*     */     
/* 570 */     byte[] quadruplet = new byte[4];
/* 571 */     int q = 0;
/*     */ 
/*     */     
/* 574 */     for (int i = 0; i < len; i++) {
/* 575 */       char ch = text.charAt(i);
/* 576 */       byte v = decodeMap[ch];
/*     */       
/* 578 */       if (v != -1) {
/* 579 */         quadruplet[q++] = v;
/*     */       }
/* 581 */       if (q == 4) {
/*     */         
/* 583 */         out[o++] = (byte)(quadruplet[0] << 2 | quadruplet[1] >> 4);
/* 584 */         if (quadruplet[2] != Byte.MAX_VALUE)
/* 585 */           out[o++] = (byte)(quadruplet[1] << 4 | quadruplet[2] >> 2); 
/* 586 */         if (quadruplet[3] != Byte.MAX_VALUE)
/* 587 */           out[o++] = (byte)(quadruplet[2] << 6 | quadruplet[3]); 
/* 588 */         q = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 592 */     if (buflen == o) {
/* 593 */       return out;
/*     */     }
/*     */     
/* 596 */     byte[] nb = new byte[o];
/* 597 */     System.arraycopy(out, 0, nb, 0, o);
/* 598 */     return nb;
/*     */   }
/*     */   
/* 601 */   private static final char[] encodeMap = initEncodeMap(); private static final DatatypeFactory datatypeFactory;
/*     */   
/*     */   private static char[] initEncodeMap() {
/* 604 */     char[] map = new char[64];
/*     */     int i;
/* 606 */     for (i = 0; i < 26; ) { map[i] = (char)(65 + i); i++; }
/* 607 */      for (i = 26; i < 52; ) { map[i] = (char)(97 + i - 26); i++; }
/* 608 */      for (i = 52; i < 62; ) { map[i] = (char)(48 + i - 52); i++; }
/* 609 */      map[62] = '+';
/* 610 */     map[63] = '/';
/*     */     
/* 612 */     return map;
/*     */   }
/*     */   
/*     */   public static char encode(int i) {
/* 616 */     return encodeMap[i & 0x3F];
/*     */   }
/*     */   
/*     */   public static byte encodeByte(int i) {
/* 620 */     return (byte)encodeMap[i & 0x3F];
/*     */   }
/*     */   
/*     */   public static String _printBase64Binary(byte[] input) {
/* 624 */     return _printBase64Binary(input, 0, input.length);
/*     */   }
/*     */   public static String _printBase64Binary(byte[] input, int offset, int len) {
/* 627 */     char[] buf = new char[(len + 2) / 3 * 4];
/* 628 */     int ptr = _printBase64Binary(input, offset, len, buf, 0);
/* 629 */     assert ptr == buf.length;
/* 630 */     return new String(buf);
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
/* 643 */     for (int i = offset; i < len; i += 3) {
/* 644 */       switch (len - i) {
/*     */         case 1:
/* 646 */           buf[ptr++] = encode(input[i] >> 2);
/* 647 */           buf[ptr++] = encode((input[i] & 0x3) << 4);
/* 648 */           buf[ptr++] = '=';
/* 649 */           buf[ptr++] = '=';
/*     */           break;
/*     */         case 2:
/* 652 */           buf[ptr++] = encode(input[i] >> 2);
/* 653 */           buf[ptr++] = encode((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 656 */           buf[ptr++] = encode((input[i + 1] & 0xF) << 2);
/* 657 */           buf[ptr++] = '=';
/*     */           break;
/*     */         default:
/* 660 */           buf[ptr++] = encode(input[i] >> 2);
/* 661 */           buf[ptr++] = encode((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 664 */           buf[ptr++] = encode((input[i + 1] & 0xF) << 2 | input[i + 2] >> 6 & 0x3);
/*     */ 
/*     */           
/* 667 */           buf[ptr++] = encode(input[i + 2] & 0x3F);
/*     */           break;
/*     */       } 
/*     */     } 
/* 671 */     return ptr;
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
/* 685 */     byte[] buf = out;
/* 686 */     int max = len + offset;
/* 687 */     for (int i = offset; i < max; i += 3) {
/* 688 */       switch (max - i) {
/*     */         case 1:
/* 690 */           buf[ptr++] = encodeByte(input[i] >> 2);
/* 691 */           buf[ptr++] = encodeByte((input[i] & 0x3) << 4);
/* 692 */           buf[ptr++] = 61;
/* 693 */           buf[ptr++] = 61;
/*     */           break;
/*     */         case 2:
/* 696 */           buf[ptr++] = encodeByte(input[i] >> 2);
/* 697 */           buf[ptr++] = encodeByte((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 700 */           buf[ptr++] = encodeByte((input[i + 1] & 0xF) << 2);
/* 701 */           buf[ptr++] = 61;
/*     */           break;
/*     */         default:
/* 704 */           buf[ptr++] = encodeByte(input[i] >> 2);
/* 705 */           buf[ptr++] = encodeByte((input[i] & 0x3) << 4 | input[i + 1] >> 4 & 0xF);
/*     */ 
/*     */           
/* 708 */           buf[ptr++] = encodeByte((input[i + 1] & 0xF) << 2 | input[i + 2] >> 6 & 0x3);
/*     */ 
/*     */           
/* 711 */           buf[ptr++] = encodeByte(input[i + 2] & 0x3F);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 716 */     return ptr;
/*     */   }
/*     */   
/*     */   private static CharSequence removeOptionalPlus(CharSequence s) {
/* 720 */     int len = s.length();
/*     */     
/* 722 */     if (len <= 1 || s.charAt(0) != '+') return s;
/*     */     
/* 724 */     s = s.subSequence(1, len);
/* 725 */     char ch = s.charAt(0);
/* 726 */     if ('0' <= ch && ch <= '9') return s; 
/* 727 */     if ('.' == ch) return s;
/*     */     
/* 729 */     throw new NumberFormatException();
/*     */   }
/*     */   
/*     */   private static boolean isDigitOrPeriodOrSign(char ch) {
/* 733 */     if ('0' <= ch && ch <= '9') return true; 
/* 734 */     if (ch == '+' || ch == '-' || ch == '.') return true; 
/* 735 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 742 */       datatypeFactory = DatatypeFactory.newInstance();
/* 743 */     } catch (DatatypeConfigurationException e) {
/* 744 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class CalendarFormatter
/*     */   {
/*     */     public static String doFormat(String format, Calendar cal) throws IllegalArgumentException {
/* 751 */       int fidx = 0;
/* 752 */       int flen = format.length();
/* 753 */       StringBuilder buf = new StringBuilder();
/*     */       
/* 755 */       while (fidx < flen) {
/* 756 */         char fch = format.charAt(fidx++);
/*     */         
/* 758 */         if (fch != '%') {
/* 759 */           buf.append(fch);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 764 */         switch (format.charAt(fidx++)) {
/*     */           case 'Y':
/* 766 */             formatYear(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'M':
/* 770 */             formatMonth(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'D':
/* 774 */             formatDays(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'h':
/* 778 */             formatHours(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'm':
/* 782 */             formatMinutes(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 's':
/* 786 */             formatSeconds(cal, buf);
/*     */             continue;
/*     */           
/*     */           case 'z':
/* 790 */             formatTimeZone(cal, buf);
/*     */             continue;
/*     */         } 
/*     */ 
/*     */         
/* 795 */         throw new InternalError();
/*     */       } 
/*     */ 
/*     */       
/* 799 */       return buf.toString();
/*     */     }
/*     */     
/*     */     private static void formatYear(Calendar cal, StringBuilder buf) {
/*     */       String s;
/* 804 */       int year = cal.get(1);
/*     */ 
/*     */       
/* 807 */       if (year <= 0) {
/* 808 */         s = Integer.toString(1 - year);
/*     */       } else {
/* 810 */         s = Integer.toString(year);
/*     */       } 
/* 812 */       while (s.length() < 4)
/* 813 */         s = '0' + s; 
/* 814 */       if (year <= 0) {
/* 815 */         s = '-' + s;
/*     */       }
/* 817 */       buf.append(s);
/*     */     }
/*     */     
/*     */     private static void formatMonth(Calendar cal, StringBuilder buf) {
/* 821 */       formatTwoDigits(cal.get(2) + 1, buf);
/*     */     }
/*     */     
/*     */     private static void formatDays(Calendar cal, StringBuilder buf) {
/* 825 */       formatTwoDigits(cal.get(5), buf);
/*     */     }
/*     */     
/*     */     private static void formatHours(Calendar cal, StringBuilder buf) {
/* 829 */       formatTwoDigits(cal.get(11), buf);
/*     */     }
/*     */     
/*     */     private static void formatMinutes(Calendar cal, StringBuilder buf) {
/* 833 */       formatTwoDigits(cal.get(12), buf);
/*     */     }
/*     */     
/*     */     private static void formatSeconds(Calendar cal, StringBuilder buf) {
/* 837 */       formatTwoDigits(cal.get(13), buf);
/* 838 */       if (cal.isSet(14)) {
/* 839 */         int n = cal.get(14);
/* 840 */         if (n != 0) {
/* 841 */           String ms = Integer.toString(n);
/* 842 */           while (ms.length() < 3) {
/* 843 */             ms = '0' + ms;
/*     */           }
/* 845 */           buf.append('.');
/* 846 */           buf.append(ms);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void formatTimeZone(Calendar cal, StringBuilder buf) {
/*     */       int offset;
/* 853 */       TimeZone tz = cal.getTimeZone();
/*     */       
/* 855 */       if (tz == null) {
/*     */         return;
/*     */       }
/*     */       
/* 859 */       if (tz.inDaylightTime(cal.getTime())) {
/* 860 */         offset = tz.getRawOffset() + (tz.useDaylightTime() ? 3600000 : 0);
/*     */       } else {
/* 862 */         offset = tz.getRawOffset();
/*     */       } 
/*     */       
/* 865 */       if (offset == 0) {
/* 866 */         buf.append('Z');
/*     */         
/*     */         return;
/*     */       } 
/* 870 */       if (offset >= 0) {
/* 871 */         buf.append('+');
/*     */       } else {
/* 873 */         buf.append('-');
/* 874 */         offset *= -1;
/*     */       } 
/*     */       
/* 877 */       offset /= 60000;
/*     */       
/* 879 */       formatTwoDigits(offset / 60, buf);
/* 880 */       buf.append(':');
/* 881 */       formatTwoDigits(offset % 60, buf);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static void formatTwoDigits(int n, StringBuilder buf) {
/* 887 */       if (n < 10) buf.append('0'); 
/* 888 */       buf.append(n);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\DatatypeConverterImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */