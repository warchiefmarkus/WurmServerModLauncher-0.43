/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.villages.Village;
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
/*     */ public final class PeaceQuestion
/*     */   extends Question
/*     */ {
/*     */   private final Creature invited;
/*     */   
/*     */   public PeaceQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) throws NoSuchCreatureException, NoSuchPlayerException {
/*  47 */     super(aResponder, aTitle, aQuestion, 30, aTarget);
/*  48 */     this.invited = Server.getInstance().getCreature(aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  57 */     setAnswer(answers);
/*  58 */     QuestionParser.parseVillagePeaceQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Creature getInvited() {
/*  67 */     return this.invited;
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
/*     */   public void sendQuestion() {
/*  91 */     Village village = getResponder().getCitizenVillage();
/*  92 */     StringBuilder buf = new StringBuilder();
/*  93 */     buf.append(getBmlHeader());
/*  94 */     buf.append("header{text=\"Peace offer by " + village.getName() + ":\"}");
/*  95 */     buf.append("text{text=\"You have been offered peace by " + getResponder().getName() + " and the village of " + village
/*  96 */         .getName() + ". \"}");
/*     */     
/*  98 */     buf.append("text{text='Do you accept?'}");
/*  99 */     buf.append("radio{ group='peace'; id='true';text='Yes'}");
/* 100 */     buf.append("radio{ group='peace'; id='false';text='No';selected='true'}");
/* 101 */     buf.append(createAnswerButton2());
/* 102 */     getInvited().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/* 103 */     getResponder().getCommunicator().sendNormalServerMessage("You send a peace offer to " + getInvited().getName() + ".");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PeaceQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */