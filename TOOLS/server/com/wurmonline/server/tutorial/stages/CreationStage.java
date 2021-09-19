/*     */ package com.wurmonline.server.tutorial.stages;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*     */ import com.wurmonline.server.tutorial.TutorialStage;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreationStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 1100;
/*     */   
/*     */   public short getWindowId() {
/*  21 */     return (short)(1100 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public CreationStage(long playerId) {
/*  26 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  32 */     return new MiningStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  38 */     return new WoodcuttingStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  44 */     this.subStages.add(new CreationWindowSubStage(getPlayerId()));
/*  45 */     this.subStages.add(new CreateKindlingSubStage(getPlayerId()));
/*  46 */     this.subStages.add(new CreateCampfireSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class CreationWindowSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public CreationWindowSubStage(long playerId) {
/*  54 */       super(CreationStage.this, playerId);
/*  55 */       setNextTrigger(PlayerTutorial.PlayerTrigger.ENABLED_CREATION);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  61 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  62 */           BMLBuilder.createCenteredNode(
/*  63 */             BMLBuilder.createVertArrayNode(false)
/*  64 */             .addText("")
/*  65 */             .addHeader("Creating Items", Color.LIGHT_GRAY)), null, 
/*     */           
/*  67 */           BMLBuilder.createVertArrayNode(false)
/*  68 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  69 */           .addText("\r\nMost item creation in Wurm can be done in one of two ways - using the Crafting Window, or by activating an item and selecting the relevant Create action on another item.\r\n\r\nLet's make some kindling with the Crafting Window. Click the Crafting Window button to open the window.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */           
/*  73 */           .addImage("image.tutorial.creation", 300, 150)
/*  74 */           .addText(""), null, 
/*     */           
/*  76 */           BMLBuilder.createLeftAlignedNode(
/*  77 */             BMLBuilder.createHorizArrayNode(false)
/*  78 */             .addButton("back", "Back", 80, 20, true)
/*  79 */             .addText("", null, null, null, 35, 0)
/*  80 */             .addButton("next", "Waiting...", 80, 20, false)
/*  81 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/*  84 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/*  92 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/*  93 */         p.getCommunicator().sendToggleQuickbarBtn((short)2011, true);
/*     */       }
/*  95 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class CreateKindlingSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public CreateKindlingSubStage(long playerId) {
/* 108 */       super(CreationStage.this, playerId);
/* 109 */       setNextTrigger(PlayerTutorial.PlayerTrigger.CREATE_KINDLING);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 115 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 116 */           BMLBuilder.createCenteredNode(
/* 117 */             BMLBuilder.createVertArrayNode(false)
/* 118 */             .addText("")
/* 119 */             .addHeader("Creating Items", Color.LIGHT_GRAY)), null, 
/*     */           
/* 121 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 122 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 123 */             .addText("\r\nDrag your hatchet from your inventory to one side of the Creation Window, and a Log from your inventory to the other side of the Creation Window.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 126 */             .addImage("image.tutorial.creation2", 300, 150)
/* 127 */             .addText("\r\nSelect Kindling from the list on the right hand side, then click the Create button.\r\n\r\nCreate some kindling to continue.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 130 */             .addText("").toString()), null, 
/*     */           
/* 132 */           BMLBuilder.createLeftAlignedNode(
/* 133 */             BMLBuilder.createHorizArrayNode(false)
/* 134 */             .addButton("back", "Back", 80, 20, true)
/* 135 */             .addText("", null, null, null, 35, 0)
/* 136 */             .addButton("next", "Waiting...", 80, 20, false)
/* 137 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 140 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class CreateCampfireSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public CreateCampfireSubStage(long playerId) {
/* 150 */       super(CreationStage.this, playerId);
/* 151 */       setNextTrigger(PlayerTutorial.PlayerTrigger.CREATE_CAMPFIRE);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 157 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 158 */           BMLBuilder.createCenteredNode(
/* 159 */             BMLBuilder.createVertArrayNode(false)
/* 160 */             .addText("")
/* 161 */             .addHeader("Creating Items", Color.LIGHT_GRAY)), null, 
/*     */           
/* 163 */           BMLBuilder.createVertArrayNode(false)
/* 164 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 165 */           .addText("\r\nNow you can create a Campfire using the kindling you just made and the steel & flint in your inventory.\r\n\r\nActivate the steel & flint, then right click the kindling and select the Create>Campfire action.\r\n\r\nAlternatively you can drag the steel & flint and kindling items from your inventory to the Creation Window and select campfire from the list on the right, then click Create.\r\n\r\nCreate a campfire to continue.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 171 */           .addText(""), null, 
/*     */           
/* 173 */           BMLBuilder.createLeftAlignedNode(
/* 174 */             BMLBuilder.createHorizArrayNode(false)
/* 175 */             .addButton("back", "Back", 80, 20, true)
/* 176 */             .addText("", null, null, null, 35, 0)
/* 177 */             .addButton("next", "Waiting...", 80, 20, false)
/* 178 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 181 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 189 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 190 */         p.getCommunicator().sendToggleQuickbarBtn((short)2012, true);
/*     */       }
/* 192 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\CreationStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */