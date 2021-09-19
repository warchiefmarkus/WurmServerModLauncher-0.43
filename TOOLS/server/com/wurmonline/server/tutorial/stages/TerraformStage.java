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
/*     */ public class TerraformStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 900;
/*     */   
/*     */   public short getWindowId() {
/*  17 */     return (short)(900 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public TerraformStage(long playerId) {
/*  22 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  28 */     return new WoodcuttingStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  34 */     return new DropTakeStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  40 */     this.subStages.add(new DigExplainSubStage(getPlayerId()));
/*  41 */     this.subStages.add(new PlayerDigSubStage(getPlayerId()));
/*  42 */     this.subStages.add(new FlattenSubStage(getPlayerId()));
/*  43 */     this.subStages.add(new LevelSubStage(getPlayerId()));
/*  44 */     this.subStages.add(new TileTypeSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class DigExplainSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public DigExplainSubStage(long playerId) {
/*  52 */       super(TerraformStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  58 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  59 */           BMLBuilder.createCenteredNode(
/*  60 */             BMLBuilder.createVertArrayNode(false)
/*  61 */             .addText("")
/*  62 */             .addHeader("Terraforming", Color.LIGHT_GRAY)), null, 
/*     */           
/*  64 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/*  65 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  66 */             .addText("\r\nTerraforming the land in Wurm is the first step in making the world your own.\r\n\r\nWith a shovel activated, hovering your mouse over the border between tiles in the world will show a slope between the tile corners.\r\n\r\nCompleting the digging action on a tile will lower the closest tile corner by 1, and dropping a pile of dirt or pile of sand will raise the closest tile corner by 1.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  72 */             .addImage("image.tutorial.digging", 300, 150)
/*  73 */             .addText("").toString()), null, 
/*     */           
/*  75 */           BMLBuilder.createLeftAlignedNode(
/*  76 */             BMLBuilder.createHorizArrayNode(false)
/*  77 */             .addButton("back", "Back", 80, 20, true)
/*  78 */             .addText("", null, null, null, 35, 0)
/*  79 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  82 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class PlayerDigSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public PlayerDigSubStage(long playerId) {
/*  92 */       super(TerraformStage.this, playerId);
/*  93 */       setNextTrigger(PlayerTutorial.PlayerTrigger.DIG_TILE);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  99 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 100 */           BMLBuilder.createCenteredNode(
/* 101 */             BMLBuilder.createVertArrayNode(false)
/* 102 */             .addText("")
/* 103 */             .addHeader("Terraforming", Color.LIGHT_GRAY)), null, 
/*     */           
/* 105 */           BMLBuilder.createVertArrayNode(false)
/* 106 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 107 */           .addText("\r\nThe slope of land you'll be able to terraform is restricted by your Digging skill, where you can dig in slopes of up to your skill multiplied by 3.\r\n\r\nStart off by digging some dirt nearby to continue.\r\n\r\n", null, null, null, 300, 400)
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
/*     */ 
/*     */ 
/*     */   
/*     */   public class FlattenSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public FlattenSubStage(long playerId) {
/* 131 */       super(TerraformStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 137 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 138 */           BMLBuilder.createCenteredNode(
/* 139 */             BMLBuilder.createVertArrayNode(false)
/* 140 */             .addText("")
/* 141 */             .addHeader("Terraforming", Color.LIGHT_GRAY)), null, 
/*     */           
/* 143 */           BMLBuilder.createVertArrayNode(false)
/* 144 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 145 */           .addText("\r\nWhen there are no height differences between corners of a tile, the slope will become flat. When all borders of a tile are flat, the entire tile is then flat.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */           
/* 148 */           .addImage("image.tutorial.flatten", 300, 150)
/* 149 */           .addText("\r\nUsing the Flatten action on a tile will attempt to get the tile as flat as possible if there is enough dirt on the tile to move around. Flat tiles are necessary for planning buildings.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */           
/* 152 */           .addText(""), null, 
/*     */           
/* 154 */           BMLBuilder.createLeftAlignedNode(
/* 155 */             BMLBuilder.createHorizArrayNode(false)
/* 156 */             .addButton("back", "Back", 80, 20, true)
/* 157 */             .addText("", null, null, null, 35, 0)
/* 158 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 161 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class LevelSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public LevelSubStage(long playerId) {
/* 171 */       super(TerraformStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 177 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 178 */           BMLBuilder.createCenteredNode(
/* 179 */             BMLBuilder.createVertArrayNode(false)
/* 180 */             .addText("")
/* 181 */             .addHeader("Terraforming", Color.LIGHT_GRAY)), null, 
/*     */           
/* 183 */           BMLBuilder.createVertArrayNode(false)
/* 184 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 185 */           .addText("\r\nOnce a tile has become flat, you can stand on the flat tile and use the Level action on adjacent tiles to terraform them to the same height as your current tile.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */           
/* 188 */           .addImage("image.tutorial.level", 300, 150)
/* 189 */           .addText("\r\nIf a tile needs extra dirt or sand to be dropped on it to become the same level as your current tile, dirt and sand will be used from your inventory automatically.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */           
/* 192 */           .addText(""), null, 
/*     */           
/* 194 */           BMLBuilder.createLeftAlignedNode(
/* 195 */             BMLBuilder.createHorizArrayNode(false)
/* 196 */             .addButton("back", "Back", 80, 20, true)
/* 197 */             .addText("", null, null, null, 35, 0)
/* 198 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 201 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class TileTypeSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public TileTypeSubStage(long playerId) {
/* 211 */       super(TerraformStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 217 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 218 */           BMLBuilder.createCenteredNode(
/* 219 */             BMLBuilder.createVertArrayNode(false)
/* 220 */             .addText("")
/* 221 */             .addHeader("Terraforming", Color.LIGHT_GRAY)), null, 
/*     */           
/* 223 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 224 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 225 */             .addText("\r\nSome land types will not be as easily terraformed as dirt and sand tiles. Tiles such as as Clay and Tar may take a lot more effort to shape, however their resources can be used for a lot more than just shaping the land.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 228 */             .addImage("image.tutorial.tiletypes", 300, 150)
/* 229 */             .addText("\r\nDigging on different tile types will give you an item of the same type. Only dirt and sand can be used to raise land for terraforming, but every resource has its use outside of terraforming.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 232 */             .addText("").toString()), null, 
/*     */           
/* 234 */           BMLBuilder.createLeftAlignedNode(
/* 235 */             BMLBuilder.createHorizArrayNode(false)
/* 236 */             .addButton("back", "Back", 80, 20, true)
/* 237 */             .addText("", null, null, null, 35, 0)
/* 238 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 241 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\TerraformStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */