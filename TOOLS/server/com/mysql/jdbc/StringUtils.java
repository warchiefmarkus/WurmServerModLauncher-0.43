/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StringUtils
/*      */ {
/*      */   private static final int BYTE_RANGE = 256;
/*   49 */   private static byte[] allBytes = new byte[256];
/*      */   
/*   51 */   private static char[] byteToChars = new char[256];
/*      */   
/*      */   private static Method toPlainStringMethod;
/*      */   
/*      */   static final int WILD_COMPARE_MATCH_NO_WILD = 0;
/*      */   
/*      */   static final int WILD_COMPARE_MATCH_WITH_WILD = 1;
/*      */   
/*      */   static final int WILD_COMPARE_NO_MATCH = -1;
/*      */   
/*      */   static {
/*   62 */     for (int i = -128; i <= 127; i++) {
/*   63 */       allBytes[i - -128] = (byte)i;
/*      */     }
/*      */     
/*   66 */     String allBytesString = new String(allBytes, 0, 255);
/*      */ 
/*      */     
/*   69 */     int allBytesStringLen = allBytesString.length();
/*      */     
/*   71 */     int j = 0;
/*   72 */     for (; j < 255 && j < allBytesStringLen; j++) {
/*   73 */       byteToChars[j] = allBytesString.charAt(j);
/*      */     }
/*      */     
/*      */     try {
/*   77 */       toPlainStringMethod = BigDecimal.class.getMethod("toPlainString", new Class[0]);
/*      */     }
/*   79 */     catch (NoSuchMethodException nsme) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String consistentToString(BigDecimal decimal) {
/*   94 */     if (decimal == null) {
/*   95 */       return null;
/*      */     }
/*      */     
/*   98 */     if (toPlainStringMethod != null) {
/*      */       try {
/*  100 */         return (String)toPlainStringMethod.invoke(decimal, null);
/*  101 */       } catch (InvocationTargetException invokeEx) {
/*      */       
/*  103 */       } catch (IllegalAccessException accessEx) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  108 */     return decimal.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String dumpAsHex(byte[] byteBuffer, int length) {
/*  122 */     StringBuffer outputBuf = new StringBuffer(length * 4);
/*      */     
/*  124 */     int p = 0;
/*  125 */     int rows = length / 8;
/*      */     
/*  127 */     for (int i = 0; i < rows && p < length; i++) {
/*  128 */       int ptemp = p;
/*      */       int k;
/*  130 */       for (k = 0; k < 8; k++) {
/*  131 */         String hexVal = Integer.toHexString(byteBuffer[ptemp] & 0xFF);
/*      */         
/*  133 */         if (hexVal.length() == 1) {
/*  134 */           hexVal = "0" + hexVal;
/*      */         }
/*      */         
/*  137 */         outputBuf.append(hexVal + " ");
/*  138 */         ptemp++;
/*      */       } 
/*      */       
/*  141 */       outputBuf.append("    ");
/*      */       
/*  143 */       for (k = 0; k < 8; k++) {
/*  144 */         int b = 0xFF & byteBuffer[p];
/*      */         
/*  146 */         if (b > 32 && b < 127) {
/*  147 */           outputBuf.append((char)b + " ");
/*      */         } else {
/*  149 */           outputBuf.append(". ");
/*      */         } 
/*      */         
/*  152 */         p++;
/*      */       } 
/*      */       
/*  155 */       outputBuf.append("\n");
/*      */     } 
/*      */     
/*  158 */     int n = 0;
/*      */     int j;
/*  160 */     for (j = p; j < length; j++) {
/*  161 */       String hexVal = Integer.toHexString(byteBuffer[j] & 0xFF);
/*      */       
/*  163 */       if (hexVal.length() == 1) {
/*  164 */         hexVal = "0" + hexVal;
/*      */       }
/*      */       
/*  167 */       outputBuf.append(hexVal + " ");
/*  168 */       n++;
/*      */     } 
/*      */     
/*  171 */     for (j = n; j < 8; j++) {
/*  172 */       outputBuf.append("   ");
/*      */     }
/*      */     
/*  175 */     outputBuf.append("    ");
/*      */     
/*  177 */     for (j = p; j < length; j++) {
/*  178 */       int b = 0xFF & byteBuffer[j];
/*      */       
/*  180 */       if (b > 32 && b < 127) {
/*  181 */         outputBuf.append((char)b + " ");
/*      */       } else {
/*  183 */         outputBuf.append(". ");
/*      */       } 
/*      */     } 
/*      */     
/*  187 */     outputBuf.append("\n");
/*      */     
/*  189 */     return outputBuf.toString();
/*      */   }
/*      */   
/*      */   private static boolean endsWith(byte[] dataFrom, String suffix) {
/*  193 */     for (int i = 1; i <= suffix.length(); i++) {
/*  194 */       int dfOffset = dataFrom.length - i;
/*  195 */       int suffixOffset = suffix.length() - i;
/*  196 */       if (dataFrom[dfOffset] != suffix.charAt(suffixOffset)) {
/*  197 */         return false;
/*      */       }
/*      */     } 
/*  200 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] escapeEasternUnicodeByteStream(byte[] origBytes, String origString, int offset, int length) {
/*  220 */     if (origBytes == null || origBytes.length == 0) {
/*  221 */       return origBytes;
/*      */     }
/*      */     
/*  224 */     int bytesLen = origBytes.length;
/*  225 */     int bufIndex = 0;
/*  226 */     int strIndex = 0;
/*      */     
/*  228 */     ByteArrayOutputStream bytesOut = new ByteArrayOutputStream(bytesLen);
/*      */     
/*      */     while (true) {
/*  231 */       if (origString.charAt(strIndex) == '\\') {
/*      */         
/*  233 */         bytesOut.write(origBytes[bufIndex++]);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  238 */         int loByte = origBytes[bufIndex];
/*      */         
/*  240 */         if (loByte < 0) {
/*  241 */           loByte += 256;
/*      */         }
/*      */ 
/*      */         
/*  245 */         bytesOut.write(loByte);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  263 */         if (loByte >= 128) {
/*  264 */           if (bufIndex < bytesLen - 1) {
/*  265 */             int hiByte = origBytes[bufIndex + 1];
/*      */             
/*  267 */             if (hiByte < 0) {
/*  268 */               hiByte += 256;
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*  273 */             bytesOut.write(hiByte);
/*  274 */             bufIndex++;
/*      */ 
/*      */             
/*  277 */             if (hiByte == 92) {
/*  278 */               bytesOut.write(hiByte);
/*      */             }
/*      */           } 
/*  281 */         } else if (loByte == 92 && 
/*  282 */           bufIndex < bytesLen - 1) {
/*  283 */           int hiByte = origBytes[bufIndex + 1];
/*      */           
/*  285 */           if (hiByte < 0) {
/*  286 */             hiByte += 256;
/*      */           }
/*      */           
/*  289 */           if (hiByte == 98) {
/*      */             
/*  291 */             bytesOut.write(92);
/*  292 */             bytesOut.write(98);
/*  293 */             bufIndex++;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  298 */         bufIndex++;
/*      */       } 
/*      */       
/*  301 */       if (bufIndex >= bytesLen) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  306 */       strIndex++;
/*      */     } 
/*      */     
/*  309 */     return bytesOut.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char firstNonWsCharUc(String searchIn) {
/*  321 */     return firstNonWsCharUc(searchIn, 0);
/*      */   }
/*      */   
/*      */   public static char firstNonWsCharUc(String searchIn, int startAt) {
/*  325 */     if (searchIn == null) {
/*  326 */       return Character.MIN_VALUE;
/*      */     }
/*      */     
/*  329 */     int length = searchIn.length();
/*      */     
/*  331 */     for (int i = startAt; i < length; i++) {
/*  332 */       char c = searchIn.charAt(i);
/*      */       
/*  334 */       if (!Character.isWhitespace(c)) {
/*  335 */         return Character.toUpperCase(c);
/*      */       }
/*      */     } 
/*      */     
/*  339 */     return Character.MIN_VALUE;
/*      */   }
/*      */   
/*      */   public static char firstAlphaCharUc(String searchIn, int startAt) {
/*  343 */     if (searchIn == null) {
/*  344 */       return Character.MIN_VALUE;
/*      */     }
/*      */     
/*  347 */     int length = searchIn.length();
/*      */     
/*  349 */     for (int i = startAt; i < length; i++) {
/*  350 */       char c = searchIn.charAt(i);
/*      */       
/*  352 */       if (Character.isLetter(c)) {
/*  353 */         return Character.toUpperCase(c);
/*      */       }
/*      */     } 
/*      */     
/*  357 */     return Character.MIN_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String fixDecimalExponent(String dString) {
/*  370 */     int ePos = dString.indexOf("E");
/*      */     
/*  372 */     if (ePos == -1) {
/*  373 */       ePos = dString.indexOf("e");
/*      */     }
/*      */     
/*  376 */     if (ePos != -1 && 
/*  377 */       dString.length() > ePos + 1) {
/*  378 */       char maybeMinusChar = dString.charAt(ePos + 1);
/*      */       
/*  380 */       if (maybeMinusChar != '-' && maybeMinusChar != '+') {
/*  381 */         StringBuffer buf = new StringBuffer(dString.length() + 1);
/*  382 */         buf.append(dString.substring(0, ePos + 1));
/*  383 */         buf.append('+');
/*  384 */         buf.append(dString.substring(ePos + 1, dString.length()));
/*  385 */         dString = buf.toString();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  390 */     return dString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*      */     try {
/*  398 */       byte[] b = null;
/*      */       
/*  400 */       if (converter != null) {
/*  401 */         b = converter.toBytes(c);
/*  402 */       } else if (encoding == null) {
/*  403 */         b = (new String(c)).getBytes();
/*      */       } else {
/*  405 */         String s = new String(c);
/*      */         
/*  407 */         b = s.getBytes(encoding);
/*      */         
/*  409 */         if (!parserKnowsUnicode && (encoding.equalsIgnoreCase("SJIS") || encoding.equalsIgnoreCase("BIG5") || encoding.equalsIgnoreCase("GBK")))
/*      */         {
/*      */ 
/*      */           
/*  413 */           if (!encoding.equalsIgnoreCase(serverEncoding)) {
/*  414 */             b = escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  419 */       return b;
/*  420 */     } catch (UnsupportedEncodingException uee) {
/*  421 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009", exceptionInterceptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*      */     try {
/*  432 */       byte[] b = null;
/*      */       
/*  434 */       if (converter != null) {
/*  435 */         b = converter.toBytes(c, offset, length);
/*  436 */       } else if (encoding == null) {
/*  437 */         byte[] temp = (new String(c, offset, length)).getBytes();
/*      */         
/*  439 */         length = temp.length;
/*      */         
/*  441 */         b = new byte[length];
/*  442 */         System.arraycopy(temp, 0, b, 0, length);
/*      */       } else {
/*  444 */         String s = new String(c, offset, length);
/*      */         
/*  446 */         byte[] temp = s.getBytes(encoding);
/*      */         
/*  448 */         length = temp.length;
/*      */         
/*  450 */         b = new byte[length];
/*  451 */         System.arraycopy(temp, 0, b, 0, length);
/*      */         
/*  453 */         if (!parserKnowsUnicode && (encoding.equalsIgnoreCase("SJIS") || encoding.equalsIgnoreCase("BIG5") || encoding.equalsIgnoreCase("GBK")))
/*      */         {
/*      */ 
/*      */           
/*  457 */           if (!encoding.equalsIgnoreCase(serverEncoding)) {
/*  458 */             b = escapeEasternUnicodeByteStream(b, s, offset, length);
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  463 */       return b;
/*  464 */     } catch (UnsupportedEncodingException uee) {
/*  465 */       throw SQLError.createSQLException(Messages.getString("StringUtils.10") + encoding + Messages.getString("StringUtils.11"), "S1009", exceptionInterceptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte[] getBytes(char[] c, String encoding, String serverEncoding, boolean parserKnowsUnicode, ConnectionImpl conn, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*      */     try {
/*  477 */       SingleByteCharsetConverter converter = null;
/*      */       
/*  479 */       if (conn != null) {
/*  480 */         converter = conn.getCharsetConverter(encoding);
/*      */       } else {
/*  482 */         converter = SingleByteCharsetConverter.getInstance(encoding, null);
/*      */       } 
/*      */       
/*  485 */       return getBytes(c, converter, encoding, serverEncoding, parserKnowsUnicode, exceptionInterceptor);
/*      */     }
/*  487 */     catch (UnsupportedEncodingException uee) {
/*  488 */       throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), "S1009", exceptionInterceptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*      */     try {
/*  519 */       byte[] b = null;
/*      */       
/*  521 */       if (converter != null) {
/*  522 */         b = converter.toBytes(s);
/*  523 */       } else if (encoding == null) {
/*  524 */         b = s.getBytes();
/*      */       } else {
/*  526 */         b = s.getBytes(encoding);
/*      */         
/*  528 */         if (!parserKnowsUnicode && (encoding.equalsIgnoreCase("SJIS") || encoding.equalsIgnoreCase("BIG5") || encoding.equalsIgnoreCase("GBK")))
/*      */         {
/*      */ 
/*      */           
/*  532 */           if (!encoding.equalsIgnoreCase(serverEncoding)) {
/*  533 */             b = escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  538 */       return b;
/*  539 */     } catch (UnsupportedEncodingException uee) {
/*  540 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009", exceptionInterceptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte[] getBytesWrapped(String s, char beginWrap, char endWrap, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*      */     try {
/*  551 */       byte[] b = null;
/*      */       
/*  553 */       if (converter != null) {
/*  554 */         b = converter.toBytesWrapped(s, beginWrap, endWrap);
/*  555 */       } else if (encoding == null) {
/*  556 */         StringBuffer buf = new StringBuffer(s.length() + 2);
/*  557 */         buf.append(beginWrap);
/*  558 */         buf.append(s);
/*  559 */         buf.append(endWrap);
/*      */         
/*  561 */         b = buf.toString().getBytes();
/*      */       } else {
/*  563 */         StringBuffer buf = new StringBuffer(s.length() + 2);
/*  564 */         buf.append(beginWrap);
/*  565 */         buf.append(s);
/*  566 */         buf.append(endWrap);
/*      */         
/*  568 */         b = buf.toString().getBytes(encoding);
/*      */         
/*  570 */         if (!parserKnowsUnicode && (encoding.equalsIgnoreCase("SJIS") || encoding.equalsIgnoreCase("BIG5") || encoding.equalsIgnoreCase("GBK")))
/*      */         {
/*      */ 
/*      */           
/*  574 */           if (!encoding.equalsIgnoreCase(serverEncoding)) {
/*  575 */             b = escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  580 */       return b;
/*  581 */     } catch (UnsupportedEncodingException uee) {
/*  582 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009", exceptionInterceptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*      */     try {
/*  616 */       byte[] b = null;
/*      */       
/*  618 */       if (converter != null) {
/*  619 */         b = converter.toBytes(s, offset, length);
/*  620 */       } else if (encoding == null) {
/*  621 */         byte[] temp = s.substring(offset, offset + length).getBytes();
/*      */         
/*  623 */         length = temp.length;
/*      */         
/*  625 */         b = new byte[length];
/*  626 */         System.arraycopy(temp, 0, b, 0, length);
/*      */       } else {
/*      */         
/*  629 */         byte[] temp = s.substring(offset, offset + length).getBytes(encoding);
/*      */ 
/*      */         
/*  632 */         length = temp.length;
/*      */         
/*  634 */         b = new byte[length];
/*  635 */         System.arraycopy(temp, 0, b, 0, length);
/*      */         
/*  637 */         if (!parserKnowsUnicode && (encoding.equalsIgnoreCase("SJIS") || encoding.equalsIgnoreCase("BIG5") || encoding.equalsIgnoreCase("GBK")))
/*      */         {
/*      */ 
/*      */           
/*  641 */           if (!encoding.equalsIgnoreCase(serverEncoding)) {
/*  642 */             b = escapeEasternUnicodeByteStream(b, s, offset, length);
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  647 */       return b;
/*  648 */     } catch (UnsupportedEncodingException uee) {
/*  649 */       throw SQLError.createSQLException(Messages.getString("StringUtils.10") + encoding + Messages.getString("StringUtils.11"), "S1009", exceptionInterceptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte[] getBytes(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode, ConnectionImpl conn, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*      */     try {
/*  676 */       SingleByteCharsetConverter converter = null;
/*      */       
/*  678 */       if (conn != null) {
/*  679 */         converter = conn.getCharsetConverter(encoding);
/*      */       } else {
/*  681 */         converter = SingleByteCharsetConverter.getInstance(encoding, null);
/*      */       } 
/*      */       
/*  684 */       return getBytes(s, converter, encoding, serverEncoding, parserKnowsUnicode, exceptionInterceptor);
/*      */     }
/*  686 */     catch (UnsupportedEncodingException uee) {
/*  687 */       throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), "S1009", exceptionInterceptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getInt(byte[] buf, int offset, int endPos) throws NumberFormatException {
/*  694 */     int base = 10;
/*      */     
/*  696 */     int s = offset;
/*      */ 
/*      */     
/*  699 */     while (Character.isWhitespace((char)buf[s]) && s < endPos) {
/*  700 */       s++;
/*      */     }
/*      */     
/*  703 */     if (s == endPos) {
/*  704 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */ 
/*      */     
/*  708 */     boolean negative = false;
/*      */     
/*  710 */     if ((char)buf[s] == '-') {
/*  711 */       negative = true;
/*  712 */       s++;
/*  713 */     } else if ((char)buf[s] == '+') {
/*  714 */       s++;
/*      */     } 
/*      */ 
/*      */     
/*  718 */     int save = s;
/*      */     
/*  720 */     int cutoff = Integer.MAX_VALUE / base;
/*  721 */     int cutlim = Integer.MAX_VALUE % base;
/*      */     
/*  723 */     if (negative) {
/*  724 */       cutlim++;
/*      */     }
/*      */     
/*  727 */     boolean overflow = false;
/*      */     
/*  729 */     int i = 0;
/*      */     
/*  731 */     for (; s < endPos; s++) {
/*  732 */       char c = (char)buf[s];
/*      */       
/*  734 */       if (Character.isDigit(c)) {
/*  735 */         c = (char)(c - 48);
/*  736 */       } else if (Character.isLetter(c)) {
/*  737 */         c = (char)(Character.toUpperCase(c) - 65 + 10);
/*      */       } else {
/*      */         break;
/*      */       } 
/*      */       
/*  742 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  747 */       if (i > cutoff || (i == cutoff && c > cutlim)) {
/*  748 */         overflow = true;
/*      */       } else {
/*  750 */         i *= base;
/*  751 */         i += c;
/*      */       } 
/*      */     } 
/*      */     
/*  755 */     if (s == save) {
/*  756 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*  759 */     if (overflow) {
/*  760 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */ 
/*      */     
/*  764 */     return negative ? -i : i;
/*      */   }
/*      */   
/*      */   public static int getInt(byte[] buf) throws NumberFormatException {
/*  768 */     return getInt(buf, 0, buf.length);
/*      */   }
/*      */   
/*      */   public static long getLong(byte[] buf) throws NumberFormatException {
/*  772 */     return getLong(buf, 0, buf.length);
/*      */   }
/*      */   
/*      */   public static long getLong(byte[] buf, int offset, int endpos) throws NumberFormatException {
/*  776 */     int base = 10;
/*      */     
/*  778 */     int s = offset;
/*      */ 
/*      */     
/*  781 */     while (Character.isWhitespace((char)buf[s]) && s < endpos) {
/*  782 */       s++;
/*      */     }
/*      */     
/*  785 */     if (s == endpos) {
/*  786 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */ 
/*      */     
/*  790 */     boolean negative = false;
/*      */     
/*  792 */     if ((char)buf[s] == '-') {
/*  793 */       negative = true;
/*  794 */       s++;
/*  795 */     } else if ((char)buf[s] == '+') {
/*  796 */       s++;
/*      */     } 
/*      */ 
/*      */     
/*  800 */     int save = s;
/*      */     
/*  802 */     long cutoff = Long.MAX_VALUE / base;
/*  803 */     long cutlim = (int)(Long.MAX_VALUE % base);
/*      */     
/*  805 */     if (negative) {
/*  806 */       cutlim++;
/*      */     }
/*      */     
/*  809 */     boolean overflow = false;
/*  810 */     long i = 0L;
/*      */     
/*  812 */     for (; s < endpos; s++) {
/*  813 */       char c = (char)buf[s];
/*      */       
/*  815 */       if (Character.isDigit(c)) {
/*  816 */         c = (char)(c - 48);
/*  817 */       } else if (Character.isLetter(c)) {
/*  818 */         c = (char)(Character.toUpperCase(c) - 65 + 10);
/*      */       } else {
/*      */         break;
/*      */       } 
/*      */       
/*  823 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  828 */       if (i > cutoff || (i == cutoff && c > cutlim)) {
/*  829 */         overflow = true;
/*      */       } else {
/*  831 */         i *= base;
/*  832 */         i += c;
/*      */       } 
/*      */     } 
/*      */     
/*  836 */     if (s == save) {
/*  837 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*  840 */     if (overflow) {
/*  841 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */ 
/*      */     
/*  845 */     return negative ? -i : i;
/*      */   }
/*      */   
/*      */   public static short getShort(byte[] buf) throws NumberFormatException {
/*  849 */     short base = 10;
/*      */     
/*  851 */     int s = 0;
/*      */ 
/*      */     
/*  854 */     while (Character.isWhitespace((char)buf[s]) && s < buf.length) {
/*  855 */       s++;
/*      */     }
/*      */     
/*  858 */     if (s == buf.length) {
/*  859 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */ 
/*      */     
/*  863 */     boolean negative = false;
/*      */     
/*  865 */     if ((char)buf[s] == '-') {
/*  866 */       negative = true;
/*  867 */       s++;
/*  868 */     } else if ((char)buf[s] == '+') {
/*  869 */       s++;
/*      */     } 
/*      */ 
/*      */     
/*  873 */     int save = s;
/*      */     
/*  875 */     short cutoff = (short)(32767 / base);
/*  876 */     short cutlim = (short)(32767 % base);
/*      */     
/*  878 */     if (negative) {
/*  879 */       cutlim = (short)(cutlim + 1);
/*      */     }
/*      */     
/*  882 */     boolean overflow = false;
/*  883 */     short i = 0;
/*      */     
/*  885 */     for (; s < buf.length; s++) {
/*  886 */       char c = (char)buf[s];
/*      */       
/*  888 */       if (Character.isDigit(c)) {
/*  889 */         c = (char)(c - 48);
/*  890 */       } else if (Character.isLetter(c)) {
/*  891 */         c = (char)(Character.toUpperCase(c) - 65 + 10);
/*      */       } else {
/*      */         break;
/*      */       } 
/*      */       
/*  896 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  901 */       if (i > cutoff || (i == cutoff && c > cutlim)) {
/*  902 */         overflow = true;
/*      */       } else {
/*  904 */         i = (short)(i * base);
/*  905 */         i = (short)(i + c);
/*      */       } 
/*      */     } 
/*      */     
/*  909 */     if (s == save) {
/*  910 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*  913 */     if (overflow) {
/*  914 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */ 
/*      */     
/*  918 */     return negative ? (short)-i : i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor) {
/*  923 */     if (searchIn == null || searchFor == null || startingPosition > searchIn.length())
/*      */     {
/*  925 */       return -1;
/*      */     }
/*      */     
/*  928 */     int patternLength = searchFor.length();
/*  929 */     int stringLength = searchIn.length();
/*  930 */     int stopSearchingAt = stringLength - patternLength;
/*      */     
/*  932 */     if (patternLength == 0) {
/*  933 */       return -1;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  938 */     char firstCharOfPatternUc = Character.toUpperCase(searchFor.charAt(0));
/*  939 */     char firstCharOfPatternLc = Character.toLowerCase(searchFor.charAt(0));
/*      */ 
/*      */     
/*  942 */     for (int i = startingPosition; i <= stopSearchingAt; i++) {
/*  943 */       if (isNotEqualIgnoreCharCase(searchIn, firstCharOfPatternUc, firstCharOfPatternLc, i))
/*      */       {
/*      */         
/*  946 */         while (++i <= stopSearchingAt && isNotEqualIgnoreCharCase(searchIn, firstCharOfPatternUc, firstCharOfPatternLc, i));
/*      */       }
/*      */ 
/*      */       
/*  950 */       if (i <= stopSearchingAt) {
/*      */ 
/*      */         
/*  953 */         int j = i + 1;
/*  954 */         int end = j + patternLength - 1;
/*  955 */         for (int k = 1; j < end && (Character.toLowerCase(searchIn.charAt(j)) == Character.toLowerCase(searchFor.charAt(k)) || Character.toUpperCase(searchIn.charAt(j)) == Character.toUpperCase(searchFor.charAt(k))); ) {
/*      */           
/*  957 */           j++; k++;
/*      */         } 
/*  959 */         if (j == end) {
/*  960 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  965 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isNotEqualIgnoreCharCase(String searchIn, char firstCharOfPatternUc, char firstCharOfPatternLc, int i) {
/*  970 */     return (Character.toLowerCase(searchIn.charAt(i)) != firstCharOfPatternLc && Character.toUpperCase(searchIn.charAt(i)) != firstCharOfPatternUc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int indexOfIgnoreCase(String searchIn, String searchFor) {
/*  985 */     return indexOfIgnoreCase(0, searchIn, searchFor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfIgnoreCaseRespectMarker(int startAt, String src, String target, String marker, String markerCloses, boolean allowBackslashEscapes) {
/*  991 */     char contextMarker = Character.MIN_VALUE;
/*  992 */     boolean escaped = false;
/*  993 */     int markerTypeFound = 0;
/*  994 */     int srcLength = src.length();
/*  995 */     int ind = 0;
/*      */     
/*  997 */     for (int i = startAt; i < srcLength; i++) {
/*  998 */       char c = src.charAt(i);
/*      */       
/* 1000 */       if (allowBackslashEscapes && c == '\\') {
/* 1001 */         escaped = !escaped;
/* 1002 */       } else if (c == markerCloses.charAt(markerTypeFound) && !escaped) {
/* 1003 */         contextMarker = Character.MIN_VALUE;
/* 1004 */       } else if ((ind = marker.indexOf(c)) != -1 && !escaped && contextMarker == '\000') {
/*      */         
/* 1006 */         markerTypeFound = ind;
/* 1007 */         contextMarker = c;
/* 1008 */       } else if ((Character.toUpperCase(c) == Character.toUpperCase(target.charAt(0)) || Character.toLowerCase(c) == Character.toLowerCase(target.charAt(0))) && !escaped && contextMarker == '\000') {
/*      */ 
/*      */         
/* 1011 */         if (startsWithIgnoreCase(src, i, target)) {
/* 1012 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1016 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfIgnoreCaseRespectQuotes(int startAt, String src, String target, char quoteChar, boolean allowBackslashEscapes) {
/* 1022 */     char contextMarker = Character.MIN_VALUE;
/* 1023 */     boolean escaped = false;
/*      */     
/* 1025 */     int srcLength = src.length();
/*      */     
/* 1027 */     for (int i = startAt; i < srcLength; i++) {
/* 1028 */       char c = src.charAt(i);
/*      */       
/* 1030 */       if (allowBackslashEscapes && c == '\\') {
/* 1031 */         escaped = !escaped;
/* 1032 */       } else if (c == contextMarker && !escaped) {
/* 1033 */         contextMarker = Character.MIN_VALUE;
/* 1034 */       } else if (c == quoteChar && !escaped && contextMarker == '\000') {
/*      */         
/* 1036 */         contextMarker = c;
/*      */       
/*      */       }
/* 1039 */       else if ((Character.toUpperCase(c) == Character.toUpperCase(target.charAt(0)) || Character.toLowerCase(c) == Character.toLowerCase(target.charAt(0))) && !escaped && contextMarker == '\000') {
/*      */ 
/*      */         
/* 1042 */         if (startsWithIgnoreCase(src, i, target)) {
/* 1043 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1047 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final List split(String stringToSplit, String delimitter, boolean trim) {
/* 1068 */     if (stringToSplit == null) {
/* 1069 */       return new ArrayList();
/*      */     }
/*      */     
/* 1072 */     if (delimitter == null) {
/* 1073 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1076 */     StringTokenizer tokenizer = new StringTokenizer(stringToSplit, delimitter, false);
/*      */ 
/*      */     
/* 1079 */     List splitTokens = new ArrayList(tokenizer.countTokens());
/*      */     
/* 1081 */     while (tokenizer.hasMoreTokens()) {
/* 1082 */       String token = tokenizer.nextToken();
/*      */       
/* 1084 */       if (trim) {
/* 1085 */         token = token.trim();
/*      */       }
/*      */       
/* 1088 */       splitTokens.add(token);
/*      */     } 
/*      */     
/* 1091 */     return splitTokens;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final List split(String stringToSplit, String delimiter, String markers, String markerCloses, boolean trim) {
/* 1111 */     if (stringToSplit == null) {
/* 1112 */       return new ArrayList();
/*      */     }
/*      */     
/* 1115 */     if (delimiter == null) {
/* 1116 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1119 */     int delimPos = 0;
/* 1120 */     int currentPos = 0;
/*      */     
/* 1122 */     List splitTokens = new ArrayList();
/*      */ 
/*      */     
/* 1125 */     while ((delimPos = indexOfIgnoreCaseRespectMarker(currentPos, stringToSplit, delimiter, markers, markerCloses, false)) != -1) {
/* 1126 */       String token = stringToSplit.substring(currentPos, delimPos);
/*      */       
/* 1128 */       if (trim) {
/* 1129 */         token = token.trim();
/*      */       }
/*      */       
/* 1132 */       splitTokens.add(token);
/* 1133 */       currentPos = delimPos + 1;
/*      */     } 
/*      */     
/* 1136 */     if (currentPos < stringToSplit.length()) {
/* 1137 */       String token = stringToSplit.substring(currentPos);
/*      */       
/* 1139 */       if (trim) {
/* 1140 */         token = token.trim();
/*      */       }
/*      */       
/* 1143 */       splitTokens.add(token);
/*      */     } 
/*      */     
/* 1146 */     return splitTokens;
/*      */   }
/*      */   
/*      */   private static boolean startsWith(byte[] dataFrom, String chars) {
/* 1150 */     for (int i = 0; i < chars.length(); i++) {
/* 1151 */       if (dataFrom[i] != chars.charAt(i)) {
/* 1152 */         return false;
/*      */       }
/*      */     } 
/* 1155 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor) {
/* 1174 */     return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean startsWithIgnoreCase(String searchIn, String searchFor) {
/* 1190 */     return startsWithIgnoreCase(searchIn, 0, searchFor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean startsWithIgnoreCaseAndNonAlphaNumeric(String searchIn, String searchFor) {
/* 1207 */     if (searchIn == null) {
/* 1208 */       return (searchFor == null);
/*      */     }
/*      */     
/* 1211 */     int beginPos = 0;
/*      */     
/* 1213 */     int inLength = searchIn.length();
/*      */     
/* 1215 */     for (beginPos = 0; beginPos < inLength; beginPos++) {
/* 1216 */       char c = searchIn.charAt(beginPos);
/*      */       
/* 1218 */       if (Character.isLetterOrDigit(c)) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/* 1223 */     return startsWithIgnoreCase(searchIn, beginPos, searchFor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor) {
/* 1239 */     return startsWithIgnoreCaseAndWs(searchIn, searchFor, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor, int beginPos) {
/* 1258 */     if (searchIn == null) {
/* 1259 */       return (searchFor == null);
/*      */     }
/*      */     
/* 1262 */     int inLength = searchIn.length();
/*      */     
/* 1264 */     for (; beginPos < inLength && 
/* 1265 */       Character.isWhitespace(searchIn.charAt(beginPos)); beginPos++);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1270 */     return startsWithIgnoreCase(searchIn, beginPos, searchFor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] stripEnclosure(byte[] source, String prefix, String suffix) {
/* 1281 */     if (source.length >= prefix.length() + suffix.length() && startsWith(source, prefix) && endsWith(source, suffix)) {
/*      */ 
/*      */       
/* 1284 */       int totalToStrip = prefix.length() + suffix.length();
/* 1285 */       int enclosedLength = source.length - totalToStrip;
/* 1286 */       byte[] enclosed = new byte[enclosedLength];
/*      */       
/* 1288 */       int startPos = prefix.length();
/* 1289 */       int numToCopy = enclosed.length;
/* 1290 */       System.arraycopy(source, startPos, enclosed, 0, numToCopy);
/*      */       
/* 1292 */       return enclosed;
/*      */     } 
/* 1294 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String toAsciiString(byte[] buffer) {
/* 1306 */     return toAsciiString(buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String toAsciiString(byte[] buffer, int startPos, int length) {
/* 1323 */     char[] charArray = new char[length];
/* 1324 */     int readpoint = startPos;
/*      */     
/* 1326 */     for (int i = 0; i < length; i++) {
/* 1327 */       charArray[i] = (char)buffer[readpoint];
/* 1328 */       readpoint++;
/*      */     } 
/*      */     
/* 1331 */     return new String(charArray);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int wildCompare(String searchIn, String searchForWildcard) {
/* 1349 */     if (searchIn == null || searchForWildcard == null) {
/* 1350 */       return -1;
/*      */     }
/*      */     
/* 1353 */     if (searchForWildcard.equals("%"))
/*      */     {
/* 1355 */       return 1;
/*      */     }
/*      */     
/* 1358 */     int result = -1;
/*      */     
/* 1360 */     char wildcardMany = '%';
/* 1361 */     char wildcardOne = '_';
/* 1362 */     char wildcardEscape = '\\';
/*      */     
/* 1364 */     int searchForPos = 0;
/* 1365 */     int searchForEnd = searchForWildcard.length();
/*      */     
/* 1367 */     int searchInPos = 0;
/* 1368 */     int searchInEnd = searchIn.length();
/*      */     
/* 1370 */     while (searchForPos != searchForEnd) {
/* 1371 */       char wildstrChar = searchForWildcard.charAt(searchForPos);
/*      */ 
/*      */       
/* 1374 */       while (searchForWildcard.charAt(searchForPos) != wildcardMany && wildstrChar != wildcardOne) {
/* 1375 */         if (searchForWildcard.charAt(searchForPos) == wildcardEscape && searchForPos + 1 != searchForEnd)
/*      */         {
/* 1377 */           searchForPos++;
/*      */         }
/*      */         
/* 1380 */         if (searchInPos == searchInEnd || Character.toUpperCase(searchForWildcard.charAt(searchForPos++)) != Character.toUpperCase(searchIn.charAt(searchInPos++)))
/*      */         {
/*      */ 
/*      */           
/* 1384 */           return 1;
/*      */         }
/*      */         
/* 1387 */         if (searchForPos == searchForEnd) {
/* 1388 */           return (searchInPos != searchInEnd) ? 1 : 0;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1395 */         result = 1;
/*      */       } 
/*      */       
/* 1398 */       if (searchForWildcard.charAt(searchForPos) == wildcardOne) {
/*      */         do {
/* 1400 */           if (searchInPos == searchInEnd)
/*      */           {
/*      */ 
/*      */ 
/*      */             
/* 1405 */             return result;
/*      */           }
/*      */           
/* 1408 */           searchInPos++;
/*      */         }
/* 1410 */         while (++searchForPos < searchForEnd && searchForWildcard.charAt(searchForPos) == wildcardOne);
/*      */         
/* 1412 */         if (searchForPos == searchForEnd) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */       
/* 1417 */       if (searchForWildcard.charAt(searchForPos) == wildcardMany) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1424 */         searchForPos++;
/*      */ 
/*      */         
/* 1427 */         for (; searchForPos != searchForEnd; searchForPos++) {
/* 1428 */           if (searchForWildcard.charAt(searchForPos) != wildcardMany)
/*      */           {
/*      */ 
/*      */             
/* 1432 */             if (searchForWildcard.charAt(searchForPos) == wildcardOne) {
/* 1433 */               if (searchInPos == searchInEnd) {
/* 1434 */                 return -1;
/*      */               }
/*      */               
/* 1437 */               searchInPos++;
/*      */             } else {
/*      */               break;
/*      */             } 
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 1445 */         if (searchForPos == searchForEnd) {
/* 1446 */           return 0;
/*      */         }
/*      */         
/* 1449 */         if (searchInPos == searchInEnd) {
/* 1450 */           return -1;
/*      */         }
/*      */         char cmp;
/* 1453 */         if ((cmp = searchForWildcard.charAt(searchForPos)) == wildcardEscape && searchForPos + 1 != searchForEnd)
/*      */         {
/* 1455 */           cmp = searchForWildcard.charAt(++searchForPos);
/*      */         }
/*      */         
/* 1458 */         searchForPos++;
/*      */ 
/*      */         
/*      */         while (true) {
/* 1462 */           if (searchInPos != searchInEnd && Character.toUpperCase(searchIn.charAt(searchInPos)) != Character.toUpperCase(cmp)) {
/*      */ 
/*      */             
/* 1465 */             searchInPos++; continue;
/*      */           } 
/* 1467 */           if (searchInPos++ == searchInEnd) {
/* 1468 */             return -1;
/*      */           }
/*      */ 
/*      */           
/* 1472 */           int tmp = wildCompare(searchIn, searchForWildcard);
/*      */           
/* 1474 */           if (tmp <= 0) {
/* 1475 */             return tmp;
/*      */           }
/*      */ 
/*      */           
/* 1479 */           if (searchInPos == searchInEnd || searchForWildcard.charAt(0) == wildcardMany)
/*      */             break; 
/* 1481 */         }  return -1;
/*      */       } 
/*      */     } 
/*      */     
/* 1485 */     return (searchInPos != searchInEnd) ? 1 : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   static byte[] s2b(String s, ConnectionImpl conn) throws SQLException {
/* 1490 */     if (s == null) {
/* 1491 */       return null;
/*      */     }
/*      */     
/* 1494 */     if (conn != null && conn.getUseUnicode()) {
/*      */       try {
/* 1496 */         String encoding = conn.getEncoding();
/*      */         
/* 1498 */         if (encoding == null) {
/* 1499 */           return s.getBytes();
/*      */         }
/*      */         
/* 1502 */         SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
/*      */ 
/*      */         
/* 1505 */         if (converter != null) {
/* 1506 */           return converter.toBytes(s);
/*      */         }
/*      */         
/* 1509 */         return s.getBytes(encoding);
/* 1510 */       } catch (UnsupportedEncodingException E) {
/* 1511 */         return s.getBytes();
/*      */       } 
/*      */     }
/*      */     
/* 1515 */     return s.getBytes();
/*      */   }
/*      */   
/*      */   public static int lastIndexOf(byte[] s, char c) {
/* 1519 */     if (s == null) {
/* 1520 */       return -1;
/*      */     }
/*      */     
/* 1523 */     for (int i = s.length - 1; i >= 0; i--) {
/* 1524 */       if (s[i] == c) {
/* 1525 */         return i;
/*      */       }
/*      */     } 
/*      */     
/* 1529 */     return -1;
/*      */   }
/*      */   
/*      */   public static int indexOf(byte[] s, char c) {
/* 1533 */     if (s == null) {
/* 1534 */       return -1;
/*      */     }
/*      */     
/* 1537 */     int length = s.length;
/*      */     
/* 1539 */     for (int i = 0; i < length; i++) {
/* 1540 */       if (s[i] == c) {
/* 1541 */         return i;
/*      */       }
/*      */     } 
/*      */     
/* 1545 */     return -1;
/*      */   }
/*      */   
/*      */   public static boolean isNullOrEmpty(String toTest) {
/* 1549 */     return (toTest == null || toTest.length() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String stripComments(String src, String stringOpens, String stringCloses, boolean slashStarComments, boolean slashSlashComments, boolean hashComments, boolean dashDashComments) {
/* 1576 */     if (src == null) {
/* 1577 */       return null;
/*      */     }
/*      */     
/* 1580 */     StringBuffer buf = new StringBuffer(src.length());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1589 */     StringReader sourceReader = new StringReader(src);
/*      */     
/* 1591 */     int contextMarker = 0;
/* 1592 */     boolean escaped = false;
/* 1593 */     int markerTypeFound = -1;
/*      */     
/* 1595 */     int ind = 0;
/*      */     
/* 1597 */     int currentChar = 0;
/*      */     
/*      */     try {
/* 1600 */       label81: while ((currentChar = sourceReader.read()) != -1) {
/*      */ 
/*      */ 
/*      */         
/* 1604 */         if (markerTypeFound != -1 && currentChar == stringCloses.charAt(markerTypeFound) && !escaped) {
/*      */           
/* 1606 */           contextMarker = 0;
/* 1607 */           markerTypeFound = -1;
/* 1608 */         } else if ((ind = stringOpens.indexOf(currentChar)) != -1 && !escaped && contextMarker == 0) {
/*      */           
/* 1610 */           markerTypeFound = ind;
/* 1611 */           contextMarker = currentChar;
/*      */         } 
/*      */         
/* 1614 */         if (contextMarker == 0 && currentChar == 47 && (slashSlashComments || slashStarComments)) {
/*      */           
/* 1616 */           currentChar = sourceReader.read();
/* 1617 */           if (currentChar == 42 && slashStarComments) {
/* 1618 */             int prevChar = 0;
/*      */             while (true) {
/* 1620 */               if ((currentChar = sourceReader.read()) != 47 || prevChar != 42) {
/* 1621 */                 if (currentChar == 13) {
/*      */                   
/* 1623 */                   currentChar = sourceReader.read();
/* 1624 */                   if (currentChar == 10) {
/* 1625 */                     currentChar = sourceReader.read();
/*      */                   }
/*      */                 }
/* 1628 */                 else if (currentChar == 10) {
/*      */                   
/* 1630 */                   currentChar = sourceReader.read();
/*      */                 } 
/*      */                 
/* 1633 */                 if (currentChar < 0)
/*      */                   continue label81; 
/* 1635 */                 prevChar = currentChar; continue;
/*      */               }  continue label81;
/*      */             } 
/* 1638 */           }  if (currentChar == 47 && slashSlashComments)
/*      */           {
/* 1640 */             while ((currentChar = sourceReader.read()) != 10 && currentChar != 13 && currentChar >= 0);
/*      */           }
/*      */         }
/* 1643 */         else if (contextMarker == 0 && currentChar == 35 && hashComments) {
/*      */ 
/*      */ 
/*      */           
/* 1647 */           while ((currentChar = sourceReader.read()) != 10 && currentChar != 13 && currentChar >= 0);
/*      */         }
/* 1649 */         else if (contextMarker == 0 && currentChar == 45 && dashDashComments) {
/*      */           
/* 1651 */           currentChar = sourceReader.read();
/*      */           
/* 1653 */           if (currentChar == -1 || currentChar != 45) {
/* 1654 */             buf.append('-');
/*      */             
/* 1656 */             if (currentChar != -1) {
/* 1657 */               buf.append(currentChar);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */ 
/*      */           
/* 1666 */           while ((currentChar = sourceReader.read()) != 10 && currentChar != 13 && currentChar >= 0);
/*      */         } 
/*      */ 
/*      */         
/* 1670 */         if (currentChar != -1) {
/* 1671 */           buf.append((char)currentChar);
/*      */         }
/*      */       } 
/* 1674 */     } catch (IOException ioEx) {}
/*      */ 
/*      */ 
/*      */     
/* 1678 */     return buf.toString();
/*      */   }
/*      */   
/*      */   public static final boolean isEmptyOrWhitespaceOnly(String str) {
/* 1682 */     if (str == null || str.length() == 0) {
/* 1683 */       return true;
/*      */     }
/*      */     
/* 1686 */     int length = str.length();
/*      */     
/* 1688 */     for (int i = 0; i < length; i++) {
/* 1689 */       if (!Character.isWhitespace(str.charAt(i))) {
/* 1690 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1694 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\StringUtils.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */