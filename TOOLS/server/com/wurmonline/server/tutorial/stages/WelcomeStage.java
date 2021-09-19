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
/*     */ public class WelcomeStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 100;
/*     */   
/*     */   public short getWindowId() {
/*  20 */     return (short)(100 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public WelcomeStage(long playerId) {
/*  25 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  31 */     return new ViewStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  43 */     this.subStages.add(new WelcomeSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class WelcomeSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public WelcomeSubStage(long playerId) {
/*  51 */       super(WelcomeStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  57 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  58 */           BMLBuilder.createCenteredNode(
/*  59 */             BMLBuilder.createVertArrayNode(false)
/*  60 */             .addText("")
/*  61 */             .addHeader("Welcome to Wurm!", Color.LIGHT_GRAY)), null, 
/*     */           
/*  63 */           BMLBuilder.createVertArrayNode(false)
/*  64 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  65 */           .addText("\r\nThis tutorial will show you how to get started in Wurm.\r\n\r\nClick [Next] below to get started.", null, null, null, 300, 400)
/*     */           
/*  67 */           .addText(""), null, 
/*     */           
/*  69 */           BMLBuilder.createLeftAlignedNode(
/*  70 */             BMLBuilder.createHorizArrayNode(false)
/*  71 */             .addButton("back", "Back", 80, 20, false)
/*  72 */             .addText("", null, null, null, 35, 0)
/*  73 */             .addButton("next", "Next", 80, 20, true)
/*  74 */             .maybeAddSkipButton(Players.getInstance().getPlayerOrNull(getPlayerId()), true)));
/*     */ 
/*     */ 
/*     */       
/*  78 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/*  86 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/*     */         
/*  88 */         p.getCommunicator().sendCloseWindow((short)9);
/*  89 */         p.getCommunicator().sendCloseWindow((short)5);
/*  90 */         p.getCommunicator().sendCloseWindow((short)1);
/*  91 */         p.getCommunicator().sendCloseWindow((short)3);
/*  92 */         p.getCommunicator().sendCloseWindow((short)16);
/*  93 */         p.getCommunicator().sendCloseWindow((short)20);
/*  94 */         p.getCommunicator().sendCloseWindow((short)26);
/*  95 */         p.getCommunicator().sendCloseWindow((short)11);
/*  96 */         p.getCommunicator().sendCloseWindow((short)4);
/*  97 */         p.getCommunicator().sendCloseWindow((short)41);
/*     */         
/*  99 */         p.getCommunicator().sendCloseWindow((short)6);
/* 100 */         p.getCommunicator().sendCloseWindow((short)7);
/* 101 */         p.getCommunicator().sendCloseWindow((short)2);
/* 102 */         p.getCommunicator().sendCloseWindow((short)12);
/* 103 */         p.getCommunicator().sendCloseWindow((short)13);
/*     */         
/* 105 */         p.getCommunicator().sendToggleAllQuickbarBtns(false);
/*     */       }
/* 107 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\WelcomeStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */