/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JExpressionImpl;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JAtom
/*    */   extends JExpressionImpl
/*    */ {
/*    */   String what;
/*    */   
/*    */   JAtom(String what) {
/* 18 */     this.what = what;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 22 */     f.p(this.what);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JAtom.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */