/*    */ package 1.0.com.sun.tools.xjc.grammar.ext;
/*    */ 
/*    */ import com.sun.codemodel.JClassAlreadyExistsException;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionPool;
/*    */ import com.sun.msv.grammar.NameClass;
/*    */ import com.sun.msv.grammar.ReferenceExp;
/*    */ import com.sun.msv.grammar.trex.ElementPattern;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ExternalItem;
/*    */ import org.xml.sax.Locator;
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
/*    */ abstract class AbstractDOMItem
/*    */   extends ExternalItem
/*    */ {
/*    */   private final Expression agm;
/*    */   protected final JCodeModel codeModel;
/*    */   
/*    */   public AbstractDOMItem(NameClass _elementName, AnnotatedGrammar grammar, Locator loc) {
/* 33 */     super("dom", _elementName, loc);
/* 34 */     ExpressionPool pool = grammar.getPool();
/*    */     
/* 36 */     this.codeModel = grammar.codeModel;
/*    */ 
/*    */ 
/*    */     
/* 40 */     ReferenceExp any = new ReferenceExp(null);
/* 41 */     any.exp = pool.createMixed(pool.createZeroOrMore(pool.createChoice(pool.createAttribute(NameClass.ALL), (Expression)new ElementPattern(NameClass.ALL, (Expression)any))));
/*    */ 
/*    */ 
/*    */     
/* 45 */     this.exp = (Expression)new ElementPattern(_elementName, (Expression)any);
/* 46 */     this.agm = this.exp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final JType createPhantomType(String name) {
/*    */     try {
/* 56 */       JDefinedClass def = this.codeModel._class(name);
/* 57 */       def.hide();
/* 58 */       return (JType)def;
/* 59 */     } catch (JClassAlreadyExistsException e) {
/* 60 */       return (JType)e.getExistingClass();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Expression createAGM(ExpressionPool pool) {
/* 66 */     return this.agm;
/*    */   }
/*    */   
/*    */   public Expression createValidationFragment() {
/* 70 */     return this.agm;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ext\AbstractDOMItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */