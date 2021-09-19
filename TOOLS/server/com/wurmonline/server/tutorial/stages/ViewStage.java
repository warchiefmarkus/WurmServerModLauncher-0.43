/*    */ package com.wurmonline.server.tutorial.stages;
/*    */ 
/*    */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*    */ import com.wurmonline.server.tutorial.TutorialStage;
/*    */ import com.wurmonline.server.utils.BMLBuilder;
/*    */ import java.awt.Color;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ViewStage
/*    */   extends TutorialStage
/*    */ {
/*    */   private static final short WINDOW_ID = 200;
/*    */   
/*    */   public short getWindowId() {
/* 17 */     return (short)(200 + getCurrentSubStage());
/*    */   }
/*    */ 
/*    */   
/*    */   public ViewStage(long playerId) {
/* 22 */     super(playerId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TutorialStage getNextStage() {
/* 28 */     return new MovementStage(getPlayerId());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TutorialStage getLastStage() {
/* 34 */     return new WelcomeStage(getPlayerId());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void buildSubStages() {
/* 40 */     this.subStages.add(new ViewSubStage(getPlayerId()));
/*    */   }
/*    */ 
/*    */   
/*    */   public class ViewSubStage
/*    */     extends TutorialStage.TutorialSubStage
/*    */   {
/*    */     public ViewSubStage(long playerId) {
/* 48 */       super(ViewStage.this, playerId);
/* 49 */       setNextTrigger(PlayerTutorial.PlayerTrigger.MOVED_PLAYER_VIEW);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void buildBMLString() {
/* 55 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 56 */           BMLBuilder.createCenteredNode(
/* 57 */             BMLBuilder.createVertArrayNode(false)
/* 58 */             .addText("")
/* 59 */             .addHeader("Looking Around", Color.LIGHT_GRAY)), null, 
/*    */           
/* 61 */           BMLBuilder.createVertArrayNode(false)
/* 62 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 63 */           .addText("\r\nClick and hold the Left Mouse Button then move the mouse to look around at your surroundings.\r\n\r\n", null, null, null, 300, 400)
/*    */ 
/*    */           
/* 66 */           .addText(""), null, 
/*    */           
/* 68 */           BMLBuilder.createLeftAlignedNode(
/* 69 */             BMLBuilder.createHorizArrayNode(false)
/* 70 */             .addButton("back", "Back", 80, 20, true)
/* 71 */             .addText("", null, null, null, 35, 0)
/* 72 */             .addButton("next", "Waiting...", 80, 20, false)
/* 73 */             .maybeAddSkipButton()));
/*    */ 
/*    */       
/* 76 */       this.bmlString = builder.toString();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\ViewStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */