/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.CaveTile;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.bodys.Wounds;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchWallException;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.support.Tickets;
/*      */ import com.wurmonline.server.tutorial.Mission;
/*      */ import com.wurmonline.server.tutorial.MissionPerformed;
/*      */ import com.wurmonline.server.tutorial.Missions;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import java.util.ArrayList;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ 
/*      */ 
/*      */ public final class BehaviourDispatcher
/*      */   implements CounterTypes, ItemTypes, MiscConstants
/*      */ {
/*   72 */   private static final Logger logger = Logger.getLogger(BehaviourDispatcher.class.getName());
/*   73 */   private static List<ActionEntry> availableActions = null;
/*   74 */   private static final List<ActionEntry> emptyActions = new LinkedList<>();
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
/*      */   public static void requestSelectionActions(Creature creature, Communicator comm, byte requestId, long subject, long target) throws NoSuchBehaviourException, NoSuchPlayerException, NoSuchCreatureException, NoSuchItemException, NoSuchWallException {
/*   87 */     if (!creature.isTeleporting()) {
/*      */       
/*   89 */       Item item = null;
/*   90 */       availableActions = null;
/*      */ 
/*      */       
/*   93 */       if (WurmId.getType(subject) == 8 || WurmId.getType(subject) == 18 || WurmId.getType(subject) == 32) {
/*   94 */         subject = -1L;
/*      */       }
/*   96 */       int targetType = WurmId.getType(target);
/*   97 */       Behaviour behaviour = Action.getBehaviour(target, creature.isOnSurface());
/*   98 */       boolean onSurface = Action.getIsOnSurface(target, creature.isOnSurface());
/*   99 */       if (subject != -1L && (
/*  100 */         WurmId.getType(subject) == 2 || WurmId.getType(subject) == 6 || 
/*  101 */         WurmId.getType(subject) == 19 || WurmId.getType(subject) == 20)) {
/*      */         
/*      */         try {
/*      */           
/*  105 */           item = Items.getItem(subject);
/*      */         }
/*  107 */         catch (NoSuchItemException nsi) {
/*      */           
/*  109 */           subject = -10L;
/*  110 */           item = null;
/*      */         } 
/*      */       }
/*  113 */       if (targetType == 3) {
/*      */         
/*  115 */         RequestParam param = requestActionForTiles(creature, target, onSurface, item, behaviour);
/*  116 */         param.filterForSelectBar();
/*  117 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  119 */       else if (targetType == 1 || targetType == 0) {
/*      */         
/*  121 */         RequestParam param = requestActionForCreaturesPlayers(creature, target, item, targetType, behaviour);
/*  122 */         param.filterForSelectBar();
/*  123 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  125 */       else if (targetType == 2 || targetType == 6 || targetType == 19 || targetType == 20) {
/*      */ 
/*      */         
/*  128 */         RequestParam param = requestActionForItemsBodyIdsCoinIds(creature, target, item, behaviour);
/*  129 */         param.filterForSelectBar();
/*  130 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  132 */       else if (targetType == 5) {
/*      */         
/*  134 */         RequestParam param = requestActionForWalls(creature, target, item, behaviour);
/*  135 */         param.filterForSelectBar();
/*  136 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  138 */       else if (targetType == 7) {
/*      */         
/*  140 */         RequestParam param = requestActionForFences(creature, target, item, behaviour);
/*  141 */         param.filterForSelectBar();
/*  142 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  144 */       else if (targetType == 12) {
/*      */         
/*  146 */         RequestParam param = requestActionForTileBorder(creature, target, item, behaviour);
/*  147 */         param.filterForSelectBar();
/*  148 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  150 */       else if (targetType == 17) {
/*      */         
/*  152 */         RequestParam param = requestActionForCaveTiles(creature, target, item, behaviour);
/*  153 */         param.filterForSelectBar();
/*  154 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  156 */       else if (targetType == 23) {
/*      */         
/*  158 */         RequestParam param = requestActionForFloors(creature, target, onSurface, item, behaviour);
/*  159 */         param.filterForSelectBar();
/*  160 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*  162 */       else if (targetType == 24) {
/*      */         
/*  164 */         RequestParam param = requestActionForIllusions(creature, target, item, targetType, behaviour);
/*  165 */         param.filterForSelectBar();
/*  166 */         sendRequestResponse(requestId, comm, param, true);
/*      */       }
/*      */       else {
/*      */         
/*  170 */         RequestParam param = new RequestParam(new LinkedList<>(), "");
/*  171 */         sendRequestResponse(requestId, comm, param, true);
/*      */       } 
/*      */     } else {
/*      */       
/*  175 */       comm.sendAlertServerMessage("You are teleporting and cannot perform actions right now.");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void requestActions(Creature creature, Communicator comm, byte requestId, long subject, long target) throws NoSuchPlayerException, NoSuchCreatureException, NoSuchItemException, NoSuchBehaviourException, NoSuchWallException {
/*  211 */     if (!creature.isTeleporting()) {
/*      */       
/*  213 */       Item item = null;
/*  214 */       availableActions = null;
/*      */ 
/*      */       
/*  217 */       if (WurmId.getType(subject) == 8 || WurmId.getType(subject) == 18 || WurmId.getType(subject) == 32) {
/*  218 */         subject = -1L;
/*      */       }
/*  220 */       int targetType = WurmId.getType(target);
/*  221 */       Behaviour behaviour = Action.getBehaviour(target, creature.isOnSurface());
/*      */       
/*  223 */       boolean onSurface = Action.getIsOnSurface(target, creature.isOnSurface());
/*  224 */       if (subject != -1L && (
/*  225 */         WurmId.getType(subject) == 2 || WurmId.getType(subject) == 6 || 
/*  226 */         WurmId.getType(subject) == 19 || WurmId.getType(subject) == 20)) {
/*      */         
/*      */         try {
/*      */           
/*  230 */           item = Items.getItem(subject);
/*      */         }
/*  232 */         catch (NoSuchItemException nsi) {
/*      */           
/*  234 */           subject = -10L;
/*  235 */           item = null;
/*      */         } 
/*      */       }
/*  238 */       if (targetType == 3)
/*      */       {
/*  240 */         RequestParam param = requestActionForTiles(creature, target, onSurface, item, behaviour);
/*  241 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  243 */       else if (targetType == 1 || targetType == 0)
/*      */       {
/*  245 */         RequestParam param = requestActionForCreaturesPlayers(creature, target, item, targetType, behaviour);
/*  246 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  248 */       else if (targetType == 2 || targetType == 6 || targetType == 19 || targetType == 20)
/*      */       {
/*      */         
/*  251 */         RequestParam param = requestActionForItemsBodyIdsCoinIds(creature, target, item, behaviour);
/*  252 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  254 */       else if (targetType == 5)
/*      */       {
/*  256 */         RequestParam param = requestActionForWalls(creature, target, item, behaviour);
/*  257 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  259 */       else if (targetType == 7)
/*      */       {
/*  261 */         RequestParam param = requestActionForFences(creature, target, item, behaviour);
/*  262 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  264 */       else if (targetType == 8 || targetType == 32)
/*      */       {
/*  266 */         requestActionForWounds(creature, comm, requestId, target, item, behaviour);
/*      */       }
/*  268 */       else if (targetType == 12)
/*      */       {
/*  270 */         RequestParam param = requestActionForTileBorder(creature, target, item, behaviour);
/*  271 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  273 */       else if (targetType == 14)
/*      */       {
/*  275 */         requestActionForPlanets(creature, comm, requestId, target, item, behaviour);
/*      */       }
/*  277 */       else if (targetType == 30)
/*      */       {
/*  279 */         requestActionForMenu(creature, comm, requestId, target, behaviour);
/*      */       }
/*  281 */       else if (targetType == 17)
/*      */       {
/*  283 */         RequestParam param = requestActionForCaveTiles(creature, target, item, behaviour);
/*  284 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  286 */       else if (targetType == 18)
/*      */       {
/*  288 */         requestActionForSkillIds(comm, requestId, target);
/*      */       }
/*  290 */       else if (targetType == 23)
/*      */       {
/*  292 */         RequestParam param = requestActionForFloors(creature, target, onSurface, item, behaviour);
/*  293 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  295 */       else if (targetType == 22)
/*      */       {
/*  297 */         requestActionForMissionPerformed(creature, comm, requestId, target, behaviour);
/*      */       }
/*  299 */       else if (targetType == 24)
/*      */       {
/*  301 */         RequestParam param = requestActionForIllusions(creature, target, item, targetType, behaviour);
/*  302 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  304 */       else if (targetType == 27)
/*      */       {
/*  306 */         RequestParam param = requestActionForTileCorner(creature, target, onSurface, item, behaviour);
/*  307 */         sendRequestResponse(requestId, comm, param, false);
/*      */       }
/*  309 */       else if (targetType == 28)
/*      */       {
/*  311 */         requestActionForBridgeParts(creature, comm, requestId, target, onSurface, item, behaviour);
/*      */       }
/*  313 */       else if (targetType == 25)
/*      */       {
/*  315 */         requestActionForTickets(creature, comm, requestId, target, behaviour);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  320 */       comm.sendAlertServerMessage("You are teleporting and cannot perform actions right now.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void sendRequestResponse(byte requestId, Communicator comm, RequestParam response, boolean sendToSelectBar) {
/*  326 */     if (!sendToSelectBar) {
/*  327 */       comm.sendAvailableActions(requestId, response.getAvailableActions(), response.getHelpString());
/*      */     } else {
/*  329 */       comm.sendAvailableSelectBarActions(requestId, response.getAvailableActions());
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
/*      */   public static final RequestParam requestActionForTiles(Creature creature, long target, boolean onSurface, Item item, Behaviour behaviour) {
/*  342 */     int x = Tiles.decodeTileX(target);
/*  343 */     int y = Tiles.decodeTileY(target);
/*      */ 
/*      */     
/*  346 */     int tile = Server.surfaceMesh.getTile(x, y);
/*      */ 
/*      */ 
/*      */     
/*  350 */     if (item == null) {
/*  351 */       availableActions = behaviour.getBehavioursFor(creature, x, y, onSurface, tile);
/*      */     } else {
/*  353 */       availableActions = behaviour.getBehavioursFor(creature, item, x, y, onSurface, tile);
/*  354 */     }  byte type = Tiles.decodeType(tile);
/*  355 */     Tiles.Tile t = Tiles.getTile(type);
/*      */     
/*  357 */     return new RequestParam(availableActions, t.tiledesc.replaceAll(" ", "_"));
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
/*      */   private static final RequestParam requestActionForTileCorner(Creature creature, long target, boolean onSurface, Item item, Behaviour behaviour) {
/*  372 */     int x = Tiles.decodeTileX(target);
/*  373 */     int y = Tiles.decodeTileY(target);
/*  374 */     int heightOffset = Tiles.decodeHeightOffset(target);
/*  375 */     int tile = Server.surfaceMesh.getTile(x, y);
/*      */     
/*  377 */     if (item == null) {
/*  378 */       availableActions = behaviour.getBehavioursFor(creature, x, y, onSurface, true, tile, heightOffset);
/*      */     } else {
/*  380 */       availableActions = behaviour.getBehavioursFor(creature, item, x, y, onSurface, true, tile, heightOffset);
/*  381 */     }  byte type = Tiles.decodeType(tile);
/*  382 */     Tiles.Tile t = Tiles.getTile(type);
/*      */     
/*  384 */     return new RequestParam(availableActions, t.tiledesc.replaceAll(" ", "_"));
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
/*      */   public static final RequestParam requestActionForCreaturesPlayers(Creature creature, long target, Item item, int targetType, Behaviour behaviour) throws NoSuchPlayerException, NoSuchCreatureException {
/*  401 */     Creature targetc = Server.getInstance().getCreature(target);
/*  402 */     if (targetc.getTemplateId() == 119)
/*      */     {
/*      */ 
/*      */       
/*  406 */       return new RequestParam(new ArrayList<>(), "Fishing");
/*      */     }
/*  408 */     if (item == null) {
/*  409 */       availableActions = behaviour.getBehavioursFor(creature, targetc);
/*      */     } else {
/*  411 */       availableActions = behaviour.getBehavioursFor(creature, item, targetc);
/*  412 */     }  if (targetType == 1) {
/*  413 */       return new RequestParam(availableActions, targetc.getTemplate().getName().replaceAll(" ", "_"));
/*      */     }
/*  415 */     return new RequestParam(availableActions, "Player:" + targetc.getName().replaceAll(" ", "_"));
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
/*      */   public static final RequestParam requestActionForItemsBodyIdsCoinIds(Creature creature, long target, Item item, Behaviour behaviour) throws NoSuchItemException {
/*  428 */     Item targetItem = Items.getItem(target);
/*  429 */     long ownerId = targetItem.getOwnerId();
/*  430 */     if (ownerId == -10L || ownerId == creature.getWurmId() || targetItem.isTraded()) {
/*      */       
/*  432 */       if (item == null) {
/*  433 */         availableActions = behaviour.getBehavioursFor(creature, targetItem);
/*      */       } else {
/*  435 */         availableActions = behaviour.getBehavioursFor(creature, item, targetItem);
/*  436 */       }  if (targetItem.isKingdomMarker() && targetItem.isNoTake()) {
/*  437 */         return new RequestParam(availableActions, targetItem.getTemplate().getName().replaceAll(" ", "_"));
/*      */       }
/*      */       
/*  440 */       String name = "";
/*  441 */       if ((targetItem.getTemplate()).sizeString != null && !(targetItem.getTemplate()).sizeString.isEmpty()) {
/*      */ 
/*      */         
/*  444 */         name = StringUtil.format("%s%s", new Object[] { (targetItem.getTemplate()).sizeString, targetItem.getTemplate().getName() }).replaceAll(" ", "_");
/*      */       } else {
/*  446 */         name = targetItem.getTemplate().getName().replaceAll(" ", "_");
/*  447 */       }  return new RequestParam(availableActions, name);
/*      */     } 
/*      */     
/*  450 */     if (ownerId != -10L) {
/*      */       
/*  452 */       availableActions = new LinkedList<>();
/*  453 */       availableActions.addAll(Actions.getDefaultItemActions());
/*  454 */       if (targetItem.isKingdomMarker() && targetItem.isNoTake()) {
/*  455 */         return new RequestParam(availableActions, targetItem.getTemplate().getName().replaceAll(" ", "_"));
/*      */       }
/*      */       
/*  458 */       String name = "";
/*  459 */       if ((targetItem.getTemplate()).sizeString.length() > 0) {
/*      */ 
/*      */         
/*  462 */         name = StringUtil.format("%s%s", new Object[] { (targetItem.getTemplate()).sizeString, targetItem.getTemplate().getName() }).replaceAll(" ", "_");
/*      */       } else {
/*  464 */         name = targetItem.getTemplate().getName().replaceAll(" ", "_");
/*  465 */       }  return new RequestParam(availableActions, name);
/*      */     } 
/*      */ 
/*      */     
/*  469 */     return new RequestParam(new LinkedList<>(), "");
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
/*      */   private static final RequestParam requestActionForWalls(Creature creature, long target, Item item, Behaviour behaviour) throws NoSuchWallException {
/*  482 */     int x = Tiles.decodeTileX(target);
/*  483 */     int y = Tiles.decodeTileY(target);
/*      */ 
/*      */ 
/*      */     
/*  487 */     boolean onSurface = (Tiles.decodeLayer(target) == 0);
/*  488 */     Wall wall = null;
/*  489 */     for (int xx = 1; xx >= -1; xx--) {
/*      */       
/*  491 */       for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/*  496 */           Zone zone = Zones.getZone(x + xx, y + yy, onSurface);
/*  497 */           VolaTile tile = zone.getTileOrNull(x + xx, y + yy);
/*  498 */           if (tile != null) {
/*      */             
/*  500 */             Wall[] walls = tile.getWalls();
/*  501 */             for (int s = 0; s < walls.length; s++) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  506 */               if (walls[s].getId() == target) {
/*      */                 
/*  508 */                 wall = walls[s];
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*  514 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  524 */     if (wall == null)
/*  525 */       throw new NoSuchWallException("No wall with id " + target); 
/*  526 */     if (item == null) {
/*  527 */       availableActions = behaviour.getBehavioursFor(creature, wall);
/*      */     } else {
/*  529 */       availableActions = behaviour.getBehavioursFor(creature, item, wall);
/*      */     } 
/*  531 */     return new RequestParam(availableActions, wall.getIdName());
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
/*      */   private static final RequestParam requestActionForFences(Creature creature, long target, Item item, Behaviour behaviour) {
/*  543 */     int x = Tiles.decodeTileX(target);
/*  544 */     int y = Tiles.decodeTileY(target);
/*  545 */     boolean onSurface = (Tiles.decodeLayer(target) == 0);
/*  546 */     Fence fence = null;
/*  547 */     VolaTile tile = Zones.getTileOrNull(x, y, onSurface);
/*  548 */     if (tile != null)
/*      */     {
/*  550 */       fence = tile.getFence(target);
/*      */     }
/*  552 */     if (fence == null) {
/*      */       
/*  554 */       logger.log(Level.WARNING, "Checking for fence with id " + target + " in other tiles. ");
/*  555 */       for (int tx = x - 1; tx <= x + 1; tx++) {
/*      */         
/*  557 */         for (int ty = y - 1; ty <= y + 1; ty++) {
/*      */           
/*  559 */           tile = Zones.getTileOrNull(tx, ty, onSurface);
/*  560 */           if (tile != null) {
/*      */             
/*  562 */             fence = tile.getFence(target);
/*      */             
/*  564 */             if (fence != null) {
/*      */ 
/*      */               
/*      */               try {
/*  568 */                 Zone zone = Zones.getZone(tx, ty, true);
/*  569 */                 logger.log(Level.INFO, "Found fence in zone " + zone.getId() + " fence has id " + fence
/*  570 */                     .getId() + " and tilex=" + fence.getTileX() + ", tiley=" + fence
/*  571 */                     .getTileY() + " dir=" + fence.getDir());
/*  572 */                 Zone correctZone = Zones.getZone(x, y, true);
/*  573 */                 logger.log(Level.INFO, "We looked for it in zone " + correctZone.getId());
/*  574 */                 if (!zone.equals(correctZone))
/*      */                 {
/*  576 */                   logger.log(Level.INFO, "Correcting the mistake.");
/*  577 */                   zone.removeFence(fence);
/*  578 */                   fence.setZoneId(correctZone.getId());
/*  579 */                   correctZone.addFence(fence);
/*  580 */                   tile.broadCast("The server tried to remedy a fence problem here. Please report if anything happened.");
/*      */                 }
/*      */               
/*      */               }
/*  584 */               catch (NoSuchZoneException nsz) {
/*      */                 
/*  586 */                 logger.log(Level.WARNING, "Weird: " + nsz.getMessage(), (Throwable)nsz);
/*      */               } 
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  594 */     if (fence != null) {
/*      */       
/*  596 */       if (item == null) {
/*  597 */         availableActions = behaviour.getBehavioursFor(creature, fence);
/*      */       } else {
/*  599 */         availableActions = behaviour.getBehavioursFor(creature, item, fence);
/*  600 */       }  return new RequestParam(availableActions, fence.getName().replaceAll(" ", "_"));
/*      */     } 
/*      */ 
/*      */     
/*  604 */     logger.log(Level.WARNING, "Failed to locate fence with id " + target + ".");
/*  605 */     return new RequestParam(new LinkedList<>(), "");
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
/*      */   private static void requestActionForWounds(Creature creature, Communicator comm, byte requestId, long target, Item item, Behaviour behaviour) {
/*      */     try {
/*  622 */       boolean found = false;
/*  623 */       Wounds wounds = creature.getBody().getWounds();
/*  624 */       if (wounds != null) {
/*      */         
/*  626 */         Wound wound = wounds.getWound(target);
/*  627 */         if (wound != null) {
/*      */           
/*  629 */           found = true;
/*  630 */           if (item == null) {
/*  631 */             availableActions = behaviour.getBehavioursFor(creature, wound);
/*      */           } else {
/*  633 */             availableActions = behaviour.getBehavioursFor(creature, item, wound);
/*  634 */           }  comm.sendAvailableActions(requestId, availableActions, wound.getDescription().replaceAll(", bandaged", "").replaceAll(" ", "_"));
/*      */         } 
/*      */       } 
/*      */       
/*  638 */       if (!found)
/*      */       {
/*  640 */         Wound wound = Wounds.getAnyWound(target);
/*  641 */         if (wound != null)
/*      */         {
/*  643 */           if (item == null) {
/*  644 */             availableActions = behaviour.getBehavioursFor(creature, wound);
/*      */           } else {
/*  646 */             availableActions = behaviour.getBehavioursFor(creature, item, wound);
/*  647 */           }  comm.sendAvailableActions(requestId, availableActions, wound.getDescription().replaceAll(", bandaged", "").replaceAll(" ", "_"));
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  652 */     } catch (Exception ex) {
/*      */ 
/*      */       
/*  655 */       if (logger.isLoggable(Level.FINE))
/*      */       {
/*  657 */         logger.log(Level.FINE, ex.getMessage(), ex);
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
/*      */   private static final RequestParam requestActionForTileBorder(Creature creature, long target, Item item, Behaviour behaviour) {
/*  671 */     int x = Tiles.decodeTileX(target);
/*  672 */     int y = Tiles.decodeTileY(target);
/*  673 */     int heightOffset = Tiles.decodeHeightOffset(target);
/*  674 */     Tiles.TileBorderDirection dir = Tiles.decodeDirection(target);
/*  675 */     boolean onSurface = (Tiles.decodeLayer(target) == 0);
/*  676 */     if (MethodsStructure.doesTileBorderContainWallOrFence(x, y, heightOffset, dir, onSurface, true)) {
/*      */       
/*  678 */       availableActions = behaviour.getBehavioursFor(creature, x, y, onSurface, dir, true, heightOffset);
/*      */ 
/*      */     
/*      */     }
/*  682 */     else if (item != null) {
/*  683 */       availableActions = behaviour.getBehavioursFor(creature, item, x, y, onSurface, dir, true, heightOffset);
/*      */     } else {
/*  685 */       availableActions = behaviour.getBehavioursFor(creature, x, y, onSurface, dir, true, heightOffset);
/*      */     } 
/*  687 */     return new RequestParam(availableActions, "Tile_Border");
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
/*      */   private static void requestActionForPlanets(Creature creature, Communicator comm, byte requestId, long target, Item item, Behaviour behaviour) {
/*  702 */     int planetId = (int)(target >> 16L) & 0xFFFF;
/*  703 */     if (item == null) {
/*  704 */       availableActions = behaviour.getBehavioursFor(creature, planetId);
/*      */     } else {
/*  706 */       availableActions = behaviour.getBehavioursFor(creature, item, planetId);
/*  707 */     }  comm.sendAvailableActions(requestId, availableActions, PlanetBehaviour.getName(planetId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void requestActionForMenu(Creature creature, Communicator comm, byte requestId, long target, Behaviour behaviour) {
/*  714 */     int planetId = (int)(target >> 16L) & 0xFFFF;
/*  715 */     availableActions = behaviour.getBehavioursFor(creature, planetId);
/*  716 */     comm.sendAvailableActions(requestId, availableActions, "");
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
/*      */   private static final RequestParam requestActionForCaveTiles(Creature creature, long target, Item item, Behaviour behaviour) {
/*  728 */     int x = Tiles.decodeTileX(target);
/*  729 */     int y = Tiles.decodeTileY(target);
/*  730 */     int dir = CaveTile.decodeCaveTileDir(target);
/*      */     
/*  732 */     int tile = Server.caveMesh.getTile(x, y);
/*  733 */     if (item == null) {
/*  734 */       availableActions = behaviour.getBehavioursFor(creature, x, y, false, tile, dir);
/*      */     } else {
/*  736 */       availableActions = behaviour.getBehavioursFor(creature, item, x, y, false, tile, dir);
/*      */     } 
/*  738 */     return new RequestParam(availableActions, (Tiles.getTile(Tiles.decodeType(tile))).tiledesc.replaceAll(" ", "_"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void requestActionForSkillIds(Communicator comm, byte requestId, long target) {
/*  748 */     int skillid = (int)(target >> 32L) & 0xFFFFFFFF;
/*      */     
/*  750 */     String name = "unknown";
/*  751 */     if (skillid == 2147483644) {
/*  752 */       comm.sendAvailableActions(requestId, emptyActions, "Favor");
/*  753 */     } else if (skillid == 2147483645) {
/*  754 */       comm.sendAvailableActions(requestId, emptyActions, "Faith");
/*  755 */     } else if (skillid == 2147483642) {
/*  756 */       comm.sendAvailableActions(requestId, emptyActions, "Alignment");
/*  757 */     } else if (skillid == 2147483643) {
/*  758 */       comm.sendAvailableActions(requestId, emptyActions, "Religion");
/*  759 */     } else if (skillid == Integer.MAX_VALUE) {
/*  760 */       comm.sendAvailableActions(requestId, emptyActions, "Skills");
/*  761 */     } else if (skillid == 2147483646) {
/*  762 */       comm.sendAvailableActions(requestId, emptyActions, "Characteristics");
/*      */     } else {
/*      */       
/*  765 */       name = SkillSystem.getNameFor(skillid);
/*  766 */       comm.sendAvailableActions(requestId, emptyActions, name.replaceAll(" ", "_"));
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
/*      */   private static final RequestParam requestActionForFloors(Creature creature, long target, boolean onSurface, Item item, Behaviour behaviour) {
/*  790 */     int x = Tiles.decodeTileX(target);
/*  791 */     int y = Tiles.decodeTileY(target);
/*  792 */     int heightOffset = Tiles.decodeHeightOffset(target);
/*      */     
/*  794 */     String fString = "unknown";
/*      */     
/*  796 */     Floor[] floors = Zones.getFloorsAtTile(x, y, heightOffset, heightOffset, onSurface ? 0 : -1);
/*      */     
/*  798 */     if (floors == null) {
/*      */       
/*  800 */       logger.log(Level.WARNING, "No such floor " + target + " (" + x + "," + y + " heightOffset=" + heightOffset + ")");
/*      */       
/*  802 */       return new RequestParam(new LinkedList<>(), "");
/*      */     } 
/*      */ 
/*      */     
/*  806 */     if (floors.length > 1)
/*      */     {
/*  808 */       logger.log(Level.WARNING, "Found more than 1 floor at " + x + "," + y + " heightOffset" + heightOffset);
/*      */     }
/*  810 */     Floor floor = floors[0];
/*  811 */     fString = floor.getName();
/*  812 */     if (item == null) {
/*  813 */       availableActions = behaviour.getBehavioursFor(creature, onSurface, floor);
/*      */     } else {
/*  815 */       availableActions = behaviour.getBehavioursFor(creature, item, onSurface, floor);
/*  816 */     }  creature.sendToLoggers("Requesting floor " + floor.getId() + " target requested=" + target + " " + floor
/*  817 */         .getHeightOffset());
/*      */     
/*  819 */     return new RequestParam(availableActions, fString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void requestActionForBridgeParts(Creature creature, Communicator comm, byte requestId, long target, boolean onSurface, Item item, Behaviour behaviour) {
/*  826 */     int x = Tiles.decodeTileX(target);
/*  827 */     int y = Tiles.decodeTileY(target);
/*  828 */     int ht = Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y));
/*  829 */     int heightOffset = Tiles.decodeHeightOffset(target) - ht;
/*  830 */     String fString = "unknown";
/*      */     
/*  832 */     BridgePart[] bridgeParts = Zones.getBridgePartsAtTile(x, y, onSurface);
/*      */     
/*  834 */     if (bridgeParts == null) {
/*      */       
/*  836 */       logger.log(Level.WARNING, "No such Bridge Part " + target + " (" + x + "," + y + " heightOffset=" + heightOffset + ")");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  841 */       if (bridgeParts.length > 1)
/*      */       {
/*  843 */         logger.log(Level.WARNING, "Found more than 1 bridge part at " + x + "," + y + " heightOffset" + heightOffset);
/*      */       }
/*  845 */       BridgePart bridgePart = bridgeParts[0];
/*  846 */       fString = bridgePart.getName();
/*  847 */       if (item == null) {
/*  848 */         availableActions = behaviour.getBehavioursFor(creature, onSurface, bridgePart);
/*      */       } else {
/*  850 */         availableActions = behaviour.getBehavioursFor(creature, item, onSurface, bridgePart);
/*  851 */       }  creature.sendToLoggers("Requesting bridge part " + bridgePart.getId() + " target requested=" + target + " " + bridgePart
/*  852 */           .getHeightOffset());
/*  853 */       comm.sendAvailableActions(requestId, availableActions, fString);
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
/*      */   private static void requestActionForMissionPerformed(Creature creature, Communicator comm, byte requestId, long target, Behaviour behaviour) {
/*  867 */     int missionId = MissionPerformed.decodeMissionId(target);
/*  868 */     Mission m = Missions.getMissionWithId(missionId);
/*  869 */     String mString = "unknown";
/*  870 */     if (m != null)
/*  871 */       mString = m.getName(); 
/*  872 */     comm.sendAvailableActions(requestId, behaviour.getBehavioursFor(creature, missionId), "Mission:" + mString);
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
/*      */   private static final RequestParam requestActionForIllusions(Creature creature, long target, Item item, int targetType, Behaviour behaviour) throws NoSuchPlayerException, NoSuchCreatureException {
/*  888 */     long wid = Creature.getWurmIdForIllusion(target);
/*  889 */     return requestActionForCreaturesPlayers(creature, wid, item, targetType, behaviour);
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
/*      */   private static void requestActionForTickets(Creature creature, Communicator comm, byte requestId, long target, Behaviour behaviour) {
/*  902 */     int ticketId = Tickets.decodeTicketId(target);
/*  903 */     comm.sendAvailableActions(requestId, behaviour.getBehavioursFor(creature, ticketId), "Ticket:" + ticketId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void action(Creature creature, Communicator comm, long subject, long target, short action) throws NoSuchPlayerException, NoSuchCreatureException, NoSuchItemException, NoSuchBehaviourException, NoSuchWallException, FailedException {
/*  910 */     String s = "unknown";
/*      */     
/*      */     try {
/*  913 */       s = Actions.getVerbForAction(action);
/*      */     }
/*  915 */     catch (Exception e) {
/*      */       
/*  917 */       s = "" + action;
/*      */     } 
/*  919 */     if (creature.isUndead())
/*      */     {
/*  921 */       if (action != 326 && action != 1 && !Action.isActionAttack(action) && 
/*  922 */         !Action.isStanceChange(action) && action != 523 && action != 522) {
/*      */         
/*  924 */         creature.getCommunicator().sendNormalServerMessage("Unnn..");
/*      */         return;
/*      */       } 
/*      */     }
/*  928 */     creature.sendToLoggers("Received action number " + s + ", target " + target + ", source " + subject + ", action " + action, (byte)2);
/*  929 */     if (creature.isFrozen()) {
/*      */       
/*  931 */       creature.sendToLoggers("Frozen. Ignoring.", (byte)2);
/*  932 */       throw new FailedException("Frozen");
/*      */     } 
/*  934 */     if (creature.isTeleporting()) {
/*      */       
/*  936 */       comm.sendAlertServerMessage("You are teleporting and cannot perform actions right now.");
/*  937 */       throw new FailedException("Teleporting");
/*      */     } 
/*  939 */     if (action == 149) {
/*      */ 
/*      */       
/*      */       try {
/*  943 */         if (creature.getCurrentAction().isSpell() || !creature.getCurrentAction().isOffensive() || 
/*  944 */           !creature.isFighting()) {
/*  945 */           creature.stopCurrentAction();
/*      */         }
/*  947 */       } catch (NoSuchActionException noSuchActionException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  958 */       float x = creature.getStatus().getPositionX();
/*  959 */       float y = creature.getStatus().getPositionY();
/*  960 */       float z = creature.getStatus().getPositionZ() + creature.getAltOffZ();
/*      */       
/*  962 */       Action toSet = new Action(creature, subject, target, action, x, y, z, creature.getStatus().getRotation());
/*      */       
/*  964 */       if (toSet.isQuick()) {
/*      */         
/*  966 */         toSet.poll();
/*      */ 
/*      */       
/*      */       }
/*  970 */       else if (toSet.isStanceChange() && toSet.getNumber() != 340) {
/*      */         
/*  972 */         if (!toSet.poll()) {
/*  973 */           creature.setAction(toSet);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  978 */         toSet.setRarity(creature.getRarity());
/*  979 */         creature.setAction(toSet);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void action(Creature creature, Communicator comm, long subject, long[] targets, short action) throws FailedException, NoSuchPlayerException, NoSuchCreatureException, NoSuchItemException, NoSuchBehaviourException {
/* 1005 */     String s = "unknown";
/*      */     
/*      */     try {
/* 1008 */       s = Actions.getVerbForAction(action);
/*      */     }
/* 1010 */     catch (Exception e) {
/*      */       
/* 1012 */       s = "" + action;
/*      */     } 
/* 1014 */     if (creature.isUndead()) {
/*      */       
/* 1016 */       creature.getCommunicator().sendNormalServerMessage("Unnn..");
/*      */       
/*      */       return;
/*      */     } 
/* 1020 */     String tgts = "";
/* 1021 */     for (int i = 0; i < targets.length; i++) {
/*      */       
/* 1023 */       if (tgts.length() > 0)
/* 1024 */         tgts = tgts + ", "; 
/* 1025 */       tgts = tgts + targets[i];
/*      */     } 
/* 1027 */     creature.sendToLoggers("Received action number " + s + ", target " + tgts, (byte)2);
/* 1028 */     if (creature.isFrozen()) {
/*      */       
/* 1030 */       creature.sendToLoggers("Frozen. Ignoring.", (byte)2);
/* 1031 */       throw new FailedException("Frozen");
/*      */     } 
/* 1033 */     if (creature.isTeleporting()) {
/*      */       
/* 1035 */       comm.sendAlertServerMessage("You are teleporting and cannot perform actions right now.");
/* 1036 */       throw new FailedException("Teleporting");
/*      */     } 
/*      */     
/* 1039 */     float x = creature.getStatus().getPositionX();
/* 1040 */     float y = creature.getStatus().getPositionY();
/* 1041 */     float z = creature.getStatus().getPositionZ() + creature.getAltOffZ();
/*      */     
/* 1043 */     Action toSet = new Action(creature, subject, targets, action, x, y, z, creature.getStatus().getRotation());
/*      */     
/* 1045 */     if (toSet.isQuick()) {
/*      */       
/* 1047 */       toSet.poll();
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1052 */       toSet.setRarity(creature.getRarity());
/* 1053 */       creature.setAction(toSet);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static class RequestParam
/*      */   {
/*      */     private final String helpString;
/*      */     private List<ActionEntry> availableActions;
/*      */     
/*      */     public RequestParam(List<ActionEntry> actions, String help) {
/* 1064 */       this.availableActions = actions;
/* 1065 */       this.helpString = help;
/*      */     }
/*      */ 
/*      */     
/*      */     public final List<ActionEntry> getAvailableActions() {
/* 1070 */       return this.availableActions;
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getHelpString() {
/* 1075 */       return this.helpString;
/*      */     }
/*      */ 
/*      */     
/*      */     public void filterForSelectBar() {
/* 1080 */       for (int i = this.availableActions.size() - 1; i >= 0; i--) {
/*      */         
/* 1082 */         ActionEntry entry = this.availableActions.get(i);
/*      */         
/* 1084 */         if (!entry.isShowOnSelectBar())
/*      */         {
/* 1086 */           this.availableActions.remove(i);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\BehaviourDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */