/*     */ package com.sun.codemodel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JFieldRef
/*     */   extends JExpressionImpl
/*     */   implements JAssignmentTarget
/*     */ {
/*     */   private JGenerable object;
/*     */   private String name;
/*     */   private JVar var;
/*     */   private boolean explicitThis;
/*     */   
/*     */   JFieldRef(JExpression object, String name) {
/*  61 */     this(object, name, false);
/*     */   }
/*     */   
/*     */   JFieldRef(JExpression object, JVar v) {
/*  65 */     this(object, v, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JFieldRef(JType type, String name) {
/*  72 */     this(type, name, false);
/*     */   }
/*     */   
/*     */   JFieldRef(JType type, JVar v) {
/*  76 */     this(type, v, false);
/*     */   }
/*     */   
/*     */   JFieldRef(JGenerable object, String name, boolean explicitThis) {
/*  80 */     this.explicitThis = explicitThis;
/*  81 */     this.object = object;
/*  82 */     if (name.indexOf('.') >= 0)
/*  83 */       throw new IllegalArgumentException("Field name contains '.': " + name); 
/*  84 */     this.name = name;
/*     */   }
/*     */   
/*     */   JFieldRef(JGenerable object, JVar var, boolean explicitThis) {
/*  88 */     this.explicitThis = explicitThis;
/*  89 */     this.object = object;
/*  90 */     this.var = var;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/*  94 */     String name = this.name;
/*  95 */     if (name == null) name = this.var.name();
/*     */     
/*  97 */     if (this.object != null) {
/*  98 */       f.g(this.object).p('.').p(name);
/*     */     }
/* 100 */     else if (this.explicitThis) {
/* 101 */       f.p("this.").p(name);
/*     */     } else {
/* 103 */       f.id(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression assign(JExpression rhs) {
/* 109 */     return JExpr.assign(this, rhs);
/*     */   }
/*     */   public JExpression assignPlus(JExpression rhs) {
/* 112 */     return JExpr.assignPlus(this, rhs);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JFieldRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */