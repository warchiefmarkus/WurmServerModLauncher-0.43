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
/*     */ public abstract class JExpressionImpl
/*     */   implements JExpression
/*     */ {
/*     */   public final JExpression minus() {
/*  33 */     return JOp.minus(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JExpression not() {
/*  40 */     return JOp.not(this);
/*     */   }
/*     */   
/*     */   public final JExpression complement() {
/*  44 */     return JOp.complement(this);
/*     */   }
/*     */   
/*     */   public final JExpression incr() {
/*  48 */     return JOp.incr(this);
/*     */   }
/*     */   
/*     */   public final JExpression decr() {
/*  52 */     return JOp.decr(this);
/*     */   }
/*     */   
/*     */   public final JExpression plus(JExpression right) {
/*  56 */     return JOp.plus(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression minus(JExpression right) {
/*  60 */     return JOp.minus(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression mul(JExpression right) {
/*  64 */     return JOp.mul(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression div(JExpression right) {
/*  68 */     return JOp.div(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression mod(JExpression right) {
/*  72 */     return JOp.mod(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shl(JExpression right) {
/*  76 */     return JOp.shl(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shr(JExpression right) {
/*  80 */     return JOp.shr(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shrz(JExpression right) {
/*  84 */     return JOp.shrz(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression band(JExpression right) {
/*  88 */     return JOp.band(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression bor(JExpression right) {
/*  92 */     return JOp.bor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression cand(JExpression right) {
/*  96 */     return JOp.cand(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression cor(JExpression right) {
/* 100 */     return JOp.cor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression xor(JExpression right) {
/* 104 */     return JOp.xor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression lt(JExpression right) {
/* 108 */     return JOp.lt(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression lte(JExpression right) {
/* 112 */     return JOp.lte(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression gt(JExpression right) {
/* 116 */     return JOp.gt(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression gte(JExpression right) {
/* 120 */     return JOp.gte(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression eq(JExpression right) {
/* 124 */     return JOp.eq(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression ne(JExpression right) {
/* 128 */     return JOp.ne(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression _instanceof(JType right) {
/* 132 */     return JOp._instanceof(this, right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JInvocation invoke(JMethod method) {
/* 141 */     return JExpr.invoke(this, method);
/*     */   }
/*     */   
/*     */   public final JInvocation invoke(String method) {
/* 145 */     return JExpr.invoke(this, method);
/*     */   }
/*     */   
/*     */   public final JFieldRef ref(JVar field) {
/* 149 */     return JExpr.ref(this, field);
/*     */   }
/*     */   
/*     */   public final JFieldRef ref(String field) {
/* 153 */     return JExpr.ref(this, field);
/*     */   }
/*     */   
/*     */   public final JArrayCompRef component(JExpression index) {
/* 157 */     return JExpr.component(this, index);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JExpressionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */