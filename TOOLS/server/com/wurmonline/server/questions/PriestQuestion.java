/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
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
/*     */ public final class PriestQuestion
/*     */   extends Question
/*     */ {
/*     */   public PriestQuestion(Creature aResponder, String aTitle, String aQuestion, long aAltar) {
/*  40 */     super(aResponder, aTitle, aQuestion, 44, aAltar);
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
/*  51 */     setAnswer(answers);
/*  52 */     QuestionParser.parsePriestQuestion(this);
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
/*  63 */     StringBuilder buf = new StringBuilder();
/*  64 */     buf.append(getBmlHeader());
/*     */     
/*  66 */     buf.append("text{text='You may choose to become a priest of " + (getResponder().getDeity()).name + ".'}text{text=''}");
/*     */     
/*  68 */     buf
/*  69 */       .append("text{text='If you answer yes, you will receive special powers from your deity, such as the ability to cast spells.'}text{text=''}");
/*  70 */     if (Servers.localServer.PVPSERVER)
/*  71 */       buf.append("text{text='You must also walk this path if you strive to become a real champion of " + 
/*  72 */           (getResponder().getDeity()).name + ".'}text{text=''}"); 
/*  73 */     buf.append("text{text='You will however be very limited in what you can do.'}");
/*  74 */     buf
/*  75 */       .append("text{text='You will for instance not be able to do such things as create, repair or improve items or use alchemy.'}");
/*  76 */     if ((getResponder().getDeity()).number == 4) {
/*  77 */       buf
/*  78 */         .append("text{text='You will also not be able to tame animals or farm to mention just a few other limitations.'}");
/*  79 */     } else if (Servers.localServer.PVPSERVER) {
/*  80 */       buf
/*  81 */         .append("text{text='You will also not be able to steal, pick locks or destroy structures to mention just a few other limitations.'}");
/*     */     } 
/*  83 */     if (Servers.localServer.EPIC)
/*  84 */       buf
/*  85 */         .append("text{text='You will also note that as you focus on your soul, you gain body and mind skills a lot slower.'}"); 
/*  86 */     if (Servers.localServer.PVPSERVER) {
/*     */       
/*  88 */       buf.append("text{text='If you later decide to become a champion of " + (getResponder().getDeity()).name + " these restrictions will be lifted.'}");
/*     */       
/*  90 */       buf
/*  91 */         .append("text{text='As a champion, you may only escape death a few times though. After that your life ends permanently.'}text{text=''}");
/*     */     } 
/*  93 */     buf.append("text{text='If your faith ever fails you, you will lose your priesthood.'}text{text=''}");
/*  94 */     buf.append("text{type='italic';text='Do you want to become a priest of " + (getResponder().getDeity()).name + " despite the severe limitations it will have on your actions?'}");
/*     */ 
/*     */     
/*  97 */     buf.append("radio{ group='priest'; id='true';text='Yes'}");
/*  98 */     buf.append("radio{ group='priest'; id='false';text='No';selected='true'}");
/*  99 */     buf.append(createAnswerButton2());
/*     */     
/* 101 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PriestQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */