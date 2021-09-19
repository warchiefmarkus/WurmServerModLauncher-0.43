/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
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
/*     */ public class ChangeStaffSkillQuestion
/*     */   extends Question
/*     */ {
/*     */   public ChangeStaffSkillQuestion(Creature aResponder) {
/*  43 */     super(aResponder, "Switch skills", "Do you want to switch your spear and staff skills?", 110, aResponder
/*  44 */         .getWurmId());
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
/*  55 */     String key = "rd";
/*  56 */     String val = answers.getProperty("rd");
/*  57 */     if (Boolean.parseBoolean(val)) {
/*     */       
/*  59 */       if (getResponder().hasFlag(11) && getResponder().getPower() <= 0) {
/*     */         
/*  61 */         getResponder().getCommunicator().sendNormalServerMessage("You have already switched those skills.");
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  67 */       Skill staff = null;
/*     */       
/*     */       try {
/*  70 */         staff = getResponder().getSkills().getSkill(10090);
/*     */       }
/*  72 */       catch (NoSuchSkillException nss) {
/*     */         
/*  74 */         staff = getResponder().getSkills().learn(10090, 1.0F);
/*     */       } 
/*  76 */       Skill spear = null;
/*     */       
/*     */       try {
/*  79 */         spear = getResponder().getSkills().getSkill(10088);
/*     */       }
/*  81 */       catch (NoSuchSkillException nsss) {
/*     */         
/*  83 */         spear = getResponder().getSkills().learn(10088, 1.0F);
/*     */       } 
/*  85 */       if (spear != null && staff != null) {
/*     */         
/*  87 */         getResponder().getSkills().switchSkillNumbers(spear, staff);
/*     */         
/*  89 */         getResponder().setFlag(11, true);
/*     */       } else {
/*     */         
/*  92 */         getResponder().getCommunicator().sendNormalServerMessage("You lack one of the skills.");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  98 */       getResponder().getCommunicator().sendNormalServerMessage("You decide not to switch those skills.");
/*     */     } 
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
/* 111 */     StringBuilder buf = new StringBuilder();
/* 112 */     buf.append(getBmlHeader());
/*     */     
/* 114 */     buf.append("text{text='Steel Spears had the stats Steel Staff should have had so you have the option to switch those skills once.'}");
/* 115 */     buf.append("text{text='Do you wish to switch your Staff skill with your Long Spear skill?'}");
/* 116 */     buf.append("radio{ group='rd'; id='true';text='Yes'}");
/* 117 */     buf.append("radio{ group='rd'; id='false';text='No';selected='true'}");
/*     */     
/* 119 */     buf.append(createAnswerButton2());
/* 120 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ChangeStaffSkillQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */