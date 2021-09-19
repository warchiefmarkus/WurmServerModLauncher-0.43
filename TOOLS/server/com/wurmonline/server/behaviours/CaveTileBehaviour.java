/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.math.Vector2f;
/*      */ import com.wurmonline.mesh.CaveTile;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.highways.HighwayPos;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.utils.CoordUtils;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
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
/*      */ 
/*      */ 
/*      */ public final class CaveTileBehaviour
/*      */   extends TileBehaviour
/*      */ {
/*   80 */   private static final Logger logger = Logger.getLogger(CaveTileBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private static final float FLATTENING_MAX_DEPTH = -7.0F;
/*      */ 
/*      */ 
/*      */   
/*      */   CaveTileBehaviour() {
/*   89 */     super((short)39);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isCave() {
/*   95 */     return true;
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
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile, int dir) {
/*  107 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, tilex, tiley, onSurface, tile);
/*      */     
/*  109 */     if (performer.getDeity() != null && performer.getDeity().isMountainGod())
/*      */     {
/*  111 */       Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]);
/*      */     }
/*      */     
/*  114 */     return toReturn;
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
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, int tilex, int tiley, boolean onSurface, int tile, int dir) {
/*  126 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, tilex, tiley, onSurface, tile);
/*  127 */     byte type = Tiles.decodeType(tile);
/*  128 */     int templateId = source.getTemplateId();
/*  129 */     if (Features.Feature.CAVE_DWELLINGS.isEnabled() && (Tiles.isReinforcedFloor(type) || Tiles.isRoadType(type)))
/*      */     {
/*      */       
/*  132 */       if (dir == 0)
/*      */       {
/*  134 */         toReturn.addAll(getBuildableTileBehaviours(tilex, tiley, performer, templateId));
/*      */       }
/*      */     }
/*  137 */     if (templateId == 492 && type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && dir == 0) {
/*      */       
/*  139 */       toReturn.add(new ActionEntry((short)155, "Prepare", "preparing the floor"));
/*      */     }
/*  141 */     else if (templateId == 97 && type == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id && dir == 0) {
/*      */       
/*  143 */       toReturn.add(new ActionEntry((short)191, "Remove mortar", "removing mortar"));
/*      */     }
/*  145 */     else if (source.isCavePaveable() && type == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id && dir == 0) {
/*      */       
/*  147 */       toReturn.add(Actions.actionEntrys[155]);
/*      */     }
/*  149 */     else if (Tiles.isRoadType(type) && dir == 0) {
/*      */       
/*  151 */       if (source.isPaveable() && source.getTemplateId() != 495) {
/*      */         
/*  153 */         HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  154 */         if (MethodsHighways.onHighway(highwaypos))
/*      */         {
/*  156 */           toReturn.add(new ActionEntry((short)155, "Replace paving", "re-paving"));
/*      */         }
/*      */       }
/*  159 */       else if (templateId == 1115) {
/*      */ 
/*      */         
/*  162 */         HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  163 */         if (!MethodsHighways.onHighway(highwaypos)) {
/*  164 */           toReturn.add(Actions.actionEntrys[191]);
/*      */         }
/*      */       } 
/*  167 */     } else if (templateId == 153 && type == Tiles.Tile.TILE_PLANKS.id && dir == 0) {
/*      */       
/*  169 */       toReturn.add(new ActionEntry((short)231, "Tar", "tarring"));
/*      */     } 
/*  171 */     if (source.isMiningtool() && (dir == 1 || type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id)) {
/*      */       
/*  173 */       int cnt = -2;
/*  174 */       boolean justrock = false;
/*  175 */       if ((dir == 0 && type == Tiles.Tile.TILE_CAVE.id) || (dir == 1 && (type == Tiles.Tile.TILE_CAVE.id || 
/*      */         
/*  177 */         Tiles.isReinforcedFloor(type)))) {
/*      */         
/*  179 */         justrock = true;
/*  180 */         cnt--;
/*      */       } 
/*  182 */       toReturn.add(new ActionEntry((short)cnt, "Mining", "Mining options"));
/*  183 */       if (type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && dir == 0) {
/*  184 */         toReturn.add(new ActionEntry((short)145, "Remove reinforcement", "removing reinforcement"));
/*      */       } else {
/*  186 */         toReturn.add(Actions.actionEntrys[145]);
/*  187 */       }  toReturn.add(Actions.actionEntrys[156]);
/*  188 */       if (justrock)
/*  189 */         if (tilex == performer.getTileX() && tiley == performer.getTileY()) {
/*  190 */           toReturn.add(Actions.actionEntrys[150]);
/*      */         } else {
/*  192 */           toReturn.add(Actions.actionEntrys[532]);
/*      */         }  
/*  194 */     } else if (source.getTemplateId() == 429 && dir == 0 && Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE.id) {
/*      */       
/*  196 */       toReturn.add(Actions.actionEntrys[229]);
/*      */     }
/*  198 */     else if (source.getTemplateId() == 782) {
/*      */       
/*  200 */       toReturn.add(Actions.actionEntrys[518]);
/*      */     } 
/*  202 */     if (performer.getDeity() != null && performer.getDeity().isMountainGod())
/*      */     {
/*  204 */       Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]);
/*      */     }
/*  206 */     if (performer.getPower() > 1) {
/*      */       
/*  208 */       if (source.getTemplateId() == 176 || source.getTemplateId() == 315)
/*      */       {
/*  210 */         if (!Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  215 */           HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, false);
/*  216 */           if (highwayPos == null || !MethodsHighways.onHighway(highwayPos))
/*  217 */             toReturn.add(Actions.actionEntrys[193]); 
/*      */         } 
/*      */       }
/*  220 */       toReturn.add(Actions.actionEntrys[476]);
/*  221 */       if (performer.getPower() >= 4 && source.getTemplateId() == 176)
/*  222 */         toReturn.add(Actions.actionEntrys[518]); 
/*      */     } 
/*  224 */     if (source.getTemplateId() == 601 || (
/*  225 */       WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/*  226 */       WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315))
/*      */     {
/*  228 */       if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE.id || 
/*  229 */         Tiles.isReinforcedFloor(Tiles.decodeType(tile))) {
/*      */ 
/*      */         
/*  232 */         HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, false);
/*  233 */         if (highwayPos == null || !MethodsHighways.onHighway(highwayPos)) {
/*  234 */           toReturn.add(new ActionEntry((short)193, "Collapse", "collapsing"));
/*      */         }
/*      */       } 
/*      */     }
/*  238 */     if (((Player)performer).isSendExtraBytes() && type == Tiles.Tile.TILE_CAVE_EXIT.id && dir == 0) {
/*      */       
/*  240 */       byte fType = Server.getClientCaveFlags(tilex, tiley);
/*  241 */       if (source.isMiningtool() && type == Tiles.Tile.TILE_CAVE_EXIT.id && fType == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id)
/*      */       {
/*  243 */         toReturn.add(new ActionEntry((short)145, "Remove reinforcement", "removing reinforcement"));
/*      */       }
/*  245 */       if (templateId == 492 && fType == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id) {
/*      */         
/*  247 */         toReturn.add(new ActionEntry((short)155, "Prepare", "preparing the floor"));
/*      */       }
/*  249 */       else if (templateId == 97 && fType == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id) {
/*      */         
/*  251 */         toReturn.add(new ActionEntry((short)191, "Remove mortar", "removing mortar"));
/*      */       }
/*  253 */       else if (source.isCavePaveable() && fType == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id) {
/*      */         
/*  255 */         toReturn.add(Actions.actionEntrys[155]);
/*      */       }
/*  257 */       else if (templateId == 1115 && Tiles.isRoadType(fType)) {
/*      */ 
/*      */         
/*  260 */         HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  261 */         if (!MethodsHighways.onHighway(highwaypos)) {
/*  262 */           toReturn.add(Actions.actionEntrys[191]);
/*      */         }
/*  264 */       } else if (Tiles.isRoadType(fType)) {
/*      */         
/*  266 */         HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  267 */         if (MethodsHighways.onHighway(highwaypos))
/*      */         {
/*  269 */           toReturn.add(Actions.actionEntrys[155]);
/*      */         }
/*      */       }
/*  272 */       else if (templateId == 153 && fType == Tiles.Tile.TILE_PLANKS.id) {
/*      */         
/*  274 */         toReturn.add(new ActionEntry((short)231, "Tar", "tarring"));
/*      */       }
/*  276 */       else if (source.getTemplateId() == 429 && fType == 0) {
/*      */         
/*  278 */         toReturn.add(Actions.actionEntrys[229]);
/*      */       } 
/*      */     } 
/*  281 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, int dir, short action, float counter) {
/*      */     Deity deity;
/*  293 */     boolean handled = false;
/*  294 */     boolean done = true;
/*  295 */     switch (action) {
/*      */ 
/*      */       
/*      */       case 1:
/*  299 */         if (dir == 0) {
/*      */           
/*  301 */           byte actualType = Tiles.decodeType(tile);
/*  302 */           byte type = (actualType == Tiles.Tile.TILE_CAVE_EXIT.id) ? Server.getClientCaveFlags(tilex, tiley) : actualType;
/*  303 */           String floorexit = (actualType == Tiles.Tile.TILE_CAVE_EXIT.id) ? "exit" : "floor";
/*  304 */           if (type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id) {
/*  305 */             performer.getCommunicator().sendNormalServerMessage("You see a cave " + floorexit + " has been reinforced with thick wooden beams and metal bands.");
/*  306 */           } else if (type == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id) {
/*  307 */             performer.getCommunicator().sendNormalServerMessage("You see a reinforced cave " + floorexit + " which has been prepared ready for paving.");
/*  308 */           } else if (Tiles.isRoadType(type)) {
/*  309 */             performer.getCommunicator().sendNormalServerMessage("You see a paved cave " + floorexit + ".");
/*  310 */           } else if (actualType == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*  311 */             performer.getCommunicator().sendNormalServerMessage("You see cave exit.");
/*      */           } else {
/*  313 */             performer.getCommunicator().sendNormalServerMessage("You see dark dungeons.");
/*      */           } 
/*      */         } else {
/*  316 */           performer.getCommunicator().sendNormalServerMessage("You see a ceiling.");
/*  317 */         }  sendVillageString(performer, tilex, tiley, false);
/*  318 */         handled = true;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 141:
/*  323 */         deity = performer.getDeity();
/*  324 */         if (deity != null && deity.isMountainGod()) {
/*      */           
/*  326 */           done = MethodsReligion.pray(act, performer, counter);
/*  327 */           handled = true;
/*      */         } 
/*      */         break;
/*      */       
/*      */       default:
/*  332 */         handled = false;
/*      */         break;
/*      */     } 
/*  335 */     if (!handled)
/*      */     {
/*  337 */       done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*      */     }
/*      */     
/*  340 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean isBlocked(int tilex, int tiley, int digTilex, int digTiley, Creature performer, short action, float counter, int dir, int h, int cceil, int encodedTile) {
/*  347 */     if (!Methods.isActionAllowed(performer, action, false, tilex, tiley, encodedTile, dir)) {
/*  348 */       return true;
/*      */     }
/*  350 */     VolaTile dropTile = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/*  351 */     if (dropTile != null)
/*      */     {
/*  353 */       if (dropTile.getNumberOfItems(performer.getFloorLevel()) > 99) {
/*      */         
/*  355 */         performer.getCommunicator().sendNormalServerMessage("There is no space to mine here. Clear the area first.");
/*  356 */         return true;
/*      */       } 
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
/*  389 */     boolean out = false;
/*  390 */     if (dir == 1) {
/*      */       
/*  392 */       if (TileRockBehaviour.allCornersAtRockHeight(digTilex, digTiley)) {
/*  393 */         out = true;
/*      */       }
/*  395 */     } else if (dir != 0) {
/*      */       
/*  397 */       short maxHeight = -25;
/*  398 */       short currHeight = Tiles.decodeHeight(Server.caveMesh.getTile(digTilex, digTiley));
/*  399 */       for (int x = -1; x <= 1; x++) {
/*  400 */         for (int y = -1; y <= 1; y++) {
/*      */ 
/*      */           
/*  403 */           if (y == 0 || x == 0)
/*      */           {
/*      */             
/*  406 */             if (!((x == 0 && y == 0) ? 1 : 0)) {
/*      */               
/*  408 */               boolean check = true;
/*  409 */               int tt = Server.caveMesh.getTile(digTilex + x, digTiley + y);
/*  410 */               if (y == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  415 */                 int tt2 = Server.caveMesh.getTile(digTilex + x, digTiley - 1);
/*  416 */                 if (Tiles.isSolidCave(Tiles.decodeType(tt2)) && Tiles.isSolidCave(Tiles.decodeType(tt)))
/*  417 */                   check = false; 
/*      */               } 
/*  419 */               if (x == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  424 */                 int tt2 = Server.caveMesh.getTile(digTilex - 1, digTiley + y);
/*  425 */                 if (Tiles.isSolidCave(Tiles.decodeType(tt2)) && Tiles.isSolidCave(Tiles.decodeType(tt)))
/*  426 */                   check = false; 
/*      */               } 
/*  428 */               if (check) {
/*      */                 
/*  430 */                 short height = Tiles.decodeHeight(tt);
/*  431 */                 if (height > maxHeight)
/*  432 */                   maxHeight = height; 
/*      */               } 
/*      */             }  } 
/*      */         } 
/*      */       } 
/*  437 */       if (maxHeight - currHeight > 200)
/*      */       {
/*  439 */         performer.getCommunicator().sendNormalServerMessage("The ground is too steep to mine at here. You need to make it more flat.");
/*      */         
/*  441 */         return true;
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  447 */       byte type = Tiles.decodeType(encodedTile);
/*  448 */       if (!Tiles.isReinforcedFloor(type))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  456 */         if (anyAdjacentReinforcedFloors(tilex, tiley, false)) {
/*      */           
/*  458 */           performer.getCommunicator().sendNormalServerMessage("You can not mine next to reinforced floors.");
/*  459 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/*  463 */     if (out) {
/*      */       
/*  465 */       if (!Terraforming.allCornersAtRockLevel(tilex, tiley, Server.surfaceMesh)) {
/*      */         
/*  467 */         performer.getCommunicator().sendNormalServerMessage("The roof sounds strangely hollow and you notice dirt flowing in, so you stop mining.");
/*      */         
/*  469 */         return true;
/*      */       } 
/*  471 */       if (counter == 1.0F)
/*      */       {
/*  473 */         Server.getInstance().broadCastAction(performer.getName() + " starts mining a hole to the outside.", performer, 5);
/*      */ 
/*      */         
/*  476 */         performer.getCommunicator().sendNormalServerMessage("You begin to mine your way to the outside.");
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  481 */     else if (dir == 1) {
/*      */       
/*  483 */       if (isAtRockLevelAndNotRock(digTilex, digTiley, h + cceil)) {
/*      */         
/*  485 */         performer.getCommunicator().sendNormalServerMessage("The roof sounds strangely hollow and you notice dirt flowing in, so you stop mining.");
/*      */         
/*  487 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  491 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, int dir, short action, float counter) {
/*      */     int digTilex, digTiley;
/*  503 */     boolean done = true;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  508 */     boolean handled = false;
/*  509 */     byte type = Tiles.decodeType(tile);
/*  510 */     byte fType = Server.getClientCaveFlags(tilex, tiley);
/*  511 */     switch (action) {
/*      */ 
/*      */       
/*      */       case 518:
/*  515 */         if (performer.getPower() < 4 || source.getTemplateId() != 176)
/*      */         {
/*      */ 
/*      */           
/*  519 */           if (source.getTemplateId() != 782) {
/*      */             
/*  521 */             handled = false;
/*      */             break;
/*      */           }  } 
/*  524 */         handled = true;
/*      */         
/*  526 */         if (anyReinforcedFloors(performer)) {
/*      */           
/*  528 */           performer.getCommunicator().sendNormalServerMessage("You cannot raise reinforced floors.");
/*  529 */           done = true;
/*      */           
/*      */           break;
/*      */         } 
/*  533 */         digTilex = (int)performer.getStatus().getPositionX() + 2 >> 2;
/*  534 */         digTiley = (int)performer.getStatus().getPositionY() + 2 >> 2;
/*  535 */         done = raiseRockLevel(performer, source, digTilex, digTiley, counter, act);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 145:
/*  541 */         if (source.isMiningtool() && (dir == 1 || type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id)) {
/*      */           
/*  543 */           handled = true;
/*  544 */           done = handle_MINE(act, performer, source, tilex, tiley, action, counter, dir);
/*      */         } 
/*  546 */         if (source.isMiningtool() && type == Tiles.Tile.TILE_CAVE_EXIT.id && fType == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id) {
/*      */           
/*  548 */           handled = true;
/*  549 */           done = handle_MINE(act, performer, source, tilex, tiley, action, counter, dir);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 156:
/*  555 */         if (source.isMiningtool() && (dir == 1 || type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id)) {
/*      */           
/*  557 */           handled = true;
/*  558 */           done = handle_PROSPECT(performer, source, tilex, tiley, tile, counter, dir);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 150:
/*  564 */         if (source.isMiningtool() && (dir == 1 || type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id)) {
/*      */           
/*  566 */           handled = true;
/*  567 */           done = handle_FLATTEN(act, performer, source, tilex, tiley, tile, counter, dir);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 532:
/*  573 */         if (source.isMiningtool() && (dir == 1 || type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id)) {
/*      */           
/*  575 */           handled = true;
/*  576 */           done = handle_LEVEL(act, performer, source, tilex, tiley, tile, counter, dir);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 229:
/*  582 */         if (source.getTemplateId() == 429) {
/*      */           
/*  584 */           handled = true;
/*  585 */           done = handle_REINFORCE(performer, source, tilex, tiley, tile, counter, dir);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 141:
/*  591 */         if (performer.getDeity() != null && performer.getDeity().isMountainGod()) {
/*      */           
/*  593 */           done = MethodsReligion.pray(act, performer, counter);
/*  594 */           handled = true;
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 193:
/*  600 */         done = true;
/*  601 */         handled = true;
/*  602 */         handle_REPAIR_STRUCT(performer, source, tilex, tiley, tile);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 176:
/*  607 */         handled = true;
/*  608 */         if (source.isRoadMarker() && Features.Feature.HIGHWAYS.isEnabled() && !source.isTraded()) {
/*      */           
/*  610 */           HighwayPos highwayPos = MethodsHighways.getNewHighwayPosCorner(performer, tilex, tiley, onSurface, null, null);
/*  611 */           if (highwayPos != null)
/*      */           {
/*  613 */             if (MethodsHighways.middleOfHighway(highwayPos) && !MethodsHighways.containsMarker(highwayPos, (byte)0)) {
/*      */               
/*  615 */               byte pLinks = MethodsHighways.getPossibleLinksFrom(highwayPos, source);
/*  616 */               if (!MethodsHighways.canPlantMarker(performer, highwayPos, source, pLinks)) {
/*  617 */                 done = true;
/*  618 */               } else if (performer.getPower() > 0) {
/*  619 */                 done = MethodsItems.plantSignFinish(performer, source, true, highwayPos.getTilex(), highwayPos.getTiley(), highwayPos
/*  620 */                     .isOnSurface(), highwayPos.getBridgeId(), false, -1L);
/*      */               } else {
/*  622 */                 done = MethodsItems.plantSign(performer, source, counter, true, highwayPos.getTilex(), highwayPos.getTiley(), highwayPos
/*  623 */                     .isOnSurface(), highwayPos.getBridgeId(), false, -1L);
/*  624 */               }  if (done && source.isPlanted())
/*      */               {
/*      */                 
/*  627 */                 MethodsHighways.autoLink(source, pLinks);
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 155:
/*  636 */         if (source.getTemplateId() == 492 && dir == 0 && (type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id || (type == Tiles.Tile.TILE_CAVE_EXIT.id && fType == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id))) {
/*      */ 
/*      */           
/*  639 */           handled = true;
/*  640 */           done = changeFloor(act, performer, source, tilex, tiley, tile, action, counter);
/*      */         } 
/*  642 */         if (source.isCavePaveable() && dir == 0 && (type == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id || (type == Tiles.Tile.TILE_CAVE_EXIT.id && fType == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id))) {
/*      */ 
/*      */           
/*  645 */           handled = true;
/*  646 */           done = changeFloor(act, performer, source, tilex, tiley, tile, action, counter);
/*      */         } 
/*  648 */         if (source.isCavePaveable() && dir == 0 && (Tiles.isRoadType(type) || (type == Tiles.Tile.TILE_CAVE_EXIT.id && 
/*  649 */           Tiles.isRoadType(fType)))) {
/*      */ 
/*      */           
/*  652 */           handled = true;
/*  653 */           done = changeFloor(act, performer, source, tilex, tiley, tile, action, counter);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 191:
/*  659 */         if (source.getTemplateId() == 1115 && dir == 0 && (Tiles.isRoadType(type) || (type == Tiles.Tile.TILE_CAVE_EXIT.id && 
/*  660 */           Tiles.isRoadType(fType)))) {
/*      */           
/*  662 */           handled = true;
/*  663 */           done = Terraforming.destroyPave(performer, source, tilex, tiley, onSurface, tile, counter);
/*      */         } 
/*  665 */         if (source.getTemplateId() == 97 && dir == 0 && (type == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id || (type == Tiles.Tile.TILE_CAVE_EXIT.id && fType == Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id))) {
/*      */ 
/*      */           
/*  668 */           handled = true;
/*  669 */           done = changeFloor(act, performer, source, tilex, tiley, tile, action, counter);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 231:
/*  675 */         if (source.getTemplateId() == 153 && dir == 0 && (type == Tiles.Tile.TILE_PLANKS.id || (type == Tiles.Tile.TILE_CAVE_EXIT.id && fType == Tiles.Tile.TILE_PLANKS.id))) {
/*      */ 
/*      */           
/*  678 */           handled = true;
/*  679 */           done = Terraforming.tarFloor(performer, source, tilex, tiley, onSurface, tile, counter);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 1:
/*  686 */         handled = true;
/*  687 */         done = action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, dir, action, counter);
/*      */         break;
/*      */       
/*      */       default:
/*  691 */         handled = false;
/*      */         break;
/*      */     } 
/*      */     
/*  695 */     if (!handled)
/*      */     {
/*  697 */       done = super.action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter);
/*      */     }
/*      */     
/*  700 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private void handle_REPAIR_STRUCT(Creature performer, Item source, int tilex, int tiley, int tile) {
/*  705 */     int templateId = source.getTemplateId();
/*  706 */     boolean shakerOrb = (templateId == 601);
/*      */ 
/*      */ 
/*      */     
/*  710 */     boolean hasSpecialPermission = ((WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315));
/*      */ 
/*      */ 
/*      */     
/*  714 */     if (!shakerOrb && !hasSpecialPermission) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  719 */     byte decodedType = Tiles.decodeType(tile);
/*  720 */     if (decodedType != Tiles.Tile.TILE_CAVE_EXIT.id && decodedType != Tiles.Tile.TILE_CAVE.id && 
/*      */       
/*  722 */       !Tiles.isReinforcedFloor(decodedType)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  727 */     Communicator comm = performer.getCommunicator();
/*      */     
/*  729 */     HighwayPos highwayPos = MethodsHighways.getHighwayPos(tilex, tiley, false);
/*  730 */     if (highwayPos != null && MethodsHighways.onHighway(highwayPos)) {
/*      */       return;
/*      */     }
/*      */     
/*  734 */     VolaTile t = Zones.getOrCreateTile(tilex, tiley, false);
/*  735 */     if (t.getStructure() != null) {
/*      */       
/*  737 */       comm.sendNormalServerMessage("You cannot do that inside a cave dwelling.");
/*      */       return;
/*      */     } 
/*  740 */     if (t.getVillage() == null)
/*      */     {
/*      */       
/*  743 */       for (int x = -1; x <= 1; x++) {
/*      */         
/*  745 */         for (int y = -1; y <= 1; y++) {
/*      */           
/*  747 */           if (x != 0 || y != 0) {
/*      */             
/*  749 */             VolaTile vt = Zones.getTileOrNull(tilex + x, tiley + y, false);
/*  750 */             if (vt != null && vt.getStructure() != null) {
/*      */               
/*  752 */               comm.sendNormalServerMessage("The nearby cave dwelling interfears with that.");
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*  760 */     if (t != null) {
/*      */       
/*  762 */       if ((t.getCreatures()).length > 0) {
/*      */         
/*  764 */         comm.sendNormalServerMessage("That tile is occupied by creatures.");
/*      */         return;
/*      */       } 
/*  767 */       if (!shakerOrb) {
/*      */ 
/*      */         
/*  770 */         t.destroyEverything();
/*      */ 
/*      */       
/*      */       }
/*  774 */       else if ((t.getItems()).length > 0) {
/*      */         
/*  776 */         comm.sendNormalServerMessage("You should remove the items first.");
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/*  782 */     int ts = Server.surfaceMesh.getTile(tilex, tiley);
/*  783 */     byte type = Tiles.decodeType(ts);
/*      */     
/*  785 */     if (type == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */ 
/*      */       
/*  788 */       VolaTile surfaceTile = Zones.getTileOrNull(tilex, tiley, true);
/*  789 */       if (surfaceTile != null && (surfaceTile.getCreatures()).length > 0) {
/*      */         
/*  791 */         comm.sendNormalServerMessage("There are creatures in the way of the cave entrance.");
/*      */         return;
/*      */       } 
/*      */     } 
/*  795 */     if (Tiles.isMineDoor(type))
/*      */     {
/*  797 */       if (shakerOrb) {
/*      */         
/*  799 */         comm.sendNormalServerMessage("You need to destroy the mine door first.");
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*  804 */     Terraforming.setAsRock(tilex, tiley, false);
/*      */ 
/*      */     
/*  807 */     if (!shakerOrb) {
/*      */       
/*  809 */       if (performer.getPower() >= 5) {
/*  810 */         comm.sendNormalServerMessage("Tried to set " + tilex + "," + tiley + " to rock.");
/*      */       } else {
/*  812 */         comm.sendNormalServerMessage("Tried to set the tile to rock.");
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  818 */     comm.sendNormalServerMessage("You throw the " + source
/*  819 */         .getName() + " on the ground. The earth suddenly shakes and the rock falls in!");
/*      */     
/*  821 */     Server.getInstance().broadCastAction(performer
/*  822 */         .getName() + " throws " + source.getNameWithGenus() + " on the ground. The earth suddenly shakes and the rock falls in!", performer, 5);
/*      */ 
/*      */     
/*  825 */     Items.destroyItem(source.getWurmId());
/*  826 */     performer.achievement(151);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handle_REINFORCE(Creature performer, Item source, int tilex, int tiley, int tile, float counter, int dir) {
/*  832 */     Communicator comm = performer.getCommunicator();
/*  833 */     if (dir == 1) {
/*      */       
/*  835 */       comm.sendNormalServerMessage("You can not reinforce ceilings.");
/*  836 */       return true;
/*      */     } 
/*      */     
/*  839 */     if (!Methods.isActionAllowed(performer, (short)229, false, tilex, tiley, tile, dir) || 
/*  840 */       !Methods.isActionAllowed(performer, (short)145, false, tilex, tiley, tile, dir))
/*      */     {
/*  842 */       return true;
/*      */     }
/*  844 */     if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */       
/*  846 */       comm.sendNormalServerMessage("The water is too deep here.");
/*  847 */       return true;
/*      */     } 
/*  849 */     if (performer.getFloorLevel() != 0) {
/*      */       
/*  851 */       comm.sendNormalServerMessage("You need to be stood on the ground to be able to do this.");
/*  852 */       return true;
/*      */     } 
/*  854 */     boolean done = false;
/*  855 */     Skills skills = performer.getSkills();
/*  856 */     boolean insta = (performer.getPower() > 3);
/*  857 */     Skill mining = skills.getSkillOrLearn(1008);
/*  858 */     int time = 0;
/*  859 */     if (counter == 1.0F) {
/*      */       
/*  861 */       SoundPlayer.playSound("sound.work.masonry", tilex, tiley, performer.isOnSurface(), 1.0F);
/*      */       
/*  863 */       time = Math.min(250, Actions.getStandardActionTime(performer, mining, source, 0.0D));
/*      */       
/*      */       try {
/*  866 */         performer.getCurrentAction().setTimeLeft(time);
/*      */       }
/*  868 */       catch (NoSuchActionException nsa) {
/*      */         
/*  870 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/*  872 */       String floorexit = (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id) ? "exit" : "floor";
/*  873 */       comm.sendNormalServerMessage("You start to reinforce the cave " + floorexit + ".");
/*  874 */       Server.getInstance()
/*  875 */         .broadCastAction(performer.getName() + " starts to reinforce the cave " + floorexit + ".", performer, 5);
/*      */ 
/*      */       
/*  878 */       performer.sendActionControl(Actions.actionEntrys[229].getVerbString(), true, time);
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*  885 */         time = performer.getCurrentAction().getTimeLeft();
/*      */       }
/*  887 */       catch (NoSuchActionException nsa) {
/*      */         
/*  889 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/*      */     } 
/*  892 */     if (counter * 10.0F > time || insta) {
/*      */       
/*  894 */       mining.skillCheck(20.0D, source, 0.0D, false, counter);
/*  895 */       done = true;
/*  896 */       String floorexit = (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id) ? "exit" : "floor";
/*  897 */       comm.sendNormalServerMessage("You reinforce the cave " + floorexit + ".");
/*  898 */       Server.getInstance().broadCastAction(performer.getName() + " reinforces the cave " + floorexit + ".", performer, 5);
/*  899 */       Items.destroyItem(source.getWurmId());
/*  900 */       if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */ 
/*      */         
/*  903 */         Server.setClientCaveFlags(tilex, tiley, Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id);
/*      */       }
/*      */       else {
/*      */         
/*  907 */         int encodedValue = Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id, 
/*      */             
/*  909 */             Tiles.decodeData(tile));
/*  910 */         Server.caveMesh.setTile(tilex, tiley, encodedValue);
/*      */       } 
/*  912 */       Players.getInstance().sendChangedTile(tilex, tiley, false, true);
/*      */     } 
/*  914 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handle_LEVEL(Action act, Creature performer, Item source, int tilex, int tiley, int tile, float counter, int dir) {
/*  920 */     int tx = performer.getTileX();
/*  921 */     int ty = performer.getTileY();
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
/*  932 */     int dx = Math.abs(tx - tilex);
/*  933 */     int dy = Math.abs(ty - tiley);
/*  934 */     if (dx > 1 || dy > 1 || dx + dy < 1) {
/*      */       
/*  936 */       performer.getCommunicator().sendNormalServerMessage("You can only level tiles that you are adjacent to.");
/*  937 */       return true;
/*      */     } 
/*  939 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && dir == 0) {
/*      */       
/*  941 */       performer.getCommunicator().sendNormalServerMessage("You cannot level a reinforced floor.");
/*  942 */       return true;
/*      */     } 
/*  944 */     if (Zones.isTileCornerProtected(tilex, tiley)) {
/*      */       
/*  946 */       performer.getCommunicator().sendNormalServerMessage("That tile is protected by the gods. You can not level there.");
/*  947 */       return true;
/*      */     } 
/*  949 */     if (performer.getFloorLevel() != 0 && dir == 0) {
/*      */       
/*  951 */       performer.getCommunicator().sendNormalServerMessage("You need to be stood on the ground to be able to do this.");
/*  952 */       return true;
/*      */     } 
/*      */     
/*  955 */     boolean done = flatten(performer, source, tile, tilex, tiley, counter, act, dir);
/*  956 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handle_FLATTEN(Action act, Creature performer, Item source, int tilex, int tiley, int tile, float counter, int dir) {
/*  962 */     if (tilex != performer.getTileX() && tiley != performer.getTileY()) {
/*      */       
/*  964 */       performer.getCommunicator()
/*  965 */         .sendNormalServerMessage("You must stand on the tile you are trying to flatten.");
/*  966 */       return true;
/*      */     } 
/*  968 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && dir == 0) {
/*      */       
/*  970 */       performer.getCommunicator().sendNormalServerMessage("You cannot flatten a reinforced floor.");
/*  971 */       return true;
/*      */     } 
/*  973 */     if (Zones.isTileCornerProtected(tilex, tiley)) {
/*      */       
/*  975 */       performer.getCommunicator().sendNormalServerMessage("This tile is protected by the gods. You can not flatten here.");
/*  976 */       return true;
/*      */     } 
/*  978 */     if (performer.getFloorLevel() != 0 && dir == 0) {
/*      */       
/*  980 */       performer.getCommunicator().sendNormalServerMessage("You need to be stood on the ground to be able to do this.");
/*  981 */       return true;
/*      */     } 
/*  983 */     boolean done = flatten(performer, source, tile, tilex, tiley, counter, act, dir);
/*  984 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handle_PROSPECT(Creature performer, Item source, int tilex, int tiley, int tile, float counter, int dir) {
/*  990 */     Communicator comm = performer.getCommunicator();
/*  991 */     if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */       
/*  993 */       comm.sendNormalServerMessage("The water is too deep to prospect.");
/*  994 */       return true;
/*      */     } 
/*  996 */     if (performer.getFloorLevel() != 0) {
/*      */       
/*  998 */       comm.sendNormalServerMessage("You need to be stood on the ground to be able to do this.");
/*  999 */       return true;
/*      */     } 
/* 1001 */     short h = Tiles.decodeHeight(tile);
/* 1002 */     if (h <= -25 && dir != 1) {
/*      */       
/* 1004 */       comm.sendNormalServerMessage("The water is too deep to prospect.");
/* 1005 */       return true;
/*      */     } 
/* 1007 */     if (dir == 0 && Tiles.isReinforcedFloor(Tiles.decodeType(tile))) {
/*      */       
/* 1009 */       comm.sendNormalServerMessage("This floor is reinforced and may not be prospected.");
/* 1010 */       return true;
/*      */     } 
/* 1012 */     Skills skills = performer.getSkills();
/* 1013 */     Skill prospecting = skills.getSkillOrLearn(10032);
/*      */     
/* 1015 */     int time = 0;
/* 1016 */     if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1025 */       String sstring = "sound.work.prospecting1";
/* 1026 */       int x = Server.rand.nextInt(3);
/* 1027 */       if (x == 0) {
/* 1028 */         sstring = "sound.work.prospecting2";
/* 1029 */       } else if (x == 1) {
/* 1030 */         sstring = "sound.work.prospecting3";
/*      */       } 
/* 1032 */       SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 1.0F);
/* 1033 */       time = (int)Math.max(30.0D, 100.0D - prospecting.getKnowledge(source, 0.0D));
/*      */       
/*      */       try {
/* 1036 */         performer.getCurrentAction().setTimeLeft(time);
/*      */       }
/* 1038 */       catch (NoSuchActionException nsa) {
/*      */         
/* 1040 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/* 1042 */       comm.sendNormalServerMessage("You start to gather fragments of the rock.");
/* 1043 */       Server.getInstance()
/* 1044 */         .broadCastAction(performer.getName() + " starts gathering fragments of the rock.", performer, 5);
/*      */       
/* 1046 */       performer.sendActionControl(Actions.actionEntrys[156].getVerbString(), true, time);
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1052 */         time = performer.getCurrentAction().getTimeLeft();
/*      */       }
/* 1054 */       catch (NoSuchActionException nsa) {
/*      */         
/* 1056 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/*      */     } 
/*      */     
/* 1060 */     if (counter * 10.0F <= time)
/*      */     {
/* 1062 */       return false;
/*      */     }
/*      */     
/* 1065 */     performer.getStatus().modifyStamina(-3000.0F);
/* 1066 */     prospecting.skillCheck(1.0D, source, 0.0D, false, counter);
/* 1067 */     source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */ 
/*      */     
/* 1070 */     String findString = "only rock.";
/*      */     
/* 1072 */     comm.sendNormalServerMessage("You find only rock.");
/* 1073 */     if (prospecting.getKnowledge(0.0D) > 20.0D) {
/*      */       
/* 1075 */       r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/*      */ 
/*      */       
/* 1078 */       int m = 100;
/* 1079 */       int max = Math.min(100, 20 + r
/*      */           
/* 1081 */           .nextInt(80));
/* 1082 */       comm.sendNormalServerMessage("It is of " + getShardQlDescription(max) + ".");
/*      */     } 
/* 1084 */     if (prospecting.getKnowledge(0.0D) > 40.0D) {
/*      */       
/* 1086 */       r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 102533L);
/* 1087 */       if (r.nextInt(100) == 0)
/* 1088 */         comm.sendNormalServerMessage("You will find salt here!"); 
/*      */     } 
/* 1090 */     if (prospecting.getKnowledge(0.0D) > 20.0D) {
/*      */       
/* 1092 */       r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 6883L);
/*      */       
/* 1094 */       if (r.nextInt(200) == 0) {
/* 1095 */         comm.sendNormalServerMessage("You will find flint here!");
/*      */       }
/*      */     } 
/* 1098 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handle_MINE(Action act, Creature performer, Item source, int tilex, int tiley, short action, float counter, int dir) {
/* 1108 */     Vector2f pos2f = performer.getPos2f();
/* 1109 */     TilePos digTilePos = CoordUtils.WorldToTile(pos2f.add(2.0F, 2.0F));
/* 1110 */     int digTilex = digTilePos.x;
/* 1111 */     int digTiley = digTilePos.y;
/* 1112 */     if (digTilex > tilex + 1 || digTilex < tilex || digTiley > tiley + 1 || digTiley < tiley) {
/*      */ 
/*      */       
/* 1115 */       if (performer.getPower() > 1) {
/* 1116 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to mine at " + tilex + "," + tiley + ".");
/*      */       } else {
/* 1118 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to mine there.");
/* 1119 */       }  return true;
/*      */     } 
/* 1121 */     return mine(act, performer, source, tilex, tiley, action, counter, dir, digTilePos);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mine(Action act, Creature performer, Item source, int tilex, int tiley, short action, float counter, int dir, TilePos digTilePos) {
/* 1127 */     Communicator comm = performer.getCommunicator();
/* 1128 */     int digCorner = Server.caveMesh.getTile(digTilePos);
/* 1129 */     int digTilex = digTilePos.x;
/* 1130 */     int digTiley = digTilePos.y;
/*      */     
/* 1132 */     if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */       
/* 1134 */       comm.sendNormalServerMessage("The water is too deep to mine.");
/* 1135 */       return true;
/*      */     } 
/* 1137 */     if (performer.isOnSurface()) {
/*      */       
/* 1139 */       comm.sendNormalServerMessage("You are too far away to mine there.");
/* 1140 */       return true;
/*      */     } 
/* 1142 */     if (performer.getFloorLevel() != 0 && dir == 0) {
/*      */       
/* 1144 */       comm.sendNormalServerMessage("You need to be stood on the ground to be able to do this.");
/* 1145 */       return true;
/*      */     } 
/* 1147 */     int meshTile = Server.caveMesh.getTile(tilex, tiley);
/* 1148 */     byte actualType = Tiles.decodeType(meshTile);
/* 1149 */     byte tileType = (actualType == Tiles.Tile.TILE_CAVE_EXIT.id) ? Server.getClientCaveFlags(tilex, tiley) : actualType;
/* 1150 */     if (Zones.isTileCornerProtected(digTilex, digTiley)) {
/*      */       
/* 1152 */       comm.sendNormalServerMessage("This tile is protected by the gods. You can not mine here.");
/* 1153 */       return true;
/*      */     } 
/* 1155 */     int h = Tiles.decodeHeight(digCorner);
/* 1156 */     if (h <= -25 && dir != 1 && tileType != Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id) {
/*      */       
/* 1158 */       comm.sendNormalServerMessage("The water is too deep to mine.");
/* 1159 */       return true;
/*      */     } 
/* 1161 */     short cceil = (short)(Tiles.decodeData(digCorner) & 0xFF);
/* 1162 */     if (dir == 1) {
/*      */       
/* 1164 */       if (cceil > 60 + performer.getFloorLevel() * 30) {
/*      */         
/* 1166 */         comm.sendNormalServerMessage("You cannot reach the ceiling.");
/* 1167 */         return true;
/*      */       } 
/*      */     } else {
/* 1170 */       if (cceil >= 254) {
/*      */         
/* 1172 */         comm.sendNormalServerMessage("Lowering the floor further would make the cavern unstable.");
/*      */         
/* 1174 */         return true;
/*      */       } 
/* 1176 */       if (dir == 0 && tileType == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && performer
/* 1177 */         .getPower() < 2) {
/*      */         
/* 1179 */         if (performer.getStrengthSkill() < 21.0D) {
/*      */           
/* 1181 */           comm.sendNormalServerMessage("You need to be stronger in order to remove the reinforcement.");
/*      */           
/* 1183 */           return true;
/*      */         } 
/*      */ 
/*      */         
/* 1187 */         if (!Methods.isActionAllowed(performer, (short)229, false, tilex, tiley, meshTile, dir) || 
/*      */ 
/*      */           
/* 1190 */           !Methods.isActionAllowed(performer, (short)145, false, tilex, tiley, meshTile, dir))
/*      */         {
/* 1192 */           return true;
/*      */         }
/*      */         
/* 1195 */         VolaTile t = Zones.getTileOrNull(tilex, tiley, false);
/* 1196 */         if (t != null && t.getStructure() != null) {
/*      */           
/* 1198 */           comm.sendNormalServerMessage("You cannot remove the reinforcement inside cave dwellings.");
/*      */           
/* 1200 */           return true;
/*      */         } 
/*      */       } 
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
/* 1216 */     if (Tiles.decodeData(digCorner) == 0 && 
/* 1217 */       Tiles.decodeHeight(digCorner) == 
/* 1218 */       Tiles.decodeHeight(Server.surfaceMesh.getTile(digTilePos))) {
/*      */       
/* 1220 */       comm.sendNormalServerMessage("You fail to produce anything here. The rock is stone hard.");
/* 1221 */       return true;
/*      */     } 
/* 1223 */     if (dir == 0 && tileType != Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && anyReinforcedFloors(performer)) {
/*      */       
/* 1225 */       comm.sendNormalServerMessage("You cannot mine a floor next to reinforced floors.");
/*      */       
/* 1227 */       return true;
/*      */     } 
/*      */     
/* 1230 */     Skills skills = performer.getSkills();
/* 1231 */     Skill mining = skills.getSkillOrLearn(1008);
/* 1232 */     Skill tool = null;
/*      */     
/* 1234 */     boolean insta = (performer.getPower() >= 5 || (performer.getPower() >= 4 && Servers.isThisATestServer()));
/*      */     
/*      */     try {
/* 1237 */       tool = skills.getSkill(source.getPrimarySkill());
/*      */     }
/* 1239 */     catch (Exception ex) {
/*      */ 
/*      */       
/*      */       try {
/* 1243 */         tool = skills.learn(source.getPrimarySkill(), 1.0F);
/*      */       }
/* 1245 */       catch (NoSuchSkillException nse) {
/*      */         
/* 1247 */         logger.log(Level.WARNING, performer
/* 1248 */             .getName() + " trying to mine with an item with no primary skill: " + source
/* 1249 */             .getName());
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1254 */     if (dir == 0) {
/*      */       
/* 1256 */       for (int x = 0; x >= -1; x--) {
/*      */         
/* 1258 */         for (int y = 0; y >= -1; y--) {
/*      */           
/* 1260 */           VolaTile volaTile = Zones.getTileOrNull(digTilex + x, digTiley + y, false);
/* 1261 */           if (volaTile != null && volaTile.getStructure() != null) {
/*      */             
/* 1263 */             if (volaTile.getStructure().isTypeHouse()) {
/*      */               
/* 1265 */               if (x == 0 && y == 0) {
/* 1266 */                 performer.getCommunicator().sendNormalServerMessage("You cannot mine in a building.", (byte)3);
/*      */               } else {
/*      */                 
/* 1269 */                 performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a building.", (byte)3);
/*      */               } 
/* 1271 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 1275 */             for (BridgePart bp : volaTile.getBridgeParts()) {
/*      */               
/* 1277 */               if (bp.getType().isSupportType()) {
/*      */                 
/* 1279 */                 performer.getCommunicator().sendNormalServerMessage("The bridge support nearby prevents mining.");
/* 1280 */                 return true;
/*      */               } 
/* 1282 */               if ((x == -1 && bp.hasEastExit()) || (x == 0 && bp
/* 1283 */                 .hasWestExit()) || (y == -1 && bp
/* 1284 */                 .hasSouthExit()) || (y == 0 && bp
/* 1285 */                 .hasNorthExit())) {
/*      */                 
/* 1287 */                 performer.getCommunicator().sendNormalServerMessage("The end of the bridge nearby prevents mining.");
/* 1288 */                 return true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1301 */       VolaTile vt = Zones.getTileOrNull(digTilex, digTiley, false);
/* 1302 */       if (vt != null && (vt.getFencesForLevel(0)).length > 0) {
/*      */         
/* 1304 */         performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a fence.", (byte)3);
/*      */         
/* 1306 */         return true;
/*      */       } 
/* 1308 */       vt = Zones.getTileOrNull(digTilex, digTiley - 1, false);
/* 1309 */       if (vt != null && (vt.getFencesForLevel(0)).length > 0)
/*      */       {
/* 1311 */         for (Fence f : vt.getFencesForLevel(0)) {
/*      */           
/* 1313 */           if (!f.isHorizontal()) {
/*      */             
/* 1315 */             performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a fence.", (byte)3);
/*      */             
/* 1317 */             return true;
/*      */           } 
/*      */         } 
/*      */       }
/* 1321 */       vt = Zones.getTileOrNull(digTilex - 1, digTiley, false);
/* 1322 */       if (vt != null && (vt.getFencesForLevel(0)).length > 0)
/*      */       {
/* 1324 */         for (Fence f : vt.getFencesForLevel(0)) {
/*      */           
/* 1326 */           if (f.isHorizontal()) {
/*      */             
/* 1328 */             performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a fence.", (byte)3);
/*      */             
/* 1330 */             return true;
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 1335 */     int time = 0;
/* 1336 */     if (counter == 1.0F) {
/*      */ 
/*      */       
/* 1339 */       boolean isBlocked = isBlocked(tilex, tiley, digTilex, digTiley, performer, action, counter, dir, h, cceil, meshTile);
/*      */       
/* 1341 */       if (isBlocked)
/*      */       {
/* 1343 */         return true;
/*      */       }
/*      */       
/* 1346 */       Server.getInstance().broadCastAction(performer.getName() + " starts mining.", performer, 5);
/* 1347 */       comm.sendNormalServerMessage("You start to mine.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1354 */       time = Actions.getStandardActionTime(performer, mining, source, 0.0D);
/*      */       
/*      */       try {
/* 1357 */         performer.getCurrentAction().setTimeLeft(time);
/*      */       }
/* 1359 */       catch (NoSuchActionException nsa) {
/*      */         
/* 1361 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/* 1363 */       performer.sendActionControl(Actions.actionEntrys[145].getVerbString(), true, time);
/*      */       
/* 1365 */       source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 1366 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */       
/* 1368 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1375 */       time = performer.getCurrentAction().getTimeLeft();
/*      */     }
/* 1377 */     catch (NoSuchActionException nsa) {
/*      */       
/* 1379 */       logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */     } 
/* 1381 */     if (act.justTickedSecond()) {
/*      */ 
/*      */       
/* 1384 */       boolean isBlocked = isBlocked(tilex, tiley, digTilex, digTiley, performer, action, counter, dir, h, cceil, meshTile);
/*      */       
/* 1386 */       if (isBlocked)
/*      */       {
/* 1388 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1392 */     if (counter * 10.0F <= time && !insta) {
/*      */       
/* 1394 */       if (act.currentSecond() % 5 == 0 || (act.currentSecond() == 3 && time < 50)) {
/*      */         
/* 1396 */         String sstring = "sound.work.mining1";
/* 1397 */         int x = Server.rand.nextInt(3);
/* 1398 */         if (x == 0) {
/* 1399 */           sstring = "sound.work.mining2";
/* 1400 */         } else if (x == 1) {
/* 1401 */           sstring = "sound.work.mining3";
/* 1402 */         }  SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 1.0F);
/*      */         
/* 1404 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 1405 */         performer.getStatus().modifyStamina(-7000.0F);
/*      */       } 
/* 1407 */       return false;
/*      */     } 
/*      */     
/* 1410 */     if (act.getRarity() != 0)
/*      */     {
/* 1412 */       performer.playPersonalSound("sound.fx.drumroll");
/*      */     }
/* 1414 */     if (dir == 0 && tileType == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id) {
/*      */       
/* 1416 */       TileEvent.log(tilex, tiley, -1, performer.getWurmId(), action);
/* 1417 */       comm.sendNormalServerMessage("You manage to remove the reinforcement.");
/*      */       
/* 1419 */       if (actualType == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */         
/* 1421 */         Server.setClientCaveFlags(tilex, tiley, (byte)0);
/*      */       }
/*      */       else {
/*      */         
/* 1425 */         Server.caveMesh.setTile(tilex, tiley, 
/* 1426 */             Tiles.encode(Tiles.decodeHeight(meshTile), Tiles.Tile.TILE_CAVE.id, 
/* 1427 */               Tiles.decodeData(meshTile)));
/*      */       } 
/* 1429 */       TileRockBehaviour.sendCaveTile(tilex, tiley, 0, 0);
/* 1430 */       return true;
/*      */     } 
/* 1432 */     VolaTile dropTile = Zones.getTileOrNull(performer.getTilePos(), performer.isOnSurface());
/* 1433 */     if (dropTile != null)
/*      */     {
/* 1435 */       if (dropTile.getNumberOfItems(performer.getFloorLevel()) > 99) {
/*      */         
/* 1437 */         comm.sendNormalServerMessage("There is no space to mine here. Clear the area first.");
/* 1438 */         return true;
/*      */       } 
/*      */     }
/* 1441 */     if (cceil >= 254) {
/*      */       
/* 1443 */       comm.sendNormalServerMessage("Lowering the floor further would make the cavern unstable.");
/* 1444 */       return true;
/*      */     } 
/*      */     
/* 1447 */     double bonus = 0.0D;
/* 1448 */     double power = 0.0D;
/*      */     
/* 1450 */     boolean done = true;
/* 1451 */     if (dir == 1) {
/*      */       
/* 1453 */       boolean createOutside = false;
/* 1454 */       if (TileRockBehaviour.allCornersAtRockHeight(tilex, tiley))
/* 1455 */         createOutside = true; 
/* 1456 */       if (!createOutside) {
/*      */         
/* 1458 */         if (isAtRockLevelAndNotRock(digTilex, digTiley, h + cceil)) {
/*      */           
/* 1460 */           comm.sendNormalServerMessage("The roof sounds strangely hollow and you notice dirt flowing in.");
/*      */           
/* 1462 */           return true;
/*      */         } 
/* 1464 */         if (Tiles.decodeHeight(digCorner) + cceil + 2 >= 
/* 1465 */           Tiles.decodeHeight(Server.surfaceMesh.getTile(digTilePos))) {
/*      */           
/* 1467 */           comm.sendNormalServerMessage("The roof sounds dangerously weak and you must abandon this attempt.");
/*      */           
/* 1469 */           return true;
/*      */         } 
/* 1471 */         Server.caveMesh.setTile(digTilex, digTiley, 
/*      */             
/* 1473 */             Tiles.encode(Tiles.decodeHeight(digCorner), 
/* 1474 */               Tiles.decodeType(digCorner), (byte)(cceil + 1)));
/*      */         
/* 1476 */         Players.getInstance().sendChangedTile(digTilePos, false, true);
/* 1477 */         if (TileRockBehaviour.allCornersAtRockHeight(tilex, tiley)) {
/* 1478 */           comm.sendNormalServerMessage("The ceiling makes a hollow sound.");
/*      */         }
/*      */       } else {
/*      */         
/* 1482 */         if (!Terraforming.allCornersAtRockLevel(tilex, tiley, Server.surfaceMesh)) {
/*      */           
/* 1484 */           comm.sendNormalServerMessage("The roof sounds strangely hollow and you notice dirt flowing in, so you stop mining.");
/*      */           
/* 1486 */           return true;
/*      */         } 
/* 1488 */         if (!TileRockBehaviour.createOutInTunnel(tilex, tiley, meshTile, performer, 0))
/*      */         {
/* 1490 */           comm.sendNormalServerMessage("You decide to stop mining in the last second. The ceiling would cave in on you!");
/*      */           
/* 1492 */           return true;
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1502 */       short ceilMod = 1;
/* 1503 */       if (Tiles.decodeData(digCorner) == 0 && 
/* 1504 */         Tiles.decodeHeight(digCorner) == Tiles.decodeHeight(Server.surfaceMesh.getTile(digTilePos))) {
/*      */         
/* 1506 */         comm.sendNormalServerMessage("You decide to stop mining in the last second. The ceiling would cave in on you!");
/*      */         
/* 1508 */         return true;
/*      */       } 
/* 1510 */       Server.caveMesh.setTile(digTilex, digTiley, Tiles.encode(
/* 1511 */             (short)(Tiles.decodeHeight(digCorner) - 1), Tiles.decodeType(digCorner), (byte)(cceil + 1)));
/*      */       
/* 1513 */       Players.getInstance().sendChangedTile(digTilePos, false, true);
/* 1514 */       for (int x = -1; x <= 0; x++) {
/*      */         
/* 1516 */         for (int y = -1; y <= 0; y++) {
/*      */ 
/*      */           
/*      */           try {
/*      */ 
/*      */ 
/*      */             
/* 1523 */             Zone toCheckForChange = Zones.getZone(digTilex + x, digTiley + y, false);
/* 1524 */             toCheckForChange.changeTile(digTilex + x, digTiley + y);
/*      */           }
/* 1526 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1528 */             logger.log(Level.INFO, "no such zone?: " + (digTilex + x) + "," + (digTiley + y), (Throwable)nsz);
/*      */ 
/*      */             
/* 1531 */             comm.sendNormalServerMessage("You can't mine there.");
/* 1532 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1538 */     int itemTemplateCreated = TileRockBehaviour.getItemTemplateForTile(Tiles.decodeType(meshTile));
/*      */     
/* 1540 */     float diff = TileRockBehaviour.getDifficultyForTile(Tiles.decodeType(meshTile));
/*      */     
/* 1542 */     float tickCounter = counter;
/*      */     
/* 1544 */     if (tool != null)
/* 1545 */       bonus = tool.skillCheck(diff, source, 0.0D, false, tickCounter) / 5.0D; 
/* 1546 */     power = Math.max(1.0D, mining
/* 1547 */         .skillCheck(diff, source, bonus, false, tickCounter));
/*      */ 
/*      */     
/*      */     try {
/* 1551 */       double imbueEnhancement = 1.0D + 0.23047D * source.getSkillSpellImprovement(1008) / 100.0D;
/*      */       
/* 1553 */       if (mining.getKnowledge(0.0D) * imbueEnhancement < power)
/* 1554 */         power = mining.getKnowledge(0.0D) * imbueEnhancement; 
/* 1555 */       r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/* 1556 */       int m = TileRockBehaviour.MAX_QL;
/* 1557 */       if (itemTemplateCreated == 146 || itemTemplateCreated == 38)
/* 1558 */         m = 100; 
/* 1559 */       if (itemTemplateCreated == 39)
/* 1560 */         performer.achievement(372); 
/* 1561 */       float modifier = 1.0F;
/* 1562 */       if (source.getSpellEffects() != null)
/*      */       {
/* 1564 */         modifier = source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */       }
/* 1566 */       int max = Math.min(m, (int)(20.0D + r.nextInt(80) * imbueEnhancement));
/* 1567 */       power = Math.min(power, max);
/* 1568 */       if (source.isCrude())
/* 1569 */         power = 1.0D; 
/* 1570 */       float orePower = GeneralUtilities.calcOreRareQuality(power * modifier, act.getRarity(), source
/* 1571 */           .getRarity());
/*      */       
/* 1573 */       Item newItem = ItemFactory.createItem(itemTemplateCreated, orePower, act.getRarity(), null);
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
/* 1585 */       newItem.setLastOwnerId(performer.getWurmId());
/* 1586 */       newItem.setDataXY(tilex, tiley);
/* 1587 */       newItem.putItemInfrontof(performer, 0.0F);
/*      */       
/* 1589 */       comm.sendNormalServerMessage("You mine some " + newItem.getName() + ".");
/* 1590 */       Server.getInstance().broadCastAction(performer.getName() + " mines some " + newItem.getName() + ".", performer, 5);
/* 1591 */       TileRockBehaviour.createGem(tilex, tiley, performer, power, false, act);
/*      */     }
/* 1593 */     catch (Exception ex) {
/*      */       
/* 1595 */       logger.log(Level.WARNING, "Factory failed to produce item", ex);
/*      */     } 
/*      */     
/* 1598 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean raiseRockLevel(Creature performer, Item source, int tilex, int tiley, float counter, Action act) {
/* 1604 */     if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */       
/* 1606 */       performer.getCommunicator().sendNormalServerMessage("The ground can not be raised here.");
/* 1607 */       return true;
/*      */     } 
/* 1609 */     int tile = 0;
/*      */     
/* 1611 */     if (performer.isOnSurface()) {
/* 1612 */       tile = Server.surfaceMesh.getTile(tilex, tiley);
/*      */     } else {
/*      */       
/* 1615 */       tile = Server.caveMesh.getTile(tilex, tiley);
/* 1616 */       if (!Tiles.isReinforcedFloor(Tiles.decodeType(tile)) && anyReinforcedFloors(performer)) {
/*      */         
/* 1618 */         performer.getCommunicator().sendNormalServerMessage("You cannot raise the corner next to reinforced floors.");
/* 1619 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 1623 */     if (counter == 1.0F || counter == 0.0F || act.justTickedSecond()) {
/*      */       
/* 1625 */       if (performer.getCurrentTile().getStructure() != null) {
/*      */         
/* 1627 */         performer.getCommunicator().sendNormalServerMessage("This cannot be done in buildings.");
/* 1628 */         return true;
/*      */       } 
/* 1630 */       if (Zones.protectedTiles[tilex][tiley]) {
/*      */         
/* 1632 */         performer.getCommunicator().sendNormalServerMessage("For some strange reason you can't bring yourself to change this place.");
/*      */         
/* 1634 */         return true;
/*      */       } 
/* 1636 */       if (Terraforming.isAltarBlocking(performer, tilex, tiley)) {
/*      */         
/* 1638 */         performer.getCommunicator().sendNormalServerMessage("You cannot build here, since this is holy ground.");
/*      */         
/* 1640 */         return true;
/*      */       } 
/* 1642 */       if (performer.getLayer() < 0) {
/*      */         
/* 1644 */         if (CaveTile.decodeCeilingHeight(tile) <= 20) {
/*      */           
/* 1646 */           performer.getCommunicator().sendNormalServerMessage("The ceiling is too close.");
/* 1647 */           return true;
/*      */         } 
/* 1649 */         if (performer.getFloorLevel() > 0) {
/*      */           
/* 1651 */           performer.getCommunicator().sendNormalServerMessage("You must be standing on the ground in order to do this!");
/* 1652 */           return true;
/*      */         } 
/* 1654 */         if (Zones.isTileCornerProtected(tilex, tiley))
/*      */         {
/* 1656 */           performer.getCommunicator().sendNormalServerMessage("This tile is protected by the gods. You can not raise the corner here.");
/* 1657 */           return true;
/*      */         }
/*      */       
/* 1660 */       } else if (performer.getFloorLevel() == 0) {
/*      */         
/* 1662 */         for (int x = 0; x >= -1; x--) {
/*      */           
/* 1664 */           for (int y = 0; y >= -1; y--) {
/*      */             
/* 1666 */             int tx = Zones.safeTileX(tilex + x);
/* 1667 */             int ty = Zones.safeTileY(tiley + y);
/* 1668 */             if (Tiles.decodeType(Server.caveMesh.getTile(tx, ty)) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */               
/* 1670 */               performer.getCommunicator().sendNormalServerMessage("The opening is too close.");
/* 1671 */               return true;
/*      */             } 
/* 1673 */             int ttile = Server.surfaceMesh.getTile(tx, ty);
/* 1674 */             if (Tiles.decodeType(ttile) != Tiles.Tile.TILE_ROCK.id) {
/*      */               
/* 1676 */               performer.getCommunicator().sendNormalServerMessage("The concrete won't stick to that.");
/* 1677 */               return true;
/*      */             } 
/* 1679 */             VolaTile vtile = Zones.getTileOrNull(tx, ty, performer.isOnSurface());
/* 1680 */             if (vtile != null) {
/*      */               
/* 1682 */               if (vtile.getStructure() != null) {
/*      */                 
/* 1684 */                 performer.getCommunicator().sendNormalServerMessage("The structure is in the way.");
/* 1685 */                 return true;
/*      */               } 
/* 1687 */               if (x == 0 && y == 0) {
/*      */                 
/* 1689 */                 Fence[] arrayOfFence = vtile.getFences(); int i = arrayOfFence.length; byte b = 0; if (b < i) { Fence fence = arrayOfFence[b];
/*      */                   
/* 1691 */                   performer.getCommunicator().sendNormalServerMessage("The " + fence
/* 1692 */                       .getName() + " is in the way.");
/* 1693 */                   return true; }
/*      */ 
/*      */               
/* 1696 */               } else if (x == -1 && y == 0) {
/*      */                 
/* 1698 */                 for (Fence fence : vtile.getFences()) {
/* 1699 */                   if (fence.isHorizontal()) {
/*      */                     
/* 1701 */                     performer.getCommunicator().sendNormalServerMessage("The " + fence
/* 1702 */                         .getName() + " is in the way.");
/* 1703 */                     return true;
/*      */                   } 
/*      */                 } 
/* 1706 */               } else if (y == -1 && x == 0) {
/*      */                 
/* 1708 */                 for (Fence fence : vtile.getFences()) {
/* 1709 */                   if (!fence.isHorizontal()) {
/*      */                     
/* 1711 */                     performer.getCommunicator().sendNormalServerMessage("The " + fence
/* 1712 */                         .getName() + " is in the way.");
/* 1713 */                     return true;
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/* 1719 */         }  int slopeDown = Terraforming.getMaxSurfaceDownSlope(tilex, tiley);
/*      */ 
/*      */         
/* 1722 */         int maxSlope = Servers.localServer.PVPSERVER ? -25 : -40;
/* 1723 */         if (performer.getPower() > 4 && source.getTemplateId() == 176) {
/*      */           
/* 1725 */           if (slopeDown < -300)
/*      */           {
/* 1727 */             performer.getCommunicator().sendNormalServerMessage("Maximum slope would be exceeded.");
/* 1728 */             return true;
/*      */           }
/*      */         
/* 1731 */         } else if (slopeDown < maxSlope) {
/*      */           
/* 1733 */           if (performer.getPower() == 4 && source.getTemplateId() == 176) {
/* 1734 */             performer.getCommunicator().sendNormalServerMessage("Maximum slope would be exceeded.");
/*      */           } else {
/* 1736 */             performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " would only flow away.");
/* 1737 */           }  return true;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1742 */         performer.getCommunicator().sendNormalServerMessage("You must be standing on the ground in order to do this!");
/* 1743 */         return true;
/*      */       } 
/* 1745 */       if (source.getTemplateId() != 176 && source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */         
/* 1747 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/* 1748 */             .getName() + " contains too little material to be usable.");
/* 1749 */         return true;
/*      */       } 
/*      */     } 
/* 1752 */     boolean done = true;
/* 1753 */     short h = Tiles.decodeHeight(tile);
/* 1754 */     if (h >= -25 || source.getTemplateId() == 176) {
/*      */       
/* 1756 */       Skills skills = performer.getSkills();
/* 1757 */       Skill masonry = null;
/* 1758 */       done = false;
/*      */       
/*      */       try {
/* 1761 */         masonry = skills.getSkill(1013);
/*      */       }
/* 1763 */       catch (Exception ex) {
/*      */         
/* 1765 */         masonry = skills.learn(1013, 1.0F);
/*      */       } 
/* 1767 */       int time = 0;
/* 1768 */       if (counter == 1.0F) {
/*      */         
/* 1770 */         time = (int)Math.max(30.0D, 100.0D - masonry.getKnowledge(source, 0.0D));
/*      */         
/*      */         try {
/* 1773 */           performer.getCurrentAction().setTimeLeft(time);
/*      */         }
/* 1775 */         catch (NoSuchActionException nsa) {
/*      */           
/* 1777 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/* 1779 */         if (source.getTemplateId() == 176) {
/*      */           
/* 1781 */           performer.getCommunicator().sendNormalServerMessage("You will the rock to raise up.");
/*      */         }
/*      */         else {
/*      */           
/* 1785 */           performer.getCommunicator().sendNormalServerMessage("You start to spread out the " + source.getName() + ".");
/* 1786 */           Server.getInstance().broadCastAction(performer.getName() + " starts spreading the " + source.getName() + ".", performer, 5);
/*      */         } 
/*      */         
/* 1789 */         performer.sendActionControl(Actions.actionEntrys[518].getVerbString(), true, time);
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1795 */           time = performer.getCurrentAction().getTimeLeft();
/*      */         }
/* 1797 */         catch (NoSuchActionException nsa) {
/*      */           
/* 1799 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*      */       } 
/* 1802 */       if (counter * 10.0F > time || source.getTemplateId() == 176) {
/*      */         
/* 1804 */         if (source.getTemplateId() != 176) {
/*      */           
/* 1806 */           performer.getStatus().modifyStamina(-3000.0F);
/* 1807 */           source.setWeight(source.getWeightGrams() - source.getTemplate().getWeightGrams(), true);
/* 1808 */           masonry.skillCheck(1.0D, source, 0.0D, false, counter);
/* 1809 */           source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */         } 
/* 1811 */         done = true;
/* 1812 */         if (performer.getLayer() < 0) {
/*      */           
/* 1814 */           Server.caveMesh.setTile(tilex, tiley, 
/*      */ 
/*      */               
/* 1817 */               Tiles.encode((short)(Tiles.decodeHeight(tile) + 1), Tiles.decodeType(tile), 
/* 1818 */                 (byte)(CaveTile.decodeCeilingHeight(tile) - 1)));
/* 1819 */           tile = Server.caveMesh.getTile(tilex, tiley);
/*      */         }
/*      */         else {
/*      */           
/* 1823 */           tile = Server.rockMesh.getTile(tilex, tiley);
/* 1824 */           Server.rockMesh
/* 1825 */             .setTile(tilex, tiley, 
/*      */ 
/*      */               
/* 1828 */               Tiles.encode((short)(Tiles.decodeHeight(tile) + 1), Tiles.decodeType(tile), 
/* 1829 */                 Tiles.decodeData(tile)));
/* 1830 */           tile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1831 */           Server.surfaceMesh
/* 1832 */             .setTile(tilex, tiley, 
/*      */ 
/*      */               
/* 1835 */               Tiles.encode((short)(Tiles.decodeHeight(tile) + 1), Tiles.decodeType(tile), 
/* 1836 */                 Tiles.decodeData(tile)));
/*      */         } 
/* 1838 */         if (source.getTemplateId() != 176 && source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */           
/* 1840 */           performer.getCommunicator().sendNormalServerMessage("The " + source
/* 1841 */               .getName() + " contains too little material to be usable.");
/* 1842 */           return true;
/*      */         } 
/* 1844 */         Players.getInstance().sendChangedTile(tilex, tiley, (performer.getLayer() >= 0), false);
/* 1845 */         performer.getCommunicator().sendNormalServerMessage("You raise the ground a bit.");
/* 1846 */         if (source.getTemplateId() != 176) {
/* 1847 */           Server.getInstance().broadCastAction(performer.getName() + " raises the ground a bit.", performer, 5);
/*      */         }
/*      */       } 
/*      */     } else {
/* 1851 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep and would only dissolve the " + source
/* 1852 */           .getName() + ".");
/* 1853 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isAtRockLevelAndNotRock(int tilex, int tiley, int height) {
/* 1858 */     int rtile = Server.rockMesh.getTile(tilex, tiley);
/* 1859 */     int rheight = Tiles.decodeHeight(rtile);
/* 1860 */     if (rheight <= height) {
/*      */       
/* 1862 */       int stile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1863 */       if (Tiles.decodeType(stile) != Tiles.Tile.TILE_ROCK.id && Tiles.decodeType(stile) != Tiles.Tile.TILE_CLIFF.id)
/*      */       {
/* 1865 */         return true;
/*      */       }
/*      */     } 
/* 1868 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean flatten(Creature performer, Item source, int tile, int tilex, int tiley, float counter, Action act, int dir) {
/* 1874 */     boolean done = false;
/*      */     
/* 1876 */     String action = act.getActionEntry().getActionString().toLowerCase();
/* 1877 */     short[][] cornerHeights = new short[2][2];
/*      */     
/* 1879 */     if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */       
/* 1881 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to " + action + ".");
/* 1882 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1886 */     byte type = Tiles.decodeType(tile);
/*      */     
/* 1888 */     if (type != Tiles.Tile.TILE_CAVE.id && ((!Tiles.isReinforcedFloor(type) && !Tiles.isRoadType(type)) || dir == 0)) {
/*      */       
/* 1890 */       performer.getCommunicator().sendNormalServerMessage("You can not " + action + " this type of tile.");
/* 1891 */       return true;
/*      */     } 
/* 1893 */     if (performer.getFloorLevel() != 0 && dir == 0) {
/*      */       
/* 1895 */       performer.getCommunicator().sendNormalServerMessage("You need to be stood on the ground to be able to do this.");
/* 1896 */       return true;
/*      */     } 
/* 1898 */     if (act.getNumber() == 532 && dir == 0 && 
/* 1899 */       !Terraforming.isFlat(performer.getTileX(), performer.getTileY(), performer.isOnSurface(), 0)) {
/*      */       
/* 1901 */       performer.getCommunicator().sendNormalServerMessage("You need to be standing on flat ground to be able to level.");
/* 1902 */       return true;
/*      */     } 
/* 1904 */     if (act.getNumber() == 532 && dir == 1 && 
/* 1905 */       !isCeilingFlat(performer.getTileX(), performer.getTileY())) {
/*      */       
/* 1907 */       performer.getCommunicator().sendNormalServerMessage("You need to be standing under a flat ceiling to be able to level an adjacent tile.");
/* 1908 */       return true;
/*      */     } 
/* 1910 */     if (act.getNumber() == 150 && tilex != performer.getTileX() && tiley != performer.getTileY()) {
/*      */       
/* 1912 */       if (dir == 0) {
/* 1913 */         performer.getCommunicator().sendNormalServerMessage("You need to be standing on the tile you are flattening.");
/*      */       } else {
/* 1915 */         performer.getCommunicator().sendNormalServerMessage("You need to be standing under the tile you are flattening.");
/* 1916 */       }  return true;
/*      */     } 
/*      */     
/* 1919 */     if (dir == 0)
/*      */     {
/* 1921 */       if (Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex - 1, tiley - 1))) || 
/* 1922 */         Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex - 1, tiley))) || 
/* 1923 */         Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex - 1, tiley + 1))) || 
/* 1924 */         Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley - 1))) || 
/* 1925 */         Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley + 1))) || 
/* 1926 */         Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex + 1, tiley - 1))) || 
/* 1927 */         Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex + 1, tiley))) || 
/* 1928 */         Tiles.isReinforcedFloor(Tiles.decodeType(Server.caveMesh.getTile(tilex + 1, tiley + 1)))) {
/*      */         
/* 1930 */         performer.getCommunicator().sendNormalServerMessage("You can not " + action + " next to reinforced floors.");
/* 1931 */         return true;
/*      */       } 
/*      */     }
/* 1934 */     boolean insta = (source.getTemplateId() == 176 && performer.getPower() > 2);
/*      */     
/* 1936 */     if (counter == 1.0F) {
/*      */       
/* 1938 */       int belowWater = 0;
/* 1939 */       short maxHeight = 0;
/* 1940 */       short minHeight = Short.MAX_VALUE;
/* 1941 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 1943 */         for (int y = 0; y <= 1; y++) {
/*      */ 
/*      */           
/* 1946 */           short ht = getHeight(tilex + x, tiley + y, dir);
/* 1947 */           cornerHeights[x][y] = ht;
/* 1948 */           if (ht < -7.0F)
/* 1949 */             belowWater++; 
/* 1950 */           if (ht > maxHeight)
/* 1951 */             maxHeight = ht; 
/* 1952 */           if (ht < minHeight) {
/* 1953 */             minHeight = ht;
/*      */           }
/* 1955 */           if (Zones.isTileCornerProtected(tilex + x, tiley + y)) {
/*      */             
/* 1957 */             performer.getCommunicator().sendNormalServerMessage("This tile is protected by the gods. You can not " + action + " here.");
/*      */             
/* 1959 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1963 */       if (belowWater == 4) {
/*      */         
/* 1965 */         performer.getCommunicator().sendNormalServerMessage("You can't " + action + " at that depth.");
/* 1966 */         return true;
/*      */       } 
/* 1968 */       int requiredHeight = minHeight;
/* 1969 */       if (dir == 1) {
/* 1970 */         requiredHeight = maxHeight;
/* 1971 */       } else if (act.getNumber() == 532) {
/*      */ 
/*      */ 
/*      */         
/* 1975 */         requiredHeight = getHeight(performer.getTileX(), performer.getTileY(), dir);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1980 */       int totalup = 0;
/* 1981 */       int totaldown = 0;
/* 1982 */       for (int i = 0; i <= 1; i++) {
/*      */         
/* 1984 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 1986 */           int diff = cornerHeights[i][y] - requiredHeight;
/* 1987 */           if (diff > 0) {
/* 1988 */             totalup += diff;
/*      */           } else {
/* 1990 */             totaldown += Math.abs(diff);
/*      */           } 
/*      */         } 
/* 1993 */       }  if (totalup + totaldown == 0) {
/*      */         
/* 1995 */         performer.getCommunicator().sendNormalServerMessage("The tile is already flat.");
/*      */         
/* 1997 */         return true;
/*      */       } 
/*      */       
/* 2000 */       float totaltime = 0.0F;
/*      */ 
/*      */       
/* 2003 */       if ((totalup > 0 && dir == 0) || (totaldown > 0 && dir == 1)) {
/*      */ 
/*      */         
/* 2006 */         Skill mining = performer.getSkills().getSkillOrLearn(1008);
/* 2007 */         int tickMining = Actions.getStandardActionTime(performer, mining, source, 0.0D) + 5;
/* 2008 */         if (dir == 0) {
/* 2009 */           totaltime = (tickMining * totalup) * 1.25F;
/*      */         } else {
/* 2011 */           totaltime = (tickMining * totaldown) * 1.25F;
/* 2012 */         }  act.setNextTick(tickMining);
/*      */       } 
/*      */       
/* 2015 */       if (totaldown > 0 && dir == 0) {
/*      */         
/* 2017 */         Skill masonry = performer.getSkills().getSkillOrLearn(1013);
/* 2018 */         int tickMasonry = Actions.getStandardActionTime(performer, masonry, null, 0.0D) + 5;
/* 2019 */         totaltime += (tickMasonry * totaldown) * 1.125F;
/* 2020 */         if (totalup == 0)
/* 2021 */           act.setNextTick(tickMasonry); 
/*      */       } 
/* 2023 */       act.setTickCount(1);
/* 2024 */       act.setTimeLeft((int)totaltime);
/* 2025 */       act.setData(requiredHeight);
/* 2026 */       if (!insta) {
/*      */         
/* 2028 */         int digTile = Server.caveMesh.getTile(tilex, tiley);
/* 2029 */         int h = Tiles.decodeHeight(digTile);
/* 2030 */         short cceil = (short)CaveTile.decodeCeilingHeight(digTile);
/* 2031 */         if (isBlocked(tilex, tiley, tilex, tiley, performer, act
/* 2032 */             .getNumber(), counter, dir, h, cceil, digTile))
/* 2033 */           return true; 
/* 2034 */         String gc = (dir == 0) ? "ground" : "ceiling";
/* 2035 */         performer.getCommunicator().sendNormalServerMessage("You start to " + action + " the " + gc + ".");
/*      */         
/* 2037 */         Server.getInstance().broadCastAction(performer
/* 2038 */             .getName() + " starts to " + action + " the " + gc + ".", performer, 5);
/*      */ 
/*      */         
/* 2041 */         act.setTimeLeft((int)totaltime);
/* 2042 */         performer.sendActionControl(action, true, (int)totaltime);
/*      */         
/* 2044 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 2045 */         performer.getStatus().modifyStamina(-1800.0F);
/*      */       } 
/*      */     } 
/*      */     
/* 2049 */     if (counter * 10.0F >= act.getNextTick() || insta) {
/*      */ 
/*      */       
/* 2052 */       if (Zones.protectedTiles[tilex][tiley]) {
/*      */         
/* 2054 */         performer.getCommunicator().sendNormalServerMessage("Your body goes limp and you find no strength to continue here. Weird.");
/*      */         
/* 2056 */         return true;
/*      */       } 
/* 2058 */       if (performer.getStatus().getStamina() < 6000) {
/*      */         
/* 2060 */         performer.getCommunicator().sendNormalServerMessage("You must rest.");
/* 2061 */         return true;
/*      */       } 
/* 2063 */       if (!insta) {
/*      */         
/* 2065 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 2066 */         performer.getStatus().modifyStamina(-5000.0F);
/*      */       } 
/*      */       
/* 2069 */       int requiredHeight = (int)act.getData();
/*      */ 
/*      */       
/* 2072 */       int highx = 0;
/* 2073 */       int highy = 0;
/* 2074 */       int highCorner = 0;
/* 2075 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 2077 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 2079 */           int diff = getHeight(tilex + x, tiley + y, dir) - requiredHeight;
/* 2080 */           if (dir == 0) {
/*      */             
/* 2082 */             if (diff > highCorner)
/*      */             {
/* 2084 */               highx = tilex + x;
/* 2085 */               highy = tiley + y;
/* 2086 */               highCorner = diff;
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 2091 */           else if (diff < highCorner) {
/*      */             
/* 2093 */             highx = tilex + x;
/* 2094 */             highy = tiley + y;
/* 2095 */             highCorner = diff;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2100 */       if (highCorner != 0) {
/*      */         
/* 2102 */         if (dir == 0 && anyReinforcedFloorTiles(highx, highy)) {
/*      */           
/* 2104 */           performer.getCommunicator().sendNormalServerMessage("You cannot modify one of the corners due to reinforced floors.");
/*      */           
/* 2106 */           return true;
/*      */         } 
/* 2108 */         int highTile = Server.caveMesh.getTile(highx, highy);
/* 2109 */         short cceil = (short)(Tiles.decodeData(highTile) & 0xFF);
/* 2110 */         if (dir == 1 && act.getNumber() == 150 && cceil > 60 + performer
/* 2111 */           .getFloorLevel() * 30) {
/*      */           
/* 2113 */           performer.getCommunicator().sendNormalServerMessage("You cannot reach the ceiling.");
/* 2114 */           return true;
/*      */         } 
/* 2116 */         if (cceil >= 254) {
/*      */           
/* 2118 */           if (dir == 0) {
/* 2119 */             performer.getCommunicator().sendNormalServerMessage("Lowering the floor further would make the cavern unstable.");
/*      */           } else {
/*      */             
/* 2122 */             performer.getCommunicator().sendNormalServerMessage("Raising the ceiling further would make the cavern unstable.");
/*      */           } 
/* 2124 */           return true;
/*      */         } 
/* 2126 */         if (dir == 1 && Tiles.decodeHeight(highTile) + cceil + 2 >= 
/* 2127 */           Tiles.decodeHeight(Server.rockMesh.getTile(highx, highy))) {
/*      */           
/* 2129 */           performer.getCommunicator().sendNormalServerMessage("The roof sounds dangerously weak and you must abandon this attempt.");
/*      */           
/* 2131 */           return true;
/*      */         } 
/*      */         
/* 2134 */         Skill mining = performer.getSkills().getSkillOrLearn(1008);
/* 2135 */         Skill tool = null;
/*      */         
/*      */         try {
/* 2138 */           tool = performer.getSkills().getSkillOrLearn(source.getPrimarySkill());
/*      */         }
/* 2140 */         catch (NoSuchSkillException nse) {
/*      */           
/* 2142 */           logger.log(Level.WARNING, performer.getName() + " trying to mine with an item with no primary skill: " + source
/* 2143 */               .getName());
/*      */         } 
/* 2145 */         int tickTime = Actions.getStandardActionTime(performer, mining, source, 0.0D);
/* 2146 */         double bonus = 0.0D;
/* 2147 */         double power = 0.0D;
/* 2148 */         int itemTemplateCreated = TileRockBehaviour.getItemTemplateForTile(Tiles.decodeType(tile));
/* 2149 */         float diff = TileRockBehaviour.getDifficultyForTile(Tiles.decodeType(tile));
/*      */         
/* 2151 */         if (!insta) {
/*      */           
/* 2153 */           String sstring = "sound.work.mining1";
/* 2154 */           int rndsound = Server.rand.nextInt(3);
/* 2155 */           if (rndsound == 0) {
/* 2156 */             sstring = "sound.work.mining2";
/* 2157 */           } else if (rndsound == 1) {
/* 2158 */             sstring = "sound.work.mining3";
/* 2159 */           }  SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 1.0F);
/*      */           
/* 2161 */           VolaTile dropTile = Zones.getTileOrNull(performer.getTileX(), performer.getTileY(), performer.isOnSurface());
/* 2162 */           if (dropTile != null)
/*      */           {
/* 2164 */             if (dropTile.getNumberOfItems(performer.getFloorLevel()) > 99) {
/*      */               
/* 2166 */               performer.getCommunicator().sendNormalServerMessage("There is no space to mine here. Clear the area first.");
/*      */               
/* 2168 */               return true;
/*      */             } 
/*      */           }
/*      */           
/* 2172 */           if (tool != null)
/* 2173 */             bonus = tool.skillCheck(diff, source, 0.0D, false, (tickTime / 10)) / 5.0D; 
/* 2174 */           power = Math.max(1.0D, mining.skillCheck(diff, source, bonus, false, (tickTime / 10)));
/*      */         } 
/*      */         
/* 2177 */         if (dir == 0) {
/*      */           
/* 2179 */           Server.caveMesh.setTile(highx, highy, Tiles.encode(
/* 2180 */                 (short)(Tiles.decodeHeight(highTile) - 1), Tiles.decodeType(highTile), (byte)(cceil + 1)));
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2185 */           Server.caveMesh.setTile(highx, highy, Tiles.encode(
/* 2186 */                 Tiles.decodeHeight(highTile), Tiles.decodeType(highTile), (byte)(cceil + 1)));
/*      */         } 
/*      */         
/* 2189 */         Players.getInstance().sendChangedTile(highx, highy, false, true);
/* 2190 */         if (dir == 0)
/*      */         {
/*      */ 
/*      */           
/* 2194 */           for (int j = -1; j <= 0; j++) {
/*      */             
/* 2196 */             for (int y = -1; y <= 0; y++) {
/*      */ 
/*      */               
/*      */               try {
/*      */ 
/*      */                 
/* 2202 */                 Zone toCheckForChange = Zones.getZone(highx + j, highy + y, false);
/* 2203 */                 toCheckForChange.changeTile(highx + j, highy + y);
/*      */               }
/* 2205 */               catch (NoSuchZoneException nsz) {
/*      */                 
/* 2207 */                 logger.log(Level.INFO, "no such zone?: " + (highx + j) + "," + (highy + y), (Throwable)nsz);
/*      */                 
/* 2209 */                 performer.getCommunicator().sendNormalServerMessage("You can't mine there.");
/* 2210 */                 return true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/* 2215 */         if (!insta) {
/*      */           try
/*      */           {
/*      */ 
/*      */             
/* 2220 */             if (act.getRarity() != 0)
/*      */             {
/* 2222 */               performer.playPersonalSound("sound.fx.drumroll");
/*      */             }
/* 2224 */             if (mining.getKnowledge(0.0D) < power)
/* 2225 */               power = mining.getKnowledge(0.0D); 
/* 2226 */             r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/* 2227 */             int m = TileRockBehaviour.MAX_QL;
/* 2228 */             if (itemTemplateCreated == 146 || itemTemplateCreated == 38)
/* 2229 */               m = 100; 
/* 2230 */             if (itemTemplateCreated == 39)
/* 2231 */               performer.achievement(372); 
/* 2232 */             float modifier = 1.0F;
/* 2233 */             if (source.getSpellEffects() != null)
/*      */             {
/* 2235 */               modifier = source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */             }
/* 2237 */             int max = Math.min(m, 20 + r
/* 2238 */                 .nextInt(80));
/* 2239 */             power = Math.min(power, max);
/* 2240 */             if (source.isCrude())
/* 2241 */               power = 1.0D; 
/* 2242 */             float orePower = GeneralUtilities.calcOreRareQuality(power * modifier, act.getRarity(), source.getRarity());
/* 2243 */             Item newItem = ItemFactory.createItem(itemTemplateCreated, orePower, act.getRarity(), null);
/* 2244 */             newItem.setLastOwnerId(performer.getWurmId());
/* 2245 */             newItem.setDataXY(tilex, tiley);
/* 2246 */             newItem.putItemInfrontof(performer, 0.0F);
/*      */             
/* 2248 */             performer.getCommunicator().sendNormalServerMessage("You mine some " + newItem.getName() + ".");
/* 2249 */             Server.getInstance().broadCastAction(performer
/* 2250 */                 .getName() + " mines some " + newItem.getName() + ".", performer, 5);
/* 2251 */             TileRockBehaviour.createGem(tilex, tiley, performer, power, false, act);
/*      */           }
/* 2253 */           catch (Exception ex)
/*      */           {
/* 2255 */             logger.log(Level.WARNING, "Factory failed to produce item", ex);
/*      */           }
/*      */         
/*      */         }
/* 2259 */       } else if (dir == 0) {
/*      */ 
/*      */         
/* 2262 */         int lowx = 0;
/* 2263 */         int lowy = 0;
/* 2264 */         int lowCorner = 0;
/* 2265 */         for (int j = 0; j <= 1; j++) {
/*      */           
/* 2267 */           for (int y = 0; y <= 1; y++) {
/*      */             
/* 2269 */             int diff = requiredHeight - Tiles.decodeHeight(Server.caveMesh.getTile(tilex + j, tiley + y));
/* 2270 */             if (diff > lowCorner) {
/*      */               
/* 2272 */               lowx = tilex + j;
/* 2273 */               lowy = tiley + y;
/* 2274 */               lowCorner = diff;
/*      */             } 
/*      */           } 
/*      */         } 
/* 2278 */         if (lowCorner > 0) {
/*      */           
/* 2280 */           if (anyReinforcedFloorTiles(lowx, lowy)) {
/*      */             
/* 2282 */             performer.getCommunicator().sendNormalServerMessage("You cannot modify one of the corners due to reinforced floors.");
/*      */             
/* 2284 */             return true;
/*      */           } 
/*      */           
/* 2287 */           int lowTile = Server.caveMesh.getTile(lowx, lowy);
/* 2288 */           if (CaveTile.decodeCeilingHeight(lowTile) <= 21) {
/*      */             
/* 2290 */             performer.getCommunicator().sendNormalServerMessage("The ceiling is too close.");
/* 2291 */             return true;
/*      */           } 
/* 2293 */           if (!insta) {
/*      */             
/* 2295 */             Skill masonry = performer.getSkills().getSkillOrLearn(1013);
/* 2296 */             int tickMasonry = Actions.getStandardActionTime(performer, masonry, null, 0.0D);
/*      */ 
/*      */             
/*      */             try {
/* 2300 */               ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(782);
/* 2301 */               Item concrete = MethodsStructure.creatureHasItem(template, performer, true);
/*      */               
/* 2303 */               if (concrete != null)
/*      */               {
/* 2305 */                 performer.getStatus().modifyStamina(-3500.0F);
/* 2306 */                 concrete.setWeight(concrete.getWeightGrams() - concrete.getTemplate().getWeightGrams(), true);
/* 2307 */                 masonry.skillCheck(1.0D, source, 0.0D, false, (tickMasonry / 10));
/* 2308 */                 concrete.setDamage(concrete.getDamage() + 5.0E-4F * concrete.getDamageModifier());
/*      */               }
/*      */               else
/*      */               {
/* 2312 */                 performer.getCommunicator().sendNormalServerMessage("One or more corners need to be filled in with concrete.");
/*      */                 
/* 2314 */                 return true;
/*      */               }
/*      */             
/* 2317 */             } catch (NoSuchTemplateException e) {
/*      */ 
/*      */               
/* 2320 */               logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 2321 */               return true;
/*      */             } 
/*      */           } 
/*      */           
/* 2325 */           Server.caveMesh.setTile(lowx, lowy, 
/*      */ 
/*      */               
/* 2328 */               Tiles.encode((short)(Tiles.decodeHeight(lowTile) + 1), Tiles.decodeType(lowTile), 
/* 2329 */                 (byte)(CaveTile.decodeCeilingHeight(lowTile) - 1)));
/* 2330 */           Players.getInstance().sendChangedTile(lowx, lowy, false, false);
/* 2331 */           performer.getCommunicator().sendNormalServerMessage("You raise the ground a bit.");
/* 2332 */           Server.getInstance().broadCastAction(performer.getName() + " raises the ground a bit.", performer, 5);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2337 */           performer.getCommunicator().sendNormalServerMessage("Done.");
/* 2338 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 2342 */       done = true;
/* 2343 */       boolean above = false;
/* 2344 */       for (int i = 0; i <= 1; i++) {
/*      */         
/* 2346 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 2348 */           short ht = getHeight(tilex + i, tiley + y, dir);
/* 2349 */           if (dir == 0) {
/*      */             
/* 2351 */             if (ht > requiredHeight) {
/*      */               
/* 2353 */               above = true;
/* 2354 */               done = false;
/*      */             } 
/* 2356 */             if (ht != requiredHeight) {
/* 2357 */               done = false;
/*      */             
/*      */             }
/*      */           }
/* 2361 */           else if (ht < requiredHeight) {
/*      */             
/* 2363 */             above = true;
/* 2364 */             done = false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2369 */       if (!done) {
/*      */         
/* 2371 */         int tickNextTime = 0;
/* 2372 */         if (above) {
/*      */ 
/*      */           
/* 2375 */           Skill mining = performer.getSkills().getSkillOrLearn(1008);
/* 2376 */           tickNextTime = Actions.getStandardActionTime(performer, mining, source, 0.0D);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2381 */           Skill masonry = performer.getSkills().getSkillOrLearn(1013);
/* 2382 */           tickNextTime = Actions.getStandardActionTime(performer, masonry, null, 0.0D);
/*      */         } 
/* 2384 */         act.incTickCount();
/* 2385 */         act.incNextTick((tickNextTime + 5));
/*      */         
/* 2387 */         act.setRarity(performer.getRarity());
/*      */ 
/*      */       
/*      */       }
/* 2391 */       else if (dir == 1) {
/*      */         
/* 2393 */         if (isCeilingFlat(tilex, tiley)) {
/* 2394 */           performer.getCommunicator().sendNormalServerMessage("The ceiling is now flat.");
/*      */         } else {
/* 2396 */           performer.getCommunicator().sendNormalServerMessage("You do not seem to be able to make the ceiling flat.");
/*      */         }
/*      */       
/*      */       }
/* 2400 */       else if (Terraforming.isFlat(tilex, tiley, false, 0)) {
/* 2401 */         performer.getCommunicator().sendNormalServerMessage("The floor is now flat.");
/*      */       } else {
/* 2403 */         performer.getCommunicator().sendNormalServerMessage("You do not seem to be able to make the floor flat.");
/*      */       } 
/*      */     } 
/*      */     
/* 2407 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean anyReinforcedFloorTiles(int tilex, int tiley) {
/* 2413 */     for (int x = tilex - 1; x <= tilex; x++) {
/*      */       
/* 2415 */       for (int y = tiley - 1; y <= tiley; y++) {
/*      */         
/* 2417 */         int digTile = Server.caveMesh.getTile(x, y);
/* 2418 */         if (Tiles.decodeType(digTile) == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id)
/* 2419 */           return true; 
/*      */       } 
/*      */     } 
/* 2422 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean anyReinforcedFloors(Creature performer) {
/* 2427 */     int digTilex = (int)(performer.getStatus().getPositionX() + 2.0F) >> 2;
/* 2428 */     int digTiley = (int)(performer.getStatus().getPositionY() + 2.0F) >> 2;
/* 2429 */     int digTile = Server.caveMesh.getTile(digTilex, digTiley);
/* 2430 */     byte digType = Tiles.decodeType(digTile);
/* 2431 */     if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2432 */       return true; 
/* 2433 */     return anyAdjacentReinforcedFloors(digTilex, digTiley, false);
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
/*      */   private static boolean anyAdjacentReinforcedFloors(int digTilex, int digTiley, boolean all) {
/* 2445 */     int digTile = Server.caveMesh.getTile(digTilex - 1, digTiley - 1);
/* 2446 */     byte digType = Tiles.decodeType(digTile);
/* 2447 */     if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2448 */       return true; 
/* 2449 */     digTile = Server.caveMesh.getTile(digTilex - 1, digTiley);
/* 2450 */     digType = Tiles.decodeType(digTile);
/* 2451 */     if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2452 */       return true; 
/* 2453 */     digTile = Server.caveMesh.getTile(digTilex, digTiley - 1);
/* 2454 */     digType = Tiles.decodeType(digTile);
/* 2455 */     if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2456 */       return true; 
/* 2457 */     if (all) {
/*      */       
/* 2459 */       digTile = Server.caveMesh.getTile(digTilex + 1, digTiley - 1);
/* 2460 */       digType = Tiles.decodeType(digTile);
/* 2461 */       if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2462 */         return true; 
/* 2463 */       digTile = Server.caveMesh.getTile(digTilex + 1, digTiley);
/* 2464 */       digType = Tiles.decodeType(digTile);
/* 2465 */       if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2466 */         return true; 
/* 2467 */       digTile = Server.caveMesh.getTile(digTilex + 1, digTiley + 1);
/* 2468 */       digType = Tiles.decodeType(digTile);
/* 2469 */       if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2470 */         return true; 
/* 2471 */       digTile = Server.caveMesh.getTile(digTilex, digTiley + 1);
/* 2472 */       digType = Tiles.decodeType(digTile);
/* 2473 */       if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2474 */         return true; 
/* 2475 */       digTile = Server.caveMesh.getTile(digTilex - 1, digTiley + 1);
/* 2476 */       digType = Tiles.decodeType(digTile);
/* 2477 */       if (Tiles.isReinforcedFloor(digType) || Tiles.isRoadType(digType))
/* 2478 */         return true; 
/*      */     } 
/* 2480 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isCeilingFlat(int tilex, int tiley) {
/* 2485 */     short cht = getRealCeilingHeight(tilex, tiley);
/* 2486 */     if (cht != getRealCeilingHeight(tilex + 1, tiley))
/* 2487 */       return false; 
/* 2488 */     if (cht != getRealCeilingHeight(tilex + 1, tiley + 1))
/* 2489 */       return false; 
/* 2490 */     if (cht != getRealCeilingHeight(tilex, tiley + 1))
/* 2491 */       return false; 
/* 2492 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private short getRealCeilingHeight(int tilex, int tiley) {
/* 2497 */     int meshTile = Server.caveMesh.getTile(tilex, tiley);
/* 2498 */     int ht = Tiles.decodeHeight(meshTile);
/* 2499 */     int cceil = CaveTile.decodeCeilingHeight(meshTile);
/* 2500 */     return (short)(ht + cceil);
/*      */   }
/*      */ 
/*      */   
/*      */   private short getHeight(int tilex, int tiley, int dir) {
/* 2505 */     if (dir == 0)
/* 2506 */       return Tiles.decodeHeight(Server.caveMesh.getTile(tilex, tiley)); 
/* 2507 */     return getRealCeilingHeight(tilex, tiley);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean changeFloor(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter) {
/*      */     byte newTileType;
/* 2515 */     int pavingItem = source.getTemplateId();
/* 2516 */     if (!Methods.isActionAllowed(performer, (short)155, tilex, tiley))
/*      */     {
/* 2518 */       return true;
/*      */     }
/* 2520 */     byte type = Tiles.decodeType(tile);
/* 2521 */     byte fType = Server.getClientCaveFlags(tilex, tiley);
/* 2522 */     boolean repaving = false;
/* 2523 */     if (Tiles.isRoadType(type) || (type == Tiles.Tile.TILE_CAVE_EXIT.id && Tiles.isRoadType(fType))) {
/*      */ 
/*      */       
/* 2526 */       repaving = true;
/* 2527 */       if (performer.getStrengthSkill() < 20.0D) {
/*      */         
/* 2529 */         performer.getCommunicator().sendNormalServerMessage("You need to be stronger to replace pavement.");
/* 2530 */         return true;
/*      */       } 
/*      */       
/* 2533 */       Village village = Villages.getVillageWithPerimeterAt(tilex, tiley, true);
/* 2534 */       if (village != null)
/*      */       {
/* 2536 */         if (!village.isActionAllowed(act.getNumber(), performer)) {
/*      */           
/* 2538 */           performer.getCommunicator().sendNormalServerMessage("You do not have permissions to do that.");
/* 2539 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/* 2543 */     if (Tiles.decodeHeight(tile) < -100) {
/*      */       
/* 2545 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to pave here.");
/* 2546 */       return true;
/*      */     } 
/* 2548 */     String floorexit = (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id) ? "exit" : "floor";
/* 2549 */     if (pavingItem == 492) {
/*      */ 
/*      */       
/* 2552 */       if (source.getWeightGrams() < 2000)
/*      */       {
/* 2554 */         performer.getCommunicator().sendNormalServerMessage("It takes 2kg of " + source
/* 2555 */             .getName() + " to prepare the " + floorexit + ".");
/* 2556 */         return true;
/*      */       }
/*      */     
/* 2559 */     } else if (pavingItem != 97) {
/*      */ 
/*      */ 
/*      */       
/* 2563 */       if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */         
/* 2565 */         performer.getCommunicator().sendNormalServerMessage("The amount of " + source
/* 2566 */             .getName() + " is too little to pave. You may need to combine them with other " + source.getTemplate().getPlural() + ".");
/* 2567 */         return true;
/*      */       } 
/* 2569 */     }  if (counter == 1.0F) {
/*      */       
/* 2571 */       Skill skill = performer.getSkills().getSkillOrLearn(10031);
/* 2572 */       int i = Actions.getStandardActionTime(performer, skill, source, 0.0D);
/* 2573 */       act.setTimeLeft(i);
/* 2574 */       String str = (pavingItem == 492) ? ("prepare the reinforced " + floorexit) : ((pavingItem == 97) ? "remove the mortar" : ((repaving ? "repave the " : "pave the prepared ") + floorexit));
/*      */ 
/*      */       
/* 2577 */       if (pavingItem == 519) {
/* 2578 */         performer.getCommunicator().sendNormalServerMessage("You break up the collosus brick and start to " + str + ".");
/*      */       } else {
/* 2580 */         performer.getCommunicator().sendNormalServerMessage("You start to " + str + " with the " + source.getName() + ".");
/* 2581 */       }  Server.getInstance().broadCastAction(performer.getName() + " starts to " + str + ".", performer, 5);
/* 2582 */       performer.sendActionControl(act.getActionEntry().getVerbString(), true, i);
/* 2583 */       performer.getStatus().modifyStamina(-1000.0F);
/* 2584 */       return false;
/*      */     } 
/*      */     
/* 2587 */     int time = act.getTimeLeft();
/*      */     
/* 2589 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 2591 */       performer.getStatus().modifyStamina(-10000.0F);
/*      */     }
/* 2593 */     if (act.mayPlaySound())
/*      */     {
/* 2595 */       Methods.sendSound(performer, "sound.work.paving");
/*      */     }
/*      */     
/* 2598 */     if (counter * 10.0F <= time)
/*      */     {
/* 2600 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2604 */     Skill paving = performer.getSkills().getSkillOrLearn(10031);
/* 2605 */     paving.skillCheck((pavingItem == 146) ? 5.0D : 30.0D, source, 0.0D, false, counter);
/*      */     
/* 2607 */     TileEvent.log(tilex, tiley, -1, performer.getWurmId(), action);
/*      */ 
/*      */ 
/*      */     
/* 2611 */     switch (pavingItem) {
/*      */       
/*      */       case 492:
/* 2614 */         newTileType = Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id;
/*      */         break;
/*      */       case 97:
/* 2617 */         newTileType = Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id;
/*      */         break;
/*      */       case 132:
/* 2620 */         newTileType = Tiles.Tile.TILE_COBBLESTONE.id;
/*      */         break;
/*      */       case 1122:
/* 2623 */         newTileType = Tiles.Tile.TILE_COBBLESTONE_ROUND.id;
/*      */         break;
/*      */       case 519:
/* 2626 */         newTileType = Tiles.Tile.TILE_COBBLESTONE_ROUGH.id;
/*      */         break;
/*      */       case 406:
/* 2629 */         newTileType = Tiles.Tile.TILE_STONE_SLABS.id;
/*      */         break;
/*      */       case 1123:
/* 2632 */         newTileType = Tiles.Tile.TILE_SLATE_BRICKS.id;
/*      */         break;
/*      */       case 771:
/* 2635 */         newTileType = Tiles.Tile.TILE_SLATE_SLABS.id;
/*      */         break;
/*      */       case 1121:
/* 2638 */         newTileType = Tiles.Tile.TILE_SANDSTONE_BRICKS.id;
/*      */         break;
/*      */       case 1124:
/* 2641 */         newTileType = Tiles.Tile.TILE_SANDSTONE_SLABS.id;
/*      */         break;
/*      */       case 787:
/* 2644 */         newTileType = Tiles.Tile.TILE_MARBLE_SLABS.id;
/*      */         break;
/*      */       case 786:
/* 2647 */         newTileType = Tiles.Tile.TILE_MARBLE_BRICKS.id;
/*      */         break;
/*      */       case 776:
/* 2650 */         newTileType = Tiles.Tile.TILE_POTTERY_BRICKS.id;
/*      */         break;
/*      */       case 495:
/* 2653 */         newTileType = Tiles.Tile.TILE_PLANKS.id;
/*      */         break;
/*      */       default:
/* 2656 */         newTileType = Tiles.Tile.TILE_GRAVEL.id;
/*      */         break;
/*      */     } 
/* 2659 */     if (type == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */       
/* 2661 */       Server.setClientCaveFlags(tilex, tiley, newTileType);
/*      */     }
/*      */     else {
/*      */       
/* 2665 */       Server.caveMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), newTileType, Tiles.decodeData(tile)));
/*      */     } 
/* 2667 */     TileRockBehaviour.sendCaveTile(tilex, tiley, 0, 0);
/*      */     
/* 2669 */     if (pavingItem == 492) {
/*      */ 
/*      */       
/* 2672 */       source.setWeight(source.getWeightGrams() - 2000, true);
/*      */     }
/* 2674 */     else if (pavingItem != 97) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2679 */       Items.destroyItem(source.getWurmId());
/*      */     } 
/* 2681 */     String prepared = (pavingItem == 492) ? "prepared" : ((pavingItem == 97) ? "back to plain reinforcement" : "paved");
/*      */     
/* 2683 */     performer.getCommunicator().sendNormalServerMessage("The cave " + floorexit + " is " + prepared + " now.");
/* 2684 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\CaveTileBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */