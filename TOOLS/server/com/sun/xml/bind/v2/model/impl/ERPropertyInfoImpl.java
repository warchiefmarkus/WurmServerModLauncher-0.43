/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import javax.xml.bind.annotation.XmlElementWrapper;
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
/*     */ abstract class ERPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   extends PropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */ {
/*     */   private final QName xmlName;
/*     */   private final boolean wrapperNillable;
/*     */   private final boolean wrapperRequired;
/*     */   
/*     */   public ERPropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> classInfo, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> propertySeed) {
/*  53 */     super(classInfo, propertySeed);
/*     */     
/*  55 */     XmlElementWrapper e = (XmlElementWrapper)this.seed.readAnnotation(XmlElementWrapper.class);
/*     */     
/*  57 */     boolean nil = false;
/*  58 */     boolean required = false;
/*  59 */     if (!isCollection()) {
/*  60 */       this.xmlName = null;
/*  61 */       if (e != null) {
/*  62 */         classInfo.builder.reportError(new IllegalAnnotationException(Messages.XML_ELEMENT_WRAPPER_ON_NON_COLLECTION.format(new Object[] { nav().getClassName(this.parent.getClazz()) + '.' + this.seed.getName() }, ), (Annotation)e));
/*     */ 
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  68 */     else if (e != null) {
/*  69 */       this.xmlName = calcXmlName(e);
/*  70 */       nil = e.nillable();
/*  71 */       required = e.required();
/*     */     } else {
/*  73 */       this.xmlName = null;
/*     */     } 
/*     */     
/*  76 */     this.wrapperNillable = nil;
/*  77 */     this.wrapperRequired = required;
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
/*     */   public final QName getXmlName() {
/*  96 */     return this.xmlName;
/*     */   }
/*     */   
/*     */   public final boolean isCollectionNillable() {
/* 100 */     return this.wrapperNillable;
/*     */   }
/*     */   
/*     */   public final boolean isCollectionRequired() {
/* 104 */     return this.wrapperRequired;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ERPropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */