/*     */ package com.sun.tools.xjc.outline;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ClassOutline
/*     */ {
/*     */   @NotNull
/*     */   public final CClassInfo target;
/*     */   @NotNull
/*     */   public final JDefinedClass ref;
/*     */   @NotNull
/*     */   public final JDefinedClass implClass;
/*     */   @NotNull
/*     */   public final JClass implRef;
/*     */   
/*     */   @NotNull
/*     */   public abstract Outline parent();
/*     */   
/*     */   @NotNull
/*     */   public PackageOutline _package() {
/*  69 */     return parent().getPackageContext(this.ref._package());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassOutline(CClassInfo _target, JDefinedClass exposedClass, JClass implRef, JDefinedClass _implClass) {
/* 108 */     this.target = _target;
/* 109 */     this.ref = exposedClass;
/* 110 */     this.implRef = implRef;
/* 111 */     this.implClass = _implClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FieldOutline[] getDeclaredFields() {
/* 119 */     List<CPropertyInfo> props = this.target.getProperties();
/* 120 */     FieldOutline[] fr = new FieldOutline[props.size()];
/* 121 */     for (int i = 0; i < fr.length; i++)
/* 122 */       fr[i] = parent().getField(props.get(i)); 
/* 123 */     return fr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassOutline getSuperClass() {
/* 132 */     CClassInfo s = this.target.getBaseClass();
/* 133 */     if (s == null) return null; 
/* 134 */     return parent().getClazz(s);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\ClassOutline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */