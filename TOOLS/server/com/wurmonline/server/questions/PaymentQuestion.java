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
/*     */ public final class PaymentQuestion
/*     */   extends Question
/*     */ {
/*  34 */   private final List<Long> playerIds = new LinkedList<>();
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
/*     */   public PaymentQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  46 */     super(aResponder, aTitle, aQuestion, 20, aTarget);
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
/*     */   public void sendQuestion() {
/*  82 */     StringBuilder buf = new StringBuilder();
/*  83 */     buf.append(getBmlHeader());
/*  84 */     buf.append("harray{label{text='Days'};input{id='days'; text='0'; maxchars='3'}}");
/*  85 */     buf.append("harray{label{text='Months'};input{id='months'; text='0'; maxchars='3'}}");
/*  86 */     buf.append("checkbox{id='extend';text='Mark this if you want to EXTEND a current payment instead of just setting from NOW'}");
/*  87 */     buf.append("harray{label{text='Player'};dropdown{id='wurmid';options='");
/*  88 */     Player[] players = Players.getInstance().getPlayers();
/*     */ 
/*     */     
/*  91 */     Arrays.sort((Object[])players);
/*  92 */     this.playerIds.add(new Long(-10L));
/*  93 */     buf.append("none");
/*  94 */     for (int x = 0; x < players.length; x++) {
/*     */       
/*  96 */       buf.append(",");
/*  97 */       buf.append(players[x].getName());
/*  98 */       this.playerIds.add(new Long(players[x].getWurmId()));
/*     */     } 
/* 100 */     buf.append("'}}");
/* 101 */     buf.append(createAnswerButton2());
/* 102 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 108 */     setAnswer(answers);
/* 109 */     QuestionParser.parsePaymentQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Long getPlayerId(int aPlayerID) {
/* 119 */     return this.playerIds.get(aPlayerID);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PaymentQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */