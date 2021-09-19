/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.ClassType;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.generator.bean.ImplStructureStrategy;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.SimpleTypeBuilder;
/*     */ import com.sun.tools.xjc.util.ReadOnlyAdapter;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlEnumValue;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlTransient;
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
/*     */ @XmlRootElement(name = "globalBindings")
/*     */ public final class BIGlobalBinding
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlTransient
/*  96 */   public NameConverter nameConverter = NameConverter.standard;
/*     */ 
/*     */   
/*     */   @XmlAttribute
/*     */   void setUnderscoreBinding(UnderscoreBinding ub) {
/* 101 */     this.nameConverter = ub.nc;
/*     */   }
/*     */   
/*     */   UnderscoreBinding getUnderscoreBinding() {
/* 105 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public JDefinedClass getSuperClass() {
/* 109 */     if (this.superClass == null) return null; 
/* 110 */     return this.superClass.getClazz(ClassType.CLASS);
/*     */   }
/*     */   
/*     */   public JDefinedClass getSuperInterface() {
/* 114 */     if (this.superInterface == null) return null; 
/* 115 */     return this.superInterface.getClazz(ClassType.INTERFACE);
/*     */   }
/*     */   
/*     */   public BIProperty getDefaultProperty() {
/* 119 */     return this.defaultProperty;
/*     */   }
/*     */   
/*     */   public boolean isJavaNamingConventionEnabled() {
/* 123 */     return this.isJavaNamingConventionEnabled;
/*     */   }
/*     */   
/*     */   public BISerializable getSerializable() {
/* 127 */     return this.serializable;
/*     */   }
/*     */   
/*     */   public boolean isGenerateElementClass() {
/* 131 */     return this.generateElementClass;
/*     */   }
/*     */   
/*     */   public boolean isChoiceContentPropertyEnabled() {
/* 135 */     return this.choiceContentProperty;
/*     */   }
/*     */   
/*     */   public int getDefaultEnumMemberSizeCap() {
/* 139 */     return this.defaultEnumMemberSizeCap;
/*     */   }
/*     */   
/*     */   public boolean isSimpleMode() {
/* 143 */     return (this.simpleMode != null);
/*     */   }
/*     */   
/*     */   public boolean isRestrictionFreshType() {
/* 147 */     return (this.treatRestrictionLikeNewType != null);
/*     */   }
/*     */   
/*     */   public EnumMemberMode getEnumMemberMode() {
/* 151 */     return this.generateEnumMemberName;
/*     */   }
/*     */   
/*     */   public boolean isSimpleTypeSubstitution() {
/* 155 */     return this.simpleTypeSubstitution;
/*     */   }
/*     */   
/*     */   public ImplStructureStrategy getCodeGenerationStrategy() {
/* 159 */     return this.codeGenerationStrategy;
/*     */   }
/*     */   
/*     */   public LocalScoping getFlattenClasses() {
/* 163 */     return this.flattenClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void errorCheck() {
/* 170 */     ErrorReceiver er = (ErrorReceiver)Ring.get(ErrorReceiver.class);
/* 171 */     for (QName n : this.enumBaseTypes) {
/* 172 */       XSSchemaSet xs = (XSSchemaSet)Ring.get(XSSchemaSet.class);
/* 173 */       XSSimpleType st = xs.getSimpleType(n.getNamespaceURI(), n.getLocalPart());
/* 174 */       if (st == null) {
/* 175 */         er.error(this.loc, Messages.ERR_UNDEFINED_SIMPLE_TYPE.format(new Object[] { n }));
/*     */         
/*     */         continue;
/*     */       } 
/* 179 */       if (!SimpleTypeBuilder.canBeMappedToTypeSafeEnum(st)) {
/* 180 */         er.error(this.loc, Messages.ERR_CANNOT_BE_BOUND_TO_SIMPLETYPE.format(new Object[] { n }));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum UnderscoreBinding
/*     */   {
/* 187 */     WORD_SEPARATOR((String)NameConverter.standard),
/*     */     
/* 189 */     CHAR_IN_WORD((String)NameConverter.jaxrpcCompatible);
/*     */     
/*     */     final NameConverter nc;
/*     */ 
/*     */     
/*     */     UnderscoreBinding(NameConverter nc) {
/* 195 */       this.nc = nc;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "enableJavaNamingConventions")
/*     */   boolean isJavaNamingConventionEnabled = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "mapSimpleTypeDef")
/*     */   boolean simpleTypeSubstitution = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlTransient
/*     */   private BIProperty defaultProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute
/*     */   private boolean fixedAttributeAsConstantProperty = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute
/* 230 */   private CollectionTypeAttribute collectionType = new CollectionTypeAttribute();
/*     */   @XmlAttribute
/*     */   void setGenerateIsSetMethod(boolean b) {
/* 233 */     this.optionalProperty = b ? OptionalPropertyMode.ISSET : OptionalPropertyMode.WRAPPER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "typesafeEnumMemberName")
/* 242 */   EnumMemberMode generateEnumMemberName = EnumMemberMode.SKIP;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "generateValueClass")
/* 248 */   ImplStructureStrategy codeGenerationStrategy = ImplStructureStrategy.BEAN_ONLY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "typesafeEnumBase")
/*     */   private Set<QName> enumBaseTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement
/* 264 */   private BISerializable serializable = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 271 */   ClassNameBean superClass = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 278 */   ClassNameBean superInterface = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(name = "simple", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 285 */   String simpleMode = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(name = "treatRestrictionLikeNewType", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 291 */   String treatRestrictionLikeNewType = null;
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute
/*     */   boolean generateElementClass = false;
/*     */ 
/*     */   
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 300 */   Boolean generateElementProperty = null;
/*     */   
/*     */   @XmlAttribute(name = "generateElementProperty")
/*     */   private void setGenerateElementPropertyStd(boolean value) {
/* 304 */     this.generateElementProperty = Boolean.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @XmlAttribute
/*     */   boolean choiceContentProperty = false;
/*     */   @XmlAttribute
/* 311 */   OptionalPropertyMode optionalProperty = OptionalPropertyMode.WRAPPER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "typesafeEnumMaxMembers")
/* 319 */   int defaultEnumMemberSizeCap = 256;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "localScoping")
/* 328 */   LocalScoping flattenClasses = LocalScoping.NESTED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlTransient
/* 336 */   private final Map<QName, BIConversion> globalConversions = new HashMap<QName, BIConversion>();
/*     */ 
/*     */   
/*     */   @XmlElement(name = "javaType")
/*     */   private void setGlobalConversions(GlobalStandardConversion[] convs) {
/* 341 */     for (GlobalStandardConversion u : convs) {
/* 342 */       this.globalConversions.put(u.xmlType, u);
/*     */     }
/*     */   }
/*     */   
/*     */   @XmlElement(name = "javaType", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   private void setGlobalConversions2(GlobalVendorConversion[] convs) {
/* 348 */     for (GlobalVendorConversion u : convs) {
/* 349 */       this.globalConversions.put(u.xmlType, u);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 359 */   String noMarshaller = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 361 */   String noUnmarshaller = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 363 */   String noValidator = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 365 */   String noValidatingUnmarshaller = null;
/*     */   @XmlElement(namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/* 367 */   TypeSubstitutionElement typeSubstitution = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(name = "serializable", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   void setXjcSerializable(BISerializable s) {
/* 375 */     this.serializable = s;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TypeSubstitutionElement
/*     */   {
/*     */     @XmlAttribute
/*     */     String type;
/*     */   }
/*     */   
/*     */   public void onSetOwner() {
/* 386 */     super.onSetOwner();
/*     */     
/* 388 */     NameConverter nc = ((Model)Ring.get(Model.class)).options.getNameConverter();
/* 389 */     if (nc != null) {
/* 390 */       this.nameConverter = nc;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(BindInfo parent) {
/* 400 */     super.setParent(parent);
/*     */     
/* 402 */     if (this.enumBaseTypes == null) {
/* 403 */       this.enumBaseTypes = Collections.singleton(new QName("http://www.w3.org/2001/XMLSchema", "string"));
/*     */     }
/* 405 */     this.defaultProperty = new BIProperty(getLocation(), null, null, null, this.collectionType, Boolean.valueOf(this.fixedAttributeAsConstantProperty), this.optionalProperty, this.generateElementProperty);
/*     */     
/* 407 */     this.defaultProperty.setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispatchGlobalConversions(XSSchemaSet schema) {
/* 415 */     for (Map.Entry<QName, BIConversion> e : this.globalConversions.entrySet()) {
/*     */       
/* 417 */       QName name = e.getKey();
/* 418 */       BIConversion conv = e.getValue();
/*     */       
/* 420 */       XSSimpleType st = schema.getSimpleType(name.getNamespaceURI(), name.getLocalPart());
/* 421 */       if (st == null) {
/* 422 */         ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(getLocation(), Messages.ERR_UNDEFINED_SIMPLE_TYPE.format(new Object[] { name }));
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 429 */       getBuilder().getOrCreateBindInfo((XSComponent)st).addDecl(conv);
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
/*     */   public boolean canBeMappedToTypeSafeEnum(QName typeName) {
/* 441 */     return this.enumBaseTypes.contains(typeName);
/*     */   }
/*     */   
/*     */   public boolean canBeMappedToTypeSafeEnum(String nsUri, String localName) {
/* 445 */     return canBeMappedToTypeSafeEnum(new QName(nsUri, localName));
/*     */   }
/*     */   
/*     */   public boolean canBeMappedToTypeSafeEnum(XSDeclaration decl) {
/* 449 */     return canBeMappedToTypeSafeEnum(decl.getTargetNamespace(), decl.getName());
/*     */   }
/*     */   
/*     */   public QName getName() {
/* 453 */     return NAME;
/* 454 */   } public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "globalBindings");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ClassNameBean
/*     */   {
/*     */     @XmlAttribute(required = true)
/*     */     String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @XmlTransient
/*     */     JDefinedClass clazz;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     JDefinedClass getClazz(ClassType t) {
/* 475 */       if (this.clazz != null) return this.clazz; 
/*     */       try {
/* 477 */         JCodeModel codeModel = (JCodeModel)Ring.get(JCodeModel.class);
/* 478 */         this.clazz = codeModel._class(this.name, t);
/* 479 */         this.clazz.hide();
/* 480 */         return this.clazz;
/* 481 */       } catch (JClassAlreadyExistsException e) {
/* 482 */         return e.getExistingClass();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ClassNameAdapter extends ReadOnlyAdapter<ClassNameBean, String> {
/*     */     public String unmarshal(BIGlobalBinding.ClassNameBean bean) throws Exception {
/* 489 */       return bean.name;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class GlobalStandardConversion extends BIConversion.User {
/*     */     @XmlAttribute
/*     */     QName xmlType;
/*     */   }
/*     */   
/*     */   static final class GlobalVendorConversion extends BIConversion.UserAdapter {
/*     */     @XmlAttribute
/*     */     QName xmlType;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIGlobalBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */