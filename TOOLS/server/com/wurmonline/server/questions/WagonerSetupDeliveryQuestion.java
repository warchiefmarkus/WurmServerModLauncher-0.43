/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.Delivery;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.highways.PathToCalculate;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.players.PlayerState;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.io.IOException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WagonerSetupDeliveryQuestion
/*     */   extends Question
/*     */   implements MonetaryConstants
/*     */ {
/*  60 */   private static final Logger logger = Logger.getLogger(WagonerSetupDeliveryQuestion.class.getName());
/*     */   
/*     */   private static final String red = "color=\"255,127,127\"";
/*     */   
/*  64 */   private static final DecimalFormat df = new DecimalFormat("#0.00");
/*     */   
/*     */   private final Item container;
/*  67 */   private int sortBy = 1;
/*  68 */   private int pageNo = 1;
/*  69 */   private long wagonerId = -10L;
/*  70 */   private long receiverId = -10L;
/*  71 */   private String receiverName = "";
/*  72 */   private String error = "";
/*  73 */   private int crates = 0;
/*     */ 
/*     */   
/*     */   public WagonerSetupDeliveryQuestion(Creature aResponder, Item container) {
/*  77 */     super(aResponder, getTitle(1), getTitle(1), 145, -10L);
/*  78 */     this.container = container;
/*  79 */     container.setIsSealedOverride(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public WagonerSetupDeliveryQuestion(Creature aResponder, Item container, int sortBy, int pageNo, long wagonerId, long receiverId, String receiverName, String error) {
/*  85 */     super(aResponder, getTitle(pageNo), getTitle(pageNo), 145, -10L);
/*  86 */     this.container = container;
/*  87 */     this.sortBy = sortBy;
/*  88 */     this.pageNo = pageNo;
/*  89 */     this.wagonerId = wagonerId;
/*  90 */     this.receiverId = receiverId;
/*  91 */     this.receiverName = receiverName;
/*  92 */     this.error = error;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final String getTitle(int pageNo) {
/*  97 */     return "Set up Delivery page " + pageNo + " of 2";
/*     */   }
/*     */   public void answer(Properties aAnswer) {
/*     */     boolean next;
/*     */     int codprice;
/*     */     WagonerSetupDeliveryQuestion wdq;
/*     */     String val;
/*     */     int senderCost;
/*     */     String sel;
/*     */     int selId;
/*     */     boolean failed;
/* 108 */     setAnswer(aAnswer);
/* 109 */     boolean cancel = getBooleanProp("cancel");
/* 110 */     if (cancel) {
/*     */ 
/*     */       
/* 113 */       getResponder().getCommunicator().sendNormalServerMessage("Set up Delivery Cancelled!");
/* 114 */       this.container.setIsSealedOverride(false);
/*     */       return;
/*     */     } 
/* 117 */     switch (this.pageNo) {
/*     */ 
/*     */       
/*     */       case 1:
/* 121 */         next = getBooleanProp("next");
/*     */         
/* 123 */         if (next) {
/*     */           
/* 125 */           this.error = "";
/*     */           
/* 127 */           String str1 = aAnswer.getProperty("sel");
/* 128 */           long l = Long.parseLong(str1);
/* 129 */           if (l == -10L) {
/*     */             
/* 131 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to do nothing.");
/* 132 */             this.container.setIsSealedOverride(false);
/*     */             
/*     */             return;
/*     */           } 
/* 136 */           Wagoner wagoner = Wagoner.getWagoner(l);
/* 137 */           if (wagoner == null) {
/*     */             
/* 139 */             this.wagonerId = -10L;
/* 140 */             this.error = "Wagoner has vanished!";
/*     */           } else {
/*     */             
/* 143 */             this.wagonerId = wagoner.getWurmId();
/*     */           } 
/* 145 */           String who = aAnswer.getProperty("playername");
/* 146 */           if (who != null && who.length() > 2) {
/*     */             
/* 148 */             if (LoginHandler.containsIllegalCharacters(who)) {
/*     */               
/* 150 */               getResponder().getCommunicator().sendNormalServerMessage("The name of the receiver contains illegal characters. Please check the name.");
/*     */               
/* 152 */               this.error = "Player " + who + " contains illegal characters!";
/*     */             } 
/*     */           } else {
/*     */             
/* 156 */             this.error = "Player name was too short!";
/*     */           } 
/* 158 */           if (this.error.length() == 0) {
/*     */ 
/*     */             
/* 161 */             this.receiverName = LoginHandler.raiseFirstLetter(who);
/* 162 */             PlayerState ps = PlayerInfoFactory.getPlayerState(this.receiverName);
/* 163 */             if (ps == null) {
/* 164 */               this.error = "Player " + this.receiverName + " not found!";
/* 165 */             } else if (ps.getServerId() != Servers.getLocalServerId()) {
/* 166 */               this.error = "Player " + this.receiverName + " is not on this server!";
/*     */             } else {
/* 168 */               this.receiverId = ps.getPlayerId();
/*     */             } 
/* 170 */           }  if (this.receiverId == -10L && this.error.length() == 0)
/*     */           {
/* 172 */             this.error = "Player " + who + " not found!";
/*     */           }
/* 174 */           if (this.error.length() == 0) {
/* 175 */             this.pageNo = 2;
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 180 */           for (String key : getAnswer().stringPropertyNames()) {
/*     */             
/* 182 */             if (key.startsWith("sort")) {
/*     */ 
/*     */               
/* 185 */               String sid = key.substring(4);
/* 186 */               this.sortBy = Integer.parseInt(sid);
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 192 */         wdq = new WagonerSetupDeliveryQuestion(getResponder(), this.container, this.sortBy, this.pageNo, this.wagonerId, this.receiverId, this.receiverName, this.error);
/*     */         
/* 194 */         switch (this.pageNo) {
/*     */ 
/*     */           
/*     */           case 1:
/* 198 */             wdq.sendQuestion();
/*     */             break;
/*     */ 
/*     */           
/*     */           case 2:
/* 203 */             wdq.sendQuestion2();
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 212 */         codprice = 0;
/* 213 */         val = aAnswer.getProperty("g");
/* 214 */         if (val != null && val.length() > 0) {
/*     */           
/*     */           try {
/*     */             
/* 218 */             codprice = Integer.parseInt(val) * 1000000;
/*     */           }
/* 220 */           catch (NumberFormatException nfe) {
/*     */             
/* 222 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to set the gold price for delivery. Note that a coin value is in whole numbers, no decimals.");
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 227 */         val = aAnswer.getProperty("s");
/* 228 */         if (val != null && val.length() > 0) {
/*     */           
/*     */           try {
/*     */             
/* 232 */             codprice += Integer.parseInt(val) * 10000;
/*     */           }
/* 234 */           catch (NumberFormatException nfe) {
/*     */             
/* 236 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to set a silver price for delivery. Note that a coin value is in whole numbers, no decimals.");
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 241 */         val = aAnswer.getProperty("c");
/* 242 */         if (val != null && val.length() > 0) {
/*     */           
/*     */           try {
/*     */             
/* 246 */             codprice += Integer.parseInt(val) * 100;
/*     */           }
/* 248 */           catch (NumberFormatException nfe) {
/*     */             
/* 250 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to set a copper price for delivery. Note that a coin value is in whole numbers, no decimals.");
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 255 */         val = aAnswer.getProperty("i");
/* 256 */         if (val != null && val.length() > 0) {
/*     */           
/*     */           try {
/*     */             
/* 260 */             codprice += Integer.parseInt(val);
/*     */           }
/* 262 */           catch (NumberFormatException nfe) {
/*     */             
/* 264 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to set an iron price for delivery. Note that a coin value is in whole numbers, no decimals.");
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 269 */         senderCost = 0;
/*     */         
/* 271 */         sel = aAnswer.getProperty("fee");
/* 272 */         selId = Integer.parseInt(sel);
/* 273 */         if (selId == 0) {
/*     */ 
/*     */           
/* 276 */           senderCost += this.crates * 100;
/*     */           
/* 278 */           long money = getResponder().getMoney();
/* 279 */           if (money < senderCost) {
/*     */             
/* 281 */             getResponder().getCommunicator().sendNormalServerMessage("You cannot afford to pay for the delivery fee, so its been cancelled.");
/*     */             
/* 283 */             this.container.setIsSealedOverride(false);
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } else {
/* 290 */           codprice += this.crates * 100;
/*     */         } 
/* 292 */         failed = false;
/*     */ 
/*     */         
/* 295 */         if (senderCost > 0) {
/*     */           
/*     */           try {
/*     */             
/* 299 */             if (getResponder().chargeMoney(senderCost))
/*     */             {
/* 301 */               getResponder().getCommunicator().sendNormalServerMessage("You have been charged " + (new Change(senderCost))
/* 302 */                   .getChangeString() + ".");
/* 303 */               Change change = Economy.getEconomy().getChangeFor(getResponder().getMoney());
/* 304 */               getResponder().getCommunicator().sendNormalServerMessage("You now have " + change.getChangeString() + " in the bank.");
/* 305 */               getResponder().getCommunicator().sendNormalServerMessage("If this amount is incorrect, please wait a while since the information may not immediately be updated.");
/*     */             
/*     */             }
/*     */             else
/*     */             {
/* 310 */               failed = true;
/*     */             }
/*     */           
/* 313 */           } catch (IOException e) {
/*     */             
/* 315 */             failed = true;
/* 316 */             logger.log(Level.WARNING, e.getMessage(), e);
/*     */           } 
/*     */         }
/* 319 */         if (failed) {
/*     */           
/* 321 */           getResponder().getCommunicator().sendNormalServerMessage("Something went wrong, delivery set up cancelled.");
/*     */           
/* 323 */           this.container.setIsSealedOverride(false);
/*     */           
/*     */           return;
/*     */         } 
/* 327 */         this.container.setIsSealedByPlayer(true);
/*     */         
/* 329 */         Delivery.addDelivery(this.container.getData(), this.container.getWurmId(), this.crates, getResponder().getWurmId(), senderCost, this.receiverId, codprice, this.wagonerId);
/*     */         
/* 331 */         getResponder().getCommunicator().sendNormalServerMessage("You have set up a delivery to " + this.receiverName + " for " + (new Change(codprice))
/* 332 */             .getChangeString() + ".");
/*     */         break;
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
/* 345 */     int height = 300;
/* 346 */     StringBuilder buf = new StringBuilder();
/* 347 */     buf.append(getBmlHeader());
/* 348 */     buf.append("closebutton{id=\"cancel\"};");
/* 349 */     buf.append("text{type=\"bold\";color=\"255,127,127\"text=\"" + this.error + "\"}");
/*     */     
/* 351 */     Set<Creature> creatureSet = Creatures.getMayUseWagonersFor(getResponder());
/*     */ 
/*     */     
/* 354 */     long endWaystoneId = this.container.getData();
/*     */     
/* 356 */     Item endWaystone = null;
/*     */     
/*     */     try {
/* 359 */       endWaystone = Items.getItem(endWaystoneId);
/*     */     }
/* 361 */     catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */ 
/*     */     
/* 365 */     Set<Distanced> wagonerSet = new HashSet<>();
/* 366 */     for (Creature creature : creatureSet) {
/*     */       
/* 368 */       Wagoner wagoner = Wagoner.getWagoner(creature.getWurmId());
/* 369 */       if (wagoner != null) {
/*     */         
/* 371 */         float dist = PathToCalculate.getRouteDistance(wagoner.getHomeWaystoneId(), endWaystoneId);
/* 372 */         if (dist != 99999.0F) {
/*     */ 
/*     */           
/* 375 */           boolean isPublic = creature.publicMayUse(getResponder());
/* 376 */           String villName = "";
/* 377 */           Village vill = creature.getCitizenVillage();
/* 378 */           if (vill != null) {
/* 379 */             villName = vill.getName();
/*     */           }
/* 381 */           wagonerSet.add(new Distanced(wagoner, isPublic, villName, dist));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 386 */     Distanced[] wagonerArr = wagonerSet.<Distanced>toArray(new Distanced[wagonerSet.size()]);
/* 387 */     int absSortBy = Math.abs(this.sortBy);
/* 388 */     final int upDown = Integer.signum(this.sortBy);
/* 389 */     switch (absSortBy) {
/*     */       
/*     */       case 1:
/* 392 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerSetupDeliveryQuestion.Distanced param1, WagonerSetupDeliveryQuestion.Distanced param2)
/*     */               {
/* 397 */                 return param1.getWagoner().getName().compareTo(param2.getWagoner().getName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 2:
/* 402 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerSetupDeliveryQuestion.Distanced param1, WagonerSetupDeliveryQuestion.Distanced param2)
/*     */               {
/* 407 */                 return param1.getType().compareTo(param2.getType()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 3:
/* 412 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerSetupDeliveryQuestion.Distanced param1, WagonerSetupDeliveryQuestion.Distanced param2)
/*     */               {
/* 417 */                 return param1.getVillageName().compareTo(param2.getVillageName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 4:
/* 422 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerSetupDeliveryQuestion.Distanced param1, WagonerSetupDeliveryQuestion.Distanced param2)
/*     */               {
/* 427 */                 if (param1.getDistance() < param2.getDistance()) {
/* 428 */                   return 1 * upDown;
/*     */                 }
/* 430 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 5:
/* 435 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerSetupDeliveryQuestion.Distanced param1, WagonerSetupDeliveryQuestion.Distanced param2)
/*     */               {
/* 440 */                 return param1.getWagoner().getStateName().compareTo(param2.getWagoner().getStateName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 6:
/* 445 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerSetupDeliveryQuestion.Distanced param1, WagonerSetupDeliveryQuestion.Distanced param2)
/*     */               {
/* 450 */                 if (param1.getQueueLength() < param2.getQueueLength()) {
/* 451 */                   return 1 * upDown;
/*     */                 }
/* 453 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */     } 
/* 458 */     buf.append("label{text=\"Select which wagoner to use \"};");
/* 459 */     buf.append("table{rows=\"1\";cols=\"8\";label{text=\"\"};" + 
/*     */         
/* 461 */         colHeader("Name", 1, this.sortBy) + 
/* 462 */         colHeader("Type", 2, this.sortBy) + 
/* 463 */         colHeader("Village", 3, this.sortBy) + 
/* 464 */         colHeader("Distance", 4, this.sortBy) + 
/* 465 */         colHeader("State", 5, this.sortBy) + 
/* 466 */         colHeader("Queue", 6, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*     */ 
/*     */     
/* 469 */     String noneSelected = "selected=\"true\";";
/* 470 */     for (Distanced distanced : wagonerArr) {
/*     */       
/* 472 */       String selected = "";
/* 473 */       if (this.wagonerId == distanced.getWagoner().getWurmId()) {
/*     */         
/* 475 */         selected = "selected=\"true\";";
/* 476 */         noneSelected = "";
/*     */       } 
/* 478 */       buf.append((distanced.getVillageName().length() == 0) ? "label{text=\"\"}" : ("radio{group=\"sel\";id=\"" + distanced
/* 479 */           .getWagoner().getWurmId() + "\";" + selected + "text=\"\"}label{text=\"" + distanced
/* 480 */           .getWagoner().getName() + "\"};label{text=\"" + distanced
/* 481 */           .getType() + "\"};label{text=\"" + distanced
/* 482 */           .getVillageName() + "\"};label{text=\"" + 
/* 483 */           (int)distanced.getDistance() + "\"};label{text=\"" + distanced
/* 484 */           .getWagoner().getStateName() + "\"};label{text=\"" + distanced
/* 485 */           .getQueueLength() + "\"};label{text=\"\"}"));
/*     */     } 
/*     */ 
/*     */     
/* 489 */     buf.append("}");
/* 490 */     buf.append("radio{group=\"sel\";id=\"-10\";" + noneSelected + "text=\" None\"}");
/* 491 */     if (endWaystone == null)
/*     */     {
/*     */       
/* 494 */       buf.append("text{text=\"Could not find the associated waystone for this wagoner container.\"}");
/*     */     }
/* 496 */     buf.append("text{text=\"\"}");
/* 497 */     boolean ownsAll = true;
/* 498 */     Item[] crates = this.container.getItemsAsArray();
/* 499 */     for (Item crate : crates) {
/*     */       
/* 501 */       if (crate.getLastOwnerId() != getResponder().getWurmId())
/* 502 */         ownsAll = false; 
/*     */     } 
/* 504 */     if (!ownsAll) {
/*     */       
/* 506 */       buf.append("text{text=\"You cannot set up a delivery on this container as you did not load all the crates.\"}");
/* 507 */       buf.append("harray{button{text=\"Close\";id=\"cancel\"}}");
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 513 */       buf.append("text{text=\"Specify who to deliver the contents of this container to.\"}");
/* 514 */       buf.append("harray{label{text=\"Deliver to \"}input{maxchars=\"40\";id=\"playername\";text=\"" + this.receiverName + "\"}}");
/*     */ 
/*     */ 
/*     */       
/* 518 */       buf.append("text{text=\"\"}");
/* 519 */       buf.append("text{text=\"They will be informed and will have to accept it before the delivery can start.\"}");
/* 520 */       buf.append("text{text=\"\"}");
/*     */       
/* 522 */       buf.append("harray{label{text=\"Continue to \"};button{text=\"Next\";id=\"next\"}label{text=\" screen to add costs.\"};}");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 528 */     buf.append("}};null;null;}");
/* 529 */     height += wagonerArr.length * 20;
/*     */     
/* 531 */     getResponder().getCommunicator().sendBml(420, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion2() {
/* 539 */     StringBuilder buf = new StringBuilder();
/* 540 */     String header = "border{scroll{vertical='true';horizontal='false';varray{rescale='true';passthrough{id='id';text='" + getId() + "'}";
/* 541 */     buf.append(header);
/* 542 */     buf.append("closebutton{id=\"cancel\"};");
/*     */     
/* 544 */     Wagoner wagoner = Wagoner.getWagoner(this.wagonerId);
/* 545 */     buf.append("text{text=\"You have selected " + wagoner.getName() + " to perform the delivery.\"}");
/* 546 */     buf.append("text{text=\"They will be delivering to " + this.receiverName + ".\"}");
/* 547 */     long money = getResponder().getMoney();
/* 548 */     if (money <= 0L) {
/* 549 */       buf.append("text{text='You have no money in the bank.'}");
/*     */     } else {
/* 551 */       buf.append("text{text='You have " + (new Change(money)).getChangeString() + " in the bank.'}");
/*     */     } 
/* 553 */     buf.append("}};null;");
/*     */     
/* 555 */     buf.append("tree{id=\"t1\";cols=\"3\";showheader=\"true\";height=\"300\"col{text=\"QL\";width=\"50\"};col{text=\"DMG\";width=\"50\"};col{text=\"Weight\";width=\"50\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 561 */     Item[] crates = this.container.getItemsAsArray();
/* 562 */     for (Item crate : crates)
/*     */     {
/* 564 */       buf.append(addCrate(crate));
/*     */     }
/*     */     
/* 567 */     buf.append("}");
/* 568 */     buf.append("null;varray{");
/*     */     
/* 570 */     this.crates = crates.length;
/* 571 */     buf.append("label{text=\"" + wagoner.getName() + " charges a delivery fee of 1C per crate. So a total of " + this.crates + "C.\"}");
/* 572 */     buf.append("harray{label{text=\"These fees will be paid for by:\"}radio{group=\"fee\";id=\"0\";text=\"You \"}radio{group=\"fee\";id=\"1\";selected=\"true\";text=\"" + this.receiverName + ".\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 578 */     buf.append("harray{label{text=\"You are charging \"};");
/*     */     
/* 580 */     buf.append("table{rows=\"1\"; cols=\"8\";label{text=\"G:\"};input{maxchars=\"2\"; id=\"g\";text=\"0\"};label{text=\"S:\"};input{maxchars=\"2\"; id=\"s\";text=\"0\"};label{text=\"C:\"};input{maxchars=\"2\"; id=\"c\";text=\"0\"};label{text=\"I:\"};input{maxchars=\"2\"; id=\"i\";text=\"0\"};}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 586 */     buf.append("label{text=\" for the goods:\"}}");
/*     */     
/* 588 */     buf.append("harray{button{text=\"Add to " + wagoner
/* 589 */         .getName() + "'s queue\";id=\"queue\"};}");
/*     */     
/* 591 */     buf.append("text=\"\"}}");
/* 592 */     getResponder().getCommunicator().sendBml(450, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String addCrate(Item crate) {
/* 597 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 599 */     String sQL = "" + df.format(crate.getQualityLevel());
/* 600 */     String sDMG = "" + df.format(crate.getDamage());
/* 601 */     String sWeight = "" + df.format((crate.getFullWeight(true) / 1000.0F));
/* 602 */     String itemName = longItemName(crate);
/* 603 */     String hover = itemName;
/* 604 */     Item[] contained = crate.getItemsAsArray();
/* 605 */     int children = contained.length;
/*     */     
/* 607 */     buf.append("row{id=\"" + this.id + "\";hover=\"" + hover + "\";name=\"" + itemName + "\";rarity=\"" + crate
/* 608 */         .getRarity() + "\";children=\"" + children + "\";col{text=\"" + sQL + "\"};col{text=\"" + sDMG + "\"};col{text=\"" + sWeight + "\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 614 */     for (Item bulkItem : contained)
/*     */     {
/* 616 */       buf.append(addBulkItem(bulkItem));
/*     */     }
/* 618 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String addBulkItem(Item bulkItem) {
/* 623 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 625 */     String sQL = "" + df.format(bulkItem.getQualityLevel());
/* 626 */     String sWeight = "" + df.format((bulkItem.getFullWeight(true) / 1000.0F));
/* 627 */     String itemName = longItemName(bulkItem);
/* 628 */     String hover = itemName;
/*     */     
/* 630 */     buf.append("row{id=\"" + this.id + "\";hover=\"" + hover + "\";name=\"" + itemName + "\";rarity=\"0\";children=\"0\";col{text=\"" + sQL + "\"};col{text=\"0.00\"};col{text=\"" + sWeight + "\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 636 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String longItemName(Item litem) {
/* 644 */     StringBuilder sb = new StringBuilder();
/* 645 */     if (litem.getRarity() == 1) {
/* 646 */       sb.append("rare ");
/* 647 */     } else if (litem.getRarity() == 2) {
/* 648 */       sb.append("supreme ");
/* 649 */     } else if (litem.getRarity() == 3) {
/* 650 */       sb.append("fantastic ");
/* 651 */     }  String name = (litem.getName().length() == 0) ? litem.getTemplate().getName() : litem.getName();
/* 652 */     MaterialUtilities.appendNameWithMaterialSuffix(sb, name.replace("\"", "''"), litem.getMaterial());
/*     */     
/* 654 */     if (litem.getDescription().length() > 0)
/* 655 */       sb.append(" (" + litem.getDescription() + ")"); 
/* 656 */     return sb.toString();
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
/*     */   public void timedOut() {
/* 668 */     this.container.setIsSealedOverride(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class Distanced
/*     */   {
/*     */     private final Wagoner wagoner;
/*     */     
/*     */     private final boolean isPublic;
/*     */     
/*     */     private final String villageName;
/*     */     
/*     */     private final float distance;
/*     */     
/*     */     private final int queueLength;
/*     */ 
/*     */     
/*     */     Distanced(Wagoner wagoner, boolean isPublic, String villageName, float distance) {
/* 687 */       this.wagoner = wagoner;
/* 688 */       this.isPublic = isPublic;
/* 689 */       this.villageName = villageName;
/* 690 */       this.distance = distance;
/* 691 */       this.queueLength = Delivery.getQueueLength(wagoner.getWurmId());
/*     */     }
/*     */ 
/*     */     
/*     */     Wagoner getWagoner() {
/* 696 */       return this.wagoner;
/*     */     }
/*     */ 
/*     */     
/*     */     float getDistance() {
/* 701 */       return this.distance;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPublic() {
/* 706 */       return this.isPublic;
/*     */     }
/*     */ 
/*     */     
/*     */     String getType() {
/* 711 */       if (this.isPublic)
/* 712 */         return "Public"; 
/* 713 */       return "Private";
/*     */     }
/*     */ 
/*     */     
/*     */     String getVillageName() {
/* 718 */       return this.villageName;
/*     */     }
/*     */ 
/*     */     
/*     */     int getQueueLength() {
/* 723 */       return this.queueLength;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WagonerSetupDeliveryQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */