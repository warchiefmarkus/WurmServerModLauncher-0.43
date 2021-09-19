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
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BreadthFirstExpressionCloner
/*     */   extends ExpressionCloner
/*     */ {
/*  53 */   private final Set visitedExps = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private final Stack queue = new Stack();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inLoop = false;
/*     */ 
/*     */ 
/*     */   
/*     */   protected BreadthFirstExpressionCloner(ExpressionPool pool) {
/*  67 */     super(pool);
/*     */   }
/*     */   
/*     */   public final Expression onElement(ElementExp exp) {
/*  71 */     if (this.visitedExps.add(exp)) {
/*  72 */       this.queue.push(exp);
/*  73 */       processQueue();
/*     */     } 
/*  75 */     return (Expression)exp;
/*     */   }
/*     */   
/*     */   public final Expression onRef(ReferenceExp exp) {
/*  79 */     if (this.visitedExps.add(exp)) {
/*  80 */       this.queue.push(exp);
/*  81 */       processQueue();
/*     */     } 
/*  83 */     return (Expression)exp;
/*     */   }
/*     */   
/*     */   public final Expression onOther(OtherExp exp) {
/*  87 */     if (this.visitedExps.add(exp)) {
/*  88 */       this.queue.push(exp);
/*  89 */       processQueue();
/*     */     } 
/*  91 */     return (Expression)exp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Expression onAttribute(AttributeExp exp) {
/*  98 */     if (this.visitedExps.contains(exp)) return (Expression)exp;
/*     */     
/* 100 */     Expression e = this.pool.createAttribute(exp.nameClass, exp.exp.visit((ExpressionVisitorExpression)this));
/* 101 */     this.visitedExps.add(e);
/* 102 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   private void processQueue() {
/* 107 */     if (this.inLoop) {
/*     */       return;
/*     */     }
/* 110 */     this.inLoop = true;
/*     */ 
/*     */     
/* 113 */     while (!this.queue.isEmpty()) {
/* 114 */       Expression e = this.queue.pop();
/* 115 */       if (e instanceof ElementExp) {
/* 116 */         ElementExp ee = (ElementExp)e;
/* 117 */         ee.contentModel = ee.contentModel.visit((ExpressionVisitorExpression)this); continue;
/*     */       } 
/* 119 */       if (e instanceof ReferenceExp) {
/* 120 */         ReferenceExp re = (ReferenceExp)e;
/* 121 */         re.exp = re.exp.visit((ExpressionVisitorExpression)this); continue;
/*     */       } 
/* 123 */       if (e instanceof OtherExp) {
/* 124 */         OtherExp oe = (OtherExp)e;
/* 125 */         oe.exp = oe.exp.visit((ExpressionVisitorExpression)this); continue;
/*     */       } 
/* 127 */       throw new JAXBAssertionError();
/*     */     } 
/*     */     
/* 130 */     this.inLoop = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\BreadthFirstExpressionCloner.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */