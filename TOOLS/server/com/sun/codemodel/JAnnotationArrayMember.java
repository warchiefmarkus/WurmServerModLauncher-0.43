/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
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
/*     */ public final class JAnnotationArrayMember
/*     */   extends JAnnotationValue
/*     */   implements JAnnotatable
/*     */ {
/*  38 */   private final List<JAnnotationValue> values = new ArrayList<JAnnotationValue>();
/*     */   private final JCodeModel owner;
/*     */   
/*     */   JAnnotationArrayMember(JCodeModel owner) {
/*  42 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(String value) {
/*  53 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/*  54 */     this.values.add(annotationValue);
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public JAnnotationArrayMember param(boolean value) {
/*  59 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/*  60 */     this.values.add(annotationValue);
/*  61 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(int value) {
/*  72 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/*  73 */     this.values.add(annotationValue);
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(float value) {
/*  85 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/*  86 */     this.values.add(annotationValue);
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public JAnnotationArrayMember param(Class value) {
/*  91 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value.getName()));
/*  92 */     this.values.add(annotationValue);
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public JAnnotationArrayMember param(JType type) {
/*  97 */     JClass clazz = type.boxify();
/*  98 */     JAnnotationValue annotationValue = new JAnnotationStringValue(clazz.dotclass());
/*  99 */     this.values.add(annotationValue);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 107 */     return annotate(this.owner.ref(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 114 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 115 */     this.values.add(a);
/* 116 */     return a;
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 120 */     return (W)TypedAnnotationWriter.create(clazz, this);
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
/*     */   public JAnnotationArrayMember param(JAnnotationUse value) {
/* 136 */     this.values.add(value);
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 141 */     f.p('{').nl().i();
/*     */     
/* 143 */     boolean first = true;
/* 144 */     for (JAnnotationValue aValue : this.values) {
/* 145 */       if (!first)
/* 146 */         f.p(',').nl(); 
/* 147 */       f.g(aValue);
/* 148 */       first = false;
/*     */     } 
/* 150 */     f.nl().o().p('}');
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAnnotationArrayMember.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */