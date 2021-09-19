/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
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
/*     */ 
/*     */ public class RedeemQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(RedeemQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedeemQuestion(Creature aResponder, String aTitle, String aQuestion) {
/*  52 */     super(aResponder, aTitle, aQuestion, 87, aResponder.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/*  63 */     String key = "plays";
/*  64 */     String val = aAnswers.getProperty("plays");
/*     */     
/*     */     try {
/*  67 */       long wid = Long.parseLong(val);
/*  68 */       if (wid > 0L) {
/*     */         
/*  70 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wid);
/*  71 */         if (pinf != null) {
/*     */           
/*  73 */           if (pinf.hasMovedInventory()) {
/*  74 */             Items.returnItemsFromFreezerFor(wid);
/*     */           }
/*  76 */           Set<Item> items = Items.loadAllItemsForCreatureWithId(wid, false);
/*  77 */           if (items.size() > 0) {
/*     */ 
/*     */             
/*     */             try {
/*  81 */               Item chest = ItemFactory.createItem(192, (50 + Server.rand
/*  82 */                   .nextInt(30)), pinf.getName());
/*  83 */               for (Item i : items) {
/*     */                 
/*  85 */                 if (!i.isInventory() && !i.isBodyPart() && !i.isHomesteadDeed() && 
/*  86 */                   !i.isNewDeed() && !i.isVillageDeed() && i
/*  87 */                   .getTemplateId() != 166)
/*  88 */                   chest.insertItem(i, true); 
/*     */               } 
/*  90 */               getResponder().getInventory().insertItem(chest, true);
/*  91 */               getResponder().getCommunicator().sendNormalServerMessage("You redeem the items for " + pinf
/*  92 */                   .getName() + " and put them in a nice chest.");
/*  93 */               getResponder().getLogger().info(
/*  94 */                   getResponder().getName() + " redeems the items of " + pinf.getName() + ".");
/*  95 */               logger.log(Level.INFO, getResponder().getName() + " redeems the items of " + pinf.getName() + ".");
/*     */             }
/*  97 */             catch (Exception ex) {
/*     */               
/*  99 */               logger.log(Level.INFO, ex.getMessage(), ex);
/*     */             } 
/*     */           } else {
/*     */             
/* 103 */             getResponder().getCommunicator().sendNormalServerMessage("There were no items for " + pinf
/* 104 */                 .getName() + ".");
/*     */           } 
/*     */         } 
/*     */       } 
/* 108 */     } catch (NumberFormatException nfn) {
/*     */       
/* 110 */       getResponder().getCommunicator().sendNormalServerMessage("Unknown player " + val);
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
/* 122 */     PlayerInfo[] plays = PlayerInfoFactory.getPlayerInfos();
/* 123 */     String lHtml = getBmlHeader();
/* 124 */     StringBuilder buf = new StringBuilder(lHtml);
/* 125 */     buf
/* 126 */       .append("text{text=\"This functionality will retrieve all non-deed and non-writ items from a banned player and put them in a chest in your inventory.\"}");
/* 127 */     buf
/* 128 */       .append("text{text=\"The suggestion is to use the hide functionality to get a pair of random coordinates to put it at or simply hide it at the suggested location.\"}");
/* 129 */     buf.append("text{text=\"Only players with at least a year of bannination left will be listed here.\"}");
/* 130 */     buf.append("text{text=\"Select a banned player from which to redeem items:\"}");
/* 131 */     buf.append("radio{group=\"plays\";id=\"-1\";text=\"None\";selected=\"true\"};");
/* 132 */     for (int x = 0; x < plays.length; x++) {
/*     */       
/* 134 */       if (plays[x].isBanned() && plays[x].getCurrentServer() == Servers.localServer.id)
/*     */       {
/* 136 */         if ((plays[x]).banexpiry - System.currentTimeMillis() > 29030400000L)
/* 137 */           buf.append("radio{group=\"plays\";id=\"" + (plays[x]).wurmId + "\";text=\"" + plays[x].getName() + "\"};"); 
/*     */       }
/*     */     } 
/* 140 */     buf.append(createAnswerButton3());
/* 141 */     getResponder().getCommunicator().sendBml(500, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\RedeemQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */