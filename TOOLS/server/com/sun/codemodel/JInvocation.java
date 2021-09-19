/*     */ package com.sun.codemodel;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JInvocation
/*     */   extends JExpressionImpl
/*     */   implements JStatement
/*     */ {
/*     */   private JGenerable object;
/*     */   private String name;
/*     */   private JMethod method;
/*     */   private boolean isConstructor = false;
/*  53 */   private List<JExpression> args = new ArrayList<JExpression>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private JType type = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JInvocation(JExpression object, String name) {
/*  72 */     this(object, name);
/*     */   }
/*     */   
/*     */   JInvocation(JExpression object, JMethod method) {
/*  76 */     this(object, method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JInvocation(JClass type, String name) {
/*  83 */     this(type, name);
/*     */   }
/*     */   
/*     */   JInvocation(JClass type, JMethod method) {
/*  87 */     this(type, method);
/*     */   }
/*     */   
/*     */   private JInvocation(JGenerable object, String name) {
/*  91 */     this.object = object;
/*  92 */     if (name.indexOf('.') >= 0)
/*  93 */       throw new IllegalArgumentException("method name contains '.': " + name); 
/*  94 */     this.name = name;
/*     */   }
/*     */   
/*     */   private JInvocation(JGenerable object, JMethod method) {
/*  98 */     this.object = object;
/*  99 */     this.method = method;
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
/*     */   JInvocation(JType c) {
/* 113 */     this.isConstructor = true;
/* 114 */     this.type = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation arg(JExpression arg) {
/* 124 */     if (arg == null) throw new IllegalArgumentException(); 
/* 125 */     this.args.add(arg);
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation arg(String v) {
/* 135 */     return arg(JExpr.lit(v));
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(JFormatter f) {
/* 140 */     if (this.isConstructor && this.type.isArray()) {
/*     */       
/* 142 */       f.p("new").g(this.type).p('{');
/*     */     }
/* 144 */     else if (this.isConstructor) {
/* 145 */       f.p("new").g(this.type).p('(');
/*     */     } else {
/* 147 */       String name = this.name;
/* 148 */       if (name == null) name = this.method.name();
/*     */       
/* 150 */       if (this.object != null) {
/* 151 */         f.g(this.object).p('.').p(name).p('(');
/*     */       } else {
/* 153 */         f.id(name).p('(');
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     f.g((Collection)this.args);
/*     */     
/* 159 */     if (this.isConstructor && this.type.isArray()) {
/* 160 */       f.p('}');
/*     */     } else {
/* 162 */       f.p(')');
/*     */     } 
/* 164 */     if (this.type instanceof JDefinedClass && ((JDefinedClass)this.type).isAnonymous()) {
/* 165 */       ((JAnonymousClass)this.type).declareBody(f);
/*     */     }
/*     */   }
/*     */   
/*     */   public void state(JFormatter f) {
/* 170 */     f.g(this).p(';').nl();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JInvocation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */