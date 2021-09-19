/*    */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*    */ 
/*    */ import com.sun.msv.datatype.DatabindableDatatype;
/*    */ import com.sun.msv.datatype.xsd.StringType;
/*    */ import com.sun.msv.datatype.xsd.XSDatatype;
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.ElementExp;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionCloner;
/*    */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*    */ import com.sun.msv.grammar.MixedExp;
/*    */ import com.sun.msv.grammar.OtherExp;
/*    */ import com.sun.msv.grammar.ReferenceExp;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
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
/*    */ 
/*    */ 
/*    */ public class MixedRemover
/*    */   extends ExpressionCloner
/*    */ {
/* 32 */   private final Set visitedExps = new HashSet();
/*    */   
/*    */   private final AnnotatedGrammar grammar;
/*    */ 
/*    */   
/*    */   public MixedRemover(AnnotatedGrammar g) {
/* 38 */     super(g.getPool());
/* 39 */     this.grammar = g;
/*    */   }
/*    */   
/*    */   public Expression onRef(ReferenceExp exp) {
/* 43 */     if (this.visitedExps.add(exp)) {
/* 44 */       exp.exp = exp.exp.visit((ExpressionVisitorExpression)this);
/*    */     }
/* 46 */     return (Expression)exp;
/*    */   }
/*    */   
/*    */   public Expression onOther(OtherExp exp) {
/* 50 */     if (this.visitedExps.add(exp)) {
/* 51 */       exp.exp = exp.exp.visit((ExpressionVisitorExpression)this);
/*    */     }
/*    */     
/* 54 */     return (Expression)exp;
/*    */   }
/*    */   
/*    */   public Expression onElement(ElementExp exp) {
/* 58 */     if (!this.visitedExps.add(exp)) {
/* 59 */       return (Expression)exp;
/*    */     }
/* 61 */     exp.contentModel = exp.contentModel.visit((ExpressionVisitorExpression)this);
/* 62 */     return (Expression)exp;
/*    */   }
/*    */   
/*    */   public Expression onAttribute(AttributeExp exp) {
/* 66 */     if (!this.visitedExps.add(exp)) {
/* 67 */       return (Expression)exp;
/*    */     }
/* 69 */     return this.pool.createAttribute(exp.nameClass, exp.exp.visit((ExpressionVisitorExpression)this));
/*    */   }
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
/*    */   public Expression onMixed(MixedExp exp) {
/* 84 */     return this.pool.createInterleave(this.pool.createZeroOrMore((Expression)this.grammar.createPrimitiveItem((Transducer)new IdentityTransducer(this.grammar.codeModel), (DatabindableDatatype)StringType.theInstance, this.pool.createData((XSDatatype)StringType.theInstance), null)), exp.exp.visit((ExpressionVisitorExpression)this));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\MixedRemover.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */