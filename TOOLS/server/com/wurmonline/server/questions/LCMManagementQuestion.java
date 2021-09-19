/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LCMManagementQuestion
/*    */   extends Question
/*    */ {
/*    */   private short actionType;
/*    */   
/*    */   public LCMManagementQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, short actionType) {
/* 17 */     super(aResponder, aTitle, aQuestion, 128, aTarget);
/* 18 */     this.actionType = actionType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 25 */     setAnswer(answers);
/* 26 */     QuestionParser.parseLCMManagementQuestion(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 32 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 33 */     buf.append("text{text='Who do you want to " + getActionVerb() + "?'};");
/* 34 */     buf.append("label{text'Name:'};input{id='name';maxchars='40';text=\"\"};");
/* 35 */     buf.append(createAnswerButton2());
/* 36 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ 
/*    */   
/*    */   private String getActionVerb() {
/* 41 */     if (this.actionType == 698)
/* 42 */       return "add or remove their CA status from"; 
/* 43 */     if (this.actionType == 699)
/* 44 */       return "add or remove their CM status from"; 
/* 45 */     if (this.actionType == 700)
/* 46 */       return "see their info of"; 
/* 47 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public short getActionType() {
/* 52 */     return this.actionType;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\LCMManagementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */