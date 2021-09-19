/*    */ package com.wurmonline.server.tutorial.stages;
/*    */ 
/*    */ import com.wurmonline.server.tutorial.TutorialStage;
/*    */ import com.wurmonline.server.utils.BMLBuilder;
/*    */ import java.awt.Color;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FinalFishingStage
/*    */   extends TutorialStage
/*    */ {
/*    */   private static final short WINDOW_ID = 2200;
/*    */   
/*    */   public FinalFishingStage(long playerId) {
/* 15 */     super(playerId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TutorialStage getNextStage() {
/* 21 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TutorialStage getLastStage() {
/* 27 */     return new RodFishingStage(getPlayerId());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void buildSubStages() {
/* 33 */     this.subStages.add(new FishingEndSubStage(getPlayerId()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public short getWindowId() {
/* 39 */     return 2200;
/*    */   }
/*    */ 
/*    */   
/*    */   public class FishingEndSubStage
/*    */     extends TutorialStage.TutorialSubStage
/*    */   {
/*    */     public FishingEndSubStage(long playerId) {
/* 47 */       super(FinalFishingStage.this, playerId);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void buildBMLString() {
/* 53 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 54 */           BMLBuilder.createCenteredNode(
/* 55 */             BMLBuilder.createVertArrayNode(false)
/* 56 */             .addText("")
/* 57 */             .addHeader("Final Tips", Color.LIGHT_GRAY)), null, 
/*    */           
/* 59 */           BMLBuilder.createVertArrayNode(false)
/* 60 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 61 */           .addText("\r\nAs well as the mentioned equipment, time and location preferences, each fish type has a base difficulty  that may need a higher Fishing skill to reliably catch.\r\n\r\nUsing the Lore action on a boat or water tile with a pole/rod/spear/net active may give you some extra information on what you are likely to catch at that time and location with the active item.\r\n\r\nCreating and carrying a supplied tacklebox will allow for automatic restocking of bait, floats and hooks when they become useless from too much damage due to fishing.\r\n\r\nNo matter where you are fishing you will have a small chance of catching clams which may contain some special items when opened with a knife.\r\n\r\n", null, null, null, 300, 400), null, 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 71 */           BMLBuilder.createLeftAlignedNode(
/* 72 */             BMLBuilder.createHorizArrayNode(false)
/* 73 */             .addButton("back", "Back", 80, 20, true)
/* 74 */             .addText("", null, null, null, 35, 0)
/* 75 */             .addButton("next", "End Tutorial", 80, 20, true)
/* 76 */             .addText("", null, null, null, 35, 0)
/* 77 */             .addButton("restart", "Restart Tutorial", " ", "Are you sure you want to restart the tutorial from the beginning?", null, false, 80, 20, true)));
/*    */ 
/*    */ 
/*    */       
/* 81 */       this.bmlString = builder.toString();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\FinalFishingStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */