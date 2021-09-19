/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.intra.PlayerTransfer;
/*     */ import com.wurmonline.server.items.InscriptionData;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.WurmMail;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class MailSendConfirmQuestion
/*     */   extends Question
/*     */   implements MonetaryConstants, MiscConstants, TimeConstants
/*     */ {
/*  52 */   private static final Logger logger = Logger.getLogger(MailSendConfirmQuestion.class.getName());
/*     */   
/*     */   private final Item mailbox;
/*     */   
/*     */   private final Item[] items;
/*     */   
/*     */   private final String receiver;
/*     */   
/*     */   private final boolean[] cods;
/*  61 */   private long fullprice = 0L;
/*     */   
/*     */   private final long receiverId;
/*     */   
/*     */   private final int targetServer;
/*     */   
/*     */   MailSendConfirmQuestion(Creature aResponder, String aTitle, String aQuestion, Item aMailbox, Item[] aItems, boolean[] aCods, String aReceiver, long[] aReceiverInfo) {
/*  68 */     super(aResponder, aTitle, aQuestion, 55, aMailbox.getWurmId());
/*  69 */     this.mailbox = aMailbox;
/*  70 */     this.items = aItems;
/*  71 */     this.receiver = LoginHandler.raiseFirstLetter(aReceiver);
/*  72 */     this.cods = aCods;
/*  73 */     this.receiverId = aReceiverInfo[1];
/*  74 */     this.targetServer = (int)aReceiverInfo[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  80 */     if (!MailSendQuestion.validateMailboxContents(this.items, this.mailbox)) {
/*     */       
/*  82 */       getResponder().getCommunicator().sendNormalServerMessage("The items in the mailbox have changed. Please try sending again.");
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     if (getResponder().getMoney() < this.fullprice) {
/*  87 */       getResponder().getCommunicator().sendNormalServerMessage("You can not afford sending the packages.");
/*  88 */     } else if (!Servers.loginServer.isAvailable(5, true)) {
/*     */       
/*  90 */       getResponder().getCommunicator().sendNormalServerMessage("You may not send mail right now. The service is unavailable.");
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  95 */       boolean charge = false;
/*  96 */       boolean revert = false;
/*     */       
/*  98 */       int codprice = 0;
/*  99 */       boolean local = (Servers.localServer.id == this.targetServer);
/* 100 */       WurmMail mail = null;
/* 101 */       int revertx = 0;
/* 102 */       Set<WurmMail> mails = null;
/* 103 */       Set<Item> mailitems = null;
/* 104 */       if (!local) {
/*     */         
/* 106 */         boolean changingCluster = false;
/* 107 */         ServerEntry entry = Servers.getServerWithId(this.targetServer);
/* 108 */         if (entry == null) {
/*     */           
/* 110 */           getResponder().getCommunicator().sendNormalServerMessage("You can not mail that far.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 122 */         if (changingCluster) {
/*     */           
/* 124 */           getResponder().getCommunicator().sendNormalServerMessage("You can not mail that far.");
/*     */           return;
/*     */         } 
/* 127 */         if (!entry.isConnected()) {
/*     */           
/* 129 */           getResponder().getCommunicator().sendNormalServerMessage("That island is not available currently. Please try again later.");
/*     */           return;
/*     */         } 
/* 132 */         mails = new HashSet<>();
/* 133 */         mailitems = new HashSet<>();
/*     */       } 
/* 135 */       long timeavail = System.currentTimeMillis() + (101 - (int)this.mailbox.getSpellCourierBonus()) * 60000L;
/* 136 */       if (getResponder().getPower() > 0) {
/* 137 */         timeavail = System.currentTimeMillis() + 60000L;
/*     */       }
/* 139 */       Item realItem = null; int x;
/* 140 */       for (x = 0; x < this.items.length; x++) {
/*     */ 
/*     */         
/*     */         try {
/* 144 */           realItem = Items.getItem(this.items[x].getWurmId());
/* 145 */           if (this.items[x].getTemplateId() == 651) {
/*     */             
/* 147 */             getResponder().getCommunicator().sendNormalServerMessage("The gift boxes are not handled by the mail service.");
/*     */             
/*     */             return;
/*     */           } 
/* 151 */           if (this.items[x].isCoin()) {
/*     */             
/* 153 */             getResponder().getCommunicator().sendNormalServerMessage("Coins are currently not handled by the mail service.");
/*     */             
/*     */             return;
/*     */           } 
/* 157 */           if (this.items[x].isBanked()) {
/*     */             
/* 159 */             getResponder().getCommunicator().sendNormalServerMessage("The " + this.items[x]
/* 160 */                 .getName() + " is currently unavailable.");
/*     */             return;
/*     */           } 
/* 163 */           if (this.items[x].isUnfinished()) {
/*     */             
/* 165 */             getResponder().getCommunicator().sendNormalServerMessage("Unfinished items would be broken by the mail service.");
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 170 */           if ((this.items[x]
/* 171 */             .getTemplateId() == 665 && this.items[x].isLocked()) || (this.items[x]
/* 172 */             .getTemplateId() == 192 && this.items[x].isLocked())) {
/*     */ 
/*     */             
/* 175 */             getResponder().getCommunicator().sendNormalServerMessage(this.items[x]
/* 176 */                 .getNameWithGenus() + " cannot be mailed while locked.");
/*     */             
/*     */             return;
/*     */           } 
/* 180 */           codprice = 0;
/* 181 */           String key = "";
/* 182 */           String val = "";
/* 183 */           if (this.cods[x]) {
/*     */             
/* 185 */             key = x + "g";
/* 186 */             val = answers.getProperty(key);
/* 187 */             if (val != null && val.length() > 0) {
/*     */               
/*     */               try {
/*     */                 
/* 191 */                 codprice = Integer.parseInt(val) * 1000000;
/*     */               }
/* 193 */               catch (NumberFormatException nfe) {
/*     */                 
/* 195 */                 getResponder().getCommunicator().sendNormalServerMessage("Failed to set the gold price for " + realItem
/* 196 */                     .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*     */               } 
/*     */             }
/*     */             
/* 200 */             key = x + "s";
/* 201 */             val = answers.getProperty(key);
/* 202 */             if (val != null && val.length() > 0) {
/*     */               
/*     */               try {
/*     */                 
/* 206 */                 codprice += Integer.parseInt(val) * 10000;
/*     */               }
/* 208 */               catch (NumberFormatException nfe) {
/*     */                 
/* 210 */                 getResponder().getCommunicator().sendNormalServerMessage("Failed to set a silver price for " + realItem
/* 211 */                     .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*     */               } 
/*     */             }
/*     */             
/* 215 */             key = x + "c";
/* 216 */             val = answers.getProperty(key);
/* 217 */             if (val != null && val.length() > 0) {
/*     */               
/*     */               try {
/*     */                 
/* 221 */                 codprice += Integer.parseInt(val) * 100;
/*     */               }
/* 223 */               catch (NumberFormatException nfe) {
/*     */                 
/* 225 */                 getResponder().getCommunicator().sendNormalServerMessage("Failed to set a copper price for " + realItem
/* 226 */                     .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*     */               } 
/*     */             }
/*     */             
/* 230 */             key = x + "i";
/* 231 */             val = answers.getProperty(key);
/* 232 */             if (val != null && val.length() > 0) {
/*     */               
/*     */               try {
/*     */                 
/* 236 */                 codprice += Integer.parseInt(val);
/*     */               }
/* 238 */               catch (NumberFormatException nfe) {
/*     */                 
/* 240 */                 getResponder().getCommunicator().sendNormalServerMessage("Failed to set an iron price for " + realItem
/* 241 */                     .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*     */               } 
/*     */             }
/*     */             
/* 245 */             if (codprice <= 0) {
/*     */               
/* 247 */               codprice = 1;
/*     */               
/* 249 */               getResponder().getCommunicator().sendNormalServerMessage("Cod price set to 1 iron, since it was negative or zero.");
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 254 */             mail = new WurmMail((byte)1, realItem.getWurmId(), getResponder().getWurmId(), this.receiverId, codprice, timeavail, System.currentTimeMillis() + (Servers.localServer.testServer ? 3600000L : 604800000L) * 2L, Servers.localServer.id, false, false);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 259 */             charge = true;
/*     */             
/* 261 */             mail = new WurmMail((byte)0, realItem.getWurmId(), getResponder().getWurmId(), this.receiverId, codprice, timeavail, System.currentTimeMillis() + (Servers.localServer.testServer ? 3600000L : 604800000L) * 2L, Servers.localServer.id, false, false);
/*     */           } 
/*     */ 
/*     */           
/* 265 */           if (local) {
/*     */             
/* 267 */             WurmMail.addWurmMail(mail);
/* 268 */             mail.createInDatabase();
/*     */           } else {
/*     */             
/* 271 */             mails.add(mail);
/*     */           } 
/*     */           
/* 274 */           if (realItem.getParentId() == this.mailbox.getWurmId()) {
/*     */             
/* 276 */             realItem.putInVoid();
/* 277 */             realItem.setMailed(true);
/* 278 */             realItem.setMailTimes((byte)(realItem.getMailTimes() + 1));
/* 279 */             Item[] contained = realItem.getAllItems(true);
/* 280 */             for (int c = 0; c < contained.length; c++) {
/*     */               
/* 282 */               contained[c].setMailed(true);
/* 283 */               contained[c].setMailTimes((byte)(contained[c].getMailTimes() + 1));
/* 284 */               if (!local)
/* 285 */                 mailitems.add(contained[c]); 
/*     */             } 
/* 287 */             if (!local)
/* 288 */               mailitems.add(realItem); 
/* 289 */             revertx = x;
/*     */           }
/*     */           else {
/*     */             
/* 293 */             revert = true;
/* 294 */             getResponder().getCommunicator().sendAlertServerMessage("The " + realItem
/* 295 */                 .getName() + " is no longer in the mailbox!");
/*     */             
/*     */             break;
/*     */           } 
/* 299 */         } catch (NoSuchItemException nsi) {
/*     */           
/* 301 */           revert = true;
/* 302 */           getResponder().getCommunicator().sendAlertServerMessage("The " + this.items[x]
/* 303 */               .getName() + " is no longer in the mailbox!");
/*     */           break;
/*     */         } 
/*     */       } 
/* 307 */       if (!local)
/*     */       {
/* 309 */         revert = sendMailSetToServer(getResponder().getWurmId(), getResponder(), this.targetServer, mails, this.receiverId, mailitems
/* 310 */             .<Item>toArray(new Item[mailitems.size()]));
/*     */       }
/* 312 */       if (revert)
/*     */       {
/* 314 */         charge = false;
/*     */       }
/* 316 */       if (charge) {
/*     */         
/* 318 */         LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 319 */         long newBalance = lsw.chargeMoney(getResponder().getName(), this.fullprice);
/* 320 */         if (newBalance < 0L) {
/*     */ 
/*     */ 
/*     */           
/* 324 */           getResponder().getCommunicator().sendAlertServerMessage("The spirits seem to deliver for free this time.");
/* 325 */           logger.log(Level.WARNING, "Failed to withdraw " + this.fullprice + " iron from " + getResponder().getName() + ". Mail was free.");
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */ 
/*     */             
/* 332 */             getResponder().setMoney(newBalance);
/*     */           }
/* 334 */           catch (IOException iox) {
/*     */             
/* 336 */             logger.log(Level.WARNING, getResponder().getName() + " " + iox.getMessage(), iox);
/*     */           } 
/* 338 */           getResponder().getCommunicator().sendNormalServerMessage("You have been charged " + (new Change(this.fullprice))
/* 339 */               .getChangeString() + ".");
/*     */         } 
/*     */       } 
/* 342 */       if (revert) {
/*     */         
/* 344 */         for (x = 0; x < revertx + 1; x++)
/*     */         {
/*     */           
/*     */           try {
/* 348 */             realItem = Items.getItem(this.items[x].getWurmId());
/* 349 */             if (realItem.getParentId() != this.mailbox.getWurmId())
/*     */             {
/* 351 */               WurmMail.removeMail(this.items[x].getWurmId());
/* 352 */               realItem.setMailed(false);
/* 353 */               realItem.setMailTimes((byte)(realItem.getMailTimes() - 1));
/* 354 */               Item[] contained = realItem.getAllItems(true);
/* 355 */               for (int c = 0; c < contained.length; c++)
/*     */               {
/* 357 */                 contained[c].setMailed(false);
/* 358 */                 contained[c].setMailTimes((byte)(contained[c].getMailTimes() - 1));
/* 359 */                 contained[c].setLastOwnerId(getResponder().getWurmId());
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 365 */           catch (NoSuchItemException noSuchItemException) {}
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 372 */         String time = "just under two hours.";
/* 373 */         float bon = this.mailbox.getSpellCourierBonus();
/* 374 */         if (bon > 90.0F) {
/* 375 */           time = "less than ten minutes.";
/* 376 */         } else if (bon > 70.0F) {
/* 377 */           time = "less than thirty minutes.";
/* 378 */         } else if (bon > 40.0F) {
/* 379 */           time = "less than an hour.";
/* 380 */         } else if (bon > 10.0F) {
/* 381 */           time = "a bit more than an hour.";
/* 382 */         }  getResponder().getCommunicator()
/* 383 */           .sendNormalServerMessage("The items silently disappear from the " + this.mailbox
/* 384 */             .getName() + ". You expect them to arrive in " + time);
/*     */         
/* 386 */         if (!local)
/*     */         {
/* 388 */           for (int i = 0; i < this.items.length; i++) {
/*     */             
/* 390 */             Item[] contained = this.items[i].getAllItems(true);
/* 391 */             for (int c = 0; c < contained.length; c++) {
/*     */               
/* 393 */               logger.log(Level.INFO, 
/* 394 */                   getResponder().getName() + " destroying contained " + contained[c].getName() + ", ql " + contained[c]
/* 395 */                   .getQualityLevel() + " wid=" + contained[c].getWurmId());
/* 396 */               Items.destroyItem(contained[c].getWurmId(), true, true);
/*     */             } 
/* 398 */             logger.log(Level.INFO, 
/*     */                 
/* 400 */                 getResponder().getName() + " destroying " + this.items[i].getName() + ", ql " + this.items[i]
/* 401 */                 .getQualityLevel() + " wid=" + this.items[i].getWurmId());
/* 402 */             Items.destroyItem(this.items[i].getWurmId(), true, true);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean sendMailSetToServer(long senderId, @Nullable Creature responder, int targetServer, Set<WurmMail> mails, long receiverId, Item[] items) {
/* 414 */     boolean revert = false;
/* 415 */     LoginServerWebConnection lsw = null;
/* 416 */     ServerEntry entry = Servers.getServerWithId(targetServer);
/* 417 */     if (entry != null) {
/*     */       
/* 419 */       if (entry.isAvailable(5, true)) {
/* 420 */         lsw = new LoginServerWebConnection(targetServer);
/*     */       } else {
/*     */         
/* 423 */         if (responder != null) {
/* 424 */           responder.getCommunicator().sendNormalServerMessage("The inter-island mail service is on strike right now. Please try later.");
/*     */         }
/* 426 */         revert = true;
/*     */       } 
/*     */     } else {
/*     */       
/* 430 */       lsw = new LoginServerWebConnection();
/* 431 */     }  if (!revert) {
/*     */       
/* 433 */       WurmMail[] mailarr = mails.<WurmMail>toArray(new WurmMail[mails.size()]);
/* 434 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 435 */       ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
/*     */       
/*     */       try {
/* 438 */         DataOutputStream dos = new DataOutputStream(bos);
/* 439 */         DataOutputStream dos2 = new DataOutputStream(bos2);
/* 440 */         dos.writeInt(items.length);
/*     */         int x;
/* 442 */         for (x = 0; x < items.length; x++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 449 */           if (responder != null) {
/* 450 */             logger.log(Level.INFO, responder
/* 451 */                 .getName() + " sending " + items[x].getName() + ", ql " + items[x].getQualityLevel() + " wid=" + items[x]
/* 452 */                 .getWurmId() + " to " + ((entry != null) ? entry
/* 453 */                 .getName() : (String)Integer.valueOf(targetServer)));
/*     */           } else {
/* 455 */             logger.log(Level.INFO, senderId + " sending " + items[x]
/* 456 */                 .getName() + ", ql " + items[x].getQualityLevel() + " wid=" + items[x]
/* 457 */                 .getWurmId() + " to " + ((entry != null) ? entry
/* 458 */                 .getName() : (String)Integer.valueOf(targetServer)));
/* 459 */           }  PlayerTransfer.sendItem(items[x], dos, false);
/*     */         } 
/* 461 */         dos.flush();
/* 462 */         dos.close();
/* 463 */         dos2.writeInt(mailarr.length);
/* 464 */         for (x = 0; x < mailarr.length; x++) {
/*     */           
/* 466 */           dos2.writeByte((mailarr[x]).type);
/* 467 */           dos2.writeLong((mailarr[x]).itemId);
/* 468 */           dos2.writeLong((mailarr[x]).sender);
/* 469 */           dos2.writeLong((mailarr[x]).receiver);
/* 470 */           dos2.writeLong((mailarr[x]).price);
/* 471 */           dos2.writeLong((mailarr[x]).sent);
/* 472 */           dos2.writeLong((mailarr[x]).expiration);
/* 473 */           dos2.writeInt((mailarr[x]).sourceserver);
/* 474 */           dos2.writeBoolean((mailarr[x]).rejected);
/*     */         } 
/* 476 */         dos2.flush();
/* 477 */         dos2.close();
/*     */       }
/* 479 */       catch (Exception ex) {
/*     */         
/* 481 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */       } 
/* 483 */       byte[] itemdata = bos.toByteArray();
/* 484 */       byte[] maildata = bos2.toByteArray();
/* 485 */       String result = lsw.sendMail(maildata, itemdata, senderId, receiverId, targetServer);
/* 486 */       if (result.length() != 0) {
/*     */         
/* 488 */         revert = true;
/* 489 */         if (responder != null) {
/*     */           
/* 491 */           responder.getCommunicator().sendAlertServerMessage("The spirits in the mailbox reported a problem when sending the contents of the mailbox. Reverting. The message was: " + result);
/*     */ 
/*     */           
/* 494 */           logger.log(Level.WARNING, responder.getName() + ", " + result);
/*     */         } 
/*     */       } 
/*     */     } 
/* 498 */     return revert;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getCostForItem(Item i, float priceMod) {
/* 503 */     if (i.isComponentItem())
/* 504 */       return 0; 
/* 505 */     if (i.getTemplateId() == 1392)
/* 506 */       return 0; 
/* 507 */     int pcost = 10;
/* 508 */     int combinePriceMod = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 515 */     if (i.getTemplateId() == 748 || i.getTemplateId() == 1272 || i.getTemplateId() == 1403) {
/*     */       
/* 517 */       InscriptionData insData = i.getInscription();
/* 518 */       if (insData != null) {
/*     */         
/* 520 */         if (insData.hasBeenInscribed()) {
/* 521 */           return 1;
/*     */         }
/*     */       } else {
/*     */         
/* 525 */         Item parent = i.getParentOrNull();
/* 526 */         if (parent != null)
/*     */         {
/* 528 */           if (parent.getTemplateId() == 1409 || parent.getTemplateId() == 1404 || parent
/* 529 */             .getTemplateId() == 1127 || parent.getTemplateId() == 1128) {
/* 530 */             return 1;
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/* 535 */     if (i.isCombine() || i.isLiquid()) {
/* 536 */       combinePriceMod = Math.max(1, i.getWeightGrams() / 5000);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 545 */     pcost *= combinePriceMod;
/* 546 */     pcost = (int)(pcost * priceMod);
/* 547 */     return pcost * 1 * 10;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 553 */     float priceMod = 1.0F;
/*     */     
/* 555 */     String lHtml = getBmlHeader();
/* 556 */     StringBuilder buf = new StringBuilder(lHtml);
/* 557 */     buf.append("text{text='This should give you an overview of how much the cost will be for sending the contents of the mailbox, visible on the bottom of this dialogue.'}");
/* 558 */     buf.append("text{text='C.O.D means Cash On Delivery, which is good for selling items to other players. A C.O.D item costs nothing for you to send.'}");
/* 559 */     buf.append("text{text='The Mail cost is what the spirits charge to deliver the item. The C.O.D cost payed by the receiver will be what you enter plus the Mail cost.'}");
/* 560 */     buf.append("text{text='Example: You check the checkbox for a Mallet which say has a 1 copper coins Mail cost.'}");
/* 561 */     buf.append("text{text='You enter 20 in the C.O.D copper coins textbox. This means the receiver will have to pay 21 copper in all in order to receive the mallet of which you receive 20.'}");
/*     */     
/* 563 */     buf.append("text{type='italic';text='Note that if a C.O.D receiver returns the item you have to pay a 1 copper (or 1 iron for paper/papryus) fee to retrieve it.'}");
/* 564 */     buf.append("text{text='If the item is rejected you have two weeks to pick it up or it will be destroyed by the spirits since it conflicts with their banking policy.'}");
/* 565 */     buf.append("text{text=''};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 575 */     this.fullprice = 0L;
/*     */     
/* 577 */     for (int x = 0; x < this.items.length; x++) {
/*     */       
/* 579 */       if (!this.cods[x]) {
/*     */         
/* 581 */         int pcost = getCostForItem(this.items[x], priceMod);
/*     */         
/* 583 */         this.fullprice += pcost;
/* 584 */         Item[] contained = this.items[x].getAllItems(true);
/* 585 */         for (int c = 0; c < contained.length; c++) {
/*     */           
/* 587 */           pcost = getCostForItem(contained[c], priceMod);
/* 588 */           this.fullprice += pcost;
/*     */         } 
/*     */       } 
/*     */     } 
/* 592 */     Change change = new Change(this.fullprice);
/* 593 */     buf.append("text{type='bold';text=\"The cost for sending these items will be " + change.getChangeString() + ".\"};");
/* 594 */     if (getResponder().getMoney() < this.fullprice) {
/* 595 */       buf.append("text{type='bold';text=\"You can not afford that. You need to add some money to your bank account.\"};");
/*     */     } else {
/*     */       
/* 598 */       int pcost = 0;
/* 599 */       buf.append("table{rows='" + (this.items.length + 1) + "'; cols='9';label{text='Item name'};label{text='QL'};label{text='DAM'};label{text='C.O.D'};label{text='Mail cost'};label{text='Your price in G'};label{text=',S'};label{text=',C'};label{text=',I'};");
/*     */ 
/*     */       
/* 602 */       for (int i = 0; i < this.items.length; i++) {
/*     */         
/* 604 */         buf.append(itemNameWithColorByRarity(this.items[i]));
/* 605 */         buf.append("label{text=\"" + String.format("%.2f", new Object[] { Float.valueOf(this.items[i].getQualityLevel()) }) + "\"};");
/* 606 */         buf.append("label{text=\"" + String.format("%.2f", new Object[] { Float.valueOf(this.items[i].getDamage()) }) + "\"};");
/* 607 */         pcost = getCostForItem(this.items[i], priceMod);
/* 608 */         Item[] contained = this.items[i].getAllItems(true);
/* 609 */         for (int c = 0; c < contained.length; c++)
/*     */         {
/* 611 */           pcost += getCostForItem(contained[c], priceMod);
/*     */         }
/* 613 */         change = new Change(pcost);
/* 614 */         if (this.cods[i]) {
/*     */           
/* 616 */           buf.append("label{text=\"yes\"};");
/* 617 */           buf.append("label{text=\"" + change.getChangeShortString() + "\"};");
/* 618 */           buf.append("input{maxchars='2'; id='" + i + "g';text='0'};");
/* 619 */           buf.append("input{maxchars='2'; id='" + i + "s';text='0'};");
/* 620 */           buf.append("input{maxchars='2'; id='" + i + "c';text='0'};");
/* 621 */           buf.append("input{maxchars='2'; id='" + i + "i';text='0'}");
/*     */         }
/*     */         else {
/*     */           
/* 625 */           buf.append("label{text=\"no\"};");
/* 626 */           buf.append("label{text=\"" + change.getChangeShortString() + "\"};");
/* 627 */           buf.append("label{text='0'};");
/* 628 */           buf.append("label{text='0'};");
/* 629 */           buf.append("label{text='0'};");
/* 630 */           buf.append("label{text='0'};");
/*     */         } 
/*     */       } 
/* 633 */       buf.append("}");
/*     */     } 
/* 635 */     buf.append(createAnswerButton3());
/* 636 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MailSendConfirmQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */