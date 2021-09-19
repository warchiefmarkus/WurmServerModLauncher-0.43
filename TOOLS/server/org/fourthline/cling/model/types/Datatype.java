/*     */ package org.fourthline.cling.model.types;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Datatype<V>
/*     */ {
/*     */   boolean isHandlingJavaType(Class paramClass);
/*     */   
/*     */   Builtin getBuiltin();
/*     */   
/*     */   boolean isValid(V paramV);
/*     */   
/*     */   String getString(V paramV) throws InvalidValueException;
/*     */   
/*     */   V valueOf(String paramString) throws InvalidValueException;
/*     */   
/*     */   String getDisplayString();
/*     */   
/*     */   public enum Default
/*     */   {
/*  44 */     BOOLEAN((String)Boolean.class, Datatype.Builtin.BOOLEAN),
/*  45 */     BOOLEAN_PRIMITIVE((String)boolean.class, Datatype.Builtin.BOOLEAN),
/*  46 */     SHORT((String)Short.class, Datatype.Builtin.I2_SHORT),
/*  47 */     SHORT_PRIMITIVE((String)short.class, Datatype.Builtin.I2_SHORT),
/*  48 */     INTEGER((String)Integer.class, Datatype.Builtin.I4),
/*  49 */     INTEGER_PRIMITIVE((String)int.class, Datatype.Builtin.I4),
/*  50 */     UNSIGNED_INTEGER_ONE_BYTE((String)UnsignedIntegerOneByte.class, Datatype.Builtin.UI1),
/*  51 */     UNSIGNED_INTEGER_TWO_BYTES((String)UnsignedIntegerTwoBytes.class, Datatype.Builtin.UI2),
/*  52 */     UNSIGNED_INTEGER_FOUR_BYTES((String)UnsignedIntegerFourBytes.class, Datatype.Builtin.UI4),
/*  53 */     FLOAT((String)Float.class, Datatype.Builtin.R4),
/*  54 */     FLOAT_PRIMITIVE((String)float.class, Datatype.Builtin.R4),
/*  55 */     DOUBLE((String)Double.class, Datatype.Builtin.FLOAT),
/*  56 */     DOUBLE_PRIMTIIVE((String)double.class, Datatype.Builtin.FLOAT),
/*  57 */     CHAR((String)Character.class, Datatype.Builtin.CHAR),
/*  58 */     CHAR_PRIMITIVE((String)char.class, Datatype.Builtin.CHAR),
/*  59 */     STRING((String)String.class, Datatype.Builtin.STRING),
/*  60 */     CALENDAR((String)Calendar.class, Datatype.Builtin.DATETIME),
/*  61 */     BYTES((String)byte[].class, Datatype.Builtin.BIN_BASE64),
/*  62 */     URI((String)URI.class, Datatype.Builtin.URI);
/*     */     
/*     */     private Class javaType;
/*     */     private Datatype.Builtin builtinType;
/*     */     
/*     */     Default(Class javaType, Datatype.Builtin builtinType) {
/*  68 */       this.javaType = javaType;
/*  69 */       this.builtinType = builtinType;
/*     */     }
/*     */     
/*     */     public Class getJavaType() {
/*  73 */       return this.javaType;
/*     */     }
/*     */     
/*     */     public Datatype.Builtin getBuiltinType() {
/*  77 */       return this.builtinType;
/*     */     }
/*     */     
/*     */     public static Default getByJavaType(Class javaType) {
/*  81 */       for (Default d : values()) {
/*  82 */         if (d.getJavaType().equals(javaType)) {
/*  83 */           return d;
/*     */         }
/*     */       } 
/*  86 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       return getJavaType() + " => " + getBuiltinType();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Builtin
/*     */   {
/* 100 */     UI1("ui1", new UnsignedIntegerOneByteDatatype()),
/* 101 */     UI2("ui2", new UnsignedIntegerTwoBytesDatatype()),
/* 102 */     UI4("ui4", new UnsignedIntegerFourBytesDatatype()),
/* 103 */     I1("i1", new IntegerDatatype(1)),
/* 104 */     I2("i2", new IntegerDatatype(2)),
/* 105 */     I2_SHORT("i2", new ShortDatatype()),
/* 106 */     I4("i4", new IntegerDatatype(4)),
/* 107 */     INT("int", new IntegerDatatype(4)),
/* 108 */     R4("r4", new FloatDatatype()),
/* 109 */     R8("r8", new DoubleDatatype()),
/* 110 */     NUMBER("number", new DoubleDatatype()),
/* 111 */     FIXED144("fixed.14.4", new DoubleDatatype()),
/* 112 */     FLOAT("float", new DoubleDatatype()),
/* 113 */     CHAR("char", new CharacterDatatype()),
/* 114 */     STRING("string", new StringDatatype()),
/* 115 */     DATE("date", new DateTimeDatatype(new String[] { "yyyy-MM-dd" }, "yyyy-MM-dd")),
/*     */ 
/*     */ 
/*     */     
/* 119 */     DATETIME("dateTime", new DateTimeDatatype(new String[] { "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss" }, "yyyy-MM-dd'T'HH:mm:ss")),
/*     */ 
/*     */ 
/*     */     
/* 123 */     DATETIME_TZ("dateTime.tz", new DateTimeDatatype(new String[] { "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssZ" }, "yyyy-MM-dd'T'HH:mm:ssZ")),
/*     */ 
/*     */ 
/*     */     
/* 127 */     TIME("time", new DateTimeDatatype(new String[] { "HH:mm:ss" }, "HH:mm:ss")),
/*     */ 
/*     */ 
/*     */     
/* 131 */     TIME_TZ("time.tz", new DateTimeDatatype(new String[] { "HH:mm:ssZ", "HH:mm:ss" }, "HH:mm:ssZ")),
/*     */ 
/*     */ 
/*     */     
/* 135 */     BOOLEAN("boolean", new BooleanDatatype()),
/* 136 */     BIN_BASE64("bin.base64", new Base64Datatype()),
/* 137 */     BIN_HEX("bin.hex", new BinHexDatatype()),
/* 138 */     URI("uri", new URIDatatype()),
/* 139 */     UUID("uuid", new StringDatatype());
/*     */     
/* 141 */     private static Map<String, Builtin> byName = new HashMap<String, Builtin>()
/*     */       {
/*     */       
/*     */       };
/*     */     
/*     */     private String descriptorName;
/*     */     private Datatype datatype;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     <VT> Builtin(String descriptorName, AbstractDatatype<VT> datatype) {
/* 154 */       datatype.setBuiltin(this);
/* 155 */       this.descriptorName = descriptorName;
/* 156 */       this.datatype = datatype;
/*     */     }
/*     */     
/*     */     public String getDescriptorName() {
/* 160 */       return this.descriptorName;
/*     */     }
/*     */     
/*     */     public Datatype getDatatype() {
/* 164 */       return this.datatype;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Builtin getByDescriptorName(String descriptorName) {
/* 171 */       if (descriptorName == null) return null; 
/* 172 */       return byName.get(descriptorName.toLowerCase(Locale.ROOT));
/*     */     }
/*     */     
/*     */     public static boolean isNumeric(Builtin builtin) {
/* 176 */       return (builtin != null && (builtin
/* 177 */         .equals(UI1) || builtin
/* 178 */         .equals(UI2) || builtin
/* 179 */         .equals(UI4) || builtin
/* 180 */         .equals(I1) || builtin
/* 181 */         .equals(I2) || builtin
/* 182 */         .equals(I4) || builtin
/* 183 */         .equals(INT)));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\Datatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */