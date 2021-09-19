/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.players.Cultist;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.SkillSystem;
/*     */ import com.wurmonline.server.skills.SkillTemplate;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ public final class LearnSkillQuestion
/*     */   extends Question
/*     */ {
/*     */   public LearnSkillQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  53 */     super(aResponder, aTitle, aQuestion, 16, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  62 */     setAnswer(answers);
/*  63 */     QuestionParser.parseLearnSkillQuestion(this);
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
/*     */   public void sendQuestion() {
/*  98 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/* 100 */     buf.append("harray{label{text='Skill'}dropdown{id='data1';options='");
/* 101 */     Collection<SkillTemplate> temps = SkillSystem.templates.values();
/* 102 */     SkillTemplate[] templates = temps.<SkillTemplate>toArray(new SkillTemplate[temps.size()]);
/*     */     
/* 104 */     Arrays.sort((Object[])templates);
/*     */     
/* 106 */     Creature receiver = null;
/* 107 */     boolean hadError = false;
/*     */     
/*     */     try {
/* 110 */       if (WurmId.getType(this.target) == 1 || 
/* 111 */         WurmId.getType(this.target) == 0) {
/* 112 */         receiver = Server.getInstance().getCreature(this.target);
/*     */       } else {
/* 114 */         receiver = getResponder();
/*     */       } 
/* 116 */       Skills skills = receiver.getSkills();
/* 117 */       for (int x = 0; x < templates.length; x++) {
/*     */         
/* 119 */         if (x > 0) {
/* 120 */           buf.append(",");
/*     */         }
/* 122 */         int sk = templates[x].getNumber();
/*     */         
/*     */         try {
/* 125 */           Skill skill = skills.getSkill(sk);
/* 126 */           String affs = "*****".substring(0, skill.affinity);
/* 127 */           buf.append(templates[x].getName() + " " + affs + " (" + skill.getKnowledge() + ")");
/*     */         }
/* 129 */         catch (NoSuchSkillException e) {
/*     */ 
/*     */           
/* 132 */           buf.append(templates[x].getName());
/*     */         }
/*     */       
/*     */       } 
/* 136 */     } catch (NoSuchPlayerException e) {
/*     */       
/* 138 */       hadError = true;
/*     */ 
/*     */     
/*     */     }
/* 142 */     catch (NoSuchCreatureException e) {
/*     */       
/* 144 */       hadError = true;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     if (hadError)
/*     */     {
/* 150 */       for (int x = 0; x < templates.length; x++) {
/*     */         
/* 152 */         if (x > 0)
/* 153 */           buf.append(","); 
/* 154 */         buf.append(templates[x].getName());
/*     */       } 
/*     */     }
/* 157 */     buf.append("'}}");
/*     */     
/* 159 */     buf.append("label{type=\"bolditalic\";text=\"Skill of 0 = no change\"}");
/*     */     
/* 161 */     buf.append("harray{label{text=\"Skill level\"}input{maxchars=\"3\"; id=\"val\"; text=\"0\"}label{text=\".\"}input{maxchars=\"6\"; id=\"dec\"; text=\"000000\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     buf.append("harray{label{text=\"Affinities\"}radio{group=\"aff\";id=\"-1\";text=\"Leave as is\";selected=\"true\"};radio{group=\"aff\";id=\"0\";text=\"None\"};radio{group=\"aff\";id=\"1\";text=\"One\"};radio{group=\"aff\";id=\"2\";text=\"Two\"};radio{group=\"aff\";id=\"3\";text=\"Three\"};radio{group=\"aff\";id=\"4\";text=\"Four\"};radio{group=\"aff\";id=\"5\";text=\"Five\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     float align = getResponder().getAlignment();
/* 176 */     buf.append("text{text=\"\"}");
/* 177 */     buf.append("label{type=\"bolditalic\";text=\"Alignment, leave blank for no change\"}");
/* 178 */     buf.append("harray{label{text=\"Alignment (" + align + ")\"}input{maxchars=\"4\"; id=\"align\"; text=\"\"}}");
/*     */ 
/*     */     
/* 181 */     int karma = getResponder().getKarma();
/* 182 */     buf.append("label{type=\"bolditalic\";text=\"Karma, leave blank for no change\"}");
/* 183 */     buf.append("harray{label{text=\"Karma (" + karma + ")\"}input{maxchars=\"5\"; id=\"karma\"; text=\"\"}}");
/*     */     
/* 185 */     int height = 270;
/*     */     
/* 187 */     if (WurmId.getType(this.target) == 0 && Servers.isThisATestServer() && (
/* 188 */       getResponder().getPower() == 5 || getResponder().getName().equals("Hestia"))) {
/*     */       
/* 190 */       height += 70;
/* 191 */       buf.append("label{text=\"----- Cultist --- Test Server Only -----\"}");
/* 192 */       Cultist cultist = Cultist.getCultist(this.target);
/* 193 */       byte path = 0;
/* 194 */       byte level = 0;
/* 195 */       if (cultist != null) {
/*     */         
/* 197 */         path = cultist.getPath();
/* 198 */         level = cultist.getLevel();
/*     */       } 
/* 200 */       String pathName = getShortPathName(path);
/* 201 */       buf.append("harray{label{text=\"Path (" + pathName + ")\"}dropdown{id=\"path\";options=\"none,Love,Hate,Knowledge,Insanity,Power\";default=\"" + path + "\"}}");
/*     */       
/* 203 */       buf.append("harray{label{text=\"Level (" + level + ") leave blank for no change\"}input{maxchars=\"2\"; id=\"level\"; text=\"\"}}");
/*     */     } 
/*     */ 
/*     */     
/* 207 */     buf.append(createAnswerButton2());
/*     */     
/* 209 */     getResponder().getCommunicator().sendBml(360, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getShortPathName(byte path) {
/* 214 */     switch (path) {
/*     */       
/*     */       case 2:
/* 217 */         return "Hate";
/*     */       case 1:
/* 219 */         return "Love";
/*     */       case 4:
/* 221 */         return "Insanity";
/*     */       case 3:
/* 223 */         return "Knowledge";
/*     */       case 5:
/* 225 */         return "Power";
/*     */     } 
/* 227 */     return "none";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\LearnSkillQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */