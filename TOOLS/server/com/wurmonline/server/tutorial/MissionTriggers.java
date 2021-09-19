/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.effects.EffectFactory;
/*     */ import com.wurmonline.server.epic.EpicMission;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.MissionManager;
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class MissionTriggers
/*     */   implements CounterTypes, MiscConstants
/*     */ {
/*  66 */   private static Logger logger = Logger.getLogger(MissionTriggers.class.getName());
/*     */   
/*     */   private static final String LOADALLTRIGGERS = "SELECT * FROM MISSIONTRIGGERS";
/*     */   
/*  70 */   private static final Map<Integer, MissionTrigger> triggers = new ConcurrentHashMap<>();
/*     */   
/*     */   public static final int SHOW_ALL = 0;
/*     */   
/*     */   public static final int SHOW_LINKED = 1;
/*     */   
/*     */   public static final int SHOW_UNLINKED = 2;
/*     */   
/*     */   static {
/*     */     try {
/*  80 */       loadAllTriggers();
/*     */     }
/*  82 */     catch (Exception ex) {
/*     */       
/*  84 */       logger.log(Level.WARNING, "Problems loading all Mission Triggers", ex);
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
/*     */   public static void addMissionTrigger(MissionTrigger trigger) {
/*  98 */     triggers.put(Integer.valueOf(trigger.getId()), trigger);
/*  99 */     MissionTargets.addMissionTrigger(trigger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTrigger[] getAllTriggers() {
/* 104 */     return (MissionTrigger[])triggers.values().toArray((Object[])new MissionTrigger[triggers.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTrigger getRespawnTriggerForMission(int missionId, float state) {
/* 109 */     MissionTrigger[] trigs = getAllTriggers();
/* 110 */     float foundState = -100.0F;
/* 111 */     MissionTrigger toret = null;
/* 112 */     for (int x = 0; x < trigs.length; x++) {
/*     */       
/* 114 */       if (trigs[x].getMissionRequired() == missionId)
/*     */       {
/*     */         
/* 117 */         if (trigs[x].getStateRequired() < state)
/*     */         {
/* 119 */           if (trigs[x].isSpawnPoint() && foundState < trigs[x].getStateRequired()) {
/*     */             
/* 121 */             foundState = trigs[x].getStateRequired();
/* 122 */             toret = trigs[x];
/*     */           } 
/*     */         }
/*     */       }
/*     */     } 
/* 127 */     return toret;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumTriggers() {
/* 132 */     return triggers.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static MissionTrigger[] getFilteredTriggers(Creature creature, int triggerId, byte creatorType, long listForUser, boolean dontListMine, boolean listMineOnly) {
/* 138 */     Set<MissionTrigger> trigs = new HashSet<>();
/* 139 */     for (MissionTrigger trig : triggers.values()) {
/*     */       
/* 141 */       boolean own = (trig.getOwnerId() == creature.getWurmId());
/* 142 */       boolean show = (creature.getPower() > 0 || own);
/* 143 */       boolean userMatch = (trig.getOwnerId() == listForUser);
/* 144 */       if (triggerId > 0 && trig.getId() == triggerId) {
/* 145 */         show = true;
/* 146 */       } else if (own) {
/*     */         
/* 148 */         if (dontListMine) {
/* 149 */           show = false;
/*     */         }
/* 151 */       } else if (listMineOnly) {
/*     */         
/* 153 */         show = false;
/* 154 */         if (listForUser != -10L && userMatch) {
/* 155 */           show = true;
/*     */         }
/* 157 */       } else if (listForUser != -10L) {
/*     */         
/* 159 */         show = false;
/* 160 */         if (userMatch)
/* 161 */           show = true; 
/*     */       } 
/* 163 */       if (creatorType == 2 && creature.getPower() < MissionManager.CAN_SEE_EPIC_MISSIONS)
/*     */       {
/* 165 */         show = false;
/*     */       }
/* 167 */       if (show)
/* 168 */         trigs.add(trig); 
/*     */     } 
/* 170 */     return trigs.<MissionTrigger>toArray(new MissionTrigger[trigs.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static MissionTrigger[] getFilteredTriggers(Creature creature, int linked, boolean includeInactive, int missionId, int triggerId) {
/* 176 */     Set<MissionTrigger> trigs = new HashSet<>();
/* 177 */     if (triggerId != 0) {
/*     */       
/* 179 */       MissionTrigger trig = triggers.get(Integer.valueOf(triggerId));
/* 180 */       if (trig != null && showTrigger(trig, creature, linked, includeInactive, missionId)) {
/* 181 */         trigs.add(trig);
/*     */       }
/*     */     } else {
/*     */       
/* 185 */       for (MissionTrigger trig : triggers.values()) {
/*     */         
/* 187 */         if (showTrigger(trig, creature, linked, includeInactive, missionId))
/* 188 */           trigs.add(trig); 
/*     */       } 
/*     */     } 
/* 191 */     return trigs.<MissionTrigger>toArray(new MissionTrigger[trigs.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean showTrigger(MissionTrigger trig, Creature creature, int linked, boolean includeInactive, int missionId) {
/* 197 */     boolean show = false;
/* 198 */     if (missionId == 0 || trig.getMissionRequired() == missionId) {
/*     */       
/* 200 */       if (missionId == 0) {
/*     */         
/* 202 */         switch (linked) {
/*     */           
/*     */           case 0:
/* 205 */             show = true;
/*     */             break;
/*     */           case 1:
/* 208 */             show = (trig.getMissionRequired() != 0);
/*     */             break;
/*     */           case 2:
/* 211 */             show = (trig.getMissionRequired() == 0);
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       } else {
/* 218 */         show = true;
/*     */       } 
/* 220 */       if (!includeInactive && !trig.isInactive())
/* 221 */         show = false; 
/* 222 */       if (show && creature.getPower() == 0 && trig.getOwnerId() != creature.getWurmId())
/* 223 */         show = false; 
/*     */     } 
/* 225 */     return show;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTrigger[] getMissionTriggers(int missionId) {
/* 230 */     Set<MissionTrigger> trigs = new HashSet<>();
/* 231 */     for (MissionTrigger m : triggers.values()) {
/*     */       
/* 233 */       if (m.getMissionRequired() == missionId)
/*     */       {
/* 235 */         trigs.add(m);
/*     */       }
/*     */     } 
/* 238 */     return trigs.<MissionTrigger>toArray(new MissionTrigger[trigs.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasMissionTriggers(int missionId) {
/* 243 */     for (MissionTrigger m : triggers.values()) {
/*     */       
/* 245 */       if (m.getMissionRequired() == missionId)
/*     */       {
/* 247 */         return true;
/*     */       }
/*     */     } 
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTrigger[] getMissionTriggersWith(int itemUsed, int action, long target) {
/* 255 */     Set<MissionTrigger> trigs = new HashSet<>();
/* 256 */     for (MissionTrigger m : triggers.values()) {
/*     */       
/* 258 */       if (m.getOnActionPerformed() <= 0 || m.getOnActionPerformed() == action)
/*     */       {
/* 260 */         if (m.getItemUsedId() <= 0 || m.getItemUsedId() == itemUsed) {
/*     */ 
/*     */ 
/*     */           
/* 264 */           if (m.getItemUsedId() <= 0 && action == 142 && 
/* 265 */             m.getItemUsedId() != itemUsed) {
/*     */             continue;
/*     */           }
/* 268 */           if ((WurmId.getType(target) == 3 && WurmId.getType(m.getTarget()) == 3) || (
/* 269 */             WurmId.getType(target) == 17 && WurmId.getType(m.getTarget()) == 17)) {
/*     */             
/* 271 */             int x = Tiles.decodeTileX(target);
/* 272 */             int y = Tiles.decodeTileY(target);
/* 273 */             int x2 = Tiles.decodeTileX(m.getTarget());
/* 274 */             int y2 = Tiles.decodeTileY(m.getTarget());
/*     */             
/* 276 */             if (x == x2 && y == y2)
/* 277 */               trigs.add(m);  continue;
/*     */           } 
/* 279 */           if (m.getTarget() <= 0L || m.getTarget() == target) {
/*     */             
/* 281 */             EpicMission mis = EpicServerStatus.getEpicMissionForMission(m.getMissionRequired());
/* 282 */             if (mis != null)
/*     */             {
/* 284 */               if (mis.getMissionType() == 116 && !Servers.localServer.PVPSERVER) {
/*     */                 
/*     */                 try {
/*     */                   
/* 288 */                   Creature killed = Creatures.getInstance().getCreature(target);
/* 289 */                   if (killed.getStatus().getModType() != 99) {
/*     */                     continue;
/*     */                   }
/* 292 */                 } catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */               }
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 299 */             trigs.add(m);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 304 */     return trigs.<MissionTrigger>toArray(new MissionTrigger[trigs.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTrigger[] getMissionTriggerPlate(int tilex, int tiley, int layer) {
/* 309 */     Set<MissionTrigger> trigs = new HashSet<>();
/* 310 */     for (MissionTrigger m : triggers.values()) {
/*     */       
/* 312 */       if (m.getOnActionPerformed() == 475) {
/*     */         
/* 314 */         if (layer == 0 && WurmId.getType(m.getTarget()) == 3) {
/*     */ 
/*     */           
/* 317 */           int x2 = Tiles.decodeTileX(m.getTarget());
/* 318 */           int y2 = Tiles.decodeTileY(m.getTarget());
/*     */           
/* 320 */           if (tilex == x2 && tiley == y2)
/* 321 */             trigs.add(m);  continue;
/*     */         } 
/* 323 */         if (layer < 0 && WurmId.getType(m.getTarget()) == 17) {
/*     */           
/* 325 */           int x2 = Tiles.decodeTileX(m.getTarget());
/* 326 */           int y2 = Tiles.decodeTileY(m.getTarget());
/*     */           
/* 328 */           if (tilex == x2 && tiley == y2)
/* 329 */             trigs.add(m); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 333 */     return trigs.<MissionTrigger>toArray(new MissionTrigger[trigs.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean activateTriggerPlate(Creature performer, int tilex, int tiley, int layer) {
/* 338 */     if (performer.isPlayer()) {
/*     */       
/* 340 */       MissionTrigger[] trigs = getMissionTriggerPlate(tilex, tiley, layer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 346 */       if (trigs.length > 0) {
/*     */         
/* 348 */         TriggerRun tr = new TriggerRun();
/* 349 */         tr.run(performer, trigs, 1);
/*     */         
/* 351 */         return tr.isTriggered();
/*     */       } 
/*     */     } 
/* 354 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean activateTriggers(Creature performer, int creatureTemplateId, int actionPerformed, long targetId, int counter) {
/* 360 */     boolean done = true;
/* 361 */     MissionTrigger[] trigs = getMissionTriggersWith(creatureTemplateId, actionPerformed, targetId);
/*     */ 
/*     */     
/* 364 */     if (trigs.length > 0) {
/*     */       
/* 366 */       TriggerRun tr = new TriggerRun();
/* 367 */       tr.run(performer, trigs, counter);
/* 368 */       done = tr.isDone();
/*     */     } 
/* 370 */     return done;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean activateTriggers(Creature performer, Item item, int actionPerformed, long targetId, int counter) {
/* 376 */     boolean done = true;
/* 377 */     if (performer.isPlayer()) {
/*     */       
/* 379 */       MissionTrigger[] trigs = getMissionTriggersWith((item != null) ? item.getTemplateId() : 0, actionPerformed, targetId);
/*     */ 
/*     */       
/* 382 */       performer.sendToLoggers("Found " + trigs.length + " triggers.", (byte)2);
/* 383 */       if (trigs.length > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 389 */         TriggerRun tr = new TriggerRun();
/* 390 */         tr.run(performer, trigs, counter);
/* 391 */         done = tr.isDone();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 396 */         if (tr.isTriggered()) {
/*     */           
/* 398 */           if (actionPerformed == 492)
/*     */           {
/* 400 */             EffectFactory.getInstance().deleteEffByOwner(targetId);
/*     */           }
/* 402 */           if (actionPerformed == 47 && item != null)
/*     */           {
/* 404 */             if (tr.getLastTrigger() != null && tr.getLastTrigger().getCreatorType() == 3) {
/*     */               
/* 406 */               if (WurmId.getType(targetId) == 1 || 
/* 407 */                 WurmId.getType(targetId) == 0) {
/*     */                 
/* 409 */                 item.putInVoid();
/*     */                 
/*     */                 try {
/* 412 */                   Creature targetC = Server.getInstance().getCreature(targetId);
/* 413 */                   targetC.getInventory().insertItem(item);
/*     */                 }
/* 415 */                 catch (NoSuchCreatureException nsc) {
/*     */                   
/* 417 */                   Items.destroyItem(item.getWurmId());
/*     */                 }
/* 419 */                 catch (NoSuchPlayerException nsp) {
/*     */                   
/* 421 */                   Items.destroyItem(item.getWurmId());
/*     */                 } 
/*     */               } 
/*     */             } else {
/*     */               
/* 426 */               Items.destroyItem(item.getWurmId());
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 431 */     return done;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isDoorOpen(Creature performer, long doorid, int counter) {
/* 436 */     MissionTarget targ = MissionTargets.getMissionTargetFor(doorid);
/* 437 */     if (targ != null) {
/*     */       
/* 439 */       MissionTrigger[] trigs = targ.getMissionTriggers();
/* 440 */       TriggerRun tr = new TriggerRun();
/* 441 */       tr.run(performer, trigs, counter);
/* 442 */       return tr.isOpenedDoor();
/*     */     } 
/* 444 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTrigger getTriggerWithId(int id) {
/* 449 */     return triggers.get(Integer.valueOf(id));
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
/*     */   public static String getTargetAsString(Creature creature, long target) {
/* 461 */     StringBuilder buf = new StringBuilder();
/* 462 */     if (target <= 0L) {
/*     */       
/* 464 */       buf.append("None");
/*     */     }
/* 466 */     else if (WurmId.getType(target) == 1) {
/*     */ 
/*     */       
/*     */       try {
/* 470 */         Creature c = Creatures.getInstance().getCreature(target);
/* 471 */         if (creature.getPower() > 0) {
/* 472 */           buf.append(c.getName() + " at " + c.getTileX() + "," + c.getTileY());
/*     */         } else {
/* 474 */           buf.append(c.getName());
/*     */         } 
/* 476 */       } catch (NoSuchCreatureException nsc) {
/*     */         
/* 478 */         buf.append("Nonexistant creature.");
/*     */       }
/*     */     
/* 481 */     } else if (WurmId.getType(target) == 0) {
/*     */ 
/*     */       
/*     */       try {
/* 485 */         Player p = Players.getInstance().getPlayer(target);
/* 486 */         if (creature.getPower() > 0) {
/* 487 */           buf.append(p.getName() + " at " + p.getTileX() + "," + p.getTileY());
/*     */         } else {
/* 489 */           buf.append(p.getName());
/*     */         } 
/* 491 */       } catch (NoSuchPlayerException nsc) {
/*     */         
/* 493 */         buf.append("Nonexistant creature.");
/*     */       }
/*     */     
/* 496 */     } else if (WurmId.getType(target) == 5) {
/*     */       
/* 498 */       int x = (int)(target >> 32L) & 0xFFFF;
/* 499 */       int y = (int)(target >> 16L) & 0xFFFF;
/* 500 */       boolean onSurface = (Tiles.decodeLayer(target) == 0);
/* 501 */       Wall wall = Wall.getWall(target);
/* 502 */       String loc = "";
/* 503 */       if (creature.getPower() > 0)
/* 504 */         loc = " at " + x + "," + y + ", " + onSurface; 
/* 505 */       if (wall == null)
/*     */       {
/* 507 */         buf.append("Unknown wall" + loc);
/*     */       }
/*     */       else
/*     */       {
/* 511 */         buf.append(wall.getName());
/* 512 */         buf.append(" (level:" + wall.getFloorLevel() + ")");
/* 513 */         buf.append(loc);
/*     */       }
/*     */     
/* 516 */     } else if (WurmId.getType(target) == 2 || 
/* 517 */       WurmId.getType(target) == 6 || 
/* 518 */       WurmId.getType(target) == 19 || 
/* 519 */       WurmId.getType(target) == 20) {
/*     */       
/*     */       try
/*     */       {
/* 523 */         Item targetItem = Items.getItem(target);
/*     */         
/* 525 */         String tgtName = targetItem.getName().replace('"', '\'');
/* 526 */         if (creature.getPower() > 0) {
/* 527 */           buf.append(tgtName + " at " + targetItem.getTileX() + "," + targetItem.getTileY());
/*     */         } else {
/* 529 */           buf.append(tgtName);
/* 530 */         }  if (targetItem.getOwnerId() != -10L)
/*     */         {
/* 532 */           if (creature.getPower() > 0)
/*     */           {
/*     */             
/* 535 */             buf.append(" owned by " + targetItem.getOwnerId());
/*     */           }
/*     */         }
/*     */       }
/* 539 */       catch (NoSuchItemException nsi)
/*     */       {
/* 541 */         buf.append("Unknown item");
/*     */       }
/*     */     
/* 544 */     } else if (WurmId.getType(target) == 7) {
/*     */       
/* 546 */       int x = (int)(target >> 32L) & 0xFFFF;
/* 547 */       int y = (int)(target >> 16L) & 0xFFFF;
/* 548 */       boolean onSurface = (Tiles.decodeLayer(target) == 0);
/* 549 */       Fence fence = Fence.getFence(target);
/* 550 */       String loc = "";
/* 551 */       if (creature.getPower() > 0)
/* 552 */         loc = " at " + x + ", " + y + ", " + onSurface; 
/* 553 */       if (fence == null)
/*     */       {
/* 555 */         buf.append("Unknown fence" + loc);
/*     */       }
/*     */       else
/*     */       {
/* 559 */         buf.append(fence.getName());
/* 560 */         if (fence.getFloorLevel() > 0)
/* 561 */           buf.append(" (level:" + fence.getFloorLevel() + ")"); 
/* 562 */         buf.append(loc);
/*     */       }
/*     */     
/* 565 */     } else if (WurmId.getType(target) == 28) {
/*     */       
/* 567 */       int x = Tiles.decodeTileX(target);
/* 568 */       int y = Tiles.decodeTileY(target);
/* 569 */       int layer = Tiles.decodeLayer(target);
/* 570 */       String loc = "";
/* 571 */       if (creature.getPower() > 0)
/* 572 */         loc = " at " + x + ", " + y + ", " + ((layer == 0) ? 1 : 0); 
/* 573 */       BridgePart[] bridgeParts = Zones.getBridgePartsAtTile(x, y, (layer == 0));
/* 574 */       if (bridgeParts.length == 0)
/*     */       {
/* 576 */         buf.append("Unknown bridge part" + loc);
/*     */       }
/* 578 */       else if (bridgeParts.length > 1)
/*     */       {
/* 580 */         buf.append("Too many bridge parts found" + loc);
/*     */       }
/*     */       else
/*     */       {
/* 584 */         buf.append(bridgeParts[0].getName() + loc);
/*     */       }
/*     */     
/* 587 */     } else if (WurmId.getType(target) == 23) {
/*     */       
/* 589 */       int x = Tiles.decodeTileX(target);
/* 590 */       int y = Tiles.decodeTileY(target);
/* 591 */       int layer = Tiles.decodeLayer(target);
/* 592 */       int htOffset = Floor.getHeightOffsetFromWurmId(target);
/* 593 */       String loc = "";
/* 594 */       if (creature.getPower() > 0)
/* 595 */         loc = " at " + x + ", " + y + ", " + ((layer == 0) ? 1 : 0); 
/* 596 */       Floor[] floors = Zones.getFloorsAtTile(x, y, htOffset, htOffset, layer);
/* 597 */       if (floors == null || floors.length == 0)
/*     */       {
/* 599 */         buf.append("Unknown floor" + loc);
/*     */       }
/*     */       else
/*     */       {
/* 603 */         buf.append(floors[0].getName());
/* 604 */         buf.append(" (level:" + floors[0].getFloorLevel() + ")");
/* 605 */         buf.append(loc);
/*     */       }
/*     */     
/* 608 */     } else if (WurmId.getType(target) == 3) {
/*     */       
/* 610 */       boolean broken = false;
/* 611 */       int x = (int)(target >> 32L) & 0xFFFF;
/* 612 */       int y = (int)(target >> 16L) & 0xFFFF;
/* 613 */       if (x > Zones.worldTileSizeX) {
/*     */         
/* 615 */         x = (int)(target >> 40L) & 0xFFFFFF;
/* 616 */         broken = true;
/*     */       } 
/*     */ 
/*     */       
/* 620 */       int heightOffset = (int)(target >> 48L) & 0xFFFF;
/* 621 */       int tile = Server.surfaceMesh.getTile(x, y);
/* 622 */       byte type = Tiles.decodeType(tile);
/* 623 */       Tiles.Tile t = Tiles.getTile(type);
/* 624 */       buf.append(t.tiledesc);
/* 625 */       if (creature.getPower() > 0)
/*     */       {
/* 627 */         if (broken)
/* 628 */           buf.append(" * "); 
/* 629 */         buf.append(" at ");
/* 630 */         buf.append(x);
/* 631 */         buf.append(", ");
/* 632 */         buf.append(y);
/* 633 */         buf.append(", true");
/*     */       }
/*     */     
/* 636 */     } else if (WurmId.getType(target) == 17) {
/*     */       
/* 638 */       int x = (int)(target >> 32L) & 0xFFFF;
/* 639 */       int y = (int)(target >> 16L) & 0xFFFF;
/* 640 */       int tile = Server.caveMesh.getTile(x, y);
/* 641 */       byte type = Tiles.decodeType(tile);
/* 642 */       Tiles.Tile t = Tiles.getTile(type);
/* 643 */       buf.append(t.tiledesc);
/* 644 */       if (creature.getPower() > 0) {
/*     */         
/* 646 */         buf.append(" at ");
/* 647 */         buf.append(x);
/* 648 */         buf.append(", ");
/* 649 */         buf.append(y);
/* 650 */         buf.append(", false");
/*     */       } 
/*     */     } 
/* 653 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean removeTrigger(int id) {
/* 658 */     MissionTrigger trigger = triggers.get(Integer.valueOf(id));
/* 659 */     boolean existed = (triggers.remove(Integer.valueOf(id)) != null);
/* 660 */     if (trigger != null)
/*     */     {
/* 662 */       MissionTargets.removeMissionTrigger(trigger, true);
/*     */     }
/* 664 */     return existed;
/*     */   }
/*     */ 
/*     */   
/*     */   static void destroyTriggersForTarget(long target) {
/* 669 */     MissionTrigger[] mtarr = getAllTriggers();
/* 670 */     for (int t = 0; t < mtarr.length; t++) {
/*     */       
/* 672 */       if (mtarr[t] != null && mtarr[t].getTarget() == target) {
/*     */         
/* 674 */         TriggerEffects.destroyEffectsForTrigger(mtarr[t].getId());
/* 675 */         removeTrigger(mtarr[t].getId());
/* 676 */         mtarr[t].destroy();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 681 */     MissionTargets.destroyMissionTarget(target, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadAllTriggers() {
/* 686 */     Connection dbcon = null;
/* 687 */     PreparedStatement ps = null;
/* 688 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 691 */       dbcon = DbConnector.getPlayerDbCon();
/* 692 */       ps = dbcon.prepareStatement("SELECT * FROM MISSIONTRIGGERS");
/* 693 */       rs = ps.executeQuery();
/* 694 */       int mid = -10;
/* 695 */       while (rs.next())
/*     */       {
/*     */         
/* 698 */         mid = rs.getInt("ID");
/* 699 */         MissionTrigger m = new MissionTrigger();
/* 700 */         m.setId(mid);
/* 701 */         m.setName(rs.getString("NAME"));
/* 702 */         m.setDescription(rs.getString("DESCRIPTION"));
/* 703 */         m.setOnItemUsedId(rs.getInt("ONITEMCREATED"));
/*     */         
/* 705 */         m.setOnActionPerformed(rs.getInt("ONACTIONPERFORMED"));
/* 706 */         m.setOnTargetId(rs.getLong("ONTARGET"));
/* 707 */         m.setMissionRequirement(rs.getInt("MISSION_REQ"));
/* 708 */         m.setStateRequirement(rs.getFloat("MISSION_STATE_REQ"));
/* 709 */         m.setStateEnd(rs.getFloat("MISSION_STATE_END"));
/* 710 */         m.setInactive(rs.getBoolean("INACTIVE"));
/* 711 */         m.setLastModifierName(rs.getString("LASTMODIFIER"));
/* 712 */         m.setCreatorName(rs.getString("CREATOR"));
/* 713 */         m.setCreatedDate(rs.getString("CREATEDDATE"));
/* 714 */         m.setLastModifierName(rs.getString("LASTMODIFIER"));
/* 715 */         Timestamp st = new Timestamp(System.currentTimeMillis());
/*     */         
/*     */         try {
/* 718 */           String lastModified = rs.getString("LASTMODIFIEDDATE");
/* 719 */           if (lastModified != null) {
/* 720 */             st = new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(lastModified).getTime());
/*     */           }
/*     */         }
/* 723 */         catch (Exception ex) {
/*     */           
/* 725 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */         } 
/* 727 */         m.setLastModifiedDate(st);
/* 728 */         m.setSeconds(rs.getInt("SECONDS"));
/* 729 */         m.setCreatorType(rs.getByte("CREATORTYPE"));
/* 730 */         m.setOwnerId(rs.getLong("CREATORID"));
/* 731 */         m.setIsSpawnpoint(rs.getBoolean("SPAWNPOINT"));
/* 732 */         addMissionTrigger(m);
/*     */       }
/*     */     
/* 735 */     } catch (SQLException sqx) {
/*     */       
/* 737 */       logger.log(Level.WARNING, sqx.getMessage());
/*     */     }
/*     */     finally {
/*     */       
/* 741 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 742 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\MissionTriggers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */