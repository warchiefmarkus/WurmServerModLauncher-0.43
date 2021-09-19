/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class JournalReward
/*    */ {
/*    */   private final String rewardDescription;
/*    */   
/*    */   public abstract void runReward(Player paramPlayer);
/*    */   
/*    */   public JournalReward(String rewardDescription) {
/* 12 */     this.rewardDescription = rewardDescription;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRewardDesc() {
/* 17 */     return this.rewardDescription;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\JournalReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */