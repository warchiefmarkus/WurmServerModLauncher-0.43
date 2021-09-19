/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*    */ import com.sun.tools.xjc.grammar.TypeItem;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InterfaceItem
/*    */   extends TypeItem
/*    */ {
/*    */   private final JClass type;
/*    */   
/*    */   protected InterfaceItem(JClass _type, Expression body, Locator loc) {
/* 22 */     super(_type.name(), loc);
/* 23 */     this.type = _type;
/* 24 */     this.exp = body;
/*    */   }
/*    */   
/*    */   public JType getType() {
/* 28 */     return (JType)this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JClass getTypeAsClass() {
/* 35 */     return this.type;
/*    */   } public ClassItem getSuperType() {
/* 37 */     return null;
/*    */   }
/*    */   public Object visitJI(JavaItemVisitor visitor) {
/* 40 */     return visitor.onInterface(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\InterfaceItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */