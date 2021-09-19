/*     */ package com.sun.tools.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.api.TypeAndAnnotation;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlJavaTypeAdapterWriter;
/*     */ import com.sun.tools.xjc.model.CAdapter;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TypeAndAnnotationImpl
/*     */   implements TypeAndAnnotation
/*     */ {
/*     */   private final TypeUse typeUse;
/*     */   private final Outline outline;
/*     */   
/*     */   public TypeAndAnnotationImpl(Outline outline, TypeUse typeUse) {
/*  64 */     this.typeUse = typeUse;
/*  65 */     this.outline = outline; } public JType getTypeClass() {
/*     */     NType nt;
/*     */     JPrimitiveType jPrimitiveType1;
/*     */     JClass jClass;
/*  69 */     CAdapter a = this.typeUse.getAdapterUse();
/*     */     
/*  71 */     if (a != null) {
/*  72 */       nt = (NType)a.customType;
/*     */     } else {
/*  74 */       nt = (NType)this.typeUse.getInfo().getType();
/*     */     } 
/*  76 */     JType jt = nt.toType(this.outline, Aspect.EXPOSED);
/*     */     
/*  78 */     JPrimitiveType prim = jt.boxify().getPrimitiveType();
/*  79 */     if (!this.typeUse.isCollection() && prim != null) {
/*  80 */       jPrimitiveType1 = prim;
/*     */     }
/*  82 */     if (this.typeUse.isCollection()) {
/*  83 */       jClass = jPrimitiveType1.array();
/*     */     }
/*  85 */     return (JType)jClass;
/*     */   }
/*     */   
/*     */   public void annotate(JAnnotatable programElement) {
/*  89 */     if (this.typeUse.getAdapterUse() == null && !this.typeUse.isCollection()) {
/*     */       return;
/*     */     }
/*  92 */     CAdapter adapterUse = this.typeUse.getAdapterUse();
/*  93 */     if (adapterUse != null)
/*     */     {
/*  95 */       if (adapterUse.getAdapterIfKnown() == SwaRefAdapter.class) {
/*  96 */         programElement.annotate(XmlAttachmentRef.class);
/*     */       }
/*     */       else {
/*     */         
/* 100 */         ((XmlJavaTypeAdapterWriter)programElement.annotate2(XmlJavaTypeAdapterWriter.class)).value((JType)((NClass)adapterUse.adapterType).toType(this.outline, Aspect.EXPOSED));
/*     */       } 
/*     */     }
/*     */     
/* 104 */     if (this.typeUse.isCollection())
/* 105 */       programElement.annotate(XmlList.class); 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 109 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 111 */     builder.append(getTypeClass());
/* 112 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 116 */     if (!(o instanceof TypeAndAnnotationImpl)) return false; 
/* 117 */     TypeAndAnnotationImpl that = (TypeAndAnnotationImpl)o;
/* 118 */     return (this.typeUse == that.typeUse);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 122 */     return this.typeUse.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\TypeAndAnnotationImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */