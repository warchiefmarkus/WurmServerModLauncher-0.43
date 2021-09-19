/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DRefPattern
/*    */   extends DPattern
/*    */ {
/*    */   private final DDefine target;
/*    */   
/*    */   public DRefPattern(DDefine target) {
/* 12 */     this.target = target;
/*    */   }
/*    */   
/*    */   public boolean isNullable() {
/* 16 */     return this.target.isNullable();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DDefine getTarget() {
/* 23 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 30 */     return this.target.getName();
/*    */   }
/*    */   
/*    */   public Object accept(DPatternVisitor visitor) {
/* 34 */     return visitor.onRef(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DRefPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */