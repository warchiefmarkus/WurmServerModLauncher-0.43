/*    */ package 1.0.com.sun.tools.xjc.reader;
/*    */ 
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*    */ import com.sun.msv.grammar.ReferenceExp;
/*    */ import com.sun.msv.grammar.util.ExpressionWalker;
/*    */ import com.sun.tools.xjc.reader.PackageTracker;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public final class HierarchicalPackageTracker
/*    */   implements PackageTracker
/*    */ {
/* 38 */   private final Map dic = new HashMap();
/*    */ 
/*    */   
/*    */   private JPackage pkg;
/*    */ 
/*    */   
/*    */   public final JPackage get(ReferenceExp exp) {
/* 45 */     return (JPackage)this.dic.get(exp);
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
/* 58 */   private final ExpressionWalker visitor = (ExpressionWalker)new Object(this);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void associate(Expression exp, JPackage _pkg) {
/* 92 */     this.pkg = _pkg;
/* 93 */     exp.visit((ExpressionVisitorVoid)this.visitor);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\HierarchicalPackageTracker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */