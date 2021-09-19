/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Delivery;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.highways.PathToCalculate;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ public class WagonerAcceptDeliveries
/*     */   extends Question
/*     */   implements MonetaryConstants
/*     */ {
/*  54 */   private static final Logger logger = Logger.getLogger(WagonerAcceptDeliveries.class.getName());
/*     */   
/*     */   private static final String red = "color=\"255,127,127\";";
/*     */   private final Item waystone;
/*  58 */   private long deliveryId = -10L;
/*  59 */   private int sortBy = 1;
/*  60 */   private int pageNo = 1;
/*     */ 
/*     */   
/*     */   public WagonerAcceptDeliveries(Creature aResponder, Item waystone) {
/*  64 */     super(aResponder, "Wagoner Accept Delivery Management", "Wagoner Accept Delivery Management", 146, waystone.getWurmId());
/*  65 */     this.waystone = waystone;
/*     */   }
/*     */ 
/*     */   
/*     */   public WagonerAcceptDeliveries(Creature aResponder, Item waystone, long deliveryId, int sortBy, int pageNo) {
/*  70 */     super(aResponder, "Wagoner Accept Delivery Management", "Wagoner Accept Delivery Management", 146, waystone.getWurmId());
/*  71 */     this.waystone = waystone;
/*  72 */     this.deliveryId = deliveryId;
/*  73 */     this.sortBy = sortBy;
/*  74 */     this.pageNo = pageNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*     */     boolean close, back, next, accept;
/*     */     WagonerAcceptDeliveries wad;
/*     */     boolean reject;
/*  85 */     setAnswer(aAnswer);
/*  86 */     switch (this.pageNo) {
/*     */ 
/*     */       
/*     */       case 1:
/*  90 */         close = getBooleanProp("close");
/*  91 */         if (close) {
/*     */           return;
/*     */         }
/*     */         
/*  95 */         next = getBooleanProp("next");
/*     */         
/*  97 */         if (next) {
/*     */           
/*  99 */           String sel = aAnswer.getProperty("sel");
/* 100 */           this.deliveryId = Long.parseLong(sel);
/* 101 */           if (this.deliveryId == -10L) {
/*     */             
/* 103 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to do nothing.");
/*     */             return;
/*     */           } 
/* 106 */           this.pageNo = 2;
/* 107 */           this.sortBy = 1;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 112 */           for (String key : getAnswer().stringPropertyNames()) {
/*     */             
/* 114 */             if (key.startsWith("sort")) {
/*     */ 
/*     */               
/* 117 */               String sid = key.substring(4);
/* 118 */               this.sortBy = Integer.parseInt(sid);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 123 */         wad = new WagonerAcceptDeliveries(getResponder(), this.waystone, this.deliveryId, this.sortBy, this.pageNo);
/*     */         
/* 125 */         switch (this.pageNo) {
/*     */ 
/*     */           
/*     */           case 1:
/* 129 */             wad.sendQuestion();
/*     */             break;
/*     */ 
/*     */           
/*     */           case 2:
/* 134 */             wad.sendQuestion2();
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/*     */         return;
/*     */       
/*     */       case 2:
/* 142 */         back = getBooleanProp("back");
/* 143 */         if (back) {
/*     */ 
/*     */           
/* 146 */           WagonerAcceptDeliveries wagonerAcceptDeliveries = new WagonerAcceptDeliveries(getResponder(), this.waystone, this.deliveryId, this.sortBy, 1);
/*     */           
/* 148 */           wagonerAcceptDeliveries.sendQuestion();
/*     */           return;
/*     */         } 
/* 151 */         accept = getBooleanProp("accept");
/* 152 */         if (accept) {
/*     */           
/* 154 */           Delivery delivery = Delivery.getDelivery(this.deliveryId);
/*     */           
/* 156 */           long rmoney = getResponder().getMoney();
/* 157 */           if (rmoney < delivery.getReceiverCost()) {
/*     */ 
/*     */             
/* 160 */             getResponder().getCommunicator().sendServerMessage("You cannot afford that delivery.", 255, 127, 127);
/*     */             
/*     */             return;
/*     */           } 
/* 164 */           boolean passed = true;
/* 165 */           if (delivery.getReceiverCost() > 0L) {
/*     */             
/*     */             try {
/*     */               
/* 169 */               passed = getResponder().chargeMoney(delivery.getReceiverCost());
/* 170 */               if (passed)
/*     */               {
/* 172 */                 Change change = Economy.getEconomy().getChangeFor(getResponder().getMoney());
/* 173 */                 getResponder().getCommunicator().sendNormalServerMessage("You now have " + change.getChangeString() + " in the bank.");
/* 174 */                 getResponder().getCommunicator().sendNormalServerMessage("If this amount is incorrect, please wait a while since the information may not immediately be updated.");
/*     */               }
/*     */             
/*     */             }
/* 178 */             catch (IOException e) {
/*     */ 
/*     */               
/* 181 */               passed = false;
/* 182 */               getResponder().getCommunicator().sendServerMessage("Something went wrong!", 255, 127, 127);
/* 183 */               logger.log(Level.WARNING, e.getMessage(), e);
/*     */             } 
/*     */           }
/* 186 */           if (passed) {
/*     */ 
/*     */             
/* 189 */             delivery.setAccepted(this.waystone.getWurmId());
/* 190 */             getResponder().getCommunicator().sendServerMessage("Delivery accepted from " + delivery
/* 191 */                 .getSenderName() + ".", 127, 255, 127);
/*     */             
/*     */             try {
/* 194 */               Player player = Players.getInstance().getPlayer(delivery.getSenderId());
/* 195 */               player.getCommunicator().sendServerMessage("Delivery accepted by " + delivery
/* 196 */                   .getReceiverName() + ".", 127, 255, 127);
/*     */             }
/* 198 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */           } 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 205 */         reject = getBooleanProp("reject");
/* 206 */         if (reject) {
/*     */           
/* 208 */           Delivery delivery = Delivery.getDelivery(this.deliveryId);
/* 209 */           delivery.setRejected();
/*     */           
/* 211 */           getResponder().getCommunicator().sendNormalServerMessage("Delivery rejected from " + delivery
/* 212 */               .getSenderName());
/*     */           
/*     */           try {
/* 215 */             Player player = Players.getInstance().getPlayer(delivery.getSenderId());
/* 216 */             player.getCommunicator().sendNormalServerMessage("Delivery rejected by " + delivery
/* 217 */                 .getReceiverName());
/*     */           }
/* 219 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 237 */     StringBuilder buf = new StringBuilder();
/* 238 */     buf.append(getBmlHeader());
/* 239 */     buf.append("text{text=\"\"}");
/*     */     
/* 241 */     buf.append("text{text=\"The following bulk deliveries are waiting, but you need to accept them first before their delivery can start.\"}");
/* 242 */     buf.append("text{text=\"If an delivery has a Cash On Delivery (C.O.D) cost associated with it, you have to pay upfront before the delivery will start, the monies will be held by the wagoner and will only be paid to the supplier when the delivery is complete.\"}");
/*     */ 
/*     */     
/* 245 */     long money = getResponder().getMoney();
/* 246 */     if (money <= 0L) {
/* 247 */       buf.append("text{text='You have no money in the bank.'}");
/*     */     } else {
/* 249 */       buf.append("text{text='You have " + (new Change(money)).getChangeString() + " in the bank.'}");
/*     */     } 
/*     */     
/* 252 */     Delivery[] deliveries = Delivery.getWaitingDeliveries(getResponder().getWurmId());
/* 253 */     int absSortBy = Math.abs(this.sortBy);
/* 254 */     final int upDown = Integer.signum(this.sortBy);
/* 255 */     switch (absSortBy) {
/*     */       
/*     */       case 1:
/* 258 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 263 */                 return param1.getSenderName().compareTo(param2.getSenderName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 2:
/* 268 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 273 */                 return param1.getReceiverName().compareTo(param2.getReceiverName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 3:
/* 278 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 283 */                 return param1.getStateName().compareTo(param2.getStateName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 4:
/* 288 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */ 
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 294 */                 if (param1.getCrates() < param2.getCrates()) {
/* 295 */                   return 1 * upDown;
/*     */                 }
/* 297 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 5:
/* 302 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 307 */                 return param1.getWagonerName().compareTo(param2.getWagonerName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 6:
/* 312 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 317 */                 return param1.getWagonerState().compareTo(param2.getWagonerState()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 7:
/* 322 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */ 
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 328 */                 if (param1.getReceiverCost() < param2.getReceiverCost()) {
/* 329 */                   return 1 * upDown;
/*     */                 }
/* 331 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */     } 
/*     */     
/* 337 */     buf.append("label{text=\"Select which delivery to view \"};");
/* 338 */     buf.append("table{rows=\"1\";cols=\"9\";label{text=\"\"};" + 
/*     */         
/* 340 */         colHeader("Sender", 1, this.sortBy) + 
/* 341 */         colHeader("Receiver", 2, this.sortBy) + 
/* 342 */         colHeader("Delivery State", 3, this.sortBy) + 
/* 343 */         colHeader("# Crates", 4, this.sortBy) + 
/* 344 */         colHeader("Wagoner", 5, this.sortBy) + 
/* 345 */         colHeader("Wagoner State", 6, this.sortBy) + 
/* 346 */         colHeader("Cost", 7, this.sortBy) + "label{type=\"bold\";text=\"\"};");
/*     */ 
/*     */     
/* 349 */     String noneSelected = "selected=\"true\";";
/* 350 */     for (Delivery delivery : deliveries) {
/*     */       
/* 352 */       boolean sameWaystone = (delivery.getCollectionWaystoneId() == this.waystone.getWurmId());
/* 353 */       boolean connected = (!sameWaystone && PathToCalculate.isWaystoneConnected(delivery.getCollectionWaystoneId(), this.waystone.getWurmId()));
/* 354 */       String selected = "";
/* 355 */       if (this.deliveryId == delivery.getDeliveryId()) {
/*     */         
/* 357 */         selected = "selected=\"true\";";
/* 358 */         noneSelected = "";
/*     */       } 
/*     */       
/* 361 */       buf.append((connected ? ("radio{group=\"sel\";id=\"" + delivery.getDeliveryId() + "\";" + selected + "text=\"\"};") : "label{text=\"  \"};") + "label{text=\"" + delivery
/*     */           
/* 363 */           .getSenderName() + "\"};label{text=\"" + delivery
/* 364 */           .getReceiverName() + "\"};label{text=\"" + delivery
/* 365 */           .getStateName() + "\"};label{text=\"" + delivery
/* 366 */           .getCrates() + "\"};label{text=\"" + delivery
/* 367 */           .getWagonerName() + "\"};label{text=\"" + delivery
/* 368 */           .getWagonerState() + "\"};label{text=\"" + (new Change(delivery
/* 369 */             .getReceiverCost())).getChangeShortString() + "\"};");
/*     */       
/* 371 */       if (sameWaystone) {
/* 372 */         buf.append("label{color=\"255,127,127\";text=\"same waystone\";hover=\"Waystone is the collection one, no need for a wagoner.\"}");
/* 373 */       } else if (!connected) {
/* 374 */         buf.append("label{color=\"255,127,127\";text=\"no route!\";hover=\"No route found from collection waystone to this waystone.\"}");
/*     */       } else {
/* 376 */         buf.append("label{text=\"\"}");
/*     */       } 
/* 378 */     }  buf.append("}");
/* 379 */     buf.append("radio{group=\"sel\";id=\"-10\";" + noneSelected + "text=\" None\"}");
/* 380 */     buf.append("text{text=\"\"}");
/*     */     
/* 382 */     if (this.waystone.getData() != -1L) {
/*     */       
/* 384 */       Wagoner wagoner = Wagoner.getWagoner(this.waystone.getData());
/* 385 */       if (wagoner == null) {
/*     */ 
/*     */         
/* 388 */         logger.log(Level.WARNING, "wagoner (" + this.waystone.getData() + ") not found that was associated with waystone " + this.waystone.getWurmId() + " @" + this.waystone
/* 389 */             .getTileX() + "," + this.waystone.getTileY() + "," + this.waystone.isOnSurface());
/* 390 */         this.waystone.setData(-1L);
/*     */       }
/*     */       else {
/*     */         
/* 394 */         buf.append("label{color=\"255,127,127\";text=\"This waystone is the home of " + wagoner.getName() + " and they wont allow deliveries here.\"};");
/* 395 */         buf.append("harray{button{text=\"Close\";id=\"close\"}}");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 400 */     if (this.waystone.getData() == -1L) {
/*     */ 
/*     */       
/* 403 */       VolaTile vt = Zones.getTileOrNull(this.waystone.getTileX(), this.waystone.getTileY(), this.waystone.isOnSurface());
/* 404 */       Village village = (vt != null) ? vt.getVillage() : null;
/* 405 */       if (village != null && !village.isActionAllowed((short)605, getResponder())) {
/*     */         
/* 407 */         buf.append("label{color=\"255,127,127\";text=\"You need Load permissions to be able to accept a delivery here!\"};");
/* 408 */         buf.append("harray{button{text=\"Close\";id=\"close\"}}");
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 414 */         buf.append("harray{label{text=\"Continue to \"};button{text=\"Next\";id=\"next\"}label{text=\" screen to view selected delivery.\"};}");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 421 */     buf.append("}};null;null;}");
/*     */     
/* 423 */     getResponder().getCommunicator().sendBml(600, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion2() {
/* 428 */     Delivery delivery = Delivery.getDelivery(this.deliveryId);
/*     */     
/* 430 */     if (delivery == null) {
/*     */ 
/*     */       
/* 433 */       getResponder().getCommunicator().sendNormalServerMessage("Delivery not found!");
/* 434 */       this.pageNo = 1;
/* 435 */       sendQuestion();
/*     */       return;
/*     */     } 
/* 438 */     this.pageNo = 2;
/*     */     
/* 440 */     long money = getResponder().getMoney();
/* 441 */     boolean connected = PathToCalculate.isWaystoneConnected(delivery.getCollectionWaystoneId(), this.waystone.getWurmId());
/* 442 */     boolean hasAccept = (money >= delivery.getReceiverCost() && connected);
/* 443 */     String buffer = WagonerDeliveriesQuestion.showDelivery(delivery, getId(), getResponder(), true, !connected, hasAccept, true, false);
/*     */ 
/*     */     
/* 446 */     getResponder().getCommunicator().sendBml(400, 400, true, true, buffer, 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WagonerAcceptDeliveries.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */