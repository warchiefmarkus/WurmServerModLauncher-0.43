/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ public abstract class DUnaryPattern
/*    */   extends DPattern
/*    */ {
/*    */   private DPattern child;
/*    */   
/*    */   public DPattern getChild() {
/* 10 */     return this.child;
/*    */   }
/*    */   
/*    */   public void setChild(DPattern child) {
/* 14 */     this.child = child;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DUnaryPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */