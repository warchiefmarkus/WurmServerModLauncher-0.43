/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.players.AchievementTemplate;
/*    */ import com.wurmonline.server.players.Achievements;
/*    */ import java.util.HashSet;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PersonalGoalsListQuestion
/*    */   extends Question
/*    */ {
/*    */   public PersonalGoalsListQuestion(Creature aResponder, long aTarget) {
/* 15 */     super(aResponder, "Personal Goals", "Personal Goals", 152, aTarget);
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
/* 27 */     StringBuilder buf = new StringBuilder();
/* 28 */     buf.append(getBmlHeader());
/* 29 */     if (getResponder().getPower() >= 4) {
/*    */       
/* 31 */       Achievements achs = Achievements.getAchievementObject(getTarget());
/* 32 */       HashSet<AchievementTemplate> goals = (HashSet<AchievementTemplate>)Achievements.getPersonalGoals(getTarget(), false);
/* 33 */       HashSet<AchievementTemplate> oldGoals = (HashSet<AchievementTemplate>)Achievements.getOldPersonalGoals(getTarget());
/*    */       
/* 35 */       buf.append("text{text='Current Personal Goals for WurmId " + getTarget() + "'}");
/* 36 */       buf.append("text{text=''}");
/* 37 */       buf.append("table{rows='" + goals.size() + "';cols='2';");
/* 38 */       for (AchievementTemplate t : goals) {
/*    */         
/* 40 */         boolean done = false;
/* 41 */         if (achs.getAchievement(t.getNumber()) != null) {
/* 42 */           done = true;
/*    */         }
/* 44 */         buf.append("label{color=\"" + (done ? "20,255,20" : "200,200,200") + "\";text=\"" + t.getName() + "\"};");
/* 45 */         buf.append("label{color=\"" + (done ? "20,255,20" : "200,200,200") + "\";text=\"" + t.getRequirement() + "\"}");
/*    */       } 
/* 47 */       buf.append("}");
/* 48 */       buf.append("text{text=''}");
/*    */       
/* 50 */       buf.append("text{text='Pre June 5 2018 Personal Goals for WurmId " + getTarget() + "'}");
/* 51 */       buf.append("text{text=''}");
/* 52 */       buf.append("table{rows='" + oldGoals.size() + "';cols='2';");
/* 53 */       for (AchievementTemplate t : oldGoals) {
/*    */         
/* 55 */         buf.append("label{text=\"" + t.getName() + "\"};");
/* 56 */         buf.append("label{text=\"" + t.getRequirement() + "\"}");
/*    */       } 
/* 58 */       buf.append("}");
/*    */       
/* 60 */       buf.append("}};null;null;}");
/* 61 */       getResponder().getCommunicator().sendBml(300, 600, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PersonalGoalsListQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */