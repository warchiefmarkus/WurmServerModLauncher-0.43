/*     */ package 1.0.com.sun.tools.xjc.grammar.util;
/*     */ 
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionCloner;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.msv.grammar.trex.TREXGrammar;
/*     */ import com.sun.tools.xjc.grammar.ExternalItem;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationRemover
/*     */   extends ExpressionCloner
/*     */ {
/*  26 */   private final Map bookmarks = new HashMap();
/*     */ 
/*     */   
/*  29 */   private final Map elements = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ReferenceExp anyContent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Grammar remove(Grammar src) {
/*  53 */     ExpressionPool newPool = new ExpressionPool();
/*     */     
/*  55 */     Expression newTop = src.getTopLevel().visit((ExpressionVisitorExpression)new com.sun.tools.xjc.grammar.util.AnnotationRemover(newPool));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     TREXGrammar grammar = new TREXGrammar(newPool);
/*  62 */     grammar.exp = newTop;
/*     */     
/*  64 */     return (Grammar)grammar;
/*     */   }
/*     */   
/*     */   public static Expression remove(Expression exp, ExpressionPool pool) {
/*  68 */     return exp.visit((ExpressionVisitorExpression)new com.sun.tools.xjc.grammar.util.AnnotationRemover(pool));
/*     */   }
/*     */   
/*     */   private AnnotationRemover(ExpressionPool pool) {
/*  72 */     super(pool);
/*  73 */     this.anyContent = new ReferenceExp("anyContent");
/*  74 */     this.anyContent.exp = pool.createZeroOrMore(pool.createChoice((Expression)new ElementPattern(NameClass.ALL, (Expression)this.anyContent), pool.createChoice(pool.createAttribute(NameClass.ALL), Expression.anyString)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression onRef(ReferenceExp exp) {
/*  84 */     if (!this.bookmarks.containsKey(exp))
/*     */     {
/*  86 */       return exp.exp.visit((ExpressionVisitorExpression)this);
/*     */     }
/*     */     
/*  89 */     ReferenceExp target = (ReferenceExp)this.bookmarks.get(exp);
/*  90 */     if (target == null) {
/*     */       
/*  92 */       target = new ReferenceExp(exp.name);
/*  93 */       target.exp = exp.exp.visit((ExpressionVisitorExpression)this);
/*     */       
/*  95 */       this.bookmarks.put(exp, target);
/*     */     } 
/*     */     
/*  98 */     return (Expression)target;
/*     */   }
/*     */   
/*     */   public Expression onOther(OtherExp exp) {
/* 102 */     if (exp instanceof ExternalItem)
/*     */     {
/*     */       
/* 105 */       return ((ExternalItem)exp).createAGM(this.pool);
/*     */     }
/*     */     
/* 108 */     return exp.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Expression onAttribute(AttributeExp exp) {
/* 112 */     return this.pool.createAttribute(exp.nameClass, exp.exp.visit((ExpressionVisitorExpression)this));
/*     */   }
/*     */   
/*     */   public Expression onElement(ElementExp exp) {
/* 116 */     ElementExp result = (ElementExp)this.elements.get(exp);
/* 117 */     if (result != null) return (Expression)result;
/*     */     
/* 119 */     ElementPattern elementPattern = new ElementPattern(exp.getNameClass(), Expression.nullSet);
/* 120 */     this.elements.put(exp, elementPattern);
/*     */ 
/*     */     
/* 123 */     ((ElementExp)elementPattern).contentModel = exp.contentModel.visit((ExpressionVisitorExpression)this);
/* 124 */     ((ElementExp)elementPattern).ignoreUndeclaredAttributes = exp.ignoreUndeclaredAttributes;
/*     */     
/* 126 */     return (Expression)elementPattern;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\AnnotationRemover.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */