/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.villages.RecruitmentAd;
/*    */ import com.wurmonline.server.villages.RecruitmentAds;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GmVillageAdInterface
/*    */   extends Question
/*    */ {
/*    */   public GmVillageAdInterface(Creature aResponder, long aTarget) {
/* 42 */     super(aResponder, "Manage Village Recruitment Ads", "", 101, aTarget);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 62 */     setAnswer(answers);
/* 63 */     QuestionParser.parseGmVillageAdQuestion(this);
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
/*    */   public void sendQuestion() {
/* 75 */     StringBuilder buf = new StringBuilder();
/* 76 */     buf.append(getBmlHeader());
/* 77 */     RecruitmentAd[] ads = RecruitmentAds.getAllRecruitmentAds();
/*    */     
/* 79 */     buf.append("table{rows=\"" + ads.length + '\001' + "\";cols=\"3\";label{text=\"Remove\"};label{text=\"Village\"};label{text=\"Contact\"};");
/*    */ 
/*    */ 
/*    */     
/* 83 */     for (int i = 0; i < ads.length; i++) {
/*    */       
/* 85 */       buf.append("checkbox{id=\"" + ads[i].getVillageId() + "remove\";selected=\"false\";text=\" \"}");
/* 86 */       buf.append("label{text=\"" + ads[i].getVillageName() + "\"};");
/* 87 */       buf.append("label{text=\"" + ads[i].getContactName() + "\"};");
/*    */     } 
/* 89 */     buf.append("}");
/* 90 */     buf.append(createAnswerButton2("Remove"));
/* 91 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GmVillageAdInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */