/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JAnnotationUse
/*     */   extends JAnnotationValue
/*     */ {
/*     */   private final JClass clazz;
/*     */   private Map<String, JAnnotationValue> memberValues;
/*     */   
/*     */   JAnnotationUse(JClass clazz) {
/*  48 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   private JCodeModel owner() {
/*  52 */     return this.clazz.owner();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addValue(String name, JAnnotationValue annotationValue) {
/*  58 */     if (this.memberValues == null)
/*  59 */       this.memberValues = new LinkedHashMap<String, JAnnotationValue>(); 
/*  60 */     this.memberValues.put(name, annotationValue);
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
/*     */   public JAnnotationUse param(String name, boolean value) {
/*  77 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/*  78 */     return this;
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
/*     */   public JAnnotationUse param(String name, int value) {
/*  94 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/*  95 */     return this;
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
/*     */   public JAnnotationUse param(String name, String value) {
/* 113 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 114 */     return this;
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
/*     */   public JAnnotationUse annotationParam(String name, Class<? extends Annotation> value) {
/* 132 */     JAnnotationUse annotationUse = new JAnnotationUse(owner().ref(value));
/* 133 */     addValue(name, annotationUse);
/* 134 */     return annotationUse;
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
/*     */   public JAnnotationUse param(String name, final Enum value) {
/* 150 */     addValue(name, new JAnnotationValue() {
/*     */           public void generate(JFormatter f) {
/* 152 */             f.t(JAnnotationUse.this.owner().ref(value.getDeclaringClass())).p('.').p(value.name());
/*     */           }
/*     */         });
/* 155 */     return this;
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
/*     */   public JAnnotationUse param(String name, JEnumConstant value) {
/* 171 */     addValue(name, new JAnnotationStringValue(value));
/* 172 */     return this;
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
/*     */   public JAnnotationUse param(String name, Class value) {
/* 197 */     return param(name, this.clazz.owner().ref(value));
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
/*     */   public JAnnotationUse param(String name, JType type) {
/* 210 */     JClass clazz = type.boxify();
/* 211 */     addValue(name, new JAnnotationStringValue(clazz.dotclass()));
/* 212 */     return this;
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
/*     */   public JAnnotationArrayMember paramArray(String name) {
/* 226 */     JAnnotationArrayMember arrayMember = new JAnnotationArrayMember(owner());
/* 227 */     addValue(name, arrayMember);
/* 228 */     return arrayMember;
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
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 258 */     JAnnotationUse annotationUse = new JAnnotationUse(owner().ref(clazz));
/* 259 */     return annotationUse;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 263 */     f.p('@').g(this.clazz);
/* 264 */     if (this.memberValues != null) {
/* 265 */       f.p('(');
/* 266 */       boolean first = true;
/*     */       
/* 268 */       if (isOptimizable()) {
/*     */         
/* 270 */         f.g(this.memberValues.get("value"));
/*     */       } else {
/* 272 */         for (Map.Entry<String, JAnnotationValue> mapEntry : this.memberValues.entrySet()) {
/* 273 */           if (!first) f.p(','); 
/* 274 */           f.p(mapEntry.getKey()).p('=').g(mapEntry.getValue());
/* 275 */           first = false;
/*     */         } 
/*     */       } 
/* 278 */       f.p(')');
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isOptimizable() {
/* 283 */     return (this.memberValues.size() == 1 && this.memberValues.containsKey("value"));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAnnotationUse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */