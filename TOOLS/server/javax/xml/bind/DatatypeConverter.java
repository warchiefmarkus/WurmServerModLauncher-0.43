/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Calendar;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DatatypeConverter
/*     */ {
/*  78 */   private static DatatypeConverterInterface theConverter = new DatatypeConverterImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDatatypeConverter(DatatypeConverterInterface converter) {
/* 102 */     if (converter == null) {
/* 103 */       throw new IllegalArgumentException(Messages.format("DatatypeConverter.ConverterMustNotBeNull"));
/*     */     }
/* 105 */     if (theConverter == null) {
/* 106 */       theConverter = converter;
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
/*     */   public static String parseString(String lexicalXSDString) {
/* 120 */     return theConverter.parseString(lexicalXSDString);
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
/*     */   public static BigInteger parseInteger(String lexicalXSDInteger) {
/* 134 */     return theConverter.parseInteger(lexicalXSDInteger);
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
/*     */   public static int parseInt(String lexicalXSDInt) {
/* 148 */     return theConverter.parseInt(lexicalXSDInt);
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
/*     */   public static long parseLong(String lexicalXSDLong) {
/* 162 */     return theConverter.parseLong(lexicalXSDLong);
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
/*     */   public static short parseShort(String lexicalXSDShort) {
/* 176 */     return theConverter.parseShort(lexicalXSDShort);
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
/*     */   public static BigDecimal parseDecimal(String lexicalXSDDecimal) {
/* 190 */     return theConverter.parseDecimal(lexicalXSDDecimal);
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
/*     */   public static float parseFloat(String lexicalXSDFloat) {
/* 204 */     return theConverter.parseFloat(lexicalXSDFloat);
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
/*     */   public static double parseDouble(String lexicalXSDDouble) {
/* 218 */     return theConverter.parseDouble(lexicalXSDDouble);
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
/*     */   public static boolean parseBoolean(String lexicalXSDBoolean) {
/* 232 */     return theConverter.parseBoolean(lexicalXSDBoolean);
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
/*     */   public static byte parseByte(String lexicalXSDByte) {
/* 246 */     return theConverter.parseByte(lexicalXSDByte);
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
/*     */   public static QName parseQName(String lexicalXSDQName, NamespaceContext nsc) {
/* 268 */     return theConverter.parseQName(lexicalXSDQName, nsc);
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
/*     */   public static Calendar parseDateTime(String lexicalXSDDateTime) {
/* 282 */     return theConverter.parseDateTime(lexicalXSDDateTime);
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
/*     */   public static byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
/* 296 */     return theConverter.parseBase64Binary(lexicalXSDBase64Binary);
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
/*     */   public static byte[] parseHexBinary(String lexicalXSDHexBinary) {
/* 310 */     return theConverter.parseHexBinary(lexicalXSDHexBinary);
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
/*     */   public static long parseUnsignedInt(String lexicalXSDUnsignedInt) {
/* 324 */     return theConverter.parseUnsignedInt(lexicalXSDUnsignedInt);
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
/*     */   public static int parseUnsignedShort(String lexicalXSDUnsignedShort) {
/* 338 */     return theConverter.parseUnsignedShort(lexicalXSDUnsignedShort);
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
/*     */   public static Calendar parseTime(String lexicalXSDTime) {
/* 352 */     return theConverter.parseTime(lexicalXSDTime);
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
/*     */   public static Calendar parseDate(String lexicalXSDDate) {
/* 365 */     return theConverter.parseDate(lexicalXSDDate);
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
/*     */   public static String parseAnySimpleType(String lexicalXSDAnySimpleType) {
/* 380 */     return theConverter.parseAnySimpleType(lexicalXSDAnySimpleType);
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
/*     */   public static String printString(String val) {
/* 394 */     return theConverter.printString(val);
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
/*     */   public static String printInteger(BigInteger val) {
/* 407 */     return theConverter.printInteger(val);
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
/*     */   public static String printInt(int val) {
/* 419 */     return theConverter.printInt(val);
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
/*     */   public static String printLong(long val) {
/* 431 */     return theConverter.printLong(val);
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
/*     */   public static String printShort(short val) {
/* 443 */     return theConverter.printShort(val);
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
/*     */   public static String printDecimal(BigDecimal val) {
/* 456 */     return theConverter.printDecimal(val);
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
/*     */   public static String printFloat(float val) {
/* 468 */     return theConverter.printFloat(val);
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
/*     */   public static String printDouble(double val) {
/* 480 */     return theConverter.printDouble(val);
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
/*     */   public static String printBoolean(boolean val) {
/* 492 */     return theConverter.printBoolean(val);
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
/*     */   public static String printByte(byte val) {
/* 504 */     return theConverter.printByte(val);
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
/*     */   public static String printQName(QName val, NamespaceContext nsc) {
/* 521 */     return theConverter.printQName(val, nsc);
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
/*     */   public static String printDateTime(Calendar val) {
/* 534 */     return theConverter.printDateTime(val);
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
/*     */   public static String printBase64Binary(byte[] val) {
/* 547 */     return theConverter.printBase64Binary(val);
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
/*     */   public static String printHexBinary(byte[] val) {
/* 560 */     return theConverter.printHexBinary(val);
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
/*     */   public static String printUnsignedInt(long val) {
/* 572 */     return theConverter.printUnsignedInt(val);
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
/*     */   public static String printUnsignedShort(int val) {
/* 584 */     return theConverter.printUnsignedShort(val);
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
/*     */   public static String printTime(Calendar val) {
/* 597 */     return theConverter.printTime(val);
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
/*     */   public static String printDate(Calendar val) {
/* 610 */     return theConverter.printDate(val);
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
/*     */   public static String printAnySimpleType(String val) {
/* 622 */     return theConverter.printAnySimpleType(val);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\DatatypeConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */