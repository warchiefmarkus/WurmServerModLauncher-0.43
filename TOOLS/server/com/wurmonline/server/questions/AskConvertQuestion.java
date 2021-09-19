/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
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
/*     */ public final class AskConvertQuestion
/*     */   extends Question
/*     */ {
/*     */   private final Item holyItem;
/*     */   
/*     */   public AskConvertQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, Item aHolyItem) {
/*  50 */     super(aResponder, aTitle, aQuestion, 27, aTarget);
/*  51 */     this.holyItem = aHolyItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  60 */     setAnswer(answers);
/*  61 */     QuestionParser.parseAskConvertQuestion(this);
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
/*     */   public void sendQuestion() {
/*     */     try {
/*  74 */       Creature asker = Server.getInstance().getCreature(this.target);
/*  75 */       StringBuilder buf = new StringBuilder();
/*  76 */       buf.append(getBmlHeader());
/*  77 */       buf.append("text{text='" + asker.getName() + " wants to teach you about " + (asker.getDeity()).name + ".'}text{text=''}text{text=''}text{text='Do you want to listen?'}");
/*     */       
/*  79 */       buf.append("text{text='After you listen, you will get the option to join the followers of " + (asker.getDeity()).name + ".'}");
/*     */ 
/*     */       
/*  82 */       buf.append("text{text=''}");
/*     */       
/*  84 */       buf.append("radio{ group='conv'; id='true';text='Yes'}");
/*  85 */       buf.append("radio{ group='conv'; id='false';text='No';selected='true'}");
/*     */       
/*  87 */       buf.append(createAnswerButton2());
/*  88 */       getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*  89 */       asker.getCommunicator().sendNormalServerMessage("You ask " + getResponder().getName() + " to listen to you.");
/*     */     }
/*  91 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */     
/*  94 */     } catch (NoSuchPlayerException noSuchPlayerException) {}
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
/*     */   Item getHolyItem() {
/* 106 */     return this.holyItem;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AskConvertQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */