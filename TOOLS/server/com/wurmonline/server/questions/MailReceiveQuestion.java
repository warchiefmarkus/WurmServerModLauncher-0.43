/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.items.InscriptionData;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemSpellEffects;
/*     */ import com.wurmonline.server.items.WurmColor;
/*     */ import com.wurmonline.server.items.WurmMail;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.players.PlayerState;
/*     */ import com.wurmonline.server.spells.SpellEffect;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.io.IOException;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public final class MailReceiveQuestion
/*     */   extends Question
/*     */   implements MonetaryConstants, TimeConstants
/*     */ {
/*  61 */   private static final Logger logger = Logger.getLogger(MailReceiveQuestion.class.getName());
/*     */   private final Item mbox;
/*  63 */   private Set<WurmMail> mailset = null;
/*     */ 
/*     */   
/*     */   public MailReceiveQuestion(Creature aResponder, String aTitle, String aQuestion, Item aMailbox) {
/*  67 */     super(aResponder, aTitle, aQuestion, 53, aMailbox.getWurmId());
/*  68 */     this.mbox = aMailbox;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  74 */     if (!this.mbox.isEmpty(false)) {
/*     */       
/*  76 */       getResponder().getCommunicator().sendNormalServerMessage("Empty the mailbox first.");
/*     */     }
/*     */     else {
/*     */       
/*  80 */       if (this.mailset.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/*  84 */       if (!Servers.loginServer.isAvailable(5, true)) {
/*     */         
/*  86 */         getResponder().getCommunicator().sendNormalServerMessage("You may not receive mail right now. Please try later.");
/*     */         
/*     */         return;
/*     */       } 
/*  90 */       int x = 0;
/*  91 */       float priceMod = 1.0F;
/*  92 */       int pcost = 0;
/*  93 */       long fullcost = 0L;
/*  94 */       Map<Long, Long> moneyToSend = new HashMap<>();
/*  95 */       WurmMail m = null;
/*  96 */       String val = "";
/*  97 */       Set<Item> itemset = new HashSet<>();
/*  98 */       Map<Long, WurmMail> toReturn = null;
/*     */       
/* 100 */       for (Iterator<WurmMail> it = this.mailset.iterator(); it.hasNext(); ) {
/*     */         
/* 102 */         x++;
/* 103 */         m = it.next();
/* 104 */         priceMod = 1.0F;
/*     */ 
/*     */         
/* 107 */         long receiver = WurmMail.getReceiverForItem(m.itemId);
/* 108 */         if (receiver == getResponder().getWurmId()) {
/*     */           
/* 110 */           val = answers.getProperty(x + "receive");
/* 111 */           if (val != null && val.equals("true")) {
/*     */ 
/*     */             
/*     */             try {
/* 115 */               Item item = Items.getItem(m.itemId);
/* 116 */               if (m.rejected) {
/*     */                 
/* 118 */                 pcost = 100;
/* 119 */                 if (item.getTemplateId() == 748 || item.getTemplateId() == 1272) {
/*     */                   
/* 121 */                   InscriptionData insData = item.getInscription();
/* 122 */                   if (insData != null)
/*     */                   {
/* 124 */                     if (insData.hasBeenInscribed())
/* 125 */                       pcost = 1; 
/*     */                   }
/*     */                 } 
/* 128 */                 fullcost += pcost;
/*     */               }
/* 130 */               else if (m.type == 1) {
/*     */                 
/* 132 */                 pcost = MailSendConfirmQuestion.getCostForItem(item, priceMod);
/* 133 */                 fullcost += pcost;
/* 134 */                 fullcost += m.price;
/* 135 */                 if (m.price > 0L) {
/*     */                   
/* 137 */                   Long msend = moneyToSend.get(Long.valueOf(m.sender));
/* 138 */                   if (msend == null) {
/* 139 */                     msend = Long.valueOf(m.price);
/*     */                   } else {
/* 141 */                     msend = Long.valueOf(msend.longValue() + m.price);
/* 142 */                   }  moneyToSend.put(Long.valueOf(m.sender), msend);
/*     */                 } 
/* 144 */                 Item[] contained = item.getAllItems(true);
/* 145 */                 for (int c = 0; c < contained.length; c++) {
/*     */                   
/* 147 */                   pcost = MailSendConfirmQuestion.getCostForItem(contained[c], priceMod);
/* 148 */                   fullcost += pcost;
/*     */                 } 
/*     */               } 
/* 151 */               itemset.add(item);
/*     */             }
/* 153 */             catch (NoSuchItemException nsi) {
/*     */               
/* 155 */               logger.log(Level.INFO, " NO SUCH ITEM");
/* 156 */               WurmMail.deleteMail(m.itemId);
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/* 161 */           val = answers.getProperty(x + "return");
/* 162 */           if (val != null && val.equals("true")) {
/*     */             
/* 164 */             if (toReturn == null)
/* 165 */               toReturn = new HashMap<>(); 
/* 166 */             toReturn.put(Long.valueOf(m.itemId), m); continue;
/*     */           } 
/* 168 */           if (m.isExpired()) {
/*     */             
/* 170 */             if (toReturn == null)
/* 171 */               toReturn = new HashMap<>(); 
/* 172 */             toReturn.put(Long.valueOf(m.itemId), m);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 177 */       if (toReturn != null) {
/*     */         
/* 179 */         Map<Integer, Set<WurmMail>> serverReturns = new HashMap<>();
/*     */         
/* 181 */         for (WurmMail retm : toReturn.values()) {
/*     */           
/* 183 */           long timeavail = System.currentTimeMillis() + (101 - (int)this.mbox.getSpellCourierBonus()) * 60000L;
/* 184 */           if (getResponder().getPower() > 0) {
/* 185 */             timeavail = System.currentTimeMillis() + 60000L;
/*     */           }
/*     */           
/* 188 */           WurmMail mail = new WurmMail((byte)1, retm.itemId, getResponder().getWurmId(), retm.sender, 0L, timeavail, System.currentTimeMillis() + (Servers.localServer.testServer ? 3600000L : 604800000L), Servers.localServer.id, true, false);
/*     */ 
/*     */           
/* 191 */           if (retm.sourceserver == Servers.localServer.id) {
/*     */             
/* 193 */             WurmMail.removeMail(retm.itemId);
/* 194 */             WurmMail.addWurmMail(mail);
/* 195 */             mail.createInDatabase();
/*     */             
/*     */             continue;
/*     */           } 
/* 199 */           Set<WurmMail> returnSet = serverReturns.get(Integer.valueOf(retm.sourceserver));
/* 200 */           if (returnSet == null)
/* 201 */             returnSet = new HashSet<>(); 
/* 202 */           returnSet.add(mail);
/* 203 */           serverReturns.put(Integer.valueOf(retm.sourceserver), returnSet);
/*     */         } 
/*     */         
/* 206 */         if (!serverReturns.isEmpty()) {
/*     */           
/* 208 */           Map<Long, ReiceverReturnMails> returnsPerReceiver = new HashMap<>();
/* 209 */           for (Iterator<Map.Entry<Integer, Set<WurmMail>>> iterator = serverReturns.entrySet().iterator(); iterator.hasNext(); ) {
/*     */             
/* 211 */             Map.Entry<Integer, Set<WurmMail>> entry = iterator.next();
/* 212 */             Integer sid = entry.getKey();
/* 213 */             Set<WurmMail> mails = entry.getValue();
/* 214 */             for (Iterator<WurmMail> it2 = mails.iterator(); it2.hasNext(); ) {
/*     */               
/* 216 */               WurmMail newmail = it2.next();
/*     */               
/*     */               try {
/* 219 */                 Item i = Items.getItem(newmail.itemId);
/* 220 */                 ReiceverReturnMails returnSetReceiver = returnsPerReceiver.get(Long.valueOf(newmail.receiver));
/* 221 */                 if (returnSetReceiver == null) {
/*     */                   
/* 223 */                   returnSetReceiver = new ReiceverReturnMails();
/* 224 */                   returnSetReceiver.setReceiverId(newmail.receiver);
/* 225 */                   returnSetReceiver.setServerId(sid.intValue());
/*     */                 } 
/* 227 */                 returnSetReceiver.addMail(newmail, i);
/* 228 */                 Item[] contained = i.getAllItems(true);
/* 229 */                 for (int c = 0; c < contained.length; c++)
/*     */                 {
/* 231 */                   returnSetReceiver.addMail(newmail, contained[c]);
/*     */                 }
/*     */                 
/* 234 */                 returnsPerReceiver.put(Long.valueOf(newmail.receiver), returnSetReceiver);
/*     */               }
/* 236 */               catch (NoSuchItemException nsi) {
/*     */                 
/* 238 */                 logger.log(Level.WARNING, "The item that should be returned is gone!");
/*     */               } 
/*     */             } 
/*     */           } 
/* 242 */           if (!returnsPerReceiver.isEmpty()) {
/*     */             
/* 244 */             boolean problem = false;
/* 245 */             Iterator<Map.Entry<Long, ReiceverReturnMails>> iterator1 = returnsPerReceiver.entrySet().iterator();
/* 246 */             while (iterator1.hasNext()) {
/*     */               
/* 248 */               Map.Entry<Long, ReiceverReturnMails> entry = iterator1.next();
/* 249 */               Long rid = entry.getKey();
/* 250 */               ReiceverReturnMails returnSetReceiver = entry.getValue();
/* 251 */               Item[] items = returnSetReceiver.getReturnItemSetAsArray();
/* 252 */               problem = MailSendConfirmQuestion.sendMailSetToServer(getResponder().getWurmId(), getResponder(), returnSetReceiver
/* 253 */                   .getServerId(), returnSetReceiver.getReturnWurmMailSet(), rid.longValue(), items);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 258 */               if (!problem)
/*     */               {
/* 260 */                 for (int a = 0; a < items.length; a++) {
/*     */                   
/* 262 */                   Item[] contained = items[a].getAllItems(true);
/* 263 */                   for (int c = 0; c < contained.length; c++)
/*     */                   {
/* 265 */                     Items.destroyItem(contained[c].getWurmId());
/*     */                   }
/* 267 */                   Items.destroyItem(items[a].getWurmId());
/* 268 */                   WurmMail.removeMail(items[a].getWurmId());
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 276 */       long money = getResponder().getMoney();
/* 277 */       if (fullcost > money) {
/*     */         
/* 279 */         Change change = new Change(fullcost - money);
/* 280 */         getResponder().getCommunicator().sendNormalServerMessage("You need " + change
/* 281 */             .getChangeString() + " in order to receive the selected items.");
/*     */ 
/*     */       
/*     */       }
/* 285 */       else if (fullcost > 0L) {
/*     */         
/* 287 */         LoginServerWebConnection lsw = new LoginServerWebConnection();
/*     */ 
/*     */         
/*     */         try {
/* 291 */           if (getResponder().chargeMoney(fullcost))
/*     */           {
/* 293 */             for (Iterator<Item> iterator = itemset.iterator(); iterator.hasNext(); ) {
/*     */               
/* 295 */               Item item = iterator.next();
/* 296 */               Item[] contained = item.getAllItems(true);
/* 297 */               for (int c = 0; c < contained.length; c++) {
/*     */                 
/* 299 */                 contained[c].setMailed(false);
/* 300 */                 contained[c].setLastOwnerId(getResponder().getWurmId());
/*     */               } 
/* 302 */               WurmMail.removeMail(item.getWurmId());
/* 303 */               this.mbox.insertItem(item, true);
/* 304 */               item.setLastOwnerId(getResponder().getWurmId());
/* 305 */               item.setMailed(false);
/* 306 */               logger.log(Level.INFO, 
/* 307 */                   getResponder().getName() + " received " + item.getName() + " " + item.getWurmId());
/*     */             } 
/* 309 */             Change change = new Change(fullcost);
/* 310 */             getResponder().getCommunicator().sendNormalServerMessage("The items are now available and you have been charged " + change
/* 311 */                 .getChangeString() + ".");
/*     */             
/* 313 */             int xx = 0;
/* 314 */             for (Iterator<Map.Entry<Long, Long>> iterator1 = moneyToSend.entrySet().iterator(); iterator1.hasNext(); )
/*     */             {
/* 316 */               xx++;
/* 317 */               Map.Entry<Long, Long> entry = iterator1.next();
/* 318 */               PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(((Long)entry.getKey()).longValue());
/* 319 */               if (pinf != null) {
/*     */                 
/* 321 */                 if (((Long)entry.getValue()).longValue() > 0L) {
/*     */                   
/* 323 */                   logger.log(Level.INFO, 
/* 324 */                       getResponder().getName() + " adding COD " + ((Long)entry.getValue()).longValue() + " to " + pinf
/*     */                       
/* 326 */                       .getName() + " via server " + lsw
/* 327 */                       .getServerId());
/* 328 */                   lsw.addMoney(pinf.wurmId, pinf.getName(), ((Long)entry.getValue()).longValue(), "Mail " + 
/*     */                       
/* 330 */                       getResponder().getName() + 
/* 331 */                       DateFormat.getInstance().format(new Date()).replace(" ", "") + xx);
/*     */                 } 
/*     */                 
/*     */                 continue;
/*     */               } 
/* 336 */               if (((Long)entry.getValue()).longValue() > 0L) {
/*     */                 
/* 338 */                 logger.log(Level.INFO, "Adding COD " + ((Long)entry.getValue()).longValue() + " to " + ((Long)entry
/* 339 */                     .getKey()).longValue() + " (no name) via server " + lsw.getServerId());
/* 340 */                 lsw.addMoney(((Long)entry.getKey()).longValue(), null, ((Long)entry.getValue()).longValue(), "Mail " + 
/*     */                     
/* 342 */                     getResponder().getName() + 
/* 343 */                     DateFormat.getInstance().format(new Date()).replace(" ", "") + xx);
/*     */                 
/*     */                 continue;
/*     */               } 
/* 347 */               getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the receiver of some money.");
/*     */               
/* 349 */               logger.log(Level.WARNING, "failed to locate receiver " + ((Long)entry
/* 350 */                   .getKey()).longValue() + " of amount " + ((Long)entry
/* 351 */                   .getValue()).longValue() + " from " + getResponder().getName() + ".");
/*     */             
/*     */             }
/*     */           
/*     */           }
/*     */           else
/*     */           {
/* 358 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to charge you the money. The bank may not be available. No mail received.");
/*     */           }
/*     */         
/* 361 */         } catch (IOException iox) {
/*     */           
/* 363 */           getResponder().getCommunicator().sendNormalServerMessage("Failed to charge you the money. The bank may not be available. No mail received.");
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 369 */         for (Iterator<Item> iterator = itemset.iterator(); iterator.hasNext(); ) {
/*     */           
/* 371 */           Item item = iterator.next();
/* 372 */           Item[] contained = item.getAllItems(true);
/* 373 */           for (int c = 0; c < contained.length; c++) {
/*     */             
/* 375 */             contained[c].setMailed(false);
/* 376 */             contained[c].setLastOwnerId(getResponder().getWurmId());
/*     */           } 
/* 378 */           WurmMail.removeMail(item.getWurmId());
/* 379 */           this.mbox.insertItem(item, true);
/* 380 */           item.setLastOwnerId(getResponder().getWurmId());
/* 381 */           item.setMailed(false);
/*     */         } 
/* 383 */         if (itemset.size() > 0)
/* 384 */           getResponder().getCommunicator().sendNormalServerMessage("The items are now available in the " + this.mbox
/* 385 */               .getName() + "."); 
/* 386 */         if (toReturn != null && toReturn.size() > 0) {
/* 387 */           getResponder().getCommunicator().sendNormalServerMessage("The spirits will return the unwanted items.");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 397 */     String lHtml = "border{scroll{vertical='true';horizontal='false';varray{rescale='true';passthrough{id='id';text='" + getId() + "'}";
/* 398 */     StringBuilder buf = new StringBuilder(lHtml);
/* 399 */     if (!this.mbox.isEmpty(false)) {
/*     */       
/* 401 */       buf.append("label{text=\"Empty the mailbox first.\"}");
/* 402 */       buf.append("text{text=\"\"};}null;varray{");
/*     */     }
/*     */     else {
/*     */       
/* 406 */       this.mailset = WurmMail.getSentMailsFor(getResponder().getWurmId(), 100);
/* 407 */       if (this.mailset.isEmpty()) {
/*     */         
/* 409 */         buf.append("text{text='You have no pending mail.'}");
/* 410 */         buf.append("text{text=\"\"};}null;");
/*     */       }
/*     */       else {
/*     */         
/* 414 */         buf.append("text{text='Use the checkboxes to select which items you wish to receive in your mailbox, and which to return to the sender.'}");
/* 415 */         buf.append("text{text='If an item has a Cash On Delivery (C.O.D) cost, you have to have that money in the bank.'}");
/* 416 */         long money = getResponder().getMoney();
/* 417 */         if (money <= 0L) {
/* 418 */           buf.append("text{text='You have no money in the bank.'}");
/*     */         } else {
/* 420 */           buf.append("text{text='You have " + (new Change(money)).getChangeString() + " in the bank.'}");
/*     */         } 
/* 422 */         buf.append("}};null;");
/*     */         
/* 424 */         int rowNumb = 0;
/* 425 */         buf.append("tree{id=\"t1\";cols=\"10\";showheader=\"true\";height=\"300\"col{text=\"QL\";width=\"45\"};col{text=\"DAM\";width=\"45\"};col{text=\"Receive\";width=\"50\"};col{text=\"Return\";width=\"50\"};col{text=\"G\";width=\"25\"};col{text=\"S\";width=\"25\"};col{text=\"C\";width=\"25\"};col{text=\"I\";width=\"25\"};col{text=\"Sender\";width=\"75\"};col{text=\"Expiry\";width=\"220\"};");
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
/* 437 */         for (Iterator<WurmMail> it = this.mailset.iterator(); it.hasNext(); ) {
/*     */           
/* 439 */           WurmMail m = it.next();
/*     */           
/*     */           try {
/* 442 */             rowNumb++;
/* 443 */             Item item = Items.getItem(m.itemId);
/* 444 */             buf.append(addItem("" + rowNumb, item, m, true));
/*     */           }
/* 446 */           catch (NoSuchItemException e) {
/*     */             
/* 448 */             buf.append("row{id=\"e" + rowNumb + "\";hover=\"Item gone.\";name=\"Item gone.\";rarity=\"0\";children=\"0\";col{text=\"n/a\"};col{text=\"n/a\"};col{text=\"n/a\"};col{text=\"n/a\"};col{text=\"\"};col{text=\"\"};col{text=\"\"};col{text=\"\"};col{text=\"n/a\"};col{text=\"n/a\"}}");
/*     */           } 
/*     */         } 
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
/* 463 */         buf.append("}");
/* 464 */         buf.append("null;varray{");
/* 465 */         if (this.mailset.size() < 100) {
/* 466 */           buf.append("label{text='You have no more mail.'}");
/*     */         } else {
/* 468 */           buf.append("text{text='You may have more mail than these. Manage these then check again.'}");
/*     */         } 
/*     */       } 
/* 471 */     }  buf.append("harray{button{text=\"Send\";id=\"submit\"}}text=\"\"}}");
/* 472 */     getResponder().getCommunicator().sendBml(700, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String addItem(String id, Item item, WurmMail m, boolean isTopLevel) {
/* 477 */     StringBuilder buf = new StringBuilder();
/* 478 */     Change change = null;
/* 479 */     float priceMod = 1.0F;
/* 480 */     int pcost = 0;
/* 481 */     long fullcost = 0L;
/*     */     
/* 483 */     Item[] contained = item.getItemsAsArray();
/* 484 */     int children = contained.length;
/*     */ 
/*     */ 
/*     */     
/* 488 */     if (m.rejected && isTopLevel) {
/*     */       
/* 490 */       pcost = 100;
/* 491 */       if (item.getTemplateId() == 748 || item.getTemplateId() == 1272) {
/*     */         
/* 493 */         InscriptionData insData = item.getInscription();
/* 494 */         if (insData != null)
/*     */         {
/* 496 */           if (insData.hasBeenInscribed())
/* 497 */             pcost = 1; 
/*     */         }
/*     */       } 
/* 500 */       change = new Change(pcost);
/*     */     }
/* 502 */     else if (m.price > 0L && isTopLevel) {
/*     */       
/* 504 */       fullcost = MailSendConfirmQuestion.getCostForItem(item, priceMod);
/* 505 */       fullcost += m.price;
/* 506 */       for (int i = 0; i < contained.length; i++) {
/*     */         
/* 508 */         pcost = MailSendConfirmQuestion.getCostForItem(contained[i], priceMod);
/* 509 */         fullcost += pcost;
/*     */       } 
/* 511 */       change = new Change(fullcost);
/*     */     } 
/*     */     
/* 514 */     String itemName = longItemName(item);
/* 515 */     String sQL = String.format("%.2f", new Object[] { Float.valueOf(item.getQualityLevel()) });
/* 516 */     String sDMG = String.format("%.2f", new Object[] { Float.valueOf(item.getDamage()) });
/* 517 */     String receive = "text=\"\"";
/* 518 */     String ret = "text=\"\"";
/* 519 */     String gold = "";
/* 520 */     String silver = "";
/* 521 */     String copper = "";
/* 522 */     String iron = "";
/* 523 */     String sender = "";
/* 524 */     String expire = "";
/*     */     
/* 526 */     if (isTopLevel) {
/*     */       
/* 528 */       receive = "checkbox=\"true\";id=\"" + id + "receive\"";
/* 529 */       if (m.rejected) {
/* 530 */         ret = "text=\"n/a\"";
/*     */       } else {
/* 532 */         ret = "checkbox=\"true\";id=\"" + id + "return\"";
/* 533 */       }  if (change != null) {
/*     */         
/* 535 */         gold = "" + change.getGoldCoins();
/* 536 */         silver = "" + change.getSilverCoins();
/* 537 */         copper = "" + change.getCopperCoins();
/* 538 */         iron = "" + change.getIronCoins();
/*     */       } 
/* 540 */       PlayerState ps = PlayerInfoFactory.getPlayerState(m.getSender());
/* 541 */       sender = (ps != null) ? ps.getPlayerName() : "Unknown";
/* 542 */       expire = "" + Server.getTimeFor(Math.max(0L, m.expiration - System.currentTimeMillis()));
/*     */     } 
/*     */     
/* 545 */     String spells = "";
/* 546 */     ItemSpellEffects eff = item.getSpellEffects();
/* 547 */     if (eff != null) {
/*     */       
/* 549 */       SpellEffect[] speffs = eff.getEffects();
/* 550 */       for (int z = 0; z < speffs.length; z++) {
/*     */         
/* 552 */         if (spells.length() > 0)
/* 553 */           spells = spells + ","; 
/* 554 */         spells = spells + speffs[z].getName() + " [" + (int)(speffs[z]).power + "]";
/*     */       } 
/*     */     } 
/* 557 */     String extra = "";
/* 558 */     if (item.getColor() != -1)
/* 559 */       extra = " [" + WurmColor.getRGBDescription(item.getColor()) + "]"; 
/* 560 */     if (item.getTemplateId() == 866) {
/*     */       
/*     */       try {
/*     */         
/* 564 */         extra = extra + " [" + CreatureTemplateFactory.getInstance().getTemplate(item.getData2()).getName() + "]";
/*     */       }
/* 566 */       catch (NoSuchCreatureTemplateException e) {
/*     */         
/* 568 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */       } 
/*     */     }
/* 571 */     if (spells.length() == 0)
/* 572 */       spells = "no enchants"; 
/* 573 */     String hover = itemName + " - " + spells + extra;
/* 574 */     buf.append("row{id=\"" + id + "\";hover=\"" + hover + "\";name=\"" + itemName + "\";rarity=\"" + item
/* 575 */         .getRarity() + "\";children=\"" + children + "\";col{text=\"" + sQL + "\"};col{text=\"" + sDMG + "\"};col{" + receive + "};col{" + ret + "};col{text=\"" + gold + "\"};col{text=\"" + silver + "\"};col{text=\"" + copper + "\"};col{text=\"" + iron + "\"};col{text=\"" + sender + "\"};col{text=\"" + expire
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 585 */         .replace(",", " ") + "\"}}");
/*     */     
/* 587 */     for (int c = 0; c < contained.length; c++)
/*     */     {
/* 589 */       buf.append(addItem(id + "c" + c, contained[c], m, false));
/*     */     }
/* 591 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String longItemName(Item litem) {
/* 599 */     StringBuilder sb = new StringBuilder();
/* 600 */     if (litem.getRarity() == 1) {
/* 601 */       sb.append("rare ");
/* 602 */     } else if (litem.getRarity() == 2) {
/* 603 */       sb.append("supreme ");
/* 604 */     } else if (litem.getRarity() == 3) {
/* 605 */       sb.append("fantastic ");
/* 606 */     }  String name = (litem.getName().length() == 0) ? litem.getTemplate().getName() : litem.getName();
/* 607 */     MaterialUtilities.appendNameWithMaterialSuffix(sb, name.replace("\"", "''"), litem.getMaterial());
/*     */     
/* 609 */     if (litem.getDescription().length() > 0)
/* 610 */       sb.append(" (" + litem.getDescription() + ")"); 
/* 611 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MailReceiveQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */