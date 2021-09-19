/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.datatype.DatabindableDatatype;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*    */ import com.sun.tools.xjc.grammar.TypeItem;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
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
/*    */ public class PrimitiveItem
/*    */   extends TypeItem
/*    */ {
/*    */   public final Transducer xducer;
/*    */   public final DatabindableDatatype guard;
/*    */   
/*    */   protected PrimitiveItem(Transducer _xducer, DatabindableDatatype _guard, Expression _exp, Locator loc) {
/* 27 */     super(_xducer.toString(), loc);
/*    */     
/* 29 */     this.xducer = _xducer;
/* 30 */     this.exp = _exp;
/* 31 */     this.guard = _guard;
/*    */   }
/*    */   public JType getType() {
/* 34 */     return this.xducer.getReturnType();
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
/*    */   public Object visitJI(JavaItemVisitor visitor) {
/* 46 */     return visitor.onPrimitive(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\PrimitiveItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */