/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JClassAlreadyExistsException
/*    */   extends Exception
/*    */ {
/*    */   private final JDefinedClass existing;
/*    */   
/*    */   public JClassAlreadyExistsException(JDefinedClass _existing) {
/* 17 */     this.existing = _existing;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JDefinedClass getExistingClass() {
/* 27 */     return this.existing;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JClassAlreadyExistsException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */