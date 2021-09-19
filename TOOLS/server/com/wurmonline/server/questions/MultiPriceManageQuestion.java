/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.creatures.TradeHandler;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.Shop;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public final class MultiPriceManageQuestion
/*     */   extends Question
/*     */ {
/*  46 */   private static final Logger logger = Logger.getLogger(MultiPriceManageQuestion.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final Map<Long, Integer> itemMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   public MultiPriceManageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  54 */     super(aResponder, aTitle, aQuestion, 23, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  63 */     setAnswer(answers);
/*  64 */     QuestionParser.parseMultiPriceQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Long, Integer> getItemMap() {
/*  73 */     return this.itemMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*     */     try {
/*  81 */       int idx = 0;
/*  82 */       Creature trader = Server.getInstance().getCreature(this.target);
/*  83 */       if (trader.isNpcTrader())
/*     */       {
/*  85 */         Shop shop = Economy.getEconomy().getShop(trader);
/*  86 */         if (shop == null)
/*     */         {
/*  88 */           getResponder().getCommunicator().sendNormalServerMessage("No shop registered for that creature.");
/*     */ 
/*     */         
/*     */         }
/*  92 */         else if (shop.getOwnerId() == getResponder().getWurmId())
/*     */         {
/*  94 */           Item[] items = trader.getInventory().getAllItems(false);
/*  95 */           Arrays.sort((Object[])items);
/*  96 */           int removed = 0;
/*  97 */           for (int x = 0; x < items.length; x++) {
/*     */ 
/*     */             
/* 100 */             if (items[x].isFullprice())
/* 101 */               removed++; 
/*     */           } 
/* 103 */           String lHtml = getBmlHeader();
/* 104 */           StringBuilder buf = new StringBuilder(lHtml);
/* 105 */           DecimalFormat df = new DecimalFormat("#.##");
/* 106 */           buf.append("text{text=\"" + trader.getName() + " may put up " + (
/* 107 */               TradeHandler.getMaxNumPersonalItems() - trader.getNumberOfShopItems()) + " more items for sale.\"}");
/*     */           
/* 109 */           buf.append("text{type=\"bold\";text=\"Prices for " + trader.getName() + "\"}text{text=''}");
/* 110 */           buf.append("table{rows=\"" + (items.length - removed + 1) + "\"; cols=\"7\";label{text=\"Item name\"};label{text=\"QL\"};label{text=\"DMG\"};label{text=\"Gold\"};label{text=\"Silver\"};label{text=\"Copper\"};label{text=\"Iron\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 119 */           for (int i = 0; i < items.length; i++) {
/*     */ 
/*     */             
/* 122 */             if (!items[i].isFullprice()) {
/*     */               
/* 124 */               long wid = items[i].getWurmId();
/* 125 */               idx++;
/* 126 */               Change change = Economy.getEconomy().getChangeFor(items[i].getPrice());
/* 127 */               buf.append(itemNameWithColorByRarity(items[i]));
/* 128 */               buf.append("label{text=\"" + df.format(items[i].getQualityLevel()) + "\"};");
/* 129 */               buf.append("label{text=\"" + df.format(items[i].getDamage()) + "\"};");
/* 130 */               buf.append("harray{input{maxchars=\"3\"; id=\"" + idx + "g\";text=\"" + change.getGoldCoins() + "\"};label{text=\" \"}};");
/* 131 */               buf.append("harray{input{maxchars=\"2\"; id=\"" + idx + "s\";text=\"" + change.getSilverCoins() + "\"};label{text=\" \"}};");
/* 132 */               buf.append("harray{input{maxchars=\"2\"; id=\"" + idx + "c\";text=\"" + change.getCopperCoins() + "\"};label{text=\" \"}};");
/* 133 */               buf.append("harray{input{maxchars=\"2\"; id=\"" + idx + "i\";text=\"" + change.getIronCoins() + "\"};label{text=\" \"}}");
/* 134 */               this.itemMap.put(new Long(wid), Integer.valueOf(idx));
/*     */             } 
/*     */           } 
/*     */           
/* 138 */           buf.append("}");
/* 139 */           buf.append(createAnswerButton2());
/* 140 */           getResponder().getCommunicator().sendBml(500, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */         }
/*     */         else
/*     */         {
/* 144 */           getResponder().getCommunicator().sendNormalServerMessage("You don't own that shop.");
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 149 */     } catch (NoSuchCreatureException nsc) {
/*     */       
/* 151 */       getResponder().getCommunicator().sendNormalServerMessage("No such creature.");
/* 152 */       logger.log(Level.WARNING, getResponder().getName(), (Throwable)nsc);
/*     */     }
/* 154 */     catch (NoSuchPlayerException nsp) {
/*     */       
/* 156 */       getResponder().getCommunicator().sendNormalServerMessage("No such creature.");
/* 157 */       logger.log(Level.WARNING, getResponder().getName(), (Throwable)nsp);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MultiPriceManageQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */