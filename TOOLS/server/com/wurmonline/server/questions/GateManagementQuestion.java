/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.FenceGate;
/*     */ import com.wurmonline.server.villages.NoSuchRoleException;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageStatus;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ public final class GateManagementQuestion
/*     */   extends Question
/*     */   implements VillageStatus, TimeConstants
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(GateManagementQuestion.class.getName());
/*     */   
/*     */   private boolean confirmed = false;
/*  45 */   private int hundredCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GateManagementQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  51 */     super(aResponder, aTitle, aQuestion, 42, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  57 */     String key = "gatenums";
/*  58 */     String val = answers.getProperty("gatenums");
/*  59 */     if (val != null && val.length() > 0)
/*     */     {
/*     */       
/*  62 */       for (int x = 0; x < 10; x++) {
/*     */         
/*  64 */         if (val.equals("g" + x)) {
/*     */           
/*  66 */           GateManagementQuestion vs = new GateManagementQuestion(getResponder(), "Manage gates", "Managing gates.", this.target);
/*     */ 
/*     */           
/*  69 */           if (vs != null) {
/*     */             
/*  71 */             vs.confirmed = true;
/*     */             
/*  73 */             vs.hundredCount = x + 1;
/*  74 */             vs.sendQuestion();
/*     */           } 
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*  80 */     setAnswer(answers);
/*  81 */     QuestionParser.parseGateManageQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*     */     try {
/*     */       Village village;
/*  92 */       if (this.target == -10L) {
/*  93 */         village = getResponder().getCitizenVillage();
/*     */       } else {
/*     */         
/*  96 */         Item deed = Items.getItem(this.target);
/*  97 */         int villageId = deed.getData2();
/*  98 */         village = Villages.getVillage(villageId);
/*     */       } 
/* 100 */       StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */ 
/*     */       
/* 103 */       buf.append("text{text=\"These settings only apply to gates with a gate lock.\"}");
/* 104 */       buf.append("text{type=\"bold\";text=\"When setting opening times, 0-23 means always open.\"}");
/* 105 */       buf.append("text{type=\"bold\";text=\"If the times are the same (such as 0-0 or 11-11) the gate will always be closed.\"}");
/*     */       
/* 107 */       Set<FenceGate> gates = village.getGates();
/* 108 */       if (gates != null) {
/*     */         
/* 110 */         int sz = gates.size();
/* 111 */         if (this.confirmed || sz < 100)
/*     */         {
/* 113 */           String rolename = "everybody";
/*     */           
/*     */           try {
/* 116 */             village.getRoleForStatus((byte)1);
/*     */           }
/* 118 */           catch (NoSuchRoleException noSuchRoleException) {}
/*     */ 
/*     */ 
/*     */           
/* 122 */           FenceGate[] gs = gates.<FenceGate>toArray(new FenceGate[gates.size()]);
/* 123 */           int max = gs.length;
/* 124 */           int start = 0;
/* 125 */           if (this.hundredCount > 0) {
/*     */ 
/*     */ 
/*     */             
/* 129 */             max = Math.min(gs.length, this.hundredCount * 100 - 1);
/* 130 */             start = Math.max(0, this.hundredCount - 1) * 100;
/*     */           } 
/* 132 */           buf.append("table{rows=\"" + (max - start) + "\";cols=\"7\";");
/*     */           
/* 134 */           for (int g = start; g < max; g++) {
/*     */             
/* 136 */             long gateId = gs[g].getWurmId();
/* 137 */             buf.append("label{text=\"Gate\"}input{id=\"gate" + gateId + "\";maxchars=\"40\";text=\"" + gs[g]
/* 138 */                 .getName() + "\"}label{text=\"Open to " + "everybody" + " from:\"}input{id=\"open" + gateId + "\";maxchars=\"2\";text=\"" + gs[g]
/*     */                 
/* 140 */                 .getOpenTime() + "\"}label{text=\"(0-23) to:\"}input{id=\"close" + gateId + "\";maxchars=\"2\"; text=\"" + gs[g]
/*     */                 
/* 142 */                 .getCloseTime() + "\"}label{text=\"(0-23)\"}");
/*     */           } 
/*     */           
/* 145 */           buf.append("}");
/*     */         }
/*     */         else
/*     */         {
/* 149 */           int x = sz / 100;
/*     */           
/* 151 */           buf.append("text{type=\"bold\";text=\"Select the range of gates to manage:\"}");
/* 152 */           for (int y = 0; y <= x; y++)
/*     */           {
/* 154 */             buf.append("radio{ group=\"gatenums\"; id=\"g" + y + "\";text=\"" + (y * 100) + "-" + y + "99\";selected=\"" + ((y == 0) ? 1 : 0) + "\"}");
/*     */           }
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 160 */         buf.append("text{text=\"No gates in this settlement found.\"}text{text=\"\"}");
/* 161 */       }  buf.append(createAnswerButton2());
/* 162 */       getResponder().getCommunicator().sendBml(450, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 164 */     catch (NoSuchItemException nsi) {
/*     */       
/* 166 */       logger.log(Level.WARNING, "Failed to locate village deed with id " + this.target, (Throwable)nsi);
/* 167 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*     */     
/*     */     }
/* 170 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 172 */       logger.log(Level.WARNING, "Failed to locate village for deed with id " + this.target, (Throwable)nsv);
/* 173 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GateManagementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */