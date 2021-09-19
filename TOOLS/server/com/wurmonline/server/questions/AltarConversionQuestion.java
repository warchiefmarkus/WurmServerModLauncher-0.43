/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deity;
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
/*     */ public final class AltarConversionQuestion
/*     */   extends Question
/*     */ {
/*     */   private final Deity deity;
/*     */   
/*     */   public AltarConversionQuestion(Creature aResponder, String aTitle, String aQuestion, long aAltar, Deity aDeity) {
/*  40 */     super(aResponder, aTitle, aQuestion, 31, aAltar);
/*  41 */     this.deity = aDeity;
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
/*  52 */     setAnswer(answers);
/*  53 */     QuestionParser.parseAltarConvertQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  64 */     StringBuilder buf = new StringBuilder();
/*  65 */     buf.append(getBmlHeader());
/*  66 */     buf.append("text{text='The inscription talks about " + this.deity.name + ".'}");
/*  67 */     buf.append("text{text=''}");
/*  68 */     for (int x = 0; x < this.deity.altarConvertText1.length; x++) {
/*     */       
/*  70 */       buf.append("text{text='" + this.deity.altarConvertText1[x] + "'}");
/*  71 */       buf.append("text{text=''}");
/*     */     } 
/*  73 */     if (getResponder().isChampion() && getResponder().getDeity() != null) {
/*     */       
/*  75 */       buf.append("text{text='You are already the devoted follower of " + (getResponder().getDeity()).name + ". " + this.deity.name + " would never accept you.'}");
/*     */       
/*  77 */       buf.append("text{text=''}");
/*     */     }
/*  79 */     else if (!QuestionParser.doesKingdomTemplateAcceptDeity(getResponder().getKingdomTemplateId(), this.deity)) {
/*     */       
/*  81 */       buf.append("text{text='" + getResponder().getKingdomName() + " would never accept a follower of " + this.deity.name + ".'}");
/*  82 */       buf.append("text{text=''}");
/*     */     }
/*  84 */     else if (getResponder().getDeity() == null || getResponder().getDeity() != this.deity) {
/*     */       
/*  86 */       buf.append("text{type='italic';text='Do you want to become a follower of " + this.deity.name + "?'}");
/*  87 */       buf.append("text{text=''}");
/*  88 */       if (getResponder().getDeity() != null)
/*  89 */         buf.append("text{type='bold';text='If you answer yes, your faith and all your abilities granted by " + 
/*  90 */             (getResponder().getDeity()).name + " will be lost!'}"); 
/*  91 */       if (!Servers.localServer.PVPSERVER) {
/*  92 */         buf.append("text{type='bold';text='Warning: Converting to a deity on Freedom then travelling to a Chaos kingdom that does notalign with your deity you will lose all faith and abilities granted, and you will stop following that deity. Libila does not align with WL kingdoms and Fo/Vynora/Magranon do not align with BL kingdoms.'}");
/*     */       }
/*     */       
/*  95 */       buf.append("text{text=''}");
/*     */       
/*  97 */       buf.append("radio{ group='conv'; id='true';text='Accept'}");
/*  98 */       buf.append("radio{ group='conv'; id='false';text='Decline';selected='true'}");
/*     */     }
/*     */     else {
/*     */       
/* 102 */       buf.append("text{text='You are already a follower of " + (getResponder().getDeity()).name + ".'}");
/* 103 */       buf.append("text{text=''}");
/*     */     } 
/* 105 */     buf.append(createAnswerButton2());
/* 106 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Deity getDeity() {
/* 116 */     return this.deity;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AltarConversionQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */