/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.webinterface.WCValreiMapUpdater;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
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
/*     */ public class MapHex
/*     */   implements MiscConstants
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(MapHex.class.getName());
/*     */ 
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final int type;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final float moveCost;
/*     */   
/*  59 */   private String presenceStringOne = " is in ";
/*     */   
/*  61 */   private String prepositionString = " in ";
/*     */   
/*  63 */   private String leavesStringOne = " leaves ";
/*     */   
/*  65 */   private static final Random rand = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private final LinkedList<Integer> nearHexes = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final LinkedList<EpicEntity> entities = new LinkedList<>();
/*     */ 
/*     */   
/*  81 */   private final Set<EpicEntity> visitedBy = new HashSet<>();
/*     */ 
/*     */   
/*  84 */   private long spawnEntityId = 0L;
/*     */ 
/*     */   
/*  87 */   private long homeEntityId = 0L;
/*     */ 
/*     */   
/*     */   public static final int TYPE_STANDARD = 0;
/*     */ 
/*     */   
/*     */   public static final int TYPE_TRAP = 1;
/*     */ 
/*     */   
/*     */   public static final int TYPE_SLOW = 2;
/*     */ 
/*     */   
/*     */   public static final int TYPE_ENHANCE_STRENGTH = 3;
/*     */ 
/*     */   
/*     */   public static final int TYPE_ENHANCE_VITALITY = 4;
/*     */ 
/*     */   
/*     */   public static final int TYPE_TELEPORT = 5;
/*     */ 
/*     */   
/*     */   private final HexMap myMap;
/*     */ 
/*     */   
/*     */   private static final String addVisitedBy = "INSERT INTO VISITED(ENTITYID,HEXID) VALUES (?,?)";
/*     */ 
/*     */   
/*     */   private static final String clearVisitedHex = "DELETE FROM VISITED WHERE HEXID=?";
/*     */ 
/*     */   
/*     */   MapHex(HexMap map, int hexNumber, String hexName, float hexMoveCost, int hexType) {
/* 118 */     this.id = hexNumber;
/* 119 */     this.name = hexName;
/* 120 */     this.moveCost = Math.max(0.5F, hexMoveCost);
/* 121 */     this.type = hexType;
/* 122 */     this.myMap = map;
/* 123 */     map.addMapHex(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getId() {
/* 133 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 138 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String getEnemyStatus(EpicEntity entity) {
/* 147 */     StringBuilder build = new StringBuilder();
/* 148 */     if (entity.isCollectable() || entity.isSource())
/* 149 */       return ""; 
/* 150 */     for (EpicEntity e : this.entities) {
/*     */       
/* 152 */       if (e != entity)
/*     */       {
/* 154 */         if (!e.isCollectable() && !e.isSource()) {
/*     */           
/* 156 */           if (e.isWurm()) {
/*     */             
/* 158 */             if (build.length() > 0)
/* 159 */               build.append(' '); 
/* 160 */             build.append(entity.getName() + " is battling the Wurm.");
/*     */           }
/* 162 */           else if (e.isSentinelMonster()) {
/*     */             
/* 164 */             if (build.length() > 0)
/* 165 */               build.append(' '); 
/* 166 */             build.append(entity.getName() + " is trying to defeat the " + e.getName() + ".");
/*     */           }
/* 168 */           else if (e.isEnemy(entity)) {
/*     */             
/* 170 */             if (build.length() > 0)
/* 171 */               build.append(' '); 
/* 172 */             build.append(entity.getName() + " is fighting " + e.getName() + ".");
/*     */           }
/* 174 */           else if (entity.getCompanion() == e) {
/*     */             
/* 176 */             if (build.length() > 0)
/* 177 */               build.append(' '); 
/* 178 */             build.append(entity.getName() + " is meeting with " + e.getName() + ".");
/*     */           } 
/* 180 */           if (e.isAlly()) {
/*     */             
/* 182 */             if (build.length() > 0)
/* 183 */               build.append(' '); 
/* 184 */             build.append(entity.getName() + " visits the " + e.getName() + ".");
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 189 */     return build.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getSpawnEntityId() {
/* 199 */     return this.spawnEntityId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getHomeEntityId() {
/* 209 */     return this.homeEntityId;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getOwnPresenceString() {
/* 214 */     return " is home" + getFullPrepositionString();
/*     */   }
/*     */ 
/*     */   
/*     */   final String getFullPresenceString() {
/* 219 */     return getPresenceStringOne() + this.name + ".";
/*     */   }
/*     */ 
/*     */   
/*     */   final String getFullPrepositionString() {
/* 224 */     return getPrepositionString() + this.name + ".";
/*     */   }
/*     */ 
/*     */   
/*     */   final float getMoveCost() {
/* 229 */     return this.moveCost;
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
/*     */   HexMap getMyMap() {
/* 241 */     return this.myMap;
/*     */   }
/*     */ 
/*     */   
/*     */   final void setPresenceStringOne(String ps) {
/* 246 */     this.presenceStringOne = ps;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getPresenceStringOne() {
/* 251 */     return this.presenceStringOne;
/*     */   }
/*     */ 
/*     */   
/*     */   final void setPrepositionString(String ps) {
/* 256 */     this.prepositionString = ps;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getPrepositionString() {
/* 261 */     return this.prepositionString;
/*     */   }
/*     */ 
/*     */   
/*     */   final void setLeavesStringOne(String ps) {
/* 266 */     this.leavesStringOne = ps;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getLeavesStringOne() {
/* 271 */     return this.leavesStringOne;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int getType() {
/* 281 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void addEntity(EpicEntity entity) {
/* 290 */     if (!this.entities.contains(entity)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 298 */       this.entities.add(entity);
/* 299 */       entity.setMapHex(this);
/* 300 */       if (entity.isWurm() || entity.isDeity()) {
/*     */         
/* 302 */         if (entity.getAttack() > entity.getInitialAttack())
/* 303 */           entity.setAttack(entity.getAttack() - 0.1F); 
/* 304 */         if (entity.getVitality() > entity.getInitialVitality()) {
/* 305 */           entity.setVitality(entity.getVitality() - 0.1F);
/* 306 */         } else if (entity.getVitality() < entity.getInitialVitality()) {
/* 307 */           entity.setVitality(entity.getVitality() + 0.1F);
/*     */         } 
/* 309 */       } else if (entity.isCollectable() || entity.isSource()) {
/* 310 */         clearVisitedBy();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   final void removeEntity(EpicEntity entity, boolean load) {
/* 316 */     if (this.entities.contains(entity)) {
/*     */       
/* 318 */       this.entities.remove(entity);
/* 319 */       entity.setMapHex(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean checkLeaveStatus(EpicEntity entity) {
/* 325 */     return setEntityEffects(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Integer[] getNearMapHexes() {
/* 330 */     return this.nearHexes.<Integer>toArray(new Integer[this.nearHexes.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   final void addNearHex(int hexId) {
/* 335 */     this.nearHexes.add(Integer.valueOf(hexId));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void addNearHexes(int hexId1, int hexId2, int hexId3, int hexId4, int hexId5, int hexId6) {
/* 341 */     this.nearHexes.add(Integer.valueOf(hexId1));
/* 342 */     this.nearHexes.add(Integer.valueOf(hexId2));
/* 343 */     this.nearHexes.add(Integer.valueOf(hexId3));
/* 344 */     this.nearHexes.add(Integer.valueOf(hexId4));
/* 345 */     this.nearHexes.add(Integer.valueOf(hexId5));
/* 346 */     this.nearHexes.add(Integer.valueOf(hexId6));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean isVisitedBy(EpicEntity entity) {
/* 355 */     for (EpicEntity ent : this.entities) {
/*     */ 
/*     */       
/* 358 */       if (ent.isCollectable() || ent.isSource())
/* 359 */         return false; 
/*     */     } 
/* 361 */     if (this.visitedBy.contains(entity))
/* 362 */       return true; 
/* 363 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final void addVisitedBy(EpicEntity entity, boolean load) {
/* 368 */     if (this.visitedBy != null && !this.visitedBy.contains(entity)) {
/*     */       
/* 370 */       this.visitedBy.add(entity);
/* 371 */       if (!load) {
/*     */         
/* 373 */         Connection dbcon = null;
/* 374 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 377 */           dbcon = DbConnector.getDeityDbCon();
/* 378 */           ps = dbcon.prepareStatement("INSERT INTO VISITED(ENTITYID,HEXID) VALUES (?,?)");
/* 379 */           ps.setLong(1, entity.getId());
/* 380 */           ps.setInt(2, getId());
/* 381 */           ps.executeUpdate();
/*     */         }
/* 383 */         catch (SQLException sqx) {
/*     */           
/* 385 */           logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */         }
/*     */         finally {
/*     */           
/* 389 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 390 */           DbConnector.returnConnection(dbcon);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final void clearVisitedBy() {
/* 398 */     this.visitedBy.clear();
/* 399 */     Connection dbcon = null;
/* 400 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 403 */       dbcon = DbConnector.getDeityDbCon();
/* 404 */       ps = dbcon.prepareStatement("DELETE FROM VISITED WHERE HEXID=?");
/* 405 */       ps.setInt(1, getId());
/* 406 */       ps.executeUpdate();
/*     */     }
/* 408 */     catch (SQLException sqx) {
/*     */       
/* 410 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 414 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 415 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   LinkedList<Integer> cloneNearHexes() {
/* 421 */     LinkedList<Integer> clone = new LinkedList<>();
/* 422 */     for (Integer i : this.nearHexes)
/*     */     {
/* 424 */       clone.add(i);
/*     */     }
/* 426 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean containsWurm() {
/* 431 */     for (EpicEntity e : this.entities) {
/*     */       
/* 433 */       if (e.isWurm())
/* 434 */         return true; 
/*     */     } 
/* 436 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean containsEnemy(EpicEntity toCheck) {
/* 441 */     for (EpicEntity e : this.entities) {
/*     */       
/* 443 */       if (e.isEnemy(toCheck))
/* 444 */         return true; 
/*     */     } 
/* 446 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean containsMonsterOrHelper() {
/* 451 */     for (EpicEntity e : this.entities) {
/*     */       
/* 453 */       if (e.isSentinelMonster() || e.isAlly())
/* 454 */         return true; 
/*     */     } 
/* 456 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean containsDeity() {
/* 461 */     for (EpicEntity e : this.entities) {
/*     */       
/* 463 */       if (e.isDeity())
/* 464 */         return true; 
/*     */     } 
/* 466 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean mayEnter(EpicEntity entity) {
/* 475 */     if (entity.isWurm() && containsMonsterOrHelper())
/* 476 */       return containsDeity(); 
/* 477 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getNextHexToWinPoint(EpicEntity entity) {
/* 486 */     if (entity.mustReturnHomeToWin()) {
/*     */       
/* 488 */       MapHex home = this.myMap.getSpawnHex(entity);
/* 489 */       if (home != null)
/*     */       {
/* 491 */         if (home != this)
/*     */         {
/* 493 */           return findClosestHexTo(home.getId(), entity, true);
/*     */         }
/*     */       }
/* 496 */       return getId();
/*     */     } 
/* 498 */     return findClosestHexTo(this.myMap.getHexNumRequiredToWin(), entity, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int findClosestHexTo(int target, EpicEntity entity, boolean avoidEnemies) {
/* 507 */     logger.log(Level.INFO, entity.getName() + " at " + getId() + " pathing to " + target);
/* 508 */     Map<Integer, Integer> steps = new HashMap<>();
/* 509 */     LinkedList<Integer> copy = cloneNearHexes();
/* 510 */     while (copy.size() > 0) {
/*     */       
/* 512 */       Integer i = copy.remove(rand.nextInt(copy.size()));
/* 513 */       if (i.intValue() == target)
/* 514 */         return target; 
/* 515 */       MapHex hex = this.myMap.getMapHex(i);
/*     */       
/* 517 */       if (hex.mayEnter(entity))
/*     */       {
/* 519 */         if (!avoidEnemies || !hex.containsEnemy(entity)) {
/*     */           
/* 521 */           Set<Integer> checked = new HashSet<>();
/* 522 */           checked.add(i);
/* 523 */           int numSteps = findNextHex(checked, hex, target, entity, avoidEnemies, 0);
/* 524 */           steps.put(Integer.valueOf(hex.getId()), Integer.valueOf(numSteps));
/*     */         } 
/*     */       }
/*     */     } 
/* 528 */     int minSteps = 100;
/* 529 */     int hexNum = 0;
/* 530 */     for (Map.Entry<Integer, Integer> entry : steps.entrySet()) {
/*     */       
/* 532 */       int csteps = ((Integer)entry.getValue()).intValue();
/* 533 */       if (csteps < minSteps) {
/*     */         
/* 535 */         minSteps = csteps;
/* 536 */         hexNum = ((Integer)entry.getKey()).intValue();
/*     */       } 
/*     */     } 
/* 539 */     return hexNum;
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
/*     */   int findNextHex(Set<Integer> checked, MapHex startHex, int targetHexId, EpicEntity entity, boolean avoidEnemies, int counter) {
/* 552 */     LinkedList<Integer> nearClone = startHex.cloneNearHexes();
/* 553 */     int minNum = 100;
/* 554 */     while (nearClone.size() > 0) {
/*     */       
/* 556 */       Integer ni = nearClone.remove(rand.nextInt(nearClone.size()));
/* 557 */       if (ni.intValue() == targetHexId)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 562 */         return counter;
/*     */       }
/* 564 */       if (!checked.contains(ni)) {
/*     */         
/* 566 */         checked.add(ni);
/* 567 */         if (counter < 6) {
/*     */           
/* 569 */           MapHex nearhex = this.myMap.getMapHex(ni);
/* 570 */           if (nearhex.mayEnter(entity))
/*     */           {
/* 572 */             if (!avoidEnemies || !nearhex.containsEnemy(entity)) {
/*     */               
/* 574 */               int steps = findNextHex(checked, nearhex, targetHexId, entity, avoidEnemies, ++counter);
/* 575 */               if (steps < minNum)
/*     */               {
/* 577 */                 minNum = steps;
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 584 */     return minNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int findNextHex(EpicEntity entity) {
/* 593 */     if (this.nearHexes.isEmpty()) {
/*     */       
/* 595 */       logger.log(Level.WARNING, "Near hexes is empty for map " + getId());
/* 596 */       return 0;
/*     */     } 
/* 598 */     if (entity.hasEnoughCollectablesToWin()) {
/*     */       
/* 600 */       if (getId() == this.myMap.getHexNumRequiredToWin())
/* 601 */         return getId(); 
/* 602 */       return getNextHexToWinPoint(entity);
/*     */     } 
/* 604 */     LinkedList<Integer> copy = cloneNearHexes();
/* 605 */     while (copy.size() > 0) {
/*     */       
/* 607 */       Integer i = copy.remove(rand.nextInt(copy.size()));
/* 608 */       MapHex hex = this.myMap.getMapHex(i);
/* 609 */       if (hex.mayEnter(entity)) {
/*     */         
/* 611 */         if (entity.isWurm())
/* 612 */           return hex.getId(); 
/* 613 */         if (!hex.isVisitedBy(entity)) {
/* 614 */           return hex.getId();
/*     */         }
/*     */       } 
/*     */     } 
/* 618 */     copy = cloneNearHexes();
/* 619 */     while (copy.size() > 0) {
/*     */       
/* 621 */       Integer i = copy.remove(rand.nextInt(copy.size()));
/* 622 */       MapHex hex = this.myMap.getMapHex(i);
/* 623 */       if (hex.mayEnter(entity)) {
/*     */         
/* 625 */         LinkedList<Integer> nearClone = hex.cloneNearHexes();
/* 626 */         while (nearClone.size() > 0) {
/*     */           
/* 628 */           Integer ni = nearClone.remove(rand.nextInt(nearClone.size()));
/* 629 */           MapHex nearhex = this.myMap.getMapHex(ni);
/* 630 */           if (!nearhex.isVisitedBy(entity)) {
/* 631 */             return hex.getId();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 637 */     copy = cloneNearHexes();
/* 638 */     while (copy.size() > 0) {
/*     */       
/* 640 */       Integer i = copy.remove(rand.nextInt(copy.size()));
/* 641 */       MapHex hex = this.myMap.getMapHex(i);
/* 642 */       if (hex.mayEnter(entity))
/*     */       {
/* 644 */         return i.intValue();
/*     */       }
/*     */     } 
/* 647 */     logger.log(Level.INFO, entity.getName() + " Failed to take random step to neighbour.");
/* 648 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrap() {
/* 653 */     return (this.type == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTeleport() {
/* 658 */     return (this.type == 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSlow() {
/* 663 */     return (this.type == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   int getSlowModifier() {
/* 668 */     return isSlow() ? 2 : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean resolveDispute(EpicEntity entity) {
/* 673 */     EpicEntity enemy = null;
/* 674 */     for (EpicEntity e : this.entities) {
/* 675 */       if (e != entity && e.isEnemy(entity)) {
/*     */         
/* 677 */         if (enemy == null) {
/* 678 */           enemy = e; continue;
/* 679 */         }  if (Server.rand.nextBoolean())
/* 680 */           enemy = e; 
/*     */       } 
/*     */     } 
/* 683 */     if (enemy == null) {
/* 684 */       return true;
/*     */     }
/* 686 */     ValreiFight vFight = new ValreiFight(this, entity, enemy);
/* 687 */     ValreiFightHistory fightHistory = vFight.completeFight(false);
/* 688 */     ValreiFightHistoryManager.getInstance().addFight(fightHistory.getFightId(), fightHistory);
/*     */     
/* 690 */     if (Servers.localServer.LOGINSERVER) {
/*     */       
/* 692 */       WCValreiMapUpdater updater = new WCValreiMapUpdater(WurmId.getNextWCCommandId(), (byte)5);
/* 693 */       updater.sendFromLoginServer();
/*     */     } 
/*     */     
/* 696 */     if (fightHistory.getFightWinner() == entity.getId()) {
/*     */       
/* 698 */       fightEndEffects(entity, enemy);
/* 699 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 703 */     fightEndEffects(enemy, entity);
/* 704 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void fightEndEffects(EpicEntity winner, EpicEntity loser) {
/* 710 */     if (loser.isWurm()) {
/* 711 */       winner.broadCastWithName(" wards off " + loser.getName() + getFullPrepositionString());
/* 712 */     } else if (winner.isWurm()) {
/* 713 */       loser.broadCastWithName(" is defeated by " + winner.getName() + getFullPrepositionString());
/* 714 */     } else if (loser.isSentinelMonster()) {
/* 715 */       winner.broadCastWithName(" prevails against " + loser.getName() + getFullPrepositionString());
/*     */     } else {
/* 717 */       loser.broadCastWithName(" is vanquished by " + winner.getName() + getFullPrepositionString());
/* 718 */     }  loser.dropAll(winner.isDemigod());
/* 719 */     removeEntity(loser, false);
/*     */ 
/*     */     
/* 722 */     addVisitedBy(loser, false);
/* 723 */     if (loser.isDemigod()) {
/* 724 */       this.myMap.destroyEntity(loser);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean resolveDisputeDeprecated(EpicEntity entity) {
/* 735 */     EpicEntity enemy = null;
/* 736 */     EpicEntity enemy2 = null;
/* 737 */     EpicEntity helper = null;
/* 738 */     EpicEntity friend = null;
/* 739 */     for (EpicEntity e : this.entities) {
/*     */       
/* 741 */       if (e != entity) {
/*     */         
/* 743 */         if (e.isEnemy(entity)) {
/*     */           
/* 745 */           if (enemy == null) {
/* 746 */             enemy = e;
/*     */           } else {
/* 748 */             enemy2 = e;
/*     */           } 
/* 750 */         } else if (e.isAlly() && e.isFriend(entity)) {
/* 751 */           helper = e;
/* 752 */         }  if (e.isDeity() || e.isDemigod() || entity.isFriend(e)) {
/* 753 */           friend = e;
/*     */         }
/*     */       } 
/*     */     } 
/* 757 */     if (friend != null)
/*     */     {
/* 759 */       if (friend.countCollectables() > 0 && entity.countCollectables() > 0 && 
/* 760 */         entity.isDeity())
/* 761 */         friend.giveCollectables(entity); 
/*     */     }
/* 763 */     if (enemy != null)
/*     */     {
/*     */       while (true) {
/*     */         
/* 767 */         if (enemy != null) {
/*     */           
/* 769 */           if (attack(enemy, entity)) {
/* 770 */             return false;
/*     */           }
/* 772 */           if (attack(entity, enemy)) {
/*     */             
/* 774 */             enemy = null;
/* 775 */             if (enemy2 == null)
/* 776 */               return true; 
/*     */           } 
/* 778 */           if (helper != null)
/*     */           {
/* 780 */             if (attack(helper, enemy)) {
/*     */               
/* 782 */               enemy = null;
/* 783 */               if (enemy2 == null)
/* 784 */                 return true; 
/*     */             } 
/*     */           }
/*     */         } 
/* 788 */         if (enemy2 != null) {
/*     */ 
/*     */           
/* 791 */           if (attack(entity, enemy2)) {
/*     */             
/* 793 */             enemy2 = null;
/* 794 */             if (enemy == null)
/* 795 */               return true; 
/*     */             continue;
/*     */           } 
/* 798 */           if (attack(enemy2, entity))
/* 799 */             return false; 
/*     */         } 
/*     */       } 
/*     */     }
/* 803 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean attack(EpicEntity entity, EpicEntity enemy) {
/* 808 */     if (entity.rollAttack())
/*     */     {
/* 810 */       if (enemy.setVitality(enemy.getVitality() - 1.0F)) {
/*     */         
/* 812 */         if (enemy.isWurm()) {
/* 813 */           entity.broadCastWithName(" wards off " + enemy.getName() + getFullPrepositionString());
/* 814 */         } else if (entity.isWurm()) {
/* 815 */           enemy.broadCastWithName(" is defeated by " + entity.getName() + getFullPrepositionString());
/* 816 */         } else if (enemy.isSentinelMonster()) {
/* 817 */           entity.broadCastWithName(" prevails against " + enemy.getName() + getFullPrepositionString());
/*     */         } else {
/* 819 */           enemy.broadCastWithName(" is vanquished by " + entity.getName() + getFullPrepositionString());
/* 820 */         }  enemy.dropAll(entity.isDemigod());
/* 821 */         removeEntity(enemy, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 828 */         addVisitedBy(enemy, false);
/* 829 */         if (enemy.isDemigod())
/*     */         {
/* 831 */           this.myMap.destroyEntity(enemy);
/*     */         }
/* 833 */         return true;
/*     */       } 
/*     */     }
/* 836 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getCollectibleName() {
/* 844 */     for (ListIterator<EpicEntity> lit = this.entities.listIterator(); lit.hasNext(); ) {
/*     */       
/* 846 */       EpicEntity next = lit.next();
/* 847 */       if (next.isCollectable())
/*     */       {
/* 849 */         return next.getName();
/*     */       }
/*     */     } 
/* 852 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   protected final int countCollectibles() {
/* 857 */     int toret = 0;
/* 858 */     for (ListIterator<EpicEntity> lit = this.entities.listIterator(); lit.hasNext(); ) {
/*     */       
/* 860 */       EpicEntity next = lit.next();
/* 861 */       if (next.isCollectable())
/*     */       {
/* 863 */         toret++;
/*     */       }
/*     */     } 
/* 866 */     return toret;
/*     */   }
/*     */ 
/*     */   
/*     */   private final void pickupStuff(EpicEntity entity) {
/* 871 */     for (ListIterator<EpicEntity> lit = this.entities.listIterator(); lit.hasNext(); ) {
/*     */       
/* 873 */       EpicEntity next = lit.next();
/* 874 */       if (next.isCollectable() || next.isSource()) {
/*     */         
/* 876 */         entity.logWithName(" found " + next.getName() + ".");
/* 877 */         lit.remove();
/* 878 */         next.setMapHex(null);
/* 879 */         next.setCarrier(entity, true, false, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStrength() {
/* 886 */     return (this.type == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVitality() {
/* 891 */     return (this.type == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean setEntityEffects(EpicEntity entity) {
/* 899 */     if (resolveDispute(entity)) {
/*     */       
/* 901 */       switch (this.type) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/* 912 */           if (entity.isDeity() || entity.isWurm()) {
/*     */             
/* 914 */             float current = entity.getCurrentSkill(102);
/* 915 */             entity.setSkill(102, current + (100.0F - current) / 1250.0F);
/* 916 */             current = entity.getCurrentSkill(104);
/* 917 */             entity.setSkill(104, current + (100.0F - current) / 1250.0F);
/* 918 */             current = entity.getCurrentSkill(105);
/* 919 */             entity.setSkill(105, current + (100.0F - current) / 1250.0F);
/*     */ 
/*     */             
/* 922 */             entity.broadCastWithName(" is strengthened by the influence of " + getName() + ".");
/*     */           } 
/*     */           break;
/*     */         case 4:
/* 926 */           if (entity.isDeity() || entity.isWurm()) {
/*     */             
/* 928 */             float current = entity.getCurrentSkill(100);
/* 929 */             entity.setSkill(100, current + (100.0F - current) / 1250.0F);
/* 930 */             current = entity.getCurrentSkill(103);
/* 931 */             entity.setSkill(103, current + (100.0F - current) / 1250.0F);
/* 932 */             current = entity.getCurrentSkill(101);
/* 933 */             entity.setSkill(101, current + (100.0F - current) / 1250.0F);
/*     */ 
/*     */             
/* 936 */             entity.broadCastWithName(" is vitalized by the influence of " + getName() + ".");
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 945 */       entity.setVitality(Math.max(entity.getInitialVitality() / 2.0F, entity.getVitality()), false);
/* 946 */       pickupStuff(entity);
/* 947 */       addVisitedBy(entity, false);
/* 948 */       return true;
/*     */     } 
/* 950 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   long getEntitySpawn() {
/* 955 */     return this.spawnEntityId;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isSpawnFor(long entityId) {
/* 960 */     return (this.spawnEntityId == entityId);
/*     */   }
/*     */ 
/*     */   
/*     */   void setSpawnEntityId(long entityId) {
/* 965 */     this.spawnEntityId = entityId;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isSpawn() {
/* 970 */     return (this.spawnEntityId != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHomeFor(long entityId) {
/* 975 */     return (this.homeEntityId == entityId);
/*     */   }
/*     */ 
/*     */   
/*     */   void setHomeEntityId(long entityId) {
/* 980 */     this.homeEntityId = entityId;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\MapHex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */