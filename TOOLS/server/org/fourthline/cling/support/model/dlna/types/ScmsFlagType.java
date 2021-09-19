/*    */ package org.fourthline.cling.support.model.dlna.types;
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
/*    */ public class ScmsFlagType
/*    */ {
/*    */   private boolean copyright;
/*    */   private boolean original;
/*    */   
/*    */   public ScmsFlagType() {
/* 27 */     this.copyright = true;
/* 28 */     this.original = true;
/*    */   }
/*    */   
/*    */   public ScmsFlagType(boolean copyright, boolean original) {
/* 32 */     this.copyright = copyright;
/* 33 */     this.original = original;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCopyright() {
/* 40 */     return this.copyright;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isOriginal() {
/* 47 */     return this.original;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\types\ScmsFlagType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */