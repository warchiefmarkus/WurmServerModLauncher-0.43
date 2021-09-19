/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Delivery
/*      */   implements MiscConstants, Comparable<Delivery>
/*      */ {
/*   64 */   private static final Logger logger = Logger.getLogger(Wagoner.class.getName());
/*      */   
/*      */   public static final byte STATE_WAITING_FOR_ACCEPT = 0;
/*      */   
/*      */   public static final byte STATE_QUEUED = 1;
/*      */   
/*      */   public static final byte STATE_WAITING_FOR_PICKUP = 2;
/*      */   public static final byte STATE_BEING_DELIVERED = 3;
/*      */   public static final byte STATE_DELIVERED = 4;
/*      */   public static final byte STATE_REJECTING = 5;
/*      */   public static final byte STATE_TIMEING_OUT = 6;
/*      */   public static final byte STATE_COMPLETED = 7;
/*      */   public static final byte STATE_REJECTED = 8;
/*      */   public static final byte STATE_CANCELLING = 9;
/*      */   public static final byte STATE_CANCELLED = 10;
/*      */   public static final byte STATE_TIMED_OUT = 11;
/*      */   public static final byte STATE_CANCELLING_NO_WAGONER = 12;
/*   81 */   private final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
/*      */   
/*      */   private static final String CREATE_DELIVERY = "INSERT INTO DELIVERYQUEUE (STATE,COLLECTION_WAYSTONE_ID,CONTAINER_ID,CRATES,SENDER_ID,SENDER_COST,RECEIVER_ID,RECEIVER_COST,DELIVERY_WAYSTONE_ID,WAGONER_ID,TS_EXPIRY,TS_WAITING_FOR_ACCEPT,TS_ACCEPTED_OR_REJECTED,TS_DELIVERY_STARTED,TS_PICKED_UP,TS_DELIVERED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */   
/*      */   private static final String UPDATE_DELIVERY_ACCEPT = "UPDATE DELIVERYQUEUE SET STATE=?,DELIVERY_WAYSTONE_ID=?,TS_ACCEPTED_OR_REJECTED=? WHERE DELIVERY_ID=?";
/*      */   
/*      */   private static final String UPDATE_DELIVERY_REJECT = "UPDATE DELIVERYQUEUE SET STATE=?,TS_ACCEPTED_OR_REJECTED=?,WAGONER_ID=? WHERE DELIVERY_ID=?";
/*      */   
/*      */   private static final String UPDATE_DELIVERY_STARTED = "UPDATE DELIVERYQUEUE SET STATE=?,TS_DELIVERY_STARTED=? WHERE DELIVERY_ID=?";
/*      */   
/*      */   private static final String UPDATE_DELIVERY_PICKED_UP = "UPDATE DELIVERYQUEUE SET STATE=?,TS_PICKED_UP=? WHERE DELIVERY_ID=?";
/*      */   
/*      */   private static final String UPDATE_DELIVERY_DELIVERED = "UPDATE DELIVERYQUEUE SET STATE=?,TS_DELIVERED=? WHERE DELIVERY_ID=?";
/*      */   
/*      */   private static final String UPDATE_DELIVERY_STATE = "UPDATE DELIVERYQUEUE SET STATE=? WHERE DELIVERY_ID=?";
/*      */   private static final String UPDATE_DELIVERY_WAGONER = "UPDATE DELIVERYQUEUE SET WAGONER_ID=? WHERE DELIVERY_ID=?";
/*      */   private static final String UPDATE_DELIVERY_CONTAINER = "UPDATE DELIVERYQUEUE SET CONTAINER_ID=? WHERE DELIVERY_ID=?";
/*      */   private static final String DELETE_DELIVERY = "DELETE FROM DELIVERYQUEUE WHERE DELIVERY_ID=?";
/*      */   private static final String GET_ALL_DELIVERIES = "SELECT * FROM DELIVERYQUEUE ORDER BY DELIVERY_ID";
/*  100 */   private static final Map<Long, Delivery> deliveryQueue = new ConcurrentHashMap<>();
/*  101 */   private static final Map<Long, Delivery> containerDelivery = new ConcurrentHashMap<>();
/*      */   
/*      */   private final long deliveryId;
/*      */   private byte state;
/*      */   private final long collectionWaystoneId;
/*      */   private long containerId;
/*      */   private final int crates;
/*      */   private final long senderId;
/*      */   private final long senderCost;
/*      */   private final long receiverId;
/*      */   private final long receiverCost;
/*      */   private long deliveryWaystoneId;
/*      */   private long wagonerId;
/*      */   private final long tsExpiry;
/*  115 */   private long tsWaitingForAccept = 0L;
/*  116 */   private long tsAcceptedOrRejected = 0L;
/*  117 */   private long tsDeliveryStarted = 0L;
/*  118 */   private long tsPickedUp = 0L;
/*  119 */   private long tsDelivered = 0L;
/*  120 */   private long lastChecked = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Delivery(long deliveryId, byte state, long collectionWaystoneId, long containerId, int crates, long senderId, long senderCost, long receiverId, long receiverCost, long deliveryWaystoneId, long wagonerId, long tsExpiry, long tsWaitingForAccept, long tsAcceptedOrRejected, long tsDeliveryStarted, long tsPickedUp, long tsDelivered) {
/*  127 */     this.deliveryId = deliveryId;
/*  128 */     this.state = state;
/*  129 */     this.collectionWaystoneId = collectionWaystoneId;
/*  130 */     this.containerId = containerId;
/*  131 */     this.crates = crates;
/*  132 */     this.senderId = senderId;
/*  133 */     this.senderCost = senderCost;
/*  134 */     this.receiverId = receiverId;
/*  135 */     this.receiverCost = receiverCost;
/*  136 */     this.deliveryWaystoneId = deliveryWaystoneId;
/*  137 */     this.wagonerId = wagonerId;
/*  138 */     this.tsExpiry = tsExpiry;
/*  139 */     this.tsWaitingForAccept = tsWaitingForAccept;
/*  140 */     this.tsAcceptedOrRejected = tsAcceptedOrRejected;
/*  141 */     this.tsDeliveryStarted = tsDeliveryStarted;
/*  142 */     this.tsPickedUp = tsPickedUp;
/*  143 */     this.tsDelivered = tsDelivered;
/*      */     
/*  145 */     addDelivery(this);
/*  146 */     if (containerId != -10L) {
/*  147 */       containerDelivery.put(Long.valueOf(containerId), this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(Delivery otherDelivery) {
/*  160 */     if (getWhenAcceptedOrRejected() == otherDelivery.getWhenAcceptedOrRejected())
/*  161 */       return 0; 
/*  162 */     if (getWhenAcceptedOrRejected() < otherDelivery.getWhenAcceptedOrRejected()) {
/*  163 */       return -1;
/*      */     }
/*  165 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDeliveryId() {
/*  174 */     return this.deliveryId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getState() {
/*  183 */     return this.state;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCollectionWaystoneId() {
/*  192 */     return this.collectionWaystoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getContainerId() {
/*  201 */     return this.containerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clrContainerId() {
/*  210 */     this.containerId = -10L;
/*  211 */     dbUpdateDeliveryContainer(this.deliveryId, this.containerId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCrates() {
/*  220 */     return this.crates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSenderId() {
/*  229 */     return this.senderId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSenderName() {
/*  238 */     return PlayerInfoFactory.getPlayerName(this.senderId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSenderCost() {
/*  247 */     return this.senderCost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSenderCostString() {
/*  256 */     if (this.senderCost <= 0L)
/*  257 */       return "none"; 
/*  258 */     return (new Change(this.senderCost)).getChangeShortString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getReceiverId() {
/*  267 */     return this.receiverId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getReceiverName() {
/*  276 */     return PlayerInfoFactory.getPlayerName(this.receiverId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getReceiverCost() {
/*  285 */     return this.receiverCost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getReceiverCostString() {
/*  294 */     if (this.receiverCost <= 0L)
/*  295 */       return "none"; 
/*  296 */     return (new Change(this.receiverCost)).getChangeShortString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDeliveryWaystoneId() {
/*  305 */     return this.deliveryWaystoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWagonerId() {
/*  314 */     return this.wagonerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getWagonerName() {
/*  323 */     Wagoner wag = Wagoner.getWagoner(this.wagonerId);
/*  324 */     if (wag == null)
/*  325 */       return "on vacation!"; 
/*  326 */     return wag.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getWagonerState() {
/*  335 */     Wagoner wag = Wagoner.getWagoner(this.wagonerId);
/*  336 */     if (wag == null) {
/*  337 */       return "on strike!";
/*      */     }
/*  339 */     if (wag.getDeliveryId() != this.deliveryId && wag.getDeliveryId() != -10L)
/*  340 */       return "busy"; 
/*  341 */     return wag.getStateName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Item getCrateContainer() {
/*  351 */     long crateContainerId = -10L;
/*  352 */     Wagoner wag = Wagoner.getWagoner(this.wagonerId);
/*  353 */     if (wag == null) {
/*  354 */       crateContainerId = this.containerId;
/*      */     }
/*  356 */     else if (wag.getDeliveryId() != this.deliveryId) {
/*  357 */       crateContainerId = this.containerId;
/*      */     }
/*      */     else {
/*      */       
/*  361 */       switch (wag.getState()) {
/*      */ 
/*      */         
/*      */         case 0:
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*  370 */           crateContainerId = this.containerId;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 7:
/*  375 */           crateContainerId = wag.getWagonId();
/*      */           break;
/*      */ 
/*      */         
/*      */         default:
/*  380 */           crateContainerId = -10L;
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  385 */     if (crateContainerId == -10L) {
/*  386 */       return null;
/*      */     }
/*      */     try {
/*  389 */       return Items.getItem(crateContainerId);
/*      */     }
/*  391 */     catch (NoSuchItemException e) {
/*      */ 
/*      */       
/*  394 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*  395 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canSeeCrates() {
/*  405 */     switch (this.state) {
/*      */       
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  411 */         return true;
/*      */     } 
/*  413 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPreDelivery() {
/*  422 */     switch (this.state) {
/*      */       
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  428 */         return true;
/*      */     } 
/*  430 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isQueued() {
/*  439 */     switch (this.state) {
/*      */       
/*      */       case 1:
/*  442 */         return true;
/*      */     } 
/*  444 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWaitingForAccept() {
/*  453 */     switch (this.state) {
/*      */       
/*      */       case 0:
/*  456 */         return true;
/*      */     } 
/*  458 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canViewDelivery() {
/*  468 */     switch (this.state) {
/*      */       
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*  473 */         return true;
/*      */     } 
/*  475 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean inProgress() {
/*  484 */     switch (this.state) {
/*      */       
/*      */       case 2:
/*      */       case 3:
/*  488 */         return true;
/*      */     } 
/*  490 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isComplete() {
/*  499 */     switch (this.state) {
/*      */       
/*      */       case 4:
/*      */       case 7:
/*  503 */         return true;
/*      */     } 
/*  505 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean wasRejected() {
/*  514 */     switch (this.state) {
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 8:
/*      */       case 11:
/*  520 */         return true;
/*      */     } 
/*  522 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWhen() {
/*  531 */     switch (this.state) {
/*      */       
/*      */       case 0:
/*  534 */         return this.tsWaitingForAccept;
/*      */       case 1:
/*  536 */         return this.tsAcceptedOrRejected;
/*      */       case 2:
/*  538 */         return this.tsDeliveryStarted;
/*      */       case 3:
/*  540 */         return this.tsPickedUp;
/*      */       case 4:
/*  542 */         return this.tsDelivered;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*  551 */         return this.tsAcceptedOrRejected;
/*      */     } 
/*  553 */     return 0L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringWhen() {
/*  562 */     long ts = getWhen();
/*  563 */     if (ts == 0L)
/*  564 */       return ""; 
/*  565 */     return this.df.format(new Date(ts));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWhenWaitingForAccept() {
/*  574 */     return this.tsWaitingForAccept;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringWaitingForAccept() {
/*  583 */     if (this.tsWaitingForAccept == 0L)
/*  584 */       return ""; 
/*  585 */     return this.df.format(new Date(this.tsWaitingForAccept));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWhenAcceptedOrRejected() {
/*  595 */     return this.tsAcceptedOrRejected;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringAcceptedOrRejected() {
/*  605 */     if (this.tsAcceptedOrRejected == 0L)
/*  606 */       return ""; 
/*  607 */     return this.df.format(new Date(this.tsAcceptedOrRejected));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWhenDeliveryStarted() {
/*  616 */     return this.tsDeliveryStarted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringDeliveryStarted() {
/*  625 */     if (this.tsDeliveryStarted == 0L)
/*  626 */       return ""; 
/*  627 */     return this.df.format(new Date(this.tsDeliveryStarted));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWhenPickedUp() {
/*  636 */     return this.tsPickedUp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringPickedUp() {
/*  645 */     if (this.tsPickedUp == 0L)
/*  646 */       return ""; 
/*  647 */     return this.df.format(new Date(this.tsPickedUp));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWhenDelivered() {
/*  656 */     return this.tsDelivered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringDelivered() {
/*  665 */     if (this.tsDelivered == 0L)
/*  666 */       return ""; 
/*  667 */     return this.df.format(new Date(this.tsDelivered));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove() {
/*  675 */     removeDelivery(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStateName() {
/*  684 */     switch (this.state) {
/*      */       
/*      */       case 0:
/*  687 */         return "waiting for accept";
/*      */       case 1:
/*  689 */         return "queued";
/*      */       case 2:
/*  691 */         return "waiting for pickup";
/*      */       case 3:
/*  693 */         return "being delivered";
/*      */       case 4:
/*  695 */         return "delivered";
/*      */       case 5:
/*  697 */         return "rejecting";
/*      */       case 6:
/*  699 */         return "timing out";
/*      */       case 11:
/*  701 */         return "timed out";
/*      */       case 7:
/*  703 */         return "completed";
/*      */       case 8:
/*  705 */         return "rejected";
/*      */       case 9:
/*      */       case 12:
/*  708 */         return "cancelling";
/*      */       case 10:
/*  710 */         return "cancelled";
/*      */     } 
/*  712 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccepted(long waystoneId) {
/*  722 */     this.tsAcceptedOrRejected = System.currentTimeMillis();
/*  723 */     this.state = 1;
/*  724 */     this.deliveryWaystoneId = waystoneId;
/*  725 */     dbUpdateDeliveryAccept(this.deliveryId, this.state, this.deliveryWaystoneId, this.tsAcceptedOrRejected);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRejected() {
/*  733 */     this.tsAcceptedOrRejected = System.currentTimeMillis();
/*  734 */     this.state = 5;
/*      */     
/*  736 */     this.wagonerId = -10L;
/*  737 */     dbUpdateDeliveryReject(this.deliveryId, this.state, this.tsAcceptedOrRejected);
/*  738 */     updateContainerVisuals();
/*  739 */     checkPayment(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCancelled() {
/*  747 */     this.tsAcceptedOrRejected = System.currentTimeMillis();
/*  748 */     this.state = 9;
/*      */     
/*  750 */     this.wagonerId = -10L;
/*  751 */     dbUpdateDeliveryReject(this.deliveryId, this.state, this.tsAcceptedOrRejected);
/*  752 */     updateContainerVisuals();
/*  753 */     checkPayment(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCancelledNoWagoner() {
/*  761 */     this.tsAcceptedOrRejected = System.currentTimeMillis();
/*  762 */     this.state = 12;
/*      */     
/*  764 */     this.wagonerId = -10L;
/*  765 */     dbUpdateDeliveryReject(this.deliveryId, this.state, this.tsAcceptedOrRejected);
/*  766 */     updateContainerVisuals();
/*  767 */     checkPayment(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimingOut() {
/*  775 */     this.tsAcceptedOrRejected = System.currentTimeMillis();
/*  776 */     this.state = 6;
/*      */     
/*  778 */     this.wagonerId = -10L;
/*  779 */     dbUpdateDeliveryReject(this.deliveryId, this.state, this.tsAcceptedOrRejected);
/*  780 */     updateContainerVisuals();
/*      */     
/*      */     try {
/*  783 */       Player player = Players.getInstance().getPlayer(getSenderId());
/*  784 */       player.getCommunicator().sendServerMessage("Delivery to " + 
/*  785 */           getReceiverName() + " timed out, was not accepted in time.", 255, 127, 127);
/*      */     }
/*  787 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  793 */       Player player = Players.getInstance().getPlayer(getReceiverId());
/*  794 */       player.getCommunicator().sendServerMessage("Delivery from " + 
/*  795 */           getSenderName() + " timed out, was not accepted in time.", 255, 127, 127);
/*      */     }
/*  797 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */     
/*  801 */     checkPayment(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStarted() {
/*  809 */     this.tsDeliveryStarted = System.currentTimeMillis();
/*  810 */     this.state = 2;
/*  811 */     dbUpdateDeliveryStarted(this.deliveryId, this.state, this.tsDeliveryStarted);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPickedUp() {
/*  819 */     this.tsPickedUp = System.currentTimeMillis();
/*  820 */     this.state = 3;
/*  821 */     dbUpdateDeliveryPickedUp(this.deliveryId, this.state, this.tsPickedUp);
/*  822 */     clrContainerId();
/*  823 */     updateContainerVisuals();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDelivered() {
/*  831 */     this.tsDelivered = System.currentTimeMillis();
/*  832 */     this.state = 4;
/*  833 */     dbUpdateDeliveryDelivered(this.deliveryId, this.state, this.tsDelivered);
/*      */ 
/*      */     
/*  836 */     int wagonersCut = getCrates() * 100;
/*  837 */     Wagoner wagoner = Wagoner.getWagoner(this.wagonerId);
/*  838 */     if (wagoner != null && wagoner.getVillageId() != -1) {
/*      */       
/*      */       try {
/*      */         
/*  842 */         Village village = Villages.getVillage(wagoner.getVillageId());
/*      */         
/*  844 */         int deedCut = (int)(wagonersCut * 0.2F);
/*      */ 
/*      */         
/*  847 */         village.plan.addMoney(deedCut);
/*  848 */         village.plan.addPayment(wagoner.getName(), wagoner.getWurmId(), deedCut);
/*  849 */         Change newch = Economy.getEconomy().getChangeFor(deedCut);
/*  850 */         village.addHistory(wagoner.getName(), "added " + newch.getChangeString() + " to upkeep");
/*  851 */         logger.log(Level.INFO, wagoner.getName() + " added " + deedCut + " irons to " + village.getName() + " upkeep.");
/*      */       }
/*  853 */       catch (NoSuchVillageException noSuchVillageException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  858 */     checkPayment(true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateContainerVisuals() {
/*      */     try {
/*  865 */       Item container = Items.getItem(this.containerId);
/*  866 */       container.updateName();
/*      */     }
/*  868 */     catch (NoSuchItemException e) {
/*      */ 
/*      */       
/*  871 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getContainerDescription() {
/*  880 */     String desc = "goods from " + getSenderName() + " to " + getReceiverName();
/*  881 */     switch (this.state) {
/*      */ 
/*      */       
/*      */       case 1:
/*  885 */         if (this.wagonerId == -10L)
/*  886 */           return desc + " [NO WAGONER}"; 
/*  887 */         return desc;
/*      */       
/*      */       case 5:
/*      */       case 8:
/*  891 */         return desc + " [REJECTED]";
/*      */       case 9:
/*      */       case 10:
/*  894 */         return desc + " [CANCELLED]";
/*      */       case 6:
/*      */       case 11:
/*  897 */         return desc + " [TIMED OUT]";
/*      */     } 
/*  899 */     return desc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean pay(long payTo, String payName, long cost, String detail, String message, byte newState) {
/*  915 */     boolean paid = (cost <= 0L);
/*  916 */     if (!paid) {
/*      */       
/*  918 */       LoginServerWebConnection lsw = new LoginServerWebConnection();
/*  919 */       if (lsw.addMoney(payTo, payName, cost, detail)) {
/*      */         
/*  921 */         paid = true;
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  926 */           Player player = Players.getInstance().getPlayer(payTo);
/*  927 */           player.getCommunicator().sendNormalServerMessage(message);
/*  928 */           Change change = Economy.getEconomy().getChangeFor(player.getMoney());
/*  929 */           player.getCommunicator().sendNormalServerMessage("You now have " + change.getChangeString() + " in the bank.");
/*  930 */           player.getCommunicator().sendNormalServerMessage("If this amount is incorrect, please wait a while since the information may not immediately be updated.");
/*      */         
/*      */         }
/*  933 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  940 */     if (paid) {
/*      */       
/*  942 */       this.state = newState;
/*  943 */       dbUpdateState(this.deliveryId, this.state);
/*      */     } 
/*  945 */     return paid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWagonerId(long wagonerId) {
/*  954 */     this.wagonerId = wagonerId;
/*  955 */     dbUpdateDeliveryDismiss(this.deliveryId, this.wagonerId);
/*  956 */     updateContainerVisuals();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void checkPayment(boolean force) {
/*      */     boolean paid;
/*      */     int deliveryCost;
/*  964 */     long now = System.currentTimeMillis();
/*  965 */     if (!force && now < this.lastChecked + 60000L)
/*      */       return; 
/*  967 */     this.lastChecked = now;
/*      */     
/*  969 */     switch (this.state) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*  974 */         if (now > this.tsExpiry) {
/*  975 */           setTimingOut();
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 12:
/*  981 */         paid = pay(this.receiverId, getReceiverName(), this.receiverCost, "Refund goods cost for delivery " + 
/*  982 */             getDeliveryId() + " as cancelled.", 
/*  983 */             getSenderName() + " has cancelled the delivery as no wagoner.", (byte)9);
/*  984 */         if (!paid) {
/*      */           break;
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       case 9:
/*  991 */         pay(this.senderId, getSenderName(), this.senderCost, "Refund delivery " + getDeliveryId() + " as cancelled.", "You have cancelled the delivery to " + 
/*  992 */             getReceiverName() + ".", (byte)10);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/*  998 */         pay(this.senderId, getSenderName(), this.senderCost, "Refund delivery " + getDeliveryId() + " as rejected.", "Your delivery to " + 
/*  999 */             getReceiverName() + " has been rejected.", (byte)8);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/* 1005 */         pay(this.senderId, getSenderName(), this.senderCost, "Refund delivery " + getDeliveryId() + " as timed out.", "Your delivery to " + 
/* 1006 */             getReceiverName() + " has timed out.", (byte)11);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/* 1012 */         deliveryCost = (this.senderCost == 0L) ? (getCrates() * 100) : 0;
/* 1013 */         pay(this.senderId, getSenderName(), this.receiverCost - deliveryCost, "Delivery " + getDeliveryId() + " paid.", "Your delivery to " + 
/* 1014 */             getReceiverName() + " has been paid.", (byte)7);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void removeDelivery(Delivery delivery) {
/* 1029 */     deliveryQueue.remove(Long.valueOf(delivery.getDeliveryId()));
/* 1030 */     dbRemoveDelivery(delivery.getDeliveryId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Delivery getDelivery(long deliveryId) {
/* 1041 */     return deliveryQueue.get(Long.valueOf(deliveryId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void addDelivery(Delivery delivery) {
/* 1051 */     deliveryQueue.put(Long.valueOf(delivery.getDeliveryId()), delivery);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void addDelivery(long collectionWaystoneId, long containerId, int crates, long senderId, long senderCost, long receiverId, long receiverCost, long wagonerId) {
/* 1072 */     byte state = 0;
/* 1073 */     long deliveryWaystoneId = -10L;
/* 1074 */     long tsExpiry = System.currentTimeMillis() + 604800000L;
/* 1075 */     long tsWaitingForAccept = System.currentTimeMillis();
/* 1076 */     long tsAcceptedOrRejected = 0L;
/* 1077 */     long tsDeliveryStarted = 0L;
/* 1078 */     long tsPickedUp = 0L;
/* 1079 */     long tsDelivered = 0L;
/*      */     
/* 1081 */     long deliveryId = dbCreateDelivery((byte)0, collectionWaystoneId, containerId, crates, senderId, senderCost, receiverId, receiverCost, -10L, wagonerId, tsExpiry, tsWaitingForAccept, 0L, 0L, 0L, 0L);
/*      */ 
/*      */ 
/*      */     
/* 1085 */     new Delivery(deliveryId, (byte)0, collectionWaystoneId, containerId, crates, senderId, senderCost, receiverId, receiverCost, -10L, wagonerId, tsExpiry, tsWaitingForAccept, 0L, 0L, 0L, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Delivery[] getWaitingDeliveries(long playerId) {
/* 1098 */     Set<Delivery> deliverySet = new HashSet<>();
/* 1099 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1101 */       if (delivery.isWaitingForAccept() && delivery.getReceiverId() == playerId)
/* 1102 */         deliverySet.add(delivery); 
/*      */     } 
/* 1104 */     return deliverySet.<Delivery>toArray(new Delivery[deliverySet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Delivery[] getLostDeliveries(long playerId) {
/* 1115 */     Set<Delivery> deliverySet = new HashSet<>();
/* 1116 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1118 */       if (delivery.isQueued() && delivery.getSenderId() == playerId && delivery.getWagonerId() == -10L)
/* 1119 */         deliverySet.add(delivery); 
/*      */     } 
/* 1121 */     return deliverySet.<Delivery>toArray(new Delivery[deliverySet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Delivery[] getPendingDeliveries(long playerId) {
/* 1132 */     Set<Delivery> deliverySet = new HashSet<>();
/* 1133 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1135 */       if (delivery
/* 1136 */         .getReceiverId() == playerId || delivery.getSenderId() == playerId)
/* 1137 */         deliverySet.add(delivery); 
/*      */     } 
/* 1139 */     return deliverySet.<Delivery>toArray(new Delivery[deliverySet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int countWaitingForAccept(long wagonerId) {
/* 1149 */     int count = 0;
/* 1150 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1152 */       if (delivery.isWaitingForAccept() && delivery.getWagonerId() == wagonerId)
/*      */       {
/* 1154 */         count++;
/*      */       }
/*      */     } 
/* 1157 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void rejectWaitingForAccept(long wagonerId) {
/* 1168 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1170 */       if (delivery.isWaitingForAccept() && delivery.getWagonerId() == wagonerId)
/*      */       {
/* 1172 */         delivery.setRejected();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void clrWagonerQueue(long wagonerId) {
/* 1185 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1187 */       if (delivery.isQueued() && delivery.getWagonerId() == wagonerId)
/*      */       {
/* 1189 */         delivery.setWagonerId(-10L);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final LinkedList<Delivery> getNextDeliveriesFor(long wagonerId) {
/* 1201 */     LinkedList<Delivery> deliveries = new LinkedList<>();
/* 1202 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1204 */       if (delivery.isQueued() && delivery.getWagonerId() == wagonerId)
/*      */       {
/* 1206 */         deliveries.add(delivery);
/*      */       }
/*      */     } 
/*      */     
/* 1210 */     Collections.sort(deliveries);
/* 1211 */     return deliveries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean hasNextDeliveryFor(long wagonerId) {
/* 1221 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1223 */       if (delivery.isQueued() && delivery.getWagonerId() == wagonerId)
/* 1224 */         return true; 
/*      */     } 
/* 1226 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Delivery[] getKnownDeliveries(long playerId) {
/* 1236 */     Set<Delivery> deliverySet = new HashSet<>();
/* 1237 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1239 */       if (delivery.getSenderId() == playerId || delivery.getReceiverId() == playerId)
/* 1240 */         deliverySet.add(delivery); 
/*      */     } 
/* 1242 */     return deliverySet.<Delivery>toArray(new Delivery[deliverySet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getQueueLength(long wagonerId) {
/* 1252 */     int queueLength = 0;
/* 1253 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1255 */       if (delivery.isQueued() && delivery.getWagonerId() == wagonerId)
/* 1256 */         queueLength++; 
/*      */     } 
/* 1258 */     return queueLength;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Delivery[] getDeliveriesFor(long wagonerId, boolean inQueue, boolean waitAccept, boolean inProgress, boolean rejected, boolean delivered) {
/* 1264 */     Set<Delivery> deliverySet = new HashSet<>();
/* 1265 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1267 */       if (delivery.getWagonerId() == wagonerId) {
/*      */         
/* 1269 */         if (inQueue && delivery.isQueued())
/* 1270 */           deliverySet.add(delivery); 
/* 1271 */         if (waitAccept && delivery.isWaitingForAccept())
/* 1272 */           deliverySet.add(delivery); 
/* 1273 */         if (inProgress && delivery.inProgress())
/* 1274 */           deliverySet.add(delivery); 
/* 1275 */         if (rejected && delivery.wasRejected())
/* 1276 */           deliverySet.add(delivery); 
/* 1277 */         if (delivered && delivery.isComplete())
/* 1278 */           deliverySet.add(delivery); 
/*      */       } 
/*      */     } 
/* 1281 */     return deliverySet.<Delivery>toArray(new Delivery[deliverySet.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Delivery getDeliveryFrom(long containerId) {
/* 1293 */     return containerDelivery.get(Long.valueOf(containerId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Delivery canViewDelivery(Item container, Creature creature) {
/* 1306 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1308 */       if (delivery.getContainerId() == container.getWurmId())
/*      */       {
/*      */         
/* 1311 */         if (delivery.canViewDelivery())
/*      */         {
/* 1313 */           if (delivery.getSenderId() == creature.getWurmId() || creature.getPower() >= 2)
/* 1314 */             return delivery; 
/*      */         }
/*      */       }
/*      */     } 
/* 1318 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final boolean canUnSealContainer(Item container, Creature creature) {
/* 1331 */     Delivery delivery = getDeliveryFrom(container.getWurmId());
/* 1332 */     if (delivery != null)
/*      */     {
/* 1334 */       if (!delivery.canViewDelivery() || delivery.getWagonerId() == -10L)
/*      */       {
/* 1336 */         if (delivery.getSenderId() == creature.getWurmId() || creature.getPower() >= 2)
/* 1337 */           return true; 
/*      */       }
/*      */     }
/* 1340 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isDeliveryPoint(long waystoneId) {
/* 1350 */     for (Delivery delivery : deliveryQueue.values()) {
/*      */       
/* 1352 */       if (delivery.getDeliveryWaystoneId() == waystoneId)
/*      */       {
/*      */         
/* 1355 */         if (delivery.isPreDelivery())
/*      */         {
/* 1357 */           return true;
/*      */         }
/*      */       }
/*      */     } 
/* 1361 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void freeContainer(long containerId) {
/* 1366 */     Delivery delivery = getDeliveryFrom(containerId);
/* 1367 */     if (delivery != null) {
/*      */       
/* 1369 */       delivery.clrContainerId();
/* 1370 */       containerDelivery.remove(Long.valueOf(containerId));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getContainerDescription(long containerId) {
/* 1382 */     Delivery delivery = getDeliveryFrom(containerId);
/* 1383 */     if (delivery != null)
/*      */     {
/* 1385 */       return delivery.getContainerDescription();
/*      */     }
/* 1387 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void poll() {
/* 1395 */     for (Delivery delivery : deliveryQueue.values())
/*      */     {
/* 1397 */       delivery.checkPayment(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void dbLoadAllDeliveries() {
/* 1406 */     logger.log(Level.INFO, "Loading all deliveries from delivery queue.");
/* 1407 */     long start = System.nanoTime();
/* 1408 */     long count = 0L;
/* 1409 */     Connection dbcon = null;
/* 1410 */     PreparedStatement ps = null;
/* 1411 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1414 */       dbcon = DbConnector.getCreatureDbCon();
/* 1415 */       ps = dbcon.prepareStatement("SELECT * FROM DELIVERYQUEUE ORDER BY DELIVERY_ID");
/* 1416 */       rs = ps.executeQuery();
/* 1417 */       while (rs.next())
/*      */       {
/* 1419 */         long deliveryId = rs.getLong("DELIVERY_ID");
/* 1420 */         byte state = rs.getByte("STATE");
/* 1421 */         long collectionWaystoneId = rs.getLong("COLLECTION_WAYSTONE_ID");
/* 1422 */         long containerId = rs.getLong("CONTAINER_ID");
/* 1423 */         byte crates = rs.getByte("CRATES");
/* 1424 */         long senderId = rs.getLong("SENDER_ID");
/* 1425 */         long senderCost = rs.getLong("SENDER_COST");
/* 1426 */         long receiverId = rs.getLong("RECEIVER_ID");
/* 1427 */         long receiverCost = rs.getLong("RECEIVER_COST");
/* 1428 */         long deliveryWaystoneId = rs.getLong("DELIVERY_WAYSTONE_ID");
/* 1429 */         long wagonerId = rs.getLong("WAGONER_ID");
/* 1430 */         long tsExpiry = rs.getLong("TS_EXPIRY");
/* 1431 */         long tsWaitingForAccept = rs.getLong("TS_WAITING_FOR_ACCEPT");
/* 1432 */         long tsAcceptedOrRejected = rs.getLong("TS_ACCEPTED_OR_REJECTED");
/* 1433 */         long tsDeliveryStarted = rs.getLong("TS_DELIVERY_STARTED");
/* 1434 */         long tsPickedUp = rs.getLong("TS_PICKED_UP");
/* 1435 */         long tsDelivered = rs.getLong("TS_DELIVERED");
/*      */         
/* 1437 */         new Delivery(deliveryId, state, collectionWaystoneId, containerId, crates, senderId, senderCost, receiverId, receiverCost, deliveryWaystoneId, wagonerId, tsExpiry, tsWaitingForAccept, tsAcceptedOrRejected, tsDeliveryStarted, tsPickedUp, tsDelivered);
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1442 */     catch (SQLException sqex) {
/*      */       
/* 1444 */       logger.log(Level.WARNING, "Failed to load all deliveries: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1448 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1449 */       DbConnector.returnConnection(dbcon);
/* 1450 */       long end = System.nanoTime();
/* 1451 */       logger.log(Level.INFO, "Loaded " + count + " deliveries from delivery queue. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long dbCreateDelivery(byte state, long collectionWaystoneId, long containerId, int crates, long senderId, long senderCost, long receiverId, long receiverCost, long deliveryWaystoneId, long wagonerId, long tsExpiry, long tsWaitingForAccept, long tsAcceptedOrRejected, long tsDeliveryStarted, long tsPickedUp, long tsDelivered) {
/* 1482 */     long deliveryId = -10L;
/* 1483 */     Connection dbcon = null;
/* 1484 */     PreparedStatement ps = null;
/* 1485 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1488 */       dbcon = DbConnector.getCreatureDbCon();
/* 1489 */       ps = dbcon.prepareStatement("INSERT INTO DELIVERYQUEUE (STATE,COLLECTION_WAYSTONE_ID,CONTAINER_ID,CRATES,SENDER_ID,SENDER_COST,RECEIVER_ID,RECEIVER_COST,DELIVERY_WAYSTONE_ID,WAGONER_ID,TS_EXPIRY,TS_WAITING_FOR_ACCEPT,TS_ACCEPTED_OR_REJECTED,TS_DELIVERY_STARTED,TS_PICKED_UP,TS_DELIVERED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/* 1490 */       ps.setByte(1, state);
/* 1491 */       ps.setLong(2, collectionWaystoneId);
/* 1492 */       ps.setLong(3, containerId);
/* 1493 */       ps.setByte(4, (byte)crates);
/* 1494 */       ps.setLong(5, senderId);
/* 1495 */       ps.setLong(6, senderCost);
/* 1496 */       ps.setLong(7, receiverId);
/* 1497 */       ps.setLong(8, receiverCost);
/* 1498 */       ps.setLong(9, deliveryWaystoneId);
/* 1499 */       ps.setLong(10, wagonerId);
/* 1500 */       ps.setLong(11, tsExpiry);
/* 1501 */       ps.setLong(12, tsWaitingForAccept);
/* 1502 */       ps.setLong(13, tsAcceptedOrRejected);
/* 1503 */       ps.setLong(14, tsDeliveryStarted);
/* 1504 */       ps.setLong(15, tsPickedUp);
/* 1505 */       ps.setLong(16, tsDelivered);
/* 1506 */       ps.executeUpdate();
/*      */       
/* 1508 */       rs = ps.getGeneratedKeys();
/* 1509 */       if (rs.next())
/*      */       {
/* 1511 */         deliveryId = rs.getLong(1);
/*      */       }
/*      */     }
/* 1514 */     catch (SQLException ex) {
/*      */       
/* 1516 */       logger.log(Level.WARNING, "Failed to create delivery in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1520 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1521 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1523 */     return deliveryId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbRemoveDelivery(long deliveryId) {
/* 1533 */     Connection dbcon = null;
/* 1534 */     PreparedStatement ps = null;
/* 1535 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1538 */       dbcon = DbConnector.getCreatureDbCon();
/* 1539 */       ps = dbcon.prepareStatement("DELETE FROM DELIVERYQUEUE WHERE DELIVERY_ID=?");
/* 1540 */       ps.setLong(1, deliveryId);
/* 1541 */       ps.executeUpdate();
/*      */     }
/* 1543 */     catch (SQLException ex) {
/*      */       
/* 1545 */       logger.log(Level.WARNING, "Failed to remove delivery " + deliveryId + " from deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1549 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1550 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateDeliveryAccept(long deliveryId, byte state, long waystoneId, long tsAcceptOrReject) {
/* 1564 */     Connection dbcon = null;
/* 1565 */     PreparedStatement ps = null;
/* 1566 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1569 */       dbcon = DbConnector.getCreatureDbCon();
/* 1570 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET STATE=?,DELIVERY_WAYSTONE_ID=?,TS_ACCEPTED_OR_REJECTED=? WHERE DELIVERY_ID=?");
/* 1571 */       ps.setByte(1, state);
/* 1572 */       ps.setLong(2, waystoneId);
/* 1573 */       ps.setLong(3, tsAcceptOrReject);
/* 1574 */       ps.setLong(4, deliveryId);
/* 1575 */       ps.executeUpdate();
/*      */     }
/* 1577 */     catch (SQLException ex) {
/*      */       
/* 1579 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to state " + state + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1583 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1584 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateDeliveryReject(long deliveryId, byte state, long tsAcceptOrReject) {
/* 1597 */     Connection dbcon = null;
/* 1598 */     PreparedStatement ps = null;
/* 1599 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1602 */       dbcon = DbConnector.getCreatureDbCon();
/* 1603 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET STATE=?,TS_ACCEPTED_OR_REJECTED=?,WAGONER_ID=? WHERE DELIVERY_ID=?");
/* 1604 */       ps.setByte(1, state);
/* 1605 */       ps.setLong(2, tsAcceptOrReject);
/* 1606 */       ps.setLong(3, -10L);
/* 1607 */       ps.setLong(4, deliveryId);
/* 1608 */       ps.executeUpdate();
/*      */     }
/* 1610 */     catch (SQLException ex) {
/*      */       
/* 1612 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to state " + state + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1616 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1617 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateDeliveryDismiss(long deliveryId, long wagonerId) {
/* 1628 */     Connection dbcon = null;
/* 1629 */     PreparedStatement ps = null;
/* 1630 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1633 */       dbcon = DbConnector.getCreatureDbCon();
/* 1634 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET WAGONER_ID=? WHERE DELIVERY_ID=?");
/* 1635 */       ps.setLong(1, wagonerId);
/* 1636 */       ps.setLong(2, deliveryId);
/* 1637 */       ps.executeUpdate();
/*      */     }
/* 1639 */     catch (SQLException ex) {
/*      */       
/* 1641 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to wagoner " + wagonerId + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1645 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1646 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateDeliveryContainer(long deliveryId, long containerId) {
/* 1657 */     Connection dbcon = null;
/* 1658 */     PreparedStatement ps = null;
/* 1659 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1662 */       dbcon = DbConnector.getCreatureDbCon();
/* 1663 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET CONTAINER_ID=? WHERE DELIVERY_ID=?");
/* 1664 */       ps.setLong(1, containerId);
/* 1665 */       ps.setLong(2, deliveryId);
/* 1666 */       ps.executeUpdate();
/*      */     }
/* 1668 */     catch (SQLException ex) {
/*      */       
/* 1670 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to container " + containerId + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1674 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1675 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateDeliveryStarted(long deliveryId, byte state, long tsStarted) {
/* 1688 */     Connection dbcon = null;
/* 1689 */     PreparedStatement ps = null;
/* 1690 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1693 */       dbcon = DbConnector.getCreatureDbCon();
/* 1694 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET STATE=?,TS_DELIVERY_STARTED=? WHERE DELIVERY_ID=?");
/* 1695 */       ps.setByte(1, state);
/* 1696 */       ps.setLong(2, tsStarted);
/* 1697 */       ps.setLong(3, deliveryId);
/* 1698 */       ps.executeUpdate();
/*      */     }
/* 1700 */     catch (SQLException ex) {
/*      */       
/* 1702 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to state " + state + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1706 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1707 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateDeliveryPickedUp(long deliveryId, byte state, long tsPickedUp) {
/* 1720 */     Connection dbcon = null;
/* 1721 */     PreparedStatement ps = null;
/* 1722 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1725 */       dbcon = DbConnector.getCreatureDbCon();
/* 1726 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET STATE=?,TS_PICKED_UP=? WHERE DELIVERY_ID=?");
/* 1727 */       ps.setByte(1, state);
/* 1728 */       ps.setLong(2, tsPickedUp);
/* 1729 */       ps.setLong(3, deliveryId);
/* 1730 */       ps.executeUpdate();
/*      */     }
/* 1732 */     catch (SQLException ex) {
/*      */       
/* 1734 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to state " + state + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1738 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1739 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateDeliveryDelivered(long deliveryId, byte state, long tsDelivered) {
/* 1752 */     Connection dbcon = null;
/* 1753 */     PreparedStatement ps = null;
/* 1754 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1757 */       dbcon = DbConnector.getCreatureDbCon();
/* 1758 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET STATE=?,TS_DELIVERED=? WHERE DELIVERY_ID=?");
/* 1759 */       ps.setByte(1, state);
/* 1760 */       ps.setLong(2, tsDelivered);
/* 1761 */       ps.setLong(3, deliveryId);
/* 1762 */       ps.executeUpdate();
/*      */     }
/* 1764 */     catch (SQLException ex) {
/*      */       
/* 1766 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to state " + state + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1770 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1771 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateState(long deliveryId, byte state) {
/* 1784 */     Connection dbcon = null;
/* 1785 */     PreparedStatement ps = null;
/* 1786 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1789 */       dbcon = DbConnector.getCreatureDbCon();
/* 1790 */       ps = dbcon.prepareStatement("UPDATE DELIVERYQUEUE SET STATE=? WHERE DELIVERY_ID=?");
/* 1791 */       ps.setByte(1, state);
/* 1792 */       ps.setLong(2, deliveryId);
/* 1793 */       ps.executeUpdate();
/*      */     }
/* 1795 */     catch (SQLException ex) {
/*      */       
/* 1797 */       logger.log(Level.WARNING, "Failed to update delivery " + deliveryId + " to state " + state + " in deliveryQueue table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1801 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1802 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Delivery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */