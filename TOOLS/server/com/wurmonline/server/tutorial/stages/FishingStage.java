/*     */ package com.wurmonline.server.tutorial.stages;
/*     */ 
/*     */ import com.wurmonline.server.tutorial.TutorialStage;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FishingStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 2000;
/*     */   
/*     */   public FishingStage(long playerId) {
/*  15 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  21 */     return new RodFishingStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  27 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  33 */     this.subStages.add(new FishingStartSubStage(getPlayerId()));
/*  34 */     this.subStages.add(new FishModifiersSubStage(getPlayerId()));
/*  35 */     this.subStages.add(new NettingSubStage(getPlayerId()));
/*  36 */     this.subStages.add(new SpearingSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public short getWindowId() {
/*  42 */     return 2000;
/*     */   }
/*     */ 
/*     */   
/*     */   public class FishingStartSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public FishingStartSubStage(long playerId) {
/*  50 */       super(FishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  56 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  57 */           BMLBuilder.createCenteredNode(
/*  58 */             BMLBuilder.createVertArrayNode(false)
/*  59 */             .addText("")
/*  60 */             .addHeader("Fishing Intro", Color.LIGHT_GRAY)), null, 
/*     */           
/*  62 */           BMLBuilder.createVertArrayNode(false)
/*  63 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  64 */           .addText("\r\nIn this tutorial you will learn about the different kinds of fishing in Wurm, what you need and how to do it, and what you can expect to catch.\r\n\r\nThere are currently 3 main types of fishing you can find in Wurm: Net Fishing, Spear Fishing and Rod/Pole Fishing. Each have their own sets of items and actions involved and may catch you different kinds of fish.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  69 */           .addImage("image.tutorial.fishing.types", 300, 150)
/*  70 */           .addText(""), null, 
/*     */           
/*  72 */           BMLBuilder.createLeftAlignedNode(
/*  73 */             BMLBuilder.createHorizArrayNode(false)
/*  74 */             .addButton("back", "Back", 80, 20, false)
/*  75 */             .addText("", null, null, null, 35, 0)
/*  76 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  79 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class FishModifiersSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public FishModifiersSubStage(long playerId) {
/*  88 */       super(FishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  94 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  95 */           BMLBuilder.createCenteredNode(
/*  96 */             BMLBuilder.createVertArrayNode(false)
/*  97 */             .addText("")
/*  98 */             .addHeader("Fishing Intro", Color.LIGHT_GRAY)), null, 
/*     */           
/* 100 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 101 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 102 */             .addText("\r\nThe first stage of fishing is deciding what you want to catch. Your chances of catching each fish type can change based on time of day, where you are fishing, depth of the water, float, bait, and fishing rod types. Aligning all of these to the fish you want will give you a much better chance of catching good quality fish.\r\n\r\nAs an example, your best chance at catching pike is on a lake in the evening or night with water up to 10m deep using a basic or fine rod with moss for a float and flies as bait.\r\n\r\nYou will still be able to catch pike if you do not meet all of the above conditions, but you may come across them less often and have a lower chance to reel them in when you do find them.\r\n\r\nFull info about all fish types and chance modifiers can be found here: https://www.wurmpedia.com/index.php/Fish\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 110 */             .toString()), null, 
/*     */           
/* 112 */           BMLBuilder.createLeftAlignedNode(
/* 113 */             BMLBuilder.createHorizArrayNode(false)
/* 114 */             .addButton("back", "Back", 80, 20, true)
/* 115 */             .addText("", null, null, null, 35, 0)
/* 116 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 119 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class NettingSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public NettingSubStage(long playerId) {
/* 128 */       super(FishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 134 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 135 */           BMLBuilder.createCenteredNode(
/* 136 */             BMLBuilder.createVertArrayNode(false)
/* 137 */             .addText("")
/* 138 */             .addHeader("Net Fishing", Color.LIGHT_GRAY)), null, 
/*     */           
/* 140 */           BMLBuilder.createVertArrayNode(false)
/* 141 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 142 */           .addText("\r\nNet fishing can be done in shallow waters using a fishing net and is a good way to catch small fish, including those used as bait in rod/pole fishing like roaches and minnow.\r\n\r\nTo start net fishing, simply activate your fishing net and choose the Fish action on a shallow water tile. You will start catching fish in your net which will need to be emptied before starting your next net fishing action.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 147 */           .addImage("image.tutorial.fishing.netting", 300, 150)
/* 148 */           .addText(""), null, 
/*     */           
/* 150 */           BMLBuilder.createLeftAlignedNode(
/* 151 */             BMLBuilder.createHorizArrayNode(false)
/* 152 */             .addButton("back", "Back", 80, 20, true)
/* 153 */             .addText("", null, null, null, 35, 0)
/* 154 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 157 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class SpearingSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public SpearingSubStage(long playerId) {
/* 166 */       super(FishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 172 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 173 */           BMLBuilder.createCenteredNode(
/* 174 */             BMLBuilder.createVertArrayNode(false)
/* 175 */             .addText("")
/* 176 */             .addHeader("Spear Fishing", Color.LIGHT_GRAY)), null, 
/*     */           
/* 178 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 179 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 180 */             .addText("\r\nSpear fishing is also an action completed in shallow waters using either a spear or long spear and can be used to catch small fish as well as the odd larger fish.\r\n\r\nActivate a spear and choose the Fish action on a shallow water tile to start spear fishing. A target reticle will appear at your mouse position and can be used to line up your spear when a fish swims past. Wait for a fish to get close to you then left click on it to throw your spear at it and catch it.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 186 */             .addImage("image.tutorial.fishing.spearing", 300, 150)
/* 187 */             .addText("\r\nRight clicking while the targeting reticle is active will cancel the action and you will put your spear away.\r\n\r\n", null, null, null, 300, 400)
/* 188 */             .toString()), null, 
/*     */           
/* 190 */           BMLBuilder.createLeftAlignedNode(
/* 191 */             BMLBuilder.createHorizArrayNode(false)
/* 192 */             .addButton("back", "Back", 80, 20, true)
/* 193 */             .addText("", null, null, null, 35, 0)
/* 194 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 197 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\FishingStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */