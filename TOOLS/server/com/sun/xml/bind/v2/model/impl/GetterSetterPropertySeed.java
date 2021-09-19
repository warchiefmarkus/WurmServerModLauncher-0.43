/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.beans.Introspector;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GetterSetterPropertySeed<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements PropertySeed<TypeT, ClassDeclT, FieldT, MethodT>
/*     */ {
/*     */   protected final MethodT getter;
/*     */   protected final MethodT setter;
/*     */   private ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent;
/*     */   
/*     */   GetterSetterPropertySeed(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, MethodT getter, MethodT setter) {
/*  60 */     this.parent = parent;
/*  61 */     this.getter = getter;
/*  62 */     this.setter = setter;
/*     */     
/*  64 */     if (getter == null && setter == null)
/*  65 */       throw new IllegalArgumentException(); 
/*     */   }
/*     */   
/*     */   public TypeT getRawType() {
/*  69 */     if (this.getter != null) {
/*  70 */       return (TypeT)this.parent.nav().getReturnType(this.getter);
/*     */     }
/*  72 */     return (TypeT)this.parent.nav().getMethodParameters(this.setter)[0];
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A readAnnotation(Class<A> annotation) {
/*  76 */     return (A)this.parent.reader().getMethodAnnotation(annotation, this.getter, this.setter, this);
/*     */   }
/*     */   
/*     */   public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/*  80 */     return this.parent.reader().hasMethodAnnotation(annotationType, getName(), this.getter, this.setter, this);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  84 */     if (this.getter != null) {
/*  85 */       return getName(this.getter);
/*     */     }
/*  87 */     return getName(this.setter);
/*     */   }
/*     */   
/*     */   private String getName(MethodT m) {
/*  91 */     String seed = this.parent.nav().getMethodName(m);
/*  92 */     String lseed = seed.toLowerCase();
/*  93 */     if (lseed.startsWith("get") || lseed.startsWith("set"))
/*  94 */       return camelize(seed.substring(3)); 
/*  95 */     if (lseed.startsWith("is"))
/*  96 */       return camelize(seed.substring(2)); 
/*  97 */     return seed;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String camelize(String s) {
/* 102 */     return Introspector.decapitalize(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locatable getUpstream() {
/* 109 */     return this.parent;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 113 */     if (this.getter != null) {
/* 114 */       return this.parent.nav().getMethodLocation(this.getter);
/*     */     }
/* 116 */     return this.parent.nav().getMethodLocation(this.setter);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\GetterSetterPropertySeed.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */