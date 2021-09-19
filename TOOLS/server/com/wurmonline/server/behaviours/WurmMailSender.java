/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.WurmMail;
/*     */ import com.wurmonline.server.questions.MailReceiveQuestion;
/*     */ import com.wurmonline.server.questions.MailSendQuestion;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
/*     */ import java.util.Set;
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
/*     */ final class WurmMailSender
/*     */   implements MonetaryConstants, MiscConstants
/*     */ {
/*     */   static final void checkForWurmMail(Creature performer, Item mailbox) {
/*  47 */     if (mailbox.getOwnerId() == -10L) {
/*     */       
/*  49 */       if (performer.isWithinDistanceTo(mailbox.getPosX(), mailbox.getPosY(), mailbox.getPosZ(), 4.0F)) {
/*     */         
/*  51 */         BlockingResult result = Blocking.getBlockerBetween(performer, mailbox, 4);
/*  52 */         if (result == null) {
/*     */           
/*  54 */           if (mailbox.getSpellCourierBonus() > 0.0F) {
/*     */             
/*  56 */             if (mailbox.hasDarkMessenger() || mailbox.hasCourier()) {
/*     */               
/*  58 */               Set<WurmMail> set = WurmMail.getSentMailsFor(performer.getWurmId(), 100);
/*     */               
/*  60 */               if (!set.isEmpty()) {
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
/*  75 */                 (new MailReceiveQuestion(performer, "Retrieving mail", "Which items do you wish to retrieve?", mailbox))
/*  76 */                   .sendQuestion();
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/*  81 */                 performer.getCommunicator().sendNormalServerMessage("You have no mail.");
/*     */               } 
/*     */             } else {
/*  84 */               performer.getCommunicator().sendNormalServerMessage("The entities inside refuse to help you.");
/*     */             } 
/*     */           } else {
/*  87 */             performer.getCommunicator().sendNormalServerMessage("You have heard rumours that the mailbox will need some kind of enchantment to work.");
/*     */           } 
/*     */         } else {
/*     */           
/*  91 */           performer.getCommunicator().sendNormalServerMessage("You can't reach the " + mailbox.getName() + " now.");
/*     */         } 
/*     */       } else {
/*  94 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to do that now.");
/*     */       } 
/*     */     } else {
/*     */       
/*  98 */       performer.getCommunicator().sendNormalServerMessage("The mailbox must be planted on the ground.");
/*     */     } 
/*     */   }
/*     */   
/*     */   static final void sendWurmMail(Creature performer, Item mailbox) {
/* 103 */     if (mailbox.getOwnerId() == -10L) {
/*     */       
/* 105 */       if (performer.isWithinDistanceTo(mailbox.getPosX(), mailbox.getPosY(), mailbox.getPosZ(), 4.0F)) {
/*     */         
/* 107 */         BlockingResult result = Blocking.getBlockerBetween(performer, mailbox, 4);
/* 108 */         if (result == null) {
/*     */           
/* 110 */           if (mailbox.getSpellCourierBonus() > 0.0F) {
/*     */             
/* 112 */             if (mailbox.hasDarkMessenger() || mailbox.hasCourier()) {
/*     */               
/* 114 */               boolean ok = true;
/* 115 */               Item[] containedItems = mailbox.getItemsAsArray();
/* 116 */               if (containedItems.length != 0) {
/*     */                 
/* 118 */                 for (Item lContainedItem : containedItems) {
/*     */                   
/* 120 */                   if (lContainedItem.isNoDrop() || lContainedItem.isArtifact() || lContainedItem.isBodyPart() || lContainedItem
/* 121 */                     .isTemporary() || lContainedItem.isLiquid()) {
/*     */                     
/* 123 */                     performer.getCommunicator().sendNormalServerMessage("You may not send the " + lContainedItem
/* 124 */                         .getName() + ".");
/* 125 */                     ok = false;
/*     */                   }
/* 127 */                   else if (lContainedItem.lastOwner != performer.getWurmId()) {
/*     */                     
/* 129 */                     performer.getCommunicator().sendNormalServerMessage("You must possess the " + lContainedItem
/* 130 */                         .getName() + " in order to send it.");
/* 131 */                     ok = false;
/*     */                   } 
/*     */                 } 
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
/* 147 */                 if (ok) {
/* 148 */                   (new MailSendQuestion(performer, "Sending mail", "Calculate the cost", mailbox))
/* 149 */                     .sendQuestion();
/*     */                 }
/*     */               } else {
/* 152 */                 performer.getCommunicator().sendNormalServerMessage("The " + mailbox.getName() + " is empty.");
/*     */               } 
/*     */             } else {
/* 155 */               performer.getCommunicator().sendNormalServerMessage("The entities inside refuse to help you.");
/*     */             } 
/*     */           } else {
/* 158 */             performer.getCommunicator().sendNormalServerMessage("You have heard rumours that the mailbox will need some kind of enchantment to work.");
/*     */           } 
/*     */         } else {
/*     */           
/* 162 */           performer.getCommunicator().sendNormalServerMessage("You can't reach the " + mailbox.getName() + " now.");
/*     */         } 
/*     */       } else {
/* 165 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to do that now.");
/*     */       } 
/*     */     } else {
/* 168 */       performer.getCommunicator().sendNormalServerMessage("The mailbox must be planted on the ground.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WurmMailSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */