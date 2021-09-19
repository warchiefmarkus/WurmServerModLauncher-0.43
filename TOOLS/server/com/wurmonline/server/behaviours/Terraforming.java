/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.math.Vector2f;
/*      */ import com.wurmonline.mesh.BushData;
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.mesh.TreeData;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.combat.Weapon;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.epic.EpicMission;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.epic.HexMap;
/*      */ import com.wurmonline.server.highways.HighwayPos;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.Materials;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.QuestionTypes;
/*      */ import com.wurmonline.server.questions.SimplePopup;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.DbFence;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*      */ import com.wurmonline.server.utils.CoordUtils;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.FocusZone;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*      */ import com.wurmonline.shared.util.TerrainUtilities;
/*      */ import edu.umd.cs.findbugs.annotations.NonNull;
/*      */ import java.io.IOException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
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
/*      */ public final class Terraforming
/*      */   implements MiscConstants, QuestionTypes, ItemTypes, CounterTypes, ItemMaterials, SoundNames, VillageStatus, TimeConstants
/*      */ {
/*      */   public static final String cvsversion = "$Id: Terraforming.java,v 1.61 2007-04-19 23:05:18 root Exp $";
/*      */   public static final short MAX_WATER_DIG_DEPTH = -7;
/*      */   public static final short MAX_PAVE_DEPTH = -100;
/*      */   public static final short MAX_HEIGHT_DIFF = 20;
/*      */   public static final short MAX_DIAG_HEIGHT_DIFF = 28;
/*  128 */   private static final Logger logger = Logger.getLogger(Terraforming.class.getName());
/*      */   
/*  130 */   private static int[][] flattenTiles = new int[4][4];
/*      */   
/*  132 */   private static int[][] rockTiles = new int[4][4];
/*      */   
/*  134 */   private static final int[] noCaveDoor = new int[] { -1, -1 };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  139 */   private static int flattenImmutable = 0;
/*      */   
/*  141 */   private static byte newType = 0;
/*      */   
/*  143 */   private static byte oldType = 0;
/*      */ 
/*      */   
/*  146 */   private static int newTile = 0;
/*      */   
/*      */   private static final float DIGGING_SKILL_MULT = 3.0F;
/*      */   
/*      */   private static final int saltUsed = 1000;
/*      */   
/*  152 */   private static final Random r = new Random();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isImmutableTile(byte type) {
/*  163 */     return (Tiles.isTree(type) || Tiles.isBush(type) || type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_PEAT.id || type == Tiles.Tile.TILE_TAR.id || type == Tiles.Tile.TILE_HOLE.id || type == Tiles.Tile.TILE_MOSS.id || type == Tiles.Tile.TILE_LAVA.id || 
/*      */ 
/*      */       
/*  166 */       Tiles.isMineDoor(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isImmutableOrRoadTile(byte type) {
/*  173 */     return (isRoad(type) || isImmutableTile(type));
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean isTileOverriddenByDirt(byte type) {
/*  178 */     return (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isRoad(byte type) {
/*  184 */     return Tiles.isRoadType(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isSculptable(byte type) {
/*  189 */     return (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_ROCK.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_TUNDRA.id || type == Tiles.Tile.TILE_KELP.id || type == Tiles.Tile.TILE_REED.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isNonDiggableTile(byte type) {
/*  197 */     return (Tiles.isTree(type) || Tiles.isBush(type) || type == Tiles.Tile.TILE_LAVA.id || 
/*  198 */       Tiles.isSolidCave(type) || type == Tiles.Tile.TILE_CAVE_EXIT.id || 
/*  199 */       Tiles.isMineDoor(type) || type == Tiles.Tile.TILE_HOLE.id || type == Tiles.Tile.TILE_CAVE.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isTileTurnToDirt(byte type) {
/*  208 */     return (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_TUNDRA.id || type == Tiles.Tile.TILE_REED.id || type == Tiles.Tile.TILE_KELP.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id || type == Tiles.Tile.TILE_FIELD2.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isCultivatable(byte type) {
/*  215 */     return (type == Tiles.Tile.TILE_DIRT_PACKED.id || type == Tiles.Tile.TILE_MOSS.id || type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_MYCELIUM.id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isSwitchableTiles(int templateId, byte tileType) {
/*  221 */     return ((templateId == 26 && tileType == Tiles.Tile.TILE_SAND.id) || (templateId == 298 && tileType == Tiles.Tile.TILE_DIRT.id));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isTileGrowTree(byte type) {
/*  227 */     return (type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_MOSS.id || type == Tiles.Tile.TILE_REED.id || type == Tiles.Tile.TILE_KELP.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isTileGrowHedge(byte type) {
/*  234 */     return (type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_MOSS.id || 
/*      */       
/*  236 */       Tiles.isTree(type) || Tiles.isBush(type) || type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_REED.id || type == Tiles.Tile.TILE_KELP.id || type == Tiles.Tile.TILE_LAWN.id || type == Tiles.Tile.TILE_MYCELIUM_LAWN.id || type == Tiles.Tile.TILE_ENCHANTED_GRASS.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isRockTile(byte type) {
/*  243 */     return (Tiles.isSolidCave(type) || type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_EXIT.id || type == Tiles.Tile.TILE_CLIFF.id || type == Tiles.Tile.TILE_ROCK.id || type == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isBuildTile(byte type) {
/*  249 */     return (!isRockTile(type) && type != Tiles.Tile.TILE_FIELD.id && type != Tiles.Tile.TILE_FIELD2.id && type != Tiles.Tile.TILE_CLAY.id && type != Tiles.Tile.TILE_SAND.id && type != Tiles.Tile.TILE_HOLE.id && 
/*      */       
/*  251 */       !Tiles.isTree(type) && !Tiles.isBush(type) && type != Tiles.Tile.TILE_LAVA.id && type != Tiles.Tile.TILE_MARSH.id && 
/*  252 */       !Tiles.isMineDoor(type));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isBridgeableTile(byte type) {
/*  257 */     return (!Tiles.isTree(type) && !Tiles.isBush(type));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isCaveEntrance(byte type) {
/*  262 */     return (type == Tiles.Tile.TILE_HOLE.id || Tiles.isMineDoor(type));
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean isPackable(byte type) {
/*  267 */     return (!isRockTile(type) && !isImmutableTile(type) && type != Tiles.Tile.TILE_DIRT_PACKED.id && 
/*  268 */       !isRoad(type) && type != Tiles.Tile.TILE_SAND.id && !Tiles.isReinforcedFloor(type));
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isCornerDone(int x, int y, int preferredHeight) {
/*  273 */     if (Tiles.decodeHeight(flattenTiles[x][y]) == Tiles.decodeHeight(rockTiles[x][y]))
/*      */     {
/*      */       
/*  276 */       return true;
/*      */     }
/*  278 */     return (Tiles.decodeHeight(flattenTiles[x][y]) == preferredHeight);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean checkHouse(Creature performer, int tilex, int tiley, int xx, int yy, int preferredHeight) {
/*  284 */     for (int x = 0; x >= -1; x--) {
/*      */       
/*  286 */       for (int y = 0; y >= -1; y--) {
/*      */ 
/*      */ 
/*      */         
/*  290 */         if (!isCornerDone(xx + x, yy + y, preferredHeight)) {
/*      */ 
/*      */           
/*      */           try {
/*  294 */             Zone zone = Zones.getZone(tilex + x, tiley + y, performer.isOnSurface());
/*  295 */             VolaTile vtile = zone.getTileOrNull(tilex + x, tiley + y);
/*  296 */             if (vtile != null && vtile.getStructure() != null)
/*      */             {
/*  298 */               flattenImmutable++;
/*  299 */               performer.getCommunicator().sendNormalServerMessage("The structure is in the way.");
/*  300 */               return true;
/*      */             }
/*      */           
/*  303 */           } catch (NoSuchZoneException nsz) {
/*      */             
/*  305 */             flattenImmutable++;
/*  306 */             performer.getCommunicator().sendNormalServerMessage("The water is too deep to flatten.");
/*  307 */             return true;
/*      */           } 
/*      */         } else {
/*      */           
/*  311 */           logger.log(Level.INFO, "Corner at " + (xx + x) + "," + (yy + y) + " is ok already. Not checking");
/*      */         } 
/*      */       } 
/*  314 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean obliterateCave(Creature performer, Action act, Item source, int tilex, int tiley, int tile, float counter, int decimeterDug) {
/*  320 */     boolean done = false;
/*  321 */     boolean insta = (performer.getPower() >= 5);
/*  322 */     byte type = Tiles.decodeType(tile);
/*  323 */     int rockTile = Server.rockMesh.getTile(tilex, tiley);
/*  324 */     short rockHeight = Tiles.decodeHeight(rockTile);
/*  325 */     int caveTile = Server.caveMesh.getTile(tilex, tiley);
/*  326 */     short caveFloor = Tiles.decodeHeight(caveTile);
/*  327 */     short caveCeilingHeight = (short)(Tiles.decodeData(caveTile) & 0xFF);
/*  328 */     int dir = (int)(act.getTarget() >> 48L) & 0xFF;
/*  329 */     boolean obliteratingCeiling = (type == Tiles.Tile.TILE_CAVE.id && dir == 1);
/*      */ 
/*      */     
/*  332 */     if (caveCeilingHeight + decimeterDug > 254) {
/*      */       
/*  334 */       performer.getCommunicator()
/*  335 */         .sendNormalServerMessage("The " + source.getName() + " vibrates, but nothing happens. 1");
/*  336 */       return true;
/*      */     } 
/*  338 */     if (obliteratingCeiling) {
/*      */ 
/*      */       
/*  341 */       if (caveFloor + caveCeilingHeight + decimeterDug >= rockHeight)
/*      */       {
/*  343 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/*  344 */             .getName() + " vibrates, but nothing happens. 2");
/*  345 */         return true;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  350 */       if (caveFloor - decimeterDug < -150) {
/*      */         
/*  352 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/*  353 */             .getName() + " vibrates, but nothing happens.");
/*  354 */         return true;
/*      */       } 
/*      */       
/*  357 */       if (caveFloor == rockHeight) {
/*      */         
/*  359 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/*  360 */             .getName() + " vibrates, but nothing happens.");
/*  361 */         return true;
/*      */       } 
/*      */     } 
/*  364 */     done = false;
/*  365 */     boolean abort = false;
/*  366 */     if (source.getQualityLevel() < (decimeterDug + 1))
/*  367 */       abort = true; 
/*  368 */     int lNewTile = Server.caveMesh.getTile(tilex - 1, tiley);
/*  369 */     if (checkSculptCaveTile(lNewTile, performer, caveFloor, caveCeilingHeight, decimeterDug, obliteratingCeiling))
/*  370 */       abort = true; 
/*  371 */     lNewTile = Server.caveMesh.getTile(tilex + 1, tiley);
/*  372 */     if (checkSculptCaveTile(lNewTile, performer, caveFloor, caveCeilingHeight, decimeterDug, obliteratingCeiling))
/*  373 */       abort = true; 
/*  374 */     lNewTile = Server.caveMesh.getTile(tilex, tiley - 1);
/*  375 */     if (checkSculptCaveTile(lNewTile, performer, caveFloor, caveCeilingHeight, decimeterDug, obliteratingCeiling)) {
/*  376 */       abort = true;
/*      */     }
/*  378 */     lNewTile = Server.caveMesh.getTile(tilex, tiley + 1);
/*  379 */     if (checkSculptCaveTile(lNewTile, performer, caveFloor, caveCeilingHeight, decimeterDug, obliteratingCeiling)) {
/*  380 */       abort = true;
/*      */     }
/*  382 */     if (abort) {
/*      */       
/*  384 */       performer.getCommunicator()
/*  385 */         .sendNormalServerMessage("The " + source.getName() + " vibrates, but nothing happens. 3");
/*  386 */       return true;
/*      */     } 
/*  388 */     int time = 30;
/*  389 */     for (int x = 0; x >= -1; x--) {
/*      */       
/*  391 */       for (int y = 0; y >= -1; y--) {
/*      */ 
/*      */         
/*      */         try {
/*  395 */           Zone zone = Zones.getZone(tilex + x, tiley + y, false);
/*  396 */           VolaTile vtile = zone.getTileOrNull(tilex + x, tiley + y);
/*  397 */           if (vtile != null) {
/*      */             
/*  399 */             if (vtile.getStructure() != null) {
/*      */               
/*  401 */               performer.getCommunicator().sendNormalServerMessage("The structure is in the way.");
/*  402 */               return true;
/*      */             } 
/*  404 */             if (x == 0 && y == 0) {
/*      */               
/*  406 */               Fence[] arrayOfFence = vtile.getFences(); int i = arrayOfFence.length; byte b = 0; if (b < i) { Fence fence = arrayOfFence[b];
/*      */                 
/*  408 */                 performer.getCommunicator().sendNormalServerMessage("The " + fence
/*  409 */                     .getName() + " is in the way.");
/*  410 */                 return true; }
/*      */ 
/*      */             
/*  413 */             } else if (x == -1 && y == 0) {
/*      */               
/*  415 */               for (Fence fence : vtile.getFences()) {
/*      */                 
/*  417 */                 if (fence.isHorizontal())
/*      */                 {
/*  419 */                   performer.getCommunicator().sendNormalServerMessage("The " + fence
/*  420 */                       .getName() + " is in the way.");
/*  421 */                   return true;
/*      */                 }
/*      */               
/*      */               } 
/*  425 */             } else if (y == -1 && x == 0) {
/*      */               
/*  427 */               for (Fence fence : vtile.getFences()) {
/*      */                 
/*  429 */                 if (!fence.isHorizontal())
/*      */                 {
/*  431 */                   performer.getCommunicator().sendNormalServerMessage("The " + fence
/*  432 */                       .getName() + " is in the way.");
/*  433 */                   return true;
/*      */                 }
/*      */               
/*      */               } 
/*      */             } 
/*      */           } 
/*  439 */         } catch (NoSuchZoneException nsz) {
/*      */           
/*  441 */           performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*  442 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*  446 */     if (counter == 1.0F && !insta) {
/*      */       
/*  448 */       act.setTimeLeft(30);
/*  449 */       performer.getCommunicator().sendNormalServerMessage("You use the " + source.getName() + ".");
/*  450 */       Server.getInstance()
/*  451 */         .broadCastAction(performer.getName() + " uses " + source.getNameWithGenus() + ".", performer, 5);
/*      */       
/*  453 */       performer.sendActionControl(Actions.actionEntrys[118].getVerbString(), true, 30);
/*      */     } 
/*  455 */     if (counter * 10.0F > 30.0F || insta) {
/*      */       
/*  457 */       done = true;
/*  458 */       int newCeil = Math.min(255, caveCeilingHeight + decimeterDug);
/*      */       
/*  460 */       if (newCeil != 255 && !insta) {
/*  461 */         source.setQualityLevel(source.getQualityLevel() - decimeterDug);
/*      */       }
/*  463 */       if (obliteratingCeiling) {
/*      */         
/*  465 */         Server.caveMesh.setTile(tilex, tiley, Tiles.encode(caveFloor, type, (byte)newCeil));
/*      */       }
/*      */       else {
/*      */         
/*  469 */         Server.caveMesh.setTile(tilex, tiley, Tiles.encode((short)(caveFloor - decimeterDug), type, (byte)newCeil));
/*      */       } 
/*  471 */       Players.getInstance().sendChangedTile(tilex, tiley, false, true);
/*  472 */       for (int i = -1; i <= 0; i++) {
/*      */         
/*  474 */         for (int y = -1; y <= 0; y++) {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/*  479 */             Zone toCheckForChange = Zones.getZone(tilex + i, tiley + y, false);
/*  480 */             toCheckForChange.changeTile(tilex + i, tiley + y);
/*      */           }
/*  482 */           catch (NoSuchZoneException nsz) {
/*      */             
/*  484 */             logger.log(Level.INFO, "no such zone?: " + (tilex + i) + "," + (tiley + y), (Throwable)nsz);
/*  485 */             performer.getCommunicator().sendNormalServerMessage("You can't mine there.");
/*  486 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/*  490 */       performer.getCommunicator().sendNormalServerMessage("You obliterate some rock.");
/*      */     } 
/*  492 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean obliterate(Creature performer, Action act, Item source, int tilex, int tiley, int tile, float counter, int decimeterDug, MeshIO mesh) {
/*  498 */     boolean done = false;
/*      */     
/*  500 */     boolean insta = (performer.getPower() >= 5);
/*  501 */     if (source.getAuxData() <= 0 || source.getQualityLevel() > 99.0F) {
/*      */       
/*  503 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " vibrates strongly!");
/*  504 */       source.setAuxData((byte)1);
/*  505 */       source.setQualityLevel(Math.min(source.getQualityLevel(), 60.0F));
/*  506 */       return true;
/*      */     } 
/*  508 */     if (tilex < 1 || tilex > (1 << Constants.meshSize) - 2 || tiley < 1 || tiley > (1 << Constants.meshSize) - 2) {
/*      */       
/*  510 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep there.");
/*  511 */       return true;
/*      */     } 
/*  513 */     byte type = Tiles.decodeType(tile);
/*  514 */     if (Tiles.isSolidCave(type) || type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*  515 */       return obliterateCave(performer, act, source, tilex, tiley, tile, counter, decimeterDug);
/*      */     }
/*  517 */     if (Math.abs(getMaxSurfaceDifference(Server.surfaceMesh.getTile(tilex, tiley), tilex, tiley)) > 60) {
/*      */       
/*  519 */       performer.getCommunicator().sendNormalServerMessage("That is too steep. Nothing happens.");
/*  520 */       return true;
/*      */     } 
/*  522 */     short tileHeight = Tiles.decodeHeight(tile);
/*  523 */     int rockTile = Server.rockMesh.getTile(tilex, tiley);
/*  524 */     short rockHeight = Tiles.decodeHeight(rockTile);
/*  525 */     int caveTile = Server.caveMesh.getTile(tilex, tiley);
/*  526 */     short caveFloor = Tiles.decodeHeight(caveTile);
/*  527 */     int caveCeilingHeight = caveFloor + (short)(Tiles.decodeData(caveTile) & 0xFF);
/*      */     
/*  529 */     short minHeight = -5000;
/*  530 */     if (tileHeight - decimeterDug > -5000) {
/*      */       
/*  532 */       if (tileHeight - decimeterDug <= caveCeilingHeight) {
/*      */         
/*  534 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*  535 */         return true;
/*      */       } 
/*  537 */       done = false;
/*  538 */       if (!insta)
/*      */       {
/*  540 */         if (source.getQualityLevel() < (decimeterDug + 1)) {
/*      */           
/*  542 */           performer.getCommunicator().sendNormalServerMessage("The " + source
/*  543 */               .getName() + " vibrates, but nothing happens.");
/*  544 */           return true;
/*      */         } 
/*      */       }
/*  547 */       if (!isSculptable(Tiles.decodeType(tile))) {
/*      */         
/*  549 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens on the " + 
/*  550 */             (Tiles.getTile(Tiles.decodeType(tile))).tiledesc + ". The " + source
/*  551 */             .getName() + " only seems to work on grass, rock, dirt and similar terrain.");
/*      */         
/*  553 */         return true;
/*      */       } 
/*  555 */       int lNewTile = mesh.getTile(tilex - 1, tiley);
/*  556 */       if (checkSculptTile(lNewTile, performer, tileHeight, decimeterDug))
/*  557 */         return true; 
/*  558 */       lNewTile = mesh.getTile(tilex + 1, tiley);
/*  559 */       if (checkSculptTile(lNewTile, performer, tileHeight, decimeterDug))
/*  560 */         return true; 
/*  561 */       lNewTile = mesh.getTile(tilex, tiley - 1);
/*  562 */       if (checkSculptTile(lNewTile, performer, tileHeight, decimeterDug))
/*  563 */         return true; 
/*  564 */       lNewTile = mesh.getTile(tilex, tiley + 1);
/*  565 */       if (checkSculptTile(lNewTile, performer, tileHeight, decimeterDug)) {
/*  566 */         return true;
/*      */       }
/*  568 */       int time = 30;
/*  569 */       for (int x = 0; x >= -1; x--) {
/*      */         
/*  571 */         for (int y = 0; y >= -1; y--) {
/*      */ 
/*      */           
/*      */           try {
/*  575 */             Zone zone = Zones.getZone(tilex + x, tiley + y, performer.isOnSurface());
/*  576 */             VolaTile vtile = zone.getTileOrNull(tilex + x, tiley + y);
/*  577 */             if (vtile != null) {
/*      */               
/*  579 */               if (vtile.getStructure() != null) {
/*      */                 
/*  581 */                 performer.getCommunicator().sendNormalServerMessage("The structure is in the way.");
/*  582 */                 return true;
/*      */               } 
/*      */               
/*  585 */               if (x == 0 && y == 0) {
/*      */                 
/*  587 */                 Fence[] arrayOfFence = vtile.getFences(); int i = arrayOfFence.length; byte b = 0; if (b < i) { Fence fence = arrayOfFence[b];
/*      */                   
/*  589 */                   performer.getCommunicator().sendNormalServerMessage("The " + fence
/*  590 */                       .getName() + " is in the way.");
/*  591 */                   return true; }
/*      */ 
/*      */               
/*  594 */               } else if (x == -1 && y == 0) {
/*      */                 
/*  596 */                 for (Fence fence : vtile.getFences()) {
/*  597 */                   if (fence.isHorizontal()) {
/*      */                     
/*  599 */                     performer.getCommunicator().sendNormalServerMessage("The " + fence
/*  600 */                         .getName() + " is in the way.");
/*  601 */                     return true;
/*      */                   } 
/*      */                 } 
/*  604 */               } else if (y == -1 && x == 0) {
/*      */                 
/*  606 */                 for (Fence fence : vtile.getFences()) {
/*  607 */                   if (!fence.isHorizontal()) {
/*      */                     
/*  609 */                     performer.getCommunicator().sendNormalServerMessage("The " + fence
/*  610 */                         .getName() + " is in the way.");
/*  611 */                     return true;
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*  616 */           } catch (NoSuchZoneException nsz) {
/*      */             
/*  618 */             performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*  619 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/*  623 */       if (counter == 1.0F && !insta) {
/*      */         
/*  625 */         act.setTimeLeft(30);
/*  626 */         performer.getCommunicator().sendNormalServerMessage("You use the " + source.getName() + ".");
/*  627 */         Server.getInstance().broadCastAction(performer.getName() + " uses " + source.getNameWithGenus() + ".", performer, 5);
/*      */ 
/*      */         
/*  630 */         performer.sendActionControl(Actions.actionEntrys[118].getVerbString(), true, 30);
/*      */       } 
/*  632 */       if (counter * 10.0F > 30.0F || insta)
/*      */       {
/*  634 */         done = true;
/*      */ 
/*      */ 
/*      */         
/*  638 */         int clayNum = Server.getDigCount(tilex, tiley);
/*  639 */         if (clayNum <= 0 || clayNum > 100)
/*  640 */           clayNum = 50 + Server.rand.nextInt(50); 
/*  641 */         boolean allCornersRock = false;
/*  642 */         for (int i = 0; i >= -1; i--) {
/*  643 */           for (int y = 0; y >= -1; y--) {
/*      */             
/*  645 */             boolean lChanged = false;
/*  646 */             lNewTile = mesh.getTile(tilex + i, tiley + y);
/*  647 */             type = Tiles.decodeType(lNewTile);
/*      */             
/*  649 */             short newTileHeight = Tiles.decodeHeight(lNewTile);
/*  650 */             rockTile = Server.rockMesh.getTile(tilex + i, tiley + y);
/*  651 */             rockHeight = Tiles.decodeHeight(rockTile);
/*  652 */             if (i == 0 && y == 0) {
/*      */               
/*  654 */               lChanged = true;
/*  655 */               newTileHeight = (short)(newTileHeight - decimeterDug);
/*      */ 
/*      */ 
/*      */               
/*  659 */               mesh.setTile(tilex + i, tiley + y, Tiles.encode(newTileHeight, type, Tiles.decodeData(lNewTile)));
/*  660 */               if (newTileHeight < rockHeight) {
/*      */                 
/*  662 */                 Server.rockMesh.setTile(tilex + i, tiley + y, Tiles.encode(newTileHeight, (short)0));
/*  663 */                 rockHeight = newTileHeight;
/*      */               } 
/*  665 */               if (insta) {
/*  666 */                 performer.getCommunicator().sendNormalServerMessage("Tile " + (tilex + i) + ", " + (tiley + y) + " now at " + newTileHeight + ", rock at " + rockHeight + ".");
/*      */               }
/*      */             } 
/*      */             
/*  670 */             allCornersRock = allCornersAtRockLevel(tilex + i, tiley + y, mesh);
/*  671 */             if (!isImmutableTile(type) && allCornersRock) {
/*      */               
/*  673 */               lChanged = true;
/*  674 */               Server.modifyFlagsByTileType(tilex + i, tiley + y, Tiles.Tile.TILE_ROCK.id);
/*  675 */               mesh.setTile(tilex + i, tiley + y, Tiles.encode(newTileHeight, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */             }
/*  677 */             else if (isTileTurnToDirt(type)) {
/*      */               
/*  679 */               lChanged = true;
/*  680 */               Server.modifyFlagsByTileType(tilex + i, tiley + y, Tiles.Tile.TILE_DIRT.id);
/*  681 */               mesh.setTile(tilex + i, tiley + y, Tiles.encode(newTileHeight, Tiles.Tile.TILE_DIRT.id, (byte)0));
/*      */             } 
/*  683 */             if (lChanged) {
/*      */               
/*  685 */               if (i == 0 && y == 0) {
/*  686 */                 source.setQualityLevel(source.getQualityLevel() - decimeterDug);
/*      */               }
/*  688 */               performer.getMovementScheme().touchFreeMoveCounter();
/*  689 */               Players.getInstance().sendChangedTile(tilex + i, tiley + y, performer.isOnSurface(), true);
/*      */ 
/*      */               
/*      */               try {
/*  693 */                 Zone toCheckForChange = Zones.getZone(tilex + i, tiley + y, performer.isOnSurface());
/*  694 */                 toCheckForChange.changeTile(tilex + i, tiley + y);
/*      */               }
/*  696 */               catch (NoSuchZoneException nsz) {
/*      */                 
/*  698 */                 logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */               } 
/*      */             } 
/*  701 */             if (performer.isOnSurface()) {
/*      */               
/*  703 */               Item[] foundArtifacts = EndGameItems.getArtifactDugUp(tilex + i, tiley + y, newTileHeight / 10.0F, allCornersRock);
/*      */               
/*  705 */               if (foundArtifacts.length > 0)
/*      */               {
/*  707 */                 for (int aa = 0; aa < foundArtifacts.length; aa++) {
/*      */                   
/*  709 */                   VolaTile atile = Zones.getOrCreateTile(tilex + i, tiley + y, performer.isOnSurface());
/*  710 */                   atile.addItem(foundArtifacts[aa], false, false);
/*  711 */                   performer.getCommunicator().sendNormalServerMessage("You find something weird! You found the " + foundArtifacts[aa]
/*  712 */                       .getName() + "!");
/*  713 */                   logger.log(Level.INFO, performer.getName() + " found the " + foundArtifacts[aa].getName() + " at tile " + (tilex + i) + ", " + (tiley + y) + "! " + foundArtifacts[aa]);
/*      */                   
/*  715 */                   HistoryManager.addHistory(performer.getName(), "reveals the " + foundArtifacts[aa]
/*  716 */                       .getName());
/*  717 */                   EndGameItem egi = EndGameItems.getEndGameItem(foundArtifacts[aa]);
/*  718 */                   if (egi != null) {
/*      */                     
/*  720 */                     egi.setLastMoved(System.currentTimeMillis());
/*  721 */                     foundArtifacts[aa].setAuxData((byte)120);
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */             } 
/*      */             
/*  727 */             Item[] foundItems = Items.getHiddenItemsAt(tilex + i, tiley + y, newTileHeight / 10.0F, true);
/*  728 */             if (foundItems.length > 0)
/*      */             {
/*  730 */               for (int aa = 0; aa < foundItems.length; aa++) {
/*      */                 
/*  732 */                 foundItems[aa].setHidden(false);
/*  733 */                 Items.revealItem(foundItems[aa]);
/*  734 */                 VolaTile atile = Zones.getOrCreateTile(tilex + i, tiley + y, performer.isOnSurface());
/*  735 */                 atile.addItem(foundItems[aa], false, false);
/*  736 */                 performer.getCommunicator().sendNormalServerMessage("You find something! You found a " + foundItems[aa]
/*  737 */                     .getName() + "!");
/*  738 */                 logger.log(Level.INFO, performer.getName() + " found a " + foundItems[aa].getName() + " at tile " + (tilex + i) + ", " + (tiley + y) + ".");
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*  743 */         performer.getCommunicator().sendNormalServerMessage("You obliterate some matter.");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  748 */       done = true;
/*  749 */       performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*      */     } 
/*  751 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getCaveDoorDifference(int digTile, int digTilex, int digTiley) {
/*  756 */     short difference = 0;
/*  757 */     short maxdifference = 0;
/*  758 */     short digTileHeight = Tiles.decodeHeight(digTile);
/*      */     
/*  760 */     int lNewTile = Server.surfaceMesh.getTile(digTilex, digTiley + 1);
/*      */     
/*  762 */     short height = Tiles.decodeHeight(lNewTile);
/*  763 */     difference = (short)Math.abs(height - digTileHeight);
/*  764 */     if (difference > maxdifference) {
/*  765 */       maxdifference = difference;
/*      */     }
/*      */     
/*  768 */     lNewTile = Server.surfaceMesh.getTile(digTilex + 1, digTiley);
/*  769 */     digTileHeight = Tiles.decodeHeight(lNewTile);
/*      */     
/*  771 */     lNewTile = Server.surfaceMesh.getTile(digTilex + 1, digTiley + 1);
/*  772 */     height = Tiles.decodeHeight(lNewTile);
/*  773 */     difference = (short)Math.abs(height - digTileHeight);
/*  774 */     if (difference > maxdifference) {
/*  775 */       maxdifference = difference;
/*      */     }
/*  777 */     return maxdifference;
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
/*      */   public static final int getMaxSurfaceDifference(int digTile, int digTilex, int digTiley) {
/*  790 */     short difference = 0;
/*  791 */     short maxdifference = 0;
/*  792 */     short digTileHeight = Tiles.decodeHeight(digTile);
/*      */     
/*  794 */     int lNewTile = Server.surfaceMesh.getTile(digTilex - 1, digTiley);
/*  795 */     short height = Tiles.decodeHeight(lNewTile);
/*  796 */     difference = (short)(height - digTileHeight);
/*  797 */     if (Math.abs(difference) > Math.abs(maxdifference)) {
/*  798 */       maxdifference = difference;
/*      */     }
/*  800 */     lNewTile = Server.surfaceMesh.getTile(digTilex, digTiley + 1);
/*  801 */     height = Tiles.decodeHeight(lNewTile);
/*  802 */     difference = (short)(height - digTileHeight);
/*  803 */     if (Math.abs(difference) > Math.abs(maxdifference)) {
/*  804 */       maxdifference = difference;
/*      */     }
/*  806 */     lNewTile = Server.surfaceMesh.getTile(digTilex, digTiley - 1);
/*  807 */     height = Tiles.decodeHeight(lNewTile);
/*  808 */     difference = (short)(height - digTileHeight);
/*  809 */     if (Math.abs(difference) > Math.abs(maxdifference)) {
/*  810 */       maxdifference = difference;
/*      */     }
/*  812 */     lNewTile = Server.surfaceMesh.getTile(digTilex + 1, digTiley);
/*  813 */     height = Tiles.decodeHeight(lNewTile);
/*  814 */     difference = (short)(height - digTileHeight);
/*  815 */     if (Math.abs(difference) > Math.abs(maxdifference))
/*  816 */       maxdifference = difference; 
/*  817 */     return maxdifference;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getMaxSurfaceDownSlope(int digTilex, int digTiley) {
/*  822 */     int digTile = Server.surfaceMesh.getTile(digTilex, digTiley);
/*  823 */     short difference = 0;
/*  824 */     short maxdifference = 0;
/*  825 */     short digTileHeight = Tiles.decodeHeight(digTile);
/*      */     
/*  827 */     int lNewTile = Server.surfaceMesh.getTile(digTilex - 1, digTiley);
/*  828 */     short height = Tiles.decodeHeight(lNewTile);
/*  829 */     difference = (short)(height - digTileHeight);
/*  830 */     if (difference < maxdifference) {
/*  831 */       maxdifference = difference;
/*      */     }
/*  833 */     lNewTile = Server.surfaceMesh.getTile(digTilex, digTiley + 1);
/*  834 */     height = Tiles.decodeHeight(lNewTile);
/*  835 */     difference = (short)(height - digTileHeight);
/*  836 */     if (difference < maxdifference) {
/*  837 */       maxdifference = difference;
/*      */     }
/*  839 */     lNewTile = Server.surfaceMesh.getTile(digTilex, digTiley - 1);
/*  840 */     height = Tiles.decodeHeight(lNewTile);
/*  841 */     difference = (short)(height - digTileHeight);
/*  842 */     if (difference < maxdifference) {
/*  843 */       maxdifference = difference;
/*      */     }
/*  845 */     lNewTile = Server.surfaceMesh.getTile(digTilex + 1, digTiley);
/*  846 */     height = Tiles.decodeHeight(lNewTile);
/*  847 */     difference = (short)(height - digTileHeight);
/*  848 */     if (difference < maxdifference)
/*  849 */       maxdifference = difference; 
/*  850 */     return maxdifference;
/*      */   }
/*      */   
/*      */   public static int getTileResource(byte type) {
/*  854 */     int templateId = 26;
/*  855 */     if (type == Tiles.Tile.TILE_CLAY.id) {
/*  856 */       templateId = 130;
/*  857 */     } else if (type == Tiles.Tile.TILE_SAND.id) {
/*  858 */       templateId = 298;
/*  859 */     } else if (type == Tiles.Tile.TILE_PEAT.id) {
/*  860 */       templateId = 467;
/*  861 */     } else if (type == Tiles.Tile.TILE_TAR.id) {
/*  862 */       templateId = 153;
/*  863 */     } else if (type == Tiles.Tile.TILE_MOSS.id) {
/*  864 */       templateId = 479;
/*  865 */     }  return templateId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean dig(Creature performer, Item source, int tilex, int tiley, int tile, float counter, boolean corner, MeshIO mesh) {
/*  871 */     return dig(performer, source, tilex, tiley, tile, counter, corner, mesh, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean dig(Creature performer, Item source, int tilex, int tiley, int tile, float counter, boolean corner, MeshIO mesh, boolean toPile) {
/*  877 */     boolean done = false;
/*  878 */     boolean dredging = false;
/*  879 */     boolean digToPile = (toPile && source != null && (!source.isDredgingTool() || source.isWand()));
/*      */     
/*      */     try {
/*      */       int digTilex, digTiley;
/*  883 */       boolean insta = (source.isWand() && performer.getPower() >= 2);
/*      */       
/*  885 */       if (source.isDredgingTool())
/*      */       {
/*  887 */         if (source.getTemplateId() != 176 || performer.getPositionZ() < 0.0F) {
/*  888 */           dredging = true;
/*      */         }
/*      */       }
/*      */       
/*  892 */       if (corner) {
/*      */         
/*  894 */         digTilex = tilex;
/*  895 */         digTiley = tiley;
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  900 */         Vector2f pos = performer.getPos2f();
/*  901 */         digTilex = CoordUtils.WorldToTile(pos.x + 2.0F);
/*  902 */         digTiley = CoordUtils.WorldToTile(pos.y + 2.0F);
/*      */       } 
/*  904 */       if (digTilex < 0 || digTilex > 1 << Constants.meshSize || digTiley < 0 || digTiley > 1 << Constants.meshSize) {
/*      */         
/*  906 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to dig.");
/*  907 */         return true;
/*      */       } 
/*  909 */       if (Features.Feature.WAGONER.isEnabled())
/*      */       {
/*      */         
/*  912 */         if (MethodsHighways.onWagonerCamp(digTilex, digTiley, true)) {
/*      */           
/*  914 */           performer.getCommunicator().sendNormalServerMessage("The wagoner whips you once and tells you never to try digging here again.");
/*  915 */           return true;
/*      */         } 
/*      */       }
/*  918 */       int digTile = mesh.getTile(digTilex, digTiley);
/*  919 */       byte type = Tiles.decodeType(digTile);
/*  920 */       int templateId = getTileResource(type);
/*      */       
/*  922 */       int currentTileHeight = Tiles.decodeHeight(digTile);
/*  923 */       int currentTileRock = Server.rockMesh.getTile(digTilex, digTiley);
/*  924 */       int currentRockHeight = Tiles.decodeHeight(currentTileRock);
/*      */ 
/*      */       
/*  927 */       if (currentTileHeight <= currentRockHeight) {
/*      */         
/*  929 */         performer.getCommunicator().sendNormalServerMessage("You can not dig in the solid rock.");
/*      */         
/*  931 */         HighwayPos highwayPos = MethodsHighways.getHighwayPos(digTilex, digTiley, true);
/*  932 */         if (!MethodsHighways.onHighway(highwayPos))
/*      */         {
/*      */ 
/*      */           
/*  936 */           for (int x = 0; x >= -1; x--) {
/*      */             
/*  938 */             for (int y = 0; y >= -1; y--) {
/*      */               
/*  940 */               int theTile = mesh.getTile(digTilex + x, digTiley + y);
/*  941 */               byte theType = Tiles.decodeType(theTile);
/*  942 */               boolean allCornersRock = allCornersAtRockLevel(digTilex + x, digTiley + y, mesh);
/*  943 */               if (!isRockTile(theType) && !isImmutableTile(theType) && allCornersRock && 
/*  944 */                 !Tiles.isTree(type) && !Tiles.isBush(type)) {
/*      */                 
/*  946 */                 float oldTileHeight = Tiles.decodeHeightAsFloat(theTile);
/*  947 */                 Server.modifyFlagsByTileType(digTilex + x, digTiley + y, Tiles.Tile.TILE_ROCK.id);
/*  948 */                 mesh.setTile(digTilex + x, digTiley + y, 
/*  949 */                     Tiles.encode(oldTileHeight, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  950 */                 Players.getInstance().sendChangedTile(digTilex + x, digTiley + y, performer.isOnSurface(), true);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*  955 */         return true;
/*      */       } 
/*      */       
/*  958 */       Village village = null;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  963 */       int encodedTile = Server.surfaceMesh.getTile(digTilex, digTiley);
/*  964 */       village = Zones.getVillage(digTilex, digTiley, performer.isOnSurface());
/*  965 */       int checkX = digTilex;
/*  966 */       int checkY = digTiley;
/*      */       
/*  968 */       if (village == null) {
/*      */ 
/*      */         
/*  971 */         checkX = (int)performer.getStatus().getPositionX() - 2 >> 2;
/*  972 */         village = Zones.getVillage(checkX, checkY, performer.isOnSurface());
/*      */       } 
/*  974 */       if (village == null) {
/*      */ 
/*      */         
/*  977 */         checkY = (int)performer.getStatus().getPositionY() - 2 >> 2;
/*  978 */         village = Zones.getVillage(checkX, checkY, performer.isOnSurface());
/*      */       } 
/*  980 */       if (village == null) {
/*      */ 
/*      */         
/*  983 */         checkX = (int)performer.getStatus().getPositionX() + 2 >> 2;
/*  984 */         village = Zones.getVillage(checkX, checkY, performer.isOnSurface());
/*      */       } 
/*  986 */       if (village != null)
/*      */       {
/*      */         
/*  989 */         if (!village.isActionAllowed((short)144, performer, false, encodedTile, 0)) {
/*      */           
/*  991 */           if (!Zones.isOnPvPServer(tilex, tiley)) {
/*      */             
/*  993 */             performer.getCommunicator().sendNormalServerMessage("This action is not allowed here, because the tile is on a player owned deed that has disallowed it.", (byte)3);
/*  994 */             return true;
/*      */           } 
/*  996 */           if (!village.isEnemy(performer))
/*      */           {
/*  998 */             if (performer.isLegal()) {
/*      */               
/* 1000 */               performer.getCommunicator().sendNormalServerMessage("That would be illegal here. You can check the settlement token for the local laws.", (byte)3);
/* 1001 */               return true;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/* 1006 */       int weight = ItemTemplateFactory.getInstance().getTemplate(templateId).getWeightGrams();
/* 1007 */       if (!insta) {
/*      */         
/* 1009 */         if (performer.getInventory().getNumItemsNotCoins() >= 100) {
/*      */           
/* 1011 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the " + 
/* 1012 */               ItemTemplateFactory.getInstance().getTemplate(templateId).getName() + ". You need to drop some things first.");
/*      */           
/* 1014 */           return true;
/*      */         } 
/* 1016 */         if (!performer.canCarry(weight)) {
/*      */           
/* 1018 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the " + 
/* 1019 */               ItemTemplateFactory.getInstance().getTemplate(templateId).getName() + ". You need to drop some things first.");
/*      */           
/* 1021 */           return true;
/*      */         } 
/* 1023 */         if (dredging && source.getFreeVolume() < 1000) {
/*      */           
/* 1025 */           performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " is full.");
/* 1026 */           return true;
/*      */         } 
/*      */       } 
/* 1029 */       short digTileHeight = Tiles.decodeHeight(digTile);
/* 1030 */       int rockTile = Server.rockMesh.getTile(digTilex, digTiley);
/* 1031 */       short rockHeight = Tiles.decodeHeight(rockTile);
/* 1032 */       short h = Tiles.decodeHeight(digTile);
/* 1033 */       short minHeight = -7;
/* 1034 */       short maxHeight = 20000;
/* 1035 */       Skills skills = performer.getSkills();
/* 1036 */       Skill digging = null;
/*      */       
/*      */       try {
/* 1039 */         digging = skills.getSkill(1009);
/*      */       }
/* 1041 */       catch (Exception ex) {
/*      */         
/* 1043 */         digging = skills.learn(1009, 0.0F);
/*      */       } 
/* 1045 */       if (insta) {
/*      */ 
/*      */         
/* 1048 */         minHeight = -300;
/*      */       }
/* 1050 */       else if (dredging) {
/*      */         
/* 1052 */         maxHeight = -1;
/* 1053 */         minHeight = (short)(int)-Math.max(3.0D, digging.getKnowledge(source, 0.0D) * 3.0D);
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
/* 1067 */       if (h > minHeight && h < maxHeight) {
/*      */         
/* 1069 */         done = false;
/* 1070 */         Skill shovel = null;
/*      */         
/* 1072 */         double power = 0.0D;
/* 1073 */         if (!insta) {
/*      */           
/*      */           try {
/*      */             
/* 1077 */             shovel = skills.getSkill(source.getPrimarySkill());
/*      */           }
/* 1079 */           catch (Exception ex) {
/*      */ 
/*      */             
/*      */             try {
/* 1083 */               shovel = skills.learn(source.getPrimarySkill(), 1.0F);
/*      */             }
/* 1085 */             catch (NoSuchSkillException nse) {
/*      */               
/* 1087 */               if (performer.getPower() <= 0) {
/* 1088 */                 logger.log(Level.WARNING, performer.getName() + " trying to dig with an item with no primary skill: " + source
/* 1089 */                     .getName());
/*      */               }
/*      */             } 
/*      */           } 
/*      */         }
/* 1094 */         short maxdifference = 0;
/* 1095 */         if (!insta) {
/*      */           
/* 1097 */           if (checkIfTerraformingOnPermaObject(digTilex, digTiley)) {
/*      */             
/* 1099 */             performer.getCommunicator().sendNormalServerMessage("The object nearby prevents digging further down.");
/* 1100 */             return true;
/*      */           } 
/* 1102 */           if (Zones.isTileCornerProtected(digTilex, digTiley)) {
/*      */             
/* 1104 */             performer.getCommunicator().sendNormalServerMessage("Your shovel fails to penetrate the earth no matter what you try. Weird.");
/*      */             
/* 1106 */             return true;
/*      */           } 
/* 1108 */           if (isTileModBlocked(performer, digTilex, digTiley, true))
/*      */           {
/* 1110 */             return true;
/*      */           }
/* 1112 */           if ((Features.Feature.HIGHWAYS.isEnabled() || digTileHeight > 0) && wouldDestroyCobble(performer, digTilex, digTiley, false)) {
/*      */             
/* 1114 */             if (Features.Feature.HIGHWAYS.isEnabled()) {
/* 1115 */               performer.getCommunicator().sendNormalServerMessage("The highway would be too steep to traverse.");
/*      */             } else {
/* 1117 */               performer.getCommunicator().sendNormalServerMessage("The road would be too steep to traverse.");
/* 1118 */             }  return true;
/*      */           } 
/* 1120 */           if (nextToTundra(mesh, digTilex, digTiley)) {
/*      */             
/* 1122 */             performer.getCommunicator().sendNormalServerMessage("The frozen soil is too hard to dig effectively.");
/* 1123 */             return true;
/*      */           } 
/* 1125 */           if (countNonDiggables(mesh, digTilex, digTiley) >= 3) {
/*      */             
/* 1127 */             performer.getCommunicator().sendNormalServerMessage("You cannot dig in such terrain.");
/* 1128 */             return true;
/*      */           } 
/*      */           
/* 1131 */           int lNewTile = mesh.getTile(digTilex, digTiley - 1);
/* 1132 */           short height = Tiles.decodeHeight(lNewTile);
/* 1133 */           short difference = (short)Math.abs(height - digTileHeight);
/* 1134 */           if (difference > maxdifference)
/* 1135 */             maxdifference = difference; 
/* 1136 */           if (checkDigTile(lNewTile, performer, digging, digTileHeight, difference)) {
/* 1137 */             return true;
/*      */           }
/* 1139 */           lNewTile = mesh.getTile(digTilex + 1, digTiley);
/* 1140 */           height = Tiles.decodeHeight(lNewTile);
/* 1141 */           difference = (short)Math.abs(height - digTileHeight);
/* 1142 */           if (difference > maxdifference)
/* 1143 */             maxdifference = difference; 
/* 1144 */           if (checkDigTile(lNewTile, performer, digging, digTileHeight, difference)) {
/* 1145 */             return true;
/*      */           }
/* 1147 */           lNewTile = mesh.getTile(digTilex, digTiley + 1);
/* 1148 */           height = Tiles.decodeHeight(lNewTile);
/* 1149 */           difference = (short)Math.abs(height - digTileHeight);
/* 1150 */           if (difference > maxdifference)
/* 1151 */             maxdifference = difference; 
/* 1152 */           if (checkDigTile(lNewTile, performer, digging, digTileHeight, difference)) {
/* 1153 */             return true;
/*      */           }
/* 1155 */           lNewTile = mesh.getTile(digTilex - 1, digTiley);
/* 1156 */           height = Tiles.decodeHeight(lNewTile);
/* 1157 */           difference = (short)Math.abs(height - digTileHeight);
/* 1158 */           if (difference > maxdifference)
/* 1159 */             maxdifference = difference; 
/* 1160 */           if (checkDigTile(lNewTile, performer, digging, digTileHeight, difference))
/* 1161 */             return true; 
/*      */         } 
/* 1163 */         Action act = null;
/*      */         
/*      */         try {
/* 1166 */           act = performer.getCurrentAction();
/*      */         }
/* 1168 */         catch (NoSuchActionException nsa) {
/*      */           
/* 1170 */           logger.log(Level.WARNING, "Weird: " + nsa.getMessage(), (Throwable)nsa);
/* 1171 */           return true;
/*      */         } 
/* 1173 */         int time = 1000;
/* 1174 */         for (int x = 0; x >= -1; x--) {
/*      */           
/* 1176 */           for (int y = 0; y >= -1; y--) {
/*      */ 
/*      */             
/*      */             try {
/* 1180 */               Zone zone = Zones.getZone(digTilex + x, digTiley + y, performer.isOnSurface());
/* 1181 */               VolaTile vtile = zone.getTileOrNull(digTilex + x, digTiley + y);
/* 1182 */               if (vtile != null) {
/*      */                 
/* 1184 */                 if (vtile.getStructure() != null) {
/*      */                   
/* 1186 */                   if (vtile.getStructure().isTypeHouse()) {
/*      */                     
/* 1188 */                     performer.getCommunicator().sendNormalServerMessage("The house is in the way.");
/* 1189 */                     return true;
/*      */                   } 
/*      */                   
/* 1192 */                   BridgePart[] bps = vtile.getBridgeParts();
/* 1193 */                   if (bps.length == 1) {
/*      */ 
/*      */                     
/* 1196 */                     if (bps[0].getType().isSupportType()) {
/*      */                       
/* 1198 */                       performer.getCommunicator().sendNormalServerMessage("The bridge support nearby prevents digging.");
/* 1199 */                       return true;
/*      */                     } 
/* 1201 */                     if ((x == -1 && bps[0].hasEastExit()) || (x == 0 && bps[0]
/* 1202 */                       .hasWestExit()) || (y == -1 && bps[0]
/* 1203 */                       .hasSouthExit()) || (y == 0 && bps[0]
/* 1204 */                       .hasNorthExit())) {
/*      */                       
/* 1206 */                       performer.getCommunicator().sendNormalServerMessage("The end of the bridge nearby prevents digging.");
/* 1207 */                       return true;
/*      */                     } 
/*      */                   } 
/*      */                 } 
/* 1211 */                 if (x == 0 && y == 0) {
/*      */                   
/* 1213 */                   Fence[] arrayOfFence = vtile.getFences(); int i = arrayOfFence.length; byte b = 0; if (b < i) { Fence fence = arrayOfFence[b];
/*      */                     
/* 1215 */                     performer.getCommunicator().sendNormalServerMessage("The " + fence
/* 1216 */                         .getName() + " is in the way.");
/* 1217 */                     return true; }
/*      */ 
/*      */                 
/* 1220 */                 } else if (x == -1 && y == 0) {
/*      */                   
/* 1222 */                   for (Fence fence : vtile.getFences()) {
/*      */                     
/* 1224 */                     if (fence.isHorizontal())
/*      */                     {
/* 1226 */                       performer.getCommunicator().sendNormalServerMessage("The " + fence
/* 1227 */                           .getName() + " is in the way.");
/* 1228 */                       return true;
/*      */                     }
/*      */                   
/*      */                   } 
/* 1232 */                 } else if (y == -1 && x == 0) {
/*      */                   
/* 1234 */                   for (Fence fence : vtile.getFences()) {
/*      */                     
/* 1236 */                     if (!fence.isHorizontal())
/*      */                     {
/* 1238 */                       performer.getCommunicator().sendNormalServerMessage("The " + fence
/* 1239 */                           .getName() + " is in the way.");
/* 1240 */                       return true;
/*      */                     }
/*      */                   
/*      */                   } 
/*      */                 } 
/*      */               } 
/* 1246 */             } catch (NoSuchZoneException nsz) {
/*      */               
/* 1248 */               performer.getCommunicator().sendNormalServerMessage("The water is too deep to dig in.");
/* 1249 */               return true;
/*      */             } 
/* 1251 */             if (performer.getStrengthSkill() < 20.0D) {
/*      */               
/* 1253 */               newTile = mesh.getTile(digTilex + x, digTiley + y);
/* 1254 */               if (isRoad(Tiles.decodeType(newTile))) {
/*      */                 
/* 1256 */                 performer.getCommunicator().sendNormalServerMessage("You need to be stronger to dig on roads.");
/*      */                 
/* 1258 */                 return true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 1263 */         if (counter == 1.0F && !insta) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1270 */           if (dredging && h < -0.5D) {
/*      */             
/* 1272 */             time = Actions.getStandardActionTime(performer, digging, source, 0.0D);
/* 1273 */             act.setTimeLeft(time);
/* 1274 */             performer.getCommunicator().sendNormalServerMessage("You start to dredge.");
/* 1275 */             Server.getInstance().broadCastAction(performer.getName() + " starts to dredge.", performer, 5);
/*      */             
/* 1277 */             performer.sendActionControl(Actions.actionEntrys[362].getVerbString(), true, time);
/*      */ 
/*      */             
/* 1280 */             performer.getStatus().modifyStamina(-3000.0F);
/*      */           }
/*      */           else {
/*      */             
/* 1284 */             time = Actions.getStandardActionTime(performer, digging, source, 0.0D);
/* 1285 */             act.setTimeLeft(time);
/* 1286 */             performer.getCommunicator().sendNormalServerMessage("You start to dig.");
/* 1287 */             Server.getInstance().broadCastAction(performer.getName() + " starts to dig.", performer, 5);
/*      */             
/* 1289 */             performer.sendActionControl(Actions.actionEntrys[144].getVerbString(), true, time);
/*      */ 
/*      */             
/* 1292 */             performer.getStatus().modifyStamina(-1000.0F);
/*      */           } 
/*      */           
/* 1295 */           source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*      */         }
/* 1297 */         else if (!insta) {
/*      */           
/* 1299 */           time = act.getTimeLeft();
/* 1300 */           if (act.justTickedSecond() && ((time < 50 && act.currentSecond() % 2 == 0) || act.currentSecond() % 5 == 0)) {
/*      */             
/* 1302 */             String sstring = "sound.work.digging1";
/* 1303 */             int i = Server.rand.nextInt(3);
/* 1304 */             if (i == 0) {
/* 1305 */               sstring = "sound.work.digging2";
/* 1306 */             } else if (i == 1) {
/* 1307 */               sstring = "sound.work.digging3";
/* 1308 */             }  SoundPlayer.playSound(sstring, performer, 0.0F);
/* 1309 */             source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*      */           } 
/*      */         } 
/* 1312 */         if (counter * 10.0F > time || insta) {
/*      */ 
/*      */           
/* 1315 */           int performerTileX = performer.getTileX();
/* 1316 */           int performerTileY = performer.getTileY();
/* 1317 */           int performerTile = mesh.getTile(performerTileX, performerTileY);
/* 1318 */           byte resType = Tiles.decodeType(performerTile);
/*      */           
/* 1320 */           if (!insta) {
/*      */             
/* 1322 */             if (act.getRarity() != 0)
/*      */             {
/* 1324 */               performer.playPersonalSound("sound.fx.drumroll");
/*      */             }
/* 1326 */             double diff = (1 + maxdifference / 5);
/* 1327 */             if (resType == Tiles.Tile.TILE_CLAY.id) {
/* 1328 */               diff = 20.0D + diff;
/* 1329 */             } else if (resType == Tiles.Tile.TILE_SAND.id) {
/* 1330 */               diff = 10.0D + diff;
/* 1331 */             } else if (resType == Tiles.Tile.TILE_TAR.id) {
/* 1332 */               diff = 35.0D + diff;
/* 1333 */             } else if (resType == Tiles.Tile.TILE_MOSS.id) {
/* 1334 */               diff = 10.0D + diff;
/* 1335 */             } else if (resType == Tiles.Tile.TILE_MARSH.id) {
/* 1336 */               diff = 30.0D + diff;
/* 1337 */             } else if (resType == Tiles.Tile.TILE_STEPPE.id) {
/* 1338 */               diff = 40.0D + diff;
/* 1339 */             } else if (resType == Tiles.Tile.TILE_TUNDRA.id) {
/* 1340 */               diff = 20.0D + diff;
/* 1341 */             }  if (shovel != null)
/* 1342 */               shovel.skillCheck(diff, source, 0.0D, false, counter); 
/* 1343 */             power = digging.skillCheck(diff, source, 0.0D, false, counter);
/* 1344 */             if (power < 0.0D)
/*      */             {
/* 1346 */               for (int j = 0; j < 20; j++) {
/*      */                 
/* 1348 */                 power = digging.skillCheck(diff, source, 0.0D, true, 1.0F);
/* 1349 */                 if (power > 1.0D)
/*      */                   break; 
/* 1351 */                 power = 1.0D;
/*      */               } 
/*      */             }
/*      */ 
/*      */             
/* 1356 */             float staminaCost = (act.getTimeLeft() * -100);
/* 1357 */             performer.getStatus().modifyStamina(staminaCost);
/*      */           } 
/* 1359 */           done = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1377 */           boolean hitRock = false;
/* 1378 */           boolean dealDirt = false;
/*      */ 
/*      */ 
/*      */           
/* 1382 */           short newDigHeight = 30000;
/* 1383 */           boolean dealClay = false;
/* 1384 */           boolean dealSand = false;
/* 1385 */           boolean dealPeat = false;
/* 1386 */           boolean dealTar = false;
/* 1387 */           boolean dealMoss = false;
/* 1388 */           int clayNum = Server.getDigCount(tilex, tiley);
/* 1389 */           if (clayNum <= 0 || clayNum > 100)
/* 1390 */             clayNum = 50 + Server.rand.nextInt(50); 
/* 1391 */           boolean allCornersRock = false;
/* 1392 */           for (int i = 0; i >= -1; i--) {
/*      */             
/* 1394 */             for (int y = 0; y >= -1; y--) {
/*      */               
/* 1396 */               boolean lChanged = false;
/* 1397 */               int lNewTile = mesh.getTile(digTilex + i, digTiley + y);
/* 1398 */               type = Tiles.decodeType(lNewTile);
/*      */               
/* 1400 */               short newTileHeight = Tiles.decodeHeight(lNewTile);
/* 1401 */               rockTile = Server.rockMesh.getTile(digTilex + i, digTiley + y);
/* 1402 */               rockHeight = Tiles.decodeHeight(rockTile);
/* 1403 */               if (i == 0 && y == 0) {
/*      */                 
/* 1405 */                 if (resType == Tiles.Tile.TILE_CLAY.id) {
/* 1406 */                   dealClay = true;
/* 1407 */                 } else if (resType == Tiles.Tile.TILE_SAND.id) {
/* 1408 */                   dealSand = true;
/* 1409 */                 } else if (resType == Tiles.Tile.TILE_PEAT.id) {
/* 1410 */                   dealPeat = true;
/* 1411 */                 } else if (resType == Tiles.Tile.TILE_TAR.id) {
/* 1412 */                   dealTar = true;
/* 1413 */                 } else if (resType == Tiles.Tile.TILE_MOSS.id) {
/* 1414 */                   dealMoss = true;
/* 1415 */                 }  if (newTileHeight > rockHeight) {
/*      */                   
/* 1417 */                   dealDirt = true;
/* 1418 */                   lChanged = true;
/* 1419 */                   if (dealClay) {
/*      */                     
/* 1421 */                     clayNum--;
/* 1422 */                     if (clayNum == 0) {
/* 1423 */                       newTileHeight = (short)Math.max(newTileHeight - 1, rockHeight);
/*      */                     }
/* 1425 */                   } else if (!dealTar && !dealMoss && !dealPeat) {
/* 1426 */                     newTileHeight = (short)Math.max(newTileHeight - 1, rockHeight);
/* 1427 */                   }  if (insta) {
/* 1428 */                     performer.getCommunicator().sendNormalServerMessage("Tile " + (digTilex + i) + ", " + (digTiley + y) + " now at " + newTileHeight + ", rock at " + rockHeight + ".");
/*      */                   }
/*      */                   
/* 1431 */                   newDigHeight = newTileHeight;
/*      */ 
/*      */                   
/* 1434 */                   mesh.setTile(digTilex + i, digTiley + y, 
/* 1435 */                       Tiles.encode(newTileHeight, type, Tiles.decodeData(lNewTile)));
/* 1436 */                   if (performer.fireTileLog())
/* 1437 */                     TileEvent.log(digTilex + i, digTiley + y, 0, performer.getWurmId(), 144); 
/*      */                 } 
/* 1439 */                 if (newTileHeight <= rockHeight)
/* 1440 */                   hitRock = true; 
/*      */               } 
/* 1442 */               HighwayPos highwayPos = MethodsHighways.getHighwayPos(digTilex + i, digTiley + y, true);
/* 1443 */               allCornersRock = allCornersAtRockLevel(digTilex + i, digTiley + y, mesh);
/* 1444 */               if (!isImmutableTile(type) && allCornersRock && !MethodsHighways.onHighway(highwayPos)) {
/*      */                 
/* 1446 */                 lChanged = true;
/* 1447 */                 Server.modifyFlagsByTileType(digTilex + i, digTiley + y, Tiles.Tile.TILE_ROCK.id);
/* 1448 */                 mesh.setTile(digTilex + i, digTiley + y, 
/* 1449 */                     Tiles.encode(newTileHeight, Tiles.Tile.TILE_ROCK.id, (byte)0));
/* 1450 */                 TileEvent.log(digTilex + i, digTiley + y, 0, performer.getWurmId(), 144);
/*      */               }
/* 1452 */               else if (isTileTurnToDirt(type)) {
/*      */                 
/* 1454 */                 if (type != Tiles.Tile.TILE_DIRT.id) {
/* 1455 */                   TileEvent.log(digTilex + i, digTiley + y, 0, performer.getWurmId(), 144);
/*      */                 }
/* 1457 */                 lChanged = true;
/* 1458 */                 Server.modifyFlagsByTileType(digTilex + i, digTiley + y, Tiles.Tile.TILE_DIRT.id);
/* 1459 */                 mesh.setTile(digTilex + i, digTiley + y, 
/* 1460 */                     Tiles.encode(newTileHeight, Tiles.Tile.TILE_DIRT.id, (byte)0));
/*      */               }
/* 1462 */               else if (isRoad(type)) {
/*      */                 
/* 1464 */                 if (Methods.isActionAllowed(performer, (short)144, false, digTilex + i, digTiley + y, digTile, 0)) {
/*      */ 
/*      */                   
/* 1467 */                   lChanged = true;
/* 1468 */                   Server.modifyFlagsByTileType(digTilex + i, digTiley + y, type);
/* 1469 */                   mesh.setTile(digTilex + i, digTiley + y, 
/* 1470 */                       Tiles.encode(newTileHeight, type, Tiles.decodeData(lNewTile)));
/*      */                 } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1497 */                 if (performer.fireTileLog())
/* 1498 */                   TileEvent.log(digTilex + i, digTiley + y, 0, performer.getWurmId(), 144); 
/*      */               } 
/* 1500 */               if (performer.getTutorialLevel() == 8 && !performer.skippedTutorial())
/*      */               {
/* 1502 */                 performer.missionFinished(true, true);
/*      */               }
/* 1504 */               if (lChanged) {
/*      */                 
/* 1506 */                 performer.getMovementScheme().touchFreeMoveCounter();
/* 1507 */                 Players.getInstance()
/* 1508 */                   .sendChangedTile(digTilex + i, digTiley + y, performer.isOnSurface(), true);
/*      */ 
/*      */                 
/*      */                 try {
/* 1512 */                   Zone toCheckForChange = Zones.getZone(digTilex + i, digTiley + y, performer
/* 1513 */                       .isOnSurface());
/* 1514 */                   toCheckForChange.changeTile(digTilex + i, digTiley + y);
/*      */                 }
/* 1516 */                 catch (NoSuchZoneException nsz) {
/*      */                   
/* 1518 */                   logger.log(Level.INFO, "no such zone?: " + digTilex + ", " + digTiley, (Throwable)nsz);
/*      */                 } 
/*      */               } 
/* 1521 */               if (performer.isOnSurface()) {
/*      */ 
/*      */                 
/* 1524 */                 Tiles.Tile theTile = Tiles.getTile(type);
/* 1525 */                 if (theTile.isTree()) {
/*      */                   
/* 1527 */                   byte data = Tiles.decodeData(lNewTile);
/* 1528 */                   Zones.reposWildHive(digTilex + i, digTiley + y, theTile, data);
/*      */                 } 
/* 1530 */                 Item[] foundArtifacts = EndGameItems.getArtifactDugUp(digTilex + i, digTiley + y, newTileHeight / 10.0F, allCornersRock);
/*      */                 
/* 1532 */                 if (foundArtifacts.length > 0)
/*      */                 {
/* 1534 */                   for (int aa = 0; aa < foundArtifacts.length; aa++) {
/*      */                     
/* 1536 */                     VolaTile atile = Zones.getOrCreateTile(digTilex + i, digTiley + y, performer
/* 1537 */                         .isOnSurface());
/* 1538 */                     atile.addItem(foundArtifacts[aa], false, false);
/* 1539 */                     performer.getCommunicator()
/* 1540 */                       .sendNormalServerMessage("You find something weird! You found the " + foundArtifacts[aa]
/*      */                         
/* 1542 */                         .getName() + "!", (byte)2);
/* 1543 */                     logger.log(Level.INFO, performer
/* 1544 */                         .getName() + " found the " + foundArtifacts[aa].getName() + " at tile " + (digTilex + i) + ", " + (digTiley + y) + "! " + foundArtifacts[aa]);
/*      */ 
/*      */                     
/* 1547 */                     HistoryManager.addHistory(performer.getName(), "reveals the " + foundArtifacts[aa]
/* 1548 */                         .getName());
/* 1549 */                     EndGameItem egi = EndGameItems.getEndGameItem(foundArtifacts[aa]);
/* 1550 */                     if (egi != null) {
/*      */                       
/* 1552 */                       egi.setLastMoved(System.currentTimeMillis());
/* 1553 */                       foundArtifacts[aa].setAuxData((byte)120);
/*      */                     } 
/*      */                   } 
/*      */                 }
/*      */               } 
/* 1558 */               Item[] foundItems = Items.getHiddenItemsAt(digTilex + i, digTiley + y, newTileHeight / 10.0F, true);
/*      */               
/* 1560 */               if (foundItems.length > 0)
/*      */               {
/* 1562 */                 for (int aa = 0; aa < foundItems.length; aa++) {
/*      */                   
/* 1564 */                   foundItems[aa].setHidden(false);
/* 1565 */                   Items.revealItem(foundItems[aa]);
/* 1566 */                   VolaTile atile = Zones.getOrCreateTile(digTilex + i, digTiley + y, performer
/* 1567 */                       .isOnSurface());
/* 1568 */                   atile.addItem(foundItems[aa], false, false);
/* 1569 */                   performer.getCommunicator().sendNormalServerMessage("You find something! You found a " + foundItems[aa]
/* 1570 */                       .getName() + "!", (byte)2);
/* 1571 */                   logger.log(Level.INFO, performer.getName() + " found a " + foundItems[aa].getName() + " at tile " + (digTilex + i) + ", " + (digTiley + y) + ".");
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/* 1578 */           if (dealClay)
/*      */           {
/* 1580 */             Server.setDigCount(tilex, tiley, clayNum);
/*      */           }
/* 1582 */           if (hitRock) {
/* 1583 */             performer.getCommunicator().sendNormalServerMessage("You hit rock.", (byte)3);
/*      */           } else {
/*      */             
/* 1586 */             performer.getCommunicator().sendNormalServerMessage("You dig a hole.");
/* 1587 */             Server.getInstance().broadCastAction(performer.getName() + " digs a hole.", performer, 5);
/*      */           } 
/* 1589 */           if (dealDirt) {
/*      */             try
/*      */             {
/*      */               
/* 1593 */               if (!insta) {
/*      */                 
/* 1595 */                 double dig = digging.getKnowledge(0.0D);
/* 1596 */                 if (power > dig) {
/* 1597 */                   power = dig;
/*      */                 } else {
/* 1599 */                   power = Math.max(1.0D, power);
/*      */                 } 
/*      */               } else {
/* 1602 */                 power = 50.0D;
/* 1603 */               }  int createdItemTemplate = 26;
/* 1604 */               if (dealClay) {
/* 1605 */                 createdItemTemplate = 130;
/* 1606 */               } else if (dealSand) {
/* 1607 */                 createdItemTemplate = 298;
/* 1608 */               } else if (dealTar) {
/* 1609 */                 createdItemTemplate = 153;
/* 1610 */               } else if (dealPeat) {
/* 1611 */                 createdItemTemplate = 467;
/* 1612 */               } else if (dealMoss) {
/* 1613 */                 createdItemTemplate = 479;
/* 1614 */               }  float modifier = 1.0F;
/* 1615 */               if (source.getSpellEffects() != null)
/*      */               {
/* 1617 */                 modifier = source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */               }
/*      */               
/* 1620 */               VolaTile ttile = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/* 1621 */               if (digToPile && ttile != null && ttile.getNumberOfItems(performer.getFloorLevel()) <= 99) {
/*      */                 
/* 1623 */                 Item newItem = ItemFactory.createItem(createdItemTemplate, 
/* 1624 */                     Math.min((float)(power + source.getRarity()) * modifier, 100.0F), performer
/* 1625 */                     .getPosX(), performer.getPosY(), Server.rand.nextFloat() * 360.0F, performer
/* 1626 */                     .isOnSurface(), act.getRarity(), -10L, null);
/* 1627 */                 newItem.setLastOwnerId(performer.getWurmId());
/* 1628 */                 performer.getCommunicator().sendNormalServerMessage("You drop a " + newItem.getName() + ".");
/*      */               }
/*      */               else {
/*      */                 
/* 1632 */                 Item created = ItemFactory.createItem(createdItemTemplate, 
/* 1633 */                     Math.min((float)(power + source.getRarity()) * modifier, 100.0F), null);
/* 1634 */                 created.setRarity(act.getRarity());
/* 1635 */                 if (dredging && h < -0.5D) {
/*      */                   
/* 1637 */                   boolean addedToBoat = false;
/*      */                   
/* 1639 */                   if (performer.getVehicle() != -10L) {
/*      */                     
/*      */                     try {
/*      */                       
/* 1643 */                       Item ivehic = Items.getItem(performer.getVehicle());
/* 1644 */                       if (ivehic.isBoat())
/*      */                       {
/* 1646 */                         if (ivehic.testInsertItem(created))
/*      */                         {
/* 1648 */                           ivehic.insertItem(created);
/* 1649 */                           performer.getCommunicator().sendNormalServerMessage("You put the " + created.getName() + " in the " + ivehic
/* 1650 */                               .getName() + ".");
/* 1651 */                           addedToBoat = true;
/*      */                         }
/*      */                       
/*      */                       }
/* 1655 */                     } catch (NoSuchItemException noSuchItemException) {}
/*      */                   }
/*      */ 
/*      */                   
/* 1659 */                   if (!addedToBoat) {
/* 1660 */                     source.insertItem(created, true);
/*      */                   }
/*      */                 } else {
/* 1663 */                   performer.getInventory().insertItem(created);
/*      */                 } 
/*      */               } 
/* 1666 */               if (Server.isDirtHeightLower(digTilex, digTiley, newDigHeight)) {
/*      */ 
/*      */                 
/* 1669 */                 if (Server.rand.nextInt(2500) == 0) {
/*      */                   
/* 1671 */                   int gemTemplateId = 374;
/* 1672 */                   if (Server.rand.nextFloat() * 100.0F >= 99.0F)
/* 1673 */                     gemTemplateId = 375; 
/* 1674 */                   float ql = Math.max(Math.min((float)(power + source.getRarity()), 100.0F), 1.0F);
/* 1675 */                   Item gem = ItemFactory.createItem(gemTemplateId, ql, null);
/*      */                   
/* 1677 */                   gem.setLastOwnerId(performer.getWurmId());
/* 1678 */                   gem.setRarity(act.getRarity());
/*      */                   
/* 1680 */                   if (gem.getQualityLevel() > 99.0F) {
/* 1681 */                     performer.achievement(363);
/* 1682 */                   } else if (gem.getQualityLevel() > 90.0F) {
/* 1683 */                     performer.achievement(364);
/* 1684 */                   }  if (act.getRarity() > 2)
/* 1685 */                     performer.achievement(365); 
/* 1686 */                   performer.getInventory().insertItem(gem, true);
/* 1687 */                   performer.getCommunicator().sendNormalServerMessage("You find " + gem
/* 1688 */                       .getNameWithGenus() + "!", (byte)2);
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/* 1693 */                 if (act.getRarity() != 0 && performer.isPaying() && Server.rand.nextInt(100) == 0) {
/*      */                   
/* 1695 */                   float ql = Math.max(Math.min((float)(power + source.getRarity()), 100.0F), 1.0F);
/* 1696 */                   Item bone = ItemFactory.createItem(867, ql, null);
/* 1697 */                   bone.setRarity(act.getRarity());
/* 1698 */                   performer.getInventory().insertItem(bone, true);
/* 1699 */                   performer.getCommunicator().sendNormalServerMessage("You find something! You found a " + 
/* 1700 */                       MethodsItems.getRarityName(act.getRarity()) + " " + bone
/* 1701 */                       .getName() + "!", (byte)2);
/* 1702 */                   performer.achievement(366);
/*      */                 } 
/*      */ 
/*      */                 
/* 1706 */                 if (Server.rand.nextInt(250) == 0) {
/*      */                   
/* 1708 */                   VolaTile t = Zones.getTileOrNull(digTilex, digTiley, performer.isOnSurface());
/* 1709 */                   if ((t != null && t.getVillage() == null) || t == null) {
/*      */                     
/* 1711 */                     EpicMission m = EpicServerStatus.getMISacrificeMission();
/* 1712 */                     if (m != null) {
/*      */                       
/*      */                       try {
/*      */                         
/* 1716 */                         Item missionItem = ItemFactory.createItem(737, (20 + Server.rand.nextInt(80)), act.getRarity(), m.getEntityName());
/* 1717 */                         missionItem.setName(HexMap.generateFirstName(m.getMissionId()) + ' ' + HexMap.generateSecondName(m.getMissionId()));
/*      */                         
/* 1719 */                         performer.getInventory().insertItem(missionItem);
/* 1720 */                         performer.getCommunicator().sendNormalServerMessage("You find a " + missionItem.getName() + " in amongst the dirt.");
/*      */                       }
/* 1722 */                       catch (FailedException|NoSuchTemplateException failedException) {}
/*      */                     }
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/* 1730 */               PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.DIG_TILE);
/*      */             }
/* 1732 */             catch (FailedException fe)
/*      */             {
/* 1734 */               logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */             }
/*      */           
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/* 1741 */         done = true;
/*      */         
/* 1743 */         if (dredging) {
/* 1744 */           if (h <= minHeight) {
/* 1745 */             performer.getCommunicator().sendNormalServerMessage("You do not have sufficient skill to dredge at that depth.", (byte)3);
/*      */           }
/*      */           else {
/*      */             
/* 1749 */             performer.getCommunicator().sendNormalServerMessage("The water is too shallow to be dredged.", (byte)3);
/*      */           } 
/* 1751 */         } else if (h <= minHeight) {
/* 1752 */           performer.getCommunicator().sendNormalServerMessage("The water is too deep to dig.", (byte)3);
/*      */         }
/*      */         else {
/*      */           
/* 1756 */           performer.getCommunicator().sendNormalServerMessage("You do not have sufficient skill to dig at that height.", (byte)3);
/*      */         }
/*      */       
/*      */       } 
/* 1760 */     } catch (NoSuchTemplateException nst) {
/*      */       
/* 1762 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/* 1763 */       done = true;
/*      */     } 
/* 1765 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int countNonDiggables(MeshIO mesh, int digTilex, int digTiley) {
/* 1770 */     int nonDiggables = 0;
/* 1771 */     int lNewTile = mesh.getTile(digTilex, digTiley);
/* 1772 */     if (isNonDiggableTile(Tiles.decodeType(lNewTile)))
/* 1773 */       nonDiggables++; 
/* 1774 */     lNewTile = mesh.getTile(digTilex, digTiley - 1);
/* 1775 */     if (isNonDiggableTile(Tiles.decodeType(lNewTile)))
/* 1776 */       nonDiggables++; 
/* 1777 */     lNewTile = mesh.getTile(digTilex - 1, digTiley - 1);
/* 1778 */     if (isNonDiggableTile(Tiles.decodeType(lNewTile)))
/* 1779 */       nonDiggables++; 
/* 1780 */     lNewTile = mesh.getTile(digTilex - 1, digTiley);
/* 1781 */     if (isNonDiggableTile(Tiles.decodeType(lNewTile)))
/* 1782 */       nonDiggables++; 
/* 1783 */     return nonDiggables;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean checkSculptCaveTile(int lNewTile, Creature performer, short floorHeight, int ceilingHeight, int decimeterChange, boolean obliterateCeiling) {
/* 1789 */     if (Tiles.decodeType(lNewTile) == Tiles.Tile.TILE_CAVE_EXIT.id)
/* 1790 */       return true; 
/* 1791 */     short nfloorHeight = Tiles.decodeHeight(lNewTile);
/* 1792 */     if (nfloorHeight != -100) {
/*      */       
/* 1794 */       int nceilingHeight = nfloorHeight + (short)(Tiles.decodeData(lNewTile) & 0xFF);
/* 1795 */       if (obliterateCeiling) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1802 */         int checkedCeilingHeight = floorHeight + ceilingHeight + decimeterChange;
/* 1803 */         if (Math.abs(checkedCeilingHeight - nceilingHeight) > 254)
/*      */         {
/* 1805 */           return true;
/*      */         }
/*      */       }
/* 1808 */       else if (Math.abs(floorHeight - decimeterChange - nfloorHeight) > 254) {
/*      */         
/* 1810 */         return true;
/*      */       } 
/*      */     } 
/* 1813 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean checkMineSurfaceTile(int lNewTile, Creature performer, short digTileHeight, short maxDiff) {
/* 1819 */     short height = Tiles.decodeHeight(lNewTile);
/* 1820 */     short difference = (short)Math.abs(height - digTileHeight - 1);
/*      */     
/* 1822 */     if (difference > maxDiff) {
/*      */       
/* 1824 */       performer.getCommunicator().sendNormalServerMessage("You are not skilled enough to mine in the steep slope.");
/* 1825 */       return true;
/*      */     } 
/* 1827 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean checkSculptTile(int lNewTile, Creature performer, short digTileHeight, int decimeterChange) {
/* 1833 */     if (!isSculptable(Tiles.decodeType(lNewTile))) {
/*      */       
/* 1835 */       performer.getCommunicator().sendNormalServerMessage("Nothing happens on the " + 
/* 1836 */           (Tiles.getTile(Tiles.decodeType(lNewTile))).tiledesc + ". The wand only seems to work on grass, rock, dirt and similar terrain.");
/*      */       
/* 1838 */       return true;
/*      */     } 
/* 1840 */     short height = Tiles.decodeHeight(lNewTile);
/* 1841 */     short difference = (short)Math.abs(height - digTileHeight - decimeterChange);
/*      */     
/* 1843 */     if (difference > 200) {
/*      */       
/* 1845 */       performer.getCommunicator().sendNormalServerMessage("Nothing happens on the steep slope.");
/* 1846 */       return true;
/*      */     } 
/* 1848 */     return false;
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
/*      */   public static boolean checkDigTile(int lNewTile, Creature performer, Skill digging, short digTileHeight, short difference) {
/* 1862 */     double diffMod = digging.getKnowledge(0.0D);
/* 1863 */     if (difference > Math.max(10.0D, 1.0D + diffMod * 3.0D)) {
/*      */       
/* 1865 */       performer.getCommunicator().sendNormalServerMessage("You are not skilled enough to dig in such steep slopes.", (byte)3);
/* 1866 */       return true;
/*      */     } 
/* 1868 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean checkHeightDiff(boolean raise, short myHeight, short checkHeight, short maxHeightDiff) {
/* 1874 */     if (raise) {
/*      */       
/* 1876 */       if (myHeight - checkHeight > maxHeightDiff) {
/* 1877 */         return true;
/*      */       
/*      */       }
/*      */     }
/* 1881 */     else if (checkHeight - myHeight > maxHeightDiff) {
/* 1882 */       return true;
/*      */     } 
/* 1884 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean nextToTundra(MeshIO mesh, int digTilex, int digTiley) {
/* 1889 */     if (Tiles.isTundra(Tiles.decodeType(mesh.getTile(digTilex, digTiley))))
/* 1890 */       return true; 
/* 1891 */     if (Tiles.isTundra(Tiles.decodeType(mesh.getTile(digTilex - 1, digTiley))))
/* 1892 */       return true; 
/* 1893 */     if (Tiles.isTundra(Tiles.decodeType(mesh.getTile(digTilex, digTiley - 1))))
/* 1894 */       return true; 
/* 1895 */     if (Tiles.isTundra(Tiles.decodeType(mesh.getTile(digTilex - 1, digTiley - 1))))
/* 1896 */       return true; 
/* 1897 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean wouldDestroyCobble(Creature performer, int changeTileX, int changeTileY, boolean raise) {
/* 1903 */     short modDiff = (short)(raise ? 1 : -1);
/* 1904 */     MeshIO mesh = Server.surfaceMesh;
/*      */ 
/*      */ 
/*      */     
/* 1908 */     int mytile = mesh.getTile(changeTileX, changeTileY);
/* 1909 */     short myHeight = (short)(Tiles.decodeHeight(mytile) + modDiff);
/*      */     
/* 1911 */     if (myHeight < 0)
/*      */     {
/*      */       
/* 1914 */       return false;
/*      */     }
/*      */     
/* 1917 */     int checkTile = mesh.getTile(changeTileX + 1, changeTileY);
/* 1918 */     short checkHeight = Tiles.decodeHeight(checkTile);
/* 1919 */     if (Tiles.isRoadType(mytile) && MethodsHighways.onHighway(changeTileX + 1, changeTileY, true)) {
/*      */       
/* 1921 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20))
/* 1922 */         return true; 
/* 1923 */       checkTile = mesh.getTile(changeTileX, changeTileY + 1);
/* 1924 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1925 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20)) {
/* 1926 */         return true;
/*      */       }
/* 1928 */       checkTile = mesh.getTile(changeTileX + 1, changeTileY + 1);
/* 1929 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1930 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)28)) {
/* 1931 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1935 */     checkTile = mesh.getTile(changeTileX - 1, changeTileY);
/* 1936 */     checkHeight = Tiles.decodeHeight(checkTile);
/* 1937 */     if (Tiles.isRoadType(checkTile) && MethodsHighways.onHighway(changeTileX - 1, changeTileY, true)) {
/*      */       
/* 1939 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20))
/* 1940 */         return true; 
/* 1941 */       checkTile = mesh.getTile(changeTileX, changeTileY + 1);
/* 1942 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1943 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20)) {
/* 1944 */         return true;
/*      */       }
/* 1946 */       checkTile = mesh.getTile(changeTileX - 1, changeTileY + 1);
/* 1947 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1948 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)28)) {
/* 1949 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1953 */     checkTile = mesh.getTile(changeTileX - 1, changeTileY - 1);
/* 1954 */     if (Tiles.isRoadType(checkTile) && MethodsHighways.onHighway(changeTileX - 1, changeTileY - 1, true)) {
/*      */ 
/*      */ 
/*      */       
/* 1958 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1959 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)28))
/* 1960 */         return true; 
/* 1961 */       checkTile = mesh.getTile(changeTileX, changeTileY - 1);
/* 1962 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1963 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20))
/* 1964 */         return true; 
/* 1965 */       checkTile = mesh.getTile(changeTileX - 1, changeTileY);
/* 1966 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1967 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20)) {
/* 1968 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1972 */     checkTile = mesh.getTile(changeTileX, changeTileY - 1);
/* 1973 */     checkHeight = Tiles.decodeHeight(checkTile);
/* 1974 */     if (Tiles.isRoadType(checkTile) && MethodsHighways.onHighway(changeTileX, changeTileY - 1, true)) {
/*      */       
/* 1976 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20))
/* 1977 */         return true; 
/* 1978 */       checkTile = mesh.getTile(changeTileX + 1, changeTileY);
/* 1979 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1980 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)20)) {
/* 1981 */         return true;
/*      */       }
/*      */       
/* 1984 */       checkTile = mesh.getTile(changeTileX + 1, changeTileY - 1);
/* 1985 */       checkHeight = Tiles.decodeHeight(checkTile);
/* 1986 */       if (checkHeightDiff(raise, myHeight, checkHeight, (short)28))
/* 1987 */         return true; 
/*      */     } 
/* 1989 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean pack(Creature performer, Item source, int tilex, int tiley, boolean onSurface, int tile, float counter, Action act) {
/* 1995 */     boolean done = false;
/* 1996 */     if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize || 
/* 1997 */       Tiles.decodeHeight(tile) < -100) {
/*      */       
/* 1999 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to pack the dirt here.");
/* 2000 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/* 2004 */       byte type = Tiles.decodeType(tile);
/* 2005 */       if (isPackable(type)) {
/*      */         
/* 2007 */         if (type != Tiles.Tile.TILE_DIRT_PACKED.id) {
/*      */           
/* 2009 */           done = false;
/* 2010 */           Skills skills = performer.getSkills();
/* 2011 */           Skill paving = null;
/* 2012 */           Skill shovel = null;
/*      */           
/*      */           try {
/* 2015 */             paving = skills.getSkill(10031);
/*      */           }
/* 2017 */           catch (Exception ex) {
/*      */             
/* 2019 */             paving = skills.learn(10031, 1.0F);
/*      */           } 
/* 2021 */           if (performer.getPower() > 0)
/*      */             
/*      */             try {
/*      */               
/* 2025 */               shovel = skills.getSkill(source.getPrimarySkill());
/*      */             }
/* 2027 */             catch (Exception ex) {
/*      */ 
/*      */               
/*      */               try {
/* 2031 */                 shovel = skills.learn(source.getPrimarySkill(), 1.0F);
/*      */               }
/* 2033 */               catch (NoSuchSkillException nse) {
/*      */                 
/* 2035 */                 if (performer.getPower() <= 0) {
/* 2036 */                   logger.log(Level.WARNING, performer.getName() + " trying to pack with an item with no primary skill: " + source
/* 2037 */                       .getName());
/*      */                 }
/*      */               } 
/*      */             }  
/* 2041 */           int time = 2000;
/* 2042 */           if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2049 */             time = Actions.getStandardActionTime(performer, paving, source, 0.0D);
/* 2050 */             act.setTimeLeft(time);
/* 2051 */             performer.getCommunicator().sendNormalServerMessage("You start to pack the ground.");
/* 2052 */             Server.getInstance().broadCastAction(performer.getName() + " starts to pack the ground.", performer, 5);
/* 2053 */             performer.sendActionControl(Actions.actionEntrys[154].getVerbString(), true, time);
/*      */             
/* 2055 */             source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*      */             
/* 2057 */             performer.getStatus().modifyStamina(-1000.0F);
/*      */           }
/*      */           else {
/*      */             
/* 2061 */             time = act.getTimeLeft();
/* 2062 */             if (act.currentSecond() % 5 == 0) {
/*      */               
/* 2064 */               SoundPlayer.playSound("sound.work.digging.pack", tilex, tiley, onSurface, 0.0F);
/* 2065 */               performer.getStatus().modifyStamina(-10000.0F);
/* 2066 */               source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*      */             } 
/*      */           } 
/* 2069 */           if (counter * 10.0F > time) {
/*      */             
/* 2071 */             if (shovel != null)
/* 2072 */               shovel.skillCheck(10.0D, source, 0.0D, false, counter); 
/* 2073 */             paving.skillCheck(1.0D, source, 0.0D, false, counter);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2080 */             done = true;
/* 2081 */             int t = Server.surfaceMesh.getTile(tilex, tiley);
/* 2082 */             short h = Tiles.decodeHeight(t);
/* 2083 */             if (!isRockTile(Tiles.decodeType(t))) {
/*      */               
/* 2085 */               TileEvent.log(tilex, tiley, 0, performer.getWurmId(), act.getNumber());
/* 2086 */               Server.setSurfaceTile(tilex, tiley, h, Tiles.Tile.TILE_DIRT_PACKED.id, (byte)0);
/* 2087 */               performer.getCommunicator().sendNormalServerMessage("The dirt is packed and hard now.");
/*      */               
/*      */               try {
/* 2090 */                 Zone toCheckForChange = Zones.getZone(tilex, tiley, onSurface);
/* 2091 */                 toCheckForChange.changeTile(tilex, tiley);
/*      */                 
/* 2093 */                 Players.getInstance().sendChangedTiles(tilex, tiley, 1, 1, onSurface, true);
/*      */               }
/* 2095 */               catch (NoSuchZoneException nsz) {
/*      */                 
/* 2097 */                 logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */               } 
/*      */             } else {
/*      */               
/* 2101 */               performer.getCommunicator().sendNormalServerMessage("The rock has been bared. No dirt remains to pack anymore.");
/*      */             }
/*      */           
/*      */           } 
/*      */         } else {
/*      */           
/* 2107 */           done = true;
/* 2108 */           performer.getCommunicator().sendNormalServerMessage("The dirt is packed here already.");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 2113 */         done = true;
/* 2114 */         performer.getCommunicator().sendNormalServerMessage("You can't pack the dirt in that place. A " + source
/* 2115 */             .getName() + " just won't do.");
/*      */       } 
/*      */     } 
/* 2118 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean destroyPave(Creature performer, Item source, int tilex, int tiley, boolean onSurface, int tile, float counter) {
/* 2124 */     boolean done = false;
/* 2125 */     if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize) {
/*      */       
/* 2127 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to destroy the pavement here.");
/* 2128 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/* 2132 */       if (performer.getStrengthSkill() < 20.0D) {
/*      */         
/* 2134 */         performer.getCommunicator().sendNormalServerMessage("You need to be stronger to destroy pavement.");
/* 2135 */         return true;
/*      */       } 
/* 2137 */       if (Zones.protectedTiles[tilex][tiley]) {
/*      */         
/* 2139 */         performer.getCommunicator().sendNormalServerMessage("Your muscles go limp and refuse. You just can't bring yourself to do this.");
/*      */         
/* 2141 */         return true;
/*      */       } 
/* 2143 */       int digTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 2144 */       if (!onSurface)
/* 2145 */         digTile = Server.caveMesh.getTile(tilex, tiley); 
/* 2146 */       byte actualType = Tiles.decodeType(digTile);
/* 2147 */       byte type = (actualType == Tiles.Tile.TILE_CAVE_EXIT.id) ? Server.getClientCaveFlags(tilex, tiley) : actualType;
/*      */       
/* 2149 */       Action act = null;
/*      */       
/*      */       try {
/* 2152 */         act = performer.getCurrentAction();
/*      */       }
/* 2154 */       catch (NoSuchActionException nsa) {
/*      */         
/* 2156 */         logger.log(Level.WARNING, nsa.getMessage(), (Throwable)nsa);
/* 2157 */         return true;
/*      */       } 
/* 2159 */       if (isRoad(type)) {
/*      */         
/* 2161 */         HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/* 2162 */         if (MethodsHighways.onHighway(highwaypos)) {
/*      */           
/* 2164 */           performer.getCommunicator().sendNormalServerMessage("You cannot remove paving next to a marker.");
/* 2165 */           return true;
/*      */         } 
/* 2167 */         done = false;
/* 2168 */         Skills skills = performer.getSkills();
/* 2169 */         Skill digging = skills.getSkillOrLearn(1009);
/* 2170 */         int time = 6000;
/* 2171 */         if (counter == 1.0F) {
/*      */           
/* 2173 */           if (digging.getRealKnowledge() < 10.0D) {
/*      */             
/* 2175 */             if (type == Tiles.Tile.TILE_PLANKS.id || type == Tiles.Tile.TILE_PLANKS_TARRED.id) {
/* 2176 */               performer
/* 2177 */                 .getCommunicator()
/* 2178 */                 .sendNormalServerMessage("You can't figure out how to remove the floor boards. You must become a bit better at digging first.");
/*      */             } else {
/*      */               
/* 2181 */               performer
/* 2182 */                 .getCommunicator()
/* 2183 */                 .sendNormalServerMessage("You can't figure out how to remove the stone. You must become a bit better at digging first.");
/*      */             } 
/* 2185 */             return true;
/*      */           } 
/* 2187 */           time = Actions.getDestroyActionTime(performer, digging, source, 0.0D);
/* 2188 */           act.setTimeLeft(time);
/* 2189 */           if (type == Tiles.Tile.TILE_PLANKS.id || type == Tiles.Tile.TILE_PLANKS_TARRED.id) {
/*      */             
/* 2191 */             performer.getCommunicator().sendNormalServerMessage("You start to remove the floor boards.");
/* 2192 */             Server.getInstance().broadCastAction(performer.getName() + " starts to remove the floor boards.", performer, 5);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2197 */             String fromWhere = onSurface ? "paved dirt." : ((actualType == Tiles.Tile.TILE_CAVE_EXIT.id) ? "cave exit." : "cave floor.");
/* 2198 */             performer.getCommunicator().sendNormalServerMessage("You start to remove the stones from the " + fromWhere);
/*      */             
/* 2200 */             Server.getInstance().broadCastAction(performer
/* 2201 */                 .getName() + " starts to remove the stones from the " + fromWhere, performer, 5);
/*      */           } 
/* 2203 */           performer.sendActionControl(Actions.actionEntrys[191].getVerbString(), true, time);
/*      */           
/* 2205 */           performer.getStatus().modifyStamina(-1500.0F);
/*      */         }
/*      */         else {
/*      */           
/* 2209 */           time = act.getTimeLeft();
/* 2210 */           if (act.currentSecond() % 5 == 0) {
/*      */             
/* 2212 */             performer.getStatus().modifyStamina(-2000.0F);
/* 2213 */             source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */           } 
/*      */         } 
/*      */         
/* 2217 */         if (counter * 10.0F > time) {
/*      */           
/* 2219 */           if (digging != null)
/* 2220 */             digging.skillCheck(40.0D, source, 0.0D, false, counter); 
/* 2221 */           done = true;
/* 2222 */           String fromWhere = onSurface ? "ground" : ((actualType == Tiles.Tile.TILE_CAVE_EXIT.id) ? "cave exit" : "cave floor");
/* 2223 */           short h = Tiles.decodeHeight(digTile);
/* 2224 */           if (isRoad(type) || type == Tiles.Tile.TILE_PLANKS.id || type == Tiles.Tile.TILE_PLANKS_TARRED.id) {
/*      */ 
/*      */             
/* 2227 */             TileEvent.log(tilex, tiley, performer.getLayer(), performer.getWurmId(), 191);
/* 2228 */             if (type == Tiles.Tile.TILE_PLANKS.id || type == Tiles.Tile.TILE_PLANKS_TARRED.id) {
/*      */               
/* 2230 */               performer.getCommunicator().sendNormalServerMessage("The " + fromWhere + " is no longer covered with planks.");
/*      */             } else {
/*      */               
/* 2233 */               performer.getCommunicator().sendNormalServerMessage("The " + fromWhere + " is no longer paved with stones.");
/* 2234 */             }  if (onSurface) {
/* 2235 */               Server.setSurfaceTile(tilex, tiley, h, Tiles.Tile.TILE_DIRT.id, (byte)0);
/*      */             
/*      */             }
/* 2238 */             else if (actualType == Tiles.Tile.TILE_CAVE_EXIT.id) {
/* 2239 */               Server.setClientCaveFlags(tilex, tiley, Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id);
/*      */             } else {
/* 2241 */               Server.caveMesh.setTile(tilex, tiley, Tiles.encode(h, Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id, Tiles.decodeData(digTile)));
/*      */             } 
/* 2243 */             performer.getMovementScheme().touchFreeMoveCounter();
/* 2244 */             Players.getInstance().sendChangedTile(tilex, tiley, onSurface, true);
/*      */             
/*      */             try {
/* 2247 */               Zone toCheckForChange = Zones.getZone(tilex, tiley, onSurface);
/* 2248 */               toCheckForChange.changeTile(tilex, tiley);
/*      */             }
/* 2250 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 2252 */               logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */             } 
/* 2254 */             performer.performActionOkey(act);
/*      */           } else {
/*      */             
/* 2257 */             performer.getCommunicator().sendNormalServerMessage("The " + fromWhere + " isn't paved any longer, and your efforts are ruined.");
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 2263 */         done = true;
/* 2264 */         if (onSurface) {
/* 2265 */           performer.getCommunicator().sendNormalServerMessage("The dirt isn't even paved here.");
/*      */         } else {
/* 2267 */           performer.getCommunicator().sendNormalServerMessage("The cave floor isn't even paved here.");
/*      */         } 
/*      */       } 
/* 2270 */     }  return done;
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
/*      */   static boolean pave(Creature performer, Item source, int tilex, int tiley, boolean onSurface, int tile, float counter, Action act) {
/*      */     byte newTileType;
/* 2291 */     Communicator comm = performer.getCommunicator();
/* 2292 */     if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize) {
/*      */       
/* 2294 */       comm.sendNormalServerMessage("The water is too deep to pave here.");
/* 2295 */       return true;
/*      */     } 
/* 2297 */     if (Tiles.decodeHeight(tile) < -100) {
/*      */       
/* 2299 */       comm.sendNormalServerMessage("The water is too deep to pave here.");
/* 2300 */       return true;
/*      */     } 
/*      */     
/* 2303 */     int digTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 2304 */     byte type = Tiles.decodeType(digTile);
/* 2305 */     byte fType = Server.getClientCaveFlags(tilex, tiley);
/* 2306 */     boolean repaving = false;
/* 2307 */     if (Tiles.isRoadType(type) || (type == Tiles.Tile.TILE_CAVE_EXIT.id && Tiles.isRoadType(fType))) {
/*      */       
/* 2309 */       repaving = true;
/*      */ 
/*      */       
/* 2312 */       Village village = Villages.getVillageWithPerimeterAt(tilex, tiley, onSurface);
/* 2313 */       if (village != null)
/*      */       {
/* 2315 */         if (!village.isActionAllowed(act.getNumber(), performer))
/*      */         {
/* 2317 */           comm.sendNormalServerMessage("You do not have permissions to do that.");
/* 2318 */           return true;
/*      */         }
/*      */       
/*      */       }
/* 2322 */     } else if (type != Tiles.Tile.TILE_DIRT_PACKED.id) {
/*      */       
/* 2324 */       comm.sendNormalServerMessage("The ground isn't packed here. You have to pack it first.");
/* 2325 */       return true;
/*      */     } 
/*      */     
/* 2328 */     if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */       
/* 2330 */       comm.sendNormalServerMessage("The amount of " + source
/* 2331 */           .getName() + " is too little to pave. You may need to combine them with other " + source.getTemplate().getPlural() + ".");
/* 2332 */       return true;
/*      */     } 
/*      */     
/* 2335 */     int pavingItem = source.getTemplateId();
/* 2336 */     short actNumber = act.getNumber();
/* 2337 */     if (counter == 1.0F) {
/*      */       
/* 2339 */       Skill skill = performer.getSkills().getSkillOrLearn(10031);
/* 2340 */       int i = Actions.getStandardActionTime(performer, skill, source, 0.0D);
/* 2341 */       act.setTimeLeft(i);
/* 2342 */       if (pavingItem == 519) {
/* 2343 */         comm.sendNormalServerMessage("You break up the collosus brick and start to pave with the parts.");
/* 2344 */       } else if (repaving) {
/* 2345 */         comm.sendNormalServerMessage("You start to repave with the " + source.getName() + ".");
/*      */       } else {
/* 2347 */         comm.sendNormalServerMessage("You start to pave the packed dirt with the " + source.getName() + ".");
/* 2348 */       }  Server.getInstance().broadCastAction(performer.getName() + " starts to pave the packed dirt.", performer, 5);
/* 2349 */       performer.sendActionControl(act.getActionEntry().getVerbString(), true, i);
/* 2350 */       performer.getStatus().modifyStamina(-1000.0F);
/* 2351 */       return false;
/*      */     } 
/*      */     
/* 2354 */     int time = act.getTimeLeft();
/*      */     
/* 2356 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 2358 */       performer.getStatus().modifyStamina(-10000.0F);
/*      */     }
/* 2360 */     if (act.mayPlaySound())
/*      */     {
/* 2362 */       Methods.sendSound(performer, "sound.work.paving");
/*      */     }
/*      */ 
/*      */     
/* 2366 */     if (counter * 10.0F <= time)
/*      */     {
/* 2368 */       return false;
/*      */     }
/*      */     
/* 2371 */     if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */       
/* 2373 */       comm.sendNormalServerMessage("The amount of " + source
/* 2374 */           .getName() + " is too little to pave. You may need to combine them with other " + source.getTemplate().getPlural() + ".");
/* 2375 */       return true;
/*      */     } 
/*      */     
/* 2378 */     int t = Server.surfaceMesh.getTile(tilex, tiley);
/* 2379 */     short h = Tiles.decodeHeight(t);
/* 2380 */     if (Tiles.decodeType(t) != Tiles.Tile.TILE_DIRT_PACKED.id && !repaving) {
/*      */ 
/*      */ 
/*      */       
/* 2384 */       comm.sendNormalServerMessage("The ground isn't fit for paving any longer, and your efforts are ruined.");
/* 2385 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2389 */     Skill paving = performer.getSkills().getSkillOrLearn(10031);
/* 2390 */     paving.skillCheck((pavingItem == 146) ? 5.0D : 30.0D, source, 0.0D, false, counter);
/*      */     
/* 2392 */     TileEvent.log(tilex, tiley, 0, performer.getWurmId(), actNumber);
/*      */     
/* 2394 */     byte dir = getDiagonalDir(performer, tilex, tiley, actNumber);
/*      */     
/* 2396 */     switch (pavingItem) {
/*      */       
/*      */       case 132:
/* 2399 */         newTileType = Tiles.Tile.TILE_COBBLESTONE.id;
/*      */         break;
/*      */       case 1122:
/* 2402 */         newTileType = Tiles.Tile.TILE_COBBLESTONE_ROUND.id;
/*      */         break;
/*      */       case 519:
/* 2405 */         newTileType = Tiles.Tile.TILE_COBBLESTONE_ROUGH.id;
/*      */         break;
/*      */       case 406:
/* 2408 */         newTileType = Tiles.Tile.TILE_STONE_SLABS.id;
/*      */         break;
/*      */       case 1123:
/* 2411 */         newTileType = Tiles.Tile.TILE_SLATE_BRICKS.id;
/*      */         break;
/*      */       case 771:
/* 2414 */         newTileType = Tiles.Tile.TILE_SLATE_SLABS.id;
/*      */         break;
/*      */       case 1121:
/* 2417 */         newTileType = Tiles.Tile.TILE_SANDSTONE_BRICKS.id;
/*      */         break;
/*      */       case 1124:
/* 2420 */         newTileType = Tiles.Tile.TILE_SANDSTONE_SLABS.id;
/*      */         break;
/*      */       case 787:
/* 2423 */         newTileType = Tiles.Tile.TILE_MARBLE_SLABS.id;
/*      */         break;
/*      */       case 786:
/* 2426 */         newTileType = Tiles.Tile.TILE_MARBLE_BRICKS.id;
/*      */         break;
/*      */       case 776:
/* 2429 */         newTileType = Tiles.Tile.TILE_POTTERY_BRICKS.id;
/*      */         break;
/*      */       default:
/* 2432 */         newTileType = Tiles.Tile.TILE_GRAVEL.id;
/*      */         break;
/*      */     } 
/* 2435 */     Server.setSurfaceTile(tilex, tiley, h, newTileType, dir);
/* 2436 */     Items.destroyItem(source.getWurmId());
/*      */     
/* 2438 */     comm.sendNormalServerMessage("The ground is paved now.");
/*      */     
/* 2440 */     Players.getInstance().sendChangedTiles(tilex, tiley, 1, 1, onSurface, true);
/*      */ 
/*      */     
/*      */     try {
/* 2444 */       Zone toCheckForChange = Zones.getZone(tilex, tiley, onSurface);
/* 2445 */       toCheckForChange.changeTile(tilex, tiley);
/*      */     }
/* 2447 */     catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */     
/* 2451 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean makeFloor(Creature performer, Item source, int tilex, int tiley, boolean onSurface, int tile, float counter) {
/* 2457 */     boolean done = false;
/* 2458 */     if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize) {
/*      */       
/* 2460 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to pave here.");
/* 2461 */       done = true;
/*      */     }
/* 2463 */     else if (Tiles.decodeHeight(tile) < -100) {
/*      */       
/* 2465 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to pave here.");
/* 2466 */       done = true;
/*      */     }
/* 2468 */     else if (source.getTemplateId() != 495) {
/*      */       
/* 2470 */       performer.getCommunicator().sendNormalServerMessage("You need floor boards.");
/* 2471 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/* 2475 */       int digTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 2476 */       byte type = Tiles.decodeType(digTile);
/* 2477 */       Action act = null;
/*      */       
/*      */       try {
/* 2480 */         act = performer.getCurrentAction();
/*      */       }
/* 2482 */       catch (NoSuchActionException nsa) {
/*      */         
/* 2484 */         logger.log(Level.WARNING, nsa.getMessage(), (Throwable)nsa);
/* 2485 */         return true;
/*      */       } 
/* 2487 */       if (type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_MARSH.id) {
/*      */         
/* 2489 */         done = false;
/*      */         
/* 2491 */         int time = 2000;
/* 2492 */         if (counter == 1.0F) {
/*      */           
/* 2494 */           Skills skills = performer.getSkills();
/* 2495 */           Skill paving = null;
/* 2496 */           if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */             
/* 2498 */             performer.getCommunicator().sendNormalServerMessage("The amount of planks is too little to do this. You may need to use another item.");
/*      */             
/* 2500 */             return true;
/*      */           } 
/*      */           
/*      */           try {
/* 2504 */             paving = skills.getSkill(10031);
/*      */           }
/* 2506 */           catch (Exception ex) {
/*      */             
/* 2508 */             paving = skills.learn(10031, 1.0F);
/*      */           } 
/* 2510 */           time = Actions.getStandardActionTime(performer, paving, source, 0.0D);
/* 2511 */           act.setTimeLeft(time);
/* 2512 */           if (type == Tiles.Tile.TILE_MARSH.id) {
/*      */             
/* 2514 */             performer.getCommunicator().sendNormalServerMessage("You start to put the floorboard in the marsh.");
/* 2515 */             Server.getInstance().broadCastAction(performer
/* 2516 */                 .getName() + " starts to put the floorboard in the marsh.", performer, 5);
/*      */           }
/*      */           else {
/*      */             
/* 2520 */             performer.getCommunicator().sendNormalServerMessage("You start to fit the floorboard in the dirt.");
/* 2521 */             Server.getInstance().broadCastAction(performer
/* 2522 */                 .getName() + " starts to fit the floorboard in the dirt.", performer, 5);
/*      */           } 
/* 2524 */           performer.sendActionControl(Actions.actionEntrys[155].getVerbString(), true, time);
/*      */           
/* 2526 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2531 */           time = act.getTimeLeft();
/* 2532 */           if (act.currentSecond() % 5 == 0)
/*      */           {
/* 2534 */             performer.getStatus().modifyStamina(-10000.0F);
/*      */           }
/*      */         } 
/*      */         
/* 2538 */         if (counter * 10.0F > time) {
/*      */           
/* 2540 */           long parentId = source.getParentId();
/*      */           
/* 2542 */           act.setDestroyedItem(source);
/* 2543 */           if (parentId != -10L) {
/*      */             
/*      */             try
/*      */             {
/* 2547 */               Items.getItem(parentId).dropItem(source.getWurmId(), false);
/*      */             }
/* 2549 */             catch (NoSuchItemException nsi)
/*      */             {
/* 2551 */               logger.log(Level.INFO, performer.getName() + " tried to make floor with nonexistant floorboards.", (Throwable)nsi);
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2557 */             logger.log(Level.WARNING, performer.getName() + " managed to pave with floorboards on ground?");
/*      */ 
/*      */             
/*      */             try {
/* 2561 */               Zone zone = Zones.getZone((int)source.getPosX() >> 2, (int)source.getPosY() >> 2, source
/* 2562 */                   .isOnSurface());
/* 2563 */               zone.removeItem(source);
/*      */             }
/* 2565 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 2567 */               logger.log(Level.WARNING, performer.getName() + " failed to locate zone", (Throwable)nsz);
/*      */             } 
/*      */           } 
/*      */           
/* 2571 */           Skills skills = performer.getSkills();
/* 2572 */           Skill paving = null;
/*      */ 
/*      */           
/*      */           try {
/* 2576 */             paving = skills.getSkill(10031);
/*      */           }
/* 2578 */           catch (Exception ex) {
/*      */             
/* 2580 */             paving = skills.learn(10031, 1.0F);
/*      */           } 
/* 2582 */           if (paving != null)
/* 2583 */             paving.skillCheck(5.0D, source, 0.0D, false, counter); 
/* 2584 */           done = true;
/*      */           
/* 2586 */           int t = Server.surfaceMesh.getTile(tilex, tiley);
/* 2587 */           short h = Tiles.decodeHeight(t);
/* 2588 */           if (Tiles.decodeType(t) == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_MARSH.id) {
/*      */             
/* 2590 */             byte dir = getDiagonalDir(performer, tilex, tiley, act.getNumber());
/* 2591 */             Server.setSurfaceTile(tilex, tiley, h, Tiles.Tile.TILE_PLANKS.id, dir);
/* 2592 */             if (Tiles.decodeType(t) == Tiles.Tile.TILE_MARSH.id) {
/* 2593 */               performer.getCommunicator().sendNormalServerMessage("You cover parts of the marsh with boards.");
/*      */             } else {
/* 2595 */               performer.getCommunicator().sendNormalServerMessage("The ground has some fine floor boards now.");
/*      */             } 
/* 2597 */             Players.getInstance().sendChangedTiles(tilex, tiley, 1, 1, onSurface, true);
/*      */             
/*      */             try {
/* 2600 */               Items.decay(source.getWurmId(), source.getDbStrings());
/* 2601 */               act.setDestroyedItem(null);
/* 2602 */               Zone toCheckForChange = Zones.getZone(tilex, tiley, onSurface);
/* 2603 */               toCheckForChange.changeTile(tilex, tiley);
/*      */             }
/* 2605 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 2607 */               logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */             } 
/*      */           } else {
/*      */             
/* 2611 */             performer.getCommunicator().sendNormalServerMessage("The ground isn't fit for floor boards any longer, and your efforts are ruined.");
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 2617 */         done = true;
/* 2618 */         performer.getCommunicator().sendNormalServerMessage("The dirt isn't loose here. You have to cultivate it first.");
/*      */       } 
/*      */     } 
/*      */     
/* 2622 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean switchTileTypes(Creature performer, @NonNull Item source, int tilex, int tiley, float counter, Action act) {
/* 2628 */     boolean done = false;
/* 2629 */     int time = 50;
/* 2630 */     int digTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 2631 */     byte type = Tiles.decodeType(digTile);
/* 2632 */     Village vill = Zones.getVillage(tilex, tiley, performer.isOnSurface());
/*      */     
/* 2634 */     if (!isSwitchableTiles(source.getTemplateId(), type)) {
/*      */       
/* 2636 */       performer.getCommunicator().sendNormalServerMessage("You can no longer switch here.");
/* 2637 */       return true;
/*      */     } 
/* 2639 */     if (vill != null) {
/*      */       
/* 2641 */       if (!vill.isActionAllowed((short)927, performer)) {
/* 2642 */         return true;
/*      */       }
/*      */     } else {
/* 2645 */       if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */         
/* 2647 */         performer.getCommunicator().sendNormalServerMessage("You need to have a full weight " + source.getName() + " to switch the tile type.");
/* 2648 */         return true;
/*      */       } 
/* 2650 */       if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize) {
/*      */         
/* 2652 */         performer.getCommunicator().sendNormalServerMessage("You can't switch here.");
/* 2653 */         return true;
/*      */       } 
/* 2655 */       if (!performer.isOnSurface()) {
/*      */         
/* 2657 */         performer.getCommunicator().sendNormalServerMessage("You have to be on the surface to be able to do this.");
/* 2658 */         return true;
/*      */       } 
/* 2660 */     }  if (counter == 1.0F) {
/*      */       
/* 2662 */       Village v = Zones.getVillage(tilex, tiley, performer.isOnSurface());
/*      */       
/* 2664 */       if (v == null || v.isActionAllowed((short)927, performer))
/*      */       {
/* 2666 */         act.setTimeLeft(50);
/* 2667 */         performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/*      */       }
/*      */       else
/*      */       {
/* 2671 */         performer.getCommunicator().sendNormalServerMessage("You may not do that here.");
/* 2672 */         return true;
/*      */       }
/*      */     
/* 2675 */     } else if (counter * 10.0F > 50.0F && act.justTickedSecond()) {
/*      */       byte toSwitchTo;
/* 2677 */       performer.getStatus().modifyStamina(-10000.0F);
/*      */ 
/*      */       
/* 2680 */       switch (source.getTemplateId()) {
/*      */         
/*      */         case 26:
/* 2683 */           toSwitchTo = 5;
/*      */           break;
/*      */         case 298:
/* 2686 */           toSwitchTo = 1;
/*      */           break;
/*      */         default:
/* 2689 */           toSwitchTo = 1;
/* 2690 */           logger.warning("Reached DEFAULT case in SWITCH. TemplateID = " + source.getTemplateId() + ". Performer = " + performer.getName());
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 2695 */       source.setWeight(source.getWeightGrams() - source.getTemplate().getWeightGrams(), true);
/*      */ 
/*      */       
/* 2698 */       Server.surfaceMesh.setTile(tilex, tiley, 
/* 2699 */           Tiles.encode(Tiles.decodeHeight(digTile), toSwitchTo, (byte)0));
/* 2700 */       Server.modifyFlagsByTileType(tilex, tiley, toSwitchTo);
/*      */ 
/*      */       
/*      */       try {
/* 2704 */         Zone toCheckForChange = Zones.getZone(tilex, tiley, performer
/* 2705 */             .isOnSurface());
/* 2706 */         toCheckForChange.changeTile(tilex, tiley);
/*      */       }
/* 2708 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 2710 */         logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */       } 
/*      */       
/* 2713 */       Players.getInstance().sendChangedTile(tilex, tiley, performer
/* 2714 */           .isOnSurface(), true);
/*      */       
/* 2716 */       done = true;
/*      */     } 
/*      */     
/* 2719 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte getDiagonalDir(Creature performer, int tilex, int tiley, short action) {
/* 2724 */     if (action == 576 || action == 694 || action == 695) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2730 */       Vector2f pos = performer.getPos2f();
/* 2731 */       int digTilex = CoordUtils.WorldToTile(pos.x + 2.0F);
/* 2732 */       int digTiley = CoordUtils.WorldToTile(pos.y + 2.0F);
/*      */ 
/*      */       
/* 2735 */       if (tilex == digTilex && tiley == digTiley)
/* 2736 */         return Tiles.TileRoadDirection.DIR_NW.getCode(); 
/* 2737 */       if (tilex + 1 == digTilex && tiley == digTiley)
/* 2738 */         return Tiles.TileRoadDirection.DIR_NE.getCode(); 
/* 2739 */       if (tilex + 1 == digTilex && tiley + 1 == digTiley)
/* 2740 */         return Tiles.TileRoadDirection.DIR_SE.getCode(); 
/* 2741 */       if (tilex == digTilex && tiley + 1 == digTiley)
/* 2742 */         return Tiles.TileRoadDirection.DIR_SW.getCode(); 
/*      */     } 
/* 2744 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean tarFloor(Creature performer, Item source, int tilex, int tiley, boolean onSurface, int tile, float counter) {
/* 2750 */     boolean done = false;
/* 2751 */     if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize) {
/*      */       
/* 2753 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep here.");
/* 2754 */       done = true;
/*      */     }
/* 2756 */     else if (source.getTemplateId() != 153) {
/*      */       
/* 2758 */       performer.getCommunicator().sendNormalServerMessage("You need tar.");
/* 2759 */       done = true;
/*      */     }
/* 2761 */     else if (Tiles.decodeHeight(tile) < -100) {
/*      */       
/* 2763 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to tar here.");
/* 2764 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/* 2768 */       MeshIO mesh = onSurface ? Server.surfaceMesh : Server.caveMesh;
/* 2769 */       int digTile = mesh.getTile(tilex, tiley);
/* 2770 */       byte type = Tiles.decodeType(digTile);
/* 2771 */       Action act = null;
/*      */       
/*      */       try {
/* 2774 */         act = performer.getCurrentAction();
/*      */       }
/* 2776 */       catch (NoSuchActionException nsa) {
/*      */         
/* 2778 */         logger.log(Level.WARNING, nsa.getMessage(), (Throwable)nsa);
/* 2779 */         return true;
/*      */       } 
/* 2781 */       if (type == Tiles.Tile.TILE_PLANKS.id) {
/*      */         
/* 2783 */         done = false;
/*      */         
/* 2785 */         int time = 2000;
/* 2786 */         if (counter == 1.0F) {
/*      */           
/* 2788 */           Skills skills = performer.getSkills();
/* 2789 */           Skill paving = null;
/* 2790 */           if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */             
/* 2792 */             performer.getCommunicator().sendNormalServerMessage("The amount of tar is too little to do this. You may need to use another item.");
/*      */             
/* 2794 */             return true;
/*      */           } 
/*      */           
/*      */           try {
/* 2798 */             paving = skills.getSkill(10031);
/*      */           }
/* 2800 */           catch (Exception ex) {
/*      */             
/* 2802 */             paving = skills.learn(10031, 1.0F);
/*      */           } 
/* 2804 */           time = Actions.getStandardActionTime(performer, paving, source, 0.0D);
/* 2805 */           act.setTimeLeft(time);
/* 2806 */           performer.getCommunicator().sendNormalServerMessage("You start to put tar on the floorboards.");
/* 2807 */           Server.getInstance().broadCastAction(performer.getName() + " starts to put tar on the floorboards.", performer, 5);
/*      */ 
/*      */           
/* 2810 */           performer.sendActionControl("tarring", true, time);
/* 2811 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2816 */           time = act.getTimeLeft();
/* 2817 */           if (act.currentSecond() % 5 == 0)
/*      */           {
/* 2819 */             performer.getStatus().modifyStamina(-10000.0F);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 2824 */         if (counter * 10.0F > time) {
/*      */           
/* 2826 */           Skills skills = performer.getSkills();
/* 2827 */           Skill paving = null;
/*      */ 
/*      */           
/*      */           try {
/* 2831 */             paving = skills.getSkill(10031);
/*      */           }
/* 2833 */           catch (Exception ex) {
/*      */             
/* 2835 */             paving = skills.learn(10031, 1.0F);
/*      */           } 
/* 2837 */           if (paving != null)
/* 2838 */             paving.skillCheck(5.0D, source, 0.0D, false, counter); 
/* 2839 */           done = true;
/*      */           
/* 2841 */           int t = mesh.getTile(tilex, tiley);
/* 2842 */           short h = Tiles.decodeHeight(t);
/* 2843 */           if (Tiles.decodeType(t) == Tiles.Tile.TILE_PLANKS.id) {
/*      */ 
/*      */             
/* 2846 */             byte data = Tiles.decodeData(t);
/* 2847 */             mesh.setTile(tilex, tiley, Tiles.encode(h, Tiles.Tile.TILE_PLANKS_TARRED.id, data));
/* 2848 */             performer.getCommunicator().sendNormalServerMessage("The floor boards are well protected now.");
/* 2849 */             TileEvent.log(tilex, tiley, 0, performer.getWurmId(), act.getNumber());
/* 2850 */             Players.getInstance().sendChangedTiles(tilex, tiley, 1, 1, onSurface, true);
/*      */             
/*      */             try {
/* 2853 */               source.setWeight(source.getWeightGrams() - source.getTemplate().getWeightGrams(), true);
/*      */               
/* 2855 */               Zone toCheckForChange = Zones.getZone(tilex, tiley, onSurface);
/* 2856 */               toCheckForChange.changeTile(tilex, tiley);
/*      */             }
/* 2858 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 2860 */               logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */             } 
/*      */           } else {
/*      */             
/* 2864 */             performer.getCommunicator().sendNormalServerMessage("The ground doesn't consist of floor boards any longer.");
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 2870 */         done = true;
/* 2871 */         performer.getCommunicator().sendNormalServerMessage("The ground doesn't consist of floor boards any longer.");
/*      */       } 
/*      */     } 
/* 2874 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean cultivate(Creature performer, Item source, int tilex, int tiley, boolean onSurface, int tile, float counter) {
/* 2880 */     boolean done = false;
/* 2881 */     if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize || 
/* 2882 */       Tiles.decodeHeight(tile) < 0) {
/*      */       
/* 2884 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to cultivate here.");
/* 2885 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/* 2889 */       int digTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 2890 */       byte type = Tiles.decodeType(digTile);
/* 2891 */       Action act = null;
/*      */       
/*      */       try {
/* 2894 */         act = performer.getCurrentAction();
/*      */       }
/* 2896 */       catch (NoSuchActionException nsa) {
/*      */         
/* 2898 */         logger.log(Level.WARNING, nsa.getMessage(), (Throwable)nsa);
/* 2899 */         return true;
/*      */       } 
/* 2901 */       if (isCultivatable(type)) {
/*      */         
/* 2903 */         done = false;
/*      */         
/* 2905 */         int time = 2000;
/* 2906 */         if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2913 */           Skills skills = performer.getSkills();
/* 2914 */           Skill digging = null;
/*      */ 
/*      */           
/*      */           try {
/* 2918 */             digging = skills.getSkill(1009);
/*      */           }
/* 2920 */           catch (Exception ex) {
/*      */             
/* 2922 */             digging = skills.learn(1009, 1.0F);
/*      */           } 
/* 2924 */           time = Actions.getStandardActionTime(performer, digging, source, 0.0D);
/* 2925 */           act.setTimeLeft(time);
/* 2926 */           performer.getCommunicator().sendNormalServerMessage("You start to cultivate the soil.");
/* 2927 */           Server.getInstance().broadCastAction(performer.getName() + " starts to cultivate the soil.", performer, 5);
/* 2928 */           performer.sendActionControl(Actions.actionEntrys[318].getVerbString(), true, time);
/*      */           
/* 2930 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         }
/*      */         else {
/*      */           
/* 2934 */           time = act.getTimeLeft();
/* 2935 */           if (act.mayPlaySound()) {
/*      */             
/* 2937 */             performer.getStatus().modifyStamina(-10000.0F);
/* 2938 */             String sstring = "sound.work.digging1";
/* 2939 */             int x = Server.rand.nextInt(3);
/* 2940 */             if (x == 0) {
/* 2941 */               sstring = "sound.work.digging2";
/* 2942 */             } else if (x == 1) {
/* 2943 */               sstring = "sound.work.digging3";
/* 2944 */             }  Methods.sendSound(performer, sstring);
/*      */           } 
/* 2946 */           if (act.currentSecond() % 5 == 0)
/* 2947 */             source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier()); 
/*      */         } 
/* 2949 */         if (counter * 10.0F > time)
/*      */         {
/* 2951 */           Skills skills = performer.getSkills();
/* 2952 */           Skill digging = null;
/*      */ 
/*      */           
/*      */           try {
/* 2956 */             digging = skills.getSkill(1009);
/*      */           }
/* 2958 */           catch (Exception ex) {
/*      */             
/* 2960 */             digging = skills.learn(1009, 1.0F);
/*      */           } 
/* 2962 */           if (digging != null)
/* 2963 */             digging.skillCheck(14.0D, source, 0.0D, false, counter); 
/* 2964 */           done = true;
/*      */           
/* 2966 */           int t = Server.surfaceMesh.getTile(tilex, tiley);
/* 2967 */           short h = Tiles.decodeHeight(t);
/* 2968 */           if (isCultivatable(Tiles.decodeType(t))) {
/*      */             
/* 2970 */             Server.setSurfaceTile(tilex, tiley, h, Tiles.Tile.TILE_DIRT.id, (byte)0);
/* 2971 */             performer.getCommunicator().sendNormalServerMessage("The ground is cultivated and ready to sow now.");
/* 2972 */             Players.getInstance().sendChangedTiles(tilex, tiley, 1, 1, onSurface, true);
/*      */             
/*      */             try {
/* 2975 */               Zone toCheckForChange = Zones.getZone(tilex, tiley, onSurface);
/* 2976 */               toCheckForChange.changeTile(tilex, tiley);
/*      */             }
/* 2978 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 2980 */               logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */             } 
/*      */           } else {
/*      */             
/* 2984 */             performer.getCommunicator().sendNormalServerMessage("The ground isn't fit for cultivating any longer, and your efforts are ruined.");
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 2991 */         done = true;
/* 2992 */         performer.getCommunicator().sendNormalServerMessage("The soil isn't cultivatable here. You may have to pack it first.");
/*      */       } 
/*      */     } 
/*      */     
/* 2996 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean allCornersAtRockLevel(int tilex, int tiley, MeshIO mesh) {
/* 3001 */     int numberOfCornersAtRockHeight = 0;
/* 3002 */     for (int x = 0; x <= 1; x++) {
/*      */       
/* 3004 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 3006 */         int tile = mesh.getTile(tilex + x, tiley + y);
/* 3007 */         short tileHeight = Tiles.decodeHeight(tile);
/* 3008 */         int rockTile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 3009 */         short rockHeight = Tiles.decodeHeight(rockTile);
/* 3010 */         if (tileHeight <= rockHeight)
/*      */         {
/* 3012 */           numberOfCornersAtRockHeight++;
/*      */         }
/*      */       } 
/*      */     } 
/* 3016 */     return (numberOfCornersAtRockHeight == 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setAsRock(int tilex, int tiley, boolean natural) {
/* 3021 */     setAsRock(tilex, tiley, natural, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setAsRock(int tilex, int tiley, boolean natural, boolean lavaflow) {
/* 3026 */     MethodsHighways.removeNearbyMarkers(tilex, tiley, false);
/* 3027 */     boolean keepTopLeftHeight = false;
/* 3028 */     boolean keepTopRightHeight = false;
/* 3029 */     boolean keepLowerLeftHeight = false;
/* 3030 */     boolean keepLowerRightHeight = false;
/*      */     
/*      */     int x;
/* 3033 */     for (x = -1; x <= 1; x++) {
/*      */       
/* 3035 */       for (int y = -1; y <= 1; y++) {
/*      */         
/* 3037 */         int t = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 3038 */         byte type = Tiles.decodeType(t);
/* 3039 */         if (x == 0 && y == 0) {
/*      */ 
/*      */           
/* 3042 */           if (type == Tiles.Tile.TILE_CAVE_EXIT.id)
/*      */           {
/* 3044 */             if (lavaflow) {
/*      */               
/* 3046 */               Server.setSurfaceTile(tilex, tiley, 
/* 3047 */                   Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_LAVA.id, (byte)0);
/* 3048 */               Server.rockMesh.setTile(tilex, tiley, Tiles.encode(
/* 3049 */                     Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_CAVE_WALL_LAVA.id, (byte)0));
/*      */               
/* 3051 */               Server.setWorldResource(tilex, tiley, 0);
/* 3052 */               Server.setCaveResource(tilex, tiley, Server.rand.nextInt(10000));
/*      */             }
/*      */             else {
/*      */               
/* 3056 */               Server.setSurfaceTile(tilex, tiley, 
/* 3057 */                   Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_ROCK.id, (byte)0);
/* 3058 */               Server.rockMesh.setTile(tilex, tiley, Tiles.encode(
/* 3059 */                     Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_ROCK.id, (byte)0));
/* 3060 */               Server.setWorldResource(tilex, tiley, 0);
/* 3061 */               Server.setCaveResource(tilex, tiley, Server.rand.nextInt(10000));
/*      */             } 
/*      */             
/* 3064 */             MineDoorPermission.deleteMineDoor(x, y);
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 3069 */         else if (type == Tiles.Tile.TILE_CAVE.id || Tiles.isReinforcedFloor(type) || type == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */ 
/*      */           
/* 3072 */           if (x == -1 && y == -1) {
/*      */             
/* 3074 */             keepTopLeftHeight = true;
/*      */           }
/* 3076 */           else if (x == 0 && y == -1) {
/*      */             
/* 3078 */             keepTopRightHeight = true;
/* 3079 */             keepTopLeftHeight = true;
/*      */           }
/* 3081 */           else if (x == 1 && y == -1) {
/*      */             
/* 3083 */             keepTopRightHeight = true;
/*      */           }
/* 3085 */           else if (x == -1 && y == 0) {
/*      */             
/* 3087 */             keepTopLeftHeight = true;
/* 3088 */             keepLowerLeftHeight = true;
/*      */           }
/* 3090 */           else if (x == 1 && y == 0) {
/*      */             
/* 3092 */             keepTopRightHeight = true;
/* 3093 */             keepLowerRightHeight = true;
/*      */           }
/* 3095 */           else if (x == -1 && y == 1) {
/*      */             
/* 3097 */             keepLowerLeftHeight = true;
/*      */           }
/* 3099 */           else if (x == 0 && y == 1) {
/*      */             
/* 3101 */             keepLowerRightHeight = true;
/* 3102 */             keepLowerLeftHeight = true;
/*      */           }
/* 3104 */           else if (x == 1 && y == 1) {
/*      */             
/* 3106 */             keepLowerRightHeight = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3112 */     for (x = 0; x <= 1; x++) {
/*      */       
/* 3114 */       for (int y = 0; y <= 1; y++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3125 */         int encodedTile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 3126 */         if (x == 0 && y == 0) {
/*      */           
/* 3128 */           byte tileType = Tiles.Tile.TILE_CAVE_WALL.id;
/*      */ 
/*      */           
/* 3131 */           if (lavaflow || (
/* 3132 */             Tiles.decodeType(Server.surfaceMesh.getTile(tilex + x, tiley + y)) == Tiles.Tile.TILE_LAVA.id && (
/* 3133 */             Tiles.decodeData(Server.surfaceMesh.getTile(tilex + x, tiley + y)) & 0xFF) == 255)) {
/* 3134 */             tileType = Tiles.Tile.TILE_CAVE_WALL_LAVA.id;
/* 3135 */           } else if (natural) {
/* 3136 */             tileType = TileRockBehaviour.prospect(tilex + x, tiley + y, false);
/*      */           } 
/*      */           
/* 3139 */           if (keepTopLeftHeight) {
/* 3140 */             Server.caveMesh.setTile(tilex + x, tiley + y, 
/* 3141 */                 Tiles.encode(Tiles.decodeHeight(encodedTile), tileType, Tiles.decodeData(encodedTile)));
/*      */           } else {
/* 3143 */             Server.caveMesh.setTile(tilex + x, tiley + y, 
/* 3144 */                 Tiles.encode((short)-100, tileType, (byte)0));
/*      */           } 
/* 3146 */         } else if (x == 1 && y == 0) {
/*      */           
/* 3148 */           if (!keepTopRightHeight) {
/* 3149 */             Server.caveMesh.setTile(tilex + x, tiley + y, 
/*      */ 
/*      */                 
/* 3152 */                 Tiles.encode((short)-100, 
/* 3153 */                   Tiles.decodeType(Server.caveMesh.getTile(tilex + x, tiley + y)), (byte)0));
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 3158 */         else if (x == 0 && y == 1) {
/*      */           
/* 3160 */           if (!keepLowerLeftHeight) {
/* 3161 */             Server.caveMesh.setTile(tilex + x, tiley + y, 
/*      */ 
/*      */                 
/* 3164 */                 Tiles.encode((short)-100, 
/* 3165 */                   Tiles.decodeType(Server.caveMesh.getTile(tilex + x, tiley + y)), (byte)0));
/*      */           }
/* 3167 */         } else if (x == 1 && y == 1) {
/*      */           
/* 3169 */           if (!keepLowerRightHeight)
/* 3170 */             Server.caveMesh.setTile(tilex + x, tiley + y, 
/*      */ 
/*      */                 
/* 3173 */                 Tiles.encode((short)-100, 
/* 3174 */                   Tiles.decodeType(Server.caveMesh.getTile(tilex + x, tiley + y)), (byte)0)); 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3178 */     Players.getInstance().sendChangedTiles(tilex, tiley, 2, 2, true, true);
/* 3179 */     Players.getInstance().sendChangedTiles(tilex - 1, tiley - 1, 3, 3, false, true);
/* 3180 */     Server.setCaveResource(tilex, tiley, 65535);
/*      */     
/* 3182 */     byte block = 0;
/* 3183 */     r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 102533L);
/* 3184 */     if (r.nextInt(100) == 0) {
/* 3185 */       block = -1;
/*      */     } else {
/*      */       
/* 3188 */       r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 123307L);
/* 3189 */       if (r.nextInt(64) == 0)
/* 3190 */         block = -1; 
/*      */     } 
/* 3192 */     Zones.setMiningState(tilex, tiley, block, false);
/* 3193 */     Zones.deleteMiningTile(tilex, tiley);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void forceSetAsRock(int tilex, int tiley, byte suggestedTile, int suggestedTileSeed) {
/* 3198 */     suggestedTileSeed = Math.max(suggestedTileSeed, 1);
/* 3199 */     int surfaceTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 3200 */     boolean isLava = (Tiles.decodeType(surfaceTile) == Tiles.Tile.TILE_LAVA.id);
/*      */     
/* 3202 */     int initialTile = Server.caveMesh.getTile(tilex, tiley);
/* 3203 */     byte oldTType = Tiles.decodeType(initialTile);
/* 3204 */     byte newTileType = Tiles.isOreCave(oldTType) ? oldTType : Tiles.Tile.TILE_CAVE_WALL.id;
/* 3205 */     if (isLava) {
/* 3206 */       newTileType = Tiles.Tile.TILE_CAVE_WALL_LAVA.id;
/* 3207 */     } else if (suggestedTile > 0 && Server.rand.nextInt(suggestedTileSeed) == 0) {
/*      */       
/* 3209 */       logger.log(Level.INFO, "Setting " + tilex + "," + tiley + " to suggested " + (Tiles.getTile(suggestedTile)).tiledesc);
/* 3210 */       newTileType = suggestedTile;
/*      */     } 
/* 3212 */     boolean changed = (newTileType != oldTType);
/* 3213 */     if (changed) {
/*      */       
/* 3215 */       boolean keepTopLeftHeight = false;
/* 3216 */       boolean keepTopRightHeight = false;
/* 3217 */       boolean keepLowerLeftHeight = false;
/* 3218 */       boolean keepLowerRightHeight = false; int x;
/* 3219 */       for (x = -1; x <= 1; x++) {
/*      */         
/* 3221 */         for (int y = -1; y <= 1; y++) {
/*      */           
/* 3223 */           int t = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 3224 */           byte type = Tiles.decodeType(t);
/* 3225 */           if (x != 0 || y != 0)
/*      */           {
/* 3227 */             if (type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_EXIT.id || Tiles.isReinforcedFloor(type))
/*      */             {
/* 3229 */               if (x == -1 && y == -1) {
/*      */                 
/* 3231 */                 keepTopLeftHeight = true;
/*      */               }
/* 3233 */               else if (x == 0 && y == -1) {
/*      */                 
/* 3235 */                 keepTopRightHeight = true;
/* 3236 */                 keepTopLeftHeight = true;
/*      */               }
/* 3238 */               else if (x == 1 && y == -1) {
/*      */                 
/* 3240 */                 keepTopRightHeight = true;
/*      */               }
/* 3242 */               else if (x == -1 && y == 0) {
/*      */                 
/* 3244 */                 keepTopLeftHeight = true;
/* 3245 */                 keepLowerLeftHeight = true;
/*      */               }
/* 3247 */               else if (x == 1 && y == 0) {
/*      */                 
/* 3249 */                 keepTopRightHeight = true;
/* 3250 */                 keepLowerRightHeight = true;
/*      */               }
/* 3252 */               else if (x == -1 && y == 1) {
/*      */                 
/* 3254 */                 keepLowerLeftHeight = true;
/*      */               }
/* 3256 */               else if (x == 0 && y == 1) {
/*      */                 
/* 3258 */                 keepLowerRightHeight = true;
/* 3259 */                 keepLowerLeftHeight = true;
/*      */               }
/* 3261 */               else if (x == 1 && y == 1) {
/*      */                 
/* 3263 */                 keepLowerRightHeight = true;
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/* 3269 */       for (x = 0; x <= 1; x++) {
/*      */         
/* 3271 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 3273 */           int encodedTile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 3274 */           boolean send = false;
/* 3275 */           if (x == 0 && y == 0) {
/*      */             
/* 3277 */             if (keepTopLeftHeight)
/*      */             {
/* 3279 */               if (Tiles.decodeType(encodedTile) != newTileType)
/*      */               {
/* 3281 */                 Server.caveMesh.setTile(tilex + x, tiley + y, 
/*      */ 
/*      */                     
/* 3284 */                     Tiles.encode(Tiles.decodeHeight(encodedTile), newTileType, 
/* 3285 */                       Tiles.decodeData(encodedTile)));
/* 3286 */                 send = true;
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 3291 */             else if (Tiles.decodeHeight(encodedTile) != -100 || 
/* 3292 */               Tiles.decodeType(encodedTile) != newTileType || 
/* 3293 */               Tiles.decodeData(encodedTile) != 0)
/*      */             {
/* 3295 */               Server.caveMesh.setTile(tilex + x, tiley + y, 
/* 3296 */                   Tiles.encode((short)-100, newTileType, (byte)0));
/* 3297 */               send = true;
/*      */             }
/*      */           
/*      */           }
/* 3301 */           else if (x == 1 && y == 0) {
/*      */             
/* 3303 */             if (!keepTopRightHeight)
/*      */             {
/* 3305 */               if (Tiles.decodeHeight(encodedTile) != -100 || 
/* 3306 */                 Tiles.decodeData(encodedTile) != 0)
/*      */               {
/* 3308 */                 Server.caveMesh
/* 3309 */                   .setTile(tilex + x, tiley + y, Tiles.encode((short)-100, 
/* 3310 */                       Tiles.decodeType(encodedTile), (byte)0));
/* 3311 */                 send = true;
/*      */               }
/*      */             
/*      */             }
/* 3315 */           } else if (x == 0 && y == 1) {
/*      */             
/* 3317 */             if (!keepLowerLeftHeight)
/*      */             {
/* 3319 */               if (Tiles.decodeHeight(encodedTile) != -100 || 
/* 3320 */                 Tiles.decodeData(encodedTile) != 0)
/*      */               {
/* 3322 */                 Server.caveMesh
/* 3323 */                   .setTile(tilex + x, tiley + y, Tiles.encode((short)-100, 
/* 3324 */                       Tiles.decodeType(encodedTile), (byte)0));
/* 3325 */                 send = true;
/*      */               }
/*      */             
/*      */             }
/* 3329 */           } else if (x == 1 && y == 1) {
/*      */             
/* 3331 */             if (!keepLowerRightHeight)
/*      */             {
/* 3333 */               if (Tiles.decodeHeight(encodedTile) != -100 || 
/* 3334 */                 Tiles.decodeData(encodedTile) != 0) {
/*      */                 
/* 3336 */                 Server.caveMesh
/* 3337 */                   .setTile(tilex + x, tiley + y, Tiles.encode((short)-100, 
/* 3338 */                       Tiles.decodeType(encodedTile), (byte)0));
/* 3339 */                 send = true;
/*      */               } 
/*      */             }
/*      */           } 
/* 3343 */           if (send)
/*      */           {
/*      */ 
/*      */             
/* 3347 */             Players.getInstance().sendChangedTile(tilex + x, tiley + y, false, true);
/*      */           }
/*      */         } 
/*      */       } 
/* 3351 */       Server.setCaveResource(tilex, tiley, 65535);
/* 3352 */       Zones.setMiningState(tilex, tiley, (byte)-1, false);
/* 3353 */       Zones.deleteMiningTile(tilex, tiley);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void caveIn(int tilex, int tiley) {
/* 3359 */     setAsRock(tilex, tiley, true);
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
/*      */   public static boolean isAllCornersInsideHeightRange(int tilex, int tiley, boolean surfaced, short maxheight, short minheight) {
/* 3406 */     if (surfaced) {
/*      */       
/* 3408 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3410 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 3412 */           if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize)
/*      */           {
/* 3414 */             return true; } 
/* 3415 */           short h = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y));
/* 3416 */           if (minheight <= h && h <= maxheight) {
/* 3417 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 3423 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3425 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 3427 */           if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize)
/*      */           {
/* 3429 */             return true; } 
/* 3430 */           short h = Tiles.decodeHeight(Server.caveMesh.getTile(tilex + x, tiley + y));
/* 3431 */           if (minheight <= h || h <= maxheight)
/* 3432 */             return true; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3436 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCornerUnderWater(int tilex, int tiley, boolean surfaced) {
/* 3442 */     if (surfaced) {
/*      */       
/* 3444 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3446 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 3448 */           if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize)
/*      */           {
/* 3450 */             return true; } 
/* 3451 */           short h = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y));
/* 3452 */           if (h <= 0) {
/* 3453 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 3459 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3461 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 3463 */           if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize)
/*      */           {
/* 3465 */             return true; } 
/* 3466 */           short h = Tiles.decodeHeight(Server.caveMesh.getTile(tilex + x, tiley + y));
/* 3467 */           if (h <= 0)
/* 3468 */             return true; 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3472 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTileUnderWater(int tile, int tilex, int tiley, boolean surfaced) {
/* 3477 */     if (surfaced) {
/*      */       
/* 3479 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3481 */         for (int y = 0; y <= 1; y++)
/*      */         {
/* 3483 */           if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize)
/*      */           {
/* 3485 */             return true; } 
/* 3486 */           if (Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y)) > -0.5D)
/*      */           {
/* 3488 */             return false;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 3495 */       if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/* 3496 */         return false;
/*      */       }
/*      */       
/* 3499 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3501 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 3503 */           if (Tiles.decodeHeight(Server.caveMesh.getTile(tilex + x, tiley + y)) > -0.5D)
/*      */           {
/* 3505 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3511 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean isWater(int tile, int tilex, int tiley, boolean surfaced) {
/* 3516 */     if (surfaced) {
/*      */       
/* 3518 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3520 */         for (int y = 0; y <= 1; y++)
/*      */         {
/* 3522 */           if (Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y)) < 0)
/*      */           {
/* 3524 */             return true;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 3531 */       if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/* 3532 */         return false;
/*      */       }
/*      */       
/* 3535 */       for (int x = 0; x <= 1; x++) {
/*      */         
/* 3537 */         for (int y = 0; y <= 1; y++) {
/*      */           
/* 3539 */           int ttile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 3540 */           if (Tiles.decodeHeight(ttile) < 0)
/*      */           {
/* 3542 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3548 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFlat(int tilex, int tiley, boolean surfaced, int maxDifference) throws IllegalArgumentException {
/* 3554 */     int lAverageHeight = 0; int x;
/* 3555 */     for (x = 0; x <= 1; x++) {
/*      */       
/* 3557 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 3559 */         if (tilex + x < 0 || tilex + x > 1 << Constants.meshSize || tiley + y < 0 || tiley + y > 1 << Constants.meshSize)
/*      */         {
/*      */           
/* 3562 */           throw new IllegalArgumentException("This tile is at the end of the world. Don't flatten it.");
/*      */         }
/* 3564 */         short ch = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y));
/* 3565 */         if (!surfaced)
/* 3566 */           ch = Tiles.decodeHeight(Server.caveMesh.getTile(tilex + x, tiley + y)); 
/* 3567 */         lAverageHeight += ch;
/*      */       } 
/*      */     } 
/* 3570 */     lAverageHeight = (short)(lAverageHeight / 4);
/* 3571 */     for (x = 0; x <= 1; x++) {
/*      */       
/* 3573 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 3575 */         short h = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y));
/* 3576 */         if (!surfaced)
/* 3577 */           h = Tiles.decodeHeight(Server.caveMesh.getTile(tilex + x, tiley + y)); 
/* 3578 */         if (h > lAverageHeight + maxDifference)
/* 3579 */           return false; 
/* 3580 */         if (h < lAverageHeight - maxDifference)
/* 3581 */           return false; 
/*      */       } 
/*      */     } 
/* 3584 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean plantSprout(Creature performer, Item sprout, int tilex, int tiley, boolean onSurface, int tile, float counter, boolean inCenter) {
/* 3590 */     boolean toReturn = true;
/* 3591 */     if (sprout.getTemplateId() == 266) {
/*      */       
/* 3593 */       if (!onSurface) {
/*      */         
/* 3595 */         performer.getCommunicator().sendNormalServerMessage("The sprout would never grow inside a cave.");
/* 3596 */         return true;
/*      */       } 
/* 3598 */       byte type = Tiles.decodeType(tile);
/* 3599 */       if (isTileGrowTree(type)) {
/*      */         
/* 3601 */         if (!Methods.isActionAllowed(performer, (short)660, tilex, tiley)) {
/* 3602 */           return true;
/*      */         }
/* 3604 */         VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, onSurface);
/* 3605 */         if (vtile != null)
/*      */         {
/* 3607 */           if (vtile.getStructure() != null) {
/*      */             
/* 3609 */             if (vtile.getStructure().isTypeHouse()) {
/* 3610 */               performer.getCommunicator().sendNormalServerMessage("The sprout would never grow inside a house.");
/*      */             } else {
/* 3612 */               performer.getCommunicator().sendNormalServerMessage("The sprout would never grow under a bridge.");
/* 3613 */             }  return true;
/*      */           } 
/*      */         }
/*      */         
/*      */         try {
/* 3618 */           Action act = performer.getCurrentAction();
/* 3619 */           double power = 0.0D;
/* 3620 */           int time = 2000;
/*      */           
/* 3622 */           toReturn = false;
/* 3623 */           if (counter == 1.0F) {
/*      */             
/* 3625 */             Skill gardening = null;
/*      */             
/*      */             try {
/* 3628 */               gardening = performer.getSkills().getSkill(10045);
/*      */             }
/* 3630 */             catch (NoSuchSkillException nss) {
/*      */               
/* 3632 */               gardening = performer.getSkills().learn(10045, 1.0F);
/*      */             } 
/* 3634 */             if (isCornerUnderWater(tilex, tiley, onSurface)) {
/*      */               
/* 3636 */               performer.getCommunicator().sendNormalServerMessage("The ground is too moist here, so the sprout would rot.");
/*      */               
/* 3638 */               return true;
/*      */             } 
/* 3640 */             time = Actions.getStandardActionTime(performer, gardening, sprout, 0.0D);
/* 3641 */             act.setTimeLeft(time);
/* 3642 */             performer.getCommunicator().sendNormalServerMessage("You start planting the sprout.");
/* 3643 */             Server.getInstance().broadCastAction(performer.getName() + " starts to plant a sprout.", performer, 5);
/* 3644 */             performer.sendActionControl(Actions.actionEntrys[186].getVerbString(), true, time);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 3649 */             time = act.getTimeLeft();
/*      */           } 
/* 3651 */           if (counter * 10.0F > time)
/*      */           {
/* 3653 */             Skill gardening = null;
/*      */             
/*      */             try {
/* 3656 */               gardening = performer.getSkills().getSkill(10045);
/*      */             }
/* 3658 */             catch (NoSuchSkillException nss) {
/*      */               
/* 3660 */               gardening = performer.getSkills().learn(10045, 1.0F);
/*      */             } 
/* 3662 */             power = gardening.skillCheck((1.0F + sprout.getDamage()), sprout.getCurrentQualityLevel(), false, counter);
/* 3663 */             toReturn = true;
/* 3664 */             if (power > 0.0D && sprout.getMaterial() != 92) {
/*      */               
/* 3666 */               SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, onSurface, 0.0F);
/*      */               
/* 3668 */               TreeData.TreeType treeType = Materials.getTreeTypeForWood(sprout.getMaterial());
/* 3669 */               if (treeType != null) {
/*      */ 
/*      */                 
/* 3672 */                 int newData = Tiles.encodeTreeData(FoliageAge.YOUNG_ONE, false, inCenter, GrassData.GrowthTreeStage.SHORT);
/*      */                 
/* 3674 */                 if (type == Tiles.Tile.TILE_MYCELIUM.id) {
/* 3675 */                   Server.setSurfaceTile(tilex, tiley, 
/* 3676 */                       Tiles.decodeHeight(tile), treeType.asMyceliumTree(), (byte)newData);
/*      */                 } else {
/* 3678 */                   Server.setSurfaceTile(tilex, tiley, 
/* 3679 */                       Tiles.decodeHeight(tile), treeType.asNormalTree(), (byte)newData);
/*      */                 } 
/*      */               } else {
/*      */                 
/* 3683 */                 BushData.BushType bushType = Materials.getBushTypeForWood(sprout.getMaterial());
/*      */                 
/* 3685 */                 int newData = Tiles.encodeTreeData(FoliageAge.YOUNG_ONE, false, inCenter, GrassData.GrowthTreeStage.SHORT);
/*      */                 
/* 3687 */                 if (type == Tiles.Tile.TILE_MYCELIUM.id) {
/* 3688 */                   Server.setSurfaceTile(tilex, tiley, 
/* 3689 */                       Tiles.decodeHeight(tile), bushType.asMyceliumBush(), (byte)newData);
/*      */                 } else {
/* 3691 */                   Server.setSurfaceTile(tilex, tiley, 
/* 3692 */                       Tiles.decodeHeight(tile), bushType.asNormalBush(), (byte)newData);
/*      */                 } 
/* 3694 */               }  Server.setWorldResource(tilex, tiley, 0);
/* 3695 */               performer.getMovementScheme().touchFreeMoveCounter();
/* 3696 */               Players.getInstance().sendChangedTile(tilex, tiley, onSurface, true);
/* 3697 */               if (performer.getDeity() != null && (performer.getDeity()).number == 1)
/*      */               {
/* 3699 */                 performer.maybeModifyAlignment(1.0F);
/*      */               }
/* 3701 */               String tosend = "You plant the sprout.";
/* 3702 */               performer.achievement(119);
/* 3703 */               double gard = gardening.getKnowledge(0.0D);
/* 3704 */               if (gard > 50.0D)
/*      */               {
/* 3706 */                 if (gard < 60.0D) {
/* 3707 */                   tosend = "You plant the sprout, and you can almost feel it start sucking nutrition from the earth.";
/* 3708 */                 } else if (gard < 70.0D) {
/* 3709 */                   tosend = "You plant the sprout, and you get a weird feeling that the plant thanks you.";
/* 3710 */                 } else if (gard < 80.0D) {
/* 3711 */                   tosend = "You plant the sprout, and you see the plant perform an almost unnoticable bow as it whispers its thanks.";
/* 3712 */                 } else if (gard < 100.0D) {
/* 3713 */                   tosend = "You plant the sprout. As you see the plant bow you hear the voice in your head of hundreds of plants thanking you.";
/*      */                 }  } 
/* 3715 */               performer.getStatus().modifyStamina(-1000.0F);
/* 3716 */               performer.getCommunicator().sendNormalServerMessage(tosend);
/* 3717 */               Server.getInstance().broadCastAction(performer.getName() + " plants a sprout.", performer, 5);
/*      */             } else {
/*      */               
/* 3720 */               performer.getCommunicator().sendNormalServerMessage("Sadly, the sprout does not survive despite your best efforts.");
/*      */             } 
/* 3722 */             Items.destroyItem(sprout.getWurmId());
/*      */           }
/*      */         
/* 3725 */         } catch (NoSuchActionException nsa) {
/*      */           
/* 3727 */           logger.log(Level.WARNING, performer.getName() + ": " + nsa.getMessage(), (Throwable)nsa);
/*      */         } 
/*      */       } else {
/*      */         
/* 3731 */         performer.getCommunicator().sendNormalServerMessage("You cannot plant a tree there.");
/*      */       } 
/*      */     } else {
/*      */       
/* 3735 */       performer.getCommunicator().sendNormalServerMessage("You need to plant with a sprout, not a " + sprout
/* 3736 */           .getName() + ".");
/*      */     } 
/* 3738 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean createMagicWall(Creature performer, Item source, int tilex, int tiley, int heightOffset, boolean onSurface, Tiles.TileBorderDirection dir, float counter, Action act) {
/* 3744 */     boolean toReturn = true;
/* 3745 */     if (source.getTemplateId() == 764) {
/*      */       
/* 3747 */       if (source.getWeightGrams() < 1000) {
/*      */         
/* 3749 */         performer.getCommunicator().sendNormalServerMessage("There is too little " + source.getName() + ". You probably will need at least " + 'Ϩ' + " g.");
/* 3750 */         return true;
/*      */       } 
/* 3752 */       if (!onSurface) {
/*      */         
/* 3754 */         performer.getCommunicator().sendNormalServerMessage("You can not reach that.");
/* 3755 */         return true;
/*      */       } 
/* 3757 */       VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, onSurface);
/* 3758 */       if (vtile != null)
/*      */       {
/* 3760 */         if (vtile.getStructure() != null) {
/*      */           
/* 3762 */           performer.getCommunicator().sendNormalServerMessage("The source would never work inside.");
/* 3763 */           return true;
/*      */         } 
/*      */       }
/*      */       
/* 3767 */       double power = 0.0D;
/* 3768 */       int time = 2000;
/*      */       
/* 3770 */       toReturn = false;
/* 3771 */       if (counter == 1.0F) {
/*      */         
/* 3773 */         VolaTile t = Zones.getTileOrNull(tilex, tiley, onSurface);
/* 3774 */         if (t != null) {
/*      */           
/* 3776 */           Wall[] walls = t.getWallsForLevel(heightOffset / 30);
/* 3777 */           for (Wall wall : walls) {
/*      */             
/* 3779 */             if (wall.isHorizontal() == ((dir == Tiles.TileBorderDirection.DIR_HORIZ)))
/*      */             {
/* 3781 */               if (wall.getStartX() == tilex && wall.getStartY() == tiley)
/* 3782 */                 return true; 
/*      */             }
/*      */           } 
/* 3785 */           Fence[] fences = t.getFencesForDir(dir);
/* 3786 */           for (Fence f : fences) {
/*      */             
/* 3788 */             if (f.getHeightOffset() == heightOffset)
/*      */             {
/* 3790 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 3794 */         if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*      */           
/* 3796 */           VolaTile t1 = Zones.getTileOrNull(tilex, tiley, onSurface);
/* 3797 */           if (t1 != null)
/* 3798 */             for (Creature c : t1.getCreatures()) {
/*      */               
/* 3800 */               if (c.isPlayer())
/* 3801 */                 return true; 
/*      */             }  
/* 3803 */           VolaTile t2 = Zones.getTileOrNull(tilex - 1, tiley, onSurface);
/* 3804 */           if (t2 != null) {
/* 3805 */             for (Creature c : t2.getCreatures()) {
/*      */               
/* 3807 */               if (c.isPlayer()) {
/* 3808 */                 return true;
/*      */               }
/*      */             } 
/*      */           }
/*      */         } else {
/* 3813 */           VolaTile t1 = Zones.getTileOrNull(tilex, tiley, onSurface);
/* 3814 */           if (t1 != null)
/* 3815 */             for (Creature c : t1.getCreatures()) {
/*      */               
/* 3817 */               if (c.isPlayer())
/* 3818 */                 return false; 
/*      */             }  
/* 3820 */           VolaTile t2 = Zones.getTileOrNull(tilex, tiley - 1, onSurface);
/* 3821 */           if (t2 != null)
/* 3822 */             for (Creature c : t2.getCreatures()) {
/*      */               
/* 3824 */               if (c.isPlayer())
/* 3825 */                 return false; 
/*      */             }  
/*      */         } 
/* 3828 */         Skill mind = null;
/*      */         
/*      */         try {
/* 3831 */           mind = performer.getSkills().getSkill(100);
/*      */         }
/* 3833 */         catch (NoSuchSkillException nss) {
/*      */           
/* 3835 */           mind = performer.getSkills().learn(100, 1.0F);
/*      */         } 
/* 3837 */         time = Actions.getQuickActionTime(performer, mind, source, 0.0D);
/* 3838 */         act.setTimeLeft(time);
/* 3839 */         performer.getCommunicator().sendNormalServerMessage("You start to weave the source.");
/* 3840 */         Server.getInstance().broadCastAction(performer.getName() + " starts to weave the source.", performer, 5);
/* 3841 */         performer.sendActionControl(Actions.actionEntrys[512].getVerbString(), true, time);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 3846 */         time = act.getTimeLeft();
/*      */       } 
/* 3848 */       if (counter * 10.0F > time)
/*      */       {
/* 3850 */         Skill mind = null;
/*      */         
/*      */         try {
/* 3853 */           mind = performer.getSkills().getSkill(100);
/*      */         }
/* 3855 */         catch (NoSuchSkillException nss) {
/*      */           
/* 3857 */           mind = performer.getSkills().learn(100, 1.0F);
/*      */         } 
/* 3859 */         power = mind.skillCheck(mind.getRealKnowledge(), source.getCurrentQualityLevel(), false, counter);
/* 3860 */         toReturn = true;
/* 3861 */         if (power > 0.0D) {
/*      */           
/* 3863 */           SoundPlayer.playSound("sound.religion.channel", tilex, tiley, onSurface, 0.0F);
/*      */           
/*      */           try {
/* 3866 */             Zone zone = Zones.getZone(tilex, tiley, true);
/*      */             
/* 3868 */             DbFence dbFence = new DbFence(StructureConstantsEnum.FENCE_MAGIC_STONE, tilex, tiley, heightOffset, 1.0F, dir, zone.getId(), performer.getLayer());
/* 3869 */             dbFence.setState(dbFence.getFinishState());
/* 3870 */             dbFence.setQualityLevel((float)power);
/* 3871 */             dbFence.improveOrigQualityLevel((float)power);
/* 3872 */             zone.addFence((Fence)dbFence);
/* 3873 */             performer.achievement(320);
/* 3874 */             performer.getCommunicator().sendNormalServerMessage("You weave the source and create a wall.");
/* 3875 */             Server.getInstance().broadCastAction(performer.getName() + " creates a wall.", performer, 5);
/*      */           }
/* 3877 */           catch (NoSuchZoneException noSuchZoneException) {}
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 3883 */           performer.getCommunicator().sendNormalServerMessage("Sadly, you fail to weave the source properly.");
/* 3884 */         }  source.setWeight(source.getWeightGrams() - 1000, true);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 3889 */       performer.getCommunicator().sendNormalServerMessage("You need to use the source, not a " + source
/* 3890 */           .getName() + ".");
/*      */     } 
/* 3892 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean plantFlowerbed(Creature performer, Item flower, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, float counter, Action act) {
/* 3898 */     boolean toReturn = true;
/* 3899 */     if (flower.isFlower()) {
/*      */       
/* 3901 */       if (!onSurface) {
/*      */         
/* 3903 */         performer.getCommunicator().sendNormalServerMessage("You can not reach that.");
/* 3904 */         performer.getCommunicator().sendActionResult(false);
/* 3905 */         return true;
/*      */       } 
/* 3907 */       int tile = Server.surfaceMesh.getTile(tilex, tiley);
/* 3908 */       byte type = Tiles.decodeType(tile);
/* 3909 */       int diffx = 0;
/* 3910 */       int diffy = 0;
/* 3911 */       if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/* 3912 */         diffx = -1;
/*      */       } else {
/* 3914 */         diffy = -1;
/* 3915 */       }  int tile2 = Server.surfaceMesh.getTile(tilex + diffx, tiley + diffy);
/* 3916 */       byte type2 = Tiles.decodeType(tile2);
/* 3917 */       if (isTileGrowHedge(type) || isTileGrowHedge(type2)) {
/*      */         
/* 3919 */         VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, onSurface);
/* 3920 */         if (vtile != null)
/*      */         {
/* 3922 */           if (vtile.getStructure() != null) {
/*      */             
/* 3924 */             performer.getCommunicator().sendNormalServerMessage("The flowers would never grow inside.");
/* 3925 */             performer.getCommunicator().sendActionResult(false);
/* 3926 */             return true;
/*      */           } 
/*      */         }
/*      */         
/* 3930 */         double power = 0.0D;
/* 3931 */         int time = 2000;
/*      */         
/* 3933 */         toReturn = false;
/* 3934 */         if (counter == 1.0F) {
/*      */           
/* 3936 */           StructureConstantsEnum fenceType = Fence.getFlowerbedType(flower.getTemplateId());
/* 3937 */           if (fenceType == StructureConstantsEnum.FENCE_PLAN_WOODEN) {
/*      */             
/* 3939 */             performer.getCommunicator().sendNormalServerMessage("Nobody has managed to grow those in flowerbeds yet.");
/*      */             
/* 3941 */             performer.getCommunicator().sendActionResult(false);
/* 3942 */             return true;
/*      */           } 
/* 3944 */           int found = 0;
/* 3945 */           boolean flowersFound = false;
/* 3946 */           boolean planksFound = false;
/* 3947 */           boolean nailsFound = false;
/* 3948 */           boolean dirtFound = false;
/* 3949 */           int plankCount = 0;
/* 3950 */           Item[] inventoryItems = performer.getInventory().getAllItems(false);
/* 3951 */           for (Item item : inventoryItems) {
/*      */             
/* 3953 */             if (item.getTemplateId() == flower.getTemplateId() && !flowersFound) {
/*      */               
/* 3955 */               if (item != flower) {
/*      */                 
/* 3957 */                 found++;
/* 3958 */                 if (found >= 4) {
/* 3959 */                   flowersFound = true;
/*      */                 }
/*      */               } 
/* 3962 */             } else if (item.getTemplateId() == 22 && !planksFound) {
/*      */               
/* 3964 */               plankCount++;
/* 3965 */               if (plankCount >= 3) {
/* 3966 */                 planksFound = true;
/*      */               }
/* 3968 */             } else if (item.getTemplateId() == 26 && !dirtFound) {
/*      */               
/* 3970 */               if (item.getWeightGrams() >= item.getTemplate().getWeightGrams()) {
/* 3971 */                 dirtFound = true;
/*      */               }
/* 3973 */             } else if (item.getTemplateId() == 218 && !nailsFound) {
/*      */               
/* 3975 */               nailsFound = true;
/*      */             } 
/*      */             
/* 3978 */             if (flowersFound && planksFound && nailsFound && dirtFound)
/*      */               break; 
/*      */           } 
/* 3981 */           if (!flowersFound || !planksFound || !nailsFound || !dirtFound) {
/*      */             
/* 3983 */             performer
/* 3984 */               .getCommunicator()
/* 3985 */               .sendNormalServerMessage("You need to have at least 5 flowers of the same kind and 3 planks, 1 small nails and atleast 20kg of dirt in your inventory.");
/*      */             
/* 3987 */             performer.getCommunicator().sendActionResult(false);
/* 3988 */             return true;
/*      */           } 
/* 3990 */           Skill gardening = null;
/*      */           
/*      */           try {
/* 3993 */             gardening = performer.getSkills().getSkill(10045);
/*      */           }
/* 3995 */           catch (NoSuchSkillException nss) {
/*      */             
/* 3997 */             gardening = performer.getSkills().learn(10045, 1.0F);
/*      */           } 
/* 3999 */           if (isCornerUnderWater(tilex, tiley, onSurface)) {
/*      */             
/* 4001 */             performer.getCommunicator().sendNormalServerMessage("The ground is too moist here, so the flowers would rot.");
/*      */             
/* 4003 */             performer.getCommunicator().sendActionResult(false);
/* 4004 */             return true;
/*      */           } 
/* 4006 */           time = Actions.getStandardActionTime(performer, gardening, flower, 0.0D);
/* 4007 */           act.setTimeLeft(time);
/* 4008 */           performer.getCommunicator().sendNormalServerMessage("You start planting the flowers.");
/* 4009 */           Server.getInstance().broadCastAction(performer.getName() + " starts to plant some flowers.", performer, 5);
/* 4010 */           performer.sendActionControl(Actions.actionEntrys[563].getVerbString(), true, time);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 4015 */           time = act.getTimeLeft();
/*      */         } 
/* 4017 */         if (counter * 10.0F > time)
/*      */         {
/* 4019 */           StructureConstantsEnum fenceType = Fence.getFlowerbedType(flower.getTemplateId());
/* 4020 */           if (fenceType == StructureConstantsEnum.FENCE_PLAN_WOODEN) {
/*      */             
/* 4022 */             performer.getCommunicator().sendNormalServerMessage("Nobody has managed to grow those in flowerbeds yet.");
/*      */             
/* 4024 */             performer.getCommunicator().sendActionResult(false);
/* 4025 */             return true;
/*      */           } 
/* 4027 */           int found = 0;
/* 4028 */           boolean flowersFound = false;
/* 4029 */           boolean planksFound = false;
/* 4030 */           boolean nailsFound = false;
/* 4031 */           boolean dirtFound = false;
/* 4032 */           int plankCount = 0;
/* 4033 */           Item[] inventoryItems = performer.getInventory().getAllItems(false);
/* 4034 */           for (Item item : inventoryItems) {
/*      */             
/* 4036 */             if (item.getTemplateId() == flower.getTemplateId() && !flowersFound) {
/*      */               
/* 4038 */               if (item != flower) {
/*      */                 
/* 4040 */                 found++;
/* 4041 */                 if (found >= 4) {
/* 4042 */                   flowersFound = true;
/*      */                 }
/*      */               } 
/* 4045 */             } else if (item.getTemplateId() == 22 && !planksFound) {
/*      */               
/* 4047 */               plankCount++;
/* 4048 */               if (plankCount >= 3) {
/* 4049 */                 planksFound = true;
/*      */               }
/* 4051 */             } else if (item.getTemplateId() == 26 && !dirtFound) {
/*      */               
/* 4053 */               if (item.getWeightGrams() >= item.getTemplate().getWeightGrams()) {
/* 4054 */                 dirtFound = true;
/*      */               }
/* 4056 */             } else if (item.getTemplateId() == 218 && !nailsFound) {
/*      */               
/* 4058 */               nailsFound = true;
/*      */             } 
/*      */             
/* 4061 */             if (flowersFound && planksFound && nailsFound && dirtFound)
/*      */               break; 
/*      */           } 
/* 4064 */           if (found < 4) {
/*      */             
/* 4066 */             performer
/* 4067 */               .getCommunicator()
/* 4068 */               .sendNormalServerMessage("You need to have at least 5 flowers of the same kind and 3 planks, 1 small nails and atleast 20kg of dirt in your inventory.");
/*      */             
/* 4070 */             performer.getCommunicator().sendActionResult(false);
/* 4071 */             return true;
/*      */           } 
/* 4073 */           float ql = flower.getQualityLevel();
/* 4074 */           float dam = flower.getDamage();
/* 4075 */           boolean flowersDone = false;
/* 4076 */           boolean dirtDone = false;
/* 4077 */           boolean planksDone = false;
/* 4078 */           boolean nailsDone = false;
/* 4079 */           for (Item item : inventoryItems) {
/*      */             
/* 4081 */             if (item.getTemplateId() == flower.getTemplateId() && !flowersDone) {
/*      */               
/* 4083 */               if (item != flower)
/*      */               {
/* 4085 */                 if (found > 0) {
/*      */                   
/* 4087 */                   ql += item.getQualityLevel();
/* 4088 */                   dam += item.getDamage();
/* 4089 */                   found--;
/* 4090 */                   if (found <= 0) {
/* 4091 */                     flowersDone = true;
/*      */                   }
/*      */                 } 
/*      */               }
/* 4095 */             } else if (item.getTemplateId() == 22 && !planksDone) {
/*      */               
/* 4097 */               if (plankCount > 0) {
/*      */                 
/* 4099 */                 ql += item.getQualityLevel();
/* 4100 */                 dam += item.getDamage();
/* 4101 */                 plankCount--;
/* 4102 */                 if (plankCount <= 0) {
/* 4103 */                   planksDone = true;
/*      */                 }
/*      */               } 
/* 4106 */             } else if (item.getTemplateId() == 218 && !nailsDone) {
/*      */               
/* 4108 */               ql += item.getQualityLevel();
/* 4109 */               dam += item.getDamage();
/* 4110 */               nailsDone = true;
/*      */             }
/* 4112 */             else if (item.getTemplateId() == 26 && !dirtDone) {
/*      */               
/* 4114 */               ql += item.getQualityLevel();
/* 4115 */               dam += item.getDamage();
/* 4116 */               dirtDone = true;
/*      */             } 
/* 4118 */             if (flowersDone && dirtDone && planksDone && nailsDone) {
/*      */               break;
/*      */             }
/*      */           } 
/* 4122 */           ql /= 10.0F;
/* 4123 */           dam /= 10.0F;
/* 4124 */           Skill gardening = null;
/*      */           
/*      */           try {
/* 4127 */             gardening = performer.getSkills().getSkill(10045);
/*      */           }
/* 4129 */           catch (NoSuchSkillException nss) {
/*      */             
/* 4131 */             gardening = performer.getSkills().learn(10045, 1.0F);
/*      */           } 
/* 4133 */           power = gardening.skillCheck((1.0F + dam), ql, false, counter);
/* 4134 */           toReturn = true;
/* 4135 */           if (power > 0.0D) {
/*      */             
/* 4137 */             SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, onSurface, 0.0F);
/*      */             
/*      */             try {
/* 4140 */               Zone zone = Zones.getZone(tilex, tiley, true);
/*      */ 
/*      */               
/* 4143 */               DbFence dbFence = new DbFence(Fence.getFlowerbedType(flower.getTemplateId()), tilex, tiley, 0, 1.0F, dir, zone.getId(), performer.getLayer());
/*      */               
/*      */               try {
/* 4146 */                 dbFence.setState(dbFence.getFinishState());
/* 4147 */                 dbFence.setQualityLevel((float)power);
/* 4148 */                 dbFence.improveOrigQualityLevel((float)power);
/* 4149 */                 dbFence.save();
/* 4150 */                 zone.addFence((Fence)dbFence);
/*      */               }
/* 4152 */               catch (IOException iox) {
/*      */                 
/* 4154 */                 logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */               } 
/*      */               
/* 4157 */               if (performer.getDeity() != null && (performer.getDeity()).number == 1)
/*      */               {
/* 4159 */                 performer.maybeModifyAlignment(1.0F);
/*      */               }
/* 4161 */               found = 4;
/* 4162 */               plankCount = 3;
/* 4163 */               dirtDone = false;
/* 4164 */               planksDone = false;
/* 4165 */               flowersDone = false;
/* 4166 */               nailsDone = false;
/* 4167 */               for (Item item : inventoryItems) {
/*      */                 
/* 4169 */                 if (item.getTemplateId() == flower.getTemplateId() && !flowersDone) {
/*      */                   
/* 4171 */                   if (item != flower)
/*      */                   {
/* 4173 */                     if (found > 0) {
/*      */                       
/* 4175 */                       Items.destroyItem(item.getWurmId());
/* 4176 */                       found--;
/* 4177 */                       if (found <= 0) {
/* 4178 */                         flowersDone = true;
/*      */                       }
/*      */                     } 
/*      */                   }
/* 4182 */                 } else if (item.getTemplateId() == 26 && !dirtDone) {
/*      */                   
/* 4184 */                   Items.destroyItem(item.getWurmId());
/* 4185 */                   dirtDone = true;
/*      */                 }
/* 4187 */                 else if (item.getTemplateId() == 22 && !planksDone) {
/*      */                   
/* 4189 */                   Items.destroyItem(item.getWurmId());
/* 4190 */                   plankCount--;
/* 4191 */                   if (plankCount <= 0) {
/* 4192 */                     planksDone = true;
/*      */                   }
/* 4194 */                 } else if (item.getTemplateId() == 218 && !nailsDone) {
/*      */                   
/* 4196 */                   Items.destroyItem(item.getWurmId());
/* 4197 */                   nailsDone = true;
/*      */                 } 
/*      */                 
/* 4200 */                 if (flowersDone && dirtDone && planksDone && nailsDone)
/*      */                   break; 
/*      */               } 
/* 4203 */               String tosend = "You plant the flowers and create a fine flowerbed.";
/* 4204 */               performer.achievement(318);
/* 4205 */               double gard = gardening.getKnowledge(0.0D);
/* 4206 */               if (gard > 50.0D)
/*      */               {
/* 4208 */                 if (Server.rand.nextBoolean())
/*      */                 {
/* 4210 */                   if (gard < 60.0D) {
/* 4211 */                     tosend = "You plant the flowerbed, and you can almost feel the plants start sucking nutrition from the earth.";
/* 4212 */                   } else if (gard < 70.0D) {
/* 4213 */                     tosend = "You plant the flowerbed, and you get a weird feeling that the plants thanks you.";
/* 4214 */                   } else if (gard < 80.0D) {
/* 4215 */                     tosend = "You plant the flowerbed, and you see the plants perform an almost unnoticable bow as they whisper their thanks.";
/* 4216 */                   } else if (gard < 100.0D) {
/*      */                     
/* 4218 */                     tosend = "You plant the flowerbed. As you see the plants bow you hear the voice in your head of hundreds of other plants thanking you.";
/* 4219 */                     performer.getStatus().modifyStamina(-1000.0F);
/*      */                   } 
/*      */                 }
/*      */               }
/*      */ 
/*      */               
/* 4225 */               TileEvent.log(dbFence.getTileX(), dbFence.getTileY(), 0, performer.getWurmId(), 563);
/* 4226 */               performer.getCommunicator().sendNormalServerMessage(tosend);
/* 4227 */               Server.getInstance().broadCastAction(performer.getName() + " plants a flowerbed.", performer, 5);
/* 4228 */               performer.getCommunicator().sendActionResult(true);
/*      */             }
/* 4230 */             catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 4237 */             performer.getCommunicator().sendNormalServerMessage("Sadly, the flowers do not survive despite your best efforts.");
/*      */             
/* 4239 */             performer.getCommunicator().sendActionResult(false);
/*      */           } 
/* 4241 */           Items.destroyItem(flower.getWurmId());
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4246 */         performer.getCommunicator().sendNormalServerMessage("You cannot plant a flowerbed there.");
/* 4247 */         performer.getCommunicator().sendActionResult(false);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 4252 */       performer.getCommunicator().sendNormalServerMessage("You need to plant with a flower, not a " + flower
/* 4253 */           .getName() + ".");
/* 4254 */       performer.getCommunicator().sendActionResult(false);
/*      */     } 
/* 4256 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean plantHedge(Creature performer, Item sprout, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, float counter, Action act) {
/* 4262 */     boolean toReturn = true;
/* 4263 */     if (sprout.getTemplateId() == 266) {
/*      */       
/* 4265 */       if (!onSurface || !performer.isOnSurface()) {
/*      */         
/* 4267 */         performer.getCommunicator().sendNormalServerMessage("The hedge would never grow inside a cave.");
/* 4268 */         performer.getCommunicator().sendActionResult(false);
/* 4269 */         return true;
/*      */       } 
/* 4271 */       int tile = Server.surfaceMesh.getTile(tilex, tiley);
/* 4272 */       byte type = Tiles.decodeType(tile);
/* 4273 */       int diffx = 0;
/* 4274 */       int diffy = 0;
/* 4275 */       if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/* 4276 */         diffx = -1;
/*      */       } else {
/* 4278 */         diffy = -1;
/* 4279 */       }  int tile2 = Server.surfaceMesh.getTile(tilex + diffx, tiley + diffy);
/* 4280 */       byte type2 = Tiles.decodeType(tile2);
/* 4281 */       if (isTileGrowHedge(type) || isTileGrowHedge(type2)) {
/*      */         
/* 4283 */         if (!Methods.isActionAllowed(performer, (short)660, tilex, tiley)) {
/* 4284 */           return true;
/*      */         }
/* 4286 */         byte treeMaterial = sprout.getMaterial();
/*      */         
/* 4288 */         double power = 0.0D;
/* 4289 */         int time = 2000;
/*      */         
/* 4291 */         toReturn = false;
/* 4292 */         if (counter == 1.0F) {
/*      */           
/* 4294 */           StructureConstantsEnum fenceType = Fence.getLowHedgeType(treeMaterial);
/* 4295 */           if (fenceType == StructureConstantsEnum.FENCE_PLAN_WOODEN) {
/*      */             
/* 4297 */             performer.getCommunicator().sendNormalServerMessage("Nobody has managed to grow those in hedges yet.");
/* 4298 */             performer.getCommunicator().sendActionResult(false);
/* 4299 */             return true;
/*      */           } 
/* 4301 */           int found = 0;
/* 4302 */           Item[] inventoryItems = performer.getInventory().getAllItems(false);
/* 4303 */           for (Item item : inventoryItems) {
/*      */             
/* 4305 */             if (item.getTemplateId() == 266)
/*      */             {
/* 4307 */               if (item != sprout)
/*      */               {
/* 4309 */                 if (item.getMaterial() == treeMaterial) {
/*      */                   
/* 4311 */                   found++;
/* 4312 */                   if (found >= 4)
/*      */                     break; 
/*      */                 } 
/*      */               }
/*      */             }
/*      */           } 
/* 4318 */           if (found < 4) {
/*      */             
/* 4320 */             performer.getCommunicator().sendNormalServerMessage("You need to have at least 5 sprouts of the same kind in your inventory.");
/*      */             
/* 4322 */             performer.getCommunicator().sendActionResult(false);
/* 4323 */             return true;
/*      */           } 
/* 4325 */           Skill gardening = null;
/*      */           
/*      */           try {
/* 4328 */             gardening = performer.getSkills().getSkill(10045);
/*      */           }
/* 4330 */           catch (NoSuchSkillException nss) {
/*      */             
/* 4332 */             gardening = performer.getSkills().learn(10045, 1.0F);
/*      */           } 
/* 4334 */           if (isCornerUnderWater(tilex, tiley, onSurface)) {
/*      */             
/* 4336 */             performer.getCommunicator().sendNormalServerMessage("The ground is too moist here, so the sprout would rot.");
/*      */             
/* 4338 */             performer.getCommunicator().sendActionResult(false);
/* 4339 */             return true;
/*      */           } 
/* 4341 */           time = Actions.getStandardActionTime(performer, gardening, sprout, 0.0D);
/* 4342 */           act.setTimeLeft(time);
/* 4343 */           performer.getCommunicator().sendNormalServerMessage("You start planting the sprout.");
/* 4344 */           Server.getInstance().broadCastAction(performer.getName() + " starts to plant a sprout.", performer, 5);
/* 4345 */           performer.sendActionControl(Actions.actionEntrys[186].getVerbString(), true, time);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 4350 */           time = act.getTimeLeft();
/*      */         } 
/* 4352 */         if (counter * 10.0F > time)
/*      */         {
/* 4354 */           StructureConstantsEnum fenceType = Fence.getLowHedgeType(treeMaterial);
/* 4355 */           if (fenceType == StructureConstantsEnum.FENCE_PLAN_WOODEN) {
/*      */             
/* 4357 */             performer.getCommunicator().sendNormalServerMessage("Nobody has managed to grow those in hedges yet.");
/* 4358 */             performer.getCommunicator().sendActionResult(false);
/* 4359 */             return true;
/*      */           } 
/* 4361 */           int found = 0;
/* 4362 */           Item[] inventoryItems = performer.getInventory().getAllItems(false);
/* 4363 */           for (Item item : inventoryItems) {
/*      */             
/* 4365 */             if (item.getTemplateId() == 266)
/*      */             {
/* 4367 */               if (item != sprout)
/*      */               {
/* 4369 */                 if (item.getMaterial() == treeMaterial) {
/*      */                   
/* 4371 */                   found++;
/* 4372 */                   if (found >= 4)
/*      */                     break; 
/*      */                 } 
/*      */               }
/*      */             }
/*      */           } 
/* 4378 */           if (found < 4) {
/*      */             
/* 4380 */             performer.getCommunicator().sendNormalServerMessage("You need to have at least 5 sprouts of the same kind in your inventory.");
/*      */             
/* 4382 */             performer.getCommunicator().sendActionResult(false);
/* 4383 */             return true;
/*      */           } 
/* 4385 */           float ql = sprout.getQualityLevel();
/* 4386 */           float dam = sprout.getDamage();
/* 4387 */           for (Item item : inventoryItems) {
/*      */             
/* 4389 */             if (item.getTemplateId() == 266)
/*      */             {
/* 4391 */               if (item != sprout)
/*      */               {
/* 4393 */                 if (found > 0)
/*      */                 {
/* 4395 */                   if (item.getMaterial() == treeMaterial) {
/*      */                     
/* 4397 */                     ql += item.getQualityLevel();
/* 4398 */                     dam += item.getDamage();
/* 4399 */                     found--;
/* 4400 */                     if (found <= 0) {
/*      */                       break;
/*      */                     }
/*      */                   } 
/*      */                 }
/*      */               }
/*      */             }
/*      */           } 
/* 4408 */           ql /= 5.0F;
/* 4409 */           dam /= 5.0F;
/* 4410 */           Skill gardening = null;
/*      */           
/*      */           try {
/* 4413 */             gardening = performer.getSkills().getSkill(10045);
/*      */           }
/* 4415 */           catch (NoSuchSkillException nss) {
/*      */             
/* 4417 */             gardening = performer.getSkills().learn(10045, 1.0F);
/*      */           } 
/* 4419 */           power = gardening.skillCheck((1.0F + dam), ql, false, counter);
/* 4420 */           toReturn = true;
/* 4421 */           if (power > 0.0D) {
/*      */             
/* 4423 */             SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, onSurface, 0.0F);
/*      */             
/*      */             try {
/* 4426 */               Zone zone = Zones.getZone(tilex, tiley, true);
/*      */               
/* 4428 */               DbFence dbFence = new DbFence(Fence.getLowHedgeType(treeMaterial), tilex, tiley, 0, 1.0F, dir, zone.getId(), performer.getLayer());
/*      */               
/*      */               try {
/* 4431 */                 dbFence.setState(dbFence.getFinishState());
/* 4432 */                 dbFence.setQualityLevel((float)power);
/* 4433 */                 dbFence.improveOrigQualityLevel((float)power);
/* 4434 */                 dbFence.save();
/* 4435 */                 zone.addFence((Fence)dbFence);
/*      */               }
/* 4437 */               catch (IOException iox) {
/*      */                 
/* 4439 */                 logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */               } 
/*      */               
/* 4442 */               if (performer.getDeity() != null && (performer.getDeity()).number == 1)
/*      */               {
/* 4444 */                 performer.maybeModifyAlignment(1.0F);
/*      */               }
/* 4446 */               found = 4;
/* 4447 */               for (Item item : inventoryItems) {
/*      */                 
/* 4449 */                 if (item.getTemplateId() == 266)
/*      */                 {
/* 4451 */                   if (item != sprout)
/*      */                   {
/* 4453 */                     if (found > 0)
/*      */                     {
/* 4455 */                       if (item.getMaterial() == treeMaterial) {
/*      */                         
/* 4457 */                         Items.destroyItem(item.getWurmId());
/* 4458 */                         found--;
/* 4459 */                         if (found <= 0)
/*      */                           break; 
/*      */                       } 
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               } 
/* 4466 */               String tosend = "You plant the sprouts and create a fine hedge.";
/* 4467 */               performer.achievement(318);
/* 4468 */               double gard = gardening.getKnowledge(0.0D);
/* 4469 */               if (gard > 50.0D)
/*      */               {
/* 4471 */                 if (Server.rand.nextBoolean())
/*      */                 {
/* 4473 */                   if (gard < 60.0D) {
/* 4474 */                     tosend = "You plant the hedge, and you can almost feel the plants start sucking nutrition from the earth.";
/* 4475 */                   } else if (gard < 70.0D) {
/* 4476 */                     tosend = "You plant the hedge, and you get a weird feeling that the plants thanks you.";
/* 4477 */                   } else if (gard < 80.0D) {
/* 4478 */                     tosend = "You plant the hedge, and you see the plants perform an almost unnoticable bow as they whisper their thanks.";
/* 4479 */                   } else if (gard < 100.0D) {
/*      */                     
/* 4481 */                     tosend = "You plant the hedge. As you see the plants bow you hear the voice in your head of hundreds of other plants thanking you.";
/* 4482 */                     performer.getStatus().modifyStamina(-1000.0F);
/*      */                   } 
/*      */                 }
/*      */               }
/*      */               
/* 4487 */               TileEvent.log(dbFence.getTileX(), dbFence.getTileY(), 0, performer.getWurmId(), 186);
/* 4488 */               performer.getCommunicator().sendNormalServerMessage(tosend);
/* 4489 */               Server.getInstance().broadCastAction(performer.getName() + " plants a hedge.", performer, 5);
/* 4490 */               performer.getCommunicator().sendActionResult(true);
/*      */             }
/* 4492 */             catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 4499 */             performer.getCommunicator().sendNormalServerMessage("Sadly, the sprout does not survive despite your best efforts.");
/*      */             
/* 4501 */             performer.getCommunicator().sendActionResult(false);
/*      */           } 
/* 4503 */           Items.destroyItem(sprout.getWurmId());
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4508 */         performer.getCommunicator().sendNormalServerMessage("You cannot plant a hedge there.");
/* 4509 */         performer.getCommunicator().sendActionResult(false);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 4514 */       performer.getCommunicator().sendNormalServerMessage("You need to plant with a sprout, not a " + sprout
/* 4515 */           .getName() + ".");
/* 4516 */       performer.getCommunicator().sendActionResult(false);
/*      */     } 
/* 4518 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean plantFlower(Creature performer, Item flower, int tilex, int tiley, boolean onSurface, int tile, float counter) {
/* 4526 */     boolean toReturn = true;
/* 4527 */     VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, onSurface);
/* 4528 */     if (vtile != null)
/*      */     {
/* 4530 */       if (vtile.getStructure() != null && vtile.getStructure().isTypeHouse())
/*      */       {
/*      */         
/* 4533 */         if (flower.getTemplateId() != 756) {
/*      */           
/* 4535 */           performer.getCommunicator().sendNormalServerMessage("The " + flower
/* 4536 */               .getName() + " would never grow inside a building.");
/* 4537 */           return true;
/*      */         } 
/*      */       }
/*      */     }
/* 4541 */     if (!Methods.isActionAllowed(performer, (short)186, tilex, tiley)) {
/* 4542 */       return true;
/*      */     }
/*      */     try {
/* 4545 */       Action act = performer.getCurrentAction();
/* 4546 */       double power = 0.0D;
/* 4547 */       int time = 2000;
/*      */       
/* 4549 */       toReturn = false;
/* 4550 */       if (counter == 1.0F) {
/*      */         
/* 4552 */         Skill gardening = null;
/*      */         
/*      */         try {
/* 4555 */           gardening = performer.getSkills().getSkill(10045);
/*      */         }
/* 4557 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4559 */           gardening = performer.getSkills().learn(10045, 1.0F);
/*      */         } 
/* 4561 */         if (isCornerUnderWater(tilex, tiley, onSurface)) {
/*      */           
/* 4563 */           performer.getCommunicator().sendNormalServerMessage("The ground is too moist here, so the " + flower
/* 4564 */               .getName() + " would rot.");
/* 4565 */           return true;
/*      */         } 
/* 4567 */         time = Actions.getStandardActionTime(performer, gardening, flower, 0.0D);
/* 4568 */         act.setTimeLeft(time);
/* 4569 */         performer.getCommunicator().sendNormalServerMessage("You start planting the " + flower.getName() + ".");
/* 4570 */         Server.getInstance().broadCastAction(performer
/* 4571 */             .getName() + " starts to plant some " + flower.getName() + ".", performer, 5);
/* 4572 */         performer.sendActionControl(Actions.actionEntrys[186].getVerbString(), true, time);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 4577 */         time = act.getTimeLeft();
/*      */       } 
/* 4579 */       if (counter * 10.0F > time) {
/*      */         
/* 4581 */         Skill gardening = null;
/*      */         
/*      */         try {
/* 4584 */           gardening = performer.getSkills().getSkill(10045);
/*      */         }
/* 4586 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4588 */           gardening = performer.getSkills().learn(10045, 1.0F);
/*      */         } 
/* 4590 */         power = gardening.skillCheck((10.0F + flower.getDamage()), flower.getCurrentQualityLevel(), false, counter);
/* 4591 */         toReturn = true;
/* 4592 */         if (power > 0.0D) {
/*      */           
/* 4594 */           int newData = TileGrassBehaviour.getDataForFlower(flower.getTemplateId());
/*      */ 
/*      */           
/* 4597 */           byte nty = Tiles.Tile.TILE_GRASS.id;
/* 4598 */           if (flower.getTemplateId() == 620) {
/* 4599 */             nty = Tiles.Tile.TILE_STEPPE.id;
/* 4600 */           } else if (flower.getTemplateId() == 479) {
/* 4601 */             nty = Tiles.Tile.TILE_MOSS.id;
/* 4602 */           }  Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), nty, (byte)newData);
/* 4603 */           Server.setWorldResource(tilex, tiley, 0);
/*      */           
/* 4605 */           Players.getInstance().sendChangedTile(tilex, tiley, onSurface, true);
/*      */           
/*      */           try {
/* 4608 */             Zone z = Zones.getZone(tilex, tiley, true);
/* 4609 */             z.changeTile(tilex, tiley);
/*      */           }
/* 4611 */           catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */           
/* 4614 */           String tosend = "You plant the " + flower.getName() + ".";
/* 4615 */           performer.achievement(123);
/* 4616 */           double gard = gardening.getKnowledge(0.0D);
/* 4617 */           if (gard > 60.0D && flower.getTemplate().isFlower()) {
/*      */             
/* 4619 */             if (gard < 70.0D) {
/* 4620 */               tosend = "You plant the " + flower.getName() + ", and you can almost feel them start sucking nutrition from the earth.";
/*      */             }
/* 4622 */             else if (gard < 80.0D) {
/* 4623 */               tosend = "You plant the " + flower.getName() + ", and you get a weird feeling that they thank you.";
/*      */             }
/* 4625 */             else if (gard < 90.0D) {
/* 4626 */               tosend = "You plant the " + flower.getName() + ", and you see them perform an almost unnoticable bow. Or was it the wind?";
/*      */             }
/* 4628 */             else if (gard < 100.0D) {
/*      */               
/* 4630 */               tosend = "You plant the " + flower.getName() + ". As you see them bow you hear their thankful tiny voices in your head.";
/*      */               
/* 4632 */               performer.getStatus().modifyStamina(-1000.0F);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 4637 */             tosend = "You plant the " + flower.getName();
/* 4638 */             performer.getStatus().modifyStamina(-1000.0F);
/*      */           } 
/*      */           
/* 4641 */           if (performer.getDeity() != null && (performer.getDeity()).number == 1)
/*      */           {
/* 4643 */             performer.maybeModifyAlignment(1.0F);
/*      */           }
/* 4645 */           performer.getCommunicator().sendNormalServerMessage(tosend);
/* 4646 */           Server.getInstance().broadCastAction(performer
/* 4647 */               .getName() + " plants some " + flower.getName() + ".", performer, 5);
/*      */         } else {
/*      */           
/* 4650 */           performer.getCommunicator().sendNormalServerMessage("Sadly, the " + flower
/* 4651 */               .getName() + " do not survive despite your best efforts.");
/*      */         } 
/*      */         
/* 4654 */         if (!flower.isFlower()) {
/*      */           
/* 4656 */           int weight = flower.getFullWeight();
/* 4657 */           if (weight <= flower.getTemplate().getWeightGrams()) {
/* 4658 */             Items.destroyItem(flower.getWurmId());
/*      */           } else {
/*      */             
/* 4661 */             weight -= flower.getTemplate().getWeightGrams();
/* 4662 */             flower.setWeight(weight, false);
/*      */           } 
/*      */         } else {
/*      */           
/* 4666 */           Items.destroyItem(flower.getWurmId());
/*      */         } 
/*      */       } 
/* 4669 */     } catch (NoSuchActionException nsa) {
/*      */       
/* 4671 */       logger.log(Level.WARNING, performer.getName() + ": " + nsa.getMessage(), (Throwable)nsa);
/*      */     } 
/* 4673 */     return toReturn;
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
/*      */   static boolean pickFlower(Creature performer, Item sickle, int tilex, int tiley, int tile, float counter, Action act) {
/* 4691 */     boolean toReturn = true;
/* 4692 */     if (sickle.getTemplateId() == 267 || sickle.getTemplateId() == 176) {
/*      */       
/* 4694 */       byte tileData = Tiles.decodeData(tile);
/* 4695 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 4697 */         performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put the flowers.");
/*      */         
/* 4699 */         return true;
/*      */       } 
/*      */       
/* 4702 */       GrassData.FlowerType flowerType = GrassData.FlowerType.decodeTileData(tileData);
/* 4703 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_GRASS.id || flowerType == GrassData.FlowerType.NONE) {
/*      */         
/* 4705 */         performer.getCommunicator().sendNormalServerMessage("No flowers grow here.");
/* 4706 */         return true;
/*      */       } 
/*      */       
/* 4709 */       toReturn = false;
/* 4710 */       int time = act.getTimeLeft();
/*      */       
/* 4712 */       if (counter == 1.0F) {
/*      */ 
/*      */         
/*      */         try {
/* 4716 */           int weight = ItemTemplateFactory.getInstance().getTemplate(498).getWeightGrams();
/* 4717 */           if (!performer.canCarry(weight))
/*      */           {
/* 4719 */             performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the flowers. You need to drop some things first.");
/*      */             
/* 4721 */             return true;
/*      */           }
/*      */         
/* 4724 */         } catch (NoSuchTemplateException nst) {
/*      */           
/* 4726 */           logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 4727 */           return true;
/*      */         } 
/*      */         
/* 4730 */         Skill gardening = null;
/* 4731 */         Skill sickskill = null;
/*      */ 
/*      */         
/*      */         try {
/* 4735 */           gardening = performer.getSkills().getSkill(10045);
/*      */         }
/* 4737 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4739 */           gardening = performer.getSkills().learn(10045, 1.0F);
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/* 4744 */           sickskill = performer.getSkills().getSkill(10046);
/*      */         }
/* 4746 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4748 */           sickskill = performer.getSkills().learn(10046, 1.0F);
/*      */         } 
/* 4750 */         time = Actions.getStandardActionTime(performer, gardening, sickle, sickskill.getKnowledge(0.0D));
/* 4751 */         performer.getCommunicator().sendNormalServerMessage("You start picking the flowers.");
/* 4752 */         Server.getInstance().broadCastAction(performer.getName() + " starts to pick some flowers.", performer, 5);
/* 4753 */         performer.sendActionControl("picking flowers", true, time);
/* 4754 */         act.setTimeLeft(time);
/*      */       } 
/* 4756 */       if (counter * 10.0F >= time)
/*      */       {
/* 4758 */         if (act.getRarity() != 0) {
/* 4759 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/*      */         try {
/* 4762 */           int weight = ItemTemplateFactory.getInstance().getTemplate(498).getWeightGrams();
/* 4763 */           if (!performer.canCarry(weight))
/*      */           {
/* 4765 */             performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the flowers. You need to drop some things first.");
/*      */             
/* 4767 */             return true;
/*      */           }
/*      */         
/* 4770 */         } catch (NoSuchTemplateException nst) {
/*      */           
/* 4772 */           logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 4773 */           return true;
/*      */         } 
/* 4775 */         sickle.setDamage(sickle.getDamage() + 0.003F * sickle.getDamageModifier());
/* 4776 */         double bonus = 0.0D;
/* 4777 */         double power = 0.0D;
/* 4778 */         Skill gardening = null;
/* 4779 */         Skill sickskill = null;
/*      */ 
/*      */         
/*      */         try {
/* 4783 */           gardening = performer.getSkills().getSkill(10045);
/*      */         }
/* 4785 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4787 */           gardening = performer.getSkills().learn(10045, 1.0F);
/*      */         } 
/*      */         
/*      */         try {
/* 4791 */           sickskill = performer.getSkills().getSkill(10046);
/*      */         }
/* 4793 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4795 */           sickskill = performer.getSkills().learn(10046, 1.0F);
/*      */         } 
/* 4797 */         bonus = Math.max(1.0D, sickskill.skillCheck(1.0D, sickle, 0.0D, false, counter));
/* 4798 */         power = gardening.skillCheck(1.0D, sickle, bonus, false, counter);
/*      */         
/* 4800 */         toReturn = true;
/*      */         
/*      */         try {
/* 4803 */           float modifier = 1.0F;
/* 4804 */           if (sickle.getSpellEffects() != null)
/*      */           {
/* 4806 */             modifier = sickle.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */           }
/* 4808 */           GrassData.GrowthStage growthStage = GrassData.GrowthStage.decodeTileData(tileData);
/* 4809 */           Item flower = ItemFactory.createItem(TileGrassBehaviour.getFlowerTypeFor(flowerType), 
/* 4810 */               Math.max(1.0F, Math.min(100.0F, (float)power * modifier + sickle.getRarity())), act.getRarity(), null);
/*      */           
/* 4812 */           if (power < 0.0D) {
/* 4813 */             flower.setDamage((float)-power / 2.0F);
/*      */           }
/* 4815 */           performer.getInventory().insertItem(flower);
/*      */ 
/*      */           
/* 4818 */           Server.setSurfaceTile(tilex, tiley, 
/* 4819 */               Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, 
/* 4820 */               GrassData.encodeGrassTileData(growthStage, GrassData.FlowerType.NONE));
/*      */           
/* 4822 */           Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 4823 */           performer.getCommunicator().sendNormalServerMessage("You pick some flowers.");
/* 4824 */           Server.getInstance().broadCastAction(performer.getName() + " picks some flowers.", performer, 5);
/*      */         }
/* 4826 */         catch (NoSuchTemplateException nst) {
/*      */           
/* 4828 */           logger.log(Level.WARNING, "No template for flowers!", (Throwable)nst);
/* 4829 */           performer.getCommunicator().sendNormalServerMessage("You fail to pick the flowers. You realize something is wrong with the world.");
/*      */         
/*      */         }
/* 4832 */         catch (FailedException fe) {
/*      */           
/* 4834 */           logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 4835 */           performer.getCommunicator().sendNormalServerMessage("You fail to pick the flowers. You realize something is wrong with the world.");
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 4842 */       performer.getCommunicator().sendNormalServerMessage("You cannot pick sprouts with that.");
/* 4843 */       logger.log(Level.WARNING, performer.getName() + " tried to pick sprout with a " + sickle.getName());
/*      */     } 
/* 4845 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean growFarm(Creature performer, int tile, int tilex, int tiley, boolean onSurface) {
/* 4851 */     int data = Tiles.decodeData(tile) & 0xFF;
/* 4852 */     byte type = Tiles.decodeType(tile);
/* 4853 */     int tileState = data >> 4;
/* 4854 */     int tileAge = tileState & 0x7;
/* 4855 */     if (tileAge < 7) {
/*      */       
/* 4857 */       int crop = data & 0xF;
/* 4858 */       tileAge++;
/* 4859 */       if (!onSurface) {
/* 4860 */         Server.caveMesh.setTile(tilex, tiley, 
/* 4861 */             Tiles.encode(Tiles.decodeHeight(tile), type, (byte)((tileAge << 4) + crop & 0xFF)));
/*      */       } else {
/* 4863 */         Server.setSurfaceTile(tilex, tiley, 
/* 4864 */             Tiles.decodeHeight(tile), type, (byte)((tileAge << 4) + crop & 0xFF));
/*      */       } 
/* 4866 */       if (WurmCalendar.isNight()) {
/*      */         
/* 4868 */         SoundPlayer.playSound("sound.birdsong.bird1", tilex, tiley, onSurface, 3.0F);
/*      */       }
/*      */       else {
/*      */         
/* 4872 */         SoundPlayer.playSound("sound.birdsong.bird2", tilex, tiley, onSurface, 3.0F);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4881 */       Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/*      */     } 
/* 4883 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean harvest(Creature performer, int tilex, int tiley, boolean onSurface, int tile, float counter, @Nullable Item item) {
/* 4891 */     boolean done = true;
/* 4892 */     byte type = Tiles.decodeType(tile);
/* 4893 */     if (type != Tiles.Tile.TILE_FIELD.id && type != Tiles.Tile.TILE_FIELD2.id)
/* 4894 */       return done; 
/* 4895 */     byte data = Tiles.decodeData(tile);
/* 4896 */     int tileAge = Crops.decodeFieldAge(data);
/* 4897 */     int crop = Crops.getCropNumber(type, data);
/* 4898 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */       
/* 4900 */       performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to keep whatever you harvest.");
/*      */       
/* 4902 */       return true;
/*      */     } 
/* 4904 */     if (crop > 3 || (item != null && item.getTemplateId() == 268))
/*      */     {
/* 4906 */       if (tileAge != 0 && tileAge != 7) {
/*      */         
/* 4908 */         double diff = Crops.getDifficultyFor(crop);
/* 4909 */         done = false;
/* 4910 */         Action act = null;
/*      */         
/*      */         try {
/* 4913 */           act = performer.getCurrentAction();
/*      */         }
/* 4915 */         catch (NoSuchActionException nsa) {
/*      */           
/* 4917 */           logger.log(Level.WARNING, nsa.getMessage(), (Throwable)nsa);
/* 4918 */           return true;
/*      */         } 
/* 4920 */         int time = 100;
/* 4921 */         if (counter == 1.0F) {
/*      */           
/* 4923 */           Skill farming = performer.getSkills().getSkillOrLearn(10049);
/* 4924 */           time = Actions.getStandardActionTime(performer, farming, null, 0.0D);
/* 4925 */           act.setTimeLeft(time);
/* 4926 */           performer.getCommunicator().sendNormalServerMessage("You start harvesting the field.");
/* 4927 */           Server.getInstance().broadCastAction(performer.getName() + " starts harvesting the field.", performer, 5);
/* 4928 */           performer.sendActionControl(Actions.actionEntrys[152].getVerbString(), true, time);
/*      */           
/* 4930 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 4935 */           time = act.getTimeLeft();
/*      */         } 
/*      */         
/* 4938 */         if (crop <= 3 && item != null && act.justTickedSecond())
/* 4939 */           item.setDamage(item.getDamage() + 3.0E-4F * item.getDamageModifier()); 
/* 4940 */         if (act.justTickedSecond()) {
/*      */           
/* 4942 */           if (act.currentSecond() % 5 == 0)
/* 4943 */             performer.getStatus().modifyStamina(-10000.0F); 
/* 4944 */           if (act.mayPlaySound())
/*      */           {
/* 4946 */             if (crop <= 3 && item != null && item.getTemplateId() == 268) {
/* 4947 */               Methods.sendSound(performer, "sound.work.farming.scythe");
/*      */             } else {
/* 4949 */               Methods.sendSound(performer, "sound.work.farming.harvest");
/*      */             }  } 
/*      */         } 
/* 4952 */         if (counter * 10.0F > time)
/*      */         {
/* 4954 */           if (act.getRarity() != 0)
/* 4955 */             performer.playPersonalSound("sound.fx.drumroll"); 
/* 4956 */           Skill farming = performer.getSkills().getSkillOrLearn(10049);
/* 4957 */           double power = farming.skillCheck(diff, 0.0D, false, counter);
/* 4958 */           Skill primskill = null;
/* 4959 */           byte itemRarity = 0;
/* 4960 */           if (crop <= 3 && item != null && item.getTemplateId() == 268) {
/*      */             
/* 4962 */             itemRarity = item.getRarity();
/*      */             
/*      */             try {
/* 4965 */               int primarySkill = item.getPrimarySkill();
/* 4966 */               primskill = performer.getSkills().getSkillOrLearn(primarySkill);
/*      */             }
/* 4968 */             catch (NoSuchSkillException nss) {
/*      */               
/* 4970 */               logger.log(Level.WARNING, "Scythe has no prim skill? :" + nss.getMessage(), (Throwable)nss);
/*      */             } 
/*      */           } 
/* 4973 */           if (primskill != null)
/*      */           {
/* 4975 */             Math.max(0.0D, primskill.skillCheck(diff, item, 0.0D, false, counter));
/*      */           }
/*      */           
/* 4978 */           TileEvent.log(tilex, tiley, 0, performer.getWurmId(), 152);
/* 4979 */           done = true;
/* 4980 */           int templateId = Crops.getProductTemplate(crop);
/* 4981 */           float knowledge = (float)farming.getKnowledge(0.0D);
/*      */           
/* 4983 */           float ql = knowledge + (100.0F - knowledge) * (float)power / 500.0F;
/*      */           
/* 4985 */           float ageYieldFactor = 0.0F;
/* 4986 */           float ageQLFactor = 0.0F;
/* 4987 */           boolean ripe = false;
/* 4988 */           String failMessage = "You realize you harvested so early that nothing had a chance to grow here.";
/* 4989 */           String passMessage = "";
/*      */           
/* 4991 */           if (tileAge >= 3)
/*      */           {
/*      */ 
/*      */             
/* 4995 */             if (tileAge < 4) {
/*      */               
/* 4997 */               ageQLFactor = 0.7F;
/* 4998 */               ageYieldFactor = 0.5F;
/* 4999 */               failMessage = "You realize you harvested much too early. There was nothing here to harvest.";
/* 5000 */               passMessage = "You realize you harvested much too early. Only sprouts grew here.";
/*      */             }
/* 5002 */             else if (tileAge < 5) {
/*      */               
/* 5004 */               ageQLFactor = 0.9F;
/* 5005 */               ageYieldFactor = 0.7F;
/* 5006 */               failMessage = "You realize you harvested too early. There was nothing here to harvest.";
/* 5007 */               passMessage = "You realize you harvested too early. The harvest is of low quality.";
/*      */             }
/* 5009 */             else if (tileAge < 7) {
/*      */               
/* 5011 */               ripe = true;
/* 5012 */               ageQLFactor = 1.0F;
/* 5013 */               ageYieldFactor = 1.0F;
/* 5014 */               failMessage = "You realize you harvested in perfect time, tending the field would have resulted in a better yield.";
/* 5015 */               passMessage = "You realize you harvested in perfect time. The harvest is of top quality.";
/*      */             }  } 
/* 5017 */           float realKnowledge = (float)farming.getKnowledge(0.0D);
/* 5018 */           int worldResource = Server.getWorldResource(tilex, tiley);
/* 5019 */           int farmedCount = worldResource >>> 11;
/* 5020 */           int farmedChance = worldResource & 0x7FF;
/*      */           
/* 5022 */           short resource = (short)(farmedChance + act.getRarity() * 110 + itemRarity * 50 + Math.min(5, farmedCount) * 50);
/* 5023 */           float div = 100.0F - realKnowledge / 15.0F;
/* 5024 */           short bonusYield = (short)(int)(resource / div / 1.5F);
/* 5025 */           float baseYield = realKnowledge / 15.0F;
/* 5026 */           int quantity = (int)((baseYield + bonusYield) * ageYieldFactor);
/* 5027 */           Server.setWorldResource(tilex, tiley, 0);
/* 5028 */           Server.getInstance().broadCastAction(performer.getName() + " has harvested the field.", performer, 5);
/* 5029 */           ql *= ageQLFactor;
/* 5030 */           if (crop <= 3 && item != null)
/*      */           {
/* 5032 */             if (item.getSpellEffects() != null) {
/*      */               
/* 5034 */               float modifier = item.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/* 5035 */               ql *= modifier;
/*      */             } 
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
/* 5047 */           if (quantity == 0 && ripe)
/* 5048 */             quantity = 1; 
/* 5049 */           if (quantity == 1 && farmedCount > 0 && ripe)
/* 5050 */             quantity++; 
/* 5051 */           if (quantity == 2 && farmedCount >= 4 && ripe) {
/* 5052 */             quantity++;
/*      */           }
/* 5054 */           if (quantity == 0 || (ripe && quantity == 1)) {
/* 5055 */             performer.getCommunicator().sendNormalServerMessage(failMessage);
/*      */           } else {
/* 5057 */             performer.getCommunicator().sendNormalServerMessage(passMessage);
/*      */           } 
/* 5059 */           String cropString = Crops.getCropName(crop);
/* 5060 */           performer.getCommunicator().sendNormalServerMessage("You managed to get a yield of " + quantity + " " + cropString + ".");
/*      */           
/* 5062 */           if (templateId == 144 && quantity >= 5) {
/* 5063 */             performer.achievement(544);
/*      */           }
/*      */           try {
/* 5066 */             for (int x = 0; x < quantity; x++)
/*      */             {
/* 5068 */               Item result = ItemFactory.createItem(templateId, Math.max(Math.min(ql, 100.0F), 1.0F), null);
/* 5069 */               if (!performer.getInventory().insertItem(result, true)) {
/* 5070 */                 performer.getCommunicator().sendNormalServerMessage("You can't carry the harvest. It falls to the ground and is ruined!");
/*      */               }
/*      */             }
/*      */           
/* 5074 */           } catch (NoSuchTemplateException nst) {
/*      */             
/* 5076 */             logger.log(Level.WARNING, "No such template", (Throwable)nst);
/*      */           }
/* 5078 */           catch (FailedException fe) {
/*      */             
/* 5080 */             logger.log(Level.WARNING, "Failed to create harvest", (Throwable)fe);
/*      */           } 
/* 5082 */           if (onSurface) {
/* 5083 */             Server.setSurfaceTile(tilex, tiley, 
/* 5084 */                 Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0);
/*      */           } else {
/* 5086 */             Server.caveMesh
/* 5087 */               .setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0));
/* 5088 */           }  performer.getMovementScheme().touchFreeMoveCounter();
/* 5089 */           Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 5094 */         performer.getCommunicator().sendNormalServerMessage("There is nothing here to harvest.");
/* 5095 */         done = true;
/*      */       } 
/*      */     }
/* 5098 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean checkIfTerraformingOnPermaObject(int digTilex, int digTiley) {
/* 5104 */     short hh = Tiles.decodeHeight(Server.surfaceMesh.getTile(digTilex, digTiley));
/* 5105 */     if (hh < 1) {
/*      */       
/* 5107 */       VolaTile t = Zones.getTileOrNull(digTilex, digTiley, true);
/* 5108 */       if (t != null && t.hasOnePerTileItem(0))
/*      */       {
/* 5110 */         return true;
/*      */       }
/* 5112 */       t = Zones.getTileOrNull(digTilex - 1, digTiley - 1, true);
/* 5113 */       if (t != null && t.hasOnePerTileItem(0))
/*      */       {
/* 5115 */         return true;
/*      */       }
/* 5117 */       t = Zones.getTileOrNull(digTilex, digTiley - 1, true);
/* 5118 */       if (t != null && t.hasOnePerTileItem(0))
/*      */       {
/* 5120 */         return true;
/*      */       }
/* 5122 */       t = Zones.getTileOrNull(digTilex - 1, digTiley, true);
/* 5123 */       if (t != null && t.hasOnePerTileItem(0))
/*      */       {
/* 5125 */         return true;
/*      */       }
/*      */     } 
/* 5128 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean pickSprout(Creature performer, Item sickle, int tilex, int tiley, int tile, Tiles.Tile theTile, float counter, Action act) {
/* 5134 */     boolean toReturn = true;
/*      */     
/* 5136 */     byte tileType = Tiles.decodeType(tile);
/* 5137 */     if (sickle.getTemplateId() == 267 && !theTile.isEnchanted()) {
/*      */       
/* 5139 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 5141 */         performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put the sprout.");
/*      */         
/* 5143 */         return true;
/*      */       } 
/* 5145 */       byte data = Tiles.decodeData(tile);
/* 5146 */       int age = FoliageAge.getAgeAsByte(data);
/* 5147 */       if (age == 7 || age == 9 || age == 11 || age == 13)
/*      */       {
/*      */         
/* 5150 */         Skill forestry = performer.getSkills().getSkillOrLearn(10048);
/* 5151 */         toReturn = false;
/* 5152 */         int time = Actions.getStandardActionTime(performer, forestry, sickle, 0.0D);
/*      */         
/* 5154 */         if (counter == 1.0F) {
/*      */ 
/*      */           
/*      */           try {
/* 5158 */             int weight = ItemTemplateFactory.getInstance().getTemplate(266).getWeightGrams();
/* 5159 */             if (!performer.canCarry(weight))
/*      */             {
/* 5161 */               performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the sprout. You need to drop some things first.");
/*      */               
/* 5163 */               return true;
/*      */             }
/*      */           
/* 5166 */           } catch (NoSuchTemplateException nst) {
/*      */             
/* 5168 */             logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 5169 */             return true;
/*      */           } 
/* 5171 */           if (theTile.isBush()) {
/*      */             
/* 5173 */             performer.getCommunicator().sendNormalServerMessage("You start cutting a sprout from the bush.");
/* 5174 */             Server.getInstance().broadCastAction(performer.getName() + " starts to cut a sprout off a bush.", performer, 5);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 5179 */             performer.getCommunicator().sendNormalServerMessage("You start cutting a sprout from the tree.");
/* 5180 */             Server.getInstance().broadCastAction(performer.getName() + " starts to cut a sprout off a tree.", performer, 5);
/*      */           } 
/*      */           
/* 5183 */           performer.sendActionControl(Actions.actionEntrys[187].getVerbString(), true, time);
/*      */         } 
/*      */ 
/*      */         
/* 5187 */         if (counter * 10.0F >= time)
/*      */         {
/* 5189 */           if (act.getRarity() != 0)
/*      */           {
/* 5191 */             performer.playPersonalSound("sound.fx.drumroll");
/*      */           }
/*      */           
/*      */           try {
/* 5195 */             int weight = ItemTemplateFactory.getInstance().getTemplate(266).getWeightGrams();
/* 5196 */             if (!performer.canCarry(weight))
/*      */             {
/* 5198 */               performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the sprout. You need to drop some things first.");
/*      */               
/* 5200 */               return true;
/*      */             }
/*      */           
/* 5203 */           } catch (NoSuchTemplateException nst) {
/*      */             
/* 5205 */             logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 5206 */             return true;
/*      */           } 
/* 5208 */           sickle.setDamage(sickle.getDamage() + 0.003F * sickle.getDamageModifier());
/* 5209 */           double bonus = 0.0D;
/* 5210 */           double power = 0.0D;
/* 5211 */           Skill sickskill = performer.getSkills().getSkillOrLearn(10046);
/* 5212 */           bonus = Math.max(1.0D, sickskill.skillCheck(1.0D, sickle, 0.0D, false, counter));
/* 5213 */           power = forestry.skillCheck(1.0D, sickle, bonus, false, counter);
/*      */           
/* 5215 */           toReturn = true;
/*      */           
/*      */           try {
/* 5218 */             byte material = 0;
/*      */             
/* 5220 */             if (theTile.isBush()) {
/*      */               
/* 5222 */               BushData.BushType bushType = theTile.getBushType(data);
/* 5223 */               material = bushType.getMaterial();
/*      */             }
/*      */             else {
/*      */               
/* 5227 */               TreeData.TreeType treeType = theTile.getTreeType(data);
/* 5228 */               material = treeType.getMaterial();
/*      */             } 
/*      */             
/* 5231 */             float modifier = 1.0F;
/* 5232 */             if (sickle.getSpellEffects() != null)
/*      */             {
/* 5234 */               modifier = sickle.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */             }
/*      */             
/* 5237 */             Item sprout = ItemFactory.createItem(266, Math.max(1.0F, Math.min(100.0F, (float)power * modifier + sickle.getRarity())), material, act
/* 5238 */                 .getRarity(), null);
/*      */             
/* 5240 */             if (power < 0.0D) {
/* 5241 */               sprout.setDamage((float)-power / 2.0F);
/*      */             }
/* 5243 */             SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 2.0F);
/*      */             
/* 5245 */             age--;
/*      */             
/* 5247 */             performer.getInventory().insertItem(sprout);
/* 5248 */             byte newData = (byte)((age << 4) + (data & 0xF) & 0xFF);
/*      */             
/* 5250 */             Server.setSurfaceTile(tilex, tiley, 
/* 5251 */                 Tiles.decodeHeight(tile), Tiles.decodeType(tile), newData);
/*      */             
/* 5253 */             Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 5254 */             if (theTile.isBush())
/*      */             {
/* 5256 */               performer.getCommunicator().sendNormalServerMessage("You cut a sprout from the bush.");
/* 5257 */               Server.getInstance().broadCastAction(performer.getName() + " cuts a sprout off a bush.", performer, 5);
/*      */             }
/*      */             else
/*      */             {
/* 5261 */               performer.getCommunicator().sendNormalServerMessage("You cut a sprout from the tree.");
/* 5262 */               Server.getInstance().broadCastAction(performer.getName() + " cuts a sprout off a tree.", performer, 5);
/*      */             }
/*      */           
/*      */           }
/* 5266 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 5268 */             logger.log(Level.WARNING, "No template for sprout!", (Throwable)nst);
/* 5269 */             performer.getCommunicator().sendNormalServerMessage("You fail to pick the sprout. You realize something is wrong with the world.");
/*      */           
/*      */           }
/* 5272 */           catch (FailedException fe) {
/*      */             
/* 5274 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 5275 */             performer.getCommunicator().sendNormalServerMessage("You fail to pick the sprout. You realize something is wrong with the world.");
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 5281 */       else if (theTile.isBush())
/*      */       {
/* 5283 */         performer.getCommunicator().sendNormalServerMessage("The bush has no sprout to pick.");
/*      */       }
/*      */       else
/*      */       {
/* 5287 */         performer.getCommunicator().sendNormalServerMessage("The tree has no sprout to pick.");
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 5293 */       performer.getCommunicator().sendNormalServerMessage("You cannot pick sprouts with that.");
/* 5294 */       logger.log(Level.WARNING, performer.getName() + " tried to pick sprout with a " + sickle.getName());
/*      */     } 
/* 5296 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getTreeHarvestingToolTemplate(TreeData.TreeType type) {
/* 5301 */     if (type == TreeData.TreeType.MAPLE) {
/* 5302 */       return 421;
/*      */     }
/* 5304 */     return 267;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean harvestTree(Action act, Creature performer, Item tool, int tilex, int tiley, int tile, Tiles.Tile theTile, float counter) {
/* 5310 */     boolean toReturn = true;
/* 5311 */     byte data = Tiles.decodeData(tile);
/* 5312 */     int age = FoliageAge.getAgeAsByte(data);
/* 5313 */     TreeData.TreeType treeType = theTile.getTreeType(data);
/*      */     
/* 5315 */     if (tool.getTemplateId() == getTreeHarvestingToolTemplate(treeType)) {
/*      */       
/* 5317 */       String treeName = treeType.getName();
/* 5318 */       if (counter == 1.0F && !TileTreeBehaviour.hasFruit(performer, tilex, tiley, age)) {
/*      */         
/* 5320 */         performer.getCommunicator().sendNormalServerMessage("There is nothing to harvest on the " + treeName + ".");
/* 5321 */         return true;
/*      */       } 
/* 5323 */       int templateId = TileTreeBehaviour.getItem(tilex, tiley, age, treeType);
/* 5324 */       if (templateId == -10) {
/*      */         
/* 5326 */         performer.getCommunicator().sendNormalServerMessage("There is nothing to harvest on the " + treeName + ".");
/* 5327 */         return true;
/*      */       } 
/*      */       
/* 5330 */       toReturn = false;
/* 5331 */       int time = 150;
/* 5332 */       Skill skill = performer.getSkills().getSkillOrLearn(10048);
/* 5333 */       Skill toolSkill = null;
/* 5334 */       if (tool.getTemplateId() == 267) {
/* 5335 */         toolSkill = performer.getSkills().getSkillOrLearn(10046);
/*      */       }
/* 5337 */       if (counter == 1.0F) {
/*      */         
/* 5339 */         if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/* 5341 */           performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put what you harvest.");
/*      */           
/* 5343 */           return true;
/*      */         } 
/* 5345 */         if (tool.getTemplate().isContainerLiquid() && tool.getFreeVolume() <= 0) {
/*      */           
/* 5347 */           performer.getCommunicator().sendNormalServerMessage("The " + tool.getName() + " is already full!");
/* 5348 */           return true;
/*      */         } 
/* 5350 */         int maxSearches = calcMaxHarvest(tile, skill.getKnowledge(0.0D), tool);
/* 5351 */         time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 5352 */         act.setNextTick(time);
/* 5353 */         act.setTickCount(1);
/* 5354 */         act.setData(0L);
/* 5355 */         float totalTime = (time * maxSearches);
/*      */         
/*      */         try {
/* 5358 */           performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */         }
/* 5360 */         catch (NoSuchActionException nsa) {
/*      */           
/* 5362 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/* 5364 */         performer.getCommunicator().sendNormalServerMessage("You start to harvest the " + treeName + ".");
/* 5365 */         Server.getInstance().broadCastAction(performer.getName() + " starts to harvest a tree.", performer, 5);
/* 5366 */         performer.sendActionControl(Actions.actionEntrys[152].getVerbString(), true, (int)totalTime);
/* 5367 */         performer.getStatus().modifyStamina(-500.0F);
/*      */       } 
/* 5369 */       if (tool != null && act.justTickedSecond()) {
/* 5370 */         tool.setDamage(tool.getDamage() + 3.0E-4F * tool.getDamageModifier());
/*      */       }
/* 5372 */       if (counter * 10.0F >= act.getNextTick())
/*      */       {
/* 5374 */         if (act.getRarity() != 0) {
/* 5375 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 5377 */         int searchCount = act.getTickCount();
/* 5378 */         int maxSearches = calcMaxHarvest(tile, skill.getKnowledge(0.0D), tool);
/* 5379 */         act.incTickCount();
/* 5380 */         act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 5381 */         int knowledge = (int)skill.getKnowledge(0.0D);
/*      */         
/* 5383 */         performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */         
/* 5386 */         if (searchCount >= maxSearches) {
/* 5387 */           toReturn = true;
/*      */         }
/* 5389 */         act.setData(act.getData() + 1L);
/* 5390 */         double bonus = 0.0D;
/* 5391 */         if (tool.getTemplateId() == 267)
/*      */         {
/* 5393 */           bonus = Math.max(1.0D, toolSkill.skillCheck(1.0D, tool, 0.0D, false, counter / searchCount));
/*      */         }
/* 5395 */         double power = skill.skillCheck(skill.getKnowledge(0.0D) - 5.0D, tool, bonus, false, counter / searchCount);
/*      */         
/*      */         try {
/* 5398 */           float modifier = 1.0F;
/* 5399 */           if (tool.getSpellEffects() != null)
/*      */           {
/* 5401 */             modifier = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */           }
/*      */           
/* 5404 */           float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/* 5405 */           ql = Math.min(100.0F, (ql + tool.getRarity()) * modifier);
/* 5406 */           Item harvested = ItemFactory.createItem(templateId, Math.max(1.0F, ql), act.getRarity(), null);
/* 5407 */           if (ql < 0.0F) {
/* 5408 */             harvested.setDamage(-ql / 2.0F);
/*      */           }
/* 5410 */           if (tool.getTemplateId() == 267) {
/*      */             
/* 5412 */             performer.getInventory().insertItem(harvested);
/* 5413 */             SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/*      */           }
/*      */           else {
/*      */             
/* 5417 */             MethodsItems.fillContainer(act, tool, harvested, performer, false);
/* 5418 */             if (!harvested.deleted && harvested.getParentId() == -10L) {
/*      */               
/* 5420 */               performer.getCommunicator().sendNormalServerMessage("Not all the " + harvested
/* 5421 */                   .getName() + " would fit in the " + tool.getName() + ".");
/* 5422 */               Items.destroyItem(harvested.getWurmId());
/* 5423 */               toReturn = true;
/*      */             } 
/*      */           } 
/* 5426 */           if (searchCount == 1) {
/* 5427 */             TileTreeBehaviour.pick(tilex, tiley);
/*      */           }
/* 5429 */           performer.getCommunicator().sendNormalServerMessage("You harvest " + harvested
/* 5430 */               .getNameWithGenus() + " from the " + treeName + ".");
/* 5431 */           Server.getInstance().broadCastAction(performer
/* 5432 */               .getName() + " harvests " + harvested.getName() + " from a tree.", performer, 5);
/*      */ 
/*      */           
/* 5435 */           if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem())
/*      */           {
/* 5437 */             performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */             
/* 5439 */             toReturn = true;
/*      */           }
/*      */         
/* 5442 */         } catch (NoSuchTemplateException nst) {
/*      */           
/* 5444 */           logger.log(Level.WARNING, "No template for " + templateId, (Throwable)nst);
/* 5445 */           performer.getCommunicator().sendNormalServerMessage("You fail to harvest. You realize something is wrong with the world.");
/*      */         
/*      */         }
/* 5448 */         catch (FailedException fe) {
/*      */           
/* 5450 */           logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 5451 */           performer.getCommunicator().sendNormalServerMessage("You fail to harvest. You realize something is wrong with the world.");
/*      */         } 
/*      */ 
/*      */         
/* 5455 */         if (searchCount < maxSearches)
/*      */         {
/* 5457 */           act.setRarity(performer.getRarity());
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 5463 */       performer.getCommunicator().sendNormalServerMessage("You cannot harvest with that.");
/* 5464 */       logger.log(Level.WARNING, performer.getName() + " tried to harvest a tree with a " + tool.getName());
/*      */     } 
/* 5466 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean harvestBush(Action act, Creature performer, Item tool, int tilex, int tiley, int tile, Tiles.Tile theTile, float counter) {
/* 5472 */     boolean toReturn = true;
/* 5473 */     if (tool.getTemplateId() == 267) {
/*      */       
/* 5475 */       byte data = Tiles.decodeData(tile);
/* 5476 */       int age = FoliageAge.getAgeAsByte(data);
/* 5477 */       BushData.BushType bushType = theTile.getBushType(data);
/* 5478 */       String treeName = bushType.getName();
/*      */       
/* 5480 */       if (counter == 1.0F && !TileTreeBehaviour.hasFruit(performer, tilex, tiley, age)) {
/*      */         
/* 5482 */         performer.getCommunicator().sendNormalServerMessage("There is nothing to harvest on the " + treeName + ".");
/* 5483 */         return true;
/*      */       } 
/* 5485 */       int templateId = TileTreeBehaviour.getItem(tilex, tiley, age, bushType);
/* 5486 */       if (templateId == -10) {
/*      */         
/* 5488 */         performer.getCommunicator().sendNormalServerMessage("There is nothing to harvest on the " + treeName + ".");
/* 5489 */         return true;
/*      */       } 
/*      */       
/* 5492 */       toReturn = false;
/* 5493 */       int time = 150;
/* 5494 */       Skill skill = performer.getSkills().getSkillOrLearn(10048);
/* 5495 */       Skill toolSkill = performer.getSkills().getSkillOrLearn(10046);
/*      */       
/* 5497 */       if (counter == 1.0F) {
/*      */         
/* 5499 */         if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/* 5501 */           performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put what you harvest.");
/*      */           
/* 5503 */           return true;
/*      */         } 
/* 5505 */         int maxSearches = calcMaxHarvest(tile, skill.getKnowledge(0.0D), tool);
/* 5506 */         time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 5507 */         act.setNextTick(time);
/* 5508 */         act.setTickCount(1);
/* 5509 */         act.setData(0L);
/* 5510 */         float totalTime = (time * maxSearches);
/*      */         
/* 5512 */         performer.getCommunicator().sendNormalServerMessage("You start to harvest the " + treeName + ".");
/* 5513 */         Server.getInstance().broadCastAction(performer.getName() + " starts to harvest a bush.", performer, 5);
/* 5514 */         performer.sendActionControl(Actions.actionEntrys[152].getVerbString(), true, (int)totalTime);
/*      */       } 
/*      */       
/* 5517 */       if (act.justTickedSecond()) {
/* 5518 */         tool.setDamage(tool.getDamage() + 3.0E-4F * tool.getDamageModifier());
/*      */       }
/* 5520 */       if (counter * 10.0F >= act.getNextTick())
/*      */       {
/* 5522 */         if (act.getRarity() != 0) {
/* 5523 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 5525 */         int searchCount = act.getTickCount();
/* 5526 */         int maxSearches = calcMaxHarvest(tile, skill.getKnowledge(0.0D), tool);
/* 5527 */         act.incTickCount();
/* 5528 */         act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 5529 */         int knowledge = (int)skill.getKnowledge(0.0D);
/*      */         
/* 5531 */         performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */         
/* 5534 */         if (searchCount >= maxSearches) {
/* 5535 */           toReturn = true;
/*      */         }
/* 5537 */         act.setData(act.getData() + 1L);
/*      */         
/* 5539 */         double bonus = Math.max(1.0D, toolSkill.skillCheck(1.0D, tool, 0.0D, false, counter / searchCount));
/* 5540 */         double power = skill.skillCheck(skill.getKnowledge(0.0D) - 5.0D, tool, bonus, false, counter / searchCount);
/*      */         
/*      */         try {
/* 5543 */           float modifier = 1.0F;
/* 5544 */           if (tool.getSpellEffects() != null)
/*      */           {
/* 5546 */             modifier = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */           }
/*      */           
/* 5549 */           float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/* 5550 */           ql = Math.min(100.0F, (ql + tool.getRarity()) * modifier);
/* 5551 */           Item harvested = ItemFactory.createItem(templateId, Math.max(1.0F, ql), act.getRarity(), null);
/* 5552 */           if (ql < 0.0F) {
/* 5553 */             harvested.setDamage(-ql / 2.0F);
/*      */           }
/* 5555 */           performer.getInventory().insertItem(harvested);
/* 5556 */           SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/*      */           
/* 5558 */           if (searchCount == 1) {
/* 5559 */             TileTreeBehaviour.pick(tilex, tiley);
/*      */           }
/* 5561 */           performer.getCommunicator().sendNormalServerMessage("You harvest " + harvested
/* 5562 */               .getName() + " from the " + treeName + ".");
/* 5563 */           Server.getInstance().broadCastAction(performer
/* 5564 */               .getName() + " harvests " + harvested.getName() + " from a bush.", performer, 5);
/*      */ 
/*      */           
/* 5567 */           if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem())
/*      */           {
/* 5569 */             performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */             
/* 5571 */             toReturn = true;
/*      */           }
/*      */         
/* 5574 */         } catch (NoSuchTemplateException nst) {
/*      */           
/* 5576 */           logger.log(Level.WARNING, "No template for " + templateId, (Throwable)nst);
/* 5577 */           performer.getCommunicator().sendNormalServerMessage("You fail to harvest. You realize something is wrong with the world.");
/*      */         
/*      */         }
/* 5580 */         catch (FailedException fe) {
/*      */           
/* 5582 */           logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 5583 */           performer.getCommunicator().sendNormalServerMessage("You fail to harvest. You realize something is wrong with the world.");
/*      */         } 
/*      */ 
/*      */         
/* 5587 */         if (searchCount < maxSearches)
/*      */         {
/* 5589 */           act.setRarity(performer.getRarity());
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 5595 */       performer.getCommunicator().sendNormalServerMessage("You cannot harvest with that.");
/* 5596 */       logger.log(Level.WARNING, performer.getName() + " tried to harvest a bush with a " + tool.getName());
/*      */     } 
/* 5598 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean prune(Action action, Creature performer, Item sickle, int tilex, int tiley, int tile, Tiles.Tile theTile, float counter) {
/* 5604 */     boolean toReturn = true;
/*      */     
/* 5606 */     if (sickle.getTemplateId() == 267) {
/*      */       
/* 5608 */       if (theTile.isEnchanted()) {
/*      */         
/* 5610 */         performer.getCommunicator().sendNormalServerMessage("It does not make sense to prune that.");
/* 5611 */         return true;
/*      */       } 
/* 5613 */       byte data = Tiles.decodeData(tile);
/* 5614 */       FoliageAge age = FoliageAge.getFoliageAge(data);
/* 5615 */       String treeName = theTile.getTileName(data).toLowerCase();
/*      */       
/* 5617 */       boolean ok = false;
/*      */       
/* 5619 */       if (age.isPrunable() || (age == FoliageAge.SHRIVELLED && theTile.isThorn(data)))
/*      */       {
/* 5621 */         ok = true;
/*      */       }
/* 5623 */       if (!ok) {
/*      */         
/* 5625 */         performer.getCommunicator().sendNormalServerMessage("It does not make sense to prune now.");
/* 5626 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 5630 */       toReturn = false;
/* 5631 */       int time = 150;
/* 5632 */       Skill forestry = performer.getSkills().getSkillOrLearn(10048);
/* 5633 */       Skill sickskill = performer.getSkills().getSkillOrLearn(10046);
/* 5634 */       if (sickle.getTemplateId() == 267)
/*      */       {
/* 5636 */         time = Actions.getStandardActionTime(performer, forestry, sickle, sickskill.getKnowledge(0.0D));
/*      */       }
/* 5638 */       if (counter == 1.0F) {
/*      */ 
/*      */         
/* 5641 */         performer.getCommunicator().sendNormalServerMessage("You start to prune the " + treeName + ".");
/* 5642 */         Server.getInstance().broadCastAction(performer.getName() + " starts to prune the " + treeName + ".", performer, 5);
/*      */         
/* 5644 */         performer.sendActionControl(Actions.actionEntrys[373].getVerbString(), true, time);
/*      */       } 
/*      */       
/* 5647 */       if (action.justTickedSecond())
/* 5648 */         sickle.setDamage(sickle.getDamage() + 3.0E-4F * sickle.getDamageModifier()); 
/* 5649 */       if (counter * 10.0F >= time)
/*      */       {
/* 5651 */         double bonus = 0.0D;
/* 5652 */         double power = 0.0D;
/*      */         
/* 5654 */         bonus = Math.max(1.0D, sickskill.skillCheck(1.0D, sickle, 0.0D, false, counter));
/* 5655 */         power = forestry.skillCheck(forestry.getKnowledge(0.0D) - 10.0D, sickle, bonus, false, counter);
/*      */         
/* 5657 */         toReturn = true;
/*      */         
/* 5659 */         SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/* 5660 */         if (power < 0.0D) {
/*      */           
/* 5662 */           performer.getCommunicator().sendNormalServerMessage("You make a lot of errors and need to take a break.");
/*      */           
/* 5664 */           return toReturn;
/*      */         } 
/*      */         
/* 5667 */         FoliageAge newage = age.getPrunedAge();
/* 5668 */         int newData = newage.encodeAsData() + (data & 0xF) & 0xFF;
/*      */ 
/*      */         
/* 5671 */         Server.setSurfaceTile(tilex, tiley, 
/* 5672 */             Tiles.decodeHeight(tile), Tiles.decodeType(tile), (byte)newData);
/* 5673 */         TileEvent.log(tilex, tiley, 0, performer.getWurmId(), 373);
/* 5674 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 5675 */         performer.getCommunicator().sendNormalServerMessage("You prune the " + treeName + ".");
/* 5676 */         Server.getInstance().broadCastAction(performer.getName() + " prunes the " + treeName + ".", performer, 5);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 5682 */       performer.getCommunicator().sendNormalServerMessage("You cannot prune with that.");
/* 5683 */       logger.log(Level.WARNING, performer.getName() + " tried to prune with a " + sickle.getName());
/*      */     } 
/* 5685 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean pickWurms(Action act, Creature performer, int tilex, int tiley, int tile, float counter) {
/* 5691 */     boolean toReturn = true;
/*      */     
/* 5693 */     if (counter == 1.0F && !Server.hasGrubs(tilex, tiley)) {
/*      */       
/* 5695 */       performer.getCommunicator().sendNormalServerMessage("There see no wurms casts here.");
/* 5696 */       return true;
/*      */     } 
/* 5698 */     toReturn = false;
/* 5699 */     int time = 150;
/* 5700 */     Skill skill = performer.getSkills().getSkillOrLearn(10071);
/*      */     
/* 5702 */     if (counter == 1.0F) {
/*      */       
/* 5704 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 5706 */         performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put any grubs.");
/*      */         
/* 5708 */         return true;
/*      */       } 
/* 5710 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 5711 */       time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 5712 */       act.setNextTick(time);
/* 5713 */       act.setTickCount(1);
/* 5714 */       act.setData(0L);
/* 5715 */       float totalTime = (time * maxSearches);
/*      */       
/*      */       try {
/* 5718 */         performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */       }
/* 5720 */       catch (NoSuchActionException nsa) {
/*      */         
/* 5722 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/* 5724 */       performer.getCommunicator().sendNormalServerMessage("You start to search the dirt tile for wurms.");
/* 5725 */       Server.getInstance().broadCastAction(performer.getName() + " starts to search a dirt tile for wurms.", performer, 5);
/* 5726 */       performer.sendActionControl(Actions.actionEntrys[935].getVerbString(), true, (int)totalTime);
/* 5727 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     } 
/* 5729 */     if (counter * 10.0F >= act.getNextTick()) {
/*      */       
/* 5731 */       if (act.getRarity() != 0) {
/* 5732 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/* 5734 */       int searchCount = act.getTickCount();
/* 5735 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 5736 */       act.incTickCount();
/* 5737 */       act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 5738 */       int knowledge = (int)skill.getKnowledge(0.0D);
/*      */       
/* 5740 */       performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */       
/* 5743 */       if (searchCount >= maxSearches) {
/* 5744 */         toReturn = true;
/*      */       }
/* 5746 */       act.setData(act.getData() + 1L);
/* 5747 */       double bonus = 0.0D;
/* 5748 */       double diff = skill.getKnowledge(0.0D) - 10.0D + (searchCount * 5);
/* 5749 */       double power = skill.skillCheck(diff, null, bonus, false, counter / searchCount);
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 5754 */         float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/*      */         
/* 5756 */         Item wurm = ItemFactory.createItem(1362, Math.max(1.0F, ql), act.getRarity(), null);
/* 5757 */         if (ql < 0.0F) {
/* 5758 */           wurm.setDamage(-ql / 2.0F);
/*      */         }
/* 5760 */         performer.getInventory().insertItem(wurm);
/* 5761 */         SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/*      */         
/* 5763 */         if (searchCount == 1) {
/* 5764 */           Server.setGrubs(tilex, tiley, false);
/*      */         }
/* 5766 */         performer.getCommunicator().sendNormalServerMessage("You do a rain danec on the dirt tile and " + wurm
/* 5767 */             .getNameWithGenus() + " pops to the surface, which you grab.");
/* 5768 */         Server.getInstance().broadCastAction(performer
/* 5769 */             .getName() + " danecs on a dirt tile and grabs " + wurm.getNameWithGenus() + " that pops to the surface.", performer, 5);
/*      */ 
/*      */         
/* 5772 */         if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/* 5774 */           performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */           
/* 5776 */           return true;
/*      */         } 
/* 5778 */         if (ql < 0.0F && searchCount < maxSearches)
/*      */         {
/* 5780 */           performer.getCommunicator().sendNormalServerMessage("You make such a mess, you stop searching.");
/*      */           
/* 5782 */           return true;
/*      */         }
/*      */       
/* 5785 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 5787 */         logger.log(Level.WARNING, "No template for 1364", (Throwable)nst);
/* 5788 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any wurms. You realize something is wrong with the world.");
/*      */       
/*      */       }
/* 5791 */       catch (FailedException fe) {
/*      */         
/* 5793 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 5794 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any wurms. You realize something is wrong with the world.");
/*      */       } 
/*      */ 
/*      */       
/* 5798 */       if (searchCount < maxSearches)
/*      */       {
/* 5800 */         act.setRarity(performer.getRarity());
/*      */       }
/*      */     } 
/* 5803 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean pickGrubs(Action act, Creature performer, Item tool, int tilex, int tiley, int tile, Tiles.Tile theTile, float counter) {
/* 5809 */     boolean toReturn = true;
/* 5810 */     byte data = Tiles.decodeData(tile);
/* 5811 */     int age = FoliageAge.getAgeAsByte(data);
/* 5812 */     TreeData.TreeType treeType = theTile.getTreeType(data);
/*      */     
/* 5814 */     if (tool.getTemplateId() == 390) {
/*      */       
/* 5816 */       String treeName = treeType.getName();
/* 5817 */       if (counter == 1.0F && (age != 15 || !Server.hasGrubs(tilex, tiley))) {
/*      */         
/* 5819 */         performer.getCommunicator().sendNormalServerMessage("You find no grubs on the " + treeName + ".");
/* 5820 */         return true;
/*      */       } 
/* 5822 */       toReturn = false;
/* 5823 */       int time = 150;
/* 5824 */       Skill skill = performer.getSkills().getSkillOrLearn(10048);
/*      */ 
/*      */ 
/*      */       
/* 5828 */       if (counter == 1.0F) {
/*      */         
/* 5830 */         if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/* 5832 */           performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put any grubs.");
/*      */           
/* 5834 */           return true;
/*      */         } 
/* 5836 */         int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), tool);
/* 5837 */         time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 5838 */         act.setNextTick(time);
/* 5839 */         act.setTickCount(1);
/* 5840 */         act.setData(0L);
/* 5841 */         float totalTime = (time * maxSearches);
/*      */         
/*      */         try {
/* 5844 */           performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */         }
/* 5846 */         catch (NoSuchActionException nsa) {
/*      */           
/* 5848 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/* 5850 */         performer.getCommunicator().sendNormalServerMessage("You start to search the " + treeName + " for grubs.");
/* 5851 */         Server.getInstance().broadCastAction(performer.getName() + " starts to search a tree for grubs.", performer, 5);
/* 5852 */         performer.sendActionControl(Actions.actionEntrys[935].getVerbString(), true, (int)totalTime);
/* 5853 */         performer.getStatus().modifyStamina(-500.0F);
/*      */       } 
/* 5855 */       if (tool != null && act.justTickedSecond()) {
/* 5856 */         tool.setDamage(tool.getDamage() + 3.0E-4F * tool.getDamageModifier());
/*      */       }
/* 5858 */       if (counter * 10.0F >= act.getNextTick())
/*      */       {
/* 5860 */         if (act.getRarity() != 0) {
/* 5861 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 5863 */         int searchCount = act.getTickCount();
/* 5864 */         int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), tool);
/* 5865 */         act.incTickCount();
/* 5866 */         act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 5867 */         int knowledge = (int)skill.getKnowledge(0.0D);
/*      */         
/* 5869 */         performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */         
/* 5872 */         if (searchCount >= maxSearches) {
/* 5873 */           toReturn = true;
/*      */         }
/* 5875 */         act.setData(act.getData() + 1L);
/* 5876 */         double bonus = 0.0D;
/* 5877 */         if (tool.getTemplateId() == 390);
/*      */ 
/*      */ 
/*      */         
/* 5881 */         double diff = skill.getKnowledge(0.0D) - 10.0D + (searchCount * 5);
/* 5882 */         double power = skill.skillCheck(diff, tool, bonus, false, counter / searchCount);
/*      */         
/*      */         try {
/* 5885 */           float modifier = 1.0F;
/* 5886 */           if (tool.getSpellEffects() != null)
/*      */           {
/* 5888 */             modifier = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */           }
/*      */           
/* 5891 */           float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/* 5892 */           ql = Math.min(100.0F, (ql + tool.getRarity()) * modifier);
/* 5893 */           Item grub = ItemFactory.createItem(1364, Math.max(1.0F, ql), act.getRarity(), null);
/* 5894 */           if (ql < 0.0F) {
/* 5895 */             grub.setDamage(-ql / 2.0F);
/*      */           }
/* 5897 */           performer.getInventory().insertItem(grub);
/* 5898 */           SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/*      */           
/* 5900 */           if (searchCount == 1) {
/* 5901 */             Server.setGrubs(tilex, tiley, false);
/*      */           }
/* 5903 */           performer.getCommunicator().sendNormalServerMessage("You prise " + grub
/* 5904 */               .getNameWithGenus() + " from the " + treeName + ".");
/* 5905 */           Server.getInstance().broadCastAction(performer
/* 5906 */               .getName() + " prises a " + grub.getName() + " from a tree.", performer, 5);
/*      */ 
/*      */           
/* 5909 */           if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem()) {
/*      */             
/* 5911 */             performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */             
/* 5913 */             return true;
/*      */           } 
/* 5915 */           if (ql < 0.0F && searchCount < maxSearches)
/*      */           {
/* 5917 */             performer.getCommunicator().sendNormalServerMessage("You make such a mess, you stop searching.");
/*      */             
/* 5919 */             return true;
/*      */           }
/*      */         
/* 5922 */         } catch (NoSuchTemplateException nst) {
/*      */           
/* 5924 */           logger.log(Level.WARNING, "No template for 1364", (Throwable)nst);
/* 5925 */           performer.getCommunicator().sendNormalServerMessage("You fail to find any grubs. You realize something is wrong with the world.");
/*      */         
/*      */         }
/* 5928 */         catch (FailedException fe) {
/*      */           
/* 5930 */           logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 5931 */           performer.getCommunicator().sendNormalServerMessage("You fail to find any grubs. You realize something is wrong with the world.");
/*      */         } 
/*      */ 
/*      */         
/* 5935 */         if (searchCount < maxSearches)
/*      */         {
/* 5937 */           act.setRarity(performer.getRarity());
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 5943 */       performer.getCommunicator().sendNormalServerMessage("You cannot prise with that.");
/* 5944 */       logger.log(Level.WARNING, performer.getName() + " tried to prise grubs from a tree with a " + tool.getName());
/*      */     } 
/* 5946 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean pickBark(Action act, Creature performer, int tilex, int tiley, int tile, Tiles.Tile theTile, float counter) {
/* 5952 */     boolean toReturn = true;
/* 5953 */     byte data = Tiles.decodeData(tile);
/* 5954 */     int age = FoliageAge.getAgeAsByte(data);
/* 5955 */     TreeData.TreeType treeType = theTile.getTreeType(data);
/*      */     
/* 5957 */     String treeName = treeType.getName();
/* 5958 */     if (counter == 1.0F && (age != 14 || !Server.hasGrubs(tilex, tiley))) {
/*      */       
/* 5960 */       performer.getCommunicator().sendNormalServerMessage("There see no loose bark on the " + treeName + ".");
/* 5961 */       return true;
/*      */     } 
/* 5963 */     toReturn = false;
/* 5964 */     int time = 150;
/* 5965 */     Skill skill = performer.getSkills().getSkillOrLearn(10048);
/*      */     
/* 5967 */     if (counter == 1.0F) {
/*      */       
/* 5969 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 5971 */         performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put any bark.");
/*      */         
/* 5973 */         return true;
/*      */       } 
/* 5975 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 5976 */       time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 5977 */       act.setNextTick(time);
/* 5978 */       act.setTickCount(1);
/* 5979 */       act.setData(0L);
/* 5980 */       float totalTime = (time * maxSearches);
/*      */       
/*      */       try {
/* 5983 */         performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */       }
/* 5985 */       catch (NoSuchActionException nsa) {
/*      */         
/* 5987 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/* 5989 */       performer.getCommunicator().sendNormalServerMessage("You start to search the " + treeName + " for loose bark.");
/* 5990 */       Server.getInstance().broadCastAction(performer.getName() + " starts to search a tree.", performer, 5);
/* 5991 */       performer.sendActionControl(Actions.actionEntrys[935].getVerbString(), true, (int)totalTime);
/* 5992 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     } 
/* 5994 */     if (counter * 10.0F >= act.getNextTick()) {
/*      */       
/* 5996 */       if (act.getRarity() != 0) {
/* 5997 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/* 5999 */       int searchCount = act.getTickCount();
/* 6000 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 6001 */       act.incTickCount();
/* 6002 */       act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 6003 */       int knowledge = (int)skill.getKnowledge(0.0D);
/*      */       
/* 6005 */       performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */       
/* 6008 */       if (searchCount >= maxSearches) {
/* 6009 */         toReturn = true;
/*      */       }
/* 6011 */       act.setData(act.getData() + 1L);
/* 6012 */       double bonus = 0.0D;
/* 6013 */       double diff = skill.getKnowledge(0.0D) - 10.0D + (searchCount * 5);
/* 6014 */       double power = skill.skillCheck(diff, null, bonus, false, counter / searchCount);
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 6019 */         float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/*      */         
/* 6021 */         Item bark = ItemFactory.createItem(1355, Math.max(1.0F, ql), act.getRarity(), null);
/* 6022 */         if (ql < 0.0F) {
/* 6023 */           bark.setDamage(-ql / 2.0F);
/*      */         }
/* 6025 */         performer.getInventory().insertItem(bark);
/* 6026 */         SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/*      */         
/* 6028 */         if (searchCount == 1) {
/* 6029 */           Server.setGrubs(tilex, tiley, false);
/*      */         }
/* 6031 */         performer.getCommunicator().sendNormalServerMessage("You remove " + bark
/* 6032 */             .getNameWithGenus() + " from the " + treeName + ".");
/* 6033 */         Server.getInstance().broadCastAction(performer
/* 6034 */             .getName() + " break a piece of " + bark.getName() + " from a tree.", performer, 5);
/*      */ 
/*      */         
/* 6037 */         if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/* 6039 */           performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */           
/* 6041 */           return true;
/*      */         } 
/* 6043 */         if (ql < 0.0F && searchCount < maxSearches)
/*      */         {
/* 6045 */           performer.getCommunicator().sendNormalServerMessage("You make such a mess, you stop searching.");
/*      */           
/* 6047 */           return true;
/*      */         }
/*      */       
/* 6050 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 6052 */         logger.log(Level.WARNING, "No template for 1364", (Throwable)nst);
/* 6053 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any loose bark. You realize something is wrong with the world.");
/*      */       
/*      */       }
/* 6056 */       catch (FailedException fe) {
/*      */         
/* 6058 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 6059 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any loose bark. You realize something is wrong with the world.");
/*      */       } 
/*      */ 
/*      */       
/* 6063 */       if (searchCount < maxSearches)
/*      */       {
/* 6065 */         act.setRarity(performer.getRarity());
/*      */       }
/*      */     } 
/* 6068 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean findTwigs(Action act, Creature performer, int tilex, int tiley, int tile, Tiles.Tile theTile, float counter) {
/* 6074 */     boolean toReturn = true;
/* 6075 */     byte data = Tiles.decodeData(tile);
/* 6076 */     int age = FoliageAge.getAgeAsByte(data);
/* 6077 */     BushData.BushType bushType = theTile.getBushType(data);
/*      */     
/* 6079 */     String bushName = bushType.getName();
/* 6080 */     if (counter == 1.0F && (age != 14 || !Server.hasGrubs(tilex, tiley))) {
/*      */       
/* 6082 */       performer.getCommunicator().sendNormalServerMessage("There see no twigs under the " + bushName + ".");
/* 6083 */       return true;
/*      */     } 
/* 6085 */     toReturn = false;
/* 6086 */     int time = 150;
/* 6087 */     Skill skill = performer.getSkills().getSkillOrLearn(10048);
/*      */     
/* 6089 */     if (counter == 1.0F) {
/*      */       
/* 6091 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 6093 */         performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put any twigs.");
/*      */         
/* 6095 */         return true;
/*      */       } 
/* 6097 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 6098 */       time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 6099 */       act.setNextTick(time);
/* 6100 */       act.setTickCount(1);
/* 6101 */       act.setData(0L);
/* 6102 */       float totalTime = (time * maxSearches);
/*      */       
/*      */       try {
/* 6105 */         performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */       }
/* 6107 */       catch (NoSuchActionException nsa) {
/*      */         
/* 6109 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/* 6111 */       performer.getCommunicator().sendNormalServerMessage("You start to search under the " + bushName + " for twigs.");
/* 6112 */       Server.getInstance().broadCastAction(performer.getName() + " starts to search under a bush.", performer, 5);
/* 6113 */       performer.sendActionControl(Actions.actionEntrys[935].getVerbString(), true, (int)totalTime);
/* 6114 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     } 
/* 6116 */     if (counter * 10.0F >= act.getNextTick()) {
/*      */       
/* 6118 */       if (act.getRarity() != 0) {
/* 6119 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/* 6121 */       int searchCount = act.getTickCount();
/* 6122 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 6123 */       act.incTickCount();
/* 6124 */       act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 6125 */       int knowledge = (int)skill.getKnowledge(0.0D);
/*      */       
/* 6127 */       performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */       
/* 6130 */       if (searchCount >= maxSearches) {
/* 6131 */         toReturn = true;
/*      */       }
/* 6133 */       act.setData(act.getData() + 1L);
/* 6134 */       double bonus = 0.0D;
/* 6135 */       double diff = skill.getKnowledge(0.0D) - 10.0D + (searchCount * 5);
/* 6136 */       double power = skill.skillCheck(diff, null, bonus, false, counter / searchCount);
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 6141 */         float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/* 6142 */         ql = Math.min(100.0F, ql);
/* 6143 */         Item twig = ItemFactory.createItem(1353, Math.max(1.0F, ql), act.getRarity(), null);
/* 6144 */         twig.setMaterial(bushType.getMaterial());
/* 6145 */         if (ql < 0.0F) {
/* 6146 */           twig.setDamage(-ql / 2.0F);
/*      */         }
/* 6148 */         performer.getInventory().insertItem(twig);
/* 6149 */         SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/*      */         
/* 6151 */         if (searchCount == 1) {
/* 6152 */           Server.setGrubs(tilex, tiley, false);
/*      */         }
/* 6154 */         performer.getCommunicator().sendNormalServerMessage("You find " + twig
/* 6155 */             .getNameWithGenus() + " under the " + bushName + ".");
/* 6156 */         Server.getInstance().broadCastAction(performer
/* 6157 */             .getName() + " finds something under a bush.", performer, 5);
/*      */ 
/*      */         
/* 6160 */         if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/* 6162 */           performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */           
/* 6164 */           return true;
/*      */         } 
/* 6166 */         if (ql < 0.0F && searchCount < maxSearches)
/*      */         {
/* 6168 */           performer.getCommunicator().sendNormalServerMessage("You make such a mess, you stop searching.");
/*      */           
/* 6170 */           return true;
/*      */         }
/*      */       
/* 6173 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 6175 */         logger.log(Level.WARNING, "No template for 1364", (Throwable)nst);
/* 6176 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any twigs. You realize something is wrong with the world.");
/*      */       
/*      */       }
/* 6179 */       catch (FailedException fe) {
/*      */         
/* 6181 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 6182 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any twigs. You realize something is wrong with the world.");
/*      */       } 
/*      */ 
/*      */       
/* 6186 */       if (searchCount < maxSearches)
/*      */       {
/* 6188 */         act.setRarity(performer.getRarity());
/*      */       }
/*      */     } 
/* 6191 */     return toReturn;
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
/*      */   static boolean findFeathers(Action act, Creature performer, int tilex, int tiley, int tile, float counter) {
/* 6207 */     boolean toReturn = true;
/*      */     
/* 6209 */     if (counter == 1.0F && !Server.hasGrubs(tilex, tiley)) {
/*      */       
/* 6211 */       performer.getCommunicator().sendNormalServerMessage("The area looks picked clean.");
/* 6212 */       return true;
/*      */     } 
/* 6214 */     toReturn = false;
/* 6215 */     int time = 150;
/* 6216 */     Skill skill = performer.getSkills().getSkillOrLearn(10071);
/*      */     
/* 6218 */     if (counter == 1.0F) {
/*      */       
/* 6220 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */         
/* 6222 */         performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put any feathers.");
/*      */         
/* 6224 */         return true;
/*      */       } 
/* 6226 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 6227 */       time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 6228 */       act.setNextTick(time);
/* 6229 */       act.setTickCount(1);
/* 6230 */       act.setData(0L);
/* 6231 */       float totalTime = (time * maxSearches);
/*      */       
/*      */       try {
/* 6234 */         performer.getCurrentAction().setTimeLeft((int)totalTime);
/*      */       }
/* 6236 */       catch (NoSuchActionException nsa) {
/*      */         
/* 6238 */         logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */       } 
/* 6240 */       performer.getCommunicator().sendNormalServerMessage("You start to search the tile for feathers.");
/* 6241 */       Server.getInstance().broadCastAction(performer.getName() + " starts to search a tile for feathers.", performer, 5);
/* 6242 */       performer.sendActionControl(Actions.actionEntrys[935].getVerbString(), true, (int)totalTime);
/* 6243 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     } 
/* 6245 */     if (counter * 10.0F >= act.getNextTick()) {
/*      */       
/* 6247 */       if (act.getRarity() != 0) {
/* 6248 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/* 6250 */       int searchCount = act.getTickCount();
/* 6251 */       int maxSearches = calcMaxGrubs(skill.getKnowledge(0.0D), null);
/* 6252 */       act.incTickCount();
/* 6253 */       act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 6254 */       int knowledge = (int)skill.getKnowledge(0.0D);
/*      */       
/* 6256 */       performer.getStatus().modifyStamina((-1500 * searchCount));
/*      */ 
/*      */       
/* 6259 */       if (searchCount >= maxSearches) {
/* 6260 */         toReturn = true;
/*      */       }
/* 6262 */       act.setData(act.getData() + 1L);
/* 6263 */       double bonus = 0.0D;
/* 6264 */       double diff = skill.getKnowledge(0.0D) - 10.0D + (searchCount * 5);
/* 6265 */       double power = skill.skillCheck(diff, null, bonus, false, counter / searchCount);
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 6270 */         float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/*      */         
/* 6272 */         Item feather = ItemFactory.createItem(1352, Math.max(1.0F, ql), act.getRarity(), null);
/* 6273 */         if (ql < 0.0F) {
/* 6274 */           feather.setDamage(-ql / 2.0F);
/*      */         }
/* 6276 */         performer.getInventory().insertItem(feather);
/* 6277 */         SoundPlayer.playSound("sound.forest.branchsnap", tilex, tiley, true, 3.0F);
/*      */         
/* 6279 */         if (searchCount == 1) {
/* 6280 */           Server.setGrubs(tilex, tiley, false);
/*      */         }
/* 6282 */         performer.getCommunicator().sendNormalServerMessage("You find " + feather
/* 6283 */             .getNameWithGenus() + " on the tile.");
/* 6284 */         Server.getInstance().broadCastAction(performer
/* 6285 */             .getName() + " finds a " + feather.getName() + " on the tile.", performer, 5);
/*      */ 
/*      */         
/* 6288 */         if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/* 6290 */           performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*      */           
/* 6292 */           return true;
/*      */         } 
/* 6294 */         if (ql < 0.0F && searchCount < maxSearches)
/*      */         {
/* 6296 */           performer.getCommunicator().sendNormalServerMessage("You make such a mess, you stop searching.");
/*      */           
/* 6298 */           return true;
/*      */         }
/*      */       
/* 6301 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 6303 */         logger.log(Level.WARNING, "No template for 1364", (Throwable)nst);
/* 6304 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any feathers. You realize something is wrong with the world.");
/*      */       
/*      */       }
/* 6307 */       catch (FailedException fe) {
/*      */         
/* 6309 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 6310 */         performer.getCommunicator().sendNormalServerMessage("You fail to find any feathers. You realize something is wrong with the world.");
/*      */       } 
/*      */ 
/*      */       
/* 6314 */       if (searchCount < maxSearches)
/*      */       {
/* 6316 */         act.setRarity(performer.getRarity());
/*      */       }
/*      */     } 
/* 6319 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean pruneHedge(Action action, Creature performer, Item sickle, Fence hedge, boolean onSurface, float counter) {
/* 6325 */     boolean toReturn = true;
/* 6326 */     boolean insta = (sickle.getTemplateId() == 176 && performer.getPower() >= 2);
/* 6327 */     if (sickle.getTemplateId() == 267 || insta) {
/*      */       
/* 6329 */       if (!hedge.isHedge()) {
/*      */         
/* 6331 */         performer.getCommunicator().sendNormalServerMessage("It does not make sense to prune that.");
/* 6332 */         return true;
/*      */       } 
/* 6334 */       if (hedge.isLowHedge()) {
/*      */         
/* 6336 */         performer.getCommunicator().sendNormalServerMessage("The hedge is too low to be pruned.");
/* 6337 */         return true;
/*      */       } 
/* 6339 */       toReturn = false;
/* 6340 */       int time = 1;
/* 6341 */       Skill forestry = performer.getSkills().getSkillOrLearn(10048);
/* 6342 */       Skill sickskill = performer.getSkills().getSkillOrLearn(10046);
/* 6343 */       if (sickle.getTemplateId() == 267)
/*      */       {
/* 6345 */         time = Actions.getStandardActionTime(performer, forestry, sickle, sickskill.getKnowledge(0.0D));
/*      */       }
/* 6347 */       if (counter == 1.0F && !insta) {
/*      */         
/* 6349 */         performer.getCommunicator().sendNormalServerMessage("You start to prune the hedge.");
/* 6350 */         Server.getInstance().broadCastAction(performer.getName() + " starts to prune the hedge.", performer, 5);
/* 6351 */         performer.sendActionControl(Actions.actionEntrys[373].getVerbString(), true, time);
/*      */       } 
/* 6353 */       if (action.justTickedSecond() && sickle.getTemplateId() == 267)
/* 6354 */         sickle.setDamage(sickle.getDamage() + 3.0E-4F * sickle.getDamageModifier()); 
/* 6355 */       if (counter * 10.0F >= time) {
/*      */         
/* 6357 */         double bonus = 0.0D;
/* 6358 */         double power = 0.0D;
/*      */         
/* 6360 */         bonus = Math.max(1.0D, sickskill.skillCheck(1.0D, sickle, 0.0D, false, counter));
/* 6361 */         power = forestry.skillCheck(forestry.getKnowledge(0.0D) - 10.0D, sickle, bonus, false, counter);
/*      */         
/* 6363 */         toReturn = true;
/*      */         
/* 6365 */         SoundPlayer.playSound("sound.forest.branchsnap", hedge.getTileX(), hedge.getTileY(), onSurface, 2.0F);
/* 6366 */         if (power < 0.0D) {
/*      */           
/* 6368 */           performer.getCommunicator().sendNormalServerMessage("You make a lot of errors and need to take a break.");
/* 6369 */           return toReturn;
/*      */         } 
/* 6371 */         hedge.setDamage(0.0F);
/* 6372 */         hedge.setType(StructureConstantsEnum.getEnumByValue((short)(byte)((hedge.getType()).value - 1)));
/*      */         
/*      */         try {
/* 6375 */           hedge.save();
/* 6376 */           VolaTile tile = Zones.getTileOrNull(hedge.getTileX(), hedge.getTileY(), hedge.isOnSurface());
/* 6377 */           if (tile != null)
/*      */           {
/* 6379 */             tile.updateFence(hedge);
/*      */           }
/*      */         }
/* 6382 */         catch (IOException iox) {
/*      */           
/* 6384 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         } 
/* 6386 */         TileEvent.log(hedge.getTileX(), hedge.getTileY(), 0, performer.getWurmId(), 373);
/* 6387 */         performer.getCommunicator().sendNormalServerMessage("You prune the hedge.");
/* 6388 */         Server.getInstance().broadCastAction(performer.getName() + " prunes the hedge.", performer, 5);
/*      */       } 
/*      */     } 
/* 6391 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean chopHedge(Action act, Creature performer, Item tool, Fence hedge, boolean onSurface, float counter) {
/* 6397 */     boolean toReturn = true;
/* 6398 */     boolean insta = (tool.getTemplateId() == 176 && performer.getPower() >= 2);
/*      */     
/* 6400 */     if (!tool.isWeaponSlash() && tool.getTemplateId() != 24 && !insta)
/*      */     {
/*      */       
/* 6403 */       return true;
/*      */     }
/* 6405 */     if (!hedge.isHedge()) {
/*      */       
/* 6407 */       performer.getCommunicator().sendNormalServerMessage("It does not make sense to chop that.");
/* 6408 */       return true;
/*      */     } 
/* 6410 */     if (!Methods.isActionAllowed(performer, (short)96, hedge.getTileX(), hedge.getTileY())) {
/* 6411 */       return true;
/*      */     }
/* 6413 */     toReturn = false;
/* 6414 */     int time = 1;
/*      */     
/*      */     try {
/* 6417 */       Skill forestry = performer.getSkills().getSkillOrLearn(10048);
/* 6418 */       Skill primskill = performer.getSkills().getSkillOrLearn(tool.getPrimarySkill());
/*      */       
/* 6420 */       int hedgeAge = 4;
/* 6421 */       if (hedge.isMediumHedge())
/* 6422 */         hedgeAge = 9; 
/* 6423 */       if (hedge.isHighHedge()) {
/* 6424 */         hedgeAge = 14;
/*      */       }
/* 6426 */       float qualityLevel = 0.0F;
/* 6427 */       if (counter == 1.0F && !insta) {
/*      */         
/* 6429 */         time = (int)(calcTime(hedgeAge, tool, primskill, forestry) * Actions.getStaminaModiferFor(performer, 20000));
/*      */         
/* 6431 */         time = Math.min(65535, time);
/* 6432 */         act.setTimeLeft(time);
/* 6433 */         performer.getCommunicator().sendNormalServerMessage("You start to cut down the " + hedge.getName() + ".");
/* 6434 */         Server.getInstance().broadCastAction(performer
/* 6435 */             .getName() + " starts to cut down the " + hedge.getName() + ".", performer, 5);
/* 6436 */         act.setActionString("cutting down " + hedge.getName());
/* 6437 */         performer.sendActionControl("cutting down " + hedge.getName(), true, time);
/* 6438 */         performer.getStatus().modifyStamina(-1500.0F);
/* 6439 */         if (tool.isWeaponAxe()) {
/* 6440 */           tool.setDamage(tool.getDamage() + 0.001F * tool.getDamageModifier());
/*      */         } else {
/* 6442 */           tool.setDamage(tool.getDamage() + 0.0025F * tool.getDamageModifier());
/*      */         } 
/* 6444 */       } else if (!insta) {
/*      */         
/* 6446 */         time = act.getTimeLeft();
/*      */         
/* 6448 */         if (act.justTickedSecond() && ((time < 50 && act.currentSecond() % 2 == 0) || act.currentSecond() % 5 == 0)) {
/*      */           
/* 6450 */           performer.getStatus().modifyStamina(-6000.0F);
/* 6451 */           if (tool.isWeaponAxe()) {
/* 6452 */             tool.setDamage(tool.getDamage() + 0.001F * tool.getDamageModifier());
/*      */           } else {
/* 6454 */             tool.setDamage(tool.getDamage() + 0.0025F * tool.getDamageModifier());
/*      */           } 
/* 6456 */         }  if (act.justTickedSecond() && counter * 10.0F < (time - 30))
/*      */         {
/* 6458 */           if (tool.getTemplateId() != 24) {
/*      */             
/* 6460 */             if ((act.currentSecond() - 2) % 3 == 0)
/*      */             {
/* 6462 */               String sstring = "sound.work.woodcutting1";
/* 6463 */               int x = Server.rand.nextInt(3);
/* 6464 */               if (x == 0) {
/* 6465 */                 sstring = "sound.work.woodcutting2";
/* 6466 */               } else if (x == 1) {
/* 6467 */                 sstring = "sound.work.woodcutting3";
/* 6468 */               }  SoundPlayer.playSound(sstring, hedge.getTileX(), hedge.getTileY(), performer.isOnSurface(), 1.0F);
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/* 6475 */           else if ((act.currentSecond() - 2) % 6 == 0 && counter * 10.0F < (time - 50)) {
/*      */             
/* 6477 */             String sstring = "sound.work.carpentry.saw";
/* 6478 */             SoundPlayer.playSound("sound.work.carpentry.saw", hedge.getTileX(), hedge.getTileY(), performer.isOnSurface(), 1.0F);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 6483 */       if (counter * 10.0F > time || insta) {
/*      */         
/* 6485 */         toReturn = true;
/* 6486 */         double bonus = 0.0D;
/* 6487 */         double hedgeDifficulty = hedge.getDifficulty();
/* 6488 */         float skillTick = 0.0F;
/*      */         
/* 6490 */         if (Servers.localServer.challengeServer) {
/*      */           
/* 6492 */           skillTick = Math.min(20.0F, counter);
/*      */         }
/*      */         else {
/*      */           
/* 6496 */           skillTick = Math.min(20.0F, counter);
/*      */         } 
/* 6498 */         if (tool.getTemplateId() == 7 || tool.getTemplateId() == 24) {
/* 6499 */           bonus = Math.max(0.0D, primskill
/* 6500 */               .skillCheck(hedgeDifficulty, tool, 0.0D, false, skillTick));
/*      */         } else {
/* 6502 */           bonus = Math.max(0.0D, primskill
/*      */               
/* 6504 */               .skillCheck(hedgeDifficulty, tool, 0.0D, (primskill.getKnowledge(0.0D) > 20.0D), skillTick));
/*      */         } 
/* 6506 */         qualityLevel = Math.max(1.0F, 
/* 6507 */             (float)forestry.skillCheck(hedgeDifficulty, tool, bonus, false, skillTick));
/* 6508 */         double imbueEnhancement = 1.0D + 0.23047D * tool.getSkillSpellImprovement(1007) / 100.0D;
/*      */         
/* 6510 */         double woodc = forestry.getKnowledge(0.0D) * imbueEnhancement;
/*      */         
/* 6512 */         if (woodc < qualityLevel) {
/* 6513 */           qualityLevel = (float)woodc;
/*      */         }
/* 6515 */         if (qualityLevel == 1.0F && imbueEnhancement > 1.0D) {
/* 6516 */           qualityLevel = (float)(1.0D + (Server.rand.nextFloat() * 10.0F) * imbueEnhancement);
/*      */         }
/* 6518 */         if (tool.getSpellEffects() != null)
/*      */         {
/* 6520 */           qualityLevel *= tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */         }
/*      */         
/* 6523 */         qualityLevel += tool.getRarity();
/*      */         
/* 6525 */         Skill strength = performer.getSkills().getSkillOrLearn(102);
/* 6526 */         double damage = Weapon.getModifiedDamageForWeapon(tool, strength) * 2.0D;
/* 6527 */         if (tool.getTemplateId() == 7) {
/*      */           
/* 6529 */           damage = tool.getCurrentQualityLevel();
/* 6530 */           damage *= 1.0D + strength.getKnowledge(0.0D) / 100.0D;
/* 6531 */           damage *= ((50.0F + Server.rand.nextFloat() * 50.0F) / 100.0F);
/*      */         } 
/*      */         
/* 6534 */         if (insta) {
/* 6535 */           damage = 100.0D;
/*      */         } else {
/* 6537 */           damage += ((15 - hedgeAge) / 15.0F * 100.0F);
/* 6538 */         }  boolean destroyed = hedge.setDamage(hedge.getDamage() + (float)damage);
/* 6539 */         if (destroyed) {
/*      */           
/* 6541 */           performer.getCommunicator().sendNormalServerMessage("You cut down the " + hedge.getName() + ".");
/* 6542 */           if (!insta) {
/* 6543 */             Server.getInstance().broadCastAction(performer.getName() + " cuts down the " + hedge.getName() + ".", performer, 5);
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 6548 */           performer.getCommunicator().sendNormalServerMessage("You chip away some wood from the " + hedge
/* 6549 */               .getName() + ".");
/* 6550 */           if (!insta) {
/* 6551 */             Server.getInstance().broadCastAction(performer
/* 6552 */                 .getName() + " chips away some wood from the " + hedge.getName() + ".", performer, 5);
/*      */           }
/*      */         } 
/*      */       } 
/* 6556 */     } catch (NoSuchSkillException ex) {
/*      */       
/* 6558 */       logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */       
/* 6560 */       toReturn = true;
/*      */     } 
/* 6562 */     return toReturn;
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
/*      */   static void rampantGrowth(Creature performer, int tilex, int tiley) {
/* 6649 */     logger.log(Level.INFO, performer.getName() + " creates trees and bushes at " + tilex + ", " + tiley);
/* 6650 */     if (performer.getLogger() != null)
/* 6651 */       performer.getLogger().log(Level.INFO, "Creates trees and bushes at " + tilex + ", " + tiley); 
/* 6652 */     for (int x = tilex - 5; x < tilex + 5; x++) {
/*      */       
/* 6654 */       for (int y = tiley - 5; y < tiley + 5; y++) {
/*      */         
/* 6656 */         int t = Server.surfaceMesh.getTile(x, y);
/* 6657 */         if (Tiles.decodeHeight(t) > 0 && (
/* 6658 */           Tiles.decodeType(t) == Tiles.Tile.TILE_DIRT.id || Tiles.decodeType(t) == Tiles.Tile.TILE_GRASS.id || 
/* 6659 */           Tiles.decodeType(t) == Tiles.Tile.TILE_SAND.id))
/*      */         {
/* 6661 */           if (Server.rand.nextInt(3) == 0) {
/*      */             
/* 6663 */             int type = 0;
/* 6664 */             if (Server.rand.nextBoolean()) {
/*      */ 
/*      */               
/* 6667 */               type = Server.rand.nextInt(BushData.BushType.getLength());
/* 6668 */               newType = BushData.BushType.fromInt(type).asNormalBush();
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 6673 */               type = Server.rand.nextInt(TreeData.TreeType.getLength());
/* 6674 */               newType = TreeData.TreeType.fromInt(type).asNormalTree();
/*      */             } 
/* 6676 */             byte tage = (byte)Server.rand.nextInt(FoliageAge.OVERAGED.getAgeId());
/* 6677 */             byte grasslen = (byte)(Server.rand.nextInt(3) + 1);
/*      */             
/* 6679 */             Server.setSurfaceTile(x, y, 
/* 6680 */                 Tiles.decodeHeight(t), newType, 
/* 6681 */                 Tiles.encodeTreeData(tage, false, false, grasslen));
/* 6682 */             Players.getInstance().sendChangedTile(x, y, true, false);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] getCaveOpeningCoords(int tilex, int tiley) {
/* 6691 */     if (tilex > 0 && tilex < Zones.worldTileSizeX - 1 && tiley > 0 && tiley < Zones.worldTileSizeY - 1) {
/*      */       
/* 6693 */       if (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)) == Tiles.Tile.TILE_HOLE.id) {
/* 6694 */         return new int[] { tilex, tiley };
/*      */       }
/* 6696 */       if (Tiles.decodeType(Server.surfaceMesh.getTile(tilex - 1, tiley)) == Tiles.Tile.TILE_HOLE.id) {
/* 6697 */         return new int[] { tilex - 1, tiley };
/*      */       }
/* 6699 */       if (Tiles.decodeType(Server.surfaceMesh.getTile(tilex + 1, tiley)) == Tiles.Tile.TILE_HOLE.id) {
/* 6700 */         return new int[] { tilex + 1, tiley };
/*      */       }
/* 6702 */       if (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley - 1)) == Tiles.Tile.TILE_HOLE.id) {
/* 6703 */         return new int[] { tilex, tiley - 1 };
/*      */       }
/* 6705 */       if (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley + 1)) == Tiles.Tile.TILE_HOLE.id) {
/* 6706 */         return new int[] { tilex, tiley + 1 };
/*      */       }
/*      */     } 
/* 6709 */     return noCaveDoor;
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
/*      */   public static Set<int[]> getAllMineDoors(int tilex, int tiley) {
/* 6722 */     if (tilex > 0 && tilex < Zones.worldTileSizeX - 1 && tiley > 0 && tiley < Zones.worldTileSizeY - 1)
/*      */     {
/* 6724 */       if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tilex - 1, tiley)))) {
/*      */         
/* 6726 */         Set<int[]> toReturn = (Set)new HashSet<>();
/* 6727 */         toReturn.add(new int[] { tilex - 1, tiley });
/*      */         
/* 6729 */         return getEastMineDoor(tilex, tiley, toReturn);
/*      */       } 
/*      */     }
/* 6732 */     return getEastMineDoor(tilex, tiley, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<int[]> getEastMineDoor(int tilex, int tiley, @Nullable Set<int[]> toReturn) {
/* 6737 */     if (tilex > 0 && tilex < Zones.worldTileSizeX - 1 && tiley > 0 && tiley < Zones.worldTileSizeY - 1)
/*      */     {
/* 6739 */       if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tilex + 1, tiley)))) {
/*      */         
/* 6741 */         if (toReturn == null)
/* 6742 */           toReturn = (Set)new HashSet<>(); 
/* 6743 */         toReturn.add(new int[] { tilex + 1, tiley });
/*      */       } 
/*      */     }
/*      */     
/* 6747 */     return getNorthMineDoor(tilex, tiley, toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<int[]> getNorthMineDoor(int tilex, int tiley, @Nullable Set<int[]> toReturn) {
/* 6752 */     if (tilex > 0 && tilex < Zones.worldTileSizeX - 1 && tiley > 0 && tiley < Zones.worldTileSizeY - 1)
/*      */     {
/* 6754 */       if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley - 1)))) {
/*      */         
/* 6756 */         if (toReturn == null)
/* 6757 */           toReturn = (Set)new HashSet<>(); 
/* 6758 */         toReturn.add(new int[] { tilex, tiley - 1 });
/*      */       } 
/*      */     }
/*      */     
/* 6762 */     return getSouthMineDoor(tilex, tiley, toReturn);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<int[]> getSouthMineDoor(int tilex, int tiley, @Nullable Set<int[]> toReturn) {
/* 6767 */     if (tilex > 0 && tilex < Zones.worldTileSizeX - 1 && tiley > 0 && tiley < Zones.worldTileSizeY - 1)
/*      */     {
/* 6769 */       if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley + 1)))) {
/*      */         
/* 6771 */         if (toReturn == null)
/* 6772 */           toReturn = (Set)new HashSet<>(); 
/* 6773 */         toReturn.add(new int[] { tilex, tiley + 1 });
/*      */       } 
/*      */     }
/*      */     
/* 6777 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float getDamageModifierForItem(Item item, byte tileid) {
/* 6783 */     float mod = 0.0F;
/* 6784 */     if (tileid == Tiles.Tile.TILE_MINE_DOOR_WOOD.id) {
/*      */       
/* 6786 */       if (item.isWeaponAxe()) {
/* 6787 */         mod = 0.03F;
/* 6788 */       } else if (item.isWeaponCrush()) {
/* 6789 */         mod = 0.02F;
/* 6790 */       } else if (item.isWeaponSlash()) {
/* 6791 */         mod = 0.015F;
/* 6792 */       } else if (item.isWeaponPierce()) {
/* 6793 */         mod = 0.01F;
/* 6794 */       } else if (item.isWeaponMisc()) {
/* 6795 */         mod = 0.007F;
/*      */       } 
/* 6797 */     } else if (tileid == Tiles.Tile.TILE_MINE_DOOR_STONE.id) {
/*      */       
/* 6799 */       if (item.getTemplateId() == 20) {
/* 6800 */         mod = 0.015F;
/* 6801 */       } else if (item.isWeaponCrush()) {
/* 6802 */         mod = 0.01F;
/* 6803 */       } else if (item.isWeaponAxe()) {
/* 6804 */         mod = 0.005F;
/* 6805 */       } else if (item.isWeaponSlash()) {
/* 6806 */         mod = 0.001F;
/* 6807 */       } else if (item.isWeaponPierce()) {
/* 6808 */         mod = 0.001F;
/* 6809 */       } else if (item.isWeaponMisc()) {
/* 6810 */         mod = 0.001F;
/*      */       } 
/* 6812 */     } else if (tileid == Tiles.Tile.TILE_MINE_DOOR_GOLD.id) {
/*      */       
/* 6814 */       if (item.getTemplateId() == 20) {
/* 6815 */         mod = 0.012F;
/* 6816 */       } else if (item.isWeaponCrush()) {
/* 6817 */         mod = 0.007F;
/* 6818 */       } else if (item.isWeaponAxe()) {
/* 6819 */         mod = 0.002F;
/* 6820 */       } else if (item.isWeaponSlash()) {
/* 6821 */         mod = 8.0E-4F;
/* 6822 */       } else if (item.isWeaponPierce()) {
/* 6823 */         mod = 8.0E-4F;
/* 6824 */       } else if (item.isWeaponMisc()) {
/* 6825 */         mod = 8.0E-4F;
/*      */       } 
/* 6827 */     } else if (tileid == Tiles.Tile.TILE_MINE_DOOR_SILVER.id) {
/*      */       
/* 6829 */       if (item.getTemplateId() == 20) {
/* 6830 */         mod = 0.012F;
/* 6831 */       } else if (item.isWeaponCrush()) {
/* 6832 */         mod = 0.007F;
/* 6833 */       } else if (item.isWeaponAxe()) {
/* 6834 */         mod = 0.002F;
/* 6835 */       } else if (item.isWeaponSlash()) {
/* 6836 */         mod = 8.0E-4F;
/* 6837 */       } else if (item.isWeaponPierce()) {
/* 6838 */         mod = 8.0E-4F;
/* 6839 */       } else if (item.isWeaponMisc()) {
/* 6840 */         mod = 8.0E-4F;
/*      */       } 
/* 6842 */     } else if (tileid == Tiles.Tile.TILE_MINE_DOOR_STEEL.id) {
/*      */       
/* 6844 */       if (item.getTemplateId() == 20) {
/* 6845 */         mod = 0.01F;
/* 6846 */       } else if (item.isWeaponCrush()) {
/* 6847 */         mod = 0.005F;
/* 6848 */       } else if (item.isWeaponAxe()) {
/* 6849 */         mod = 0.001F;
/* 6850 */       } else if (item.isWeaponSlash()) {
/* 6851 */         mod = 6.0E-4F;
/* 6852 */       } else if (item.isWeaponPierce()) {
/* 6853 */         mod = 6.0E-4F;
/* 6854 */       } else if (item.isWeaponMisc()) {
/* 6855 */         mod = 1.0E-4F;
/*      */       } 
/* 6857 */     }  return mod;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean removeMineDoor(Creature performer, Action act, Item destroyItem, int tilex, int tiley, boolean onSurface, float counter) {
/* 6863 */     if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)))) {
/*      */       
/* 6865 */       int time = 600;
/* 6866 */       boolean toReturn = false;
/* 6867 */       boolean insta = destroyItem.isWand();
/* 6868 */       if (!onSurface) {
/*      */         
/* 6870 */         performer.getCommunicator().sendNormalServerMessage("You need to do this from the outside.");
/* 6871 */         return true;
/*      */       } 
/*      */       
/* 6874 */       if (counter == 1.0F) {
/*      */         
/* 6876 */         if (!insta) {
/*      */           
/* 6878 */           Skills skills = performer.getSkills();
/*      */           
/*      */           try {
/* 6881 */             Skill str = skills.getSkill(102);
/* 6882 */             if (!((str.getRealKnowledge() > 21.0D) ? 1 : 0))
/*      */             {
/* 6884 */               performer.getCommunicator().sendNormalServerMessage("You are too weak to do that.");
/* 6885 */               return true;
/*      */             }
/*      */           
/* 6888 */           } catch (NoSuchSkillException nss) {
/*      */             
/* 6890 */             logger.log(Level.WARNING, "Weird, " + performer.getName() + " has no strength!");
/* 6891 */             performer.getCommunicator().sendNormalServerMessage("You are too weak to do that.");
/* 6892 */             return true;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 6897 */         performer.getCommunicator().sendNormalServerMessage("You start to remove the door.");
/* 6898 */         Server.getInstance().broadCastAction(performer.getName() + " starts to remove a door.", performer, 5);
/*      */         
/* 6900 */         performer.sendActionControl(Actions.actionEntrys[906]
/* 6901 */             .getVerbString(), true, time);
/* 6902 */         act.setTimeLeft(time);
/* 6903 */         performer.getStatus().modifyStamina(-1000.0F);
/*      */       }
/*      */       else {
/*      */         
/* 6907 */         time = act.getTimeLeft();
/*      */       } 
/*      */       
/* 6910 */       if (act.currentSecond() % 5 == 0) {
/*      */         
/* 6912 */         MethodsStructure.sendDestroySound(performer, destroyItem, 
/* 6913 */             (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)) == 25));
/* 6914 */         performer.getStatus().modifyStamina(-5000.0F);
/*      */       } 
/*      */       
/* 6917 */       if (counter * 10.0F > time || insta) {
/*      */ 
/*      */         
/* 6920 */         int currQl = Server.getWorldResource(tilex, tiley) / 100;
/*      */         
/*      */         try {
/* 6923 */           byte tile = Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley));
/* 6924 */           int doorType = getMineDoorTemplateForTile(tile);
/* 6925 */           Item removed = ItemFactory.createItem(doorType, Math.min(100.0F, Math.max(1.0F, currQl)), (byte)0, null);
/* 6926 */           performer.getInventory().insertItem(removed);
/*      */         }
/* 6928 */         catch (Exception ex) {
/*      */           
/* 6930 */           logger.log(Level.SEVERE, "Factory failed to produce minedoor for " + performer.getName(), ex);
/*      */         } 
/*      */ 
/*      */         
/* 6934 */         TileEvent.log(tilex, tiley, 0, performer.getWurmId(), act.getNumber());
/* 6935 */         TileEvent.log(tilex, tiley, -1, performer.getWurmId(), act.getNumber());
/* 6936 */         if (Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/* 6937 */           Server.setSurfaceTile(tilex, tiley, 
/* 6938 */               Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_HOLE.id, (byte)0);
/*      */         } else {
/* 6940 */           Server.setSurfaceTile(tilex, tiley, 
/* 6941 */               Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_ROCK.id, (byte)0);
/* 6942 */         }  Players.getInstance().sendChangedTile(tilex, tiley, true, true);
/*      */ 
/*      */         
/*      */         try {
/* 6946 */           MineDoorPermission md = MineDoorPermission.getPermission(tilex, tiley);
/* 6947 */           Zones.getZone(tilex, tiley, true).getOrCreateTile(tilex, tiley).removeMineDoor(md);
/*      */         }
/* 6949 */         catch (NoSuchZoneException e) {
/*      */           
/* 6951 */           logger.log(Level.WARNING, "Zone for mine door removal not found");
/*      */         } 
/* 6953 */         MineDoorPermission.deleteMineDoor(tilex, tiley);
/* 6954 */         performer.getCommunicator().sendNormalServerMessage("You remove the mine door from the opening.");
/* 6955 */         return true;
/*      */       } 
/* 6957 */       return false;
/*      */     } 
/* 6959 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean destroyMineDoor(Creature performer, Action act, Item destroyItem, int tilex, int tiley, boolean onSurface, float counter) {
/* 6965 */     if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)))) {
/*      */       
/* 6967 */       boolean toReturn = true;
/* 6968 */       int time = 300;
/* 6969 */       if (Servers.localServer.isChallengeServer())
/* 6970 */         time = 100; 
/* 6971 */       float mod = 1.0F;
/* 6972 */       if (destroyItem == null && !(performer instanceof Player)) {
/* 6973 */         mod = 0.003F;
/* 6974 */       } else if (destroyItem != null) {
/* 6975 */         mod = getDamageModifierForItem(destroyItem, Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)));
/*      */       } else {
/* 6977 */         mod = 0.0F;
/* 6978 */       }  boolean insta = destroyItem.isWand();
/*      */ 
/*      */       
/* 6981 */       if (!performer.isWithinDistanceTo(tilex, tiley, 1)) {
/*      */         
/* 6983 */         performer.getCommunicator().sendNormalServerMessage("You are too far away from the mine door.");
/* 6984 */         return true;
/*      */       } 
/*      */       
/* 6987 */       if (!onSurface) {
/*      */         
/* 6989 */         performer.getCommunicator().sendNormalServerMessage("You need to do this from the outside.");
/* 6990 */         return true;
/*      */       } 
/* 6992 */       if (mod <= 0.0F && !insta) {
/*      */         
/* 6994 */         performer.getCommunicator().sendNormalServerMessage("You will not do any damage to the door with that.");
/* 6995 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 6999 */       toReturn = false;
/* 7000 */       if (counter == 1.0F) {
/*      */         
/* 7002 */         if (!insta) {
/*      */           
/* 7004 */           Skills skills = performer.getSkills();
/*      */           
/*      */           try {
/* 7007 */             Skill str = skills.getSkill(102);
/* 7008 */             if (!((str.getRealKnowledge() > 21.0D) ? 1 : 0))
/*      */             {
/* 7010 */               performer.getCommunicator().sendNormalServerMessage("You are too weak to do that.");
/* 7011 */               return true;
/*      */             }
/*      */           
/* 7014 */           } catch (NoSuchSkillException nss) {
/*      */             
/* 7016 */             logger.log(Level.WARNING, "Weird, " + performer.getName() + " has no strength!");
/* 7017 */             performer.getCommunicator().sendNormalServerMessage("You are too weak to do that.");
/* 7018 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 7022 */         performer.getCommunicator().sendNormalServerMessage("You start to destroy the door.");
/* 7023 */         Server.getInstance().broadCastAction(performer.getName() + " starts to destroy a door.", performer, 5);
/*      */         
/* 7025 */         performer.sendActionControl(Actions.actionEntrys[82]
/* 7026 */             .getVerbString(), true, time);
/* 7027 */         act.setTimeLeft(time);
/* 7028 */         performer.getStatus().modifyStamina(-1000.0F);
/*      */       }
/*      */       else {
/*      */         
/* 7032 */         time = act.getTimeLeft();
/*      */       } 
/* 7034 */       if (act.currentSecond() % 5 == 0) {
/*      */         
/* 7036 */         MethodsStructure.sendDestroySound(performer, destroyItem, 
/* 7037 */             (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)) == 25));
/* 7038 */         performer.getStatus().modifyStamina(-5000.0F);
/* 7039 */         if (destroyItem != null && 
/* 7040 */           !destroyItem.isBodyPartAttached())
/* 7041 */           destroyItem.setDamage(destroyItem.getDamage() + mod * destroyItem.getDamageModifier()); 
/*      */       } 
/* 7043 */       if (counter * 10.0F > time || insta) {
/*      */         
/* 7045 */         Skills skills = performer.getSkills();
/* 7046 */         Skill destroySkill = null;
/*      */         
/*      */         try {
/* 7049 */           destroySkill = skills.getSkill(102);
/*      */         }
/* 7051 */         catch (NoSuchSkillException nss) {
/*      */           
/* 7053 */           destroySkill = skills.learn(102, 1.0F);
/*      */         } 
/* 7055 */         destroySkill.skillCheck(20.0D, destroyItem, 0.0D, false, counter);
/* 7056 */         toReturn = true;
/* 7057 */         double damage = 1.0D;
/* 7058 */         int currQl = Server.getWorldResource(tilex, tiley);
/* 7059 */         if (insta && mod == 0.0F) {
/* 7060 */           damage = 20.0D;
/* 7061 */         } else if (destroyItem != null) {
/*      */           
/* 7063 */           boolean iswood = (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)) == Tiles.Tile.TILE_MINE_DOOR_WOOD.id);
/* 7064 */           if (iswood && destroyItem.isCarpentryTool()) {
/* 7065 */             damage = 100.0D * (1.0D + destroySkill.getKnowledge(0.0D) / 100.0D);
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 7071 */             damage = Weapon.getModifiedDamageForWeapon(destroyItem, destroySkill) * 2.0D;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 7076 */           damage /= (currQl / 20.0F);
/* 7077 */           VolaTile t = Zones.getOrCreateTile(tilex, tiley, true);
/*      */           
/* 7079 */           Village vill = t.getVillage();
/* 7080 */           if (vill != null) {
/*      */             
/* 7082 */             if (MethodsStructure.isCitizenAndMayPerformAction((short)82, performer, vill)) {
/*      */               
/* 7084 */               damage *= 50.0D;
/*      */             }
/* 7086 */             else if (MethodsStructure.isAllyAndMayPerformAction((short)82, performer, vill)) {
/*      */               
/* 7088 */               damage *= 25.0D;
/*      */             }
/* 7090 */             else if (!vill.isChained()) {
/*      */               
/* 7092 */               damage *= 3.0D;
/*      */             } 
/*      */           } else {
/*      */             
/* 7096 */             damage *= 5.0D;
/* 7097 */           }  damage *= Weapon.getMaterialBashModifier(destroyItem.getMaterial());
/* 7098 */           if (performer.getCultist() != null && performer.getCultist().doubleStructDamage())
/* 7099 */             damage *= 2.0D; 
/* 7100 */           damage = (float)(damage * mod * 100.0D);
/*      */         } 
/* 7102 */         damage *= 100.0D;
/* 7103 */         if (Servers.localServer.isChallengeServer()) {
/* 7104 */           damage *= 2.5D;
/*      */         }
/*      */         
/* 7107 */         currQl = (int)Math.max(0.0D, currQl - damage);
/* 7108 */         Server.setWorldResource(tilex, tiley, currQl);
/*      */         
/* 7110 */         if (currQl == 0) {
/*      */           
/* 7112 */           TileEvent.log(tilex, tiley, 0, performer.getWurmId(), act.getNumber());
/* 7113 */           TileEvent.log(tilex, tiley, -1, performer.getWurmId(), act.getNumber());
/* 7114 */           if (Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/* 7115 */             Server.setSurfaceTile(tilex, tiley, 
/* 7116 */                 Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_HOLE.id, (byte)0);
/*      */           } else {
/* 7118 */             Server.setSurfaceTile(tilex, tiley, 
/* 7119 */                 Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), Tiles.Tile.TILE_ROCK.id, (byte)0);
/* 7120 */           }  Players.getInstance().sendChangedTile(tilex, tiley, true, true);
/*      */ 
/*      */           
/*      */           try {
/* 7124 */             MineDoorPermission md = MineDoorPermission.getPermission(tilex, tiley);
/* 7125 */             Zones.getZone(tilex, tiley, true).getOrCreateTile(tilex, tiley).removeMineDoor(md);
/*      */           }
/* 7127 */           catch (NoSuchZoneException e) {
/*      */             
/* 7129 */             logger.log(Level.WARNING, "Zone for mine door removal not found");
/*      */           } 
/* 7131 */           MineDoorPermission.deleteMineDoor(tilex, tiley);
/* 7132 */           performer.getCommunicator().sendNormalServerMessage("The last parts of the door fall down with a crash.");
/*      */           
/* 7134 */           Server.getInstance().broadCastAction(performer
/* 7135 */               .getName() + " damages a door and the last parts fall down with a crash.", performer, 5);
/*      */           
/* 7137 */           if (performer.getDeity() != null)
/*      */           {
/* 7139 */             performer.performActionOkey(act);
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 7144 */           performer.getCommunicator().sendNormalServerMessage("You damage the door.");
/* 7145 */           Server.getInstance().broadCastAction(performer.getName() + " damages the door.", performer, 5);
/*      */         } 
/*      */       } 
/*      */       
/* 7149 */       return toReturn;
/*      */     } 
/* 7151 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isAltarBlocking(Creature performer, int tilex, int tiley) {
/* 7156 */     EndGameItem alt = EndGameItems.getEvilAltar();
/* 7157 */     if (alt != null) {
/*      */       
/* 7159 */       int maxnorth = Zones.safeTileY(tiley - 20);
/* 7160 */       int maxsouth = Zones.safeTileY(tiley + 20);
/* 7161 */       int maxeast = Zones.safeTileX(tilex - 20);
/* 7162 */       int maxwest = Zones.safeTileX(tilex + 20);
/* 7163 */       if (alt.getItem() != null)
/*      */       {
/* 7165 */         if ((int)alt.getItem().getPosX() >> 2 < maxwest && (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 7166 */           (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */         {
/* 7168 */           return true;
/*      */         }
/*      */       }
/*      */     } 
/* 7172 */     alt = EndGameItems.getGoodAltar();
/* 7173 */     if (alt != null) {
/*      */       
/* 7175 */       int maxnorth = Zones.safeTileY(tiley - 20);
/* 7176 */       int maxsouth = Zones.safeTileY(tiley + 20);
/* 7177 */       int maxeast = Zones.safeTileX(tilex - 20);
/* 7178 */       int maxwest = Zones.safeTileX(tilex + 20);
/* 7179 */       if (alt.getItem() != null)
/*      */       {
/* 7181 */         if ((int)alt.getItem().getPosX() >> 2 < maxwest && (int)alt.getItem().getPosX() >> 2 > maxeast && 
/* 7182 */           (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth)
/*      */         {
/* 7184 */           return true;
/*      */         }
/*      */       }
/*      */     } 
/* 7188 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean buildMineDoor(Creature performer, Item source, Action act, int tilex, int tiley, boolean onSurface, float counter) {
/* 7194 */     boolean done = true;
/* 7195 */     if (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)) == Tiles.Tile.TILE_HOLE.id) {
/*      */ 
/*      */       
/* 7198 */       if (!performer.isOnSurface()) {
/*      */         
/* 7200 */         performer.getCommunicator().sendNormalServerMessage("You need to do this from the outside.");
/* 7201 */         return true;
/*      */       } 
/* 7203 */       if (performer.getPower() < 5) {
/*      */ 
/*      */         
/* 7206 */         if (Zones.isInPvPZone(tilex, tiley)) {
/*      */           
/* 7208 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to build this in the PvP zone.");
/* 7209 */           return true;
/*      */         } 
/* 7211 */         if (getCaveDoorDifference(Server.surfaceMesh.getTile(tilex, tiley), tilex, tiley) > 90) {
/*      */           
/* 7213 */           performer.getCommunicator().sendNormalServerMessage("That hole is too big to be covered.");
/* 7214 */           return true;
/*      */         } 
/* 7216 */         if (isTileModBlocked(performer, tilex, tiley, true))
/* 7217 */           return true; 
/* 7218 */         if (isAltarBlocking(performer, tilex, tiley)) {
/*      */           
/* 7220 */           performer.getCommunicator().sendSafeServerMessage("You cannot build here, since this is holy ground.");
/* 7221 */           return true;
/*      */         } 
/* 7223 */         if (!Methods.isActionAllowed(performer, (short)363))
/* 7224 */           return true; 
/*      */       } 
/* 7226 */       if (source.isMineDoor() || source.isWand()) {
/*      */         
/* 7228 */         done = false;
/* 7229 */         boolean insta = (performer.getPower() > 0);
/* 7230 */         if (counter == 1.0F && !insta) {
/*      */           
/* 7232 */           Skills skills = performer.getSkills();
/*      */           
/*      */           try {
/* 7235 */             Skill str = skills.getSkill(1008);
/* 7236 */             if (source.getTemplateId() != 592 && !((str.getRealKnowledge() > 21.0D) ? 1 : 0))
/*      */             {
/* 7238 */               performer.getCommunicator().sendNormalServerMessage("You do not know how to do that effectively.");
/* 7239 */               return true;
/*      */             }
/*      */           
/* 7242 */           } catch (NoSuchSkillException nss) {
/*      */             
/* 7244 */             performer.getCommunicator().sendNormalServerMessage("You do not know how to do that effectively.");
/* 7245 */             return true;
/*      */           } 
/* 7247 */           performer.getCommunicator().sendNormalServerMessage("You start to fit the door in the entrance.");
/* 7248 */           Server.getInstance().broadCastAction(performer.getName() + " starts to fit a door in the entrance.", performer, 5);
/*      */ 
/*      */           
/* 7251 */           performer.sendActionControl(Actions.actionEntrys[363].getVerbString(), true, 150);
/*      */ 
/*      */           
/* 7254 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         } 
/* 7256 */         if (act.currentSecond() % 5 == 0)
/*      */         {
/* 7258 */           performer.getStatus().modifyStamina(-5000.0F);
/*      */         }
/* 7260 */         if (act.mayPlaySound()) {
/*      */           
/* 7262 */           String s = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/* 7263 */           if (source.isStone())
/* 7264 */             s = "sound.work.masonry"; 
/* 7265 */           if (source.isMetal())
/* 7266 */             s = "sound.work.smithing.metal"; 
/* 7267 */           SoundPlayer.playSound(s, performer, 1.0F);
/*      */         } 
/* 7269 */         if (counter > 15.0F || insta) {
/*      */           
/* 7271 */           Skills skills = performer.getSkills();
/* 7272 */           Skill mining = null;
/*      */           
/*      */           try {
/* 7275 */             mining = skills.getSkill(1008);
/* 7276 */             mining.skillCheck(20.0D, source.getQualityLevel(), false, 15.0F);
/*      */           }
/* 7278 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */           
/* 7282 */           if (!flattenTopMineBorder(performer, tilex, tiley)) {
/*      */             
/* 7284 */             performer.getCommunicator().sendNormalServerMessage("Mine door cannot be placed as the upper part of the entrance is not flat.");
/* 7285 */             return true;
/*      */           } 
/*      */           
/* 7288 */           if (MineDoorPermission.getPermission(tilex, tiley) != null)
/* 7289 */             MineDoorPermission.deleteMineDoor(tilex, tiley); 
/* 7290 */           Village vill = Villages.getVillage(tilex, tiley, onSurface);
/* 7291 */           Server.setWorldResource(tilex, tiley, Math.max(1, (int)source.getCurrentQualityLevel() * 100));
/* 7292 */           Server.setSurfaceTile(tilex, tiley, 
/* 7293 */               Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley)), 
/* 7294 */               getNewTileTypeForMineDoor(source.getTemplateId()), (byte)0);
/* 7295 */           TileEvent.log(tilex, tiley, 0, performer.getWurmId(), act.getNumber());
/* 7296 */           TileEvent.log(tilex, tiley, -1, performer.getWurmId(), act.getNumber());
/* 7297 */           Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 7298 */           new MineDoorPermission(tilex, tiley, performer.getWurmId(), vill, false, false, "", 0);
/* 7299 */           if (source.isMineDoor())
/* 7300 */             Items.destroyItem(source.getWurmId()); 
/* 7301 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/* 7305 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean flattenTopMineBorder(Creature performer, int tilex, int tiley) {
/* 7310 */     Point highestCorner = TileRockBehaviour.findHighestCorner(tilex, tiley);
/* 7311 */     if (highestCorner == null) {
/* 7312 */       return false;
/*      */     }
/* 7314 */     Point nextHighestCorner = TileRockBehaviour.findNextHighestCorner(tilex, tiley, highestCorner);
/* 7315 */     if (nextHighestCorner == null) {
/* 7316 */       return false;
/*      */     }
/*      */     
/* 7319 */     if (nextHighestCorner.getH() != highestCorner.getH() && 
/* 7320 */       TileRockBehaviour.isStructureNear(highestCorner.getX(), highestCorner.getY()))
/*      */     {
/* 7322 */       return false;
/*      */     }
/*      */     
/* 7325 */     short targetUpperHeight = (short)nextHighestCorner.getH();
/*      */     
/* 7327 */     short tileData = Tiles.decodeTileData(Server.surfaceMesh.getTile(highestCorner.getX(), highestCorner.getY()));
/* 7328 */     Server.surfaceMesh.setTile(highestCorner.getX(), highestCorner.getY(), 
/* 7329 */         Tiles.encode(targetUpperHeight, tileData));
/* 7330 */     tileData = Tiles.decodeTileData(Server.rockMesh.getTile(highestCorner.getX(), highestCorner.getY()));
/* 7331 */     Server.rockMesh.setTile(highestCorner.getX(), highestCorner.getY(), 
/* 7332 */         Tiles.encode(targetUpperHeight, tileData));
/* 7333 */     tileData = Tiles.decodeTileData(Server.caveMesh.getTile(highestCorner.getX(), highestCorner.getY()));
/*      */     
/* 7335 */     Players.getInstance().sendChangedTile(highestCorner.getX(), highestCorner.getY(), true, true);
/* 7336 */     Players.getInstance().sendChangedTile(highestCorner.getX(), highestCorner.getY(), false, true);
/* 7337 */     Players.getInstance().sendChangedTile(tilex, tiley, true, true);
/*      */     
/* 7339 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getMineDoorTemplateForTile(byte tile) {
/* 7344 */     if (tile == 27)
/* 7345 */       return 594; 
/* 7346 */     if (tile == 25)
/* 7347 */       return 592; 
/* 7348 */     if (tile == 26)
/* 7349 */       return 593; 
/* 7350 */     if (tile == 28)
/* 7351 */       return 595; 
/* 7352 */     if (tile == 29) {
/* 7353 */       return 596;
/*      */     }
/* 7355 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final byte getNewTileTypeForMineDoor(int templateId) {
/* 7360 */     if (templateId == 594)
/* 7361 */       return 27; 
/* 7362 */     if (templateId == 592)
/* 7363 */       return 25; 
/* 7364 */     if (templateId == 593)
/* 7365 */       return 26; 
/* 7366 */     if (templateId == 595)
/* 7367 */       return 28; 
/* 7368 */     if (templateId == 596)
/* 7369 */       return 29; 
/* 7370 */     if (templateId == 315)
/* 7371 */       return 25; 
/* 7372 */     if (templateId == 176) {
/* 7373 */       return 25;
/*      */     }
/* 7375 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean enchantNature(Creature performer, int tilex, int tiley, boolean onSurface, int tile, float counter, Action act) {
/* 7381 */     boolean done = true;
/* 7382 */     if (Methods.isActionAllowed(performer, (short)384) && performer
/* 7383 */       .getCultist() != null && performer.getCultist().mayEnchantNature()) {
/*      */       
/* 7385 */       oldType = Tiles.decodeType(tile);
/* 7386 */       Tiles.Tile oldTile = Tiles.getTile(oldType);
/* 7387 */       newType = Tiles.Tile.TILE_ENCHANTED_GRASS.id;
/* 7388 */       byte oldData = Tiles.decodeData(tile);
/* 7389 */       if (oldType == Tiles.Tile.TILE_KELP.id || oldType == Tiles.Tile.TILE_REED.id || oldType == Tiles.Tile.TILE_LAWN.id || oldType == Tiles.Tile.TILE_BUSH_LINGONBERRY.id) {
/*      */         
/* 7391 */         performer.getCommunicator().sendNormalServerMessage("The area refuses to accept your love.");
/* 7392 */         return true;
/*      */       } 
/* 7394 */       if (oldType == Tiles.Tile.TILE_GRASS.id || oldTile.isNormalTree() || oldTile.isNormalBush()) {
/*      */         
/* 7396 */         done = false;
/* 7397 */         BlockingResult result = Blocking.getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), ((tilex << 2) + 2), ((tiley << 2) + 2), performer
/* 7398 */             .getPositionZ(), Zones.getHeightForNode(tilex, tiley, 0), true, true, false, 5, -1L, performer
/* 7399 */             .getBridgeId(), -10L, false);
/*      */         
/* 7401 */         if (result != null) {
/*      */           
/* 7403 */           performer.getCommunicator().sendCombatNormalMessage("The " + result
/* 7404 */               .getFirstBlocker().getName() + " is in the way. You fail to focus.");
/* 7405 */           return true;
/*      */         } 
/* 7407 */         if (counter == 1.0F) {
/*      */           
/* 7409 */           performer.getCommunicator().sendNormalServerMessage("You start to focus your love on the surroundings.");
/* 7410 */           Server.getInstance().broadCastAction(performer
/* 7411 */               .getName() + " smiles and closes " + performer.getHisHerItsString() + " eyes.", performer, 5);
/*      */ 
/*      */           
/* 7414 */           performer.sendActionControl(Actions.actionEntrys[388].getVerbString(), true, 50);
/*      */ 
/*      */           
/* 7417 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         }
/* 7419 */         else if (act.currentSecond() >= 5) {
/*      */           
/* 7421 */           TileEvent.log(tilex, tilex, 0, performer.getWurmId(), act.getNumber());
/* 7422 */           if (performer.getCultist() != null) {
/* 7423 */             performer.getCultist().touchCooldown2();
/*      */           }
/* 7425 */           if (oldTile.isNormalTree()) {
/* 7426 */             newType = oldTile.getTreeType(oldData).asEnchantedTree();
/* 7427 */           } else if (oldTile.isNormalBush()) {
/* 7428 */             newType = oldTile.getBushType(oldData).asEnchantedBush();
/*      */           } 
/* 7430 */           byte newData = oldData;
/*      */           
/* 7432 */           Server.setSurfaceTile(tilex, tiley, 
/* 7433 */               Tiles.decodeHeight(tile), newType, newData);
/* 7434 */           performer.getCommunicator().sendNormalServerMessage("You let your love change the area.");
/* 7435 */           Server.getInstance().broadCastAction(performer
/* 7436 */               .getName() + " changes the area with " + performer.getHisHerItsString() + " love.", performer, 5);
/*      */ 
/*      */           
/* 7439 */           done = true;
/* 7440 */           performer.getMovementScheme().touchFreeMoveCounter();
/* 7441 */           Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/* 7442 */           return done;
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 7447 */       performer.getCommunicator().sendNormalServerMessage("You fail to enchant the spot.");
/*      */     } 
/* 7449 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean freezeLava(Creature performer, int tilex, int tiley, boolean onSurface, int tile, float counter, boolean cultistSpawn) {
/* 7455 */     VolaTile vt = Zones.getOrCreateTile(tilex, tiley, onSurface);
/* 7456 */     byte type = Tiles.decodeType(tile);
/* 7457 */     if (type != Tiles.Tile.TILE_LAVA.id && type != Tiles.Tile.TILE_CAVE_WALL_LAVA.id) {
/*      */       
/* 7459 */       performer.getCommunicator().sendNormalServerMessage("The tile is not lava any longer.");
/* 7460 */       return true;
/*      */     } 
/* 7462 */     if (!Methods.isActionAllowed(performer, (short)384))
/* 7463 */       return true; 
/* 7464 */     if (vt.getVillage() != null)
/*      */     {
/* 7466 */       if (!vt.getVillage().isCitizen(performer))
/*      */       {
/* 7468 */         if (vt.getKingdom() == performer.getKingdomId())
/*      */         {
/* 7470 */           if (!vt.getVillage().isAlly(performer)) {
/*      */             
/* 7472 */             performer
/* 7473 */               .getCommunicator()
/* 7474 */               .sendNormalServerMessage("Some psychological issue stops you from freezing the lava here. If you were an ally of the village maybe you would feel more comfortable.");
/*      */             
/* 7476 */             return true;
/*      */           } 
/*      */         }
/*      */       }
/*      */     }
/* 7481 */     boolean done = false;
/* 7482 */     if (counter == 1.0F) {
/*      */       
/* 7484 */       performer.getCommunicator().sendNormalServerMessage("You start concentrating on the lava.");
/* 7485 */       Server.getInstance().broadCastAction(performer.getName() + " starts to stare intensely at the lava.", performer, 5);
/* 7486 */       if (cultistSpawn) {
/* 7487 */         performer.sendActionControl("Freezing", true, 1000);
/*      */       }
/*      */     } 
/* 7490 */     if (!cultistSpawn || counter > 100.0F) {
/*      */       
/* 7492 */       done = true;
/* 7493 */       if ((Tiles.decodeData(tile) & 0xFF) == 255) {
/*      */         
/* 7495 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens with the lava.. the permanent flow from beneath is too powerful.");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 7500 */         performer.getCommunicator().sendNormalServerMessage("The lava cools down and turns grey.");
/* 7501 */         Server.getInstance().broadCastAction("The previously hot lava is now still and grey rock instead.", performer, 5);
/*      */         
/* 7503 */         TileEvent.log(tilex, tiley, 0, performer.getWurmId(), 327);
/*      */         
/* 7505 */         if (cultistSpawn)
/* 7506 */           performer.getCultist().touchCooldown2(); 
/* 7507 */         if (type == Tiles.Tile.TILE_LAVA.id) {
/*      */           
/* 7509 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_ROCK.id, (byte)0);
/* 7510 */           for (int xx = 0; xx <= 1; xx++) {
/*      */             
/* 7512 */             for (int yy = 0; yy <= 1; yy++) {
/*      */ 
/*      */               
/*      */               try {
/* 7516 */                 int tempint3 = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + xx, tiley + yy));
/* 7517 */                 Server.rockMesh.setTile(tilex + xx, tiley + yy, 
/* 7518 */                     Tiles.encode((short)tempint3, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */               }
/* 7520 */               catch (Exception exception) {}
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 7526 */           int caveTile = Server.caveMesh.getTile(tilex, tiley);
/* 7527 */           byte caveType = Tiles.decodeType(caveTile);
/* 7528 */           if (caveType == Tiles.Tile.TILE_CAVE_WALL_LAVA.id)
/* 7529 */             setAsRock(tilex, tiley, false, false); 
/* 7530 */           Players.getInstance().sendChangedTile(tilex, tiley, true, true);
/*      */         }
/*      */         else {
/*      */           
/* 7534 */           setAsRock(tilex, tiley, false, false);
/*      */         } 
/*      */       } 
/*      */     } 
/* 7538 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean handleChopAction(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/* 7545 */     boolean done = false;
/*      */     
/* 7547 */     if (!source.isWeaponSlash() && source.getTemplateId() != 24)
/*      */     {
/*      */       
/* 7550 */       return true;
/*      */     }
/*      */     
/* 7553 */     byte tileType = Tiles.decodeType(tile);
/* 7554 */     Tiles.Tile theTile = Tiles.getTile(tileType);
/* 7555 */     byte tileData = Tiles.decodeData(tile);
/* 7556 */     int treeAge = tileData >> 4 & 0xF;
/*      */     
/* 7558 */     if (!theTile.isBush() && !theTile.isTree()) {
/* 7559 */       return true;
/*      */     }
/* 7561 */     if (Zones.protectedTiles[tilex][tiley]) {
/*      */       
/* 7563 */       performer.getCommunicator().sendNormalServerMessage("Your muscles weaken as you try to cut down the tree. You just can't bring yourself to do it.");
/*      */       
/* 7565 */       return true;
/*      */     } 
/* 7567 */     if (!Methods.isActionAllowed(performer, (short)96, false, tilex, tiley, tile, 0)) {
/* 7568 */       return true;
/*      */     }
/*      */     
/* 7571 */     VolaTile vt = Zones.getTileOrNull(tilex, tiley, onSurface);
/* 7572 */     Item hive = null;
/* 7573 */     if (vt != null) {
/*      */       
/* 7575 */       hive = vt.findHive(1239, true);
/* 7576 */       if (hive != null)
/*      */       {
/* 7578 */         if (performer.getBestBeeSmoker() == null) {
/*      */ 
/*      */ 
/*      */           
/* 7582 */           performer.getCommunicator().sendSafeServerMessage("The bees get angry and defend the wild hive by stinging you.");
/*      */           
/* 7584 */           performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (5000.0F + Server.rand
/* 7585 */               .nextFloat() * 7000.0F), 0.0F, 35.0F, false, false);
/*      */           
/* 7587 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/* 7591 */     if (!onSurface) {
/*      */       
/* 7593 */       performer.getCommunicator().sendNormalServerMessage("You can not reach that.");
/* 7594 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 7599 */       Skill woodcutting = performer.getSkills().getSkillOrLearn(1007);
/* 7600 */       Skill primskill = performer.getSkills().getSkillOrLearn(source.getPrimarySkill());
/*      */       
/* 7602 */       int time = 0;
/* 7603 */       float qualityLevel = 0.0F;
/* 7604 */       if (counter == 1.0F) {
/*      */         
/* 7606 */         time = (int)(calcTime(treeAge, source, primskill, woodcutting) * Actions.getStaminaModiferFor(performer, 20000));
/*      */         
/* 7608 */         time = Math.min(65535, time);
/* 7609 */         act.setTimeLeft(time);
/* 7610 */         String treeString = theTile.getTileName(tileData);
/*      */         
/* 7612 */         performer.getCommunicator().sendNormalServerMessage("You start to cut down the " + treeString + ".");
/* 7613 */         Server.getInstance().broadCastAction(performer
/* 7614 */             .getName() + " starts to cut down the " + treeString + ".", performer, 5);
/* 7615 */         act.setActionString("cutting down " + treeString);
/* 7616 */         performer.sendActionControl("cutting down " + treeString, true, time);
/* 7617 */         performer.getStatus().modifyStamina(-1500.0F);
/* 7618 */         if (source.isWeaponAxe()) {
/* 7619 */           source.setDamage(source.getDamage() + 0.001F * source.getDamageModifier());
/*      */         } else {
/* 7621 */           source.setDamage(source.getDamage() + 0.0025F * source.getDamageModifier());
/*      */         } 
/*      */       } else {
/*      */         
/* 7625 */         time = act.getTimeLeft();
/*      */         
/* 7627 */         if (act.justTickedSecond() && ((time < 50 && act.currentSecond() % 2 == 0) || act.currentSecond() % 5 == 0)) {
/*      */           
/* 7629 */           performer.getStatus().modifyStamina(-6000.0F);
/* 7630 */           if (source.isWeaponAxe()) {
/* 7631 */             source.setDamage(source.getDamage() + 0.001F * source.getDamageModifier());
/*      */           } else {
/* 7633 */             source.setDamage(source.getDamage() + 0.0025F * source.getDamageModifier());
/*      */           } 
/* 7635 */         }  if (act.justTickedSecond() && counter * 10.0F < (time - 30))
/*      */         {
/* 7637 */           if (source.getTemplateId() != 24) {
/*      */             
/* 7639 */             if ((act.currentSecond() - 2) % 3 == 0)
/*      */             {
/* 7641 */               String sstring = "sound.work.woodcutting1";
/* 7642 */               int x = Server.rand.nextInt(3);
/* 7643 */               if (x == 0) {
/* 7644 */                 sstring = "sound.work.woodcutting2";
/* 7645 */               } else if (x == 1) {
/* 7646 */                 sstring = "sound.work.woodcutting3";
/* 7647 */               }  SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 1.0F);
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/* 7654 */           else if ((act.currentSecond() - 2) % 6 == 0 && counter * 10.0F < (time - 50)) {
/*      */             
/* 7656 */             String sstring = "sound.work.carpentry.saw";
/* 7657 */             SoundPlayer.playSound("sound.work.carpentry.saw", tilex, tiley, performer.isOnSurface(), 1.0F);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 7662 */       if (counter * 10.0F > time)
/*      */       {
/* 7664 */         if (act.getRarity() != 0)
/*      */         {
/* 7666 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 7668 */         done = true;
/* 7669 */         double bonus = 0.0D;
/* 7670 */         int stumpAge = treeAge;
/* 7671 */         if (treeAge == 15) {
/* 7672 */           treeAge = 3;
/*      */         }
/* 7674 */         double treeDifficulty = 2.0D;
/* 7675 */         float skillTick = 0.0F;
/*      */         
/* 7677 */         if (Servers.localServer.challengeServer) {
/*      */           
/* 7679 */           skillTick = Math.min(20.0F, counter);
/* 7680 */           int base = theTile.getWoodDificulity();
/* 7681 */           treeDifficulty = base * (1.0D + treeAge / 14.0D);
/*      */         }
/*      */         else {
/*      */           
/* 7685 */           skillTick = Math.min(20.0F, counter);
/* 7686 */           treeDifficulty = (15 - treeAge);
/*      */         } 
/* 7688 */         if (source.getTemplateId() == 7 || source.getTemplateId() == 24) {
/* 7689 */           bonus = Math.max(0.0D, primskill
/* 7690 */               .skillCheck(treeDifficulty, source, 0.0D, false, skillTick));
/*      */         } else {
/* 7692 */           bonus = Math.max(0.0D, primskill
/*      */               
/* 7694 */               .skillCheck(treeDifficulty, source, 0.0D, (primskill.getKnowledge(0.0D) > 20.0D), skillTick));
/*      */         } 
/* 7696 */         qualityLevel = Math.max(1.0F, 
/* 7697 */             (float)woodcutting.skillCheck(treeDifficulty, source, bonus, false, skillTick));
/* 7698 */         double imbueEnhancement = 1.0D + 0.23047D * source.getSkillSpellImprovement(1007) / 100.0D;
/*      */         
/* 7700 */         double woodc = woodcutting.getKnowledge(0.0D) * imbueEnhancement;
/*      */         
/* 7702 */         if (woodc < qualityLevel) {
/* 7703 */           qualityLevel = (float)woodc;
/*      */         }
/* 7705 */         if (qualityLevel == 1.0F && imbueEnhancement > 1.0D) {
/* 7706 */           qualityLevel = (float)(1.0D + (Server.rand.nextFloat() * 10.0F) * imbueEnhancement);
/*      */         }
/* 7708 */         if (source.getSpellEffects() != null)
/*      */         {
/* 7710 */           qualityLevel *= source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */         }
/*      */         
/* 7713 */         qualityLevel += source.getRarity();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7722 */         Skill strength = performer.getSkills().getSkillOrLearn(102);
/* 7723 */         double damage = Weapon.getModifiedDamageForWeapon(source, strength) * 2.0D;
/* 7724 */         if (source.getTemplateId() == 7) {
/*      */           
/* 7726 */           damage = source.getCurrentQualityLevel();
/* 7727 */           damage *= 1.0D + strength.getKnowledge(0.0D) / 100.0D;
/* 7728 */           damage *= ((50.0F + Server.rand.nextFloat() * 50.0F) / 100.0F);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7736 */         damage += ((15 - treeAge) / 15.0F * 100.0F);
/*      */ 
/*      */         
/* 7739 */         float dam = Server.getWorldResource(tilex, tiley);
/* 7740 */         if (dam == 65535.0F)
/* 7741 */           dam = 0.0F; 
/* 7742 */         if (dam < 100.0F) {
/* 7743 */           dam = (float)(dam + damage);
/*      */         }
/* 7745 */         String treeString = theTile.getTileName(tileData);
/*      */         
/* 7747 */         if (dam >= 100.0F) {
/*      */           
/* 7749 */           Server.setWorldResource(tilex, tiley, 0);
/* 7750 */           TileEvent.log(tilex, tiley, 0, performer.getWurmId(), action);
/* 7751 */           performer.getCommunicator().sendNormalServerMessage("You cut down the " + treeString + ".");
/* 7752 */           Server.getInstance().broadCastAction(performer.getName() + " cuts down the " + treeString + ".", performer, 5);
/*      */ 
/*      */           
/* 7755 */           MissionTriggers.activateTriggers(performer, source, 492, 
/* 7756 */               EpicServerStatus.getTileId(tilex, tiley), 1);
/*      */           
/* 7758 */           if (treeAge > 4) {
/*      */             
/* 7760 */             performer.achievement(129);
/* 7761 */             if (source.getTemplateId() == 25) {
/* 7762 */               performer.achievement(135);
/*      */             }
/*      */           } 
/* 7765 */           GrassData.GrowthTreeStage treeStage = TreeData.getGrassLength(tileData);
/*      */           
/* 7767 */           int newGrassLength = Math.max(0, treeStage.getCode() - 1);
/*      */           
/* 7769 */           GrassData.GrowthStage grassStage = GrassData.GrowthStage.fromInt(newGrassLength);
/*      */           
/* 7771 */           GrassData.FlowerType flowerType = GrassData.FlowerType.NONE;
/*      */           
/* 7773 */           byte newData = GrassData.encodeGrassTileData(grassStage, flowerType);
/* 7774 */           byte newt = Tiles.Tile.TILE_GRASS.id;
/* 7775 */           if (allCornersAtRockLevel(tilex, tiley, Server.surfaceMesh)) {
/*      */             
/* 7777 */             newt = Tiles.Tile.TILE_ROCK.id;
/* 7778 */             newData = 0;
/*      */           }
/* 7780 */           else if (theTile.isMycelium()) {
/* 7781 */             newt = Tiles.Tile.TILE_MYCELIUM.id;
/* 7782 */           } else if (theTile.isEnchanted()) {
/*      */             
/* 7784 */             newt = Tiles.Tile.TILE_ENCHANTED_GRASS.id;
/* 7785 */             newData = 0;
/*      */           }
/* 7787 */           else if (tileType == Tiles.Tile.TILE_BUSH_LINGONBERRY.id) {
/* 7788 */             newt = Tiles.Tile.TILE_TUNDRA.id;
/*      */           } 
/* 7790 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), newt, newData);
/*      */ 
/*      */           
/*      */           try {
/* 7794 */             Zone z = Zones.getZone(tilex, tiley, true);
/* 7795 */             z.changeTile(tilex, tiley);
/*      */           }
/* 7797 */           catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */           
/* 7800 */           Players.getInstance().sendChangedTile(tilex, tiley, onSurface, true);
/*      */           
/* 7802 */           if (theTile.isTree()) {
/*      */             
/* 7804 */             TreeData.TreeType treeType = theTile.getTreeType(tileData);
/* 7805 */             boolean inCenter = TreeData.isCentre(tileData);
/*      */             
/*      */             try {
/* 7808 */               byte material = treeType.getMaterial();
/*      */               
/* 7810 */               int templateId = 9;
/* 7811 */               if (treeAge >= 8 && !treeType.isFruitTree())
/* 7812 */                 templateId = 385; 
/* 7813 */               double sizeMod = treeAge / 15.0D;
/* 7814 */               if (treeType.isFruitTree()) {
/* 7815 */                 sizeMod *= 0.25D;
/*      */               }
/* 7817 */               float dir = Creature.normalizeAngle(TerrainUtilities.getTreeRotation(tilex, tiley));
/*      */ 
/*      */ 
/*      */               
/* 7821 */               float xNew = (tilex << 2) + 2.0F;
/* 7822 */               float yNew = (tiley << 2) + 2.0F;
/* 7823 */               if (!inCenter) {
/*      */ 
/*      */                 
/* 7826 */                 xNew = (tilex << 2) + 4.0F * TerrainUtilities.getTreePosX(tilex, tiley);
/* 7827 */                 yNew = (tiley << 2) + 4.0F * TerrainUtilities.getTreePosY(tilex, tiley);
/*      */               } 
/* 7829 */               ItemTemplate t = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*      */               
/* 7831 */               int weight = (int)Math.max(1000.0D, sizeMod * t.getWeightGrams());
/* 7832 */               if (weight < 1500)
/* 7833 */                 templateId = 169; 
/* 7834 */               if (templateId == 385) {
/*      */                 
/* 7836 */                 SoundPlayer.playSound("sound.tree.falling", tilex, tiley, true, 1.0F);
/*      */                 
/* 7838 */                 Item stump = ItemFactory.createItem(731, Math.min(100.0F, qualityLevel), xNew, yNew, dir, onSurface, material, act
/* 7839 */                     .getRarity(), -10L, null, (byte)stumpAge);
/* 7840 */                 stump.setLastOwnerId(-10L);
/* 7841 */                 stump.setWeight(weight, true);
/*      */               } 
/* 7843 */               int ta = treeAge;
/* 7844 */               Item newItem = ItemFactory.createItem(templateId, Math.min(100.0F, qualityLevel), xNew, yNew, dir, onSurface, material, act
/* 7845 */                   .getRarity(), -10L, null, (byte)ta);
/* 7846 */               if (templateId == 385) {
/* 7847 */                 newItem.setAuxData((byte)treeAge);
/*      */               }
/*      */               
/* 7850 */               if (treeAge >= 5 && performer.getDeity() != null && 
/* 7851 */                 (performer.getDeity()).number == 3)
/*      */               {
/* 7853 */                 performer.maybeModifyAlignment(1.0F);
/*      */               }
/* 7855 */               newItem.setWeight(weight, true);
/* 7856 */               newItem.setLastOwnerId(performer.getWurmId());
/* 7857 */               if (performer.getTutorialLevel() == 3 && !performer.skippedTutorial())
/*      */               {
/* 7859 */                 if (templateId == 9)
/*      */                 {
/* 7861 */                   performer.missionFinished(true, true);
/*      */                 
/*      */                 }
/*      */                 else
/*      */                 {
/* 7866 */                   String text = "You should now chop the tree up. Right-click the " + newItem.getName() + " and chop it up. Then get the log.";
/* 7867 */                   SimplePopup popup = new SimplePopup(performer, "Chop up the tree", text);
/* 7868 */                   popup.sendQuestion();
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */             }
/* 7874 */             catch (Exception ex) {
/*      */               
/* 7876 */               logger.log(Level.WARNING, "Factory failed to produce item log", ex);
/*      */             } 
/*      */           } 
/*      */           
/* 7880 */           if (hive != null) {
/*      */             
/* 7882 */             if (performer.getBestBeeSmoker() == null)
/*      */             {
/*      */               
/* 7885 */               performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (4000.0F + Server.rand
/* 7886 */                   .nextFloat() * 3000.0F), 0.0F, 0.0F, false, false);
/*      */             }
/*      */             
/* 7889 */             for (Item item : hive.getItemsAsArray())
/*      */             {
/* 7891 */               Items.destroyItem(item.getWurmId());
/*      */             }
/* 7893 */             Items.destroyItem(hive.getWurmId());
/*      */           } 
/* 7895 */           PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.FELL_TREE);
/*      */         }
/*      */         else {
/*      */           
/* 7899 */           Server.setWorldResource(tilex, tiley, (short)(int)dam);
/* 7900 */           performer.getCommunicator().sendNormalServerMessage("You chip away some wood from the " + treeString + ".");
/*      */           
/* 7902 */           Server.getInstance().broadCastAction(performer
/* 7903 */               .getName() + " chips away some wood from the " + treeString + ".", performer, 5);
/*      */         } 
/* 7905 */         PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.CUT_TREE);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 7914 */     catch (NoSuchSkillException ex) {
/*      */       
/* 7916 */       logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */       
/* 7918 */       done = true;
/*      */     }
/* 7920 */     catch (Exception noe) {
/*      */       
/* 7922 */       logger.log(Level.WARNING, "Failed to chop at tree: ", noe);
/* 7923 */       done = true;
/*      */     } 
/*      */     
/* 7926 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static short calcTime(int treeage, Item source, Skill weaponSkill, Skill woodcuttingskill) {
/* 7931 */     if (treeage == 15) {
/* 7932 */       treeage = 7;
/*      */     }
/* 7934 */     int mintime = (short)(30 + treeage * 5);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7944 */     mintime = (int)(mintime * 0.75F + mintime * 0.2F * Math.max(1.0F, 100.0F - source.getSpellSpeedBonus()) / 100.0F);
/*      */ 
/*      */     
/* 7947 */     short time = (short)mintime;
/* 7948 */     double bonus = 0.0D;
/* 7949 */     if (weaponSkill != null) {
/* 7950 */       bonus = weaponSkill.getKnowledge(source, 0.0D);
/*      */     }
/*      */     
/* 7953 */     time = (short)(int)(time * (1.0D + (100.0D - woodcuttingskill.getKnowledge(source, bonus)) / 100.0D));
/*      */ 
/*      */ 
/*      */     
/* 7957 */     time = (short)(int)(time / Servers.localServer.getActionTimer());
/* 7958 */     return time;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isTileModBlocked(Creature performer, int tilex, int tiley, boolean surfaced) {
/* 7964 */     if (performer.getPower() <= 0) {
/*      */       
/* 7966 */       if (Zones.isWithinDuelRing(tilex, tiley, true) != null) {
/*      */         
/* 7968 */         performer.getCommunicator().sendAlertServerMessage("This is too close to the duelling ring.");
/* 7969 */         return true;
/*      */       } 
/* 7971 */       if (Features.Feature.BLOCK_HOTA.isEnabled())
/*      */       {
/* 7973 */         for (FocusZone fz : FocusZone.getZonesAt(tilex, tiley)) {
/*      */           
/* 7975 */           if (fz.isBattleCamp() || fz.isPvPHota() || fz.isNoBuild())
/*      */           {
/* 7977 */             if (fz.covers(tilex, tiley)) {
/*      */               
/* 7979 */               performer.getCommunicator().sendAlertServerMessage("This land is protected by the deities and may not be modified.");
/*      */               
/* 7981 */               return true;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 7987 */     return false;
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
/*      */   public static void paintTerrain(Player player, Item wand, int tileX, int tileY) {
/* 8000 */     byte aux = wand.getAuxData();
/* 8001 */     if (aux == 0)
/*      */       return; 
/* 8003 */     byte newtype = (Tiles.getTile(aux)).id;
/* 8004 */     if (!player.isOnSurface()) {
/*      */       
/* 8006 */       paintCaveTerrain(player, newtype, tileX, tileY);
/*      */       return;
/*      */     } 
/* 8009 */     int dx = Math.max(0, wand.getData1());
/* 8010 */     int dy = Math.max(0, wand.getData2());
/*      */     
/* 8012 */     if (dx > 10)
/* 8013 */       dx = 0; 
/* 8014 */     if (dy > 10) {
/* 8015 */       dy = 0;
/*      */     }
/* 8017 */     if (dx > 10 || dy > 10 || dx < 0 || dy < 0) {
/*      */       
/* 8019 */       player.getCommunicator().sendNormalServerMessage("The data1 and data2 range should be between 0 and 10.");
/*      */       return;
/*      */     } 
/* 8022 */     if (dx == 0 && dy == 0 && Tiles.decodeType(Server.surfaceMesh.getTile(tileX, tileY)) == newtype) {
/*      */       
/* 8024 */       player.getCommunicator().sendNormalServerMessage("The terrain is already of that type.");
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 8030 */     if (Tiles.isSolidCave(newtype) || newtype == Tiles.Tile.TILE_CAVE.id || newtype == Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id) {
/*      */ 
/*      */       
/* 8033 */       if (player.getPower() >= 5) {
/*      */         
/* 8035 */         if (Tiles.isSolidCave(newtype)) {
/*      */           
/* 8037 */           Tiles.Tile theNewTile = Tiles.getTile(newtype);
/* 8038 */           if (theNewTile != null) {
/*      */             
/* 8040 */             Server.caveMesh.setTile(tileX, tileY, Tiles.encode(Tiles.decodeHeight(Server.caveMesh.getTile(tileX, tileY)), theNewTile.id, 
/* 8041 */                   Tiles.decodeData(Server.caveMesh.getTile(tileX, tileY))));
/* 8042 */             Players.getInstance().sendChangedTiles(tileX, tileY, 1, 1, false, false);
/*      */           } 
/*      */         } else {
/*      */           
/* 8046 */           player.getCommunicator().sendNormalServerMessage("You can only change to solid rock types at the moment.");
/*      */         } 
/*      */       } else {
/*      */         
/* 8050 */         player.getCommunicator().sendNormalServerMessage("Only implementors may set the terrain to some sort of rock.");
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 8056 */     for (int x = 0; x < Math.max(1, dx); x++) {
/*      */       
/* 8058 */       for (int y = 0; y < Math.max(1, dy); y++) {
/*      */         
/* 8060 */         byte oldType = Tiles.decodeType(Server.surfaceMesh.getTile(tileX - dx / 2 + x, tileY - dy / 2 + y));
/*      */         
/* 8062 */         if (player.getPower() < 5 && (newtype == Tiles.Tile.TILE_ROCK.id || oldType == Tiles.Tile.TILE_ROCK.id || newtype == Tiles.Tile.TILE_CLIFF.id || oldType == Tiles.Tile.TILE_CLIFF.id)) {
/*      */ 
/*      */ 
/*      */           
/* 8066 */           player
/* 8067 */             .getCommunicator()
/* 8068 */             .sendNormalServerMessage("That would have impact on the rock layer, and is not allowed for now.");
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 8073 */           Tiles.Tile theNewTile = Tiles.getTile(newtype);
/* 8074 */           byte data = 0;
/* 8075 */           byte theNewType = newtype;
/* 8076 */           if (newtype == Tiles.Tile.TILE_GRASS.id) {
/*      */             
/* 8078 */             GrassData.FlowerType flowerType = getRandomFlower(GrassData.FlowerType.NONE, false);
/* 8079 */             if (flowerType != GrassData.FlowerType.NONE) {
/*      */ 
/*      */               
/* 8082 */               GrassData.GrowthStage stage = GrassData.GrowthStage.decodeTileData(0);
/* 8083 */               data = GrassData.encodeGrassTileData(stage, flowerType);
/*      */             } 
/*      */           } 
/* 8086 */           if (newtype == Tiles.Tile.TILE_ROCK.id) {
/*      */             
/* 8088 */             Server.caveMesh.setTile(tileX - dx / 2 + x, tileY - dy / 2 + y, 
/*      */ 
/*      */                 
/* 8091 */                 Tiles.encode((short)-100, Tiles.Tile.TILE_CAVE_WALL.id, (byte)0));
/*      */ 
/*      */             
/* 8094 */             Server.rockMesh
/* 8095 */               .setTile(tileX - dx / 2 + x, tileY - dy / 2 + y, 
/*      */ 
/*      */                 
/* 8098 */                 Tiles.encode(
/* 8099 */                   Tiles.decodeHeight(Server.surfaceMesh.getTile(tileX - dx / 2 + x, tileY - dy / 2 + y)), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 8104 */           else if (theNewTile.isTree() || theNewTile.isBush()) {
/*      */ 
/*      */             
/* 8107 */             byte treeAge = (byte)Server.rand.nextInt((FoliageAge.values()).length);
/*      */             
/* 8109 */             byte grass = (byte)(1 + Server.rand.nextInt(3));
/* 8110 */             data = Tiles.encodeTreeData(treeAge, false, false, grass);
/*      */           } 
/* 8112 */           if ((Tiles.getTile(aux)).id == Tiles.Tile.TILE_ROCK.id) {
/*      */             
/* 8114 */             Server.caveMesh.setTile(tileX - dx / 2 + x, tileY - dy / 2 + y, 
/*      */ 
/*      */                 
/* 8117 */                 Tiles.encode((short)-100, Tiles.Tile.TILE_CAVE_WALL.id, (byte)0));
/*      */             
/* 8119 */             Server.rockMesh
/* 8120 */               .setTile(tileX - dx / 2 + x, tileY - dy / 2 + y, 
/*      */ 
/*      */                 
/* 8123 */                 Tiles.encode(Tiles.decodeHeight(Server.surfaceMesh.getTile(tileX - dx / 2 + x, tileY - dy / 2 + y)), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 8128 */           else if (oldType != Tiles.Tile.TILE_HOLE.id && !Tiles.isMineDoor(oldType)) {
/*      */ 
/*      */             
/* 8131 */             Server.setSurfaceTile(tileX - dx / 2 + x, tileY - dy / 2 + y, 
/* 8132 */                 Tiles.decodeHeight(Server.surfaceMesh.getTile(tileX - dx / 2 + x, tileY - dy / 2 + y)), theNewType, data);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 8138 */     Players.getInstance().sendChangedTiles(tileX - dx / 2, tileY - dy / 2, Math.max(1, dx), Math.max(1, dy), true, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void paintCaveTerrain(Player player, byte newtype, int tilex, int tiley) {
/* 8146 */     int currentTile = Server.caveMesh.getTile(tilex, tiley);
/* 8147 */     byte currentType = Tiles.decodeType(currentTile);
/* 8148 */     if (currentType != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */       
/* 8150 */       if (newtype == Tiles.Tile.TILE_CAVE.id || 
/* 8151 */         Tiles.isReinforcedFloor(newtype) || 
/* 8152 */         Tiles.isSolidCave(newtype)) {
/*      */         boolean succeeded;
/*      */ 
/*      */         
/* 8156 */         if (Tiles.isSolidCave(currentType)) {
/*      */           
/* 8158 */           if (Tiles.isSolidCave(newtype))
/*      */           {
/*      */             
/* 8161 */             succeeded = true;
/*      */ 
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*      */             
/* 8168 */             int rtx = player.getTileX();
/* 8169 */             int rty = player.getTileY();
/*      */             
/* 8171 */             int dir = 2;
/*      */             
/* 8173 */             if (rty - tiley < 0) {
/*      */               
/* 8175 */               dir = 5;
/*      */             }
/* 8177 */             else if (rty - tiley > 0) {
/*      */               
/* 8179 */               dir = 3;
/*      */             }
/* 8181 */             else if (rtx - tilex < 0) {
/*      */               
/* 8183 */               dir = 4;
/*      */             } 
/*      */ 
/*      */             
/* 8187 */             succeeded = TileRockBehaviour.createInsideTunnel(tilex, tiley, currentTile, (Creature)player, 145, dir, true, null);
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 8194 */         else if (currentType == Tiles.Tile.TILE_CAVE.id || 
/* 8195 */           Tiles.isReinforcedFloor(currentType)) {
/*      */           
/* 8197 */           if (Tiles.isSolidCave(newtype)) {
/*      */ 
/*      */             
/* 8200 */             setAsRock(tilex, tiley, false);
/* 8201 */             succeeded = true;
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 8207 */             succeeded = true;
/*      */           } 
/*      */         } else {
/* 8210 */           succeeded = true;
/*      */         } 
/* 8212 */         if (succeeded)
/*      */         {
/* 8214 */           int returnTile = Server.caveMesh.getTile(tilex, tiley);
/* 8215 */           Server.caveMesh.setTile(tilex, tiley, 
/* 8216 */               Tiles.encode(Tiles.decodeHeight(returnTile), newtype, 
/* 8217 */                 Tiles.decodeData(returnTile)));
/*      */           
/* 8219 */           Players.getInstance().sendChangedTile(tilex, tiley, false, true);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 8224 */         player.getCommunicator().sendNormalServerMessage("You must select a cave tile to change to.");
/*      */       }
/*      */     
/* 8227 */     } else if (currentType == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */       
/* 8229 */       if (Tiles.isRoadType(newtype)) {
/*      */         
/* 8231 */         Server.setClientCaveFlags(tilex, tiley, newtype);
/* 8232 */         Players.getInstance().sendChangedTile(tilex, tiley, false, true);
/*      */       }
/*      */       else {
/*      */         
/* 8236 */         player.getCommunicator().sendNormalServerMessage("Removing cave openings is not supported. Use a shaker orb.");
/*      */       } 
/*      */     } else {
/*      */       
/* 8240 */       player.getCommunicator().sendNormalServerMessage("Removing cave openings is not supported. Use a shaker orb.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static GrassData.FlowerType getRandomFlower(GrassData.FlowerType flowerType, boolean ignoreSeason) {
/* 8246 */     int rnd = Server.rand.nextInt(60000);
/* 8247 */     if (rnd < 1000) {
/*      */       
/* 8249 */       if (flowerType == GrassData.FlowerType.NONE && (ignoreSeason || !WurmCalendar.isAutumnWinter())) {
/*      */         
/* 8251 */         if (rnd > 998)
/* 8252 */           return GrassData.FlowerType.FLOWER_7; 
/* 8253 */         if (rnd > 990)
/* 8254 */           return GrassData.FlowerType.FLOWER_6; 
/* 8255 */         if (rnd > 962)
/* 8256 */           return GrassData.FlowerType.FLOWER_5; 
/* 8257 */         if (rnd > 900)
/* 8258 */           return GrassData.FlowerType.FLOWER_4; 
/* 8259 */         if (rnd > 800)
/* 8260 */           return GrassData.FlowerType.FLOWER_3; 
/* 8261 */         if (rnd > 500) {
/* 8262 */           return GrassData.FlowerType.FLOWER_2;
/*      */         }
/* 8264 */         return GrassData.FlowerType.FLOWER_1;
/*      */       } 
/* 8266 */       if (!ignoreSeason && WurmCalendar.isAutumnWinter())
/*      */       {
/* 8268 */         return GrassData.FlowerType.NONE;
/*      */       }
/*      */     } 
/* 8271 */     return flowerType;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean cannotMakeLawn(Creature performer, int tilex, int tiley) {
/* 8276 */     if (!Methods.isActionAllowed(performer, (short)644, performer.getTileX(), performer.getTileY()))
/* 8277 */       return true; 
/* 8278 */     return false;
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
/*      */   private static final boolean isCaveExitBorder(int x, int y) {
/* 8295 */     int currtile = Server.caveMesh.getTile(x, y);
/* 8296 */     short cceil = (short)(Tiles.decodeData(currtile) & 0xFF);
/*      */     
/* 8298 */     if (cceil == 0) {
/* 8299 */       return true;
/*      */     }
/*      */     
/* 8302 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   enum HolderCave
/*      */   {
/* 8311 */     NO_CAVE(0),
/* 8312 */     VALUE_OF_SHAME(0),
/* 8313 */     CAVE_NEIGHBOUR(1),
/* 8314 */     CAVE(2),
/* 8315 */     CAVE_EXIT(3),
/* 8316 */     CAVE_EXIT_CORNER_MATTERS_ROCK(20),
/* 8317 */     CAVE_EXIT_CORNER_MATTERS_DIRT(30),
/* 8318 */     CAVE_EXIT_CORNER_MATTERS_BOTH(40),
/* 8319 */     CAVE_EXIT_NORTH_NW(40),
/* 8320 */     CAVE_EXIT_NORTH_NE(20),
/* 8321 */     CAVE_EXIT_NORTH_SE(3),
/* 8322 */     CAVE_EXIT_NORTH_SW(3),
/* 8323 */     CAVE_EXIT_EAST_NW(30),
/* 8324 */     CAVE_EXIT_EAST_NE(20),
/* 8325 */     CAVE_EXIT_EAST_SE(20),
/* 8326 */     CAVE_EXIT_EAST_SW(3),
/* 8327 */     CAVE_EXIT_SOUTH_NW(30),
/* 8328 */     CAVE_EXIT_SOUTH_NE(3),
/* 8329 */     CAVE_EXIT_SOUTH_SE(20),
/* 8330 */     CAVE_EXIT_SOUTH_SW(3),
/* 8331 */     CAVE_EXIT_WEST_NW(40),
/* 8332 */     CAVE_EXIT_WEST_NE(3),
/* 8333 */     CAVE_EXIT_WEST_SE(3),
/* 8334 */     CAVE_EXIT_WEST_SW(3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     HolderCave(int value) {
/* 8392 */       this.value = value;
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
/*      */   public static final void flattenImmediately(final Creature performer, int stx, int endtx, int sty, int endty, final float minDirtDist, int lowerByAmount, final boolean toRock) {
/* 8426 */     float flattenToHeight = performer.getPositionZ() - lowerByAmount / 10.0F;
/* 8427 */     float totalHeightRock = flattenToHeight - (toRock ? 0.0F : minDirtDist);
/* 8428 */     float totalHeightDirt = flattenToHeight;
/*      */ 
/*      */     
/* 8431 */     if (performer.getPower() < 4) {
/*      */       
/* 8433 */       logger.warning(performer.getName() + " attempted to use flattenImmediately with a power level of " + performer.getPower() + ", DENYING!");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 8438 */     logger.info(performer.getName() + " used flattenImmediately stx:" + stx + ", sty:" + sty + ", endx:" + endtx + ", endy:" + endty + ", lower by extra amount:" + lowerByAmount + ", minDirtDist:" + minDirtDist + ", flattenToRock:" + toRock);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 8443 */     double mapSize = Math.pow(2.0D, Constants.meshSize) - 1.0D;
/* 8444 */     if (endtx > mapSize || endty > mapSize || stx < 1 || sty < 1) {
/* 8445 */       performer.getCommunicator().sendAlertServerMessage("YOU CAN NOT MAKE THE FLATTEN ZONE EXPAND PAST A SERVER BORDER");
/*      */       
/*      */       return;
/*      */     } 
/* 8449 */     if (performer.getPositionZ() + minDirtDist > 32767.0F || performer
/* 8450 */       .getPositionZ() + lowerByAmount > 32767.0F || performer
/* 8451 */       .getPositionZ() + minDirtDist - lowerByAmount > 32767.0F) {
/* 8452 */       performer.getCommunicator().sendAlertServerMessage("You may not set a (combined) value larger than 32767");
/*      */       return;
/*      */     } 
/* 8455 */     if (performer.getPositionZ() - minDirtDist < -32768.0F || performer
/* 8456 */       .getPositionZ() - lowerByAmount < -32768.0F || performer
/* 8457 */       .getPositionZ() - minDirtDist + lowerByAmount < -32768.0F) {
/* 8458 */       performer.getCommunicator().sendAlertServerMessage("You may not set a (combined) value less than 32768");
/*      */       return;
/*      */     } 
/*      */     class compareCoordinates
/*      */       implements Comparable<compareCoordinates>
/*      */     {
/*      */       private final int x;
/*      */       
/*      */       private final int y;
/*      */       
/*      */       public compareCoordinates(int _x, int _y) {
/* 8469 */         this.x = _x;
/* 8470 */         this.y = _y;
/*      */       }
/*      */       
/*      */       public int getX() {
/* 8474 */         return this.x;
/*      */       }
/*      */       
/*      */       public int getY() {
/* 8478 */         return this.y;
/*      */       }
/*      */ 
/*      */       
/*      */       public int compareTo(compareCoordinates o) {
/* 8483 */         if (o.getX() == this.x) {
/* 8484 */           if (o.getY() == getY()) return 0; 
/* 8485 */           if (o.getY() < getY()) return 1; 
/* 8486 */           return -1;
/*      */         } 
/* 8488 */         if (o.getX() < getX()) return 1; 
/* 8489 */         return -1;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */     
/*      */     class InstantFlattenHolder
/*      */     {
/* 8497 */       private Terraforming.HolderCave hasCave = Terraforming.HolderCave.NO_CAVE;
/*      */       
/*      */       private float flattenToHeightDirt;
/*      */       
/*      */       private float flattenToHeightRock;
/*      */       
/*      */       private final int x;
/*      */       private final int y;
/*      */       private final int storedSurface;
/*      */       private final int storedRock;
/*      */       private final int storedCave;
/*      */       
/*      */       public InstantFlattenHolder(int _x, int _y, int _surface, int _rock, int _cave, float _flattenToHeightRock, float _flattenToHeightDirt) {
/* 8510 */         this.flattenToHeightRock = _flattenToHeightRock;
/* 8511 */         this.flattenToHeightDirt = _flattenToHeightDirt;
/* 8512 */         this.x = _x;
/* 8513 */         this.y = _y;
/* 8514 */         this.storedSurface = _surface;
/* 8515 */         this.storedRock = _rock;
/* 8516 */         this.storedCave = _cave;
/*      */       }
/*      */       
/*      */       public Terraforming.HolderCave getCave() {
/* 8520 */         return this.hasCave;
/*      */       }
/*      */       
/*      */       public void setCave(Terraforming.HolderCave cave) {
/* 8524 */         this.hasCave = cave;
/*      */       }
/*      */       
/*      */       public float getFlattenToHeightRock() {
/* 8528 */         return this.flattenToHeightRock;
/*      */       }
/*      */ 
/*      */       
/*      */       public void setFlattenToHeightRock(float _flattenToHeightRock) {
/* 8533 */         this.flattenToHeightRock = _flattenToHeightRock;
/*      */       }
/*      */       
/*      */       public float getFlattenToHeightDirt() {
/* 8537 */         return this.flattenToHeightDirt;
/*      */       }
/*      */ 
/*      */       
/*      */       public void setFlattenToHeightDirt(float _flattenToHeightDirt) {
/* 8542 */         this.flattenToHeightDirt = _flattenToHeightDirt;
/*      */       }
/*      */ 
/*      */       
/*      */       public void handleCaveCalcMagic() {
/* 8547 */         float surfaceHeight = Tiles.decodeHeightAsFloat(this.storedSurface);
/* 8548 */         float rockHeight = Tiles.decodeHeightAsFloat(this.storedRock);
/* 8549 */         float caveHeight = Tiles.decodeHeightAsFloat(this.storedCave);
/* 8550 */         short ceilingHeight = (short)(Tiles.decodeData(this.storedCave) & 0xFF);
/*      */         
/* 8552 */         float totalCaveHeightCaves = ceilingHeight / 10.0F + caveHeight + 0.2F;
/* 8553 */         float totalCaveHeightExits = ceilingHeight / 10.0F + caveHeight;
/*      */         
/* 8555 */         if ((getCave()).value == Terraforming.HolderCave.CAVE.value || 
/* 8556 */           (getCave()).value == Terraforming.HolderCave.CAVE_NEIGHBOUR.value) {
/*      */           
/* 8558 */           if (toRock) {
/*      */             
/* 8560 */             if (this.flattenToHeightRock < totalCaveHeightCaves)
/*      */             {
/* 8562 */               setFlattenToHeightRock(totalCaveHeightCaves);
/* 8563 */               setFlattenToHeightDirt(totalCaveHeightCaves);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 8568 */             float toRockMod = toRock ? 0.0F : minDirtDist;
/* 8569 */             float currentDifference = surfaceHeight - rockHeight;
/* 8570 */             float dirtHeight = (currentDifference < toRockMod) ? currentDifference : toRockMod;
/*      */             
/* 8572 */             if (this.flattenToHeightRock < totalCaveHeightCaves)
/*      */             {
/* 8574 */               setFlattenToHeightRock(totalCaveHeightCaves);
/* 8575 */               setFlattenToHeightDirt(totalCaveHeightCaves + dirtHeight);
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 8582 */         else if ((getCave()).value >= Terraforming.HolderCave.CAVE_EXIT.value) {
/*      */ 
/*      */           
/* 8585 */           setFlattenToHeightDirt(totalCaveHeightExits);
/* 8586 */           setFlattenToHeightRock(totalCaveHeightExits);
/*      */         } else {
/*      */           
/* 8589 */           performer.getCommunicator().sendAlertServerMessage("This should never be reached, but were for [" + this.x + ", " + this.y + "] it has a getCave value of " + 
/* 8590 */               getCave().toString());
/* 8591 */           setCave(Terraforming.HolderCave.VALUE_OF_SHAME);
/*      */         } 
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 8599 */     TreeMap<compareCoordinates, InstantFlattenHolder> flattenArea = new TreeMap<>();
/*      */     
/*      */     int currx;
/*      */     
/* 8603 */     for (currx = stx; currx <= endtx + 1; currx++) {
/* 8604 */       for (int curry = sty; curry <= endty + 1; curry++) {
/*      */         float toPutRockHeight;
/* 8606 */         int currTileCave = Server.caveMesh.getTile(currx, curry);
/* 8607 */         int currTileRock = Server.rockMesh.getTile(currx, curry);
/* 8608 */         int currTileSurface = Server.surfaceMesh.getTile(currx, curry);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 8613 */         if (!toRock) {
/*      */           
/* 8615 */           float currentRockLayerHeight = Tiles.decodeHeightAsFloat(currTileRock);
/*      */           
/* 8617 */           if (totalHeightRock > currentRockLayerHeight) {
/* 8618 */             toPutRockHeight = currentRockLayerHeight;
/*      */           } else {
/* 8620 */             toPutRockHeight = totalHeightRock;
/*      */           } 
/*      */         } else {
/*      */           
/* 8624 */           toPutRockHeight = totalHeightRock;
/*      */         } 
/*      */ 
/*      */         
/* 8628 */         flattenArea.put(new compareCoordinates(currx, curry), new InstantFlattenHolder(currx, curry, currTileSurface, currTileRock, currTileCave, toPutRockHeight, totalHeightDirt));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 8634 */     for (currx = stx; currx <= endtx + 1; currx++) {
/* 8635 */       for (int curry = sty; curry <= endty + 1; curry++) {
/*      */ 
/*      */         
/* 8638 */         InstantFlattenHolder NW = flattenArea.get(new compareCoordinates(currx, curry));
/*      */         
/* 8640 */         byte ID = Tiles.decodeType(InstantFlattenHolder.access$100(NW));
/*      */ 
/*      */         
/* 8643 */         if (ID == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */ 
/*      */           
/* 8646 */           InstantFlattenHolder NE = flattenArea.get(new compareCoordinates(InstantFlattenHolder.access$200(NW) + 1, InstantFlattenHolder.access$300(NW)));
/* 8647 */           InstantFlattenHolder SE = flattenArea.get(new compareCoordinates(InstantFlattenHolder.access$200(NW) + 1, InstantFlattenHolder.access$300(NW) + 1));
/* 8648 */           InstantFlattenHolder SW = flattenArea.get(new compareCoordinates(InstantFlattenHolder.access$200(NW), InstantFlattenHolder.access$300(NW) + 1));
/*      */ 
/*      */           
/* 8651 */           if (isCaveExitBorder(currx, curry)) {
/*      */             
/* 8653 */             if (isCaveExitBorder(currx + 1, curry)) {
/* 8654 */               if (NW != null) NW.setCave(HolderCave.CAVE_EXIT_NORTH_NW); 
/* 8655 */               if (NE != null) NE.setCave(HolderCave.CAVE_EXIT_NORTH_NE); 
/* 8656 */               if (SE != null) SE.setCave(HolderCave.CAVE_EXIT_NORTH_SE); 
/* 8657 */               if (SW != null) SW.setCave(HolderCave.CAVE_EXIT_NORTH_SW);
/*      */               
/* 8659 */               if (Constants.devmode) {
/* 8660 */                 performer.getCommunicator().sendAlertServerMessage("Cave entrance with north border at [" + currx + ", " + curry + "]");
/*      */ 
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/* 8669 */               if (NW != null) NW.setCave(HolderCave.CAVE_EXIT_WEST_NW); 
/* 8670 */               if (NE != null) NE.setCave(HolderCave.CAVE_EXIT_WEST_NE); 
/* 8671 */               if (SE != null) SE.setCave(HolderCave.CAVE_EXIT_WEST_SE); 
/* 8672 */               if (SW != null) SW.setCave(HolderCave.CAVE_EXIT_WEST_SW); 
/* 8673 */               if (Constants.devmode) {
/* 8674 */                 performer.getCommunicator().sendAlertServerMessage("Cave entrance with west border at [" + currx + ", " + curry + "]");
/*      */               }
/*      */             }
/*      */           
/* 8678 */           } else if (isCaveExitBorder(currx + 1, curry)) {
/* 8679 */             if (NW != null) NW.setCave(HolderCave.CAVE_EXIT_EAST_NW); 
/* 8680 */             if (NE != null) NE.setCave(HolderCave.CAVE_EXIT_EAST_NE); 
/* 8681 */             if (SE != null) SE.setCave(HolderCave.CAVE_EXIT_EAST_SE); 
/* 8682 */             if (SW != null) SW.setCave(HolderCave.CAVE_EXIT_EAST_SW);
/*      */             
/* 8684 */             if (Constants.devmode) {
/* 8685 */               performer.getCommunicator().sendAlertServerMessage("Cave entrance with east border at [" + currx + ", " + curry + "]");
/*      */             }
/*      */           } else {
/*      */             
/* 8689 */             if (NW != null) NW.setCave(HolderCave.CAVE_EXIT_SOUTH_NW); 
/* 8690 */             if (NE != null) NE.setCave(HolderCave.CAVE_EXIT_SOUTH_NE); 
/* 8691 */             if (SE != null) SE.setCave(HolderCave.CAVE_EXIT_SOUTH_SE); 
/* 8692 */             if (SW != null) SW.setCave(HolderCave.CAVE_EXIT_SOUTH_SW);
/*      */             
/* 8694 */             if (Constants.devmode) {
/* 8695 */               performer.getCommunicator().sendAlertServerMessage("Cave entrance with south border at [" + currx + ", " + curry + "]");
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
/*      */           
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
/*      */         }
/* 8724 */         else if (!Tiles.isSolidCave(ID)) {
/* 8725 */           InstantFlattenHolder NE = flattenArea.get(new compareCoordinates(InstantFlattenHolder.access$200(NW) + 1, InstantFlattenHolder.access$300(NW)));
/* 8726 */           InstantFlattenHolder SE = flattenArea.get(new compareCoordinates(InstantFlattenHolder.access$200(NW) + 1, InstantFlattenHolder.access$300(NW) + 1));
/* 8727 */           InstantFlattenHolder SW = flattenArea.get(new compareCoordinates(InstantFlattenHolder.access$200(NW), InstantFlattenHolder.access$300(NW) + 1));
/*      */           
/* 8729 */           if (NW != null)
/*      */           {
/* 8731 */             if ((NW.getCave()).value < HolderCave.CAVE.value)
/* 8732 */               NW.setCave(HolderCave.CAVE); 
/*      */           }
/* 8734 */           if (NE != null)
/*      */           {
/* 8736 */             if ((NE.getCave()).value < HolderCave.CAVE_NEIGHBOUR.value)
/* 8737 */               NE.setCave(HolderCave.CAVE_NEIGHBOUR); 
/*      */           }
/* 8739 */           if (SE != null)
/*      */           {
/* 8741 */             if ((SE.getCave()).value < HolderCave.CAVE_NEIGHBOUR.value)
/* 8742 */               SE.setCave(HolderCave.CAVE_NEIGHBOUR); 
/*      */           }
/* 8744 */           if (SW != null)
/*      */           {
/* 8746 */             if ((SW.getCave()).value < HolderCave.CAVE_NEIGHBOUR.value) {
/* 8747 */               SW.setCave(HolderCave.CAVE_NEIGHBOUR);
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 8755 */     for (InstantFlattenHolder ifh : flattenArea.values()) {
/*      */       
/* 8757 */       if (ifh.getCave() == HolderCave.NO_CAVE)
/* 8758 */         continue;  ifh.handleCaveCalcMagic();
/*      */     } 
/*      */ 
/*      */     
/* 8762 */     for (InstantFlattenHolder ifh : flattenArea.values()) {
/*      */       byte spawnedTypeRock;
/*      */ 
/*      */       
/*      */       float toReturnRock;
/*      */       
/*      */       byte spawnedTypeSurface;
/*      */       
/*      */       float toReturnDirtHeight;
/*      */       
/* 8772 */       byte rockData = Tiles.decodeData(InstantFlattenHolder.access$400(ifh));
/*      */ 
/*      */       
/* 8775 */       if ((ifh.getCave()).value == HolderCave.CAVE_EXIT_CORNER_MATTERS_ROCK.value || 
/* 8776 */         (ifh.getCave()).value == HolderCave.CAVE_EXIT_CORNER_MATTERS_BOTH.value) {
/*      */ 
/*      */         
/* 8779 */         toReturnRock = ifh.getFlattenToHeightRock();
/* 8780 */         spawnedTypeRock = Tiles.decodeType(InstantFlattenHolder.access$400(ifh));
/*      */       }
/*      */       else {
/*      */         
/* 8784 */         toReturnRock = ifh.getFlattenToHeightRock();
/* 8785 */         spawnedTypeRock = 4;
/*      */       } 
/*      */ 
/*      */       
/* 8789 */       Server.rockMesh.setTile(InstantFlattenHolder.access$200(ifh), InstantFlattenHolder.access$300(ifh), Tiles.encode(toReturnRock, spawnedTypeRock, rockData));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 8804 */       if (ifh.getCave() == HolderCave.VALUE_OF_SHAME) {
/*      */         
/* 8806 */         spawnedTypeSurface = 37;
/* 8807 */         toReturnDirtHeight = Tiles.decodeHeightAsFloat(InstantFlattenHolder.access$400(ifh));
/*      */       
/*      */       }
/* 8810 */       else if ((ifh.getCave()).value == HolderCave.CAVE_EXIT_CORNER_MATTERS_DIRT.value || 
/* 8811 */         (ifh.getCave()).value == HolderCave.CAVE_EXIT_CORNER_MATTERS_BOTH.value) {
/*      */         
/* 8813 */         toReturnDirtHeight = ifh.getFlattenToHeightRock();
/* 8814 */         spawnedTypeSurface = Tiles.decodeType(InstantFlattenHolder.access$500(ifh));
/*      */       }
/* 8816 */       else if (ifh.getCave() == HolderCave.CAVE || ifh
/* 8817 */         .getCave() == HolderCave.CAVE_NEIGHBOUR || 
/* 8818 */         (ifh.getCave()).value == HolderCave.CAVE_EXIT.value) {
/*      */         
/* 8820 */         if (toRock) { spawnedTypeSurface = 4; }
/* 8821 */         else { spawnedTypeSurface = (byte)((performer.getKingdomTemplateId() == 3) ? 10 : 2); }
/*      */         
/* 8823 */         toReturnDirtHeight = ifh.getFlattenToHeightDirt();
/*      */       
/*      */       }
/* 8826 */       else if (toRock) {
/*      */         
/* 8828 */         spawnedTypeSurface = 4;
/* 8829 */         toReturnDirtHeight = ifh.getFlattenToHeightDirt();
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 8834 */         spawnedTypeSurface = (byte)((performer.getKingdomTemplateId() == 3) ? 10 : 2);
/*      */ 
/*      */         
/* 8837 */         toReturnDirtHeight = totalHeightDirt;
/*      */       } 
/*      */ 
/*      */       
/* 8841 */       Server.surfaceMesh.setTile(InstantFlattenHolder.access$200(ifh), InstantFlattenHolder.access$300(ifh), 
/* 8842 */           Tiles.encode(toReturnDirtHeight, spawnedTypeSurface, (byte)0));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 8847 */     Players.getInstance().sendChangedTiles(stx, sty, endtx - stx + 2, endty - sty + 2, true, false);
/* 8848 */     Players.getInstance().sendChangedTiles(stx, sty, endtx - stx + 2, endty - sty + 2, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean plantTrellis(Creature performer, Item trellis, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, short action, float counter, Action act) {
/* 8857 */     if (trellis.getCurrentQualityLevel() < 10.0F) {
/*      */       
/* 8859 */       performer.getCommunicator().sendNormalServerMessage("The " + trellis
/* 8860 */           .getName() + " is of too poor quality to be planted.");
/* 8861 */       return true;
/*      */     } 
/* 8863 */     if (trellis.getDamage() > 70.0F) {
/*      */       
/* 8865 */       performer.getCommunicator().sendNormalServerMessage("The " + trellis
/* 8866 */           .getName() + " is too heavily damaged to be planted.");
/* 8867 */       return true;
/*      */     } 
/* 8869 */     if (performer.getFloorLevel() != 0) {
/*      */       
/* 8871 */       performer.getCommunicator().sendNormalServerMessage("The " + trellis
/* 8872 */           .getName() + " can not be planted unless on ground level.");
/* 8873 */       return true;
/*      */     } 
/* 8875 */     if (trellis.isSurfaceOnly() && !performer.isOnSurface()) {
/*      */       
/* 8877 */       performer.getCommunicator().sendNormalServerMessage("The " + trellis
/* 8878 */           .getName() + " can only be planted on the surface.");
/* 8879 */       return true;
/*      */     } 
/* 8881 */     if (!onSurface) {
/*      */       
/* 8883 */       performer.getCommunicator().sendNormalServerMessage("The " + trellis.getName() + " would never grow inside a cave.");
/* 8884 */       return true;
/*      */     } 
/* 8886 */     if (trellis.isPlanted()) {
/*      */       
/* 8888 */       performer.getCommunicator().sendNormalServerMessage("The " + trellis.getName() + " is already planted.", (byte)3);
/*      */       
/* 8890 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 8895 */     float rot = 0.0F;
/* 8896 */     float hoff = 0.3F;
/* 8897 */     float hoffx = 2.0F;
/* 8898 */     float hoffy = 2.0F;
/* 8899 */     if (tilex == performer.getTileX()) {
/*      */       
/* 8901 */       if (tiley == performer.getTileY()) {
/*      */         
/* 8903 */         if (dir == Tiles.TileBorderDirection.DIR_HORIZ)
/*      */         {
/*      */           
/* 8906 */           hoffy = 0.3F;
/*      */           
/* 8908 */           hoffx = (action == 746) ? 1.0F : ((action == 747) ? 3.0F : 2.0F);
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 8913 */           hoffx = 0.3F;
/* 8914 */           rot = 270.0F;
/*      */           
/* 8916 */           hoffy = (action == 746) ? 3.0F : ((action == 747) ? 1.0F : 2.0F);
/*      */         }
/*      */       
/* 8919 */       } else if (tiley - 1 == performer.getTileY()) {
/*      */ 
/*      */         
/* 8922 */         hoffy = 3.7F;
/* 8923 */         rot = 180.0F;
/*      */         
/* 8925 */         hoffx = (action == 746) ? 3.0F : ((action == 747) ? 1.0F : 2.0F);
/*      */       }
/*      */       else {
/*      */         
/* 8929 */         performer.getCommunicator().sendNormalServerMessage("You cannot reach that far.");
/* 8930 */         return true;
/*      */       }
/*      */     
/* 8933 */     } else if (tilex - 1 == performer.getTileX()) {
/*      */       
/* 8935 */       if (tiley == performer.getTileY())
/*      */       {
/*      */         
/* 8938 */         hoffx = 3.7F;
/* 8939 */         rot = 90.0F;
/*      */         
/* 8941 */         hoffy = (action == 746) ? 1.0F : ((action == 747) ? 3.0F : 2.0F);
/*      */       }
/*      */       else
/*      */       {
/* 8945 */         performer.getCommunicator().sendNormalServerMessage("You cannot reach that far.");
/* 8946 */         return true;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 8951 */       performer.getCommunicator().sendNormalServerMessage("You cannot reach that far.");
/* 8952 */       return true;
/*      */     } 
/*      */     
/* 8955 */     int time = Actions.getPlantActionTime(performer, trellis);
/* 8956 */     if (counter == 1.0F) {
/*      */       
/* 8958 */       if (performer instanceof Player) {
/*      */         
/* 8960 */         Player p = (Player)performer;
/*      */ 
/*      */         
/*      */         try {
/* 8964 */           Skills skills = p.getSkills();
/* 8965 */           Skill dig = skills.getSkill(1009);
/* 8966 */           if (dig.getRealKnowledge() < 10.0D)
/*      */           {
/* 8968 */             performer.getCommunicator().sendNormalServerMessage("You need to have 10 in the skill digging to secure " + trellis
/*      */                 
/* 8970 */                 .getTemplate().getPlural() + " to the ground.", (byte)3);
/*      */ 
/*      */             
/* 8973 */             return true;
/*      */           }
/*      */         
/* 8976 */         } catch (NoSuchSkillException nss) {
/*      */           
/* 8978 */           performer.getCommunicator().sendNormalServerMessage("You need 10 digging to plant " + trellis
/* 8979 */               .getTemplate().getPlural() + ".", (byte)3);
/*      */           
/* 8981 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 8985 */       if (!Methods.isActionAllowed(performer, (short)176))
/* 8986 */         return true; 
/* 8987 */       int tile = performer.getCurrentTileNum();
/* 8988 */       if (Tiles.decodeHeight(tile) < 0) {
/*      */         
/* 8990 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to plant the " + trellis
/* 8991 */             .getName() + ".", (byte)3);
/* 8992 */         return true;
/*      */       } 
/* 8994 */       if (performer.getStatus().getBridgeId() != -10L) {
/*      */         
/* 8996 */         performer.getCommunicator().sendNormalServerMessage("You cannot plant a " + trellis
/* 8997 */             .getName() + " on a bridge as no soil for it to grow from.", (byte)3);
/*      */         
/* 8999 */         return true;
/*      */       } 
/* 9001 */       if (trellis.isFourPerTile()) {
/*      */         
/* 9003 */         VolaTile vt = Zones.getTileOrNull(performer.getTileX(), performer.getTileY(), trellis.isOnSurface());
/* 9004 */         if (vt != null && vt.getFourPerTileCount(0) >= 4) {
/*      */           
/* 9006 */           performer.getCommunicator().sendNormalServerMessage("You cannot plant a " + trellis
/* 9007 */               .getName() + " as there are four here already.", (byte)3);
/*      */           
/* 9009 */           return true;
/*      */         } 
/*      */       } 
/* 9012 */       act.setTimeLeft(time);
/* 9013 */       performer.getCommunicator().sendNormalServerMessage("You start to plant the " + trellis.getName() + ".");
/* 9014 */       Server.getInstance().broadCastAction(performer.getName() + " starts to plant " + trellis.getNameWithGenus() + ".", performer, 5);
/*      */       
/* 9016 */       performer.sendActionControl("Planting " + trellis.getName(), true, time);
/* 9017 */       performer.getStatus().modifyStamina(-400.0F);
/*      */     }
/*      */     else {
/*      */       
/* 9021 */       time = act.getTimeLeft();
/* 9022 */       if (act.currentSecond() % 5 == 0)
/* 9023 */         performer.getStatus().modifyStamina(-1000.0F); 
/*      */     } 
/* 9025 */     if (counter * 10.0F > time) {
/*      */ 
/*      */       
/*      */       try {
/* 9029 */         if (trellis.isFourPerTile()) {
/*      */           
/* 9031 */           VolaTile vt = Zones.getTileOrNull(performer.getTileX(), performer.getTileY(), trellis.isOnSurface());
/* 9032 */           if (vt != null && vt.getFourPerTileCount(0) == 4) {
/*      */             
/* 9034 */             performer.getCommunicator().sendNormalServerMessage("You cannot plant a " + trellis
/* 9035 */                 .getName() + " as there are four here already.", (byte)3);
/*      */             
/* 9037 */             return true;
/*      */           } 
/*      */         } 
/* 9040 */         long lParentId = trellis.getParentId();
/* 9041 */         if (lParentId != -10L) {
/*      */           
/* 9043 */           Item parent = Items.getItem(lParentId);
/* 9044 */           parent.dropItem(trellis.getWurmId(), false);
/*      */         } 
/* 9046 */         int encodedTile = Server.surfaceMesh.getTile(performer.getTileX(), performer.getTileY());
/* 9047 */         float npsz = Tiles.decodeHeightAsFloat(encodedTile);
/* 9048 */         trellis.setPos((performer.getTileX() * 4) + hoffx, (performer.getTileY() * 4) + hoffy, npsz, rot, -10L);
/*      */ 
/*      */         
/* 9051 */         Zone zone = Zones.getZone(Zones.safeTileX(performer.getTileX()), 
/* 9052 */             Zones.safeTileY(performer.getTileY()), performer.isOnSurface());
/*      */         
/* 9054 */         zone.addItem(trellis);
/* 9055 */         trellis.setIsPlanted(true);
/* 9056 */         performer.getCommunicator().sendNormalServerMessage("You plant the " + trellis.getName() + ".");
/* 9057 */         Server.getInstance().broadCastAction(performer.getName() + " plants the " + trellis.getName() + ".", performer, 5);
/*      */       }
/* 9059 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 9061 */         performer.getCommunicator().sendNormalServerMessage("You fail to plant the " + trellis
/* 9062 */             .getName() + ". Something is weird.");
/* 9063 */         logger.log(Level.WARNING, performer.getName() + ": " + nsz.getMessage(), (Throwable)nsz);
/*      */       }
/* 9065 */       catch (NoSuchItemException nsie) {
/*      */         
/* 9067 */         performer.getCommunicator().sendNormalServerMessage("You fail to plant the " + trellis
/* 9068 */             .getName() + ". Something is weird.");
/* 9069 */         logger.log(Level.WARNING, performer.getName() + ": " + nsie.getMessage(), (Throwable)nsie);
/*      */       } 
/* 9071 */       return true;
/*      */     } 
/* 9073 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int calcMaxGrubs(double currentSkill, @Nullable Item tool) {
/* 9078 */     int bonus = 0;
/* 9079 */     if (tool != null && tool.getSpellEffects() != null) {
/*      */       
/* 9081 */       float extraChance = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_FARMYIELD) - 1.0F;
/* 9082 */       if (extraChance > 0.0F && Server.rand.nextFloat() < extraChance)
/*      */       {
/* 9084 */         bonus++;
/*      */       }
/*      */     } 
/*      */     
/* 9088 */     return Math.min(4, (int)(currentSkill + 28.0D) / 27 + bonus);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int calcMaxHarvest(int tile, double currentSkill, Item tool) {
/* 9094 */     byte data = Tiles.decodeData(tile);
/* 9095 */     byte age = FoliageAge.getAgeAsByte(data);
/* 9096 */     int maxByAge = 1;
/* 9097 */     if (age < FoliageAge.OLD_ONE.getAgeId()) {
/* 9098 */       maxByAge = 1;
/* 9099 */     } else if (age < FoliageAge.OLD_TWO.getAgeId()) {
/* 9100 */       maxByAge = 2;
/* 9101 */     } else if (age < FoliageAge.VERY_OLD.getAgeId()) {
/* 9102 */       maxByAge = 3;
/* 9103 */     } else if (age < FoliageAge.OVERAGED.getAgeId()) {
/* 9104 */       maxByAge = 4;
/*      */     } 
/* 9106 */     int bonus = 0;
/* 9107 */     if (tool.getSpellEffects() != null) {
/*      */       
/* 9109 */       float extraChance = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_FARMYIELD) - 1.0F;
/* 9110 */       if (extraChance > 0.0F && Server.rand.nextFloat() < extraChance)
/*      */       {
/* 9112 */         bonus++;
/*      */       }
/*      */     } 
/*      */     
/* 9116 */     return Math.min(maxByAge, (int)(currentSkill + 28.0D) / 27 + bonus);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Terraforming.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */