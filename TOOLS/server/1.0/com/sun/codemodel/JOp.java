/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  23 */     return (e instanceof UnaryOp || e instanceof BinaryOp);
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
/*     */ 
/*     */   
/*     */   public static JExpression minus(JExpression e) {
/*  55 */     return (JExpression)new UnaryOp("-", e);
/*     */   }
/*     */   
/*     */   public static JExpression not(JExpression e) {
/*  59 */     if (e == JExpr.TRUE) return JExpr.FALSE; 
/*  60 */     if (e == JExpr.FALSE) return JExpr.TRUE; 
/*  61 */     return (JExpression)new UnaryOp("!", e);
/*     */   }
/*     */   
/*     */   public static JExpression complement(JExpression e) {
/*  65 */     return (JExpression)new UnaryOp("~", e);
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
/*     */   public static JExpression incr(JExpression e) {
/*  84 */     return (JExpression)new TightUnaryOp(e, "++");
/*     */   }
/*     */   
/*     */   public static JExpression decr(JExpression e) {
/*  88 */     return (JExpression)new TightUnaryOp(e, "--");
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
/*     */   public static JExpression plus(JExpression left, JExpression right) {
/* 113 */     return (JExpression)new BinaryOp("+", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression minus(JExpression left, JExpression right) {
/* 117 */     return (JExpression)new BinaryOp("-", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression mul(JExpression left, JExpression right) {
/* 121 */     return (JExpression)new BinaryOp("*", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression div(JExpression left, JExpression right) {
/* 125 */     return (JExpression)new BinaryOp("/", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression mod(JExpression left, JExpression right) {
/* 129 */     return (JExpression)new BinaryOp("%", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression shl(JExpression left, JExpression right) {
/* 133 */     return (JExpression)new BinaryOp("<<", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression shr(JExpression left, JExpression right) {
/* 137 */     return (JExpression)new BinaryOp(">>", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression shrz(JExpression left, JExpression right) {
/* 141 */     return (JExpression)new BinaryOp(">>>", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression band(JExpression left, JExpression right) {
/* 145 */     return (JExpression)new BinaryOp("&", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression bor(JExpression left, JExpression right) {
/* 149 */     return (JExpression)new BinaryOp("|", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression cand(JExpression left, JExpression right) {
/* 153 */     if (left == JExpr.TRUE) return right; 
/* 154 */     if (right == JExpr.TRUE) return left; 
/* 155 */     if (left == JExpr.FALSE) return left; 
/* 156 */     if (right == JExpr.FALSE) return right; 
/* 157 */     return (JExpression)new BinaryOp("&&", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression cor(JExpression left, JExpression right) {
/* 161 */     if (left == JExpr.TRUE) return left; 
/* 162 */     if (right == JExpr.TRUE) return right; 
/* 163 */     if (left == JExpr.FALSE) return right; 
/* 164 */     if (right == JExpr.FALSE) return left; 
/* 165 */     return (JExpression)new BinaryOp("||", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression xor(JExpression left, JExpression right) {
/* 169 */     return (JExpression)new BinaryOp("^", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression lt(JExpression left, JExpression right) {
/* 173 */     return (JExpression)new BinaryOp("<", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression lte(JExpression left, JExpression right) {
/* 177 */     return (JExpression)new BinaryOp("<=", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression gt(JExpression left, JExpression right) {
/* 181 */     return (JExpression)new BinaryOp(">", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression gte(JExpression left, JExpression right) {
/* 185 */     return (JExpression)new BinaryOp(">=", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression eq(JExpression left, JExpression right) {
/* 189 */     return (JExpression)new BinaryOp("==", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression ne(JExpression left, JExpression right) {
/* 193 */     return (JExpression)new BinaryOp("!=", left, (JGenerable)right);
/*     */   }
/*     */   
/*     */   public static JExpression _instanceof(JExpression left, JType right) {
/* 197 */     return (JExpression)new BinaryOp("instanceof", left, (JGenerable)right);
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
/*     */   public static JExpression cond(JExpression cond, JExpression ifTrue, JExpression ifFalse) {
/* 227 */     return (JExpression)new TernaryOp("?", ":", cond, ifTrue, ifFalse);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JOp.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */