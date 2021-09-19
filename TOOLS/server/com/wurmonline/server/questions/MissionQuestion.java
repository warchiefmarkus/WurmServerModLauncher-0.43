/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.tutorial.OldMission;
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
/*     */ public final class MissionQuestion
/*     */   extends Question
/*     */ {
/*     */   private int missionNumber;
/*     */   
/*     */   public MissionQuestion(int aMissionNum, Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  38 */     super(aResponder, aTitle, aQuestion, 61, aTarget);
/*  39 */     this.missionNumber = aMissionNum;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  45 */     Creature guide = null;
/*     */     
/*     */     try {
/*  48 */       guide = Server.getInstance().getCreature(this.target);
/*     */     }
/*  50 */     catch (NoSuchCreatureException nsc) {
/*     */       
/*  52 */       getResponder().getCommunicator().sendNormalServerMessage("Your guide has left!");
/*     */       
/*     */       return;
/*  55 */     } catch (NoSuchPlayerException nsp) {
/*     */       
/*  57 */       getResponder().getCommunicator().sendNormalServerMessage("Your guide has left!");
/*     */       return;
/*     */     } 
/*  60 */     OldMission cm = OldMission.getMission(this.missionNumber, getResponder().getKingdomId());
/*  61 */     boolean ok = false;
/*  62 */     boolean skip = false;
/*  63 */     if (cm.hasCheckBox()) {
/*     */       
/*  65 */       boolean done = answers.getProperty("check").equals("true");
/*  66 */       if (done) {
/*     */         
/*  68 */         skip = true;
/*  69 */         if (this.missionNumber == 9999) {
/*     */           
/*  71 */           this.missionNumber = getResponder().getTutorialLevel();
/*  72 */           if (this.missionNumber != 9999) {
/*  73 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to continue following the instructions from the " + guide
/*  74 */                 .getName() + ".");
/*     */           }
/*     */         } else {
/*     */           
/*  78 */           this.missionNumber++;
/*  79 */           getResponder().missionFinished(true, false);
/*     */         } 
/*  81 */         if (this.missionNumber != 9999) {
/*     */           
/*  83 */           OldMission m = OldMission.getMission(this.missionNumber, getResponder().getKingdomId());
/*  84 */           if (m != null)
/*     */           {
/*  86 */             MissionQuestion ms = new MissionQuestion(m.number, getResponder(), m.title, m.missionDescription, this.target);
/*     */             
/*  88 */             ms.sendQuestion();
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/*  93 */           ((Player)getResponder()).setTutorialLevel(9999);
/*     */           
/*  95 */           SimplePopup popup = new SimplePopup(getResponder(), "Tutorial done!", "That concludes the tutorial! The " + guide.getName() + " is most pleased. Congratulations and good luck!");
/*     */           
/*  97 */           popup.sendQuestion();
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 102 */         SimplePopup popup = new SimplePopup(getResponder(), "Wait for now.", "You decide to take a pause and maybe come back later.");
/*     */         
/* 104 */         popup.sendQuestion();
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 109 */     else if (answers.getProperty("mission") != null) {
/*     */       
/* 111 */       ok = answers.getProperty("mission").equals("do");
/* 112 */       if (ok) {
/*     */         
/* 114 */         OldMission m = OldMission.getMission(this.missionNumber, getResponder().getKingdomId());
/* 115 */         if (m != null) {
/*     */           
/* 117 */           SimplePopup popup = new SimplePopup(getResponder(), this.title, "You accept the mission.");
/* 118 */           popup.sendQuestion();
/*     */         }
/*     */         else {
/*     */           
/* 122 */           ((Player)getResponder()).setTutorialLevel(9999);
/*     */           
/* 124 */           SimplePopup popup = new SimplePopup(getResponder(), "Tutorial done!", "That concludes the tutorial! The " + guide.getName() + " is most pleased. Congratulations and good luck!");
/*     */           
/* 126 */           popup.sendQuestion();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     if (answers.getProperty("mission") != null) {
/*     */       
/* 133 */       ok = answers.getProperty("mission").equals("wait");
/* 134 */       if (!skip && ok) {
/*     */         
/* 136 */         SimplePopup popup = new SimplePopup(getResponder(), "Wait for now.", "You decide to take a pause and maybe come back later.");
/*     */         
/* 138 */         popup.sendQuestion();
/*     */       } 
/* 140 */       ok = answers.getProperty("mission").equals("skip");
/* 141 */       if (ok) {
/*     */         
/* 143 */         OldMission m = OldMission.getMission(this.missionNumber + 1, getResponder().getKingdomId());
/* 144 */         if (m != null) {
/*     */           
/* 146 */           ((Player)getResponder()).setTutorialLevel(this.missionNumber + 1);
/* 147 */           MissionQuestion ms = new MissionQuestion(m.number, getResponder(), m.title, m.missionDescription, this.target);
/*     */           
/* 149 */           ms.sendQuestion();
/*     */         }
/*     */         else {
/*     */           
/* 153 */           ((Player)getResponder()).setTutorialLevel(9999);
/*     */           
/* 155 */           SimplePopup popup = new SimplePopup(getResponder(), "Tutorial done!", "That concludes the tutorial! The " + guide.getName() + " is most pleased. Congratulations and good luck!");
/*     */           
/* 157 */           popup.sendQuestion();
/*     */         } 
/*     */       } 
/* 160 */       ok = answers.getProperty("mission").equals("skipall");
/* 161 */       if (ok) {
/*     */         
/* 163 */         ((Player)getResponder()).setTutorialLevel(9999);
/* 164 */         SimplePopup popup = new SimplePopup(getResponder(), "Tutorial done!", "You decide to skip all the missions for now. You may return later. Good luck!");
/*     */         
/* 166 */         popup.sendQuestion();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 174 */     Creature guide = null;
/*     */     
/*     */     try {
/* 177 */       guide = Server.getInstance().getCreature(this.target);
/*     */     }
/* 179 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */     
/*     */     }
/* 183 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */ 
/*     */     
/* 187 */     OldMission m = OldMission.getMission(this.missionNumber, getResponder().getKingdomId());
/*     */     
/* 189 */     StringBuilder buf = new StringBuilder();
/* 190 */     buf.append(getBmlHeaderNoQuestion());
/* 191 */     if (guide != null)
/* 192 */       buf.append("text{text=\"The " + guide.getName() + " looks at you sternly and says:\"}"); 
/* 193 */     buf.append("text{text=''}");
/* 194 */     buf.append("text{text=\"" + getQuestion() + "\"}");
/* 195 */     buf.append("text{text=''}");
/* 196 */     if (m.missionDescription2.length() > 0) {
/*     */       
/* 198 */       buf.append("text{text=\"" + m.missionDescription2 + "\"}");
/* 199 */       buf.append("text{text=''}");
/*     */     } 
/* 201 */     if (m.missionDescription3.length() > 0) {
/*     */       
/* 203 */       buf.append("text{text=\"" + m.missionDescription3 + "\"}");
/* 204 */       buf.append("text{text=''}");
/*     */     } 
/* 206 */     if (m.hasCheckBox()) {
/* 207 */       buf.append("checkbox{id='check';selected='false';text=\"" + m.checkBoxString + "\"}");
/*     */     } else {
/*     */       
/* 210 */       buf.append("radio{ group='mission'; id='do';text='I will do this';selected='true'}");
/* 211 */       buf.append("radio{ group='mission'; id='wait';text='I want to wait a while'}");
/*     */     } 
/* 213 */     buf.append("text{text=''}");
/* 214 */     buf.append("text{type='italic';text='You may see your current instructions by typing /mission in a chat window.'}");
/*     */     
/* 216 */     buf.append(createAnswerButton2());
/* 217 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MissionQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */