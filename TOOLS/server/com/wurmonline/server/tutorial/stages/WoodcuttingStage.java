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
/*     */ public class WoodcuttingStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 1000;
/*     */   
/*     */   public short getWindowId() {
/*  17 */     return (short)(1000 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public WoodcuttingStage(long playerId) {
/*  22 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  28 */     return new CreationStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  34 */     return new TerraformStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  40 */     this.subStages.add(new CutDownSubStage(getPlayerId()));
/*  41 */     this.subStages.add(new FellTreeSubStage(getPlayerId()));
/*  42 */     this.subStages.add(new CreateLogSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class CutDownSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public CutDownSubStage(long playerId) {
/*  50 */       super(WoodcuttingStage.this, playerId);
/*  51 */       setNextTrigger(PlayerTutorial.PlayerTrigger.CUT_TREE);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  57 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  58 */           BMLBuilder.createCenteredNode(
/*  59 */             BMLBuilder.createVertArrayNode(false)
/*  60 */             .addText("")
/*  61 */             .addHeader("Woodcutting", Color.LIGHT_GRAY)), null, 
/*     */           
/*  63 */           BMLBuilder.createVertArrayNode(false)
/*  64 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  65 */           .addText("\r\nAttempting to terraform a tile that has too many trees or bushes adjacent to it may make it impossible to terraform. Fortunately you can cut them down, and the tile will return to grass once the tree has fallen.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */           
/*  69 */           .addImage("image.tutorial.cuttree", 300, 150)
/*  70 */           .addText("\r\nActivate a hatchet and select the Cut Down action on a tree tile to continue.\r\n\r\n", null, null, null, 300, 400)
/*     */           
/*  72 */           .addText(""), null, 
/*     */           
/*  74 */           BMLBuilder.createLeftAlignedNode(
/*  75 */             BMLBuilder.createHorizArrayNode(false)
/*  76 */             .addButton("back", "Back", 80, 20, true)
/*  77 */             .addText("", null, null, null, 35, 0)
/*  78 */             .addButton("next", "Waiting...", 80, 20, false)
/*  79 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/*  82 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class FellTreeSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public FellTreeSubStage(long playerId) {
/*  92 */       super(WoodcuttingStage.this, playerId);
/*  93 */       setNextTrigger(PlayerTutorial.PlayerTrigger.FELL_TREE);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  99 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 100 */           BMLBuilder.createCenteredNode(
/* 101 */             BMLBuilder.createVertArrayNode(false)
/* 102 */             .addText("")
/* 103 */             .addHeader("Woodcutting", Color.LIGHT_GRAY)), null, 
/*     */           
/* 105 */           BMLBuilder.createVertArrayNode(false)
/* 106 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 107 */           .addText("\r\nMost of the time a single swing at a tree will not be enough to fell it. Selecting the Examine action on a tree tile that has been damaged will show the current damage of the tree.\r\n\r\nWhen a tree or bush gets to 100 damage, it will fall and the tile will become grass.\r\n\r\nFinish cutting down a tree to continue.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 112 */           .addText(""), null, 
/*     */           
/* 114 */           BMLBuilder.createLeftAlignedNode(
/* 115 */             BMLBuilder.createHorizArrayNode(false)
/* 116 */             .addButton("back", "Back", 80, 20, true)
/* 117 */             .addText("", null, null, null, 35, 0)
/* 118 */             .addButton("next", "Waiting...", 80, 20, false)
/* 119 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 122 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class CreateLogSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public CreateLogSubStage(long playerId) {
/* 132 */       super(WoodcuttingStage.this, playerId);
/* 133 */       setNextTrigger(PlayerTutorial.PlayerTrigger.CREATE_LOG);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 139 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 140 */           BMLBuilder.createCenteredNode(
/* 141 */             BMLBuilder.createVertArrayNode(false)
/* 142 */             .addText("")
/* 143 */             .addHeader("Woodcutting", Color.LIGHT_GRAY)), null, 
/*     */           
/* 145 */           BMLBuilder.createVertArrayNode(false)
/* 146 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 147 */           .addText("\r\nWith a hatchet as your active item, you can select the Chop Up action on a felled tree in order to turn it into logs.\r\n\r\nLogs can then be used to craft some items, or create other resources through further processing.\r\n\r\nCreate a log from the felled tree to continue.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */           
/* 151 */           .addText(""), null, 
/*     */           
/* 153 */           BMLBuilder.createLeftAlignedNode(
/* 154 */             BMLBuilder.createHorizArrayNode(false)
/* 155 */             .addButton("back", "Back", 80, 20, true)
/* 156 */             .addText("", null, null, null, 35, 0)
/* 157 */             .addButton("next", "Waiting...", 80, 20, false)
/* 158 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 161 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\WoodcuttingStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */