/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.bind.v2.model.core.AttributePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlSchema;
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
/*     */ class AttributePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   extends SingleTypePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements AttributePropertyInfo<TypeT, ClassDeclT>
/*     */ {
/*     */   private final QName xmlName;
/*     */   private final boolean isRequired;
/*     */   
/*     */   AttributePropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> seed) {
/*  59 */     super(parent, seed);
/*  60 */     XmlAttribute att = (XmlAttribute)seed.readAnnotation(XmlAttribute.class);
/*  61 */     assert att != null;
/*     */     
/*  63 */     if (att.required())
/*  64 */     { this.isRequired = true; }
/*  65 */     else { this.isRequired = nav().isPrimitive(getIndividualType()); }
/*     */     
/*  67 */     this.xmlName = calcXmlName(att);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private QName calcXmlName(XmlAttribute att) {
/*  74 */     String uri = att.namespace();
/*  75 */     String local = att.name();
/*     */ 
/*     */     
/*  78 */     if (local.equals("##default"))
/*  79 */       local = NameConverter.standard.toVariableName(getName()); 
/*  80 */     if (uri.equals("##default")) {
/*  81 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
/*     */ 
/*     */       
/*  84 */       if (xs != null) {
/*  85 */         switch (xs.attributeFormDefault()) {
/*     */           case QUALIFIED:
/*  87 */             uri = this.parent.getTypeName().getNamespaceURI();
/*  88 */             if (uri.length() == 0)
/*  89 */               uri = this.parent.builder.defaultNsUri; 
/*     */             break;
/*     */           case UNQUALIFIED:
/*     */           case UNSET:
/*  93 */             uri = ""; break;
/*     */         } 
/*     */       } else {
/*  96 */         uri = "";
/*     */       } 
/*     */     } 
/*  99 */     return new QName(uri.intern(), local.intern());
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 103 */     return this.isRequired;
/*     */   }
/*     */   
/*     */   public final QName getXmlName() {
/* 107 */     return this.xmlName;
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/* 111 */     return PropertyKind.ATTRIBUTE;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\AttributePropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */