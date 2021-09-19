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
/*    */ 
/*    */ class JBreak
/*    */   implements JStatement
/*    */ {
/*    */   private final JLabel label;
/*    */   
/*    */   JBreak(JLabel _label) {
/* 24 */     this.label = _label;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 28 */     if (this.label == null) {
/* 29 */       f.p("break;").nl();
/*    */     } else {
/* 31 */       f.p("break").p(this.label.label).p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JBreak.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */