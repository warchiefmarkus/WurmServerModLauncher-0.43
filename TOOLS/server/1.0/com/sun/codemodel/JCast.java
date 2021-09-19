/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JExpressionImpl;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JCast
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private JType type;
/*    */   private JExpression object;
/*    */   
/*    */   JCast(JType type, JExpression object) {
/* 36 */     this.type = type;
/* 37 */     this.object = object;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 41 */     f.p("((").g((JGenerable)this.type).p(')').g((JGenerable)this.object).p(')');
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JCast.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */