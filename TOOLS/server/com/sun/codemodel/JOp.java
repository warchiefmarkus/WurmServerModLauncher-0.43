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
/*     */ public abstract class JOp
/*     */ {
/*     */   static boolean hasTopOp(JExpression e) {
/*  39 */     return (e instanceof UnaryOp || e instanceof BinaryOp);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class UnaryOp
/*     */     extends JExpressionImpl
/*     */   {
/*     */     protected String op;
/*     */     protected JExpression e;
/*     */     protected boolean opFirst = true;
/*     */     
/*     */     UnaryOp(String op, JExpression e) {
/*  51 */       this.op = op;
/*  52 */       this.e = e;
/*     */     }
/*     */     
/*     */     UnaryOp(JExpression e, String op) {
/*  56 */       this.op = op;
/*  57 */       this.e = e;
/*  58 */       this.opFirst = false;
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/*  62 */       if (this.opFirst) {
/*  63 */         f.p('(').p(this.op).g(this.e).p(')');
/*     */       } else {
/*  65 */         f.p('(').g(this.e).p(this.op).p(')');
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static JExpression minus(JExpression e) {
/*  71 */     return new UnaryOp("-", e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression not(JExpression e) {
/*  78 */     if (e == JExpr.TRUE) return JExpr.FALSE; 
/*  79 */     if (e == JExpr.FALSE) return JExpr.TRUE; 
/*  80 */     return new UnaryOp("!", e);
/*     */   }
/*     */   
/*     */   public static JExpression complement(JExpression e) {
/*  84 */     return new UnaryOp("~", e);
/*     */   }
/*     */   
/*     */   private static class TightUnaryOp
/*     */     extends UnaryOp {
/*     */     TightUnaryOp(JExpression e, String op) {
/*  90 */       super(e, op);
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/*  94 */       if (this.opFirst) {
/*  95 */         f.p(this.op).g(this.e);
/*     */       } else {
/*  97 */         f.g(this.e).p(this.op);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static JExpression incr(JExpression e) {
/* 103 */     return new TightUnaryOp(e, "++");
/*     */   }
/*     */   
/*     */   public static JExpression decr(JExpression e) {
/* 107 */     return new TightUnaryOp(e, "--");
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BinaryOp
/*     */     extends JExpressionImpl
/*     */   {
/*     */     String op;
/*     */     
/*     */     JExpression left;
/*     */     JGenerable right;
/*     */     
/*     */     BinaryOp(String op, JExpression left, JGenerable right) {
/* 120 */       this.left = left;
/* 121 */       this.op = op;
/* 122 */       this.right = right;
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/* 126 */       f.p('(').g(this.left).p(this.op).g(this.right).p(')');
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static JExpression plus(JExpression left, JExpression right) {
/* 132 */     return new BinaryOp("+", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression minus(JExpression left, JExpression right) {
/* 136 */     return new BinaryOp("-", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression mul(JExpression left, JExpression right) {
/* 140 */     return new BinaryOp("*", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression div(JExpression left, JExpression right) {
/* 144 */     return new BinaryOp("/", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression mod(JExpression left, JExpression right) {
/* 148 */     return new BinaryOp("%", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression shl(JExpression left, JExpression right) {
/* 152 */     return new BinaryOp("<<", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression shr(JExpression left, JExpression right) {
/* 156 */     return new BinaryOp(">>", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression shrz(JExpression left, JExpression right) {
/* 160 */     return new BinaryOp(">>>", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression band(JExpression left, JExpression right) {
/* 164 */     return new BinaryOp("&", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression bor(JExpression left, JExpression right) {
/* 168 */     return new BinaryOp("|", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression cand(JExpression left, JExpression right) {
/* 172 */     if (left == JExpr.TRUE) return right; 
/* 173 */     if (right == JExpr.TRUE) return left; 
/* 174 */     if (left == JExpr.FALSE) return left; 
/* 175 */     if (right == JExpr.FALSE) return right; 
/* 176 */     return new BinaryOp("&&", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression cor(JExpression left, JExpression right) {
/* 180 */     if (left == JExpr.TRUE) return left; 
/* 181 */     if (right == JExpr.TRUE) return right; 
/* 182 */     if (left == JExpr.FALSE) return right; 
/* 183 */     if (right == JExpr.FALSE) return left; 
/* 184 */     return new BinaryOp("||", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression xor(JExpression left, JExpression right) {
/* 188 */     return new BinaryOp("^", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression lt(JExpression left, JExpression right) {
/* 192 */     return new BinaryOp("<", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression lte(JExpression left, JExpression right) {
/* 196 */     return new BinaryOp("<=", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression gt(JExpression left, JExpression right) {
/* 200 */     return new BinaryOp(">", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression gte(JExpression left, JExpression right) {
/* 204 */     return new BinaryOp(">=", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression eq(JExpression left, JExpression right) {
/* 208 */     return new BinaryOp("==", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression ne(JExpression left, JExpression right) {
/* 212 */     return new BinaryOp("!=", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression _instanceof(JExpression left, JType right) {
/* 216 */     return new BinaryOp("instanceof", left, right);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TernaryOp
/*     */     extends JExpressionImpl
/*     */   {
/*     */     String op1;
/*     */     
/*     */     String op2;
/*     */     JExpression e1;
/*     */     JExpression e2;
/*     */     JExpression e3;
/*     */     
/*     */     TernaryOp(String op1, String op2, JExpression e1, JExpression e2, JExpression e3) {
/* 231 */       this.e1 = e1;
/* 232 */       this.op1 = op1;
/* 233 */       this.e2 = e2;
/* 234 */       this.op2 = op2;
/* 235 */       this.e3 = e3;
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/* 239 */       f.p('(').g(this.e1).p(this.op1).g(this.e2).p(this.op2).g(this.e3).p(')');
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression cond(JExpression cond, JExpression ifTrue, JExpression ifFalse) {
/* 246 */     return new TernaryOp("?", ":", cond, ifTrue, ifFalse);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JOp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */