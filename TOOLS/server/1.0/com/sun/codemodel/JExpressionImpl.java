/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JArrayCompRef;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldRef;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JOp;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ 
/*     */ 
/*     */ public abstract class JExpressionImpl
/*     */   implements JExpression
/*     */ {
/*     */   public final JExpression minus() {
/*  18 */     return JOp.minus(this);
/*     */   }
/*     */   
/*     */   public final JExpression not() {
/*  22 */     return JOp.not(this);
/*     */   }
/*     */   
/*     */   public final JExpression complement() {
/*  26 */     return JOp.complement(this);
/*     */   }
/*     */   
/*     */   public final JExpression incr() {
/*  30 */     return JOp.incr(this);
/*     */   }
/*     */   
/*     */   public final JExpression decr() {
/*  34 */     return JOp.decr(this);
/*     */   }
/*     */   
/*     */   public final JExpression plus(JExpression right) {
/*  38 */     return JOp.plus(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression minus(JExpression right) {
/*  42 */     return JOp.minus(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression mul(JExpression right) {
/*  46 */     return JOp.mul(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression div(JExpression right) {
/*  50 */     return JOp.div(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression mod(JExpression right) {
/*  54 */     return JOp.mod(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shl(JExpression right) {
/*  58 */     return JOp.shl(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shr(JExpression right) {
/*  62 */     return JOp.shr(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shrz(JExpression right) {
/*  66 */     return JOp.shrz(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression band(JExpression right) {
/*  70 */     return JOp.band(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression bor(JExpression right) {
/*  74 */     return JOp.bor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression cand(JExpression right) {
/*  78 */     return JOp.cand(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression cor(JExpression right) {
/*  82 */     return JOp.cor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression xor(JExpression right) {
/*  86 */     return JOp.xor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression lt(JExpression right) {
/*  90 */     return JOp.lt(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression lte(JExpression right) {
/*  94 */     return JOp.lte(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression gt(JExpression right) {
/*  98 */     return JOp.gt(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression gte(JExpression right) {
/* 102 */     return JOp.gte(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression eq(JExpression right) {
/* 106 */     return JOp.eq(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression ne(JExpression right) {
/* 110 */     return JOp.ne(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression _instanceof(JType right) {
/* 114 */     return JOp._instanceof(this, right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JInvocation invoke(JMethod method) {
/* 123 */     return JExpr.invoke(this, method);
/*     */   }
/*     */   
/*     */   public final JInvocation invoke(String method) {
/* 127 */     return JExpr.invoke(this, method);
/*     */   }
/*     */   
/*     */   public final JFieldRef ref(JVar field) {
/* 131 */     return JExpr.ref(this, field);
/*     */   }
/*     */   
/*     */   public final JFieldRef ref(String field) {
/* 135 */     return JExpr.ref(this, field);
/*     */   }
/*     */   
/*     */   public final JArrayCompRef component(JExpression index) {
/* 139 */     return JExpr.component(this, index);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JExpressionImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */