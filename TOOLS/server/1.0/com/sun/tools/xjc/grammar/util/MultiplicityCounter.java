/*     */ package 1.0.com.sun.tools.xjc.grammar.util;
/*     */ 
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.ConcurExp;
/*     */ import com.sun.msv.grammar.DataExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionVisitor;
/*     */ import com.sun.msv.grammar.InterleaveExp;
/*     */ import com.sun.msv.grammar.ListExp;
/*     */ import com.sun.msv.grammar.MixedExp;
/*     */ import com.sun.msv.grammar.OneOrMoreExp;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.msv.grammar.SequenceExp;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MultiplicityCounter
/*     */   implements ExpressionVisitor
/*     */ {
/*  40 */   public static final com.sun.tools.xjc.grammar.util.MultiplicityCounter javaItemCounter = (com.sun.tools.xjc.grammar.util.MultiplicityCounter)new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Multiplicity isChild(Expression paramExpression);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object onSequence(SequenceExp exp) {
/*  57 */     Multiplicity m = isChild((Expression)exp);
/*  58 */     if (m != null) return m;
/*     */     
/*  60 */     return Multiplicity.group((Multiplicity)exp.exp1.visit(this), (Multiplicity)exp.exp2.visit(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object onInterleave(InterleaveExp exp) {
/*  67 */     Multiplicity m = isChild((Expression)exp);
/*  68 */     if (m != null) return m;
/*     */     
/*  70 */     return Multiplicity.group((Multiplicity)exp.exp1.visit(this), (Multiplicity)exp.exp2.visit(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object onChoice(ChoiceExp exp) {
/*  77 */     Multiplicity m = isChild((Expression)exp);
/*  78 */     if (m != null) return m;
/*     */     
/*  80 */     return Multiplicity.choice((Multiplicity)exp.exp1.visit(this), (Multiplicity)exp.exp2.visit(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object onOneOrMore(OneOrMoreExp exp) {
/*  87 */     Multiplicity m = isChild((Expression)exp);
/*  88 */     if (m != null) return m; 
/*  89 */     return Multiplicity.oneOrMore((Multiplicity)exp.exp.visit(this));
/*     */   }
/*     */   
/*     */   public Object onMixed(MixedExp exp) {
/*  93 */     Multiplicity m = isChild((Expression)exp);
/*  94 */     if (m != null) return m; 
/*  95 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   public Object onList(ListExp exp) {
/*  99 */     Multiplicity m = isChild((Expression)exp);
/* 100 */     if (m != null) return m; 
/* 101 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   public Object onEpsilon() {
/* 105 */     Multiplicity m = isChild(Expression.epsilon);
/* 106 */     if (m == null) m = Multiplicity.zero; 
/* 107 */     return m;
/*     */   }
/*     */   
/*     */   public Object onAnyString() {
/* 111 */     Multiplicity m = isChild(Expression.anyString);
/* 112 */     if (m == null) m = Multiplicity.zero; 
/* 113 */     return m;
/*     */   }
/*     */   
/*     */   public Object onData(DataExp exp) {
/* 117 */     Multiplicity m = isChild((Expression)exp);
/* 118 */     if (m == null) m = Multiplicity.zero; 
/* 119 */     return m;
/*     */   }
/*     */   
/*     */   public Object onValue(ValueExp exp) {
/* 123 */     Multiplicity m = isChild((Expression)exp);
/* 124 */     if (m == null) m = Multiplicity.zero; 
/* 125 */     return m;
/*     */   }
/*     */   
/*     */   public Object onElement(ElementExp exp) {
/* 129 */     Multiplicity m = isChild((Expression)exp);
/* 130 */     if (m != null) return m; 
/* 131 */     return exp.contentModel.visit(this);
/*     */   }
/*     */   
/*     */   public Object onAttribute(AttributeExp exp) {
/* 135 */     Multiplicity m = isChild((Expression)exp);
/* 136 */     if (m != null) return m; 
/* 137 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   public Object onRef(ReferenceExp exp) {
/* 141 */     Multiplicity m = isChild((Expression)exp);
/* 142 */     if (m != null) return m; 
/* 143 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   public Object onOther(OtherExp exp) {
/* 147 */     Multiplicity m = isChild((Expression)exp);
/* 148 */     if (m != null) return m; 
/* 149 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   public Object onConcur(ConcurExp exp) {
/* 153 */     throw new Error(); } public Object onNullSet() {
/* 154 */     throw new Error();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\MultiplicityCounter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */