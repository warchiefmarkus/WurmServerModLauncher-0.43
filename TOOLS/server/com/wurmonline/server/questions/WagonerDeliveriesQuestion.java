/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.Delivery;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.highways.PathToCalculate;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ public class WagonerDeliveriesQuestion
/*     */   extends Question
/*     */ {
/*  58 */   private static final Logger logger = Logger.getLogger(WagonerDeliveriesQuestion.class.getName());
/*  59 */   private static final DecimalFormat df = new DecimalFormat("#0.00");
/*     */   private long deliveryId;
/*  61 */   private int sortBy = 1;
/*  62 */   private int pageNo = 1;
/*     */   
/*     */   private boolean hasBack = false;
/*     */   private final boolean hasList;
/*     */   
/*     */   public WagonerDeliveriesQuestion(Creature aResponder, long deliveryId, boolean hasList) {
/*  68 */     super(aResponder, getTitle(deliveryId), getTitle(deliveryId), 147, deliveryId);
/*  69 */     this.deliveryId = deliveryId;
/*  70 */     this.hasList = hasList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public WagonerDeliveriesQuestion(Creature aResponder, long deliveryId, boolean hasList, int sortBy, int pageNo, boolean hasBack) {
/*  76 */     super(aResponder, getTitle(deliveryId), getTitle(deliveryId), 147, deliveryId);
/*  77 */     this.deliveryId = deliveryId;
/*  78 */     this.hasList = hasList;
/*  79 */     this.sortBy = sortBy;
/*  80 */     this.pageNo = pageNo;
/*  81 */     this.hasBack = hasBack;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getTitle(long deliveryId) {
/*  86 */     if (deliveryId == -10L)
/*  87 */       return "Wagoner Deliveries Management"; 
/*  88 */     return "Wagoner Delivery Management";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*     */     boolean next, cancel, close;
/*     */     WagonerDeliveriesQuestion wdq;
/*     */     boolean assign, bool1, back;
/*     */     WagonerDeliveriesQuestion wagonerDeliveriesQuestion1;
/*     */     boolean reject;
/*  99 */     setAnswer(aAnswer);
/* 100 */     switch (this.pageNo) {
/*     */ 
/*     */       
/*     */       case 1:
/* 104 */         next = getBooleanProp("next");
/*     */         
/* 106 */         if (next) {
/*     */           
/* 108 */           String sel = aAnswer.getProperty("sel");
/* 109 */           this.deliveryId = Long.parseLong(sel);
/* 110 */           if (this.deliveryId == -10L) {
/*     */             
/* 112 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to do nothing.");
/*     */             return;
/*     */           } 
/* 115 */           this.pageNo = 2;
/* 116 */           this.sortBy = 1;
/* 117 */           this.hasBack = true;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 122 */           for (String key : getAnswer().stringPropertyNames()) {
/*     */             
/* 124 */             if (key.startsWith("sort")) {
/*     */ 
/*     */               
/* 127 */               String sid = key.substring(4);
/* 128 */               this.sortBy = Integer.parseInt(sid);
/*     */               break;
/*     */             } 
/* 131 */             if (key.startsWith("assign")) {
/*     */ 
/*     */               
/* 134 */               String sid = key.substring(6);
/* 135 */               this.deliveryId = Integer.parseInt(sid);
/* 136 */               WagonerDeliveriesQuestion wagonerDeliveriesQuestion = new WagonerDeliveriesQuestion(getResponder(), this.deliveryId, this.hasList, this.sortBy, this.pageNo, this.hasBack);
/*     */               
/* 138 */               wagonerDeliveriesQuestion.sendQuestion3();
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 143 */         wdq = new WagonerDeliveriesQuestion(getResponder(), this.deliveryId, this.hasList, this.sortBy, this.pageNo, this.hasBack);
/*     */         
/* 145 */         switch (this.pageNo) {
/*     */ 
/*     */           
/*     */           case 1:
/* 149 */             wdq.sendQuestion();
/*     */             break;
/*     */ 
/*     */           
/*     */           case 2:
/* 154 */             wdq.sendQuestion2();
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/*     */         return;
/*     */       
/*     */       case 2:
/* 162 */         cancel = getBooleanProp("cancel");
/* 163 */         if (cancel) {
/*     */ 
/*     */           
/* 166 */           Delivery delivery = Delivery.getDelivery(this.deliveryId);
/* 167 */           if (delivery != null && delivery.getState() == 0) {
/*     */ 
/*     */             
/* 170 */             delivery.setCancelled();
/* 171 */             getResponder().getCommunicator().sendServerMessage("You have cancelled your delivery to " + delivery
/* 172 */                 .getReceiverName() + ".", 255, 127, 127);
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 177 */           else if (delivery == null) {
/* 178 */             getResponder().getCommunicator().sendNormalServerMessage("Delivery cannot be found!");
/*     */           } else {
/* 180 */             getResponder().getCommunicator().sendNormalServerMessage("Delivery may not be cancelled as it has been accepted.");
/*     */           } 
/*     */           return;
/*     */         } 
/* 184 */         assign = getBooleanProp("assign");
/* 185 */         if (assign) {
/*     */           
/* 187 */           WagonerDeliveriesQuestion wagonerDeliveriesQuestion = new WagonerDeliveriesQuestion(getResponder(), this.deliveryId, this.hasList, this.sortBy, this.pageNo, this.hasBack);
/*     */           
/* 189 */           wagonerDeliveriesQuestion.sendQuestion3();
/*     */           return;
/*     */         } 
/* 192 */         back = getBooleanProp("back");
/* 193 */         if (back) {
/*     */ 
/*     */           
/* 196 */           WagonerDeliveriesQuestion wagonerDeliveriesQuestion = new WagonerDeliveriesQuestion(getResponder(), -10L, true, 1, 1, false);
/* 197 */           wagonerDeliveriesQuestion.sendQuestion();
/*     */           return;
/*     */         } 
/* 200 */         reject = getBooleanProp("reject");
/* 201 */         if (reject) {
/*     */           
/* 203 */           Delivery delivery = Delivery.getDelivery(this.deliveryId);
/* 204 */           delivery.setRejected();
/* 205 */           getResponder().getCommunicator().sendNormalServerMessage("Delivery rejected from " + delivery
/* 206 */               .getSenderName());
/*     */           
/*     */           try {
/* 209 */             Player player = Players.getInstance().getPlayer(delivery.getSenderId());
/* 210 */             player.getCommunicator().sendNormalServerMessage("Delivery rejected by " + delivery
/* 211 */                 .getReceiverName());
/*     */           }
/* 213 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 222 */         close = getBooleanProp("close");
/* 223 */         if (close) {
/*     */           return;
/*     */         }
/*     */         
/* 227 */         bool1 = getBooleanProp("cancel");
/* 228 */         if (bool1) {
/*     */ 
/*     */           
/* 231 */           Delivery delivery = Delivery.getDelivery(this.deliveryId);
/* 232 */           delivery.setCancelledNoWagoner();
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 238 */           String sel = aAnswer.getProperty("sel");
/* 239 */           long selId = Long.parseLong(sel);
/* 240 */           if (selId == -10L) {
/*     */             
/* 242 */             getResponder().getCommunicator().sendNormalServerMessage("No wagoner selected.");
/*     */           }
/*     */           else {
/*     */             
/* 246 */             Delivery delivery = Delivery.getDelivery(this.deliveryId);
/* 247 */             delivery.setWagonerId(selId);
/*     */           } 
/*     */         } 
/*     */         
/* 251 */         wagonerDeliveriesQuestion1 = new WagonerDeliveriesQuestion(getResponder(), this.deliveryId, this.hasList, this.sortBy, this.pageNo, this.hasBack);
/*     */         
/* 253 */         if (this.hasList) {
/* 254 */           wagonerDeliveriesQuestion1.sendQuestion(); break;
/*     */         } 
/* 256 */         wagonerDeliveriesQuestion1.sendQuestion2();
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
/* 269 */     this.pageNo = 1;
/*     */     
/* 271 */     StringBuilder buf = new StringBuilder();
/* 272 */     buf.append(getBmlHeader());
/* 273 */     buf.append("text{text=\"\"}");
/*     */ 
/*     */     
/* 276 */     Delivery[] deliveries = Delivery.getPendingDeliveries(getResponder().getWurmId());
/* 277 */     int absSortBy = Math.abs(this.sortBy);
/* 278 */     final int upDown = Integer.signum(this.sortBy);
/* 279 */     switch (absSortBy) {
/*     */       
/*     */       case 7:
/* 282 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 287 */                 return param1.getSenderName().compareTo(param2.getSenderName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 2:
/* 292 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 297 */                 return param1.getReceiverName().compareTo(param2.getReceiverName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 3:
/* 302 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 307 */                 return param1.getStateName().compareTo(param2.getStateName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 4:
/* 312 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */ 
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 318 */                 if (param1.getCrates() < param2.getCrates()) {
/* 319 */                   return 1 * upDown;
/*     */                 }
/* 321 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 5:
/* 326 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 331 */                 return param1.getWagonerName().compareTo(param2.getWagonerName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 6:
/* 336 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 341 */                 return param1.getWagonerState().compareTo(param2.getWagonerState()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 1:
/* 346 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 351 */                 if (param1.getWhen() < param2.getWhen()) {
/* 352 */                   return 1 * upDown;
/*     */                 }
/* 354 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */     } 
/*     */     
/* 360 */     buf.append("label{text=\"Select which delivery to view \"};");
/* 361 */     buf.append("table{rows=\"1\";cols=\"8\";label{text=\"\"};" + 
/*     */         
/* 363 */         colHeader("Sender", 7, this.sortBy) + 
/* 364 */         colHeader("Receiver", 2, this.sortBy) + 
/* 365 */         colHeader("Delivery State", 3, this.sortBy) + 
/* 366 */         colHeader("# Crates", 4, this.sortBy) + 
/* 367 */         colHeader("Wagoner", 5, this.sortBy) + 
/* 368 */         colHeader("Wagoner State", 6, this.sortBy) + 
/* 369 */         colHeader("Last state change", 1, this.sortBy));
/*     */     
/* 371 */     String noneSelected = "selected=\"true\";";
/* 372 */     for (Delivery delivery : deliveries) {
/*     */       String rad, wagonerName, wagonerState;
/*     */       
/* 375 */       if (delivery.canSeeCrates()) {
/* 376 */         rad = "radio{group=\"sel\";id=\"" + delivery.getDeliveryId() + "\"text=\"\"}";
/*     */       } else {
/* 378 */         rad = "label{text=\"  \"};";
/*     */       } 
/*     */       
/* 381 */       if (delivery.getWagonerId() == -10L && delivery.isQueued()) {
/*     */         
/* 383 */         wagonerName = "button{text=\"Assign wagoner\";id=\"assign" + delivery.getDeliveryId() + "\"}";
/* 384 */         wagonerState = "label{text=\"\"};";
/*     */       }
/*     */       else {
/*     */         
/* 388 */         wagonerName = "label{text=\"" + delivery.getWagonerName() + "\"};";
/* 389 */         wagonerState = "label{text=\"" + delivery.getWagonerState() + "\"};";
/*     */       } 
/* 391 */       buf.append(rad + "label{text=\"" + delivery
/* 392 */           .getSenderName() + "\"};label{text=\"" + delivery
/* 393 */           .getReceiverName() + "\"};label{text=\"" + delivery
/* 394 */           .getStateName() + "\"};label{text=\"" + delivery
/* 395 */           .getCrates() + "\"};" + wagonerName + wagonerState + "label{text=\"" + delivery
/*     */ 
/*     */           
/* 398 */           .getStringWhen() + "\"}");
/*     */     } 
/*     */     
/* 401 */     buf.append("}");
/* 402 */     buf.append("radio{group=\"sel\";id=\"-10\";" + noneSelected + "text=\" None\"}");
/* 403 */     buf.append("text{text=\"\"}");
/*     */     
/* 405 */     buf.append("harray{label{text=\"Continue to \"};button{text=\"Next\";id=\"next\"}label{text=\" screen to view selected delivery.\"};}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 410 */     buf.append("}};null;null;}");
/*     */     
/* 412 */     getResponder().getCommunicator().sendBml(600, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion2() {
/* 417 */     Delivery delivery = Delivery.getDelivery(this.deliveryId);
/*     */     
/* 419 */     if (delivery == null) {
/*     */ 
/*     */       
/* 422 */       getResponder().getCommunicator().sendNormalServerMessage("Delivery not found!");
/* 423 */       if (this.hasBack) {
/*     */         
/* 425 */         this.pageNo = 1;
/* 426 */         sendQuestion();
/*     */       } 
/*     */       return;
/*     */     } 
/* 430 */     this.pageNo = 2;
/* 431 */     boolean hasCancel = (delivery.getSenderId() == getResponder().getWurmId() && delivery.getState() == 0);
/* 432 */     boolean hasReject = (delivery.getReceiverId() == getResponder().getWurmId() && delivery.getState() == 0);
/* 433 */     Creature creature = (delivery.getReceiverId() == getResponder().getWurmId()) ? getResponder() : null;
/* 434 */     String buffer = showDelivery(delivery, getId(), creature, this.hasBack, false, false, hasReject, hasCancel);
/* 435 */     getResponder().getCommunicator().sendBml(400, 400, true, true, buffer, 200, 200, 200, this.title);
/*     */   }
/*     */   
/*     */   public void sendQuestion3() {
/*     */     String assignButton;
/* 440 */     Delivery delivery = Delivery.getDelivery(this.deliveryId);
/*     */     
/* 442 */     if (delivery == null) {
/*     */ 
/*     */       
/* 445 */       getResponder().getCommunicator().sendNormalServerMessage("Delivery not found!");
/* 446 */       if (this.hasBack) {
/*     */         
/* 448 */         this.pageNo = 1;
/* 449 */         sendQuestion();
/*     */       } 
/*     */       return;
/*     */     } 
/* 453 */     this.pageNo = 3;
/*     */     
/* 455 */     Set<Creature> creatureSet = Creatures.getMayUseWagonersFor(getResponder());
/*     */ 
/*     */     
/* 458 */     Set<Distanced> wagonerSet = new HashSet<>();
/* 459 */     float dist = PathToCalculate.getRouteDistance(delivery.getCollectionWaystoneId(), delivery.getDeliveryWaystoneId());
/* 460 */     if (dist != 99999.0F)
/*     */     {
/* 462 */       for (Creature creature : creatureSet) {
/*     */         
/* 464 */         Wagoner wagoner = Wagoner.getWagoner(creature.getWurmId());
/* 465 */         if (wagoner != null) {
/*     */           
/* 467 */           dist = PathToCalculate.getRouteDistance(wagoner.getHomeWaystoneId(), delivery.getCollectionWaystoneId());
/* 468 */           if (dist != 99999.0F) {
/*     */ 
/*     */             
/* 471 */             boolean isPublic = creature.publicMayUse(getResponder());
/* 472 */             String villName = "";
/* 473 */             Village vill = creature.getCitizenVillage();
/* 474 */             if (vill != null) {
/* 475 */               villName = vill.getName();
/*     */             }
/* 477 */             wagonerSet.add(new Distanced(wagoner, isPublic, villName, dist));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 483 */     Distanced[] wagonerArr = wagonerSet.<Distanced>toArray(new Distanced[wagonerSet.size()]);
/* 484 */     int absSortBy = Math.abs(this.sortBy);
/* 485 */     final int upDown = Integer.signum(this.sortBy);
/* 486 */     switch (absSortBy) {
/*     */       
/*     */       case 1:
/* 489 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerDeliveriesQuestion.Distanced param1, WagonerDeliveriesQuestion.Distanced param2)
/*     */               {
/* 494 */                 return param1.getWagoner().getName().compareTo(param2.getWagoner().getName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 2:
/* 499 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerDeliveriesQuestion.Distanced param1, WagonerDeliveriesQuestion.Distanced param2)
/*     */               {
/* 504 */                 return param1.getType().compareTo(param2.getType()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 3:
/* 509 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerDeliveriesQuestion.Distanced param1, WagonerDeliveriesQuestion.Distanced param2)
/*     */               {
/* 514 */                 return param1.getVillageName().compareTo(param2.getVillageName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 4:
/* 519 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerDeliveriesQuestion.Distanced param1, WagonerDeliveriesQuestion.Distanced param2)
/*     */               {
/* 524 */                 if (param1.getDistance() < param2.getDistance()) {
/* 525 */                   return 1 * upDown;
/*     */                 }
/* 527 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 5:
/* 532 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerDeliveriesQuestion.Distanced param1, WagonerDeliveriesQuestion.Distanced param2)
/*     */               {
/* 537 */                 return param1.getWagoner().getStateName().compareTo(param2.getWagoner().getStateName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 6:
/* 542 */         Arrays.sort(wagonerArr, new Comparator<Distanced>()
/*     */             {
/*     */               
/*     */               public int compare(WagonerDeliveriesQuestion.Distanced param1, WagonerDeliveriesQuestion.Distanced param2)
/*     */               {
/* 547 */                 if (param1.getQueueLength() < param2.getQueueLength()) {
/* 548 */                   return 1 * upDown;
/*     */                 }
/* 550 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */     } 
/* 555 */     int height = 300;
/* 556 */     StringBuilder buf = new StringBuilder();
/* 557 */     buf.append(getBmlHeader());
/* 558 */     buf.append("closebutton{id=\"close\"};");
/* 559 */     buf.append("label{text=\"Select which wagoner to use \"};");
/* 560 */     buf.append("table{rows=\"1\";cols=\"8\";label{text=\"\"};" + 
/*     */         
/* 562 */         colHeader("Name", 1, this.sortBy) + 
/* 563 */         colHeader("Type", 2, this.sortBy) + 
/* 564 */         colHeader("Village", 3, this.sortBy) + 
/* 565 */         colHeader("Distance", 4, this.sortBy) + 
/* 566 */         colHeader("State", 5, this.sortBy) + 
/* 567 */         colHeader("Queue", 6, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*     */ 
/*     */     
/* 570 */     String noneSelected = "selected=\"true\";";
/* 571 */     for (Distanced distanced : wagonerArr)
/*     */     {
/* 573 */       buf.append((distanced.getVillageName().length() == 0) ? "label{text=\"\"}" : ("radio{group=\"sel\";id=\"" + distanced
/* 574 */           .getWagoner().getWurmId() + "\";text=\"\"}label{text=\"" + distanced
/* 575 */           .getWagoner().getName() + "\"};label{text=\"" + distanced
/* 576 */           .getType() + "\"};label{text=\"" + distanced
/* 577 */           .getVillageName() + "\"};label{text=\"" + 
/* 578 */           (int)distanced.getDistance() + "\"};label{text=\"" + distanced
/* 579 */           .getWagoner().getStateName() + "\"};label{text=\"" + distanced
/* 580 */           .getQueueLength() + "\"};label{text=\"\"}"));
/*     */     }
/*     */ 
/*     */     
/* 584 */     buf.append("}");
/* 585 */     buf.append("radio{group=\"sel\";id=\"-10\";" + noneSelected + "text=\" None\"}");
/* 586 */     buf.append("text{text=\"\"}");
/*     */ 
/*     */     
/* 589 */     if (wagonerArr.length > 0) {
/* 590 */       assignButton = "button{text=\"Assign\";id=\"assign\"};label{text=\" \"};";
/*     */     } else {
/* 592 */       assignButton = "";
/* 593 */     }  buf.append("harray{" + assignButton + "button{text=\"Cancel Delivery\";id=\"cancel\";hover=\"This will remove this delivery from the wagoner queue\";confirm=\"You are about to cancel a delivery to " + delivery
/*     */ 
/*     */ 
/*     */         
/* 597 */         .getReceiverName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"};button{text=\"Back to Delivery\";id=\"back\"}label{text=\" \"};button{text=\"Close\";id=\"close\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 605 */     buf.append("}};null;null;}");
/* 606 */     height += wagonerArr.length * 20;
/*     */     
/* 608 */     getResponder().getCommunicator().sendBml(420, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   static String showDelivery(Delivery delivery, int questionId, @Nullable Creature creature, boolean hasBack, boolean isNotConnected, boolean hasAccept, boolean hasReject, boolean hasCancel) {
/*     */     String wagonerName;
/* 614 */     StringBuilder buf = new StringBuilder();
/* 615 */     buf.append("border{scroll{vertical='true';horizontal='false';varray{rescale='true';passthrough{id='id';text='" + questionId + "'}");
/*     */     
/* 617 */     buf.append("text{text=\"Delivery of " + delivery.getCrates() + " crate" + (
/* 618 */         (delivery.getCrates() == 1) ? "" : "s") + " from " + delivery
/* 619 */         .getSenderName() + " to " + delivery.getReceiverName() + ".\"}");
/*     */ 
/*     */     
/* 622 */     if (delivery.getWagonerId() == -10L && delivery.isQueued()) {
/* 623 */       wagonerName = "harray{label{text=\"Using \"};button{text=\"Assign wagoner\";id=\"assign\"};label{text=\" \"};}";
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 629 */       wagonerName = "text{text=\"Using " + delivery.getWagonerName() + ".\"}";
/*     */     } 
/* 631 */     buf.append(wagonerName);
/*     */     
/* 633 */     if (isNotConnected) {
/* 634 */       buf.append("label{text=\"This waystone is not connected to the collection waystone, so cannot accept it here.\"}");
/* 635 */     } else if (creature != null) {
/*     */       
/* 637 */       long money = creature.getMoney();
/* 638 */       if (money <= 0L) {
/* 639 */         buf.append("text{text='You have no money in the bank.'}");
/*     */       } else {
/* 641 */         buf.append("text{text='You have " + (new Change(money)).getChangeString() + " in the bank.'}");
/*     */       } 
/* 643 */     }  buf.append("}};null;");
/*     */     
/* 645 */     buf.append("tree{id=\"t1\";cols=\"3\";showheader=\"true\";height=\"300\"col{text=\"QL\";width=\"50\"};col{text=\"DMG\";width=\"50\"};col{text=\"Weight\";width=\"50\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 650 */     Item crateContainer = delivery.getCrateContainer();
/* 651 */     if (crateContainer != null) {
/*     */ 
/*     */       
/* 654 */       Item[] crates = crateContainer.getItemsAsArray();
/* 655 */       for (Item crate : crates)
/*     */       {
/* 657 */         buf.append(addCrate(crate));
/*     */       }
/*     */     } 
/* 660 */     buf.append("}");
/*     */     
/* 662 */     buf.append("null;varray{");
/*     */     
/* 664 */     if (delivery.getSenderCost() > 0L) {
/*     */       
/* 666 */       buf.append("label{text=\"The delivery fees of " + (new Change(delivery.getSenderCost())).getChangeString() + " have been paid for by " + delivery
/* 667 */           .getSenderName() + ".\"}");
/*     */     }
/*     */     else {
/*     */       
/* 671 */       int deliveryFees = delivery.getCrates() * 100;
/* 672 */       buf.append("label{text=\"The delivery fees of " + (new Change(deliveryFees)).getChangeString() + " has been added into the goods cost.\"}");
/*     */     } 
/* 674 */     if (delivery.getReceiverCost() == 0L) {
/*     */       
/* 676 */       buf.append("label{text=\"The goods have already been paid for by " + delivery.getSenderName() + ".\"}");
/*     */ 
/*     */     
/*     */     }
/* 680 */     else if (delivery.getState() == 0) {
/* 681 */       buf.append("label{text=\"The goods cost of " + (new Change(delivery.getReceiverCost())).getChangeString() + " have yet to be agreed by " + delivery
/* 682 */           .getReceiverName() + ".\"}");
/*     */     } else {
/* 684 */       buf.append("label{text=\"The goods cost of " + (new Change(delivery.getReceiverCost())).getChangeString() + " has been paid for by " + delivery
/* 685 */           .getReceiverName() + ".\"}");
/*     */     } 
/* 687 */     buf.append("label{text=\"All monies are held by the wagoner until the delivery is complete.\"}");
/*     */     
/* 689 */     buf.append("harray{" + (hasAccept ? "button{text=\"Accept\";id=\"accept\"};label{text=\" \"};" : "") + (hasReject ? "button{text=\"Reject\";id=\"reject\"};label{text=\" \"};" : "") + ((!hasAccept && !hasReject) ? "button{text=\"Close\";id=\"submit\"};label{text=\" \"};" : "") + (hasCancel ? ("button{text=\"Cancel Delivery\";id=\"cancel\";hover=\"This will remove this delivery from the wagoner queue\";confirm=\"You are about to cancel a delivery to " + delivery
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 695 */         .getReceiverName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"};") : "") + (hasBack ? "button{text=\"Back to list\";id=\"back\"};" : "") + "}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 700 */     buf.append("text=\"\"}}");
/*     */     
/* 702 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String addCrate(Item crate) {
/* 707 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 709 */     String sQL = "" + df.format(crate.getQualityLevel());
/* 710 */     String sDMG = "" + df.format(crate.getDamage());
/* 711 */     String sWeight = "" + df.format((crate.getFullWeight(true) / 1000.0F));
/* 712 */     String itemName = longItemName(crate);
/* 713 */     String hover = itemName;
/* 714 */     Item[] contained = crate.getItemsAsArray();
/* 715 */     int children = contained.length;
/*     */     
/* 717 */     buf.append("row{id=\"" + crate.getWurmId() + "\";hover=\"" + hover + "\";name=\"" + itemName + "\";rarity=\"" + crate
/* 718 */         .getRarity() + "\";children=\"" + children + "\";col{text=\"" + sQL + "\"};col{text=\"" + sDMG + "\"};col{text=\"" + sWeight + "\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 724 */     for (Item bulkItem : contained)
/*     */     {
/* 726 */       buf.append(addBulkItem(bulkItem));
/*     */     }
/* 728 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String addBulkItem(Item bulkItem) {
/* 733 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 735 */     String sQL = "" + df.format(bulkItem.getQualityLevel());
/* 736 */     String sWeight = "" + df.format((bulkItem.getFullWeight(true) / 1000.0F));
/* 737 */     String itemName = longItemName(bulkItem);
/* 738 */     String hover = itemName;
/*     */     
/* 740 */     buf.append("row{id=\"" + bulkItem.getWurmId() + "\";hover=\"" + hover + "\";name=\"" + itemName + "\";rarity=\"0\";children=\"0\";col{text=\"" + sQL + "\"};col{text=\"0.00\"};col{text=\"" + sWeight + "\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 746 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String longItemName(Item litem) {
/* 754 */     StringBuilder sb = new StringBuilder();
/* 755 */     if (litem.getRarity() == 1) {
/* 756 */       sb.append("rare ");
/* 757 */     } else if (litem.getRarity() == 2) {
/* 758 */       sb.append("supreme ");
/* 759 */     } else if (litem.getRarity() == 3) {
/* 760 */       sb.append("fantastic ");
/* 761 */     }  String name = (litem.getName().length() == 0) ? litem.getTemplate().getName() : litem.getName();
/* 762 */     MaterialUtilities.appendNameWithMaterialSuffix(sb, name.replace("\"", "''"), litem.getMaterial());
/*     */     
/* 764 */     if (litem.getDescription().length() > 0)
/* 765 */       sb.append(" (" + litem.getDescription() + ")"); 
/* 766 */     return sb.toString();
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
/* 785 */       this.wagoner = wagoner;
/* 786 */       this.isPublic = isPublic;
/* 787 */       this.villageName = villageName;
/* 788 */       this.distance = distance;
/* 789 */       this.queueLength = Delivery.getQueueLength(wagoner.getWurmId());
/*     */     }
/*     */ 
/*     */     
/*     */     Wagoner getWagoner() {
/* 794 */       return this.wagoner;
/*     */     }
/*     */ 
/*     */     
/*     */     float getDistance() {
/* 799 */       return this.distance;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPublic() {
/* 804 */       return this.isPublic;
/*     */     }
/*     */ 
/*     */     
/*     */     String getType() {
/* 809 */       if (this.isPublic)
/* 810 */         return "Public"; 
/* 811 */       return "Private";
/*     */     }
/*     */ 
/*     */     
/*     */     String getVillageName() {
/* 816 */       return this.villageName;
/*     */     }
/*     */ 
/*     */     
/*     */     int getQueueLength() {
/* 821 */       return this.queueLength;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WagonerDeliveriesQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */