/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.villages.PvPAlliance;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageRole;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class AllianceQuestion
/*     */   extends Question
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(AllianceQuestion.class.getName());
/*     */ 
/*     */   
/*     */   public AllianceQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  43 */     super(aResponder, aTitle, aQuestion, 18, aTarget);
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
/*  54 */     setAnswer(answers);
/*  55 */     QuestionParser.parsePvPAllianceQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAllianceName() {
/*     */     try {
/*  62 */       Creature sender = Server.getInstance().getCreature(this.target);
/*  63 */       if (sender.getCitizenVillage() != null)
/*     */       {
/*  65 */         PvPAlliance all = PvPAlliance.getPvPAlliance(sender.getCitizenVillage().getAllianceNumber());
/*  66 */         if (all != null) {
/*  67 */           return all.getName();
/*     */         }
/*     */         
/*  70 */         if (getResponder().getCitizenVillage() != null)
/*  71 */           return sender.getCitizenVillage().getName()
/*  72 */             .substring(0, Math.min(8, sender.getCitizenVillage().getName().length())) + "and" + 
/*     */             
/*  74 */             getResponder().getCitizenVillage().getName()
/*  75 */             .substring(0, Math.min(8, getResponder().getCitizenVillage().getName().length())); 
/*  76 */         return sender.getCitizenVillage().getName();
/*     */       }
/*     */     
/*     */     }
/*  80 */     catch (Exception nsc) {
/*     */       
/*  82 */       logger.log(Level.WARNING, nsc.getMessage(), nsc);
/*     */     } 
/*  84 */     return "unknown";
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
/*  97 */       Creature sender = Server.getInstance().getCreature(this.target);
/*  98 */       Village senderVillage = sender.getCitizenVillage();
/*  99 */       Village responderVillage = getResponder().getCitizenVillage();
/* 100 */       if (senderVillage.equals(responderVillage)) {
/* 101 */         sender.getCommunicator().sendNormalServerMessage("You cannot form an alliance within a settlement.");
/*     */       } else {
/*     */         
/* 104 */         PvPAlliance respAlliance = PvPAlliance.getPvPAlliance(responderVillage.getAllianceNumber());
/* 105 */         if (respAlliance != null) {
/*     */           
/* 107 */           sender.getCommunicator().sendNormalServerMessage(
/* 108 */               getResponder().getName() + " is already in the " + respAlliance.getName() + " alliance.");
/*     */           return;
/*     */         } 
/* 111 */         StringBuilder buf = new StringBuilder();
/* 112 */         VillageRole role = sender.getCitizenVillage().getCitizen(sender.getWurmId()).getRole();
/* 113 */         buf.append(getBmlHeader());
/* 114 */         buf.append("header{text=\"Citizens of " + responderVillage.getName() + "!\"}");
/* 115 */         buf.append("text{text=\"We, the citizens of " + senderVillage
/* 116 */             .getName() + ", under the wise leadership of " + sender
/*     */             
/* 118 */             .getName() + ", wish to declare our sincere intentions to invite you to the " + 
/* 119 */             getAllianceName() + " alliance.\"}");
/*     */ 
/*     */         
/* 122 */         buf.append("text{text='This union would stand forever, strengthening both our positions in this unfriendly world against common foes.'}");
/* 123 */         buf.append("text{text=''}");
/* 124 */         buf.append("text{text='We hope you see the possibilities in this, and return with a positive answer'}");
/* 125 */         buf.append("text{text=''}");
/* 126 */         buf.append("text{type='italic';text=\"" + sender.getName() + ", " + role.getName() + ", " + senderVillage
/* 127 */             .getName() + "\"}");
/*     */         
/* 129 */         buf.append("radio{ group='join'; id='accept';text='Accept'}");
/* 130 */         buf.append("radio{ group='join'; id='decline';text='Decline';selected='true'}");
/*     */         
/* 132 */         buf.append(createAnswerButton2());
/* 133 */         getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */       }
/*     */     
/* 136 */     } catch (NoSuchCreatureException nsc) {
/*     */       
/* 138 */       logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*     */     }
/* 140 */     catch (NoSuchPlayerException nsp) {
/*     */       
/* 142 */       logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AllianceQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */