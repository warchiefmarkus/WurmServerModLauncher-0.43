/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JFormatter;
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
/*    */ 
/*    */ public class JLabel
/*    */   implements JStatement
/*    */ {
/*    */   final String label;
/*    */   
/*    */   JLabel(String _label) {
/* 24 */     this.label = _label;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 28 */     f.p(this.label + ':').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JLabel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */