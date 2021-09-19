/*     */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.datatype.xsd.Base64BinaryType;
/*     */ import com.sun.msv.datatype.xsd.BooleanType;
/*     */ import com.sun.msv.datatype.xsd.ByteType;
/*     */ import com.sun.msv.datatype.xsd.DateTimeType;
/*     */ import com.sun.msv.datatype.xsd.DateType;
/*     */ import com.sun.msv.datatype.xsd.DoubleType;
/*     */ import com.sun.msv.datatype.xsd.FloatType;
/*     */ import com.sun.msv.datatype.xsd.HexBinaryType;
/*     */ import com.sun.msv.datatype.xsd.IDREFType;
/*     */ import com.sun.msv.datatype.xsd.IDType;
/*     */ import com.sun.msv.datatype.xsd.IntType;
/*     */ import com.sun.msv.datatype.xsd.IntegerType;
/*     */ import com.sun.msv.datatype.xsd.LongType;
/*     */ import com.sun.msv.datatype.xsd.NormalizedStringType;
/*     */ import com.sun.msv.datatype.xsd.NumberType;
/*     */ import com.sun.msv.datatype.xsd.QnameType;
/*     */ import com.sun.msv.datatype.xsd.ShortType;
/*     */ import com.sun.msv.datatype.xsd.SimpleURType;
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.TimeType;
/*     */ import com.sun.msv.datatype.xsd.TokenType;
/*     */ import com.sun.msv.datatype.xsd.UnsignedByteType;
/*     */ import com.sun.msv.datatype.xsd.UnsignedIntType;
/*     */ import com.sun.msv.datatype.xsd.UnsignedShortType;
/*     */ import com.sun.msv.datatype.xsd.WhiteSpaceFacet;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.datatype.xsd.XSDatatypeImpl;
/*     */ import com.sun.tools.xjc.generator.util.WhitespaceNormalizer;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.id.IDREFTransducer;
/*     */ import com.sun.tools.xjc.grammar.id.IDTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.DateTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.QNameTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.UserTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.WhitespaceTransducer;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Calendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuiltinDatatypeTransducerFactory
/*     */ {
/*     */   private static Transducer create(JCodeModel model, Class type) {
/*     */     try {
/*  73 */       Method m = type.getMethod("load", new Class[] { String.class });
/*  74 */       String className = type.getName();
/*     */ 
/*     */       
/*  77 */       if (!Modifier.isStatic(m.getModifiers()))
/*     */       {
/*  79 */         throw new JAXBAssertionError();
/*     */       }
/*     */ 
/*     */       
/*  83 */       return (Transducer)new UserTransducer((JType)model.ref(m.getReturnType()), className + ".load", className + ".save");
/*     */     }
/*  85 */     catch (NoSuchMethodException e) {
/*  86 */       throw new NoSuchMethodError("cannot find the load method for " + type.getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Transducer create(JType returnType, String stem) {
/*  92 */     return (Transducer)new UserTransducer(returnType, "javax.xml.bind.DatatypeConverter.parse" + stem, "javax.xml.bind.DatatypeConverter.print" + stem);
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
/*     */   public static Transducer get(AnnotatedGrammar grammar, XSDatatype dt) {
/* 111 */     Transducer base = getWithoutWhitespaceNormalization(grammar, dt);
/*     */ 
/*     */     
/* 114 */     if (dt instanceof XSDatatypeImpl) {
/* 115 */       return WhitespaceTransducer.create(base, grammar.codeModel, ((XSDatatypeImpl)dt).whiteSpace);
/*     */     }
/* 117 */     WhiteSpaceFacet wsf = (WhiteSpaceFacet)dt.getFacetObject("whiteSpace");
/* 118 */     if (wsf != null) {
/* 119 */       return WhitespaceTransducer.create(base, grammar.codeModel, wsf.whiteSpace);
/*     */     }
/* 121 */     return WhitespaceTransducer.create(base, grammar.codeModel, WhitespaceNormalizer.COLLAPSE);
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
/*     */   public static Transducer getWithoutWhitespaceNormalization(AnnotatedGrammar grammar, XSDatatype dt) {
/* 137 */     return (Transducer)new Object(_getWithoutWhitespaceNormalization(grammar, dt));
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
/*     */   private static Transducer _getWithoutWhitespaceNormalization(AnnotatedGrammar grammar, XSDatatype dt) {
/* 149 */     JCodeModel codeModel = grammar.codeModel;
/*     */     
/* 151 */     if (dt.getVariety() != 1)
/*     */     {
/* 153 */       throw new JAXBAssertionError();
/*     */     }
/*     */     
/* 156 */     if (dt == SimpleURType.theInstance) {
/* 157 */       return (Transducer)new IdentityTransducer(codeModel);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     if (dt == StringType.theInstance || dt == NormalizedStringType.theInstance || dt == TokenType.theInstance)
/*     */     {
/*     */       
/* 166 */       return (Transducer)new IdentityTransducer(codeModel);
/*     */     }
/* 168 */     if (dt == IDType.theInstance) {
/* 169 */       return (Transducer)new IDTransducer(codeModel, grammar.defaultSymbolSpace);
/*     */     }
/* 171 */     if (dt == IDREFType.theInstance) {
/* 172 */       return (Transducer)new IDREFTransducer(codeModel, grammar.defaultSymbolSpace, false);
/*     */     }
/* 174 */     if (dt == BooleanType.theInstance) {
/* 175 */       return create((JType)codeModel.BOOLEAN, "Boolean");
/*     */     }
/* 177 */     if (dt == Base64BinaryType.theInstance) {
/* 178 */       return create(codeModel, Base64BinaryType.class);
/*     */     }
/* 180 */     if (dt == HexBinaryType.theInstance) {
/* 181 */       return create(codeModel, HexBinaryType.class);
/*     */     }
/* 183 */     if (dt == FloatType.theInstance) {
/* 184 */       return create((JType)codeModel.FLOAT, "Float");
/*     */     }
/* 186 */     if (dt == DoubleType.theInstance) {
/* 187 */       return create((JType)codeModel.DOUBLE, "Double");
/*     */     }
/* 189 */     if (dt == NumberType.theInstance) {
/* 190 */       return create((JType)codeModel.ref(BigDecimal.class), "Decimal");
/*     */     }
/* 192 */     if (dt == IntegerType.theInstance) {
/* 193 */       return create((JType)codeModel.ref(BigInteger.class), "Integer");
/*     */     }
/* 195 */     if (dt == LongType.theInstance || dt == UnsignedIntType.theInstance)
/*     */     {
/* 197 */       return create((JType)codeModel.LONG, "Long");
/*     */     }
/* 199 */     if (dt == IntType.theInstance || dt == UnsignedShortType.theInstance)
/*     */     {
/* 201 */       return create((JType)codeModel.INT, "Int");
/*     */     }
/* 203 */     if (dt == ShortType.theInstance || dt == UnsignedByteType.theInstance)
/*     */     {
/* 205 */       return create((JType)codeModel.SHORT, "Short");
/*     */     }
/* 207 */     if (dt == ByteType.theInstance) {
/* 208 */       return create((JType)codeModel.BYTE, "Byte");
/*     */     }
/* 210 */     if (dt == QnameType.theInstance) {
/* 211 */       return (Transducer)new QNameTransducer(codeModel);
/*     */     }
/* 213 */     if (dt == DateType.theInstance) {
/* 214 */       return create((JType)codeModel.ref(Calendar.class), "Date");
/*     */     }
/*     */     
/* 217 */     if (dt == TimeType.theInstance) {
/* 218 */       return (Transducer)new DateTransducer(codeModel, codeModel.ref(TimeType.class));
/*     */     }
/* 220 */     if (dt == DateTimeType.theInstance) {
/* 221 */       return (Transducer)new DateTransducer(codeModel, codeModel.ref(DateTimeType.class));
/*     */     }
/*     */ 
/*     */     
/* 225 */     return _getWithoutWhitespaceNormalization(grammar, dt.getBaseType());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\BuiltinDatatypeTransducerFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */