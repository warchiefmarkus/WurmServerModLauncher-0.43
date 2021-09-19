/*    */ package 1.0.com.sun.tools.xjc.reader.relaxng;
/*    */ 
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.reader.GrammarReader;
/*    */ import com.sun.msv.reader.State;
/*    */ import com.sun.msv.util.StartTagInfo;
/*    */ import com.sun.tools.xjc.grammar.ClassCandidateItem;
/*    */ import com.sun.tools.xjc.reader.NameConverter;
/*    */ import com.sun.tools.xjc.reader.decorator.DecoratorImpl;
/*    */ import com.sun.tools.xjc.reader.relaxng.TRELAXNGReader;
/*    */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultDecorator
/*    */   extends DecoratorImpl
/*    */ {
/*    */   DefaultDecorator(TRELAXNGReader reader, NameConverter nc) {
/* 27 */     super((GrammarReader)reader, reader.annGrammar, nc);
/*    */   }
/*    */ 
/*    */   
/*    */   private CodeModelClassFactory getClassFactory() {
/* 32 */     return ((TRELAXNGReader)this.reader).classFactory;
/*    */   }
/*    */   
/*    */   public Expression decorate(State state, Expression exp) {
/* 36 */     StartTagInfo tag = state.getStartTag();
/* 37 */     TRELAXNGReader reader = (TRELAXNGReader)this.reader;
/*    */     
/* 39 */     if (tag.localName.equals("define") && (
/* 40 */       exp == Expression.nullSet || exp == Expression.epsilon) && state.getStartTag().containsAttribute("combine"))
/*    */     {
/*    */ 
/*    */       
/* 44 */       return exp;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     if ((tag.localName.equals("element") || tag.localName.equals("define")) && !(exp instanceof com.sun.tools.xjc.grammar.ClassItem) && !(exp instanceof ClassCandidateItem)) {
/*    */ 
/*    */ 
/*    */       
/* 54 */       String baseName = decideName(state, exp, "class", "", state.getLocation());
/*    */ 
/*    */       
/* 57 */       return (Expression)new ClassCandidateItem(getClassFactory(), this.grammar, reader.packageManager.getCurrentPackage(), baseName, state.getLocation(), exp);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 66 */     if (exp instanceof AttributeExp && !(((AttributeExp)exp).nameClass instanceof com.sun.msv.grammar.SimpleNameClass))
/*    */     {
/* 68 */       return (Expression)this.grammar.createClassItem(getClassFactory().createInterface((JClassContainer)reader.packageManager.getCurrentPackage(), decideName(state, exp, "class", "Attr", state.getLocation()), state.getLocation()), exp, state.getLocation());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     return exp;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\relaxng\DefaultDecorator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */