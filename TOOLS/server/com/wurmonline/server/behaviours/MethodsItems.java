/*       */ package com.wurmonline.server.behaviours;
/*       */ 
/*       */ import com.wurmonline.mesh.FoliageAge;
/*       */ import com.wurmonline.mesh.MeshIO;
/*       */ import com.wurmonline.mesh.Tiles;
/*       */ import com.wurmonline.mesh.TreeData;
/*       */ import com.wurmonline.server.Constants;
/*       */ import com.wurmonline.server.FailedException;
/*       */ import com.wurmonline.server.Features;
/*       */ import com.wurmonline.server.GeneralUtilities;
/*       */ import com.wurmonline.server.Items;
/*       */ import com.wurmonline.server.LoginServerWebConnection;
/*       */ import com.wurmonline.server.MiscConstants;
/*       */ import com.wurmonline.server.NoSuchItemException;
/*       */ import com.wurmonline.server.NoSuchPlayerException;
/*       */ import com.wurmonline.server.Players;
/*       */ import com.wurmonline.server.Point;
/*       */ import com.wurmonline.server.Point4f;
/*       */ import com.wurmonline.server.Server;
/*       */ import com.wurmonline.server.Servers;
/*       */ import com.wurmonline.server.TimeConstants;
/*       */ import com.wurmonline.server.WurmCalendar;
/*       */ import com.wurmonline.server.WurmId;
/*       */ import com.wurmonline.server.combat.CombatEngine;
/*       */ import com.wurmonline.server.combat.Weapon;
/*       */ import com.wurmonline.server.creatures.Communicator;
/*       */ import com.wurmonline.server.creatures.Creature;
/*       */ import com.wurmonline.server.creatures.CreatureTemplate;
/*       */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*       */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*       */ import com.wurmonline.server.economy.Change;
/*       */ import com.wurmonline.server.economy.Economy;
/*       */ import com.wurmonline.server.effects.Effect;
/*       */ import com.wurmonline.server.effects.EffectFactory;
/*       */ import com.wurmonline.server.epic.Hota;
/*       */ import com.wurmonline.server.highways.MethodsHighways;
/*       */ import com.wurmonline.server.items.CreationEntry;
/*       */ import com.wurmonline.server.items.CreationMatrix;
/*       */ import com.wurmonline.server.items.Item;
/*       */ import com.wurmonline.server.items.ItemFactory;
/*       */ import com.wurmonline.server.items.ItemMealData;
/*       */ import com.wurmonline.server.items.ItemSettings;
/*       */ import com.wurmonline.server.items.ItemSpellEffects;
/*       */ import com.wurmonline.server.items.ItemTemplate;
/*       */ import com.wurmonline.server.items.ItemTemplateFactory;
/*       */ import com.wurmonline.server.items.ItemTypes;
/*       */ import com.wurmonline.server.items.Materials;
/*       */ import com.wurmonline.server.items.NoSuchTemplateException;
/*       */ import com.wurmonline.server.items.NotOwnedException;
/*       */ import com.wurmonline.server.items.Puppet;
/*       */ import com.wurmonline.server.items.RuneUtilities;
/*       */ import com.wurmonline.server.items.WurmColor;
/*       */ import com.wurmonline.server.kingdom.GuardTower;
/*       */ import com.wurmonline.server.kingdom.Kingdom;
/*       */ import com.wurmonline.server.kingdom.Kingdoms;
/*       */ import com.wurmonline.server.players.Abilities;
/*       */ import com.wurmonline.server.players.Friend;
/*       */ import com.wurmonline.server.players.ItemBonus;
/*       */ import com.wurmonline.server.players.PermissionsHistories;
/*       */ import com.wurmonline.server.players.Player;
/*       */ import com.wurmonline.server.players.PlayerInfo;
/*       */ import com.wurmonline.server.players.PlayerInfoFactory;
/*       */ import com.wurmonline.server.players.Titles;
/*       */ import com.wurmonline.server.questions.QuestionTypes;
/*       */ import com.wurmonline.server.questions.SleepQuestion;
/*       */ import com.wurmonline.server.skills.AffinitiesTimed;
/*       */ import com.wurmonline.server.skills.NoSuchSkillException;
/*       */ import com.wurmonline.server.skills.Skill;
/*       */ import com.wurmonline.server.skills.Skills;
/*       */ import com.wurmonline.server.sounds.SoundPlayer;
/*       */ import com.wurmonline.server.spells.Spell;
/*       */ import com.wurmonline.server.spells.SpellEffect;
/*       */ import com.wurmonline.server.spells.Spells;
/*       */ import com.wurmonline.server.structures.Blocker;
/*       */ import com.wurmonline.server.structures.Blocking;
/*       */ import com.wurmonline.server.structures.BlockingResult;
/*       */ import com.wurmonline.server.structures.BridgePart;
/*       */ import com.wurmonline.server.structures.Fence;
/*       */ import com.wurmonline.server.structures.NoSuchStructureException;
/*       */ import com.wurmonline.server.structures.Structure;
/*       */ import com.wurmonline.server.structures.TempFence;
/*       */ import com.wurmonline.server.tutorial.MissionTriggers;
/*       */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*       */ import com.wurmonline.server.utils.StringUtil;
/*       */ import com.wurmonline.server.utils.logging.TileEvent;
/*       */ import com.wurmonline.server.villages.Reputation;
/*       */ import com.wurmonline.server.villages.Village;
/*       */ import com.wurmonline.server.villages.VillageStatus;
/*       */ import com.wurmonline.server.villages.Villages;
/*       */ import com.wurmonline.server.zones.NoSuchZoneException;
/*       */ import com.wurmonline.server.zones.VirtualZone;
/*       */ import com.wurmonline.server.zones.VolaTile;
/*       */ import com.wurmonline.server.zones.WaterType;
/*       */ import com.wurmonline.server.zones.Zone;
/*       */ import com.wurmonline.server.zones.Zones;
/*       */ import com.wurmonline.shared.constants.CounterTypes;
/*       */ import com.wurmonline.shared.constants.ItemMaterials;
/*       */ import com.wurmonline.shared.constants.SoundNames;
/*       */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*       */ import com.wurmonline.shared.exceptions.WurmServerException;
/*       */ import com.wurmonline.shared.util.TerrainUtilities;
/*       */ import java.io.IOException;
/*       */ import java.util.ArrayList;
/*       */ import java.util.HashMap;
/*       */ import java.util.Iterator;
/*       */ import java.util.LinkedList;
/*       */ import java.util.List;
/*       */ import java.util.ListIterator;
/*       */ import java.util.Map;
/*       */ import java.util.Set;
/*       */ import java.util.logging.Level;
/*       */ import java.util.logging.Logger;
/*       */ import javax.annotation.Nonnull;
/*       */ import javax.annotation.Nullable;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ public final class MethodsItems
/*       */   implements MiscConstants, QuestionTypes, ItemTypes, CounterTypes, ItemMaterials, SoundNames, VillageStatus, TimeConstants
/*       */ {
/*   144 */   private static final float spawnDamageMod = Servers.localServer.isChallengeOrEpicServer() ? 5.0E-4F : 0.005F;
/*       */   
/*       */   public static final String cvsversion = "$Id: MethodsItems.java,v 1.84 2007-04-19 23:05:18 root Exp $";
/*   147 */   private static final Logger logger = Logger.getLogger(MethodsItems.class.getName());
/*       */ 
/*       */   
/*       */   static final byte PICKTYPE_NONE = 0;
/*       */ 
/*       */   
/*       */   static final byte PICKTYPE_DOOR = 1;
/*       */ 
/*       */   
/*       */   static final byte PICKTYPE_LARGEVEHICLE = 2;
/*       */ 
/*       */   
/*       */   static final byte PICKTYPE_GATE = 3;
/*       */   
/*       */   private static final int MAX_STRAIGHT_SLOPE = 40;
/*       */   
/*       */   private static final int MAX_DIAGONAL_SLOPE = 56;
/*       */ 
/*       */   
/*       */   static final TakeResultEnum take(Action act, Creature performer, Item target) {
/*   167 */     if (target.isBusy() && act.getNumber() != 925) {
/*       */       
/*   169 */       TakeResultEnum.TARGET_IN_USE.setIndexText(performer.getWurmId(), new String[] { target.getName() });
/*   170 */       return TakeResultEnum.TARGET_IN_USE;
/*       */     } 
/*       */     
/*       */     try {
/*   174 */       long ownId = target.getOwner();
/*   175 */       if (ownId != -10L && act.getNumber() != 582)
/*   176 */         return TakeResultEnum.TARGET_HAS_NO_OWNER; 
/*   177 */       if (ownId == performer.getWurmId()) {
/*   178 */         return TakeResultEnum.PERFORMER_IS_OWNER;
/*       */       }
/*   180 */     } catch (Exception ex) {
/*       */       
/*   182 */       if (target.isCoin() || performer.getPossessions().getInventory().mayCreatureInsertItem() || performer
/*   183 */         .getPower() > 0) {
/*       */         
/*   185 */         if (target.mailed) {
/*   186 */           return TakeResultEnum.TARGET_IS_UNREACHABLE;
/*       */         }
/*   188 */         if (target.isLiquid()) {
/*   189 */           return TakeResultEnum.TARGET_IS_LIQUID;
/*       */         }
/*   191 */         if ((target.isBulkContainer() || target.isTent()) && !target.isEmpty(true))
/*   192 */           return TakeResultEnum.TARGET_FILLED_BULK_CONTAINER; 
/*   193 */         if (target.isBulkItem())
/*       */         {
/*   195 */           return TakeResultEnum.TARGET_BULK_ITEM;
/*       */         }
/*   197 */         if (target.isTent()) {
/*       */           
/*   199 */           Vehicle vehicle = Vehicles.getVehicle(target);
/*   200 */           if (vehicle != null)
/*       */           {
/*   202 */             if (vehicle.getDraggers() != null && vehicle.getDraggers().size() > 0) {
/*   203 */               return TakeResultEnum.HITCHED;
/*       */             }
/*       */           }
/*       */         } 
/*       */ 
/*       */ 
/*       */         
/*   210 */         float weight = target.getFullWeight();
/*   211 */         if (weight == 0.0F || performer.canCarry(target.getFullWeight())) {
/*       */ 
/*       */           
/*       */           try {
/*   215 */             BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*   216 */             if (result == null)
/*       */             {
/*   218 */               if (!target.isNoTake())
/*       */               {
/*       */                 
/*   221 */                 boolean sameVehicle = false;
/*   222 */                 Item top = target.getTopParentOrNull();
/*   223 */                 if (top != null && top.isVehicle())
/*       */                 {
/*   225 */                   if (top.getWurmId() == performer.getVehicle())
/*   226 */                     sameVehicle = true; 
/*       */                 }
/*   228 */                 if (sameVehicle || performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 5.0F)) {
/*       */                   
/*   230 */                   Zone tzone = Zones.getZone((int)target.getPosX() >> 2, (int)target.getPosY() >> 2, target
/*   231 */                       .isOnSurface());
/*   232 */                   VolaTile tile = tzone.getTileOrNull((int)target.getPosX() >> 2, 
/*   233 */                       (int)target.getPosY() >> 2);
/*   234 */                   if (tile != null) {
/*       */                     
/*   236 */                     Structure struct = tile.getStructure();
/*       */                     
/*   238 */                     VolaTile tile2 = performer.getCurrentTile();
/*   239 */                     if (tile2 != null) {
/*       */                       
/*   241 */                       if (tile.getStructure() != struct)
/*       */                       {
/*   243 */                         if (struct == null || !struct.isTypeBridge())
/*       */                         {
/*   245 */                           performer.getCommunicator().sendNormalServerMessage("You can't reach the " + target
/*   246 */                               .getName() + " through the wall.");
/*   247 */                           return TakeResultEnum.TARGET_BLOCKED;
/*       */                         
/*       */                         }
/*       */                       
/*       */                       }
/*       */                     }
/*   253 */                     else if (struct != null) {
/*       */                       
/*   255 */                       if (!struct.isTypeBridge()) {
/*       */                         
/*   257 */                         performer.getCommunicator().sendNormalServerMessage("You can't reach the " + target
/*   258 */                             .getName() + " through the wall.");
/*   259 */                         return TakeResultEnum.TARGET_BLOCKED;
/*       */                       } 
/*       */                     } 
/*       */                   } 
/*       */                   
/*   264 */                   long toppar = target.getTopParent();
/*       */                   
/*   266 */                   if (!isLootableBy(performer, target))
/*   267 */                     return TakeResultEnum.MAY_NOT_LOOT_THAT_ITEM; 
/*   268 */                   boolean mayUseVehicle = true;
/*       */                   
/*       */                   try {
/*   271 */                     Item topparent = Items.getItem(toppar);
/*       */                     
/*   273 */                     if (topparent.isDraggable())
/*       */                     {
/*   275 */                       mayUseVehicle = mayUseInventoryOfVehicle(performer, topparent);
/*       */                     }
/*   277 */                     if (!mayUseVehicle && target.lastOwner != performer.getWurmId())
/*       */                     {
/*   279 */                       if (((topparent.isVehicle() && topparent.getLockId() != -10L) || 
/*   280 */                         Items.isItemDragged(topparent)) && 
/*   281 */                         performer.getDraggedItem() != topparent)
/*       */                       {
/*   283 */                         TakeResultEnum.VEHICLE_IS_WATCHED.setIndexText(performer.getWurmId(), new String[] { topparent
/*   284 */                               .getName() });
/*   285 */                         return TakeResultEnum.VEHICLE_IS_WATCHED;
/*       */                       }
/*       */                     
/*       */                     }
/*   289 */                   } catch (NoSuchItemException noSuchItemException) {}
/*       */ 
/*       */                   
/*   292 */                   if (checkIfStealing(target, performer, act)) {
/*       */                     
/*   294 */                     if (act.getNumber() != 100) {
/*       */                       
/*   296 */                       TakeResultEnum.NEEDS_TO_STEAL.setIndexText(performer.getWurmId(), new String[] { target.getName() });
/*   297 */                       return TakeResultEnum.NEEDS_TO_STEAL;
/*       */                     } 
/*       */ 
/*       */                     
/*   301 */                     if (Action.checkLegalMode(performer))
/*   302 */                       return TakeResultEnum.IN_LEGAL_MODE; 
/*   303 */                     if (!performer.maySteal())
/*       */                     {
/*   305 */                       return TakeResultEnum.MAY_NOT_STEAL;
/*       */                     }
/*   307 */                     if (target.getItems().size() > 0) {
/*       */                       
/*   309 */                       TakeResultEnum.NEED_TO_BE_EMPTY_BEFORE_THEFT.setIndexText(performer
/*   310 */                           .getWurmId(), new String[] { target
/*   311 */                             .getName() });
/*   312 */                       return TakeResultEnum.NEED_TO_BE_EMPTY_BEFORE_THEFT;
/*       */                     } 
/*   314 */                     Skill stealing = null;
/*   315 */                     boolean dryRun = false;
/*   316 */                     Village v = Zones.getVillage(target.getTileX(), target.getTileY(), true);
/*   317 */                     if (v != null) {
/*       */                       
/*   319 */                       Reputation rep = v.getReputationObject(performer.getWurmId());
/*   320 */                       if (rep != null)
/*       */                       {
/*   322 */                         if (rep.getValue() >= 0 && rep.isPermanent())
/*   323 */                           dryRun = true; 
/*       */                       }
/*       */                     } 
/*   326 */                     if (setTheftEffects(performer, act, target)) {
/*       */                       
/*   328 */                       stealing = performer.getStealSkill();
/*   329 */                       stealing.skillCheck(target.getQualityLevel(), 0.0D, dryRun, 10.0F);
/*   330 */                       return TakeResultEnum.PREVENTED_THEFT;
/*       */                     } 
/*       */ 
/*       */                     
/*   334 */                     stealing = performer.getStealSkill();
/*   335 */                     stealing.skillCheck(target.getQualityLevel(), 0.0D, dryRun, 10.0F);
/*       */                   } 
/*       */ 
/*       */                   
/*   339 */                   if (target.getParentId() == -10L || 
/*   340 */                     WurmId.getType(target.getParentId()) == 6) {
/*       */                     
/*   342 */                     long targid = target.getWurmId();
/*       */                     
/*   344 */                     if (toppar == targid) {
/*       */                       
/*       */                       try {
/*       */                         
/*   348 */                         Creature[] watchers = target.getWatchers();
/*   349 */                         for (Creature lWatcher : watchers) {
/*   350 */                           lWatcher.getCommunicator().sendCloseInventoryWindow(targid);
/*       */                         }
/*   352 */                       } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */                     }
/*       */ 
/*       */ 
/*       */                     
/*   357 */                     if (WurmId.getType(target.getParentId()) == 6) {
/*       */                       
/*   359 */                       Item parent = Items.getItem(target.getParentId());
/*   360 */                       parent.dropItem(target.getWurmId(), true);
/*       */                     } 
/*   362 */                     int x = (int)target.getPosX() >> 2;
/*   363 */                     int y = (int)target.getPosY() >> 2;
/*   364 */                     Zone zone = Zones.getZone(x, y, target.isOnSurface());
/*   365 */                     zone.removeItem(target);
/*   366 */                     if (performer.getDraggedItem() == target) {
/*   367 */                       stopDragging(performer, target);
/*       */                     }
/*       */                   } else {
/*       */                     
/*   371 */                     Item parent = Items.getItem(target.getParentId());
/*   372 */                     parent.dropItem(target.getWurmId(), true);
/*       */                   } 
/*       */                   
/*   375 */                   if (target.isPlanted() && (target
/*   376 */                     .isSign() || target.isStreetLamp() || target.isFlag() || target
/*   377 */                     .isBulkContainer() || target.getTemplateId() == 742)) {
/*       */ 
/*       */                     
/*   380 */                     target.setIsPlanted(false);
/*   381 */                     if (target.isAbility()) {
/*       */                       
/*   383 */                       target.hatching = false;
/*   384 */                       target.setRarity((byte)0);
/*       */                     } 
/*       */                   } 
/*   387 */                   if (target == performer.getDraggedItem())
/*   388 */                     performer.setDraggedItem(null); 
/*   389 */                   if (target.isMushroom())
/*       */                   {
/*   391 */                     if (target.getLastOwnerId() <= 0L)
/*       */                     {
/*   393 */                       performer.achievement(139);
/*       */                     }
/*       */                   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                   
/*   440 */                   if (target.getTemplate().isContainerWithSubItems()) {
/*       */                     
/*   442 */                     ArrayList<Item> toMove = new ArrayList<>();
/*   443 */                     for (Item i : target.getItems()) {
/*   444 */                       if (i.isPlacedOnParent())
/*   445 */                         toMove.add(i); 
/*       */                     } 
/*   447 */                     for (Item i : toMove) {
/*       */                       
/*   449 */                       target.dropItem(i.getWurmId(), true);
/*   450 */                       Zones.getZone(i.getTileX(), i.getTileY(), target.isOnSurface()).addItem(i);
/*   451 */                       performer.getCommunicator().sendNormalServerMessage("The " + i.getName() + " drops to the ground.");
/*       */                     } 
/*       */                   } 
/*   454 */                   performer.getPossessions().getInventory().insertItem(target);
/*       */                   
/*   456 */                   target.setOnBridge(-10L);
/*   457 */                   target.setLastMaintained(WurmCalendar.currentTime);
/*       */                   
/*   459 */                   return TakeResultEnum.SUCCESS;
/*       */                 } 
/*       */ 
/*       */                 
/*   463 */                 TakeResultEnum.TOO_FAR_AWAY.setIndexText(performer.getWurmId(), new String[] { target.getName() });
/*   464 */                 return TakeResultEnum.TOO_FAR_AWAY;
/*       */               }
/*       */             
/*       */             }
/*       */             else
/*       */             {
/*   470 */               TakeResultEnum.TARGET_BLOCKED.setIndexText(performer.getWurmId(), new String[] { target.getName(), result
/*   471 */                     .getFirstBlocker().getName() });
/*   472 */               return TakeResultEnum.TARGET_BLOCKED;
/*       */             }
/*       */           
/*   475 */           } catch (NoSuchZoneException|NoSuchItemException e) {
/*       */             
/*   477 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */           }
/*       */         
/*       */         } else {
/*       */           
/*   482 */           performer.achievement(165);
/*   483 */           TakeResultEnum.CARRYING_TOO_MUCH.setIndexText(performer.getWurmId(), new String[] { target.getName() });
/*   484 */           return TakeResultEnum.CARRYING_TOO_MUCH;
/*       */         } 
/*       */       } else {
/*       */         
/*   488 */         return TakeResultEnum.INVENTORY_FULL;
/*       */       } 
/*   490 */     }  return TakeResultEnum.UNKNOWN_FAILURE;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean setTheftEffects(Creature performer, Action act, Item target) {
/*   579 */     return setTheftEffects(performer, act, target.getTileX(), target.getTileY(), target.isOnSurface());
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean setTheftEffects(Creature performer, Action act, int tilex, int tiley, boolean surfaced) {
/*   585 */     boolean noticed = false;
/*   586 */     boolean deityUpset = true;
/*   587 */     Village village = Zones.getVillage(tilex, tiley, performer.isOnSurface());
/*   588 */     if (village != null) {
/*       */       
/*   590 */       if (village.guards.size() > 0 && village.checkGuards(act, performer))
/*       */       {
/*   592 */         noticed = true;
/*   593 */         if (!village.isEnemy(performer.getCitizenVillage())) {
/*       */           
/*   595 */           performer.setUnmotivatedAttacker();
/*   596 */           if (performer.getKingdomTemplateId() != 3)
/*       */           {
/*   598 */             if (Servers.localServer.HOMESERVER) {
/*       */               
/*   600 */               performer.setReputation(Math.max(-3, performer.getReputation() - 35));
/*   601 */               performer.setStealth(false);
/*       */             } else {
/*       */               
/*   604 */               performer.setReputation(performer.getReputation() - 35);
/*       */             }  } 
/*   606 */           village.modifyReputations(act, performer);
/*       */         } else {
/*       */           
/*   609 */           deityUpset = false;
/*   610 */         }  performer.getCommunicator().sendNormalServerMessage("A guard has noted you!", (byte)4);
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/*   615 */       VolaTile tile = Zones.getTileOrNull(tilex, tiley, surfaced);
/*   616 */       if (tile != null && performer.isFriendlyKingdom(tile.getKingdom()) && tile.getKingdom() != 3) {
/*       */         
/*   618 */         Structure struct = tile.getStructure();
/*   619 */         if (struct != null && struct.isFinished()) {
/*       */           
/*   621 */           if (Servers.localServer.HOMESERVER)
/*       */           {
/*   623 */             if (!performer.isOnPvPServer()) {
/*       */               
/*   625 */               performer.setUnmotivatedAttacker();
/*   626 */               performer.setReputation(Math.max(-3, performer.getReputation() - 35));
/*   627 */               performer.getCommunicator().sendNormalServerMessage("You get the feeling someone noticed you!", (byte)4);
/*       */             } 
/*       */           }
/*   630 */           for (VirtualZone vz : tile.getWatchers()) {
/*       */ 
/*       */             
/*       */             try {
/*   634 */               if (vz.getWatcher() != null && vz.getWatcher().getCurrentTile() != null)
/*       */               {
/*   636 */                 if (vz.getWatcher().isFriendlyKingdom(performer.getKingdomId())) {
/*       */                   
/*   638 */                   performer.setUnmotivatedAttacker();
/*   639 */                   boolean cares = false;
/*   640 */                   if (Servers.localServer.HOMESERVER && !performer.isOnPvPServer()) {
/*       */                     
/*   642 */                     cares = true;
/*       */                     
/*   644 */                     if (vz.getWatcher().isPlayer())
/*       */                     {
/*   646 */                       if (vz.getWatcher().getWurmId() != performer.getWurmId()) {
/*       */                         
/*   648 */                         performer.setStealth(false);
/*   649 */                         vz.getWatcher()
/*   650 */                           .getCommunicator()
/*   651 */                           .sendNormalServerMessage("You notice " + performer
/*   652 */                             .getName() + " trying to do something fishy!", (byte)4);
/*       */                       } 
/*       */                     }
/*       */ 
/*       */                     
/*   657 */                     if (vz.getWatcher().isKingdomGuard())
/*       */                     {
/*   659 */                       cares = true;
/*       */                     }
/*   661 */                     if (!cares)
/*       */                     {
/*   663 */                       cares = struct.isGuest(vz.getWatcher());
/*       */                     }
/*   665 */                     if (vz.getWatcher().getWurmId() != performer.getWurmId()) {
/*       */                       
/*   667 */                       float dist = Math.max(
/*   668 */                           Math.abs((vz.getWatcher().getCurrentTile()).tilex - tilex), 
/*   669 */                           Math.abs((vz.getWatcher().getCurrentTile()).tiley - tiley));
/*   670 */                       if (cares && dist <= 20.0F)
/*       */                       {
/*   672 */                         if (cares) if (performer
/*   673 */                             .getStealSkill().skillCheck((100 - 
/*   674 */                               Math.min(
/*   675 */                                 Math.abs((vz.getWatcher().getCurrentTile()).tilex - tilex), 
/*       */                                 
/*   677 */                                 Math.abs((vz.getWatcher().getCurrentTile()).tiley - tiley)) * 4), 0.0D, false, 10.0F) < 0.0D) {
/*       */ 
/*       */                             
/*   680 */                             noticed = true;
/*   681 */                             performer.setReputation(performer.getReputation() - 10);
/*       */                             
/*   683 */                             performer.getCommunicator().sendNormalServerMessage("You get the feeling someone noticed you!", (byte)4);
/*       */                             
/*   685 */                             vz.getWatcher()
/*   686 */                               .getCommunicator()
/*   687 */                               .sendNormalServerMessage("You notice " + performer
/*   688 */                                 .getName() + " trying to do something fishy!", (byte)4);
/*       */ 
/*       */                             
/*   691 */                             performer.setStealth(false);
/*       */                             break;
/*       */                           } 
/*       */                       
/*       */                       }
/*       */                     } 
/*       */                   } 
/*       */                 } 
/*       */               }
/*   700 */             } catch (Exception e) {
/*       */               
/*   702 */               logger.log(Level.WARNING, e.getMessage(), e);
/*       */             } 
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     } 
/*   708 */     if (deityUpset && performer.getDeity() != null && !performer.getDeity().isLibila())
/*       */     {
/*   710 */       if (Server.rand.nextInt(Math.max(1, (int)performer.getFaith())) < 5)
/*       */       {
/*   712 */         if (act.getNumber() != 101) {
/*       */           
/*   714 */           performer.getCommunicator().sendNormalServerMessage(
/*   715 */               (performer.getDeity()).name + " has noticed you and is upset at your thieving behaviour!", (byte)4);
/*       */           
/*   717 */           performer.modifyFaith(-0.25F);
/*   718 */           performer.maybeModifyAlignment(-5.0F);
/*       */         } 
/*       */       }
/*       */     }
/*   722 */     return noticed;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final boolean checkIfStealing(Item target, Creature player, @Nullable Action act) {
/*   727 */     if (target.getOwnerId() == -10L) {
/*       */       
/*   729 */       if (player.getPower() > 0)
/*   730 */         return false; 
/*   731 */       if (target.lastOwner == player.getWurmId())
/*   732 */         return false; 
/*   733 */       if (target.getTemplateId() == 128)
/*   734 */         return false; 
/*   735 */       if (target.getParentId() != -10L) {
/*       */         
/*       */         try {
/*       */           
/*   739 */           Item topparent = Items.getItem(target.getTopParent());
/*   740 */           if (topparent.isDraggable() && topparent.isLockable() && 
/*   741 */             mayUseInventoryOfVehicle(player, topparent))
/*       */           {
/*   743 */             return false;
/*       */           }
/*   745 */           if (topparent.isLocked() && topparent
/*   746 */             .mayShowPermissions(player) && topparent.mayAccessHold(player))
/*   747 */             return false; 
/*   748 */           VolaTile parentTile = Zones.getTileOrNull(topparent.getTilePos(), target.isOnSurface());
/*   749 */           if (parentTile != null && parentTile.getStructure() != null) {
/*       */             
/*       */             try {
/*       */               
/*   753 */               if (parentTile.getStructure().mayPass(player)) {
/*       */                 
/*   755 */                 Item current = target.getParentOrNull();
/*   756 */                 boolean mayEatThis = false;
/*       */                 
/*   758 */                 while (current != null) {
/*       */                   
/*   760 */                   if (current.isLocked() && current.mayAccessHold(player)) {
/*   761 */                     mayEatThis = true;
/*   762 */                   } else if (current.isLocked() && !current.mayAccessHold(player)) {
/*   763 */                     return true;
/*       */                   } 
/*   765 */                   current = current.getParentOrNull();
/*       */                 } 
/*       */                 
/*   768 */                 if (mayEatThis && act != null && act.getNumber() == 182) {
/*   769 */                   return false;
/*       */                 }
/*       */               } 
/*   772 */               if (!mayTakeThingsFromStructure(player, target, parentTile.tilex, parentTile.tiley))
/*       */               {
/*   774 */                 if (target.lastOwner != -10L && target.lastOwner != player.getWurmId()) {
/*   775 */                   return true;
/*       */                 }
/*       */               }
/*   778 */             } catch (NoSuchStructureException noSuchStructureException) {}
/*       */ 
/*       */           
/*       */           }
/*       */         
/*       */         }
/*   784 */         catch (NoSuchItemException noSuchItemException) {}
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*   790 */       if (act != null && act.getNumber() == 74 && target.isVehicle() && target
/*   791 */         .mayShowPermissions(player) && target.mayDrag(player))
/*   792 */         return false; 
/*   793 */       if (act != null && act.getNumber() == 606)
/*   794 */         return false; 
/*   795 */       if (act != null && act.getNumber() == 189 && target.isOilConsuming())
/*   796 */         return false; 
/*   797 */       if (act != null && act.getNumber() == 183 && target.mayAccessHold(player))
/*   798 */         return false; 
/*   799 */       if (act == null && target.mayShowPermissions(player) && target.mayAccessHold(player)) {
/*   800 */         return false;
/*       */       }
/*   802 */       int tilex = target.getTileX();
/*   803 */       int tiley = target.getTileY();
/*   804 */       Village village = Zones.getVillage(tilex, tiley, target.isOnSurface());
/*   805 */       if (village != null) {
/*       */         
/*   807 */         if (!player.isOnPvPServer() || (player
/*   808 */           .isFriendlyKingdom(village.kingdom) && !village.isEnemy(player.getCitizenVillage())))
/*       */         {
/*       */           
/*       */           try {
/*   812 */             if (!mayTakeThingsFromStructure(player, target, tilex, tiley))
/*       */             {
/*   814 */               if (target.lastOwner != -10L && target.lastOwner != player.getWurmId())
/*       */               {
/*   816 */                 return true;
/*       */               }
/*       */             }
/*       */           }
/*   820 */           catch (NoSuchStructureException nss) {
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*   825 */             if (act != null && act.getNumber() == 83) {
/*       */ 
/*       */ 
/*       */               
/*   829 */               if (target.lastOwner != -10L && target.lastOwner != player.getWurmId() && 
/*   830 */                 !Methods.isActionAllowed(player, (short)83, target))
/*   831 */                 return true; 
/*       */             } else {
/*   833 */               if (target.lastOwner != -10L && target.lastOwner != player.getWurmId() && 
/*   834 */                 !village.isActionAllowed((short)6, player, false, 0, 0))
/*       */               {
/*   836 */                 return true;
/*       */               }
/*       */ 
/*       */               
/*   840 */               if (act != null && act.getNumber() == 6)
/*       */               {
/*   842 */                 if (target.isPlanted() && (target.isSign() || target.isStreetLamp() || target.isFlag() || target
/*   843 */                   .isBulkContainer() || target.getTemplateId() == 742))
/*       */                 {
/*       */                   
/*   846 */                   if (target.lastOwner != -10L && target.lastOwner != player.getWurmId() && 
/*   847 */                     !village.isActionAllowed((short)685, player, false, 0, 0))
/*       */                   {
/*   849 */                     return true;
/*       */                   }
/*       */                 }
/*       */               }
/*       */             } 
/*       */           } 
/*   855 */           return isStealingInVicinity(target, player);
/*       */         }
/*       */       
/*   858 */       } else if (player.getKingdomTemplateId() != 3) {
/*       */ 
/*       */         
/*       */         try {
/*       */ 
/*       */           
/*   864 */           if (!mayTakeThingsFromStructure(player, target, tilex, tiley))
/*       */           {
/*   866 */             if (target.lastOwner != -10L && target.lastOwner != player.getWurmId())
/*       */             {
/*   868 */               return true;
/*       */             }
/*       */           }
/*       */         }
/*   872 */         catch (NoSuchStructureException noSuchStructureException) {}
/*       */ 
/*       */         
/*   875 */         return isStealingInVicinity(target, player);
/*       */       } 
/*       */     } 
/*   878 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final boolean isStealingInVicinity(Item target, Creature player) {
/*   883 */     if (!Servers.localServer.PVPSERVER)
/*       */     {
/*   885 */       if (target.lastOwner != -10L && target.lastOwner != player.getWurmId() && player.getPower() < 2)
/*       */       {
/*   887 */         if (WurmId.getType(target.lastOwner) == 0) {
/*       */           
/*       */           try {
/*       */             
/*   891 */             Player lastOwner = Players.getInstance().getPlayer(target.lastOwner);
/*   892 */             if (lastOwner.getPower() < 2 && (lastOwner
/*   893 */               .getCitizenVillage() == null || lastOwner
/*   894 */               .getCitizenVillage() != player.getCitizenVillage()))
/*       */             {
/*   896 */               if (!lastOwner.isFriend(player.getWurmId()))
/*       */               {
/*   898 */                 if (lastOwner.getCitizenVillage() == null || 
/*   899 */                   !lastOwner.getCitizenVillage().isAlly(player))
/*       */                 {
/*   901 */                   if (lastOwner.getTeam() == null || lastOwner.getTeam() != player.getTeam())
/*       */                   {
/*   903 */                     if (lastOwner.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 
/*   904 */                         target.isVehicle() ? 100.0F : 10.0F))
/*       */                     {
/*   906 */                       return true;
/*       */                     }
/*       */                   }
/*       */                 }
/*       */               }
/*       */             }
/*       */           }
/*   913 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */         }
/*       */       }
/*       */     }
/*       */ 
/*       */ 
/*       */     
/*   920 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static final boolean mayTakeThingsFromStructure(Creature performer, @Nullable Item item, int tilex, int tiley) throws NoSuchStructureException {
/*   926 */     boolean onSurface = false;
/*   927 */     if (item == null) {
/*   928 */       onSurface = performer.isOnSurface();
/*       */     } else {
/*   930 */       onSurface = item.isOnSurface();
/*       */     } 
/*   932 */     VolaTile tile = Zones.getTileOrNull(tilex, tiley, onSurface);
/*   933 */     if (tile != null) {
/*       */       
/*   935 */       Structure struct = tile.getStructure();
/*   936 */       if (performer.isInPvPZone())
/*   937 */         return true; 
/*   938 */       if (struct != null && struct.isFinished() && struct.isTypeHouse()) {
/*       */         
/*   940 */         if (!struct.isEnemy(performer) && !struct.mayPass(performer))
/*   941 */           return false; 
/*   942 */         long wid = performer.getWurmId();
/*       */         
/*   944 */         if (wid != struct.getOwnerId()) {
/*       */           
/*   946 */           if (!struct.isActionAllowed(performer, (short)6))
/*   947 */             return false; 
/*   948 */           if (item != null && item.isPlanted())
/*   949 */             return struct.isActionAllowed(performer, (short)685); 
/*       */         } 
/*   951 */         return true;
/*       */       } 
/*       */     } 
/*   954 */     throw new NoSuchStructureException("No structure");
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static final boolean isEnemiesNearby(Creature performer, int tileDist, boolean requireSameVillage) {
/*  1003 */     int sx = Zones.safeTileX(performer.getTileX() - tileDist);
/*  1004 */     int sy = Zones.safeTileY(performer.getTileY() - tileDist);
/*  1005 */     int ex = Zones.safeTileX(performer.getTileX() + tileDist);
/*  1006 */     int ey = Zones.safeTileY(performer.getTileY() + tileDist);
/*  1007 */     for (int x = sx; x <= ex; x++) {
/*  1008 */       for (int y = sy; y <= ey; y++) {
/*       */         
/*  1010 */         VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/*  1011 */         if (t != null)
/*       */         {
/*  1013 */           for (Creature c : t.getCreatures()) {
/*       */             
/*  1015 */             if (c.isPlayer() && !c.isFriendlyKingdom(performer.getKingdomId()) && c.isPaying() && c.getPower() == 0)
/*  1016 */               if (requireSameVillage) {
/*       */                 
/*  1018 */                 if (c.getCurrentVillage() == performer.getCurrentVillage()) {
/*  1019 */                   return true;
/*       */                 }
/*       */               } else {
/*  1022 */                 return true;
/*       */               }  
/*       */           } 
/*       */         }
/*  1026 */         VolaTile oT = Zones.getTileOrNull(x, y, !performer.isOnSurface());
/*  1027 */         if (oT != null)
/*       */         {
/*  1029 */           for (Creature c : oT.getCreatures()) {
/*       */             
/*  1031 */             if (c.isPlayer() && !c.isFriendlyKingdom(performer.getKingdomId()) && c.isPaying() && c.getPower() == 0)
/*  1032 */               if (requireSameVillage) {
/*       */                 
/*  1034 */                 if (c.getCurrentVillage() == performer.getCurrentVillage()) {
/*  1035 */                   return true;
/*       */                 }
/*       */               } else {
/*  1038 */                 return true;
/*       */               }  
/*       */           }  } 
/*       */       } 
/*  1042 */     }  return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static final boolean isLootableBy(Creature performer, Item target) {
/*  1053 */     if (performer.getPower() <= 0) {
/*       */       
/*  1055 */       if (target.getTemplateId() == 272 && target.getWasBrandedTo() != -10L)
/*       */       {
/*       */         
/*  1058 */         return target.mayCommand(performer);
/*       */       }
/*  1060 */       long targid = target.getWurmId();
/*  1061 */       long toppar = target.getTopParent();
/*  1062 */       Item topparent = null;
/*       */       
/*       */       try {
/*  1065 */         if (toppar != targid) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  1071 */           topparent = Items.getItem(toppar);
/*       */         } else {
/*       */           
/*  1074 */           topparent = target;
/*       */         } 
/*  1076 */         if (topparent.isTent() || topparent.isUseMaterialAndKingdom())
/*       */         {
/*  1078 */           if (topparent.isNewbieItem() && !Servers.localServer.PVPSERVER) {
/*       */             
/*  1080 */             if (topparent.lastOwner != performer.getWurmId())
/*       */             {
/*  1082 */               return false;
/*       */             }
/*       */           }
/*  1085 */           else if (topparent.getLockId() > -10L) {
/*       */             
/*  1087 */             if (topparent.lastOwner != performer.getWurmId())
/*       */             {
/*  1089 */               return false;
/*       */             }
/*       */           } 
/*       */         }
/*  1093 */         if (topparent.getTemplateId() == 272) {
/*       */           
/*  1095 */           VolaTile ttile = Zones.getTileOrNull(topparent.getTileX(), topparent.getTileY(), topparent
/*  1096 */               .isOnSurface());
/*  1097 */           int dist = (ttile == null || ttile.getVillage() == null) ? 10 : 5;
/*  1098 */           if (Servers.localServer.isChallengeOrEpicServer() && isEnemiesNearby(performer, dist, true))
/*  1099 */             return false; 
/*  1100 */           if (!topparent.isCorpseLootable())
/*       */           {
/*  1102 */             if (WurmId.getType(topparent.lastOwner) == 0)
/*       */             {
/*  1104 */               if (topparent.lastOwner != performer.getWurmId()) {
/*       */                 
/*  1106 */                 byte kingdom = Players.getInstance().getKingdomForPlayer(topparent.lastOwner);
/*  1107 */                 if (kingdom != 3 && kingdom == performer.getKingdomId() && 
/*  1108 */                   !Servers.isThisAChaosServer())
/*       */                 {
/*  1110 */                   PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(topparent.lastOwner);
/*  1111 */                   if (pinf != null) {
/*       */                     
/*  1113 */                     if (!pinf.isFlagSet(34)) {
/*       */                       
/*  1115 */                       Friend[] friends = pinf.getFriends();
/*  1116 */                       for (Friend lFriend : friends) {
/*       */                         
/*  1118 */                         if (lFriend.getFriendId() == performer.getWurmId() && lFriend
/*  1119 */                           .getCategory() == Friend.Category.Trusted) {
/*  1120 */                           return true;
/*       */                         }
/*       */                       } 
/*       */                     } 
/*       */                     try {
/*  1125 */                       Village citizenVillage = Villages.getVillageForCreature(topparent.lastOwner);
/*  1126 */                       if (citizenVillage != null) {
/*       */                         
/*  1128 */                         if (citizenVillage.isCitizen(performer) && 
/*  1129 */                           !pinf.isFlagSet(33))
/*  1130 */                           return true; 
/*  1131 */                         if (citizenVillage.isAlly(performer) && 
/*  1132 */                           !pinf.isFlagSet(32))
/*  1133 */                           return true; 
/*       */                       } 
/*  1135 */                       Player p = Players.getInstance().getPlayer(topparent.lastOwner);
/*  1136 */                       if (p.getCitizenVillage() != null) {
/*       */                         
/*  1138 */                         if (p.getCitizenVillage().isCitizen(performer) && 
/*  1139 */                           !pinf.isFlagSet(33))
/*  1140 */                           return true; 
/*  1141 */                         if (p.getCitizenVillage().isAlly(performer) && 
/*  1142 */                           !pinf.isFlagSet(32)) {
/*  1143 */                           return true;
/*       */                         }
/*       */                       } 
/*  1146 */                     } catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */                   } 
/*       */ 
/*       */ 
/*       */                   
/*  1151 */                   return false;
/*       */                 }
/*       */               
/*       */               } 
/*       */             }
/*       */           }
/*       */         } 
/*  1158 */       } catch (NoSuchItemException nsi) {
/*       */ 
/*       */         
/*  1161 */         logger.log(Level.INFO, "No top parent for " + target.getTopParent());
/*  1162 */         return false;
/*       */       } 
/*       */     } 
/*  1165 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean mayDropDirt(Creature performer) {
/*  1171 */     int dropTileX = (int)performer.getStatus().getPositionX() + 2 >> 2;
/*  1172 */     int dropTileY = (int)performer.getStatus().getPositionY() + 2 >> 2;
/*       */     
/*  1174 */     Point tiles = findDropTile(dropTileX, dropTileY, performer.isOnSurface() ? Server.surfaceMesh : Server.caveMesh);
/*  1175 */     dropTileX = tiles.getX();
/*  1176 */     dropTileY = tiles.getY();
/*  1177 */     for (int x = 0; x >= -1; x--) {
/*       */       
/*  1179 */       for (int y = 0; y >= -1; y--) {
/*       */         
/*  1181 */         Village village = Zones.getVillage(dropTileX + x, dropTileY + y, performer.isOnSurface());
/*  1182 */         if (village != null && !village.isActionAllowed((short)37, performer))
/*  1183 */           return false; 
/*  1184 */         if (Zones.protectedTiles[dropTileX + x][dropTileY + y]) {
/*       */           
/*  1186 */           performer.getCommunicator().sendAlertServerMessage("The tile is protected by the gods.");
/*  1187 */           return false;
/*       */         } 
/*       */       } 
/*       */     } 
/*       */ 
/*       */     
/*  1193 */     if (Features.Feature.WAGONER.isEnabled())
/*       */     {
/*       */       
/*  1196 */       if (MethodsHighways.onWagonerCamp(dropTileX, dropTileY, performer.isOnSurface())) {
/*       */         
/*  1198 */         performer.getCommunicator().sendNormalServerMessage("The wagoner whips you once and tells you never to try dropping that here again.");
/*  1199 */         return false;
/*       */       } 
/*       */     }
/*  1202 */     if (Terraforming.wouldDestroyCobble(performer, dropTileX, dropTileY, true)) {
/*       */       
/*  1204 */       if (Features.Feature.HIGHWAYS.isEnabled()) {
/*  1205 */         performer.getCommunicator().sendAlertServerMessage("The tile is protected by the highway.", (byte)3);
/*       */       } else {
/*  1207 */         performer.getCommunicator().sendAlertServerMessage("This would destroy the pavement.", (byte)3);
/*  1208 */       }  return false;
/*       */     } 
/*  1210 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static void handlePlaceItem(Creature performer, long itemId, long parentId, float xPos, float yPos, float zPos, float rotation) {
/*  1216 */     boolean largeItem = (performer.getPlacementItem() != null && performer.getPlacementItem().getWurmId() == itemId);
/*  1217 */     if (!performer.isPlacingItem()) {
/*       */       
/*  1219 */       performer.getCommunicator().sendNormalServerMessage("An error occured while placing that item.");
/*       */       
/*       */       return;
/*       */     } 
/*       */     
/*  1224 */     if (!largeItem) {
/*  1225 */       performer.setPlacingItem(false);
/*       */     }
/*  1227 */     if (itemId == -10L) {
/*       */       
/*  1229 */       performer.getCommunicator().sendNormalServerMessage("You decide against placing the item.");
/*  1230 */       performer.setPlacingItem(false);
/*       */       return;
/*       */     } 
/*  1233 */     Item parent = null;
/*  1234 */     if (parentId != -10L) {
/*       */       
/*       */       try {
/*       */         
/*  1238 */         parent = Items.getItem(parentId);
/*       */       }
/*  1240 */       catch (NoSuchItemException nsi) {
/*       */         
/*  1242 */         parent = null;
/*       */       } 
/*       */     }
/*       */     
/*  1246 */     if (parent != null && largeItem) {
/*       */       
/*  1248 */       performer.getCommunicator().sendNormalServerMessage("You must place the item from your inventory to put it there.");
/*  1249 */       performer.setPlacingItem(false);
/*       */       
/*       */       return;
/*       */     } 
/*  1253 */     float newPosX = Math.max(1.0F, (parent != null) ? (parent.getPosX() + xPos) : xPos);
/*  1254 */     float newPosY = Math.max(1.0F, (parent != null) ? (parent.getPosY() + yPos) : yPos);
/*  1255 */     newPosY = Math.min(Zones.worldMeterSizeY, newPosY);
/*  1256 */     newPosX = Math.min(Zones.worldMeterSizeX, newPosX);
/*       */     
/*  1258 */     if (!Methods.isActionAllowed(performer, largeItem ? 99 : 7, (int)newPosX >> 2, (int)newPosY >> 2) && parent != null && 
/*  1259 */       Methods.isActionAllowed(performer, (short)3, parent)) {
/*       */       
/*  1261 */       performer.setPlacingItem(false);
/*       */       return;
/*       */     } 
/*  1264 */     float xDist = Math.abs(performer.getPosX() - newPosX);
/*  1265 */     float yDist = Math.abs(performer.getPosY() - newPosY);
/*  1266 */     if ((xDist > 4.0F || yDist > 4.0F) && performer.getPower() < 2) {
/*       */       
/*  1268 */       performer.getCommunicator().sendNormalServerMessage("You cannot place the item that far away.");
/*  1269 */       performer.setPlacingItem(false);
/*       */       
/*       */       return;
/*       */     } 
/*       */     
/*       */     try {
/*  1275 */       Item target = Items.getItem(itemId);
/*  1276 */       boolean onSurface = performer.isOnSurface();
/*  1277 */       BlockingResult result = Blocking.getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), newPosX, newPosY, performer
/*  1278 */           .getPositionZ(), performer.getPositionZ(), onSurface, onSurface, false, 4, -1L, performer
/*  1279 */           .getBridgeId(), performer.getBridgeId(), false);
/*  1280 */       if (result != null) {
/*       */         
/*  1282 */         performer.getCommunicator().sendNormalServerMessage("You cannot reach that spot to place the " + target.getName() + ".");
/*       */         return;
/*       */       } 
/*  1285 */       if (target.isOnePerTile())
/*       */       {
/*  1287 */         if (!mayDropOnTile((int)newPosX >> 2, (int)newPosY >> 2, onSurface, performer.getFloorLevel())) {
/*       */           
/*  1289 */           performer.getCommunicator().sendNormalServerMessage("You cannot place that item here, since there is not enough space on that tile.", (byte)3);
/*       */           
/*  1291 */           performer.setPlacingItem(false);
/*       */           return;
/*       */         } 
/*       */       }
/*  1295 */       if (target.isTent())
/*       */       {
/*  1297 */         if (!mayDropTentOnTile(performer)) {
/*       */           
/*  1299 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to put your tent there.", (byte)3);
/*  1300 */           performer.setPlacingItem(false);
/*       */           return;
/*       */         } 
/*       */       }
/*  1304 */       if (!onSurface)
/*       */       {
/*  1306 */         if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(Zones.safeTileX((int)newPosX >> 2), 
/*  1307 */                 Zones.safeTileY((int)newPosY >> 2))))) {
/*       */           
/*  1309 */           performer.getCommunicator().sendNormalServerMessage("You cannot place the " + target.getName() + " inside the wall.");
/*  1310 */           performer.setPlacingItem(false);
/*       */           return;
/*       */         } 
/*       */       }
/*  1314 */       if (parent != null) {
/*       */         
/*  1316 */         if (!parent.testInsertItem(target) || !parent.mayCreatureInsertItem()) {
/*       */           
/*  1318 */           performer.getCommunicator().sendNormalServerMessage("There is no room for the " + target.getName() + " on the " + parent.getName() + ".");
/*  1319 */           performer.setPlacingItem(false);
/*       */           return;
/*       */         } 
/*  1322 */         if (target.isUnfinished() && target.getRealTemplate() != null && target.getRealTemplate().getVolume() > parent.getFreeVolume()) {
/*       */           
/*  1324 */           performer.getCommunicator().sendNormalServerMessage("There is no room for the " + target.getName() + " on the " + parent.getName() + ".");
/*  1325 */           performer.setPlacingItem(false);
/*       */           return;
/*       */         } 
/*  1328 */         if (parent.getPlacedItemCount() >= parent.getMaxPlaceableItems()) {
/*       */           
/*  1330 */           performer.getCommunicator().sendNormalServerMessage("You cannot put the " + target.getName() + " there, that would mean too many items on the " + parent
/*  1331 */               .getName() + ".");
/*  1332 */           performer.setPlacingItem(false);
/*       */           
/*       */           return;
/*       */         } 
/*       */       } 
/*  1337 */       if (!performer.isWithinDistanceTo(newPosX, newPosY, zPos, 6.0F) && performer.getPower() < 2) {
/*       */         
/*  1339 */         performer.getCommunicator().sendNormalServerMessage("You cannot place the item that far away.");
/*  1340 */         performer.setPlacingItem(false);
/*       */         
/*       */         return;
/*       */       } 
/*  1344 */       if (largeItem) {
/*       */         
/*  1346 */         float actualDist = (float)Math.sqrt((xDist * xDist + yDist * yDist));
/*       */ 
/*       */         
/*  1349 */         float totalTime = (float)((performer.getPlacementItem().getWeightGrams(true) / 100000.0F * actualDist) * 20.0D / performer.getBodyStrength().getKnowledge(0.0D)) * 100.0F;
/*       */         
/*       */         try {
/*  1352 */           performer.getCurrentAction().setTimeLeft((int)Math.max(50.0F, Math.min(900.0F, totalTime)));
/*  1353 */           performer.getCurrentAction().resetCounter();
/*  1354 */           performer.sendActionControl(Actions.actionEntrys[926].getActionString(), true, (int)Math.max(50.0F, Math.min(900.0F, totalTime)));
/*  1355 */           float rot = target.isVehicle() ? (rotation + 180.0F) : rotation;
/*  1356 */           while (rot > 360.0F)
/*  1357 */             rot -= 360.0F; 
/*  1358 */           while (rot < 0.0F)
/*  1359 */             rot += 360.0F; 
/*  1360 */           performer.setPendingPlacement(newPosX, newPosY, zPos, rot);
/*       */         }
/*  1362 */         catch (NoSuchActionException e) {
/*       */           
/*  1364 */           performer.getCommunicator().sendNormalServerMessage("An error occured while placing that item, please try again.");
/*  1365 */           performer.setPlacingItem(false, null);
/*       */         } 
/*       */       } else {
/*       */         
/*       */         try
/*       */         {
/*       */           
/*  1372 */           Zone zone = Zones.getZone(Zones.safeTileX((int)newPosX >> 2), Zones.safeTileY((int)newPosY >> 2), onSurface);
/*       */           
/*  1374 */           long lParentId = target.getParentId();
/*  1375 */           if (lParentId != -10L) {
/*       */             
/*  1377 */             Item currentParent = Items.getItem(lParentId);
/*  1378 */             currentParent.dropItem(target.getWurmId(), false);
/*       */           } 
/*       */           
/*  1381 */           if (parent != null) {
/*       */             
/*  1383 */             target.setPos(newPosX - parent.getPosX(), newPosY - parent.getPosY(), zPos, rotation, parent.onBridge());
/*  1384 */             if (parent.insertItem(target, false, false, true))
/*       */             {
/*  1386 */               if ((parent.isLight() || parent.isFire() || parent.getTemplate().isCooker()) && target.isBurnable())
/*  1387 */                 performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " will take damage if the " + parent
/*  1388 */                     .getName() + " is lit.", (byte)4); 
/*  1389 */               if (parent.getTemplate().isContainerWithSubItems()) {
/*  1390 */                 target.setPlacedOnParent(true);
/*       */               }
/*  1392 */               VolaTile vt = Zones.getTileOrNull(parent.getTileX(), parent.getTileY(), parent.isOnSurface());
/*  1393 */               if (vt != null)
/*       */               {
/*  1395 */                 for (VirtualZone vz : vt.getWatchers()) {
/*       */                   
/*  1397 */                   if (vz.isVisible(parent, vt)) {
/*       */ 
/*       */                     
/*  1400 */                     vz.getWatcher().getCommunicator().sendItem(target, -10L, false);
/*  1401 */                     if (target.isLight() && target.isOnFire())
/*  1402 */                       vt.addLightSource(target); 
/*  1403 */                     if ((target.getEffects()).length > 0)
/*  1404 */                       for (Effect e : target.getEffects())
/*  1405 */                         vz.addEffect(e, false);  
/*  1406 */                     if (target.getColor() != -1) {
/*  1407 */                       vz.sendRepaint(target.getWurmId(), (byte)WurmColor.getColorRed(target.getColor()), 
/*  1408 */                           (byte)WurmColor.getColorGreen(target.getColor()), (byte)WurmColor.getColorBlue(target.getColor()), (byte)-1, (byte)0);
/*       */                     }
/*  1410 */                     if (target.getColor2() != -1) {
/*  1411 */                       vz.sendRepaint(target.getWurmId(), (byte)WurmColor.getColorRed(target.getColor2()), 
/*  1412 */                           (byte)WurmColor.getColorGreen(target.getColor2()), (byte)WurmColor.getColorBlue(target.getColor2()), (byte)-1, (byte)1);
/*       */                     }
/*       */                   } 
/*       */                 } 
/*       */               }
/*  1417 */               performer.achievement(509);
/*       */             }
/*       */           
/*       */           } else {
/*       */             
/*  1422 */             target.setOnBridge(performer.getBridgeId());
/*  1423 */             float npsz = Zones.calculatePosZ(newPosX, newPosY, null, onSurface, (target
/*  1424 */                 .isFloating() && target.getCurrentQualityLevel() > 10.0F), performer.getPositionZ(), performer, target.onBridge());
/*  1425 */             float rot = target.isVehicle() ? (rotation + 180.0F) : rotation;
/*  1426 */             while (rot > 360.0F)
/*  1427 */               rot -= 360.0F; 
/*  1428 */             while (rot < 0.0F)
/*  1429 */               rot += 360.0F; 
/*  1430 */             target.setPos(newPosX, newPosY, npsz, rot, target.onBridge());
/*  1431 */             target.setSurfaced(onSurface);
/*  1432 */             zone.addItem(target);
/*       */           } 
/*       */           
/*  1435 */           SoundPlayer.playSound("sound.object.move.pushpull", target, 0.0F);
/*  1436 */           performer.getCommunicator().sendNormalServerMessage("You place " + target.getNameWithGenus() + ".");
/*  1437 */           Server.getInstance().broadCastAction(performer.getName() + " places " + target.getNameWithGenus() + ".", performer, 5);
/*  1438 */           PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.PLACED_ITEM);
/*       */         }
/*  1440 */         catch (NoSuchZoneException sex)
/*       */         {
/*  1442 */           logger.log(Level.WARNING, sex.getMessage(), (Throwable)sex);
/*  1443 */           performer.getCommunicator().sendNormalServerMessage("Unable to place the " + target.getName() + " there.");
/*       */         }
/*       */       
/*       */       } 
/*  1447 */     } catch (NoSuchItemException e) {
/*       */       
/*  1449 */       logger.log(Level.INFO, "Unable to find item " + itemId + " from " + performer.getName() + " place item response.");
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean placeItem(Creature performer, Item target, Action act, float counter) {
/*  1455 */     if (counter > 1.0F && !performer.isPlacingItem()) {
/*  1456 */       return true;
/*       */     }
/*  1458 */     if (!target.canBeDropped(true)) {
/*       */       
/*  1460 */       if (target.isHollow()) {
/*  1461 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to drop that. Make sure it doesn't contain non-dropable items.", (byte)3);
/*       */       } else {
/*       */         
/*  1464 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to drop that.", (byte)3);
/*  1465 */       }  return true;
/*       */     } 
/*  1467 */     if (target.isOnePerTile())
/*       */     {
/*  1469 */       if (!mayDropOnTile(performer)) {
/*       */         
/*  1471 */         performer.getCommunicator().sendNormalServerMessage("You cannot drop that item here, since there is not enough space in front of you.", (byte)3);
/*       */         
/*  1473 */         return true;
/*       */       } 
/*       */     }
/*  1476 */     if (target.isTent())
/*       */     {
/*  1478 */       if (!mayDropTentOnTile(performer)) {
/*       */         
/*  1480 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to put your tent there.", (byte)3);
/*  1481 */         return true;
/*       */       } 
/*       */     }
/*  1484 */     long ownId = target.getOwnerId();
/*  1485 */     if (ownId == -10L)
/*       */     {
/*  1487 */       return true;
/*       */     }
/*  1489 */     if (ownId != performer.getWurmId()) {
/*       */       
/*  1491 */       logger.log(Level.WARNING, "Hmm " + performer.getName() + " tried to drop " + target.getName() + " which wasn't his.");
/*       */       
/*  1493 */       return true;
/*       */     } 
/*  1495 */     if (!Methods.isActionAllowed(performer, (short)7)) {
/*  1496 */       return true;
/*       */     }
/*  1498 */     if (counter == 1.0F) {
/*       */       
/*  1500 */       performer.getCommunicator().sendNormalServerMessage("You start to place " + target.getNameWithGenus() + ".");
/*  1501 */       Server.getInstance().broadCastAction(performer.getName() + " starts to place " + target.getNameWithGenus() + ".", performer, 5);
/*  1502 */       act.setTimeLeft(1200);
/*  1503 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/*  1504 */       performer.getCommunicator().sendPlaceItem(target);
/*  1505 */       performer.setPlacingItem(true);
/*       */       
/*  1507 */       return false;
/*       */     } 
/*       */ 
/*       */     
/*  1511 */     if (performer.isPlacingItem()) {
/*       */       
/*  1513 */       if (counter * 10.0F >= act.getTimeLeft()) {
/*       */         
/*  1515 */         performer.getCommunicator().sendNormalServerMessage("You decide against placing " + target.getNameWithGenus() + ".");
/*  1516 */         performer.getCommunicator().sendCancelPlacingItem();
/*  1517 */         performer.setPlacingItem(false, null);
/*       */         
/*  1519 */         return true;
/*       */       } 
/*       */       
/*  1522 */       return false;
/*       */     } 
/*       */ 
/*       */     
/*  1526 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean placeLargeItem(Creature performer, Item target, Action act, float counter) {
/*  1531 */     if (counter > 1.0F && (!performer.isPlacingItem() || performer.getPlacementItem() != target)) {
/*  1532 */       return true;
/*       */     }
/*  1534 */     if (target.getParentId() != -10L) {
/*       */       
/*  1536 */       performer.getCommunicator().sendNormalServerMessage("You can not place that right now.", (byte)3);
/*  1537 */       return true;
/*       */     } 
/*  1539 */     if (performer.isGuest()) {
/*       */       
/*  1541 */       performer.getCommunicator().sendNormalServerMessage("Sorry, but we cannot allow our guests to place items.", (byte)3);
/*  1542 */       return true;
/*       */     } 
/*  1544 */     if (!target.isTurnable(performer) || !target.isMoveable(performer)) {
/*       */       
/*  1546 */       performer.getCommunicator().sendNormalServerMessage("Sorry, but you are not allowed to place that.", (byte)3);
/*  1547 */       return true;
/*       */     } 
/*  1549 */     if (checkIfStealing(target, performer, act)) {
/*       */       
/*  1551 */       if (!performer.maySteal()) {
/*       */         
/*  1553 */         performer.getCommunicator().sendNormalServerMessage("You need more body control to steal things.", (byte)3);
/*  1554 */         return true;
/*       */       } 
/*  1556 */       performer.getCommunicator().sendNormalServerMessage("You have to steal the " + target.getName() + " instead.", (byte)3);
/*  1557 */       return true;
/*       */     } 
/*  1559 */     if (target.isCorpse() && target.getWasBrandedTo() != -10L && !target.mayCommand(performer)) {
/*       */       
/*  1561 */       performer.getCommunicator().sendNormalServerMessage("You may not move the corpse as you do not have permissions.", (byte)3);
/*  1562 */       return true;
/*       */     } 
/*  1564 */     if (target.isCorpse() && Servers.localServer.isChallengeOrEpicServer()) {
/*       */       
/*  1566 */       VolaTile ttile = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  1567 */       int distance = (ttile == null || ttile.getVillage() == null) ? 10 : 5;
/*  1568 */       if (isEnemiesNearby(performer, distance, true)) {
/*       */         
/*  1570 */         performer.getCommunicator().sendNormalServerMessage("You may not move the corpse when there are enemies nearby.", (byte)3);
/*  1571 */         return true;
/*       */       } 
/*       */     } 
/*  1574 */     if (Items.isItemDragged(target)) {
/*       */       
/*  1576 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is being dragged and may not be moved that way at the moment.", (byte)3);
/*       */       
/*  1578 */       return true;
/*       */     } 
/*       */     
/*  1581 */     Vehicle vehicle = Vehicles.getVehicle(target);
/*  1582 */     boolean performerIsAllowedToDriveVehicle = false;
/*  1583 */     if (vehicle != null) {
/*       */       
/*  1585 */       for (Seat lSeat : vehicle.seats) {
/*       */         
/*  1587 */         if (lSeat.isOccupied()) {
/*       */           
/*  1589 */           performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is occupied and may not be moved that way at the moment.", (byte)3);
/*       */           
/*  1591 */           return true;
/*       */         } 
/*       */       } 
/*  1594 */       if (vehicle.draggers != null && vehicle.draggers.size() > 0) {
/*       */         
/*  1596 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " may not be moved that way at the moment.", (byte)3);
/*  1597 */         return true;
/*       */       } 
/*  1599 */       if (VehicleBehaviour.mayDriveVehicle(performer, target, act) && VehicleBehaviour.canBeDriverOfVehicle(performer, vehicle))
/*       */       {
/*  1601 */         performerIsAllowedToDriveVehicle = true;
/*       */       }
/*       */     } 
/*  1604 */     if (target.isBoat())
/*       */     {
/*  1606 */       if (target.getData() != -1L) {
/*       */         
/*  1608 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " won't budge. It's moored.", (byte)3);
/*  1609 */         return true;
/*       */       } 
/*       */     }
/*  1612 */     if (performerIsAllowedToDriveVehicle != true || !target.isVehicle())
/*       */     {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  1619 */       if (!Methods.isActionAllowed(performer, (short)99, target)) {
/*  1620 */         return true;
/*       */       }
/*       */     }
/*  1623 */     if (counter == 1.0F) {
/*       */       
/*  1625 */       performer.getCommunicator().sendNormalServerMessage("You start to place " + target.getNameWithGenus() + ".");
/*  1626 */       Server.getInstance().broadCastAction(performer.getName() + " starts to place " + target.getNameWithGenus() + ".", performer, 5);
/*  1627 */       act.setTimeLeft(1200);
/*  1628 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/*  1629 */       performer.getCommunicator().sendPlaceItem(target);
/*  1630 */       performer.setPlacingItem(true, target);
/*       */       
/*  1632 */       return false;
/*       */     } 
/*       */ 
/*       */     
/*  1636 */     if (performer.isPlacingItem()) {
/*       */       
/*  1638 */       if (performer.getPendingPlacement() != null) {
/*       */         
/*  1640 */         if (act.justTickedSecond())
/*       */         {
/*       */           
/*  1643 */           float[] targetPoint = performer.getPendingPlacement();
/*  1644 */           if (targetPoint == null) {
/*       */             
/*  1646 */             performer.getCommunicator().sendNormalServerMessage("You somehow forget where you were moving " + target.getNameWithGenus() + " to.");
/*  1647 */             performer.getCommunicator().sendCancelPlacingItem();
/*  1648 */             performer.setPlacingItem(false, null);
/*  1649 */             return true;
/*       */           } 
/*  1651 */           float percentComplete = Math.min(1.0F, (counter - 1.0F) * 10.0F / act.getTimeLeft());
/*  1652 */           float[] diff = { (targetPoint[4] - targetPoint[0]) * percentComplete, (targetPoint[5] - targetPoint[1]) * percentComplete, (targetPoint[6] - targetPoint[2]) * percentComplete, (targetPoint[7] - targetPoint[3]) * percentComplete };
/*       */ 
/*       */ 
/*       */           
/*  1656 */           float newPosX = targetPoint[0] + diff[0];
/*  1657 */           float newPosY = targetPoint[1] + diff[1];
/*  1658 */           float newPosZ = targetPoint[2] + diff[2];
/*  1659 */           float rotation = targetPoint[3] + diff[3];
/*  1660 */           boolean onSurface = performer.getPlacementItem().isOnSurface();
/*       */ 
/*       */           
/*       */           try {
/*  1664 */             Zone oldZone = Zones.getZone(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  1665 */             oldZone.removeItem(target, true, true);
/*  1666 */             long lParentId = target.getParentId();
/*  1667 */             if (lParentId != -10L) {
/*       */               
/*  1669 */               Item parent = Items.getItem(lParentId);
/*  1670 */               parent.dropItem(target.getWurmId(), false);
/*       */             } 
/*       */             
/*  1673 */             target.setOnBridge(performer.getBridgeId());
/*  1674 */             float npsz = Zones.calculatePosZ(newPosX, newPosY, null, onSurface, (target
/*  1675 */                 .isFloating() && target.getCurrentQualityLevel() > 10.0F), performer.getPositionZ(), performer, target.onBridge());
/*  1676 */             target.setPos(newPosX, newPosY, npsz, rotation, target.onBridge());
/*  1677 */             target.setSurfaced(onSurface);
/*       */             
/*  1679 */             Zone newZone = Zones.getZone(target.getTileX(), target.getTileY(), onSurface);
/*  1680 */             newZone.addItem(target, true, false, false);
/*  1681 */             performer.getStatus().modifyStamina(-1000.0F);
/*       */           }
/*  1683 */           catch (NoSuchZoneException|NoSuchItemException sex) {
/*       */             
/*  1685 */             logger.log(Level.WARNING, sex.getMessage(), (Throwable)sex);
/*  1686 */             performer.getCommunicator().sendNormalServerMessage("Unable to place the " + target.getName() + " there.");
/*       */           } 
/*       */           
/*  1689 */           if (percentComplete >= 1.0F) {
/*       */             
/*  1691 */             performer.getCommunicator().sendNormalServerMessage("You finish placing " + target.getNameWithGenus() + ".");
/*  1692 */             Server.getInstance().broadCastAction(performer.getName() + " finishes placing " + target.getNameWithGenus() + ".", performer, 5);
/*  1693 */             performer.getCommunicator().sendCancelPlacingItem();
/*  1694 */             performer.setPlacingItem(false, null);
/*       */             
/*  1696 */             return true;
/*       */           } 
/*       */           
/*  1699 */           return false;
/*       */         }
/*       */       
/*  1702 */       } else if (counter * 10.0F >= act.getTimeLeft()) {
/*       */         
/*  1704 */         performer.getCommunicator().sendNormalServerMessage("You decide against placing " + target.getNameWithGenus() + ".");
/*  1705 */         performer.getCommunicator().sendCancelPlacingItem();
/*  1706 */         performer.setPlacingItem(false, null);
/*       */         
/*  1708 */         return true;
/*       */       } 
/*       */       
/*  1711 */       return false;
/*       */     } 
/*       */ 
/*       */     
/*  1715 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static String[] drop(Creature performer, Item target, boolean onGround) {
/*  1731 */     String[] fail = new String[0];
/*  1732 */     if (!target.canBeDropped(true)) {
/*       */       
/*  1734 */       if (target.isHollow()) {
/*  1735 */         performer.getCommunicator().sendSafeServerMessage("You are not allowed to drop that. Make sure it doesn't contain non-dropable items.");
/*       */       } else {
/*       */         
/*  1738 */         performer.getCommunicator().sendSafeServerMessage("You are not allowed to drop that.", (byte)3);
/*       */       } 
/*       */     } else {
/*       */ 
/*       */       
/*       */       try {
/*  1744 */         if (target.isOnePerTile())
/*       */         {
/*  1746 */           if (!mayDropOnTile(performer)) {
/*       */             
/*  1748 */             performer.getCommunicator().sendNormalServerMessage("You cannot drop that item here, since there is not enough space in front of you.", (byte)3);
/*       */             
/*  1750 */             return fail;
/*       */           } 
/*       */         }
/*  1753 */         if (target.isBeingWorkedOn()) {
/*       */           
/*  1755 */           performer.getCommunicator().sendNormalServerMessage("You cannot drop that, since you are busy working with it.", (byte)3);
/*       */           
/*  1757 */           return fail;
/*       */         } 
/*  1759 */         if (target.isTent())
/*       */         {
/*  1761 */           if (!mayDropTentOnTile(performer)) {
/*       */             
/*  1763 */             performer.getCommunicator().sendNormalServerMessage("You are not allowed to put your tent there.", (byte)3);
/*       */             
/*  1765 */             return fail;
/*       */           } 
/*       */         }
/*  1768 */         long ownId = target.getOwnerId();
/*  1769 */         if (ownId == -10L)
/*       */         {
/*       */ 
/*       */           
/*  1773 */           return fail;
/*       */         }
/*  1775 */         if (ownId != performer.getWurmId()) {
/*       */           
/*  1777 */           logger.log(Level.WARNING, "Hmm " + performer.getName() + " tried to drop " + target.getName() + " which wasn't his.");
/*       */           
/*  1779 */           return fail;
/*       */         } 
/*  1781 */         if (!Methods.isActionAllowed(performer, (short)7))
/*  1782 */           return fail; 
/*  1783 */         boolean dropAsPile = true;
/*  1784 */         if (!performer.isOnSurface() || (performer.getCurrentTile()).isTransition)
/*  1785 */           dropAsPile = false; 
/*  1786 */         if (dropAsPile && onGround && (target
/*  1787 */           .getTemplateId() == 26 || target.getTemplateId() == 298))
/*       */         {
/*  1789 */           int dropTileX = (int)performer.getStatus().getPositionX() + 2 >> 2;
/*  1790 */           int dropTileY = (int)performer.getStatus().getPositionY() + 2 >> 2;
/*       */           
/*  1792 */           if (dropTileX < 0 || dropTileX > 1 << Constants.meshSize || dropTileY < 0 || dropTileY > 1 << Constants.meshSize) {
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  1797 */             String str = target.getName();
/*  1798 */             Items.destroyItem(target.getWurmId());
/*  1799 */             return new String[] { "The deep water absorbs the ", str, " and it disappears in the currents.", " into deep water and it vanishs." };
/*       */           } 
/*       */ 
/*       */           
/*  1803 */           MeshIO mesh = Server.surfaceMesh;
/*  1804 */           if (!performer.isOnSurface())
/*  1805 */             mesh = Server.caveMesh; 
/*  1806 */           int digTile = mesh.getTile(dropTileX, dropTileY);
/*       */ 
/*       */ 
/*       */           
/*  1810 */           short h = Tiles.decodeHeight(digTile);
/*  1811 */           Point tiles = findDropTile(dropTileX, dropTileY, mesh);
/*  1812 */           dropTileX = tiles.getX();
/*  1813 */           dropTileY = tiles.getY(); int x;
/*  1814 */           for (x = 0; x >= -1; x--) {
/*       */             
/*  1816 */             for (int y = 0; y >= -1; y--) {
/*       */               
/*  1818 */               if (!Methods.isActionAllowed(performer, (short)37))
/*  1819 */                 return fail; 
/*       */             } 
/*       */           } 
/*  1822 */           if (!mayDropDirt(performer))
/*  1823 */             return fail; 
/*  1824 */           for (x = 0; x >= -1; x--) {
/*       */             
/*  1826 */             for (int y = 0; y >= -1; y--) {
/*       */               
/*  1828 */               VolaTile tile = Zones.getTileOrNull(dropTileX + x, dropTileY + y, performer.isOnSurface());
/*  1829 */               if (tile != null) {
/*       */                 
/*  1831 */                 Structure struct = tile.getStructure();
/*  1832 */                 if (struct != null)
/*       */                 {
/*  1834 */                   if (struct.isTypeBridge() && tile.getBridgeParts() != null) {
/*       */                     
/*  1836 */                     BridgePart bridgePart = tile.getBridgeParts()[0];
/*       */                     
/*  1838 */                     if (bridgePart.getType().isSupportType()) {
/*       */                       
/*  1840 */                       performer.getCommunicator().sendNormalServerMessage("The bridge support nearby prevents dropping dirt.", (byte)3);
/*       */                       
/*  1842 */                       return fail;
/*       */                     } 
/*  1844 */                     if ((x == -1 && bridgePart.hasEastExit()) || (x == 0 && bridgePart
/*  1845 */                       .hasWestExit()) || (y == -1 && bridgePart
/*  1846 */                       .hasSouthExit()) || (y == 0 && bridgePart
/*  1847 */                       .hasNorthExit())) {
/*       */                       
/*  1849 */                       performer
/*  1850 */                         .getCommunicator()
/*  1851 */                         .sendNormalServerMessage("You are too close to the end of the bridge to drop dirt here.", (byte)3);
/*       */ 
/*       */                       
/*  1854 */                       return fail;
/*       */                     } 
/*       */                     
/*  1857 */                     int bridgeHeight = bridgePart.getRealHeight();
/*  1858 */                     int tileHeight = Tiles.decodeHeight(mesh.getTile(dropTileX, dropTileY));
/*  1859 */                     if (bridgeHeight - tileHeight < 25)
/*       */                     {
/*  1861 */                       performer.getCommunicator()
/*  1862 */                         .sendNormalServerMessage("You are too close to the bottom of the bridge to drop the dirt.", (byte)3);
/*       */ 
/*       */                       
/*  1865 */                       return fail;
/*       */                     }
/*       */                   
/*       */                   } else {
/*       */                     
/*  1870 */                     performer.getCommunicator()
/*  1871 */                       .sendNormalServerMessage("The dirt would flow down into a structure. You are not allowed to drop dirt on structures.", (byte)3);
/*       */ 
/*       */                     
/*  1874 */                     return fail;
/*       */                   } 
/*       */                 }
/*  1877 */                 Fence[] fences = tile.getFencesForLevel(0);
/*  1878 */                 for (Fence f : fences) {
/*       */                   
/*  1880 */                   if ((y == 0 && f.isHorizontal()) || (x == 0 && !f.isHorizontal())) {
/*       */                     
/*  1882 */                     performer.getCommunicator()
/*  1883 */                       .sendNormalServerMessage("The dirt would flow down onto a fence. You are not allowed to drop dirt on fences.", (byte)3);
/*       */ 
/*       */                     
/*  1886 */                     return fail;
/*       */                   } 
/*       */                 } 
/*       */               } 
/*  1890 */               if (!performer.isPaying()) {
/*       */                 
/*  1892 */                 int newTile = mesh.getTile(dropTileX + x, dropTileY + y);
/*  1893 */                 if (Terraforming.isRoad(Tiles.decodeType(newTile))) {
/*       */                   
/*  1895 */                   performer.getCommunicator()
/*  1896 */                     .sendNormalServerMessage("The dirt would flow down onto a road. Only premium players are allowed to drop dirt on roads.", (byte)3);
/*       */ 
/*       */                   
/*  1899 */                   return fail;
/*       */                 } 
/*       */               } 
/*       */             } 
/*       */           } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  1909 */           String tNameg = target.getNameWithGenus();
/*  1910 */           boolean change = false;
/*       */           
/*  1912 */           if (target.getWeightGrams() >= 20000)
/*  1913 */             change = true; 
/*  1914 */           String tName = target.getName(); int i;
/*  1915 */           for (i = 0; i >= -1; i--) {
/*       */             
/*  1917 */             for (int y = 0; y >= -1; y--) {
/*       */               
/*  1919 */               int newTile = mesh.getTile(dropTileX + i, dropTileY + y);
/*  1920 */               byte newType = Tiles.decodeType(newTile);
/*  1921 */               if (newType == Tiles.Tile.TILE_HOLE.id || Tiles.isMineDoor(newType)) {
/*       */ 
/*       */ 
/*       */                 
/*  1925 */                 performer.getCommunicator()
/*  1926 */                   .sendNormalServerMessage("The dirt would flow down into a mine.", (byte)3);
/*       */ 
/*       */                 
/*  1929 */                 return fail;
/*       */               } 
/*  1931 */               if (newType == Tiles.Tile.TILE_LAVA.id) {
/*       */                 
/*  1933 */                 Items.destroyItem(target.getWurmId());
/*       */ 
/*       */                 
/*  1936 */                 return new String[] { "The ", tName, " disappears into the lava.", " which then disappears into the lava." };
/*       */               } 
/*       */             } 
/*       */           } 
/*       */ 
/*       */           
/*  1942 */           Items.destroyItem(target.getWurmId());
/*       */           
/*  1944 */           if (change) {
/*       */             
/*  1946 */             for (i = 0; i >= -1; i--) {
/*       */               
/*  1948 */               for (int y = 0; y >= -1; y--) {
/*       */                 
/*  1950 */                 boolean changed = false;
/*  1951 */                 int newTile = mesh.getTile(dropTileX + i, dropTileY + y);
/*  1952 */                 byte newType = Tiles.decodeType(newTile);
/*  1953 */                 byte oldType = newType;
/*  1954 */                 h = Tiles.decodeHeight(newTile);
/*  1955 */                 short mod = 0;
/*  1956 */                 if (i == 0 && y == 0) {
/*       */                   
/*  1958 */                   mod = 1;
/*  1959 */                   changed = true;
/*       */                 } 
/*  1961 */                 short newHeight = (short)Math.min(32767, h + mod);
/*  1962 */                 if (newType == Tiles.Tile.TILE_ROCK.id || newType == Tiles.Tile.TILE_DIRT.id || newType == Tiles.Tile.TILE_DIRT_PACKED.id || newType == Tiles.Tile.TILE_STEPPE.id || newType == Tiles.Tile.TILE_SAND.id || newType == Tiles.Tile.TILE_CLIFF.id) {
/*       */ 
/*       */ 
/*       */ 
/*       */                   
/*  1967 */                   if (target.getTemplateId() == 298) {
/*  1968 */                     newType = Tiles.Tile.TILE_SAND.id;
/*  1969 */                   } else if (target.getTemplateId() == 26) {
/*  1970 */                     newType = Tiles.Tile.TILE_DIRT.id;
/*  1971 */                   }  if (oldType != newType)
/*  1972 */                     TileEvent.log(dropTileX + i, dropTileY + y, 0, performer.getWurmId(), 7); 
/*  1973 */                   mesh.setTile(dropTileX + i, dropTileY + y, 
/*  1974 */                       Tiles.encode(newHeight, newType, (byte)0));
/*  1975 */                   Server.modifyFlagsByTileType(dropTileX + i, dropTileY + y, newType);
/*  1976 */                   Server.isDirtHeightLower(dropTileX + i, dropTileY + y, newHeight);
/*  1977 */                   changed = true;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*       */                 }
/*  1984 */                 else if (mod != 0) {
/*       */                   
/*  1986 */                   mesh.setTile(dropTileX + i, dropTileY + y, 
/*       */ 
/*       */                       
/*  1989 */                       Tiles.encode(newHeight, newType, 
/*  1990 */                         Tiles.decodeData(newTile)));
/*  1991 */                   Server.modifyFlagsByTileType(dropTileX + i, dropTileY + y, newType);
/*  1992 */                   Server.isDirtHeightLower(dropTileX + i, dropTileY + y, newHeight);
/*  1993 */                   changed = true;
/*       */                 } 
/*       */                 
/*  1996 */                 if (changed) {
/*       */ 
/*       */                   
/*       */                   try {
/*  2000 */                     Zone toCheckForChange = Zones.getZone(dropTileX + i, dropTileY + y, performer
/*  2001 */                         .isOnSurface());
/*  2002 */                     toCheckForChange.changeTile(dropTileX + i, dropTileY + y);
/*       */                   }
/*  2004 */                   catch (NoSuchZoneException nsz) {
/*       */                     
/*  2006 */                     logger.log(Level.INFO, "no such zone?: " + (dropTileX + i) + ", " + (dropTileY + y), (Throwable)nsz);
/*       */                   } 
/*       */                   
/*  2009 */                   Players.getInstance().sendChangedTile(dropTileX + i, dropTileY + y, performer
/*  2010 */                       .isOnSurface(), true);
/*       */                 } 
/*       */                 
/*  2013 */                 Tiles.Tile theTile = Tiles.getTile(newType);
/*  2014 */                 if (theTile.isTree()) {
/*       */                   
/*  2016 */                   byte data = Tiles.decodeData(newTile);
/*  2017 */                   Zones.reposWildHive(dropTileX + i, dropTileY + y, theTile, data);
/*       */                 } 
/*       */               } 
/*       */             } 
/*  2021 */             return new String[] { "You drop ", tNameg, ".", "." };
/*       */           } 
/*       */ 
/*       */ 
/*       */           
/*  2026 */           performer.getCommunicator().sendNormalServerMessage("You pour the " + tName + " on the ground. It's too little matter to change anything.");
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*       */         }
/*  2033 */         else if (target.isLiquid())
/*       */         {
/*  2035 */           String tName = target.getName();
/*  2036 */           Items.destroyItem(target.getWurmId());
/*  2037 */           performer.getCommunicator().sendNormalServerMessage("You pour the " + tName + " on the ground.");
/*       */           
/*  2039 */           Server.getInstance().broadCastAction(performer
/*  2040 */               .getName() + " pours some " + tName + " on the ground.", performer, 5);
/*       */         }
/*       */         else
/*       */         {
/*  2044 */           if (!target.isCoin() && (performer.getPower() == 0 || Servers.localServer.testServer)) {
/*       */             
/*  2046 */             int[] tilecoords = Item.getDropTile(performer);
/*       */             
/*  2048 */             VolaTile t = Zones.getTileOrNull(tilecoords[0], tilecoords[1], performer.isOnSurface());
/*       */ 
/*       */             
/*  2051 */             if (t != null) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  2057 */               if (t.getNumberOfItems(t.getDropFloorLevel(performer.getFloorLevel())) > 99) {
/*       */                 
/*  2059 */                 performer.getCommunicator().sendNormalServerMessage("That place is too littered with items already.", (byte)3);
/*       */                 
/*  2061 */                 return fail;
/*       */               } 
/*  2063 */               if (target.isDecoration())
/*       */               {
/*  2065 */                 if (t.getStructure() != null)
/*       */                 {
/*  2067 */                   if (t.getNumberOfDecorations(t.getDropFloorLevel(performer.getFloorLevel())) > 14) {
/*       */                     
/*  2069 */                     performer.getCommunicator().sendNormalServerMessage("That place is too littered with decorations already.", (byte)3);
/*       */ 
/*       */                     
/*  2072 */                     return fail;
/*       */                   } 
/*       */                 }
/*       */               }
/*  2076 */               if (target.isOutsideOnly())
/*       */               {
/*  2078 */                 if (t.getStructure() != null) {
/*       */                   
/*  2080 */                   performer.getCommunicator().sendNormalServerMessage("You cannot drop that inside.", (byte)3);
/*       */                   
/*  2082 */                   return fail;
/*       */                 } 
/*       */               }
/*       */             } 
/*       */           } 
/*  2087 */           if (performer.getCurrentTile().getNumberOfItems(performer.getFloorLevel()) > 120) {
/*       */ 
/*       */             
/*  2090 */             performer.getCommunicator().sendNormalServerMessage("This area is too littered with items already.", (byte)3);
/*       */             
/*  2092 */             return fail;
/*       */           } 
/*  2094 */           target.putItemInfrontof(performer);
/*  2095 */           performer.checkTheftWarnQuestion();
/*       */ 
/*       */ 
/*       */           
/*  2099 */           if (target.isTent())
/*       */           {
/*  2101 */             performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/*       */           }
/*  2103 */           return new String[] { "You drop ", target
/*  2104 */               .getNameWithGenus(), ".", "." };
/*       */         }
/*       */       
/*       */       }
/*  2108 */       catch (NoSuchItemException nsi) {
/*       */         
/*  2110 */         logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*       */       }
/*  2112 */       catch (NoSuchCreatureException nsc) {
/*       */         
/*  2114 */         logger.log(Level.WARNING, "Failed to locate creature " + performer.getWurmId(), (Throwable)nsc);
/*       */       }
/*  2116 */       catch (NoSuchPlayerException nsp) {
/*       */         
/*  2118 */         logger.log(Level.WARNING, "Failed to locate player " + performer.getWurmId(), (Throwable)nsp);
/*       */       }
/*  2120 */       catch (NoSuchZoneException nsz) {
/*       */         
/*  2122 */         Items.destroyItem(target.getWurmId());
/*       */       } 
/*       */     } 
/*  2125 */     return fail;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean startFire(Creature performer, Item source, Item target, float counter) {
/*  2130 */     boolean toReturn = false;
/*  2131 */     int time = 200;
/*  2132 */     Skills skills = performer.getSkills();
/*  2133 */     Skill primSkill = null;
/*  2134 */     Action act = null;
/*       */     
/*       */     try {
/*  2137 */       act = performer.getCurrentAction();
/*       */     }
/*  2139 */     catch (NoSuchActionException nsa) {
/*       */       
/*  2141 */       logger.log(Level.WARNING, "This action doesn't exist? " + performer.getName(), (Throwable)nsa);
/*  2142 */       return true;
/*       */     } 
/*       */     
/*       */     try {
/*  2146 */       primSkill = skills.getSkill(1010);
/*       */     }
/*  2148 */     catch (Exception ex) {
/*       */       
/*  2150 */       primSkill = skills.learn(1010, 1.0F);
/*       */     } 
/*       */     
/*  2153 */     if (target.isOnFire()) {
/*       */       
/*  2155 */       performer.getCommunicator().sendNormalServerMessage("The fire is already burning.", (byte)3);
/*  2156 */       return true;
/*       */     } 
/*       */     
/*  2159 */     if (target.getTemplate().isTransportable())
/*       */     {
/*  2161 */       if (target.getTopParent() != target.getWurmId()) {
/*       */         
/*  2163 */         String message = StringUtil.format("The %s must be on the ground before you can light it.", new Object[] { target
/*       */               
/*  2165 */               .getName() });
/*  2166 */         performer.getCommunicator().sendNormalServerMessage(message, (byte)3);
/*  2167 */         return true;
/*       */       } 
/*       */     }
/*       */     
/*  2171 */     if (counter == 1.0F) {
/*       */       
/*  2173 */       if (target.getTemplateId() == 74)
/*       */       {
/*  2175 */         if (target.getData2() > 0)
/*  2176 */           performer.getCommunicator().sendNormalServerMessage("The dale is already burning.", (byte)3); 
/*       */       }
/*  2178 */       if (target.getTemplateId() != 1243) {
/*       */         
/*  2180 */         Item kindling = performer.getCarriedItem(36);
/*  2181 */         if (kindling == null) {
/*       */           
/*  2183 */           performer.getCommunicator().sendNormalServerMessage("You need at least one kindling to start a fire.", (byte)3);
/*       */           
/*  2185 */           return true;
/*       */         } 
/*  2187 */         int templateWeight = kindling.getTemplate().getWeightGrams();
/*  2188 */         int currentWeight = kindling.getWeightGrams();
/*  2189 */         if (currentWeight < templateWeight)
/*       */         {
/*  2191 */           performer.getCommunicator().sendNormalServerMessage("The kindling contains too little material to light the " + target
/*  2192 */               .getName() + ".  Try to combine any of them with a similar object to get larger pieces.");
/*       */           
/*  2194 */           return true;
/*       */         }
/*       */       
/*  2197 */       } else if (target.getAuxData() == 0) {
/*       */         
/*  2199 */         performer.getCommunicator().sendNormalServerMessage("You need to add fuel the bee smoker first.");
/*  2200 */         return true;
/*       */       } 
/*  2202 */       time = Actions.getStandardActionTime(performer, primSkill, source, 0.0D);
/*  2203 */       act.setTimeLeft(time);
/*  2204 */       performer.getCommunicator().sendNormalServerMessage("You start to light the " + target.getName() + ".");
/*  2205 */       Server.getInstance().broadCastAction(performer.getName() + " starts to light the " + target.getName() + ".", performer, 5);
/*       */       
/*  2207 */       performer.sendActionControl("Lighting " + target.getName(), true, time);
/*  2208 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     }
/*       */     else {
/*       */       
/*  2212 */       time = act.getTimeLeft();
/*       */     } 
/*  2214 */     if (act.mayPlaySound())
/*       */     {
/*  2216 */       SoundPlayer.playSound("sound.fire.lighting.flintsteel", performer, 1.0F);
/*       */     }
/*  2218 */     if (act.currentSecond() == 5)
/*       */     {
/*  2220 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     }
/*  2222 */     if (counter * 10.0F > time) {
/*       */       
/*  2224 */       if (target.getTemplateId() != 1243) {
/*       */         
/*  2226 */         Item kindling = performer.getCarriedItem(36);
/*  2227 */         if (kindling == null) {
/*       */           
/*  2229 */           performer.getCommunicator().sendNormalServerMessage("You need at least one kindling to start a fire.", (byte)3);
/*       */           
/*  2231 */           return true;
/*       */         } 
/*       */ 
/*       */         
/*  2235 */         int templateWeight = kindling.getTemplate().getWeightGrams();
/*  2236 */         int currentWeight = kindling.getWeightGrams();
/*  2237 */         if (currentWeight < templateWeight) {
/*       */           
/*  2239 */           performer.getCommunicator().sendNormalServerMessage("The kindling contains too little material to light the " + target
/*  2240 */               .getName() + ".  Try to combine it with other kindling to get larger pieces.");
/*       */           
/*  2242 */           return true;
/*       */         } 
/*  2244 */         kindling.setWeight(currentWeight - templateWeight, true);
/*       */ 
/*       */ 
/*       */       
/*       */       }
/*  2249 */       else if (target.getAuxData() == 0) {
/*       */         
/*  2251 */         performer.getCommunicator().sendNormalServerMessage("You need to add fuel the bee smoker first.");
/*  2252 */         return true;
/*       */       } 
/*  2254 */       primSkill.skillCheck((Server.getWeather().getRain() * 10.0F), source.getCurrentQualityLevel(), false, counter);
/*  2255 */       if (source != null && source.isBurnable() && source.getTemperature() > 1000 && !source.isIndestructible()) {
/*       */         
/*  2257 */         performer.getCommunicator().sendNormalServerMessage("You throw the burning remnants of " + source
/*  2258 */             .getNameWithGenus() + " into " + target.getNameWithGenus() + ".");
/*  2259 */         Server.getInstance().broadCastAction(performer.getName() + " throws the burning remnants of " + source
/*  2260 */             .getNameWithGenus() + " into " + target.getNameWithGenus() + ".", performer, 5);
/*  2261 */         Items.destroyItem(source.getWurmId());
/*       */       } 
/*  2263 */       toReturn = setFire(performer, target);
/*       */     } 
/*  2265 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean setFire(Creature performer, Item target) {
/*  2270 */     if (target.getTemplate().isTransportable())
/*       */     {
/*  2272 */       if (target.getTopParent() != target.getWurmId()) {
/*       */         
/*  2274 */         String message = StringUtil.format("The %s must be on the ground before you can light it.", new Object[] { target
/*       */               
/*  2276 */               .getName() });
/*  2277 */         performer.getCommunicator().sendNormalServerMessage(message, (byte)3);
/*  2278 */         return true;
/*       */       } 
/*       */     }
/*  2281 */     if (target.getTemplateId() == 1243 && target.getAuxData() == 0) {
/*       */       
/*  2283 */       performer.getCommunicator().sendNormalServerMessage("You need to add fuel the bee smoker first.");
/*  2284 */       return true;
/*       */     } 
/*  2286 */     performer.getCommunicator().sendNormalServerMessage("You light the " + target.getName() + ".");
/*  2287 */     if (target.getTemplateId() == 178) {
/*  2288 */       target.setTemperature((short)6000);
/*       */     } else {
/*  2290 */       target.setTemperature((short)10000);
/*       */     } 
/*  2292 */     if (target.getTemplateId() == 889)
/*       */     {
/*  2294 */       target.setAuxData((byte)Math.min(255, target.getAuxData() + 2));
/*       */     }
/*  2296 */     if (target.getTemplateId() != 1243) {
/*       */       
/*  2298 */       Effect effect = EffectFactory.getInstance().createFire(target.getWurmId(), target.getPosX(), target.getPosY(), target
/*  2299 */           .getPosZ(), performer.isOnSurface());
/*  2300 */       target.addEffect(effect);
/*       */     } 
/*  2302 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean gatherRiftResource(Creature performer, Item sourceTool, Item targetItem, float counter, Action act) {
/*  2307 */     if (targetItem.deleted) {
/*       */       
/*  2309 */       performer.getCommunicator().sendNormalServerMessage("The " + targetItem.getName() + " has nothing left to gather.", (byte)3);
/*       */       
/*  2311 */       return true;
/*       */     } 
/*  2313 */     boolean toReturn = true;
/*  2314 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*       */       
/*  2316 */       performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put what you might gather.");
/*       */       
/*  2318 */       return true;
/*       */     } 
/*  2320 */     if (performer.isWithinDistanceTo(targetItem.getPosX(), targetItem.getPosY(), targetItem.getPosZ(), 4.0F)) {
/*       */       
/*  2322 */       toReturn = false;
/*       */       
/*  2324 */       if (counter == 1.0F) {
/*       */         
/*  2326 */         Skills skills = performer.getSkills();
/*  2327 */         Skill relevantSkill = null;
/*       */         
/*       */         try {
/*  2330 */           switch (act.getActionEntry().getNumber()) {
/*       */             
/*       */             case 145:
/*  2333 */               relevantSkill = skills.getSkill(1008);
/*       */               break;
/*       */             case 96:
/*  2336 */               relevantSkill = skills.getSkill(1007);
/*       */               break;
/*       */             case 156:
/*  2339 */               relevantSkill = skills.getSkill(10032);
/*       */               break;
/*       */           } 
/*       */ 
/*       */         
/*  2344 */         } catch (NoSuchSkillException nss) {
/*       */           
/*  2346 */           switch (act.getActionEntry().getNumber()) {
/*       */             
/*       */             case 145:
/*  2349 */               relevantSkill = skills.learn(1008, 1.0F);
/*       */               break;
/*       */             case 96:
/*  2352 */               relevantSkill = skills.learn(1007, 1.0F);
/*       */               break;
/*       */             case 156:
/*  2355 */               relevantSkill = skills.learn(10032, 1.0F);
/*       */               break;
/*       */           } 
/*       */         
/*       */         } 
/*  2360 */         act.setTimeLeft(Actions.getSlowActionTime(performer, relevantSkill, sourceTool, 0.0D) * 1);
/*  2361 */         performer.getCommunicator().sendNormalServerMessage("You start to gather resources from " + targetItem.getNameWithGenus() + ".");
/*  2362 */         Server.getInstance().broadCastAction(performer.getName() + " starts to gather resources from " + targetItem.getNameWithGenus() + ".", performer, 5);
/*  2363 */         performer.sendActionControl("Gathering from " + targetItem.getName(), true, act.getTimeLeft());
/*  2364 */         performer.getStatus().modifyStamina(-1000.0F);
/*       */       }
/*       */       else {
/*       */         
/*  2368 */         if (act.mayPlaySound()) {
/*       */           
/*  2370 */           String soundName = "sound.work.mining1";
/*  2371 */           int soundNum = Server.rand.nextInt(3);
/*  2372 */           switch (act.getActionEntry().getNumber()) {
/*       */             
/*       */             case 145:
/*  2375 */               if (soundNum == 1) {
/*  2376 */                 soundName = "sound.work.mining2"; break;
/*  2377 */               }  if (soundNum == 2)
/*  2378 */                 soundName = "sound.work.mining3"; 
/*       */               break;
/*       */             case 96:
/*  2381 */               if (soundNum == 0) {
/*  2382 */                 soundName = "sound.work.woodcutting1"; break;
/*  2383 */               }  if (soundNum == 1) {
/*  2384 */                 soundName = "sound.work.woodcutting2"; break;
/*  2385 */               }  if (soundNum == 2)
/*  2386 */                 soundName = "sound.work.woodcutting3"; 
/*       */               break;
/*       */             case 156:
/*  2389 */               if (soundNum == 0) {
/*  2390 */                 soundName = "sound.work.prospecting1"; break;
/*  2391 */               }  if (soundNum == 1) {
/*  2392 */                 soundName = "sound.work.prospecting2"; break;
/*  2393 */               }  if (soundNum == 2) {
/*  2394 */                 soundName = "sound.work.prospecting3";
/*       */               }
/*       */               break;
/*       */           } 
/*  2398 */           SoundPlayer.playSound(soundName, performer, 1.0F);
/*       */         } 
/*  2400 */         if (act.currentSecond() % 5 == 0) {
/*       */           
/*  2402 */           sourceTool.setDamage(sourceTool.getDamage() + 0.0015F * sourceTool.getDamageModifier());
/*  2403 */           performer.getStatus().modifyStamina(-3000.0F);
/*       */         } 
/*       */       } 
/*       */       
/*  2407 */       if (counter * 10.0F > act.getTimeLeft()) {
/*       */         
/*  2409 */         if (act.getRarity() != 0)
/*       */         {
/*  2411 */           performer.playPersonalSound("sound.fx.drumroll");
/*       */         }
/*  2413 */         Skills skills = performer.getSkills();
/*  2414 */         Skill relevantSkill = null;
/*  2415 */         Skill toolSkill = null;
/*       */         
/*       */         try {
/*  2418 */           switch (act.getActionEntry().getNumber()) {
/*       */             
/*       */             case 145:
/*  2421 */               relevantSkill = skills.getSkill(1008);
/*       */               break;
/*       */             case 96:
/*  2424 */               relevantSkill = skills.getSkill(1007);
/*       */               break;
/*       */             case 156:
/*  2427 */               relevantSkill = skills.getSkill(10032);
/*       */               break;
/*       */           } 
/*       */ 
/*       */         
/*  2432 */         } catch (NoSuchSkillException nss) {
/*       */           
/*  2434 */           switch (act.getActionEntry().getNumber()) {
/*       */             
/*       */             case 145:
/*  2437 */               relevantSkill = skills.learn(1008, 1.0F);
/*       */               break;
/*       */             case 96:
/*  2440 */               relevantSkill = skills.learn(1007, 1.0F);
/*       */               break;
/*       */             case 156:
/*  2443 */               relevantSkill = skills.learn(10032, 1.0F);
/*       */               break;
/*       */           } 
/*       */ 
/*       */         
/*       */         } 
/*       */         try {
/*  2450 */           toolSkill = skills.getSkill(sourceTool.getPrimarySkill());
/*       */         }
/*  2452 */         catch (Exception ex) {
/*       */ 
/*       */           
/*       */           try {
/*  2456 */             toolSkill = skills.learn(sourceTool.getPrimarySkill(), 1.0F);
/*       */           }
/*  2458 */           catch (NoSuchSkillException nse) {
/*       */             
/*  2460 */             logger.log(Level.WARNING, performer.getName() + " trying to gather resources with an item with no primary skill: " + sourceTool
/*  2461 */                 .getName());
/*       */           } 
/*       */         } 
/*       */         
/*  2465 */         toReturn = true;
/*  2466 */         float difficulty = 60.0F;
/*  2467 */         double bonus = toolSkill.skillCheck(60.0D, sourceTool, 0.0D, false, counter) / 5.0D;
/*  2468 */         double power = relevantSkill.skillCheck(60.0D, sourceTool, bonus, false, counter);
/*  2469 */         float maxQL = targetItem.getCurrentQualityLevel();
/*       */         
/*  2471 */         if (targetItem.getTemplate().isRiftStoneDeco() || targetItem
/*  2472 */           .getTemplate().isRiftCrystalDeco()) {
/*       */           
/*       */           try
/*       */           {
/*  2476 */             if (power > 0.0D) {
/*       */               
/*  2478 */               float resPower = GeneralUtilities.calcRareQuality(power, act.getRarity(), sourceTool.getRarity());
/*  2479 */               Item riftResource = ItemFactory.createItem(targetItem.getTemplate().isRiftCrystalDeco() ? 1103 : 1102, 
/*  2480 */                   Math.min(maxQL, resPower), act.getRarity(), performer.getName());
/*  2481 */               performer.getInventory().insertItem(riftResource);
/*  2482 */               performer.getCommunicator().sendNormalServerMessage("You get " + riftResource.getNameWithGenus() + ".", (byte)2);
/*  2483 */               Server.getInstance().broadCastAction(performer.getName() + " finds " + riftResource.getNameWithGenus() + ".", performer, 5);
/*       */             }
/*       */             else {
/*       */               
/*  2487 */               performer.getCommunicator().sendNormalServerMessage("You find it too difficult to get useful stone from the " + targetItem
/*  2488 */                   .getName() + ".", (byte)2);
/*       */             } 
/*       */             
/*  2491 */             float stonePower = GeneralUtilities.calcOreRareQuality(power, act.getRarity(), sourceTool.getRarity());
/*  2492 */             Item stoneShards = ItemFactory.createItem(146, Math.min(maxQL, stonePower), performer.getPosX(), performer
/*  2493 */                 .getPosY(), Server.rand.nextFloat() * 360.0F, performer.isOnSurface(), act.getRarity(), -10L, null);
/*  2494 */             if (targetItem.getWeightGrams() < stoneShards.getWeightGrams())
/*  2495 */               stoneShards.setWeight(targetItem.getWeightGrams(), true); 
/*  2496 */             targetItem.setWeight(targetItem.getWeightGrams() - stoneShards.getWeightGrams(), true);
/*  2497 */             performer.getCommunicator().sendNormalServerMessage("You finish mining " + stoneShards.getName() + " from the " + targetItem
/*  2498 */                 .getName() + ".");
/*  2499 */             Server.getInstance().broadCastAction(performer.getName() + " mines " + stoneShards.getName() + " from the " + targetItem
/*  2500 */                 .getName() + ".", performer, 5);
/*       */           }
/*  2502 */           catch (Exception ex)
/*       */           {
/*  2504 */             logger.log(Level.WARNING, ex.getMessage());
/*  2505 */             performer.getCommunicator().sendNormalServerMessage("You stumble at the last second and nothing happens.");
/*       */           }
/*       */         
/*  2508 */         } else if (targetItem.getTemplate().isRiftPlantDeco()) {
/*       */ 
/*       */           
/*       */           try {
/*  2512 */             if (power > 0.0D) {
/*       */               
/*  2514 */               float resPower = GeneralUtilities.calcRareQuality(power, act.getRarity(), sourceTool.getRarity());
/*  2515 */               Item riftWood = ItemFactory.createItem(1104, Math.min(maxQL, resPower), act.getRarity(), performer.getName());
/*  2516 */               if (targetItem.getTemplateId() == 1041) {
/*  2517 */                 riftWood.setMaterial((byte)38);
/*  2518 */               } else if (targetItem.getTemplateId() == 1042) {
/*  2519 */                 riftWood.setMaterial((byte)40);
/*  2520 */               } else if (targetItem.getTemplateId() == 1043) {
/*  2521 */                 riftWood.setMaterial((byte)41);
/*  2522 */               } else if (targetItem.getTemplateId() == 1044) {
/*  2523 */                 riftWood.setMaterial((byte)51);
/*       */               } 
/*  2525 */               performer.getInventory().insertItem(riftWood);
/*  2526 */               performer.getCommunicator().sendNormalServerMessage("You get " + riftWood.getNameWithGenus() + ".", (byte)2);
/*  2527 */               Server.getInstance().broadCastAction(performer.getName() + " finds " + riftWood.getNameWithGenus() + ".", performer, 5);
/*       */             }
/*       */             else {
/*       */               
/*  2531 */               performer.getCommunicator().sendNormalServerMessage("You find it too difficult to get useful wood from the " + targetItem
/*  2532 */                   .getName() + ".", (byte)2);
/*       */             } 
/*       */             
/*  2535 */             byte material = 0;
/*  2536 */             if (targetItem.getTemplateId() == 1041) {
/*  2537 */               material = 38;
/*  2538 */             } else if (targetItem.getTemplateId() == 1042) {
/*  2539 */               material = 40;
/*  2540 */             } else if (targetItem.getTemplateId() == 1043) {
/*  2541 */               material = 41;
/*  2542 */             } else if (targetItem.getTemplateId() == 1044) {
/*  2543 */               material = 51;
/*  2544 */             }  float woodPower = GeneralUtilities.calcRareQuality(power, act.getRarity(), sourceTool.getRarity());
/*  2545 */             Item woodLog = ItemFactory.createItem(9, Math.min(maxQL, woodPower), performer.getPosX(), performer
/*  2546 */                 .getPosY(), Server.rand.nextFloat() * 360.0F, performer.isOnSurface(), material, act.getRarity(), -10L, null);
/*  2547 */             woodLog.setLastOwnerId(performer.getWurmId());
/*  2548 */             if (targetItem.getTemplateId() == 1044) {
/*       */               
/*  2550 */               if (targetItem.getWeightGrams() < 24000)
/*       */               {
/*  2552 */                 float ratio = targetItem.getWeightGrams() / 24000.0F;
/*  2553 */                 woodLog.setWeight((int)(4000.0F * ratio), true);
/*  2554 */                 targetItem.setWeight(0, true);
/*       */               }
/*       */               else
/*       */               {
/*  2558 */                 woodLog.setWeight(4000, true);
/*  2559 */                 targetItem.setWeight(targetItem.getWeightGrams() - 24000, true);
/*       */               }
/*       */             
/*       */             } else {
/*       */               
/*  2564 */               if (targetItem.getWeightGrams() < woodLog.getWeightGrams())
/*  2565 */                 woodLog.setWeight(targetItem.getWeightGrams(), true); 
/*  2566 */               targetItem.setWeight(targetItem.getWeightGrams() - woodLog.getWeightGrams(), true);
/*       */             } 
/*  2568 */             performer.getCommunicator().sendNormalServerMessage("You finish chopping " + woodLog.getNameWithGenus() + " from the " + targetItem
/*  2569 */                 .getName() + ".");
/*  2570 */             Server.getInstance().broadCastAction(performer.getName() + " chops " + woodLog.getNameWithGenus() + " from the " + targetItem
/*  2571 */                 .getName() + ".", performer, 5);
/*       */           }
/*  2573 */           catch (Exception ex) {
/*       */             
/*  2575 */             logger.log(Level.WARNING, ex.getMessage());
/*  2576 */             performer.getCommunicator().sendNormalServerMessage("You stumble at the last second and nothing happens.");
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     } else {
/*       */       
/*  2582 */       performer.getCommunicator().sendNormalServerMessage("You can't reach the " + targetItem.getName() + ".", (byte)3);
/*  2583 */     }  return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean mine(Creature performer, Item pickAxe, Item boulder, float counter, Action act) {
/*  2588 */     if (boulder.deleted) {
/*       */       
/*  2590 */       performer.getCommunicator().sendNormalServerMessage("The " + boulder.getName() + " contains no more ore.", (byte)3);
/*       */       
/*  2592 */       return true;
/*       */     } 
/*  2594 */     boolean toReturn = true;
/*  2595 */     if (boulder.getZoneId() != -10L) {
/*       */       
/*  2597 */       if (performer.isWithinDistanceTo(boulder.getPosX(), boulder.getPosY(), boulder.getPosZ(), 4.0F)) {
/*       */         
/*  2599 */         toReturn = false;
/*  2600 */         int time = 150;
/*       */         
/*  2602 */         if (counter == 1.0F) {
/*       */           
/*  2604 */           Skills skills = performer.getSkills();
/*  2605 */           Skill mining = null;
/*       */           
/*       */           try {
/*  2608 */             mining = skills.getSkill(1008);
/*       */           }
/*  2610 */           catch (NoSuchSkillException nss) {
/*       */             
/*  2612 */             mining = skills.learn(1008, 1.0F);
/*       */           } 
/*  2614 */           time = Actions.getStandardActionTime(performer, mining, pickAxe, 0.0D);
/*  2615 */           act.setTimeLeft(time);
/*  2616 */           performer.getCommunicator().sendNormalServerMessage("You start to mine the " + boulder.getName() + ".");
/*       */           
/*  2618 */           Server.getInstance().broadCastAction(performer
/*  2619 */               .getName() + " starts to mine " + boulder.getNameWithGenus() + ".", performer, 5);
/*  2620 */           performer.sendActionControl("Mining " + boulder.getName(), true, time);
/*  2621 */           performer.getStatus().modifyStamina(-400.0F);
/*       */         }
/*       */         else {
/*       */           
/*  2625 */           time = act.getTimeLeft();
/*  2626 */           if (act.mayPlaySound()) {
/*       */             
/*  2628 */             String sstring = "sound.work.mining1";
/*  2629 */             int x = Server.rand.nextInt(3);
/*  2630 */             if (x == 0) {
/*  2631 */               sstring = "sound.work.mining2";
/*  2632 */             } else if (x == 1) {
/*  2633 */               sstring = "sound.work.mining3";
/*  2634 */             }  SoundPlayer.playSound(sstring, performer, 1.0F);
/*       */           } 
/*  2636 */           if (act.currentSecond() % 5 == 0) {
/*       */             
/*  2638 */             pickAxe.setDamage(pickAxe.getDamage() + 0.0015F * pickAxe.getDamageModifier());
/*  2639 */             performer.getStatus().modifyStamina(-7000.0F);
/*       */           } 
/*       */         } 
/*  2642 */         if (counter * 10.0F > time) {
/*       */           
/*  2644 */           Skills skills = performer.getSkills();
/*  2645 */           Skill mining = null;
/*       */           
/*       */           try {
/*  2648 */             mining = skills.getSkill(1008);
/*       */           }
/*  2650 */           catch (NoSuchSkillException nss) {
/*       */             
/*  2652 */             mining = skills.learn(1008, 1.0F);
/*       */           } 
/*  2654 */           Skill tool = null;
/*       */           
/*       */           try {
/*  2657 */             tool = skills.getSkill(pickAxe.getPrimarySkill());
/*       */           }
/*  2659 */           catch (Exception ex) {
/*       */ 
/*       */             
/*       */             try {
/*  2663 */               tool = skills.learn(pickAxe.getPrimarySkill(), 1.0F);
/*       */             }
/*  2665 */             catch (NoSuchSkillException nse) {
/*       */               
/*  2667 */               logger.log(Level.WARNING, performer.getName() + " trying to mine with an item with no primary skill: " + pickAxe
/*  2668 */                   .getName());
/*       */             } 
/*       */           } 
/*  2671 */           toReturn = true;
/*  2672 */           int itemTemplateCreated = 146;
/*  2673 */           float diff = 1.0F;
/*  2674 */           double bonus = tool.skillCheck(1.0D, pickAxe, 0.0D, false, counter) / 5.0D;
/*  2675 */           double power = Math.max(1.0D, mining.skillCheck(1.0D, pickAxe, bonus, false, counter));
/*  2676 */           if (mining.getKnowledge(0.0D) < power)
/*  2677 */             power = mining.getKnowledge(0.0D); 
/*  2678 */           if (power > 0.0D) {
/*       */             
/*  2680 */             int m = 100;
/*  2681 */             power = Math.min(power, 100.0D);
/*  2682 */             if (pickAxe.isCrude())
/*  2683 */               power = 1.0D; 
/*  2684 */             if (boulder.getTemplateId() == 692) {
/*  2685 */               itemTemplateCreated = 693;
/*  2686 */             } else if (boulder.getTemplateId() == 696) {
/*  2687 */               itemTemplateCreated = 697;
/*       */             } 
/*       */           } else {
/*       */             
/*  2691 */             power = Math.max(1.0F, Server.rand.nextInt(10));
/*       */           } 
/*       */           
/*       */           try {
/*  2695 */             float modifier = 1.0F;
/*  2696 */             if (pickAxe.getSpellEffects() != null)
/*       */             {
/*  2698 */               modifier = pickAxe.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*       */             }
/*       */             
/*  2701 */             float orePower = GeneralUtilities.calcOreRareQuality(power * modifier, act.getRarity(), pickAxe.getRarity());
/*       */             
/*  2703 */             Item newItem = ItemFactory.createItem(itemTemplateCreated, orePower, performer
/*  2704 */                 .getPosX(), performer
/*  2705 */                 .getPosY(), Server.rand.nextFloat() * 360.0F, performer.isOnSurface(), act.getRarity(), -10L, null);
/*       */             
/*  2707 */             newItem.setLastOwnerId(performer.getWurmId());
/*  2708 */             newItem.setWeight((int)(newItem.getWeightGrams() * 0.25F), true);
/*  2709 */             Items.destroyItem(boulder.getWurmId());
/*  2710 */             performer.getCommunicator().sendNormalServerMessage("You mine some " + newItem.getName() + ".");
/*  2711 */             Server.getInstance().broadCastAction(performer.getName() + " mines some " + newItem.getName() + ".", performer, 5);
/*       */           
/*       */           }
/*  2714 */           catch (Exception ex) {
/*       */             
/*  2716 */             logger.log(Level.WARNING, ex.getMessage());
/*  2717 */             performer.getCommunicator().sendNormalServerMessage("You stumble at the last second and nothing happens.");
/*       */           }
/*       */         
/*       */         } 
/*       */       } else {
/*       */         
/*  2723 */         performer.getCommunicator().sendNormalServerMessage("You can't reach the " + boulder.getName() + " now.", (byte)3);
/*       */       } 
/*       */     } else {
/*       */       
/*  2727 */       performer.getCommunicator().sendNormalServerMessage("You can't reach the " + boulder.getName() + " now.", (byte)3);
/*       */     } 
/*  2729 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean plantSign(Creature performer, Item sign, float counter, boolean inCorner, int cornerX, int cornerY, boolean onSurface, long bridgeId, boolean atFeet, long data) {
/*  2735 */     if (cannotPlant(performer, sign))
/*  2736 */       return true; 
/*  2737 */     String planted = (sign.getParentId() == -10L) ? "secured" : "planted";
/*  2738 */     String plant = (sign.getParentId() == -10L) ? "secure" : "plant";
/*  2739 */     if (counter == 1.0F && sign.isTrellis())
/*       */     {
/*  2741 */       if (!performer.isOnSurface())
/*  2742 */         performer.getCommunicator().sendNormalServerMessage("The " + sign.getName() + " can only be harvested if " + planted + " on the surface."); 
/*       */     }
/*  2744 */     if (sign.isEnchantedTurret() || sign.isUnenchantedTurret())
/*       */     {
/*  2746 */       if (performer.getLayer() < 0) {
/*       */         
/*  2748 */         performer.getCommunicator().sendNormalServerMessage("The " + sign
/*  2749 */             .getName() + " can not be " + planted + " beneath ground.", (byte)3);
/*  2750 */         return true;
/*       */       } 
/*       */     }
/*  2753 */     if (inCorner && !performer.isWithinDistanceTo((cornerX << 2), (cornerY << 2), 0.0F, 4.0F)) {
/*       */       
/*  2755 */       performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.", (byte)3);
/*  2756 */       return true;
/*       */     } 
/*  2758 */     if (counter == 1.0F && sign.isAbility())
/*       */     {
/*  2760 */       if (!surveyAbilitySigns(sign, performer)) {
/*       */         
/*  2762 */         performer.getCommunicator().sendNormalServerMessage("You cannot activate this item here.", (byte)3);
/*       */ 
/*       */         
/*  2765 */         return true;
/*       */       } 
/*       */     }
/*  2768 */     boolean toReturn = false;
/*  2769 */     int time = Actions.getPlantActionTime(performer, sign);
/*  2770 */     Action act = null;
/*       */     
/*       */     try {
/*  2773 */       act = performer.getCurrentAction();
/*       */     }
/*  2775 */     catch (NoSuchActionException nsa) {
/*       */       
/*  2777 */       logger.log(Level.WARNING, "This action doesn't exist? " + performer.getName(), (Throwable)nsa);
/*  2778 */       return true;
/*       */     } 
/*  2780 */     if (counter == 1.0F) {
/*       */       
/*  2782 */       if (performer instanceof Player) {
/*       */         
/*  2784 */         Player p = (Player)performer;
/*  2785 */         boolean ownVillage = false;
/*  2786 */         if (performer.getCitizenVillage() != null)
/*       */         {
/*  2788 */           if (performer.getCurrentVillage() == performer.getCitizenVillage())
/*  2789 */             ownVillage = true; 
/*       */         }
/*  2791 */         if (sign.isPlantOneAWeek() && p.hasPlantedSign() && !ownVillage) {
/*       */           
/*  2793 */           performer.getCommunicator().sendNormalServerMessage("You may only plant one " + sign
/*  2794 */               .getName() + " outside your settlement per week.", (byte)3);
/*       */           
/*  2796 */           return true;
/*       */         } 
/*       */ 
/*       */ 
/*       */         
/*       */         try {
/*  2802 */           Skills skills = p.getSkills();
/*  2803 */           Skill dig = skills.getSkill(1009);
/*  2804 */           if (dig.getRealKnowledge() < 10.0D)
/*       */           {
/*  2806 */             performer.getCommunicator().sendNormalServerMessage("You need to have 10 in the skill digging to secure " + sign
/*  2807 */                 .getTemplate().getPlural() + " to the ground.", (byte)3);
/*       */ 
/*       */             
/*  2810 */             return true;
/*       */           }
/*       */         
/*  2813 */         } catch (NoSuchSkillException nss) {
/*       */           
/*  2815 */           performer.getCommunicator().sendNormalServerMessage("You need 10 digging to plant " + sign
/*  2816 */               .getTemplate().getPlural() + ".", (byte)3);
/*  2817 */           return true;
/*       */         } 
/*       */       } 
/*       */       
/*  2821 */       if (!Methods.isActionAllowed(performer, (short)176))
/*  2822 */         return true; 
/*  2823 */       if (sign.isSign() && sign.getDescription().length() == 0) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  2833 */         performer.getCommunicator().sendNormalServerMessage("Write something on the " + sign.getName() + " first.", (byte)3);
/*       */         
/*  2835 */         return true;
/*       */       } 
/*  2837 */       int tile = performer.getCurrentTileNum();
/*  2838 */       if (sign.getTemplateId() == 1342) {
/*       */         
/*  2840 */         if (performer.getStatus().getBridgeId() != -10L) {
/*       */           
/*  2842 */           performer.getCommunicator().sendNormalServerMessage(sign
/*  2843 */               .getName() + " must be planted in water, not on a bridge.", (byte)3);
/*  2844 */           return true;
/*       */         } 
/*  2846 */         int depth = FishEnums.getWaterDepth(performer.getPosX(), performer.getPosY(), performer.isOnSurface());
/*  2847 */         if (depth < 2) {
/*       */           
/*  2849 */           performer.getCommunicator().sendNormalServerMessage("The water is not deep enough for the " + sign.getName() + ".");
/*  2850 */           return true;
/*       */         } 
/*  2852 */         if (depth > 30)
/*       */         {
/*  2854 */           performer.getCommunicator().sendNormalServerMessage("The water is too deep for the " + sign.getName() + ".");
/*  2855 */           return true;
/*       */         }
/*       */       
/*       */       }
/*  2859 */       else if (sign.getTemplateId() != 805 && sign.getTemplateId() != 1396 && performer.getStatus().getBridgeId() == -10L) {
/*       */ 
/*       */         
/*  2862 */         if ((sign.isRoadMarker() && Tiles.decodeHeight(tile) < -7) || (
/*  2863 */           !sign.isRoadMarker() && Tiles.decodeHeight(tile) < 0))
/*       */         {
/*  2865 */           performer.getCommunicator().sendNormalServerMessage("The water is too deep to plant the " + sign
/*  2866 */               .getName() + ".", (byte)3);
/*  2867 */           return true;
/*       */         }
/*       */       
/*  2870 */       } else if (Tiles.decodeHeight(tile) < 0 && sign.getTemplateId() != 805 && sign
/*  2871 */         .getTemplateId() != 1396 && !sign.isRoadMarker() && performer.getStatus().getBridgeId() == -10L) {
/*       */         
/*  2873 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to plant the " + sign
/*  2874 */             .getName() + ".", (byte)3);
/*  2875 */         return true;
/*       */       } 
/*  2877 */       act.setTimeLeft(time);
/*  2878 */       performer.getCommunicator().sendNormalServerMessage("You start to " + plant + " the " + sign.getName() + ".");
/*  2879 */       if (sign.isAbility())
/*       */       {
/*  2881 */         performer.getCommunicator().sendAlertServerMessage("WARNING: This can NOT be aborted once the " + sign
/*  2882 */             .getName() + " finishes planting!", (byte)2);
/*       */       }
/*       */       
/*  2885 */       if (sign.isSign()) {
/*  2886 */         Server.getInstance().broadCastAction(performer
/*  2887 */             .getName() + " starts to " + plant + " a sign which says: " + sign.getDescription() + ".", performer, 5);
/*       */       } else {
/*       */         
/*  2890 */         Server.getInstance().broadCastAction(performer.getName() + " starts to " + plant + " " + sign.getNameWithGenus() + ".", performer, 5);
/*       */       } 
/*  2892 */       performer.sendActionControl("Planting " + sign.getName(), true, time);
/*  2893 */       performer.getStatus().modifyStamina(-400.0F);
/*       */     }
/*       */     else {
/*       */       
/*  2897 */       time = act.getTimeLeft();
/*  2898 */       if (act.currentSecond() % 5 == 0)
/*  2899 */         performer.getStatus().modifyStamina(-1000.0F); 
/*       */     } 
/*  2901 */     if (counter * 10.0F > time)
/*       */     {
/*  2903 */       toReturn = plantSignFinish(performer, sign, inCorner, cornerX, cornerY, onSurface, bridgeId, atFeet, data);
/*       */     }
/*  2905 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean plantSignFinish(Creature performer, Item sign, boolean inCorner, int cornerX, int cornerY, boolean onSurface, long bridgeId, boolean atFeet, long data) {
/*  2911 */     if (cannotPlant(performer, sign)) {
/*  2912 */       return true;
/*       */     }
/*  2914 */     if (sign.getTemplateId() == 1342)
/*  2915 */       atFeet = true; 
/*  2916 */     String plant = (sign.getParentId() != -10L) ? "plant" : "secure";
/*       */ 
/*       */     
/*       */     try {
/*  2920 */       if (data != -1L)
/*  2921 */         sign.setData(data); 
/*  2922 */       sign.setIsPlanted(true);
/*       */       
/*  2924 */       if (sign.getParentId() != -10L) {
/*       */         
/*  2926 */         if (inCorner || atFeet) {
/*  2927 */           sign.putItemInCorner(performer, cornerX, cornerY, onSurface, bridgeId, atFeet);
/*       */         } else {
/*  2929 */           sign.putItemInfrontof(performer);
/*  2930 */         }  if (sign.isEnchantedTurret() || sign.isUnenchantedTurret()) {
/*       */           
/*  2932 */           int dist = 8;
/*  2933 */           for (int x = Zones.safeTileX(sign.getTileX() - 8); x < Zones.safeTileX(sign.getTileX() + 8); x++) {
/*       */             
/*  2935 */             for (int y = Zones.safeTileY(sign.getTileY() - 8); y < Zones.safeTileY(sign.getTileY() + 8); y++) {
/*       */               
/*  2937 */               VolaTile t = Zones.getTileOrNull(x, y, sign.isOnSurface());
/*  2938 */               if (t != null)
/*       */               {
/*  2940 */                 if (t.hasOnePerTileItem(0)) {
/*       */                   
/*  2942 */                   Item i = t.getOnePerTileItem(0);
/*  2943 */                   if (sign != i && (i.isEnchantedTurret() || i.isUnenchantedTurret()) && i.isPlanted()) {
/*       */                     
/*  2945 */                     performer.getCommunicator().sendNormalServerMessage("The " + i
/*  2946 */                         .getName() + " is too close to " + sign.getName() + ".", (byte)3);
/*       */                     
/*  2948 */                     sign.setIsPlanted(false);
/*  2949 */                     return true;
/*       */                   } 
/*       */                 } 
/*       */               }
/*       */             } 
/*       */           } 
/*       */         } 
/*       */       } 
/*  2957 */       performer.getCommunicator().sendNormalServerMessage("You " + plant + " the " + sign.getName() + ".");
/*  2958 */       Server.getInstance().broadCastAction(performer.getName() + " " + plant + "s the " + sign.getName() + ".", performer, 5);
/*       */       
/*  2960 */       boolean ownVillage = false;
/*  2961 */       if (performer.getCitizenVillage() != null)
/*       */       {
/*  2963 */         if (performer.getCurrentVillage() == performer.getCitizenVillage())
/*  2964 */           ownVillage = true; 
/*       */       }
/*  2966 */       if (sign.isPlantOneAWeek() && !ownVillage && performer instanceof Player)
/*       */       {
/*  2968 */         ((Player)performer).plantSign();
/*       */       }
/*  2970 */       if (sign.isAbility()) {
/*       */         
/*  2972 */         int signTileX = sign.getTileX();
/*  2973 */         int signTileY = sign.getTileY();
/*       */         
/*  2975 */         if (surveyAbilitySigns(sign, performer)) {
/*       */           
/*  2977 */           sign.setIsNoTake(true);
/*  2978 */           sign.hatching = true;
/*       */         } else {
/*       */           
/*  2981 */           performer.getCommunicator().sendNormalServerMessage("Nothing seems to happen. Maybe you're too close to a settlement or in enemy territory.");
/*       */         }
/*       */       
/*       */       } 
/*  2985 */     } catch (NoSuchZoneException nsz) {
/*       */       
/*  2987 */       performer.getCommunicator().sendNormalServerMessage("You fail to " + plant + " the " + sign
/*  2988 */           .getName() + ". Something is weird.");
/*  2989 */       logger.log(Level.WARNING, performer.getName() + ": " + nsz.getMessage(), (Throwable)nsz);
/*       */     }
/*  2991 */     catch (NoSuchCreatureException nsc) {
/*       */       
/*  2993 */       performer.getCommunicator().sendNormalServerMessage("You fail to " + plant + " the " + sign
/*  2994 */           .getName() + ". Something is weird.");
/*  2995 */       logger.log(Level.WARNING, performer.getName() + ": " + nsc.getMessage(), (Throwable)nsc);
/*       */     }
/*  2997 */     catch (NoSuchPlayerException nsp) {
/*       */       
/*  2999 */       performer.getCommunicator().sendNormalServerMessage("You fail to " + plant + " the " + sign
/*  3000 */           .getName() + ". Something is weird.");
/*  3001 */       logger.log(Level.WARNING, performer.getName() + ": " + nsp.getMessage(), (Throwable)nsp);
/*       */     }
/*  3003 */     catch (NoSuchItemException nst) {
/*       */       
/*  3005 */       performer.getCommunicator().sendNormalServerMessage("You fail to " + plant + " the " + sign
/*  3006 */           .getName() + ". Something is weird.");
/*  3007 */       logger.log(Level.WARNING, performer.getName() + ": " + nst.getMessage(), (Throwable)nst);
/*       */     } 
/*  3009 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean surveyAbilitySigns(Item sign, Creature performer) {
/*  3024 */     if (performer != null && sign != null && sign.getTemplate() != null && sign.isAbility()) {
/*       */       
/*  3026 */       boolean ok = false;
/*  3027 */       int signTileX = sign.getTileX();
/*  3028 */       int signTileY = sign.getTileY();
/*  3029 */       if (sign.getTemplateId() == 1009) {
/*       */         
/*  3031 */         if (signTileX > 200 && signTileX < Zones.worldTileSizeX - 200)
/*       */         {
/*  3033 */           if (signTileY > 200 && signTileY < Zones.worldTileSizeY - 200) {
/*       */             
/*  3035 */             ok = true;
/*  3036 */             for (int x = signTileX - 75; x < signTileX + 75; x++) {
/*       */               
/*  3038 */               for (int y = signTileY - 75; y < signTileY + 75; y++) {
/*       */                 
/*  3040 */                 byte kd = Zones.getKingdom(x, y);
/*  3041 */                 if (kd != 0 && kd != performer.getKingdomId()) {
/*       */                   
/*  3043 */                   ok = false;
/*       */                   break;
/*       */                 } 
/*  3046 */                 if (Villages.getVillageWithPerimeterAt(x, y, true) != null) {
/*       */                   
/*  3048 */                   ok = false;
/*       */                   break;
/*       */                 } 
/*  3051 */                 VolaTile t = Zones.getTileOrNull(x, y, sign.isOnSurface());
/*  3052 */                 if (t != null && t.getStructure() != null) {
/*  3053 */                   ok = false;
/*       */                 }
/*       */               } 
/*       */             } 
/*       */           } 
/*       */         }
/*  3059 */       } else if (sign.getTemplateId() == 805) {
/*       */         
/*  3061 */         if (signTileX > 200 && signTileX < Zones.worldTileSizeX - 200)
/*       */         {
/*  3063 */           if (signTileY > 200 && signTileY < Zones.worldTileSizeY - 200) {
/*       */             
/*  3065 */             ok = true;
/*  3066 */             for (int x = signTileX - 30; x < signTileX + 30; x++) {
/*       */               
/*  3068 */               for (int y = signTileY - 30; y < signTileY + 30; y++) {
/*       */                 
/*  3070 */                 byte kd = Zones.getKingdom(x, y);
/*  3071 */                 if (kd != 0 && kd != performer.getKingdomId()) {
/*       */                   
/*  3073 */                   ok = false;
/*       */                   break;
/*       */                 } 
/*  3076 */                 if (Villages.getVillageWithPerimeterAt(x, y, true) != null) {
/*       */                   
/*  3078 */                   ok = false;
/*       */                   break;
/*       */                 } 
/*  3081 */                 VolaTile t = Zones.getTileOrNull(x, y, sign.isOnSurface());
/*  3082 */                 if (t != null && t.getStructure() != null)
/*  3083 */                   ok = false; 
/*  3084 */                 int tilenum = Zones.getTileIntForTile(x, y, 0);
/*  3085 */                 if (Tiles.decodeHeight(tilenum) >= 0) {
/*       */                   
/*  3087 */                   performer.getCommunicator().sendNormalServerMessage("You are too close to land.", (byte)3);
/*       */                   
/*  3089 */                   ok = false;
/*       */                   break;
/*       */                 } 
/*       */               } 
/*  3093 */               if (!ok)
/*       */                 break; 
/*       */             } 
/*       */           } 
/*       */         }
/*       */       } 
/*  3099 */       return ok;
/*       */     } 
/*       */     
/*  3102 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean cannotPlant(Creature performer, Item sign) {
/*  3107 */     String planted = (sign.getParentId() == -10L) ? "secured" : "planted";
/*  3108 */     String plant = (sign.getParentId() == -10L) ? "secure" : "plant";
/*  3109 */     if (sign.getCurrentQualityLevel() < 10.0F) {
/*       */       
/*  3111 */       performer.getCommunicator().sendNormalServerMessage("The " + sign.getName() + " is of too poor quality to be " + planted + ".");
/*  3112 */       return true;
/*       */     } 
/*  3114 */     if (sign.getDamage() > 70.0F) {
/*       */       
/*  3116 */       performer.getCommunicator().sendNormalServerMessage("The " + sign.getName() + " is too heavily damaged to be " + planted + ".");
/*  3117 */       return true;
/*       */     } 
/*  3119 */     if (sign.isPlanted()) {
/*       */       
/*  3121 */       performer.getCommunicator().sendNormalServerMessage("The " + sign.getName() + " is already " + planted + ".", (byte)3);
/*       */       
/*  3123 */       return true;
/*       */     } 
/*  3125 */     if (sign.isSurfaceOnly() && !performer.isOnSurface()) {
/*       */       
/*  3127 */       performer.getCommunicator().sendNormalServerMessage("The " + sign.getName() + " can only be " + planted + " on the surface.");
/*  3128 */       return true;
/*       */     } 
/*  3130 */     if (sign.isOutsideOnly()) {
/*       */ 
/*       */       
/*  3133 */       VolaTile vt = Zones.getTileOrNull(performer.getTileX(), performer.getTileY(), performer.isOnSurface());
/*  3134 */       if (vt != null && vt.getStructure() != null) {
/*       */         
/*  3136 */         performer.getCommunicator().sendNormalServerMessage("The " + sign.getName() + " can only be " + planted + " outside.");
/*  3137 */         return true;
/*       */       } 
/*       */     } 
/*  3140 */     if (sign.isFourPerTile()) {
/*       */       
/*  3142 */       VolaTile vt = Zones.getTileOrNull(performer.getTileX(), performer.getTileY(), performer.isOnSurface());
/*  3143 */       if (vt != null && vt.getFourPerTileCount(0) >= 4) {
/*       */         
/*  3145 */         performer.getCommunicator().sendNormalServerMessage("You cannot " + plant + " " + sign
/*  3146 */             .getNameWithGenus() + " as there are four here already.", (byte)3);
/*       */         
/*  3148 */         return true;
/*       */       } 
/*       */     } 
/*  3151 */     if (!sign.canBeDropped(true)) {
/*       */       
/*  3153 */       if (sign.isHollow()) {
/*  3154 */         performer.getCommunicator().sendSafeServerMessage("You are not allowed to " + plant + " that. Make sure it doesn't contain non-dropable items.");
/*       */       } else {
/*       */         
/*  3157 */         performer.getCommunicator().sendSafeServerMessage("You are not allowed to " + plant + " that.", (byte)3);
/*       */       } 
/*  3159 */       return true;
/*       */     } 
/*       */     
/*  3162 */     Item topParent = sign.getTopParentOrNull();
/*  3163 */     if (topParent != sign && !topParent.isInventory()) {
/*       */       
/*  3165 */       performer.getCommunicator().sendSafeServerMessage("You can only secure an item that is on the ground.", (byte)3);
/*       */       
/*  3167 */       return true;
/*       */     } 
/*       */     
/*  3170 */     if (sign.getParentId() == -10L) {
/*       */       
/*  3172 */       if (sign.canHavePermissions())
/*       */       {
/*       */         
/*  3175 */         if (!sign.canManage(performer)) {
/*       */           
/*  3177 */           performer.getCommunicator().sendSafeServerMessage("You do not have permission to " + plant + " that.", (byte)3);
/*       */           
/*  3179 */           return true;
/*       */         } 
/*       */       }
/*       */       
/*  3183 */       if (sign.getLastOwnerId() != performer.getWurmId() && performer.getPower() <= 1)
/*       */       {
/*  3185 */         performer.getCommunicator().sendSafeServerMessage("You do not have permission to " + plant + " that.", (byte)3);
/*       */         
/*  3187 */         return true;
/*       */       }
/*       */     
/*  3190 */     } else if (sign.isOnePerTile()) {
/*       */       
/*  3192 */       if (!mayDropOnTile(performer)) {
/*       */         
/*  3194 */         performer.getCommunicator().sendNormalServerMessage("You cannot " + plant + " that item here, since there is not enough space in front of you.", (byte)3);
/*       */ 
/*       */         
/*  3197 */         return true;
/*       */       } 
/*       */     } 
/*       */     
/*  3201 */     if (sign.getTemplateId() == 1342) {
/*       */       
/*  3203 */       int depth = FishEnums.getWaterDepth(sign.getPosX(), sign.getPosY(), sign.isOnSurface());
/*  3204 */       if (depth < 2) {
/*       */         
/*  3206 */         performer.getCommunicator().sendNormalServerMessage("The water is not deep enough for the " + sign.getName() + ".");
/*  3207 */         return true;
/*       */       } 
/*  3209 */       if (depth > 30) {
/*       */         
/*  3211 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep for the " + sign.getName() + ".");
/*  3212 */         return true;
/*       */       } 
/*       */     } 
/*  3215 */     if (sign.getTemplateId() == 1396) {
/*       */ 
/*       */       
/*  3218 */       int depth = FishEnums.getWaterDepth(sign.getPosX(), sign.getPosY(), sign.isOnSurface());
/*  3219 */       if (depth < 0) {
/*       */         
/*  3221 */         performer.getCommunicator().sendNormalServerMessage("You may not plant that out of water.");
/*  3222 */         return true;
/*       */       } 
/*       */     } 
/*  3225 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean moveItem(Creature performer, Item item, float counter, short action, Action act) {
/*  3230 */     boolean toReturn = false;
/*  3231 */     int time = 150;
/*  3232 */     if (item.getParentId() != -10L && (item.getParentOrNull() != item.getTopParentOrNull() || 
/*  3233 */       !item.getParentOrNull().getTemplate().hasViewableSubItems() || (item
/*  3234 */       .getParentOrNull().getTemplate().isContainerWithSubItems() && !item.isPlacedOnParent()))) {
/*       */       
/*  3236 */       performer.getCommunicator().sendNormalServerMessage("You can not turn that right now.", (byte)3);
/*  3237 */       return true;
/*       */     } 
/*  3239 */     if (performer.isGuest()) {
/*       */       
/*  3241 */       performer.getCommunicator().sendNormalServerMessage("Sorry, but we cannot allow our guests to move items.", (byte)3);
/*       */       
/*  3243 */       return true;
/*       */     } 
/*  3245 */     if ((action == 177 || action == 178) && !item.isTurnable(performer) && !item.isGuardTower()) {
/*       */       
/*  3247 */       performer.getCommunicator().sendNormalServerMessage("Sorry, but you are not allowed to turn that.", (byte)3);
/*       */       
/*  3249 */       return true;
/*       */     } 
/*  3251 */     if ((action == 181 || action == 99 || action == 697 || action == 696) && 
/*  3252 */       checkIfStealing(item, performer, act)) {
/*       */       
/*  3254 */       if (!performer.maySteal()) {
/*       */         
/*  3256 */         performer.getCommunicator().sendNormalServerMessage("You need more body control to steal things.", (byte)3);
/*       */         
/*  3258 */         return true;
/*       */       } 
/*  3260 */       performer.getCommunicator().sendNormalServerMessage("You have to steal the " + item.getName() + " instead.", (byte)3);
/*       */       
/*  3262 */       return true;
/*       */     } 
/*  3264 */     if (item.isCorpse() && item.getWasBrandedTo() != -10L && !item.mayCommand(performer)) {
/*       */       
/*  3266 */       performer.getCommunicator().sendNormalServerMessage("You may not move the corpse as you do not have permissions.", (byte)3);
/*       */       
/*  3268 */       return true;
/*       */     } 
/*  3270 */     if (item.isCorpse() && Servers.localServer.isChallengeOrEpicServer()) {
/*       */       
/*  3272 */       VolaTile ttile = Zones.getTileOrNull(item.getTileX(), item.getTileY(), item.isOnSurface());
/*  3273 */       int distance = (ttile == null || ttile.getVillage() == null) ? 10 : 5;
/*  3274 */       if (isEnemiesNearby(performer, distance, true)) {
/*       */         
/*  3276 */         performer.getCommunicator().sendNormalServerMessage("You may not move the corpse when there are enemies nearby.", (byte)3);
/*       */         
/*  3278 */         return true;
/*       */       } 
/*       */     } 
/*       */     
/*  3282 */     if (item.getTemplateId() == 931)
/*       */     {
/*  3284 */       if (Servers.localServer.PVPSERVER) {
/*       */         
/*  3286 */         if (action == 181 || action == 697) {
/*       */           
/*  3288 */           performer.getCommunicator().sendNormalServerMessage("The " + item
/*  3289 */               .getName() + " can only be pushed.", (byte)3);
/*  3290 */           return true;
/*       */         } 
/*       */ 
/*       */         
/*  3294 */         if (performer.getTileX() != item.getTileX() || performer.getTileY() != item.getTileY()) {
/*       */           
/*  3296 */           performer.getCommunicator().sendNormalServerMessage("You need to stand right behind the " + item
/*  3297 */               .getName() + " in order to move it.", (byte)3);
/*       */           
/*  3299 */           return true;
/*       */         } 
/*       */       } 
/*       */     }
/*       */     
/*  3304 */     if (Items.isItemDragged(item)) {
/*       */       
/*  3306 */       performer.getCommunicator().sendNormalServerMessage("The " + item
/*  3307 */           .getName() + " is being dragged and may not be moved that way at the moment.", (byte)3);
/*       */       
/*  3309 */       return true;
/*       */     } 
/*       */     
/*  3312 */     Vehicle vehicle = Vehicles.getVehicle(item);
/*  3313 */     boolean performerIsAllowedToDriveVehicle = false;
/*  3314 */     if (vehicle != null) {
/*       */       
/*  3316 */       for (Seat lSeat : vehicle.seats) {
/*       */         
/*  3318 */         if (lSeat.isOccupied()) {
/*       */           
/*  3320 */           performer.getCommunicator().sendNormalServerMessage("The " + item
/*  3321 */               .getName() + " is occupied and may not be moved that way at the moment.", (byte)3);
/*       */           
/*  3323 */           return true;
/*       */         } 
/*       */       } 
/*  3326 */       if (vehicle.draggers != null && vehicle.draggers.size() > 0) {
/*       */         
/*  3328 */         performer.getCommunicator().sendNormalServerMessage("The " + item
/*  3329 */             .getName() + " may not be moved that way at the moment.", (byte)3);
/*  3330 */         return true;
/*       */       } 
/*  3332 */       if (VehicleBehaviour.mayDriveVehicle(performer, item, act) && 
/*  3333 */         VehicleBehaviour.canBeDriverOfVehicle(performer, vehicle))
/*       */       {
/*  3335 */         performerIsAllowedToDriveVehicle = true;
/*       */       }
/*       */     } 
/*       */     
/*  3339 */     if (item.isBoat())
/*       */     {
/*  3341 */       if (item.getData() != -1L) {
/*       */         
/*  3343 */         performer.getCommunicator().sendNormalServerMessage("The " + item.getName() + " won't budge. It's moored.", (byte)3);
/*       */         
/*  3345 */         return true;
/*       */       } 
/*       */     }
/*       */     
/*  3349 */     if (performerIsAllowedToDriveVehicle != true || !item.isVehicle())
/*       */     {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  3358 */       if (!Methods.isActionAllowed(performer, action, item)) {
/*  3359 */         return true;
/*       */       }
/*       */     }
/*  3362 */     boolean insta = (performer.getPower() > 0);
/*  3363 */     if (counter == 1.0F) {
/*       */       
/*  3365 */       time = Actions.getMoveActionTime(performer);
/*  3366 */       act.setTimeLeft(time);
/*  3367 */       String actString = "turn";
/*  3368 */       if (action == 864)
/*  3369 */         actString = "move"; 
/*  3370 */       if (action == 99 || action == 696) {
/*  3371 */         actString = "push";
/*  3372 */       } else if (action == 181 || action == 697) {
/*  3373 */         actString = "pull";
/*  3374 */       }  performer.getCommunicator().sendNormalServerMessage("You start to " + actString + " the " + item.getName() + ".");
/*  3375 */       Server.getInstance().broadCastAction(performer
/*  3376 */           .getName() + " starts to " + actString + " the " + item.getName() + ".", performer, 5);
/*  3377 */       if (!insta)
/*       */       {
/*  3379 */         performer.sendActionControl("Moving " + item.getName(), true, time);
/*       */         
/*  3381 */         performer.getStatus().modifyStamina(-200.0F);
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/*  3386 */       time = act.getTimeLeft();
/*       */     } 
/*  3388 */     if (counter * 10.0F > time || insta) {
/*       */       
/*  3390 */       if (action == 99 || action == 181 || action == 696 || action == 697 || action == 864)
/*       */       {
/*  3392 */         SoundPlayer.playSound("sound.object.move.pushpull", item, 0.0F);
/*       */       }
/*  3394 */       performer.getStatus().modifyStamina(-250.0F);
/*  3395 */       String actString = "turn";
/*  3396 */       if (action == 864)
/*  3397 */         actString = "move"; 
/*  3398 */       if (action == 99 || action == 696) {
/*  3399 */         actString = "push";
/*  3400 */       } else if (action == 181 || action == 697) {
/*  3401 */         actString = "pull";
/*       */       } 
/*       */       try {
/*  3404 */         float dir = item.getRotation();
/*  3405 */         if (action == 177 || action == 178) {
/*       */           
/*  3407 */           float mod = item.isFence() ? 90.0F : 22.5F;
/*       */           
/*  3409 */           if (action == 177) {
/*  3410 */             dir += mod;
/*  3411 */           } else if (action == 178) {
/*  3412 */             dir -= mod;
/*  3413 */           }  if (dir < 0.0F) {
/*  3414 */             dir = 360.0F + dir;
/*  3415 */           } else if (dir > 360.0F) {
/*  3416 */             dir -= 360.0F;
/*  3417 */           }  if (item.isFence()) {
/*       */             
/*  3419 */             VolaTile next2 = Zones.getOrCreateTile(item.getTileX(), item.getTileY(), item
/*  3420 */                 .isOnSurface());
/*  3421 */             if (next2 != null)
/*       */             {
/*  3423 */               for (Creature c : next2.getCreatures()) {
/*       */                 
/*  3425 */                 if (!c.isFriendlyKingdom(performer.getKingdomId()) || (performer
/*  3426 */                   .getCitizenVillage() != null && performer.getCitizenVillage().isEnemy(c))) {
/*       */                   
/*  3428 */                   performer.getCommunicator().sendNormalServerMessage("There are enemies blocking your turning.", (byte)3);
/*       */                   
/*  3430 */                   return true;
/*       */                 } 
/*       */               } 
/*       */             }
/*  3434 */             int offz = 0;
/*       */             
/*       */             try {
/*  3437 */               offz = (int)((item.getPosZ() - Zones.calculateHeight(item.getPosX(), item.getPosY(), item
/*  3438 */                   .isOnSurface())) / 10.0F);
/*       */             }
/*  3440 */             catch (NoSuchZoneException nsz) {
/*       */               
/*  3442 */               logger.log(Level.WARNING, "Rotating fence item outside zones.");
/*       */             } 
/*  3444 */             float rot = Creature.normalizeAngle(item.getRotation());
/*  3445 */             if (rot >= 45.0F && rot < 135.0F) {
/*       */               
/*  3447 */               VolaTile next1 = Zones.getOrCreateTile(item.getTileX() + 1, item.getTileY(), item
/*  3448 */                   .isOnSurface());
/*  3449 */               if (next1 != null && (next1.getCreatures()).length > 0) {
/*       */                 
/*  3451 */                 performer.getCommunicator().sendNormalServerMessage("There are creatures blocking your turning.", (byte)3);
/*       */                 
/*  3453 */                 return true;
/*       */               } 
/*       */               
/*  3456 */               VolaTile next = Zones.getOrCreateTile(item.getTileX() + 1, item.getTileY(), true);
/*       */               
/*  3458 */               next.removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX() + 1, item.getTileY(), offz, item, Tiles.TileBorderDirection.DIR_DOWN, next
/*  3459 */                     .getZone().getId(), next.getLayer()));
/*       */             }
/*  3461 */             else if (rot >= 135.0F && rot < 225.0F) {
/*       */               
/*  3463 */               VolaTile next1 = Zones.getOrCreateTile(item.getTileX(), item.getTileY() + 1, item
/*  3464 */                   .isOnSurface());
/*  3465 */               if (next1 != null && (next1.getCreatures()).length > 0) {
/*       */                 
/*  3467 */                 performer.getCommunicator().sendNormalServerMessage("There are creatures blocking your turning.", (byte)3);
/*       */                 
/*  3469 */                 return true;
/*       */               } 
/*  3471 */               VolaTile next = Zones.getOrCreateTile(item.getTileX(), item.getTileY() + 1, true);
/*       */               
/*  3473 */               next.removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX(), item.getTileY() + 1, offz, item, Tiles.TileBorderDirection.DIR_HORIZ, next
/*  3474 */                     .getZone().getId(), next.getLayer()));
/*       */             }
/*  3476 */             else if (rot >= 225.0F && rot < 315.0F) {
/*       */ 
/*       */               
/*  3479 */               VolaTile next1 = Zones.getOrCreateTile(item.getTileX() - 1, item.getTileY(), item
/*  3480 */                   .isOnSurface());
/*  3481 */               if (next1 != null && (next1.getCreatures()).length > 0) {
/*       */                 
/*  3483 */                 performer.getCommunicator().sendNormalServerMessage("There are creatures blocking your turning.", (byte)3);
/*       */                 
/*  3485 */                 return true;
/*       */               } 
/*  3487 */               VolaTile next = Zones.getOrCreateTile(item.getTileX(), item.getTileY(), true);
/*  3488 */               next.removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX(), item.getTileY(), offz, item, Tiles.TileBorderDirection.DIR_DOWN, next
/*       */                     
/*  3490 */                     .getZone().getId(), next.getLayer()));
/*       */             }
/*       */             else {
/*       */               
/*  3494 */               VolaTile next1 = Zones.getOrCreateTile(item.getTileX(), item.getTileY() - 1, item
/*  3495 */                   .isOnSurface());
/*  3496 */               if (next1 != null && (next1.getCreatures()).length > 0) {
/*       */                 
/*  3498 */                 performer.getCommunicator().sendNormalServerMessage("There are creatures blocking your turning.", (byte)3);
/*       */                 
/*  3500 */                 return true;
/*       */               } 
/*  3502 */               VolaTile next = Zones.getOrCreateTile(item.getTileX(), item.getTileY(), true);
/*  3503 */               next.removeFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX(), item.getTileY(), offz, item, Tiles.TileBorderDirection.DIR_HORIZ, next
/*       */                     
/*  3505 */                     .getZone().getId(), next.getLayer()));
/*       */             } 
/*       */           } 
/*  3508 */           item.setRotation(dir);
/*  3509 */           if (item.isFence())
/*       */           {
/*  3511 */             if (item.isOnSurface()) {
/*       */               
/*  3513 */               int offz = 0;
/*       */               
/*       */               try {
/*  3516 */                 offz = (int)((item.getPosZ() - Zones.calculateHeight(item.getPosX(), item.getPosY(), item
/*  3517 */                     .isOnSurface())) / 10.0F);
/*       */               }
/*  3519 */               catch (NoSuchZoneException nsz) {
/*       */                 
/*  3521 */                 logger.log(Level.WARNING, "Dropping fence item outside zones.");
/*       */               } 
/*  3523 */               float rot = Creature.normalizeAngle(item.getRotation());
/*  3524 */               if (rot >= 45.0F && rot < 135.0F) {
/*       */                 
/*  3526 */                 VolaTile next = Zones.getOrCreateTile(item.getTileX() + 1, item.getTileY(), item
/*  3527 */                     .isOnSurface());
/*  3528 */                 next.addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX() + 1, item.getTileY(), offz, item, Tiles.TileBorderDirection.DIR_DOWN, next
/*  3529 */                       .getZone().getId(), next.getLayer()));
/*       */               }
/*  3531 */               else if (rot >= 135.0F && rot < 225.0F) {
/*       */                 
/*  3533 */                 VolaTile next = Zones.getOrCreateTile(item.getTileX(), item.getTileY() + 1, item
/*  3534 */                     .isOnSurface());
/*  3535 */                 next.addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX(), item.getTileY() + 1, offz, item, Tiles.TileBorderDirection.DIR_HORIZ, next
/*  3536 */                       .getZone().getId(), next.getLayer()));
/*       */               }
/*  3538 */               else if (rot >= 225.0F && rot < 315.0F) {
/*       */                 
/*  3540 */                 VolaTile next = Zones.getOrCreateTile(item.getTileX(), item.getTileY(), item
/*  3541 */                     .isOnSurface());
/*  3542 */                 next.addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX(), item.getTileY(), offz, item, Tiles.TileBorderDirection.DIR_DOWN, next
/*       */ 
/*       */                       
/*  3545 */                       .getZone().getId(), next.getLayer()));
/*       */               }
/*       */               else {
/*       */                 
/*  3549 */                 VolaTile next = Zones.getOrCreateTile(item.getTileX(), item.getTileY(), item
/*  3550 */                     .isOnSurface());
/*  3551 */                 next.addFence((Fence)new TempFence(StructureConstantsEnum.FENCE_SIEGEWALL, item.getTileX(), item.getTileY(), offz, item, Tiles.TileBorderDirection.DIR_HORIZ, next
/*       */ 
/*       */                       
/*  3554 */                       .getZone().getId(), next.getLayer()));
/*       */               } 
/*       */             } 
/*       */           }
/*  3558 */           Zone zone = Zones.getZone((int)item.getPosX() >> 2, (int)item.getPosY() >> 2, item.isOnSurface());
/*  3559 */           if (performer.getVisionArea() != null)
/*  3560 */             performer.getVisionArea().broadCastUpdateSelectBar(item.getWurmId(), true); 
/*  3561 */           if (item.isGuardTower())
/*       */           {
/*       */ 
/*       */ 
/*       */             
/*  3566 */             VolaTile tile = zone.getOrCreateTile(item.getTileX(), item.getTileY());
/*  3567 */             tile.makeInvisible(item);
/*  3568 */             tile.makeVisible(item);
/*       */           }
/*       */           else
/*       */           {
/*  3572 */             Item parent = item.getParentOrNull();
/*  3573 */             if (parent != null && parent.getTemplate().hasViewableSubItems() && (
/*  3574 */               !parent.getTemplate().isContainerWithSubItems() || item.isPlacedOnParent())) {
/*       */               
/*  3576 */               VolaTile vt = Zones.getTileOrNull(parent.getTileX(), parent.getTileY(), parent.isOnSurface());
/*  3577 */               if (vt != null)
/*       */               {
/*  3579 */                 for (VirtualZone vz : vt.getWatchers()) {
/*       */                   
/*  3581 */                   if (vz.isVisible(parent, vt))
/*       */                   {
/*       */                     
/*  3584 */                     vz.getWatcher().getCommunicator().sendItem(item, -10L, false);
/*       */                   }
/*       */                 } 
/*       */               }
/*       */             } else {
/*       */               
/*  3590 */               zone.removeItem(item, true, true);
/*  3591 */               zone.addItem(item, true, false, false);
/*       */             }
/*       */           
/*       */           }
/*       */         
/*       */         }
/*  3597 */         else if (item.isMoveable(performer)) {
/*       */           
/*  3599 */           float iposx = item.getPosX();
/*  3600 */           float iposy = item.getPosY();
/*  3601 */           int ix = item.getTileX();
/*  3602 */           int iy = item.getTileY();
/*  3603 */           long bridgeId = item.getBridgeId();
/*  3604 */           if (action == 181 || action == 697) {
/*       */             
/*  3606 */             double rotRads = Math.atan2((performer.getStatus().getPositionY() - iposy), (performer
/*  3607 */                 .getStatus().getPositionX() - iposx));
/*  3608 */             float rot = (float)(rotRads * 57.29577951308232D) + 90.0F;
/*  3609 */             float length = (item.getTemplateId() == 938 || item.getTemplateId() == 931) ? 4.0F : ((action == 181) ? 0.2F : 0.04F);
/*       */             
/*  3611 */             float xPosMod = (float)Math.sin((rot * 0.017453292F)) * length;
/*  3612 */             float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * length;
/*  3613 */             float newPosX = iposx + xPosMod;
/*  3614 */             float newPosY = iposy + yPosMod;
/*  3615 */             int placedX = (int)newPosX >> 2;
/*  3616 */             int placedY = (int)newPosY >> 2;
/*  3617 */             boolean surf = item.isOnSurface();
/*  3618 */             if (surf && item.isSurfaceOnly() && Terraforming.isCaveEntrance(Zones.getTextureForTile(placedX, placedY, 0))) {
/*       */               
/*  3620 */               performer.getCommunicator().sendNormalServerMessage("You cannot pull the " + item
/*  3621 */                   .getName() + " into a cave.", (byte)3);
/*  3622 */               return true;
/*       */             } 
/*  3624 */             BlockingResult result = Blocking.getBlockerBetween(performer, iposx, iposy, newPosX, newPosY, performer
/*  3625 */                 .getPositionZ(), item.getPosZ(), performer.isOnSurface(), item.isOnSurface(), false, 6, -1L, performer
/*  3626 */                 .getBridgeId(), 
/*  3627 */                 (Math.abs(performer.getPositionZ() - item.getPosZ()) < 2.0F) ? performer.getBridgeId() : item
/*  3628 */                 .getBridgeId(), false);
/*       */             
/*  3630 */             if (result != null) {
/*       */               
/*  3632 */               performer.getCommunicator().sendNormalServerMessage("You cannot pull the " + item
/*  3633 */                   .getName() + " into the wall.", (byte)3);
/*  3634 */               return true;
/*       */             } 
/*       */ 
/*       */             
/*  3638 */             boolean changingTile = (placedX != ix || placedY != iy);
/*  3639 */             if (changingTile) {
/*       */               
/*  3641 */               if (item.isOnePerTile() || item.isFourPerTile()) {
/*       */                 
/*  3643 */                 VolaTile t = Zones.getTileOrNull(placedX, placedY, performer.isOnSurface());
/*  3644 */                 if (t != null)
/*       */                 {
/*  3646 */                   if ((item.isOnePerTile() && t.hasOnePerTileItem(performer.getFloorLevel())) || (item
/*  3647 */                     .isFourPerTile() && t.getFourPerTileCount(performer.getFloorLevel()) >= 4)) {
/*       */                     
/*  3649 */                     performer.getCommunicator().sendNormalServerMessage("You cannot move that item here, since there is not enough space.", (byte)3);
/*       */ 
/*       */                     
/*  3652 */                     return true;
/*       */                   } 
/*       */                 }
/*       */               } 
/*  3656 */               if (item.getTemplateId() == 1309) {
/*       */                 
/*       */                 try {
/*       */ 
/*       */ 
/*       */                   
/*  3662 */                   Item waystone = Items.getItem(item.getData());
/*  3663 */                   int dx = Math.abs(placedX - waystone.getTileX());
/*  3664 */                   int dy = Math.abs(placedY - waystone.getTileY());
/*  3665 */                   if (dx > 1 || dy > 1)
/*       */                   {
/*  3667 */                     performer.getCommunicator().sendNormalServerMessage("You cannot move that item here, as it would be too far away from its associated waystone.", (byte)3);
/*       */ 
/*       */                     
/*  3670 */                     return true;
/*       */                   }
/*       */                 
/*  3673 */                 } catch (NoSuchItemException e) {
/*       */ 
/*       */                   
/*  3676 */                   logger.log(Level.WARNING, "Associated waystone missing! " + e.getMessage(), (Throwable)e);
/*  3677 */                   performer.getCommunicator().sendNormalServerMessage("You cannot move that item here, as its associated waystone is missing.", (byte)3);
/*       */ 
/*       */                   
/*  3680 */                   return true;
/*       */                 } 
/*       */               }
/*       */             } 
/*       */             
/*  3685 */             if (item.isDecoration()) {
/*       */               
/*  3687 */               VolaTile t = Zones.getTileOrNull(placedX, placedY, performer.isOnSurface());
/*  3688 */               if (t != null)
/*       */               {
/*  3690 */                 if (t.getStructure() != null)
/*       */                 {
/*  3692 */                   if (t.getNumberOfDecorations(t.getDropFloorLevel(performer.getFloorLevel())) > 14) {
/*       */                     
/*  3694 */                     performer.getCommunicator().sendNormalServerMessage("That place is too littered with decorations already.", (byte)3);
/*       */ 
/*       */                     
/*  3697 */                     return true;
/*       */                   } 
/*       */                 }
/*       */               }
/*       */             } 
/*  3702 */             Zone zone = Zones.getZone(item.getTileX(), item.getTileY(), item.isOnSurface());
/*       */             
/*  3704 */             if (performer.getVisionArea() != null)
/*  3705 */               performer.getVisionArea().broadCastUpdateSelectBar(item.getWurmId(), true); 
/*  3706 */             zone.removeItem(item, true, true);
/*  3707 */             item.setPosXY(newPosX, newPosY);
/*  3708 */             if (changingTile && placedX == performer.getTileX() && placedY == performer.getTileY())
/*  3709 */               item.setOnBridge(performer.getBridgeId()); 
/*  3710 */             Zone z = Zones.getZone((int)newPosX >> 2, (int)newPosY >> 2, performer.isOnSurface());
/*  3711 */             z.addItem(item, true, false, false);
/*  3712 */             if (surf != item.isOnSurface()) {
/*  3713 */               z = Zones.getZone((int)newPosX >> 2, (int)newPosY >> 2, item.isOnSurface());
/*       */             }
/*  3715 */             Effect[] effects = item.getEffects();
/*  3716 */             if (effects != null)
/*       */             {
/*  3718 */               for (Effect lEffect : effects) {
/*       */                 
/*  3720 */                 zone.removeEffect(lEffect);
/*  3721 */                 lEffect.setPosX(newPosX);
/*  3722 */                 lEffect.setPosY(newPosY);
/*  3723 */                 z.addEffect(lEffect, false);
/*       */               } 
/*       */             }
/*       */           } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  3749 */           if (action == 864)
/*       */           {
/*  3751 */             Zone zone = Zones.getZone(item.getTileX(), item.getTileY(), item.isOnSurface());
/*  3752 */             zone.removeItem(item, true, true);
/*  3753 */             item.setPosXY((((int)item.getPosX() >> 2) * 4 + 2), (((int)item.getPosY() >> 2) * 4 + 2));
/*  3754 */             zone.addItem(item, true, false, false);
/*       */           }
/*  3756 */           else if (action == 99 || action == 696)
/*       */           {
/*       */ 
/*       */ 
/*       */             
/*  3761 */             float rot = (item.getTemplateId() == 931) ? item.getRotation() : performer.getStatus().getRotation();
/*  3762 */             float length = (item.getTemplateId() == 938 || item.getTemplateId() == 931) ? 4.0F : ((action == 99) ? 0.2F : 0.04F);
/*       */             
/*  3764 */             float xPosMod = (float)Math.sin((rot * 0.017453292F)) * length;
/*  3765 */             float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * length;
/*  3766 */             float newPosX = iposx + xPosMod;
/*  3767 */             float newPosY = iposy + yPosMod;
/*  3768 */             int placedX = (int)newPosX >> 2;
/*  3769 */             int placedY = (int)newPosY >> 2;
/*  3770 */             boolean surf = item.isOnSurface();
/*  3771 */             if (surf && item.isSurfaceOnly() && Terraforming.isCaveEntrance(Zones.getTextureForTile(placedX, placedY, 0))) {
/*       */               
/*  3773 */               performer.getCommunicator().sendNormalServerMessage("You cannot push the " + item
/*  3774 */                   .getName() + " into a cave.", (byte)3);
/*  3775 */               return true;
/*       */             } 
/*       */             
/*  3778 */             BlockingResult result = Blocking.getBlockerBetween(performer, iposx, iposy, newPosX, newPosY, performer
/*  3779 */                 .getPositionZ(), item.getPosZ(), performer.isOnSurface(), item.isOnSurface(), false, 6, -1L, performer
/*  3780 */                 .getBridgeId(), 
/*  3781 */                 (Math.abs(performer.getPositionZ() - item.getPosZ()) < 2.0F) ? performer.getBridgeId() : item
/*  3782 */                 .getBridgeId(), false);
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  3787 */             if (result != null) {
/*       */               
/*  3789 */               boolean skip = false;
/*  3790 */               if (item.getTemplateId() == 931)
/*       */               {
/*       */                 
/*  3793 */                 if ((result.getBlockerArray()).length < 2)
/*       */                 {
/*  3795 */                   for (Blocker b : result.getBlockerArray()) {
/*       */                     
/*  3797 */                     if (b.getTempId() == item.getWurmId())
/*       */                     {
/*  3799 */                       skip = true;
/*       */                     }
/*       */                   } 
/*       */                 }
/*       */               }
/*  3804 */               if (!skip) {
/*       */                 
/*  3806 */                 performer.getCommunicator().sendNormalServerMessage("You cannot push the " + item
/*  3807 */                     .getName() + " into the wall.", (byte)3);
/*  3808 */                 return true;
/*       */               } 
/*       */             } 
/*  3811 */             boolean changingTile = (placedX != ix || placedY != iy);
/*  3812 */             if (changingTile) {
/*       */               
/*  3814 */               if (item.isOnePerTile() || item.isFourPerTile()) {
/*       */                 
/*  3816 */                 VolaTile t = Zones.getTileOrNull(placedX, placedY, performer.isOnSurface());
/*  3817 */                 if (t != null) {
/*       */                   
/*  3819 */                   if ((item.isOnePerTile() && t.hasOnePerTileItem(performer.getFloorLevel())) || (item
/*  3820 */                     .isFourPerTile() && t.getFourPerTileCount(performer.getFloorLevel()) >= 4)) {
/*       */                     
/*  3822 */                     performer.getCommunicator().sendNormalServerMessage("You cannot move that item here, since there is not enough space.", (byte)3);
/*       */ 
/*       */                     
/*  3825 */                     return true;
/*       */                   } 
/*  3827 */                   if (item.isFence())
/*       */                   {
/*  3829 */                     if ((t.getCreatures()).length > 0) {
/*       */                       
/*  3831 */                       performer.getCommunicator().sendNormalServerMessage("You cannot move that item here, since the creatures block you.", (byte)3);
/*       */ 
/*       */                       
/*  3834 */                       return true;
/*       */                     } 
/*       */                   }
/*       */                 } 
/*       */               } 
/*  3839 */               if (item.getTemplateId() == 1309) {
/*       */                 
/*       */                 try {
/*       */ 
/*       */ 
/*       */                   
/*  3845 */                   Item waystone = Items.getItem(item.getData());
/*  3846 */                   int dx = Math.abs(placedX - waystone.getTileX());
/*  3847 */                   int dy = Math.abs(placedY - waystone.getTileY());
/*  3848 */                   if (dx > 1 || dy > 1)
/*       */                   {
/*  3850 */                     performer.getCommunicator().sendNormalServerMessage("You cannot move that item there, as it would be too far away from its associated waystone.", (byte)3);
/*       */ 
/*       */                     
/*  3853 */                     return true;
/*       */                   }
/*       */                 
/*  3856 */                 } catch (NoSuchItemException e) {
/*       */ 
/*       */                   
/*  3859 */                   logger.log(Level.WARNING, "Associated waystone missing! " + e.getMessage(), (Throwable)e);
/*  3860 */                   performer.getCommunicator().sendNormalServerMessage("You cannot move that item there, as its associated waystone is missing.", (byte)3);
/*       */ 
/*       */                   
/*  3863 */                   return true;
/*       */                 } 
/*       */               }
/*       */               
/*  3867 */               if (bridgeId == -10L) {
/*       */                 
/*  3869 */                 int floorLevel = item.getFloorLevel();
/*  3870 */                 BridgePart bridgePart = Zones.getBridgePartFor(placedX, placedY, item.isOnSurface());
/*  3871 */                 if (bridgePart != null && bridgePart.hasAnExit())
/*       */                 {
/*       */                   
/*  3874 */                   if (placedY < iy) {
/*       */ 
/*       */                     
/*  3877 */                     if (bridgePart.hasSouthExit() && bridgePart.getSouthExitFloorLevel() == floorLevel) {
/*  3878 */                       bridgeId = bridgePart.getStructureId();
/*       */                     }
/*  3880 */                   } else if (placedX > ix) {
/*       */ 
/*       */                     
/*  3883 */                     if (bridgePart.hasWestExit() && bridgePart.getWestExitFloorLevel() == floorLevel) {
/*  3884 */                       bridgeId = bridgePart.getStructureId();
/*       */                     }
/*  3886 */                   } else if (placedY > iy) {
/*       */ 
/*       */                     
/*  3889 */                     if (bridgePart.hasNorthExit() && bridgePart.getNorthExitFloorLevel() == floorLevel) {
/*  3890 */                       bridgeId = bridgePart.getStructureId();
/*       */                     }
/*  3892 */                   } else if (placedX < ix) {
/*       */ 
/*       */                     
/*  3895 */                     if (bridgePart.hasEastExit() && bridgePart.getEastExitFloorLevel() == floorLevel) {
/*  3896 */                       bridgeId = bridgePart.getStructureId();
/*       */                     }
/*       */                   }
/*       */                 
/*       */                 }
/*       */               }
/*       */               else {
/*       */                 
/*  3904 */                 BridgePart newBridgePart = Zones.getBridgePartFor(placedX, placedY, item.isOnSurface());
/*  3905 */                 if (newBridgePart == null) {
/*       */                   
/*  3907 */                   BridgePart bridgePart = Zones.getBridgePartFor(ix, iy, item.isOnSurface());
/*  3908 */                   if (bridgePart == null || bridgePart.getStructureId() != bridgeId) {
/*       */ 
/*       */                     
/*  3911 */                     performer.getCommunicator().sendNormalServerMessage("Error: Item is on a bridge, but it isnt!.");
/*       */                     
/*  3913 */                     return true;
/*       */                   } 
/*       */ 
/*       */ 
/*       */                   
/*  3918 */                   int newFloorLevel = -1;
/*       */                   
/*  3920 */                   if (placedY < iy) {
/*       */ 
/*       */                     
/*  3923 */                     if (bridgePart.hasNorthExit()) {
/*  3924 */                       newFloorLevel = bridgePart.getNorthExitFloorLevel();
/*       */                     }
/*  3926 */                   } else if (placedX > ix) {
/*       */ 
/*       */                     
/*  3929 */                     if (bridgePart.hasEastExit()) {
/*  3930 */                       newFloorLevel = bridgePart.getEastExitFloorLevel();
/*       */                     }
/*  3932 */                   } else if (placedY > iy) {
/*       */ 
/*       */                     
/*  3935 */                     if (bridgePart.hasSouthExit()) {
/*  3936 */                       newFloorLevel = bridgePart.getSouthExitFloorLevel();
/*       */                     }
/*  3938 */                   } else if (placedX < ix) {
/*       */ 
/*       */                     
/*  3941 */                     if (bridgePart.hasWestExit())
/*  3942 */                       newFloorLevel = bridgePart.getWestExitFloorLevel(); 
/*       */                   } 
/*  3944 */                   if (newFloorLevel == -1) {
/*       */                     
/*  3946 */                     performer.getCommunicator().sendNormalServerMessage("Cannot find the floor level off the end of this bridge.");
/*       */                     
/*  3948 */                     return true;
/*       */                   } 
/*       */ 
/*       */                   
/*  3952 */                   bridgeId = -10L;
/*       */                 } 
/*       */               } 
/*       */             } 
/*  3956 */             if (item.isDecoration()) {
/*       */               
/*  3958 */               VolaTile t = Zones.getTileOrNull(placedX, placedY, performer.isOnSurface());
/*  3959 */               if (t != null)
/*       */               {
/*  3961 */                 if (t.getStructure() != null)
/*       */                 {
/*  3963 */                   if (t.getNumberOfDecorations(t.getDropFloorLevel(performer.getFloorLevel())) > 14) {
/*       */                     
/*  3965 */                     performer.getCommunicator().sendNormalServerMessage("That place is too littered with decorations already.", (byte)3);
/*       */ 
/*       */                     
/*  3968 */                     return true;
/*       */                   } 
/*       */                 }
/*       */               }
/*       */             } 
/*       */             
/*  3974 */             if (item.getTemplateId() == 1311) {
/*       */               
/*  3976 */               VolaTile t = Zones.getTileOrNull(placedX, placedY, performer.isOnSurface());
/*  3977 */               if (t != null)
/*       */               {
/*  3979 */                 if (t.getFourPerTileCount(performer.getFloorLevel()) >= 4) {
/*       */                   
/*  3981 */                   performer.getCommunicator().sendNormalServerMessage("You cannot move this item, there isn't enough room.");
/*       */                   
/*  3983 */                   return true;
/*       */                 } 
/*       */               }
/*       */             } 
/*       */             
/*  3988 */             Zone zone = Zones.getZone(item.getTileX(), item.getTileY(), item.isOnSurface());
/*  3989 */             if (item.getTemplateId() == 931)
/*       */             {
/*  3991 */               VolaTile t = zone.getTileOrNull(item.getTileX(), item.getTileY());
/*  3992 */               float newPosZ = Zones.calculatePosZ(newPosX, newPosY, t, performer.isOnSurface(), false, item
/*  3993 */                   .getPosZ(), performer, -10L);
/*  3994 */               if (newPosZ < -1.0F && item.getPosZ() >= -1.0F) {
/*       */                 
/*  3996 */                 performer.getCommunicator().sendNormalServerMessage("That place is too deep. The " + item
/*  3997 */                     .getName() + " would get stuck.", (byte)3);
/*       */                 
/*  3999 */                 return true;
/*       */               } 
/*  4001 */               t.moveItem(item, newPosX, newPosY, newPosZ, item.getRotation(), performer.isOnSurface(), item
/*  4002 */                   .getPosZ());
/*       */             }
/*       */             else
/*       */             {
/*  4006 */               zone.removeItem(item, true, true);
/*  4007 */               item.setPosXY(newPosX, newPosY);
/*  4008 */               item.setOnBridge(bridgeId);
/*  4009 */               Zone z = Zones.getZone((int)newPosX >> 2, (int)newPosY >> 2, performer.isOnSurface());
/*  4010 */               z.addItem(item, true, false, false);
/*  4011 */               if (surf != item.isOnSurface())
/*  4012 */                 z = Zones.getZone((int)newPosX >> 2, (int)newPosY >> 2, item.isOnSurface()); 
/*  4013 */               Effect[] effects = item.getEffects();
/*  4014 */               if (effects != null) {
/*  4015 */                 for (Effect lEffect : effects)
/*       */                 {
/*  4017 */                   zone.removeEffect(lEffect);
/*  4018 */                   lEffect.setPosX(newPosX);
/*  4019 */                   lEffect.setPosY(newPosY);
/*  4020 */                   z.addEffect(lEffect, false);
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*       */                 }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*       */               }
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*       */             }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*       */           }
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*       */         }
/*       */         else {
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  4053 */           performer.getCommunicator().sendNormalServerMessage("The " + item
/*  4054 */               .getName() + " won't budge. It is stuck.", (byte)3);
/*  4055 */           return true;
/*       */         } 
/*       */         
/*  4058 */         performer.getCommunicator().sendNormalServerMessage("You " + actString + " the " + item.getName() + " a bit.");
/*  4059 */         if (action == 99 || action == 696)
/*  4060 */           actString = actString + "e"; 
/*  4061 */         Server.getInstance().broadCastAction(performer
/*  4062 */             .getName() + " " + actString + "s the " + item.getName() + " a bit.", performer, 5);
/*       */       }
/*  4064 */       catch (NoSuchZoneException nsz) {
/*       */         
/*  4066 */         performer.getCommunicator().sendNormalServerMessage("You fail to " + actString + " the " + item
/*  4067 */             .getName() + ". It must be stuck.");
/*  4068 */         logger.log(Level.WARNING, performer.getName() + ": " + nsz.getMessage(), (Throwable)nsz);
/*       */       } 
/*  4070 */       toReturn = true;
/*       */     } 
/*  4072 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean lock(Creature performer, Item lock, Item target, float counter, boolean replacing) {
/*  4078 */     if (lock.isLocked()) {
/*       */       
/*  4080 */       performer.getCommunicator().sendNormalServerMessage("The " + lock.getName() + " is already in use.", (byte)3);
/*       */       
/*  4082 */       return true;
/*       */     } 
/*  4084 */     if ((target.getLockId() == -10L && !replacing) || (target
/*  4085 */       .getLockId() != -10L && replacing)) {
/*       */ 
/*       */       
/*  4088 */       if (!target.isOwner(performer))
/*       */       {
/*  4090 */         if (!Methods.isActionAllowed(performer, (short)161))
/*       */         {
/*       */ 
/*       */           
/*  4094 */           return true;
/*       */         }
/*       */       }
/*       */       
/*  4098 */       if (target.isBoat()) {
/*       */         
/*  4100 */         if (!lock.isBoatLock()) {
/*       */           
/*  4102 */           performer.getCommunicator().sendNormalServerMessage("You need to lock the " + target
/*  4103 */               .getName() + " with a boat lock.", (byte)3);
/*  4104 */           return true;
/*       */         } 
/*  4106 */         if (performer.getWurmId() != target.getLastOwnerId())
/*       */         {
/*  4108 */           performer.getCommunicator().sendNormalServerMessage("You do not have the right to lock this boat. Are you really the captain?", (byte)3);
/*       */           
/*  4110 */           return true;
/*       */         
/*       */         }
/*       */       
/*       */       }
/*  4115 */       else if (!lock.mayLockItems()) {
/*       */         
/*  4117 */         performer.getCommunicator().sendNormalServerMessage("You can't lock the " + target
/*  4118 */             .getName() + " with that.", (byte)3);
/*  4119 */         return true;
/*       */       } 
/*       */       
/*  4122 */       if (!performer.hasAllKeysForLock(lock)) {
/*  4123 */         performer.getCommunicator().sendAlertServerMessage("Security Warning: You do not have all the keys for that lock!", (byte)2);
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  4132 */       long oldLockId = target.getLockId();
/*  4133 */       if (oldLockId != -10L) {
/*       */         
/*       */         try {
/*       */           
/*  4137 */           Item oldLock = Items.getItem(oldLockId);
/*  4138 */           oldLock.setLocked(false);
/*  4139 */           performer.getInventory().insertItem(oldLock);
/*       */         }
/*  4141 */         catch (NoSuchItemException e) {
/*       */ 
/*       */           
/*  4144 */           logger.log(Level.WARNING, "Old lock was not found: " + e.getMessage(), (Throwable)e);
/*       */         } 
/*       */       }
/*  4147 */       lock.putInVoid();
/*  4148 */       performer.getCommunicator().sendNormalServerMessage("You lock the " + target
/*  4149 */           .getName() + " with the " + lock.getName() + ".");
/*  4150 */       Server.getInstance().broadCastAction(performer
/*  4151 */           .getName() + " locks the " + target.getName() + " with a " + lock.getName() + ".", performer, 5);
/*  4152 */       target.setLockId(lock.getWurmId());
/*  4153 */       int tilex = target.getTileX();
/*  4154 */       int tiley = target.getTileY();
/*  4155 */       SoundPlayer.playSound("sound.object.lockunlock", tilex, tiley, performer.isOnSurface(), 1.0F);
/*       */       
/*  4157 */       lock.setLocked(true);
/*  4158 */       PermissionsHistories.addHistoryEntry(target.getWurmId(), System.currentTimeMillis(), performer
/*  4159 */           .getWurmId(), performer.getName(), replacing ? "Replaced Lock" : "Attached Lock");
/*       */       
/*  4161 */       return true;
/*       */     } 
/*       */     
/*  4164 */     performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already locked.", (byte)3);
/*       */     
/*  4166 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean unlock(Creature performer, Item key, Item target, float counter) {
/*  4171 */     long lockId = target.getLockId();
/*  4172 */     if (lockId == -10L) {
/*       */       
/*  4174 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is not locked.", (byte)3);
/*       */       
/*  4176 */       return false;
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/*       */     try {
/*  4182 */       Item lock = Items.getItem(lockId);
/*  4183 */       long keyId = -10L;
/*  4184 */       if (key != null)
/*  4185 */         keyId = key.getWurmId(); 
/*  4186 */       long[] keys = lock.getKeyIds();
/*  4187 */       int tilex = target.getTileX();
/*  4188 */       int tiley = target.getTileY();
/*  4189 */       SoundPlayer.playSound("sound.object.lockunlock", tilex, tiley, performer.isOnSurface(), 1.0F);
/*  4190 */       boolean hasKey = (performer.hasKeyForLock(lock) || target.isOwner(performer));
/*  4191 */       boolean foundKey = hasKey;
/*  4192 */       if (!foundKey)
/*       */       {
/*  4194 */         for (long lKey : keys) {
/*       */           
/*  4196 */           if (lKey == keyId) {
/*       */             
/*  4198 */             foundKey = true;
/*       */             break;
/*       */           } 
/*       */         } 
/*       */       }
/*  4203 */       if (foundKey) {
/*       */         
/*  4205 */         performer.getCommunicator().sendNormalServerMessage("You unlock the " + target.getName() + ".");
/*  4206 */         Server.getInstance().broadCastAction(performer.getName() + " unlocks the " + target.getName() + ".", performer, 5);
/*       */         
/*  4208 */         target.setLockId(-10L);
/*  4209 */         lock.setLocked(false);
/*  4210 */         ItemSettings.remove(target.getWurmId());
/*  4211 */         PermissionsHistories.addHistoryEntry(target.getWurmId(), System.currentTimeMillis(), performer
/*  4212 */             .getWurmId(), performer.getName(), "Removed Lock");
/*  4213 */         performer.getInventory().insertItem(lock, true);
/*  4214 */         return true;
/*       */       } 
/*       */       
/*  4217 */       performer.getCommunicator().sendNormalServerMessage("The key does not fit.", (byte)3);
/*       */     }
/*  4219 */     catch (NoSuchItemException nsi) {
/*       */       
/*  4221 */       logger.log(Level.WARNING, "No such lock, but it should be locked." + nsi.getMessage(), (Throwable)nsi);
/*  4222 */       return true;
/*       */     } 
/*  4224 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static final void checkLockpickBreakage(Creature performer, Item lockpick, int breakBonus, double power) {
/*  4231 */     breakBonus += lockpick.getRarity() * 20;
/*  4232 */     if (power > 0.0D) {
/*       */       
/*  4234 */       if (Server.rand.nextInt(85 + breakBonus) <= (100.0F - lockpick.getCurrentQualityLevel()) / 5.0F) {
/*       */         
/*  4236 */         performer.getCommunicator().sendNormalServerMessage("The lockpick breaks.", (byte)3);
/*  4237 */         SoundPlayer.playSound("sound.object.lockpick.break.ogg", performer, 1.0F);
/*  4238 */         Items.destroyItem(lockpick.getWurmId());
/*       */       } else {
/*       */         
/*  4241 */         lockpick.setDamage(lockpick.getDamage() + (0.25F - lockpick.getRarity() * 0.05F) * lockpick
/*  4242 */             .getDamageModifier());
/*       */       }
/*       */     
/*       */     }
/*  4246 */     else if (Server.rand.nextInt(65 + breakBonus) <= (100.0F - lockpick.getCurrentQualityLevel()) / 5.0F) {
/*       */       
/*  4248 */       performer.getCommunicator().sendNormalServerMessage("The lockpick breaks.", (byte)3);
/*  4249 */       SoundPlayer.playSound("sound.object.lockpick.break.ogg", performer, 1.0F);
/*  4250 */       Items.destroyItem(lockpick.getWurmId());
/*       */     } else {
/*       */       
/*  4253 */       lockpick.setDamage(lockpick.getDamage() + (0.5F - lockpick.getRarity() * 0.1F) * lockpick.getDamageModifier());
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean picklock(Creature performer, Item lockpick, Item target, float counter, Action act) {
/*  4260 */     long lockId = target.getLockId();
/*  4261 */     boolean done = false;
/*  4262 */     if (lockId == -10L) {
/*       */       
/*  4264 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is not locked.", (byte)3);
/*  4265 */       return true;
/*       */     } 
/*  4267 */     if (lockpick.getTemplateId() != 463) {
/*       */       
/*  4269 */       performer.getCommunicator()
/*  4270 */         .sendNormalServerMessage("The " + lockpick.getName() + " can not be used as a lockpick.", (byte)3);
/*  4271 */       return true;
/*       */     } 
/*       */ 
/*       */     
/*  4275 */     if (target.getWurmId() == 5390789413122L)
/*       */     {
/*  4277 */       if (target.getParentId() == 5390755858690L) {
/*       */         
/*  4279 */         boolean ok = true;
/*  4280 */         if (!performer.hasAbility(Abilities.getAbilityForItem(809, performer)))
/*  4281 */           ok = false; 
/*  4282 */         if (!performer.hasAbility(Abilities.getAbilityForItem(808, performer)))
/*  4283 */           ok = false; 
/*  4284 */         if (!performer.hasAbility(Abilities.getAbilityForItem(798, performer)))
/*  4285 */           ok = false; 
/*  4286 */         if (!performer.hasAbility(Abilities.getAbilityForItem(810, performer)))
/*  4287 */           ok = false; 
/*  4288 */         if (!performer.hasAbility(Abilities.getAbilityForItem(807, performer)))
/*  4289 */           ok = false; 
/*  4290 */         if (!ok) {
/*       */           
/*  4292 */           performer.getCommunicator().sendAlertServerMessage("There is some mysterious enchantment on this lock!");
/*  4293 */           return true;
/*       */         } 
/*       */       } 
/*       */     }
/*       */     
/*  4298 */     boolean insta = (performer.getPower() >= 5 || (Servers.localServer.testServer && performer.getPower() > 1));
/*  4299 */     Skill lockpicking = null;
/*  4300 */     Skills skills = performer.getSkills();
/*       */     
/*       */     try {
/*  4303 */       lockpicking = skills.getSkill(10076);
/*       */     }
/*  4305 */     catch (NoSuchSkillException nss) {
/*       */       
/*  4307 */       lockpicking = skills.learn(10076, 1.0F);
/*       */     } 
/*  4309 */     int time = 300;
/*  4310 */     if (counter == 1.0F) {
/*       */       
/*  4312 */       for (Player p : Players.getInstance().getPlayers()) {
/*       */         
/*  4314 */         if (p.getWurmId() != performer.getWurmId()) {
/*       */           
/*       */           try {
/*  4317 */             Action pact = p.getCurrentAction();
/*  4318 */             if (act.getNumber() == pact.getNumber() && act.getTarget() == pact.getTarget())
/*       */             {
/*  4320 */               performer.getCommunicator().sendNormalServerMessage("The " + target
/*  4321 */                   .getName() + " is already being picked by " + p.getName() + ".", (byte)3);
/*  4322 */               return true;
/*       */             }
/*       */           
/*       */           }
/*  4326 */           catch (NoSuchActionException noSuchActionException) {}
/*       */         }
/*       */       } 
/*       */ 
/*       */       
/*  4331 */       if (!performer.isOnPvPServer())
/*       */       {
/*  4333 */         if (target.getOwnerId() == -10L) {
/*       */           
/*  4335 */           boolean ok = false;
/*  4336 */           if (target.getLastOwnerId() != -10L) {
/*       */             
/*  4338 */             Village v = Villages.getVillageForCreature(target.getLastOwnerId());
/*  4339 */             if (v != null) {
/*       */               
/*  4341 */               if (performer.getCitizenVillage() == v)
/*  4342 */                 ok = true; 
/*  4343 */               if (v.isEnemy(performer.getCitizenVillage()))
/*  4344 */                 ok = true; 
/*       */             } 
/*  4346 */             if (target.getLastOwnerId() == performer.getWurmId()) {
/*  4347 */               ok = true;
/*  4348 */             } else if (!ok) {
/*       */               
/*  4350 */               PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(target.getLastOwnerId());
/*  4351 */               if (pinf != null) {
/*       */                 
/*  4353 */                 Friend[] friends = pinf.getFriends();
/*  4354 */                 for (Friend f : friends) {
/*       */                   
/*  4356 */                   if (f.getFriendId() == performer.getWurmId() && f.getCategory() == Friend.Category.Trusted)
/*  4357 */                     ok = true; 
/*       */                 } 
/*       */               } 
/*       */             } 
/*       */           } 
/*  4362 */           if (!ok) {
/*       */             
/*  4364 */             performer.getCommunicator().sendNormalServerMessage("You are not allowed to pick the lock of that in these peaceful lands.", (byte)3);
/*       */ 
/*       */             
/*  4367 */             return true;
/*       */           } 
/*       */         } 
/*       */       }
/*       */       
/*       */       try {
/*  4373 */         Item lock = Items.getItem(lockId);
/*  4374 */         if (lock.getQualityLevel() - lockpick.getQualityLevel() > 20.0F)
/*       */         {
/*  4376 */           performer.getCommunicator().sendNormalServerMessage("You need a more advanced lock pick for this high quality lock.", (byte)3);
/*       */           
/*  4378 */           return true;
/*       */         }
/*       */       
/*  4381 */       } catch (NoSuchItemException nsi) {
/*       */         
/*  4383 */         performer.getCommunicator().sendNormalServerMessage("There is no lock to pick.", (byte)3);
/*  4384 */         logger.log(Level.WARNING, "No such lock, but it should be locked." + nsi.getMessage(), (Throwable)nsi);
/*  4385 */         return true;
/*       */       } 
/*  4387 */       time = Actions.getPickActionTime(performer, lockpicking, lockpick, 0.0D);
/*       */       
/*  4389 */       act.setTimeLeft(time);
/*       */       
/*  4391 */       performer.getCommunicator().sendNormalServerMessage("You start to pick the lock of the " + target
/*  4392 */           .getName() + ".");
/*  4393 */       Server.getInstance().broadCastAction(performer
/*  4394 */           .getName() + " starts to pick the lock of the " + target.getName() + ".", performer, 5);
/*  4395 */       performer.sendActionControl("picking lock", true, time);
/*  4396 */       performer.getStatus().modifyStamina(-2000.0F);
/*       */     }
/*       */     else {
/*       */       
/*  4400 */       time = act.getTimeLeft();
/*       */     } 
/*  4402 */     if (act.currentSecond() == 2)
/*       */     {
/*  4404 */       checkLockpickBreakage(performer, lockpick, 100, 80.0D);
/*       */     }
/*  4406 */     if (counter * 10.0F > time || insta) {
/*       */       
/*  4408 */       performer.getStatus().modifyStamina(-2000.0F);
/*  4409 */       boolean dryRun = false;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       try {
/*  4435 */         Item lock = Items.getItem(lockId);
/*  4436 */         done = true;
/*       */         
/*  4438 */         if (target.getOwnerId() == performer.getWurmId() || performer
/*  4439 */           .isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*       */           
/*  4441 */           boolean stealing = checkIfStealing(target, performer, act);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  4451 */           double bonus = (100.0F * Item.getMaterialLockpickBonus(lockpick.getMaterial()));
/*  4452 */           int breakBonus = (int)(bonus * 2.0D);
/*  4453 */           bonus -= (lock.getRarity() * 10);
/*  4454 */           bonus = Math.min(99.0D, bonus);
/*       */ 
/*       */           
/*  4457 */           if (stealing) {
/*       */             
/*  4459 */             if (Action.checkLegalMode(performer))
/*  4460 */               return true; 
/*  4461 */             if (!performer.maySteal()) {
/*       */               
/*  4463 */               performer.getCommunicator()
/*  4464 */                 .sendNormalServerMessage("You need more body control to pick locks.", (byte)3);
/*  4465 */               return true;
/*       */             } 
/*  4467 */             if (setTheftEffects(performer, act, target)) {
/*       */               
/*  4469 */               double d = lockpicking.skillCheck(lock.getCurrentQualityLevel(), lockpick, bonus, false, 10.0F);
/*       */ 
/*       */               
/*  4472 */               checkLockpickBreakage(performer, lockpick, breakBonus, d);
/*  4473 */               return true;
/*       */             } 
/*       */           } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  4514 */           double power = lockpicking.skillCheck(lock.getCurrentQualityLevel(), lockpick, bonus, false, 10.0F);
/*  4515 */           float rarityMod = 1.0F;
/*  4516 */           if (lock.getRarity() > 0) {
/*  4517 */             rarityMod += lock.getRarity() * 0.2F;
/*       */           }
/*  4519 */           if (target.getRarity() > 0)
/*  4520 */             rarityMod += target.getRarity() * 0.2F; 
/*  4521 */           if (lockpick.getRarity() > 0)
/*  4522 */             rarityMod -= lockpick.getRarity() * 0.1F; 
/*  4523 */           byte picktype = 0;
/*  4524 */           if (target.isVehicle())
/*       */           {
/*  4526 */             if (target.getPosZ() < 1.0F)
/*       */             {
/*  4528 */               if (target.getSizeZ() > 5) {
/*  4529 */                 picktype = 2;
/*       */               }
/*       */             }
/*       */           }
/*       */           
/*  4534 */           float chance = getPickChance(target.getCurrentQualityLevel(), lockpick.getCurrentQualityLevel(), lock.getCurrentQualityLevel(), (float)lockpicking.getRealKnowledge(), picktype) / rarityMod * (1.0F + Item.getMaterialLockpickBonus(lockpick.getMaterial()));
/*  4535 */           if (Server.rand.nextFloat() * 100.0F < chance) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  4544 */             performer.getCommunicator().sendNormalServerMessage("You pick the lock of the " + target
/*  4545 */                 .getName() + ".");
/*  4546 */             Server.getInstance().broadCastAction(performer
/*  4547 */                 .getName() + " picks the lock of the " + target.getName() + ".", performer, 5);
/*  4548 */             target.setLockId(-10L);
/*  4549 */             ItemSettings.remove(target.getWurmId());
/*  4550 */             PermissionsHistories.addHistoryEntry(target.getWurmId(), System.currentTimeMillis(), performer
/*  4551 */                 .getWurmId(), performer.getName(), "Lock Picked");
/*  4552 */             lock.setLocked(false);
/*  4553 */             performer.getInventory().insertItem(lock, true);
/*  4554 */             SoundPlayer.playSound("sound.object.lockunlock", target, 0.2F);
/*  4555 */             performer.achievement(111);
/*  4556 */             if (target.isBoat()) {
/*  4557 */               performer.achievement(108);
/*       */ 
/*       */             
/*       */             }
/*       */ 
/*       */           
/*       */           }
/*       */           else {
/*       */ 
/*       */ 
/*       */             
/*  4568 */             performer.getCommunicator().sendNormalServerMessage("You fail to pick the lock of the " + target
/*  4569 */                 .getName() + ".", (byte)3);
/*  4570 */             Server.getInstance().broadCastAction(performer
/*  4571 */                 .getName() + " silently curses as " + performer.getHeSheItString() + " fails to pick the lock of the " + target
/*  4572 */                 .getName() + ".", performer, 5);
/*       */           } 
/*       */           
/*  4575 */           if (power > 0.0D) {
/*  4576 */             checkLockpickBreakage(performer, lockpick, breakBonus, 100.0D);
/*       */           } else {
/*  4578 */             checkLockpickBreakage(performer, lockpick, breakBonus, -100.0D);
/*       */           } 
/*       */         } else {
/*       */           
/*  4582 */           performer.getCommunicator().sendNormalServerMessage("You are too far away from the " + target
/*  4583 */               .getName() + " to pick the lock.", (byte)3);
/*       */         } 
/*       */         
/*  4586 */         return true;
/*       */       }
/*  4588 */       catch (NoSuchItemException nsi) {
/*       */         
/*  4590 */         performer.getCommunicator().sendNormalServerMessage("There is no lock to pick.", (byte)3);
/*  4591 */         logger.log(Level.WARNING, "No such lock, but it should be locked." + nsi.getMessage(), (Throwable)nsi);
/*  4592 */         return true;
/*       */       } 
/*       */     } 
/*  4595 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static final float getPickModChance(float chance) {
/*  4608 */     return (1000.0F + chance * chance) / 11000.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static final float getPickChance(float containerQl, float pickQl, float lockQl, float skill, byte pickType) {
/*  4615 */     float chance = getPickModChance(skill);
/*  4616 */     float baseChance = Math.max(1.0F, Math.min(95.0F, 100.0F - lockQl));
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  4639 */     if (skill > lockQl && pickType != 2)
/*  4640 */       baseChance += skill - lockQl; 
/*  4641 */     chance *= baseChance;
/*  4642 */     if (pickType == 3) {
/*  4643 */       chance *= 2.0F;
/*       */     }
/*       */     
/*  4646 */     float contMod = 1.0F + (100.0F - containerQl) / 100.0F;
/*       */ 
/*       */     
/*  4649 */     chance *= contMod;
/*       */     
/*  4651 */     if (pickType == 2) {
/*  4652 */       chance /= 3.0F;
/*       */     }
/*  4654 */     float mod = getPickModChance(pickQl);
/*       */     
/*  4656 */     chance *= mod;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  4674 */     return Math.max(0.001F, Math.min(99.0F, chance));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private static Point findDropTile(int tileX, int tileY, MeshIO mesh) {
/*  4732 */     List<Point> slopes = new ArrayList<>();
/*  4733 */     short h = Tiles.decodeHeight(mesh.getTile(tileX, tileY));
/*       */     
/*  4735 */     for (int xx = 1; xx >= -1; xx--) {
/*       */       
/*  4737 */       for (int yy = 1; yy >= -1; yy--) {
/*       */         
/*  4739 */         if (GeneralUtilities.isValidTileLocation(tileX + xx, tileY + yy)) {
/*       */           
/*  4741 */           short th = Tiles.decodeHeight(mesh.getTile(tileX + xx, tileY + yy));
/*       */           
/*  4743 */           if ((xx == 0 && yy != 0) || (yy == 0 && xx != 0))
/*       */           {
/*  4745 */             if (th <= h - 40) {
/*  4746 */               slopes.add(new Point(tileX + xx, tileY + yy));
/*       */             }
/*       */           }
/*  4749 */           if (xx != 0 && yy != 0)
/*       */           {
/*  4751 */             if (th <= h - 56)
/*  4752 */               slopes.add(new Point(tileX + xx, tileY + yy)); 
/*       */           }
/*       */         } 
/*       */       } 
/*       */     } 
/*  4757 */     if (slopes.size() > 0) {
/*       */       
/*  4759 */       int r = 0;
/*       */       
/*  4761 */       if (slopes.size() > 1)
/*  4762 */         r = Server.rand.nextInt(slopes.size()); 
/*  4763 */       return findDropTile(((Point)slopes.get(r)).getX(), ((Point)slopes.get(r)).getY(), mesh);
/*       */     } 
/*       */ 
/*       */     
/*  4767 */     return new Point(tileX, tileY);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean takePile(Action act, Creature performer, Item origTarget) {
/*  4773 */     boolean toReturn = true;
/*       */     
/*       */     try {
/*  4776 */       long ownId = origTarget.getOwner();
/*  4777 */       if (ownId != -10L)
/*  4778 */         return true; 
/*  4779 */       if (ownId == performer.getWurmId()) {
/*  4780 */         return true;
/*       */       }
/*  4782 */     } catch (Exception ex) {
/*       */ 
/*       */       
/*       */       try {
/*  4786 */         Zone zone = Zones.getZone((int)origTarget.getPosX() >> 2, (int)origTarget.getPosY() >> 2, origTarget
/*  4787 */             .isOnSurface());
/*  4788 */         VolaTile tile = zone.getTileOrNull((int)origTarget.getPosX() >> 2, (int)origTarget.getPosY() >> 2);
/*  4789 */         if (tile != null) {
/*       */           
/*  4791 */           if (performer.getPower() == 0)
/*       */           {
/*  4793 */             Structure struct = tile.getStructure();
/*       */             
/*  4795 */             VolaTile tile2 = performer.getCurrentTile();
/*  4796 */             if (tile2 != null)
/*       */             {
/*  4798 */               if (tile2.getStructure() != struct)
/*       */               {
/*  4800 */                 performer.getCommunicator().sendNormalServerMessage("You can't reach the " + origTarget
/*  4801 */                     .getName() + " through the wall.");
/*  4802 */                 return true;
/*       */               
/*       */               }
/*       */             
/*       */             }
/*  4807 */             else if (struct != null)
/*       */             {
/*  4809 */               performer.getCommunicator().sendNormalServerMessage("You can't reach the " + origTarget
/*  4810 */                   .getName() + " through the wall.");
/*  4811 */               return true;
/*       */             }
/*       */           
/*       */           }
/*       */         
/*       */         } else {
/*       */           
/*  4818 */           logger.log(Level.WARNING, performer.getName() + " scam?:No tile found in zone.");
/*  4819 */           return true;
/*       */         }
/*       */       
/*  4822 */       } catch (NoSuchZoneException nsz) {
/*       */         
/*  4824 */         logger.log(Level.WARNING, performer.getName() + " scam?:" + nsz.getMessage(), (Throwable)nsz);
/*  4825 */         return true;
/*       */       } 
/*  4827 */       Set<Item> items = origTarget.getItems();
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  4833 */       Item[] itemarr = items.<Item>toArray(new Item[items.size()]);
/*  4834 */       List<TakeResultEnum> printed = new ArrayList<>();
/*  4835 */       Map<String, Integer> taken = new HashMap<>();
/*  4836 */       int largestItem = 0;
/*  4837 */       for (int i = 0; i < itemarr.length; i++) {
/*       */         
/*  4839 */         TakeResultEnum result = take(act, performer, itemarr[i]);
/*  4840 */         if (result.shouldPrint()) {
/*       */           
/*  4842 */           if (!printed.contains(result)) {
/*       */             
/*  4844 */             result.sendToPerformer(performer);
/*  4845 */             printed.add(result);
/*       */           } 
/*       */           
/*  4848 */           if (result.abortsTakeFromPile()) {
/*       */             break;
/*       */           }
/*       */         } else {
/*       */           
/*  4853 */           if (result.abortsTakeFromPile()) {
/*       */             break;
/*       */           }
/*       */           
/*  4857 */           if (result == TakeResultEnum.SUCCESS) {
/*       */             
/*  4859 */             if (!taken.containsKey(itemarr[i].getName())) {
/*  4860 */               taken.put(itemarr[i].getName(), Integer.valueOf(1));
/*       */             } else {
/*       */               
/*  4863 */               taken.put(itemarr[i].getName(), 
/*  4864 */                   Integer.valueOf(((Integer)taken.get(itemarr[i].getName())).intValue() + 1));
/*       */             } 
/*  4866 */             int size = itemarr[i].getSizeZ() / 10;
/*  4867 */             if (size > largestItem)
/*  4868 */               largestItem = size; 
/*       */           } 
/*       */         } 
/*       */       } 
/*  4872 */       if (taken.size() > 0) {
/*       */         
/*  4874 */         String takeString = "";
/*  4875 */         for (String t : taken.keySet()) {
/*       */           
/*  4877 */           if (takeString.isEmpty()) {
/*  4878 */             takeString = StringUtil.format("%d %s", new Object[] { Integer.valueOf(((Integer)taken.get(t)).intValue()), t }); continue;
/*       */           } 
/*  4880 */           takeString = StringUtil.format("%s, %d %s", new Object[] { takeString, Integer.valueOf(((Integer)taken.get(t)).intValue()), t });
/*       */         } 
/*       */         
/*  4883 */         performer.getCommunicator().sendNormalServerMessage("You get " + takeString + ".");
/*  4884 */         Server.getInstance().broadCastAction(performer
/*  4885 */             .getName() + " gets " + takeString + ".", performer, 
/*  4886 */             Math.max(3, largestItem));
/*       */       } 
/*       */     } 
/*  4889 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public static void fillContainer(Item source, @Nullable Creature performer, boolean isBrackish) {
/*  4894 */     if (source.isContainerLiquid()) {
/*       */       
/*  4896 */       if (source.isTraded()) {
/*       */         
/*  4898 */         if (performer != null) {
/*  4899 */           performer.getCommunicator().sendNormalServerMessage("The container is traded.");
/*       */         }
/*       */       } else {
/*       */         
/*  4903 */         Item contained = null;
/*  4904 */         Item liquid = null;
/*       */         
/*  4906 */         for (Iterator<Item> it = source.getItems().iterator(); it.hasNext(); ) {
/*       */           
/*  4908 */           contained = it.next();
/*  4909 */           if ((!contained.isFood() && !contained.isLiquid() && !contained.isRecipeItem()) || (contained
/*  4910 */             .isLiquid() && contained.getTemplateId() != 128)) {
/*       */             
/*  4912 */             if (performer != null)
/*  4913 */               performer.getCommunicator().sendNormalServerMessage("That would destroy the liquid."); 
/*       */             return;
/*       */           } 
/*  4916 */           if (contained.isLiquid())
/*       */           {
/*  4918 */             liquid = contained;
/*       */           }
/*       */         } 
/*  4921 */         int volAvail = source.getFreeVolume();
/*  4922 */         contained = liquid;
/*  4923 */         if (contained != null && contained.getRarity() != 0) {
/*       */           
/*  4925 */           if (performer != null)
/*  4926 */             performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " would lose its rarity."); 
/*       */           return;
/*       */         } 
/*  4929 */         if (volAvail > 0) {
/*       */           
/*  4931 */           int capac = volAvail;
/*  4932 */           if (performer != null)
/*  4933 */             capac = performer.getCarryingCapacityLeft(); 
/*  4934 */           float fract = 1.0F;
/*  4935 */           if (capac < volAvail) {
/*       */             
/*  4937 */             fract = capac / volAvail;
/*  4938 */             volAvail = (int)(fract * volAvail);
/*       */           } 
/*  4940 */           if (volAvail >= 1) {
/*       */             
/*  4942 */             if (liquid != null) {
/*       */               
/*  4944 */               int allWeight = liquid.getWeightGrams() + volAvail;
/*       */               
/*  4946 */               float newQl = ((100 * volAvail) + contained.getCurrentQualityLevel() * contained.getWeightGrams()) / allWeight;
/*       */               
/*  4948 */               liquid.setWeight(liquid.getWeightGrams() + volAvail, true);
/*  4949 */               liquid.setQualityLevel(newQl);
/*  4950 */               liquid.setDamage(0.0F);
/*  4951 */               if (isBrackish) {
/*  4952 */                 liquid.setIsSalted(true);
/*       */               }
/*       */             } else {
/*       */ 
/*       */               
/*       */               try {
/*  4958 */                 Item water = ItemFactory.createItem(128, 100.0F, (byte)26, (byte)0, null);
/*  4959 */                 water.setSizes(1, 1, 1);
/*  4960 */                 water.setWeight(volAvail, false);
/*  4961 */                 if (isBrackish)
/*  4962 */                   water.setIsSalted(true); 
/*  4963 */                 if (!source.insertItem(water)) {
/*       */                   
/*  4965 */                   if (performer != null) {
/*  4966 */                     performer.getCommunicator().sendNormalServerMessage("The container can't keep any of the water.");
/*       */                   }
/*  4968 */                   Items.decay(water.getWurmId(), water.getDbStrings());
/*       */                   
/*       */                   return;
/*       */                 } 
/*  4972 */               } catch (NoSuchTemplateException nst) {
/*       */                 
/*  4974 */                 logger.log(Level.WARNING, "No template for water?!", (Throwable)nst);
/*       */               }
/*  4976 */               catch (FailedException fe) {
/*       */                 
/*  4978 */                 logger.log(Level.WARNING, "Creation of water failed: ", (Throwable)fe);
/*       */               } 
/*       */             } 
/*  4981 */             int tid = source.getTemplateId();
/*  4982 */             String sound = "sound.liquid.fillcontainer";
/*  4983 */             if (tid == 190 || tid == 189 || tid == 768) {
/*  4984 */               sound = "sound.liquid.fillcontainer.barrel";
/*  4985 */             } else if (tid == 421) {
/*  4986 */               sound = "sound.liquid.fillcontainer.bucket";
/*  4987 */             } else if (tid == 76) {
/*  4988 */               sound = "sound.liquid.fillcontainer.jar";
/*  4989 */             }  if (performer != null)
/*       */             {
/*  4991 */               Methods.sendSound(performer, sound);
/*  4992 */               performer.getCommunicator().sendNormalServerMessage("You fill the " + source
/*  4993 */                   .getName() + " with " + (isBrackish ? "salty " : "") + "water.");
/*       */             }
/*       */           
/*       */           } 
/*  4997 */         } else if (performer != null) {
/*  4998 */           performer.getCommunicator().sendNormalServerMessage("You wouldn't be able to carry the weight of the water.");
/*       */         }
/*       */       
/*       */       } 
/*  5002 */     } else if (performer != null) {
/*  5003 */       performer.getCommunicator().sendNormalServerMessage("You cannot keep liquid in that.");
/*       */     } 
/*       */   }
/*       */   
/*       */   public static void fillContainer(Action act, Item source, Item target, Creature performer, boolean quiet) {
/*  5008 */     if (source.isContainerLiquid()) {
/*       */       
/*  5010 */       if (!source.isTraded() && !target.isTraded()) {
/*       */         
/*  5012 */         if (target.isNoTake()) {
/*       */           
/*  5014 */           performer.getCommunicator().sendSafeServerMessage("The " + target.getName() + " cannot be taken.");
/*       */           return;
/*       */         } 
/*  5017 */         Item contained = null;
/*  5018 */         Item liquid = null;
/*  5019 */         int volAvail = source.getFreeVolume();
/*  5020 */         if (source.getOwnerId() == performer.getWurmId()) {
/*       */           
/*  5022 */           int canCarry = performer.getCarryingCapacityLeft();
/*  5023 */           if (canCarry <= 0) {
/*       */             
/*  5025 */             performer.getCommunicator().sendNormalServerMessage("You won't be able to carry that much.");
/*       */             
/*       */             return;
/*       */           } 
/*  5029 */           if (volAvail > canCarry) {
/*  5030 */             volAvail = canCarry;
/*       */           }
/*       */         } 
/*  5033 */         for (Iterator<Item> it = source.getItems().iterator(); it.hasNext(); ) {
/*       */           
/*  5035 */           contained = it.next();
/*       */ 
/*       */           
/*  5038 */           if (wouldDestroyLiquid(source, contained, target)) {
/*       */             
/*  5040 */             performer.getCommunicator().sendNormalServerMessage("That would destroy the liquid.");
/*       */             
/*       */             return;
/*       */           } 
/*  5044 */           if (contained.isLiquid() && contained.getTemplateId() == target.getTemplateId() && contained
/*  5045 */             .getRealTemplateId() == target.getRealTemplateId())
/*       */           {
/*  5047 */             liquid = contained;
/*       */           }
/*       */         } 
/*  5050 */         contained = liquid;
/*  5051 */         if (contained != null && contained.getRarity() > target.getRarity())
/*       */         {
/*  5053 */           contained.setRarity(target.getRarity());
/*       */         }
/*  5055 */         if (volAvail > 0) {
/*       */           
/*  5057 */           if (checkIfStealing(target, performer, act)) {
/*       */             
/*  5059 */             if (Action.checkLegalMode(performer))
/*       */               return; 
/*  5061 */             if (!performer.maySteal()) {
/*       */               
/*  5063 */               performer.getCommunicator().sendNormalServerMessage("You need more body control to steal things.");
/*       */               
/*       */               return;
/*       */             } 
/*  5067 */             if (setTheftEffects(performer, act, target))
/*       */               return; 
/*       */           } 
/*  5070 */           if (volAvail < target.getWeightGrams()) {
/*       */             
/*  5072 */             if (!performer.canCarry(volAvail) && source.getOwnerId() == performer.getWurmId()) {
/*       */               
/*  5074 */               performer.getCommunicator().sendNormalServerMessage("You won't be able to carry that much.");
/*       */               return;
/*       */             } 
/*  5077 */             if (contained == null) {
/*       */ 
/*       */               
/*       */               try {
/*  5081 */                 Item splitItem = splitLiquid(target, volAvail, performer);
/*  5082 */                 source.insertItem(splitItem);
/*       */               }
/*  5084 */               catch (FailedException fe) {
/*       */                 
/*  5086 */                 logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*       */                 
/*       */                 return;
/*  5089 */               } catch (NoSuchTemplateException nst) {
/*       */                 
/*  5091 */                 logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*       */ 
/*       */ 
/*       */                 
/*       */                 return;
/*       */               } 
/*       */             } else {
/*  5098 */               boolean differentOwners = (target.getOwnerId() != source.getOwnerId());
/*       */               
/*  5100 */               target.setWeight(target.getWeightGrams() - volAvail, true, differentOwners);
/*       */               
/*  5102 */               int allWeight = contained.getWeightGrams() + volAvail;
/*       */               
/*  5104 */               float newQl = (target.getCurrentQualityLevel() * volAvail + contained.getCurrentQualityLevel() * contained.getWeightGrams()) / allWeight;
/*       */               
/*  5106 */               if (contained.isColor())
/*       */               {
/*  5108 */                 contained.setColor(WurmColor.mixColors(contained.color, contained.getWeightGrams(), target.color, volAvail, newQl));
/*       */               }
/*       */ 
/*       */               
/*  5112 */               float tmod = volAvail / allWeight;
/*  5113 */               float contMod = contained.getWeightGrams() / allWeight;
/*       */               
/*  5115 */               contained.setTemperature(
/*  5116 */                   (short)(int)(target.getTemperature() * tmod + contMod * contained.getTemperature()));
/*  5117 */               contained.setQualityLevel(newQl);
/*  5118 */               contained.setWeight(contained.getWeightGrams() + volAvail, true, differentOwners);
/*  5119 */               contained.setDamage(0.0F);
/*       */             }
/*       */           
/*       */           } else {
/*       */             
/*  5124 */             if (!performer.canCarry(target.getWeightGrams()) && source.getOwnerId() == performer.getWurmId()) {
/*       */               
/*  5126 */               performer.getCommunicator().sendNormalServerMessage("You won't be able to carry that much.");
/*       */               return;
/*       */             } 
/*  5129 */             if (contained != null) {
/*       */               
/*  5131 */               if (contained.getTemplateId() == 417 || target
/*  5132 */                 .getTemplateId() == 417)
/*       */               {
/*  5134 */                 if (contained.getRealTemplateId() != target.getRealTemplateId()) {
/*       */                   
/*  5136 */                   String name1 = "fruit";
/*  5137 */                   String name2 = "fruit";
/*  5138 */                   ItemTemplate t = contained.getRealTemplate();
/*  5139 */                   if (t != null)
/*  5140 */                     name1 = t.getName(); 
/*  5141 */                   ItemTemplate t2 = target.getRealTemplate();
/*  5142 */                   if (t2 != null) {
/*  5143 */                     name2 = t2.getName();
/*       */                   }
/*  5145 */                   if (!name1.equals(name2))
/*  5146 */                     contained.setName(name1 + " and " + name2 + " juice"); 
/*  5147 */                   contained.setRealTemplate(-10);
/*       */                 } 
/*       */               }
/*  5150 */               int allWeight = target.getWeightGrams() + contained.getWeightGrams();
/*       */               
/*  5152 */               float newQl = (target.getCurrentQualityLevel() * target.getWeightGrams() + contained.getCurrentQualityLevel() * contained.getWeightGrams()) / allWeight;
/*       */               
/*  5154 */               if (contained.isColor())
/*       */               {
/*  5156 */                 contained.setColor(WurmColor.mixColors(contained.color, contained.getWeightGrams(), target.color, target
/*  5157 */                       .getWeightGrams(), newQl));
/*       */               }
/*       */               
/*  5160 */               float tmod = target.getWeightGrams() / allWeight;
/*  5161 */               float contMod = contained.getWeightGrams() / allWeight;
/*       */               
/*  5163 */               contained.setTemperature(
/*  5164 */                   (short)(int)(target.getTemperature() * tmod + contMod * contained.getTemperature()));
/*  5165 */               contained.setQualityLevel(newQl);
/*  5166 */               contained.setDamage(0.0F);
/*  5167 */               contained.setWeight(allWeight, true);
/*  5168 */               Items.destroyItem(target.getWurmId());
/*       */             }
/*  5170 */             else if (source.testInsertItem(target)) {
/*       */ 
/*       */               
/*       */               try {
/*  5174 */                 Item parent = target.getParent();
/*  5175 */                 parent.dropItem(target.getWurmId(), false);
/*       */               }
/*  5177 */               catch (NoSuchItemException noSuchItemException) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  5184 */               source.insertItem(target);
/*       */             } else {
/*       */               return;
/*       */             } 
/*       */           } 
/*       */ 
/*       */ 
/*       */           
/*  5192 */           if (act.getNumber() != 345) {
/*       */             
/*  5194 */             int tid = source.getTemplateId();
/*  5195 */             String sound = "sound.liquid.fillcontainer";
/*  5196 */             if (tid == 190 || tid == 189 || tid == 768) {
/*  5197 */               sound = "sound.liquid.fillcontainer.barrel";
/*  5198 */             } else if (tid == 421) {
/*  5199 */               sound = "sound.liquid.fillcontainer.bucket";
/*  5200 */             } else if (tid == 76) {
/*  5201 */               sound = "sound.liquid.fillcontainer.jar";
/*  5202 */             }  Methods.sendSound(performer, sound);
/*       */           } 
/*  5204 */           if (!quiet) {
/*       */             
/*  5206 */             int tilex = performer.getTileX();
/*  5207 */             int tiley = (int)performer.getStatus().getPositionY() >> 2;
/*  5208 */             VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, performer.isOnSurface());
/*       */             
/*  5210 */             performer.getCommunicator().sendNormalServerMessage("You fill the " + source
/*  5211 */                 .getName() + " with " + target.getName() + ".");
/*  5212 */             vtile.broadCastAction(performer.getName() + " fills a " + source.getName() + " with " + target.getName() + ".", performer, false);
/*       */           } 
/*       */         } else {
/*       */           
/*       */           return;
/*       */         } 
/*       */       } else {
/*       */         
/*  5220 */         performer.getCommunicator().sendNormalServerMessage("You are trading one of those items.");
/*       */       } 
/*       */     } else {
/*  5223 */       performer.getCommunicator().sendNormalServerMessage("You cannot keep liquid in that.");
/*       */     } 
/*       */   }
/*       */   
/*       */   public static Item splitLiquid(Item target, int volAvail, Creature performer) throws FailedException, NoSuchTemplateException {
/*  5228 */     Item splitItem = ItemFactory.createItem(target.getTemplateId(), target.getQualityLevel(), target.creator);
/*       */     
/*  5230 */     splitItem.setWeight(volAvail, true);
/*  5231 */     splitItem.setTemperature(target.getTemperature());
/*  5232 */     splitItem.setAuxData(target.getAuxData());
/*  5233 */     splitItem.setName(target.getActualName());
/*  5234 */     splitItem.setCreator(target.creator);
/*  5235 */     splitItem.setDamage(target.getDamage());
/*  5236 */     splitItem.setMaterial(target.getMaterial());
/*  5237 */     splitItem.setSizes(1, 1, 1);
/*  5238 */     if (target.getRealTemplate() != null)
/*  5239 */       splitItem.setRealTemplate(target.getRealTemplateId()); 
/*  5240 */     if (target.descIsExam()) {
/*       */       
/*  5242 */       splitItem.setDescription(target.examine(performer));
/*       */     } else {
/*       */       
/*  5245 */       splitItem.setDescription(target.getDescription());
/*  5246 */     }  if (target.color != -1) {
/*  5247 */       splitItem.setColor(target.color);
/*       */     }
/*  5249 */     ItemMealData imd = ItemMealData.getItemMealData(target.getWurmId());
/*  5250 */     if (imd != null)
/*       */     {
/*  5252 */       ItemMealData.save(splitItem.getWurmId(), imd.getRecipeId(), imd.getCalories(), imd
/*  5253 */           .getCarbs(), imd.getFats(), imd.getProteins(), imd.getBonus(), imd
/*  5254 */           .getStages(), imd.getIngredients());
/*       */     }
/*  5256 */     target.setWeight(target.getWeightGrams() - volAvail, true);
/*  5257 */     return splitItem;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static int eat(Creature performer, Item food) {
/*  5269 */     float qlevel = food.getCurrentQualityLevel();
/*  5270 */     int weight = food.getWeightGrams();
/*  5271 */     float damage = food.getDamage();
/*  5272 */     float hungerStilled = weight * qlevel * (100.0F - damage) / 100.0F;
/*  5273 */     if (performer.getSize() == 5) {
/*  5274 */       hungerStilled *= 0.5F;
/*  5275 */     } else if (performer.getSize() == 4) {
/*  5276 */       hungerStilled *= 0.7F;
/*  5277 */     } else if (performer.getSize() == 2) {
/*  5278 */       hungerStilled *= 5.0F;
/*  5279 */     } else if (performer.getSize() == 1) {
/*  5280 */       hungerStilled *= 10.0F;
/*  5281 */     }  if (food.getTemplateId() == 272) {
/*       */       
/*  5283 */       int fat = food.getFat();
/*  5284 */       if (fat > 0) {
/*       */         
/*  5286 */         food.setDamage(food.getDamage() + 50.0F);
/*  5287 */         food.setButchered();
/*       */       } else {
/*       */         
/*  5290 */         hungerStilled = 0.0F;
/*       */       } 
/*       */     } else {
/*  5293 */       Items.destroyItem(food.getWurmId());
/*  5294 */     }  return (int)hungerStilled;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean eat(Action act, Creature performer, Item food, float counter) {
/*  5299 */     boolean done = false;
/*  5300 */     float qlevel = food.getCurrentQualityLevel();
/*  5301 */     int weight = food.getWeightGrams();
/*  5302 */     int temp = food.getTemperature();
/*  5303 */     String hotness = "";
/*  5304 */     if (temp > 1000) {
/*  5305 */       hotness = "hot ";
/*       */     }
/*  5307 */     if (!food.isFood())
/*       */     {
/*  5309 */       return true;
/*       */     }
/*  5311 */     if (food.getOwnerId() != performer.getWurmId()) {
/*       */       
/*  5313 */       if (!isLootableBy(performer, food)) {
/*       */         
/*  5315 */         performer.getCommunicator().sendNormalServerMessage("You may not loot that item.");
/*  5316 */         return true;
/*       */       } 
/*  5318 */       if (food.isBanked()) {
/*       */         
/*  5320 */         performer.getCommunicator().sendNormalServerMessage("You can't eat from there.");
/*  5321 */         return true;
/*       */       } 
/*  5323 */       if (checkIfStealing(food, performer, act)) {
/*       */         
/*  5325 */         if (Action.checkLegalMode(performer))
/*  5326 */           return true; 
/*  5327 */         if (!performer.maySteal()) {
/*       */           
/*  5329 */           performer.getCommunicator().sendNormalServerMessage("You need more body control to steal things.");
/*  5330 */           return true;
/*       */         } 
/*       */         
/*  5333 */         if (setTheftEffects(performer, act, food))
/*  5334 */           return true; 
/*       */       } 
/*       */     } 
/*  5337 */     float bonus = 1.0F;
/*  5338 */     if (performer.getDeity() != null && performer.getDeity().isFoodBonus() && performer.getFaith() >= 20.0F && performer
/*  5339 */       .getFavor() >= 20.0F)
/*  5340 */       bonus = 1.25F; 
/*  5341 */     if (food.getDamage() > 90.0F) {
/*       */ 
/*       */       
/*  5344 */       performer.getCommunicator().sendNormalServerMessage("Eww.. the " + hotness + food
/*  5345 */           .getName() + " tastes funny and won't feed you at all!");
/*  5346 */       return true;
/*       */     } 
/*       */     
/*  5349 */     if ((food.getTemplateId() != 488 || food.getRealTemplateId() != 488) && food
/*  5350 */       .getTemplateId() != 666 && !food.isSource())
/*       */     {
/*       */ 
/*       */ 
/*       */       
/*  5355 */       if (performer.getStatus().getHunger() - 3000 < 0) {
/*       */         
/*  5357 */         performer.getCommunicator().sendNormalServerMessage("You are so full, you cannot possibly eat anything else.");
/*  5358 */         if (counter != 1.0F)
/*       */         {
/*  5360 */           if (food.isWrapped() && (food.canBeClothWrapped() || food.canBePapyrusWrapped() || food.canBeRawWrapped()))
/*       */           {
/*  5362 */             performer.getCommunicator().sendNormalServerMessage("You carefully re-wrap the " + hotness + food
/*  5363 */                 .getName(false) + " to keep it fresher.");
/*       */           }
/*       */         }
/*       */         
/*  5367 */         return true;
/*       */       }  } 
/*  5369 */     if (counter == 1.0F) {
/*       */       
/*  5371 */       if (temp > 2500) {
/*       */         
/*  5373 */         performer.getCommunicator().sendNormalServerMessage("The " + food.getName() + " is too hot to eat.");
/*  5374 */         return true;
/*       */       } 
/*  5376 */       if (food.isCheese() && food.isZombiefied())
/*       */       {
/*  5378 */         if (performer.getKingdomTemplateId() == 3) {
/*       */           
/*  5380 */           performer.getCommunicator().sendNormalServerMessage("The " + food
/*  5381 */               .getName() + " tastes weird, but good.");
/*       */         }
/*       */         else {
/*       */           
/*  5385 */           performer.getCommunicator().sendNormalServerMessage("The " + food
/*  5386 */               .getName() + " is horrible, and you can't eat it.");
/*  5387 */           return true;
/*       */         } 
/*       */       }
/*  5390 */       if (food.isWrapped() && (food.canBeClothWrapped() || food.canBePapyrusWrapped() || food.canBeRawWrapped()))
/*       */       {
/*  5392 */         performer.getCommunicator().sendNormalServerMessage("You carefully unwrap the " + hotness + food
/*  5393 */             .getName(false) + " before eating it.");
/*       */       }
/*       */       
/*  5396 */       int time = 0;
/*  5397 */       time = (int)Math.min(performer.getStatus().getHunger() / 30.0F * qlevel, food.getWeightGrams() / 30.0F) * 10;
/*       */ 
/*       */       
/*  5400 */       Server.getInstance().broadCastAction(performer.getName() + " starts to eat " + food.getNameWithGenus() + ".", performer, 3);
/*       */       
/*  5402 */       performer.sendActionControl("Eating " + hotness + food.getName(), true, time);
/*  5403 */       if (qlevel > 50.0F) {
/*       */         
/*  5405 */         performer.getCommunicator().sendNormalServerMessage("The " + hotness + food
/*  5406 */             .getName() + " tastes " + food.getTasteString());
/*  5407 */         if (food.getTasteString().contains("singing"))
/*  5408 */           performer.achievement(146); 
/*       */       } 
/*       */     } 
/*  5411 */     if (((Player)performer).getAlcohol() > 90.0F)
/*       */     {
/*  5413 */       if (Server.rand.nextInt(100) < ((Player)performer).getAlcohol()) {
/*       */         
/*  5415 */         performer.getCommunicator().sendNormalServerMessage("You miss the mouth and it ends up in your face. Who gives a damn?");
/*       */         
/*  5417 */         Server.getInstance().broadCastAction(performer
/*  5418 */             .getName() + " eats " + food.getNameWithGenus() + " with " + performer
/*  5419 */             .getHisHerItsString() + " whole face instead of only with the mouth.", performer, 3);
/*       */         
/*  5421 */         if (food.getTemplateId() != 666) {
/*       */           
/*  5423 */           Items.destroyItem(food.getWurmId());
/*  5424 */           SoundPlayer.playSound("sound.fish.splash", performer, 10.0F);
/*       */         } 
/*  5426 */         return true;
/*       */       } 
/*       */     }
/*  5429 */     if (food.getSpellFoodBonus() > 0.0F)
/*  5430 */       qlevel *= 1.0F + food.getSpellFoodBonus() / 100.0F; 
/*  5431 */     float rarityMod = 0.0F;
/*  5432 */     if (food.getRarity() > 0) {
/*       */ 
/*       */       
/*  5435 */       rarityMod = Math.max(1, food.getWeightGrams() / food.getTemplate().getWeightGrams());
/*  5436 */       rarityMod = food.getRarity() * 10.0F / rarityMod;
/*  5437 */       rarityMod /= 100.0F;
/*  5438 */       rarityMod = Math.max(0.0F, Math.min(0.3F, rarityMod));
/*       */     } 
/*  5440 */     int fed = (int)((weight < 30) ? (weight * qlevel * bonus) : (30.0F * qlevel * bonus));
/*       */     
/*  5442 */     if (food.getTemplateId() == 488) {
/*  5443 */       performer.getStatus().modifyStamina((int)(qlevel * 100.0F));
/*       */     }
/*  5445 */     if (weight < 30) {
/*       */       
/*  5447 */       done = true;
/*  5448 */       if (food.getTemplateId() == 666)
/*       */       {
/*  5450 */         if (performer.isPlayer())
/*       */         {
/*  5452 */           if (((Player)performer).getSaveFile().getSleepLeft() < (((Player)performer).getSaveFile().isFlagSet(77) ? 5L : 4L) * 3600L) {
/*       */             
/*  5454 */             ((Player)performer).getSaveFile().addToSleep(3600);
/*       */           }
/*       */           else {
/*       */             
/*  5458 */             performer.getCommunicator().sendNormalServerMessage("You just taste it, because eating it would be a waste right now.");
/*       */             
/*  5460 */             return true;
/*       */           } 
/*       */         }
/*       */       }
/*  5464 */       modifyHunger(performer, food, weight, rarityMod, fed);
/*  5465 */       if (food.isSource())
/*  5466 */         performer.modifyKarma(weight); 
/*  5467 */       if (food.isWrapped() && (food.canBeClothWrapped() || food.canBePapyrusWrapped() || food.canBeRawWrapped()))
/*       */       {
/*  5469 */         performer.getCommunicator().sendNormalServerMessage("You throw away the old wrapping, litter-bug.");
/*       */       }
/*       */ 
/*       */       
/*  5473 */       Items.destroyItem(food.getWurmId());
/*       */     }
/*       */     else {
/*       */       
/*  5477 */       if (food.isCheese() && food.isZombiefied())
/*       */       {
/*  5479 */         if (performer.getKingdomId() == 3)
/*       */         {
/*  5481 */           performer.healRandomWound((int)(qlevel / 20.0F));
/*       */         }
/*       */       }
/*  5484 */       modifyHunger(performer, food, 30, rarityMod, fed);
/*  5485 */       food.setWeight(weight - 30, true);
/*  5486 */       if (food.isFish())
/*  5487 */         food.setIsUnderWeight(true); 
/*  5488 */       if (food.isSource())
/*  5489 */         performer.modifyKarma(30); 
/*  5490 */       if (food.getTemplateId() == 572)
/*       */       {
/*  5492 */         if (WurmCalendar.currentTime - food.creationDate < 7200L)
/*       */         {
/*  5494 */           if (act.currentSecond() % 5 == 0) {
/*       */             
/*  5496 */             performer.getCommunicator().sendNormalServerMessage("The " + food
/*  5497 */                 .getName() + " is still alive! Its tentacles writhes about your face!");
/*  5498 */             Server.getInstance().broadCastAction(performer
/*  5499 */                 .getName() + " eats a live " + food.getName() + "! Its tentacles writhes about " + performer
/*  5500 */                 .getHisHerItsString() + " face!", performer, 5);
/*       */           } 
/*       */         }
/*       */       }
/*       */     } 
/*  5505 */     if (act.currentSecond() % 5 == 0)
/*       */     {
/*  5507 */       SoundPlayer.playSound("sound.food.eat", performer, 1.0F);
/*       */     }
/*  5509 */     if (performer.getStatus().getHunger() == 1) {
/*       */       
/*  5511 */       if (food.isWrapped() && (food.canBeClothWrapped() || food.canBePapyrusWrapped() || food.canBeRawWrapped()))
/*       */       {
/*  5513 */         performer.getCommunicator().sendNormalServerMessage("You carefully re-wrap the " + hotness + food
/*  5514 */             .getName(false) + " to keep it fresher.");
/*       */       }
/*  5516 */       done = true;
/*       */     } 
/*  5518 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static void modifyHunger(Creature performer, Item food, int weight, float rarityMod, int fed) {
/*  5524 */     float complexity = food.getFoodComplexity() * 50.0F;
/*  5525 */     if (food.getTemplateId() == 1276) {
/*       */       
/*  5527 */       complexity *= 0.1F;
/*       */       
/*  5529 */       performer.getStatus().modifyThirst((-fed * 2 / 3), food
/*  5530 */           .getCaloriesByWeight(weight) * complexity, food
/*  5531 */           .getCarbsByWeight(weight) * complexity, food
/*  5532 */           .getFatsByWeight(weight) * complexity, food
/*  5533 */           .getProteinsByWeight(weight) * complexity);
/*       */       
/*       */       return;
/*       */     } 
/*  5537 */     performer.getStatus().modifyHunger(-fed, 
/*  5538 */         Math.min(0.99F, food.getNutritionLevel() + rarityMod), food
/*  5539 */         .getCaloriesByWeight(weight) * complexity, food
/*  5540 */         .getCarbsByWeight(weight) * complexity, food
/*  5541 */         .getFatsByWeight(weight) * complexity, food
/*  5542 */         .getProteinsByWeight(weight) * complexity);
/*       */     
/*  5544 */     AffinitiesTimed.addTimedAffinityFromBonus(performer, weight, food);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean drinkChampagne(Creature performer, Item target) {
/*  5550 */     Item parent = target.getParentOrNull();
/*       */     
/*  5552 */     if (parent != null)
/*       */     {
/*  5554 */       if (parent.isSealedByPlayer()) {
/*       */         
/*  5556 */         performer.getCommunicator().sendNormalServerMessage("You can't drink from there.");
/*  5557 */         return true;
/*       */       } 
/*       */     }
/*  5560 */     if (target.getOwnerId() == performer.getWurmId()) {
/*       */       
/*  5562 */       if (target.getAuxData() < 10)
/*       */       {
/*  5564 */         target.setAuxData((byte)(target.getAuxData() + 1));
/*  5565 */         if (performer.isPlayer()) {
/*       */           
/*  5567 */           if (((Player)performer).getAlcohol() > 90.0F)
/*       */           {
/*  5569 */             if (Server.rand.nextInt(100) < ((Player)performer).getAlcohol()) {
/*       */               
/*  5571 */               performer.getCommunicator().sendNormalServerMessage("You spill the " + target.getName() + " out. Who cares?");
/*  5572 */               Server.getInstance().broadCastAction(performer.getName() + " throws the " + target.getName() + " over " + performer.getHisHerItsString() + " shoulder instead of drinking it.", performer, 3);
/*  5573 */               return true;
/*       */             } 
/*       */           }
/*  5576 */           ((Player)performer).setRarityShader((target.getRarity() > 0) ? target.getRarity() : 1);
/*       */         } 
/*  5578 */         performer.getStatus().modifyStamina((int)(target.getQualityLevel() * (20 + target.getRarity() * 10)));
/*  5579 */         addAlcohol(performer);
/*  5580 */         performer.getStatus().modifyThirst((int)(-6554.0F * target.getQualityLevel() / 100.0F));
/*  5581 */         performer.getCommunicator().sendNormalServerMessage("Ahh! The " + target.getName() + " tastes goood!");
/*       */       }
/*       */       else
/*       */       {
/*  5585 */         performer.getCommunicator().sendNormalServerMessage("Sadly, the Champagne bottle is empty.");
/*  5586 */         return true;
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/*  5591 */       performer.getCommunicator().sendNormalServerMessage("You need to hold the Champagne to drink it.");
/*  5592 */       return true;
/*       */     } 
/*  5594 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean drink(Action act, Creature performer, Item drink, float counter) {
/*  5599 */     if (performer.getStatus().getThirst() < 1000 && !drink.isSource() && !drink.isAlcohol()) {
/*       */       
/*  5601 */       performer.getCommunicator().sendNormalServerMessage("You are so bloated you cannot bring yourself to drink any thing else.");
/*  5602 */       return true;
/*       */     } 
/*       */     
/*  5605 */     Item parent = drink.getParentOrNull();
/*       */     
/*  5607 */     if (parent != null)
/*       */     {
/*  5609 */       if (parent.isSealedByPlayer()) {
/*       */         
/*  5611 */         performer.getCommunicator().sendNormalServerMessage("You can't drink from there.");
/*  5612 */         return true;
/*       */       } 
/*       */     }
/*  5615 */     boolean done = false;
/*       */     
/*  5617 */     float fillmod = 6554.0F;
/*  5618 */     float qlevel = drink.getCurrentQualityLevel();
/*  5619 */     if (drink.getRarity() > 0) {
/*       */ 
/*       */ 
/*       */       
/*  5623 */       float spamModifier = Math.max(1, drink.getWeightGrams() / drink.getTemplate().getWeightGrams());
/*  5624 */       qlevel = Math.min(99.99F, qlevel + drink.getRarity() * 10.0F / spamModifier);
/*       */     } 
/*  5626 */     int temp = drink.getTemperature();
/*  5627 */     int weight = drink.getWeightGrams();
/*  5628 */     int template = drink.getTemplateId();
/*  5629 */     if (template == 128) {
/*  5630 */       qlevel = Math.max(90.0F, qlevel);
/*       */     } else {
/*       */       
/*  5633 */       if (drink.getOwnerId() != performer.getWurmId()) {
/*       */         
/*  5635 */         if (drink.isBanked()) {
/*       */           
/*  5637 */           performer.getCommunicator().sendNormalServerMessage("You can't drink from there.");
/*  5638 */           return true;
/*       */         } 
/*  5640 */         if (drink.getTemplateId() != 128) {
/*       */           
/*  5642 */           if (!isLootableBy(performer, drink)) {
/*       */             
/*  5644 */             performer.getCommunicator().sendNormalServerMessage("You may not loot that item.");
/*  5645 */             return true;
/*       */           } 
/*  5647 */           if (checkIfStealing(drink, performer, act)) {
/*       */             
/*  5649 */             if (Action.checkLegalMode(performer))
/*  5650 */               return true; 
/*  5651 */             if (!performer.maySteal()) {
/*       */               
/*  5653 */               performer.getCommunicator().sendNormalServerMessage("You need more body control to steal things.");
/*  5654 */               return true;
/*       */             } 
/*       */             
/*  5657 */             if (setTheftEffects(performer, act, drink))
/*  5658 */               return true; 
/*       */           } 
/*       */         } 
/*       */       } 
/*  5662 */       if (drink.isMilk()) {
/*       */         
/*  5664 */         if (qlevel > 5.0F) {
/*  5665 */           qlevel = Math.min(100.0F, (float)(qlevel * 1.5D));
/*       */         }
/*  5667 */       } else if (!drink.isDrinkable()) {
/*  5668 */         qlevel = 0.0F;
/*       */       } 
/*       */     } 
/*       */ 
/*       */     
/*  5673 */     if (counter == 1.0F && qlevel < 5.0F) {
/*       */ 
/*       */       
/*  5676 */       performer.getCommunicator().sendNormalServerMessage("Eww.. the " + drink
/*  5677 */           .getName() + " tastes funny and won't quench your thirst at all!");
/*  5678 */       done = true;
/*       */     }
/*       */     else {
/*       */       
/*  5682 */       if (performer.isPlayer() && ((Player)performer).getAlcohol() > 90.0F)
/*       */       {
/*  5684 */         if (Server.rand.nextInt(100) < ((Player)performer).getAlcohol()) {
/*       */           
/*  5686 */           performer.getCommunicator()
/*  5687 */             .sendNormalServerMessage("You spill the " + drink.getName() + " out. Who cares?");
/*  5688 */           Server.getInstance().broadCastAction(performer
/*  5689 */               .getName() + " throws the " + drink.getName() + " over " + performer.getHisHerItsString() + " shoulder instead of drinking it.", performer, 3);
/*       */           
/*  5691 */           Items.destroyItem(drink.getWurmId());
/*  5692 */           return true;
/*       */         } 
/*       */       }
/*  5695 */       if (counter == 1.0F) {
/*       */         
/*  5697 */         if (temp > 600 && template != 425) {
/*       */           
/*  5699 */           performer.getCommunicator().sendNormalServerMessage("The " + drink.getName() + " is too hot to drink.");
/*  5700 */           return true;
/*       */         } 
/*  5702 */         if (drink.isMilk() && drink.isZombiefied()) {
/*       */           
/*  5704 */           if (performer.getKingdomTemplateId() != 3) {
/*       */             
/*  5706 */             performer.getCommunicator().sendNormalServerMessage("Eww.. the " + drink
/*  5707 */                 .getName() + " tastes horrible! You can't drink it.");
/*  5708 */             return true;
/*       */           } 
/*       */ 
/*       */           
/*  5712 */           performer.getCommunicator().sendNormalServerMessage("Hmm.. the " + drink
/*  5713 */               .getName() + " tastes very special.");
/*       */         } 
/*       */         
/*  5716 */         int time = 10;
/*  5717 */         time = (int)Math.min(performer.getStatus().getThirst() / 6554.0F * qlevel / 100.0F * 10.0F, drink
/*  5718 */             .getWeightGrams() / 200.0F);
/*  5719 */         Server.getInstance().broadCastAction(performer.getName() + " drinks some " + drink.getName() + ".", performer, 3);
/*       */         
/*  5721 */         performer.sendActionControl("Drinking " + drink.getName(), true, time);
/*       */         
/*  5723 */         if (qlevel > 50.0F) {
/*       */           
/*  5725 */           if (drink.getTemplateId() == 128) {
/*       */             
/*  5727 */             if (temp < 300)
/*       */             {
/*  5729 */               if (drink.isSalted())
/*       */               {
/*  5731 */                 performer.getCommunicator().sendNormalServerMessage("The water is slightly salty but still cools you down.");
/*       */               
/*       */               }
/*       */               else
/*       */               {
/*  5736 */                 performer.getCommunicator().sendNormalServerMessage("The water is refreshing and it cools you down.");
/*       */               
/*       */               }
/*       */ 
/*       */             
/*       */             }
/*  5742 */             else if (drink.isSalted())
/*       */             {
/*  5744 */               performer.getCommunicator().sendNormalServerMessage("The water isn't exactly cold and has a salty taste.");
/*       */             
/*       */             }
/*       */             else
/*       */             {
/*  5749 */               performer.getCommunicator().sendNormalServerMessage("The water isn't exactly cold but still refreshens you.");
/*       */             }
/*       */           
/*       */           }
/*       */           else {
/*       */             
/*  5755 */             performer.getCommunicator().sendNormalServerMessage("The " + drink
/*  5756 */                 .getName() + " tastes " + drink.getTasteString());
/*       */           } 
/*  5758 */         } else if (drink.isSalted()) {
/*  5759 */           performer.getCommunicator().sendNormalServerMessage("The water has a salty taste.");
/*       */         } 
/*       */       } 
/*       */       
/*  5763 */       if (drink.getTemplateId() == 427 || drink.isAlcohol()) {
/*       */         
/*  5765 */         performer.getStatus().modifyStamina((int)(qlevel * (20 + drink.getRarity() * 10)));
/*  5766 */         if (drink.isAlcohol())
/*       */         {
/*  5768 */           if (performer.isPlayer()) {
/*       */             
/*  5770 */             Player player = (Player)performer;
/*  5771 */             float drinkMod = 1.0F;
/*  5772 */             drinkMod += drink.getCurrentQualityLevel() * 0.005F;
/*  5773 */             if (drink.getWeightGrams() < 200)
/*       */             {
/*       */               
/*  5776 */               drinkMod *= drink.getWeightGrams() / 200.0F;
/*       */             }
/*       */             
/*  5779 */             float addAlcohol = drink.getAlcoholStrength() * 0.2F * drinkMod;
/*  5780 */             player.setAlcohol(((Player)performer).getAlcohol() + addAlcohol);
/*       */             
/*  5782 */             if (player.getAlcohol() > 20.0F)
/*       */             {
/*  5784 */               if (player.getSaveFile().setAlcoholTime(player.getAlcoholAddiction() + 10L))
/*       */               {
/*  5786 */                 player.getCommunicator().sendNormalServerMessage("You have just received the title '" + Titles.Title.Alcoholic
/*  5787 */                     .getName(player.isNotFemale()) + "'!");
/*       */               }
/*       */             }
/*  5790 */             if (player.getAlcohol() == 100.0F) {
/*       */               
/*  5792 */               performer.getCommunicator().sendNormalServerMessage("You made it to the top! You are perfectly drunk!");
/*       */               
/*  5794 */               Server.getInstance().broadCastAction(performer
/*  5795 */                   .getName() + " hits the record in drunkenness! " + performer.getHeSheItString() + " is perfectly drunk and can't drink any more!", performer, 3);
/*       */               
/*  5797 */               player.addTitle(Titles.Title.Drunkard);
/*  5798 */               player.achievement(296);
/*       */             }
/*  5800 */             else if (((Player)performer).getAlcohol() >= 95.0F) {
/*       */               
/*  5802 */               performer.getCommunicator().sendNormalServerMessage("You are setting some kind of record.");
/*  5803 */               Server.getInstance().broadCastAction(performer
/*  5804 */                   .getName() + " has that dead, watery look in " + performer.getHisHerItsString() + " eyes now.", performer, 3);
/*       */             
/*       */             }
/*  5807 */             else if (player.getAlcohol() >= 90.0F) {
/*       */               
/*  5809 */               performer.getCommunicator().sendNormalServerMessage("You can barely walk.");
/*  5810 */               Server.getInstance().broadCastAction(performer
/*  5811 */                   .getName() + " looks concentrated and sways back and forth.", performer, 3);
/*  5812 */               performer.getMovementScheme().setDrunkMod(true);
/*       */             }
/*  5814 */             else if (player.getAlcohol() >= 60.0F) {
/*       */               
/*  5816 */               performer.getCommunicator().sendNormalServerMessage("You are really really drunk.");
/*  5817 */               Server.getInstance().broadCastAction(performer.getName() + " is starting to drool.", performer, 3);
/*       */             }
/*  5819 */             else if (player.getAlcohol() >= 30.0F) {
/*       */               
/*  5821 */               performer.getCommunicator().sendNormalServerMessage("You are drunk.");
/*  5822 */               Server.getInstance().broadCastAction(performer.getName() + " is verifyibly drunk now.", performer, 3);
/*       */ 
/*       */ 
/*       */               
/*  5826 */               performer.achievement(567);
/*       */             }
/*  5828 */             else if (player.getAlcohol() >= 20.0F) {
/*       */               
/*  5830 */               performer.getCommunicator().sendNormalServerMessage("You are getting drunk.");
/*  5831 */               Server.getInstance().broadCastAction(performer.getName() + " suddenly giggles uncontrollably.", performer, 3);
/*       */             
/*       */             }
/*  5834 */             else if (player.getAlcohol() >= 10.0F) {
/*       */               
/*  5836 */               performer.getCommunicator().sendNormalServerMessage("You are tipsy.");
/*       */             } 
/*       */           } 
/*       */         }
/*       */       } 
/*  5841 */       float complexity = drink.getFoodComplexity() * 20.0F;
/*  5842 */       if (weight * qlevel / 10.0F <= 2000.0F) {
/*       */         
/*  5844 */         performer.getStatus().modifyThirst((int)(-(weight * qlevel / 20000.0F) * 6554.0F), drink
/*  5845 */             .getCaloriesByWeight(weight) * complexity, drink
/*  5846 */             .getCarbsByWeight(weight) * complexity, drink
/*  5847 */             .getFatsByWeight(weight) * complexity, drink
/*  5848 */             .getProteinsByWeight(weight) * complexity);
/*       */         
/*  5850 */         AffinitiesTimed.addTimedAffinityFromBonus(performer, weight, drink);
/*  5851 */         done = true;
/*  5852 */         Items.destroyItem(drink.getWurmId());
/*  5853 */         if (drink.isSource()) {
/*  5854 */           performer.modifyKarma(weight);
/*       */         }
/*       */       } else {
/*       */         
/*  5858 */         if (drink.isMilk() && drink.isZombiefied() && performer.getStatus().getThirst() > 1000)
/*       */         {
/*  5860 */           if (performer.getKingdomId() == 3 && Server.rand.nextInt(10) == 0)
/*       */           {
/*  5862 */             performer.healRandomWound((int)(qlevel / 10.0F));
/*       */           }
/*       */         }
/*  5865 */         performer.getStatus().modifyThirst((int)(-6554.0F * qlevel / 100.0F), drink
/*  5866 */             .getCaloriesByWeight(200) * complexity, drink
/*  5867 */             .getCarbsByWeight(200) * complexity, drink
/*  5868 */             .getFatsByWeight(200) * complexity, drink
/*  5869 */             .getProteinsByWeight(200) * complexity);
/*       */         
/*  5871 */         AffinitiesTimed.addTimedAffinityFromBonus(performer, 200, drink);
/*  5872 */         drink.setWeight(weight - 200, false);
/*  5873 */         if (drink.isSource())
/*  5874 */           performer.modifyKarma(200); 
/*  5875 */         if (drink.getWeightGrams() <= 0)
/*       */         {
/*  5877 */           Items.destroyItem(drink.getWurmId());
/*       */         }
/*       */       } 
/*  5880 */       if (act.mayPlaySound())
/*       */       {
/*  5882 */         SoundPlayer.playSound("sound.liquid.drink", performer, 1.0F);
/*       */       }
/*       */     } 
/*  5885 */     if (performer.getStatus().getThirst() <= 1)
/*  5886 */       done = true; 
/*  5887 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final void addAlcohol(Creature performer) {
/*  5892 */     if (performer.isPlayer()) {
/*       */       
/*  5894 */       Player player = (Player)performer;
/*  5895 */       player.setAlcohol(((Player)performer).getAlcohol() + 3.0F);
/*       */       
/*  5897 */       if (player.getAlcohol() > 20.0F)
/*       */       {
/*  5899 */         if (player.getSaveFile().setAlcoholTime(player.getAlcoholAddiction() + 10L))
/*       */         {
/*  5901 */           player.getCommunicator().sendNormalServerMessage("You have just received the title '" + Titles.Title.Alcoholic.getName(player.isNotFemale()) + "'!");
/*       */         }
/*       */       }
/*  5904 */       if (player.getAlcohol() == 100.0F) {
/*       */         
/*  5906 */         performer.getCommunicator().sendNormalServerMessage("You made it to the top! You are perfectly drunk!");
/*  5907 */         Server.getInstance().broadCastAction(performer.getName() + " hits the record in drunkenness! " + performer.getHeSheItString() + " is perfectly drunk and can't drink any more!", performer, 3);
/*  5908 */         player.addTitle(Titles.Title.Drunkard);
/*  5909 */         player.achievement(296);
/*       */       }
/*  5911 */       else if (player.getAlcohol() >= 95.0F) {
/*       */         
/*  5913 */         performer.getCommunicator().sendNormalServerMessage("You are setting some kind of record.");
/*  5914 */         Server.getInstance().broadCastAction(performer.getName() + " has that dead, watery look in " + performer.getHisHerItsString() + " eyes now.", performer, 3);
/*       */       }
/*  5916 */       else if (player.getAlcohol() >= 90.0F) {
/*       */         
/*  5918 */         performer.getCommunicator().sendNormalServerMessage("You can barely walk.");
/*  5919 */         Server.getInstance().broadCastAction(performer.getName() + " looks concentrated and sways back and forth.", performer, 3);
/*  5920 */         performer.getMovementScheme().setDrunkMod(true);
/*       */       }
/*  5922 */       else if (player.getAlcohol() >= 60.0F) {
/*       */         
/*  5924 */         performer.getCommunicator().sendNormalServerMessage("You are really really drunk.");
/*  5925 */         Server.getInstance().broadCastAction(performer.getName() + " is starting to drool.", performer, 3);
/*       */       }
/*  5927 */       else if (player.getAlcohol() >= 30.0F) {
/*       */         
/*  5929 */         performer.getCommunicator().sendNormalServerMessage("You are drunk.");
/*  5930 */         Server.getInstance().broadCastAction(performer.getName() + " is verifyibly drunk now.", performer, 3);
/*       */       }
/*  5932 */       else if (player.getAlcohol() >= 20.0F) {
/*       */         
/*  5934 */         performer.getCommunicator().sendNormalServerMessage("You are getting drunk.");
/*  5935 */         Server.getInstance().broadCastAction(performer.getName() + " suddenly giggles uncontrollably.", performer, 3);
/*       */       }
/*  5937 */       else if (player.getAlcohol() >= 10.0F) {
/*       */         
/*  5939 */         performer.getCommunicator().sendNormalServerMessage("You are tipsy.");
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static boolean drink(Creature performer, int tilex, int tiley, int tile, float counter, Action act) {
/*  5947 */     boolean done = false;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  5962 */     if (performer.isPlayer() && ((Player)performer).getAlcohol() > 90.0F)
/*       */     {
/*  5964 */       if (Server.rand.nextInt(100) < ((Player)performer).getAlcohol()) {
/*       */         
/*  5966 */         performer.getCommunicator().sendNormalServerMessage("You fall into the water and crawl back up all soaked. Who gives a damn?");
/*       */         
/*  5968 */         SoundPlayer.playSound("sound.fish.splash", performer, 10.0F);
/*  5969 */         return true;
/*       */       } 
/*       */     }
/*  5972 */     if (counter == 1.0F) {
/*       */       
/*  5974 */       int time = 10;
/*  5975 */       time = (int)(performer.getStatus().getThirst() / 6554.0F) * 10;
/*  5976 */       if (WaterType.isBrackish(tilex, tiley, performer.isOnSurface())) {
/*       */         
/*  5978 */         Server.getInstance().broadCastAction(performer.getNameWithGenus() + " drinks some salty water.", performer, 3);
/*  5979 */         performer.getCommunicator().sendNormalServerMessage("The water is slightly salty but still cools you down.");
/*       */       }
/*       */       else {
/*       */         
/*  5983 */         Server.getInstance().broadCastAction(performer.getNameWithGenus() + " drinks some water.", performer, 3);
/*  5984 */         performer.getCommunicator().sendNormalServerMessage("The water is refreshing and it cools you down.");
/*       */       } 
/*  5986 */       performer.sendActionControl("Drinking water", true, time);
/*       */     } 
/*  5988 */     performer.getStatus().modifyThirst(-6554.0F);
/*       */     
/*  5990 */     if (act.mayPlaySound())
/*       */     {
/*  5992 */       SoundPlayer.playSound("sound.liquid.drink", performer, 10.0F);
/*       */     }
/*  5994 */     if (performer.getStatus().getThirst() <= 1)
/*       */     {
/*  5996 */       done = true;
/*       */     }
/*  5998 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean startDragging(Action act, Creature performer, Item dragged) {
/*  6003 */     if (dragged.isVehicle()) {
/*       */       
/*  6005 */       Vehicle vehic = Vehicles.getVehicle(dragged);
/*  6006 */       if (vehic.pilotId != -10L) {
/*       */         
/*  6008 */         performer.getCommunicator().sendNormalServerMessage("The " + dragged
/*  6009 */             .getName() + " can not be dragged right now.");
/*  6010 */         return true;
/*       */       } 
/*  6012 */       if (dragged.isMooredBoat()) {
/*       */         
/*  6014 */         performer.getCommunicator().sendNormalServerMessage("The " + dragged
/*  6015 */             .getName() + " is moored and can not be dragged right now.");
/*  6016 */         return true;
/*       */       } 
/*  6018 */       if (dragged.getWurmId() == performer.getVehicle()) {
/*       */         
/*  6020 */         performer.getCommunicator().sendNormalServerMessage("You can not drag the " + dragged.getName() + " now.");
/*  6021 */         return true;
/*       */       } 
/*  6023 */       if (dragged.getLockId() != -10L && VehicleBehaviour.hasKeyForVehicle(performer, dragged) && 
/*  6024 */         !VehicleBehaviour.mayDriveVehicle(performer, dragged, act)) {
/*       */         
/*  6026 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to drag the " + dragged
/*  6027 */             .getName() + ".");
/*  6028 */         return true;
/*       */       } 
/*  6030 */       if (vehic.draggers != null && vehic.draggers.size() > 0) {
/*       */         
/*  6032 */         performer.getCommunicator().sendNormalServerMessage("The " + dragged
/*  6033 */             .getName() + " won't budge since it is already dragged.");
/*  6034 */         return true;
/*       */       } 
/*  6036 */       if (performer.isPlayer() && 
/*  6037 */         System.currentTimeMillis() - ((Player)performer).lastStoppedDragging < 2000L) {
/*       */         
/*  6039 */         performer.getCommunicator().sendNormalServerMessage("You need to take a breath first.");
/*  6040 */         return true;
/*       */       } 
/*       */     } 
/*  6043 */     if (dragged.getTemplateId() == 445)
/*       */     {
/*  6045 */       if (Servers.localServer.PVPSERVER && performer.getBodyStrength().getRealKnowledge() < 21.0D) {
/*       */         
/*  6047 */         performer.getCommunicator().sendNormalServerMessage("The " + dragged.getName() + " is too heavy.");
/*  6048 */         return true;
/*       */       } 
/*       */     }
/*  6051 */     if (!performer.isWithinDistanceTo(dragged.getPosX(), dragged.getPosY(), dragged.getPosZ(), 4.0F)) {
/*       */       
/*  6053 */       performer.getCommunicator().sendNormalServerMessage("You are too far away.");
/*  6054 */       return true;
/*       */     } 
/*       */     
/*  6057 */     if (!Items.isItemDragged(dragged)) {
/*       */       
/*  6059 */       if (checkIfStealing(dragged, performer, act)) {
/*       */         
/*  6061 */         if (Action.checkLegalMode(performer))
/*  6062 */           return true; 
/*  6063 */         if (!performer.maySteal()) {
/*       */           
/*  6065 */           performer.getCommunicator().sendNormalServerMessage("You need more body control to steal things.");
/*  6066 */           return true;
/*       */         } 
/*  6068 */         if (dragged.getItems().size() > 0) {
/*       */           
/*  6070 */           performer.getCommunicator().sendNormalServerMessage("You must empty the " + dragged
/*  6071 */               .getName() + " before you steal it.");
/*  6072 */           return true;
/*       */         } 
/*       */         
/*  6075 */         if (setTheftEffects(performer, act, dragged))
/*  6076 */           return true; 
/*       */       } 
/*  6078 */       if (dragged.isBoat() && dragged.getCurrentQualityLevel() < 10.0F) {
/*       */         
/*  6080 */         performer.getCommunicator().sendNormalServerMessage("The " + dragged
/*  6081 */             .getName() + " is in too poor shape to be used.");
/*  6082 */         return true;
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  6098 */       TileEvent.log(dragged.getTileX(), dragged.getTileY(), dragged.isOnSurface() ? 0 : -1, performer.getWurmId(), 74);
/*       */       
/*  6100 */       Items.startDragging(performer, dragged);
/*       */     } else {
/*       */       
/*  6103 */       performer.getCommunicator().sendNormalServerMessage("That item is already being dragged by someone.");
/*  6104 */     }  return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public static boolean stopDragging(Creature performer, Item dragged) {
/*  6109 */     BlockingResult result = Blocking.getBlockerBetween(performer, dragged, 4);
/*  6110 */     if (result == null)
/*       */     {
/*  6112 */       if (performer.getDraggedItem() == dragged) {
/*       */         
/*  6114 */         Items.stopDragging(dragged);
/*  6115 */         if (performer.getVisionArea() != null)
/*  6116 */           performer.getVisionArea().broadCastUpdateSelectBar(dragged.getWurmId()); 
/*       */       } 
/*       */     }
/*  6119 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean yoyo(Creature performer, Item yoyo, float counter, Action act) {
/*  6124 */     boolean toReturn = false;
/*  6125 */     Skills skills = performer.getSkills();
/*  6126 */     Skill yoyosk = null;
/*  6127 */     String succ = "but fail";
/*  6128 */     String point = ".";
/*  6129 */     String trick = "hurricane";
/*  6130 */     double diff = 5.0D;
/*  6131 */     double check = 0.0D;
/*       */     
/*       */     try {
/*  6134 */       yoyosk = skills.getSkill(10050);
/*       */     }
/*  6136 */     catch (NoSuchSkillException nsk) {
/*       */       
/*  6138 */       yoyosk = skills.learn(10050, 1.0F);
/*       */     } 
/*       */     
/*  6141 */     if (counter == 1.0F) {
/*       */       
/*  6143 */       performer.getCommunicator().sendNormalServerMessage("You start to spin your " + yoyo.getName() + ".");
/*  6144 */       Server.getInstance().broadCastAction(performer
/*  6145 */           .getName() + " starts to spin " + performer.getHisHerItsString() + " " + yoyo.getName() + ".", performer, 5);
/*       */       
/*  6147 */       performer.sendActionControl(Actions.actionEntrys[190].getVerbString(), true, 800);
/*       */     }
/*  6149 */     else if (act.currentSecond() == 5) {
/*       */       
/*  6151 */       performer.getCommunicator().sendNormalServerMessage("The " + yoyo.getName() + " has good speed now.");
/*  6152 */       Server.getInstance().broadCastAction("The " + yoyo.getName() + " has good speed now.", performer, 5);
/*       */     }
/*  6154 */     else if (act.currentSecond() == 10) {
/*       */       
/*  6156 */       diff = (5 - yoyo.getRarity());
/*  6157 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6158 */       trick = "sun";
/*       */     }
/*  6160 */     else if (act.currentSecond() == 20) {
/*       */       
/*  6162 */       diff = (10 - yoyo.getRarity());
/*  6163 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6164 */       trick = "grind";
/*       */     }
/*  6166 */     else if (act.currentSecond() == 30) {
/*       */       
/*  6168 */       diff = (20 - yoyo.getRarity());
/*  6169 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6170 */       trick = "cradle";
/*       */     }
/*  6172 */     else if (act.currentSecond() == 40) {
/*       */       
/*  6174 */       diff = (40 - yoyo.getRarity());
/*  6175 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6176 */       trick = "suicide basilisk";
/*       */     }
/*  6178 */     else if (act.currentSecond() == 50) {
/*       */       
/*  6180 */       diff = (60.0F - yoyo.getRarity() * 1.5F);
/*  6181 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6182 */       trick = "whip";
/*       */     }
/*  6184 */     else if (act.currentSecond() == 60) {
/*       */       
/*  6186 */       diff = (70 - yoyo.getRarity() * 2);
/*  6187 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6188 */       trick = "orbit";
/*       */     }
/*  6190 */     else if (act.currentSecond() == 70) {
/*       */       
/*  6192 */       diff = (80.0F - yoyo.getRarity() * 2.5F);
/*  6193 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6194 */       trick = "orbit over back";
/*       */     }
/*  6196 */     else if (act.currentSecond() == 80) {
/*       */       
/*  6198 */       diff = (90 - yoyo.getRarity() * 3);
/*  6199 */       check = yoyosk.skillCheck(diff, yoyo, 0.0D, false, 10.0F);
/*  6200 */       trick = "dragon knot";
/*  6201 */       toReturn = true;
/*       */     } 
/*  6203 */     boolean destroyed = false;
/*  6204 */     if (check != 0.0D) {
/*       */       
/*  6206 */       if (check > 0.0D) {
/*       */         
/*  6208 */         succ = "and succeed";
/*  6209 */         point = "!";
/*       */       } else {
/*       */         
/*  6212 */         toReturn = true;
/*  6213 */       }  performer.getCommunicator().sendNormalServerMessage("You try the '" + trick + "' " + succ + point);
/*  6214 */       Server.getInstance().broadCastAction(performer.getName() + " tries the " + trick + " " + succ + "s" + point, performer, 5);
/*       */       
/*  6216 */       if (yoyo.setDamage(yoyo.getDamage() + 0.005F * yoyo.getDamageModifier())) {
/*       */         
/*  6218 */         destroyed = true;
/*  6219 */         toReturn = true;
/*       */       } 
/*       */     } 
/*  6222 */     if (!destroyed && toReturn) {
/*       */       
/*  6224 */       performer.getCommunicator().sendNormalServerMessage("You reel the " + yoyo.getName() + " in.");
/*  6225 */       Server.getInstance().broadCastAction(performer.getName() + " reels the " + yoyo.getName() + " in.", performer, 5);
/*       */     } 
/*  6227 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final int getImproveTemplateId(Item item) {
/*  6232 */     if (item.isNoImprove()) {
/*  6233 */       return -10;
/*       */     }
/*  6235 */     byte material = getImproveMaterial(item);
/*       */     
/*  6237 */     if (material == 0) {
/*  6238 */       return -10;
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  6247 */     return Materials.getTemplateIdForMaterial(material);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static final int getImproveSkill(Item item) {
/*  6253 */     int material = item.getMaterial();
/*  6254 */     if (material == 0) {
/*  6255 */       return -10;
/*       */     }
/*       */     
/*  6258 */     CreationEntry entry = CreationMatrix.getInstance().getCreationEntry(item.getTemplateId());
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  6263 */     if (entry != null) {
/*       */       
/*  6265 */       if (item.getTemplateId() == 623 && (material == 7 || material == 8 || material == 96)) {
/*  6266 */         return 10043;
/*       */       }
/*  6268 */       return entry.getPrimarySkill();
/*       */     } 
/*  6270 */     if (item.getTemplateId() == 430 || item.getTemplateId() == 528 || item
/*  6271 */       .getTemplateId() == 638)
/*  6272 */       return 1013; 
/*  6273 */     if (item.getTemplate().isStatue()) {
/*  6274 */       return 10074;
/*       */     }
/*  6276 */     return -10;
/*       */   }
/*       */ 
/*       */   
/*       */   private static final int getImproveSkill(byte material, int templateId) {
/*  6281 */     if (material == 0) {
/*  6282 */       return -10;
/*       */     }
/*       */     
/*  6285 */     CreationEntry entry = CreationMatrix.getInstance().getCreationEntry(templateId);
/*  6286 */     if (entry != null)
/*  6287 */       return entry.getPrimarySkill(); 
/*  6288 */     if (templateId == 430 || templateId == 528 || templateId == 638)
/*       */     {
/*  6290 */       return 1013;
/*       */     }
/*  6292 */     return -10;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static void triggerImproveAchievements(Creature performer, Item target, Skill improve, boolean wasHighest, float oldQL) {
/*  6298 */     performer.achievement(205);
/*       */     
/*  6300 */     if (target.getQualityLevel() > improve.getKnowledge(0.0D) && improve.getKnowledge(0.0D) > 30.0D)
/*  6301 */       performer.achievement(217); 
/*  6302 */     if (!wasHighest && 
/*  6303 */       Items.isHighestQLForTemplate(target.getTemplateId(), target.getQualityLevel(), target.getWurmId(), false)) {
/*  6304 */       performer.achievement(317);
/*       */     }
/*       */     
/*  6307 */     if (target.getQualityLevel() >= 99.0F && oldQL < 99.0F) {
/*  6308 */       performer.achievement(222);
/*  6309 */     } else if (target.getQualityLevel() >= 90.0F && oldQL < 90.0F) {
/*  6310 */       performer.achievement(221);
/*  6311 */     } else if (target.getQualityLevel() >= 70.0F && oldQL < 70.0F) {
/*       */       
/*  6313 */       performer.achievement(220);
/*  6314 */       if (target.isTool()) {
/*  6315 */         performer.achievement(565);
/*       */       }
/*  6317 */     } else if (target.getQualityLevel() >= 50.0F && oldQL < 50.0F) {
/*       */       
/*  6319 */       performer.achievement(219);
/*  6320 */       if (target.isTool())
/*  6321 */         performer.achievement(543); 
/*  6322 */       if (target.isWeapon()) {
/*  6323 */         performer.achievement(554);
/*       */       }
/*  6325 */     } else if (target.getQualityLevel() >= 15.0F && oldQL < 15.0F) {
/*       */       
/*  6327 */       if (target.isMetal()) {
/*  6328 */         performer.achievement(520);
/*       */       }
/*       */     } 
/*  6331 */     if (target.isArmour() && target.getQualityLevel() >= 80.0F && oldQL < 80.0F) {
/*  6332 */       performer.achievement(597);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean improveItem(Action act, Creature performer, Item source, Item target, float counter) {
/*  6338 */     boolean toReturn = false;
/*       */     
/*  6340 */     boolean insta = (performer.getPower() >= 5);
/*       */     
/*  6342 */     if (counter == 0.0F || counter == 1.0F || act.justTickedSecond()) {
/*       */       
/*  6344 */       if (source.getWurmId() == target.getWurmId()) {
/*       */         
/*  6346 */         performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + target
/*  6347 */             .getName() + " using itself as a tool.");
/*  6348 */         return true;
/*       */       } 
/*  6350 */       if (!target.isRepairable()) {
/*       */         
/*  6352 */         performer.getCommunicator().sendNormalServerMessage("You cannot improve that item.");
/*  6353 */         return true;
/*       */       } 
/*  6355 */       if (target.getParentId() != -10L) {
/*       */         
/*       */         try {
/*       */           
/*  6359 */           ItemTemplate temp = target.getRealTemplate();
/*  6360 */           if (temp != null && !temp.isVehicle()) {
/*       */             
/*  6362 */             Item parent = target.getParent();
/*  6363 */             if (parent.getSizeX() < temp.getSizeX() || parent.getSizeY() < temp.getSizeY() || parent
/*  6364 */               .getSizeZ() <= temp.getSizeZ())
/*       */             {
/*  6366 */               if (parent.getTemplateId() != 177 && parent.getTemplateId() != 0)
/*       */               {
/*  6368 */                 performer.getCommunicator().sendNormalServerMessage("It's too tight to try and work on the " + target
/*  6369 */                     .getName() + " in the " + parent
/*  6370 */                     .getName() + ".");
/*  6371 */                 return true;
/*       */               }
/*       */             
/*       */             }
/*       */           } 
/*  6376 */         } catch (NoSuchItemException noSuchItemException) {}
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  6382 */       if (target.creationState != 0) {
/*       */         
/*  6384 */         performer.getCommunicator().sendNormalServerMessage("You can not improve the " + target
/*  6385 */             .getName() + " by adding more material right now.");
/*  6386 */         return true;
/*       */       } 
/*  6388 */       if (!insta) {
/*       */         
/*  6390 */         if (target.getDamage() > 0.0F) {
/*       */           
/*  6392 */           performer.getCommunicator().sendNormalServerMessage("Repair the " + target
/*  6393 */               .getName() + " before you try to improve it.");
/*  6394 */           return true;
/*       */         } 
/*  6396 */         if (target.isMetal() && !target.isNoTake())
/*       */         {
/*  6398 */           if (target.getTemperature() < 3500) {
/*       */             
/*  6400 */             performer.getCommunicator().sendNormalServerMessage("Metal needs to be glowing hot while smithing.");
/*  6401 */             return true;
/*       */           } 
/*       */         }
/*  6404 */         if (source.isCombine() && source.isMetal())
/*       */         {
/*  6406 */           if (source.getTemperature() < 3500) {
/*       */             
/*  6408 */             performer.getCommunicator().sendNormalServerMessage("Metal needs to be glowing hot while smithing.");
/*  6409 */             return true;
/*       */           } 
/*       */         }
/*       */       } 
/*       */     } 
/*  6414 */     Skills skills = performer.getSkills();
/*  6415 */     Skill improve = null;
/*  6416 */     int skillNum = getImproveSkill(target);
/*  6417 */     if (skillNum == -10 || target.isNewbieItem() || target.isChallengeNewbieItem()) {
/*       */       
/*  6419 */       performer.getCommunicator().sendNormalServerMessage("You cannot improve that item.");
/*  6420 */       return true;
/*       */     } 
/*  6422 */     int time = 1000;
/*  6423 */     int templateId = getImproveTemplateId(target);
/*       */     
/*  6425 */     if (source.getTemplateId() == templateId) {
/*       */ 
/*       */       
/*       */       try {
/*  6429 */         improve = skills.getSkill(skillNum);
/*       */       }
/*  6431 */       catch (NoSuchSkillException nss) {
/*       */         
/*  6433 */         improve = skills.learn(skillNum, 1.0F);
/*       */       } 
/*  6435 */       Skill secondarySkill = null;
/*       */       
/*       */       try {
/*  6438 */         secondarySkill = skills.getSkill(source.getPrimarySkill());
/*       */       }
/*  6440 */       catch (Exception ex) {
/*       */ 
/*       */         
/*       */         try {
/*  6444 */           secondarySkill = skills.learn(source.getPrimarySkill(), 1.0F);
/*       */         }
/*  6446 */         catch (Exception exception) {}
/*       */       } 
/*       */ 
/*       */ 
/*       */       
/*  6451 */       double power = 0.0D;
/*  6452 */       double bonus = 0.0D;
/*       */ 
/*       */       
/*  6455 */       if (performer.isPriest())
/*  6456 */         bonus = -20.0D; 
/*  6457 */       float runeModifier = 1.0F;
/*  6458 */       if (target.getSpellEffects() != null)
/*       */       {
/*  6460 */         runeModifier = target.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_IMPPERCENT);
/*       */       }
/*  6462 */       float imbueEnhancement = 1.0F + source.getSkillSpellImprovement(skillNum) / 100.0F;
/*  6463 */       double improveBonus = 0.23047D * imbueEnhancement * runeModifier;
/*  6464 */       float improveItemBonus = ItemBonus.getImproveSkillMaxBonus(performer);
/*  6465 */       double max = improve.getKnowledge(0.0D) * improveItemBonus + (100.0D - improve.getKnowledge(0.0D) * improveItemBonus) * improveBonus;
/*       */       
/*  6467 */       double diff = Math.max(0.0D, max - target.getQualityLevel());
/*  6468 */       float skillgainMod = 1.0F;
/*       */ 
/*       */ 
/*       */       
/*  6472 */       if (diff <= 0.0D)
/*  6473 */         skillgainMod = 2.0F; 
/*  6474 */       if (counter == 1.0F) {
/*       */         
/*  6476 */         if (source.isCombine() || templateId == 9)
/*       */         {
/*  6478 */           if (source.getCurrentQualityLevel() <= target.getQualityLevel()) {
/*       */             
/*  6480 */             performer.getCommunicator().sendNormalServerMessage("The " + source
/*  6481 */                 .getName() + " is in too poor shape to improve the " + target.getName() + ".");
/*  6482 */             return true;
/*       */           } 
/*       */         }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  6504 */         performer.getCommunicator().sendNormalServerMessage("You start to improve the " + target.getName() + ".");
/*  6505 */         Server.getInstance().broadCastAction(performer
/*  6506 */             .getName() + " starts to improve " + target.getNameWithGenus() + ".", performer, 5);
/*       */         
/*  6508 */         time = Actions.getImproveActionTime(performer, source);
/*  6509 */         performer.sendActionControl(Actions.actionEntrys[192].getVerbString(), true, time);
/*  6510 */         act.setTimeLeft(time);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  6524 */         if (performer.getDeity() != null && performer.getDeity().isRepairer() && 
/*  6525 */           performer.getFaith() >= 80.0F && performer.getFavor() >= 40.0F) {
/*  6526 */           bonus += 10.0D;
/*       */         }
/*  6528 */         power = improve.skillCheck(target.getQualityLevel(), source, bonus, true, 1.0F);
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  6533 */         double mod = ((100.0F - target.getQualityLevel()) / 20.0F / 100.0F * (Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat()) / 2.0F);
/*       */         
/*  6535 */         if (power < 0.0D)
/*       */         {
/*  6537 */           act.setFailSecond((int)Math.max(20.0F, time * Server.rand.nextFloat()));
/*  6538 */           act.setPower((float)(-mod * Math.max(1.0D, diff)));
/*       */         
/*       */         }
/*       */         else
/*       */         {
/*       */           
/*  6544 */           if (diff <= 0.0D) {
/*  6545 */             mod *= 0.009999999776482582D;
/*       */           }
/*  6547 */           double regain = 1.0D;
/*  6548 */           if (target.getQualityLevel() < target.getOriginalQualityLevel())
/*  6549 */             regain = 2.0D; 
/*  6550 */           diff *= regain;
/*  6551 */           int tid = target.getTemplateId();
/*       */ 
/*       */           
/*  6554 */           if (target.isArmour() || target.isCreatureWearableOnly() || target.isWeapon() || target.isShield() || tid == 455 || tid == 454 || tid == 456 || tid == 453 || tid == 451 || tid == 452)
/*       */           {
/*       */             
/*  6557 */             mod *= 2.0D; } 
/*  6558 */           if (tid == 455 || tid == 454 || tid == 456 || tid == 453 || tid == 451 || tid == 452)
/*       */           {
/*  6560 */             mod *= 2.0D; } 
/*  6561 */           Titles.Title title = performer.getTitle();
/*  6562 */           if (title != null && title.getSkillId() == improve.getNumber() && (target
/*  6563 */             .isArmour() || target.isCreatureWearableOnly()))
/*  6564 */             mod *= 1.2999999523162842D; 
/*  6565 */           if (target.getRarity() > 0)
/*  6566 */             mod *= (1.0F + target.getRarity() * 0.1F); 
/*  6567 */           act.setPower((float)(mod * Math.max(1.0D, diff)));
/*       */         
/*       */         }
/*       */ 
/*       */       
/*       */       }
/*       */       else {
/*       */         
/*  6575 */         time = act.getTimeLeft();
/*  6576 */         float failsec = act.getFailSecond();
/*  6577 */         power = act.getPower();
/*  6578 */         if (counter >= failsec) {
/*       */           
/*  6580 */           if (secondarySkill != null) {
/*  6581 */             bonus = Math.max(bonus, secondarySkill
/*       */                 
/*  6583 */                 .skillCheck(target.getQualityLevel(), source, bonus, false, 
/*  6584 */                   performer.isPriest() ? (counter / 3.0F) : (counter / 2.0F)));
/*       */           }
/*       */           
/*  6587 */           if (performer.isPriest())
/*  6588 */             bonus = Math.min(bonus, 0.0D); 
/*  6589 */           improve.skillCheck(target.getQualityLevel(), source, bonus, false, performer.isPriest() ? (counter / 2.0F) : counter);
/*       */           
/*  6591 */           if (power != 0.0D) {
/*       */ 
/*       */             
/*  6594 */             if (!target.isBodyPart())
/*       */             {
/*  6596 */               if (!target.isLiquid())
/*       */               {
/*  6598 */                 target.setDamage(target.getDamage() - act.getPower());
/*  6599 */                 performer.getCommunicator().sendNormalServerMessage("You damage the " + target
/*  6600 */                     .getName() + " a little.");
/*  6601 */                 Server.getInstance().broadCastAction(performer
/*  6602 */                     .getName() + " grunts as " + performer.getHeSheItString() + " damages " + target
/*  6603 */                     .getNameWithGenus() + " a little.", performer, 5);
/*       */               }
/*       */               else
/*       */               {
/*  6607 */                 performer.getCommunicator().sendNormalServerMessage("You fail.");
/*  6608 */                 Server.getInstance().broadCastAction(performer
/*  6609 */                     .getName() + " grunts as " + performer.getHeSheItString() + " fails.", performer, 5);
/*       */               }
/*       */             
/*       */             }
/*       */           }
/*       */           else {
/*       */             
/*  6616 */             performer.getCommunicator().sendNormalServerMessage("You realize you almost damaged the " + target
/*  6617 */                 .getName() + " and stop.");
/*  6618 */             Server.getInstance().broadCastAction(performer
/*  6619 */                 .getName() + " stops improving " + target.getNameWithGenus() + ".", performer, 5);
/*       */           } 
/*  6621 */           performer.getStatus().modifyStamina(-counter * 1000.0F);
/*  6622 */           return true;
/*       */         } 
/*       */       } 
/*  6625 */       if (act.mayPlaySound())
/*       */       {
/*  6627 */         sendImproveSound(performer, source, target, skillNum);
/*       */       }
/*  6629 */       if (counter * 10.0F > time || insta)
/*       */       {
/*  6631 */         if (act.getRarity() != 0)
/*       */         {
/*  6633 */           performer.playPersonalSound("sound.fx.drumroll");
/*       */         }
/*  6635 */         float maxGain = 1.0F;
/*  6636 */         byte rarity = target.getRarity();
/*       */         
/*  6638 */         float rarityChance = 0.2F;
/*  6639 */         if (target.getSpellEffects() != null)
/*       */         {
/*  6641 */           rarityChance *= target.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RARITYIMP);
/*       */         }
/*  6643 */         if (act.getRarity() > rarity && 
/*  6644 */           Server.rand.nextFloat() <= rarityChance)
/*  6645 */           rarity = act.getRarity(); 
/*  6646 */         float switchImproveChance = 1.0F;
/*  6647 */         if (source.isCombine() || source.getTemplateId() == 9 || source.getTemplateId() == 72 || source
/*  6648 */           .isDragonArmour()) {
/*       */           
/*  6650 */           float mod = 0.05F;
/*  6651 */           if (Servers.localServer.EPIC && source.isDragonArmour())
/*  6652 */             mod = 0.01F; 
/*  6653 */           int usedWeight = (int)Math.min(500.0F, Math.max(1.0F, target.getWeightGrams() * mod));
/*  6654 */           if (source.getWeightGrams() < Math.min(source.getTemplate().getWeightGrams(), usedWeight)) {
/*       */             
/*  6656 */             maxGain = Math.min(1.0F, source.getWeightGrams() / usedWeight);
/*  6657 */             switchImproveChance = source.getWeightGrams() / usedWeight;
/*       */           } 
/*  6659 */           source.setWeight(source.getWeightGrams() - usedWeight, true);
/*       */           
/*  6661 */           if (source.deleted && 
/*  6662 */             source.getRarity() > rarity && 
/*  6663 */             Server.rand.nextInt(100) == 0) {
/*  6664 */             rarity = source.getRarity();
/*       */           
/*       */           }
/*       */         }
/*  6668 */         else if (!source.isBodyPart() && !source.isLiquid()) {
/*  6669 */           source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*       */         } 
/*  6671 */         if (secondarySkill != null) {
/*  6672 */           bonus = Math.max(bonus, secondarySkill
/*       */               
/*  6674 */               .skillCheck(target.getQualityLevel(), source, bonus, false, skillgainMod * (
/*  6675 */                 performer.isPriest() ? (counter / 3.0F) : (counter / 2.0F))));
/*       */         }
/*       */         
/*  6678 */         if (performer.isPriest())
/*  6679 */           bonus = Math.min(bonus, 0.0D); 
/*  6680 */         improve.skillCheck(target.getQualityLevel(), source, bonus, false, skillgainMod * (
/*  6681 */             performer.isPriest() ? (counter / 2.0F) : counter));
/*       */         
/*  6683 */         power = act.getPower();
/*  6684 */         if (power > 0.0D) {
/*       */           
/*  6686 */           performer.getCommunicator().sendNormalServerMessage("You improve the " + target.getName() + " a bit.");
/*  6687 */           if (insta) {
/*  6688 */             performer.getCommunicator().sendNormalServerMessage("before: " + target
/*  6689 */                 .getQualityLevel() + " now: " + (target.getQualityLevel() + power) + " power=" + power);
/*       */           }
/*       */           
/*  6692 */           if (Servers.isThisATestServer())
/*       */           {
/*  6694 */             performer.getCommunicator().sendNormalServerMessage("switchImproveChance = " + switchImproveChance);
/*       */           }
/*  6696 */           Server.getInstance().broadCastAction(performer
/*  6697 */               .getName() + " improves " + target.getNameWithGenus() + " a bit.", performer, 5);
/*       */           
/*  6699 */           byte newState = 0;
/*       */ 
/*       */           
/*  6702 */           if (switchImproveChance >= Server.rand.nextFloat()) {
/*  6703 */             newState = (byte)Server.rand.nextInt(5);
/*       */           }
/*       */ 
/*       */           
/*  6707 */           if (Server.rand.nextFloat() * 20.0F > target.getQualityLevel())
/*  6708 */             newState = 0; 
/*  6709 */           Item toRarify = target;
/*  6710 */           if (target.getTemplateId() == 128)
/*  6711 */             toRarify = source; 
/*  6712 */           if (rarity > toRarify.getRarity()) {
/*       */             
/*  6714 */             toRarify.setRarity(rarity);
/*  6715 */             for (Item sub : toRarify.getItems()) {
/*       */               
/*  6717 */               if (sub == null) {
/*       */                 continue;
/*       */               }
/*  6720 */               if (sub.isComponentItem()) {
/*  6721 */                 sub.setRarity(rarity);
/*       */               }
/*       */             } 
/*  6724 */             if (toRarify.getRarity() > 2) {
/*  6725 */               performer.achievement(300);
/*  6726 */             } else if (toRarify.getRarity() == 1) {
/*  6727 */               performer.achievement(301);
/*  6728 */             } else if (toRarify.getRarity() == 2) {
/*  6729 */               performer.achievement(302);
/*       */             } 
/*  6731 */           }  if (newState != 0) {
/*       */             
/*  6733 */             target.setCreationState(newState);
/*  6734 */             String newString = getNeededCreationAction(getImproveMaterial(target), newState, target);
/*  6735 */             performer.getCommunicator().sendNormalServerMessage(newString);
/*       */ 
/*       */           
/*       */           }
/*  6739 */           else if (skillNum != -10) {
/*       */ 
/*       */             
/*       */             try {
/*  6743 */               ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  6744 */               performer.getCommunicator()
/*  6745 */                 .sendNormalServerMessage("The " + target
/*  6746 */                   .getName() + " could be improved with some more " + temp
/*  6747 */                   .getName() + ".");
/*       */             }
/*  6749 */             catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */           } 
/*       */ 
/*       */ 
/*       */           
/*  6754 */           boolean wasHighest = Items.isHighestQLForTemplate(target.getTemplateId(), target.getQualityLevel(), target
/*  6755 */               .getWurmId(), true);
/*  6756 */           float oldQL = target.getQualityLevel();
/*  6757 */           float modifier = 1.0F;
/*  6758 */           if (target.getSpellEffects() != null)
/*       */           {
/*  6760 */             modifier = target.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_IMPQL);
/*       */           }
/*  6762 */           modifier *= target.getMaterialImpBonus();
/*  6763 */           target.setQualityLevel(Math.min(100.0F, (float)(target.getQualityLevel() + power * maxGain * modifier)));
/*  6764 */           if (target.getQualityLevel() > target.getOriginalQualityLevel())
/*       */           {
/*  6766 */             target.setOriginalQualityLevel(target.getQualityLevel());
/*  6767 */             triggerImproveAchievements(performer, target, improve, wasHighest, oldQL);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*       */           }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*       */         }
/*       */         else {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  6790 */           if (insta) {
/*  6791 */             performer.getCommunicator().sendNormalServerMessage("Dam before: " + target
/*  6792 */                 .getDamage() + " now: " + (target.getDamage() - power) + " power=" + power);
/*       */           }
/*  6794 */           if (!target.isBodyPart())
/*       */           {
/*  6796 */             if (!target.isLiquid()) {
/*       */               
/*  6798 */               target.setDamage(target.getDamage() - (float)power);
/*       */               
/*  6800 */               performer.getCommunicator().sendNormalServerMessage("You damage the " + target
/*  6801 */                   .getName() + " a little.");
/*  6802 */               Server.getInstance().broadCastAction(performer
/*  6803 */                   .getName() + " grunts as " + performer.getHeSheItString() + " damages " + target
/*  6804 */                   .getNameWithGenus() + " a little.", performer, 5);
/*  6805 */               performer.achievement(206);
/*       */             }
/*       */             else {
/*       */               
/*  6809 */               performer.getCommunicator().sendNormalServerMessage("You fail.");
/*  6810 */               Server.getInstance().broadCastAction(performer
/*  6811 */                   .getName() + " grunts as " + performer.getHeSheItString() + " fails.", performer, 5);
/*       */             } 
/*       */           }
/*       */         } 
/*       */         
/*  6816 */         performer.getStatus().modifyStamina(-counter * 1000.0F);
/*  6817 */         toReturn = true;
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/*  6822 */       performer.getCommunicator().sendNormalServerMessage("You cannot improve the item with that.");
/*  6823 */       toReturn = true;
/*       */     } 
/*  6825 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static final boolean destroyItem(int action, Creature performer, Item destroyItem, Item target, boolean dealItems, float counter) {
/*  6831 */     if (!Methods.isActionAllowed(performer, (short)action, target)) {
/*  6832 */       return true;
/*       */     }
/*  6834 */     if (target.getTemplateId() == 1432)
/*       */     {
/*  6836 */       for (Item item : target.getItemsAsArray()) {
/*       */         
/*  6838 */         if (item.getTemplateId() == 1436)
/*       */         {
/*  6840 */           if (!item.isEmpty(true)) {
/*       */             
/*  6842 */             performer.getCommunicator().sendNormalServerMessage("You cannot destroy this coop with a creature inside.");
/*       */             
/*  6844 */             return true;
/*       */           } 
/*       */         }
/*       */       } 
/*       */     }
/*  6849 */     boolean toReturn = true;
/*  6850 */     boolean ok = false;
/*  6851 */     if (!target.isTraded()) {
/*       */       
/*  6853 */       if (target.getOwnerId() == performer.getWurmId()) {
/*       */         
/*  6855 */         ok = true;
/*       */ 
/*       */       
/*       */       }
/*  6859 */       else if (target.getZoneId() != -10L) {
/*       */         
/*  6861 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*       */           
/*  6863 */           ok = true;
/*  6864 */           if ((target.isKingdomMarker() && target.getKingdom() == performer.getKingdomId()) || (target
/*  6865 */             .isTent() && !Servers.localServer.PVPSERVER))
/*       */           {
/*  6867 */             if (performer.getWurmId() != target.lastOwner) {
/*       */               
/*  6869 */               VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target
/*  6870 */                   .isOnSurface());
/*  6871 */               if (t == null || t.getVillage() == null || t.getVillage() != performer.getCitizenVillage()) {
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*  6876 */                 performer.getCommunicator().sendNormalServerMessage("You are not allowed to destroy the " + target
/*  6877 */                     .getName() + ".");
/*  6878 */                 return true;
/*       */               } 
/*       */             } 
/*       */           }
/*       */           
/*  6883 */           if (target.isGuardTower() && target.getKingdom() != performer.getKingdomId()) {
/*       */             
/*  6885 */             GuardTower tower = Kingdoms.getTower(target);
/*  6886 */             if (tower.getGuardCount() > 0)
/*       */             {
/*  6888 */               performer.getCommunicator().sendNormalServerMessage("A nearby guard stops you with a warning.");
/*  6889 */               return true;
/*       */             }
/*       */           
/*       */           } 
/*       */         } else {
/*       */           
/*  6895 */           performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/*  6896 */           return true;
/*       */         }
/*       */       
/*       */       } 
/*       */     } else {
/*       */       
/*  6902 */       ok = false;
/*       */     } 
/*  6904 */     if (!ok) {
/*  6905 */       return true;
/*       */     }
/*  6907 */     int time = 1000;
/*       */ 
/*       */     
/*  6910 */     boolean insta = (performer.getPower() >= 2 && destroyItem.isWand());
/*  6911 */     float mod = target.getDamageModifierForItem(destroyItem);
/*       */     
/*  6913 */     if (mod <= 0.0F && !insta) {
/*       */       
/*  6915 */       performer.getCommunicator().sendNormalServerMessage("You will not do any damage to the " + target
/*  6916 */           .getName() + " with that.");
/*  6917 */       return true;
/*       */     } 
/*       */ 
/*       */     
/*  6921 */     toReturn = false;
/*  6922 */     Action act = null;
/*  6923 */     String destString = "destroy";
/*  6924 */     if (dealItems)
/*  6925 */       destString = "disassemble"; 
/*  6926 */     if (action == 757) {
/*  6927 */       destString = "pry";
/*       */     }
/*       */     try {
/*  6930 */       act = performer.getCurrentAction();
/*       */     }
/*  6932 */     catch (NoSuchActionException nsa) {
/*       */       
/*  6934 */       logger.log(Level.WARNING, "No Action for " + performer.getName() + "!", (Throwable)nsa);
/*  6935 */       return true;
/*       */     } 
/*  6937 */     if (counter == 1.0F) {
/*       */       
/*  6939 */       time = 300;
/*  6940 */       if (action == 757)
/*  6941 */         time = 200; 
/*  6942 */       if (Servers.localServer.isChallengeServer())
/*       */       {
/*  6944 */         if (!target.isEnchantedTurret()) {
/*  6945 */           time /= 2;
/*       */         } else {
/*  6947 */           time /= 3;
/*       */         }  } 
/*  6949 */       performer.getCommunicator().sendNormalServerMessage("You start to " + destString + " the " + target
/*  6950 */           .getName() + ".");
/*  6951 */       Server.getInstance().broadCastAction(performer
/*  6952 */           .getName() + " starts to " + destString + " " + target.getNameWithGenus() + ".", performer, 5);
/*       */       
/*  6954 */       performer.sendActionControl(Actions.actionEntrys[action].getVerbString(), true, time);
/*  6955 */       act.setTimeLeft(time);
/*  6956 */       performer.getStatus().modifyStamina(-800.0F);
/*       */     }
/*       */     else {
/*       */       
/*  6960 */       time = act.getTimeLeft();
/*       */     } 
/*  6962 */     if (act.currentSecond() % 5 == 0) {
/*       */       
/*  6964 */       String s = "sound.destroyobject.wood.axe";
/*  6965 */       if (destroyItem.isWeaponCrush())
/*  6966 */         s = "sound.destroyobject.wood.maul"; 
/*  6967 */       if (target.isStone())
/*       */       {
/*  6969 */         if (destroyItem.isWeaponCrush()) {
/*  6970 */           s = "sound.destroyobject.stone.maul";
/*       */         } else {
/*  6972 */           s = "sound.destroyobject.stone.axe";
/*       */         }  } 
/*  6974 */       if (target.isMetal())
/*       */       {
/*  6976 */         if (destroyItem.isWeaponCrush()) {
/*  6977 */           s = "sound.destroyobject.metal.maul";
/*       */         } else {
/*  6979 */           s = "sound.destroyobject.metal.axe";
/*       */         }  } 
/*  6981 */       SoundPlayer.playSound(s, target, 0.5F);
/*  6982 */       performer.getStatus().modifyStamina(-5000.0F);
/*       */       
/*  6984 */       if (destroyItem != null && 
/*  6985 */         !destroyItem.isBodyPartAttached())
/*  6986 */         destroyItem.setDamage(destroyItem.getDamage() + mod * destroyItem.getDamageModifier()); 
/*       */     } 
/*  6988 */     if (counter * 10.0F > time || insta) {
/*       */       
/*  6990 */       Skills skills = performer.getSkills();
/*  6991 */       Skill destroySkill = null;
/*       */       
/*       */       try {
/*  6994 */         destroySkill = skills.getSkill(102);
/*       */       }
/*  6996 */       catch (NoSuchSkillException nss) {
/*       */         
/*  6998 */         destroySkill = skills.learn(102, 1.0F);
/*       */       } 
/*  7000 */       destroySkill.skillCheck(20.0D, destroyItem, 0.0D, false, Math.min(10.0F, counter));
/*       */       
/*  7002 */       double damage = 0.0D;
/*  7003 */       if (insta && mod <= 0.0F) {
/*       */         
/*  7005 */         damage = 100.0D;
/*  7006 */         mod = 1.0F;
/*       */ 
/*       */       
/*       */       }
/*       */       else {
/*       */ 
/*       */ 
/*       */         
/*  7014 */         damage = Weapon.getModifiedDamageForWeapon(destroyItem, destroySkill) * 50.0D;
/*  7015 */         damage /= (target.getQualityLevel() / 10.0F);
/*       */         
/*  7017 */         if (target.getTemplateId() == 445 || target.getTemplateId() == 1125) {
/*  7018 */           damage *= 50.0D;
/*  7019 */         } else if (target.getTemplateId() == 937) {
/*  7020 */           damage *= 25.0D;
/*  7021 */         } else if (Servers.localServer.isChallengeServer()) {
/*  7022 */           damage *= 5.0D;
/*       */         } 
/*  7024 */       }  VolaTile tile = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/*       */       
/*  7026 */       if (target.isKingdomMarker()) {
/*       */         
/*  7028 */         if (target.getKingdom() != performer.getKingdomId()) {
/*       */ 
/*       */           
/*  7031 */           mod *= 0.75F;
/*       */           
/*  7033 */           if (!Features.Feature.TOWER_CHAINING.isEnabled()) {
/*       */             
/*  7035 */             GuardTower t = Kingdoms.getTower(target);
/*  7036 */             if (t != null) {
/*  7037 */               t.sendAttackWarning();
/*       */             }
/*       */           } 
/*  7040 */         } else if (performer.getWurmId() == target.getLastOwnerId()) {
/*       */ 
/*       */           
/*  7043 */           mod = 2.0F;
/*       */         }
/*       */       
/*  7046 */       } else if (target.isRoadMarker() && action == 757 && destroyItem.getTemplateId() == 1115) {
/*       */         
/*  7048 */         damage *= 15.0D;
/*  7049 */         mod *= 10.0F;
/*       */       } 
/*  7051 */       if (tile != null && tile.getVillage() != null) {
/*       */         
/*  7053 */         if (MethodsStructure.isCitizenAndMayPerformAction((short)83, performer, tile.getVillage()))
/*       */         {
/*  7055 */           if (!target.isKingdomMarker() || !Servers.localServer.PVPSERVER || target
/*  7056 */             .getLastOwnerId() == performer.getWurmId())
/*       */           {
/*  7058 */             damage *= 50.0D;
/*       */           }
/*       */         }
/*  7061 */         else if (MethodsStructure.isAllyAndMayPerformAction((short)83, performer, tile.getVillage()))
/*       */         {
/*  7063 */           if (!target.isKingdomMarker() || !Servers.localServer.PVPSERVER || target
/*  7064 */             .getLastOwnerId() == performer.getWurmId())
/*       */           {
/*  7066 */             damage *= 25.0D;
/*       */           }
/*       */         }
/*       */       
/*  7070 */       } else if (target.isStreetLamp()) {
/*  7071 */         damage *= 20.0D;
/*  7072 */       } else if (target.isSign() && Servers.localServer.PVPSERVER) {
/*  7073 */         damage *= 10.0D;
/*  7074 */       } else if (target.isMarketStall()) {
/*  7075 */         damage *= 10.0D;
/*  7076 */       } else if (!target.isKingdomMarker() && !Servers.localServer.PVPSERVER && target.getLastOwnerId() == performer.getWurmId()) {
/*  7077 */         damage *= 5.0D;
/*  7078 */       }  damage *= Weapon.getMaterialBashModifier(destroyItem.getMaterial());
/*       */       
/*  7080 */       if (performer.getCultist() != null && performer.getCultist().doubleStructDamage()) {
/*  7081 */         damage *= 2.0D;
/*       */       }
/*  7083 */       if (target.getTemplateId() == 521) {
/*       */         
/*  7085 */         mod += 0.01F;
/*  7086 */         if (Server.rand.nextInt(100) == 0)
/*       */         {
/*  7088 */           if (target.getData1() > 0)
/*       */             
/*       */             try {
/*  7091 */               CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(target
/*  7092 */                   .getData1());
/*  7093 */               byte sex = ctemplate.getSex();
/*  7094 */               if (sex == 0 && !ctemplate.keepSex)
/*       */               {
/*  7096 */                 if (Server.rand.nextInt(2) == 0)
/*  7097 */                   sex = 1; 
/*       */               }
/*  7099 */               byte ctype = target.getAuxData();
/*  7100 */               if (Server.rand.nextInt(40) == 0)
/*  7101 */                 ctype = 99; 
/*  7102 */               Creature.doNew(ctemplate.getTemplateId(), ctype, target.getPosX(), target.getPosY(), Server.rand
/*  7103 */                   .nextInt(360), target.isOnSurface() ? 0 : -1, "", sex);
/*       */             }
/*  7105 */             catch (Exception ex) {
/*       */               
/*  7107 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*       */             }  
/*       */         }
/*       */       } 
/*  7111 */       if (target.getTemplateId() == 731) {
/*       */         
/*  7113 */         damage = 100.0D;
/*  7114 */         mod = 1.0F;
/*       */       } 
/*  7116 */       float newDam = (float)(target.getDamage() + damage * mod);
/*  7117 */       float oldDam = target.getDamage();
/*       */       
/*  7119 */       String towerName = "Unknown";
/*       */       
/*  7121 */       if (Features.Feature.TOWER_CHAINING.isEnabled() && target.isKingdomMarker()) {
/*       */         
/*  7123 */         GuardTower t = Kingdoms.getTower(target);
/*  7124 */         if (t != null) {
/*       */ 
/*       */           
/*  7127 */           if (newDam >= 100.0F) {
/*  7128 */             towerName = t.getName();
/*       */           }
/*  7130 */           t.checkBashDamage(oldDam, newDam);
/*       */         } 
/*       */       } 
/*       */       
/*  7134 */       if (target.isRoadMarker()) {
/*       */ 
/*       */         
/*  7137 */         if (performer.fireTileLog()) {
/*  7138 */           TileEvent.log(target.getTileX(), target.getTileY(), target.isOnSurface() ? 0 : -1, performer.getWurmId(), action);
/*       */         }
/*  7140 */         if (newDam >= 100.0F) {
/*  7141 */           target.setWhatHappened("bashed by " + performer.getName());
/*       */         }
/*       */       } 
/*       */       
/*  7145 */       if (target.setDamage(newDam)) {
/*       */         
/*  7147 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/*  7148 */             .getName() + " falls apart with a crash.");
/*  7149 */         Server.getInstance().broadCastAction(performer
/*  7150 */             .getName() + " damages " + target.getNameWithGenus() + " and it falls apart with a crash.", performer, 5);
/*       */ 
/*       */         
/*  7153 */         if (target.isKingdomMarker() && Features.Feature.TOWER_CHAINING.isEnabled()) {
/*       */           
/*  7155 */           Players.getInstance().broadCastDestroyInfo(performer, towerName + " has been destroyed.");
/*  7156 */           Server.getInstance().broadCastEpicEvent(towerName + " has been destroyed.");
/*       */         } 
/*  7158 */         if (performer.getDeity() != null)
/*       */         {
/*  7160 */           performer.performActionOkey(act);
/*       */         }
/*  7162 */         MissionTriggers.activateTriggers(performer, destroyItem, 913, target.getWurmId(), (int)counter);
/*  7163 */         if (target.getTemplateId() == 521)
/*       */         {
/*  7165 */           if (Servers.localServer.PVPSERVER && !Servers.isThisAChaosServer())
/*       */           {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  7179 */             performer.getFightingSkill().setKnowledge(performer
/*  7180 */                 .getFightingSkill().getRealKnowledge() + (100.0D - performer.getFightingSkill().getRealKnowledge()) * spawnDamageMod, false);
/*       */           
/*       */           }
/*       */         }
/*       */       }
/*       */       else {
/*       */         
/*  7187 */         performer.getCommunicator().sendNormalServerMessage("You damage the " + target.getName() + ".");
/*  7188 */         Server.getInstance().broadCastAction(performer.getName() + " damages " + target.getNameWithGenus() + ".", performer, 5);
/*       */       } 
/*       */       
/*  7191 */       toReturn = true;
/*       */     } 
/*       */     
/*  7194 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean filet(Action act, Creature performer, Item source, Item target, float counter) {
/*  7210 */     boolean done = false;
/*       */     
/*  7212 */     if (target.isRoyal() || target.isIndestructible()) {
/*       */       
/*  7214 */       performer.getCommunicator().sendNormalServerMessage("You mysteriously cut yourself when trying to filet that!");
/*  7215 */       CombatEngine.addWound(performer, performer, (byte)1, 13, (2000 + Server.rand
/*  7216 */           .nextInt(2000)), 0.0F, "cut", null, 0.0F, 0.0F, false, false, false, false);
/*       */       
/*  7218 */       return true;
/*       */     } 
/*  7220 */     int time = 200;
/*       */     
/*  7222 */     if (target.getOwnerId() != performer.getWurmId()) {
/*       */       
/*  7224 */       performer.getCommunicator().sendNormalServerMessage("You need to be in possession of the " + target
/*  7225 */           .getName() + " in order to filet it.");
/*  7226 */       return true;
/*       */     } 
/*  7228 */     if (counter == 1.0F) {
/*       */       
/*  7230 */       Skill butchering = performer.getSkills().getSkillOrLearn(10059);
/*  7231 */       int nums = 2;
/*  7232 */       int tid = 368;
/*       */       
/*       */       try {
/*  7235 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(tid);
/*  7236 */         nums = target.getWeightGrams() / template.getWeightGrams();
/*  7237 */         if (nums == 0)
/*       */         {
/*  7239 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  7240 */               .getName() + " is too small to produce a filet.");
/*  7241 */           done = true;
/*       */         }
/*       */       
/*  7244 */       } catch (NoSuchTemplateException nst) {
/*       */         
/*  7246 */         logger.log(Level.WARNING, "No template for filet?" + nst.getMessage(), (Throwable)nst);
/*  7247 */         done = true;
/*       */       } 
/*  7249 */       if (!done)
/*       */       {
/*  7251 */         performer.getCommunicator().sendNormalServerMessage("You start to filet the " + target.getName() + ".");
/*  7252 */         Server.getInstance().broadCastAction(performer.getName() + " starts to filet a " + target.getName() + ".", performer, 5);
/*       */         
/*  7254 */         time = Actions.getStandardActionTime(performer, butchering, source, 0.0D);
/*  7255 */         act.setTimeLeft(time);
/*  7256 */         performer.sendActionControl(Actions.actionEntrys[225].getVerbString(), true, time);
/*  7257 */         SoundPlayer.playSound("sound.butcherKnife", performer, 1.0F);
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/*  7262 */       time = act.getTimeLeft();
/*  7263 */       if (act.currentSecond() % 5 == 0) {
/*       */         
/*  7265 */         source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*  7266 */         SoundPlayer.playSound("sound.butcherKnife", performer, 1.0F);
/*       */       } 
/*       */     } 
/*  7269 */     if (counter * 10.0F >= time) {
/*       */       
/*  7271 */       Skill butchering = performer.getSkills().getSkillOrLearn(10059);
/*       */       
/*  7273 */       done = true;
/*  7274 */       boolean filet = true;
/*  7275 */       int nums = 2;
/*       */       
/*       */       try {
/*  7278 */         int tid = 368;
/*       */         
/*  7280 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(tid);
/*       */         
/*  7282 */         int tweight = template.getWeightGrams();
/*  7283 */         nums = target.getWeightGrams() / tweight;
/*  7284 */         if (nums == 0) {
/*       */           
/*  7286 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  7287 */               .getName() + " is too small to produce a filet.");
/*  7288 */           done = true;
/*  7289 */           filet = false;
/*       */         } 
/*  7291 */         int invnums = performer.getInventory().getNumItemsNotCoins();
/*       */         
/*  7293 */         if (invnums + nums >= 100) {
/*       */           
/*  7295 */           performer.getCommunicator().sendNormalServerMessage("You can't make space in your inventory for the filets.");
/*       */           
/*  7297 */           done = true;
/*  7298 */           filet = false;
/*       */         } 
/*       */         
/*  7301 */         if (filet)
/*       */         {
/*  7303 */           double bonus = 0.0D;
/*       */           
/*       */           try {
/*  7306 */             int primarySkill = source.getPrimarySkill();
/*  7307 */             Skill primskill = null;
/*       */             
/*       */             try {
/*  7310 */               primskill = performer.getSkills().getSkill(primarySkill);
/*       */             }
/*  7312 */             catch (Exception ex) {
/*       */               
/*  7314 */               primskill = performer.getSkills().learn(primarySkill, 1.0F);
/*       */             } 
/*  7316 */             bonus = primskill.skillCheck(10.0D, 0.0D, false, counter);
/*       */           }
/*  7318 */           catch (NoSuchSkillException noSuchSkillException) {}
/*       */ 
/*       */ 
/*       */           
/*  7322 */           performer.getCommunicator().sendNormalServerMessage("You filet the " + target.getName() + ".");
/*  7323 */           Server.getInstance().broadCastAction(performer.getName() + " filets " + target.getNameWithGenus() + ".", performer, 5);
/*       */           
/*  7325 */           float ql = 0.0F;
/*       */           
/*  7327 */           float imbueEnhancement = 1.0F;
/*  7328 */           float max = target.getCurrentQualityLevel() * 1.0F;
/*       */           
/*  7330 */           for (int x = 0; x < nums; x++) {
/*       */             
/*  7332 */             ql = Math.max(1.0F, (float)butchering.skillCheck(target.getDamage(), source, bonus, (nums > 10), 1.0F));
/*       */ 
/*       */             
/*       */             try {
/*  7336 */               Item fil = ItemFactory.createItem(tid, Math.min(max, ql), null);
/*  7337 */               fil.setName("fillet of " + target.getActualName().toLowerCase());
/*  7338 */               if (target.isMeat()) {
/*  7339 */                 fil.setMaterial(target.getMaterial());
/*       */               } else {
/*  7341 */                 fil.setRealTemplate(target.getTemplateId());
/*  7342 */               }  fil.setAuxData(target.getAuxData());
/*  7343 */               if (target.getTemperature() > 200)
/*  7344 */                 fil.setTemperature(target.getTemperature()); 
/*  7345 */               performer.getInventory().insertItem(fil);
/*       */             }
/*  7347 */             catch (FailedException fe) {
/*       */               
/*  7349 */               logger.log(Level.WARNING, performer.getName() + ":" + fe.getMessage(), (Throwable)fe);
/*       */             }
/*  7351 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  7353 */               logger.log(Level.WARNING, "No template for filet?" + nst.getMessage(), (Throwable)nst);
/*       */             } 
/*       */           } 
/*  7356 */           Items.destroyItem(target.getWurmId());
/*       */         }
/*       */       
/*  7359 */       } catch (NoSuchTemplateException nst) {
/*       */         
/*  7361 */         logger.log(Level.WARNING, "No template for filet?" + nst.getMessage(), (Throwable)nst);
/*  7362 */         done = true;
/*       */       } 
/*       */     } 
/*  7365 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean filetFish(Action act, Creature performer, Item source, Item target, float counter) {
/*  7381 */     if (target.isRoyal() || target.isIndestructible()) {
/*       */       
/*  7383 */       performer.getCommunicator().sendNormalServerMessage("You mysteriously cut yourself when trying to filet that!");
/*  7384 */       CombatEngine.addWound(performer, performer, (byte)1, 13, (2000 + Server.rand
/*  7385 */           .nextInt(2000)), 0.0F, "cut", null, 0.0F, 0.0F, false, false, false, false);
/*  7386 */       return true;
/*       */     } 
/*  7388 */     int time = 200;
/*  7389 */     if (target.getOwnerId() != performer.getWurmId()) {
/*       */       
/*  7391 */       performer.getCommunicator().sendNormalServerMessage("You need to be in possession of the " + target
/*  7392 */           .getName() + " in order to filet it.");
/*  7393 */       return true;
/*       */     } 
/*  7395 */     if (counter == 1.0F) {
/*       */ 
/*       */       
/*       */       try {
/*  7399 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(369);
/*  7400 */         int tweight = template.getWeightGrams();
/*  7401 */         int waste = tweight / 10;
/*       */         
/*  7403 */         int nums = (target.getWeightGrams() + waste) / (tweight + waste);
/*  7404 */         if (nums == 0) {
/*       */           
/*  7406 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  7407 */               .getName() + " is too small to produce a filet.");
/*  7408 */           return true;
/*       */         } 
/*       */         
/*  7411 */         time = nums * 10;
/*       */       }
/*  7413 */       catch (NoSuchTemplateException nst) {
/*       */         
/*  7415 */         performer.getCommunicator().sendNormalServerMessage("Something went horribly wrong with " + target
/*  7416 */             .getName() + ". Please use /support.");
/*  7417 */         logger.log(Level.WARNING, "No template for filet?" + nst.getMessage(), (Throwable)nst);
/*  7418 */         return true;
/*       */       } 
/*  7420 */       performer.getCommunicator().sendNormalServerMessage("You start to filet the " + target.getName() + ".");
/*  7421 */       Server.getInstance().broadCastAction(performer.getName() + " starts to filet a " + target.getName() + ".", performer, 5);
/*       */       
/*  7423 */       act.setTimeLeft(time);
/*  7424 */       performer.sendActionControl(Actions.actionEntrys[225].getVerbString(), true, time);
/*  7425 */       SoundPlayer.playSound("sound.butcherKnife", performer, 1.0F);
/*  7426 */       return false;
/*       */     } 
/*  7428 */     time = act.getTimeLeft();
/*  7429 */     if (act.mayPlaySound()) {
/*       */       
/*  7431 */       source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*  7432 */       SoundPlayer.playSound("sound.butcherKnife", performer, 1.0F);
/*       */     } 
/*       */     
/*  7435 */     if (act.justTickedSecond()) {
/*       */       
/*  7437 */       int nums, tweight, waste, invnums = performer.getInventory().getNumItemsNotCoins();
/*  7438 */       if (invnums + 1 >= 100) {
/*       */         
/*  7440 */         performer.getCommunicator().sendNormalServerMessage("You can't make space in your inventory for the filet.");
/*       */         
/*  7442 */         return true;
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       try {
/*  7451 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(369);
/*  7452 */         tweight = template.getWeightGrams();
/*  7453 */         waste = tweight / 10;
/*       */         
/*  7455 */         nums = (target.getWeightGrams() + waste) / (tweight + waste);
/*  7456 */         if (nums == 0)
/*       */         {
/*  7458 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  7459 */               .getName() + " is too small to produce a filet.");
/*  7460 */           return true;
/*       */         }
/*       */       
/*  7463 */       } catch (NoSuchTemplateException nst) {
/*       */         
/*  7465 */         performer.getCommunicator().sendNormalServerMessage("Something went horribly wrong with " + target
/*  7466 */             .getName() + ". Please use /support.");
/*  7467 */         logger.log(Level.WARNING, "No template for filet?" + nst.getMessage(), (Throwable)nst);
/*  7468 */         return true;
/*       */       } 
/*       */       
/*  7471 */       Skill butchering = performer.getSkills().getSkillOrLearn(10059);
/*  7472 */       double bonus = 0.0D;
/*       */       
/*       */       try {
/*  7475 */         Skill primskill = null;
/*  7476 */         int primarySkill = source.getPrimarySkill();
/*       */         
/*       */         try {
/*  7479 */           primskill = performer.getSkills().getSkill(primarySkill);
/*       */         }
/*  7481 */         catch (Exception ex) {
/*       */           
/*  7483 */           primskill = performer.getSkills().learn(primarySkill, 1.0F);
/*       */         } 
/*  7485 */         bonus = primskill.skillCheck(10.0D, 0.0D, false, 1.0F);
/*       */       }
/*  7487 */       catch (NoSuchSkillException noSuchSkillException) {}
/*       */ 
/*       */ 
/*       */       
/*  7491 */       performer.getCommunicator().sendNormalServerMessage("You filet the " + target.getName() + ".");
/*  7492 */       Server.getInstance().broadCastAction(performer.getName() + " filets " + target.getNameWithGenus() + ".", performer, 5);
/*       */       
/*  7494 */       float max = target.getCurrentQualityLevel();
/*       */       
/*  7496 */       float ql = Math.max(1.0F, (float)butchering.skillCheck(target.getDamage(), source, bonus, (counter > 10.0F), 1.0F));
/*  7497 */       boolean destroyed = false;
/*       */       
/*       */       try {
/*  7500 */         Item fil = ItemFactory.createItem(369, Math.min(max, ql), null);
/*  7501 */         fil.setName("fillet of " + target.getActualName().toLowerCase());
/*  7502 */         fil.setRealTemplate(target.getTemplateId());
/*  7503 */         fil.setAuxData(target.getAuxData());
/*  7504 */         fil.setIsUnderWeight(false);
/*  7505 */         if (target.getTemperature() > 200)
/*  7506 */           fil.setTemperature(target.getTemperature()); 
/*  7507 */         performer.getInventory().insertItem(fil);
/*  7508 */         target.setIsUnderWeight(true);
/*  7509 */         destroyed = target.setWeight(target.getWeightGrams() - tweight - waste, true);
/*       */       }
/*  7511 */       catch (FailedException fe) {
/*       */         
/*  7513 */         logger.log(Level.WARNING, performer.getName() + ":" + fe.getMessage(), (Throwable)fe);
/*       */       }
/*  7515 */       catch (NoSuchTemplateException nst) {
/*       */         
/*  7517 */         logger.log(Level.WARNING, "No template for filet?" + nst.getMessage(), (Throwable)nst);
/*       */       } 
/*       */       
/*  7520 */       if (nums == 1) {
/*       */         
/*  7522 */         int rand = Server.rand.nextInt(80 + ((target != null) ? (source.getRarity() * 10) : 0));
/*  7523 */         if (rand > 60)
/*       */         {
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  7529 */           if (target.getTemplateId() == 572) {
/*       */             
/*  7531 */             if (invnums + 1 > 100) {
/*       */               
/*  7533 */               performer.getCommunicator().sendNormalServerMessage("You can't make space in your inventory for the item you found in the " + target
/*  7534 */                   .getName() + ".");
/*  7535 */               return true;
/*       */             } 
/*       */             
/*       */             try {
/*  7539 */               Item bonusItem = ItemFactory.createItem(752, Math.min(max, ql), null);
/*  7540 */               performer.getInventory().insertItem(bonusItem);
/*       */             }
/*  7542 */             catch (FailedException fe) {
/*       */               
/*  7544 */               logger.log(Level.WARNING, performer.getName() + ":" + fe.getMessage(), (Throwable)fe);
/*       */             }
/*  7546 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  7548 */               logger.log(Level.WARNING, "No template for inkSac?" + nst.getMessage(), (Throwable)nst);
/*       */             } 
/*       */           } 
/*       */         }
/*  7552 */         if (!destroyed) {
/*       */ 
/*       */           
/*  7555 */           int rweight = target.getWeightGrams();
/*       */           
/*  7557 */           if (rweight < tweight) {
/*       */             
/*       */             try {
/*       */ 
/*       */               
/*  7562 */               Items.destroyItem(target.getWurmId());
/*  7563 */               Item bonusItem = ItemFactory.createItem(1363, Math.min(max, ql), null);
/*  7564 */               performer.getInventory().insertItem(bonusItem);
/*       */             }
/*  7566 */             catch (FailedException fe) {
/*       */               
/*  7568 */               logger.log(Level.WARNING, performer.getName() + ":" + fe.getMessage(), (Throwable)fe);
/*       */             }
/*  7570 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  7572 */               logger.log(Level.WARNING, "No template for inkSac?" + nst.getMessage(), (Throwable)nst);
/*       */             } 
/*       */           }
/*       */         } 
/*  7576 */         return true;
/*       */       } 
/*       */     } 
/*  7579 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean chop(Action act, Creature performer, Item source, Item target, float counter) {
/*  7585 */     boolean done = false;
/*       */     
/*       */     try {
/*  7588 */       if (!source.isWeaponAxe() && source.getTemplateId() != 24) {
/*       */         
/*  7590 */         performer.getCommunicator().sendNormalServerMessage("You cannot chop with a " + source.getName() + ".");
/*  7591 */         return true;
/*       */       } 
/*  7593 */       String action = (source.getTemplateId() != 24) ? "chop" : "saw";
/*  7594 */       if (target.getTemplateId() != 385) {
/*       */         
/*  7596 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/*  7597 */             .getName() + " is not a huge log. You cannot " + action + " that into to smaller logs.");
/*  7598 */         return true;
/*       */       } 
/*  7600 */       if (target.getLastOwnerId() != performer.getWurmId() && !Methods.isActionAllowed(performer, (short)6, target)) {
/*  7601 */         return true;
/*       */       }
/*  7603 */       int time = 200;
/*  7604 */       if (counter == 1.0F) {
/*       */         
/*  7606 */         float posX = performer.getStatus().getPositionX();
/*  7607 */         float posY = performer.getStatus().getPositionY();
/*  7608 */         float rot = performer.getStatus().getRotation();
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  7616 */         float xPosMod = (float)Math.sin((rot * 0.017453292F));
/*  7617 */         float yPosMod = -((float)Math.cos((rot * 0.017453292F)));
/*  7618 */         posX += xPosMod;
/*  7619 */         posY += yPosMod;
/*       */         
/*  7621 */         int placedX = (int)posX >> 2;
/*  7622 */         int placedY = (int)posY >> 2;
/*  7623 */         VolaTile t = Zones.getTileOrNull(placedX, placedY, performer.isOnSurface());
/*  7624 */         if (t != null)
/*       */         {
/*       */           
/*  7627 */           if (target.getOwnerId() == -10L && t
/*  7628 */             .getNumberOfItems(performer.getFloorLevel()) > 99) {
/*       */             
/*  7630 */             performer.getCommunicator().sendNormalServerMessage("There is no space to " + action + " wood here. Clear the area first.");
/*       */             
/*  7632 */             return true;
/*       */           } 
/*       */         }
/*  7635 */         Skill woodcutting = performer.getSkills().getSkillOrLearn(1007);
/*  7636 */         Skill primskill = performer.getSkills().getSkillOrLearn(source.getPrimarySkill());
/*  7637 */         performer.getCommunicator().sendNormalServerMessage("You start to " + action + " up the " + target
/*  7638 */             .getName() + ".");
/*  7639 */         Server.getInstance().broadCastAction(performer
/*  7640 */             .getName() + " starts to " + action + " up a " + target.getName() + ".", performer, 5);
/*       */ 
/*       */         
/*  7643 */         int treeAge = 5;
/*       */         
/*  7645 */         time = (int)(Terraforming.calcTime(5, source, primskill, woodcutting) * Actions.getStaminaModiferFor(performer, 20000));
/*  7646 */         time = Math.min(65535, time);
/*       */         
/*  7648 */         act.setTimeLeft(time);
/*  7649 */         performer.getStatus().modifyStamina(-1000.0F);
/*  7650 */         performer.sendActionControl((source.getTemplateId() != 24) ? "Chopping" : "Sawing", true, time);
/*       */       
/*       */       }
/*       */       else {
/*       */         
/*  7655 */         time = act.getTimeLeft();
/*       */         
/*  7657 */         if (act.justTickedSecond() && ((time < 50 && act.currentSecond() % 2 == 0) || act.currentSecond() % 5 == 0)) {
/*       */           
/*  7659 */           source.setDamage(source.getDamage() + 0.001F * source.getDamageModifier());
/*  7660 */           performer.getStatus().modifyStamina(-5000.0F);
/*       */         } 
/*       */         
/*  7663 */         if (act.justTickedSecond() && counter * 10.0F < (time - 30))
/*       */         {
/*  7665 */           if (source.getTemplateId() != 24) {
/*       */             
/*  7667 */             if ((act.currentSecond() - 2) % 4 == 0)
/*       */             {
/*  7669 */               String sstring = "sound.work.woodcutting1";
/*  7670 */               int x = Server.rand.nextInt(3);
/*  7671 */               if (x == 0) {
/*  7672 */                 sstring = "sound.work.woodcutting2";
/*  7673 */               } else if (x == 1) {
/*  7674 */                 sstring = "sound.work.woodcutting3";
/*  7675 */               }  SoundPlayer.playSound(sstring, target, 1.0F);
/*       */ 
/*       */             
/*       */             }
/*       */ 
/*       */           
/*       */           }
/*  7682 */           else if ((act.currentSecond() - 2) % 5 == 0 && counter * 10.0F < (time - 50)) {
/*       */             
/*  7684 */             String sstring = "sound.work.carpentry.saw";
/*  7685 */             SoundPlayer.playSound("sound.work.carpentry.saw", target, 1.0F);
/*       */           } 
/*       */         }
/*       */       } 
/*       */       
/*  7690 */       if (counter * 10.0F >= time)
/*       */       {
/*  7692 */         if (act.getRarity() != 0)
/*       */         {
/*  7694 */           performer.playPersonalSound("sound.fx.drumroll");
/*       */         }
/*  7696 */         if (source.getTemplateId() != 24) {
/*       */ 
/*       */           
/*  7699 */           String sstring = "sound.work.woodcutting1";
/*  7700 */           int x = Server.rand.nextInt(3);
/*  7701 */           if (x == 0) {
/*  7702 */             sstring = "sound.work.woodcutting2";
/*  7703 */           } else if (x == 1) {
/*  7704 */             sstring = "sound.work.woodcutting3";
/*  7705 */           }  SoundPlayer.playSound(sstring, target, 1.0F);
/*       */         } 
/*  7707 */         float posX = performer.getStatus().getPositionX();
/*  7708 */         float posY = performer.getStatus().getPositionY();
/*  7709 */         float rot = performer.getStatus().getRotation();
/*       */         
/*  7711 */         float xPosMod = (float)Math.sin((rot * 0.017453292F)) * 2.0F;
/*  7712 */         float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * 2.0F;
/*  7713 */         posX += xPosMod;
/*  7714 */         posY += yPosMod;
/*       */         
/*  7716 */         int placedX = (int)posX >> 2;
/*  7717 */         int placedY = (int)posY >> 2;
/*  7718 */         VolaTile t = Zones.getTileOrNull(placedX, placedY, performer.isOnSurface());
/*  7719 */         if (t != null)
/*       */         {
/*       */           
/*  7722 */           if (target.getOwnerId() == -10L && t
/*  7723 */             .getNumberOfItems(performer.getFloorLevel()) > 99) {
/*       */             
/*  7725 */             performer.getCommunicator().sendNormalServerMessage("There is no space to " + action + " wood here. Clear the area first.");
/*       */             
/*  7727 */             return true;
/*       */           } 
/*       */         }
/*       */         
/*  7731 */         float tickCounter = counter;
/*       */         
/*  7733 */         TreeData.TreeType tType = Materials.getTreeTypeForWood(target.getMaterial());
/*  7734 */         double difficulty = tType.getDifficulty();
/*  7735 */         Skill woodcutting = performer.getSkills().getSkillOrLearn(1007);
/*       */         
/*       */         try {
/*  7738 */           int primarySkill = source.getPrimarySkill();
/*  7739 */           Skill primskill = performer.getSkills().getSkillOrLearn(primarySkill);
/*  7740 */           if (primskill.getKnowledge() < 20.0D || primarySkill == 10003 || primarySkill == 10008) {
/*  7741 */             primskill.skillCheck(difficulty, source, 0.0D, false, tickCounter);
/*       */           }
/*  7743 */         } catch (NoSuchSkillException ss) {
/*       */           
/*  7745 */           logger.log(Level.WARNING, "No primary skill for " + source.getName());
/*       */         } 
/*  7747 */         done = true;
/*  7748 */         int nums = 1;
/*       */         
/*       */         try {
/*  7751 */           ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(9);
/*  7752 */           nums = target.getWeightGrams() / template.getWeightGrams();
/*  7753 */           if (nums == 0) {
/*       */             
/*  7755 */             performer.getCommunicator().sendNormalServerMessage("The " + target
/*  7756 */                 .getName() + " is too small to produce a log.");
/*  7757 */             target.setTemplateId(9);
/*       */ 
/*       */             
/*  7760 */             done = true;
/*  7761 */             if (performer.getTutorialLevel() == 3 && !performer.skippedTutorial())
/*       */             {
/*  7763 */               performer.missionFinished(true, true);
/*       */             }
/*       */           }
/*       */           else {
/*       */             
/*  7768 */             performer.getCommunicator().sendNormalServerMessage("You create a smaller log from the " + target
/*  7769 */                 .getName() + ".");
/*  7770 */             Server.getInstance()
/*  7771 */               .broadCastAction(performer
/*  7772 */                 .getName() + " creates a smaller log from the " + target.getName() + ".", performer, 5);
/*       */ 
/*       */ 
/*       */             
/*  7776 */             double power = woodcutting.skillCheck(difficulty, source, 0.0D, false, tickCounter);
/*  7777 */             double cappedPower = Math.min(power, target.getCurrentQualityLevel());
/*  7778 */             float ql = GeneralUtilities.calcRareQuality(cappedPower, act.getRarity(), source.getRarity(), target
/*  7779 */                 .getRarity());
/*       */ 
/*       */             
/*       */             try {
/*  7783 */               Item log = ItemFactory.createItem(9, ql, target
/*  7784 */                   .getMaterial(), act
/*  7785 */                   .getRarity(), null);
/*       */               
/*  7787 */               if (target.getOwnerId() != -10L) {
/*  7788 */                 performer.getInventory().insertItem(log);
/*       */               } else {
/*       */                 
/*  7791 */                 log.putItemInfrontof(performer);
/*       */               } 
/*  7793 */               log.setLastOwnerId(performer.getWurmId());
/*  7794 */               target.setWeight(target.getWeightGrams() - log.getWeightGrams(), true);
/*  7795 */               if (!target.deleted)
/*       */               {
/*  7797 */                 if (target.getWeightGrams() <= log.getTemplate().getWeightGrams())
/*       */                 {
/*  7799 */                   if (target.getWeightGrams() < 1000) {
/*       */ 
/*       */ 
/*       */                     
/*  7803 */                     Items.destroyItem(target.getWurmId());
/*       */ 
/*       */                   
/*       */                   }
/*       */                   else {
/*       */ 
/*       */                     
/*  7810 */                     Item spareLog = ItemFactory.createItem(9, target
/*  7811 */                         .getCurrentQualityLevel(), target
/*  7812 */                         .getMaterial(), target
/*  7813 */                         .getRarity(), null);
/*  7814 */                     spareLog.setWeight(target.getWeightGrams(), false);
/*  7815 */                     Items.destroyItem(target.getWurmId());
/*  7816 */                     if (log.getParentId() == performer.getInventory().getWurmId()) {
/*  7817 */                       performer.getInventory().insertItem(spareLog);
/*       */                     } else {
/*  7819 */                       spareLog.putItemInfrontof(performer);
/*       */                     } 
/*       */                   }  } 
/*       */               }
/*  7823 */               if (performer.getTutorialLevel() == 3 && !performer.skippedTutorial())
/*       */               {
/*  7825 */                 performer.missionFinished(true, true);
/*       */               }
/*       */             }
/*  7828 */             catch (FailedException fe) {
/*       */               
/*  7830 */               logger.log(Level.WARNING, performer.getName() + ":" + fe.getMessage(), (Throwable)fe);
/*       */             }
/*  7832 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  7834 */               logger.log(Level.WARNING, "No template for log?" + nst.getMessage(), (Throwable)nst);
/*       */             }
/*  7836 */             catch (NoSuchItemException nsi) {
/*       */               
/*  7838 */               logger.log(Level.WARNING, performer.getName() + " no such item?", (Throwable)nsi);
/*       */             }
/*  7840 */             catch (NoSuchPlayerException nsp) {
/*       */               
/*  7842 */               logger.log(Level.WARNING, performer.getName() + " no such player?", (Throwable)nsp);
/*       */             }
/*  7844 */             catch (NoSuchCreatureException nsc) {
/*       */               
/*  7846 */               logger.log(Level.WARNING, performer.getName() + " no such creature?", (Throwable)nsc);
/*       */             }
/*  7848 */             catch (NoSuchZoneException nsz) {
/*       */               
/*  7850 */               logger.log(Level.WARNING, performer.getName() + " no such zone?", (Throwable)nsz);
/*       */             }
/*       */           
/*       */           } 
/*  7854 */         } catch (NoSuchTemplateException nst) {
/*       */           
/*  7856 */           logger.log(Level.WARNING, "No template for log?" + nst.getMessage(), (Throwable)nst);
/*       */         } 
/*  7858 */         PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.CREATE_LOG);
/*       */       }
/*       */     
/*  7861 */     } catch (NoSuchSkillException e) {
/*       */ 
/*       */       
/*  7864 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*  7865 */       done = true;
/*       */     } 
/*  7867 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final byte getNewCreationState(byte material) {
/*  7872 */     if (Materials.isWood(material))
/*       */     {
/*  7874 */       return (byte)Server.rand.nextInt(5);
/*       */     }
/*  7876 */     if (Materials.isMetal(material))
/*       */     {
/*  7878 */       return (byte)Server.rand.nextInt(5);
/*       */     }
/*  7880 */     if (Materials.isLeather(material))
/*       */     {
/*  7882 */       return (byte)Server.rand.nextInt(5);
/*       */     }
/*  7884 */     if (Materials.isCloth(material))
/*       */     {
/*  7886 */       return (byte)Server.rand.nextInt(5);
/*       */     }
/*  7888 */     if (Materials.isClay(material))
/*       */     {
/*  7890 */       return (byte)Server.rand.nextInt(5);
/*       */     }
/*  7892 */     if (Materials.isStone(material))
/*       */     {
/*  7894 */       return (byte)Server.rand.nextInt(5);
/*       */     }
/*  7896 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final byte getImproveMaterial(Item item) {
/*  7901 */     if (!item.isImproveUsingTypeAsMaterial())
/*  7902 */       return item.getMaterial(); 
/*  7903 */     if (item.getTemplate().isCloth() && item.getMaterial() != 69)
/*  7904 */       return 17; 
/*  7905 */     return item.getMaterial();
/*       */   }
/*       */ 
/*       */   
/*       */   public static final int getItemForImprovement(byte material, byte state) {
/*  7910 */     int template = -10;
/*       */     
/*  7912 */     if (Materials.isWood(material))
/*       */     
/*  7914 */     { switch (state)
/*       */       
/*       */       { case 1:
/*  7917 */           template = 8;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  8032 */           return template;case 2: template = 63; return template;case 3: template = 388; return template;case 4: template = 313; return template; }  template = -10; } else if (Materials.isMetal(material)) { switch (state) { case 1: template = 296; return template;case 2: template = 62; return template;case 3: template = 128; return template;case 4: template = 313; return template; }  template = -10; } else if (Materials.isLeather(material)) { switch (state) { case 1: template = 215; return template;case 2: template = 390; return template;case 3: template = 392; return template;case 4: template = 63; return template; }  template = -10; } else if (Materials.isCloth(material)) { switch (state) { case 1: template = 215; return template;case 2: template = 394; return template;case 3: template = 128; return template;case 4: template = 215; return template; }  template = -10; } else if (Materials.isStone(material)) { switch (state) { case 1: template = 97; return template;case 2: template = 97; return template;case 3: template = 97; return template;case 4: template = 97; return template; }  template = -10; } else if (Materials.isClay(material)) { switch (state) { case 1: template = 14; return template;case 2: template = 128; return template;case 3: template = 396; return template;case 4: template = 397; return template; }  template = -10; }  return template;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final String getNeededCreationAction(byte material, byte state, Item item) {
/*  8037 */     String todo = "";
/*  8038 */     String fstring = "improve";
/*  8039 */     if (item.getTemplateId() == 386)
/*  8040 */       fstring = "finish"; 
/*  8041 */     if (Materials.isWood(material))
/*       */     
/*  8043 */     { switch (state)
/*       */       
/*       */       { case 1:
/*  8046 */           todo = "You notice some notches you must carve away in order to " + fstring + " the " + item.getName() + ".";
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  8162 */           return todo;case 2: todo = "You must use a mallet on the " + item.getName() + " in order to " + fstring + " it."; return todo;case 3: todo = "You must use a file to smooth out the " + item.getName() + " in order to " + fstring + " it."; return todo;case 4: todo = "You will want to polish the " + item.getName() + " with a pelt to " + fstring + " it."; return todo; }  todo = ""; } else if (Materials.isMetal(material)) { switch (state) { case 1: todo = "The " + item.getName() + (item.isNamePlural() ? " need" : " needs") + " to be sharpened with a whetstone."; return todo;case 2: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some dents that must be flattened by a hammer."; return todo;case 3: todo = "You need to temper the " + item.getName() + " by dipping it in water while it's hot."; return todo;case 4: todo = "You need to polish the " + item.getName() + " with a pelt."; return todo; }  todo = ""; } else if (Materials.isLeather(material)) { switch (state) { case 1: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some holes and must be tailored with an iron needle to " + fstring + "."; return todo;case 2: todo = "The " + item.getName() + (item.isNamePlural() ? " need" : " needs") + " some holes punched with an awl."; return todo;case 3: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some excess leather that needs to be cut away with a leather knife."; return todo;case 4: todo = "A mallet must be used on the " + item.getName() + " in order to smooth out a quirk."; return todo; }  todo = ""; } else if (Materials.isCloth(material)) { switch (state) { case 1: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " an open seam that must be backstitched with an iron needle to " + fstring + "."; return todo;case 2: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some excess cloth that needs to be cut away with a scissors."; return todo;case 3: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some stains that must be washed away."; return todo;case 4: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " a seam that needs to be hidden by slipstitching with an iron needle."; return todo; }  todo = ""; } else if (Materials.isStone(material)) { switch (state) { case 1: case 2: case 3: case 4: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some irregularities that must be removed with a stone chisel."; return todo; }  todo = ""; } else if (Materials.isClay(material)) { switch (state) { case 1: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some flaws that must be removed by hand."; return todo;case 2: todo = "The " + item.getName() + " needs water."; return todo;case 3: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some flaws that must be fixed with a clay shaper."; return todo;case 4: todo = "The " + item.getName() + (item.isNamePlural() ? " have" : " has") + " some flaws that must be fixed with a spatula."; return todo; }  todo = ""; }  return todo;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getCreationActionString(byte material, byte state, Item used, Item item) {
/*  8176 */     String todo = "";
/*  8177 */     if (Materials.isWood(material))
/*       */     
/*  8179 */     { switch (state)
/*       */       
/*       */       { case 1:
/*  8182 */           todo = "You carve on the " + item.getName() + ".";
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  8299 */           return todo;case 2: todo = "You hammer on the " + item.getName() + "."; return todo;case 3: todo = "You file on the " + item.getName() + " meticulously."; return todo;case 4: todo = "You polish the " + item.getName() + " carefully."; return todo; }  todo = ""; } else if (Materials.isMetal(material)) { switch (state) { case 1: todo = "You sharpen the " + item.getName() + "."; return todo;case 2: todo = "You hammer on the " + item.getName() + "."; return todo;case 3: todo = "You dip the " + item.getName() + " in the water."; return todo;case 4: todo = "You polish the " + item.getName() + " carefully."; return todo; }  todo = ""; } else if (Materials.isLeather(material)) { switch (state) { case 1: todo = "You sew the " + item.getName() + "."; return todo;case 2: todo = "You punch some holes in the " + item.getName() + "."; return todo;case 3: todo = "You cut away some excess leather from the " + item.getName() + "."; return todo;case 4: todo = "You hammer the " + item.getName() + " and flatten a bulge."; return todo; }  todo = ""; } else if (Materials.isCloth(material)) { switch (state) { case 1: todo = "You backstitch the " + item.getName() + " elegantly."; return todo;case 2: todo = "You cut away some excess cloth from the " + item.getName() + "."; return todo;case 3: todo = "You wash the " + item.getName() + "."; return todo;case 4: todo = "You nimbly slipstitch the " + item.getName() + "."; return todo; }  todo = ""; } else if (Materials.isStone(material)) { switch (state) { case 1: todo = "You carefully chip away some rock from the " + item.getName() + "."; return todo;case 2: todo = "You carefully chip away some rock from the " + item.getName() + "."; return todo;case 3: todo = "You carefully chip away some rock from the " + item.getName() + "."; return todo;case 4: todo = "You carefully chip away some rock from the " + item.getName() + "."; return todo; }  todo = ""; } else if (Materials.isClay(material)) { switch (state) { case 1: todo = "You skillfully fix some irregularities in the " + item.getName() + "."; return todo;case 2: todo = "You add some water to the " + item.getName() + "."; return todo;case 3: todo = "Meticulously you use the " + used.getName() + " to create the desired form of the " + item.getName() + "."; return todo;case 4: todo = "You carefully use the " + used.getName() + " to remove some unnecessary clay from the " + item.getName() + "."; return todo; }  todo = ""; }  return todo;
/*       */   }
/*       */ 
/*       */   
/*       */   private static final String getImproveActionString(byte material, byte state) {
/*  8304 */     String todo = "fixing";
/*  8305 */     if (Materials.isWood(material))
/*       */     
/*  8307 */     { switch (state)
/*       */       
/*       */       { case 1:
/*  8310 */           todo = "carving";
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  8425 */           return todo;case 2: todo = "hammering"; return todo;case 3: todo = "filing"; return todo;case 4: todo = "polishing"; return todo; }  todo = "fixing"; } else if (Materials.isMetal(material)) { switch (state) { case 1: todo = "sharpening"; return todo;case 2: todo = "hammering"; return todo;case 3: todo = "tempering"; return todo;case 4: todo = "polishing"; return todo; }  todo = "fixing"; } else if (Materials.isLeather(material)) { switch (state) { case 1: todo = "sewing"; return todo;case 2: todo = "punching"; return todo;case 3: todo = "cutting"; return todo;case 4: todo = "hammering"; return todo; }  todo = "fixing"; } else if (Materials.isCloth(material)) { switch (state) { case 1: todo = "backstitching"; return todo;case 2: todo = "cutting"; return todo;case 3: todo = "washing"; return todo;case 4: todo = "slipstitching"; return todo; }  todo = "fixing"; } else if (Materials.isStone(material)) { switch (state) { case 1: todo = "chipping"; return todo;case 2: todo = "chipping"; return todo;case 3: todo = "chipping"; return todo;case 4: todo = "chipping"; return todo; }  todo = "fixing"; } else if (Materials.isClay(material)) { switch (state) { case 1: todo = "molding"; return todo;case 2: todo = "watering"; return todo;case 3: todo = "molding"; return todo;case 4: todo = "molding"; return todo; }  todo = "fixing"; }  return todo;
/*       */   }
/*       */ 
/*       */   
/*       */   static final String getImproveAction(byte material, byte state) {
/*  8430 */     String todo = "Fix";
/*  8431 */     if (Materials.isWood(material))
/*       */     
/*  8433 */     { switch (state)
/*       */       
/*       */       { case 1:
/*  8436 */           todo = "Carve";
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  8551 */           return todo;case 2: todo = "Hammer"; return todo;case 3: todo = "File"; return todo;case 4: todo = "Polish"; return todo; }  todo = "Fix"; } else if (Materials.isMetal(material)) { switch (state) { case 1: todo = "Sharpen"; return todo;case 2: todo = "Hammer"; return todo;case 3: todo = "Temper"; return todo;case 4: todo = "Polish"; return todo; }  todo = "Fix"; } else if (Materials.isLeather(material)) { switch (state) { case 1: todo = "Sew"; return todo;case 2: todo = "Punch"; return todo;case 3: todo = "Cut"; return todo;case 4: todo = "Hammer"; return todo; }  todo = "Fix"; } else if (Materials.isCloth(material)) { switch (state) { case 1: todo = "Backstitch"; return todo;case 2: todo = "Cut"; return todo;case 3: todo = "Wash"; return todo;case 4: todo = "Slipstitch"; return todo; }  todo = "Fix"; } else if (Materials.isStone(material)) { switch (state) { case 1: todo = "Chip"; return todo;case 2: todo = "Chip"; return todo;case 3: todo = "Chip"; return todo;case 4: todo = "Chip"; return todo; }  todo = "Fix"; } else if (Materials.isClay(material)) { switch (state) { case 1: todo = "Mold"; return todo;case 2: todo = "Water"; return todo;case 3: todo = "Mold"; return todo;case 4: todo = "Mold"; return todo; }  todo = "Fix"; }  return todo;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static final String getRarityDesc(byte rarity) {
/*  8563 */     switch (rarity) {
/*       */       
/*       */       case 1:
/*  8566 */         return " This is a very rare and interesting version of the item.";
/*       */       case 2:
/*  8568 */         return " This is a supreme example of the item, with fine details and slick design.";
/*       */       case 3:
/*  8570 */         return " This is a fantastic example of the item, with fascinating design details and perfect ideas for functionality.";
/*       */     } 
/*  8572 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static final String getRarityName(byte rarity) {
/*  8578 */     switch (rarity) {
/*       */       
/*       */       case 1:
/*  8581 */         return "rare";
/*       */       case 2:
/*  8583 */         return "supreme";
/*       */       case 3:
/*  8585 */         return "fantastic";
/*       */     } 
/*  8587 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static final int getTemperWaterAmountFor(Item target) {
/*  8593 */     if (target.getWeightGrams() > 10000)
/*  8594 */       return 1000; 
/*  8595 */     if (target.getWeightGrams() < 2000) {
/*  8596 */       return 200;
/*       */     }
/*  8598 */     return target.getWeightGrams() / 10;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static final boolean polishItem(Action act, Creature performer, Item source, Item target, float counter) {
/*  8604 */     byte state = target.creationState;
/*  8605 */     boolean improving = (act.getNumber() == 192);
/*  8606 */     if (counter == 0.0F || counter == 1.0F || act.justTickedSecond()) {
/*       */       
/*  8608 */       if (state == 0 || target.isNewbieItem() || target.isChallengeNewbieItem()) {
/*       */         
/*  8610 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " does not need that.");
/*  8611 */         return true;
/*       */       } 
/*  8613 */       if (source.getWurmId() == target.getWurmId()) {
/*       */         
/*  8615 */         performer.getCommunicator().sendNormalServerMessage("You cannot improve the " + source
/*  8616 */             .getName() + " using itself as a tool.");
/*  8617 */         return true;
/*       */       } 
/*       */ 
/*       */       
/*  8621 */       int templateId = getItemForImprovement(getImproveMaterial(target), state);
/*       */       
/*  8623 */       if (templateId != source.getTemplateId()) {
/*       */         
/*  8625 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/*  8626 */             .getName() + " does not need the touch of " + source.getNameWithGenus() + ".");
/*  8627 */         return true;
/*       */       } 
/*  8629 */       if (templateId == 128 && (target.isMetal() || target.isCloth()))
/*       */       {
/*  8631 */         if (source.getWeightGrams() < getTemperWaterAmountFor(target)) {
/*       */           
/*  8633 */           if (target.isCloth()) {
/*  8634 */             performer.getCommunicator().sendNormalServerMessage("You need more water in order to wash the " + target
/*  8635 */                 .getName() + ".");
/*       */           } else {
/*  8637 */             performer.getCommunicator().sendNormalServerMessage("You need more water in order to cool the " + target
/*  8638 */                 .getName() + ".");
/*  8639 */           }  return true;
/*       */         } 
/*       */       }
/*       */       
/*  8643 */       if (target.getParentId() != -10L && target.getTemplateId() == 386) {
/*       */         
/*       */         try {
/*       */           
/*  8647 */           ItemTemplate temp = target.getRealTemplate();
/*  8648 */           if (temp != null && !temp.isVehicle())
/*       */           {
/*  8650 */             Item parent = target.getParent();
/*       */             
/*  8652 */             if (parent.isNoWorkParent()) {
/*       */               
/*  8654 */               performer.getCommunicator().sendNormalServerMessage("You can't work with the " + target
/*  8655 */                   .getName() + " in the " + parent.getName() + ".");
/*  8656 */               throw new NoSuchItemException("The " + target.getName() + " can't be modified in the " + parent
/*  8657 */                   .getName() + ".");
/*       */             } 
/*  8659 */             if (parent.getContainerSizeX() < temp.getSizeX() || parent
/*  8660 */               .getContainerSizeY() < temp.getSizeY() || parent
/*  8661 */               .getContainerSizeZ() <= temp.getSizeZ())
/*       */             {
/*  8663 */               if (parent.getTemplateId() != 177 && parent.getTemplateId() != 0)
/*       */               {
/*  8665 */                 performer.getCommunicator().sendNormalServerMessage("It's too tight to try and work on the " + target
/*  8666 */                     .getName() + " in the " + parent
/*  8667 */                     .getName() + ".");
/*  8668 */                 return true;
/*       */               }
/*       */             
/*       */             }
/*       */           }
/*       */         
/*  8674 */         } catch (NoSuchItemException noSuchItemException) {}
/*       */       }
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/*  8680 */     Skills skills = performer.getSkills();
/*  8681 */     Skill improve = null;
/*  8682 */     int skillNum = -10;
/*  8683 */     if (improving) {
/*  8684 */       skillNum = getImproveSkill(target);
/*       */     } else {
/*  8686 */       skillNum = getImproveSkill(target.getMaterial(), target.realTemplate);
/*  8687 */     }  if (skillNum == -10) {
/*       */       
/*  8689 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " can not be improved right now.");
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  8694 */       return true;
/*       */     } 
/*  8696 */     int time = 1000;
/*       */     
/*  8698 */     boolean insta = (performer.getPower() >= 5);
/*       */ 
/*       */     
/*       */     try {
/*  8702 */       improve = skills.getSkill(skillNum);
/*       */     }
/*  8704 */     catch (NoSuchSkillException nss) {
/*       */       
/*  8706 */       improve = skills.learn(skillNum, 1.0F);
/*       */     } 
/*       */     
/*  8709 */     if (!insta)
/*       */     {
/*  8711 */       if (target.getDamage() > 0.0F) {
/*       */         
/*  8713 */         performer.getCommunicator().sendNormalServerMessage("Repair the " + target
/*  8714 */             .getName() + " before you try to " + (target.isUnfinished() ? "finish" : "improve") + " it.");
/*  8715 */         return true;
/*       */       } 
/*       */     }
/*  8718 */     double power = 0.0D;
/*  8719 */     double bonus = 0.0D;
/*       */     
/*  8721 */     if (performer.isPriest())
/*  8722 */       bonus = -20.0D; 
/*  8723 */     Skill secondarySkill = null;
/*       */     
/*       */     try {
/*  8726 */       secondarySkill = skills.getSkill(source.getPrimarySkill());
/*       */     }
/*  8728 */     catch (Exception ex) {
/*       */ 
/*       */       
/*       */       try {
/*  8732 */         secondarySkill = skills.learn(source.getPrimarySkill(), 1.0F);
/*       */       }
/*  8734 */       catch (Exception exception) {}
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  8740 */     float runeModifier = 1.0F;
/*  8741 */     if (target.getSpellEffects() != null)
/*       */     {
/*  8743 */       runeModifier = target.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_IMPPERCENT);
/*       */     }
/*  8745 */     float imbueEnhancement = 1.0F + source.getSkillSpellImprovement(skillNum) / 100.0F;
/*  8746 */     double improveBonus = 0.23047D * imbueEnhancement * runeModifier;
/*  8747 */     double max = improve.getKnowledge(0.0D) + (100.0D - improve.getKnowledge(0.0D)) * improveBonus;
/*       */     
/*  8749 */     double diff = Math.max(0.0D, max - target.getQualityLevel());
/*  8750 */     float skillgainMod = 1.0F;
/*       */ 
/*       */ 
/*       */     
/*  8754 */     if (diff <= 0.0D)
/*  8755 */       skillgainMod = 2.0F; 
/*  8756 */     if (counter == 1.0F) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  8766 */       if (!insta && 
/*  8767 */         target.isMetal() && !target.isNoTake())
/*       */       {
/*  8769 */         if (target.getTemperature() < 3500) {
/*       */           
/*  8771 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/*  8772 */               .getName() + " needs to be glowing hot to be improved.");
/*  8773 */           return true;
/*       */         } 
/*       */       }
/*  8776 */       String improvestring = getImproveActionString(getImproveMaterial(target), state);
/*  8777 */       performer.getCommunicator()
/*  8778 */         .sendNormalServerMessage("You start " + improvestring + " the " + target.getName() + ".");
/*  8779 */       Server.getInstance().broadCastAction(performer
/*  8780 */           .getName() + " starts " + improvestring + " " + target.getNameWithGenus() + ".", performer, 5);
/*       */       
/*  8782 */       time = Actions.getImproveActionTime(performer, source);
/*  8783 */       performer.sendActionControl(improvestring, true, time);
/*       */       
/*  8785 */       act.setTimeLeft(time);
/*  8786 */       double impmod = 0.5D;
/*  8787 */       if (improving) {
/*  8788 */         impmod = 1.0D;
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  8795 */       double mod = impmod * ((100.0F - target.getQualityLevel()) / 20.0F / 100.0F * (Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat()) / 2.0F);
/*       */       
/*  8797 */       if (improving)
/*       */       {
/*       */ 
/*       */         
/*  8801 */         power = improve.skillCheck(target.getQualityLevel(), source, bonus, true, 1.0F);
/*       */         
/*  8803 */         if (power < 0.0D)
/*       */         {
/*  8805 */           act.setFailSecond((int)Math.max(20.0F, time * Server.rand.nextFloat()));
/*  8806 */           act.setPower((float)(-mod * Math.max(1.0D, diff)));
/*       */         
/*       */         }
/*       */         else
/*       */         {
/*  8811 */           if (diff <= 0.0D) {
/*  8812 */             mod *= 0.009999999776482582D;
/*       */           }
/*  8814 */           double regain = 1.0D;
/*  8815 */           if (target.getQualityLevel() < target.getOriginalQualityLevel())
/*  8816 */             regain = 2.0D; 
/*  8817 */           diff *= regain;
/*  8818 */           int tid = target.getTemplateId();
/*       */ 
/*       */           
/*  8821 */           if (target.isArmour() || target.isWeapon() || target.isShield() || tid == 455 || tid == 454 || tid == 456 || tid == 453 || tid == 451 || tid == 452)
/*       */           {
/*       */             
/*  8824 */             mod *= 2.0D; } 
/*  8825 */           Titles.Title title = performer.getTitle();
/*  8826 */           if (title != null && title.getSkillId() == improve.getNumber() && (target
/*  8827 */             .isArmour() || target.isCreatureWearableOnly()))
/*  8828 */             mod *= 1.2999999523162842D; 
/*  8829 */           act.setPower((float)(mod * Math.max(1.0D, diff)));
/*       */         }
/*       */       
/*       */       }
/*       */       else
/*       */       {
/*  8835 */         double regain = 1.0D;
/*  8836 */         if (target.getQualityLevel() < target.getOriginalQualityLevel())
/*  8837 */           regain = 2.0D; 
/*  8838 */         diff *= regain;
/*  8839 */         int tid = target.getTemplateId();
/*       */ 
/*       */         
/*  8842 */         if (target.isArmour() || target.isCreatureWearableOnly() || target.isWeapon() || target.isShield() || tid == 455 || tid == 454 || tid == 456 || tid == 453 || tid == 451 || tid == 452)
/*       */         {
/*       */           
/*  8845 */           mod *= 2.0D; } 
/*  8846 */         Titles.Title title = performer.getTitle();
/*  8847 */         if (title != null && title.getSkillId() == improve.getNumber() && (target
/*  8848 */           .isArmour() || target.isCreatureWearableOnly()))
/*  8849 */           mod *= 1.2999999523162842D; 
/*  8850 */         act.setPower((float)(mod * Math.max(1.0D, diff)));
/*       */       
/*       */       }
/*       */     
/*       */     }
/*       */     else {
/*       */       
/*  8857 */       time = act.getTimeLeft();
/*  8858 */       float failsec = act.getFailSecond();
/*  8859 */       power = act.getPower();
/*  8860 */       if (counter >= failsec) {
/*       */         
/*  8862 */         if (secondarySkill != null) {
/*  8863 */           bonus = Math.max(bonus, secondarySkill
/*       */               
/*  8865 */               .skillCheck(target.getQualityLevel(), source, bonus, false, 
/*  8866 */                 performer.isPriest() ? (counter / 3.0F) : (counter / 2.0F)));
/*       */         }
/*       */         
/*  8869 */         if (performer.isPriest())
/*  8870 */           bonus = Math.min(bonus, 0.0D); 
/*  8871 */         improve.skillCheck(target.getQualityLevel(), source, bonus, false, performer.isPriest() ? (counter / 2.0F) : counter);
/*       */         
/*  8873 */         if (power != 0.0D) {
/*       */ 
/*       */           
/*  8876 */           if (!target.isBodyPart())
/*       */           {
/*  8878 */             if (!target.isLiquid())
/*       */             {
/*  8880 */               target.setDamage(target.getDamage() - act.getPower());
/*  8881 */               performer.getCommunicator().sendNormalServerMessage("You damage the " + target
/*  8882 */                   .getName() + " a little.");
/*  8883 */               Server.getInstance().broadCastAction(performer
/*  8884 */                   .getName() + " grunts as " + performer.getHeSheItString() + " damages " + target
/*  8885 */                   .getNameWithGenus() + " a little.", performer, 5);
/*       */             }
/*       */             else
/*       */             {
/*  8889 */               performer.getCommunicator().sendNormalServerMessage("You fail.");
/*  8890 */               Server.getInstance().broadCastAction(performer
/*  8891 */                   .getName() + " grunts as " + performer.getHeSheItString() + " fails.", performer, 5);
/*       */             }
/*       */           
/*       */           }
/*       */         }
/*       */         else {
/*       */           
/*  8898 */           performer.getCommunicator().sendNormalServerMessage("You realize you almost damaged the " + target
/*  8899 */               .getName() + " and stop.");
/*  8900 */           Server.getInstance().broadCastAction(performer
/*  8901 */               .getName() + " stops improving " + target.getNameWithGenus() + ".", performer, 5);
/*       */         } 
/*  8903 */         performer.getStatus().modifyStamina(-counter * 1000.0F);
/*  8904 */         return true;
/*       */       } 
/*       */     } 
/*  8907 */     if (act.mayPlaySound())
/*       */     {
/*  8909 */       sendImproveSound(performer, source, target, skillNum);
/*       */     }
/*  8911 */     if (counter * 10.0F > time || insta) {
/*       */       
/*  8913 */       if (act.getRarity() != 0)
/*       */       {
/*  8915 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/*  8917 */       float maxGain = 1.0F;
/*       */       
/*  8919 */       if (source.isLiquid() && (target.isMetal() || target.isUnfired() || target
/*  8920 */         .isCloth() || Materials.isCloth(getImproveMaterial(target)))) {
/*       */         
/*  8922 */         if (source.getTemplateId() == 128)
/*       */         {
/*  8924 */           source.setWeight(source.getWeightGrams() - getTemperWaterAmountFor(target), true);
/*       */         }
/*       */         else
/*       */         {
/*  8928 */           int usedWeight = (int)Math.min(500.0D, Math.max(1.0D, target.getWeightGrams() * 0.05D));
/*  8929 */           if (source.getWeightGrams() < usedWeight)
/*  8930 */             maxGain = Math.min(1.0F, source.getWeightGrams() / usedWeight); 
/*  8931 */           source.setWeight(source.getWeightGrams() - usedWeight, true);
/*       */         }
/*       */       
/*  8934 */       } else if (!source.isLiquid()) {
/*  8935 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*  8936 */       }  if (secondarySkill != null) {
/*  8937 */         bonus = Math.max(bonus, secondarySkill
/*       */             
/*  8939 */             .skillCheck(target.getQualityLevel(), source, bonus, false, skillgainMod * (
/*  8940 */               performer.isPriest() ? (counter / 3.0F) : (counter / 2.0F))));
/*       */       }
/*       */       
/*  8943 */       if (performer.isPriest())
/*  8944 */         bonus = Math.min(bonus, 0.0D); 
/*  8945 */       improve.skillCheck(target.getQualityLevel(), source, bonus, false, skillgainMod * (
/*  8946 */           performer.isPriest() ? (counter / 2.0F) : counter));
/*  8947 */       String improvestring = getImproveActionString(getImproveMaterial(target), state);
/*  8948 */       Server.getInstance().broadCastAction(performer
/*  8949 */           .getName() + " ceases " + improvestring + " " + target.getNameWithGenus() + ".", performer, 5);
/*       */       
/*  8951 */       power = act.getPower();
/*       */       
/*  8953 */       if (power > 0.0D) {
/*       */ 
/*       */         
/*  8956 */         byte rarity = target.getRarity();
/*       */         
/*  8958 */         float rarityChance = 0.2F;
/*  8959 */         if (target.getSpellEffects() != null)
/*       */         {
/*  8961 */           rarityChance *= target.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RARITYIMP);
/*       */         }
/*       */         
/*  8964 */         if (act.getRarity() > rarity && 
/*  8965 */           Server.rand.nextFloat() <= rarityChance) {
/*  8966 */           rarity = act.getRarity();
/*       */         }
/*  8968 */         byte newState = getNewCreationState(getImproveMaterial(target));
/*       */ 
/*       */         
/*  8971 */         while (newState != 0 && newState == state) {
/*       */           
/*  8973 */           newState = getNewCreationState(getImproveMaterial(target));
/*  8974 */           int temp = getItemForImprovement(getImproveMaterial(target), newState);
/*  8975 */           if (temp == target.getTemplateId())
/*  8976 */             newState = (byte)(newState - 1); 
/*       */         } 
/*  8978 */         target.setCreationState(newState);
/*  8979 */         Item toRarify = target;
/*  8980 */         if (target.getTemplateId() == 128)
/*  8981 */           toRarify = source; 
/*  8982 */         if (rarity > toRarify.getRarity()) {
/*       */           
/*  8984 */           toRarify.setRarity(rarity);
/*       */           
/*  8986 */           for (Item sub : toRarify.getItems()) {
/*       */             
/*  8988 */             if (sub == null) {
/*       */               continue;
/*       */             }
/*  8991 */             if (sub.isComponentItem()) {
/*  8992 */               sub.setRarity(rarity);
/*       */             }
/*       */           } 
/*  8995 */           if (toRarify.getRarity() > 2) {
/*  8996 */             performer.achievement(300);
/*  8997 */           } else if (toRarify.getRarity() == 1) {
/*  8998 */             performer.achievement(301);
/*  8999 */           } else if (toRarify.getRarity() == 2) {
/*  9000 */             performer.achievement(302);
/*       */           } 
/*  9002 */         }  if (newState != 0) {
/*       */           
/*  9004 */           String newString = getNeededCreationAction(getImproveMaterial(target), newState, target);
/*  9005 */           performer.getCommunicator().sendNormalServerMessage(newString);
/*       */ 
/*       */         
/*       */         }
/*  9009 */         else if (target.getTemplateId() == 386) {
/*       */           
/*  9011 */           MissionTriggers.activateTriggers(performer, target, 148, 0L, 1);
/*  9012 */           performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is finished!");
/*       */           
/*  9014 */           target.setTemplateId(target.realTemplate);
/*  9015 */           if (target.isUseOnGroundOnly())
/*       */           {
/*  9017 */             if (target.getOwnerId() != performer.getWurmId()) {
/*       */               
/*       */               try {
/*       */                 
/*  9021 */                 target.putItemInfrontof(performer);
/*       */               }
/*  9023 */               catch (Exception nsz) {
/*       */                 
/*  9025 */                 logger.log(Level.INFO, performer.getName() + ": " + nsz.getMessage());
/*       */               } 
/*       */             }
/*       */           }
/*  9029 */           skillNum = getImproveSkill(target);
/*  9030 */           int templateId = getImproveTemplateId(target);
/*  9031 */           if (skillNum != -10) {
/*       */             
/*       */             try {
/*       */               
/*  9035 */               ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  9036 */               performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9037 */                   .getName() + " could be improved with " + temp.getNameWithGenus() + ".");
/*       */             }
/*  9039 */             catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */           
/*       */           }
/*       */         
/*       */         }
/*       */         else {
/*       */           
/*  9046 */           performer.getCommunicator().sendNormalServerMessage("You improve the " + target.getName() + " a bit.");
/*       */           
/*  9048 */           skillNum = getImproveSkill(target);
/*  9049 */           int templateId = getImproveTemplateId(target);
/*  9050 */           if (skillNum != -10) {
/*       */             
/*       */             try {
/*       */               
/*  9054 */               ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  9055 */               performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9056 */                   .getName() + " could be improved with " + temp.getNameWithGenus() + ".");
/*       */             }
/*  9058 */             catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */           }
/*       */         } 
/*       */ 
/*       */ 
/*       */         
/*  9064 */         if (insta) {
/*  9065 */           performer.getCommunicator().sendNormalServerMessage("before: " + target
/*  9066 */               .getQualityLevel() + " now: " + (target.getQualityLevel() + power * maxGain) + " power=" + power + " maxGain=" + maxGain);
/*       */         }
/*       */         
/*  9069 */         float oldQL = target.getQualityLevel();
/*  9070 */         boolean wasHighest = Items.isHighestQLForTemplate(target.getTemplateId(), target.getQualityLevel(), target
/*  9071 */             .getWurmId(), true);
/*  9072 */         float modifier = 1.0F;
/*  9073 */         if (target.getSpellEffects() != null)
/*       */         {
/*  9075 */           modifier = target.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_IMPQL);
/*       */         }
/*  9077 */         modifier *= target.getMaterialImpBonus();
/*  9078 */         target.setQualityLevel(Math.min(100.0F, (float)(target.getQualityLevel() + power * maxGain * modifier)));
/*       */         
/*  9080 */         if (target.getQualityLevel() > target.getOriginalQualityLevel())
/*       */         {
/*  9082 */           target.setOriginalQualityLevel(target.getQualityLevel());
/*  9083 */           triggerImproveAchievements(performer, target, improve, wasHighest, oldQL);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*       */         }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       }
/*       */       else {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  9105 */         if (insta)
/*  9106 */           performer.getCommunicator().sendNormalServerMessage("Dam before: " + target
/*  9107 */               .getDamage() + " now: " + (target.getDamage() - power) + " power=" + power); 
/*  9108 */         if (!target.isBodyPart())
/*       */         {
/*  9110 */           if (!target.isLiquid()) {
/*       */             
/*  9112 */             target.setDamage(target.getDamage() - (float)power);
/*  9113 */             performer.getCommunicator()
/*  9114 */               .sendNormalServerMessage("You damage the " + target.getName() + " a little.");
/*  9115 */             Server.getInstance().broadCastAction(performer
/*  9116 */                 .getName() + " grunts as " + performer.getHeSheItString() + " damages " + target
/*  9117 */                 .getNameWithGenus() + " a little.", performer, 5);
/*       */           }
/*       */           else {
/*       */             
/*  9121 */             performer.getCommunicator().sendNormalServerMessage("You fail.");
/*  9122 */             Server.getInstance().broadCastAction(performer
/*  9123 */                 .getName() + " grunts as " + performer.getHeSheItString() + " fails.", performer, 5);
/*       */           } 
/*       */         }
/*       */       } 
/*  9127 */       performer.getStatus().modifyStamina(-counter * 1000.0F);
/*  9128 */       return true;
/*       */     } 
/*  9130 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean temper(Action act, Creature performer, Item source, Item target, float counter) {
/*  9136 */     byte state = source.creationState;
/*  9137 */     boolean improving = (act.getNumber() == 192);
/*  9138 */     boolean insta = (performer.getPower() >= 5);
/*       */     
/*  9140 */     if (counter == 0.0F || counter == 1.0F || act.justTickedSecond()) {
/*       */       
/*  9142 */       if (state == 0 || source.isNewbieItem() || source.isChallengeNewbieItem()) {
/*       */         
/*  9144 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/*  9145 */             .getName() + " does not need tempering right now.");
/*  9146 */         return true;
/*       */       } 
/*       */ 
/*       */       
/*  9150 */       int templateId = getItemForImprovement(source.getMaterial(), state);
/*  9151 */       if (templateId != target.getTemplateId()) {
/*       */         
/*  9153 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/*  9154 */             .getName() + " does not need tempering right now.");
/*  9155 */         return true;
/*       */       } 
/*       */       
/*  9158 */       if (!insta)
/*       */       {
/*  9160 */         if (source.getDamage() > 0.0F) {
/*       */           
/*  9162 */           performer.getCommunicator().sendNormalServerMessage("Repair the " + source
/*  9163 */               .getName() + " before you temper it.");
/*  9164 */           return true;
/*       */         } 
/*       */       }
/*  9167 */       if (target.getWeightGrams() < getTemperWaterAmountFor(source)) {
/*       */         
/*  9169 */         performer.getCommunicator().sendNormalServerMessage("You need more water in order to cool the " + source
/*  9170 */             .getName() + ".");
/*  9171 */         return true;
/*       */       } 
/*  9173 */       if (source.getParentId() != -10L && source.getTemplateId() == 386) {
/*       */         
/*       */         try {
/*       */           
/*  9177 */           ItemTemplate temp = source.getRealTemplate();
/*  9178 */           if (temp != null && !temp.isVehicle()) {
/*       */             
/*  9180 */             Item parent = source.getParent();
/*       */             
/*  9182 */             if (parent.isNoWorkParent()) {
/*       */               
/*  9184 */               performer.getCommunicator().sendNormalServerMessage("You can't work with the " + source
/*  9185 */                   .getName() + " in the " + parent.getName() + ".");
/*  9186 */               throw new NoSuchItemException("The " + source.getName() + " can't be modified in the " + parent
/*  9187 */                   .getName() + ".");
/*       */             } 
/*  9189 */             if (parent.getContainerSizeX() < temp.getSizeX() || parent
/*  9190 */               .getContainerSizeY() < temp.getSizeY() || parent
/*  9191 */               .getContainerSizeZ() <= temp.getSizeZ())
/*       */             {
/*  9193 */               if (parent.getTemplateId() != 177 && parent.getTemplateId() != 0)
/*       */               {
/*  9195 */                 performer.getCommunicator().sendNormalServerMessage("It's too tight to try and work on the " + source
/*  9196 */                     .getName() + " in the " + parent
/*  9197 */                     .getName() + ".");
/*  9198 */                 return true;
/*       */               }
/*       */             
/*       */             }
/*       */           } 
/*  9203 */         } catch (NoSuchItemException noSuchItemException) {}
/*       */       }
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/*  9209 */     Skills skills = performer.getSkills();
/*  9210 */     Skill improve = null;
/*  9211 */     int skillNum = -10;
/*  9212 */     if (improving) {
/*  9213 */       skillNum = getImproveSkill(source);
/*       */     } else {
/*  9215 */       skillNum = getImproveSkill(source.getMaterial(), source.realTemplate);
/*  9216 */     }  if (skillNum == -10) {
/*       */       
/*  9218 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " can not be tempered with that.");
/*  9219 */       return true;
/*       */     } 
/*  9221 */     int time = 1000;
/*       */     
/*       */     try {
/*  9224 */       improve = skills.getSkill(skillNum);
/*       */     }
/*  9226 */     catch (NoSuchSkillException nss) {
/*       */       
/*  9228 */       improve = skills.learn(skillNum, 1.0F);
/*       */     } 
/*       */     
/*  9231 */     double power = 0.0D;
/*  9232 */     double bonus = 0.0D;
/*       */ 
/*       */     
/*  9235 */     if (performer.isPriest()) {
/*  9236 */       bonus -= 10.0D;
/*       */     }
/*       */ 
/*       */ 
/*       */     
/*  9241 */     float runeModifier = 1.0F;
/*  9242 */     if (source.getSpellEffects() != null)
/*       */     {
/*  9244 */       runeModifier = source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_IMPPERCENT);
/*       */     }
/*  9246 */     float imbueEnhancement = 1.0F + source.getSkillSpellImprovement(skillNum) / 100.0F;
/*  9247 */     double improveBonus = 0.23047D * imbueEnhancement * runeModifier;
/*  9248 */     double max = improve.getKnowledge(0.0D) + (100.0D - improve.getKnowledge(0.0D)) * improveBonus;
/*       */     
/*  9250 */     double diff = Math.max(0.0D, max - source.getQualityLevel());
/*  9251 */     float skillgainMod = 1.0F;
/*       */ 
/*       */ 
/*       */     
/*  9255 */     if (diff <= 0.0D)
/*  9256 */       skillgainMod = 2.0F; 
/*  9257 */     if (counter == 1.0F) {
/*       */       
/*  9259 */       if (!insta && 
/*  9260 */         source.isMetal())
/*       */       {
/*  9262 */         if (source.getTemperature() < 3500 && !source.isNoTake()) {
/*       */           
/*  9264 */           performer.getCommunicator().sendNormalServerMessage("The " + source
/*  9265 */               .getName() + " needs to be glowing hot to be tempered.");
/*  9266 */           return true;
/*       */         } 
/*       */       }
/*  9269 */       if (improving);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  9280 */       String improvestring = getImproveActionString(getImproveMaterial(source), state);
/*  9281 */       performer.getCommunicator()
/*  9282 */         .sendNormalServerMessage("You start " + improvestring + " the " + source.getName() + ".");
/*  9283 */       Server.getInstance().broadCastAction(performer
/*  9284 */           .getName() + " starts " + improvestring + " " + source.getNameWithGenus() + ".", performer, 5);
/*       */       
/*  9286 */       time = Actions.getImproveActionTime(performer, source);
/*  9287 */       performer.sendActionControl(improvestring, true, time);
/*       */       
/*  9289 */       act.setTimeLeft(time);
/*  9290 */       double impmod = 0.5D;
/*  9291 */       if (improving) {
/*  9292 */         impmod = 1.0D;
/*       */       }
/*       */ 
/*       */ 
/*       */       
/*  9297 */       double mod = impmod * ((100.0F - source.getQualityLevel()) / 20.0F / 100.0F * (Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat() + Server.rand.nextFloat()) / 2.0F);
/*       */       
/*  9299 */       if (improving)
/*       */       {
/*  9301 */         power = improve.skillCheck(source.getQualityLevel(), bonus, true, 1.0F);
/*  9302 */         if (power < 0.0D)
/*       */         {
/*  9304 */           act.setFailSecond((int)Math.max(20.0F, time * Server.rand.nextFloat()));
/*  9305 */           act.setPower((float)(-mod * Math.max(1.0D, diff)));
/*       */         
/*       */         }
/*       */         else
/*       */         {
/*       */           
/*  9311 */           if (diff <= 0.0D) {
/*  9312 */             mod *= 0.009999999776482582D;
/*       */           }
/*  9314 */           double regain = 1.0D;
/*  9315 */           if (source.getQualityLevel() < source.getOriginalQualityLevel())
/*  9316 */             regain = 2.0D; 
/*  9317 */           diff *= regain;
/*  9318 */           int tid = source.getTemplateId();
/*       */ 
/*       */           
/*  9321 */           if (tid == 455 || tid == 454 || tid == 456 || tid == 453 || tid == 451 || tid == 452)
/*       */           {
/*  9323 */             mod *= 2.0D; } 
/*  9324 */           act.setPower((float)(mod * Math.max(1.0D, diff)));
/*       */         }
/*       */       
/*       */       }
/*       */       else
/*       */       {
/*  9330 */         double regain = 1.0D;
/*  9331 */         if (source.getQualityLevel() < source.getOriginalQualityLevel())
/*  9332 */           regain = 5.0D; 
/*  9333 */         diff = Math.min(diff, 10.0D * regain);
/*  9334 */         int tid = source.getTemplateId();
/*       */ 
/*       */         
/*  9337 */         if (tid == 455 || tid == 454 || tid == 456 || tid == 453 || tid == 451 || tid == 452)
/*       */         {
/*  9339 */           mod *= 3.0D; } 
/*  9340 */         act.setPower((float)(mod * Math.max(1.0D, diff)));
/*       */       
/*       */       }
/*       */     
/*       */     }
/*       */     else {
/*       */       
/*  9347 */       time = act.getTimeLeft();
/*  9348 */       float failsec = act.getFailSecond();
/*  9349 */       power = act.getPower();
/*  9350 */       if (counter >= failsec) {
/*       */         
/*  9352 */         improve.skillCheck(source.getQualityLevel(), bonus, false, performer.isPriest() ? (counter / 2.0F) : counter);
/*  9353 */         if (power != 0.0D) {
/*       */ 
/*       */           
/*  9356 */           source.setDamage(source.getDamage() - act.getPower());
/*  9357 */           performer.getCommunicator().sendNormalServerMessage("You damage the " + source.getName() + " a little.");
/*  9358 */           Server.getInstance().broadCastAction(performer
/*  9359 */               .getName() + " grunts as " + performer.getHeSheItString() + " damages " + source
/*  9360 */               .getNameWithGenus() + " a little.", performer, 5);
/*       */         }
/*       */         else {
/*       */           
/*  9364 */           performer.getCommunicator().sendNormalServerMessage("You realize you almost damaged the " + source
/*  9365 */               .getName() + " and stop.");
/*  9366 */           Server.getInstance().broadCastAction(performer
/*  9367 */               .getName() + " stops improving " + source.getNameWithGenus() + ".", performer, 5);
/*       */         } 
/*  9369 */         performer.getStatus().modifyStamina(-counter * 1000.0F);
/*  9370 */         return true;
/*       */       } 
/*       */     } 
/*  9373 */     if (act.mayPlaySound())
/*       */     {
/*  9375 */       Methods.sendSound(performer, "sound.work.smithing.temper");
/*       */     }
/*  9377 */     if (counter * 10.0F > time || insta) {
/*       */       
/*  9379 */       if (act.getRarity() != 0)
/*       */       {
/*  9381 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/*  9383 */       if (target.getWeightGrams() < getTemperWaterAmountFor(source)) {
/*       */         
/*  9385 */         target.setWeight(target.getWeightGrams() - getTemperWaterAmountFor(source), true);
/*  9386 */         performer.getCommunicator().sendNormalServerMessage("There is too little water to temper the metal. It boils away instantly.");
/*       */         
/*  9388 */         return true;
/*       */       } 
/*  9390 */       target.setWeight(target.getWeightGrams() - getTemperWaterAmountFor(source), true);
/*       */       
/*  9392 */       improve.skillCheck(source.getQualityLevel(), bonus, false, skillgainMod * (
/*  9393 */           performer.isPriest() ? (counter / 2.0F) : counter));
/*  9394 */       Server.getInstance().broadCastAction(performer.getName() + " tempers " + source.getNameWithGenus() + ".", performer, 5);
/*       */ 
/*       */       
/*  9397 */       power = act.getPower();
/*  9398 */       if (power > 0.0D) {
/*       */         
/*  9400 */         byte newState = getNewCreationState(source.getMaterial());
/*  9401 */         int tempr = getItemForImprovement(source.getMaterial(), newState);
/*  9402 */         if (tempr == source.getTemplateId())
/*  9403 */           newState = (byte)(newState - 1); 
/*  9404 */         source.setCreationState(newState);
/*       */ 
/*       */ 
/*       */         
/*  9408 */         byte rarity = source.getRarity();
/*       */         
/*  9410 */         float rarityChance = 0.2F;
/*  9411 */         if (source.getSpellEffects() != null)
/*       */         {
/*  9413 */           rarityChance *= source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RARITYIMP);
/*       */         }
/*  9415 */         if (act.getRarity() > rarity && 
/*  9416 */           Server.rand.nextFloat() <= rarityChance)
/*  9417 */           rarity = act.getRarity(); 
/*  9418 */         Item toRarify = source;
/*  9419 */         if (source.getTemplateId() == 128)
/*  9420 */           toRarify = target; 
/*  9421 */         if (rarity > toRarify.getRarity()) {
/*       */           
/*  9423 */           toRarify.setRarity(rarity);
/*       */           
/*  9425 */           for (Item sub : toRarify.getItems()) {
/*       */             
/*  9427 */             if (sub == null) {
/*       */               continue;
/*       */             }
/*  9430 */             if (sub.isComponentItem()) {
/*  9431 */               sub.setRarity(rarity);
/*       */             }
/*       */           } 
/*  9434 */           if (toRarify.getRarity() > 2) {
/*  9435 */             performer.achievement(300);
/*  9436 */           } else if (toRarify.getRarity() == 1) {
/*  9437 */             performer.achievement(301);
/*  9438 */           } else if (toRarify.getRarity() == 2) {
/*  9439 */             performer.achievement(302);
/*       */           } 
/*  9441 */         }  if (newState != 0) {
/*       */           
/*  9443 */           String newString = getNeededCreationAction(source.getMaterial(), newState, source);
/*  9444 */           performer.getCommunicator().sendNormalServerMessage(newString);
/*       */ 
/*       */         
/*       */         }
/*  9448 */         else if (source.getTemplateId() == 386) {
/*       */           
/*  9450 */           performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " is finished!");
/*       */           
/*  9452 */           source.setTemplateId(source.realTemplate);
/*  9453 */           skillNum = getImproveSkill(source);
/*  9454 */           int templateId = getImproveTemplateId(source);
/*  9455 */           if (skillNum != -10) {
/*       */             
/*       */             try {
/*       */               
/*  9459 */               ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  9460 */               performer.getCommunicator().sendNormalServerMessage("The " + source
/*  9461 */                   .getName() + " could be improved with " + temp.getNameWithGenus() + ".");
/*       */             }
/*  9463 */             catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */           
/*       */           }
/*       */         
/*       */         }
/*       */         else {
/*       */           
/*  9470 */           performer.getCommunicator().sendNormalServerMessage("You improve the " + source.getName() + " a bit.");
/*       */           
/*  9472 */           skillNum = getImproveSkill(source);
/*  9473 */           int templateId = getImproveTemplateId(source);
/*  9474 */           if (skillNum != -10) {
/*       */             
/*       */             try {
/*       */               
/*  9478 */               ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  9479 */               performer.getCommunicator().sendNormalServerMessage("The " + source
/*  9480 */                   .getName() + " could be improved with " + temp.getNameWithGenus() + ".");
/*       */             }
/*  9482 */             catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */           }
/*       */         } 
/*       */ 
/*       */ 
/*       */         
/*  9488 */         if (insta) {
/*  9489 */           performer.getCommunicator().sendNormalServerMessage("before: " + source
/*  9490 */               .getQualityLevel() + " now: " + (source.getQualityLevel() + power) + " power=" + power);
/*       */         }
/*  9492 */         float oldQL = source.getQualityLevel();
/*  9493 */         boolean wasHighest = Items.isHighestQLForTemplate(source.getTemplateId(), source.getQualityLevel(), source
/*  9494 */             .getWurmId(), true);
/*  9495 */         float modifier = 1.0F;
/*  9496 */         if (source.getSpellEffects() != null)
/*       */         {
/*  9498 */           modifier = source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_IMPQL);
/*       */         }
/*  9500 */         modifier *= target.getMaterialImpBonus();
/*  9501 */         source.setQualityLevel(Math.min(100.0F, (float)(source.getQualityLevel() + power * modifier)));
/*       */         
/*  9503 */         if (source.getQualityLevel() > source.getOriginalQualityLevel())
/*       */         {
/*  9505 */           source.setOriginalQualityLevel(source.getQualityLevel());
/*       */           
/*  9507 */           triggerImproveAchievements(performer, source, improve, wasHighest, oldQL);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*       */         }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       }
/*       */       else {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  9530 */         if (insta) {
/*  9531 */           performer.getCommunicator().sendNormalServerMessage("Dam before: " + source
/*  9532 */               .getDamage() + " now: " + (source.getDamage() - power) + " power=" + power);
/*       */         }
/*  9534 */         source.setDamage(source.getDamage() - (float)power);
/*       */         
/*  9536 */         performer.getCommunicator().sendNormalServerMessage("You damage the " + source.getName() + " a little.");
/*  9537 */         Server.getInstance().broadCastAction(performer
/*  9538 */             .getName() + " grunts as " + performer.getHeSheItString() + " damages " + source
/*  9539 */             .getNameWithGenus() + " a little.", performer, 5);
/*       */         
/*  9541 */         performer.achievement(206);
/*       */       } 
/*  9543 */       performer.getStatus().modifyStamina(-counter * 1000.0F);
/*  9544 */       return true;
/*       */     } 
/*  9546 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public static void sendImproveSound(Creature performer, Item source, Item target, int skillNum) {
/*  9551 */     String sound = "";
/*  9552 */     int stid = source.getTemplateId();
/*  9553 */     if (stid == 296) {
/*  9554 */       sound = "sound.work.smithing.whetstone";
/*  9555 */     } else if (stid == 313 || stid == 171) {
/*  9556 */       sound = "sound.work.smithing.polish";
/*  9557 */     } else if (stid == 24) {
/*  9558 */       sound = "sound.work.carpentry.saw";
/*  9559 */     } else if (stid == 8) {
/*  9560 */       sound = "sound.work.carpentry.carvingknife";
/*  9561 */     } else if (stid == 388) {
/*  9562 */       sound = "sound.work.carpentry.rasp";
/*  9563 */     } else if (target.isWood()) {
/*  9564 */       sound = (Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2";
/*  9565 */     } else if (target.isMetal()) {
/*  9566 */       sound = "sound.work.smithing.metal";
/*  9567 */     } else if (target.isStone()) {
/*       */       
/*  9569 */       if (skillNum == 10074 || stid == 97) {
/*  9570 */         sound = "sound.work.stonecutting";
/*       */       } else {
/*  9572 */         sound = "sound.work.masonry";
/*       */       } 
/*  9574 */     }  Methods.sendSound(performer, sound);
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean seal(Creature performer, Item source, Item target, Action act) {
/*  9579 */     boolean done = false;
/*  9580 */     if (!target.canBeSealedByPlayer() && !target.canBePeggedByPlayer()) {
/*       */       
/*  9582 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9583 */           .getName() + " cannot be sealed.");
/*  9584 */       return true;
/*       */     } 
/*  9586 */     if (target.getItems().isEmpty()) {
/*       */       
/*  9588 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9589 */           .getName() + " cannot be sealed as there is nothing in it.");
/*  9590 */       return true;
/*       */     } 
/*       */     
/*  9593 */     Item liquid = null;
/*  9594 */     for (Item item : target.getItems()) {
/*       */       
/*  9596 */       if (item.isLiquid())
/*  9597 */         liquid = item; 
/*       */     } 
/*  9599 */     if (liquid == null) {
/*       */       
/*  9601 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9602 */           .getName() + " cannot be sealed as there is no liquid in it.");
/*  9603 */       return true;
/*       */     } 
/*  9605 */     if (act.currentSecond() == 1) {
/*       */       
/*  9607 */       act.setTimeLeft(50);
/*  9608 */       performer.getCommunicator().sendNormalServerMessage("You start to seal the " + target.getName() + ".");
/*  9609 */       Server.getInstance().broadCastAction(performer
/*  9610 */           .getName() + " starts to seal " + target.getNameWithGenus() + ".", performer, 5);
/*  9611 */       performer.sendActionControl(Actions.actionEntrys[739].getVerbString(), true, act.getTimeLeft());
/*       */     }
/*       */     else {
/*       */       
/*  9615 */       if (source.getTemplateId() == 561) {
/*       */         
/*  9617 */         if (act.currentSecond() == 2) {
/*  9618 */           performer.getCommunicator().sendNormalServerMessage("You position the peg over the hole on the top.");
/*  9619 */         } else if (act.currentSecond() == 3) {
/*  9620 */           performer.getCommunicator().sendNormalServerMessage("You press the peg in slowly so not to disturb the liquid.");
/*  9621 */         } else if (act.currentSecond() == 4) {
/*  9622 */           performer.getCommunicator().sendNormalServerMessage("You give the peg one final tap.");
/*       */         }
/*       */       
/*       */       }
/*  9626 */       else if (act.currentSecond() == 2) {
/*  9627 */         performer.getCommunicator().sendNormalServerMessage("You carefully wax the cloth.");
/*  9628 */       } else if (act.currentSecond() == 3) {
/*  9629 */         performer.getCommunicator().sendNormalServerMessage("You put the waxed cloth over the " + target.getName() + ".");
/*  9630 */       } else if (act.currentSecond() == 4) {
/*  9631 */         performer.getCommunicator().sendNormalServerMessage("You tie the string around " + target.getName() + ".");
/*       */       } 
/*  9633 */       int timeleft = act.getTimeLeft();
/*  9634 */       if (act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */         
/*  9636 */         target.closeAll();
/*  9637 */         done = true;
/*  9638 */         performer.getCommunicator().sendNormalServerMessage("You sealed the " + target.getName() + ".");
/*  9639 */         target.setIsSealedByPlayer(true);
/*  9640 */         if (source.getTemplateId() == 1255)
/*       */         {
/*  9642 */           target.setData((int)source.getQualityLevel(), (int)source.getDamage());
/*       */         }
/*  9644 */         Items.destroyItem(source.getWurmId());
/*       */       } 
/*       */     } 
/*  9647 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean breakSeal(Creature performer, Item target, Action act) {
/*  9652 */     boolean done = false;
/*  9653 */     Item liquid = null;
/*  9654 */     for (Item item : target.getItemsAsArray()) {
/*       */       
/*  9656 */       if (item.isLiquid()) {
/*       */         
/*  9658 */         liquid = item;
/*       */         
/*       */         break;
/*       */       } 
/*       */     } 
/*  9663 */     if (liquid != null && liquid.isFermenting()) {
/*       */       
/*  9665 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9666 */           .getName() + " is still fermenting, therefore it makes no sense to unseal at this time..");
/*  9667 */       return true;
/*       */     } 
/*       */     
/*  9670 */     if (act.currentSecond() == 1) {
/*       */       
/*  9672 */       act.setTimeLeft(50);
/*  9673 */       performer.getCommunicator().sendNormalServerMessage("You start to unseal the " + target.getName() + ".");
/*  9674 */       Server.getInstance().broadCastAction(performer
/*  9675 */           .getName() + " starts to unseal " + target.getNameWithGenus() + ".", performer, 5);
/*  9676 */       performer.sendActionControl(Actions.actionEntrys[740].getVerbString(), true, act.getTimeLeft());
/*       */     }
/*       */     else {
/*       */       
/*  9680 */       if (target.canBePeggedByPlayer()) {
/*       */         
/*  9682 */         if (act.currentSecond() == 2) {
/*  9683 */           performer.getCommunicator().sendNormalServerMessage("You put your hand on the peg on the top.");
/*  9684 */         } else if (act.currentSecond() == 3) {
/*  9685 */           performer.getCommunicator().sendNormalServerMessage("You carefully remove the peg from the top.");
/*  9686 */         } else if (act.currentSecond() == 4) {
/*  9687 */           performer.getCommunicator().sendNormalServerMessage("But you notice the peg is damaged so discard it.");
/*       */         }
/*       */       
/*       */       }
/*  9691 */       else if (act.currentSecond() == 2) {
/*  9692 */         performer.getCommunicator().sendNormalServerMessage("You remove the string.");
/*  9693 */       } else if (act.currentSecond() == 3) {
/*  9694 */         performer.getCommunicator().sendNormalServerMessage("You break the wax seal.");
/*  9695 */       } else if (act.currentSecond() == 4) {
/*  9696 */         performer.getCommunicator().sendNormalServerMessage("You try to get the wax seal off so it can be used again.");
/*       */       } 
/*  9698 */       int timeleft = act.getTimeLeft();
/*  9699 */       if (act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */         
/*  9701 */         target.closeAll();
/*  9702 */         if (target.getData1() != -1) {
/*       */ 
/*       */           
/*  9705 */           boolean decayed = true;
/*       */           
/*       */           try {
/*  9708 */             Item kit = ItemFactory.createItem(1255, target.getData1(), performer.getName());
/*  9709 */             decayed = kit.setDamage((target.getData2() + 3 + Server.rand.nextInt(5)), true);
/*  9710 */             if (!decayed)
/*       */             {
/*  9712 */               performer.getInventory().insertItem(kit);
/*  9713 */               performer.getCommunicator().sendNormalServerMessage("You managed to get the wax sealing kit off, but damage it a bit.");
/*       */             }
/*       */           
/*  9716 */           } catch (FailedException e) {
/*       */ 
/*       */             
/*  9719 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */           }
/*  9721 */           catch (NoSuchTemplateException e) {
/*       */ 
/*       */             
/*  9724 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */           } 
/*  9726 */           target.setData(-1, -1);
/*  9727 */           if (decayed) {
/*  9728 */             performer.getCommunicator().sendNormalServerMessage("You unsealed the " + target.getName() + " by destroying the wax sealing.");
/*       */           }
/*       */         } else {
/*  9731 */           performer.getCommunicator().sendNormalServerMessage("You unsealed the " + target.getName() + ".");
/*  9732 */         }  target.setIsSealedByPlayer(false);
/*  9733 */         done = true;
/*       */       } 
/*       */     } 
/*  9736 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean removeSecuritySeal(Creature performer, Item target, Action act) {
/*  9749 */     boolean done = false;
/*  9750 */     if (act.currentSecond() == 1) {
/*       */       
/*  9752 */       act.setTimeLeft(50);
/*  9753 */       performer.getCommunicator().sendNormalServerMessage("You start to remove the security seal on the " + target.getName() + ".");
/*  9754 */       Server.getInstance().broadCastAction(performer
/*  9755 */           .getName() + " starts to remove the security seal on the " + target.getNameWithGenus() + ".", performer, 5);
/*  9756 */       performer.sendActionControl(Actions.actionEntrys[740].getVerbString(), true, act.getTimeLeft());
/*       */     }
/*       */     else {
/*       */       
/*  9760 */       int timeleft = act.getTimeLeft();
/*  9761 */       if (act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */         
/*  9763 */         target.closeAll();
/*  9764 */         performer.getCommunicator().sendNormalServerMessage("You have removed the security seal from the " + target.getName() + ".");
/*  9765 */         target.setIsSealedByPlayer(false);
/*  9766 */         if (target.isCrate()) {
/*       */ 
/*       */           
/*  9769 */           for (Item item : target.getItems()) {
/*       */             
/*  9771 */             item.setLastOwnerId(target.getLastOwnerId());
/*  9772 */             item.setLastMaintained(WurmCalendar.currentTime);
/*       */           } 
/*       */           
/*  9775 */           target.setData(-10L);
/*       */         } 
/*  9777 */         done = true;
/*       */       } 
/*       */     } 
/*  9780 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean wrap(Creature performer, @Nullable Item source, Item target, Action act) {
/*  9785 */     boolean done = true;
/*  9786 */     if (target.isWrapped()) {
/*       */       
/*  9788 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9789 */           .getName() + " is already wrapped.");
/*  9790 */       return true;
/*       */     } 
/*  9792 */     if (!target.usesFoodState()) {
/*       */       
/*  9794 */       performer.getCommunicator().sendNormalServerMessage("You just cannot figure out how to wrap the " + target
/*  9795 */           .getName() + ".");
/*  9796 */       return true;
/*       */     } 
/*  9798 */     boolean insta = (performer.getPower() >= 5);
/*  9799 */     done = false;
/*  9800 */     if (act.currentSecond() == 1) {
/*       */       
/*  9802 */       act.setTimeLeft(50);
/*  9803 */       if (source != null) {
/*       */         
/*  9805 */         performer.getCommunicator().sendNormalServerMessage("You start to wrap the " + target.getName() + ".");
/*  9806 */         Server.getInstance().broadCastAction(performer
/*  9807 */             .getName() + " starts to wrap " + target.getNameWithGenus() + ".", performer, 5);
/*       */       }
/*       */       else {
/*       */         
/*  9811 */         performer.getCommunicator().sendNormalServerMessage("You start to wrap the " + target.getName() + " using some grass, leaves and moss.");
/*       */         
/*  9813 */         Server.getInstance().broadCastAction(performer.getName() + " starts to wrap " + target.getNameWithGenus() + " using some grass, leaves and moss.", performer, 5);
/*       */       } 
/*       */       
/*  9816 */       performer.sendActionControl(Actions.actionEntrys[739].getVerbString(), true, act.getTimeLeft());
/*       */     }
/*       */     else {
/*       */       
/*  9820 */       int timeleft = act.getTimeLeft();
/*  9821 */       if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */         
/*  9823 */         done = true;
/*  9824 */         performer.getCommunicator().sendNormalServerMessage("You wrapped the " + target.getName() + ".");
/*  9825 */         target.setIsWrapped(true);
/*  9826 */         if (source != null)
/*  9827 */           Items.destroyItem(source.getWurmId()); 
/*       */       } 
/*       */     } 
/*  9830 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean unwrap(Creature performer, Item target, Action act) {
/*  9835 */     boolean done = true;
/*  9836 */     if (!target.isWrapped()) {
/*       */       
/*  9838 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/*  9839 */           .getName() + " is not wrapped.");
/*  9840 */       return true;
/*       */     } 
/*  9842 */     boolean insta = (performer.getPower() >= 5);
/*  9843 */     done = false;
/*  9844 */     if (act.currentSecond() == 1) {
/*       */       
/*  9846 */       act.setTimeLeft(50);
/*  9847 */       performer.getCommunicator().sendNormalServerMessage("You start to unwrap the " + target.getName() + ".");
/*  9848 */       Server.getInstance().broadCastAction(performer
/*  9849 */           .getName() + " starts to unwrap " + target.getNameWithGenus() + ".", performer, 5);
/*  9850 */       performer.sendActionControl(Actions.actionEntrys[740].getVerbString(), true, act.getTimeLeft());
/*       */     }
/*       */     else {
/*       */       
/*  9854 */       int timeleft = act.getTimeLeft();
/*  9855 */       if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */         
/*  9857 */         done = true;
/*  9858 */         target.setIsWrapped(false);
/*  9859 */         performer.getCommunicator().sendNormalServerMessage("You unwrapped the " + target.getName() + " and throw away the used wrapping.");
/*       */       } 
/*       */     } 
/*       */     
/*  9863 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static boolean conquerTarget(Creature performer, Item target, Communicator comm, float counter, Action act) {
/*  9880 */     if (target.isWarTarget()) {
/*       */       
/*  9882 */       if (target.getKingdom() == performer.getKingdomId() && target.getData1() == 100) {
/*       */         
/*  9884 */         comm.sendNormalServerMessage("The " + target
/*  9885 */             .getName() + " is already conquered.");
/*  9886 */         return true;
/*       */       } 
/*       */ 
/*       */       
/*  9890 */       Kingdom pk = Kingdoms.getKingdom(performer.getKingdomId());
/*  9891 */       if (pk != null && pk.isAllied(target.getKingdom())) {
/*       */         
/*  9893 */         comm.sendNormalServerMessage("The " + target
/*  9894 */             .getName() + " is already conquered by your alliance.");
/*  9895 */         return true;
/*       */       } 
/*       */ 
/*       */ 
/*       */       
/*  9900 */       if (performer.isOnSurface() != target.isOnSurface()) {
/*       */         
/*  9902 */         if (performer.isOnSurface()) {
/*  9903 */           comm.sendNormalServerMessage("You need to be in cave to conquer the " + target
/*  9904 */               .getName() + " (because it is).");
/*       */         } else {
/*  9906 */           comm.sendNormalServerMessage("You need to be on surface to conquer the " + target
/*  9907 */               .getName() + ".");
/*  9908 */         }  return true;
/*       */       } 
/*       */       
/*  9911 */       int time = 6000;
/*  9912 */       if (performer.getPower() > 0) {
/*  9913 */         time = 200;
/*       */       }
/*  9915 */       if (target.getKingdom() == 0) {
/*       */ 
/*       */         
/*  9918 */         if (target.getData2() == performer.getKingdomId()) {
/*  9919 */           time = (100 - target.getData1()) * 30;
/*       */         } else {
/*  9921 */           time = target.getData1() * 30;
/*       */         } 
/*  9923 */       } else if (target.getData2() == performer.getKingdomId()) {
/*       */         
/*  9925 */         time = (100 - target.getData1()) * 30;
/*       */       }
/*       */       else {
/*       */         
/*  9929 */         time = target.getData1() * 30;
/*       */       } 
/*       */       
/*  9932 */       if (!Servers.localServer.HOMESERVER)
/*       */       {
/*  9934 */         if (target.isWarTarget())
/*  9935 */           time = (int)(time * (1.0F + Zones.getPercentLandForKingdom(performer.getKingdomId()) / 300.0F)); 
/*       */       }
/*  9937 */       act.setTimeLeft(time);
/*       */       
/*  9939 */       if (counter == 1.0F) {
/*       */ 
/*       */         
/*  9942 */         for (Player player : Players.getInstance().getPlayers()) {
/*       */           
/*  9944 */           if (player.getWurmId() != performer.getWurmId()) {
/*       */             
/*       */             try {
/*       */               
/*  9948 */               Action acta = player.getCurrentAction();
/*  9949 */               if (acta.getNumber() == 504 && acta.getTarget() == target.getWurmId())
/*       */               {
/*  9951 */                 comm.sendNormalServerMessage("The " + target
/*  9952 */                     .getName() + " can not be used by more than one person.");
/*  9953 */                 return true;
/*       */               }
/*       */             
/*  9956 */             } catch (NoSuchActionException noSuchActionException) {}
/*       */           }
/*       */         } 
/*       */         
/*  9960 */         Long last = ItemBehaviour.conquers.get(Long.valueOf(target.getWurmId()));
/*  9961 */         if (last != null)
/*       */         {
/*  9963 */           if (System.currentTimeMillis() - last.longValue() < 3600000L)
/*       */           {
/*  9965 */             comm.sendAlertServerMessage(String.format("You will have to wait %s if you want to receive battle rank for conquering the %s.", new Object[] {
/*  9966 */                     Server.getTimeFor(last.longValue() + 3600000L - System.currentTimeMillis()), target.getName()
/*       */                   })); } 
/*       */         }
/*  9969 */         if (target.getKingdom() == performer.getKingdomId()) {
/*       */           
/*  9971 */           comm.sendNormalServerMessage("You start to secure the " + target.getName() + ".");
/*  9972 */           performer.sendActionControl(Actions.actionEntrys[504].getVerbString(), true, time);
/*  9973 */           Server.getInstance().broadCastAction(performer
/*  9974 */               .getName() + " starts securing the " + target.getName() + ".", performer, 10);
/*       */         }
/*       */         else {
/*       */           
/*  9978 */           comm.sendNormalServerMessage("You start to conquer the " + target.getName() + ".");
/*  9979 */           performer.sendActionControl(Actions.actionEntrys[504].getVerbString(), true, time);
/*  9980 */           Server.getInstance().broadCastAction(performer
/*  9981 */               .getName() + " starts conquering the " + target.getName() + ".", performer, 10);
/*       */         } 
/*       */       } 
/*       */ 
/*       */       
/*  9986 */       if (act.justTickedSecond() && (int)counter % 60 == 0) {
/*       */         
/*  9988 */         String name = target.getName();
/*  9989 */         if (target.getKingdom() != performer.getKingdomId()) {
/*       */           
/*  9991 */           Players.getInstance().broadCastConquerInfo(performer, name + " is being conquered.");
/*  9992 */           Server.getInstance().broadCastEpicEvent(name + " is being conquered.");
/*       */         } 
/*       */         
/*  9995 */         if (target.getKingdom() == 0) {
/*       */           
/*  9997 */           if (target.getData2() != performer.getKingdomId()) {
/*       */             
/*  9999 */             target.setData1(Math.max(0, target.getData1() - 20));
/*       */           } else {
/*       */             
/* 10002 */             target.setData1(Math.min(100, target.getData1() + 20));
/*       */           } 
/* 10004 */         } else if (target.getData2() == performer.getKingdomId()) {
/*       */           
/* 10006 */           target.setData1(Math.min(100, target.getData1() + 20));
/*       */         } else {
/*       */           
/* 10009 */           target.setData1(Math.max(0, target.getData1() - 20));
/*       */         } 
/* 10011 */         VolaTile t = Zones.getOrCreateTile(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 10012 */         t.updateTargetStatus(target.getWurmId(), (byte)target.getData2(), target.getData1());
/*       */       } 
/* 10014 */       if ((target.getData1() == 100 || target.getData1() == 0) && counter > 60.0F)
/*       */       {
/*       */         
/* 10017 */         boolean dealPoints = (target.getKingdom() != performer.getKingdomId() && target.getData1() == 100);
/* 10018 */         Long last = ItemBehaviour.conquers.get(Long.valueOf(target.getWurmId()));
/* 10019 */         if (last != null)
/*       */         {
/*       */           
/* 10022 */           if (System.currentTimeMillis() - last.longValue() < 3600000L)
/* 10023 */             dealPoints = false; 
/*       */         }
/* 10025 */         if (dealPoints) {
/*       */           
/* 10027 */           ItemBehaviour.conquers.put(Long.valueOf(target.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/* 10028 */           for (Player p : Players.getInstance().getPlayers()) {
/*       */             
/* 10030 */             if (p.isFriendlyKingdom(performer.getKingdomId()) && p
/* 10031 */               .isWithinDistanceTo(target.getPosX(), target.getPosY(), p.getPositionZ(), 300.0F)) {
/*       */               
/* 10033 */               p.modifyKarma(10);
/*       */               
/*       */               try {
/* 10036 */                 p.setRank(p.getRank());
/*       */               }
/* 10038 */               catch (IOException iOException) {}
/*       */             } 
/*       */           } 
/*       */         } 
/* 10042 */         if (target.getKingdom() != performer.getKingdomId()) {
/*       */           
/* 10044 */           if (target.getData1() == 100)
/*       */           {
/* 10046 */             Players.getInstance().broadCastConquerInfo(performer, performer
/* 10047 */                 .getName() + " conquers " + target.getName() + ".");
/* 10048 */             Server.getInstance().broadCastEpicEvent(performer
/* 10049 */                 .getName() + " conquers " + target.getName() + ".");
/* 10050 */             performer.achievement(368);
/* 10051 */             target.setData2(performer.getKingdomId());
/* 10052 */             target.setAuxData(performer.getKingdomId());
/* 10053 */             Kingdoms.addWarTargetKingdom(target);
/* 10054 */             for (int x = 0; x < 2 + Server.rand.nextInt(4); x++) {
/*       */ 
/*       */               
/*       */               try {
/* 10058 */                 GuardTower.spawnSoldier(target, performer.getKingdomId());
/* 10059 */               } catch (Exception e) {
/*       */                 
/* 10061 */                 logger.log(Level.WARNING, e.getMessage(), e);
/*       */               } 
/*       */             } 
/* 10064 */             GuardTower.spawnCommander(target, performer.getKingdomId());
/*       */           }
/* 10066 */           else if (target.getData1() == 0)
/*       */           {
/* 10068 */             target.setData2(performer.getKingdomId());
/* 10069 */             Players.getInstance().broadCastConquerInfo(performer, performer
/* 10070 */                 .getName() + " neutralizes " + target.getName() + ".");
/* 10071 */             Server.getInstance().broadCastEpicEvent(performer
/* 10072 */                 .getName() + " neutralizes " + target.getName() + ".");
/* 10073 */             comm.sendNormalServerMessage("You neutralize the " + target
/* 10074 */                 .getName() + ".");
/*       */             
/* 10076 */             target.setAuxData((byte)0);
/*       */           
/*       */           }
/*       */         
/*       */         }
/* 10081 */         else if (target.getData1() == 100) {
/*       */           
/* 10083 */           comm.sendNormalServerMessage("You secure the " + target
/* 10084 */               .getName() + ".");
/* 10085 */           target.setData2(performer.getKingdomId());
/*       */         }
/* 10087 */         else if (target.getData1() == 0) {
/*       */ 
/*       */           
/* 10090 */           target.setData2(performer.getKingdomId());
/* 10091 */           Players.getInstance().broadCastConquerInfo(performer, performer
/* 10092 */               .getName() + " neutralizes " + target.getName() + ".");
/* 10093 */           Server.getInstance().broadCastEpicEvent(performer
/* 10094 */               .getName() + " neutralizes " + target.getName() + ".");
/* 10095 */           comm.sendNormalServerMessage("You neutralize the " + target
/* 10096 */               .getName() + ".");
/*       */           
/* 10098 */           target.setAuxData((byte)0);
/*       */         } 
/*       */         
/* 10101 */         VolaTile t = Zones.getOrCreateTile(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 10102 */         t.updateTargetStatus(target.getWurmId(), (byte)target.getData2(), target.getData1());
/* 10103 */         return true;
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/* 10108 */       if (!performer.isPaying()) {
/*       */         
/* 10110 */         comm.sendNormalServerMessage("Due to exploitability, only premium players may conquer pillars.");
/* 10111 */         return true;
/*       */       } 
/* 10113 */       if (performer.getFightingSkill().getRealKnowledge() < 20.0D) {
/*       */         
/* 10115 */         comm.sendNormalServerMessage("You need fighting skill 20 in order to conquer pillars.");
/* 10116 */         return true;
/*       */       } 
/* 10118 */       if (Servers.localServer.getNextHota() != Long.MAX_VALUE) {
/*       */         
/* 10120 */         comm.sendNormalServerMessage("The Hunt is not on.");
/* 10121 */         return true;
/*       */       } 
/* 10123 */       if (performer.getCitizenVillage() == null) {
/*       */         
/* 10125 */         comm.sendNormalServerMessage("You have no alliance and can't assume control of the " + target.getName() + ".");
/* 10126 */         return true;
/*       */       } 
/* 10128 */       if (target.getData1() != 0)
/*       */       {
/* 10130 */         if (performer.getCitizenVillage() != null)
/*       */         {
/* 10132 */           if (target.getData1() == performer.getCitizenVillage().getAllianceNumber() || target
/* 10133 */             .getData1() == performer.getCitizenVillage().getId()) {
/*       */             
/* 10135 */             comm.sendNormalServerMessage("Your alliance is already in control of the " + target
/* 10136 */                 .getName() + ".");
/* 10137 */             Hota.addPillarTouched(performer, target);
/* 10138 */             return true;
/*       */           } 
/*       */         }
/*       */       }
/* 10142 */       if (counter == 1.0F) {
/*       */         
/* 10144 */         comm.sendNormalServerMessage("You start to conquer the " + target
/* 10145 */             .getName() + ".");
/*       */         
/* 10147 */         performer.sendActionControl(Actions.actionEntrys[504].getVerbString(), true, 1000);
/* 10148 */         Server.getInstance().broadCastAction(performer
/* 10149 */             .getName() + " starts conquering the " + target.getName() + ".", performer, 10);
/* 10150 */         Hota.addPillarTouched(performer, target);
/*       */       } 
/* 10152 */       if (counter > 90.0F)
/*       */       {
/* 10154 */         if (target.getData1() != 0) {
/*       */           
/* 10156 */           target.deleteAllEffects();
/* 10157 */           target.setData1(0);
/* 10158 */           Server.getInstance().broadCastSafe(performer
/* 10159 */               .getName() + " neutralizes the " + target.getName() + ".");
/* 10160 */           Server.getInstance().broadCastAction(performer
/* 10161 */               .getName() + " neutralizes the " + target.getName() + ".", performer, 10);
/* 10162 */           if (performer.getCitizenVillage() != null)
/* 10163 */             Hota.addPillarConquered(performer, target); 
/*       */         } 
/*       */       }
/* 10166 */       if (counter > 100.0F) {
/*       */         
/* 10168 */         if (performer.getCitizenVillage() != null) {
/*       */           
/* 10170 */           Server.getInstance().broadCastSafe(performer.getName() + " conquers the " + target.getName() + ".");
/* 10171 */           target.addEffect(EffectFactory.getInstance().createFire(target.getWurmId(), target.getPosX(), target
/* 10172 */                 .getPosY(), target.getPosZ(), true));
/* 10173 */           if (performer.getCitizenVillage().getAllianceNumber() == 0) {
/* 10174 */             target.setData1(performer.getCitizenVillage().getId() + 2000000);
/*       */           } else {
/* 10176 */             target.setData1(performer.getCitizenVillage().getAllianceNumber());
/* 10177 */           }  Hota.addPillarConquered(performer, target);
/*       */         } else {
/*       */           
/* 10180 */           comm.sendNormalServerMessage("You can only conquer the pillar in the name of a settlement or alliance.");
/*       */         } 
/* 10182 */         return true;
/*       */       } 
/* 10184 */       return false;
/*       */     } 
/* 10186 */     return false;
/*       */   }
/*       */   static final boolean colorItem(Creature performer, Item colour, Item target, Action act, boolean primary) {
/*       */     int colourNeeded;
/*       */     String type;
/* 10191 */     boolean done = true;
/* 10192 */     if (target.isIndestructible()) {
/*       */       
/* 10194 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10195 */           .getName() + " is indestructible and the colour can not be replaced.");
/* 10196 */       return true;
/*       */     } 
/* 10198 */     String sItem = "";
/* 10199 */     if (primary) {
/*       */       
/* 10201 */       if (target.getTemplateId() == 1396)
/*       */       {
/* 10203 */         sItem = "barrel";
/*       */       }
/* 10205 */       if (target.color != -1) {
/*       */         
/* 10207 */         if (sItem.length() == 0) {
/*       */           
/* 10209 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10210 */               .getName() + " already has colour on it.");
/*       */         }
/*       */         else {
/*       */           
/* 10214 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10215 */               .getName() + " already has colour on it's " + sItem + ". Remove it first with a metal brush.");
/*       */         } 
/* 10217 */         return true;
/*       */       } 
/*       */     } 
/* 10220 */     if (!primary) {
/*       */       
/* 10222 */       if (target.getTemplateId() == 1396) {
/*       */         
/* 10224 */         sItem = "lamp";
/*       */       }
/*       */       else {
/*       */         
/* 10228 */         sItem = target.getSecondryItemName();
/*       */       } 
/* 10230 */       if (target.color2 != -1) {
/*       */         
/* 10232 */         if (sItem.length() == 0) {
/*       */           
/* 10234 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10235 */               .getName() + " already has colour on it.");
/*       */         }
/*       */         else {
/*       */           
/* 10239 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10240 */               .getName() + " already has colour on it's " + sItem + ". Remove it first with some lye.");
/*       */         } 
/* 10242 */         return true;
/*       */       } 
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 10249 */     int dyeOverride = target.getTemplate().getDyePrimaryAmountGrams();
/* 10250 */     if (!primary && !target.isDragonArmour())
/*       */     {
/* 10252 */       if (target.getTemplate().getDyeSecondaryAmountGrams() > 0) {
/* 10253 */         dyeOverride = target.getTemplate().getDyeSecondaryAmountGrams();
/* 10254 */       } else if (dyeOverride > 0) {
/* 10255 */         dyeOverride = (int)(dyeOverride * 0.3F);
/*       */       }  } 
/* 10257 */     if (dyeOverride > 0) {
/* 10258 */       colourNeeded = dyeOverride;
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 10263 */       colourNeeded = (int)Math.max(1.0D, target.getSurfaceArea() * (primary ? 1.0D : 0.3D) / 25.0D);
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 10273 */     boolean insta = (performer.getPower() >= 5);
/*       */     
/* 10275 */     if (primary || target.isDragonArmour()) {
/* 10276 */       type = "paint";
/*       */     } else {
/* 10278 */       type = "dye";
/* 10279 */     }  if (!insta && colourNeeded > colour.getWeightGrams()) {
/* 10280 */       performer.getCommunicator().sendNormalServerMessage("You need more " + type + " to colour that item - at least " + colourNeeded + "g of " + type + ".");
/*       */     }
/*       */     else {
/*       */       
/* 10284 */       done = false;
/* 10285 */       if (act.currentSecond() == 1) {
/*       */         String verb;
/* 10287 */         act.setTimeLeft(Math.max(50, colourNeeded / 50));
/* 10288 */         if ((primary || target.isDragonArmour()) && target.getTemplateId() != 1396) {
/* 10289 */           performer.getCommunicator().sendNormalServerMessage("You start to " + type + " the " + target.getName() + " (using " + colourNeeded + "g of " + type + ").");
/*       */         } else {
/*       */           
/* 10292 */           performer.getCommunicator().sendNormalServerMessage("You start to " + type + " the " + target.getName() + "'s " + sItem + " (using " + colourNeeded + "g of " + type + ").");
/*       */         } 
/* 10294 */         Server.getInstance().broadCastAction(performer
/* 10295 */             .getName() + " starts to " + type + " " + target.getNameWithGenus() + ".", performer, 5);
/*       */         
/* 10297 */         if (primary) {
/* 10298 */           verb = Actions.actionEntrys[231].getVerbString();
/*       */         } else {
/* 10300 */           verb = Actions.actionEntrys[923].getVerbString();
/* 10301 */         }  performer.sendActionControl(verb, true, act.getTimeLeft());
/*       */       }
/*       */       else {
/*       */         
/* 10305 */         int timeleft = act.getTimeLeft();
/* 10306 */         if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */           
/* 10308 */           done = true;
/* 10309 */           if (primary) {
/* 10310 */             target.setColor(colour.getColor());
/*       */           } else {
/* 10312 */             target.setColor2(colour.getColor());
/*       */           } 
/* 10314 */           colour.setWeight(colour.getWeightGrams() - colourNeeded, true);
/* 10315 */           if (primary || target.isDragonArmour()) {
/*       */             
/* 10317 */             if (target.getTemplateId() == 1396)
/*       */             {
/* 10319 */               performer.getCommunicator().sendNormalServerMessage("You paint the " + target.getName() + "'s " + sItem + ".");
/*       */             
/*       */             }
/*       */             else
/*       */             {
/* 10324 */               performer.getCommunicator().sendNormalServerMessage("You paint the " + target.getName() + ".");
/*       */             
/*       */             }
/*       */           
/*       */           }
/* 10329 */           else if (target.getTemplateId() == 1396) {
/*       */             
/* 10331 */             performer.getCommunicator().sendNormalServerMessage("You paint the " + target.getName() + "'s " + sItem + ".");
/*       */           
/*       */           }
/*       */           else {
/*       */             
/* 10336 */             performer.getCommunicator().sendNormalServerMessage("You dye the " + target.getName() + "'s " + sItem + ".");
/*       */           } 
/*       */ 
/*       */ 
/*       */           
/* 10341 */           if (target.isBoat() && !primary)
/* 10342 */             performer.achievement(494); 
/* 10343 */           if (target.isArmour())
/* 10344 */             performer.achievement(493); 
/*       */         } 
/*       */       } 
/*       */     } 
/* 10348 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean improveColor(Creature performer, Item colorComponent, Item dye, Action act) {
/* 10353 */     boolean done = true;
/* 10354 */     boolean insta = (performer.getPower() >= 5);
/* 10355 */     performer.sendToLoggers(colorComponent.getName() + " weight: " + colorComponent.getWeightGrams() + ", " + dye.getName() + " weight: " + dye
/* 10356 */         .getWeightGrams());
/* 10357 */     if (colorComponent.getTemplate().getWeightGrams() > colorComponent.getWeightGrams()) {
/* 10358 */       performer.getCommunicator().sendNormalServerMessage("You need more " + colorComponent
/* 10359 */           .getName() + " to improve the dye.");
/*       */     } else {
/*       */       
/* 10362 */       done = false;
/* 10363 */       if (act.currentSecond() == 1) {
/*       */         
/* 10365 */         act.setTimeLeft(50);
/* 10366 */         performer.getCommunicator().sendNormalServerMessage("You start to improve the " + dye.getName() + ".");
/* 10367 */         Server.getInstance().broadCastAction(performer
/* 10368 */             .getName() + " starts to improve " + dye.getNameWithGenus() + ".", performer, 5);
/* 10369 */         performer.sendActionControl(Actions.actionEntrys[283].getVerbString(), true, act.getTimeLeft());
/*       */       }
/*       */       else {
/*       */         
/* 10373 */         int timeleft = act.getTimeLeft();
/* 10374 */         if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */           
/* 10376 */           done = true;
/* 10377 */           dye.setColor(WurmColor.getCompositeColor(dye.color, dye.getWeightGrams(), colorComponent.getTemplateId(), colorComponent
/* 10378 */                 .getQualityLevel()));
/*       */           
/* 10380 */           colorComponent.setWeight(colorComponent.getWeightGrams() - colorComponent.getTemplate().getWeightGrams(), true);
/*       */           
/* 10382 */           performer.getCommunicator().sendNormalServerMessage("You try to improve the " + dye.getName() + ".");
/*       */         } 
/*       */       } 
/*       */     } 
/* 10386 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean removeColor(Creature performer, Item brush, Item target, Action act, boolean primary) {
/* 10391 */     boolean done = true;
/* 10392 */     if (target.isIndestructible()) {
/*       */       
/* 10394 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10395 */           .getName() + " is indestructible and the colour can not be removed.");
/* 10396 */       return true;
/*       */     } 
/* 10398 */     if (target.isDragonArmour() && primary) {
/*       */       
/* 10400 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is too tough for the " + brush
/* 10401 */           .getName() + " and the colour refuses to disappear.");
/* 10402 */       return true;
/*       */     } 
/*       */     
/* 10405 */     boolean insta = (performer.getPower() >= 5);
/* 10406 */     int colourNeeded = 0;
/* 10407 */     String sItem = "";
/* 10408 */     if (target.getTemplateId() == 1396) {
/*       */       
/* 10410 */       if ((primary && brush.getTemplateId() != 441) || (!primary && brush
/* 10411 */         .getTemplateId() != 73)) {
/*       */         
/* 10413 */         performer.getCommunicator().sendNormalServerMessage("You cannot use the " + brush.getName() + " to do this.");
/* 10414 */         return true;
/*       */       } 
/* 10416 */       if (primary) {
/* 10417 */         sItem = "barrel";
/*       */       } else {
/* 10419 */         sItem = "lamp";
/*       */       } 
/*       */     } else {
/*       */       
/* 10423 */       if (primary && brush.getTemplateId() != 441) {
/*       */         
/* 10425 */         performer.getCommunicator().sendNormalServerMessage("You cannot use the " + brush.getName() + " to do this.");
/* 10426 */         return true;
/*       */       } 
/* 10428 */       if (!primary)
/* 10429 */         sItem = target.getSecondryItemName(); 
/*       */     } 
/* 10431 */     if (!insta && target.color == -1 && primary) {
/*       */       
/* 10433 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " has no colour.");
/* 10434 */       return true;
/*       */     } 
/* 10436 */     if (!primary)
/*       */     {
/*       */       
/* 10439 */       if (brush.getTemplateId() == 441) {
/*       */         
/* 10441 */         if (!insta && target.color2 == -1) {
/*       */           
/* 10443 */           performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " has no colour.");
/* 10444 */           return true;
/*       */         } 
/* 10446 */         if (target.isColorable() && target.getTemplateId() != 1396)
/*       */         {
/* 10448 */           performer.getCommunicator().sendNormalServerMessage("You cannot use the " + brush.getName() + " to do this.");
/* 10449 */           return true;
/*       */         }
/*       */       
/*       */       } else {
/*       */         
/* 10454 */         if (brush.getTemplateId() != 73) {
/*       */           
/* 10456 */           performer.getCommunicator().sendNormalServerMessage("You cannot use the " + brush.getName() + " to do this.");
/* 10457 */           return true;
/*       */         } 
/*       */         
/* 10460 */         int dyeOverride = target.getTemplate().getDyePrimaryAmountGrams();
/* 10461 */         if (!primary)
/*       */         {
/* 10463 */           if (target.getTemplate().getDyeSecondaryAmountGrams() > 0) {
/* 10464 */             dyeOverride = target.getTemplate().getDyeSecondaryAmountGrams();
/* 10465 */           } else if (dyeOverride > 0) {
/* 10466 */             dyeOverride = (int)(dyeOverride * 0.3F);
/*       */           }  } 
/* 10468 */         if (dyeOverride > 0) {
/* 10469 */           colourNeeded = dyeOverride;
/*       */         } else {
/*       */           
/* 10472 */           colourNeeded = (int)Math.max(1.0D, target.getSurfaceArea() * (primary ? 1.0D : 0.3D) / 25.0D);
/*       */         } 
/* 10474 */         colourNeeded = Math.max(1, colourNeeded / 2);
/* 10475 */         if (!insta && colourNeeded > brush.getWeightGrams()) {
/*       */           
/* 10477 */           performer.getCommunicator().sendNormalServerMessage("You need more lye (" + colourNeeded + "g) to bleach that item.");
/* 10478 */           return true;
/*       */         } 
/*       */       } 
/*       */     }
/* 10482 */     done = false;
/* 10483 */     if (act.currentSecond() == 1) {
/*       */       String type, verb;
/*       */       
/* 10486 */       if (brush.getTemplateId() == 441) {
/* 10487 */         type = "brush";
/*       */       } else {
/* 10489 */         type = "bleach";
/* 10490 */       }  act.setTimeLeft(150);
/* 10491 */       performer.getCommunicator().sendNormalServerMessage("You start to " + type + " the " + target.getName() + ".");
/* 10492 */       Server.getInstance().broadCastAction(performer
/* 10493 */           .getName() + " starts to " + type + " " + target.getNameWithGenus() + ".", performer, 5);
/*       */       
/* 10495 */       if (brush.getTemplateId() == 441 && target.getTemplateId() != 1396) {
/* 10496 */         verb = Actions.actionEntrys[232].getVerbString();
/*       */       } else {
/* 10498 */         verb = Actions.actionEntrys[924].getVerbString();
/* 10499 */       }  performer.sendActionControl(verb, true, act.getTimeLeft());
/*       */     }
/*       */     else {
/*       */       
/* 10503 */       int timeleft = act.getTimeLeft();
/* 10504 */       if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */         
/* 10506 */         done = true;
/* 10507 */         if (primary) {
/*       */           
/* 10509 */           target.setColor(-1);
/* 10510 */           if (brush.getTemplateId() == 441) {
/* 10511 */             brush.setDamage((float)(brush.getDamage() + 0.5D * brush.getDamageModifier()));
/*       */           } else {
/* 10513 */             brush.setWeight(brush.getWeightGrams() - colourNeeded, true);
/* 10514 */           }  if (sItem.length() == 0) {
/* 10515 */             performer.getCommunicator().sendNormalServerMessage("You remove the colour from the " + target
/* 10516 */                 .getName() + ".");
/*       */           } else {
/* 10518 */             performer.getCommunicator().sendNormalServerMessage("You remove the colour from the " + target
/* 10519 */                 .getName() + "'s " + sItem + ".");
/*       */           } 
/*       */         } else {
/*       */           
/* 10523 */           target.setColor2(-1);
/* 10524 */           if (brush.getTemplateId() == 441) {
/* 10525 */             brush.setDamage((float)(brush.getDamage() + 0.5D * brush.getDamageModifier()));
/*       */           } else {
/* 10527 */             brush.setWeight(brush.getWeightGrams() - colourNeeded, true);
/* 10528 */           }  if (sItem.length() == 0) {
/* 10529 */             performer.getCommunicator().sendNormalServerMessage("You remove the colour from the " + target
/* 10530 */                 .getName() + ".");
/*       */           } else {
/* 10532 */             performer.getCommunicator().sendNormalServerMessage("You remove the colour from the " + target
/* 10533 */                 .getName() + "'s " + sItem + ".");
/*       */           } 
/*       */         } 
/*       */       } 
/* 10537 */     }  return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean attachBags(Creature performer, Item bags, Item saddle, Action act) {
/* 10542 */     boolean done = true;
/*       */     
/* 10544 */     if (bags.getTemplateId() == 1333 && saddle.getTemplateId() == 621) {
/*       */       
/* 10546 */       if (saddle.getAuxData() == 0) {
/*       */         
/* 10548 */         saddle.setAuxData((byte)1);
/* 10549 */         if (bags.getRarity() > saddle.getRarity())
/* 10550 */           saddle.setRarity(bags.getRarity()); 
/* 10551 */         Items.destroyItem(bags.getWurmId());
/* 10552 */         Item topParent = saddle.getTopParentOrNull();
/* 10553 */         if (topParent != null && topParent.isHollow()) {
/*       */           
/*       */           try {
/*       */             
/* 10557 */             Creature[] watchers = saddle.getWatchers();
/* 10558 */             if (watchers != null) {
/*       */               
/* 10560 */               long inventoryWindow = saddle.getTopParent();
/* 10561 */               if (topParent.isInventory()) {
/* 10562 */                 inventoryWindow = -1L;
/*       */               }
/* 10564 */               for (Creature watcher : watchers)
/*       */               {
/*       */                 
/* 10567 */                 watcher.getCommunicator().sendRemoveFromInventory(saddle, inventoryWindow);
/* 10568 */                 watcher.getCommunicator().sendAddToInventory(saddle, inventoryWindow, -1L, -1);
/*       */               }
/*       */             
/*       */             } 
/* 10572 */           } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */         }
/*       */ 
/*       */ 
/*       */         
/* 10577 */         performer.getCommunicator().sendNormalServerMessage("You add bags to the saddle.");
/* 10578 */         Server.getInstance().broadCastAction(performer.getName() + " adds bags to a saddle.", performer, 5);
/*       */       
/*       */       }
/*       */       else {
/*       */ 
/*       */         
/* 10584 */         performer.getCommunicator().sendNormalServerMessage("The saddle already has bags attached.");
/*       */       } 
/*       */     } else {
/*       */       
/* 10588 */       performer.getCommunicator().sendNormalServerMessage("You can not attach the " + bags.getName() + " to the " + saddle.getName() + ".");
/*       */     } 
/* 10590 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean removeBags(Creature performer, Item saddle, Action act) {
/* 10595 */     boolean done = true;
/*       */     
/* 10597 */     if (saddle.getTemplateId() == 621) {
/*       */       
/* 10599 */       if (saddle.getAuxData() == 1) {
/*       */         
/* 10601 */         if (saddle.getItemCount() > 0) {
/* 10602 */           performer.getCommunicator().sendNormalServerMessage("You must empty the bags before removing them.");
/*       */         } else {
/*       */           
/*       */           try
/*       */           {
/* 10607 */             saddle.setAuxData((byte)0);
/* 10608 */             Item bags = ItemFactory.createItem(1333, 20.0F, (byte)16, (byte)0, null);
/* 10609 */             Item inventory = performer.getInventory();
/* 10610 */             inventory.insertItem(bags);
/* 10611 */             Item topParent = saddle.getTopParentOrNull();
/* 10612 */             if (topParent != null && topParent.isHollow()) {
/*       */               
/*       */               try {
/*       */                 
/* 10616 */                 Creature[] watchers = saddle.getWatchers();
/* 10617 */                 if (watchers != null) {
/*       */                   
/* 10619 */                   long inventoryWindow = saddle.getTopParent();
/* 10620 */                   if (topParent.isInventory()) {
/* 10621 */                     inventoryWindow = -1L;
/*       */                   }
/* 10623 */                   for (Creature watcher : watchers)
/*       */                   {
/*       */                     
/* 10626 */                     watcher.getCommunicator().sendRemoveFromInventory(saddle, inventoryWindow);
/* 10627 */                     watcher.getCommunicator().sendAddToInventory(saddle, inventoryWindow, -1L, -1);
/*       */                   }
/*       */                 
/*       */                 } 
/* 10631 */               } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */             }
/*       */ 
/*       */ 
/*       */             
/* 10636 */             performer.getCommunicator().sendNormalServerMessage("You remove the bags from the saddle.");
/* 10637 */             Server.getInstance().broadCastAction(performer
/* 10638 */                 .getName() + " removes the bags from a saddle.", performer, 5);
/*       */           
/*       */           }
/* 10641 */           catch (FailedException fe)
/*       */           {
/* 10643 */             logger.log(Level.WARNING, performer.getName() + " " + fe.getMessage(), (Throwable)fe);
/*       */           }
/* 10645 */           catch (NoSuchTemplateException nst)
/*       */           {
/* 10647 */             logger.log(Level.WARNING, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/*       */           }
/*       */         
/*       */         }
/*       */       
/*       */       } else {
/*       */         
/* 10654 */         performer.getCommunicator().sendNormalServerMessage("The " + saddle.getName() + " does not have bags attached.");
/*       */       } 
/*       */     } else {
/*       */       
/* 10658 */       performer.getCommunicator().sendNormalServerMessage("You can not remove bags from the " + saddle.getName() + ".");
/*       */     } 
/* 10660 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean string(Creature performer, Item bowstring, Item bow, Action act) {
/* 10665 */     boolean done = true;
/* 10666 */     if (bowstring.getTemplateId() == 457) {
/*       */       
/* 10668 */       boolean insta = (performer.getPower() >= 5);
/* 10669 */       if (!bow.isBowUnstringed()) {
/* 10670 */         performer.getCommunicator().sendNormalServerMessage("The " + bow.getName() + " cannot be strung.");
/*       */       } else {
/*       */         
/* 10673 */         done = false;
/*       */         
/*       */         try {
/* 10676 */           Item parent = bow.getParent();
/* 10677 */           if (parent.isBodyPartAttached())
/*       */           {
/* 10679 */             if (parent.getPlace() == 13) {
/*       */               
/* 10681 */               Item rightHandWeapon = performer.getRighthandWeapon();
/* 10682 */               if (rightHandWeapon != null)
/*       */               {
/* 10684 */                 performer.getCommunicator().sendNormalServerMessage("You can not string the " + bow
/* 10685 */                     .getName() + " while wielding " + rightHandWeapon
/* 10686 */                     .getNameWithGenus() + ".");
/* 10687 */                 return true;
/*       */               }
/*       */             
/* 10690 */             } else if (parent.getPlace() == 14) {
/*       */               
/* 10692 */               Item leftHandWeapon = performer.getLefthandWeapon();
/* 10693 */               if (leftHandWeapon != null)
/*       */               {
/* 10695 */                 performer.getCommunicator().sendNormalServerMessage("You can not string the " + bow
/* 10696 */                     .getName() + " while wielding " + leftHandWeapon
/* 10697 */                     .getNameWithGenus() + ".");
/* 10698 */                 return true;
/*       */               }
/*       */             
/*       */             } 
/*       */           }
/* 10703 */         } catch (NoSuchItemException noSuchItemException) {}
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 10708 */         if (act.currentSecond() == 1) {
/*       */           
/* 10710 */           act.setTimeLeft(150);
/* 10711 */           performer.getCommunicator().sendNormalServerMessage("You start to string the " + bow.getName() + ".");
/* 10712 */           Server.getInstance().broadCastAction(performer
/* 10713 */               .getName() + " starts to string " + bow.getNameWithGenus() + ".", performer, 5);
/* 10714 */           performer.sendActionControl(Actions.actionEntrys[132].getVerbString(), true, act.getTimeLeft());
/*       */         
/*       */         }
/*       */         else {
/*       */           
/* 10719 */           int timeleft = act.getTimeLeft();
/* 10720 */           if (insta || act.getCounterAsFloat() * 10.0F >= timeleft)
/*       */           {
/* 10722 */             if (bowstring.getRarity() != 0)
/*       */             {
/* 10724 */               performer.playPersonalSound("sound.fx.drumroll");
/*       */             }
/* 10726 */             int realTemplate = 447;
/* 10727 */             if (bow.getTemplateId() == 461) {
/* 10728 */               realTemplate = 449;
/* 10729 */             } else if (bow.getTemplateId() == 460) {
/* 10730 */               realTemplate = 448;
/*       */             } 
/* 10732 */             done = true;
/* 10733 */             bow.setTemplateId(realTemplate);
/* 10734 */             bow.setAuxData((byte)(int)bowstring.getCurrentQualityLevel());
/* 10735 */             if (bowstring.getRarity() != 0 && Server.rand.nextInt(100) == 0 && 
/* 10736 */               bowstring.getRarity() > bow.getRarity())
/* 10737 */               bow.setRarity(bowstring.getRarity()); 
/* 10738 */             Items.destroyItem(bowstring.getWurmId());
/* 10739 */             performer.getCommunicator().sendNormalServerMessage("You string the " + bow.getName() + ".");
/* 10740 */             Server.getInstance().broadCastAction(performer.getName() + " strings " + bow.getNameWithGenus() + ".", performer, 5);
/*       */           }
/*       */         
/*       */         } 
/*       */       } 
/*       */     } else {
/*       */       
/* 10747 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the " + bowstring
/* 10748 */           .getName() + " to string the bow.");
/* 10749 */     }  return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean stringRod(Creature performer, Item string, Item rod, Action act) {
/* 10754 */     boolean done = true;
/* 10755 */     if (string.getTemplateId() == 150 || string
/* 10756 */       .getTemplateId() == 151) {
/*       */       
/* 10758 */       boolean insta = (performer.getPower() >= 5);
/* 10759 */       if (rod.getTemplateId() != 780) {
/* 10760 */         performer.getCommunicator().sendNormalServerMessage("The " + rod.getName() + " cannot be stringed.");
/*       */       } else {
/*       */         
/* 10763 */         done = false;
/*       */         
/* 10765 */         if (act.currentSecond() == 1) {
/*       */           
/* 10767 */           act.setTimeLeft(150);
/* 10768 */           performer.getCommunicator().sendNormalServerMessage("You start to string the " + rod.getName() + ".");
/* 10769 */           Server.getInstance().broadCastAction(performer
/* 10770 */               .getName() + " starts to string " + rod.getNameWithGenus() + ".", performer, 5);
/* 10771 */           performer.sendActionControl(Actions.actionEntrys[132].getVerbString(), true, act.getTimeLeft());
/*       */         
/*       */         }
/*       */         else {
/*       */           
/* 10776 */           int timeleft = act.getTimeLeft();
/* 10777 */           if (insta || act.getCounterAsFloat() * 10.0F >= timeleft)
/*       */           {
/* 10779 */             if (string.getRarity() != 0)
/*       */             {
/* 10781 */               performer.playPersonalSound("sound.fx.drumroll");
/*       */             }
/* 10783 */             int realTemplate = 94;
/* 10784 */             if (string.getTemplateId() == 150) {
/* 10785 */               realTemplate = 94;
/* 10786 */             } else if (string.getTemplateId() == 151) {
/* 10787 */               realTemplate = 152;
/*       */             } 
/* 10789 */             done = true;
/* 10790 */             rod.setTemplateId(realTemplate);
/* 10791 */             if (string.getRarity() != 0 && Server.rand.nextInt(100) == 0 && 
/* 10792 */               string.getRarity() > rod.getRarity())
/* 10793 */               rod.setRarity(string.getRarity()); 
/* 10794 */             Items.destroyItem(string.getWurmId());
/* 10795 */             performer.getCommunicator().sendNormalServerMessage("You string the " + rod.getName() + ".");
/* 10796 */             Server.getInstance().broadCastAction(performer.getName() + " strings " + rod.getNameWithGenus() + ".", performer, 5);
/*       */           }
/*       */         
/*       */         } 
/*       */       } 
/*       */     } else {
/*       */       
/* 10803 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the " + string
/* 10804 */           .getName() + " to string the rod.");
/* 10805 */     }  return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean unstringBow(Creature performer, Item bow, Action act, float counter) {
/* 10810 */     boolean done = true;
/*       */     
/* 10812 */     if (bow.getTopParent() != performer.getInventory().getWurmId()) {
/*       */       
/* 10814 */       performer.getCommunicator().sendNormalServerMessage("You must first pick the " + bow
/* 10815 */           .getTemplate().getName() + " up in order to do that.");
/* 10816 */       return true;
/*       */     } 
/*       */     
/* 10819 */     if (bow.isBowUnstringed()) {
/* 10820 */       performer.getCommunicator().sendNormalServerMessage("The bow is already unstringed.");
/* 10821 */     } else if (bow.isWeaponBow()) {
/*       */       
/* 10823 */       boolean insta = (performer.getPower() >= 5);
/* 10824 */       done = false;
/* 10825 */       if (act.currentSecond() == 1) {
/*       */         
/* 10827 */         act.setTimeLeft(150);
/* 10828 */         performer.getCommunicator().sendNormalServerMessage("You start to unstring the " + bow.getName() + ".");
/* 10829 */         Server.getInstance().broadCastAction(performer
/* 10830 */             .getName() + " starts to unstring " + bow.getNameWithGenus() + ".", performer, 5);
/* 10831 */         performer
/* 10832 */           .sendActionControl(Actions.actionEntrys[133].getVerbString(), true, act.getTimeLeft());
/*       */       }
/*       */       else {
/*       */         
/* 10836 */         int timeleft = act.getTimeLeft();
/* 10837 */         if (insta || act.getCounterAsFloat() * 10.0F >= timeleft) {
/*       */           
/* 10839 */           done = true;
/* 10840 */           byte ql = (byte)Math.max(2, bow.getAuxData() - 1);
/*       */           
/*       */           try {
/* 10843 */             Item bowstring = ItemFactory.createItem(457, ql, null);
/* 10844 */             int realTemplate = 459;
/* 10845 */             if (bow.getTemplateId() == 449) {
/* 10846 */               realTemplate = 461;
/* 10847 */             } else if (bow.getTemplateId() == 448) {
/* 10848 */               realTemplate = 460;
/* 10849 */             }  performer.getCommunicator().sendNormalServerMessage("You unstring the " + bow.getName() + ".");
/* 10850 */             Server.getInstance().broadCastAction(performer
/* 10851 */                 .getName() + " unstrings " + performer.getHisHerItsString() + " " + bow.getName() + ".", performer, 5);
/*       */             
/* 10853 */             bow.setTemplateId(realTemplate);
/* 10854 */             bow.setAuxData((byte)0);
/* 10855 */             performer.getInventory().insertItem(bowstring, true);
/*       */           }
/* 10857 */           catch (NoSuchTemplateException nst) {
/*       */             
/* 10859 */             logger.log(Level.WARNING, performer.getName() + ":" + bow.getName() + " " + nst.getMessage(), (Throwable)nst);
/* 10860 */             performer.getCommunicator().sendNormalServerMessage("You fail. The string seems stuck.");
/*       */           }
/* 10862 */           catch (FailedException nf) {
/*       */             
/* 10864 */             logger.log(Level.WARNING, performer.getName() + ":" + bow.getName() + " " + nf.getMessage(), (Throwable)nf);
/* 10865 */             performer.getCommunicator().sendNormalServerMessage("You fail. The string seems stuck.");
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     } else {
/*       */       
/* 10871 */       performer.getCommunicator().sendNormalServerMessage("You can not unstring that!");
/* 10872 */     }  return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean smear(Creature performer, Item potion, Item target, Action act, float counter) {
/*       */     ItemTemplate potionTemplate;
/* 10878 */     boolean done = false;
/*       */ 
/*       */     
/* 10881 */     if (!Spell.mayBeEnchanted(target)) {
/*       */       
/* 10883 */       performer.getCommunicator().sendNormalServerMessage("You can't imbue that.");
/* 10884 */       return true;
/*       */     } 
/*       */ 
/*       */     
/*       */     try {
/* 10889 */       potionTemplate = ItemTemplateFactory.getInstance().getTemplate(potion.getTemplateId());
/*       */     }
/* 10891 */     catch (NoSuchTemplateException e) {
/*       */       
/* 10893 */       performer.getCommunicator().sendAlertServerMessage("ERROR: Could not load item template for" + potion
/* 10894 */           .getName() + ". Please report this.");
/* 10895 */       logger.warning("Could not locate template for " + potion.getName() + " with wid=" + potion
/* 10896 */           .getWurmId() + ". " + e);
/* 10897 */       return true;
/*       */     } 
/*       */ 
/*       */     
/* 10901 */     if (potion.isLiquid())
/*       */     {
/* 10903 */       if (potion.getWeightGrams() < potionTemplate.getWeightGrams()) {
/*       */         
/* 10905 */         performer.getCommunicator().sendNormalServerMessage("The " + potion.getName() + " does not contain enough liquid to smear the " + target
/* 10906 */             .getName());
/* 10907 */         return true;
/*       */       } 
/*       */     }
/* 10910 */     if (!done && counter == 1.0F) {
/*       */       
/* 10912 */       byte enchantment = potion.getEnchantForPotion();
/* 10913 */       if (enchantment == 91 || enchantment == 90 || enchantment == 92)
/*       */       {
/* 10915 */         if (target.enchantment != 0) {
/*       */           
/* 10917 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10918 */               .getName() + " already has that kind of enchantment.");
/* 10919 */           return true;
/*       */         } 
/*       */       }
/* 10922 */       ItemSpellEffects currentEffects = target.getSpellEffects();
/* 10923 */       if (currentEffects == null)
/* 10924 */         currentEffects = new ItemSpellEffects(target.getWurmId()); 
/* 10925 */       SpellEffect eff = currentEffects.getSpellEffect(enchantment);
/* 10926 */       if (eff != null && enchantment == 98) {
/*       */         
/* 10928 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10929 */             .getName() + " is already protected against shattering.");
/* 10930 */         return true;
/*       */       } 
/* 10932 */       if (eff != null && eff.getPower() >= 100.0F) {
/*       */         
/* 10934 */         performer.getCommunicator().sendNormalServerMessage("The power of the " + target
/* 10935 */             .getName() + " is already at max.");
/* 10936 */         return true;
/*       */       } 
/* 10938 */       act.setTimeLeft(200);
/* 10939 */       performer.getCommunicator().sendNormalServerMessage("You start to smear the " + potion
/* 10940 */           .getName() + " on the " + target.getName() + ".");
/* 10941 */       Server.getInstance().broadCastAction(performer
/* 10942 */           .getName() + " starts to smear " + potion.getNameWithGenus() + " on " + target
/* 10943 */           .getNameWithGenus() + ".", performer, 5);
/* 10944 */       performer.sendActionControl(Actions.actionEntrys[633].getVerbString(), true, act.getTimeLeft());
/*       */     } 
/* 10946 */     if (!done && counter > (act.getTimeLeft() / 10)) {
/*       */       
/* 10948 */       done = true;
/* 10949 */       performer.getCommunicator().sendNormalServerMessage("You imbue " + target
/* 10950 */           .getNameWithGenus() + " with " + potion.getNameWithGenus() + ".");
/* 10951 */       Server.getInstance().broadCastAction(performer.getName() + " imbues " + target.getNameWithGenus() + ".", performer, 5);
/*       */ 
/*       */       
/* 10954 */       Skill alch = null;
/*       */       
/*       */       try {
/* 10957 */         alch = performer.getSkills().getSkill(10042);
/*       */       }
/* 10959 */       catch (NoSuchSkillException nss) {
/*       */         
/* 10961 */         alch = performer.getSkills().learn(10042, 1.0F);
/*       */       } 
/* 10963 */       byte enchantment = potion.getEnchantForPotion();
/* 10964 */       if (enchantment == 91 || enchantment == 90 || enchantment == 92) {
/*       */         
/* 10966 */         if (target.enchantment != 0) {
/*       */           
/* 10968 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10969 */               .getName() + " already has that kind of enchantment.");
/* 10970 */           return true;
/*       */         } 
/*       */ 
/*       */         
/* 10974 */         target.enchant(enchantment);
/* 10975 */         String damtype = "fire";
/* 10976 */         if (enchantment == 90) {
/* 10977 */           damtype = "acid";
/* 10978 */         } else if (enchantment == 92) {
/* 10979 */           damtype = "frost";
/* 10980 */         }  performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10981 */             .getName() + " will now deal " + damtype + " damage.");
/*       */       
/*       */       }
/*       */       else {
/*       */         
/* 10986 */         ItemSpellEffects spellEffects = target.getSpellEffects();
/* 10987 */         if (spellEffects == null)
/* 10988 */           spellEffects = new ItemSpellEffects(target.getWurmId()); 
/* 10989 */         SpellEffect eff = spellEffects.getSpellEffect(enchantment);
/* 10990 */         double skpower = alch.skillCheck(50.0D, potion.getCurrentQualityLevel(), false, 1.0F);
/* 10991 */         double power = 5.0D + Math.max(20.0D, skpower) / 20.0D + (potion.getCurrentQualityLevel() / 20.0F);
/*       */         
/* 10993 */         if (eff == null && potion.getTemplateId() == 1091) {
/*       */ 
/*       */           
/* 10996 */           float toReturnPower = 100.0F;
/*       */           
/* 10998 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10999 */               .getName() + " will now be protected against cracking and shattering.");
/* 11000 */           eff = new SpellEffect(target.getWurmId(), enchantment, toReturnPower, 20000000);
/* 11001 */           spellEffects.addSpellEffect(eff);
/* 11002 */           Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*       */         }
/* 11004 */         else if (eff == null) {
/*       */           
/* 11006 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 11007 */               .getName() + " will now be more effective when used for the specific purpose.");
/* 11008 */           eff = new SpellEffect(target.getWurmId(), enchantment, (float)power, 20000000);
/* 11009 */           spellEffects.addSpellEffect(eff);
/* 11010 */           Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*       */ 
/*       */         
/*       */         }
/* 11014 */         else if (eff.getPower() < 100.0F) {
/*       */           
/* 11016 */           performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the effectiveness of the " + target
/* 11017 */               .getName() + ".");
/* 11018 */           eff.setPower(Math.min(100.0F, eff.getPower() + (float)power));
/* 11019 */           Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*       */         } 
/*       */       } 
/*       */       
/* 11023 */       potion.setWeight(potion.getWeightGrams() - potionTemplate.getWeightGrams(), true);
/*       */     } 
/* 11025 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean createOil(Creature performer, Item sourceSalt, Item target, Action act, float counter) {
/* 11031 */     boolean done = true;
/* 11032 */     Skill alch = null;
/*       */     
/*       */     try {
/* 11035 */       alch = performer.getSkills().getSkill(10042);
/*       */     }
/* 11037 */     catch (NoSuchSkillException nss) {
/*       */       
/* 11039 */       alch = performer.getSkills().learn(10042, 1.0F);
/*       */     } 
/* 11041 */     int knowl = (int)alch.getKnowledge(0.0D);
/* 11042 */     done = false;
/* 11043 */     if (sourceSalt.getWeightGrams() < sourceSalt.getTemplate().getWeightGrams() * 10) {
/*       */       
/* 11045 */       performer.getCommunicator().sendNormalServerMessage("The " + sourceSalt
/* 11046 */           .getName() + " contains too little material.");
/* 11047 */       return true;
/*       */     } 
/* 11049 */     if (target.getWeightGrams() < target.getTemplate().getWeightGrams()) {
/*       */       
/* 11051 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " contains too little material.");
/* 11052 */       return true;
/*       */     } 
/* 11054 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*       */       
/* 11056 */       performer.getCommunicator().sendNormalServerMessage("You do not have enough room in your inventory.");
/* 11057 */       return true;
/*       */     } 
/* 11059 */     if (!done && counter == 1.0F) {
/*       */       
/* 11061 */       int time = 200;
/* 11062 */       time = Math.max(50, time - knowl);
/* 11063 */       act.setTimeLeft(time);
/* 11064 */       performer.getCommunicator().sendNormalServerMessage("You start to create something from the " + sourceSalt
/* 11065 */           .getName() + " and the " + target.getName() + ".");
/* 11066 */       Server.getInstance().broadCastAction(performer
/* 11067 */           .getName() + " starts to create something from " + sourceSalt.getNameWithGenus() + " and " + target
/* 11068 */           .getNameWithGenus() + ".", performer, 5);
/* 11069 */       performer.sendActionControl(Actions.actionEntrys[283].getVerbString(), true, act.getTimeLeft());
/*       */     } 
/* 11071 */     if (!done && counter > (act.getTimeLeft() / 10)) {
/*       */       
/* 11073 */       done = true;
/* 11074 */       double power = alch.skillCheck(20.0D, sourceSalt, 0.0D, false, counter);
/* 11075 */       if (power > 0.0D) {
/*       */         
/*       */         try
/*       */         {
/* 11079 */           Item potion = ItemFactory.createItem(target.getPotionTemplateIdForBlood(), (float)power, null);
/* 11080 */           performer.getCommunicator().sendNormalServerMessage("You create " + potion.getNameWithGenus() + ".");
/* 11081 */           Server.getInstance().broadCastAction(performer.getName() + " creates " + potion.getNameWithGenus() + ".", performer, 5);
/*       */           
/* 11083 */           int allWeight = sourceSalt.getTemplate().getWeightGrams() * 10 + target.getTemplate().getWeightGrams();
/* 11084 */           potion.setWeight(allWeight, true);
/* 11085 */           performer.getInventory().insertItem(potion, true);
/*       */         }
/* 11087 */         catch (Exception ex)
/*       */         {
/* 11089 */           performer.getCommunicator().sendNormalServerMessage("You fail to create anything useful.");
/* 11090 */           Server.getInstance().broadCastAction(performer.getName() + " fails to create anything useful.", performer, 5);
/*       */         }
/*       */       
/*       */       }
/*       */       else {
/*       */         
/* 11096 */         performer.getCommunicator().sendNormalServerMessage("You fail to create anything useful.");
/* 11097 */         Server.getInstance().broadCastAction(performer.getName() + " fails to create anything useful.", performer, 5);
/*       */       } 
/* 11099 */       sourceSalt.setWeight(sourceSalt.getWeightGrams() - sourceSalt.getTemplate().getWeightGrams() * 10, true);
/* 11100 */       target.setWeight(target.getWeightGrams() - target.getTemplate().getWeightGrams(), true);
/*       */     } 
/* 11102 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean createSalve(Creature performer, Item source, Item target, Action act, float counter) {
/* 11108 */     boolean done = true;
/* 11109 */     Skill alch = null;
/*       */     
/*       */     try {
/* 11112 */       alch = performer.getSkills().getSkill(10042);
/*       */     }
/* 11114 */     catch (NoSuchSkillException nss) {
/*       */       
/* 11116 */       alch = performer.getSkills().learn(10042, 1.0F);
/*       */     } 
/* 11118 */     int knowl = (int)alch.getKnowledge(0.0D);
/* 11119 */     int pow = source.getAlchemyType() * target.getAlchemyType();
/* 11120 */     done = false;
/* 11121 */     if (source.getWeightGrams() < source.getTemplate().getWeightGrams()) {
/*       */       
/* 11123 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " contains too little material.");
/* 11124 */       return true;
/*       */     } 
/* 11126 */     if (target.getWeightGrams() < target.getTemplate().getWeightGrams()) {
/*       */       
/* 11128 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " contains too little material.");
/* 11129 */       return true;
/*       */     } 
/* 11131 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*       */       
/* 11133 */       performer.getCommunicator().sendNormalServerMessage("You do not have enough room in your inventory.");
/* 11134 */       return true;
/*       */     } 
/* 11136 */     if (!done && counter == 1.0F) {
/*       */       
/* 11138 */       int time = 200;
/* 11139 */       time = Math.max(50, time - knowl);
/* 11140 */       act.setTimeLeft(time);
/* 11141 */       performer.getCommunicator().sendNormalServerMessage("You start to create a healing cover of the " + source
/* 11142 */           .getName() + ", the " + target.getName() + " and some grass, leaves and moss.");
/*       */       
/* 11144 */       Server.getInstance().broadCastAction(performer
/* 11145 */           .getName() + " starts to create something from " + source.getNameWithGenus() + ", " + target
/* 11146 */           .getNameWithGenus() + " and some grass, leaves and moss.", performer, 5);
/* 11147 */       performer.sendActionControl(Actions.actionEntrys[283].getVerbString(), true, act.getTimeLeft());
/*       */     } 
/* 11149 */     if (!done && counter > (act.getTimeLeft() / 10)) {
/*       */       
/* 11151 */       done = true;
/* 11152 */       double power = alch.skillCheck(pow, source, 0.0D, false, counter);
/* 11153 */       if (power > 0.0D) {
/*       */         
/*       */         try
/*       */         {
/* 11157 */           Item cover = ItemFactory.createItem(481, (float)power, null);
/* 11158 */           performer.getCommunicator().sendNormalServerMessage("You create a healing cover.");
/* 11159 */           Server.getInstance().broadCastAction(performer.getName() + " creates a healing cover.", performer, 5);
/* 11160 */           int allWeight = source.getTemplate().getWeightGrams() + target.getTemplate().getWeightGrams();
/* 11161 */           cover.setAuxData((byte)pow);
/* 11162 */           cover.setWeight(allWeight, true);
/* 11163 */           cover.setDescription("made from " + source.getName().toLowerCase() + " and " + target
/* 11164 */               .getName().toLowerCase());
/* 11165 */           performer.getInventory().insertItem(cover, true);
/*       */ 
/*       */           
/* 11168 */           performer.achievement(546);
/*       */         }
/* 11170 */         catch (Exception ex)
/*       */         {
/* 11172 */           performer.getCommunicator().sendNormalServerMessage("You fail to create a healing cover.");
/* 11173 */           Server.getInstance().broadCastAction(performer.getName() + " fails to create a healing cover.", performer, 5);
/*       */         }
/*       */       
/*       */       }
/*       */       else {
/*       */         
/* 11179 */         performer.getCommunicator().sendNormalServerMessage("You fail to create a healing cover.");
/* 11180 */         Server.getInstance().broadCastAction(performer.getName() + " fails to create a healing cover.", performer, 5);
/*       */       } 
/* 11182 */       source.setWeight(source.getWeightGrams() - source.getTemplate().getWeightGrams(), true);
/* 11183 */       target.setWeight(target.getWeightGrams() - target.getTemplate().getWeightGrams(), true);
/*       */     } 
/* 11185 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static final boolean createPotion(Creature performer, Item source, Item target, Action act, float counter, byte potionAuxData) {
/* 11191 */     boolean done = true;
/* 11192 */     Skill alch = performer.getSkills().getSkillOrLearn(10042);
/* 11193 */     int knowl = (int)alch.getKnowledge(0.0D);
/* 11194 */     done = false;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11210 */     int totalWeight = 0;
/* 11211 */     Item[] contents = target.getAllItems(false);
/* 11212 */     int templateWeight = 0;
/* 11213 */     boolean allSame = false;
/* 11214 */     float solidAverageQL = 0.0F;
/* 11215 */     if (contents.length >= 1) {
/*       */       
/* 11217 */       allSame = true;
/*       */       
/* 11219 */       int lookingFor = contents[0].getTemplateId();
/* 11220 */       templateWeight = contents[0].getTemplate().getWeightGrams();
/* 11221 */       for (Item i : contents)
/*       */       {
/* 11223 */         int solidWeight = i.getWeightGrams();
/* 11224 */         if (i.getTemplateId() != lookingFor) {
/*       */           
/* 11226 */           allSame = false;
/*       */ 
/*       */           
/*       */           break;
/*       */         } 
/*       */         
/* 11232 */         float qlwt = solidAverageQL * totalWeight;
/* 11233 */         qlwt += solidWeight * i.getCurrentQualityLevel();
/* 11234 */         totalWeight += solidWeight;
/* 11235 */         solidAverageQL = qlwt / totalWeight;
/*       */       }
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 11241 */       performer.getCommunicator().sendNormalServerMessage("There is nothing in the " + target
/* 11242 */           .getName() + " to do anything with.");
/* 11243 */       return true;
/*       */     } 
/* 11245 */     if (!allSame) {
/*       */       
/* 11247 */       performer.getCommunicator().sendNormalServerMessage("There are contaminants in the " + target
/* 11248 */           .getName() + ", so liquid would be ruined.");
/* 11249 */       return true;
/*       */     } 
/*       */     
/* 11252 */     int numbItems = totalWeight / templateWeight;
/*       */     
/* 11254 */     if (numbItems == 0) {
/*       */       
/* 11256 */       performer.getCommunicator().sendNormalServerMessage("There is not enough of " + contents[0]
/* 11257 */           .getName() + " to do anything with.");
/* 11258 */       return true;
/*       */     } 
/*       */     
/* 11261 */     int neededLiquid = (int)(numbItems * 50.0F);
/*       */     
/* 11263 */     if (neededLiquid > source.getWeightGrams()) {
/*       */       
/* 11265 */       performer.getCommunicator().sendNormalServerMessage("You dont have enough " + source
/* 11266 */           .getName() + " for that much " + contents[0]
/* 11267 */           .getName() + ".");
/* 11268 */       return true;
/*       */     } 
/* 11270 */     if (!done && counter == 1.0F) {
/*       */       
/* 11272 */       int time = 200;
/* 11273 */       time = Math.max(50, time - knowl);
/* 11274 */       act.setTimeLeft(time);
/* 11275 */       performer.getCommunicator().sendNormalServerMessage("You start to create some transmutation liquid using " + source
/* 11276 */           .getName() + " on the " + contents[0].getName() + ".");
/* 11277 */       Server.getInstance().broadCastAction(performer
/* 11278 */           .getName() + " starts to create something from " + source.getNameWithGenus() + " and " + contents[0]
/* 11279 */           .getNameWithGenus(), performer, 5);
/* 11280 */       performer.sendActionControl(Actions.actionEntrys[283].getVerbString(), true, act.getTimeLeft());
/*       */     } 
/* 11282 */     if (act.currentSecond() == 2) {
/* 11283 */       performer.getCommunicator().sendNormalServerMessage("You see the " + contents[0].getName() + " absorb the " + source.getName() + ".");
/* 11284 */     } else if (act.currentSecond() == 4) {
/* 11285 */       performer.getCommunicator().sendNormalServerMessage("Now the " + contents[0].getName() + " starts to effervesce.");
/* 11286 */     } else if (act.currentSecond() == 6) {
/* 11287 */       performer.getCommunicator().sendNormalServerMessage("The bubbles now obscure the " + contents[0].getName() + ".");
/* 11288 */     } else if (act.currentSecond() == 8) {
/* 11289 */       performer.getCommunicator().sendNormalServerMessage("The bubbles start receeding.");
/* 11290 */     }  if (act.currentSecond() % 5 == 0)
/*       */     {
/* 11292 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     }
/* 11294 */     if (!done && counter > (act.getTimeLeft() / 10)) {
/*       */       
/* 11296 */       int difficulty = getTransmutationLiquidDifficulty(potionAuxData);
/* 11297 */       if (act.getRarity() != 0) {
/*       */         
/* 11299 */         performer.playPersonalSound("sound.fx.drumroll");
/* 11300 */         switch (act.getRarity()) {
/*       */ 
/*       */           
/*       */           case 1:
/* 11304 */             difficulty = (int)(difficulty * 0.8F);
/*       */             break;
/*       */           case 2:
/* 11307 */             difficulty = (int)(difficulty * 0.5F);
/*       */             break;
/*       */           case 3:
/* 11310 */             difficulty = (int)(difficulty * 0.2F);
/*       */             break;
/*       */         } 
/*       */       
/*       */       } 
/* 11315 */       done = true;
/* 11316 */       float alc = 0.0F;
/* 11317 */       if (performer.isPlayer()) {
/* 11318 */         alc = ((Player)performer).getAlcohol();
/*       */       }
/* 11320 */       float bonus = (solidAverageQL + source.getCurrentQualityLevel()) / 2.0F;
/* 11321 */       double power = alch.skillCheck((difficulty + alc), source, bonus, false, counter);
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 11326 */       switch (act.getRarity()) {
/*       */ 
/*       */         
/*       */         case 1:
/* 11330 */           power *= 1.2000000476837158D;
/*       */           break;
/*       */         case 2:
/* 11333 */           power *= 1.5D;
/*       */           break;
/*       */         case 3:
/* 11336 */           power *= 1.7999999523162842D;
/*       */           break;
/*       */       } 
/*       */       
/* 11340 */       power = Math.min(power, 100.0D);
/* 11341 */       if (power > 0.0D) {
/*       */         
/*       */         try
/*       */         {
/*       */           
/* 11346 */           Item potion = ItemFactory.createItem(654, (float)power, null);
/*       */           
/* 11348 */           potion.setAuxData(potionAuxData);
/*       */           
/* 11350 */           potion.setWeight(numbItems * templateWeight, false);
/* 11351 */           potion.setDescription(getTransmutationLiquidDescription(potionAuxData));
/*       */           
/* 11353 */           for (Item i : contents) {
/* 11354 */             Items.destroyItem(i.getWurmId());
/*       */           }
/* 11356 */           source.setWeight(source.getWeightGrams() - neededLiquid, true);
/*       */           
/* 11358 */           target.insertItem(potion, true);
/* 11359 */           performer.getCommunicator().sendNormalServerMessage("You create some transmutation liquid.");
/* 11360 */           Server.getInstance().broadCastAction(performer.getName() + " creates some transmutation liquid.", performer, 5);
/*       */         }
/* 11362 */         catch (Exception ex)
/*       */         {
/* 11364 */           performer.getCommunicator().sendNormalServerMessage("You fail to create the transmutation liquid.");
/* 11365 */           Server.getInstance().broadCastAction(performer.getName() + " fails to create the transmutation liquid.", performer, 5);
/*       */         }
/*       */       
/*       */       }
/*       */       else {
/*       */         
/* 11371 */         performer.getCommunicator().sendNormalServerMessage("You fail to create the transmutation liquid.");
/* 11372 */         Server.getInstance().broadCastAction(performer.getName() + " fails to create the transmutation liquid.", performer, 5);
/*       */       } 
/*       */     } 
/*       */     
/* 11376 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean askSleep(Action act, Creature performer, Item target, float counter) {
/* 11381 */     if (!performer.isPlayer())
/* 11382 */       return true; 
/* 11383 */     if (performer.isGuest()) {
/*       */       
/* 11385 */       performer.getCommunicator().sendNormalServerMessage("Guests are not allowed to sleep in beds.");
/* 11386 */       return true;
/*       */     } 
/* 11388 */     if ((((Player)performer).getSaveFile()).bed > 0L) {
/*       */       
/* 11390 */       performer.getCommunicator().sendNormalServerMessage("You are already asleep. Or are you sleepwalking?");
/* 11391 */       return true;
/*       */     } 
/* 11393 */     PlayerInfo info = PlayerInfoFactory.getPlayerSleepingInBed(target.getWurmId());
/* 11394 */     if (info != null) {
/*       */       
/* 11396 */       performer.getCommunicator().sendNormalServerMessage("The bed is already occupied by " + info.getName() + ".");
/* 11397 */       return true;
/*       */     } 
/* 11399 */     VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 11400 */     if (t != null) {
/*       */       
/* 11402 */       if (t.getStructure() == null || !t.getStructure().isTypeHouse()) {
/*       */         
/* 11404 */         performer.getCommunicator().sendNormalServerMessage("You would get no sleep outside tonight.");
/* 11405 */         return true;
/*       */       } 
/* 11407 */       if (!t.getStructure().isFinished()) {
/*       */         
/* 11409 */         performer.getCommunicator().sendNormalServerMessage("The house is too windy to provide protection.");
/* 11410 */         return true;
/*       */       } 
/*       */       
/* 11413 */       if (target.getWhenRented() > System.currentTimeMillis() - 86400000L) {
/*       */         
/* 11415 */         if (target.getData() != performer.getWurmId()) {
/*       */           
/* 11417 */           PlayerInfo renter = PlayerInfoFactory.getPlayerInfoWithWurmId(target.getData());
/* 11418 */           if (renter != null) {
/*       */             
/* 11420 */             performer.getCommunicator().sendNormalServerMessage(renter.getName() + " has already rented that bed, so you are unable to use it.");
/* 11421 */             return true;
/*       */           } 
/*       */           
/* 11424 */           target.setWhenRented(0L);
/*       */         } 
/*       */       } else {
/*       */         
/* 11428 */         target.setWhenRented(0L);
/*       */       } 
/* 11430 */       if (!((target.getRentCost() > 0 && target.getData() == performer.getWurmId()) ? 1 : 0) && 
/* 11431 */         !target.mayUseBed(performer)) {
/*       */         
/* 11433 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to sleep here.");
/* 11434 */         return true;
/*       */       } 
/*       */     } else {
/*       */       
/* 11438 */       logger.log(Level.WARNING, "Why is tile for bed at " + target
/* 11439 */           .getTileX() + "," + target.getTileY() + "," + target.isOnSurface() + " null?");
/*       */     } 
/* 11441 */     SleepQuestion spm = new SleepQuestion(performer, "Go to sleep?", "Sleep:", target.getWurmId());
/* 11442 */     spm.sendQuestion();
/* 11443 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean sleep(Action act, Creature performer, Item target, float counter) {
/* 11448 */     boolean done = true;
/* 11449 */     if (!performer.isPlayer()) {
/* 11450 */       return true;
/*       */     }
/*       */     
/* 11453 */     if ((((Player)performer).getSaveFile()).bed > 0L && counter <= act.getPower()) {
/*       */       
/* 11455 */       performer.getCommunicator().sendNormalServerMessage("You are already asleep. Or are you sleepwalking?");
/* 11456 */       return true;
/*       */     } 
/*       */     
/* 11459 */     if (target.getTopParent() != target.getWurmId()) {
/*       */       
/* 11461 */       performer.getCommunicator().sendNormalServerMessage("The bed needs to be on the ground.");
/* 11462 */       return true;
/*       */     } 
/*       */     
/* 11465 */     PlayerInfo info = PlayerInfoFactory.getPlayerSleepingInBed(target.getWurmId());
/* 11466 */     if (info != null)
/*       */     {
/* 11468 */       if (info.wurmId != performer.getWurmId()) {
/*       */         
/* 11470 */         performer.getCommunicator().sendNormalServerMessage("The bed is already occupied by " + info
/* 11471 */             .getName() + ".");
/* 11472 */         performer.achievement(100);
/* 11473 */         return true;
/*       */       } 
/*       */     }
/*       */     
/* 11477 */     VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 11478 */     if (t != null) {
/*       */       
/* 11480 */       if (performer.getPower() <= 0) {
/*       */         
/* 11482 */         if (t.getStructure() == null || !t.getStructure().isTypeHouse()) {
/*       */           
/* 11484 */           performer.getCommunicator().sendNormalServerMessage("You would get no sleep outside tonight.");
/* 11485 */           return true;
/*       */         } 
/* 11487 */         if (!t.getStructure().isFinished()) {
/*       */           
/* 11489 */           performer.getCommunicator().sendNormalServerMessage("The house is too windy to provide protection.");
/* 11490 */           return true;
/*       */         } 
/*       */         
/* 11493 */         if (!((target.getRentCost() > 0 && target.getData() == performer.getWurmId()) ? 1 : 0) && 
/* 11494 */           !target.mayUseBed(performer)) {
/*       */           
/* 11496 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to sleep here.");
/* 11497 */           return true;
/*       */         } 
/* 11499 */         if (target.getData() == performer.getWurmId());
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 11505 */       done = false;
/*       */     } else {
/*       */       
/* 11508 */       logger.log(Level.WARNING, "Why is tile for bed at " + target.getTileX() + "," + target.getTileY() + "," + target
/* 11509 */           .isOnSurface() + " null?");
/* 11510 */     }  if (counter == 1.0F) {
/*       */       
/* 11512 */       int timetologout = ((Player)performer).getSecondsToLogout() * 10;
/* 11513 */       act.setPower(((Player)performer).getSecondsToLogout());
/* 11514 */       performer.getCommunicator().sendNormalServerMessage("You start to go to sleep.");
/* 11515 */       Server.getInstance().broadCastAction(performer
/* 11516 */           .getName() + " starts to go to sleep in " + target.getNameWithGenus() + ".", performer, 5);
/* 11517 */       performer.sendActionControl("Sleep - logging off", true, timetologout);
/*       */     } 
/* 11519 */     if (act.currentSecond() > act.getPower())
/*       */     {
/* 11521 */       if (act.justTickedSecond()) {
/*       */         
/* 11523 */         performer.getCommunicator().sendShutDown("You went to sleep. Sweet dreams.", true);
/* 11524 */         ((Player)performer).getSaveFile().setBed(target.getWurmId());
/* 11525 */         ((Player)performer).setLogout();
/* 11526 */         Server.getInstance().broadCastAction(performer
/* 11527 */             .getName() + " goes to sleep in " + target.getNameWithGenus() + ".", performer, 5);
/* 11528 */         target.setDamage(target.getDamage() + 0.01F * target.getDamageModifier());
/*       */         
/* 11530 */         if (act.currentSecond() > act.getPower() + 2.0F) {
/*       */ 
/*       */ 
/*       */           
/* 11534 */           done = true;
/* 11535 */           if (performer.getCurrentTile() != null)
/* 11536 */             performer.getCurrentTile().deleteCreature(performer); 
/* 11537 */           Players.getInstance().logoutPlayer((Player)performer);
/*       */ 
/*       */           
/* 11540 */           performer.achievement(547);
/*       */         } 
/*       */       } 
/*       */     }
/*       */     
/* 11545 */     return done;
/*       */   }
/*       */ 
/*       */   
/*       */   public static boolean mayDropOnTile(int tileX, int tileY, boolean surfaced, int floorLevel) {
/* 11550 */     VolaTile t = Zones.getTileOrNull(tileX, tileY, surfaced);
/* 11551 */     if (t != null) {
/*       */       
/* 11553 */       if (t.hasOnePerTileItem(t.getDropFloorLevel(floorLevel)))
/* 11554 */         return false; 
/* 11555 */       if (t.getNumberOfItems(t.getDropFloorLevel(floorLevel)) >= 100)
/* 11556 */         return false; 
/* 11557 */       if (t.getFourPerTileCount(floorLevel) >= 4)
/* 11558 */         return false; 
/*       */     } 
/* 11560 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static boolean mayDropOnTile(Creature performer) {
/* 11566 */     float posX = performer.getStatus().getPositionX();
/* 11567 */     float posY = performer.getStatus().getPositionY();
/* 11568 */     float rot = performer.getStatus().getRotation();
/*       */     
/* 11570 */     float xPosMod = (float)Math.sin((rot * 0.017453292F)) * 2.0F;
/* 11571 */     float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * 2.0F;
/* 11572 */     posX += xPosMod;
/* 11573 */     posY += yPosMod;
/*       */     
/* 11575 */     int placedX = (int)posX >> 2;
/* 11576 */     int placedY = (int)posY >> 2;
/*       */     
/* 11578 */     return mayDropOnTile(placedX, placedY, performer.isOnSurface(), performer.getFloorLevel());
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static byte[] getAllNormalWoodTypes() {
/* 11584 */     byte[] creationWoodTypes = { 14, 37, 38, 39, 40, 41, 42, 43, 44, 45, 63, 64, 65, 66, 88, 51, 46, 47, 48, 49, 50, 71, 90, 91 };
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11594 */     return creationWoodTypes;
/*       */   }
/*       */ 
/*       */   
/*       */   public static byte[] getAllMetalTypes() {
/* 11599 */     byte[] metalTypes = { 7, 8, 9, 10, 11, 12, 13, 30, 31, 34, 56, 57, 67, 96 };
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11605 */     return metalTypes;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static boolean mayDropTentOnTile(Creature performer) {
/* 11611 */     for (int x = performer.getTileX() - 1; x <= performer.getTileX() + 1; x++) {
/* 11612 */       for (int y = performer.getTileY() - 1; y <= performer.getTileY() + 1; y++) {
/*       */         
/* 11614 */         VolaTile t = Zones.getTileOrNull(Zones.safeTileX(x), Zones.safeTileY(y), performer.isOnSurface());
/* 11615 */         if (t != null) {
/*       */           
/* 11617 */           if (t.getVillage() != null && t.getVillage() != performer.getCitizenVillage())
/*       */           {
/* 11619 */             return false;
/*       */           }
/* 11621 */           if (Villages.getVillageWithPerimeterAt(Zones.safeTileX(x), Zones.safeTileY(y), true) != null)
/*       */           {
/* 11623 */             return false;
/*       */           }
/*       */         } 
/* 11626 */         if (performer.isOnSurface()) {
/*       */           
/* 11628 */           int tile = Server.surfaceMesh.getTile(Zones.safeTileX(x), Zones.safeTileY(y));
/* 11629 */           if (Tiles.decodeHeight(tile) < 1)
/*       */           {
/* 11631 */             return false; } 
/*       */         } 
/*       */       } 
/*       */     } 
/* 11635 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean setRent(Action act, Creature performer, Item target) {
/* 11640 */     if (!target.isBed()) {
/*       */       
/* 11642 */       performer.getCommunicator().sendNormalServerMessage("You may not rent that!");
/* 11643 */       return true;
/*       */     } 
/* 11645 */     VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 11646 */     if (t != null) {
/*       */       
/* 11648 */       if (t.getStructure() == null) {
/*       */         
/* 11650 */         performer.getCommunicator().sendNormalServerMessage("There is no structure here, so you can not charge for the bed.");
/*       */         
/* 11652 */         return true;
/*       */       } 
/* 11654 */       if (t.getStructure().isOwner(performer)) {
/*       */         
/* 11656 */         if (act.getNumber() == 319) {
/*       */           
/* 11658 */           target.setAuxData((byte)1);
/* 11659 */           performer.getCommunicator().sendNormalServerMessage("You set the rent to 1 copper.");
/*       */         }
/* 11661 */         else if (act.getNumber() == 320) {
/*       */           
/* 11663 */           target.setAuxData((byte)2);
/* 11664 */           performer.getCommunicator().sendNormalServerMessage("You set the rent to 10 copper.");
/*       */         }
/* 11666 */         else if (act.getNumber() == 321) {
/*       */           
/* 11668 */           target.setAuxData((byte)3);
/* 11669 */           performer.getCommunicator().sendNormalServerMessage("You set the rent to 1 silver.");
/*       */         }
/* 11671 */         else if (act.getNumber() == 322) {
/*       */           
/* 11673 */           target.setAuxData((byte)4);
/* 11674 */           performer.getCommunicator().sendNormalServerMessage("You set the rent to 10 silver.");
/*       */         }
/* 11676 */         else if (act.getNumber() == 365) {
/*       */           
/* 11678 */           target.setAuxData((byte)5);
/* 11679 */           performer.getCommunicator().sendNormalServerMessage("You set the rent to 10 iron.");
/*       */         }
/* 11681 */         else if (act.getNumber() == 366) {
/*       */           
/* 11683 */           target.setAuxData((byte)6);
/* 11684 */           performer.getCommunicator().sendNormalServerMessage("You set the rent to 25 iron.");
/*       */         }
/* 11686 */         else if (act.getNumber() == 367) {
/*       */           
/* 11688 */           target.setAuxData((byte)7);
/* 11689 */           performer.getCommunicator().sendNormalServerMessage("You set the rent to 50 iron.");
/*       */         }
/* 11691 */         else if (act.getNumber() == 323) {
/*       */           
/* 11693 */           target.setAuxData((byte)0);
/* 11694 */           performer.getCommunicator().sendNormalServerMessage("The bed may now only be used by you and your guests.");
/*       */         } 
/*       */       } else {
/*       */         
/* 11698 */         performer.getCommunicator().sendNormalServerMessage("You do not own this house, so you can not charge for the bed.");
/*       */       } 
/*       */     } else {
/*       */       
/* 11702 */       performer.getCommunicator().sendNormalServerMessage("There is no structure here, so you can not charge for the bed.");
/*       */     } 
/* 11704 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean rent(Action act, Creature performer, Item target) {
/* 11709 */     if (!target.isBed()) {
/*       */       
/* 11711 */       performer.getCommunicator().sendNormalServerMessage("You may not rent that!");
/* 11712 */       return true;
/*       */     } 
/* 11714 */     if (performer.isGuest()) {
/*       */       
/* 11716 */       performer.getCommunicator().sendNormalServerMessage("Guests are not allowed to sleep in or hire beds.");
/* 11717 */       return true;
/*       */     } 
/* 11719 */     if (performer.isPlayer()) {
/*       */       
/*       */       try {
/*       */         
/* 11723 */         long oldbed = (((Player)performer).getSaveFile()).bed;
/* 11724 */         if (oldbed > 0L && oldbed != target.getWurmId())
/*       */         {
/* 11726 */           Item beds = Items.getItem(oldbed);
/* 11727 */           beds.setData(0L);
/* 11728 */           beds.setWhenRented(0L);
/* 11729 */           performer.getCommunicator().sendNormalServerMessage("Your old bed is now available for someone else to rent.");
/*       */         }
/*       */       
/*       */       }
/* 11733 */       catch (NoSuchItemException noSuchItemException) {}
/*       */     }
/*       */ 
/*       */     
/* 11737 */     PlayerInfo pinf = PlayerInfoFactory.getPlayerSleepingInBed(target.getWurmId());
/*       */     
/* 11739 */     if (pinf != null) {
/*       */       
/* 11741 */       performer.getCommunicator().sendNormalServerMessage("Some kind of mysterious haze lingers over the bed, and you notice that the bed is occupied by the spirit of " + pinf
/*       */           
/* 11743 */           .getName() + ".");
/* 11744 */       return true;
/*       */     } 
/* 11746 */     if (target.getData() > 0L && target.getData() != performer.getWurmId()) {
/*       */       
/* 11748 */       PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(target.getData());
/* 11749 */       if (info != null) {
/*       */ 
/*       */         
/* 11752 */         if (target.getWhenRented() > System.currentTimeMillis() - 86400000L) {
/*       */           
/* 11754 */           performer.getCommunicator().sendNormalServerMessage(info.getName() + " has already rented that bed.");
/* 11755 */           return true;
/*       */         } 
/*       */         
/* 11758 */         target.setWhenRented(0L);
/*       */       } 
/*       */     } 
/* 11761 */     VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 11762 */     if (t != null) {
/*       */       
/* 11764 */       if (t.getStructure() == null) {
/*       */         
/* 11766 */         performer.getCommunicator().sendNormalServerMessage("There is no structure here, so you can not rent the bed.");
/* 11767 */         return true;
/*       */       } 
/* 11769 */       if (t.getStructure().getOwnerId() == performer.getWurmId()) {
/*       */         
/* 11771 */         performer.getCommunicator().sendNormalServerMessage("You don't need to rent the bed in order to sleep in it.");
/* 11772 */         return true;
/*       */       } 
/*       */ 
/*       */       
/* 11776 */       Village vill = t.getVillage();
/* 11777 */       long rentMoney = target.getRentCost();
/* 11778 */       if (rentMoney > 0L) {
/*       */         
/* 11780 */         LoginServerWebConnection conn = new LoginServerWebConnection();
/*       */         
/*       */         try {
/* 11783 */           if (charge(performer, rentMoney))
/*       */           {
/* 11785 */             if (vill != null && 
/* 11786 */               vill.plan != null) {
/*       */               
/* 11788 */               vill.plan.addMoney(rentMoney / 2L);
/* 11789 */               rentMoney /= 2L;
/*       */             } 
/*       */             
/* 11792 */             PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(t.getStructure().getOwnerId());
/* 11793 */             if (info != null)
/* 11794 */               conn.addMoney(t.getStructure().getOwnerId(), info.getName(), rentMoney, "Rented bed"); 
/* 11795 */             target.setData(performer.getWurmId());
/* 11796 */             target.setWhenRented(System.currentTimeMillis());
/* 11797 */             performer.getCommunicator().sendNormalServerMessage("You rent the bed.");
/* 11798 */             if (info != null) {
/*       */               
/*       */               try {
/* 11801 */                 Player p = Players.getInstance().getPlayer(info.wurmId);
/* 11802 */                 p.getCommunicator().sendNormalServerMessage("You rent a bed in " + t
/* 11803 */                     .getStructure().getName() + " to " + performer.getName() + ".");
/*       */               
/*       */               }
/* 11806 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */             
/*       */             }
/*       */           
/*       */           }
/*       */           else
/*       */           {
/* 11813 */             performer.getCommunicator().sendNormalServerMessage("You cannot rent the bed right now. Make sure you have enough money in the bank.");
/*       */             
/* 11815 */             return true;
/*       */           }
/*       */         
/* 11818 */         } catch (Exception ex) {
/*       */           
/* 11820 */           performer.getCommunicator().sendNormalServerMessage(ex.getMessage());
/* 11821 */           return true;
/*       */         } 
/* 11823 */         return false;
/*       */       } 
/*       */       
/* 11826 */       logger.log(Level.INFO, "No Rent cost=" + target.getRentCost());
/*       */     }
/*       */     else {
/*       */       
/* 11830 */       performer.getCommunicator().sendNormalServerMessage("There is no structure here, so you can not charge for the bed.");
/*       */     } 
/* 11832 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   private static final boolean charge(Creature responder, long coinsNeeded) throws FailedException {
/* 11837 */     Item[] items = responder.getInventory().getAllItems(false);
/* 11838 */     List<Item> coins = new LinkedList<>();
/* 11839 */     long value = 0L;
/* 11840 */     for (Item lItem : items) {
/*       */       
/* 11842 */       if (lItem.isCoin()) {
/*       */         
/* 11844 */         coins.add(lItem);
/* 11845 */         value += Economy.getValueFor(lItem.getTemplateId());
/*       */       } 
/*       */     } 
/* 11848 */     if (value < coinsNeeded) {
/*       */       
/* 11850 */       Change change = new Change(coinsNeeded);
/* 11851 */       throw new FailedException("You need " + change.getChangeString() + " coins.");
/*       */     } 
/*       */ 
/*       */     
/* 11855 */     long curv = 0L;
/* 11856 */     for (ListIterator<Item> it = coins.listIterator(); it.hasNext(); ) {
/*       */       
/* 11858 */       Item coin = it.next();
/* 11859 */       curv += Economy.getValueFor(coin.getTemplateId());
/*       */       
/*       */       try {
/* 11862 */         Item parent = coin.getParent();
/* 11863 */         parent.dropItem(coin.getWurmId(), false);
/* 11864 */         Economy.getEconomy().returnCoin(coin, "Charged");
/*       */       }
/* 11866 */       catch (NoSuchItemException nsi) {
/*       */         
/* 11868 */         logger.log(Level.WARNING, responder
/* 11869 */             .getName() + ":  Failed to locate the container for coin " + coin.getName() + ". Value returned is " + (new Change(curv))
/* 11870 */             .getChangeString() + " coins.");
/* 11871 */         Item[] newCoins = Economy.getEconomy().getCoinsFor(Economy.getValueFor(coin.getTemplateId()));
/* 11872 */         Item inventory = responder.getInventory();
/* 11873 */         for (Item lNewCoin : newCoins)
/* 11874 */           inventory.insertItem(lNewCoin); 
/* 11875 */         throw new FailedException("Failed to locate the container for coin " + coin.getName() + ". This is serious and should be reported. Returned " + (new Change(curv))
/* 11876 */             .getChangeString() + " coins.");
/*       */       } 
/*       */       
/* 11879 */       if (curv >= coinsNeeded)
/*       */         break; 
/*       */     } 
/* 11882 */     if (curv > coinsNeeded) {
/*       */       
/* 11884 */       Item[] newCoins = Economy.getEconomy().getCoinsFor(curv - coinsNeeded);
/* 11885 */       Item inventory = responder.getInventory();
/* 11886 */       for (Item lNewCoin : newCoins) {
/* 11887 */         inventory.insertItem(lNewCoin);
/*       */       }
/*       */     } 
/* 11890 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   static final boolean watchSpyglass(Creature performer, Item spyglass, Action act, float counter) {
/* 11895 */     boolean done = false;
/*       */     
/* 11897 */     if (act.currentSecond() == 1) {
/*       */       
/* 11899 */       performer.getCommunicator().sendNormalServerMessage("You start spying through the " + spyglass.getName() + ".");
/* 11900 */       Server.getInstance().broadCastAction(performer
/* 11901 */           .getName() + " starts spying through " + spyglass.getNameWithGenus() + ".", performer, 5);
/* 11902 */       performer.sendActionControl(Actions.actionEntrys[329].getVerbString(), true, 300);
/*       */     }
/* 11904 */     else if (act.currentSecond() >= 30) {
/*       */       
/* 11906 */       done = true;
/* 11907 */       performer.getCommunicator().sendNormalServerMessage("You stop spying through the " + spyglass.getName() + ".");
/* 11908 */       Server.getInstance().broadCastAction(performer
/* 11909 */           .getName() + " stops spying through " + spyglass.getNameWithGenus() + ".", performer, 5);
/*       */     } 
/* 11911 */     return done;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static final boolean mayUseInventoryOfVehicle(Creature performer, Item vehicle) {
/* 11924 */     if (performer.getWurmId() == vehicle.lastOwner)
/* 11925 */       return true; 
/* 11926 */     return vehicle.mayAccessHold(performer);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static final boolean hasKeyForContainer(Creature performer, Item target) {
/*       */     try {
/* 11941 */       Item lock = Items.getItem(target.getLockId());
/*       */       
/* 11943 */       if (!lock.getLocked() || target.isOwner(performer))
/*       */       {
/* 11945 */         return true;
/*       */       }
/*       */ 
/*       */       
/* 11949 */       long[] keys = lock.getKeyIds();
/* 11950 */       for (long keyId : keys)
/*       */       {
/* 11952 */         Item key = Items.getItem(keyId);
/* 11953 */         if (key.getTopParent() == performer.getInventory().getWurmId())
/*       */         {
/* 11955 */           return true;
/*       */         }
/*       */       }
/*       */     
/*       */     }
/* 11960 */     catch (NoSuchItemException e) {
/*       */ 
/*       */       
/* 11963 */       logger.log(Level.WARNING, "Item not found for key " + e.getMessage(), (Throwable)e);
/*       */     } 
/* 11965 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean useBellTower(Creature performer, Item bellTower, Action act, float counter) {
/* 11970 */     boolean toReturn = false;
/* 11971 */     if (counter == 1.0F) {
/*       */       
/* 11973 */       performer.getCommunicator().sendNormalServerMessage("You start swinging the bell.");
/* 11974 */       Server.getInstance().broadCastAction(performer.getName() + " starts to swing the bell.", performer, 5);
/* 11975 */       performer.sendActionControl("playing", true, 600);
/* 11976 */       act.setTimeLeft(600);
/* 11977 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     }
/* 11979 */     else if (act.currentSecond() % 10 == 0) {
/*       */       
/* 11981 */       performer.getStatus().modifyStamina(-1000.0F);
/* 11982 */       String soundName = "sound.bell.dong.1";
/* 11983 */       int sound = (int)(bellTower.getCurrentQualityLevel() / 10.0F);
/* 11984 */       if (sound < 3) {
/* 11985 */         soundName = "sound.bell.dong.1";
/* 11986 */       } else if (sound < 5) {
/* 11987 */         soundName = "sound.bell.dong.2";
/* 11988 */       } else if (sound < 7) {
/* 11989 */         soundName = "sound.bell.dong.3";
/* 11990 */       } else if (sound < 8) {
/* 11991 */         soundName = "sound.bell.dong.4";
/*       */       } else {
/* 11993 */         soundName = "sound.bell.dong.5";
/*       */       } 
/* 11995 */       SoundPlayer.playSound(soundName, bellTower, 3.0F);
/* 11996 */       bellTower.setDamage(bellTower.getDamage() + 0.001F);
/*       */     } 
/* 11998 */     if (counter > 60.0F)
/*       */     {
/* 12000 */       toReturn = true;
/*       */     }
/* 12002 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean usePendulum(Creature performer, Item pendulum, Action act, float counter) {
/* 12007 */     boolean toReturn = false;
/* 12008 */     int time = 200;
/* 12009 */     Skills skills = performer.getSkills();
/* 12010 */     Skill primSkill = null;
/*       */     
/*       */     try {
/* 12013 */       primSkill = skills.getSkill(106);
/*       */     }
/* 12015 */     catch (Exception ex) {
/*       */       
/* 12017 */       primSkill = skills.learn(106, 1.0F);
/*       */     } 
/* 12019 */     if (counter == 1.0F) {
/*       */       
/* 12021 */       time = Actions.getStandardActionTime(performer, primSkill, pendulum, 0.0D);
/* 12022 */       act.setTimeLeft(time);
/* 12023 */       performer.getCommunicator().sendNormalServerMessage("You concentrate on the " + pendulum.getName() + ".");
/* 12024 */       Server.getInstance().broadCastAction(performer.getName() + " concentrates on the " + pendulum.getName() + ".", performer, 5);
/*       */       
/* 12026 */       performer.sendActionControl("Concentrating", true, time);
/* 12027 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     }
/*       */     else {
/*       */       
/* 12031 */       time = act.getTimeLeft();
/*       */     } 
/* 12033 */     if (act.currentSecond() == 5)
/*       */     {
/* 12035 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     }
/* 12037 */     if (counter * 10.0F > time) {
/*       */       
/* 12039 */       toReturn = true;
/* 12040 */       if (pendulum.isLocateItem()) {
/*       */         
/* 12042 */         Locates.useLocateItem(performer, pendulum, primSkill);
/*       */       } else {
/*       */         
/* 12045 */         Locates.locateSpring(performer, pendulum, primSkill);
/* 12046 */       }  pendulum.setDamage(pendulum.getDamage() + 0.01F);
/*       */     } 
/* 12048 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   static boolean puppetSpeak(Creature performerOne, Item puppetOne, Item puppetTwo, Action act, float counter) {
/*       */     Creature performerTwo;
/* 12054 */     if (puppetTwo == null) {
/*       */       
/* 12056 */       performerOne.getCommunicator().sendNormalServerMessage("The " + puppetOne
/* 12057 */           .getName() + " needs someone to speak with.");
/* 12058 */       return true;
/*       */     } 
/*       */     
/*       */     try {
/* 12062 */       puppetOne.getOwner();
/* 12063 */       puppetTwo.getOwner();
/* 12064 */       if (puppetOne.equals(puppetTwo))
/*       */       {
/* 12066 */         performerOne.getCommunicator().sendNormalServerMessage("The " + puppetOne
/* 12067 */             .getName() + " is not interested in speaking to itself.");
/* 12068 */         return true;
/*       */       }
/*       */     
/* 12071 */     } catch (NotOwnedException noe) {
/*       */       
/* 12073 */       performerOne.getCommunicator().sendNormalServerMessage("Both the " + puppetOne
/* 12074 */           .getName() + " and the " + puppetTwo.getName() + " need to be held.");
/* 12075 */       return true;
/*       */     } 
/*       */     
/* 12078 */     boolean toReturn = false;
/* 12079 */     int time = 0;
/*       */ 
/*       */     
/*       */     try {
/* 12083 */       performerTwo = Server.getInstance().getCreature(puppetTwo.getOwnerId());
/*       */     }
/* 12085 */     catch (NoSuchCreatureException nsc) {
/*       */       
/* 12087 */       performerOne.getCommunicator()
/* 12088 */         .sendNormalServerMessage("The " + puppetTwo.getName() + " must be played by someone.");
/* 12089 */       return true;
/*       */     }
/* 12091 */     catch (NoSuchPlayerException nsp) {
/*       */       
/* 12093 */       performerOne.getCommunicator()
/* 12094 */         .sendNormalServerMessage("The " + puppetTwo.getName() + " must be played by someone.");
/* 12095 */       return true;
/*       */     } 
/* 12097 */     if (performerOne.getPower() > 0 && performerOne.getPower() < 5 && Servers.localServer.testServer) {
/*       */ 
/*       */       
/* 12100 */       performerOne.getCommunicator().sendNormalServerMessage("Nothing happens.");
/* 12101 */       return true;
/*       */     } 
/* 12103 */     if (performerTwo.getPower() > 0 && performerTwo.getPower() < 5 && Servers.localServer.testServer) {
/*       */ 
/*       */       
/* 12106 */       performerOne.getCommunicator().sendNormalServerMessage("Nothing happens.");
/* 12107 */       return true;
/*       */     } 
/* 12109 */     int conversationCounter = 0;
/* 12110 */     if (counter == 1.0F) {
/*       */       
/* 12112 */       if (!puppetTwo.isPuppet()) {
/*       */         
/* 12114 */         performerOne.getCommunicator().sendNormalServerMessage("The " + puppetOne
/* 12115 */             .getName() + " does not want to speak to " + puppetTwo.getName() + ".");
/* 12116 */         return true;
/*       */       } 
/* 12118 */       if (!Puppet.mayPuppetMaster(performerOne)) {
/*       */         
/* 12120 */         performerOne.getCommunicator().sendNormalServerMessage("You are still mentally exhausted from your last show.");
/* 12121 */         return true;
/*       */       } 
/*       */       
/* 12124 */       int nums = Puppet.getConversationLength(true, act, puppetOne, puppetTwo, performerOne, performerTwo, 0);
/* 12125 */       int nums2 = Puppet.getConversationLength(true, act, puppetTwo, puppetOne, performerTwo, performerOne, 0);
/* 12126 */       int seconds = nums + nums2;
/*       */ 
/*       */       
/* 12129 */       time = (seconds + 2) * 5 * 10;
/*       */ 
/*       */       
/* 12132 */       act.setPower(conversationCounter);
/*       */       
/* 12134 */       if (performerTwo.equals(performerOne)) {
/*       */         
/* 12136 */         performerOne.getCommunicator().sendNormalServerMessage("You start playing with the " + puppetOne
/* 12137 */             .getName() + " and " + puppetTwo.getName() + ".");
/* 12138 */         Server.getInstance().broadCastAction(performerOne
/* 12139 */             .getName() + " starts playing with " + puppetOne.getNameWithGenus() + " " + puppetTwo
/* 12140 */             .getNameWithGenus() + ".", performerOne, 5);
/*       */       }
/*       */       else {
/*       */         
/* 12144 */         if (!Puppet.mayPuppetMaster(performerTwo)) {
/*       */           
/* 12146 */           performerTwo.getCommunicator().sendNormalServerMessage("You are still mentally exhausted from your last show.");
/*       */           
/* 12148 */           return true;
/*       */         } 
/* 12150 */         if (performerTwo.getPower() < 5) {
/* 12151 */           Puppet.startPuppeteering(performerTwo);
/*       */         }
/* 12153 */         performerTwo.sendActionControl("Puppeteering", true, time);
/* 12154 */         performerOne.getCommunicator().sendNormalServerMessage("You start playing with the " + puppetOne
/* 12155 */             .getName() + " and the " + puppetTwo.getName() + ".");
/* 12156 */         performerTwo.getCommunicator().sendNormalServerMessage(performerOne
/* 12157 */             .getName() + " starts playing with your " + puppetTwo.getName() + ".");
/* 12158 */         Server.getInstance().broadCastAction(performerOne
/* 12159 */             .getName() + " starts a show with " + performerTwo.getName() + ".", performerOne, performerTwo, 5);
/*       */       } 
/*       */       
/* 12162 */       if (performerOne.getPower() < 5)
/* 12163 */         Puppet.startPuppeteering(performerOne); 
/* 12164 */       performerOne.sendActionControl("Puppeteering", true, time);
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 12169 */       conversationCounter = (int)act.getPower();
/*       */     } 
/* 12171 */     if (act.currentSecond() % 5 == 0) {
/*       */       
/* 12173 */       performerOne.getStatus().modifyStamina(-500.0F);
/* 12174 */       performerTwo.getStatus().modifyStamina(-500.0F);
/*       */       
/* 12176 */       if (act.currentSecond() % 10 == 0) {
/*       */         
/* 12178 */         toReturn = Puppet.sendConversationString(act, puppetTwo, puppetOne, performerTwo, performerOne, conversationCounter);
/*       */         
/* 12180 */         if (toReturn) {
/*       */           
/* 12182 */           toReturn = false;
/* 12183 */           if (act.getTimeLeft() > 0) {
/*       */ 
/*       */ 
/*       */             
/* 12187 */             if (act.getTimeLeft() >= 100) {
/*       */               
/* 12189 */               toReturn = true;
/* 12190 */               act.setTimeLeft(125);
/*       */             } 
/*       */           } else {
/*       */             
/* 12194 */             act.setTimeLeft(25);
/*       */           } 
/* 12196 */         }  puppetTwo.setDamage(puppetTwo.getDamage() + puppetTwo.getDamageModifier() * 0.02F);
/*       */       }
/*       */       else {
/*       */         
/* 12200 */         toReturn = Puppet.sendConversationString(act, puppetOne, puppetTwo, performerOne, performerTwo, conversationCounter);
/*       */         
/* 12202 */         if (toReturn) {
/*       */           
/* 12204 */           toReturn = false;
/* 12205 */           if (act.getTimeLeft() > 0) {
/*       */ 
/*       */ 
/*       */             
/* 12209 */             if (act.getTimeLeft() < 100)
/*       */             {
/* 12211 */               toReturn = true;
/* 12212 */               act.setTimeLeft(125);
/*       */             }
/*       */           
/*       */           } else {
/*       */             
/* 12217 */             act.setTimeLeft(100);
/*       */           } 
/* 12219 */         }  puppetOne.setDamage(puppetOne.getDamage() + puppetOne.getDamageModifier() * 0.02F);
/*       */       } 
/* 12221 */       if (act.getFailSecond() == 1.0F || act.currentSecond() % 10 == 0 || puppetOne
/* 12222 */         .getTemplateId() == puppetTwo.getTemplateId()) {
/*       */ 
/*       */         
/* 12225 */         act.setFailSecond(10.0F);
/*       */         
/* 12227 */         conversationCounter++;
/*       */       } 
/*       */ 
/*       */       
/* 12231 */       act.setPower(conversationCounter);
/*       */       
/* 12233 */       if (toReturn)
/*       */       {
/* 12235 */         if (!performerTwo.equals(performerOne))
/* 12236 */           performerTwo.sendActionControl("Puppeteering", false, 0); 
/*       */       }
/*       */     } 
/* 12239 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static String getColorDesc(int color) {
/* 12251 */     return " Colors: R=" + WurmColor.getColorRed(color) + ", G=" + WurmColor.getColorGreen(color) + ", B=" + 
/* 12252 */       WurmColor.getColorBlue(color) + ".";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static String getImpDesc(@Nonnull Creature performer, @Nonnull Item item) {
/* 12265 */     String toReturn = "";
/* 12266 */     if (performer.getPlayingTime() < 604800000L) {
/* 12267 */       toReturn = " It can not be improved.";
/*       */     }
/* 12269 */     if (item.isRepairable() && item.creationState == 0 && !item.isNoImprove()) {
/*       */       
/* 12271 */       int skillNum = getImproveSkill(item);
/* 12272 */       int templateId = getImproveTemplateId(item);
/* 12273 */       if (skillNum != -10) {
/*       */         
/*       */         try {
/*       */           
/* 12277 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/* 12278 */           toReturn = " It could be improved with " + temp.getNameWithGenus() + ".";
/*       */         }
/* 12280 */         catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */ 
/*       */       
/*       */       }
/*       */     
/*       */     }
/* 12286 */     else if (item.creationState != 0) {
/*       */       
/* 12288 */       toReturn = ' ' + getNeededCreationAction(getImproveMaterial(item), item.creationState, item);
/* 12289 */     }  return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public static List<String> getEnhancementStrings(Item item) {
/* 12294 */     List<String> strings = new ArrayList<>();
/* 12295 */     if (item.enchantment != 0) {
/*       */       
/* 12297 */       Spell ench = Spells.getEnchantment(item.enchantment);
/* 12298 */       if (ench != null)
/* 12299 */         strings.add("It is enchanted with " + ench.name + ", and " + ench.effectdesc); 
/*       */     } 
/* 12301 */     ItemSpellEffects eff = item.getSpellEffects();
/* 12302 */     if (eff != null) {
/*       */       
/* 12304 */       SpellEffect[] speffs = eff.getEffects();
/* 12305 */       for (int x = 0; x < speffs.length; x++) {
/*       */         
/* 12307 */         if ((speffs[x]).type < -10L) {
/* 12308 */           strings.add("A single " + speffs[x].getName() + " has been attached to it, so it " + speffs[x].getLongDesc());
/*       */         } else {
/* 12310 */           strings.add(speffs[x].getName() + " has been cast on it, so it " + speffs[x].getLongDesc() + " [" + (int)(speffs[x]).power + "]");
/*       */         } 
/*       */       } 
/*       */     } 
/* 12314 */     return strings;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final void sendEnchantmentStrings(Item item, Creature creature) {
/* 12319 */     if (creature == null) {
/*       */       return;
/*       */     }
/* 12322 */     if (item == null) {
/*       */       return;
/*       */     }
/* 12325 */     for (String s : getEnhancementStrings(item))
/*       */     {
/* 12327 */       creature.getCommunicator().sendNormalServerMessage(s);
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static byte getTransmutationLiquidAuxByteFor(Item source, Item target) {
/* 12350 */     if (source.getTemplateId() == 417) {
/*       */       
/* 12352 */       if (Features.Feature.TRANSFORM_TO_RESOURCE_TILES.isEnabled()) {
/*       */         
/* 12354 */         if (target.getTemplateId() == 220 && source.getRealTemplateId() == 6)
/* 12355 */           return 1; 
/* 12356 */         if (target.getTemplateId() == 204 && source.getRealTemplateId() == 410)
/* 12357 */           return 2; 
/* 12358 */         if (target.getTemplateId() == 46 && source.getRealTemplateId() == 409)
/* 12359 */           return 3; 
/* 12360 */         if (target.getTemplateId() == 47 && source.getRealTemplateId() == 1283)
/* 12361 */           return 7; 
/*       */       } 
/* 12363 */       if (target.getTemplateId() == 48 && source.getRealTemplateId() == 410)
/* 12364 */         return 4; 
/* 12365 */       if (target.getTemplateId() == 479 && source.getRealTemplateId() == 409)
/* 12366 */         return 5; 
/* 12367 */       if (target.getTemplateId() == 49 && source.getRealTemplateId() == 6)
/* 12368 */         return 6; 
/* 12369 */       if (target.getTemplateId() == 46 && source.getRealTemplateId() == 1196)
/* 12370 */         return 8; 
/*       */     } 
/* 12372 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public static String getTransmutationLiquidDescription(byte auxData) {
/* 12377 */     if (Features.Feature.TRANSFORM_TO_RESOURCE_TILES.isEnabled())
/*       */     {
/* 12379 */       switch (auxData) {
/*       */         
/*       */         case 1:
/* 12382 */           return "Transmutes sand to clay";
/*       */         case 2:
/* 12384 */           return "Transmutes grass or mycelium to peat";
/*       */         case 3:
/* 12386 */           return "Transmutes steppe to tar";
/*       */         case 7:
/* 12388 */           return "Transmutes moss to tundra";
/*       */       } 
/*       */     }
/* 12391 */     switch (auxData) {
/*       */       
/*       */       case 4:
/* 12394 */         return "Transmutes clay to dirt";
/*       */       case 5:
/* 12396 */         return "Transmutes peat to dirt";
/*       */       case 6:
/* 12398 */         return "Transmutes tar to dirt";
/*       */       case 8:
/* 12400 */         return "Transmutes tundra to dirt";
/*       */     } 
/*       */     
/* 12403 */     return "";
/*       */   }
/*       */ 
/*       */   
/*       */   public static int getTransmutationLiquidDifficulty(byte auxData) {
/* 12408 */     switch (auxData) {
/*       */       
/*       */       case 1:
/* 12411 */         return 30;
/*       */       case 2:
/* 12413 */         return 20;
/*       */       case 3:
/* 12415 */         return 10;
/*       */       case 4:
/* 12417 */         return 35;
/*       */       case 5:
/* 12419 */         return 25;
/*       */       case 6:
/* 12421 */         return 15;
/*       */       case 7:
/* 12423 */         return 5;
/*       */       case 8:
/* 12425 */         return 10;
/*       */     } 
/* 12427 */     return 1;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   static int getTransmutationSolidTemplateWeightGrams(byte auxData) {
/* 12433 */     int templateId = 46;
/* 12434 */     switch (auxData) {
/*       */       
/*       */       case 1:
/* 12437 */         templateId = 220;
/*       */         break;
/*       */       case 2:
/* 12440 */         templateId = 204;
/*       */         break;
/*       */       case 3:
/* 12443 */         templateId = 46;
/*       */         break;
/*       */       case 4:
/* 12446 */         templateId = 48;
/*       */         break;
/*       */       case 5:
/* 12449 */         templateId = 479;
/*       */         break;
/*       */       case 6:
/* 12452 */         templateId = 49;
/*       */         break;
/*       */       case 7:
/* 12455 */         templateId = 47;
/*       */         break;
/*       */       case 8:
/* 12458 */         templateId = 46;
/*       */         break;
/*       */       default:
/* 12461 */         templateId = 46;
/*       */         break;
/*       */     } 
/*       */     
/*       */     try {
/* 12466 */       ItemTemplate it = ItemTemplateFactory.getInstance().getTemplate(templateId);
/* 12467 */       return it.getWeightGrams();
/*       */     }
/* 12469 */     catch (NoSuchTemplateException e) {
/*       */       
/* 12471 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       
/* 12473 */       return 1000;
/*       */     } 
/*       */   }
/*       */   
/*       */   static byte getTransmutedToTileType(byte auxData) {
/* 12478 */     switch (auxData) {
/*       */       
/*       */       case 1:
/* 12481 */         return Tiles.Tile.TILE_CLAY.id;
/*       */       case 2:
/* 12483 */         return Tiles.Tile.TILE_PEAT.id;
/*       */       case 3:
/* 12485 */         return Tiles.Tile.TILE_TAR.id;
/*       */       case 4:
/* 12487 */         return Tiles.Tile.TILE_DIRT.id;
/*       */       case 5:
/* 12489 */         return Tiles.Tile.TILE_DIRT.id;
/*       */       case 6:
/* 12491 */         return Tiles.Tile.TILE_DIRT.id;
/*       */       case 7:
/* 12493 */         return Tiles.Tile.TILE_TUNDRA.id;
/*       */       case 8:
/* 12495 */         return Tiles.Tile.TILE_DIRT.id;
/*       */     } 
/* 12497 */     return Tiles.Tile.TILE_DIRT.id;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   static float getTransmutationMod(Creature performer, int tilex, int tiley, byte auxData, boolean reverting) {
/* 12504 */     float mod = 1.0F;
/* 12505 */     switch (auxData) {
/*       */       
/*       */       case 1:
/* 12508 */         if (Servers.isThisAPvpServer())
/* 12509 */           mod *= 1.5F; 
/*       */         break;
/*       */       case 2:
/* 12512 */         if (Servers.isThisAPvpServer())
/* 12513 */           mod *= 1.5F; 
/*       */         break;
/*       */       case 3:
/* 12516 */         if (Servers.isThisAPvpServer())
/* 12517 */           mod *= 1.5F; 
/*       */         break;
/*       */       case 4:
/* 12520 */         if (reverting)
/* 12521 */           mod /= 2.0F; 
/*       */         break;
/*       */       case 5:
/* 12524 */         if (reverting)
/* 12525 */           mod /= 2.0F; 
/*       */         break;
/*       */       case 6:
/* 12528 */         if (reverting)
/* 12529 */           mod /= 2.0F; 
/*       */         break;
/*       */       case 7:
/* 12532 */         mod /= 5.0F;
/* 12533 */         if (reverting)
/* 12534 */           mod /= 2.0F; 
/*       */         break;
/*       */       case 8:
/* 12537 */         mod /= 5.0F;
/* 12538 */         if (Servers.isThisAPvpServer()) {
/* 12539 */           mod *= 1.5F;
/*       */         }
/*       */         break;
/*       */     } 
/* 12543 */     Village vp = Villages.getVillageWithPerimeterAt(tilex, tiley, true);
/* 12544 */     if (vp != null && (vp.isCitizen(performer) || vp.isAlly(performer) || vp.isEnemy(performer)))
/* 12545 */       mod /= 1.5F; 
/* 12546 */     return mod;
/*       */   }
/*       */ 
/*       */   
/*       */   public static boolean wouldDestroyLiquid(Item container, Item contained, Item target) {
/* 12551 */     if (container.getTemplate().isContainerWithSubItems() && contained.isPlacedOnParent())
/* 12552 */       return false; 
/* 12553 */     if (!contained.isFood() && !contained.isLiquid() && !contained.isRecipeItem())
/* 12554 */       return true; 
/* 12555 */     if ((contained.isFood() || contained.isRecipeItem()) && !contained.isLiquid())
/* 12556 */       return false; 
/* 12557 */     ItemTemplate ct = contained.isBulkItem() ? contained.getRealTemplate() : contained.getTemplate();
/* 12558 */     int cid = ct.getTemplateId();
/*       */     
/* 12560 */     ItemTemplate rt = target.isBulkItem() ? target.getRealTemplate() : target.getTemplate();
/* 12561 */     int tid = rt.getTemplateId();
/* 12562 */     if (rt.isFood() && !rt.isLiquid())
/* 12563 */       return false; 
/* 12564 */     if (container.isFoodMaker())
/*       */     {
/* 12566 */       if (container.getTemplateId() != 768 || !rt.isLiquid() || !contained.isLiquid())
/*       */       {
/*       */ 
/*       */         
/* 12570 */         if (contained.getTemplate().isLiquidCooking() && rt.isLiquidCooking())
/*       */         {
/* 12572 */           if (container.getTemplateId() != 76) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/* 12578 */             if (container.getTemplateId() != 75) {
/* 12579 */               return false;
/*       */             }
/* 12581 */             if (rt.getFoodGroup() == 1263 && cid == tid) {
/* 12582 */               return false;
/*       */             }
/* 12584 */             if (tid == 1212 || cid == 1212)
/* 12585 */               return false; 
/*       */           } 
/*       */         }
/*       */       }
/*       */     }
/* 12590 */     if (cid != tid)
/* 12591 */       return true; 
/* 12592 */     if (contained.getAuxData() != target.getAuxData())
/* 12593 */       return true; 
/* 12594 */     if (contained.getBless() != target.getBless())
/* 12595 */       return true; 
/* 12596 */     if (contained.getRarity() != target.getRarity()) {
/* 12597 */       return true;
/*       */     }
/* 12599 */     if (!target.isBulkItem() && contained.getRealTemplateId() != target.getRealTemplateId())
/* 12600 */       return true; 
/* 12601 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static void setSizes(@Nullable Item container, int newWeight, Item item) {
/* 12612 */     float mod = newWeight / item.getWeightGrams();
/* 12613 */     int maxSizeMod = 4;
/* 12614 */     ItemTemplate template = item.getTemplate();
/* 12615 */     int sizeX = 0;
/* 12616 */     int sizeY = 0;
/* 12617 */     int sizeZ = 0;
/* 12618 */     if (mod > 64.0F) {
/*       */       
/* 12620 */       sizeX = template.getSizeX() * 4;
/* 12621 */       sizeY = template.getSizeY() * 4;
/* 12622 */       sizeZ = template.getSizeZ() * 4;
/*       */     }
/* 12624 */     else if (mod > 16.0F) {
/*       */       
/* 12626 */       mod = mod / 4.0F * 4.0F;
/* 12627 */       sizeX = (int)(template.getSizeX() * mod);
/* 12628 */       sizeY = template.getSizeY() * 4;
/* 12629 */       sizeZ = template.getSizeZ() * 4;
/*       */     }
/* 12631 */     else if (mod > 4.0F) {
/*       */       
/* 12633 */       sizeX = template.getSizeX();
/* 12634 */       sizeY = (int)(template.getSizeY() * mod);
/* 12635 */       sizeZ = template.getSizeZ() * 4;
/* 12636 */       mod /= 4.0F;
/*       */     }
/*       */     else {
/*       */       
/* 12640 */       sizeX = Math.max(1, (int)(template.getSizeX() * mod));
/* 12641 */       sizeY = Math.max(1, (int)(template.getSizeY() * mod));
/* 12642 */       sizeZ = Math.max(1, (int)(template.getSizeZ() * mod));
/*       */     } 
/* 12644 */     if (container != null) {
/*       */       
/* 12646 */       sizeX = Math.min(sizeX, container.getSizeX());
/* 12647 */       sizeY = Math.min(sizeY, container.getSizeY());
/* 12648 */       sizeZ = Math.min(sizeZ, container.getSizeZ());
/*       */     } 
/* 12650 */     item.setSizes(sizeX, sizeY, sizeZ);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static Point4f getHivePos(int tilex, int tiley, Tiles.Tile theTile, byte data) {
/* 12662 */     byte age = FoliageAge.getAgeAsByte(data);
/* 12663 */     float treex = 2.0F;
/* 12664 */     float treey = 2.0F;
/*       */     
/* 12666 */     if (!TreeData.isCentre(data)) {
/*       */ 
/*       */       
/* 12669 */       treex = TerrainUtilities.getTreePosX(tilex, tiley) * 4.0F;
/* 12670 */       treey = TerrainUtilities.getTreePosY(tilex, tiley) * 4.0F;
/*       */     } 
/* 12672 */     float posx = (tilex * 4) + treex;
/* 12673 */     float posy = (tiley * 4) + treey;
/* 12674 */     float dir = Creature.normalizeAngle(TerrainUtilities.getTreeRotation(tilex, tiley));
/*       */ 
/*       */     
/*       */     try {
/* 12678 */       float basePosZ = Zones.calculateHeight(posx, posy, true);
/* 12679 */       float tht = theTile.getTreeImageHeight(data);
/* 12680 */       float posz = basePosZ + tht * (age + 1) * 0.4F;
/* 12681 */       return new Point4f(posx, posy, posz, dir);
/*       */     }
/* 12683 */     catch (NoSuchZoneException e) {
/*       */ 
/*       */       
/* 12686 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       
/* 12688 */       return new Point4f(posx, posy, 0.0F, dir);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static float getAverageQL(@Nullable Item source, Item target) {
/* 12699 */     int count = 0;
/* 12700 */     float totalQL = 0.0F;
/* 12701 */     if (source != null)
/*       */     {
/* 12703 */       if (!source.isCookingTool() && !source.isRecipeItem()) {
/*       */         
/* 12705 */         count++;
/* 12706 */         float rarityMult = 1.0F + (source.getRarity() * source.getRarity()) * 0.1F;
/* 12707 */         totalQL += source.getCurrentQualityLevel() * rarityMult;
/*       */       } 
/*       */     }
/* 12710 */     if (target.isFoodMaker() || target.getTemplate().isCooker()) {
/*       */       
/* 12712 */       Item[] items = target.getItemsAsArray();
/* 12713 */       for (Item item : items) {
/*       */         
/* 12715 */         count++;
/* 12716 */         float f = 1.0F + (item.getRarity() * item.getRarity()) * 0.1F;
/* 12717 */         totalQL += item.getCurrentQualityLevel() * f;
/*       */       } 
/*       */       
/* 12720 */       float rarityMult = 1.0F + (target.getRarity() * target.getRarity()) * 0.1F;
/* 12721 */       totalQL += target.getCurrentQualityLevel() * rarityMult / Math.max(1, 11 - items.length);
/*       */     }
/*       */     else {
/*       */       
/* 12725 */       count++;
/* 12726 */       float rarityMult = 1.0F + (target.getRarity() * target.getRarity()) * 0.1F;
/* 12727 */       totalQL += target.getCurrentQualityLevel() * rarityMult;
/*       */     } 
/*       */     
/* 12730 */     if (count > 0)
/* 12731 */       return Math.min(99.995F, Math.max(1.0F, totalQL / count)); 
/* 12732 */     return 1.0F;
/*       */   }
/*       */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MethodsItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */