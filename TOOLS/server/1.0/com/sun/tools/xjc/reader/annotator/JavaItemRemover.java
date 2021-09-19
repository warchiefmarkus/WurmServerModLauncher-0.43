/*    */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*    */ 
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.ConcurExp;
/*    */ import com.sun.msv.grammar.ElementExp;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionCloner;
/*    */ import com.sun.msv.grammar.ExpressionPool;
/*    */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*    */ import com.sun.msv.grammar.OtherExp;
/*    */ import com.sun.msv.grammar.ReferenceExp;
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JavaItemRemover
/*    */   extends ExpressionCloner
/*    */ {
/* 26 */   private static PrintStream debug = null; private final Set targets;
/*    */   private final Set visitedExps;
/*    */   
/* 29 */   JavaItemRemover(ExpressionPool pool, Set targets) { super(pool);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     this.visitedExps = new HashSet();
/*    */     this.targets = targets; }
/*    */   public Expression onNullSet() { throw new Error(); }
/* 50 */   public Expression onElement(ElementExp exp) { if (!this.visitedExps.add(exp))
/*    */     {
/* 52 */       return (Expression)exp; } 
/* 53 */     exp.contentModel = exp.contentModel.visit((ExpressionVisitorExpression)this);
/* 54 */     return (Expression)exp; }
/*    */   public Expression onConcur(ConcurExp exp) { throw new Error(); }
/*    */   public Expression onAttribute(AttributeExp exp) { Expression body = exp.exp.visit((ExpressionVisitorExpression)this); if (body == exp.exp)
/*    */       return (Expression)exp; 
/* 58 */     return this.pool.createAttribute(exp.nameClass, body); } public Expression onRef(ReferenceExp exp) { if (!this.visitedExps.add(exp))
/*    */     {
/* 60 */       return (Expression)exp;
/*    */     }
/* 62 */     exp.exp = exp.exp.visit((ExpressionVisitorExpression)this);
/* 63 */     return (Expression)exp; }
/*    */ 
/*    */   
/*    */   public Expression onOther(OtherExp exp) {
/* 67 */     if (this.targets.contains(exp)) {
/* 68 */       if (debug != null) {
/* 69 */         debug.println(" " + exp + ": found and removed");
/*    */       }
/*    */ 
/*    */       
/* 73 */       return exp.exp.visit((ExpressionVisitorExpression)this);
/*    */     } 
/*    */ 
/*    */     
/* 77 */     exp.exp = exp.exp.visit((ExpressionVisitorExpression)this);
/* 78 */     return (Expression)exp;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\JavaItemRemover.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */