/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.combat.Weapon;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.QuestionTypes;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.DbFence;
/*      */ import com.wurmonline.server.structures.DbFenceGate;
/*      */ import com.wurmonline.server.structures.DbFloor;
/*      */ import com.wurmonline.server.structures.DbWall;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.IFloor;
/*      */ import com.wurmonline.server.structures.NoSuchLockException;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.StructureSupport;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.zones.NoSuchTileException;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import com.wurmonline.shared.constants.StructureConstants;
/*      */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*      */ import com.wurmonline.shared.constants.StructureMaterialEnum;
/*      */ import com.wurmonline.shared.constants.StructureStateEnum;
/*      */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*      */ import com.wurmonline.shared.constants.WallConstants;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
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
/*      */ public final class MethodsStructure
/*      */   implements MiscConstants, QuestionTypes, ItemTypes, CounterTypes, ItemMaterials, SoundNames, VillageStatus, TimeConstants
/*      */ {
/*      */   public static final String cvsversion = "$Id: MethodsStructure.java,v 1.39 2007-04-19 23:05:18 root Exp $";
/*  119 */   private static final Logger logger = Logger.getLogger(MethodsStructure.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private static final float DEITY_FENCE_QL_GV = 100.0F;
/*      */ 
/*      */   
/*      */   private static final float DEITY_FENCE_QL_OTHER = 80.0F;
/*      */ 
/*      */   
/*      */   public static final int minDistanceToAltars = 20;
/*      */ 
/*      */ 
/*      */   
/*      */   static void tryToFinalize(Creature performer, int tilex, int tiley) {
/*      */     Structure structure;
/*      */     try {
/*  136 */       structure = performer.getStructure();
/*  137 */       if (structure.isFinalized()) {
/*      */         
/*  139 */         performer.getCommunicator().sendNormalServerMessage("The " + structure.getName() + " is already finalized.", (byte)3);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  144 */       if (structure.getWurmId() != performer.getBuildingId()) {
/*      */         
/*  146 */         performer.getCommunicator().sendNormalServerMessage("You are not planning this house.", (byte)3);
/*      */         
/*      */         return;
/*      */       } 
/*  150 */       if (!structure.contains(tilex, tiley)) {
/*      */         
/*  152 */         performer.getCommunicator().sendNormalServerMessage("You don't even plan to build there!", (byte)3);
/*      */         
/*      */         return;
/*      */       } 
/*  156 */       if (!Methods.isActionAllowed(performer, (short)58))
/*      */       {
/*      */         return;
/*      */       }
/*      */     }
/*  161 */     catch (NoSuchStructureException nss) {
/*      */       
/*  163 */       performer.getCommunicator().sendNormalServerMessage("You don't even plan to build there!", (byte)3);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*      */     try {
/*  169 */       if (!hasEnoughSkillToExpandStructure(performer, tilex, tiley, structure)) {
/*      */         return;
/*      */       }
/*      */       
/*  173 */       if (!structure.makeFinal(performer, performer.getName() + "'s ")) {
/*      */         
/*  175 */         performer.getCommunicator().sendNormalServerMessage("You don't even have a house planned.", (byte)3);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  180 */       performer.getCommunicator().sendNormalServerMessage("You finish planning the house.");
/*  181 */       Server.getInstance().broadCastAction(performer.getName() + " finishes planning the house.", performer, 5);
/*      */       
/*  183 */       performer.getStatus().setBuildingId(structure.getWurmId());
/*  184 */       performer.achievement(518);
/*      */     }
/*  186 */     catch (IOException iox) {
/*      */       
/*  188 */       logger.log(Level.WARNING, "Failed to save house for " + performer.getWurmId() + " at " + tilex + ", " + tiley, iox);
/*      */       
/*  190 */       performer.getCommunicator().sendNormalServerMessage("You finish planning the house, but there was a problem on the server. Please report this.");
/*      */     
/*      */     }
/*  193 */     catch (NoSuchZoneException nsz) {
/*      */       
/*  195 */       logger.log(Level.WARNING, "Failed to locate zone for " + performer.getWurmId() + " building at " + tilex + ", " + tiley, (Throwable)nsz);
/*      */       
/*  197 */       performer.getCommunicator().sendNormalServerMessage("You cannot finish planning the house, as there was a problem on the server. Please report this.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean canPlanStructureAt(Creature performer, Item tool, int tilex, int tiley, int tile) {
/*  207 */     byte type = Tiles.decodeType(tile);
/*      */     
/*  209 */     if (performer.isGuest()) {
/*      */       
/*  211 */       performer.getCommunicator().sendNormalServerMessage("Sorry, guests can't build structures.", (byte)3);
/*  212 */       return false;
/*      */     } 
/*  214 */     if (tool == null) {
/*      */       
/*  216 */       performer.getCommunicator().sendNormalServerMessage("You need a proper building tool activated to do that.", (byte)3);
/*      */       
/*  218 */       return false;
/*      */     } 
/*      */     
/*  221 */     if (!isCorrectToolForPlanning(performer, tool.getTemplateId())) {
/*      */       
/*  223 */       performer.getCommunicator().sendNormalServerMessage("You need a proper building tool activated to do that.", (byte)3);
/*      */       
/*  225 */       return false;
/*      */     } 
/*  227 */     if (tileBordersToFence(tilex, tiley, 0, performer.isOnSurface())) {
/*      */       
/*  229 */       performer.getCommunicator().sendNormalServerMessage("A fence already exists there.", (byte)3);
/*  230 */       return false;
/*      */     } 
/*  232 */     if (Terraforming.isTileModBlocked(performer, tilex, tiley, true)) {
/*  233 */       return false;
/*      */     }
/*  235 */     EndGameItem alt = EndGameItems.getEvilAltar();
/*  236 */     if (alt != null) {
/*      */       
/*  238 */       int maxnorth = Zones.safeTileY(tiley - 20);
/*  239 */       int maxsouth = Zones.safeTileY(tiley + 20);
/*  240 */       int maxeast = Zones.safeTileX(tilex - 20);
/*  241 */       int maxwest = Zones.safeTileX(tilex + 20);
/*  242 */       if (alt.getItem() != null)
/*      */       {
/*  244 */         if ((int)alt.getItem().getPosX() >> 2 < maxwest && (int)alt.getItem().getPosX() >> 2 > maxeast && 
/*  245 */           (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth) {
/*      */           
/*  247 */           performer.getCommunicator().sendSafeServerMessage("You cannot build here, since this is holy ground.", (byte)3);
/*      */           
/*  249 */           return false;
/*      */         } 
/*      */       }
/*      */     } 
/*  253 */     alt = EndGameItems.getGoodAltar();
/*  254 */     if (alt != null) {
/*      */       
/*  256 */       int maxnorth = Zones.safeTileY(tiley - 20);
/*  257 */       int maxsouth = Zones.safeTileY(tiley + 20);
/*  258 */       int maxeast = Zones.safeTileX(tilex - 20);
/*  259 */       int maxwest = Zones.safeTileX(tilex + 20);
/*  260 */       if (alt.getItem() != null)
/*      */       {
/*  262 */         if ((int)alt.getItem().getPosX() >> 2 < maxwest && (int)alt.getItem().getPosX() >> 2 > maxeast && 
/*  263 */           (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth) {
/*      */           
/*  265 */           performer.getCommunicator().sendSafeServerMessage("You cannot build here, since this is holy ground.", (byte)3);
/*      */           
/*  267 */           return false;
/*      */         } 
/*      */       }
/*      */     } 
/*  271 */     if (Features.Feature.CAVE_DWELLINGS.isEnabled() && !performer.isOnSurface()) {
/*      */       
/*  273 */       if (!Tiles.isReinforcedFloor(type) && !Tiles.isRoadType(type)) {
/*      */         
/*  275 */         performer.getCommunicator().sendSafeServerMessage("You cannot build here, you need to reinforce the floor first.", (byte)3);
/*      */         
/*  277 */         return false;
/*      */       } 
/*      */       
/*  280 */       if (needSurroundingTilesFloors(performer, tilex, tiley)) {
/*      */         
/*  282 */         performer.getCommunicator().sendSafeServerMessage("You cannot build here, there must be a gap around the building.", (byte)3);
/*      */         
/*  284 */         return false;
/*      */       } 
/*      */       
/*  287 */       for (int x = 0; x <= 1; x++) {
/*      */         
/*  289 */         for (int y = 0; y <= 1; y++) {
/*      */           
/*  291 */           int theTtile = Server.caveMesh.getTile(tilex + x, tiley + y);
/*  292 */           short ceil = (short)(Tiles.decodeData(theTtile) & 0xFF);
/*  293 */           if (ceil < 30)
/*      */           {
/*  295 */             performer.getCommunicator().sendNormalServerMessage("The ceiling is too close.");
/*  296 */             return false;
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*  301 */     } else if (performer.isOnSurface() && !Terraforming.isBuildTile(type)) {
/*      */       
/*  303 */       performer.getCommunicator().sendSafeServerMessage("You cannot build here, you need to prepare the ground first.", (byte)3);
/*      */       
/*  305 */       return false;
/*      */     } 
/*  307 */     if (!Terraforming.isFlat(tilex, tiley, performer.isOnSurface(), 0)) {
/*      */       
/*  309 */       performer.getCommunicator().sendNormalServerMessage("The ground is not flat enough.", (byte)3);
/*  310 */       return false;
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
/*  332 */     if (Terraforming.isCornerUnderWater(tilex, tiley, performer.isOnSurface())) {
/*      */       
/*  334 */       performer.getCommunicator().sendNormalServerMessage("You can't build on water.", (byte)3);
/*  335 */       return false;
/*      */     } 
/*      */     
/*  338 */     if (!Zones.isOnPvPServer(tilex, tiley)) {
/*      */       
/*  340 */       VolaTile vtile = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/*      */ 
/*      */ 
/*      */       
/*  344 */       if (vtile == null || vtile.getVillage() == null)
/*      */       {
/*  346 */         if (Tiles.isRoadType(type) && MethodsHighways.onHighway(tilex, tiley, performer.isOnSurface())) {
/*      */           
/*  348 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to build structures on highways outside of settlements.", (byte)3);
/*      */           
/*  350 */           return false;
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
/*      */     
/*  367 */     if (!Methods.isActionAllowed(performer, (short)56, tilex, tiley))
/*      */     {
/*  369 */       return false;
/*      */     }
/*      */     
/*  372 */     if (wouldBuildOnOutsideItem(tilex, tiley, performer.isOnSurface())) {
/*      */       
/*  374 */       performer.getCommunicator().sendNormalServerMessage("An item is in the way.");
/*  375 */       return false;
/*      */     } 
/*  377 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean expandHouseTile(Creature performer, Item tool, int tilex, int tiley, int tile, float counter) {
/*  387 */     List<Structure> nearStructures = null;
/*  388 */     Structure plannedStructure = null;
/*  389 */     if (!canPlanStructureAt(performer, tool, tilex, tiley, tile)) {
/*  390 */       return true;
/*      */     }
/*  392 */     VolaTile newTile = Zones.getOrCreateTile(tilex, tiley, performer.isOnSurface());
/*  393 */     if (newTile.getStructure() != null) {
/*      */       
/*  395 */       performer.getCommunicator().sendNormalServerMessage("There is already a building there.");
/*  396 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  400 */     nearStructures = getStructuresNear(tilex, tiley, performer.isOnSurface());
/*  401 */     if (nearStructures.isEmpty()) {
/*      */       
/*  403 */       performer.getCommunicator().sendNormalServerMessage("There is no building near that can be expanded upon.");
/*  404 */       return true;
/*      */     } 
/*      */     
/*  407 */     if (nearStructures.size() > 1) {
/*      */       
/*  409 */       performer.getCommunicator().sendNormalServerMessage("You cannot expand in that direction. Too many buildings are close.");
/*      */       
/*  411 */       return true;
/*      */     } 
/*      */     
/*  414 */     Structure structureToExpand = nearStructures.get(0);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  419 */       plannedStructure = performer.getStructure();
/*  420 */       if (plannedStructure != null && plannedStructure.getWurmId() != structureToExpand.getWurmId())
/*      */       {
/*  422 */         performer
/*  423 */           .getCommunicator()
/*  424 */           .sendNormalServerMessage("You already have another building under construction. Finish that one before trying to expand another.");
/*      */         
/*  426 */         return true;
/*      */       }
/*      */     
/*  429 */     } catch (NoSuchStructureException noSuchStructureException) {}
/*      */ 
/*      */ 
/*      */     
/*  433 */     if (Features.Feature.CAVE_DWELLINGS.isEnabled() && !performer.isOnSurface()) {
/*      */       
/*  435 */       byte type = Tiles.decodeType(tile);
/*  436 */       if (!Tiles.isReinforcedFloor(type) && !Tiles.isRoadType(type)) {
/*      */         
/*  438 */         performer.getCommunicator().sendSafeServerMessage("You cannot build here, you need to reinforce the floor first.", (byte)3);
/*      */         
/*  440 */         return false;
/*      */       } 
/*      */       
/*  443 */       if (needSurroundingTilesFloors(performer, tilex, tiley)) {
/*      */         
/*  445 */         performer.getCommunicator().sendSafeServerMessage("You cannot build here, there must be a gap around the building.", (byte)3);
/*      */         
/*  447 */         return false;
/*      */       } 
/*      */       
/*  450 */       for (int x = 0; x <= 1; x++) {
/*      */         
/*  452 */         for (int y = 0; y <= 1; y++) {
/*      */           
/*  454 */           int theTtile = Server.caveMesh.getTile(tilex + x, tiley + y);
/*  455 */           short ceil = (short)(Tiles.decodeData(theTtile) & 0xFF);
/*  456 */           if (ceil < 30)
/*      */           {
/*  458 */             performer.getCommunicator().sendNormalServerMessage("The ceiling is too close.");
/*  459 */             return false;
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*  464 */     } else if (performer.isOnSurface() && !Terraforming.isBuildTile(Tiles.decodeType(tile))) {
/*      */       
/*  466 */       performer.getCommunicator().sendSafeServerMessage("You cannot build here, you need to prepare the ground first.", (byte)3);
/*      */       
/*  468 */       return false;
/*      */     } 
/*  470 */     if (!hasEnoughSkillToExpandStructure(performer, tilex, tiley, structureToExpand))
/*      */     {
/*      */       
/*  473 */       return true;
/*      */     }
/*      */     
/*  476 */     if (!mayModifyStructure(performer, structureToExpand, newTile, (short)116)) {
/*      */       
/*  478 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to expand " + structureToExpand
/*  479 */           .getName() + ".");
/*  480 */       return true;
/*      */     } 
/*      */     
/*  483 */     if (!tileisNextToStructure(structureToExpand.getStructureTiles(), tilex, tiley)) {
/*      */       
/*  485 */       performer.getCommunicator().sendNormalServerMessage("That is not adjacent to any building.");
/*      */       
/*  487 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  492 */       Structure.expandStructureToTile(structureToExpand, newTile);
/*  493 */       structureToExpand.addNewBuildTile(newTile.tilex, newTile.tiley, newTile.getLayer());
/*  494 */       newTile.addBuildMarker(structureToExpand);
/*  495 */       structureToExpand.addMissingWallPlans(newTile);
/*  496 */       Structure.adjustWallsAroundAddedStructureTile(structureToExpand, tilex, tiley);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  501 */       newTile.addStructure(structureToExpand);
/*  502 */       structureToExpand.save();
/*      */     }
/*  504 */     catch (NoSuchZoneException nsz) {
/*      */       
/*  506 */       performer.getCommunicator().sendNormalServerMessage("A strong wind blows your markers away.");
/*      */       
/*  508 */       return true;
/*      */     }
/*  510 */     catch (IOException e) {
/*      */ 
/*      */       
/*  513 */       logger.log(Level.WARNING, "Exception when trying to save structure after expansion: ", e);
/*      */     } 
/*      */ 
/*      */     
/*  517 */     structureToExpand.updateStructureFinishFlag();
/*      */     
/*  519 */     performer.getCommunicator().sendNormalServerMessage("You successfully expand your building.");
/*      */     
/*  521 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean hasEnoughSkillToExpandStructure(Creature performer, int tilex, int tiley, Structure plannedStructure) {
/*  527 */     Skill carpentry = null;
/*      */     
/*      */     try {
/*  530 */       carpentry = performer.getSkills().getSkill(1005);
/*      */     }
/*  532 */     catch (NoSuchSkillException nss) {
/*      */       
/*  534 */       performer.getCommunicator().sendNormalServerMessage("You have no idea of how you would build a house.");
/*      */       
/*  536 */       return false;
/*      */     } 
/*      */     
/*  539 */     if (carpentry == null) {
/*      */       
/*  541 */       performer.getCommunicator().sendNormalServerMessage("You have no idea of how you would build a house.");
/*      */       
/*  543 */       return false;
/*      */     } 
/*  545 */     int limit = 5;
/*  546 */     if (plannedStructure != null) {
/*      */       
/*  548 */       limit = plannedStructure.getLimitFor(tilex, tiley, performer.isOnSurface(), true);
/*      */     }
/*      */     else {
/*      */       
/*  552 */       limit = 5;
/*      */     } 
/*      */     
/*  555 */     if (limit == 0) {
/*      */       
/*  557 */       performer.getCommunicator().sendNormalServerMessage("The house seems to have no walls. Please replan.");
/*  558 */       return false;
/*      */     } 
/*      */     
/*  561 */     if (limit > carpentry.getKnowledge(0.0D)) {
/*      */       
/*  563 */       performer.getCommunicator().sendNormalServerMessage("You are not skilled enough in Carpentry to build this size of structure.");
/*      */       
/*  565 */       return false;
/*      */     } 
/*      */     
/*  568 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean hasEnoughSkillToContractStructure(Creature performer, int tilex, int tiley, Structure plannedStructure) {
/*  574 */     Skill carpentry = null;
/*      */     
/*      */     try {
/*  577 */       carpentry = performer.getSkills().getSkill(1005);
/*      */     }
/*  579 */     catch (NoSuchSkillException nss) {
/*      */       
/*  581 */       performer.getCommunicator().sendNormalServerMessage("You have no idea of how you would modify a house.");
/*      */       
/*  583 */       return false;
/*      */     } 
/*      */     
/*  586 */     if (carpentry == null) {
/*      */       
/*  588 */       performer.getCommunicator().sendNormalServerMessage("You have no idea of how you would modify a house.");
/*      */       
/*  590 */       return false;
/*      */     } 
/*  592 */     int limit = 5;
/*  593 */     if (plannedStructure.getSize() > 1) {
/*      */       
/*  595 */       limit = plannedStructure.getLimitFor(tilex, tiley, performer.isOnSurface(), false);
/*      */     }
/*      */     else {
/*      */       
/*  599 */       limit = 5;
/*      */     } 
/*      */     
/*  602 */     if (limit == 0) {
/*      */       
/*  604 */       performer.getCommunicator().sendNormalServerMessage("The house seems to have no walls. Please replan.");
/*  605 */       return false;
/*      */     } 
/*      */     
/*  608 */     if (limit > carpentry.getKnowledge(0.0D)) {
/*      */       
/*  610 */       performer.getCommunicator().sendNormalServerMessage("You are not skilled enough in Carpentry to modify this structure in that way.");
/*      */       
/*  612 */       return false;
/*      */     } 
/*      */     
/*  615 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean buildPlan(Creature performer, Item tool, int tilex, int tiley, int tile, float counter) {
/*  621 */     boolean done = true;
/*      */     
/*  623 */     Structure planningStructure = null;
/*      */     
/*  625 */     if (!canPlanStructureAt(performer, tool, tilex, tiley, tile))
/*      */     {
/*      */       
/*  628 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  633 */       planningStructure = performer.getStructure();
/*      */     }
/*  635 */     catch (NoSuchStructureException noSuchStructureException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  641 */     if (planningStructure != null && (planningStructure.isFinalFinished() || 
/*  642 */       System.currentTimeMillis() - planningStructure.getCreationDate() > 345600000L)) {
/*      */       
/*  644 */       performer.setStructure(null);
/*  645 */       logger.log(Level.INFO, performer.getName() + " just made another structure possible.");
/*  646 */       planningStructure = null;
/*      */     } 
/*  648 */     if (getStructureAt(tilex, tiley, performer.isOnSurface()) != null) {
/*      */       
/*  650 */       performer.getCommunicator().sendNormalServerMessage("You cannot build a building inside a building.");
/*      */       
/*  652 */       return true;
/*      */     } 
/*  654 */     if (planningStructure == null) {
/*      */       
/*  656 */       List<Structure> nearStructures = getStructuresNear(tilex, tiley, performer.isOnSurface());
/*  657 */       if (!nearStructures.isEmpty())
/*      */       {
/*  659 */         performer.getCommunicator().sendNormalServerMessage("You cannot build a building next to another building.");
/*      */         
/*  661 */         return true;
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*  667 */     else if (hasOtherStructureNear(planningStructure, tilex, tiley, performer.isOnSurface())) {
/*      */       
/*  669 */       performer.getCommunicator().sendNormalServerMessage("You cannot build a building next to another building.");
/*      */       
/*  671 */       return true;
/*      */     } 
/*      */     
/*  674 */     VolaTile t = Zones.getOrCreateTile(tilex, tiley, performer.isOnSurface());
/*  675 */     if (planningStructure == null) {
/*      */       
/*  677 */       if (!hasEnoughSkillToExpandStructure(performer, tilex, tiley, planningStructure)) {
/*  678 */         return true;
/*      */       }
/*  680 */       performer.addStructureTile(t, (byte)0);
/*  681 */       if (t.getVillage() == null)
/*      */       {
/*  683 */         if (t.getKingdom() == 0) {
/*  684 */           performer.getCommunicator()
/*  685 */             .sendAlertServerMessage("You are planning a structure outside a kingdom, and in no village. This is extremely risky, and the structure will probably be pillaged and looted by other players.");
/*      */         } else {
/*      */           
/*  688 */           performer.getCommunicator()
/*  689 */             .sendAlertServerMessage("You are planning a structure outside any known village. This is very risky, and the structure may very well be pillaged and looted by other players.");
/*      */         } 
/*      */       }
/*  692 */       return true;
/*      */     } 
/*      */     
/*  695 */     if (planningStructure.isTypeBridge()) {
/*      */       
/*  697 */       performer.getCommunicator().sendNormalServerMessage("You cannot design a house as your mind keeps reverting back to the bridge \"" + planningStructure
/*      */           
/*  699 */           .getName() + "\" that you are currently constructing.");
/*  700 */       return true;
/*      */     } 
/*      */     
/*  703 */     if (hasOtherStructureNear(planningStructure, tilex, tiley, performer.isOnSurface())) {
/*      */       
/*  705 */       performer.getCommunicator().sendNormalServerMessage("You need space to build the walls. Another building is too close.");
/*      */       
/*  707 */       return true;
/*      */     } 
/*      */     
/*  710 */     if (wouldBuildOnStructure(planningStructure, tilex, tiley, performer.isOnSurface())) {
/*      */       
/*  712 */       performer.getCommunicator().sendNormalServerMessage("There is already a building there.");
/*      */       
/*  714 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  718 */     if (planningStructure.contains(tilex, tiley)) {
/*      */       
/*  720 */       performer.getCommunicator().sendNormalServerMessage("You already plan to build there.");
/*  721 */       return true;
/*      */     } 
/*      */     
/*  724 */     if (!tileisNextToStructure(planningStructure.getStructureTiles(), tilex, tiley)) {
/*      */ 
/*      */       
/*  727 */       if (planningStructure.isFinalized()) {
/*  728 */         performer.getCommunicator().sendNormalServerMessage("You cannot design a new house as your mind keeps reverting back to the house \"" + planningStructure
/*      */             
/*  730 */             .getName() + "\" that you are currently constructing.");
/*      */       } else {
/*  732 */         performer.getCommunicator().sendNormalServerMessage("You cannot design a new house as your mind keeps reverting back to the house that you are currently in the process of planning.");
/*      */       } 
/*      */       
/*  735 */       return true;
/*      */     } 
/*      */     
/*  738 */     if (planningStructure.isFinalized()) {
/*      */       
/*  740 */       performer.getCommunicator().sendNormalServerMessage("Your current planning phase is already complete, use \"Add to building\" to expand it.");
/*      */       
/*  742 */       return true;
/*      */     } 
/*  744 */     if (!hasEnoughSkillToExpandStructure(performer, tilex, tiley, planningStructure)) {
/*  745 */       return true;
/*      */     }
/*  747 */     performer.addStructureTile(t, (byte)0);
/*  748 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean wouldBuildOnOutsideItem(int tilex, int tiley, boolean surfaced) {
/*  753 */     VolaTile tile = Zones.getTileOrNull(tilex, tiley, surfaced);
/*  754 */     if (tile == null) {
/*  755 */       return false;
/*      */     }
/*      */     
/*  758 */     Item[] items = tile.getItems();
/*  759 */     for (int x = 0; x < items.length; x++) {
/*      */       
/*  761 */       if (items[x].isOutsideOnly()) {
/*  762 */         return true;
/*      */       }
/*      */     } 
/*  765 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean buildPlanRemove(Creature performer, int tilex, int tiley, int tile, float counter) {
/*  771 */     boolean done = true;
/*      */     
/*      */     try {
/*  774 */       Structure structure = performer.getStructure();
/*  775 */       if (structure.isFinalized())
/*  776 */         return true; 
/*  777 */       if (structure.getWurmId() != performer.getBuildingId()) {
/*  778 */         performer.getCommunicator().sendNormalServerMessage("You are not planning this house.");
/*  779 */       } else if (structure.contains(tilex, tiley)) {
/*      */         
/*  781 */         if (!hasEnoughSkillToContractStructure(performer, tilex, tiley, structure))
/*      */         {
/*      */           
/*  784 */           return true;
/*      */         }
/*  786 */         if (!structure.removeTileFromPlannedStructure(performer, tilex, tiley))
/*      */         {
/*  788 */           performer.getCommunicator().sendNormalServerMessage("You can't divide the house in different parts.");
/*      */         }
/*      */       } else {
/*      */         
/*  792 */         performer.getCommunicator().sendNormalServerMessage("You don't even plan to build there!");
/*      */       } 
/*  794 */     } catch (NoSuchStructureException nss) {
/*      */       
/*  796 */       performer.getCommunicator().sendNormalServerMessage("You don't even plan to build there!");
/*      */     } 
/*  798 */     return true;
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
/*      */   public static VolaTile findFenceStart(VolaTile a, VolaTile b) {
/*  810 */     int tileax = a.getTileX();
/*  811 */     int tileay = a.getTileY();
/*  812 */     int tilebx = b.getTileX();
/*  813 */     int tileby = b.getTileY();
/*      */     
/*  815 */     if (tileay < tileby)
/*  816 */       return a; 
/*  817 */     if (tileby < tileay)
/*  818 */       return b; 
/*  819 */     if (tileax < tilebx) {
/*  820 */       return a;
/*      */     }
/*  822 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Fence getFenceAtTileBorderOrNull(int tilex, int tiley, Tiles.TileBorderDirection dir, int heightOffset, boolean surfaced) {
/*  828 */     VolaTile tile = null;
/*  829 */     tile = Zones.getTileOrNull(tilex, tiley, surfaced);
/*      */     
/*  831 */     if (tile != null) {
/*      */       
/*  833 */       Fence[] fences = tile.getFences();
/*  834 */       if (fences != null)
/*      */       {
/*  836 */         for (Fence fence : fences) {
/*      */           
/*  838 */           if (fence.getDir() == dir && fence.getHeightOffset() == heightOffset)
/*      */           {
/*  840 */             return fence;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*  845 */     return null;
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
/*      */ 
/*      */   
/*      */   public static final boolean doesTileBorderContainWallOrFence(int x, int y, int heightOffset, Tiles.TileBorderDirection dir, boolean surfaced, boolean ignoreArchs) {
/*  916 */     VolaTile tile = null;
/*  917 */     tile = Zones.getTileOrNull(x, y, surfaced);
/*      */     
/*  919 */     if (tile != null) {
/*      */       
/*  921 */       Fence[] fences = tile.getFences();
/*  922 */       if (fences != null)
/*      */       {
/*  924 */         for (Fence fence : fences) {
/*      */           
/*  926 */           if (fence.getDir() == dir && fence.getHeightOffset() == heightOffset && fence
/*  927 */             .isOnSurface() == surfaced)
/*      */           {
/*  929 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/*  933 */       Wall[] walls = tile.getWalls();
/*  934 */       for (int s = 0; s < walls.length; s++) {
/*      */         
/*  936 */         if (walls[s].getStartX() == x && walls[s].getStartY() == y && walls[s]
/*  937 */           .getHeight() == heightOffset && walls[s].isOnSurface() == surfaced)
/*      */         {
/*  939 */           if ((walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_HORIZ) || (
/*  940 */             !walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_DOWN))
/*      */           {
/*  942 */             if (!ignoreArchs || 
/*  943 */               !walls[s].isArched() || !walls[s].isFinished())
/*      */             {
/*      */ 
/*      */               
/*  947 */               return true; } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*  952 */     if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*      */       
/*  954 */       tile = Zones.getTileOrNull(x, y - 1, surfaced);
/*  955 */       if (tile != null) {
/*      */         
/*  957 */         Wall[] walls = tile.getWalls();
/*  958 */         for (int s = 0; s < walls.length; s++) {
/*      */           
/*  960 */           if (walls[s].getStartX() == x && walls[s].getStartY() == y && walls[s]
/*  961 */             .getHeight() == heightOffset && walls[s].isOnSurface() == surfaced)
/*      */           {
/*  963 */             if ((walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_HORIZ) || (
/*  964 */               !walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_DOWN))
/*      */             {
/*  966 */               if (!ignoreArchs || 
/*  967 */                 !walls[s].isArched() || !walls[s].isFinished())
/*      */               {
/*      */ 
/*      */                 
/*  971 */                 return true;
/*      */               }
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  979 */       tile = Zones.getTileOrNull(x - 1, y, surfaced);
/*  980 */       if (tile != null) {
/*      */         
/*  982 */         Wall[] walls = tile.getWalls();
/*  983 */         for (int s = 0; s < walls.length; s++) {
/*      */           
/*  985 */           if (walls[s].getStartX() == x && walls[s].getStartY() == y && walls[s]
/*  986 */             .isOnSurface() == surfaced)
/*      */           {
/*  988 */             if ((walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_HORIZ && walls[s].getHeight() == heightOffset) || (
/*  989 */               !walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_DOWN && walls[s]
/*  990 */               .getHeight() == heightOffset))
/*      */             {
/*  992 */               if (!ignoreArchs || 
/*  993 */                 !walls[s].isArched() || !walls[s].isFinished())
/*      */               {
/*      */ 
/*      */                 
/*  997 */                 return true; } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1003 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean doesTileBorderContainUnfinishedWallOrFenceBelow(int x, int y, int heightOffset, Tiles.TileBorderDirection dir, boolean surfaced, boolean ignoreArchs) {
/* 1009 */     VolaTile tile = null;
/* 1010 */     tile = Zones.getTileOrNull(x, y, surfaced);
/*      */     
/* 1012 */     if (tile != null) {
/*      */       
/* 1014 */       Fence[] fences = tile.getFences();
/* 1015 */       if (fences != null)
/*      */       {
/* 1017 */         for (Fence fence : fences) {
/*      */           
/* 1019 */           if (fence.getDir() == dir && fence.getHeightOffset() == heightOffset - 30)
/*      */           {
/* 1021 */             if (!fence.isFinished())
/* 1022 */               return true; 
/*      */           }
/*      */         } 
/*      */       }
/* 1026 */       Wall[] walls = tile.getWalls();
/* 1027 */       for (int s = 0; s < walls.length; s++) {
/*      */         
/* 1029 */         if (walls[s].getStartX() == x && walls[s].getStartY() == y && walls[s].getHeight() == heightOffset - 30)
/*      */         {
/* 1031 */           if ((walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_HORIZ) || (
/* 1032 */             !walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_DOWN))
/*      */           {
/* 1034 */             if (!walls[s].isFinished())
/* 1035 */               return true; 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/* 1040 */     if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*      */       
/* 1042 */       tile = Zones.getTileOrNull(x, y - 1, surfaced);
/* 1043 */       if (tile != null) {
/*      */         
/* 1045 */         Wall[] walls = tile.getWalls();
/* 1046 */         for (int s = 0; s < walls.length; s++) {
/*      */           
/* 1048 */           if (walls[s].getStartX() == x && walls[s].getStartY() == y && walls[s].getHeight() == heightOffset - 30)
/*      */           {
/* 1050 */             if ((walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_HORIZ) || (
/* 1051 */               !walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_DOWN))
/*      */             {
/* 1053 */               if (!walls[s].isFinished()) {
/* 1054 */                 return true;
/*      */               }
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1062 */       tile = Zones.getTileOrNull(x - 1, y, surfaced);
/* 1063 */       if (tile != null) {
/*      */         
/* 1065 */         Wall[] walls = tile.getWalls();
/* 1066 */         for (int s = 0; s < walls.length; s++) {
/*      */           
/* 1068 */           if (walls[s].getStartX() == x && walls[s].getStartY() == y)
/*      */           {
/* 1070 */             if ((walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_HORIZ && walls[s].getHeight() == heightOffset - 30) || (
/* 1071 */               !walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_DOWN && walls[s]
/* 1072 */               .getHeight() == heightOffset - 30))
/*      */             {
/* 1074 */               if (!walls[s].isFinished())
/* 1075 */                 return true; 
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1081 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean tileCornerBordersToCaveExit(int tilex, int tiley) {
/* 1086 */     for (int x = -1; x <= 0; x++) {
/*      */       
/* 1088 */       for (int y = -1; y <= 0; y++) {
/*      */ 
/*      */         
/* 1091 */         if (Tiles.decodeType(Server.caveMesh.getTile(Zones.safeTileX(tilex + x), Zones.safeTileY(tiley + y))) == Tiles.Tile.TILE_CAVE_EXIT.id)
/* 1092 */           return true; 
/*      */       } 
/*      */     } 
/* 1095 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isFirstFenceTileOk(Creature performer, VolaTile vtile, int tile, int tilex, int tiley, int heightOffset, boolean horizontal) {
/* 1101 */     if (vtile == null) {
/*      */       
/* 1103 */       performer.getCommunicator().sendAlertServerMessage("You cannot place a fence there.");
/* 1104 */       return false;
/*      */     } 
/* 1106 */     MeshIO mesh = performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh;
/* 1107 */     int x = 0;
/* 1108 */     int y = 0;
/* 1109 */     if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize) {
/*      */       
/* 1111 */       performer.getCommunicator().sendAlertServerMessage("The water is too deep.");
/* 1112 */       return false;
/*      */     } 
/* 1114 */     if (Math.abs(performer.getPosX() - (tilex + x << 2)) > 4.0F || Math.abs(performer.getPosX() - (tilex << 2)) > 4.0F || 
/* 1115 */       Math.abs(performer.getPosY() - (tiley + y << 2)) > 4.0F || Math.abs(performer.getPosY() - (tiley << 2)) > 4.0F) {
/*      */       
/* 1117 */       performer.getCommunicator().sendAlertServerMessage("You are too far from the end.");
/* 1118 */       return false;
/*      */     } 
/* 1120 */     if (Terraforming.isTileModBlocked(performer, tilex, tiley, performer.isOnSurface())) {
/* 1121 */       return false;
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
/* 1144 */     if (horizontal) {
/* 1145 */       x = 1;
/*      */     } else {
/* 1147 */       y = 1;
/* 1148 */     }  if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize) {
/*      */       
/* 1150 */       performer.getCommunicator().sendAlertServerMessage("The water is too deep.");
/* 1151 */       return false;
/*      */     } 
/* 1153 */     if (Math.abs(performer.getPosX() - (tilex + x << 2)) > 4.0F || Math.abs(performer.getPosX() - (tilex << 2)) > 4.0F || 
/* 1154 */       Math.abs(performer.getPosY() - (tiley + y << 2)) > 4.0F || Math.abs(performer.getPosY() - (tiley << 2)) > 4.0F) {
/*      */       
/* 1156 */       performer.getCommunicator().sendAlertServerMessage("You are too far from the end.");
/* 1157 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1161 */     if (Servers.localServer.entryServer)
/*      */     {
/* 1163 */       if (tileCornerBordersToCaveExit(tilex + x, tiley + y)) {
/*      */         
/* 1165 */         performer
/* 1166 */           .getCommunicator()
/* 1167 */           .sendAlertServerMessage("Regulations in these lands require you to build further from the cave entrance. Use a mine door to protect it instead.");
/*      */         
/* 1169 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1178 */     short h = Tiles.decodeHeight(mesh.getTile(tilex, tiley));
/* 1179 */     if (h <= -15) {
/*      */       
/* 1181 */       performer.getCommunicator().sendAlertServerMessage("The water is too deep.");
/* 1182 */       return false;
/*      */     } 
/* 1184 */     h = Tiles.decodeHeight(mesh.getTile(tilex + x, tiley + y));
/* 1185 */     if (h <= -15) {
/*      */       
/* 1187 */       performer.getCommunicator().sendAlertServerMessage("The water is too deep.");
/* 1188 */       return false;
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
/* 1209 */     Fence[] fences = vtile.getFencesForLevel(Math.max(0, heightOffset / 30));
/* 1210 */     if (fences.length > 1)
/*      */     {
/* 1212 */       if (fences[0] != null && fences[1] != null) {
/*      */         
/* 1214 */         performer.getCommunicator().sendAlertServerMessage("You cannot place a fence there. Fences already exist.");
/* 1215 */         return false;
/*      */       } 
/*      */     }
/* 1218 */     EndGameItem alt = EndGameItems.getEvilAltar();
/* 1219 */     if (alt != null) {
/*      */       
/* 1221 */       int maxnorth = Math.max(0, tiley - 20);
/* 1222 */       int maxsouth = Math.min(Zones.worldTileSizeY, tiley + 20);
/* 1223 */       int maxeast = Math.max(0, tilex - 20);
/* 1224 */       int maxwest = Math.min(Zones.worldTileSizeX, tilex + 20);
/* 1225 */       if (alt.getItem() != null)
/*      */       {
/* 1227 */         if ((int)alt.getItem().getPosX() >> 2 < maxwest && (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 1228 */           (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth) {
/*      */           
/* 1230 */           performer.getCommunicator().sendSafeServerMessage("You cannot place a fence here, since this is holy ground.");
/*      */           
/* 1232 */           return false;
/*      */         } 
/*      */       }
/*      */     } 
/* 1236 */     alt = EndGameItems.getGoodAltar();
/* 1237 */     if (alt != null) {
/*      */       
/* 1239 */       int maxnorth = Math.max(0, tiley - 20);
/* 1240 */       int maxsouth = Math.min(Zones.worldTileSizeY, tiley + 20);
/* 1241 */       int maxeast = Math.max(0, tilex - 20);
/* 1242 */       int maxwest = Math.min(Zones.worldTileSizeX, tilex + 20);
/* 1243 */       if (alt.getItem() != null)
/*      */       {
/* 1245 */         if ((int)alt.getItem().getPosX() >> 2 < maxwest && (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 1246 */           (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth) {
/*      */           
/* 1248 */           performer.getCommunicator().sendSafeServerMessage("You cannot place a fence here, since this is holy ground.");
/*      */           
/* 1250 */           return false;
/*      */         } 
/*      */       }
/*      */     } 
/* 1254 */     return true;
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
/*      */   static boolean startFenceSection(Creature performer, Item source, Tiles.TileBorderDirection dir, int tilex, int tiley, boolean onSurface, int heightOffset, long borderId, int action, float counter, boolean instaFinish) {
/* 1268 */     VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, onSurface);
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
/* 1286 */     Structure structure = getStructureOrNullAtTileBorder(tilex, tiley, dir, onSurface);
/*      */ 
/*      */     
/* 1289 */     if (heightOffset > 0)
/*      */     {
/* 1291 */       if (structure == null) {
/*      */         
/* 1293 */         performer.getCommunicator().sendNormalServerMessage("The structural integrity of the building is at risk.");
/* 1294 */         logger.log(Level.WARNING, "Structure not found while trying to add a wall at [" + tilex + "," + tiley + "]");
/* 1295 */         return true;
/*      */       } 
/*      */     }
/* 1298 */     MeshIO mesh = onSurface ? Server.surfaceMesh : Server.caveMesh;
/* 1299 */     int tile = mesh.getTile(tilex, tiley);
/* 1300 */     if (!isFirstFenceTileOk(performer, vtile, tile, tilex, tiley, heightOffset, (dir == Tiles.TileBorderDirection.DIR_HORIZ))) {
/* 1301 */       return true;
/*      */     }
/* 1303 */     boolean horizontal = (dir == Tiles.TileBorderDirection.DIR_HORIZ);
/*      */     
/*      */     try {
/* 1306 */       Zone zone = Zones.getZone(tilex, tiley, onSurface);
/* 1307 */       DbFence dbFence = new DbFence(Fence.getFencePlanType(action), tilex, tiley, heightOffset, 1.0F, dir, zone.getId(), onSurface ? 0 : -1);
/*      */       
/* 1309 */       Skill primskill = null;
/* 1310 */       primskill = Fence.getSkillNeededForFence(performer, (Fence)dbFence);
/* 1311 */       if (primskill == null) {
/*      */         
/* 1313 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate skill needed for this " + dbFence
/* 1314 */             .getName() + ". You cannot progress.");
/* 1315 */         logger.log(Level.WARNING, "Failed to find out what skill was needed for " + dbFence
/* 1316 */             .getName() + " at :" + dbFence.getTileX() + ", " + dbFence
/* 1317 */             .getTileY());
/* 1318 */         return true;
/*      */       } 
/* 1320 */       double knowledge = primskill.getKnowledge(0.0D);
/* 1321 */       short height = Tiles.decodeHeight(tile);
/* 1322 */       int nexttile = mesh.getTile(tilex + 1, tiley);
/* 1323 */       if (!horizontal)
/* 1324 */         nexttile = mesh.getTile(tilex, tiley + 1); 
/* 1325 */       VolaTile lastT = Zones.getTileOrNull(tilex - 1, tiley, onSurface);
/* 1326 */       int lasttile = mesh.getTile(tilex - 1, tiley);
/* 1327 */       if (horizontal) {
/*      */         
/* 1329 */         lastT = Zones.getTileOrNull(tilex, tiley - 1, onSurface);
/* 1330 */         lasttile = mesh.getTile(tilex, tiley - 1);
/*      */       } 
/* 1332 */       if (!dbFence.isOnPvPServer() && performer.getPower() < 2)
/*      */       {
/*      */ 
/*      */         
/* 1336 */         if (vtile.getVillage() == null && (lastT == null || lastT.getVillage() == null))
/*      */         {
/* 1338 */           if (Tiles.isRoadType(lasttile) && 
/* 1339 */             Tiles.isRoadType(tile) && heightOffset == 0) {
/*      */             
/* 1341 */             performer.getCommunicator().sendNormalServerMessage("You are not allowed to build fences on roads outside of settlements.");
/*      */             
/* 1343 */             return true;
/*      */           } 
/*      */         }
/*      */       }
/* 1347 */       if (dbFence.getType() != StructureConstantsEnum.FENCE_PLAN_STONEWALL || (
/* 1348 */         Tiles.decodeType(tile) != Tiles.Tile.TILE_HOLE.id && Tiles.decodeType(lasttile) != Tiles.Tile.TILE_HOLE.id)) {
/*      */         
/* 1350 */         short secondHeight = Tiles.decodeHeight(nexttile);
/* 1351 */         if (dbFence.getType() != StructureConstantsEnum.FENCE_PLAN_PORTCULLIS) {
/*      */           
/* 1353 */           if (Math.abs(secondHeight - height) > Math.sqrt(knowledge) * 3.0D + 10.0D)
/*      */           {
/* 1355 */             performer.getCommunicator().sendAlertServerMessage("You are not skilled enough to build in such steep slopes.");
/*      */             
/* 1357 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1362 */           int maxSlope = 10;
/* 1363 */           int slope = Math.abs(secondHeight - height);
/* 1364 */           if (slope > 10) {
/*      */ 
/*      */             
/* 1367 */             String message = StringUtil.format("You are not allowed to build this type of fence in a slope of: %d. The slope must be %d or less.", new Object[] {
/*      */                   
/* 1369 */                   Integer.valueOf(slope), Integer.valueOf(10) });
/* 1370 */             performer.getCommunicator().sendAlertServerMessage(message);
/* 1371 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1376 */       if (structure != null && 
/* 1377 */         FloorBehaviour.getRequiredBuildSkillForFloorLevel(heightOffset / 30, false) > knowledge) {
/*      */         
/* 1379 */         performer.getCommunicator().sendAlertServerMessage("You are not skilled enough to build at this height.");
/*      */         
/* 1381 */         return true;
/*      */       } 
/*      */       
/*      */       try {
/* 1385 */         if (buildFirstFence(performer, source, (Fence)dbFence, counter, action, instaFinish))
/*      */         {
/*      */ 
/*      */           
/* 1389 */           zone.addFence((Fence)dbFence);
/* 1390 */           if (instaFinish) {
/*      */             
/* 1392 */             if (dbFence.isDoor()) {
/*      */               
/* 1394 */               DbFenceGate dbFenceGate = new DbFenceGate((Fence)dbFence);
/* 1395 */               dbFenceGate.addToTiles();
/* 1396 */               vtile = dbFenceGate.getInnerTile();
/* 1397 */               Village village = vtile.getVillage();
/* 1398 */               if (village != null)
/*      */               {
/* 1400 */                 village.addGate((FenceGate)dbFenceGate);
/*      */               }
/*      */               else
/*      */               {
/* 1404 */                 vtile = dbFenceGate.getOuterTile();
/* 1405 */                 village = vtile.getVillage();
/* 1406 */                 if (village != null)
/*      */                 {
/* 1408 */                   village.addGate((FenceGate)dbFenceGate);
/*      */                 }
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 1415 */             performer.getCommunicator().sendAddFenceToCreationWindow((Fence)dbFence, borderId);
/*      */           } 
/* 1417 */           performer.getCommunicator().sendActionResult(true);
/* 1418 */           return true;
/*      */         }
/*      */       
/* 1421 */       } catch (FailedException fe) {
/*      */         
/* 1423 */         performer.getCommunicator().sendActionResult(false);
/* 1424 */         return true;
/*      */       } 
/* 1426 */       return false;
/*      */     }
/* 1428 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 1430 */       logger.log(Level.INFO, performer.getName() + ": " + nsz.getMessage(), (Throwable)nsz);
/*      */       
/* 1432 */       return true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void instaDestroyFence(Creature performer, Fence fence) {
/*      */     try {
/* 1439 */       Zone zone = Zones.getZone(fence.getZoneId());
/* 1440 */       zone.removeFence(fence);
/* 1441 */       fence.delete();
/*      */     }
/* 1443 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 1445 */       logger.log(Level.WARNING, "Fence in nonexistant zone? " + performer.getName() + " " + nsz.getMessage(), (Throwable)nsz);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeFencePlan(Creature performer, Item source, Fence fence, float counter, int action, Action act) {
/* 1452 */     boolean toReturn = false;
/* 1453 */     int time = 300;
/* 1454 */     if (fence.isFinished())
/*      */     {
/* 1456 */       return true;
/*      */     }
/* 1458 */     if (counter == 1.0F) {
/*      */       
/* 1460 */       if (fence.getDamageModifierForItem(source, false) <= 0.0F) {
/*      */         
/* 1462 */         performer.getCommunicator().sendNormalServerMessage("You will not do any damage to the " + fence
/* 1463 */             .getName() + " with that.");
/* 1464 */         return true;
/*      */       } 
/*      */       
/* 1467 */       VolaTile tile = Zones.getTileOrNull(fence.getTileX(), fence.getTileY(), fence.isOnSurface());
/* 1468 */       if (tile != null) {
/*      */         
/* 1470 */         Structure struct = tile.getStructure();
/* 1471 */         if (struct != null)
/*      */         {
/* 1473 */           if (struct.wouldCreateFlyingStructureIfRemoved((StructureSupport)fence)) {
/*      */             
/* 1475 */             performer.getCommunicator().sendNormalServerMessage("Removing that would cause a collapsing section.");
/*      */             
/* 1477 */             return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1497 */         performer.getCurrentAction().setTimeLeft(time);
/*      */       }
/* 1499 */       catch (NoSuchActionException nsa) {
/*      */         
/* 1501 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/* 1503 */       performer.getCommunicator().sendNormalServerMessage("You start to remove the " + fence.getName() + ".");
/* 1504 */       Server.getInstance().broadCastAction(performer.getName() + " starts to remove a " + fence.getName() + ".", performer, 5);
/*      */       
/* 1506 */       performer.sendActionControl("Removing " + fence.getName(), true, time);
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1512 */         time = performer.getCurrentAction().getTimeLeft();
/* 1513 */         if (act.currentSecond() % 5 == 0) {
/*      */           
/* 1515 */           sendDestroySound(performer, source, (!fence.isStone() && !fence.isIron()));
/*      */           
/* 1517 */           performer.getStatus().modifyStamina(-5000.0F);
/*      */           
/* 1519 */           if (source != null && (
/* 1520 */             !source.isBodyPart() || source.getAuxData() == 100))
/*      */           {
/* 1522 */             source.setDamage(source.getDamage() + fence.getDamageModifierForItem(source, false) * source.getDamageModifier());
/*      */           }
/*      */         } 
/* 1525 */       } catch (NoSuchActionException nsa) {
/*      */         
/* 1527 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/*      */     } 
/* 1530 */     if (counter * 10.0F > time) {
/*      */       
/* 1532 */       VolaTile tile = Zones.getTileOrNull(fence.getTileX(), fence.getTileY(), fence.isOnSurface());
/* 1533 */       if (tile != null) {
/*      */         
/* 1535 */         Structure struct = tile.getStructure();
/* 1536 */         if (struct != null)
/*      */         {
/* 1538 */           if (struct.wouldCreateFlyingStructureIfRemoved((StructureSupport)fence)) {
/*      */             
/* 1540 */             performer.getCommunicator().sendNormalServerMessage("Removing that would cause a collapsing section.");
/*      */             
/* 1542 */             return true;
/*      */           } 
/*      */         }
/*      */       } 
/* 1546 */       toReturn = true;
/*      */       
/*      */       try {
/* 1549 */         Zone zone = Zones.getZone(fence.getZoneId());
/* 1550 */         zone.removeFence(fence);
/* 1551 */         fence.delete();
/*      */       }
/* 1553 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 1555 */         logger.log(Level.WARNING, "Fence in nonexistant zone? " + performer.getName() + " " + nsz.getMessage(), (Throwable)nsz);
/*      */       } 
/*      */     } 
/* 1558 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean buildFirstFence(Creature performer, Item source, Fence fence, float counter, int action, boolean instaFinish) throws FailedException {
/* 1564 */     boolean toReturn = false;
/* 1565 */     Skill primskill = null;
/* 1566 */     primskill = Fence.getSkillNeededForFence(performer, fence);
/* 1567 */     if (primskill == null) {
/*      */       
/* 1569 */       performer.getCommunicator().sendNormalServerMessage("Failed to locate skill needed for this " + fence
/* 1570 */           .getName() + ". You cannot progress.");
/* 1571 */       logger.log(Level.WARNING, "Failed to find out what skill was needed for " + fence
/* 1572 */           .getName() + " at :" + fence.getTileX() + ", " + fence
/* 1573 */           .getTileY());
/* 1574 */       throw new FailedException("Failed to locate skill needed for this " + fence.getName());
/*      */     } 
/*      */     
/* 1577 */     if (fence.getLayer() != performer.getLayer()) {
/*      */       
/* 1579 */       performer.getCommunicator().sendNormalServerMessage("You would not be able to reach the " + fence.getName() + ".");
/* 1580 */       throw new FailedException("You would not be able to reach the " + fence.getName() + ".");
/*      */     } 
/* 1582 */     Skill hammer = null;
/* 1583 */     int[] tNeeded = null;
/* 1584 */     int time = 100;
/* 1585 */     double bonus = 0.0D;
/* 1586 */     Action act = null;
/*      */     
/*      */     try {
/* 1589 */       act = performer.getCurrentAction();
/*      */     }
/* 1591 */     catch (NoSuchActionException nsa) {
/*      */       
/* 1593 */       logger.log(Level.INFO, performer.getName() + " - this action does not exist?", (Throwable)nsa);
/* 1594 */       return true;
/*      */     } 
/* 1596 */     if (!instaFinish)
/*      */     {
/* 1598 */       if (counter == 1.0F) {
/*      */         
/* 1600 */         tNeeded = Fence.getItemTemplatesNeededForFence(fence);
/* 1601 */         if (tNeeded == null) {
/*      */           
/* 1603 */           performer.getCommunicator().sendNormalServerMessage("You failed to figure out what is needed for this " + fence
/* 1604 */               .getName() + ". You cannot progress.");
/* 1605 */           logger.log(Level.WARNING, "Failed to find out what items were needed for " + fence.getName() + " at :" + fence
/* 1606 */               .getTileX() + ", " + fence.getTileY());
/* 1607 */           throw new FailedException("You failed to figure out what is needed for this " + fence.getName() + ". You cannot progress.");
/*      */         } 
/*      */         
/* 1610 */         for (int x = 0; x < tNeeded.length; x++) {
/*      */           
/* 1612 */           if (tNeeded[x] != -1) {
/*      */             
/* 1614 */             if (!instaFinish) {
/*      */               
/*      */               try {
/* 1617 */                 ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(tNeeded[x]);
/* 1618 */                 Item check = creatureHasItem(template, performer, false);
/* 1619 */                 if (check == null) {
/*      */                   
/* 1621 */                   performer.getCommunicator().sendNormalServerMessage("You need at least " + template
/* 1622 */                       .getNameWithGenus() + " to continue this " + fence
/* 1623 */                       .getName() + ".");
/* 1624 */                   throw new FailedException("You need at least " + template.getNameWithGenus() + " to continue this " + fence
/* 1625 */                       .getName() + ".");
/*      */                 } 
/* 1627 */                 if (template.getTemplateId() == 385) {
/*      */                   
/* 1629 */                   if (check.getWeightGrams() < template.getWeightGrams() * 0.5D)
/*      */                   {
/* 1631 */                     performer.getCommunicator().sendNormalServerMessage("The " + check
/* 1632 */                         .getName() + " you try to use contains too little material to continue that " + fence
/*      */                         
/* 1634 */                         .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */                     
/* 1636 */                     throw new FailedException("The " + check.getName() + " you try to use contains too little material to continue that " + fence
/*      */                         
/* 1638 */                         .getName());
/*      */                   }
/*      */                 
/* 1641 */                 } else if (check.getWeightGrams() < template.getWeightGrams()) {
/*      */                   
/* 1643 */                   performer.getCommunicator().sendNormalServerMessage("The " + check
/* 1644 */                       .getName() + " you try to use contains too little material to continue that " + fence
/*      */                       
/* 1646 */                       .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */                   
/* 1648 */                   throw new FailedException("The " + check.getName() + " you try to use contains too little material to continue that " + fence
/*      */                       
/* 1650 */                       .getName());
/*      */                 }
/*      */               
/* 1653 */               } catch (NoSuchTemplateException nst) {
/*      */                 
/* 1655 */                 performer.getCommunicator().sendNormalServerMessage("You can't figure out what is needed to continue this " + fence
/* 1656 */                     .getName() + ".");
/* 1657 */                 logger.log(Level.WARNING, "Failed to find out what items were needed for fence at :" + fence
/* 1658 */                     .getTileX() + ", " + fence
/* 1659 */                     .getTileY() + " Template was: " + tNeeded[x]);
/* 1660 */                 throw new FailedException("You can't figure out what is needed to continue this " + fence
/* 1661 */                     .getName() + ".");
/*      */               } 
/*      */             }
/*      */           } else {
/*      */             
/* 1666 */             performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " is already finished.");
/* 1667 */             throw new FailedException("The " + fence.getName() + " is already finished.");
/*      */           } 
/*      */         } 
/*      */         
/* 1671 */         time = Actions.getStandardActionTime(performer, primskill, source, 0.0D);
/* 1672 */         act.setTimeLeft(time);
/* 1673 */         performer.getCommunicator().sendNormalServerMessage("You start to build a " + fence.getName() + ".");
/* 1674 */         Server.getInstance().broadCastAction(performer.getName() + " starts to build a " + fence.getName() + ".", performer, 5);
/*      */         
/* 1676 */         performer.sendActionControl("Building " + fence.getName(), true, time);
/*      */         
/* 1678 */         performer.getStatus().modifyStamina(-1500.0F);
/* 1679 */         if (source.getTemplateId() == 63) {
/* 1680 */           source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/* 1681 */         } else if (source.getTemplateId() == 62) {
/* 1682 */           source.setDamage(source.getDamage() + 1.0E-4F * source.getDamageModifier());
/*      */         } 
/*      */       } else {
/*      */         
/* 1686 */         time = act.getTimeLeft();
/*      */         
/* 1688 */         if (act.currentSecond() % 5 == 0) {
/*      */           
/* 1690 */           performer.getStatus().modifyStamina(-10000.0F);
/* 1691 */           if (source.getTemplateId() == 63) {
/* 1692 */             source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/* 1693 */           } else if (source.getTemplateId() == 62) {
/* 1694 */             source.setDamage(source.getDamage() + 1.0E-4F * source.getDamageModifier());
/*      */           } 
/* 1696 */         }  if (act.mayPlaySound()) {
/*      */           
/* 1698 */           String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 1699 */           if (fence.isStone())
/* 1700 */             s = "sound.work.masonry"; 
/* 1701 */           SoundPlayer.playSound(s, performer, 1.0F);
/*      */         } 
/*      */       } 
/*      */     }
/* 1705 */     if (counter * 10.0F > time || instaFinish) {
/*      */       
/* 1707 */       tNeeded = Fence.getItemTemplatesNeededForFence(fence);
/* 1708 */       if (tNeeded == null) {
/*      */         
/* 1710 */         performer.getCommunicator().sendNormalServerMessage("You failed to figure out what is needed for this " + fence
/* 1711 */             .getName() + ". You cannot progress.");
/* 1712 */         logger.log(Level.WARNING, "Failed to find out what items were needed for " + fence
/* 1713 */             .getName() + " at :" + fence.getTileX() + ", " + fence
/* 1714 */             .getTileY());
/* 1715 */         throw new FailedException("You failed to figure out what is needed for this " + fence.getName() + ". You cannot progress.");
/*      */       } 
/*      */       
/* 1718 */       for (int x = 0; x < tNeeded.length; x++) {
/*      */         
/* 1720 */         if (tNeeded[x] != -1) {
/*      */ 
/*      */           
/*      */           try {
/* 1724 */             ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(tNeeded[x]);
/* 1725 */             Item check = creatureHasItem(template, performer, false);
/* 1726 */             if (instaFinish) {
/*      */               
/* 1728 */               performer.getCommunicator()
/* 1729 */                 .sendNormalServerMessage("You conjure up " + template
/* 1730 */                   .getNameWithGenus() + " to build that " + fence
/* 1731 */                   .getName() + ".");
/*      */             } else {
/* 1733 */               if (check == null) {
/*      */                 
/* 1735 */                 performer.getCommunicator().sendNormalServerMessage("You need " + template
/* 1736 */                     .getNameWithGenus() + " to build that " + fence.getName() + ".");
/* 1737 */                 throw new FailedException("You need " + template.getNameWithGenus() + " to build that " + fence
/* 1738 */                     .getName() + ".");
/*      */               } 
/* 1740 */               if (template.getTemplateId() == 385) {
/*      */                 
/* 1742 */                 if (check.getWeightGrams() < template.getWeightGrams() * 0.5D) {
/*      */                   
/* 1744 */                   performer.getCommunicator().sendNormalServerMessage("The " + check
/* 1745 */                       .getName() + " you try to use contains too little material to build that " + fence
/*      */                       
/* 1747 */                       .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */                   
/* 1749 */                   throw new FailedException("The " + check.getName() + " you try to use contains too little material to build that " + fence
/* 1750 */                       .getName());
/*      */                 } 
/*      */                 
/* 1753 */                 Items.destroyItem(check.getWurmId());
/*      */               } else {
/* 1755 */                 if (check.getWeightGrams() < template.getWeightGrams()) {
/*      */                   
/* 1757 */                   performer.getCommunicator().sendNormalServerMessage("The " + check
/* 1758 */                       .getName() + " you try to use contains too little material to build that " + fence
/* 1759 */                       .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */                   
/* 1761 */                   throw new FailedException("The " + check.getName() + " you try to use contains too little material to build that " + fence
/* 1762 */                       .getName());
/*      */                 } 
/*      */ 
/*      */                 
/* 1766 */                 check.setWeight(check.getWeightGrams() - template.getWeightGrams(), true);
/*      */               } 
/* 1768 */             }  if (!instaFinish) {
/* 1769 */               act.setPower(check.getCurrentQualityLevel());
/*      */             }
/* 1771 */           } catch (NoSuchTemplateException nst) {
/*      */             
/* 1773 */             performer.getCommunicator().sendNormalServerMessage("You can't figure out what is needed to build that " + fence
/* 1774 */                 .getName() + ".");
/* 1775 */             logger.log(Level.WARNING, "Failed to find out what items were needed for fence at :" + fence.getTileX() + ", " + fence
/* 1776 */                 .getTileY() + " Template was: " + tNeeded[x]);
/* 1777 */             throw new FailedException("You can't figure out what is needed to build that " + fence.getName() + ".");
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1782 */           performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " is already finished.");
/* 1783 */           throw new FailedException("The " + fence.getName() + " is already finished.");
/*      */         } 
/*      */       } 
/*      */       
/*      */       try {
/* 1788 */         hammer = performer.getSkills().getSkill(source.getPrimarySkill());
/*      */       }
/* 1790 */       catch (NoSuchSkillException nss) {
/*      */ 
/*      */         
/*      */         try {
/* 1794 */           performer.getSkills().learn(source.getPrimarySkill(), 1.0F);
/*      */         }
/* 1796 */         catch (NoSuchSkillException noSuchSkillException) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1801 */       if (hammer != null) {
/*      */         
/* 1803 */         hammer.skillCheck(10.0D, source, 0.0D, false, counter);
/* 1804 */         bonus = hammer.getKnowledge(source, 0.0D) / 10.0D;
/*      */       } 
/* 1806 */       primskill.skillCheck(10.0D, source, bonus, false, counter);
/* 1807 */       double power = act.getPower() / (fence.getFinishState()).state;
/* 1808 */       power = Math.min(fence.getQualityLevel() + power, primskill.getKnowledge(0.0D));
/* 1809 */       if (instaFinish) {
/*      */         
/* 1811 */         power = Math.min(100.0D, source.getAuxData());
/* 1812 */         fence.setState(fence.getFinishState());
/* 1813 */         fence.setType(Fence.getFenceForPlan(fence.getType()));
/*      */       } else {
/*      */         
/* 1816 */         fence.setState(StructureStateEnum.INITIALIZED);
/* 1817 */       }  fence.setQualityLevel((float)power);
/* 1818 */       fence.improveOrigQualityLevel((float)power);
/*      */       
/*      */       try {
/* 1821 */         fence.save();
/*      */       }
/* 1823 */       catch (IOException iox) {
/*      */         
/* 1825 */         logger.log(Level.WARNING, "Failed to save fence " + fence.getTileX() + ", " + fence.getTileY() + ", " + performer
/* 1826 */             .getName() + ":" + iox.getMessage(), iox);
/*      */       } 
/* 1828 */       if (!instaFinish)
/* 1829 */         performer.getCommunicator().sendNormalServerMessage("You lay the foundation to the " + fence.getName() + "."); 
/* 1830 */       return true;
/*      */     } 
/* 1832 */     return false;
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
/*      */   public static Item creatureHasItem(ItemTemplate template, Creature performer, boolean checkWeight) {
/* 1863 */     Item[] items = performer.getInventory().getAllItems(false); int i;
/* 1864 */     for (i = 0; i < items.length; i++) {
/*      */       
/* 1866 */       if (items[i].getTemplateId() == template.getTemplateId() && (items[i]
/* 1867 */         .getWeightGrams() >= template.getWeightGrams() || !checkWeight))
/*      */       {
/* 1869 */         return items[i];
/*      */       }
/*      */     } 
/* 1872 */     items = performer.getBody().getBodyItem().getAllItems(false);
/* 1873 */     for (i = 0; i < items.length; i++) {
/*      */       
/* 1875 */       if (items[i].getTemplateId() == template.getTemplateId() && (items[i]
/* 1876 */         .getWeightGrams() >= template.getWeightGrams() || !checkWeight))
/*      */       {
/* 1878 */         return items[i];
/*      */       }
/*      */     } 
/* 1881 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean continueFence(Creature performer, Fence fence, Item source, float counter, int action, Action act) {
/* 1887 */     boolean toReturn = false;
/* 1888 */     Skill primskill = Fence.getSkillNeededForFence(performer, fence);
/* 1889 */     if (primskill == null) {
/*      */       
/* 1891 */       performer.getCommunicator().sendNormalServerMessage("Failed to locate skill needed for this " + fence
/* 1892 */           .getName() + ". You cannot progress.");
/* 1893 */       logger.log(Level.WARNING, "Failed to find out what skill was needed for " + fence
/* 1894 */           .getName() + " at :" + fence.getTileX() + ", " + fence
/* 1895 */           .getTileY());
/* 1896 */       performer.getCommunicator().sendActionResult(false);
/* 1897 */       return true;
/*      */     } 
/* 1899 */     if (fence.isFinished()) {
/* 1900 */       return true;
/*      */     }
/*      */     
/* 1903 */     if (fence.getLayer() != performer.getLayer()) {
/*      */       
/* 1905 */       performer.getCommunicator().sendNormalServerMessage("You would not be able to reach the " + fence.getName() + ".");
/* 1906 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1910 */     if (performer.isFighting()) {
/*      */       
/* 1912 */       performer.getCommunicator().sendNormalServerMessage("You cannot do that while in combat.");
/* 1913 */       return true;
/*      */     } 
/*      */     
/* 1916 */     Structure structure = getStructureOrNullAtTileBorder(fence.getTileX(), fence.getTileY(), fence
/* 1917 */         .getDir(), performer
/* 1918 */         .isOnSurface());
/*      */     
/* 1920 */     if (structure != null && 
/* 1921 */       FloorBehaviour.getRequiredBuildSkillForFloorLevel(fence.getFloorLevel(), false) > primskill.getKnowledge(0.0D)) {
/*      */       
/* 1923 */       performer.getCommunicator().sendAlertServerMessage("You are not skilled enough to build at this height.", (byte)3);
/*      */       
/* 1925 */       performer.getCommunicator().sendActionResult(false);
/* 1926 */       return true;
/*      */     } 
/* 1928 */     Skill hammer = null;
/* 1929 */     int[] tNeeded = null;
/* 1930 */     int time = 100;
/* 1931 */     double bonus = 0.0D;
/* 1932 */     StructureStateEnum state = fence.getState();
/* 1933 */     boolean mayFinish = true;
/* 1934 */     if (counter == 1.0F) {
/*      */ 
/*      */       
/* 1937 */       tNeeded = Fence.getItemTemplatesNeededForFence(fence);
/* 1938 */       if (tNeeded == null) {
/*      */         
/* 1940 */         performer.getCommunicator().sendNormalServerMessage("You failed to figure out what is needed for this " + fence
/* 1941 */             .getName() + ". You cannot progress.");
/* 1942 */         logger.log(Level.WARNING, "Failed to find out what items were needed for " + fence
/* 1943 */             .getName() + " at :" + fence.getTileX() + ", " + fence
/* 1944 */             .getTileY());
/* 1945 */         performer.getCommunicator().sendActionResult(false);
/* 1946 */         return true;
/*      */       } 
/* 1948 */       for (int x = 0; x < tNeeded.length; x++) {
/*      */         
/* 1950 */         if (tNeeded[x] != -1) {
/*      */ 
/*      */           
/*      */           try {
/* 1954 */             ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(tNeeded[x]);
/* 1955 */             Item check = creatureHasItem(template, performer, false);
/* 1956 */             if (check == null) {
/*      */               
/* 1958 */               performer.getCommunicator().sendNormalServerMessage("You need " + template
/* 1959 */                   .getNameWithGenus() + " to continue that " + fence.getName() + ".");
/* 1960 */               performer.getCommunicator().sendActionResult(false);
/* 1961 */               return true;
/*      */             } 
/* 1963 */             if (template.getTemplateId() == 385) {
/*      */               
/* 1965 */               if (check.getWeightGrams() < template.getWeightGrams() * 0.5D)
/*      */               {
/* 1967 */                 performer.getCommunicator().sendNormalServerMessage("The " + check
/* 1968 */                     .getName() + " you try to use contains too little material to continue that " + fence
/*      */                     
/* 1970 */                     .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */                 
/* 1972 */                 performer.getCommunicator().sendActionResult(false);
/* 1973 */                 return true;
/*      */               }
/*      */             
/* 1976 */             } else if (check.getWeightGrams() < template.getWeightGrams()) {
/*      */               
/* 1978 */               performer.getCommunicator().sendNormalServerMessage("The " + check
/* 1979 */                   .getName() + " you try to use contains too little material to continue that " + fence
/* 1980 */                   .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */               
/* 1982 */               performer.getCommunicator().sendActionResult(false);
/* 1983 */               return true;
/*      */             }
/*      */           
/* 1986 */           } catch (NoSuchTemplateException nst) {
/*      */             
/* 1988 */             performer.getCommunicator().sendNormalServerMessage("You can't figure out what is needed to continue this " + fence
/* 1989 */                 .getName() + ".");
/* 1990 */             logger.log(Level.WARNING, "Failed to find out what items were needed for fence at :" + fence.getTileX() + ", " + fence
/* 1991 */                 .getTileY() + " Template was: " + tNeeded[x]);
/* 1992 */             performer.getCommunicator().sendActionResult(false);
/* 1993 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1998 */           performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " is already finished.");
/* 1999 */           performer.getCommunicator().sendActionResult(false);
/* 2000 */           return true;
/*      */         } 
/*      */       } 
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
/* 2018 */       time = Actions.getStandardActionTime(performer, primskill, source, 0.0D);
/* 2019 */       act.setTimeLeft(time);
/* 2020 */       performer.getCommunicator().sendNormalServerMessage("You continue to build a " + fence.getName() + ".");
/* 2021 */       Server.getInstance().broadCastAction(performer.getName() + " continues to build a " + fence.getName() + ".", performer, 5);
/*      */       
/* 2023 */       performer.sendActionControl("Continuing " + fence.getName(), true, time);
/*      */       
/* 2025 */       performer.getStatus().modifyStamina(-1500.0F);
/* 2026 */       if (source.getTemplateId() == 63) {
/* 2027 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 2028 */       } else if (source.getTemplateId() == 62) {
/* 2029 */         source.setDamage(source.getDamage() + 3.0E-4F * source.getDamageModifier());
/* 2030 */       }  if (performer.getDeity() != null && (performer.getDeity()).number == 3)
/*      */       {
/* 2032 */         performer.maybeModifyAlignment(0.5F);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 2037 */       time = act.getTimeLeft();
/* 2038 */       if (state.state >= (fence.getFinishState()).state) {
/*      */         
/* 2040 */         performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " is already finished.");
/* 2041 */         performer.getCommunicator().sendActionResult(false);
/* 2042 */         return true;
/*      */       } 
/* 2044 */       if (Math.abs(performer.getPosX() - (fence.getEndX() << 2)) > 8.0F || Math.abs(performer.getPosX() - (fence.getStartX() << 2)) > 8.0F || 
/* 2045 */         Math.abs(performer.getPosY() - (fence.getEndY() << 2)) > 8.0F || Math.abs(performer.getPosY() - (fence.getStartY() << 2)) > 8.0F) {
/*      */         
/* 2047 */         performer.getCommunicator().sendAlertServerMessage("You are too far from the end.");
/* 2048 */         return true;
/*      */       } 
/* 2050 */       if (state.state == (fence.getFinishState()).state - 1) {
/*      */         
/* 2052 */         float posx = performer.getStatus().getPositionX();
/* 2053 */         float posy = performer.getStatus().getPositionY();
/* 2054 */         int tilex = (int)posx >> 2;
/* 2055 */         int tiley = (int)posy >> 2;
/* 2056 */         if (fence.isHorizontal()) {
/*      */           
/* 2058 */           if (tilex == fence.getTileX())
/*      */           {
/* 2060 */             float ty = (fence.getTileY() << 2);
/* 2061 */             if (posy > ty - 2.0F && posy < ty + 2.0F) {
/* 2062 */               mayFinish = false;
/*      */             }
/*      */           }
/*      */         
/*      */         }
/* 2067 */         else if (tiley == fence.getTileY()) {
/*      */           
/* 2069 */           float tx = (fence.getTileX() << 2);
/* 2070 */           if (posx > tx - 2.0F && posx < tx + 2.0F) {
/* 2071 */             mayFinish = false;
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
/* 2082 */       if (act.currentSecond() % 5 == 0) {
/*      */         
/* 2084 */         performer.getStatus().modifyStamina(-10000.0F);
/* 2085 */         if (source.getTemplateId() == 63) {
/* 2086 */           source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 2087 */         } else if (source.getTemplateId() == 62) {
/* 2088 */           source.setDamage(source.getDamage() + 3.0E-4F * source.getDamageModifier());
/* 2089 */         }  if (!mayFinish) {
/*      */           
/* 2091 */           performer.getCommunicator().sendNormalServerMessage("You are standing in the way of finishing this wall or fence. You have to move to the side.");
/*      */           
/* 2093 */           performer.getActions().clear();
/* 2094 */           return true;
/*      */         } 
/*      */       } 
/* 2097 */       if (act.mayPlaySound()) {
/*      */         
/* 2099 */         String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 2100 */         if (fence.isStone())
/* 2101 */           s = "sound.work.masonry"; 
/* 2102 */         SoundPlayer.playSound(s, performer, 1.0F);
/*      */       } 
/*      */     } 
/* 2105 */     if (counter * 10.0F > time && mayFinish) {
/*      */       
/* 2107 */       toReturn = true;
/* 2108 */       tNeeded = Fence.getItemTemplatesNeededForFence(fence);
/* 2109 */       Item check = null;
/* 2110 */       if (Math.abs(performer.getPosX() - (fence.getEndX() << 2)) > 8.0F || Math.abs(performer.getPosX() - (fence.getStartX() << 2)) > 8.0F || 
/* 2111 */         Math.abs(performer.getPosY() - (fence.getEndY() << 2)) > 8.0F || Math.abs(performer.getPosY() - (fence.getStartY() << 2)) > 8.0F) {
/*      */         
/* 2113 */         performer.getCommunicator().sendAlertServerMessage("You are too far from the end.");
/* 2114 */         return true;
/*      */       } 
/* 2116 */       if (tNeeded == null) {
/*      */         
/* 2118 */         performer.getCommunicator().sendNormalServerMessage("You failed to figure out what is needed for this " + fence
/* 2119 */             .getName() + ". You cannot progress.");
/* 2120 */         logger.log(Level.WARNING, "Failed to find out what items were needed for " + fence
/* 2121 */             .getName() + " at :" + fence.getTileX() + ", " + fence
/* 2122 */             .getTileY());
/* 2123 */         performer.getCommunicator().sendActionResult(false);
/* 2124 */         return true;
/*      */       } 
/* 2126 */       Set<Item> destroyedItems = new HashSet<>();
/* 2127 */       for (int x = 0; x < tNeeded.length; x++) {
/*      */         
/* 2129 */         check = null;
/* 2130 */         if (tNeeded[x] != -1) {
/*      */           
/*      */           try
/*      */           {
/* 2134 */             ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(tNeeded[x]);
/* 2135 */             check = creatureHasItem(template, performer, false);
/* 2136 */             if (check == null) {
/*      */               
/* 2138 */               performer.getCommunicator().sendNormalServerMessage("You need " + template
/* 2139 */                   .getNameWithGenus() + " to continue that " + fence.getName() + ".");
/* 2140 */               destroyedItems.clear();
/* 2141 */               performer.getCommunicator().sendActionResult(false);
/* 2142 */               return true;
/*      */             } 
/* 2144 */             if (template.getTemplateId() == 385) {
/*      */               
/* 2146 */               if (check.getWeightGrams() < template.getWeightGrams() * 0.5D) {
/*      */                 
/* 2148 */                 performer.getCommunicator().sendNormalServerMessage("The " + check
/* 2149 */                     .getName() + " you try to use contains too little material to build that " + fence
/*      */                     
/* 2151 */                     .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */                 
/* 2153 */                 destroyedItems.clear();
/* 2154 */                 performer.getCommunicator().sendActionResult(false);
/* 2155 */                 return true;
/*      */               } 
/*      */               
/* 2158 */               destroyedItems.add(check);
/*      */             } else {
/*      */               
/* 2161 */               if (check.getWeightGrams() < template.getWeightGrams()) {
/*      */                 
/* 2163 */                 performer.getCommunicator().sendNormalServerMessage("The " + check
/* 2164 */                     .getName() + " you try to use contains too little material to continue that " + fence
/* 2165 */                     .getName() + ". Please drop it or combine it with another item of the same kind.");
/*      */                 
/* 2167 */                 destroyedItems.clear();
/* 2168 */                 performer.getCommunicator().sendActionResult(false);
/* 2169 */                 return true;
/*      */               } 
/*      */ 
/*      */               
/* 2173 */               destroyedItems.add(check);
/*      */             } 
/* 2175 */             act.setPower(check.getCurrentQualityLevel());
/*      */           }
/* 2177 */           catch (NoSuchTemplateException nst)
/*      */           {
/* 2179 */             performer.getCommunicator().sendNormalServerMessage("You can't figure out what is needed to continue this " + fence
/* 2180 */                 .getName() + ".");
/* 2181 */             logger.log(Level.WARNING, "Failed to find out what items were needed for fence at :" + fence.getTileX() + ", " + fence
/* 2182 */                 .getTileY() + " Template was: " + tNeeded[x]);
/* 2183 */             destroyedItems.clear();
/* 2184 */             performer.getCommunicator().sendActionResult(false);
/* 2185 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 2190 */           performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " is already finished.");
/* 2191 */           destroyedItems.clear();
/* 2192 */           performer.getCommunicator().sendActionResult(false);
/* 2193 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/*      */       try {
/* 2198 */         hammer = performer.getSkills().getSkill(source.getPrimarySkill());
/*      */       }
/* 2200 */       catch (NoSuchSkillException nss) {
/*      */ 
/*      */         
/*      */         try {
/* 2204 */           hammer = performer.getSkills().learn(source.getPrimarySkill(), 1.0F);
/*      */         }
/* 2206 */         catch (NoSuchSkillException noSuchSkillException) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2211 */       if (hammer != null) {
/*      */         
/* 2213 */         hammer.skillCheck(fence.getDifficulty(), source, 0.0D, false, counter);
/* 2214 */         bonus = hammer.getKnowledge(source, 0.0D) / 10.0D;
/*      */       } 
/* 2216 */       double power = primskill.skillCheck(fence.getDifficulty(), source, bonus, false, counter);
/* 2217 */       if (power > 0.0D) {
/*      */         
/* 2219 */         performer.getCommunicator().sendActionResult(true);
/* 2220 */         Item[] destroyed = destroyedItems.<Item>toArray(new Item[destroyedItems.size()]);
/* 2221 */         for (int i = 0; i < destroyed.length; i++) {
/*      */           
/* 2223 */           if (destroyed[i].getTemplateId() == 385) {
/*      */             
/* 2225 */             Items.destroyItem(destroyed[i].getWurmId());
/*      */           }
/*      */           else {
/*      */             
/* 2229 */             destroyed[i].setWeight(destroyed[i].getWeightGrams() - destroyed[i].getTemplate().getWeightGrams(), true);
/*      */           } 
/*      */         } 
/*      */         
/* 2233 */         destroyedItems.clear();
/* 2234 */         power = act.getPower() / (fence.getFinishState()).state;
/* 2235 */         double skilladdition = fence.getQualityLevel() + primskill.getKnowledge(0.0D) / (fence.getFinishState()).state;
/* 2236 */         fence.setQualityLevel((float)Math.min(fence.getQualityLevel() + power, skilladdition));
/* 2237 */         fence.improveOrigQualityLevel(fence.getQualityLevel());
/* 2238 */         if ((fence.getState()).state < (fence.getFinishState()).state) {
/* 2239 */           fence.setState(StructureStateEnum.getStateByValue((byte)(state.state + 1)));
/*      */         }
/*      */         try {
/* 2242 */           fence.save();
/*      */         }
/* 2244 */         catch (IOException iox) {
/*      */           
/* 2246 */           logger.log(Level.WARNING, "Failed to save fence " + fence.getTileX() + ", " + fence.getTileY() + ", " + performer
/* 2247 */               .getName() + ":" + iox.getMessage(), iox);
/*      */         } 
/* 2249 */         if (fence.isFinished()) {
/*      */           
/* 2251 */           performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " is finished now.");
/* 2252 */           TileEvent.log(fence.getTileX(), fence.getTileY(), 0, performer.getWurmId(), action);
/* 2253 */           VolaTile tile = Zones.getTileOrNull(fence.getTileX(), fence.getTileY(), fence.isOnSurface());
/* 2254 */           if (tile != null) {
/*      */             
/* 2256 */             fence.setType(Fence.getFenceForPlan(fence.getType()));
/*      */             
/*      */             try {
/* 2259 */               fence.save();
/*      */             }
/* 2261 */             catch (IOException iox) {
/*      */               
/* 2263 */               logger.log(Level.WARNING, "Failed to save fence " + fence.getTileX() + ", " + fence.getTileY() + ", " + performer
/* 2264 */                   .getName() + ":" + iox.getMessage(), iox);
/*      */             } 
/* 2266 */             tile.updateFence(fence);
/* 2267 */             if (fence.isDoor()) {
/*      */               
/* 2269 */               DbFenceGate dbFenceGate = new DbFenceGate(fence);
/* 2270 */               dbFenceGate.addToTiles();
/* 2271 */               VolaTile vtile = dbFenceGate.getInnerTile();
/* 2272 */               Village village = vtile.getVillage();
/* 2273 */               if (village != null) {
/*      */                 
/* 2275 */                 village.addGate((FenceGate)dbFenceGate);
/*      */               }
/*      */               else {
/*      */                 
/* 2279 */                 vtile = dbFenceGate.getOuterTile();
/* 2280 */                 village = vtile.getVillage();
/* 2281 */                 if (village != null)
/*      */                 {
/* 2283 */                   village.addGate((FenceGate)dbFenceGate);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/* 2289 */             logger.log(Level.WARNING, "Tried to finish and update fence on tile where no fence was located: " + fence
/* 2290 */                 .getTileX() + "," + fence.getTileY());
/* 2291 */           }  return true;
/*      */         } 
/*      */ 
/*      */         
/* 2295 */         destroyedItems.clear();
/* 2296 */         performer.getCommunicator().sendNormalServerMessage("You continue on the " + fence.getName() + ".");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2301 */         destroyedItems.clear();
/* 2302 */         performer.getCommunicator().sendNormalServerMessage("You fail to continue the " + fence.getName() + ".");
/* 2303 */         performer.getCommunicator().sendActionResult(false);
/* 2304 */         return true;
/*      */       } 
/*      */     } 
/* 2307 */     return toReturn;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean tileBordersToFence(int tilex, int tiley, int heightOffset, boolean surfaced) {
/* 2419 */     VolaTile tile = Zones.getOrCreateTile(tilex, tiley, surfaced);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2424 */     for (Fence f : tile.getFences()) {
/*      */       
/* 2426 */       if (f.getHeightOffset() == heightOffset)
/*      */       {
/* 2428 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 2432 */     tile = Zones.getTileOrNull(tilex, tiley + 1, surfaced);
/* 2433 */     if (tile != null)
/*      */     {
/* 2435 */       for (Fence f : tile.getFences()) {
/*      */         
/* 2437 */         if (f.isHorizontal() && f.getHeightOffset() == heightOffset)
/*      */         {
/* 2439 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/* 2443 */     tile = Zones.getTileOrNull(tilex + 1, tiley, surfaced);
/* 2444 */     if (tile != null)
/*      */     {
/* 2446 */       for (Fence f : tile.getFences()) {
/*      */         
/* 2448 */         if (!f.isHorizontal() && f.getHeightOffset() == heightOffset)
/*      */         {
/* 2450 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 2455 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean repairFence(Action act, Creature performer, Item repairItem, Fence fence, float counter) {
/* 2461 */     if (performer.isFighting()) {
/*      */       
/* 2463 */       performer.getCommunicator().sendNormalServerMessage("You cannot do that while in combat.");
/* 2464 */       return true;
/*      */     } 
/*      */     
/* 2467 */     if (fence.getLayer() != performer.getLayer()) {
/*      */       
/* 2469 */       performer.getCommunicator().sendNormalServerMessage("You cannot repair that, you are too far away.");
/* 2470 */       return true;
/*      */     } 
/* 2472 */     if (fence.getDamage() == 0.0F) {
/*      */       
/* 2474 */       performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " does not need repairing.");
/* 2475 */       return true;
/*      */     } 
/* 2477 */     if (!Methods.isActionAllowed(performer, (short)116, fence.getTileX(), fence.getTileY()))
/*      */     {
/* 2479 */       return true;
/*      */     }
/*      */     
/* 2482 */     boolean insta = (performer.getPower() >= 5);
/* 2483 */     int templateId = fence.getRepairItemTemplate();
/* 2484 */     if (repairItem.getTemplateId() != templateId && !insta) {
/*      */       
/* 2486 */       performer.getCommunicator().sendNormalServerMessage("You cannot repair the " + fence.getName() + " with that item.");
/* 2487 */       return true;
/*      */     } 
/* 2489 */     if (repairItem.getWeightGrams() < repairItem.getTemplate().getWeightGrams() * ((repairItem.getTemplateId() == 9) ? 0.7F : 1.0F)) {
/*      */       
/* 2491 */       performer.getCommunicator().sendNormalServerMessage("The " + repairItem.getName() + " contains too little material to repair the " + fence.getName() + ".");
/* 2492 */       return true;
/*      */     } 
/*      */     
/* 2495 */     Skill buildSkill = performer.getSkills().getSkillOrLearn(1005);
/* 2496 */     if (fence.isStone() || fence.isSlate() || fence.isMarble() || fence.isPlastered() || fence
/* 2497 */       .isRoundedStone() || fence.isPottery() || fence.isSandstone() || fence.isIron())
/* 2498 */       buildSkill = performer.getSkills().getSkillOrLearn(1013); 
/* 2499 */     Skill repairSkill = performer.getSkills().getSkillOrLearn(10035);
/*      */     
/* 2501 */     int time = 400;
/* 2502 */     if (counter == 1.0F) {
/*      */       
/* 2504 */       performer.getCommunicator().sendNormalServerMessage("You start to repair the " + fence.getName() + ".");
/* 2505 */       Server.getInstance().broadCastAction(performer.getName() + " starts to repair a " + fence.getName() + ".", performer, 5);
/*      */       
/* 2507 */       time = Actions.getRepairActionTime(performer, repairSkill, buildSkill.getKnowledge(0.0D));
/* 2508 */       performer.sendActionControl(Actions.actionEntrys[193].getVerbString(), true, time);
/* 2509 */       act.setTimeLeft(time);
/* 2510 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     }
/*      */     else {
/*      */       
/* 2514 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 2517 */     if (act.mayPlaySound()) {
/*      */       
/* 2519 */       String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 2520 */       if (buildSkill.getNumber() == 1013)
/* 2521 */         s = "sound.work.masonry"; 
/* 2522 */       SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/* 2524 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 2526 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     
/* 2529 */     if (counter * 10.0F >= time || insta) {
/*      */       
/* 2531 */       if (repairItem.isCombine()) {
/*      */         
/* 2533 */         repairItem.setWeight(repairItem.getWeightGrams() - repairItem.getTemplate().getWeightGrams(), false);
/* 2534 */         if (repairItem.getWeightGrams() <= 0)
/*      */         {
/* 2536 */           repairItem.putInVoid();
/* 2537 */           act.setDestroyedItem(repairItem);
/*      */         }
/*      */       
/* 2540 */       } else if (insta) {
/*      */         
/* 2542 */         performer.sendToLoggers("Repairing fence with ID:" + fence.getId() + " located at " + fence.getTile().toString());
/*      */ 
/*      */         
/* 2545 */         fence.setDamage(0.0F);
/* 2546 */         performer.getCommunicator().sendNormalServerMessage("You magically repair the " + fence.getName() + " fence (ID:" + fence.getId() + ") with your powers.");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2551 */         repairItem.putInVoid();
/* 2552 */         act.setDestroyedItem(repairItem);
/*      */       } 
/*      */       
/* 2555 */       double power = repairSkill.skillCheck((fence.getDamage() / 10.0F), repairItem, 0.0D, false, counter);
/* 2556 */       Item destroyed = act.getDestroyedItem();
/* 2557 */       if (destroyed != null)
/* 2558 */         Items.decay(destroyed.getWurmId(), destroyed.getDbStrings()); 
/* 2559 */       act.setDestroyedItem(null);
/* 2560 */       if (insta)
/* 2561 */         power = 100.0D; 
/* 2562 */       if (power > 0.0D) {
/*      */         
/* 2564 */         performer.getCommunicator().sendNormalServerMessage("You repair the " + fence.getName() + " a bit.");
/*      */         
/* 2566 */         float cq = fence.getCurrentQualityLevel();
/* 2567 */         float diffcq = fence.getQualityLevel() - cq;
/* 2568 */         float newOrigcq = fence.getQualityLevel() - (float)(diffcq * (100.0D - power)) / 10000.0F;
/* 2569 */         float repairAmnt = 5.0F + (float)(5.0D * (repairSkill.getKnowledge(buildSkill.getKnowledge(0.0D)) + repairItem.getCurrentQualityLevel()) / 200.0D);
/*      */         
/* 2571 */         fence.setQualityLevel(newOrigcq);
/* 2572 */         fence.setDamage(Math.max(0.0F, fence.getDamage() - repairAmnt));
/* 2573 */         performer.achievement(155);
/*      */       }
/* 2575 */       else if (power < -90.0D) {
/*      */         
/* 2577 */         performer.getCommunicator().sendNormalServerMessage("You fail to repair the " + fence.getName() + " and damage it instead!");
/* 2578 */         fence.setDamage((float)Math.min(100.0D, fence.getDamage() - power / 100.0D));
/*      */       }
/*      */       else {
/*      */         
/* 2582 */         performer.getCommunicator().sendNormalServerMessage("You fail to repair the " + fence.getName() + ".");
/*      */       } 
/*      */       
/* 2585 */       fence.setLastUsed(System.currentTimeMillis());
/* 2586 */       Server.getInstance().broadCastAction(performer.getName() + " repairs a " + fence.getName() + " a bit.", performer, 5);
/*      */       
/* 2588 */       return true;
/*      */     } 
/*      */     
/* 2591 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean destroyFence(short action, Creature performer, Item destroyItem, Fence fence, boolean dealItems, float counter) {
/* 2597 */     if (!Methods.isActionAllowed(performer, action, false, fence.getTileX(), fence.getTileY(), 0, 0)) {
/* 2598 */       return true;
/*      */     }
/* 2600 */     boolean toReturn = true;
/*      */     
/* 2602 */     int time = 1000;
/*      */ 
/*      */     
/* 2605 */     boolean insta = (performer.getPower() >= 2);
/* 2606 */     float mod = fence.getDamageModifierForItem(destroyItem, true);
/*      */     
/* 2608 */     if (Servers.localServer.entryServer && fence.getOriginalQualityLevel() > 99.0F && !Servers.localServer.testServer)
/*      */     {
/* 2610 */       mod = 0.0F; } 
/* 2611 */     if (Servers.localServer.isChallengeOrEpicServer() && Zones.protectedTiles[fence.getTileX()][fence.getTileY()])
/* 2612 */       mod = 0.0F; 
/* 2613 */     if (mod <= 0.0F && !insta) {
/*      */       
/* 2615 */       performer.getCommunicator().sendNormalServerMessage("You will not do any damage to the " + fence
/* 2616 */           .getName() + " with that.");
/* 2617 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2621 */     toReturn = false;
/* 2622 */     Action act = null;
/* 2623 */     String destString = "destroy";
/* 2624 */     if (dealItems) {
/* 2625 */       destString = "disassemble";
/*      */     }
/*      */     try {
/* 2628 */       act = performer.getCurrentAction();
/*      */     }
/* 2630 */     catch (NoSuchActionException nsa) {
/*      */       
/* 2632 */       logger.log(Level.WARNING, "No Action for " + performer.getName() + "!", (Throwable)nsa);
/* 2633 */       return true;
/*      */     } 
/*      */     
/* 2636 */     if (counter == 1.0F) {
/*      */       
/* 2638 */       if (Servers.localServer.challengeServer) {
/*      */         
/* 2640 */         if (fence.getType() == StructureConstantsEnum.FENCE_STONEWALL_HIGH || fence
/* 2641 */           .getType() == StructureConstantsEnum.FENCE_PORTCULLIS) {
/* 2642 */           time = 150;
/*      */         } else {
/* 2644 */           time = 100;
/*      */         } 
/*      */       } else {
/* 2647 */         time = 300;
/* 2648 */       }  performer.getCommunicator().sendNormalServerMessage("You start to " + destString + " the " + fence
/* 2649 */           .getName() + ".");
/* 2650 */       Server.getInstance().broadCastAction(performer
/* 2651 */           .getName() + " starts to " + destString + " a " + fence.getName() + ".", performer, 5);
/*      */       
/* 2653 */       performer.sendActionControl(Actions.actionEntrys[action].getVerbString(), true, time);
/* 2654 */       act.setTimeLeft(time);
/* 2655 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     }
/*      */     else {
/*      */       
/* 2659 */       time = act.getTimeLeft();
/*      */     } 
/* 2661 */     if (act.currentSecond() % 5 == 0) {
/*      */       
/* 2663 */       sendDestroySound(performer, destroyItem, (!fence.isStone() && !fence.isIron()));
/*      */       
/* 2665 */       performer.getStatus().modifyStamina(-5000.0F);
/* 2666 */       float toolMod = fence.getDamageModifierForItem(destroyItem, false);
/* 2667 */       if (destroyItem != null && (
/* 2668 */         !destroyItem.isBodyPart() || destroyItem.getAuxData() == 100))
/* 2669 */         destroyItem.setDamage(destroyItem.getDamage() + toolMod * destroyItem.getDamageModifier()); 
/*      */     } 
/* 2671 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 2673 */       Skills skills = performer.getSkills();
/* 2674 */       Skill destroySkill = null;
/*      */       
/*      */       try {
/* 2677 */         destroySkill = skills.getSkill(102);
/*      */       }
/* 2679 */       catch (NoSuchSkillException nss) {
/*      */         
/* 2681 */         destroySkill = skills.learn(102, 1.0F);
/*      */       } 
/* 2683 */       destroySkill.skillCheck(20.0D, destroyItem, 0.0D, false, counter);
/*      */       
/* 2685 */       double damage = 0.0D;
/* 2686 */       if (insta && mod <= 0.0F) {
/*      */         
/* 2688 */         damage = 20.0D;
/* 2689 */         mod = 1.0F;
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2699 */         damage = Weapon.getModifiedDamageForWeapon(destroyItem, destroySkill) * 5.0D;
/* 2700 */         if (!Servers.localServer.challengeServer) {
/* 2701 */           damage /= (fence.getQualityLevel() / 10.0F);
/*      */         } else {
/*      */           
/* 2704 */           float divider = 10.0F;
/*      */           
/* 2706 */           if (fence.getType() == StructureConstantsEnum.FENCE_STONEWALL_HIGH || fence
/* 2707 */             .getType() == StructureConstantsEnum.FENCE_PORTCULLIS) {
/*      */             
/* 2709 */             divider--;
/*      */           }
/*      */           else {
/*      */             
/* 2713 */             divider += 2.0F;
/*      */           } 
/* 2715 */           damage /= (fence.getQualityLevel() / divider);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2720 */       Village vill = getVillageForFence(fence);
/* 2721 */       boolean citizen = false;
/* 2722 */       if (vill != null) {
/*      */         
/* 2724 */         if (isCitizenAndMayPerformAction(action, performer, vill))
/*      */         {
/* 2726 */           damage *= 50.0D;
/* 2727 */           citizen = true;
/*      */         }
/* 2729 */         else if (isAllyAndMayPerformAction(action, performer, vill))
/*      */         {
/* 2731 */           damage *= 25.0D;
/* 2732 */           citizen = true;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 2737 */       else if (Zones.isInPvPZone(fence.getTileX(), fence.getTileY())) {
/* 2738 */         damage *= 10.0D;
/*      */       } 
/*      */ 
/*      */       
/* 2742 */       if (!citizen)
/*      */       {
/* 2744 */         if (performer.getCultist() != null && performer.getCultist().doubleStructDamage()) {
/* 2745 */           damage *= 4.0D;
/*      */         } else {
/* 2747 */           damage *= 2.0D;
/*      */         }  } 
/* 2749 */       damage *= Weapon.getMaterialBashModifier(destroyItem.getMaterial());
/*      */       
/* 2751 */       if (!fence.isFinished()) {
/*      */         
/* 2753 */         int modifier = (fence.getFinishState()).state - (fence.getState()).state;
/* 2754 */         damage *= modifier;
/*      */       } 
/*      */       
/* 2757 */       float newDam = (float)(fence.getDamage() + damage * mod * 10.0D);
/* 2758 */       fence.setDamage(newDam);
/* 2759 */       if (newDam >= 100.0F) {
/*      */         
/* 2761 */         performer.getCommunicator().sendNormalServerMessage("The last parts of the " + fence
/* 2762 */             .getName() + " falls down with a crash.");
/* 2763 */         Server.getInstance().broadCastAction(performer
/* 2764 */             .getName() + " damages a " + fence.getName() + " and it falls down with a crash.", performer, 5);
/*      */         
/* 2766 */         if (performer.getDeity() != null)
/*      */         {
/* 2768 */           performer.performActionOkey(act);
/*      */         }
/* 2770 */         TileEvent.log(fence.getTileX(), fence.getTileY(), 0, performer.getWurmId(), action);
/* 2771 */         if (Servers.localServer.isChallengeServer() && fence.getType() != StructureConstantsEnum.FENCE_RUBBLE) {
/*      */ 
/*      */ 
/*      */           
/* 2775 */           DbFence dbFence = new DbFence(StructureConstantsEnum.FENCE_RUBBLE, fence.getTileX(), fence.getTileY(), fence.getHeightOffset(), 100.0F, fence.getDir(), fence.getZoneId(), fence.getLayer());
/*      */           
/*      */           try {
/* 2778 */             dbFence.setState(StructureStateEnum.FINISHED);
/* 2779 */             Zone zone = Zones.getZone(fence.getZoneId());
/* 2780 */             zone.addFence((Fence)dbFence);
/*      */           }
/* 2782 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 2784 */             logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*      */           }
/*      */         
/*      */         } 
/* 2788 */       } else if (damage > 0.0D) {
/*      */         
/* 2790 */         performer.getCommunicator().sendNormalServerMessage("You damage the " + fence.getName() + ".");
/* 2791 */         Server.getInstance().broadCastAction(performer.getName() + " damages the " + fence.getName() + ".", performer, 5);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2796 */         performer.getCommunicator().sendNormalServerMessage("You fail to damage the " + fence.getName() + ".");
/*      */       } 
/* 2798 */       toReturn = true;
/*      */     } 
/*      */     
/* 2801 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village getVillageForBlocker(Blocker blocker) {
/* 2806 */     if (blocker == null)
/* 2807 */       return null; 
/* 2808 */     int tilex = blocker.getTileX();
/* 2809 */     int tiley = blocker.getTileY();
/*      */     
/* 2811 */     Village village = Zones.getVillage(tilex, tiley, blocker.isOnSurface());
/* 2812 */     if (village != null)
/*      */     {
/* 2814 */       return village;
/*      */     }
/* 2816 */     if (blocker.isHorizontal()) {
/*      */       
/* 2818 */       village = Zones.getVillage(tilex, tiley - 1, blocker.isOnSurface());
/* 2819 */       if (village != null)
/*      */       {
/* 2821 */         return village;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 2826 */       village = Zones.getVillage(tilex - 1, tiley, blocker.isOnSurface());
/* 2827 */       if (village != null)
/*      */       {
/* 2829 */         return village;
/*      */       }
/*      */     } 
/* 2832 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Village getVillageForFence(Fence fence) {
/* 2837 */     if (fence == null)
/* 2838 */       return null; 
/* 2839 */     int tilex = fence.getTileX();
/* 2840 */     int tiley = fence.getTileY();
/*      */     
/* 2842 */     Village village = Zones.getVillage(tilex, tiley, fence.isOnSurface());
/* 2843 */     if (village != null)
/*      */     {
/* 2845 */       return village;
/*      */     }
/* 2847 */     if (fence.getDirAsByte() == 2) {
/*      */       
/* 2849 */       village = Zones.getVillage(tilex - 1, tiley, fence.isOnSurface());
/* 2850 */       if (village != null)
/*      */       {
/* 2852 */         return village;
/*      */       }
/*      */     }
/* 2855 */     else if (fence.getDirAsByte() == 0) {
/*      */       
/* 2857 */       village = Zones.getVillage(tilex, tiley - 1, fence.isOnSurface());
/* 2858 */       if (village != null)
/*      */       {
/* 2860 */         return village;
/*      */       }
/*      */     } 
/* 2863 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isCitizenAndMayPerformAction(short action, Creature performer, Village village) {
/* 2868 */     if (village != null)
/*      */     {
/* 2870 */       if (village.isCitizen(performer))
/*      */       {
/* 2872 */         if (village.isActionAllowed(action, performer))
/* 2873 */           return true; 
/*      */       }
/*      */     }
/* 2876 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isAllyAndMayPerformAction(short action, Creature performer, Village village) {
/* 2881 */     if (village != null)
/*      */     {
/* 2883 */       if (village.isAlly(performer))
/*      */       {
/* 2885 */         if (village.isActionAllowed(action, performer))
/* 2886 */           return true; 
/*      */       }
/*      */     }
/* 2889 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Village getVillageForWall(Creature performer, Wall wall) {
/*      */     try {
/* 2896 */       VolaTile t = wall.getOrCreateInnerTile(true);
/* 2897 */       int tilex = t.tilex;
/* 2898 */       int tiley = t.tiley;
/* 2899 */       return Zones.getVillage(tilex, tiley, wall.isOnSurface());
/*      */     
/*      */     }
/* 2902 */     catch (NoSuchTileException nst) {
/*      */       
/* 2904 */       logger.log(Level.WARNING, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/*      */     }
/* 2906 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 2908 */       logger.log(Level.WARNING, performer.getName() + " " + nsz.getMessage(), (Throwable)nsz);
/*      */     } 
/* 2910 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean improveFence(Action act, Creature performer, Item repairItem, Fence fence, float counter) {
/* 2915 */     if (fence.getQualityLevel() == 100.0F) {
/*      */       
/* 2917 */       performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " does not need improving.");
/* 2918 */       return true;
/*      */     } 
/* 2920 */     if (!fence.isFinished()) {
/*      */       
/* 2922 */       performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " is not finished yet so you can not improve it.");
/* 2923 */       return true;
/*      */     } 
/* 2925 */     if (fence.getDamage() > 0.0F) {
/*      */       
/* 2927 */       performer.getCommunicator().sendNormalServerMessage("The " + fence.getName() + " has damage you need to repair first.");
/* 2928 */       return true;
/*      */     } 
/* 2930 */     if (!Methods.isActionAllowed(performer, (short)116, fence.getTileX(), fence.getTileY()))
/*      */     {
/* 2932 */       return true;
/*      */     }
/*      */     
/* 2935 */     boolean insta = (performer.getPower() >= 5);
/* 2936 */     int templateId = fence.getRepairItemTemplate();
/* 2937 */     if (repairItem.getTemplateId() != templateId && !insta) {
/*      */       
/* 2939 */       performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + fence.getName() + " with that item.");
/* 2940 */       return true;
/*      */     } 
/* 2942 */     if (repairItem.getWeightGrams() < repairItem.getTemplate().getWeightGrams() * ((repairItem.getTemplateId() == 9) ? 0.7F : 1.0F)) {
/*      */       
/* 2944 */       performer.getCommunicator().sendNormalServerMessage("The " + repairItem.getName() + " contains too little material to improve the " + fence.getName() + ".");
/* 2945 */       return true;
/*      */     } 
/*      */     
/* 2948 */     Skill buildSkill = performer.getSkills().getSkillOrLearn(1005);
/* 2949 */     if (fence.isStone() || fence.isSlate() || fence.isMarble() || fence.isPlastered() || fence
/* 2950 */       .isRoundedStone() || fence.isPottery() || fence.isSandstone() || fence.isIron()) {
/* 2951 */       buildSkill = performer.getSkills().getSkillOrLearn(1013);
/*      */     }
/* 2953 */     int time = 400;
/* 2954 */     if (counter == 1.0F) {
/*      */       
/* 2956 */       double power = (repairItem.getCurrentQualityLevel() + buildSkill.getKnowledge(0.0D)) / 2.0D;
/* 2957 */       double diff = power - fence.getQualityLevel();
/* 2958 */       if (diff < 0.0D) {
/*      */         
/* 2960 */         performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + fence.getName() + " with that item and your knowledge.");
/* 2961 */         return true;
/*      */       } 
/*      */       
/* 2964 */       act.setPower((float)(diff * 0.2D));
/*      */       
/* 2966 */       performer.getCommunicator().sendNormalServerMessage("You start to improve the " + fence.getName() + ".");
/* 2967 */       Server.getInstance().broadCastAction(performer.getName() + " starts to improve a " + fence.getName() + ".", performer, 5);
/*      */       
/* 2969 */       time = Actions.getRepairActionTime(performer, buildSkill, 0.0D);
/* 2970 */       performer.sendActionControl(Actions.actionEntrys[192].getVerbString(), true, time);
/* 2971 */       act.setTimeLeft(time);
/*      */       
/* 2973 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     else {
/*      */       
/* 2977 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 2980 */     if (act.mayPlaySound()) {
/*      */       
/* 2982 */       String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 2983 */       if (buildSkill.getNumber() == 1013)
/* 2984 */         s = "sound.work.masonry"; 
/* 2985 */       SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/* 2987 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 2989 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     
/* 2992 */     if (counter * 10.0F >= time || insta) {
/*      */       
/* 2994 */       if (repairItem.isCombine()) {
/*      */         
/* 2996 */         repairItem.setWeight(repairItem.getWeightGrams() - repairItem.getTemplate().getWeightGrams(), false);
/* 2997 */         if (repairItem.getWeightGrams() <= 0)
/*      */         {
/* 2999 */           repairItem.putInVoid();
/* 3000 */           act.setDestroyedItem(repairItem);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 3005 */         repairItem.putInVoid();
/* 3006 */         act.setDestroyedItem(repairItem);
/*      */       } 
/*      */       
/* 3009 */       buildSkill.skillCheck(buildSkill.getKnowledge(0.0D) - 10.0D, repairItem, 0.0D, false, counter);
/* 3010 */       Item destroyed = act.getDestroyedItem();
/* 3011 */       if (destroyed != null)
/* 3012 */         Items.decay(destroyed.getWurmId(), destroyed.getDbStrings()); 
/* 3013 */       act.setDestroyedItem(null);
/*      */       
/* 3015 */       double power = insta ? 100.0D : act.getPower();
/* 3016 */       float min = (performer.getPower() > 0 && performer.getPower() < 5) ? 30.0F : 100.0F;
/*      */       
/* 3018 */       performer.getCommunicator().sendNormalServerMessage("You improve the " + fence.getName() + " a bit.");
/* 3019 */       fence.improveOrigQualityLevel(Math.min(min, (float)(fence.getOriginalQualityLevel() + power)));
/* 3020 */       fence.setQualityLevel(Math.min(min, (float)(fence.getQualityLevel() + power)));
/*      */       
/* 3022 */       Server.getInstance().broadCastAction(performer.getName() + " improves a " + fence.getName() + " a bit.", performer, 5);
/* 3023 */       return true;
/*      */     } 
/*      */     
/* 3026 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean repairFloor(Creature performer, Item repairItem, IFloor floor, float counter, Action act) {
/* 3033 */     if (performer.isFighting()) {
/*      */       
/* 3035 */       performer.getCommunicator().sendNormalServerMessage("You cannot do that while in combat.");
/* 3036 */       return true;
/*      */     } 
/*      */     
/* 3039 */     if (floor.getDamage() == 0.0F) {
/*      */       
/* 3041 */       performer.getCommunicator().sendNormalServerMessage("The " + floor.getName() + " does not need repairing.");
/* 3042 */       return true;
/*      */     } 
/* 3044 */     if (!Methods.isActionAllowed(performer, (short)116, floor.getTileX(), floor.getTileY()))
/*      */     {
/* 3046 */       return true;
/*      */     }
/*      */     
/* 3049 */     boolean insta = (performer.getPower() >= 5);
/* 3050 */     int templateId = floor.getRepairItemTemplate();
/* 3051 */     if (repairItem.getTemplateId() != templateId && !insta) {
/*      */       
/* 3053 */       performer.getCommunicator().sendNormalServerMessage("You cannot repair the " + floor.getName() + " with that item.");
/* 3054 */       return true;
/*      */     } 
/* 3056 */     if (repairItem.getWeightGrams() < repairItem.getTemplate().getWeightGrams()) {
/*      */       
/* 3058 */       performer.getCommunicator().sendNormalServerMessage("The " + repairItem.getName() + " contains too little material to repair the " + floor.getName() + ".");
/* 3059 */       return true;
/*      */     } 
/*      */     
/* 3062 */     Skill buildSkill = performer.getSkills().getSkillOrLearn(getSkillFor(floor));
/* 3063 */     Skill repairSkill = performer.getSkills().getSkillOrLearn(10035);
/*      */     
/* 3065 */     int time = 400;
/* 3066 */     if (counter == 1.0F) {
/*      */       
/* 3068 */       performer.getCommunicator().sendNormalServerMessage("You start to repair the " + floor.getName() + ".");
/* 3069 */       Server.getInstance().broadCastAction(performer.getName() + " starts to repair a " + floor.getName() + ".", performer, 5);
/*      */       
/* 3071 */       time = Actions.getRepairActionTime(performer, repairSkill, buildSkill.getKnowledge(0.0D));
/* 3072 */       performer.sendActionControl(Actions.actionEntrys[193].getVerbString(), true, time);
/* 3073 */       act.setTimeLeft(time);
/* 3074 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     }
/*      */     else {
/*      */       
/* 3078 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 3081 */     if (act.mayPlaySound()) {
/*      */       
/* 3083 */       String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 3084 */       if (floor.isStone() || floor.isMarble() || floor.isSlate() || floor.isSandstone()) {
/* 3085 */         s = "sound.work.masonry";
/* 3086 */       } else if (floor.isMetal()) {
/* 3087 */         s = "sound.work.smithing.metal";
/* 3088 */       }  SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/* 3090 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 3092 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     
/* 3095 */     if (counter * 10.0F >= time || insta) {
/*      */       
/* 3097 */       if (repairItem.isCombine()) {
/*      */         
/* 3099 */         repairItem.setWeight(repairItem.getWeightGrams() - repairItem.getTemplate().getWeightGrams(), false);
/* 3100 */         if (repairItem.getWeightGrams() <= 0)
/*      */         {
/* 3102 */           repairItem.putInVoid();
/* 3103 */           act.setDestroyedItem(repairItem);
/*      */         }
/*      */       
/* 3106 */       } else if (insta) {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */           
/* 3113 */           Structure struct = Structures.getStructure(floor.getStructureId());
/* 3114 */           performer.sendToLoggers("Repairing wall with ID:" + floor.getId() + " part of structure with  ID:" + floor.getStructureId() + " Owned by  " + struct
/* 3115 */               .getOwnerName() + " at " + floor.getTile().toString());
/*      */         }
/* 3117 */         catch (NoSuchStructureException nss) {
/*      */           
/* 3119 */           logger.warning("No such structure on attempting to repair a wall? " + nss);
/*      */         } 
/*      */         
/* 3122 */         floor.setDamage(0.0F);
/* 3123 */         performer.getCommunicator().sendNormalServerMessage("You magically repair the " + floor.getName() + " (ID:" + floor.getId() + ") with your powers.");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 3128 */         repairItem.putInVoid();
/* 3129 */         act.setDestroyedItem(repairItem);
/*      */       } 
/*      */       
/* 3132 */       double power = repairSkill.skillCheck((floor.getDamage() / 10.0F), repairItem, 0.0D, false, counter);
/* 3133 */       Item destroyed = act.getDestroyedItem();
/* 3134 */       if (destroyed != null)
/* 3135 */         Items.decay(destroyed.getWurmId(), destroyed.getDbStrings()); 
/* 3136 */       act.setDestroyedItem(null);
/* 3137 */       if (insta)
/* 3138 */         power = 100.0D; 
/* 3139 */       if (power > 0.0D) {
/*      */         
/* 3141 */         performer.getCommunicator().sendNormalServerMessage("You repair the " + floor.getName() + " a bit.");
/*      */         
/* 3143 */         float cq = floor.getCurrentQualityLevel();
/* 3144 */         float diffcq = floor.getQualityLevel() - cq;
/* 3145 */         float newOrigcq = floor.getQualityLevel() - (float)(diffcq * (100.0D - power)) / 10000.0F;
/* 3146 */         float repairAmnt = 5.0F + (float)(5.0D * (repairSkill.getKnowledge(buildSkill.getKnowledge(0.0D)) + repairItem.getCurrentQualityLevel()) / 200.0D);
/*      */         
/* 3148 */         floor.setQualityLevel(newOrigcq);
/* 3149 */         floor.setDamage(Math.max(0.0F, floor.getDamage() - repairAmnt));
/* 3150 */         performer.achievement(155);
/*      */       }
/* 3152 */       else if (power < -90.0D) {
/*      */         
/* 3154 */         performer.getCommunicator().sendNormalServerMessage("You fail to repair the " + floor.getName() + " and damage it instead!");
/* 3155 */         floor.setDamage((float)Math.min(100.0D, floor.getDamage() - power / 100.0D));
/*      */       }
/*      */       else {
/*      */         
/* 3159 */         performer.getCommunicator().sendNormalServerMessage("You fail to repair the " + floor.getName() + ".");
/*      */       } 
/*      */       
/* 3162 */       floor.setLastUsed(System.currentTimeMillis());
/* 3163 */       Server.getInstance().broadCastAction(performer.getName() + " repairs a " + floor.getName() + " a bit.", performer, 5);
/*      */       
/* 3165 */       return true;
/*      */     } 
/*      */     
/* 3168 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean repairWall(Action act, Creature performer, Item repairItem, Wall wall, float counter) {
/* 3174 */     if (performer.getLayer() != wall.getLayer()) {
/*      */       
/* 3176 */       performer.getCommunicator().sendNormalServerMessage("You cannot reach the wall.");
/* 3177 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 3181 */     if (performer.isFighting()) {
/*      */       
/* 3183 */       performer.getCommunicator().sendNormalServerMessage("You cannot do that while in combat.");
/* 3184 */       return true;
/*      */     } 
/*      */     
/* 3187 */     if (wall.getDamage() == 0.0F) {
/*      */       
/* 3189 */       performer.getCommunicator().sendNormalServerMessage("The wall does not need repairing.");
/* 3190 */       return true;
/*      */     } 
/* 3192 */     if (!Methods.isActionAllowed(performer, (short)116, wall.getTileX(), wall.getTileY()))
/*      */     {
/* 3194 */       return true;
/*      */     }
/*      */     
/* 3197 */     boolean insta = (performer.getPower() >= 4);
/* 3198 */     int templateId = wall.getRepairItemTemplate();
/* 3199 */     if (repairItem.getTemplateId() != templateId && !insta) {
/*      */       
/* 3201 */       performer.getCommunicator().sendNormalServerMessage("You cannot repair the wall with that item.");
/* 3202 */       return true;
/*      */     } 
/* 3204 */     if (repairItem.getWeightGrams() < repairItem.getTemplate().getWeightGrams()) {
/*      */       
/* 3206 */       performer.getCommunicator().sendNormalServerMessage("The " + repairItem.getName() + " contains too little material to repair the wall.");
/* 3207 */       return true;
/*      */     } 
/*      */     
/* 3210 */     Skill buildSkill = performer.getSkills().getSkillOrLearn(1005);
/* 3211 */     if (wall.isStone() || wall.isPlainStone() || wall.isSlate() || wall.isMarble() || wall
/* 3212 */       .isRoundedStone() || wall.isRendered() || wall.isPottery() || wall.isSandstone())
/* 3213 */       buildSkill = performer.getSkills().getSkillOrLearn(1013); 
/* 3214 */     Skill repairSkill = performer.getSkills().getSkillOrLearn(10035);
/*      */     
/* 3216 */     int time = 400;
/* 3217 */     if (counter == 1.0F) {
/*      */       
/* 3219 */       performer.getCommunicator().sendNormalServerMessage("You start to repair the wall.");
/* 3220 */       Server.getInstance().broadCastAction(performer.getName() + " starts to repair a wall.", performer, 5);
/*      */       
/* 3222 */       time = Actions.getRepairActionTime(performer, repairSkill, buildSkill.getKnowledge(0.0D));
/* 3223 */       performer.sendActionControl(Actions.actionEntrys[193].getVerbString(), true, time);
/* 3224 */       act.setTimeLeft(time);
/* 3225 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     }
/*      */     else {
/*      */       
/* 3229 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 3232 */     if (act.mayPlaySound()) {
/*      */       
/* 3234 */       String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 3235 */       if (buildSkill.getNumber() == 1013)
/* 3236 */         s = "sound.work.masonry"; 
/* 3237 */       SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/* 3239 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 3241 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     
/* 3244 */     if (counter * 10.0F >= time || insta) {
/*      */       
/* 3246 */       if (repairItem.isCombine()) {
/*      */         
/* 3248 */         repairItem.setWeight(repairItem.getWeightGrams() - repairItem.getTemplate().getWeightGrams(), false);
/* 3249 */         if (repairItem.getWeightGrams() <= 0)
/*      */         {
/* 3251 */           repairItem.putInVoid();
/* 3252 */           act.setDestroyedItem(repairItem);
/*      */         }
/*      */       
/* 3255 */       } else if (insta) {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */           
/* 3262 */           Structure struct = Structures.getStructure(wall.getStructureId());
/* 3263 */           performer.sendToLoggers("Repairing wall with ID:" + wall.getId() + " part of structure with  ID:" + wall.getStructureId() + " Owned by  " + struct
/* 3264 */               .getOwnerName() + " at " + wall.getTile().toString());
/*      */         }
/* 3266 */         catch (NoSuchStructureException nss) {
/*      */           
/* 3268 */           logger.warning("No such structure on attempting to repair a wall? " + nss);
/*      */         } 
/*      */         
/* 3271 */         wall.setDamage(0.0F);
/* 3272 */         performer.getCommunicator().sendNormalServerMessage("You magically repair the " + wall.getMaterialString() + " wall (ID:" + wall.getId() + ") with your powers.");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 3277 */         repairItem.putInVoid();
/* 3278 */         act.setDestroyedItem(repairItem);
/*      */       } 
/*      */       
/* 3281 */       double power = repairSkill.skillCheck((wall.getDamage() / 10.0F), repairItem, 0.0D, false, counter);
/* 3282 */       Item destroyed = act.getDestroyedItem();
/* 3283 */       if (destroyed != null)
/* 3284 */         Items.decay(destroyed.getWurmId(), destroyed.getDbStrings()); 
/* 3285 */       act.setDestroyedItem(null);
/* 3286 */       if (insta)
/* 3287 */         power = 100.0D; 
/* 3288 */       if (power > 0.0D) {
/*      */         
/* 3290 */         performer.getCommunicator().sendNormalServerMessage("You repair the wall a bit.");
/*      */         
/* 3292 */         float cq = wall.getCurrentQualityLevel();
/* 3293 */         float diffcq = wall.getQualityLevel() - cq;
/* 3294 */         float newOrigcq = wall.getQualityLevel() - (float)(diffcq * (100.0D - power)) / 10000.0F;
/* 3295 */         float repairAmnt = 5.0F + (float)(5.0D * (repairSkill.getKnowledge(buildSkill.getKnowledge(0.0D)) + repairItem.getCurrentQualityLevel()) / 200.0D);
/*      */         
/* 3297 */         wall.setQualityLevel(newOrigcq);
/* 3298 */         wall.setDamage(Math.max(0.0F, wall.getDamage() - repairAmnt));
/* 3299 */         performer.achievement(155);
/*      */       }
/* 3301 */       else if (power < -90.0D) {
/*      */         
/* 3303 */         performer.getCommunicator().sendNormalServerMessage("You fail to repair the wall and damage it instead!");
/* 3304 */         wall.setDamage((float)Math.min(100.0D, wall.getDamage() - power / 100.0D));
/*      */       }
/*      */       else {
/*      */         
/* 3308 */         performer.getCommunicator().sendNormalServerMessage("You fail to repair the wall.");
/*      */       } 
/*      */       
/* 3311 */       wall.setLastUsed(System.currentTimeMillis());
/* 3312 */       Server.getInstance().broadCastAction(performer.getName() + " repairs a wall a bit.", performer, 5);
/*      */       
/* 3314 */       return true;
/*      */     } 
/*      */     
/* 3317 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean improveFloor(Creature performer, Item repairItem, IFloor floor, float counter, Action act) {
/* 3323 */     if (floor.getQualityLevel() == 100.0F) {
/*      */       
/* 3325 */       performer.getCommunicator().sendNormalServerMessage("The " + floor.getName() + " does not need improving.");
/* 3326 */       return true;
/*      */     } 
/* 3328 */     if (!floor.isFinished()) {
/*      */       
/* 3330 */       performer.getCommunicator().sendNormalServerMessage("The " + floor.getName() + " is not finished yet so you can not improve it.");
/* 3331 */       return true;
/*      */     } 
/* 3333 */     if (floor.getDamage() > 0.0F) {
/*      */       
/* 3335 */       performer.getCommunicator().sendNormalServerMessage("The " + floor.getName() + " has damage you need to repair first.");
/* 3336 */       return true;
/*      */     } 
/* 3338 */     if (!Methods.isActionAllowed(performer, (short)116, floor.getTileX(), floor.getTileY()))
/*      */     {
/* 3340 */       return true;
/*      */     }
/*      */     
/* 3343 */     boolean insta = (performer.getPower() >= 5);
/* 3344 */     int templateId = floor.getRepairItemTemplate();
/* 3345 */     if (repairItem.getTemplateId() != templateId && !insta) {
/*      */       
/* 3347 */       performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + floor.getName() + " with that item.");
/* 3348 */       return true;
/*      */     } 
/* 3350 */     if (repairItem.getWeightGrams() < repairItem.getTemplate().getWeightGrams()) {
/*      */       
/* 3352 */       performer.getCommunicator().sendNormalServerMessage("The " + repairItem.getName() + " contains too little material to improve the " + floor.getName() + ".");
/* 3353 */       return true;
/*      */     } 
/*      */     
/* 3356 */     Skill buildSkill = performer.getSkills().getSkillOrLearn(getSkillFor(floor));
/* 3357 */     int time = 400;
/* 3358 */     if (counter == 1.0F) {
/*      */       
/* 3360 */       double power = (repairItem.getCurrentQualityLevel() + buildSkill.getKnowledge(0.0D)) / 2.0D;
/* 3361 */       double diff = power - floor.getQualityLevel();
/* 3362 */       if (diff < 0.0D) {
/*      */         
/* 3364 */         performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + floor.getName() + " with that item and your knowledge.");
/* 3365 */         return true;
/*      */       } 
/*      */       
/* 3368 */       act.setPower((float)(diff * 0.2D));
/*      */       
/* 3370 */       performer.getCommunicator().sendNormalServerMessage("You start to improve the " + floor.getName() + ".");
/* 3371 */       Server.getInstance().broadCastAction(performer.getName() + " starts to improve a " + floor.getName() + ".", performer, 5);
/*      */       
/* 3373 */       time = Actions.getRepairActionTime(performer, buildSkill, 0.0D);
/* 3374 */       performer.sendActionControl(Actions.actionEntrys[192].getVerbString(), true, time);
/* 3375 */       act.setTimeLeft(time);
/*      */       
/* 3377 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     else {
/*      */       
/* 3381 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 3384 */     if (act.mayPlaySound()) {
/*      */       
/* 3386 */       String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 3387 */       if (floor.isStone() || floor.isSlate() || floor.isMarble() || floor.isSandstone()) {
/* 3388 */         s = "sound.work.masonry";
/* 3389 */       } else if (floor.isMetal()) {
/* 3390 */         s = "sound.work.smithing.metal";
/* 3391 */       }  SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/* 3393 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 3395 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     
/* 3398 */     if (counter * 10.0F >= time || insta) {
/*      */       
/* 3400 */       if (repairItem.isCombine()) {
/*      */         
/* 3402 */         repairItem.setWeight(repairItem.getWeightGrams() - repairItem.getTemplate().getWeightGrams(), false);
/* 3403 */         if (repairItem.getWeightGrams() <= 0)
/*      */         {
/* 3405 */           repairItem.putInVoid();
/* 3406 */           act.setDestroyedItem(repairItem);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 3411 */         repairItem.putInVoid();
/* 3412 */         act.setDestroyedItem(repairItem);
/*      */       } 
/*      */       
/* 3415 */       buildSkill.skillCheck(buildSkill.getKnowledge(0.0D) - 10.0D, repairItem, 0.0D, false, counter);
/* 3416 */       double power = ((performer.getPower() >= 4) ? ((performer.getPower() >= 5) ? 100 : 80) : act.getPower());
/*      */       
/* 3418 */       Item destroyed = act.getDestroyedItem();
/* 3419 */       if (destroyed != null)
/* 3420 */         Items.decay(destroyed.getWurmId(), destroyed.getDbStrings()); 
/* 3421 */       act.setDestroyedItem(null);
/*      */       
/* 3423 */       performer.getCommunicator().sendNormalServerMessage("You improve the " + floor.getName() + " a bit.");
/* 3424 */       float min = 100.0F;
/* 3425 */       if (performer.getPower() >= 3 || performer.getPower() == 4)
/* 3426 */         min = 80.0F; 
/* 3427 */       floor.setQualityLevel(Math.min(min, (float)(floor.getQualityLevel() + power)));
/*      */       
/* 3429 */       Server.getInstance().broadCastAction(performer.getName() + " improves the " + floor.getName() + " a bit.", performer, 5);
/* 3430 */       if (performer.getDeity() != null && performer.getDeity().getBuildWallBonus() > 0.0F) {
/* 3431 */         performer.maybeModifyAlignment(0.5F);
/*      */       }
/* 3433 */       return true;
/*      */     } 
/*      */     
/* 3436 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean improveWall(Action act, Creature performer, Item repairItem, Wall wall, float counter) {
/* 3441 */     if (wall.getQualityLevel() == 100.0F) {
/*      */       
/* 3443 */       performer.getCommunicator().sendNormalServerMessage("The " + wall.getName() + " does not need improving.");
/* 3444 */       return true;
/*      */     } 
/* 3446 */     if (!wall.isFinished()) {
/*      */       
/* 3448 */       performer.getCommunicator().sendNormalServerMessage("The " + wall.getName() + " is not finished yet so you can not improve it.");
/* 3449 */       return true;
/*      */     } 
/* 3451 */     if (wall.getDamage() > 0.0F) {
/*      */       
/* 3453 */       performer.getCommunicator().sendNormalServerMessage("The " + wall.getName() + " has damage you need to repair first.");
/* 3454 */       return true;
/*      */     } 
/* 3456 */     if (!Methods.isActionAllowed(performer, (short)116, wall.getTileX(), wall.getTileY()))
/*      */     {
/* 3458 */       return true;
/*      */     }
/*      */     
/* 3461 */     boolean insta = (performer.getPower() >= 5);
/* 3462 */     int templateId = wall.getRepairItemTemplate();
/* 3463 */     if (repairItem.getTemplateId() != templateId && !insta) {
/*      */       
/* 3465 */       performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + wall.getName() + " with that item.");
/* 3466 */       return true;
/*      */     } 
/* 3468 */     if (repairItem.getWeightGrams() < repairItem.getTemplate().getWeightGrams()) {
/*      */       
/* 3470 */       performer.getCommunicator().sendNormalServerMessage("The " + repairItem.getName() + " contains too little material to improve the " + wall.getName() + ".");
/* 3471 */       return true;
/*      */     } 
/*      */     
/* 3474 */     Skill buildSkill = performer.getSkills().getSkillOrLearn(1005);
/* 3475 */     if (wall.isStone() || wall.isPlainStone() || wall.isSlate() || wall.isMarble() || wall
/* 3476 */       .isRoundedStone() || wall.isRendered() || wall.isPottery() || wall.isSandstone()) {
/* 3477 */       buildSkill = performer.getSkills().getSkillOrLearn(1013);
/*      */     }
/* 3479 */     int time = 400;
/* 3480 */     if (counter == 1.0F) {
/*      */       
/* 3482 */       double power = (repairItem.getCurrentQualityLevel() + buildSkill.getKnowledge(0.0D)) / 2.0D;
/* 3483 */       double diff = power - wall.getQualityLevel();
/* 3484 */       if (diff < 0.0D) {
/*      */         
/* 3486 */         performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + wall.getName() + " with that item and your knowledge.");
/* 3487 */         return true;
/*      */       } 
/*      */       
/* 3490 */       act.setPower((float)(diff * 0.2D));
/*      */       
/* 3492 */       performer.getCommunicator().sendNormalServerMessage("You start to improve the " + wall.getName() + ".");
/* 3493 */       Server.getInstance().broadCastAction(performer.getName() + " starts to improve a " + wall.getName() + ".", performer, 5);
/*      */       
/* 3495 */       time = Actions.getRepairActionTime(performer, buildSkill, 0.0D);
/* 3496 */       performer.sendActionControl(Actions.actionEntrys[192].getVerbString(), true, time);
/* 3497 */       act.setTimeLeft(time);
/*      */       
/* 3499 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     else {
/*      */       
/* 3503 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 3506 */     if (act.mayPlaySound()) {
/*      */       
/* 3508 */       String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 3509 */       if (buildSkill.getNumber() == 1013)
/* 3510 */         s = "sound.work.masonry"; 
/* 3511 */       SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/* 3513 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 3515 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     
/* 3518 */     if (counter * 10.0F >= time || insta) {
/*      */       
/* 3520 */       if (!insta) {
/*      */         
/* 3522 */         if (repairItem.isCombine()) {
/* 3523 */           repairItem.setWeight(repairItem.getWeightGrams() - repairItem.getTemplate().getWeightGrams(), false);
/* 3524 */           if (repairItem.getWeightGrams() <= 0)
/*      */           {
/* 3526 */             repairItem.putInVoid();
/* 3527 */             act.setDestroyedItem(repairItem);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 3532 */           repairItem.putInVoid();
/* 3533 */           act.setDestroyedItem(repairItem);
/*      */         } 
/*      */         
/* 3536 */         buildSkill.skillCheck(buildSkill.getKnowledge(0.0D) - 10.0D, repairItem, 0.0D, false, counter);
/* 3537 */         Item destroyed = act.getDestroyedItem();
/* 3538 */         if (destroyed != null)
/* 3539 */           Items.decay(destroyed.getWurmId(), destroyed.getDbStrings()); 
/* 3540 */         act.setDestroyedItem(null);
/*      */       } 
/*      */       
/* 3543 */       double power = insta ? 100.0D : act.getPower();
/* 3544 */       float min = (performer.getPower() > 0 && performer.getPower() < 5) ? 30.0F : 100.0F;
/*      */       
/* 3546 */       performer.getCommunicator().sendNormalServerMessage("You improve the " + wall.getName() + " a bit.");
/* 3547 */       wall.improveOrigQualityLevel(Math.min(min, (float)(wall.getOriginalQualityLevel() + power)));
/* 3548 */       wall.setQualityLevel(Math.min(min, (float)(wall.getQualityLevel() + power)));
/*      */       
/* 3550 */       Server.getInstance().broadCastAction(performer.getName() + " improves a " + wall.getName() + " a bit.", performer, 5);
/* 3551 */       if (performer.getDeity() != null && performer.getDeity().getBuildWallBonus() > 0.0F)
/* 3552 */         performer.maybeModifyAlignment(0.5F); 
/* 3553 */       return true;
/*      */     } 
/*      */     
/* 3556 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean destroyFloor(short action, Creature performer, Item destroyItem, IFloor floor, float counter) {
/* 3562 */     boolean toReturn = false;
/* 3563 */     Structure structure = null;
/* 3564 */     int time = 300;
/* 3565 */     if (performer.getPower() >= 2)
/* 3566 */       time = 15; 
/* 3567 */     float mod = 1.0F;
/* 3568 */     if (destroyItem == null && !performer.isPlayer()) {
/* 3569 */       mod = 0.003F;
/* 3570 */     } else if (destroyItem != null) {
/* 3571 */       mod = floor.getDamageModifierForItem(destroyItem);
/*      */     } else {
/* 3573 */       mod = 0.0F;
/*      */     } 
/* 3575 */     if (!floor.isOnSurface()) {
/* 3576 */       mod *= 1.5F;
/*      */     }
/* 3578 */     if (mod <= 0.0F) {
/*      */       
/* 3580 */       performer.getCommunicator().sendNormalServerMessage("You will not do any damage to the " + floor
/* 3581 */           .getName() + " with that.");
/* 3582 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3588 */       structure = Structures.getStructure(floor.getStructureId());
/*      */     }
/* 3590 */     catch (NoSuchStructureException nss) {
/*      */       
/* 3592 */       if (performer.getPower() > 0)
/* 3593 */         performer.getCommunicator().sendNormalServerMessage("Could not find the structure that " + floor
/* 3594 */             .getName() + " belongs to."); 
/* 3595 */       return true;
/*      */     } 
/*      */     
/* 3598 */     Action act = null;
/*      */     
/*      */     try {
/* 3601 */       act = performer.getCurrentAction();
/*      */     }
/* 3603 */     catch (NoSuchActionException nsa) {
/*      */       
/* 3605 */       logger.log(Level.WARNING, "No Action for " + performer.getName() + "!", (Throwable)nsa);
/* 3606 */       return true;
/*      */     } 
/*      */     
/* 3609 */     if (counter == 1.0F) {
/*      */       
/* 3611 */       if (checkStructureDestruction(performer, structure, floor.getTile())) {
/* 3612 */         return true;
/*      */       }
/* 3614 */       performer.getCommunicator().sendNormalServerMessage("You start to destroy the " + floor.getName() + ".");
/* 3615 */       Server.getInstance().broadCastAction(performer.getName() + " starts to destroy a " + floor.getName() + ".", performer, 5);
/*      */ 
/*      */       
/* 3618 */       performer.sendActionControl(Actions.actionEntrys[action].getVerbString(), true, time);
/* 3619 */       act.setTimeLeft(time);
/* 3620 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3625 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 3628 */     if (act.currentSecond() % 5 == 0) {
/*      */       
/* 3630 */       sendDestroySound(performer, destroyItem, (floor.isThatch() || floor.isWood()));
/*      */       
/* 3632 */       performer.getStatus().modifyStamina(-5000.0F);
/* 3633 */       if (destroyItem != null && 
/* 3634 */         !destroyItem.isBodyPartAttached()) {
/* 3635 */         destroyItem.setDamage(destroyItem.getDamage() + mod * destroyItem.getDamageModifier());
/*      */       }
/*      */     } 
/* 3638 */     if (counter * 10.0F > time) {
/*      */       
/* 3640 */       toReturn = true;
/* 3641 */       boolean citizen = false;
/* 3642 */       Skills skills = performer.getSkills();
/* 3643 */       Skill destroySkill = null;
/*      */       
/*      */       try {
/* 3646 */         destroySkill = skills.getSkill(102);
/*      */       }
/* 3648 */       catch (NoSuchSkillException nss) {
/*      */         
/* 3650 */         destroySkill = skills.learn(102, 1.0F);
/*      */       } 
/*      */       
/* 3653 */       Village vill = floor.getTile().getVillage();
/* 3654 */       double damage = 10.0D;
/* 3655 */       if (destroyItem != null) {
/*      */         
/* 3657 */         destroySkill.skillCheck(20.0D, destroyItem, 0.0D, false, counter);
/* 3658 */         if (floor.isWood() && destroyItem.isCarpentryTool()) {
/* 3659 */           damage = 100.0D * (1.0D + destroySkill.getKnowledge(0.0D) / 100.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/* 3665 */         else if (destroyItem.isWeapon()) {
/* 3666 */           damage = Weapon.getModifiedDamageForWeapon(destroyItem, destroySkill) * 2.0D;
/*      */         } else {
/* 3668 */           damage = floor.getDamageModifierForItem(destroyItem);
/*      */         } 
/* 3670 */         damage *= Weapon.getMaterialBashModifier(destroyItem.getMaterial());
/*      */       } 
/* 3672 */       if (vill != null) {
/*      */         
/* 3674 */         if (isCitizenAndMayPerformAction(action, performer, vill))
/*      */         {
/* 3676 */           damage *= 50.0D;
/* 3677 */           citizen = true;
/*      */         }
/* 3679 */         else if (isAllyAndMayPerformAction(action, performer, vill))
/*      */         {
/* 3681 */           damage *= 25.0D;
/* 3682 */           citizen = true;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 3687 */       else if (Zones.isInPvPZone(floor.getTileX(), floor.getTileY())) {
/* 3688 */         damage *= 10.0D;
/*      */       } 
/*      */ 
/*      */       
/* 3692 */       if (!citizen && performer.getCultist() != null && performer.getCultist().doubleStructDamage()) {
/* 3693 */         damage *= 2.0D;
/*      */       }
/*      */       
/* 3696 */       damage /= (floor.getQualityLevel() / 20.0F);
/*      */       
/* 3698 */       float newdam = (float)((performer.getPower() >= 5) ? (floor.getDamage() + 40.0F) : (floor.getDamage() + damage * mod * 10.0D));
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
/* 3728 */       if (floor.setDamage(newdam))
/*      */       {
/* 3730 */         floor.destroyOrRevertToPlan();
/*      */       }
/*      */       
/* 3733 */       if (newdam >= 100.0F || floor.isAPlan()) {
/*      */         
/* 3735 */         TileEvent.log(floor.getTileX(), floor.getTileY(), 0, performer.getWurmId(), action);
/* 3736 */         performer.getCommunicator().sendNormalServerMessage("The last parts of the " + floor
/* 3737 */             .getName() + " fall down with a crash.");
/* 3738 */         Server.getInstance().broadCastAction(performer
/* 3739 */             .getName() + " damages a " + floor.getName() + " and the last parts fall down with a crash.", performer, 5);
/*      */ 
/*      */         
/* 3742 */         if (performer.getDeity() != null)
/*      */         {
/* 3744 */           performer.performActionOkey(act);
/*      */         }
/* 3746 */         if (structure.isBridgeGone())
/*      */         {
/* 3748 */           performer.getCommunicator().sendNormalServerMessage("The last parts of the bridge fall down with a crash.");
/*      */ 
/*      */           
/* 3751 */           Server.getInstance().broadCastAction(performer
/* 3752 */               .getName() + " cheers as the last parts of the bridge fall down with a crash.", performer, 5);
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 3758 */         performer.getCommunicator().sendNormalServerMessage("You damage the " + floor.getName() + ".");
/* 3759 */         Server.getInstance().broadCastAction(performer.getName() + " damages a " + floor.getName() + ".", performer, 5);
/*      */       } 
/*      */     } 
/* 3762 */     return toReturn;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean checkStructureDestruction(Creature performer, Structure structure, VolaTile tile) {
/* 3972 */     Village village = null;
/* 3973 */     if (tile != null) {
/*      */       
/* 3975 */       village = tile.getVillage();
/* 3976 */       if (village != null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 3981 */         if (village.isActionAllowed((short)82, performer))
/* 3982 */           return false; 
/*      */       }
/*      */     } 
/* 3985 */     if (!Servers.isThisAChaosServer() && performer.getKingdomTemplateId() != 3 && Servers.localServer.HOMESERVER && 
/* 3986 */       !performer.isOnHostileHomeServer()) {
/*      */ 
/*      */       
/* 3989 */       if (!mayModifyStructure(performer, structure, tile, (short)82))
/*      */       {
/* 3991 */         if (!Servers.localServer.isChallengeOrEpicServer() || 
/* 3992 */           Players.getInstance().getKingdomForPlayer(structure.getOwnerId()) == Servers.localServer.KINGDOM)
/*      */         {
/* 3994 */           performer.getCommunicator().sendNormalServerMessage("You need permission in order to make modifications to this structure.");
/*      */           
/* 3996 */           return true;
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     }
/* 4002 */     else if (!mayModifyStructure(performer, structure, tile, (short)82)) {
/*      */       
/* 4004 */       Skills skills = performer.getSkills();
/*      */       
/*      */       try {
/* 4007 */         Skill str = skills.getSkill(102);
/* 4008 */         if (!((str.getRealKnowledge() > 21.0D) ? 1 : 0))
/*      */         {
/* 4010 */           performer.getCommunicator().sendNormalServerMessage("You are too weak to do that.");
/* 4011 */           return true;
/*      */         }
/*      */       
/* 4014 */       } catch (NoSuchSkillException nss) {
/*      */         
/* 4016 */         logger.log(Level.WARNING, "Weird, " + performer.getName() + " has no strength!");
/* 4017 */         performer.getCommunicator().sendNormalServerMessage("You are too weak to do that.");
/* 4018 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 4022 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean destroyWall(short action, Creature performer, Item destroyItem, Wall wall, boolean dealItems, float counter) {
/* 4028 */     boolean toReturn = true;
/* 4029 */     Structure structure = null;
/* 4030 */     int time = 300;
/* 4031 */     if (Servers.localServer.challengeServer) {
/* 4032 */       time = 100;
/*      */     }
/* 4034 */     float mod = 1.0F;
/* 4035 */     if (destroyItem == null && !(performer instanceof Player)) {
/* 4036 */       mod = 0.003F;
/* 4037 */     } else if (destroyItem != null) {
/* 4038 */       mod = wall.getDamageModifierForItem(destroyItem);
/*      */     } else {
/* 4040 */       mod = 0.0F;
/*      */     } 
/* 4042 */     boolean insta = false;
/* 4043 */     if (mod <= 0.0F) {
/*      */       
/* 4045 */       performer.getCommunicator().sendNormalServerMessage("You will not do any damage to the wall with that.");
/* 4046 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 4052 */       structure = Structures.getStructure(wall.getStructureId());
/*      */     }
/* 4054 */     catch (NoSuchStructureException nss) {
/*      */ 
/*      */ 
/*      */       
/* 4058 */       return true;
/*      */     } 
/* 4060 */     toReturn = false;
/* 4061 */     Action act = null;
/* 4062 */     String destString = "destroy";
/* 4063 */     if (dealItems) {
/* 4064 */       destString = "disassemble";
/*      */     }
/*      */     try {
/* 4067 */       act = performer.getCurrentAction();
/*      */     }
/* 4069 */     catch (NoSuchActionException nsa) {
/*      */       
/* 4071 */       logger.log(Level.WARNING, "No Action for " + performer.getName() + "!", (Throwable)nsa);
/* 4072 */       return true;
/*      */     } 
/* 4074 */     if (counter == 1.0F) {
/*      */       
/* 4076 */       if (!wall.isIndoor() && wall.getState() == StructureStateEnum.INITIALIZED) {
/*      */         
/* 4078 */         performer.getCommunicator().sendNormalServerMessage("The " + wall
/* 4079 */             .getName() + " can not be destroyed further.");
/* 4080 */         return true;
/*      */       } 
/* 4082 */       if (checkStructureDestruction(performer, structure, wall.getTile())) {
/* 4083 */         return true;
/*      */       }
/*      */       
/* 4086 */       performer.getCommunicator().sendNormalServerMessage("You start to " + destString + " the wall.");
/* 4087 */       Server.getInstance().broadCastAction(performer.getName() + " starts to " + destString + " a wall.", performer, 5);
/*      */ 
/*      */       
/* 4090 */       performer.sendActionControl(Actions.actionEntrys[action].getVerbString(), true, time);
/* 4091 */       act.setTimeLeft(time);
/* 4092 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*      */     else {
/*      */       
/* 4096 */       time = act.getTimeLeft();
/*      */     } 
/* 4098 */     if (act.currentSecond() % 5 == 0) {
/*      */       
/* 4100 */       sendDestroySound(performer, destroyItem, wall.isWood());
/*      */       
/* 4102 */       performer.getStatus().modifyStamina(-5000.0F);
/* 4103 */       if (destroyItem != null && 
/* 4104 */         !destroyItem.isBodyPartAttached())
/*      */       {
/* 4106 */         destroyItem.setDamage(destroyItem.getDamage() + mod * destroyItem.getDamageModifier());
/*      */       }
/*      */     } 
/* 4109 */     if (counter * 10.0F > time) {
/*      */       
/* 4111 */       Skills skills = performer.getSkills();
/* 4112 */       Skill destroySkill = null;
/*      */       
/*      */       try {
/* 4115 */         destroySkill = skills.getSkill(102);
/*      */       }
/* 4117 */       catch (NoSuchSkillException nss) {
/*      */         
/* 4119 */         destroySkill = skills.learn(102, 1.0F);
/*      */       } 
/* 4121 */       destroySkill.skillCheck(20.0D, destroyItem, 0.0D, false, counter);
/* 4122 */       toReturn = true;
/* 4123 */       boolean citizen = false;
/* 4124 */       double damage = 1.0D;
/* 4125 */       Village vill = getVillageForWall(performer, wall);
/* 4126 */       if (destroyItem != null) {
/*      */         
/* 4128 */         if (wall.isWood() && destroyItem.isCarpentryTool()) {
/* 4129 */           damage = 100.0D * (1.0D + destroySkill.getKnowledge(0.0D) / 100.0D);
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 4135 */           damage = Weapon.getModifiedDamageForWeapon(destroyItem, destroySkill) * 2.0D;
/*      */         } 
/*      */ 
/*      */         
/* 4139 */         damage /= (wall.getQualityLevel() / 20.0F);
/*      */         
/* 4141 */         if (vill != null) {
/*      */           
/* 4143 */           if (isCitizenAndMayPerformAction(action, performer, vill))
/*      */           {
/* 4145 */             damage *= 50.0D;
/* 4146 */             citizen = true;
/*      */           }
/* 4148 */           else if (isAllyAndMayPerformAction(action, performer, vill))
/*      */           {
/* 4150 */             damage *= 25.0D;
/* 4151 */             citizen = true;
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 4156 */         else if (Zones.isInPvPZone(wall.getTileX(), wall.getTileY())) {
/* 4157 */           damage *= 10.0D;
/*      */         } 
/*      */         
/* 4160 */         if (wall.isIndoor()) {
/* 4161 */           damage *= 5.0D;
/*      */         }
/* 4163 */         if (wall.isArched()) {
/* 4164 */           damage *= 3.0D;
/*      */         }
/*      */         
/* 4167 */         if (!wall.isFinished()) {
/*      */           
/* 4169 */           int modifier = (wall.getFinalState()).state - (wall.getState()).state;
/* 4170 */           damage *= modifier;
/*      */         } 
/*      */         
/* 4173 */         if (!wall.isOnSurface())
/* 4174 */           damage *= 1.5D; 
/* 4175 */         damage *= Weapon.getMaterialBashModifier(destroyItem.getMaterial());
/*      */         
/* 4177 */         if (!citizen && performer.getCultist() != null && performer.getCultist().doubleStructDamage()) {
/* 4178 */           damage *= 2.0D;
/*      */         }
/*      */       } 
/* 4181 */       float newdam = (float)(wall.getDamage() + damage * mod * 10.0D);
/*      */       
/* 4183 */       wall.setDamage(newdam);
/*      */       
/* 4185 */       if (newdam >= 100.0F || wall.getState() == StructureStateEnum.INITIALIZED) {
/*      */         
/* 4187 */         TileEvent.log(wall.getTileX(), wall.getTileY(), 0, performer.getWurmId(), action);
/* 4188 */         performer.getCommunicator().sendNormalServerMessage("The last parts of the wall fall down with a crash.");
/* 4189 */         Server.getInstance().broadCastAction(performer
/* 4190 */             .getName() + " damages a wall and the last parts fall down with a crash.", performer, 5);
/* 4191 */         if (performer.getDeity() != null)
/*      */         {
/* 4193 */           performer.performActionOkey(act);
/*      */         }
/* 4195 */         if (structure != null)
/*      */         {
/* 4197 */           if (!structure.hasWalls())
/*      */           {
/* 4199 */             structure.totallyDestroy();
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*      */             
/* 4205 */             structure.updateStructureFinishFlag();
/*      */           }
/*      */         
/*      */         }
/*      */       } else {
/*      */         
/* 4211 */         performer.getCommunicator().sendNormalServerMessage("You damage the wall.");
/* 4212 */         Server.getInstance().broadCastAction(performer.getName() + " damages a wall.", performer, 5);
/*      */       } 
/*      */     } 
/*      */     
/* 4216 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sendDestroySound(Creature performer, Item item, boolean wallIsWood) {
/* 4221 */     String sound = "sound.destroywall.wood.axe";
/* 4222 */     if (item.isWeaponCrush() && !wallIsWood) {
/* 4223 */       sound = "sound.destroywall.stone.maul";
/* 4224 */     } else if (item.isWeaponCrush()) {
/* 4225 */       sound = "sound.destroywall.wood.maul";
/* 4226 */     } else if (!wallIsWood) {
/* 4227 */       sound = "sound.destroywall.stone.axe";
/* 4228 */     }  Methods.sendSound(performer, sound);
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean colorWall(Creature performer, Item colour, Wall target, Action act) {
/* 4233 */     boolean done = true;
/* 4234 */     int colourNeeded = 1000;
/* 4235 */     boolean insta = (performer.getPower() >= 5);
/* 4236 */     if (!insta && 1000 > colour.getWeightGrams()) {
/* 4237 */       performer.getCommunicator().sendNormalServerMessage("You need more dye to paint the " + target.getName() + " - at least " + 'Ϩ' + "g.");
/*      */     }
/*      */     else {
/*      */       
/* 4241 */       done = false;
/* 4242 */       if (act.currentSecond() == 1) {
/*      */         
/* 4244 */         performer.getCommunicator().sendNormalServerMessage("You start to paint the " + target.getName() + ".");
/* 4245 */         Server.getInstance().broadCastAction(performer.getName() + " starts to paint a " + target.getName() + ".", performer, 5);
/*      */         
/* 4247 */         performer.sendActionControl(Actions.actionEntrys[231].getVerbString(), true, 100);
/*      */ 
/*      */       
/*      */       }
/* 4251 */       else if (insta || act.getCounterAsFloat() >= 10.0F) {
/*      */         
/* 4253 */         done = true;
/* 4254 */         target.setColor(colour.getColor());
/* 4255 */         colour.setWeight(colour.getWeightGrams() - 1000, true);
/* 4256 */         performer.getCommunicator().sendNormalServerMessage("You put some colour on the " + target.getName() + ".");
/*      */       } 
/*      */     } 
/*      */     
/* 4260 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean removeColor(Creature performer, Item brush, Wall target, Action act) {
/* 4265 */     boolean done = true;
/* 4266 */     if (brush.getTemplateId() == 441) {
/*      */       
/* 4268 */       boolean insta = (performer.getPower() >= 5);
/* 4269 */       if (!insta && target.getColor() == -1) {
/* 4270 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " has no paint.");
/*      */       } else {
/*      */         
/* 4273 */         done = false;
/* 4274 */         if (act.currentSecond() == 1) {
/*      */           
/* 4276 */           act.setTimeLeft(150);
/* 4277 */           performer.getCommunicator().sendNormalServerMessage("You start to brush the " + target.getName() + ".");
/* 4278 */           Server.getInstance().broadCastAction(performer.getName() + " starts to brush a " + target.getName() + ".", performer, 5);
/*      */           
/* 4280 */           performer.sendActionControl(Actions.actionEntrys[232].getVerbString(), true, act
/* 4281 */               .getTimeLeft());
/*      */         }
/*      */         else {
/*      */           
/* 4285 */           int timeleft = act.getTimeLeft();
/* 4286 */           if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*      */             
/* 4288 */             done = true;
/* 4289 */             target.setColor(-1);
/* 4290 */             brush.setDamage((float)(brush.getDamage() + 0.5D * brush.getDamageModifier()));
/* 4291 */             performer.getCommunicator().sendNormalServerMessage("You remove the paint from the " + target
/* 4292 */                 .getName() + ".");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 4298 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the " + brush
/* 4299 */           .getName() + " to remove the paint.");
/* 4300 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean colorFence(Creature performer, Item colour, Fence target, Action act) {
/* 4305 */     boolean done = true;
/* 4306 */     int colourNeeded = 1000;
/* 4307 */     boolean insta = (performer.getPower() >= 5);
/* 4308 */     if (!insta && 1000 > colour.getWeightGrams()) {
/* 4309 */       performer.getCommunicator().sendNormalServerMessage("You need more dye to paint the " + target.getName() + " - at least " + 'Ϩ' + "g.");
/*      */     }
/*      */     else {
/*      */       
/* 4313 */       done = false;
/* 4314 */       if (act.currentSecond() == 1) {
/*      */         
/* 4316 */         performer.getCommunicator().sendNormalServerMessage("You start to paint the " + target.getName() + ".");
/* 4317 */         Server.getInstance().broadCastAction(performer.getName() + " starts to paint a " + target.getName() + ".", performer, 5);
/*      */         
/* 4319 */         performer.sendActionControl(Actions.actionEntrys[231].getVerbString(), true, 100);
/*      */ 
/*      */       
/*      */       }
/* 4323 */       else if (insta || act.getCounterAsFloat() >= 10.0F) {
/*      */         
/* 4325 */         done = true;
/* 4326 */         target.setColor(colour.getColor());
/* 4327 */         colour.setWeight(colour.getWeightGrams() - 1000, true);
/* 4328 */         performer.getCommunicator().sendNormalServerMessage("You put some colour on the " + target.getName() + ".");
/*      */       } 
/*      */     } 
/*      */     
/* 4332 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean removeColor(Creature performer, Item brush, Fence target, Action act) {
/* 4337 */     boolean done = true;
/* 4338 */     if (brush.getTemplateId() == 441) {
/*      */       
/* 4340 */       boolean insta = (performer.getPower() >= 5);
/* 4341 */       if (!insta && target.getColor() == -1) {
/* 4342 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " has no paint.");
/*      */       } else {
/*      */         
/* 4345 */         done = false;
/* 4346 */         if (act.currentSecond() == 1) {
/*      */           
/* 4348 */           act.setTimeLeft(150);
/* 4349 */           performer.getCommunicator().sendNormalServerMessage("You start to brush the " + target.getName() + ".");
/* 4350 */           Server.getInstance().broadCastAction(performer.getName() + " starts to brush a " + target.getName() + ".", performer, 5);
/*      */           
/* 4352 */           performer.sendActionControl(Actions.actionEntrys[232].getVerbString(), true, act
/* 4353 */               .getTimeLeft());
/*      */         }
/*      */         else {
/*      */           
/* 4357 */           int timeleft = act.getTimeLeft();
/* 4358 */           if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*      */             
/* 4360 */             done = true;
/* 4361 */             target.setColor(-1);
/* 4362 */             brush.setDamage((float)(brush.getDamage() + 0.5D * brush.getDamageModifier()));
/* 4363 */             performer.getCommunicator().sendNormalServerMessage("You remove the paint from the " + target
/* 4364 */                 .getName() + ".");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 4370 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the " + brush
/* 4371 */           .getName() + " to remove the paint.");
/* 4372 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final int getImproveItem(byte minedoortype) {
/* 4377 */     if (minedoortype == Tiles.Tile.TILE_MINE_DOOR_GOLD.id)
/* 4378 */       return 44; 
/* 4379 */     if (minedoortype == Tiles.Tile.TILE_MINE_DOOR_STEEL.id)
/* 4380 */       return 205; 
/* 4381 */     if (minedoortype == Tiles.Tile.TILE_MINE_DOOR_SILVER.id)
/* 4382 */       return 45; 
/* 4383 */     if (minedoortype == Tiles.Tile.TILE_MINE_DOOR_STONE.id)
/* 4384 */       return 146; 
/* 4385 */     return 22;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean improveTileDoor(Creature performer, Item repairItem, int tilex, int tiley, int tile, Action act, float counter) {
/* 4391 */     boolean insta = (performer.getPower() >= 5);
/* 4392 */     int templateId = getImproveItem(Tiles.decodeType(tile));
/* 4393 */     if (repairItem.getTemplateId() != templateId && !insta) {
/*      */       
/* 4395 */       performer.getCommunicator().sendNormalServerMessage("You cannot improve the mine door with that item.");
/* 4396 */       return true;
/*      */     } 
/* 4398 */     if (repairItem.getWeightGrams() < repairItem.getTemplate().getWeightGrams()) {
/*      */       
/* 4400 */       performer.getCommunicator().sendNormalServerMessage("The " + repairItem.getName() + " contains too little material to improve the mine door.");
/* 4401 */       return true;
/*      */     } 
/* 4403 */     if (!insta && 
/* 4404 */       repairItem.isCombine() && repairItem.isMetal() && 
/* 4405 */       repairItem.getTemperature() < 3500) {
/* 4406 */       performer.getCommunicator().sendNormalServerMessage("Metal needs to be glowing hot while improving mine doors.");
/* 4407 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 4412 */     int strength = Server.getWorldResource(tilex, tiley);
/* 4413 */     double mineQuality = (strength <= 100) ? 1.0D : (strength / 100);
/*      */     
/* 4415 */     int skillnum = 10015;
/* 4416 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MINE_DOOR_STONE.id) {
/* 4417 */       skillnum = 1013;
/* 4418 */     } else if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MINE_DOOR_WOOD.id) {
/* 4419 */       skillnum = 1005;
/*      */     } 
/* 4421 */     Skill buildSkill = performer.getSkills().getSkillOrLearn(skillnum);
/* 4422 */     int time = 400;
/* 4423 */     if (counter == 1.0F) {
/*      */       
/* 4425 */       double power = buildSkill.skillCheck(mineQuality, repairItem, 0.0D, true, 1.0F);
/*      */       
/* 4427 */       float imbueEnhancement = 1.0F + repairItem.getSkillSpellImprovement(skillnum) / 100.0F;
/* 4428 */       double improveBonus = 0.23047D * imbueEnhancement;
/* 4429 */       float improveItemBonus = ItemBonus.getImproveSkillMaxBonus(performer);
/* 4430 */       double max = buildSkill.getKnowledge(0.0D) * improveItemBonus + (100.0D - buildSkill.getKnowledge(0.0D) * improveItemBonus) * improveBonus;
/*      */       
/* 4432 */       double diff = Math.max(0.0D, max - mineQuality);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4437 */       double mod = (100.0D - mineQuality) / 20.0D / 100.0D * (Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat()) / 2.0D;
/*      */       
/* 4439 */       if (power < 0.0D) {
/*      */         
/* 4441 */         act.setPower((float)(-mod * Math.max(1.0D, diff)));
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 4447 */         if (diff <= 0.0D) {
/* 4448 */           mod *= 0.009999999776482582D;
/*      */         }
/* 4450 */         act.setPower((float)(mod * Math.max(1.0D, diff)));
/*      */       } 
/*      */       
/* 4453 */       if (repairItem.getCurrentQualityLevel() <= mineQuality) {
/*      */         
/* 4455 */         performer.getCommunicator().sendNormalServerMessage("The " + repairItem
/* 4456 */             .getName() + " is in too poor shape to improve the mine door.");
/* 4457 */         return true;
/*      */       } 
/*      */       
/* 4460 */       performer.getCommunicator().sendNormalServerMessage("You start to strengthen the mine door.");
/* 4461 */       Server.getInstance().broadCastAction(performer.getName() + " starts to strengthen a mine door.", performer, 5);
/*      */       
/* 4463 */       time = Actions.getImproveActionTime(performer, repairItem) * 2;
/* 4464 */       performer.sendActionControl(Actions.actionEntrys[192].getVerbString(), true, time);
/* 4465 */       act.setTimeLeft(time);
/* 4466 */       performer.getStatus().modifyStamina(-counter * 1000.0F);
/*      */     }
/*      */     else {
/*      */       
/* 4470 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 4473 */     if (act.mayPlaySound()) {
/*      */       
/* 4475 */       String s = "sound.work.smithing.hammer";
/* 4476 */       if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MINE_DOOR_STONE.id) {
/* 4477 */         s = "sound.work.masonry";
/* 4478 */       } else if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MINE_DOOR_WOOD.id) {
/* 4479 */         s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/*      */       } 
/* 4481 */       SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/*      */     
/* 4484 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 4486 */       buildSkill.skillCheck(buildSkill.getKnowledge(0.0D) - 10.0D, repairItem, 0.0D, false, counter);
/* 4487 */       double power = act.getPower();
/* 4488 */       if (insta) {
/* 4489 */         power = 5.0D;
/*      */       }
/* 4491 */       if (power > 0.0D) {
/*      */         
/* 4493 */         performer.getCommunicator().sendNormalServerMessage("You strengthen the mine door a bit.");
/* 4494 */         Server.getInstance().broadCastAction(performer.getName() + " strengthens the mine door a bit.", performer, 5);
/* 4495 */         Server.setWorldResource(tilex, tiley, Math.min(10000, strength + (int)Math.round(power * 100.0D)));
/*      */       } else {
/* 4497 */         performer.getCommunicator().sendNormalServerMessage("You fail to strengthen the mine door.");
/* 4498 */         Server.getInstance().broadCastAction(performer.getName() + " fails to strengthen the mine door.", performer, 5);
/*      */       } 
/*      */ 
/*      */       
/* 4502 */       if (repairItem.isCombine()) {
/* 4503 */         repairItem.setWeight(repairItem.getWeightGrams() - repairItem.getTemplate().getWeightGrams(), true);
/*      */       } else {
/* 4505 */         Items.destroyItem(repairItem.getWurmId());
/*      */       } 
/*      */       
/* 4508 */       return true;
/*      */     } 
/*      */     
/* 4511 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean picklock(Creature performer, Item lockpick, Door target, String name, float counter, Action act) {
/* 4517 */     long lockId = -10L;
/* 4518 */     boolean done = false;
/*      */     
/*      */     try {
/* 4521 */       lockId = target.getLockId();
/*      */     }
/* 4523 */     catch (NoSuchLockException nsl) {
/*      */       
/* 4525 */       performer.getCommunicator().sendNormalServerMessage("The " + name + " has no lock to pick.", (byte)3);
/* 4526 */       return true;
/*      */     } 
/* 4528 */     if (lockpick.getTemplateId() != 463) {
/*      */       
/* 4530 */       performer.getCommunicator()
/* 4531 */         .sendNormalServerMessage("The " + lockpick.getName() + " can not be used as a lockpick.", (byte)3);
/* 4532 */       return true;
/*      */     } 
/* 4534 */     if (target.isNotLockpickable()) {
/*      */       
/* 4536 */       performer.getCommunicator().sendNormalServerMessage("The " + name + " can not be lockpicked.", (byte)3);
/* 4537 */       return true;
/*      */     } 
/* 4539 */     if (target.getLockCounter() > 0) {
/*      */       
/* 4541 */       performer.getCommunicator().sendNormalServerMessage("The " + name + " has already been lockpicked.", (byte)3);
/* 4542 */       return true;
/*      */     } 
/*      */     
/* 4545 */     boolean insta = (performer.getPower() >= 5 || (Servers.localServer.testServer && performer.getPower() > 1));
/* 4546 */     Skill lockpicking = null;
/* 4547 */     Skills skills = performer.getSkills();
/*      */     
/*      */     try {
/* 4550 */       lockpicking = skills.getSkill(10076);
/*      */     }
/* 4552 */     catch (NoSuchSkillException nss) {
/*      */       
/* 4554 */       lockpicking = skills.learn(10076, 1.0F);
/*      */     } 
/* 4556 */     int time = 300;
/* 4557 */     if (counter == 1.0F) {
/*      */       
/* 4559 */       for (Player p : Players.getInstance().getPlayers()) {
/*      */         
/* 4561 */         if (p.getWurmId() != performer.getWurmId()) {
/*      */           
/*      */           try {
/* 4564 */             Action pact = p.getCurrentAction();
/* 4565 */             if (act.getNumber() == pact.getNumber() && act.getTarget() == pact.getTarget())
/*      */             {
/* 4567 */               performer.getCommunicator().sendNormalServerMessage("The " + name + " is already being picked by " + p
/* 4568 */                   .getName() + ".", (byte)3);
/* 4569 */               return true;
/*      */             }
/*      */           
/* 4572 */           } catch (NoSuchActionException noSuchActionException) {}
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 4579 */         Item lock = Items.getItem(lockId);
/* 4580 */         if (lock.getQualityLevel() - lockpick.getQualityLevel() > 20.0F)
/*      */         {
/* 4582 */           performer.getCommunicator().sendNormalServerMessage("You need a more advanced lock pick for this high quality lock.", (byte)3);
/*      */           
/* 4584 */           return true;
/*      */         }
/*      */       
/* 4587 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 4589 */         performer.getCommunicator().sendNormalServerMessage("There is no lock to pick.", (byte)3);
/* 4590 */         logger.log(Level.WARNING, "No such lock, but it should be locked." + nsi.getMessage(), (Throwable)nsi);
/* 4591 */         return true;
/*      */       } 
/* 4593 */       time = Actions.getPickActionTime(performer, lockpicking, lockpick, 0.0D);
/* 4594 */       act.setTimeLeft(time);
/*      */       
/* 4596 */       performer.getCommunicator().sendNormalServerMessage("You start to pick the lock of the " + name + ".");
/* 4597 */       Server.getInstance().broadCastAction(performer.getName() + " starts to pick the lock of the " + name + ".", performer, 5);
/*      */       
/* 4599 */       if (target.isLocked()) {
/* 4600 */         performer.sendActionControl("picking lock", true, time);
/*      */       } else {
/* 4602 */         performer.sendActionControl("locking", true, time);
/* 4603 */       }  performer.getStatus().modifyStamina(-2000.0F);
/*      */     }
/*      */     else {
/*      */       
/* 4607 */       time = act.getTimeLeft();
/*      */     } 
/* 4609 */     if (act.currentSecond() == 2)
/*      */     {
/* 4611 */       MethodsItems.checkLockpickBreakage(performer, lockpick, 100, 80.0D);
/*      */     }
/* 4613 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 4615 */       boolean dryRun = false;
/* 4616 */       performer.getStatus().modifyStamina(-2000.0F);
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
/*      */       try {
/* 4641 */         Item lock = Items.getItem(lockId);
/* 4642 */         double bonus = (100.0F * Item.getMaterialLockpickBonus(lockpick.getMaterial()));
/* 4643 */         int breakBonus = (int)(bonus * 2.0D);
/*      */         
/* 4645 */         bonus = Math.min(99.0D, bonus);
/* 4646 */         done = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4652 */         float wallQl = target.getQualityLevel();
/* 4653 */         if (performer.getPower() <= 1 || Servers.localServer.testServer) {
/*      */           
/* 4655 */           Village village = Zones.getVillage(act.getTileX(), act.getTileY(), performer.isOnSurface());
/* 4656 */           if (village != null) {
/*      */             
/* 4658 */             if (!village.isActionAllowed(act.getNumber(), performer))
/*      */             {
/* 4660 */               if (Action.checkLegalMode(performer))
/* 4661 */                 return true; 
/* 4662 */               if (MethodsItems.setTheftEffects(performer, act, act.getTileX(), act.getTileY(), performer
/* 4663 */                   .isOnSurface()))
/*      */               {
/* 4665 */                 double d = lockpicking.skillCheck((lock.getCurrentQualityLevel() / 3.0F + wallQl / 3.0F), lockpick, bonus, false, 10.0F);
/*      */ 
/*      */                 
/* 4668 */                 MethodsItems.checkLockpickBreakage(performer, lockpick, breakBonus, d);
/* 4669 */                 return true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 4695 */           else if (MethodsItems.setTheftEffects(performer, act, act.getTileX(), act.getTileY(), performer
/* 4696 */               .isOnSurface())) {
/*      */ 
/*      */             
/* 4699 */             double d = lockpicking.skillCheck((lock.getCurrentQualityLevel() + wallQl / 3.0F), lockpick, bonus, false, 10.0F);
/*      */             
/* 4701 */             MethodsItems.checkLockpickBreakage(performer, lockpick, breakBonus, d);
/* 4702 */             return true;
/*      */           } 
/*      */         } 
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
/* 4722 */         float rarityMod = 1.0F;
/* 4723 */         if (lock.getRarity() > 0) {
/* 4724 */           rarityMod += lock.getRarity() * 0.2F;
/*      */         }
/* 4726 */         if (lockpick.getRarity() > 0)
/* 4727 */           rarityMod -= lockpick.getRarity() * 0.1F; 
/* 4728 */         double power = lockpicking.skillCheck(lock.getCurrentQualityLevel(), lockpick, bonus, false, 10.0F);
/*      */ 
/*      */         
/* 4731 */         float chance = MethodsItems.getPickChance(wallQl, lockpick.getCurrentQualityLevel(), lock.getCurrentQualityLevel(), (float)lockpicking.getRealKnowledge(), (byte)1) / rarityMod * (1.0F + Item.getMaterialLockpickBonus(lockpick.getMaterial()));
/* 4732 */         if (Server.rand.nextFloat() * 100.0F < chance) {
/*      */           
/* 4734 */           String opentime = "";
/*      */ 
/*      */           
/* 4737 */           target.setLockCounter((short)1200);
/*      */           
/* 4739 */           opentime = " It will stay unlocked for 10 minutes.";
/* 4740 */           PermissionsHistories.addHistoryEntry(target.getWurmId(), System.currentTimeMillis(), performer
/* 4741 */               .getWurmId(), performer.getName(), "Lock Picked");
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
/* 4763 */           performer.getCommunicator().sendNormalServerMessage("You pick the lock of the " + name + " and unlock it." + opentime);
/*      */           
/* 4765 */           Server.getInstance().broadCastAction(performer
/* 4766 */               .getName() + " picks the lock of the " + name + " and unlocks it.", performer, 5);
/* 4767 */           TileEvent.log(performer.getTileX(), performer.getTileY(), performer.isOnSurface() ? 0 : -1, performer
/* 4768 */               .getWurmId(), 101);
/* 4769 */           performer.achievement(111);
/*      */         }
/*      */         else {
/*      */           
/* 4773 */           performer.getCommunicator().sendNormalServerMessage("You fail to pick the lock of the " + name + ".", (byte)3);
/*      */           
/* 4775 */           Server.getInstance().broadCastAction(performer
/* 4776 */               .getName() + " silently curses as " + performer.getHeSheItString() + " fails to pick the lock of the " + name + ".", performer, 5);
/*      */         } 
/*      */ 
/*      */         
/* 4780 */         if (power > 0.0D) {
/* 4781 */           MethodsItems.checkLockpickBreakage(performer, lockpick, breakBonus, 100.0D);
/*      */         } else {
/* 4783 */           MethodsItems.checkLockpickBreakage(performer, lockpick, breakBonus, -100.0D);
/*      */         } 
/* 4785 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 4787 */         performer.getCommunicator().sendNormalServerMessage("There is no lock to pick.", (byte)3);
/* 4788 */         logger.log(Level.WARNING, "No such lock, but it should be locked." + nsi.getMessage(), (Throwable)nsi);
/* 4789 */         return true;
/*      */       } 
/*      */     } 
/* 4792 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final void addLockPickEntry(Creature performer, Item source, Door target, boolean isFence, Item lock, List<ActionEntry> toReturn) {
/* 4798 */     float rarityMod = 1.0F;
/* 4799 */     if (lock.getRarity() > 0)
/* 4800 */       rarityMod += lock.getRarity() * 0.2F; 
/* 4801 */     if (source.getRarity() > 0) {
/* 4802 */       rarityMod -= source.getRarity() * 0.1F;
/*      */     }
/*      */ 
/*      */     
/* 4806 */     float difficulty = MethodsItems.getPickChance(target.getQualityLevel(), source.getCurrentQualityLevel(), lock.getCurrentQualityLevel(), (float)performer.getLockPickingSkillVal(), isFence ? 3 : 1) / rarityMod * (1.0F + Item.getMaterialLockpickBonus(source.getMaterial()));
/* 4807 */     String pick = "Pick lock: " + difficulty + "%";
/* 4808 */     toReturn.add(new ActionEntry((short)101, pick, "picking lock"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final void addLockUnlockEntry(Creature performer, Wall wall, List<ActionEntry> toReturn) {
/* 4815 */     Door door = wall.getDoor();
/*      */     
/* 4817 */     if (door == null) {
/*      */       return;
/*      */     }
/* 4820 */     long structureId = wall.getStructureId();
/* 4821 */     Structure structure = null;
/*      */     
/*      */     try {
/* 4824 */       structure = Structures.getStructure(structureId);
/*      */     }
/* 4826 */     catch (NoSuchStructureException nse) {
/*      */       
/* 4828 */       structure = null;
/* 4829 */       logger.log(Level.WARNING, "Wall " + wall.getId() + " missing structure: getStructure('" + structureId + "')");
/*      */     } 
/*      */     
/* 4832 */     if (door.isLocked()) {
/*      */       
/* 4834 */       toReturn.add(new ActionEntry((short)102, "Unlock door", "unlocking door"));
/*      */     }
/*      */     else {
/*      */       
/* 4838 */       toReturn.add(new ActionEntry((short)28, "Lock door", "unlocking door"));
/*      */     } 
/*      */     
/* 4841 */     if (structure.getOwnerId() == performer.getWurmId())
/*      */     {
/* 4843 */       toReturn.add(new ActionEntry((short)59, "Rename door", "renaming door"));
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
/*      */   public static final boolean isWallInsideStructure(Wall wall, boolean surfaced) {
/* 4863 */     if (wall.getFloorLevel() > 0)
/*      */     {
/* 4865 */       return true;
/*      */     }
/* 4867 */     int x = Math.min(wall.getStartX(), wall.getEndX());
/* 4868 */     int y = Math.min(wall.getStartY(), wall.getEndY());
/* 4869 */     Tiles.TileBorderDirection dir = (wall.getStartY() == wall.getEndY()) ? Tiles.TileBorderDirection.DIR_HORIZ : Tiles.TileBorderDirection.DIR_DOWN;
/*      */     
/* 4871 */     VolaTile vtile = Zones.getTileOrNull(x, y, surfaced);
/* 4872 */     Structure structure = null;
/* 4873 */     if (vtile != null)
/*      */     {
/* 4875 */       structure = vtile.getStructure();
/*      */     }
/*      */ 
/*      */     
/* 4879 */     if (structure == null) {
/* 4880 */       return false;
/*      */     }
/* 4882 */     structure = null;
/* 4883 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*      */ 
/*      */       
/* 4886 */       vtile = Zones.getTileOrNull(x - 1, y, surfaced);
/* 4887 */       if (vtile != null) {
/* 4888 */         structure = vtile.getStructure();
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 4893 */       vtile = Zones.getTileOrNull(x, y - 1, surfaced);
/* 4894 */       if (vtile != null) {
/* 4895 */         structure = vtile.getStructure();
/*      */       }
/*      */     } 
/* 4898 */     return (structure != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isBorderInsideStructure(int x, int y, Tiles.TileBorderDirection dir, boolean surfaced) {
/* 4904 */     return (getStructureOrNullAtTileBorder(x, y, dir, surfaced) == null);
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
/*      */   public static boolean floorPlanAbove(Creature performer, Item source, int tilex, int tiley, int encodedTile, int layer, float counter, Action act, StructureConstants.FloorType floorType) {
/* 4932 */     return floorPlan(performer, source, tilex, tiley, encodedTile, layer, counter, act, (short)30, floorType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean floorPlanBelow(Creature performer, Item source, int tilex, int tiley, int encodedTile, int layer, float counter, Action act) {
/* 4938 */     return floorPlan(performer, source, tilex, tiley, encodedTile, layer, counter, act, (short)0, StructureConstants.FloorType.FLOOR);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean floorPlanRoof(Creature performer, Item source, int tilex, int tiley, int encodedTile, int layer, float counter, Action act) {
/* 4949 */     return floorPlan(performer, source, tilex, tiley, encodedTile, layer, counter, act, (short)30, StructureConstants.FloorType.ROOF);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean hasSupportAtTile(Creature performer, int tilex, int tiley, Structure structure, int heightOffset) {
/* 4955 */     Floor[] floors = structure.getFloorsAtTile(tilex, tiley, heightOffset - 30, heightOffset + 30);
/*      */     
/* 4957 */     for (Floor floor : floors) {
/*      */       
/* 4959 */       if (floor.getHeightOffset() == heightOffset && floor.isFinished())
/*      */       {
/* 4961 */         return true;
/*      */       }
/*      */     } 
/* 4964 */     return false;
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
/*      */   public static boolean floorPlan(Creature performer, Item source, int tilex, int tiley, int encodedTile, int layer, float counter, Action act, short heightOffsetFromPerformer, StructureConstants.FloorType floorType) {
/* 4987 */     boolean surfaced = (layer >= 0);
/*      */ 
/*      */     
/* 4990 */     VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, (layer != -1));
/* 4991 */     if (vtile == null) {
/*      */       
/* 4993 */       performer.getCommunicator().sendNormalServerMessage("Your sensitive mind notices an anomaly in the fabric of space.");
/*      */       
/* 4995 */       return true;
/*      */     } 
/*      */     
/* 4998 */     if (!Terraforming.isFlat(tilex, tiley, surfaced, 0)) {
/*      */       
/* 5000 */       performer.getCommunicator().sendNormalServerMessage("You cannot build that here. The ground needs to be absolutely flat!");
/*      */       
/* 5002 */       return true;
/*      */     } 
/*      */     
/* 5005 */     Structure structure = vtile.getStructure();
/*      */     
/* 5007 */     if (structure == null) {
/*      */       
/* 5009 */       performer.getCommunicator().sendNormalServerMessage("There is no structure here to remodel.");
/* 5010 */       return true;
/*      */     } 
/*      */     
/* 5013 */     if (!structure.isFinalFinished()) {
/*      */       
/* 5015 */       performer.getCommunicator().sendNormalServerMessage("You need to finish the outer walls on the first floor before adding floors or roof to this structure.");
/*      */       
/* 5017 */       return true;
/*      */     } 
/*      */     
/* 5020 */     if (!structure.isTypeHouse()) {
/*      */       
/* 5022 */       performer.getCommunicator().sendNormalServerMessage("You can only build floors and roofs in a buildinge.");
/* 5023 */       return true;
/*      */     } 
/*      */     
/* 5026 */     if (!mayModifyStructure(performer, structure, vtile, (short)116)) {
/*      */       
/* 5028 */       performer.getCommunicator().sendNormalServerMessage("You need permission in order to make modifications to this structure.");
/*      */       
/* 5030 */       return true;
/*      */     } 
/* 5032 */     if (!Methods.isActionAllowed(performer, (short)116, tilex, tiley))
/*      */     {
/* 5034 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5041 */     short decodedTileHeight = Tiles.decodeHeight(encodedTile);
/*      */     
/* 5043 */     int playerPosition = (int)(performer.getStatus().getPositionZ() * 10.0F) - decodedTileHeight;
/* 5044 */     playerPosition = Math.abs(playerPosition) - Math.abs(playerPosition % 30);
/* 5045 */     int buildOffset = 1;
/* 5046 */     int fLevel = performer.getFloorLevel(true);
/* 5047 */     if (heightOffsetFromPerformer == 0) {
/* 5048 */       buildOffset = 0;
/*      */     }
/*      */     
/* 5051 */     Skill floorBuildSkill = FloorBehaviour.getBuildSkill(floorType, StructureConstants.FloorMaterial.WOOD, performer);
/* 5052 */     if (!FloorBehaviour.mayPlanAtLevel(performer, fLevel + buildOffset, floorBuildSkill, (floorType == StructureConstants.FloorType.ROOF))) {
/* 5053 */       return true;
/*      */     }
/*      */     
/* 5056 */     if (fLevel + buildOffset == 0)
/*      */     {
/* 5058 */       if (MethodsHighways.onHighway(tilex, tiley, surfaced)) {
/*      */         
/* 5060 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to add ground flooring to a highway.");
/*      */         
/* 5062 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 5066 */     if (floorBuildSkill.getKnowledge(0.0D) < FloorBehaviour.getRequiredBuildSkillForFloorType(StructureConstants.FloorMaterial.WOOD)) {
/*      */       
/* 5068 */       performer.getCommunicator().sendNormalServerMessage("You need higher " + floorBuildSkill
/* 5069 */           .getName() + " skill to plan here.");
/* 5070 */       return true;
/*      */     } 
/*      */     
/* 5073 */     int floorBuildOffset = playerPosition + heightOffsetFromPerformer;
/*      */     
/* 5075 */     performer.sendToLoggers("Player pos=" + playerPosition + ", floorBuildOffset=" + floorBuildOffset + " heightOffsetFromPerformer=" + heightOffsetFromPerformer + " floorBuildOffset=" + floorBuildOffset);
/*      */ 
/*      */     
/* 5078 */     long structureId = structure.getWurmId();
/* 5079 */     Floor[] floors = structure.getFloorsAtTile(tilex, tiley, floorBuildOffset - 30, floorBuildOffset + 30);
/*      */ 
/*      */     
/* 5082 */     boolean hasFloorUnder = false;
/*      */     
/* 5084 */     boolean neighbourSection = (heightOffsetFromPerformer == 0);
/* 5085 */     if (floors != null)
/*      */     {
/* 5087 */       for (Floor floor : floors) {
/*      */         
/* 5089 */         if (floor.getTileX() == tilex && floor.getTileY() == tiley) {
/*      */ 
/*      */           
/* 5092 */           if (floor.getHeightOffset() == floorBuildOffset) {
/*      */             
/* 5094 */             performer.getCommunicator().sendNormalServerMessage("There is already a " + (
/* 5095 */                 (floor.getFloorState() == StructureConstants.FloorState.PLANNING) ? "planned " : "") + floor
/* 5096 */                 .getName() + " there. ");
/* 5097 */             return true;
/*      */           } 
/*      */ 
/*      */           
/* 5101 */           if (floorBuildOffset >= 30 && floor
/* 5102 */             .getHeightOffset() == floorBuildOffset - 30) {
/*      */             
/* 5104 */             hasFloorUnder = true;
/* 5105 */             if (floor.isRoof()) {
/*      */               
/* 5107 */               performer.getCommunicator().sendNormalServerMessage("There is already a " + (
/* 5108 */                   (floor.getFloorState() == StructureConstants.FloorState.PLANNING) ? "planned " : "") + floor
/* 5109 */                   .getName() + " there. ");
/* 5110 */               return true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 5117 */     if (floorBuildOffset > 0) {
/*      */       
/* 5119 */       neighbourSection = hasSupportAtTile(performer, tilex - 1, tiley, structure, floorBuildOffset);
/* 5120 */       if (!neighbourSection)
/*      */       {
/* 5122 */         neighbourSection = hasSupportAtTile(performer, tilex + 1, tiley, structure, floorBuildOffset);
/*      */       }
/* 5124 */       if (!neighbourSection)
/*      */       {
/* 5126 */         neighbourSection = hasSupportAtTile(performer, tilex, tiley - 1, structure, floorBuildOffset);
/*      */       }
/* 5128 */       if (!neighbourSection)
/*      */       {
/* 5130 */         neighbourSection = hasSupportAtTile(performer, tilex, tiley + 1, structure, floorBuildOffset);
/*      */       }
/*      */     } 
/*      */     
/* 5134 */     if (hasFloorUnder != true && floorBuildOffset > 30) {
/*      */       
/* 5136 */       performer.getCommunicator().sendNormalServerMessage("You need to have a floor under this one to be able to build safely.");
/*      */       
/* 5138 */       return true;
/*      */     } 
/* 5140 */     if (!Terraforming.isAllCornersInsideHeightRange(tilex, tiley, surfaced, decodedTileHeight, decodedTileHeight)) {
/*      */       
/* 5142 */       performer.getCommunicator().sendNormalServerMessage("The ground must be flat for advanced constructions.");
/* 5143 */       return true;
/*      */     } 
/*      */     
/* 5146 */     if (layer < 0)
/*      */     {
/* 5148 */       if (floorType == StructureConstants.FloorType.OPENING || floorType == StructureConstants.FloorType.DOOR || floorType == StructureConstants.FloorType.STAIRCASE || floorType == StructureConstants.FloorType.WIDE_STAIRCASE || floorType == StructureConstants.FloorType.LEFT_STAIRCASE || floorType == StructureConstants.FloorType.RIGHT_STAIRCASE || floorType == StructureConstants.FloorType.ANTICLOCKWISE_STAIRCASE || floorType == StructureConstants.FloorType.CLOCKWISE_STAIRCASE) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5156 */         short ceil = 260;
/* 5157 */         for (int x = 0; x <= 1; x++) {
/*      */           
/* 5159 */           for (int y = 0; y <= 1; y++) {
/*      */             
/* 5161 */             int tile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 5162 */             short ht = (short)(Tiles.decodeData(tile) & 0xFF);
/* 5163 */             if (ht < ceil)
/* 5164 */               ceil = ht; 
/*      */           } 
/*      */         } 
/* 5167 */         if (floorBuildOffset + 30 > ceil) {
/*      */           
/* 5169 */           performer.getCommunicator().sendNormalServerMessage("There is not enough room for a further floor. E.g. ceiling is too close.");
/* 5170 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 5175 */     if (!neighbourSection) {
/*      */       
/* 5177 */       for (Wall wall : vtile.getWalls()) {
/*      */         
/* 5179 */         performer.sendToLoggers(wall.getFloorLevel() + ";" + (wall.getFloorLevel() * 30) + "==" + floorBuildOffset + " OR " + (wall
/* 5180 */             .getFloorLevel() * 30 + 30));
/* 5181 */         if (wall.getFloorLevel() * 30 == floorBuildOffset || wall
/* 5182 */           .getFloorLevel() * 30 + 30 == floorBuildOffset)
/*      */         {
/* 5184 */           if (wall.isFinished()) {
/*      */             
/* 5186 */             neighbourSection = true;
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/* 5191 */       VolaTile eastTile = Zones.getTileOrNull(tilex + 1, tiley, structure.isOnSurface());
/* 5192 */       if (eastTile != null)
/* 5193 */         for (Wall wall : eastTile.getWalls()) {
/*      */           
/* 5195 */           performer.sendToLoggers(wall.getFloorLevel() + ";" + (wall.getFloorLevel() * 30) + "==" + floorBuildOffset + " OR " + (wall
/* 5196 */               .getFloorLevel() * 30 + 30));
/* 5197 */           if (wall.getFloorLevel() * 30 == floorBuildOffset || wall
/* 5198 */             .getFloorLevel() * 30 + 30 == floorBuildOffset)
/*      */           {
/* 5200 */             if (wall.isFinished()) {
/*      */               
/* 5202 */               if (!wall.isHorizontal() && wall.getStartX() == eastTile.getTileX())
/* 5203 */                 neighbourSection = true; 
/*      */               break;
/*      */             } 
/*      */           }
/*      */         }  
/* 5208 */       VolaTile southTile = Zones.getTileOrNull(tilex, tiley + 1, structure.isOnSurface());
/* 5209 */       if (southTile != null)
/* 5210 */         for (Wall wall : southTile.getWalls()) {
/*      */           
/* 5212 */           performer.sendToLoggers(wall.getFloorLevel() + ";" + (wall.getFloorLevel() * 30) + "==" + floorBuildOffset + " OR " + (wall
/* 5213 */               .getFloorLevel() * 30 + 30));
/* 5214 */           if (wall.getFloorLevel() * 30 == floorBuildOffset || wall
/* 5215 */             .getFloorLevel() * 30 + 30 == floorBuildOffset)
/*      */           {
/* 5217 */             if (wall.isFinished()) {
/*      */               
/* 5219 */               if (wall.isHorizontal() && wall.getStartY() == southTile.getTileY())
/* 5220 */                 neighbourSection = true; 
/*      */               break;
/*      */             } 
/*      */           }
/*      */         }  
/*      */     } 
/* 5226 */     if (!neighbourSection) {
/*      */       
/* 5228 */       performer.getCommunicator().sendNormalServerMessage("You need to build in connection with a finished neighbouring floor, roof or wall.");
/*      */       
/* 5230 */       return true;
/*      */     } 
/*      */     
/* 5233 */     Skill craftSkill = performer.getSkills().getSkillOrLearn(1005);
/* 5234 */     float qualityLevel = calculateNewQualityLevel(act.getPower(), craftSkill.getKnowledge(0.0D), 0.0F, 21);
/*      */     
/* 5236 */     DbFloor dbFloor = new DbFloor(floorType, tilex, tiley, floorBuildOffset, qualityLevel, structureId, StructureConstants.FloorMaterial.WOOD, layer);
/*      */     
/*      */     try {
/* 5239 */       dbFloor.save();
/*      */     }
/* 5241 */     catch (IOException e) {
/*      */       
/* 5243 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/* 5245 */     vtile.addFloor((Floor)dbFloor);
/*      */     
/* 5247 */     performer.getCommunicator().sendNormalServerMessage("You plan a " + dbFloor
/* 5248 */         .getName() + ((heightOffsetFromPerformer > 0) ? " above you." : " below you."));
/* 5249 */     return true;
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
/*      */   public static boolean floorDestroy(Creature performer, Item source, int tilex, int tiley, int layer, float counter, Action act, boolean surface) {
/* 5266 */     int time = 200;
/* 5267 */     VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, (layer != -1));
/* 5268 */     if (vtile == null) {
/*      */       
/* 5270 */       performer.getCommunicator().sendNormalServerMessage("Your sensitive mind notices a wrongness in the fabric of space.");
/*      */       
/* 5272 */       return true;
/*      */     } 
/*      */     
/* 5275 */     Structure structure = vtile.getStructure();
/*      */     
/* 5277 */     if (structure == null) {
/*      */       
/* 5279 */       performer.getCommunicator().sendNormalServerMessage("There is no structure here to remodel.");
/* 5280 */       return true;
/*      */     } 
/* 5282 */     if (structure.getOwnerId() != performer.getWurmId()) {
/*      */       
/* 5284 */       performer.getCommunicator().sendNormalServerMessage("Only the owner of " + structure
/* 5285 */           .getName() + " is allowed to remodel it.");
/* 5286 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5292 */     Floor[] floors = vtile.getFloors(0, 30);
/*      */     
/* 5294 */     if (floors == null) {
/*      */       
/* 5296 */       performer.getCommunicator().sendNormalServerMessage("No floors found here!");
/* 5297 */       return true;
/*      */     } 
/*      */     
/* 5300 */     if (!mayModifyStructure(performer, structure, vtile, (short)524)) {
/*      */       
/* 5302 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to do that!");
/* 5303 */       return true;
/*      */     } 
/*      */     
/* 5306 */     if (floors.length > 1)
/*      */     {
/* 5308 */       logger.log(Level.WARNING, "Weirdness! Multiple floors found for tile: " + tilex + " " + tiley);
/*      */     }
/*      */     
/* 5311 */     if (counter == 1.0F) {
/*      */       
/* 5313 */       performer.getCommunicator().sendNormalServerMessage("You start to destroy the floor.");
/*      */     }
/*      */     else {
/*      */       
/* 5317 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 5320 */     if (counter * 10.0F > time) {
/*      */       
/* 5322 */       for (int i = 0; i < floors.length; i++) {
/*      */         
/* 5324 */         vtile.removeFloor(floors[i]);
/* 5325 */         floors[i].delete();
/*      */       } 
/* 5327 */       performer.getCommunicator().sendNormalServerMessage("The last parts of the floor falls down with a loud crash.");
/* 5328 */       performer.performActionOkey(act);
/* 5329 */       return true;
/*      */     } 
/* 5331 */     return false;
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
/*      */   static boolean isCorrectToolForPlanning(Creature performer, int toolTemplateId) {
/* 5358 */     return (toolTemplateId == 62 || toolTemplateId == 63 || (toolTemplateId == 176 && 
/*      */       
/* 5360 */       WurmPermissions.mayUseGMWand(performer)) || (toolTemplateId == 315 && performer
/* 5361 */       .getPower() >= 2 && Servers.isThisATestServer()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCorrectToolForBuilding(Creature performer, int toolTemplateId) {
/* 5367 */     return (toolTemplateId == 62 || toolTemplateId == 63 || toolTemplateId == 493 || (toolTemplateId == 176 && 
/*      */ 
/*      */       
/* 5370 */       WurmPermissions.mayUseGMWand(performer)) || (toolTemplateId == 315 && performer
/* 5371 */       .getPower() >= 2 && Servers.isThisATestServer()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int[] getCorrectToolsForBuildingFences() {
/* 5377 */     int[] tools = { 62, 63, 493 };
/*      */     
/* 5379 */     return tools;
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
/*      */   public static boolean planWallAt(Action act, Creature aPerformer, Item aSource, int aTileX, int aTileY, boolean onSurfaced, int heightOffset, Tiles.TileBorderDirection dir, short action, float counter) {
/*      */     Skill craftSkill;
/*      */     int xend, yend;
/* 5401 */     if (aSource == null) {
/*      */       
/* 5403 */       aPerformer.getCommunicator().sendNormalServerMessage("You need an activated tool to plan a wall.");
/* 5404 */       return true;
/*      */     } 
/*      */     
/* 5407 */     if (!isCorrectToolForBuilding(aPerformer, aSource.getTemplateId())) {
/*      */       
/* 5409 */       aPerformer.getCommunicator().sendNormalServerMessage("You cannot do that with " + aSource.getNameWithGenus() + ".");
/* 5410 */       return true;
/*      */     } 
/*      */     
/* 5413 */     if (action != 20000 + StructureTypeEnum.SOLID.value) {
/*      */       
/* 5415 */       aPerformer.getCommunicator().sendNormalServerMessage("You can only plan walls here.");
/* 5416 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5421 */     Structure structure = getStructureOrNullAtTileBorder(aTileX, aTileY, dir, onSurfaced);
/*      */     
/* 5423 */     if (structure == null) {
/*      */       
/* 5425 */       aPerformer.getCommunicator().sendNormalServerMessage("The structural integrity of the building is at risk.");
/* 5426 */       logger.log(Level.WARNING, "Structure not found while trying to add a wall at [" + aTileX + "," + aTileY + "]");
/* 5427 */       return true;
/*      */     } 
/*      */     
/* 5430 */     structure.updateStructureFinishFlag();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 5440 */       if (aSource.getTemplateId() == 493) {
/* 5441 */         craftSkill = aPerformer.getSkills().getSkill(1013);
/*      */       } else {
/* 5443 */         craftSkill = aPerformer.getSkills().getSkill(1005);
/*      */       } 
/* 5445 */     } catch (NoSuchSkillException nss) {
/*      */       
/* 5447 */       if (aSource.getTemplateId() == 493) {
/* 5448 */         craftSkill = aPerformer.getSkills().learn(1013, 1.0F);
/*      */       } else {
/* 5450 */         craftSkill = aPerformer.getSkills().learn(1005, 1.0F);
/*      */       } 
/*      */     } 
/* 5453 */     if (FloorBehaviour.getRequiredBuildSkillForFloorLevel(heightOffset / 30, false) > craftSkill
/* 5454 */       .getKnowledge(0.0D)) {
/*      */       
/* 5456 */       aPerformer.getCommunicator().sendNormalServerMessage("Construction of walls is reserved for craftsmen of higher rank than yours.");
/*      */       
/* 5458 */       if (Servers.localServer.testServer)
/* 5459 */         aPerformer.getCommunicator().sendNormalServerMessage("You have " + craftSkill
/* 5460 */             .getRealKnowledge() + " and need " + 
/* 5461 */             FloorBehaviour.getRequiredBuildSkillForFloorLevel(heightOffset / 30, false)); 
/* 5462 */       return true;
/*      */     } 
/*      */     
/* 5465 */     VolaTile vtile = Zones.getOrCreateTile(aTileX, aTileY, onSurfaced);
/*      */ 
/*      */     
/* 5468 */     if (vtile == null) {
/*      */       
/* 5470 */       aPerformer.getCommunicator().sendNormalServerMessage("The structural integrity of the building is at risk.");
/* 5471 */       return true;
/*      */     } 
/*      */     
/* 5474 */     int structureTileX = aTileX;
/* 5475 */     int structureTileY = aTileY;
/* 5476 */     if (vtile.getStructure() == null || !vtile.getStructure().isTypeHouse())
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 5481 */       if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*      */         
/* 5483 */         structureTileX--;
/*      */       }
/* 5485 */       else if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*      */         
/* 5487 */         structureTileY--;
/*      */       } 
/*      */     }
/*      */     
/* 5491 */     vtile = Zones.getOrCreateTile(structureTileX, structureTileY, onSurfaced);
/*      */     
/* 5493 */     if (vtile == null) {
/*      */       
/* 5495 */       aPerformer.getCommunicator().sendNormalServerMessage("The structural integrity of the building is at risk.");
/* 5496 */       return true;
/*      */     } 
/*      */     
/* 5499 */     if (!mayModifyStructure(aPerformer, structure, vtile, (short)116)) {
/*      */       
/* 5501 */       aPerformer.getCommunicator().sendNormalServerMessage(" You are not allowed to add to " + structure
/* 5502 */           .getName() + " without permission.");
/* 5503 */       return true;
/*      */     } 
/*      */     
/* 5506 */     if (!Methods.isActionAllowed(aPerformer, (short)116, structureTileX, structureTileY)) {
/* 5507 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5514 */     Wall[] walls = vtile.getWalls();
/* 5515 */     for (int s = 0; s < walls.length; s++) {
/*      */       
/* 5517 */       if (walls[s].getStartX() == aTileX && walls[s].getStartY() == aTileY)
/*      */       {
/* 5519 */         if ((walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_HORIZ) || (
/* 5520 */           !walls[s].isHorizontal() && dir == Tiles.TileBorderDirection.DIR_DOWN)) {
/*      */           
/* 5522 */           if (walls[s].getHeight() == heightOffset) {
/*      */             
/* 5524 */             aPerformer.getCommunicator().sendNormalServerMessage("There is already a wall here!");
/* 5525 */             logger.log(Level.WARNING, "Wall already exists here: " + structure.getWurmId());
/* 5526 */             return true;
/*      */           } 
/* 5528 */           if (walls[s].getHeight() == heightOffset - 30)
/*      */           {
/* 5530 */             if (!walls[s].isFinished()) {
/*      */               
/* 5532 */               aPerformer.getCommunicator().sendNormalServerMessage("You need to finish the " + walls[s].getName() + " below first.");
/* 5533 */               return true;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 5540 */     int xstart = aTileX;
/* 5541 */     int ystart = aTileY;
/*      */ 
/*      */ 
/*      */     
/* 5545 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*      */ 
/*      */       
/* 5548 */       xend = aTileX;
/* 5549 */       yend = aTileY + 1;
/*      */     }
/* 5551 */     else if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*      */ 
/*      */       
/* 5554 */       xend = aTileX + 1;
/* 5555 */       yend = aTileY;
/*      */     }
/*      */     else {
/*      */       
/* 5559 */       aPerformer.getCommunicator().sendNormalServerMessage("You don't know how to build a wall in that direction.");
/* 5560 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5567 */     StructureTypeEnum wallType = StructureTypeEnum.PLAN;
/* 5568 */     StructureMaterialEnum wallMaterial = StructureMaterialEnum.WOOD;
/* 5569 */     if (aSource.getTemplateId() == 493)
/*      */     {
/* 5571 */       wallMaterial = StructureMaterialEnum.STONE;
/*      */     }
/*      */     
/* 5574 */     float qualityLevel = calculateNewQualityLevel(act.getPower(), craftSkill.getRealKnowledge(), 0.0F, 21);
/*      */ 
/*      */     
/* 5577 */     DbWall dbWall = new DbWall(wallType, structureTileX, structureTileY, xstart, ystart, xend, yend, qualityLevel, structure.getWurmId(), wallMaterial, true, heightOffset, structure.getLayer());
/*      */     
/* 5579 */     dbWall.setState(StructureStateEnum.INITIALIZED);
/* 5580 */     vtile.addWall((Wall)dbWall);
/* 5581 */     vtile.updateWall((Wall)dbWall);
/*      */     
/* 5583 */     aPerformer.getCommunicator().sendNormalServerMessage("You plan a wall for " + structure.getName() + ".");
/* 5584 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Structure getStructureOrNullAtTileBorder(int tilex, int tiley, Tiles.TileBorderDirection dir, boolean surfaced) {
/* 5590 */     VolaTile vtile = null;
/* 5591 */     return getStructureOrNullAtTileBorder(tilex, tiley, dir, surfaced, vtile);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Structure getStructureOrNullAtTileBorder(int tilex, int tiley, Tiles.TileBorderDirection dir, boolean surfaced, @Nullable VolaTile vtile) {
/* 5597 */     vtile = Zones.getTileOrNull(tilex, tiley, surfaced);
/* 5598 */     Structure structure = null;
/* 5599 */     if (vtile != null) {
/*      */       
/* 5601 */       Structure lstructure = vtile.getStructure();
/* 5602 */       if (lstructure != null && lstructure.isTypeHouse())
/* 5603 */         structure = lstructure; 
/*      */     } 
/* 5605 */     if (structure == null)
/*      */     {
/* 5607 */       if (dir == Tiles.TileBorderDirection.DIR_DOWN || dir == Tiles.TileBorderDirection.CORNER) {
/*      */ 
/*      */         
/* 5610 */         vtile = Zones.getTileOrNull(tilex - 1, tiley, surfaced);
/* 5611 */         if (vtile != null) {
/*      */           
/* 5613 */           Structure lstructure = vtile.getStructure();
/* 5614 */           if (lstructure != null && lstructure.isTypeHouse())
/* 5615 */             structure = lstructure; 
/*      */         } 
/*      */       } 
/*      */     }
/* 5619 */     if (structure == null)
/*      */     {
/* 5621 */       if (dir == Tiles.TileBorderDirection.DIR_HORIZ || dir == Tiles.TileBorderDirection.CORNER) {
/*      */ 
/*      */         
/* 5624 */         vtile = Zones.getTileOrNull(tilex, tiley - 1, surfaced);
/* 5625 */         if (vtile != null) {
/*      */           
/* 5627 */           Structure lstructure = vtile.getStructure();
/* 5628 */           if (lstructure != null && lstructure.isTypeHouse())
/* 5629 */             structure = lstructure; 
/*      */         } 
/*      */       } 
/*      */     }
/* 5633 */     return structure;
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
/*      */   public static boolean buildFence(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, Tiles.TileBorderDirection dir, long borderId, short action, float counter) {
/* 5654 */     if (source == null) {
/*      */       
/* 5656 */       performer.getCommunicator().sendNormalServerMessage("You need an activated tool to plan that fence.");
/* 5657 */       return true;
/*      */     } 
/*      */     
/* 5660 */     int templateId = source.getTemplateId();
/* 5661 */     if (!isCorrectToolForBuilding(performer, templateId)) {
/*      */       
/* 5663 */       performer.getCommunicator().sendNormalServerMessage("You need to use the right tool to do that.");
/* 5664 */       return true;
/*      */     } 
/*      */     
/* 5667 */     if (doesTileBorderContainWallOrFence(tilex, tiley, heightOffset, dir, onSurface, true)) {
/*      */       
/* 5669 */       performer.getCommunicator().sendNormalServerMessage("There is already a wall or fence there.");
/* 5670 */       return true;
/*      */     } 
/*      */     
/* 5673 */     if (doesTileBorderContainUnfinishedWallOrFenceBelow(tilex, tiley, heightOffset, dir, onSurface, true)) {
/*      */       
/* 5675 */       performer.getCommunicator().sendNormalServerMessage("There is an unfinished wall or fence below which you need to finish first.");
/* 5676 */       return true;
/*      */     } 
/*      */     
/* 5679 */     VolaTile vtile = null;
/* 5680 */     Structure structure = getStructureOrNullAtTileBorder(tilex, tiley, dir, onSurface, vtile);
/*      */     
/* 5682 */     if (structure != null)
/*      */     {
/* 5684 */       if (!mayModifyStructure(performer, structure, vtile, (short)116)) {
/*      */         
/* 5686 */         performer.getCommunicator().sendNormalServerMessage(" You are not allowed to add to " + structure
/* 5687 */             .getName() + " without permission.");
/* 5688 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 5692 */     if (!onSurface) {
/*      */ 
/*      */       
/* 5695 */       StructureConstantsEnum fencePlanType = Fence.getFencePlanType(action);
/* 5696 */       StructureConstantsEnum fenceType = Fence.getFenceForPlan(fencePlanType);
/* 5697 */       float heightClearanceNeeded = WallConstants.getCollisionHeight(fenceType);
/*      */       
/* 5699 */       boolean ceilingTooClose = false;
/* 5700 */       int tile = Server.caveMesh.getTile(tilex, tiley);
/* 5701 */       short ht = (short)(Tiles.decodeData(tile) & 0xFF);
/* 5702 */       if (ht < heightClearanceNeeded * 10.0F) {
/*      */         
/* 5704 */         ceilingTooClose = true;
/*      */       }
/*      */       else {
/*      */         
/* 5708 */         if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/* 5709 */           tile = Server.caveMesh.getTile(tilex + 1, tiley);
/*      */         } else {
/* 5711 */           tile = Server.caveMesh.getTile(tilex, tiley + 1);
/* 5712 */         }  ht = (short)(Tiles.decodeData(tile) & 0xFF);
/* 5713 */         if (ht < heightClearanceNeeded * 10.0F)
/*      */         {
/* 5715 */           ceilingTooClose = true;
/*      */         }
/*      */       } 
/* 5718 */       if (ceilingTooClose) {
/*      */         
/* 5720 */         if (ht == 0) {
/* 5721 */           performer.getCommunicator().sendNormalServerMessage("Fences cannot be built next to a cave exit.");
/*      */         } else {
/* 5723 */           performer.getCommunicator().sendNormalServerMessage("The ceiling is too close for that type of fence.");
/* 5724 */         }  return true;
/*      */       } 
/*      */     } 
/* 5727 */     return startFenceSection(performer, source, dir, tilex, tiley, onSurface, heightOffset, borderId, action, counter, 
/* 5728 */         (performer.getPower() >= 2));
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
/*      */   public static boolean mayModifyStructure(Creature performer, Structure structure, @Nullable VolaTile tile, short action) {
/* 5744 */     if (performer.getPower() > 1) {
/* 5745 */       return true;
/*      */     }
/* 5747 */     if (structure.isTypeHouse()) {
/*      */       
/* 5749 */       long ownerId = structure.getOwnerId();
/* 5750 */       byte k = Players.getInstance().getKingdomForPlayer(ownerId);
/* 5751 */       if (ownerId != -10L && !performer.isFriendlyKingdom(k))
/* 5752 */         return false; 
/* 5753 */       if ((structure.isEnemy(performer) && structure.isEnemyAllowed(performer, action)) || structure.mayModify(performer))
/* 5754 */         return true; 
/* 5755 */       if (tile != null && !structure.isFinished()) {
/*      */         
/* 5757 */         Village village = tile.getVillage();
/* 5758 */         if (village != null && village.isCitizen(performer.getWurmId()))
/*      */         {
/* 5760 */           if (village.isActionAllowed(action, performer))
/* 5761 */             return true; 
/*      */         }
/*      */       } 
/* 5764 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5769 */     if (tile != null) {
/*      */       
/* 5771 */       Village village = tile.getVillage();
/* 5772 */       if (village != null)
/*      */       {
/* 5774 */         return village.isActionAllowed(action, performer);
/*      */       }
/*      */     } 
/*      */     
/* 5778 */     return true;
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
/*      */   public static Structure getStructureAt(int tilex, int tiley, boolean surfaced) {
/* 5791 */     Structure structure = null;
/*      */     
/*      */     try {
/* 5794 */       Zone zone = Zones.getZone(tilex, tiley, surfaced);
/* 5795 */       VolaTile tile = zone.getOrCreateTile(tilex, tiley);
/* 5796 */       structure = tile.getStructure();
/* 5797 */       if (structure == null)
/* 5798 */         return null; 
/* 5799 */       return structure;
/*      */     }
/* 5801 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 5803 */       logger.log(Level.WARNING, nsz.getMessage());
/*      */ 
/*      */       
/* 5806 */       return structure;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<Structure> getStructuresNear(int tilex, int tiley, boolean surfaced) {
/* 5812 */     List<Structure> structures = new ArrayList<>();
/* 5813 */     for (int x = 1; x >= -1; x--) {
/*      */       
/* 5815 */       for (int y = 1; y >= -1; y--) {
/*      */         
/* 5817 */         Structure structure = getStructureAt(tilex + x, tiley + y, surfaced);
/* 5818 */         if (structure != null && structure.isTypeHouse() && !structures.contains(structure))
/*      */         {
/* 5820 */           structures.add(structure);
/*      */         }
/*      */       } 
/*      */     } 
/* 5824 */     return structures;
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
/*      */   public static final boolean wouldBuildOnStructure(Structure structure, int tilex, int tiley, boolean surfaced) {
/* 5839 */     Structure struct = getStructureAt(tilex, tiley, surfaced);
/* 5840 */     if (struct != null) {
/* 5841 */       return true;
/*      */     }
/* 5843 */     List<Structure> structures = getStructuresNear(tilex, tiley, surfaced);
/*      */     
/* 5845 */     if (structures == null) {
/* 5846 */       return false;
/*      */     }
/* 5848 */     if (structures.isEmpty()) {
/* 5849 */       return false;
/*      */     }
/* 5851 */     if (structures.contains(structure))
/*      */     {
/*      */       
/* 5854 */       return false;
/*      */     }
/* 5856 */     return true;
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
/*      */   public static final boolean tileBordersToHouse(int tilex, int tiley, boolean surfaced) {
/* 5869 */     List<Structure> structures = getStructuresNear(tilex, tiley, surfaced);
/* 5870 */     if (structures == null) {
/* 5871 */       return false;
/*      */     }
/* 5873 */     if (structures.isEmpty()) {
/* 5874 */       return false;
/*      */     }
/* 5876 */     return true;
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
/*      */   public static boolean removeHouseTile(Creature performer, int tilex, int tiley, int tile, float counter) {
/* 5890 */     if (!Methods.isActionAllowed(performer, (short)116, tilex, tiley))
/*      */     {
/* 5892 */       return true;
/*      */     }
/*      */     
/* 5895 */     VolaTile volaTile = Zones.getOrCreateTile(tilex, tiley, performer.isOnSurface());
/* 5896 */     if (hasFloors(volaTile)) {
/*      */       
/* 5898 */       performer.getCommunicator().sendNormalServerMessage("You must remove existing floor and roofs first, before you can tear down this part of the building.");
/*      */       
/* 5900 */       return true;
/*      */     } 
/*      */     
/* 5903 */     if (hasWalls(volaTile)) {
/*      */       
/* 5905 */       performer.getCommunicator().sendNormalServerMessage("You must destroy adjacent walls first, before you can tear down this part of the building.");
/*      */       
/* 5907 */       return true;
/*      */     } 
/* 5909 */     if (hasFences(volaTile)) {
/*      */       
/* 5911 */       performer.getCommunicator().sendNormalServerMessage("You must destroy adjacent fences first, before you can tear down this part of the building.");
/*      */       
/* 5913 */       return true;
/*      */     } 
/* 5915 */     if (hasBridgeEntrance(volaTile)) {
/*      */       
/* 5917 */       performer
/* 5918 */         .getCommunicator()
/* 5919 */         .sendNormalServerMessage("You must destroy the bridge that has an entrance on this tile first, before you can tear down this part of the building.");
/*      */       
/* 5921 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5927 */     Structure structureToShrink = getStructureAt(tilex, tiley, performer.isOnSurface());
/* 5928 */     if (structureToShrink == null || !structureToShrink.isTypeHouse()) {
/*      */       
/* 5930 */       performer.getCommunicator().sendNormalServerMessage("Could not find a building there.");
/*      */       
/* 5932 */       return true;
/*      */     } 
/* 5934 */     if (structureToShrink.getSize() == 1) {
/*      */       
/* 5936 */       performer.getCommunicator().sendNormalServerMessage("Cannot remove the last part of a building doing that, please use the writ to destroy the building.");
/*      */       
/* 5938 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 5944 */       Structure plannedStructure = performer.getStructure();
/* 5945 */       if (plannedStructure != null && structureToShrink.getWurmId() != plannedStructure.getWurmId())
/*      */       {
/* 5947 */         performer.getCommunicator().sendNormalServerMessage("You already have a " + (
/* 5948 */             plannedStructure.isTypeBridge() ? "bridge" : "building") + " under construction. Finish that one before trying to work here.");
/*      */         
/* 5950 */         return true;
/*      */       }
/*      */     
/* 5953 */     } catch (NoSuchStructureException noSuchStructureException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5958 */     if (!mayModifyStructure(performer, structureToShrink, volaTile, (short)82)) {
/*      */       
/* 5960 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to shrink " + structureToShrink
/* 5961 */           .getName() + ".");
/* 5962 */       return true;
/*      */     } 
/*      */     
/* 5965 */     if (!structureToShrink.testRemove(volaTile)) {
/*      */       
/* 5967 */       performer.getCommunicator().sendNormalServerMessage("You cannot remove this part of the building. Your building must connect everywhere.");
/*      */       
/* 5969 */       return true;
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
/* 5983 */     if (!structureToShrink.contains(tilex, tiley)) {
/*      */       
/* 5985 */       performer.getCommunicator().sendNormalServerMessage("There is no structure there to modify.");
/* 5986 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 5990 */     if (!structureToShrink.removeTileFromFinishedStructure(performer, tilex, tiley, performer.isOnSurface() ? 0 : -1)) {
/*      */       
/* 5992 */       performer.getCommunicator().sendNormalServerMessage("You can't divide the house in different parts.");
/* 5993 */       return true;
/*      */     } 
/* 5995 */     if (!hasEnoughSkillToContractStructure(performer, tilex, tiley, structureToShrink))
/*      */     {
/*      */       
/* 5998 */       return true;
/*      */     }
/*      */     
/* 6001 */     structureToShrink.updateStructureFinishFlag();
/*      */     
/* 6003 */     performer.getCommunicator().sendNormalServerMessage("You remove this area from the building.");
/*      */     
/* 6005 */     return true;
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
/*      */   public static void removeBuildMarker(Structure structure, int tilex, int tiley) {
/* 6017 */     VolaTile tile = Zones.getTileOrNull(tilex, tiley, structure.isOnSurface());
/* 6018 */     if (tile == null) {
/*      */       return;
/*      */     }
/* 6021 */     tile.removeBuildMarker(structure, tilex, tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean hasFences(VolaTile tile) {
/* 6026 */     Structure struct = tile.getStructure();
/* 6027 */     boolean isBadFence = false;
/* 6028 */     Fence[] fences = tile.getAllFences();
/*      */ 
/*      */     
/* 6031 */     for (Fence fence : fences) {
/*      */       
/* 6033 */       if (!fence.isSupportedByGround()) {
/*      */ 
/*      */         
/* 6036 */         if (fence.isHorizontal() && fence
/* 6037 */           .getStartY() == tile.getTileY() && fence.getEndY() == tile.getTileY() && fence
/* 6038 */           .getStartX() == tile.getTileX() && fence.getEndX() == tile.getTileX() + 1)
/*      */         {
/*      */           
/* 6041 */           if (!struct.contains(tile.getTileX(), tile.getTileY() - 1))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 6047 */             isBadFence = true;
/*      */           }
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 6053 */         if (fence.isHorizontal() && fence
/* 6054 */           .getStartY() == tile.getTileY() + 1 && fence.getEndY() == tile.getTileY() + 1 && fence
/* 6055 */           .getStartX() == tile.getTileX() && fence.getEndX() == tile.getTileX() + 1)
/*      */         {
/*      */           
/* 6058 */           if (!struct.contains(tile.getTileX(), tile.getTileY() + 1))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 6064 */             isBadFence = true;
/*      */           }
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 6070 */         if (!fence.isHorizontal() && fence
/* 6071 */           .getStartX() == tile.getTileX() + 1 && fence.getEndX() == tile.getTileX() + 1 && fence
/* 6072 */           .getStartY() == tile.getTileY() && fence.getEndY() == tile.getTileY() + 1)
/*      */         {
/* 6074 */           if (!struct.contains(tile.getTileX() + 1, tile.getTileY()))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 6080 */             isBadFence = true;
/*      */           }
/*      */         }
/*      */ 
/*      */         
/* 6085 */         if (!fence.isHorizontal() && fence
/* 6086 */           .getStartX() == tile.getTileX() && fence.getEndX() == tile.getTileX() && fence
/* 6087 */           .getStartY() == tile.getTileY() && fence.getEndY() == tile.getTileY() + 1)
/*      */         {
/* 6089 */           if (!struct.contains(tile.getTileX() - 1, tile.getTileY()))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 6095 */             isBadFence = true;
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 6101 */     return isBadFence;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean hasWalls(VolaTile tile) {
/* 6112 */     Wall[] walls = tile.getWalls();
/* 6113 */     Structure struct = tile.getStructure();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6121 */     if (walls.length == 0) {
/* 6122 */       return false;
/*      */     }
/* 6124 */     for (Wall wall : walls) {
/*      */ 
/*      */       
/* 6127 */       if (wall.isHorizontal() && wall
/* 6128 */         .getStartY() == tile.getTileY() && wall.getEndY() == tile.getTileY() && wall
/* 6129 */         .getStartX() == tile.getTileX() && wall.getEndX() == tile.getTileX() + 1)
/*      */       {
/*      */         
/* 6132 */         if (struct.contains(tile.getTileX(), tile.getTileY() - 1)) {
/*      */ 
/*      */           
/* 6135 */           VolaTile t = Zones.getTileOrNull(tile.getTileX(), tile.getTileY() - 1, tile.isOnSurface());
/* 6136 */           if (t != null)
/*      */           {
/* 6138 */             tile.removeWall(wall, true);
/* 6139 */             wall.setTile(tile.getTileX(), tile.getTileY() - 1);
/* 6140 */             t.addWall(wall);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 6146 */         else if (wall.getType() != StructureTypeEnum.PLAN) {
/* 6147 */           return true;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 6152 */       if (wall.isHorizontal() && wall
/* 6153 */         .getStartY() == tile.getTileY() + 1 && wall.getEndY() == tile.getTileY() + 1 && wall
/* 6154 */         .getStartX() == tile.getTileX() && wall.getEndX() == tile.getTileX() + 1)
/*      */       {
/*      */         
/* 6157 */         if (struct.contains(tile.getTileX(), tile.getTileY() + 1)) {
/*      */           
/* 6159 */           VolaTile t = Zones.getTileOrNull(tile.getTileX(), tile.getTileY() + 1, tile.isOnSurface());
/* 6160 */           if (t != null)
/*      */           {
/* 6162 */             tile.removeWall(wall, true);
/* 6163 */             wall.setTile(tile.getTileX(), tile.getTileY() + 1);
/* 6164 */             t.addWall(wall);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 6170 */         else if (wall.getType() != StructureTypeEnum.PLAN) {
/* 6171 */           return true;
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 6177 */       if (!wall.isHorizontal() && wall
/* 6178 */         .getStartX() == tile.getTileX() + 1 && wall.getEndX() == tile.getTileX() + 1 && wall
/* 6179 */         .getStartY() == tile.getTileY() && wall.getEndY() == tile.getTileY() + 1)
/*      */       {
/* 6181 */         if (struct.contains(tile.getTileX() + 1, tile.getTileY())) {
/*      */           
/* 6183 */           VolaTile t = Zones.getTileOrNull(tile.getTileX() + 1, tile.getTileY(), tile.isOnSurface());
/* 6184 */           if (t != null)
/*      */           {
/* 6186 */             tile.removeWall(wall, true);
/* 6187 */             wall.setTile(tile.getTileX() + 1, tile.getTileY());
/* 6188 */             t.addWall(wall);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 6194 */         else if (wall.getType() != StructureTypeEnum.PLAN) {
/* 6195 */           return true;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 6200 */       if (!wall.isHorizontal() && wall
/* 6201 */         .getStartX() == tile.getTileX() && wall.getEndX() == tile.getTileX() && wall
/* 6202 */         .getStartY() == tile.getTileY() && wall.getEndY() == tile.getTileY() + 1)
/*      */       {
/* 6204 */         if (struct.contains(tile.getTileX() - 1, tile.getTileY())) {
/*      */           
/* 6206 */           VolaTile t = Zones.getTileOrNull(tile.getTileX() - 1, tile.getTileY(), tile.isOnSurface());
/* 6207 */           if (t != null)
/*      */           {
/* 6209 */             tile.removeWall(wall, true);
/* 6210 */             wall.setTile(tile.getTileX() - 1, tile.getTileY());
/* 6211 */             t.addWall(wall);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 6217 */         else if (wall.getType() != StructureTypeEnum.PLAN) {
/* 6218 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 6223 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean hasFloors(VolaTile tile) {
/* 6228 */     Floor[] floors = tile.getFloors();
/* 6229 */     for (Floor floor : floors) {
/*      */       
/* 6231 */       if (floor.getFloorState() != StructureConstants.FloorState.PLANNING)
/* 6232 */         return true; 
/*      */     } 
/* 6234 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean hasBridgeEntrance(VolaTile tile) {
/* 6240 */     VolaTile vtNorth = Zones.getTileOrNull(tile.getTileX(), tile.getTileY() - 1, tile.isOnSurface());
/* 6241 */     if (vtNorth != null) {
/*      */       
/* 6243 */       Structure structNorth = vtNorth.getStructure();
/* 6244 */       if (structNorth != null && structNorth.isTypeBridge()) {
/*      */         
/* 6246 */         BridgePart[] bps = vtNorth.getBridgeParts();
/* 6247 */         if (bps.length == 1 && bps[0].hasHouseSouthExit()) {
/* 6248 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 6252 */     VolaTile vtEast = Zones.getTileOrNull(tile.getTileX() + 1, tile.getTileY(), tile.isOnSurface());
/* 6253 */     if (vtEast != null) {
/*      */       
/* 6255 */       Structure structEast = vtEast.getStructure();
/* 6256 */       if (structEast != null && structEast.isTypeBridge()) {
/*      */         
/* 6258 */         BridgePart[] bps = vtEast.getBridgeParts();
/* 6259 */         if (bps.length == 1 && bps[0].hasHouseWestExit()) {
/* 6260 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 6264 */     VolaTile vtSouth = Zones.getTileOrNull(tile.getTileX(), tile.getTileY() + 1, tile.isOnSurface());
/* 6265 */     if (vtSouth != null) {
/*      */       
/* 6267 */       Structure structSouth = vtSouth.getStructure();
/* 6268 */       if (structSouth != null && structSouth.isTypeBridge()) {
/*      */         
/* 6270 */         BridgePart[] bps = vtSouth.getBridgeParts();
/* 6271 */         if (bps.length == 1 && bps[0].hasHouseNorthExit()) {
/* 6272 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 6276 */     VolaTile vtWest = Zones.getTileOrNull(tile.getTileX() - 1, tile.getTileY(), tile.isOnSurface());
/* 6277 */     if (vtWest != null) {
/*      */       
/* 6279 */       Structure structWest = vtWest.getStructure();
/* 6280 */       if (structWest != null && structWest.isTypeBridge()) {
/*      */         
/* 6282 */         BridgePart[] bps = vtWest.getBridgeParts();
/* 6283 */         if (bps.length == 1 && bps[0].hasHouseEastExit())
/* 6284 */           return true; 
/*      */       } 
/*      */     } 
/* 6287 */     return false;
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
/*      */   public static boolean tileisNextToStructure(VolaTile[] structureTiles, int tilex, int tiley) {
/* 6300 */     if (structureTiles.length <= 0) {
/* 6301 */       return true;
/*      */     }
/* 6303 */     for (VolaTile tile : structureTiles) {
/*      */       
/* 6305 */       if (tiley == tile.getTileY()) {
/*      */         
/* 6307 */         if (tilex == tile.getTileX() + 1 || tilex == tile.getTileX() - 1) {
/* 6308 */           return true;
/*      */         }
/* 6310 */       } else if (tilex == tile.getTileX()) {
/*      */         
/* 6312 */         if (tiley == tile.getTileY() + 1 || tiley == tile.getTileY() - 1)
/* 6313 */           return true; 
/*      */       } 
/*      */     } 
/* 6316 */     return false;
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
/*      */   public static boolean hasOtherStructureNear(Structure structure, int tilex, int tiley, boolean surfaced) {
/* 6331 */     List<Structure> structsNear = getStructuresNear(tilex, tiley, surfaced);
/* 6332 */     if (structure == null)
/*      */     {
/* 6334 */       if (!structsNear.isEmpty())
/* 6335 */         return true; 
/*      */     }
/* 6337 */     if (structure != null)
/*      */     {
/* 6339 */       for (Structure struct : structsNear) {
/*      */         
/* 6341 */         if (struct.getWurmId() != structure.getWurmId())
/*      */         {
/* 6343 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/* 6347 */     return false;
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
/*      */   static final float calculateNewQualityLevel(float materialPower, double realKnowledge, float oldql, int needed) {
/* 6363 */     float qualityLevel = (float)Math.min((materialPower + oldql), oldql + realKnowledge / (needed + 1.0F));
/* 6364 */     qualityLevel = Math.max(1.0F, qualityLevel);
/* 6365 */     return qualityLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getSkillFor(IFloor floor) {
/* 6370 */     if (floor instanceof Floor)
/*      */     {
/* 6372 */       return FloorBehaviour.getSkillForFloor(((Floor)floor).getMaterial());
/*      */     }
/*      */ 
/*      */     
/* 6376 */     return BridgePartBehaviour.getRequiredSkill(((BridgePart)floor).getMaterial());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isWritHolder(Creature performer, long structureId) {
/*      */     try {
/* 6384 */       Structure structure = Structures.getStructure(structureId);
/*      */       
/* 6386 */       long writId = structure.getWritId();
/* 6387 */       if (writId != -10L) {
/*      */         
/* 6389 */         Item[] items = performer.getKeys();
/* 6390 */         for (int x = 0; x < items.length; x++)
/*      */         {
/* 6392 */           if (items[x].getWurmId() == writId)
/*      */           {
/* 6394 */             return true;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/* 6399 */     } catch (NoSuchStructureException nss) {
/*      */       
/* 6401 */       return false;
/*      */     } 
/* 6403 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean rotateFloor(Creature performer, Floor floor, float counter, Action act) {
/* 6409 */     boolean insta = (performer.getPower() >= 4);
/* 6410 */     VolaTile floorTile = Zones.getOrCreateTile(floor.getTileX(), floor.getTileY(), floor.isOnSurface());
/* 6411 */     if (floorTile == null)
/* 6412 */       return true; 
/* 6413 */     Structure structure = floorTile.getStructure();
/* 6414 */     if (!insta && !mayModifyStructure(performer, structure, floorTile, act.getNumber())) {
/*      */       
/* 6416 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to modify the structure.");
/*      */       
/* 6418 */       return true;
/*      */     } 
/* 6420 */     if (!floor.isFinished() && !floor.getType().isStair()) {
/*      */       
/* 6422 */       performer.getCommunicator().sendNormalServerMessage("You cannot rotate an unfinished floor.");
/* 6423 */       return true;
/*      */     } 
/*      */     
/* 6426 */     boolean toReturn = false;
/* 6427 */     int time = 40;
/*      */     
/* 6429 */     if (counter == 1.0F) {
/*      */       
/* 6431 */       if (!Methods.isActionAllowed(performer, (short)116, floor.getTileX(), floor.getTileY()))
/*      */       {
/* 6433 */         return true;
/*      */       }
/* 6435 */       performer.getCommunicator().sendNormalServerMessage("You start to rotate the " + floor.getName() + ".");
/* 6436 */       Server.getInstance().broadCastAction(performer.getName() + " starts to rotate a " + floor.getName() + ".", performer, 5);
/*      */ 
/*      */       
/* 6439 */       performer.sendActionControl(Actions.actionEntrys[act
/* 6440 */             .getNumber()].getVerbString(), true, time);
/* 6441 */       act.setTimeLeft(time);
/* 6442 */       performer.getStatus().modifyStamina(-300.0F);
/*      */     }
/*      */     else {
/*      */       
/* 6446 */       time = act.getTimeLeft();
/*      */     } 
/* 6448 */     if (act.mayPlaySound()) {
/*      */       
/* 6450 */       String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 6451 */       if (floor.isStone() || floor.isMarble() || floor.isSlate() || floor.isSandstone()) {
/* 6452 */         s = "sound.work.masonry";
/* 6453 */       } else if (floor.isMetal()) {
/* 6454 */         s = "sound.work.smithing.metal";
/* 6455 */       }  SoundPlayer.playSound(s, performer, 1.0F);
/*      */     } 
/* 6457 */     if (act.currentSecond() % 5 == 0) {
/*      */       
/* 6459 */       performer.getStatus().modifyStamina(-700.0F);
/*      */     }
/* 6461 */     else if (counter * 10.0F > time || insta) {
/*      */       
/* 6463 */       if (act.getNumber() == 177) {
/* 6464 */         floor.rotate(2);
/*      */       } else {
/* 6466 */         floor.rotate(-2);
/*      */       } 
/* 6468 */       performer.getCommunicator().sendNormalServerMessage("You rotate the " + floor.getName() + " through 90 degrees.");
/*      */       
/* 6470 */       floor.setLastUsed(System.currentTimeMillis());
/* 6471 */       Server.getInstance().broadCastAction(performer.getName() + " rotates a " + floor.getName() + " a bit.", performer, 5);
/*      */ 
/*      */       
/* 6474 */       toReturn = true;
/*      */     } 
/*      */     
/* 6477 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean needSurroundingTilesFloors(Creature performer, int tilex, int tiley) {
/* 6482 */     if (Features.Feature.CAVE_DWELLINGS.isEnabled() && !performer.isOnSurface()) {
/*      */ 
/*      */       
/* 6485 */       VolaTile vt = Zones.getOrCreateTile(tilex, tiley, false);
/* 6486 */       if (vt.getVillage() == null)
/*      */       {
/*      */         
/* 6489 */         for (int x = -1; x <= 1; x++) {
/*      */           
/* 6491 */           for (int y = -1; y <= 1; y++) {
/*      */             
/* 6493 */             int ttile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 6494 */             byte ttype = Tiles.decodeType(ttile);
/* 6495 */             if (!Tiles.isReinforcedFloor(ttype) && ttype != Tiles.Tile.TILE_CAVE.id)
/*      */             {
/* 6497 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 6503 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasInsideFence(Wall wall) {
/* 6509 */     VolaTile vt = wall.getTile();
/* 6510 */     for (Fence f : vt.getAllFences()) {
/*      */       
/* 6512 */       if (f.getStartX() == wall.getStartX() && f.getStartY() == wall.getStartY() && f
/* 6513 */         .getEndX() == wall.getEndX() && f.getEndY() == wall.getEndY() && f
/* 6514 */         .getHeightOffset() == wall.getHeight())
/*      */       {
/* 6516 */         return true;
/*      */       }
/*      */     } 
/* 6519 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MethodsStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */