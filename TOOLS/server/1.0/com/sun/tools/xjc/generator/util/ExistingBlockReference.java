/*    */ package 1.0.com.sun.tools.xjc.generator.util;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExistingBlockReference
/*    */   implements BlockReference
/*    */ {
/*    */   private final JBlock block;
/*    */   
/*    */   public ExistingBlockReference(JBlock _block) {
/* 18 */     this.block = _block;
/*    */   }
/*    */   
/*    */   public JBlock get(boolean create) {
/* 22 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\util\ExistingBlockReference.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */