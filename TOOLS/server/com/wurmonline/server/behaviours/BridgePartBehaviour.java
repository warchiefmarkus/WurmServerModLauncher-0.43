/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.questions.MissionManager;
/*      */ import com.wurmonline.server.questions.QuestionTypes;
/*      */ import com.wurmonline.server.questions.TextInputQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.IFloor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.LinkedList;
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
/*      */ public final class BridgePartBehaviour
/*      */   extends TileBehaviour
/*      */   implements QuestionTypes
/*      */ {
/*      */   BridgePartBehaviour() {
/*   71 */     super((short)51);
/*      */   }
/*      */ 
/*      */   
/*   75 */   private static final Logger logger = Logger.getLogger(BridgePartBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, boolean onSurface, BridgePart bridgePart) {
/*   87 */     List<ActionEntry> toReturn = new LinkedList<>();
/*   88 */     if (!bridgePart.isFinished())
/*   89 */       toReturn.add(Actions.actionEntrys[607]); 
/*   90 */     toReturn.addAll(Actions.getDefaultTileActions());
/*      */     
/*   92 */     toReturn.addAll(getTileAndFloorBehavioursFor(performer, (Item)null, bridgePart.getTileX(), bridgePart.getTileY(), Tiles.Tile.TILE_DIRT.id));
/*      */ 
/*      */     
/*   95 */     Structure bridge = Structures.getBridge(bridgePart.getStructureId());
/*   96 */     if (bridge != null && (bridge.isOwner(performer) || performer.getPower() >= 2)) {
/*   97 */       toReturn.add(Actions.actionEntrys[59]);
/*      */     }
/*   99 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, boolean onSurface, BridgePart bridgePart) {
/*  106 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  107 */     if (!bridgePart.isFinished())
/*  108 */       toReturn.add(Actions.actionEntrys[607]); 
/*  109 */     toReturn.addAll(getTileAndFloorBehavioursFor(performer, source, bridgePart.getTileX(), bridgePart.getTileY(), Tiles.Tile.TILE_DIRT.id));
/*      */ 
/*      */     
/*  112 */     VolaTile floorTile = Zones.getOrCreateTile(bridgePart.getTileX(), bridgePart.getTileY(), bridgePart.isOnSurface());
/*  113 */     BridgeConstants.BridgeState bpState = bridgePart.getBridgePartState();
/*  114 */     Structure structure = null;
/*      */     
/*      */     try {
/*  117 */       structure = Structures.getStructure(bridgePart.getStructureId());
/*      */     }
/*  119 */     catch (NoSuchStructureException e) {
/*      */       
/*  121 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*  122 */       toReturn.addAll(Actions.getDefaultItemActions());
/*  123 */       return toReturn;
/*      */     } 
/*      */     
/*  126 */     if (MethodsStructure.mayModifyStructure(performer, structure, floorTile, (short)169))
/*      */     {
/*  128 */       if (bpState.isBeingBuilt()) {
/*      */         
/*  130 */         toReturn.add(new ActionEntry((short)169, "Continue building", "building"));
/*      */       }
/*  132 */       else if (bpState == BridgeConstants.BridgeState.PLANNED) {
/*      */ 
/*      */         
/*  135 */         toReturn.add(new ActionEntry((short)(20000 + bridgePart.getMaterial().getCode()), "Build " + bridgePart
/*  136 */               .getMaterial().getName().toLowerCase() + " " + bridgePart
/*  137 */               .getType().getName(), bridgePart.getMaterial().getName() + " " + bridgePart
/*  138 */               .getType().getName(), emptyIntArr));
/*      */       } 
/*      */     }
/*      */     
/*  142 */     if (!source.isTraded()) {
/*      */       
/*  144 */       if (source.getTemplateId() == bridgePart.getRepairItemTemplate())
/*      */       {
/*  146 */         if (bridgePart.getDamage() > 0.0F) {
/*      */           
/*  148 */           if (!bridgePart.isNoRepair())
/*      */           {
/*  150 */             toReturn.add(Actions.actionEntrys[193]);
/*      */           }
/*      */         }
/*  153 */         else if (bridgePart.getQualityLevel() < 100.0F && !bridgePart.isNoImprove()) {
/*      */           
/*  155 */           toReturn.add(Actions.actionEntrys[192]);
/*      */         } 
/*      */       }
/*  158 */       toReturn.addAll(Actions.getDefaultItemActions());
/*  159 */       if (!bridgePart.isIndestructible())
/*      */       {
/*      */         
/*  162 */         if (!MethodsHighways.onHighway(bridgePart)) {
/*      */           
/*  164 */           toReturn.add(new ActionEntry((short)-1, "Destroy", "Destroy"));
/*  165 */           toReturn.add(new ActionEntry((short)524, bridgePart.getName(), "destroying"));
/*      */         } 
/*      */       }
/*      */       
/*  169 */       if ((source.getTemplateId() == 176 || source.getTemplateId() == 315) && 
/*  170 */         WurmPermissions.mayUseGMWand(performer) && performer.getPower() >= 2) {
/*      */         
/*  172 */         toReturn.add(new ActionEntry((short)-1, "Annihilate", "Annihilate"));
/*  173 */         toReturn.add(new ActionEntry((short)82, "Destroy " + bridgePart.getMaterial().getName() + " bridge", "destroying"));
/*      */       } 
/*      */       
/*  176 */       if ((source.getTemplateId() == 315 || source.getTemplateId() == 176) && performer
/*  177 */         .getPower() >= 2) {
/*  178 */         toReturn.add(Actions.actionEntrys[684]);
/*      */       }
/*  180 */       if (bridgePart.isFinished() && bridgePart.getMaterial() != BridgeConstants.BridgeMaterial.ROPE && bridgePart.getMaterial() != BridgeConstants.BridgeMaterial.WOOD) {
/*      */         
/*  182 */         byte roadType = bridgePart.getRoadType();
/*      */         
/*  184 */         if ((source.getTemplateId() == 492 || (
/*  185 */           WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/*  186 */           WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) && roadType == 0) {
/*      */ 
/*      */           
/*  189 */           toReturn.add(new ActionEntry((short)155, "Prepare", "preparing the floor"));
/*      */         }
/*  191 */         else if ((source.getTemplateId() == 97 || (
/*  192 */           WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/*  193 */           WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) && roadType == Tiles.Tile.TILE_PREPARED_BRIDGE.id) {
/*      */ 
/*      */           
/*  196 */           toReturn.add(new ActionEntry((short)191, "Remove mortar", "removing mortar"));
/*      */         }
/*  198 */         else if (source.isPaveable() && roadType == Tiles.Tile.TILE_PREPARED_BRIDGE.id) {
/*      */           
/*  200 */           toReturn.add(Actions.actionEntrys[155]);
/*      */         }
/*  202 */         else if ((source.getTemplateId() == 1115 || (
/*  203 */           WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/*  204 */           WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) && 
/*  205 */           Tiles.isRoadType(roadType)) {
/*      */           
/*  207 */           toReturn.add(Actions.actionEntrys[191]);
/*      */         }
/*  209 */         else if (source.getTemplateId() == 153 && roadType == Tiles.Tile.TILE_PLANKS.id) {
/*      */           
/*  211 */           toReturn.add(new ActionEntry((short)231, "Tar", "tarring"));
/*      */         } 
/*  213 */         if (MethodsHighways.onHighway(bridgePart) && source.isPaveable() && roadType != 0 && roadType != Tiles.Tile.TILE_PREPARED_BRIDGE.id)
/*      */         {
/*      */ 
/*      */           
/*  217 */           if (source.isPaveable() && roadType != 0)
/*      */           {
/*  219 */             toReturn.add(new ActionEntry((short)155, "Replace paving", "re-paving"));
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  225 */     Structure bridge = Structures.getBridge(bridgePart.getStructureId());
/*  226 */     if (bridge != null && (bridge.isOwner(performer) || performer.getPower() >= 2)) {
/*  227 */       toReturn.add(Actions.actionEntrys[59]);
/*      */     }
/*  229 */     return toReturn;
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
/*      */   private boolean buildAction(Action act, Creature performer, Item source, BridgePart bridgePart, short action, float counter) {
/*  247 */     if (bridgePart.getBridgePartState() == BridgeConstants.BridgeState.PLANNED) {
/*      */ 
/*      */       
/*  250 */       if (action - 20000 != bridgePart.getMaterial().getCode() && action != 169) {
/*      */ 
/*      */         
/*  253 */         performer.getCommunicator().sendNormalServerMessage("You Cannot build a " + bridgePart
/*  254 */             .getName() + " made of " + 
/*  255 */             BridgeConstants.BridgeMaterial.fromByte((byte)(action - 20000)).getName().toLowerCase() + " as It's already planned to be made of " + bridgePart
/*  256 */             .getMaterial().getName().toLowerCase() + ".");
/*      */         
/*  258 */         return true;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  263 */       if (!isOkToBuild(performer, source, bridgePart))
/*      */       {
/*  265 */         return true;
/*      */       }
/*      */       
/*  268 */       bridgePart.setBridgePartState(BridgeConstants.BridgeState.STAGE1);
/*  269 */       bridgePart.setMaterialCount(0);
/*  270 */       BuildAllMaterials bam = getRequiredMaterials(bridgePart);
/*  271 */       bam.setNeeded(bridgePart.getState(), bridgePart.getMaterialCount());
/*      */ 
/*      */       
/*      */       try {
/*  275 */         bridgePart.save();
/*      */ 
/*      */         
/*  278 */         VolaTile bridgeTile = Zones.getOrCreateTile(bridgePart.getTileX(), bridgePart.getTileY(), bridgePart.isOnSurface());
/*  279 */         bridgeTile.updateBridgePart(bridgePart);
/*      */       }
/*  281 */       catch (IOException e) {
/*      */         
/*  283 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*  285 */       performer.getCommunicator().sendNormalServerMessage("You prepare a " + bridgePart.getName() + ".");
/*      */     } 
/*      */     
/*  288 */     if (bridgePart.getBridgePartState().isBeingBuilt()) {
/*      */       
/*  290 */       if (bridgePartBuilding(act, performer, source, bridgePart, (short)169, counter)) {
/*      */         
/*  292 */         performer.getCommunicator().sendAddBridgePartToCreationWindow(bridgePart, bridgePart.getId());
/*  293 */         return true;
/*      */       } 
/*      */       
/*  296 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  300 */     logger.log(Level.WARNING, "BridgePartBehaviour buildAction on a completed bridge part, it should not happen?!");
/*  301 */     performer.getCommunicator().sendNormalServerMessage("You failed to find anything to do with that.");
/*  302 */     return true;
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
/*      */   static boolean advanceNextState(Creature performer, BridgePart bridgePart, Action act, boolean justCheckIfItemsArePresent) {
/*  317 */     BuildAllMaterials mats = getRequiredMaterials(bridgePart);
/*      */     
/*  319 */     if (takeItemsFromCreature(performer, bridgePart, act, mats, justCheckIfItemsArePresent))
/*      */     {
/*  321 */       return true;
/*      */     }
/*      */     
/*  324 */     if (performer.getPower() >= 2 && !justCheckIfItemsArePresent && Servers.isThisATestServer()) {
/*      */       
/*  326 */       performer.getCommunicator().sendNormalServerMessage("You magically summon some necessary materials.");
/*  327 */       return true;
/*      */     } 
/*      */     
/*  330 */     return false;
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
/*      */   static boolean takeItemsFromCreature(Creature performer, BridgePart bridgePart, Action act, BuildAllMaterials bam, boolean justCheckIfItemsArePresent) {
/*  349 */     Item[] inventoryItems = performer.getInventory().getAllItems(false);
/*  350 */     Item[] bodyItems = performer.getBody().getAllItems();
/*  351 */     List<Item> takeItemsOnSuccess = new ArrayList<>();
/*      */ 
/*      */     
/*  354 */     List<BuildMaterial> mats = bam.getBuildStageMaterials(bridgePart.getState()).getBuildMaterials();
/*  355 */     for (Item item : inventoryItems) {
/*      */       
/*  357 */       for (BuildMaterial mat : mats) {
/*      */         
/*  359 */         if (mat.getNeededQuantity() > 0)
/*      */         {
/*  361 */           if (item.getTemplateId() == mat.getTemplateId() && item.getWeightGrams() >= mat.getWeightGrams()) {
/*      */             
/*  363 */             takeItemsOnSuccess.add(item);
/*  364 */             mat.setNeededQuantity(0);
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  371 */     for (Item item : bodyItems) {
/*      */       
/*  373 */       for (BuildMaterial mat : mats) {
/*      */         
/*  375 */         if (mat.getNeededQuantity() > 0)
/*      */         {
/*  377 */           if (item.getTemplateId() == mat.getTemplateId() && item.getWeightGrams() >= mat.getWeightGrams()) {
/*      */             
/*  379 */             takeItemsOnSuccess.add(item);
/*  380 */             mat.setNeededQuantity(0);
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  386 */     float divider = 1.0F;
/*      */ 
/*      */     
/*  389 */     for (BuildMaterial mat : mats) {
/*      */       
/*  391 */       divider += mat.getTotalQuantityRequired();
/*  392 */       if (mat.getNeededQuantity() > 0)
/*      */       {
/*      */         
/*  395 */         return false;
/*      */       }
/*      */     } 
/*  398 */     float qlevel = 0.0F;
/*  399 */     if (!justCheckIfItemsArePresent)
/*      */     {
/*  401 */       for (Item item : takeItemsOnSuccess) {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */           
/*  407 */           Items.getItem(item.getWurmId());
/*      */         }
/*  409 */         catch (NoSuchItemException nsie) {
/*      */           
/*  411 */           performer.getCommunicator().sendAlertServerMessage("ERROR: " + item.getName() + " not found, WurmID: " + item.getWurmId());
/*  412 */           return false;
/*      */         } 
/*      */ 
/*      */         
/*  416 */         act.setPower(item.getCurrentQualityLevel() / divider);
/*  417 */         performer.sendToLoggers("Adding " + item.getCurrentQualityLevel() + ", divider=" + divider + "=" + act
/*  418 */             .getPower());
/*  419 */         qlevel += item.getCurrentQualityLevel() / 21.0F;
/*  420 */         if (item.isCombine()) {
/*      */           
/*  422 */           item.setWeight(item.getWeightGrams() - item.getTemplate().getWeightGrams(), true);
/*      */           
/*      */           continue;
/*      */         } 
/*  426 */         Items.destroyItem(item.getWurmId());
/*      */       } 
/*      */     }
/*      */     
/*  430 */     act.setPower(qlevel);
/*  431 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getRequiredSkill(BridgeConstants.BridgeMaterial material) {
/*  436 */     switch (material) {
/*      */       
/*      */       case BRICK:
/*  439 */         return 1013;
/*      */       case MARBLE:
/*      */       case SLATE:
/*      */       case ROUNDED_STONE:
/*      */       case POTTERY:
/*      */       case SANDSTONE:
/*      */       case RENDERED:
/*  446 */         return 1013;
/*      */       case ROPE:
/*  448 */         return 1014;
/*      */       case WOOD:
/*  450 */         return 1005;
/*      */     } 
/*  452 */     return 1005;
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
/*      */   public static BuildAllMaterials getRequiredMaterials(BridgePart bridgePart) {
/*  466 */     BuildAllMaterials toReturn = getRequiredMaterials(bridgePart.getType(), bridgePart
/*  467 */         .getMaterial(), bridgePart.getNumberOfExtensions());
/*  468 */     toReturn.setNeeded(bridgePart.getState(), bridgePart.getMaterialCount());
/*  469 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static BuildAllMaterials getRequiredMaterials(BridgeConstants.BridgeType bridgeType, BridgeConstants.BridgeMaterial bridgeMaterial, int bridgeExtensions) {
/*  475 */     BuildAllMaterials toReturn = new BuildAllMaterials();
/*      */     
/*  477 */     BuildStageMaterials main = new BuildStageMaterials("Main");
/*  478 */     BuildStageMaterials foundations = new BuildStageMaterials("Foundations");
/*  479 */     BuildStageMaterials ballast = new BuildStageMaterials("Ballast");
/*  480 */     BuildStageMaterials filler = new BuildStageMaterials("Filler");
/*  481 */     BuildStageMaterials roadway = new BuildStageMaterials("Roadway");
/*  482 */     BuildStageMaterials wall = new BuildStageMaterials("Walls");
/*  483 */     BuildStageMaterials keystone = new BuildStageMaterials("Keystone"); try {
/*      */       int wcw; int wcs;
/*      */       int wcm;
/*  486 */       switch (bridgeMaterial) {
/*      */ 
/*      */ 
/*      */         
/*      */         case WOOD:
/*  491 */           if (bridgeType.isAbutment()) {
/*      */             
/*  493 */             main.add(860, 8);
/*  494 */             main.add(188, 4);
/*  495 */             main.add(217, 2);
/*      */           } 
/*      */           
/*  498 */           if (bridgeType.isCrown()) {
/*      */             
/*  500 */             main.add(860, 4);
/*  501 */             main.add(188, 2);
/*  502 */             main.add(217, 1);
/*      */           } 
/*      */           
/*  505 */           if (bridgeType.isSupportType()) {
/*      */             
/*  507 */             main.add(860, 12);
/*  508 */             main.add(188, 6);
/*  509 */             main.add(217, 3);
/*      */             
/*  511 */             if (bridgeExtensions > 0) {
/*      */               
/*  513 */               foundations.add(860, 4 * bridgeExtensions);
/*  514 */               foundations.add(188, 2 * bridgeExtensions);
/*  515 */               foundations.add(217, 1 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/*  520 */           roadway.add(22, 20);
/*  521 */           roadway.add(217, 2);
/*      */ 
/*      */           
/*  524 */           wcw = bridgeType.wallCount();
/*  525 */           if (wcw > 0) {
/*      */             
/*  527 */             wall.add(22, 2 * wcw);
/*  528 */             wall.add(23, 2 * wcw);
/*  529 */             wall.add(218, 1 * wcw);
/*      */           } 
/*      */           break;
/*      */ 
/*      */         
/*      */         case BRICK:
/*  535 */           if (bridgeType.isAbutment()) {
/*      */             
/*  537 */             main.add(132, 40);
/*  538 */             main.add(492, 40);
/*  539 */             filler.add(146, 16);
/*      */           } 
/*  541 */           if (bridgeType.isBracing()) {
/*      */             
/*  543 */             main.add(132, 25);
/*  544 */             main.add(492, 25);
/*  545 */             filler.add(146, 8);
/*      */           } 
/*  547 */           if (bridgeType.isCrown()) {
/*      */             
/*  549 */             main.add(132, 10);
/*  550 */             main.add(492, 10);
/*  551 */             keystone.add(905, 1);
/*  552 */             filler.add(146, 4);
/*      */           } 
/*  554 */           if (bridgeType.isFloating()) {
/*      */             
/*  556 */             main.add(132, 10);
/*  557 */             main.add(492, 10);
/*  558 */             filler.add(146, 4);
/*      */           } 
/*  560 */           if (bridgeType.isDoubleBracing()) {
/*      */             
/*  562 */             main.add(132, 30);
/*  563 */             main.add(492, 30);
/*  564 */             filler.add(146, 12);
/*      */           } 
/*  566 */           if (bridgeType.isDoubleAbutment()) {
/*      */             
/*  568 */             main.add(132, 60);
/*  569 */             main.add(492, 60);
/*  570 */             filler.add(146, 24);
/*      */           } 
/*  572 */           if (bridgeType.isSupportType()) {
/*      */ 
/*      */             
/*  575 */             main.add(132, 90);
/*  576 */             main.add(492, 90);
/*  577 */             filler.add(146, 36);
/*      */             
/*  579 */             if (bridgeExtensions > 0) {
/*      */               
/*  581 */               foundations.add(132, 30 * bridgeExtensions);
/*  582 */               foundations.add(492, 30 * bridgeExtensions);
/*  583 */               ballast.add(146, 12 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */           
/*  587 */           roadway.add(132, 2);
/*      */ 
/*      */           
/*  590 */           wcs = bridgeType.wallCount();
/*  591 */           if (wcs > 0)
/*      */           {
/*  593 */             wall.add(132, 10 * wcs);
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         case MARBLE:
/*  599 */           if (bridgeType.isAbutment()) {
/*      */             
/*  601 */             main.add(786, 40);
/*  602 */             main.add(492, 40);
/*  603 */             filler.add(146, 16);
/*      */           } 
/*  605 */           if (bridgeType.isBracing()) {
/*      */             
/*  607 */             main.add(786, 25);
/*  608 */             main.add(492, 25);
/*  609 */             filler.add(146, 8);
/*      */           } 
/*  611 */           if (bridgeType.isCrown()) {
/*      */             
/*  613 */             main.add(786, 10);
/*  614 */             main.add(492, 10);
/*  615 */             keystone.add(906, 1);
/*  616 */             filler.add(146, 4);
/*      */           } 
/*  618 */           if (bridgeType.isFloating()) {
/*      */             
/*  620 */             main.add(786, 10);
/*  621 */             main.add(492, 10);
/*  622 */             filler.add(146, 4);
/*      */           } 
/*  624 */           if (bridgeType.isDoubleBracing()) {
/*      */             
/*  626 */             main.add(786, 30);
/*  627 */             main.add(492, 30);
/*  628 */             filler.add(146, 12);
/*      */           } 
/*  630 */           if (bridgeType.isDoubleAbutment()) {
/*      */             
/*  632 */             main.add(786, 60);
/*  633 */             main.add(492, 60);
/*  634 */             filler.add(146, 24);
/*      */           } 
/*  636 */           if (bridgeType.isSupportType()) {
/*      */ 
/*      */             
/*  639 */             main.add(786, 90);
/*  640 */             main.add(492, 90);
/*  641 */             filler.add(146, 36);
/*      */             
/*  643 */             if (bridgeExtensions > 0) {
/*      */               
/*  645 */               foundations.add(786, 30 * bridgeExtensions);
/*  646 */               foundations.add(492, 30 * bridgeExtensions);
/*  647 */               ballast.add(146, 12 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */           
/*  651 */           roadway.add(786, 2);
/*      */ 
/*      */           
/*  654 */           wcm = bridgeType.wallCount();
/*  655 */           if (wcm > 0)
/*      */           {
/*  657 */             wall.add(786, 10 * wcm);
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         case SLATE:
/*  663 */           if (bridgeType.isAbutment()) {
/*      */             
/*  665 */             main.add(1123, 40);
/*  666 */             main.add(492, 40);
/*  667 */             filler.add(146, 16);
/*      */           } 
/*  669 */           if (bridgeType.isBracing()) {
/*      */             
/*  671 */             main.add(1123, 25);
/*  672 */             main.add(492, 25);
/*  673 */             filler.add(146, 8);
/*      */           } 
/*  675 */           if (bridgeType.isCrown()) {
/*      */             
/*  677 */             main.add(1123, 10);
/*  678 */             main.add(492, 10);
/*  679 */             keystone.add(1302, 1);
/*  680 */             filler.add(146, 4);
/*      */           } 
/*  682 */           if (bridgeType.isFloating()) {
/*      */             
/*  684 */             main.add(1123, 10);
/*  685 */             main.add(492, 10);
/*  686 */             filler.add(146, 4);
/*      */           } 
/*  688 */           if (bridgeType.isDoubleBracing()) {
/*      */             
/*  690 */             main.add(1123, 30);
/*  691 */             main.add(492, 30);
/*  692 */             filler.add(146, 12);
/*      */           } 
/*  694 */           if (bridgeType.isDoubleAbutment()) {
/*      */             
/*  696 */             main.add(1123, 60);
/*  697 */             main.add(492, 60);
/*  698 */             filler.add(146, 24);
/*      */           } 
/*  700 */           if (bridgeType.isSupportType()) {
/*      */ 
/*      */             
/*  703 */             main.add(1123, 90);
/*  704 */             main.add(492, 90);
/*  705 */             filler.add(146, 36);
/*      */             
/*  707 */             if (bridgeExtensions > 0) {
/*      */               
/*  709 */               foundations.add(1123, 30 * bridgeExtensions);
/*  710 */               foundations.add(492, 30 * bridgeExtensions);
/*  711 */               ballast.add(146, 12 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */           
/*  715 */           roadway.add(1123, 2);
/*      */ 
/*      */           
/*  718 */           wcm = bridgeType.wallCount();
/*  719 */           if (wcm > 0)
/*      */           {
/*  721 */             wall.add(1123, 10 * wcm);
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         case ROUNDED_STONE:
/*  727 */           if (bridgeType.isAbutment()) {
/*      */             
/*  729 */             main.add(1122, 40);
/*  730 */             main.add(492, 40);
/*  731 */             filler.add(146, 16);
/*      */           } 
/*  733 */           if (bridgeType.isBracing()) {
/*      */             
/*  735 */             main.add(1122, 25);
/*  736 */             main.add(492, 25);
/*  737 */             filler.add(146, 8);
/*      */           } 
/*  739 */           if (bridgeType.isCrown()) {
/*      */             
/*  741 */             main.add(1122, 10);
/*  742 */             main.add(492, 10);
/*  743 */             keystone.add(905, 1);
/*  744 */             filler.add(146, 4);
/*      */           } 
/*  746 */           if (bridgeType.isFloating()) {
/*      */             
/*  748 */             main.add(1122, 10);
/*  749 */             main.add(492, 10);
/*  750 */             filler.add(146, 4);
/*      */           } 
/*  752 */           if (bridgeType.isDoubleBracing()) {
/*      */             
/*  754 */             main.add(1122, 30);
/*  755 */             main.add(492, 30);
/*  756 */             filler.add(146, 12);
/*      */           } 
/*  758 */           if (bridgeType.isDoubleAbutment()) {
/*      */             
/*  760 */             main.add(1122, 60);
/*  761 */             main.add(492, 60);
/*  762 */             filler.add(146, 24);
/*      */           } 
/*  764 */           if (bridgeType.isSupportType()) {
/*      */ 
/*      */             
/*  767 */             main.add(1122, 90);
/*  768 */             main.add(492, 90);
/*  769 */             filler.add(146, 36);
/*      */             
/*  771 */             if (bridgeExtensions > 0) {
/*      */               
/*  773 */               foundations.add(1122, 30 * bridgeExtensions);
/*  774 */               foundations.add(492, 30 * bridgeExtensions);
/*  775 */               ballast.add(146, 12 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */           
/*  779 */           roadway.add(1122, 2);
/*      */ 
/*      */           
/*  782 */           wcm = bridgeType.wallCount();
/*  783 */           if (wcm > 0)
/*      */           {
/*  785 */             wall.add(1122, 10 * wcm);
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         case POTTERY:
/*  791 */           if (bridgeType.isAbutment()) {
/*      */             
/*  793 */             main.add(776, 40);
/*  794 */             main.add(492, 40);
/*  795 */             filler.add(146, 16);
/*      */           } 
/*  797 */           if (bridgeType.isBracing()) {
/*      */             
/*  799 */             main.add(776, 25);
/*  800 */             main.add(492, 25);
/*  801 */             filler.add(146, 8);
/*      */           } 
/*  803 */           if (bridgeType.isCrown()) {
/*      */             
/*  805 */             main.add(776, 10);
/*  806 */             main.add(492, 10);
/*  807 */             keystone.add(1304, 1);
/*  808 */             filler.add(146, 4);
/*      */           } 
/*  810 */           if (bridgeType.isFloating()) {
/*      */             
/*  812 */             main.add(776, 10);
/*  813 */             main.add(492, 10);
/*  814 */             filler.add(146, 4);
/*      */           } 
/*  816 */           if (bridgeType.isDoubleBracing()) {
/*      */             
/*  818 */             main.add(776, 30);
/*  819 */             main.add(492, 30);
/*  820 */             filler.add(146, 12);
/*      */           } 
/*  822 */           if (bridgeType.isDoubleAbutment()) {
/*      */             
/*  824 */             main.add(776, 60);
/*  825 */             main.add(492, 60);
/*  826 */             filler.add(146, 24);
/*      */           } 
/*  828 */           if (bridgeType.isSupportType()) {
/*      */ 
/*      */             
/*  831 */             main.add(776, 90);
/*  832 */             main.add(492, 90);
/*  833 */             filler.add(146, 36);
/*      */             
/*  835 */             if (bridgeExtensions > 0) {
/*      */               
/*  837 */               foundations.add(776, 30 * bridgeExtensions);
/*  838 */               foundations.add(492, 30 * bridgeExtensions);
/*  839 */               ballast.add(146, 12 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */           
/*  843 */           roadway.add(776, 2);
/*      */ 
/*      */           
/*  846 */           wcm = bridgeType.wallCount();
/*  847 */           if (wcm > 0)
/*      */           {
/*  849 */             wall.add(776, 10 * wcm);
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         case SANDSTONE:
/*  855 */           if (bridgeType.isAbutment()) {
/*      */             
/*  857 */             main.add(1121, 40);
/*  858 */             main.add(492, 40);
/*  859 */             filler.add(146, 16);
/*      */           } 
/*  861 */           if (bridgeType.isBracing()) {
/*      */             
/*  863 */             main.add(1121, 25);
/*  864 */             main.add(492, 25);
/*  865 */             filler.add(146, 8);
/*      */           } 
/*  867 */           if (bridgeType.isCrown()) {
/*      */             
/*  869 */             main.add(1121, 10);
/*  870 */             main.add(492, 10);
/*  871 */             keystone.add(1305, 1);
/*  872 */             filler.add(146, 4);
/*      */           } 
/*  874 */           if (bridgeType.isFloating()) {
/*      */             
/*  876 */             main.add(1121, 10);
/*  877 */             main.add(492, 10);
/*  878 */             filler.add(146, 4);
/*      */           } 
/*  880 */           if (bridgeType.isDoubleBracing()) {
/*      */             
/*  882 */             main.add(1121, 30);
/*  883 */             main.add(492, 30);
/*  884 */             filler.add(146, 12);
/*      */           } 
/*  886 */           if (bridgeType.isDoubleAbutment()) {
/*      */             
/*  888 */             main.add(1121, 60);
/*  889 */             main.add(492, 60);
/*  890 */             filler.add(146, 24);
/*      */           } 
/*  892 */           if (bridgeType.isSupportType()) {
/*      */ 
/*      */             
/*  895 */             main.add(1121, 90);
/*  896 */             main.add(492, 90);
/*  897 */             filler.add(146, 36);
/*      */             
/*  899 */             if (bridgeExtensions > 0) {
/*      */               
/*  901 */               foundations.add(1121, 30 * bridgeExtensions);
/*  902 */               foundations.add(492, 30 * bridgeExtensions);
/*  903 */               ballast.add(146, 12 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */           
/*  907 */           roadway.add(1121, 2);
/*      */ 
/*      */           
/*  910 */           wcm = bridgeType.wallCount();
/*  911 */           if (wcm > 0)
/*      */           {
/*  913 */             wall.add(1121, 10 * wcm);
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         case RENDERED:
/*  919 */           if (bridgeType.isAbutment()) {
/*      */             
/*  921 */             main.add(132, 40);
/*  922 */             main.add(492, 40);
/*  923 */             main.add(130, 4);
/*  924 */             filler.add(146, 16);
/*      */           } 
/*  926 */           if (bridgeType.isBracing()) {
/*      */             
/*  928 */             main.add(132, 25);
/*  929 */             main.add(492, 25);
/*  930 */             main.add(130, 2);
/*  931 */             filler.add(146, 8);
/*      */           } 
/*  933 */           if (bridgeType.isCrown()) {
/*      */             
/*  935 */             main.add(132, 10);
/*  936 */             main.add(492, 10);
/*  937 */             main.add(130, 1);
/*  938 */             keystone.add(905, 1);
/*  939 */             filler.add(146, 4);
/*      */           } 
/*  941 */           if (bridgeType.isFloating()) {
/*      */             
/*  943 */             main.add(132, 10);
/*  944 */             main.add(492, 10);
/*  945 */             main.add(130, 1);
/*  946 */             filler.add(146, 4);
/*      */           } 
/*  948 */           if (bridgeType.isDoubleBracing()) {
/*      */             
/*  950 */             main.add(132, 30);
/*  951 */             main.add(492, 30);
/*  952 */             main.add(130, 3);
/*  953 */             filler.add(146, 12);
/*      */           } 
/*  955 */           if (bridgeType.isDoubleAbutment()) {
/*      */             
/*  957 */             main.add(132, 60);
/*  958 */             main.add(492, 60);
/*  959 */             main.add(130, 6);
/*  960 */             filler.add(146, 24);
/*      */           } 
/*  962 */           if (bridgeType.isSupportType()) {
/*      */ 
/*      */             
/*  965 */             main.add(132, 90);
/*  966 */             main.add(492, 90);
/*  967 */             main.add(130, 9);
/*  968 */             filler.add(146, 36);
/*      */             
/*  970 */             if (bridgeExtensions > 0) {
/*      */               
/*  972 */               foundations.add(132, 30 * bridgeExtensions);
/*  973 */               foundations.add(492, 30 * bridgeExtensions);
/*  974 */               foundations.add(130, 3 * bridgeExtensions);
/*  975 */               ballast.add(146, 12 * bridgeExtensions);
/*      */             } 
/*      */           } 
/*      */           
/*  979 */           roadway.add(132, 2);
/*  980 */           roadway.add(130, 1);
/*      */ 
/*      */           
/*  983 */           wcm = bridgeType.wallCount();
/*  984 */           if (wcm > 0) {
/*      */             
/*  986 */             wall.add(132, 10 * wcm);
/*  987 */             wall.add(130, wcm);
/*      */           } 
/*      */           break;
/*      */ 
/*      */         
/*      */         case ROPE:
/*  993 */           if (bridgeType.isAbutment()) {
/*      */             
/*  995 */             main.add(557, 2);
/*  996 */             main.add(9, 4);
/*  997 */             main.add(558, 2);
/*      */           } 
/*  999 */           if (bridgeType.isDoubleAbutment()) {
/*      */ 
/*      */             
/* 1002 */             main.add(557, 4);
/* 1003 */             main.add(9, 8);
/* 1004 */             main.add(558, 4);
/*      */           } 
/* 1006 */           if (bridgeType.isCrown())
/*      */           {
/* 1008 */             main.add(558, 2);
/*      */           }
/*      */           
/* 1011 */           roadway.add(22, 10);
/*      */ 
/*      */ 
/*      */           
/* 1015 */           wall.add(558, 4);
/* 1016 */           wall.add(559, 4);
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1022 */       if (roadway.getBuildMaterials().size() > 0) {
/*      */         
/* 1024 */         int stageNo = 1;
/*      */ 
/*      */         
/* 1027 */         main.setStageNumber(stageNo++);
/* 1028 */         toReturn.add(main);
/* 1029 */         if (foundations.getBuildMaterials().size() > 0) {
/*      */           
/* 1031 */           foundations.setStageNumber(stageNo++);
/* 1032 */           toReturn.add(foundations);
/*      */         } 
/* 1034 */         if (ballast.getBuildMaterials().size() > 0) {
/*      */           
/* 1036 */           ballast.setStageNumber(stageNo++);
/* 1037 */           toReturn.add(ballast);
/*      */         } 
/* 1039 */         if (filler.getBuildMaterials().size() > 0) {
/*      */           
/* 1041 */           filler.setStageNumber(stageNo++);
/* 1042 */           toReturn.add(filler);
/*      */         } 
/* 1044 */         if (keystone.getBuildMaterials().size() > 0) {
/*      */           
/* 1046 */           keystone.setStageNumber(stageNo++);
/* 1047 */           toReturn.add(keystone);
/*      */         } 
/* 1049 */         if (roadway.getBuildMaterials().size() > 0) {
/*      */           
/* 1051 */           roadway.setStageNumber(stageNo++);
/* 1052 */           toReturn.add(roadway);
/*      */         } 
/* 1054 */         if (wall.getBuildMaterials().size() > 0)
/*      */         {
/* 1056 */           wall.setStageNumber(stageNo++);
/* 1057 */           toReturn.add(wall);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1062 */         logger.log(Level.WARNING, "Someone tried to make a bridge but the material choice was not supported (" + bridgeMaterial
/*      */             
/* 1064 */             .toString() + ")");
/*      */       } 
/* 1066 */     } catch (NoSuchTemplateException nste) {
/*      */       
/* 1068 */       logger.log(Level.WARNING, "BridgePartBehaviour.getRequiredMaterials trying to use material that have a non existing template.", (Throwable)nste);
/*      */     } 
/*      */ 
/*      */     
/* 1072 */     return toReturn;
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
/*      */   static final boolean isOkToBuild(Creature performer, Item tool, BridgePart bridgePart) {
/* 1086 */     if (bridgePart == null) {
/*      */       
/* 1088 */       performer.getCommunicator().sendNormalServerMessage("You fail to focus, and cannot find that bridge part.");
/*      */       
/* 1090 */       return false;
/*      */     } 
/*      */     
/* 1093 */     if (!hasValidTool(bridgePart.getMaterial(), tool)) {
/*      */       
/* 1095 */       performer.getCommunicator().sendNormalServerMessage("You need to activate the correct building tool if you want to build that.");
/*      */       
/* 1097 */       return false;
/*      */     } 
/*      */     
/* 1100 */     Skill buildSkill = getBuildSkill(bridgePart.getType(), bridgePart.getMaterial(), performer);
/* 1101 */     if (buildSkill.getKnowledge(0.0D) < bridgePart.minRequiredSkill()) {
/*      */       
/* 1103 */       performer.getCommunicator().sendNormalServerMessage("You are not skilled enough to build that.");
/* 1104 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1108 */     if (performer.getTileX() == bridgePart.getTileX() && performer.getTileY() == bridgePart.getTileY()) {
/*      */       
/* 1110 */       performer.getCommunicator().sendNormalServerMessage("You cannot work on the bridge part when standing on the same tile.");
/*      */       
/* 1112 */       return false;
/*      */     } 
/*      */     
/* 1115 */     if (performer.getBridgeId() == bridgePart.getStructureId()) {
/* 1116 */       return true;
/*      */     }
/*      */     
/* 1119 */     if (!bridgePart.hasAnExit()) {
/*      */       
/* 1121 */       performer.getCommunicator().sendNormalServerMessage("You cannot work on the bridge part when not standing on the bridge.");
/*      */       
/* 1123 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1127 */     boolean ok = false;
/* 1128 */     if (performer.getTileX() == bridgePart.getTileX() && performer.getTileY() + 1 == bridgePart.getTileY() && bridgePart
/* 1129 */       .hasNorthExit()) {
/* 1130 */       ok = true;
/* 1131 */     } else if (performer.getTileX() == bridgePart.getTileX() && performer.getTileY() - 1 == bridgePart.getTileY() && bridgePart
/* 1132 */       .hasSouthExit()) {
/* 1133 */       ok = true;
/* 1134 */     } else if (performer.getTileX() + 1 == bridgePart.getTileX() && performer.getTileY() == bridgePart.getTileY() && bridgePart
/* 1135 */       .hasWestExit()) {
/* 1136 */       ok = true;
/* 1137 */     } else if (performer.getTileX() - 1 == bridgePart.getTileX() && performer.getTileY() == bridgePart.getTileY() && bridgePart
/* 1138 */       .hasEastExit()) {
/* 1139 */       ok = true;
/*      */     } 
/* 1141 */     if (!ok) {
/*      */       
/* 1143 */       performer.getCommunicator().sendNormalServerMessage("You cannot work on the bridge part when not standing next to it.");
/*      */       
/* 1145 */       return false;
/*      */     } 
/*      */     
/* 1148 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean bridgePartBuilding(Action act, Creature performer, Item source, BridgePart bridgePart, short action, float counter) {
/* 1159 */     if (!isOkToBuild(performer, source, bridgePart)) {
/* 1160 */       return true;
/*      */     }
/* 1162 */     int time = 10;
/*      */ 
/*      */     
/* 1165 */     boolean insta = (((Servers.isThisATestServer() && performer.getPower() > 1) || performer.getPower() >= 4) && (source.getTemplateId() == 315 || source.getTemplateId() == 176));
/* 1166 */     if (bridgePart.isFinished()) {
/*      */       
/* 1168 */       performer.getCommunicator().sendNormalServerMessage("The " + bridgePart.getName() + " is finished already.");
/* 1169 */       performer.getCommunicator().sendActionResult(false);
/* 1170 */       return true;
/*      */     } 
/* 1172 */     if (!Methods.isActionAllowed(performer, (short)116, bridgePart.getTileX(), bridgePart.getTileY()))
/*      */     {
/*      */       
/* 1175 */       return true;
/*      */     }
/* 1177 */     if (counter == 1.0F) {
/*      */ 
/*      */       
/*      */       try {
/* 1181 */         Structures.getStructure(bridgePart.getStructureId());
/*      */       }
/* 1183 */       catch (NoSuchStructureException e) {
/*      */         
/* 1185 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 1186 */         performer.getCommunicator().sendNormalServerMessage("Your sensitive mind notices a wrongness in the fabric of space.");
/*      */         
/* 1188 */         performer.getCommunicator().sendActionResult(false);
/* 1189 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1193 */       if (!advanceNextState(performer, bridgePart, act, true) && !insta) {
/*      */         
/* 1195 */         BuildAllMaterials bam = getRequiredMaterials(bridgePart);
/* 1196 */         BuildStageMaterials bsm = bam.getBuildStageMaterials(bridgePart.getState());
/* 1197 */         String message = bsm.getRequiredMaterialString(false);
/* 1198 */         performer.getCommunicator().sendNormalServerMessage("You need " + message + " to start building the " + bridgePart
/* 1199 */             .getName() + " of " + bridgePart
/* 1200 */             .getMaterial().getName().toLowerCase() + ".");
/* 1201 */         performer.getCommunicator().sendActionResult(false);
/* 1202 */         return true;
/*      */       } 
/*      */       
/* 1205 */       Skill buildSkill = getBuildSkill(bridgePart.getType(), bridgePart.getMaterial(), performer);
/* 1206 */       time = Actions.getSlowActionTime(performer, buildSkill, source, 0.0D);
/* 1207 */       act.setTimeLeft(time);
/* 1208 */       performer.getStatus().modifyStamina(-1000.0F);
/* 1209 */       damageTool(performer, bridgePart, source);
/*      */       
/* 1211 */       Server.getInstance().broadCastAction(performer.getName() + " continues to build a " + bridgePart.getName() + ".", performer, 5);
/*      */       
/* 1213 */       performer.getCommunicator().sendNormalServerMessage("You continue to build a " + bridgePart.getName() + ".");
/* 1214 */       if (!insta) {
/* 1215 */         performer.sendActionControl("Building a " + bridgePart.getName(), true, time);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1220 */       time = act.getTimeLeft();
/* 1221 */       if (act.currentSecond() % 5 == 0) {
/*      */         
/* 1223 */         SoundPlayer.playSound(bridgePart.getSoundByMaterial(), bridgePart.getTileX(), bridgePart.getTileY(), performer
/* 1224 */             .isOnSurface(), 1.6F);
/* 1225 */         performer.getStatus().modifyStamina(-1000.0F);
/* 1226 */         damageTool(performer, bridgePart, source);
/*      */       } 
/*      */     } 
/*      */     
/* 1230 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 1232 */       BuildAllMaterials bam = getRequiredMaterials(bridgePart);
/* 1233 */       BuildStageMaterials bsm = bam.getBuildStageMaterials(bridgePart.getState());
/* 1234 */       String message = bsm.getRequiredMaterialString(false);
/* 1235 */       if (!advanceNextState(performer, bridgePart, act, false) && !insta) {
/*      */         
/* 1237 */         performer.getCommunicator().sendNormalServerMessage("You need " + message + " to build the " + bridgePart
/* 1238 */             .getName() + ".");
/* 1239 */         performer.getCommunicator().sendActionResult(false);
/* 1240 */         return true;
/*      */       } 
/* 1242 */       double bonus = 0.0D;
/* 1243 */       Skill toolSkill = getToolSkill(bridgePart, performer, source);
/* 1244 */       if (toolSkill != null) {
/*      */         
/* 1246 */         toolSkill.skillCheck(10.0D, source, 0.0D, false, counter);
/* 1247 */         bonus = toolSkill.getKnowledge(source, 0.0D) / 10.0D;
/*      */       } 
/* 1249 */       Skill buildSkill = getBuildSkill(bridgePart.getType(), bridgePart.getMaterial(), performer);
/*      */       
/* 1251 */       double check = buildSkill.skillCheck(buildSkill.getRealKnowledge(), source, bonus, false, counter);
/* 1252 */       int inc = 1;
/* 1253 */       if (WurmPermissions.mayUseGMWand(performer) && (source
/* 1254 */         .getTemplateId() == 315 || source.getTemplateId() == 176))
/*      */       {
/* 1256 */         if (Servers.isThisATestServer()) {
/* 1257 */           inc = 100;
/*      */         } else {
/* 1259 */           inc = 10;
/*      */         } 
/*      */       }
/* 1262 */       bridgePart.buildProgress(inc);
/* 1263 */       bam.setNeeded(bridgePart.getState(), bridgePart.getMaterialCount());
/* 1264 */       Server.getInstance().broadCastAction(performer
/* 1265 */           .getName() + " attaches " + message + " to a " + bridgePart.getName() + ".", performer, 5);
/* 1266 */       performer.getCommunicator()
/* 1267 */         .sendNormalServerMessage("You attach " + message + " to a " + bridgePart.getName() + ".");
/*      */ 
/*      */       
/* 1270 */       float divider = bam.getTotalQuantityRequired();
/* 1271 */       float oldql = bridgePart.getQualityLevel();
/* 1272 */       float qlevel = Math.max(1.0F, 
/* 1273 */           (float)Math.min((act.getPower() + oldql), oldql + buildSkill.getKnowledge(0.0D) / divider));
/* 1274 */       bridgePart.setQualityLevel(qlevel);
/*      */       
/* 1276 */       if (bsm.isStageComplete(bridgePart)) {
/*      */         
/* 1278 */         performer.getCommunicator().sendNormalServerMessage(bsm.getName() + " stage completed!");
/*      */         
/* 1280 */         if (bam.getStageCount() - 1 >= bridgePart.getState() + 1) {
/*      */ 
/*      */           
/* 1283 */           bridgePart.incBridgePartStage();
/*      */         }
/*      */         else {
/*      */           
/* 1287 */           if (insta) {
/*      */             
/* 1289 */             bridgePart.setQualityLevel(80.0F);
/* 1290 */             if (!Servers.isThisATestServer()) {
/* 1291 */               logger.info("Insta Bridge: " + performer.getName() + " completed bridge part at " + bridgePart
/* 1292 */                   .getTileX() + "," + bridgePart.getTileY());
/*      */             }
/*      */           } 
/* 1295 */           bridgePart.setBridgePartState(BridgeConstants.BridgeState.COMPLETED);
/*      */         } 
/*      */         
/* 1298 */         VolaTile bridgeTile = Zones.getOrCreateTile(bridgePart.getTileX(), bridgePart.getTileY(), bridgePart.isOnSurface());
/* 1299 */         bridgeTile.updateBridgePart(bridgePart);
/*      */         
/* 1301 */         if (bam.getStageCount() - 1 < bridgePart.getState()) {
/*      */           
/* 1303 */           Server.getInstance().broadCastAction(performer
/* 1304 */               .getName() + " completes a " + bridgePart.getName() + ".", performer, 5);
/* 1305 */           performer.getCommunicator().sendNormalServerMessage(bridgePart.getName() + " completed!");
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 1311 */         bridgePart.save();
/* 1312 */         if (bridgePart.getState() == BridgeConstants.BridgeState.COMPLETED.getCode())
/*      */         {
/* 1314 */           Structure structure = Structures.getStructure(bridgePart.getStructureId());
/* 1315 */           if (!structure.isFinished())
/*      */           {
/* 1317 */             if (structure.updateStructureFinishFlag())
/*      */             {
/* 1319 */               if (bridgePart.getMaterial() == BridgeConstants.BridgeMaterial.ROPE) {
/* 1320 */                 performer.achievement(358);
/* 1321 */               } else if (bridgePart.getMaterial() == BridgeConstants.BridgeMaterial.WOOD) {
/* 1322 */                 performer.achievement(359);
/* 1323 */               } else if (bridgePart.getMaterial() == BridgeConstants.BridgeMaterial.BRICK) {
/* 1324 */                 performer.achievement(360);
/* 1325 */               } else if (bridgePart.getMaterial() == BridgeConstants.BridgeMaterial.MARBLE) {
/* 1326 */                 performer.achievement(361);
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1342 */       catch (IOException e) {
/*      */         
/* 1344 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       }
/* 1346 */       catch (NoSuchStructureException e) {
/*      */ 
/*      */         
/* 1349 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/*      */       
/* 1352 */       if (bridgePart.isFinished()) {
/* 1353 */         performer.getCommunicator().sendRemoveFromCreationWindow(bridgePart.getId());
/*      */       } else {
/* 1355 */         performer.getCommunicator().sendAddBridgePartToCreationWindow(bridgePart, bridgePart.getId());
/* 1356 */       }  performer.getCommunicator().sendActionResult(true);
/*      */       
/* 1358 */       return true;
/*      */     } 
/*      */     
/* 1361 */     return false;
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
/*      */   public static final Skill getBuildSkill(BridgeConstants.BridgeType bridgepartType, BridgeConstants.BridgeMaterial bridgeMaterial, Creature performer) {
/* 1377 */     return performer.getSkills().getSkillOrLearn(getRequiredSkill(bridgeMaterial));
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
/*      */   private static boolean hasValidTool(BridgeConstants.BridgeMaterial bridgeMaterial, Item source) {
/* 1391 */     if (bridgeMaterial == BridgeConstants.BridgeMaterial.ROPE)
/* 1392 */       return true; 
/* 1393 */     if (source == null || bridgeMaterial == null) {
/* 1394 */       return false;
/*      */     }
/* 1396 */     int tid = source.getTemplateId();
/* 1397 */     boolean hasRightTool = false;
/*      */     
/* 1399 */     switch (bridgeMaterial) {
/*      */       
/*      */       case BRICK:
/* 1402 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       case WOOD:
/* 1405 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case MARBLE:
/*      */       case SLATE:
/*      */       case ROUNDED_STONE:
/*      */       case POTTERY:
/*      */       case SANDSTONE:
/*      */       case RENDERED:
/* 1413 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       default:
/* 1416 */         logger.log(Level.WARNING, "Enum value '" + bridgeMaterial
/*      */ 
/*      */             
/* 1419 */             .toString() + "' added to BridgeMaterial but not to a switch statement in method BridgePartBehaviour.hasValidTool()");
/*      */         break;
/*      */     } 
/*      */     
/* 1423 */     if (tid == 315)
/* 1424 */       return true; 
/* 1425 */     if (tid == 176) {
/* 1426 */       return true;
/*      */     }
/* 1428 */     return hasRightTool;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean actionDestroyBridgePart(Action act, Creature performer, Item source, BridgePart bridgePart, short action, float counter) {
/* 1434 */     if (!Methods.isActionAllowed(performer, (short)116, bridgePart.getTileX(), bridgePart.getTileY()))
/*      */     {
/* 1436 */       return true;
/*      */     }
/* 1438 */     if (MethodsHighways.onHighway(bridgePart)) {
/*      */       
/* 1440 */       performer.getCommunicator().sendNormalServerMessage("That bridge part is protected by the highway.");
/* 1441 */       return true;
/*      */     } 
/* 1443 */     if (bridgePart.getBridgePartState().isBeingBuilt()) {
/*      */       
/* 1445 */       if ((WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/* 1446 */         WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315))
/*      */       {
/* 1448 */         return maybeDestroy(performer, bridgePart);
/*      */       }
/* 1450 */       return MethodsStructure.destroyFloor(action, performer, source, (IFloor)bridgePart, counter);
/*      */     } 
/* 1452 */     if (bridgePart.getBridgePartState() == BridgeConstants.BridgeState.COMPLETED) {
/*      */       
/* 1454 */       if ((WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/* 1455 */         WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315))
/*      */       {
/* 1457 */         return maybeDestroy(performer, bridgePart);
/*      */       }
/*      */       
/* 1460 */       return MethodsStructure.destroyFloor(action, performer, source, (IFloor)bridgePart, counter);
/*      */     } 
/* 1462 */     if (bridgePart.getBridgePartState() == BridgeConstants.BridgeState.PLANNED)
/*      */     {
/* 1464 */       return maybeDestroy(performer, bridgePart);
/*      */     }
/*      */     
/* 1467 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean maybeDestroy(Creature performer, BridgePart bridgePart) {
/* 1472 */     String wand = "";
/* 1473 */     String effort = "";
/* 1474 */     if (performer.getPower() > 1) {
/*      */       
/* 1476 */       wand = " with your magic wand";
/* 1477 */       effort = " effortlessly";
/*      */     } 
/*      */     
/* 1480 */     bridgePart.setBridgePartState(BridgeConstants.BridgeState.PLANNED);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1485 */       Structure structure = Structures.getStructure(bridgePart.getStructureId());
/* 1486 */       if (structure.isBridgeGone()) {
/*      */         
/* 1488 */         performer.getCommunicator().sendNormalServerMessage("The last parts of the bridge falls down with a crash.");
/*      */ 
/*      */         
/* 1491 */         Server.getInstance().broadCastAction(performer
/* 1492 */             .getName() + " cheers as the last parts of the bridge fall down with a crash.", performer, 5);
/*      */         
/* 1494 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1498 */       bridgePart.revertToPlan();
/* 1499 */       performer.getCommunicator().sendNormalServerMessage("You revert the " + bridgePart
/* 1500 */           .getName() + " back to planning stage" + wand + ".");
/* 1501 */       Server.getInstance().broadCastAction(performer
/* 1502 */           .getName() + effort + " reverts the " + bridgePart.getName() + wand + ".", performer, 3);
/*      */ 
/*      */       
/* 1505 */       return true;
/*      */     }
/* 1507 */     catch (NoSuchStructureException nss) {
/*      */       
/* 1509 */       if (performer.getPower() > 1)
/* 1510 */         performer.getCommunicator().sendNormalServerMessage("Could not find the bridge that " + bridgePart
/* 1511 */             .getName() + " belongs to."); 
/* 1512 */       return true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, boolean onSurface, BridgePart bridgePart, int encodedTile, short action, float counter) {
/* 1520 */     if (action == 1)
/*      */     {
/* 1522 */       return examine(performer, bridgePart);
/*      */     }
/* 1524 */     if (action == 607) {
/*      */       
/* 1526 */       if (!bridgePart.isFinished())
/*      */       {
/* 1528 */         performer.getCommunicator().sendAddBridgePartToCreationWindow(bridgePart, -10L);
/*      */       }
/* 1530 */       return true;
/*      */     } 
/* 1532 */     if (action == 59) {
/*      */ 
/*      */       
/* 1535 */       Structure bridge = Structures.getBridge(bridgePart.getStructureId());
/* 1536 */       if (bridge == null) {
/*      */         
/* 1538 */         performer.getCommunicator().sendNormalServerMessage("Could not find the bridge that " + bridgePart
/* 1539 */             .getName() + " belongs to.");
/*      */ 
/*      */       
/*      */       }
/* 1543 */       else if (bridge.isOwner(performer) || performer.getPower() >= 2) {
/*      */         
/* 1545 */         int maxSize = 40;
/*      */         
/* 1547 */         TextInputQuestion tiq = new TextInputQuestion(performer, "Setting description for " + bridge.getName() + ".", "Set the new description:", 1, bridge.getWurmId(), 40, false);
/*      */ 
/*      */         
/* 1550 */         tiq.setOldtext(bridge.getName());
/* 1551 */         tiq.sendQuestion();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1556 */     return super.action(act, performer, bridgePart.getTileX(), bridgePart.getTileY(), onSurface, 
/* 1557 */         Zones.getTileIntForTile(bridgePart.getTileX(), bridgePart.getTileY(), 0), action, counter);
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
/*      */   public boolean action(Action act, Creature performer, Item source, boolean onSurface, BridgePart bridgePart, int encodedTile, short action, float counter) {
/* 1570 */     byte roadType = bridgePart.getRoadType();
/* 1571 */     if (action == 1)
/*      */     {
/* 1573 */       return examine(performer, bridgePart);
/*      */     }
/* 1575 */     if (action == 607) {
/*      */       
/* 1577 */       if (!bridgePart.isFinished())
/*      */       {
/* 1579 */         performer.getCommunicator().sendAddBridgePartToCreationWindow(bridgePart, -10L);
/*      */       }
/* 1581 */       return true;
/*      */     } 
/* 1583 */     if (source == null)
/*      */     {
/*      */       
/* 1586 */       return super.action(act, performer, bridgePart.getTileX(), bridgePart.getTileY(), onSurface, 
/* 1587 */           Zones.getTileIntForTile(bridgePart.getTileX(), bridgePart.getTileY(), 0), action, counter);
/*      */     }
/* 1589 */     if (action == 179) {
/*      */       
/* 1591 */       if ((source.getTemplateId() == 176 || source.getTemplateId() == 315) && 
/* 1592 */         WurmPermissions.mayUseGMWand(performer))
/*      */       {
/* 1594 */         Methods.sendSummonQuestion(performer, source, bridgePart.getTileX(), bridgePart.getTileY(), bridgePart
/* 1595 */             .getStructureId());
/*      */       }
/* 1597 */       return true;
/*      */     } 
/* 1599 */     if (action == 684) {
/*      */       
/* 1601 */       if ((source.getTemplateId() == 315 || source.getTemplateId() == 176) && performer
/* 1602 */         .getPower() >= 2) {
/*      */         
/* 1604 */         Methods.sendItemRestrictionManagement(performer, (Permissions.IAllow)bridgePart, bridgePart.getId());
/*      */       } else {
/*      */         
/* 1607 */         logger.log(Level.WARNING, performer.getName() + " hacking the protocol by trying to set the restrictions of " + bridgePart + ", counter: " + counter + '!');
/*      */       } 
/*      */       
/* 1610 */       return true;
/*      */     } 
/* 1612 */     if (action == 472) {
/*      */       
/* 1614 */       if (source.getTemplateId() == 676 && source.getOwnerId() == performer.getWurmId()) {
/*      */ 
/*      */ 
/*      */         
/* 1618 */         MissionManager m = new MissionManager(performer, "Manage missions", "Select action", bridgePart.getId(), bridgePart.getName(), source.getWurmId());
/* 1619 */         m.sendQuestion();
/*      */       } 
/* 1621 */       return true;
/*      */     } 
/* 1623 */     if (bridgePart.isFinished() && bridgePart.getMaterial() != BridgeConstants.BridgeMaterial.ROPE && bridgePart.getMaterial() != BridgeConstants.BridgeMaterial.WOOD) {
/*      */       
/* 1625 */       if (action == 155 && (source
/* 1626 */         .getTemplateId() == 492 || (
/* 1627 */         WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/* 1628 */         WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) && roadType == 0)
/*      */       {
/*      */         
/* 1631 */         return changeFloor(act, performer, source, bridgePart, action, counter);
/*      */       }
/* 1633 */       if (action == 155 && source.isPaveable() && roadType == Tiles.Tile.TILE_PREPARED_BRIDGE.id)
/*      */       {
/* 1635 */         return changeFloor(act, performer, source, bridgePart, action, counter);
/*      */       }
/* 1637 */       if (action == 191 && (source
/* 1638 */         .getTemplateId() == 1115 || (
/* 1639 */         WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/* 1640 */         WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) && 
/* 1641 */         Tiles.isRoadType(roadType))
/*      */       {
/* 1643 */         return changeFloor(act, performer, source, bridgePart, action, counter);
/*      */       }
/* 1645 */       if (action == 191 && (source
/* 1646 */         .getTemplateId() == 97 || (
/* 1647 */         WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/* 1648 */         WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) && roadType == Tiles.Tile.TILE_PREPARED_BRIDGE.id)
/*      */       {
/*      */         
/* 1651 */         return changeFloor(act, performer, source, bridgePart, action, counter);
/*      */       }
/* 1653 */       if (action == 231 && source.getTemplateId() == 153 && roadType == Tiles.Tile.TILE_PLANKS.id)
/*      */       {
/* 1655 */         return changeFloor(act, performer, source, bridgePart, action, counter);
/*      */       }
/* 1657 */       if (MethodsHighways.onHighway(bridgePart) && source.isPaveable() && roadType != 0 && roadType != Tiles.Tile.TILE_PREPARED_BRIDGE.id)
/*      */       {
/*      */ 
/*      */         
/* 1661 */         return changeFloor(act, performer, source, bridgePart, action, counter);
/*      */       }
/*      */     } 
/* 1664 */     if (!isConstructionAction(action))
/*      */     {
/*      */       
/* 1667 */       return super.action(act, performer, source, bridgePart.getTileX(), bridgePart.getTileY(), onSurface, bridgePart
/* 1668 */           .getHeightOffset(), 
/* 1669 */           Zones.getTileIntForTile(bridgePart.getTileX(), bridgePart.getTileY(), 0), action, counter);
/*      */     }
/*      */     
/* 1672 */     if (action == 524 && !bridgePart.isIndestructible())
/*      */     {
/* 1674 */       return actionDestroyBridgePart(act, performer, source, bridgePart, action, counter);
/*      */     }
/* 1676 */     if (action == 193 && !bridgePart.isNoRepair())
/*      */     {
/* 1678 */       return MethodsStructure.repairFloor(performer, source, (IFloor)bridgePart, counter, act);
/*      */     }
/* 1680 */     if (action == 192 && !bridgePart.isNoImprove())
/*      */     {
/* 1682 */       return MethodsStructure.improveFloor(performer, source, (IFloor)bridgePart, counter, act);
/*      */     }
/* 1684 */     if (action == 169 || action - 20000 >= 0) {
/*      */       
/* 1686 */       if (buildAction(act, performer, source, bridgePart, action, counter)) {
/* 1687 */         return true;
/*      */       }
/* 1689 */       return false;
/*      */     } 
/* 1691 */     if (action == 82)
/*      */     {
/* 1693 */       if ((source.getTemplateId() == 176 || source.getTemplateId() == 315) && 
/* 1694 */         WurmPermissions.mayUseGMWand(performer) && performer.getPower() >= 2) {
/*      */         
/*      */         try {
/*      */           
/* 1698 */           Structure struct = Structures.getStructure(bridgePart.getStructureId());
/* 1699 */           performer.getLogger().log(Level.INFO, performer
/*      */               
/* 1701 */               .getName() + " destroyed bridge " + struct.getName() + " at " + bridgePart.getTileX() + ", " + bridgePart
/*      */               
/* 1703 */               .getTileY());
/*      */ 
/*      */           
/* 1706 */           Zones.flash(bridgePart.getTileX(), bridgePart.getTileY(), false);
/* 1707 */           struct.totallyDestroy();
/*      */         }
/* 1709 */         catch (NoSuchStructureException nss) {
/*      */           
/* 1711 */           logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/*      */         } 
/*      */       }
/*      */     }
/*      */     
/* 1716 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean isConstructionAction(short action) {
/* 1721 */     if (action - 20000 >= 0) {
/* 1722 */       return true;
/*      */     }
/* 1724 */     switch (action) {
/*      */       
/*      */       case 82:
/*      */       case 169:
/*      */       case 192:
/*      */       case 193:
/*      */       case 524:
/* 1731 */         return true;
/*      */     } 
/* 1733 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean examine(Creature performer, BridgePart bridgePart) {
/* 1743 */     BridgeConstants.BridgeState bpState = bridgePart.getBridgePartState();
/* 1744 */     if (bpState.isBeingBuilt()) {
/*      */       
/* 1746 */       BuildAllMaterials bam = getRequiredMaterials(bridgePart);
/*      */       
/* 1748 */       BuildStageMaterials bsm = bam.getBuildStageMaterials(bpState.getCode());
/* 1749 */       String allmaterials = bam.getRequiredMaterialString(true);
/* 1750 */       String stagematerials = bsm.getRequiredMaterialString(true);
/* 1751 */       performer.getCommunicator().sendNormalServerMessage("You see a " + bridgePart
/* 1752 */           .getName() + " under construction. The " + bridgePart.getName() + " requires " + allmaterials + " to be finished.");
/*      */ 
/*      */       
/* 1755 */       performer.getCommunicator().sendNormalServerMessage("The current stage being constructed is " + bridgePart
/* 1756 */           .getFloorStageAsString() + "of " + bam
/* 1757 */           .getStageCountAsString() + " and requires " + stagematerials + " to be completed.");
/*      */       
/* 1759 */       sendQlString(performer, bridgePart);
/*      */     }
/* 1761 */     else if (bpState == BridgeConstants.BridgeState.PLANNED) {
/*      */       
/* 1763 */       BuildAllMaterials bam = getRequiredMaterials(bridgePart);
/*      */       
/* 1765 */       BuildStageMaterials bsm = bam.getBuildStageMaterials(BridgeConstants.BridgeState.STAGE1.getCode());
/* 1766 */       String allmaterials = bam.getRequiredMaterialString(true);
/* 1767 */       String stagematerials = bsm.getRequiredMaterialString(true);
/* 1768 */       performer.getCommunicator().sendNormalServerMessage("You see plans for a " + bridgePart
/* 1769 */           .getName() + ". The " + bridgePart.getName() + " requires " + allmaterials + " to be finished.");
/*      */ 
/*      */       
/* 1772 */       performer.getCommunicator().sendNormalServerMessage("There are " + bam
/* 1773 */           .getStageCountAsString() + " stages and the initial stage requires " + stagematerials + " to be completed.");
/*      */       
/* 1775 */       sendQlString(performer, bridgePart);
/*      */     }
/*      */     else {
/*      */       
/* 1779 */       performer.getCommunicator().sendNormalServerMessage("It is a " + bridgePart
/* 1780 */           .getName() + ".");
/* 1781 */       sendQlString(performer, bridgePart);
/* 1782 */       int templateId = bridgePart.getRepairItemTemplate();
/*      */       
/*      */       try {
/* 1785 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(templateId);
/* 1786 */         if (bridgePart.getDamage() > 0.0F)
/*      */         {
/* 1788 */           performer.getCommunicator().sendNormalServerMessage("It needs to be repaired with " + template
/* 1789 */               .getNameWithGenus() + ".");
/*      */         }
/*      */         else
/*      */         {
/* 1793 */           performer.getCommunicator().sendNormalServerMessage("It can be improved with " + template
/* 1794 */               .getNameWithGenus() + ".");
/*      */         }
/*      */       
/* 1797 */       } catch (NoSuchTemplateException e) {
/*      */ 
/*      */         
/* 1800 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/* 1802 */       if (bridgePart.getRoadType() != 0) {
/*      */         
/* 1804 */         Tiles.Tile tile = Tiles.getTile(bridgePart.getRoadType());
/* 1805 */         if (bridgePart.getRoadType() == Tiles.Tile.TILE_PREPARED_BRIDGE.id) {
/* 1806 */           performer.getCommunicator().sendNormalServerMessage("The surface has been " + tile.getDesc().toLowerCase() + ".");
/*      */         } else {
/* 1808 */           performer.getCommunicator().sendNormalServerMessage("The surface has been paved with " + tile.getDesc().toLowerCase() + ".");
/*      */         } 
/*      */       } 
/* 1811 */     }  if (performer.getPower() >= 2) {
/*      */       
/* 1813 */       Structure bridge = Structures.getBridge(bridgePart.getStructureId());
/* 1814 */       if (bridge != null) {
/* 1815 */         performer.getCommunicator().sendNormalServerMessage("Planned by " + bridge.getPlanner() + ".");
/*      */       }
/*      */     } 
/* 1818 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendQlString(Creature performer, BridgePart bridgePart) {
/* 1823 */     performer.getCommunicator()
/* 1824 */       .sendNormalServerMessage("QL = " + bridgePart.getCurrentQL() + ", dam=" + bridgePart.getDamage() + ".");
/* 1825 */     if (performer.getPower() > 0)
/*      */     {
/* 1827 */       performer.getCommunicator().sendNormalServerMessage("id: " + bridgePart
/* 1828 */           .getId() + " @" + bridgePart.getTileX() + "," + bridgePart.getTileY() + " height: " + bridgePart
/* 1829 */           .getHeightOffset() + " " + bridgePart
/* 1830 */           .getMaterial().getName() + " " + bridgePart.getType().getName() + " (" + bridgePart
/* 1831 */           .getBridgePartState().toString().toLowerCase() + ").");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void damageTool(Creature performer, BridgePart bridgePart, Item source) {
/* 1837 */     if (source.getTemplateId() == 63) {
/* 1838 */       source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 1839 */     } else if (source.getTemplateId() == 62) {
/* 1840 */       source.setDamage(source.getDamage() + 3.0E-4F * source.getDamageModifier());
/* 1841 */     } else if (source.getTemplateId() == 493) {
/* 1842 */       source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Skill getToolSkill(BridgePart bridgePart, Creature performer, Item source) {
/* 1847 */     if (bridgePart.getMaterial() == BridgeConstants.BridgeMaterial.ROPE)
/*      */     {
/*      */       
/* 1850 */       return null;
/*      */     }
/*      */     
/*      */     try {
/* 1854 */       return performer.getSkills().getSkillOrLearn(source.getPrimarySkill());
/*      */     }
/* 1856 */     catch (NoSuchSkillException e) {
/*      */       
/* 1858 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean changeFloor(Action act, Creature performer, Item source, BridgePart bridgePart, short action, float counter) {
/*      */     byte newTileType;
/* 1865 */     int pavingItem = source.getTemplateId();
/* 1866 */     if (!Methods.isActionAllowed(performer, action, bridgePart.getTileX(), bridgePart.getTileY()))
/*      */     {
/* 1868 */       return true;
/*      */     }
/* 1870 */     byte roadType = bridgePart.getRoadType();
/* 1871 */     String prepared = "";
/* 1872 */     String finished = "";
/* 1873 */     int checkSkill = 0;
/* 1874 */     boolean insta = false;
/* 1875 */     if (performer.getPower() >= 2 && (source.getTemplateId() == 176 || source.getTemplateId() == 315))
/*      */     {
/* 1877 */       insta = true;
/*      */     }
/* 1879 */     if (pavingItem == 492 || (insta && roadType == 0)) {
/*      */ 
/*      */       
/* 1882 */       if (!insta && source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */         
/* 1884 */         performer.getCommunicator().sendNormalServerMessage("It takes " + (source
/* 1885 */             .getTemplate().getWeightGrams() / 1000) + "kg of " + source.getName() + " to prepare the bridge part.");
/* 1886 */         return true;
/*      */       } 
/* 1888 */       prepared = "prepare the bridge part";
/* 1889 */       finished = "The bridge part roadway is prepared now.";
/* 1890 */       checkSkill = 10031;
/*      */     }
/* 1892 */     else if (pavingItem == 153) {
/*      */ 
/*      */       
/* 1895 */       if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */         
/* 1897 */         performer.getCommunicator().sendNormalServerMessage("It takes " + (source
/* 1898 */             .getTemplate().getWeightGrams() / 1000) + "kg of " + source.getName() + " to colour the floorboards.");
/* 1899 */         return true;
/*      */       } 
/* 1901 */       prepared = "colour the floorboards";
/* 1902 */       finished = "The floorboards are now coated with tar.";
/*      */     }
/* 1904 */     else if (pavingItem == 97 || (insta && roadType == Tiles.Tile.TILE_PREPARED_BRIDGE.id)) {
/*      */ 
/*      */       
/* 1907 */       prepared = "remove the mortar";
/* 1908 */       finished = "The mortar has been removed.";
/* 1909 */       checkSkill = 10031;
/*      */     }
/* 1911 */     else if (pavingItem == 1115 || (insta && Tiles.isRoadType(roadType))) {
/*      */ 
/*      */       
/* 1914 */       prepared = "remove the paving";
/* 1915 */       finished = "The paving has been removed.";
/* 1916 */       checkSkill = 1009;
/*      */     }
/*      */     else {
/*      */       
/* 1920 */       if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*      */         
/* 1922 */         performer.getCommunicator().sendNormalServerMessage("The amount of " + source
/* 1923 */             .getName() + " is too little to pave. You may need to combine them with other " + source.getTemplate().getPlural() + ".");
/* 1924 */         return true;
/*      */       } 
/* 1926 */       if (Tiles.isRoadType(roadType)) {
/*      */         
/* 1928 */         if (performer.getStrengthSkill() < 20.0D) {
/*      */           
/* 1930 */           performer.getCommunicator().sendNormalServerMessage("You need to be stronger to replace pavement.");
/* 1931 */           return true;
/*      */         } 
/* 1933 */         prepared = "repave the bridge part";
/*      */       } else {
/*      */         
/* 1936 */         prepared = "pave the prepared bridge part";
/* 1937 */       }  finished = "The bridge part is now paved.";
/* 1938 */       checkSkill = 10031;
/*      */     } 
/* 1940 */     if (counter == 1.0F && !insta) {
/*      */       
/* 1942 */       Skill skill = performer.getSkills().getSkillOrLearn(checkSkill);
/* 1943 */       if (checkSkill == 1009 && skill.getRealKnowledge() < 10.0D) {
/*      */         
/* 1945 */         if (roadType == Tiles.Tile.TILE_PLANKS.id || roadType == Tiles.Tile.TILE_PLANKS_TARRED.id) {
/* 1946 */           performer
/* 1947 */             .getCommunicator()
/* 1948 */             .sendNormalServerMessage("You can't figure out how to remove the floor boards. You must become a bit better at digging first.");
/*      */         } else {
/*      */           
/* 1951 */           performer
/* 1952 */             .getCommunicator()
/* 1953 */             .sendNormalServerMessage("You can't figure out how to remove the stone. You must become a bit better at digging first.");
/*      */         } 
/* 1955 */         return true;
/*      */       } 
/* 1957 */       int i = 500;
/* 1958 */       if (pavingItem == 1115 || pavingItem == 97) {
/* 1959 */         i = Actions.getDestroyActionTime(performer, skill, source, 0.0D);
/*      */       } else {
/* 1961 */         i = Actions.getStandardActionTime(performer, skill, source, 0.0D);
/* 1962 */       }  act.setTimeLeft(i);
/* 1963 */       if (pavingItem == 519) {
/* 1964 */         performer.getCommunicator().sendNormalServerMessage("You break up the collosus brick and start to " + prepared + ".");
/*      */       } else {
/* 1966 */         performer.getCommunicator().sendNormalServerMessage("You start to " + prepared + " with the " + source.getName() + ".");
/* 1967 */       }  Server.getInstance().broadCastAction(performer.getName() + " starts to " + prepared + ".", performer, 5);
/* 1968 */       performer.sendActionControl(act.getActionEntry().getVerbString(), true, i);
/* 1969 */       performer.getStatus().modifyStamina(-1000.0F);
/* 1970 */       return false;
/*      */     } 
/*      */     
/* 1973 */     int time = act.getTimeLeft();
/*      */     
/* 1975 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 1977 */       performer.getStatus().modifyStamina(-10000.0F);
/*      */     }
/* 1979 */     if (act.mayPlaySound())
/*      */     {
/* 1981 */       Methods.sendSound(performer, "sound.work.paving");
/*      */     }
/*      */     
/* 1984 */     if (counter * 10.0F <= time && !insta)
/*      */     {
/* 1986 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1990 */     Skill paving = performer.getSkills().getSkillOrLearn(checkSkill);
/* 1991 */     paving.skillCheck((pavingItem == 146) ? 5.0D : 30.0D, source, 0.0D, false, counter);
/*      */     
/* 1993 */     TileEvent.log(bridgePart.getTileX(), bridgePart.getTileY(), -1, performer.getWurmId(), action);
/*      */ 
/*      */ 
/*      */     
/* 1997 */     switch (pavingItem) {
/*      */       
/*      */       case 176:
/*      */       case 315:
/* 2001 */         newTileType = 0;
/* 2002 */         if (roadType == 0)
/*      */         {
/* 2004 */           newTileType = Tiles.Tile.TILE_PREPARED_BRIDGE.id;
/*      */         }
/*      */         break;
/*      */       case 492:
/* 2008 */         newTileType = Tiles.Tile.TILE_PREPARED_BRIDGE.id;
/*      */         break;
/*      */       case 97:
/* 2011 */         newTileType = 0;
/*      */         break;
/*      */       case 1115:
/* 2014 */         newTileType = 0;
/*      */         break;
/*      */       case 132:
/* 2017 */         newTileType = Tiles.Tile.TILE_COBBLESTONE.id;
/*      */         break;
/*      */       case 1122:
/* 2020 */         newTileType = Tiles.Tile.TILE_COBBLESTONE_ROUND.id;
/*      */         break;
/*      */       case 519:
/* 2023 */         newTileType = Tiles.Tile.TILE_COBBLESTONE_ROUGH.id;
/*      */         break;
/*      */       case 406:
/* 2026 */         newTileType = Tiles.Tile.TILE_STONE_SLABS.id;
/*      */         break;
/*      */       case 1123:
/* 2029 */         newTileType = Tiles.Tile.TILE_SLATE_BRICKS.id;
/*      */         break;
/*      */       case 771:
/* 2032 */         newTileType = Tiles.Tile.TILE_SLATE_SLABS.id;
/*      */         break;
/*      */       case 1121:
/* 2035 */         newTileType = Tiles.Tile.TILE_SANDSTONE_BRICKS.id;
/*      */         break;
/*      */       case 1124:
/* 2038 */         newTileType = Tiles.Tile.TILE_SANDSTONE_SLABS.id;
/*      */         break;
/*      */       case 787:
/* 2041 */         newTileType = Tiles.Tile.TILE_MARBLE_SLABS.id;
/*      */         break;
/*      */       case 786:
/* 2044 */         newTileType = Tiles.Tile.TILE_MARBLE_BRICKS.id;
/*      */         break;
/*      */       case 776:
/* 2047 */         newTileType = Tiles.Tile.TILE_POTTERY_BRICKS.id;
/*      */         break;
/*      */       case 495:
/* 2050 */         newTileType = Tiles.Tile.TILE_PLANKS.id;
/*      */         break;
/*      */       case 153:
/* 2053 */         newTileType = Tiles.Tile.TILE_PLANKS_TARRED.id;
/*      */         break;
/*      */       default:
/* 2056 */         newTileType = Tiles.Tile.TILE_GRAVEL.id;
/*      */         break;
/*      */     } 
/* 2059 */     bridgePart.saveRoadType(newTileType);
/*      */     
/* 2061 */     if (pavingItem == 492 || pavingItem == 153) {
/*      */ 
/*      */       
/* 2064 */       source.setWeight(source.getWeightGrams() - source.getTemplate().getWeightGrams(), true);
/*      */     }
/* 2066 */     else if (!insta && pavingItem != 97 && pavingItem != 1115) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2071 */       Items.destroyItem(source.getWurmId());
/*      */     } 
/* 2073 */     performer.getCommunicator().sendNormalServerMessage(finished);
/*      */     
/* 2075 */     VolaTile vt = Zones.getOrCreateTile(bridgePart.getTileX(), bridgePart.getTileY(), bridgePart.isOnSurface());
/* 2076 */     if (vt != null)
/* 2077 */       vt.updateBridgePart(bridgePart); 
/* 2078 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\BridgePartBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */