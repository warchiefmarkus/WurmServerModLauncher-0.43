/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.creatures.TradeHandler;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.Shop;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.WurmMail;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ public final class TraderManagementQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  52 */   private static final Logger logger = Logger.getLogger(TraderManagementQuestion.class.getName());
/*     */   
/*     */   public static final int maxTraders = 1;
/*     */   private final boolean isDismissing;
/*     */   
/*     */   public TraderManagementQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  58 */     super(aResponder, aTitle, aQuestion, 22, aTarget);
/*  59 */     this.isDismissing = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TraderManagementQuestion(Creature aResponder, String aTitle, String aQuestion, Creature aTarget) {
/*  65 */     super(aResponder, aTitle, aQuestion, 22, aTarget.getWurmId());
/*  66 */     this.isDismissing = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dismissMerchant(Creature dismisser, long target) {
/*     */     try {
/*  76 */       Creature trader = Creatures.getInstance().getCreature(target);
/*  77 */       if (trader != null) {
/*     */         
/*  79 */         if (!trader.isTrading()) {
/*     */           
/*  81 */           Server.getInstance().broadCastAction(trader
/*  82 */               .getName() + " grunts, packs " + trader.getHisHerItsString() + " things and is off.", trader, 5);
/*     */           
/*  84 */           if (dismisser != null) {
/*     */             
/*  86 */             dismisser.getCommunicator().sendNormalServerMessage("You dismiss " + trader
/*  87 */                 .getName() + " from " + trader.getHisHerItsString() + " post.");
/*  88 */             logger.log(Level.INFO, dismisser.getName() + " dismisses trader " + trader.getName() + " with WurmID: " + target);
/*     */           }
/*     */           else {
/*     */             
/*  92 */             logger.log(Level.INFO, "Merchant " + trader.getName() + " with WurmID: " + target + " dismissed by timeout");
/*     */           } 
/*     */           
/*  95 */           Item[] items = Items.getAllItems();
/*     */           
/*  97 */           for (int x = 0; x < items.length; x++) {
/*     */             
/*  99 */             if (items[x].getTemplateId() == 300)
/*     */             {
/* 101 */               if (items[x].getData() == target) {
/*     */                 
/* 103 */                 items[x].setData(-1, -1);
/*     */                 break;
/*     */               } 
/*     */             }
/*     */           } 
/* 108 */           Shop shop = Economy.getEconomy().getShop(trader);
/* 109 */           if (shop != null) {
/*     */ 
/*     */             
/*     */             try {
/* 113 */               Item backPack = ItemFactory.createItem(1, 10.0F + Server.rand
/* 114 */                   .nextFloat() * 10.0F, trader.getName());
/* 115 */               backPack.setDescription("Due to poor business I have moved on. Thank you for your time. " + trader
/* 116 */                   .getName());
/*     */ 
/*     */ 
/*     */               
/* 120 */               ArrayList<Item> largeItems = new ArrayList<>();
/* 121 */               for (Item realItem : trader.getInventory().getAllItems(false)) {
/*     */                 
/* 123 */                 if (realItem.getTemplate() != null && !realItem.getTemplate().isComponentItem() && 
/* 124 */                   !backPack.insertItem(realItem, false)) {
/* 125 */                   largeItems.add(realItem);
/*     */                 }
/*     */               } 
/*     */ 
/*     */               
/* 130 */               WurmMail mail = new WurmMail((byte)0, backPack.getWurmId(), shop.getOwnerId(), shop.getOwnerId(), 0L, System.currentTimeMillis() + 60000L, System.currentTimeMillis() + (Servers.isThisATestServer() ? 3600000L : 14515200000L), Servers.localServer.id, false, false);
/*     */               
/* 132 */               WurmMail.addWurmMail(mail);
/* 133 */               mail.createInDatabase();
/* 134 */               backPack.putInVoid();
/* 135 */               backPack.setMailed(true);
/* 136 */               backPack.setMailTimes((byte)(backPack.getMailTimes() + 1));
/*     */               
/* 138 */               for (Item i : largeItems)
/*     */               {
/*     */ 
/*     */ 
/*     */                 
/* 143 */                 WurmMail largeMail = new WurmMail((byte)0, i.getWurmId(), shop.getOwnerId(), shop.getOwnerId(), 0L, System.currentTimeMillis() + 60000L, System.currentTimeMillis() + (Servers.isThisATestServer() ? 3600000L : 14515200000L), Servers.localServer.id, false, false);
/*     */                 
/* 145 */                 WurmMail.addWurmMail(largeMail);
/* 146 */                 largeMail.createInDatabase();
/* 147 */                 i.putInVoid();
/* 148 */                 i.setMailed(true);
/* 149 */                 i.setMailTimes((byte)(i.getMailTimes() + 1));
/*     */               }
/*     */             
/* 152 */             } catch (Exception e) {
/*     */               
/* 154 */               logger.log(Level.WARNING, e.getMessage() + " " + trader.getName() + " at " + trader
/* 155 */                   .getTileX() + ", " + trader.getTileY(), e);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 172 */             logger.log(Level.WARNING, "No shop when dismissing trader " + trader
/* 173 */                 .getName() + " " + trader.getWurmId());
/* 174 */           }  trader.destroy();
/*     */         }
/* 176 */         else if (dismisser != null) {
/* 177 */           dismisser.getCommunicator().sendNormalServerMessage(trader.getName() + " is trading. Try later.");
/*     */         } 
/* 179 */       } else if (dismisser != null) {
/* 180 */         dismisser.getCommunicator().sendNormalServerMessage("An error occured on the server while dismissing the trader.");
/*     */       }
/*     */     
/* 183 */     } catch (NoSuchCreatureException nsc) {
/*     */       
/* 185 */       if (dismisser != null) {
/* 186 */         dismisser.getCommunicator().sendNormalServerMessage("The merchant can not be dismissed now.");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 198 */     setAnswer(answers);
/* 199 */     if (this.isDismissing) {
/*     */       
/* 201 */       String key = "dism";
/* 202 */       String val = answers.getProperty("dism");
/* 203 */       if (Boolean.parseBoolean(val)) {
/*     */         
/* 205 */         dismissMerchant(getResponder(), this.target);
/*     */       } else {
/*     */         
/* 208 */         getResponder().getCommunicator().sendNormalServerMessage("You decide not to dismiss the trader.");
/*     */       } 
/*     */     } else {
/* 211 */       QuestionParser.parseTraderManagementQuestion(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 222 */     StringBuilder buf = new StringBuilder();
/* 223 */     if (this.isDismissing) {
/* 224 */       buf.append(mayorDismissingQuestion());
/*     */     } else {
/* 226 */       buf.append(contractQuestion());
/*     */     } 
/* 228 */     buf.append(createAnswerButton3());
/*     */     
/* 230 */     if (this.isDismissing) {
/* 231 */       getResponder().getCommunicator().sendBml(250, 200, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } else {
/* 233 */       getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String contractQuestion() {
/* 238 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 240 */     Item contract = null;
/* 241 */     Creature trader = null;
/* 242 */     Shop shop = null;
/* 243 */     long traderId = -1L;
/*     */     
/*     */     try {
/* 246 */       contract = Items.getItem(this.target);
/* 247 */       traderId = contract.getData();
/* 248 */       if (traderId != -1L) {
/*     */         
/* 250 */         trader = Server.getInstance().getCreature(traderId);
/* 251 */         if (trader.isNpcTrader()) {
/* 252 */           shop = Economy.getEconomy().getShop(trader);
/*     */         }
/*     */       } 
/* 255 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 257 */       logger.log(Level.WARNING, getResponder().getName() + " contract is missing!");
/*     */     }
/* 259 */     catch (NoSuchPlayerException nsp) {
/*     */       
/* 261 */       logger.log(Level.WARNING, "Trader for " + getResponder().getName() + " is a player? Well it can't be found.");
/* 262 */       contract.setData(-10L);
/*     */     }
/* 264 */     catch (NoSuchCreatureException nsc) {
/*     */       
/* 266 */       logger.log(Level.WARNING, "Trader for " + getResponder().getName() + " can't be found.");
/* 267 */       contract.setData(-10L);
/*     */     } 
/* 269 */     if (shop != null) {
/* 270 */       buf.append(getBmlHeaderWithScroll());
/*     */     } else {
/* 272 */       buf.append(getBmlHeader());
/*     */     } 
/* 274 */     buf.append("text{type=\"bold\";text=\"Trader information:\"}");
/* 275 */     buf.append("text{type=\"italic\";text=\"A personal merchant tries to sell anything you give him to other players. Then you can come back and collect the money.\"}");
/* 276 */     buf.append("text{text=\"Merchants will only appear by market stalls or in finished structures where no other creatures but you stand.\"}");
/* 277 */     buf.append("text{type=\"bold\";text=\"Note that if you change kingdom for any reason, you will lose this contract since the merchant stays in the old kingdom.\"}");
/* 278 */     buf.append("text{text=\"If you are away for several months the merchant may leave or be forced to leave with all the items and coins in his inventory.\"}");
/*     */     
/* 280 */     if (shop != null) {
/*     */       
/* 282 */       buf.append("text{type=\"bold\";text=\"Use local price\"};text{text=\" means that the merchant will use his local supply when determining price for items.\"}");
/* 283 */       buf.append("text{text=\"Otherwise the price used will be the standard base value for the item.\"}");
/* 284 */       buf.append("text{type=\"bold\";text=\"Price modifier\"};text{text=\" is the value he will apply to the price.\"}");
/* 285 */       buf.append("text{text=\"If you set specific prices on items, those prices will be used regardless of any price settings.\"}");
/* 286 */       buf.append("text{type=\"bold\";text=\"Last sold\"};text{text=\"is the number of days, hours and minutes since a personal merchant last sold an item.\"}");
/*     */       
/* 288 */       long timeleft = 0L;
/* 289 */       if (trader != null) {
/*     */         
/* 291 */         buf.append("table{rows=\"2\";cols=\"9\";label{text=\"name\"};label{text=\"Use local price\"};label{text=\"Price modifier\"};label{text=\"Manage prices\"};label{text=\"Last sold\"};label{text=\"Sold month\"};label{text=\"Sold life\"};label{text=\"Ratio\"};label{text=\"Free slots\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 301 */         timeleft = System.currentTimeMillis() - shop.getLastPolled();
/* 302 */         long daysleft = timeleft / 86400000L;
/* 303 */         long hoursleft = (timeleft - daysleft * 86400000L) / 3600000L;
/* 304 */         long minutesleft = (timeleft - daysleft * 86400000L - hoursleft * 3600000L) / 60000L;
/* 305 */         String times = "";
/* 306 */         if (daysleft > 0L)
/* 307 */           times = times + daysleft + " days"; 
/* 308 */         if (hoursleft > 0L) {
/*     */           
/* 310 */           String aft = "";
/* 311 */           if (daysleft > 0L && minutesleft > 0L) {
/*     */             
/* 313 */             times = times + ", ";
/* 314 */             aft = aft + " and ";
/*     */           }
/* 316 */           else if (daysleft > 0L) {
/*     */             
/* 318 */             times = times + " and ";
/*     */           }
/* 320 */           else if (minutesleft > 0L) {
/* 321 */             aft = aft + " and ";
/* 322 */           }  times = times + hoursleft + " hours" + aft;
/*     */         } 
/* 324 */         if (minutesleft > 0L)
/* 325 */           times = times + minutesleft + " minutes"; 
/* 326 */         buf.append("label{text=\"" + trader.getName() + "\"};");
/*     */         
/* 328 */         String ch = shop.usesLocalPrice() ? "selected=\"true\";" : "selected=\"false\";";
/* 329 */         buf.append("checkbox{id=\"" + traderId + "local\";" + ch + "text=\" \"}");
/* 330 */         buf.append("input{maxchars=\"4\"; id=\"" + traderId + "pricemod\"; text=\"" + shop.getPriceModifier() + "\"}");
/* 331 */         buf.append("checkbox{id=\"" + traderId + "manage\";selected=\"false\";text=\" \"}");
/* 332 */         buf.append("label{text=\"" + times + "\"}");
/* 333 */         buf.append("label{text=\"" + (new Change(shop.getMoneyEarnedMonth())).getChangeShortString() + "\"}");
/* 334 */         buf.append("label{text=\"" + (new Change(shop.getMoneyEarnedLife())).getChangeShortString() + "\"}");
/* 335 */         buf.append("label{text=\"" + shop.getSellRatio() + "\"}");
/* 336 */         buf.append("label{text=\"" + (TradeHandler.getMaxNumPersonalItems() - trader.getNumberOfShopItems()) + "\"}}");
/*     */ 
/*     */         
/* 339 */         buf.append("text{type=\"bold\";text=\"Dismissing\"};text{text=\"if you dismiss a merchant they will take all items with them!\"}");
/* 340 */         buf.append("harray{label{text=\"Dismiss\"};checkbox{id=\"" + traderId + "dismiss\";selected=\"false\";text=\" \"}}");
/*     */       }
/*     */       else {
/*     */         
/* 344 */         buf.append("label{text=\"A merchant that should be here is missing. The id is " + traderId + "\"}");
/*     */       } 
/*     */     } else {
/*     */       
/* 348 */       buf.append("text{type=\"bold\";text=\"Hire personal merchant:\"}");
/* 349 */       buf.append("text{text=\"By using this contract a personal merchant will appear.\"}");
/* 350 */       buf.append("text{text=\"The merchant will appear where you stand, if the tile contains no other creature.\"}");
/* 351 */       buf.append("text{text=\"Every trade he does he will charge one tenth (10%) of the value sold.\"}");
/* 352 */       buf.append("text{text=\"You add items to his stock, and retrieve money for items he has sold by trading with him.\"}");
/* 353 */       buf.append("text{text=\"Gender: \"}");
/* 354 */       if (getResponder().getSex() == 1) {
/*     */         
/* 356 */         buf.append("radio{ group=\"gender\"; id=\"male\";text=\"Male\"}");
/* 357 */         buf.append("radio{ group=\"gender\"; id=\"female\";text=\"Female\";selected=\"true\"}");
/*     */       }
/*     */       else {
/*     */         
/* 361 */         buf.append("radio{ group=\"gender\"; id=\"male\";text=\"Male\";selected=\"true\"}");
/* 362 */         buf.append("radio{ group=\"gender\"; id=\"female\";text=\"Female\"}");
/*     */       } 
/* 364 */       buf.append("harray{label{text=\"The merchant shalt be called \"};input{id=\"ptradername\";maxchars=\"20\"};label{text=\"!\"}}");
/*     */     } 
/* 366 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String mayorDismissingQuestion() {
/* 371 */     StringBuilder buf = new StringBuilder();
/* 372 */     buf.append(getBmlHeader());
/*     */ 
/*     */     
/*     */     try {
/* 376 */       Creature trader = Creatures.getInstance().getCreature(this.target);
/* 377 */       if (trader.isNpcTrader()) {
/*     */         
/* 379 */         Shop shop = Economy.getEconomy().getShop(trader);
/*     */         
/* 381 */         buf.append("text{text=\"You may dismiss this merchant now, since ");
/* 382 */         if (shop.getNumberOfItems() == 0 && shop.howLongEmpty() > 2419200000L) {
/* 383 */           buf.append("it has not had anything for sale for a long time and is bored.\"}");
/*     */         } else {
/* 385 */           buf.append("the person controlling it is long gone.\"}");
/*     */         } 
/* 387 */         buf.append("text{text=\"Will you dismiss this merchant?\"}");
/*     */         
/* 389 */         buf.append("radio{ group=\"dism\";id=\"true\";text=\"Yes\"}");
/* 390 */         buf.append("radio{ group=\"dism\";id=\"false\";text=\"No\";selected=\"true\"}");
/*     */       } else {
/*     */         
/* 393 */         buf.append("text{text=\"Not a merchant?\"}");
/*     */       } 
/* 395 */     } catch (NoSuchCreatureException e) {
/*     */       
/* 397 */       logger.log(Level.WARNING, "Merchant for " + getResponder().getName() + " can't be found.");
/* 398 */       buf.append("label{text=\"Missing merchant?\"}");
/*     */     } 
/*     */     
/* 401 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TraderManagementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */