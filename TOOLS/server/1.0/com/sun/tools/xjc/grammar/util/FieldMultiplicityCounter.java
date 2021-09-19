/*    */ package 1.0.com.sun.tools.xjc.grammar.util;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionVisitor;
/*    */ import com.sun.tools.xjc.grammar.FieldItem;
/*    */ import com.sun.tools.xjc.grammar.FieldUse;
/*    */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*    */ import com.sun.tools.xjc.grammar.util.MultiplicityCounter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldMultiplicityCounter
/*    */   extends MultiplicityCounter
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   private FieldMultiplicityCounter(String _name) {
/* 23 */     this.name = _name;
/*    */   }
/*    */   
/*    */   public static Multiplicity count(Expression exp, FieldItem fi) {
/* 27 */     return (Multiplicity)exp.visit((ExpressionVisitor)new com.sun.tools.xjc.grammar.util.FieldMultiplicityCounter(fi.name));
/*    */   }
/*    */ 
/*    */   
/*    */   public static Multiplicity count(Expression exp, FieldUse fu) {
/* 32 */     return (Multiplicity)exp.visit((ExpressionVisitor)new com.sun.tools.xjc.grammar.util.FieldMultiplicityCounter(fu.name));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Multiplicity isChild(Expression exp) {
/* 40 */     if (exp instanceof FieldItem) {
/* 41 */       FieldItem fi = (FieldItem)exp;
/* 42 */       if (fi.name.equals(this.name)) return fi.multiplicity; 
/* 43 */       return Multiplicity.zero;
/*    */     } 
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\FieldMultiplicityCounter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */