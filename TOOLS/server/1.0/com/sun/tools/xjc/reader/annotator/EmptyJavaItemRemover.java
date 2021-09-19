/*     */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*     */ 
/*     */ import com.sun.msv.grammar.AttributeExp;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmptyJavaItemRemover
/*     */   extends ExpressionCloner
/*     */ {
/*     */   private final Set visitedExps;
/*     */   
/*     */   public EmptyJavaItemRemover(ExpressionPool pool) {
/*  61 */     super(pool);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     this.visitedExps = new HashSet();
/*     */   } public Expression onRef(ReferenceExp exp) { if (!this.visitedExps.contains(exp)) { exp.exp = exp.exp.visit((ExpressionVisitorExpression)this); this.visitedExps.add(exp); }
/*     */      if (exp.exp == Expression.epsilon)
/* 100 */       return Expression.epsilon;  return (Expression)exp; } public Expression onElement(ElementExp exp) { if (!this.visitedExps.add(exp)) {
/* 101 */       return (Expression)exp;
/*     */     }
/* 103 */     exp.contentModel = exp.contentModel.visit((ExpressionVisitorExpression)this);
/* 104 */     return (Expression)exp; }
/*     */   public Expression onOther(OtherExp exp) { if (!this.visitedExps.contains(exp)) {
/*     */       exp.exp = exp.exp.visit((ExpressionVisitorExpression)this); this.visitedExps.add(exp);
/*     */     }  if ((exp instanceof com.sun.tools.xjc.grammar.SuperClassItem || exp instanceof com.sun.tools.xjc.grammar.FieldItem) && exp.exp == Expression.epsilon)
/* 108 */       return Expression.epsilon;  return (Expression)exp; } public Expression onAttribute(AttributeExp exp) { if (!this.visitedExps.add(exp)) {
/* 109 */       return (Expression)exp;
/*     */     }
/* 111 */     return this.pool.createAttribute(exp.nameClass, exp.exp.visit((ExpressionVisitorExpression)this)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\EmptyJavaItemRemover.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */