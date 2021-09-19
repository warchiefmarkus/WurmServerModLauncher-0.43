/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.TODO;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.core.ArrayInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
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
/*     */ public class ArrayInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   extends TypeInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements ArrayInfo<TypeT, ClassDeclT>, Location
/*     */ {
/*     */   private final NonElement<TypeT, ClassDeclT> itemType;
/*     */   private final QName typeName;
/*     */   private final TypeT arrayType;
/*     */   
/*     */   public ArrayInfoImpl(ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder, Locatable upstream, TypeT arrayType) {
/*  71 */     super(builder, upstream);
/*  72 */     this.arrayType = arrayType;
/*  73 */     TypeT componentType = (TypeT)nav().getComponentType(arrayType);
/*  74 */     this.itemType = builder.getTypeInfo(componentType, this);
/*     */     
/*  76 */     QName n = this.itemType.getTypeName();
/*  77 */     if (n == null) {
/*  78 */       builder.reportError(new IllegalAnnotationException(Messages.ANONYMOUS_ARRAY_ITEM.format(new Object[] { nav().getTypeName(componentType) }, ), this));
/*     */       
/*  80 */       n = new QName("#dummy");
/*     */     } 
/*  82 */     this.typeName = calcArrayTypeName(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QName calcArrayTypeName(QName n) {
/*     */     String uri;
/*  90 */     if (n.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema")) {
/*  91 */       TODO.checkSpec("this URI");
/*  92 */       uri = "http://jaxb.dev.java.net/array";
/*     */     } else {
/*  94 */       uri = n.getNamespaceURI();
/*  95 */     }  return new QName(uri, n.getLocalPart() + "Array");
/*     */   }
/*     */   
/*     */   public NonElement<TypeT, ClassDeclT> getItemType() {
/*  99 */     return this.itemType;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/* 103 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public TypeT getType() {
/* 111 */     return this.arrayType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean canBeReferencedByIDREF() {
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 125 */     return this;
/*     */   }
/*     */   public String toString() {
/* 128 */     return nav().getTypeName(this.arrayType);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ArrayInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */