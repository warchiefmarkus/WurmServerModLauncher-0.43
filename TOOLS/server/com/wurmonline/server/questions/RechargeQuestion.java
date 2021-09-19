/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.items.Item;
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
/*     */ public final class RechargeQuestion
/*     */   extends Question
/*     */   implements MonetaryConstants
/*     */ {
/*     */   public RechargeQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  38 */     super(aResponder, aTitle, aQuestion, 73, aTarget);
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
/*     */   public void answer(Properties aAnswers) {
/*     */     try {
/*  51 */       Item wand = Items.getItem(this.target);
/*  52 */       if (wand.getOwnerId() == getResponder().getWurmId()) {
/*     */         
/*  54 */         String key = "recharge";
/*  55 */         String val = aAnswers.getProperty("recharge");
/*  56 */         if (val != null && val.equals("true")) {
/*     */           
/*  58 */           if (wand.getQualityLevel() < 90.0F) {
/*     */ 
/*     */             
/*     */             try {
/*  62 */               if (QuestionParser.charge(getResponder(), 50000L, "Recharging", 0.3F)) {
/*     */                 
/*  64 */                 getResponder().getCommunicator().sendNormalServerMessage("Something rummages through your pockets. The " + wand.getName() + " hums softly.");
/*  65 */                 wand.setQualityLevel(wand.getQualityLevel() + 10.0F);
/*     */               } else {
/*     */                 
/*  68 */                 getResponder().getCommunicator().sendNormalServerMessage("The spirits demand that you carry 5 silver in your inventory in coinage.");
/*     */               } 
/*  70 */             } catch (Exception ex) {
/*     */               
/*  72 */               getResponder().getCommunicator().sendNormalServerMessage(ex.getMessage());
/*     */             } 
/*     */           } else {
/*     */             
/*  76 */             getResponder().getCommunicator().sendNormalServerMessage("You may not recharge the " + wand.getName() + " while it has a quality level above 89.");
/*     */           } 
/*     */         } else {
/*  79 */           getResponder().getCommunicator().sendNormalServerMessage("You decide not to recharge the " + wand.getName() + " for now.");
/*     */         } 
/*     */       } else {
/*  82 */         getResponder().getCommunicator().sendNormalServerMessage("You are not in possession of the " + wand.getName() + " any longer and may not recharge it.");
/*     */       } 
/*  84 */     } catch (NoSuchItemException nsi) {
/*     */       
/*  86 */       getResponder().getCommunicator().sendNormalServerMessage("You can not recharge the item now.");
/*     */       return;
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
/*     */   public void sendQuestion() {
/*  99 */     StringBuilder buf = new StringBuilder();
/* 100 */     buf.append(getBmlHeader());
/*     */     
/*     */     try {
/* 103 */       Item item = Items.getItem(this.target);
/*     */       
/* 105 */       buf.append("label{text=\"Recharge the " + item.getName() + "?\"};");
/* 106 */       buf.append("text{text='Recharging will cost 5 silver coins and increase the quality level by 10 to a maximum of 99.'}text{text=''}");
/* 107 */       buf.append("text{text='You need to have the coins in your inventory.'}text{text=''}");
/* 108 */       buf.append("text{text=\"The current quality level is " + item.getQualityLevel() + ".\"}text{text=''}");
/* 109 */       buf.append("text{text=\"\"}");
/* 110 */       if (item.getQualityLevel() > 89.0F) {
/* 111 */         buf.append("text{text=\"You may not recharge the item yet.\"}");
/*     */       } else {
/*     */         
/* 114 */         buf.append("text{type='italic';text=\"Do you want to recharge the " + item.getName() + "?\"}");
/*     */         
/* 116 */         buf.append("radio{ group='recharge'; id='true';text='Yes'}");
/* 117 */         buf.append("radio{ group='recharge'; id='false';text='No';selected='true'}");
/*     */       }
/*     */     
/* 120 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 122 */       buf.append("text{text='The item can not be found.'}text{text=''}");
/*     */     } 
/* 124 */     buf.append(createAnswerButton2());
/* 125 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\RechargeQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */