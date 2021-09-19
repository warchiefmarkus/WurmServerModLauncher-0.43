/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public final class JEnumConstant
/*     */   extends JExpressionImpl
/*     */   implements JDeclaration, JAnnotatable
/*     */ {
/*     */   private final String name;
/*     */   private final JDefinedClass type;
/*  47 */   private JDocComment jdoc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private List<JExpression> args = null;
/*     */   
/*     */   JEnumConstant(JDefinedClass type, String name) {
/*  62 */     this.name = name;
/*  63 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JEnumConstant arg(JExpression arg) {
/*  73 */     if (arg == null) throw new IllegalArgumentException(); 
/*  74 */     if (this.args == null)
/*  75 */       this.args = new ArrayList<JExpression>(); 
/*  76 */     this.args.add(arg);
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  86 */     return this.type.fullName().concat(".").concat(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/*  95 */     if (this.jdoc == null)
/*  96 */       this.jdoc = new JDocComment(this.type.owner()); 
/*  97 */     return this.jdoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 106 */     if (this.annotations == null)
/* 107 */       this.annotations = new ArrayList<JAnnotationUse>(); 
/* 108 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 109 */     this.annotations.add(a);
/* 110 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 120 */     return annotate(this.type.owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 124 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 128 */     if (this.jdoc != null)
/* 129 */       f.nl().g(this.jdoc); 
/* 130 */     if (this.annotations != null)
/* 131 */       for (int i = 0; i < this.annotations.size(); i++) {
/* 132 */         f.g(this.annotations.get(i)).nl();
/*     */       } 
/* 134 */     f.id(this.name);
/* 135 */     if (this.args != null) {
/* 136 */       f.p('(').g((Collection)this.args).p(')');
/*     */     }
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 141 */     f.t(this.type).p('.').p(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JEnumConstant.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */