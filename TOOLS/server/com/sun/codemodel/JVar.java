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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JVar
/*     */   extends JExpressionImpl
/*     */   implements JDeclaration, JAssignmentTarget, JAnnotatable
/*     */ {
/*     */   private JMods mods;
/*     */   private JType type;
/*     */   private String name;
/*     */   private JExpression init;
/*  57 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JVar(JMods mods, JType type, String name, JExpression init) {
/*  74 */     this.mods = mods;
/*  75 */     this.type = type;
/*  76 */     this.name = name;
/*  77 */     this.init = init;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar init(JExpression init) {
/*  88 */     this.init = init;
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  98 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void name(String name) {
/* 105 */     if (!JJavaName.isJavaIdentifier(name))
/* 106 */       throw new IllegalArgumentException(); 
/* 107 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType type() {
/* 116 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods mods() {
/* 125 */     return this.mods;
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
/*     */   public JType type(JType newType) {
/* 138 */     JType r = this.type;
/* 139 */     if (newType == null)
/* 140 */       throw new IllegalArgumentException(); 
/* 141 */     this.type = newType;
/* 142 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 152 */     if (this.annotations == null)
/* 153 */       this.annotations = new ArrayList<JAnnotationUse>(); 
/* 154 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 155 */     this.annotations.add(a);
/* 156 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 166 */     return annotate(this.type.owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 170 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */   
/*     */   protected boolean isAnnotated() {
/* 174 */     return (this.annotations != null);
/*     */   }
/*     */   
/*     */   public void bind(JFormatter f) {
/* 178 */     if (this.annotations != null)
/* 179 */       for (int i = 0; i < this.annotations.size(); i++) {
/* 180 */         f.g(this.annotations.get(i)).nl();
/*     */       } 
/* 182 */     f.g(this.mods).g(this.type).id(this.name);
/* 183 */     if (this.init != null)
/* 184 */       f.p('=').g(this.init); 
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 188 */     f.b(this).p(';').nl();
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 192 */     f.id(this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression assign(JExpression rhs) {
/* 197 */     return JExpr.assign(this, rhs);
/*     */   }
/*     */   public JExpression assignPlus(JExpression rhs) {
/* 200 */     return JExpr.assignPlus(this, rhs);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JVar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */