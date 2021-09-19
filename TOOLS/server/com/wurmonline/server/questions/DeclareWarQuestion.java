/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DeclareWarQuestion
/*     */   extends Question
/*     */ {
/*     */   private final Village targetVillage;
/*     */   
/*     */   public DeclareWarQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) throws NoSuchCreatureException, NoSuchPlayerException, NoSuchVillageException {
/*  52 */     super(aResponder, aTitle, aQuestion, 29, aTarget);
/*     */     
/*  54 */     this.targetVillage = Villages.getVillage((int)aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  63 */     setAnswer(answers);
/*  64 */     QuestionParser.parseVillageWarQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Village getTargetVillage() {
/*  73 */     return this.targetVillage;
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
/*     */   public void sendQuestion() {
/* 104 */     Village village = getResponder().getCitizenVillage();
/* 105 */     StringBuilder buf = new StringBuilder();
/* 106 */     buf.append(getBmlHeader());
/* 107 */     buf.append("header{text=\"Declaring war on " + this.targetVillage.getName() + ":\"}");
/* 108 */     buf.append("text{text=\"Do you really want to declare war on " + this.targetVillage.getName() + "?\"}");
/* 109 */     buf.append("text{text=\"" + this.targetVillage.getName() + " will have 24 hours to accept the challenge.\"}");
/* 110 */     buf.append("text{text=\"If they haven't answered in that time the war will begin.\"}");
/* 111 */     if (village.isAlly(this.targetVillage)) {
/* 112 */       buf.append("text{text=\"Your alliance with " + this.targetVillage.getName() + " will be broken.\"}");
/*     */     }
/* 114 */     buf.append("radio{ group='declare'; id='true';text='Yes'}");
/* 115 */     buf.append("radio{ group='declare'; id='false';text='No';selected='true'}");
/* 116 */     buf.append(createAnswerButton2());
/* 117 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\DeclareWarQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */