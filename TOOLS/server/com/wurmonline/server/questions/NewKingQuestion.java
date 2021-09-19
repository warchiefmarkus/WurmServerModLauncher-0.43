/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.kingdom.King;
/*    */ import com.wurmonline.server.kingdom.Kingdoms;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NewKingQuestion
/*    */   extends Question
/*    */ {
/*    */   public NewKingQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/* 30 */     super(aResponder, aTitle, aQuestion, 69, aTarget);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 42 */     StringBuilder buf = new StringBuilder();
/* 43 */     buf.append(getBmlHeader());
/*    */     
/* 45 */     buf.append("text{type='bold';text='Welcome as the " + 
/* 46 */         King.getRulerTitle(getResponder().isNotFemale(), getResponder().getKingdomId()) + " of " + 
/* 47 */         Kingdoms.getNameFor(getResponder().getKingdomId()) + "!'}");
/* 48 */     buf.append("text{type='';text='There are some things you should be aware of:'}");
/* 49 */     buf.append("text{type='';text='As the " + 
/* 50 */         King.getRulerTitle(getResponder().isNotFemale(), getResponder().getKingdomId()) + ", you have one general goal, apart from your personal ones.'}");
/*    */     
/* 52 */     buf.append("text{type='';text='This is to gain land.'}");
/* 53 */     buf.append("text{type='';text='The more land you gain, the better your public title will become.'}");
/* 54 */     buf.append("text{type='';text='The more land you control, the more and finer titles and orders you may bestow upon your subjects.'}");
/* 55 */     buf.append("text{type='';text='Therefor a good idea is to reward those who gain land for you.'}");
/* 56 */     buf.append("text{type='';text='Land also has the benefit of yielding more coins to traders from the pool.'}");
/* 57 */     buf.append("text{text=''}");
/* 58 */     buf.append("text{type='';text='Good luck in your rulership!'}");
/* 59 */     buf.append("text{text=''}");
/* 60 */     buf.append(createAnswerButton2());
/* 61 */     getResponder().getCommunicator().sendBml(400, 250, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\NewKingQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */