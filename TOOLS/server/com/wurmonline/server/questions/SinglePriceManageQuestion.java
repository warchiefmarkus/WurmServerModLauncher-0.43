/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.items.Item;
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
/*     */ public final class SinglePriceManageQuestion
/*     */   extends Question
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(SinglePriceManageQuestion.class.getName());
/*     */   
/*     */   private final Item[] items;
/*     */   
/*     */   public SinglePriceManageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  41 */     super(aResponder, aTitle, aQuestion, 23, aTarget);
/*  42 */     this.items = new Item[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public SinglePriceManageQuestion(Creature aResponder, String aTitle, String aQuestion, Item[] aTargets) {
/*  47 */     super(aResponder, aTitle, aQuestion, 23, -10L);
/*  48 */     this.items = aTargets;
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
/*  59 */     setAnswer(answers);
/*  60 */     QuestionParser.parseSinglePriceQuestion(this);
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
/*  71 */     if (this.target == -10L) {
/*     */ 
/*     */       
/*  74 */       StringBuilder buf = new StringBuilder(getBmlHeader());
/*  75 */       buf.append("header{text=\"Price for multiple items\"}");
/*  76 */       buf.append("table{rows=\"2\";cols=\"8\";");
/*  77 */       buf.append("label{text=\" \"};label{text='Gold'};label{text=\" \"};label{text='Silver'};label{text=\" \"};label{text='Copper'};label{text=\" \"};label{text='Iron'};");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       buf.append("label{text=\" \"};input{maxchars=\"2\";id=\"gold\";text=\"0\"};label{text=\" \"};input{maxchars=\"2\";id=\"silver\";text=\"0\"};label{text=\" \"};input{maxchars=\"2\";id=\"copper\";text=\"0\"};label{text=\" \"};input{maxchars=\"2\";id=\"iron\";;text=\"0\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  87 */       buf.append("}");
/*  88 */       buf.append(createAnswerButton2());
/*  89 */       getResponder().getCommunicator().sendBml(400, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  95 */         Item item = Items.getItem(this.target);
/*  96 */         if (item.getOwnerId() == getResponder().getWurmId()) {
/*     */           
/*  98 */           StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */           
/* 100 */           buf.append("header{text=\"Price for " + item.getName().replace("\"", "''") + "\"}");
/* 101 */           buf.append("table{rows=\"2\";cols=\"5\";");
/*     */           
/* 103 */           buf.append("label{text=\"Item name\"};label{text='Gold'};label{text='Silver'};label{text='Copper'};label{text='Iron'};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 109 */           long wid = item.getWurmId();
/* 110 */           Change change = Economy.getEconomy().getChangeFor(item.getPrice());
/* 111 */           buf.append("label{text=\"" + item.getName().replace("\"", "''") + "\"};harray{label{text=\" \"};input{maxchars=\"3\";id=\"" + wid + "gold\";text=\"" + change
/* 112 */               .getGoldCoins() + "\"};};harray{label{text=\" \"};input{maxchars=\"3\";id=\"" + wid + "silver\";text=\"" + change
/* 113 */               .getSilverCoins() + "\"};};harray{label{text=\" \"};input{maxchars=\"3\";id=\"" + wid + "copper\";text=\"" + change
/* 114 */               .getCopperCoins() + "\"};};harray{label{text=\" \"};input{maxchars=\"3\";id=\"" + wid + "iron\";text=\"" + change
/* 115 */               .getIronCoins() + "\"};};");
/*     */           
/* 117 */           buf.append("}");
/*     */           
/* 119 */           buf.append(createAnswerButton2());
/* 120 */           getResponder().getCommunicator().sendBml(400, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */         } else {
/*     */           
/* 123 */           getResponder().getCommunicator().sendNormalServerMessage("You don't own that item.");
/*     */         } 
/* 125 */       } catch (NoSuchItemException nsc) {
/*     */         
/* 127 */         getResponder().getCommunicator().sendNormalServerMessage("No such item.");
/* 128 */         logger.log(Level.WARNING, getResponder().getName(), (Throwable)nsc);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Item[] getItems() {
/* 135 */     return this.items;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SinglePriceManageQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */