/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PowerManagementQuestion
/*     */   extends Question
/*     */ {
/*  34 */   private final List<Long> playerIds = new LinkedList<>();
/*     */ 
/*     */   
/*     */   public PowerManagementQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  38 */     super(aResponder, aTitle, aQuestion, 20, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  79 */     StringBuilder buf = new StringBuilder();
/*  80 */     buf.append(getBmlHeader());
/*  81 */     buf.append("harray{label{text='Player'};dropdown{id='wurmid';options='");
/*  82 */     Player[] players = Players.getInstance().getPlayers();
/*     */ 
/*     */     
/*  85 */     Arrays.sort((Object[])players);
/*  86 */     this.playerIds.add(new Long(-10L));
/*  87 */     buf.append("none");
/*  88 */     for (int x = 0; x < players.length; x++) {
/*     */       
/*  90 */       buf.append(",");
/*  91 */       buf.append(players[x].getName());
/*  92 */       this.playerIds.add(new Long(players[x].getWurmId()));
/*     */     } 
/*  94 */     buf.append("'}}");
/*     */     
/*  96 */     buf.append("harray{label{text='Power'};dropdown{id='power';options='");
/*  97 */     buf.append("none,");
/*  98 */     buf.append("hero,");
/*  99 */     buf.append("demigod,");
/* 100 */     buf.append("high god,");
/* 101 */     buf.append("arch angel,");
/* 102 */     buf.append("implementor");
/* 103 */     buf.append("'}}");
/* 104 */     buf.append(createAnswerButton2());
/*     */     
/* 106 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 112 */     setAnswer(answers);
/* 113 */     QuestionParser.parsePowerManagementQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Long getPlayerId(int aPlayerID) {
/* 123 */     return this.playerIds.get(aPlayerID);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PowerManagementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */