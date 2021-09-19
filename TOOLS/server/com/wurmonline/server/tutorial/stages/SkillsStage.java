/*     */ package com.wurmonline.server.tutorial.stages;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.tutorial.TutorialStage;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkillsStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 1300;
/*     */   
/*     */   public short getWindowId() {
/*  20 */     return (short)(1300 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public SkillsStage(long playerId) {
/*  25 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  31 */     return new CombatStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  37 */     return new MiningStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  43 */     this.subStages.add(new SkillWindowSubStage(getPlayerId()));
/*  44 */     this.subStages.add(new SkillGainSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class SkillWindowSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public SkillWindowSubStage(long playerId) {
/*  52 */       super(SkillsStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  58 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  59 */           BMLBuilder.createCenteredNode(
/*  60 */             BMLBuilder.createVertArrayNode(false)
/*  61 */             .addText("")
/*  62 */             .addHeader("Skills", Color.LIGHT_GRAY)), null, 
/*     */           
/*  64 */           BMLBuilder.createVertArrayNode(false)
/*  65 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  66 */           .addText("\r\nMost actions you can complete in Wurm also raise a related Skill, and are sometimes limited by the level you have in the related Skill.\r\n\r\nAll skills start out at level 1, and will slowly increase as you complete actions - up to 100. However the higher the skill, the slower the increases will come.\r\n\r\nAs a free player on Wurm Online your skills are limited to a maximum of 20. This limit is then removed while you are a premium player or during special event weekends.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  73 */           .addText(""), null, 
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/*  90 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/*  91 */         p.getCommunicator().sendOpenWindow((short)16, true);
/*  92 */         p.getCommunicator().sendToggleQuickbarBtn((short)2008, true);
/*     */       }
/*  94 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class SkillGainSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public SkillGainSubStage(long playerId) {
/* 107 */       super(SkillsStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 113 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 114 */           BMLBuilder.createCenteredNode(
/* 115 */             BMLBuilder.createVertArrayNode(false)
/* 116 */             .addText("")
/* 117 */             .addHeader("Skills", Color.LIGHT_GRAY)), null, 
/*     */           
/* 119 */           BMLBuilder.createVertArrayNode(false)
/* 120 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 121 */           .addText("\r\nThere are various ways to increase the amount of skill gain you receive from an action such as Affinities, Item Enchantments and Sleep Bonus.\r\n\r\nSleep Bonus is gained while you are offline and have logged out from a bed or from completing special missions. When you have some Sleep Bonus, you can enable it from the quickbar to double any skill gains (with a few exceptions) you receive while Sleep Bonus is active.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 127 */           .addImage("image.tutorial.sleepbonus", 300, 100)
/* 128 */           .addText(""), null, 
/*     */           
/* 130 */           BMLBuilder.createLeftAlignedNode(
/* 131 */             BMLBuilder.createHorizArrayNode(false)
/* 132 */             .addButton("back", "Back", 80, 20, true)
/* 133 */             .addText("", null, null, null, 35, 0)
/* 134 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 137 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 145 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 146 */         p.getCommunicator().sendToggleQuickbarBtn((short)2006, true);
/*     */       }
/* 148 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\SkillsStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */