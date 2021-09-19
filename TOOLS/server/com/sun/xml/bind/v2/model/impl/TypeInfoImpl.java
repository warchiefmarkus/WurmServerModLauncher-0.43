/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.bind.annotation.XmlType;
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
/*     */ abstract class TypeInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements TypeInfo<TypeT, ClassDeclT>, Locatable
/*     */ {
/*     */   private final Locatable upstream;
/*     */   protected final TypeInfoSetImpl<TypeT, ClassDeclT, FieldT, MethodT> owner;
/*     */   protected ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder;
/*     */   
/*     */   protected TypeInfoImpl(ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder, Locatable upstream) {
/*  81 */     this.builder = builder;
/*  82 */     this.owner = builder.typeInfoSet;
/*  83 */     this.upstream = upstream;
/*     */   }
/*     */   
/*     */   public Locatable getUpstream() {
/*  87 */     return this.upstream;
/*     */   }
/*     */   
/*     */   void link() {
/*  91 */     this.builder = null;
/*     */   }
/*     */   
/*     */   protected final Navigator<TypeT, ClassDeclT, FieldT, MethodT> nav() {
/*  95 */     return this.owner.nav;
/*     */   }
/*     */   
/*     */   protected final AnnotationReader<TypeT, ClassDeclT, FieldT, MethodT> reader() {
/*  99 */     return this.owner.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final QName parseElementName(ClassDeclT clazz) {
/* 110 */     XmlRootElement e = (XmlRootElement)reader().getClassAnnotation(XmlRootElement.class, clazz, this);
/* 111 */     if (e == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     String local = e.name();
/* 115 */     if (local.equals("##default"))
/*     */     {
/* 117 */       local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
/*     */     }
/* 119 */     String nsUri = e.namespace();
/* 120 */     if (nsUri.equals("##default")) {
/*     */       
/* 122 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, clazz, this);
/* 123 */       if (xs != null) {
/* 124 */         nsUri = xs.namespace();
/*     */       } else {
/* 126 */         nsUri = this.builder.defaultNsUri;
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     return new QName(nsUri.intern(), local.intern());
/*     */   }
/*     */   
/*     */   protected final QName parseTypeName(ClassDeclT clazz) {
/* 134 */     return parseTypeName(clazz, (XmlType)reader().getClassAnnotation(XmlType.class, clazz, this));
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
/*     */   protected final QName parseTypeName(ClassDeclT clazz, XmlType t) {
/* 149 */     String nsUri = "##default";
/* 150 */     String local = "##default";
/* 151 */     if (t != null) {
/* 152 */       nsUri = t.namespace();
/* 153 */       local = t.name();
/*     */     } 
/*     */     
/* 156 */     if (local.length() == 0) {
/* 157 */       return null;
/*     */     }
/*     */     
/* 160 */     if (local.equals("##default"))
/*     */     {
/* 162 */       local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
/*     */     }
/* 164 */     if (nsUri.equals("##default")) {
/*     */       
/* 166 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, clazz, this);
/* 167 */       if (xs != null) {
/* 168 */         nsUri = xs.namespace();
/*     */       } else {
/* 170 */         nsUri = this.builder.defaultNsUri;
/*     */       } 
/*     */     } 
/*     */     
/* 174 */     return new QName(nsUri.intern(), local.intern());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\TypeInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */