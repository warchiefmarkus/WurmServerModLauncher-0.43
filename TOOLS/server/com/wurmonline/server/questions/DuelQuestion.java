/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
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
/*     */ public final class DuelQuestion
/*     */   extends Question
/*     */ {
/*     */   public DuelQuestion(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget) {
/*  31 */     super(aResponder, aTitle, aQuestion, aType, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*     */     try {
/*  39 */       Player tplay = Players.getInstance().getPlayer(this.target);
/*     */       
/*  41 */       String key = "duel";
/*  42 */       String val = answers.getProperty("duel");
/*  43 */       if (val != null && val.equals("true")) {
/*     */         
/*  45 */         if (this.type == 59) {
/*     */           
/*  47 */           ((Player)getResponder()).addDuellist((Creature)tplay);
/*  48 */           tplay.addDuellist(getResponder());
/*     */         }
/*  50 */         else if (this.type == 60) {
/*     */           
/*  52 */           ((Player)getResponder()).addSparrer((Creature)tplay);
/*  53 */           tplay.addSparrer(getResponder());
/*     */         } 
/*  55 */         getResponder().getCommunicator().sendNormalServerMessage("You may now attack " + tplay.getName() + " without penalty.");
/*  56 */         tplay.getCommunicator().sendNormalServerMessage("You may now attack " + getResponder().getName() + " without penalty.");
/*     */       } else {
/*     */         
/*  59 */         getResponder().getCommunicator().sendNormalServerMessage("You decide not to fight " + tplay.getName() + " now.");
/*     */       } 
/*  61 */     } catch (NoSuchPlayerException nsp) {
/*     */       
/*  63 */       getResponder().getCommunicator().sendNormalServerMessage("Your opponent seem to have left.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  70 */     StringBuilder buf = new StringBuilder();
/*  71 */     buf.append(getBmlHeader());
/*  72 */     String name = "An unknown player";
/*     */     
/*     */     try {
/*  75 */       Player tplay = Players.getInstance().getPlayer(this.target);
/*  76 */       name = tplay.getName();
/*     */     }
/*  78 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */     
/*  81 */     if (this.type == 59) {
/*     */       
/*  83 */       buf.append("text{text='" + name + " wants to duel to the death with you.'}text{text=''}");
/*  84 */       buf.append("text{text='You will receive no penalties for attacking or killing eachother.'}");
/*  85 */       buf.append("text{text='Make sure you are in a safe environment if you do not trust your opponent in order to avoid some kind of trap.'}");
/*  86 */       buf.append("text{text='The winner may loot the loser!'}");
/*     */     }
/*  88 */     else if (this.type == 60) {
/*     */       
/*  90 */       buf.append("text{text='" + name + " wants to duel friendly with you.'}text{text=''}");
/*  91 */       buf.append("text{text='You will receive no penalties for attacking eachother.'}");
/*  92 */       buf.append("text{text='Before the final blow, the duel will normally end and a winner be declared.'}");
/*  93 */       buf.append("text{text='On rare occasions the winning player unfortunately fails to hold his final blow back, and the opponent is slain!'}");
/*  94 */       buf.append("text{text='Make sure you are in a safe environment if you do not trust your opponent in order to avoid some kind of trap.'}");
/*  95 */       buf.append("text{text='The winner may not loot the loser'}");
/*     */     } 
/*  97 */     buf.append("radio{ group='duel'; id='true';text='Yes'}");
/*  98 */     buf.append("radio{ group='duel'; id='false';text='No';selected='true'}");
/*  99 */     buf.append(createAnswerButton2());
/*     */     
/* 101 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\DuelQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */