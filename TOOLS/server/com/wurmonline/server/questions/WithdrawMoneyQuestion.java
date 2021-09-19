/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WithdrawMoneyQuestion
/*     */   extends Question
/*     */ {
/*     */   public WithdrawMoneyQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  43 */     super(aResponder, aTitle, aQuestion, 36, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  54 */     setAnswer(answers);
/*  55 */     QuestionParser.parseWithdrawMoneyQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  61 */     StringBuilder buf = new StringBuilder();
/*  62 */     buf.append(getBmlHeader());
/*  63 */     fillDialogText(buf);
/*  64 */     buf.append(createAnswerButton2());
/*  65 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private void fillDialogText(StringBuilder buf) {
/*  70 */     long money = getResponder().getMoney();
/*  71 */     if (!Server.getInstance().isPS() && (Servers.localServer.entryServer || 
/*  72 */       getResponder().getPower() > 0) && !Servers.localServer.testServer) {
/*     */ 
/*     */       
/*  75 */       buf.append("text{text='You are not allowed to withdraw money on this server since it will be lost when you use a portal.'}");
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     if (money <= 0L) {
/*     */       
/*  81 */       buf.append("text{text='You have no money in the bank.'}");
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     Change change = Economy.getEconomy().getChangeFor(money);
/*  86 */     buf.append("text{text='You may withdraw up to " + change.getChangeString() + ".'}");
/*  87 */     buf.append("text{text='The money will end up in your inventory.'}");
/*  88 */     long gold = change.getGoldCoins();
/*  89 */     long silver = change.getSilverCoins();
/*  90 */     long copper = change.getCopperCoins();
/*  91 */     long iron = change.getIronCoins();
/*     */     
/*  93 */     if (money >= 1000000L)
/*  94 */       buf.append("harray{input{text='0'; id='gold'; maxchars='10'}label{text='(" + gold + ") Gold coins'}}"); 
/*  95 */     if (money >= 10000L) {
/*  96 */       buf.append("harray{input{text='0'; id='silver'; maxchars='10'}label{text='(" + silver + ") Silver coins'}}");
/*     */     }
/*  98 */     if (money >= 100L) {
/*  99 */       buf.append("harray{input{text='0'; id='copper'; maxchars='10'}label{text='(" + copper + ") Copper coins'}}");
/*     */     }
/* 101 */     if (money >= 1L)
/* 102 */       buf.append("harray{input{text='0'; id='iron'; maxchars='10'}label{text='(" + iron + ") Iron coins'}}"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WithdrawMoneyQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */