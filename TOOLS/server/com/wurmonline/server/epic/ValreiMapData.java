/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.webinterface.WCValreiMapUpdater;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class ValreiMapData
/*     */ {
/*  42 */   private static Map<Long, ValreiMapData> enteties = new ConcurrentHashMap<>();
/*  43 */   static long lastPolled = 0L;
/*  44 */   static long lastUpdatedTime = 0L;
/*     */   
/*     */   private static boolean hasInitialData = false;
/*     */   private static boolean firstRequest = true;
/*  48 */   private static final Logger logger = Logger.getLogger(ValreiMapData.class.getName());
/*     */   
/*     */   private long entityId;
/*     */   
/*     */   private int hexId;
/*     */   
/*     */   private int type;
/*     */   
/*     */   private int targetHexId;
/*     */   
/*     */   private String name;
/*     */   private long timeRemaining;
/*     */   private float attack;
/*     */   private float vitality;
/*     */   private float bodyStr;
/*     */   private float bodySta;
/*     */   private float bodyCon;
/*     */   private float mindLog;
/*     */   private float mindSpe;
/*     */   private float soulStr;
/*     */   private float soulDep;
/*     */   private boolean dirty = false;
/*     */   private boolean dirtyTime = false;
/*  71 */   private List<CollectedValreiItem> carriedItems = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValreiMapData(long _entityId, int _hexId, int _type, int _targetHexId, String _name, long _remainingTime, float _bodyStr, float _bodySta, float _bodyCon, float _mindLog, float _mindSpe, float _soulStr, float _soulDep, List<CollectedValreiItem> _carried) {
/*  77 */     this.entityId = _entityId;
/*  78 */     this.hexId = _hexId;
/*  79 */     this.type = _type;
/*  80 */     this.targetHexId = _targetHexId;
/*  81 */     this.name = _name;
/*  82 */     this.timeRemaining = _remainingTime;
/*  83 */     this.bodyStr = _bodyStr;
/*  84 */     this.bodySta = _bodySta;
/*  85 */     this.bodyCon = _bodyCon;
/*  86 */     this.mindLog = _mindLog;
/*  87 */     this.mindSpe = _mindSpe;
/*  88 */     this.soulStr = _soulStr;
/*  89 */     this.soulDep = _soulDep;
/*  90 */     this.carriedItems = _carried;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void updateEntity(long _entityId, int _hexId, int _type, int _targetHexId, String _name, long _remainingTime, float _bodyStr, float _bodySta, float _bodyCon, float _mindLog, float _mindSpe, float _soulStr, float _soulDep, List<CollectedValreiItem> _carried) {
/*  97 */     ValreiMapData entity = enteties.get(Long.valueOf(_entityId));
/*  98 */     if (entity == null) {
/*     */       
/* 100 */       enteties.put(Long.valueOf(_entityId), new ValreiMapData(_entityId, _hexId, _type, _targetHexId, _name, _remainingTime, _bodyStr, _bodySta, _bodyCon, _mindLog, _mindSpe, _soulStr, _soulDep, (_carried != null) ? _carried : new ArrayList<>()));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 105 */       entity.setEntityId(_entityId);
/* 106 */       entity.setHexId(_hexId);
/* 107 */       entity.setType(_type);
/* 108 */       entity.setTargetHexId(_targetHexId);
/* 109 */       entity.setName(_name);
/* 110 */       entity.setBodyStr(_bodyStr);
/* 111 */       entity.setBodySta(_bodySta);
/* 112 */       entity.setBodyCon(_bodyCon);
/* 113 */       entity.setMindLog(_mindLog);
/* 114 */       entity.setMindSpe(_mindSpe);
/* 115 */       entity.setSoulStr(_soulStr);
/* 116 */       entity.setSoulDep(_soulDep);
/* 117 */       entity.setCarried(_carried);
/* 118 */       entity.setTimeRemaining(_remainingTime);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void updateEntityTime(long _entityId, long _time) {
/* 124 */     ValreiMapData entity = enteties.get(Long.valueOf(_entityId));
/* 125 */     if (entity != null)
/*     */     {
/* 127 */       entity.setTimeRemaining(_time);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized long getEntityId() {
/* 133 */     return this.entityId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized int getHexId() {
/* 138 */     return this.hexId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized int getType() {
/* 143 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized String getName() {
/* 148 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized long getTimeRemaining() {
/* 153 */     return this.timeRemaining;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized float getAttack() {
/* 158 */     return this.attack;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized float getVitality() {
/* 163 */     return this.vitality;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized int getTargetHexId() {
/* 168 */     return this.targetHexId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized List<CollectedValreiItem> getCarried() {
/* 173 */     return this.carriedItems;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setEntityId(long id) {
/* 178 */     if (this.entityId != id) {
/*     */       
/* 180 */       this.entityId = id;
/* 181 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setHexId(int id) {
/* 187 */     if (this.hexId != id) {
/*     */       
/* 189 */       this.hexId = id;
/* 190 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setType(int newType) {
/* 196 */     if (this.type != newType) {
/*     */       
/* 198 */       this.type = newType;
/* 199 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setTargetHexId(int newTarget) {
/* 205 */     if (this.targetHexId != newTarget) {
/*     */       
/* 207 */       this.targetHexId = newTarget;
/* 208 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setName(String newName) {
/* 214 */     if (!this.name.equals(newName)) {
/*     */       
/* 216 */       this.name = newName;
/* 217 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setTimeRemaining(long remaining) {
/* 223 */     if (this.timeRemaining != remaining) {
/*     */       
/* 225 */       this.timeRemaining = remaining;
/* 226 */       onTimeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setAttack(float newAttack) {
/* 232 */     if (this.attack != newAttack) {
/*     */       
/* 234 */       this.attack = newAttack;
/* 235 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setVitality(float newVitality) {
/* 241 */     if (this.vitality != newVitality) {
/*     */       
/* 243 */       this.vitality = newVitality;
/* 244 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBodyStr(float bodyStr) {
/* 250 */     if (this.bodyStr != bodyStr) {
/*     */       
/* 252 */       this.bodyStr = bodyStr;
/* 253 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBodySta(float bodySta) {
/* 259 */     if (this.bodySta != bodySta) {
/*     */       
/* 261 */       this.bodySta = bodySta;
/* 262 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBodyCon(float bodyCon) {
/* 268 */     if (this.bodyCon != bodyCon) {
/*     */       
/* 270 */       this.bodyCon = bodyCon;
/* 271 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMindLog(float mindLog) {
/* 277 */     if (this.mindLog != mindLog) {
/*     */       
/* 279 */       this.mindLog = mindLog;
/* 280 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMindSpe(float mindSpe) {
/* 286 */     if (this.mindSpe != mindSpe) {
/*     */       
/* 288 */       this.mindSpe = mindSpe;
/* 289 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSoulStr(float soulStr) {
/* 295 */     if (this.soulStr != soulStr) {
/*     */       
/* 297 */       this.soulStr = soulStr;
/* 298 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSoulDep(float soulDep) {
/* 304 */     if (this.soulDep != soulDep) {
/*     */       
/* 306 */       this.soulDep = soulDep;
/* 307 */       onChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getBodyStr() {
/* 312 */     return this.bodyStr;
/*     */   }
/*     */   
/*     */   public float getBodySta() {
/* 316 */     return this.bodySta;
/*     */   }
/*     */   
/*     */   public float getBodyCon() {
/* 320 */     return this.bodyCon;
/*     */   }
/*     */   
/*     */   public float getMindLog() {
/* 324 */     return this.mindLog;
/*     */   }
/*     */   
/*     */   public float getMindSpe() {
/* 328 */     return this.mindSpe;
/*     */   }
/*     */   
/*     */   public float getSoulStr() {
/* 332 */     return this.soulStr;
/*     */   }
/*     */   
/*     */   public float getSoulDep() {
/* 336 */     return this.soulDep;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setCarried(List<CollectedValreiItem> _carried) {
/* 341 */     if (this.carriedItems != _carried || !this.carriedItems.containsAll(_carried)) {
/*     */       
/* 343 */       this.carriedItems = _carried;
/* 344 */       onChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void onChanged() {
/* 350 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void onTimeChanged() {
/* 355 */     this.dirtyTime = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toggleNotDirty() {
/* 360 */     this.dirty = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toggleTimeNotDirty() {
/* 365 */     this.dirtyTime = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDirty() {
/* 370 */     return this.dirty;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTimeDirty() {
/* 375 */     return this.dirtyTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCollectable() {
/* 380 */     return (this.type == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSourceItem() {
/* 385 */     return (this.type == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDemiGod() {
/* 390 */     return (this.type == 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSentinel() {
/* 395 */     return (this.type == 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAlly() {
/* 400 */     return (this.type == 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isItem() {
/* 405 */     return (isCollectable() || isSourceItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCustomGod() {
/* 410 */     if (isItem() || isDemiGod()) {
/* 411 */       return false;
/*     */     }
/* 413 */     return (this.entityId > 100L);
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void setHasInitialData() {
/* 418 */     hasInitialData = true;
/* 419 */     lastUpdatedTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final synchronized boolean hasInitialData() {
/* 424 */     return hasInitialData;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void pollValreiData() {
/* 429 */     if (!Features.Feature.VALREI_MAP.isEnabled()) {
/*     */       return;
/*     */     }
/* 432 */     long now = System.currentTimeMillis();
/* 433 */     if (lastPolled == 0L || lastUpdatedTime == 0L) {
/*     */       
/* 435 */       lastPolled = now;
/* 436 */       lastUpdatedTime = now;
/*     */       
/*     */       return;
/*     */     } 
/* 440 */     long elapsed = now - lastPolled;
/* 441 */     long elapsedSinceTimeUpdate = now - lastUpdatedTime;
/* 442 */     if (!hasInitialData()) {
/*     */ 
/*     */       
/* 445 */       if (firstRequest || elapsed > 600000L) {
/*     */         
/* 447 */         lastPolled = now;
/* 448 */         firstRequest = false;
/*     */         
/* 450 */         if (!Servers.localServer.LOGINSERVER) {
/*     */           
/* 452 */           WCValreiMapUpdater updater = new WCValreiMapUpdater(WurmId.getNextWCCommandId(), (byte)0);
/* 453 */           updater.sendToLoginServer();
/*     */         }
/*     */         else {
/*     */           
/* 457 */           collectLocalData();
/* 458 */           setHasInitialData();
/*     */         } 
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 465 */     if (elapsedSinceTimeUpdate > 2400000L)
/*     */     {
/* 467 */       if (!Servers.localServer.LOGINSERVER) {
/*     */         
/* 469 */         WCValreiMapUpdater updater = new WCValreiMapUpdater(WurmId.getNextWCCommandId(), (byte)3);
/* 470 */         updater.sendToLoginServer();
/*     */       }
/*     */       else {
/*     */         
/* 474 */         updateTimeData();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 479 */     if (elapsed < 1800000L) {
/*     */       return;
/*     */     }
/* 482 */     lastPolled = now;
/*     */     
/* 484 */     Player[] players = Players.getInstance().getPlayers();
/*     */     
/* 486 */     for (Player player : players) {
/*     */       
/* 488 */       boolean sent = false;
/* 489 */       for (Iterator<ValreiMapData> iterator = enteties.values().iterator(); iterator.hasNext(); ) {
/*     */         
/* 491 */         ValreiMapData data = iterator.next();
/* 492 */         if (data.isDirty() || !player.hasReceivedInitialValreiData) {
/*     */           
/* 494 */           player.getCommunicator().sendValreiMapData(data);
/* 495 */           sent = true;
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 501 */         if (!data.isItem() && data.isTimeDirty()) {
/*     */           
/* 503 */           player.getCommunicator().sendValreiMapDataTimeUpdate(data);
/* 504 */           sent = true;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 509 */       if (sent && !player.hasReceivedInitialValreiData)
/*     */       {
/* 511 */         player.hasReceivedInitialValreiData = true;
/*     */       }
/*     */     } 
/*     */     
/* 515 */     for (Iterator<ValreiMapData> it = enteties.values().iterator(); it.hasNext(); ) {
/*     */       
/* 517 */       ValreiMapData data = it.next();
/* 518 */       if (data.isDirty()) {
/*     */         
/* 520 */         data.toggleNotDirty();
/* 521 */         data.toggleTimeNotDirty(); continue;
/*     */       } 
/* 523 */       if (data.isTimeDirty())
/*     */       {
/* 525 */         data.toggleTimeNotDirty();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sendAllMapData(Player player) {
/* 532 */     if (player.hasReceivedInitialValreiData)
/* 533 */       player.hasReceivedInitialValreiData = false; 
/* 534 */     if (Features.Feature.VALREI_MAP.isEnabled())
/*     */     {
/* 536 */       if (hasInitialData() && player != null) {
/*     */         
/* 538 */         player.getCommunicator().sendValreiMapDataList(enteties.values());
/* 539 */         player.hasReceivedInitialValreiData = (enteties.values().size() > 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void updateTimeData() {
/* 546 */     EpicEntity[] epicEnts = EpicServerStatus.getValrei().getAllEntities();
/*     */     
/* 548 */     if (epicEnts != null) {
/*     */       
/* 550 */       long now = System.currentTimeMillis();
/* 551 */       for (EpicEntity ent : epicEnts) {
/*     */         
/* 553 */         long id = ent.getId();
/* 554 */         long remaining = ent.getTimeUntilLeave() - now;
/* 555 */         updateEntityTime(id, remaining);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void collectLocalData() {
/* 562 */     EpicEntity[] epicEnts = EpicServerStatus.getValrei().getAllEntities();
/*     */     
/* 564 */     if (epicEnts != null) {
/*     */       
/* 566 */       long now = System.currentTimeMillis();
/* 567 */       for (EpicEntity ent : epicEnts) {
/*     */         
/* 569 */         long id = ent.getId();
/* 570 */         int hexId = (ent.getMapHex() != null) ? ent.getMapHex().getId() : -1;
/* 571 */         int type = ent.getType();
/* 572 */         int targetHex = ent.getTargetHex();
/* 573 */         String name = ent.getName();
/* 574 */         long remaining = ent.getTimeToNextHex() - now;
/* 575 */         float attack = ent.getAttack();
/* 576 */         float vitality = ent.getVitality();
/* 577 */         float bodyStr = ent.getCurrentSkill(102);
/* 578 */         float bodySta = ent.getCurrentSkill(103);
/* 579 */         float bodyCon = ent.getCurrentSkill(104);
/* 580 */         float mindLog = ent.getCurrentSkill(100);
/* 581 */         float mindSpe = ent.getCurrentSkill(101);
/* 582 */         float soulStr = ent.getCurrentSkill(105);
/* 583 */         float soulDep = ent.getCurrentSkill(106);
/*     */         
/* 585 */         List<CollectedValreiItem> carried = CollectedValreiItem.fromList(ent.getAllCollectedItems());
/*     */         
/* 587 */         updateEntity(id, hexId, type, targetHex, name, remaining, bodyStr, bodySta, bodyCon, mindLog, mindSpe, soulStr, soulDep, carried);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void updateFromEpicEntity(EpicEntity ent) {
/* 594 */     if (Servers.localServer.LOGINSERVER) {
/*     */       
/* 596 */       long now = System.currentTimeMillis();
/* 597 */       WCValreiMapUpdater updater = new WCValreiMapUpdater(WurmId.getNextWCCommandId(), (byte)2);
/* 598 */       updater.setEntityToUpdate(ent);
/* 599 */       updater.sendFromLoginServer();
/*     */       
/* 601 */       long id = ent.getId();
/* 602 */       int hexId = (ent.getMapHex() != null) ? ent.getMapHex().getId() : -1;
/* 603 */       int type = ent.getType();
/* 604 */       int targetHex = ent.getTargetHex();
/* 605 */       String name = ent.getName();
/* 606 */       long remaining = ent.getTimeUntilLeave() - now;
/* 607 */       float attack = ent.getAttack();
/* 608 */       float vitality = ent.getVitality();
/* 609 */       float bodyStr = ent.getCurrentSkill(102);
/* 610 */       float bodySta = ent.getCurrentSkill(103);
/* 611 */       float bodyCon = ent.getCurrentSkill(104);
/* 612 */       float mindLog = ent.getCurrentSkill(100);
/* 613 */       float mindSpe = ent.getCurrentSkill(101);
/* 614 */       float soulStr = ent.getCurrentSkill(105);
/* 615 */       float soulDep = ent.getCurrentSkill(106);
/*     */       
/* 617 */       List<CollectedValreiItem> carried = CollectedValreiItem.fromList(ent.getAllCollectedItems());
/*     */       
/* 619 */       updateEntity(id, hexId, type, targetHex, name, remaining, bodyStr, bodySta, bodyCon, mindLog, mindSpe, soulStr, soulDep, carried);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\ValreiMapData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */