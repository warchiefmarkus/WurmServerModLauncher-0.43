/*    */ package com.wurmonline.server.creatures.ai;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Idea
/*    */ {
/* 28 */   private int priority = 0;
/*    */ 
/*    */   
/*    */   static final int MOVEMENTPRIO = 0;
/*    */   
/*    */   static final int PURCHASEPRIO = 1;
/*    */   
/*    */   static final int BUILDPRIO = 0;
/*    */   
/*    */   static final int ATTACKPRIO = 5;
/*    */   
/*    */   static final int DEFENDPRIO = 7;
/*    */   
/*    */   static final int RETREATPRIO = 8;
/*    */   
/*    */   static final int SCOUTPRIO = 3;
/*    */ 
/*    */   
/*    */   public abstract boolean resolve();
/*    */ 
/*    */   
/*    */   public final int getPriority() {
/* 50 */     return this.priority;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setPriority(int aPriority) {
/* 59 */     this.priority = aPriority;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\Idea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */