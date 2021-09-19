/*    */ package 1.0.com.sun.tools.xjc.grammar.util;
/*    */ 
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.DataExp;
/*    */ import com.sun.msv.grammar.ElementExp;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionVisitorBoolean;
/*    */ import com.sun.msv.grammar.ListExp;
/*    */ import com.sun.msv.grammar.MixedExp;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.msv.grammar.util.ExpressionFinder;
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
/*    */ public final class TextFinder
/*    */   extends ExpressionFinder
/*    */ {
/*    */   public static boolean find(Expression e) {
/* 26 */     return e.visit((ExpressionVisitorBoolean)theInstance);
/*    */   }
/*    */   
/* 29 */   private static final ExpressionFinder theInstance = new com.sun.tools.xjc.grammar.util.TextFinder();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onAttribute(AttributeExp exp) {
/* 35 */     return false;
/*    */   }
/*    */   public boolean onElement(ElementExp exp) {
/* 38 */     return false;
/*    */   }
/*    */   
/*    */   public boolean onAnyString() {
/* 42 */     return true;
/*    */   }
/*    */   public boolean onData(DataExp exp) {
/* 45 */     return true;
/*    */   }
/*    */   public boolean onList(ListExp exp) {
/* 48 */     return true;
/*    */   }
/*    */   public boolean onMixed(MixedExp exp) {
/* 51 */     return true;
/*    */   }
/*    */   public boolean onValue(ValueExp exp) {
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\TextFinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */