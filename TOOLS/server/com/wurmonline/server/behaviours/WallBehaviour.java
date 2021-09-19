/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.questions.DemolishCheckQuestion;
/*      */ import com.wurmonline.server.questions.ManageObjectList;
/*      */ import com.wurmonline.server.questions.ManagePermissions;
/*      */ import com.wurmonline.server.questions.MissionManager;
/*      */ import com.wurmonline.server.questions.PermissionsHistory;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.DbDoor;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchLockException;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.NoSuchWallException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.StructureSupport;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.structures.WallEnum;
/*      */ import com.wurmonline.server.tutorial.MissionTrigger;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.StructureMaterialEnum;
/*      */ import com.wurmonline.shared.constants.StructureStateEnum;
/*      */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*      */ import java.io.IOException;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
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
/*      */ public final class WallBehaviour
/*      */   extends Behaviour
/*      */   implements MiscConstants, ItemTypes
/*      */ {
/*   85 */   private static final Logger logger = Logger.getLogger(WallBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   91 */   private static Item[] inventoryItems = new Item[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   97 */   private static Item[] bodyItems = new Item[0];
/*      */   
/*   99 */   private static int[] foundTemplates = new int[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   WallBehaviour() {
/*  106 */     super((short)20);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isValidModifyableWall(Wall wall, Item tool) {
/*  111 */     int templateId = tool.getTemplateId();
/*      */ 
/*      */     
/*  114 */     if (wall.getMaterial() == StructureMaterialEnum.STONE)
/*      */     {
/*  116 */       return (templateId == 219);
/*      */     }
/*  118 */     if (wall.getMaterial() == StructureMaterialEnum.PLAIN_STONE)
/*      */     {
/*  120 */       if (templateId == 62 || templateId == 63)
/*  121 */         return (wall.getType() != StructureTypeEnum.NARROW_WINDOW && wall.getType() != StructureTypeEnum.BARRED); 
/*      */     }
/*  123 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Wall wall) {
/*  129 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  130 */     toReturn.addAll(super.getBehavioursFor(performer, source, wall));
/*      */     
/*  132 */     VolaTile wallTile = Zones.getOrCreateTile(wall.getTileX(), wall.getTileY(), wall.isOnSurface());
/*  133 */     Structure structure = Structures.getStructureOrNull(wall.getStructureId());
/*  134 */     if (structure != null && structure.isFinalized() && !wall.isFinished()) {
/*  135 */       toReturn.add(Actions.actionEntrys[607]);
/*      */     }
/*  137 */     if (isValidModifyableWall(wall, source))
/*      */     {
/*  139 */       if (source.getTemplateId() == 219) {
/*  140 */         toReturn.add(new ActionEntry((short)647, "Remove decorations", "removing decorations"));
/*      */       } else {
/*  142 */         toReturn.add(new ActionEntry((short)647, "Add decorations", "adding decorations"));
/*      */       } 
/*      */     }
/*  145 */     if (MethodsStructure.isCorrectToolForBuilding(performer, source.getTemplateId())) {
/*      */       
/*  147 */       if (structure != null) {
/*      */         
/*  149 */         boolean hasMarker = StructureBehaviour.hasMarker(wall.getStartX(), wall.getStartY(), wall.isOnSurface(), wall.getDir(), wall.getHeight());
/*  150 */         if (!structure.isFinalized()) {
/*      */           
/*  152 */           toReturn.add(Actions.actionEntrys[58]);
/*      */         }
/*  154 */         else if (wall.getType() == StructureTypeEnum.PLAN && hasMarker) {
/*      */           
/*  156 */           toReturn.addAll(addBuildMenu(performer, source, wall, hasMarker));
/*      */         }
/*  158 */         else if (wall.getType() == StructureTypeEnum.PLAN && structure.needsDoor()) {
/*      */           
/*  160 */           toReturn.addAll(addBuildMenu(performer, source, wall, hasMarker));
/*      */ 
/*      */ 
/*      */ 
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
/*  174 */         else if (!wall.isFinished()) {
/*      */ 
/*      */ 
/*      */           
/*  178 */           if (wall.getState() == StructureStateEnum.INITIALIZED)
/*      */           {
/*  180 */             toReturn.addAll(addBuildMenu(performer, source, wall, hasMarker));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  196 */             WallEnum typeOfWall = WallEnum.getWall(wall.getType(), wall.getMaterial());
/*  197 */             StructureTypeEnum type = wall.getType();
/*  198 */             if (typeOfWall != WallEnum.WALL_PLAN)
/*      */             {
/*  200 */               if (type == StructureTypeEnum.DOOR) {
/*  201 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue door", "building door", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  204 */               else if (type == StructureTypeEnum.DOUBLE_DOOR) {
/*  205 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue door", "building double door", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  208 */               else if (type == StructureTypeEnum.ARCHED) {
/*  209 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue arched wall", "building arched wall", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  212 */               else if (type == StructureTypeEnum.ARCHED_LEFT) {
/*  213 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue left arch", "building arched wall", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  216 */               else if (type == StructureTypeEnum.ARCHED_RIGHT) {
/*  217 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue right arch", "building arched wall", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  220 */               else if (type == StructureTypeEnum.ARCHED_T) {
/*  221 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue T arch", "building arched wall", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  224 */               else if (type == StructureTypeEnum.SOLID) {
/*  225 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue wall", "building wall", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  228 */               else if (type == StructureTypeEnum.PORTCULLIS) {
/*  229 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue portcullis", "building portcullis", new int[] { 43 }, 2));
/*      */ 
/*      */               
/*      */               }
/*  233 */               else if (type == StructureTypeEnum.BARRED) {
/*  234 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue barred wall", "building barred wall", new int[] { 43 }, 2));
/*      */ 
/*      */               
/*      */               }
/*  238 */               else if (type == StructureTypeEnum.BALCONY) {
/*  239 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue balcony", "building balcony", new int[] { 43 }, 2));
/*      */ 
/*      */               
/*      */               }
/*  243 */               else if (type == StructureTypeEnum.JETTY) {
/*  244 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue jetty", "building jetty", new int[] { 43 }, 2));
/*      */ 
/*      */               
/*      */               }
/*  248 */               else if (type == StructureTypeEnum.ORIEL) {
/*  249 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue oriel", "building oriel", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*  252 */               else if (type == StructureTypeEnum.CANOPY_DOOR) {
/*  253 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue canopy door", "building canopy", new int[] { 43 }, 2));
/*      */ 
/*      */               
/*      */               }
/*  257 */               else if (type == StructureTypeEnum.WIDE_WINDOW) {
/*  258 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue wide window", "building wide widnow", new int[] { 43 }, 2));
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/*  263 */                 toReturn.add(new ActionEntry(typeOfWall.getActionId(), "Continue window", "building window", new int[] { 43 }, 2));
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*  271 */     } else if (source.isLock() && source.getTemplateId() == 167) {
/*      */       
/*  273 */       if (wall.getType() == StructureTypeEnum.DOOR || wall.getType() == StructureTypeEnum.DOUBLE_DOOR || wall
/*  274 */         .getType() == StructureTypeEnum.PORTCULLIS || wall.getType() == StructureTypeEnum.CANOPY_DOOR)
/*      */       {
/*  276 */         if (wall.isFinished())
/*      */         {
/*  278 */           if (structure != null)
/*      */           {
/*  280 */             if (structure.mayModify(performer)) {
/*      */               
/*  282 */               Door[] doors = structure.getAllDoors();
/*  283 */               for (int x = 0; x < doors.length; x++) {
/*      */ 
/*      */                 
/*      */                 try {
/*  287 */                   if (doors[x].getWall() == wall) {
/*      */ 
/*      */                     
/*      */                     try {
/*  291 */                       doors[x].getLockId();
/*  292 */                       toReturn.add(new ActionEntry((short)161, "Change lock", "changing lock", emptyIntArr));
/*      */ 
/*      */                     
/*      */                     }
/*  296 */                     catch (NoSuchLockException nsl) {
/*      */                       
/*  298 */                       toReturn.add(Actions.actionEntrys[161]);
/*      */                     } 
/*      */                     
/*      */                     break;
/*      */                   } 
/*  303 */                 } catch (NoSuchWallException nsw) {
/*      */                   
/*  305 */                   logger.log(Level.WARNING, "No inner wall");
/*      */                 }
/*      */               
/*      */               } 
/*      */             } 
/*      */           }
/*      */         }
/*      */       }
/*  313 */     } else if (source.getTemplateId() == 463) {
/*      */       
/*  315 */       if ((wall.getType() == StructureTypeEnum.DOOR || wall.getType() == StructureTypeEnum.DOUBLE_DOOR || wall
/*  316 */         .getType() == StructureTypeEnum.PORTCULLIS || wall.getType() == StructureTypeEnum.CANOPY_DOOR) && (wall
/*  317 */         .isOnPvPServer() || Servers.isThisATestServer()))
/*      */       {
/*  319 */         if (wall.isFinished())
/*      */         {
/*  321 */           Door door = wall.getDoor();
/*  322 */           if (door != null) {
/*      */             
/*      */             try {
/*      */               
/*  326 */               Item lock = door.getLock();
/*  327 */               if (performer.isWithinDistanceTo(wall.getTileX(), wall.getTileY(), 1))
/*      */               {
/*  329 */                 MethodsStructure.addLockPickEntry(performer, source, door, false, lock, toReturn);
/*      */               }
/*      */             }
/*  332 */             catch (NoSuchLockException noSuchLockException) {}
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     }
/*  340 */     else if (source.getTemplateId() == wall.getRepairItemTemplate()) {
/*      */       
/*  342 */       if (wall.getDamage() > 0.0F) {
/*      */         
/*  344 */         if (!wall.isNoRepair())
/*      */         {
/*  346 */           if (!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0) {
/*  347 */             toReturn.add(Actions.actionEntrys[193]);
/*      */           }
/*      */         }
/*  350 */       } else if (wall.isFinished() && wall.getQualityLevel() < 100.0F && !wall.isNoImprove()) {
/*      */         
/*  352 */         toReturn.add(Actions.actionEntrys[192]);
/*      */       }
/*      */     
/*  355 */     } else if (source.isColor()) {
/*      */       
/*  357 */       if (wall.isFinished())
/*      */       {
/*  359 */         if (!wall.isNotPaintable()) {
/*  360 */           toReturn.add(Actions.actionEntrys[231]);
/*      */         }
/*      */       }
/*  363 */     } else if (source.getTemplateId() == 441) {
/*      */       
/*  365 */       if (wall.getColor() != -1 && !wall.isNotPaintable()) {
/*  366 */         toReturn.add(Actions.actionEntrys[232]);
/*      */       }
/*  368 */     } else if (source.getTemplateId() == 676) {
/*      */       
/*  370 */       if (source.getOwnerId() == performer.getWurmId())
/*  371 */         toReturn.add(Actions.actionEntrys[472]); 
/*      */     } 
/*  373 */     boolean addedDestroy = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  384 */     if (performer.getPower() >= 2) {
/*      */       
/*  386 */       if (structure != null) {
/*      */         
/*  388 */         int num = 0;
/*  389 */         if (structure.isFinalized() && wall.getType() != StructureTypeEnum.PLAN) {
/*      */           
/*  391 */           if (wall.getDamage() > 0.0F) {
/*  392 */             num++;
/*  393 */           } else if (wall.getQualityLevel() < 100.0F) {
/*  394 */             num++;
/*      */           } 
/*  396 */           if (performer.getPower() >= 5) {
/*  397 */             num++;
/*      */           }
/*  399 */           toReturn.add(new ActionEntry((short)-(num + 3), "Wall", "wall"));
/*  400 */           if (wall.getDamage() > 0.0F) {
/*      */             
/*  402 */             if (!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0) {
/*  403 */               toReturn.add(Actions.actionEntrys[193]);
/*      */             }
/*  405 */           } else if (wall.getQualityLevel() < 100.0F) {
/*      */             
/*  407 */             toReturn.add(Actions.actionEntrys[192]);
/*      */           } 
/*  409 */           toReturn.add(Actions.actionEntrys[180]);
/*  410 */           toReturn.add(new ActionEntry((short)-1, "Annihilate", "Annihilate"));
/*  411 */           toReturn.add(Actions.actionEntrys[82]);
/*  412 */           toReturn.add(Actions.actionEntrys[662]);
/*  413 */           if (performer.getPower() >= 5)
/*      */           {
/*  415 */             toReturn.add(Actions.actionEntrys[90]);
/*      */           }
/*      */         }
/*  418 */         else if (structure.isFinalized() && wall.getType() == StructureTypeEnum.PLAN && performer
/*  419 */           .getPower() >= 4) {
/*      */           
/*  421 */           toReturn.add(Actions.actionEntrys[866]);
/*      */         }
/*  423 */         else if ((source.getTemplateId() == 176 || source.getTemplateId() == 315) && 
/*  424 */           WurmPermissions.mayUseGMWand(performer)) {
/*      */           
/*  426 */           if (!addedDestroy) {
/*      */             
/*  428 */             toReturn.add(new ActionEntry((short)-1, "Annihilate", "Annihilate"));
/*  429 */             toReturn.add(Actions.actionEntrys[82]);
/*  430 */             addedDestroy = true;
/*      */           } 
/*  432 */           toReturn.add(Actions.actionEntrys[662]);
/*      */         } 
/*  434 */         toReturn.add(Actions.actionEntrys[78]);
/*      */       } 
/*  436 */       if ((source.getTemplateId() == 315 || source.getTemplateId() == 176) && performer
/*  437 */         .getPower() >= 2)
/*  438 */         toReturn.add(Actions.actionEntrys[684]); 
/*      */     } 
/*  440 */     Skills skills = performer.getSkills();
/*      */     
/*      */     try {
/*  443 */       Skill str = skills.getSkill(102);
/*  444 */       if (str.getRealKnowledge() > 21.0D)
/*      */       {
/*  446 */         if (!wall.isWallPlan())
/*      */         {
/*  448 */           if (!wall.isRubble())
/*      */           {
/*  450 */             toReturn.add(new ActionEntry((short)-1, "Wall", "Wall"));
/*  451 */             toReturn.add(Actions.actionEntrys[174]);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*  456 */         else if (structure != null)
/*      */         {
/*      */           
/*  459 */           if (!structure.hasWalls() && !addedDestroy)
/*      */           {
/*  461 */             toReturn.add(new ActionEntry((short)-1, "Structure", "Structure"));
/*  462 */             toReturn.add(Actions.actionEntrys[82]);
/*  463 */             addedDestroy = true;
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  469 */     } catch (NoSuchSkillException nss) {
/*      */       
/*  471 */       logger.log(Level.WARNING, "Weird, " + performer.getName() + " has no strength!");
/*      */     } 
/*  473 */     if (isIndoorWallPlan(wall) == true)
/*      */     {
/*  475 */       toReturn.add(Actions.actionEntrys[57]);
/*      */     }
/*  477 */     if (source.isTrellis() && performer.getFloorLevel() == 0) {
/*      */       
/*  479 */       toReturn.add(new ActionEntry((short)-3, "Plant", "Plant options"));
/*  480 */       toReturn.add(Actions.actionEntrys[746]);
/*  481 */       toReturn.add(new ActionEntry((short)176, "In center", "planting"));
/*  482 */       toReturn.add(Actions.actionEntrys[747]);
/*      */     } 
/*      */     
/*  485 */     MissionTrigger[] m2 = MissionTriggers.getMissionTriggersWith(source.getTemplateId(), 473, wall.getId());
/*  486 */     if (m2.length > 0)
/*  487 */       toReturn.add(Actions.actionEntrys[473]); 
/*  488 */     MissionTrigger[] m3 = MissionTriggers.getMissionTriggersWith(source.getTemplateId(), 474, wall.getId());
/*  489 */     if (m3.length > 0) {
/*  490 */       toReturn.add(Actions.actionEntrys[474]);
/*      */     }
/*  492 */     addWarStuff(toReturn, performer, wall);
/*  493 */     toReturn.addAll(addManage(performer, structure, wall));
/*      */     
/*  495 */     if (wall.isFinished())
/*      */     {
/*  497 */       if (MethodsStructure.mayModifyStructure(performer, structure, wallTile, (short)683)) {
/*      */         
/*  499 */         toReturn.add(Actions.actionEntrys[683]);
/*  500 */         if (wall.isPlainStone() && (source.getTemplateId() == 130 || (source
/*  501 */           .isWand() && performer.getPower() >= 4))) {
/*  502 */           toReturn.add(Actions.actionEntrys[847]);
/*  503 */         } else if (wall.isPlastered() && (source.getTemplateId() == 1115 || (source
/*  504 */           .isWand() && performer.getPower() >= 4))) {
/*  505 */           toReturn.add(new ActionEntry((short)847, "Remove render", "removing"));
/*  506 */         }  if (wall.isLRArch() && (source.getTemplateId() == 1115 || (source
/*  507 */           .isWand() && performer.getPower() >= 4)))
/*  508 */           toReturn.add(Actions.actionEntrys[848]); 
/*      */       } 
/*      */     }
/*  511 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final List<ActionEntry> addBuildMenu(Creature performer, Item source, Wall wall, boolean hasMarker) {
/*  516 */     List<ActionEntry> hlist = new LinkedList<>();
/*  517 */     List<List<ActionEntry>> alist = new LinkedList<>();
/*  518 */     StructureMaterialEnum[] materials = WallEnum.getMaterialsFromToolType(source, performer);
/*  519 */     for (StructureMaterialEnum material : materials) {
/*      */       
/*  521 */       List<ActionEntry> mlist = new LinkedList<>();
/*  522 */       List<WallEnum> wallTypes = WallEnum.getWallsByToolAndMaterial(performer, source, false, (hasMarker || 
/*  523 */           MethodsStructure.hasInsideFence(wall)), material);
/*  524 */       if (wallTypes.size() > 0) {
/*      */         
/*  526 */         hlist.add(new ActionEntry((short)-wallTypes.size(), Wall.getMaterialName(material), "building"));
/*  527 */         for (int i = 0; i < wallTypes.size(); i++)
/*      */         {
/*  529 */           mlist.add(((WallEnum)wallTypes.get(i)).createActionEntry());
/*      */         }
/*  531 */         Collections.sort(mlist);
/*  532 */         alist.add(mlist);
/*      */       } 
/*      */     } 
/*  535 */     List<ActionEntry> toReturn = new LinkedList<>();
/*      */     
/*  537 */     switch (hlist.size()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*  561 */         return toReturn;
/*      */       case 1:
/*      */         toReturn.add(new ActionEntry((short)-((List)alist.get(0)).size(), "Build", "building")); toReturn.addAll(alist.get(0));
/*      */     }  toReturn.add(new ActionEntry((short)-hlist.size(), "Build", "building")); int count = 0; for (List<ActionEntry> zlist : alist) {
/*      */       toReturn.add(hlist.get(count++)); toReturn.addAll(zlist);
/*  566 */     }  } private static final void addWarStuff(List<ActionEntry> toReturn, Creature performer, Wall wall) { Village targVill = wall.getTile().getVillage();
/*  567 */     Village village = performer.getCitizenVillage();
/*  568 */     if (village != null && village.mayDoDiplomacy(performer) && targVill != null)
/*      */     {
/*  570 */       if (village != targVill) {
/*      */         
/*  572 */         boolean atPeace = village.mayDeclareWarOn(targVill);
/*  573 */         if (atPeace) {
/*      */           
/*  575 */           toReturn.add(new ActionEntry((short)-1, "Village", "Village options", emptyIntArr));
/*  576 */           toReturn.add(Actions.actionEntrys[209]);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final List<ActionEntry> addManage(Creature performer, Structure structure, Wall wall) {
/*  584 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  585 */     List<ActionEntry> permissions = new LinkedList<>();
/*      */     
/*  587 */     if (structure.isFinalized()) {
/*      */       
/*  589 */       if (structure.mayManage(performer))
/*  590 */         permissions.add(Actions.actionEntrys[673]); 
/*  591 */       if (structure.mayShowPermissions(performer) || structure.isActualOwner(performer.getWurmId()))
/*  592 */         permissions.add(Actions.actionEntrys[664]); 
/*  593 */       Door door = wall.getDoor();
/*  594 */       if (door != null && (door.mayShowPermissions(performer) || structure
/*  595 */         .mayManage(performer) || performer.getPower() > 1))
/*  596 */         permissions.add(Actions.actionEntrys[666]); 
/*  597 */       if (structure.maySeeHistory(performer)) {
/*      */         
/*  599 */         permissions.add(Actions.actionEntrys[691]);
/*  600 */         if (wall.isFinished() && door != null)
/*  601 */           permissions.add(Actions.actionEntrys[692]); 
/*      */       } 
/*  603 */       if (!permissions.isEmpty()) {
/*      */         
/*  605 */         if (permissions.size() > 1) {
/*      */           
/*  607 */           Collections.sort(permissions);
/*  608 */           toReturn.add(new ActionEntry((short)-permissions.size(), "Permissions", "viewing"));
/*      */         } 
/*  610 */         toReturn.addAll(permissions);
/*      */       } 
/*  612 */       if (door != null)
/*      */       {
/*  614 */         if (door.mayLock(performer) && door.hasLock())
/*      */         {
/*  616 */           if (door.isLocked()) {
/*  617 */             toReturn.add(Actions.actionEntrys[102]);
/*      */           } else {
/*  619 */             toReturn.add(Actions.actionEntrys[28]);
/*      */           }  } 
/*      */       }
/*      */     } 
/*  623 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean mayLockDoor(Creature performer, Wall wall, Door door) {
/*  628 */     if (wall.isFinished())
/*      */     {
/*  630 */       if (door != null)
/*  631 */         return door.mayLock(performer); 
/*      */     }
/*  633 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Wall wall) {
/*  639 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  640 */     toReturn.addAll(super.getBehavioursFor(performer, wall));
/*      */     
/*  642 */     VolaTile wallTile = Zones.getOrCreateTile(wall.getTileX(), wall.getTileY(), (wall.getLayer() >= 0));
/*  643 */     Structure structure = Structures.getStructureOrNull(wall.getStructureId());
/*  644 */     if (structure == null) {
/*      */       
/*  646 */       toReturn.addAll(Actions.getDefaultItemActions());
/*  647 */       return toReturn;
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
/*  659 */     if (canRemoveWallPlan(performer, wall))
/*      */     {
/*      */       
/*  662 */       toReturn.add(Actions.actionEntrys[57]);
/*      */     }
/*      */     
/*  665 */     if (structure.isFinalized() && !wall.isFinished())
/*  666 */       toReturn.add(Actions.actionEntrys[607]); 
/*  667 */     toReturn.addAll(addManage(performer, structure, wall));
/*      */     
/*  669 */     if (wall.isFinished())
/*      */     {
/*  671 */       if (MethodsStructure.mayModifyStructure(performer, structure, wallTile, (short)683))
/*  672 */         toReturn.add(Actions.actionEntrys[683]); 
/*      */     }
/*  674 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getConstructionMessage(Wall wall) {
/*      */     String genus, type;
/*  682 */     switch (wall.getType()) {
/*      */       
/*      */       case SOLID:
/*  685 */         genus = "a";
/*  686 */         type = "wall";
/*      */         break;
/*      */       case WINDOW:
/*  689 */         genus = "a";
/*  690 */         type = "window";
/*      */         break;
/*      */       case WIDE_WINDOW:
/*  693 */         genus = "a";
/*  694 */         type = "wide window";
/*      */         break;
/*      */       case DOOR:
/*      */       case DOUBLE_DOOR:
/*      */       case CANOPY_DOOR:
/*  699 */         genus = "a";
/*  700 */         type = "door";
/*      */         break;
/*      */       case ARCHED:
/*      */       case ARCHED_LEFT:
/*      */       case ARCHED_RIGHT:
/*      */       case ARCHED_T:
/*  706 */         genus = "an";
/*  707 */         type = "arched wall";
/*      */         break;
/*      */       case NARROW_WINDOW:
/*  710 */         genus = "a";
/*  711 */         type = "narrow window";
/*      */         break;
/*      */       case PORTCULLIS:
/*  714 */         genus = "a";
/*  715 */         type = "portcullis";
/*      */         break;
/*      */       case BARRED:
/*  718 */         genus = "a";
/*  719 */         type = "barred wall";
/*      */         break;
/*      */       case JETTY:
/*  722 */         genus = "a";
/*  723 */         type = "jetty wall";
/*      */         break;
/*      */       case BALCONY:
/*  726 */         genus = "a";
/*  727 */         type = "balcony";
/*      */         break;
/*      */       case ORIEL:
/*  730 */         genus = "an";
/*  731 */         type = "oriel wall";
/*      */         break;
/*      */       default:
/*  734 */         genus = "a";
/*  735 */         type = "wall";
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  740 */     String msg = StringUtil.format("You see %s %s under construction. The %s needs ", new Object[] { genus, type, type });
/*  741 */     int[] neededMats = WallEnum.getMaterialsNeeded(wall);
/*  742 */     String part2 = "";
/*  743 */     for (int i = 0; i < neededMats.length; i += 2) {
/*      */ 
/*      */       
/*      */       try {
/*  747 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(neededMats[i]);
/*  748 */         int count = neededMats[i + 1];
/*  749 */         if (part2.length() > 0)
/*  750 */           part2 = part2 + ", "; 
/*  751 */         part2 = part2 + count + " " + template.sizeString + template.getPlural();
/*      */       }
/*  753 */       catch (NoSuchTemplateException e) {
/*      */         
/*  755 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/*      */     } 
/*      */     
/*  759 */     if (part2.length() > 0) {
/*  760 */       part2 = part2 + ".";
/*      */     }
/*  762 */     return msg + part2;
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendQlString(Communicator comm, Wall wall) {
/*  767 */     comm.sendNormalServerMessage("QL=" + wall.getQualityLevel() + ", dam=" + wall.getDamage());
/*  768 */     if (comm.player != null && comm.player.getPower() > 0) {
/*      */       
/*      */       try {
/*      */         
/*  772 */         Structure struct = Structures.getStructure(wall.getStructureId());
/*  773 */         String ownerName = "unknown";
/*  774 */         PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(struct.getOwnerId());
/*  775 */         if (info != null)
/*  776 */           ownerName = info.getName(); 
/*  777 */         comm.sendNormalServerMessage("Structure=" + wall.getStructureId() + ", owner=" + ownerName + " (" + struct
/*  778 */             .getOwnerId() + ")");
/*  779 */         comm.sendNormalServerMessage("Structure finished=" + struct.isFinished());
/*  780 */         if (comm.player.getPower() >= 5)
/*      */         {
/*  782 */           comm.sendNormalServerMessage("wall= " + wall.getNumber() + " start[" + wall.getStartX() + "," + wall
/*  783 */               .getStartY() + "] end=[" + wall.getEndX() + "," + wall.getEndY() + "] state=" + wall
/*  784 */               .getState() + " color=" + wall.getColor() + " material=" + wall.getMaterial() + " type=" + wall
/*  785 */               .getType() + " cover=" + wall.getCover() + " walltile=[" + (wall.getTile()).tilex + "," + 
/*  786 */               (wall.getTile()).tiley + "] finished=" + wall.getTile().getStructure().isFinished() + " isIndoor=" + wall
/*  787 */               .isIndoor() + " height=" + wall.getHeight() + " layer=" + wall.getLayer() + ")");
/*      */         }
/*      */       }
/*  790 */       catch (NoSuchStructureException noSuchStructureException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Wall wall, short action, float counter) {
/*  799 */     boolean done = true;
/*  800 */     Structure structure = Structures.getStructureOrNull(wall.getStructureId());
/*  801 */     Door door = wall.getDoor();
/*  802 */     if (action == 1) {
/*      */       
/*  804 */       Communicator comm = performer.getCommunicator();
/*  805 */       StructureTypeEnum type = wall.getType();
/*  806 */       comm.sendNormalServerMessage("Material " + wall.getMaterialString());
/*  807 */       if (type == StructureTypeEnum.DOOR || type == StructureTypeEnum.DOUBLE_DOOR || type == StructureTypeEnum.PORTCULLIS || type == StructureTypeEnum.CANOPY_DOOR) {
/*      */ 
/*      */         
/*  810 */         if (!wall.isFinished()) {
/*      */           
/*  812 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  834 */           sendQlString(comm, wall);
/*  835 */           return true;
/*      */         } 
/*  837 */         if (structure != null) {
/*      */           
/*  839 */           Floor[] floors = structure.getFloors();
/*  840 */           for (Floor floor : floors) {
/*      */             
/*  842 */             if (floor.getFloorLevel() == 0)
/*      */             {
/*  844 */               if (performer.getPower() > 0)
/*  845 */                 comm.sendNormalServerMessage("State=" + floor.getState() + "  x=" + floor
/*  846 */                     .getTileX() + ", " + floor.getTileY() + " finished=" + floor.isFinished()); 
/*      */             }
/*      */           } 
/*  849 */           Door[] doors = structure.getAllDoors();
/*  850 */           for (int x = 0; x < doors.length; x++) {
/*      */ 
/*      */             
/*      */             try {
/*  854 */               if (doors[x].getWall() == wall) {
/*      */                 
/*  856 */                 if (performer.getPower() > 0) {
/*  857 */                   comm.sendNormalServerMessage("State=" + wall.getState() + " inner x=" + doors[x]
/*  858 */                       .getInnerTile().getTileX() + ", " + doors[x].getInnerTile().getTileY() + ", outer: " + doors[x]
/*  859 */                       .getOuterTile().getTileX() + ", y=" + doors[x]
/*  860 */                       .getOuterTile().getTileY());
/*      */                 }
/*      */                 
/*      */                 try {
/*  864 */                   Item lock = doors[x].getLock();
/*  865 */                   String lockStrength = lock.getLockStrength();
/*  866 */                   comm.sendNormalServerMessage("You see a door with a lock. The lock is of " + lockStrength + " quality.");
/*      */                   
/*  868 */                   if (performer.getPower() >= 5)
/*  869 */                     comm.sendNormalServerMessage("Lock WurmId=" + lock.getWurmId() + ", dam=" + lock
/*  870 */                         .getDamage()); 
/*  871 */                   if (wall.getColor() != -1)
/*  872 */                     comm.sendNormalServerMessage("Colors: R=" + WurmColor.getColorRed(wall.getColor()) + ", G=" + 
/*  873 */                         WurmColor.getColorGreen(wall.getColor()) + ", B=" + 
/*  874 */                         WurmColor.getColorBlue(wall.getColor()) + "."); 
/*  875 */                   if (doors[x].getLockCounter() > 0) {
/*  876 */                     comm.sendNormalServerMessage("The door is picked open and will shut in " + doors[x]
/*  877 */                         .getLockCounterTime());
/*  878 */                   } else if (lock.isLocked()) {
/*  879 */                     comm.sendNormalServerMessage("It is locked.");
/*      */                   } else {
/*  881 */                     comm.sendNormalServerMessage("It is unlocked.");
/*  882 */                   }  sendQlString(comm, wall);
/*  883 */                   return true;
/*      */                 }
/*  885 */                 catch (NoSuchLockException nsl) {
/*      */                   
/*  887 */                   comm.sendNormalServerMessage("You see a door. The door has no lock.");
/*  888 */                   if (wall.getColor() != -1)
/*  889 */                     comm.sendNormalServerMessage("Colors: R=" + WurmColor.getColorRed(wall.getColor()) + ", G=" + 
/*  890 */                         WurmColor.getColorGreen(wall.getColor()) + ", B=" + 
/*  891 */                         WurmColor.getColorBlue(wall.getColor()) + "."); 
/*  892 */                   if (doors[x].getLockCounter() > 0)
/*  893 */                     comm.sendNormalServerMessage("The door is picked open and will shut in " + (doors[x]
/*  894 */                         .getLockCounter() / 2) + " seconds."); 
/*  895 */                   sendQlString(comm, wall);
/*  896 */                   return true;
/*      */                 }
/*      */               
/*      */               } 
/*  900 */             } catch (NoSuchWallException nsw) {
/*      */               
/*  902 */               logger.log(Level.WARNING, "No inner wall");
/*      */             }
/*      */           
/*      */           } 
/*      */         } else {
/*      */           
/*  908 */           logger.log(Level.WARNING, "This wall has no structure: " + wall.getId());
/*  909 */           comm.sendNormalServerMessage("This wall has a problem with its data. Please report this.");
/*      */         }
/*      */       
/*      */       }
/*  913 */       else if (type == StructureTypeEnum.NARROW_WINDOW) {
/*      */         
/*  915 */         if (wall.isFinished())
/*      */         {
/*  917 */           comm.sendNormalServerMessage("You see a narrow window.");
/*  918 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/*  922 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/*  923 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/*  926 */       } else if (Wall.isArched(type)) {
/*      */         
/*  928 */         if (wall.isFinished())
/*      */         {
/*  930 */           comm.sendNormalServerMessage("You see an arched wall opening.");
/*  931 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/*  935 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/*  936 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/*  939 */       } else if (type == StructureTypeEnum.SOLID) {
/*      */         
/*  941 */         if (wall.isFinished())
/*      */         {
/*  943 */           comm.sendNormalServerMessage("You see a wall.");
/*  944 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/*  948 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/*  949 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/*  952 */       } else if (type == StructureTypeEnum.BARRED) {
/*      */         
/*  954 */         if (wall.isFinished())
/*      */         {
/*  956 */           comm.sendNormalServerMessage("You see a barred wall.");
/*  957 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/*  961 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/*  962 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/*  965 */       } else if (type == StructureTypeEnum.BALCONY) {
/*      */         
/*  967 */         if (wall.isFinished())
/*      */         {
/*  969 */           comm.sendNormalServerMessage("You see a balcony.");
/*  970 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/*  974 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/*  975 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/*  978 */       } else if (type == StructureTypeEnum.ORIEL) {
/*      */         
/*  980 */         if (wall.isFinished())
/*      */         {
/*  982 */           comm.sendNormalServerMessage("You see an oriel wall.");
/*  983 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/*  987 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/*  988 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/*  991 */       } else if (type == StructureTypeEnum.JETTY) {
/*      */         
/*  993 */         if (wall.isFinished())
/*      */         {
/*  995 */           comm.sendNormalServerMessage("You see a jetty wall.");
/*  996 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/* 1000 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/* 1001 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/* 1004 */       } else if (type == StructureTypeEnum.WINDOW) {
/*      */         
/* 1006 */         if (wall.isFinished())
/*      */         {
/* 1008 */           comm.sendNormalServerMessage("You see a window.");
/* 1009 */           sendQlString(comm, wall);
/*      */         }
/*      */         else
/*      */         {
/* 1013 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/* 1014 */           sendQlString(comm, wall);
/*      */         }
/*      */       
/* 1017 */       } else if (type == StructureTypeEnum.WIDE_WINDOW) {
/*      */         
/* 1019 */         if (wall.isFinished()) {
/*      */           
/* 1021 */           comm.sendNormalServerMessage("You see a wide window");
/* 1022 */           sendQlString(comm, wall);
/*      */         }
/*      */         else {
/*      */           
/* 1026 */           comm.sendNormalServerMessage(getConstructionMessage(wall));
/* 1027 */           sendQlString(comm, wall);
/*      */         } 
/*      */       } else {
/*      */         
/* 1031 */         comm.sendNormalServerMessage("You see some markers for a new structure.");
/* 1032 */       }  if (wall.getColor() != -1)
/* 1033 */         comm.sendNormalServerMessage("Colors: R=" + WurmColor.getColorRed(wall.getColor()) + ", G=" + 
/* 1034 */             WurmColor.getColorGreen(wall.getColor()) + ", B=" + WurmColor.getColorBlue(wall.getColor()) + "."); 
/* 1035 */       if (performer.getPower() >= 2) {
/*      */         
/* 1037 */         if (structure != null)
/*      */         {
/* 1039 */           comm.sendNormalServerMessage("State=" + wall.getState() + ", wall id=" + wall.getId() + ", structure id=" + wall
/* 1040 */               .getStructureId() + " writid=" + structure.getWritId());
/* 1041 */           comm.sendNormalServerMessage("Planned by " + structure.getPlanner() + ".");
/*      */         }
/*      */         else
/*      */         {
/* 1045 */           comm.sendNormalServerMessage("No Such structure " + wall.getStructureId());
/*      */         }
/*      */       
/* 1048 */       } else if (performer.getPower() > 1) {
/*      */         
/* 1050 */         comm.sendNormalServerMessage("State=" + wall.getState());
/*      */       }
/*      */     
/* 1053 */     } else if (action == 607) {
/*      */       
/* 1055 */       performer.getCommunicator().sendAddWallToCreationWindow(wall, -10L);
/*      */     }
/* 1057 */     else if (action == 57) {
/*      */       
/* 1059 */       if (canRemoveWallPlan(performer, wall))
/*      */       {
/* 1061 */         wall.destroy();
/* 1062 */         performer.getCommunicator().sendNormalServerMessage("You remove a plan for a new wall.");
/* 1063 */         Server.getInstance().broadCastAction(performer.getName() + " removes a plan for a new wall.", performer, 3);
/*      */       }
/*      */       else
/*      */       {
/* 1067 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to do that!");
/*      */       }
/*      */     
/* 1070 */     } else if (action == 82) {
/*      */ 
/*      */       
/* 1073 */       DemolishCheckQuestion dcq = new DemolishCheckQuestion(performer, "Demolish Building", "A word of warning!", wall.getStructureId());
/* 1074 */       dcq.sendQuestion();
/*      */     } else {
/* 1076 */       if (action == 683 && !wall.isNotTurnable())
/*      */       {
/* 1078 */         return rotateWall(performer, wall, act, counter);
/*      */       }
/* 1080 */       if (action == 209) {
/*      */         
/* 1082 */         if (performer.getCitizenVillage() != null) {
/*      */           
/* 1084 */           if (wall.getTile() != null)
/*      */           {
/* 1086 */             if (wall.getTile().getVillage() != null)
/*      */             {
/* 1088 */               if (performer.getCitizenVillage().mayDeclareWarOn(wall.getTile().getVillage())) {
/*      */                 
/* 1090 */                 Methods.sendWarDeclarationQuestion(performer, wall
/* 1091 */                     .getTile().getVillage());
/*      */               } else {
/*      */                 
/* 1094 */                 performer.getCommunicator().sendAlertServerMessage(wall
/* 1095 */                     .getTile().getVillage().getName() + " is already at war with your village.");
/*      */               } 
/*      */             }
/*      */           }
/*      */         } else {
/* 1100 */           performer.getCommunicator().sendAlertServerMessage("You are no longer a citizen of a village.");
/*      */         } 
/* 1102 */       } else if (action == 664) {
/*      */         
/* 1104 */         manageBuilding(performer, structure, wall);
/*      */       }
/* 1106 */       else if (action == 666) {
/*      */         
/* 1108 */         manageDoor(performer, structure, wall);
/*      */       }
/* 1110 */       else if (action == 673) {
/*      */         
/* 1112 */         manageAllDoors(performer, structure, wall);
/*      */       }
/* 1114 */       else if (action == 691) {
/*      */         
/* 1116 */         historyBuilding(performer, structure, wall);
/*      */       }
/* 1118 */       else if (action == 692) {
/*      */         
/* 1120 */         historyDoor(performer, wall, door);
/*      */       }
/* 1122 */       else if (action == 102 && mayLockDoor(performer, wall, door)) {
/*      */         
/* 1124 */         if (door != null && door.hasLock() && door.isLocked() && !door.isNotLockable())
/*      */         {
/* 1126 */           door.unlock(true);
/* 1127 */           PermissionsHistories.addHistoryEntry(door.getWurmId(), System.currentTimeMillis(), performer
/* 1128 */               .getWurmId(), performer.getName(), "Unlocked door");
/*      */         }
/*      */       
/* 1131 */       } else if (action == 28 && mayLockDoor(performer, wall, door)) {
/*      */         
/* 1133 */         if (door != null && door.hasLock() && !door.isLocked() && !door.isNotLockable()) {
/*      */           
/* 1135 */           door.lock(true);
/* 1136 */           PermissionsHistories.addHistoryEntry(door.getWurmId(), System.currentTimeMillis(), performer
/* 1137 */               .getWurmId(), performer.getName(), "Locked door");
/*      */         } 
/*      */       } 
/* 1140 */     }  return true;
/*      */   }
/*      */ 
/*      */   
/*      */   void manageBuilding(Creature performer, Structure structure, Wall wall) {
/* 1145 */     if (structure != null) {
/*      */       
/* 1147 */       if (structure.getWritId() != -10L && structure.isActualOwner(performer.getWurmId())) {
/*      */         
/* 1149 */         Items.destroyItem(structure.getWritId());
/* 1150 */         structure.setWritid(-10L, true);
/*      */       } 
/* 1152 */       if (structure.mayShowPermissions(performer) || structure.isActualOwner(performer.getWurmId())) {
/*      */         
/* 1154 */         ManagePermissions mp = new ManagePermissions(performer, ManageObjectList.Type.BUILDING, (PermissionsPlayerList.ISettings)structure, false, -10L, false, null, "");
/*      */         
/* 1156 */         mp.sendQuestion();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void manageDoor(Creature performer, Structure structure, Wall wall) {
/* 1163 */     if (wall.isFinished()) {
/*      */       
/* 1165 */       Door door = wall.getDoor();
/* 1166 */       if (door != null && (door.mayShowPermissions(performer) || structure
/* 1167 */         .mayManage(performer) || performer.getPower() > 1)) {
/*      */         
/* 1169 */         ManagePermissions mp = new ManagePermissions(performer, ManageObjectList.Type.DOOR, (PermissionsPlayerList.ISettings)door, false, -10L, false, null, "");
/*      */         
/* 1171 */         mp.sendQuestion();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void manageAllDoors(Creature performer, Structure structure, Wall wall) {
/* 1178 */     if (structure != null && structure.mayManage(performer)) {
/*      */ 
/*      */       
/* 1181 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.DOOR, wall.getStructureId(), false, 1, "", true);
/* 1182 */       mol.sendQuestion();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void historyBuilding(Creature performer, Structure structure, Wall wall) {
/* 1188 */     if (structure != null)
/*      */     {
/* 1190 */       if (structure.isOwner(performer) || performer.getPower() > 0) {
/*      */         
/* 1192 */         PermissionsHistory ph = new PermissionsHistory(performer, wall.getStructureId());
/* 1193 */         ph.sendQuestion();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void historyDoor(Creature performer, Wall wall, Door door) {
/* 1200 */     if (wall.isFinished())
/*      */     {
/* 1202 */       if (door != null) {
/*      */         
/* 1204 */         PermissionsHistory ph = new PermissionsHistory(performer, wall.getId());
/* 1205 */         ph.sendQuestion();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item source, Wall wall, short action, float counter) {
/* 1214 */     boolean done = true;
/* 1215 */     Structure structure = Structures.getStructureOrNull(wall.getStructureId());
/* 1216 */     Door door = wall.getDoor();
/* 1217 */     if (act.isBuildHouseWallAction() && performer.isFighting()) {
/*      */       
/* 1219 */       performer.getCommunicator().sendNormalServerMessage("You cannot do that while in combat.");
/* 1220 */       performer.getCommunicator().sendActionResult(false);
/* 1221 */       return true;
/*      */     } 
/* 1223 */     if (action == 1) {
/*      */       
/* 1225 */       done = action(act, performer, wall, action, counter);
/*      */     } else {
/* 1227 */       if (action == 647)
/*      */       {
/* 1229 */         return modifyWall(performer, source, wall, act, counter);
/*      */       }
/* 1231 */       if (action == 683)
/*      */       {
/* 1233 */         return action(act, performer, wall, action, counter);
/*      */       }
/* 1235 */       if (action == 847 && wall.isPlainStone() && (source
/* 1236 */         .getTemplateId() == 130 || (source.isWand() && performer.getPower() >= 4)))
/*      */       {
/* 1238 */         return toggleRenderWall(performer, source, wall, act, counter);
/*      */       }
/* 1240 */       if (action == 847 && wall.isPlastered() && (source
/* 1241 */         .getTemplateId() == 1115 || (source.isWand() && performer.getPower() >= 4)))
/*      */       {
/* 1243 */         return toggleRenderWall(performer, source, wall, act, counter);
/*      */       }
/* 1245 */       if (action == 848 && wall.isLRArch() && (source
/* 1246 */         .getTemplateId() == 1115 || (source.isWand() && performer.getPower() >= 4)))
/*      */       {
/* 1248 */         return toggleLeftRightArch(performer, source, wall, act, counter);
/*      */       }
/* 1250 */       if (act.isBuildHouseWallAction()) {
/*      */ 
/*      */ 
/*      */         
/* 1254 */         WallEnum targetWallType = WallEnum.getWallByActionId(action);
/* 1255 */         done = false;
/* 1256 */         String buildString = "wall";
/* 1257 */         if (act.isBuildWindowAction()) {
/* 1258 */           buildString = "window";
/* 1259 */         } else if (act.isBuildDoorAction()) {
/* 1260 */           buildString = "door";
/* 1261 */         } else if (act.isBuildDoubleDoorAction()) {
/* 1262 */           buildString = "double door";
/* 1263 */         } else if (act.isBuildArchedWallAction()) {
/* 1264 */           buildString = "arched wall";
/* 1265 */         } else if (act.isBuildPortcullisAction()) {
/* 1266 */           buildString = "portcullis";
/* 1267 */         } else if (act.isBuildBarredWall()) {
/* 1268 */           buildString = "barred wall";
/* 1269 */         } else if (act.isBuildBalcony()) {
/* 1270 */           buildString = "balcony";
/* 1271 */         } else if (act.isBuildJetty()) {
/* 1272 */           buildString = "jetty";
/* 1273 */         } else if (act.isBuildOriel()) {
/* 1274 */           buildString = "oriel";
/* 1275 */         } else if (act.isBuildCanopyDoor()) {
/* 1276 */           buildString = "canopy";
/*      */         } 
/* 1278 */         int tilex = wall.getStartX();
/* 1279 */         int tiley = wall.getStartY();
/* 1280 */         VolaTile wallTile = null;
/* 1281 */         Wall orig = null;
/* 1282 */         boolean usesRightItem = false;
/* 1283 */         if (MethodsStructure.isCorrectToolForBuilding(performer, source.getTemplateId()) && targetWallType
/* 1284 */           .isCorrectToolForType(source, performer))
/*      */         {
/* 1286 */           usesRightItem = true;
/*      */         }
/*      */         
/* 1289 */         if (!usesRightItem) {
/*      */           
/* 1291 */           performer.getCommunicator().sendNormalServerMessage("You can't use that.");
/* 1292 */           performer.getCommunicator().sendActionResult(false);
/* 1293 */           return true;
/*      */         } 
/* 1295 */         if (structure != null) {
/*      */           
/* 1297 */           boolean hasMarker = StructureBehaviour.hasMarker(tilex, tiley, wall.isOnSurface(), wall.getDir(), wall.getHeight());
/* 1298 */           if (hasMarker && targetWallType
/* 1299 */             .getType() != StructureTypeEnum.ARCHED_LEFT && targetWallType
/* 1300 */             .getType() != StructureTypeEnum.ARCHED_RIGHT && targetWallType
/* 1301 */             .getType() != StructureTypeEnum.ARCHED_T) {
/*      */             
/* 1303 */             performer.getCommunicator().sendNormalServerMessage("You can't build those over a highway.");
/* 1304 */             performer.getCommunicator().sendActionResult(false);
/* 1305 */             return true;
/*      */           } 
/* 1307 */           if (wall.getType() == StructureTypeEnum.PLAN && structure.needsDoor())
/*      */           {
/* 1309 */             if (!act.isBuildDoorAction() && !act.isBuildDoubleDoorAction() && 
/* 1310 */               !act.isBuildArchedWallAction() && !act.isBuildPortcullisAction() && !act.isBuildCanopyDoor())
/*      */             {
/* 1312 */               performer.getCommunicator().sendNormalServerMessage("Houses need at least one door. Build a door first.");
/*      */               
/* 1314 */               performer.getCommunicator().sendActionResult(false);
/* 1315 */               return true;
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
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1337 */           logger.log(Level.WARNING, "Structure with id " + wall.getStructureId() + " not found!");
/* 1338 */           performer.getCommunicator().sendActionResult(false);
/* 1339 */           return done;
/*      */         } 
/* 1341 */         for (int xx = 1; xx >= -1; xx--) {
/*      */           
/* 1343 */           for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */             
/*      */             try {
/* 1347 */               Zone zone = Zones.getZone(tilex + xx, tiley + yy, performer.isOnSurface());
/* 1348 */               VolaTile tile = zone.getTileOrNull(tilex + xx, tiley + yy);
/* 1349 */               if (tile != null) {
/*      */                 
/* 1351 */                 Wall[] walls = tile.getWalls();
/* 1352 */                 for (int s = 0; s < walls.length; s++) {
/*      */                   
/* 1354 */                   if (walls[s].getId() == wall.getId()) {
/*      */                     
/* 1356 */                     wallTile = tile;
/* 1357 */                     orig = walls[s];
/* 1358 */                     if (wallTile.getStructure() != null && !wallTile.getStructure().isFinalized()) {
/*      */                       
/* 1360 */                       performer.getCommunicator().sendNormalServerMessage("You need to finalize the build plan before you start building.");
/*      */                       
/* 1362 */                       performer.getCommunicator().sendActionResult(false);
/* 1363 */                       return done;
/*      */                     } 
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*      */               } 
/* 1370 */             } catch (NoSuchZoneException noSuchZoneException) {}
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1376 */         if (orig == null) {
/*      */           
/* 1378 */           performer.getCommunicator().sendNormalServerMessage("No structure is planned there at the moment.");
/* 1379 */           performer.getCommunicator().sendActionResult(false);
/* 1380 */           return true;
/*      */         } 
/* 1382 */         if (orig.isFinished()) {
/*      */           
/* 1384 */           performer.getCommunicator().sendNormalServerMessage("You need to destroy the " + orig
/* 1385 */               .getName() + " before modifying it.");
/* 1386 */           performer.getCommunicator().sendActionResult(false);
/* 1387 */           return true;
/*      */         } 
/* 1389 */         if (!MethodsStructure.mayModifyStructure(performer, structure, wallTile, action)) {
/*      */           
/* 1391 */           performer.getCommunicator().sendNormalServerMessage("You need permission in order to make modifications to this structure.");
/*      */           
/* 1393 */           performer.getCommunicator().sendActionResult(false);
/* 1394 */           return true;
/*      */         } 
/* 1396 */         StructureMaterialEnum material = targetWallType.getMaterial();
/* 1397 */         StructureTypeEnum actionType = targetWallType.getType();
/* 1398 */         int primskillTemplate = targetWallType.getSkillNumber();
/*      */         
/* 1400 */         if (StructureStateEnum.INITIALIZED != wall.getState() && StructureStateEnum.FINISHED != wall.getState()) {
/*      */           
/* 1402 */           if (material != wall.getMaterial())
/*      */           {
/* 1404 */             if (source.getTemplateId() == 176 && WurmPermissions.mayUseGMWand(performer)) {
/*      */               
/* 1406 */               material = wall.getMaterial();
/* 1407 */               performer.getCommunicator().sendNormalServerMessage("You use the power of your " + source
/* 1408 */                   .getName() + " to change the material of the wall!");
/*      */             }
/*      */             else {
/*      */               
/* 1412 */               performer.getCommunicator().sendNormalServerMessage("You may not change the material of the wall now that you are building it.");
/*      */               
/* 1414 */               performer.getCommunicator().sendActionResult(false);
/* 1415 */               return true;
/*      */             } 
/*      */           }
/*      */           
/* 1419 */           if (wall.getType() != actionType)
/*      */           {
/* 1421 */             if (source.getTemplateId() == 176 && WurmPermissions.mayUseGMWand(performer))
/*      */             {
/* 1423 */               wall.setType(actionType);
/* 1424 */               performer.getCommunicator().sendNormalServerMessage("You use the power of your " + source
/* 1425 */                   .getName() + " to change the structure of the wall!");
/*      */             }
/*      */             else
/*      */             {
/* 1429 */               performer.getCommunicator().sendNormalServerMessage("You may not change the type of wall now that you are building it.");
/*      */               
/* 1431 */               performer.getCommunicator().sendActionResult(false);
/* 1432 */               return true;
/*      */             }
/*      */           
/*      */           }
/* 1436 */         } else if (StructureStateEnum.INITIALIZED == wall.getState()) {
/*      */           
/* 1438 */           wall.setMaterial(material);
/*      */         } 
/* 1440 */         Skill carpentry = null;
/* 1441 */         Skill hammer = null;
/*      */ 
/*      */         
/*      */         try {
/* 1445 */           carpentry = performer.getSkills().getSkill(primskillTemplate);
/* 1446 */           if (primskillTemplate == 1013)
/*      */           {
/* 1448 */             if (carpentry.getKnowledge(0.0D) < 30.0D)
/*      */             {
/* 1450 */               performer.getCommunicator().sendNormalServerMessage("You need at least 30 masonry to build stone house walls.");
/*      */               
/* 1452 */               performer.getCommunicator().sendActionResult(false);
/* 1453 */               return true;
/*      */             }
/*      */           
/*      */           }
/* 1457 */         } catch (NoSuchSkillException nss) {
/*      */           
/* 1459 */           if (primskillTemplate == 1013) {
/*      */             
/* 1461 */             performer.getCommunicator().sendNormalServerMessage("You need at least 30 masonry to build stone house walls.");
/*      */             
/* 1463 */             performer.getCommunicator().sendActionResult(false);
/* 1464 */             return true;
/*      */           } 
/* 1466 */           carpentry = performer.getSkills().learn(primskillTemplate, 1.0F);
/*      */         } 
/* 1468 */         if (FloorBehaviour.getRequiredBuildSkillForFloorLevel(wall.getFloorLevel(), false) > carpentry.getKnowledge(0.0D)) {
/*      */           
/* 1470 */           performer.getCommunicator().sendNormalServerMessage("Construction of walls is reserved for craftsmen of higher rank than yours.");
/*      */           
/* 1472 */           if (Servers.localServer.testServer) {
/* 1473 */             performer.getCommunicator().sendNormalServerMessage("You have " + carpentry
/* 1474 */                 .getKnowledge(0.0D) + " and need " + 
/* 1475 */                 FloorBehaviour.getRequiredBuildSkillForFloorLevel(wall.getFloorLevel(), false));
/*      */           }
/* 1477 */           performer.getCommunicator().sendActionResult(false);
/* 1478 */           return true;
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/* 1483 */           hammer = performer.getSkills().getSkill(source.getPrimarySkill());
/*      */         }
/* 1485 */         catch (NoSuchSkillException nss) {
/*      */ 
/*      */           
/*      */           try {
/* 1489 */             hammer = performer.getSkills().learn(source.getPrimarySkill(), 1.0F);
/*      */           }
/* 1491 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1496 */         int time = 10;
/*      */         
/* 1498 */         double bonus = 0.0D;
/* 1499 */         StructureTypeEnum oldType = orig.getType();
/*      */ 
/*      */         
/* 1502 */         boolean immediate = ((source.getTemplateId() == 176 && WurmPermissions.mayUseGMWand(performer)) || (source.getTemplateId() == 315 && performer.getPower() >= 2 && Servers.isThisATestServer()));
/* 1503 */         if (oldType == actionType)
/*      */         {
/* 1505 */           if (orig.isFinished()) {
/*      */             
/* 1507 */             performer.getCommunicator().sendNormalServerMessage("The wall is finished already.");
/* 1508 */             performer.getCommunicator().sendActionResult(false);
/* 1509 */             return true;
/*      */           } 
/*      */         }
/* 1512 */         if (counter == 1.0F && !immediate) {
/*      */           
/* 1514 */           time = Actions.getSlowActionTime(performer, carpentry, source, 0.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1522 */           if (checkWallItem2(performer, wall, buildString, time, act)) {
/*      */             
/* 1524 */             performer.getCommunicator().sendActionResult(false);
/* 1525 */             return true;
/*      */           } 
/*      */ 
/*      */           
/* 1529 */           act.setTimeLeft(time);
/* 1530 */           if (oldType == actionType) {
/*      */             
/* 1532 */             performer.getCommunicator().sendNormalServerMessage("You continue to build a " + buildString + ".");
/* 1533 */             Server.getInstance().broadCastAction(performer
/* 1534 */                 .getName() + " continues to build a " + buildString + ".", performer, 5);
/*      */           } 
/*      */           
/* 1537 */           performer.sendActionControl("Building " + buildString, true, time);
/*      */           
/* 1539 */           performer.getStatus().modifyStamina(-1000.0F);
/* 1540 */           if (source.getTemplateId() == 63) {
/* 1541 */             source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 1542 */           } else if (source.getTemplateId() == 62) {
/* 1543 */             source.setDamage(source.getDamage() + 3.0E-4F * source.getDamageModifier());
/* 1544 */           } else if (source.getTemplateId() == 493) {
/* 1545 */             source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */           } 
/*      */         } else {
/*      */           
/* 1549 */           time = act.getTimeLeft();
/* 1550 */           if (Math.abs(performer.getPosX() - (wall.getEndX() << 2)) > 8.0F || Math.abs(performer.getPosX() - (wall.getStartX() << 2)) > 8.0F || 
/* 1551 */             Math.abs(performer.getPosY() - (wall.getEndY() << 2)) > 8.0F || Math.abs(performer.getPosY() - (wall.getStartY() << 2)) > 8.0F) {
/*      */             
/* 1553 */             performer.getCommunicator().sendAlertServerMessage("You are too far from the end.");
/* 1554 */             performer.getCommunicator().sendActionResult(false);
/* 1555 */             return true;
/*      */           } 
/* 1557 */           if (act.currentSecond() % 5 == 0) {
/*      */ 
/*      */             
/* 1560 */             if (wall.isStone() || wall.isPlainStone() || wall.isSlate() || wall.isRoundedStone() || wall
/* 1561 */               .isPottery() || wall.isSandstone() || wall.isMarble()) {
/* 1562 */               SoundPlayer.playSound("sound.work.masonry", tilex, tiley, performer.isOnSurface(), 1.6F);
/*      */             } else {
/* 1564 */               SoundPlayer.playSound((Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2", tilex, tiley, performer
/* 1565 */                   .isOnSurface(), 1.6F);
/* 1566 */             }  performer.getStatus().modifyStamina(-10000.0F);
/* 1567 */             if (source.getTemplateId() == 63) {
/* 1568 */               source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 1569 */             } else if (source.getTemplateId() == 62) {
/* 1570 */               source.setDamage(source.getDamage() + 3.0E-4F * source.getDamageModifier());
/* 1571 */             } else if (source.getTemplateId() == 493) {
/* 1572 */               source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */             } 
/*      */           } 
/* 1575 */         }  if (counter * 10.0F > time || immediate) {
/*      */           
/* 1577 */           if (!immediate && !depleteWallItems2(performer, wall, act)) {
/*      */             
/* 1579 */             performer.getCommunicator().sendActionResult(false);
/* 1580 */             return true;
/*      */           } 
/* 1582 */           if (hammer != null) {
/*      */             
/* 1584 */             hammer.skillCheck(10.0D, source, 0.0D, false, counter);
/* 1585 */             bonus = hammer.getKnowledge(source, 0.0D) / 10.0D;
/*      */           } 
/* 1587 */           carpentry.skillCheck(10.0D, source, bonus, false, counter);
/* 1588 */           done = true;
/*      */ 
/*      */           
/*      */           try {
/* 1592 */             float oldql = wall.getQualityLevel();
/* 1593 */             float qlevel = MethodsStructure.calculateNewQualityLevel(act.getPower(), carpentry.getKnowledge(0.0D), oldql, 
/* 1594 */                 (wall.getFinalState()).state);
/* 1595 */             qlevel = Math.max(1.0F, qlevel);
/* 1596 */             if (immediate)
/* 1597 */               qlevel = 50.0F; 
/* 1598 */             boolean updateOrig = false;
/*      */             
/* 1600 */             if (oldType != actionType) {
/*      */               
/* 1602 */               orig.setType(actionType);
/* 1603 */               orig.setDamage(0.0F);
/* 1604 */               qlevel = MethodsStructure.calculateNewQualityLevel(act.getPower(), carpentry.getKnowledge(0.0D), 0.0F, 
/* 1605 */                   (wall.getFinalState()).state);
/* 1606 */               orig.setState(StructureStateEnum.INITIALIZED);
/* 1607 */               updateOrig = true;
/*      */             } 
/* 1609 */             StructureStateEnum oldState = orig.getState();
/* 1610 */             StructureStateEnum state = oldState;
/* 1611 */             if (state.state < Byte.MAX_VALUE) {
/*      */               
/* 1613 */               state = StructureStateEnum.getStateByValue((byte)(state.state + 1));
/* 1614 */               if (WurmPermissions.mayUseGMWand(performer) && (source
/* 1615 */                 .getTemplateId() == 315 || source.getTemplateId() == 176) && 
/* 1616 */                 Servers.isThisATestServer() == true) {
/*      */                 
/* 1618 */                 state = StructureStateEnum.FINISHED;
/* 1619 */                 qlevel = 80.0F;
/*      */               }
/* 1621 */               else if (performer.getPower() >= 4 && source.getTemplateId() == 176) {
/*      */                 
/* 1623 */                 state = StructureStateEnum.FINISHED;
/* 1624 */                 qlevel = 80.0F;
/*      */               } 
/*      */             } 
/* 1627 */             orig.setState(state);
/* 1628 */             orig.setQualityLevel(qlevel);
/* 1629 */             orig.setDamage(0.0F);
/* 1630 */             orig.setMaterial(material);
/* 1631 */             if (updateOrig || orig.isFinished()) {
/*      */               
/* 1633 */               wallTile.updateWall(orig);
/* 1634 */               if (performer.getDeity() != null && (performer.getDeity()).number == 3)
/*      */               {
/* 1636 */                 performer.maybeModifyAlignment(1.0F);
/*      */               }
/* 1638 */               if (wall.isFinished() && (wall.isStone() || wall.isPlainStone()))
/* 1639 */                 performer.achievement(525); 
/* 1640 */               if (wall.isFinished() && wall.getFloorLevel() == 1)
/* 1641 */                 performer.achievement(539); 
/*      */             } 
/* 1643 */             if (orig.isFinished()) {
/* 1644 */               performer.getCommunicator().sendRemoveFromCreationWindow(orig.getId());
/*      */             } else {
/* 1646 */               performer.getCommunicator().sendAddWallToCreationWindow(wall, orig.getId());
/*      */             } 
/* 1648 */             if (wall.isHalfArch() && oldState == StructureStateEnum.INITIALIZED) {
/*      */               
/* 1650 */               String beam = (wall.isWood() || wall.isTimberFramed()) ? "a beam" : "an iron bar";
/* 1651 */               Server.getInstance().broadCastAction(performer.getName() + " add " + beam + " as reinforcement to the arch.", performer, 5);
/* 1652 */               performer.getCommunicator().sendNormalServerMessage("You add " + beam + " as reinforcement to the arch.");
/*      */             }
/* 1654 */             else if (wall.isWood()) {
/*      */               
/* 1656 */               Server.getInstance().broadCastAction(performer.getName() + " nails a plank to the wall.", performer, 5);
/* 1657 */               performer.getCommunicator().sendNormalServerMessage("You nail a plank to the wall.");
/*      */             }
/* 1659 */             else if (wall.isTimberFramed()) {
/*      */               
/* 1661 */               if (state.state < 7)
/*      */               {
/* 1663 */                 Server.getInstance().broadCastAction(performer.getName() + " affixes a beam to the frame.", performer, 5);
/* 1664 */                 performer.getCommunicator().sendNormalServerMessage("You affix a beam to the frame.");
/*      */               }
/* 1666 */               else if (state.state < 17)
/*      */               {
/* 1668 */                 Server.getInstance().broadCastAction(performer.getName() + " adds some clay and mixed grass to the wall.", performer, 5);
/* 1669 */                 performer.getCommunicator().sendNormalServerMessage("You add some clay and mixed grass to the wall.");
/*      */               }
/*      */               else
/*      */               {
/* 1673 */                 Server.getInstance().broadCastAction(performer.getName() + " reinforces the wall with more clay.", performer, 5);
/* 1674 */                 performer.getCommunicator().sendNormalServerMessage("You reinforce the wall with more clay.");
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 1680 */               String brickType = wall.getBrickName();
/* 1681 */               Server.getInstance().broadCastAction(performer
/* 1682 */                   .getName() + " adds a " + brickType + " and some mortar to the wall.", performer, 5);
/* 1683 */               performer.getCommunicator().sendNormalServerMessage("You add a " + brickType + " and some mortar to the wall.");
/*      */             } 
/* 1685 */             performer.getCommunicator().sendActionResult(true);
/*      */             
/*      */             try {
/* 1688 */               orig.save();
/*      */             }
/* 1690 */             catch (IOException iox) {
/*      */               
/* 1692 */               logger.log(Level.WARNING, "Failed to save wall with id " + orig.getId());
/*      */             } 
/* 1694 */             if ((!structure.isFinished() || !structure.isFinalFinished()) && 
/* 1695 */               structure.updateStructureFinishFlag()) {
/*      */               
/* 1697 */               performer.achievement(216);
/* 1698 */               if (!structure.isOnSurface())
/* 1699 */                 performer.achievement(571); 
/*      */             } 
/* 1701 */             if (oldType == StructureTypeEnum.DOOR || oldType == StructureTypeEnum.DOUBLE_DOOR || oldType == StructureTypeEnum.CANOPY_DOOR) {
/*      */ 
/*      */ 
/*      */               
/* 1705 */               Door[] doors = structure.getAllDoors();
/* 1706 */               for (int x = 0; x < doors.length; x++) {
/*      */                 
/* 1708 */                 if (doors[x].getWall() == wall) {
/*      */                   
/* 1710 */                   structure.removeDoor(doors[x]);
/* 1711 */                   doors[x].removeFromTiles();
/*      */                 } 
/*      */               } 
/*      */             } 
/* 1715 */             if ((act.isBuildDoorAction() || act.isBuildDoubleDoorAction() || act.isBuildPortcullisAction() || act
/* 1716 */               .isBuildCanopyDoor()) && orig.isFinished())
/*      */             {
/*      */               
/* 1719 */               DbDoor dbDoor = new DbDoor(orig);
/* 1720 */               dbDoor.setStructureId(structure.getWurmId());
/* 1721 */               structure.addDoor((Door)dbDoor);
/* 1722 */               dbDoor.setIsManaged(true, (Player)performer);
/* 1723 */               dbDoor.save();
/* 1724 */               dbDoor.addToTiles();
/*      */             }
/*      */           
/* 1727 */           } catch (Exception ex) {
/*      */             
/* 1729 */             logger.log(Level.WARNING, "Error when building wall:", ex);
/* 1730 */             performer.getCommunicator().sendNormalServerMessage("An error occured on the server when building wall. Please tell the administrators.");
/*      */             
/* 1732 */             performer.getCommunicator().sendActionResult(false);
/*      */           }
/*      */         
/*      */         } 
/* 1736 */       } else if (action == 58) {
/*      */         
/* 1738 */         int tilex = wall.getTileX();
/* 1739 */         int tiley = wall.getTileY();
/* 1740 */         MethodsStructure.tryToFinalize(performer, tilex, tiley);
/*      */       }
/* 1742 */       else if (action == 57) {
/*      */         
/* 1744 */         if (canRemoveWallPlan(performer, wall)) {
/*      */           
/* 1746 */           wall.destroy();
/* 1747 */           performer.getCommunicator().sendNormalServerMessage("You remove a plan for a new wall.");
/* 1748 */           Server.getInstance().broadCastAction(performer.getName() + " removes a plan for a new wall.", performer, 3);
/*      */         } else {
/*      */           
/* 1751 */           performer.getCommunicator().sendNormalServerMessage("This would cause a section of the structure to crash down since it lacks support.");
/*      */         }
/*      */       
/* 1754 */       } else if (action == 209) {
/*      */         
/* 1756 */         done = true;
/* 1757 */         if (performer.getCitizenVillage() != null) {
/*      */           
/* 1759 */           if (wall.getTile() != null && 
/* 1760 */             wall.getTile().getVillage() != null)
/*      */           {
/* 1762 */             if (performer.getCitizenVillage().mayDeclareWarOn(wall.getTile().getVillage())) {
/*      */               
/* 1764 */               Methods.sendWarDeclarationQuestion(performer, wall
/* 1765 */                   .getTile().getVillage());
/*      */             } else {
/*      */               
/* 1768 */               performer.getCommunicator().sendAlertServerMessage(wall
/* 1769 */                   .getTile().getVillage().getName() + " is already at war with your village.");
/*      */             } 
/*      */           }
/*      */         } else {
/* 1773 */           performer.getCommunicator().sendAlertServerMessage("You are no longer a citizen of a village.");
/*      */         } 
/* 1775 */       } else if (action == 161 && source.isLock() && source.getTemplateId() == 167) {
/*      */         
/* 1777 */         if (source.isLocked()) {
/*      */           
/* 1779 */           performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " is already in use.");
/* 1780 */           return true;
/*      */         } 
/*      */         
/* 1783 */         if (wall.getType() == StructureTypeEnum.DOOR || wall
/* 1784 */           .getType() == StructureTypeEnum.DOUBLE_DOOR || wall
/* 1785 */           .getType() == StructureTypeEnum.PORTCULLIS || wall
/* 1786 */           .getType() == StructureTypeEnum.CANOPY_DOOR) {
/*      */           
/* 1788 */           done = false;
/* 1789 */           Skill carpentry = null;
/*      */           
/*      */           try {
/* 1792 */             carpentry = performer.getSkills().getSkill(1005);
/*      */           }
/* 1794 */           catch (NoSuchSkillException nss) {
/*      */             
/* 1796 */             carpentry = performer.getSkills().learn(1005, 1.0F);
/*      */           } 
/* 1798 */           int time = 10;
/* 1799 */           if (counter == 1.0F) {
/*      */             
/* 1801 */             if (structure != null) {
/*      */               
/* 1803 */               if (!structure.mayModify(performer))
/*      */               {
/* 1805 */                 return true;
/*      */               }
/*      */             }
/*      */             else {
/*      */               
/* 1810 */               logger.log(Level.WARNING, "This wall has no structure: " + wall.getId());
/* 1811 */               performer.getCommunicator().sendNormalServerMessage("This wall has a problem with its data. Please report this.");
/*      */             } 
/*      */             
/* 1814 */             time = (int)Math.max(100.0D, (100.0D - carpentry.getKnowledge(source, 0.0D)) * 3.0D);
/*      */             
/*      */             try {
/* 1817 */               performer.getCurrentAction().setTimeLeft(time);
/*      */             }
/* 1819 */             catch (NoSuchActionException nsa) {
/*      */               
/* 1821 */               logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */             } 
/* 1823 */             performer.getCommunicator().sendNormalServerMessage("You start to attach the lock.");
/* 1824 */             Server.getInstance().broadCastAction(performer.getName() + " starts to attach a lock.", performer, 5);
/*      */             
/* 1826 */             performer.sendActionControl(Actions.actionEntrys[161].getVerbString(), true, time);
/*      */           } else {
/*      */ 
/*      */             
/*      */             try {
/*      */ 
/*      */               
/* 1833 */               time = performer.getCurrentAction().getTimeLeft();
/*      */             }
/* 1835 */             catch (NoSuchActionException nsa) {
/*      */               
/* 1837 */               logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */             } 
/*      */             
/* 1840 */             if (counter * 10.0F > time) {
/*      */               
/* 1842 */               carpentry.skillCheck((100.0F - source.getCurrentQualityLevel()), 0.0D, false, counter);
/* 1843 */               done = true;
/* 1844 */               if (structure != null) {
/*      */                 
/* 1846 */                 long parentId = source.getParentId();
/*      */                 
/* 1848 */                 if (parentId != -10L) {
/*      */                   
/*      */                   try
/*      */                   {
/* 1852 */                     Items.getItem(parentId).dropItem(source.getWurmId(), false);
/*      */                   }
/* 1854 */                   catch (NoSuchItemException nsi)
/*      */                   {
/* 1856 */                     logger.log(Level.INFO, performer.getName() + " tried to attach nonexistant lock or lock with no parent.");
/*      */                   }
/*      */                 
/*      */                 }
/*      */                 else {
/*      */                   
/* 1862 */                   logger.log(Level.INFO, performer.getName() + " tried to attach lock with no parent.");
/* 1863 */                   performer.getCommunicator().sendNormalServerMessage("You may not use that lock.");
/*      */                 } 
/*      */                 
/* 1866 */                 source.addKey(structure.getWritId());
/* 1867 */                 Door[] doors = structure.getAllDoors();
/* 1868 */                 for (int x = 0; x < doors.length; x++) {
/*      */ 
/*      */                   
/*      */                   try {
/* 1872 */                     if (doors[x].getWall() == wall) {
/*      */                       
/* 1874 */                       if (!doors[x].isNotLockable()) {
/*      */ 
/*      */                         
/*      */                         try {
/* 1878 */                           Item oldlock = doors[x].getLock();
/* 1879 */                           oldlock.removeKey(structure.getWritId());
/* 1880 */                           oldlock.unlock();
/* 1881 */                           performer.getInventory().insertItem(oldlock);
/*      */                         }
/* 1883 */                         catch (NoSuchLockException noSuchLockException) {}
/*      */ 
/*      */ 
/*      */                         
/* 1887 */                         doors[x].setLock(source.getWurmId());
/* 1888 */                         source.lock();
/* 1889 */                         PermissionsHistories.addHistoryEntry(doors[x].getWurmId(), System.currentTimeMillis(), performer
/* 1890 */                             .getWurmId(), performer.getName(), "Attached lock to door");
/* 1891 */                         Server.getInstance().broadCastAction(performer.getName() + " attaches the lock.", performer, 5);
/*      */                         
/* 1893 */                         performer.getCommunicator().sendNormalServerMessage("You attach the lock and lock the door.");
/*      */                       } 
/*      */ 
/*      */                       
/*      */                       break;
/*      */                     } 
/* 1899 */                   } catch (NoSuchWallException nsw) {
/*      */                     
/* 1901 */                     logger.log(Level.WARNING, "No inner wall");
/*      */                   }
/*      */                 
/*      */                 } 
/*      */               } else {
/*      */                 
/* 1907 */                 logger.log(Level.WARNING, "This wall has no structure: " + wall.getId());
/* 1908 */                 performer.getCommunicator().sendNormalServerMessage("This wall has a problem with its data. Please report this.");
/*      */               }
/*      */             
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/* 1915 */           performer.getCommunicator().sendNormalServerMessage("You can only attach locks to doors and fence gates.");
/*      */         } 
/* 1917 */       } else if (action == 101) {
/*      */         
/* 1919 */         if (wall.isOnPvPServer() || Servers.isThisATestServer())
/*      */         {
/* 1921 */           if (wall.getType() == StructureTypeEnum.DOOR || wall
/* 1922 */             .getType() == StructureTypeEnum.DOUBLE_DOOR || wall
/* 1923 */             .getType() == StructureTypeEnum.PORTCULLIS || wall
/* 1924 */             .getType() == StructureTypeEnum.CANOPY_DOOR)
/*      */           {
/* 1926 */             if (wall.isFinished() && !wall.isNotLockpickable())
/*      */             {
/* 1928 */               if (structure != null) {
/*      */                 
/* 1930 */                 Door[] doors = structure.getAllDoors();
/* 1931 */                 for (int x = 0; x < doors.length; x++) {
/*      */ 
/*      */                   
/*      */                   try {
/* 1935 */                     if (doors[x].getWall() == wall) {
/*      */                       
/* 1937 */                       done = false;
/* 1938 */                       done = MethodsStructure.picklock(performer, source, doors[x], wall.getName(), counter, act);
/*      */ 
/*      */                       
/*      */                       break;
/*      */                     } 
/* 1943 */                   } catch (NoSuchWallException nsw) {
/*      */                     
/* 1945 */                     logger.log(Level.WARNING, "No inner wall");
/*      */                   }
/*      */                 
/*      */                 } 
/*      */               } else {
/*      */                 
/* 1951 */                 logger.log(Level.WARNING, "This wall has no structure: " + wall.getId());
/* 1952 */                 performer.getCommunicator().sendNormalServerMessage("This wall has a problem with its data. Please report this.");
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1959 */       else if (action == 193) {
/*      */         
/* 1961 */         if ((!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0) && 
/* 1962 */           !wall.isNoRepair()) {
/* 1963 */           done = MethodsStructure.repairWall(act, performer, source, wall, counter);
/*      */         } else {
/* 1965 */           done = true;
/*      */         } 
/* 1967 */       } else if (action == 192) {
/*      */         
/* 1969 */         if (source == null || wall.isNoImprove()) {
/* 1970 */           done = true;
/*      */         } else {
/* 1972 */           done = MethodsStructure.improveWall(act, performer, source, wall, counter);
/*      */         } 
/* 1974 */       } else if (action == 180) {
/*      */         
/* 1976 */         if (performer.getPower() >= 2)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1985 */           performer.getLogger().log(Level.INFO, performer
/* 1986 */               .getName() + " destroyed a wall at " + wall.getTileX() + ", " + wall.getTileY());
/* 1987 */           wall.setDamage(100.0F);
/* 1988 */           done = true;
/* 1989 */           performer.getCommunicator().sendNormalServerMessage("You deal a lot of damage to the wall!");
/*      */         }
/*      */       
/* 1992 */       } else if (action == 174 && !wall.isIndestructible()) {
/*      */         
/* 1994 */         if (!wall.isRubble())
/*      */         {
/* 1996 */           int tilex = wall.getStartX();
/* 1997 */           int tiley = wall.getStartY();
/* 1998 */           VolaTile wallTile = null;
/* 1999 */           for (int xx = 1; xx >= -1; xx--) {
/*      */             
/* 2001 */             for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */               
/*      */               try {
/* 2005 */                 Zone zone = Zones.getZone(tilex + xx, tiley + yy, wall.isOnSurface());
/* 2006 */                 VolaTile tile = zone.getTileOrNull(tilex + xx, tiley + yy);
/* 2007 */                 if (tile != null) {
/*      */                   
/* 2009 */                   Wall[] walls = tile.getWalls();
/* 2010 */                   for (int s = 0; s < walls.length; s++) {
/*      */                     
/* 2012 */                     if (walls[s].getId() == wall.getId()) {
/*      */                       
/* 2014 */                       wallTile = tile;
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                   } 
/*      */                 } 
/* 2020 */               } catch (NoSuchZoneException noSuchZoneException) {}
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 2026 */           if (wallTile == null) {
/*      */             
/* 2028 */             performer.getCommunicator().sendNormalServerMessage("You fail to destroy the wall.");
/* 2029 */             return true;
/*      */           } 
/* 2031 */           done = MethodsStructure.destroyWall(action, performer, source, wall, false, counter);
/*      */         }
/*      */         else
/*      */         {
/* 2035 */           performer.getCommunicator().sendNormalServerMessage("The rubble will clear by itself soon.");
/* 2036 */           return true;
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 2044 */       else if (action == 231) {
/*      */         
/* 2046 */         if (wall.isFinished()) {
/*      */           
/* 2048 */           if (Methods.isActionAllowed(performer, action, wall.getTileX(), wall.getTileY()))
/*      */           {
/* 2050 */             if (wall.isNotPaintable()) {
/*      */               
/* 2052 */               performer.getCommunicator().sendNormalServerMessage("You are not allowed to paint this wall.");
/* 2053 */               return true;
/*      */             } 
/*      */             
/* 2056 */             done = MethodsStructure.colorWall(performer, source, wall, act);
/*      */           }
/*      */           else
/*      */           {
/* 2060 */             performer.getCommunicator().sendNormalServerMessage("You are not allowed to paint this wall.");
/* 2061 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 2066 */           performer.getCommunicator().sendNormalServerMessage("Finish the wall first.");
/* 2067 */           return true;
/*      */         }
/*      */       
/* 2070 */       } else if (action == 232) {
/*      */         
/* 2072 */         if (Methods.isActionAllowed(performer, action, wall.getTileX(), wall.getTileY()))
/*      */         {
/* 2074 */           done = MethodsStructure.removeColor(performer, source, wall, act);
/*      */         }
/*      */         else
/*      */         {
/* 2078 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to remove the paint from this wall.");
/* 2079 */           return true;
/*      */         }
/*      */       
/* 2082 */       } else if (action == 82) {
/*      */ 
/*      */         
/* 2085 */         DemolishCheckQuestion dcq = new DemolishCheckQuestion(performer, "Demolish Building", "A word of warning!", wall.getStructureId());
/* 2086 */         dcq.sendQuestion();
/*      */       } else {
/* 2088 */         if (action == 662) {
/*      */           
/* 2090 */           if (performer.getPower() >= 2) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2100 */             wall.setIndoor(!wall.isIndoor());
/* 2101 */             performer.getCommunicator().sendNormalServerMessage("Wall toggled and now is " + (
/* 2102 */                 wall.isIndoor() ? "Inside" : "Outside"));
/*      */             
/* 2104 */             if (structure != null) {
/*      */               
/* 2106 */               structure.updateStructureFinishFlag();
/*      */             }
/*      */             else {
/*      */               
/* 2110 */               performer.getCommunicator().sendNormalServerMessage("The structural integrity of the building is at risk.");
/* 2111 */               logger.log(Level.WARNING, "Structure not found while trying to toggle a wall at [" + wall.getStartX() + "," + wall
/* 2112 */                   .getStartY() + "]");
/*      */             } 
/*      */           } 
/* 2115 */           return true;
/*      */         } 
/* 2117 */         if (action == 78)
/*      */         
/* 2119 */         { if (performer.getPower() >= 2) {
/*      */             
/*      */             try {
/*      */               
/* 2123 */               Structure struct = Structures.getStructure(wall.getStructureId());
/*      */               
/*      */               try {
/* 2126 */                 Items.getItem(struct.getWritId());
/* 2127 */                 performer.getCommunicator().sendNormalServerMessage("Writ item exists for structure.");
/*      */               }
/* 2129 */               catch (NoSuchItemException nss) {
/*      */                 
/* 2131 */                 performer.getCommunicator().sendNormalServerMessage("Writ item does not exist for structure. Replacing.");
/*      */ 
/*      */                 
/*      */                 try {
/* 2135 */                   Item newWrit = ItemFactory.createItem(166, 80.0F + Server.rand
/* 2136 */                       .nextFloat() * 20.0F, performer.getName());
/* 2137 */                   newWrit.setDescription(struct.getName());
/* 2138 */                   performer.getInventory().insertItem(newWrit);
/* 2139 */                   struct.setWritid(newWrit.getWurmId(), true);
/*      */                 }
/* 2141 */                 catch (NoSuchTemplateException nst) {
/*      */                   
/* 2143 */                   performer.getCommunicator().sendNormalServerMessage("Failed replace:" + nst.getMessage());
/*      */                 }
/* 2145 */                 catch (FailedException enst) {
/*      */                   
/* 2147 */                   performer.getCommunicator().sendNormalServerMessage("Failed replace:" + enst.getMessage());
/*      */                 }
/*      */               
/*      */               } 
/* 2151 */             } catch (NoSuchStructureException nss) {
/*      */               
/* 2153 */               logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/* 2154 */               performer.getCommunicator().sendNormalServerMessage("No such structure. Bug. Good luck.");
/*      */             }
/*      */           
/*      */           } }
/* 2158 */         else if (action == 472)
/*      */         
/* 2160 */         { done = true;
/* 2161 */           if (source.getTemplateId() == 676 && source.getOwnerId() == performer.getWurmId())
/*      */           {
/*      */             
/* 2164 */             MissionManager m = new MissionManager(performer, "Manage missions", "Select action", wall.getId(), wall.getName(), source.getWurmId());
/* 2165 */             m.sendQuestion();
/*      */           }
/*      */            }
/* 2168 */         else if (action == 90)
/*      */         
/* 2170 */         { if (performer.getPower() < 4) {
/*      */             
/* 2172 */             logger.log(Level.WARNING, "Possible hack attempt by " + performer.getName() + " calling Actions.POLL on wall in WallBehaviour without enough power.");
/*      */             
/* 2174 */             return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2194 */           int tilex = wall.getStartX();
/* 2195 */           int tiley = wall.getStartY();
/* 2196 */           VolaTile wallTile = Zones.getOrCreateTile(tilex, tiley, true);
/* 2197 */           if (wallTile != null)
/*      */           {
/* 2199 */             Structure struct = null;
/*      */             
/*      */             try {
/* 2202 */               struct = Structures.getStructure(wall.getStructureId());
/*      */             }
/* 2204 */             catch (NoSuchStructureException e) {
/*      */               
/* 2206 */               logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */             } 
/*      */             
/* 2209 */             if (struct == null) {
/*      */               
/* 2211 */               performer.getCommunicator().sendNormalServerMessage("Couldn't find structure for wall '" + wall
/* 2212 */                   .getId() + "'.");
/* 2213 */               return true;
/*      */             } 
/* 2215 */             wall.poll(struct.getCreationDate() + 604800000L, wallTile, struct);
/* 2216 */             performer.getCommunicator().sendNormalServerMessage("Poll performed for wall '" + wall.getId() + "'.");
/*      */           }
/*      */           else
/*      */           {
/* 2220 */             performer.getCommunicator().sendNormalServerMessage("Unexpectedly missing a tile for " + tilex + "," + tiley + ".");
/*      */           }
/*      */            }
/*      */         
/* 2224 */         else if (action == 664)
/*      */         
/* 2226 */         { manageBuilding(performer, structure, wall); }
/*      */         
/* 2228 */         else if (action == 666)
/*      */         
/* 2230 */         { manageDoor(performer, structure, wall); }
/*      */         
/* 2232 */         else if (action == 673)
/*      */         
/* 2234 */         { manageAllDoors(performer, structure, wall); }
/*      */         
/* 2236 */         else if (action == 102 && mayLockDoor(performer, wall, door))
/*      */         
/* 2238 */         { if (door != null && door.hasLock() && door.isLocked() && !door.isNotLockable())
/*      */           {
/* 2240 */             door.unlock(true);
/* 2241 */             PermissionsHistories.addHistoryEntry(door.getWurmId(), System.currentTimeMillis(), performer
/* 2242 */                 .getWurmId(), performer.getName(), "Unlocked door");
/*      */           }
/*      */            }
/* 2245 */         else if (action == 28 && mayLockDoor(performer, wall, door))
/*      */         
/* 2247 */         { if (door != null && door.hasLock() && !door.isLocked() && !door.isNotLockable()) {
/*      */             
/* 2249 */             door.lock(true);
/* 2250 */             PermissionsHistories.addHistoryEntry(door.getWurmId(), System.currentTimeMillis(), performer
/* 2251 */                 .getWurmId(), performer.getName(), "Locked door");
/*      */           }  }
/*      */         else
/* 2254 */         { if (action == 866) {
/*      */             
/* 2256 */             if (performer.getPower() >= 4)
/*      */             {
/* 2258 */               Methods.sendGmBuildAllWallsQuestion(performer, structure);
/*      */             }
/* 2260 */             return true;
/*      */           } 
/* 2262 */           if (action == 684) {
/*      */             
/* 2264 */             if ((source.getTemplateId() == 315 || source.getTemplateId() == 176) && performer
/* 2265 */               .getPower() >= 2) {
/*      */               
/* 2267 */               Methods.sendItemRestrictionManagement(performer, (Permissions.IAllow)wall, wall.getId());
/*      */             } else {
/*      */               
/* 2270 */               logger.log(Level.WARNING, performer.getName() + " hacking the protocol by trying to set the restrictions of " + wall + ", counter: " + counter + '!');
/*      */             } 
/*      */             
/* 2273 */             return true;
/*      */           } 
/* 2275 */           if (source.isTrellis() && (action == 176 || action == 746 || action == 747))
/*      */           {
/* 2277 */             done = Terraforming.plantTrellis(performer, source, wall.getMinX(), wall.getMinY(), wall.isOnSurface(), wall.getDir(), action, counter, act); }  } 
/*      */       } 
/* 2279 */     }  return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final int getItemTemplateWeightInGrams(int itemTemplateId) {
/*      */     int neededTemplateWeightGrams;
/*      */     
/* 2287 */     try { neededTemplateWeightGrams = ItemTemplateFactory.getInstance().getTemplate(itemTemplateId).getWeightGrams(); }
/*      */     
/* 2289 */     catch (NoSuchTemplateException nst)
/*      */     
/*      */     { 
/* 2292 */       switch (itemTemplateId)
/*      */       
/*      */       { case 22:
/* 2295 */           neededTemplateWeightGrams = 2000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2312 */           return neededTemplateWeightGrams;case 217: neededTemplateWeightGrams = 300; return neededTemplateWeightGrams;case 492: neededTemplateWeightGrams = 2000; return neededTemplateWeightGrams;case 132: neededTemplateWeightGrams = 150000; return neededTemplateWeightGrams; }  neededTemplateWeightGrams = 150000; }  return neededTemplateWeightGrams;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean toggleRenderWall(Creature performer, Item tool, Wall wall, Action act, float counter) {
/* 2317 */     boolean insta = (tool.isWand() && performer.getPower() >= 4);
/* 2318 */     VolaTile wallTile = getWallTile(wall);
/* 2319 */     if (wallTile == null) {
/* 2320 */       return true;
/*      */     }
/* 2322 */     Structure structure = wallTile.getStructure();
/* 2323 */     if (!insta && structure != null && !MethodsStructure.mayModifyStructure(performer, structure, wallTile, (short)683)) {
/*      */       
/* 2325 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to modify the structure.");
/*      */       
/* 2327 */       return true;
/*      */     } 
/* 2329 */     if (!Methods.isActionAllowed(performer, (short)116, wallTile.getTileX(), wallTile.getTileY()))
/*      */     {
/* 2331 */       return true;
/*      */     }
/* 2333 */     if (wall.isPlainStone() && !insta)
/*      */     {
/*      */       
/* 2336 */       if (tool.getWeightGrams() < 20000) {
/*      */         
/* 2338 */         performer.getCommunicator().sendNormalServerMessage("It takes 20kg of " + tool
/* 2339 */             .getName() + " to render the " + wall.getName() + ".");
/* 2340 */         return true;
/*      */       } 
/*      */     }
/* 2343 */     int time = 40;
/* 2344 */     if (counter == 1.0F) {
/*      */       
/* 2346 */       String render = wall.isPlainStone() ? "render" : "remove the render from";
/* 2347 */       String action = wall.isPlainStone() ? "rendering wall" : "removing the wall render";
/* 2348 */       act.setTimeLeft(time);
/* 2349 */       performer.sendActionControl(action, true, time);
/* 2350 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You start to " + render + " the %s.", new Object[] { wall.getName() }));
/* 2351 */       Server.getInstance().broadCastAction(
/* 2352 */           StringUtil.format("%s starts to " + render + " the %s.", new Object[] { performer.getName(), wall.getName() }), performer, 5);
/* 2353 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2357 */     time = act.getTimeLeft();
/*      */     
/* 2359 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 2361 */       String render = wall.isPlainStone() ? "render" : "remove the render from";
/* 2362 */       String renders = wall.isPlainStone() ? "renders" : "removes the render from";
/* 2363 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You " + render + " the %s.", new Object[] { wall.getName() }));
/* 2364 */       Server.getInstance().broadCastAction(
/* 2365 */           StringUtil.format("%s " + renders + " the %s.", new Object[] { performer.getName(), wall.getName() }), performer, 5);
/* 2366 */       if (wall.isPlainStone() && !insta)
/*      */       {
/* 2368 */         tool.setWeight(tool.getWeightGrams() - 20000, true);
/*      */       }
/*      */       
/* 2371 */       if (wall.isPlainStone()) {
/* 2372 */         wall.setMaterial(StructureMaterialEnum.RENDERED);
/*      */       } else {
/* 2374 */         wall.setMaterial(StructureMaterialEnum.PLAIN_STONE);
/*      */       } 
/*      */       try {
/* 2377 */         wall.save();
/*      */       }
/* 2379 */       catch (IOException e) {
/*      */ 
/*      */         
/* 2382 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/* 2384 */       wallTile.updateWall(wall);
/* 2385 */       return true;
/*      */     } 
/* 2387 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean toggleLeftRightArch(Creature performer, Item tool, Wall wall, Action act, float counter) {
/* 2392 */     boolean insta = (tool.isWand() && performer.getPower() >= 4);
/* 2393 */     VolaTile wallTile = getWallTile(wall);
/* 2394 */     if (wallTile == null) {
/* 2395 */       return true;
/*      */     }
/* 2397 */     Structure structure = wallTile.getStructure();
/* 2398 */     if (!insta && !MethodsStructure.mayModifyStructure(performer, structure, wallTile, (short)683)) {
/*      */       
/* 2400 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to modify the structure.");
/*      */       
/* 2402 */       return true;
/*      */     } 
/* 2404 */     if (!Methods.isActionAllowed(performer, (short)116, wallTile.getTileX(), wallTile.getTileY()))
/*      */     {
/* 2406 */       return true;
/*      */     }
/*      */     
/* 2409 */     int time = 40;
/* 2410 */     if (counter == 1.0F) {
/*      */       
/* 2412 */       act.setTimeLeft(time);
/* 2413 */       performer.sendActionControl("Moving Arch", true, time);
/* 2414 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You start to move the %s.", new Object[] { wall.getName() }));
/* 2415 */       Server.getInstance().broadCastAction(
/* 2416 */           StringUtil.format("%s starts to move the %s.", new Object[] { performer.getName(), wall.getName() }), performer, 5);
/* 2417 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2421 */     time = act.getTimeLeft();
/*      */     
/* 2423 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 2425 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You move the %s.", new Object[] { wall.getName() }));
/* 2426 */       Server.getInstance().broadCastAction(
/* 2427 */           StringUtil.format("%s move the %s.", new Object[] { performer.getName(), wall.getName() }), performer, 5);
/* 2428 */       if (wall.getType() == StructureTypeEnum.ARCHED_LEFT) {
/* 2429 */         wall.setType(StructureTypeEnum.ARCHED_RIGHT);
/*      */       } else {
/* 2431 */         wall.setType(StructureTypeEnum.ARCHED_LEFT);
/*      */       } 
/*      */       try {
/* 2434 */         wall.save();
/*      */       }
/* 2436 */       catch (IOException e) {
/*      */ 
/*      */         
/* 2439 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/* 2441 */       wallTile.updateWall(wall);
/* 2442 */       return true;
/*      */     } 
/*      */     
/* 2445 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean rotateWall(Creature performer, Wall wall, Action act, float counter) {
/* 2450 */     boolean insta = (performer.getPower() >= 4);
/* 2451 */     VolaTile wallTile = getWallTile(wall);
/* 2452 */     if (wallTile == null) {
/* 2453 */       return true;
/*      */     }
/* 2455 */     Structure structure = wallTile.getStructure();
/* 2456 */     if (!insta && !MethodsStructure.mayModifyStructure(performer, structure, wallTile, (short)683)) {
/*      */       
/* 2458 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to modify the structure.");
/*      */       
/* 2460 */       return true;
/*      */     } 
/* 2462 */     if (!Methods.isActionAllowed(performer, (short)116, wallTile.getTileX(), wallTile.getTileY()))
/*      */     {
/* 2464 */       return true;
/*      */     }
/*      */     
/* 2467 */     int time = 40;
/* 2468 */     if (counter == 1.0F) {
/*      */       
/* 2470 */       act.setTimeLeft(time);
/* 2471 */       performer.sendActionControl("Rotating wall", true, time);
/* 2472 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You start to rotate the %s.", new Object[] { wall.getName() }));
/* 2473 */       Server.getInstance().broadCastAction(
/* 2474 */           StringUtil.format("%s starts to rotate the %s.", new Object[] { performer.getName(), wall.getName() }), performer, 5);
/* 2475 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2479 */     time = act.getTimeLeft();
/*      */     
/* 2481 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 2483 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You rotate the %s.", new Object[] { wall.getName() }));
/* 2484 */       Server.getInstance().broadCastAction(
/* 2485 */           StringUtil.format("%s rotates the %s.", new Object[] { performer.getName(), wall.getName() }), performer, 5);
/* 2486 */       wall.setWallOrientation(!wall.getWallOrientationFlag());
/*      */ 
/*      */       
/* 2489 */       return true;
/*      */     } 
/*      */     
/* 2492 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   static final VolaTile getWallTile(Wall wall) {
/* 2498 */     int tilex = wall.getStartX();
/* 2499 */     int tiley = wall.getStartY();
/* 2500 */     for (int xx = 1; xx >= -1; xx--) {
/*      */       
/* 2502 */       for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */         
/*      */         try {
/* 2506 */           Zone zone = Zones.getZone(tilex + xx, tiley + yy, wall.isOnSurface());
/* 2507 */           VolaTile tile = zone.getTileOrNull(tilex + xx, tiley + yy);
/* 2508 */           if (tile != null) {
/*      */             
/* 2510 */             Wall[] walls = tile.getWalls();
/* 2511 */             for (int s = 0; s < walls.length; s++)
/*      */             {
/* 2513 */               if (walls[s].getId() == wall.getId())
/*      */               {
/* 2515 */                 return tile;
/*      */               }
/*      */             }
/*      */           
/*      */           } 
/* 2520 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2526 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean modifyWall(Creature performer, Item tool, Wall wall, Action act, float counter) {
/* 2533 */     if (!isValidModifyableWall(wall, tool))
/*      */     {
/* 2535 */       return true;
/*      */     }
/* 2537 */     int tilex = wall.getStartX();
/* 2538 */     int tiley = wall.getStartY();
/* 2539 */     VolaTile wallTile = null;
/*      */ 
/*      */     
/* 2542 */     for (int xx = 1; xx >= -1; xx--) {
/*      */       
/* 2544 */       for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */         
/*      */         try {
/* 2548 */           Zone zone = Zones.getZone(tilex + xx, tiley + yy, performer.isOnSurface());
/* 2549 */           VolaTile tile = zone.getTileOrNull(tilex + xx, tiley + yy);
/* 2550 */           if (tile != null) {
/*      */             
/* 2552 */             Wall[] walls = tile.getWalls();
/* 2553 */             for (int s = 0; s < walls.length; s++) {
/*      */               
/* 2555 */               if (walls[s].getId() == wall.getId()) {
/*      */                 
/* 2557 */                 wallTile = tile;
/*      */                 
/* 2559 */                 if (wallTile.getStructure() != null && !wallTile.getStructure().isFinalized()) {
/*      */                   
/* 2561 */                   performer.getCommunicator().sendNormalServerMessage("You need to finalize the build plan before you start building.");
/*      */                   
/* 2563 */                   performer.getCommunicator().sendActionResult(false);
/* 2564 */                   return true;
/*      */                 } 
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/* 2571 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2578 */     if (wallTile == null) {
/* 2579 */       return true;
/*      */     }
/* 2581 */     Structure structure = wallTile.getStructure();
/*      */     
/* 2583 */     if (!MethodsStructure.mayModifyStructure(performer, structure, wallTile, act.getNumber())) {
/*      */       
/* 2585 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to modify the structure.");
/*      */       
/* 2587 */       return true;
/*      */     } 
/* 2589 */     if (!Methods.isActionAllowed(performer, (short)116, wallTile.getTileX(), wallTile.getTileY()))
/*      */     {
/* 2591 */       return true;
/*      */     }
/* 2593 */     int time = 40;
/* 2594 */     if (counter == 1.0F) {
/*      */       
/* 2596 */       String action = (tool.getTemplateId() == 219) ? "removing decoration" : "adding decoration";
/* 2597 */       String modify = (tool.getTemplateId() == 219) ? "remove the decorations from the " : "add decorations to the ";
/* 2598 */       act.setTimeLeft(time);
/* 2599 */       performer.sendActionControl(action, true, time);
/* 2600 */       performer.getCommunicator().sendNormalServerMessage("You start to " + modify + wall.getName() + ".");
/* 2601 */       Server.getInstance().broadCastAction(performer
/* 2602 */           .getName() + " starts to " + modify + wall.getName() + ".", performer, 5);
/* 2603 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2607 */     time = act.getTimeLeft();
/* 2608 */     if (counter * 10.0F > time) {
/*      */       
/* 2610 */       String modify = (tool.getTemplateId() == 219) ? "removed the decorations from the " : "added decorations to the ";
/* 2611 */       performer.getCommunicator().sendNormalServerMessage("You " + modify + wall.getName() + ".");
/* 2612 */       Server.getInstance().broadCastAction(performer
/* 2613 */           .getName() + modify + wall.getName() + ".", performer, 5);
/* 2614 */       wall.setMaterial((wall.getMaterial() == StructureMaterialEnum.STONE) ? StructureMaterialEnum.PLAIN_STONE : StructureMaterialEnum.STONE);
/* 2615 */       wallTile.updateWall(wall);
/*      */       
/* 2617 */       return true;
/*      */     } 
/*      */     
/* 2620 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean checkWallItem2(Creature performer, Wall wall, String buildString, int time, Action act) {
/* 2628 */     inventoryItems = performer.getInventory().getAllItems(false);
/* 2629 */     bodyItems = performer.getBody().getAllItems();
/*      */ 
/*      */     
/* 2632 */     int[] neededTemplates = wall.getTemplateIdsNeededForNextState(WallEnum.getWallByActionId(act.getNumber()).getType());
/*      */     
/* 2634 */     resetFoundTemplates(neededTemplates);
/*      */     
/* 2636 */     for (int x = 0; x < inventoryItems.length; x++) {
/*      */       
/* 2638 */       for (int i = 0; i < neededTemplates.length; i++) {
/*      */         
/* 2640 */         if (inventoryItems[x].getTemplateId() == neededTemplates[i]) {
/*      */           
/* 2642 */           int neededTemplateWeightGrams = getItemTemplateWeightInGrams(inventoryItems[x].getTemplateId());
/* 2643 */           if (neededTemplateWeightGrams <= inventoryItems[x].getWeightGrams())
/*      */           {
/* 2645 */             foundTemplates[i] = neededTemplates[i];
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 2650 */     boolean found = true;
/* 2651 */     for (int f = 0; f < foundTemplates.length; f++) {
/*      */       
/* 2653 */       if (foundTemplates[f] == -1)
/* 2654 */         found = false; 
/*      */     } 
/* 2656 */     if (!found) {
/*      */       
/* 2658 */       for (int j = 0; j < bodyItems.length; j++) {
/*      */         
/* 2660 */         for (int k = 0; k < neededTemplates.length; k++) {
/*      */           
/* 2662 */           if (bodyItems[j].getTemplateId() == neededTemplates[k]) {
/*      */             
/* 2664 */             int neededTemplateWeightGrams = getItemTemplateWeightInGrams(bodyItems[j].getTemplateId());
/* 2665 */             if (neededTemplateWeightGrams <= bodyItems[j].getWeightGrams())
/*      */             {
/* 2667 */               foundTemplates[k] = neededTemplates[k];
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 2672 */       found = true;
/* 2673 */       for (int i = 0; i < foundTemplates.length; i++) {
/*      */         
/* 2675 */         if (foundTemplates[i] == -1)
/* 2676 */           found = false; 
/*      */       } 
/*      */     } 
/* 2679 */     for (int n = 0; n < foundTemplates.length; n++) {
/*      */       
/* 2681 */       if (foundTemplates[n] == -1) {
/*      */ 
/*      */         
/*      */         try {
/* 2685 */           if (neededTemplates[n] == 217) {
/* 2686 */             performer.getCommunicator().sendNormalServerMessage("You need large iron nails.");
/*      */           } else {
/*      */             
/* 2689 */             ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(neededTemplates[n]);
/* 2690 */             if (template.isCombine()) {
/* 2691 */               performer.getCommunicator().sendNormalServerMessage("You need " + template.getName() + ".");
/*      */             } else {
/* 2693 */               performer.getCommunicator()
/* 2694 */                 .sendNormalServerMessage("You need " + template.getNameWithGenus() + ".");
/*      */             } 
/*      */           } 
/* 2697 */         } catch (NoSuchTemplateException nst) {
/*      */           
/* 2699 */           logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */         } 
/* 2701 */         found = false;
/*      */       } 
/*      */     } 
/* 2704 */     if (!found)
/* 2705 */       return true; 
/* 2706 */     if (wall.getState() == StructureStateEnum.INITIALIZED) {
/*      */       
/* 2708 */       String a_an = (buildString.charAt(0) == 'a') ? "an" : "a";
/* 2709 */       Server.getInstance().broadCastAction(performer.getName() + " starts to build " + a_an + " " + buildString + ".", performer, 5);
/*      */       
/* 2711 */       performer.getCommunicator().sendNormalServerMessage("You start to build " + a_an + " " + buildString + ".");
/* 2712 */       performer.sendActionControl("Building " + buildString, true, time);
/*      */     } 
/* 2714 */     return false;
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
/*      */   static final void resetFoundTemplates(int[] needed) {
/* 2745 */     foundTemplates = new int[needed.length];
/* 2746 */     for (int x = 0; x < foundTemplates.length; x++) {
/* 2747 */       foundTemplates[x] = -1;
/*      */     }
/*      */   }
/*      */   
/*      */   static final boolean depleteWallItems2(Creature performer, Wall wall, Action act) {
/* 2752 */     inventoryItems = performer.getInventory().getAllItems(false);
/* 2753 */     bodyItems = performer.getBody().getAllItems();
/* 2754 */     float qlevel = 0.0F;
/*      */     
/* 2756 */     int[] neededTemplates = wall.getTemplateIdsNeededForNextState(WallEnum.getWallByActionId(act.getNumber()).getType());
/* 2757 */     resetFoundTemplates(neededTemplates);
/* 2758 */     Item[] depleteItems = new Item[neededTemplates.length];
/* 2759 */     for (int i = 0; i < neededTemplates.length; i++) {
/*      */       
/* 2761 */       if (foundTemplates[i] == -1)
/*      */       {
/* 2763 */         for (int k = 0; k < inventoryItems.length; k++) {
/*      */           
/* 2765 */           if (inventoryItems[k].getTemplateId() == neededTemplates[i]) {
/*      */             
/* 2767 */             int neededTemplateWeightGrams = getItemTemplateWeightInGrams(inventoryItems[k].getTemplateId());
/* 2768 */             if (neededTemplateWeightGrams <= inventoryItems[k].getWeightGrams()) {
/*      */               
/* 2770 */               depleteItems[i] = inventoryItems[k];
/* 2771 */               foundTemplates[i] = neededTemplates[i];
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 2778 */     boolean allInitialized = true; int j;
/* 2779 */     for (j = 0; j < foundTemplates.length; j++) {
/*      */       
/* 2781 */       if (foundTemplates[j] == -1) {
/*      */         
/* 2783 */         allInitialized = false;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 2788 */     if (!allInitialized) {
/*      */       
/* 2790 */       for (j = 0; j < neededTemplates.length; j++) {
/*      */         
/* 2792 */         if (foundTemplates[j] == -1)
/*      */         {
/* 2794 */           for (int k = 0; k < bodyItems.length; k++) {
/*      */             
/* 2796 */             if (bodyItems[k].getTemplateId() == neededTemplates[j]) {
/*      */               
/* 2798 */               int neededTemplateWeightGrams = getItemTemplateWeightInGrams(bodyItems[k].getTemplateId());
/* 2799 */               if (neededTemplateWeightGrams <= bodyItems[k].getWeightGrams()) {
/*      */                 
/* 2801 */                 depleteItems[j] = bodyItems[k];
/* 2802 */                 foundTemplates[j] = neededTemplates[j];
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           }  } 
/*      */       } 
/* 2808 */       allInitialized = true;
/* 2809 */       for (j = 0; j < foundTemplates.length; j++) {
/*      */         
/* 2811 */         if (foundTemplates[j] == -1) {
/*      */           
/* 2813 */           allInitialized = false;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2819 */     if (!allInitialized) {
/*      */       
/* 2821 */       for (j = 0; j < foundTemplates.length; j++) {
/*      */         
/* 2823 */         if (foundTemplates[j] == -1) {
/*      */           
/*      */           try {
/*      */             
/* 2827 */             ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(neededTemplates[j]);
/* 2828 */             performer.getCommunicator().sendNormalServerMessage("You did not have enough " + template
/* 2829 */                 .getPlural() + ".");
/*      */           }
/* 2831 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 2833 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         }
/*      */       } 
/* 2837 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2841 */     for (j = 0; j < depleteItems.length; j++) {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 2847 */         Items.getItem(depleteItems[j].getWurmId());
/*      */       }
/* 2849 */       catch (NoSuchItemException nsie) {
/*      */         
/* 2851 */         performer.getCommunicator().sendAlertServerMessage("ERROR: " + depleteItems[j].getName() + " not found, WurmID: " + depleteItems[j].getWurmId());
/* 2852 */         return false;
/*      */       } 
/*      */       
/* 2855 */       if (depleteItems[j].isCombine()) {
/* 2856 */         depleteItems[j].setWeight(depleteItems[j]
/* 2857 */             .getWeightGrams() - depleteItems[j].getTemplate().getWeightGrams(), true);
/*      */       } else {
/* 2859 */         Items.destroyItem(depleteItems[j].getWurmId());
/* 2860 */       }  qlevel += depleteItems[j].getCurrentQualityLevel() / 21.0F;
/*      */     } 
/* 2862 */     act.setPower(qlevel);
/* 2863 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isIndoorWallPlan(Wall aWall) {
/* 2869 */     if (!aWall.isIndoor())
/*      */     {
/* 2871 */       return false;
/*      */     }
/*      */     
/* 2874 */     if (!aWall.isWallPlan())
/*      */     {
/* 2876 */       return false;
/*      */     }
/* 2878 */     return true;
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
/*      */   private boolean canRemoveWallPlan(Creature aPerformer, Wall aWall) {
/* 2894 */     if (!isIndoorWallPlan(aWall))
/*      */     {
/* 2896 */       return false;
/*      */     }
/* 2898 */     if (!Methods.isActionAllowed(aPerformer, (short)57, aWall.getTileX(), aWall.getTileY()))
/*      */     {
/* 2900 */       return false;
/*      */     }
/*      */     
/*      */     try {
/* 2904 */       Structure struct = Structures.getStructure(aWall.getStructureId());
/*      */       
/* 2906 */       if (struct.wouldCreateFlyingStructureIfRemoved((StructureSupport)aWall))
/*      */       {
/* 2908 */         return false;
/*      */       }
/*      */     }
/* 2911 */     catch (NoSuchStructureException nsc) {
/*      */       
/* 2913 */       return true;
/*      */     } 
/* 2915 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WallBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */