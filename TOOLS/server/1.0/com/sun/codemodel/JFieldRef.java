/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JAssignmentTarget;
/*    */ import com.sun.codemodel.JExpr;
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
/*    */ 
/*    */ public class JFieldRef
/*    */   extends JExpressionImpl
/*    */   implements JAssignmentTarget
/*    */ {
/*    */   private JGenerable object;
/*    */   private String name;
/*    */   private boolean explicitThis;
/*    */   
/*    */   JFieldRef(JExpression object, String name) {
/* 41 */     this((JGenerable)object, name, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JFieldRef(JType type, String name) {
/* 48 */     this((JGenerable)type, name, false);
/*    */   }
/*    */   
/*    */   JFieldRef(JGenerable object, String name, boolean explicitThis) {
/* 52 */     this.explicitThis = explicitThis;
/* 53 */     this.object = object;
/* 54 */     if (name.indexOf('.') >= 0)
/* 55 */       throw new IllegalArgumentException("Field name contains '.': " + name); 
/* 56 */     this.name = name;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 60 */     if (this.object != null) {
/* 61 */       f.g(this.object).p('.').p(this.name);
/*    */     }
/* 63 */     else if (this.explicitThis) {
/* 64 */       f.p("this.").p(this.name);
/*    */     } else {
/* 66 */       f.p(this.name);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public JExpression assign(JExpression rhs) {
/* 72 */     return JExpr.assign(this, rhs);
/*    */   }
/*    */   public JExpression assignPlus(JExpression rhs) {
/* 75 */     return JExpr.assignPlus(this, rhs);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JFieldRef.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */