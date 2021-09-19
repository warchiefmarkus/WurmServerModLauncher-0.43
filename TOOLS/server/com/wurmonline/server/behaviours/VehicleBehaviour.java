/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.PlonkData;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.MountAction;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.DbItem;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemSettings;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.ManageObjectList;
/*      */ import com.wurmonline.server.questions.ManagePermissions;
/*      */ import com.wurmonline.server.questions.PermissionsHistory;
/*      */ import com.wurmonline.server.questions.SetDestinationQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class VehicleBehaviour
/*      */   extends ItemBehaviour
/*      */ {
/*   81 */   private static final Logger logger = Logger.getLogger(VehicleBehaviour.class.getName());
/*      */   
/*      */   private static Vehicle vehicle;
/*      */   private static boolean addedPassenger;
/*      */   private static boolean addedDriver;
/*      */   
/*      */   public VehicleBehaviour() {
/*   88 */     super((short)41);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*   99 */     List<ActionEntry> toReturn = new LinkedList<>();
/*      */     
/*  101 */     toReturn.addAll(super.getBehavioursFor(performer, target));
/*  102 */     toReturn.addAll(getVehicleBehaviours(performer, target));
/*      */     
/*  104 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<ActionEntry> getVehicleBehaviours(Creature performer, Item target) {
/*  109 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  110 */     if (performer.getVehicle() == -10L && target.getOwnerId() == -10L)
/*      */     {
/*  112 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 8.0F))
/*      */       {
/*  114 */         if (performer.isSwimming() || performer
/*  115 */           .getFloorLevel() == target.getFloorLevel() || performer.getPower() > 0) {
/*      */           
/*  117 */           VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), performer.isOnSurface());
/*  118 */           if (t == null || performer
/*  119 */             .getCurrentTile().getStructure() == t.getStructure() || (performer
/*  120 */             .getCurrentTile().getStructure() != null && performer
/*  121 */             .getCurrentTile().getStructure().isTypeBridge()) || (t
/*  122 */             .getStructure() != null && t.getStructure().isTypeBridge())) {
/*      */             
/*  124 */             BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  125 */             if (result == null) {
/*      */               
/*  127 */               boolean havePermission = hasPermission(performer, target);
/*  128 */               addedPassenger = !((havePermission || target.mayPassenger(performer)) ? 1 : 0);
/*  129 */               addedDriver = !((havePermission || target.mayCommand(performer)) ? 1 : 0);
/*  130 */               VehicleBehaviour.vehicle = Vehicles.getVehicle(target);
/*  131 */               if (VehicleBehaviour.vehicle != null) {
/*      */                 
/*  133 */                 if (!VehicleBehaviour.vehicle.isUnmountable())
/*      */                 {
/*  135 */                   for (int x = 0; x < VehicleBehaviour.vehicle.seats.length; x++) {
/*      */                     
/*  137 */                     if (!addedDriver && !VehicleBehaviour.vehicle.seats[x].isOccupied() && (VehicleBehaviour.vehicle.seats[x]).type == 0) {
/*      */ 
/*      */                       
/*  140 */                       if (!Items.isItemDragged(target))
/*  141 */                         toReturn.add(Actions.actionEntrys[331]); 
/*  142 */                       addedDriver = true;
/*      */                     }
/*  144 */                     else if (!addedPassenger && !VehicleBehaviour.vehicle.seats[x].isOccupied() && (VehicleBehaviour.vehicle.seats[x]).type == 1) {
/*      */ 
/*      */                       
/*  147 */                       if (VehicleBehaviour.vehicle.isChair()) {
/*      */                         
/*  149 */                         toReturn.add(Actions.actionEntrys[701]);
/*      */                         
/*  151 */                         if (VehicleBehaviour.vehicle.seats.length == 2 && VehicleBehaviour.vehicle.seats[0]
/*  152 */                           .getType() == 1 && VehicleBehaviour.vehicle.seats[1]
/*  153 */                           .getType() == 1)
/*      */                         {
/*  155 */                           if (!VehicleBehaviour.vehicle.seats[0].isOccupied())
/*      */                           {
/*  157 */                             toReturn.add(Actions.actionEntrys[702]);
/*      */                           }
/*  159 */                           if (!VehicleBehaviour.vehicle.seats[1].isOccupied())
/*      */                           {
/*  161 */                             toReturn.add(Actions.actionEntrys[703]);
/*      */                           }
/*      */                         }
/*      */                       
/*      */                       } else {
/*      */                         
/*  167 */                         toReturn.add(Actions.actionEntrys[332]);
/*      */                       } 
/*  169 */                       addedPassenger = true;
/*  170 */                       if (addedDriver)
/*      */                         break; 
/*      */                     } 
/*      */                   } 
/*      */                 }
/*  175 */                 if (VehicleBehaviour.vehicle.hitched.length > 0)
/*      */                 {
/*  177 */                   if ((performer.getFollowers()).length > 0)
/*      */                   {
/*  179 */                     if (!Items.isItemDragged(target) && target
/*  180 */                       .getTopParent() == target.getWurmId() && (
/*  181 */                       !target.isTent() || target.getLastOwnerId() == performer.getWurmId())) {
/*  182 */                       toReturn.add(Actions.actionEntrys[377]);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     }
/*  192 */     if (performer.getVehicle() != -10L) {
/*      */       
/*  194 */       if (performer.getVehicle() == target.getWurmId() && target.isBoat()) {
/*      */         
/*  196 */         if (target.getData() != -1L)
/*      */         {
/*  198 */           if (performer.isVehicleCommander())
/*  199 */             toReturn.add(Actions.actionEntrys[361]); 
/*      */         }
/*  201 */         if (target.getExtra() != -1L)
/*      */         {
/*      */           
/*  204 */           toReturn.add(new ActionEntry((short)944, "Detach keep net", "detaching"));
/*      */         }
/*  206 */         toReturn.add(Actions.actionEntrys[383]);
/*      */         
/*  208 */         if ((Features.Feature.BOAT_DESTINATION.isEnabled() || performer.getPower() >= 2) && performer
/*  209 */           .isVehicleCommander())
/*  210 */           toReturn.add(Actions.actionEntrys[717]); 
/*      */       } 
/*  212 */       Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle());
/*  213 */       if (vehicle.isChair()) {
/*  214 */         toReturn.add(Actions.actionEntrys[708]);
/*      */       } else {
/*  216 */         toReturn.add(Actions.actionEntrys[333]);
/*      */       } 
/*      */     } 
/*  219 */     if (target.getExtra() != -1L)
/*      */     {
/*  221 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), (target
/*  222 */           .isVehicle() && !target.isTent()) ? Math.max(6, target.getSizeZ() / 100) : 6.0F)) {
/*      */         
/*  224 */         boolean watching = false;
/*      */         
/*      */         try {
/*  227 */           Item keepnet = Items.getItem(target.getExtra());
/*  228 */           Creature[] watchers = keepnet.getWatchers();
/*  229 */           for (Creature lWatcher : watchers) {
/*      */             
/*  231 */             if (lWatcher == performer) {
/*      */               
/*  233 */               watching = true;
/*      */               break;
/*      */             } 
/*      */           } 
/*  237 */           if (watching) {
/*  238 */             toReturn.add(Actions.actionEntrys[941]);
/*      */           }
/*  240 */         } catch (NoSuchItemException|NoSuchCreatureException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  245 */         if (!watching)
/*  246 */           toReturn.add(Actions.actionEntrys[940]); 
/*      */       } 
/*      */     }
/*  249 */     List<ActionEntry> permissions = new LinkedList<>();
/*  250 */     if (target.mayManage(performer))
/*      */     {
/*      */       
/*  253 */       if (target.getTemplateId() == 186) {
/*  254 */         permissions.add(Actions.actionEntrys[687]);
/*  255 */       } else if (target.getTemplateId() == 539) {
/*  256 */         permissions.add(Actions.actionEntrys[665]);
/*  257 */       } else if (target.getTemplateId() == 850) {
/*  258 */         permissions.add(Actions.actionEntrys[669]);
/*  259 */       } else if (target.getTemplateId() == 853) {
/*  260 */         permissions.add(new ActionEntry((short)669, "Manage Ship Carrier", "managing"));
/*  261 */       } else if (target.getTemplateId() == 1410) {
/*  262 */         permissions.add(new ActionEntry((short)669, "Manage creature transporter", "managing"));
/*  263 */       } else if (target.isBoat()) {
/*  264 */         permissions.add(Actions.actionEntrys[668]);
/*      */       }  } 
/*  266 */     if (target.maySeeHistory(performer))
/*      */     {
/*  268 */       if (target.getTemplateId() == 186) {
/*  269 */         permissions.add(new ActionEntry((short)691, "History of Small Cart", "viewing"));
/*  270 */       } else if (target.getTemplateId() == 539) {
/*  271 */         permissions.add(new ActionEntry((short)691, "History of Large Cart", "viewing"));
/*  272 */       } else if (target.getTemplateId() == 850) {
/*  273 */         permissions.add(new ActionEntry((short)691, "History of Wagon", "viewing"));
/*  274 */       } else if (target.getTemplateId() == 853) {
/*  275 */         permissions.add(new ActionEntry((short)691, "History of Ship Carrier", "viewing"));
/*  276 */       } else if (target.getTemplateId() == 1410) {
/*  277 */         permissions.add(new ActionEntry((short)691, "History of Creature Transporter", "viewing"));
/*  278 */       } else if (target.isBoat()) {
/*  279 */         permissions.add(new ActionEntry((short)691, "History of Ship", "viewing"));
/*      */       }  } 
/*  281 */     if (!permissions.isEmpty()) {
/*      */       
/*  283 */       if (permissions.size() > 1) {
/*      */         
/*  285 */         Collections.sort(permissions);
/*  286 */         toReturn.add(new ActionEntry((short)-permissions.size(), "Permissions", "viewing"));
/*      */       } 
/*  288 */       toReturn.addAll(permissions);
/*      */     } 
/*  290 */     if (target.getWurmId() == performer.getVehicle()) {
/*      */       
/*  292 */       if (performer.isVehicleCommander()) {
/*      */         
/*  294 */         VehicleBehaviour.vehicle = Vehicles.getVehicle(target);
/*  295 */         if (VehicleBehaviour.vehicle != null)
/*      */         {
/*  297 */           if (VehicleBehaviour.vehicle.getDraggers() != null && VehicleBehaviour.vehicle.getDraggers().size() > 0)
/*      */           {
/*  299 */             toReturn.add(new ActionEntry((short)-1, "Animals", "Animal options"));
/*  300 */             toReturn.add(Actions.actionEntrys[378]);
/*      */           }
/*      */         
/*      */         }
/*      */       } 
/*  305 */     } else if (performer.getVehicle() == -10L) {
/*      */       
/*  307 */       if (hasKeyForVehicle(performer, target) || mayDriveVehicle(performer, target, null)) {
/*      */         
/*  309 */         VehicleBehaviour.vehicle = Vehicles.getVehicle(target);
/*  310 */         if (VehicleBehaviour.vehicle != null)
/*      */         {
/*  312 */           if (VehicleBehaviour.vehicle.getDraggers() != null && VehicleBehaviour.vehicle.getDraggers().size() > 0) {
/*      */             
/*  314 */             toReturn.add(new ActionEntry((short)-1, "Animals", "Animal options"));
/*  315 */             toReturn.add(Actions.actionEntrys[378]);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  320 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  331 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  332 */     toReturn.addAll(super.getBehavioursFor(performer, source, target));
/*  333 */     toReturn.addAll(getVehicleBehaviours(performer, target));
/*  334 */     if (performer.getVehicle() == target.getWurmId() && target.isBoat()) {
/*      */       
/*  336 */       if (target.getData() == -1L)
/*      */       {
/*  338 */         if (source.isAnchor()) {
/*  339 */           toReturn.add(Actions.actionEntrys[360]);
/*      */         }
/*      */       }
/*  342 */       if ((target.getTemplateId() == 490 || target.getTemplateId() == 491) && target.getExtra() == -1L)
/*      */       {
/*  344 */         if (source.getTemplateId() == 1342) {
/*  345 */           toReturn.add(new ActionEntry((short)943, "Attach keep net", "attaching"));
/*      */         }
/*      */       }
/*  348 */       if (source.isDredgingTool() && target.isBoat() && performer
/*  349 */         .getVehicle() == target.getWurmId() && performer.isOnSurface()) {
/*  350 */         toReturn.add(Actions.actionEntrys[362]);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  356 */       if (source.getTemplateId() == 1344 || source.getTemplateId() == 1346)
/*      */       {
/*  358 */         toReturn.add(Actions.actionEntrys[160]);
/*      */       }
/*      */ 
/*      */       
/*  362 */       if (source.getTemplateId() == 1344 || source.getTemplateId() == 1346)
/*      */       {
/*  364 */         toReturn.add(new ActionEntry((short)285, "Lore", "thinking"));
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  373 */     return toReturn;
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
/*      */   public static final boolean hasKeyForVehicle(Creature performer, Item aVehicle) {
/*  390 */     if (aVehicle.isTent()) {
/*      */       
/*  392 */       if (aVehicle.getLastOwnerId() == performer.getWurmId()) {
/*  393 */         return true;
/*      */       }
/*  395 */       return false;
/*      */     } 
/*  397 */     long lockId = aVehicle.getLockId();
/*  398 */     if (lockId != -10L) {
/*      */       
/*      */       try {
/*      */         
/*  402 */         Item lock = Items.getItem(lockId);
/*  403 */         if (performer.hasKeyForLock(lock))
/*  404 */           return true; 
/*  405 */         return false;
/*      */       }
/*  407 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  414 */     VolaTile vt = Zones.getTileOrNull(aVehicle.getTileX(), aVehicle.getTileY(), aVehicle.isOnSurface());
/*  415 */     Village vill = (vt == null) ? null : vt.getVillage();
/*  416 */     return (vill == null || vill.isActionAllowed((short)6, performer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  427 */     boolean done = true;
/*  428 */     if (action == 331 || action == 332 || action == 701 || action == 702 || action == 703) {
/*      */ 
/*      */       
/*  431 */       if (target.getTopParent() != target.getWurmId()) {
/*      */         
/*  433 */         performer.getCommunicator().sendNormalServerMessage(
/*  434 */             StringUtil.format("You can't embark upon the %s right now, it needs to be on the ground.", new Object[] {
/*  435 */                 target.getName() }));
/*  436 */         return true;
/*      */       } 
/*  438 */       if (performer.getFloorLevel() != target.getFloorLevel() && !performer.isSwimming()) {
/*      */         
/*  440 */         performer.getCommunicator().sendNormalServerMessage("You are too far away now.");
/*  441 */         return done;
/*      */       } 
/*  443 */       if (!GeneralUtilities.isOnSameLevel(performer, target))
/*      */       {
/*  445 */         performer.getCommunicator().sendNormalServerMessage("You must be on the same level to embark.");
/*      */       }
/*  447 */       if (performer.isClimbing()) {
/*      */         
/*  449 */         performer.getCommunicator().sendNormalServerMessage("You need to stop climbing first.");
/*  450 */         return done;
/*      */       } 
/*  452 */       if (Math.abs(target.getPosZ() - performer.getPositionZ()) > 4.0F) {
/*      */         
/*  454 */         performer.getCommunicator().sendNormalServerMessage("You need to get closer to the " + target.getName() + ".");
/*  455 */         return done;
/*      */       } 
/*  457 */       if (target.isChair() && target.getPosZ() < 0.0F) {
/*      */         
/*  459 */         performer.getCommunicator().sendNormalServerMessage("You cannot sit on " + target.getName() + ". It is too wet.");
/*  460 */         return done;
/*      */       } 
/*  462 */       if (target.isOwnedByWagoner()) {
/*      */         
/*  464 */         performer.getCommunicator().sendNormalServerMessage("You cannot use the " + target.getName() + "  as a wagoner owns it.");
/*  465 */         return done;
/*      */       } 
/*  467 */       if (performer.getPower() < 2 && action == 331 && targetHasActiveQueen(target, performer))
/*      */       {
/*  469 */         performer.getCommunicator().sendNormalServerMessage("The bees sting you.");
/*      */       }
/*  471 */       if (performer.getVehicle() == -10L && target.getOwnerId() == -10L)
/*      */       {
/*  473 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 8.0F)) {
/*      */           
/*  475 */           if (target.getCurrentQualityLevel() < 10.0F) {
/*      */             
/*  477 */             performer.getCommunicator().sendNormalServerMessage("The " + target
/*  478 */                 .getName() + " is in too poor shape to be used.");
/*  479 */             return true;
/*      */           } 
/*  481 */           if (target.isOnSurface() != performer.isOnSurface()) {
/*      */             
/*  483 */             if (performer.isOnSurface()) {
/*  484 */               performer.getCommunicator().sendNormalServerMessage("You need to enter the cave first.");
/*      */             } else {
/*  486 */               performer.getCommunicator().sendNormalServerMessage("You need to leave the cave first.");
/*  487 */             }  return true;
/*      */           } 
/*      */ 
/*      */           
/*  491 */           if (performer.getBridgeId() != target.getBridgeId()) {
/*      */             
/*  493 */             performer.getCommunicator().sendNormalServerMessage("You need to be in the same structure as the " + target
/*  494 */                 .getName() + ".");
/*  495 */             return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  535 */           vehicle = Vehicles.getVehicle(target);
/*  536 */           if (vehicle != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  552 */             String vehicName = Vehicle.getVehicleName(vehicle);
/*  553 */             if (action == 331)
/*      */             {
/*  555 */               if (hasKeyForVehicle(performer, target) || mayDriveVehicle(performer, target, act))
/*      */               {
/*  557 */                 if (!Items.isItemDragged(target)) {
/*      */                   
/*  559 */                   if (canBeDriverOfVehicle(performer, vehicle)) {
/*      */                     
/*  561 */                     addedDriver = false;
/*  562 */                     for (int x = 0; x < vehicle.seats.length; x++) {
/*      */                       
/*  564 */                       if (!vehicle.seats[x].isOccupied() && (vehicle.seats[x]).type == 0) {
/*      */ 
/*      */                         
/*  567 */                         float r = -(target.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/*  568 */                         float s = (float)Math.sin(r);
/*  569 */                         float c = (float)Math.cos(r);
/*  570 */                         float xo = s * -(vehicle.seats[x]).offx - c * -(vehicle.seats[x]).offy;
/*  571 */                         float yo = c * -(vehicle.seats[x]).offx + s * -(vehicle.seats[x]).offy;
/*  572 */                         float newposx = target.getPosX() + xo;
/*  573 */                         float newposy = target.getPosY() + yo;
/*  574 */                         BlockingResult result = Blocking.getBlockerBetween(performer, performer
/*  575 */                             .getPosX(), performer.getPosY(), newposx, newposy, performer
/*  576 */                             .getPositionZ(), target.getPosZ(), performer.isOnSurface(), target
/*  577 */                             .isOnSurface(), false, 4, -1L, performer
/*  578 */                             .getBridgeId(), target.getBridgeId(), false);
/*      */                         
/*  580 */                         if (result == null) {
/*      */                           
/*  582 */                           TileEvent.log(target.getTileX(), target.getTileY(), 
/*  583 */                               target.isOnSurface() ? 0 : -1, performer
/*  584 */                               .getWurmId(), action);
/*  585 */                           addedDriver = true;
/*  586 */                           vehicle.seats[x].occupy(vehicle, performer);
/*  587 */                           vehicle.pilotId = performer.getWurmId();
/*  588 */                           performer.setVehicleCommander(true);
/*  589 */                           MountAction m = new MountAction(null, target, vehicle, x, true, (vehicle.seats[x]).offz);
/*      */                           
/*  591 */                           performer.setMountAction(m);
/*  592 */                           performer.setVehicle(target.getWurmId(), true, (byte)0);
/*      */ 
/*      */                           
/*  595 */                           if (performer.isPlayer() && ((Player)performer).getAlcohol() > 5.0F)
/*      */                           {
/*  597 */                             if (target.isBoat()) {
/*  598 */                               performer.achievement(133);
/*  599 */                             } else if (!target.isChair()) {
/*  600 */                               performer.achievement(134);
/*      */                             }  } 
/*  602 */                           switch (target.getTemplateId()) {
/*      */                             
/*      */                             case 540:
/*  605 */                               performer.achievement(54);
/*      */                               break;
/*      */                             case 542:
/*  608 */                               performer.achievement(55);
/*      */                               break;
/*      */                             case 541:
/*  611 */                               performer.achievement(56);
/*      */                               break;
/*      */                             case 490:
/*  614 */                               performer.achievement(57);
/*      */                               break;
/*      */                             case 491:
/*  617 */                               performer.achievement(58);
/*      */                               break;
/*      */                             case 543:
/*  620 */                               performer.achievement(59);
/*      */                               break;
/*      */                           } 
/*      */ 
/*      */ 
/*      */                           
/*  626 */                           if (vehicle.hasDestinationSet()) {
/*      */                             
/*  628 */                             ServerEntry entry = vehicle.getDestinationServer();
/*  629 */                             if (!Servers.mayEnterServer(performer, entry) || ((entry.PVPSERVER || entry
/*  630 */                               .isChaosServer()) && ((Player)performer)
/*  631 */                               .isBlockingPvP())) {
/*      */                               
/*  633 */                               vehicle.clearDestination();
/*  634 */                               performer.getCommunicator()
/*  635 */                                 .sendAlertServerMessage("The previous course is unavailable and has been cleared.");
/*      */                             }
/*      */                             else {
/*      */                               
/*  639 */                               performer.getCommunicator()
/*  640 */                                 .sendAlertServerMessage("The " + vehicName + " is on a course for " + entry
/*  641 */                                   .getName() + ".");
/*      */                             } 
/*      */                           } 
/*      */                           
/*  645 */                           if (target.isBoat() && !PlonkData.ON_A_BOAT.hasSeenThis(performer))
/*  646 */                             PlonkData.ON_A_BOAT.trigger(performer); 
/*      */                           break;
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*  651 */                     if (!addedDriver)
/*      */                     {
/*  653 */                       String text = "You may not %s the %s as a %s right now.The space is occupied or unreachable.";
/*      */ 
/*      */                       
/*  656 */                       performer.getCommunicator().sendNormalServerMessage(
/*  657 */                           StringUtil.format("You may not %s the %s as a %s right now.The space is occupied or unreachable.", new Object[] { vehicle.embarkString, vehicName, vehicle.pilotName }));
/*      */                     
/*      */                     }
/*      */ 
/*      */                   
/*      */                   }
/*      */                   else {
/*      */                     
/*  665 */                     String text = "You are not smart enough to figure out how to be the %s of the %s. You need %.2f in %s.";
/*      */                     
/*  667 */                     performer.getCommunicator().sendNormalServerMessage(
/*  668 */                         StringUtil.format("You are not smart enough to figure out how to be the %s of the %s. You need %.2f in %s.", new Object[] {
/*      */ 
/*      */                             
/*  671 */                             vehicle.pilotName, vehicName, Float.valueOf(vehicle.skillNeeded), 
/*  672 */                             SkillSystem.getNameFor(100)
/*      */                           }));
/*      */                   } 
/*      */                 } else {
/*      */                   
/*  677 */                   String text = "You may not %s the %s as a %s right now. It is being dragged.";
/*  678 */                   performer.getCommunicator().sendNormalServerMessage(
/*  679 */                       StringUtil.format("You may not %s the %s as a %s right now. It is being dragged.", new Object[] { vehicle.embarkString, vehicName, vehicle.pilotName }));
/*      */                 
/*      */                 }
/*      */ 
/*      */               
/*      */               }
/*      */               else
/*      */               {
/*  687 */                 String text = "You are not allowed to %s the %s as a %s.";
/*  688 */                 performer.getCommunicator().sendNormalServerMessage(
/*  689 */                     StringUtil.format("You are not allowed to %s the %s as a %s.", new Object[] { vehicle.embarkString, vehicName, vehicle.pilotName }));
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/*  695 */             else if (action == 332 || action == 701 || action == 702 || action == 703)
/*      */             {
/*      */               
/*  698 */               actionEmbarkPassenger(performer, target, action);
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/*  704 */           else if (action == 701 || action == 702 || action == 703) {
/*  705 */             performer.getCommunicator().sendNormalServerMessage("You can't sit on that right now.");
/*      */           } else {
/*  707 */             performer.getCommunicator().sendNormalServerMessage("You can't embark on that right now.");
/*      */           } 
/*      */         } else {
/*      */           
/*  711 */           performer.getCommunicator().sendNormalServerMessage("You are too far away now.");
/*      */         } 
/*      */       }
/*  714 */     } else if (Actions.isActionDisembark(action)) {
/*      */       
/*  716 */       if (performer.getVehicle() != -10L)
/*      */       {
/*  718 */         if (performer.getVisionArea() != null)
/*  719 */           performer.getVisionArea().broadCastUpdateSelectBar(performer.getWurmId(), true); 
/*  720 */         performer.disembark(true);
/*      */       }
/*      */     
/*  723 */     } else if (action == 361) {
/*      */       
/*  725 */       done = actionRaiseAnchor(performer, target, counter, done);
/*      */     }
/*  727 */     else if (action == 944) {
/*      */       
/*  729 */       done = actionDetachKeepnet(act, performer, target, counter, done);
/*      */     }
/*  731 */     else if (action == 940) {
/*      */       
/*  733 */       if (target.getExtra() != -1L)
/*      */       {
/*  735 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 8.0F)) {
/*      */           
/*      */           try {
/*      */             
/*  739 */             Item keepnet = Items.getItem(target.getExtra());
/*  740 */             if (performer.addItemWatched(keepnet)) {
/*      */               
/*  742 */               performer.getCommunicator().sendOpenInventoryWindow(keepnet.getWurmId(), keepnet.getName());
/*  743 */               keepnet.addWatcher(keepnet.getWurmId(), performer);
/*  744 */               keepnet.sendContainedItems(keepnet.getWurmId(), performer);
/*      */             } 
/*  746 */             performer.getCommunicator().sendUpdateSelectBar(target.getWurmId(), false);
/*      */           }
/*  748 */           catch (NoSuchItemException e) {
/*      */ 
/*      */             
/*  751 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */           } 
/*      */         }
/*      */       }
/*      */     } else {
/*  756 */       if (action == 941) {
/*      */         
/*  758 */         if (target.getExtra() != -1L) {
/*      */           
/*      */           try {
/*      */             
/*  762 */             Item keepnet = Items.getItem(target.getExtra());
/*  763 */             keepnet.close(performer);
/*  764 */             performer.getCommunicator().sendUpdateSelectBar(target.getWurmId(), false);
/*      */           }
/*  766 */           catch (NoSuchItemException e) {
/*      */ 
/*      */             
/*  769 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */           } 
/*      */         }
/*  772 */         return true;
/*      */       } 
/*  774 */       if (action == 717 && (Features.Feature.BOAT_DESTINATION.isEnabled() || performer.getPower() >= 2))
/*      */       
/*  776 */       { SetDestinationQuestion sdq = new SetDestinationQuestion(performer, target);
/*  777 */         sdq.sendQuestion();
/*  778 */         done = true; }
/*      */       
/*  780 */       else if (action == 717 && Features.Feature.BOAT_DESTINATION.isEnabled())
/*      */       
/*  782 */       { SetDestinationQuestion sdq = new SetDestinationQuestion(performer, target);
/*  783 */         sdq.sendQuestion();
/*  784 */         done = true; }
/*      */       
/*  786 */       else if (action == 378)
/*      */       
/*  788 */       { boolean ok = false;
/*  789 */         if (target.getWurmId() == performer.getVehicle()) {
/*      */           
/*  791 */           if (performer.isVehicleCommander())
/*      */           {
/*  793 */             ok = true;
/*      */           }
/*      */         }
/*  796 */         else if (performer.getVehicle() == -10L) {
/*      */           
/*  798 */           if (hasKeyForVehicle(performer, target) || mayDriveVehicle(performer, target, act))
/*  799 */             ok = true; 
/*      */         } 
/*  801 */         if (!ok) {
/*      */           
/*  803 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to do that.");
/*  804 */           return true;
/*      */         } 
/*  806 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 8.0F)) {
/*      */           
/*  808 */           vehicle = Vehicles.getVehicle(target);
/*  809 */           if (vehicle != null)
/*      */           {
/*  811 */             if (vehicle.draggers != null)
/*      */             {
/*  813 */               if (vehicle.draggers.size() > 0)
/*      */               {
/*  815 */                 Creature[] crets = vehicle.draggers.<Creature>toArray(new Creature[vehicle.draggers.size()]);
/*  816 */                 for (int x = 0; x < crets.length; x++)
/*      */                 {
/*  818 */                   if (!vehicle.positionDragger(crets[x], performer)) {
/*      */                     
/*  820 */                     performer.getCommunicator().sendNormalServerMessage("You can't unhitch the " + crets[x]
/*  821 */                         .getName() + " here. Please move the vehicle.");
/*  822 */                     return true;
/*      */                   } 
/*  824 */                   Zone z = null;
/*      */                   
/*      */                   try {
/*  827 */                     z = Zones.getZone(crets[x].getTileX(), crets[x].getTileY(), crets[x]
/*  828 */                         .isOnSurface());
/*  829 */                     crets[x].getStatus().savePosition(crets[x].getWurmId(), true, z.getId(), true);
/*      */                   }
/*  831 */                   catch (Exception exception) {}
/*      */ 
/*      */                   
/*  834 */                   Creatures.getInstance().setLastLed(crets[x].getWurmId(), performer.getWurmId());
/*  835 */                   vehicle.removeDragger(crets[x]);
/*  836 */                   VolaTile t = crets[x].getCurrentTile();
/*  837 */                   if (t != null)
/*      */                   {
/*  839 */                     t.sendAttachCreature(crets[x].getWurmId(), -10L, 0.0F, 0.0F, 0.0F, 0);
/*  840 */                     if (z != null) {
/*      */                       
/*      */                       try {
/*      */                         
/*  844 */                         z.removeCreature(crets[x], true, false);
/*  845 */                         z.addCreature(crets[x].getWurmId());
/*      */                       }
/*  847 */                       catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */                       
/*  850 */                       } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */                     }
/*      */                   }
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         } else {
/*      */           
/*  862 */           performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/*  863 */           return true;
/*      */         }
/*      */          }
/*  866 */       else if (action == 383)
/*      */       
/*  868 */       { if (performer.getVehicle() == target.getWurmId()) {
/*      */           
/*  870 */           Item[] its = target.getAllItems(true);
/*  871 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  872 */               .getName() + " contains " + its.length + " items.");
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */           
/*  881 */           performer.getCommunicator().sendNormalServerMessage("You miscalculate.. 200-43.. is that 12? No. No that can't be it. Damn it's so hard.");
/*      */         } 
/*  883 */         done = true; }
/*      */       
/*  885 */       else if (action == 687)
/*      */       
/*  887 */       { if (target.mayManage(performer))
/*      */         {
/*  889 */           ManagePermissions mp = new ManagePermissions(performer, ManageObjectList.Type.SMALL_CART, (PermissionsPlayerList.ISettings)target, false, -10L, false, null, "");
/*      */           
/*  891 */           mp.sendQuestion();
/*      */         }
/*      */          }
/*  894 */       else if (action == 665)
/*      */       
/*  896 */       { if (target.mayManage(performer))
/*      */         {
/*  898 */           ManagePermissions mp = new ManagePermissions(performer, ManageObjectList.Type.LARGE_CART, (PermissionsPlayerList.ISettings)target, false, -10L, false, null, "");
/*      */           
/*  900 */           mp.sendQuestion();
/*      */         }
/*      */          }
/*  903 */       else if (action == 669)
/*      */       
/*  905 */       { if (target.mayManage(performer))
/*      */         {
/*  907 */           ManageObjectList.Type molt = ManageObjectList.Type.WAGON;
/*  908 */           if (target.getTemplateId() == 853) {
/*  909 */             molt = ManageObjectList.Type.SHIP_CARRIER;
/*  910 */           } else if (target.getTemplateId() == 1410) {
/*  911 */             molt = ManageObjectList.Type.CREATURE_CARRIER;
/*  912 */           }  ManagePermissions mp = new ManagePermissions(performer, molt, (PermissionsPlayerList.ISettings)target, false, -10L, false, null, "");
/*      */           
/*  914 */           mp.sendQuestion();
/*      */         }
/*      */          }
/*  917 */       else if (action == 668)
/*      */       
/*  919 */       { if (target.mayManage(performer))
/*      */         {
/*  921 */           ManagePermissions mp = new ManagePermissions(performer, ManageObjectList.Type.SHIP, (PermissionsPlayerList.ISettings)target, false, -10L, false, null, "");
/*      */           
/*  923 */           mp.sendQuestion();
/*      */         }
/*      */          }
/*  926 */       else if (action == 691)
/*      */       
/*  928 */       { if (target.maySeeHistory(performer))
/*      */         {
/*  930 */           PermissionsHistory ph = new PermissionsHistory(performer, target.getWurmId());
/*  931 */           ph.sendQuestion();
/*      */         }
/*      */          }
/*  934 */       else if (action == 377)
/*      */       
/*  936 */       { if (performer.getDraggedItem() != null && performer.getDraggedItem() == target) {
/*      */           
/*  938 */           performer.getCommunicator().sendNormalServerMessage("You must stop dragging the " + target
/*  939 */               .getName() + " before you hitch creatures to it.");
/*  940 */           return true;
/*      */         } 
/*  942 */         if (target.getTopParent() != target.getWurmId()) {
/*      */           
/*  944 */           String message = "The %s needs to be on the ground before you can hitch anything to it.";
/*  945 */           performer.getCommunicator().sendNormalServerMessage(
/*  946 */               StringUtil.format("The %s needs to be on the ground before you can hitch anything to it.", new Object[] { target.getName() }));
/*  947 */           return true;
/*      */         } 
/*  949 */         if (Items.isItemDragged(target)) {
/*      */           
/*  951 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  952 */               .getName() + " is dragged and you can not hitch creatures to it.");
/*  953 */           return true;
/*      */         } 
/*  955 */         if (target.getCurrentQualityLevel() < 10.0F && !target.isTent()) {
/*      */           
/*  957 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  958 */               .getName() + " is in too poor shape to be dragged by animals.");
/*  959 */           return true;
/*      */         } 
/*  961 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 8.0F)) {
/*      */           
/*  963 */           if (hasKeyForVehicle(performer, target) || mayDriveVehicle(performer, target, act)) {
/*      */             
/*  965 */             if (!target.isTent() || target.getLastOwnerId() == performer.getWurmId()) {
/*      */               
/*  967 */               vehicle = Vehicles.getVehicle(target);
/*  968 */               actionHitch(performer, target);
/*      */             } 
/*      */           } else {
/*      */             
/*  972 */             performer.getCommunicator().sendNormalServerMessage("You can't mount that right now so you can't hitch either.");
/*      */           } 
/*      */         } else {
/*      */           
/*  976 */           performer.getCommunicator().sendNormalServerMessage("You can't reach that right now.");
/*  977 */         }  done = true; }
/*      */       else
/*      */       
/*  980 */       { done = super.action(act, performer, target, action, counter); } 
/*  981 */     }  return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean targetHasActiveQueen(Item carrier, Creature performer) {
/*  987 */     for (Item item : carrier.getItemsAsArray()) {
/*      */       
/*  989 */       if (item.getTemplateId() == 1175 && item.hasQueen() && !WurmCalendar.isSeasonWinter())
/*      */       {
/*  991 */         if (performer.getBestBeeSmoker() == null) {
/*      */           
/*  993 */           performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (4000.0F + Server.rand
/*  994 */               .nextFloat() * 3000.0F), 0.0F, 0.0F, false, false);
/*  995 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/*  999 */     return false;
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
/*      */   private void actionEmbarkPassenger(Creature performer, Item target, short action) {
/* 1012 */     if (vehicle.hasDestinationSet() && (vehicle.getDestinationServer()).PVPSERVER && ((Player)performer).isBlockingPvP()) {
/*      */       
/* 1014 */       performer.getCommunicator().sendAlertServerMessage("The " + Vehicle.getVehicleName(vehicle) + " is on a course for hostile territory, but you have elected to avoid hostility. You may change this preference in your profile.");
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1020 */     if (hasKeyForVehicle(performer, target) || mayEmbarkVehicle(performer, target)) {
/*      */       
/* 1022 */       if (performer.getDraggedItem() == null || performer.getDraggedItem().getWurmId() != target.getWurmId()) {
/*      */         
/* 1024 */         addedPassenger = false;
/* 1025 */         TileEvent.log(target.getTileX(), target.getTileY(), target.isOnSurface() ? 0 : -1, performer.getWurmId(), action);
/*      */ 
/*      */ 
/*      */         
/* 1029 */         boolean wallInWay = false;
/* 1030 */         if (action == 332 || action == 701) {
/*      */           
/* 1032 */           for (int x = 0; x < vehicle.seats.length; x++)
/*      */           {
/* 1034 */             wallInWay = addPassenger(performer, vehicle, target, x) ? true : wallInWay;
/* 1035 */             if (addedPassenger) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         
/*      */         }
/* 1041 */         else if (action == 702) {
/*      */           
/* 1043 */           wallInWay = addPassenger(performer, vehicle, target, 0) ? true : wallInWay;
/*      */         }
/* 1045 */         else if (action == 703) {
/*      */           
/* 1047 */           wallInWay = addPassenger(performer, vehicle, target, 1) ? true : wallInWay;
/*      */         } 
/*      */         
/* 1050 */         if (!addedPassenger) {
/*      */           
/* 1052 */           performer.getCommunicator().sendNormalServerMessage(
/* 1053 */               StringUtil.format("You may not %s the %s as a passenger right now.", new Object[] {
/*      */ 
/*      */                   
/* 1056 */                   vehicle.embarkString, Vehicle.getVehicleName(vehicle)
/*      */                 }));
/* 1058 */           if (wallInWay) {
/* 1059 */             performer.getCommunicator()
/* 1060 */               .sendNormalServerMessage("The wall is in the way. You can not reach a seat.");
/*      */           } else {
/* 1062 */             performer.getCommunicator().sendNormalServerMessage("The seats are all occupied.");
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/* 1067 */         performer.getCommunicator().sendNormalServerMessage(
/* 1068 */             StringUtil.format("You may not %s the %s since you are dragging it.", new Object[] {
/*      */ 
/*      */                 
/* 1071 */                 vehicle.embarkString, Vehicle.getVehicleName(vehicle)
/*      */               }));
/*      */       } 
/*      */     } else {
/*      */       
/* 1076 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to " + vehicle.embarkString + " the " + target
/* 1077 */           .getName() + ".");
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
/*      */   public static boolean addPassenger(Creature performer, Vehicle vehicle, Item target, int seatNum) {
/* 1089 */     boolean wallInWay = false;
/* 1090 */     if (seatNum >= 0 && seatNum < vehicle.seats.length && 
/* 1091 */       !vehicle.seats[seatNum].isOccupied() && (vehicle.seats[seatNum]).type == 1) {
/*      */       
/* 1093 */       float r = -(target.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1094 */       float s = (float)Math.sin(r);
/* 1095 */       float c = (float)Math.cos(r);
/* 1096 */       float xo = s * -(vehicle.seats[seatNum]).offx - c * -(vehicle.seats[seatNum]).offy;
/* 1097 */       float yo = c * -(vehicle.seats[seatNum]).offx + s * -(vehicle.seats[seatNum]).offy;
/* 1098 */       float newposx = target.getPosX() + xo;
/* 1099 */       float newposy = target.getPosY() + yo;
/*      */       
/* 1101 */       BlockingResult result = Blocking.getBlockerBetween(performer, performer.getPosX(), performer
/* 1102 */           .getPosY(), newposx, newposy, performer.getPositionZ(), target.getPosZ(), performer
/* 1103 */           .isOnSurface(), target.isOnSurface(), false, 4, -1L, performer
/* 1104 */           .getBridgeId(), target.getBridgeId(), false);
/*      */       
/* 1106 */       if (result == null) {
/*      */         
/* 1108 */         addedPassenger = true;
/* 1109 */         vehicle.seats[seatNum].occupy(vehicle, performer);
/* 1110 */         MountAction m = new MountAction(null, target, vehicle, seatNum, false, (vehicle.seats[seatNum]).offz);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1116 */         performer.setMountAction(m);
/* 1117 */         performer.setVehicle(target.getWurmId(), true, (byte)1);
/* 1118 */         if (performer.isPlayer() && ((Player)performer).getAlcohol() > 5.0F && 
/* 1119 */           target.isBoat()) {
/* 1120 */           performer.achievement(133);
/*      */         }
/* 1122 */         if (vehicle.hasDestinationSet()) {
/*      */           
/* 1124 */           ServerEntry entry = vehicle.getDestinationServer();
/* 1125 */           if (entry.PVPSERVER && (!entry.EPIC || Server.getInstance().isPS())) {
/*      */ 
/*      */             
/* 1128 */             byte pKingdom = (((Player)performer).getSaveFile().getChaosKingdom() == 0) ? 4 : ((Player)performer).getSaveFile().getChaosKingdom();
/* 1129 */             String toWhere = "The " + vehicle.getName() + " will be heading to " + entry.getName() + ", which is hostile territory";
/*      */             
/* 1131 */             if (pKingdom != performer.getKingdomId())
/* 1132 */               toWhere = toWhere + " and you will join the " + Kingdoms.getNameFor(pKingdom) + " kingdom until you return"; 
/* 1133 */             performer.getCommunicator().sendAlertServerMessage(toWhere + ".");
/* 1134 */             vehicle.alertPassengerOfEnemies(performer, entry, true);
/*      */           } else {
/*      */             
/* 1137 */             performer.getCommunicator().sendAlertServerMessage("The " + vehicle
/* 1138 */                 .getName() + " will be heading to " + entry.getName() + ".");
/*      */           } 
/*      */         } 
/* 1141 */         if (target.isBoat() && !PlonkData.ON_A_BOAT.hasSeenThis(performer)) {
/* 1142 */           PlonkData.ON_A_BOAT.trigger(performer);
/*      */         }
/*      */       } else {
/* 1145 */         wallInWay = true;
/*      */       } 
/*      */     } 
/* 1148 */     return wallInWay;
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
/*      */   private boolean actionRaiseAnchor(Creature performer, Item target, float counter, boolean done) {
/* 1160 */     if (performer.getVehicle() != -10L)
/*      */     {
/* 1162 */       if (performer.getVehicle() == target.getWurmId() && target.isBoat())
/*      */       {
/* 1164 */         if (target.getData() != -1L && performer.isVehicleCommander()) {
/*      */           
/*      */           try {
/*      */             
/* 1168 */             Item anchor = Items.getItem(target.getData());
/* 1169 */             done = false;
/* 1170 */             if (counter == 1.0F) {
/*      */               
/* 1172 */               performer.getCommunicator().sendNormalServerMessage("You start to raise the " + anchor
/* 1173 */                   .getName() + ".");
/* 1174 */               Server.getInstance().broadCastAction(performer
/* 1175 */                   .getName() + " starts to raise the " + anchor.getName() + ".", performer, 5);
/* 1176 */               performer.sendActionControl(Actions.actionEntrys[361]
/* 1177 */                   .getVerbString(), true, 100);
/*      */             } 
/* 1179 */             if (counter > 10.0F)
/*      */             {
/* 1181 */               done = true;
/* 1182 */               performer.getInventory().insertItem(anchor, true);
/* 1183 */               target.setData(-1L);
/* 1184 */               performer.getCommunicator().sendNormalServerMessage("You raise the " + anchor.getName() + ".");
/* 1185 */               Server.getInstance().broadCastAction(performer.getName() + " raises the " + anchor.getName() + ".", performer, 5);
/*      */               
/* 1187 */               Vehicle veh = Vehicles.getVehicle(target);
/*      */               
/*      */               try {
/* 1190 */                 Creature pilot = Server.getInstance().getCreature(veh.getPilotId());
/* 1191 */                 pilot.getMovementScheme().addWindImpact(veh.getWindImpact());
/* 1192 */                 pilot.getMovementScheme().setMooredMod(false);
/*      */               }
/* 1194 */               catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */               
/*      */               }
/* 1198 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/* 1204 */           catch (NoSuchItemException nsi) {
/*      */             
/* 1206 */             logger.log(Level.INFO, "No such anchor item.");
/*      */           } 
/*      */         }
/*      */       }
/*      */     }
/* 1211 */     return done;
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
/*      */   private boolean actionDetachKeepnet(Action act, Creature performer, Item target, float counter, boolean done) {
/* 1223 */     if (performer.getVehicle() != -10L)
/*      */     {
/* 1225 */       if (performer.getVehicle() == target.getWurmId() && target.isBoat())
/*      */       {
/* 1227 */         if (target.getExtra() != -1L) {
/*      */           
/*      */           try {
/*      */             
/* 1231 */             Item keepnet = Items.getItem(target.getExtra());
/* 1232 */             if (!keepnet.isEmpty(false)) {
/*      */               
/* 1234 */               performer.getCommunicator().sendNormalServerMessage("The " + keepnet
/* 1235 */                   .getName() + " must be empty to be detached.");
/* 1236 */               return true;
/*      */             } 
/* 1238 */             done = false;
/* 1239 */             int time = 20;
/* 1240 */             if (counter == 1.0F) {
/*      */               
/* 1242 */               performer.getCommunicator().sendNormalServerMessage("You start to detach the " + keepnet
/* 1243 */                   .getName() + ".");
/* 1244 */               Server.getInstance().broadCastAction(performer
/* 1245 */                   .getName() + " starts to detach a " + keepnet.getName() + ".", performer, 5);
/* 1246 */               performer.sendActionControl(Actions.actionEntrys[944]
/* 1247 */                   .getVerbString(), true, time);
/* 1248 */               act.setTimeLeft(time);
/*      */             } 
/* 1250 */             time = act.getTimeLeft();
/* 1251 */             if (counter * 10.0F > time)
/*      */             {
/*      */               
/*      */               try {
/* 1255 */                 for (Creature c : keepnet.getWatchers())
/*      */                 {
/* 1257 */                   keepnet.close(c);
/* 1258 */                   c.getCommunicator().sendUpdateSelectBar(target.getWurmId(), false);
/*      */                 }
/*      */               
/* 1261 */               } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1266 */               done = true;
/* 1267 */               performer.getInventory().insertItem(keepnet, true);
/* 1268 */               target.setExtra(-1L);
/* 1269 */               keepnet.setData(-1L);
/*      */               
/* 1271 */               VolaTile vt = Zones.getOrCreateTile(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 1272 */               vt.sendBoatDetachment(target.getWurmId(), keepnet.getTemplateId(), (byte)1);
/*      */               
/* 1274 */               performer.getCommunicator().sendNormalServerMessage("You detach the " + keepnet.getName() + ".");
/* 1275 */               Server.getInstance().broadCastAction(performer.getName() + " detachs the " + keepnet.getName() + ".", performer, 5);
/*      */             }
/*      */           
/*      */           }
/* 1279 */           catch (NoSuchItemException nsi) {
/*      */             
/* 1281 */             logger.log(Level.INFO, "No such keepnet item.");
/*      */           } 
/*      */         }
/*      */       }
/*      */     }
/* 1286 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void actionHitch(Creature performer, Item target) {
/* 1295 */     if (vehicle.hitched.length > 0) {
/*      */       
/* 1297 */       if (vehicle.hasHumanDragger()) {
/*      */         
/* 1299 */         performer.getCommunicator()
/* 1300 */           .sendNormalServerMessage("Someone is dragging the " + target.getName().toLowerCase() + " already.");
/*      */         
/*      */         return;
/*      */       } 
/* 1304 */       if (vehicle.mayAddDragger()) {
/*      */         
/* 1306 */         Creature[] folls = performer.getFollowers();
/* 1307 */         if (folls.length > 0) {
/*      */           
/* 1309 */           for (int x = 0; x < folls.length; x++) {
/*      */             
/* 1311 */             if (folls[x].isOnSurface() == target.isOnSurface()) {
/*      */               
/* 1313 */               if (!folls[x].isRidden()) {
/*      */                 
/* 1315 */                 if (folls[x].isDomestic() || folls[x].getStatus().getBattleRatingTypeModifier() <= 1.2F) {
/*      */                   
/* 1317 */                   if (isStrongEnoughToDrag(folls[x], target)) {
/*      */                     
/* 1319 */                     if (folls[x].canUseWithEquipment()) {
/*      */                       
/* 1321 */                       if (vehicle.addDragger(folls[x])) {
/*      */                         
/* 1323 */                         folls[x].setLeader(null);
/* 1324 */                         folls[x].setHitched(vehicle, false);
/* 1325 */                         folls[x].setVisible(false);
/* 1326 */                         performer.getCommunicator()
/* 1327 */                           .sendNormalServerMessage(
/* 1328 */                             StringUtil.format("You hitch the %s to the %s.", new Object[] {
/*      */                                 
/* 1330 */                                 StringUtil.toLowerCase(folls[x].getName()), 
/* 1331 */                                 StringUtil.toLowerCase(target.getName())
/*      */                               }));
/* 1333 */                         Seat driverseat = vehicle.getHitchSeatFor(folls[x].getWurmId());
/* 1334 */                         float _r = (-target.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1335 */                         float _s = (float)Math.sin(_r);
/* 1336 */                         float _c = (float)Math.cos(_r);
/* 1337 */                         float xo = _s * -driverseat.offx - _c * -driverseat.offy;
/* 1338 */                         float yo = _c * -driverseat.offx + _s * -driverseat.offy;
/* 1339 */                         float nPosX = target.getPosX() + xo;
/* 1340 */                         float nPosY = target.getPosY() + yo;
/* 1341 */                         float nPosZ = target.getPosZ() + driverseat.offz;
/*      */                         
/* 1343 */                         folls[x].getStatus().setPositionX(nPosX);
/* 1344 */                         folls[x].getStatus().setPositionY(nPosY);
/* 1345 */                         folls[x].setRotation(-target.getRotation());
/* 1346 */                         folls[x].getMovementScheme().setPosition(folls[x].getStatus().getPositionX(), folls[x]
/* 1347 */                             .getStatus().getPositionY(), nPosZ, folls[x]
/* 1348 */                             .getStatus().getRotation(), folls[x].getLayer());
/* 1349 */                         folls[x].getCurrentTile().sendAttachCreature(folls[x].getWurmId(), target
/* 1350 */                             .getWurmId(), (vehicle.getHitchSeatFor(folls[x].getWurmId())).offx, 
/* 1351 */                             (vehicle.getHitchSeatFor(folls[x].getWurmId())).offy, 
/* 1352 */                             (vehicle.getHitchSeatFor(folls[x].getWurmId())).offz, vehicle
/* 1353 */                             .getSeatNumberFor(vehicle.getHitchSeatFor(folls[x].getWurmId())));
/* 1354 */                         folls[x].setVisible(true);
/*      */                         
/*      */                         break;
/*      */                       } 
/*      */                       
/* 1359 */                       performer.getCommunicator().sendNormalServerMessage(
/* 1360 */                           StringUtil.format("The %s could not be hitched right now.", new Object[] {
/* 1361 */                               StringUtil.toLowerCase(folls[x].getName())
/*      */                             }));
/*      */                     }
/*      */                     else {
/*      */                       
/* 1366 */                       performer.getCommunicator().sendNormalServerMessage(
/* 1367 */                           StringUtil.format("The %s looks confused by its equipment and refuses to move.", new Object[] {
/* 1368 */                               StringUtil.toLowerCase(folls[x].getName())
/*      */                             }));
/*      */                     } 
/*      */                   } else {
/*      */                     
/* 1373 */                     performer.getCommunicator().sendNormalServerMessage(
/* 1374 */                         StringUtil.format("The %s is too weak.", new Object[] {
/* 1375 */                             StringUtil.toLowerCase(folls[x].getName())
/*      */                           }));
/*      */                   } 
/*      */                 } else {
/*      */                   
/* 1380 */                   performer.getCommunicator().sendNormalServerMessage(
/* 1381 */                       StringUtil.format("The %s is too unruly to be hitched.", new Object[] {
/* 1382 */                           StringUtil.toLowerCase(folls[x].getName())
/*      */                         }));
/*      */                 } 
/*      */               } else {
/*      */                 
/* 1387 */                 performer.getCommunicator().sendNormalServerMessage(
/* 1388 */                     StringUtil.format("The %s is ridden and may not drag now.", new Object[] {
/* 1389 */                         StringUtil.toLowerCase(folls[x].getName())
/*      */                       }));
/*      */               } 
/*      */             } else {
/*      */               
/* 1394 */               performer.getCommunicator().sendNormalServerMessage(
/* 1395 */                   StringUtil.format("The %s is not close enough to the %s.", new Object[] {
/*      */                       
/* 1397 */                       StringUtil.toLowerCase(folls[x].getName()), 
/* 1398 */                       StringUtil.toLowerCase(target.getName())
/*      */                     }));
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/* 1404 */           performer.getCommunicator().sendNormalServerMessage(
/* 1405 */               StringUtil.format("You have no creature to hitch to the %s.", new Object[] {
/*      */                   
/* 1407 */                   StringUtil.toLowerCase(target.getName())
/*      */                 }));
/*      */         } 
/*      */       } else {
/*      */         
/* 1412 */         performer.getCommunicator().sendNormalServerMessage(
/* 1413 */             StringUtil.format("The %s has no spaces left to hitch to.", new Object[] {
/*      */                 
/* 1415 */                 StringUtil.toLowerCase(target.getName())
/*      */               }));
/*      */       } 
/*      */     } else {
/*      */       
/* 1420 */       performer.getCommunicator().sendNormalServerMessage(
/* 1421 */           StringUtil.format("The %s has no spaces to hitch to.", new Object[] {
/*      */               
/* 1423 */               StringUtil.toLowerCase(target.getName())
/*      */             }));
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isStrongEnoughToDrag(Creature creature, Item aVehicle) {
/* 1429 */     return (creature.getStrengthSkill() > (aVehicle.getTemplate().getWeightGrams() / 10000.0F));
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
/*      */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 1441 */     boolean done = true;
/* 1442 */     if (action == 331 || action == 332 || action == 333 || action == 708 || action == 361 || action == 383 || action == 378 || action == 687 || action == 665 || action == 669 || action == 668 || action == 691 || action == 944 || action == 940)
/*      */     
/*      */     { 
/*      */ 
/*      */       
/* 1447 */       done = action(act, performer, target, action, counter); }
/* 1448 */     else { if (action == 671 || action == 672) {
/*      */         
/* 1450 */         if (source.getTemplateId() == 319 || source.getTemplateId() == 1029)
/* 1451 */           return CargoTransportationMethods.haul(performer, target, counter, action, act); 
/* 1452 */         return true;
/*      */       } 
/* 1454 */       if (action == 360)
/*      */       
/* 1456 */       { if (performer.getVehicle() == target.getWurmId() && target.isBoat())
/*      */         {
/* 1458 */           if (target.getData() == -1L)
/*      */           {
/* 1460 */             if (source.isAnchor()) {
/*      */               
/* 1462 */               done = false;
/* 1463 */               if (counter == 1.0F) {
/*      */                 
/* 1465 */                 performer.getCommunicator().sendNormalServerMessage("You start to moor the " + target
/* 1466 */                     .getName() + ".");
/* 1467 */                 Server.getInstance().broadCastAction(performer
/* 1468 */                     .getName() + " starts to moor the " + target.getName() + ".", performer, 5);
/* 1469 */                 performer.sendActionControl(Actions.actionEntrys[360].getVerbString(), true, 10);
/*      */               } 
/*      */               
/* 1472 */               if (counter > 1.0F) {
/*      */                 
/* 1474 */                 done = true;
/* 1475 */                 source.putInVoid();
/* 1476 */                 target.setData(source.getWurmId());
/* 1477 */                 Vehicle veh = Vehicles.getVehicle(target);
/*      */                 
/*      */                 try {
/* 1480 */                   Creature pilot = Server.getInstance().getCreature(veh.getPilotId());
/* 1481 */                   pilot.getMovementScheme().addWindImpact((byte)0);
/* 1482 */                   pilot.getMovementScheme().setMooredMod(true);
/*      */                 }
/* 1484 */                 catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */                 
/*      */                 }
/* 1488 */                 catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */                 
/* 1492 */                 target.savePosition();
/* 1493 */                 performer.getCommunicator().sendNormalServerMessage("You moor the " + target.getName() + ".");
/* 1494 */                 Server.getInstance().broadCastAction(performer.getName() + " moors the " + target.getName() + ".", performer, 5);
/*      */                 
/* 1496 */                 if (Item.getMaterialAnchorBonus(source.getMaterial()) < 1.0F) {
/* 1497 */                   performer.getCommunicator().sendNormalServerMessage("You're unsure if the " + source.getName() + " is of a heavy enough material to completely stop the " + target
/* 1498 */                       .getName() + " from drifting.");
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } }
/* 1504 */       else if (action == 943)
/*      */       
/* 1506 */       { if (performer.getVehicle() == target.getWurmId() && (target.getTemplateId() == 490 || target.getTemplateId() == 491))
/*      */         {
/* 1508 */           if (target.getExtra() == -1L)
/*      */           {
/* 1510 */             if (source.getTemplateId() == 1342)
/*      */             {
/* 1512 */               done = false;
/* 1513 */               int time = 20;
/* 1514 */               if (counter == 1.0F) {
/*      */                 
/* 1516 */                 performer.getCommunicator().sendNormalServerMessage("You start to attach the " + source
/* 1517 */                     .getName() + " to the " + target.getName() + ".");
/* 1518 */                 Server.getInstance().broadCastAction(performer
/* 1519 */                     .getName() + " starts to attach a " + source.getName() + ".", performer, 5);
/* 1520 */                 performer.sendActionControl(Actions.actionEntrys[943].getVerbString(), true, time);
/*      */                 
/* 1522 */                 act.setTimeLeft(time);
/*      */               } 
/* 1524 */               time = act.getTimeLeft();
/* 1525 */               if (counter * 10.0F > time)
/*      */               {
/* 1527 */                 done = true;
/* 1528 */                 source.putInVoid();
/* 1529 */                 target.setExtra(source.getWurmId());
/* 1530 */                 source.setData(target.getWurmId());
/* 1531 */                 ((DbItem)target).maybeUpdateKeepnetPos();
/*      */                 
/* 1533 */                 VolaTile vt = Zones.getOrCreateTile(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 1534 */                 vt.sendBoatAttachment(target.getWurmId(), source.getTemplateId(), source.getMaterial(), (byte)1, source.getAuxData());
/*      */                 
/* 1536 */                 performer.getCommunicator().sendNormalServerMessage("You attach the " + source.getName() + " to the " + target
/* 1537 */                     .getName() + ".");
/* 1538 */                 Server.getInstance().broadCastAction(performer.getName() + " attaches a " + source.getName() + " to the " + target
/* 1539 */                     .getName() + ".", performer, 5);
/*      */               }
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         } }
/* 1546 */       else if (action == 362)
/*      */       
/* 1548 */       { done = true;
/* 1549 */         if (performer.getVehicle() == target.getWurmId() && target.isBoat())
/*      */         {
/* 1551 */           if (source.isDredgingTool()) {
/*      */             
/* 1553 */             if (performer.isOnSurface()) {
/*      */ 
/*      */               
/*      */               try {
/* 1557 */                 Item boat = Items.getItem(performer.getVehicle());
/* 1558 */                 if (boat.isOnSurface() && boat.getPosZ() <= 0.0F) {
/*      */                   
/* 1560 */                   int tile = Server.surfaceMesh.getTile(boat.getTileX(), boat.getTileY());
/* 1561 */                   if (!Terraforming.isNonDiggableTile(Tiles.decodeType(tile))) {
/*      */                     
/* 1563 */                     done = Terraforming.dig(performer, source, boat.getTileX(), boat.getTileY(), tile, counter, false, 
/* 1564 */                         performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh);
/*      */                   } else {
/*      */                     
/* 1567 */                     performer.getCommunicator().sendNormalServerMessage("You may not dredge here.");
/*      */                   } 
/*      */                 } else {
/* 1570 */                   performer.getCommunicator().sendNormalServerMessage("You may not dredge here.");
/*      */                 } 
/* 1572 */               } catch (NoSuchItemException noSuchItemException) {}
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/* 1578 */               performer.getCommunicator().sendNormalServerMessage("You may not dredge here.");
/*      */             } 
/*      */           } else {
/* 1581 */             performer.getCommunicator().sendNormalServerMessage("You may not use that.");
/*      */           } 
/*      */         } }
/* 1584 */       else if (action == 160)
/*      */       
/*      */       { 
/*      */         try {
/* 1588 */           Item boat = Items.getItem(performer.getVehicle());
/* 1589 */           int tile = Server.surfaceMesh.getTile(boat.getTileX(), boat.getTileY());
/* 1590 */           if (!performer.isOnSurface()) {
/* 1591 */             tile = Server.caveMesh.getTile(boat.getTileX(), boat.getTileY());
/*      */           }
/*      */           
/* 1594 */           if (source.getTemplateId() == 1344 || source.getTemplateId() == 1346) {
/* 1595 */             done = MethodsFishing.fish(performer, source, boat.getTileX(), boat.getTileY(), tile, counter, act);
/*      */           } else {
/* 1597 */             done = true;
/*      */           } 
/* 1599 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */         
/*      */          }
/*      */       
/* 1603 */       else if (action == 285)
/*      */       
/* 1605 */       { done = true;
/*      */ 
/*      */ 
/*      */         
/* 1609 */         if (source.getTemplateId() == 1344 || source.getTemplateId() == 1346) {
/*      */           
/*      */           try {
/*      */             
/* 1613 */             Item boat = Items.getItem(performer.getVehicle());
/* 1614 */             done = MethodsFishing.showFishTable(performer, source, boat.getTileX(), boat.getTileY(), counter, act);
/*      */           }
/* 1616 */           catch (NoSuchItemException noSuchItemException) {}
/*      */         
/*      */         }
/*      */          }
/*      */       
/*      */       else
/*      */       
/* 1623 */       { done = super.action(act, performer, source, target, action, counter); }  }
/* 1624 */      return done;
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
/*      */   public static boolean canBeDriverOfVehicle(Creature aPerformer, Vehicle aVehicle) {
/* 1647 */     boolean toReturn = false;
/* 1648 */     if (aVehicle != null && aPerformer != null) {
/*      */       
/* 1650 */       if (aVehicle.isUnmountable())
/* 1651 */         return false; 
/* 1652 */       Skill checkSkill = null;
/* 1653 */       if (aVehicle.creature) {
/*      */ 
/*      */         
/*      */         try {
/* 1657 */           checkSkill = aPerformer.getSkills().getSkill(104);
/*      */         }
/* 1659 */         catch (NoSuchSkillException nss) {
/*      */           
/* 1661 */           logger.log(Level.WARNING, aPerformer.getName() + " no body control.");
/* 1662 */           checkSkill = aPerformer.getSkills().learn(104, 1.0F);
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1669 */           checkSkill = aPerformer.getSkills().getSkill(100);
/*      */         }
/* 1671 */         catch (NoSuchSkillException nss) {
/*      */           
/* 1673 */           logger.log(Level.WARNING, aPerformer.getName() + " no mind logic.");
/* 1674 */           checkSkill = aPerformer.getSkills().learn(100, 1.0F);
/*      */         } 
/*      */       } 
/* 1677 */       if (checkSkill.getRealKnowledge() > aVehicle.skillNeeded)
/*      */       {
/* 1679 */         toReturn = true;
/*      */       }
/*      */     } 
/* 1682 */     return toReturn;
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
/*      */   public static boolean mayDriveVehicle(Creature aPerformer, Item aVehicle, @Nullable Action act) {
/* 1698 */     if (aPerformer == null || aVehicle == null) {
/*      */       
/* 1700 */       logger.warning("null arguments - Performer: " + aPerformer + ", Vehicle: " + aVehicle);
/* 1701 */       return false;
/*      */     } 
/* 1703 */     if (aVehicle.isTent())
/* 1704 */       return false; 
/* 1705 */     if (aPerformer.getWurmId() == aVehicle.lastOwner || aVehicle.lastOwner == -10L)
/*      */     {
/*      */       
/* 1708 */       return true;
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
/* 1740 */     if (aVehicle.mayCommand(aPerformer))
/* 1741 */       return true; 
/* 1742 */     if (aVehicle.isInPvPZone() && MethodsItems.checkIfStealing(aVehicle, aPerformer, act))
/*      */     {
/* 1744 */       return true;
/*      */     }
/* 1746 */     return false;
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
/*      */   public static boolean mayEmbarkVehicle(Creature aPerformer, Item aVehicle) {
/* 1764 */     if (aPerformer == null || aVehicle == null) {
/*      */       
/* 1766 */       logger.warning("null arguments - Performer: " + aPerformer + ", Vehicle: " + aVehicle);
/* 1767 */       return false;
/*      */     } 
/* 1769 */     if (aVehicle.isTent())
/* 1770 */       return false; 
/* 1771 */     if (aVehicle.isChair()) {
/* 1772 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 1776 */     return aVehicle.mayPassenger(aPerformer);
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
/*      */   public static final boolean isFriendAndMayMount(Creature aPerformer, Item aVehicle) {
/* 1794 */     if (aVehicle.isTent()) {
/* 1795 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1799 */     if (ItemSettings.mayCommand((PermissionsPlayerList.ISettings)aVehicle, aPerformer))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1811 */       return true;
/*      */     }
/* 1813 */     return false;
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
/*      */   public static boolean mayDriveVehicle(Creature aPerformer, Creature aVehicle) {
/* 1944 */     if (aPerformer == null || aVehicle == null) {
/*      */       
/* 1946 */       logger.warning("null arguments - Performer: " + aPerformer + ", Vehicle: " + aVehicle);
/* 1947 */       return false;
/*      */     } 
/* 1949 */     if (aPerformer.getWurmId() == aVehicle.dominator || aVehicle.getLeader() == aPerformer) {
/*      */       
/* 1951 */       if (!Servers.isThisAPvpServer() && aVehicle.isBranded())
/* 1952 */         return aVehicle.mayCommand(aPerformer); 
/* 1953 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1958 */     return false;
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
/*      */   public static boolean mayEmbarkVehicle(Creature aPerformer, Creature aVehicle) {
/* 1976 */     if (aPerformer == null || aVehicle == null) {
/*      */       
/* 1978 */       logger.warning("null arguments - Performer: " + aPerformer + ", Vehicle: " + aVehicle);
/* 1979 */       return false;
/*      */     } 
/* 1981 */     if (aVehicle.dominator != -10L || aVehicle.getLeader() != null) {
/*      */       
/* 1983 */       if (!Servers.isThisAPvpServer() && aVehicle.isBranded()) {
/* 1984 */         return aVehicle.mayPassenger(aPerformer);
/*      */       }
/* 1986 */       return true;
/*      */     } 
/* 1988 */     return false;
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
/*      */   static boolean hasPermission(Creature performer, Item target) {
/* 2002 */     if (target.isChair()) {
/*      */ 
/*      */       
/* 2005 */       if (target.isOwnedByWagoner())
/* 2006 */         return false; 
/* 2007 */       return true;
/*      */     } 
/* 2009 */     if (!target.isLocked()) {
/*      */       
/* 2011 */       VolaTile vt = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 2012 */       Village vill = (vt == null) ? null : vt.getVillage();
/* 2013 */       return (vill == null || vill.isActionAllowed((short)6, performer));
/*      */     } 
/* 2015 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\VehicleBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */