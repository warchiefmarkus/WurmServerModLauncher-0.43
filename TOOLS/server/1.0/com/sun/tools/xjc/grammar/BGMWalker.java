/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*    */ import com.sun.msv.grammar.OtherExp;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.grammar.ExternalItem;
/*    */ import com.sun.tools.xjc.grammar.FieldItem;
/*    */ import com.sun.tools.xjc.grammar.IgnoreItem;
/*    */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*    */ import com.sun.tools.xjc.grammar.JavaItem;
/*    */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*    */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*    */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*    */ 
/*    */ public abstract class BGMWalker extends ExpressionWalker implements JavaItemVisitor {
/*    */   public void onOther(OtherExp exp) {
/* 16 */     if (exp instanceof JavaItem) {
/* 17 */       ((JavaItem)exp).visitJI(this);
/*    */     } else {
/* 19 */       exp.exp.visit((ExpressionVisitorVoid)this);
/*    */     } 
/*    */   }
/*    */   public Object onClass(ClassItem item) {
/* 23 */     item.exp.visit((ExpressionVisitorVoid)this);
/* 24 */     return null;
/*    */   }
/*    */   
/*    */   public Object onField(FieldItem item) {
/* 28 */     item.exp.visit((ExpressionVisitorVoid)this);
/* 29 */     return null;
/*    */   }
/*    */   
/*    */   public Object onIgnore(IgnoreItem item) {
/* 33 */     item.exp.visit((ExpressionVisitorVoid)this);
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   public Object onInterface(InterfaceItem item) {
/* 38 */     item.exp.visit((ExpressionVisitorVoid)this);
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   public Object onPrimitive(PrimitiveItem item) {
/* 43 */     item.exp.visit((ExpressionVisitorVoid)this);
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public Object onSuper(SuperClassItem item) {
/* 48 */     item.exp.visit((ExpressionVisitorVoid)this);
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public Object onExternal(ExternalItem item) {
/* 53 */     item.exp.visit((ExpressionVisitorVoid)this);
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\BGMWalker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */