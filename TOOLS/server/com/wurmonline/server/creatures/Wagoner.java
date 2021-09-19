/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.highways.PathToCalculate;
/*      */ import com.wurmonline.server.highways.Route;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
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
/*      */ public class Wagoner
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*   56 */   private static final Logger logger = Logger.getLogger(Wagoner.class.getName());
/*      */   
/*      */   public static final byte STATE_IDLE = 0;
/*      */   
/*      */   public static final byte STATE_GOING_TO_SLEEP = 1;
/*      */   
/*      */   public static final byte STATE_SLEEPING = 2;
/*      */   
/*      */   public static final byte STATE_WAKING_UP = 3;
/*      */   
/*      */   public static final byte STATE_GETTING_READY = 4;
/*      */   
/*      */   public static final byte STATE_DRIVING_TO_COLLECTION_POINT = 5;
/*      */   
/*      */   public static final byte STATE_LOADING = 6;
/*      */   
/*      */   public static final byte STATE_DELIVERING = 7;
/*      */   
/*      */   public static final byte STATE_UNLOADING = 8;
/*      */   
/*      */   public static final byte STATE_GOING_HOME = 9;
/*      */   
/*      */   public static final byte STATE_PARKING = 10;
/*      */   
/*      */   public static final byte STUCK_COLLECTING = 11;
/*      */   
/*      */   public static final byte STUCK_DELIVERING = 12;
/*      */   
/*      */   public static final byte STUCK_GOING_HOME = 13;
/*      */   public static final byte TEST_WAITING = 14;
/*      */   public static final byte TEST_DRIVING = 15;
/*      */   public static final int MAX_NOT_MOVING = 30;
/*      */   public static final byte SPEECH_CHATTYNESS_SELDOM = 0;
/*      */   public static final byte SPEECH_CHATTYNESS_SILENT = 1;
/*      */   public static final byte SPEECH_CHATTYNESS_NOISY = 2;
/*      */   public static final byte SPEECH_CHATTYNESS_RANDOM = 3;
/*      */   public static final byte SPEECH_CONTEXT_TEST = 1;
/*      */   public static final byte SPEECH_CONTEXT_ERROR = 2;
/*      */   public static final byte SPEECH_CONTEXT_FOOD = 4;
/*      */   public static final byte SPEECH_CONTEXT_WORK = 8;
/*      */   public static final byte SPEECH_CONTEXT_SLEEP = 16;
/*      */   public static final byte SPEECH_CONTEXT_RANDOM = 32;
/*      */   public static final byte SPEECH_STYLE_NORMAL = 0;
/*      */   public static final byte SPEECH_STYLE_WHITTY = 1;
/*      */   public static final byte SPEECH_STYLE_GRUMPY = 2;
/*      */   public static final byte SPEECH_STYLE_CHEERY = 3;
/*      */   private static final String CREATE_WAGONER = "INSERT INTO WAGONER (WURMID,STATE,OWNERID,CONTRACT_ID,HOME_WAYSTONE_ID,HOME_VILLAGE_ID,WAGON_ID,RESTING_PLACE_ID,CHAIR_ID,TENT_ID,BED_ID,DELIVERY_ID,WAGON_POSX,WAGON_POSY,WAGON_ON_SURFACE,CAMP_ROT,LAST_WAYSTONE_ID,GOAL_WAYSTONE_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */   private static final String UPDATE_WAGONER_STATE = "UPDATE WAGONER SET STATE=? WHERE WURMID=?";
/*      */   private static final String UPDATE_WAGONER_DELIVERY = "UPDATE WAGONER SET DELIVERY_ID=?, STATE=? WHERE WURMID=?";
/*      */   private static final String UPDATE_WAGONER_LAST_ID = "UPDATE WAGONER SET LAST_WAYSTONE_ID=? WHERE WURMID=?";
/*      */   private static final String UPDATE_WAGONER_GOAL_ID = "UPDATE WAGONER SET GOAL_WAYSTONE_ID=? WHERE WURMID=?";
/*      */   private static final String UPDATE_WAGONER_CHAT_OPTIONS = "UPDATE WAGONER SET CHAT=? WHERE WURMID=?";
/*      */   private static final String DELETE_WAGONER = "DELETE FROM WAGONER WHERE WURMID=?";
/*      */   private static final String GET_ALL_WAGONERS = "SELECT * FROM WAGONER";
/*  110 */   private static final Map<Long, Wagoner> wagoners = new ConcurrentHashMap<>();
/*      */   
/*      */   private long wagonerId;
/*      */   
/*      */   private byte state;
/*      */   
/*      */   private long ownerId;
/*      */   
/*      */   private long contractId;
/*      */   private long homeWaystoneId;
/*      */   private int homeVillageId;
/*      */   private long wagonId;
/*      */   private long restingPlaceId;
/*      */   private long chairId;
/*      */   private long tentId;
/*      */   private long bedId;
/*      */   private long deliveryId;
/*      */   private float wagonPosX;
/*      */   private float wagonPosY;
/*      */   private boolean wagonOnSurface;
/*      */   private float campRot;
/*  131 */   private long lastWaystoneId = -10L;
/*  132 */   private long goalWaystoneId = -10L;
/*  133 */   private List<Route> path = null;
/*  134 */   private LinkedList<Item> currentCatseyes = null;
/*      */   
/*      */   private boolean updateCatseyes = false;
/*  137 */   private LinkedList<Item> catseyesCollecting = null;
/*  138 */   private LinkedList<Item> catseyesDelivering = null;
/*  139 */   private LinkedList<Item> catseyesReturning = null;
/*      */   
/*  141 */   private Item wagon = null;
/*  142 */   private Creature creature = null;
/*      */   private boolean forceStateChange = false;
/*  144 */   private byte forcedNewState = 0;
/*      */   
/*      */   private final Random rnd;
/*  147 */   private long lazy = 0L;
/*  148 */   private long lastCheck = 0L;
/*  149 */   private byte subState = 0;
/*  150 */   private int notMoving = 0;
/*  151 */   private int lastDir = -1;
/*  152 */   private int tilex = 0;
/*  153 */   private int tiley = 0;
/*  154 */   private int tileCount = 0;
/*      */   
/*  156 */   private byte speechChattyness = 0;
/*      */   private boolean speechContextFood = false;
/*      */   private boolean speechContextWork = false;
/*      */   private boolean speechContextSleep = false;
/*      */   private boolean speechContextRandom = false;
/*  161 */   private byte speechStyle = 0;
/*  162 */   private int randomness = 1;
/*  163 */   private long chatDelay = 0L;
/*  164 */   private long lastChat = 0L;
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
/*      */   public Wagoner(long wagonerId, byte state, long ownerId, long contractId, long homeWaystoneId, int homeVillageId, long wagonId, long restingPlaceId, long chairId, long tentId, long bedId, long deliveryId, float wagonPosX, float wagonPosY, boolean wagonOnSurface, float campRot, long lastWaystoneId, long goalWaystoneId, byte speachChatty, boolean speachFood, boolean speachWork, boolean speachSleep, boolean speachRandom, byte speachType) {
/*  176 */     this.wagonerId = wagonerId;
/*  177 */     this.state = state;
/*  178 */     this.ownerId = ownerId;
/*  179 */     this.contractId = contractId;
/*  180 */     this.homeWaystoneId = homeWaystoneId;
/*  181 */     this.homeVillageId = homeVillageId;
/*  182 */     this.wagonId = wagonId;
/*  183 */     this.restingPlaceId = restingPlaceId;
/*  184 */     this.chairId = chairId;
/*  185 */     this.tentId = tentId;
/*  186 */     this.bedId = bedId;
/*  187 */     this.deliveryId = deliveryId;
/*      */     
/*  189 */     this.wagonPosX = wagonPosX;
/*  190 */     this.wagonPosY = wagonPosY;
/*  191 */     this.wagonOnSurface = wagonOnSurface;
/*  192 */     this.campRot = campRot;
/*      */     
/*  194 */     this.lastWaystoneId = lastWaystoneId;
/*  195 */     this.goalWaystoneId = goalWaystoneId;
/*      */     
/*  197 */     this.rnd = new Random(wagonerId);
/*  198 */     this.speechChattyness = speachChatty;
/*  199 */     this.speechContextFood = speachFood;
/*  200 */     this.speechContextWork = speachWork;
/*  201 */     this.speechContextSleep = speachSleep;
/*  202 */     this.speechContextRandom = speachRandom;
/*  203 */     this.speechStyle = speachType;
/*  204 */     this.randomness = this.rnd.nextInt(5) + 5;
/*      */     
/*      */     try {
/*  207 */       this.wagon = Items.getItem(this.wagonId);
/*  208 */       this.wagon.setWagonerWagon(true);
/*  209 */       this.wagon.setDamage(0.0F);
/*  210 */       addWagoner(this);
/*      */     }
/*  212 */     catch (NoSuchItemException e) {
/*      */       
/*  214 */       logger.log(Level.WARNING, "Wagoner wagon (" + this.wagonId + ") missing! " + e.getMessage(), (Throwable)e);
/*      */     } 
/*      */     
/*  217 */     if (this.deliveryId != -10L) {
/*  218 */       grabDeliveryCatseyes(this.deliveryId);
/*      */     }
/*  220 */     this.lazy = 60000L + 3000L * this.rnd.nextInt(60);
/*  221 */     setChatGap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWurmId() {
/*  230 */     return this.wagonerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getState() {
/*  239 */     return this.state;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStateName() {
/*  248 */     return getStateName(this.state);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIdle() {
/*  257 */     switch (this.state) {
/*      */       
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  263 */         return true;
/*      */     } 
/*  265 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getOwnerId() {
/*  274 */     return this.ownerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOwnerId(long newOwnerId) {
/*  283 */     this.ownerId = newOwnerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getContractId() {
/*  292 */     return this.contractId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getHomeWaystoneId() {
/*  301 */     return this.homeWaystoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getVillageId() {
/*  310 */     return this.homeVillageId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWagonId() {
/*  319 */     return this.wagonId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item getWagon() {
/*  328 */     return this.wagon;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getRestingPlaceId() {
/*  337 */     return this.restingPlaceId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getChairId() {
/*  346 */     return this.chairId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTentId() {
/*  355 */     return this.tentId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getBedId() {
/*  364 */     return this.bedId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDeliveryId() {
/*  373 */     return this.deliveryId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWagonPosX() {
/*  383 */     return this.wagonPosX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWagonPosY() {
/*  393 */     return this.wagonPosY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getWagonOnSurface() {
/*  403 */     return this.wagonOnSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCampRot() {
/*  413 */     return this.campRot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastWaystoneId() {
/*  423 */     return this.lastWaystoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastWaystoneId(long newLastWaystoneId) {
/*  432 */     this.lastWaystoneId = newLastWaystoneId;
/*  433 */     dbUpdateLastWaystoneId(this.wagonerId, this.lastWaystoneId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getGoalWaystoneId() {
/*  444 */     return this.goalWaystoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGoalWaystoneId(long newGoalWaystoneId) {
/*  454 */     this.goalWaystoneId = newGoalWaystoneId;
/*  455 */     dbUpdateGoalWaystoneId(this.wagonerId, this.goalWaystoneId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void calculateRoute(long newLastWaystoneId, long newGoalWaystoneId) {
/*  466 */     setGoalWaystoneId(newGoalWaystoneId);
/*  467 */     calculateRoute(newLastWaystoneId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void calculateRoute(long newLastWaystoneId) {
/*  477 */     setLastWaystoneId(newLastWaystoneId);
/*  478 */     calculateRoute();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void calculateRoute() {
/*  486 */     if (this.lastWaystoneId == this.goalWaystoneId) {
/*  487 */       this.path = null;
/*      */     } else {
/*      */       
/*  490 */       this.path = PathToCalculate.getRoute(this.lastWaystoneId, this.goalWaystoneId);
/*  491 */       this.updateCatseyes = hasPath();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public LinkedList<Item> getCurrentCatseyes() {
/*  497 */     return this.currentCatseyes;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean maybeUpdateCatseyes() {
/*  502 */     if (this.path == null) {
/*      */       
/*  504 */       this.updateCatseyes = false;
/*  505 */       return false;
/*      */     } 
/*  507 */     if (this.updateCatseyes) {
/*      */       
/*  509 */       LinkedList<Item> catseyes = new LinkedList<>();
/*      */       
/*  511 */       while (!this.path.isEmpty()) {
/*      */         
/*  513 */         Route actualRoute = this.path.remove(0);
/*  514 */         catseyes.addAll(actualRoute.getCatseyesListCopy());
/*      */         
/*  516 */         catseyes.add(actualRoute.getEndNode().getWaystone());
/*      */       } 
/*  518 */       this.currentCatseyes = catseyes;
/*  519 */       this.updateCatseyes = false;
/*  520 */       return true;
/*      */     } 
/*  522 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updateCatseyes(Item marker) {
/*  533 */     this.path = PathToCalculate.getRoute(this.lastWaystoneId, this.goalWaystoneId);
/*  534 */     this.currentCatseyes = new LinkedList<>();
/*  535 */     while (!this.path.isEmpty()) {
/*      */       
/*  537 */       Route actualRoute = this.path.remove(0);
/*  538 */       this.currentCatseyes.addAll(actualRoute.getCatseyesListCopy());
/*      */       
/*  540 */       this.currentCatseyes.add(actualRoute.getEndNode().getWaystone());
/*      */     } 
/*  542 */     if (this.currentCatseyes.contains(marker)) {
/*      */       
/*  544 */       while (this.currentCatseyes.getFirst() != marker) {
/*  545 */         this.currentCatseyes.removeFirst();
/*      */       }
/*      */     } else {
/*  548 */       return false;
/*  549 */     }  this.updateCatseyes = false;
/*  550 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<Route> getPath() {
/*  560 */     return this.path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPath() {
/*  569 */     return (this.path != null && !this.path.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove() {
/*  577 */     removeWagoner(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Creature getCreature() {
/*  587 */     return this.creature;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreature(Creature creature) {
/*  592 */     this.creature = creature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  601 */     if (this.creature == null) {
/*  602 */       return "Unknown";
/*      */     }
/*  604 */     return this.creature.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateState(byte newState) {
/*  613 */     if (this.state != newState) {
/*      */       
/*  615 */       this.state = newState;
/*  616 */       dbUpdateWagonerState(this.wagonerId, this.state);
/*  617 */       this.forceStateChange = false;
/*      */     } 
/*  619 */     if (newState == 6)
/*      */     {
/*      */       
/*  622 */       this.catseyesCollecting = new LinkedList<>();
/*      */     }
/*  624 */     if (newState == 8)
/*      */     {
/*      */       
/*  627 */       this.catseyesDelivering = new LinkedList<>();
/*      */     }
/*      */     
/*  630 */     if (isIdle() && this.homeVillageId == -1) {
/*      */       
/*  632 */       removeWagonerCamp();
/*      */       
/*  634 */       Delivery.rejectWaitingForAccept(this.wagonerId);
/*  635 */       Delivery.clrWagonerQueue(this.wagonerId);
/*  636 */       remove();
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
/*      */   public boolean updateDeliveryId(long newDeliveryId) {
/*  648 */     if (newDeliveryId != -10L)
/*      */     {
/*      */ 
/*      */       
/*  652 */       if (!grabDeliveryCatseyes(newDeliveryId)) {
/*      */ 
/*      */         
/*  655 */         this.catseyesCollecting = null;
/*  656 */         this.catseyesDelivering = null;
/*  657 */         this.catseyesReturning = null;
/*      */         
/*  659 */         return false;
/*      */       } 
/*      */     }
/*  662 */     this.deliveryId = newDeliveryId;
/*      */     
/*  664 */     if (newDeliveryId == -10L) {
/*      */       
/*  666 */       this.state = 0;
/*  667 */       this.catseyesCollecting = null;
/*  668 */       this.catseyesDelivering = null;
/*  669 */       this.catseyesReturning = null;
/*      */     }
/*      */     else {
/*      */       
/*  673 */       this.state = 4;
/*      */     } 
/*  675 */     dbUpdateWagonerDelivery(this.wagonerId, this.deliveryId, this.state);
/*  676 */     this.forceStateChange = false;
/*  677 */     return true;
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
/*      */   public boolean grabDeliveryCatseyes(long deliveryId) {
/*      */     List<Route> pathCollecting, pathDelivering, pathReturning;
/*  691 */     Delivery delivery = Delivery.getDelivery(deliveryId);
/*  692 */     if (delivery == null) {
/*  693 */       return false;
/*      */     }
/*  695 */     this.catseyesCollecting = new LinkedList<>();
/*  696 */     this.catseyesDelivering = new LinkedList<>();
/*  697 */     this.catseyesReturning = new LinkedList<>();
/*      */     
/*  699 */     switch (this.state) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*      */       case 4:
/*      */       case 5:
/*      */       case 11:
/*  707 */         pathCollecting = PathToCalculate.getRoute(this.homeWaystoneId, delivery.getCollectionWaystoneId());
/*  708 */         if (pathCollecting == null || pathCollecting.isEmpty())
/*  709 */           return false; 
/*  710 */         while (!pathCollecting.isEmpty()) {
/*      */           
/*  712 */           Route actualRoute = pathCollecting.remove(0);
/*  713 */           this.catseyesCollecting.addAll(actualRoute.getCatseyesListCopy());
/*      */           
/*  715 */           this.catseyesCollecting.add(actualRoute.getEndNode().getWaystone());
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 7:
/*      */       case 12:
/*  724 */         pathDelivering = PathToCalculate.getRoute(delivery.getCollectionWaystoneId(), delivery.getDeliveryWaystoneId());
/*  725 */         if (pathDelivering == null || pathDelivering.isEmpty())
/*  726 */           return false; 
/*  727 */         while (!pathDelivering.isEmpty()) {
/*      */           
/*  729 */           Route actualRoute = pathDelivering.remove(0);
/*  730 */           this.catseyesDelivering.addAll(actualRoute.getCatseyesListCopy());
/*      */           
/*  732 */           this.catseyesDelivering.add(actualRoute.getEndNode().getWaystone());
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 8:
/*      */       case 9:
/*      */       case 13:
/*  741 */         pathReturning = PathToCalculate.getRoute(delivery.getDeliveryWaystoneId(), this.homeWaystoneId);
/*  742 */         if (pathReturning == null || pathReturning.isEmpty())
/*  743 */           return false; 
/*  744 */         while (!pathReturning.isEmpty()) {
/*      */           
/*  746 */           Route actualRoute = pathReturning.remove(0);
/*  747 */           this.catseyesReturning.addAll(actualRoute.getCatseyesListCopy());
/*      */           
/*  749 */           this.catseyesReturning.add(actualRoute.getEndNode().getWaystone());
/*      */         } 
/*  751 */         return true;
/*      */     } 
/*      */     
/*  754 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean markerOnDeliveryRoute(Item marker) {
/*  764 */     if (this.deliveryId == -10L)
/*  765 */       return false; 
/*  766 */     if (this.catseyesReturning == null || this.catseyesReturning.isEmpty())
/*      */     {
/*  768 */       if (!grabDeliveryCatseyes(this.deliveryId))
/*  769 */         return false; 
/*      */     }
/*  771 */     return (this.catseyesCollecting.contains(marker) || this.catseyesDelivering
/*  772 */       .contains(marker) || this.catseyesReturning
/*  773 */       .contains(marker));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void forceStateChange(byte newState) {
/*  782 */     this.forcedNewState = newState;
/*  783 */     this.forceStateChange = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isForcedState() {
/*  792 */     return this.forceStateChange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getForcedState() {
/*  801 */     return this.forcedNewState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getQueueLength() {
/*  810 */     return Delivery.getQueueLength(this.wagonerId);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clrTileCount() {
/*  815 */     this.tileCount = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTile(int tilex, int tiley) {
/*  825 */     this.tilex = tilex;
/*  826 */     this.tiley = tiley;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean moved(int tilex, int tiley) {
/*  837 */     if (tilex != this.tilex || tiley != this.tiley) {
/*      */       
/*  839 */       setTile(tilex, tiley);
/*  840 */       this.tileCount++;
/*  841 */       return true;
/*      */     } 
/*  843 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clrVillage() {
/*  853 */     logger.log(Level.WARNING, getName() + " (" + this.wagonerId + ") removed from village id: " + this.homeVillageId + ".", new Exception());
/*      */     
/*  855 */     if (this.homeVillageId != -1) {
/*      */       
/*  857 */       this.homeVillageId = -1;
/*  858 */       Delivery.rejectWaitingForAccept(this.wagonerId);
/*  859 */       if (isIdle()) {
/*      */         
/*  861 */         removeWagonerCamp();
/*  862 */         Delivery.clrWagonerQueue(this.wagonerId);
/*  863 */         remove();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeWagonerCamp() {
/*      */     try {
/*  883 */       Creatures.getInstance().getCreature(this.wagonerId).destroy();
/*  884 */       this.creature = null;
/*      */     }
/*  886 */     catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */ 
/*      */     
/*  890 */     Items.destroyItem(this.wagonId, true);
/*  891 */     Items.destroyItem(this.chairId, true);
/*  892 */     Items.destroyItem(this.bedId, true);
/*  893 */     Items.destroyItem(this.tentId, true);
/*  894 */     Items.destroyItem(this.restingPlaceId, true);
/*      */     
/*      */     try {
/*  897 */       Item homeWaystone = Items.getItem(this.homeWaystoneId);
/*  898 */       homeWaystone.setData(-1L);
/*      */     
/*      */     }
/*  901 */     catch (NoSuchItemException e) {
/*      */ 
/*      */       
/*  904 */       logger.log(Level.WARNING, "Home Waystone is missing " + e.getMessage(), (Throwable)e);
/*      */     } 
/*      */     
/*      */     try {
/*  908 */       Item contract = Items.getItem(this.contractId);
/*  909 */       contract.setData(-1L);
/*      */       
/*  911 */       contract.setDescription("");
/*      */     }
/*  913 */     catch (NoSuchItemException e) {
/*      */ 
/*      */       
/*  916 */       logger.log(Level.WARNING, "Wagoner Contract is missing " + e.getMessage(), (Throwable)e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void say(Speech speech) {
/*  927 */     say(speech, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void say(Speech speech, @Nullable Delivery delivery) {
/*  938 */     this.lastChat = WurmCalendar.getCurrentTime();
/*  939 */     boolean inContext = true;
/*  940 */     switch (speech.getContext()) {
/*      */       
/*      */       case 4:
/*  943 */         inContext = this.speechContextFood;
/*      */         break;
/*      */       case 8:
/*  946 */         inContext = this.speechContextWork;
/*      */         break;
/*      */       case 16:
/*  949 */         inContext = this.speechContextSleep;
/*      */         break;
/*      */       case 1:
/*  952 */         inContext = Servers.isThisATestServer();
/*      */         break;
/*      */       case 2:
/*  955 */         inContext = true;
/*      */         break;
/*      */       default:
/*  958 */         inContext = false;
/*      */         break;
/*      */     } 
/*  961 */     if (inContext && sayIt()) {
/*      */       String sub1; String msg; String sub2; String str1;
/*  963 */       switch (speech.getParams()) {
/*      */ 
/*      */         
/*      */         case 0:
/*  967 */           this.creature.say(speech.getMsg(this.speechStyle), speech.isEmote(this.speechStyle));
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 1:
/*  973 */           sub1 = (delivery.getCrates() == 1) ? "1 crate" : (delivery.getCrates() + " crates");
/*  974 */           msg = String.format(speech.getMsg(this.speechStyle), new Object[] { sub1 });
/*  975 */           this.creature.say(msg, speech.isEmote(this.speechStyle));
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 2:
/*  981 */           sub1 = (delivery.getCrates() == 1) ? "1 crate" : (delivery.getCrates() + " crates");
/*  982 */           sub2 = delivery.getReceiverName();
/*  983 */           str1 = String.format(speech.getMsg(this.speechStyle), new Object[] { sub1, sub2 });
/*  984 */           this.creature.say(str1, speech.isEmote(this.speechStyle));
/*      */           break;
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
/*      */   public void sayRandom() {
/*  997 */     long now = WurmCalendar.getCurrentTime();
/*  998 */     if (this.lastChat + this.chatDelay > now) {
/*      */       return;
/*      */     }
/* 1001 */     if (this.speechContextRandom && this.tileCount % 10 == 5 && sayIt() && this.rnd.nextInt(4) == 0) {
/*      */       
/* 1003 */       this.lastChat = now;
/*      */       
/* 1005 */       long time = now + 300L;
/* 1006 */       int mins = (int)(time % 3600L / 60L);
/* 1007 */       if (mins <= 10) {
/*      */         
/* 1009 */         int hour = (int)(time % 86400L / 3600L);
/* 1010 */         for (TimedSpeech ts : TimedSpeech.values()) {
/*      */           
/* 1012 */           if (ts.getHour() == hour) {
/*      */ 
/*      */             
/* 1015 */             this.creature.say(ts.getMsg(this.speechStyle, this.creature.isOnSurface()), ts.isEmote(this.speechStyle, this.creature.isOnSurface()));
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1021 */       RandomSpeech rs = RandomSpeech.values()[this.rnd.nextInt((RandomSpeech.values()).length)];
/* 1022 */       this.creature.say(rs.getMsg(this.speechStyle, this.creature.isOnSurface()), rs.isEmote(this.speechStyle, this.creature.isOnSurface()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sayStuck() {
/* 1031 */     long now = WurmCalendar.getCurrentTime();
/* 1032 */     if (this.lastChat + this.chatDelay > now) {
/*      */       return;
/*      */     }
/* 1035 */     this.lastChat = now;
/* 1036 */     RandomSpeech rs = RandomSpeech.values()[this.rnd.nextInt((RandomSpeech.values()).length)];
/* 1037 */     this.creature.say(rs.getMsg(this.speechStyle, this.creature.isOnSurface()), rs.isEmote(this.speechStyle, this.creature.isOnSurface()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean sayIt() {
/* 1047 */     switch (this.speechChattyness) {
/*      */       
/*      */       case 0:
/* 1050 */         return (this.rnd.nextInt(3) == 0);
/*      */       case 1:
/* 1052 */         return false;
/*      */       case 2:
/* 1054 */         return true;
/*      */       case 3:
/* 1056 */         return (this.rnd.nextInt(this.randomness) < 4);
/*      */     } 
/* 1058 */     return true;
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
/*      */   public long getLazy() {
/* 1071 */     return this.lazy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastCheck() {
/* 1080 */     return this.lastCheck;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastCheck(long now) {
/* 1089 */     this.lastCheck = now;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getSubState() {
/* 1098 */     return this.subState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void incSubState() {
/* 1106 */     this.subState = (byte)(this.subState + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clrSubState() {
/* 1114 */     this.subState = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSubState(byte newSubState) {
/* 1123 */     this.subState = newSubState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNotMoving() {
/* 1132 */     return this.notMoving;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void incNotMoving() {
/* 1140 */     this.notMoving++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clrNotMoving() {
/* 1148 */     this.notMoving = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLastDir() {
/* 1157 */     return this.lastDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastDir(int newDir) {
/* 1166 */     this.lastDir = newDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getSpeechChattyness() {
/* 1175 */     return this.speechChattyness;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpeachContextFood() {
/* 1184 */     return this.speechContextFood;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpeachContextWork() {
/* 1193 */     return this.speechContextWork;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpeachContextSleep() {
/* 1202 */     return this.speechContextSleep;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpeachContextRandom() {
/* 1211 */     return this.speechContextRandom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getSpeechStyle() {
/* 1220 */     return this.speechStyle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSpeechOptions(byte newChattyness, boolean newContextFood, boolean newContextWork, boolean newContextSleep, boolean newContextRandom, byte newChatType) {
/* 1230 */     this.speechChattyness = newChattyness;
/* 1231 */     this.speechContextFood = newContextFood;
/* 1232 */     this.speechContextWork = newContextWork;
/* 1233 */     this.speechContextSleep = newContextSleep;
/* 1234 */     this.speechContextRandom = newContextRandom;
/* 1235 */     this.speechStyle = newChatType;
/*      */     
/* 1237 */     setChatGap();
/*      */     
/* 1239 */     byte newSpeachOptions = (byte)(newChattyness + (newContextFood ? 0 : 4) + (newContextWork ? 0 : 8) + (newContextSleep ? 0 : 16) + (newContextRandom ? 32 : 0) + (newChatType << 6));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1246 */     dbUpdateWagonerChatOptions(this.wagonerId, newSpeachOptions);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setChatGap() {
/* 1251 */     switch (this.speechChattyness) {
/*      */       
/*      */       case 0:
/* 1254 */         this.chatDelay = 300L;
/*      */         break;
/*      */       case 1:
/* 1257 */         this.chatDelay = 3600L;
/*      */         break;
/*      */       case 2:
/* 1260 */         this.chatDelay = 60L;
/*      */         break;
/*      */       case 3:
/* 1263 */         this.chatDelay = 60L + this.rnd.nextInt(300);
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
/*      */   
/*      */   public static String getStateName(byte state) {
/* 1279 */     switch (state) {
/*      */       
/*      */       case 0:
/* 1282 */         return "Idle";
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/* 1286 */         return "Sleeping";
/*      */       case 4:
/* 1288 */         return "Getting ready";
/*      */       case 5:
/*      */       case 11:
/* 1291 */         return "Driving to collection point";
/*      */       case 6:
/* 1293 */         return "Loading";
/*      */       case 7:
/*      */       case 12:
/* 1296 */         return "Delivering";
/*      */       case 8:
/* 1298 */         return "Unloading";
/*      */       case 9:
/*      */       case 13:
/* 1301 */         return "Going home";
/*      */       case 10:
/* 1303 */         return "Parking";
/*      */       case 14:
/* 1305 */         return "waiting";
/*      */       case 15:
/* 1307 */         return "driving";
/*      */     } 
/* 1309 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Wagoner getWagoner(long wurmId) {
/* 1319 */     return wagoners.get(Long.valueOf(wurmId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Map<Long, Wagoner> getWagoners() {
/* 1328 */     return wagoners;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Wagoner[] getAllWagoners() {
/* 1337 */     return (Wagoner[])wagoners.values().toArray((Object[])new Wagoner[wagoners.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void removeWagoner(Wagoner wagoner) {
/* 1346 */     wagoners.remove(Long.valueOf(wagoner.getWurmId()));
/* 1347 */     dbRemoveWagoner(wagoner.getWurmId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void addWagoner(Wagoner wagoner) {
/* 1356 */     wagoners.put(Long.valueOf(wagoner.getWurmId()), wagoner);
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
/*      */   public static final void addWagoner(long wurmId, long ownerId, long contractId, long homeWaystoneId, int homeVillageId, Item wagon, long restingPlaceId, long chairId, long tentId, long bedId, byte speachChatty, boolean speachFood, boolean speachWork, boolean speachSleep, boolean speachRandom, byte speachType) {
/* 1377 */     byte state = 0;
/* 1378 */     long deliveryId = -10L;
/* 1379 */     long lastWaystoneId = -10L;
/* 1380 */     long goalWaystoneId = -10L;
/*      */ 
/*      */ 
/*      */     
/* 1384 */     Wagoner wagoner = new Wagoner(wurmId, (byte)0, ownerId, contractId, homeWaystoneId, homeVillageId, wagon.getWurmId(), restingPlaceId, chairId, tentId, bedId, -10L, wagon.getPosX(), wagon.getPosY(), wagon.isOnSurface(), wagon.getRotation(), -10L, -10L, speachChatty, speachFood, speachWork, speachSleep, speachRandom, speachType);
/*      */     
/* 1386 */     dbCreateWagoner(wagoner);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getWagonerNameFrom(long waystoneId) {
/* 1396 */     for (Wagoner wagoner : wagoners.values()) {
/*      */       
/* 1398 */       if (wagoner.getHomeWaystoneId() == waystoneId)
/*      */       {
/* 1400 */         return wagoner.getName() + " camp";
/*      */       }
/*      */     } 
/* 1403 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isOnActiveDeliveryRoute(Item marker) {
/* 1413 */     for (Wagoner wagoner : wagoners.values()) {
/*      */       
/* 1415 */       if (wagoner.markerOnDeliveryRoute(marker))
/*      */       {
/* 1417 */         return true;
/*      */       }
/*      */     } 
/* 1420 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void dbLoadAllWagoners() {
/* 1428 */     logger.log(Level.INFO, "Loading all wagoners.");
/* 1429 */     long start = System.nanoTime();
/* 1430 */     Connection dbcon = null;
/* 1431 */     PreparedStatement ps = null;
/* 1432 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1435 */       dbcon = DbConnector.getCreatureDbCon();
/* 1436 */       ps = dbcon.prepareStatement("SELECT * FROM WAGONER");
/* 1437 */       rs = ps.executeQuery();
/* 1438 */       while (rs.next())
/*      */       {
/* 1440 */         long wurmId = rs.getLong("WURMID");
/* 1441 */         byte state = rs.getByte("STATE");
/* 1442 */         long ownerId = rs.getLong("OWNERID");
/* 1443 */         long contractId = rs.getLong("CONTRACT_ID");
/* 1444 */         long homeWaystoneId = rs.getLong("HOME_WAYSTONE_ID");
/* 1445 */         int homeVillageId = rs.getInt("HOME_VILLAGE_ID");
/* 1446 */         long wagonId = rs.getLong("WAGON_ID");
/* 1447 */         long restingPlaceId = rs.getLong("RESTING_PLACE_ID");
/* 1448 */         long chairId = rs.getLong("CHAIR_ID");
/* 1449 */         long tentId = rs.getLong("TENT_ID");
/* 1450 */         long bedId = rs.getLong("BED_ID");
/* 1451 */         long deliveryId = rs.getLong("DELIVERY_ID");
/* 1452 */         float wagonPosX = rs.getFloat("WAGON_POSX");
/* 1453 */         float wagonPosY = rs.getFloat("WAGON_POSY");
/* 1454 */         boolean wagonOnSurface = rs.getBoolean("WAGON_ON_SURFACE");
/* 1455 */         float campRot = rs.getFloat("CAMP_ROT");
/* 1456 */         long lastWaystoneId = rs.getLong("LAST_WAYSTONE_ID");
/* 1457 */         long goalWaystoneId = rs.getLong("GOAL_WAYSTONE_ID");
/* 1458 */         byte chat = rs.getByte("CHAT");
/* 1459 */         byte speachChatty = (byte)(chat & 0x3);
/* 1460 */         boolean speachFood = ((chat & 0x4) != 4);
/* 1461 */         boolean speachWork = ((chat & 0x8) != 8);
/* 1462 */         boolean speachSleep = ((chat & 0x10) != 16);
/* 1463 */         boolean speachRandom = ((chat & 0x20) == 32);
/* 1464 */         byte speachType = (byte)(chat >> 6 & 0x3);
/* 1465 */         new Wagoner(wurmId, state, ownerId, contractId, homeWaystoneId, homeVillageId, wagonId, restingPlaceId, chairId, tentId, bedId, deliveryId, wagonPosX, wagonPosY, wagonOnSurface, campRot, lastWaystoneId, goalWaystoneId, speachChatty, speachFood, speachWork, speachSleep, speachRandom, speachType);
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1470 */     catch (SQLException sqex) {
/*      */       
/* 1472 */       logger.log(Level.WARNING, "Failed to load all wagoners: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1476 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1477 */       DbConnector.returnConnection(dbcon);
/* 1478 */       long end = System.nanoTime();
/* 1479 */       logger.log(Level.INFO, "Loaded " + wagoners.size() + " wagoners. That took " + ((float)(end - start) / 1000000.0F) + " ms.");
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
/*      */   private static void dbCreateWagoner(Wagoner wag) {
/* 1491 */     Connection dbcon = null;
/* 1492 */     PreparedStatement ps = null;
/* 1493 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1496 */       dbcon = DbConnector.getCreatureDbCon();
/* 1497 */       ps = dbcon.prepareStatement("INSERT INTO WAGONER (WURMID,STATE,OWNERID,CONTRACT_ID,HOME_WAYSTONE_ID,HOME_VILLAGE_ID,WAGON_ID,RESTING_PLACE_ID,CHAIR_ID,TENT_ID,BED_ID,DELIVERY_ID,WAGON_POSX,WAGON_POSY,WAGON_ON_SURFACE,CAMP_ROT,LAST_WAYSTONE_ID,GOAL_WAYSTONE_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/* 1498 */       ps.setLong(1, wag.getWurmId());
/* 1499 */       ps.setByte(2, wag.getState());
/* 1500 */       ps.setLong(3, wag.getOwnerId());
/* 1501 */       ps.setLong(4, wag.getContractId());
/* 1502 */       ps.setLong(5, wag.getHomeWaystoneId());
/* 1503 */       ps.setInt(6, wag.getVillageId());
/* 1504 */       ps.setLong(7, wag.getWagonId());
/* 1505 */       ps.setLong(8, wag.getRestingPlaceId());
/* 1506 */       ps.setLong(9, wag.getChairId());
/* 1507 */       ps.setLong(10, wag.getTentId());
/* 1508 */       ps.setLong(11, wag.getBedId());
/* 1509 */       ps.setLong(12, wag.getDeliveryId());
/* 1510 */       ps.setFloat(13, wag.getWagonPosX());
/* 1511 */       ps.setFloat(14, wag.getWagonPosY());
/* 1512 */       ps.setBoolean(15, wag.getWagonOnSurface());
/* 1513 */       ps.setFloat(16, wag.getCampRot());
/* 1514 */       ps.setLong(17, wag.getLastWaystoneId());
/* 1515 */       ps.setLong(18, wag.getGoalWaystoneId());
/* 1516 */       ps.executeUpdate();
/*      */     }
/* 1518 */     catch (SQLException ex) {
/*      */       
/* 1520 */       logger.log(Level.WARNING, "Failed to create wagoner " + wag.getWurmId() + " in wagoner table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1524 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1525 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbRemoveWagoner(long wagonerId) {
/* 1536 */     Connection dbcon = null;
/* 1537 */     PreparedStatement ps = null;
/* 1538 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1541 */       dbcon = DbConnector.getCreatureDbCon();
/* 1542 */       ps = dbcon.prepareStatement("DELETE FROM WAGONER WHERE WURMID=?");
/* 1543 */       ps.setLong(1, wagonerId);
/* 1544 */       ps.executeUpdate();
/*      */     }
/* 1546 */     catch (SQLException ex) {
/*      */       
/* 1548 */       logger.log(Level.WARNING, "Failed to remove wagoner " + wagonerId + " from wagoner table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1552 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1553 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void dbUpdateWagonerState(long wagonerId, byte state) {
/* 1565 */     Connection dbcon = null;
/* 1566 */     PreparedStatement ps = null;
/* 1567 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1570 */       dbcon = DbConnector.getCreatureDbCon();
/* 1571 */       ps = dbcon.prepareStatement("UPDATE WAGONER SET STATE=? WHERE WURMID=?");
/* 1572 */       ps.setByte(1, state);
/* 1573 */       ps.setLong(2, wagonerId);
/* 1574 */       ps.executeUpdate();
/*      */     }
/* 1576 */     catch (SQLException ex) {
/*      */       
/* 1578 */       logger.log(Level.WARNING, "Failed to update wagoner " + wagonerId + " to state " + state + " in wagoner table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1582 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1583 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void dbUpdateWagonerDelivery(long wagonerId, long deliveryId, byte state) {
/* 1596 */     Connection dbcon = null;
/* 1597 */     PreparedStatement ps = null;
/* 1598 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1601 */       dbcon = DbConnector.getCreatureDbCon();
/* 1602 */       ps = dbcon.prepareStatement("UPDATE WAGONER SET DELIVERY_ID=?, STATE=? WHERE WURMID=?");
/* 1603 */       ps.setLong(1, deliveryId);
/* 1604 */       ps.setByte(2, state);
/* 1605 */       ps.setLong(3, wagonerId);
/* 1606 */       ps.executeUpdate();
/*      */     }
/* 1608 */     catch (SQLException ex) {
/*      */       
/* 1610 */       logger.log(Level.WARNING, "Failed to update wagoner " + wagonerId + " delivery " + deliveryId + " and state " + state + " in wagoner table.", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1614 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1615 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void dbUpdateLastWaystoneId(long wagonerId, long lastWaystoneId) {
/* 1627 */     Connection dbcon = null;
/* 1628 */     PreparedStatement ps = null;
/* 1629 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1632 */       dbcon = DbConnector.getCreatureDbCon();
/* 1633 */       ps = dbcon.prepareStatement("UPDATE WAGONER SET LAST_WAYSTONE_ID=? WHERE WURMID=?");
/* 1634 */       ps.setLong(1, lastWaystoneId);
/* 1635 */       ps.setLong(2, wagonerId);
/* 1636 */       ps.executeUpdate();
/*      */     }
/* 1638 */     catch (SQLException ex) {
/*      */       
/* 1640 */       logger.log(Level.WARNING, "Failed to update wagoner " + wagonerId + " last waystone to " + lastWaystoneId + ".", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1644 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1645 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void dbUpdateGoalWaystoneId(long wagonerId, long goalWaystoneId) {
/* 1657 */     Connection dbcon = null;
/* 1658 */     PreparedStatement ps = null;
/* 1659 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1662 */       dbcon = DbConnector.getCreatureDbCon();
/* 1663 */       ps = dbcon.prepareStatement("UPDATE WAGONER SET GOAL_WAYSTONE_ID=? WHERE WURMID=?");
/* 1664 */       ps.setLong(1, goalWaystoneId);
/* 1665 */       ps.setLong(2, wagonerId);
/* 1666 */       ps.executeUpdate();
/*      */     }
/* 1668 */     catch (SQLException ex) {
/*      */       
/* 1670 */       logger.log(Level.WARNING, "Failed to update wagoner " + wagonerId + " goal waystone to " + goalWaystoneId + ".", ex);
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
/*      */   private static void dbUpdateWagonerChatOptions(long wagonerId, byte chatOptions) {
/* 1687 */     Connection dbcon = null;
/* 1688 */     PreparedStatement ps = null;
/* 1689 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1692 */       dbcon = DbConnector.getCreatureDbCon();
/* 1693 */       ps = dbcon.prepareStatement("UPDATE WAGONER SET CHAT=? WHERE WURMID=?");
/* 1694 */       ps.setByte(1, chatOptions);
/* 1695 */       ps.setLong(2, wagonerId);
/* 1696 */       ps.executeUpdate();
/*      */     }
/* 1698 */     catch (SQLException ex) {
/*      */       
/* 1700 */       logger.log(Level.WARNING, "Failed to update wagoner " + wagonerId + " chat options to " + chatOptions + ".", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1704 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1705 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Speech
/*      */   {
/* 1714 */     YAWNS((byte)16, 0, true, "Yawns!", true, "Yawns!", true, "Yawns!", true, "Yawns!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1719 */     BED((byte)16, 0, false, "Going to bed before I fall asleep in my chair.", false, "Falling asleep in a chair, I'm getting old. Time for bed!", false, "Time to sleep so I can do it all over again tomorrow, lucky me.", false, "It's been a great day, but time for sleep!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1724 */     ZZZZ((byte)16, 0, true, "zzzZzzzZzz.", true, "zzzZzzZzzz.", true, "zzzzzZzzZz.", true, "zzZzzzzzZz."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1729 */     GETUP((byte)16, 0, false, "Time to get up already?", false, "I don't wanna go to school Muuummm... Never mind, I'm up, I'm up, ignore that.", false, "It's too early, why am I up!", false, "Early bird gets the wurm!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1734 */     SLEPT_IN((byte)16, 0, false, "Looks like I slept in!", false, "I swear I just closed my eyes for a few seconds.", false, "Yeesh, can't I sleep in some days without the third degree?", false, "Oops, looks like I was a bit of a sleepy head!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1739 */     MORNING((byte)16, 0, false, "Morning all.", false, "Don't talk to me until I've had my... I don't know.", false, "Yeah yeah, morning, nothing good about it.", false, "Good morning everyone, I hope you have a great day - I know I will!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1744 */     BREAKFAST((byte)4, 0, false, "Ah breakfast!", false, "Is that growling a bear or am I just hungry?", false, "Finally breakfast, let's hope this is better than the last meal.", false, "Breakfast time! What did I get today, I hope it's my favourite."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1749 */     LUNCH((byte)4, 0, false, "I hope it's chips.", false, "One can never have too many sandwiches for lunch, unless you have more than two.", false, "Lunch, the one good thing about daytime.", false, "Lunch time! The day goes by so fast when you're having fun."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1754 */     DINNER((byte)4, 0, false, "Hog roast, that's what to do with roadkill!", false, "Why can't I eat dinner at a table?", false, "I'm so over spider meat soup.", false, "Dinnertime! Time to use those mushrooms I foraged!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1759 */     DELIVERY((byte)8, 0, false, "Woo hoo, a delivery to do!", false, "Another delivery? Why didn't I listen to my mother and become a carpenter.", false, "Another one? Really? Really?!?", false, "Woo hoo, a delivery to do!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1764 */     SUMMON((byte)8, 0, true, "Cracks a whip, and four wagon creatures appear.", true, "Cracks a whip, and four wagon creatures appear.", true, "Cracks a whip, and four wagon creatures appear.", true, "Cracks a whip, and four wagon creatures appear."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1769 */     START_DELIVERY((byte)8, 0, false, "And off I go.", false, "And I'm off like a sausage in the sun.", false, "The sooner I do this the sooner it's over.", false, "Another adventure, I'm excited!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1774 */     LOADING((byte)8, 1, false, "Loading %s.", false, "%s to go in the load, some crates to go. You take one out and load it about, one less crate to go in the load.", false, "%s? Couldn't have made it fewer?", false, "Wow, %s, that's a lot! Someone is going to be happy to receive them all."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1779 */     UNLOADING((byte)8, 2, false, "Delivery of %s for %s.", false, "I have %s for %s here, and I only peeked in one of them!", false, "I've got %s for %s here, and they're not going to unload themselves.", false, "HI! I have %s here for %s!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1784 */     DELIVERY_DONE((byte)8, 0, false, "Delivery done, off back home now.", false, "Well, I'm not paid by the hour, time to get going... We should form a union!", false, "Right, that's all of them.  Not here to chat, I'm off.", false, "And that's all of them, I'm off back home.  Have a great day!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1789 */     SEND_TO_VOID((byte)8, 0, true, "Cracks a whip and the wagoner creatures vanish.", true, "Cracks a whip and the wagoner creatures vanish.", true, "Cracks a whip and the wagoner creatures vanish.", true, "Cracks a whip and the wagoner creatures vanish."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1794 */     ARRIVED((byte)8, 0, false, "Yay, I've arrived!", false, "What has two thumbs and crates for you?  This wagoner!", false, "All right, all right, I'm here, quit nagging.", false, "I've arrived, and what a beautiful place you have!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1799 */     REPAIR((byte)8, 0, true, "Cleaning the wagon.", false, "Someone scratched my paint job. But it looks better now!.", false, "Another scratch! More work as i'll just have to polish it out.", false, "A scratch, it looks just like a flame!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1804 */     ERROR_VANISHED((byte)2, 0, false, "Delivery vanished, so parking up!", false, "Delivery vanished, so parking up!", false, "Delivery vanished, so parking up!", false, "Delivery vanished, so parking up!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1809 */     ERROR_NO_ROUTE((byte)2, 0, false, "no eye dear!", false, "no eye dear!", false, "no eye dear!", false, "no eye dear!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1814 */     ERROR_STUCK((byte)2, 0, false, "I seem to have stopped moving; maybe I'm stuck?", false, "Is it just me or did the scenery stop moving by?", false, "Ugh, it better not be a broken wheel!", false, "Oh no, it looks like I'm stuck!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1819 */     TEST_STOP((byte)1, 0, false, "Ok, ok, stopping here!", false, "Right here, like this?", false, "Not going to go any further.", false, "Looks like it's time to stop!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1824 */     TEST_GET_READY((byte)1, 0, false, "Ok, ok, i'll get ready for delivery.", false, "Let me go put on my makeup and get ready.", false, "Ugh, guess I better get ready to deliver.", false, "Yay another delivery, I'll get ready as quick as can be!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1829 */     TEST_SLEEP((byte)1, 0, false, "Ok, ok, i'll go to sleep.", false, "Bedtime? Now? But it's not even dark!", false, "Right, nothing worth staying up for, bedtime.", false, "Bedtime? I agree, goodnight!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1834 */     TEST_GETUP((byte)1, 0, false, "Stop poking me, I'll get up.", false, "Didn't your mother tell you not to spoke a sleeping person? You must have been an annoying child.", false, "Poke me with that again and you'll lose it!", false, "Stop poking me, it tickles! I'll get up and ready!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1839 */     TEST_NOWHERE_TO_GO((byte)1, 0, false, "Nowhere to go... Must have been a test of getting ready, waiting for next command.", false, "Wait a minute, there's nowhere to go! You're pranking me arent you!", false, "Are you just pulling my leg with some sort of test?", false, "Oh no, it looks like that was just a test, I'll keep waiting for sure!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1844 */     TEST_DRIVE((byte)1, 0, false, "Ok, ok, I'll drive there.", false, "Over there? You're sorely under-utilising my skills you know.", false, "Alright alright I'm going, quit nagging.", false, "You want me to drive there? Sure thing!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1849 */     TEST_PARK((byte)1, 0, false, "Ok, ok, I'll park up.", false, "Careful, I never learned to parallel park.", false, "All right, all right, I'll park. Jeesh, the nerve of some people.", false, "Park here? Can do!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1854 */     TEST_HOME((byte)1, 0, false, "Guess I'm not wanted anymore, so I'll drive home.", false, "I feel about as welcome as a fart in a plate armour suit, so I'll be going.", false, "Guess no one wants me here, me included. I'm going home.", false, "Don't need me here? That's okay, I'll go home, hope I helped!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1859 */     STUCK_COLLECTING((byte)2, 0, true, "Wonder if I have a puncture.", false, "Beam me up scotty!", false, "Oh no, not stuck again!", false, "Oh I left the handbreak on..."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1864 */     STUCK_DELIVERING((byte)2, 0, false, "I have a flat!.", false, "Lucky the wheel is only flat at the bottom!", false, "Oh no, not a flat again!", false, "i get to try my powers out!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1869 */     STUCK_GOING_HOME((byte)2, 0, false, "Looks like I may be home early.", true, "thinks of home!", true, "If only I still had enough karma...", false, "Yay, a use for my karma!");
/*      */     
/*      */     private final byte context;
/*      */     
/*      */     private final int params;
/*      */     
/*      */     private final boolean normalEmote;
/*      */     
/*      */     private final String normalMsg;
/*      */     
/*      */     private final boolean whittyEmote;
/*      */     
/*      */     private final String whittyMsg;
/*      */     
/*      */     private final boolean grumpyEmote;
/*      */     
/*      */     private final String grumpyMsg;
/*      */     private final boolean cheeryEmote;
/*      */     private final String cheeryMsg;
/*      */     
/*      */     Speech(byte context, int params, boolean normalEmote, String normalMsg, boolean whittyEmote, String whittyMsg, boolean grumpyEmote, String grumpyMsg, boolean cheeryEmote, String cheeryMsg) {
/* 1890 */       this.context = context;
/* 1891 */       this.params = params;
/* 1892 */       this.normalEmote = normalEmote;
/* 1893 */       this.normalMsg = normalMsg;
/* 1894 */       this.whittyEmote = whittyEmote;
/* 1895 */       this.whittyMsg = whittyMsg;
/* 1896 */       this.grumpyEmote = grumpyEmote;
/* 1897 */       this.grumpyMsg = grumpyMsg;
/* 1898 */       this.cheeryEmote = cheeryEmote;
/* 1899 */       this.cheeryMsg = cheeryMsg;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte getContext() {
/* 1904 */       return this.context;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getParams() {
/* 1909 */       return this.params;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmote(byte speechType) {
/* 1914 */       switch (speechType) {
/*      */ 
/*      */         
/*      */         case 1:
/* 1918 */           return this.whittyEmote;
/*      */ 
/*      */         
/*      */         case 2:
/* 1922 */           return this.grumpyEmote;
/*      */ 
/*      */         
/*      */         case 3:
/* 1926 */           return this.cheeryEmote;
/*      */       } 
/*      */ 
/*      */       
/* 1930 */       return this.normalEmote;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMsg(byte speechType) {
/* 1937 */       switch (speechType) {
/*      */ 
/*      */         
/*      */         case 1:
/* 1941 */           return this.whittyMsg;
/*      */ 
/*      */         
/*      */         case 2:
/* 1945 */           return this.grumpyMsg;
/*      */ 
/*      */         
/*      */         case 3:
/* 1949 */           return this.cheeryMsg;
/*      */       } 
/*      */ 
/*      */       
/* 1953 */       return this.normalMsg;
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
/*      */   public enum RandomSpeech
/*      */   {
/* 1966 */     TILES(false, "What a view!", false, "Not much of a view in this tunnel.", false, "I can see for tiles and tiles!", false, "I can almost see to the next tile in the dark here.", false, "oh, not that mountain again!", false, "Those wagoner creatures better not get lose down here, I'll never get them back on the wagon.", false, "Oh look at that, a nice mountain!", false, "What a finely built tunnel, the miners guild would be proud!"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1975 */     TREES(false, "That reminds me, must sharpen my axe.", false, "I bet there's a petrified forest underground here.", false, "I can't see the forest for the trees.", false, "I wonder if you use an axe or a pick to harvest petrified forest.", false, "Trees, trees always trees.  I wish they'd just leaf me alone!", false, "So glad there's no trees down here, cut 'em all down I say.", false, "I like trees, the way they branch out.", false, "I'd like to come back and look for fossil trees someday near here."),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1984 */     SAND(true, "Wonder where my shovel is.", false, "I cant see the desert for the sand.", false, "Sand everywhere!", false, "Oh can make sandcastles."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1989 */     BACKGROUND(false, "oh a butterfly!", false, "duck!", false, "Always Ducks!", false, "Oh, a hen, anyone for an omlette."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1994 */     VILLAGE(false, "I'm sure there is a village near here.", false, "I'm sure there's buried treasure near by.", false, "Some days you're the wagon, some days you're the squished wildlife.", false, "Some days you're the pick, some days you're the rockshard.", false, "I'm always on the road, some day soon I'm going to quit!", false, "Tunnels tunnels tunnels always going on, when do they ever end.", false, "Wonder if anyone will hear me!", false, "The echos in this tunnel are amazing!"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2003 */     TROLLS(false, "Oh, a troll!", false, "Who's your mama, troll!", false, "Not another troll!", false, "Oh a chance to get a recipe."),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2008 */     DAY(false, "I hope I remembered a lantern this trip.", false, "Hope the tar for the lantern holds out while I'm down in this tunnel.", false, "This scenery doesn't look right, where's my map?", false, "Is it left or right at the next turning...", false, "I miss my campfire, this delivery better not take too long.", false, "What a damp and miserable tunnel, nothing like my cozy campfire.", false, "It's a fine day/night for a delivery.", false, "What fun to see a tunnel like this on the way to my delivery.");
/*      */     
/*      */     private final boolean surfaceNormalEmote;
/*      */     
/*      */     private final String surfaceNormalMsg;
/*      */     
/*      */     private final boolean caveNormalEmote;
/*      */     
/*      */     private final String caveNormalMsg;
/*      */     
/*      */     private final boolean surfaceWhittyEmote;
/*      */     
/*      */     private final String surfaceWhittyMsg;
/*      */     
/*      */     private final boolean caveWhittyEmote;
/*      */     
/*      */     private final String caveWhittyMsg;
/*      */     
/*      */     private final boolean surfaceGrumpyEmote;
/*      */     
/*      */     private final String surfaceGrumpyMsg;
/*      */     
/*      */     private final boolean caveGrumpyEmote;
/*      */     
/*      */     private final String caveGrumpyMsg;
/*      */     
/*      */     private final boolean surfaceCheeryEmote;
/*      */     
/*      */     private final String surfaceCheeryMsg;
/*      */     
/*      */     private final boolean caveCheeryEmote;
/*      */     
/*      */     private final String caveCheeryMsg;
/*      */ 
/*      */     
/*      */     RandomSpeech(boolean surfaceNormalEmote, String surfaceNormalMsg, boolean caveNormalEmote, String caveNormalMsg, boolean surfaceWhittyEmote, String surfaceWhittyMsg, boolean caveWhittyEmote, String caveWhittyMsg, boolean surfaceGrumpyEmote, String surfaceGrumpyMsg, boolean caveGrumpyEmote, String caveGrumpyMsg, boolean surfaceCheeryEmote, String surfaceCheeryMsg, boolean caveCheeryEmote, String caveCheeryMsg) {
/* 2044 */       this.surfaceNormalEmote = surfaceNormalEmote;
/* 2045 */       this.surfaceNormalMsg = surfaceNormalMsg;
/* 2046 */       this.caveNormalEmote = caveNormalEmote;
/* 2047 */       this.caveNormalMsg = caveNormalMsg;
/* 2048 */       this.surfaceWhittyEmote = surfaceWhittyEmote;
/* 2049 */       this.surfaceWhittyMsg = surfaceWhittyMsg;
/* 2050 */       this.caveWhittyEmote = caveWhittyEmote;
/* 2051 */       this.caveWhittyMsg = caveWhittyMsg;
/* 2052 */       this.surfaceGrumpyEmote = surfaceGrumpyEmote;
/* 2053 */       this.surfaceGrumpyMsg = surfaceGrumpyMsg;
/* 2054 */       this.caveGrumpyEmote = caveGrumpyEmote;
/* 2055 */       this.caveGrumpyMsg = caveGrumpyMsg;
/* 2056 */       this.surfaceCheeryEmote = surfaceCheeryEmote;
/* 2057 */       this.surfaceCheeryMsg = surfaceCheeryMsg;
/* 2058 */       this.caveCheeryEmote = caveCheeryEmote;
/* 2059 */       this.caveCheeryMsg = caveCheeryMsg;
/*      */     }
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
/*      */     public boolean isEmote(byte speechType, boolean onSurface) {
/* 2075 */       switch (speechType) {
/*      */ 
/*      */         
/*      */         case 1:
/* 2079 */           return onSurface ? this.surfaceWhittyEmote : this.caveWhittyEmote;
/*      */ 
/*      */         
/*      */         case 2:
/* 2083 */           return onSurface ? this.surfaceGrumpyEmote : this.caveGrumpyEmote;
/*      */ 
/*      */         
/*      */         case 3:
/* 2087 */           return onSurface ? this.surfaceCheeryEmote : this.caveCheeryEmote;
/*      */       } 
/*      */ 
/*      */       
/* 2091 */       return onSurface ? this.surfaceNormalEmote : this.caveNormalEmote;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMsg(byte speechType, boolean onSurface) {
/* 2098 */       switch (speechType) {
/*      */ 
/*      */         
/*      */         case 1:
/* 2102 */           return onSurface ? this.surfaceWhittyMsg : this.caveWhittyMsg;
/*      */ 
/*      */         
/*      */         case 2:
/* 2106 */           return onSurface ? this.surfaceGrumpyMsg : this.caveGrumpyMsg;
/*      */ 
/*      */         
/*      */         case 3:
/* 2110 */           return onSurface ? this.surfaceCheeryMsg : this.caveCheeryMsg;
/*      */       } 
/*      */ 
/*      */       
/* 2114 */       return onSurface ? this.surfaceNormalMsg : this.caveNormalMsg;
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
/*      */   public enum TimedSpeech
/*      */   {
/* 2127 */     EARLY(4, false, "Getting nice and light.", false, "Was that thunder or my stomach?", false, "I'm so hungry, I could eat a horse!  Even a hell horse!", false, "Breakfast soon!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2132 */     MORNING(5, false, "Morning!", false, "Morning!", false, "Its 5am and all's well!", false, "Its 5am and all's well!", false, "I should be back at camp having breakfast.", false, "I should be back at camp having breakfast.", false, "What a picture when dawn strikes!", false, "If only I was outside, I could see the dawn!"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2141 */     LUNCH(11, false, "Almost time for lunch!", false, "Was that thunder or my stomach?", false, "So hungry, I could eat a troll.  Well, most of one, anyway.", false, "Lunch soon!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2146 */     NOON(12, false, "Sun in the sky says it's high noon.", false, "Hard to tell time in the dark, but I think it's noon.", false, "Guess I'll have to eat this packaged spider meal, I wonder what's actually in it?", false, "It's too dark to see what's in this packaged meal, and I think that's for the best.", false, "I hate having to work through lunch.  And when is tea time?", false, "The way this tunnel winds on and on, I'll never get back in time for lunch.", false, "Looks like I'm on the road for lunch today.", false, "Its a good day to have lunch in a tunnel."),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2155 */     DINNER(17, false, "Almost time for Dinner!", false, "Was that thunder or my stomach?", false, "So hungry, I could eat a bison, horns, tail and all!", false, "Dinner soon!"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2160 */     EVENING(20, false, "Getting dark, hope someone has added lamps to the highway?", false, "Getting dark, hope someone has added lamps to the highway!", false, "If it gets much darker I'll be doing a slalom on the highway.", false, "If it gets much darker I'll be doing a slalom on the highway.", false, "Oh, getting dark again, I hate the dark.", false, "Oh, getting dark again, I hate the dark.", false, "I do like seeing the stars!", false, "At least being underground, I dont get distracted by the stars!"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2169 */     MIDNIGHT(0, false, "Sure is dark at midnight.", false, "Dark at midnight isn't dark until you're driving in a tunnel.", false, "I'm glad I'm not scared of traveling at night...much...", false, "This tunnel seems a bit spooky at midnight.", false, "Why don't we have bedrolls for those of us who have to travel at night?", false, "I'd like to stretch out in the wagon for a rest along with those crates!", false, "Racing through the night on a delivery run, nothing stops Wurm wagoners!", false, "I don't even notice the late hour when I'm driving through a tunnel like this.");
/*      */     
/*      */     private final int hour;
/*      */     
/*      */     private final boolean surfaceNormalEmote;
/*      */     
/*      */     private final String surfaceNormalMsg;
/*      */     
/*      */     private final boolean caveNormalEmote;
/*      */     
/*      */     private final String caveNormalMsg;
/*      */     
/*      */     private final boolean surfaceWhittyEmote;
/*      */     
/*      */     private final String surfaceWhittyMsg;
/*      */     
/*      */     private final boolean caveWhittyEmote;
/*      */     
/*      */     private final String caveWhittyMsg;
/*      */     
/*      */     private final boolean surfaceGrumpyEmote;
/*      */     
/*      */     private final String surfaceGrumpyMsg;
/*      */     
/*      */     private final boolean caveGrumpyEmote;
/*      */     
/*      */     private final String caveGrumpyMsg;
/*      */     
/*      */     private final boolean surfaceCheeryEmote;
/*      */     
/*      */     private final String surfaceCheeryMsg;
/*      */     
/*      */     private final boolean caveCheeryEmote;
/*      */     
/*      */     private final String caveCheeryMsg;
/*      */ 
/*      */     
/*      */     TimedSpeech(int hour, boolean surfaceNormalEmote, String surfaceNormalMsg, boolean caveNormalEmote, String caveNormalMsg, boolean surfaceWhittyEmote, String surfaceWhittyMsg, boolean caveWhittyEmote, String caveWhittyMsg, boolean surfaceGrumpyEmote, String surfaceGrumpyMsg, boolean caveGrumpyEmote, String caveGrumpyMsg, boolean surfaceCheeryEmote, String surfaceCheeryMsg, boolean caveCheeryEmote, String caveCheeryMsg) {
/* 2207 */       this.hour = hour;
/* 2208 */       this.surfaceNormalEmote = surfaceNormalEmote;
/* 2209 */       this.surfaceNormalMsg = surfaceNormalMsg;
/* 2210 */       this.caveNormalEmote = caveNormalEmote;
/* 2211 */       this.caveNormalMsg = caveNormalMsg;
/* 2212 */       this.surfaceWhittyEmote = surfaceWhittyEmote;
/* 2213 */       this.surfaceWhittyMsg = surfaceWhittyMsg;
/* 2214 */       this.caveWhittyEmote = caveWhittyEmote;
/* 2215 */       this.caveWhittyMsg = caveWhittyMsg;
/* 2216 */       this.surfaceGrumpyEmote = surfaceGrumpyEmote;
/* 2217 */       this.surfaceGrumpyMsg = surfaceGrumpyMsg;
/* 2218 */       this.caveGrumpyEmote = caveGrumpyEmote;
/* 2219 */       this.caveGrumpyMsg = caveGrumpyMsg;
/* 2220 */       this.surfaceCheeryEmote = surfaceCheeryEmote;
/* 2221 */       this.surfaceCheeryMsg = surfaceCheeryMsg;
/* 2222 */       this.caveCheeryEmote = caveCheeryEmote;
/* 2223 */       this.caveCheeryMsg = caveCheeryMsg;
/*      */     }
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
/*      */     public int getHour() {
/* 2240 */       return this.hour;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmote(byte speechType, boolean onSurface) {
/* 2245 */       switch (speechType) {
/*      */ 
/*      */         
/*      */         case 1:
/* 2249 */           return onSurface ? this.surfaceWhittyEmote : this.caveWhittyEmote;
/*      */ 
/*      */         
/*      */         case 2:
/* 2253 */           return onSurface ? this.surfaceGrumpyEmote : this.caveGrumpyEmote;
/*      */ 
/*      */         
/*      */         case 3:
/* 2257 */           return onSurface ? this.surfaceCheeryEmote : this.caveCheeryEmote;
/*      */       } 
/*      */ 
/*      */       
/* 2261 */       return onSurface ? this.surfaceNormalEmote : this.caveNormalEmote;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMsg(byte speechType, boolean onSurface) {
/* 2268 */       switch (speechType) {
/*      */ 
/*      */         
/*      */         case 1:
/* 2272 */           return onSurface ? this.surfaceWhittyMsg : this.caveWhittyMsg;
/*      */ 
/*      */         
/*      */         case 2:
/* 2276 */           return onSurface ? this.surfaceGrumpyMsg : this.caveGrumpyMsg;
/*      */ 
/*      */         
/*      */         case 3:
/* 2280 */           return onSurface ? this.surfaceCheeryMsg : this.caveCheeryMsg;
/*      */       } 
/*      */ 
/*      */       
/* 2284 */       return onSurface ? this.surfaceNormalMsg : this.caveNormalMsg;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Wagoner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */