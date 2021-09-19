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
/*     */ public class MovementStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 300;
/*     */   
/*     */   public short getWindowId() {
/*  21 */     return (short)(300 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public MovementStage(long playerId) {
/*  26 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  32 */     return new InventoryStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  38 */     return new ViewStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  44 */     this.subStages.add(new WASDSubStage(getPlayerId()));
/*  45 */     this.subStages.add(new ClimbingOnSubStage(getPlayerId()));
/*  46 */     this.subStages.add(new ClimbingOffSubStage(getPlayerId()));
/*  47 */     this.subStages.add(new HealthStaminaSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class WASDSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public WASDSubStage(long playerId) {
/*  55 */       super(MovementStage.this, playerId);
/*  56 */       setNextTrigger(PlayerTutorial.PlayerTrigger.MOVED_PLAYER);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  62 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  63 */           BMLBuilder.createCenteredNode(
/*  64 */             BMLBuilder.createVertArrayNode(false)
/*  65 */             .addText("")
/*  66 */             .addHeader("Movement", Color.LIGHT_GRAY)), null, 
/*     */           
/*  68 */           BMLBuilder.createVertArrayNode(false)
/*  69 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  70 */           .addText("\r\nUse the [$bind:MOVE_FORWARD$], [$bind:MOVE_LEFT$], [$bind:MOVE_BACK$] and [$bind:MOVE_RIGHT$] keys in order to move around.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */           
/*  73 */           .addText(""), null, 
/*     */           
/*  75 */           BMLBuilder.createLeftAlignedNode(
/*  76 */             BMLBuilder.createHorizArrayNode(false)
/*  77 */             .addButton("back", "Back", 80, 20, true)
/*  78 */             .addText("", null, null, null, 35, 0)
/*  79 */             .addButton("next", "Waiting...", 80, 20, false)
/*  80 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/*  83 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class ClimbingOnSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public ClimbingOnSubStage(long playerId) {
/*  93 */       super(MovementStage.this, playerId);
/*  94 */       setNextTrigger(PlayerTutorial.PlayerTrigger.ENABLED_CLIMBING);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 100 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 101 */           BMLBuilder.createCenteredNode(
/* 102 */             BMLBuilder.createVertArrayNode(false)
/* 103 */             .addText("")
/* 104 */             .addHeader("Movement", Color.LIGHT_GRAY)), null, 
/*     */           
/* 106 */           BMLBuilder.createVertArrayNode(false)
/* 107 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 108 */           .addText("\r\nSome land in Wurm can be too steep for you to simply walk across.\r\n\r\nEnable climbing mode by pressing $bind:TOGGLE_CLIMB$ or the climb button in order to climb steep slopes.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */           
/* 111 */           .addImage("image.tutorial.climbing", 300, 150)
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 130 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 131 */         p.getCommunicator().sendOpenWindow((short)9, true);
/* 132 */         p.getCommunicator().sendToggleQuickbarBtn((short)2001, true);
/*     */       }
/* 134 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class ClimbingOffSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public ClimbingOffSubStage(long playerId) {
/* 147 */       super(MovementStage.this, playerId);
/* 148 */       setNextTrigger(PlayerTutorial.PlayerTrigger.DISABLED_CLIMBING);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 154 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 155 */           BMLBuilder.createCenteredNode(
/* 156 */             BMLBuilder.createVertArrayNode(false)
/* 157 */             .addText("")
/* 158 */             .addHeader("Movement", Color.LIGHT_GRAY)), null, 
/*     */           
/* 160 */           BMLBuilder.createVertArrayNode(false)
/* 161 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 162 */           .addText("\r\nWhile climbing mode is enabled, you'll walk a lot slower and drain more stamina than usual.\r\n\r\nDisable climbing mode by pressing $bind:TOGGLE_CLIMB$ or the climb button again.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */           
/* 165 */           .addText(""), null, 
/*     */           
/* 167 */           BMLBuilder.createLeftAlignedNode(
/* 168 */             BMLBuilder.createHorizArrayNode(false)
/* 169 */             .addButton("back", "Back", 80, 20, true)
/* 170 */             .addText("", null, null, null, 35, 0)
/* 171 */             .addButton("next", "Waiting...", 80, 20, false)
/* 172 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 175 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class HealthStaminaSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public HealthStaminaSubStage(long playerId) {
/* 185 */       super(MovementStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 191 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 192 */           BMLBuilder.createCenteredNode(
/* 193 */             BMLBuilder.createVertArrayNode(false)
/* 194 */             .addText("")
/* 195 */             .addHeader("Movement", Color.LIGHT_GRAY)), null, 
/*     */           
/* 197 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 198 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 199 */             .addText("\r\nKeep an eye on your stamina while moving, climbing or doing actions.\r\n\r\nRunning out of stamina can lead to your character getting tired and walking slower, completing actions slower, or possibly falling from a steep slope if you are climbing.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */             
/* 203 */             .addImage("image.tutorial.stamina", 300, 100)
/* 204 */             .addText("\r\nTo regain your stamina simply stand still for a few seconds. You cannot regain stamina while climbing is toggled on.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 207 */             .addText("").toString()), null, 
/*     */           
/* 209 */           BMLBuilder.createLeftAlignedNode(
/* 210 */             BMLBuilder.createHorizArrayNode(false)
/* 211 */             .addButton("back", "Back", 80, 20, true)
/* 212 */             .addText("", null, null, null, 35, 0)
/* 213 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 216 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 224 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 225 */         p.getCommunicator().sendOpenWindow((short)5, true);
/* 226 */         p.getCommunicator().sendOpenWindow((short)13, false);
/*     */       }
/* 228 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\MovementStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */