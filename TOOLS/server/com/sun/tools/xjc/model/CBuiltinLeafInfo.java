/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.runtime.ZeroOneBooleanAdapter;
/*     */ import com.sun.tools.xjc.util.NamespaceContextAdapter;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.impl.BuiltinLeafInfoImpl;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import java.awt.Image;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
/*     */ import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Source;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CBuiltinLeafInfo
/*     */   extends BuiltinLeafInfoImpl<NType, NClass>
/*     */   implements CNonElement
/*     */ {
/*     */   private final ID id;
/*     */   
/*     */   private CBuiltinLeafInfo(NType typeToken, QName typeName, ID id) {
/* 112 */     super(typeToken, new QName[] { typeName });
/* 113 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType toType(Outline o, Aspect aspect) {
/* 120 */     return ((NType)getType()).toType(o, aspect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isCollection() {
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public CNonElement getInfo() {
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public ID idUse() {
/* 141 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 148 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public final CAdapter getAdapterUse() {
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 157 */     return Model.EMPTY_LOCATOR;
/*     */   }
/*     */   
/*     */   public final XSComponent getSchemaComponent() {
/* 161 */     throw new UnsupportedOperationException("TODO. If you hit this, let us know.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeUse makeCollection() {
/* 168 */     return TypeUseFactory.makeCollection(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeUse makeAdapted(Class<? extends XmlAdapter> adapter, boolean copy) {
/* 175 */     return TypeUseFactory.adapt(this, adapter, copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeUse makeMimeTyped(MimeType mt) {
/* 182 */     return TypeUseFactory.makeMimeTyped(this, mt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Builtin
/*     */     extends CBuiltinLeafInfo
/*     */   {
/*     */     protected Builtin(Class c, String typeName) {
/* 191 */       this(c, typeName, ID.NONE);
/*     */     }
/*     */     protected Builtin(Class c, String typeName, ID id) {
/* 194 */       super((NType)NavigatorImpl.theInstance.ref(c), new QName("http://www.w3.org/2001/XMLSchema", typeName), id);
/* 195 */       LEAVES.put(getType(), this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CCustomizations getCustomizations() {
/* 202 */       return CCustomizations.EMPTY;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NoConstantBuiltin extends Builtin {
/*     */     public NoConstantBuiltin(Class c, String typeName) {
/* 208 */       super(c, typeName);
/*     */     }
/*     */     public JExpression createConstant(Outline outline, XmlString lexical) {
/* 211 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public static final Map<NType, CBuiltinLeafInfo> LEAVES = new HashMap<NType, CBuiltinLeafInfo>();
/*     */ 
/*     */   
/* 221 */   public static final CBuiltinLeafInfo ANYTYPE = new NoConstantBuiltin(Object.class, "anyType");
/* 222 */   public static final CBuiltinLeafInfo STRING = new Builtin(String.class, "string") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 224 */         return JExpr.lit(lexical.value);
/*     */       }
/*     */     };
/* 227 */   public static final CBuiltinLeafInfo BOOLEAN = new Builtin(Boolean.class, "boolean") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 229 */         return JExpr.lit(DatatypeConverterImpl._parseBoolean(lexical.value));
/*     */       }
/*     */     };
/* 232 */   public static final CBuiltinLeafInfo INT = new Builtin(Integer.class, "int") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 234 */         return JExpr.lit(DatatypeConverterImpl._parseInt(lexical.value));
/*     */       }
/*     */     };
/* 237 */   public static final CBuiltinLeafInfo LONG = new Builtin(Long.class, "long") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 239 */         return JExpr.lit(DatatypeConverterImpl._parseLong(lexical.value));
/*     */       }
/*     */     };
/* 242 */   public static final CBuiltinLeafInfo BYTE = new Builtin(Byte.class, "byte") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 244 */         return (JExpression)JExpr.cast((JType)(outline.getCodeModel()).BYTE, JExpr.lit(DatatypeConverterImpl._parseByte(lexical.value)));
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 249 */   public static final CBuiltinLeafInfo SHORT = new Builtin(Short.class, "short") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 251 */         return (JExpression)JExpr.cast((JType)(outline.getCodeModel()).SHORT, JExpr.lit(DatatypeConverterImpl._parseShort(lexical.value)));
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 256 */   public static final CBuiltinLeafInfo FLOAT = new Builtin(Float.class, "float") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 258 */         return JExpr.lit(DatatypeConverterImpl._parseFloat(lexical.value));
/*     */       }
/*     */     };
/* 261 */   public static final CBuiltinLeafInfo DOUBLE = new Builtin(Double.class, "double") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 263 */         return JExpr.lit(DatatypeConverterImpl._parseDouble(lexical.value));
/*     */       }
/*     */     };
/* 266 */   public static final CBuiltinLeafInfo QNAME = new Builtin(QName.class, "QName") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 268 */         QName qn = DatatypeConverterImpl._parseQName(lexical.value, (NamespaceContext)new NamespaceContextAdapter(lexical));
/* 269 */         return (JExpression)JExpr._new(outline.getCodeModel().ref(QName.class)).arg(qn.getNamespaceURI()).arg(qn.getLocalPart()).arg(qn.getPrefix());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   public static final CBuiltinLeafInfo CALENDAR = new NoConstantBuiltin(XMLGregorianCalendar.class, "\000");
/* 279 */   public static final CBuiltinLeafInfo DURATION = new NoConstantBuiltin(Duration.class, "duration");
/*     */   
/* 281 */   public static final CBuiltinLeafInfo BIG_INTEGER = new Builtin(BigInteger.class, "integer") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 283 */         return (JExpression)JExpr._new(outline.getCodeModel().ref(BigInteger.class)).arg(lexical.value.trim());
/*     */       }
/*     */     };
/*     */   
/* 287 */   public static final CBuiltinLeafInfo BIG_DECIMAL = new Builtin(BigDecimal.class, "decimal") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 289 */         return (JExpression)JExpr._new(outline.getCodeModel().ref(BigDecimal.class)).arg(lexical.value.trim());
/*     */       }
/*     */     };
/*     */   
/* 293 */   public static final CBuiltinLeafInfo BASE64_BYTE_ARRAY = new Builtin(byte[].class, "base64Binary") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 295 */         return (JExpression)outline.getCodeModel().ref(DatatypeConverter.class).staticInvoke("parseBase64Binary").arg(lexical.value);
/*     */       }
/*     */     };
/*     */   
/* 299 */   public static final CBuiltinLeafInfo DATA_HANDLER = new NoConstantBuiltin(DataHandler.class, "base64Binary");
/* 300 */   public static final CBuiltinLeafInfo IMAGE = new NoConstantBuiltin(Image.class, "base64Binary");
/* 301 */   public static final CBuiltinLeafInfo XML_SOURCE = new NoConstantBuiltin(Source.class, "base64Binary");
/*     */   
/* 303 */   public static final TypeUse HEXBIN_BYTE_ARRAY = STRING.makeAdapted((Class)HexBinaryAdapter.class, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 309 */   public static final TypeUse TOKEN = STRING.makeAdapted((Class)CollapsedStringAdapter.class, false);
/*     */ 
/*     */   
/* 312 */   public static final TypeUse NORMALIZED_STRING = STRING.makeAdapted((Class)NormalizedStringAdapter.class, false);
/*     */ 
/*     */   
/* 315 */   public static final TypeUse ID = TypeUseFactory.makeID(TOKEN, ID.ID);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   public static final TypeUse BOOLEAN_ZERO_OR_ONE = STRING.makeAdapted((Class)ZeroOneBooleanAdapter.class, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 329 */   public static final TypeUse IDREF = TypeUseFactory.makeID(ANYTYPE, ID.IDREF);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 334 */   public static final TypeUse STRING_LIST = STRING.makeCollection();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CBuiltinLeafInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */