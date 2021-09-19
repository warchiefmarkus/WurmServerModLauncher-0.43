/*    */ package 1.0.com.sun.tools.xjc.grammar.util;
/*    */ 
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.ChoiceNameClass;
/*    */ import com.sun.msv.grammar.ElementExp;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*    */ import com.sun.msv.grammar.NameClass;
/*    */ import com.sun.msv.grammar.ReferenceExp;
/*    */ import com.sun.msv.grammar.util.ExpressionWalker;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public abstract class NameFinder
/*    */   extends ExpressionWalker
/*    */ {
/*    */   public static NameClass findElement(Expression e) {
/* 28 */     return find(e, (com.sun.tools.xjc.grammar.util.NameFinder)new Object());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NameClass findAttribute(Expression e) {
/* 35 */     return find(e, (com.sun.tools.xjc.grammar.util.NameFinder)new Object());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static NameClass find(Expression e, com.sun.tools.xjc.grammar.util.NameFinder f) {
/* 43 */     e.visit((ExpressionVisitorVoid)f);
/* 44 */     if (f.nc == null) return NameClass.NONE; 
/* 45 */     return f.nc.simplify();
/*    */   }
/*    */   
/* 48 */   private NameClass nc = null;
/* 49 */   private final Set visited = new HashSet();
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onName(NameClass child) {
/* 54 */     if (this.nc == null) { this.nc = child; }
/*    */     else
/* 56 */     { this.nc = (NameClass)new ChoiceNameClass(this.nc, child); }
/*    */   
/*    */   } public void onRef(ReferenceExp exp) {
/* 59 */     if (this.visited.add(exp))
/* 60 */       super.onRef(exp); 
/*    */   }
/*    */   
/*    */   public void onAttribute(AttributeExp exp) {}
/*    */   
/*    */   public void onElement(ElementExp exp) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\gramma\\util\NameFinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */