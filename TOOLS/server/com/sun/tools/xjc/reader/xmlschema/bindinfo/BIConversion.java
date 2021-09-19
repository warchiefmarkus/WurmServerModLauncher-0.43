/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.model.CAdapter;
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ClassSelector;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public abstract class BIConversion
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @Deprecated
/*     */   public BIConversion(Locator loc) {
/*  83 */     super(loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BIConversion() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypeUse getTypeUse(XSSimpleType paramXSSimpleType);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getName() {
/* 102 */     return NAME;
/*     */   }
/*     */   
/* 105 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "conversion");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Static
/*     */     extends BIConversion
/*     */   {
/*     */     private final TypeUse transducer;
/*     */ 
/*     */ 
/*     */     
/*     */     public Static(Locator loc, TypeUse transducer) {
/* 118 */       super(loc);
/* 119 */       this.transducer = transducer;
/*     */     }
/*     */     
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/* 123 */       return this.transducer;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlRootElement(name = "javaType")
/*     */   public static class User
/*     */     extends BIConversion
/*     */   {
/*     */     @XmlAttribute
/*     */     private String parseMethod;
/*     */ 
/*     */     
/*     */     @XmlAttribute
/*     */     private String printMethod;
/*     */     
/*     */     @XmlAttribute(name = "name")
/* 141 */     private String type = "java.lang.String";
/*     */ 
/*     */     
/*     */     private JType inMemoryType;
/*     */     
/*     */     private TypeUse typeUse;
/*     */ 
/*     */     
/*     */     public User(Locator loc, String parseMethod, String printMethod, JType inMemoryType) {
/* 150 */       super(loc);
/* 151 */       this.parseMethod = parseMethod;
/* 152 */       this.printMethod = printMethod;
/* 153 */       this.inMemoryType = inMemoryType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public User() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/* 165 */       if (this.typeUse != null) {
/* 166 */         return this.typeUse;
/*     */       }
/* 168 */       JCodeModel cm = getCodeModel();
/*     */       
/* 170 */       if (this.inMemoryType == null) {
/* 171 */         this.inMemoryType = TypeUtil.getType(cm, this.type, (ErrorReceiver)Ring.get(ErrorReceiver.class), getLocation());
/*     */       }
/* 173 */       JDefinedClass adapter = generateAdapter(parseMethodFor(owner), printMethodFor(owner), owner);
/*     */ 
/*     */       
/* 176 */       this.typeUse = TypeUseFactory.adapt((TypeUse)CBuiltinLeafInfo.STRING, new CAdapter((JClass)adapter));
/*     */       
/* 178 */       return this.typeUse;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private JDefinedClass generateAdapter(String parseMethod, String printMethod, XSSimpleType owner) {
/*     */       JExpression inv;
/* 185 */       JDefinedClass adapter = null;
/*     */       
/* 187 */       int id = 1;
/* 188 */       while (adapter == null) {
/*     */         try {
/* 190 */           JPackage pkg = ((ClassSelector)Ring.get(ClassSelector.class)).getClassScope().getOwnerPackage();
/* 191 */           adapter = pkg._class("Adapter" + id);
/* 192 */         } catch (JClassAlreadyExistsException e) {
/*     */ 
/*     */ 
/*     */           
/* 196 */           id++;
/*     */         } 
/*     */       } 
/*     */       
/* 200 */       JClass bim = this.inMemoryType.boxify();
/*     */       
/* 202 */       adapter._extends(getCodeModel().ref(XmlAdapter.class).narrow(String.class).narrow(bim));
/*     */       
/* 204 */       JMethod unmarshal = adapter.method(1, (JType)bim, "unmarshal");
/* 205 */       JVar $value = unmarshal.param(String.class, "value");
/*     */ 
/*     */ 
/*     */       
/* 209 */       if (parseMethod.equals("new")) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 214 */         JInvocation jInvocation = JExpr._new(bim).arg((JExpression)$value);
/*     */       } else {
/* 216 */         int i = parseMethod.lastIndexOf('.');
/* 217 */         if (i < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 223 */           JInvocation jInvocation = bim.staticInvoke(parseMethod).arg((JExpression)$value);
/*     */         } else {
/* 225 */           inv = JExpr.direct(parseMethod + "(value)");
/*     */         } 
/*     */       } 
/* 228 */       unmarshal.body()._return(inv);
/*     */ 
/*     */       
/* 231 */       JMethod marshal = adapter.method(1, String.class, "marshal");
/* 232 */       $value = marshal.param((JType)bim, "value");
/*     */       
/* 234 */       if (printMethod.startsWith("javax.xml.bind.DatatypeConverter."))
/*     */       {
/*     */         
/* 237 */         marshal.body()._if($value.eq(JExpr._null()))._then()._return(JExpr._null());
/*     */       }
/*     */       
/* 240 */       int idx = printMethod.lastIndexOf('.');
/* 241 */       if (idx < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 246 */         JInvocation jInvocation = $value.invoke(printMethod);
/*     */ 
/*     */         
/* 249 */         JConditional jcon = marshal.body()._if($value.eq(JExpr._null()));
/* 250 */         jcon._then()._return(JExpr._null());
/*     */       
/*     */       }
/* 253 */       else if (this.printMethod == null) {
/*     */         
/* 255 */         JType t = this.inMemoryType.unboxify();
/* 256 */         inv = JExpr.direct(printMethod + "((" + findBaseConversion(owner).toLowerCase() + ")(" + t.fullName() + ")value)");
/*     */       } else {
/* 258 */         inv = JExpr.direct(printMethod + "(value)");
/*     */       } 
/* 260 */       marshal.body()._return(inv);
/*     */       
/* 262 */       return adapter;
/*     */     }
/*     */     
/*     */     private String printMethodFor(XSSimpleType owner) {
/* 266 */       if (this.printMethod != null) return this.printMethod;
/*     */       
/* 268 */       if (this.inMemoryType.unboxify().isPrimitive()) {
/* 269 */         String method = getConversionMethod("print", owner);
/* 270 */         if (method != null) {
/* 271 */           return method;
/*     */         }
/*     */       } 
/* 274 */       return "toString";
/*     */     }
/*     */     
/*     */     private String parseMethodFor(XSSimpleType owner) {
/* 278 */       if (this.parseMethod != null) return this.parseMethod;
/*     */       
/* 280 */       if (this.inMemoryType.unboxify().isPrimitive()) {
/* 281 */         String method = getConversionMethod("parse", owner);
/* 282 */         if (method != null)
/*     */         {
/* 284 */           return '(' + this.inMemoryType.unboxify().fullName() + ')' + method;
/*     */         }
/*     */       } 
/*     */       
/* 288 */       return "new";
/*     */     }
/*     */     
/* 291 */     private static final String[] knownBases = new String[] { "Float", "Double", "Byte", "Short", "Int", "Long", "Boolean" };
/*     */ 
/*     */ 
/*     */     
/*     */     private String getConversionMethod(String methodPrefix, XSSimpleType owner) {
/* 296 */       String bc = findBaseConversion(owner);
/* 297 */       if (bc == null) return null;
/*     */       
/* 299 */       return DatatypeConverter.class.getName() + '.' + methodPrefix + bc;
/*     */     }
/*     */ 
/*     */     
/*     */     private String findBaseConversion(XSSimpleType owner) {
/* 304 */       for (XSSimpleType st = owner; st != null; st = st.getSimpleBaseType()) {
/* 305 */         if ("http://www.w3.org/2001/XMLSchema".equals(st.getTargetNamespace())) {
/*     */ 
/*     */           
/* 308 */           String name = st.getName().intern();
/* 309 */           for (String s : knownBases) {
/* 310 */             if (name.equalsIgnoreCase(s))
/* 311 */               return s; 
/*     */           } 
/*     */         } 
/* 314 */       }  return null;
/*     */     }
/*     */     public QName getName() {
/* 317 */       return NAME;
/*     */     }
/*     */     
/* 320 */     public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "javaType");
/*     */   }
/*     */   
/*     */   @XmlRootElement(name = "javaType", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   public static class UserAdapter
/*     */     extends BIConversion {
/*     */     @XmlAttribute(name = "name")
/* 327 */     private String type = null;
/*     */     
/*     */     @XmlAttribute
/* 330 */     private String adapter = null;
/*     */     private TypeUse typeUse;
/*     */     
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/*     */       JDefinedClass jDefinedClass;
/* 335 */       if (this.typeUse != null) {
/* 336 */         return this.typeUse;
/*     */       }
/* 338 */       JCodeModel cm = getCodeModel();
/*     */ 
/*     */       
/*     */       try {
/* 342 */         jDefinedClass = cm._class(this.adapter);
/* 343 */         jDefinedClass.hide();
/* 344 */         jDefinedClass._extends(cm.ref(XmlAdapter.class).narrow(String.class).narrow(cm.ref(this.type)));
/*     */       }
/* 346 */       catch (JClassAlreadyExistsException e) {
/* 347 */         jDefinedClass = e.getExistingClass();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 352 */       this.typeUse = TypeUseFactory.adapt((TypeUse)CBuiltinLeafInfo.STRING, new CAdapter((JClass)jDefinedClass));
/*     */ 
/*     */ 
/*     */       
/* 356 */       return this.typeUse;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIConversion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */