/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JLabel;
/*    */ import com.sun.codemodel.JStatement;
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
/*    */ class JContinue
/*    */   implements JStatement
/*    */ {
/*    */   private final JLabel label;
/*    */   
/*    */   JContinue(JLabel _label) {
/* 23 */     this.label = _label;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 27 */     if (this.label == null) {
/* 28 */       f.p("continue;").nl();
/*    */     } else {
/* 30 */       f.p("continue").p(this.label.label).p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JContinue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */