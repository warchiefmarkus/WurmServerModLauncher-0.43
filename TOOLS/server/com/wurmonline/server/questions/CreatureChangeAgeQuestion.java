/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
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
/*     */ 
/*     */ 
/*     */ public final class CreatureChangeAgeQuestion
/*     */   extends Question
/*     */ {
/*     */   public CreatureChangeAgeQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  44 */     super(aResponder, aTitle, aQuestion, 153, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  52 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*  53 */     int width = 150;
/*  54 */     int height = 150;
/*     */     
/*     */     try {
/*  57 */       Creature target = Creatures.getInstance().getCreature(this.target);
/*  58 */       int age = (target.getStatus()).age;
/*  59 */       buf.append("harray{input{id='newAge'; maxchars='3'; text='").append(age).append("'}label{text='Age'}}");
/*     */     }
/*  61 */     catch (NoSuchCreatureException ex) {
/*     */       
/*  63 */       ex.printStackTrace();
/*     */     } 
/*     */     
/*  66 */     buf.append(createAnswerButton2());
/*  67 */     getResponder().getCommunicator().sendBml(width, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  75 */     setAnswer(answers);
/*  76 */     init(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(CreatureChangeAgeQuestion question) {
/*  84 */     Creature responder = question.getResponder();
/*  85 */     int newAge = 0;
/*  86 */     long target = question.getTarget();
/*     */     
/*     */     try {
/*  89 */       Creature creature = Creatures.getInstance().getCreature(target);
/*  90 */       String age = question.getAnswer().getProperty("newAge");
/*  91 */       newAge = Integer.parseInt(age);
/*  92 */       ((DbCreatureStatus)creature.getStatus()).updateAge(newAge);
/*     */ 
/*     */       
/*  95 */       (creature.getStatus()).lastPolledAge = 0L;
/*  96 */       creature.pollAge();
/*     */       
/*  98 */       creature.refreshVisible();
/*     */     }
/* 100 */     catch (NoSuchCreatureException|java.io.IOException ex) {
/*     */       
/* 102 */       ex.printStackTrace();
/*     */     } 
/* 104 */     responder.getCommunicator().sendNormalServerMessage("Age = " + newAge + ".");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\CreatureChangeAgeQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */