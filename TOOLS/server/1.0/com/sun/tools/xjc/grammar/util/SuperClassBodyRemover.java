/*     */ package 1.0.com.sun.tools.xjc.grammar.util;
/*     */ 
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionCloner;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SuperClassBodyRemover
/*     */   extends ExpressionCloner
/*     */ {
/*  76 */   private final Set visitedRefs = new HashSet();
/*     */   
/*     */   public static void remove(AnnotatedGrammar g) {
/*  79 */     com.sun.tools.xjc.grammar.util.SuperClassBodyRemover su = new com.sun.tools.xjc.grammar.util.SuperClassBodyRemover(g.getPool());
/*     */     
/*  81 */     ClassItem[] cls = g.getClasses();
/*  82 */     for (int i = 0; i < cls.length; i++)
/*  83 */       (cls[i]).exp = (cls[i]).exp.visit((ExpressionVisitorExpression)su); 
/*     */   }
/*     */   private ExpressionCloner remover;
/*     */   public Expression onAttribute(AttributeExp exp) {
/*  87 */     return this.pool.createAttribute(exp.nameClass, exp.exp.visit((ExpressionVisitorExpression)this));
/*     */   }
/*     */   
/*     */   public Expression onElement(ElementExp exp) {
/*  91 */     if (this.visitedRefs.add(exp))
/*  92 */       exp.contentModel = exp.contentModel; 
/*  93 */     return (Expression)exp;
/*     */   }
/*     */   
/*     */   public Expression onRef(ReferenceExp exp) {
/*  97 */     if (this.visitedRefs.add(exp))
/*  98 */       exp.exp = exp.exp.visit((ExpressionVisitorExpression)this); 
/*  99 */     return (Expression)exp;
/*     */   }
/*     */   
/*     */   public Expression onOther(OtherExp exp) {
/* 103 */     if (exp instanceof com.sun.tools.xjc.grammar.SuperClassItem) {
/* 104 */       return exp.exp.visit((ExpressionVisitorExpression)this.remover);
/*     */     }
/* 106 */     if (this.visitedRefs.add(exp))
/* 107 */       exp.exp = exp.exp.visit((ExpressionVisitorExpression)this); 
/* 108 */     return (Expression)exp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SuperClassBodyRemover(ExpressionPool pool) {
/* 115 */     super(pool);
/* 116 */     this.remover = (ExpressionCloner)new Object(this, pool);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\SuperClassBodyRemover.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */