/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.ConcurExp;
/*     */ import com.sun.msv.grammar.DataExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.InterleaveExp;
/*     */ import com.sun.msv.grammar.ListExp;
/*     */ import com.sun.msv.grammar.MixedExp;
/*     */ import com.sun.msv.grammar.OneOrMoreExp;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.msv.grammar.SequenceExp;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.StringTokenizer;
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FixedExpBuilder
/*     */   implements ExpressionVisitorExpression
/*     */ {
/*     */   private final AnnotatedGrammar grammar;
/*     */   private final ExpressionPool pool;
/*     */   private String token;
/*     */   private final ValidationContext context;
/*     */   
/*     */   public static Expression build(Expression exp, String token, AnnotatedGrammar grammar, ValidationContext context) {
/*  96 */     return exp.visit(new com.sun.tools.xjc.reader.xmlschema.FixedExpBuilder(grammar, token, context));
/*     */   }
/*     */   
/*     */   private FixedExpBuilder(AnnotatedGrammar _grammar, String _token, ValidationContext _context) {
/* 100 */     this.grammar = _grammar;
/* 101 */     this.pool = this.grammar.getPool();
/* 102 */     this.token = _token;
/* 103 */     this.context = _context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression onOther(OtherExp exp) {
/* 114 */     if (exp instanceof PrimitiveItem) {
/* 115 */       PrimitiveItem pi = (PrimitiveItem)exp;
/*     */       
/* 117 */       Expression body = exp.exp.visit(this);
/* 118 */       if (body == Expression.nullSet) return body;
/*     */       
/* 120 */       return (Expression)this.grammar.createPrimitiveItem(pi.xducer, pi.guard, body, pi.locator);
/*     */     } 
/* 122 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   public Expression onList(ListExp exp) {
/* 126 */     String oldToken = this.token;
/*     */     
/* 128 */     Expression residual = exp.exp;
/* 129 */     Expression result = Expression.epsilon;
/* 130 */     StringTokenizer tokens = new StringTokenizer(this.token);
/* 131 */     while (tokens.hasMoreTokens()) {
/* 132 */       this.token = tokens.nextToken();
/* 133 */       result = this.pool.createSequence(result, residual.visit(this));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     result = this.pool.createList(result);
/*     */     
/* 145 */     this.token = oldToken;
/* 146 */     return result;
/*     */   }
/*     */   
/*     */   public Expression onRef(ReferenceExp exp) {
/* 150 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   private static void _assert(boolean b) {
/* 154 */     if (!b) {
/* 155 */       throw new JAXBAssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   public Expression onAnyString() {
/* 160 */     return this.pool.createValue((XSDatatype)StringType.theInstance, this.token);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression onChoice(ChoiceExp exp) {
/* 166 */     Expression r = exp.exp1.visit(this);
/* 167 */     if (r != Expression.nullSet) return r; 
/* 168 */     return exp.exp2.visit(this);
/*     */   }
/*     */   
/*     */   public Expression onEpsilon() {
/* 172 */     return Expression.nullSet;
/*     */   }
/*     */   
/*     */   public Expression onNullSet() {
/* 176 */     return Expression.nullSet;
/*     */   }
/*     */   
/*     */   public Expression onOneOrMore(OneOrMoreExp exp) {
/* 180 */     return exp.exp.visit(this);
/*     */   }
/*     */   
/*     */   public Expression onSequence(SequenceExp exp) {
/* 184 */     Expression r = exp.exp1.visit(this);
/*     */ 
/*     */ 
/*     */     
/* 188 */     if (r == Expression.nullSet && exp.exp1.isEpsilonReducible()) {
/* 189 */       r = exp.exp2.visit(this);
/*     */     }
/* 191 */     return r;
/*     */   }
/*     */   
/*     */   public Expression onData(DataExp exp) {
/* 195 */     if (exp.dt.isValid(this.token, this.context)) {
/* 196 */       return this.pool.createValue(exp.dt, exp.name, exp.dt.createValue(this.token, this.context));
/*     */     }
/*     */ 
/*     */     
/* 200 */     return Expression.nullSet;
/*     */   }
/*     */   
/*     */   public Expression onValue(ValueExp exp) {
/* 204 */     if (exp.dt.sameValue(exp.value, exp.dt.createValue(this.token, this.context))) {
/* 205 */       return (Expression)exp;
/*     */     }
/* 207 */     return Expression.nullSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression onAttribute(AttributeExp exp) {
/* 212 */     _assert(false); return null; }
/* 213 */   public Expression onElement(ElementExp exp) { _assert(false); return null; }
/* 214 */   public Expression onConcur(ConcurExp p) { _assert(false); return null; }
/* 215 */   public Expression onInterleave(InterleaveExp p) { _assert(false); return null; } public Expression onMixed(MixedExp exp) {
/* 216 */     _assert(false); return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\FixedExpBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */