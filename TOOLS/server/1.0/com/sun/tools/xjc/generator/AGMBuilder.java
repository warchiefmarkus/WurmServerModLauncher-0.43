/*     */ package 1.0.com.sun.tools.xjc.generator;
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
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.ExternalItem;
/*     */ import com.sun.xml.bind.GrammarImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AGMBuilder
/*     */   extends ExpressionCloner
/*     */ {
/*  39 */   private final Map class2agm = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private final Map ref2exp = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private final Map elem2exp = new HashMap();
/*     */ 
/*     */ 
/*     */ 
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
/*  76 */   private final ArrayList plugs = new ArrayList();
/*     */   
/*  78 */   private final GrammarImpl grammar = new GrammarImpl(new ExpressionPool());
/*     */   
/*     */   public static Grammar remove(AnnotatedGrammar src) {
/*  81 */     com.sun.tools.xjc.generator.AGMBuilder builder = new com.sun.tools.xjc.generator.AGMBuilder(src);
/*     */     
/*  83 */     builder.grammar.setTopLevel(src.getTopLevel().visit((ExpressionVisitorExpression)builder));
/*  84 */     builder.grammar.setPlugs((GrammarImpl.Plug[])builder.plugs.toArray((Object[])new GrammarImpl.Plug[0]));
/*     */     
/*  86 */     return (Grammar)builder.grammar;
/*     */   }
/*     */ 
/*     */   
/*     */   private AGMBuilder(AnnotatedGrammar grammar) {
/*  91 */     super(new ExpressionPool());
/*     */ 
/*     */     
/*  94 */     this.anyContent = new ReferenceExp("anyContent");
/*  95 */     this.anyContent.exp = this.pool.createZeroOrMore(this.pool.createChoice((Expression)new ElementPattern(NameClass.ALL, (Expression)this.anyContent), this.pool.createChoice(this.pool.createAttribute(NameClass.ALL), Expression.anyString)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     ClassItem[] ci = grammar.getClasses(); int i;
/* 104 */     for (i = 0; i < ci.length; i++) {
/* 105 */       if ((ci[i]).agm.exp == null) (ci[i]).agm.exp = (ci[i]).exp; 
/* 106 */       this.class2agm.put(ci[i], new ReferenceExp(null, (Expression)(ci[i]).agm));
/*     */     } 
/*     */ 
/*     */     
/* 110 */     for (i = 0; i < ci.length; i++) {
/* 111 */       ReferenceExp e = (ReferenceExp)this.class2agm.get(ci[i]);
/* 112 */       e.exp = e.exp.visit((ExpressionVisitorExpression)this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Expression onRef(ReferenceExp exp) {
/* 117 */     Expression e = (Expression)this.ref2exp.get(exp);
/* 118 */     if (e != null) return e;
/*     */     
/* 120 */     e = exp.exp.visit((ExpressionVisitorExpression)this);
/* 121 */     this.ref2exp.put(exp, e);
/*     */     
/* 123 */     return e;
/*     */   }
/*     */   
/*     */   public Expression onOther(OtherExp exp) {
/* 127 */     if (exp instanceof ExternalItem) {
/* 128 */       Expression e = ((ExternalItem)exp).createAGM(this.pool);
/* 129 */       if (e instanceof GrammarImpl.Plug)
/* 130 */         this.plugs.add(e); 
/* 131 */       return e;
/*     */     } 
/* 133 */     if (exp instanceof ClassItem) {
/* 134 */       return (Expression)this.class2agm.get(exp);
/*     */     }
/*     */     
/* 137 */     return exp.exp.visit((ExpressionVisitorExpression)this);
/*     */   }
/*     */   
/*     */   public Expression onAttribute(AttributeExp exp) {
/* 141 */     return this.pool.createAttribute(exp.nameClass, exp.exp.visit((ExpressionVisitorExpression)this));
/*     */   }
/*     */   public Expression onElement(ElementExp exp) {
/*     */     ElementPattern elementPattern;
/* 145 */     ElementExp result = (ElementExp)this.elem2exp.get(exp);
/* 146 */     if (result == null) {
/* 147 */       elementPattern = this.grammar.createElement(exp.getNameClass(), Expression.nullSet);
/* 148 */       this.elem2exp.put(exp, elementPattern);
/* 149 */       ((ElementExp)elementPattern).contentModel = exp.getContentModel().visit((ExpressionVisitorExpression)this);
/* 150 */       ((ElementExp)elementPattern).ignoreUndeclaredAttributes = exp.ignoreUndeclaredAttributes;
/*     */     } 
/* 152 */     return (Expression)elementPattern;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\AGMBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */