/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.grammar.JavaItem;
/*    */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SuperClassItem
/*    */   extends JavaItem
/*    */ {
/*    */   public ClassItem definition;
/*    */   
/*    */   public SuperClassItem(Expression exp, Locator loc) {
/* 19 */     super("superClass-marker", loc);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 24 */     this.definition = null;
/*    */     this.exp = exp;
/*    */   } public Object visitJI(JavaItemVisitor visitor) {
/* 27 */     return visitor.onSuper(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\SuperClassItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */