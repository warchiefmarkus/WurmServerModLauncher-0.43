/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.villages.Village;
/*    */ import java.util.Properties;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageShowPlan
/*    */   extends Question
/*    */ {
/*    */   private final Village deed;
/*    */   
/*    */   public VillageShowPlan(Creature aResponder, Village tokenVill) {
/* 45 */     super(aResponder, "Plan of " + tokenVill.getName(), "", 125, tokenVill.getId());
/* 46 */     this.deed = tokenVill;
/*    */   }
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
/*    */   public void answer(Properties aAnswers) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 69 */     int perimTiles = this.deed.getTotalPerimeterSize();
/* 70 */     getResponder().getCommunicator().sendShowDeedPlan(getId(), this.deed.getName(), this.deed
/* 71 */         .getTokenX(), this.deed.getTokenY(), this.deed
/* 72 */         .getStartX(), this.deed.getStartY(), this.deed
/* 73 */         .getEndX(), this.deed.getEndY(), perimTiles);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageShowPlan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */