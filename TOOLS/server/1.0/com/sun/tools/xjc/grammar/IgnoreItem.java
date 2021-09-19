/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.JavaItem;
/*    */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IgnoreItem
/*    */   extends JavaItem
/*    */ {
/*    */   public IgnoreItem(Locator loc) {
/* 19 */     super("$ignore", loc);
/*    */   }
/*    */   
/*    */   public IgnoreItem(Expression exp, Locator loc) {
/* 23 */     this(loc);
/* 24 */     this.exp = exp;
/*    */   }
/*    */   public Object visitJI(JavaItemVisitor visitor) {
/* 27 */     return visitor.onIgnore(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\IgnoreItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */