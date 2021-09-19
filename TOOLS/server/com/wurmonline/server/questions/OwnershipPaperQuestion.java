/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OwnershipPaperQuestion
/*    */   extends Question
/*    */ {
/*    */   public OwnershipPaperQuestion(Creature aResponder, Item aTarget) {
/* 38 */     super(aResponder, "Ownership Papers", "", 123, aTarget.getWurmId());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties aAnswers) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 60 */     StringBuilder buf = new StringBuilder();
/* 61 */     buf.append(getBmlHeaderWithScroll());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 67 */     buf.append("label{type=\"bold\";text=\"I herby declare that YYY is the current owner of XXX.\"}");
/* 68 */     buf.append("text{type=\"bolditalic\";text=\"lock info?\"}");
/*    */     
/* 70 */     buf.append("label{text=\"These papers can be traded to another player to transfer the ownership of XXX\"}");
/* 71 */     buf.append("text{text=\"\"}");
/* 72 */     buf.append("text{type=\"bold\";text=\"The King\"}");
/*    */     
/* 74 */     buf.append(createAnswerButton2("Close"));
/* 75 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\OwnershipPaperQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */