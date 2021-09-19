/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.ai.NoPathException;
/*      */ import com.wurmonline.server.creatures.ai.Order;
/*      */ import com.wurmonline.server.creatures.ai.Path;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.epic.EpicMission;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.epic.EpicTargetItems;
/*      */ import com.wurmonline.server.epic.HexMap;
/*      */ import com.wurmonline.server.epic.Valrei;
/*      */ import com.wurmonline.server.highways.HighwayPos;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.FragmentUtilities;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.Materials;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.kingdom.InfluenceChain;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.questions.ManageObjectList;
/*      */ import com.wurmonline.server.questions.ManagePermissions;
/*      */ import com.wurmonline.server.questions.MissionManager;
/*      */ import com.wurmonline.server.questions.PermissionsHistory;
/*      */ import com.wurmonline.server.questions.TeamManagementQuestion;
/*      */ import com.wurmonline.server.questions.TestQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.spells.Spell;
/*      */ import com.wurmonline.server.spells.Spells;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.tutorial.MissionPerformed;
/*      */ import com.wurmonline.server.tutorial.MissionPerformer;
/*      */ import com.wurmonline.server.tutorial.TriggerEffect;
/*      */ import com.wurmonline.server.tutorial.TriggerEffects;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.DeadVillage;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.webinterface.WcEpicStatusReport;
/*      */ import com.wurmonline.server.zones.EncounterType;
/*      */ import com.wurmonline.server.zones.ErrorChecks;
/*      */ import com.wurmonline.server.zones.FaithZone;
/*      */ import com.wurmonline.server.zones.HiveZone;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.SpawnTable;
/*      */ import com.wurmonline.server.zones.TilePoller;
/*      */ import com.wurmonline.server.zones.Trap;
/*      */ import com.wurmonline.server.zones.TurretZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.WaterType;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.StructureConstants;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.io.IOException;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Random;
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
/*      */ class TileBehaviour
/*      */   extends Behaviour
/*      */   implements ItemTypes, MiscConstants, ItemMaterials, TimeConstants
/*      */ {
/*  143 */   private static final Logger logger = Logger.getLogger(TileBehaviour.class.getName());
/*      */   
/*      */   private static final int MIN_SKILL_FORAGE_STEPPE = 23;
/*      */   
/*      */   private static final int MIN_SKILL_BOTANIZE_MARSH = 27;
/*      */   
/*      */   private static final int MIN_SKILL_FORAGE_TUNDRA = 33;
/*      */   
/*      */   private static final int MIN_SKILL_BOTANIZE_MOSS = 35;
/*      */   private static final int MIN_SKILL_FORAGE_MARSH = 43;
/*      */   private static final int MIN_SKILL_BOTANIZE_PEAT = 42;
/*  154 */   static final Random r = new Random();
/*      */ 
/*      */   
/*      */   TileBehaviour() {
/*  158 */     super((short)5);
/*      */   }
/*      */ 
/*      */   
/*      */   TileBehaviour(short type) {
/*  163 */     super(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCave() {
/*  168 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sendVillageString(Creature performer, int tilex, int tiley, boolean surfaced) {
/*  174 */     VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, surfaced);
/*  175 */     Communicator comm = performer.getCommunicator();
/*  176 */     if (vtile.getVillage() != null) {
/*      */       
/*  178 */       comm.sendNormalServerMessage("This is within the village of " + vtile
/*  179 */           .getVillage().getName() + ".");
/*      */     }
/*      */     else {
/*      */       
/*  183 */       Village v = Villages.getVillageWithPerimeterAt(tilex, tiley, true);
/*  184 */       if (v != null)
/*      */       {
/*  186 */         comm.sendNormalServerMessage("This is within the perimeter of " + v.getName() + ".");
/*      */       }
/*      */     } 
/*      */     
/*  190 */     if (vtile.getStructure() != null) {
/*      */       
/*  192 */       comm.sendNormalServerMessage("This is within the structure of " + vtile.getStructure().getName() + ".");
/*  193 */       if (performer.getPower() > 0) {
/*  194 */         comm.sendNormalServerMessage(vtile
/*  195 */             .getStructure().getName() + " at " + vtile.getStructure().getCenterX() + ", " + vtile
/*  196 */             .getStructure().getCenterY() + " has wurmid " + vtile.getStructure().getWurmId());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item source, int tilex, int tiley, boolean onSurface, int tile) {
/*  205 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  206 */     toReturn.addAll(super.getBehavioursFor(performer, source, tilex, tiley, onSurface, tile));
/*      */     
/*  208 */     byte type = Tiles.decodeType(tile);
/*  209 */     byte data = Tiles.decodeData(tile);
/*  210 */     Tiles.Tile theTile = Tiles.getTile(type);
/*  211 */     int templateId = source.getTemplateId();
/*  212 */     if (!source.isTraded()) {
/*      */       
/*  214 */       if (source.getTemplateId() == 176 && performer.getPower() >= 2)
/*      */       {
/*  216 */         toReturn.add(Actions.actionEntrys[604]);
/*      */       }
/*  218 */       if (source.isDiggingtool() && Terraforming.isPackable(type))
/*      */       {
/*  220 */         toReturn.add(Actions.actionEntrys[154]);
/*      */       }
/*  222 */       if (source.isDiggingtool() && onSurface && !Terraforming.isNonDiggableTile(type)) {
/*      */         
/*  224 */         toReturn.add(Actions.actionEntrys[144]);
/*  225 */         if (isCloseTile(performer.getTileX(), performer.getTileY(), tilex, tiley))
/*      */         {
/*  227 */           toReturn.add(Actions.actionEntrys[150]);
/*      */         }
/*      */         
/*  230 */         if (isAdjacentTile(performer.getTileX(), performer.getTileY(), tilex, tiley))
/*      */         {
/*  232 */           toReturn.add(Actions.actionEntrys[532]);
/*      */         }
/*      */       } 
/*  235 */       if (source.isDredgingTool() && onSurface && !Terraforming.isNonDiggableTile(type)) {
/*      */         
/*  237 */         toReturn.add(Actions.actionEntrys[362]);
/*      */         
/*  239 */         if (isCloseTile(performer.getTileX(), performer.getTileY(), tilex, tiley))
/*      */         {
/*  241 */           toReturn.add(Actions.actionEntrys[150]);
/*      */         }
/*  243 */         if (isAdjacentTile(performer.getTileX(), performer.getTileY(), tilex, tiley))
/*      */         {
/*  245 */           toReturn.add(Actions.actionEntrys[532]);
/*      */         }
/*      */       } 
/*  248 */       if (source.isTrap()) {
/*      */         
/*  250 */         if (Trap.mayTrapTemplateOnTile(templateId, type))
/*      */         {
/*  252 */           if (Trap.getTrap(tilex, tiley, performer.getLayer()) == null)
/*      */           {
/*  254 */             toReturn.add(Actions.actionEntrys[374]);
/*      */           }
/*      */         }
/*  257 */         else if (templateId == 612)
/*      */         {
/*  259 */           if (Trap.mayPlantCorrosion(tilex, tiley, performer.getLayer()))
/*      */           {
/*  261 */             if (Trap.getTrap(tilex, tiley, performer.getLayer()) == null)
/*      */             {
/*  263 */               toReturn.add(Actions.actionEntrys[374]);
/*      */             }
/*      */           }
/*      */         }
/*      */       
/*  268 */       } else if (source.isDisarmTrap()) {
/*  269 */         toReturn.add(Actions.actionEntrys[375]);
/*  270 */       }  if (onSurface && source.isDiggingtool() && (
/*  271 */         Terraforming.isRoad(type) || type == Tiles.Tile.TILE_PLANKS.id || type == Tiles.Tile.TILE_PLANKS_TARRED.id)) {
/*      */ 
/*      */         
/*  274 */         HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  275 */         if (!MethodsHighways.onHighway(highwaypos)) {
/*  276 */           toReturn.add(Actions.actionEntrys[191]);
/*      */         }
/*  278 */       } else if (templateId == 153 && Tiles.decodeType(tile) == Tiles.Tile.TILE_PLANKS.id && onSurface) {
/*  279 */         toReturn.add(new ActionEntry((short)231, "Tar", "tarring"));
/*  280 */       }  if (onSurface && Tiles.isRoadType(type))
/*      */       {
/*  282 */         if (source.isPaveable() && source.getTemplateId() != 495) {
/*      */           
/*  284 */           HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  285 */           if (MethodsHighways.onHighway(highwaypos)) {
/*      */             
/*  287 */             toReturn.add(new ActionEntry((short)-2, "Re-Pave", "re-paving", emptyIntArr));
/*  288 */             toReturn.add(new ActionEntry((short)155, "Replace paving", "re-paving"));
/*  289 */             toReturn.add(new ActionEntry((short)576, "Replace using nearest corner", "re-paving"));
/*      */           } 
/*      */         } 
/*      */       }
/*  293 */       if (type == Tiles.Tile.TILE_DIRT_PACKED.id) {
/*      */         
/*  295 */         if (source.isPaveable() && source.getTemplateId() != 495)
/*      */         {
/*  297 */           toReturn.add(new ActionEntry((short)-2, "Pave", "paving", emptyIntArr));
/*  298 */           toReturn.add(Actions.actionEntrys[155]);
/*  299 */           toReturn.add(Actions.actionEntrys[576]);
/*      */         }
/*      */       
/*  302 */       } else if (type == Tiles.Tile.TILE_MARSH.id) {
/*      */         
/*  304 */         if (templateId == 495) {
/*      */           
/*  306 */           toReturn.add(new ActionEntry((short)-2, "Lay boards", "paving", emptyIntArr));
/*  307 */           toReturn.add(new ActionEntry((short)155, "Over marsh", "laying", new int[] { 43 }));
/*      */ 
/*      */           
/*  310 */           toReturn.add(new ActionEntry((short)576, "In nearest corner", "laying", new int[] { 43 }));
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  315 */       if (Terraforming.isCultivatable(type))
/*      */       {
/*  317 */         if (templateId == 27 || templateId == 25)
/*  318 */           toReturn.add(Actions.actionEntrys[318]); 
/*      */       }
/*  320 */       if (templateId == 1115 || (source.isWand() && Tiles.isMineDoor(type))) {
/*      */         
/*  322 */         MineDoorPermission md = MineDoorPermission.getPermission(tilex, tiley);
/*  323 */         if (md != null && (md.mayManage(performer) || md.isActualOwner(performer.getWurmId())))
/*      */         {
/*  325 */           toReturn.add(new ActionEntry((short)906, "Remove mine door", "removing"));
/*      */         }
/*      */       } 
/*  328 */       if (Terraforming.isSwitchableTiles(templateId, type))
/*      */       {
/*  330 */         toReturn.add(Actions.actionEntrys[927]);
/*      */       }
/*  332 */       if ((source.isWeapon() || source.isWand()) && Tiles.isMineDoor(type))
/*      */       {
/*  334 */         toReturn.add(new ActionEntry((short)174, "Destroy door", "destroying"));
/*      */       }
/*  336 */       if (Terraforming.isBuildTile(type) && onSurface)
/*      */       {
/*  338 */         toReturn.addAll(getBuildableTileBehaviours(tilex, tiley, performer, templateId));
/*      */       }
/*  340 */       if (source.getTemplate().isRune()) {
/*      */         
/*  342 */         Skill soulDepth = performer.getSoulDepth();
/*  343 */         double diff = (20.0F + source.getDamage()) - (source.getCurrentQualityLevel() + source.getRarity()) - 45.0D;
/*  344 */         double chance = soulDepth.getChance(diff, null, source.getCurrentQualityLevel());
/*  345 */         if (RuneUtilities.isSingleUseRune(source) && RuneUtilities.getSpellForRune(source) != null && 
/*  346 */           RuneUtilities.getSpellForRune(source).isTargetTile())
/*  347 */           toReturn.add(new ActionEntry((short)945, "Use Rune: " + chance + "%", "using rune", emptyIntArr)); 
/*      */       } 
/*  349 */       if (onSurface && (source.getTemplate().isDiggingtool() || source.getTemplateId() == 493))
/*      */       {
/*  351 */         if (source.getTemplateId() == 493) {
/*  352 */           toReturn.add(Actions.actionEntrys[910]);
/*      */         } else {
/*      */           
/*  355 */           Skill arch = performer.getSkills().getSkillOrLearn(10069);
/*  356 */           if (arch.getKnowledge(0.0D) >= 20.0D) {
/*  357 */             toReturn.add(Actions.actionEntrys[910]);
/*      */           }
/*      */         } 
/*      */       }
/*  361 */       if (templateId == 174 || templateId == 524 || templateId == 525) {
/*      */ 
/*      */         
/*  364 */         int tx = source.getData1();
/*  365 */         int ty = source.getData2();
/*  366 */         if (tx != -1 && ty != -1)
/*  367 */           toReturn.add(Actions.actionEntrys[95]); 
/*  368 */         if (templateId == 174 || templateId == 524)
/*  369 */           toReturn.add(Actions.actionEntrys[94]); 
/*      */       } 
/*  371 */       toReturn.addAll(getTileAndFloorBehavioursFor(performer, source, tilex, tiley, tile));
/*      */       
/*  373 */       if (templateId == 489 || (
/*  374 */         WurmPermissions.mayUseGMWand(performer) && (templateId == 176 || templateId == 315))) {
/*  375 */         toReturn.add(Actions.actionEntrys[329]);
/*  376 */       } else if (performer.getCultist() != null) {
/*      */         
/*  378 */         if (performer.getCultist().mayInfoLocal())
/*  379 */           toReturn.add(Actions.actionEntrys[185]); 
/*  380 */         if ((type == Tiles.Tile.TILE_LAVA.id || type == Tiles.Tile.TILE_CAVE_WALL_LAVA.id) && performer
/*  381 */           .getCultist().maySpawnVolcano())
/*  382 */           toReturn.add(new ActionEntry((short)78, "Freeze", "freezing")); 
/*      */       } 
/*  384 */       if (templateId == 602)
/*      */       {
/*  386 */         toReturn.add(Actions.actionEntrys[369]);
/*      */       }
/*  388 */       boolean water = Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface());
/*      */       
/*  390 */       if (water) {
/*      */         
/*  392 */         if (source.getTemplateId() == 1343 || source
/*  393 */           .getTemplateId() == 705 || source.getTemplateId() == 707 || source
/*  394 */           .getTemplateId() == 1344 || source.getTemplateId() == 1346)
/*      */         {
/*  396 */           toReturn.add(Actions.actionEntrys[160]);
/*      */         }
/*  398 */         if (source.isContainerLiquid() && !source.isSealedByPlayer())
/*  399 */           toReturn.add(Actions.actionEntrys[189]); 
/*  400 */         toReturn.add(Actions.actionEntrys[19]);
/*  401 */         toReturn.add(Actions.actionEntrys[183]);
/*  402 */         if (performer.getDeity() != null && performer.getDeity().isWaterGod()) {
/*  403 */           Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]);
/*      */         }
/*      */         
/*  406 */         if (source.getTemplateId() == 1344 || source.getTemplateId() == 1343 || source
/*  407 */           .getTemplateId() == 705 || source.getTemplateId() == 707 || source
/*  408 */           .getTemplateId() == 1346)
/*      */         {
/*  410 */           toReturn.add(new ActionEntry((short)285, "Lore", "thinking"));
/*      */         }
/*      */       } 
/*      */       
/*  414 */       if (performer.getVehicle() != -10L) {
/*      */         
/*  416 */         Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle());
/*  417 */         VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/*  418 */         if (t == null || t.getStructure() == null)
/*  419 */           if (vehicle.isChair()) {
/*  420 */             toReturn.add(Actions.actionEntrys[708]);
/*      */           } else {
/*  422 */             toReturn.add(Actions.actionEntrys[333]);
/*      */           }  
/*  424 */       }  if (performer.getKingdomTemplateId() == 3 && Tiles.getTile(type).isMycelium())
/*      */       {
/*  426 */         toReturn.add(Actions.actionEntrys[347]);
/*      */       }
/*  428 */       if (Features.Feature.TRANSFORM_RESOURCE_TILES.isEnabled() && source
/*  429 */         .getTemplateId() == 654 && source
/*  430 */         .getAuxData() != 0 && source.getBless() != null)
/*      */       {
/*      */         
/*  433 */         if (Features.Feature.TRANSFORM_TO_RESOURCE_TILES.isEnabled() && ((source
/*  434 */           .getAuxData() == 1 && type == Tiles.Tile.TILE_SAND.id) || (source
/*  435 */           .getAuxData() == 2 && type == Tiles.Tile.TILE_GRASS.id) || (source
/*  436 */           .getAuxData() == 2 && type == Tiles.Tile.TILE_MYCELIUM.id) || (source
/*  437 */           .getAuxData() == 3 && type == Tiles.Tile.TILE_STEPPE.id) || (source
/*  438 */           .getAuxData() == 7 && type == Tiles.Tile.TILE_MOSS.id))) {
/*      */           
/*  440 */           toReturn.add(new ActionEntry((short)-1, "Alchemy", "Alchemy"));
/*  441 */           toReturn.add(Actions.actionEntrys[462]);
/*      */         }
/*  443 */         else if ((source.getAuxData() == 4 && type == Tiles.Tile.TILE_CLAY.id) || (source
/*  444 */           .getAuxData() == 5 && type == Tiles.Tile.TILE_PEAT.id) || (source
/*  445 */           .getAuxData() == 6 && type == Tiles.Tile.TILE_TAR.id) || (source
/*  446 */           .getAuxData() == 8 && type == Tiles.Tile.TILE_TUNDRA.id)) {
/*      */           
/*  448 */           toReturn.add(new ActionEntry((short)-1, "Alchemy", "Alchemy"));
/*  449 */           toReturn.add(Actions.actionEntrys[462]);
/*      */         } 
/*      */       }
/*      */     } 
/*  453 */     if (Tiles.isMineDoor(type)) {
/*      */       
/*  455 */       MineDoorPermission md = MineDoorPermission.getPermission(tilex, tiley);
/*  456 */       if (md != null) {
/*      */         
/*  458 */         List<ActionEntry> permissions = new LinkedList<>();
/*      */         
/*  460 */         if (md.mayManage(performer) || md.isActualOwner(performer.getWurmId())) {
/*  461 */           permissions.add(Actions.actionEntrys[364]);
/*      */         }
/*  463 */         if (md.mayManage(performer))
/*      */         {
/*  465 */           if (type != Tiles.Tile.TILE_MINE_DOOR_STONE.id)
/*      */           {
/*  467 */             if (source.getTemplateId() == MethodsStructure.getImproveItem(type))
/*      */             {
/*  469 */               if (!source.isTraded())
/*      */               {
/*  471 */                 toReturn.add(Actions.actionEntrys[192]);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  476 */         if (md.maySeeHistory(performer))
/*  477 */           permissions.add(new ActionEntry((short)691, "History of Mine Door", "viewing")); 
/*  478 */         if (!permissions.isEmpty()) {
/*      */           
/*  480 */           if (permissions.size() > 1) {
/*      */             
/*  482 */             Collections.sort(permissions);
/*  483 */             toReturn.add(new ActionEntry((short)-permissions.size(), "Permissions", "viewing"));
/*      */           } 
/*  485 */           toReturn.addAll(permissions);
/*      */         } 
/*      */       } 
/*      */     } 
/*  489 */     if (type != Tiles.Tile.TILE_GRASS.id && type != Tiles.Tile.TILE_MYCELIUM.id && !theTile.isBush() && !theTile.isTree()) {
/*      */ 
/*      */       
/*  492 */       List<ActionEntry> nature = new LinkedList<>();
/*  493 */       toReturn.addAll(getNatureMenu(performer, tilex, tiley, type, data, nature));
/*      */     } 
/*  495 */     addEmotes(toReturn);
/*  496 */     if (performer.isOnSurface() && performer.getPower() >= 2) {
/*      */       
/*  498 */       if (Zones.protectedTiles[tilex][tiley]) {
/*  499 */         toReturn.add(Actions.actionEntrys[382]);
/*      */       } else {
/*  501 */         toReturn.add(Actions.actionEntrys[381]);
/*  502 */       }  toReturn.add(Actions.actionEntrys[476]);
/*      */     } 
/*  504 */     if ((templateId == 176 || templateId == 315) && WurmPermissions.mayUseGMWand(performer))
/*      */     {
/*  506 */       if (performer.getTaggedItemId() != -10L) {
/*  507 */         toReturn.add(new ActionEntry((short)675, "Summon '" + performer.getTaggedItemName() + "'", "summoning"));
/*      */       }
/*      */     }
/*  510 */     if (performer.isTeamLeader())
/*  511 */       toReturn.add(Actions.actionEntrys[471]); 
/*  512 */     if (performer.getTeam() != null) {
/*  513 */       toReturn.add(Actions.actionEntrys[470]);
/*      */     }
/*      */     
/*  516 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   List<ActionEntry> getNatureMenu(Creature performer, int tilex, int tiley, byte type, byte data, List<ActionEntry> menu) {
/*  522 */     List<ActionEntry> toReturn = new LinkedList<>();
/*      */     
/*  524 */     int sz = -menu.size();
/*  525 */     boolean canForage = canForage(performer, type, data);
/*  526 */     boolean canBotanize = canBotanize(performer, type, data);
/*  527 */     boolean canCollect = canCollectSnow(performer, tilex, tiley, type, data);
/*  528 */     if (canForage)
/*  529 */       sz--; 
/*  530 */     if (canBotanize) {
/*  531 */       sz--;
/*      */     }
/*  533 */     if (sz < 0) {
/*      */       
/*  535 */       toReturn.add(new ActionEntry((short)sz, "Nature", "nature", emptyIntArr));
/*  536 */       toReturn.addAll(menu);
/*      */       
/*  538 */       if (canForage)
/*  539 */         toReturn.addAll(getBehavioursForForage(performer)); 
/*  540 */       if (canBotanize)
/*  541 */         toReturn.addAll(getBehavioursForBotanize(performer)); 
/*      */     } 
/*  543 */     if (canCollect) {
/*  544 */       toReturn.add(Actions.actionEntrys[741]);
/*      */     }
/*  546 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursForForage(Creature performer) {
/*  557 */     List<ActionEntry> toReturn = new LinkedList<>();
/*      */     
/*      */     try {
/*  560 */       Skill forage = performer.getSkills().getSkill(10071);
/*  561 */       if (forage.getKnowledge(0.0D) > 20.0D) {
/*      */         
/*  563 */         toReturn.add(new ActionEntry((short)-4, "Forage for", "foraging", emptyIntArr));
/*  564 */         toReturn.add(new ActionEntry((short)223, "Anything", "foraging", new int[] { 43 }));
/*      */         
/*  566 */         toReturn.add(Actions.actionEntrys[569]);
/*  567 */         toReturn.add(Actions.actionEntrys[570]);
/*  568 */         toReturn.add(Actions.actionEntrys[571]);
/*      */       } else {
/*      */         
/*  571 */         toReturn.add(Actions.actionEntrys[223]);
/*      */       } 
/*  573 */     } catch (NoSuchSkillException nss) {
/*      */ 
/*      */       
/*  576 */       toReturn.add(Actions.actionEntrys[223]);
/*      */     } 
/*  578 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursForBotanize(Creature performer) {
/*  589 */     List<ActionEntry> toReturn = new LinkedList<>();
/*      */     
/*      */     try {
/*  592 */       Skill botanize = performer.getSkills().getSkill(10072);
/*  593 */       if (botanize.getKnowledge(0.0D) > 20.0D) {
/*      */ 
/*      */         
/*  596 */         toReturn.add(new ActionEntry((short)-5, "Botanize for", "botanizing", emptyIntArr));
/*  597 */         toReturn.add(new ActionEntry((short)224, "Anything", "botanizing", new int[] { 43 }));
/*      */         
/*  599 */         toReturn.add(Actions.actionEntrys[573]);
/*  600 */         toReturn.add(Actions.actionEntrys[575]);
/*  601 */         toReturn.add(Actions.actionEntrys[572]);
/*  602 */         toReturn.add(Actions.actionEntrys[720]);
/*      */       } else {
/*      */         
/*  605 */         toReturn.add(Actions.actionEntrys[224]);
/*      */       } 
/*  607 */     } catch (NoSuchSkillException nss) {
/*      */ 
/*      */       
/*  610 */       toReturn.add(Actions.actionEntrys[224]);
/*      */     } 
/*  612 */     return toReturn;
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
/*      */   @Nonnull
/*      */   static List<ActionEntry> getBuildableTileBehaviours(int tilex, int tiley, Creature performer, int toolTemplateId) {
/*  629 */     List<ActionEntry> toReturn = new ArrayList<>();
/*      */ 
/*      */     
/*  632 */     toReturn.addAll(getExistingStructureBehaviours(tilex, tiley, performer, toolTemplateId));
/*      */ 
/*      */     
/*  635 */     toReturn.addAll(getStructurePlanningBehaviours(tilex, tiley, performer, toolTemplateId));
/*      */     
/*  637 */     return toReturn;
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
/*      */   private static List<ActionEntry> getExistingStructureBehaviours(int tilex, int tiley, Creature performer, int toolTemplateId) {
/*  654 */     List<ActionEntry> toReturn = new ArrayList<>();
/*  655 */     Structure existingStructure = MethodsStructure.getStructureAt(tilex, tiley, performer.isOnSurface());
/*      */     
/*  657 */     if (existingStructure == null) {
/*      */       
/*  659 */       if (MethodsStructure.isCorrectToolForPlanning(performer, toolTemplateId)) {
/*      */         
/*  661 */         List<Structure> structuresNear = MethodsStructure.getStructuresNear(tilex, tiley, performer
/*  662 */             .isOnSurface());
/*      */         
/*  664 */         if (structuresNear.size() == 1)
/*      */         {
/*  666 */           for (Structure structure : structuresNear) {
/*      */             
/*  668 */             if (structure.isTypeHouse() && structure.isFinalized())
/*  669 */               toReturn.add(Actions.actionEntrys[530]); 
/*      */           } 
/*      */         }
/*      */       } 
/*  673 */       return toReturn;
/*      */     } 
/*      */     
/*  676 */     if (existingStructure.isFinalized() && existingStructure.isTypeHouse()) {
/*      */       
/*  678 */       if (MethodsStructure.isCorrectToolForBuilding(performer, toolTemplateId)) {
/*      */ 
/*      */ 
/*      */         
/*  682 */         short groundLevel = GeneralUtilities.getHeight(tilex, tiley, performer.isOnSurface());
/*  683 */         int performerAtHeight = groundLevel - (int)performer.getStatus().getPositionZ() * 10;
/*  684 */         int performerFloorLevel = performerAtHeight / 30;
/*      */ 
/*      */ 
/*      */         
/*  688 */         if (performerFloorLevel == 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  703 */           toReturn.addAll(FloorBehaviour.getCompletedFloorsBehaviour(false, performer.isOnSurface()));
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  710 */       if (toolTemplateId == 62 || toolTemplateId == 63 || (toolTemplateId == 176 && performer
/*  711 */         .getPower() >= 3))
/*      */       {
/*  713 */         toReturn.add(Actions.actionEntrys[531]);
/*      */       }
/*      */     } 
/*  716 */     return toReturn;
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
/*      */   private static List<ActionEntry> getStructurePlanningBehaviours(int tilex, int tiley, Creature performer, int toolTemplateId) {
/*  730 */     List<ActionEntry> toReturn = new ArrayList<>();
/*      */     
/*  732 */     if (!MethodsStructure.isCorrectToolForPlanning(performer, toolTemplateId)) {
/*  733 */       return toReturn;
/*      */     }
/*  735 */     if (MethodsStructure.tileBordersToFence(tilex, tiley, 0, performer.isOnSurface()))
/*      */     {
/*  737 */       return toReturn;
/*      */     }
/*  739 */     Structure planningStructure = null;
/*      */     
/*      */     try {
/*  742 */       planningStructure = performer.getStructure();
/*      */     }
/*  744 */     catch (NoSuchStructureException noSuchStructureException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  749 */     if (planningStructure != null && (planningStructure.isFinalFinished() || 
/*  750 */       System.currentTimeMillis() - planningStructure.getCreationDate() > 345600000L)) {
/*      */       
/*  752 */       performer.setStructure(null);
/*  753 */       logger.log(Level.INFO, performer.getName() + " just made another structure possible.");
/*  754 */       planningStructure = null;
/*      */     } 
/*      */ 
/*      */     
/*  758 */     if (planningStructure != null && planningStructure.contains(tilex, tiley) && !planningStructure.isFinalized()) {
/*      */       
/*  760 */       toReturn.add(Actions.actionEntrys[57]);
/*  761 */       toReturn.add(Actions.actionEntrys[58]);
/*  762 */       return toReturn;
/*      */     } 
/*      */     
/*  765 */     toReturn.add(Actions.actionEntrys[56]);
/*  766 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<ActionEntry> getTileAndFloorBehavioursFor(Creature performer, Item subject, int tilex, int tiley, int tile) {
/*  773 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  774 */     byte type = Tiles.decodeType(tile);
/*      */     
/*  776 */     if (subject != null && !subject.isTraded()) {
/*      */       
/*  778 */       int templateId = subject.getTemplateId();
/*      */       
/*  780 */       if (Servers.localServer.testServer || performer.getPower() >= 5)
/*  781 */         toReturn.add(Actions.actionEntrys[486]); 
/*  782 */       if (templateId == 301 && 
/*  783 */         WurmPermissions.mayCreateItems(performer))
/*  784 */         toReturn.add(Actions.actionEntrys[148]); 
/*  785 */       if (templateId == 176 && WurmPermissions.mayUseDeityWand(performer)) {
/*      */         
/*  787 */         int nums = -7;
/*  788 */         int tx = subject.getData1();
/*  789 */         int ty = subject.getData2();
/*  790 */         if (tx != -1 && ty != -1)
/*  791 */           nums--; 
/*  792 */         if (Servers.localServer.serverNorth != null)
/*  793 */           nums--; 
/*  794 */         if (Servers.localServer.serverEast != null)
/*  795 */           nums--; 
/*  796 */         if (Servers.localServer.serverSouth != null)
/*  797 */           nums--; 
/*  798 */         if (Servers.localServer.serverWest != null)
/*  799 */           nums--; 
/*  800 */         if (WurmPermissions.mayCreateItems(performer))
/*  801 */           nums--; 
/*  802 */         if (WurmPermissions.mayChangeTile(performer))
/*  803 */           nums--; 
/*  804 */         if (performer.getPower() >= 3) {
/*      */           
/*  806 */           nums--;
/*  807 */           if (performer.getPower() >= 5) {
/*      */             
/*  809 */             nums--;
/*  810 */             nums--;
/*  811 */             nums--;
/*  812 */             if (Servers.localServer.testServer)
/*  813 */               nums--; 
/*      */           } 
/*      */         } 
/*  816 */         boolean spike = false;
/*  817 */         if (isSpike(performer, tilex, tiley, false)) {
/*      */           
/*  819 */           spike = true;
/*  820 */           nums--;
/*      */         } 
/*  822 */         toReturn.add(new ActionEntry((short)nums, "Specials", "specials"));
/*      */         
/*  824 */         toReturn.add(Actions.actionEntrys[64]);
/*  825 */         toReturn.add(Actions.actionEntrys[88]);
/*  826 */         toReturn.add(Actions.actionEntrys[179]);
/*  827 */         toReturn.add(Actions.actionEntrys[34]);
/*  828 */         toReturn.add(Actions.actionEntrys[94]);
/*      */         
/*  830 */         if (tx != -1 && ty != -1)
/*  831 */           toReturn.add(Actions.actionEntrys[95]); 
/*  832 */         if (Servers.localServer.serverNorth != null)
/*  833 */           toReturn.add(Actions.actionEntrys[240]); 
/*  834 */         if (Servers.localServer.serverEast != null)
/*  835 */           toReturn.add(Actions.actionEntrys[241]); 
/*  836 */         if (Servers.localServer.serverSouth != null)
/*  837 */           toReturn.add(Actions.actionEntrys[242]); 
/*  838 */         if (Servers.localServer.serverWest != null)
/*  839 */           toReturn.add(Actions.actionEntrys[243]); 
/*  840 */         if (spike)
/*  841 */           toReturn.add(Actions.actionEntrys[162]); 
/*  842 */         if (WurmPermissions.mayCreateItems(performer))
/*  843 */           toReturn.add(Actions.actionEntrys[148]); 
/*  844 */         if (WurmPermissions.mayChangeTile(performer)) {
/*  845 */           toReturn.add(Actions.actionEntrys[335]);
/*      */         }
/*  847 */         if ((performer.getStatus()).visible) {
/*  848 */           toReturn.add(Actions.actionEntrys[577]);
/*      */         } else {
/*  850 */           toReturn.add(Actions.actionEntrys[578]);
/*      */         } 
/*  852 */         if (((Player)performer).GMINVULN) {
/*  853 */           toReturn.add(Actions.actionEntrys[580]);
/*      */         } else {
/*  855 */           toReturn.add(Actions.actionEntrys[579]);
/*      */         } 
/*  857 */         if (performer.getPower() >= 2) {
/*      */ 
/*      */           
/*  860 */           toReturn.add(Actions.actionEntrys[185]);
/*  861 */           if (performer.getPower() >= 5) {
/*      */             
/*  863 */             toReturn.add(Actions.actionEntrys[90]);
/*  864 */             toReturn.add(Actions.actionEntrys[194]);
/*  865 */             toReturn.add(Actions.actionEntrys[352]);
/*      */           } 
/*  867 */           if (Servers.localServer.testServer || performer.getPower() >= 5)
/*      */           {
/*  869 */             if (performer.getPower() >= 3)
/*  870 */               toReturn.add(Actions.actionEntrys[483]); 
/*      */           }
/*      */         } 
/*  873 */         toReturn.add(new ActionEntry((short)-1, "Skills", "Skills stuff"));
/*  874 */         toReturn.add(Actions.actionEntrys[92]);
/*      */         
/*      */         try {
/*  877 */           if (performer.getBody().getWounds() != null && (
/*  878 */             performer.getBody().getWounds().getWounds()).length > 0) {
/*  879 */             toReturn.add(Actions.actionEntrys[346]);
/*      */           }
/*  881 */         } catch (Exception ex) {
/*      */           
/*  883 */           logger.log(Level.WARNING, performer.getName() + ": " + ex.getMessage(), ex);
/*      */         }
/*      */       
/*  886 */       } else if (templateId == 315 && WurmPermissions.mayUseGMWand(performer)) {
/*      */         
/*  888 */         int nums = -5;
/*  889 */         int tx = subject.getData1();
/*  890 */         int ty = subject.getData2();
/*  891 */         if (tx != -1 && ty != -1)
/*  892 */           nums--; 
/*  893 */         if (Servers.localServer.serverNorth != null)
/*  894 */           nums--; 
/*  895 */         if (Servers.localServer.serverEast != null)
/*  896 */           nums--; 
/*  897 */         if (Servers.localServer.serverSouth != null)
/*  898 */           nums--; 
/*  899 */         if (Servers.localServer.serverWest != null)
/*  900 */           nums--; 
/*  901 */         toReturn.add(new ActionEntry((short)nums, "Specials", "specials"));
/*  902 */         if (tx != -1 && ty != -1) {
/*  903 */           toReturn.add(Actions.actionEntrys[95]);
/*      */         }
/*  905 */         toReturn.add(Actions.actionEntrys[64]);
/*  906 */         toReturn.add(Actions.actionEntrys[94]);
/*  907 */         toReturn.add(Actions.actionEntrys[179]);
/*  908 */         if ((performer.getStatus()).visible) {
/*  909 */           toReturn.add(Actions.actionEntrys[577]);
/*      */         } else {
/*  911 */           toReturn.add(Actions.actionEntrys[578]);
/*  912 */         }  if (((Player)performer).GMINVULN) {
/*  913 */           toReturn.add(Actions.actionEntrys[580]);
/*      */         } else {
/*  915 */           toReturn.add(Actions.actionEntrys[579]);
/*  916 */         }  if (Servers.localServer.serverNorth != null)
/*  917 */           toReturn.add(Actions.actionEntrys[240]); 
/*  918 */         if (Servers.localServer.serverEast != null)
/*  919 */           toReturn.add(Actions.actionEntrys[241]); 
/*  920 */         if (Servers.localServer.serverSouth != null)
/*  921 */           toReturn.add(Actions.actionEntrys[242]); 
/*  922 */         if (Servers.localServer.serverWest != null) {
/*  923 */           toReturn.add(Actions.actionEntrys[243]);
/*      */         }
/*      */         try {
/*  926 */           if (performer.getBody().getWounds() != null && (
/*  927 */             performer.getBody().getWounds().getWounds()).length > 0) {
/*  928 */             toReturn.add(Actions.actionEntrys[346]);
/*      */           }
/*  930 */         } catch (Exception ex) {
/*      */           
/*  932 */           logger.log(Level.WARNING, "Problem getting " + performer.getName() + "'s body wounds for HealFast action due to: " + ex
/*  933 */               .getMessage(), ex);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  938 */         nums = -1;
/*  939 */         if (type == Tiles.Tile.TILE_TREE.id || type == Tiles.Tile.TILE_BUSH.id)
/*  940 */           nums--; 
/*  941 */         toReturn.add(new ActionEntry((short)nums, "Nature", "nature"));
/*  942 */         toReturn.add(new ActionEntry((short)118, "Grow trees", "growing"));
/*  943 */         if (type == Tiles.Tile.TILE_TREE.id || type == Tiles.Tile.TILE_BUSH.id) {
/*  944 */           toReturn.add(Actions.actionEntrys[90]);
/*      */         }
/*      */       } 
/*  947 */       if (subject.isSign() || subject.isEnchantedTurret() || subject.isUnenchantedTurret())
/*  948 */         toReturn.add(Actions.actionEntrys[176]); 
/*  949 */       if (subject.isHolyItem()) {
/*      */         
/*  951 */         if (subject.isHolyItem(performer.getDeity()))
/*      */         {
/*  953 */           if (performer.isPriest() || performer.getPower() > 0) {
/*      */             
/*  955 */             float faith = performer.getFaith();
/*  956 */             Spell[] spells = performer.getDeity().getSpellsTargettingTiles((int)faith);
/*      */             
/*  958 */             if (spells.length > 0) {
/*      */               
/*  960 */               toReturn.add(new ActionEntry((short)-spells.length, "Spells", "spells"));
/*  961 */               for (int x = 0; x < spells.length; x++)
/*  962 */                 toReturn.add(Actions.actionEntrys[(spells[x]).number]); 
/*      */             } 
/*  964 */             if (performer.isLinked()) {
/*  965 */               toReturn.add(Actions.actionEntrys[399]);
/*      */             }
/*      */           } 
/*      */         }
/*  969 */       } else if (templateId == 676) {
/*      */         
/*  971 */         if (subject.getOwnerId() == performer.getWurmId()) {
/*  972 */           toReturn.add(Actions.actionEntrys[472]);
/*      */         }
/*  974 */       } else if (subject.isMagicStaff() || (subject
/*  975 */         .getTemplateId() == 176 && performer.getPower() >= 2 && Servers.isThisATestServer())) {
/*      */         
/*  977 */         List<ActionEntry> slist = new LinkedList<>();
/*      */         
/*  979 */         if (performer.knowsKarmaSpell(631))
/*  980 */           slist.add(Actions.actionEntrys[631]); 
/*  981 */         if (performer.knowsKarmaSpell(630))
/*  982 */           slist.add(Actions.actionEntrys[630]); 
/*  983 */         if (performer.knowsKarmaSpell(629))
/*  984 */           slist.add(Actions.actionEntrys[629]); 
/*  985 */         if (performer.knowsKarmaSpell(560))
/*  986 */           slist.add(Actions.actionEntrys[560]); 
/*  987 */         if (performer.knowsKarmaSpell(561))
/*      */         {
/*  989 */           slist.add(Actions.actionEntrys[561]);
/*      */         }
/*      */         
/*  992 */         if (performer.knowsKarmaSpell(562)) {
/*  993 */           slist.add(Actions.actionEntrys[562]);
/*      */         }
/*  995 */         if (performer.getPower() >= 4)
/*  996 */           toReturn.add(new ActionEntry((short)-slist.size(), "Sorcery", "casting")); 
/*  997 */         toReturn.addAll(slist);
/*      */       } 
/*  999 */       if (templateId == 901)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1004 */         toReturn.add(Actions.actionEntrys[636]);
/*      */       }
/*      */     } 
/*      */     
/* 1008 */     if (performer.getPet() != null) {
/*      */       
/* 1010 */       short nums = -2;
/* 1011 */       if (performer.getPet().isAnimal() && !performer.getPet().isReborn())
/* 1012 */         nums = (short)(nums - 1); 
/* 1013 */       toReturn.add(new ActionEntry(nums, "Pet", "Pet"));
/* 1014 */       toReturn.add(Actions.actionEntrys[41]);
/* 1015 */       toReturn.add(Actions.actionEntrys[40]);
/* 1016 */       if (performer.getPet().isAnimal() && !performer.getPet().isReborn())
/*      */       {
/* 1018 */         if (performer.getPet().isStayonline()) {
/* 1019 */           toReturn.add(Actions.actionEntrys[45]);
/*      */         } else {
/* 1021 */           toReturn.add(Actions.actionEntrys[44]);
/*      */         } 
/*      */       }
/*      */     } 
/* 1025 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/* 1033 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 1034 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, tile));
/* 1035 */     byte type = Tiles.decodeType(tile);
/* 1036 */     byte data = Tiles.decodeData(tile);
/* 1037 */     Tiles.Tile theTile = Tiles.getTile(type);
/* 1038 */     boolean water = Terraforming.isWater(tile, tilex, tiley, onSurface);
/*      */     
/* 1040 */     if (water) {
/*      */       
/* 1042 */       toReturn.add(Actions.actionEntrys[19]);
/* 1043 */       toReturn.add(Actions.actionEntrys[183]);
/* 1044 */       if (performer.getDeity() != null && performer.getDeity().isWaterGod())
/* 1045 */         Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]); 
/*      */     } 
/* 1047 */     if (performer.getVehicle() != -10L) {
/*      */       
/* 1049 */       Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle());
/* 1050 */       VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 1051 */       if (t == null || t.getStructure() == null)
/* 1052 */         if (vehicle.isChair()) {
/* 1053 */           toReturn.add(Actions.actionEntrys[708]);
/*      */         } else {
/* 1055 */           toReturn.add(Actions.actionEntrys[333]);
/*      */         }  
/* 1057 */     }  if (performer.getKingdomTemplateId() == 3 && Tiles.getTile(type).isMycelium())
/*      */     {
/* 1059 */       toReturn.add(Actions.actionEntrys[347]);
/*      */     }
/* 1061 */     if (performer.getPet() != null) {
/*      */       
/* 1063 */       short nums = -2;
/* 1064 */       if (performer.getPet().isAnimal() && !performer.getPet().isReborn())
/* 1065 */         nums = (short)(nums - 1); 
/* 1066 */       toReturn.add(new ActionEntry(nums, "Pet", "Pet"));
/* 1067 */       toReturn.add(Actions.actionEntrys[41]);
/* 1068 */       toReturn.add(Actions.actionEntrys[40]);
/* 1069 */       if (performer.getPet().isAnimal() && !performer.getPet().isReborn())
/*      */       {
/* 1071 */         if (performer.getPet().isStayonline()) {
/* 1072 */           toReturn.add(Actions.actionEntrys[45]);
/*      */         } else {
/* 1074 */           toReturn.add(Actions.actionEntrys[44]);
/*      */         }  } 
/*      */     } 
/* 1077 */     if (Tiles.isMineDoor(Tiles.decodeType(tile))) {
/*      */       
/* 1079 */       MineDoorPermission md = MineDoorPermission.getPermission(tilex, tiley);
/* 1080 */       if (md != null) {
/*      */         
/* 1082 */         List<ActionEntry> permissions = new LinkedList<>();
/*      */         
/* 1084 */         if (md.mayManage(performer) || md.isActualOwner(performer.getWurmId())) {
/* 1085 */           permissions.add(Actions.actionEntrys[364]);
/*      */         }
/* 1087 */         if (md.maySeeHistory(performer)) {
/* 1088 */           permissions.add(new ActionEntry((short)691, "History of Mine Door", "viewing"));
/*      */         }
/* 1090 */         if (!permissions.isEmpty()) {
/*      */           
/* 1092 */           if (permissions.size() > 1) {
/*      */             
/* 1094 */             Collections.sort(permissions);
/* 1095 */             toReturn.add(new ActionEntry((short)-permissions.size(), "Permissions", "viewing"));
/*      */           } 
/* 1097 */           toReturn.addAll(permissions);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1101 */     if (onSurface && type != Tiles.Tile.TILE_GRASS.id && !theTile.isBush() && !theTile.isTree()) {
/*      */ 
/*      */       
/* 1104 */       List<ActionEntry> nature = new LinkedList<>();
/* 1105 */       toReturn.addAll(getNatureMenu(performer, tilex, tiley, type, data, nature));
/*      */     } 
/* 1107 */     addEmotes(toReturn);
/* 1108 */     if (performer.isOnSurface() && performer.getPower() >= 2) {
/*      */       
/* 1110 */       if (Zones.protectedTiles[tilex][tiley]) {
/* 1111 */         toReturn.add(Actions.actionEntrys[382]);
/*      */       } else {
/* 1113 */         toReturn.add(Actions.actionEntrys[381]);
/* 1114 */       }  toReturn.add(Actions.actionEntrys[476]);
/*      */     } 
/* 1116 */     if (performer.getCultist() != null) {
/*      */       
/* 1118 */       if (performer.getCultist().mayInfoLocal())
/* 1119 */         toReturn.add(Actions.actionEntrys[185]); 
/* 1120 */       if ((type == Tiles.Tile.TILE_LAVA.id || type == Tiles.Tile.TILE_CAVE_WALL_LAVA.id) && performer.getCultist().maySpawnVolcano())
/* 1121 */         toReturn.add(new ActionEntry((short)78, "Freeze", "freezing")); 
/*      */     } 
/* 1123 */     if (performer.isTeamLeader())
/* 1124 */       toReturn.add(Actions.actionEntrys[471]); 
/* 1125 */     if (performer.getTeam() != null) {
/* 1126 */       toReturn.add(Actions.actionEntrys[470]);
/*      */     }
/*      */     
/* 1129 */     return toReturn; } public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, short action, float counter) { Vehicle vehic; int zid;
/*      */     Creature pet;
/*      */     List<TileEvent> events;
/*      */     int surf, cave, rock, caveCeil;
/*      */     String cavename;
/*      */     int ttx, tty;
/*      */     VolaTile vtile;
/* 1136 */     boolean done = true;
/* 1137 */     byte data = Tiles.decodeData(tile);
/* 1138 */     MineDoorPermission md = MineDoorPermission.getPermission(tilex, tiley);
/* 1139 */     Communicator comm = performer.getCommunicator();
/*      */     
/* 1141 */     switch (action) {
/*      */ 
/*      */       
/*      */       case 1:
/* 1145 */         handleEXAMINE(performer, tilex, tiley, tile, md);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 223:
/*      */       case 642:
/* 1151 */         done = forage(act, performer, tilex, tiley, tile, data, counter);
/*      */         break;
/*      */       
/*      */       case 569:
/*      */       case 570:
/*      */       case 571:
/* 1157 */         done = forageV11(act, performer, tilex, tiley, tile, data, counter);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 224:
/* 1162 */         done = herbalize(act, performer, tilex, tiley, tile, data, counter);
/*      */         break;
/*      */       
/*      */       case 572:
/*      */       case 573:
/*      */       case 575:
/*      */       case 720:
/* 1169 */         done = botanizeV11(act, performer, tilex, tiley, tile, data, counter);
/*      */         break;
/*      */       
/*      */       case 741:
/* 1173 */         done = collectSnow(act, performer, tilex, tiley, tile, data, counter);
/*      */         break;
/*      */       
/*      */       case 109:
/* 1177 */         done = MethodsCreatures.track(performer, tilex, tiley, tile, counter);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 347:
/* 1183 */         if (performer.getKingdomTemplateId() == 3)
/*      */         {
/* 1185 */           done = MethodsCreatures.absorb(performer, tilex, tiley, tile, counter, act);
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 38:
/* 1193 */         if (performer.isClimbing()) {
/*      */           
/* 1195 */           comm.sendNormalServerMessage("You are already climbing.", (byte)3);
/*      */           
/*      */           break;
/*      */         } 
/*      */         try {
/* 1200 */           performer.setClimbing(true);
/*      */         }
/* 1202 */         catch (Exception iox) {
/*      */           
/* 1204 */           comm.sendAlertServerMessage("Failed to start climbing. This is a bug.");
/* 1205 */           logger.log(Level.WARNING, performer
/* 1206 */               .getName() + " Failed to start climbing " + iox.getMessage(), iox);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 39:
/* 1215 */         if (!performer.isClimbing()) {
/*      */           
/* 1217 */           comm.sendNormalServerMessage("You are not climbing.");
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/*      */         try {
/* 1223 */           performer.setClimbing(false);
/*      */         }
/* 1225 */         catch (Exception iox) {
/*      */           
/* 1227 */           comm.sendAlertServerMessage("Failed to stop climbing. This is a bug.");
/* 1228 */           logger.log(Level.WARNING, performer
/* 1229 */               .getName() + " Failed to stop climbing. " + iox.getMessage(), iox);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 34:
/* 1238 */         done = true;
/* 1239 */         if (performer.getPower() > 0) {
/*      */           
/* 1241 */           if (performer.isOnSurface()) {
/*      */ 
/*      */             
/* 1244 */             BlockingResult result = Blocking.getBlockerBetween(performer, act
/* 1245 */                 .getTarget(), true, 6, performer
/*      */ 
/*      */                 
/* 1248 */                 .getBridgeId(), -10L);
/*      */             
/* 1250 */             if (result != null) {
/*      */               
/* 1252 */               Blocker firstBlocker = result.getFirstBlocker();
/* 1253 */               assert firstBlocker != null;
/* 1254 */               comm.sendNormalServerMessage("Between tiles detected blocker: " + firstBlocker
/* 1255 */                   .getName());
/*      */             } 
/*      */           } 
/*      */           
/* 1259 */           Path path = null;
/*      */           
/*      */           try {
/* 1262 */             path = performer.findPath(tilex, tiley, null);
/*      */           }
/* 1264 */           catch (NoPathException noPathException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1269 */           if (path == null) {
/*      */             
/* 1271 */             comm.sendNormalServerMessage("No path available.");
/*      */             
/*      */             break;
/*      */           } 
/* 1275 */           while (!path.isEmpty()) {
/*      */ 
/*      */             
/*      */             try {
/* 1279 */               PathTile p = path.getFirst();
/* 1280 */               ItemFactory.createItem(344, 1.0F, ((p
/*      */                   
/* 1282 */                   .getTileX() << 2) + 2), ((p
/* 1283 */                   .getTileY() << 2) + 2), 180.0F, performer
/*      */                   
/* 1285 */                   .isOnSurface(), (byte)0, performer
/*      */                   
/* 1287 */                   .getBridgeId(), null);
/*      */               
/* 1289 */               path.removeFirst();
/*      */             }
/* 1291 */             catch (FailedException|NoSuchTemplateException e) {
/*      */               
/* 1293 */               logger.log(Level.INFO, performer.getName() + " " + e.getMessage(), (Throwable)e);
/* 1294 */               comm.sendNormalServerMessage("Failed to create marker.");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 577:
/* 1320 */         performer.setVisible(false);
/* 1321 */         comm.sendSafeServerMessage("You are now invisible. Only gms can see you. Some actions and emotes may still be visible though.");
/*      */         
/* 1323 */         return true;
/*      */ 
/*      */       
/*      */       case 578:
/* 1327 */         performer.setVisible(true);
/* 1328 */         comm.sendSafeServerMessage("You are now visible again.");
/* 1329 */         return true;
/*      */ 
/*      */       
/*      */       case 579:
/* 1333 */         ((Player)performer).GMINVULN = true;
/* 1334 */         comm.sendNormalServerMessage("You are now invulnerable again.");
/* 1335 */         return true;
/*      */ 
/*      */       
/*      */       case 580:
/* 1339 */         ((Player)performer).GMINVULN = false;
/* 1340 */         comm.sendNormalServerMessage("You are now no longer invulnerable.");
/* 1341 */         return true;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 333:
/*      */       case 708:
/* 1348 */         done = true;
/* 1349 */         if (performer.getVehicle() == -10L) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/* 1354 */         vehic = Vehicles.getVehicleForId(performer.getVehicle());
/* 1355 */         if (!vehic.isCreature()) {
/*      */ 
/*      */           
/*      */           try {
/* 1359 */             Item vehicle = Items.getItem(performer.getVehicle());
/* 1360 */             if (!vehicle.isChair())
/*      */             {
/* 1362 */               if (checkTileDisembark(performer, tilex, tiley))
/*      */               {
/* 1364 */                 return true;
/*      */               }
/*      */             }
/*      */             
/* 1368 */             if (!vehicle.isChair() && (
/* 1369 */               Math.abs(vehicle.getTileX() - tilex) > 2 || Math.abs(vehicle.getTileY() - tiley) > 2)) {
/*      */               
/* 1371 */               comm.sendNormalServerMessage("That is too far away", (byte)3);
/*      */               
/*      */               break;
/*      */             } 
/* 1375 */             if (vehicle.isChair()) {
/*      */               
/* 1377 */               if (performer.getVisionArea() != null)
/* 1378 */                 performer.getVisionArea().broadCastUpdateSelectBar(performer.getWurmId(), true); 
/* 1379 */               performer.disembark(true);
/*      */               
/*      */               break;
/*      */             } 
/* 1383 */             VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 1384 */             if (t != null && t.getStructure() != null) {
/*      */               
/* 1386 */               comm
/* 1387 */                 .sendNormalServerMessage("The structure is in the way.");
/*      */               
/*      */               break;
/*      */             } 
/*      */             
/* 1392 */             BlockingResult result = Blocking.getBlockerBetween(performer, vehicle
/* 1393 */                 .getPosX(), vehicle
/* 1394 */                 .getPosY(), ((tilex << 2) + 2), ((tiley << 2) + 2), vehicle
/*      */ 
/*      */                 
/* 1397 */                 .getPosZ(), vehicle
/* 1398 */                 .getPosZ(), vehicle
/* 1399 */                 .isOnSurface(), vehicle
/* 1400 */                 .isOnSurface(), false, 4, -1L, performer
/*      */ 
/*      */ 
/*      */                 
/* 1404 */                 .getBridgeId(), performer
/* 1405 */                 .getBridgeId(), false);
/*      */             
/* 1407 */             if (result != null) {
/*      */               
/* 1409 */               comm.sendNormalServerMessage("You can't get there.");
/*      */               
/*      */               break;
/*      */             } 
/* 1413 */             if (performer.getVisionArea() != null)
/* 1414 */               performer.getVisionArea()
/* 1415 */                 .broadCastUpdateSelectBar(performer.getWurmId(), true); 
/* 1416 */             performer.disembark(true, tilex, tiley);
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1421 */           catch (NoSuchItemException nsi) {
/*      */             
/* 1423 */             comm.sendNormalServerMessage("An error has occurred. Please log on again to correct this.");
/* 1424 */             logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*      */           } 
/*      */           
/*      */           break;
/*      */         } 
/* 1429 */         if (checkTileDisembark(performer, tilex, tiley)) {
/* 1430 */           return true;
/*      */         }
/*      */         try {
/* 1433 */           Creature vehicle = Creatures.getInstance().getCreature(performer.getVehicle());
/* 1434 */           if (Math.abs(vehicle.getTileX() - tilex) <= 2 && 
/* 1435 */             Math.abs(vehicle.getTileY() - tiley) <= 2) {
/*      */             
/* 1437 */             VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 1438 */             if (t != null && t.getStructure() != null) {
/*      */               
/* 1440 */               comm.sendNormalServerMessage("The structure is in the way.");
/*      */               
/*      */               break;
/*      */             } 
/*      */             
/* 1445 */             BlockingResult result = Blocking.getBlockerBetween(performer, vehicle
/* 1446 */                 .getPosX(), vehicle
/* 1447 */                 .getPosY(), ((tilex << 2) + 2), ((tiley << 2) + 2), vehicle
/*      */ 
/*      */                 
/* 1450 */                 .getPositionZ(), vehicle
/* 1451 */                 .getPositionZ(), vehicle
/* 1452 */                 .isOnSurface(), vehicle
/* 1453 */                 .isOnSurface(), false, 4, -1L, performer
/*      */ 
/*      */ 
/*      */                 
/* 1457 */                 .getBridgeId(), performer
/* 1458 */                 .getBridgeId(), false);
/*      */             
/* 1460 */             if (result != null) {
/*      */               
/* 1462 */               comm.sendNormalServerMessage("You can't get there.");
/*      */               
/*      */               break;
/*      */             } 
/* 1466 */             if (performer.getVisionArea() != null)
/* 1467 */               performer.getVisionArea()
/* 1468 */                 .broadCastUpdateSelectBar(performer.getWurmId(), true); 
/* 1469 */             performer.disembark(true, tilex, tiley);
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/* 1474 */           comm.sendNormalServerMessage("That is too far away");
/*      */         }
/* 1476 */         catch (NoSuchCreatureException nsi) {
/*      */           
/* 1478 */           comm.sendNormalServerMessage("An error has occurred. Please log on again to correct this.");
/*      */           
/* 1480 */           logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 471:
/* 1489 */         if (!performer.isTeamLeader()) {
/*      */           break;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1501 */           TeamManagementQuestion tme = new TeamManagementQuestion(performer, "Managing the team", "Managing " + performer.getTeam().getName(), false, performer.getWurmId(), true, false);
/*      */ 
/*      */           
/* 1504 */           tme.sendQuestion();
/*      */         }
/* 1506 */         catch (Exception exception) {}
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 470:
/* 1518 */         if (performer.getTeam() != null)
/*      */         {
/* 1520 */           performer.setTeam(null, true);
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 64:
/* 1528 */         done = true;
/* 1529 */         if (performer.getPower() > 0)
/*      */         {
/* 1531 */           comm.sendNormalServerMessage("That tile is at " + tilex + ", " + tiley + ", you surfaced=" + performer
/* 1532 */               .isOnSurface());
/*      */         }
/* 1534 */         if (performer.getPower() < 4) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/* 1539 */         zid = -1;
/*      */         
/*      */         try {
/* 1542 */           Zone z = Zones.getZone(tilex, tiley, true);
/* 1543 */           zid = z.getId();
/*      */         }
/* 1545 */         catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */         
/* 1549 */         surf = Server.surfaceMesh.getTile(tilex, tiley);
/* 1550 */         cave = Server.caveMesh.getTile(tilex, tiley);
/* 1551 */         rock = Server.rockMesh.getTile(tilex, tiley);
/* 1552 */         caveCeil = Tiles.decodeData(cave);
/* 1553 */         cavename = (Tiles.getTile(Tiles.decodeType(cave))).tiledesc;
/*      */         
/* 1555 */         if (performer.getPower() >= 4) {
/*      */ 
/*      */ 
/*      */           
/* 1559 */           String msg = "ZoneId=" + zid + " Surface=" + Tiles.decodeHeight(surf) + ", rock=" + Tiles.decodeHeight(rock) + " cave=" + Tiles.decodeHeight(cave) + " ceiling=" + caveCeil;
/*      */           
/* 1561 */           if (performer.getPower() >= 5)
/*      */           {
/* 1563 */             msg = msg + ". Cave is " + cavename;
/*      */           }
/* 1565 */           comm.sendNormalServerMessage(msg);
/*      */         } 
/*      */         
/* 1568 */         ttx = performer.getTileX();
/* 1569 */         tty = (int)performer.getStatus().getPositionY() >> 2;
/* 1570 */         vtile = Zones.getOrCreateTile(tilex, tiley, performer.isOnSurface());
/* 1571 */         comm.sendNormalServerMessage("You are on " + ttx + ", " + tty + " z=" + performer
/* 1572 */             .getStatus().getPositionZ() + ". Tile here has ZoneId=" + vtile
/* 1573 */             .getZone().getId() + ". Flat=" + 
/* 1574 */             Terraforming.isFlat(ttx, tty, performer.isOnSurface(), 0));
/*      */         
/*      */         try {
/* 1577 */           Item marker = ItemFactory.createItem(344, 1.0F, null);
/* 1578 */           marker.setPosXY(((tilex << 2) + 2), ((tiley << 2) + 2));
/* 1579 */           vtile.addItem(marker, false, false);
/* 1580 */           comm.sendNormalServerMessage("The marker ended up on " + marker
/* 1581 */               .getTileX() + ", " + marker.getTileY() + ". It now has ZoneId=" + marker
/*      */               
/* 1583 */               .getZoneId());
/*      */         }
/* 1585 */         catch (FailedException|NoSuchTemplateException e) {
/*      */           
/* 1587 */           logger.log(Level.INFO, performer.getName() + " " + e.getMessage(), (Throwable)e);
/* 1588 */           comm.sendNormalServerMessage("Failed to create marker.");
/*      */         } 
/*      */         
/* 1591 */         if (onSurface) {
/*      */           
/* 1593 */           short dirtHeight = (short)(Tiles.decodeHeight(surf) - Tiles.decodeHeight(rock));
/* 1594 */           comm.sendNormalServerMessage("Dirt height = " + dirtHeight + ".");
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 19:
/* 1602 */         if (Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface())) {
/*      */           
/* 1604 */           if (WaterType.isBrackish(tilex, tiley, performer.isOnSurface())) {
/* 1605 */             comm.sendNormalServerMessage("The water tastes slightly salty."); break;
/*      */           } 
/* 1607 */           comm.sendNormalServerMessage("The water tastes fresh.");
/*      */           break;
/*      */         } 
/* 1610 */         comm.sendNormalServerMessage("The taste is very dry.");
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 183:
/* 1617 */         if (Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface())) {
/*      */           
/* 1619 */           done = false;
/* 1620 */           if (act.justTickedSecond())
/* 1621 */             done = MethodsItems.drink(performer, tilex, tiley, tile, counter, act); 
/*      */           break;
/*      */         } 
/* 1624 */         done = true;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 141:
/* 1631 */         if (performer.getDeity() != null && performer.getDeity().isWaterGod()) {
/*      */           
/* 1633 */           if (Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface())) {
/* 1634 */             done = MethodsReligion.pray(act, performer, counter); break;
/*      */           } 
/* 1636 */           done = true;
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 40:
/* 1645 */         if (performer.getPet() != null)
/*      */         {
/* 1647 */           if (DbCreatureStatus.getIsLoaded(performer.getPet().getWurmId()) == 1) {
/*      */             
/* 1649 */             performer.getCommunicator().sendNormalServerMessage("The " + performer
/* 1650 */                 .getPet().getName() + " tilts " + performer
/* 1651 */                 .getPet().getHisHerItsString() + " head while looking at you. There is a cage stopping " + performer
/*      */                 
/* 1653 */                 .getPet().getHimHerItString() + " from moving there.", (byte)3);
/*      */             
/* 1655 */             return true;
/*      */           } 
/*      */         }
/* 1658 */         done = true;
/* 1659 */         pet = performer.getPet();
/* 1660 */         if (pet != null) {
/*      */           
/* 1662 */           if (pet.isWithinDistanceTo(performer.getPosX(), performer
/* 1663 */               .getPosY(), performer
/* 1664 */               .getPositionZ(), 200.0F, 0.0F)) {
/*      */ 
/*      */ 
/*      */             
/* 1668 */             if (pet.mayReceiveOrder()) {
/*      */               
/* 1670 */               boolean ok = true;
/* 1671 */               int layer = 0;
/* 1672 */               if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE.id || Tiles.isReinforcedFloor(Tiles.decodeType(tile))) {
/* 1673 */                 layer = -1;
/* 1674 */               } else if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_EXIT.id && pet.isOnSurface()) {
/* 1675 */                 layer = -1;
/* 1676 */               } else if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*      */                 
/* 1678 */                 comm.sendNormalServerMessage("You cannot order " + pet
/* 1679 */                     .getName() + " into the rock.");
/* 1680 */                 ok = false;
/*      */               }
/* 1682 */               else if (tilex < 10 || tiley < 10 || tilex > Zones.worldTileSizeX - 10 || tiley > Zones.worldTileSizeY - 10) {
/*      */ 
/*      */                 
/* 1685 */                 comm.sendNormalServerMessage("The " + pet
/* 1686 */                     .getName() + " hesitates and does not go there.");
/* 1687 */                 ok = false;
/*      */               } 
/* 1689 */               Village v = Villages.getVillage(tilex, tiley, true);
/* 1690 */               if (v != null)
/*      */               {
/* 1692 */                 if (v.isEnemy(performer)) {
/*      */                   
/* 1694 */                   comm.sendNormalServerMessage("The " + pet
/* 1695 */                       .getName() + " hesitates and does not enter " + v.getName() + ".");
/*      */                   
/* 1697 */                   ok = false;
/*      */                 } 
/*      */               }
/* 1700 */               if (pet.getHitched() != null || pet.isRidden()) {
/*      */                 
/* 1702 */                 comm.sendNormalServerMessage("The " + pet
/* 1703 */                     .getName() + " is restrained and ignores your order.");
/* 1704 */                 ok = false;
/*      */               } 
/* 1706 */               if (ok) {
/*      */                 
/* 1708 */                 Order o = new Order(tilex, tiley, layer);
/* 1709 */                 pet.addOrder(o);
/* 1710 */                 comm.sendNormalServerMessage("You issue an order to " + pet.getName() + ".");
/*      */               } 
/*      */               break;
/*      */             } 
/* 1714 */             comm.sendNormalServerMessage("The " + pet.getName() + " ignores your order.");
/*      */             break;
/*      */           } 
/* 1717 */           comm.sendNormalServerMessage("The " + pet.getName() + " is too far away.");
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 41:
/* 1723 */         pet = performer.getPet();
/* 1724 */         if (pet != null) {
/*      */           
/* 1726 */           if (pet.isWithinDistanceTo(performer.getPosX(), performer
/* 1727 */               .getPosY(), performer
/* 1728 */               .getPositionZ(), 200.0F, 0.0F)) {
/*      */ 
/*      */ 
/*      */             
/* 1732 */             pet.clearOrders();
/* 1733 */             comm.sendNormalServerMessage("You order the " + pet
/* 1734 */                 .getName() + " to forget all you told " + pet
/* 1735 */                 .getHimHerItString() + ".");
/*      */             break;
/*      */           } 
/* 1738 */           comm.sendNormalServerMessage("The " + pet.getName() + " is too far away.");
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 352:
/* 1747 */         done = true;
/* 1748 */         if (performer.getPower() >= 5) {
/*      */           
/* 1750 */           comm.sendNormalServerMessage("Logging on = " + ((-10L == performer.loggerCreature1) ? 1 : 0));
/* 1751 */           if (performer.loggerCreature1 == -10L) {
/* 1752 */             performer.loggerCreature1 = performer.getWurmId(); break;
/*      */           } 
/* 1754 */           performer.loggerCreature1 = -10L;
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 45:
/* 1762 */         done = true;
/* 1763 */         if (performer.getPet() != null) {
/*      */           
/* 1765 */           if (performer.getPet().isAnimal() && !performer.getPet().isReborn()) {
/*      */             
/* 1767 */             performer.getPet().setStayOnline(false);
/* 1768 */             comm.sendNormalServerMessage("The " + performer
/* 1769 */                 .getPet().getName() + " will now leave the world when you do.");
/*      */             break;
/*      */           } 
/* 1772 */           comm.sendNormalServerMessage("The " + performer
/* 1773 */               .getPet().getName() + " may not go offline.");
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 44:
/* 1779 */         done = true;
/* 1780 */         if (performer.getPet() != null) {
/*      */           
/* 1782 */           if (performer.getPet().isAnimal() && !performer.getPet().isReborn()) {
/*      */             
/* 1784 */             performer.getPet().setStayOnline(true);
/* 1785 */             comm.sendNormalServerMessage("The " + performer
/* 1786 */                 .getPet().getName() + " will now stay in the world when you log off.");
/*      */             
/*      */             break;
/*      */           } 
/* 1790 */           comm.sendNormalServerMessage("The " + performer
/* 1791 */               .getPet().getName() + " may not go offline.");
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 364:
/* 1800 */         if (md != null && (md.mayManage(performer) || md.isActualOwner(performer.getWurmId()))) {
/*      */           
/* 1802 */           ManagePermissions mp = new ManagePermissions(performer, ManageObjectList.Type.MINEDOOR, (PermissionsPlayerList.ISettings)md, false, -10L, false, null, "");
/*      */           
/* 1804 */           mp.sendQuestion();
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 691:
/* 1812 */         if (md != null && md.maySeeHistory(performer)) {
/*      */           
/* 1814 */           PermissionsHistory ph = new PermissionsHistory(performer, md.getWurmId());
/* 1815 */           ph.sendQuestion();
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 476:
/* 1823 */         if (performer.getPower() <= 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1827 */         if (!Constants.useTileEventLog) {
/*      */           
/* 1829 */           comm.sendNormalServerMessage("This server does not register tile events.");
/*      */           
/*      */           break;
/*      */         } 
/* 1833 */         if (performer.getLogger() != null) {
/* 1834 */           performer.getLogger().log(Level.INFO, performer
/* 1835 */               .getName() + " checked tile logs @" + tilex + "," + tiley + "," + performer.isOnSurface());
/*      */         }
/*      */         
/* 1838 */         events = TileEvent.getEventsFor(tilex, tiley, performer.isOnSurface() ? 0 : -1);
/* 1839 */         if (!events.isEmpty()) {
/*      */           
/* 1841 */           for (TileEvent t : events)
/*      */           {
/* 1843 */             comm.sendNormalServerMessage(getStringForTileEvent(t));
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/* 1848 */         comm.sendNormalServerMessage("No events registered here.");
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 240:
/*      */       case 241:
/*      */       case 242:
/*      */       case 243:
/* 1858 */         done = Methods.transferPlayer(performer, performer, act, counter);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 162:
/* 1864 */         if (performer.getPower() <= 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1868 */         if (isSpike(performer, tilex, tiley, true)) {
/*      */           
/* 1870 */           comm.sendNormalServerMessage("You level the terrain.");
/*      */           
/*      */           break;
/*      */         } 
/* 1874 */         comm.sendNormalServerMessage("The terrain was not considered to contain a spike or hole.");
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 381:
/* 1882 */         if (!performer.isOnSurface() || performer.getPower() < 2) {
/*      */           break;
/*      */         }
/*      */         
/* 1886 */         Zones.protectedTiles[tilex][tiley] = true;
/* 1887 */         if (Zones.isOnPvPServer(tilex, tiley)) {
/*      */           
/* 1889 */           comm.sendNormalServerMessage("You protect the tile. Please note that this should be extremely rare on pvp servers as it may be regarded as power abuse.");
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1895 */         comm.sendNormalServerMessage("You protect the tile.");
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 382:
/* 1901 */         if (performer.isOnSurface() && performer.getPower() >= 2) {
/*      */           
/* 1903 */           Zones.protectedTiles[tilex][tiley] = false;
/* 1904 */           comm.sendNormalServerMessage("You remove the  protection from the tile.");
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 388:
/* 1912 */         if (Methods.isActionAllowed(performer, (short)384) && performer
/* 1913 */           .getCultist() != null && performer
/* 1914 */           .getCultist().mayEnchantNature())
/*      */         {
/* 1916 */           done = Terraforming.enchantNature(performer, tilex, tiley, onSurface, tile, counter, act);
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 185:
/* 1924 */         done = true;
/* 1925 */         if (Methods.isActionAllowed(performer, (short)384) && performer
/* 1926 */           .getCultist() != null && performer.getCultist().mayInfoLocal()) {
/*      */           
/* 1928 */           performer.getVisionArea().getSurface().sendHostileCreatures();
/* 1929 */           performer.getVisionArea().getUnderGround().sendHostileCreatures();
/* 1930 */           performer.getCultist().touchCooldown2();
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 78:
/* 1938 */         done = true;
/* 1939 */         if (!Methods.isActionAllowed(performer, (short)384) || performer
/* 1940 */           .getCultist() == null) {
/*      */           
/* 1942 */           comm.sendNormalServerMessage("You do not have that power.");
/*      */           break;
/*      */         } 
/* 1945 */         if (!performer.getCultist().maySpawnVolcano()) {
/*      */           
/* 1947 */           comm.sendNormalServerMessage("Nothing happens.");
/*      */           break;
/*      */         } 
/* 1950 */         done = Terraforming.freezeLava(performer, tilex, tiley, onSurface, tile, counter, true);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 399:
/* 1957 */         performer.disableLink();
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 180:
/* 1972 */         done = destroyAllFloorsAt(act, performer, counter);
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1979 */     return done; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void handleEXAMINE(Creature performer, int tilex, int tiley, int tile, MineDoorPermission md) {
/* 1985 */     Communicator comm = performer.getCommunicator();
/*      */     
/* 1987 */     byte decodedTileType = Tiles.decodeType(tile);
/*      */     
/* 1989 */     if (Tiles.isMineDoor(decodedTileType)) {
/*      */ 
/*      */       
/* 1992 */       String doorName = "";
/* 1993 */       String ownerName = "Unknown";
/* 1994 */       String ownerSig = "";
/* 1995 */       boolean maypass = false;
/* 1996 */       int str = Server.getWorldResource(tilex, tiley);
/*      */       
/* 1998 */       if (md != null) {
/*      */         
/* 2000 */         long oId = md.getOwnerId();
/* 2001 */         ownerName = PlayerInfoFactory.getPlayerName(oId);
/* 2002 */         int ql = str / 100;
/* 2003 */         if (ql >= 20 && ql < 90) {
/* 2004 */           ownerSig = Item.obscureWord(ownerName, ql);
/* 2005 */         } else if (ql >= 90) {
/* 2006 */           ownerSig = ownerName;
/* 2007 */         }  doorName = md.getObjectName();
/* 2008 */         maypass = md.mayPass(performer);
/*      */       } 
/*      */       
/* 2011 */       switch (decodedTileType) {
/*      */         
/*      */         case 27:
/* 2014 */           comm.sendNormalServerMessage("You see a golden door.");
/*      */           break;
/*      */         case 28:
/* 2017 */           comm.sendNormalServerMessage("You see a silver door.");
/*      */           break;
/*      */         case 29:
/* 2020 */           comm.sendNormalServerMessage("You see a steel door.");
/*      */           break;
/*      */         case 25:
/* 2023 */           comm.sendNormalServerMessage("You see a wooden mine door.");
/*      */           break;
/*      */         case 26:
/* 2026 */           comm.sendNormalServerMessage("You see hard rock.");
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 2031 */       if (decodedTileType == 26) {
/*      */ 
/*      */         
/* 2034 */         if (performer.getPower() > 0 || Server.rand
/* 2035 */           .nextInt(100) <= performer.getMindLogical().getKnowledge(0.0D))
/*      */         {
/* 2037 */           comm.sendNormalServerMessage("You skillfully detect a mine door in the rock.");
/* 2038 */           comm.sendNormalServerMessage("Strength=" + str + ".");
/* 2039 */           if (!doorName.isEmpty())
/*      */           {
/* 2041 */             comm.sendNormalServerMessage("You notice something chiselled out near the top of the door, it's \"" + doorName + "\"");
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2051 */         comm.sendNormalServerMessage("Strength=" + str + ".");
/*      */         
/* 2053 */         int template = MethodsStructure.getImproveItem(decodedTileType);
/*      */         
/*      */         try {
/* 2056 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(template);
/* 2057 */           comm.sendNormalServerMessage("It could be improved with " + temp.getNameWithGenus() + ".");
/*      */         }
/* 2059 */         catch (NoSuchTemplateException noSuchTemplateException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2064 */         if (!doorName.isEmpty())
/*      */         {
/* 2066 */           comm.sendNormalServerMessage("You notice something inscribed near the top of the door, it's \"" + doorName + "\"");
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2073 */       if (performer.getPower() >= 2 && !ownerName.isEmpty())
/*      */       {
/* 2075 */         comm.sendNormalServerMessage("In the bottom right corner, you notice a signature of \"" + ownerName + "\"");
/*      */       
/*      */       }
/* 2078 */       else if (maypass && !ownerSig.isEmpty())
/*      */       {
/* 2080 */         comm.sendNormalServerMessage("In the bottom right corner, you notice a signature of \"" + ownerSig + "\"");
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 2085 */     else if (Tiles.decodeHeight(tile) < -7) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2090 */       comm.sendNormalServerMessage("You see the glittering surface of water.");
/*      */     }
/*      */     else {
/*      */       
/* 2094 */       comm.sendNormalServerMessage(getTileDescription(tile));
/*      */     } 
/* 2096 */     sendVillageString(performer, tilex, tiley, true);
/* 2097 */     sendTileTransformationState(performer, tilex, tiley, decodedTileType);
/*      */     
/* 2099 */     Trap t = Trap.getTrap(tilex, tiley, performer.getLayer());
/*      */     
/* 2101 */     if (performer.getPower() >= 3) {
/*      */ 
/*      */       
/* 2104 */       comm.sendNormalServerMessage("Your rot: " + 
/* 2105 */           Creature.normalizeAngle(performer.getStatus().getRotation()) + ", Wind rot=" + 
/* 2106 */           Server.getWeather().getWindRotation() + ", pow=" + 
/* 2107 */           Server.getWeather().getWindPower() + " x=" + 
/* 2108 */           Server.getWeather().getXWind() + ", y=" + Server.getWeather().getYWind());
/*      */       
/* 2110 */       comm.sendNormalServerMessage("Tile is spring=" + Zone.hasSpring(tilex, tiley));
/*      */       
/* 2112 */       if (performer.getPower() >= 5)
/*      */       {
/* 2114 */         comm.sendNormalServerMessage("tilex: " + tilex + ", tiley=" + tiley);
/*      */       }
/*      */       
/* 2117 */       if (t != null) {
/*      */         
/* 2119 */         String str = "none";
/* 2120 */         if (t.getVillage() > 0) {
/*      */           
/*      */           try {
/*      */             
/* 2124 */             str = Villages.getVillage(t.getVillage()).getName();
/*      */           }
/* 2126 */           catch (NoSuchVillageException noSuchVillageException) {}
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2131 */         comm.sendNormalServerMessage("A " + t.getName() + ", ql=" + t
/* 2132 */             .getQualityLevel() + " kingdom=" + 
/* 2133 */             Kingdoms.getNameFor(t.getKingdom()) + ", vill=" + str + ", rotdam=" + t
/*      */             
/* 2135 */             .getRotDamage() + " firedam=" + t
/* 2136 */             .getFireDamage() + " speed=" + t
/* 2137 */             .getSpeedBon());
/*      */       } 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 2145 */     if (t == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 2150 */     if (t.getKingdom() != performer.getKingdomId() && performer.getDetectDangerBonus() <= 0.0F) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 2155 */     String qlString = "average";
/* 2156 */     if (t.getQualityLevel() < 20) {
/* 2157 */       qlString = "low";
/* 2158 */     } else if (t.getQualityLevel() > 80) {
/* 2159 */       qlString = "deadly";
/* 2160 */     } else if (t.getQualityLevel() > 50) {
/* 2161 */       qlString = "high";
/*      */     } 
/* 2163 */     String villageName = ".";
/* 2164 */     if (t.getVillage() > 0) {
/*      */       
/*      */       try {
/*      */         
/* 2168 */         villageName = " of " + Villages.getVillage(t.getVillage()).getName() + ".";
/*      */       }
/* 2170 */       catch (NoSuchVillageException noSuchVillageException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2176 */     String rotDam = "";
/* 2177 */     if (t.getRotDamage() > 0)
/*      */     {
/* 2179 */       rotDam = " It has ugly black-green speckles.";
/*      */     }
/*      */     
/* 2182 */     String fireDam = "";
/* 2183 */     if (t.getFireDamage() > 0)
/*      */     {
/* 2185 */       fireDam = " It has the rune of fire.";
/*      */     }
/*      */     
/* 2188 */     comm.sendNormalServerMessage("You detect a " + t
/* 2189 */         .getName() + " here, of " + qlString + " quality. It has been set by people from " + 
/*      */         
/* 2191 */         Kingdoms.getNameFor(t.getKingdom()) + villageName + rotDam + fireDam);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/* 2200 */     boolean done = true;
/* 2201 */     int stid = source.getTemplateId();
/* 2202 */     byte type = Tiles.decodeType(tile);
/*      */     
/* 2204 */     Communicator comm = performer.getCommunicator();
/* 2205 */     switch (action)
/*      */     
/*      */     { 
/*      */ 
/*      */       
/*      */       case 150:
/*      */       case 532:
/* 2212 */         if (Flattening.isTileTooDeep(tilex, tiley, 2, 2, 4)) {
/*      */           
/* 2214 */           if (source.isDredgingTool())
/*      */           {
/* 2216 */             done = Flattening.flatten(performer, source, tile, tilex, tiley, counter, act);
/*      */           }
/*      */           else
/*      */           {
/* 2220 */             comm.sendNormalServerMessage("You need a dredge to do that at that depth.");
/* 2221 */             done = true;
/*      */           }
/*      */         
/*      */         }
/* 2225 */         else if (source.isDiggingtool()) {
/*      */           
/* 2227 */           done = Flattening.flatten(performer, source, tile, tilex, tiley, counter, act);
/*      */         } else {
/*      */           
/* 2230 */           done = action(act, performer, tilex, tiley, onSurface, tile, action, counter);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3027 */         return done;case 144: if (source.isDiggingtool() && onSurface) { done = Terraforming.dig(performer, source, tilex, tiley, tile, counter, false, performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 921: if (source.isDiggingtool() && onSurface) { done = Terraforming.dig(performer, source, tilex, tiley, tile, counter, false, performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh, true); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 362: if (source.isDredgingTool() && onSurface) { done = Terraforming.dig(performer, source, tilex, tiley, tile, counter, false, performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 604: if (source.getTemplateId() == 176 && performer.getPower() >= 2) { Terraforming.paintTerrain((Player)performer, source, tilex, tiley); done = true; } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 927: if (Terraforming.isSwitchableTiles(source.getTemplateId(), type)) { done = Terraforming.switchTileTypes(performer, source, tilex, tiley, counter, act); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 154: if (source.isDiggingtool() && onSurface) { done = Terraforming.pack(performer, source, tilex, tiley, onSurface, tile, counter, act); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 155: if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MARSH.id) { if (stid == 495) done = Terraforming.makeFloor(performer, source, tilex, tiley, onSurface, tile, counter);  } else if (!onSurface && source.isCavePaveable()) { if (Tiles.isRoadType(type)) { HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface); if (!MethodsHighways.onHighway(highwaypos)) { comm.sendSafeServerMessage("You can only replace paving on highways."); return true; }  }  done = Terraforming.pave(performer, source, tilex, tiley, onSurface, tile, counter, act); } else if (onSurface && source.isPaveable() && source.getTemplateId() != 495) { if (Tiles.isRoadType(type)) { if (performer.getStrengthSkill() < 20.0D) { performer.getCommunicator().sendNormalServerMessage("You need to be stronger to replace pavement."); return true; }  HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface); if (!MethodsHighways.onHighway(highwaypos)) { comm.sendSafeServerMessage("You can only replace paving on highways."); return true; }  }  done = Terraforming.pave(performer, source, tilex, tiley, onSurface, tile, counter, act); }  return done;case 576: if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MARSH.id) { if (stid == 495) done = Terraforming.makeFloor(performer, source, tilex, tiley, onSurface, tile, counter);  } else if (!onSurface && source.isCavePaveable()) { if (Tiles.isRoadType(type)) { if (performer.getStrengthSkill() < 20.0D) { performer.getCommunicator().sendNormalServerMessage("You need to be stronger to replace pavement."); return true; }  HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface); if (!MethodsHighways.onHighway(highwaypos)) { comm.sendSafeServerMessage("You can only replace paving on highways."); return true; }  }  done = Terraforming.pave(performer, source, tilex, tiley, onSurface, tile, counter, act); } else if (onSurface && source.isPaveable() && source.getTemplateId() != 495) { done = Terraforming.pave(performer, source, tilex, tiley, onSurface, tile, counter, act); }  return done;case 318: if (stid == 27 || stid == 25) { done = Terraforming.cultivate(performer, source, tilex, tiley, onSurface, tile, counter); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 160: if (source.getTemplateId() == 1343 || source.getTemplateId() == 705 || source.getTemplateId() == 707 || source.getTemplateId() == 1344 || source.getTemplateId() == 1346) done = MethodsFishing.fish(performer, source, tilex, tiley, tile, counter, act);  return done;case 285: done = true; if (source.getTemplateId() == 1344 || source.getTemplateId() == 1343 || source.getTemplateId() == 705 || source.getTemplateId() == 707 || source.getTemplateId() == 1346) done = MethodsFishing.showFishTable(performer, source, tilex, tiley, counter, act);  return done;case 109: done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); return done;case 191: if (source.isDiggingtool()) { done = Terraforming.destroyPave(performer, source, tilex, tiley, onSurface, tile, counter); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 231: if (stid == 153 && Tiles.decodeType(tile) == Tiles.Tile.TILE_PLANKS.id && onSurface) { done = Terraforming.tarFloor(performer, source, tilex, tiley, onSurface, tile, counter); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 141: if (!Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface())) { done = true; } else if (performer.getDeity() == null || !performer.getDeity().isWaterGod()) { done = true; } else { done = MethodsReligion.pray(act, performer, counter); }  return done;case 374: if (source.isTrap()) if (Trap.mayTrapTemplateOnTile(stid, type) || (stid == 612 && Trap.mayPlantCorrosion(tilex, tiley, performer.getLayer()))) if (Trap.getTrap(tilex, tiley, performer.getLayer()) == null) return Trap.trap(performer, source, tile, tilex, tiley, performer.getLayer(), counter, act);    return done;case 375: return Trap.disarm(performer, source, tilex, tiley, performer.getLayer(), counter, act);case 56: done = MethodsStructure.buildPlan(performer, source, tilex, tiley, tile, counter); return done;case 530: done = MethodsStructure.expandHouseTile(performer, source, tilex, tiley, tile, counter); return done;case 531: done = MethodsStructure.removeHouseTile(performer, tilex, tiley, tile, counter); return done;case 57: done = MethodsStructure.buildPlanRemove(performer, tilex, tiley, tile, counter); return done;case 58: MethodsStructure.tryToFinalize(performer, tilex, tiley); return done;case 508: return MethodsStructure.floorPlanAbove(performer, source, tilex, tiley, tile, performer.getLayer(), counter, act, StructureConstants.FloorType.FLOOR);case 514: return MethodsStructure.floorPlanAbove(performer, source, tilex, tiley, tile, performer.getLayer(), counter, act, StructureConstants.FloorType.DOOR);case 515: return MethodsStructure.floorPlanAbove(performer, source, tilex, tiley, tile, performer.getLayer(), counter, act, StructureConstants.FloorType.OPENING);case 509: return MethodsStructure.floorPlanBelow(performer, source, tilex, tiley, tile, performer.getLayer(), counter, act);case 507: return MethodsStructure.floorPlanRoof(performer, source, tilex, tiley, tile, performer.getLayer(), counter, act);case 95: done = true; MethodsCreatures.teleportCreature(performer, source); return done;case 94: done = true; if (((stid == 176 || stid == 315) && performer.getPower() >= 2) || (stid == 174 && performer.getPower() >= 1)) { Methods.sendTeleportQuestion(performer, source); } else if (stid == 174 || stid == 524 || stid == 525) { MethodsCreatures.teleportSet(performer, source, tilex, tiley); }  return done;case 577: performer.setVisible(false); comm.sendSafeServerMessage("You are now invisible. Only gms can see you. Some actions and emotes may still be visible though."); return true;case 578: performer.setVisible(true); comm.sendSafeServerMessage("You are now visible again."); return true;case 579: ((Player)performer).GMINVULN = true; comm.sendNormalServerMessage("You are now invulnerable again."); return true;case 580: ((Player)performer).GMINVULN = false; comm.sendNormalServerMessage("You are now no longer invulnerable."); return true;case 88: done = true; if ((stid == 176 && performer.getPower() >= 4) || (stid == 176 && performer.getPower() >= 2 && Servers.isThisATestServer())) { Methods.sendTileDataQuestion(performer, source, tilex, tiley); } else { logger.log(Level.WARNING, performer.getName() + " tried to set tile data without a wand or power."); }  return done;case 148: done = true; if (WurmPermissions.mayCreateItems(performer) && (stid == 176 || stid == 301)) Methods.sendCreateQuestion(performer, source);  return done;case 335: done = true; if (WurmPermissions.mayChangeTile(performer) && stid == 176) Methods.sendTerraformingQuestion(performer, source, tilex, tiley);  return done;case 369: if (stid == 602) done = Terraforming.obliterate(performer, act, source, tilex, tiley, tile, counter, source.getAuxData(), performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh);  return done;case 90: done = true; if (Servers.localServer.entryServer && (Tiles.decodeType(tile) == Tiles.Tile.TILE_TREE.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_BUSH.id)) { byte aData = Tiles.decodeData(tile); int age = aData >> 4 & 0xF; if (age < 14) { int tree = aData & 0xF; age++; int newData = (age << 4) + tree & 0xFF; Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.decodeType(tile), (byte)newData); Players.getInstance().sendChangedTile(tilex, tiley, true, false); }  }  if (performer.getPower() >= 5) if (stid == 176) { boolean oldSurf = TilePoller.pollingSurface; TilePoller.pollingSurface = true; TilePoller.currentMesh = Server.surfaceMesh; TilePoller.checkEffects(tile, tilex, tiley, Tiles.decodeType(tile), Tiles.decodeData(tile)); comm.sendNormalServerMessage("You poll " + tilex + "," + tiley + " surfaced=" + TilePoller.pollingSurface); TilePoller.pollingSurface = oldSurf; if (!oldSurf) TilePoller.currentMesh = Server.caveMesh;  VolaTile tempvtile1 = Zones.getTileOrNull(tilex, tiley, onSurface); if (tempvtile1 != null) { tempvtile1.pollStructures(System.currentTimeMillis()); tempvtile1.poll(true, 0, false); }  }   return done;case 185: done = true; handle_GETINFO(performer, tilex, tiley, stid); return done;case 194: done = true; if (performer.getPower() >= 5) Methods.sendPlayerPaymentQuestion(performer);  return done;case 352: done = true; if (performer.getPower() >= 5) { comm.sendNormalServerMessage("Logging on = " + ((-10L == performer.loggerCreature1) ? 1 : 0)); if (performer.loggerCreature1 == -10L) { performer.loggerCreature1 = performer.getWurmId(); } else { performer.loggerCreature1 = -10L; }  }  return done;case 179: done = true; if ((stid == 176 || stid == 315) && WurmPermissions.mayUseGMWand(performer)) Methods.sendSummonQuestion(performer, source, tilex, tiley, -10L);  return done;case 675: if (performer.getTaggedItemId() == -10L) { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); } else { done = true; if ((stid == 176 || stid == 315) && WurmPermissions.mayUseGMWand(performer)) try { Item target = Items.getItem(performer.getTaggedItemId()); Zone currZone = Zones.getZone((int)target.getPosX() >> 2, (int)target.getPosY() >> 2, target.isOnSurface()); currZone.removeItem(target); target.putItemInfrontof(performer); } catch (NoSuchZoneException nsz) { comm.sendNormalServerMessage("Failed to locate the zone for that item. Failed to summon."); logger.log(Level.WARNING, performer.getTaggedItemId() + ": " + nsz.getMessage(), (Throwable)nsz); } catch (NoSuchCreatureException nsc) { comm.sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon."); logger.log(Level.WARNING, performer.getTaggedItemId() + ": " + nsc.getMessage(), (Throwable)nsc); } catch (NoSuchItemException nsi) { comm.sendNormalServerMessage("Failed to locate the item for that request! Failed to summon."); logger.log(Level.WARNING, performer.getTaggedItemId() + ":" + nsi.getMessage(), (Throwable)nsi); } catch (NoSuchPlayerException nsp) { comm.sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon."); logger.log(Level.WARNING, performer.getTaggedItemId() + ":" + nsp.getMessage(), (Throwable)nsp); }   performer.setTagItem(-10L, ""); }  return done;case 176: if (source.isSign()) { done = MethodsItems.plantSign(performer, source, counter, false, 0, 0, performer.isOnSurface(), performer.getBridgeId(), false, -1L); } else { done = true; }  return done;case 189: if (Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface())) MethodsItems.fillContainer(source, performer, WaterType.isBrackish(tilex, tiley, performer.isOnSurface()));  done = true; return done;case 19: if (Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface())) { if (WaterType.isBrackish(tilex, tiley, performer.isOnSurface())) { comm.sendNormalServerMessage("The water tastes slightly salty."); } else { comm.sendNormalServerMessage("The water tastes fresh."); }  } else { comm.sendNormalServerMessage("The taste is very dry."); }  return done;case 183: if (Terraforming.isWater(tile, tilex, tiley, performer.isOnSurface())) { done = false; if (act.justTickedSecond()) done = MethodsItems.drink(performer, tilex, tiley, tile, counter, act);  } else { done = true; }  return done;case 192: if (Tiles.isMineDoor(type)) if (type != Tiles.Tile.TILE_MINE_DOOR_STONE.id) { MineDoorPermission md = MineDoorPermission.getPermission(tilex, tiley); if (md != null && md.mayManage(performer)) done = MethodsStructure.improveTileDoor(performer, source, tilex, tiley, tile, act, counter);  }   return done;case 92: if (WurmPermissions.mayUseDeityWand(performer) && stid == 176) Methods.sendLearnSkillQuestion(performer, source, -10L);  return done;case 346: if ((stid == 176 || stid == 315) && WurmPermissions.mayUseGMWand(performer)) try { Wound[] wounds = performer.getBody().getWounds().getWounds(); for (Wound wound : wounds) wound.heal();  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   return done;case 329: if (stid == 489 || (WurmPermissions.mayUseGMWand(performer) && (stid == 176 || source.getTemplateId() == 315))) done = MethodsItems.watchSpyglass(performer, source, act, counter);  return done;case 906: if (stid == 1115 || (source.isWand() && Tiles.isMineDoor(Tiles.decodeType(tile)))) done = Terraforming.removeMineDoor(performer, act, source, tilex, tiley, onSurface, counter);  return done;case 174: if ((source.isWeapon() || source.isWand()) && Tiles.isMineDoor(Tiles.decodeType(tile))) done = Terraforming.destroyMineDoor(performer, act, source, tilex, tiley, onSurface, counter);  return done;case 180: done = destroyAllFloorsAt(act, performer, counter); return done;case 118: if (source.getTemplateId() == 315 && WurmPermissions.mayUseGMWand(performer) && !Zones.isOnPvPServer(tilex, tiley)) { Terraforming.rampantGrowth(performer, tilex, tiley); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 945: if (source.getTemplate().isRune()) if (RuneUtilities.isSingleUseRune(source) && RuneUtilities.getSpellForRune(source) != null && RuneUtilities.getSpellForRune(source).isTargetTile()) done = useRuneOnTile(act, performer, source, tilex, tiley, performer.getLayer(), heightOffset, action, counter);   return done;case 910: if (onSurface && (source.getTemplate().isDiggingtool() || source.getTemplateId() == 493)) done = investigateTile(act, performer, source, tilex, tiley, performer.getLayer(), tile, heightOffset, action, counter);  return done;case 636: if (source.getTemplateId() == 901) { done = hold(act, performer, source, tilex, tiley, tile, counter); } else { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }  return done;case 472: done = true; if (source.getTemplateId() == 676 && source.getOwnerId() == performer.getWurmId()) { MissionManager m = new MissionManager(performer, "Manage missions", "Select action", act.getTarget(), Tiles.getTile(type).getHelpSubject(Tiles.decodeData(tile)), source.getWurmId()); m.sendQuestion(); }  return done;case 486: handle_TESTCASE(performer, source, tilex, tiley, tile); done = true; return done;case 462: if (Features.Feature.TRANSFORM_RESOURCE_TILES.isEnabled()) if (onSurface) { if (source.getTemplateId() == 654 && source.getAuxData() != 0 && source.getBless() != null) { if (Features.Feature.TRANSFORM_TO_RESOURCE_TILES.isEnabled() && ((source.getAuxData() == 1 && type == Tiles.Tile.TILE_SAND.id) || (source.getAuxData() == 2 && type == Tiles.Tile.TILE_GRASS.id) || (source.getAuxData() == 2 && type == Tiles.Tile.TILE_MYCELIUM.id) || (source.getAuxData() == 3 && type == Tiles.Tile.TILE_STEPPE.id) || (source.getAuxData() == 7 && type == Tiles.Tile.TILE_MOSS.id))) { if (Zones.getKingdom(tilex, tiley) != performer.getKingdomId()) { comm.sendNormalServerMessage("You can only transmutate to a resource tiles within your own kingdom influence.", (byte)3); } else { done = handle_TRANSMUTATE(performer, source, tilex, tiley, tile, act, counter); }  } else if ((source.getAuxData() == 4 && type == Tiles.Tile.TILE_CLAY.id) || (source.getAuxData() == 5 && type == Tiles.Tile.TILE_PEAT.id) || (source.getAuxData() == 6 && type == Tiles.Tile.TILE_TAR.id) || (source.getAuxData() == 8 && type == Tiles.Tile.TILE_TUNDRA.id)) { done = handle_TRANSMUTATE(performer, source, tilex, tiley, tile, act, counter); } else { comm.sendNormalServerMessage("That would be a waste of this liquid."); }  } else { comm.sendNormalServerMessage("That would be a waste of this liquid."); }  } else { comm.sendNormalServerMessage("That would be a waste of this liquid."); }   return done; }  if (source.isEnchantedTurret() || source.isUnenchantedTurret()) { if (source.isSign()) { done = MethodsItems.plantSign(performer, source, counter, false, 0, 0, performer.isOnSurface(), performer.getBridgeId(), false, -1L); } else { done = true; }  } else if (!act.isSpell()) { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); } else { done = true; Spell spell = Spells.getSpell(action); if (spell == null) { logger.log(Level.INFO, performer.getName() + " tries to cast unknown spell:" + Actions.actionEntrys[action].getActionString()); comm.sendNormalServerMessage("That spell is unknown."); } else if (spell.religious) { if (performer.getDeity() == null) { comm.sendNormalServerMessage("You have no deity and cannot cast the spell."); } else if (!source.isHolyItem(performer.getDeity()) && !performer.isSpellCaster() && !performer.isSummoner()) { comm.sendNormalServerMessage((performer.getDeity()).name + " will not let you use that item."); } else if (Methods.isActionAllowed(performer, (short)245)) { done = Methods.castSpell(performer, spell, tilex, tiley, performer.getLayer(), heightOffset, counter); }  } else if (performer.isSpellCaster() || performer.isSummoner() || source.isMagicStaff() || (source.getTemplateId() == 176 && performer.getPower() >= 2 && Servers.isThisATestServer())) { if (Methods.isActionAllowed(performer, (short)547)) done = Methods.castSpell(performer, spell, tilex, tiley, performer.getLayer(), heightOffset, counter);  } else { comm.sendNormalServerMessage("You need to use a magic staff."); done = true; }  }  return done;
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
/*      */   private void displayVillageInfo(Creature performer, Communicator comm, Village village, int tilex, int tiley, boolean inVillage) {
/* 3042 */     if (inVillage) {
/*      */       
/* 3044 */       message = "This is within the village of " + village.getName() + ".";
/*      */     }
/*      */     else {
/*      */       
/* 3048 */       message = "This is within the perimeter of " + village.getName() + ".";
/*      */     } 
/*      */ 
/*      */     
/* 3052 */     int tokenX = village.getTokenX();
/* 3053 */     int tokenY = village.getTokenY();
/* 3054 */     int distX = Math.abs(tokenX - tilex);
/* 3055 */     int distY = Math.abs(tokenY - tiley);
/* 3056 */     int maxDist = Math.max(distX, distY);
/* 3057 */     String message = message + String.format(" %s has it's token %d tiles away at %d, %d.", new Object[] { village.getName(), Integer.valueOf(maxDist), Integer.valueOf(tokenX), Integer.valueOf(tokenY) });
/* 3058 */     comm.sendNormalServerMessage(message);
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
/*      */   private void handle_GETINFO(Creature performer, int tilex, int tiley, int stid) {
/* 3072 */     if (Methods.isActionAllowed(performer, (short)384) && performer
/* 3073 */       .getCultist() != null && performer
/* 3074 */       .getCultist().mayInfoLocal()) {
/*      */       
/* 3076 */       performer.getVisionArea().getSurface().sendHostileCreatures();
/* 3077 */       performer.getVisionArea().getUnderGround().sendHostileCreatures();
/* 3078 */       performer.getCultist().touchCooldown2();
/*      */       
/*      */       return;
/*      */     } 
/* 3082 */     if (performer.getPower() < 2) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3088 */     if (stid != 176 && stid != 315) {
/*      */       return;
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
/* 3103 */     ErrorChecks.getInfo(performer, tilex, tiley, performer.getLayer());
/*      */     
/* 3105 */     Communicator comm = performer.getCommunicator();
/*      */ 
/*      */     
/* 3108 */     VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, true);
/* 3109 */     Village village = vtile.getVillage();
/* 3110 */     boolean inVillage = false;
/* 3111 */     if (village != null) {
/*      */       
/* 3113 */       inVillage = true;
/*      */     }
/*      */     else {
/*      */       
/* 3117 */       village = Villages.getVillageWithPerimeterAt(tilex, tiley, true);
/*      */     } 
/* 3119 */     if (village != null)
/*      */     {
/* 3121 */       displayVillageInfo(performer, comm, village, tilex, tiley, inVillage);
/*      */     }
/*      */ 
/*      */     
/* 3125 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 3126 */     if (t != null) {
/*      */       
/* 3128 */       Door[] doors = t.getDoors();
/* 3129 */       for (Door door : doors)
/*      */       {
/* 3131 */         comm.sendNormalServerMessage(" door: " + door
/* 3132 */             .getTileX() + ", " + door
/* 3133 */             .getTileY());
/*      */       }
/*      */       
/* 3136 */       Floor[] floors = t.getFloors();
/* 3137 */       for (Floor floor : floors)
/*      */       {
/* 3139 */         comm.sendNormalServerMessage(" floor: " + floor
/* 3140 */             .getTileX() + ", " + floor
/* 3141 */             .getTileY() + " " + floor
/* 3142 */             .getHeightOffset());
/*      */       }
/*      */       
/* 3145 */       Wall[] walls = t.getWalls();
/* 3146 */       for (Wall wall : walls) {
/*      */         
/* 3148 */         String msg = "";
/* 3149 */         if (performer.getPower() >= 3)
/*      */         {
/* 3151 */           msg = msg + "#number=" + wall.getNumber();
/*      */         }
/* 3153 */         comm.sendNormalServerMessage(msg + " wall at tile [" + wall
/*      */ 
/*      */             
/* 3156 */             .getTileX() + "," + wall.getTileY() + ", " + wall.getHeight() + "]:  start [" + wall
/* 3157 */             .getStartX() + ", " + wall.getStartY() + "] to end [" + wall
/* 3158 */             .getEndX() + ", " + wall.getEndY() + "] (t=" + wall
/* 3159 */             .getType() + ", state=" + wall
/* 3160 */             .getState() + ", indoor=" + wall
/* 3161 */             .isIndoor() + ", m=" + wall
/* 3162 */             .getMaterialString() + ")");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3169 */       FaithZone fz = Zones.getFaithZone(tilex, tiley, performer.isOnSurface());
/* 3170 */       if (fz != null && fz.getCurrentRuler() != null) {
/*      */         
/* 3172 */         int strength = Features.Feature.NEWDOMAINS.isEnabled() ? fz.getStrengthForTile(tilex, tiley, performer.isOnSurface()) : fz.getStrength();
/* 3173 */         String faithZoneMessage = String.format("Faith zone for %s with strength of %d.", new Object[] { fz.getCurrentRuler().getName(), Integer.valueOf(strength) });
/* 3174 */         comm.sendNormalServerMessage(faithZoneMessage);
/*      */       } 
/* 3176 */       HiveZone hz = Zones.getHiveZoneAt(tilex, tiley, performer.isOnSurface());
/* 3177 */       if (hz != null) {
/*      */         
/* 3179 */         Item hive = hz.getCurrentHive();
/* 3180 */         String hiveMessage = String.format("Hive named %s with strength %d found at %d, %d. Quality %.2f.", new Object[] { hive
/* 3181 */               .getName(), Integer.valueOf(hz.getStrengthForTile(tilex, tiley, performer.isOnSurface())), Integer.valueOf(hive.getTileX()), Integer.valueOf(hive.getTileY()), Float.valueOf(hive.getCurrentQualityLevel()) });
/* 3182 */         comm.sendNormalServerMessage(hiveMessage);
/*      */       } 
/* 3184 */       TurretZone tz = Zones.getTurretZone(tilex, tiley, performer.isOnSurface());
/* 3185 */       if (tz != null) {
/*      */         
/* 3187 */         Item turret = tz.getZoneItem();
/* 3188 */         comm.sendNormalServerMessage("Current turret: '" + turret.getName() + "' with strength: " + tz.getStrengthForTile(tilex, tiley, performer.isOnSurface()));
/*      */       } 
/*      */ 
/*      */       
/* 3192 */       if (Features.Feature.TOWER_CHAINING.isEnabled() && t != null) {
/*      */         
/* 3194 */         byte kingdom = t.getKingdom();
/* 3195 */         InfluenceChain chain = InfluenceChain.getInfluenceChain(kingdom);
/* 3196 */         Item closestTower = null;
/* 3197 */         int closest = Integer.MAX_VALUE;
/* 3198 */         for (Item item : chain.getChainMarkers()) {
/*      */           
/* 3200 */           if (item.isGuardTower()) {
/*      */             
/* 3202 */             int distx = Math.abs(item.getTileX() - performer.getTileX());
/* 3203 */             int disty = Math.abs(item.getTileY() - performer.getTileY());
/* 3204 */             int currDist = Math.max(distx, disty);
/*      */             
/* 3206 */             if (closestTower == null) {
/*      */               
/* 3208 */               closestTower = item;
/* 3209 */               closest = currDist;
/*      */               
/*      */               continue;
/*      */             } 
/* 3213 */             if (closest > currDist) {
/*      */               
/* 3215 */               closestTower = item;
/* 3216 */               closest = currDist;
/*      */             } 
/*      */           } 
/*      */         } 
/* 3220 */         if (closestTower != null)
/*      */         {
/* 3222 */           String towerMessage = String.format("Kingdom %s has their closest tower %d tiles away at %d, %d.", new Object[] {
/* 3223 */                 Kingdoms.getNameFor(kingdom), Integer.valueOf(closest), Integer.valueOf(closestTower.getTileX()), Integer.valueOf(closestTower.getTileY()) });
/* 3224 */           comm.sendNormalServerMessage(towerMessage);
/*      */         }
/*      */       
/*      */       } 
/* 3228 */     } catch (NoSuchZoneException noSuchZoneException) {} } private void handle_TESTCASE(Creature performer, Item source, int tilex, int tiley, int tile) { Item inventory; EncounterType et; float ql; int template;
/*      */     EpicTargetItems targs;
/*      */     int current, days, times, timesPerDay;
/*      */     Skill lockPicking;
/*      */     int nums, succ, fails;
/* 3233 */     if (!Servers.localServer.testServer && performer
/* 3234 */       .getPower() < 3) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 3239 */     performer.getStatus().refresh(0.99F, true);
/*      */     
/* 3241 */     Communicator comm = performer.getCommunicator();
/* 3242 */     if (source.getTemplateId() == 315) {
/*      */ 
/*      */       
/* 3245 */       if (source.getAuxData() == 1 && performer.isOnSurface()) {
/*      */         
/* 3247 */         int sx = Zones.safeTileX(performer.getTileX() - 10);
/* 3248 */         int ex = Zones.safeTileX(performer.getTileX() + 10);
/* 3249 */         int sy = Zones.safeTileY(performer.getTileY() - 10);
/* 3250 */         int ey = Zones.safeTileY(performer.getTileY() + 10);
/* 3251 */         for (int x = sx; x <= ex; x++) {
/* 3252 */           for (int y = sy; y <= ey; y++) {
/*      */             
/* 3254 */             int ttile = Zones.getTileIntForTile(x, y, 0);
/* 3255 */             if (Tiles.decodeType(ttile) == Tiles.Tile.TILE_TREE.id) {
/*      */               
/* 3257 */               int flowerType = Server.rand.nextInt(60000);
/*      */               
/* 3259 */               if (flowerType >= 1000) { flowerType = 0; }
/* 3260 */               else if (flowerType > 998) { flowerType = 7; }
/* 3261 */               else if (flowerType > 990) { flowerType = 6; }
/* 3262 */               else if (flowerType > 962) { flowerType = 5; }
/* 3263 */               else if (flowerType > 900) { flowerType = 4; }
/* 3264 */               else if (flowerType > 800) { flowerType = 3; }
/* 3265 */               else if (flowerType > 500) { flowerType = 2; }
/* 3266 */               else { flowerType = 1; }
/*      */               
/* 3268 */               Server.setSurfaceTile(x, y, 
/*      */                   
/* 3270 */                   Tiles.decodeHeight(ttile), Tiles.Tile.TILE_GRASS.id, (byte)flowerType);
/*      */ 
/*      */               
/* 3273 */               Players.getInstance().sendChangedTile(x, y, true, false);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         return;
/*      */       } 
/* 3279 */       if (source.getAuxData() == 2) {
/*      */         
/* 3281 */         float xmod = 0.0F;
/* 3282 */         float ymod = 0.0F;
/*      */         
/* 3284 */         float ymode = 2.0F;
/* 3285 */         float xmode = 2.0F;
/* 3286 */         float stx = 0.0F;
/* 3287 */         float sty = 0.0F;
/* 3288 */         float etx = 0.0F;
/* 3289 */         float ety = 0.0F;
/* 3290 */         float posStartX = 0.0F;
/* 3291 */         float posStartY = 0.0F;
/* 3292 */         float posEndX = 0.0F;
/* 3293 */         float posEndY = 0.0F;
/* 3294 */         boolean blocked = true;
/*      */ 
/*      */         
/* 3297 */         for (TilePos tPos : TilePos.areaIterator(1, 1, 9, 9)) {
/*      */           
/* 3299 */           xmod = 4.0F * tPos.x / 10.0F;
/* 3300 */           ymod = 4.0F * tPos.y / 10.0F;
/* 3301 */           posStartX = (performer.getTileX() * 4) + xmod;
/* 3302 */           posStartY = (performer.getTileY() * 4) + ymod;
/* 3303 */           stx = posStartX / 4.0F;
/* 3304 */           sty = posStartY / 4.0F;
/* 3305 */           posEndX = (tilex * 4) + xmode;
/* 3306 */           posEndY = (tiley * 4) + ymode;
/* 3307 */           etx = posEndX / 4.0F;
/* 3308 */           ety = posEndY / 4.0F;
/*      */           
/* 3310 */           BlockingResult b = Blocking.getBlockerBetween(performer, posStartX, posStartY, posEndX, posEndY, performer
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 3315 */               .getPositionZ(), 
/* 3316 */               Zones.getHeightForNode((int)etx, (int)ety, 
/* 3317 */                 isCave() ? -1 : 0), true, 
/*      */               
/* 3319 */               !isCave(), false, 6, -1L, performer
/*      */ 
/*      */ 
/*      */               
/* 3323 */               .getBridgeId(), performer
/* 3324 */               .getBridgeId(), false);
/*      */           
/* 3326 */           if (b == null) {
/*      */             
/* 3328 */             blocked = false;
/* 3329 */             comm.sendNormalServerMessage("A: No blocker between " + posStartX + "," + posStartY + " to " + posEndX + ", " + posEndY + " tiles :" + stx + "," + sty + " to " + etx + "," + ety);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3340 */           for (TilePos tPos2 : TilePos.areaIterator(1, 1, 9, 9)) {
/*      */             
/* 3342 */             xmode = 4.0F * tPos2.x / 10.0F;
/* 3343 */             ymode = 4.0F * tPos2.y / 10.0F;
/* 3344 */             posStartX = (performer.getTileX() * 4) + xmod;
/* 3345 */             posStartY = (performer.getTileY() * 4) + ymod;
/* 3346 */             stx = posStartX / 4.0F;
/* 3347 */             sty = posStartY / 4.0F;
/* 3348 */             posEndX = (tilex * 4) + xmode;
/* 3349 */             posEndY = (tiley * 4) + ymode;
/* 3350 */             etx = posEndX / 4.0F;
/* 3351 */             ety = posEndY / 4.0F;
/*      */             
/* 3353 */             BlockingResult b2 = Blocking.getBlockerBetween(performer, posStartX, posStartY, posEndX, posEndY, performer
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3358 */                 .getPositionZ(), 
/* 3359 */                 Zones.getHeightForNode((int)etx, (int)ety, 
/*      */                   
/* 3361 */                   isCave() ? -1 : 0), true, 
/*      */ 
/*      */ 
/*      */                 
/* 3365 */                 !isCave(), false, 6, -1L, performer
/*      */ 
/*      */ 
/*      */                 
/* 3369 */                 .getBridgeId(), performer
/* 3370 */                 .getBridgeId(), false);
/*      */             
/* 3372 */             if (b2 == null) {
/*      */               
/* 3374 */               blocked = false;
/* 3375 */               comm.sendNormalServerMessage("B: No blocker between " + posStartX + "," + posStartY + " to " + posEndX + ", " + posEndY + " tiles :" + stx + "," + sty + " to " + etx + "," + ety);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3386 */         if (blocked)
/*      */         {
/* 3388 */           comm.sendNormalServerMessage("A: " + stx + "," + sty + " to " + etx + "," + ety + " Blocked!");
/*      */         }
/*      */ 
/*      */         
/* 3392 */         if (blocked)
/*      */         {
/* 3394 */           comm.sendNormalServerMessage("B: " + stx + "," + sty + " to " + etx + "," + ety + " Blocked!");
/*      */         }
/*      */         
/*      */         return;
/*      */       } 
/* 3399 */       if (source.getAuxData() == 22) {
/*      */         
/* 3401 */         if (performer.getPower() < 3) {
/*      */           return;
/*      */         }
/* 3404 */         ((Player)performer).reimbursePacks(true);
/*      */         return;
/*      */       } 
/* 3407 */       if (source.getAuxData() == 23) {
/*      */         
/* 3409 */         if (performer.getPower() < 3) {
/*      */           return;
/*      */         }
/* 3412 */         ((Player)performer).reimbAnniversaryGift(true);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/* 3418 */     if (source.getTemplateId() != 176) {
/*      */       
/* 3420 */       TestQuestion tq = new TestQuestion(performer, tile);
/* 3421 */       tq.sendQuestion();
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 3429 */     switch (source.getAuxData()) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 3:
/* 3434 */         inventory = performer.getInventory();
/*      */         
/* 3436 */         ql = 50.0F + Server.rand.nextFloat() * 40.0F;
/*      */         
/*      */         try {
/* 3439 */           Item c = Creature.createItem(274, ql);
/* 3440 */           inventory.insertItem(c);
/* 3441 */           c = Creature.createItem(274, ql);
/* 3442 */           inventory.insertItem(c);
/* 3443 */           c = Creature.createItem(279, ql);
/* 3444 */           inventory.insertItem(c);
/* 3445 */           c = Creature.createItem(277, ql);
/* 3446 */           inventory.insertItem(c);
/* 3447 */           c = Creature.createItem(277, ql);
/* 3448 */           inventory.insertItem(c);
/*      */           
/* 3450 */           c = Creature.createItem(278, ql);
/* 3451 */           inventory.insertItem(c);
/* 3452 */           c = Creature.createItem(278, ql);
/* 3453 */           inventory.insertItem(c);
/* 3454 */           c = Creature.createItem(275, ql);
/* 3455 */           inventory.insertItem(c);
/* 3456 */           c = Creature.createItem(276, ql);
/* 3457 */           inventory.insertItem(c);
/* 3458 */           performer.wearItems();
/*      */         }
/* 3460 */         catch (Exception ex) {
/*      */           
/* 3462 */           comm.sendNormalServerMessage("Failed to create:" + ex.getMessage());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/* 3470 */         inventory = performer.getInventory();
/*      */         
/*      */         try {
/* 3473 */           Item c = Creature.createItem(87, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3474 */           inventory.insertItem(c, true);
/* 3475 */           c = Creature.createItem(21, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3476 */           inventory.insertItem(c, true);
/* 3477 */           c = Creature.createItem(80, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3478 */           inventory.insertItem(c, true);
/* 3479 */           c = Creature.createItem(290, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3480 */           inventory.insertItem(c, true);
/* 3481 */           c = Creature.createItem(291, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3482 */           inventory.insertItem(c, true);
/* 3483 */           c = Creature.createItem(706, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3484 */           inventory.insertItem(c, true);
/* 3485 */           c = Creature.createItem(707, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3486 */           inventory.insertItem(c, true);
/* 3487 */           c = Creature.createItem(705, 50.0F + Server.rand.nextFloat() * 40.0F);
/* 3488 */           inventory.insertItem(c, true);
/*      */         }
/* 3490 */         catch (Exception ex) {
/*      */           
/* 3492 */           comm.sendNormalServerMessage("Failed to create:" + ex.getMessage());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/* 3500 */         inventory = performer.getInventory();
/*      */         
/*      */         try {
/* 3503 */           ql = 50.0F + Server.rand.nextFloat() * 40.0F;
/* 3504 */           Item c = Creature.createItem(105, ql);
/* 3505 */           inventory.insertItem(c);
/* 3506 */           c = Creature.createItem(105, ql);
/* 3507 */           inventory.insertItem(c);
/* 3508 */           c = Creature.createItem(107, ql);
/* 3509 */           inventory.insertItem(c);
/* 3510 */           c = Creature.createItem(106, ql);
/* 3511 */           inventory.insertItem(c);
/* 3512 */           c = Creature.createItem(106, ql);
/* 3513 */           inventory.insertItem(c);
/*      */           
/* 3515 */           c = Creature.createItem(103, ql);
/* 3516 */           inventory.insertItem(c);
/* 3517 */           c = Creature.createItem(103, ql);
/* 3518 */           inventory.insertItem(c);
/* 3519 */           c = Creature.createItem(108, ql);
/* 3520 */           inventory.insertItem(c);
/* 3521 */           c = Creature.createItem(104, ql);
/* 3522 */           inventory.insertItem(c);
/* 3523 */           performer.wearItems();
/*      */         }
/* 3525 */         catch (Exception ex) {
/*      */           
/* 3527 */           comm.sendNormalServerMessage("Failed to create:" + ex.getMessage());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/* 3535 */         inventory = performer.getInventory();
/*      */         
/*      */         try {
/* 3538 */           ql = 50.0F + Server.rand.nextFloat() * 40.0F;
/* 3539 */           Item c = Creature.createItem(116, ql);
/* 3540 */           inventory.insertItem(c);
/* 3541 */           c = Creature.createItem(116, ql);
/* 3542 */           inventory.insertItem(c);
/* 3543 */           c = Creature.createItem(117, ql);
/* 3544 */           inventory.insertItem(c);
/* 3545 */           c = Creature.createItem(115, ql);
/* 3546 */           inventory.insertItem(c);
/* 3547 */           c = Creature.createItem(115, ql);
/* 3548 */           inventory.insertItem(c);
/*      */           
/* 3550 */           c = Creature.createItem(119, ql);
/* 3551 */           inventory.insertItem(c);
/* 3552 */           c = Creature.createItem(119, ql);
/* 3553 */           inventory.insertItem(c);
/* 3554 */           c = Creature.createItem(118, ql);
/* 3555 */           inventory.insertItem(c);
/* 3556 */           c = Creature.createItem(120, ql);
/* 3557 */           inventory.insertItem(c);
/* 3558 */           performer.wearItems();
/*      */         }
/* 3560 */         catch (Exception ex) {
/*      */           
/* 3562 */           comm
/* 3563 */             .sendNormalServerMessage("Failed to create:" + ex.getMessage());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 7:
/* 3571 */         inventory = performer.getInventory();
/*      */         
/*      */         try {
/* 3574 */           ql = 50.0F + Server.rand.nextFloat() * 40.0F;
/* 3575 */           Item c = Creature.createItem(474, ql);
/* 3576 */           inventory.insertItem(c);
/* 3577 */           c = Creature.createItem(474, ql);
/* 3578 */           inventory.insertItem(c);
/* 3579 */           c = Creature.createItem(476, ql);
/* 3580 */           inventory.insertItem(c);
/* 3581 */           c = Creature.createItem(477, ql);
/* 3582 */           inventory.insertItem(c);
/* 3583 */           c = Creature.createItem(477, ql);
/* 3584 */           inventory.insertItem(c);
/* 3585 */           c = Creature.createItem(478, ql);
/* 3586 */           inventory.insertItem(c);
/* 3587 */           c = Creature.createItem(478, ql);
/* 3588 */           inventory.insertItem(c);
/* 3589 */           c = Creature.createItem(475, ql);
/* 3590 */           inventory.insertItem(c);
/* 3591 */           c = Creature.createItem(285, ql);
/* 3592 */           inventory.insertItem(c);
/* 3593 */           performer.wearItems();
/*      */         }
/* 3595 */         catch (Exception ex) {
/*      */           
/* 3597 */           comm
/* 3598 */             .sendNormalServerMessage("Failed to create:" + ex.getMessage());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 8:
/* 3606 */         inventory = performer.getInventory();
/*      */         
/*      */         try {
/* 3609 */           ql = 50.0F + Server.rand.nextFloat() * 40.0F;
/* 3610 */           Item c = Creature.createItem(280, ql);
/* 3611 */           inventory.insertItem(c);
/* 3612 */           c = Creature.createItem(280, ql);
/* 3613 */           inventory.insertItem(c);
/* 3614 */           c = Creature.createItem(282, ql);
/* 3615 */           inventory.insertItem(c);
/* 3616 */           c = Creature.createItem(283, ql);
/* 3617 */           inventory.insertItem(c);
/* 3618 */           c = Creature.createItem(283, ql);
/* 3619 */           inventory.insertItem(c);
/* 3620 */           c = Creature.createItem(284, ql);
/* 3621 */           inventory.insertItem(c);
/* 3622 */           c = Creature.createItem(284, ql);
/* 3623 */           inventory.insertItem(c);
/* 3624 */           c = Creature.createItem(281, ql);
/* 3625 */           inventory.insertItem(c);
/* 3626 */           c = Creature.createItem(286, ql);
/* 3627 */           inventory.insertItem(c);
/* 3628 */           performer.wearItems();
/*      */         }
/* 3630 */         catch (Exception ex) {
/*      */           
/* 3632 */           comm
/* 3633 */             .sendNormalServerMessage("Failed to create:" + ex.getMessage());
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 9:
/* 3640 */         logger.log(Level.INFO, "Testing epic mission progress");
/* 3641 */         if (performer.getDeity() != null) {
/*      */ 
/*      */           
/* 3644 */           EpicMission em = EpicServerStatus.getEpicMissionForEntity(
/* 3645 */               Deities.translateEntityForDeity((performer.getDeity()).number));
/* 3646 */           if (em != null) {
/*      */             
/* 3648 */             logger.log(Level.INFO, "Got " + em
/* 3649 */                 .getScenarioName() + ". Mission id=" + em.getMissionId());
/*      */ 
/*      */             
/* 3652 */             TriggerEffect[] trigs = TriggerEffects.getEffectsForMission(em.getMissionId());
/*      */             
/* 3654 */             logger.log(Level.INFO, "Found " + trigs.length + " triggers");
/* 3655 */             MissionPerformer mp = new MissionPerformer(performer.getWurmId());
/* 3656 */             MissionPerformed perf = new MissionPerformed(em.getMissionId(), mp);
/* 3657 */             for (TriggerEffect t : trigs) {
/*      */               
/* 3659 */               logger.log(Level.INFO, "Effect " + t.getName());
/* 3660 */               t.effect(performer, perf, performer.getWurmId(), false, true);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 10:
/* 3668 */         logger.log(Level.INFO, performer.getName() + " testing achievement " + source.getData1());
/* 3669 */         performer.achievement(source.getData1(), source.getData2());
/*      */         break;
/*      */ 
/*      */       
/*      */       case 11:
/* 3674 */         logger.log(Level.INFO, performer
/* 3675 */             .getName() + " testing encounters " + (byte)source.getData1() + ", " + 
/* 3676 */             (byte)source.getData2());
/*      */         
/* 3678 */         et = SpawnTable.getType((byte)source.getData1(), (byte)source.getData2());
/* 3679 */         if (et != null)
/*      */         {
/* 3681 */           et.getRandomEncounter(performer);
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 12:
/* 3687 */         performer.modifyKarma(4);
/* 3688 */         performer.setKarma(performer.getKarma() + 110);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 14:
/* 3693 */         Zones.createBattleCamp(tilex, tiley);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 15:
/* 3698 */         template = source.getData1();
/*      */ 
/*      */         
/*      */         try {
/* 3702 */           ItemTemplate it = ItemTemplateFactory.getInstance().getTemplate(template);
/* 3703 */           comm.sendNormalServerMessage(it.getName() + "...");
/*      */         }
/* 3705 */         catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3710 */         targs = EpicTargetItems.getEpicTargets(performer.getKingdomTemplateId());
/* 3711 */         comm.sendNormalServerMessage(targs
/* 3712 */             .getGlobalMapPlacementRequirementString(template) + " (region " + targs
/* 3713 */             .getGlobalMapPlacementRequirement(template) + ")");
/* 3714 */         current = targs.getCurrentCounter(template);
/* 3715 */         comm
/* 3716 */           .sendNormalServerMessage("Current counter =" + current);
/* 3717 */         if (source.getData2() == 10) {
/*      */           
/* 3719 */           comm.sendNormalServerMessage("Setting current to " + current);
/* 3720 */           targs.testSetCounter(current, Server.rand.nextLong());
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 21:
/* 3738 */         logger.log(Level.INFO, performer.getName() + " deity=" + performer.getDeity());
/* 3739 */         if (performer.getDeity() != null) {
/*      */ 
/*      */           
/* 3742 */           WcEpicStatusReport wce = new WcEpicStatusReport(WurmId.getNextWCCommandId(), true, (performer.getDeity()).number, (byte)101, 4);
/* 3743 */           wce.sendToLoginServer();
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 22:
/* 3749 */         if (Servers.localServer.testServer) {
/*      */           
/* 3751 */           int deityNumber = 1;
/* 3752 */           String entityName = "Fo";
/* 3753 */           if (performer.getDeity() != null) {
/*      */             
/* 3755 */             deityNumber = (performer.getDeity()).number;
/* 3756 */             entityName = performer.getDeity().getName();
/*      */           } 
/* 3758 */           EpicServerStatus es = new EpicServerStatus();
/* 3759 */           es.generateNewMissionForEpicEntity(deityNumber, entityName, 1, 600, entityName + "'s funny stuff", Server.rand
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 3764 */               .nextInt(), "You must really do this for " + entityName + " because yeah you know.", true);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 23:
/* 3774 */         if (!Servers.localServer.testServer) {
/*      */           break;
/*      */         }
/*      */         
/* 3778 */         days = source.getData1();
/* 3779 */         times = source.getData2();
/* 3780 */         timesPerDay = 144;
/* 3781 */         lockPicking = null;
/*      */         
/*      */         try {
/* 3784 */           lockPicking = performer.getSkills().getSkill(10076);
/*      */         }
/* 3786 */         catch (Exception ex) {
/*      */           
/* 3788 */           lockPicking = performer.getSkills().learn(10076, 1.0F);
/*      */         } 
/* 3790 */         lockPicking.minimum = lockPicking.getKnowledge();
/* 3791 */         nums = days * 144;
/* 3792 */         succ = 0;
/* 3793 */         fails = 0;
/* 3794 */         comm.sendNormalServerMessage("Going to run for " + days + " days and " + '' + "=" + nums + " times at factor " + times + ".");
/*      */ 
/*      */ 
/*      */         
/* 3798 */         while (succ < nums) {
/*      */           
/* 3800 */           lockPicking.lastUsed = 0L;
/* 3801 */           if (lockPicking.skillCheck(lockPicking.getKnowledge(0.0D), 10.0D, false, times) > 0.0D) {
/* 3802 */             succ++; continue;
/*      */           } 
/* 3804 */           fails++;
/*      */         } 
/* 3806 */         comm.sendNormalServerMessage("Ok. " + succ + " successes after " + days + " days of practice. " + fails + " fails. Skill now at " + lockPicking
/*      */ 
/*      */             
/* 3809 */             .getKnowledge() + ". This is an epic server:" + Servers.localServer.EPIC);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 25:
/* 3818 */         if (Servers.localServer.testServer)
/*      */         {
/* 3820 */           ((Valrei)EpicServerStatus.getValrei()).testValreiFight(performer);
/*      */         }
/*      */         break;
/*      */       case 26:
/* 3824 */         if (Servers.localServer.testServer)
/*      */         {
/* 3826 */           ((Valrei)EpicServerStatus.getValrei()).testSingleValreiFight(performer, source);
/*      */         }
/*      */         break;
/*      */     }  }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean destroyAllFloorsAt(Action act, Creature performer, float counter) {
/* 3844 */     Item tool = null;
/*      */     
/*      */     try {
/* 3847 */       tool = Items.getItem(act.getSubjectId());
/*      */     }
/* 3849 */     catch (NoSuchItemException nsie) {
/*      */       
/* 3851 */       logger.log(Level.WARNING, nsie.getMessage(), (Throwable)nsie);
/*      */     } 
/*      */     
/* 3854 */     VolaTile vtile = Zones.getOrCreateTile(act.getTileX(), act.getTileY(), (performer.getLayer() >= 0));
/* 3855 */     Floor[] floors = vtile.getFloors();
/* 3856 */     for (Floor floor : floors)
/*      */     {
/* 3858 */       FloorBehaviour.actionDestroyFloor(act, performer, tool, floor, act.getNumber(), counter);
/*      */     }
/* 3860 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean herbalize(Action act, Creature performer, int tilex, int tiley, int tile, byte data, float counter) {
/* 3866 */     return botanizeV11(act, performer, tilex, tiley, tile, data, counter);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean forage(Action act, Creature performer, int tilex, int tiley, int tile, byte data, float counter) {
/* 3872 */     boolean toReturn = false;
/* 3873 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_ROCK.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_CLIFF.id) {
/*      */       
/* 3875 */       if (Tiles.decodeHeight(tile) < -1) {
/*      */         
/* 3877 */         performer.getCommunicator().sendNormalServerMessage("You can't rummage around down there.");
/* 3878 */         return true;
/*      */       } 
/* 3880 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 3882 */         performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put whatever you find.");
/*      */         
/* 3884 */         return true;
/*      */       } 
/* 3886 */       int time = 200;
/* 3887 */       boolean containsForage = Server.isForagable(tilex, tiley);
/*      */       
/* 3889 */       if (counter == 1.0F) {
/*      */         
/* 3891 */         Skill forage = performer.getSkills().getSkillOrLearn(10071);
/*      */         
/* 3893 */         if (!containsForage) {
/*      */           
/* 3895 */           if (performer.getPlayingTime() < 86400000L) {
/* 3896 */             performer.getCommunicator().sendNormalServerMessage("This area looks picked clean. New resources may appear over time so check back later.");
/*      */           } else {
/*      */             
/* 3899 */             performer.getCommunicator().sendNormalServerMessage("This area looks picked clean.");
/* 3900 */           }  return true;
/*      */         } 
/* 3902 */         time = (int)Math.max(100.0D, (100.0D - forage.getKnowledge(0.0D)) * 2.0D);
/* 3903 */         performer.getCommunicator().sendNormalServerMessage("You start to rummage for something useful.");
/* 3904 */         Server.getInstance().broadCastAction(performer.getName() + " starts to rummage for something useful.", performer, 5);
/*      */         
/* 3906 */         performer.sendActionControl("rummaging", true, time);
/* 3907 */         performer.getStatus().modifyStamina(-500.0F);
/* 3908 */         act.setTimeLeft(time);
/*      */       }
/*      */       else {
/*      */         
/* 3912 */         time = act.getTimeLeft();
/*      */       } 
/*      */       
/* 3915 */       if (counter * 10.0F >= time) {
/*      */         
/* 3917 */         if (act.getRarity() != 0)
/*      */         {
/* 3919 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 3921 */         toReturn = true;
/* 3922 */         Skill forage = performer.getSkills().getSkillOrLearn(10071);
/* 3923 */         int herbCreated = 0;
/* 3924 */         int knowledge = (int)forage.getKnowledge(0.0D);
/*      */         
/* 3926 */         if (knowledge > 50) {
/* 3927 */           forage.skillCheck((knowledge - 20), 0.0D, false, counter);
/*      */         } else {
/* 3929 */           forage.skillCheck((knowledge + 20), 0.0D, false, counter);
/* 3930 */         }  if (containsForage) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3937 */           int num = Server.rand.nextInt(100);
/* 3938 */           if (num < 20) {
/* 3939 */             herbCreated = 146;
/* 3940 */           } else if (num < 95) {
/* 3941 */             herbCreated = 684;
/* 3942 */           } else if (num < 100) {
/* 3943 */             herbCreated = 446;
/*      */           } 
/* 3945 */           if (herbCreated > 0) {
/*      */             
/* 3947 */             TilePoller.setGrassHasSeeds(tilex, tiley, false, false);
/* 3948 */             double power = forage.skillCheck(knowledge, 0.0D, false, counter);
/*      */             
/*      */             try {
/* 3951 */               Item newItem = ItemFactory.createItem(herbCreated, 
/* 3952 */                   Math.max(Math.abs((float)power * 0.1F), 1.0F), (byte)0, act
/* 3953 */                   .getRarity(), null);
/* 3954 */               Item inventory = performer.getInventory();
/* 3955 */               inventory.insertItem(newItem);
/* 3956 */               performer.getCommunicator().sendNormalServerMessage("You find " + newItem.getName() + "!");
/* 3957 */               Server.getInstance().broadCastAction(performer
/* 3958 */                   .getName() + " puts something in " + performer.getHisHerItsString() + " pocket.", performer, 5);
/*      */               
/* 3960 */               if (performer.getTutorialLevel() == 6 && !performer.skippedTutorial())
/*      */               {
/* 3962 */                 if (performer.getKingdomId() != 3) {
/* 3963 */                   performer.missionFinished(true, true);
/*      */                 }
/*      */               }
/*      */             }
/* 3967 */             catch (FailedException fe) {
/*      */               
/* 3969 */               logger.log(Level.WARNING, performer.getName() + " " + fe.getMessage(), (Throwable)fe);
/*      */             }
/* 3971 */             catch (NoSuchTemplateException nst) {
/*      */               
/* 3973 */               logger.log(Level.WARNING, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/*      */             }
/*      */           
/*      */           } 
/*      */         } else {
/*      */           
/* 3979 */           performer.getCommunicator().sendNormalServerMessage("You find nothing useful.");
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 3985 */       toReturn = forageV11(act, performer, tilex, tiley, tile, data, counter);
/*      */     } 
/* 3987 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCloseTile(int cx, int cy, int tilex, int tiley) {
/* 3992 */     if (Math.abs(cx - tilex) > 1 || Math.abs(cy - tiley) > 1)
/* 3993 */       return false; 
/* 3994 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAdjacentTile(int cx, int cy, int tilex, int tiley) {
/* 3999 */     if (cx == tilex && cy == tiley)
/* 4000 */       return false; 
/* 4001 */     if (Math.abs(cx - tilex) > 1 || Math.abs(cy - tiley) > 1)
/* 4002 */       return false; 
/* 4003 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAdjacent(int tilex, int tiley, int tilexx, int tileyy) {
/* 4008 */     if (tilex - tilexx != 0 && tiley - tileyy != 0)
/* 4009 */       return false; 
/* 4010 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSpike(Creature performer, int x, int y, boolean fix) {
/* 4015 */     int min = 1;
/* 4016 */     int ms = Constants.meshSize;
/* 4017 */     int max = (1 << ms) - 1;
/* 4018 */     if (x > 1 && x < max && y > 1 && y < max) {
/*      */       
/* 4020 */       int tile = Server.surfaceMesh.getTile(x, y);
/* 4021 */       short height = Tiles.decodeHeight(tile);
/* 4022 */       int prevTile = Server.surfaceMesh.getTile(x - 1, y);
/* 4023 */       short prevHeight = Tiles.decodeHeight(prevTile);
/* 4024 */       short nextHeight = Tiles.decodeHeight(Server.surfaceMesh.getTile(x + 1, y));
/*      */ 
/*      */       
/* 4027 */       if (Math.abs(prevHeight - height) > 500 || Math.abs(nextHeight - height) > 500) {
/*      */         
/* 4029 */         if (fix) {
/*      */           
/* 4031 */           byte prevType = Tiles.decodeType(prevTile);
/* 4032 */           byte prevData = Tiles.decodeData(prevTile);
/* 4033 */           logger.log(Level.INFO, performer.getName() + " levelling layer at " + x + "," + y + ", height=" + height + " prevHeight=" + prevHeight);
/*      */           
/* 4035 */           Server.setSurfaceTile(x, y, prevHeight, prevType, prevData);
/* 4036 */           short prevRockHeight = Tiles.decodeHeight(Server.rockMesh.getTile(x - 1, y));
/* 4037 */           Server.rockMesh.setTile(x, y, Tiles.encode(prevRockHeight, (short)0));
/* 4038 */           performer.getMovementScheme().touchFreeMoveCounter();
/* 4039 */           Players.getInstance().sendChangedTile(x, y, performer.isOnSurface(), true);
/*      */ 
/*      */           
/*      */           try {
/* 4043 */             Zone toCheckForChange = Zones.getZone(x, y, performer.isOnSurface());
/* 4044 */             toCheckForChange.changeTile(x, y);
/*      */           }
/* 4046 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 4048 */             logger.log(Level.INFO, "no such zone?: " + x + ", " + y, (Throwable)nsz);
/*      */           } 
/*      */         } 
/* 4051 */         return true;
/*      */       } 
/*      */     } 
/* 4054 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getStringForTileEvent(TileEvent t) {
/* 4059 */     String performer = "Unknown";
/* 4060 */     PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(t.getPerformer());
/* 4061 */     if (p != null)
/* 4062 */       performer = p.getName(); 
/* 4063 */     String action = "Unknown";
/*      */     
/*      */     try {
/* 4066 */       action = Actions.getVerbForAction((short)t.getAction());
/* 4067 */       if (Actions.actionEntrys[t.getAction()].isSpell())
/*      */       {
/* 4069 */         action = action + " ";
/* 4070 */         action = action + Actions.actionEntrys[t.getAction()].getActionString();
/* 4071 */         action = action + " (successful cast)";
/*      */       }
/*      */     
/* 4074 */     } catch (Exception ex) {
/*      */       
/* 4076 */       logger.warning("TileEvent malformed, event larger than ActionEntrys list. N=" + ((t != null) ? (String)Integer.valueOf(t.getAction()) : null));
/*      */     } 
/*      */     
/* 4079 */     String date = (new SimpleDateFormat("yyyy.MMM.dd.HH.mm.ss")).format(new Timestamp(t.getDate()));
/* 4080 */     return date + ' ' + performer + ' ' + action + " (" + t
/* 4081 */       .getTileX() + ", " + t.getTileY() + "," + t.getLayer() + ")";
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
/*      */   public static String getTileDescription(int encodedTile) {
/* 4093 */     byte tileType = Tiles.decodeType(encodedTile);
/* 4094 */     switch (tileType) {
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 5:
/*      */       case 32:
/*      */       case 33:
/* 4101 */         return "You see an eerie part of the lands of Wurm.";
/*      */       
/*      */       case 3:
/*      */       case 31:
/* 4105 */         return "You see an eerie part of the lands of Wurm.";
/*      */       
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/* 4112 */         return "You see an eerie mine door";
/*      */       
/*      */       case 4:
/* 4115 */         return "You see an eerie rock. It is part of the lands of Wurm.";
/*      */       case 0:
/* 4117 */         return "You see a hole.";
/*      */       case 1:
/* 4119 */         return "You see a lot of sand.";
/*      */       case 15:
/* 4121 */         return "You see some planks laid down by someone.";
/*      */       case 39:
/* 4123 */         return "You see some planks laid down by someone. They are protected from the environment by a thin layer of tar.";
/*      */       
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 30:
/*      */       case 34:
/*      */       case 43:
/* 4146 */         return "You see a part of the lands of Wurm.";
/*      */     } 
/* 4148 */     return "You see a part of the lands of Wurm.";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static String getShardQlDescription(int shardQl) {
/* 4154 */     if (shardQl < 10)
/* 4155 */       return "really poor quality"; 
/* 4156 */     if (shardQl < 30)
/* 4157 */       return "poor quality"; 
/* 4158 */     if (shardQl < 40)
/* 4159 */       return "acceptable quality"; 
/* 4160 */     if (shardQl < 60)
/* 4161 */       return "normal quality"; 
/* 4162 */     if (shardQl < 80)
/* 4163 */       return "good quality"; 
/* 4164 */     if (shardQl < 95)
/* 4165 */       return "very good quality"; 
/* 4166 */     return "utmost quality";
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
/*      */   static boolean canCollectSnow(Creature performer, int tilex, int tiley, byte type, byte data) {
/* 4180 */     if (!performer.isOnSurface()) {
/* 4181 */       return false;
/*      */     }
/* 4183 */     if (!WurmCalendar.isSeasonWinter()) {
/* 4184 */       return false;
/*      */     }
/* 4186 */     VolaTile vt = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 4187 */     if (vt != null && vt.getStructure() != null) {
/* 4188 */       return false;
/*      */     }
/* 4190 */     if (!Server.isGatherable(tilex, tiley)) {
/* 4191 */       return false;
/*      */     }
/*      */     
/* 4194 */     Tiles.Tile theTile = Tiles.getTile(type);
/* 4195 */     if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_TUNDRA.id || type == Tiles.Tile.TILE_LAWN.id) {
/* 4196 */       return true;
/*      */     }
/* 4198 */     if (type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id) {
/* 4199 */       return true;
/*      */     }
/* 4201 */     if (type == Tiles.Tile.TILE_SLATE_SLABS.id || type == Tiles.Tile.TILE_MARBLE_SLABS.id || type == Tiles.Tile.TILE_STONE_SLABS.id || type == Tiles.Tile.TILE_SANDSTONE_SLABS.id)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 4206 */       return true;
/*      */     }
/* 4208 */     if (theTile.isNormalTree() || theTile.isNormalBush() || theTile
/* 4209 */       .isMyceliumTree() || theTile.isMyceliumBush())
/*      */     {
/* 4211 */       return true;
/*      */     }
/* 4213 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean collectSnow(Action act, Creature performer, int tilex, int tiley, int tile, byte data, float counter) {
/* 4219 */     boolean toReturn = false;
/* 4220 */     byte tileType = Tiles.decodeType(tile);
/* 4221 */     if (counter > 1.0F || canCollectSnow(performer, tilex, tiley, tileType, data)) {
/*      */       
/* 4223 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 4225 */         performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put whatever you find.");
/*      */         
/* 4227 */         return true;
/*      */       } 
/* 4229 */       int time = 20;
/*      */       
/* 4231 */       if (counter == 1.0F) {
/*      */         
/* 4233 */         if (!Server.isGatherable(tilex, tiley)) {
/*      */           
/* 4235 */           if (performer.getPlayingTime() < 86400000L) {
/* 4236 */             performer.getCommunicator().sendNormalServerMessage("This is not enough snow here to make a snowball, try somewhere else. Snow will gather here over time if it snows enough, so check back later.");
/*      */           }
/*      */           else {
/*      */             
/* 4240 */             performer.getCommunicator().sendNormalServerMessage("This is not enough snow here to make a snowball, try somewhere else.");
/* 4241 */           }  return true;
/*      */         } 
/* 4243 */         int maxSearches = getFBGrassLength(tile).getCode() + 1;
/* 4244 */         act.setNextTick(time);
/* 4245 */         act.setTickCount(1);
/* 4246 */         float totalTime = (time * maxSearches);
/*      */ 
/*      */         
/*      */         try {
/* 4250 */           performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */         }
/* 4252 */         catch (NoSuchActionException nsa) {
/*      */           
/* 4254 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/* 4256 */         performer.getCommunicator().sendNormalServerMessage("You start to collect snow in the area.");
/* 4257 */         Server.getInstance().broadCastAction(performer.getName() + " starts to collect snow in the area.", performer, 5);
/* 4258 */         performer.sendActionControl(Actions.actionEntrys[act.getNumber()].getVerbString(), true, (int)totalTime);
/* 4259 */         performer.getStatus().modifyStamina(-100.0F);
/*      */       } else {
/*      */         
/* 4262 */         time = act.getTimeLeft();
/*      */       } 
/* 4264 */       if (act.mayPlaySound()) {
/* 4265 */         Methods.sendSound(performer, "sound.work.foragebotanize");
/*      */       }
/* 4267 */       if (counter * 10.0F >= act.getNextTick()) {
/*      */         
/* 4269 */         if (act.getRarity() != 0) {
/* 4270 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 4272 */         int searchCount = act.getTickCount();
/* 4273 */         int maxSearches = getFBGrassLength(tile).getCode() + 1;
/* 4274 */         act.incTickCount();
/* 4275 */         act.incNextTick(20.0F);
/* 4276 */         performer.getStatus().modifyStamina((-200 * searchCount));
/*      */ 
/*      */         
/* 4279 */         if (searchCount >= maxSearches) {
/* 4280 */           toReturn = true;
/*      */         }
/*      */ 
/*      */         
/* 4284 */         if (searchCount == 1) {
/* 4285 */           Server.setGatherable(tilex, tiley, false);
/*      */         }
/*      */         
/*      */         try {
/* 4289 */           float power = Math.min(((maxSearches - searchCount + 1) + Server.rand.nextFloat()) * Math.max(5 - maxSearches, 1) * 20.0F, 99.0F);
/* 4290 */           Item newItem = ItemFactory.createItem(1276, Math.max(Math.abs(power), 1.0F), (byte)0, act
/* 4291 */               .getRarity(), null);
/* 4292 */           Item inventory = performer.getInventory();
/* 4293 */           inventory.insertItem(newItem);
/* 4294 */           performer.getCommunicator().sendNormalServerMessage("You collect enough snow to make a " + newItem.getName() + "!");
/*      */           
/* 4296 */           Server.getInstance().broadCastAction(performer
/* 4297 */               .getName() + " puts something in " + performer.getHisHerItsString() + " pocket.", performer, 5);
/*      */           
/* 4299 */           if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem())
/*      */           {
/* 4301 */             performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */             
/* 4303 */             toReturn = true;
/*      */           }
/*      */         
/* 4306 */         } catch (FailedException fe) {
/*      */           
/* 4308 */           logger.log(Level.WARNING, performer.getName() + " " + fe.getMessage(), (Throwable)fe);
/*      */         }
/* 4310 */         catch (NoSuchTemplateException nst) {
/*      */           
/* 4312 */           logger.log(Level.WARNING, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/*      */         } 
/*      */         
/* 4315 */         if (searchCount < maxSearches)
/*      */         {
/* 4317 */           act.setRarity(performer.getRarity());
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 4322 */       toReturn = true;
/*      */     } 
/* 4324 */     return toReturn;
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
/*      */   private boolean botanizeV11(Action act, Creature performer, int tilex, int tiley, int tile, byte data, float counter) {
/* 4341 */     boolean toReturn = false;
/* 4342 */     byte tileType = Tiles.decodeType(tile);
/* 4343 */     if (canBotanize(performer, tileType, data)) {
/*      */       
/* 4345 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 4347 */         performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put whatever you find.");
/*      */         
/* 4349 */         return true;
/*      */       } 
/* 4351 */       int time = 200;
/*      */       
/* 4353 */       boolean containsForage = Server.isForagable(tilex, tiley);
/* 4354 */       boolean containsHerb = Server.isBotanizable(tilex, tiley);
/* 4355 */       Skill botanize = performer.getSkills().getSkillOrLearn(10072);
/*      */       
/* 4357 */       if (act.getNumber() != 224 && botanize.getKnowledge(0.0D) <= 20.0D)
/*      */       {
/* 4359 */         return true;
/*      */       }
/* 4361 */       if (counter == 1.0F) {
/*      */         
/* 4363 */         if (!containsHerb) {
/*      */           
/* 4365 */           if (performer.getPlayingTime() < 86400000L) {
/* 4366 */             performer.getCommunicator().sendNormalServerMessage("This area looks picked clean. You should check another spot. New plants and herbs will grow over time so check back later.");
/*      */           }
/*      */           else {
/*      */             
/* 4370 */             performer.getCommunicator().sendNormalServerMessage("This area looks picked clean.", (byte)2);
/* 4371 */           }  return true;
/*      */         } 
/*      */         
/* 4374 */         int maxSearches = calcFBMaxSearches(getFBGrassLength(tile), botanize.getKnowledge(0.0D));
/*      */         
/* 4376 */         time = calcFBTickTimer(performer, botanize);
/* 4377 */         act.setNextTick(time);
/* 4378 */         act.setTickCount(1);
/* 4379 */         float totalTime = (time * maxSearches);
/*      */ 
/*      */         
/*      */         try {
/* 4383 */           performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */         }
/* 4385 */         catch (NoSuchActionException nsa) {
/*      */           
/* 4387 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*      */         
/* 4390 */         performer.getCommunicator().sendNormalServerMessage("You start to botanize in the area.");
/* 4391 */         Server.getInstance().broadCastAction(performer.getName() + " starts to botanize in the area.", performer, 5);
/* 4392 */         performer.sendActionControl(Actions.actionEntrys[act.getNumber()].getVerbString(), true, (int)totalTime);
/* 4393 */         performer.getStatus().modifyStamina(-500.0F);
/*      */       }
/*      */       else {
/*      */         
/* 4397 */         time = act.getTimeLeft();
/*      */       } 
/* 4399 */       if (act.mayPlaySound()) {
/* 4400 */         Methods.sendSound(performer, "sound.work.foragebotanize");
/*      */       }
/* 4402 */       if (counter * 10.0F >= act.getNextTick()) {
/*      */         
/* 4404 */         if (act.getRarity() != 0) {
/* 4405 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 4407 */         int searchCount = act.getTickCount();
/* 4408 */         GrassData.GrowthStage growthStage = getFBGrassLength(tile);
/* 4409 */         int currentSkill = (int)botanize.getKnowledge(0.0D);
/* 4410 */         int maxSearches = calcFBMaxSearches(growthStage, currentSkill);
/* 4411 */         act.incTickCount();
/* 4412 */         act.incNextTick(calcFBTickTimer(performer, botanize));
/* 4413 */         performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */         
/* 4416 */         if (searchCount >= maxSearches) {
/* 4417 */           toReturn = true;
/*      */         }
/* 4419 */         int knowledge = (int)botanize.getKnowledge(0.0D);
/*      */ 
/*      */ 
/*      */         
/* 4423 */         GrassData.GrowthStage currentGrowthStage = GrassData.GrowthStage.fromInt(maxSearches - searchCount);
/* 4424 */         Herb herb = Herb.getRandomHerb(performer, tileType, currentGrowthStage, act
/* 4425 */             .getNumber(), knowledge, tilex, tiley);
/* 4426 */         if (searchCount == 1)
/* 4427 */           TilePoller.setGrassHasSeeds(tilex, tiley, containsForage, false); 
/* 4428 */         if (herb == null) {
/*      */ 
/*      */           
/* 4431 */           String stritem = (act.getNumber() == 224) ? "anything" : ("any " + ((act.getData() > 1L) ? "more " : "") + act.getActionEntry().getActionString().toLowerCase());
/*      */           
/* 4433 */           if (maxSearches == 1) {
/* 4434 */             performer.getCommunicator().sendNormalServerMessage("You fail to find " + stritem + "!");
/* 4435 */           } else if (searchCount >= maxSearches) {
/* 4436 */             performer.getCommunicator().sendNormalServerMessage("You fail to find " + stritem + " and decide to stop looking!");
/*      */           } 
/*      */           
/* 4439 */           if (searchCount >= maxSearches && act.getData() == 0L) {
/* 4440 */             Server.getInstance().broadCastAction(performer
/* 4441 */                 .getName() + " looks displeased.", performer, 5);
/*      */           }
/*      */           
/* 4444 */           botanize.skillCheck(0.0D, 0.0D, false, counter / searchCount);
/*      */         }
/*      */         else {
/*      */           
/* 4448 */           float diff = herb.getDifficultyAt(knowledge);
/* 4449 */           double power = botanize.skillCheck(diff, 0.0D, false, counter / searchCount);
/*      */           
/*      */           try {
/* 4452 */             float ql = Herb.getQL(power, knowledge);
/* 4453 */             Item newItem = ItemFactory.createItem(herb.getItem(), Math.max(1.0F, ql), herb
/* 4454 */                 .getMaterial(), act.getRarity(), null);
/* 4455 */             if (ql < 0.0F) {
/* 4456 */               newItem.setDamage(-ql / 2.0F);
/*      */             } else {
/* 4458 */               newItem.setIsFresh(true);
/* 4459 */             }  Item inventory = performer.getInventory();
/* 4460 */             inventory.insertItem(newItem);
/* 4461 */             performer.getCommunicator().sendNormalServerMessage("You find " + newItem.getName() + "!");
/*      */             
/* 4463 */             Server.getInstance().broadCastAction(performer
/* 4464 */                 .getName() + " puts something in " + performer.getHisHerItsString() + " pocket.", performer, 5);
/*      */             
/* 4466 */             if (performer.getTutorialLevel() == 6 && !performer.skippedTutorial())
/*      */             {
/* 4468 */               if (performer.getKingdomId() != 3) {
/* 4469 */                 performer.missionFinished(true, true);
/*      */               }
/*      */             }
/* 4472 */             if (performer.checkCoinAward(100 * (maxSearches - searchCount + 1)))
/* 4473 */               performer.getCommunicator().sendSafeServerMessage("You also find a rare coin!"); 
/* 4474 */             if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem())
/*      */             {
/* 4476 */               performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */               
/* 4478 */               toReturn = true;
/*      */             }
/*      */           
/* 4481 */           } catch (FailedException fe) {
/*      */             
/* 4483 */             logger.log(Level.WARNING, performer.getName() + " " + fe.getMessage(), (Throwable)fe);
/*      */           }
/* 4485 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 4487 */             logger.log(Level.WARNING, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 4492 */         if (searchCount < maxSearches)
/*      */         {
/* 4494 */           act.setRarity(performer.getRarity());
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 4499 */       toReturn = true;
/*      */     } 
/* 4501 */     return toReturn;
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
/*      */   private boolean canBotanize(Creature performer, byte type, byte data) {
/* 4514 */     Tiles.Tile theTile = Tiles.getTile(type);
/* 4515 */     if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_MYCELIUM.id)
/*      */     {
/*      */       
/* 4518 */       return true; } 
/* 4519 */     if (type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_MOSS.id || type == Tiles.Tile.TILE_PEAT.id) {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 4525 */         Skill botanize = performer.getSkills().getSkill(10072);
/* 4526 */         if (type == Tiles.Tile.TILE_MARSH.id && botanize.getKnowledge(0.0D) >= 27.0D)
/* 4527 */           return true; 
/* 4528 */         if (type == Tiles.Tile.TILE_MOSS.id && botanize.getKnowledge(0.0D) >= 35.0D)
/* 4529 */           return true; 
/* 4530 */         if (type == Tiles.Tile.TILE_PEAT.id && botanize.getKnowledge(0.0D) >= 42.0D) {
/* 4531 */           return true;
/*      */         }
/* 4533 */       } catch (NoSuchSkillException noSuchSkillException) {}
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 4538 */       if (theTile == Tiles.Tile.TILE_BUSH_LINGONBERRY)
/*      */       {
/* 4540 */         return false;
/*      */       }
/* 4542 */       if (theTile.isNormalTree() || theTile.isNormalBush() || theTile
/* 4543 */         .isMyceliumTree() || theTile.isMyceliumBush())
/*      */       {
/*      */         
/* 4546 */         return (GrassData.GrowthTreeStage.decodeTileData(data) != GrassData.GrowthTreeStage.LAWN); } 
/*      */     } 
/* 4548 */     return false;
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
/*      */   private boolean forageV11(Action act, Creature performer, int tilex, int tiley, int tile, byte data, float counter) {
/* 4565 */     boolean toReturn = false;
/* 4566 */     byte tileType = Tiles.decodeType(tile);
/* 4567 */     if (canForage(performer, tileType, data)) {
/*      */       
/* 4569 */       if (Tiles.decodeHeight(tile) < 0) {
/*      */         
/* 4571 */         performer.getCommunicator().sendNormalServerMessage("Nothing useful will be found there.");
/* 4572 */         return true;
/*      */       } 
/* 4574 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 4576 */         performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put whatever you find.");
/*      */         
/* 4578 */         return true;
/*      */       } 
/* 4580 */       int time = 200;
/*      */       
/* 4582 */       boolean containsForage = Server.isForagable(tilex, tiley);
/* 4583 */       boolean containsHerb = Server.isBotanizable(tilex, tiley);
/* 4584 */       Skill foraging = performer.getSkills().getSkillOrLearn(10071);
/*      */       
/* 4586 */       if (act.getNumber() != 223 && foraging.getKnowledge(0.0D) <= 20.0D)
/*      */       {
/* 4588 */         return true;
/*      */       }
/* 4590 */       if (counter == 1.0F) {
/*      */         
/* 4592 */         if (!containsForage) {
/*      */           
/* 4594 */           if (performer.getPlayingTime() < 86400000L) {
/* 4595 */             performer.getCommunicator().sendNormalServerMessage("This area looks picked clean. You should check another spot. New plants and herbs will grow over time so check back later.");
/*      */           }
/*      */           else {
/*      */             
/* 4599 */             performer.getCommunicator().sendNormalServerMessage("This area looks picked clean.");
/* 4600 */           }  return true;
/*      */         } 
/*      */         
/* 4603 */         int maxSearches = calcFBMaxSearches(getFBGrassLength(tile), foraging.getKnowledge(0.0D));
/*      */         
/* 4605 */         time = calcFBTickTimer(performer, foraging);
/* 4606 */         act.setNextTick(time);
/* 4607 */         act.setTickCount(1);
/* 4608 */         act.setData(0L);
/* 4609 */         float totalTime = (time * maxSearches);
/*      */ 
/*      */         
/*      */         try {
/* 4613 */           performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */         }
/* 4615 */         catch (NoSuchActionException nsa) {
/*      */           
/* 4617 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*      */         
/* 4620 */         performer.getCommunicator().sendNormalServerMessage("You start to forage in the area.");
/* 4621 */         Server.getInstance().broadCastAction(performer.getName() + " starts to forage in the area.", performer, 5);
/* 4622 */         performer.sendActionControl(Actions.actionEntrys[act.getNumber()].getVerbString(), true, (int)totalTime);
/* 4623 */         performer.getStatus().modifyStamina(-500.0F);
/*      */       } else {
/*      */         
/* 4626 */         time = act.getTimeLeft();
/*      */       } 
/* 4628 */       if (act.mayPlaySound()) {
/* 4629 */         Methods.sendSound(performer, "sound.work.foragebotanize");
/*      */       }
/* 4631 */       if (counter * 10.0F >= act.getNextTick()) {
/*      */         
/* 4633 */         if (act.getRarity() != 0) {
/* 4634 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 4636 */         int searchCount = act.getTickCount();
/* 4637 */         GrassData.GrowthStage growthStage = getFBGrassLength(tile);
/* 4638 */         int currentSkill = (int)foraging.getKnowledge(0.0D);
/* 4639 */         int maxSearches = calcFBMaxSearches(growthStage, currentSkill);
/* 4640 */         act.incTickCount();
/* 4641 */         act.incNextTick(calcFBTickTimer(performer, foraging));
/*      */         
/* 4643 */         performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */         
/* 4646 */         if (searchCount >= maxSearches) {
/* 4647 */           toReturn = true;
/*      */         }
/* 4649 */         int knowledge = (int)foraging.getKnowledge(0.0D);
/*      */ 
/*      */ 
/*      */         
/* 4653 */         GrassData.GrowthStage currentGrowthStage = GrassData.GrowthStage.fromInt(Math.max(0, maxSearches - searchCount));
/* 4654 */         Forage forage = Forage.getRandomForage(performer, tileType, currentGrowthStage, act
/* 4655 */             .getNumber(), knowledge, tilex, tiley);
/* 4656 */         if (searchCount == 1)
/* 4657 */           TilePoller.setGrassHasSeeds(tilex, tiley, false, containsHerb); 
/* 4658 */         if (forage == null) {
/*      */ 
/*      */           
/* 4661 */           String stritem = (act.getNumber() == 223) ? "anything" : ("any " + ((act.getData() > 1L) ? "more " : "") + act.getActionEntry().getActionString().toLowerCase());
/*      */           
/* 4663 */           if (maxSearches == 1) {
/* 4664 */             performer.getCommunicator().sendNormalServerMessage("You fail to find " + stritem + "!");
/* 4665 */           } else if (searchCount >= maxSearches) {
/* 4666 */             performer.getCommunicator().sendNormalServerMessage("You fail to find " + stritem + " and decide to stop looking!");
/*      */           } 
/*      */           
/* 4669 */           if (searchCount >= maxSearches && act.getData() == 0L) {
/* 4670 */             Server.getInstance().broadCastAction(performer
/* 4671 */                 .getName() + " looks displeased.", performer, 5);
/*      */           }
/*      */           
/* 4674 */           foraging.skillCheck(0.0D, 0.0D, false, counter / searchCount);
/*      */         }
/*      */         else {
/*      */           
/* 4678 */           act.setData(act.getData() + 1L);
/* 4679 */           float diff = forage.getDifficultyAt(knowledge);
/*      */           
/* 4681 */           double power = foraging.skillCheck(diff, 0.0D, false, counter / searchCount);
/*      */           
/*      */           try {
/* 4684 */             float ql = Forage.getQL(power, knowledge);
/* 4685 */             Item newItem = ItemFactory.createItem(forage.getItem(), Math.max(1.0F, ql), forage
/* 4686 */                 .getMaterial(), act.getRarity(), null);
/* 4687 */             if (ql < 0.0F) {
/* 4688 */               newItem.setDamage(-ql / 2.0F);
/*      */             } else {
/* 4690 */               newItem.setIsFresh(true);
/* 4691 */             }  if (forage.getItem() == 464 && Items.mayLayEggs() && Server.rand
/* 4692 */               .nextInt(5) == 0)
/*      */             {
/*      */               
/* 4695 */               newItem.setData1(48);
/*      */             }
/* 4697 */             if (forage.getItem() == 466) {
/*      */               
/* 4699 */               performer.getCommunicator().sendNormalServerMessage("You find your Easter egg!");
/* 4700 */               if (performer.isPlayer()) {
/*      */                 
/*      */                 try {
/* 4703 */                   ((Player)performer).getSaveFile().setReimbursed(true);
/*      */                 }
/* 4705 */                 catch (IOException iOException) {}
/*      */               
/*      */               }
/*      */             }
/* 4709 */             else if (newItem.getTemplateId() == 266) {
/*      */               
/* 4711 */               String mat = Materials.convertMaterialByteIntoString(newItem.getMaterial());
/* 4712 */               performer.getCommunicator().sendNormalServerMessage("You find " + StringUtilities.addGenus(mat) + " " + newItem.getName() + "!");
/*      */             }
/*      */             else {
/*      */               
/* 4716 */               performer.getCommunicator().sendNormalServerMessage("You find " + StringUtilities.addGenus(newItem.getName()) + "!");
/*      */             } 
/* 4718 */             Item inventory = performer.getInventory();
/* 4719 */             inventory.insertItem(newItem);
/*      */             
/* 4721 */             if (performer.checkCoinAward(100 * (maxSearches - searchCount + 1)))
/* 4722 */               performer.getCommunicator().sendSafeServerMessage("You also find a rare coin!"); 
/* 4723 */             Server.getInstance().broadCastAction(performer
/* 4724 */                 .getName() + " puts something in " + performer.getHisHerItsString() + " pocket.", performer, 5);
/*      */             
/* 4726 */             if (performer.getTutorialLevel() == 6 && !performer.skippedTutorial())
/*      */             {
/* 4728 */               if (performer.getKingdomId() != 3) {
/* 4729 */                 performer.missionFinished(true, true);
/*      */               }
/*      */             }
/* 4732 */             if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem())
/*      */             {
/* 4734 */               performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */               
/* 4736 */               toReturn = true;
/*      */             }
/*      */           
/* 4739 */           } catch (FailedException fe) {
/*      */             
/* 4741 */             logger.log(Level.WARNING, performer.getName() + " " + fe.getMessage(), (Throwable)fe);
/*      */           }
/* 4743 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 4745 */             logger.log(Level.WARNING, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 4750 */         if (searchCount < maxSearches)
/*      */         {
/* 4752 */           act.setRarity(performer.getRarity());
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 4757 */       toReturn = true;
/*      */     } 
/* 4759 */     return toReturn;
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
/*      */   private boolean canForage(Creature performer, byte type, byte data) {
/* 4772 */     Tiles.Tile theTile = Tiles.getTile(type);
/* 4773 */     if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id)
/*      */     {
/* 4775 */       return true; } 
/* 4776 */     if (type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_TUNDRA.id || type == Tiles.Tile.TILE_MARSH.id) {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 4782 */         Skill forage = performer.getSkills().getSkill(10071);
/* 4783 */         if (type == Tiles.Tile.TILE_STEPPE.id && forage.getKnowledge(0.0D) >= 23.0D)
/* 4784 */           return true; 
/* 4785 */         if (type == Tiles.Tile.TILE_TUNDRA.id && forage.getKnowledge(0.0D) >= 33.0D)
/* 4786 */           return true; 
/* 4787 */         if (type == Tiles.Tile.TILE_MARSH.id && forage.getKnowledge(0.0D) >= 43.0D) {
/* 4788 */           return true;
/*      */         }
/* 4790 */       } catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 4795 */     else if (theTile.isNormalTree() || theTile.isNormalBush() || theTile
/* 4796 */       .isMyceliumTree() || theTile.isMyceliumBush()) {
/*      */ 
/*      */       
/* 4799 */       return (GrassData.GrowthTreeStage.decodeTileData(data) != GrassData.GrowthTreeStage.LAWN);
/*      */     } 
/* 4801 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int calcFBMaxSearches(GrassData.GrowthStage grassLength, double currentSkill) {
/* 4808 */     return Math.min(grassLength.getCode() + 1, (int)(currentSkill + 28.0D) / 27);
/*      */   }
/*      */ 
/*      */   
/*      */   private static GrassData.GrowthStage getFBGrassLength(int tile) {
/* 4813 */     byte tileType = Tiles.decodeType(tile);
/* 4814 */     Tiles.Tile theTile = Tiles.getTile(tileType);
/* 4815 */     if (tileType == Tiles.Tile.TILE_GRASS.id)
/*      */     {
/* 4817 */       return GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile));
/*      */     }
/* 4819 */     if (theTile.isTree() || theTile.isBush())
/*      */     {
/*      */       
/* 4822 */       return GrassData.GrowthStage.decodeTreeData(Tiles.decodeData(tile));
/*      */     }
/* 4824 */     return GrassData.GrowthStage.SHORT;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int calcFBTickTimer(Creature performer, Skill skill) {
/* 4829 */     return Actions.getQuickActionTime(performer, skill, null, 0.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hold(Action act, Creature performer, Item source, int tilex, int tiley, int tile, float counter) {
/* 4836 */     boolean toReturn = false;
/* 4837 */     int staminaDrainMod = 1;
/* 4838 */     if (Servers.localServer.EPIC)
/* 4839 */       staminaDrainMod = 3; 
/* 4840 */     int time = 12000;
/*      */     
/*      */     try {
/* 4843 */       performer.getCurrentAction().setTimeLeft(time);
/*      */     }
/* 4845 */     catch (NoSuchActionException nsa) {
/*      */       
/* 4847 */       logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */     } 
/* 4849 */     if (counter == 1.0F) {
/*      */       
/* 4851 */       performer.getCommunicator().sendNormalServerMessage("You hold the range pole vertically in front of you.");
/* 4852 */       Server.getInstance().broadCastAction(performer.getName() + " holds a range pole vertically in front of them.", performer, 5);
/*      */       
/* 4854 */       performer.sendActionControl(Actions.actionEntrys[act.getNumber()].getVerbString(), true, time);
/* 4855 */       performer.getStatus().modifyStamina((-3000 / staminaDrainMod));
/* 4856 */       act.setTickCount(0);
/*      */     } else {
/*      */       
/* 4859 */       time = act.getTimeLeft();
/*      */     } 
/*      */     
/* 4862 */     if (act.currentSecond() % 5 == 0) {
/*      */       
/* 4864 */       if (act.getTickCount() == 0) {
/*      */         
/* 4866 */         act.incTickCount();
/* 4867 */         Emotes.emoteAt((short)2014, performer, source);
/*      */       } 
/* 4869 */       performer.getStatus().modifyStamina((-2000 / staminaDrainMod));
/*      */     } 
/*      */     
/* 4872 */     if (performer.getStatus().getStamina() < 1000) {
/*      */       
/* 4874 */       performer.getCommunicator().sendNormalServerMessage("Your arms have got tired holding the range pole vertical in front of you, so you stop.");
/*      */       
/* 4876 */       Server.getInstance().broadCastAction(performer
/* 4877 */           .getName() + " stops holding the range pole vertically in front of them.", performer, 5);
/* 4878 */       toReturn = true;
/*      */     }
/* 4880 */     else if (counter * 10.0F >= act.getTimeLeft()) {
/*      */       
/* 4882 */       performer.getCommunicator().sendNormalServerMessage("You have got borred holding the range pole vertical in front of you, so you stop.");
/*      */       
/* 4884 */       Server.getInstance().broadCastAction(performer
/* 4885 */           .getName() + " stops holding the range pole vertically in front of them.", performer, 5);
/* 4886 */       toReturn = true;
/*      */     } 
/*      */     
/* 4889 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkTileDisembark(Creature performer, int tilex, int tiley) {
/*      */     try {
/* 4896 */       float targPosz = Zones.calculateHeight(((tilex << 2) + 2), ((tiley << 2) + 2), performer.isOnSurface());
/* 4897 */       float posz = performer.getPositionZ() + performer.getAltOffZ();
/* 4898 */       if (Math.abs(posz - targPosz) > 6.0F && targPosz > -1.0F)
/*      */       {
/* 4900 */         performer.getCommunicator().sendNormalServerMessage("That is too high.");
/* 4901 */         return true;
/*      */       }
/*      */     
/* 4904 */     } catch (NoSuchZoneException nsz) {
/*      */       
/* 4906 */       performer.getCommunicator().sendNormalServerMessage("That place is inaccessible.");
/* 4907 */       return true;
/*      */     } 
/* 4909 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handle_TRANSMUTATE(Creature performer, Item source, int tilex, int tiley, int tile, Action act, float counter) {
/* 4915 */     VolaTile vt = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 4916 */     if (vt != null && vt.getStructure() != null && vt.getStructure().isTypeHouse()) {
/*      */       
/* 4918 */       performer.getCommunicator().sendNormalServerMessage("You cannot transform a tile in a house.", (byte)3);
/* 4919 */       return true;
/*      */     } 
/* 4921 */     if (!performer.isPaying()) {
/*      */       
/* 4923 */       performer.getCommunicator().sendNormalServerMessage("You need to have premium time left in order to transform tiles.", (byte)3);
/*      */       
/* 4925 */       return true;
/*      */     } 
/*      */     
/* 4928 */     byte type = Tiles.decodeType(tile);
/* 4929 */     Tiles.Tile theTile = Tiles.getTile(type);
/* 4930 */     boolean reverting = Server.wasTransformed(tilex, tiley);
/*      */     
/* 4932 */     float potionQL = source.getCurrentQualityLevel();
/* 4933 */     int potionWeight = source.getWeightGrams();
/* 4934 */     int templateWeight = MethodsItems.getTransmutationSolidTemplateWeightGrams(source.getAuxData());
/* 4935 */     int numbSolid = potionWeight / templateWeight;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4940 */     float mod = MethodsItems.getTransmutationMod(performer, tilex, tiley, source.getAuxData(), reverting);
/* 4941 */     int extraQL = (int)(numbSolid * potionQL / 100.0F * mod);
/* 4942 */     if (extraQL == 0) {
/*      */       
/* 4944 */       performer.getCommunicator().sendNormalServerMessage("There is not enough " + source
/* 4945 */           .getName() + " there to make any difference to the " + theTile.getName() + ".", (byte)3);
/* 4946 */       return true;
/*      */     } 
/* 4948 */     if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4971 */       if (!Server.isBeingTransformed(tilex, tiley)) {
/*      */         
/* 4973 */         Server.setBeingTransformed(tilex, tiley, true);
/* 4974 */         Server.setPotionQLCount(tilex, tiley, 0);
/*      */       } 
/* 4976 */       int time = 100;
/* 4977 */       act.setTimeLeft(100);
/* 4978 */       performer.getCommunicator().sendNormalServerMessage("You start to pour the " + source
/* 4979 */           .getName() + " onto the " + theTile.getName() + ".");
/* 4980 */       Server.getInstance().broadCastAction(performer
/* 4981 */           .getName() + " starts to pour something onto a " + theTile.getName() + ".", performer, 5);
/* 4982 */       performer.sendActionControl(Actions.actionEntrys[462].getVerbString(), true, act.getTimeLeft());
/*      */     } 
/* 4984 */     if (act.currentSecond() == 2) {
/* 4985 */       performer.getCommunicator().sendNormalServerMessage("You see the " + theTile.getName() + " absorb the " + source.getName() + ".");
/* 4986 */     } else if (act.currentSecond() == 4) {
/* 4987 */       performer.getCommunicator().sendNormalServerMessage("You see the " + theTile.getName() + " start to effervesce.");
/* 4988 */     } else if (act.currentSecond() == 6) {
/* 4989 */       performer.getCommunicator().sendNormalServerMessage("The bubbles now obscure the " + theTile.getName() + ".");
/* 4990 */     } else if (act.currentSecond() == 8) {
/* 4991 */       performer.getCommunicator().sendNormalServerMessage("The bubbles start receeding.");
/* 4992 */     }  if (act.currentSecond() % 2 == 0)
/*      */     {
/* 4994 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     }
/* 4996 */     if (counter > (act.getTimeLeft() / 10)) {
/*      */       
/* 4998 */       if (act.getRarity() != 0) {
/*      */         
/* 5000 */         performer.playPersonalSound("sound.fx.drumroll");
/* 5001 */         switch (act.getRarity()) {
/*      */           
/*      */           case 1:
/* 5004 */             extraQL = (int)(extraQL * 1.2F);
/*      */             break;
/*      */           case 2:
/* 5007 */             extraQL = (int)(extraQL * 1.5F);
/*      */             break;
/*      */           case 3:
/* 5010 */             extraQL = (int)(extraQL * 1.8F);
/*      */             break;
/*      */         } 
/*      */ 
/*      */       
/*      */       } 
/* 5016 */       int potionTileQLCount = Server.getPotionQLCount(tilex, tiley);
/* 5017 */       int newTileQLCount = potionTileQLCount + extraQL;
/*      */       
/* 5019 */       if (newTileQLCount >= 100) {
/*      */ 
/*      */ 
/*      */         
/* 5023 */         byte newTileType = MethodsItems.getTransmutedToTileType(source.getAuxData());
/* 5024 */         short height = Tiles.decodeHeight(tile);
/* 5025 */         Server.setSurfaceTile(tilex, tiley, height, newTileType, (byte)0);
/* 5026 */         Server.setBeingTransformed(tilex, tiley, false);
/* 5027 */         Server.setTransformed(tilex, tiley, true);
/* 5028 */         Players.getInstance().sendChangedTiles(tilex, tiley, 1, 1, true, true);
/*      */         
/*      */         try {
/* 5031 */           Zone toCheckForChange = Zones.getZone(tilex, tiley, true);
/* 5032 */           toCheckForChange.changeTile(tilex, tiley);
/*      */         }
/* 5034 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 5036 */           logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */         } 
/*      */         
/* 5039 */         newTileQLCount = 0;
/* 5040 */         Tiles.Tile newTile = Tiles.getTile(newTileType);
/* 5041 */         performer.getCommunicator().sendNormalServerMessage("Yeah! You changed the " + theTile
/* 5042 */             .getName() + " tile to " + newTile.getName() + "!", (byte)2);
/*      */         
/* 5044 */         if (performer.fireTileLog()) {
/* 5045 */           TileEvent.log(tilex, tiley, 0, performer.getWurmId(), 462);
/*      */         }
/*      */       } else {
/*      */         
/* 5049 */         performer.getCommunicator().sendNormalServerMessage("Looks like that tile needs more of that liquid to change it.", (byte)2);
/*      */       } 
/*      */ 
/*      */       
/* 5053 */       Server.setPotionQLCount(tilex, tiley, newTileQLCount);
/* 5054 */       Items.destroyItem(source.getWurmId());
/* 5055 */       return true;
/*      */     } 
/* 5057 */     return false;
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
/*      */   static void sendTileTransformationState(Creature performer, int tilex, int tiley, byte tileType) {
/* 5071 */     if (tileType == Tiles.Tile.TILE_GRASS.id || tileType == Tiles.Tile.TILE_MYCELIUM.id || tileType == Tiles.Tile.TILE_SAND.id || tileType == Tiles.Tile.TILE_STEPPE.id || tileType == Tiles.Tile.TILE_CLAY.id || tileType == Tiles.Tile.TILE_PEAT.id || tileType == Tiles.Tile.TILE_TAR.id) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5076 */       if (Server.wasTransformed(tilex, tiley))
/*      */       {
/* 5078 */         performer.getCommunicator().sendNormalServerMessage("The tile has been transformed before.");
/*      */       }
/* 5080 */       if (Server.isBeingTransformed(tilex, tiley)) {
/*      */ 
/*      */         
/* 5083 */         int potionCount = Server.getPotionQLCount(tilex, tiley);
/* 5084 */         if (potionCount == 255)
/*      */           return; 
/* 5086 */         if (potionCount > 97) {
/* 5087 */           performer.getCommunicator().sendNormalServerMessage("The tile is so close to being completely transformed.");
/* 5088 */         } else if (potionCount > 90) {
/* 5089 */           performer.getCommunicator().sendNormalServerMessage("The tile has almost been completely transformed.");
/* 5090 */         } else if (potionCount > 75) {
/* 5091 */           performer.getCommunicator().sendNormalServerMessage("The tile is over three quarters transformed.");
/* 5092 */         } else if (potionCount > 50) {
/* 5093 */           performer.getCommunicator().sendNormalServerMessage("The tile is over half way transformed.");
/* 5094 */         } else if (potionCount > 25) {
/* 5095 */           performer.getCommunicator().sendNormalServerMessage("The tile is over a quarter transformed.");
/* 5096 */         } else if (potionCount > 0) {
/* 5097 */           performer.getCommunicator().sendNormalServerMessage("Someone has started transforming this tile.");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean useRuneOnTile(Action act, Creature performer, Item source, int x, int y, int layer, int heightOffset, short action, float counter) {
/* 5105 */     if (RuneUtilities.isEnchantRune(source)) {
/*      */       
/* 5107 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the rune on that.", (byte)3);
/* 5108 */       return true;
/*      */     } 
/* 5110 */     if (RuneUtilities.isSingleUseRune(source) && RuneUtilities.getSpellForRune(source) != null)
/*      */     {
/* 5112 */       if (!Methods.isActionAllowed(performer, (short)245, x, y)) {
/*      */         
/* 5114 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to use that here.", (byte)3);
/* 5115 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 5119 */     int time = act.getTimeLeft();
/* 5120 */     if (counter == 1.0F) {
/*      */       
/* 5122 */       String actionString = "use the rune";
/* 5123 */       performer.getCommunicator().sendNormalServerMessage("You start to use the rune.");
/* 5124 */       time = Actions.getSlowActionTime(performer, performer.getSoulDepth(), null, 0.0D);
/* 5125 */       act.setTimeLeft(time);
/* 5126 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 5127 */       performer.getStatus().modifyStamina(-600.0F);
/*      */     } 
/* 5129 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 5131 */       performer.getStatus().modifyStamina(-300.0F);
/*      */     }
/* 5133 */     if (counter * 10.0F > time) {
/*      */       
/* 5135 */       Skill soulDepth = performer.getSoulDepth();
/* 5136 */       double diff = (20.0F + source.getDamage()) - (source.getCurrentQualityLevel() + source.getRarity()) - 45.0D;
/* 5137 */       double power = soulDepth.skillCheck(diff, source.getCurrentQualityLevel(), false, counter);
/* 5138 */       if (power > 0.0D) {
/*      */         
/* 5140 */         if (RuneUtilities.getSpellForRune(source) != null && RuneUtilities.getSpellForRune(source).isTargetTile())
/*      */         {
/* 5142 */           RuneUtilities.getSpellForRune(source).castSpell(50.0D, performer, x, y, layer, heightOffset);
/*      */         }
/*      */         else
/*      */         {
/* 5146 */           performer.getCommunicator().sendNormalServerMessage("You can't use the rune on that.", (byte)3);
/* 5147 */           return true;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 5152 */         performer.getCommunicator().sendNormalServerMessage("You try to use the rune but fail.", (byte)3);
/*      */       } 
/* 5154 */       if (Servers.isThisATestServer())
/*      */       {
/* 5156 */         performer.getCommunicator().sendNormalServerMessage("Diff: " + diff + ", bonus: " + source.getCurrentQualityLevel() + ", sd: " + soulDepth
/* 5157 */             .getKnowledge() + ", power: " + power + ", chance: " + soulDepth.getChance(diff, null, source.getCurrentQualityLevel()));
/*      */       }
/* 5159 */       Items.destroyItem(source.getWurmId());
/* 5160 */       return true;
/*      */     } 
/* 5162 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean investigateTile(Action act, Creature performer, Item source, int tilex, int tiley, int layer, int tile, int heightOffset, short action, float counter) {
/* 5168 */     if (!source.getTemplate().isDiggingtool() && source.getTemplateId() != 493) {
/*      */       
/* 5170 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " wouldn't really help for investigating this area.", (byte)3);
/* 5171 */       return true;
/*      */     } 
/* 5173 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */       
/* 5175 */       performer.getCommunicator().sendNormalServerMessage("You have no more space in your inventory for any found items. Make some room first.", (byte)3);
/* 5176 */       return true;
/*      */     } 
/* 5178 */     if (!performer.canCarry(1000)) {
/*      */       
/* 5180 */       performer.getCommunicator().sendNormalServerMessage("You're unable to carry the weight of any found items. Make some room first.", (byte)3);
/* 5181 */       return true;
/*      */     } 
/*      */     
/* 5184 */     Skill archaeology = performer.getSkills().getSkillOrLearn(10069);
/* 5185 */     double archSkill = archaeology.getKnowledge(0.0D);
/* 5186 */     if (source.getTemplate().isDiggingtool() && archSkill < 20.0D) {
/*      */       
/* 5188 */       performer.getCommunicator().sendNormalServerMessage("You don't have enough skill to use the " + source.getName() + " as an investigative tool.", (byte)3);
/*      */       
/* 5190 */       return true;
/*      */     } 
/* 5192 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 5193 */     if (t != null)
/*      */     {
/* 5195 */       if (t.getStructure() != null) {
/*      */         
/* 5197 */         performer.getCommunicator().sendNormalServerMessage("You decide against investigating inside the structure. Maybe outside will be better.");
/* 5198 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 5202 */     boolean canInvestigate = Server.isInvestigatable(tilex, tiley);
/*      */     
/* 5204 */     int time = act.getTimeLeft();
/* 5205 */     if (counter == 1.0F) {
/*      */       
/* 5207 */       if (!canInvestigate) {
/*      */         
/* 5209 */         performer.getCommunicator().sendNormalServerMessage("The area looks picked clean.");
/* 5210 */         return true;
/*      */       } 
/*      */       
/* 5213 */       performer.getCommunicator().sendNormalServerMessage("You start to investigate the area.");
/* 5214 */       Server.getInstance().broadCastAction(performer.getName() + " starts to investigate the area.", performer, 5);
/* 5215 */       time = Actions.getVariableActionTime(performer, archaeology, source, 0.0D, 200, 60, 2500);
/* 5216 */       if (source.getTemplateId() == 493)
/* 5217 */         time = (int)(time - time / 10.0F); 
/* 5218 */       act.setTimeLeft(time);
/* 5219 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 5220 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     } 
/* 5222 */     if (act.currentSecond() % 5 == 0) {
/*      */       
/* 5224 */       performer.getStatus().modifyStamina(-500.0F);
/* 5225 */       if (Server.rand.nextInt(25) == 0 || performer.getPower() >= 4) {
/*      */         
/* 5227 */         double power = archaeology.skillCheck(30.0D, source, 0.0D, false, 10.0F);
/* 5228 */         if (power > 0.0D) {
/*      */           
/* 5230 */           ArrayList<DeadVillage> currentTargets = Villages.getDeadVillagesFor(tilex, tiley);
/* 5231 */           ArrayList<DeadVillage> nearbyTargets = Villages.getDeadVillagesNear(tilex, tiley, (int)(power * 2.5D));
/* 5232 */           nearbyTargets.removeAll(currentTargets);
/* 5233 */           if (!nearbyTargets.isEmpty()) {
/*      */             
/* 5235 */             String toSend = "You spot some markers of an old settlement in the area.";
/* 5236 */             if (archSkill >= 30.0D) {
/*      */               
/* 5238 */               int randomDeed = Server.rand.nextInt(nearbyTargets.size());
/* 5239 */               DeadVillage actualDeed = nearbyTargets.get(randomDeed);
/* 5240 */               String distance = "off";
/* 5241 */               if (archSkill >= 50.0D)
/* 5242 */                 distance = actualDeed.getDistanceFrom(tilex, tiley); 
/* 5243 */               toSend = toSend + " It looks like it may be " + distance + " to the " + actualDeed.getDirectionFrom(tilex, tiley) + " from here.";
/* 5244 */               if (archSkill >= 70.0D)
/* 5245 */                 toSend = toSend + " You find a small scrap of something that has the deed name on it... '" + actualDeed.getDeedName() + "'."; 
/*      */             } 
/* 5247 */             performer.getCommunicator().sendNormalServerMessage(toSend);
/*      */ 
/*      */           
/*      */           }
/* 5251 */           else if (currentTargets.isEmpty()) {
/* 5252 */             performer.getCommunicator().sendNormalServerMessage("You can't find any traces of any recognizable nearby settlements.");
/*      */           } else {
/* 5254 */             performer.getCommunicator().sendNormalServerMessage("You can't find any traces of any other distance settlements, only what used to be in this area.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 5260 */     if (counter * 10.0F > time) {
/*      */       
/* 5262 */       if (act.getRarity() != 0) {
/* 5263 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/* 5265 */       byte type = Tiles.decodeType(tile);
/* 5266 */       ArrayList<DeadVillage> possibleTargets = Villages.getDeadVillagesFor(tilex, tiley);
/* 5267 */       double negBase = 0.1D * archSkill;
/* 5268 */       double negBonus = 0.0D;
/* 5269 */       Server.setInvestigatable(tilex, tiley, false);
/*      */       
/* 5271 */       if (possibleTargets.isEmpty()) {
/*      */         
/* 5273 */         negBonus += negBase * 3.0D;
/* 5274 */         ArrayList<DeadVillage> nearbyTargets = Villages.getDeadVillagesNear(tilex, tiley, (int)(archSkill * 2.0D));
/* 5275 */         if (nearbyTargets.isEmpty()) {
/* 5276 */           negBonus += negBase * 3.0D;
/*      */         }
/*      */       } else {
/*      */         
/* 5280 */         for (DeadVillage dv : possibleTargets) {
/*      */           
/* 5282 */           negBonus -= Math.min(negBase * 5.0D, (dv.getTotalAge() * dv.getTimeSinceDisband()));
/* 5283 */           if (dv.getKingdomId() != performer.getKingdomId())
/* 5284 */             negBonus -= 5.0D; 
/*      */         } 
/*      */       } 
/* 5287 */       if (Terraforming.isRoad(type)) {
/* 5288 */         negBonus += negBase * 2.0D;
/* 5289 */       } else if (Tiles.isGrassType(type) || Tiles.isTree(type)) {
/*      */ 
/*      */ 
/*      */         
/* 5293 */         GrassData.GrowthStage grass = Tiles.isGrassType(type) ? GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile)) : GrassData.GrowthStage.decodeTreeData(Tiles.decodeData(tile));
/* 5294 */         if (grass == GrassData.GrowthStage.TALL) {
/* 5295 */           negBonus -= negBase * 0.5D;
/* 5296 */         } else if (grass == GrassData.GrowthStage.WILD) {
/* 5297 */           negBonus -= negBase;
/*      */         } 
/* 5299 */       }  if (t != null) {
/*      */         
/* 5301 */         if (t.getVillage() != null)
/* 5302 */           negBonus += negBase * 2.0D; 
/* 5303 */         Fence[] allFences = t.getAllFences();
/* 5304 */         if (allFences.length > 0)
/* 5305 */           negBonus += allFences.length * negBase; 
/* 5306 */         negBonus += t.getNumberOfItems(0) * negBase * 0.5D;
/* 5307 */         negBonus += t.getNumberOfDecorations(0) * negBase * 0.5D;
/*      */       } 
/* 5309 */       if (source.isDiggingtool()) {
/* 5310 */         negBonus += negBase;
/*      */       }
/* 5312 */       double tileMax = Math.min(archSkill + (100.0D - archSkill) * 0.20000000298023224D, archSkill * 0.75D - negBonus);
/* 5313 */       double diffBonus = Math.max(0.0D, Math.min(archSkill * 0.25D, -negBonus * 2.0D - tileMax) * 0.5D);
/* 5314 */       double power = archaeology.skillCheck(Math.max(tileMax * 0.8999999761581421D - diffBonus, archSkill / 5.0D), source, 0.0D, false, counter);
/* 5315 */       double difference = Math.max(0.0D, archSkill - tileMax);
/*      */ 
/*      */       
/*      */       try {
/* 5319 */         performer.getSkills().getSkillOrLearn(source.getPrimarySkill()).skillCheck(Math.max(tileMax, archSkill / 5.0D), 0.0D, false, counter);
/*      */       }
/* 5321 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */       
/* 5325 */       source.setDamage((float)(source.getDamage() + (100.0D - power) * 9.999999747378752E-5D * source.getDamageModifier()));
/*      */       
/* 5327 */       if (Servers.localServer.testServer)
/* 5328 */         performer.getCommunicator().sendNormalServerMessage("[TEST: negBonus: " + negBonus + ", tileMax: " + tileMax + ", arch: " + archSkill + ", power: " + power + ", #deadDeeds: " + possibleTargets
/* 5329 */             .size() + "]"); 
/* 5330 */       if (power >= difference) {
/*      */         
/*      */         try {
/*      */           
/* 5334 */           double fragmentPower = (tileMax < 0.0D && Server.rand.nextInt(3) == 0) ? (archSkill / 5.0D) : tileMax;
/* 5335 */           FragmentUtilities.Fragment f = FragmentUtilities.getRandomFragmentForSkill(fragmentPower, !((power >= tileMax && tileMax >= archSkill) ? 1 : 0));
/* 5336 */           if (f != null) {
/*      */             
/* 5338 */             Item fragment = ItemFactory.createItem(1307, 
/* 5339 */                 (float)Math.min(power, archSkill + (100.0D - archSkill) * 0.20000000298023224D), act
/* 5340 */                 .getRarity(), null);
/* 5341 */             fragment.setRealTemplate(f.getItemId());
/* 5342 */             if (fragment.getRealTemplate().getMaterial() != f.getMaterial())
/* 5343 */               fragment.setMaterial((byte)f.getMaterial()); 
/* 5344 */             if (power > 75.0D) {
/*      */               
/* 5346 */               fragment.setData1(1);
/* 5347 */               fragment.setData2((int)(power / 2.0D));
/* 5348 */               fragment.setAuxData((byte)1);
/* 5349 */               fragment.setWeight(fragment.getRealTemplate().getWeightGrams() / fragment.getRealTemplate().getFragmentAmount(), false);
/*      */             } 
/*      */             
/* 5352 */             performer.getInventory().insertItem(fragment);
/* 5353 */             performer.getCommunicator().sendNormalServerMessage("You pick out a fragment of some item wedged into the ground." + (Servers.localServer.testServer ? (" [TEST: " + fragment
/* 5354 */                 .getActualName() + " ql: " + fragment.getCurrentQualityLevel() + " realTemplate: " + fragment
/* 5355 */                 .getRealTemplate().getName() + "]") : ""));
/* 5356 */             Server.getInstance().broadCastAction(performer.getName() + " looks excited as " + performer.getHeSheItString() + " picks up a small fragment of something.", performer, 5);
/*      */ 
/*      */             
/* 5359 */             performer.achievement(479);
/* 5360 */             if (fragment.getRarity() > 0) {
/* 5361 */               performer.achievement(481);
/*      */             }
/*      */           } 
/* 5364 */           if (WurmCalendar.isAnniversary() && !performer.hasFlag(55) && Server.rand
/* 5365 */             .nextInt(15) == 0 && performer.isPaying())
/*      */           {
/* 5367 */             Item specialFragment = ItemFactory.createItem(1307, 80.0F, act
/* 5368 */                 .getRarity(), null);
/* 5369 */             specialFragment.setRealTemplate(651);
/* 5370 */             specialFragment.setName("special fragment");
/* 5371 */             performer.getInventory().insertItem(specialFragment);
/* 5372 */             performer.getCommunicator().sendSafeServerMessage("You also find another fragment that looks a bit different.");
/*      */           }
/*      */         
/* 5375 */         } catch (FailedException|NoSuchTemplateException failedException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5381 */       if (power > 0.0D && !possibleTargets.isEmpty()) {
/*      */         
/* 5383 */         StringBuilder toSend = new StringBuilder();
/* 5384 */         if (archSkill >= 15.0D && Server.rand.nextInt(3) == 0) {
/*      */           DeadVillage dv;
/*      */           
/* 5387 */           if (possibleTargets.size() > 1) {
/*      */             
/* 5389 */             dv = possibleTargets.get(Server.rand.nextInt(possibleTargets.size()));
/* 5390 */             toSend.append("You can see multiple markers of abandoned settlements here. ");
/* 5391 */             if (archSkill >= 20.0D) {
/* 5392 */               toSend.append("Based on your knowledge of the area and small hints you can find, one of the settlements must have been called " + dv
/* 5393 */                   .getDeedName() + ". ");
/*      */             }
/*      */           } else {
/*      */             
/* 5397 */             dv = possibleTargets.get(0);
/* 5398 */             toSend.append("You can see signs of a single abandoned settlement here. ");
/* 5399 */             if (archSkill >= 20.0D)
/* 5400 */               toSend.append("Based on your knowledge of the area and small hints you can find, the settlement must have been called " + dv
/* 5401 */                   .getDeedName() + ". "); 
/*      */           } 
/* 5403 */           Item report = null;
/* 5404 */           for (Item i : performer.getInventory().getAllItems(false)) {
/*      */             
/* 5406 */             boolean gotExistingReport = false;
/* 5407 */             if (i.getTemplateId() == 1404)
/*      */             {
/* 5409 */               for (Item j : i.getAllItems(false)) {
/*      */                 
/* 5411 */                 if (j.getTemplateId() == 1403)
/*      */                 {
/* 5413 */                   if (report == null && j.getData() == -1L) {
/* 5414 */                     report = j;
/* 5415 */                   } else if (j.getData() == dv.getDeedId()) {
/*      */                     
/* 5417 */                     report = j;
/* 5418 */                     gotExistingReport = true;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             }
/* 5425 */             if (gotExistingReport) {
/*      */               break;
/*      */             }
/*      */           } 
/* 5429 */           if (report != null && report.getData() == -1L) {
/*      */             
/* 5431 */             report.setData(dv.getDeedId());
/* 5432 */             report.setName(dv.getDeedName() + " report");
/* 5433 */             report.setAuxBit(0, true);
/* 5434 */             report.sendUpdate();
/* 5435 */             toSend.append("You write down some initial location details about " + dv.getDeedName() + " in a blank report. ");
/*      */           }
/* 5437 */           else if (report != null) {
/*      */             
/* 5439 */             if (!report.getAuxBit(1) && archSkill >= 25.0D && (Server.rand.nextInt(5) == 0 || performer
/* 5440 */               .getPower() >= 4)) {
/*      */               
/* 5442 */               report.setAuxBit(1, true);
/* 5443 */               toSend.append("You note some additional details about the location of " + dv.getDeedName() + " in the report. ");
/*      */             }
/* 5445 */             else if (report.getAuxBit(1) == true && !report.getAuxBit(2) && archSkill >= 30.0D && (Server.rand
/* 5446 */               .nextInt(10) == 0 || performer.getPower() >= 4)) {
/*      */               
/* 5448 */               report.setAuxBit(2, true);
/* 5449 */               toSend.append("You find some extra clues that help narrow down where " + dv.getDeedName() + " once was and write it down in the report. ");
/*      */             
/*      */             }
/* 5452 */             else if (report.getAuxBit(2) == true && !report.getAuxBit(3) && archSkill >= 35.0D && (Server.rand
/* 5453 */               .nextInt(20) == 0 || performer.getPower() >= 4)) {
/*      */               
/* 5455 */               report.setAuxBit(3, true);
/* 5456 */               toSend.append("You feel confident you know exactly where " + dv.getDeedName() + " once lay, and complete the location details in the report. ");
/*      */             } 
/*      */           } 
/*      */           
/* 5460 */           if (archSkill >= 40.0D && (Server.rand.nextInt(10) == 0 || performer.getPower() >= 4)) {
/*      */             
/* 5462 */             toSend.append("You find a scrap of washed out parchment signed by the last mayor, " + dv.getMayorName() + ". ");
/* 5463 */             if (report != null && !report.getAuxBit(4)) {
/*      */               
/* 5465 */               report.setAuxBit(4, true);
/* 5466 */               toSend.append("You write that down in your report. ");
/*      */             } 
/*      */           } 
/* 5469 */           if (archSkill >= 55.0D && (Server.rand.nextInt(10) == 0 || performer.getPower() >= 4)) {
/*      */             
/* 5471 */             toSend.append("You recall this settlement, and remember the name of the founder as " + dv.getFounderName() + ". ");
/* 5472 */             if (report != null && !report.getAuxBit(5)) {
/*      */               
/* 5474 */               report.setAuxBit(5, true);
/* 5475 */               toSend.append("You write that down in your report. ");
/*      */             } 
/*      */           } 
/* 5478 */           if (archSkill >= 70.0D && (Server.rand.nextInt(10) == 0 || performer.getPower() >= 4)) {
/*      */             
/* 5480 */             toSend.append("It looks to have been abandoned for roughly " + 
/* 5481 */                 DeadVillage.getTimeString(dv.getTimeSinceDisband(), (dv.getTimeSinceDisband() > 12.0F)) + ". ");
/* 5482 */             if (report != null && !report.getAuxBit(6)) {
/*      */               
/* 5484 */               report.setAuxBit(6, true);
/* 5485 */               toSend.append("You write that down in your report. ");
/*      */             } 
/*      */           } 
/* 5488 */           if (archSkill >= 80.0D && (Server.rand.nextInt(10) == 0 || performer.getPower() >= 4)) {
/*      */             
/* 5490 */             toSend.append("You make a rough estimate that the settlement was inhabited for about " + 
/* 5491 */                 DeadVillage.getTimeString(dv.getTotalAge(), false) + ". ");
/* 5492 */             if (report != null && !report.getAuxBit(7)) {
/*      */               
/* 5494 */               report.setAuxBit(7, true);
/* 5495 */               toSend.append("You write that down in your report. ");
/*      */             } 
/*      */           } 
/*      */           
/* 5499 */           if (report == null) {
/* 5500 */             toSend.append("If you had an archaeology journal with a blank report in it you could record your findings.");
/*      */           }
/*      */         } else {
/*      */           
/* 5504 */           toSend.append("You can't quite make anything definitive out, but there may have once been a settlement here.");
/*      */         } 
/* 5506 */         performer.getCommunicator().sendNormalServerMessage(toSend.toString());
/*      */         
/* 5508 */         if (Server.rand.nextInt(50) == 0)
/*      */         {
/* 5510 */           if ((t != null && t.getVillage() == null) || t == null)
/*      */           {
/* 5512 */             EpicMission m = EpicServerStatus.getMISacrificeMission();
/* 5513 */             if (m != null) {
/*      */               
/*      */               try {
/*      */                 
/* 5517 */                 Item missionItem = ItemFactory.createItem(737, (20 + Server.rand.nextInt(80)), act.getRarity(), m.getEntityName());
/* 5518 */                 missionItem.setName(HexMap.generateFirstName(m.getMissionId()) + ' ' + HexMap.generateSecondName(m.getMissionId()));
/*      */                 
/* 5520 */                 performer.getInventory().insertItem(missionItem);
/* 5521 */                 performer.getCommunicator().sendNormalServerMessage("You find a " + missionItem.getName() + " in amongst the dirt.");
/*      */               }
/* 5523 */               catch (FailedException|NoSuchTemplateException failedException) {}
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       }
/* 5531 */       else if (power > 0.0D) {
/*      */         
/* 5533 */         performer.getCommunicator().sendNormalServerMessage("You can't find any traces of any abandoned settlements here.");
/*      */       }
/*      */       else {
/*      */         
/* 5537 */         performer.getCommunicator().sendNormalServerMessage("You can't seem to find anything of use.");
/*      */       } 
/*      */       
/* 5540 */       return true;
/*      */     } 
/* 5542 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */