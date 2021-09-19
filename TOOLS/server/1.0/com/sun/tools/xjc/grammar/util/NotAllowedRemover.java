/*     */ package 1.0.com.sun.tools.xjc.grammar.util;
/*     */ 
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.DataExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionCloner;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NotAllowedRemover
/*     */   extends ExpressionCloner
/*     */ {
/*     */   private final Set visitedExps;
/*     */   
/*     */   public NotAllowedRemover(ExpressionPool pool) {
/*  36 */     super(pool);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     this.visitedExps = new HashSet();
/*     */   } public Expression onRef(ReferenceExp exp) { if (!this.visitedExps.contains(exp)) { exp.exp = exp.exp.visit((ExpressionVisitorExpression)this); this.visitedExps.add(exp); }
/*     */      if (exp.exp == Expression.nullSet)
/*  74 */       return Expression.nullSet;  return (Expression)exp; } public Expression onElement(ElementExp exp) { if (!this.visitedExps.add(exp)) {
/*  75 */       return (Expression)exp;
/*     */     }
/*  77 */     Expression body = exp.contentModel.visit((ExpressionVisitorExpression)this);
/*  78 */     if (body == Expression.nullSet) {
/*  79 */       return Expression.nullSet;
/*     */     }
/*  81 */     exp.contentModel = body;
/*  82 */     return (Expression)exp; }
/*     */   public Expression onOther(OtherExp exp) { if (!this.visitedExps.contains(exp)) {
/*     */       exp.exp = exp.exp.visit((ExpressionVisitorExpression)this); this.visitedExps.add(exp);
/*     */     }  if (exp.exp == Expression.nullSet)
/*  86 */       return Expression.nullSet;  return (Expression)exp; } public Expression onAttribute(AttributeExp exp) { if (!this.visitedExps.add(exp)) {
/*  87 */       return (Expression)exp;
/*     */     }
/*  89 */     Expression body = exp.exp.visit((ExpressionVisitorExpression)this);
/*  90 */     if (body == Expression.nullSet) {
/*  91 */       return Expression.nullSet;
/*     */     }
/*  93 */     return this.pool.createAttribute(exp.nameClass, body); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression onData(DataExp exp) {
/*  98 */     if (exp.dt instanceof com.sun.msv.grammar.relax.NoneType) {
/*  99 */       return Expression.nullSet;
/*     */     }
/* 101 */     return (Expression)exp;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\NotAllowedRemover.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */