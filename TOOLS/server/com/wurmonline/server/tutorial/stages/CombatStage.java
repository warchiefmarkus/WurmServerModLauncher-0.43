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
/*     */ public class CombatStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 1400;
/*     */   
/*     */   public short getWindowId() {
/*  20 */     return (short)(1400 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public CombatStage(long playerId) {
/*  25 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  31 */     return new FinalStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  37 */     return new SkillsStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  43 */     this.subStages.add(new WarningSubStage(getPlayerId()));
/*  44 */     this.subStages.add(new OutlineSubStage(getPlayerId()));
/*  45 */     this.subStages.add(new TargetSubStage(getPlayerId()));
/*  46 */     this.subStages.add(new SimpleSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class WarningSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public WarningSubStage(long playerId) {
/*  54 */       super(CombatStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  60 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  61 */           BMLBuilder.createCenteredNode(
/*  62 */             BMLBuilder.createVertArrayNode(false)
/*  63 */             .addText("")
/*  64 */             .addHeader("Combat", Color.LIGHT_GRAY)), null, 
/*     */           
/*  66 */           BMLBuilder.createVertArrayNode(false)
/*  67 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  68 */           .addText("\r\nThe lands of wurm are dangerous and there are many threats. It is not uncommon to come face to face with dangers such as spiders, bears and trolls during your adventures.\r\n\r\nIt is recommended not to underestimate any creature you may come up against, as even something as small as a rat may cause you issues as you start your Wurm adventure.\r\n\r\nMake sure you have equipped armour and weapons before exploring the world, or you may soon come to regret it.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  74 */           .addText(""), null, 
/*     */           
/*  76 */           BMLBuilder.createLeftAlignedNode(
/*  77 */             BMLBuilder.createHorizArrayNode(false)
/*  78 */             .addButton("back", "Back", 80, 20, true)
/*  79 */             .addText("", null, null, null, 35, 0)
/*  80 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  83 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class OutlineSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public OutlineSubStage(long playerId) {
/*  93 */       super(CombatStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  99 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 100 */           BMLBuilder.createCenteredNode(
/* 101 */             BMLBuilder.createVertArrayNode(false)
/* 102 */             .addText("")
/* 103 */             .addHeader("Combat", Color.LIGHT_GRAY)), null, 
/*     */           
/* 105 */           BMLBuilder.createVertArrayNode(false)
/* 106 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 107 */           .addText("\r\nHovering your mouse over a creature can tell you if a creature is hostile to you or not. If it has a red outline, then getting too close to that creature will cause it to target you and start attacking.\r\n\r\nA blue outline means that creature is neutral to you, and will generally ignore or run from you as you get close.\r\n\r\nA green outline is reserved for friendly creatures, such as players on your friends list, any pets you have tamed, or pets of your friends.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 113 */           .addText(""), null, 
/*     */           
/* 115 */           BMLBuilder.createLeftAlignedNode(
/* 116 */             BMLBuilder.createHorizArrayNode(false)
/* 117 */             .addButton("back", "Back", 80, 20, true)
/* 118 */             .addText("", null, null, null, 35, 0)
/* 119 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 122 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class TargetSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public TargetSubStage(long playerId) {
/* 132 */       super(CombatStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 138 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 139 */           BMLBuilder.createCenteredNode(
/* 140 */             BMLBuilder.createVertArrayNode(false)
/* 141 */             .addText("")
/* 142 */             .addHeader("Combat", Color.LIGHT_GRAY)), null, 
/*     */           
/* 144 */           BMLBuilder.createVertArrayNode(false)
/* 145 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 146 */           .addText("\r\nToggling the Autofight option on the quickbar so it is turned off will enable manual fighting mode.\r\n\r\nDouble clicking on a creature or right clicking and choosing the Target action will cause you to target that creature and if you are close enough to it, you will start trying to swing your weapon at it.\r\n\r\nYou can only have one target at a time, and that will be shown in your target window. Just like the select bar you can interact with the target via right clicking the target window, and also will be able to see its current health.\r\n\r\nYour fighting style, distance to the target, focus level and fighting stances can all be seen and controlled from the fight window which will expand when you're close enough to a target to start attacking.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 154 */           .addText(""), null, 
/*     */           
/* 156 */           BMLBuilder.createLeftAlignedNode(
/* 157 */             BMLBuilder.createHorizArrayNode(false)
/* 158 */             .addButton("back", "Back", 80, 20, true)
/* 159 */             .addText("", null, null, null, 35, 0)
/* 160 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 163 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 171 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 172 */         p.getCommunicator().sendOpenWindow((short)4, true);
/* 173 */         p.getCommunicator().sendOpenWindow((short)11, true);
/* 174 */         p.getCommunicator().sendToggleQuickbarBtn((short)2005, true);
/*     */       }
/* 176 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class SimpleSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public SimpleSubStage(long playerId) {
/* 189 */       super(CombatStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 195 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 196 */           BMLBuilder.createCenteredNode(
/* 197 */             BMLBuilder.createVertArrayNode(false)
/* 198 */             .addText("")
/* 199 */             .addHeader("Combat", Color.LIGHT_GRAY)), null, 
/*     */           
/* 201 */           BMLBuilder.createVertArrayNode(false)
/* 202 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 203 */           .addText("\r\nA lot of factors go into determining the winner of a fight including: skill levels of both sides, armour quality and type, weapon quality and type, fighting styles and stances, focus level, distance to the target, enchantments and many more.\r\n\r\nTo keep things simple to start, make sure your armour and weapon are as high quality as you can get them and always be prepared to run if the fight starts turning against you.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 208 */           .addText(""), null, 
/*     */           
/* 210 */           BMLBuilder.createLeftAlignedNode(
/* 211 */             BMLBuilder.createHorizArrayNode(false)
/* 212 */             .addButton("back", "Back", 80, 20, true)
/* 213 */             .addText("", null, null, null, 35, 0)
/* 214 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 217 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\CombatStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */