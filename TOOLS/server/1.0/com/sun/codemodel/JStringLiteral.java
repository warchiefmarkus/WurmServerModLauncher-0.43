/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpressionImpl;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JStringLiteral
/*    */   extends JExpressionImpl
/*    */ {
/*    */   public final String str;
/*    */   
/*    */   JStringLiteral(String what) {
/* 18 */     this.str = what;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 22 */     f.p(JExpr.quotify('"', this.str));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JStringLiteral.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */