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
/*     */ public class MiningStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 1200;
/*     */   
/*     */   public short getWindowId() {
/*  17 */     return (short)(1200 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public MiningStage(long playerId) {
/*  22 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  28 */     return new SkillsStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  34 */     return new CreationStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  40 */     this.subStages.add(new DigRockSubStage(getPlayerId()));
/*  41 */     this.subStages.add(new MineIronSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class DigRockSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public DigRockSubStage(long playerId) {
/*  49 */       super(MiningStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  55 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  56 */           BMLBuilder.createCenteredNode(
/*  57 */             BMLBuilder.createVertArrayNode(false)
/*  58 */             .addText("")
/*  59 */             .addHeader("Mining", Color.LIGHT_GRAY)), null, 
/*     */           
/*  61 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/*  62 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  63 */             .addText("\r\nMany things in Wurm need metals in their creation process, and to get these metals you will need to get underground in order to mine them.\r\n\r\nThe first step to opening a mine is completing the Tunnel action on a Rock tile with a Pickaxe activated.\r\n\r\nRock tiles can be found in the world, or uncovered by digging up all of the dirt or sand on a tile. Once all 4 corners of a tile have no more dirt or sand to dig the tile will turn to Rock.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  69 */             .addImage("image.tutorial.rock", 300, 150)
/*  70 */             .addText("").toString()), null, 
/*     */           
/*  72 */           BMLBuilder.createLeftAlignedNode(
/*  73 */             BMLBuilder.createHorizArrayNode(false)
/*  74 */             .addButton("back", "Back", 80, 20, true)
/*  75 */             .addText("", null, null, null, 35, 0)
/*  76 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  79 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class MineIronSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public MineIronSubStage(long playerId) {
/*  89 */       super(MiningStage.this, playerId);
/*  90 */       setNextTrigger(PlayerTutorial.PlayerTrigger.MINE_IRON);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  96 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  97 */           BMLBuilder.createCenteredNode(
/*  98 */             BMLBuilder.createVertArrayNode(false)
/*  99 */             .addText("")
/* 100 */             .addHeader("Mining", Color.LIGHT_GRAY)), null, 
/*     */           
/* 102 */           BMLBuilder.createVertArrayNode(false)
/* 103 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 104 */           .addText("\r\nOnce you have opened a tunnel, mining deeper is a matter of destroying cave walls using the Mine action.\r\n\r\nOccasionally you will find metal ore veins or special stone veins such as marble, slate and sandstone. These veins may take a lot longer to destroy than normal rock walls.\r\n\r\nRock, marble, slate and sandstone shards can be shaped into bricks and other items for use in Masonry, and metal ores can be smelted down in a Furnace or Smelter for useable metal lumps.\r\n\r\nMine some Iron Ore to continue.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 111 */           .addText(""), null, 
/*     */           
/* 113 */           BMLBuilder.createLeftAlignedNode(
/* 114 */             BMLBuilder.createHorizArrayNode(false)
/* 115 */             .addButton("back", "Back", 80, 20, true)
/* 116 */             .addText("", null, null, null, 35, 0)
/* 117 */             .addButton("next", "Waiting...", 80, 20, false)
/* 118 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 121 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\MiningStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */