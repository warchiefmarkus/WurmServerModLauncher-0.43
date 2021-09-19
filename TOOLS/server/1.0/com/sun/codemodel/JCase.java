/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JStatement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JCase
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression label;
/* 20 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isDefaultCase = false;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JCase(JExpression label) {
/* 31 */     this(label, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JCase(JExpression label, boolean isDefaultCase) {
/* 39 */     this.label = label;
/* 40 */     this.isDefaultCase = isDefaultCase;
/*    */   }
/*    */   
/*    */   public JExpression label() {
/* 44 */     return this.label;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 48 */     if (this.body == null) this.body = new JBlock(false, true); 
/* 49 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 53 */     f.i();
/* 54 */     if (!this.isDefaultCase) {
/* 55 */       f.p("case ").g((JGenerable)this.label).p(':').nl();
/*    */     } else {
/* 57 */       f.p("default:").nl();
/*    */     } 
/* 59 */     if (this.body != null)
/* 60 */       f.s((JStatement)this.body); 
/* 61 */     f.o();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JCase.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */