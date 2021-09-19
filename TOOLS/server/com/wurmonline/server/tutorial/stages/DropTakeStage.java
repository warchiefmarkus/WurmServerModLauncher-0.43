/*     */ package com.wurmonline.server.tutorial.stages;
/*     */ 
/*     */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*     */ import com.wurmonline.server.tutorial.TutorialStage;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DropTakeStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 800;
/*     */   
/*     */   public short getWindowId() {
/*  17 */     return (short)(800 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public DropTakeStage(long playerId) {
/*  22 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  28 */     return new TerraformStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  34 */     return new WorldStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  40 */     this.subStages.add(new DropSubStage(getPlayerId()));
/*  41 */     this.subStages.add(new PlaceSubStage(getPlayerId()));
/*  42 */     this.subStages.add(new TakeSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class DropSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public DropSubStage(long playerId) {
/*  50 */       super(DropTakeStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  56 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  57 */           BMLBuilder.createCenteredNode(
/*  58 */             BMLBuilder.createVertArrayNode(false)
/*  59 */             .addText("")
/*  60 */             .addHeader("Dropping & Taking", Color.LIGHT_GRAY)), null, 
/*     */           
/*  62 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/*  63 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  64 */             .addText("\r\nRight clicking on an item in your Inventory or Character and selecting the Drop action will cause the item to drop on the ground in front of you.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/*  67 */             .addImage("image.tutorial.drop", 300, 150)
/*  68 */             .addText("\r\nA special exception to this is when dropping a Pile of Dirt or Pile of Sand, which will change the terrain at the nearest tile corner to your position. Top drop these as an item on the ground, select the Drop As Pile action.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */             
/*  72 */             .addText("").toString()), null, 
/*     */           
/*  74 */           BMLBuilder.createLeftAlignedNode(
/*  75 */             BMLBuilder.createHorizArrayNode(false)
/*  76 */             .addButton("back", "Back", 80, 20, true)
/*  77 */             .addText("", null, null, null, 35, 0)
/*  78 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  81 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class PlaceSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public PlaceSubStage(long playerId) {
/*  91 */       super(DropTakeStage.this, playerId);
/*  92 */       setNextTrigger(PlayerTutorial.PlayerTrigger.PLACED_ITEM);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  98 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  99 */           BMLBuilder.createCenteredNode(
/* 100 */             BMLBuilder.createVertArrayNode(false)
/* 101 */             .addText("")
/* 102 */             .addHeader("Dropping & Taking", Color.LIGHT_GRAY)), null, 
/*     */           
/* 104 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 105 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 106 */             .addText("\r\nYou can also select the Place option from the same menu instead of Drop. This will allow you to place the item in an exact position nearby using your mouse, with the rotation that you want.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 109 */             .addImage("image.tutorial.place", 300, 150)
/* 110 */             .addText("\r\nUse the Mouse Scroll to rotate the item while placing, Left click to confirm placement, or right click to cancel placement.\r\n\r\nPlace an item from your Inventory to continue.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */             
/* 114 */             .addText("").toString()), null, 
/*     */           
/* 116 */           BMLBuilder.createLeftAlignedNode(
/* 117 */             BMLBuilder.createHorizArrayNode(false)
/* 118 */             .addButton("back", "Back", 80, 20, true)
/* 119 */             .addText("", null, null, null, 35, 0)
/* 120 */             .addButton("next", "Waiting...", 80, 20, false)
/* 121 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 124 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class TakeSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public TakeSubStage(long playerId) {
/* 134 */       super(DropTakeStage.this, playerId);
/* 135 */       setNextTrigger(PlayerTutorial.PlayerTrigger.TAKEN_ITEM);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 141 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 142 */           BMLBuilder.createCenteredNode(
/* 143 */             BMLBuilder.createVertArrayNode(false)
/* 144 */             .addText("")
/* 145 */             .addHeader("Dropping & Taking", Color.LIGHT_GRAY)), null, 
/*     */           
/* 147 */           BMLBuilder.createVertArrayNode(false)
/* 148 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 149 */           .addText("\r\nSimilar to dropping, once an item is on the ground or in the world, you can right click it and select the Take action in order to pick it up.\r\n\r\nTake an item from the ground to continue.\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */           
/* 153 */           .addText(""), null, 
/*     */           
/* 155 */           BMLBuilder.createLeftAlignedNode(
/* 156 */             BMLBuilder.createHorizArrayNode(false)
/* 157 */             .addButton("back", "Back", 80, 20, true)
/* 158 */             .addText("", null, null, null, 35, 0)
/* 159 */             .addButton("next", "Waiting...", 80, 20, false)
/* 160 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 163 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\DropTakeStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */