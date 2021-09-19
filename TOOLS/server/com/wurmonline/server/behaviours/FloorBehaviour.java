/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.IFloor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.RoofFloorEnum;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.StructureSupport;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.StructureConstants;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
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
/*      */ public final class FloorBehaviour
/*      */   extends TileBehaviour
/*      */ {
/*      */   FloorBehaviour() {
/*   67 */     super((short)45);
/*      */   }
/*      */ 
/*      */   
/*   71 */   private static final Logger logger = Logger.getLogger(FloorBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, boolean onSurface, Floor floor) {
/*   83 */     List<ActionEntry> toReturn = new LinkedList<>();
/*   84 */     if (!floor.isFinished())
/*   85 */       toReturn.add(Actions.actionEntrys[607]); 
/*   86 */     toReturn.addAll(Actions.getDefaultTileActions());
/*      */     
/*   88 */     toReturn.addAll(getTileAndFloorBehavioursFor(performer, null, floor.getTileX(), floor.getTileY(), Tiles.Tile.TILE_DIRT.id));
/*      */     
/*   90 */     if (floor.getType() == StructureConstants.FloorType.OPENING)
/*      */     {
/*   92 */       if (floor.isFinished()) {
/*      */         
/*   94 */         if (floor.getFloorLevel() == performer.getFloorLevel()) {
/*   95 */           toReturn.add(Actions.actionEntrys[523]);
/*   96 */         } else if (floor.getFloorLevel() == performer.getFloorLevel() + 1) {
/*   97 */           toReturn.add(Actions.actionEntrys[522]);
/*      */         } 
/*   99 */       } else if (floor.getFloorLevel() == performer.getFloorLevel()) {
/*  100 */         toReturn.add(Actions.actionEntrys[523]);
/*      */       } 
/*      */     }
/*  103 */     VolaTile floorTile = Zones.getOrCreateTile(floor.getTileX(), floor.getTileY(), (floor.getLayer() >= 0));
/*  104 */     Structure structure = null;
/*      */     
/*      */     try {
/*  107 */       structure = Structures.getStructure(floor.getStructureId());
/*      */     }
/*  109 */     catch (NoSuchStructureException e) {
/*      */       
/*  111 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*  112 */       return toReturn;
/*      */     } 
/*      */     
/*  115 */     if (MethodsStructure.mayModifyStructure(performer, structure, floorTile, (short)177)) {
/*      */ 
/*      */ 
/*      */       
/*  119 */       toReturn.add(new ActionEntry((short)-2, "Rotate", "rotating"));
/*  120 */       toReturn.add(new ActionEntry((short)177, "Turn clockwise", "turning"));
/*  121 */       toReturn.add(new ActionEntry((short)178, "Turn counterclockwise", "turning"));
/*      */     } 
/*      */     
/*  124 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final List<ActionEntry> getCompletedFloorsBehaviour(boolean andStaircases, boolean onSurface) {
/*  129 */     List<ActionEntry> plantypes = new ArrayList<>();
/*  130 */     plantypes.add(Actions.actionEntrys[508]);
/*  131 */     plantypes.add(Actions.actionEntrys[515]);
/*  132 */     if (andStaircases) {
/*      */       
/*  134 */       plantypes.add(Actions.actionEntrys[659]);
/*  135 */       plantypes.add(Actions.actionEntrys[704]);
/*  136 */       plantypes.add(Actions.actionEntrys[713]);
/*  137 */       plantypes.add(Actions.actionEntrys[714]);
/*  138 */       plantypes.add(Actions.actionEntrys[715]);
/*  139 */       plantypes.add(Actions.actionEntrys[705]);
/*  140 */       plantypes.add(Actions.actionEntrys[706]);
/*  141 */       plantypes.add(Actions.actionEntrys[709]);
/*  142 */       plantypes.add(Actions.actionEntrys[710]);
/*  143 */       plantypes.add(Actions.actionEntrys[711]);
/*  144 */       plantypes.add(Actions.actionEntrys[712]);
/*      */     } 
/*  146 */     plantypes.add(Actions.actionEntrys[509]);
/*      */     
/*  148 */     if (onSurface)
/*  149 */       plantypes.add(Actions.actionEntrys[507]); 
/*  150 */     Collections.sort(plantypes);
/*      */     
/*  152 */     List<ActionEntry> toReturn = new ArrayList<>(5);
/*  153 */     toReturn.add(new ActionEntry((short)-plantypes.size(), "Plan", "planning"));
/*  154 */     toReturn.addAll(plantypes);
/*  155 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, boolean onSurface, Floor floor) {
/*  162 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  163 */     if (!floor.isFinished())
/*  164 */       toReturn.add(Actions.actionEntrys[607]); 
/*  165 */     toReturn.addAll(getTileAndFloorBehavioursFor(performer, source, floor.getTileX(), floor.getTileY(), Tiles.Tile.TILE_DIRT.id));
/*      */ 
/*      */     
/*  168 */     if (floor.getType() == StructureConstants.FloorType.OPENING)
/*      */     {
/*  170 */       if (floor.isFinished()) {
/*      */         
/*  172 */         if (floor.getFloorLevel() == performer.getFloorLevel()) {
/*  173 */           toReturn.add(Actions.actionEntrys[523]);
/*  174 */         } else if (floor.getFloorLevel() == performer.getFloorLevel() + 1) {
/*  175 */           toReturn.add(Actions.actionEntrys[522]);
/*      */         } 
/*  177 */       } else if (floor.getFloorLevel() == performer.getFloorLevel()) {
/*  178 */         toReturn.add(Actions.actionEntrys[523]);
/*      */       } 
/*      */     }
/*  181 */     VolaTile floorTile = Zones.getOrCreateTile(floor.getTileX(), floor.getTileY(), floor.isOnSurface());
/*  182 */     Structure structure = null;
/*      */     
/*      */     try {
/*  185 */       structure = Structures.getStructure(floor.getStructureId());
/*      */     }
/*  187 */     catch (NoSuchStructureException e) {
/*      */       
/*  189 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*  190 */       toReturn.addAll(Actions.getDefaultItemActions());
/*  191 */       return toReturn;
/*      */     } 
/*      */     
/*  194 */     if (MethodsStructure.mayModifyStructure(performer, structure, floorTile, (short)169)) {
/*      */       List<RoofFloorEnum> list;
/*  196 */       switch (floor.getFloorState()) {
/*      */         
/*      */         case WOOD:
/*  199 */           toReturn.add(new ActionEntry((short)169, "Continue building", "building"));
/*      */           break;
/*      */         case CLAY_BRICK:
/*  202 */           if (floor.getType() == StructureConstants.FloorType.ROOF) {
/*      */             
/*  204 */             List<RoofFloorEnum> list1 = RoofFloorEnum.getRoofsByTool(source);
/*  205 */             if (list1.size() > 0) {
/*      */               
/*  207 */               toReturn.add(new ActionEntry((short)-list1.size(), "Build", "building"));
/*  208 */               for (RoofFloorEnum en : list1)
/*      */               {
/*  210 */                 toReturn.add(en.createActionEntry());
/*      */               }
/*      */             } 
/*      */             
/*      */             break;
/*      */           } 
/*  216 */           list = RoofFloorEnum.getFloorByToolAndType(source, floor.getType());
/*  217 */           if (list.size() > 0) {
/*      */             
/*  219 */             toReturn.add(new ActionEntry((short)-list.size(), "Build", "building"));
/*  220 */             for (RoofFloorEnum en : list)
/*      */             {
/*  222 */               toReturn.add(en.createActionEntry());
/*      */             }
/*      */           } 
/*      */           break;
/*      */         
/*      */         case SLATE_SLAB:
/*  228 */           if (floor.getType() != StructureConstants.FloorType.ROOF)
/*      */           {
/*  230 */             toReturn.addAll(getCompletedFloorsBehaviour(true, floor.isOnSurface()));
/*      */           }
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  239 */       toReturn.add(new ActionEntry((short)-2, "Rotate", "rotating"));
/*  240 */       toReturn.add(new ActionEntry((short)177, "Turn clockwise", "turning"));
/*  241 */       toReturn.add(new ActionEntry((short)178, "Turn counterclockwise", "turning"));
/*      */     } 
/*      */ 
/*      */     
/*  245 */     if (!source.isTraded()) {
/*      */       
/*  247 */       if (source.getTemplateId() == floor.getRepairItemTemplate())
/*      */       {
/*  249 */         if (floor.getDamage() > 0.0F) {
/*      */           
/*  251 */           if ((!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0) && 
/*  252 */             !floor.isNoRepair()) {
/*  253 */             toReturn.add(Actions.actionEntrys[193]);
/*      */           }
/*  255 */         } else if (floor.getQualityLevel() < 100.0F) {
/*      */           
/*  257 */           if (!floor.isNoImprove())
/*  258 */             toReturn.add(Actions.actionEntrys[192]); 
/*      */         } 
/*      */       }
/*  261 */       toReturn.addAll(Actions.getDefaultItemActions());
/*  262 */       if (!floor.isIndestructible())
/*      */       {
/*  264 */         if (floor.getType() == StructureConstants.FloorType.ROOF) {
/*      */           
/*  266 */           toReturn.add(Actions.actionEntrys[525]);
/*      */ 
/*      */         
/*      */         }
/*  270 */         else if (!MethodsHighways.onHighway(floor)) {
/*  271 */           toReturn.add(Actions.actionEntrys[524]);
/*      */         } 
/*      */       }
/*  274 */       if ((source.getTemplateId() == 315 || source.getTemplateId() == 176) && performer
/*  275 */         .getPower() >= 2)
/*  276 */         toReturn.add(Actions.actionEntrys[684]); 
/*      */     } 
/*  278 */     return toReturn;
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
/*      */   private boolean buildAction(Action act, Creature performer, Item source, Floor floor, short action, float counter) {
/*      */     boolean autoAdvance;
/*      */     Skill craftSkill;
/*      */     StructureConstants.FloorMaterial newMaterial, oldMaterial;
/*      */     float oldql, qlevel;
/*  297 */     switch (floor.getFloorState()) {
/*      */ 
/*      */       
/*      */       case CLAY_BRICK:
/*  301 */         autoAdvance = (performer.getPower() >= 2 && source.getTemplateId() == 176);
/*  302 */         craftSkill = null;
/*      */         
/*      */         try {
/*  305 */           craftSkill = performer.getSkills().getSkill(1005);
/*      */         }
/*  307 */         catch (NoSuchSkillException nss) {
/*      */           
/*  309 */           craftSkill = performer.getSkills().learn(1005, 1.0F);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  314 */         newMaterial = StructureConstants.FloorMaterial.fromByte((byte)(action - 20000));
/*      */         
/*  316 */         oldMaterial = floor.getMaterial();
/*  317 */         floor.setMaterial(newMaterial);
/*  318 */         if (!isOkToBuild(performer, source, floor, floor.getFloorLevel(), floor
/*  319 */             .isRoof())) {
/*      */           
/*  321 */           floor.setMaterial(oldMaterial);
/*  322 */           return true;
/*      */         } 
/*      */         
/*  325 */         if (!autoAdvance && !advanceNextState(performer, floor, act, true)) {
/*      */           
/*  327 */           String message = buildRequiredMaterialString(floor, false);
/*  328 */           performer.getCommunicator().sendNormalServerMessage("You need " + message + " to start building that.");
/*      */           
/*  330 */           floor.setMaterial(oldMaterial);
/*  331 */           return true;
/*      */         } 
/*  333 */         floor.setFloorState(StructureConstants.FloorState.BUILDING);
/*  334 */         oldql = floor.getQualityLevel();
/*  335 */         qlevel = MethodsStructure.calculateNewQualityLevel(act.getPower(), craftSkill.getKnowledge(0.0D), oldql, 
/*  336 */             getTotalMaterials(floor));
/*  337 */         floor.setQualityLevel(qlevel);
/*      */         
/*      */         try {
/*  340 */           floor.save();
/*      */         }
/*  342 */         catch (IOException e) {
/*      */           
/*  344 */           logger.log(Level.WARNING, e.getMessage(), e);
/*      */         } 
/*  346 */         if (floor.getMaterial() == StructureConstants.FloorMaterial.STANDALONE) {
/*      */           
/*  348 */           performer.getCommunicator().sendNormalServerMessage("You plan a " + floor
/*  349 */               .getName() + ".");
/*      */         }
/*  351 */         else if (floor.getType() == StructureConstants.FloorType.ROOF) {
/*      */           
/*  353 */           performer.getCommunicator().sendNormalServerMessage("You plan a " + floor
/*  354 */               .getName() + " made of " + getMaterialDescription(floor) + ".");
/*      */         }
/*      */         else {
/*      */           
/*  358 */           performer.getCommunicator().sendNormalServerMessage("You plan a " + floor
/*  359 */               .getName() + " made of " + floor.getMaterial().getName().toLowerCase() + ".");
/*      */         } 
/*  361 */         return floorBuilding(act, performer, source, floor, action, counter);
/*      */ 
/*      */       
/*      */       case WOOD:
/*  365 */         if (floorBuilding(act, performer, source, floor, action, counter)) {
/*      */           
/*  367 */           performer.getCommunicator().sendAddFloorRoofToCreationWindow(floor, floor.getId());
/*  368 */           return true;
/*      */         } 
/*      */         
/*  371 */         return false;
/*      */       
/*      */       case SLATE_SLAB:
/*  374 */         logger.log(Level.WARNING, "FloorBehaviour buildAction on a completed floor, it should not happen?!");
/*  375 */         performer.getCommunicator().sendNormalServerMessage("You failed to find anything to do with that.");
/*  376 */         return true;
/*      */     } 
/*  378 */     logger.log(Level.WARNING, "Enum value added to FloorState but not to a switch statement in method FloorBehaviour.action()");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  383 */     return false;
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
/*      */   static boolean advanceNextState(Creature performer, Floor floor, Action act, boolean justCheckIfItemsArePresent) {
/*  397 */     List<BuildMaterial> mats = getRequiredMaterialsAtState(floor);
/*  398 */     if (takeItemsFromCreature(performer, floor, act, mats, justCheckIfItemsArePresent))
/*      */     {
/*  400 */       return true;
/*      */     }
/*      */     
/*  403 */     if (performer.getPower() >= 4 && !justCheckIfItemsArePresent) {
/*      */       
/*  405 */       performer.getCommunicator().sendNormalServerMessage("You magically summon some necessary materials.");
/*  406 */       return true;
/*      */     } 
/*      */     
/*  409 */     return false;
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
/*      */   static boolean takeItemsFromCreature(Creature performer, Floor floor, Action act, List<BuildMaterial> mats, boolean justCheckIfItemsArePresent) {
/*  428 */     Item[] inventoryItems = performer.getInventory().getAllItems(false);
/*  429 */     Item[] bodyItems = performer.getBody().getAllItems();
/*  430 */     List<Item> takeItemsOnSuccess = new ArrayList<>();
/*      */ 
/*      */     
/*  433 */     for (Item item : inventoryItems) {
/*      */       
/*  435 */       for (BuildMaterial mat : mats) {
/*      */         
/*  437 */         if (mat.getNeededQuantity() > 0)
/*      */         {
/*  439 */           if (item.getTemplateId() == mat.getTemplateId() && item.getWeightGrams() >= mat.getWeightGrams()) {
/*      */             
/*  441 */             takeItemsOnSuccess.add(item);
/*  442 */             mat.setNeededQuantity(0);
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  449 */     for (Item item : bodyItems) {
/*      */       
/*  451 */       for (BuildMaterial mat : mats) {
/*      */         
/*  453 */         if (mat.getNeededQuantity() > 0)
/*      */         {
/*  455 */           if (item.getTemplateId() == mat.getTemplateId() && item.getWeightGrams() >= mat.getWeightGrams()) {
/*      */             
/*  457 */             takeItemsOnSuccess.add(item);
/*  458 */             mat.setNeededQuantity(0);
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  464 */     float divider = 1.0F;
/*      */ 
/*      */     
/*  467 */     for (BuildMaterial mat : mats) {
/*      */       
/*  469 */       divider += mat.getTotalQuantityRequired();
/*  470 */       if (mat.getNeededQuantity() > 0)
/*      */       {
/*      */         
/*  473 */         return false;
/*      */       }
/*      */     } 
/*  476 */     float qlevel = 0.0F;
/*  477 */     if (!justCheckIfItemsArePresent)
/*      */     {
/*  479 */       for (Item item : takeItemsOnSuccess) {
/*      */         
/*  481 */         act.setPower(item.getCurrentQualityLevel() / divider);
/*  482 */         performer.sendToLoggers("Adding " + item.getCurrentQualityLevel() + ", divider=" + divider + "=" + act
/*  483 */             .getPower());
/*  484 */         qlevel += item.getCurrentQualityLevel() / 21.0F;
/*  485 */         if (item.isCombine()) {
/*      */           
/*  487 */           item.setWeight(item.getWeightGrams() - item.getTemplate().getWeightGrams(), true);
/*      */           
/*      */           continue;
/*      */         } 
/*  491 */         Items.destroyItem(item.getWurmId());
/*      */       } 
/*      */     }
/*      */     
/*  495 */     act.setPower(qlevel);
/*  496 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getSkillForRoof(StructureConstants.FloorMaterial material) {
/*  501 */     switch (material) {
/*      */       
/*      */       case WOOD:
/*  504 */         return 1005;
/*      */       case CLAY_BRICK:
/*  506 */         return 1013;
/*      */       case SLATE_SLAB:
/*  508 */         return 1013;
/*      */       case STONE_BRICK:
/*  510 */         return 1013;
/*      */       case SANDSTONE_SLAB:
/*  512 */         return 1013;
/*      */       case STONE_SLAB:
/*  514 */         return 1013;
/*      */       case MARBLE_SLAB:
/*  516 */         return 1013;
/*      */       case THATCH:
/*  518 */         return 10092;
/*      */       case METAL_IRON:
/*  520 */         return 10015;
/*      */       case METAL_COPPER:
/*  522 */         return 10015;
/*      */       case METAL_STEEL:
/*  524 */         return 10015;
/*      */       case METAL_SILVER:
/*  526 */         return 10015;
/*      */       case METAL_GOLD:
/*  528 */         return 10015;
/*      */       case STANDALONE:
/*  530 */         return 1005;
/*      */     } 
/*  532 */     return 1005;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getSkillForFloor(StructureConstants.FloorMaterial material) {
/*  538 */     switch (material) {
/*      */       
/*      */       case WOOD:
/*  541 */         return 1005;
/*      */       case CLAY_BRICK:
/*  543 */         return 10031;
/*      */       case SLATE_SLAB:
/*  545 */         return 10031;
/*      */       case STONE_BRICK:
/*  547 */         return 10031;
/*      */       case SANDSTONE_SLAB:
/*  549 */         return 10031;
/*      */       case STONE_SLAB:
/*  551 */         return 10031;
/*      */       case MARBLE_SLAB:
/*  553 */         return 10031;
/*      */       case THATCH:
/*  555 */         return 10092;
/*      */       case METAL_IRON:
/*  557 */         return 10015;
/*      */       case METAL_COPPER:
/*  559 */         return 10015;
/*      */       case METAL_STEEL:
/*  561 */         return 10015;
/*      */       case METAL_SILVER:
/*  563 */         return 10015;
/*      */       case METAL_GOLD:
/*  565 */         return 10015;
/*      */       case STANDALONE:
/*  567 */         return 1005;
/*      */     } 
/*  569 */     return 1005;
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
/*      */   static byte getFinishedState(Floor floor) {
/*  581 */     byte numStates = 0;
/*  582 */     List<BuildMaterial> mats = getRequiredMaterialsFor(floor);
/*      */     
/*  584 */     for (BuildMaterial mat : mats) {
/*      */       
/*  586 */       if (numStates < mat.getTotalQuantityRequired())
/*      */       {
/*      */         
/*  589 */         numStates = (byte)mat.getTotalQuantityRequired();
/*      */       }
/*      */     } 
/*  592 */     if (numStates <= 0)
/*      */     {
/*  594 */       numStates = 1;
/*      */     }
/*      */     
/*  597 */     return numStates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte getTotalMaterials(Floor floor) {
/*  605 */     int total = 0;
/*  606 */     List<BuildMaterial> mats = getRequiredMaterialsFor(floor);
/*  607 */     for (BuildMaterial mat : mats) {
/*      */       
/*  609 */       int totalReq = mat.getTotalQuantityRequired();
/*  610 */       if (totalReq > total) {
/*  611 */         total = totalReq;
/*      */       }
/*      */     } 
/*  614 */     return (byte)total;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final List<BuildMaterial> getRequiredMaterialsForRoof(StructureConstants.FloorMaterial material) {
/*  619 */     List<BuildMaterial> toReturn = new ArrayList<>();
/*      */ 
/*      */ 
/*      */     
/*  623 */     try { switch (material)
/*      */       
/*      */       { case WOOD:
/*  626 */           toReturn.add(new BuildMaterial(790, 10));
/*  627 */           toReturn.add(new BuildMaterial(218, 2));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  659 */           return toReturn;case STONE_BRICK: toReturn.add(new BuildMaterial(132, 10)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case CLAY_BRICK: toReturn.add(new BuildMaterial(778, 10)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case THATCH: toReturn.add(new BuildMaterial(756, 10)); toReturn.add(new BuildMaterial(444, 10)); return toReturn;case SLATE_SLAB: toReturn.add(new BuildMaterial(784, 10)); toReturn.add(new BuildMaterial(492, 5)); return toReturn; }  logger.log(Level.WARNING, "Someone tried to make a roof but the material choice was not supported (" + material.toString() + ")"); } catch (NoSuchTemplateException nste) { logger.log(Level.WARNING, "FloorBehaviour.getRequiredMaterialsAtState trying to use material that have a non existing template.", (Throwable)nste); }  return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<BuildMaterial> getRequiredMaterialsForFloor(StructureConstants.FloorType type, StructureConstants.FloorMaterial material) {
/*  664 */     List<BuildMaterial> toReturn = new ArrayList<>();
/*      */ 
/*      */     
/*  667 */     try { if (type == StructureConstants.FloorType.OPENING)
/*      */       
/*  669 */       { switch (material)
/*      */         
/*      */         { case WOOD:
/*  672 */             toReturn.add(new BuildMaterial(22, 5));
/*  673 */             toReturn.add(new BuildMaterial(218, 1));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  999 */             return toReturn;case STONE_BRICK: toReturn.add(new BuildMaterial(132, 5)); toReturn.add(new BuildMaterial(492, 5)); return toReturn;case SANDSTONE_SLAB: toReturn.add(new BuildMaterial(1124, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case STONE_SLAB: toReturn.add(new BuildMaterial(406, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case CLAY_BRICK: toReturn.add(new BuildMaterial(776, 5)); toReturn.add(new BuildMaterial(492, 5)); return toReturn;case SLATE_SLAB: toReturn.add(new BuildMaterial(771, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case MARBLE_SLAB: toReturn.add(new BuildMaterial(787, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case STANDALONE: toReturn.add(new BuildMaterial(23, 2)); toReturn.add(new BuildMaterial(218, 1)); return toReturn; }  logger.log(Level.WARNING, "Someone tried to make a floor with an opening but the material choice was not supported (" + material.toString() + ")"); } else if (type == StructureConstants.FloorType.WIDE_STAIRCASE) { toReturn.add(new BuildMaterial(22, 30)); toReturn.add(new BuildMaterial(217, 2)); } else if (type == StructureConstants.FloorType.WIDE_STAIRCASE_RIGHT || type == StructureConstants.FloorType.WIDE_STAIRCASE_LEFT) { toReturn.add(new BuildMaterial(22, 30)); toReturn.add(new BuildMaterial(23, 5)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 2)); } else if (type == StructureConstants.FloorType.WIDE_STAIRCASE_BOTH) { toReturn.add(new BuildMaterial(22, 30)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 2)); } else if (type == StructureConstants.FloorType.STAIRCASE || type == StructureConstants.FloorType.RIGHT_STAIRCASE || type == StructureConstants.FloorType.LEFT_STAIRCASE) { switch (material) { case WOOD: toReturn.add(new BuildMaterial(22, 20)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 2)); toReturn.add(new BuildMaterial(217, 1)); return toReturn;case STONE_BRICK: toReturn.add(new BuildMaterial(132, 5)); toReturn.add(new BuildMaterial(492, 5)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); return toReturn;case SANDSTONE_SLAB: toReturn.add(new BuildMaterial(1124, 2)); toReturn.add(new BuildMaterial(492, 10)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); return toReturn;case STONE_SLAB: toReturn.add(new BuildMaterial(406, 2)); toReturn.add(new BuildMaterial(492, 10)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); return toReturn;case CLAY_BRICK: toReturn.add(new BuildMaterial(776, 5)); toReturn.add(new BuildMaterial(492, 5)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); return toReturn;case SLATE_SLAB: toReturn.add(new BuildMaterial(771, 2)); toReturn.add(new BuildMaterial(492, 10)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); return toReturn;case MARBLE_SLAB: toReturn.add(new BuildMaterial(787, 2)); toReturn.add(new BuildMaterial(492, 10)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); return toReturn;case STANDALONE: toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 10)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); return toReturn; }  logger.log(Level.WARNING, "Someone tried to make a staircase with an opening but the material choice was not supported (" + material.toString() + ")"); } else if (type == StructureConstants.FloorType.CLOCKWISE_STAIRCASE || type == StructureConstants.FloorType.ANTICLOCKWISE_STAIRCASE) { switch (material) { case WOOD: toReturn.add(new BuildMaterial(22, 20)); toReturn.add(new BuildMaterial(218, 2)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 20)); return toReturn;case STONE_BRICK: toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 25)); toReturn.add(new BuildMaterial(492, 25)); return toReturn;case SANDSTONE_SLAB: toReturn.add(new BuildMaterial(1124, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn;case STONE_SLAB: toReturn.add(new BuildMaterial(406, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn;case CLAY_BRICK: toReturn.add(new BuildMaterial(776, 5)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 25)); return toReturn;case SLATE_SLAB: toReturn.add(new BuildMaterial(771, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn;case MARBLE_SLAB: toReturn.add(new BuildMaterial(787, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(218, 1)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn; }  logger.log(Level.WARNING, "Someone tried to make a spiral staircase but the material choice was not supported (" + material.toString() + ")"); } else if (type == StructureConstants.FloorType.CLOCKWISE_STAIRCASE_WITH || type == StructureConstants.FloorType.ANTICLOCKWISE_STAIRCASE_WITH) { switch (material) { case WOOD: toReturn.add(new BuildMaterial(22, 20)); toReturn.add(new BuildMaterial(23, 15)); toReturn.add(new BuildMaterial(218, 5)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 20)); return toReturn;case STONE_BRICK: toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 15)); toReturn.add(new BuildMaterial(218, 4)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 25)); toReturn.add(new BuildMaterial(492, 25)); return toReturn;case SANDSTONE_SLAB: toReturn.add(new BuildMaterial(1124, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 15)); toReturn.add(new BuildMaterial(218, 4)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn;case STONE_SLAB: toReturn.add(new BuildMaterial(406, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 15)); toReturn.add(new BuildMaterial(218, 4)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn;case CLAY_BRICK: toReturn.add(new BuildMaterial(776, 5)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 15)); toReturn.add(new BuildMaterial(218, 4)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 25)); return toReturn;case SLATE_SLAB: toReturn.add(new BuildMaterial(771, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 15)); toReturn.add(new BuildMaterial(218, 4)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn;case MARBLE_SLAB: toReturn.add(new BuildMaterial(787, 2)); toReturn.add(new BuildMaterial(22, 15)); toReturn.add(new BuildMaterial(23, 15)); toReturn.add(new BuildMaterial(218, 4)); toReturn.add(new BuildMaterial(217, 1)); toReturn.add(new BuildMaterial(132, 20)); toReturn.add(new BuildMaterial(492, 30)); return toReturn; }  logger.log(Level.WARNING, "Someone tried to make a staircase with an opening but the material choice was not supported (" + material.toString() + ")"); } else { switch (material) { case WOOD: toReturn.add(new BuildMaterial(22, 10)); toReturn.add(new BuildMaterial(218, 2)); return toReturn;case STONE_BRICK: toReturn.add(new BuildMaterial(132, 10)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case SANDSTONE_SLAB: toReturn.add(new BuildMaterial(1124, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case STONE_SLAB: toReturn.add(new BuildMaterial(406, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case CLAY_BRICK: toReturn.add(new BuildMaterial(776, 10)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case SLATE_SLAB: toReturn.add(new BuildMaterial(771, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn;case MARBLE_SLAB: toReturn.add(new BuildMaterial(787, 2)); toReturn.add(new BuildMaterial(492, 10)); return toReturn; }  logger.log(Level.WARNING, "Someone tried to make a floor but the material choice was not supported (" + material.toString() + ")"); }  } catch (NoSuchTemplateException nste) { logger.log(Level.WARNING, "FloorBehaviour.getRequiredMaterialsAtState trying to use material that have a non existing template.", (Throwable)nste); }  return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   static List<BuildMaterial> getRequiredMaterialsFor(Floor floor) {
/* 1004 */     if (floor.getType() == StructureConstants.FloorType.ROOF) {
/* 1005 */       return getRequiredMaterialsForRoof(floor.getMaterial());
/*      */     }
/* 1007 */     return getRequiredMaterialsForFloor(floor.getType(), floor.getMaterial());
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
/*      */   static List<BuildMaterial> getRequiredMaterialsForFloor(Floor floor) {
/* 1020 */     List<BuildMaterial> toReturn = getRequiredMaterialsForFloor(floor.getType(), floor.getMaterial());
/* 1021 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<BuildMaterial> getRequiredMaterialsAtState(Floor floor) {
/* 1026 */     if (floor.getType() == StructureConstants.FloorType.ROOF) {
/* 1027 */       return getRequiredMaterialsAtStateForRoof(floor);
/*      */     }
/* 1029 */     return getRequiredMaterialsAtStateForFloor(floor);
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<BuildMaterial> getRequiredMaterialsAtStateForRoof(Floor floor) {
/* 1034 */     List<BuildMaterial> mats = getRequiredMaterialsForRoof(floor.getMaterial());
/* 1035 */     for (BuildMaterial mat : mats) {
/*      */       
/* 1037 */       int qty = mat.getTotalQuantityRequired();
/*      */       
/* 1039 */       if (floor.getState() > 0) {
/* 1040 */         qty -= floor.getState();
/* 1041 */       } else if (qty < 0) {
/* 1042 */         qty = 0;
/*      */       } 
/* 1044 */       mat.setNeededQuantity(qty);
/*      */     } 
/* 1046 */     return mats;
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<BuildMaterial> getRequiredMaterialsAtStateForFloor(Floor floor) {
/* 1051 */     List<BuildMaterial> mats = getRequiredMaterialsForFloor(floor.getType(), floor.getMaterial());
/* 1052 */     for (BuildMaterial mat : mats) {
/*      */       
/* 1054 */       int qty = mat.getTotalQuantityRequired();
/*      */       
/* 1056 */       if (floor.getState() > 0) {
/* 1057 */         qty -= floor.getState();
/* 1058 */       } else if (qty < 0) {
/* 1059 */         qty = 0;
/*      */       } 
/* 1061 */       mat.setNeededQuantity(qty);
/*      */     } 
/* 1063 */     return mats;
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
/*      */   static final boolean isOkToBuild(Creature performer, Item tool, Floor floor, int floorLevel, boolean roof) {
/* 1078 */     if (tool == null) {
/*      */       
/* 1080 */       performer.getCommunicator().sendNormalServerMessage("You need to activate a building tool if you want to build something.");
/*      */       
/* 1082 */       return false;
/*      */     } 
/*      */     
/* 1085 */     if (floor == null) {
/*      */       
/* 1087 */       performer.getCommunicator().sendNormalServerMessage("You fail to focus, and cannot find that floor.");
/*      */       
/* 1089 */       return false;
/*      */     } 
/* 1091 */     StructureConstants.FloorMaterial floorMaterial = floor.getMaterial();
/* 1092 */     String nameOfWhatIsBeingBuilt = floor.getName();
/*      */     
/* 1094 */     if (!hasValidTool(floor.getMaterial(), tool)) {
/*      */       
/* 1096 */       performer.getCommunicator().sendNormalServerMessage("You need to activate the correct building tool if you want to build that.");
/*      */       
/* 1098 */       return false;
/*      */     } 
/*      */     
/* 1101 */     Skill buildSkill = getBuildSkill(floor.getType(), floorMaterial, performer);
/* 1102 */     if (!mayPlanAtLevel(performer, floorLevel, buildSkill, roof)) {
/* 1103 */       return false;
/*      */     }
/* 1105 */     if (buildSkill.getKnowledge(0.0D) < getRequiredBuildSkillForFloorType(floorMaterial)) {
/*      */       
/* 1107 */       if (floor.getMaterial() == StructureConstants.FloorMaterial.STANDALONE) {
/* 1108 */         performer.getCommunicator().sendNormalServerMessage("You need higher " + buildSkill
/* 1109 */             .getName() + " skill to build " + nameOfWhatIsBeingBuilt + " with " + floor
/* 1110 */             .getMaterial().getName() + ".");
/*      */       } else {
/* 1112 */         performer.getCommunicator().sendNormalServerMessage("You need higher " + buildSkill
/* 1113 */             .getName() + " skill to build " + floor.getMaterial().getName() + " " + nameOfWhatIsBeingBuilt + ".");
/*      */       } 
/* 1115 */       return false;
/*      */     } 
/*      */     
/* 1118 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean mayPlanAtLevel(Creature performer, int floorLevel, Skill buildSkill, boolean roof) {
/* 1124 */     return mayPlanAtLevel(performer, floorLevel, buildSkill, roof, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean mayPlanAtLevel(Creature performer, int floorLevel, Skill buildSkill, boolean roof, boolean sendMessage) {
/* 1130 */     if (buildSkill.getKnowledge(0.0D) < getRequiredBuildSkillForFloorLevel(floorLevel, roof)) {
/*      */       
/* 1132 */       if (sendMessage)
/*      */       {
/* 1134 */         performer.getCommunicator().sendNormalServerMessage("You need higher " + buildSkill
/* 1135 */             .getName() + " skill to build at that height.");
/*      */       }
/* 1137 */       return false;
/*      */     } 
/* 1139 */     return true;
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
/*      */   private static final boolean floorBuilding(Action act, Creature performer, Item source, Floor floor, short action, float counter) {
/* 1151 */     if (performer.isFighting()) {
/*      */       
/* 1153 */       performer.getCommunicator().sendNormalServerMessage("You cannot do that while in combat.");
/* 1154 */       return true;
/*      */     } 
/* 1156 */     if (!isOkToBuild(performer, source, floor, floor.getFloorLevel(), floor.isRoof())) {
/*      */       
/* 1158 */       performer.getCommunicator().sendActionResult(false);
/* 1159 */       return true;
/*      */     } 
/* 1161 */     int time = 10;
/*      */ 
/*      */     
/* 1164 */     boolean insta = ((Servers.isThisATestServer() || performer.getPower() >= 4) && performer.getPower() > 1 && (source.getTemplateId() == 315 || source.getTemplateId() == 176));
/* 1165 */     if (floor.isFinished()) {
/*      */       
/* 1167 */       performer.getCommunicator().sendNormalServerMessage("The " + floor.getName() + " is finished already.");
/* 1168 */       performer.getCommunicator().sendActionResult(false);
/* 1169 */       return true;
/*      */     } 
/* 1171 */     if (!Methods.isActionAllowed(performer, (short)116, floor.getTileX(), floor.getTileY()))
/*      */     {
/*      */       
/* 1174 */       return true;
/*      */     }
/* 1176 */     if (counter == 1.0F) {
/*      */       Structure structure;
/*      */ 
/*      */       
/*      */       try {
/* 1181 */         structure = Structures.getStructure(floor.getStructureId());
/*      */       }
/* 1183 */       catch (NoSuchStructureException e) {
/*      */         
/* 1185 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 1186 */         performer.getCommunicator().sendNormalServerMessage("Your sensitive mind notices a wrongness in the fabric of space.");
/*      */         
/* 1188 */         performer.getCommunicator().sendActionResult(false);
/* 1189 */         return true;
/*      */       } 
/* 1191 */       if (!MethodsStructure.mayModifyStructure(performer, structure, floor.getTile(), action)) {
/*      */         
/* 1193 */         performer.getCommunicator().sendNormalServerMessage("You need permission in order to make modifications to this structure.");
/*      */         
/* 1195 */         performer.getCommunicator().sendActionResult(false);
/* 1196 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1200 */       if (!advanceNextState(performer, floor, act, true) && !insta) {
/*      */         
/* 1202 */         String message = buildRequiredMaterialString(floor, false);
/* 1203 */         if (floor.getType() == StructureConstants.FloorType.WIDE_STAIRCASE) {
/* 1204 */           performer.getCommunicator().sendNormalServerMessage("You need " + message + " to start building the " + floor
/* 1205 */               .getName() + " with " + floor
/* 1206 */               .getMaterial().getName().toLowerCase() + ".");
/*      */         } else {
/* 1208 */           performer.getCommunicator().sendNormalServerMessage("You need " + message + " to start building the " + floor
/* 1209 */               .getName() + " of " + floor
/* 1210 */               .getMaterial().getName().toLowerCase() + ".");
/* 1211 */         }  performer.getCommunicator().sendActionResult(false);
/* 1212 */         return true;
/*      */       } 
/*      */       
/* 1215 */       Skill buildSkill = getBuildSkill(floor.getType(), floor.getMaterial(), performer);
/* 1216 */       time = Actions.getSlowActionTime(performer, buildSkill, source, 0.0D);
/* 1217 */       act.setTimeLeft(time);
/* 1218 */       performer.getStatus().modifyStamina(-1000.0F);
/* 1219 */       damageTool(performer, floor, source);
/*      */       
/* 1221 */       Server.getInstance().broadCastAction(performer.getName() + " continues to build a " + floor.getName() + ".", performer, 5);
/*      */       
/* 1223 */       performer.getCommunicator().sendNormalServerMessage("You continue to build a " + floor.getName() + ".");
/* 1224 */       if (!insta) {
/* 1225 */         performer.sendActionControl("Building a " + floor.getName(), true, time);
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1231 */       time = act.getTimeLeft();
/* 1232 */       if (act.currentSecond() % 5 == 0) {
/*      */         
/* 1234 */         SoundPlayer.playSound(getBuildSound(floor), floor.getTileX(), floor.getTileY(), performer.isOnSurface(), 1.6F);
/* 1235 */         performer.getStatus().modifyStamina(-1000.0F);
/* 1236 */         damageTool(performer, floor, source);
/*      */       } 
/*      */     } 
/*      */     
/* 1240 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 1242 */       String message = buildRequiredMaterialString(floor, false);
/* 1243 */       if (!advanceNextState(performer, floor, act, false) && !insta) {
/*      */         
/* 1245 */         if (floor.getType() == StructureConstants.FloorType.WIDE_STAIRCASE) {
/* 1246 */           performer.getCommunicator().sendNormalServerMessage("You need " + message + " to build the " + floor
/* 1247 */               .getName() + " with " + floor
/* 1248 */               .getMaterial().getName().toLowerCase() + ".");
/*      */         } else {
/* 1250 */           performer.getCommunicator().sendNormalServerMessage("You need " + message + " to build the " + floor
/* 1251 */               .getName() + " of " + floor
/* 1252 */               .getMaterial().getName().toLowerCase() + ".");
/* 1253 */         }  performer.getCommunicator().sendActionResult(false);
/* 1254 */         return true;
/*      */       } 
/* 1256 */       double bonus = 0.0D;
/* 1257 */       Skill toolSkill = getToolSkill(floor, performer, source);
/* 1258 */       if (toolSkill != null) {
/*      */         
/* 1260 */         toolSkill.skillCheck(10.0D, source, 0.0D, false, counter);
/* 1261 */         bonus = toolSkill.getKnowledge(source, 0.0D) / 10.0D;
/*      */       } 
/* 1263 */       Skill buildSkill = getBuildSkill(floor.getType(), floor.getMaterial(), performer);
/*      */ 
/*      */       
/* 1266 */       double check = buildSkill.skillCheck(buildSkill.getRealKnowledge(), source, bonus, false, counter);
/*      */       
/* 1268 */       floor.buildProgress(1);
/* 1269 */       if (WurmPermissions.mayUseGMWand(performer) && (source
/* 1270 */         .getTemplateId() == 315 || source.getTemplateId() == 176) && (
/* 1271 */         Servers.isThisATestServer() || performer.getPower() >= 4)) {
/*      */         
/* 1273 */         if (!Servers.isThisATestServer())
/* 1274 */           performer.sendToLoggers("Building floor with GM powers at [" + floor.getTile().getTileX() + "," + floor.getTile().getTileY() + "] at floor level " + floor
/* 1275 */               .getFloorLevel()); 
/* 1276 */         floor.setFloorState(StructureConstants.FloorState.COMPLETED);
/*      */       } 
/*      */       
/* 1279 */       Server.getInstance().broadCastAction(performer
/* 1280 */           .getName() + " attaches " + message + " to a " + floor.getName() + ".", performer, 5);
/* 1281 */       performer.getCommunicator().sendNormalServerMessage("You attach " + message + " to a " + floor.getName() + ".");
/*      */       
/* 1283 */       float oldql = floor.getQualityLevel();
/* 1284 */       float qlevel = MethodsStructure.calculateNewQualityLevel(act.getPower(), buildSkill.getKnowledge(0.0D), oldql, 
/* 1285 */           getTotalMaterials(floor));
/* 1286 */       qlevel = Math.max(1.0F, qlevel);
/* 1287 */       floor.setQualityLevel(qlevel);
/*      */       
/* 1289 */       if (floor.getState() >= getFinishedState(floor)) {
/*      */         
/* 1291 */         if (insta) {
/* 1292 */           floor.setQualityLevel(80.0F);
/*      */         }
/* 1294 */         floor.setFloorState(StructureConstants.FloorState.COMPLETED);
/* 1295 */         VolaTile floorTile = Zones.getOrCreateTile(floor.getTileX(), floor.getTileY(), (floor.getLayer() >= 0));
/* 1296 */         floorTile.updateFloor(floor);
/* 1297 */         String floorName = Character.toUpperCase(floor.getName().charAt(0)) + floor.getName().substring(1);
/* 1298 */         performer.getCommunicator().sendNormalServerMessage(floorName + " completed!");
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 1303 */         floor.save();
/*      */       }
/* 1305 */       catch (IOException e) {
/*      */         
/* 1307 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */       
/* 1310 */       if (floor.isFinished())
/*      */       {
/* 1312 */         performer.getCommunicator().sendRemoveFromCreationWindow(floor.getId());
/*      */       }
/* 1314 */       performer.getCommunicator().sendActionResult(true);
/*      */       
/* 1316 */       return true;
/*      */     } 
/*      */     
/* 1319 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float getRequiredBuildSkillForFloorLevel(int floorLevel, boolean roof) {
/* 1325 */     int fLevel = roof ? (floorLevel - 1) : floorLevel;
/* 1326 */     if (fLevel <= 0)
/* 1327 */       return 5.0F; 
/* 1328 */     switch (fLevel) {
/*      */       
/*      */       case 1:
/* 1331 */         return 21.0F;
/*      */       case 2:
/* 1333 */         return 30.0F;
/*      */       case 3:
/* 1335 */         return 39.0F;
/*      */       case 4:
/* 1337 */         return 47.0F;
/*      */       case 5:
/* 1339 */         return 55.0F;
/*      */       case 6:
/* 1341 */         return 63.0F;
/*      */       case 7:
/* 1343 */         return 70.0F;
/*      */       case 8:
/* 1345 */         return 77.0F;
/*      */       case 9:
/* 1347 */         return 83.0F;
/*      */       case 10:
/* 1349 */         return 88.0F;
/*      */       case 11:
/* 1351 */         return 92.0F;
/*      */       case 12:
/* 1353 */         return 95.0F;
/*      */       case 13:
/* 1355 */         return 97.0F;
/*      */       case 14:
/* 1357 */         return 98.0F;
/*      */       case 15:
/* 1359 */         return 99.0F;
/*      */     } 
/* 1361 */     return 200.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float getRequiredBuildSkillForFloorType(StructureConstants.FloorMaterial floorMaterial) {
/* 1367 */     switch (floorMaterial) {
/*      */       
/*      */       case WOOD:
/*      */       case STANDALONE:
/* 1371 */         return 5.0F;
/*      */       case THATCH:
/* 1373 */         return 21.0F;
/*      */       case STONE_BRICK:
/* 1375 */         return 21.0F;
/*      */       case SANDSTONE_SLAB:
/* 1377 */         return 21.0F;
/*      */       case STONE_SLAB:
/* 1379 */         return 21.0F;
/*      */       case CLAY_BRICK:
/* 1381 */         return 25.0F;
/*      */       case SLATE_SLAB:
/* 1383 */         return 30.0F;
/*      */       case MARBLE_SLAB:
/* 1385 */         return 40.0F;
/*      */       case METAL_IRON:
/*      */       case METAL_COPPER:
/*      */       case METAL_STEEL:
/*      */       case METAL_SILVER:
/*      */       case METAL_GOLD:
/* 1391 */         return 99.0F;
/*      */     } 
/* 1393 */     return 1.0F;
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
/*      */   public static final Skill getBuildSkill(StructureConstants.FloorType floorType, StructureConstants.FloorMaterial floorMaterial, Creature performer) {
/*      */     int primSkillTemplate;
/* 1410 */     if (floorType == StructureConstants.FloorType.ROOF) {
/*      */       
/* 1412 */       primSkillTemplate = getSkillForRoof(floorMaterial);
/*      */     }
/*      */     else {
/*      */       
/* 1416 */       primSkillTemplate = getSkillForFloor(floorMaterial);
/*      */     } 
/* 1418 */     Skill workSkill = null;
/*      */     
/*      */     try {
/* 1421 */       workSkill = performer.getSkills().getSkill(primSkillTemplate);
/*      */     }
/* 1423 */     catch (NoSuchSkillException nss) {
/*      */       
/* 1425 */       workSkill = performer.getSkills().learn(primSkillTemplate, 1.0F);
/*      */     } 
/*      */     
/* 1428 */     return workSkill;
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
/*      */   public static boolean hasValidTool(StructureConstants.FloorMaterial floorMaterial, Item source) {
/* 1486 */     if (source == null || floorMaterial == null) {
/* 1487 */       return false;
/*      */     }
/* 1489 */     int tid = source.getTemplateId();
/*      */     
/* 1491 */     boolean hasRightTool = false;
/*      */     
/* 1493 */     switch (floorMaterial) {
/*      */       
/*      */       case METAL_STEEL:
/* 1496 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case CLAY_BRICK:
/* 1499 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       case SLATE_SLAB:
/* 1502 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       case STONE_BRICK:
/* 1505 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       case THATCH:
/* 1508 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case WOOD:
/* 1511 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case SANDSTONE_SLAB:
/* 1514 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       case STONE_SLAB:
/* 1517 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       case METAL_COPPER:
/* 1520 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case METAL_IRON:
/* 1523 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case MARBLE_SLAB:
/* 1526 */         hasRightTool = (tid == 493);
/*      */         break;
/*      */       case METAL_GOLD:
/* 1529 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case METAL_SILVER:
/* 1532 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       case STANDALONE:
/* 1535 */         hasRightTool = (tid == 62 || tid == 63);
/*      */         break;
/*      */       default:
/* 1538 */         logger.log(Level.WARNING, "Enum value '" + floorMaterial.toString() + "' added to FloorMaterial but not to a switch statement in method FloorBehaviour.hasValidTool()");
/*      */         break;
/*      */     } 
/*      */     
/* 1542 */     if (tid == 315)
/* 1543 */       return true; 
/* 1544 */     if (tid == 176) {
/* 1545 */       return true;
/*      */     }
/* 1547 */     return hasRightTool;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean actionDestroyFloor(Action act, Creature performer, Item source, Floor floor, short action, float counter) {
/* 1553 */     if (source.getTemplateId() == 824 || source.getTemplateId() == 0) {
/*      */       
/* 1555 */       performer.getCommunicator().sendNormalServerMessage("You will not do any damage to the floor with that.");
/* 1556 */       return true;
/*      */     } 
/* 1558 */     if (floor.isIndestructible()) {
/*      */       
/* 1560 */       performer.getCommunicator().sendNormalServerMessage("That " + floor.getName() + " looks indestructable.");
/* 1561 */       return true;
/*      */     } 
/* 1563 */     if (!Methods.isActionAllowed(performer, act.getNumber(), floor.getTileX(), floor.getTileY()))
/*      */     {
/* 1565 */       return true;
/*      */     }
/* 1567 */     if (action == 524 && MethodsHighways.onHighway(floor)) {
/*      */       
/* 1569 */       performer.getCommunicator().sendNormalServerMessage("That floor is protected by the highway.");
/* 1570 */       return true;
/*      */     } 
/* 1572 */     if (floor.getFloorState() == StructureConstants.FloorState.BUILDING) {
/*      */       
/* 1574 */       if ((WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/* 1575 */         WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) {
/*      */         
/* 1577 */         floor.destroyOrRevertToPlan();
/* 1578 */         performer.getCommunicator().sendNormalServerMessage("You remove a " + floor
/* 1579 */             .getName() + " with your magic wand.");
/* 1580 */         Server.getInstance().broadCastAction(performer
/* 1581 */             .getName() + " effortlessly removes a " + floor.getName() + " with a magic wand.", performer, 3);
/*      */         
/* 1583 */         return true;
/*      */       } 
/*      */       
/* 1586 */       return MethodsStructure.destroyFloor(action, performer, source, (IFloor)floor, counter);
/*      */     } 
/*      */     
/* 1589 */     if (floor.getFloorState() == StructureConstants.FloorState.COMPLETED) {
/*      */       
/* 1591 */       if ((WurmPermissions.mayUseDeityWand(performer) && source.getTemplateId() == 176) || (
/* 1592 */         WurmPermissions.mayUseGMWand(performer) && source.getTemplateId() == 315)) {
/*      */         
/* 1594 */         floor.destroyOrRevertToPlan();
/*      */         
/* 1596 */         performer.getCommunicator().sendNormalServerMessage("You remove a " + floor
/* 1597 */             .getName() + " with your magic wand.");
/* 1598 */         Server.getInstance().broadCastAction(performer
/* 1599 */             .getName() + " effortlessly removes a " + floor.getName() + " with a magic wand.", performer, 3);
/*      */         
/* 1601 */         return true;
/*      */       } 
/*      */       
/* 1604 */       return MethodsStructure.destroyFloor(action, performer, source, (IFloor)floor, counter);
/*      */     } 
/* 1606 */     if (floor.getFloorState() == StructureConstants.FloorState.PLANNING) {
/*      */       
/* 1608 */       VolaTile vtile = Zones.getOrCreateTile(floor.getTileX(), floor.getTileY(), (floor.getLayer() >= 0));
/* 1609 */       Structure structure = vtile.getStructure();
/* 1610 */       if (structure.wouldCreateFlyingStructureIfRemoved((StructureSupport)floor)) {
/*      */         
/* 1612 */         performer.getCommunicator().sendNormalServerMessage("Removing that would cause a collapsing section.");
/* 1613 */         return true;
/*      */       } 
/*      */       
/* 1616 */       floor.destroy();
/* 1617 */       performer.getCommunicator().sendNormalServerMessage("You remove a plan for a new floor.");
/* 1618 */       Server.getInstance().broadCastAction(performer.getName() + " removes a plan for a new floor.", performer, 3);
/* 1619 */       return true;
/*      */     } 
/*      */     
/* 1622 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, boolean onSurface, Floor floor, int encodedTile, short action, float counter) {
/* 1630 */     if (action == 523 || action == 522) {
/*      */       
/* 1632 */       boolean done = false;
/* 1633 */       if (floor.getType() == StructureConstants.FloorType.OPENING && (floor.isFinished() || action == 523)) {
/*      */         
/* 1635 */         if (floor.getFloorLevel() == performer.getFloorLevel()) {
/*      */           
/* 1637 */           if (action != 523)
/* 1638 */             return true; 
/*      */         } else {
/* 1640 */           if (floor.getFloorLevel() != performer.getFloorLevel() + 1)
/* 1641 */             return true; 
/* 1642 */           if (action != 522) {
/* 1643 */             return true;
/*      */           }
/*      */         } 
/*      */       } else {
/* 1647 */         performer.getCommunicator().sendNormalServerMessage("Move a little bit closer to the ladder.", (byte)3);
/* 1648 */         return true;
/*      */       } 
/* 1650 */       if (performer.getVehicle() != -10L) {
/*      */         
/* 1652 */         performer.getCommunicator().sendNormalServerMessage("You can't climb right now.");
/* 1653 */         return true;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1661 */       if ((performer.getFollowers()).length > 0) {
/*      */         
/* 1663 */         performer.getCommunicator().sendNormalServerMessage("You stop leading.", (byte)3);
/* 1664 */         performer.stopLeading();
/*      */       } 
/* 1666 */       if (counter == 1.0F) {
/*      */         
/* 1668 */         float qx = performer.getPosX() % 4.0F;
/* 1669 */         float qy = performer.getPosY() % 4.0F;
/* 1670 */         boolean getCloser = false;
/* 1671 */         if (performer.getTileX() != floor.getTileX() || performer.getTileY() != floor.getTileY()) {
/*      */           
/* 1673 */           performer.getCommunicator().sendNormalServerMessage("You are too far away to climb that.", (byte)3);
/* 1674 */           return true;
/*      */         } 
/* 1676 */         if (floor.getMaterial() == StructureConstants.FloorMaterial.STANDALONE) {
/*      */ 
/*      */           
/* 1679 */           switch (floor.getDir()) {
/*      */ 
/*      */             
/*      */             case 0:
/* 1683 */               getCloser = (qx < 1.0F || qx > 3.0F || qy < 3.0F);
/*      */               break;
/*      */             
/*      */             case 6:
/* 1687 */               getCloser = (qx < 1.0F || qy < 1.0F || qy > 3.0F);
/*      */               break;
/*      */             
/*      */             case 4:
/* 1691 */               getCloser = (qx < 1.0F || qx > 3.0F || qy > 1.0F);
/*      */               break;
/*      */             
/*      */             case 2:
/* 1695 */               getCloser = (qx > 1.0F || qy < 1.0F || qy > 3.0F);
/*      */               break;
/*      */             default:
/* 1698 */               getCloser = true;
/*      */               break;
/*      */           } 
/*      */         
/*      */         } else {
/* 1703 */           getCloser = (qx < 1.0F || qx > 3.0F || qy < 1.0F || qy > 3.0F);
/*      */         } 
/* 1705 */         if (getCloser) {
/*      */           
/* 1707 */           performer.getCommunicator().sendNormalServerMessage("Move a little bit closer to the ladder.", (byte)3);
/* 1708 */           return true;
/*      */         } 
/*      */         
/* 1711 */         performer.sendActionControl("Climbing", true, 22);
/* 1712 */         if (action == 523)
/*      */         {
/* 1714 */           int groundoffset = 3;
/* 1715 */           if (performer.getFloorLevel() - 1 == 0) {
/*      */             
/* 1717 */             VolaTile t = performer.getCurrentTile();
/* 1718 */             if ((t.getFloors(-10, 10)).length == 0)
/*      */             {
/* 1720 */               groundoffset = 0;
/*      */             }
/*      */           }
/*      */           else {
/*      */             
/* 1725 */             VolaTile t = performer.getCurrentTile();
/* 1726 */             int tfloor = (performer.getFloorLevel() - 1) * 30;
/* 1727 */             if ((t.getFloors(tfloor - 10, tfloor + 10)).length == 0) {
/*      */ 
/*      */               
/* 1730 */               performer.getCommunicator().sendNormalServerMessage("You can't climb down there.", (byte)3);
/* 1731 */               return true;
/*      */             } 
/*      */           } 
/* 1734 */           performer.getCommunicator().setGroundOffset(groundoffset + (performer.getFloorLevel() - 1) * 30, false);
/*      */         }
/* 1736 */         else if (action == 522)
/*      */         {
/* 1738 */           performer.getCommunicator().setGroundOffset((performer.getFloorLevel() + 1) * 30, false);
/*      */         }
/*      */       
/* 1741 */       } else if (counter > 2.0F) {
/*      */         
/* 1743 */         done = true;
/*      */       } 
/* 1745 */       return done;
/*      */     } 
/* 1747 */     if (action == 177 || action == 178) {
/*      */       
/* 1749 */       if (!floor.isNotTurnable()) {
/* 1750 */         return MethodsStructure.rotateFloor(performer, floor, counter, act);
/*      */       }
/*      */       
/* 1753 */       performer.getCommunicator().sendNormalServerMessage("Looks like that floor is stuck in place.");
/* 1754 */       return true;
/*      */     } 
/*      */     
/* 1757 */     if (action == 607) {
/*      */       
/* 1759 */       if (!floor.isFinished())
/* 1760 */         performer.getCommunicator().sendAddFloorRoofToCreationWindow(floor, -10L); 
/* 1761 */       return true;
/*      */     } 
/* 1763 */     return super.action(act, performer, floor.getTileX(), floor.getTileY(), onSurface, 
/* 1764 */         Zones.getTileIntForTile(floor.getTileX(), floor.getTileY(), 0), action, counter);
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
/*      */   public boolean action(Action act, Creature performer, Item source, boolean onSurface, Floor floor, int encodedTile, short action, float counter) {
/* 1777 */     if (action == 1)
/*      */     {
/* 1779 */       return examine(performer, floor);
/*      */     }
/* 1781 */     if (action == 607) {
/*      */       
/* 1783 */       if (!floor.isFinished())
/* 1784 */         performer.getCommunicator().sendAddFloorRoofToCreationWindow(floor, -10L); 
/* 1785 */       return true;
/*      */     } 
/* 1787 */     if (action == 523 || action == 522 || action == 177 || action == 178)
/*      */     {
/*      */       
/* 1790 */       return action(act, performer, onSurface, floor, encodedTile, action, counter);
/*      */     }
/* 1792 */     if (source == null)
/*      */     {
/*      */       
/* 1795 */       return super.action(act, performer, floor.getTileX(), floor.getTileY(), onSurface, 
/* 1796 */           Zones.getTileIntForTile(floor.getTileX(), floor.getTileY(), 0), action, counter);
/*      */     }
/* 1798 */     if (action == 179) {
/*      */       
/* 1800 */       if ((source.getTemplateId() == 176 || source.getTemplateId() == 315) && 
/* 1801 */         WurmPermissions.mayUseGMWand(performer))
/*      */       {
/* 1803 */         Methods.sendSummonQuestion(performer, source, floor.getTileX(), floor.getTileY(), floor.getStructureId());
/*      */       }
/* 1805 */       return true;
/*      */     } 
/* 1807 */     if (action == 684) {
/*      */       
/* 1809 */       if ((source.getTemplateId() == 315 || source.getTemplateId() == 176) && performer
/* 1810 */         .getPower() >= 2) {
/*      */         
/* 1812 */         Methods.sendItemRestrictionManagement(performer, (Permissions.IAllow)floor, floor.getId());
/*      */       } else {
/*      */         
/* 1815 */         logger.log(Level.WARNING, performer.getName() + " hacking the protocol by trying to set the restrictions of " + floor + ", counter: " + counter + '!');
/*      */       } 
/*      */       
/* 1818 */       return true;
/*      */     } 
/* 1820 */     if (!isConstructionAction(action))
/*      */     {
/*      */       
/* 1823 */       return super.action(act, performer, source, floor.getTileX(), floor.getTileY(), onSurface, floor
/* 1824 */           .getHeightOffset(), Zones.getTileIntForTile(floor.getTileX(), floor.getTileY(), 0), action, counter);
/*      */     }
/*      */ 
/*      */     
/* 1828 */     if (action == 524 || action == 525)
/*      */     {
/* 1830 */       return actionDestroyFloor(act, performer, source, floor, action, counter);
/*      */     }
/* 1832 */     if (action == 193) {
/*      */       
/* 1834 */       if ((!Servers.localServer.challengeServer || performer.getEnemyPresense() <= 0) && 
/* 1835 */         !floor.isNoRepair()) {
/* 1836 */         return MethodsStructure.repairFloor(performer, source, (IFloor)floor, counter, act);
/*      */       }
/* 1838 */       return true;
/*      */     } 
/* 1840 */     if (action == 192) {
/*      */       
/* 1842 */       if (!floor.isNoImprove()) {
/* 1843 */         return MethodsStructure.improveFloor(performer, source, (IFloor)floor, counter, act);
/*      */       }
/* 1845 */       return true;
/*      */     } 
/* 1847 */     if (action == 169) {
/*      */       
/* 1849 */       if (floor.getFloorState() != StructureConstants.FloorState.BUILDING) {
/*      */         
/* 1851 */         performer.getCommunicator().sendNormalServerMessage("The floor is in an invalid state to be continued.");
/*      */         
/* 1853 */         performer.getCommunicator().sendActionResult(false);
/* 1854 */         return true;
/*      */       } 
/* 1856 */       if (floorBuilding(act, performer, source, floor, action, counter)) {
/*      */         
/* 1858 */         performer.getCommunicator().sendAddFloorRoofToCreationWindow(floor, floor.getId());
/* 1859 */         return true;
/*      */       } 
/*      */       
/* 1862 */       return false;
/*      */     } 
/* 1864 */     if (action == 508) {
/*      */       
/* 1866 */       if (floor.isRoof()) {
/*      */         
/* 1868 */         performer.getCommunicator().sendNormalServerMessage("You can't plan above the " + floor.getName() + ".");
/* 1869 */         return true;
/*      */       } 
/* 1871 */       return MethodsStructure.floorPlanAbove(performer, source, floor.getTileX(), floor.getTileY(), encodedTile, performer
/* 1872 */           .getLayer(), counter, act, StructureConstants.FloorType.FLOOR);
/*      */     } 
/* 1874 */     if (action == 507)
/*      */     {
/* 1876 */       return MethodsStructure.floorPlanRoof(performer, source, floor.getTileX(), floor.getTileY(), encodedTile, floor
/* 1877 */           .getLayer(), counter, act);
/*      */     }
/* 1879 */     if (action == 514)
/*      */     {
/* 1881 */       return MethodsStructure.floorPlanAbove(performer, source, floor.getTileX(), floor.getTileY(), encodedTile, performer
/* 1882 */           .getLayer(), counter, act, StructureConstants.FloorType.DOOR);
/*      */     }
/* 1884 */     if (action == 515) {
/*      */       
/* 1886 */       if (floor.isRoof()) {
/*      */         
/* 1888 */         performer.getCommunicator().sendNormalServerMessage("You can't plan above the " + floor.getName() + ".");
/* 1889 */         return true;
/*      */       } 
/* 1891 */       return MethodsStructure.floorPlanAbove(performer, source, floor.getTileX(), floor.getTileY(), encodedTile, performer
/* 1892 */           .getLayer(), counter, act, StructureConstants.FloorType.OPENING);
/*      */     } 
/* 1894 */     if (action == 659 || action == 704 || action == 705 || action == 706 || action == 709 || action == 710 || action == 711 || action == 712 || action == 713 || action == 714 || action == 715) {
/*      */       StructureConstants.FloorType ft;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1906 */       if (floor.isRoof()) {
/*      */         
/* 1908 */         performer.getCommunicator().sendNormalServerMessage("You can't plan above the " + floor.getName() + ".");
/* 1909 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1913 */       if (action == 704) {
/* 1914 */         ft = StructureConstants.FloorType.WIDE_STAIRCASE;
/* 1915 */       } else if (action == 705) {
/* 1916 */         ft = StructureConstants.FloorType.RIGHT_STAIRCASE;
/* 1917 */       } else if (action == 706) {
/* 1918 */         ft = StructureConstants.FloorType.LEFT_STAIRCASE;
/* 1919 */       } else if (action == 709) {
/* 1920 */         ft = StructureConstants.FloorType.CLOCKWISE_STAIRCASE;
/* 1921 */       } else if (action == 710) {
/* 1922 */         ft = StructureConstants.FloorType.ANTICLOCKWISE_STAIRCASE;
/* 1923 */       } else if (action == 711) {
/* 1924 */         ft = StructureConstants.FloorType.CLOCKWISE_STAIRCASE_WITH;
/* 1925 */       } else if (action == 712) {
/* 1926 */         ft = StructureConstants.FloorType.ANTICLOCKWISE_STAIRCASE_WITH;
/* 1927 */       } else if (action == 713) {
/* 1928 */         ft = StructureConstants.FloorType.WIDE_STAIRCASE_RIGHT;
/* 1929 */       } else if (action == 714) {
/* 1930 */         ft = StructureConstants.FloorType.WIDE_STAIRCASE_LEFT;
/* 1931 */       } else if (action == 715) {
/* 1932 */         ft = StructureConstants.FloorType.WIDE_STAIRCASE_BOTH;
/*      */       } else {
/* 1934 */         ft = StructureConstants.FloorType.STAIRCASE;
/* 1935 */       }  return MethodsStructure.floorPlanAbove(performer, source, floor.getTileX(), floor.getTileY(), encodedTile, performer
/* 1936 */           .getLayer(), counter, act, ft);
/*      */     } 
/* 1938 */     if (action - 20000 >= 0)
/*      */     {
/* 1940 */       return buildAction(act, performer, source, floor, action, counter);
/*      */     }
/*      */     
/* 1943 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isConstructionAction(short action) {
/* 1948 */     if (action - 20000 >= 0) {
/* 1949 */       return true;
/*      */     }
/* 1951 */     switch (action) {
/*      */       
/*      */       case 169:
/*      */       case 192:
/*      */       case 193:
/*      */       case 507:
/*      */       case 508:
/*      */       case 514:
/*      */       case 515:
/*      */       case 524:
/*      */       case 525:
/*      */       case 659:
/*      */       case 704:
/*      */       case 705:
/*      */       case 706:
/*      */       case 709:
/*      */       case 710:
/*      */       case 711:
/*      */       case 712:
/*      */       case 713:
/*      */       case 714:
/*      */       case 715:
/* 1973 */         return true;
/*      */     } 
/* 1975 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean examine(Creature performer, Floor floor) {
/* 1985 */     String materials = "";
/* 1986 */     if (floor.getFloorState() == StructureConstants.FloorState.BUILDING) {
/*      */       
/* 1988 */       materials = buildRequiredMaterialString(floor, true);
/*      */       
/* 1990 */       performer.getCommunicator().sendNormalServerMessage("You see a " + floor
/* 1991 */           .getName() + " under construction. The " + floor.getName() + " requires " + materials + " to be finished.");
/*      */     }
/*      */     else {
/*      */       
/* 1995 */       if (floor.getFloorState() == StructureConstants.FloorState.PLANNING) {
/*      */         
/* 1997 */         performer.getCommunicator().sendNormalServerMessage("You see plans for a " + floor.getName() + ".");
/*      */         
/* 1999 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 2003 */       performer.getCommunicator().sendNormalServerMessage("It is a normal " + floor
/* 2004 */           .getName() + " made of " + getMaterialDescription(floor).toLowerCase() + ".");
/*      */     } 
/* 2006 */     sendQlString(performer, floor);
/* 2007 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String getMaterialDescription(Floor floor) {
/* 2016 */     if (floor.getType() == StructureConstants.FloorType.ROOF) {
/*      */       
/* 2018 */       switch (floor.getMaterial()) {
/*      */         
/*      */         case CLAY_BRICK:
/* 2021 */           return "pottery shingles";
/*      */         case SLATE_SLAB:
/* 2023 */           return "slate shingles";
/*      */         case WOOD:
/* 2025 */           return "wood shingles";
/*      */       } 
/* 2027 */       return floor.getMaterial().getName().toLowerCase();
/*      */     } 
/*      */ 
/*      */     
/* 2031 */     return floor.getMaterial().getName();
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendQlString(Creature performer, Floor floor) {
/* 2036 */     performer.getCommunicator()
/* 2037 */       .sendNormalServerMessage("QL = " + floor.getCurrentQL() + ", dam=" + floor.getDamage() + ".");
/* 2038 */     if (performer.getPower() > 0)
/*      */     {
/* 2040 */       performer.getCommunicator().sendNormalServerMessage("id: " + floor
/* 2041 */           .getId() + " " + floor.getTileX() + "," + floor.getTileY() + " height: " + floor
/* 2042 */           .getHeightOffset() + " " + floor
/* 2043 */           .getMaterial().getName() + " " + floor.getType().getName() + " (" + floor
/* 2044 */           .getFloorState().toString().toLowerCase() + ").");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final String buildRequiredMaterialString(Floor floor, boolean detailed) {
/* 2050 */     String description = new String();
/* 2051 */     int numMats = 0;
/* 2052 */     List<BuildMaterial> billOfMaterial = getRequiredMaterialsAtState(floor);
/*      */     
/* 2054 */     int maxMats = 0;
/* 2055 */     for (BuildMaterial mat : billOfMaterial) {
/*      */       
/* 2057 */       if (mat.getNeededQuantity() > 0)
/*      */       {
/* 2059 */         maxMats++;
/*      */       }
/*      */     } 
/* 2062 */     for (BuildMaterial mat : billOfMaterial) {
/*      */       
/* 2064 */       if (mat.getNeededQuantity() > 0) {
/*      */         
/* 2066 */         numMats++;
/* 2067 */         ItemTemplate template = null;
/*      */         
/*      */         try {
/* 2070 */           template = ItemTemplateFactory.getInstance().getTemplate(mat.getTemplateId());
/*      */         }
/* 2072 */         catch (NoSuchTemplateException e) {
/*      */           
/* 2074 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */         } 
/*      */         
/* 2077 */         if (numMats > 1)
/*      */         {
/* 2079 */           if (numMats < maxMats) {
/* 2080 */             description = description + ", ";
/*      */           } else {
/* 2082 */             description = description + " and ";
/*      */           }  } 
/* 2084 */         if (template != null) {
/*      */           
/* 2086 */           if (detailed)
/* 2087 */             description = description + mat.getNeededQuantity() + " "; 
/* 2088 */           if (template.sizeString.length() > 0)
/* 2089 */             description = description + template.sizeString; 
/* 2090 */           description = description + ((mat.getNeededQuantity() > 1) ? template.getPlural() : template.getName());
/*      */         } 
/*      */         
/* 2093 */         if (description.length() == 0)
/* 2094 */           description = "unknown quantities of unknown materials"; 
/*      */       } 
/*      */     } 
/* 2097 */     if (description.length() == 0)
/*      */     {
/* 2099 */       description = "no materials";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2104 */     return description;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getBuildSound(Floor floor) {
/* 2109 */     String soundToPlay = "";
/* 2110 */     switch (floor.getMaterial())
/*      */     
/*      */     { case CLAY_BRICK:
/*      */       case SLATE_SLAB:
/*      */       case STONE_BRICK:
/*      */       case SANDSTONE_SLAB:
/*      */       case STONE_SLAB:
/*      */       case MARBLE_SLAB:
/* 2118 */         soundToPlay = "sound.work.masonry";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2133 */         return soundToPlay;case METAL_IRON: case METAL_COPPER: case METAL_STEEL: case METAL_SILVER: case METAL_GOLD: soundToPlay = "sound.work.smithing.metal"; return soundToPlay; }  soundToPlay = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2"; return soundToPlay;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void damageTool(Creature performer, Floor floor, Item source) {
/* 2138 */     if (source.getTemplateId() == 63) {
/* 2139 */       source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 2140 */     } else if (source.getTemplateId() == 62) {
/* 2141 */       source.setDamage(source.getDamage() + 3.0E-4F * source.getDamageModifier());
/* 2142 */     } else if (source.getTemplateId() == 493) {
/* 2143 */       source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Skill getToolSkill(Floor floor, Creature performer, Item source) {
/* 2148 */     Skill toolSkill = null;
/*      */     
/*      */     try {
/* 2151 */       toolSkill = performer.getSkills().getSkill(source.getPrimarySkill());
/*      */     }
/* 2153 */     catch (NoSuchSkillException nss) {
/*      */ 
/*      */       
/*      */       try {
/* 2157 */         toolSkill = performer.getSkills().learn(source.getPrimarySkill(), 1.0F);
/*      */       }
/* 2159 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2164 */     return toolSkill;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\FloorBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */