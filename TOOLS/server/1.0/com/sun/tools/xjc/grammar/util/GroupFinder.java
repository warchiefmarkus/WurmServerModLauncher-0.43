/*    */ package 1.0.com.sun.tools.xjc.grammar.util;
/*    */ 
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.ElementExp;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionVisitorBoolean;
/*    */ import com.sun.msv.grammar.InterleaveExp;
/*    */ import com.sun.msv.grammar.ListExp;
/*    */ import com.sun.msv.grammar.SequenceExp;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class GroupFinder
/*    */   extends ExpressionFinder
/*    */ {
/* 27 */   private static final ExpressionFinder theInstance = new com.sun.tools.xjc.grammar.util.GroupFinder();
/*    */ 
/*    */   
/*    */   public static boolean find(Expression e) {
/* 31 */     return e.visit((ExpressionVisitorBoolean)theInstance);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onAttribute(AttributeExp exp) {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public boolean onElement(ElementExp exp) {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public boolean onList(ListExp exp) {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onInterleave(InterleaveExp exp) {
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   public boolean onSequence(SequenceExp exp) {
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\GroupFinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */