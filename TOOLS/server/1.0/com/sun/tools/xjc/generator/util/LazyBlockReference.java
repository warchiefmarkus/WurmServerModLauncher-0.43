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
/*    */ 
/*    */ 
/*    */ public abstract class LazyBlockReference
/*    */   implements BlockReference
/*    */ {
/* 17 */   private JBlock block = null;
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract JBlock create();
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock get(boolean create) {
/* 26 */     if (!create) return this.block; 
/* 27 */     if (this.block == null)
/* 28 */       this.block = create(); 
/* 29 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\util\LazyBlockReference.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */