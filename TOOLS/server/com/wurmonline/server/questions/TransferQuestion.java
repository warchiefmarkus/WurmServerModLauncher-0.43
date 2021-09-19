/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
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
/*     */ public class TransferQuestion
/*     */   extends Question
/*     */ {
/*     */   public TransferQuestion(Creature aResponder, String aTitle, String aQuestion) {
/*  44 */     super(aResponder, aTitle, aQuestion, 88, -10L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  50 */     if (getResponder().isPlayer()) {
/*     */       
/*  52 */       Player rp = (Player)getResponder();
/*  53 */       if (rp.hasFlag(74)) {
/*     */         
/*  55 */         rp.getCommunicator().sendNormalServerMessage("You do not have a free faith transfer available.");
/*     */         return;
/*     */       } 
/*  58 */       if (rp.getDeity() == null) {
/*     */         
/*  60 */         rp.getCommunicator().sendNormalServerMessage("You currently pray to no deity and cannot transfer to a new one.");
/*     */         return;
/*     */       } 
/*  63 */       if (rp.isChampion()) {
/*     */         
/*  65 */         rp.getCommunicator().sendNormalServerMessage("Champions cannot convert faith with this command.");
/*     */         
/*     */         return;
/*     */       } 
/*  69 */       String key = "deityid";
/*  70 */       String val = answers.getProperty("deityid");
/*  71 */       if (val != null) {
/*     */         
/*     */         try
/*     */         {
/*  75 */           int index = Integer.parseInt(val);
/*  76 */           int newDeity = Deities.getDeities()[index].getNumber();
/*     */           
/*  78 */           if (newDeity == 0 || newDeity == rp.getDeity().getNumber()) {
/*     */             
/*  80 */             rp.getCommunicator().sendNormalServerMessage("You decide not to change deity for now.");
/*     */             
/*     */             return;
/*     */           } 
/*  84 */           Deity newd = Deities.getDeity(newDeity);
/*  85 */           if (!QuestionParser.doesKingdomTemplateAcceptDeity(rp.getKingdomTemplateId(), newd)) {
/*     */             
/*  87 */             rp.getCommunicator().sendNormalServerMessage("Your kingdom does not allow following that god.");
/*     */             
/*     */             return;
/*     */           } 
/*     */           try {
/*  92 */             rp.getSaveFile().transferDeity(newd);
/*     */             
/*  94 */             rp.getCommunicator().sendNormalServerMessage("You decide to use your transfer and change deity to " + newd.getName() + ".");
/*  95 */             rp.setFlag(74, true);
/*     */           }
/*  97 */           catch (IOException iox) {
/*     */             
/*  99 */             rp.getCommunicator().sendNormalServerMessage("An exception occurred when changing deity. Please try again later or use /support if this persists.");
/*     */           }
/*     */         
/*     */         }
/* 103 */         catch (NumberFormatException nfe)
/*     */         {
/* 105 */           rp.getCommunicator().sendNormalServerMessage("Failed to parse index " + val);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 110 */         getResponder().getCommunicator().sendNormalServerMessage("You decide not to change deity for now.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 118 */     Deity[] deities = Deities.getDeities();
/* 119 */     String[] deityNames = new String[deities.length];
/* 120 */     int defaultId = 0;
/* 121 */     for (int i = 0; i < deities.length; i++) {
/*     */       
/* 123 */       deityNames[i] = deities[i].getName();
/* 124 */       if (deities[i] == getResponder().getDeity())
/* 125 */         defaultId = i; 
/*     */     } 
/* 127 */     BMLBuilder bml = BMLBuilder.createNormalWindow(Integer.toString(getId()), "Transfer your faith to which deity?", 
/* 128 */         BMLBuilder.createGenericBuilder()
/* 129 */         .addText("You currently have a single use free faith transfer from your current deity of " + getResponder().getDeity().getName() + " to another of your choice from the list below.")
/*     */         
/* 131 */         .addText("")
/* 132 */         .addText("Once you select which deity to transfer to and click accept, you will not be able to transfer back. Choose wisely.", null, BMLBuilder.TextType.BOLD, null)
/*     */         
/* 134 */         .addText("")
/* 135 */         .addText("Warning: Converting to a deity on Freedom then travelling to a Chaos kingdom that does not align with your deity you will lose all faith and abilities granted, and you will stop following that deity. Libila does not align with WL kingdoms and Fo/Vynora/Magranon do not align with BL kingdoms.", null, BMLBuilder.TextType.BOLD, Color.RED)
/*     */ 
/*     */         
/* 138 */         .addText("")
/* 139 */         .addLabel("Choose a deity to transfer your faith to:")
/* 140 */         .addDropdown("deityid", Integer.toString(defaultId), deityNames)
/* 141 */         .addText("")
/* 142 */         .addButton("submit", "Accept", null, null, null, true));
/*     */     
/* 144 */     getResponder().getCommunicator().sendBml(350, 330, true, true, bml.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TransferQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */