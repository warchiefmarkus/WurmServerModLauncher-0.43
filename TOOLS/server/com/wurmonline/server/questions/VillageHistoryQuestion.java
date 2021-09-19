/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.villages.Village;
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
/*    */ public final class VillageHistoryQuestion
/*    */   extends Question
/*    */ {
/*    */   public VillageHistoryQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/* 30 */     super(aResponder, aTitle, aQuestion, 40, aTarget);
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
/* 42 */     Village citizenVillage = getResponder().getCitizenVillage();
/* 43 */     StringBuilder sb = new StringBuilder();
/* 44 */     sb.append(getBmlHeader());
/* 45 */     if (citizenVillage != null) {
/*    */       
/* 47 */       sb.append("header{text=\"Latest events in " + citizenVillage.getName() + ":\"}");
/* 48 */       String[] list = citizenVillage.getHistoryAsStrings(50);
/* 49 */       for (int x = 0; x < list.length; x++) {
/* 50 */         sb.append("text{text=\"" + list[x] + "\"}");
/*    */       }
/*    */     } else {
/* 53 */       sb.append("text{text='You are not citizen of a village.'}");
/* 54 */     }  sb.append(createAnswerButton2());
/* 55 */     getResponder().getCommunicator().sendBml(500, 300, true, true, sb.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageHistoryQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */