/*      */ package com.wurmonline.server.behaviours;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.villages.Reputation;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.StructureConstants;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.io.IOException;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ public final class CargoTransportationMethods implements MiscConstants, CreatureTemplateIds {
/*   37 */   private static final Logger logger = Logger.getLogger(CargoTransportationMethods.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final double LOAD_STRENGTH_NEEDED = 23.0D;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean loadCargo(Creature performer, Item target, float counter) {
/*   58 */     Action act = null;
/*      */ 
/*      */     
/*      */     try {
/*   62 */       act = performer.getCurrentAction();
/*      */     }
/*   64 */     catch (NoSuchActionException nsa) {
/*      */       
/*   66 */       logger.log(Level.WARNING, "Unable to get current action in loadCargo().", (Throwable)nsa);
/*   67 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*   71 */     for (Item item : target.getAllItems(true)) {
/*      */       
/*   73 */       if (item.getTemplateId() == 1436)
/*      */       {
/*   75 */         if (!item.isEmpty(true)) {
/*      */           
/*   77 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*   78 */               .getName() + " has chickens in it, remove them first.");
/*   79 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*   85 */     if (target.getTemplateId() == 1311) {
/*      */ 
/*      */       
/*      */       try {
/*   89 */         Item item = Items.getItem(performer.getVehicle());
/*   90 */         if (item.getTemplateId() == 491) {
/*      */           
/*   92 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*   93 */               .getName() + " will not fit in the " + item.getName() + ".");
/*   94 */           return true;
/*      */         } 
/*   96 */         if (item.getTemplateId() == 490)
/*      */         {
/*   98 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*   99 */               .getName() + " will not fit in the " + item.getName() + ".");
/*  100 */           return true;
/*      */         }
/*      */       
/*  103 */       } catch (NoSuchItemException ex) {
/*      */         
/*  105 */         logger.log(Level.WARNING, "NoSuchItemException: " + String.valueOf(ex));
/*  106 */         ex.printStackTrace();
/*      */       } 
/*      */ 
/*      */       
/*  110 */       if (target.getTemplateId() == 1311 && !target.isEmpty(true))
/*      */       {
/*  112 */         for (Item item : target.getAllItems(true)) {
/*      */ 
/*      */           
/*      */           try {
/*  116 */             Creature getCreature = Creatures.getInstance().getCreature(item.getData());
/*  117 */             if (getCreature.getDominator() != null)
/*      */             {
/*  119 */               if (getCreature.getDominator() != performer)
/*      */               {
/*  121 */                 performer.getCommunicator().sendNormalServerMessage("You cannot load this cage, the creature inside is not tamed by you.");
/*      */                 
/*  123 */                 return true;
/*      */               }
/*      */             
/*      */             }
/*  127 */           } catch (NoSuchCreatureException ex) {
/*      */             
/*  129 */             logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */           } 
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/*      */         int max;
/*      */         
/*  138 */         Item item = Items.getItem(performer.getVehicle());
/*  139 */         int theVessel = item.getTemplateId();
/*  140 */         switch (theVessel) {
/*      */           
/*      */           case 541:
/*  143 */             max = 5;
/*      */             break;
/*      */           case 540:
/*  146 */             max = 6;
/*      */             break;
/*      */           case 542:
/*  149 */             max = 4;
/*      */             break;
/*      */           case 543:
/*  152 */             max = 8;
/*      */             break;
/*      */           case 850:
/*  155 */             max = 2;
/*      */             break;
/*      */           case 1410:
/*  158 */             max = 4;
/*      */             break;
/*      */           default:
/*  161 */             max = 0;
/*      */             break;
/*      */         } 
/*  164 */         if (item.getInsertItem() != null)
/*      */         {
/*  166 */           if (item.getInsertItem().getNumberCages() >= max)
/*      */           {
/*  168 */             performer.getCommunicator().sendNormalServerMessage("The " + target
/*  169 */                 .getName() + " will not fit in the " + item.getName() + ".");
/*  170 */             return true;
/*      */           }
/*      */         
/*      */         }
/*  174 */       } catch (NoSuchItemException ex) {
/*      */         
/*  176 */         logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  181 */     if (isLargeMagicChest(target))
/*      */     {
/*  183 */       if (!performerIsLastOwner(performer, target)) {
/*  184 */         return true;
/*      */       }
/*      */     }
/*  187 */     if (isAutoRefillWell(target)) {
/*      */       
/*  189 */       performer
/*  190 */         .getCommunicator()
/*  191 */         .sendNormalServerMessage("This is a special version of the item that is designed to exist only on starter deeds, and is therefor not transportable.");
/*      */       
/*  193 */       return true;
/*      */     } 
/*      */     
/*  196 */     if (targetIsNotTransportable(target, performer)) {
/*  197 */       return true;
/*      */     }
/*  199 */     if (targetIsNotOnTheGround(target, performer, false)) {
/*  200 */       return true;
/*      */     }
/*  202 */     if (targetIsDraggedCheck(target, performer)) {
/*  203 */       return true;
/*      */     }
/*  205 */     if (targetIsPlantedCheck(target, performer)) {
/*  206 */       return true;
/*      */     }
/*  208 */     if (targetIsOccupiedBed(target, performer, act.getNumber())) {
/*  209 */       return true;
/*      */     }
/*  211 */     if (performerIsNotOnAVehicle(performer)) {
/*  212 */       return true;
/*      */     }
/*  214 */     Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle());
/*  215 */     if (performerIsNotOnATransportVehicle(performer, vehicle)) {
/*  216 */       return true;
/*      */     }
/*  218 */     if (targetIsSameAsCarrier(performer, vehicle, target)) {
/*  219 */       return true;
/*      */     }
/*  221 */     if (targetIsHitchedOrCommanded(target, performer)) {
/*  222 */       return true;
/*      */     }
/*  224 */     Seat seat = vehicle.getSeatFor(performer.getWurmId());
/*  225 */     if (performerIsNotSeatedOnAVehicle(performer, seat)) {
/*  226 */       return true;
/*      */     }
/*  228 */     if (perfomerActionTargetIsBlocked(performer, target)) {
/*  229 */       return true;
/*      */     }
/*  231 */     Item carrier = getCarrierItem(vehicle, performer);
/*  232 */     if (carrier == null) {
/*  233 */       return true;
/*      */     }
/*  235 */     int distance = getLoadActionDistance(carrier);
/*  236 */     if (!performerIsWithinDistanceToTarget(performer, target, distance)) {
/*  237 */       return true;
/*      */     }
/*  239 */     if (!target.isVehicle()) {
/*      */       
/*  241 */       if (targetIsLockedCheck(target, performer, vehicle)) {
/*  242 */         return true;
/*      */       }
/*      */     } else {
/*      */       
/*  246 */       if (target.isOwnedByWagoner()) {
/*      */         
/*  248 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/*  249 */             .getName() + " is owned by a wagoner, and will not allow that to be loaded.");
/*  250 */         return true;
/*      */       } 
/*  252 */       if (targetVehicleIsLockedCheck(target, performer, vehicle)) {
/*  253 */         return true;
/*      */       }
/*      */     } 
/*  256 */     if (performerMayNotUseInventory(performer, carrier)) {
/*  257 */       return true;
/*      */     }
/*  259 */     if (targetIsLoadedWarmachine(target, performer, carrier)) {
/*  260 */       return true;
/*      */     }
/*  262 */     if (targetIsNotEmptyContainerCheck(target, performer, carrier, true)) {
/*  263 */       return true;
/*      */     }
/*  265 */     if (targetIsOnFireCheck(target, performer, carrier)) {
/*  266 */       return true;
/*      */     }
/*  268 */     if (targetHasActiveQueen(target, performer, carrier, true)) {
/*  269 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  273 */     if (target.getTemplateId() != 1311)
/*      */     {
/*  275 */       if (targetCanNotBeInsertedCheck(target, carrier, vehicle, performer)) {
/*  276 */         return true;
/*      */       }
/*      */     }
/*  279 */     if (!isOnSameLevelLoad(target, performer)) {
/*  280 */       return true;
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
/*  292 */     if (!Methods.isActionAllowed(performer, (short)605)) {
/*  293 */       return true;
/*      */     }
/*  295 */     if (target.isPlanted() && !Methods.isActionAllowed(performer, (short)685)) {
/*  296 */       return true;
/*      */     }
/*  298 */     if (target.isCrate() && target.isSealedByPlayer() && target.getLastOwnerId() != performer.getWurmId()) {
/*      */       
/*  300 */       String pname = PlayerInfoFactory.getPlayerName(target.getLastOwnerId());
/*  301 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/*  302 */           .getName() + " has a security seal on it, and may only be loaded by " + pname + ".");
/*  303 */       return true;
/*      */     } 
/*      */     
/*  306 */     if (target.isMarketStall()) {
/*      */       
/*  308 */       int tilex = target.getTileX();
/*  309 */       int tiley = target.getTileY();
/*      */       
/*      */       try {
/*  312 */         Zone zone = Zones.getZone(tilex, tiley, target.isOnSurface());
/*  313 */         VolaTile t = zone.getOrCreateTile(tilex, tiley);
/*  314 */         if (t != null && (t.getCreatures()).length > 0)
/*      */         {
/*  316 */           for (Creature cret : t.getCreatures()) {
/*      */             
/*  318 */             if (cret.isNpcTrader())
/*      */             {
/*  320 */               performer.getCommunicator().sendSafeServerMessage("This stall is currently in use.");
/*  321 */               return true;
/*      */             }
/*      */           
/*      */           } 
/*      */         }
/*  326 */       } catch (NoSuchZoneException nsz) {
/*      */         
/*  328 */         logger.warning(String.format("Could not find zone at tile [%s, %s] for item %s.", new Object[] {
/*  329 */                 Integer.valueOf(tilex), Integer.valueOf(tiley), target.getName()
/*      */               }));
/*      */       } 
/*      */     } 
/*  333 */     int time = Actions.getLoadUnloadActionTime(performer);
/*      */     
/*  335 */     if (counter == 1.0F) {
/*      */       
/*  337 */       if (!strengthCheck(performer, 23.0D)) {
/*      */         
/*  339 */         String message = StringUtil.format("You are not strong enough to do this, you need at least %.1f body strength.", new Object[] {
/*      */               
/*  341 */               Double.valueOf(23.0D) });
/*  342 */         performer.getCommunicator().sendNormalServerMessage(message);
/*  343 */         return true;
/*      */       } 
/*      */       
/*  346 */       act.setTimeLeft(time);
/*  347 */       performer.getCommunicator().sendNormalServerMessage("You start to load the " + target.getName() + ".");
/*  348 */       Server.getInstance().broadCastAction(performer.getName() + " starts to load the " + target.getName() + ".", performer, 5);
/*      */       
/*  350 */       performer.sendActionControl(Actions.actionEntrys[605].getVerbString(), true, time);
/*      */       
/*  352 */       performer.getStatus().modifyStamina(-10000.0F);
/*  353 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  357 */     time = act.getTimeLeft();
/*  358 */     if (act.currentSecond() % 5 == 0)
/*  359 */       performer.getStatus().modifyStamina(-10000.0F); 
/*  360 */     if (act.currentSecond() == 3 && target.isLocked())
/*      */     {
/*      */       
/*  363 */       if (target.isGuest(-30L) || target
/*  364 */         .isGuest(-20L) || target
/*  365 */         .isGuest(-40L))
/*      */       {
/*      */         
/*  368 */         performer.getCommunicator().sendServerMessage("WARNING - " + target
/*  369 */             .getName() + " has Group permissions, this WILL cause problems when crossing servers!", 255, 127, 63);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  375 */     if (counter * 10.0F > time) {
/*      */       
/*  377 */       if (target.getTemplateId() != 1311 && 
/*  378 */         targetCanNotBeInsertedCheck(target, carrier, vehicle, performer)) {
/*  379 */         return true;
/*      */       }
/*  381 */       boolean isStealing = MethodsItems.checkIfStealing(target, performer, act);
/*  382 */       if (performerIsTryingToStealInLawfulMode(performer, isStealing)) {
/*  383 */         return true;
/*      */       }
/*  385 */       Creature[] watchers = null;
/*      */ 
/*      */       
/*      */       try {
/*  389 */         watchers = target.getWatchers();
/*      */       }
/*  391 */       catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  396 */       if (isStealing) {
/*      */         
/*  398 */         if (performerMayNotStealCheck(performer)) {
/*  399 */           return true;
/*      */         }
/*  401 */         Skill stealing = null;
/*  402 */         boolean dryRun = false;
/*  403 */         Village v = Zones.getVillage(target.getTileX(), target.getTileY(), true);
/*  404 */         if (v != null) {
/*      */           
/*  406 */           Reputation rep = v.getReputationObject(performer.getWurmId());
/*  407 */           if (rep != null)
/*      */           {
/*  409 */             if (rep.getValue() >= 0 && rep.isPermanent())
/*  410 */               dryRun = true; 
/*      */           }
/*      */         } 
/*  413 */         if (MethodsItems.setTheftEffects(performer, act, target)) {
/*      */           
/*  415 */           stealing = performer.getStealSkill();
/*  416 */           stealing.skillCheck(target.getQualityLevel(), 0.0D, dryRun, 10.0F);
/*  417 */           return true;
/*      */         } 
/*      */ 
/*      */         
/*  421 */         stealing = performer.getStealSkill();
/*  422 */         stealing.skillCheck(target.getQualityLevel(), 0.0D, dryRun, 10.0F);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  428 */         if (targetIsNotOnTheGround(target, performer, true))
/*  429 */           return true; 
/*  430 */         Zone zone = Zones.getZone(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  431 */         zone.removeItem(target);
/*  432 */         if (shouldRemovePlantedFlag(target))
/*      */         {
/*  434 */           target.setIsPlanted(false);
/*      */         }
/*  436 */         carrier.insertItem(target, true);
/*  437 */         if (carrier.getTemplateId() == 1410) {
/*  438 */           updateItemModel(carrier);
/*      */         }
/*  440 */         if (watchers != null)
/*      */         {
/*  442 */           for (Creature c : watchers)
/*      */           {
/*  444 */             c.getCommunicator().sendCloseInventoryWindow(target.getWurmId());
/*      */           }
/*      */         }
/*  447 */         if (target.isCrate() && target.isSealedByPlayer() && target.getLastOwnerId() == performer.getWurmId()) {
/*      */           
/*  449 */           performer.getCommunicator().sendNormalServerMessage("You accidently knock off the security seal.");
/*  450 */           target.setIsSealedByPlayer(false);
/*      */         } 
/*  452 */         performer.getCommunicator().sendNormalServerMessage("You finish loading the " + target.getName() + ".");
/*  453 */         Server.getInstance().broadCastAction(performer.getName() + " finishes loading the " + target.getName() + ".", performer, 5);
/*      */ 
/*      */         
/*  456 */         target.setLastMaintained(WurmCalendar.currentTime);
/*      */       }
/*  458 */       catch (NoSuchZoneException nsz) {
/*      */         
/*  460 */         String message = StringUtil.format("Unable to find zone for x: %d y: %d surface: %s in loadCargo().", new Object[] {
/*      */               
/*  462 */               Integer.valueOf(target.getTileX()), 
/*  463 */               Integer.valueOf(target.getTileY()), 
/*  464 */               Boolean.toString(target.isOnSurface()) });
/*  465 */         logger.log(Level.WARNING, message, (Throwable)nsz);
/*      */       } 
/*      */       
/*  468 */       return true;
/*      */     } 
/*  470 */     return false;
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
/*      */   static boolean loadCreature(Creature performer, Item target, float counter) {
/*      */     Action act;
/*  485 */     Creature[] folls = performer.getFollowers();
/*      */     
/*      */     try {
/*  488 */       act = performer.getCurrentAction();
/*      */     }
/*  490 */     catch (NoSuchActionException nsa) {
/*      */       
/*  492 */       logger.log(Level.WARNING, "Unable to get current action in loadCreature().", (Throwable)nsa);
/*  493 */       return true;
/*      */     } 
/*      */     
/*  496 */     if (!target.isEmpty(true)) {
/*      */       
/*  498 */       performer.getCommunicator().sendNormalServerMessage("There is already a creature in this " + target
/*  499 */           .getName() + ".", (byte)3);
/*  500 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  504 */     for (Creature foll : folls) {
/*      */       
/*  506 */       if (foll.getStatus().getBody().isWounded()) {
/*      */         
/*  508 */         performer.getCommunicator().sendNormalServerMessage("The creature whimpers in pain, and refuses to enter the cage.");
/*      */         
/*  510 */         return true;
/*      */       } 
/*  512 */       if ((foll.getBody().getAllItems()).length > 0) {
/*      */         
/*  514 */         performer.getCommunicator().sendNormalServerMessage("You cannot load the creature with items on it, remove them first.");
/*      */         
/*  516 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  520 */     if (folls.length > 1) {
/*      */       
/*  522 */       performer.getCommunicator().sendNormalServerMessage("You are currently leading too many creatures.");
/*      */       
/*  524 */       return true;
/*      */     } 
/*      */     
/*  527 */     if (target.getParentId() != -10L) {
/*      */       
/*  529 */       performer.getCommunicator().sendNormalServerMessage("You must unload the cage to load creatures into it.");
/*      */       
/*  531 */       return true;
/*      */     } 
/*      */     
/*  534 */     if (target.isLocked() && !target.mayAccessHold(performer)) {
/*      */       
/*  536 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to load creatures into this cage. The target is locked.");
/*      */       
/*  538 */       return true;
/*      */     } 
/*      */     
/*  541 */     if (target.getCurrentQualityLevel() < 10.0F) {
/*      */       
/*  543 */       performer.getCommunicator().sendNormalServerMessage("The cage is in too poor of shape to be used.");
/*      */       
/*  545 */       return true;
/*      */     } 
/*  547 */     int time = Actions.getLoadUnloadActionTime(performer);
/*      */     
/*  549 */     if (counter == 1.0F) {
/*      */       
/*  551 */       for (Creature foll : folls) {
/*      */         
/*  553 */         if (foll.isPregnant()) {
/*      */ 
/*      */           
/*  556 */           performer.getCommunicator().sendNormalServerMessage("You feel terrible caging the unborn offspring of " + foll
/*      */               
/*  558 */               .getNameWithGenus() + ", and decide not to.");
/*  559 */           return true;
/*      */         } 
/*  561 */         if (!performer.isWithinDistanceTo(foll, 5.0F)) {
/*      */ 
/*      */           
/*  564 */           performer.getCommunicator().sendNormalServerMessage("You are too far away from the creature.");
/*      */           
/*  566 */           return true;
/*      */         } 
/*  568 */         if (!foll.isWithinDistanceTo(target, 5.0F)) {
/*      */           
/*  570 */           performer.getCommunicator().sendNormalServerMessage("The creature is too far away from the cage.");
/*      */           
/*  572 */           return true;
/*      */         } 
/*  574 */         if (!performer.isWithinDistanceTo(target, 5.0F)) {
/*      */           
/*  576 */           performer.getCommunicator().sendNormalServerMessage("You are too far away from the cage.");
/*      */           
/*  578 */           return true;
/*      */         } 
/*      */         
/*  581 */         act.setTimeLeft(time);
/*      */         
/*  583 */         performer.getCommunicator().sendNormalServerMessage("You start to load the " + foll
/*  584 */             .getName() + ".");
/*      */         
/*  586 */         Server.getInstance().broadCastAction(performer
/*  587 */             .getName() + " starts to load the " + foll.getName() + ".", performer, 5);
/*      */ 
/*      */         
/*  590 */         performer.sendActionControl(Actions.actionEntrys[907].getVerbString(), true, time);
/*      */ 
/*      */         
/*  593 */         performer.getStatus().modifyStamina(-10000.0F);
/*      */       } 
/*  595 */       return false;
/*      */     } 
/*      */     
/*  598 */     if (counter * 10.0F > time) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  603 */         for (Creature foll : folls)
/*      */         {
/*      */           
/*  606 */           if (!foll.getInventory().isEmpty(true))
/*      */           {
/*  608 */             for (Item item : foll.getInventory().getAllItems(true))
/*      */             {
/*      */               
/*  611 */               Items.destroyItem(item.getWurmId());
/*      */             }
/*      */           }
/*  614 */           Item insertTarget = target.getInsertItem();
/*  615 */           Item i = ItemFactory.createItem(1310, 
/*  616 */               (foll.getStatus()).age, (byte)0, null);
/*  617 */           if (insertTarget != null) {
/*      */             
/*  619 */             insertTarget.insertItem(i, true);
/*  620 */             i.setData(foll.getWurmId());
/*  621 */             i.setName(foll.getName());
/*  622 */             i.setWeight((int)foll.getWeight(), false);
/*      */           }
/*      */           else {
/*      */             
/*  626 */             logger.log(Level.WARNING, "" + performer.getName() + " caused Null pointer.");
/*      */           } 
/*      */ 
/*      */           
/*  630 */           DbCreatureStatus.setLoaded(1, foll.getWurmId());
/*      */           
/*  632 */           target.setAuxData((byte)foll.getTemplate().getTemplateId());
/*  633 */           foll.setLeader(null);
/*  634 */           foll.getCurrentTile().deleteCreature(foll);
/*  635 */           foll.savePosition(-10);
/*  636 */           foll.destroyVisionArea();
/*  637 */           foll.getStatus().setDead(true);
/*  638 */           target.setName("creature cage [" + foll.getName() + "]");
/*  639 */           updateItemModel(target);
/*      */           
/*  641 */           target.setData(System.currentTimeMillis());
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  646 */       catch (IOException|FailedException|com.wurmonline.server.items.NoSuchTemplateException ex) {
/*      */         
/*  648 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/*  650 */       for (Creature foll : folls) {
/*      */ 
/*      */         
/*  653 */         if (foll.getStatus().getAgeString().equals("venerable"))
/*      */         {
/*  655 */           performer.getCommunicator().sendNormalServerMessage("The " + foll
/*  656 */               .getName() + " is " + "venerable" + ", and may die if it crosses to another server.", (byte)3);
/*      */         }
/*      */ 
/*      */         
/*  660 */         performer.getCommunicator().sendNormalServerMessage("You finish loading the " + foll
/*  661 */             .getName() + ".");
/*  662 */         Server.getInstance().broadCastAction(performer
/*  663 */             .getName() + " finishes loading the " + foll
/*  664 */             .getName() + ".", performer, 5);
/*      */       } 
/*  666 */       return true;
/*      */     } 
/*  668 */     return false;
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
/*      */   static boolean unloadCreature(Creature performer, Item target, float counter) {
/*      */     Action act;
/*      */     try {
/*  685 */       act = performer.getCurrentAction();
/*      */     }
/*  687 */     catch (NoSuchActionException nsa) {
/*      */       
/*  689 */       logger.log(Level.WARNING, "Unable to get current action in unloadCreature().", (Throwable)nsa);
/*  690 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  694 */     if (!Methods.isActionAllowed(performer, (short)234))
/*      */     {
/*  696 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  701 */       Creature getCreature = Creatures.getInstance().getCreature(target.getData());
/*  702 */       if (getCreature.getDominator() != null)
/*      */       {
/*  704 */         if (getCreature.getDominator() != performer)
/*      */         {
/*  706 */           performer.getCommunicator().sendNormalServerMessage("You cannot unload this creature, it is not your pet.");
/*      */           
/*  708 */           return true;
/*      */         }
/*      */       
/*      */       }
/*  712 */     } catch (NoSuchCreatureException ex) {
/*      */       
/*  714 */       logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  719 */       if (target.getParent().getParentId() != -10L)
/*      */       {
/*  721 */         performer.getCommunicator().sendNormalServerMessage("You must unload the cage to unload the creature within.");
/*      */         
/*  723 */         return true;
/*      */       }
/*      */     
/*  726 */     } catch (NoSuchItemException ex) {
/*      */       
/*  728 */       logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */     } 
/*      */     
/*  731 */     if (target.getData() == -10L) {
/*      */       
/*  733 */       performer.getCommunicator().sendNormalServerMessage("The loaded creature has no data, return to the origin server and re-load it.");
/*      */       
/*  735 */       return true;
/*      */     } 
/*      */     
/*  738 */     int time = Actions.getLoadUnloadActionTime(performer);
/*      */     
/*  740 */     if (counter == 1.0F) {
/*      */       
/*  742 */       act.setTimeLeft(time);
/*  743 */       performer.getCommunicator().sendNormalServerMessage("You start to unload the " + target
/*  744 */           .getName() + ".");
/*  745 */       Server.getInstance().broadCastAction(performer
/*  746 */           .getName() + " starts to unload the " + target.getName() + ".", performer, 5);
/*      */       
/*  748 */       performer.sendActionControl(Actions.actionEntrys[908].getVerbString(), true, time);
/*      */       
/*  750 */       performer.getStatus().modifyStamina(-10000.0F);
/*  751 */       return false;
/*      */     } 
/*      */     
/*  754 */     if (counter * 10.0F > time) {
/*      */ 
/*      */       
/*      */       try {
/*  758 */         target.getParent().setName("creature cage [Empty]");
/*  759 */         Creature getCreature = Creatures.getInstance().getCreature(target.getData());
/*  760 */         Creatures cstat = Creatures.getInstance();
/*  761 */         getCreature.getStatus().setDead(false);
/*  762 */         cstat.removeCreature(getCreature);
/*  763 */         cstat.addCreature(getCreature, false);
/*  764 */         getCreature.putInWorld();
/*  765 */         CreatureBehaviour.blinkTo(getCreature, performer.getPosX(), performer.getPosY(), performer.getLayer(), performer.getPositionZ(), performer.getBridgeId(), performer.getFloorLevel());
/*  766 */         target.getParent().setDamage((float)(target.getParent().getDamage() + getCreature.getSkills().getSkill(102).getKnowledge(0.0D) / target.getParent().getQualityLevel()));
/*  767 */         target.getParent().setAuxData((byte)0);
/*  768 */         updateItemModel(target.getParent());
/*  769 */         performer.getCommunicator().sendNormalServerMessage("Creature unloaded.");
/*  770 */         performer.getCommunicator().sendNormalServerMessage("The creature damages the cage from anger.");
/*      */ 
/*      */         
/*  773 */         getCreature.save();
/*  774 */         getCreature.savePosition(target.getZoneId());
/*  775 */         Items.destroyItem(target.getWurmId());
/*      */         
/*  777 */         target.setLastOwnerId(performer.getWurmId());
/*      */         
/*  779 */         DbCreatureStatus.setLoaded(0, getCreature.getWurmId());
/*      */       }
/*  781 */       catch (Exception ex) {
/*      */         
/*  783 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/*  785 */       performer.getCommunicator().sendNormalServerMessage("You finish unloading the " + target
/*  786 */           .getName() + ".");
/*  787 */       Server.getInstance().broadCastAction(performer
/*  788 */           .getName() + " finishes unloading the " + target.getName() + ".", performer, 5);
/*      */       
/*  790 */       return true;
/*      */     } 
/*  792 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isOnSameLevelLoad(Item target, Creature player) {
/*  797 */     if (!GeneralUtilities.isOnSameLevel(player, target)) {
/*      */       
/*  799 */       String m = StringUtil.format("You must be on the same floor level as the %s in order to load it.", new Object[] { target
/*  800 */             .getName() });
/*  801 */       player.getCommunicator().sendNormalServerMessage(m);
/*  802 */       return false;
/*      */     } 
/*  804 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isOnSameLevelUnload(Item target, Creature player) {
/*  809 */     if (!GeneralUtilities.isOnSameLevel(player, target)) {
/*      */       
/*  811 */       String m = StringUtil.format("You must be on the same floor level as the %s in order to unload items from it.", new Object[] { target
/*      */             
/*  813 */             .getName() });
/*  814 */       player.getCommunicator().sendNormalServerMessage(m);
/*  815 */       return false;
/*      */     } 
/*  817 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean shouldRemovePlantedFlag(Item target) {
/*  827 */     if (target.isPlanted())
/*      */     {
/*  829 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  833 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean loadShip(Creature performer, Item target, float counter) {
/*  838 */     Action act = null;
/*      */     
/*      */     try {
/*  841 */       act = performer.getCurrentAction();
/*      */     }
/*  843 */     catch (NoSuchActionException nsa) {
/*      */       
/*  845 */       logger.log(Level.WARNING, "Unable to get current action in loadShip().", (Throwable)nsa);
/*  846 */       return true;
/*      */     } 
/*      */     
/*  849 */     if (targetIsMoored(target, performer))
/*  850 */       return true; 
/*  851 */     if (targetIsDraggedCheck(target, performer)) {
/*  852 */       return true;
/*      */     }
/*  854 */     if (targetIsNotOnTheGround(target, performer, false)) {
/*  855 */       return true;
/*      */     }
/*  857 */     Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle());
/*  858 */     if (performerIsNotOnATransportVehicle(performer, vehicle)) {
/*  859 */       return true;
/*      */     }
/*  861 */     Seat seat = vehicle.getSeatFor(performer.getWurmId());
/*  862 */     if (performerIsNotSeatedOnAVehicle(performer, seat)) {
/*  863 */       return true;
/*      */     }
/*  865 */     if (perfomerActionTargetIsBlocked(performer, target)) {
/*  866 */       return true;
/*      */     }
/*  868 */     if (targetVehicleIsLockedCheck(target, performer, vehicle)) {
/*  869 */       return true;
/*      */     }
/*  871 */     Item carrier = getCarrierItem(vehicle, performer);
/*  872 */     if (carrier == null) {
/*  873 */       return true;
/*      */     }
/*  875 */     if (performerMayNotUseInventory(performer, carrier)) {
/*  876 */       return true;
/*      */     }
/*  878 */     if (carrier.getItems().size() > 0) {
/*      */       
/*  880 */       performer.getCommunicator().sendNormalServerMessage(
/*  881 */           StringUtil.format("The %s is full.", new Object[] { carrier.getName() }));
/*  882 */       return true;
/*      */     } 
/*      */     
/*  885 */     if (targetIsHitchedOrCommanded(target, performer)) {
/*  886 */       return true;
/*      */     }
/*  888 */     if (targetIsNotEmptyContainerCheck(target, performer, carrier, true)) {
/*  889 */       return true;
/*      */     }
/*  891 */     if (!Methods.isActionAllowed(performer, (short)605)) {
/*  892 */       return true;
/*      */     }
/*  894 */     int time = Actions.getLoadUnloadActionTime(performer);
/*      */     
/*  896 */     if (counter == 1.0F) {
/*      */       
/*  898 */       float modifier = target.getFullWeight(true) / target.getTemplate().getWeightGrams();
/*  899 */       if (target.isUnfinished())
/*  900 */         modifier = 1.0F; 
/*  901 */       if (!strengthCheck(performer, 23.0D + (3.0F * modifier - 3.0F))) {
/*      */         
/*  903 */         String message = StringUtil.format("You are not strong enough to do this, you need at least %.1f body strength.", new Object[] {
/*      */               
/*  905 */               Double.valueOf(23.0D + (3.0F * modifier - 3.0F)) });
/*  906 */         performer.getCommunicator().sendNormalServerMessage(message);
/*  907 */         return true;
/*      */       } 
/*      */       
/*  910 */       act.setTimeLeft(time);
/*  911 */       performer.getCommunicator().sendNormalServerMessage("You start to load the " + target.getName() + ".");
/*  912 */       Server.getInstance().broadCastAction(performer.getName() + " starts to load the " + target.getName() + ".", performer, 5);
/*      */       
/*  914 */       performer.sendActionControl(Actions.actionEntrys[605].getVerbString(), true, time);
/*      */       
/*  916 */       performer.getStatus().modifyStamina(-10000.0F);
/*  917 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  921 */     time = act.getTimeLeft();
/*  922 */     if (act.currentSecond() % 5 == 0) {
/*  923 */       performer.getStatus().modifyStamina(-10000.0F);
/*      */     }
/*      */     
/*  926 */     if (counter * 10.0F > time) {
/*      */       
/*  928 */       boolean isStealing = MethodsItems.checkIfStealing(target, performer, act);
/*  929 */       if (performerIsTryingToStealInLawfulMode(performer, isStealing)) {
/*  930 */         return true;
/*      */       }
/*  932 */       if (isStealing) {
/*      */         
/*  934 */         if (performerMayNotStealCheck(performer)) {
/*  935 */           return true;
/*      */         }
/*  937 */         Skill stealing = null;
/*  938 */         boolean dryRun = false;
/*  939 */         Village v = Zones.getVillage(target.getTileX(), target.getTileY(), true);
/*  940 */         if (v != null) {
/*      */           
/*  942 */           Reputation rep = v.getReputationObject(performer.getWurmId());
/*  943 */           if (rep != null)
/*      */           {
/*  945 */             if (rep.getValue() >= 0 && rep.isPermanent())
/*  946 */               dryRun = true; 
/*      */           }
/*      */         } 
/*  949 */         if (MethodsItems.setTheftEffects(performer, act, target)) {
/*      */           
/*  951 */           stealing = performer.getStealSkill();
/*  952 */           stealing.skillCheck(target.getQualityLevel(), 0.0D, dryRun, 10.0F);
/*  953 */           return true;
/*      */         } 
/*      */ 
/*      */         
/*  957 */         stealing = performer.getStealSkill();
/*  958 */         stealing.skillCheck(target.getQualityLevel(), 0.0D, dryRun, 10.0F);
/*      */       } 
/*      */ 
/*      */       
/*  962 */       Creature[] watchers = null;
/*      */       
/*      */       try {
/*  965 */         watchers = target.getWatchers();
/*      */       }
/*  967 */       catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  974 */         if (targetIsNotOnTheGround(target, performer, true)) {
/*  975 */           return true;
/*      */         }
/*  977 */         Zone zone = Zones.getZone(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  978 */         zone.removeItem(target);
/*  979 */         carrier.insertItem(target, true);
/*      */         
/*  981 */         updateItemModel(carrier);
/*      */         
/*  983 */         performer.getCommunicator().sendNormalServerMessage("You finish loading the " + target.getName() + ".");
/*  984 */         Server.getInstance().broadCastAction(performer.getName() + " finishes loading the " + target.getName() + ".", performer, 5);
/*      */ 
/*      */         
/*  987 */         if (watchers != null)
/*      */         {
/*  989 */           for (Creature c : watchers)
/*      */           {
/*  991 */             c.getCommunicator().sendCloseInventoryWindow(target.getWurmId());
/*      */           }
/*      */         }
/*      */       }
/*  995 */       catch (NoSuchZoneException nsz) {
/*      */         
/*  997 */         String message = StringUtil.format("Unable to find zone for x: %d y: %d surface: %s in loadShip().", new Object[] {
/*      */               
/*  999 */               Integer.valueOf(target.getTileX()), 
/* 1000 */               Integer.valueOf(target.getTileY()), 
/* 1001 */               Boolean.toString(target.isOnSurface()) });
/* 1002 */         logger.log(Level.WARNING, message, (Throwable)nsz);
/*      */       } 
/* 1004 */       return true;
/*      */     } 
/* 1006 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void updateItemModel(Item toUpdate) {
/* 1011 */     toUpdate.updateModelNameOnGroundItem();
/* 1012 */     toUpdate.updateName();
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
/*      */   private static final boolean targetIsDraggedCheck(Item target, Creature performer) {
/* 1024 */     if (target.isDraggable())
/*      */     {
/* 1026 */       if (Items.isItemDragged(target)) {
/*      */         
/* 1028 */         String message = StringUtil.format("You can not load the %s while it's being dragged.", new Object[] { target
/*      */               
/* 1030 */               .getName() });
/* 1031 */         performer.getCommunicator().sendNormalServerMessage(message);
/* 1032 */         return true;
/*      */       } 
/*      */     }
/* 1035 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean targetIsSameAsCarrier(Creature performer, Vehicle vehicle, Item target) {
/* 1040 */     if (vehicle.getWurmid() == target.getWurmId()) {
/*      */       
/* 1042 */       performer.getCommunicator().sendNormalServerMessage("You are unable to bend reality enough to accomplish that!");
/*      */       
/* 1044 */       return true;
/*      */     } 
/* 1046 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean targetIsNotOnTheGround(Item target, Creature performer, boolean finalCheck) {
/* 1051 */     if (target.getTopParent() != target.getWurmId()) {
/*      */       String message;
/*      */       
/* 1054 */       if (finalCheck) {
/* 1055 */         message = "The %s is no longer on the ground, so you can't load it.";
/*      */       } else {
/* 1057 */         message = "The %s needs to be on the ground.";
/* 1058 */       }  performer.getCommunicator().sendNormalServerMessage(
/* 1059 */           StringUtil.format(message, new Object[] { target.getName() }));
/* 1060 */       return true;
/*      */     } 
/*      */     
/* 1063 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean targetIsMoored(Item target, Creature player) {
/* 1068 */     if (target.getData() != -1L) {
/*      */       
/*      */       try {
/*      */         
/* 1072 */         Item anchor = Items.getItem(target.getData());
/* 1073 */         if (anchor.isAnchor())
/*      */         {
/* 1075 */           player.getCommunicator().sendNormalServerMessage("You are not allowed to load a moored ship.");
/*      */           
/* 1077 */           return true;
/*      */         }
/*      */       
/* 1080 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 1082 */         logger.log(Level.FINE, "Unable to find item with id: " + target.getData(), (Throwable)nsi);
/*      */       } 
/*      */     }
/* 1085 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean targetIsHitchedOrCommanded(Item target, Creature performer) {
/* 1090 */     if (target.isVehicle()) {
/*      */       
/* 1092 */       Vehicle targetVehicle = Vehicles.getVehicle(target);
/* 1093 */       if (targetVehicle == null)
/* 1094 */         return false; 
/*      */       int i;
/* 1096 */       for (i = 0; i < (targetVehicle.getHitched()).length; i++) {
/*      */         
/* 1098 */         if (targetVehicle.getHitched()[i].isOccupied()) {
/*      */           
/* 1100 */           String m = StringUtil.format("You can not load the %s while it's being dragged or has creatures hitched.", new Object[] { targetVehicle
/*      */                 
/* 1102 */                 .getName() });
/* 1103 */           performer.getCommunicator().sendNormalServerMessage(m);
/* 1104 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 1108 */       for (i = 0; i < (targetVehicle.getSeats()).length; i++) {
/*      */         
/* 1110 */         if (targetVehicle.getSeats()[i].isOccupied()) {
/*      */           
/* 1112 */           String m = StringUtil.format("You can not load the %s while someone is using it.", new Object[] { targetVehicle
/*      */                 
/* 1114 */                 .getName() });
/* 1115 */           performer.getCommunicator().sendNormalServerMessage(m);
/* 1116 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1120 */     return false;
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
/*      */   private static final boolean targetIsPlantedCheck(Item target, Creature performer) {
/* 1135 */     if (target.isEnchantedTurret()) {
/*      */       
/* 1137 */       if (target.isPlanted() && (target.getKingdom() != performer.getKingdomId() || target
/* 1138 */         .getLastOwnerId() != performer.getWurmId()))
/*      */       {
/* 1140 */         VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 1141 */         if (t != null)
/*      */         {
/* 1143 */           if (t.getVillage() != null && t.getVillage().isCitizen(performer) && target
/* 1144 */             .getKingdom() == performer.getKingdomId())
/*      */           {
/* 1146 */             return false;
/*      */           }
/*      */         }
/* 1149 */         String message = StringUtil.format("The %s is firmly planted in the ground.", new Object[] { target
/*      */               
/* 1151 */               .getName() });
/* 1152 */         performer.getCommunicator().sendNormalServerMessage(message, (byte)3);
/* 1153 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1158 */     else if (target.isPlanted() && !ItemBehaviour.isSignManipulationOk(target, performer, (short)6)) {
/*      */       
/* 1160 */       String message = StringUtil.format("The %s is firmly planted in the ground.", new Object[] { target
/*      */             
/* 1162 */             .getName() });
/* 1163 */       performer.getCommunicator().sendNormalServerMessage(message, (byte)3);
/* 1164 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1168 */     return false;
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
/*      */   private static final boolean targetIsNotTransportable(Item target, Creature performer) {
/* 1180 */     if (!target.getTemplate().isTransportable()) {
/*      */       
/* 1182 */       String message = StringUtil.format("%s is not transportable in this way.", new Object[] { target.getName() });
/* 1183 */       performer.getCommunicator().sendNormalServerMessage(message);
/* 1184 */       return true;
/*      */     } 
/* 1186 */     return false;
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
/*      */   private static final boolean targetCanNotBeInsertedCheck(Item target, Item carrier, Vehicle vehicle, Creature performer) {
/* 1201 */     boolean result = true;
/* 1202 */     int freevol = carrier.getFreeVolume();
/* 1203 */     int targetvol = target.getVolume();
/* 1204 */     if (!carrier.isBoat()) {
/*      */       
/* 1206 */       if (carrier.getContainerSizeX() >= target.getSizeX() && carrier
/* 1207 */         .getContainerSizeY() >= target.getSizeY() && carrier
/* 1208 */         .getContainerSizeZ() > target.getSizeZ() && freevol >= targetvol)
/*      */       {
/*      */         
/* 1211 */         result = !carrier.mayCreatureInsertItem();
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1216 */     else if (freevol >= targetvol) {
/* 1217 */       result = !carrier.mayCreatureInsertItem();
/*      */     } 
/*      */     
/* 1220 */     if (result) {
/*      */       
/* 1222 */       String message = StringUtil.format("There is not enough room in the %s for the %s.", new Object[] { vehicle
/*      */             
/* 1224 */             .getName(), target
/* 1225 */             .getName() });
/* 1226 */       performer.getCommunicator().sendNormalServerMessage(message);
/* 1227 */       return result;
/*      */     } 
/* 1229 */     return result;
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
/*      */   private static final boolean targetIsOnFireCheck(Item target, Creature performer, Item carrier) {
/* 1242 */     if (target.isOnFire() && !target.isAlwaysLit()) {
/*      */       
/* 1244 */       String message = StringUtil.format("The %s is still burning and can not be loaded on to the %s.", new Object[] { target
/*      */             
/* 1246 */             .getName(), carrier
/* 1247 */             .getName() });
/* 1248 */       performer.getCommunicator().sendNormalServerMessage(message);
/* 1249 */       return true;
/*      */     } 
/* 1251 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean targetHasActiveQueen(Item target, Creature performer, Item carrier, boolean load) {
/* 1256 */     if (target.getTemplateId() == 1175 && target.hasQueen())
/*      */     {
/* 1258 */       if (performer.getBestBeeSmoker() == null && performer.getPower() <= 1) {
/*      */         
/* 1260 */         performer.getCommunicator().sendSafeServerMessage("The bees get angry and defend the " + target
/* 1261 */             .getName() + " by stinging you.");
/* 1262 */         performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (4000.0F + Server.rand
/* 1263 */             .nextFloat() * 8000.0F), 0.0F, 25.0F, false, false);
/* 1264 */         return true;
/*      */       } 
/*      */     }
/* 1267 */     return false;
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
/*      */   private static final boolean targetIsNotEmptyContainerCheck(Item target, Creature performer, Item carrier, boolean load) {
/* 1300 */     return false;
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
/*      */   private static final boolean targetIsLoadedWarmachine(Item target, Creature performer, Item carrier) {
/* 1313 */     if (WarmachineBehaviour.isLoaded(target)) {
/*      */       
/* 1315 */       String message = StringUtil.format("The %s must be unloaded before being loaded on to the %s.", new Object[] { target
/*      */             
/* 1317 */             .getName(), carrier.getName() });
/* 1318 */       performer.getCommunicator().sendNormalServerMessage(message);
/* 1319 */       return true;
/*      */     } 
/* 1321 */     return false;
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
/*      */   private static final Item getCarrierItem(Vehicle vehicle, Creature performer) {
/*      */     try {
/* 1335 */       return Items.getItem(vehicle.getWurmid());
/*      */     }
/* 1337 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1339 */       String message = StringUtil.format("Unable to get vehicle item for vehicle with id: %d.", new Object[] {
/*      */             
/* 1341 */             Long.valueOf(vehicle.getWurmid()) });
/* 1342 */       logger.log(Level.WARNING, message, (Throwable)nsi);
/* 1343 */       return null;
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
/*      */   private static final Item getCarrierItemForTarget(Item target, Creature performer) {
/*      */     try {
/* 1358 */       return Items.getItem(target.getTopParent());
/*      */     }
/* 1360 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1362 */       String message = StringUtil.format("Unable to get parent vehicle for: %s with top parent: %d", new Object[] { target
/*      */             
/* 1364 */             .getName(), 
/* 1365 */             Long.valueOf(target.getTopParent()) });
/* 1366 */       logger.log(Level.WARNING, message, (Throwable)nsi);
/* 1367 */       return null;
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
/*      */   private static final int getLoadActionDistance(Item carrier) {
/* 1379 */     int DEFAULT_MAX_RANGE = 4;
/* 1380 */     if (!carrier.isVehicle())
/*      */     {
/* 1382 */       return 4;
/*      */     }
/*      */     
/* 1385 */     Vehicle vehicle = Vehicles.getVehicle(carrier);
/* 1386 */     if (vehicle != null) {
/* 1387 */       return vehicle.getMaxAllowedLoadDistance();
/*      */     }
/* 1389 */     return 4;
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
/*      */   private static final boolean targetIsLockedCheck(Item target, Creature performer, Vehicle vehicle) {
/* 1402 */     return targetIsLockedCheck(target, performer, vehicle, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean targetIsLockedCheck(Item target, Creature performer, Vehicle vehicle, boolean sendMessage) {
/* 1408 */     if (target.getLockId() != -10L) {
/*      */       
/* 1410 */       boolean locked = false;
/* 1411 */       boolean hasKey = false;
/*      */       
/*      */       try {
/* 1414 */         Item lock = Items.getItem(target.getLockId());
/* 1415 */         locked = lock.isLocked();
/* 1416 */         hasKey = performer.hasKeyForLock(lock);
/*      */         
/* 1418 */         if (target.getTemplateId() == 1311 && target.isLocked())
/*      */         {
/*      */           
/* 1421 */           if (target.mayAccessHold(performer))
/*      */           {
/* 1423 */             hasKey = true;
/*      */           }
/*      */           else
/*      */           {
/* 1427 */             if (sendMessage)
/*      */             {
/* 1429 */               performer.getCommunicator().sendNormalServerMessage("You cannot open the " + target
/* 1430 */                   .getName() + " so therefore cannot load it.");
/*      */             }
/* 1432 */             return locked;
/*      */           }
/*      */         
/*      */         }
/* 1436 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 1438 */         locked = true;
/*      */       } 
/* 1440 */       if (locked && !hasKey) {
/*      */         
/* 1442 */         if (sendMessage) {
/*      */           
/* 1444 */           String message = "The %s is locked. It needs to be unlocked before being loaded on to the %s.";
/* 1445 */           performer.getCommunicator().sendSafeServerMessage(
/* 1446 */               StringUtil.format("The %s is locked. It needs to be unlocked before being loaded on to the %s.", new Object[] { target.getName(), vehicle.getName() }));
/*      */         } 
/* 1448 */         return locked;
/*      */       } 
/*      */     } 
/*      */     
/* 1452 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean targetVehicleIsLockedCheck(Item target, Creature performer, Vehicle vehicle) {
/* 1457 */     if (targetIsLockedCheck(target, performer, vehicle, false) && 
/* 1458 */       !VehicleBehaviour.mayDriveVehicle(performer, target, null)) {
/*      */       
/* 1460 */       String message = "The %s is locked. It needs to be unlocked before being loaded on to the %s, or you must be allowed to embark as a driver.";
/*      */ 
/*      */       
/* 1463 */       performer.getCommunicator().sendSafeServerMessage(StringUtil.format("The %s is locked. It needs to be unlocked before being loaded on to the %s, or you must be allowed to embark as a driver.", new Object[] { target.getName(), vehicle.getName() }));
/* 1464 */       return true;
/*      */     } 
/* 1466 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean performerIsNotOnAVehicle(Creature performer) {
/* 1477 */     if (performer.getVehicle() == -10L) {
/*      */       
/* 1479 */       performer.getCommunicator().sendNormalServerMessage("You are not on a vehicle.");
/* 1480 */       return true;
/*      */     } 
/* 1482 */     return false;
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
/*      */   private static final boolean performerIsNotOnATransportVehicle(Creature performer, Vehicle vehicle) {
/* 1494 */     if (vehicle == null || vehicle.creature) {
/*      */       
/* 1496 */       performer.getCommunicator().sendNormalServerMessage("You are not the commander or passenger of a vehicle.");
/* 1497 */       return true;
/*      */     } 
/* 1499 */     return false;
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
/*      */   private static final boolean performerIsNotSeatedOnAVehicle(Creature performer, Seat seat) {
/* 1511 */     if (seat == null) {
/*      */       
/* 1513 */       performer.getCommunicator().sendNormalServerMessage("You need to be the commander or a passenger to do this.");
/* 1514 */       return true;
/*      */     } 
/* 1516 */     return false;
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
/*      */   private static final boolean performerIsWithinDistanceToTarget(Creature performer, Item target, int maxDistance) {
/* 1530 */     if (!performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), maxDistance)) {
/*      */       
/* 1532 */       performer.getCommunicator().sendNormalServerMessage("You need to be closer to do this.");
/* 1533 */       return false;
/*      */     } 
/* 1535 */     return true;
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
/*      */   private static final boolean carrierIsNotVehicle(Item carrier, Item target, Creature performer) {
/* 1548 */     if (!carrier.isVehicle()) {
/*      */       
/* 1550 */       String message = StringUtil.format("%s must be loaded on a vehicle for this to work.", new Object[] { target
/*      */             
/* 1552 */             .getName() });
/* 1553 */       performer.getCommunicator().sendNormalServerMessage(message, (byte)3);
/* 1554 */       return true;
/*      */     } 
/* 1556 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean performerMayNotStealCheck(Creature performer) {
/* 1567 */     if (!performer.maySteal()) {
/*      */       
/* 1569 */       performer.getCommunicator().sendNormalServerMessage("You need more body control to steal things.", (byte)3);
/*      */       
/* 1571 */       return true;
/*      */     } 
/* 1573 */     return false;
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
/*      */   private static final boolean performerIsTryingToStealInLawfulMode(Creature performer, boolean isStealing) {
/* 1585 */     if (isStealing && Action.checkLegalMode(performer)) {
/*      */       
/* 1587 */       performer.getCommunicator().sendNormalServerMessage("This would be stealing. You need to deactivate lafwful mode in order to steal.", (byte)3);
/*      */       
/* 1589 */       return true;
/*      */     } 
/* 1591 */     return false;
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
/*      */   private static final boolean perfomerActionTargetIsBlocked(Creature performer, Item target) {
/* 1603 */     BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/* 1604 */     if (result != null) {
/*      */       
/* 1606 */       String message = StringUtil.format("You can't reach the %s through the %s.", new Object[] { target
/*      */             
/* 1608 */             .getName(), result
/* 1609 */             .getFirstBlocker().getName() });
/* 1610 */       performer.getCommunicator().sendSafeServerMessage(message, (byte)3);
/* 1611 */       return true;
/*      */     } 
/* 1613 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean performerIsLastOwner(Creature performer, Item target) {
/* 1618 */     if (target.getLastOwnerId() != performer.getWurmId()) {
/*      */       
/* 1620 */       performer.getCommunicator().sendNormalServerMessage(
/* 1621 */           StringUtil.format("You are not the owner of the %s.", new Object[] { target.getName() }), (byte)3);
/* 1622 */       return false;
/*      */     } 
/* 1624 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isLargeMagicChest(Item target) {
/* 1629 */     return (target.getTemplateId() == 664);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isAutoRefillWell(Item target) {
/* 1634 */     if (target.getTemplateId() == 608) {
/*      */       
/* 1636 */       byte aux = target.getAuxData();
/* 1637 */       if (aux == 4 || aux == 5 || aux == 6)
/* 1638 */         return true; 
/*      */     } 
/* 1640 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean performerMayNotUseInventory(Creature performer, Item carrier) {
/* 1645 */     if (carrier.isLocked() && !MethodsItems.mayUseInventoryOfVehicle(performer, carrier)) {
/*      */       
/* 1647 */       String message = "You are not allowed to access the inventory of the %s.";
/* 1648 */       performer.getCommunicator().sendNormalServerMessage(
/* 1649 */           StringUtil.format("You are not allowed to access the inventory of the %s.", new Object[] { carrier.getName() }), (byte)3);
/* 1650 */       return true;
/*      */     } 
/* 1652 */     return false;
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
/*      */   private static final boolean targetIsOccupiedBed(Item target, Creature performer, short actionId) {
/* 1664 */     if (target.isBed()) {
/*      */       
/* 1666 */       PlayerInfo info = PlayerInfoFactory.getPlayerSleepingInBed(target.getWurmId());
/* 1667 */       if (info != null) {
/*      */         
/* 1669 */         if (actionId == 672 || actionId == 671) {
/*      */           
/* 1671 */           String message = StringUtil.format("You can not haul the %s because it's occupied by %s.", new Object[] { target
/*      */                 
/* 1673 */                 .getName(), info
/* 1674 */                 .getName() });
/* 1675 */           performer.getCommunicator().sendNormalServerMessage(message, (byte)3);
/*      */         }
/*      */         else {
/*      */           
/* 1679 */           String message = StringUtil.format("You can not load the %s because it's occupied by %s.", new Object[] { target
/*      */                 
/* 1681 */                 .getName(), info
/* 1682 */                 .getName() });
/* 1683 */           performer.getCommunicator().sendNormalServerMessage(message, (byte)3);
/*      */         } 
/* 1685 */         return true;
/*      */       } 
/*      */     } 
/* 1688 */     return false;
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
/*      */   public static final boolean unloadCargo(Creature performer, Item target, float counter) {
/* 1701 */     if (target.getTopParent() == target.getWurmId()) {
/* 1702 */       return true;
/*      */     }
/* 1704 */     Action act = null;
/*      */     
/*      */     try {
/* 1707 */       act = performer.getCurrentAction();
/*      */     }
/* 1709 */     catch (NoSuchActionException nsa) {
/*      */       
/* 1711 */       String message = StringUtil.format("Unable to find current action for %s in unloadCargo.", new Object[] { performer
/*      */             
/* 1713 */             .getName() });
/* 1714 */       logger.log(Level.WARNING, message, (Throwable)nsa);
/* 1715 */       return true;
/*      */     } 
/*      */     
/* 1718 */     if (target.getTemplateId() == 1311) {
/*      */       
/* 1720 */       VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), performer.isOnSurface());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1734 */       if (t.getFourPerTileCount(performer.getFloorLevel()) >= 4) {
/*      */         
/* 1736 */         performer.getCommunicator().sendNormalServerMessage("You cannot unload this here, there isn't enough room.");
/*      */         
/* 1738 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 1742 */     Item carrier = getCarrierItemForTarget(target, performer);
/* 1743 */     if (carrier == null) {
/* 1744 */       return true;
/*      */     }
/* 1746 */     if (carrierIsNotVehicle(carrier, target, performer)) {
/* 1747 */       return true;
/*      */     }
/* 1749 */     if (cantBeUnloaded(target, carrier, performer)) {
/* 1750 */       return true;
/*      */     }
/* 1752 */     if (!Methods.isActionAllowed(performer, (short)234)) {
/* 1753 */       return true;
/*      */     }
/* 1755 */     int distance = getLoadActionDistance(carrier);
/* 1756 */     if (!performerIsWithinDistanceToTarget(performer, target, distance)) {
/* 1757 */       return true;
/*      */     }
/* 1759 */     if (targetIsNotEmptyContainerCheck(target, performer, carrier, false)) {
/* 1760 */       return true;
/*      */     }
/* 1762 */     if (targetHasActiveQueen(target, performer, carrier, false)) {
/* 1763 */       return true;
/*      */     }
/* 1765 */     if (!mayUnloadHereCheck(target, performer)) {
/* 1766 */       return true;
/*      */     }
/* 1768 */     if (!isOnSameLevelUnload(carrier, performer)) {
/* 1769 */       return true;
/*      */     }
/* 1771 */     int time = Actions.getLoadUnloadActionTime(performer);
/*      */     
/* 1773 */     if (counter == 1.0F) {
/*      */       
/* 1775 */       if (!strengthCheck(performer, 23.0D)) {
/*      */         
/* 1777 */         performer.getCommunicator().sendNormalServerMessage("You are not strong enough to do this, you need at least 23 body strength.", (byte)3);
/*      */         
/* 1779 */         return true;
/*      */       } 
/* 1781 */       act.setTimeLeft(time);
/* 1782 */       String youMessage = StringUtil.format("You start to unload the %s from the %s.", new Object[] { target
/*      */             
/* 1784 */             .getName(), carrier
/* 1785 */             .getName() });
/* 1786 */       String broadcastMessage = StringUtil.format("%s starts to unload the %s from the %s.", new Object[] { performer
/*      */             
/* 1788 */             .getName(), target
/* 1789 */             .getName(), carrier
/* 1790 */             .getName() });
/* 1791 */       performer.getCommunicator().sendNormalServerMessage(youMessage);
/* 1792 */       Server.getInstance().broadCastAction(broadcastMessage, performer, 5);
/* 1793 */       performer.sendActionControl(Actions.actionEntrys[606].getVerbString(), true, time);
/*      */       
/* 1795 */       performer.getStatus().modifyStamina(-10000.0F);
/* 1796 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1800 */     time = act.getTimeLeft();
/* 1801 */     if (act.currentSecond() == 3) {
/*      */ 
/*      */       
/* 1804 */       Village village = Zones.getVillage(target.getTileX(), target.getTileY(), performer.isOnSurface());
/* 1805 */       VolaTile vt = Zones.getTileOrNull(target.getTileX(), target.getTileY(), performer.isOnSurface());
/* 1806 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 1807 */       if ((!performer.isOnSurface() || structure == null || !structure.isTypeHouse() || 
/* 1808 */         !structure.isFinished() || !structure.isActionAllowed(performer, (short)605)) && village != null && 
/* 1809 */         !village.isActionAllowed((short)605, performer))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1815 */         performer.getCommunicator().sendServerMessage("WARNING: You currently do not have permissions to re-load this item.", 255, 127, 63);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1822 */     if (counter * 10.0F > time) {
/*      */ 
/*      */       
/*      */       try {
/* 1826 */         carrier.dropItem(target.getWurmId(), false);
/*      */       }
/* 1828 */       catch (NoSuchItemException nsi) {
/*      */         
/* 1830 */         String message = StringUtil.format("Unable to find and drop item: %s with id:%d", new Object[] { target
/*      */               
/* 1832 */               .getName(), 
/* 1833 */               Long.valueOf(target.getWurmId()) });
/* 1834 */         logger.log(Level.WARNING, message, (Throwable)nsi);
/* 1835 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 1840 */         Zone zone = Zones.getZone(performer.getTileX(), performer.getTileY(), performer.isOnSurface());
/*      */         
/* 1842 */         target.setPos(performer.getPosX(), performer.getPosY(), performer.getPositionZ(), target
/* 1843 */             .getRotation(), performer.getBridgeId());
/* 1844 */         zone.addItem(target);
/* 1845 */         if (target.getLockId() == -10L && !target.isVehicle() && !isLargeMagicChest(target))
/* 1846 */           target.setLastOwnerId(performer.getWurmId()); 
/* 1847 */         if (target.getTemplateId() == 891)
/* 1848 */           target.setLastOwnerId(performer.getWurmId()); 
/* 1849 */         if (carrier.getTemplateId() == 853 || carrier.getTemplateId() == 1410) {
/* 1850 */           updateItemModel(carrier);
/*      */         }
/* 1852 */         String youMessage = StringUtil.format("You finish unloading the %s from the %s.", new Object[] { target
/*      */               
/* 1854 */               .getName(), carrier
/* 1855 */               .getName() });
/* 1856 */         String broadcastMessage = StringUtil.format("%s finishes unloading the %s from the %s.", new Object[] { performer
/*      */               
/* 1858 */               .getName(), target
/* 1859 */               .getName(), carrier
/* 1860 */               .getName() });
/* 1861 */         performer.getCommunicator().sendNormalServerMessage(youMessage);
/* 1862 */         Server.getInstance().broadCastAction(broadcastMessage, performer, 5);
/*      */         
/* 1864 */         return true;
/*      */       }
/* 1866 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 1868 */         String message = StringUtil.format("Unable to find zone for x:%d y:%d surface:%s.", new Object[] {
/*      */               
/* 1870 */               Integer.valueOf(performer.getTileX()), 
/* 1871 */               Integer.valueOf(performer.getTileY()), 
/* 1872 */               Boolean.toString(performer.isOnSurface()) });
/* 1873 */         logger.log(Level.WARNING, message, (Throwable)nsz);
/* 1874 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 1878 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean mayUnloadHereCheck(Item target, Creature player) {
/* 1883 */     VolaTile tile = Zones.getTileOrNull(player.getTileX(), player.getTileY(), player.isOnSurface());
/* 1884 */     if (tile == null)
/* 1885 */       return false; 
/* 1886 */     int level = tile.getDropFloorLevel(player.getFloorLevel(true));
/*      */     
/* 1888 */     if (tile.getNumberOfItems(level) >= 100) {
/*      */       
/* 1890 */       String message = "You cannot unload the %s here, since there are too many items here already.";
/* 1891 */       player.getCommunicator().sendNormalServerMessage(StringUtil.format("You cannot unload the %s here, since there are too many items here already.", new Object[] {
/* 1892 */               StringUtil.toLowerCase(target.getName()) }), (byte)3);
/* 1893 */       return false;
/*      */     } 
/* 1895 */     if (tile.getNumberOfDecorations(level) >= 15 && !target.isCrate()) {
/*      */       
/* 1897 */       String message = "You cannot unload the %s here, since there are too many decorations here already.";
/* 1898 */       player.getCommunicator().sendNormalServerMessage(StringUtil.format("You cannot unload the %s here, since there are too many decorations here already.", new Object[] {
/* 1899 */               StringUtil.toLowerCase(target.getName()) }), (byte)3);
/* 1900 */       return false;
/*      */     } 
/* 1902 */     if ((target.isOnePerTile() && tile.hasOnePerTileItem(level)) || (target
/* 1903 */       .isFourPerTile() && tile.getFourPerTileCount(level) == 4)) {
/*      */       
/* 1905 */       String message = "You cannot unload the %s here, since there is not enough space in front of you.";
/* 1906 */       player.getCommunicator().sendNormalServerMessage(StringUtil.format("You cannot unload the %s here, since there is not enough space in front of you.", new Object[] {
/* 1907 */               StringUtil.toLowerCase(target.getName()) }), (byte)3);
/* 1908 */       return false;
/*      */     } 
/* 1910 */     if (target.isOutsideOnly() && tile.getStructure() != null) {
/*      */       
/* 1912 */       String message = "You cannot unload the %s here, it must be unloaded outside.";
/* 1913 */       player.getCommunicator().sendNormalServerMessage(StringUtil.format("You cannot unload the %s here, it must be unloaded outside.", new Object[] {
/* 1914 */               StringUtil.toLowerCase(target.getName()) }), (byte)3);
/* 1915 */       return false;
/*      */     } 
/* 1917 */     if (target.isSurfaceOnly() && !player.isOnSurface()) {
/*      */       
/* 1919 */       String message = "You cannot unload the %s here, it must be unloaded on the surface.";
/* 1920 */       player.getCommunicator().sendNormalServerMessage(StringUtil.format("You cannot unload the %s here, it must be unloaded on the surface.", new Object[] {
/* 1921 */               StringUtil.toLowerCase(target.getName()) }), (byte)3);
/* 1922 */       return false;
/*      */     } 
/*      */     
/* 1925 */     if (player.isOnSurface() && target.isSurfaceOnly()) {
/*      */       
/* 1927 */       int encodedTile = Server.surfaceMesh.getTile(player.getTileX(), player.getTileY());
/* 1928 */       byte tileType = Tiles.decodeType(encodedTile);
/* 1929 */       if (tileType == 0 || Tiles.isMineDoor(tileType)) {
/*      */ 
/*      */         
/* 1932 */         String message = "You cannot unload the %s here, it cannot be unloaded on a mine door.";
/* 1933 */         player.getCommunicator().sendNormalServerMessage(StringUtil.format("You cannot unload the %s here, it cannot be unloaded on a mine door.", new Object[] {
/* 1934 */                 StringUtil.toLowerCase(target.getName()) }), (byte)3);
/* 1935 */         return false;
/*      */       } 
/*      */     } 
/* 1938 */     return true;
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
/*      */   public static final boolean strengthCheck(Creature player, double neededStrength) {
/*      */     try {
/* 1952 */       Skill strength = player.getSkills().getSkill(102);
/* 1953 */       return (strength.getRealKnowledge() >= neededStrength);
/*      */     }
/* 1955 */     catch (NoSuchSkillException nss) {
/*      */       
/* 1957 */       logger.log(Level.WARNING, "Unable to find body strength of player: " + player.getName(), (Throwable)nss);
/* 1958 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final List<ActionEntry> getHaulActions(Creature player, Item target) {
/* 1964 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 1965 */     if (target.getTemplate().isTransportable() && !target.isBoat()) {
/*      */       
/* 1967 */       int playerFloorLevel = player.getFloorLevel(true);
/* 1968 */       int targetFloorLevel = target.getFloorLevel();
/* 1969 */       if (playerFloorLevel > targetFloorLevel) {
/*      */         
/* 1971 */         int floorDiff = playerFloorLevel - targetFloorLevel;
/* 1972 */         if (playerFloorLevel > 0 && floorDiff == 1)
/*      */         {
/* 1974 */           VolaTile tile = player.getCurrentTile();
/* 1975 */           if (tile == null)
/* 1976 */             return toReturn; 
/* 1977 */           Structure structure = tile.getStructure();
/* 1978 */           if (structure == null) {
/* 1979 */             return toReturn;
/*      */           }
/* 1981 */           Floor[] floors = structure.getFloorsAtTile(tile.tilex, tile.tiley, playerFloorLevel * 30, playerFloorLevel * 30);
/*      */           
/* 1983 */           if (floors == null || floors.length == 0)
/* 1984 */             return toReturn; 
/* 1985 */           if (floors[0].getType() != StructureConstants.FloorType.OPENING) {
/* 1986 */             return toReturn;
/*      */           }
/* 1988 */           toReturn.add(Actions.actionEntrys[671]);
/*      */         }
/*      */       
/* 1991 */       } else if (playerFloorLevel == targetFloorLevel) {
/*      */         
/* 1993 */         VolaTile tile = player.getCurrentTile();
/* 1994 */         if (tile == null)
/* 1995 */           return toReturn; 
/* 1996 */         Structure structure = tile.getStructure();
/* 1997 */         if (structure == null) {
/* 1998 */           return toReturn;
/*      */         }
/* 2000 */         Floor[] floors = structure.getFloorsAtTile(tile.tilex, tile.tiley, playerFloorLevel * 30, playerFloorLevel * 30);
/*      */         
/* 2002 */         if (floors == null || floors.length == 0)
/* 2003 */           return toReturn; 
/* 2004 */         if (floors[0].getType() != StructureConstants.FloorType.OPENING)
/* 2005 */           return toReturn; 
/* 2006 */         toReturn.add(Actions.actionEntrys[672]);
/*      */       } 
/*      */     } 
/* 2009 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean haul(Creature performer, Item target, float counter, short actionId, Action act) {
/* 2015 */     double strNeeded = 21.0D;
/* 2016 */     if (!strengthCheck(performer, 21.0D)) {
/*      */       
/* 2018 */       performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You need at least %.2f Body Strength to haul.", new Object[] {
/* 2019 */               Double.valueOf(21.0D)
/*      */             }), (byte)3);
/* 2021 */       return true;
/*      */     } 
/*      */     
/* 2024 */     if (targetIsNotOnTheGround(target, performer, false))
/*      */     {
/* 2026 */       return true;
/*      */     }
/*      */     
/* 2029 */     if (isAutoRefillWell(target)) {
/*      */       
/* 2031 */       performer
/* 2032 */         .getCommunicator()
/* 2033 */         .sendNormalServerMessage("This is a special version of the item that is designed to exist only on starter deeds, and is therefor not transportable.");
/*      */       
/* 2035 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2039 */     if (!target.getTemplate().isTransportable()) {
/*      */       
/* 2041 */       performer.getCommunicator().sendNormalServerMessage("You can not haul this item.", (byte)3);
/* 2042 */       return true;
/*      */     } 
/* 2044 */     if (target.getTemplate().isContainerWithSubItems())
/*      */     {
/* 2046 */       for (Item i : target.getItems()) {
/* 2047 */         if (i.isPlacedOnParent()) {
/*      */           
/* 2049 */           performer.getCommunicator().sendNormalServerMessage("You can not haul this item while it has items placed on top of it.", (byte)3);
/*      */           
/* 2051 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 2056 */     if (target.isBoat()) {
/*      */       
/* 2058 */       performer.getCommunicator().sendNormalServerMessage("You may not haul boats up or down in houses.", (byte)3);
/*      */       
/* 2060 */       return true;
/*      */     } 
/*      */     
/* 2063 */     if (targetIsPlantedCheck(target, performer)) {
/* 2064 */       return true;
/*      */     }
/* 2066 */     if (targetIsOccupiedBed(target, performer, actionId)) {
/* 2067 */       return true;
/*      */     }
/*      */     
/* 2070 */     if (target.isVehicle()) {
/*      */       
/* 2072 */       if (target.getTemplateId() == 853 || target.getTemplateId() == 1410)
/*      */       {
/* 2074 */         if (target.getItemCount() != 0) {
/*      */           
/* 2076 */           performer.getCommunicator().sendNormalServerMessage(StringUtil.format("The %s needs to be empty before you can haul it.", new Object[] { target
/* 2077 */                   .getName() }), (byte)3);
/*      */           
/* 2079 */           return true;
/*      */         } 
/*      */       }
/*      */       
/* 2083 */       Vehicle vehicle = Vehicles.getVehicle(target);
/* 2084 */       if (vehicle.isAnySeatOccupied()) {
/*      */         
/* 2086 */         performer.getCommunicator().sendNormalServerMessage(
/* 2087 */             StringUtil.format("You may not haul the %s when it's in use by another player.", new Object[] {
/* 2088 */                 target.getName()
/*      */               }), (byte)3);
/* 2090 */         return true;
/*      */       } 
/*      */       
/* 2093 */       if (vehicle.isAnythingHitched()) {
/*      */         
/* 2095 */         performer.getCommunicator().sendNormalServerMessage(
/* 2096 */             StringUtil.format("You may not haul the %s when there are creatures hitched to it.", new Object[] {
/* 2097 */                 target.getName()
/*      */               }), (byte)3);
/* 2099 */         return true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2105 */     if (target.isDraggable())
/*      */     {
/* 2107 */       if (Items.isItemDragged(target)) {
/*      */         
/* 2109 */         performer.getCommunicator().sendNormalServerMessage(
/* 2110 */             StringUtil.format("You can not haul the %s while it's being dragged.", new Object[] { target.getName() }), (byte)3);
/*      */         
/* 2112 */         return true;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 2117 */     int playerFloorLevel = performer.getFloorLevel(true);
/* 2118 */     int targetFloorLevel = target.getFloorLevel();
/* 2119 */     int diff = Math.abs(playerFloorLevel - targetFloorLevel);
/* 2120 */     if (actionId == 671) {
/*      */       
/* 2122 */       if (diff > 1)
/*      */       {
/* 2124 */         performer.getCommunicator().sendNormalServerMessage("The difference in floor levels is to great, you need to be closer.", (byte)3);
/*      */ 
/*      */         
/* 2127 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 2132 */     else if (diff != 0) {
/*      */       
/* 2134 */       performer.getCommunicator().sendNormalServerMessage("You must be on the same floor level.", (byte)3);
/*      */       
/* 2136 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2140 */     if (!performerIsWithinDistanceToTarget(performer, target, 4)) {
/* 2141 */       return true;
/*      */     }
/* 2143 */     boolean onDeed = Actions.canReceiveDeedSpeedBonus(performer);
/* 2144 */     int time = onDeed ? 10 : 50;
/*      */     
/* 2146 */     if (counter == 1.0F) {
/*      */       
/* 2148 */       if (!strengthCheck(performer, 21.0D)) {
/*      */         
/* 2150 */         performer.getCommunicator().sendNormalServerMessage(StringUtil.format("You need at least %.2f Body Strength to haul.", new Object[] {
/* 2151 */                 Double.valueOf(21.0D)
/*      */               }), (byte)3);
/* 2153 */         return true;
/*      */       } 
/*      */       
/* 2156 */       act.setTimeLeft(time);
/* 2157 */       performer.getCommunicator().sendNormalServerMessage("You start to haul the " + target.getName() + ".");
/* 2158 */       Server.getInstance().broadCastAction(performer.getName() + " starts to haul the " + target.getName() + ".", performer, 5);
/*      */       
/* 2160 */       performer.sendActionControl(Actions.actionEntrys[actionId].getVerbString(), true, time);
/*      */       
/* 2162 */       performer.getStatus().modifyStamina(-10000.0F);
/* 2163 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2168 */     time = act.getTimeLeft();
/* 2169 */     if (act.currentSecond() % 5 == 0) {
/* 2170 */       performer.getStatus().modifyStamina(-10000.0F);
/*      */     }
/*      */ 
/*      */     
/* 2174 */     if (counter * 10.0F > time) {
/*      */       
/* 2176 */       int tileX = target.getTileX();
/* 2177 */       int tileY = target.getTileY();
/*      */ 
/*      */       
/*      */       try {
/* 2181 */         Zone zone = Zones.getZone(tileX, tileY, target.isOnSurface());
/* 2182 */         float z = 0.0F;
/* 2183 */         if (actionId == 671) {
/*      */ 
/*      */           
/* 2186 */           z = Zones.calculatePosZ(performer.getPosX(), performer.getPosY(), performer.getCurrentTile(), target
/* 2187 */               .isOnSurface(), performer.isOnSurface(), target.getPosZ(), performer, -10L);
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 2193 */           z = Zones.calculateHeight(target.getPosX(), target.getPosY(), target.isOnSurface()) + (performer.getFloorLevel() - 1) * 3.0F;
/*      */         } 
/*      */ 
/*      */         
/* 2197 */         zone.removeItem(target);
/* 2198 */         target.setPosXYZ(performer.getPosX(), performer.getPosY(), z);
/* 2199 */         zone.addItem(target);
/*      */       
/*      */       }
/* 2202 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 2204 */         logger.log(Level.WARNING, "Unable to find zone for item.", (Throwable)nsz);
/*      */       } 
/* 2206 */       return true;
/*      */     } 
/*      */     
/* 2209 */     return false;
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
/*      */   public static final List<ActionEntry> getLoadUnloadActions(Creature player, Item target) {
/* 2221 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 2222 */     if (!target.getTemplate().isTransportable() && !target.isBoat() && !target.isUnfinished())
/* 2223 */       return toReturn; 
/* 2224 */     if (target.getTopParent() == target.getWurmId()) {
/*      */       
/* 2226 */       if (player.getVehicle() != -10L) {
/*      */         
/* 2228 */         Vehicle vehicle = Vehicles.getVehicleForId(player.getVehicle());
/* 2229 */         if (vehicle != null && !vehicle.creature && !vehicle.isChair()) {
/*      */           
/* 2231 */           Seat seat = vehicle.getSeatFor(player.getWurmId());
/* 2232 */           if (seat != null) {
/*      */             try
/*      */             {
/*      */               
/* 2236 */               Item vehicleItem = Items.getItem(vehicle.getWurmid());
/* 2237 */               if (target.isBoat() && vehicleItem.getTemplateId() != 853) {
/* 2238 */                 return toReturn;
/*      */               }
/* 2240 */               if (target.isUnfinished() && vehicleItem.getTemplateId() == 853) {
/*      */                 
/* 2242 */                 ItemTemplate template = target.getRealTemplate();
/* 2243 */                 if (template == null)
/* 2244 */                   return toReturn; 
/* 2245 */                 if (!template.isBoat()) {
/* 2246 */                   return toReturn;
/*      */                 }
/*      */               } 
/* 2249 */               if (MethodsItems.mayUseInventoryOfVehicle(player, vehicleItem) || vehicleItem
/* 2250 */                 .getLockId() == -10L || 
/* 2251 */                 Items.isItemDragged(vehicleItem))
/*      */               {
/* 2253 */                 toReturn.add(Actions.actionEntrys[605]);
/*      */               }
/*      */             }
/* 2256 */             catch (NoSuchItemException nsi)
/*      */             {
/* 2258 */               String message = StringUtil.format("Unable to find vehicle item with id: %s.", new Object[] {
/*      */                     
/* 2260 */                     Long.valueOf(vehicle.getWurmid()) });
/* 2261 */               logger.log(Level.WARNING, message, (Throwable)nsi);
/*      */             }
/*      */           
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 2269 */       Vehicle vehicle = Vehicles.getVehicleForId(target.getTopParent());
/*      */       
/*      */       try {
/* 2272 */         if (vehicle != null)
/*      */         {
/* 2274 */           Item vehicleItem = Items.getItem(vehicle.getWurmid());
/* 2275 */           if (vehicle != null && !vehicle.creature && (
/* 2276 */             MethodsItems.mayUseInventoryOfVehicle(player, vehicleItem) || vehicleItem
/* 2277 */             .getLockId() == -10L || 
/* 2278 */             Items.isItemDragged(vehicleItem)))
/*      */           {
/* 2280 */             toReturn.add(Actions.actionEntrys[606]);
/*      */           }
/*      */         }
/*      */       
/* 2284 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 2286 */         String message = StringUtil.format("Unable to find vehicle item with id: %d.", new Object[] {
/*      */               
/* 2288 */               Long.valueOf(vehicle.getWurmid()) });
/* 2289 */         logger.log(Level.WARNING, message, (Throwable)nsi);
/*      */       } 
/*      */     } 
/* 2292 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean cantBeUnloaded(Item target, Item carrier, Creature performer) {
/* 2297 */     if (target.getTopParentOrNull() != null)
/*      */     {
/* 2299 */       if (target.getParentOrNull() != null && target.getParentOrNull().isVehicle() && target
/* 2300 */         .getTopParentOrNull() != target.getParentOrNull()) {
/*      */         
/* 2302 */         performer.getCommunicator().sendNormalServerMessage("You can't unload the " + target.getName() + " from there. Unload the " + target
/* 2303 */             .getParentOrNull().getName() + " first.");
/* 2304 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 2308 */     if ((target.isBoat() || isUnfinishedBoat(target)) && carrier.getTemplateId() == 853)
/*      */     {
/* 2310 */       return false;
/*      */     }
/* 2312 */     if (target.getTemplateId() == 1311 && carrier.getTemplateId() == 1410)
/*      */     {
/* 2314 */       return false;
/*      */     }
/* 2316 */     if (!targetIsNotTransportable(target, performer))
/*      */     {
/* 2318 */       return false;
/*      */     }
/* 2320 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isUnfinishedBoat(Item item) {
/* 2325 */     return (item.isUnfinished() && item.getRealTemplate() != null && item.getRealTemplate().isBoat());
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
/*      */   static boolean loadChicken(Creature performer, Item target, float counter) {
/*      */     Action act;
/* 2339 */     Creature[] folls = performer.getFollowers();
/*      */     
/*      */     try {
/* 2342 */       act = performer.getCurrentAction();
/*      */     }
/* 2344 */     catch (NoSuchActionException nsa) {
/*      */       
/* 2346 */       logger.log(Level.WARNING, "Unable to get current action in loadChicken().", (Throwable)nsa);
/* 2347 */       return true;
/*      */     } 
/*      */     
/* 2350 */     if (folls.length > 1) {
/*      */       
/* 2352 */       performer.getCommunicator().sendNormalServerMessage("You are currently leading too many creatures.");
/* 2353 */       return true;
/*      */     } 
/*      */     
/* 2356 */     if (target.isLocked() && !target.mayAccessHold(performer)) {
/*      */       
/* 2358 */       performer.getCommunicator().sendNormalServerMessage("You cant put a chicken in this coop, its locked.");
/* 2359 */       return true;
/*      */     } 
/*      */     
/* 2362 */     if (target.getCurrentQualityLevel() < 10.0F) {
/*      */       
/* 2364 */       performer.getCommunicator().sendNormalServerMessage("The coop is in too poor of shape to be used.");
/* 2365 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2369 */     for (Item item : target.getAllItems(true)) {
/*      */       
/* 2371 */       if (item.getTemplateId() == 1434)
/*      */       {
/* 2373 */         if (item.isEmpty(true)) {
/*      */           
/* 2375 */           performer.getCommunicator().sendNormalServerMessage("You need to put food in the " + item.getName() + " first.");
/* 2376 */           return true;
/*      */         } 
/*      */       }
/* 2379 */       if (item.getTemplateId() == 1435)
/*      */       {
/* 2381 */         if (item.isEmpty(true)) {
/*      */           
/* 2383 */           performer.getCommunicator().sendNormalServerMessage("You need to put water in the " + item.getName() + " first.");
/* 2384 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 2389 */     if (target.getParentId() != -10L) {
/*      */       
/* 2391 */       performer.getCommunicator().sendNormalServerMessage("You must unload the coop to load creatures into it.");
/*      */       
/* 2393 */       return true;
/*      */     } 
/*      */     
/* 2396 */     int time = Actions.getLoadUnloadActionTime(performer);
/*      */     
/* 2398 */     if (counter == 1.0F) {
/*      */       
/* 2400 */       for (Creature foll : folls) {
/*      */ 
/*      */         
/* 2403 */         for (Item item : target.getAllItems(true)) {
/*      */           
/* 2405 */           if (item.getTemplateId() == 1436) {
/*      */             
/*      */             try {
/*      */               
/* 2409 */               if ((item.getAllItems(true)).length >= (int)item.getParent().getQualityLevel() / 10 + item.getParent().getRarity())
/*      */               {
/* 2411 */                 performer.getCommunicator().sendNormalServerMessage("The " + foll
/* 2412 */                     .getName() + " refuses to enter the coop. There is no space.");
/* 2413 */                 return true;
/*      */               }
/*      */             
/* 2416 */             } catch (NoSuchItemException ex) {
/*      */               
/* 2418 */               logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/* 2423 */         if (foll.getTemplate().getTemplateId() != 45) {
/*      */           
/* 2425 */           performer.getCommunicator().sendNormalServerMessage("The " + foll
/* 2426 */               .getName() + " refuses to enter the coop.");
/* 2427 */           return true;
/*      */         } 
/* 2429 */         if (!performer.isWithinDistanceTo(foll, 5.0F)) {
/*      */ 
/*      */           
/* 2432 */           performer.getCommunicator().sendNormalServerMessage("You are too far away from the creature.");
/* 2433 */           return true;
/*      */         } 
/* 2435 */         if (!foll.isWithinDistanceTo(target, 5.0F)) {
/*      */           
/* 2437 */           performer.getCommunicator().sendNormalServerMessage("The creature is too far away from the coop.");
/* 2438 */           return true;
/*      */         } 
/* 2440 */         if (!performer.isWithinDistanceTo(target, 5.0F)) {
/*      */           
/* 2442 */           performer.getCommunicator().sendNormalServerMessage("You are too far away from the coop.");
/* 2443 */           return true;
/*      */         } 
/* 2445 */         act.setTimeLeft(time);
/* 2446 */         performer.getCommunicator().sendNormalServerMessage("You start to load the " + foll.getName() + ".");
/* 2447 */         Server.getInstance().broadCastAction(performer.getName() + " starts to load the " + foll.getName() + ".", performer, 5);
/* 2448 */         performer.sendActionControl(Actions.actionEntrys[907].getVerbString(), true, time);
/* 2449 */         performer.getStatus().modifyStamina(-10000.0F);
/*      */       } 
/* 2451 */       return false;
/*      */     } 
/*      */     
/* 2454 */     if (counter * 10.0F > time) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 2459 */         for (Creature foll : folls)
/*      */         {
/* 2461 */           Item i = ItemFactory.createItem(1310, (foll.getStatus()).age, (byte)0, null);
/* 2462 */           for (Item item : target.getAllItems(true)) {
/*      */             
/* 2464 */             if (item.getTemplateId() == 1436) {
/*      */               
/* 2466 */               i.setData(foll.getWurmId());
/* 2467 */               i.setName(foll.getName());
/* 2468 */               i.setWeight((int)foll.getWeight(), false);
/* 2469 */               item.insertItem(i, true);
/*      */             } 
/*      */           } 
/*      */           
/* 2473 */           DbCreatureStatus.setLoaded(1, foll.getWurmId());
/*      */           
/* 2475 */           target.setAuxData((byte)foll.getTemplate().getTemplateId());
/* 2476 */           foll.setLeader(null);
/* 2477 */           foll.getCurrentTile().deleteCreature(foll);
/*      */           
/*      */           try {
/* 2480 */             foll.savePosition(-10);
/* 2481 */             foll.getStatus().setDead(true);
/*      */           }
/* 2483 */           catch (IOException ex) {
/*      */             
/* 2485 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */           } 
/* 2487 */           foll.destroyVisionArea();
/* 2488 */           updateItemModel(target);
/*      */           
/* 2490 */           target.setData(System.currentTimeMillis());
/* 2491 */           performer.getCommunicator().sendNormalServerMessage("You finish loading the " + foll
/* 2492 */               .getName() + ".");
/* 2493 */           Server.getInstance().broadCastAction(performer
/* 2494 */               .getName() + " finishes loading the " + foll.getName() + ".", performer, 5);
/*      */         }
/*      */       
/* 2497 */       } catch (FailedException|com.wurmonline.server.items.NoSuchTemplateException ex) {
/*      */         
/* 2499 */         logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */       } 
/* 2501 */       return true;
/*      */     } 
/* 2503 */     return false;
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
/*      */   static boolean unloadChicken(Creature performer, Item target, float counter) {
/*      */     Action act;
/*      */     try {
/* 2519 */       act = performer.getCurrentAction();
/*      */     }
/* 2521 */     catch (NoSuchActionException nsa) {
/*      */       
/* 2523 */       logger.log(Level.WARNING, "Unable to get current action in loadChicken().", (Throwable)nsa);
/* 2524 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2528 */     if (!Methods.isActionAllowed(performer, (short)234))
/*      */     {
/* 2530 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 2535 */       Creature getCreature = Creatures.getInstance().getCreature(target.getData());
/* 2536 */       if (getCreature.getDominator() != null)
/*      */       {
/* 2538 */         if (getCreature.getDominator() != performer)
/*      */         {
/* 2540 */           performer.getCommunicator().sendNormalServerMessage("You cannot unload this creature, it is not your pet.");
/* 2541 */           return true;
/*      */         }
/*      */       
/*      */       }
/* 2545 */     } catch (NoSuchCreatureException e) {
/*      */       
/* 2547 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     } 
/*      */     
/* 2550 */     if (target.getData() == -10L) {
/*      */       
/* 2552 */       logger.log(Level.WARNING, target.getWurmId() + " has no data, this should not happen, destroying.");
/* 2553 */       Items.destroyItem(target.getWurmId());
/* 2554 */       return true;
/*      */     } 
/*      */     
/* 2557 */     int time = Actions.getLoadUnloadActionTime(performer);
/*      */     
/* 2559 */     if (counter == 1.0F) {
/*      */       
/* 2561 */       act.setTimeLeft(time);
/* 2562 */       performer.getCommunicator().sendNormalServerMessage("You start to unload the " + target.getName() + ".");
/* 2563 */       Server.getInstance().broadCastAction(performer.getName() + " starts to unload the " + target.getName() + ".", performer, 5);
/* 2564 */       performer.sendActionControl(Actions.actionEntrys[908].getVerbString(), true, time);
/* 2565 */       performer.getStatus().modifyStamina(-10000.0F);
/* 2566 */       return false;
/*      */     } 
/*      */     
/* 2569 */     if (counter * 10.0F > time) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 2574 */         Creature getCreature = Creatures.getInstance().getCreature(target.getData());
/*      */ 
/*      */         
/* 2577 */         DbCreatureStatus.setLoaded(0, getCreature.getWurmId());
/*      */ 
/*      */         
/* 2580 */         Creatures cstat = Creatures.getInstance();
/* 2581 */         getCreature.getStatus().setDead(false);
/* 2582 */         cstat.removeCreature(getCreature);
/* 2583 */         cstat.addCreature(getCreature, false);
/* 2584 */         getCreature.putInWorld();
/*      */ 
/*      */         
/* 2587 */         CreatureBehaviour.blinkTo(getCreature, performer.getPosX(), performer.getPosY(), performer.getLayer(), performer.getPositionZ(), performer.getBridgeId(), performer.getFloorLevel());
/* 2588 */         performer.getCommunicator().sendNormalServerMessage("You finish unloading the " + target.getName() + ".");
/* 2589 */         Server.getInstance().broadCastAction(performer.getName() + " finishes unloading the " + target.getName() + ".", performer, 5);
/*      */ 
/*      */         
/* 2592 */         getCreature.save();
/* 2593 */         getCreature.savePosition(target.getZoneId());
/*      */ 
/*      */         
/* 2596 */         if ((performer.getFollowers()).length < 4) {
/*      */           
/* 2598 */           getCreature.setLeader(performer);
/* 2599 */           performer.addFollower(getCreature, performer.getLeadingItem(getCreature));
/*      */         } 
/*      */ 
/*      */         
/* 2603 */         Item nestingBox = target.getParent();
/* 2604 */         Item coop = nestingBox.getParent();
/* 2605 */         Items.destroyItem(target.getWurmId());
/* 2606 */         updateItemModel(coop);
/*      */       }
/* 2608 */       catch (NoSuchCreatureException|IOException|NoSuchItemException ex) {
/*      */         
/* 2610 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/* 2612 */       return true;
/*      */     } 
/* 2614 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\CargoTransportationMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */