/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.OtherExp;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.util.CodeModelClassFactory;
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
/*    */ 
/*    */ 
/*    */ public class ClassCandidateItem
/*    */   extends OtherExp
/*    */ {
/*    */   public final String name;
/*    */   private final CodeModelClassFactory classFactory;
/*    */   private final AnnotatedGrammar grammar;
/*    */   public final JPackage targetPackage;
/*    */   public final Locator locator;
/*    */   private ClassItem ci;
/*    */   
/*    */   public ClassCandidateItem(CodeModelClassFactory _classFactory, AnnotatedGrammar _grammar, JPackage _targetPackage, String _name, Locator _loc, Expression body) {
/* 35 */     super(body);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 44 */     this.ci = null;
/*    */     this.grammar = _grammar;
/*    */     this.classFactory = _classFactory;
/*    */     this.targetPackage = _targetPackage;
/*    */     this.name = _name;
/*    */     this.locator = _loc;
/*    */   }
/*    */   
/*    */   public ClassItem toClassItem() {
/* 53 */     if (this.ci == null) {
/* 54 */       this.ci = this.grammar.createClassItem(this.classFactory.createInterface((JClassContainer)this.targetPackage, this.name, this.locator), this.exp, this.locator);
/*    */     }
/*    */ 
/*    */     
/* 58 */     return this.ci;
/*    */   }
/*    */   
/*    */   public String printName() {
/* 62 */     return super.printName() + "#" + this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ClassCandidateItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */