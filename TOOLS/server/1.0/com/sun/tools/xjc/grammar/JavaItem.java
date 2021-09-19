/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.msv.grammar.OtherExp;
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
/*    */ public abstract class JavaItem
/*    */   extends OtherExp
/*    */ {
/*    */   public String name;
/*    */   public final Locator locator;
/*    */   
/*    */   public JavaItem(String name, Locator loc) {
/* 21 */     this.name = name;
/* 22 */     this.locator = loc;
/*    */   }
/*    */   
/*    */   public abstract Object visitJI(JavaItemVisitor paramJavaItemVisitor);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\JavaItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */