/*     */ package com.wurmonline.server.tutorial.stages;
/*     */ 
/*     */ import com.wurmonline.server.tutorial.TutorialStage;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RodFishingStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 2100;
/*     */   
/*     */   public RodFishingStage(long playerId) {
/*  15 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  21 */     return new FinalFishingStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  27 */     return new FishingStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  33 */     this.subStages.add(new RodStartSubStage(getPlayerId()));
/*  34 */     this.subStages.add(new RodSetupSubStage(getPlayerId()));
/*  35 */     this.subStages.add(new RodActionSubStage(getPlayerId()));
/*  36 */     this.subStages.add(new RodHookingSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public short getWindowId() {
/*  42 */     return 2100;
/*     */   }
/*     */ 
/*     */   
/*     */   public class RodStartSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public RodStartSubStage(long playerId) {
/*  50 */       super(RodFishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  56 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  57 */           BMLBuilder.createCenteredNode(
/*  58 */             BMLBuilder.createVertArrayNode(false)
/*  59 */             .addText("")
/*  60 */             .addHeader("Pole/Rod Fishing", Color.LIGHT_GRAY)), null, 
/*     */           
/*  62 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/*  63 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  64 */             .addText("\r\nPole and Rod fishing is slightly more involved than net and spear fishing and will require a few more items to get going for the fish you want to catch. For pole fishing you will need a fishing pole, line, fishing hook, float, and optional bait. For rod fishing you will need all of that as well as a fishing reel.\r\n\r\nWhether you want to be pole fishing or rod fishing will depend on the fish you want to catch as there is no one-fits-all solution that all fish will like. Using the wrong equipment when seeking out some fish will make catching them become much more difficult than using the correct equipment.\r\n\r\nCheck https://www.wurmpedia.com/index.php/Fish for the equipment that you are going to need for the fish you want to catch.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  71 */             .toString()), null, 
/*     */           
/*  73 */           BMLBuilder.createLeftAlignedNode(
/*  74 */             BMLBuilder.createHorizArrayNode(false)
/*  75 */             .addButton("back", "Back", 80, 20, true)
/*  76 */             .addText("", null, null, null, 35, 0)
/*  77 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  80 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class RodSetupSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public RodSetupSubStage(long playerId) {
/*  89 */       super(RodFishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  95 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  96 */           BMLBuilder.createCenteredNode(
/*  97 */             BMLBuilder.createVertArrayNode(false)
/*  98 */             .addText("")
/*  99 */             .addHeader("Pole/Rod Fishing", Color.LIGHT_GRAY)), null, 
/*     */           
/* 101 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 102 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 103 */             .addText("\r\nOnce you have the items necessary for the fish you want to catch, insert them into the indicated empty slots to build up your pole or rod. A completed pole or rod should have all non-optional empty slots filled.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 106 */             .addImage("image.tutorial.fishing.rodinventory", 300, 150)
/* 107 */             .addText("\r\nKeeping an eye on the damage of these items during fishing is important as long battles with fish can cause items be take enough damage to break which may destroy all internal items of the part that broke.\r\n\r\n", null, null, null, 300, 400)
/*     */             
/* 109 */             .toString()), null, 
/*     */           
/* 111 */           BMLBuilder.createLeftAlignedNode(
/* 112 */             BMLBuilder.createHorizArrayNode(false)
/* 113 */             .addButton("back", "Back", 80, 20, true)
/* 114 */             .addText("", null, null, null, 35, 0)
/* 115 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 118 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class RodActionSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public RodActionSubStage(long playerId) {
/* 127 */       super(RodFishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 133 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 134 */           BMLBuilder.createCenteredNode(
/* 135 */             BMLBuilder.createVertArrayNode(false)
/* 136 */             .addText("")
/* 137 */             .addHeader("Pole/Rod Fishing", Color.LIGHT_GRAY)), null, 
/*     */           
/* 139 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 140 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 141 */             .addText("\r\nWith your rod or pole active, you can select the Fish action on a water tile or a boat you are embarked on which will start the fishing action. A target reticle will appear at your mouse position for you to select where you cast your line. Left click to cast your line or right click to cancel the action.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */             
/* 145 */             .addImage("image.tutorial.fishing.casting", 300, 150)
/* 146 */             .addText("\r\nOnce casted you'll need to wait for a fish to come along and nibble on your line.\r\n\r\n", null, null, null, 300, 400)
/* 147 */             .toString()), null, 
/*     */           
/* 149 */           BMLBuilder.createLeftAlignedNode(
/* 150 */             BMLBuilder.createHorizArrayNode(false)
/* 151 */             .addButton("back", "Back", 80, 20, true)
/* 152 */             .addText("", null, null, null, 35, 0)
/* 153 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 156 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class RodHookingSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public RodHookingSubStage(long playerId) {
/* 165 */       super(RodFishingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 171 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 172 */           BMLBuilder.createCenteredNode(
/* 173 */             BMLBuilder.createVertArrayNode(false)
/* 174 */             .addText("")
/* 175 */             .addHeader("Pole/Rod Fishing", Color.LIGHT_GRAY)), null, 
/*     */           
/* 177 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 178 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 179 */             .addText("\r\nWhen a fish nibbles on your line you'll see the float dip in the water and your rod will twitch. Left click anywhere on the screen to hook the fish and start reeling it in.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 182 */             .addImage("image.tutorial.fishing.reeling", 300, 150)
/* 183 */             .addText("\r\nThis will begin a battle between you and the fish where you'll either reel the fish in slightly or it will manage to pull slightly away from you. The winner of this battle will depend on your fishing skill and the equipment you are using to catch the fish.\r\n\r\nIf successful, you will catch the fish and it will be put into your inventory, otherwise it may jump the hook and get away.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */             
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


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\RodFishingStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */