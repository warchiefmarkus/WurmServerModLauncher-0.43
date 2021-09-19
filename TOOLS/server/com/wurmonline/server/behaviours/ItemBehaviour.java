/*       */ package com.wurmonline.server.behaviours;
/*       */ import com.wurmonline.mesh.Tiles;
/*       */ import com.wurmonline.server.Constants;
/*       */ import com.wurmonline.server.FailedException;
/*       */ import com.wurmonline.server.Features;
/*       */ import com.wurmonline.server.GeneralUtilities;
/*       */ import com.wurmonline.server.Items;
/*       */ import com.wurmonline.server.LoginHandler;
/*       */ import com.wurmonline.server.MiscConstants;
/*       */ import com.wurmonline.server.NoSuchEntryException;
/*       */ import com.wurmonline.server.NoSuchItemException;
/*       */ import com.wurmonline.server.NoSuchPlayerException;
/*       */ import com.wurmonline.server.Players;
/*       */ import com.wurmonline.server.Server;
/*       */ import com.wurmonline.server.ServerEntry;
/*       */ import com.wurmonline.server.Servers;
/*       */ import com.wurmonline.server.TimeConstants;
/*       */ import com.wurmonline.server.WurmCalendar;
/*       */ import com.wurmonline.server.WurmId;
/*       */ import com.wurmonline.server.combat.Archery;
/*       */ import com.wurmonline.server.combat.CombatEngine;
/*       */ import com.wurmonline.server.creatures.Brand;
/*       */ import com.wurmonline.server.creatures.Communicator;
/*       */ import com.wurmonline.server.creatures.Creature;
/*       */ import com.wurmonline.server.creatures.Creatures;
/*       */ import com.wurmonline.server.creatures.Delivery;
/*       */ import com.wurmonline.server.creatures.MovementScheme;
/*       */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*       */ import com.wurmonline.server.creatures.Traits;
/*       */ import com.wurmonline.server.creatures.Wagoner;
/*       */ import com.wurmonline.server.deities.Deity;
/*       */ import com.wurmonline.server.economy.Economy;
/*       */ import com.wurmonline.server.economy.MonetaryConstants;
/*       */ import com.wurmonline.server.effects.Effect;
/*       */ import com.wurmonline.server.effects.EffectFactory;
/*       */ import com.wurmonline.server.endgames.EndGameItem;
/*       */ import com.wurmonline.server.endgames.EndGameItems;
/*       */ import com.wurmonline.server.epic.MissionHelper;
/*       */ import com.wurmonline.server.highways.MethodsHighways;
/*       */ import com.wurmonline.server.items.CreationEntry;
/*       */ import com.wurmonline.server.items.CreationMatrix;
/*       */ import com.wurmonline.server.items.CreationRequirement;
/*       */ import com.wurmonline.server.items.FragmentUtilities;
/*       */ import com.wurmonline.server.items.Ingredient;
/*       */ import com.wurmonline.server.items.InscriptionData;
/*       */ import com.wurmonline.server.items.Item;
/*       */ import com.wurmonline.server.items.ItemFactory;
/*       */ import com.wurmonline.server.items.ItemMealData;
/*       */ import com.wurmonline.server.items.ItemSpellEffects;
/*       */ import com.wurmonline.server.items.ItemTemplate;
/*       */ import com.wurmonline.server.items.ItemTemplateFactory;
/*       */ import com.wurmonline.server.items.ItemTypes;
/*       */ import com.wurmonline.server.items.Materials;
/*       */ import com.wurmonline.server.items.NoSuchTemplateException;
/*       */ import com.wurmonline.server.items.NotOwnedException;
/*       */ import com.wurmonline.server.items.Recipe;
/*       */ import com.wurmonline.server.items.Recipes;
/*       */ import com.wurmonline.server.items.RecipesByPlayer;
/*       */ import com.wurmonline.server.items.RuneUtilities;
/*       */ import com.wurmonline.server.items.WurmColor;
/*       */ import com.wurmonline.server.kingdom.GuardTower;
/*       */ import com.wurmonline.server.kingdom.King;
/*       */ import com.wurmonline.server.kingdom.Kingdoms;
/*       */ import com.wurmonline.server.players.Abilities;
/*       */ import com.wurmonline.server.players.Achievements;
/*       */ import com.wurmonline.server.players.Cultist;
/*       */ import com.wurmonline.server.players.Cults;
/*       */ import com.wurmonline.server.players.Permissions;
/*       */ import com.wurmonline.server.players.PermissionsPlayerList;
/*       */ import com.wurmonline.server.players.Player;
/*       */ import com.wurmonline.server.players.PlayerInfo;
/*       */ import com.wurmonline.server.players.PlayerInfoFactory;
/*       */ import com.wurmonline.server.questions.AbdicationQuestion;
/*       */ import com.wurmonline.server.questions.AchievementCreation;
/*       */ import com.wurmonline.server.questions.AffinityQuestion;
/*       */ import com.wurmonline.server.questions.AppointmentsQuestion;
/*       */ import com.wurmonline.server.questions.ChangeAppearanceQuestion;
/*       */ import com.wurmonline.server.questions.ChangeMedPathQuestion;
/*       */ import com.wurmonline.server.questions.ChangeNameQuestion;
/*       */ import com.wurmonline.server.questions.ConchQuestion;
/*       */ import com.wurmonline.server.questions.CultQuestion;
/*       */ import com.wurmonline.server.questions.FeatureManagement;
/*       */ import com.wurmonline.server.questions.GmInterface;
/*       */ import com.wurmonline.server.questions.GmVillageAdInterface;
/*       */ import com.wurmonline.server.questions.GroupCAHelpQuestion;
/*       */ import com.wurmonline.server.questions.InGameVoteSetupQuestion;
/*       */ import com.wurmonline.server.questions.KingdomFoundationQuestion;
/*       */ import com.wurmonline.server.questions.KingdomHistory;
/*       */ import com.wurmonline.server.questions.KingdomMembersQuestion;
/*       */ import com.wurmonline.server.questions.KingdomStatusQuestion;
/*       */ import com.wurmonline.server.questions.LCMManagementQuestion;
/*       */ import com.wurmonline.server.questions.ManageObjectList;
/*       */ import com.wurmonline.server.questions.ManagePermissions;
/*       */ import com.wurmonline.server.questions.MissionManager;
/*       */ import com.wurmonline.server.questions.PermissionsHistory;
/*       */ import com.wurmonline.server.questions.PortalQuestion;
/*       */ import com.wurmonline.server.questions.QuestionTypes;
/*       */ import com.wurmonline.server.questions.SinglePriceManageQuestion;
/*       */ import com.wurmonline.server.questions.SwapDeityQuestion;
/*       */ import com.wurmonline.server.questions.TextInputQuestion;
/*       */ import com.wurmonline.server.questions.VillageMessageBoard;
/*       */ import com.wurmonline.server.questions.VillageMessagePopup;
/*       */ import com.wurmonline.server.questions.WishQuestion;
/*       */ import com.wurmonline.server.skills.AffinitiesTimed;
/*       */ import com.wurmonline.server.skills.NoSuchSkillException;
/*       */ import com.wurmonline.server.skills.Skill;
/*       */ import com.wurmonline.server.skills.SkillSystem;
/*       */ import com.wurmonline.server.skills.SkillTemplate;
/*       */ import com.wurmonline.server.skills.Skills;
/*       */ import com.wurmonline.server.sounds.SoundPlayer;
/*       */ import com.wurmonline.server.spells.Spell;
/*       */ import com.wurmonline.server.spells.SpellEffect;
/*       */ import com.wurmonline.server.spells.Spells;
/*       */ import com.wurmonline.server.statistics.ChallengePointEnum;
/*       */ import com.wurmonline.server.structures.Blocking;
/*       */ import com.wurmonline.server.structures.BlockingResult;
/*       */ import com.wurmonline.server.structures.Structure;
/*       */ import com.wurmonline.server.tutorial.MissionTrigger;
/*       */ import com.wurmonline.server.tutorial.MissionTriggers;
/*       */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*       */ import com.wurmonline.server.utils.NameCountList;
/*       */ import com.wurmonline.server.utils.StringUtil;
/*       */ import com.wurmonline.server.villages.Citizen;
/*       */ import com.wurmonline.server.villages.DeadVillage;
/*       */ import com.wurmonline.server.villages.NoSuchVillageException;
/*       */ import com.wurmonline.server.villages.RecruitmentAd;
/*       */ import com.wurmonline.server.villages.RecruitmentAds;
/*       */ import com.wurmonline.server.villages.Village;
/*       */ import com.wurmonline.server.villages.Villages;
/*       */ import com.wurmonline.server.zones.NoSuchZoneException;
/*       */ import com.wurmonline.server.zones.VolaTile;
/*       */ import com.wurmonline.server.zones.Zone;
/*       */ import com.wurmonline.server.zones.Zones;
/*       */ import com.wurmonline.shared.constants.ItemMaterials;
/*       */ import com.wurmonline.shared.exceptions.WurmServerException;
/*       */ import com.wurmonline.shared.util.MaterialUtilities;
/*       */ import com.wurmonline.shared.util.StringUtilities;
/*       */ import java.io.IOException;
/*       */ import java.util.ArrayList;
/*       */ import java.util.Collections;
/*       */ import java.util.HashMap;
/*       */ import java.util.Iterator;
/*       */ import java.util.LinkedList;
/*       */ import java.util.List;
/*       */ import java.util.Map;
/*       */ import java.util.Random;
/*       */ import java.util.Set;
/*       */ import java.util.concurrent.ConcurrentHashMap;
/*       */ import java.util.logging.Level;
/*       */ import java.util.logging.Logger;
/*       */ import javax.annotation.Nullable;
/*       */ 
/*       */ public class ItemBehaviour extends Behaviour implements ItemTypes, MiscConstants, QuestionTypes, TimeConstants, MonetaryConstants, ItemMaterials {
/*   154 */   private static final Logger logger = Logger.getLogger(ItemBehaviour.class.getName());
/*   155 */   public static final Map<Long, Long> conquers = new ConcurrentHashMap<>();
/*   156 */   private static final Random recipeRandom = new Random();
/*       */ 
/*       */   
/*   159 */   private final float gemChance = 1.0F;
/*   160 */   private final float barChance = 10.0F;
/*   161 */   private final float imbuePotionChance = 2.0F;
/*   162 */   private final float supremeBoneChance = 0.1F;
/*   163 */   private final float transRodChance = 0.05F;
/*   164 */   private final float staffLandChance = 0.01F;
/*   165 */   private final float paleMaskChance = 0.01F;
/*   166 */   private final float plumedHelmChance = 0.025F;
/*   167 */   private final float cavalierHelmChance = 0.1F;
/*   168 */   private final float challMaskChance = 0.1F;
/*   169 */   private final float challStatueChance = 0.5F;
/*   170 */   private final float shatterProtPotionChance = 1.0F;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   ItemBehaviour() {
/*   177 */     super((short)1);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   ItemBehaviour(short type) {
/*   187 */     super(type);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*   198 */     List<ActionEntry> toReturn = new LinkedList<>();
/*   199 */     toReturn.addAll(super.getBehavioursFor(performer, target));
/*       */     
/*   201 */     int tid = target.getTemplateId();
/*       */     
/*   203 */     if (!target.isTraded()) {
/*       */ 
/*       */       
/*   206 */       if (target.canBePlanted()) {
/*       */         
/*   208 */         long ownerId = target.getOwnerId();
/*   209 */         if (ownerId == -10L) {
/*       */           
/*   211 */           if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*       */             
/*   213 */             boolean isPvpServer = Servers.isThisAPvpServer();
/*   214 */             if (MethodsItems.checkIfStealing(target, performer, null)) {
/*       */               
/*   216 */               if (isPvpServer) {
/*   217 */                 toReturn.add(Actions.actionEntrys[100]);
/*       */               }
/*   219 */             } else if (target.isKingdomFlag() && target.getAuxData() != performer.getKingdomId()) {
/*       */               
/*   221 */               if (isSignManipulationOk(target, performer, (short)6)) {
/*   222 */                 toReturn.add(Actions.actionEntrys[6]);
/*   223 */               } else if (isPvpServer) {
/*   224 */                 toReturn.add(Actions.actionEntrys[100]);
/*       */               } 
/*   226 */             } else if ((!target.isBulkContainer() && target.getTemplate().getInitialContainers() == null) || target
/*   227 */               .isEmpty(true)) {
/*       */               
/*   229 */               if (!target.isNoTake(performer))
/*   230 */                 if (isSignManipulationOk(target, performer, (short)6)) {
/*       */                   
/*   232 */                   toReturn.add(Actions.actionEntrys[6]);
/*   233 */                   if ((target.getParentId() == -10L && tid != 26) || tid != 298) {
/*   234 */                     toReturn.add(Actions.actionEntrys[925]);
/*       */                   }
/*   236 */                 } else if (isPvpServer) {
/*   237 */                   toReturn.add(Actions.actionEntrys[100]);
/*       */                 }  
/*   239 */             }  if (!target.isPlanted() && target.getParentId() == -10L && !target.isRoadMarker()) {
/*   240 */               toReturn.add(new ActionEntry((short)176, "Secure", "securing the " + target.getName()));
/*       */             }
/*       */           } 
/*   243 */         } else if (ownerId == performer.getWurmId() && !target.isRoadMarker()) {
/*       */           
/*   245 */           if (target.getTemplateId() == 1396) {
/*       */             int placedTile;
/*       */ 
/*       */             
/*   249 */             if (performer.isOnSurface()) {
/*   250 */               placedTile = Server.surfaceMesh.getTile(performer.getTileX(), performer.getTileY());
/*       */             } else {
/*   252 */               placedTile = Server.caveMesh.getTile(performer.getTileX(), performer.getTileY());
/*   253 */             }  if (Tiles.decodeHeight(placedTile) < 0)
/*       */             {
/*   255 */               toReturn.add(Actions.actionEntrys[176]);
/*       */             }
/*       */           }
/*   258 */           else if (!target.isAbility()) {
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*   263 */             toReturn.add(Actions.actionEntrys[176]);
/*       */           } 
/*       */         } 
/*   266 */         if (target.getTemplateId() == 835) {
/*       */           
/*   268 */           Village village = Villages.getVillageForCreature(performer);
/*   269 */           if (village != null) {
/*       */             
/*   271 */             if (!RecruitmentAds.containsAdForVillage(village.getId())) {
/*       */               
/*   273 */               Citizen cit = village.getCitizen(performer.getWurmId());
/*   274 */               if (cit != null && cit.getRole().mayInviteCitizens())
/*       */               {
/*   276 */                 toReturn.add(Actions.actionEntrys[598]);
/*       */               }
/*       */             }
/*       */             else {
/*       */               
/*   281 */               Citizen cit = village.getCitizen(performer.getWurmId());
/*   282 */               if (cit != null && cit.getRole().mayInviteCitizens())
/*       */               {
/*   284 */                 toReturn.add(Actions.actionEntrys[602]);
/*   285 */                 toReturn.add(Actions.actionEntrys[603]);
/*       */               }
/*       */             
/*       */             } 
/*       */           } else {
/*       */             
/*   291 */             toReturn.add(Actions.actionEntrys[601]);
/*       */           } 
/*       */         } 
/*   294 */         if (target.getTemplateId() == 1271)
/*       */         {
/*   296 */           if (target.mayAccessHold(performer)) {
/*   297 */             toReturn.add(new ActionEntry((short)17, "Read messages", "reading messages"));
/*       */           }
/*       */         }
/*       */       } 
/*       */       
/*       */       try {
/*   303 */         if (target.getOwner() == performer.getWurmId())
/*       */         {
/*   305 */           if (target.isLight() || target.isFire())
/*       */           {
/*   307 */             if (target.isOnFire() && !target.isAlwaysLit())
/*       */             {
/*   309 */               if (target.getTemplateId() == 729) {
/*   310 */                 toReturn.add(new ActionEntry((short)53, "Blow out", "blowing out", new int[] { 0, 43 }));
/*       */               } else {
/*       */                 
/*   313 */                 toReturn.add(Actions.actionEntrys[53]);
/*       */               } 
/*       */             }
/*       */           }
/*       */           try {
/*   318 */             Item p = Items.getItem(target.getTopParent());
/*   319 */             if (p != null)
/*       */             {
/*   321 */               if (target.isHollow() && !target.isSealedByPlayer() && target.getTemplateId() != 1342 && (target
/*   322 */                 .getTopParent() == performer.getInventory().getWurmId() || p.isBodyPart())) {
/*       */                 
/*   324 */                 long lockId = target.getLockId();
/*   325 */                 if (lockId == -10L) {
/*       */                   
/*   327 */                   toReturn.add(Actions.actionEntrys[568]);
/*       */                 } else {
/*       */ 
/*       */                   
/*       */                   try {
/*       */                     
/*   333 */                     Item lock = Items.getItem(lockId);
/*   334 */                     if (performer.hasKeyForLock(lock) || target.isOwner(performer)) {
/*       */                       
/*   336 */                       toReturn.add(Actions.actionEntrys[568]);
/*   337 */                       if (target.isOwner(performer))
/*       */                       {
/*   339 */                         toReturn.add(new ActionEntry((short)-1, LoginHandler.raiseFirstLetter(target.getActualName()), target.getActualName()));
/*   340 */                         toReturn.add(Actions.actionEntrys[102]);
/*       */                       }
/*       */                     
/*   343 */                     } else if (target.mayAccessHold(performer)) {
/*       */                       
/*   345 */                       toReturn.add(Actions.actionEntrys[568]);
/*       */                     }
/*       */                   
/*   348 */                   } catch (NoSuchItemException nse) {
/*       */                     
/*   350 */                     logger.log(Level.WARNING, "No lock with id " + lockId + ", although the item has that.");
/*       */                   
/*       */                   }
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*   357 */               else if (target.getTemplateId() == 94 || target
/*   358 */                 .getTemplateId() == 152 || target
/*   359 */                 .getTemplateId() == 780 || target
/*   360 */                 .getTemplateId() == 95 || target
/*   361 */                 .getTemplateId() == 150 || target
/*   362 */                 .getTemplateId() == 96 || target
/*   363 */                 .getTemplateId() == 151) {
/*   364 */                 toReturn.add(Actions.actionEntrys[939]);
/*       */               }
/*       */             
/*       */             }
/*   368 */           } catch (NoSuchItemException noSuchItemException) {}
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*   373 */           toReturn.addAll(AutoEquipMethods.getBehaviours(target, performer));
/*   374 */           addCreationWindowOption(performer, target, toReturn);
/*       */           
/*   376 */           if (target.isRoyal())
/*       */           {
/*   378 */             if (tid == 536 || tid == 530 || tid == 533) {
/*       */               
/*   380 */               toReturn.add(Actions.actionEntrys[355]);
/*   381 */               toReturn.add(Actions.actionEntrys[356]);
/*   382 */               toReturn.add(Actions.actionEntrys[358]);
/*       */             } 
/*       */           }
/*   385 */           if (target.isLiquid()) {
/*       */             
/*   387 */             toReturn.add(new ActionEntry((short)-1, "Pour", "pouring", new int[0]));
/*   388 */             toReturn.add(new ActionEntry((short)7, "On ground", "pouring", new int[0]));
/*       */           }
/*       */           else {
/*       */             
/*   392 */             int templateId = target.getTemplateId();
/*   393 */             if (templateId == 26 || templateId == 298) {
/*       */               
/*   395 */               toReturn.add(new ActionEntry((short)-2, "Drop", "dropping", new int[0]));
/*   396 */               toReturn.add(new ActionEntry((short)7, "On ground", "dropping", new int[0]));
/*   397 */               toReturn.add(Actions.actionEntrys[638]);
/*       */             }
/*   399 */             else if (!target.isComponentItem()) {
/*       */               
/*   401 */               toReturn.add(new ActionEntry((short)-2, "Drop", "dropping", new int[0]));
/*   402 */               toReturn.add(new ActionEntry((short)7, "On ground", "dropping", new int[0]));
/*   403 */               toReturn.add(Actions.actionEntrys[925]);
/*       */             } 
/*       */           } 
/*       */           
/*   407 */           if (!target.isNoRename() && (!target.isVehicle() || target.isChair() || target.isTent()))
/*   408 */             toReturn.add(Actions.actionEntrys[59]); 
/*   409 */           if (tid == 175 || tid == 651 || tid == 1097 || tid == 1098 || (tid == 466 && target
/*   410 */             .getAuxData() == 1)) {
/*   411 */             toReturn.add(Actions.actionEntrys[3]);
/*   412 */           } else if (tid == 782) {
/*   413 */             toReturn.add(Actions.actionEntrys[518]);
/*   414 */           } else if (tid == 1172) {
/*       */ 
/*       */             
/*   417 */             toReturn.add(new ActionEntry((short)-13, "Set volume to", "setting", new int[0]));
/*   418 */             toReturn.add(Actions.actionEntrys[737]);
/*   419 */             toReturn.add(Actions.actionEntrys[736]);
/*   420 */             toReturn.add(Actions.actionEntrys[735]);
/*   421 */             toReturn.add(Actions.actionEntrys[734]);
/*   422 */             toReturn.add(Actions.actionEntrys[733]);
/*   423 */             toReturn.add(Actions.actionEntrys[732]);
/*   424 */             toReturn.add(Actions.actionEntrys[731]);
/*   425 */             toReturn.add(Actions.actionEntrys[730]);
/*   426 */             toReturn.add(Actions.actionEntrys[729]);
/*   427 */             toReturn.add(Actions.actionEntrys[728]);
/*   428 */             toReturn.add(Actions.actionEntrys[727]);
/*   429 */             toReturn.add(Actions.actionEntrys[726]);
/*   430 */             toReturn.add(Actions.actionEntrys[725]);
/*       */           }
/*   432 */           else if (tid == 200 || tid == 1192 || tid == 69 || tid == 66 || tid == 68 || tid == 29 || tid == 32) {
/*       */ 
/*       */ 
/*       */             
/*   436 */             toReturn.add(Actions.actionEntrys[936]);
/*       */           }
/*   438 */           else if (tid == 479) {
/*       */             
/*   440 */             toReturn.add(Actions.actionEntrys[937]);
/*       */           } 
/*   442 */           if (tid == 176) {
/*       */             
/*   444 */             if (performer.getPower() >= 2) {
/*       */               
/*   446 */               toReturn.add(Actions.actionEntrys[244]);
/*   447 */               toReturn.add(Actions.actionEntrys[503]);
/*   448 */               toReturn.add(Actions.actionEntrys[719]);
/*       */             } 
/*   450 */             if (performer.getPower() >= 3) {
/*       */               
/*   452 */               short nos = -5;
/*   453 */               if (Servers.isThisLoginServer())
/*   454 */                 nos = (short)(nos - 1); 
/*   455 */               if (Servers.isThisLoginServer())
/*   456 */                 nos = (short)(nos - 1); 
/*   457 */               toReturn.add(new ActionEntry(nos, "Server", "Server stuff", emptyIntArr));
/*   458 */               toReturn.add(Actions.actionEntrys[184]);
/*   459 */               toReturn.add(Actions.actionEntrys[195]);
/*   460 */               toReturn.add(Actions.actionEntrys[194]);
/*   461 */               toReturn.add(Actions.actionEntrys[212]);
/*   462 */               toReturn.add(Actions.actionEntrys[481]);
/*   463 */               if (Servers.isThisLoginServer())
/*   464 */                 toReturn.add(Actions.actionEntrys[609]); 
/*   465 */               if (Servers.isThisLoginServer()) {
/*   466 */                 toReturn.add(Actions.actionEntrys[635]);
/*       */               }
/*   468 */             } else if (WurmPermissions.maySetFaith(performer)) {
/*   469 */               toReturn.add(Actions.actionEntrys[212]);
/*       */             } 
/*   471 */           }  if (tid == 315) {
/*       */             
/*   473 */             if (performer.getPower() >= 2)
/*       */             {
/*   475 */               toReturn.add(Actions.actionEntrys[244]);
/*   476 */               toReturn.add(Actions.actionEntrys[503]);
/*       */             }
/*       */           
/*   479 */           } else if (tid == 682) {
/*       */             
/*   481 */             if (Servers.localServer.PVPSERVER)
/*       */             {
/*   483 */               toReturn.add(Actions.actionEntrys[480]);
/*       */             }
/*       */           }
/*   486 */           else if (tid == 1024) {
/*       */             
/*   488 */             toReturn.add(Actions.actionEntrys[115]);
/*       */           } 
/*   490 */           if (!target.isFullprice()) {
/*       */             
/*   492 */             toReturn.add(new ActionEntry((short)-2, "Prices", "Prices", emptyIntArr));
/*   493 */             toReturn.add(Actions.actionEntrys[86]);
/*   494 */             toReturn.add(Actions.actionEntrys[87]);
/*       */           }
/*       */           else {
/*       */             
/*   498 */             toReturn.add(new ActionEntry((short)-1, "Prices", "Prices", emptyIntArr));
/*   499 */             toReturn.add(Actions.actionEntrys[87]);
/*       */           } 
/*   501 */           if (target.isEgg()) {
/*       */             
/*   503 */             toReturn.add(Actions.actionEntrys[328]);
/*   504 */             if (tid == 465)
/*   505 */               toReturn.add(Actions.actionEntrys[330]); 
/*       */           } 
/*   507 */           if (!target.isLiquid() && target.isWrapped()) {
/*       */             
/*   509 */             toReturn.add(new ActionEntry((short)740, "Unwrap", "unwrapping", emptyIntArr));
/*       */           }
/*   511 */           else if (target.isRaw() && target.canBeRawWrapped() && target.isPStateNone()) {
/*       */             
/*   513 */             toReturn.add(new ActionEntry((short)739, "Wrap", "wrapping", emptyIntArr));
/*       */           } 
/*   515 */           if (tid == 1101) {
/*       */             
/*   517 */             toReturn.add(Actions.actionEntrys[183]);
/*       */           }
/*   519 */           else if (target.isFood()) {
/*       */             
/*   521 */             if (!target.isNoEatOrDrink())
/*       */             {
/*   523 */               toReturn.add(Actions.actionEntrys[19]);
/*   524 */               toReturn.add(Actions.actionEntrys[182]);
/*       */             }
/*       */           
/*   527 */           } else if (target.isLiquid()) {
/*       */             
/*   529 */             if (!target.isNoEatOrDrink() && !target.isUndistilled() && target.isDrinkable())
/*       */             {
/*   531 */               toReturn.add(Actions.actionEntrys[19]);
/*   532 */               toReturn.add(Actions.actionEntrys[183]);
/*       */             }
/*       */           
/*   535 */           } else if (target.isRepairable() || tid == 179 || tid == 386) {
/*       */             
/*   537 */             if (!target.isKingdomMarker() || performer.isFriendlyKingdom(target.getAuxData()))
/*   538 */               toReturn.add(Actions.actionEntrys[162]); 
/*       */           } 
/*   540 */           if (target.isContainerLiquid() && target.getItemCount() == 1)
/*       */           {
/*   542 */             for (Item i : target.getItems()) {
/*       */               
/*   544 */               if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable())
/*       */               {
/*   546 */                 if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable() && !target.isSealedByPlayer()) {
/*       */                   
/*   548 */                   toReturn.add(Actions.actionEntrys[19]);
/*   549 */                   toReturn.add(Actions.actionEntrys[183]);
/*       */                   break;
/*       */                 } 
/*       */               }
/*       */             } 
/*       */           }
/*   555 */           if (target.isWeaponBow()) {
/*   556 */             toReturn.add(Actions.actionEntrys[133]);
/*   557 */           } else if (target.isGem()) {
/*       */             
/*   559 */             if (target.getData1() > 0) {
/*   560 */               toReturn.add(Actions.actionEntrys[118]);
/*       */             }
/*   562 */           } else if (tid == 233) {
/*       */             
/*   564 */             toReturn.add(Actions.actionEntrys[682]);
/*       */           }
/*   566 */           else if (tid == 781 || tid == 843 || tid == 1300) {
/*       */             
/*   568 */             if (target.getOwnerId() != -10L) {
/*   569 */               toReturn.add(Actions.actionEntrys[118]);
/*       */             }
/*   571 */           } else if (tid == 719) {
/*   572 */             toReturn.add(Actions.actionEntrys[118]);
/*   573 */           } else if (target.isServerPortal()) {
/*       */             
/*   575 */             toReturn.add(Actions.actionEntrys[118]);
/*       */           }
/*   577 */           else if (tid == 602) {
/*       */             
/*   579 */             toReturn.add(Actions.actionEntrys[118]);
/*       */           }
/*   581 */           else if (target.isDeathProtection() || tid == 527 || tid == 5 || tid == 834 || tid == 836) {
/*       */             
/*   583 */             toReturn.add(Actions.actionEntrys[118]);
/*   584 */           } else if (target.isAbility() && !target.canBePlanted()) {
/*       */             
/*   586 */             toReturn.add(Actions.actionEntrys[118]);
/*       */           }
/*   588 */           else if (target.getTemplateId() == 1438) {
/*   589 */             toReturn.add(new ActionEntry((short)118, "Claim affinity", "claiming"));
/*   590 */           }  if (target.isInstaDiscard())
/*       */           {
/*   592 */             toReturn.add(Actions.actionEntrys[600]);
/*       */           
/*       */           }
/*       */         
/*       */         }
/*   597 */         else if (!target.isNoRename() && performer.getPower() >= 2 && (!target.isVehicle() || target.isChair() || target.isTent()))
/*       */         {
/*   599 */           toReturn.add(Actions.actionEntrys[59]);
/*       */         }
/*       */       
/*   602 */       } catch (NotOwnedException ex) {
/*       */         
/*   604 */         if (!target.isBanked()) {
/*       */           
/*   606 */           float maxDist = 6.0F;
/*   607 */           if (target.isVehicle()) {
/*       */             
/*   609 */             Vehicle vehicle = Vehicles.getVehicle(target);
/*   610 */             if (vehicle != null)
/*   611 */               maxDist = Math.max(maxDist, vehicle.getMaxAllowedLoadDistance()); 
/*       */           } 
/*   613 */           if (target.getTopParent() == performer.getVehicle() || performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target
/*   614 */               .getPosZ(), maxDist)) {
/*       */             
/*   616 */             BlockingResult result = (target.getWurmId() == performer.getVehicle()) ? null : Blocking.getBlockerBetween(performer, target, 4);
/*   617 */             if (result == null) {
/*       */               
/*   619 */               boolean sameStruct = true;
/*       */               
/*       */               try {
/*   622 */                 Zone tzone = Zones.getZone((int)target.getPosX() >> 2, (int)target.getPosY() >> 2, target
/*   623 */                     .isOnSurface());
/*   624 */                 VolaTile tile = tzone.getTileOrNull((int)target.getPosX() >> 2, 
/*   625 */                     (int)target.getPosY() >> 2);
/*   626 */                 if (tile != null) {
/*       */                   
/*   628 */                   Structure struct = tile.getStructure();
/*   629 */                   VolaTile tile2 = performer.getCurrentTile();
/*   630 */                   if (tile2 != null) {
/*       */                     
/*   632 */                     if (tile2.getStructure() != struct)
/*       */                     {
/*   634 */                       if (struct != null && struct.isTypeHouse())
/*   635 */                         sameStruct = false; 
/*   636 */                       if (tile2.getStructure() != null && tile2.getStructure().isTypeHouse()) {
/*   637 */                         sameStruct = false;
/*       */                       }
/*       */                     }
/*       */                   
/*       */                   }
/*   642 */                   else if (struct != null && struct.isTypeHouse()) {
/*   643 */                     sameStruct = false;
/*       */                   }
/*       */                 
/*       */                 } 
/*   647 */               } catch (NoSuchZoneException noSuchZoneException) {}
/*       */ 
/*       */ 
/*       */               
/*   651 */               if (sameStruct) {
/*       */                 
/*   653 */                 if (target.isLight() || target.isFire() || target.getTemplateId() == 1243)
/*       */                 {
/*   655 */                   if (target.isOnFire()) {
/*       */                     
/*   657 */                     if (!target.isAlwaysLit())
/*       */                     {
/*   659 */                       if (target.getTemplateId() == 729) {
/*   660 */                         toReturn.add(new ActionEntry((short)53, "Blow out", "blowing out", new int[] { 0, 43 }));
/*       */                       } else {
/*       */                         
/*   663 */                         toReturn.add(Actions.actionEntrys[53]);
/*       */                       } 
/*       */                     }
/*   666 */                   } else if (target.getTemplateId() == 1396) {
/*       */                     
/*   668 */                     if (target.getAuxData() > 0 && target.isPlanted()) {
/*   669 */                       toReturn.add(Actions.actionEntrys[12]);
/*       */                     }
/*   671 */                   } else if (target.isLight() || target.getTemplateId() == 742) {
/*       */                     
/*   673 */                     if (target.getTemplateId() != 729 || target.getAuxData() > 0)
/*   674 */                       toReturn.add(Actions.actionEntrys[12]); 
/*       */                   } 
/*       */                 }
/*   677 */                 if (tid == 538 && !Servers.localServer.isChallengeServer()) {
/*   678 */                   toReturn.add(Actions.actionEntrys[353]);
/*   679 */                 } else if (tid == 726) {
/*       */                   
/*   681 */                   if (performer.getKingdomId() == target.getAuxData()) {
/*       */                     
/*   683 */                     King k = King.getKing(target.getAuxData());
/*   684 */                     if (k != null)
/*       */                     {
/*   686 */                       if (k.mayBeChallenged()) {
/*   687 */                         toReturn.add(Actions.actionEntrys[488]);
/*   688 */                       } else if (k.hasFailedAllChallenges()) {
/*   689 */                         toReturn.add(Actions.actionEntrys[487]);
/*       */                       }  } 
/*       */                   } 
/*   692 */                   if (performer.getPower() >= 3)
/*       */                   {
/*   694 */                     toReturn.add(Actions.actionEntrys[118]);
/*       */                   }
/*       */                 }
/*   697 */                 else if (target.isMeditation()) {
/*       */                   
/*   699 */                   short nums = -1;
/*   700 */                   Cultist c = Cultist.getCultist(performer.getWurmId());
/*   701 */                   if (c != null) {
/*       */                     
/*   703 */                     nums = (short)(nums - 1);
/*   704 */                     if (c.getLevel() > 2)
/*   705 */                       nums = (short)(nums - 1); 
/*   706 */                     if (c.getPath() == 4)
/*   707 */                       nums = (short)(nums - 1); 
/*       */                   } 
/*   709 */                   toReturn.add(new ActionEntry(nums, "Nature", "meditation"));
/*   710 */                   toReturn.add(Actions.actionEntrys[384]);
/*   711 */                   if (nums < -1)
/*   712 */                     toReturn.add(Actions.actionEntrys[385]); 
/*   713 */                   if (nums < -2 && c.getLevel() > 2)
/*   714 */                     toReturn.add(Actions.actionEntrys[386]); 
/*   715 */                   if (nums < -2 && c.getPath() == 4) {
/*   716 */                     toReturn.add(Actions.actionEntrys[722]);
/*       */                   }
/*       */                 } 
/*   719 */                 toReturn.addAll(AutoEquipMethods.getBehaviours(target, performer));
/*   720 */                 addCreationWindowOption(performer, target, toReturn);
/*       */                 
/*   722 */                 if (tid == 652)
/*   723 */                   toReturn.add(Actions.actionEntrys[214]); 
/*   724 */                 if (!target.isNoTake(performer) && !target.isOutsideOnly() && !target.canBePlanted())
/*       */                 {
/*   726 */                   if (!target.isLiquid())
/*       */                   {
/*   728 */                     if (!target.isBulkContainer())
/*       */                     {
/*   730 */                       if (MethodsItems.checkIfStealing(target, performer, null)) {
/*   731 */                         toReturn.add(Actions.actionEntrys[100]);
/*       */                       } else {
/*       */                         
/*   734 */                         toReturn.add(Actions.actionEntrys[6]);
/*   735 */                         if ((target.getParentId() == -10L && tid != 26) || tid != 298)
/*   736 */                           toReturn.add(Actions.actionEntrys[925]); 
/*       */                       } 
/*       */                     }
/*       */                   }
/*       */                 }
/*   741 */                 if (target.isHollow() && 
/*   742 */                   !((target.isSealedByPlayer() || (target.getTemplateId() == 1342 && !target.isPlanted())) ? 1 : 0) && (
/*   743 */                   !target.getTemplate().hasViewableSubItems() || target.getTemplate().isContainerWithSubItems() || performer
/*   744 */                   .getPower() > 0)) {
/*       */ 
/*       */ 
/*       */                   
/*   748 */                   boolean isTop = (target.getWurmId() == target.getTopParent() || (target.getTopParentOrNull() != null && target.getTopParentOrNull().getTemplate().hasViewableSubItems() && (!target.getTopParentOrNull().getTemplate().isContainerWithSubItems() || target.isPlacedOnParent())));
/*   749 */                   if ((target.getLockId() == -10L && isTop) || (target
/*   750 */                     .isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target)) || (target
/*   751 */                     .getTemplateId() == 850 && MethodsItems.mayUseInventoryOfVehicle(performer, target))) {
/*       */ 
/*       */                     
/*       */                     try {
/*       */                       
/*   756 */                       Creature[] watchers = target.getWatchers();
/*   757 */                       boolean watching = false;
/*   758 */                       for (Creature lWatcher : watchers) {
/*       */                         
/*   760 */                         if (lWatcher == performer) {
/*       */                           
/*   762 */                           watching = true;
/*       */                           break;
/*       */                         } 
/*       */                       } 
/*   766 */                       if (watching) {
/*   767 */                         toReturn.add(Actions.actionEntrys[4]);
/*   768 */                       } else if (target.getTemplateId() == 272 && target.getWasBrandedTo() != -10L) {
/*       */                         
/*   770 */                         if (target.mayCommand(performer)) {
/*   771 */                           toReturn.add(Actions.actionEntrys[3]);
/*       */                         }
/*       */                       } else {
/*   774 */                         toReturn.add(Actions.actionEntrys[3]);
/*       */                       } 
/*   776 */                     } catch (NoSuchCreatureException nsc) {
/*       */                       
/*   778 */                       if (target.getTemplateId() == 272 && target.getWasBrandedTo() != -10L) {
/*       */                         
/*   780 */                         if (target.mayCommand(performer)) {
/*   781 */                           toReturn.add(Actions.actionEntrys[3]);
/*       */                         }
/*       */                       } else {
/*   784 */                         toReturn.add(Actions.actionEntrys[3]);
/*       */                       } 
/*   786 */                     }  if (target.isOwner(performer) && target.isLocked())
/*       */                     {
/*   788 */                       toReturn.add(new ActionEntry((short)-1, LoginHandler.raiseFirstLetter(target.getActualName()), target.getActualName()));
/*   789 */                       toReturn.add(Actions.actionEntrys[102]);
/*       */                     }
/*       */                   
/*   792 */                   } else if (target.getLockId() != -10L) {
/*       */ 
/*       */                     
/*       */                     try {
/*   796 */                       Item lock = Items.getItem(target.getLockId());
/*   797 */                       boolean hasKey = (performer.hasKeyForLock(lock) || target.isOwner(performer));
/*   798 */                       if (target.mayAccessHold(performer))
/*   799 */                         hasKey = true; 
/*   800 */                       if (hasKey) {
/*       */ 
/*       */                         
/*       */                         try {
/*   804 */                           Creature[] watchers = target.getWatchers();
/*   805 */                           boolean watching = false;
/*   806 */                           for (Creature lWatcher : watchers) {
/*       */                             
/*   808 */                             if (lWatcher == performer) {
/*       */                               
/*   810 */                               watching = true;
/*       */                               break;
/*       */                             } 
/*       */                           } 
/*   814 */                           if (watching) {
/*   815 */                             toReturn.add(Actions.actionEntrys[4]);
/*       */                           } else {
/*   817 */                             toReturn.add(Actions.actionEntrys[3]);
/*       */                           } 
/*   819 */                         } catch (NoSuchCreatureException nsc) {
/*       */                           
/*   821 */                           toReturn.add(Actions.actionEntrys[3]);
/*       */                         } 
/*   823 */                         if (target.isOwner(performer) && target.isLocked())
/*       */                         {
/*   825 */                           toReturn.add(new ActionEntry((short)-1, LoginHandler.raiseFirstLetter(target.getActualName()), target.getActualName()));
/*   826 */                           toReturn.add(Actions.actionEntrys[102]);
/*       */                         }
/*       */                       
/*       */                       } 
/*   830 */                     } catch (NoSuchItemException noSuchItemException) {}
/*       */                   } 
/*       */ 
/*       */ 
/*       */                   
/*   835 */                   if (target.isMailBox())
/*       */                   {
/*   837 */                     if (target.isEmpty(false)) {
/*   838 */                       toReturn.add(Actions.actionEntrys[336]);
/*       */                     } else {
/*   840 */                       toReturn.add(Actions.actionEntrys[337]);
/*       */                     }  } 
/*       */                 } 
/*   843 */                 if (target.getTemplateId() != 272 || target.getWasBrandedTo() == -10L || target.mayCommand(performer))
/*       */                 {
/*       */ 
/*       */ 
/*       */                   
/*   848 */                   toReturn.addAll(makeMoveSubMenu(performer, target)); } 
/*   849 */                 if (target.isServerPortal()) {
/*       */                   
/*   851 */                   toReturn.add(Actions.actionEntrys[118]);
/*       */                 }
/*   853 */                 else if (tid == 972) {
/*       */                   
/*   855 */                   toReturn.add(new ActionEntry((short)118, "Pat", "patting"));
/*       */                 }
/*   857 */                 else if (tid == 738 || tid == 741) {
/*   858 */                   toReturn.add(Actions.actionEntrys[118]);
/*   859 */                 } else if (tid == 739 || target.isWarTarget()) {
/*   860 */                   toReturn.add(Actions.actionEntrys[504]);
/*   861 */                 } else if (tid == 722) {
/*   862 */                   toReturn.add(Actions.actionEntrys[118]);
/*   863 */                 }  if (target.isDraggable()) {
/*       */                   
/*   865 */                   boolean ok = true;
/*   866 */                   if (target.isVehicle()) {
/*       */                     
/*   868 */                     Vehicle vehic = Vehicles.getVehicle(target);
/*   869 */                     if (vehic.pilotId != -10L)
/*   870 */                       ok = false; 
/*   871 */                     if (vehic.draggers != null && !vehic.draggers.isEmpty()) {
/*   872 */                       ok = false;
/*       */                     }
/*       */                   } 
/*       */                   
/*   876 */                   if (performer.getVehicle() != -10L)
/*   877 */                     ok = false; 
/*   878 */                   if (ok && !Items.isItemDragged(target) && target.getTopParent() == target.getWurmId()) {
/*       */                     
/*   880 */                     boolean havePermission = VehicleBehaviour.hasPermission(performer, target);
/*   881 */                     if (havePermission || target.mayDrag(performer)) {
/*   882 */                       toReturn.add(Actions.actionEntrys[74]);
/*       */                     }
/*   884 */                   } else if (performer.getDraggedItem() == target) {
/*   885 */                     toReturn.add(Actions.actionEntrys[75]);
/*       */                   } 
/*   887 */                 }  if (target.isBed())
/*       */                 {
/*   889 */                   addBedOptions(performer, target, toReturn);
/*       */                 }
/*   891 */                 if (target.isFood()) {
/*       */                   
/*   893 */                   if (!target.isNoEatOrDrink())
/*       */                   {
/*   895 */                     toReturn.add(Actions.actionEntrys[19]);
/*   896 */                     toReturn.add(Actions.actionEntrys[182]);
/*       */                   }
/*       */                 
/*   899 */                 } else if (target.isLiquid()) {
/*       */                   
/*   901 */                   if (!target.isNoEatOrDrink() && !target.isUndistilled() && target.isDrinkable())
/*       */                   {
/*   903 */                     toReturn.add(Actions.actionEntrys[19]);
/*   904 */                     toReturn.add(Actions.actionEntrys[183]);
/*       */                   }
/*       */                 
/*   907 */                 } else if (target.isRepairable() || tid == 179 || tid == 386) {
/*       */ 
/*       */                   
/*   910 */                   if (!target.isKingdomMarker() || performer.isFriendlyKingdom(target.getAuxData()))
/*   911 */                     toReturn.add(Actions.actionEntrys[162]); 
/*       */                 } 
/*   913 */                 if (target.isContainerLiquid() && target.getItemCount() == 1)
/*       */                 {
/*   915 */                   for (Item i : target.getItems()) {
/*       */                     
/*   917 */                     if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable())
/*       */                     {
/*   919 */                       if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable() && !target.isSealedByPlayer()) {
/*       */                         
/*   921 */                         toReturn.add(Actions.actionEntrys[19]);
/*   922 */                         toReturn.add(Actions.actionEntrys[183]);
/*       */                         break;
/*       */                       } 
/*       */                     }
/*       */                   } 
/*       */                 }
/*   928 */                 if (target.isWeaponBow())
/*   929 */                   toReturn.add(Actions.actionEntrys[133]); 
/*   930 */                 if (tid == 442)
/*       */                 {
/*   932 */                   toReturn.add(new ActionEntry((short)91, "Taste the julbord", "eating"));
/*       */                 }
/*   934 */                 if (target.isEgg())
/*       */                 {
/*   936 */                   if (tid == 465)
/*   937 */                     toReturn.add(Actions.actionEntrys[330]); 
/*       */                 }
/*   939 */                 if ((target.lastOwner == performer.getWurmId() || performer.getPower() >= 2 || target.isShelf()) && 
/*   940 */                   !target.isNoRename() && (!target.isVehicle() || target.isChair() || target.isTent()))
/*       */                 {
/*       */ 
/*       */                   
/*   944 */                   if (target.isShelf() && performer.getPower() < 1 && target
/*   945 */                     .getLastOwnerId() != performer.getWurmId()) {
/*       */                     
/*   947 */                     Item outerParent = target.getOuterItemOrNull();
/*   948 */                     if (outerParent != null && outerParent.mayManage(performer))
/*       */                     {
/*   950 */                       toReturn.add(Actions.actionEntrys[59]);
/*       */                     }
/*       */                   }
/*   953 */                   else if (performer.getPower() >= 2 || target.getLastOwnerId() == performer.getWurmId()) {
/*   954 */                     toReturn.add(Actions.actionEntrys[59]);
/*       */                   } 
/*       */                 }
/*   957 */                 if (target.isEpicTargetItem() || target.isKingdomMarker()) {
/*       */                   
/*   959 */                   MissionTrigger[] mr1 = MissionTriggers.getMissionTriggersWith(-1, 501, target
/*   960 */                       .getWurmId());
/*   961 */                   if (mr1.length > 0)
/*   962 */                     toReturn.add(Actions.actionEntrys[501]); 
/*   963 */                   MissionTrigger[] mr2 = MissionTriggers.getMissionTriggersWith(-1, 496, target
/*   964 */                       .getWurmId());
/*   965 */                   if (mr2.length > 0)
/*   966 */                     toReturn.add(Actions.actionEntrys[496]); 
/*   967 */                   MissionTrigger[] mr3 = MissionTriggers.getMissionTriggersWith(-1, 498, target
/*   968 */                       .getWurmId());
/*   969 */                   if (mr3.length > 0)
/*   970 */                     toReturn.add(Actions.actionEntrys[498]); 
/*   971 */                   MissionTrigger[] mr4 = MissionTriggers.getMissionTriggersWith(-1, 500, target
/*   972 */                       .getWurmId());
/*   973 */                   if (mr4.length > 0)
/*   974 */                     toReturn.add(Actions.actionEntrys[500]); 
/*   975 */                   MissionTrigger[] mr5 = MissionTriggers.getMissionTriggersWith(-1, 502, target
/*   976 */                       .getWurmId());
/*   977 */                   if (mr5.length > 0)
/*   978 */                     toReturn.add(Actions.actionEntrys[502]); 
/*   979 */                   MissionTrigger[] mr6 = MissionTriggers.getMissionTriggersWith(-1, 497, target
/*   980 */                       .getWurmId());
/*   981 */                   if (mr6.length > 0)
/*   982 */                     toReturn.add(Actions.actionEntrys[497]); 
/*   983 */                   MissionTrigger[] mr7 = MissionTriggers.getMissionTriggersWith(-1, 499, target
/*   984 */                       .getWurmId());
/*   985 */                   if (mr7.length > 0)
/*   986 */                     toReturn.add(Actions.actionEntrys[499]); 
/*       */                 } 
/*       */               } 
/*       */             } 
/*       */           } 
/*       */         } 
/*   992 */         toReturn.addAll(CargoTransportationMethods.getLoadUnloadActions(performer, target));
/*       */       } 
/*   994 */       if (target.isCrate() && target.isSealedByPlayer() && target.getLastOwnerId() == performer.getWurmId())
/*   995 */         toReturn.add(Actions.actionEntrys[740]); 
/*   996 */       if (target.canHavePermissions()) {
/*       */         
/*   998 */         List<ActionEntry> permissions = new LinkedList<>();
/*   999 */         if (target.isBed() && target.mayManage(performer)) {
/*  1000 */           permissions.add(new ActionEntry((short)688, "Manage Bed", "managing"));
/*  1001 */         } else if (target.getTemplateId() == 1271 && target.mayManage(performer)) {
/*  1002 */           permissions.add(new ActionEntry((short)688, "Manage Message Board", "managing"));
/*  1003 */         } else if (!target.isVehicle() && target.mayManage(performer)) {
/*  1004 */           permissions.add(Actions.actionEntrys[688]);
/*  1005 */         }  if (target.isBed() && (target.isOwner(performer) || performer.getPower() > 1)) {
/*  1006 */           permissions.add(new ActionEntry((short)691, "History Of Bed", "viewing"));
/*  1007 */         } else if (target.getTemplateId() == 1271 && (target.isOwner(performer) || performer.getPower() > 1)) {
/*  1008 */           permissions.add(new ActionEntry((short)691, "History Of Message Board", "viewing"));
/*  1009 */         } else if (!target.isVehicle() && (target.isOwner(performer) || performer.getPower() > 1)) {
/*  1010 */           permissions.add(new ActionEntry((short)691, "History Of Item", "viewing"));
/*       */         } 
/*  1012 */         if (!permissions.isEmpty()) {
/*       */           
/*  1014 */           Collections.sort(permissions);
/*  1015 */           toReturn.add(new ActionEntry((short)-permissions.size(), "Permissions", "viewing"));
/*  1016 */           toReturn.addAll(permissions);
/*       */         } 
/*       */       } 
/*       */       
/*  1020 */       if (target.isSealedByPlayer() && target.getTemplateId() != 1309 && !target.isCrate()) {
/*       */ 
/*       */         
/*  1023 */         Item liquid = null;
/*  1024 */         for (Item item : target.getItemsAsArray()) {
/*       */           
/*  1026 */           if (item.isLiquid()) {
/*       */             
/*  1028 */             liquid = item;
/*       */             
/*       */             break;
/*       */           } 
/*       */         } 
/*  1033 */         if (liquid == null || !liquid.isFermenting())
/*  1034 */           toReturn.add(Actions.actionEntrys[740]); 
/*  1035 */         toReturn.add(Actions.actionEntrys[19]);
/*       */       } 
/*  1037 */       if ((target.isFoodMaker() || target.getTemplate().isCooker()) && !target.isSealedByPlayer()) {
/*  1038 */         toReturn.add(Actions.actionEntrys[285]);
/*       */       }
/*       */     } else {
/*       */       
/*  1042 */       toReturn.add(new ActionEntry((short)-1, "Prices", "Prices"));
/*  1043 */       toReturn.add(Actions.actionEntrys[87]);
/*       */     } 
/*  1045 */     if (tid == 257)
/*  1046 */       toReturn.add(Actions.actionEntrys[79]); 
/*  1047 */     if (target.getTemplateId() == 1310)
/*       */     {
/*  1049 */       if (target.getData() != -10L) {
/*       */         
/*       */         try {
/*       */           
/*  1053 */           Creature cagedCreature = Creatures.getInstance().getCreature(target.getData());
/*  1054 */           if (cagedCreature != null && 
/*  1055 */             !cagedCreature.isPlayer() && !cagedCreature.isHuman() && !cagedCreature.isGhost() && !cagedCreature.isReborn() && (
/*  1056 */             !cagedCreature.isCaredFor() || cagedCreature.getCareTakerId() == performer.getWurmId())) {
/*  1057 */             toReturn.add(Actions.actionEntrys[493]);
/*       */           }
/*  1059 */         } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */       }
/*       */     }
/*       */ 
/*       */ 
/*       */     
/*  1065 */     addEmotes(toReturn);
/*       */ 
/*       */     
/*  1068 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static void addLockOptions(Creature performer, Item source, Item target, long lockId, int stid, boolean isTop, List<ActionEntry> toReturn, Creature[] watchers) {
/*  1074 */     if (lockId != -10L) {
/*       */       
/*  1076 */       boolean addedOpen = false;
/*  1077 */       boolean test1 = (target.isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target));
/*       */ 
/*       */       
/*  1080 */       boolean test2 = (target.getTemplateId() == 850 && MethodsItems.mayUseInventoryOfVehicle(performer, target));
/*       */       
/*  1082 */       if (isTop && (test1 || test2)) {
/*       */         
/*  1084 */         toReturn.add(Actions.actionEntrys[3]);
/*  1085 */         addedOpen = true;
/*       */       } 
/*       */       
/*       */       try {
/*  1089 */         Item lock = Items.getItem(lockId);
/*       */         
/*  1091 */         if (performer.hasKeyForLock(lock) || target.isOwner(performer)) {
/*       */           
/*  1093 */           if (isTop && !addedOpen)
/*  1094 */             toReturn.add(Actions.actionEntrys[3]); 
/*  1095 */           if (target.isOwner(performer))
/*       */           {
/*  1097 */             int sz = -1;
/*  1098 */             if (source.isLock())
/*  1099 */               sz--; 
/*  1100 */             toReturn.add(new ActionEntry((short)sz, LoginHandler.raiseFirstLetter(target.getActualName()), target.getActualName()));
/*  1101 */             if (source.isLock())
/*  1102 */               toReturn.add(new ActionEntry((short)78, "Replace lock", "replacing lock")); 
/*  1103 */             toReturn.add(Actions.actionEntrys[102]);
/*       */           }
/*       */         
/*  1106 */         } else if (target.mayAccessHold(performer)) {
/*       */           
/*  1108 */           if (isTop && !addedOpen)
/*  1109 */             toReturn.add(Actions.actionEntrys[3]); 
/*       */         } 
/*  1111 */         if (stid == 463)
/*       */         {
/*  1113 */           addLockPickEntry(performer, source, target, lock, toReturn);
/*       */         }
/*       */       }
/*  1116 */       catch (NoSuchItemException nsi) {
/*       */         
/*  1118 */         logger.log(Level.WARNING, "No lock with id " + lockId + ", although the item has that.");
/*  1119 */         if (source.isLock() && target.isOwner(performer))
/*       */         {
/*  1121 */           toReturn.add(new ActionEntry((short)-1, LoginHandler.raiseFirstLetter(target.getActualName()), target.getActualName()));
/*  1122 */           toReturn.add(new ActionEntry((short)78, "Replace lock", "replacing lock"));
/*       */         }
/*       */       
/*       */       } 
/*       */     } else {
/*       */       
/*  1128 */       if (isTop)
/*  1129 */         toReturn.add(Actions.actionEntrys[3]); 
/*  1130 */       if (!target.isTent() || target
/*  1131 */         .getLastOwnerId() == performer.getWurmId() || target.getOwnerId() == performer.getWurmId())
/*       */       {
/*  1133 */         if (lockId == -10L && (target.getParentId() != -10L || watchers == null || watchers.length == 0) && ((source
/*  1134 */           .isBoatLock() && target.isBoat()) || source.mayLockItems()) && 
/*  1135 */           target.isOwner(performer)) {
/*  1136 */           toReturn.add(Actions.actionEntrys[161]);
/*       */         }
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   protected static void addLockPickEntry(Creature performer, Item source, Item target, Item lock, List<ActionEntry> toReturn) {
/*  1144 */     boolean isLargeVehicle = (target.isVehicle() && target.getSizeZ() > 5);
/*  1145 */     float rarityMod = 1.0F;
/*  1146 */     if (lock.getRarity() > 0)
/*  1147 */       rarityMod += lock.getRarity() * 0.2F; 
/*  1148 */     if (target.getRarity() > 0)
/*  1149 */       rarityMod += target.getRarity() * 0.2F; 
/*  1150 */     if (source.getRarity() > 0) {
/*  1151 */       rarityMod -= source.getRarity() * 0.1F;
/*       */     }
/*       */ 
/*       */     
/*  1155 */     float difficulty = MethodsItems.getPickChance(target.getCurrentQualityLevel(), source.getCurrentQualityLevel(), lock.getCurrentQualityLevel(), (float)performer.getLockPickingSkillVal(), isLargeVehicle ? 2 : 0) / rarityMod * (1.0F + Item.getMaterialLockpickBonus(source.getMaterial()));
/*  1156 */     String pick = "Pick lock: " + difficulty + "%";
/*  1157 */     toReturn.add(new ActionEntry((short)101, pick, "picking lock"));
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static void addCreationWindowOption(Creature performer, Item target, List<ActionEntry> toReturn) {
/*  1163 */     if (target.isUseOnGroundOnly() && !target.isDomainItem() && 
/*  1164 */       !target.isKingdomMarker() && 
/*  1165 */       !target.hideAddToCreationWindow() && !target.isNoDrop() && 
/*  1166 */       !target.isMailBox()) {
/*       */       
/*  1168 */       if (target.getTopParent() == target.getWurmId()) {
/*  1169 */         toReturn.add(Actions.actionEntrys[607]);
/*       */       }
/*  1171 */     } else if (target.isUnfinished()) {
/*       */       
/*  1173 */       toReturn.add(Actions.actionEntrys[607]);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private static void decay(Item item, Creature performer) {
/*  1179 */     if (item.getTemplateId() == 176 && performer.getPower() > 1 && 
/*  1180 */       Servers.isThisATestServer()) {
/*       */       
/*  1182 */       WurmCalendar.incrementHour();
/*       */       
/*       */       return;
/*       */     } 
/*  1186 */     long decayTime = item.getDecayTime();
/*  1187 */     if (decayTime == Long.MAX_VALUE)
/*       */       return; 
/*  1189 */     long time = item.getLastMaintained();
/*  1190 */     item.setLastMaintained(time - decayTime);
/*  1191 */     if (WurmCalendar.currentTime <= item.creationDate + 1382400L) {
/*  1192 */       item.creationDate -= decayTime;
/*       */     }
/*  1194 */     if (item.isBulkContainer())
/*       */     {
/*  1196 */       if (item.getItemCount() > 0) {
/*       */         
/*  1198 */         Item[] items = item.getItemsAsArray();
/*  1199 */         for (int i = 0; i < item.getItemCount(); i++) {
/*       */           
/*  1201 */           long decayTime2 = items[i].getDecayTime();
/*  1202 */           long time2 = items[i].getLastMaintained();
/*  1203 */           items[i].setLastMaintained(time2 - decayTime2);
/*  1204 */           if (WurmCalendar.currentTime <= (items[i]).creationDate + 1382400L) {
/*  1205 */             (items[i]).creationDate -= decayTime;
/*       */           }
/*       */         } 
/*       */       } 
/*       */     }
/*  1210 */     Item topParent = null;
/*       */     
/*       */     try {
/*  1213 */       topParent = Items.getItem(item.getTopParent());
/*       */     }
/*  1215 */     catch (NoSuchItemException noSuchItemException) {}
/*       */ 
/*       */ 
/*       */     
/*  1219 */     if (topParent != null && topParent.getTemplateId() == 0) {
/*  1220 */       item.pollOwned(performer);
/*       */     } else {
/*       */       
/*  1223 */       int tX = item.getTileX();
/*  1224 */       int tY = item.getTileY();
/*  1225 */       VolaTile tile = Zones.getTileOrNull(tX, tY, item.isOnSurface());
/*  1226 */       if (tile != null) {
/*       */         
/*  1228 */         item.poll((tile.getStructure() != null), (tile.getVillage() != null), 0L);
/*       */       }
/*       */       else {
/*       */         
/*  1232 */         item.poll(false, false, 0L);
/*       */       } 
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*       */     // Byte code:
/*       */     //   0: new java/util/LinkedList
/*       */     //   3: dup
/*       */     //   4: invokespecial <init> : ()V
/*       */     //   7: astore #4
/*       */     //   9: aload_3
/*       */     //   10: invokevirtual getTemplateId : ()I
/*       */     //   13: istore #5
/*       */     //   15: aload_2
/*       */     //   16: invokevirtual getTemplateId : ()I
/*       */     //   19: istore #6
/*       */     //   21: iconst_0
/*       */     //   22: istore #7
/*       */     //   24: aload #4
/*       */     //   26: aload_0
/*       */     //   27: aload_1
/*       */     //   28: aload_2
/*       */     //   29: aload_3
/*       */     //   30: invokespecial getBehavioursFor : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Ljava/util/List;
/*       */     //   33: invokeinterface addAll : (Ljava/util/Collection;)Z
/*       */     //   38: pop
/*       */     //   39: goto -> 44
/*       */     //   42: astore #8
/*       */     //   44: ldc2_w -10
/*       */     //   47: lstore #8
/*       */     //   49: aload_3
/*       */     //   50: invokevirtual isTraded : ()Z
/*       */     //   53: ifne -> 5226
/*       */     //   56: aload_3
/*       */     //   57: invokevirtual canBePlanted : ()Z
/*       */     //   60: ifeq -> 912
/*       */     //   63: aload_3
/*       */     //   64: invokevirtual getOwnerId : ()J
/*       */     //   67: lstore #10
/*       */     //   69: lload #10
/*       */     //   71: ldc2_w -10
/*       */     //   74: lcmp
/*       */     //   75: ifne -> 464
/*       */     //   78: aload_1
/*       */     //   79: aload_3
/*       */     //   80: invokevirtual getPosX : ()F
/*       */     //   83: aload_3
/*       */     //   84: invokevirtual getPosY : ()F
/*       */     //   87: aload_3
/*       */     //   88: invokevirtual getPosZ : ()F
/*       */     //   91: ldc 4.0
/*       */     //   93: invokevirtual isWithinDistanceTo : (FFFF)Z
/*       */     //   96: ifeq -> 402
/*       */     //   99: aload_1
/*       */     //   100: aload_3
/*       */     //   101: iconst_4
/*       */     //   102: invokestatic getBlockerBetween : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;I)Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   105: astore #12
/*       */     //   107: aload #12
/*       */     //   109: ifnonnull -> 402
/*       */     //   112: invokestatic isThisAPvpServer : ()Z
/*       */     //   115: istore #13
/*       */     //   117: aload_3
/*       */     //   118: aload_1
/*       */     //   119: aconst_null
/*       */     //   120: invokestatic checkIfStealing : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/behaviours/Action;)Z
/*       */     //   123: ifeq -> 148
/*       */     //   126: iload #13
/*       */     //   128: ifeq -> 335
/*       */     //   131: aload #4
/*       */     //   133: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   136: bipush #100
/*       */     //   138: aaload
/*       */     //   139: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   144: pop
/*       */     //   145: goto -> 335
/*       */     //   148: aload_3
/*       */     //   149: invokevirtual isKingdomFlag : ()Z
/*       */     //   152: ifeq -> 215
/*       */     //   155: aload_3
/*       */     //   156: invokevirtual getAuxData : ()B
/*       */     //   159: aload_1
/*       */     //   160: invokevirtual getKingdomId : ()B
/*       */     //   163: if_icmpeq -> 215
/*       */     //   166: aload_3
/*       */     //   167: aload_1
/*       */     //   168: bipush #6
/*       */     //   170: invokestatic isSignManipulationOk : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/creatures/Creature;S)Z
/*       */     //   173: ifeq -> 193
/*       */     //   176: aload #4
/*       */     //   178: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   181: bipush #6
/*       */     //   183: aaload
/*       */     //   184: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   189: pop
/*       */     //   190: goto -> 335
/*       */     //   193: iload #13
/*       */     //   195: ifeq -> 335
/*       */     //   198: aload #4
/*       */     //   200: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   203: bipush #100
/*       */     //   205: aaload
/*       */     //   206: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   211: pop
/*       */     //   212: goto -> 335
/*       */     //   215: aload_3
/*       */     //   216: invokevirtual isBulkContainer : ()Z
/*       */     //   219: ifne -> 232
/*       */     //   222: aload_3
/*       */     //   223: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   226: invokevirtual getInitialContainers : ()[Lcom/wurmonline/server/items/InitialContainer;
/*       */     //   229: ifnull -> 240
/*       */     //   232: aload_3
/*       */     //   233: iconst_1
/*       */     //   234: invokevirtual isEmpty : (Z)Z
/*       */     //   237: ifeq -> 335
/*       */     //   240: aload_3
/*       */     //   241: aload_1
/*       */     //   242: invokevirtual isNoTake : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   245: ifne -> 335
/*       */     //   248: aload_3
/*       */     //   249: aload_1
/*       */     //   250: bipush #6
/*       */     //   252: invokestatic isSignManipulationOk : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/creatures/Creature;S)Z
/*       */     //   255: ifeq -> 316
/*       */     //   258: aload #4
/*       */     //   260: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   263: bipush #6
/*       */     //   265: aaload
/*       */     //   266: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   271: pop
/*       */     //   272: aload_3
/*       */     //   273: invokevirtual getParentId : ()J
/*       */     //   276: ldc2_w -10
/*       */     //   279: lcmp
/*       */     //   280: ifne -> 290
/*       */     //   283: iload #5
/*       */     //   285: bipush #26
/*       */     //   287: if_icmpne -> 298
/*       */     //   290: iload #5
/*       */     //   292: sipush #298
/*       */     //   295: if_icmpeq -> 335
/*       */     //   298: aload #4
/*       */     //   300: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   303: sipush #925
/*       */     //   306: aaload
/*       */     //   307: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   312: pop
/*       */     //   313: goto -> 335
/*       */     //   316: iload #13
/*       */     //   318: ifeq -> 335
/*       */     //   321: aload #4
/*       */     //   323: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   326: bipush #100
/*       */     //   328: aaload
/*       */     //   329: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   334: pop
/*       */     //   335: aload_3
/*       */     //   336: invokevirtual isPlanted : ()Z
/*       */     //   339: ifne -> 402
/*       */     //   342: aload_3
/*       */     //   343: invokevirtual getParentId : ()J
/*       */     //   346: ldc2_w -10
/*       */     //   349: lcmp
/*       */     //   350: ifne -> 402
/*       */     //   353: aload_3
/*       */     //   354: invokevirtual isRoadMarker : ()Z
/*       */     //   357: ifne -> 402
/*       */     //   360: aload #4
/*       */     //   362: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   365: dup
/*       */     //   366: sipush #176
/*       */     //   369: ldc 'Secure'
/*       */     //   371: new java/lang/StringBuilder
/*       */     //   374: dup
/*       */     //   375: invokespecial <init> : ()V
/*       */     //   378: ldc 'securing the '
/*       */     //   380: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   383: aload_3
/*       */     //   384: invokevirtual getName : ()Ljava/lang/String;
/*       */     //   387: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   390: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   393: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   396: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   401: pop
/*       */     //   402: getstatic com/wurmonline/server/Features$Feature.SINGLE_PLAYER_BRIDGES : Lcom/wurmonline/server/Features$Feature;
/*       */     //   405: invokevirtual isEnabled : ()Z
/*       */     //   408: ifeq -> 655
/*       */     //   411: aload_2
/*       */     //   412: invokevirtual getTemplateId : ()I
/*       */     //   415: sipush #903
/*       */     //   418: if_icmpne -> 655
/*       */     //   421: aload_3
/*       */     //   422: invokevirtual getTemplateId : ()I
/*       */     //   425: sipush #901
/*       */     //   428: if_icmpne -> 655
/*       */     //   431: aload #4
/*       */     //   433: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   436: sipush #637
/*       */     //   439: aaload
/*       */     //   440: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   445: pop
/*       */     //   446: aload #4
/*       */     //   448: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   451: sipush #640
/*       */     //   454: aaload
/*       */     //   455: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   460: pop
/*       */     //   461: goto -> 655
/*       */     //   464: lload #10
/*       */     //   466: aload_1
/*       */     //   467: invokevirtual getWurmId : ()J
/*       */     //   470: lcmp
/*       */     //   471: ifne -> 655
/*       */     //   474: aload_3
/*       */     //   475: invokevirtual isRoadMarker : ()Z
/*       */     //   478: ifne -> 655
/*       */     //   481: aload_3
/*       */     //   482: invokevirtual getTemplateId : ()I
/*       */     //   485: sipush #1396
/*       */     //   488: if_icmpne -> 559
/*       */     //   491: aload_1
/*       */     //   492: invokevirtual isOnSurface : ()Z
/*       */     //   495: ifeq -> 517
/*       */     //   498: getstatic com/wurmonline/server/Server.surfaceMesh : Lcom/wurmonline/mesh/MeshIO;
/*       */     //   501: aload_1
/*       */     //   502: invokevirtual getTileX : ()I
/*       */     //   505: aload_1
/*       */     //   506: invokevirtual getTileY : ()I
/*       */     //   509: invokevirtual getTile : (II)I
/*       */     //   512: istore #12
/*       */     //   514: goto -> 533
/*       */     //   517: getstatic com/wurmonline/server/Server.caveMesh : Lcom/wurmonline/mesh/MeshIO;
/*       */     //   520: aload_1
/*       */     //   521: invokevirtual getTileX : ()I
/*       */     //   524: aload_1
/*       */     //   525: invokevirtual getTileY : ()I
/*       */     //   528: invokevirtual getTile : (II)I
/*       */     //   531: istore #12
/*       */     //   533: iload #12
/*       */     //   535: invokestatic decodeHeight : (I)S
/*       */     //   538: ifge -> 556
/*       */     //   541: aload #4
/*       */     //   543: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   546: sipush #176
/*       */     //   549: aaload
/*       */     //   550: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   555: pop
/*       */     //   556: goto -> 655
/*       */     //   559: aload_2
/*       */     //   560: invokevirtual isAbility : ()Z
/*       */     //   563: ifne -> 573
/*       */     //   566: aload_3
/*       */     //   567: invokevirtual isAbility : ()Z
/*       */     //   570: ifeq -> 640
/*       */     //   573: aload_2
/*       */     //   574: aload_3
/*       */     //   575: if_acmpne -> 655
/*       */     //   578: aload #4
/*       */     //   580: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   583: sipush #640
/*       */     //   586: aaload
/*       */     //   587: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   592: pop
/*       */     //   593: aload #4
/*       */     //   595: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   598: dup
/*       */     //   599: sipush #176
/*       */     //   602: ldc_w 'Activate'
/*       */     //   605: new java/lang/StringBuilder
/*       */     //   608: dup
/*       */     //   609: invokespecial <init> : ()V
/*       */     //   612: ldc_w 'activating the '
/*       */     //   615: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   618: aload_3
/*       */     //   619: invokevirtual getName : ()Ljava/lang/String;
/*       */     //   622: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   625: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   628: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   631: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   636: pop
/*       */     //   637: goto -> 655
/*       */     //   640: aload #4
/*       */     //   642: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   645: sipush #176
/*       */     //   648: aaload
/*       */     //   649: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   654: pop
/*       */     //   655: aload_3
/*       */     //   656: invokevirtual getTemplateId : ()I
/*       */     //   659: sipush #835
/*       */     //   662: if_icmpne -> 807
/*       */     //   665: aload_1
/*       */     //   666: invokestatic getVillageForCreature : (Lcom/wurmonline/server/creatures/Creature;)Lcom/wurmonline/server/villages/Village;
/*       */     //   669: astore #12
/*       */     //   671: aload #12
/*       */     //   673: ifnull -> 792
/*       */     //   676: aload #12
/*       */     //   678: invokevirtual getId : ()I
/*       */     //   681: invokestatic containsAdForVillage : (I)Z
/*       */     //   684: ifne -> 732
/*       */     //   687: aload #12
/*       */     //   689: aload_1
/*       */     //   690: invokevirtual getWurmId : ()J
/*       */     //   693: invokevirtual getCitizen : (J)Lcom/wurmonline/server/villages/Citizen;
/*       */     //   696: astore #13
/*       */     //   698: aload #13
/*       */     //   700: ifnull -> 729
/*       */     //   703: aload #13
/*       */     //   705: invokevirtual getRole : ()Lcom/wurmonline/server/villages/VillageRole;
/*       */     //   708: invokevirtual mayInviteCitizens : ()Z
/*       */     //   711: ifeq -> 729
/*       */     //   714: aload #4
/*       */     //   716: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   719: sipush #598
/*       */     //   722: aaload
/*       */     //   723: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   728: pop
/*       */     //   729: goto -> 807
/*       */     //   732: aload #12
/*       */     //   734: aload_1
/*       */     //   735: invokevirtual getWurmId : ()J
/*       */     //   738: invokevirtual getCitizen : (J)Lcom/wurmonline/server/villages/Citizen;
/*       */     //   741: astore #13
/*       */     //   743: aload #13
/*       */     //   745: ifnull -> 789
/*       */     //   748: aload #13
/*       */     //   750: invokevirtual getRole : ()Lcom/wurmonline/server/villages/VillageRole;
/*       */     //   753: invokevirtual mayInviteCitizens : ()Z
/*       */     //   756: ifeq -> 789
/*       */     //   759: aload #4
/*       */     //   761: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   764: sipush #602
/*       */     //   767: aaload
/*       */     //   768: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   773: pop
/*       */     //   774: aload #4
/*       */     //   776: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   779: sipush #603
/*       */     //   782: aaload
/*       */     //   783: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   788: pop
/*       */     //   789: goto -> 807
/*       */     //   792: aload #4
/*       */     //   794: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   797: sipush #601
/*       */     //   800: aaload
/*       */     //   801: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   806: pop
/*       */     //   807: aload_3
/*       */     //   808: invokevirtual getTemplateId : ()I
/*       */     //   811: sipush #1271
/*       */     //   814: if_icmpne -> 912
/*       */     //   817: aload_3
/*       */     //   818: aload_1
/*       */     //   819: invokevirtual mayAccessHold : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   822: ifeq -> 846
/*       */     //   825: aload #4
/*       */     //   827: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   830: dup
/*       */     //   831: bipush #17
/*       */     //   833: ldc 'Read messages'
/*       */     //   835: ldc 'reading messages'
/*       */     //   837: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   840: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   845: pop
/*       */     //   846: aload_3
/*       */     //   847: aload_1
/*       */     //   848: invokevirtual mayPostNotices : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   851: ifne -> 862
/*       */     //   854: aload_3
/*       */     //   855: aload_1
/*       */     //   856: invokevirtual mayAddPMs : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   859: ifeq -> 912
/*       */     //   862: aload_2
/*       */     //   863: invokevirtual getAuxData : ()B
/*       */     //   866: ifne -> 912
/*       */     //   869: aload_2
/*       */     //   870: invokevirtual getTemplateId : ()I
/*       */     //   873: sipush #748
/*       */     //   876: if_icmpeq -> 889
/*       */     //   879: aload_2
/*       */     //   880: invokevirtual getTemplateId : ()I
/*       */     //   883: sipush #1272
/*       */     //   886: if_icmpne -> 912
/*       */     //   889: aload #4
/*       */     //   891: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   894: dup
/*       */     //   895: bipush #118
/*       */     //   897: ldc_w 'Post message'
/*       */     //   900: ldc_w 'posting message'
/*       */     //   903: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   906: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   911: pop
/*       */     //   912: aload_3
/*       */     //   913: invokevirtual getOwner : ()J
/*       */     //   916: lstore #8
/*       */     //   918: lload #8
/*       */     //   920: aload_1
/*       */     //   921: invokevirtual getWurmId : ()J
/*       */     //   924: lcmp
/*       */     //   925: ifne -> 2947
/*       */     //   928: aload_3
/*       */     //   929: invokevirtual getTopParent : ()J
/*       */     //   932: invokestatic getItem : (J)Lcom/wurmonline/server/items/Item;
/*       */     //   935: astore #10
/*       */     //   937: aload #10
/*       */     //   939: ifnull -> 1513
/*       */     //   942: aload_3
/*       */     //   943: invokevirtual isHollow : ()Z
/*       */     //   946: ifeq -> 1405
/*       */     //   949: aload_3
/*       */     //   950: invokevirtual isSealedByPlayer : ()Z
/*       */     //   953: ifne -> 1405
/*       */     //   956: aload_3
/*       */     //   957: invokevirtual getTemplateId : ()I
/*       */     //   960: sipush #1342
/*       */     //   963: if_icmpeq -> 1405
/*       */     //   966: aload_3
/*       */     //   967: invokevirtual getTopParent : ()J
/*       */     //   970: aload_1
/*       */     //   971: invokevirtual getInventory : ()Lcom/wurmonline/server/items/Item;
/*       */     //   974: invokevirtual getWurmId : ()J
/*       */     //   977: lcmp
/*       */     //   978: ifeq -> 989
/*       */     //   981: aload #10
/*       */     //   983: invokevirtual isBodyPart : ()Z
/*       */     //   986: ifeq -> 1405
/*       */     //   989: aload_3
/*       */     //   990: invokevirtual getLockId : ()J
/*       */     //   993: lstore #11
/*       */     //   995: lload #11
/*       */     //   997: ldc2_w -10
/*       */     //   1000: lcmp
/*       */     //   1001: ifne -> 1148
/*       */     //   1004: aload #4
/*       */     //   1006: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1009: sipush #568
/*       */     //   1012: aaload
/*       */     //   1013: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1018: pop
/*       */     //   1019: aload_3
/*       */     //   1020: invokevirtual isLockable : ()Z
/*       */     //   1023: ifeq -> 1060
/*       */     //   1026: aload_2
/*       */     //   1027: invokevirtual mayLockItems : ()Z
/*       */     //   1030: ifeq -> 1060
/*       */     //   1033: aload_3
/*       */     //   1034: aload_1
/*       */     //   1035: invokevirtual getWurmId : ()J
/*       */     //   1038: invokevirtual setNewOwner : (J)Z
/*       */     //   1041: pop
/*       */     //   1042: aload #4
/*       */     //   1044: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1047: sipush #161
/*       */     //   1050: aaload
/*       */     //   1051: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1056: pop
/*       */     //   1057: goto -> 1402
/*       */     //   1060: getstatic com/wurmonline/server/Features$Feature.TRANSFORM_RESOURCE_TILES : Lcom/wurmonline/server/Features$Feature;
/*       */     //   1063: invokevirtual isEnabled : ()Z
/*       */     //   1066: ifeq -> 1402
/*       */     //   1069: iload #5
/*       */     //   1071: sipush #1020
/*       */     //   1074: if_icmpne -> 1402
/*       */     //   1077: aload_2
/*       */     //   1078: invokevirtual isLiquid : ()Z
/*       */     //   1081: ifeq -> 1402
/*       */     //   1084: aload_2
/*       */     //   1085: aload_3
/*       */     //   1086: invokestatic getTileTransmutationLiquidAuxData : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)B
/*       */     //   1089: ifeq -> 1402
/*       */     //   1092: aload #4
/*       */     //   1094: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   1097: dup
/*       */     //   1098: bipush #-2
/*       */     //   1100: ldc_w 'Alchemy'
/*       */     //   1103: ldc_w 'Alchemy'
/*       */     //   1106: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   1109: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1114: pop
/*       */     //   1115: aload #4
/*       */     //   1117: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1120: sipush #283
/*       */     //   1123: aaload
/*       */     //   1124: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1129: pop
/*       */     //   1130: aload #4
/*       */     //   1132: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1135: sipush #285
/*       */     //   1138: aaload
/*       */     //   1139: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1144: pop
/*       */     //   1145: goto -> 1402
/*       */     //   1148: lload #11
/*       */     //   1150: invokestatic getItem : (J)Lcom/wurmonline/server/items/Item;
/*       */     //   1153: astore #13
/*       */     //   1155: aload_1
/*       */     //   1156: aload #13
/*       */     //   1158: invokevirtual hasKeyForLock : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   1161: ifne -> 1172
/*       */     //   1164: aload_3
/*       */     //   1165: aload_1
/*       */     //   1166: invokevirtual isOwner : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   1169: ifeq -> 1284
/*       */     //   1172: aload #4
/*       */     //   1174: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1177: sipush #568
/*       */     //   1180: aaload
/*       */     //   1181: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1186: pop
/*       */     //   1187: aload_3
/*       */     //   1188: aload_1
/*       */     //   1189: invokevirtual isOwner : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   1192: ifeq -> 1307
/*       */     //   1195: iconst_m1
/*       */     //   1196: istore #14
/*       */     //   1198: aload_2
/*       */     //   1199: invokevirtual isLock : ()Z
/*       */     //   1202: ifeq -> 1208
/*       */     //   1205: iinc #14, -1
/*       */     //   1208: aload #4
/*       */     //   1210: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   1213: dup
/*       */     //   1214: iload #14
/*       */     //   1216: i2s
/*       */     //   1217: aload_3
/*       */     //   1218: invokevirtual getActualName : ()Ljava/lang/String;
/*       */     //   1221: invokestatic raiseFirstLetter : (Ljava/lang/String;)Ljava/lang/String;
/*       */     //   1224: aload_3
/*       */     //   1225: invokevirtual getActualName : ()Ljava/lang/String;
/*       */     //   1228: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   1231: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1236: pop
/*       */     //   1237: aload_2
/*       */     //   1238: invokevirtual isLock : ()Z
/*       */     //   1241: ifeq -> 1267
/*       */     //   1244: aload #4
/*       */     //   1246: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   1249: dup
/*       */     //   1250: bipush #78
/*       */     //   1252: ldc_w 'Replace lock'
/*       */     //   1255: ldc_w 'replacing lock'
/*       */     //   1258: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   1261: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1266: pop
/*       */     //   1267: aload #4
/*       */     //   1269: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1272: bipush #102
/*       */     //   1274: aaload
/*       */     //   1275: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1280: pop
/*       */     //   1281: goto -> 1307
/*       */     //   1284: aload_3
/*       */     //   1285: aload_1
/*       */     //   1286: invokevirtual mayAccessHold : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   1289: ifeq -> 1307
/*       */     //   1292: aload #4
/*       */     //   1294: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1297: sipush #568
/*       */     //   1300: aaload
/*       */     //   1301: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1306: pop
/*       */     //   1307: iload #6
/*       */     //   1309: sipush #463
/*       */     //   1312: if_icmpne -> 1325
/*       */     //   1315: aload_1
/*       */     //   1316: aload_2
/*       */     //   1317: aload_3
/*       */     //   1318: aload #13
/*       */     //   1320: aload #4
/*       */     //   1322: invokestatic addLockPickEntry : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;Ljava/util/List;)V
/*       */     //   1325: goto -> 1402
/*       */     //   1328: astore #13
/*       */     //   1330: getstatic com/wurmonline/server/behaviours/ItemBehaviour.logger : Ljava/util/logging/Logger;
/*       */     //   1333: getstatic java/util/logging/Level.WARNING : Ljava/util/logging/Level;
/*       */     //   1336: new java/lang/StringBuilder
/*       */     //   1339: dup
/*       */     //   1340: invokespecial <init> : ()V
/*       */     //   1343: ldc 'No lock with id '
/*       */     //   1345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   1348: lload #11
/*       */     //   1350: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*       */     //   1353: ldc ', although the item has that.'
/*       */     //   1355: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   1358: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   1361: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
/*       */     //   1364: aload_2
/*       */     //   1365: invokevirtual isLock : ()Z
/*       */     //   1368: ifeq -> 1402
/*       */     //   1371: aload_3
/*       */     //   1372: aload_1
/*       */     //   1373: invokevirtual isOwner : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   1376: ifeq -> 1402
/*       */     //   1379: aload #4
/*       */     //   1381: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   1384: dup
/*       */     //   1385: bipush #78
/*       */     //   1387: ldc_w 'Replace lock'
/*       */     //   1390: ldc_w 'replacing lock'
/*       */     //   1393: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   1396: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1401: pop
/*       */     //   1402: goto -> 1513
/*       */     //   1405: iload #5
/*       */     //   1407: bipush #94
/*       */     //   1409: if_icmpeq -> 1458
/*       */     //   1412: iload #5
/*       */     //   1414: sipush #152
/*       */     //   1417: if_icmpeq -> 1458
/*       */     //   1420: iload #5
/*       */     //   1422: sipush #780
/*       */     //   1425: if_icmpeq -> 1458
/*       */     //   1428: iload #5
/*       */     //   1430: bipush #95
/*       */     //   1432: if_icmpeq -> 1458
/*       */     //   1435: iload #5
/*       */     //   1437: sipush #150
/*       */     //   1440: if_icmpeq -> 1458
/*       */     //   1443: iload #5
/*       */     //   1445: bipush #96
/*       */     //   1447: if_icmpeq -> 1458
/*       */     //   1450: iload #5
/*       */     //   1452: sipush #151
/*       */     //   1455: if_icmpne -> 1473
/*       */     //   1458: aload #4
/*       */     //   1460: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1463: sipush #939
/*       */     //   1466: aaload
/*       */     //   1467: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1472: pop
/*       */     //   1473: iload #5
/*       */     //   1475: sipush #1394
/*       */     //   1478: if_icmpne -> 1513
/*       */     //   1481: aload_2
/*       */     //   1482: invokevirtual isWeaponKnife : ()Z
/*       */     //   1485: ifne -> 1498
/*       */     //   1488: aload_2
/*       */     //   1489: invokevirtual getTemplateId : ()I
/*       */     //   1492: sipush #258
/*       */     //   1495: if_icmpne -> 1513
/*       */     //   1498: aload #4
/*       */     //   1500: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1503: sipush #942
/*       */     //   1506: aaload
/*       */     //   1507: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1512: pop
/*       */     //   1513: goto -> 1518
/*       */     //   1516: astore #10
/*       */     //   1518: aload_2
/*       */     //   1519: aload_3
/*       */     //   1520: invokevirtual equals : (Ljava/lang/Object;)Z
/*       */     //   1523: ifne -> 2893
/*       */     //   1526: iload #7
/*       */     //   1528: ifne -> 1543
/*       */     //   1531: aload #4
/*       */     //   1533: aload_1
/*       */     //   1534: aload_2
/*       */     //   1535: aload_3
/*       */     //   1536: invokestatic addCreationEntrys : (Ljava/util/List;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Ljava/util/List;
/*       */     //   1539: pop
/*       */     //   1540: iconst_1
/*       */     //   1541: istore #7
/*       */     //   1543: iload #5
/*       */     //   1545: sipush #1076
/*       */     //   1548: if_icmpne -> 1580
/*       */     //   1551: aload_2
/*       */     //   1552: invokevirtual isGem : ()Z
/*       */     //   1555: ifeq -> 1580
/*       */     //   1558: aload_3
/*       */     //   1559: invokevirtual getData1 : ()I
/*       */     //   1562: ifgt -> 1580
/*       */     //   1565: aload #4
/*       */     //   1567: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1570: sipush #463
/*       */     //   1573: aaload
/*       */     //   1574: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1579: pop
/*       */     //   1580: aload_2
/*       */     //   1581: invokevirtual isCombine : ()Z
/*       */     //   1584: ifeq -> 1619
/*       */     //   1587: iload #6
/*       */     //   1589: iload #5
/*       */     //   1591: if_icmpne -> 1619
/*       */     //   1594: aload_2
/*       */     //   1595: invokevirtual getRarity : ()B
/*       */     //   1598: aload_3
/*       */     //   1599: invokevirtual getRarity : ()B
/*       */     //   1602: if_icmpne -> 1619
/*       */     //   1605: aload #4
/*       */     //   1607: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1610: bipush #93
/*       */     //   1612: aaload
/*       */     //   1613: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1618: pop
/*       */     //   1619: iload #6
/*       */     //   1621: sipush #1193
/*       */     //   1624: if_icmpeq -> 1635
/*       */     //   1627: iload #6
/*       */     //   1629: sipush #417
/*       */     //   1632: if_icmpne -> 1678
/*       */     //   1635: iload #5
/*       */     //   1637: iload #6
/*       */     //   1639: if_icmpne -> 1678
/*       */     //   1642: aload_2
/*       */     //   1643: invokevirtual getRealTemplateId : ()I
/*       */     //   1646: aload_3
/*       */     //   1647: invokevirtual getRealTemplateId : ()I
/*       */     //   1650: if_icmpeq -> 1678
/*       */     //   1653: aload_2
/*       */     //   1654: invokevirtual getRarity : ()B
/*       */     //   1657: aload_3
/*       */     //   1658: invokevirtual getRarity : ()B
/*       */     //   1661: if_icmpne -> 1678
/*       */     //   1664: aload #4
/*       */     //   1666: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1669: bipush #93
/*       */     //   1671: aaload
/*       */     //   1672: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1677: pop
/*       */     //   1678: iload #5
/*       */     //   1680: sipush #765
/*       */     //   1683: if_icmpne -> 1714
/*       */     //   1686: iload #6
/*       */     //   1688: bipush #62
/*       */     //   1690: if_icmpeq -> 1700
/*       */     //   1693: aload_2
/*       */     //   1694: invokevirtual isWeaponCrush : ()Z
/*       */     //   1697: ifeq -> 1714
/*       */     //   1700: aload #4
/*       */     //   1702: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1705: bipush #54
/*       */     //   1707: aaload
/*       */     //   1708: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1713: pop
/*       */     //   1714: iload #5
/*       */     //   1716: sipush #1307
/*       */     //   1719: if_icmpne -> 1763
/*       */     //   1722: aload_3
/*       */     //   1723: invokevirtual getData1 : ()I
/*       */     //   1726: ifle -> 1763
/*       */     //   1729: iload #6
/*       */     //   1731: sipush #1307
/*       */     //   1734: if_icmpne -> 1763
/*       */     //   1737: aload_2
/*       */     //   1738: invokevirtual getRealTemplateId : ()I
/*       */     //   1741: aload_3
/*       */     //   1742: invokevirtual getRealTemplateId : ()I
/*       */     //   1745: if_icmpne -> 1763
/*       */     //   1748: aload #4
/*       */     //   1750: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1753: sipush #912
/*       */     //   1756: aaload
/*       */     //   1757: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1762: pop
/*       */     //   1763: aload_3
/*       */     //   1764: invokevirtual isLight : ()Z
/*       */     //   1767: ifne -> 1785
/*       */     //   1770: aload_3
/*       */     //   1771: invokevirtual isFire : ()Z
/*       */     //   1774: ifne -> 1785
/*       */     //   1777: iload #5
/*       */     //   1779: sipush #1243
/*       */     //   1782: if_icmpne -> 2016
/*       */     //   1785: aload_3
/*       */     //   1786: invokevirtual isOnFire : ()Z
/*       */     //   1789: ifeq -> 1862
/*       */     //   1792: aload_3
/*       */     //   1793: invokevirtual isAlwaysLit : ()Z
/*       */     //   1796: ifne -> 2016
/*       */     //   1799: aload_3
/*       */     //   1800: invokevirtual getTemplateId : ()I
/*       */     //   1803: sipush #729
/*       */     //   1806: if_icmpne -> 1845
/*       */     //   1809: aload #4
/*       */     //   1811: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   1814: dup
/*       */     //   1815: bipush #53
/*       */     //   1817: ldc 'Blow out'
/*       */     //   1819: ldc 'blowing out'
/*       */     //   1821: iconst_2
/*       */     //   1822: newarray int
/*       */     //   1824: dup
/*       */     //   1825: iconst_0
/*       */     //   1826: iconst_0
/*       */     //   1827: iastore
/*       */     //   1828: dup
/*       */     //   1829: iconst_1
/*       */     //   1830: bipush #43
/*       */     //   1832: iastore
/*       */     //   1833: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   1836: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1841: pop
/*       */     //   1842: goto -> 2016
/*       */     //   1845: aload #4
/*       */     //   1847: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1850: bipush #53
/*       */     //   1852: aaload
/*       */     //   1853: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1858: pop
/*       */     //   1859: goto -> 2016
/*       */     //   1862: aload_3
/*       */     //   1863: invokevirtual getTemplateId : ()I
/*       */     //   1866: sipush #1396
/*       */     //   1869: if_icmpne -> 1927
/*       */     //   1872: iload #6
/*       */     //   1874: sipush #143
/*       */     //   1877: if_icmpeq -> 1896
/*       */     //   1880: iload #6
/*       */     //   1882: sipush #176
/*       */     //   1885: if_icmpne -> 2016
/*       */     //   1888: aload_1
/*       */     //   1889: invokevirtual getPower : ()I
/*       */     //   1892: iconst_2
/*       */     //   1893: if_icmplt -> 2016
/*       */     //   1896: aload_3
/*       */     //   1897: invokevirtual getAuxData : ()B
/*       */     //   1900: ifle -> 2016
/*       */     //   1903: aload_3
/*       */     //   1904: invokevirtual isPlanted : ()Z
/*       */     //   1907: ifeq -> 2016
/*       */     //   1910: aload #4
/*       */     //   1912: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   1915: bipush #12
/*       */     //   1917: aaload
/*       */     //   1918: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   1923: pop
/*       */     //   1924: goto -> 2016
/*       */     //   1927: iload #6
/*       */     //   1929: sipush #143
/*       */     //   1932: if_icmpeq -> 1985
/*       */     //   1935: iload #6
/*       */     //   1937: sipush #176
/*       */     //   1940: if_icmpne -> 1951
/*       */     //   1943: aload_1
/*       */     //   1944: invokevirtual getPower : ()I
/*       */     //   1947: iconst_2
/*       */     //   1948: if_icmpge -> 1985
/*       */     //   1951: aload_3
/*       */     //   1952: invokevirtual getTemplateId : ()I
/*       */     //   1955: sipush #742
/*       */     //   1958: if_icmpeq -> 1985
/*       */     //   1961: aload_2
/*       */     //   1962: invokevirtual isBurnable : ()Z
/*       */     //   1965: ifeq -> 2016
/*       */     //   1968: aload_2
/*       */     //   1969: invokevirtual isIndestructible : ()Z
/*       */     //   1972: ifne -> 2016
/*       */     //   1975: aload_2
/*       */     //   1976: invokevirtual getTemperature : ()S
/*       */     //   1979: sipush #1000
/*       */     //   1982: if_icmple -> 2016
/*       */     //   1985: aload_3
/*       */     //   1986: invokevirtual getTemplateId : ()I
/*       */     //   1989: sipush #729
/*       */     //   1992: if_icmpne -> 2002
/*       */     //   1995: aload_3
/*       */     //   1996: invokevirtual getAuxData : ()B
/*       */     //   1999: ifle -> 2016
/*       */     //   2002: aload #4
/*       */     //   2004: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2007: bipush #12
/*       */     //   2009: aaload
/*       */     //   2010: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2015: pop
/*       */     //   2016: aload_2
/*       */     //   2017: invokevirtual isWeaponKnife : ()Z
/*       */     //   2020: ifeq -> 2074
/*       */     //   2023: iload #5
/*       */     //   2025: bipush #92
/*       */     //   2027: if_icmpeq -> 2059
/*       */     //   2030: iload #5
/*       */     //   2032: sipush #129
/*       */     //   2035: if_icmpeq -> 2059
/*       */     //   2038: aload_3
/*       */     //   2039: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   2042: invokevirtual getFoodGroup : ()I
/*       */     //   2045: sipush #1201
/*       */     //   2048: if_icmpne -> 2074
/*       */     //   2051: iload #5
/*       */     //   2053: sipush #369
/*       */     //   2056: if_icmpeq -> 2074
/*       */     //   2059: aload #4
/*       */     //   2061: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2064: sipush #225
/*       */     //   2067: aaload
/*       */     //   2068: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2073: pop
/*       */     //   2074: aload_2
/*       */     //   2075: invokevirtual isWeaponKnife : ()Z
/*       */     //   2078: ifne -> 2091
/*       */     //   2081: aload_2
/*       */     //   2082: invokevirtual getTemplateId : ()I
/*       */     //   2085: sipush #258
/*       */     //   2088: if_icmpne -> 2264
/*       */     //   2091: iload #5
/*       */     //   2093: sipush #729
/*       */     //   2096: if_icmpne -> 2126
/*       */     //   2099: aload #4
/*       */     //   2101: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2104: dup
/*       */     //   2105: sipush #225
/*       */     //   2108: ldc_w 'Cut up'
/*       */     //   2111: ldc_w 'cutting'
/*       */     //   2114: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   2117: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2122: pop
/*       */     //   2123: goto -> 2264
/*       */     //   2126: iload #5
/*       */     //   2128: sipush #203
/*       */     //   2131: if_icmpne -> 2161
/*       */     //   2134: aload #4
/*       */     //   2136: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2139: dup
/*       */     //   2140: sipush #225
/*       */     //   2143: ldc_w 'Slice up'
/*       */     //   2146: ldc_w 'cutting'
/*       */     //   2149: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   2152: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2157: pop
/*       */     //   2158: goto -> 2264
/*       */     //   2161: aload_3
/*       */     //   2162: invokevirtual isBulk : ()Z
/*       */     //   2165: ifne -> 2264
/*       */     //   2168: aload_3
/*       */     //   2169: invokevirtual isFood : ()Z
/*       */     //   2172: ifeq -> 2264
/*       */     //   2175: aload_3
/*       */     //   2176: invokevirtual isDrinkable : ()Z
/*       */     //   2179: ifne -> 2264
/*       */     //   2182: aload_3
/*       */     //   2183: invokevirtual isMeat : ()Z
/*       */     //   2186: ifne -> 2264
/*       */     //   2189: aload_3
/*       */     //   2190: invokevirtual isFish : ()Z
/*       */     //   2193: ifne -> 2264
/*       */     //   2196: aload_3
/*       */     //   2197: invokevirtual getTemplateId : ()I
/*       */     //   2200: sipush #729
/*       */     //   2203: if_icmpeq -> 2264
/*       */     //   2206: aload_3
/*       */     //   2207: invokevirtual getName : ()Ljava/lang/String;
/*       */     //   2210: ldc_w 'portion of '
/*       */     //   2213: invokevirtual contains : (Ljava/lang/CharSequence;)Z
/*       */     //   2216: ifeq -> 2232
/*       */     //   2219: aload_3
/*       */     //   2220: invokevirtual getName : ()Ljava/lang/String;
/*       */     //   2223: ldc_w 'slice of '
/*       */     //   2226: invokevirtual contains : (Ljava/lang/CharSequence;)Z
/*       */     //   2229: ifne -> 2264
/*       */     //   2232: iload #6
/*       */     //   2234: sipush #258
/*       */     //   2237: if_icmpne -> 2264
/*       */     //   2240: aload #4
/*       */     //   2242: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2245: dup
/*       */     //   2246: sipush #225
/*       */     //   2249: ldc_w 'Take Portion'
/*       */     //   2252: ldc_w 'taking'
/*       */     //   2255: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   2258: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2263: pop
/*       */     //   2264: iload #6
/*       */     //   2266: sipush #1255
/*       */     //   2269: if_icmpne -> 2301
/*       */     //   2272: aload_3
/*       */     //   2273: invokevirtual canBeSealedByPlayer : ()Z
/*       */     //   2276: ifeq -> 2301
/*       */     //   2279: aload_3
/*       */     //   2280: invokevirtual isSealedByPlayer : ()Z
/*       */     //   2283: ifne -> 2301
/*       */     //   2286: aload #4
/*       */     //   2288: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2291: sipush #739
/*       */     //   2294: aaload
/*       */     //   2295: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2300: pop
/*       */     //   2301: aload_3
/*       */     //   2302: invokevirtual isLiquid : ()Z
/*       */     //   2305: ifne -> 2343
/*       */     //   2308: aload_3
/*       */     //   2309: invokevirtual isWrapped : ()Z
/*       */     //   2312: ifeq -> 2343
/*       */     //   2315: aload #4
/*       */     //   2317: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2320: dup
/*       */     //   2321: sipush #740
/*       */     //   2324: ldc 'Unwrap'
/*       */     //   2326: ldc 'unwrapping'
/*       */     //   2328: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   2331: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   2334: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2339: pop
/*       */     //   2340: goto -> 2513
/*       */     //   2343: aload_3
/*       */     //   2344: invokevirtual isRaw : ()Z
/*       */     //   2347: ifeq -> 2392
/*       */     //   2350: aload_3
/*       */     //   2351: invokevirtual canBeRawWrapped : ()Z
/*       */     //   2354: ifeq -> 2392
/*       */     //   2357: aload_3
/*       */     //   2358: invokevirtual isPStateNone : ()Z
/*       */     //   2361: ifeq -> 2392
/*       */     //   2364: aload #4
/*       */     //   2366: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2369: dup
/*       */     //   2370: sipush #739
/*       */     //   2373: ldc 'Wrap'
/*       */     //   2375: ldc 'wrapping'
/*       */     //   2377: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   2380: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   2383: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2388: pop
/*       */     //   2389: goto -> 2513
/*       */     //   2392: iload #6
/*       */     //   2394: sipush #748
/*       */     //   2397: if_icmpeq -> 2408
/*       */     //   2400: iload #6
/*       */     //   2402: sipush #1272
/*       */     //   2405: if_icmpne -> 2458
/*       */     //   2408: aload_3
/*       */     //   2409: invokevirtual canBePapyrusWrapped : ()Z
/*       */     //   2412: ifeq -> 2458
/*       */     //   2415: aload_3
/*       */     //   2416: invokevirtual isWrapped : ()Z
/*       */     //   2419: ifne -> 2458
/*       */     //   2422: aload_2
/*       */     //   2423: invokevirtual getAuxData : ()B
/*       */     //   2426: iconst_2
/*       */     //   2427: if_icmpne -> 2458
/*       */     //   2430: aload #4
/*       */     //   2432: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2435: dup
/*       */     //   2436: sipush #739
/*       */     //   2439: ldc 'Wrap'
/*       */     //   2441: ldc 'wrapping'
/*       */     //   2443: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   2446: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   2449: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2454: pop
/*       */     //   2455: goto -> 2513
/*       */     //   2458: iload #6
/*       */     //   2460: sipush #213
/*       */     //   2463: if_icmpeq -> 2474
/*       */     //   2466: iload #6
/*       */     //   2468: sipush #926
/*       */     //   2471: if_icmpne -> 2513
/*       */     //   2474: aload_3
/*       */     //   2475: invokevirtual canBeClothWrapped : ()Z
/*       */     //   2478: ifeq -> 2513
/*       */     //   2481: aload_3
/*       */     //   2482: invokevirtual isWrapped : ()Z
/*       */     //   2485: ifne -> 2513
/*       */     //   2488: aload #4
/*       */     //   2490: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2493: dup
/*       */     //   2494: sipush #739
/*       */     //   2497: ldc 'Wrap'
/*       */     //   2499: ldc 'wrapping'
/*       */     //   2501: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   2504: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   2507: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2512: pop
/*       */     //   2513: aload_3
/*       */     //   2514: invokevirtual isSealedByPlayer : ()Z
/*       */     //   2517: ifeq -> 2631
/*       */     //   2520: aload_3
/*       */     //   2521: invokevirtual getTemplateId : ()I
/*       */     //   2524: sipush #1309
/*       */     //   2527: if_icmpeq -> 2631
/*       */     //   2530: aload_3
/*       */     //   2531: invokevirtual isCrate : ()Z
/*       */     //   2534: ifne -> 2631
/*       */     //   2537: aconst_null
/*       */     //   2538: astore #10
/*       */     //   2540: aload_3
/*       */     //   2541: invokevirtual getItemsAsArray : ()[Lcom/wurmonline/server/items/Item;
/*       */     //   2544: astore #11
/*       */     //   2546: aload #11
/*       */     //   2548: arraylength
/*       */     //   2549: istore #12
/*       */     //   2551: iconst_0
/*       */     //   2552: istore #13
/*       */     //   2554: iload #13
/*       */     //   2556: iload #12
/*       */     //   2558: if_icmpge -> 2589
/*       */     //   2561: aload #11
/*       */     //   2563: iload #13
/*       */     //   2565: aaload
/*       */     //   2566: astore #14
/*       */     //   2568: aload #14
/*       */     //   2570: invokevirtual isLiquid : ()Z
/*       */     //   2573: ifeq -> 2583
/*       */     //   2576: aload #14
/*       */     //   2578: astore #10
/*       */     //   2580: goto -> 2589
/*       */     //   2583: iinc #13, 1
/*       */     //   2586: goto -> 2554
/*       */     //   2589: aload #10
/*       */     //   2591: ifnull -> 2602
/*       */     //   2594: aload #10
/*       */     //   2596: invokevirtual isFermenting : ()Z
/*       */     //   2599: ifne -> 2617
/*       */     //   2602: aload #4
/*       */     //   2604: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2607: sipush #740
/*       */     //   2610: aaload
/*       */     //   2611: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2616: pop
/*       */     //   2617: aload #4
/*       */     //   2619: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2622: bipush #19
/*       */     //   2624: aaload
/*       */     //   2625: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2630: pop
/*       */     //   2631: aload_2
/*       */     //   2632: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   2635: invokevirtual isRune : ()Z
/*       */     //   2638: ifeq -> 2893
/*       */     //   2641: aload_1
/*       */     //   2642: invokevirtual getSoulDepth : ()Lcom/wurmonline/server/skills/Skill;
/*       */     //   2645: astore #10
/*       */     //   2647: ldc_w 20.0
/*       */     //   2650: aload_2
/*       */     //   2651: invokevirtual getDamage : ()F
/*       */     //   2654: fadd
/*       */     //   2655: f2d
/*       */     //   2656: aload_2
/*       */     //   2657: invokevirtual getCurrentQualityLevel : ()F
/*       */     //   2660: aload_2
/*       */     //   2661: invokevirtual getRarity : ()B
/*       */     //   2664: i2f
/*       */     //   2665: fadd
/*       */     //   2666: f2d
/*       */     //   2667: ldc2_w 45.0
/*       */     //   2670: dsub
/*       */     //   2671: dsub
/*       */     //   2672: dstore #11
/*       */     //   2674: aload #10
/*       */     //   2676: dload #11
/*       */     //   2678: aconst_null
/*       */     //   2679: aload_2
/*       */     //   2680: invokevirtual getCurrentQualityLevel : ()F
/*       */     //   2683: f2d
/*       */     //   2684: invokevirtual getChance : (DLcom/wurmonline/server/items/Item;D)D
/*       */     //   2687: dstore #13
/*       */     //   2689: aload_2
/*       */     //   2690: invokestatic isEnchantRune : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   2693: ifeq -> 2827
/*       */     //   2696: aload_2
/*       */     //   2697: aload_3
/*       */     //   2698: invokestatic canApplyRuneTo : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Z
/*       */     //   2701: ifeq -> 2827
/*       */     //   2704: aload_3
/*       */     //   2705: invokestatic getNumberOfRuneEffects : (Lcom/wurmonline/server/items/Item;)I
/*       */     //   2708: ifne -> 2765
/*       */     //   2711: aload #4
/*       */     //   2713: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2716: dup
/*       */     //   2717: sipush #945
/*       */     //   2720: new java/lang/StringBuilder
/*       */     //   2723: dup
/*       */     //   2724: invokespecial <init> : ()V
/*       */     //   2727: ldc_w 'Attach Rune: '
/*       */     //   2730: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   2733: dload #13
/*       */     //   2735: invokevirtual append : (D)Ljava/lang/StringBuilder;
/*       */     //   2738: ldc_w '%'
/*       */     //   2741: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   2744: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   2747: ldc_w 'attaching rune'
/*       */     //   2750: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   2753: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   2756: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2761: pop
/*       */     //   2762: goto -> 2893
/*       */     //   2765: aload_3
/*       */     //   2766: invokestatic getNumberOfRuneEffects : (Lcom/wurmonline/server/items/Item;)I
/*       */     //   2769: iconst_1
/*       */     //   2770: if_icmpne -> 2893
/*       */     //   2773: aload #4
/*       */     //   2775: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2778: dup
/*       */     //   2779: sipush #945
/*       */     //   2782: new java/lang/StringBuilder
/*       */     //   2785: dup
/*       */     //   2786: invokespecial <init> : ()V
/*       */     //   2789: ldc_w 'Replace Rune: '
/*       */     //   2792: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   2795: dload #13
/*       */     //   2797: invokevirtual append : (D)Ljava/lang/StringBuilder;
/*       */     //   2800: ldc_w '%'
/*       */     //   2803: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   2806: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   2809: ldc_w 'replacing rune'
/*       */     //   2812: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   2815: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   2818: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2823: pop
/*       */     //   2824: goto -> 2893
/*       */     //   2827: aload_2
/*       */     //   2828: invokestatic isSingleUseRune : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   2831: ifeq -> 2893
/*       */     //   2834: aload_2
/*       */     //   2835: aload_3
/*       */     //   2836: invokestatic isCorrectTarget : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Z
/*       */     //   2839: ifeq -> 2893
/*       */     //   2842: aload #4
/*       */     //   2844: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   2847: dup
/*       */     //   2848: sipush #945
/*       */     //   2851: new java/lang/StringBuilder
/*       */     //   2854: dup
/*       */     //   2855: invokespecial <init> : ()V
/*       */     //   2858: ldc_w 'Use Rune: '
/*       */     //   2861: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   2864: dload #13
/*       */     //   2866: invokevirtual append : (D)Ljava/lang/StringBuilder;
/*       */     //   2869: ldc_w '%'
/*       */     //   2872: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   2875: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   2878: ldc_w 'using rune'
/*       */     //   2881: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   2884: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   2887: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2892: pop
/*       */     //   2893: iload #5
/*       */     //   2895: sipush #682
/*       */     //   2898: if_icmpne -> 2925
/*       */     //   2901: getstatic com/wurmonline/server/Servers.localServer : Lcom/wurmonline/server/ServerEntry;
/*       */     //   2904: getfield PVPSERVER : Z
/*       */     //   2907: ifeq -> 2925
/*       */     //   2910: aload #4
/*       */     //   2912: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2915: sipush #480
/*       */     //   2918: aaload
/*       */     //   2919: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2924: pop
/*       */     //   2925: iload #5
/*       */     //   2927: sipush #1024
/*       */     //   2930: if_icmpne -> 2947
/*       */     //   2933: aload #4
/*       */     //   2935: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2938: bipush #115
/*       */     //   2940: aaload
/*       */     //   2941: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2946: pop
/*       */     //   2947: aload_2
/*       */     //   2948: invokevirtual isPuppet : ()Z
/*       */     //   2951: ifeq -> 2984
/*       */     //   2954: aload_3
/*       */     //   2955: invokevirtual isPuppet : ()Z
/*       */     //   2958: ifeq -> 2984
/*       */     //   2961: aload_2
/*       */     //   2962: aload_3
/*       */     //   2963: invokevirtual equals : (Ljava/lang/Object;)Z
/*       */     //   2966: ifne -> 2984
/*       */     //   2969: aload #4
/*       */     //   2971: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   2974: sipush #397
/*       */     //   2977: aaload
/*       */     //   2978: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   2983: pop
/*       */     //   2984: goto -> 4804
/*       */     //   2987: astore #10
/*       */     //   2989: aload_2
/*       */     //   2990: invokevirtual getTemplateId : ()I
/*       */     //   2993: bipush #25
/*       */     //   2995: if_icmpne -> 3017
/*       */     //   2998: aload_3
/*       */     //   2999: invokevirtual getAuxData : ()B
/*       */     //   3002: bipush #30
/*       */     //   3004: if_icmpne -> 3017
/*       */     //   3007: aload_3
/*       */     //   3008: invokevirtual getTemplateId : ()I
/*       */     //   3011: sipush #180
/*       */     //   3014: if_icmpeq -> 3101
/*       */     //   3017: aload_2
/*       */     //   3018: invokevirtual getTemplateId : ()I
/*       */     //   3021: bipush #25
/*       */     //   3023: if_icmpne -> 3045
/*       */     //   3026: aload_3
/*       */     //   3027: invokevirtual getAuxData : ()B
/*       */     //   3030: bipush #30
/*       */     //   3032: if_icmpne -> 3045
/*       */     //   3035: aload_3
/*       */     //   3036: invokevirtual getTemplateId : ()I
/*       */     //   3039: sipush #178
/*       */     //   3042: if_icmpeq -> 3101
/*       */     //   3045: aload_2
/*       */     //   3046: invokevirtual getTemplateId : ()I
/*       */     //   3049: bipush #25
/*       */     //   3051: if_icmpne -> 3073
/*       */     //   3054: aload_3
/*       */     //   3055: invokevirtual getAuxData : ()B
/*       */     //   3058: bipush #30
/*       */     //   3060: if_icmpne -> 3073
/*       */     //   3063: aload_3
/*       */     //   3064: invokevirtual getTemplateId : ()I
/*       */     //   3067: sipush #1023
/*       */     //   3070: if_icmpeq -> 3101
/*       */     //   3073: aload_2
/*       */     //   3074: invokevirtual getTemplateId : ()I
/*       */     //   3077: bipush #25
/*       */     //   3079: if_icmpne -> 3129
/*       */     //   3082: aload_3
/*       */     //   3083: invokevirtual getAuxData : ()B
/*       */     //   3086: bipush #30
/*       */     //   3088: if_icmpne -> 3129
/*       */     //   3091: aload_3
/*       */     //   3092: invokevirtual getTemplateId : ()I
/*       */     //   3095: sipush #1028
/*       */     //   3098: if_icmpne -> 3129
/*       */     //   3101: aload_1
/*       */     //   3102: aload_3
/*       */     //   3103: iconst_4
/*       */     //   3104: invokestatic getBlockerBetween : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;I)Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   3107: astore #11
/*       */     //   3109: aload #11
/*       */     //   3111: ifnonnull -> 3129
/*       */     //   3114: aload #4
/*       */     //   3116: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3119: sipush #922
/*       */     //   3122: aaload
/*       */     //   3123: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3128: pop
/*       */     //   3129: aload_2
/*       */     //   3130: invokevirtual isWeaponBow : ()Z
/*       */     //   3133: ifeq -> 3159
/*       */     //   3136: iload #5
/*       */     //   3138: sipush #458
/*       */     //   3141: if_icmpne -> 3159
/*       */     //   3144: aload #4
/*       */     //   3146: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3149: sipush #134
/*       */     //   3152: aaload
/*       */     //   3153: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3158: pop
/*       */     //   3159: iload #5
/*       */     //   3161: sipush #652
/*       */     //   3164: if_icmpne -> 3182
/*       */     //   3167: aload #4
/*       */     //   3169: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3172: sipush #214
/*       */     //   3175: aaload
/*       */     //   3176: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3181: pop
/*       */     //   3182: aload_3
/*       */     //   3183: aload_1
/*       */     //   3184: invokevirtual isNoTake : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   3187: ifne -> 3197
/*       */     //   3190: aload_3
/*       */     //   3191: invokevirtual isUseOnGroundOnly : ()Z
/*       */     //   3194: ifeq -> 4272
/*       */     //   3197: aload_3
/*       */     //   3198: invokevirtual isRiftAltar : ()Z
/*       */     //   3201: ifeq -> 3234
/*       */     //   3204: iload #6
/*       */     //   3206: sipush #764
/*       */     //   3209: if_icmpeq -> 3220
/*       */     //   3212: iload #6
/*       */     //   3214: sipush #1096
/*       */     //   3217: if_icmpne -> 3234
/*       */     //   3220: aload #4
/*       */     //   3222: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3225: bipush #117
/*       */     //   3227: aaload
/*       */     //   3228: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3233: pop
/*       */     //   3234: aload_3
/*       */     //   3235: invokevirtual getWurmId : ()J
/*       */     //   3238: aload_1
/*       */     //   3239: invokevirtual getVehicle : ()J
/*       */     //   3242: lcmp
/*       */     //   3243: ifeq -> 3303
/*       */     //   3246: aload_1
/*       */     //   3247: aload_3
/*       */     //   3248: invokevirtual getPosX : ()F
/*       */     //   3251: aload_3
/*       */     //   3252: invokevirtual getPosY : ()F
/*       */     //   3255: aload_3
/*       */     //   3256: invokevirtual getPosZ : ()F
/*       */     //   3259: aload_3
/*       */     //   3260: invokevirtual isVehicle : ()Z
/*       */     //   3263: ifeq -> 3295
/*       */     //   3266: aload_3
/*       */     //   3267: invokevirtual isTent : ()Z
/*       */     //   3270: ifne -> 3295
/*       */     //   3273: aload_3
/*       */     //   3274: invokevirtual isChair : ()Z
/*       */     //   3277: ifne -> 3295
/*       */     //   3280: iconst_4
/*       */     //   3281: aload_3
/*       */     //   3282: invokevirtual getSizeZ : ()I
/*       */     //   3285: bipush #100
/*       */     //   3287: idiv
/*       */     //   3288: invokestatic max : (II)I
/*       */     //   3291: i2f
/*       */     //   3292: goto -> 3297
/*       */     //   3295: ldc 4.0
/*       */     //   3297: invokevirtual isWithinDistanceTo : (FFFF)Z
/*       */     //   3300: ifeq -> 4804
/*       */     //   3303: aload_3
/*       */     //   3304: invokevirtual getWurmId : ()J
/*       */     //   3307: aload_1
/*       */     //   3308: invokevirtual getVehicle : ()J
/*       */     //   3311: lcmp
/*       */     //   3312: ifne -> 3319
/*       */     //   3315: aconst_null
/*       */     //   3316: goto -> 3325
/*       */     //   3319: aload_1
/*       */     //   3320: aload_3
/*       */     //   3321: iconst_4
/*       */     //   3322: invokestatic getBlockerBetween : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;I)Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   3325: astore #11
/*       */     //   3327: aload #11
/*       */     //   3329: ifnonnull -> 4240
/*       */     //   3332: iload #5
/*       */     //   3334: sipush #538
/*       */     //   3337: if_icmpne -> 3367
/*       */     //   3340: getstatic com/wurmonline/server/Servers.localServer : Lcom/wurmonline/server/ServerEntry;
/*       */     //   3343: invokevirtual isChallengeServer : ()Z
/*       */     //   3346: ifne -> 3367
/*       */     //   3349: aload #4
/*       */     //   3351: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3354: sipush #353
/*       */     //   3357: aaload
/*       */     //   3358: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3363: pop
/*       */     //   3364: goto -> 3471
/*       */     //   3367: iload #5
/*       */     //   3369: sipush #726
/*       */     //   3372: if_icmpne -> 3471
/*       */     //   3375: aload_1
/*       */     //   3376: invokevirtual getKingdomId : ()B
/*       */     //   3379: aload_3
/*       */     //   3380: invokevirtual getAuxData : ()B
/*       */     //   3383: if_icmpne -> 3449
/*       */     //   3386: aload_3
/*       */     //   3387: invokevirtual getAuxData : ()B
/*       */     //   3390: invokestatic getKing : (B)Lcom/wurmonline/server/kingdom/King;
/*       */     //   3393: astore #12
/*       */     //   3395: aload #12
/*       */     //   3397: ifnull -> 3449
/*       */     //   3400: aload #12
/*       */     //   3402: invokevirtual mayBeChallenged : ()Z
/*       */     //   3405: ifeq -> 3426
/*       */     //   3408: aload #4
/*       */     //   3410: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3413: sipush #488
/*       */     //   3416: aaload
/*       */     //   3417: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3422: pop
/*       */     //   3423: goto -> 3449
/*       */     //   3426: aload #12
/*       */     //   3428: invokevirtual hasFailedAllChallenges : ()Z
/*       */     //   3431: ifeq -> 3449
/*       */     //   3434: aload #4
/*       */     //   3436: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3439: sipush #487
/*       */     //   3442: aaload
/*       */     //   3443: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3448: pop
/*       */     //   3449: aload_1
/*       */     //   3450: invokevirtual getPower : ()I
/*       */     //   3453: iconst_3
/*       */     //   3454: if_icmplt -> 3471
/*       */     //   3457: aload #4
/*       */     //   3459: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3462: bipush #118
/*       */     //   3464: aaload
/*       */     //   3465: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3470: pop
/*       */     //   3471: iload #7
/*       */     //   3473: ifne -> 3488
/*       */     //   3476: aload #4
/*       */     //   3478: aload_1
/*       */     //   3479: aload_2
/*       */     //   3480: aload_3
/*       */     //   3481: invokestatic addCreationEntrys : (Ljava/util/List;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Ljava/util/List;
/*       */     //   3484: pop
/*       */     //   3485: iconst_1
/*       */     //   3486: istore #7
/*       */     //   3488: aload_3
/*       */     //   3489: invokevirtual isSealedByPlayer : ()Z
/*       */     //   3492: ifeq -> 3606
/*       */     //   3495: aload_3
/*       */     //   3496: invokevirtual getTemplateId : ()I
/*       */     //   3499: sipush #1309
/*       */     //   3502: if_icmpeq -> 3606
/*       */     //   3505: aload_3
/*       */     //   3506: invokevirtual isCrate : ()Z
/*       */     //   3509: ifne -> 3606
/*       */     //   3512: aconst_null
/*       */     //   3513: astore #12
/*       */     //   3515: aload_3
/*       */     //   3516: invokevirtual getItemsAsArray : ()[Lcom/wurmonline/server/items/Item;
/*       */     //   3519: astore #13
/*       */     //   3521: aload #13
/*       */     //   3523: arraylength
/*       */     //   3524: istore #14
/*       */     //   3526: iconst_0
/*       */     //   3527: istore #15
/*       */     //   3529: iload #15
/*       */     //   3531: iload #14
/*       */     //   3533: if_icmpge -> 3564
/*       */     //   3536: aload #13
/*       */     //   3538: iload #15
/*       */     //   3540: aaload
/*       */     //   3541: astore #16
/*       */     //   3543: aload #16
/*       */     //   3545: invokevirtual isLiquid : ()Z
/*       */     //   3548: ifeq -> 3558
/*       */     //   3551: aload #16
/*       */     //   3553: astore #12
/*       */     //   3555: goto -> 3564
/*       */     //   3558: iinc #15, 1
/*       */     //   3561: goto -> 3529
/*       */     //   3564: aload #12
/*       */     //   3566: ifnull -> 3577
/*       */     //   3569: aload #12
/*       */     //   3571: invokevirtual isFermenting : ()Z
/*       */     //   3574: ifne -> 3592
/*       */     //   3577: aload #4
/*       */     //   3579: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3582: sipush #740
/*       */     //   3585: aaload
/*       */     //   3586: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3591: pop
/*       */     //   3592: aload #4
/*       */     //   3594: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3597: bipush #19
/*       */     //   3599: aaload
/*       */     //   3600: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3605: pop
/*       */     //   3606: iload #6
/*       */     //   3608: sipush #561
/*       */     //   3611: if_icmpne -> 3643
/*       */     //   3614: aload_3
/*       */     //   3615: invokevirtual canBePeggedByPlayer : ()Z
/*       */     //   3618: ifeq -> 3643
/*       */     //   3621: aload_3
/*       */     //   3622: invokevirtual isSealedByPlayer : ()Z
/*       */     //   3625: ifne -> 3643
/*       */     //   3628: aload #4
/*       */     //   3630: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   3633: sipush #739
/*       */     //   3636: aaload
/*       */     //   3637: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3642: pop
/*       */     //   3643: aload_2
/*       */     //   3644: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   3647: invokevirtual isRune : ()Z
/*       */     //   3650: ifeq -> 3926
/*       */     //   3653: aload_1
/*       */     //   3654: invokevirtual getSoulDepth : ()Lcom/wurmonline/server/skills/Skill;
/*       */     //   3657: astore #12
/*       */     //   3659: ldc_w 20.0
/*       */     //   3662: aload_2
/*       */     //   3663: invokevirtual getDamage : ()F
/*       */     //   3666: fadd
/*       */     //   3667: f2d
/*       */     //   3668: aload_2
/*       */     //   3669: invokevirtual getCurrentQualityLevel : ()F
/*       */     //   3672: aload_2
/*       */     //   3673: invokevirtual getRarity : ()B
/*       */     //   3676: i2f
/*       */     //   3677: fadd
/*       */     //   3678: f2d
/*       */     //   3679: ldc2_w 45.0
/*       */     //   3682: dsub
/*       */     //   3683: dsub
/*       */     //   3684: dstore #13
/*       */     //   3686: aload #12
/*       */     //   3688: dload #13
/*       */     //   3690: aconst_null
/*       */     //   3691: aload_2
/*       */     //   3692: invokevirtual getCurrentQualityLevel : ()F
/*       */     //   3695: f2d
/*       */     //   3696: invokevirtual getChance : (DLcom/wurmonline/server/items/Item;D)D
/*       */     //   3699: dstore #15
/*       */     //   3701: aload_2
/*       */     //   3702: invokestatic isEnchantRune : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   3705: ifeq -> 3839
/*       */     //   3708: aload_2
/*       */     //   3709: aload_3
/*       */     //   3710: invokestatic canApplyRuneTo : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Z
/*       */     //   3713: ifeq -> 3839
/*       */     //   3716: aload_3
/*       */     //   3717: invokestatic getNumberOfRuneEffects : (Lcom/wurmonline/server/items/Item;)I
/*       */     //   3720: ifne -> 3777
/*       */     //   3723: aload #4
/*       */     //   3725: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   3728: dup
/*       */     //   3729: sipush #945
/*       */     //   3732: new java/lang/StringBuilder
/*       */     //   3735: dup
/*       */     //   3736: invokespecial <init> : ()V
/*       */     //   3739: ldc_w 'Attach Rune: '
/*       */     //   3742: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   3745: dload #15
/*       */     //   3747: invokevirtual append : (D)Ljava/lang/StringBuilder;
/*       */     //   3750: ldc_w '%'
/*       */     //   3753: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   3756: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   3759: ldc_w 'attaching rune'
/*       */     //   3762: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   3765: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   3768: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3773: pop
/*       */     //   3774: goto -> 3926
/*       */     //   3777: aload_3
/*       */     //   3778: invokestatic getNumberOfRuneEffects : (Lcom/wurmonline/server/items/Item;)I
/*       */     //   3781: iconst_1
/*       */     //   3782: if_icmpne -> 3926
/*       */     //   3785: aload #4
/*       */     //   3787: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   3790: dup
/*       */     //   3791: sipush #945
/*       */     //   3794: new java/lang/StringBuilder
/*       */     //   3797: dup
/*       */     //   3798: invokespecial <init> : ()V
/*       */     //   3801: ldc_w 'Replace Rune: '
/*       */     //   3804: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   3807: dload #15
/*       */     //   3809: invokevirtual append : (D)Ljava/lang/StringBuilder;
/*       */     //   3812: ldc_w '%'
/*       */     //   3815: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   3818: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   3821: ldc_w 'replacing rune'
/*       */     //   3824: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   3827: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   3830: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3835: pop
/*       */     //   3836: goto -> 3926
/*       */     //   3839: aload_2
/*       */     //   3840: invokestatic isSingleUseRune : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   3843: ifeq -> 3926
/*       */     //   3846: aload_2
/*       */     //   3847: aload_3
/*       */     //   3848: invokestatic isCorrectTarget : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Z
/*       */     //   3851: ifeq -> 3926
/*       */     //   3854: aload_3
/*       */     //   3855: invokevirtual isInventory : ()Z
/*       */     //   3858: ifne -> 3926
/*       */     //   3861: aload_3
/*       */     //   3862: invokevirtual isTemporary : ()Z
/*       */     //   3865: ifne -> 3926
/*       */     //   3868: aload_3
/*       */     //   3869: invokevirtual isNotSpellTarget : ()Z
/*       */     //   3872: ifne -> 3926
/*       */     //   3875: aload #4
/*       */     //   3877: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   3880: dup
/*       */     //   3881: sipush #945
/*       */     //   3884: new java/lang/StringBuilder
/*       */     //   3887: dup
/*       */     //   3888: invokespecial <init> : ()V
/*       */     //   3891: ldc_w 'Use Rune: '
/*       */     //   3894: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   3897: dload #15
/*       */     //   3899: invokevirtual append : (D)Ljava/lang/StringBuilder;
/*       */     //   3902: ldc_w '%'
/*       */     //   3905: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   3908: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   3911: ldc_w 'using rune'
/*       */     //   3914: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   3917: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   3920: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   3925: pop
/*       */     //   3926: iload #6
/*       */     //   3928: sipush #143
/*       */     //   3931: if_icmpeq -> 3974
/*       */     //   3934: iload #6
/*       */     //   3936: sipush #176
/*       */     //   3939: if_icmpne -> 3950
/*       */     //   3942: aload_1
/*       */     //   3943: invokevirtual getPower : ()I
/*       */     //   3946: iconst_2
/*       */     //   3947: if_icmpge -> 3974
/*       */     //   3950: aload_2
/*       */     //   3951: invokevirtual isBurnable : ()Z
/*       */     //   3954: ifeq -> 4043
/*       */     //   3957: aload_2
/*       */     //   3958: invokevirtual isIndestructible : ()Z
/*       */     //   3961: ifne -> 4043
/*       */     //   3964: aload_2
/*       */     //   3965: invokevirtual getTemperature : ()S
/*       */     //   3968: sipush #1000
/*       */     //   3971: if_icmple -> 4043
/*       */     //   3974: iload #5
/*       */     //   3976: sipush #180
/*       */     //   3979: if_icmpeq -> 4053
/*       */     //   3982: iload #5
/*       */     //   3984: sipush #178
/*       */     //   3987: if_icmpeq -> 4053
/*       */     //   3990: iload #5
/*       */     //   3992: bipush #37
/*       */     //   3994: if_icmpeq -> 4053
/*       */     //   3997: iload #5
/*       */     //   3999: bipush #74
/*       */     //   4001: if_icmpeq -> 4053
/*       */     //   4004: iload #5
/*       */     //   4006: sipush #889
/*       */     //   4009: if_icmpeq -> 4053
/*       */     //   4012: iload #5
/*       */     //   4014: sipush #1023
/*       */     //   4017: if_icmpeq -> 4053
/*       */     //   4020: iload #5
/*       */     //   4022: sipush #1028
/*       */     //   4025: if_icmpeq -> 4053
/*       */     //   4028: aload_3
/*       */     //   4029: invokevirtual isLight : ()Z
/*       */     //   4032: ifne -> 4053
/*       */     //   4035: iload #5
/*       */     //   4037: sipush #1178
/*       */     //   4040: if_icmpeq -> 4053
/*       */     //   4043: aload_3
/*       */     //   4044: invokevirtual getTemplateId : ()I
/*       */     //   4047: sipush #742
/*       */     //   4050: if_icmpne -> 4202
/*       */     //   4053: aload_3
/*       */     //   4054: invokevirtual isOnFire : ()Z
/*       */     //   4057: ifne -> 4132
/*       */     //   4060: aload_3
/*       */     //   4061: invokevirtual getTemplateId : ()I
/*       */     //   4064: sipush #729
/*       */     //   4067: if_icmpne -> 4094
/*       */     //   4070: aload_3
/*       */     //   4071: invokevirtual getAuxData : ()B
/*       */     //   4074: ifle -> 4240
/*       */     //   4077: aload #4
/*       */     //   4079: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4082: bipush #12
/*       */     //   4084: aaload
/*       */     //   4085: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4090: pop
/*       */     //   4091: goto -> 4240
/*       */     //   4094: aload_3
/*       */     //   4095: invokevirtual getParentId : ()J
/*       */     //   4098: ldc2_w -10
/*       */     //   4101: lcmp
/*       */     //   4102: ifeq -> 4115
/*       */     //   4105: aload_3
/*       */     //   4106: invokevirtual getTemplateId : ()I
/*       */     //   4109: sipush #1243
/*       */     //   4112: if_icmpne -> 4240
/*       */     //   4115: aload #4
/*       */     //   4117: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4120: bipush #12
/*       */     //   4122: aaload
/*       */     //   4123: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4128: pop
/*       */     //   4129: goto -> 4240
/*       */     //   4132: aload_3
/*       */     //   4133: invokevirtual isAlwaysLit : ()Z
/*       */     //   4136: ifne -> 4240
/*       */     //   4139: aload_3
/*       */     //   4140: invokevirtual getTemplateId : ()I
/*       */     //   4143: sipush #729
/*       */     //   4146: if_icmpne -> 4185
/*       */     //   4149: aload #4
/*       */     //   4151: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   4154: dup
/*       */     //   4155: bipush #53
/*       */     //   4157: ldc 'Blow out'
/*       */     //   4159: ldc 'blowing out'
/*       */     //   4161: iconst_2
/*       */     //   4162: newarray int
/*       */     //   4164: dup
/*       */     //   4165: iconst_0
/*       */     //   4166: iconst_0
/*       */     //   4167: iastore
/*       */     //   4168: dup
/*       */     //   4169: iconst_1
/*       */     //   4170: bipush #43
/*       */     //   4172: iastore
/*       */     //   4173: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   4176: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4181: pop
/*       */     //   4182: goto -> 4240
/*       */     //   4185: aload #4
/*       */     //   4187: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4190: bipush #53
/*       */     //   4192: aaload
/*       */     //   4193: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4198: pop
/*       */     //   4199: goto -> 4240
/*       */     //   4202: aload_2
/*       */     //   4203: invokevirtual isCoin : ()Z
/*       */     //   4206: ifeq -> 4240
/*       */     //   4209: aload_3
/*       */     //   4210: invokevirtual isSpringFilled : ()Z
/*       */     //   4213: ifeq -> 4240
/*       */     //   4216: aload_3
/*       */     //   4217: invokevirtual getSpellCourierBonus : ()F
/*       */     //   4220: fconst_0
/*       */     //   4221: fcmpl
/*       */     //   4222: ifle -> 4240
/*       */     //   4225: aload #4
/*       */     //   4227: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4230: sipush #380
/*       */     //   4233: aaload
/*       */     //   4234: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4239: pop
/*       */     //   4240: iload #5
/*       */     //   4242: sipush #442
/*       */     //   4245: if_icmpne -> 4269
/*       */     //   4248: aload #4
/*       */     //   4250: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   4253: dup
/*       */     //   4254: bipush #91
/*       */     //   4256: ldc 'Taste the julbord'
/*       */     //   4258: ldc 'eating'
/*       */     //   4260: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   4263: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4268: pop
/*       */     //   4269: goto -> 4804
/*       */     //   4272: aload_3
/*       */     //   4273: invokevirtual isLight : ()Z
/*       */     //   4276: ifeq -> 4421
/*       */     //   4279: aload_3
/*       */     //   4280: invokevirtual isAlwaysLit : ()Z
/*       */     //   4283: ifne -> 4421
/*       */     //   4286: iload #6
/*       */     //   4288: sipush #143
/*       */     //   4291: if_icmpeq -> 4310
/*       */     //   4294: iload #6
/*       */     //   4296: sipush #176
/*       */     //   4299: if_icmpne -> 4351
/*       */     //   4302: aload_1
/*       */     //   4303: invokevirtual getPower : ()I
/*       */     //   4306: iconst_2
/*       */     //   4307: if_icmplt -> 4351
/*       */     //   4310: aload_3
/*       */     //   4311: invokevirtual isOnFire : ()Z
/*       */     //   4314: ifne -> 4351
/*       */     //   4317: aload_3
/*       */     //   4318: invokevirtual getTemplateId : ()I
/*       */     //   4321: sipush #729
/*       */     //   4324: if_icmpne -> 4334
/*       */     //   4327: aload_3
/*       */     //   4328: invokevirtual getAuxData : ()B
/*       */     //   4331: ifle -> 4804
/*       */     //   4334: aload #4
/*       */     //   4336: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4339: bipush #12
/*       */     //   4341: aaload
/*       */     //   4342: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4347: pop
/*       */     //   4348: goto -> 4804
/*       */     //   4351: aload_3
/*       */     //   4352: invokevirtual isOnFire : ()Z
/*       */     //   4355: ifeq -> 4804
/*       */     //   4358: aload_3
/*       */     //   4359: invokevirtual getTemplateId : ()I
/*       */     //   4362: sipush #729
/*       */     //   4365: if_icmpne -> 4404
/*       */     //   4368: aload #4
/*       */     //   4370: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   4373: dup
/*       */     //   4374: bipush #53
/*       */     //   4376: ldc 'Blow out'
/*       */     //   4378: ldc 'blowing out'
/*       */     //   4380: iconst_2
/*       */     //   4381: newarray int
/*       */     //   4383: dup
/*       */     //   4384: iconst_0
/*       */     //   4385: iconst_0
/*       */     //   4386: iastore
/*       */     //   4387: dup
/*       */     //   4388: iconst_1
/*       */     //   4389: bipush #43
/*       */     //   4391: iastore
/*       */     //   4392: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   4395: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4400: pop
/*       */     //   4401: goto -> 4804
/*       */     //   4404: aload #4
/*       */     //   4406: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4409: bipush #53
/*       */     //   4411: aaload
/*       */     //   4412: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4417: pop
/*       */     //   4418: goto -> 4804
/*       */     //   4421: aload_3
/*       */     //   4422: invokevirtual isMeditation : ()Z
/*       */     //   4425: ifeq -> 4606
/*       */     //   4428: iconst_m1
/*       */     //   4429: istore #11
/*       */     //   4431: aload_1
/*       */     //   4432: invokevirtual getWurmId : ()J
/*       */     //   4435: invokestatic getCultist : (J)Lcom/wurmonline/server/players/Cultist;
/*       */     //   4438: astore #12
/*       */     //   4440: aload #12
/*       */     //   4442: ifnull -> 4484
/*       */     //   4445: iload #11
/*       */     //   4447: iconst_1
/*       */     //   4448: isub
/*       */     //   4449: i2s
/*       */     //   4450: istore #11
/*       */     //   4452: aload #12
/*       */     //   4454: invokevirtual getLevel : ()B
/*       */     //   4457: iconst_2
/*       */     //   4458: if_icmple -> 4468
/*       */     //   4461: iload #11
/*       */     //   4463: iconst_1
/*       */     //   4464: isub
/*       */     //   4465: i2s
/*       */     //   4466: istore #11
/*       */     //   4468: aload #12
/*       */     //   4470: invokevirtual getPath : ()B
/*       */     //   4473: iconst_4
/*       */     //   4474: if_icmpne -> 4484
/*       */     //   4477: iload #11
/*       */     //   4479: iconst_1
/*       */     //   4480: isub
/*       */     //   4481: i2s
/*       */     //   4482: istore #11
/*       */     //   4484: aload #4
/*       */     //   4486: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   4489: dup
/*       */     //   4490: iload #11
/*       */     //   4492: ldc 'Nature'
/*       */     //   4494: ldc 'meditation'
/*       */     //   4496: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   4499: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4504: pop
/*       */     //   4505: aload #4
/*       */     //   4507: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4510: sipush #384
/*       */     //   4513: aaload
/*       */     //   4514: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4519: pop
/*       */     //   4520: iload #11
/*       */     //   4522: iconst_m1
/*       */     //   4523: if_icmpge -> 4541
/*       */     //   4526: aload #4
/*       */     //   4528: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4531: sipush #385
/*       */     //   4534: aaload
/*       */     //   4535: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4540: pop
/*       */     //   4541: iload #11
/*       */     //   4543: bipush #-2
/*       */     //   4545: if_icmpge -> 4572
/*       */     //   4548: aload #12
/*       */     //   4550: invokevirtual getLevel : ()B
/*       */     //   4553: iconst_2
/*       */     //   4554: if_icmple -> 4572
/*       */     //   4557: aload #4
/*       */     //   4559: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4562: sipush #386
/*       */     //   4565: aaload
/*       */     //   4566: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4571: pop
/*       */     //   4572: iload #11
/*       */     //   4574: bipush #-2
/*       */     //   4576: if_icmpge -> 4603
/*       */     //   4579: aload #12
/*       */     //   4581: invokevirtual getPath : ()B
/*       */     //   4584: iconst_4
/*       */     //   4585: if_icmpne -> 4603
/*       */     //   4588: aload #4
/*       */     //   4590: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4593: sipush #722
/*       */     //   4596: aaload
/*       */     //   4597: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4602: pop
/*       */     //   4603: goto -> 4804
/*       */     //   4606: aload_3
/*       */     //   4607: invokevirtual isSealedByPlayer : ()Z
/*       */     //   4610: ifeq -> 4727
/*       */     //   4613: aload_3
/*       */     //   4614: invokevirtual getTemplateId : ()I
/*       */     //   4617: sipush #1309
/*       */     //   4620: if_icmpeq -> 4727
/*       */     //   4623: aload_3
/*       */     //   4624: invokevirtual isCrate : ()Z
/*       */     //   4627: ifne -> 4727
/*       */     //   4630: aconst_null
/*       */     //   4631: astore #11
/*       */     //   4633: aload_3
/*       */     //   4634: invokevirtual getItemsAsArray : ()[Lcom/wurmonline/server/items/Item;
/*       */     //   4637: astore #12
/*       */     //   4639: aload #12
/*       */     //   4641: arraylength
/*       */     //   4642: istore #13
/*       */     //   4644: iconst_0
/*       */     //   4645: istore #14
/*       */     //   4647: iload #14
/*       */     //   4649: iload #13
/*       */     //   4651: if_icmpge -> 4682
/*       */     //   4654: aload #12
/*       */     //   4656: iload #14
/*       */     //   4658: aaload
/*       */     //   4659: astore #15
/*       */     //   4661: aload #15
/*       */     //   4663: invokevirtual isLiquid : ()Z
/*       */     //   4666: ifeq -> 4676
/*       */     //   4669: aload #15
/*       */     //   4671: astore #11
/*       */     //   4673: goto -> 4682
/*       */     //   4676: iinc #14, 1
/*       */     //   4679: goto -> 4647
/*       */     //   4682: aload #11
/*       */     //   4684: ifnull -> 4695
/*       */     //   4687: aload #11
/*       */     //   4689: invokevirtual isFermenting : ()Z
/*       */     //   4692: ifne -> 4710
/*       */     //   4695: aload #4
/*       */     //   4697: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4700: sipush #740
/*       */     //   4703: aaload
/*       */     //   4704: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4709: pop
/*       */     //   4710: aload #4
/*       */     //   4712: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4715: bipush #19
/*       */     //   4717: aaload
/*       */     //   4718: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4723: pop
/*       */     //   4724: goto -> 4804
/*       */     //   4727: iload #6
/*       */     //   4729: sipush #1255
/*       */     //   4732: if_icmpne -> 4767
/*       */     //   4735: aload_3
/*       */     //   4736: invokevirtual canBeSealedByPlayer : ()Z
/*       */     //   4739: ifeq -> 4767
/*       */     //   4742: aload_3
/*       */     //   4743: invokevirtual isSealedByPlayer : ()Z
/*       */     //   4746: ifne -> 4767
/*       */     //   4749: aload #4
/*       */     //   4751: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4754: sipush #739
/*       */     //   4757: aaload
/*       */     //   4758: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4763: pop
/*       */     //   4764: goto -> 4804
/*       */     //   4767: iload #6
/*       */     //   4769: sipush #561
/*       */     //   4772: if_icmpne -> 4804
/*       */     //   4775: aload_3
/*       */     //   4776: invokevirtual canBePeggedByPlayer : ()Z
/*       */     //   4779: ifeq -> 4804
/*       */     //   4782: aload_3
/*       */     //   4783: invokevirtual isSealedByPlayer : ()Z
/*       */     //   4786: ifne -> 4804
/*       */     //   4789: aload #4
/*       */     //   4791: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4794: sipush #739
/*       */     //   4797: aaload
/*       */     //   4798: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4803: pop
/*       */     //   4804: aload_3
/*       */     //   4805: invokevirtual isCrate : ()Z
/*       */     //   4808: ifeq -> 4845
/*       */     //   4811: aload_3
/*       */     //   4812: invokevirtual isSealedByPlayer : ()Z
/*       */     //   4815: ifeq -> 4845
/*       */     //   4818: aload_3
/*       */     //   4819: invokevirtual getLastOwnerId : ()J
/*       */     //   4822: aload_1
/*       */     //   4823: invokevirtual getWurmId : ()J
/*       */     //   4826: lcmp
/*       */     //   4827: ifne -> 4845
/*       */     //   4830: aload #4
/*       */     //   4832: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4835: sipush #740
/*       */     //   4838: aaload
/*       */     //   4839: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4844: pop
/*       */     //   4845: aload_2
/*       */     //   4846: invokevirtual isWand : ()Z
/*       */     //   4849: ifeq -> 4911
/*       */     //   4852: aload_1
/*       */     //   4853: invokevirtual getPower : ()I
/*       */     //   4856: iconst_2
/*       */     //   4857: if_icmplt -> 4911
/*       */     //   4860: aload_3
/*       */     //   4861: invokevirtual isLock : ()Z
/*       */     //   4864: ifeq -> 4911
/*       */     //   4867: aload_3
/*       */     //   4868: invokevirtual isLocked : ()Z
/*       */     //   4871: ifeq -> 4891
/*       */     //   4874: aload #4
/*       */     //   4876: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4879: bipush #102
/*       */     //   4881: aaload
/*       */     //   4882: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4887: pop
/*       */     //   4888: goto -> 4911
/*       */     //   4891: invokestatic isThisATestServer : ()Z
/*       */     //   4894: ifeq -> 4911
/*       */     //   4897: aload #4
/*       */     //   4899: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   4902: bipush #28
/*       */     //   4904: aaload
/*       */     //   4905: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4910: pop
/*       */     //   4911: aload_3
/*       */     //   4912: invokevirtual canHavePermissions : ()Z
/*       */     //   4915: ifeq -> 5322
/*       */     //   4918: new java/util/LinkedList
/*       */     //   4921: dup
/*       */     //   4922: invokespecial <init> : ()V
/*       */     //   4925: astore #10
/*       */     //   4927: aload_3
/*       */     //   4928: invokevirtual isBed : ()Z
/*       */     //   4931: ifeq -> 4967
/*       */     //   4934: aload_3
/*       */     //   4935: aload_1
/*       */     //   4936: invokevirtual mayManage : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   4939: ifeq -> 4967
/*       */     //   4942: aload #10
/*       */     //   4944: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   4947: dup
/*       */     //   4948: sipush #688
/*       */     //   4951: ldc 'Manage Bed'
/*       */     //   4953: ldc 'managing'
/*       */     //   4955: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   4958: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   4963: pop
/*       */     //   4964: goto -> 5040
/*       */     //   4967: aload_3
/*       */     //   4968: invokevirtual getTemplateId : ()I
/*       */     //   4971: sipush #1271
/*       */     //   4974: if_icmpne -> 5010
/*       */     //   4977: aload_3
/*       */     //   4978: aload_1
/*       */     //   4979: invokevirtual mayManage : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   4982: ifeq -> 5010
/*       */     //   4985: aload #10
/*       */     //   4987: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   4990: dup
/*       */     //   4991: sipush #688
/*       */     //   4994: ldc 'Manage Message Board'
/*       */     //   4996: ldc 'managing'
/*       */     //   4998: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5001: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5006: pop
/*       */     //   5007: goto -> 5040
/*       */     //   5010: aload_3
/*       */     //   5011: invokevirtual isVehicle : ()Z
/*       */     //   5014: ifne -> 5040
/*       */     //   5017: aload_3
/*       */     //   5018: aload_1
/*       */     //   5019: invokevirtual mayManage : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   5022: ifeq -> 5040
/*       */     //   5025: aload #10
/*       */     //   5027: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5030: sipush #688
/*       */     //   5033: aaload
/*       */     //   5034: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5039: pop
/*       */     //   5040: aload_3
/*       */     //   5041: invokevirtual isBed : ()Z
/*       */     //   5044: ifeq -> 5081
/*       */     //   5047: aload_3
/*       */     //   5048: aload_1
/*       */     //   5049: invokevirtual maySeeHistory : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   5052: ifeq -> 5081
/*       */     //   5055: aload #10
/*       */     //   5057: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5060: dup
/*       */     //   5061: sipush #691
/*       */     //   5064: ldc_w 'History Bed'
/*       */     //   5067: ldc 'viewing'
/*       */     //   5069: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5072: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5077: pop
/*       */     //   5078: goto -> 5170
/*       */     //   5081: aload_3
/*       */     //   5082: invokevirtual getTemplateId : ()I
/*       */     //   5085: sipush #1271
/*       */     //   5088: if_icmpne -> 5132
/*       */     //   5091: aload_3
/*       */     //   5092: aload_1
/*       */     //   5093: invokevirtual isOwner : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   5096: ifne -> 5107
/*       */     //   5099: aload_1
/*       */     //   5100: invokevirtual getPower : ()I
/*       */     //   5103: iconst_1
/*       */     //   5104: if_icmple -> 5132
/*       */     //   5107: aload #10
/*       */     //   5109: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5112: dup
/*       */     //   5113: sipush #691
/*       */     //   5116: ldc 'History Of Message Board'
/*       */     //   5118: ldc 'viewing'
/*       */     //   5120: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5123: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5128: pop
/*       */     //   5129: goto -> 5170
/*       */     //   5132: aload_3
/*       */     //   5133: invokevirtual isVehicle : ()Z
/*       */     //   5136: ifne -> 5170
/*       */     //   5139: aload_3
/*       */     //   5140: aload_1
/*       */     //   5141: invokevirtual maySeeHistory : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   5144: ifeq -> 5170
/*       */     //   5147: aload #10
/*       */     //   5149: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5152: dup
/*       */     //   5153: sipush #691
/*       */     //   5156: ldc_w 'History Item'
/*       */     //   5159: ldc 'viewing'
/*       */     //   5161: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5164: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5169: pop
/*       */     //   5170: aload #10
/*       */     //   5172: invokeinterface isEmpty : ()Z
/*       */     //   5177: ifne -> 5223
/*       */     //   5180: aload #10
/*       */     //   5182: invokestatic sort : (Ljava/util/List;)V
/*       */     //   5185: aload #4
/*       */     //   5187: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5190: dup
/*       */     //   5191: aload #10
/*       */     //   5193: invokeinterface size : ()I
/*       */     //   5198: ineg
/*       */     //   5199: i2s
/*       */     //   5200: ldc 'Permissions'
/*       */     //   5202: ldc 'viewing'
/*       */     //   5204: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5207: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5212: pop
/*       */     //   5213: aload #4
/*       */     //   5215: aload #10
/*       */     //   5217: invokeinterface addAll : (Ljava/util/Collection;)Z
/*       */     //   5222: pop
/*       */     //   5223: goto -> 5322
/*       */     //   5226: iload #6
/*       */     //   5228: sipush #315
/*       */     //   5231: if_icmpeq -> 5242
/*       */     //   5234: iload #6
/*       */     //   5236: sipush #176
/*       */     //   5239: if_icmpne -> 5322
/*       */     //   5242: aload_1
/*       */     //   5243: invokevirtual getPower : ()I
/*       */     //   5246: iconst_2
/*       */     //   5247: if_icmplt -> 5322
/*       */     //   5250: bipush #-3
/*       */     //   5252: istore #10
/*       */     //   5254: aload #4
/*       */     //   5256: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5259: dup
/*       */     //   5260: bipush #-3
/*       */     //   5262: ldc_w 'Item'
/*       */     //   5265: ldc_w 'Item stuff'
/*       */     //   5268: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5271: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5276: pop
/*       */     //   5277: aload #4
/*       */     //   5279: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5282: sipush #180
/*       */     //   5285: aaload
/*       */     //   5286: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5291: pop
/*       */     //   5292: aload #4
/*       */     //   5294: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5297: sipush #185
/*       */     //   5300: aaload
/*       */     //   5301: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5306: pop
/*       */     //   5307: aload #4
/*       */     //   5309: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5312: sipush #684
/*       */     //   5315: aaload
/*       */     //   5316: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5321: pop
/*       */     //   5322: aload #4
/*       */     //   5324: aload_3
/*       */     //   5325: aload_1
/*       */     //   5326: invokestatic getBehaviours : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/creatures/Creature;)Ljava/util/List;
/*       */     //   5329: invokeinterface addAll : (Ljava/util/Collection;)Z
/*       */     //   5334: pop
/*       */     //   5335: aload #4
/*       */     //   5337: aload_1
/*       */     //   5338: aload_3
/*       */     //   5339: invokestatic getLoadUnloadActions : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;)Ljava/util/List;
/*       */     //   5342: invokeinterface addAll : (Ljava/util/Collection;)Z
/*       */     //   5347: pop
/*       */     //   5348: aload_2
/*       */     //   5349: invokevirtual getTemplateId : ()I
/*       */     //   5352: sipush #319
/*       */     //   5355: if_icmpeq -> 5368
/*       */     //   5358: aload_2
/*       */     //   5359: invokevirtual getTemplateId : ()I
/*       */     //   5362: sipush #1029
/*       */     //   5365: if_icmpne -> 5381
/*       */     //   5368: aload #4
/*       */     //   5370: aload_1
/*       */     //   5371: aload_3
/*       */     //   5372: invokestatic getHaulActions : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;)Ljava/util/List;
/*       */     //   5375: invokeinterface addAll : (Ljava/util/Collection;)Z
/*       */     //   5380: pop
/*       */     //   5381: aload_2
/*       */     //   5382: invokevirtual getTemplateId : ()I
/*       */     //   5385: sipush #315
/*       */     //   5388: if_icmpne -> 5401
/*       */     //   5391: aload_3
/*       */     //   5392: invokevirtual getTemplateId : ()I
/*       */     //   5395: sipush #1310
/*       */     //   5398: if_icmpeq -> 5421
/*       */     //   5401: aload_2
/*       */     //   5402: invokevirtual getTemplateId : ()I
/*       */     //   5405: sipush #176
/*       */     //   5408: if_icmpne -> 5436
/*       */     //   5411: aload_3
/*       */     //   5412: invokevirtual getTemplateId : ()I
/*       */     //   5415: sipush #1310
/*       */     //   5418: if_icmpne -> 5436
/*       */     //   5421: aload #4
/*       */     //   5423: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5426: sipush #909
/*       */     //   5429: aaload
/*       */     //   5430: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5435: pop
/*       */     //   5436: aload_3
/*       */     //   5437: invokevirtual getTemplateId : ()I
/*       */     //   5440: sipush #1311
/*       */     //   5443: if_icmpne -> 5491
/*       */     //   5446: getstatic com/wurmonline/server/Features$Feature.TRANSPORTABLE_CREATURES : Lcom/wurmonline/server/Features$Feature;
/*       */     //   5449: invokevirtual isEnabled : ()Z
/*       */     //   5452: ifeq -> 5491
/*       */     //   5455: aload_1
/*       */     //   5456: invokevirtual getFollowers : ()[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   5459: astore #10
/*       */     //   5461: aload #10
/*       */     //   5463: arraylength
/*       */     //   5464: ifle -> 5491
/*       */     //   5467: aload #4
/*       */     //   5469: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5472: dup
/*       */     //   5473: sipush #907
/*       */     //   5476: ldc_w 'Load Creature'
/*       */     //   5479: ldc_w 'loading '
/*       */     //   5482: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5485: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5490: pop
/*       */     //   5491: aload_3
/*       */     //   5492: invokevirtual getTemplateId : ()I
/*       */     //   5495: sipush #1310
/*       */     //   5498: if_icmpne -> 5633
/*       */     //   5501: aload_2
/*       */     //   5502: invokevirtual isLeadCreature : ()Z
/*       */     //   5505: ifeq -> 5532
/*       */     //   5508: aload #4
/*       */     //   5510: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5513: dup
/*       */     //   5514: sipush #908
/*       */     //   5517: ldc_w 'Unload Creature'
/*       */     //   5520: ldc_w 'unloading '
/*       */     //   5523: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5526: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5531: pop
/*       */     //   5532: aload_3
/*       */     //   5533: invokevirtual getData : ()J
/*       */     //   5536: ldc2_w -10
/*       */     //   5539: lcmp
/*       */     //   5540: ifeq -> 5633
/*       */     //   5543: invokestatic getInstance : ()Lcom/wurmonline/server/creatures/Creatures;
/*       */     //   5546: aload_3
/*       */     //   5547: invokevirtual getData : ()J
/*       */     //   5550: invokevirtual getCreature : (J)Lcom/wurmonline/server/creatures/Creature;
/*       */     //   5553: astore #10
/*       */     //   5555: aload #10
/*       */     //   5557: ifnull -> 5628
/*       */     //   5560: aload #10
/*       */     //   5562: invokevirtual isPlayer : ()Z
/*       */     //   5565: ifne -> 5628
/*       */     //   5568: aload #10
/*       */     //   5570: invokevirtual isHuman : ()Z
/*       */     //   5573: ifne -> 5628
/*       */     //   5576: aload #10
/*       */     //   5578: invokevirtual isGhost : ()Z
/*       */     //   5581: ifne -> 5628
/*       */     //   5584: aload #10
/*       */     //   5586: invokevirtual isReborn : ()Z
/*       */     //   5589: ifne -> 5628
/*       */     //   5592: aload #10
/*       */     //   5594: invokevirtual isCaredFor : ()Z
/*       */     //   5597: ifeq -> 5613
/*       */     //   5600: aload #10
/*       */     //   5602: invokevirtual getCareTakerId : ()J
/*       */     //   5605: aload_1
/*       */     //   5606: invokevirtual getWurmId : ()J
/*       */     //   5609: lcmp
/*       */     //   5610: ifne -> 5628
/*       */     //   5613: aload #4
/*       */     //   5615: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5618: sipush #493
/*       */     //   5621: aaload
/*       */     //   5622: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5627: pop
/*       */     //   5628: goto -> 5633
/*       */     //   5631: astore #10
/*       */     //   5633: getstatic com/wurmonline/server/Features$Feature.CHICKEN_COOPS : Lcom/wurmonline/server/Features$Feature;
/*       */     //   5636: invokevirtual isEnabled : ()Z
/*       */     //   5639: ifeq -> 5689
/*       */     //   5642: aload_3
/*       */     //   5643: invokevirtual getTemplateId : ()I
/*       */     //   5646: sipush #1432
/*       */     //   5649: if_icmpne -> 5689
/*       */     //   5652: aload_1
/*       */     //   5653: invokevirtual getFollowers : ()[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   5656: astore #10
/*       */     //   5658: aload #10
/*       */     //   5660: arraylength
/*       */     //   5661: iconst_1
/*       */     //   5662: if_icmpne -> 5689
/*       */     //   5665: aload #4
/*       */     //   5667: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5670: dup
/*       */     //   5671: sipush #907
/*       */     //   5674: ldc_w 'Load Creature'
/*       */     //   5677: ldc_w 'loading '
/*       */     //   5680: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   5683: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5688: pop
/*       */     //   5689: aload_1
/*       */     //   5690: aload_3
/*       */     //   5691: aload #4
/*       */     //   5693: invokestatic addCreationWindowOption : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Ljava/util/List;)V
/*       */     //   5696: iconst_0
/*       */     //   5697: istore #10
/*       */     //   5699: aload_3
/*       */     //   5700: invokevirtual isTraded : ()Z
/*       */     //   5703: ifne -> 12642
/*       */     //   5706: lload #8
/*       */     //   5708: aload_1
/*       */     //   5709: invokevirtual getWurmId : ()J
/*       */     //   5712: lcmp
/*       */     //   5713: ifne -> 6666
/*       */     //   5716: iconst_1
/*       */     //   5717: istore #10
/*       */     //   5719: iload #6
/*       */     //   5721: sipush #457
/*       */     //   5724: if_icmpne -> 5749
/*       */     //   5727: aload_3
/*       */     //   5728: invokevirtual isBowUnstringed : ()Z
/*       */     //   5731: ifeq -> 5749
/*       */     //   5734: aload #4
/*       */     //   5736: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5739: sipush #132
/*       */     //   5742: aaload
/*       */     //   5743: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5748: pop
/*       */     //   5749: iload #6
/*       */     //   5751: sipush #150
/*       */     //   5754: if_icmpeq -> 5765
/*       */     //   5757: iload #6
/*       */     //   5759: sipush #151
/*       */     //   5762: if_icmpne -> 5788
/*       */     //   5765: iload #5
/*       */     //   5767: sipush #780
/*       */     //   5770: if_icmpne -> 5788
/*       */     //   5773: aload #4
/*       */     //   5775: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5778: sipush #132
/*       */     //   5781: aaload
/*       */     //   5782: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5787: pop
/*       */     //   5788: aload_3
/*       */     //   5789: invokevirtual isLiquid : ()Z
/*       */     //   5792: ifeq -> 5845
/*       */     //   5795: aload #4
/*       */     //   5797: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5800: dup
/*       */     //   5801: iconst_m1
/*       */     //   5802: ldc 'Pour'
/*       */     //   5804: ldc 'pouring'
/*       */     //   5806: iconst_0
/*       */     //   5807: newarray int
/*       */     //   5809: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   5812: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5817: pop
/*       */     //   5818: aload #4
/*       */     //   5820: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5823: dup
/*       */     //   5824: bipush #7
/*       */     //   5826: ldc 'On ground'
/*       */     //   5828: ldc 'pouring'
/*       */     //   5830: iconst_0
/*       */     //   5831: newarray int
/*       */     //   5833: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   5836: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5841: pop
/*       */     //   5842: goto -> 6002
/*       */     //   5845: aload_3
/*       */     //   5846: invokevirtual getTemplateId : ()I
/*       */     //   5849: istore #11
/*       */     //   5851: iload #11
/*       */     //   5853: bipush #26
/*       */     //   5855: if_icmpeq -> 5866
/*       */     //   5858: iload #11
/*       */     //   5860: sipush #298
/*       */     //   5863: if_icmpne -> 5932
/*       */     //   5866: aload #4
/*       */     //   5868: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5871: dup
/*       */     //   5872: bipush #-2
/*       */     //   5874: ldc 'Drop'
/*       */     //   5876: ldc 'dropping'
/*       */     //   5878: iconst_0
/*       */     //   5879: newarray int
/*       */     //   5881: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   5884: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5889: pop
/*       */     //   5890: aload #4
/*       */     //   5892: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5895: dup
/*       */     //   5896: bipush #7
/*       */     //   5898: ldc 'On ground'
/*       */     //   5900: ldc 'dropping'
/*       */     //   5902: iconst_0
/*       */     //   5903: newarray int
/*       */     //   5905: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   5908: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5913: pop
/*       */     //   5914: aload #4
/*       */     //   5916: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5919: sipush #638
/*       */     //   5922: aaload
/*       */     //   5923: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5928: pop
/*       */     //   5929: goto -> 6002
/*       */     //   5932: aload_3
/*       */     //   5933: invokevirtual isComponentItem : ()Z
/*       */     //   5936: ifne -> 6002
/*       */     //   5939: aload #4
/*       */     //   5941: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5944: dup
/*       */     //   5945: bipush #-2
/*       */     //   5947: ldc 'Drop'
/*       */     //   5949: ldc 'dropping'
/*       */     //   5951: iconst_0
/*       */     //   5952: newarray int
/*       */     //   5954: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   5957: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5962: pop
/*       */     //   5963: aload #4
/*       */     //   5965: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   5968: dup
/*       */     //   5969: bipush #7
/*       */     //   5971: ldc 'On ground'
/*       */     //   5973: ldc 'dropping'
/*       */     //   5975: iconst_0
/*       */     //   5976: newarray int
/*       */     //   5978: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   5981: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   5986: pop
/*       */     //   5987: aload #4
/*       */     //   5989: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   5992: sipush #925
/*       */     //   5995: aaload
/*       */     //   5996: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6001: pop
/*       */     //   6002: iload #5
/*       */     //   6004: sipush #175
/*       */     //   6007: if_icmpeq -> 6050
/*       */     //   6010: iload #5
/*       */     //   6012: sipush #651
/*       */     //   6015: if_icmpeq -> 6050
/*       */     //   6018: iload #5
/*       */     //   6020: sipush #1097
/*       */     //   6023: if_icmpeq -> 6050
/*       */     //   6026: iload #5
/*       */     //   6028: sipush #1098
/*       */     //   6031: if_icmpeq -> 6050
/*       */     //   6034: iload #5
/*       */     //   6036: sipush #466
/*       */     //   6039: if_icmpne -> 6066
/*       */     //   6042: aload_3
/*       */     //   6043: invokevirtual getAuxData : ()B
/*       */     //   6046: iconst_1
/*       */     //   6047: if_icmpne -> 6066
/*       */     //   6050: aload #4
/*       */     //   6052: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6055: iconst_3
/*       */     //   6056: aaload
/*       */     //   6057: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6062: pop
/*       */     //   6063: goto -> 6089
/*       */     //   6066: iload #5
/*       */     //   6068: sipush #782
/*       */     //   6071: if_icmpne -> 6089
/*       */     //   6074: aload #4
/*       */     //   6076: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6079: sipush #518
/*       */     //   6082: aaload
/*       */     //   6083: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6088: pop
/*       */     //   6089: iload #5
/*       */     //   6091: sipush #1101
/*       */     //   6094: if_icmpne -> 6112
/*       */     //   6097: aload #4
/*       */     //   6099: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6102: sipush #183
/*       */     //   6105: aaload
/*       */     //   6106: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6111: pop
/*       */     //   6112: aload_3
/*       */     //   6113: invokevirtual isFullprice : ()Z
/*       */     //   6116: ifne -> 6171
/*       */     //   6119: aload #4
/*       */     //   6121: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   6124: dup
/*       */     //   6125: bipush #-2
/*       */     //   6127: ldc 'Prices'
/*       */     //   6129: ldc 'Prices'
/*       */     //   6131: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   6134: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6139: pop
/*       */     //   6140: aload #4
/*       */     //   6142: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6145: bipush #86
/*       */     //   6147: aaload
/*       */     //   6148: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6153: pop
/*       */     //   6154: aload #4
/*       */     //   6156: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6159: bipush #87
/*       */     //   6161: aaload
/*       */     //   6162: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6167: pop
/*       */     //   6168: goto -> 6205
/*       */     //   6171: aload #4
/*       */     //   6173: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   6176: dup
/*       */     //   6177: iconst_m1
/*       */     //   6178: ldc 'Prices'
/*       */     //   6180: ldc 'Prices'
/*       */     //   6182: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   6185: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6190: pop
/*       */     //   6191: aload #4
/*       */     //   6193: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6196: bipush #87
/*       */     //   6198: aaload
/*       */     //   6199: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6204: pop
/*       */     //   6205: aload_3
/*       */     //   6206: invokevirtual isGem : ()Z
/*       */     //   6209: ifeq -> 6236
/*       */     //   6212: aload_3
/*       */     //   6213: invokevirtual getData1 : ()I
/*       */     //   6216: ifle -> 6415
/*       */     //   6219: aload #4
/*       */     //   6221: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6224: bipush #118
/*       */     //   6226: aaload
/*       */     //   6227: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6232: pop
/*       */     //   6233: goto -> 6415
/*       */     //   6236: iload #5
/*       */     //   6238: sipush #602
/*       */     //   6241: if_icmpne -> 6261
/*       */     //   6244: aload #4
/*       */     //   6246: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6249: bipush #118
/*       */     //   6251: aaload
/*       */     //   6252: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6257: pop
/*       */     //   6258: goto -> 6415
/*       */     //   6261: aload_3
/*       */     //   6262: invokevirtual isDeathProtection : ()Z
/*       */     //   6265: ifne -> 6298
/*       */     //   6268: iload #5
/*       */     //   6270: sipush #527
/*       */     //   6273: if_icmpeq -> 6298
/*       */     //   6276: iload #5
/*       */     //   6278: iconst_5
/*       */     //   6279: if_icmpeq -> 6298
/*       */     //   6282: iload #5
/*       */     //   6284: sipush #834
/*       */     //   6287: if_icmpeq -> 6298
/*       */     //   6290: iload #5
/*       */     //   6292: sipush #836
/*       */     //   6295: if_icmpne -> 6315
/*       */     //   6298: aload #4
/*       */     //   6300: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6303: bipush #118
/*       */     //   6305: aaload
/*       */     //   6306: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6311: pop
/*       */     //   6312: goto -> 6415
/*       */     //   6315: iload #5
/*       */     //   6317: sipush #233
/*       */     //   6320: if_icmpne -> 6341
/*       */     //   6323: aload #4
/*       */     //   6325: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6328: sipush #682
/*       */     //   6331: aaload
/*       */     //   6332: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6337: pop
/*       */     //   6338: goto -> 6415
/*       */     //   6341: iload #5
/*       */     //   6343: sipush #781
/*       */     //   6346: if_icmpeq -> 6365
/*       */     //   6349: iload #5
/*       */     //   6351: sipush #843
/*       */     //   6354: if_icmpeq -> 6365
/*       */     //   6357: iload #5
/*       */     //   6359: sipush #1300
/*       */     //   6362: if_icmpne -> 6393
/*       */     //   6365: aload_3
/*       */     //   6366: invokevirtual getOwnerId : ()J
/*       */     //   6369: ldc2_w -10
/*       */     //   6372: lcmp
/*       */     //   6373: ifeq -> 6415
/*       */     //   6376: aload #4
/*       */     //   6378: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6381: bipush #118
/*       */     //   6383: aaload
/*       */     //   6384: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6389: pop
/*       */     //   6390: goto -> 6415
/*       */     //   6393: iload #5
/*       */     //   6395: sipush #719
/*       */     //   6398: if_icmpne -> 6415
/*       */     //   6401: aload #4
/*       */     //   6403: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6406: bipush #118
/*       */     //   6408: aaload
/*       */     //   6409: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6414: pop
/*       */     //   6415: aload_2
/*       */     //   6416: invokevirtual isHealing : ()Z
/*       */     //   6419: ifeq -> 6429
/*       */     //   6422: aload_3
/*       */     //   6423: invokevirtual isHealing : ()Z
/*       */     //   6426: ifne -> 6469
/*       */     //   6429: aload_2
/*       */     //   6430: invokevirtual getTemplateId : ()I
/*       */     //   6433: sipush #764
/*       */     //   6436: if_icmpne -> 6449
/*       */     //   6439: aload_3
/*       */     //   6440: invokevirtual getTemplateId : ()I
/*       */     //   6443: sipush #866
/*       */     //   6446: if_icmpeq -> 6469
/*       */     //   6449: aload_2
/*       */     //   6450: invokevirtual getTemplateId : ()I
/*       */     //   6453: sipush #866
/*       */     //   6456: if_icmpne -> 6532
/*       */     //   6459: aload_3
/*       */     //   6460: invokevirtual getTemplateId : ()I
/*       */     //   6463: sipush #764
/*       */     //   6466: if_icmpne -> 6532
/*       */     //   6469: iload #6
/*       */     //   6471: iload #5
/*       */     //   6473: if_icmpeq -> 6641
/*       */     //   6476: aload #4
/*       */     //   6478: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   6481: dup
/*       */     //   6482: bipush #-2
/*       */     //   6484: ldc_w 'Alchemy'
/*       */     //   6487: ldc_w 'Alchemy'
/*       */     //   6490: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   6493: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6498: pop
/*       */     //   6499: aload #4
/*       */     //   6501: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6504: sipush #283
/*       */     //   6507: aaload
/*       */     //   6508: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6513: pop
/*       */     //   6514: aload #4
/*       */     //   6516: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6519: sipush #285
/*       */     //   6522: aaload
/*       */     //   6523: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6528: pop
/*       */     //   6529: goto -> 6641
/*       */     //   6532: aload_2
/*       */     //   6533: invokevirtual isSmearable : ()Z
/*       */     //   6536: ifeq -> 6579
/*       */     //   6539: aload #4
/*       */     //   6541: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   6544: dup
/*       */     //   6545: iconst_m1
/*       */     //   6546: ldc_w 'Alchemy'
/*       */     //   6549: ldc_w 'Alchemy'
/*       */     //   6552: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   6555: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6560: pop
/*       */     //   6561: aload #4
/*       */     //   6563: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6566: sipush #633
/*       */     //   6569: aaload
/*       */     //   6570: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6575: pop
/*       */     //   6576: goto -> 6641
/*       */     //   6579: aload_3
/*       */     //   6580: invokevirtual isAbility : ()Z
/*       */     //   6583: ifeq -> 6610
/*       */     //   6586: aload_3
/*       */     //   6587: invokevirtual canBePlanted : ()Z
/*       */     //   6590: ifne -> 6610
/*       */     //   6593: aload #4
/*       */     //   6595: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6598: bipush #118
/*       */     //   6600: aaload
/*       */     //   6601: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6606: pop
/*       */     //   6607: goto -> 6641
/*       */     //   6610: aload_3
/*       */     //   6611: invokevirtual getTemplateId : ()I
/*       */     //   6614: sipush #1438
/*       */     //   6617: if_icmpne -> 6641
/*       */     //   6620: aload #4
/*       */     //   6622: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   6625: dup
/*       */     //   6626: bipush #118
/*       */     //   6628: ldc 'Claim affinity'
/*       */     //   6630: ldc 'claiming'
/*       */     //   6632: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   6635: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6640: pop
/*       */     //   6641: aload_3
/*       */     //   6642: invokevirtual isInstaDiscard : ()Z
/*       */     //   6645: ifeq -> 7984
/*       */     //   6648: aload #4
/*       */     //   6650: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6653: sipush #600
/*       */     //   6656: aaload
/*       */     //   6657: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6662: pop
/*       */     //   6663: goto -> 7984
/*       */     //   6666: aload_3
/*       */     //   6667: invokevirtual getZoneId : ()I
/*       */     //   6670: i2l
/*       */     //   6671: ldc2_w -10
/*       */     //   6674: lcmp
/*       */     //   6675: ifeq -> 7984
/*       */     //   6678: aload_3
/*       */     //   6679: invokevirtual getTopParentOrNull : ()Lcom/wurmonline/server/items/Item;
/*       */     //   6682: astore #11
/*       */     //   6684: aload #11
/*       */     //   6686: ifnonnull -> 6692
/*       */     //   6689: aload_3
/*       */     //   6690: astore #11
/*       */     //   6692: aload #11
/*       */     //   6694: invokevirtual getWurmId : ()J
/*       */     //   6697: aload_1
/*       */     //   6698: invokevirtual getVehicle : ()J
/*       */     //   6701: lcmp
/*       */     //   6702: ifeq -> 6763
/*       */     //   6705: aload_1
/*       */     //   6706: aload_3
/*       */     //   6707: invokevirtual getPosX : ()F
/*       */     //   6710: aload_3
/*       */     //   6711: invokevirtual getPosY : ()F
/*       */     //   6714: aload_3
/*       */     //   6715: invokevirtual getPosZ : ()F
/*       */     //   6718: aload_3
/*       */     //   6719: invokevirtual isVehicle : ()Z
/*       */     //   6722: ifeq -> 6755
/*       */     //   6725: aload_3
/*       */     //   6726: invokevirtual isTent : ()Z
/*       */     //   6729: ifne -> 6755
/*       */     //   6732: aload_3
/*       */     //   6733: invokevirtual isChair : ()Z
/*       */     //   6736: ifne -> 6755
/*       */     //   6739: bipush #6
/*       */     //   6741: aload_3
/*       */     //   6742: invokevirtual getSizeZ : ()I
/*       */     //   6745: bipush #100
/*       */     //   6747: idiv
/*       */     //   6748: invokestatic max : (II)I
/*       */     //   6751: i2f
/*       */     //   6752: goto -> 6757
/*       */     //   6755: ldc 6.0
/*       */     //   6757: invokevirtual isWithinDistanceTo : (FFFF)Z
/*       */     //   6760: ifeq -> 7984
/*       */     //   6763: aload_3
/*       */     //   6764: invokevirtual getWurmId : ()J
/*       */     //   6767: aload_1
/*       */     //   6768: invokevirtual getVehicle : ()J
/*       */     //   6771: lcmp
/*       */     //   6772: ifne -> 6779
/*       */     //   6775: aconst_null
/*       */     //   6776: goto -> 6785
/*       */     //   6779: aload_1
/*       */     //   6780: aload_3
/*       */     //   6781: iconst_4
/*       */     //   6782: invokestatic getBlockerBetween : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;I)Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   6785: astore #12
/*       */     //   6787: aload #12
/*       */     //   6789: ifnonnull -> 7984
/*       */     //   6792: iconst_1
/*       */     //   6793: istore #10
/*       */     //   6795: aload_3
/*       */     //   6796: invokevirtual getPosX : ()F
/*       */     //   6799: f2i
/*       */     //   6800: iconst_2
/*       */     //   6801: ishr
/*       */     //   6802: aload_3
/*       */     //   6803: invokevirtual getPosY : ()F
/*       */     //   6806: f2i
/*       */     //   6807: iconst_2
/*       */     //   6808: ishr
/*       */     //   6809: aload_3
/*       */     //   6810: invokevirtual isOnSurface : ()Z
/*       */     //   6813: invokestatic getZone : (IIZ)Lcom/wurmonline/server/zones/Zone;
/*       */     //   6816: astore #13
/*       */     //   6818: aload #13
/*       */     //   6820: aload_3
/*       */     //   6821: invokevirtual getPosX : ()F
/*       */     //   6824: f2i
/*       */     //   6825: iconst_2
/*       */     //   6826: ishr
/*       */     //   6827: aload_3
/*       */     //   6828: invokevirtual getPosY : ()F
/*       */     //   6831: f2i
/*       */     //   6832: iconst_2
/*       */     //   6833: ishr
/*       */     //   6834: invokevirtual getTileOrNull : (II)Lcom/wurmonline/server/zones/VolaTile;
/*       */     //   6837: astore #14
/*       */     //   6839: aload #14
/*       */     //   6841: ifnull -> 6907
/*       */     //   6844: aload #14
/*       */     //   6846: invokevirtual getStructure : ()Lcom/wurmonline/server/structures/Structure;
/*       */     //   6849: astore #15
/*       */     //   6851: aload_1
/*       */     //   6852: invokevirtual getCurrentTile : ()Lcom/wurmonline/server/zones/VolaTile;
/*       */     //   6855: astore #16
/*       */     //   6857: aload #16
/*       */     //   6859: ifnull -> 6891
/*       */     //   6862: aload #14
/*       */     //   6864: invokevirtual getStructure : ()Lcom/wurmonline/server/structures/Structure;
/*       */     //   6867: aload #15
/*       */     //   6869: if_acmpeq -> 6907
/*       */     //   6872: aload #15
/*       */     //   6874: ifnull -> 6885
/*       */     //   6877: aload #15
/*       */     //   6879: invokevirtual isTypeBridge : ()Z
/*       */     //   6882: ifne -> 6907
/*       */     //   6885: iconst_0
/*       */     //   6886: istore #10
/*       */     //   6888: goto -> 6907
/*       */     //   6891: aload #15
/*       */     //   6893: ifnull -> 6907
/*       */     //   6896: aload #15
/*       */     //   6898: invokevirtual isTypeBridge : ()Z
/*       */     //   6901: ifne -> 6907
/*       */     //   6904: iconst_0
/*       */     //   6905: istore #10
/*       */     //   6907: goto -> 6912
/*       */     //   6910: astore #13
/*       */     //   6912: iload #10
/*       */     //   6914: ifeq -> 7984
/*       */     //   6917: aload_3
/*       */     //   6918: aload_1
/*       */     //   6919: invokevirtual isNoTake : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   6922: ifne -> 7034
/*       */     //   6925: aload_3
/*       */     //   6926: invokevirtual isOutsideOnly : ()Z
/*       */     //   6929: ifne -> 7034
/*       */     //   6932: aload_3
/*       */     //   6933: invokevirtual canBePlanted : ()Z
/*       */     //   6936: ifne -> 7034
/*       */     //   6939: aload_3
/*       */     //   6940: invokevirtual isLiquid : ()Z
/*       */     //   6943: ifne -> 7034
/*       */     //   6946: aload_3
/*       */     //   6947: invokevirtual isBulkContainer : ()Z
/*       */     //   6950: ifne -> 7034
/*       */     //   6953: aload_3
/*       */     //   6954: aload_1
/*       */     //   6955: aconst_null
/*       */     //   6956: invokestatic checkIfStealing : (Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/behaviours/Action;)Z
/*       */     //   6959: ifeq -> 6979
/*       */     //   6962: aload #4
/*       */     //   6964: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6967: bipush #100
/*       */     //   6969: aaload
/*       */     //   6970: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6975: pop
/*       */     //   6976: goto -> 7034
/*       */     //   6979: aload #4
/*       */     //   6981: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   6984: bipush #6
/*       */     //   6986: aaload
/*       */     //   6987: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   6992: pop
/*       */     //   6993: aload_3
/*       */     //   6994: invokevirtual getParentId : ()J
/*       */     //   6997: ldc2_w -10
/*       */     //   7000: lcmp
/*       */     //   7001: ifne -> 7011
/*       */     //   7004: iload #5
/*       */     //   7006: bipush #26
/*       */     //   7008: if_icmpne -> 7019
/*       */     //   7011: iload #5
/*       */     //   7013: sipush #298
/*       */     //   7016: if_icmpeq -> 7034
/*       */     //   7019: aload #4
/*       */     //   7021: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7024: sipush #925
/*       */     //   7027: aaload
/*       */     //   7028: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7033: pop
/*       */     //   7034: aload_3
/*       */     //   7035: invokevirtual isHollow : ()Z
/*       */     //   7038: ifeq -> 7100
/*       */     //   7041: aload_3
/*       */     //   7042: invokevirtual isSealedByPlayer : ()Z
/*       */     //   7045: ifne -> 7065
/*       */     //   7048: aload_3
/*       */     //   7049: invokevirtual getTemplateId : ()I
/*       */     //   7052: sipush #1342
/*       */     //   7055: if_icmpne -> 7069
/*       */     //   7058: aload_3
/*       */     //   7059: invokevirtual isPlanted : ()Z
/*       */     //   7062: ifne -> 7069
/*       */     //   7065: iconst_1
/*       */     //   7066: goto -> 7070
/*       */     //   7069: iconst_0
/*       */     //   7070: ifne -> 7100
/*       */     //   7073: aload_3
/*       */     //   7074: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   7077: invokevirtual hasViewableSubItems : ()Z
/*       */     //   7080: ifeq -> 7108
/*       */     //   7083: aload_3
/*       */     //   7084: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   7087: invokevirtual isContainerWithSubItems : ()Z
/*       */     //   7090: ifne -> 7108
/*       */     //   7093: aload_1
/*       */     //   7094: invokevirtual getPower : ()I
/*       */     //   7097: ifgt -> 7108
/*       */     //   7100: iload #5
/*       */     //   7102: sipush #865
/*       */     //   7105: if_icmpne -> 7508
/*       */     //   7108: aload_3
/*       */     //   7109: invokevirtual getWurmId : ()J
/*       */     //   7112: aload_3
/*       */     //   7113: invokevirtual getTopParent : ()J
/*       */     //   7116: lcmp
/*       */     //   7117: ifeq -> 7160
/*       */     //   7120: aload_3
/*       */     //   7121: invokevirtual getTopParentOrNull : ()Lcom/wurmonline/server/items/Item;
/*       */     //   7124: ifnull -> 7164
/*       */     //   7127: aload_3
/*       */     //   7128: invokevirtual getTopParentOrNull : ()Lcom/wurmonline/server/items/Item;
/*       */     //   7131: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   7134: invokevirtual hasViewableSubItems : ()Z
/*       */     //   7137: ifeq -> 7164
/*       */     //   7140: aload_3
/*       */     //   7141: invokevirtual getTopParentOrNull : ()Lcom/wurmonline/server/items/Item;
/*       */     //   7144: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   7147: invokevirtual isContainerWithSubItems : ()Z
/*       */     //   7150: ifeq -> 7160
/*       */     //   7153: aload_3
/*       */     //   7154: invokevirtual isPlacedOnParent : ()Z
/*       */     //   7157: ifeq -> 7164
/*       */     //   7160: iconst_1
/*       */     //   7161: goto -> 7165
/*       */     //   7164: iconst_0
/*       */     //   7165: istore #13
/*       */     //   7167: aload_3
/*       */     //   7168: invokevirtual isLockable : ()Z
/*       */     //   7171: ifeq -> 7311
/*       */     //   7174: aload_3
/*       */     //   7175: invokevirtual getLockId : ()J
/*       */     //   7178: lstore #14
/*       */     //   7180: aload_3
/*       */     //   7181: invokevirtual getWatchers : ()[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   7184: astore #16
/*       */     //   7186: iconst_0
/*       */     //   7187: istore #17
/*       */     //   7189: aload #16
/*       */     //   7191: astore #18
/*       */     //   7193: aload #18
/*       */     //   7195: arraylength
/*       */     //   7196: istore #19
/*       */     //   7198: iconst_0
/*       */     //   7199: istore #20
/*       */     //   7201: iload #20
/*       */     //   7203: iload #19
/*       */     //   7205: if_icmpge -> 7230
/*       */     //   7208: aload #18
/*       */     //   7210: iload #20
/*       */     //   7212: aaload
/*       */     //   7213: astore #21
/*       */     //   7215: aload #21
/*       */     //   7217: aload_1
/*       */     //   7218: if_acmpne -> 7224
/*       */     //   7221: iconst_1
/*       */     //   7222: istore #17
/*       */     //   7224: iinc #20, 1
/*       */     //   7227: goto -> 7201
/*       */     //   7230: iload #17
/*       */     //   7232: ifeq -> 7272
/*       */     //   7235: iload #13
/*       */     //   7237: ifeq -> 7253
/*       */     //   7240: aload #4
/*       */     //   7242: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7245: iconst_4
/*       */     //   7246: aaload
/*       */     //   7247: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7252: pop
/*       */     //   7253: aload_1
/*       */     //   7254: aload_2
/*       */     //   7255: aload_3
/*       */     //   7256: lload #14
/*       */     //   7258: iload #6
/*       */     //   7260: iload #13
/*       */     //   7262: aload #4
/*       */     //   7264: aload #16
/*       */     //   7266: invokestatic addLockOptions : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;JIZLjava/util/List;[Lcom/wurmonline/server/creatures/Creature;)V
/*       */     //   7269: goto -> 7288
/*       */     //   7272: aload_1
/*       */     //   7273: aload_2
/*       */     //   7274: aload_3
/*       */     //   7275: lload #14
/*       */     //   7277: iload #6
/*       */     //   7279: iload #13
/*       */     //   7281: aload #4
/*       */     //   7283: aload #16
/*       */     //   7285: invokestatic addLockOptions : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;JIZLjava/util/List;[Lcom/wurmonline/server/creatures/Creature;)V
/*       */     //   7288: goto -> 7308
/*       */     //   7291: astore #16
/*       */     //   7293: aload_1
/*       */     //   7294: aload_2
/*       */     //   7295: aload_3
/*       */     //   7296: lload #14
/*       */     //   7298: iload #6
/*       */     //   7300: iload #13
/*       */     //   7302: aload #4
/*       */     //   7304: aconst_null
/*       */     //   7305: invokestatic addLockOptions : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;JIZLjava/util/List;[Lcom/wurmonline/server/creatures/Creature;)V
/*       */     //   7308: goto -> 7508
/*       */     //   7311: iload #13
/*       */     //   7313: ifeq -> 7508
/*       */     //   7316: aload_3
/*       */     //   7317: invokevirtual getWatchers : ()[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   7320: astore #14
/*       */     //   7322: iconst_0
/*       */     //   7323: istore #15
/*       */     //   7325: aload #14
/*       */     //   7327: astore #16
/*       */     //   7329: aload #16
/*       */     //   7331: arraylength
/*       */     //   7332: istore #17
/*       */     //   7334: iconst_0
/*       */     //   7335: istore #18
/*       */     //   7337: iload #18
/*       */     //   7339: iload #17
/*       */     //   7341: if_icmpge -> 7366
/*       */     //   7344: aload #16
/*       */     //   7346: iload #18
/*       */     //   7348: aaload
/*       */     //   7349: astore #19
/*       */     //   7351: aload #19
/*       */     //   7353: aload_1
/*       */     //   7354: if_acmpne -> 7360
/*       */     //   7357: iconst_1
/*       */     //   7358: istore #15
/*       */     //   7360: iinc #18, 1
/*       */     //   7363: goto -> 7337
/*       */     //   7366: iload #15
/*       */     //   7368: ifeq -> 7387
/*       */     //   7371: aload #4
/*       */     //   7373: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7376: iconst_4
/*       */     //   7377: aaload
/*       */     //   7378: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7383: pop
/*       */     //   7384: goto -> 7445
/*       */     //   7387: aload_3
/*       */     //   7388: invokevirtual getTemplateId : ()I
/*       */     //   7391: sipush #272
/*       */     //   7394: if_icmpne -> 7432
/*       */     //   7397: aload_3
/*       */     //   7398: invokevirtual getWasBrandedTo : ()J
/*       */     //   7401: ldc2_w -10
/*       */     //   7404: lcmp
/*       */     //   7405: ifeq -> 7432
/*       */     //   7408: aload_3
/*       */     //   7409: aload_1
/*       */     //   7410: invokevirtual mayCommand : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   7413: ifeq -> 7445
/*       */     //   7416: aload #4
/*       */     //   7418: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7421: iconst_3
/*       */     //   7422: aaload
/*       */     //   7423: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7428: pop
/*       */     //   7429: goto -> 7445
/*       */     //   7432: aload #4
/*       */     //   7434: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7437: iconst_3
/*       */     //   7438: aaload
/*       */     //   7439: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7444: pop
/*       */     //   7445: goto -> 7508
/*       */     //   7448: astore #14
/*       */     //   7450: aload_3
/*       */     //   7451: invokevirtual getTemplateId : ()I
/*       */     //   7454: sipush #272
/*       */     //   7457: if_icmpne -> 7495
/*       */     //   7460: aload_3
/*       */     //   7461: invokevirtual getWasBrandedTo : ()J
/*       */     //   7464: ldc2_w -10
/*       */     //   7467: lcmp
/*       */     //   7468: ifeq -> 7495
/*       */     //   7471: aload_3
/*       */     //   7472: aload_1
/*       */     //   7473: invokevirtual mayCommand : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   7476: ifeq -> 7508
/*       */     //   7479: aload #4
/*       */     //   7481: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7484: iconst_3
/*       */     //   7485: aaload
/*       */     //   7486: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7491: pop
/*       */     //   7492: goto -> 7508
/*       */     //   7495: aload #4
/*       */     //   7497: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7500: iconst_3
/*       */     //   7501: aaload
/*       */     //   7502: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7507: pop
/*       */     //   7508: aload_3
/*       */     //   7509: invokevirtual isMailBox : ()Z
/*       */     //   7512: ifeq -> 7556
/*       */     //   7515: aload_3
/*       */     //   7516: iconst_0
/*       */     //   7517: invokevirtual isEmpty : (Z)Z
/*       */     //   7520: ifeq -> 7541
/*       */     //   7523: aload #4
/*       */     //   7525: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7528: sipush #336
/*       */     //   7531: aaload
/*       */     //   7532: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7537: pop
/*       */     //   7538: goto -> 7556
/*       */     //   7541: aload #4
/*       */     //   7543: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7546: sipush #337
/*       */     //   7549: aaload
/*       */     //   7550: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7555: pop
/*       */     //   7556: aload_3
/*       */     //   7557: invokevirtual getTemplateId : ()I
/*       */     //   7560: sipush #272
/*       */     //   7563: if_icmpne -> 7588
/*       */     //   7566: aload_3
/*       */     //   7567: invokevirtual getWasBrandedTo : ()J
/*       */     //   7570: ldc2_w -10
/*       */     //   7573: lcmp
/*       */     //   7574: ifeq -> 7588
/*       */     //   7577: aload_3
/*       */     //   7578: aload_1
/*       */     //   7579: invokevirtual mayCommand : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   7582: ifne -> 7588
/*       */     //   7585: goto -> 7602
/*       */     //   7588: aload #4
/*       */     //   7590: aload_0
/*       */     //   7591: aload_1
/*       */     //   7592: aload_3
/*       */     //   7593: invokespecial makeMoveSubMenu : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;)Ljava/util/List;
/*       */     //   7596: invokeinterface addAll : (Ljava/util/Collection;)Z
/*       */     //   7601: pop
/*       */     //   7602: iload #7
/*       */     //   7604: ifne -> 7619
/*       */     //   7607: aload #4
/*       */     //   7609: aload_1
/*       */     //   7610: aload_2
/*       */     //   7611: aload_3
/*       */     //   7612: invokestatic addCreationEntrys : (Ljava/util/List;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)Ljava/util/List;
/*       */     //   7615: pop
/*       */     //   7616: iconst_1
/*       */     //   7617: istore #7
/*       */     //   7619: aload_3
/*       */     //   7620: invokevirtual isDraggable : ()Z
/*       */     //   7623: ifeq -> 7778
/*       */     //   7626: iconst_1
/*       */     //   7627: istore #13
/*       */     //   7629: aload_3
/*       */     //   7630: invokevirtual isVehicle : ()Z
/*       */     //   7633: ifeq -> 7681
/*       */     //   7636: aload_3
/*       */     //   7637: invokestatic getVehicle : (Lcom/wurmonline/server/items/Item;)Lcom/wurmonline/server/behaviours/Vehicle;
/*       */     //   7640: astore #14
/*       */     //   7642: aload #14
/*       */     //   7644: getfield pilotId : J
/*       */     //   7647: ldc2_w -10
/*       */     //   7650: lcmp
/*       */     //   7651: ifeq -> 7657
/*       */     //   7654: iconst_0
/*       */     //   7655: istore #13
/*       */     //   7657: aload #14
/*       */     //   7659: getfield draggers : Ljava/util/Set;
/*       */     //   7662: ifnull -> 7681
/*       */     //   7665: aload #14
/*       */     //   7667: getfield draggers : Ljava/util/Set;
/*       */     //   7670: invokeinterface isEmpty : ()Z
/*       */     //   7675: ifne -> 7681
/*       */     //   7678: iconst_0
/*       */     //   7679: istore #13
/*       */     //   7681: aload_1
/*       */     //   7682: invokevirtual getVehicle : ()J
/*       */     //   7685: ldc2_w -10
/*       */     //   7688: lcmp
/*       */     //   7689: ifeq -> 7695
/*       */     //   7692: iconst_0
/*       */     //   7693: istore #13
/*       */     //   7695: iload #13
/*       */     //   7697: ifeq -> 7756
/*       */     //   7700: aload_3
/*       */     //   7701: invokestatic isItemDragged : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   7704: ifne -> 7756
/*       */     //   7707: aload_3
/*       */     //   7708: invokevirtual getTopParent : ()J
/*       */     //   7711: aload_3
/*       */     //   7712: invokevirtual getWurmId : ()J
/*       */     //   7715: lcmp
/*       */     //   7716: ifne -> 7756
/*       */     //   7719: aload_1
/*       */     //   7720: aload_3
/*       */     //   7721: invokestatic hasPermission : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;)Z
/*       */     //   7724: istore #14
/*       */     //   7726: iload #14
/*       */     //   7728: ifne -> 7739
/*       */     //   7731: aload_3
/*       */     //   7732: aload_1
/*       */     //   7733: invokevirtual mayDrag : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   7736: ifeq -> 7753
/*       */     //   7739: aload #4
/*       */     //   7741: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7744: bipush #74
/*       */     //   7746: aaload
/*       */     //   7747: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7752: pop
/*       */     //   7753: goto -> 7778
/*       */     //   7756: aload_1
/*       */     //   7757: invokevirtual getDraggedItem : ()Lcom/wurmonline/server/items/Item;
/*       */     //   7760: aload_3
/*       */     //   7761: if_acmpne -> 7778
/*       */     //   7764: aload #4
/*       */     //   7766: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7769: bipush #75
/*       */     //   7771: aaload
/*       */     //   7772: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7777: pop
/*       */     //   7778: iload #5
/*       */     //   7780: sipush #741
/*       */     //   7783: if_icmpne -> 7803
/*       */     //   7786: aload #4
/*       */     //   7788: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7791: bipush #118
/*       */     //   7793: aaload
/*       */     //   7794: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7799: pop
/*       */     //   7800: goto -> 7858
/*       */     //   7803: iload #5
/*       */     //   7805: sipush #739
/*       */     //   7808: if_icmpeq -> 7818
/*       */     //   7811: aload_3
/*       */     //   7812: invokevirtual isWarTarget : ()Z
/*       */     //   7815: ifeq -> 7836
/*       */     //   7818: aload #4
/*       */     //   7820: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7823: sipush #504
/*       */     //   7826: aaload
/*       */     //   7827: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7832: pop
/*       */     //   7833: goto -> 7858
/*       */     //   7836: iload #5
/*       */     //   7838: sipush #722
/*       */     //   7841: if_icmpne -> 7858
/*       */     //   7844: aload #4
/*       */     //   7846: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7849: bipush #118
/*       */     //   7851: aaload
/*       */     //   7852: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7857: pop
/*       */     //   7858: aload_3
/*       */     //   7859: invokevirtual isBed : ()Z
/*       */     //   7862: ifeq -> 7873
/*       */     //   7865: aload_0
/*       */     //   7866: aload_1
/*       */     //   7867: aload_3
/*       */     //   7868: aload #4
/*       */     //   7870: invokespecial addBedOptions : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Ljava/util/List;)V
/*       */     //   7873: iload #6
/*       */     //   7875: bipush #20
/*       */     //   7877: if_icmpne -> 7921
/*       */     //   7880: iload #5
/*       */     //   7882: sipush #692
/*       */     //   7885: if_icmpeq -> 7906
/*       */     //   7888: iload #5
/*       */     //   7890: sipush #696
/*       */     //   7893: if_icmpeq -> 7906
/*       */     //   7896: aload_3
/*       */     //   7897: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   7900: invokevirtual isRiftStoneDeco : ()Z
/*       */     //   7903: ifeq -> 7921
/*       */     //   7906: aload #4
/*       */     //   7908: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7911: sipush #145
/*       */     //   7914: aaload
/*       */     //   7915: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7920: pop
/*       */     //   7921: aload_2
/*       */     //   7922: invokevirtual isWeaponAxe : ()Z
/*       */     //   7925: ifeq -> 7952
/*       */     //   7928: aload_3
/*       */     //   7929: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   7932: invokevirtual isRiftPlantDeco : ()Z
/*       */     //   7935: ifeq -> 7952
/*       */     //   7938: aload #4
/*       */     //   7940: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7943: bipush #96
/*       */     //   7945: aaload
/*       */     //   7946: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7951: pop
/*       */     //   7952: iload #6
/*       */     //   7954: bipush #20
/*       */     //   7956: if_icmpne -> 7984
/*       */     //   7959: aload_3
/*       */     //   7960: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   7963: invokevirtual isRiftCrystalDeco : ()Z
/*       */     //   7966: ifeq -> 7984
/*       */     //   7969: aload #4
/*       */     //   7971: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   7974: sipush #156
/*       */     //   7977: aaload
/*       */     //   7978: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   7983: pop
/*       */     //   7984: iload #10
/*       */     //   7986: ifeq -> 11062
/*       */     //   7989: aload_3
/*       */     //   7990: invokevirtual isFood : ()Z
/*       */     //   7993: ifeq -> 8065
/*       */     //   7996: aload_3
/*       */     //   7997: invokevirtual isNoEatOrDrink : ()Z
/*       */     //   8000: ifne -> 8032
/*       */     //   8003: aload #4
/*       */     //   8005: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8008: bipush #19
/*       */     //   8010: aaload
/*       */     //   8011: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8016: pop
/*       */     //   8017: aload #4
/*       */     //   8019: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8022: sipush #182
/*       */     //   8025: aaload
/*       */     //   8026: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8031: pop
/*       */     //   8032: aload_3
/*       */     //   8033: invokevirtual isEgg : ()Z
/*       */     //   8036: ifeq -> 8430
/*       */     //   8039: iload #5
/*       */     //   8041: sipush #465
/*       */     //   8044: if_icmpne -> 8430
/*       */     //   8047: aload #4
/*       */     //   8049: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8052: sipush #330
/*       */     //   8055: aaload
/*       */     //   8056: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8061: pop
/*       */     //   8062: goto -> 8430
/*       */     //   8065: aload_2
/*       */     //   8066: invokevirtual isColor : ()Z
/*       */     //   8069: ifeq -> 8290
/*       */     //   8072: aload_3
/*       */     //   8073: invokevirtual isColorable : ()Z
/*       */     //   8076: ifeq -> 8165
/*       */     //   8079: aload_3
/*       */     //   8080: invokevirtual getTemplateId : ()I
/*       */     //   8083: sipush #1396
/*       */     //   8086: if_icmpne -> 8116
/*       */     //   8089: aload #4
/*       */     //   8091: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8094: dup
/*       */     //   8095: sipush #231
/*       */     //   8098: ldc_w 'Paint barrel'
/*       */     //   8101: ldc_w 'painting'
/*       */     //   8104: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8107: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8112: pop
/*       */     //   8113: goto -> 8165
/*       */     //   8116: aload_3
/*       */     //   8117: invokevirtual isWood : ()Z
/*       */     //   8120: ifeq -> 8150
/*       */     //   8123: aload #4
/*       */     //   8125: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8128: dup
/*       */     //   8129: sipush #231
/*       */     //   8132: ldc_w 'Paint wood'
/*       */     //   8135: ldc_w 'painting'
/*       */     //   8138: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8141: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8146: pop
/*       */     //   8147: goto -> 8165
/*       */     //   8150: aload #4
/*       */     //   8152: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8155: sipush #231
/*       */     //   8158: aaload
/*       */     //   8159: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8164: pop
/*       */     //   8165: aload_3
/*       */     //   8166: invokevirtual supportsSecondryColor : ()Z
/*       */     //   8169: ifeq -> 8430
/*       */     //   8172: aload_3
/*       */     //   8173: invokevirtual getTemplateId : ()I
/*       */     //   8176: sipush #1396
/*       */     //   8179: if_icmpne -> 8209
/*       */     //   8182: aload #4
/*       */     //   8184: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8187: dup
/*       */     //   8188: sipush #923
/*       */     //   8191: ldc_w 'Paint lamp'
/*       */     //   8194: ldc_w 'painting'
/*       */     //   8197: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8200: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8205: pop
/*       */     //   8206: goto -> 8430
/*       */     //   8209: aload_3
/*       */     //   8210: invokevirtual isColorable : ()Z
/*       */     //   8213: ifeq -> 8263
/*       */     //   8216: aload #4
/*       */     //   8218: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8221: dup
/*       */     //   8222: sipush #923
/*       */     //   8225: new java/lang/StringBuilder
/*       */     //   8228: dup
/*       */     //   8229: invokespecial <init> : ()V
/*       */     //   8232: ldc_w 'Dye '
/*       */     //   8235: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   8238: aload_3
/*       */     //   8239: invokevirtual getSecondryItemName : ()Ljava/lang/String;
/*       */     //   8242: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   8245: invokevirtual toString : ()Ljava/lang/String;
/*       */     //   8248: ldc_w 'dyeing'
/*       */     //   8251: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8254: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8259: pop
/*       */     //   8260: goto -> 8430
/*       */     //   8263: aload #4
/*       */     //   8265: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8268: dup
/*       */     //   8269: sipush #923
/*       */     //   8272: ldc_w 'Paint'
/*       */     //   8275: ldc_w 'painting'
/*       */     //   8278: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8281: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8286: pop
/*       */     //   8287: goto -> 8430
/*       */     //   8290: iload #6
/*       */     //   8292: sipush #676
/*       */     //   8295: if_icmpne -> 8343
/*       */     //   8298: aload_2
/*       */     //   8299: invokevirtual getOwnerId : ()J
/*       */     //   8302: aload_1
/*       */     //   8303: invokevirtual getWurmId : ()J
/*       */     //   8306: lcmp
/*       */     //   8307: ifne -> 8430
/*       */     //   8310: aload #4
/*       */     //   8312: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8315: sipush #472
/*       */     //   8318: aaload
/*       */     //   8319: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8324: pop
/*       */     //   8325: aload #4
/*       */     //   8327: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8330: sipush #510
/*       */     //   8333: aaload
/*       */     //   8334: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8339: pop
/*       */     //   8340: goto -> 8430
/*       */     //   8343: iload #6
/*       */     //   8345: sipush #751
/*       */     //   8348: if_icmpne -> 8389
/*       */     //   8351: iload #5
/*       */     //   8353: sipush #676
/*       */     //   8356: if_icmpne -> 8389
/*       */     //   8359: aload_2
/*       */     //   8360: invokevirtual getOwnerId : ()J
/*       */     //   8363: aload_1
/*       */     //   8364: invokevirtual getWurmId : ()J
/*       */     //   8367: lcmp
/*       */     //   8368: ifne -> 8430
/*       */     //   8371: aload #4
/*       */     //   8373: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8376: sipush #370
/*       */     //   8379: aaload
/*       */     //   8380: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8385: pop
/*       */     //   8386: goto -> 8430
/*       */     //   8389: iload #6
/*       */     //   8391: sipush #867
/*       */     //   8394: if_icmpne -> 8430
/*       */     //   8397: aload_3
/*       */     //   8398: invokestatic mayBeEnchanted : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   8401: ifeq -> 8430
/*       */     //   8404: aload_3
/*       */     //   8405: invokevirtual getRarity : ()B
/*       */     //   8408: aload_2
/*       */     //   8409: invokevirtual getRarity : ()B
/*       */     //   8412: if_icmpge -> 8430
/*       */     //   8415: aload #4
/*       */     //   8417: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8420: sipush #632
/*       */     //   8423: aaload
/*       */     //   8424: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8429: pop
/*       */     //   8430: aload_3
/*       */     //   8431: invokevirtual isColorable : ()Z
/*       */     //   8434: ifeq -> 8520
/*       */     //   8437: aload_3
/*       */     //   8438: getfield color : I
/*       */     //   8441: iconst_m1
/*       */     //   8442: if_icmpeq -> 8520
/*       */     //   8445: aload_3
/*       */     //   8446: invokevirtual isLiquid : ()Z
/*       */     //   8449: ifne -> 8520
/*       */     //   8452: aload_3
/*       */     //   8453: invokevirtual getTemplateId : ()I
/*       */     //   8456: sipush #1396
/*       */     //   8459: if_icmpne -> 8497
/*       */     //   8462: iload #6
/*       */     //   8464: sipush #441
/*       */     //   8467: if_icmpne -> 8520
/*       */     //   8470: aload #4
/*       */     //   8472: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8475: dup
/*       */     //   8476: sipush #232
/*       */     //   8479: ldc_w 'Remove barrel colour'
/*       */     //   8482: ldc_w 'removing'
/*       */     //   8485: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8488: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8493: pop
/*       */     //   8494: goto -> 8520
/*       */     //   8497: iload #6
/*       */     //   8499: sipush #441
/*       */     //   8502: if_icmpne -> 8520
/*       */     //   8505: aload #4
/*       */     //   8507: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8510: sipush #232
/*       */     //   8513: aaload
/*       */     //   8514: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8519: pop
/*       */     //   8520: aload_3
/*       */     //   8521: getfield color2 : I
/*       */     //   8524: iconst_m1
/*       */     //   8525: if_icmpeq -> 8650
/*       */     //   8528: aload_3
/*       */     //   8529: invokevirtual supportsSecondryColor : ()Z
/*       */     //   8532: ifeq -> 8650
/*       */     //   8535: aload_3
/*       */     //   8536: invokevirtual isLiquid : ()Z
/*       */     //   8539: ifne -> 8650
/*       */     //   8542: aload_3
/*       */     //   8543: invokevirtual getTemplateId : ()I
/*       */     //   8546: sipush #1396
/*       */     //   8549: if_icmpne -> 8586
/*       */     //   8552: iload #6
/*       */     //   8554: bipush #73
/*       */     //   8556: if_icmpne -> 8650
/*       */     //   8559: aload #4
/*       */     //   8561: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8564: dup
/*       */     //   8565: sipush #924
/*       */     //   8568: ldc_w 'Remove lamp colour'
/*       */     //   8571: ldc_w 'removing'
/*       */     //   8574: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8577: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8582: pop
/*       */     //   8583: goto -> 8650
/*       */     //   8586: iload #6
/*       */     //   8588: bipush #73
/*       */     //   8590: if_icmpne -> 8611
/*       */     //   8593: aload #4
/*       */     //   8595: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8598: sipush #924
/*       */     //   8601: aaload
/*       */     //   8602: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8607: pop
/*       */     //   8608: goto -> 8650
/*       */     //   8611: aload_3
/*       */     //   8612: invokevirtual isColorable : ()Z
/*       */     //   8615: ifne -> 8650
/*       */     //   8618: iload #6
/*       */     //   8620: sipush #441
/*       */     //   8623: if_icmpne -> 8650
/*       */     //   8626: aload #4
/*       */     //   8628: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8631: dup
/*       */     //   8632: sipush #924
/*       */     //   8635: ldc_w 'Remove paint'
/*       */     //   8638: ldc_w 'removing'
/*       */     //   8641: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8644: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8649: pop
/*       */     //   8650: iload #5
/*       */     //   8652: sipush #1307
/*       */     //   8655: if_icmpne -> 8695
/*       */     //   8658: aload_3
/*       */     //   8659: invokevirtual getData1 : ()I
/*       */     //   8662: ifgt -> 8695
/*       */     //   8665: iload #6
/*       */     //   8667: sipush #441
/*       */     //   8670: if_icmpeq -> 8680
/*       */     //   8673: iload #6
/*       */     //   8675: bipush #97
/*       */     //   8677: if_icmpne -> 8695
/*       */     //   8680: aload #4
/*       */     //   8682: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8685: sipush #911
/*       */     //   8688: aaload
/*       */     //   8689: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8694: pop
/*       */     //   8695: iload #6
/*       */     //   8697: sipush #788
/*       */     //   8700: if_icmpne -> 8800
/*       */     //   8703: aload_3
/*       */     //   8704: invokevirtual isMetal : ()Z
/*       */     //   8707: ifeq -> 8800
/*       */     //   8710: aload_3
/*       */     //   8711: invokevirtual isLiquid : ()Z
/*       */     //   8714: ifne -> 8800
/*       */     //   8717: aload_3
/*       */     //   8718: invokevirtual isIndestructible : ()Z
/*       */     //   8721: ifne -> 8800
/*       */     //   8724: aload_3
/*       */     //   8725: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   8728: invokevirtual isRune : ()Z
/*       */     //   8731: ifne -> 8800
/*       */     //   8734: aload_3
/*       */     //   8735: invokevirtual getTemplateId : ()I
/*       */     //   8738: sipush #1307
/*       */     //   8741: if_icmpeq -> 8800
/*       */     //   8744: aload_3
/*       */     //   8745: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   8748: invokevirtual isStatue : ()Z
/*       */     //   8751: ifne -> 8800
/*       */     //   8754: aload_3
/*       */     //   8755: invokevirtual getTemplateId : ()I
/*       */     //   8758: sipush #1423
/*       */     //   8761: if_icmpeq -> 8800
/*       */     //   8764: aload_3
/*       */     //   8765: invokevirtual getTemperature : ()S
/*       */     //   8768: sipush #6000
/*       */     //   8771: if_icmple -> 8800
/*       */     //   8774: aload_3
/*       */     //   8775: invokevirtual getParentId : ()J
/*       */     //   8778: ldc2_w -10
/*       */     //   8781: lcmp
/*       */     //   8782: ifle -> 8800
/*       */     //   8785: aload #4
/*       */     //   8787: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8790: sipush #519
/*       */     //   8793: aaload
/*       */     //   8794: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8799: pop
/*       */     //   8800: aload_3
/*       */     //   8801: invokevirtual isServerPortal : ()Z
/*       */     //   8804: ifeq -> 8824
/*       */     //   8807: aload #4
/*       */     //   8809: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8812: bipush #118
/*       */     //   8814: aaload
/*       */     //   8815: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8820: pop
/*       */     //   8821: goto -> 8948
/*       */     //   8824: iload #5
/*       */     //   8826: sipush #700
/*       */     //   8829: if_icmpne -> 8894
/*       */     //   8832: aload_2
/*       */     //   8833: invokevirtual isLight : ()Z
/*       */     //   8836: ifne -> 8846
/*       */     //   8839: aload_2
/*       */     //   8840: invokevirtual isFire : ()Z
/*       */     //   8843: ifeq -> 8853
/*       */     //   8846: aload_2
/*       */     //   8847: invokevirtual isOnFire : ()Z
/*       */     //   8850: ifne -> 8877
/*       */     //   8853: iload #6
/*       */     //   8855: sipush #315
/*       */     //   8858: if_icmpeq -> 8877
/*       */     //   8861: iload #6
/*       */     //   8863: sipush #176
/*       */     //   8866: if_icmpeq -> 8877
/*       */     //   8869: iload #6
/*       */     //   8871: sipush #143
/*       */     //   8874: if_icmpne -> 8894
/*       */     //   8877: aload #4
/*       */     //   8879: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8882: bipush #118
/*       */     //   8884: aaload
/*       */     //   8885: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8890: pop
/*       */     //   8891: goto -> 8948
/*       */     //   8894: iload #5
/*       */     //   8896: sipush #972
/*       */     //   8899: if_icmpne -> 8926
/*       */     //   8902: aload #4
/*       */     //   8904: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   8907: dup
/*       */     //   8908: bipush #118
/*       */     //   8910: ldc 'Pat'
/*       */     //   8912: ldc 'patting'
/*       */     //   8914: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   8917: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8922: pop
/*       */     //   8923: goto -> 8948
/*       */     //   8926: iload #5
/*       */     //   8928: sipush #738
/*       */     //   8931: if_icmpne -> 8948
/*       */     //   8934: aload #4
/*       */     //   8936: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8939: bipush #118
/*       */     //   8941: aaload
/*       */     //   8942: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8947: pop
/*       */     //   8948: aload_2
/*       */     //   8949: invokevirtual isColor : ()Z
/*       */     //   8952: ifeq -> 8962
/*       */     //   8955: aload_3
/*       */     //   8956: invokevirtual isColorComponent : ()Z
/*       */     //   8959: ifne -> 8976
/*       */     //   8962: aload_3
/*       */     //   8963: invokevirtual isColor : ()Z
/*       */     //   8966: ifeq -> 8991
/*       */     //   8969: aload_2
/*       */     //   8970: invokevirtual isColorComponent : ()Z
/*       */     //   8973: ifeq -> 8991
/*       */     //   8976: aload #4
/*       */     //   8978: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   8981: sipush #283
/*       */     //   8984: aaload
/*       */     //   8985: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   8990: pop
/*       */     //   8991: iload #6
/*       */     //   8993: sipush #489
/*       */     //   8996: if_icmpeq -> 9023
/*       */     //   8999: aload_1
/*       */     //   9000: invokevirtual getPower : ()I
/*       */     //   9003: iconst_2
/*       */     //   9004: if_icmplt -> 9038
/*       */     //   9007: iload #6
/*       */     //   9009: sipush #176
/*       */     //   9012: if_icmpeq -> 9023
/*       */     //   9015: iload #6
/*       */     //   9017: sipush #315
/*       */     //   9020: if_icmpne -> 9038
/*       */     //   9023: aload #4
/*       */     //   9025: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9028: sipush #329
/*       */     //   9031: aaload
/*       */     //   9032: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9037: pop
/*       */     //   9038: aload_2
/*       */     //   9039: invokevirtual isRoyal : ()Z
/*       */     //   9042: ifeq -> 9109
/*       */     //   9045: iload #6
/*       */     //   9047: sipush #535
/*       */     //   9050: if_icmpeq -> 9069
/*       */     //   9053: iload #6
/*       */     //   9055: sipush #529
/*       */     //   9058: if_icmpeq -> 9069
/*       */     //   9061: iload #6
/*       */     //   9063: sipush #532
/*       */     //   9066: if_icmpne -> 9109
/*       */     //   9069: aload #4
/*       */     //   9071: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9074: sipush #354
/*       */     //   9077: aaload
/*       */     //   9078: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9083: pop
/*       */     //   9084: aload_1
/*       */     //   9085: invokevirtual getKingdomId : ()B
/*       */     //   9088: invokestatic isCustomKingdom : (B)Z
/*       */     //   9091: ifeq -> 9109
/*       */     //   9094: aload #4
/*       */     //   9096: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9099: sipush #719
/*       */     //   9102: aaload
/*       */     //   9103: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9108: pop
/*       */     //   9109: aload_3
/*       */     //   9110: invokevirtual isRoyal : ()Z
/*       */     //   9113: ifeq -> 9185
/*       */     //   9116: iload #5
/*       */     //   9118: sipush #536
/*       */     //   9121: if_icmpeq -> 9140
/*       */     //   9124: iload #5
/*       */     //   9126: sipush #530
/*       */     //   9129: if_icmpeq -> 9140
/*       */     //   9132: iload #5
/*       */     //   9134: sipush #533
/*       */     //   9137: if_icmpne -> 9185
/*       */     //   9140: aload #4
/*       */     //   9142: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9145: sipush #355
/*       */     //   9148: aaload
/*       */     //   9149: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9154: pop
/*       */     //   9155: aload #4
/*       */     //   9157: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9160: sipush #356
/*       */     //   9163: aaload
/*       */     //   9164: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9169: pop
/*       */     //   9170: aload #4
/*       */     //   9172: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9175: sipush #358
/*       */     //   9178: aaload
/*       */     //   9179: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9184: pop
/*       */     //   9185: iconst_0
/*       */     //   9186: istore #11
/*       */     //   9188: ldc_w ''
/*       */     //   9191: astore #12
/*       */     //   9193: aload_3
/*       */     //   9194: invokevirtual isContainerLiquid : ()Z
/*       */     //   9197: ifeq -> 9310
/*       */     //   9200: aload_3
/*       */     //   9201: invokevirtual getItemCount : ()I
/*       */     //   9204: iconst_1
/*       */     //   9205: if_icmpne -> 9310
/*       */     //   9208: aload_3
/*       */     //   9209: invokevirtual getItems : ()Ljava/util/Set;
/*       */     //   9212: invokeinterface iterator : ()Ljava/util/Iterator;
/*       */     //   9217: astore #13
/*       */     //   9219: aload #13
/*       */     //   9221: invokeinterface hasNext : ()Z
/*       */     //   9226: ifeq -> 9307
/*       */     //   9229: aload #13
/*       */     //   9231: invokeinterface next : ()Ljava/lang/Object;
/*       */     //   9236: checkcast com/wurmonline/server/items/Item
/*       */     //   9239: astore #14
/*       */     //   9241: aload #14
/*       */     //   9243: invokevirtual isNoEatOrDrink : ()Z
/*       */     //   9246: ifne -> 9304
/*       */     //   9249: aload #14
/*       */     //   9251: invokevirtual isUndistilled : ()Z
/*       */     //   9254: ifne -> 9304
/*       */     //   9257: aload #14
/*       */     //   9259: invokevirtual isDrinkable : ()Z
/*       */     //   9262: ifeq -> 9304
/*       */     //   9265: aload_3
/*       */     //   9266: invokevirtual isSealedByPlayer : ()Z
/*       */     //   9269: ifne -> 9304
/*       */     //   9272: aload #4
/*       */     //   9274: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9277: bipush #19
/*       */     //   9279: aaload
/*       */     //   9280: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9285: pop
/*       */     //   9286: aload #4
/*       */     //   9288: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9291: sipush #183
/*       */     //   9294: aaload
/*       */     //   9295: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9300: pop
/*       */     //   9301: goto -> 9307
/*       */     //   9304: goto -> 9219
/*       */     //   9307: goto -> 9660
/*       */     //   9310: aload_3
/*       */     //   9311: invokevirtual isLiquid : ()Z
/*       */     //   9314: ifeq -> 9561
/*       */     //   9317: aload_3
/*       */     //   9318: invokevirtual isFood : ()Z
/*       */     //   9321: ifne -> 9374
/*       */     //   9324: aload_3
/*       */     //   9325: invokevirtual isNoEatOrDrink : ()Z
/*       */     //   9328: ifne -> 9374
/*       */     //   9331: aload_3
/*       */     //   9332: invokevirtual isUndistilled : ()Z
/*       */     //   9335: ifne -> 9374
/*       */     //   9338: aload_3
/*       */     //   9339: invokevirtual isDrinkable : ()Z
/*       */     //   9342: ifeq -> 9374
/*       */     //   9345: aload #4
/*       */     //   9347: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9350: bipush #19
/*       */     //   9352: aaload
/*       */     //   9353: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9358: pop
/*       */     //   9359: aload #4
/*       */     //   9361: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9364: sipush #183
/*       */     //   9367: aaload
/*       */     //   9368: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9373: pop
/*       */     //   9374: aload_2
/*       */     //   9375: invokevirtual isContainerLiquid : ()Z
/*       */     //   9378: ifne -> 9388
/*       */     //   9381: aload_2
/*       */     //   9382: invokevirtual isOilConsuming : ()Z
/*       */     //   9385: ifeq -> 9422
/*       */     //   9388: aload_2
/*       */     //   9389: invokevirtual getWurmId : ()J
/*       */     //   9392: aload_3
/*       */     //   9393: invokevirtual getParentId : ()J
/*       */     //   9396: lcmp
/*       */     //   9397: ifeq -> 9422
/*       */     //   9400: aload_2
/*       */     //   9401: invokevirtual isSealedByPlayer : ()Z
/*       */     //   9404: ifne -> 9422
/*       */     //   9407: aload #4
/*       */     //   9409: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9412: sipush #189
/*       */     //   9415: aaload
/*       */     //   9416: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9421: pop
/*       */     //   9422: iload #6
/*       */     //   9424: sipush #386
/*       */     //   9427: if_icmpne -> 9490
/*       */     //   9430: aload_2
/*       */     //   9431: invokestatic getImproveMaterial : (Lcom/wurmonline/server/items/Item;)B
/*       */     //   9434: istore #13
/*       */     //   9436: iload #13
/*       */     //   9438: aload_2
/*       */     //   9439: getfield creationState : B
/*       */     //   9442: invokestatic getItemForImprovement : (BB)I
/*       */     //   9445: istore #14
/*       */     //   9447: iload #14
/*       */     //   9449: iload #5
/*       */     //   9451: if_icmpne -> 9487
/*       */     //   9454: iload #13
/*       */     //   9456: aload_2
/*       */     //   9457: getfield creationState : B
/*       */     //   9460: invokestatic getImproveAction : (BB)Ljava/lang/String;
/*       */     //   9463: astore #12
/*       */     //   9465: aload #4
/*       */     //   9467: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   9470: dup
/*       */     //   9471: sipush #228
/*       */     //   9474: aload #12
/*       */     //   9476: aload #12
/*       */     //   9478: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   9481: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9486: pop
/*       */     //   9487: goto -> 9660
/*       */     //   9490: aload_2
/*       */     //   9491: invokevirtual isRepairable : ()Z
/*       */     //   9494: ifeq -> 9660
/*       */     //   9497: aload_2
/*       */     //   9498: getfield creationState : B
/*       */     //   9501: ifeq -> 9660
/*       */     //   9504: aload_2
/*       */     //   9505: invokevirtual isNewbieItem : ()Z
/*       */     //   9508: ifne -> 9660
/*       */     //   9511: aload_2
/*       */     //   9512: invokevirtual isChallengeNewbieItem : ()Z
/*       */     //   9515: ifne -> 9660
/*       */     //   9518: aload_2
/*       */     //   9519: invokestatic getImproveMaterial : (Lcom/wurmonline/server/items/Item;)B
/*       */     //   9522: istore #13
/*       */     //   9524: iload #13
/*       */     //   9526: aload_2
/*       */     //   9527: getfield creationState : B
/*       */     //   9530: invokestatic getItemForImprovement : (BB)I
/*       */     //   9533: istore #14
/*       */     //   9535: iload #14
/*       */     //   9537: aload_3
/*       */     //   9538: invokevirtual getTemplateId : ()I
/*       */     //   9541: if_icmpne -> 9558
/*       */     //   9544: iconst_1
/*       */     //   9545: istore #11
/*       */     //   9547: iload #13
/*       */     //   9549: aload_2
/*       */     //   9550: getfield creationState : B
/*       */     //   9553: invokestatic getImproveAction : (BB)Ljava/lang/String;
/*       */     //   9556: astore #12
/*       */     //   9558: goto -> 9660
/*       */     //   9561: aload_3
/*       */     //   9562: invokevirtual isOilConsuming : ()Z
/*       */     //   9565: ifeq -> 9593
/*       */     //   9568: aload_2
/*       */     //   9569: invokevirtual isLiquidInflammable : ()Z
/*       */     //   9572: ifeq -> 9660
/*       */     //   9575: aload #4
/*       */     //   9577: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9580: sipush #189
/*       */     //   9583: aaload
/*       */     //   9584: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9589: pop
/*       */     //   9590: goto -> 9660
/*       */     //   9593: aload_3
/*       */     //   9594: invokevirtual isCandleHolder : ()Z
/*       */     //   9597: ifeq -> 9660
/*       */     //   9600: iload #6
/*       */     //   9602: sipush #133
/*       */     //   9605: if_icmpne -> 9660
/*       */     //   9608: aload_3
/*       */     //   9609: invokevirtual getTemplateId : ()I
/*       */     //   9612: sipush #729
/*       */     //   9615: if_icmpne -> 9645
/*       */     //   9618: aload #4
/*       */     //   9620: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   9623: dup
/*       */     //   9624: sipush #189
/*       */     //   9627: ldc_w 'Add candles'
/*       */     //   9630: ldc_w 'adding'
/*       */     //   9633: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   9636: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9641: pop
/*       */     //   9642: goto -> 9660
/*       */     //   9645: aload #4
/*       */     //   9647: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9650: sipush #189
/*       */     //   9653: aaload
/*       */     //   9654: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9659: pop
/*       */     //   9660: iload #6
/*       */     //   9662: sipush #654
/*       */     //   9665: if_icmpne -> 9690
/*       */     //   9668: aload_2
/*       */     //   9669: invokevirtual getAuxData : ()B
/*       */     //   9672: ifne -> 9690
/*       */     //   9675: aload #4
/*       */     //   9677: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9680: sipush #462
/*       */     //   9683: aaload
/*       */     //   9684: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9689: pop
/*       */     //   9690: aload_3
/*       */     //   9691: invokevirtual isRepairable : ()Z
/*       */     //   9694: ifne -> 9718
/*       */     //   9697: iload #11
/*       */     //   9699: ifne -> 9718
/*       */     //   9702: iload #5
/*       */     //   9704: sipush #179
/*       */     //   9707: if_icmpeq -> 9718
/*       */     //   9710: iload #5
/*       */     //   9712: sipush #386
/*       */     //   9715: if_icmpne -> 9966
/*       */     //   9718: aload_3
/*       */     //   9719: invokestatic getImproveSkill : (Lcom/wurmonline/server/items/Item;)I
/*       */     //   9722: istore #13
/*       */     //   9724: aload_3
/*       */     //   9725: invokestatic getImproveMaterial : (Lcom/wurmonline/server/items/Item;)B
/*       */     //   9728: istore #14
/*       */     //   9730: aload_3
/*       */     //   9731: invokestatic getImproveTemplateId : (Lcom/wurmonline/server/items/Item;)I
/*       */     //   9734: istore #15
/*       */     //   9736: aload_3
/*       */     //   9737: getfield creationState : B
/*       */     //   9740: ifeq -> 9754
/*       */     //   9743: iload #14
/*       */     //   9745: aload_3
/*       */     //   9746: getfield creationState : B
/*       */     //   9749: invokestatic getItemForImprovement : (BB)I
/*       */     //   9752: istore #15
/*       */     //   9754: iload #13
/*       */     //   9756: bipush #-10
/*       */     //   9758: if_icmpeq -> 9798
/*       */     //   9761: iload #15
/*       */     //   9763: aload_2
/*       */     //   9764: invokevirtual getTemplateId : ()I
/*       */     //   9767: if_icmpeq -> 9788
/*       */     //   9770: aload_2
/*       */     //   9771: invokevirtual getTemplateId : ()I
/*       */     //   9774: sipush #176
/*       */     //   9777: if_icmpne -> 9798
/*       */     //   9780: aload_1
/*       */     //   9781: invokevirtual getPower : ()I
/*       */     //   9784: iconst_2
/*       */     //   9785: if_icmplt -> 9798
/*       */     //   9788: aload_3
/*       */     //   9789: invokevirtual isNewbieItem : ()Z
/*       */     //   9792: ifne -> 9798
/*       */     //   9795: iconst_1
/*       */     //   9796: istore #11
/*       */     //   9798: iload #11
/*       */     //   9800: ifeq -> 9926
/*       */     //   9803: aload_3
/*       */     //   9804: getfield creationState : B
/*       */     //   9807: ifeq -> 9870
/*       */     //   9810: aload_3
/*       */     //   9811: invokevirtual isGuardTower : ()Z
/*       */     //   9814: ifeq -> 9826
/*       */     //   9817: aload_3
/*       */     //   9818: invokevirtual getDamage : ()F
/*       */     //   9821: fconst_0
/*       */     //   9822: fcmpl
/*       */     //   9823: ifne -> 9926
/*       */     //   9826: iload #14
/*       */     //   9828: aload_3
/*       */     //   9829: getfield creationState : B
/*       */     //   9832: invokestatic getImproveAction : (BB)Ljava/lang/String;
/*       */     //   9835: astore #12
/*       */     //   9837: aload #4
/*       */     //   9839: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   9842: dup
/*       */     //   9843: sipush #192
/*       */     //   9846: aload #12
/*       */     //   9848: aload #12
/*       */     //   9850: iconst_1
/*       */     //   9851: newarray int
/*       */     //   9853: dup
/*       */     //   9854: iconst_0
/*       */     //   9855: bipush #43
/*       */     //   9857: iastore
/*       */     //   9858: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   9861: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9866: pop
/*       */     //   9867: goto -> 9926
/*       */     //   9870: aload #12
/*       */     //   9872: invokevirtual isEmpty : ()Z
/*       */     //   9875: ifne -> 9911
/*       */     //   9878: aload #4
/*       */     //   9880: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   9883: dup
/*       */     //   9884: sipush #192
/*       */     //   9887: aload #12
/*       */     //   9889: aload #12
/*       */     //   9891: iconst_1
/*       */     //   9892: newarray int
/*       */     //   9894: dup
/*       */     //   9895: iconst_0
/*       */     //   9896: bipush #43
/*       */     //   9898: iastore
/*       */     //   9899: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   9902: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9907: pop
/*       */     //   9908: goto -> 9926
/*       */     //   9911: aload #4
/*       */     //   9913: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9916: sipush #192
/*       */     //   9919: aaload
/*       */     //   9920: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9925: pop
/*       */     //   9926: aload_3
/*       */     //   9927: invokevirtual isLiquid : ()Z
/*       */     //   9930: ifne -> 9966
/*       */     //   9933: aload_3
/*       */     //   9934: invokevirtual isKingdomMarker : ()Z
/*       */     //   9937: ifeq -> 9951
/*       */     //   9940: aload_1
/*       */     //   9941: aload_3
/*       */     //   9942: invokevirtual getAuxData : ()B
/*       */     //   9945: invokevirtual isFriendlyKingdom : (B)Z
/*       */     //   9948: ifeq -> 9966
/*       */     //   9951: aload #4
/*       */     //   9953: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   9956: sipush #162
/*       */     //   9959: aaload
/*       */     //   9960: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   9965: pop
/*       */     //   9966: aload_1
/*       */     //   9967: aload_3
/*       */     //   9968: invokevirtual mayDestroy : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   9971: ifeq -> 10093
/*       */     //   9974: aload_3
/*       */     //   9975: invokevirtual isIndestructible : ()Z
/*       */     //   9978: ifne -> 10093
/*       */     //   9981: aload_3
/*       */     //   9982: invokevirtual isRoadMarker : ()Z
/*       */     //   9985: ifne -> 10093
/*       */     //   9988: aload #4
/*       */     //   9990: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   9993: dup
/*       */     //   9994: iconst_m1
/*       */     //   9995: ldc_w 'Bash'
/*       */     //   9998: ldc_w 'Bash'
/*       */     //   10001: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   10004: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10009: pop
/*       */     //   10010: aload_3
/*       */     //   10011: invokevirtual isWarmachine : ()Z
/*       */     //   10014: ifne -> 10054
/*       */     //   10017: aload_3
/*       */     //   10018: invokevirtual getTemplateId : ()I
/*       */     //   10021: sipush #938
/*       */     //   10024: if_icmpeq -> 10054
/*       */     //   10027: aload_3
/*       */     //   10028: invokevirtual getTemplateId : ()I
/*       */     //   10031: sipush #931
/*       */     //   10034: if_icmpeq -> 10054
/*       */     //   10037: aload #4
/*       */     //   10039: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10042: bipush #83
/*       */     //   10044: aaload
/*       */     //   10045: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10050: pop
/*       */     //   10051: goto -> 10093
/*       */     //   10054: aload #4
/*       */     //   10056: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   10059: dup
/*       */     //   10060: bipush #83
/*       */     //   10062: ldc_w 'Destroy'
/*       */     //   10065: ldc_w 'Destroying'
/*       */     //   10068: iconst_3
/*       */     //   10069: newarray int
/*       */     //   10071: dup
/*       */     //   10072: iconst_0
/*       */     //   10073: iconst_5
/*       */     //   10074: iastore
/*       */     //   10075: dup
/*       */     //   10076: iconst_1
/*       */     //   10077: iconst_4
/*       */     //   10078: iastore
/*       */     //   10079: dup
/*       */     //   10080: iconst_2
/*       */     //   10081: bipush #43
/*       */     //   10083: iastore
/*       */     //   10084: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   10087: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10092: pop
/*       */     //   10093: aload_3
/*       */     //   10094: invokevirtual isWeaponBow : ()Z
/*       */     //   10097: ifeq -> 10115
/*       */     //   10100: aload #4
/*       */     //   10102: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10105: sipush #133
/*       */     //   10108: aaload
/*       */     //   10109: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10114: pop
/*       */     //   10115: aload_2
/*       */     //   10116: invokevirtual isHolyItem : ()Z
/*       */     //   10119: ifeq -> 10274
/*       */     //   10122: aload_3
/*       */     //   10123: invokevirtual isInventory : ()Z
/*       */     //   10126: ifne -> 10274
/*       */     //   10129: aload_3
/*       */     //   10130: invokevirtual isTemporary : ()Z
/*       */     //   10133: ifne -> 10274
/*       */     //   10136: aload_3
/*       */     //   10137: invokevirtual isNotSpellTarget : ()Z
/*       */     //   10140: ifne -> 10274
/*       */     //   10143: aload_1
/*       */     //   10144: invokevirtual getDeity : ()Lcom/wurmonline/server/deities/Deity;
/*       */     //   10147: ifnull -> 10274
/*       */     //   10150: aload_1
/*       */     //   10151: invokevirtual isPriest : ()Z
/*       */     //   10154: ifne -> 10164
/*       */     //   10157: aload_1
/*       */     //   10158: invokevirtual getPower : ()I
/*       */     //   10161: ifle -> 10274
/*       */     //   10164: aload_2
/*       */     //   10165: aload_1
/*       */     //   10166: invokevirtual getDeity : ()Lcom/wurmonline/server/deities/Deity;
/*       */     //   10169: invokevirtual isHolyItem : (Lcom/wurmonline/server/deities/Deity;)Z
/*       */     //   10172: ifeq -> 10274
/*       */     //   10175: aload_1
/*       */     //   10176: invokevirtual getFaith : ()F
/*       */     //   10179: fstore #13
/*       */     //   10181: aload_1
/*       */     //   10182: invokevirtual getDeity : ()Lcom/wurmonline/server/deities/Deity;
/*       */     //   10185: fload #13
/*       */     //   10187: f2i
/*       */     //   10188: invokevirtual getSpellsTargettingItems : (I)[Lcom/wurmonline/server/spells/Spell;
/*       */     //   10191: astore #14
/*       */     //   10193: aload #14
/*       */     //   10195: arraylength
/*       */     //   10196: ifle -> 10274
/*       */     //   10199: aload #4
/*       */     //   10201: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   10204: dup
/*       */     //   10205: aload #14
/*       */     //   10207: arraylength
/*       */     //   10208: ineg
/*       */     //   10209: i2s
/*       */     //   10210: ldc_w 'Spells'
/*       */     //   10213: ldc_w 'spells'
/*       */     //   10216: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   10219: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10224: pop
/*       */     //   10225: aload #14
/*       */     //   10227: astore #15
/*       */     //   10229: aload #15
/*       */     //   10231: arraylength
/*       */     //   10232: istore #16
/*       */     //   10234: iconst_0
/*       */     //   10235: istore #17
/*       */     //   10237: iload #17
/*       */     //   10239: iload #16
/*       */     //   10241: if_icmpge -> 10274
/*       */     //   10244: aload #15
/*       */     //   10246: iload #17
/*       */     //   10248: aaload
/*       */     //   10249: astore #18
/*       */     //   10251: aload #4
/*       */     //   10253: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10256: aload #18
/*       */     //   10258: getfield number : I
/*       */     //   10261: aaload
/*       */     //   10262: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10267: pop
/*       */     //   10268: iinc #17, 1
/*       */     //   10271: goto -> 10237
/*       */     //   10274: aload_3
/*       */     //   10275: invokevirtual getOwnerId : ()J
/*       */     //   10278: aload_1
/*       */     //   10279: invokevirtual getWurmId : ()J
/*       */     //   10282: lcmp
/*       */     //   10283: ifeq -> 10306
/*       */     //   10286: aload_3
/*       */     //   10287: getfield lastOwner : J
/*       */     //   10290: aload_1
/*       */     //   10291: invokevirtual getWurmId : ()J
/*       */     //   10294: lcmp
/*       */     //   10295: ifeq -> 10306
/*       */     //   10298: aload_1
/*       */     //   10299: invokevirtual getPower : ()I
/*       */     //   10302: iconst_2
/*       */     //   10303: if_icmplt -> 10348
/*       */     //   10306: aload_3
/*       */     //   10307: invokevirtual isNoRename : ()Z
/*       */     //   10310: ifne -> 10348
/*       */     //   10313: aload_3
/*       */     //   10314: invokevirtual isVehicle : ()Z
/*       */     //   10317: ifeq -> 10334
/*       */     //   10320: aload_3
/*       */     //   10321: invokevirtual isChair : ()Z
/*       */     //   10324: ifne -> 10334
/*       */     //   10327: aload_3
/*       */     //   10328: invokevirtual isTent : ()Z
/*       */     //   10331: ifeq -> 10348
/*       */     //   10334: aload #4
/*       */     //   10336: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10339: bipush #59
/*       */     //   10341: aaload
/*       */     //   10342: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10347: pop
/*       */     //   10348: aload_3
/*       */     //   10349: invokevirtual isEpicTargetItem : ()Z
/*       */     //   10352: ifne -> 10362
/*       */     //   10355: aload_3
/*       */     //   10356: invokevirtual isKingdomMarker : ()Z
/*       */     //   10359: ifeq -> 10680
/*       */     //   10362: iload #6
/*       */     //   10364: sipush #473
/*       */     //   10367: aload_3
/*       */     //   10368: invokevirtual getWurmId : ()J
/*       */     //   10371: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10374: astore #13
/*       */     //   10376: aload #13
/*       */     //   10378: arraylength
/*       */     //   10379: ifle -> 10397
/*       */     //   10382: aload #4
/*       */     //   10384: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10387: sipush #473
/*       */     //   10390: aaload
/*       */     //   10391: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10396: pop
/*       */     //   10397: iload #6
/*       */     //   10399: sipush #474
/*       */     //   10402: aload_3
/*       */     //   10403: invokevirtual getWurmId : ()J
/*       */     //   10406: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10409: astore #14
/*       */     //   10411: aload #14
/*       */     //   10413: arraylength
/*       */     //   10414: ifle -> 10432
/*       */     //   10417: aload #4
/*       */     //   10419: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10422: sipush #474
/*       */     //   10425: aaload
/*       */     //   10426: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10431: pop
/*       */     //   10432: iload #6
/*       */     //   10434: sipush #501
/*       */     //   10437: aload_3
/*       */     //   10438: invokevirtual getWurmId : ()J
/*       */     //   10441: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10444: astore #15
/*       */     //   10446: aload #15
/*       */     //   10448: arraylength
/*       */     //   10449: ifle -> 10467
/*       */     //   10452: aload #4
/*       */     //   10454: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10457: sipush #501
/*       */     //   10460: aaload
/*       */     //   10461: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10466: pop
/*       */     //   10467: iload #6
/*       */     //   10469: sipush #496
/*       */     //   10472: aload_3
/*       */     //   10473: invokevirtual getWurmId : ()J
/*       */     //   10476: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10479: astore #16
/*       */     //   10481: aload #16
/*       */     //   10483: arraylength
/*       */     //   10484: ifle -> 10502
/*       */     //   10487: aload #4
/*       */     //   10489: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10492: sipush #496
/*       */     //   10495: aaload
/*       */     //   10496: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10501: pop
/*       */     //   10502: iload #6
/*       */     //   10504: sipush #498
/*       */     //   10507: aload_3
/*       */     //   10508: invokevirtual getWurmId : ()J
/*       */     //   10511: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10514: astore #17
/*       */     //   10516: aload #17
/*       */     //   10518: arraylength
/*       */     //   10519: ifle -> 10537
/*       */     //   10522: aload #4
/*       */     //   10524: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10527: sipush #498
/*       */     //   10530: aaload
/*       */     //   10531: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10536: pop
/*       */     //   10537: iload #6
/*       */     //   10539: sipush #500
/*       */     //   10542: aload_3
/*       */     //   10543: invokevirtual getWurmId : ()J
/*       */     //   10546: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10549: astore #18
/*       */     //   10551: aload #18
/*       */     //   10553: arraylength
/*       */     //   10554: ifle -> 10572
/*       */     //   10557: aload #4
/*       */     //   10559: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10562: sipush #500
/*       */     //   10565: aaload
/*       */     //   10566: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10571: pop
/*       */     //   10572: iload #6
/*       */     //   10574: sipush #502
/*       */     //   10577: aload_3
/*       */     //   10578: invokevirtual getWurmId : ()J
/*       */     //   10581: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10584: astore #19
/*       */     //   10586: aload #19
/*       */     //   10588: arraylength
/*       */     //   10589: ifle -> 10607
/*       */     //   10592: aload #4
/*       */     //   10594: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10597: sipush #502
/*       */     //   10600: aaload
/*       */     //   10601: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10606: pop
/*       */     //   10607: iload #6
/*       */     //   10609: sipush #497
/*       */     //   10612: aload_3
/*       */     //   10613: invokevirtual getWurmId : ()J
/*       */     //   10616: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10619: astore #20
/*       */     //   10621: aload #20
/*       */     //   10623: arraylength
/*       */     //   10624: ifle -> 10642
/*       */     //   10627: aload #4
/*       */     //   10629: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10632: sipush #497
/*       */     //   10635: aaload
/*       */     //   10636: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10641: pop
/*       */     //   10642: iload #6
/*       */     //   10644: sipush #499
/*       */     //   10647: aload_3
/*       */     //   10648: invokevirtual getWurmId : ()J
/*       */     //   10651: invokestatic getMissionTriggersWith : (IIJ)[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10654: astore #21
/*       */     //   10656: aload #21
/*       */     //   10658: arraylength
/*       */     //   10659: ifle -> 10677
/*       */     //   10662: aload #4
/*       */     //   10664: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10667: sipush #499
/*       */     //   10670: aaload
/*       */     //   10671: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10676: pop
/*       */     //   10677: goto -> 11059
/*       */     //   10680: iload #5
/*       */     //   10682: sipush #1172
/*       */     //   10685: if_icmpne -> 10910
/*       */     //   10688: aload #4
/*       */     //   10690: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   10693: dup
/*       */     //   10694: bipush #-13
/*       */     //   10696: ldc 'Set volume to'
/*       */     //   10698: ldc 'setting'
/*       */     //   10700: iconst_0
/*       */     //   10701: newarray int
/*       */     //   10703: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   10706: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10711: pop
/*       */     //   10712: aload #4
/*       */     //   10714: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10717: sipush #737
/*       */     //   10720: aaload
/*       */     //   10721: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10726: pop
/*       */     //   10727: aload #4
/*       */     //   10729: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10732: sipush #736
/*       */     //   10735: aaload
/*       */     //   10736: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10741: pop
/*       */     //   10742: aload #4
/*       */     //   10744: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10747: sipush #735
/*       */     //   10750: aaload
/*       */     //   10751: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10756: pop
/*       */     //   10757: aload #4
/*       */     //   10759: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10762: sipush #734
/*       */     //   10765: aaload
/*       */     //   10766: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10771: pop
/*       */     //   10772: aload #4
/*       */     //   10774: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10777: sipush #733
/*       */     //   10780: aaload
/*       */     //   10781: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10786: pop
/*       */     //   10787: aload #4
/*       */     //   10789: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10792: sipush #732
/*       */     //   10795: aaload
/*       */     //   10796: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10801: pop
/*       */     //   10802: aload #4
/*       */     //   10804: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10807: sipush #731
/*       */     //   10810: aaload
/*       */     //   10811: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10816: pop
/*       */     //   10817: aload #4
/*       */     //   10819: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10822: sipush #730
/*       */     //   10825: aaload
/*       */     //   10826: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10831: pop
/*       */     //   10832: aload #4
/*       */     //   10834: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10837: sipush #729
/*       */     //   10840: aaload
/*       */     //   10841: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10846: pop
/*       */     //   10847: aload #4
/*       */     //   10849: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10852: sipush #728
/*       */     //   10855: aaload
/*       */     //   10856: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10861: pop
/*       */     //   10862: aload #4
/*       */     //   10864: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10867: sipush #727
/*       */     //   10870: aaload
/*       */     //   10871: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10876: pop
/*       */     //   10877: aload #4
/*       */     //   10879: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10882: sipush #726
/*       */     //   10885: aaload
/*       */     //   10886: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10891: pop
/*       */     //   10892: aload #4
/*       */     //   10894: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10897: sipush #725
/*       */     //   10900: aaload
/*       */     //   10901: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10906: pop
/*       */     //   10907: goto -> 11059
/*       */     //   10910: iload #5
/*       */     //   10912: sipush #200
/*       */     //   10915: if_icmpeq -> 10961
/*       */     //   10918: iload #5
/*       */     //   10920: sipush #1192
/*       */     //   10923: if_icmpeq -> 10961
/*       */     //   10926: iload #5
/*       */     //   10928: bipush #69
/*       */     //   10930: if_icmpeq -> 10961
/*       */     //   10933: iload #5
/*       */     //   10935: bipush #66
/*       */     //   10937: if_icmpeq -> 10961
/*       */     //   10940: iload #5
/*       */     //   10942: bipush #68
/*       */     //   10944: if_icmpeq -> 10961
/*       */     //   10947: iload #5
/*       */     //   10949: bipush #29
/*       */     //   10951: if_icmpeq -> 10961
/*       */     //   10954: iload #5
/*       */     //   10956: bipush #32
/*       */     //   10958: if_icmpne -> 10979
/*       */     //   10961: aload #4
/*       */     //   10963: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   10966: sipush #936
/*       */     //   10969: aaload
/*       */     //   10970: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   10975: pop
/*       */     //   10976: goto -> 11059
/*       */     //   10979: aload_3
/*       */     //   10980: invokevirtual isFish : ()Z
/*       */     //   10983: ifeq -> 11036
/*       */     //   10986: aload_3
/*       */     //   10987: invokevirtual getWeightGrams : ()I
/*       */     //   10990: sipush #300
/*       */     //   10993: if_icmpge -> 11036
/*       */     //   10996: iload #6
/*       */     //   10998: sipush #258
/*       */     //   11001: if_icmpeq -> 11018
/*       */     //   11004: iload #6
/*       */     //   11006: bipush #93
/*       */     //   11008: if_icmpeq -> 11018
/*       */     //   11011: iload #6
/*       */     //   11013: bipush #126
/*       */     //   11015: if_icmpne -> 11036
/*       */     //   11018: aload #4
/*       */     //   11020: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11023: sipush #936
/*       */     //   11026: aaload
/*       */     //   11027: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11032: pop
/*       */     //   11033: goto -> 11059
/*       */     //   11036: iload #5
/*       */     //   11038: sipush #479
/*       */     //   11041: if_icmpne -> 11059
/*       */     //   11044: aload #4
/*       */     //   11046: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11049: sipush #937
/*       */     //   11052: aaload
/*       */     //   11053: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11058: pop
/*       */     //   11059: goto -> 11264
/*       */     //   11062: aload_3
/*       */     //   11063: invokevirtual getTemplateId : ()I
/*       */     //   11066: sipush #931
/*       */     //   11069: if_icmpne -> 11264
/*       */     //   11072: aload_1
/*       */     //   11073: aload_3
/*       */     //   11074: invokevirtual getPosX : ()F
/*       */     //   11077: aload_3
/*       */     //   11078: invokevirtual getPosY : ()F
/*       */     //   11081: aload_3
/*       */     //   11082: invokevirtual getPosZ : ()F
/*       */     //   11085: ldc 4.0
/*       */     //   11087: invokevirtual isWithinDistanceTo : (FFFF)Z
/*       */     //   11090: ifeq -> 11264
/*       */     //   11093: aload_1
/*       */     //   11094: aload_3
/*       */     //   11095: iconst_4
/*       */     //   11096: invokestatic getBlockerBetween : (Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;I)Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   11099: astore #11
/*       */     //   11101: iconst_1
/*       */     //   11102: istore #12
/*       */     //   11104: aload #11
/*       */     //   11106: ifnonnull -> 11115
/*       */     //   11109: iconst_0
/*       */     //   11110: istore #12
/*       */     //   11112: goto -> 11176
/*       */     //   11115: aload #11
/*       */     //   11117: invokevirtual getFirstBlocker : ()Lcom/wurmonline/server/structures/Blocker;
/*       */     //   11120: aload #11
/*       */     //   11122: invokevirtual getLastBlocker : ()Lcom/wurmonline/server/structures/Blocker;
/*       */     //   11125: if_acmpne -> 11176
/*       */     //   11128: aload #11
/*       */     //   11130: invokevirtual getFirstBlocker : ()Lcom/wurmonline/server/structures/Blocker;
/*       */     //   11133: invokeinterface isFence : ()Z
/*       */     //   11138: ifeq -> 11176
/*       */     //   11141: aload #11
/*       */     //   11143: invokevirtual getFirstBlocker : ()Lcom/wurmonline/server/structures/Blocker;
/*       */     //   11146: instanceof com/wurmonline/server/structures/Fence
/*       */     //   11149: ifeq -> 11176
/*       */     //   11152: aload #11
/*       */     //   11154: invokevirtual getFirstBlocker : ()Lcom/wurmonline/server/structures/Blocker;
/*       */     //   11157: checkcast com/wurmonline/server/structures/Fence
/*       */     //   11160: astore #13
/*       */     //   11162: aload #13
/*       */     //   11164: invokevirtual getType : ()Lcom/wurmonline/shared/constants/StructureConstantsEnum;
/*       */     //   11167: getstatic com/wurmonline/shared/constants/StructureConstantsEnum.FENCE_SIEGEWALL : Lcom/wurmonline/shared/constants/StructureConstantsEnum;
/*       */     //   11170: if_acmpne -> 11176
/*       */     //   11173: iconst_0
/*       */     //   11174: istore #12
/*       */     //   11176: iload #12
/*       */     //   11178: ifne -> 11264
/*       */     //   11181: aload_1
/*       */     //   11182: aload_3
/*       */     //   11183: invokevirtual mayDestroy : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   11186: ifeq -> 11264
/*       */     //   11189: aload_3
/*       */     //   11190: invokevirtual isIndestructible : ()Z
/*       */     //   11193: ifne -> 11264
/*       */     //   11196: aload_3
/*       */     //   11197: invokevirtual isRoadMarker : ()Z
/*       */     //   11200: ifne -> 11264
/*       */     //   11203: aload #4
/*       */     //   11205: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   11208: dup
/*       */     //   11209: iconst_m1
/*       */     //   11210: ldc_w 'Bash'
/*       */     //   11213: ldc_w 'Bash'
/*       */     //   11216: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   11219: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11224: pop
/*       */     //   11225: aload #4
/*       */     //   11227: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   11230: dup
/*       */     //   11231: bipush #83
/*       */     //   11233: ldc_w 'Destroy'
/*       */     //   11236: ldc_w 'Destroying'
/*       */     //   11239: iconst_3
/*       */     //   11240: newarray int
/*       */     //   11242: dup
/*       */     //   11243: iconst_0
/*       */     //   11244: iconst_5
/*       */     //   11245: iastore
/*       */     //   11246: dup
/*       */     //   11247: iconst_1
/*       */     //   11248: iconst_4
/*       */     //   11249: iastore
/*       */     //   11250: dup
/*       */     //   11251: iconst_2
/*       */     //   11252: bipush #43
/*       */     //   11254: iastore
/*       */     //   11255: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   11258: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11263: pop
/*       */     //   11264: iload #6
/*       */     //   11266: sipush #176
/*       */     //   11269: if_icmpne -> 12045
/*       */     //   11272: aload_1
/*       */     //   11273: invokestatic mayUseDeityWand : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   11276: ifeq -> 12045
/*       */     //   11279: invokestatic isThisATestServer : ()Z
/*       */     //   11282: ifeq -> 11290
/*       */     //   11285: bipush #-8
/*       */     //   11287: goto -> 11292
/*       */     //   11290: bipush #-7
/*       */     //   11292: istore #11
/*       */     //   11294: aload_1
/*       */     //   11295: invokevirtual getPower : ()I
/*       */     //   11298: iconst_4
/*       */     //   11299: if_icmplt -> 11312
/*       */     //   11302: aload_3
/*       */     //   11303: invokestatic mayBeEnchanted : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   11306: ifeq -> 11312
/*       */     //   11309: iinc #11, -1
/*       */     //   11312: iinc #11, -1
/*       */     //   11315: getstatic com/wurmonline/server/Servers.localServer : Lcom/wurmonline/server/ServerEntry;
/*       */     //   11318: getfield testServer : Z
/*       */     //   11321: ifeq -> 11335
/*       */     //   11324: iload #5
/*       */     //   11326: sipush #1437
/*       */     //   11329: if_icmpne -> 11335
/*       */     //   11332: iinc #11, -1
/*       */     //   11335: aload #4
/*       */     //   11337: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   11340: dup
/*       */     //   11341: iload #11
/*       */     //   11343: i2s
/*       */     //   11344: ldc_w 'Item'
/*       */     //   11347: ldc_w 'Item stuff'
/*       */     //   11350: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   11353: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11358: pop
/*       */     //   11359: aload #4
/*       */     //   11361: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11364: sipush #503
/*       */     //   11367: aaload
/*       */     //   11368: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11373: pop
/*       */     //   11374: invokestatic isThisATestServer : ()Z
/*       */     //   11377: ifeq -> 11395
/*       */     //   11380: aload #4
/*       */     //   11382: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11385: sipush #581
/*       */     //   11388: aaload
/*       */     //   11389: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11394: pop
/*       */     //   11395: aload #4
/*       */     //   11397: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11400: sipush #180
/*       */     //   11403: aaload
/*       */     //   11404: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11409: pop
/*       */     //   11410: aload_1
/*       */     //   11411: invokevirtual getPower : ()I
/*       */     //   11414: iconst_4
/*       */     //   11415: if_icmplt -> 11440
/*       */     //   11418: aload_3
/*       */     //   11419: invokestatic mayBeEnchanted : (Lcom/wurmonline/server/items/Item;)Z
/*       */     //   11422: ifeq -> 11440
/*       */     //   11425: aload #4
/*       */     //   11427: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11430: sipush #539
/*       */     //   11433: aaload
/*       */     //   11434: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11439: pop
/*       */     //   11440: aload #4
/*       */     //   11442: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11445: sipush #185
/*       */     //   11448: aaload
/*       */     //   11449: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11454: pop
/*       */     //   11455: aload #4
/*       */     //   11457: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11460: sipush #684
/*       */     //   11463: aaload
/*       */     //   11464: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11469: pop
/*       */     //   11470: aload #4
/*       */     //   11472: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   11475: dup
/*       */     //   11476: bipush #91
/*       */     //   11478: ldc_w 'Refresh'
/*       */     //   11481: ldc_w 'refreshing'
/*       */     //   11484: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   11487: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11492: pop
/*       */     //   11493: aload #4
/*       */     //   11495: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11498: bipush #88
/*       */     //   11500: aaload
/*       */     //   11501: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11506: pop
/*       */     //   11507: aload #4
/*       */     //   11509: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11512: sipush #179
/*       */     //   11515: aaload
/*       */     //   11516: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11521: pop
/*       */     //   11522: aload #4
/*       */     //   11524: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11527: sipush #674
/*       */     //   11530: aaload
/*       */     //   11531: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11536: pop
/*       */     //   11537: getstatic com/wurmonline/server/Servers.localServer : Lcom/wurmonline/server/ServerEntry;
/*       */     //   11540: getfield testServer : Z
/*       */     //   11543: ifeq -> 11569
/*       */     //   11546: iload #5
/*       */     //   11548: sipush #1437
/*       */     //   11551: if_icmpne -> 11569
/*       */     //   11554: aload #4
/*       */     //   11556: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11559: sipush #486
/*       */     //   11562: aaload
/*       */     //   11563: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11568: pop
/*       */     //   11569: aload_1
/*       */     //   11570: invokevirtual getPower : ()I
/*       */     //   11573: iconst_3
/*       */     //   11574: if_icmplt -> 11793
/*       */     //   11577: aload #4
/*       */     //   11579: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11582: sipush #135
/*       */     //   11585: aaload
/*       */     //   11586: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11591: pop
/*       */     //   11592: bipush #-7
/*       */     //   11594: istore #12
/*       */     //   11596: invokestatic isThisLoginServer : ()Z
/*       */     //   11599: ifeq -> 11609
/*       */     //   11602: iload #12
/*       */     //   11604: iconst_1
/*       */     //   11605: isub
/*       */     //   11606: i2s
/*       */     //   11607: istore #12
/*       */     //   11609: invokestatic isThisLoginServer : ()Z
/*       */     //   11612: ifeq -> 11622
/*       */     //   11615: iload #12
/*       */     //   11617: iconst_1
/*       */     //   11618: isub
/*       */     //   11619: i2s
/*       */     //   11620: istore #12
/*       */     //   11622: aload #4
/*       */     //   11624: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   11627: dup
/*       */     //   11628: iload #12
/*       */     //   11630: ldc 'Server'
/*       */     //   11632: ldc 'Server stuff'
/*       */     //   11634: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   11637: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11642: pop
/*       */     //   11643: aload #4
/*       */     //   11645: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11648: sipush #184
/*       */     //   11651: aaload
/*       */     //   11652: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11657: pop
/*       */     //   11658: aload #4
/*       */     //   11660: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11663: sipush #195
/*       */     //   11666: aaload
/*       */     //   11667: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11672: pop
/*       */     //   11673: aload #4
/*       */     //   11675: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11678: sipush #194
/*       */     //   11681: aaload
/*       */     //   11682: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11687: pop
/*       */     //   11688: aload #4
/*       */     //   11690: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11693: sipush #212
/*       */     //   11696: aaload
/*       */     //   11697: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11702: pop
/*       */     //   11703: aload #4
/*       */     //   11705: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11708: sipush #244
/*       */     //   11711: aaload
/*       */     //   11712: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11717: pop
/*       */     //   11718: aload #4
/*       */     //   11720: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11723: sipush #481
/*       */     //   11726: aaload
/*       */     //   11727: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11732: pop
/*       */     //   11733: aload #4
/*       */     //   11735: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11738: sipush #503
/*       */     //   11741: aaload
/*       */     //   11742: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11747: pop
/*       */     //   11748: invokestatic isThisLoginServer : ()Z
/*       */     //   11751: ifeq -> 11769
/*       */     //   11754: aload #4
/*       */     //   11756: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11759: sipush #609
/*       */     //   11762: aaload
/*       */     //   11763: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11768: pop
/*       */     //   11769: invokestatic isThisLoginServer : ()Z
/*       */     //   11772: ifeq -> 11790
/*       */     //   11775: aload #4
/*       */     //   11777: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11780: sipush #635
/*       */     //   11783: aaload
/*       */     //   11784: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11789: pop
/*       */     //   11790: goto -> 11815
/*       */     //   11793: aload_1
/*       */     //   11794: invokestatic maySetFaith : (Lcom/wurmonline/server/creatures/Creature;)Z
/*       */     //   11797: ifeq -> 11815
/*       */     //   11800: aload #4
/*       */     //   11802: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11805: sipush #212
/*       */     //   11808: aaload
/*       */     //   11809: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11814: pop
/*       */     //   11815: bipush #-3
/*       */     //   11817: istore #12
/*       */     //   11819: aload_1
/*       */     //   11820: invokevirtual getPower : ()I
/*       */     //   11823: iconst_3
/*       */     //   11824: if_icmplt -> 11831
/*       */     //   11827: bipush #-4
/*       */     //   11829: istore #12
/*       */     //   11831: aload_1
/*       */     //   11832: invokevirtual getPower : ()I
/*       */     //   11835: iconst_4
/*       */     //   11836: if_icmplt -> 11846
/*       */     //   11839: iload #12
/*       */     //   11841: iconst_1
/*       */     //   11842: isub
/*       */     //   11843: i2s
/*       */     //   11844: istore #12
/*       */     //   11846: aload #4
/*       */     //   11848: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   11851: dup
/*       */     //   11852: iload #12
/*       */     //   11854: ldc_w 'Creatures'
/*       */     //   11857: ldc_w 'Creatures stuff'
/*       */     //   11860: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   11863: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11868: pop
/*       */     //   11869: aload #4
/*       */     //   11871: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11874: bipush #89
/*       */     //   11876: aaload
/*       */     //   11877: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11882: pop
/*       */     //   11883: aload #4
/*       */     //   11885: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11888: sipush #467
/*       */     //   11891: aaload
/*       */     //   11892: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11897: pop
/*       */     //   11898: aload #4
/*       */     //   11900: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11903: sipush #719
/*       */     //   11906: aaload
/*       */     //   11907: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11912: pop
/*       */     //   11913: aload_1
/*       */     //   11914: invokevirtual getPower : ()I
/*       */     //   11917: iconst_4
/*       */     //   11918: if_icmplt -> 11936
/*       */     //   11921: aload #4
/*       */     //   11923: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11926: sipush #535
/*       */     //   11929: aaload
/*       */     //   11930: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11935: pop
/*       */     //   11936: aload_1
/*       */     //   11937: invokevirtual getPower : ()I
/*       */     //   11940: iconst_3
/*       */     //   11941: if_icmplt -> 11958
/*       */     //   11944: aload #4
/*       */     //   11946: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11949: bipush #92
/*       */     //   11951: aaload
/*       */     //   11952: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11957: pop
/*       */     //   11958: aload_2
/*       */     //   11959: invokevirtual getData1 : ()I
/*       */     //   11962: istore #13
/*       */     //   11964: aload_2
/*       */     //   11965: invokevirtual getData2 : ()I
/*       */     //   11968: istore #14
/*       */     //   11970: iload #13
/*       */     //   11972: iconst_m1
/*       */     //   11973: if_icmpeq -> 11996
/*       */     //   11976: iload #14
/*       */     //   11978: iconst_m1
/*       */     //   11979: if_icmpeq -> 11996
/*       */     //   11982: aload #4
/*       */     //   11984: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   11987: bipush #95
/*       */     //   11989: aaload
/*       */     //   11990: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   11995: pop
/*       */     //   11996: aload #4
/*       */     //   11998: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12001: bipush #94
/*       */     //   12003: aaload
/*       */     //   12004: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12009: pop
/*       */     //   12010: iload #5
/*       */     //   12012: sipush #176
/*       */     //   12015: if_icmpne -> 12042
/*       */     //   12018: aload #4
/*       */     //   12020: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   12023: dup
/*       */     //   12024: sipush #598
/*       */     //   12027: ldc_w 'Manage Recruitment Ads'
/*       */     //   12030: ldc_w 'Manage Recruitment Ads'
/*       */     //   12033: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   12036: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12041: pop
/*       */     //   12042: goto -> 12600
/*       */     //   12045: iload #6
/*       */     //   12047: sipush #315
/*       */     //   12050: if_icmpne -> 12396
/*       */     //   12053: aload_1
/*       */     //   12054: invokevirtual getPower : ()I
/*       */     //   12057: ifle -> 12396
/*       */     //   12060: iload #5
/*       */     //   12062: sipush #315
/*       */     //   12065: if_icmpeq -> 12076
/*       */     //   12068: iload #5
/*       */     //   12070: sipush #176
/*       */     //   12073: if_icmpne -> 12084
/*       */     //   12076: aload_1
/*       */     //   12077: invokevirtual getPower : ()I
/*       */     //   12080: iconst_5
/*       */     //   12081: if_icmplt -> 12209
/*       */     //   12084: bipush #-6
/*       */     //   12086: istore #11
/*       */     //   12088: aload #4
/*       */     //   12090: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   12093: dup
/*       */     //   12094: bipush #-6
/*       */     //   12096: ldc_w 'Item'
/*       */     //   12099: ldc_w 'Item stuff'
/*       */     //   12102: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   12105: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12110: pop
/*       */     //   12111: aload #4
/*       */     //   12113: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12116: sipush #180
/*       */     //   12119: aaload
/*       */     //   12120: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12125: pop
/*       */     //   12126: aload #4
/*       */     //   12128: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12131: sipush #185
/*       */     //   12134: aaload
/*       */     //   12135: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12140: pop
/*       */     //   12141: aload #4
/*       */     //   12143: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12146: sipush #684
/*       */     //   12149: aaload
/*       */     //   12150: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12155: pop
/*       */     //   12156: aload #4
/*       */     //   12158: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   12161: dup
/*       */     //   12162: bipush #91
/*       */     //   12164: ldc_w 'Refresh'
/*       */     //   12167: ldc_w 'refreshing'
/*       */     //   12170: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   12173: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12178: pop
/*       */     //   12179: aload #4
/*       */     //   12181: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12184: sipush #179
/*       */     //   12187: aaload
/*       */     //   12188: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12193: pop
/*       */     //   12194: aload #4
/*       */     //   12196: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12199: sipush #674
/*       */     //   12202: aaload
/*       */     //   12203: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12208: pop
/*       */     //   12209: aload_1
/*       */     //   12210: invokevirtual getPower : ()I
/*       */     //   12213: iconst_2
/*       */     //   12214: if_icmplt -> 12341
/*       */     //   12217: aload #4
/*       */     //   12219: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12222: sipush #244
/*       */     //   12225: aaload
/*       */     //   12226: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12231: pop
/*       */     //   12232: aload #4
/*       */     //   12234: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12237: sipush #503
/*       */     //   12240: aaload
/*       */     //   12241: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12246: pop
/*       */     //   12247: bipush #-2
/*       */     //   12249: istore #11
/*       */     //   12251: aload_1
/*       */     //   12252: invokevirtual getPower : ()I
/*       */     //   12255: iconst_4
/*       */     //   12256: if_icmplt -> 12266
/*       */     //   12259: iload #11
/*       */     //   12261: iconst_1
/*       */     //   12262: isub
/*       */     //   12263: i2s
/*       */     //   12264: istore #11
/*       */     //   12266: aload #4
/*       */     //   12268: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   12271: dup
/*       */     //   12272: iload #11
/*       */     //   12274: ldc_w 'Creatures'
/*       */     //   12277: ldc_w 'Creatures stuff'
/*       */     //   12280: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   12283: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12288: pop
/*       */     //   12289: aload #4
/*       */     //   12291: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12294: bipush #89
/*       */     //   12296: aaload
/*       */     //   12297: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12302: pop
/*       */     //   12303: aload #4
/*       */     //   12305: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12308: sipush #467
/*       */     //   12311: aaload
/*       */     //   12312: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12317: pop
/*       */     //   12318: aload_1
/*       */     //   12319: invokevirtual getPower : ()I
/*       */     //   12322: iconst_4
/*       */     //   12323: if_icmplt -> 12341
/*       */     //   12326: aload #4
/*       */     //   12328: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12331: sipush #535
/*       */     //   12334: aaload
/*       */     //   12335: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12340: pop
/*       */     //   12341: aload_2
/*       */     //   12342: invokevirtual getData1 : ()I
/*       */     //   12345: istore #11
/*       */     //   12347: aload_2
/*       */     //   12348: invokevirtual getData2 : ()I
/*       */     //   12351: istore #12
/*       */     //   12353: iload #11
/*       */     //   12355: iconst_m1
/*       */     //   12356: if_icmpeq -> 12379
/*       */     //   12359: iload #12
/*       */     //   12361: iconst_m1
/*       */     //   12362: if_icmpeq -> 12379
/*       */     //   12365: aload #4
/*       */     //   12367: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12370: bipush #95
/*       */     //   12372: aaload
/*       */     //   12373: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12378: pop
/*       */     //   12379: aload #4
/*       */     //   12381: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12384: bipush #94
/*       */     //   12386: aaload
/*       */     //   12387: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12392: pop
/*       */     //   12393: goto -> 12600
/*       */     //   12396: iload #6
/*       */     //   12398: sipush #1027
/*       */     //   12401: if_icmpne -> 12561
/*       */     //   12404: iload #5
/*       */     //   12406: sipush #1027
/*       */     //   12409: if_icmpne -> 12561
/*       */     //   12412: aload_1
/*       */     //   12413: invokevirtual getPower : ()I
/*       */     //   12416: iconst_1
/*       */     //   12417: if_icmplt -> 12561
/*       */     //   12420: aload #4
/*       */     //   12422: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   12425: dup
/*       */     //   12426: bipush #-3
/*       */     //   12428: ldc_w 'LCM'
/*       */     //   12431: ldc_w 'checking'
/*       */     //   12434: getstatic com/wurmonline/server/behaviours/ItemBehaviour.emptyIntArr : [I
/*       */     //   12437: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;[I)V
/*       */     //   12440: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12445: pop
/*       */     //   12446: aload #4
/*       */     //   12448: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12451: sipush #698
/*       */     //   12454: aaload
/*       */     //   12455: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12460: pop
/*       */     //   12461: aload #4
/*       */     //   12463: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12466: sipush #699
/*       */     //   12469: aaload
/*       */     //   12470: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12475: pop
/*       */     //   12476: aload #4
/*       */     //   12478: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12481: sipush #700
/*       */     //   12484: aaload
/*       */     //   12485: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12490: pop
/*       */     //   12491: aload #4
/*       */     //   12493: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12496: sipush #244
/*       */     //   12499: aaload
/*       */     //   12500: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12505: pop
/*       */     //   12506: aload #4
/*       */     //   12508: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12511: bipush #94
/*       */     //   12513: aaload
/*       */     //   12514: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12519: pop
/*       */     //   12520: aload_2
/*       */     //   12521: invokevirtual getData1 : ()I
/*       */     //   12524: istore #11
/*       */     //   12526: aload_2
/*       */     //   12527: invokevirtual getData2 : ()I
/*       */     //   12530: istore #12
/*       */     //   12532: iload #11
/*       */     //   12534: iconst_m1
/*       */     //   12535: if_icmpeq -> 12558
/*       */     //   12538: iload #12
/*       */     //   12540: iconst_m1
/*       */     //   12541: if_icmpeq -> 12558
/*       */     //   12544: aload #4
/*       */     //   12546: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12549: bipush #95
/*       */     //   12551: aaload
/*       */     //   12552: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12557: pop
/*       */     //   12558: goto -> 12600
/*       */     //   12561: iload #6
/*       */     //   12563: sipush #174
/*       */     //   12566: if_icmpne -> 12600
/*       */     //   12569: iload #5
/*       */     //   12571: sipush #174
/*       */     //   12574: if_icmpne -> 12600
/*       */     //   12577: aload_1
/*       */     //   12578: invokevirtual getPower : ()I
/*       */     //   12581: iconst_1
/*       */     //   12582: if_icmplt -> 12600
/*       */     //   12585: aload #4
/*       */     //   12587: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12590: sipush #244
/*       */     //   12593: aaload
/*       */     //   12594: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12599: pop
/*       */     //   12600: aload_3
/*       */     //   12601: invokevirtual isFoodMaker : ()Z
/*       */     //   12604: ifne -> 12617
/*       */     //   12607: aload_3
/*       */     //   12608: invokevirtual getTemplate : ()Lcom/wurmonline/server/items/ItemTemplate;
/*       */     //   12611: invokevirtual isCooker : ()Z
/*       */     //   12614: ifeq -> 12676
/*       */     //   12617: aload_3
/*       */     //   12618: invokevirtual isSealedByPlayer : ()Z
/*       */     //   12621: ifne -> 12676
/*       */     //   12624: aload #4
/*       */     //   12626: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12629: sipush #285
/*       */     //   12632: aaload
/*       */     //   12633: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12638: pop
/*       */     //   12639: goto -> 12676
/*       */     //   12642: aload #4
/*       */     //   12644: new com/wurmonline/server/behaviours/ActionEntry
/*       */     //   12647: dup
/*       */     //   12648: iconst_m1
/*       */     //   12649: ldc 'Prices'
/*       */     //   12651: ldc 'Prices'
/*       */     //   12653: invokespecial <init> : (SLjava/lang/String;Ljava/lang/String;)V
/*       */     //   12656: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12661: pop
/*       */     //   12662: aload #4
/*       */     //   12664: getstatic com/wurmonline/server/behaviours/Actions.actionEntrys : [Lcom/wurmonline/server/behaviours/ActionEntry;
/*       */     //   12667: bipush #87
/*       */     //   12669: aaload
/*       */     //   12670: invokeinterface add : (Ljava/lang/Object;)Z
/*       */     //   12675: pop
/*       */     //   12676: aload #4
/*       */     //   12678: invokestatic addEmotes : (Ljava/util/List;)V
/*       */     //   12681: aload #4
/*       */     //   12683: areturn
/*       */     // Line number table:
/*       */     //   Java source line number -> byte code offset
/*       */     //   #1245	-> 0
/*       */     //   #1246	-> 9
/*       */     //   #1247	-> 15
/*       */     //   #1248	-> 21
/*       */     //   #1251	-> 24
/*       */     //   #1256	-> 39
/*       */     //   #1253	-> 42
/*       */     //   #1257	-> 44
/*       */     //   #1258	-> 49
/*       */     //   #1261	-> 56
/*       */     //   #1263	-> 63
/*       */     //   #1264	-> 69
/*       */     //   #1266	-> 78
/*       */     //   #1268	-> 99
/*       */     //   #1269	-> 107
/*       */     //   #1271	-> 112
/*       */     //   #1272	-> 117
/*       */     //   #1274	-> 126
/*       */     //   #1275	-> 131
/*       */     //   #1277	-> 148
/*       */     //   #1279	-> 166
/*       */     //   #1280	-> 176
/*       */     //   #1281	-> 193
/*       */     //   #1282	-> 198
/*       */     //   #1284	-> 215
/*       */     //   #1285	-> 234
/*       */     //   #1287	-> 240
/*       */     //   #1288	-> 248
/*       */     //   #1290	-> 258
/*       */     //   #1291	-> 272
/*       */     //   #1292	-> 298
/*       */     //   #1294	-> 316
/*       */     //   #1295	-> 321
/*       */     //   #1297	-> 335
/*       */     //   #1298	-> 360
/*       */     //   #1301	-> 402
/*       */     //   #1302	-> 422
/*       */     //   #1304	-> 431
/*       */     //   #1305	-> 446
/*       */     //   #1308	-> 464
/*       */     //   #1310	-> 481
/*       */     //   #1314	-> 491
/*       */     //   #1315	-> 498
/*       */     //   #1317	-> 517
/*       */     //   #1318	-> 533
/*       */     //   #1320	-> 541
/*       */     //   #1322	-> 556
/*       */     //   #1323	-> 559
/*       */     //   #1325	-> 573
/*       */     //   #1327	-> 578
/*       */     //   #1328	-> 593
/*       */     //   #1332	-> 640
/*       */     //   #1335	-> 655
/*       */     //   #1337	-> 665
/*       */     //   #1338	-> 671
/*       */     //   #1340	-> 676
/*       */     //   #1342	-> 687
/*       */     //   #1343	-> 698
/*       */     //   #1345	-> 714
/*       */     //   #1347	-> 729
/*       */     //   #1350	-> 732
/*       */     //   #1351	-> 743
/*       */     //   #1353	-> 759
/*       */     //   #1354	-> 774
/*       */     //   #1356	-> 789
/*       */     //   #1360	-> 792
/*       */     //   #1363	-> 807
/*       */     //   #1365	-> 817
/*       */     //   #1366	-> 825
/*       */     //   #1367	-> 846
/*       */     //   #1369	-> 862
/*       */     //   #1370	-> 870
/*       */     //   #1371	-> 889
/*       */     //   #1378	-> 912
/*       */     //   #1379	-> 918
/*       */     //   #1383	-> 928
/*       */     //   #1384	-> 937
/*       */     //   #1386	-> 942
/*       */     //   #1387	-> 967
/*       */     //   #1389	-> 989
/*       */     //   #1390	-> 995
/*       */     //   #1392	-> 1004
/*       */     //   #1393	-> 1019
/*       */     //   #1398	-> 1033
/*       */     //   #1399	-> 1042
/*       */     //   #1401	-> 1060
/*       */     //   #1402	-> 1078
/*       */     //   #1404	-> 1084
/*       */     //   #1407	-> 1092
/*       */     //   #1408	-> 1115
/*       */     //   #1409	-> 1130
/*       */     //   #1417	-> 1148
/*       */     //   #1418	-> 1155
/*       */     //   #1420	-> 1172
/*       */     //   #1421	-> 1187
/*       */     //   #1423	-> 1195
/*       */     //   #1424	-> 1198
/*       */     //   #1425	-> 1205
/*       */     //   #1426	-> 1208
/*       */     //   #1427	-> 1237
/*       */     //   #1428	-> 1244
/*       */     //   #1429	-> 1267
/*       */     //   #1430	-> 1281
/*       */     //   #1432	-> 1284
/*       */     //   #1434	-> 1292
/*       */     //   #1436	-> 1307
/*       */     //   #1438	-> 1315
/*       */     //   #1446	-> 1325
/*       */     //   #1441	-> 1328
/*       */     //   #1443	-> 1330
/*       */     //   #1444	-> 1364
/*       */     //   #1445	-> 1379
/*       */     //   #1448	-> 1402
/*       */     //   #1452	-> 1405
/*       */     //   #1459	-> 1458
/*       */     //   #1460	-> 1473
/*       */     //   #1461	-> 1489
/*       */     //   #1462	-> 1498
/*       */     //   #1469	-> 1513
/*       */     //   #1466	-> 1516
/*       */     //   #1470	-> 1518
/*       */     //   #1472	-> 1526
/*       */     //   #1474	-> 1531
/*       */     //   #1475	-> 1540
/*       */     //   #1477	-> 1543
/*       */     //   #1479	-> 1558
/*       */     //   #1481	-> 1565
/*       */     //   #1484	-> 1580
/*       */     //   #1485	-> 1595
/*       */     //   #1486	-> 1605
/*       */     //   #1487	-> 1619
/*       */     //   #1488	-> 1643
/*       */     //   #1489	-> 1654
/*       */     //   #1490	-> 1664
/*       */     //   #1500	-> 1678
/*       */     //   #1502	-> 1700
/*       */     //   #1504	-> 1714
/*       */     //   #1509	-> 1722
/*       */     //   #1510	-> 1738
/*       */     //   #1511	-> 1748
/*       */     //   #1513	-> 1763
/*       */     //   #1515	-> 1785
/*       */     //   #1517	-> 1792
/*       */     //   #1519	-> 1799
/*       */     //   #1520	-> 1809
/*       */     //   #1523	-> 1845
/*       */     //   #1526	-> 1862
/*       */     //   #1528	-> 1872
/*       */     //   #1529	-> 1889
/*       */     //   #1530	-> 1896
/*       */     //   #1531	-> 1910
/*       */     //   #1533	-> 1927
/*       */     //   #1534	-> 1944
/*       */     //   #1535	-> 1952
/*       */     //   #1536	-> 1962
/*       */     //   #1538	-> 1985
/*       */     //   #1539	-> 2002
/*       */     //   #1542	-> 2016
/*       */     //   #1544	-> 2023
/*       */     //   #1545	-> 2039
/*       */     //   #1547	-> 2059
/*       */     //   #1550	-> 2074
/*       */     //   #1552	-> 2091
/*       */     //   #1554	-> 2099
/*       */     //   #1556	-> 2126
/*       */     //   #1558	-> 2134
/*       */     //   #1560	-> 2161
/*       */     //   #1562	-> 2206
/*       */     //   #1564	-> 2232
/*       */     //   #1565	-> 2240
/*       */     //   #1569	-> 2264
/*       */     //   #1571	-> 2286
/*       */     //   #1573	-> 2301
/*       */     //   #1575	-> 2315
/*       */     //   #1577	-> 2343
/*       */     //   #1579	-> 2364
/*       */     //   #1581	-> 2392
/*       */     //   #1582	-> 2409
/*       */     //   #1585	-> 2430
/*       */     //   #1587	-> 2458
/*       */     //   #1588	-> 2475
/*       */     //   #1590	-> 2488
/*       */     //   #1592	-> 2513
/*       */     //   #1594	-> 2537
/*       */     //   #1595	-> 2540
/*       */     //   #1597	-> 2568
/*       */     //   #1599	-> 2576
/*       */     //   #1600	-> 2580
/*       */     //   #1595	-> 2583
/*       */     //   #1604	-> 2589
/*       */     //   #1605	-> 2602
/*       */     //   #1606	-> 2617
/*       */     //   #1608	-> 2631
/*       */     //   #1610	-> 2641
/*       */     //   #1611	-> 2647
/*       */     //   #1612	-> 2674
/*       */     //   #1613	-> 2689
/*       */     //   #1615	-> 2704
/*       */     //   #1616	-> 2711
/*       */     //   #1617	-> 2765
/*       */     //   #1618	-> 2773
/*       */     //   #1620	-> 2827
/*       */     //   #1621	-> 2842
/*       */     //   #1635	-> 2893
/*       */     //   #1637	-> 2901
/*       */     //   #1639	-> 2910
/*       */     //   #1642	-> 2925
/*       */     //   #1644	-> 2933
/*       */     //   #1647	-> 2947
/*       */     //   #1649	-> 2969
/*       */     //   #1853	-> 2984
/*       */     //   #1652	-> 2987
/*       */     //   #1654	-> 2989
/*       */     //   #1655	-> 3008
/*       */     //   #1656	-> 3018
/*       */     //   #1657	-> 3036
/*       */     //   #1658	-> 3046
/*       */     //   #1659	-> 3064
/*       */     //   #1660	-> 3074
/*       */     //   #1661	-> 3092
/*       */     //   #1663	-> 3101
/*       */     //   #1664	-> 3109
/*       */     //   #1666	-> 3114
/*       */     //   #1669	-> 3129
/*       */     //   #1670	-> 3144
/*       */     //   #1671	-> 3159
/*       */     //   #1672	-> 3167
/*       */     //   #1673	-> 3182
/*       */     //   #1675	-> 3197
/*       */     //   #1677	-> 3204
/*       */     //   #1678	-> 3220
/*       */     //   #1680	-> 3234
/*       */     //   #1681	-> 3260
/*       */     //   #1680	-> 3297
/*       */     //   #1683	-> 3303
/*       */     //   #1684	-> 3327
/*       */     //   #1686	-> 3332
/*       */     //   #1687	-> 3349
/*       */     //   #1688	-> 3367
/*       */     //   #1690	-> 3375
/*       */     //   #1692	-> 3386
/*       */     //   #1693	-> 3395
/*       */     //   #1695	-> 3400
/*       */     //   #1696	-> 3408
/*       */     //   #1697	-> 3426
/*       */     //   #1698	-> 3434
/*       */     //   #1701	-> 3449
/*       */     //   #1703	-> 3457
/*       */     //   #1706	-> 3471
/*       */     //   #1708	-> 3476
/*       */     //   #1709	-> 3485
/*       */     //   #1711	-> 3488
/*       */     //   #1713	-> 3512
/*       */     //   #1714	-> 3515
/*       */     //   #1716	-> 3543
/*       */     //   #1718	-> 3551
/*       */     //   #1719	-> 3555
/*       */     //   #1714	-> 3558
/*       */     //   #1723	-> 3564
/*       */     //   #1724	-> 3577
/*       */     //   #1725	-> 3592
/*       */     //   #1727	-> 3606
/*       */     //   #1729	-> 3628
/*       */     //   #1731	-> 3643
/*       */     //   #1733	-> 3653
/*       */     //   #1734	-> 3659
/*       */     //   #1735	-> 3686
/*       */     //   #1736	-> 3701
/*       */     //   #1738	-> 3716
/*       */     //   #1739	-> 3723
/*       */     //   #1740	-> 3777
/*       */     //   #1741	-> 3785
/*       */     //   #1743	-> 3839
/*       */     //   #1745	-> 3854
/*       */     //   #1747	-> 3875
/*       */     //   #1751	-> 3926
/*       */     //   #1752	-> 3951
/*       */     //   #1755	-> 4029
/*       */     //   #1757	-> 4044
/*       */     //   #1759	-> 4053
/*       */     //   #1761	-> 4060
/*       */     //   #1764	-> 4070
/*       */     //   #1765	-> 4077
/*       */     //   #1767	-> 4094
/*       */     //   #1768	-> 4115
/*       */     //   #1770	-> 4132
/*       */     //   #1772	-> 4139
/*       */     //   #1773	-> 4149
/*       */     //   #1776	-> 4185
/*       */     //   #1779	-> 4202
/*       */     //   #1781	-> 4216
/*       */     //   #1782	-> 4225
/*       */     //   #1785	-> 4240
/*       */     //   #1787	-> 4248
/*       */     //   #1789	-> 4269
/*       */     //   #1791	-> 4272
/*       */     //   #1793	-> 4286
/*       */     //   #1794	-> 4311
/*       */     //   #1796	-> 4317
/*       */     //   #1797	-> 4334
/*       */     //   #1799	-> 4351
/*       */     //   #1801	-> 4358
/*       */     //   #1802	-> 4368
/*       */     //   #1805	-> 4404
/*       */     //   #1808	-> 4421
/*       */     //   #1810	-> 4428
/*       */     //   #1811	-> 4431
/*       */     //   #1812	-> 4440
/*       */     //   #1814	-> 4445
/*       */     //   #1815	-> 4452
/*       */     //   #1816	-> 4461
/*       */     //   #1817	-> 4468
/*       */     //   #1818	-> 4477
/*       */     //   #1820	-> 4484
/*       */     //   #1821	-> 4505
/*       */     //   #1822	-> 4520
/*       */     //   #1823	-> 4526
/*       */     //   #1824	-> 4541
/*       */     //   #1825	-> 4557
/*       */     //   #1826	-> 4572
/*       */     //   #1827	-> 4588
/*       */     //   #1828	-> 4603
/*       */     //   #1829	-> 4606
/*       */     //   #1831	-> 4630
/*       */     //   #1832	-> 4633
/*       */     //   #1834	-> 4661
/*       */     //   #1836	-> 4669
/*       */     //   #1837	-> 4673
/*       */     //   #1832	-> 4676
/*       */     //   #1841	-> 4682
/*       */     //   #1842	-> 4695
/*       */     //   #1843	-> 4710
/*       */     //   #1844	-> 4724
/*       */     //   #1845	-> 4727
/*       */     //   #1847	-> 4749
/*       */     //   #1849	-> 4767
/*       */     //   #1851	-> 4789
/*       */     //   #1854	-> 4804
/*       */     //   #1855	-> 4830
/*       */     //   #1856	-> 4845
/*       */     //   #1858	-> 4867
/*       */     //   #1859	-> 4874
/*       */     //   #1860	-> 4891
/*       */     //   #1861	-> 4897
/*       */     //   #1863	-> 4911
/*       */     //   #1865	-> 4918
/*       */     //   #1866	-> 4927
/*       */     //   #1867	-> 4942
/*       */     //   #1868	-> 4967
/*       */     //   #1869	-> 4985
/*       */     //   #1870	-> 5010
/*       */     //   #1871	-> 5025
/*       */     //   #1872	-> 5040
/*       */     //   #1873	-> 5055
/*       */     //   #1874	-> 5081
/*       */     //   #1875	-> 5107
/*       */     //   #1876	-> 5132
/*       */     //   #1877	-> 5147
/*       */     //   #1879	-> 5170
/*       */     //   #1881	-> 5180
/*       */     //   #1882	-> 5185
/*       */     //   #1883	-> 5213
/*       */     //   #1885	-> 5223
/*       */     //   #1887	-> 5226
/*       */     //   #1889	-> 5250
/*       */     //   #1890	-> 5254
/*       */     //   #1891	-> 5277
/*       */     //   #1892	-> 5292
/*       */     //   #1893	-> 5307
/*       */     //   #1896	-> 5322
/*       */     //   #1897	-> 5335
/*       */     //   #1898	-> 5348
/*       */     //   #1899	-> 5368
/*       */     //   #1902	-> 5381
/*       */     //   #1903	-> 5402
/*       */     //   #1905	-> 5421
/*       */     //   #1907	-> 5436
/*       */     //   #1909	-> 5446
/*       */     //   #1911	-> 5455
/*       */     //   #1912	-> 5461
/*       */     //   #1914	-> 5467
/*       */     //   #1918	-> 5491
/*       */     //   #1920	-> 5501
/*       */     //   #1921	-> 5508
/*       */     //   #1923	-> 5532
/*       */     //   #1927	-> 5543
/*       */     //   #1928	-> 5555
/*       */     //   #1929	-> 5560
/*       */     //   #1930	-> 5592
/*       */     //   #1931	-> 5613
/*       */     //   #1936	-> 5628
/*       */     //   #1933	-> 5631
/*       */     //   #1939	-> 5633
/*       */     //   #1941	-> 5642
/*       */     //   #1943	-> 5652
/*       */     //   #1944	-> 5658
/*       */     //   #1946	-> 5665
/*       */     //   #1952	-> 5689
/*       */     //   #1954	-> 5696
/*       */     //   #1956	-> 5699
/*       */     //   #1958	-> 5706
/*       */     //   #1960	-> 5716
/*       */     //   #1961	-> 5719
/*       */     //   #1962	-> 5734
/*       */     //   #1963	-> 5749
/*       */     //   #1965	-> 5773
/*       */     //   #1966	-> 5788
/*       */     //   #1968	-> 5795
/*       */     //   #1969	-> 5818
/*       */     //   #1973	-> 5845
/*       */     //   #1974	-> 5851
/*       */     //   #1976	-> 5866
/*       */     //   #1977	-> 5890
/*       */     //   #1978	-> 5914
/*       */     //   #1980	-> 5932
/*       */     //   #1982	-> 5939
/*       */     //   #1983	-> 5963
/*       */     //   #1984	-> 5987
/*       */     //   #1987	-> 6002
/*       */     //   #1988	-> 6043
/*       */     //   #1989	-> 6050
/*       */     //   #1990	-> 6066
/*       */     //   #1991	-> 6074
/*       */     //   #1992	-> 6089
/*       */     //   #1994	-> 6097
/*       */     //   #1996	-> 6112
/*       */     //   #1998	-> 6119
/*       */     //   #1999	-> 6140
/*       */     //   #2000	-> 6154
/*       */     //   #2004	-> 6171
/*       */     //   #2005	-> 6191
/*       */     //   #2007	-> 6205
/*       */     //   #2009	-> 6212
/*       */     //   #2010	-> 6219
/*       */     //   #2012	-> 6236
/*       */     //   #2013	-> 6244
/*       */     //   #2015	-> 6261
/*       */     //   #2017	-> 6298
/*       */     //   #2018	-> 6315
/*       */     //   #2020	-> 6323
/*       */     //   #2022	-> 6341
/*       */     //   #2024	-> 6365
/*       */     //   #2025	-> 6376
/*       */     //   #2027	-> 6393
/*       */     //   #2028	-> 6401
/*       */     //   #2029	-> 6415
/*       */     //   #2030	-> 6430
/*       */     //   #2031	-> 6450
/*       */     //   #2033	-> 6469
/*       */     //   #2035	-> 6476
/*       */     //   #2036	-> 6499
/*       */     //   #2037	-> 6514
/*       */     //   #2040	-> 6532
/*       */     //   #2042	-> 6539
/*       */     //   #2043	-> 6561
/*       */     //   #2045	-> 6579
/*       */     //   #2047	-> 6593
/*       */     //   #2049	-> 6610
/*       */     //   #2050	-> 6620
/*       */     //   #2052	-> 6641
/*       */     //   #2054	-> 6648
/*       */     //   #2067	-> 6666
/*       */     //   #2069	-> 6678
/*       */     //   #2070	-> 6684
/*       */     //   #2071	-> 6689
/*       */     //   #2072	-> 6692
/*       */     //   #2073	-> 6719
/*       */     //   #2072	-> 6757
/*       */     //   #2076	-> 6763
/*       */     //   #2077	-> 6782
/*       */     //   #2078	-> 6787
/*       */     //   #2080	-> 6792
/*       */     //   #2083	-> 6795
/*       */     //   #2084	-> 6810
/*       */     //   #2083	-> 6813
/*       */     //   #2085	-> 6818
/*       */     //   #2086	-> 6828
/*       */     //   #2085	-> 6834
/*       */     //   #2087	-> 6839
/*       */     //   #2089	-> 6844
/*       */     //   #2091	-> 6851
/*       */     //   #2092	-> 6857
/*       */     //   #2094	-> 6862
/*       */     //   #2096	-> 6872
/*       */     //   #2098	-> 6885
/*       */     //   #2104	-> 6891
/*       */     //   #2106	-> 6896
/*       */     //   #2108	-> 6904
/*       */     //   #2118	-> 6907
/*       */     //   #2114	-> 6910
/*       */     //   #2119	-> 6912
/*       */     //   #2121	-> 6917
/*       */     //   #2123	-> 6939
/*       */     //   #2125	-> 6946
/*       */     //   #2127	-> 6953
/*       */     //   #2128	-> 6962
/*       */     //   #2131	-> 6979
/*       */     //   #2132	-> 6993
/*       */     //   #2133	-> 7019
/*       */     //   #2138	-> 7034
/*       */     //   #2139	-> 7049
/*       */     //   #2140	-> 7074
/*       */     //   #2141	-> 7094
/*       */     //   #2144	-> 7108
/*       */     //   #2145	-> 7121
/*       */     //   #2146	-> 7141
/*       */     //   #2147	-> 7167
/*       */     //   #2149	-> 7174
/*       */     //   #2152	-> 7180
/*       */     //   #2153	-> 7186
/*       */     //   #2154	-> 7189
/*       */     //   #2156	-> 7215
/*       */     //   #2157	-> 7221
/*       */     //   #2154	-> 7224
/*       */     //   #2159	-> 7230
/*       */     //   #2161	-> 7235
/*       */     //   #2162	-> 7240
/*       */     //   #2163	-> 7253
/*       */     //   #2168	-> 7272
/*       */     //   #2175	-> 7288
/*       */     //   #2172	-> 7291
/*       */     //   #2174	-> 7293
/*       */     //   #2176	-> 7308
/*       */     //   #2177	-> 7311
/*       */     //   #2181	-> 7316
/*       */     //   #2182	-> 7322
/*       */     //   #2183	-> 7325
/*       */     //   #2185	-> 7351
/*       */     //   #2186	-> 7357
/*       */     //   #2183	-> 7360
/*       */     //   #2188	-> 7366
/*       */     //   #2189	-> 7371
/*       */     //   #2190	-> 7387
/*       */     //   #2192	-> 7408
/*       */     //   #2193	-> 7416
/*       */     //   #2196	-> 7432
/*       */     //   #2207	-> 7445
/*       */     //   #2198	-> 7448
/*       */     //   #2200	-> 7450
/*       */     //   #2202	-> 7471
/*       */     //   #2203	-> 7479
/*       */     //   #2206	-> 7495
/*       */     //   #2210	-> 7508
/*       */     //   #2212	-> 7515
/*       */     //   #2213	-> 7523
/*       */     //   #2215	-> 7541
/*       */     //   #2217	-> 7556
/*       */     //   #2222	-> 7588
/*       */     //   #2223	-> 7602
/*       */     //   #2225	-> 7607
/*       */     //   #2226	-> 7616
/*       */     //   #2228	-> 7619
/*       */     //   #2230	-> 7626
/*       */     //   #2231	-> 7629
/*       */     //   #2233	-> 7636
/*       */     //   #2234	-> 7642
/*       */     //   #2235	-> 7654
/*       */     //   #2236	-> 7657
/*       */     //   #2237	-> 7678
/*       */     //   #2239	-> 7681
/*       */     //   #2240	-> 7692
/*       */     //   #2241	-> 7695
/*       */     //   #2243	-> 7719
/*       */     //   #2244	-> 7726
/*       */     //   #2245	-> 7739
/*       */     //   #2246	-> 7753
/*       */     //   #2247	-> 7756
/*       */     //   #2248	-> 7764
/*       */     //   #2250	-> 7778
/*       */     //   #2251	-> 7786
/*       */     //   #2252	-> 7803
/*       */     //   #2253	-> 7818
/*       */     //   #2254	-> 7836
/*       */     //   #2255	-> 7844
/*       */     //   #2256	-> 7858
/*       */     //   #2258	-> 7865
/*       */     //   #2260	-> 7873
/*       */     //   #2261	-> 7897
/*       */     //   #2263	-> 7906
/*       */     //   #2265	-> 7921
/*       */     //   #2267	-> 7938
/*       */     //   #2269	-> 7952
/*       */     //   #2271	-> 7969
/*       */     //   #2278	-> 7984
/*       */     //   #2280	-> 7989
/*       */     //   #2282	-> 7996
/*       */     //   #2284	-> 8003
/*       */     //   #2285	-> 8017
/*       */     //   #2287	-> 8032
/*       */     //   #2289	-> 8039
/*       */     //   #2290	-> 8047
/*       */     //   #2293	-> 8065
/*       */     //   #2295	-> 8072
/*       */     //   #2297	-> 8079
/*       */     //   #2299	-> 8089
/*       */     //   #2303	-> 8116
/*       */     //   #2304	-> 8123
/*       */     //   #2306	-> 8150
/*       */     //   #2309	-> 8165
/*       */     //   #2311	-> 8172
/*       */     //   #2313	-> 8182
/*       */     //   #2317	-> 8209
/*       */     //   #2318	-> 8216
/*       */     //   #2320	-> 8263
/*       */     //   #2324	-> 8290
/*       */     //   #2326	-> 8298
/*       */     //   #2328	-> 8310
/*       */     //   #2329	-> 8325
/*       */     //   #2332	-> 8343
/*       */     //   #2334	-> 8359
/*       */     //   #2335	-> 8371
/*       */     //   #2337	-> 8389
/*       */     //   #2339	-> 8397
/*       */     //   #2341	-> 8415
/*       */     //   #2344	-> 8430
/*       */     //   #2346	-> 8452
/*       */     //   #2348	-> 8462
/*       */     //   #2349	-> 8470
/*       */     //   #2353	-> 8497
/*       */     //   #2354	-> 8505
/*       */     //   #2357	-> 8520
/*       */     //   #2359	-> 8542
/*       */     //   #2362	-> 8552
/*       */     //   #2363	-> 8559
/*       */     //   #2367	-> 8586
/*       */     //   #2368	-> 8593
/*       */     //   #2369	-> 8611
/*       */     //   #2370	-> 8626
/*       */     //   #2373	-> 8650
/*       */     //   #2375	-> 8658
/*       */     //   #2376	-> 8680
/*       */     //   #2378	-> 8695
/*       */     //   #2380	-> 8717
/*       */     //   #2381	-> 8745
/*       */     //   #2383	-> 8764
/*       */     //   #2385	-> 8774
/*       */     //   #2387	-> 8785
/*       */     //   #2392	-> 8800
/*       */     //   #2394	-> 8807
/*       */     //   #2396	-> 8824
/*       */     //   #2397	-> 8833
/*       */     //   #2400	-> 8877
/*       */     //   #2402	-> 8894
/*       */     //   #2404	-> 8902
/*       */     //   #2406	-> 8926
/*       */     //   #2407	-> 8934
/*       */     //   #2408	-> 8948
/*       */     //   #2410	-> 8976
/*       */     //   #2412	-> 8991
/*       */     //   #2413	-> 9000
/*       */     //   #2414	-> 9023
/*       */     //   #2415	-> 9038
/*       */     //   #2417	-> 9045
/*       */     //   #2420	-> 9069
/*       */     //   #2421	-> 9084
/*       */     //   #2422	-> 9094
/*       */     //   #2426	-> 9109
/*       */     //   #2428	-> 9116
/*       */     //   #2430	-> 9140
/*       */     //   #2431	-> 9155
/*       */     //   #2432	-> 9170
/*       */     //   #2435	-> 9185
/*       */     //   #2436	-> 9188
/*       */     //   #2437	-> 9193
/*       */     //   #2439	-> 9208
/*       */     //   #2441	-> 9241
/*       */     //   #2443	-> 9272
/*       */     //   #2444	-> 9286
/*       */     //   #2445	-> 9301
/*       */     //   #2447	-> 9304
/*       */     //   #2449	-> 9310
/*       */     //   #2451	-> 9317
/*       */     //   #2453	-> 9345
/*       */     //   #2454	-> 9359
/*       */     //   #2456	-> 9374
/*       */     //   #2458	-> 9388
/*       */     //   #2459	-> 9400
/*       */     //   #2460	-> 9407
/*       */     //   #2462	-> 9422
/*       */     //   #2464	-> 9430
/*       */     //   #2465	-> 9436
/*       */     //   #2468	-> 9447
/*       */     //   #2470	-> 9454
/*       */     //   #2471	-> 9465
/*       */     //   #2473	-> 9487
/*       */     //   #2474	-> 9490
/*       */     //   #2476	-> 9504
/*       */     //   #2478	-> 9518
/*       */     //   #2479	-> 9524
/*       */     //   #2480	-> 9535
/*       */     //   #2482	-> 9544
/*       */     //   #2483	-> 9547
/*       */     //   #2485	-> 9558
/*       */     //   #2488	-> 9561
/*       */     //   #2490	-> 9568
/*       */     //   #2492	-> 9575
/*       */     //   #2495	-> 9593
/*       */     //   #2497	-> 9600
/*       */     //   #2499	-> 9608
/*       */     //   #2500	-> 9618
/*       */     //   #2502	-> 9645
/*       */     //   #2505	-> 9660
/*       */     //   #2507	-> 9675
/*       */     //   #2510	-> 9690
/*       */     //   #2513	-> 9718
/*       */     //   #2514	-> 9724
/*       */     //   #2515	-> 9730
/*       */     //   #2516	-> 9736
/*       */     //   #2517	-> 9743
/*       */     //   #2518	-> 9754
/*       */     //   #2519	-> 9764
/*       */     //   #2520	-> 9781
/*       */     //   #2522	-> 9788
/*       */     //   #2524	-> 9795
/*       */     //   #2527	-> 9798
/*       */     //   #2531	-> 9803
/*       */     //   #2533	-> 9810
/*       */     //   #2535	-> 9826
/*       */     //   #2536	-> 9837
/*       */     //   #2542	-> 9870
/*       */     //   #2545	-> 9878
/*       */     //   #2550	-> 9911
/*       */     //   #2557	-> 9926
/*       */     //   #2558	-> 9934
/*       */     //   #2559	-> 9951
/*       */     //   #2561	-> 9966
/*       */     //   #2563	-> 9988
/*       */     //   #2564	-> 10010
/*       */     //   #2565	-> 10028
/*       */     //   #2566	-> 10037
/*       */     //   #2568	-> 10054
/*       */     //   #2573	-> 10093
/*       */     //   #2574	-> 10100
/*       */     //   #2575	-> 10115
/*       */     //   #2577	-> 10122
/*       */     //   #2579	-> 10143
/*       */     //   #2581	-> 10164
/*       */     //   #2584	-> 10175
/*       */     //   #2585	-> 10181
/*       */     //   #2586	-> 10193
/*       */     //   #2588	-> 10199
/*       */     //   #2589	-> 10225
/*       */     //   #2591	-> 10251
/*       */     //   #2589	-> 10268
/*       */     //   #2598	-> 10274
/*       */     //   #2599	-> 10299
/*       */     //   #2600	-> 10314
/*       */     //   #2601	-> 10334
/*       */     //   #2602	-> 10348
/*       */     //   #2604	-> 10362
/*       */     //   #2605	-> 10376
/*       */     //   #2606	-> 10382
/*       */     //   #2607	-> 10397
/*       */     //   #2608	-> 10411
/*       */     //   #2609	-> 10417
/*       */     //   #2610	-> 10432
/*       */     //   #2611	-> 10438
/*       */     //   #2610	-> 10441
/*       */     //   #2612	-> 10446
/*       */     //   #2613	-> 10452
/*       */     //   #2614	-> 10467
/*       */     //   #2615	-> 10473
/*       */     //   #2614	-> 10476
/*       */     //   #2616	-> 10481
/*       */     //   #2617	-> 10487
/*       */     //   #2618	-> 10502
/*       */     //   #2619	-> 10508
/*       */     //   #2618	-> 10511
/*       */     //   #2620	-> 10516
/*       */     //   #2621	-> 10522
/*       */     //   #2622	-> 10537
/*       */     //   #2623	-> 10543
/*       */     //   #2622	-> 10546
/*       */     //   #2624	-> 10551
/*       */     //   #2625	-> 10557
/*       */     //   #2626	-> 10572
/*       */     //   #2627	-> 10578
/*       */     //   #2626	-> 10581
/*       */     //   #2628	-> 10586
/*       */     //   #2629	-> 10592
/*       */     //   #2630	-> 10607
/*       */     //   #2631	-> 10613
/*       */     //   #2630	-> 10616
/*       */     //   #2632	-> 10621
/*       */     //   #2633	-> 10627
/*       */     //   #2634	-> 10642
/*       */     //   #2635	-> 10648
/*       */     //   #2634	-> 10651
/*       */     //   #2636	-> 10656
/*       */     //   #2637	-> 10662
/*       */     //   #2638	-> 10677
/*       */     //   #2639	-> 10680
/*       */     //   #2642	-> 10688
/*       */     //   #2643	-> 10712
/*       */     //   #2644	-> 10727
/*       */     //   #2645	-> 10742
/*       */     //   #2646	-> 10757
/*       */     //   #2647	-> 10772
/*       */     //   #2648	-> 10787
/*       */     //   #2649	-> 10802
/*       */     //   #2650	-> 10817
/*       */     //   #2651	-> 10832
/*       */     //   #2652	-> 10847
/*       */     //   #2653	-> 10862
/*       */     //   #2654	-> 10877
/*       */     //   #2655	-> 10892
/*       */     //   #2657	-> 10910
/*       */     //   #2661	-> 10961
/*       */     //   #2663	-> 10979
/*       */     //   #2666	-> 11018
/*       */     //   #2668	-> 11036
/*       */     //   #2670	-> 11044
/*       */     //   #2672	-> 11059
/*       */     //   #2673	-> 11062
/*       */     //   #2675	-> 11072
/*       */     //   #2677	-> 11093
/*       */     //   #2678	-> 11101
/*       */     //   #2679	-> 11104
/*       */     //   #2681	-> 11109
/*       */     //   #2685	-> 11115
/*       */     //   #2687	-> 11128
/*       */     //   #2689	-> 11141
/*       */     //   #2691	-> 11152
/*       */     //   #2692	-> 11162
/*       */     //   #2693	-> 11173
/*       */     //   #2699	-> 11176
/*       */     //   #2701	-> 11181
/*       */     //   #2703	-> 11203
/*       */     //   #2704	-> 11225
/*       */     //   #2713	-> 11264
/*       */     //   #2715	-> 11279
/*       */     //   #2717	-> 11294
/*       */     //   #2718	-> 11309
/*       */     //   #2719	-> 11312
/*       */     //   #2721	-> 11315
/*       */     //   #2722	-> 11332
/*       */     //   #2724	-> 11335
/*       */     //   #2725	-> 11359
/*       */     //   #2726	-> 11374
/*       */     //   #2727	-> 11380
/*       */     //   #2728	-> 11395
/*       */     //   #2729	-> 11410
/*       */     //   #2730	-> 11425
/*       */     //   #2731	-> 11440
/*       */     //   #2732	-> 11455
/*       */     //   #2733	-> 11470
/*       */     //   #2734	-> 11493
/*       */     //   #2735	-> 11507
/*       */     //   #2736	-> 11522
/*       */     //   #2737	-> 11537
/*       */     //   #2738	-> 11554
/*       */     //   #2740	-> 11569
/*       */     //   #2742	-> 11577
/*       */     //   #2743	-> 11592
/*       */     //   #2744	-> 11596
/*       */     //   #2745	-> 11602
/*       */     //   #2746	-> 11609
/*       */     //   #2747	-> 11615
/*       */     //   #2748	-> 11622
/*       */     //   #2749	-> 11643
/*       */     //   #2750	-> 11658
/*       */     //   #2751	-> 11673
/*       */     //   #2752	-> 11688
/*       */     //   #2753	-> 11703
/*       */     //   #2754	-> 11718
/*       */     //   #2755	-> 11733
/*       */     //   #2756	-> 11748
/*       */     //   #2757	-> 11754
/*       */     //   #2758	-> 11769
/*       */     //   #2759	-> 11775
/*       */     //   #2760	-> 11790
/*       */     //   #2761	-> 11793
/*       */     //   #2762	-> 11800
/*       */     //   #2763	-> 11815
/*       */     //   #2764	-> 11819
/*       */     //   #2765	-> 11827
/*       */     //   #2766	-> 11831
/*       */     //   #2767	-> 11839
/*       */     //   #2768	-> 11846
/*       */     //   #2769	-> 11869
/*       */     //   #2770	-> 11883
/*       */     //   #2771	-> 11898
/*       */     //   #2772	-> 11913
/*       */     //   #2773	-> 11921
/*       */     //   #2774	-> 11936
/*       */     //   #2775	-> 11944
/*       */     //   #2776	-> 11958
/*       */     //   #2777	-> 11964
/*       */     //   #2778	-> 11970
/*       */     //   #2779	-> 11982
/*       */     //   #2780	-> 11996
/*       */     //   #2781	-> 12010
/*       */     //   #2782	-> 12018
/*       */     //   #2784	-> 12042
/*       */     //   #2785	-> 12045
/*       */     //   #2787	-> 12060
/*       */     //   #2789	-> 12084
/*       */     //   #2790	-> 12088
/*       */     //   #2791	-> 12111
/*       */     //   #2792	-> 12126
/*       */     //   #2793	-> 12141
/*       */     //   #2794	-> 12156
/*       */     //   #2795	-> 12179
/*       */     //   #2796	-> 12194
/*       */     //   #2798	-> 12209
/*       */     //   #2800	-> 12217
/*       */     //   #2801	-> 12232
/*       */     //   #2802	-> 12247
/*       */     //   #2803	-> 12251
/*       */     //   #2804	-> 12259
/*       */     //   #2805	-> 12266
/*       */     //   #2806	-> 12289
/*       */     //   #2807	-> 12303
/*       */     //   #2808	-> 12318
/*       */     //   #2809	-> 12326
/*       */     //   #2811	-> 12341
/*       */     //   #2812	-> 12347
/*       */     //   #2813	-> 12353
/*       */     //   #2814	-> 12365
/*       */     //   #2815	-> 12379
/*       */     //   #2816	-> 12393
/*       */     //   #2817	-> 12396
/*       */     //   #2819	-> 12420
/*       */     //   #2820	-> 12446
/*       */     //   #2821	-> 12461
/*       */     //   #2822	-> 12476
/*       */     //   #2823	-> 12491
/*       */     //   #2824	-> 12506
/*       */     //   #2826	-> 12520
/*       */     //   #2827	-> 12526
/*       */     //   #2828	-> 12532
/*       */     //   #2829	-> 12544
/*       */     //   #2831	-> 12558
/*       */     //   #2832	-> 12561
/*       */     //   #2834	-> 12585
/*       */     //   #2836	-> 12600
/*       */     //   #2837	-> 12624
/*       */     //   #2841	-> 12642
/*       */     //   #2842	-> 12662
/*       */     //   #2846	-> 12676
/*       */     //   #2847	-> 12681
/*       */     // Local variable table:
/*       */     //   start	length	slot	name	descriptor
/*       */     //   117	285	13	isPvpServer	Z
/*       */     //   107	295	12	result	Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   514	3	12	placedTile	I
/*       */     //   533	23	12	placedTile	I
/*       */     //   698	31	13	cit	Lcom/wurmonline/server/villages/Citizen;
/*       */     //   743	46	13	cit	Lcom/wurmonline/server/villages/Citizen;
/*       */     //   671	136	12	village	Lcom/wurmonline/server/villages/Village;
/*       */     //   69	843	10	ownerId	J
/*       */     //   1198	83	14	sz	I
/*       */     //   1155	170	13	lock	Lcom/wurmonline/server/items/Item;
/*       */     //   1330	72	13	nse	Lcom/wurmonline/server/NoSuchItemException;
/*       */     //   995	407	11	lockId	J
/*       */     //   937	576	10	p	Lcom/wurmonline/server/items/Item;
/*       */     //   2568	15	14	item	Lcom/wurmonline/server/items/Item;
/*       */     //   2540	91	10	liquid	Lcom/wurmonline/server/items/Item;
/*       */     //   2647	246	10	soulDepth	Lcom/wurmonline/server/skills/Skill;
/*       */     //   2674	219	11	diff	D
/*       */     //   2689	204	13	chance	D
/*       */     //   3109	20	11	result	Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   3395	54	12	k	Lcom/wurmonline/server/kingdom/King;
/*       */     //   3543	15	16	item	Lcom/wurmonline/server/items/Item;
/*       */     //   3515	91	12	liquid	Lcom/wurmonline/server/items/Item;
/*       */     //   3659	267	12	soulDepth	Lcom/wurmonline/server/skills/Skill;
/*       */     //   3686	240	13	diff	D
/*       */     //   3701	225	15	chance	D
/*       */     //   3327	942	11	result	Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   4431	172	11	nums	S
/*       */     //   4440	163	12	c	Lcom/wurmonline/server/players/Cultist;
/*       */     //   4661	15	15	item	Lcom/wurmonline/server/items/Item;
/*       */     //   4633	91	11	liquid	Lcom/wurmonline/server/items/Item;
/*       */     //   2989	1815	10	noe	Lcom/wurmonline/server/items/NotOwnedException;
/*       */     //   4927	296	10	permissions	Ljava/util/List;
/*       */     //   5254	68	10	itint	S
/*       */     //   5461	30	10	folls	[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   5555	73	10	cagedCreature	Lcom/wurmonline/server/creatures/Creature;
/*       */     //   5658	31	10	folls	[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   5851	151	11	templateId	I
/*       */     //   6851	56	15	struct	Lcom/wurmonline/server/structures/Structure;
/*       */     //   6857	50	16	tile2	Lcom/wurmonline/server/zones/VolaTile;
/*       */     //   6818	89	13	tzone	Lcom/wurmonline/server/zones/Zone;
/*       */     //   6839	68	14	tile	Lcom/wurmonline/server/zones/VolaTile;
/*       */     //   7215	9	21	lWatcher	Lcom/wurmonline/server/creatures/Creature;
/*       */     //   7186	102	16	watchers	[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   7189	99	17	watching	Z
/*       */     //   7293	15	16	nsc	Lcom/wurmonline/server/creatures/NoSuchCreatureException;
/*       */     //   7180	128	14	lockId	J
/*       */     //   7351	9	19	lWatcher	Lcom/wurmonline/server/creatures/Creature;
/*       */     //   7322	123	14	watchers	[Lcom/wurmonline/server/creatures/Creature;
/*       */     //   7325	120	15	watching	Z
/*       */     //   7450	58	14	nsc	Lcom/wurmonline/server/creatures/NoSuchCreatureException;
/*       */     //   7167	341	13	isTop	Z
/*       */     //   7642	39	14	vehic	Lcom/wurmonline/server/behaviours/Vehicle;
/*       */     //   7726	27	14	havePermission	Z
/*       */     //   7629	149	13	ok	Z
/*       */     //   6787	1197	12	result	Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   6684	1300	11	top	Lcom/wurmonline/server/items/Item;
/*       */     //   9241	63	14	i	Lcom/wurmonline/server/items/Item;
/*       */     //   9436	51	13	material	B
/*       */     //   9447	40	14	tid	I
/*       */     //   9524	34	13	material	B
/*       */     //   9535	23	14	tid	I
/*       */     //   9724	242	13	skillNum	I
/*       */     //   9730	236	14	material	B
/*       */     //   9736	230	15	templateId	I
/*       */     //   10251	17	18	lSpell	Lcom/wurmonline/server/spells/Spell;
/*       */     //   10181	93	13	faith	F
/*       */     //   10193	81	14	spells	[Lcom/wurmonline/server/spells/Spell;
/*       */     //   10376	301	13	m2	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10411	266	14	m3	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10446	231	15	mr1	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10481	196	16	mr2	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10516	161	17	mr3	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10551	126	18	mr4	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10586	91	19	mr5	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10621	56	20	mr6	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   10656	21	21	mr7	[Lcom/wurmonline/server/tutorial/MissionTrigger;
/*       */     //   9188	1871	11	improvable	Z
/*       */     //   9193	1866	12	actString	Ljava/lang/String;
/*       */     //   11162	14	13	fence	Lcom/wurmonline/server/structures/Fence;
/*       */     //   11101	163	11	result	Lcom/wurmonline/server/structures/BlockingResult;
/*       */     //   11104	160	12	blocked	Z
/*       */     //   11596	194	12	nos	S
/*       */     //   11294	748	11	itint	I
/*       */     //   11819	223	12	nums	S
/*       */     //   11964	78	13	tx	I
/*       */     //   11970	72	14	ty	I
/*       */     //   12088	121	11	itint	S
/*       */     //   12251	90	11	nums	S
/*       */     //   12347	46	11	tx	I
/*       */     //   12353	40	12	ty	I
/*       */     //   12526	32	11	tx	I
/*       */     //   12532	26	12	ty	I
/*       */     //   0	12684	0	this	Lcom/wurmonline/server/behaviours/ItemBehaviour;
/*       */     //   0	12684	1	performer	Lcom/wurmonline/server/creatures/Creature;
/*       */     //   0	12684	2	source	Lcom/wurmonline/server/items/Item;
/*       */     //   0	12684	3	target	Lcom/wurmonline/server/items/Item;
/*       */     //   9	12675	4	toReturn	Ljava/util/List;
/*       */     //   15	12669	5	ttid	I
/*       */     //   21	12663	6	stid	I
/*       */     //   24	12660	7	added	Z
/*       */     //   49	12635	8	owner	J
/*       */     //   5699	6985	10	mayManipulate	Z
/*       */     // Local variable type table:
/*       */     //   start	length	slot	name	signature
/*       */     //   4927	296	10	permissions	Ljava/util/List<Lcom/wurmonline/server/behaviours/ActionEntry;>;
/*       */     //   9	12675	4	toReturn	Ljava/util/List<Lcom/wurmonline/server/behaviours/ActionEntry;>;
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   24	39	42	java/lang/Exception
/*       */     //   912	2984	2987	com/wurmonline/server/items/NotOwnedException
/*       */     //   928	1513	1516	com/wurmonline/server/NoSuchItemException
/*       */     //   1148	1325	1328	com/wurmonline/server/NoSuchItemException
/*       */     //   5543	5628	5631	com/wurmonline/server/creatures/NoSuchCreatureException
/*       */     //   6795	6907	6910	com/wurmonline/server/zones/NoSuchZoneException
/*       */     //   7180	7288	7291	com/wurmonline/server/creatures/NoSuchCreatureException
/*       */     //   7316	7445	7448	com/wurmonline/server/creatures/NoSuchCreatureException
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static String getMachineInfo(Item target) {
/*  2852 */     String toReturn = "";
/*  2853 */     boolean ready = true;
/*  2854 */     if (target.getData() > 0L) {
/*       */       
/*       */       try
/*       */       {
/*  2858 */         Item item = Items.getItem(target.getData());
/*       */ 
/*       */         
/*  2861 */         toReturn = "It is loaded with " + item.getNameWithGenus() + ".";
/*       */       }
/*  2863 */       catch (NoSuchItemException nsi)
/*       */       {
/*  2865 */         toReturn = "It is not loaded.";
/*  2866 */         ready = false;
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/*  2871 */       toReturn = "It is not loaded.";
/*  2872 */       ready = false;
/*       */     } 
/*  2874 */     if (target.getAuxData() > 0)
/*  2875 */       toReturn = "It is winched " + target.getAuxData() + " laps."; 
/*  2876 */     if (ready)
/*  2877 */       toReturn = "It is ready to fire."; 
/*  2878 */     return toReturn;
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
/*       */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  2890 */     int ttid = target.getTemplateId();
/*  2891 */     boolean toReturn = true;
/*  2892 */     Communicator comm = performer.getCommunicator();
/*  2893 */     if (target.canBePlanted() || target.getTemplateId() == 1309) {
/*       */       
/*  2895 */       if ((action == 177 || action == 178) && target.isTurnable(performer) && (
/*  2896 */         !target.isRoadMarker() || !target.isPlanted())) {
/*       */         
/*  2898 */         if (isSignManipulationOk(target, performer, (short)177)) {
/*  2899 */           return MethodsItems.moveItem(performer, target, counter, action, act);
/*       */         }
/*  2901 */         comm.sendNormalServerMessage("You may not turn that " + target.getName() + ".");
/*  2902 */         return true;
/*       */       } 
/*  2904 */       if (action == 176 && !target.isRoadMarker()) {
/*       */         
/*  2906 */         if (performer.getPower() > 0) {
/*  2907 */           return MethodsItems.plantSignFinish(performer, target, false, 0, 0, performer.isOnSurface(), performer.getBridgeId(), false, -1L);
/*       */         }
/*  2909 */         return MethodsItems.plantSign(performer, target, counter, false, 0, 0, performer.isOnSurface(), performer.getBridgeId(), false, -1L);
/*       */       } 
/*  2911 */       if (action == 598) {
/*       */         
/*  2913 */         comm.sendOpenWindowByTypeID((byte)0);
/*  2914 */         return true;
/*       */       } 
/*  2916 */       if (action == 601) {
/*       */         
/*  2918 */         comm.sendOpenWindowByTypeID((byte)1);
/*       */         
/*  2920 */         comm.sendViableVillageRecruitmentAds();
/*  2921 */         return true;
/*       */       } 
/*  2923 */       if (action == 602) {
/*       */         
/*  2925 */         RecruitmentAds.deleteVillageAd((Player)performer);
/*  2926 */         return true;
/*       */       } 
/*  2928 */       if (action == 603) {
/*       */         
/*  2930 */         Village village = Villages.getVillageForCreature(performer);
/*  2931 */         if (village == null) {
/*       */           
/*  2933 */           comm.sendNormalServerMessage("You are not a member of a village.");
/*  2934 */           return true;
/*       */         } 
/*  2936 */         RecruitmentAd ad = RecruitmentAds.getVillageAd(village.getId(), performer.getKingdomId());
/*  2937 */         if (ad == null) {
/*       */           
/*  2939 */           comm.sendNormalServerMessage("Your village does not have a recruitment ad that can be edited.");
/*       */           
/*  2941 */           return true;
/*       */         } 
/*  2943 */         comm.sendOpenManageRecruitWindowWithData(ad.getDescription());
/*  2944 */         return true;
/*       */       } 
/*  2946 */       if (Recipes.isRecipeAction(action)) {
/*       */         
/*  2948 */         Recipe recipe = Recipes.getRecipeByActionId(action);
/*  2949 */         if (recipe == null) {
/*       */           
/*  2951 */           performer.getCommunicator().sendNormalServerMessage("Recipe" + (
/*  2952 */               (performer.getPower() > 1) ? (" " + action) : "") + " not found, most odd!");
/*  2953 */           return true;
/*       */         } 
/*       */ 
/*       */         
/*  2957 */         return handleRecipe(act, performer, (Item)null, target, action, counter, recipe);
/*       */       } 
/*       */       
/*  2960 */       if (action == 17 && target.getTemplateId() == 1271) {
/*       */         
/*  2962 */         readVillageMessages(performer, target);
/*       */       } else {
/*  2964 */         if ((action == 6 || action == 100 || (action == 925 && target
/*  2965 */           .getOwnerId() != performer.getWurmId() && counter == 1.0F)) && !target.isNoTake(performer)) {
/*       */           
/*  2967 */           if (target.getTemplateId() == 1178 && target.getTemperature() > 399 && target.getParentId() == -10L && 
/*  2968 */             !target.isEmpty(true)) {
/*       */             
/*  2970 */             comm.sendNormalServerMessage("The " + target.getName() + " is too hot to handle.");
/*  2971 */             return true;
/*       */           } 
/*  2973 */           if (target.getTemplateId() == 1175 && target.hasQueen()) {
/*       */             
/*  2975 */             comm.sendNormalServerMessage("The " + target.getName() + " can not be taken when it has a queen in it, try loading it.");
/*  2976 */             return true;
/*       */           } 
/*  2978 */           if (target.isChair()) {
/*       */             
/*  2980 */             Vehicle chair = Vehicles.getVehicle(target);
/*  2981 */             if (chair.isAnySeatOccupied()) {
/*       */               
/*  2983 */               comm.sendNormalServerMessage("The " + Vehicle.getVehicleName(chair) + " is occupied and may not be taken.");
/*  2984 */               return true;
/*       */             } 
/*       */           } 
/*  2987 */           if ((target.isBulkContainer() || target.getTemplate().getInitialContainers() != null || target
/*  2988 */             .getTemplateId() == 1342) && 
/*  2989 */             !target.isEmpty(true)) {
/*       */             
/*  2991 */             comm.sendNormalServerMessage("The " + target.getName() + " needs to be empty to pick it up.");
/*  2992 */             return true;
/*       */           } 
/*  2994 */           if (target.getTemplateId() == 1175 && target.hasQueen() && !WurmCalendar.isSeasonWinter()) {
/*       */ 
/*       */             
/*  2997 */             performer.getCommunicator().sendSafeServerMessage("The bees get angry and defend the " + target
/*  2998 */                 .getName() + " by stinging you.");
/*  2999 */             performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (5000.0F + Server.rand
/*  3000 */                 .nextFloat() * 7000.0F), 0.0F, 30.0F, false, false);
/*  3001 */             return true;
/*       */           } 
/*  3003 */           if (target.isPlanted()) {
/*       */             
/*  3005 */             toReturn = true;
/*  3006 */             boolean ok = isSignManipulationOk(target, performer, (short)6);
/*       */             
/*  3008 */             if (ok) {
/*       */               
/*  3010 */               TakeResultEnum result = MethodsItems.take(act, performer, target);
/*  3011 */               if (result == TakeResultEnum.SUCCESS)
/*       */               {
/*  3013 */                 target.setIsPlanted(false);
/*  3014 */                 if (target.getTemplateId() == 1342) {
/*       */ 
/*       */                   
/*  3017 */                   performer.getCommunicator().sendRemoveFromInventory(target, -1L);
/*  3018 */                   performer.getCommunicator().sendAddToInventory(target, -1L, -1L, -1);
/*       */                 } 
/*  3020 */                 if (action == 925)
/*       */                 {
/*  3022 */                   return MethodsItems.placeItem(performer, target, act, counter);
/*       */                 }
/*       */ 
/*       */                 
/*  3026 */                 comm.sendNormalServerMessage("You get " + target
/*  3027 */                     .getNameWithGenus() + ".");
/*  3028 */                 Server.getInstance().broadCastAction(performer
/*  3029 */                     .getName() + " gets " + target.getNameWithGenus() + ".", performer, 
/*  3030 */                     Math.min(Math.max(3, target.getSizeZ() / 10), 10));
/*  3031 */                 PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.TAKEN_ITEM);
/*       */               }
/*       */               else
/*       */               {
/*  3035 */                 result.sendToPerformer(performer);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*       */             }
/*       */             else {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  3083 */               comm.sendNormalServerMessage("The " + target
/*  3084 */                   .getName() + " is firmly planted in the ground.");
/*       */             } 
/*       */           } else {
/*       */             
/*  3088 */             TakeResultEnum result = MethodsItems.take(act, performer, target);
/*  3089 */             if (result == TakeResultEnum.SUCCESS) {
/*       */               
/*  3091 */               target.setIsPlanted(false);
/*  3092 */               if (action == 925)
/*       */               {
/*  3094 */                 return MethodsItems.placeItem(performer, target, act, counter);
/*       */               }
/*       */ 
/*       */               
/*  3098 */               comm.sendNormalServerMessage("You get " + target
/*  3099 */                   .getNameWithGenus() + ".");
/*  3100 */               Server.getInstance().broadCastAction(performer
/*  3101 */                   .getName() + " gets " + target.getNameWithGenus() + ".", performer, 
/*  3102 */                   Math.min(Math.max(3, target.getSizeZ() / 10), 10));
/*  3103 */               PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.TAKEN_ITEM);
/*       */             }
/*       */             else {
/*       */               
/*  3107 */               result.sendToPerformer(performer);
/*  3108 */             }  return true;
/*       */           } 
/*  3110 */           return toReturn;
/*       */         } 
/*  3112 */         if (action == 1)
/*       */         {
/*  3114 */           return examine(act, performer, target, action, counter);
/*       */         }
/*       */       } 
/*       */     } 
/*  3118 */     if (target.isCrate() && target.isSealedByPlayer() && action == 1)
/*       */     {
/*  3120 */       return examine(act, performer, target, action, counter);
/*       */     }
/*  3122 */     if (action == 1) {
/*       */       
/*  3124 */       String descString = target.examine(performer);
/*       */       
/*  3126 */       if (target.isKingdomMarker() || target.getTemplateId() == 996) {
/*       */ 
/*       */         
/*       */         try {
/*  3130 */           String name = Players.getInstance().getNameFor(target.lastOwner);
/*  3131 */           descString = descString + " The name of the founder, " + name + ", has been carved into the stone above the door.";
/*       */         }
/*  3133 */         catch (IOException iox) {
/*       */           
/*  3135 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*       */         }
/*  3137 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */ 
/*       */ 
/*       */         
/*  3141 */         GuardTower tower = Kingdoms.getTower(target);
/*  3142 */         if (tower != null) {
/*       */           
/*  3144 */           descString = descString + " '" + tower.getName() + "' is engraved in a metal plaque on the door.";
/*  3145 */           if (performer.getPower() > 1)
/*       */           {
/*  3147 */             descString = descString + " There are " + tower.getGuardCount() + " guards out of a max of " + tower.getMaxGuards() + " guards associated with this tower.";
/*       */           }
/*       */         } 
/*       */         
/*  3151 */         Long last = conquers.get(Long.valueOf(target.getWurmId()));
/*  3152 */         if (last != null)
/*       */         {
/*  3154 */           if (System.currentTimeMillis() - last.longValue() < 3600000L)
/*       */           {
/*       */ 
/*       */             
/*  3158 */             descString = descString + " You will have to wait " + Server.getTimeFor(last.longValue() + 3600000L - System.currentTimeMillis()) + " if you want to receive battle rank for conquering the " + target.getName() + ".";
/*       */           }
/*       */         }
/*       */       }
/*  3162 */       else if (target.isWarTarget()) {
/*       */         
/*  3164 */         Long last = conquers.get(Long.valueOf(target.getWurmId()));
/*  3165 */         if (last != null)
/*       */         {
/*  3167 */           if (System.currentTimeMillis() - last.longValue() < 3600000L)
/*       */           {
/*       */ 
/*       */             
/*  3171 */             descString = descString + " You will have to wait " + Server.getTimeFor(last.longValue() + 3600000L - System.currentTimeMillis()) + " if you want to receive battle rank for conquering the " + target.getName() + ".";
/*       */           }
/*       */         }
/*       */       } 
/*  3175 */       if (target.isBed()) {
/*       */         
/*  3177 */         PlayerInfo info = PlayerInfoFactory.getPlayerSleepingInBed(target.getWurmId());
/*  3178 */         if (info != null)
/*       */         {
/*  3180 */           comm.sendNormalServerMessage("Some kind of mysterious haze lingers over the " + target
/*  3181 */               .getName() + ", and you notice that the " + target
/*  3182 */               .getName() + " is occupied by the spirit of " + info.getName() + ".");
/*       */         }
/*       */         
/*  3185 */         if (target.getData() > 0L) {
/*       */           
/*  3187 */           info = PlayerInfoFactory.getPlayerInfoWithWurmId(target.getData());
/*  3188 */           if (info != null)
/*       */           {
/*  3190 */             if (info.lastLogin > 0L || info.lastLogout < System.currentTimeMillis() - 86400000L)
/*       */             {
/*  3192 */               comm.sendNormalServerMessage(info
/*  3193 */                   .getName() + " has rented the " + target.getName() + ".");
/*       */             }
/*       */           }
/*       */         } 
/*       */       } 
/*  3198 */       if (target.isHitchTarget() || (target.isVehicle() && !Vehicles.getVehicle(target).isChair())) {
/*       */ 
/*       */         
/*       */         try {
/*  3202 */           String where = " in the stern.";
/*  3203 */           if (target.isTent())
/*  3204 */             where = " on a pole."; 
/*  3205 */           if (target.isFence())
/*  3206 */             where = " on the bottom."; 
/*  3207 */           String name = Players.getInstance().getNameFor(target.lastOwner);
/*  3208 */           descString = descString + " The name of the owner, " + name + ", has been etched" + where;
/*       */         }
/*  3210 */         catch (IOException iox) {
/*       */           
/*  3212 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*       */         }
/*  3214 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  3220 */         if (target.isLockable()) {
/*       */           
/*  3222 */           long lockId = target.getLockId();
/*  3223 */           if (lockId != -10L) {
/*       */             
/*       */             try {
/*       */               
/*  3227 */               Items.getItem(lockId);
/*       */             }
/*  3229 */             catch (NoSuchItemException e) {
/*       */               
/*  3231 */               logger.log(Level.WARNING, "No lock with id " + lockId + ", although the item has that.");
/*  3232 */               comm.sendNormalServerMessage("It looks like the lock has nearly rusted away, it should be replaced.");
/*       */             } 
/*       */           }
/*       */         } 
/*       */ 
/*       */         
/*  3238 */         if (performer.getPower() > 3) {
/*       */           
/*  3240 */           comm.sendNormalServerMessage("Windrot=" + 
/*  3241 */               Server.getWeather().getWindRotation() + " power=" + Server.getWeather().getWindPower() + ", impact=" + performer
/*  3242 */               .getMovementScheme().getWindImpact() + " speedmod=" + performer
/*  3243 */               .getMovementScheme().getSpeedMod());
/*  3244 */           comm.sendNormalServerMessage("Vrot=" + performer
/*       */               
/*  3246 */               .getMovementScheme().getVehicleRotation() + ", speed=" + performer
/*       */               
/*  3248 */               .getMovementScheme().getMountSpeed() + ", power=" + 
/*       */               
/*  3250 */               MovementScheme.getWindPower(Server.getWeather().getWindRotation() - 180.0F, performer
/*  3251 */                 .getMovementScheme().getVehicleRotation()));
/*       */         } 
/*  3253 */         Vehicle vehic = Vehicles.getVehicle(target);
/*  3254 */         if (performer.getPower() > 0)
/*       */         {
/*  3256 */           if (vehic != null) {
/*       */             
/*  3258 */             Set<Creature> draggers = vehic.draggers;
/*  3259 */             if (draggers != null) {
/*       */               
/*  3261 */               for (Creature c : draggers)
/*  3262 */                 comm.sendNormalServerMessage("Dragged  by " + c.getName()); 
/*  3263 */               Seat[] hitched = vehic.hitched;
/*  3264 */               for (Seat lElement : hitched) {
/*  3265 */                 comm.sendNormalServerMessage("Hitch seat " + lElement.id + " occupied by " + lElement
/*  3266 */                     .getOccupant());
/*       */               }
/*       */             } else {
/*  3269 */               comm.sendNormalServerMessage("No draggers registered.");
/*  3270 */             }  comm.sendNormalServerMessage("Pilot id=" + vehic.pilotId);
/*       */           } else {
/*       */             
/*  3273 */             comm.sendNormalServerMessage("Failed to locate vehicle data");
/*       */           } 
/*       */         }
/*  3276 */         if (vehic != null) {
/*       */           
/*  3278 */           String passengers = "";
/*  3279 */           for (Seat seat : vehic.getSeats()) {
/*       */             
/*  3281 */             if (seat.isOccupied()) {
/*       */               
/*  3283 */               Player occupant = null;
/*       */               try {
/*  3285 */                 occupant = Players.getInstance().getPlayer(seat.getOccupant());
/*  3286 */               } catch (NoSuchPlayerException e) {
/*  3287 */                 logger.log(Level.WARNING, "Occupant with ID: " + seat.getOccupant() + " was not found...");
/*       */               } 
/*       */ 
/*       */               
/*  3291 */               if (occupant == null || occupant.isVisibleTo(performer)) {
/*       */ 
/*       */                 
/*  3294 */                 String pName = PlayerInfoFactory.getPlayerName(seat.getOccupant());
/*  3295 */                 if (seat.type == 0) {
/*       */                   
/*  3297 */                   comm.sendNormalServerMessage("Commander: " + pName + ".");
/*       */                 }
/*  3299 */                 else if (seat.type == 1) {
/*       */                   
/*  3301 */                   if (!passengers.isEmpty())
/*  3302 */                     passengers = passengers + ", "; 
/*  3303 */                   passengers = passengers + pName;
/*       */                 } 
/*       */               } 
/*       */             } 
/*  3307 */           }  String pass = "Passenger" + ((passengers.length() == 1) ? "" : "s");
/*  3308 */           if (!passengers.isEmpty()) {
/*  3309 */             comm.sendNormalServerMessage(pass + ": " + passengers + ".");
/*       */           }
/*       */         } 
/*  3312 */         assert vehic != null;
/*  3313 */         if (performer.getVehicle() == vehic.getWurmid()) {
/*       */           
/*  3315 */           String whereTo = "The " + vehic.getName();
/*  3316 */           boolean isPvP = false;
/*  3317 */           if (vehic.hasDestinationSet()) {
/*       */             
/*  3319 */             whereTo = whereTo + " has a course plotted to " + vehic.getDestinationServer().getName();
/*  3320 */             if ((vehic.getDestinationServer()).PVPSERVER) {
/*       */               
/*  3322 */               whereTo = whereTo + ", which will take you in to hostile territory";
/*  3323 */               isPvP = true;
/*       */             } 
/*       */           } else {
/*       */             
/*  3327 */             whereTo = whereTo + " does not have a course plotted";
/*  3328 */           }  if (isPvP) {
/*  3329 */             comm.sendAlertServerMessage(whereTo + ".");
/*       */           } else {
/*  3331 */             comm.sendNormalServerMessage(whereTo + ".");
/*       */           } 
/*       */         } 
/*  3334 */       }  comm.sendNormalServerMessage(descString);
/*  3335 */       if (ttid == 1172) {
/*       */         
/*  3337 */         int vol = target.getVolume();
/*  3338 */         String vm = (vol >= 1000) ? ((vol / 1000) + "kg") : (vol + "g");
/*  3339 */         comm.sendNormalServerMessage("You check the wheel on the bottom and it indicates the volume is set to " + vm + ".");
/*       */       } 
/*       */       
/*  3342 */       if (target.isFish() && target.isNamed() && target.getCreatorName() != null && target.getCreatorName().length() > 0) {
/*       */         
/*  3344 */         comm.sendNormalServerMessage("Caught by " + target
/*  3345 */             .getCreatorName() + " on " + WurmCalendar.getDateFor(target.creationDate));
/*       */       }
/*       */       else {
/*       */         
/*  3349 */         String s = target.getSignature();
/*  3350 */         if (s != null && s.length() > 2 && !target.isDish()) {
/*       */           
/*  3352 */           comm.sendNormalServerMessage("You can barely make out the signature of its maker,  '" + s + "'.");
/*       */         
/*       */         }
/*  3355 */         else if (target.isNamed()) {
/*       */           
/*  3357 */           if (target.getOwnerId() == performer.getWurmId())
/*       */           {
/*  3359 */             if (target.getCurrentQualityLevel() >= 20.0F && !target.isLiquid() && !target.isDish() && target
/*  3360 */               .getTemplateId() != 1307) {
/*       */               
/*  3362 */               target.setCreator(performer.getName());
/*  3363 */               comm.sendNormalServerMessage("Since its creator tag has faded, you decide to keep history alive by scratching your name on it. Afterwards you proudly read '" + target
/*       */                   
/*  3365 */                   .getCreatorName() + "'.");
/*       */             } 
/*       */           }
/*       */         } 
/*       */       } 
/*  3370 */       target.sendEnchantmentStrings(comm);
/*  3371 */       target.sendExtraStrings(comm);
/*       */ 
/*       */       
/*  3374 */       if (target.isNoTake(performer)) {
/*  3375 */         MissionHelper.printHelpForMission(target.getWurmId(), target.getName(), performer);
/*       */       }
/*  3377 */     } else if (!target.isTraded()) {
/*       */       
/*  3379 */       if (action == 6 || action == 100 || (action == 925 && target
/*  3380 */         .getOwnerId() != performer.getWurmId() && counter == 1.0F)) {
/*       */         
/*  3382 */         if (target.isVehicle()) {
/*       */           
/*  3384 */           Vehicle vehicle = Vehicles.getVehicle(target);
/*  3385 */           for (Seat seat : vehicle.seats) {
/*       */             
/*  3387 */             if (seat.isOccupied()) {
/*       */               
/*  3389 */               comm.sendNormalServerMessage("You cannot take this item.");
/*  3390 */               return true;
/*       */             } 
/*       */           } 
/*       */         } 
/*  3394 */         if (target.getTemplateId() == 1178 && target.getTemperature() > 200) {
/*       */           
/*  3396 */           comm.sendNormalServerMessage("The " + target.getName() + " is too hot to handle.");
/*  3397 */           return true;
/*       */         } 
/*  3399 */         if (target.getTemplateId() == 1175 && target.hasQueen() && !WurmCalendar.isSeasonWinter()) {
/*       */ 
/*       */           
/*  3402 */           performer.getCommunicator().sendSafeServerMessage("The bees get angry and defend the " + target
/*  3403 */               .getName() + " by stinging you.");
/*  3404 */           performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (5000.0F + Server.rand
/*  3405 */               .nextFloat() * 7000.0F), 0.0F, 30.0F, false, false);
/*  3406 */           return true;
/*       */         } 
/*  3408 */         if (!target.isNoTake(performer) || performer.getPower() > 0)
/*       */         {
/*  3410 */           if (!target.isCrate()) {
/*       */             
/*  3412 */             Item topp = target.getTopParentOrNull();
/*       */             
/*  3414 */             TakeResultEnum result = MethodsItems.take(act, performer, target);
/*  3415 */             if (result == TakeResultEnum.SUCCESS) {
/*       */               
/*  3417 */               target.setIsPlanted(false);
/*  3418 */               if (action == 925)
/*       */               {
/*  3420 */                 return MethodsItems.placeItem(performer, target, act, counter);
/*       */               }
/*       */ 
/*       */               
/*  3424 */               if (topp != null && topp.isItemSpawn())
/*  3425 */                 performer.addChallengeScore(ChallengePointEnum.ChallengePoint.ITEMSLOOTED.getEnumtype(), 0.01F); 
/*  3426 */               comm.sendNormalServerMessage("You get " + target
/*  3427 */                   .getNameWithGenus() + ".");
/*  3428 */               Server.getInstance().broadCastAction(performer
/*  3429 */                   .getName() + " gets " + target.getNameWithGenus() + ".", performer, 
/*  3430 */                   Math.min(Math.max(3, target.getSizeZ() / 10), 10));
/*  3431 */               PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.TAKEN_ITEM);
/*       */             
/*       */             }
/*       */             else {
/*       */               
/*  3436 */               result.sendToPerformer(performer);
/*       */             } 
/*       */           } 
/*       */         }
/*  3440 */         return true;
/*       */       } 
/*  3442 */       if (action == 12) {
/*       */         
/*  3444 */         if (target.getTemplateId() == 1396 && target.isPlanted() && target.getAuxData() > 0) {
/*  3445 */           lightItem(target, (Item)null, performer);
/*  3446 */         } else if (target.getTemplateId() != 729 || target.getAuxData() > 0) {
/*  3447 */           lightItem(target, (Item)null, performer);
/*  3448 */         }  return true;
/*       */       } 
/*  3450 */       if (action == 607) {
/*       */         
/*  3452 */         comm.sendAddToCreationWindow(target);
/*  3453 */         target.addCreationWindowWatcher((Player)performer);
/*  3454 */         return true;
/*       */       } 
/*  3456 */       if (action == 605) {
/*       */         
/*  3458 */         if (target.isChair()) {
/*       */           
/*  3460 */           Vehicle chair = Vehicles.getVehicle(target);
/*  3461 */           if (chair.isAnySeatOccupied()) {
/*       */             
/*  3463 */             comm.sendNormalServerMessage("The " + Vehicle.getVehicleName(chair) + " is occupied and may not be taken.");
/*  3464 */             return true;
/*       */           } 
/*       */         } 
/*  3467 */         if (performer.getVehicle() != -10L) {
/*       */           
/*       */           try {
/*       */             
/*  3471 */             Item vehicle = Items.getItem(performer.getVehicle());
/*  3472 */             if (vehicle.getTemplateId() == 853) {
/*       */               
/*  3474 */               if (target.isBoat() || (target
/*       */                 
/*  3476 */                 .isUnfinished() && target.getRealTemplate() != null && target.getRealTemplate()
/*  3477 */                 .isBoat()))
/*       */               {
/*  3479 */                 return CargoTransportationMethods.loadShip(performer, target, counter);
/*       */               }
/*       */ 
/*       */               
/*  3483 */               comm.sendNormalServerMessage(
/*  3484 */                   StringUtil.format("You can't load the %s on to the %s.", new Object[] {
/*  3485 */                       StringUtil.toLowerCase(target.getName()), 
/*  3486 */                       StringUtil.toLowerCase(vehicle.getName())
/*       */                     }));
/*       */             }
/*  3489 */             else if (vehicle.getTemplateId() == 1410) {
/*       */               
/*  3491 */               if (target.getTemplateId() == 1311) {
/*  3492 */                 return CargoTransportationMethods.loadCargo(performer, target, counter);
/*       */               }
/*  3494 */               comm.sendNormalServerMessage(
/*  3495 */                   StringUtil.format("You can't load the %s on to the %s.", new Object[] {
/*  3496 */                       StringUtil.toLowerCase(target.getName()), 
/*  3497 */                       StringUtil.toLowerCase(vehicle.getName())
/*       */                     }));
/*       */             } else {
/*       */               
/*  3501 */               return CargoTransportationMethods.loadCargo(performer, target, counter);
/*       */             }
/*       */           
/*  3504 */           } catch (NoSuchItemException nsi) {
/*       */             
/*  3506 */             logger.log(Level.FINE, "Unable to find vehicle item.", (Throwable)nsi);
/*  3507 */             return true;
/*       */           } 
/*       */         }
/*       */       } else {
/*  3511 */         if (action == 606)
/*       */         {
/*  3513 */           return CargoTransportationMethods.unloadCargo(performer, target, counter);
/*       */         }
/*  3515 */         if (action == 907)
/*       */         
/*  3517 */         { if (target.getTemplateId() == 1432) {
/*       */             
/*  3519 */             if (Features.Feature.CHICKEN_COOPS.isEnabled())
/*       */             {
/*  3521 */               Creature[] folls = performer.getFollowers();
/*  3522 */               if (folls.length > 0)
/*       */               {
/*  3524 */                 return CargoTransportationMethods.loadChicken(performer, target, counter);
/*       */               }
/*       */             }
/*       */           
/*  3528 */           } else if (target.getTemplateId() == 1311) {
/*       */             
/*  3530 */             if (Features.Feature.TRANSPORTABLE_CREATURES.isEnabled())
/*       */             {
/*  3532 */               Creature[] folls = performer.getFollowers();
/*  3533 */               if (folls.length > 0)
/*       */               {
/*  3535 */                 return CargoTransportationMethods.loadCreature(performer, target, counter);
/*       */               }
/*       */             }
/*       */           
/*       */           }  }
/*  3540 */         else if (action == 908)
/*       */         
/*       */         { try
/*       */           {
/*  3544 */             long tpid = target.getTopParent();
/*  3545 */             Item topParent = Items.getItem(tpid);
/*  3546 */             if (topParent.getTemplateId() == 1432) {
/*  3547 */               return CargoTransportationMethods.unloadChicken(performer, target, counter);
/*       */             }
/*  3549 */             return CargoTransportationMethods.unloadCreature(performer, target, counter);
/*       */           }
/*  3551 */           catch (NoSuchItemException ex)
/*       */           {
/*  3553 */             logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*       */           }
/*       */            }
/*  3556 */         else if (action == 493)
/*       */         
/*  3558 */         { if (target.getData() != -10L) {
/*       */             
/*       */             try {
/*       */               
/*  3562 */               Creature cagedCreature = Creatures.getInstance().getCreature(target.getData());
/*  3563 */               if (cagedCreature != null) {
/*  3564 */                 CreatureBehaviour.handle_CAGE_SET_PROTECTED(performer, cagedCreature);
/*       */               }
/*  3566 */             } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */ 
/*       */           
/*       */           }
/*       */            }
/*       */         
/*  3572 */         else if (action == 7 || action == 638 || action == 925)
/*       */         
/*  3574 */         { if (target.isSurfaceOnly() && !performer.isOnSurface()) {
/*       */             
/*  3576 */             comm.sendNormalServerMessage(target.getName() + " can only be dropped on the surface.");
/*  3577 */             return true;
/*       */           } 
/*  3579 */           if ((!target.isNoDrop() || performer.getPower() > 0) && !target.isComponentItem()) {
/*       */             
/*  3581 */             if (action == 925)
/*       */             {
/*  3583 */               return MethodsItems.placeItem(performer, target, act, counter);
/*       */             }
/*       */ 
/*       */             
/*  3587 */             String[] msg = MethodsItems.drop(performer, target, (action == 7));
/*  3588 */             if (msg.length > 0) {
/*       */               
/*  3590 */               comm.sendNormalServerMessage(msg[0] + msg[1] + msg[2]);
/*  3591 */               Server.getInstance().broadCastAction(performer.getName() + " drops " + msg[1] + msg[3], performer, 5);
/*       */             } 
/*       */             
/*  3594 */             return true;
/*       */           }  }
/*       */         else
/*       */         
/*  3598 */         { if (action == 598) {
/*       */             
/*  3600 */             if (target.getTemplateId() == 176) {
/*       */               
/*  3602 */               GmVillageAdInterface vad = new GmVillageAdInterface(performer, target.getWurmId());
/*  3603 */               vad.sendQuestion();
/*       */             } 
/*       */             
/*  3606 */             return true;
/*       */           } 
/*  3608 */           if (target.isLockable() && action == 102) {
/*       */             
/*  3610 */             if (target.getLastOwnerId() == performer.getWurmId() || target
/*  3611 */               .getOwnerId() == performer.getWurmId()) {
/*  3612 */               return MethodsItems.unlock(performer, null, target, counter);
/*       */             }
/*  3614 */             comm.sendNormalServerMessage("Only the owner can unlock that.");
/*  3615 */             return true;
/*       */           } 
/*  3617 */           if (action == 568)
/*       */           
/*  3619 */           { if (target.isLockable()) {
/*       */               
/*  3621 */               if (target.getLockId() == -10L || (target
/*  3622 */                 .isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target))) {
/*       */                 
/*  3624 */                 comm.sendOpenInventoryContainer(target.getWurmId());
/*       */               } else {
/*       */                 
/*       */                 try
/*       */                 {
/*       */ 
/*       */                   
/*  3631 */                   Item lock = Items.getItem(target.getLockId());
/*       */                   
/*  3633 */                   if (!lock.getLocked() || target.isOwner(performer)) {
/*       */                     
/*  3635 */                     comm.sendOpenInventoryContainer(target.getWurmId());
/*  3636 */                     return true;
/*       */                   } 
/*       */ 
/*       */                   
/*  3640 */                   long[] keys = lock.getKeyIds();
/*  3641 */                   for (int i = 0; i < keys.length; i++) {
/*       */                     
/*  3643 */                     Item key = Items.getItem(keys[i]);
/*  3644 */                     if (key.getTopParent() == performer.getInventory().getWurmId()) {
/*       */                       
/*  3646 */                       comm.sendOpenInventoryContainer(target.getWurmId());
/*  3647 */                       return true;
/*       */                     } 
/*       */                   } 
/*       */                   
/*  3651 */                   comm.sendSafeServerMessage("The " + target
/*  3652 */                       .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                 }
/*  3654 */                 catch (NoSuchItemException nsi)
/*       */                 {
/*  3656 */                   comm.sendSafeServerMessage("The " + target
/*  3657 */                       .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                 }
/*       */               
/*       */               }
/*       */             
/*       */             }
/*  3663 */             else if (target.isHollow() && !target.isSealedByPlayer() && target.getTemplateId() != 1342) {
/*  3664 */               comm.sendOpenInventoryContainer(target.getWurmId());
/*       */             } 
/*  3666 */             toReturn = true; }
/*       */           
/*  3668 */           else if (action == 162)
/*       */           
/*  3670 */           { if (target.isRepairable() || ttid == 179 || ttid == 386) {
/*       */               
/*  3672 */               if (target.getTemplateId() == 1311 && !target.isEmpty(true)) {
/*       */                 
/*  3674 */                 comm.sendNormalServerMessage("You must first remove the creature from the cage, in order to repair it.");
/*       */                 
/*  3676 */                 return true;
/*       */               } 
/*  3678 */               toReturn = false;
/*  3679 */               int time = 0;
/*  3680 */               if (counter == 1.0F) {
/*       */                 
/*  3682 */                 if (target.getDamage() == 0.0F) {
/*       */                   
/*  3684 */                   if (performer.getPower() >= 5 && Servers.localServer.testServer) {
/*  3685 */                     target.setDamage(30.0F);
/*       */                   } else {
/*       */                     
/*  3688 */                     comm.sendNormalServerMessage("The " + target
/*  3689 */                         .getName() + " doesn't need repairing.");
/*  3690 */                     toReturn = true;
/*       */                   }
/*       */                 
/*  3693 */                 } else if (performer.isGuest()) {
/*       */                   
/*  3695 */                   comm.sendNormalServerMessage("Guests are not allowed to repair items.");
/*  3696 */                   toReturn = true;
/*       */                 }
/*  3698 */                 else if (target.getTemperature() > 1000 && (target
/*  3699 */                   .isWood() || target.isCloth() || target.isMelting() || target.isLiquidInflammable())) {
/*       */                   
/*  3701 */                   comm.sendNormalServerMessage("The " + target
/*  3702 */                       .getName() + " is too hot to be repaired.");
/*  3703 */                   toReturn = true;
/*       */                 }
/*  3705 */                 else if (target.isKingdomMarker() && (
/*  3706 */                   !performer.isFriendlyKingdom(target.getAuxData()) || performer.getEnemyPresense() > 0 || performer
/*  3707 */                   .getFightingSkill().getRealKnowledge() < 20.0D || !performer.isPaying())) {
/*       */                   
/*  3709 */                   if (performer.getEnemyPresense() > 0) {
/*  3710 */                     comm.sendNormalServerMessage("You are not allowed to repair the " + target
/*  3711 */                         .getName() + " while enemies are about.");
/*       */                   }
/*  3713 */                   else if (performer.getFightingSkill().getRealKnowledge() < 20.0D) {
/*  3714 */                     comm.sendNormalServerMessage("You are not allowed to repair the " + target
/*  3715 */                         .getName() + " until you have 20 fighting skill.");
/*       */                   }
/*  3717 */                   else if (!performer.isFriendlyKingdom(target.getAuxData())) {
/*  3718 */                     comm.sendNormalServerMessage("You are not allowed to repair the " + target
/*  3719 */                         .getName() + " it is not part of your kingdom.");
/*       */                   }
/*  3721 */                   else if (!performer.isPaying()) {
/*  3722 */                     comm.sendNormalServerMessage("You are not allowed to repair the " + target
/*  3723 */                         .getName() + " without premium status.");
/*       */                   } else {
/*       */                     
/*  3726 */                     comm.sendNormalServerMessage("You are not allowed to repair the " + target
/*  3727 */                         .getName() + ".");
/*  3728 */                   }  toReturn = true;
/*       */                 }
/*  3730 */                 else if (target.getOwnerId() != -10L || Methods.isActionAllowed(performer, action, target)) {
/*       */                   
/*  3732 */                   act.setPower(target.getDamage());
/*  3733 */                   time = target.getRepairTime(performer);
/*  3734 */                   comm.sendNormalServerMessage("You start repairing the " + target
/*  3735 */                       .getName() + ".");
/*  3736 */                   Server.getInstance().broadCastAction(performer
/*  3737 */                       .getName() + " starts repairing " + target.getNameWithGenus() + ".", performer, 5);
/*  3738 */                   performer.sendActionControl(Actions.actionEntrys[162].getVerbString(), true, time);
/*       */                   
/*  3740 */                   act.setTimeLeft(time);
/*       */                 } else {
/*       */                   
/*  3743 */                   return true;
/*       */                 } 
/*       */               } else {
/*       */                 
/*  3747 */                 time = act.getTimeLeft();
/*  3748 */                 if (target.getZoneId() == -10L && performer.isOnSurface() != target.isOnSurface()) {
/*       */                   
/*  3750 */                   comm.sendNormalServerMessage("You can't reach the " + target
/*  3751 */                       .getName() + " now.");
/*  3752 */                   return true;
/*       */                 } 
/*  3754 */                 if (!target.isRepairable() && ttid != 179 && ttid != 386) {
/*       */                   
/*  3756 */                   comm.sendNormalServerMessage(target.getName() + "is not repairable.");
/*  3757 */                   return true;
/*       */                 } 
/*  3759 */                 if (act.justTickedSecond())
/*       */                 {
/*  3761 */                   target.repair(performer, (short)time, act.getPower());
/*       */                 }
/*  3763 */                 if (counter * 10.0F > time || target.getDamage() == 0.0F)
/*       */                 {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                   
/*  3771 */                   comm.sendNormalServerMessage("You repair the " + target.getName() + ".");
/*  3772 */                   if (target.getDamage() > 0.0F) {
/*  3773 */                     target.setDamage(0.0F);
/*       */                   }
/*  3775 */                   Server.getInstance().broadCastAction(performer
/*  3776 */                       .getName() + " repairs " + target.getNameWithGenus() + ".", performer, 5);
/*  3777 */                   toReturn = true;
/*       */                 }
/*       */               
/*       */               } 
/*       */             }  }
/*  3782 */           else if (action == 384)
/*       */           
/*  3784 */           { if (target.getOwnerId() == -10L && target.isMeditation()) {
/*  3785 */               return Cults.meditate(performer, performer.getLayer(), act, counter, target);
/*       */             }
/*  3787 */             toReturn = true; }
/*       */           
/*  3789 */           else if (action == 385)
/*       */           
/*  3791 */           { if (target.getOwnerId() == -10L && target.isMeditation()) {
/*       */               
/*  3793 */               Cultist c = performer.getCultist();
/*  3794 */               if (c != null) {
/*       */ 
/*       */                 
/*  3797 */                 CultQuestion cq = new CultQuestion(performer, "Leaving the path", "Are you sure?", -1L, c, c.getPath(), true, false);
/*  3798 */                 cq.sendQuestion();
/*       */               } 
/*       */             } 
/*  3801 */             toReturn = true; }
/*       */           
/*  3803 */           else if (action == 386)
/*       */           
/*  3805 */           { if (target.getOwnerId() == -10L && target.isMeditation()) {
/*       */               
/*  3807 */               Cultist c = performer.getCultist();
/*  3808 */               if (c != null && c.getLevel() > 2) {
/*       */                 
/*  3810 */                 CultQuestion cq = new CultQuestion(performer, "Path leadership", "Leaders", -1L, c, c.getPath(), true, true);
/*       */                 
/*  3812 */                 cq.sendQuestion();
/*       */               } 
/*       */             } 
/*  3815 */             toReturn = true; }
/*       */           
/*  3817 */           else if (action == 722)
/*       */           
/*  3819 */           { if (!Features.Feature.ALLOW_MEDPATHCHANGE.isEnabled()) {
/*       */               
/*  3821 */               performer.getCommunicator().sendNormalServerMessage("That feature is not currently available.");
/*       */             }
/*  3823 */             else if (target.getOwnerId() == -10L && target.isMeditation()) {
/*       */               
/*  3825 */               Cultist c = performer.getCultist();
/*  3826 */               if (c != null && c.getPath() == 4) {
/*       */                 
/*  3828 */                 ChangeMedPathQuestion cq = new ChangeMedPathQuestion(performer, c, target);
/*  3829 */                 cq.sendQuestion();
/*       */               } 
/*       */             } 
/*  3832 */             toReturn = true; }
/*       */           
/*  3834 */           else if (action == 59)
/*       */           
/*  3836 */           { toReturn = true;
/*  3837 */             if (target.getOwnerId() == performer.getWurmId() || target.lastOwner == performer.getWurmId() || performer
/*  3838 */               .getPower() >= 2) {
/*       */               
/*  3840 */               if (!target.isNoRename() && (!target.isVehicle() || target.isChair() || target.isTent()))
/*       */               {
/*  3842 */                 int maxSize = 20;
/*  3843 */                 if (target.isSign()) {
/*       */                   
/*  3845 */                   int mod = 1;
/*  3846 */                   if (ttid == 209)
/*  3847 */                     mod = 2; 
/*  3848 */                   maxSize = Math.max(5, (int)((target.getRarity() * 3) + target.getCurrentQualityLevel() * mod));
/*       */                 } 
/*  3850 */                 if (target.getTemplateId() == 651) {
/*  3851 */                   maxSize = 32;
/*       */                 }
/*       */ 
/*       */                 
/*  3855 */                 TextInputQuestion tiq = new TextInputQuestion(performer, "Setting description for " + target.getName() + ".", "Set the new description:", 1, target.getWurmId(), maxSize, (target.getTemplateId() == 656));
/*  3856 */                 tiq.setOldtext(target.getDescription());
/*  3857 */                 tiq.sendQuestion();
/*       */               }
/*       */             
/*  3860 */             } else if (!target.isNoRename() && target.isShelf()) {
/*       */               
/*  3862 */               Item shelfParent = target.getOuterItemOrNull();
/*  3863 */               if (shelfParent != null && shelfParent.mayManage(performer))
/*       */               {
/*  3865 */                 int maxSize = 20;
/*       */                 
/*  3867 */                 TextInputQuestion tiq = new TextInputQuestion(performer, "Setting description for " + target.getName() + ".", "Set the new description:", 1, target.getWurmId(), maxSize, false);
/*       */                 
/*  3869 */                 tiq.setOldtext(target.getDescription());
/*  3870 */                 tiq.sendQuestion();
/*       */               }
/*       */             
/*       */             }  }
/*  3874 */           else if (action == 518)
/*       */           
/*  3876 */           { if (ttid == 782) {
/*       */               
/*  3878 */               int digTilex = (int)performer.getStatus().getPositionX() + 2 >> 2;
/*  3879 */               int digTiley = (int)performer.getStatus().getPositionY() + 2 >> 2;
/*  3880 */               toReturn = CaveTileBehaviour.raiseRockLevel(performer, target, digTilex, digTiley, counter, act);
/*       */             } else {
/*       */               
/*  3883 */               comm.sendNormalServerMessage("You can not use this to raise ground.");
/*       */             }  }
/*  3885 */           else if (action == 3 && (ttid == 175 || ttid == 651 || ttid == 1097 || ttid == 1098 || (ttid == 466 && target
/*  3886 */             .getAuxData() == 1)))
/*       */           
/*  3888 */           { if (!performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), (target
/*  3889 */                 .isVehicle() && !target.isTent() && !target.isChair()) ? 
/*  3890 */                 Math.max(4, target.getSizeZ() / 100) : 4.0F))
/*  3891 */               return true; 
/*  3892 */             if (ttid == 175) {
/*       */               
/*  3894 */               if (!Servers.localServer.testServer || performer.getPower() >= 5) {
/*       */ 
/*       */                 
/*       */                 try {
/*  3898 */                   long parentId = target.getParentId();
/*  3899 */                   Item parent = Items.getItem(parentId);
/*  3900 */                   parent.dropItem(target.getWurmId(), false);
/*       */                   
/*  3902 */                   float qlCreated = 99.0F;
/*  3903 */                   int createdId = 527;
/*  3904 */                   if (target.getAuxData() == 1) {
/*       */ 
/*       */                     
/*  3907 */                     createdId = 602;
/*  3908 */                     qlCreated = 60.0F;
/*       */                   }
/*  3910 */                   else if (target.getAuxData() == 2) {
/*       */ 
/*       */ 
/*       */                     
/*  3914 */                     createdId = 653;
/*  3915 */                     qlCreated = 60.0F;
/*       */                   }
/*  3917 */                   else if (target.getAuxData() == 3) {
/*       */ 
/*       */                     
/*  3920 */                     createdId = 700;
/*  3921 */                     qlCreated = 99.0F;
/*       */                   }
/*  3923 */                   else if (target.getAuxData() == 4) {
/*       */ 
/*       */                     
/*  3926 */                     createdId = 738;
/*  3927 */                     qlCreated = 1.0F;
/*       */                   }
/*  3929 */                   else if (target.getAuxData() == 5) {
/*       */ 
/*       */                     
/*  3932 */                     createdId = 791;
/*  3933 */                     qlCreated = 99.0F;
/*       */                   }
/*  3935 */                   else if (target.getAuxData() == 6) {
/*       */ 
/*       */                     
/*  3938 */                     createdId = 844;
/*  3939 */                     qlCreated = 99.0F;
/*       */                   }
/*  3941 */                   else if (target.getAuxData() == 7) {
/*       */ 
/*       */                     
/*  3944 */                     createdId = 972;
/*  3945 */                     qlCreated = 99.0F;
/*       */                   }
/*  3947 */                   else if (target.getAuxData() == 8) {
/*       */ 
/*       */                     
/*  3950 */                     createdId = 1032;
/*  3951 */                     qlCreated = 99.0F;
/*       */                   }
/*  3953 */                   else if (target.getAuxData() == 9) {
/*       */ 
/*       */                     
/*  3956 */                     createdId = 1297;
/*  3957 */                     qlCreated = 99.0F;
/*       */                   }
/*  3959 */                   else if (target.getAuxData() == 10) {
/*       */ 
/*       */                     
/*  3962 */                     createdId = 1334;
/*  3963 */                     qlCreated = 99.0F;
/*       */                   }
/*  3965 */                   else if (target.getAuxData() == 11) {
/*       */ 
/*       */                     
/*  3968 */                     createdId = 1437;
/*  3969 */                     qlCreated = 99.0F;
/*       */                   } 
/*       */ 
/*       */                   
/*  3973 */                   if (performer.getInventory() == null) {
/*       */                     
/*  3975 */                     performer.getCommunicator().sendAlertServerMessage("Something went wrong while attempting to open your gift, please try again later. If this persists, please contact an administrator.");
/*       */                     
/*  3977 */                     return true;
/*       */                   } 
/*  3979 */                   Item gift = ItemFactory.createItem(createdId, qlCreated, performer.getName());
/*       */                   
/*  3981 */                   performer.getInventory().insertItem(gift, true);
/*  3982 */                   comm.sendSafeServerMessage("There is something inside with your name on it!");
/*  3983 */                   if (target.getAuxData() == 1) {
/*       */                     
/*  3985 */                     gift.setAuxData((byte)1);
/*  3986 */                     comm.sendSafeServerMessage("You hear a barely audible humming sound.");
/*       */                   }
/*  3988 */                   else if (target.getAuxData() == 2) {
/*       */                     
/*  3990 */                     createdId = 654;
/*  3991 */                     qlCreated = 99.0F;
/*  3992 */                     Item liquid = ItemFactory.createItem(createdId, qlCreated, null);
/*  3993 */                     gift.insertItem(liquid, true);
/*       */                   }
/*  3995 */                   else if (target.getAuxData() == 4) {
/*       */                     
/*  3997 */                     gift.setAuxData((byte)99);
/*       */                   }
/*  3999 */                   else if (target.getAuxData() == 6) {
/*       */                     
/*  4001 */                     gift.setAuxData((byte)99);
/*       */                   } 
/*  4003 */                   Items.decay(target.getWurmId(), target.getDbStrings());
/*       */                   
/*  4005 */                   SoundPlayer.playSong("sound.music.song.christmas", performer);
/*       */                 }
/*  4007 */                 catch (NoSuchTemplateException nst) {
/*       */                   
/*  4009 */                   logger.log(Level.WARNING, performer
/*  4010 */                       .getName() + " Christmas present template gone? " + nst.getMessage(), (Throwable)nst);
/*       */                 }
/*  4012 */                 catch (NoSuchItemException nsi) {
/*       */                   
/*  4014 */                   logger.log(Level.WARNING, performer.getName() + " Christmas present loss: " + nsi.getMessage(), (Throwable)nsi);
/*       */                 }
/*  4016 */                 catch (FailedException fe) {
/*       */                   
/*  4018 */                   logger.log(Level.WARNING, performer
/*  4019 */                       .getName() + " receives no Christmas present: " + fe.getMessage(), (Throwable)fe);
/*       */                 } 
/*       */               } else {
/*       */                 
/*  4023 */                 comm.sendNormalServerMessage("Nothing happens here in these weird lands!");
/*       */               } 
/*       */             } else {
/*       */               
/*  4027 */               if (target.getOwnerId() != performer.getWurmId()) {
/*       */                 
/*  4029 */                 comm.sendSafeServerMessage("You need to carry the " + target
/*  4030 */                     .getName() + " in order to open it.");
/*  4031 */                 return true;
/*       */               } 
/*       */               
/*       */               try {
/*  4035 */                 if (performer.getPower() <= 0 && !target.getDescription().isEmpty() && 
/*  4036 */                   !target.getDescription().equalsIgnoreCase(performer.getName()))
/*       */                 {
/*  4038 */                   if (!target.getCreatorName().equalsIgnoreCase(performer.getName()))
/*       */                   {
/*  4040 */                     if (!Servers.localServer.PVPSERVER || Servers.localServer.testServer) {
/*       */                       
/*  4042 */                       comm.sendNormalServerMessage("This gift is not for you to open!");
/*  4043 */                       return false;
/*       */                     } 
/*       */                   }
/*       */                 }
/*  4047 */                 if (ttid == 651) {
/*       */                   
/*  4049 */                   long parentId = target.getParentId();
/*  4050 */                   Item parent = Items.getItem(parentId);
/*  4051 */                   long wurmid = target.getData();
/*  4052 */                   if (wurmid <= 0L) {
/*       */                     
/*  4054 */                     comm.sendSafeServerMessage("The " + target.getName() + " is empty.");
/*  4055 */                     return true;
/*       */                   } 
/*  4057 */                   Item toInsert = Items.getItem(wurmid);
/*       */                   
/*  4059 */                   parent.dropItem(target.getWurmId(), false);
/*  4060 */                   comm.sendSafeServerMessage("There is " + toInsert.getNameWithGenus() + " inside with your name on it!");
/*  4061 */                   performer.getInventory().insertItem(toInsert, true);
/*       */                 }
/*  4063 */                 else if (ttid == 1097 || ttid == 1098) {
/*       */                   
/*  4065 */                   int itemTemplate = getRandomItemFromPack(ttid);
/*       */ 
/*       */                   
/*       */                   try {
/*  4069 */                     byte rarity = 0;
/*  4070 */                     if (itemTemplate == 867)
/*       */                     {
/*  4072 */                       if (Server.rand.nextInt(10) == 0) {
/*  4073 */                         rarity = 2;
/*       */                       } else {
/*  4075 */                         rarity = 0;
/*       */                       }  } 
/*  4077 */                     Item toInsert = ItemFactory.createItem(itemTemplate, 80.0F + Server.rand.nextFloat() * 20.0F, (ttid == 1098) ? 34 : 0, rarity, performer.getName());
/*       */                     
/*  4079 */                     comm.sendSafeServerMessage("There is " + toInsert.getNameWithGenus() + " inside with your name on it!");
/*  4080 */                     performer.getInventory().insertItem(toInsert, true);
/*       */                   }
/*  4082 */                   catch (Exception ex) {
/*       */                     
/*  4084 */                     logger.log(Level.WARNING, performer.getName() + " opening gift pack:" + ex.getMessage(), ex);
/*       */                   }
/*       */                 
/*  4087 */                 } else if (ttid == 466 && target.getAuxData() == 1) {
/*       */ 
/*       */                   
/*       */                   try {
/*  4091 */                     Item chocolate = ItemFactory.createItem(1185, 50.0F + Server.rand.nextFloat() * 40.0F, "Easter Bunny");
/*  4092 */                     Item sleepPowder = ItemFactory.createItem(666, 99.0F, "Easter Bunny");
/*  4093 */                     Item bonusItem = null;
/*  4094 */                     if (Server.rand.nextFloat() < 0.66F) {
/*       */                       
/*  4096 */                       bonusItem = ItemFactory.createItem(getRandomGem(true), 50.0F + Server.rand.nextFloat() * 49.0F, "Easter Bunny");
/*       */                     }
/*       */                     else {
/*       */                       
/*  4100 */                       bonusItem = ItemFactory.createItem(1307, 50.0F + Server.rand.nextFloat() * 40.0F, "Easter Bunny");
/*  4101 */                       bonusItem.setRealTemplate(getRandomStatueFragment());
/*  4102 */                       bonusItem.setAuxData((byte)1);
/*  4103 */                       bonusItem.setData1(1);
/*  4104 */                       bonusItem.setData2(30 + Server.rand.nextInt(50));
/*  4105 */                       bonusItem.setWeight(bonusItem.getRealTemplate().getWeightGrams() / bonusItem.getRealTemplate().getFragmentAmount(), false);
/*       */                     } 
/*       */                     
/*  4108 */                     comm.sendSafeServerMessage("You break the egg open and find some items!");
/*  4109 */                     performer.getInventory().insertItem(chocolate, true);
/*  4110 */                     performer.getInventory().insertItem(sleepPowder, true);
/*  4111 */                     performer.getInventory().insertItem(bonusItem, true);
/*       */                   }
/*  4113 */                   catch (Exception e) {
/*       */                     
/*  4115 */                     logger.log(Level.WARNING, performer.getName() + " opening easter egg:" + e.getMessage(), e);
/*       */                   } 
/*       */                 } 
/*  4118 */                 Items.destroyItem(target.getWurmId());
/*       */               
/*       */               }
/*  4121 */               catch (NoSuchItemException nsi) {
/*       */                 
/*  4123 */                 comm.sendSafeServerMessage("Something was not in the package! It poofs gone. What a mess!");
/*       */                 
/*  4125 */                 logger.log(Level.WARNING, performer.getName() + " gift item loss: " + nsi.getMessage(), (Throwable)nsi);
/*       */               }
/*       */             
/*       */             }  }
/*  4129 */           else if (action == 118)
/*       */           
/*  4131 */           { if (target.isGem()) {
/*       */               
/*  4133 */               if (target.getData1() > 0)
/*       */               {
/*  4135 */                 if (performer.getWurmId() == target.getOwnerId()) {
/*       */                   
/*  4137 */                   int diff = (int)(performer.getFaith() - performer.getFavor());
/*  4138 */                   if (diff > 0) {
/*       */                     
/*  4140 */                     int avail = target.getData1();
/*  4141 */                     int received = Math.min(diff, avail);
/*       */                     
/*       */                     try {
/*  4144 */                       float qlMod = target.getRarity() + 1.0F;
/*  4145 */                       performer.setFavor(performer.getFavor() + received);
/*  4146 */                       target.setData1(target.getData1() - (int)(received / qlMod));
/*       */ 
/*       */                       
/*  4149 */                       target.setQualityLevel(target.getQualityLevel() - received / 2.0F / qlMod);
/*  4150 */                       if (target.getQualityLevel() < 1.0F)
/*  4151 */                         Items.destroyItem(target.getWurmId()); 
/*  4152 */                       comm.sendNormalServerMessage("You feel a rush of blood to your head as the power from the gem enters your body.");
/*       */                       
/*  4154 */                       Server.getInstance().broadCastAction(performer
/*  4155 */                           .getName() + " straightens up as " + performer.getHeSheItString() + " draws power from a gem.", performer, 5);
/*       */ 
/*       */                       
/*  4158 */                       if (received >= 50.0F) {
/*  4159 */                         performer.achievement(625);
/*       */                       }
/*  4161 */                     } catch (IOException iox) {
/*       */                       
/*  4163 */                       logger.log(Level.WARNING, performer.getName() + ": " + iox.getMessage(), iox);
/*       */                     }
/*       */                   
/*       */                   } 
/*       */                 } 
/*       */               }
/*  4169 */             } else if (target.isDeathProtection()) {
/*       */               
/*  4171 */               toReturn = true;
/*  4172 */               if (performer.getWurmId() == target.getOwnerId())
/*       */               {
/*  4174 */                 if (performer.isDeathProtected())
/*       */                 {
/*  4176 */                   comm.sendNormalServerMessage("Nothing happens.");
/*       */                 }
/*       */                 else
/*       */                 {
/*  4180 */                   comm.sendNormalServerMessage("Your skin tickles all over for a few seconds, as if a thousand ants crawled upon it.");
/*       */                   
/*  4182 */                   Server.getInstance().broadCastAction(performer
/*  4183 */                       .getName() + " shivers for a few seconds as " + performer.getHeSheItString() + " uses " + target
/*  4184 */                       .getNameWithGenus() + ".", performer, 5);
/*  4185 */                   performer.setDeathProtected(true);
/*  4186 */                   Items.destroyItem(target.getWurmId());
/*  4187 */                   performer.achievement(153);
/*       */                 }
/*       */               
/*       */               }
/*  4191 */             } else if (ttid == 527) {
/*       */               
/*  4193 */               if (target.getOwnerId() == performer.getWurmId()) {
/*       */                 
/*  4195 */                 if (performer.getEnemyPresense() <= 0) {
/*       */                   
/*  4197 */                   performer.achievement(5);
/*  4198 */                   performer.activeFarwalkerAmulet(target);
/*       */                 } else {
/*       */                   
/*  4201 */                   comm.sendNormalServerMessage("You fiddle with the " + target
/*  4202 */                       .getName() + " but are too stressed to use it.");
/*       */                 } 
/*       */               } else {
/*  4205 */                 comm.sendNormalServerMessage("You need to carry the " + target
/*  4206 */                     .getName() + " in order to use it.");
/*       */               } 
/*  4208 */             } else if (ttid == 738) {
/*       */               
/*  4210 */               if (target.getAuxData() > 0)
/*       */               {
/*  4212 */                 String sound = "sound.emote.chuckle";
/*  4213 */                 if (target.getAuxData() > 20) {
/*       */                   
/*  4215 */                   if (target.getCurrentQualityLevel() > 80.0F) {
/*  4216 */                     sound = "sound.emote.laugh";
/*  4217 */                   } else if (target.getCurrentQualityLevel() > 60.0F) {
/*  4218 */                     sound = "sound.emote.applaud";
/*  4219 */                   } else if (target.getCurrentQualityLevel() > 30.0F) {
/*  4220 */                     sound = "sound.emote.call";
/*       */                   }
/*       */                 
/*       */                 }
/*  4224 */                 else if (target.getCurrentQualityLevel() > 80.0F) {
/*  4225 */                   sound = "sound.emote.curse";
/*  4226 */                 } else if (target.getCurrentQualityLevel() > 60.0F) {
/*  4227 */                   sound = "sound.emote.insult";
/*  4228 */                 } else if (target.getCurrentQualityLevel() > 30.0F) {
/*  4229 */                   sound = "sound.emote.disagree";
/*       */                 } else {
/*  4231 */                   sound = "sound.emote.worry";
/*       */                 } 
/*  4233 */                 SoundPlayer.playSound(sound, target, 1.5F);
/*  4234 */                 comm.sendAnimation(target.getWurmId(), "model.animation.use", false, false);
/*  4235 */                 target.setAuxData((byte)(target.getAuxData() - 1));
/*       */               }
/*       */             
/*  4238 */             } else if (ttid == 972) {
/*       */               
/*  4240 */               comm.sendAnimation(target.getWurmId(), "use", false, false);
/*  4241 */               comm.sendNormalServerMessage("The Yule Goat likes that!");
/*       */             }
/*  4243 */             else if (ttid == 741) {
/*       */               
/*  4245 */               if (target.getOwnerId() == performer.getWurmId()) {
/*  4246 */                 comm.sendNormalServerMessage("You can't use the " + target
/*  4247 */                     .getName() + " while carrying it.");
/*       */               } else {
/*  4249 */                 performer.activeFarwalkerAmulet(target);
/*       */               } 
/*  4251 */             } else if (ttid == 5 || ttid == 834 || ttid == 836) {
/*       */               
/*  4253 */               if (target.getOwnerId() == performer.getWurmId()) {
/*  4254 */                 performer.activePotion(target);
/*       */               } else {
/*  4256 */                 comm.sendNormalServerMessage("You need to carry the " + target
/*  4257 */                     .getName() + " in order to use it.");
/*       */               } 
/*  4259 */             } else if (ttid == 781 || ttid == 1300) {
/*       */               
/*  4261 */               if (target.getOwnerId() == performer.getWurmId())
/*       */               {
/*  4263 */                 if (ttid == 781 || target.getAuxData() == 1) {
/*  4264 */                   comm.sendCustomizeFace(performer.getFace(), target.getWurmId());
/*       */                 } else {
/*       */                   
/*  4267 */                   ChangeAppearanceQuestion question = new ChangeAppearanceQuestion(performer, target);
/*  4268 */                   question.sendQuestion();
/*       */                 } 
/*  4270 */                 toReturn = true;
/*       */               }
/*       */             
/*  4273 */             } else if (ttid == 722) {
/*       */               
/*  4275 */               if (target.getOwnerId() != performer.getWurmId()) {
/*  4276 */                 toReturn = MethodsItems.useBellTower(performer, target, act, counter);
/*       */               }
/*  4278 */             } else if (ttid == 719) {
/*       */               
/*  4280 */               if (target.getOwnerId() == performer.getWurmId()) {
/*       */                 
/*  4282 */                 toReturn = false;
/*  4283 */                 if (counter == 1.0F) {
/*       */                   
/*  4285 */                   if (Server.rand.nextInt(100) == 0) {
/*  4286 */                     SoundPlayer.playSound("sound.bell.handbell.long", performer, 1.5F);
/*       */                   } else {
/*  4288 */                     SoundPlayer.playSound("sound.bell.handbell", performer, 1.5F);
/*  4289 */                   }  performer.sendActionControl(Actions.actionEntrys[118].getVerbString(), true, 50);
/*       */                 } 
/*  4291 */                 if (counter >= 5.0F)
/*  4292 */                   toReturn = true; 
/*       */               } 
/*       */             } else {
/*  4295 */               if (target.isServerPortal()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*  4302 */                 String title = "Using a portal";
/*  4303 */                 int data1 = target.getData1();
/*  4304 */                 ServerEntry entry = Servers.getServerWithId(data1);
/*  4305 */                 if (entry != null) {
/*       */                   
/*  4307 */                   if (entry.id == Servers.loginServer.id)
/*  4308 */                     entry = Servers.loginServer; 
/*  4309 */                   if (performer.getPower() == 0 && entry.entryServer && !entry.testServer) {
/*       */                     
/*  4311 */                     title = "Dormant portal";
/*       */ 
/*       */                   
/*       */                   }
/*  4315 */                   else if (entry.HOMESERVER) {
/*       */                     
/*  4317 */                     if (entry.PVPSERVER) {
/*  4318 */                       title = Kingdoms.getNameFor(entry.KINGDOM) + " HOME";
/*       */                     } else {
/*  4320 */                       title = "Dormant portal";
/*       */                     } 
/*       */                   } 
/*       */                 } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*  4341 */                 PortalQuestion port = new PortalQuestion(performer, target.getName(), title, target);
/*  4342 */                 port.sendQuestion();
/*  4343 */                 Server.getInstance().broadCastAction(performer.getName() + " approaches the " + target.getName() + ".", performer, 5);
/*       */                 
/*  4345 */                 return true;
/*       */               } 
/*  4347 */               if (ttid == 602)
/*       */               
/*  4349 */               { if (target.getOwnerId() == performer.getWurmId())
/*       */                 {
/*  4351 */                   int current = target.getAuxData();
/*  4352 */                   if (current <= 1)
/*       */                   {
/*  4354 */                     comm.sendNormalServerMessage("You rub the wand and it makes a humming sound.");
/*       */                     
/*  4356 */                     target.setAuxData((byte)3);
/*  4357 */                     SoundPlayer.playSound("sound.fx.humm", performer, 1.0F);
/*       */                   
/*       */                   }
/*  4360 */                   else if (current == 3)
/*       */                   {
/*  4362 */                     comm.sendNormalServerMessage("You rub the wand and it makes a loud humming sound.");
/*       */                     
/*  4364 */                     target.setAuxData((byte)5);
/*  4365 */                     SoundPlayer.playSound("sound.fx.humm", performer, 1.0F);
/*       */                   
/*       */                   }
/*  4368 */                   else if (current == 5)
/*       */                   {
/*  4370 */                     comm.sendNormalServerMessage("You rub the wand and it makes a barely audible humming sound.");
/*       */ 
/*       */                     
/*  4373 */                     target.setAuxData((byte)1);
/*  4374 */                     SoundPlayer.playSound("sound.fx.humm", performer, 1.0F);
/*       */                   }
/*       */                 
/*       */                 }
/*       */                  }
/*  4379 */               else if (ttid == 843)
/*       */               
/*  4381 */               { if (Features.Feature.NAMECHANGE.isEnabled()) {
/*       */                   
/*  4383 */                   if (target.getOwnerId() == performer.getWurmId()) {
/*       */                     
/*  4385 */                     ChangeNameQuestion ncq = new ChangeNameQuestion(performer, target);
/*  4386 */                     ncq.sendQuestion();
/*  4387 */                     toReturn = true;
/*       */                   } 
/*       */                 } else {
/*       */                   
/*  4391 */                   comm.sendNormalServerMessage("This feature is disabled for now.");
/*       */                 }  }
/*  4393 */               else if (target.isAbility())
/*       */               
/*  4395 */               { toReturn = Abilities.useItem(performer, target, act, counter); }
/*       */               
/*  4397 */               else if (target.isDuelRing())
/*       */               
/*  4399 */               { toReturn = true;
/*  4400 */                 if (performer.getPower() >= 3)
/*       */                 {
/*  4402 */                   if (doesRingMarkersExist(target.getTileX(), target.getTileY())) {
/*  4403 */                     return true;
/*       */                   }
/*       */                   
/*  4406 */                   createMarkers(target.getTileX(), target.getTileY());
/*       */                 }
/*       */                  }
/*       */               
/*  4410 */               else if (target.getTemplateId() == 1438)
/*       */               
/*  4412 */               { toReturn = true;
/*  4413 */                 AffinityQuestion aq = new AffinityQuestion(performer, target);
/*  4414 */                 aq.sendQuestion(); }
/*       */               else
/*       */               
/*  4417 */               { toReturn = true; } 
/*       */             }  }
/*  4419 */           else if (action == 682)
/*       */           
/*  4421 */           { toReturn = true;
/*  4422 */             if (ttid == 233)
/*       */             {
/*  4424 */               if (target.getOwnerId() == performer.getWurmId()) {
/*  4425 */                 toReturn = MethodsItems.usePendulum(performer, target, act, counter);
/*       */               }
/*       */             } }
/*  4428 */           else if (action == 3 && target.isHollow() && !target.isSealedByPlayer() && (
/*  4429 */             !target.getTemplate().hasViewableSubItems() || target.getTemplate().isContainerWithSubItems() || performer
/*  4430 */             .getPower() > 0))
/*       */           
/*  4432 */           { if (target.isSealedByPlayer())
/*  4433 */               return true; 
/*  4434 */             if (target.getTemplateId() == 1342 && !target.isPlanted())
/*  4435 */               return true; 
/*  4436 */             toReturn = true;
/*       */             
/*  4438 */             if (target.getWurmId() != performer.getVehicle() && 
/*  4439 */               !performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), (target
/*  4440 */                 .isVehicle() && !target.isTent()) ? 
/*  4441 */                 Math.max(6, target.getSizeZ() / 100) : 6.0F)) {
/*  4442 */               return toReturn;
/*       */             }
/*       */             
/*  4445 */             boolean isTop = (target.getWurmId() == target.getTopParent() || (target.getTopParentOrNull() != null && target.getTopParentOrNull().getTemplate().hasViewableSubItems() && (!target.getTopParentOrNull().getTemplate().isContainerWithSubItems() || target.isPlacedOnParent())));
/*  4446 */             if (!isTop) {
/*       */               
/*  4448 */               if (target.isLockable()) {
/*       */                 
/*  4450 */                 if (target.getLockId() == -10L || (target
/*  4451 */                   .isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target)) || (target
/*  4452 */                   .getTemplateId() == 850 && MethodsItems.mayUseInventoryOfVehicle(performer, target)) || (target
/*       */                   
/*  4454 */                   .isLocked() && target.mayAccessHold(performer))) {
/*       */                   
/*  4456 */                   comm.sendOpenInventoryContainer(target.getWurmId());
/*       */                 } else {
/*       */                   
/*       */                   try
/*       */                   {
/*       */                     
/*  4462 */                     Item lock = Items.getItem(target.getLockId());
/*       */                     
/*  4464 */                     if (!lock.getLocked() || target.isOwner(performer)) {
/*       */                       
/*  4466 */                       comm.sendOpenInventoryContainer(target.getWurmId());
/*  4467 */                       return true;
/*       */                     } 
/*       */ 
/*       */                     
/*  4471 */                     long[] keys = lock.getKeyIds();
/*  4472 */                     for (int i = 0; i < keys.length; i++) {
/*       */                       
/*  4474 */                       Item key = Items.getItem(keys[i]);
/*  4475 */                       if (key.getTopParent() == performer.getInventory().getWurmId()) {
/*       */                         
/*  4477 */                         comm.sendOpenInventoryContainer(target.getWurmId());
/*  4478 */                         return true;
/*       */                       } 
/*       */                     } 
/*       */                     
/*  4482 */                     comm.sendSafeServerMessage("The " + target
/*  4483 */                         .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                   }
/*  4485 */                   catch (NoSuchItemException nsi)
/*       */                   {
/*  4487 */                     comm.sendSafeServerMessage("The " + target
/*  4488 */                         .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                   }
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*  4494 */               else if (target.getTemplateId() == 272 && target.getWasBrandedTo() != -10L) {
/*       */                 
/*  4496 */                 if (target.mayCommand(performer)) {
/*  4497 */                   comm.sendOpenInventoryContainer(target.getWurmId());
/*       */                 } else {
/*       */                   
/*  4500 */                   performer.getCommunicator().sendNormalServerMessage("You do not have permissions.");
/*       */                   
/*  4502 */                   return true;
/*       */                 } 
/*       */               } else {
/*       */                 
/*  4506 */                 comm.sendOpenInventoryContainer(target.getWurmId());
/*       */               } 
/*  4508 */               return true;
/*       */             } 
/*  4510 */             if (target.getTemplateId() == 272 && target.getWasBrandedTo() != -10L && !target.mayCommand(performer)) {
/*       */               
/*  4512 */               performer.getCommunicator().sendNormalServerMessage("You do not have permissions.");
/*       */               
/*  4514 */               return true;
/*       */             } 
/*  4516 */             if ((target.getTemplateId() == 1239 || target.getTemplateId() == 1175) && target
/*  4517 */               .hasQueen() && !WurmCalendar.isSeasonWinter() && performer
/*  4518 */               .getBestBeeSmoker() == null && performer.getPower() < 2) {
/*       */ 
/*       */               
/*  4521 */               performer.getCommunicator().sendSafeServerMessage("The bees get angry and defend the " + target
/*  4522 */                   .getName() + " by stinging you.");
/*  4523 */               performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (5000.0F + Server.rand
/*  4524 */                   .nextFloat() * 7000.0F), 0.0F, 30.0F, false, false);
/*  4525 */               return true;
/*       */             } 
/*  4527 */             if (target.getTemplateId() == 1239)
/*  4528 */               Achievements.triggerAchievement(performer.getWurmId(), 552); 
/*  4529 */             if (target.isLockable())
/*       */             {
/*  4531 */               if (target.getLockId() == -10L || (target
/*  4532 */                 .isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target)) || (target
/*  4533 */                 .getTemplateId() == 850 && MethodsItems.mayUseInventoryOfVehicle(performer, target))) {
/*       */ 
/*       */                 
/*  4536 */                 if (performer.addItemWatched(target))
/*       */                 {
/*  4538 */                   if (target.getTemplateId() == 995)
/*       */                   {
/*  4540 */                     if (target.getAuxData() < 100) {
/*       */                       
/*  4542 */                       performer.achievement(367);
/*  4543 */                       target.setAuxData((byte)100);
/*       */                     } 
/*       */                   }
/*  4546 */                   if (target.getDescription().isEmpty()) {
/*  4547 */                     comm.sendOpenInventoryWindow(target.getWurmId(), target.getName());
/*       */                   } else {
/*  4549 */                     comm.sendOpenInventoryWindow(target.getWurmId(), target
/*  4550 */                         .getName() + " [" + target.getDescription() + "]");
/*  4551 */                   }  target.addWatcher(target.getWurmId(), performer);
/*  4552 */                   target.sendContainedItems(target.getWurmId(), performer);
/*       */                 }
/*       */               
/*  4555 */               } else if (target.getLockId() != -10L) {
/*       */ 
/*       */                 
/*       */                 try {
/*  4559 */                   Item lock = Items.getItem(target.getLockId());
/*  4560 */                   boolean hasKey = (performer.hasKeyForLock(lock) || target.isOwner(performer));
/*  4561 */                   if (hasKey || (target.isLocked() && target.mayAccessHold(performer))) {
/*       */                     
/*  4563 */                     if (performer.addItemWatched(target))
/*       */                     {
/*  4565 */                       if (target.getDescription().isEmpty()) {
/*  4566 */                         comm.sendOpenInventoryWindow(target.getWurmId(), target
/*  4567 */                             .getName());
/*       */                       } else {
/*  4569 */                         comm.sendOpenInventoryWindow(target.getWurmId(), target
/*  4570 */                             .getName() + " [" + target.getDescription() + "]");
/*  4571 */                       }  target.addWatcher(target.getWurmId(), performer);
/*  4572 */                       target.sendContainedItems(target.getWurmId(), performer);
/*       */                     }
/*       */                   
/*       */                   } else {
/*       */                     
/*  4577 */                     comm.sendSafeServerMessage("The " + target
/*  4578 */                         .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                   }
/*       */                 
/*  4581 */                 } catch (NoSuchItemException nsi) {
/*       */                   
/*  4583 */                   comm.sendSafeServerMessage("The " + target
/*  4584 */                       .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                 }
/*       */               
/*       */               } else {
/*       */                 
/*  4589 */                 comm.sendSafeServerMessage("The " + target
/*  4590 */                     .getName() + " is locked. Please use the key to unlock and open it.");
/*       */               
/*       */               }
/*       */             
/*       */             }
/*  4595 */             else if (performer.addItemWatched(target))
/*       */             {
/*  4597 */               if (target.getTemplateId() == 995)
/*       */               {
/*  4599 */                 if (target.getAuxData() < 100) {
/*       */                   
/*  4601 */                   performer.achievement(367);
/*  4602 */                   target.setAuxData((byte)100);
/*       */                 } 
/*       */               }
/*       */ 
/*       */               
/*  4607 */               if (target.getDescription().isEmpty()) {
/*  4608 */                 comm.sendOpenInventoryWindow(target.getWurmId(), target.getName());
/*       */               } else {
/*  4610 */                 comm.sendOpenInventoryWindow(target.getWurmId(), target
/*  4611 */                     .getName() + " [" + target.getDescription() + "]");
/*  4612 */               }  target.addWatcher(target.getWurmId(), performer);
/*  4613 */               target.sendContainedItems(target.getWurmId(), performer);
/*       */             }
/*       */              }
/*       */           
/*  4617 */           else if (action == 740 && !target.isLiquid())
/*       */           
/*  4619 */           { if (target.isSealedByPlayer()) {
/*       */               
/*  4621 */               if (target.isCrate()) {
/*       */ 
/*       */                 
/*  4624 */                 if (performer.getWurmId() == target.getLastOwnerId() || performer.getPower() > 1) {
/*  4625 */                   toReturn = MethodsItems.removeSecuritySeal(performer, target, act);
/*       */                 } else {
/*  4627 */                   comm.sendSafeServerMessage("Only the last owner can remove the security seal on the " + target
/*  4628 */                       .getName() + ".");
/*       */                 } 
/*       */               } else {
/*  4631 */                 toReturn = MethodsItems.breakSeal(performer, target, act);
/*       */               } 
/*  4633 */             } else if (target.isWrapped()) {
/*       */               
/*  4635 */               toReturn = MethodsItems.unwrap(performer, target, act);
/*       */             }
/*       */              }
/*  4638 */           else if (action == 19)
/*       */           
/*  4640 */           { if (target.isSealedByPlayer()) {
/*  4641 */               tasteLiquid(performer, target);
/*  4642 */             } else if (target.isFood() || target.isLiquid()) {
/*  4643 */               taste(performer, target);
/*  4644 */             } else if (target.isContainerLiquid() && target.getItemCount() == 1) {
/*       */               
/*  4646 */               for (Item i : target.getItems()) {
/*       */                 
/*  4648 */                 if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable()) {
/*       */                   
/*  4650 */                   taste(performer, i);
/*       */                   
/*       */                   break;
/*       */                 } 
/*       */               } 
/*       */             }  }
/*  4656 */           else if (action == 739)
/*       */           
/*  4658 */           { if (target.isRaw() && target.canBeRawWrapped() && !target.isWrapped())
/*       */             {
/*  4660 */               toReturn = MethodsItems.wrap(performer, null, target, act);
/*       */             } }
/*       */           
/*  4663 */           else if (action >= 496 && action <= 502)
/*       */           
/*  4665 */           { toReturn = MethodsReligion.performRitual(performer, null, target, counter, action, act); }
/*       */           
/*  4667 */           else if (action == 504)
/*       */           
/*  4669 */           { toReturn = MethodsItems.conquerTarget(performer, target, comm, counter, act); }
/*       */           
/*  4671 */           else if (action == 336)
/*       */           
/*  4673 */           { toReturn = true;
/*  4674 */             if (target.isMailBox())
/*       */             {
/*  4676 */               if (target.isEmpty(false)) {
/*       */                 
/*  4678 */                 WurmMailSender.checkForWurmMail(performer, target);
/*       */               } else {
/*       */                 
/*  4681 */                 comm.sendNormalServerMessage("The mailbox needs to be empty.");
/*       */               } 
/*       */             } }
/*  4684 */           else if (action == 337)
/*       */           
/*  4686 */           { toReturn = true;
/*  4687 */             if (target.isMailBox())
/*       */             {
/*  4689 */               if (!target.isEmpty(false)) {
/*       */                 
/*  4691 */                 WurmMailSender.sendWurmMail(performer, target);
/*       */               } else {
/*       */                 
/*  4694 */                 comm.sendNormalServerMessage("The mailbox is empty.");
/*       */               } 
/*       */             } }
/*  4697 */           else if (action == 4 && target.isHollow())
/*       */           
/*  4699 */           { toReturn = true;
/*  4700 */             target.close(performer); }
/*       */           
/*  4702 */           else if (action == 181 || action == 99 || action == 697 || action == 696 || action == 864)
/*       */           
/*  4704 */           { if (target.isMoveable(performer) && ((!target.isRoadMarker() && target.getTemplateId() != 1342) || !target.isPlanted())) {
/*  4705 */               toReturn = MethodsItems.moveItem(performer, target, counter, action, act);
/*       */             } else {
/*  4707 */               comm.sendNormalServerMessage("You may not move that item right now.");
/*       */             }  }
/*  4709 */           else if (action == 177 || action == 178)
/*       */           
/*  4711 */           { if (target.isTurnable(performer) && (!target.isRoadMarker() || !target.isPlanted())) {
/*  4712 */               toReturn = MethodsItems.moveItem(performer, target, counter, action, act);
/*       */             } else {
/*  4714 */               comm.sendNormalServerMessage("You may not turn that item right now.");
/*       */             }  }
/*  4716 */           else if (action == 926)
/*       */           
/*  4718 */           { toReturn = MethodsItems.placeLargeItem(performer, target, act, counter); }
/*       */           
/*  4720 */           else if (action == 74)
/*       */           
/*  4722 */           { boolean ok = target.isDraggable();
/*  4723 */             boolean doneMsg = false;
/*  4724 */             if (performer.isTeleporting())
/*       */             {
/*  4726 */               ok = false;
/*       */             }
/*  4728 */             if (target.isVehicle()) {
/*       */               
/*  4730 */               Vehicle vehic = Vehicles.getVehicle(target);
/*  4731 */               if (vehic.pilotId != -10L)
/*  4732 */                 ok = false; 
/*  4733 */               if (vehic.draggers != null && !vehic.draggers.isEmpty())
/*  4734 */                 ok = false; 
/*  4735 */               if (target.getTemplateId() != 186 && target.getKingdom() != performer.getKingdomId())
/*  4736 */                 ok = false; 
/*  4737 */               boolean havePermission = VehicleBehaviour.hasPermission(performer, target);
/*  4738 */               if (!havePermission && !target.mayDrag(performer))
/*  4739 */                 ok = false; 
/*       */             } 
/*  4741 */             if (performer.getVehicle() != -10L) {
/*  4742 */               ok = false;
/*       */             }
/*  4744 */             if (!GeneralUtilities.isOnSameLevel(performer, target)) {
/*       */               
/*  4746 */               VolaTile theTile = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  4747 */               ok = false;
/*  4748 */               if (theTile != null) {
/*       */                 
/*  4750 */                 if (theTile.getStructure() != null) {
/*       */                   
/*  4752 */                   comm.sendNormalServerMessage("You must be on the same floor level to start dragging.");
/*       */                 
/*       */                 }
/*       */                 else {
/*       */                   
/*  4757 */                   comm.sendNormalServerMessage("You need to get closer to the " + target
/*  4758 */                       .getName() + ".");
/*       */                 } 
/*  4760 */                 doneMsg = true;
/*       */               } 
/*       */             } 
/*  4763 */             if (target.getTemplateId() == 1125 && (target
/*  4764 */               .isBusy() || (System.currentTimeMillis() - target.lastRammed < 30000L && performer
/*  4765 */               .getWurmId() != target.lastRamUser))) {
/*       */               
/*  4767 */               ok = false;
/*  4768 */               comm.sendNormalServerMessage("You cannot drag the " + target.getName() + " while it is being used.");
/*  4769 */               doneMsg = true;
/*       */             } 
/*  4771 */             if (performer.getDraggedItem() != null) {
/*       */               
/*  4773 */               ok = false;
/*  4774 */               comm.sendNormalServerMessage("You need to stop dragging the " + performer
/*  4775 */                   .getDraggedItem().getName() + " first.");
/*  4776 */               doneMsg = true;
/*       */             }
/*  4778 */             else if (ok && !Items.isItemDragged(target)) {
/*       */               
/*  4780 */               if (!performer.isGuest()) {
/*  4781 */                 toReturn = MethodsItems.startDragging(act, performer, target);
/*       */               } else {
/*  4783 */                 comm.sendNormalServerMessage("You are not allowed to drag items as a guest.");
/*       */               } 
/*  4785 */             } else if (!doneMsg) {
/*  4786 */               comm.sendNormalServerMessage("You are not allowed to drag that now.");
/*       */             }  }
/*  4788 */           else { if (action == 75) {
/*       */               
/*  4790 */               if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F))
/*       */               {
/*  4792 */                 toReturn = MethodsItems.stopDragging(performer, target);
/*       */               }
/*  4794 */               return true;
/*       */             } 
/*  4796 */             if (action == 182)
/*       */             
/*  4798 */             { if (!target.isNoEatOrDrink())
/*       */               {
/*  4800 */                 toReturn = false;
/*  4801 */                 if (act.justTickedSecond())
/*       */                 {
/*  4803 */                   toReturn = MethodsItems.eat(act, performer, target, counter);
/*       */                 }
/*       */               }
/*       */                }
/*  4807 */             else if (action == 328)
/*       */             
/*  4809 */             { if (target.isEgg())
/*       */               {
/*  4811 */                 comm.sendNormalServerMessage("You suck on the " + target
/*  4812 */                     .getName() + " a little. Weird feeling.");
/*  4813 */                 Server.getInstance().broadCastAction(performer
/*  4814 */                     .getName() + " sucks on " + target.getNameWithGenus() + ". A strange feeling runs through you.", performer, 3);
/*       */               }
/*       */                }
/*       */             
/*  4818 */             else if (action == 330)
/*       */             
/*  4820 */             { toReturn = true;
/*  4821 */               if (target.isEgg())
/*       */               {
/*  4823 */                 if (target.getTemplateId() == 465)
/*       */                 {
/*  4825 */                   if (target.hatching) {
/*  4826 */                     comm.sendNormalServerMessage("The " + target
/*  4827 */                         .getName() + " is already hatching.");
/*  4828 */                   } else if (target.getDamage() > 80.0F || performer.getPower() >= 5) {
/*       */                     
/*  4830 */                     comm.sendNormalServerMessage("You make a small hole in the " + target
/*  4831 */                         .getName() + ".");
/*  4832 */                     Server.getInstance().broadCastAction(performer
/*  4833 */                         .getName() + " makes a small hole in " + target.getNameWithGenus() + "!", performer, 20);
/*       */                     
/*  4835 */                     target.hatching = true;
/*       */                   } else {
/*       */                     
/*  4838 */                     comm.sendNormalServerMessage("The shell of the " + target
/*  4839 */                         .getName() + " is too hard still.");
/*       */                   } 
/*       */                 }
/*       */               } }
/*  4843 */             else if (action == 325)
/*  4844 */             { toReturn = MethodsItems.askSleep(act, performer, target, counter); }
/*  4845 */             else if (action == 140)
/*  4846 */             { toReturn = MethodsItems.sleep(act, performer, target, counter); }
/*  4847 */             else if (action == 365 || action == 366 || action == 367 || action == 320 || action == 319 || action == 322 || action == 321 || action == 323)
/*       */             
/*       */             { 
/*  4850 */               toReturn = MethodsItems.setRent(act, performer, target); }
/*  4851 */             else if (action == 324)
/*       */             
/*  4853 */             { toReturn = false;
/*  4854 */               if (target.getData() != performer.getWurmId())
/*  4855 */                 toReturn = MethodsItems.rent(act, performer, target); 
/*  4856 */               if (!toReturn) {
/*  4857 */                 toReturn = MethodsItems.askSleep(act, performer, target, counter);
/*       */               } }
/*  4859 */             else if (action == 183)
/*       */             
/*  4861 */             { if (ttid == 1101)
/*       */               {
/*  4863 */                 return MethodsItems.drinkChampagne(performer, target);
/*       */               }
/*  4865 */               if (target.isContainerLiquid() && target.getItemCount() == 1)
/*       */               {
/*  4867 */                 for (Item i : target.getItems()) {
/*       */                   
/*  4869 */                   if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable()) {
/*       */                     
/*  4871 */                     toReturn = false;
/*  4872 */                     if (act.justTickedSecond())
/*  4873 */                       toReturn = MethodsItems.drink(act, performer, i, counter); 
/*       */                     break;
/*       */                   } 
/*       */                 } 
/*       */               }
/*  4878 */               if (!target.isFood() && !target.isNoEatOrDrink() && !target.isUndistilled() && target.isDrinkable())
/*       */               {
/*  4880 */                 toReturn = false;
/*  4881 */                 if (act.justTickedSecond())
/*       */                 {
/*  4883 */                   toReturn = MethodsItems.drink(act, performer, target, counter);
/*       */                 }
/*       */               }
/*       */                }
/*  4887 */             else if (action == 600)
/*       */             
/*  4889 */             { if (target.isInstaDiscard()) {
/*       */                 
/*  4891 */                 toReturn = false;
/*  4892 */                 if (act.justTickedSecond()) {
/*  4893 */                   toReturn = Methods.discardSellItem(performer, act, target, counter);
/*       */                 }
/*       */               }  }
/*  4896 */             else if (action == 91)
/*       */             
/*  4898 */             { if (target.getTemplateId() == 442)
/*       */               {
/*  4900 */                 if (performer.getStatus().canEat()) {
/*       */                   
/*  4902 */                   String eatString = "You eat some Christmas ham and spare ribs in gravy with Jansson's frestelse topped with apple jam.";
/*  4903 */                   int food = Server.rand.nextInt(4);
/*  4904 */                   if (food == 0) {
/*  4905 */                     eatString = "You can't keep off the candy. You swallow all sorts of chocolate, marshmallows, marzipan pigs, and top it off with a fruit salad with whipped cream.";
/*  4906 */                   } else if (food == 1) {
/*  4907 */                     eatString = "The fish is delicious. Red salmon with cooked potatoes and mayonnaise. The pickled herring is particularly good.";
/*  4908 */                   } else if (food == 2) {
/*  4909 */                     eatString = "You serve yourself from the cold dishes. Roast beef, potato and mimosa salad. Various kinds of salami and sausage. Yum!";
/*       */                   } 
/*  4911 */                   comm.sendNormalServerMessage(eatString);
/*  4912 */                   Server.getInstance().broadCastAction(performer
/*  4913 */                       .getName() + " returns from the " + target.getName() + " with a loud burp.", performer, 5);
/*       */                   
/*  4915 */                   performer.getStatus().setMaxCCFP();
/*  4916 */                   performer.getStatus().refresh(0.99F, true);
/*       */                 } else {
/*       */                   
/*  4919 */                   comm.sendNormalServerMessage("You can't bring yourself to eat more right now.");
/*       */                 }  } 
/*  4921 */               toReturn = true; }
/*       */             
/*  4923 */             else if (action == 86)
/*       */             
/*  4925 */             { toReturn = true;
/*  4926 */               if (target.isFullprice()) {
/*  4927 */                 comm.sendNormalServerMessage("You cannot set the price of that object.");
/*       */               } else {
/*       */                 
/*  4930 */                 Methods.sendSinglePriceQuestion(performer, target);
/*       */               }
/*       */                }
/*  4933 */             else if (action == 87)
/*       */             
/*  4935 */             { toReturn = true;
/*  4936 */               if (target.isFullprice()) {
/*  4937 */                 comm.sendNormalServerMessage("Price is set to " + 
/*  4938 */                     Economy.getEconomy().getChangeFor(target.getValue()).getChangeString() + ".");
/*       */               
/*       */               }
/*  4941 */               else if (target.getPrice() <= 0) {
/*       */                 
/*  4943 */                 if (target.getValue() <= 1) {
/*  4944 */                   comm.sendNormalServerMessage("A trader would deem this pretty worthless.");
/*       */                 } else {
/*  4946 */                   comm.sendNormalServerMessage("A trader would sell this for about " + 
/*       */                       
/*  4948 */                       Economy.getEconomy().getChangeFor((target.getValue() / 2)).getChangeString() + ".");
/*       */                 } 
/*       */               } else {
/*  4951 */                 comm.sendNormalServerMessage("Price is set to " + 
/*  4952 */                     Economy.getEconomy().getChangeFor(target.getPrice()).getChangeString() + ".");
/*       */               } 
/*       */ 
/*       */               
/*  4956 */               if (MethodsReligion.canBeSacrificed(target)) {
/*       */                 
/*  4958 */                 Deity deity = performer.getDeity();
/*  4959 */                 float favorValue = MethodsReligion.getFavorValue(deity, target);
/*  4960 */                 float favorMod = MethodsReligion.getFavorModifier(deity, target);
/*  4961 */                 String deityName = "a deity";
/*  4962 */                 if (deity != null)
/*  4963 */                   deityName = deity.getName(); 
/*  4964 */                 int favorGain = (int)(favorValue * favorMod / 1000.0F);
/*  4965 */                 if (favorGain > 1.0F) {
/*  4966 */                   comm.sendNormalServerMessage(String.format("You think you can sacrifice this to %s for about %d favor.", new Object[] { deityName, 
/*  4967 */                           Integer.valueOf(favorGain) }));
/*       */                 } else {
/*  4969 */                   comm.sendNormalServerMessage(String.format("You think %s would not provide much favor for sacrificing this.", new Object[] { deityName }));
/*       */                 }
/*       */               
/*       */               }  }
/*  4973 */             else if (action == 133)
/*       */             
/*  4975 */             { if (Methods.isActionAllowed(performer, action)) {
/*  4976 */                 toReturn = MethodsItems.unstringBow(performer, target, act, counter);
/*       */ 
/*       */               
/*       */               }
/*       */               
/*       */                }
/*       */             
/*  4983 */             else if (action == 53)
/*       */             
/*  4985 */             { toReturn = true;
/*  4986 */               if (target.isFire() || target.isLight() || target.isForgeOrOven() || target
/*  4987 */                 .getTemplateId() == 1243 || target
/*  4988 */                 .getTemplateId() == 889 || target
/*  4989 */                 .getTemplateId() == 1023 || target
/*  4990 */                 .getTemplateId() == 1028 || target
/*  4991 */                 .getTemplateId() == 1178)
/*       */               {
/*  4993 */                 if (target.isOnFire()) {
/*       */                   
/*  4995 */                   if (target.getTemplateId() == 729) {
/*       */                     
/*  4997 */                     comm.sendNormalServerMessage("You blow out the " + target.getName() + ".");
/*  4998 */                     Server.getInstance().broadCastAction(performer
/*  4999 */                         .getName() + " blows out " + target.getNameWithGenus() + ".", performer, 5);
/*  5000 */                     if (target.isAlwaysLit()) {
/*  5001 */                       comm.sendNormalServerMessage("The candles quickly relight.");
/*       */                     } else {
/*  5003 */                       target.setTemperature((short)200);
/*       */                     } 
/*       */                   } else {
/*       */                     
/*  5007 */                     comm.sendNormalServerMessage("You snuff the " + target.getName() + ".");
/*  5008 */                     Server.getInstance().broadCastAction(performer
/*  5009 */                         .getName() + " snuffs " + target.getNameWithGenus() + ".", performer, 5);
/*  5010 */                     if (target.isAlwaysLit()) {
/*  5011 */                       comm.sendNormalServerMessage("Magically the " + target.getName() + " quickly relights.");
/*       */                     } else {
/*       */                       
/*  5014 */                       target.setTemperature((short)200);
/*  5015 */                       if (target.getTemplateId() == 889) {
/*  5016 */                         target.deleteFireEffect();
/*       */                       }
/*       */                     } 
/*       */                   } 
/*       */                 } else {
/*  5021 */                   comm.sendNormalServerMessage("The " + target.getName() + " is not burning.");
/*       */                 } 
/*       */               } }
/*  5024 */             else if (action == 719)
/*       */             
/*  5026 */             { byte kId = performer.getKingdomId();
/*  5027 */               if (performer.getPower() >= 2 && (target.getTemplateId() == 176 || target
/*  5028 */                 .getTemplateId() == 315))
/*       */               {
/*  5030 */                 KingdomMembersQuestion kmq = new KingdomMembersQuestion(performer, target.getWurmId(), (byte)0);
/*  5031 */                 kmq.sendQuestion();
/*       */               }
/*  5033 */               else if (Kingdoms.isCustomKingdom(kId) && King.isKing(performer.getWurmId(), kId))
/*       */               {
/*       */                 
/*  5036 */                 KingdomMembersQuestion kmq = new KingdomMembersQuestion(performer, target.getWurmId(), Kingdoms.getNameFor(kId), kId);
/*  5037 */                 kmq.sendQuestion();
/*       */               }
/*       */                }
/*  5040 */             else if (action == 89)
/*       */             
/*  5042 */             { toReturn = true;
/*  5043 */               if (performer.getPower() <= 0) {
/*       */                 
/*  5045 */                 MethodsCreatures.sendSetKingdomQuestion(performer, target);
/*       */               }
/*  5047 */               else if (WurmPermissions.mayUseDeityWand(performer)) {
/*  5048 */                 MethodsCreatures.sendSetKingdomQuestion(performer, target);
/*       */               }  }
/*  5050 */             else if (action == 467)
/*       */             
/*  5052 */             { toReturn = true;
/*  5053 */               if (performer.getPower() >= 2 && target.getOwnerId() == performer.getWurmId())
/*       */               {
/*  5055 */                 GmInterface gmi = new GmInterface(performer, target.getWurmId());
/*  5056 */                 gmi.sendQuestion();
/*       */               }
/*       */                }
/*  5059 */             else if (action == 535)
/*       */             
/*  5061 */             { toReturn = true;
/*  5062 */               if (performer.getPower() >= 4 && target.getOwnerId() == performer.getWurmId())
/*       */               {
/*  5064 */                 FeatureManagement fm = new FeatureManagement(performer, target.getWurmId());
/*  5065 */                 fm.sendQuestion();
/*       */               }
/*       */                }
/*  5068 */             else if (action == 609 && Servers.isThisLoginServer())
/*       */             
/*  5070 */             { toReturn = true;
/*  5071 */               if (performer.getPower() >= 4 && target.getOwnerId() == performer.getWurmId())
/*       */               {
/*  5073 */                 InGameVoteSetupQuestion igvsq = new InGameVoteSetupQuestion(performer);
/*  5074 */                 igvsq.sendQuestion();
/*       */               }
/*       */                }
/*  5077 */             else if (action == 635 && Servers.isThisLoginServer())
/*       */             
/*  5079 */             { toReturn = true;
/*  5080 */               if (performer.getPower() >= 4 && target.getOwnerId() == performer.getWurmId())
/*       */               {
/*  5082 */                 GroupCAHelpQuestion gchq = new GroupCAHelpQuestion(performer);
/*  5083 */                 gchq.sendQuestion();
/*       */               }
/*       */                }
/*  5086 */             else if (action == 353)
/*       */             
/*  5088 */             { if (Servers.localServer.isChallengeServer()) {
/*  5089 */                 toReturn = true;
/*  5090 */               } else if (ttid == 538) {
/*       */                 
/*  5092 */                 if (King.getKing((byte)2) != null) {
/*       */                   
/*  5094 */                   toReturn = true;
/*  5095 */                   comm.sendNormalServerMessage("The " + 
/*       */                       
/*  5097 */                       King.getRulerTitle(((King.getKing((byte)2)).gender == 0), (byte)2) + " is appointed already. The stone is empty.");
/*       */                   
/*  5099 */                   Methods.resetMolrStone();
/*       */                 }
/*       */                 else {
/*       */                   
/*  5103 */                   toReturn = Methods.aspireKing(performer, (byte)2, target, null, act, counter);
/*  5104 */                   if (toReturn) {
/*  5105 */                     Methods.resetMolrStone();
/*       */                   }
/*       */                 } 
/*       */               }  }
/*  5109 */             else if (action == 480)
/*       */             
/*  5111 */             { if (ttid == 682 && Servers.localServer.PVPSERVER)
/*       */               {
/*  5113 */                 KingdomFoundationQuestion kfq = new KingdomFoundationQuestion(performer, target.getWurmId());
/*  5114 */                 kfq.sendQuestion();
/*       */               }
/*       */                }
/*  5117 */             else if (action == 115 && target.getTemplateId() == 1024)
/*       */             
/*  5119 */             { ConchQuestion question = new ConchQuestion(performer, target.getWurmId());
/*  5120 */               question.sendQuestion();
/*  5121 */               performer.playPersonalSound("sound.fx.conch"); }
/*       */             
/*  5123 */             else if (action == 214)
/*       */             
/*  5125 */             { if (ttid == 652)
/*       */               {
/*  5127 */                 if (performer.isReallyPaying() || performer.getPower() >= 2) {
/*       */                   
/*  5129 */                   if (!performer.hasFlag(63)) {
/*       */                     
/*  5131 */                     if (WurmCalendar.isChristmas()) {
/*       */                       
/*  5133 */                       if (!performer.hasFlag(62)) {
/*       */                         
/*  5135 */                         if (performer.getKingdomTemplateId() == 3) {
/*       */                           
/*  5137 */                           comm.sendNormalServerMessage("Seems that you have been a bad person this year, " + performer
/*  5138 */                               .getName() + ". There is a gift for you beneath the tree!");
/*       */ 
/*       */ 
/*       */                         
/*       */                         }
/*  5143 */                         else if (performer.getReputation() < 0) {
/*  5144 */                           comm.sendNormalServerMessage("You have been a bad person this year, " + performer
/*  5145 */                               .getName() + ", but there is a gift for you anyways.");
/*       */                         } else {
/*       */                           
/*  5148 */                           comm.sendNormalServerMessage("You have been a good person this year, " + performer
/*  5149 */                               .getName() + ". Santa left a gift for you beneath the tree!");
/*       */                         } 
/*       */                         
/*  5152 */                         awardChristmasPresent(performer);
/*       */                       }
/*       */                       else {
/*       */                         
/*  5156 */                         comm.sendNormalServerMessage("There are no more gifts for you from Santa beneath the tree.");
/*       */                       } 
/*       */                     } else {
/*       */                       
/*  5160 */                       comm.sendNormalServerMessage("It is not christmas so there are no gifts from Santa beneath the tree.");
/*       */                     } 
/*       */                   } else {
/*       */                     
/*  5164 */                     comm.sendNormalServerMessage("You need to have paid for premium time to receive this year's gift.");
/*       */                   } 
/*       */                 } else {
/*       */                   
/*  5168 */                   comm.sendNormalServerMessage("You need to be a premium player to receive this year's gift.");
/*       */                 }
/*       */               
/*       */               } }
/*  5172 */             else if (action == 285 && !target.isSealedByPlayer() && (target
/*  5173 */               .isFoodMaker() || target.getTemplate().isCooker()))
/*       */             
/*  5175 */             { showRecipeInfo(performer, (Item)null, target); }
/*       */             
/*  5177 */             else if (action == 79)
/*  5178 */             { comm.sendNormalServerMessage("Nerd."); }
/*  5179 */             else if (action == 688)
/*       */             
/*  5181 */             { if (target.canHavePermissions() && target.mayManage(performer))
/*       */               {
/*  5183 */                 ManageObjectList.Type molt = ManageObjectList.Type.ITEM;
/*  5184 */                 if (target.isBed())
/*  5185 */                   molt = ManageObjectList.Type.BED; 
/*  5186 */                 if (target.getTemplateId() == 1271)
/*  5187 */                   molt = ManageObjectList.Type.MESSAGE_BOARD; 
/*  5188 */                 if (target.getTemplateId() == 272) {
/*  5189 */                   molt = ManageObjectList.Type.CORPSE;
/*       */                 }
/*  5191 */                 ManagePermissions mp = new ManagePermissions(performer, molt, (PermissionsPlayerList.ISettings)target, false, -10L, false, null, "");
/*       */                 
/*  5193 */                 mp.sendQuestion();
/*       */               }
/*       */                }
/*  5196 */             else if (action == 691)
/*       */             
/*  5198 */             { if (target.canHavePermissions() && target.maySeeHistory(performer))
/*       */               {
/*  5200 */                 PermissionsHistory ph = new PermissionsHistory(performer, target.getWurmId());
/*  5201 */                 ph.sendQuestion();
/*       */               }
/*       */                }
/*  5204 */             else if (action == 725)
/*  5205 */             { setVolume(performer, target, 12); }
/*  5206 */             else if (action == 726)
/*  5207 */             { setVolume(performer, target, 11); }
/*  5208 */             else if (action == 727)
/*  5209 */             { setVolume(performer, target, 10); }
/*  5210 */             else if (action == 728)
/*  5211 */             { setVolume(performer, target, 9); }
/*  5212 */             else if (action == 729)
/*  5213 */             { setVolume(performer, target, 8); }
/*  5214 */             else if (action == 730)
/*  5215 */             { setVolume(performer, target, 7); }
/*  5216 */             else if (action == 731)
/*  5217 */             { setVolume(performer, target, 6); }
/*  5218 */             else if (action == 732)
/*  5219 */             { setVolume(performer, target, 5); }
/*  5220 */             else if (action == 733)
/*  5221 */             { setVolume(performer, target, 4); }
/*  5222 */             else if (action == 734)
/*  5223 */             { setVolume(performer, target, 3); }
/*  5224 */             else if (action == 735)
/*  5225 */             { setVolume(performer, target, 2); }
/*  5226 */             else if (action == 736)
/*  5227 */             { setVolume(performer, target, 1); }
/*  5228 */             else if (action == 737)
/*  5229 */             { setVolume(performer, target, 0); }
/*  5230 */             else { if (action == 936)
/*  5231 */                 return makeBait(act, performer, target, action, counter); 
/*  5232 */               if (action == 937 && target.getTemplateId() == 479)
/*  5233 */                 return makeFloat(act, performer, target, action, counter);  }  }  } 
/*       */       } 
/*  5235 */     } else if (action == 87) {
/*       */       
/*  5237 */       toReturn = true;
/*  5238 */       if (target.isFullprice()) {
/*  5239 */         comm.sendNormalServerMessage("Price is set to " + 
/*  5240 */             Economy.getEconomy().getChangeFor(target.getValue()).getChangeString() + ".");
/*       */       
/*       */       }
/*  5243 */       else if (target.getPrice() <= 1) {
/*       */         
/*  5245 */         if (target.getValue() <= 1) {
/*  5246 */           comm.sendNormalServerMessage("A trader would deem this pretty worthless.");
/*       */         } else {
/*  5248 */           comm.sendNormalServerMessage("A trader would sell this for about " + 
/*       */               
/*  5250 */               Economy.getEconomy().getChangeFor((target.getValue() / 2)).getChangeString() + ".");
/*       */         } 
/*       */       } else {
/*  5253 */         comm.sendNormalServerMessage("Price is set to " + 
/*  5254 */             Economy.getEconomy().getChangeFor(target.getPrice()).getChangeString() + ".");
/*       */       } 
/*       */     } 
/*       */     
/*  5258 */     if (action == 909) {
/*       */ 
/*       */       
/*  5261 */       Creature[] storedanimal = Creatures.getInstance().getCreatures();
/*  5262 */       for (Creature cret : storedanimal) {
/*       */         
/*  5264 */         if (cret.getWurmId() == target.getData()) {
/*       */ 
/*       */           
/*  5267 */           Brand brand = Creatures.getInstance().getBrand(target.getData());
/*  5268 */           if (brand != null) {
/*       */             
/*       */             try {
/*       */               
/*  5272 */               Village v = Villages.getVillage((int)brand.getBrandId());
/*  5273 */               performer.getCommunicator().sendNormalServerMessage("It has been branded by and belongs to the settlement of " + v
/*  5274 */                   .getName() + ".");
/*       */             }
/*  5276 */             catch (NoSuchVillageException nsv) {
/*       */               
/*  5278 */               brand.deleteBrand();
/*       */             } 
/*       */           }
/*       */           
/*  5282 */           if (cret.isCaredFor()) {
/*       */             
/*  5284 */             long careTaker = cret.getCareTakerId();
/*  5285 */             PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(careTaker);
/*  5286 */             if (info != null)
/*       */             {
/*  5288 */               performer.getCommunicator().sendNormalServerMessage("It is being taken care of by " + info
/*  5289 */                   .getName() + ".");
/*       */             }
/*       */           } 
/*       */           
/*  5293 */           performer.getCommunicator().sendNormalServerMessage(StringUtilities.raiseFirstLetter(cret
/*  5294 */                 .getStatus().getBodyType()));
/*       */           
/*  5296 */           if (cret.isDomestic())
/*       */           {
/*  5298 */             if (System.currentTimeMillis() - cret.getLastGroomed() > 172800000L)
/*       */             {
/*  5300 */               performer.getCommunicator().sendNormalServerMessage("This creature could use some grooming.");
/*       */             }
/*       */           }
/*       */ 
/*       */           
/*  5305 */           if (cret.hasTraits()) {
/*       */             try {
/*       */               double knowl;
/*       */               
/*  5309 */               Skill breeding = performer.getSkills().getSkill(10085);
/*       */               
/*  5311 */               if (performer.getPower() > 0) {
/*       */                 
/*  5313 */                 knowl = 99.99D;
/*       */               }
/*       */               else {
/*       */                 
/*  5317 */                 knowl = breeding.getKnowledge(0.0D);
/*       */               } 
/*  5319 */               if (knowl > 20.0D) {
/*       */                 
/*  5321 */                 StringBuilder buf = new StringBuilder();
/*  5322 */                 for (int x = 0; x < 64; x++) {
/*       */                   
/*  5324 */                   if (cret.hasTrait(x) && knowl - 20.0D > x) {
/*       */                     
/*  5326 */                     String l = Traits.getTraitString(x);
/*  5327 */                     if (l.length() > 0) {
/*       */                       
/*  5329 */                       buf.append(l);
/*  5330 */                       buf.append(' ');
/*       */                     } 
/*       */                   } 
/*       */                 } 
/*  5334 */                 if (buf.toString().length() > 0) {
/*  5335 */                   performer.getCommunicator().sendNormalServerMessage(buf.toString());
/*       */                 }
/*       */               } 
/*  5338 */             } catch (NoSuchSkillException noSuchSkillException) {}
/*       */           }
/*       */         } 
/*       */       } 
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  5347 */     if (action == 185) {
/*       */       
/*  5349 */       toReturn = true;
/*  5350 */       if (performer.getPower() >= 0)
/*       */       {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  5359 */         comm.sendNormalServerMessage("It is made from " + 
/*  5360 */             MaterialUtilities.getMaterialString(target.getMaterial()) + " (" + target
/*  5361 */             .getMaterial() + ") " + ".");
/*  5362 */         comm.sendNormalServerMessage("WurmId:" + target
/*  5363 */             .getWurmId() + ", posx=" + target.getPosX() + "(" + ((int)target.getPosX() >> 2) + "), posy=" + target
/*  5364 */             .getPosY() + "(" + ((int)target.getPosY() >> 2) + "), posz=" + target
/*  5365 */             .getPosZ() + ", rot" + target.getRotation() + " layer=" + (
/*  5366 */             target.isOnSurface() ? 0 : -1) + " bridgeid=" + target.getBridgeId());
/*  5367 */         comm.sendNormalServerMessage("Ql:" + target
/*  5368 */             .getQualityLevel() + ", damage=" + target.getDamage() + ", weight=" + target
/*  5369 */             .getWeightGrams() + ", temp=" + target.getTemperature());
/*  5370 */         comm.sendNormalServerMessage("parentid=" + target
/*  5371 */             .getParentId() + " ownerid=" + target.getOwnerId() + " zoneid=" + target
/*  5372 */             .getZoneId() + " sizex=" + target.getSizeX() + ", sizey=" + target.getSizeY() + " sizez=" + target
/*  5373 */             .getSizeZ() + ".");
/*       */         
/*  5375 */         long timeSince = WurmCalendar.currentTime - target.getLastMaintained();
/*       */         
/*  5377 */         String timeString = Server.getTimeFor(timeSince * 1000L);
/*  5378 */         comm.sendNormalServerMessage("Last maintained " + timeString + " ago.");
/*  5379 */         String lastOwnerS = String.valueOf(target.lastOwner);
/*       */         
/*  5381 */         PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(target.getLastOwnerId());
/*  5382 */         if (p != null) {
/*  5383 */           lastOwnerS = p.getName();
/*       */         } else {
/*       */ 
/*       */           
/*       */           try {
/*  5388 */             Creature c = Creatures.getInstance().getCreature(target.lastOwner);
/*  5389 */             lastOwnerS = c.getName();
/*       */           }
/*  5391 */           catch (NoSuchCreatureException nsc) {
/*       */             
/*  5393 */             lastOwnerS = "dead " + lastOwnerS;
/*       */           } 
/*       */         } 
/*       */         
/*  5397 */         comm.sendNormalServerMessage("lastownerid=" + lastOwnerS + ", Model=" + target
/*  5398 */             .getModelName());
/*  5399 */         if (performer.getPower() >= 5) {
/*       */           
/*  5401 */           comm.sendNormalServerMessage("Zoneid=" + target
/*  5402 */               .getZoneId() + " real zid=" + target.zoneId + " Counter=" + 
/*  5403 */               WurmId.getNumber(target.getWurmId()) + " origin=" + WurmId.getOrigin(target.getWurmId()) + " fl=" + target
/*  5404 */               .getFloorLevel() + " your fl=" + performer.getFloorLevel() + " bridge=" + target
/*  5405 */               .getBridgeId());
/*  5406 */           if (target.isVehicle()) {
/*       */             
/*  5408 */             float diffposx = target.getPosX() - performer.getPosX();
/*  5409 */             float diffposy = target.getPosY() - performer.getPosY();
/*  5410 */             comm.sendNormalServerMessage("Relative: offx=" + diffposx + ", offy=" + diffposy + ", offz=" + performer
/*  5411 */                 .getPositionZ() + " altOffZ=" + performer
/*  5412 */                 .getAltOffZ());
/*       */           } 
/*       */         } 
/*  5415 */         if (target.hasData())
/*       */         {
/*  5417 */           comm.sendNormalServerMessage("data=" + target
/*  5418 */               .getData() + ", data1=" + target.getData1() + " data2=" + target
/*  5419 */               .getData2());
/*       */         }
/*       */         
/*  5422 */         String creator = ", creator=" + target.creator;
/*       */         
/*  5424 */         if (target.creator == null || target.creator.isEmpty())
/*       */         {
/*  5426 */           creator = "";
/*       */         }
/*       */         
/*  5429 */         comm.sendNormalServerMessage("auxdata=" + target.getAuxData() + creator);
/*       */         
/*  5431 */         if (target.isKey())
/*       */         {
/*  5433 */           comm.sendNormalServerMessage("lock id=" + target.getLockId());
/*       */         }
/*       */         
/*  5436 */         if (target.isLock()) {
/*       */           
/*  5438 */           long[] keys = target.getKeyIds();
/*  5439 */           comm.sendNormalServerMessage("Keys:");
/*  5440 */           for (long lKey : keys) {
/*  5441 */             comm.sendNormalServerMessage(String.valueOf(lKey));
/*       */           }
/*       */         } 
/*  5444 */         if (target.getTemplateId() == 1311)
/*       */         {
/*  5446 */           comm.sendNormalServerMessage("Loader / Unloader WurmID = " + target
/*  5447 */               .getLastOwnerId() + ", Name = " + lastOwnerS);
/*       */         }
/*       */       }
/*       */     
/*  5451 */     } else if (action == 355) {
/*       */       
/*  5453 */       if (target.isRoyal())
/*       */       {
/*  5455 */         if (ttid == 536 || ttid == 530 || ttid == 533)
/*       */         {
/*       */           
/*  5458 */           KingdomStatusQuestion kq = new KingdomStatusQuestion(performer, "Kingdom status", "Kingdoms", performer.getWurmId());
/*  5459 */           kq.sendQuestion();
/*       */         }
/*       */       
/*       */       }
/*  5463 */     } else if (action == 356) {
/*       */       
/*  5465 */       if (target.isRoyal())
/*       */       {
/*  5467 */         if (ttid == 536 || ttid == 530 || ttid == 533)
/*       */         {
/*       */           
/*  5470 */           KingdomHistory kq = new KingdomHistory(performer, "Kingdom history", "History of the kingdoms", performer.getWurmId());
/*  5471 */           kq.sendQuestion();
/*       */         }
/*       */       
/*       */       }
/*  5475 */     } else if (action == 358) {
/*       */       
/*  5477 */       if (target.isRoyal())
/*       */       {
/*  5479 */         if (ttid == 536 || ttid == 530 || ttid == 533)
/*       */         {
/*       */           
/*  5482 */           AbdicationQuestion kq = new AbdicationQuestion(performer, "Abdication", "Do you want to abdicate?", performer.getWurmId());
/*  5483 */           kq.sendQuestion();
/*       */         }
/*       */       
/*       */       }
/*  5487 */     } else if (action == 487) {
/*       */       
/*  5489 */       if (ttid == 726)
/*       */       {
/*  5491 */         if (performer.getPower() == 0 || Servers.localServer.testServer)
/*       */         {
/*  5493 */           if (performer.getKingdomId() == target.getAuxData() || Servers.localServer.testServer)
/*       */           {
/*  5495 */             if (System.currentTimeMillis() - (((Player)performer).getSaveFile()).lastChangedKindom > 7257600000L || Servers.localServer.testServer) {
/*       */ 
/*       */               
/*  5498 */               King k = King.getKing(target.getAuxData());
/*  5499 */               if (k != null)
/*       */               {
/*  5501 */                 if (k.hasFailedAllChallenges())
/*       */                 {
/*  5503 */                   if (!performer.hasVotedKing()) {
/*       */                     
/*  5505 */                     performer.setVotedKing(true);
/*  5506 */                     comm.sendNormalServerMessage("You vote to remove " + k
/*  5507 */                         .getFullTitle() + ".");
/*  5508 */                     if (k.getVotesNeeded() == 0)
/*       */                     {
/*  5510 */                       k.removeByVote();
/*       */                     }
/*       */                   } else {
/*       */                     
/*  5514 */                     comm.sendNormalServerMessage("You have already voted to remove " + k
/*  5515 */                         .getFullTitle() + ".");
/*       */                   } 
/*       */                 }
/*       */               }
/*       */             } else {
/*  5520 */               comm.sendNormalServerMessage("You have have not been part of this kingdom long enough.");
/*       */             }
/*       */           
/*       */           }
/*       */         }
/*       */       }
/*  5526 */     } else if (action == 488) {
/*       */       
/*  5528 */       if (ttid == 726)
/*       */       {
/*  5530 */         if (performer.getKingdomId() == target.getAuxData() || Servers.localServer.testServer)
/*       */         {
/*  5532 */           if (performer.getPower() == 0 || Servers.localServer.testServer)
/*       */           {
/*  5534 */             if (System.currentTimeMillis() - (((Player)performer).getSaveFile()).lastChangedKindom > 7257600000L || Servers.localServer.testServer)
/*       */             {
/*       */               
/*  5537 */               King k = King.getKing(target.getAuxData());
/*  5538 */               if (k != null)
/*       */               {
/*  5540 */                 if (k.mayBeChallenged())
/*       */                 {
/*  5542 */                   boolean alreadyChallenged = k.hasBeenChallenged();
/*  5543 */                   k.addChallenge(performer);
/*  5544 */                   comm.sendNormalServerMessage("You vote to challenge " + k
/*  5545 */                       .getFullTitle() + ".");
/*  5546 */                   if (performer.getPower() > 0);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                   
/*  5552 */                   if (!alreadyChallenged && k.hasBeenChallenged()) {
/*       */                     
/*       */                     try {
/*       */                       
/*  5556 */                       Player ck = Players.getInstance().getPlayer(k.kingid);
/*  5557 */                       MethodsCreatures.sendChallengeKingQuestion((Creature)ck);
/*       */                     }
/*  5559 */                     catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */                   
/*       */                   }
/*       */                 }
/*       */               
/*       */               }
/*       */             }
/*       */             else
/*       */             {
/*  5568 */               comm.sendNormalServerMessage("You have have not been part of this kingdom long enough.");
/*       */             }
/*       */           
/*       */           }
/*       */         }
/*       */       }
/*  5574 */     } else if (action == 88) {
/*       */       
/*  5576 */       toReturn = true;
/*  5577 */       if (performer.getPower() >= 2)
/*       */       {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  5590 */         Methods.sendSetDataQuestion(performer, target);
/*       */       }
/*       */       else
/*       */       {
/*  5594 */         logger.log(Level.WARNING, performer.getName() + " hacking the protocol by trying to set the data of " + target + ", counter: " + counter + '!');
/*       */       }
/*       */     
/*  5597 */     } else if (action == 684) {
/*       */       
/*  5599 */       toReturn = true;
/*  5600 */       if (performer.getPower() >= 2) {
/*       */         
/*  5602 */         Methods.sendItemRestrictionManagement(performer, (Permissions.IAllow)target, target.getWurmId());
/*       */       } else {
/*       */         
/*  5605 */         logger.log(Level.WARNING, performer.getName() + " hacking the protocol by trying to set the restrictions of " + target + ", counter: " + counter + '!');
/*       */       }
/*       */     
/*       */     }
/*  5609 */     else if (action == 608) {
/*       */       
/*  5611 */       toReturn = true;
/*  5612 */       if (performer.getDeity() != null && (performer
/*  5613 */         .getDeity().getName().equals("Nahjo") || Servers.isThisATestServer()))
/*       */       {
/*  5615 */         if (target.getTemplateId() == performer.getDeity().getHolyItem())
/*       */         {
/*  5617 */           SwapDeityQuestion sdq = new SwapDeityQuestion(performer);
/*  5618 */           sdq.sendQuestion();
/*       */         }
/*       */       
/*       */       }
/*  5622 */     } else if (action == 698 || action == 699 || action == 700) {
/*       */       
/*  5624 */       toReturn = true;
/*  5625 */       LCMManagementQuestion question = new LCMManagementQuestion(performer, "Info", "Take an action", performer.getWurmId(), action);
/*  5626 */       question.sendQuestion();
/*       */     }
/*  5628 */     else if (action == 939) {
/*       */       
/*  5630 */       if (target.getTemplateId() == 94 || target
/*  5631 */         .getTemplateId() == 152 || target
/*  5632 */         .getTemplateId() == 780 || target
/*  5633 */         .getTemplateId() == 95 || target
/*  5634 */         .getTemplateId() == 150 || target
/*  5635 */         .getTemplateId() == 96 || target
/*  5636 */         .getTemplateId() == 151)
/*       */       {
/*  5638 */         convertFishingEquipment(performer, target);
/*       */       }
/*  5640 */       toReturn = true;
/*       */     } 
/*  5642 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean action(Action act, Creature performer, Item[] targets, short action, float counter) {
/*  5653 */     boolean toReturn = true;
/*  5654 */     if (action == 7 || action == 638) {
/*       */ 
/*       */ 
/*       */       
/*  5658 */       boolean dropOnGround = (action == 7);
/*  5659 */       String pre = "";
/*  5660 */       String post = "";
/*  5661 */       String broadcastPost = "";
/*       */       
/*  5663 */       NameCountList dropping = new NameCountList();
/*  5664 */       for (int x = 0; x < targets.length; x++) {
/*       */         
/*  5666 */         if (targets[x].isSurfaceOnly() && !performer.isOnSurface()) {
/*       */           
/*  5668 */           performer.getCommunicator().sendNormalServerMessage(targets[x].getName() + " can only be dropped on the surface.");
/*       */         }
/*  5670 */         else if ((!targets[x].isNoDrop() || performer.getPower() > 0) && !targets[x].isComponentItem()) {
/*       */           
/*  5672 */           String[] msg = MethodsItems.drop(performer, targets[x], dropOnGround);
/*  5673 */           if (msg.length > 0) {
/*       */             
/*  5675 */             if (pre.isEmpty())
/*  5676 */               pre = msg[0]; 
/*  5677 */             if (post.isEmpty())
/*  5678 */               post = msg[2]; 
/*  5679 */             if (broadcastPost.isEmpty()) {
/*  5680 */               broadcastPost = msg[3];
/*       */             }
/*  5682 */             dropping.add(targets[x].getName());
/*       */           } 
/*       */         } 
/*       */       } 
/*       */       
/*  5687 */       String line = dropping.toString();
/*  5688 */       if (!line.isEmpty())
/*       */       {
/*  5690 */         performer.getCommunicator().sendNormalServerMessage(pre + line + post);
/*  5691 */         Server.getInstance().broadCastAction(performer.getName() + " drops " + line + post, performer, 7);
/*       */       }
/*       */     
/*  5694 */     } else if (action == 59) {
/*       */       
/*  5696 */       TextInputQuestion tiq = new TextInputQuestion(performer, "Setting description for multiple items", "Set the new descriptions:", targets);
/*       */       
/*  5698 */       tiq.sendQuestion();
/*       */     }
/*  5700 */     else if (action == 86) {
/*       */       
/*  5702 */       SinglePriceManageQuestion spm = new SinglePriceManageQuestion(performer, "Price management for multiple items", "Set the desired price:", targets);
/*       */ 
/*       */       
/*  5705 */       spm.sendQuestion();
/*       */     } 
/*  5707 */     return true;
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
/*       */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  5719 */     if (target.canBePlanted()) {
/*       */       
/*  5721 */       if (action == 177 || action == 176 || action == 178)
/*  5722 */         return action(act, performer, target, action, counter); 
/*  5723 */       if (action == 6 || action == 100)
/*  5724 */         return action(act, performer, target, action, counter); 
/*  5725 */       if (action == 1)
/*       */       {
/*  5727 */         return examine(act, performer, target, action, counter);
/*       */       }
/*  5729 */       if (Features.Feature.SINGLE_PLAYER_BRIDGES.isEnabled() && action == 637 && source
/*  5730 */         .getTemplateId() == 903 && target
/*  5731 */         .getTemplateId() == 901)
/*       */       {
/*  5733 */         return MethodsSurveying.planBridge(act, performer, source, null, target, action, counter);
/*       */       }
/*  5735 */       if (Features.Feature.SINGLE_PLAYER_BRIDGES.isEnabled() && action == 640 && source
/*  5736 */         .getTemplateId() == 903 && target
/*  5737 */         .getTemplateId() == 901)
/*       */       {
/*  5739 */         return MethodsSurveying.survey(act, performer, source, null, target, action, counter);
/*       */       }
/*  5741 */       if (action == 640 && (source
/*  5742 */         .getTemplateId() == 805 || source
/*  5743 */         .getTemplateId() == 1009)) {
/*       */         
/*  5745 */         if (MethodsItems.surveyAbilitySigns(source, performer)) {
/*       */           
/*  5747 */           performer.getCommunicator().sendNormalServerMessage("You may activate this item here.");
/*       */         }
/*       */         else {
/*       */           
/*  5751 */           performer.getCommunicator().sendNormalServerMessage("You cannot activate this item here.");
/*       */         } 
/*  5753 */         return true;
/*       */       } 
/*       */     } 
/*  5756 */     boolean done = true;
/*  5757 */     int ttid = target.getTemplateId();
/*  5758 */     int stid = source.getTemplateId();
/*  5759 */     if (!target.isTraded())
/*       */     
/*  5761 */     { if (action > 10000)
/*       */       
/*  5763 */       { int itemToCreate = action - 10000;
/*  5764 */         boolean actionResult = false;
/*       */         
/*       */         try {
/*  5767 */           if (target.isNoTake(performer) || target.isUseOnGroundOnly())
/*       */           {
/*  5769 */             if (target.getWurmId() != performer.getVehicle() && !performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*       */               
/*  5771 */               performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/*  5772 */               return done;
/*       */             } 
/*       */           }
/*       */           
/*  5776 */           CreationEntry creation = CreationMatrix.getInstance().getCreationEntry(stid, ttid, itemToCreate);
/*  5777 */           if (source.getWurmId() == target.getWurmId()) {
/*       */             
/*  5779 */             performer.getCommunicator().sendNormalServerMessage("The same item can not be used twice during creation.");
/*       */             
/*  5781 */             return true;
/*       */           } 
/*  5783 */           done = false;
/*  5784 */           Item created = creation.run(performer, source, target.getWurmId(), counter);
/*  5785 */           actionResult = true;
/*       */ 
/*       */           
/*  5788 */           if (created.getTemplateId() == 1293 && created.getMaterial() == 56) {
/*       */             
/*  5790 */             int c = WurmColor.createColor(1 + Server.rand
/*  5791 */                 .nextInt(255), 1 + Server.rand
/*  5792 */                 .nextInt(255), 1 + Server.rand
/*  5793 */                 .nextInt(255));
/*  5794 */             created.setColor(c);
/*       */           } 
/*       */           
/*  5797 */           if (created.isLock()) {
/*       */             
/*       */             try {
/*       */               
/*  5801 */               if (created.getTemplateId() != 167)
/*       */               {
/*  5803 */                 Item key = ItemFactory.createItem(168, created.getQualityLevel(), performer
/*  5804 */                     .getName());
/*  5805 */                 key.setMaterial(created.getMaterial());
/*  5806 */                 performer.getInventory().insertItem(key, true);
/*  5807 */                 created.addKey(key.getWurmId());
/*  5808 */                 key.setLockId(created.getWurmId());
/*       */               }
/*       */             
/*  5811 */             } catch (NoSuchTemplateException nst) {
/*       */               
/*  5813 */               logger.log(Level.WARNING, performer.getName() + " failed to create key: " + nst.getMessage(), (Throwable)nst);
/*       */             }
/*  5815 */             catch (FailedException fe) {
/*       */               
/*  5817 */               logger.log(Level.WARNING, performer.getName() + " failed to create key: " + fe.getMessage(), (Throwable)fe);
/*       */             } 
/*       */           }
/*  5820 */           if (created.isDrinkable() || created.isFood())
/*       */           {
/*  5822 */             if (performer.isRoyalChef())
/*       */             {
/*  5824 */               created.setQualityLevel(Math.min(99.0F, created.getQualityLevel() + 10.0F));
/*       */             }
/*       */           }
/*       */           
/*  5828 */           if (!created.isLiquid() && created.getTemplateId() != 1269)
/*       */           {
/*  5830 */             if (!creation.isCreateOnGround() && performer.getInventory().insertItem(created)) {
/*       */               
/*  5832 */               performer.getCommunicator().sendNormalServerMessage("You create " + created
/*  5833 */                   .getNameWithGenus() + ".");
/*  5834 */               Server.getInstance().broadCastAction(performer
/*  5835 */                   .getName() + " creates " + created.getNameWithGenus() + ".", performer, 5);
/*  5836 */               performer.getCommunicator().sendActionResult(true);
/*       */             }
/*       */             else {
/*       */               
/*  5840 */               created.setLastOwnerId(performer.getWurmId());
/*       */               try {
/*       */                 int placedTile;
/*  5843 */                 float posX = performer.getStatus().getPositionX();
/*  5844 */                 float posY = performer.getStatus().getPositionY();
/*  5845 */                 float rot = performer.getStatus().getRotation();
/*       */                 
/*  5847 */                 float xPosMod = (float)Math.sin((rot * 0.017453292F)) * 2.0F;
/*  5848 */                 float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * 2.0F;
/*  5849 */                 posX += xPosMod;
/*  5850 */                 posY += yPosMod;
/*       */                 
/*  5852 */                 int placedX = (int)posX >> 2;
/*  5853 */                 int placedY = (int)posY >> 2;
/*       */                 
/*  5855 */                 if (performer.isOnSurface()) {
/*  5856 */                   placedTile = Server.surfaceMesh.getTile(placedX, placedY);
/*       */                 } else {
/*  5858 */                   placedTile = Server.caveMesh.getTile(placedX, placedY);
/*  5859 */                 }  if (Tiles.decodeHeight(placedTile) < 0) {
/*       */                   
/*  5861 */                   if (created.getTemplateId() == 37)
/*       */                   {
/*  5863 */                     performer.getCommunicator().sendNormalServerMessage("The fire fizzles in the water and goes out.");
/*       */                     
/*  5865 */                     Items.decay(created.getWurmId(), created.getDbStrings());
/*       */                   }
/*       */                   else
/*       */                   {
/*  5869 */                     created.putItemInfrontof(performer);
/*  5870 */                     performer.getCommunicator().sendNormalServerMessage("You create " + created
/*  5871 */                         .getNameWithGenus() + " in front of you on the ground.");
/*  5872 */                     Server.getInstance().broadCastAction(performer
/*  5873 */                         .getName() + " creates " + created.getNameWithGenus() + ".", performer, 
/*  5874 */                         Math.min(Math.max(3, created.getSizeZ() / 10), 10));
/*       */                   }
/*       */                 
/*       */                 } else {
/*       */                   
/*  5879 */                   created.putItemInfrontof(performer);
/*  5880 */                   performer.getCommunicator().sendNormalServerMessage("You create " + created
/*  5881 */                       .getNameWithGenus() + " in front of you on the ground.");
/*  5882 */                   Server.getInstance().broadCastAction(performer
/*  5883 */                       .getName() + " creates " + created.getNameWithGenus() + ".", performer, 
/*  5884 */                       Math.min(Math.max(3, created.getSizeZ() / 10), 10));
/*  5885 */                   if (created.getTemplateId() == 37 && !created.deleted)
/*       */                   {
/*  5887 */                     created.setTemperature((short)10000);
/*  5888 */                     Effect effect = EffectFactory.getInstance().createFire(created.getWurmId(), created
/*  5889 */                         .getPosX(), created.getPosY(), created.getPosZ(), performer
/*  5890 */                         .isOnSurface());
/*  5891 */                     created.addEffect(effect);
/*       */                   }
/*       */                 
/*       */                 } 
/*  5895 */               } catch (NoSuchZoneException nsz) {
/*       */                 
/*  5897 */                 logger.log(Level.WARNING, performer.getName() + ": " + nsz.getMessage(), (Throwable)nsz);
/*       */               }
/*  5899 */               catch (NoSuchPlayerException nsp) {
/*       */                 
/*  5901 */                 logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*       */               }
/*  5903 */               catch (NoSuchCreatureException nsc) {
/*       */                 
/*  5905 */                 logger.log(Level.INFO, nsc.getMessage(), (Throwable)nsc);
/*       */               } 
/*       */             } 
/*       */           }
/*  5909 */           done = true;
/*       */         }
/*  5911 */         catch (NoSuchEntryException nse) {
/*       */           
/*  5913 */           logger.log(Level.WARNING, performer.getName() + ":" + nse.getMessage(), (Throwable)nse);
/*  5914 */           done = true;
/*       */         }
/*  5916 */         catch (NoSuchSkillException nss) {
/*       */           
/*  5918 */           logger.log(Level.WARNING, performer.getName() + ":" + nss.getMessage(), (Throwable)nss);
/*  5919 */           done = true;
/*       */         }
/*  5921 */         catch (NoSuchItemException nsi) {
/*       */ 
/*       */           
/*  5924 */           done = true;
/*       */         }
/*  5926 */         catch (FailedException failedException) {}
/*       */ 
/*       */ 
/*       */         
/*  5930 */         if (done) {
/*  5931 */           performer.getCommunicator().sendActionResult(actionResult);
/*       */         } }
/*  5933 */       else if (Recipes.isRecipeAction(action))
/*       */       
/*  5935 */       { Recipe recipe = Recipes.getRecipeByActionId(action);
/*  5936 */         if (recipe == null) {
/*       */           
/*  5938 */           performer.getCommunicator().sendNormalServerMessage("Recipe" + (
/*  5939 */               (performer.getPower() > 1) ? (" " + action) : "") + " not found, most odd!");
/*  5940 */           return true;
/*       */         } 
/*       */ 
/*       */         
/*  5944 */         done = handleRecipe(act, performer, source, target, action, counter, recipe);
/*       */          }
/*       */       
/*  5947 */       else if (action == 17 && target.getTemplateId() == 1271)
/*       */       
/*  5949 */       { readVillageMessages(performer, target); }
/*       */       
/*  5951 */       else if (action == 118)
/*       */       
/*  5953 */       { if (target.getTemplateId() == 1271 && (source
/*  5954 */           .getTemplateId() == 748 || source.getTemplateId() == 1272))
/*       */         {
/*  5956 */           postVillageMessage(performer, source, target);
/*       */         }
/*  5958 */         else if (ttid == 700)
/*       */         {
/*  5960 */           done = true;
/*  5961 */           if (((source.isLight() || source.isFire()) && source.isOnFire()) || stid == 315 || stid == 176 || stid == 143) {
/*       */ 
/*       */             
/*  5964 */             if (target.getOwnerId() == -10L) {
/*       */               
/*  5966 */               SoundPlayer.playSound("sound.object.fzz", target, 1.0F);
/*  5967 */               short num = (short)(5 + Server.rand.nextInt(5));
/*  5968 */               Players.getInstance().sendEffect(num, target.getPosX(), target.getPosY(), target
/*  5969 */                   .getPosZ() + 10.0F + Server.rand.nextInt(30), performer.isOnSurface(), 500.0F);
/*  5970 */               target.setQualityLevel(target.getQualityLevel() - 1.0F);
/*  5971 */               if (target.getQualityLevel() > 0.0F)
/*  5972 */                 performer.getCommunicator().sendNormalServerMessage("The fireworks now has " + 
/*  5973 */                     Math.ceil(target.getQualityLevel()) + " charges left."); 
/*  5974 */               Server.getInstance().broadCastAction(performer.getName() + " fires off some fireworks!", performer, 5);
/*       */             } else {
/*       */               
/*  5977 */               performer.getCommunicator().sendNormalServerMessage("You need to light the fireworks on the ground.");
/*       */             } 
/*       */           } else {
/*       */             
/*  5981 */             performer.getCommunicator().sendNormalServerMessage("You can't light fireworks with that.");
/*       */           }
/*       */         
/*       */         }
/*       */         else
/*       */         {
/*  5987 */           done = action(act, performer, target, action, counter);
/*       */         }
/*       */          }
/*  5990 */       else if (action == 942)
/*       */       
/*  5992 */       { if (ttid == 1394 && (source
/*  5993 */           .isWeaponKnife() || source.getTemplateId() == 258))
/*       */         {
/*       */           
/*  5996 */           done = openClam(act, performer, source, target, action, counter);
/*       */         } }
/*       */       
/*  5999 */       else if (action == 945)
/*       */       
/*  6001 */       { if (source.getTemplate().isRune())
/*       */         {
/*  6003 */           done = useRuneOnItem(act, performer, source, target, action, counter);
/*       */         } }
/*       */       
/*  6006 */       else if (action == 162)
/*       */       
/*  6008 */       { done = action(act, performer, target, action, counter); }
/*       */       
/*  6010 */       else if (action == 671 || action == 672)
/*       */       
/*  6012 */       { if (source.getTemplateId() == 319 || source.getTemplateId() == 1029)
/*  6013 */           done = CargoTransportationMethods.haul(performer, target, counter, action, act);  }
/*       */       else
/*  6015 */       { if (action == 581 && stid == 176 && Servers.isThisATestServer()) {
/*       */           
/*  6017 */           decay(target, performer);
/*  6018 */           return true;
/*       */         } 
/*  6020 */         if (action == 83)
/*       */         
/*  6022 */         { if (target.getTemplateId() == 1112 && Items.isWaystoneInUse(target.getWurmId())) {
/*       */             
/*  6024 */             performer.getCommunicator().sendNormalServerMessage("That waystone is the anchor point for a container or a delivery point, thus may not be bashed.");
/*       */             
/*  6026 */             done = true;
/*       */           }
/*  6028 */           else if (target.isRoadMarker() && MethodsHighways.isNextToACamp(MethodsHighways.getHighwayPos(target))) {
/*       */             
/*  6030 */             performer.getCommunicator().sendNormalServerMessage("That " + target
/*  6031 */                 .getName() + " is in a wagoners camp, who does not allow such actions.");
/*  6032 */             done = true;
/*       */           }
/*  6034 */           else if (target.isRoadMarker() && Wagoner.isOnActiveDeliveryRoute(target)) {
/*       */             
/*  6036 */             performer.getCommunicator().sendNormalServerMessage("That " + target
/*  6037 */                 .getName() + " is on a wagoners route, you will have to wait till they have done that delivery.");
/*  6038 */             done = true;
/*       */           }
/*  6040 */           else if (target.getTemplateId() == 1311 && !target.isEmpty(true)) {
/*       */             
/*  6042 */             performer.getCommunicator().sendNormalServerMessage("You cannot destroy this cage with a creature inside.");
/*       */             
/*  6044 */             done = true;
/*       */           }
/*  6046 */           else if (performer.mayDestroy(target) && !target.isIndestructible()) {
/*  6047 */             done = MethodsItems.destroyItem(action, performer, source, target, false, counter);
/*       */           } else {
/*  6049 */             done = true;
/*       */           }  }
/*  6051 */         else { if (action == 185) {
/*       */ 
/*       */             
/*  6054 */             if (source.getTemplateId() == 176 && performer.getPower() > 0 && source
/*  6055 */               .getAuxData() == 1 && target.isVehicle()) {
/*       */               
/*  6057 */               Vehicle v = Vehicles.getVehicle(target);
/*       */               
/*  6059 */               String wPos = StringUtil.format("Vehicle TileX: %d TileY: %d FloorLevel: %d", new Object[] {
/*  6060 */                     Integer.valueOf(target.getTileX()), 
/*  6061 */                     Integer.valueOf(target.getTileY()), 
/*  6062 */                     Integer.valueOf(target.getFloorLevel()) });
/*  6063 */               performer.getCommunicator().sendNormalServerMessage(wPos);
/*  6064 */               if (v.getDraggers() != null) {
/*  6065 */                 for (Creature c : v.getDraggers()) {
/*       */                   
/*  6067 */                   String s = StringUtil.format("Dragger TileX: %d TileY: %d, id: %d FloorLevel: %d, PosZ: %d, StatusPosZ: %.2f", new Object[] {
/*       */                         
/*  6069 */                         Integer.valueOf(c.getTileX()), 
/*  6070 */                         Integer.valueOf(c.getTileY()), 
/*  6071 */                         Long.valueOf(c.getWurmId()), 
/*  6072 */                         Integer.valueOf(c.getFloorLevel()), 
/*  6073 */                         Integer.valueOf(c.getPosZDirts()), 
/*  6074 */                         Float.valueOf(c.getStatus().getPositionZ()) });
/*  6075 */                   performer.getCommunicator().sendNormalServerMessage(s);
/*       */                 } 
/*       */               }
/*  6078 */               Seat[] seats = v.getSeats();
/*  6079 */               if (seats != null)
/*       */               {
/*  6081 */                 for (int i = 0; i < seats.length; i++) {
/*       */                   
/*  6083 */                   Player p = Players.getInstance().getPlayerOrNull(seats[i].getOccupant());
/*  6084 */                   if (p != null) {
/*       */                     
/*  6086 */                     String s = StringUtil.format("Name: %s, TileX: %d TileY: %d, ZPos: %.2f, FloorLevel: %d", new Object[] { p
/*  6087 */                           .getName(), 
/*  6088 */                           Integer.valueOf(p.getTileX()), 
/*  6089 */                           Integer.valueOf(p.getTileX()), 
/*  6090 */                           Float.valueOf(p.getStatus().getPositionZ()), 
/*  6091 */                           Integer.valueOf(p.getFloorLevel(true)) });
/*  6092 */                     performer.getCommunicator().sendNormalServerMessage(s);
/*       */                   } 
/*       */                 } 
/*       */               }
/*  6096 */               return true;
/*       */             } 
/*       */             
/*  6099 */             return action(act, performer, target, action, counter);
/*       */           } 
/*  6101 */           if (action == 568)
/*       */           
/*  6103 */           { if (target.isLockable()) {
/*       */               
/*  6105 */               if (target.getLockId() == -10L || (target
/*  6106 */                 .isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target))) {
/*       */                 
/*  6108 */                 performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId());
/*       */               } else {
/*       */                 
/*       */                 try
/*       */                 {
/*       */                   
/*  6114 */                   Item lock = Items.getItem(target.getLockId());
/*       */                   
/*  6116 */                   if (!lock.getLocked() || target.isOwner(performer)) {
/*       */                     
/*  6118 */                     performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId());
/*  6119 */                     return true;
/*       */                   } 
/*       */ 
/*       */                   
/*  6123 */                   long[] keys = lock.getKeyIds();
/*  6124 */                   for (int i = 0; i < keys.length; i++) {
/*       */                     
/*  6126 */                     Item key = Items.getItem(keys[i]);
/*  6127 */                     if (key.getTopParent() == performer.getInventory().getWurmId()) {
/*       */                       
/*  6129 */                       performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId());
/*  6130 */                       return true;
/*       */                     } 
/*       */                   } 
/*       */                   
/*  6134 */                   performer.getCommunicator().sendSafeServerMessage("The " + target
/*  6135 */                       .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                 }
/*  6137 */                 catch (NoSuchItemException nsi)
/*       */                 {
/*  6139 */                   performer.getCommunicator().sendSafeServerMessage("The " + target
/*  6140 */                       .getName() + " is locked. Please use the key to unlock and open it.");
/*       */                 }
/*       */               
/*       */               }
/*       */             
/*       */             }
/*  6146 */             else if (target.isHollow() && !target.isSealedByPlayer() && target.getTemplateId() != 1342) {
/*  6147 */               performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId());
/*       */             } 
/*  6149 */             done = true; }
/*       */           
/*  6151 */           else if (action == 192)
/*       */           
/*  6153 */           { if (target.isNoImprove()) {
/*       */               
/*  6155 */               performer.getCommunicator().sendNormalServerMessage("That item cannot be improved");
/*  6156 */               return true;
/*       */             } 
/*  6158 */             if (target.getOwnerId() != -10L || Methods.isActionAllowed(performer, action, target)) {
/*       */               
/*  6160 */               if (target.creationState != 0 || source.creationState != 0) {
/*       */                 
/*  6162 */                 done = true;
/*  6163 */                 boolean targetWater = (target.getTemplateId() == 128);
/*  6164 */                 Item toImprove = !targetWater ? target : source;
/*  6165 */                 Item improveItem = targetWater ? target : source;
/*  6166 */                 byte material = MethodsItems.getImproveMaterial(toImprove);
/*  6167 */                 int tid = MethodsItems.getItemForImprovement(material, toImprove.creationState);
/*       */ 
/*       */                 
/*  6170 */                 if (tid == improveItem.getTemplateId()) {
/*       */ 
/*       */ 
/*       */                   
/*  6174 */                   if (tid == 128 && Materials.isMetal(material))
/*       */                   {
/*  6176 */                     done = MethodsItems.temper(act, performer, toImprove, improveItem, counter);
/*       */                   }
/*       */                   else
/*       */                   {
/*  6180 */                     done = MethodsItems.polishItem(act, performer, improveItem, toImprove, counter);
/*       */                   }
/*       */                 
/*  6183 */                 } else if (source.creationState != 0) {
/*       */                   
/*  6185 */                   tid = MethodsItems.getItemForImprovement(material, improveItem.creationState);
/*       */ 
/*       */ 
/*       */                   
/*  6189 */                   if (tid == toImprove.getTemplateId())
/*       */                   {
/*  6191 */                     if (tid == 128 && Materials.isMetal(improveItem.getMaterial())) {
/*       */ 
/*       */                       
/*  6194 */                       done = MethodsItems.temper(act, performer, improveItem, toImprove, counter);
/*       */                     
/*       */                     }
/*       */                     else {
/*       */                       
/*  6199 */                       done = MethodsItems.polishItem(act, performer, toImprove, improveItem, counter);
/*       */                     } 
/*       */                   }
/*       */                 } 
/*       */               } else {
/*       */                 
/*  6205 */                 done = MethodsItems.improveItem(act, performer, source, target, counter);
/*       */               } 
/*       */             } else {
/*  6208 */               return true;
/*       */             }  }
/*  6210 */           else if (action == 228)
/*       */           
/*  6212 */           { if (source.getTemplateId() == 386) {
/*       */               
/*  6214 */               done = true;
/*  6215 */               byte material = MethodsItems.getImproveMaterial(source);
/*  6216 */               int tid = MethodsItems.getItemForImprovement(material, source.creationState);
/*       */ 
/*       */               
/*  6219 */               if (tid == target.getTemplateId())
/*       */               {
/*  6221 */                 if (tid == 128 && Materials.isMetal(material)) {
/*  6222 */                   done = MethodsItems.temper(act, performer, source, target, counter);
/*       */                 } else {
/*       */                   
/*  6225 */                   done = MethodsItems.polishItem(act, performer, target, source, counter);
/*       */                 } 
/*       */               }
/*       */             }  }
/*  6229 */           else if (action == 93 && source
/*  6230 */             .getRarity() == target.getRarity())
/*       */           
/*  6232 */           { done = true;
/*  6233 */             if ((stid == 1193 || stid == 417) && ttid == stid && source
/*  6234 */               .getRealTemplateId() != target.getRealTemplateId()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  6244 */               int freeVol = source.getWeightGrams();
/*  6245 */               Item p = source.getParentOrNull();
/*  6246 */               if (source.getParentId() != target.getParentId()) {
/*       */                 
/*  6248 */                 if (p == null)
/*       */                 {
/*       */                   
/*  6251 */                   return true;
/*       */                 }
/*  6253 */                 freeVol = Math.min(freeVol, p.getFreeVolume());
/*       */               } 
/*  6255 */               if (freeVol > 0) {
/*       */ 
/*       */ 
/*       */                 
/*  6259 */                 float newQl = (target.getCurrentQualityLevel() * target.getWeightGrams() + source.getCurrentQualityLevel() * freeVol) / (target.getWeightGrams() + freeVol);
/*       */                 
/*  6261 */                 float caloriesTotal = target.getCaloriesByWeight() + source.getCaloriesByWeight(freeVol);
/*  6262 */                 float carbsTotal = target.getCarbsByWeight() + source.getCarbsByWeight(freeVol);
/*  6263 */                 float fatsTotal = target.getFatsByWeight() + source.getFatsByWeight(freeVol);
/*  6264 */                 float proteinsTotal = target.getProteinsByWeight() + source.getProteinsByWeight(freeVol);
/*  6265 */                 int weight = target.getWeightGrams() + freeVol;
/*  6266 */                 short calories = (short)(int)(caloriesTotal * 1000.0F / weight);
/*  6267 */                 short carbs = (short)(int)(carbsTotal * 1000.0F / weight);
/*  6268 */                 short fats = (short)(int)(fatsTotal * 1000.0F / weight);
/*  6269 */                 short proteins = (short)(int)(proteinsTotal * 1000.0F / weight);
/*  6270 */                 int ibonus = (target.getBonus() + source.getBonus()) / 2;
/*  6271 */                 byte bonus = (byte)(ibonus % SkillSystem.getNumberOfSkillTemplates());
/*       */                 
/*  6273 */                 byte stages = target.getFoodStages();
/*  6274 */                 byte ingredients = target.getFoodIngredients();
/*       */                 
/*  6276 */                 target.setRealTemplate(-10);
/*       */                 
/*  6278 */                 target.setWeight(target.getWeightGrams() + freeVol, true);
/*  6279 */                 target.setQualityLevel(newQl);
/*  6280 */                 target.setOriginalQualityLevel(newQl);
/*  6281 */                 target.setDamage(0.0F);
/*  6282 */                 ItemMealData.update(target.getWurmId(), (short)-1, calories, carbs, fats, proteins, bonus, stages, ingredients);
/*  6283 */                 target.setName(target.getTemplate().getName(), true);
/*       */                 
/*  6285 */                 source.setWeight(source.getWeightGrams() - freeVol, true);
/*       */               } else {
/*       */                 
/*  6288 */                 performer.getCommunicator().sendNormalServerMessage("No room in " + p.getName() + " to combine those.");
/*       */               } 
/*       */             } else {
/*       */ 
/*       */               
/*       */               try {
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*  6298 */                 performer.getMovementScheme().stopSendingSpeedModifier();
/*  6299 */                 if (performer.isPlayer())
/*  6300 */                   ((Player)performer).justCombined = true; 
/*  6301 */                 if (source.combine(target, performer)) {
/*  6302 */                   performer.getCommunicator().sendNormalServerMessage("You combine the items into one.");
/*       */                 }
/*  6304 */               } catch (FailedException fe) {
/*       */                 
/*  6306 */                 performer.getCommunicator().sendNormalServerMessage(fe.getMessage());
/*       */               }
/*       */             
/*       */             }  }
/*  6310 */           else if (action == 283)
/*       */           
/*  6312 */           { done = true;
/*  6313 */             if (source.isHealing() && target.isHealing()) {
/*       */               
/*  6315 */               if (source.getTemplateId() != target.getTemplateId())
/*       */               {
/*  6317 */                 return MethodsItems.createSalve(performer, source, target, act, counter);
/*       */               }
/*       */               
/*  6320 */               performer.getCommunicator().sendNormalServerMessage("Nothing would happen if you 'mixed' these.");
/*       */             }
/*  6322 */             else if (source.isColorComponent() && target.isColor()) {
/*       */               
/*  6324 */               done = MethodsItems.improveColor(performer, source, target, act);
/*       */             }
/*  6326 */             else if (target.isColorComponent() && source.isColor()) {
/*       */               
/*  6328 */               done = MethodsItems.improveColor(performer, target, source, act);
/*       */             }
/*  6330 */             else if (source.getTemplateId() == 764 && target.getTemplateId() == 866) {
/*       */               
/*  6332 */               done = MethodsItems.createOil(performer, source, target, act, counter);
/*       */             }
/*  6334 */             else if (source.getTemplateId() == 866 && target.getTemplateId() == 764) {
/*       */               
/*  6336 */               done = MethodsItems.createOil(performer, target, source, act, counter);
/*       */             }
/*  6338 */             else if (Features.Feature.TRANSFORM_RESOURCE_TILES.isEnabled() && ttid == 1020 && source
/*  6339 */               .isLiquid()) {
/*       */               
/*  6341 */               byte potionAuxData = getTileTransmutationLiquidAuxData(source, target);
/*  6342 */               if (potionAuxData == 0) {
/*  6343 */                 performer.getCommunicator().sendNormalServerMessage("You cannot mix these.");
/*       */               } else {
/*  6345 */                 done = MethodsItems.createPotion(performer, source, target, act, counter, potionAuxData);
/*       */               } 
/*       */             } else {
/*  6348 */               performer.getCommunicator().sendNormalServerMessage("You cannot mix these.");
/*       */             }  }
/*  6350 */           else if (action == 633)
/*       */           
/*  6352 */           { done = true;
/*  6353 */             if (source.isSmearable()) {
/*  6354 */               done = MethodsItems.smear(performer, source, target, act, counter);
/*       */             } }
/*  6356 */           else if (action == 285)
/*       */           
/*  6358 */           { done = true;
/*  6359 */             if (source.isHealing() && target.isHealing())
/*       */             
/*  6361 */             { if (source.getTemplateId() != target.getTemplateId()) {
/*       */                 
/*       */                 try
/*       */                 {
/*  6365 */                   Skill alch = performer.getSkills().getSkill(10042);
/*  6366 */                   int knowl = (int)alch.getKnowledge(0.0D);
/*  6367 */                   int pow = source.getAlchemyType() * target.getAlchemyType();
/*  6368 */                   if (knowl < 10) {
/*  6369 */                     performer.getCommunicator().sendNormalServerMessage("You have no clue what would happen if you mixed these.");
/*       */                   }
/*  6371 */                   else if (knowl < 30) {
/*       */                     
/*  6373 */                     if (pow < 10) {
/*  6374 */                       performer.getCommunicator().sendNormalServerMessage("You would probably create a fairly weak healing cover.");
/*       */                     } else {
/*       */                       
/*  6377 */                       performer.getCommunicator().sendNormalServerMessage("You would probably create a fairly strong healing cover.");
/*       */                     
/*       */                     }
/*       */                   
/*       */                   }
/*  6382 */                   else if (pow < 10) {
/*  6383 */                     performer.getCommunicator().sendNormalServerMessage("You would probably create a fairly weak healing cover.");
/*       */                   }
/*  6385 */                   else if (pow < 20) {
/*  6386 */                     performer.getCommunicator().sendNormalServerMessage("You would probably create a fairly strong healing cover.");
/*       */                   }
/*  6388 */                   else if (pow < 25) {
/*  6389 */                     performer.getCommunicator().sendNormalServerMessage("You would probably create a very strong healing cover.");
/*       */                   } else {
/*       */                     
/*  6392 */                     performer.getCommunicator().sendNormalServerMessage("You would probably create an extremely strong healing cover.");
/*       */                   }
/*       */                 
/*       */                 }
/*  6396 */                 catch (NoSuchSkillException nss)
/*       */                 {
/*  6398 */                   performer.getCommunicator().sendNormalServerMessage("You have no clue what would happen if you mixed these.");
/*       */                 }
/*       */               
/*       */               } else {
/*       */                 
/*  6403 */                 performer.getCommunicator().sendNormalServerMessage("Nothing would happen if you 'mixed' these.");
/*       */               }  }
/*  6405 */             else if (source.getTemplateId() == 764 || target.getTemplateId() == 764)
/*       */             
/*  6407 */             { if (source.getTemplateId() == 866 || target.getTemplateId() == 866) {
/*       */ 
/*       */                 
/*       */                 try {
/*  6411 */                   Skill alch = performer.getSkills().getSkill(10042);
/*  6412 */                   int knowl = (int)alch.getKnowledge(0.0D);
/*  6413 */                   if (knowl < 10) {
/*  6414 */                     performer.getCommunicator().sendNormalServerMessage("You have no clue what would happen if you mixed these.");
/*       */                   }
/*  6416 */                   else if (knowl < 30) {
/*       */                     
/*  6418 */                     performer.getCommunicator().sendNormalServerMessage("You would create some sort of potion.");
/*       */                   
/*       */                   }
/*       */                   else {
/*       */                     
/*  6423 */                     Item blood = (target.getTemplateId() == 866) ? target : source;
/*       */                     
/*       */                     try {
/*  6426 */                       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(blood
/*  6427 */                           .getPotionTemplateIdForBlood());
/*  6428 */                       performer.getCommunicator().sendNormalServerMessage("You would create " + template
/*  6429 */                           .getNameWithGenus() + ".");
/*       */                     }
/*  6431 */                     catch (NoSuchTemplateException nsi) {
/*       */                       
/*  6433 */                       performer.getCommunicator().sendNormalServerMessage("You have no clue what would happen if you mixed these.");
/*       */                     }
/*       */                   
/*       */                   }
/*       */                 
/*  6438 */                 } catch (NoSuchSkillException nss) {
/*       */                   
/*  6440 */                   performer.getCommunicator().sendNormalServerMessage("You have no clue what would happen if you mixed these.");
/*       */                 }
/*       */               
/*       */               } else {
/*       */                 
/*  6445 */                 performer.getCommunicator().sendNormalServerMessage("You cannot mix these.");
/*       */               }  }
/*  6447 */             else if (Features.Feature.TRANSFORM_RESOURCE_TILES.isEnabled() && ttid == 1020 && source
/*  6448 */               .isLiquid())
/*       */             
/*  6450 */             { byte potionAuxData = getTileTransmutationLiquidAuxData(source, target);
/*  6451 */               switch (potionAuxData)
/*       */               
/*       */               { case 1:
/*  6454 */                   performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a sand tile into a clay tile, but will require blessing to activate it.");
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                   
/*  8522 */                   return done;case 2: performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a grass or mycelium tile into a peat tile, but will require blessing to activate it."); return done;case 3: performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a steppe tile into a tar tile, but will require blessing to activate it."); return done;case 4: performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a clay tile into a dirt tile, but will require blessing to activate it."); return done;case 5: performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a peat tile into a dirt tile, but will require blessing to activate it."); return done;case 6: performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a tar tile into a dirt tile, but will require blessing to activate it."); return done;case 7: performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a moss tile into a tundra tile, but will require blessing to activate it."); return done;case 8: performer.getCommunicator().sendNormalServerMessage("That would make a liquid that could transform a tundra tile into a dirt tile, but will require blessing to activate it."); return done; }  performer.getCommunicator().sendNormalServerMessage("You cannot mix these."); } else if (!target.isSealedByPlayer() && (target.isFoodMaker() || target.getTemplate().isCooker())) { showRecipeInfo(performer, source, target); } else { performer.getCommunicator().sendNormalServerMessage("You cannot mix these."); }  } else { if (action == 12) { if (target.getTemplateId() == 1396 && target.isPlanted() && target.getAuxData() > 0) { lightItem(target, source, performer); } else if (target.getTemplateId() != 729 || target.getAuxData() > 0) { lightItem(target, source, performer); }  return true; }  if (action == 134) { if (source.isWeaponBow() && ttid == 458) done = Archery.attack(performer, target, source, counter, act);  } else if (target.isLockable() && (action == 161 || action == 78)) { done = true; if (source.isLock()) { if (source.isLocked()) { performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " is already in use."); return true; }  if (target.getLastOwnerId() == performer.getWurmId() || target.getOwnerId() == performer.getWurmId()) { done = MethodsItems.lock(performer, source, target, counter, (action == 78)); } else { performer.getCommunicator().sendNormalServerMessage("The owner will have to lock that."); }  }  } else if (target.isLockable() && action == 102) { done = true; if (target.getLastOwnerId() == performer.getWurmId() || target.getOwnerId() == performer.getWurmId()) { done = MethodsItems.unlock(performer, source, target, counter); } else { performer.getCommunicator().sendNormalServerMessage("Only the owner can unlock that."); }  } else if (source.isWand() && performer.getPower() >= 2 && target.isLock() && action == 28 && Servers.isThisATestServer()) { target.setLocked(true); performer.getCommunicator().sendNormalServerMessage("You set that lock to its locked (bugged) state."); } else if (source.isWand() && performer.getPower() >= 2 && target.isLock() && action == 102) { target.setLocked(false); performer.getCommunicator().sendNormalServerMessage("You set that lock to its unlocked state."); } else if (target.isLockable() && action == 101 && !target.isNotLockpickable()) { VolaTile vt = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface()); Village vill = (vt == null) ? null : vt.getVillage(); boolean ok = false; if (target.getLastOwnerId() == performer.getWurmId()) { ok = true; } else if (target.isInPvPZone() && Methods.isActionAllowed(performer, action, true, target.getTileX(), target.getTileY(), 0, 0)) { ok = true; } else if (!target.isInPvPZone() && !target.isVehicle() && vill != null && vill.isActionAllowed(action, performer)) { ok = true; }  if (ok) { done = MethodsItems.picklock(performer, source, target, counter, act); } else { done = true; performer.getCommunicator().sendNormalServerMessage("Stealing " + target.getTemplate().getPlural() + " is punished with death penalty here. You decide not to."); }  } else if (action == 182) { if (target.isWrapped()) { performer.getCommunicator().sendNormalServerMessage("You need to unwrap it first before eating it."); return true; }  if (!target.isNoEatOrDrink()) { done = false; if (act.justTickedSecond()) done = MethodsItems.eat(act, performer, target, counter);  }  } else if (action == 600) { if (target.isInstaDiscard()) { done = false; if (act.justTickedSecond()) done = Methods.discardSellItem(performer, act, target, counter);  }  } else if (action == 325) { done = MethodsItems.askSleep(act, performer, target, counter); } else if (action == 140) { done = MethodsItems.sleep(act, performer, target, counter); } else if (action == 365 || action == 366 || action == 367 || action == 320 || action == 319 || action == 322 || action == 321 || action == 323) { done = MethodsItems.setRent(act, performer, target); } else if (action == 324) { done = MethodsItems.rent(act, performer, target); } else if (action == 183) { if (ttid == 1101) return MethodsItems.drinkChampagne(performer, target);  if (target.isContainerLiquid() && target.getItemCount() == 1) { for (Item i : target.getItems()) { if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable()) { done = false; if (act.justTickedSecond()) done = MethodsItems.drink(act, performer, i, counter);  break; }  }  } else if (!target.isFood() && !target.isNoEatOrDrink() && !target.isUndistilled() && target.isDrinkable()) { done = false; if (act.justTickedSecond()) done = MethodsItems.drink(act, performer, target, counter);  }  } else if (action == 225) { if ((ttid == 729 || ttid == 203) && (source.isWeaponKnife() || stid == 258)) { if (performer.getWurmId() == target.getOwnerId()) { int toMake = (ttid == 203) ? 1170 : 730; try { ItemTemplate it = ItemTemplateFactory.getInstance().getTemplate(toMake); int slices = target.getWeightGrams() / it.getWeightGrams(); if (slices * it.getWeightGrams() < target.getWeightGrams()) slices++;  performer.getCommunicator().sendNormalServerMessage("You cut the " + target.getName() + " into " + slices + " elegant slices."); Server.getInstance().broadCastAction(performer.getName() + " cuts the " + target.getName() + " into " + slices + " elegant slices.", performer, 5); if (performer.getInventory().getNumItemsNotCoins() < 100 - slices) { int slicesWeight = 0; for (int x = 0; x < slices; x++) { try { Item slice = ItemFactory.createItem(toMake, target.getCurrentQualityLevel(), performer.getName()); slice.setName("slice of " + target.getName()); slice.setMaterial(target.getMaterial()); slice.setRealTemplate(target.getRealTemplateId()); if (slicesWeight + it.getWeightGrams() > target.getWeightGrams()) slice.setWeight(target.getWeightGrams() - slicesWeight, true);  slicesWeight += slice.getWeightGrams(); performer.getInventory().insertItem(slice, true); } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, nst.getMessage()); } catch (FailedException fe) { logger.log(Level.WARNING, fe.getMessage()); }  }  Items.destroyItem(target.getWurmId()); } else { performer.getCommunicator().sendNormalServerMessage("You don't have space for the slices."); return true; }  } catch (NoSuchTemplateException nste) { logger.log(Level.WARNING, nste.getMessage(), (Throwable)nste); performer.getCommunicator().sendNormalServerMessage("Slice type does not exist, please report this bug."); return true; }  }  } else if (source.isWeaponKnife()) { if (ttid == 92 || ttid == 129) { done = MethodsItems.filet(act, performer, source, target, counter); } else if (target.getTemplate().getFoodGroup() == 1201 && ttid != 369) { done = MethodsItems.filetFish(act, performer, source, target, counter); }  } else if (!target.isBulk() && target.isFood() && !target.isDrinkable() && !target.isMeat() && !target.isFish() && target.getTemplateId() != 729) { if (target.getWeightGrams() < 2000) { performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " is too small."); return true; }  if (target.getName().contains("portion of ")) { performer.getCommunicator().sendNormalServerMessage("It makes no sense to remove a portion of " + target.getNameWithGenus() + "."); return true; }  if (target.getName().contains("slice of ")) { performer.getCommunicator().sendNormalServerMessage("It makes no sense to remove a slice of " + target.getNameWithGenus() + "."); return true; }  performer.getCommunicator().sendNormalServerMessage("You get a portion of " + target.getNameWithGenus() + "."); Server.getInstance().broadCastAction(performer.getName() + " gets a portion of " + target.getNameWithGenus() + ".", performer, 5); if (performer.getInventory().getNumItemsNotCoins() < 99) { try { int w = 2000; Item slice = ItemFactory.createItem(target.getTemplateId(), target.getQualityLevel(), target.creator); slice.setWeight(w, true); target.setWeight(target.getWeightGrams() - w, true); slice.setTemperature(target.getTemperature()); slice.setAuxData(target.getAuxData()); slice.setName("portion of " + target.getActualName()); slice.setCreator(target.creator); slice.setDamage(target.getDamage()); slice.setMaterial(target.getMaterial()); slice.setSizes(10, 10, 20); if (target.getRealTemplate() != null) slice.setRealTemplate(target.getRealTemplateId());  if (target.descIsExam()) { slice.setDescription(target.examine(performer)); } else { slice.setDescription(target.getDescription()); }  if (target.color != -1) slice.setColor(target.color);  ItemMealData imd = ItemMealData.getItemMealData(target.getWurmId()); if (imd != null) ItemMealData.save(slice.getWurmId(), imd.getRecipeId(), imd.getCalories(), imd.getCarbs(), imd.getFats(), imd.getProteins(), imd.getBonus(), imd.getStages(), imd.getIngredients());  performer.getInventory().insertItem(slice); } catch (NoSuchTemplateException|FailedException e) { logger.log(Level.WARNING, e.getMessage()); }  } else { performer.getCommunicator().sendNormalServerMessage("You don't have space for the slices."); return true; }  }  } else if (action == 739) { if (stid == 1255 && target.canBeSealedByPlayer() && !target.isSealedByPlayer()) { done = MethodsItems.seal(performer, source, target, act); } else if (stid == 561 && target.canBePeggedByPlayer() && !target.isSealedByPlayer()) { done = MethodsItems.seal(performer, source, target, act); } else if ((stid == 748 || stid == 1272) && target.canBePapyrusWrapped() && !target.isWrapped() && source.getAuxData() == 2) { done = MethodsItems.wrap(performer, source, target, act); } else if ((stid == 213 || stid == 926) && target.canBeClothWrapped() && !target.isWrapped()) { done = MethodsItems.wrap(performer, source, target, act); } else if (target.isRaw() && target.canBeRawWrapped() && target.isPStateNone()) { done = MethodsItems.wrap(performer, null, target, act); }  } else if (action == 740 && !target.isLiquid()) { if (target.isSealedByPlayer()) { if (target.isCrate()) { if (performer.getWurmId() == target.getLastOwnerId() || performer.getPower() > 1) { done = MethodsItems.removeSecuritySeal(performer, target, act); } else { performer.getCommunicator().sendSafeServerMessage("Only the last owner can remove the security seal on the " + target.getName() + "."); }  } else { done = MethodsItems.breakSeal(performer, target, act); }  } else if (target.isWrapped()) { done = MethodsItems.unwrap(performer, target, act); }  } else if (action == 19) { if (target.isSealedByPlayer()) { tasteLiquid(performer, target); } else if (target.isFood() || target.isLiquid()) { taste(performer, target); } else if (target.isContainerLiquid() && target.getItemCount() == 1) { for (Item i : target.getItems()) { if (!i.isNoEatOrDrink() && !i.isUndistilled() && i.isDrinkable()) { taste(performer, i); break; }  }  }  } else if (action == 231) { if (target.isColorable() && source.isColor()) done = MethodsItems.colorItem(performer, source, target, act, true);  } else if (action == 232) { if (stid == 441 && !target.isLiquid()) done = MethodsItems.removeColor(performer, source, target, act, true);  } else if (action == 923) { if (source.isColor() && target.supportsSecondryColor()) done = MethodsItems.colorItem(performer, source, target, act, false);  } else if (action == 924) { if (stid == 73 && !target.isLiquid() && target.supportsSecondryColor()) done = MethodsItems.removeColor(performer, source, target, act, false);  } else if (action == 329) { if (stid == 489 || (performer.getPower() >= 2 && (stid == 176 || stid == 315))) done = MethodsItems.watchSpyglass(performer, source, act, counter);  } else if (action == 397) { if (source.isPuppet() && target.isPuppet() && !source.equals(target)) done = MethodsItems.puppetSpeak(performer, source, target, act, counter);  } else if (action == 472) { if (source.getOwnerId() == performer.getWurmId() && stid == 676) { done = true; MissionManager m = new MissionManager(performer, "Manage missions", "Select action", target.getWurmId(), target.getName(), source.getWurmId()); m.sendQuestion(); }  } else if (action == 510) { if (source.getOwnerId() == performer.getWurmId() && stid == 676) { done = true; if (source.getAuxData() >= 10 || performer.getPower() > 0) { AchievementCreation m = new AchievementCreation(performer, "Achievement Management", "Achievement properties", source.getWurmId()); m.sendQuestion(); } else { performer.getCommunicator().sendNormalServerMessage("The cost for creating an achievement is 10 charges. The " + source.getName() + " contains " + source.getAuxData() + "."); }  }  } else if (action == 370) { done = true; if (stid == 751 && ttid == 676) if (source.getOwnerId() != performer.getWurmId() || source.isTraded()) { performer.getCommunicator().sendNormalServerMessage("You can not use the " + source.getName() + " right now."); } else if (target.getAuxData() > 90) { performer.getCommunicator().sendNormalServerMessage("The charges would be lost. Use the " + target.getName() + " a bit more first."); } else { Items.destroyItem(source.getWurmId()); target.setAuxData((byte)(target.getAuxData() + 10)); performer.getCommunicator().sendNormalServerMessage("You recharge the " + target.getName() + ". The " + source.getName() + " crumbles in the process."); }   } else if (action == 632) { done = true; if (stid == 867) { if (Spell.mayBeEnchanted(target)) { if (target.getRarity() < source.getRarity()) { if (!target.isFood() || source.getRarity() < 2) { if ((target.isCombine() && target.getWeightGrams() <= target.getTemplate().getWeightGrams()) || !target.isCombine()) { target.setRarity(source.getRarity()); for (Item sub : target.getItems()) { if (sub == null) continue;  if (sub.isComponentItem()) sub.setRarity(source.getRarity());  }  Items.destroyItem(source.getWurmId()); logger.info(performer.getName() + " using a " + MethodsItems.getRarityDesc(source.getRarity()) + " strange bone (" + source.getWurmId() + ") on a " + target.getName() + " (" + target.getWurmId() + ")"); } else { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " has too much material."); }  } else { performer.getCommunicator().sendNormalServerMessage("Nothing happens."); }  } else { performer.getCommunicator().sendNormalServerMessage("The rarity would not improve."); }  } else { performer.getCommunicator().sendNormalServerMessage("Nothing happens."); }  } else { performer.getCommunicator().sendNormalServerMessage("Nothing happens."); }  } else if (action == 54) { done = true; if (ttid == 765 && (stid == 62 || source.isWeaponCrush())) { performer.getCommunicator().sendNormalServerMessage("You crush the " + target.getName() + ". The " + target.getName() + " crumbles to salt."); try { Item salt = ItemFactory.createItem(764, 99.0F, ""); salt.setWeight(target.getWeightGrams(), true); performer.getInventory().insertItem(salt, true); Items.destroyItem(target.getWurmId()); } catch (FailedException e) { logger.log(Level.WARNING, e.getMessage(), (Throwable)e); } catch (NoSuchTemplateException e) { logger.log(Level.WARNING, e.getMessage(), (Throwable)e); }  }  } else if (action == 911) { done = identifyFragment(act, performer, source, target, action, counter); } else if (action == 912) { done = combineFragment(act, performer, source, target, action, counter); } else if (action == 354) { done = true; if (source.isRoyal()) if (stid == 535 || stid == 529 || stid == 532) if (performer.isKing()) { AppointmentsQuestion question = new AppointmentsQuestion(performer, "Appointments", "Which appointments do you wish to do today?", source.getWurmId()); question.sendQuestion(); } else { performer.getCommunicator().sendNormalServerMessage("You laugh at yourself - you who probably couldn't even appoint a cat to catch mice, now wielding a mighty sceptre! How preposterous!"); }    } else if (action == 3 && target.isHollow() && !target.isSealedByPlayer() && (!target.getTemplate().hasViewableSubItems() || target.getTemplate().isContainerWithSubItems() || performer.getPower() > 0)) { if (target.isSealedByPlayer()) return true;  if (target.getTemplateId() == 1342 && !target.isPlanted()) return true;  done = true; boolean isTop = (target.getWurmId() == target.getTopParent() || (target.getTopParentOrNull() != null && target.getTopParentOrNull().getTemplate().hasViewableSubItems() && (!target.getTopParentOrNull().getTemplate().isContainerWithSubItems() || target.isPlacedOnParent()))); if (!isTop) { if (target.isLockable()) { if (target.getLockId() == -10L || (target.isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target))) { performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId()); } else { try { Item lock = Items.getItem(target.getLockId()); if (!lock.getLocked() || target.isOwner(performer)) { performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId()); } else { long[] keys = lock.getKeyIds(); for (int i = 0; i < keys.length; i++) { Item key = Items.getItem(keys[i]); if (key.getTopParent() == performer.getInventory().getWurmId()) { performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId()); return true; }  }  }  performer.getCommunicator().sendSafeServerMessage("The " + target.getName() + " is locked. Please use the key to unlock and open it."); } catch (NoSuchItemException nsi) { performer.getCommunicator().sendSafeServerMessage("The " + target.getName() + " is locked. Please use the key to unlock and open it."); }  }  } else if (target.getTemplateId() == 272 && target.getWasBrandedTo() != -10L) { if (target.mayCommand(performer)) { performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId()); } else { performer.getCommunicator().sendNormalServerMessage("You do not have permissions."); return true; }  } else { performer.getCommunicator().sendOpenInventoryContainer(target.getWurmId()); }  return done; }  if (target.getTemplateId() == 272 && target.getWasBrandedTo() != -10L && !target.mayCommand(performer)) { performer.getCommunicator().sendNormalServerMessage("You do not have permissions."); return true; }  if (target.getWurmId() != performer.getVehicle() && !performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), (target.isVehicle() && !target.isTent()) ? Math.max(6, target.getSizeZ() / 100) : 6.0F)) return done;  if ((target.getTemplateId() == 1239 || target.getTemplateId() == 1175) && target.hasQueen() && !WurmCalendar.isSeasonWinter() && performer.getBestBeeSmoker() == null && performer.getPower() < 2) { performer.getCommunicator().sendSafeServerMessage("The bees get angry and defend the " + target.getName() + " by stinging you."); performer.addWoundOfType(null, (byte)5, 2, true, 1.0F, false, (5000.0F + Server.rand.nextFloat() * 7000.0F), 0.0F, 20.0F, false, false); return true; }  if (target.getTemplateId() == 1239) Achievements.triggerAchievement(performer.getWurmId(), 552);  if (target.isLockable()) { long lockId = target.getLockId(); if (target.getLockId() == -10L || (target.isDraggable() && MethodsItems.mayUseInventoryOfVehicle(performer, target)) || (target.getTemplateId() == 850 && MethodsItems.mayUseInventoryOfVehicle(performer, target)) || (target.isLocked() && target.mayAccessHold(performer))) { if (performer.addItemWatched(target)) { if (target.getTemplateId() == 995) if (target.getAuxData() < 100) { performer.achievement(367); target.setAuxData((byte)100); }   if (target.getDescription().isEmpty()) { performer.getCommunicator().sendOpenInventoryWindow(target.getWurmId(), target.getName()); } else { performer.getCommunicator().sendOpenInventoryWindow(target.getWurmId(), target.getName() + " [" + target.getDescription() + "]"); }  target.addWatcher(target.getWurmId(), performer); target.sendContainedItems(target.getWurmId(), performer); }  } else { try { Item lock = Items.getItem(lockId); boolean hasKey = performer.hasKeyForLock(lock); if (hasKey) { if (performer.addItemWatched(target)) { if (target.getDescription().isEmpty()) { performer.getCommunicator().sendOpenInventoryWindow(target.getWurmId(), target.getName()); } else { performer.getCommunicator().sendOpenInventoryWindow(target.getWurmId(), target.getName() + " [" + target.getDescription() + "]"); }  target.addWatcher(target.getWurmId(), performer); target.sendContainedItems(target.getWurmId(), performer); }  return true; }  } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, "No lock with id " + lockId + ", although the item has that."); }  }  } else if (performer.addItemWatched(target)) { if (target.getTemplateId() == 995) if (target.getAuxData() < 100) { performer.achievement(367); target.setAuxData((byte)100); }   if (target.getDescription().isEmpty()) { performer.getCommunicator().sendOpenInventoryWindow(target.getWurmId(), target.getName()); } else { performer.getCommunicator().sendOpenInventoryWindow(target.getWurmId(), target.getName() + " [" + target.getDescription() + "]"); }  target.addWatcher(target.getWurmId(), performer); target.sendContainedItems(target.getWurmId(), performer); }  } else if (action == 181 || action == 99 || action == 697 || action == 696) { if (target.isMoveable(performer) && (!target.isRoadMarker() || !target.isPlanted())) { done = MethodsItems.moveItem(performer, target, counter, action, act); } else { performer.getCommunicator().sendNormalServerMessage("You may not move that item right now."); }  } else if (action == 177 || action == 178) { if (target.isTurnable(performer) && (!target.isRoadMarker() || !target.isPlanted())) { done = MethodsItems.moveItem(performer, target, counter, action, act); } else { performer.getCommunicator().sendNormalServerMessage("You may not turn that item right now."); }  } else if (action == 926) { done = MethodsItems.placeLargeItem(performer, target, act, counter); } else if (action == 180) { done = true; if (performer.getPower() < 3 && (target.isArtifact() || target.isHugeAltar())) { performer.getCommunicator().sendNormalServerMessage("You are not allowed to destroy that item."); } else { performer.getLogger().log(Level.INFO, performer.getName() + " destroyed " + target.toString()); if (target.isRoadMarker()) target.setWhatHappened("destroyed by " + performer.getName());  Items.destroyItem(target.getWurmId()); if (target.isHugeAltar() && Constants.loadEndGameItems) for (EndGameItem eg : EndGameItems.altars.values()) { if (eg.getWurmid() == target.getWurmId()) { if (eg.isHoly() && EndGameItems.getEvilAltar() != null) { performer.getCommunicator().sendAlertServerMessage("You will also need to destroy the Bone Altar to trigger a respawn on the next server restart."); } else if (!eg.isHoly() && EndGameItems.getGoodAltar() != null) { performer.getCommunicator().sendAlertServerMessage("You will also need to destroy the Altar of Three to trigger a respawn on the next server restart."); } else { performer.getCommunicator().sendAlertServerMessage("The huge altars will respawn on the next server restart."); }  performer.getCommunicator().sendRemoveEffect(eg.getWurmid()); EndGameItems.deleteEndGameItem(eg); break; }  }   performer.getCommunicator().sendNormalServerMessage("You destroy " + target.getNameWithGenus() + "."); Items.destroyItem(target.getWurmId()); }  } else if (action == 486) { done = true; if (performer.getPower() < 2 || ttid != 1437) { performer.getCommunicator().sendNormalServerMessage("Now now, none of that. You shouldn't be here."); } else { target.addSnowmanItem(); }  } else if (action == 519) { if (stid == 788 && target.isMetal() && !target.isLiquid()) if (target.getTemplateId() != Materials.getTemplateIdForMaterial(target.getMaterial())) { if (!target.isIndestructible() && !target.isArtifact() && !target.isUnique() && !target.getTemplate().isStatue() && !target.isRoyal() && !target.getTemplate().isRune() && target.getTemplateId() != 1307 && target.getTemplateId() != 1423) { if (target.getTemperature() > 6000) { if (target.getParentId() > -10L) { if (!target.isHollow() || target.isEmpty(false)) { if (target.getSpellEffects() == null || (target.getSpellEffects().getEffects()).length == 0) { if (!MethodsItems.checkIfStealing(target, performer, act)) { if (target.enchantment == 0) { if (target.isComponentItem()) { performer.getCommunicator().sendNormalServerMessage("You cannot smelt that."); return true; }  if (target.isUnfinished()) { performer.getCommunicator().sendNormalServerMessage("You cannot smelt that, finish it first."); return true; }  if (target.getTemplateId() == 692 || target.getTemplateId() == 693 || target.getTemplateId() == 696 || target.getTemplateId() == 697) { performer.getCommunicator().sendNormalServerMessage("The ore needs to be hotter."); return true; }  if (target.getTemplateId() == 1100) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " seems to resist smelting!"); return true; }  if (target.getTemplateId() == 143) { target.setWeight(3, true); } else { CreationEntry e = CreationMatrix.getInstance().getCreationEntry(target.getTemplateId()); int weightToReturn = 0; int weightToReduce = 0; boolean isCombine = false; if (e != null) { try { ItemTemplate src = ItemTemplateFactory.getInstance().getTemplate(e.getObjectSource()); if (src.getMaterial() == target.getMaterial()) { if (src.isCombine()) isCombine = true;  weightToReturn = Math.min(target.getWeightGrams(), src.getWeightGrams()); logger.log(Level.INFO, "1. Adding weightToReturn " + weightToReturn + " for " + src.getTemplateId() + " srcw=" + src.getWeightGrams() + " tgw=" + target.getWeightGrams() + " combine=" + isCombine); } else if (e.depleteSource || e.depleteEqually) { weightToReduce = Math.min(target.getWeightGrams(), src.getWeightGrams()); logger.log(Level.INFO, "2. Adding weightToReduce " + weightToReduce + " for " + src.getTemplateId() + " srcw=" + src.getWeightGrams() + " tgw=" + target.getWeightGrams()); }  } catch (NoSuchTemplateException noSuchTemplateException) {} try { ItemTemplate src = ItemTemplateFactory.getInstance().getTemplate(e.getObjectTarget()); if (Materials.isMetal(src.getMaterial()) || src.getMaterial() == target.getMaterial() || (src.getMaterial() == 0 && src.isMetal())) { if (src.isCombine()) isCombine = true;  weightToReturn += Math.min(target.getWeightGrams(), src.getWeightGrams()); logger.log(Level.INFO, "3. Adding weightToReturn " + weightToReturn + " for " + src.getTemplateId() + " srcw=" + src.getWeightGrams() + " tgw=" + target.getWeightGrams() + " combine=" + isCombine); } else { weightToReduce = Math.min(target.getWeightGrams(), src.getWeightGrams()); logger.log(Level.INFO, "4. Adding weightToReduce " + weightToReduce + " for " + src.getTemplateId() + " srcw=" + src.getWeightGrams() + " tgw=" + target.getWeightGrams()); }  } catch (NoSuchTemplateException noSuchTemplateException) {} if (!isCombine) { for (CreationRequirement req : e.getRequirements()) { if (req.willBeConsumed()) try { ItemTemplate src = ItemTemplateFactory.getInstance().getTemplate(req.getResourceTemplateId()); if (src.getMaterial() == target.getMaterial()) weightToReturn += src.getWeightGrams() * req.getResourceNumber();  } catch (NoSuchTemplateException noSuchTemplateException) {}  }  } else { for (CreationRequirement req : e.getRequirements()) { if (req.willBeConsumed()) try { ItemTemplate src = ItemTemplateFactory.getInstance().getTemplate(req.getResourceTemplateId()); if (src.getMaterial() != target.getMaterial()) weightToReduce += src.getWeightGrams() * req.getResourceNumber();  } catch (NoSuchTemplateException noSuchTemplateException) {}  }  }  } else { weightToReturn = target.getWeightGrams(); weightToReduce = target.getWeightGrams() / 10; }  if (weightToReduce + weightToReturn == target.getWeightGrams()) { logger.log(Level.INFO, "8. Setting weight " + (weightToReturn - weightToReturn / 10)); target.setWeight(weightToReturn - weightToReturn / 10, true); } else if (!isCombine) { target.setWeight(weightToReturn - weightToReturn / 10, true); logger.log(Level.INFO, "9. Setting weight " + (weightToReturn - weightToReturn / 10)); } else if (weightToReduce + weightToReturn > target.getWeightGrams()) { logger.log(Level.INFO, "10. Setting weight " + (target.getWeightGrams() - weightToReduce - weightToReturn / 10)); target.setWeight(target.getWeightGrams() - weightToReduce - weightToReturn / 10, true); } else if (weightToReduce < target.getWeightGrams()) { target.setWeight(target.getWeightGrams() - weightToReduce - target.getWeightGrams() / 10, true); } else { target.setWeight(weightToReturn - weightToReturn / 10, true); }  }  if (!target.deleted) { target.setDamage(0.0F); float qlMod = 1.0F; if (!target.isRepairable()) { qlMod = 0.975F - (100.0F - source.getCurrentQualityLevel()) / 100.0F * 0.05F; if (target.isMoonMetal() || target.isAlloyMetal()) qlMod -= 0.05F;  }  target.setQualityLevel(Math.min(target.getCurrentQualityLevel() * qlMod, source.getCurrentQualityLevel())); target.setCreationState((byte)0); target.setCreator(""); target.setRealTemplate(-10); target.setTemplateId(Materials.getTemplateIdForMaterial(target.getMaterial())); if (target.getLockId() > 0L) target.setLockId(-10L);  }  source.setDamage(source.getDamage() + source.getDamageModifier() * 0.002F); } else { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " resists smelting."); }  } else { performer.getCommunicator().sendNormalServerMessage("You need to steal the " + target.getName() + " first."); }  } else { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " resists smelting."); }  } else { performer.getCommunicator().sendNormalServerMessage("You'd want to empty the " + target.getName() + " first."); }  } else { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " needs to be in a container."); }  } else { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is not hot enough."); }  } else { performer.getCommunicator().sendNormalServerMessage("You can't bring yourself to smelt the " + target.getName() + ". What's up with you today?"); }  } else { performer.getCommunicator().sendNormalServerMessage("It makes no sense to smelt " + target.getNameWithGenus() + ". There would be no change."); }   } else if (action == 380) { if (source.isCoin() && target.isSpringFilled()) if (target.getSpellCourierBonus() > 0.0F) { WishQuestion wq = new WishQuestion(performer, "Make a wish", "What do you wish?", target.getWurmId(), source.getWurmId()); wq.sendQuestion(); }   } else if (action == 145) { done = true; if (stid == 20 && (ttid == 692 || ttid == 696)) { done = MethodsItems.mine(performer, source, target, counter, act); } else if (stid == 20 && target.getTemplate().isRiftStoneDeco()) { done = MethodsItems.gatherRiftResource(performer, source, target, counter, act); }  } else if (action == 96) { if (source.isWeaponAxe() && target.getTemplate().isRiftPlantDeco()) done = MethodsItems.gatherRiftResource(performer, source, target, counter, act);  } else if (action == 156) { if (stid == 20 && target.getTemplate().isRiftCrystalDeco()) done = MethodsItems.gatherRiftResource(performer, source, target, counter, act);  } else if (action >= 496 && action <= 502) { done = MethodsReligion.performRitual(performer, source, target, counter, action, act); } else if (action == 184) { done = true; if (performer.getPower() >= 3) { if (stid == 176) { Methods.sendShutdownQuestion(performer, source); } else if (ttid == 176) { Methods.sendShutdownQuestion(performer, target); }  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 135) { done = true; if (performer.getPower() >= 3) { if (stid == 176) { if (ttid != 176) { Methods.sendHideQuestion(performer, source, target); } else { performer.getCommunicator().sendNormalServerMessage("You can't hide a " + target.getName() + "."); }  } else if (ttid == 176) { Methods.sendHideQuestion(performer, source, target); }  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 194) { done = true; if (performer.getPower() >= 3) { if (stid == 176) { Methods.sendPaymentQuestion(performer, source); } else if (ttid == 176) { Methods.sendPaymentQuestion(performer, target); }  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 460) { performer.getCommunicator().sendNormalServerMessage("Wrapping is disabled for now since it was mostly used to exploit."); done = true; } else if (action == 463) { done = true; if (target.getOwnerId() != performer.getWurmId() || source.getOwnerId() != performer.getWurmId()) { performer.getCommunicator().sendNormalServerMessage("You need to hold both the " + source.getName() + " and the " + target.getName() + "."); return true; }  if (ttid == 1076 && source.isGem()) if (target.getData1() <= 0) { done = false; if (counter == 1.0F) { performer.getCommunicator().sendNormalServerMessage("You start inserting the " + source.getName() + "."); Server.getInstance().broadCastAction(performer.getName() + " starts inserting " + source.getNameWithGenus() + " into " + target.getNameWithGenus() + ".", performer, 5); performer.sendActionControl(act.getActionString(), true, 400); }  if (act.currentSecond() == 5) { performer.getCommunicator().sendNormalServerMessage("You investigate the " + source.getName() + " and the " + target.getName() + "."); Server.getInstance().broadCastAction(performer.getName() + " investigates the " + source.getName() + " and the " + target.getName() + ".", performer, 5); }  if (act.currentSecond() == 10) { int skillRequired = 40; if (stid == 383 || stid == 375 || stid == 377 || stid == 379 || stid == 381) skillRequired = 60;  boolean ok = false; try { Skill jewelry = performer.getSkills().getSkill(10043); if (jewelry.getKnowledge(0.0D) >= skillRequired) ok = true;  if (ok) { performer.getCommunicator().sendNormalServerMessage("You believe that this should be possible given your jewelry smithing skills."); Server.getInstance().broadCastAction(performer.getName() + " grunts and focuses on the task.", performer, 5); }  } catch (NoSuchSkillException noSuchSkillException) {} if (!ok) { performer.getCommunicator().sendNormalServerMessage("You don't believe this will be possible given your relative inexperience with jewelry smithing."); Server.getInstance().broadCastAction(performer.getName() + " sighs and shrugs and gives up.", performer, 5); return true; }  }  if (act.currentSecond() == 40) { done = true; int qlset = (int)Math.max(1.0F, source.getQualityLevel() / 2.0F); int skillRequired = 40; if (stid == 383 || stid == 375 || stid == 377 || stid == 379 || stid == 381) { skillRequired = 60; qlset = (int)(50.0F + source.getQualityLevel() / 2.0F); }  try { Skill jewelry = performer.getSkills().getSkill(10043); jewelry.skillCheck(skillRequired, source.getQualityLevel(), false, 40.0F); } catch (NoSuchSkillException noSuchSkillException) {} performer.getCommunicator().sendNormalServerMessage("You skillfully insert the " + source.getName() + " into the " + target.getName() + "."); Server.getInstance().broadCastAction(performer.getName() + " rejoices as " + performer.getHeSheItString() + " inserts the " + source.getName() + " into the " + target.getName() + "!", performer, 5); target.setData2(qlset); int data1 = 0; switch (stid) { case 382: case 383: data1 = 1; break;case 374: case 375: data1 = 2; break;case 376: case 377: data1 = 3; break;case 378: case 379: data1 = 4; break;case 380: case 381: data1 = 5; break; }  if (source.getRarity() > target.getRarity()) { target.setRarity(source.getRarity()); if (target.getRarity() == 1) { target.setAuxData((byte)50); } else if (target.getRarity() == 2) { target.setAuxData((byte)80); } else if (target.getRarity() == 3) { target.setAuxData((byte)120); }  }  target.setData1(data1); Items.destroyItem(source.getWurmId()); }  } else { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " already contains a gem."); }   } else if (action == 922) { if (!performer.getInventory().mayCreatureInsertItem()) { performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find."); return true; }  if (target.getTemperature() > 200) { performer.getCommunicator().sendNormalServerMessage("Ouch! Maybe you shouldn't do that while the " + target.getName() + " is lit."); return true; }  if (target.getTemplateId() == 180 || target.getTemplateId() == 178 || target.getTemplateId() == 1023 || target.getTemplateId() == 1028) { if (source.getTemplateId() == 25) { if (target.getParentId() != -10L) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " must be on the ground."); return true; }  if (performer.getStatus().getStamina() < 10000) { performer.getCommunicator().sendNormalServerMessage("You are too exhausted."); return true; }  done = false; if (counter == 1.0F) { if (target.getAuxData() == 0) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already clean."); return true; }  performer.getCommunicator().sendNormalServerMessage("You start removing the ash from the " + target.getName() + "."); Server.getInstance().broadCastAction(performer.getName() + " starts removing the ash from " + target.getNameWithGenus() + ".", performer, 5); performer.sendActionControl(act.getActionString(), true, 300); target.setAuxData((byte)0); }  if (counter >= act.getNextTick()) { if (act.getRarity() != 0) performer.playPersonalSound("sound.fx.drumroll");  int searchCount = act.getTickCount(); int maxSearches = target.getAuxData() / 10; act.incTickCount(); act.incNextTick(10.0F); try { float power = (float)performer.getSkills().getSkill(1010).getKnowledge(); float min = Math.min(target.getQualityLevel(), power); float max = Math.max(target.getQualityLevel(), power); float ql = Server.rand.nextFloat() * (max - min) + min; Item newItem = ItemFactory.createItem(141, ql, act.getRarity(), null); Item inventory = performer.getInventory(); inventory.insertItem(newItem); performer.getCommunicator().sendNormalServerMessage("You found some " + newItem.getName() + "!"); Server.getInstance().broadCastAction(performer.getName() + " puts something in " + performer.getHisHerItsString() + " pocket.", performer, 5); target.setAuxData((byte)0); } catch (FailedException|NoSuchTemplateException|NoSuchSkillException fe) { logger.log(Level.WARNING, performer.getName() + " " + fe.getMessage(), (Throwable)fe); }  if (searchCount < maxSearches) act.setRarity(performer.getRarity());  }  if (act.currentSecond() >= 29) { try { Skill fms = performer.getSkills().getSkillOrLearn(1010); int knowledge = (int)fms.getKnowledge(0.0D); fms.skillCheck(knowledge, 0.0D, false, 1.0F); double power = fms.skillCheck(knowledge, 0.0D, false, 1.0F); if (target.getQualityLevel() > power) power = (float)performer.getSkills().getSkill(1010).getKnowledge();  if (target.getQualityLevel() < power) power = (float)performer.getSkills().getSkill(1010).getKnowledge();  float min = (float)Math.min(target.getQualityLevel(), power); float max = (float)Math.max(target.getQualityLevel(), power); float ql = Server.rand.nextFloat() * (max - min) + min; Item newItem = ItemFactory.createItem(141, ql, act.getRarity(), null); Item inventory = performer.getInventory(); inventory.insertItem(newItem); performer.getCommunicator().sendNormalServerMessage("You found some " + newItem.getName() + "!"); Server.getInstance().broadCastAction(performer.getName() + " puts something in " + performer.getHisHerItsString() + " pocket.", performer, 5); target.setAuxData((byte)0); } catch (Exception ex) { ex.printStackTrace(); }  return true; }  } else { performer.getCommunicator().sendNormalServerMessage("You need to use a shovel to do that."); }  } else { performer.getCommunicator().sendNormalServerMessage("You can't clean that."); }  } else if (action == 462) { done = true; if (stid == 654 && source.getAuxData() == 0) { if (target.getOwnerId() == performer.getWurmId() || target.getOwnerId() == -10L) { if (target.getOwnerId() == -10L) if (MethodsItems.checkIfStealing(target, performer, act)) { performer.getCommunicator().sendNormalServerMessage("You are not allowed to do that."); return true; }   if (target.isTransmutable()) { int targetTemplateId = Materials.getTransmutedTemplate(target.getTemplateId()); if (target.getTemplateId() == 204) if (Server.rand.nextInt((performer.getPower() >= 5) ? 5 : 100) == 0) targetTemplateId = 380;   int nums = target.getWeightGrams() / target.getTemplate().getWeightGrams(); done = false; if (counter == 1.0F) { performer.getCommunicator().sendNormalServerMessage("You notice that a reaction occurs!"); Server.getInstance().broadCastAction(performer.getName() + " starts to transmutate " + target.getNameWithGenus() + ".", performer, 5); performer.sendActionControl(act.getActionString(), true, 200); }  if (act.currentSecond() == 5) { if (nums <= 0) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " contains too little material to be transmutated properly."); return true; }  if (nums > source.getWeightGrams()) { performer.getCommunicator().sendNormalServerMessage("You understand that your " + source.getName() + " will not suffice to transmutate all the material."); return true; }  }  if (act.currentSecond() == 10) { performer.getCommunicator().sendNormalServerMessage("You watch with interest as the reaction proceeds."); Server.getInstance().broadCastAction(performer.getName() + " intensely watches the " + target.getNameWithGenus() + " as it seems to change.", performer, 5); }  if (act.currentSecond() == 20) { done = true; performer.getCommunicator().sendNormalServerMessage("The change is complete!"); Server.getInstance().broadCastAction(performer.getName() + " rejoices as the " + target.getNameWithGenus() + " changes to something new!", performer, 5); try { ItemTemplate targetTemplate = ItemTemplateFactory.getInstance().getTemplate(targetTemplateId); int weight = Math.min(targetTemplate.getWeightGrams() * nums, target.getWeightGrams()); target.setWeight(weight, true); target.setTemplateId(targetTemplateId); } catch (NoSuchTemplateException nst) { performer.getCommunicator().sendNormalServerMessage("Nothing happens after all."); return true; }  source.setWeight(source.getWeightGrams() - nums, true); }  } else { performer.getCommunicator().sendNormalServerMessage("A quick test reveals that no reaction occurs."); }  } else { performer.getCommunicator().sendNormalServerMessage("You need to be in control of the " + target.getName() + "."); }  } else { performer.getCommunicator().sendNormalServerMessage("You do a basic test and conclude that nothing will happen to the " + target.getName() + "."); }  } else if (action == 195) { done = true; if (performer.getPower() >= 3) { if (stid == 176) { Methods.sendPowerManagementQuestion(performer, source); } else if (ttid == 176) { Methods.sendPowerManagementQuestion(performer, target); }  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 212) { done = true; if (WurmPermissions.maySetFaith(performer)) { if (stid == 176) { Methods.sendFaithManagementQuestion(performer, source); } else if (ttid == 176) { Methods.sendFaithManagementQuestion(performer, target); }  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 481) { done = true; if (performer.getPower() >= 3) { if (stid == 176) { Methods.sendConfigureTwitter(performer, Servers.localServer.id, false, Servers.localServer.name); } else if (ttid == 176) { Methods.sendConfigureTwitter(performer, Servers.localServer.id, false, Servers.localServer.name); }  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 503) { done = true; if (performer.getPower() >= 2) if (stid == 176 || stid == 315) Methods.sendCreateZone(performer);   } else if (action == 244) { done = true; if (performer.getPower() >= 1) { if (stid == 176 || stid == 315 || stid == 1027 || stid == 174) { Methods.sendServerManagementQuestion(performer, source.getWurmId()); } else if (ttid == 176 || ttid == 315 || ttid == 1027 || stid == 174) { Methods.sendServerManagementQuestion(performer, target.getWurmId()); }  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 132) { if (source.getTemplateId() == 457 && target.isBowUnstringed()) { done = MethodsItems.string(performer, source, target, act); } else if ((stid == 150 || stid == 151) && ttid == 780) { done = MethodsItems.stringRod(performer, source, target, act); }  } else if (action == 674) { done = true; if ((stid == 176 || stid == 315) && performer.getPower() >= 2) { String name = target.getName(); if (target.getRarity() == 1) { name = "rare " + name; } else if (target.getRarity() == 2) { name = "supreme " + name; } else if (target.getRarity() == 3) { name = "fantastic " + name; }  performer.setTagItem(target.getWurmId(), name); }  } else if (action == 179) { done = true; if ((stid == 176 || stid == 315) && performer.getPower() >= 2) try { Zone currZone = Zones.getZone((int)target.getPosX() >> 2, (int)target.getPosY() >> 2, target.isOnSurface()); currZone.removeItem(target); target.putItemInfrontof(performer); } catch (NoSuchZoneException nsz) { performer.getCommunicator().sendNormalServerMessage("Failed to locate the zone for that item. Failed to summon."); logger.log(Level.WARNING, target.getWurmId() + ": " + nsz.getMessage(), (Throwable)nsz); } catch (NoSuchCreatureException nsc) { performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon."); logger.log(Level.WARNING, target.getWurmId() + ": " + nsc.getMessage(), (Throwable)nsc); } catch (NoSuchItemException nsi) { performer.getCommunicator().sendNormalServerMessage("Failed to locate the item for that request! Failed to summon."); logger.log(Level.WARNING, target.getWurmId() + ":" + nsi.getMessage(), (Throwable)nsi); } catch (NoSuchPlayerException nsp) { performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that request.. you! Failed to summon."); logger.log(Level.WARNING, target.getWurmId() + ":" + nsp.getMessage(), (Throwable)nsp); }   } else if (action == 92) { done = true; if (performer.getPower() >= 3) { if (source.getTemplateId() == 176) Methods.sendLearnSkillQuestion(performer, source, target.getWurmId());  } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 189) { done = true; if (source.isContainerLiquid() && target.isLiquid()) { MethodsItems.fillContainer(act, source, target, performer, false); } else if (source.isOilConsuming() && target.isLiquidInflammable()) { byte already = source.getAuxData(); byte avail = (byte)(126 - already); if (avail == 0) { performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " is full already."); } else if (avail > target.getWeightGrams()) { source.setAuxData((byte)(already + target.getWeightGrams())); performer.getCommunicator().sendNormalServerMessage("You fill the " + source.getName() + "."); Items.destroyItem(target.getWurmId()); } else { source.setAuxData((byte)126); target.setWeight(target.getWeightGrams() - avail, true); performer.getCommunicator().sendNormalServerMessage("You fill the " + source.getName() + "."); }  } else if (source.isOilConsuming() && target.isLiquid()) { performer.getCommunicator().sendNormalServerMessage("You must fill the " + source.getName() + " with a burning liquid."); } else if (target.isOilConsuming() && source.isLiquidInflammable()) { byte already = target.getAuxData(); byte avail = (byte)(126 - already); if (avail == 0) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is full already."); } else if (avail > source.getWeightGrams()) { target.setAuxData((byte)(already + source.getWeightGrams())); performer.getCommunicator().sendNormalServerMessage("You fill the " + target.getName() + "."); Items.destroyItem(source.getWurmId()); } else { target.setAuxData((byte)126); source.setWeight(source.getWeightGrams() - avail, true); performer.getCommunicator().sendNormalServerMessage("You fill the " + target.getName() + "."); }  } else if (stid == 133) { if (target.isCandleHolder()) if (target.getAuxData() >= 126) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " can hold no more candles."); } else { boolean nocandle = (target.getAuxData() == 0); performer.getCommunicator().sendNormalServerMessage("You put the " + source.getName() + " " + ((ttid == 729) ? "on" : "in") + " the " + target.getName() + "."); int toadd = (int)(source.getCurrentQualityLevel() / ((source.getRealTemplateId() == -1) ? 2.0F : 1.5F)); target.setAuxData((byte)Math.min(126, target.getAuxData() + Math.max(1, toadd))); Items.destroyItem(source.getWurmId()); if (nocandle && ttid == 729) { performer.getCommunicator().sendNormalServerMessage("You changed the cake into a birthday cake!"); performer.getCommunicator().sendUpdateInventoryItem(target); performer.getCommunicator().sendRename(target, target.getName(), target.getModelName()); }  }   }  } else if (action == 89) { done = true; if (performer.getPower() <= 0) { MethodsCreatures.sendSetKingdomQuestion(performer, target); } else if (performer.getPower() >= 2) { MethodsCreatures.sendSetKingdomQuestion(performer, target); } else { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); }  } else if (action == 91) { if ((source.getTemplateId() == 176 || source.getTemplateId() == 315) && performer.getPower() >= 2) { float nut = (50 + Server.rand.nextInt(49)) / 100.0F; performer.getStatus().setMaxCCFP(); performer.getStatus().refresh(nut, false); if (performer.getPower() >= 4 && performer.getDeity() != null && performer.isPriest()) try { performer.setFavor(performer.getFaith()); } catch (IOException e) { logger.log(Level.WARNING, e.getMessage(), e); }   done = true; } else { done = action(act, performer, target, action, counter); }  } else if (action == 95) { done = true; MethodsCreatures.teleportCreature(performer, source); } else if (action == 94) { done = true; if ((source.getTemplateId() == 176 || source.getTemplateId() == 315 || source.getTemplateId() == 1027) && performer.getPower() >= 1) Methods.sendTeleportQuestion(performer, source);  } else if (action == 539) { done = true; if (performer.getPower() >= 4 && Spell.mayBeEnchanted(target)) Methods.sendGmSetEnchantQuestion(performer, target);  } else if (act.isSpell()) { done = true; Spell spell = Spells.getSpell(action); if (spell != null) { if (spell.religious) { if (performer.getDeity() != null) { if (source.isHolyItem(performer.getDeity())) { if (Methods.isActionAllowed(performer, (short)245)) done = Methods.castSpell(performer, spell, target, counter);  } else { performer.getCommunicator().sendNormalServerMessage((performer.getDeity()).name + " will not let you use that item."); }  } else { performer.getCommunicator().sendNormalServerMessage("You have no deity and cannot cast the spell."); }  } else if (Methods.isActionAllowed(performer, (short)547)) { done = Methods.castSpell(performer, spell, target, counter); }  } else { logger.log(Level.INFO, performer.getName() + " tries to cast unknown spell:" + Actions.actionEntrys[action].getActionString()); performer.getCommunicator().sendNormalServerMessage("That spell is unknown."); }  } else if (Features.Feature.MOVE_BULK_TO_BULK.isEnabled() && action == 914) { done = moveBulkItemAsAction(act, performer, source, target, counter); } else if (action == 936 && target.isFish() && (stid == 258 || stid == 93 || stid == 126)) { done = makeBait(act, performer, source, target, action, counter); } else { done = action(act, performer, target, action, counter); }  }  }  }  } else { done = action(act, performer, target, action, counter); }  return done;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static List<ActionEntry> addCreationEntrys(List<ActionEntry> toReturn, Creature performer, Item source, Item target) {
/*  8528 */     Recipe recipe = Recipes.getRecipeFor(performer.getWurmId(), (byte)2, source, target, true, false);
/*  8529 */     if (recipe != null) {
/*       */ 
/*       */       
/*  8532 */       Item realSource = source;
/*  8533 */       Item realTarget = target;
/*       */ 
/*       */ 
/*       */       
/*  8537 */       if (recipe.hasActiveItem() && source != null && recipe.getActiveItem().getTemplateId() != realSource.getTemplateId() && recipe
/*  8538 */         .getActiveItem().getTemplateId() != 14) {
/*       */         
/*  8540 */         realSource = target;
/*  8541 */         realTarget = source;
/*       */       } 
/*       */       
/*  8544 */       toReturn.add(new ActionEntry((short)-1, "Create", "creating"));
/*  8545 */       if (recipe.getResultItem().isDrinkable()) {
/*  8546 */         toReturn.add(new ActionEntry((short)-1, "Drink", "creating"));
/*  8547 */       } else if (recipe.getResultItem().isLiquid()) {
/*  8548 */         toReturn.add(new ActionEntry((short)-1, "Liquid", "creating"));
/*       */       } else {
/*  8550 */         toReturn.add(new ActionEntry((short)-1, "Food", "creating"));
/*  8551 */       }  int chance = (int)recipe.getChanceFor(realSource, realTarget, performer);
/*  8552 */       String dif = " (dif:" + recipe.getDifficulty(realTarget) + ")";
/*  8553 */       if (performer.getPower() == 5) {
/*  8554 */         dif = dif + " [" + recipe.getRecipeId() + "]";
/*       */       }
/*  8556 */       toReturn.add(new ActionEntry(recipe.getMenuId(), recipe
/*  8557 */             .getSubMenuName(realTarget) + ": " + chance + "%" + dif, "creating"));
/*       */     } 
/*  8559 */     CreationEntry[] options = CreationMatrix.getInstance().getCreationOptionsFor(source, target);
/*  8560 */     if (options.length > 0) {
/*       */ 
/*       */       
/*  8563 */       Map<String, Map<CreationEntry, Integer>> map = generateMapfromOptions(performer, source, target, options);
/*  8564 */       String key = "Miscellaneous";
/*       */       
/*  8566 */       if (!map.isEmpty()) {
/*       */         
/*  8568 */         int sz = -map.size();
/*       */         
/*  8570 */         toReturn.add(new ActionEntry((short)sz, "Create", "creating"));
/*       */         
/*  8572 */         for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
/*       */           
/*  8574 */           key = it.next();
/*  8575 */           Map<CreationEntry, Integer> map2 = map.get(key);
/*  8576 */           toReturn.add(new ActionEntry((short)-map2.size(), key, "creating " + key));
/*  8577 */           for (CreationEntry entry : map2.keySet()) {
/*       */ 
/*       */             
/*       */             try {
/*  8581 */               int difficulty = ((Integer)map2.get(entry)).intValue();
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  8586 */               ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(entry
/*  8587 */                   .getObjectCreated());
/*  8588 */               toReturn.add(new ActionEntry((short)(10000 + entry.getObjectCreated()), template.sizeString + template
/*  8589 */                     .getName() + ": " + difficulty + "%", template.getName(), emptyIntArr));
/*       */             
/*       */             }
/*  8592 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  8594 */               logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*       */             } 
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     } 
/*  8600 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static Map<String, Map<CreationEntry, Integer>> generateMapfromOptions(Creature performer, Item source, Item target, CreationEntry[] options) {
/*  8606 */     Map<String, Map<CreationEntry, Integer>> map = new HashMap<>();
/*       */     
/*  8608 */     String key = "Miscellaneous";
/*  8609 */     boolean add = true;
/*  8610 */     for (CreationEntry lOption : options) {
/*       */       
/*  8612 */       if (lOption.meetsCreatureRestriction(source, target)) {
/*       */         
/*  8614 */         add = true;
/*       */         
/*       */         try {
/*  8617 */           int chance = (int)lOption.getDifficultyFor(source, target, performer);
/*  8618 */           if ((chance <= 5 && !lOption.hasCustomCreationChanceCutOff()) || (lOption
/*  8619 */             .hasCustomCreationChanceCutOff() && chance < lOption.getCustomCutOffChance()))
/*  8620 */             chance = 0; 
/*  8621 */           ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(lOption.getObjectCreated());
/*  8622 */           key = "Miscellaneous";
/*       */           
/*  8624 */           if (template.isEpicTargetItem()) {
/*       */ 
/*       */             
/*  8627 */             key = "Epic";
/*  8628 */             MissionTrigger[] mtr = MissionTriggers.getMissionTriggersWith(lOption.getObjectCreated(), 148, -10L);
/*       */             
/*  8630 */             if (mtr.length > 0) {
/*  8631 */               add = true;
/*  8632 */             } else if (!Servers.localServer.PVPSERVER) {
/*  8633 */               add = true;
/*       */             } else {
/*  8635 */               add = false;
/*       */             } 
/*       */           } else {
/*       */             
/*  8639 */             key = lOption.getCategory().getCategoryName();
/*       */           } 
/*       */           
/*  8642 */           if (chance == 0) {
/*  8643 */             key = "Unavailable";
/*       */           }
/*  8645 */           if (add)
/*       */           {
/*  8647 */             Map<CreationEntry, Integer> map2 = map.get(key);
/*  8648 */             if (map2 == null)
/*  8649 */               map2 = new HashMap<>(); 
/*  8650 */             map2.put(lOption, Integer.valueOf(chance));
/*  8651 */             map.put(key, map2);
/*       */           }
/*       */         
/*       */         }
/*  8655 */         catch (NoSuchTemplateException nst) {
/*       */           
/*  8657 */           logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*       */         } 
/*       */       } 
/*       */     } 
/*  8661 */     return map;
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
/*       */   private void addBedOptions(Creature performer, Item target, List<ActionEntry> toReturn) {
/*  8689 */     VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  8690 */     if (t != null)
/*       */     {
/*  8692 */       if (t.getStructure() != null && t.getStructure().isTypeHouse()) {
/*       */         
/*  8694 */         if (target.getData() > 0L && target.getWhenRented() < System.currentTimeMillis() - 86400000L) {
/*       */ 
/*       */           
/*  8697 */           target.setData(0L);
/*  8698 */           target.setWhenRented(0L);
/*       */         } 
/*  8700 */         if (t.getStructure().isOwner(performer)) {
/*       */           
/*  8702 */           toReturn.add(new ActionEntry((short)-9, "Bed", "Bops"));
/*  8703 */           toReturn.add(Actions.actionEntrys[325]);
/*  8704 */           toReturn.add(Actions.actionEntrys[365]);
/*  8705 */           toReturn.add(Actions.actionEntrys[366]);
/*  8706 */           toReturn.add(Actions.actionEntrys[367]);
/*  8707 */           toReturn.add(Actions.actionEntrys[319]);
/*  8708 */           toReturn.add(Actions.actionEntrys[320]);
/*  8709 */           toReturn.add(Actions.actionEntrys[321]);
/*  8710 */           toReturn.add(Actions.actionEntrys[322]);
/*  8711 */           toReturn.add(Actions.actionEntrys[323]);
/*       */         }
/*  8713 */         else if (target.mayUseBed(performer)) {
/*       */           
/*  8715 */           if (target.mayFreeSleep(performer) || target.getRentCost() == 0 || target
/*  8716 */             .getData() == performer.getWurmId()) {
/*  8717 */             toReturn.add(Actions.actionEntrys[325]);
/*       */           } else {
/*       */ 
/*       */             
/*       */             try {
/*  8722 */               toReturn.add(new ActionEntry((short)324, "Hire for " + 
/*  8723 */                     Economy.getEconomy().getChangeFor(target.getRentCost()).getChangeString(), "Bops"));
/*       */             }
/*  8725 */             catch (Exception ex) {
/*       */               
/*  8727 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*       */             } 
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   private static void lightItem(Item target, Item tool, Creature performer) {
/*  8737 */     boolean validTool = false;
/*  8738 */     if (target.getTemplateId() == 742) {
/*  8739 */       validTool = true;
/*  8740 */     } else if (tool != null && (tool.getTemplateId() == 143 || ((tool
/*  8741 */       .getTemplateId() == 176 || target.getTemplateId() == 315) && performer
/*  8742 */       .getPower() >= 2))) {
/*  8743 */       validTool = true;
/*  8744 */     } else if (tool != null && tool.isBurnable() && tool.getTemperature() > 1000 && !tool.isIndestructible() && !tool.deleted) {
/*  8745 */       validTool = true;
/*  8746 */     }  if (target.isLight() || target.isFire() || target.getTemplateId() == 742) {
/*       */       
/*  8748 */       if (!validTool)
/*       */         return; 
/*  8750 */       if (!target.isOnFire()) {
/*       */         
/*  8752 */         if (target.getTemplate().isTransportable())
/*       */         {
/*  8754 */           if (target.getTopParent() != target.getWurmId()) {
/*       */             
/*  8756 */             String message = StringUtil.format("The %s must be on the ground before you can light it.", new Object[] { target
/*       */                   
/*  8758 */                   .getName() });
/*  8759 */             performer.getCommunicator().sendNormalServerMessage(message);
/*       */             return;
/*       */           } 
/*       */         }
/*  8763 */         if (target.isOilConsuming())
/*       */         {
/*  8765 */           if (target.getAuxData() <= 0) {
/*       */             
/*  8767 */             performer.getCommunicator().sendNormalServerMessage("The " + target
/*  8768 */                 .getName() + " contains no oil.");
/*       */             return;
/*       */           } 
/*       */         }
/*  8772 */         if (target.isCandleHolder())
/*       */         {
/*  8774 */           if (target.getAuxData() <= 0) {
/*       */             
/*  8776 */             if (target.getTemplateId() == 729) {
/*       */               
/*  8778 */               performer.getCommunicator().sendNormalServerMessage("The " + target
/*  8779 */                   .getName() + " is not a birthday cake.");
/*       */             }
/*       */             else {
/*       */               
/*  8783 */               performer.getCommunicator().sendNormalServerMessage("The " + target
/*  8784 */                   .getName() + " contains only candle stumps. Fill with new ones.");
/*       */             } 
/*       */             return;
/*       */           } 
/*       */         }
/*  8789 */         if ((target.isStreetLamp() || target.getTemplateId() == 1396) && !target.isPlanted())
/*       */         {
/*  8791 */           performer.getCommunicator().sendNormalServerMessage("You need to plant the " + target
/*  8792 */               .getName() + " first.");
/*       */         }
/*  8794 */         else if (tool != null && tool.isBurnable() && tool.getTemperature() > 1000 && !tool.isIndestructible())
/*       */         {
/*  8796 */           performer.getCommunicator().sendNormalServerMessage("You light the " + target.getName() + " using the burning remnants of " + tool
/*  8797 */               .getNameWithGenus() + ".");
/*  8798 */           Server.getInstance().broadCastAction(performer.getName() + " lights " + target.getNameWithGenus() + " using the burning remnants of " + tool
/*  8799 */               .getNameWithGenus() + ".", performer, 5);
/*  8800 */           target.setTemperature((short)10000);
/*  8801 */           Items.destroyItem(tool.getWurmId());
/*       */         }
/*       */         else
/*       */         {
/*  8805 */           performer.getCommunicator().sendNormalServerMessage("You light the " + target.getName() + ".");
/*  8806 */           Server.getInstance().broadCastAction(performer
/*  8807 */               .getName() + " lights " + target.getNameWithGenus() + ".", performer, 5);
/*  8808 */           target.setTemperature((short)10000);
/*       */         }
/*       */       
/*       */       } else {
/*       */         
/*  8813 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already burning.");
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static void awardChristmasPresent(Creature performer) {
/*       */     try {
/*  8822 */       Item present = ItemFactory.createItem(175, 99.0F, null);
/*  8823 */       present.setAuxData((byte)11);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  8836 */       performer.getInventory().insertItem(present, true);
/*  8837 */       performer.setFlag(62, true);
/*       */     }
/*  8839 */     catch (Exception ex) {
/*       */       
/*  8841 */       logger.log(Level.WARNING, performer.getName() + " no gift received: " + ex.getMessage(), ex);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private static boolean doesRingMarkersExist(int ringx, int ringy) {
/*  8847 */     int xa = Zones.safeTileX(ringx - 20);
/*  8848 */     int xe = Zones.safeTileX(ringx + 20);
/*  8849 */     int ya = Zones.safeTileY(ringy - 20);
/*  8850 */     int ye = Zones.safeTileY(ringy + 20);
/*  8851 */     for (int x = xa; x <= xe; x++) {
/*       */       
/*  8853 */       for (int y = ya; y <= ye; y++) {
/*       */ 
/*       */         
/*       */         try {
/*       */           
/*  8858 */           Zone zone = Zones.getZone(x, y, true);
/*  8859 */           VolaTile vtile = zone.getTileOrNull(x, y);
/*  8860 */           if (vtile != null) {
/*       */             
/*  8862 */             Item[] items = vtile.getItems();
/*  8863 */             for (int i = 0; i < items.length; i++)
/*       */             {
/*  8865 */               if (items[i].getTemplateId() == 727 || items[i]
/*  8866 */                 .getTemplateId() == 728)
/*       */               {
/*  8868 */                 return true;
/*       */               }
/*       */             }
/*       */           
/*       */           } 
/*  8873 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*       */       } 
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/*  8879 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private static void createMarkers(int tx, int ty) {
/*  8884 */     int xa = Zones.safeTileX(tx - 20);
/*  8885 */     int xe = Zones.safeTileX(tx + 20);
/*  8886 */     int ya = Zones.safeTileY(ty - 20);
/*  8887 */     int ye = Zones.safeTileY(ty + 20);
/*  8888 */     for (int x = xa; x <= xe; x++) {
/*       */       
/*  8890 */       for (int y = ya; y <= ye; y++) {
/*       */         
/*  8892 */         boolean create = false;
/*  8893 */         boolean createCorner = false;
/*  8894 */         if (x == xa) {
/*       */           
/*  8896 */           if (y == ya || y == ye) {
/*  8897 */             createCorner = true;
/*  8898 */           } else if (y % 5 == 0) {
/*  8899 */             create = true;
/*       */           } 
/*  8901 */         } else if (x == xe) {
/*       */           
/*  8903 */           if (y == ya || y == ye) {
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  8908 */             createCorner = true;
/*       */           }
/*  8910 */           else if (y % 5 == 0) {
/*  8911 */             create = true;
/*       */           } 
/*  8913 */         } else if (y == ya || y == ye) {
/*       */           
/*  8915 */           if (x % 5 == 0)
/*  8916 */             create = true; 
/*       */         } 
/*  8918 */         if (create) {
/*       */           
/*       */           try
/*       */           {
/*  8922 */             Item i = ItemFactory.createItem(727, 80.0F + Server.rand.nextFloat() * 10.0F, null);
/*  8923 */             i.setPosXYZ(((x << 2) + 2), ((y << 2) + 2), Zones.calculateHeight(((x << 2) + 2), ((y << 2) + 2), true));
/*  8924 */             Zones.getZone(x, y, true).addItem(i);
/*       */           }
/*  8926 */           catch (Exception ex)
/*       */           {
/*  8928 */             logger.log(Level.INFO, ex.getMessage());
/*       */           }
/*       */         
/*  8931 */         } else if (createCorner) {
/*       */ 
/*       */           
/*       */           try {
/*       */             
/*  8936 */             Item i = ItemFactory.createItem(728, 80.0F + Server.rand.nextFloat() * 10.0F, null);
/*  8937 */             i.setPosXYZ(((x << 2) + 2), ((y << 2) + 2), Zones.calculateHeight(((x << 2) + 2), ((y << 2) + 2), true));
/*  8938 */             Zones.getZone(x, y, true).addItem(i);
/*       */           }
/*  8940 */           catch (Exception ex) {
/*       */             
/*  8942 */             logger.log(Level.INFO, ex.getMessage());
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private List<ActionEntry> makeMoveSubMenu(Creature performer, Item target) {
/*  8951 */     List<ActionEntry> menulist = new LinkedList<>();
/*  8952 */     if (target.isTurnable(performer) && (!target.isRoadMarker() || !target.isPlanted())) {
/*       */       
/*  8954 */       menulist.add(Actions.actionEntrys[177]);
/*  8955 */       menulist.add(Actions.actionEntrys[178]);
/*       */     } 
/*  8957 */     if (target.isMoveable(performer) && (!target.isRoadMarker() || !target.isPlanted())) {
/*       */       
/*  8959 */       menulist.add(Actions.actionEntrys[99]);
/*  8960 */       menulist.add(Actions.actionEntrys[696]);
/*  8961 */       menulist.add(Actions.actionEntrys[864]);
/*  8962 */       menulist.add(Actions.actionEntrys[926]);
/*  8963 */       if (target.getTemplateId() != 931 || !Servers.localServer.PVPSERVER) {
/*       */         
/*  8965 */         menulist.add(Actions.actionEntrys[181]);
/*  8966 */         menulist.add(Actions.actionEntrys[697]);
/*       */       } 
/*       */     } 
/*  8969 */     if (menulist.isEmpty()) {
/*  8970 */       return menulist;
/*       */     }
/*       */     
/*  8973 */     List<ActionEntry> submenu = new LinkedList<>();
/*  8974 */     submenu.add(new ActionEntry((short)-menulist.size(), "Move", "Move item"));
/*  8975 */     submenu.addAll(menulist);
/*  8976 */     return submenu;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static boolean isSignManipulationOk(Item target, Creature performer, short action) {
/*  8982 */     if (target.lastOwner == performer.getWurmId())
/*  8983 */       return true; 
/*  8984 */     if (performer.getPower() > 0) {
/*  8985 */       return true;
/*       */     }
/*       */ 
/*       */     
/*  8989 */     if (Players.getInstance().getKingdomForPlayer(target.getLastOwnerId()) != performer.getKingdomId() && performer
/*  8990 */       .isPaying() && CargoTransportationMethods.strengthCheck(performer, 21.0D)) {
/*  8991 */       return true;
/*       */     }
/*       */ 
/*       */     
/*  8995 */     VolaTile t = Zones.getTileOrNull(target.getTileX(), target.getTileY(), target.isOnSurface());
/*  8996 */     if (t != null) {
/*       */       
/*  8998 */       Structure structure = t.getStructure();
/*  8999 */       Village village = t.getVillage();
/*       */ 
/*       */       
/*  9002 */       if (target.isPlanted()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  9008 */         boolean ok = false;
/*  9009 */         if (structure != null && structure.isTypeHouse()) {
/*  9010 */           ok = structure.isActionAllowed(performer, (short)685);
/*  9011 */         } else if (village != null) {
/*  9012 */           ok = village.isActionAllowed((short)685, performer);
/*  9013 */         }  if (!ok)
/*  9014 */           return false; 
/*       */       } 
/*  9016 */       if (Actions.isActionDestroy(action) && village != null && village.isActionAllowed(action, performer))
/*  9017 */         return true; 
/*  9018 */       if (structure != null && structure.isTypeHouse() && structure.isFinished()) {
/*       */         
/*  9020 */         if (!Actions.isActionBuildingPermission(action) && village != null && village
/*  9021 */           .isActionAllowed(action, performer))
/*  9022 */           return true; 
/*  9023 */         return structure.isActionAllowed(performer, action);
/*       */       } 
/*       */       
/*  9026 */       if (village != null) {
/*  9027 */         return village.isActionAllowed(action, performer);
/*       */       }
/*       */     } 
/*  9030 */     return !target.isPlanted();
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
/*       */   private static boolean examine(Action act, Creature performer, Item target, short action, float counter) {
/*  9061 */     if (target == null) {
/*       */       
/*  9063 */       logger.log(Level.WARNING, "target was null when trying to examine it in SignBehaviour.");
/*  9064 */       return false;
/*       */     } 
/*  9066 */     ItemTemplate template = target.getTemplate();
/*  9067 */     if (template == null) {
/*       */       
/*  9069 */       logger.log(Level.WARNING, "item (" + target.getWurmId() + ") did not have a template when trying to examine it in SignBehaviour.");
/*       */       
/*  9071 */       return true;
/*       */     } 
/*  9073 */     StringBuilder sendString = new StringBuilder(template.getDescriptionLong());
/*  9074 */     if (target.getTemplateId() == 850 && target.isWagonerWagon()) {
/*       */       
/*  9076 */       sendString.append(" This is used by a wagoner to transport bulk goods around the server.");
/*  9077 */       if (target.getItemCount() > 0) {
/*  9078 */         sendString.append(" It is currently loaded with " + target.getItemCount() + " crates.");
/*       */       } else {
/*  9080 */         sendString.append(" It is currently empty.");
/*       */       } 
/*  9082 */     }  if (target.isPlanted()) {
/*       */       
/*  9084 */       PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(target.lastOwner);
/*  9085 */       String plantedBy = "someone";
/*  9086 */       if (pInfo != null)
/*  9087 */         plantedBy = pInfo.getName(); 
/*  9088 */       sendString.append(" The " + target.getName() + " has been firmly secured to the ground by " + plantedBy + ".");
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  9095 */     String s = target.getSignature();
/*       */     
/*  9097 */     if (s != null && !s.isEmpty() && !target.isDish()) {
/*  9098 */       sendString.append(" You can barely make out the signature of its maker,  '" + s + "'.");
/*       */     }
/*       */     
/*  9101 */     if (target.getRarity() > 0) {
/*  9102 */       sendString.append(MethodsItems.getRarityDesc(target.rarity));
/*       */     }
/*  9104 */     if (target.getColor() != -1)
/*       */     {
/*  9106 */       if (!target.isDragonArmour() || target.getColor2() == -1) {
/*       */         
/*  9108 */         sendString.append(" ");
/*  9109 */         if (target.isWood()) {
/*       */           
/*  9111 */           sendString.append("Wood ");
/*  9112 */           sendString.append(MethodsItems.getColorDesc(target.getColor()).toLowerCase());
/*       */         } else {
/*       */           
/*  9115 */           sendString.append(MethodsItems.getColorDesc(target.getColor()));
/*       */         } 
/*       */       }  } 
/*  9118 */     if (target.supportsSecondryColor() && target.getColor2() != -1) {
/*       */       
/*  9120 */       sendString.append(" ");
/*  9121 */       if (target.isDragonArmour()) {
/*  9122 */         sendString.append(MethodsItems.getColorDesc(target.getColor2()));
/*       */       } else {
/*       */         
/*  9125 */         sendString.append(LoginHandler.raiseFirstLetter(target.getSecondryItemName()));
/*  9126 */         sendString.append(MethodsItems.getColorDesc(target.getColor2()).toLowerCase());
/*       */       } 
/*       */     } 
/*  9129 */     if (target.getLockId() != -10L) {
/*       */       
/*       */       try {
/*       */         
/*  9133 */         Item lock = Items.getItem(target.getLockId());
/*  9134 */         sendString.append(" It is locked with a lock of " + lock.getLockStrength() + " quality.");
/*       */       }
/*  9136 */       catch (NoSuchItemException nsi) {
/*       */         
/*  9138 */         logger.log(Level.WARNING, target.getWurmId() + " has a lock that can't be found: " + target.getLockId(), (Throwable)nsi);
/*       */       } 
/*       */     }
/*  9141 */     sendString.append(" Ql: " + target.getQualityLevel() + ", Dam: " + target.getDamage() + ".");
/*  9142 */     if (target.getBless() != null)
/*       */     {
/*  9144 */       if (performer.getFaith() > 20.0F)
/*       */       {
/*  9146 */         if (performer.getFaith() < 30.0F) {
/*  9147 */           sendString.append(" It has an interesting aura.");
/*  9148 */         } else if (performer.getFaith() < 40.0F) {
/*       */           
/*  9150 */           if (target.getBless().isHateGod()) {
/*  9151 */             sendString.append(" It has a malevolent aura.");
/*       */           } else {
/*  9153 */             sendString.append(" It has a benevolent aura.");
/*       */           } 
/*       */         } else {
/*  9156 */           sendString.append(" It bears an aura of " + (target.getBless()).name + ".");
/*       */         }  } 
/*       */     }
/*  9159 */     if (target.getTemplateId() == 1112 && target.isWagonerCamp()) {
/*       */       
/*  9161 */       Wagoner wagoner = Wagoner.getWagoner(target.getData());
/*  9162 */       if (wagoner != null)
/*       */       {
/*       */         
/*  9165 */         sendString.append(" This is the center of the " + wagoner.getName() + "'s camp.");
/*       */       }
/*       */     } 
/*  9168 */     if (target.isOwnedByWagoner()) {
/*       */       
/*  9170 */       Wagoner wagoner = Wagoner.getWagoner(target.getLastOwnerId());
/*  9171 */       if (wagoner != null)
/*       */       {
/*  9173 */         sendString.append(" This is owned by " + wagoner.getName() + ".");
/*       */       }
/*       */     } 
/*  9176 */     if (target.isCrate() && target.isSealedByPlayer() && target.getData() > -1L) {
/*       */       
/*  9178 */       Delivery delivery = Delivery.getDelivery(target.getData());
/*  9179 */       if (delivery != null) {
/*       */         
/*  9181 */         Wagoner wagoner = Wagoner.getWagoner(delivery.getWagonerId());
/*  9182 */         String appliedBy = (wagoner == null) ? "" : (" applied by " + wagoner.getName() + ",");
/*  9183 */         sendString.append(" It has a security seal," + appliedBy + " and was sent from " + delivery.getSenderName() + " to " + delivery.getReceiverName() + ".");
/*       */       } else {
/*       */         
/*  9186 */         target.setData(-10L);
/*       */       } 
/*  9188 */     }  if (target.getTemplateId() == 1309 && target.isSealedByPlayer()) {
/*       */ 
/*       */       
/*  9191 */       Delivery delivery = Delivery.getDeliveryFrom(target.getWurmId());
/*  9192 */       if (delivery != null) {
/*       */         
/*  9194 */         Wagoner wagoner = Wagoner.getWagoner(delivery.getWagonerId());
/*  9195 */         String appliedBy = (wagoner == null) ? "" : (" applied by " + wagoner.getName() + ",");
/*  9196 */         sendString.append(" It has a security seal," + appliedBy + " and is the collection container used for a delivery from " + delivery.getSenderName() + " to " + delivery.getReceiverName() + ".");
/*       */       } 
/*       */     } 
/*  9199 */     performer.getCommunicator().sendNormalServerMessage(sendString.toString());
/*  9200 */     target.sendEnchantmentStrings(performer.getCommunicator());
/*  9201 */     target.sendExtraStrings(performer.getCommunicator());
/*       */     
/*  9203 */     String improvedBy = MethodsItems.getImpDesc(performer, target);
/*  9204 */     if (!improvedBy.isEmpty())
/*  9205 */       performer.getCommunicator().sendNormalServerMessage(improvedBy); 
/*  9206 */     if (target.getTemplate().isRune()) {
/*       */       
/*  9208 */       String runeDesc = "";
/*  9209 */       if (RuneUtilities.isEnchantRune(target)) {
/*       */         
/*  9211 */         runeDesc = runeDesc + "It can be attached to " + RuneUtilities.getAttachmentTargets(target) + " and will " + RuneUtilities.getRuneLongDesc(RuneUtilities.getEnchantForRune(target)) + ".";
/*  9212 */       } else if (RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(target), RuneUtilities.ModifierEffect.SINGLE_COLOR) > 0.0F || (
/*  9213 */         RuneUtilities.getSpellForRune(target) != null && RuneUtilities.getSpellForRune(target).isTargetAnyItem() && 
/*  9214 */         !RuneUtilities.getSpellForRune(target).isTargetTile())) {
/*       */         
/*  9216 */         runeDesc = runeDesc + "It can be used on " + RuneUtilities.getAttachmentTargets(target) + " and will " + RuneUtilities.getRuneLongDesc(RuneUtilities.getEnchantForRune(target)) + ".";
/*       */       } else {
/*  9218 */         runeDesc = runeDesc + "It will " + RuneUtilities.getRuneLongDesc(RuneUtilities.getEnchantForRune(target)) + ".";
/*       */       } 
/*  9220 */       performer.getCommunicator().sendNormalServerMessage(runeDesc);
/*       */     } 
/*  9222 */     if (target.getTemplateId() == 1423)
/*       */     {
/*  9224 */       if (target.getData() != -1L)
/*       */       {
/*  9226 */         if (target.getAuxData() != 0) {
/*       */           
/*  9228 */           DeadVillage dv = Villages.getDeadVillage(target.getData());
/*       */           
/*  9230 */           String toReturn = dv.getDeedName();
/*  9231 */           if (target.getAuxBit(1)) {
/*       */             
/*  9233 */             toReturn = toReturn + " was founded by " + dv.getFounderName();
/*  9234 */             if (target.getAuxBit(3)) {
/*  9235 */               toReturn = toReturn + " and was inhabited for about " + DeadVillage.getTimeString(dv.getTotalAge(), false) + ".";
/*       */             } else {
/*  9237 */               toReturn = toReturn + ".";
/*       */             } 
/*  9239 */           } else if (target.getAuxBit(3)) {
/*       */             
/*  9241 */             toReturn = toReturn + " was inhabited for about " + DeadVillage.getTimeString(dv.getTotalAge(), false) + ".";
/*       */           } 
/*       */           
/*  9244 */           if (target.getAuxBit(2)) {
/*       */             
/*  9246 */             if (target.getAuxBit(1) || target.getAuxBit(3))
/*  9247 */               toReturn = toReturn + " It"; 
/*  9248 */             toReturn = toReturn + " has been abandoned for roughly " + DeadVillage.getTimeString(dv.getTimeSinceDisband(), false);
/*  9249 */             if (target.getAuxBit(0)) {
/*  9250 */               toReturn = toReturn + " and was last mayored by " + dv.getMayorName() + ".";
/*       */             } else {
/*  9252 */               toReturn = toReturn + ".";
/*       */             } 
/*       */           } else {
/*       */             
/*  9256 */             if (target.getAuxBit(1) || target.getAuxBit(3))
/*  9257 */               toReturn = toReturn + " It"; 
/*  9258 */             toReturn = toReturn + " was last mayored by " + dv.getMayorName() + ".";
/*       */           } 
/*       */           
/*  9261 */           performer.getCommunicator().sendNormalServerMessage(toReturn);
/*       */         } 
/*       */       }
/*       */     }
/*       */     
/*  9266 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   private static byte getTileTransmutationLiquidAuxData(Item source, Item target) {
/*  9271 */     Item[] contents = target.getAllItems(false);
/*  9272 */     boolean allSame = false;
/*  9273 */     if (contents.length >= 1) {
/*       */       
/*  9275 */       allSame = true;
/*       */       
/*  9277 */       int lookingFor = contents[0].getTemplateId();
/*  9278 */       for (Item i : contents) {
/*       */         
/*  9280 */         if (i.getTemplateId() != lookingFor) {
/*       */           
/*  9282 */           allSame = false;
/*       */           
/*       */           break;
/*       */         } 
/*       */       } 
/*  9287 */       if (allSame)
/*  9288 */         return MethodsItems.getTransmutationLiquidAuxByteFor(source, contents[0]); 
/*       */     } 
/*  9290 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getRandomStatueFragment() {
/*  9295 */     int randomFragment = Server.rand.nextInt(8);
/*  9296 */     switch (randomFragment) {
/*       */       
/*       */       case 0:
/*  9299 */         return 1329;
/*       */       case 1:
/*  9301 */         return 1328;
/*       */       case 2:
/*  9303 */         return 1327;
/*       */       case 3:
/*  9305 */         return 1326;
/*       */       case 4:
/*  9307 */         return 1325;
/*       */       case 5:
/*  9309 */         return 1330;
/*       */       case 6:
/*  9311 */         return 1323;
/*       */     } 
/*       */     
/*  9314 */     return 1324;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getRandomGem(boolean starGemsPossible) {
/*  9320 */     int randomGem = Server.rand.nextInt(5);
/*  9321 */     boolean giveStarGem = (starGemsPossible && Server.rand.nextFloat() * 100.0F < 1.0F);
/*  9322 */     switch (randomGem) {
/*       */       
/*       */       case 0:
/*  9325 */         if (giveStarGem)
/*  9326 */           return 375; 
/*  9327 */         return 374;
/*       */       case 1:
/*  9329 */         if (giveStarGem)
/*  9330 */           return 381; 
/*  9331 */         return 380;
/*       */       case 2:
/*  9333 */         if (giveStarGem)
/*  9334 */           return 379; 
/*  9335 */         return 378;
/*       */       case 3:
/*  9337 */         if (giveStarGem)
/*  9338 */           return 377; 
/*  9339 */         return 376;
/*       */       case 4:
/*  9341 */         if (giveStarGem)
/*  9342 */           return 383; 
/*  9343 */         return 382;
/*       */     } 
/*       */     
/*  9346 */     return 374;
/*       */   }
/*       */   
/*       */   public final int getRandomItemFromPack(int packType) {
/*       */     float rand, chance;
/*  9351 */     int templateId = -1;
/*  9352 */     switch (packType) {
/*       */       
/*       */       case 1097:
/*  9355 */         rand = Server.rand.nextFloat() * 100.0F;
/*       */         
/*  9357 */         chance = 1.0F;
/*  9358 */         if (rand <= chance) {
/*       */           
/*  9360 */           switch (Server.rand.nextInt(5)) {
/*       */             
/*       */             case 0:
/*  9363 */               templateId = 375;
/*       */               break;
/*       */             case 1:
/*  9366 */               templateId = 381;
/*       */               break;
/*       */             case 2:
/*  9369 */               templateId = 379;
/*       */               break;
/*       */             case 3:
/*  9372 */               templateId = 377;
/*       */               break;
/*       */             case 4:
/*  9375 */               templateId = 383;
/*       */               break;
/*       */           } 
/*       */           
/*  9379 */           return templateId;
/*       */         } 
/*  9381 */         chance += 10.0F;
/*  9382 */         if (rand <= chance)
/*  9383 */           return 837; 
/*  9384 */         chance += 10.0F;
/*  9385 */         if (rand <= chance)
/*  9386 */           return 694; 
/*  9387 */         chance += 10.0F;
/*  9388 */         if (rand <= chance)
/*  9389 */           return 698; 
/*  9390 */         chance += 2.0F;
/*  9391 */         if (rand <= chance)
/*  9392 */           return Item.getRandomImbuePotionTemplateId(); 
/*  9393 */         chance += 0.1F;
/*  9394 */         if (rand <= chance)
/*  9395 */           return 867; 
/*  9396 */         chance += 0.05F;
/*  9397 */         if (rand <= chance)
/*  9398 */           return 668; 
/*  9399 */         chance += 0.01F;
/*  9400 */         if (rand <= chance)
/*  9401 */           return 986; 
/*  9402 */         chance += 0.01F;
/*  9403 */         if (rand <= chance)
/*  9404 */           return 975; 
/*  9405 */         chance += 0.025F;
/*  9406 */         if (rand <= chance)
/*  9407 */           return 980; 
/*  9408 */         chance += 0.1F;
/*  9409 */         if (rand <= chance)
/*  9410 */           return 998; 
/*  9411 */         chance += 0.1F;
/*  9412 */         if (rand <= chance)
/*  9413 */           return 977; 
/*  9414 */         chance += 0.5F;
/*  9415 */         if (rand <= chance)
/*       */         {
/*  9417 */           switch (Server.rand.nextInt(4)) {
/*       */             
/*       */             case 0:
/*  9420 */               return 983;
/*       */             case 1:
/*  9422 */               return 981;
/*       */             case 2:
/*  9424 */               return 984;
/*       */             case 3:
/*  9426 */               return 982;
/*       */           } 
/*       */ 
/*       */         
/*       */         }
/*  9431 */         chance++;
/*  9432 */         if (rand <= chance)
/*  9433 */           return 666; 
/*  9434 */         return 666;
/*       */ 
/*       */       
/*       */       case 1098:
/*  9438 */         switch (Server.rand.nextInt(8)) {
/*       */           
/*       */           case 0:
/*  9441 */             templateId = 27;
/*       */             break;
/*       */           case 1:
/*  9444 */             templateId = 7;
/*       */             break;
/*       */           case 2:
/*  9447 */             templateId = 62;
/*       */             break;
/*       */           case 3:
/*  9450 */             templateId = 392;
/*       */             break;
/*       */           case 4:
/*  9453 */             templateId = 24;
/*       */             break;
/*       */           case 5:
/*  9456 */             templateId = 25;
/*       */             break;
/*       */           case 6:
/*  9459 */             templateId = 20;
/*       */             break;
/*       */           case 7:
/*  9462 */             templateId = 388;
/*       */             break;
/*       */         } 
/*       */ 
/*       */         
/*       */         break;
/*       */     } 
/*       */ 
/*       */     
/*  9471 */     return templateId;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static boolean handleRecipe(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter, Recipe recipe) {
/*  9477 */     if (performer.getVehicle() != -10L) {
/*       */       
/*  9479 */       performer.getCommunicator().sendNormalServerMessage("You need to be on solid ground to do that.");
/*  9480 */       return true;
/*       */     } 
/*       */     
/*  9483 */     int swapped = Recipes.isRecipeOk(performer.getWurmId(), recipe, source, target, true, true);
/*  9484 */     if (swapped == 0) {
/*       */       
/*  9486 */       performer.getCommunicator().sendNormalServerMessage("Insufficient materials to make " + recipe.getRecipeName() + ".");
/*  9487 */       return true;
/*       */     } 
/*       */ 
/*       */     
/*  9491 */     Item realSource = (swapped != 2) ? (recipe.hasActiveItem() ? source : null) : target;
/*  9492 */     Item realTarget = (swapped != 2) ? target : source;
/*  9493 */     int chance = (int)recipe.getChanceFor(realSource, realTarget, performer);
/*  9494 */     if (chance <= 5) {
/*       */       
/*  9496 */       performer.getCommunicator().sendNormalServerMessage("This is impossible, perhaps you are not skilled enough.");
/*       */       
/*  9498 */       return true;
/*       */     } 
/*       */     
/*  9501 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*       */       
/*  9503 */       performer.getCommunicator().sendNormalServerMessage("Your inventory is full.");
/*  9504 */       return true;
/*       */     } 
/*  9506 */     if (realSource != null && realSource.isCookingTool() && realTarget.isMilk() && realTarget.getWeightGrams() < 600) {
/*       */       
/*  9508 */       performer.getCommunicator().sendNormalServerMessage("The " + realTarget.getNameWithGenus() + " contains too little material to create " + recipe
/*  9509 */           .getResultNameWithGenus(realTarget) + ".  Try to combine it with a similar object to get a larger amount.");
/*       */       
/*  9511 */       return true;
/*       */     } 
/*       */ 
/*       */     
/*  9515 */     if (recipe.getResultItem().isLiquid() && !realTarget.isContainerLiquid() && !realSource.isContainerLiquid()) {
/*       */       
/*  9517 */       Item parent = realTarget.getParentOrNull();
/*  9518 */       if (parent == null || !parent.isContainerLiquid()) {
/*       */         
/*  9520 */         performer.getCommunicator().sendNormalServerMessage("The " + realTarget.getName() + " needs to be in a container that can hold liquids.");
/*       */         
/*  9522 */         return true;
/*       */       } 
/*       */     } 
/*  9525 */     if (realSource != null && (realSource.getTemplateId() == 169 || realSource.getTemplateId() == 1104))
/*       */     {
/*       */       
/*  9528 */       if (realSource.getTopParent() != performer.getInventory().getWurmId()) {
/*       */         
/*  9530 */         performer.getCommunicator().sendNormalServerMessage("You need to be carrying the " + realSource
/*  9531 */             .getName() + " to use it.");
/*  9532 */         return true;
/*       */       } 
/*       */     }
/*       */     
/*  9536 */     boolean noRarity = recipe.getResultItem().isLiquid();
/*       */     
/*  9538 */     Skills skills = performer.getSkills();
/*  9539 */     Skill primSkill = skills.getSkillOrLearn(recipe.getSkillId());
/*  9540 */     ItemTemplate template = recipe.getResultTemplate(realTarget);
/*       */     
/*  9542 */     int time = 10;
/*  9543 */     if (counter == 1.0F) {
/*       */       
/*  9545 */       int start = 150;
/*       */ 
/*       */       
/*  9548 */       realTarget.setBusy(true);
/*  9549 */       if (realSource != null) {
/*  9550 */         realSource.setBusy(true);
/*       */       }
/*       */       try {
/*  9553 */         time = Actions.getRecipeCreationTime(150, performer, primSkill, recipe, realSource, realTarget, template
/*  9554 */             .isMassProduction());
/*       */       }
/*  9556 */       catch (NoSuchTemplateException nst) {
/*       */         
/*  9558 */         if (realSource != null) {
/*  9559 */           logger.log(Level.WARNING, "No template when creating with " + realSource
/*  9560 */               .getName() + " and " + realTarget.getName() + "." + nst
/*  9561 */               .getMessage(), (Throwable)nst);
/*       */         } else {
/*  9563 */           logger.log(Level.WARNING, "No template when creating " + realTarget
/*  9564 */               .getName() + "." + nst
/*  9565 */               .getMessage(), (Throwable)nst);
/*  9566 */         }  performer.getCommunicator().sendSafeServerMessage("You cannot create that item right now. Please contact administrators.");
/*       */         
/*  9568 */         return true;
/*       */       } 
/*  9570 */       act.setTimeLeft(time);
/*  9571 */       performer.sendActionControl(Actions.actionEntrys[148].getVerbString() + " " + template.getName(), true, time);
/*       */       
/*  9573 */       if (realSource != null) {
/*       */         
/*  9575 */         performer.getCommunicator().sendNormalServerMessage("You start to work with the " + realSource
/*  9576 */             .getName() + " on the " + realTarget.getName() + ".");
/*  9577 */         Server.getInstance().broadCastAction(performer
/*  9578 */             .getName() + " starts working with the " + realSource.getName() + " on the " + realTarget
/*  9579 */             .getName() + ".", performer, 5);
/*  9580 */         playToolSound(performer, realSource);
/*       */       }
/*       */       else {
/*       */         
/*  9584 */         performer.getCommunicator().sendNormalServerMessage("You start to work on the " + realTarget
/*  9585 */             .getName() + ".");
/*  9586 */         Server.getInstance().broadCastAction(performer
/*  9587 */             .getName() + " starts working on the " + realTarget
/*  9588 */             .getName() + ".", performer, 5);
/*       */       } 
/*  9590 */       performer.getStatus().modifyStamina(-400.0F);
/*  9591 */       if (realSource.isCookingTool())
/*  9592 */         realSource.setDamage(realSource.getDamage() + 0.0025F * realSource.getDamageModifier()); 
/*  9593 */       return false;
/*       */     } 
/*  9595 */     if (act.currentSecond() % 5 == 0) {
/*       */       
/*  9597 */       performer.getStatus().modifyStamina(-400.0F);
/*  9598 */       if (realSource != null) {
/*       */         
/*  9600 */         playToolSound(performer, realSource);
/*  9601 */         if (realSource.isCookingTool())
/*  9602 */           realSource.setDamage(realSource.getDamage() + 0.0025F * realSource.getDamageModifier()); 
/*       */       } 
/*       */     } 
/*  9605 */     time = act.getTimeLeft();
/*  9606 */     if (counter * 10.0F > time) {
/*       */       
/*  9608 */       if (act.getRarity() != 0 && !noRarity)
/*       */       {
/*  9610 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/*  9612 */       double bonus = performer.getVillageSkillModifier();
/*  9613 */       float skillMultiplier = Math.min(Math.max(1.0F, counter / 3.0F), 20.0F);
/*  9614 */       performer.sendToLoggers("Skill multiplier=" + skillMultiplier, (byte)3);
/*  9615 */       float power = 1.0F;
/*  9616 */       float alc = 0.0F;
/*  9617 */       if (performer.isPlayer())
/*  9618 */         alc = ((Player)performer).getAlcohol(); 
/*  9619 */       int diff = recipe.getDifficulty(realTarget);
/*  9620 */       if (realSource == null || realSource.isBodyPart()) {
/*  9621 */         power = (float)primSkill.skillCheck((diff + alc), null, bonus, false, skillMultiplier);
/*       */       } else {
/*  9623 */         power = (float)primSkill.skillCheck((diff + alc), realSource, bonus, false, skillMultiplier);
/*       */       } 
/*  9625 */       boolean chefMade = false;
/*  9626 */       if (performer.isRoyalChef())
/*  9627 */         chefMade = true; 
/*  9628 */       byte material = recipe.getResultMaterial(realTarget);
/*  9629 */       Item newItem = null;
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       try {
/*  9635 */         double avgQL = MethodsItems.getAverageQL(realSource, realTarget);
/*  9636 */         double ql = Math.min(100.0D, Math.max(1.0D, avgQL + (power / 10.0F)));
/*  9637 */         if (chefMade) {
/*  9638 */           ql = Math.max(30.0D, ql);
/*       */         }
/*  9640 */         float maxMod = 1.0F;
/*  9641 */         if (template.isLowNutrition()) {
/*  9642 */           maxMod = 4.0F;
/*  9643 */         } else if (template.isMediumNutrition()) {
/*  9644 */           maxMod = 3.0F;
/*  9645 */         } else if (template.isGoodNutrition()) {
/*  9646 */           maxMod = 2.0F;
/*  9647 */         } else if (template.isHighNutrition()) {
/*  9648 */           maxMod = 1.0F;
/*  9649 */         }  ql = Math.max(1.0D, Math.min(primSkill.getKnowledge(0.0D) * maxMod, ql));
/*  9650 */         boolean showOwner = (primSkill.getKnowledge(0.0D) > 70.0D);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  9660 */         byte sourceRarity = (realSource == null) ? 0 : realSource.getRarity();
/*  9661 */         if (act.getRarity() > 0 || sourceRarity > 0 || realTarget.getRarity() > 0)
/*       */         {
/*       */           
/*  9664 */           ql = GeneralUtilities.calcRareQuality(ql, act.getRarity(), sourceRarity, realTarget.getRarity());
/*       */         }
/*  9666 */         String owner = showOwner ? performer.getName() : null;
/*  9667 */         newItem = ItemFactory.createItem(template.getTemplateId(), (float)ql, material, noRarity ? 0 : act
/*  9668 */             .getRarity(), owner);
/*  9669 */         newItem.setIsSalted(getSalted(realSource, realTarget));
/*  9670 */         int createdCount = 1;
/*  9671 */         int newWeight = template.getWeightGrams();
/*       */         
/*  9673 */         if (!recipe.useResultTemplateWeight())
/*       */         {
/*       */ 
/*       */           
/*  9677 */           if (realTarget.isFoodMaker()) {
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  9682 */             newWeight = 0;
/*  9683 */             int liquid = 0;
/*  9684 */             for (Item item : realTarget.getItemsAsArray()) {
/*       */               
/*  9686 */               if (item.isLiquid()) {
/*       */                 
/*  9688 */                 Ingredient ii = recipe.findMatchingIngredient(item);
/*  9689 */                 if (ii != null) {
/*  9690 */                   liquid = (int)(liquid + item.getWeightGrams() * (100 - ii.getLoss()) / 100.0F);
/*       */                 }
/*       */               } else {
/*  9693 */                 newWeight += item.getWeightGrams();
/*       */               } 
/*  9695 */             }  newWeight += liquid;
/*  9696 */             newItem.setWeight(newWeight, true);
/*       */           }
/*  9698 */           else if (realSource == null || !realSource.isCookingTool() || 
/*  9699 */             !recipe.useResultTemplateWeight() || template.isLiquid()) {
/*       */ 
/*       */ 
/*       */             
/*  9703 */             if (realSource == null || realSource.getTemplateId() != 202 || realTarget
/*  9704 */               .getTemplateId() != 1238)
/*       */             {
/*       */ 
/*       */ 
/*       */               
/*  9709 */               if (realSource != null && realSource.isCookingTool()) {
/*       */ 
/*       */                 
/*  9712 */                 newWeight = (int)(realTarget.getWeightGrams() * (100 - recipe.getTargetItem().getLoss()) / 100.0F);
/*  9713 */                 newItem.setWeight(newWeight, true);
/*       */               }
/*  9715 */               else if (realSource != null && realSource.getTemplateId() == 688 && realTarget.isCorpse()) {
/*       */ 
/*       */ 
/*       */                 
/*  9719 */                 newWeight = (int)(Math.sqrt(((realSource.getWeightGrams() + realTarget.getWeightGrams()) / 1000)) * 1000.0D);
/*  9720 */                 newItem.setWeight(newWeight, true);
/*       */               
/*       */               }
/*       */               else {
/*       */                 
/*  9725 */                 newWeight = realTarget.getWeightGrams();
/*  9726 */                 if (realSource != null)
/*       */                 {
/*  9728 */                   newWeight += recipe.getUsedActiveItemWeightGrams(realSource, realTarget);
/*       */                 }
/*  9730 */                 newItem.setWeight(newWeight, true);
/*       */               }  } 
/*       */           }  } 
/*  9733 */         if (newWeight >= 0) {
/*       */ 
/*       */           
/*  9736 */           if (template.getWeightGrams() != newWeight) {
/*  9737 */             MethodsItems.setSizes(realTarget, newWeight, newItem);
/*       */           }
/*       */         } else {
/*       */           
/*  9741 */           performer.getCommunicator().sendNormalServerMessage("Not enough of " + realTarget
/*  9742 */               .getName() + " to make " + newItem.getName() + ".");
/*  9743 */           return true;
/*       */         } 
/*       */         
/*  9746 */         if (RecipesByPlayer.saveRecipe(performer, recipe, performer.getWurmId(), realSource, realTarget)) {
/*  9747 */           performer.getCommunicator().sendServerMessage("Recipe \"" + recipe.getName() + "\" added to your cookbook.", 216, 165, 32, (byte)2);
/*       */         }
/*       */         
/*  9750 */         newItem.calculateAndSaveNutrition(realSource, realTarget, recipe);
/*       */         
/*  9752 */         recipe.addAchievements(performer, newItem);
/*  9753 */         if (newItem.getRarity() > 2) {
/*  9754 */           performer.achievement(300);
/*  9755 */         } else if (newItem.getRarity() > 1) {
/*  9756 */           performer.achievement(302);
/*  9757 */         } else if (newItem.getRarity() > 0) {
/*  9758 */           performer.achievement(301);
/*       */         } 
/*  9760 */         newItem.setName(recipe.getResultName(realTarget));
/*       */         
/*  9762 */         ItemTemplate rit = recipe.getResultRealTemplate(realTarget);
/*  9763 */         if (rit != null)
/*  9764 */           newItem.setRealTemplate(rit.getTemplateId()); 
/*  9765 */         if (recipe.hasResultState()) {
/*  9766 */           newItem.setAuxData(recipe.getResultState());
/*       */         }
/*       */ 
/*       */ 
/*       */         
/*  9771 */         String newNameWithGenus = recipe.getResultNameWithGenus(realTarget);
/*  9772 */         String newName = recipe.getResultName(realTarget);
/*  9773 */         if (newItem.isLiquid()) {
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  9778 */           if (realSource != null && realTarget.getTemplateId() == 768 && (realSource
/*  9779 */             .getTemplateId() == 169 || realSource.getTemplateId() == 1104))
/*       */           {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  9786 */             realTarget.closeAll();
/*  9787 */             for (Item item : realTarget.getItemsAsArray())
/*       */             {
/*  9789 */               Items.destroyItem(item.getWurmId());
/*       */             }
/*  9791 */             MethodsItems.fillContainer(act, realTarget, newItem, performer, true);
/*  9792 */             if (!newItem.deleted && newItem.getParentId() == -10L) {
/*       */               
/*  9794 */               performer.getCommunicator().sendNormalServerMessage("Not all the " + newItem
/*  9795 */                   .getName() + " would fit in the " + realTarget.getName() + ".");
/*  9796 */               Items.decay(newItem.getWurmId(), newItem.getDbStrings());
/*  9797 */               newItem = null;
/*       */             } 
/*       */ 
/*       */             
/*  9801 */             realSource.setAuxData((byte)(int)primSkill.getKnowledge(0.0D));
/*  9802 */             realTarget.insertItem(realSource, true);
/*       */             
/*  9804 */             realTarget.setIsSealedByPlayer(true);
/*       */           }
/*       */           else
/*       */           {
/*  9808 */             Item parent = realTarget;
/*  9809 */             if (realSource.isContainerLiquid())
/*       */             {
/*  9811 */               parent = realSource;
/*       */             }
/*  9813 */             if (!parent.isContainerLiquid())
/*       */             {
/*  9815 */               parent = parent.getParentOrNull();
/*       */             }
/*  9817 */             if (parent != null)
/*       */             {
/*  9819 */               if (realTarget.isFoodMaker()) {
/*       */ 
/*       */                 
/*  9822 */                 for (Item item : realTarget.getItemsAsArray())
/*       */                 {
/*  9824 */                   Items.destroyItem(item.getWurmId());
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*       */               else {
/*       */                 
/*  9831 */                 if (realSource != null && !realSource.isCookingTool() && !realSource.isRecipeItem()) {
/*       */                   
/*  9833 */                   int sWeight = recipe.getUsedActiveItemWeightGrams(realSource, realTarget);
/*  9834 */                   realSource.setWeight(realSource.getWeightGrams() - sWeight, true);
/*       */                 } 
/*  9836 */                 Items.destroyItem(realTarget.getWurmId());
/*       */               } 
/*  9838 */               MethodsItems.fillContainer(act, parent, newItem, performer, true);
/*  9839 */               if (!newItem.deleted && newItem.getParentId() == -10L)
/*       */               {
/*  9841 */                 performer.getCommunicator().sendNormalServerMessage("Not all the " + newItem
/*  9842 */                     .getName() + " would fit in the " + parent.getName() + ".");
/*  9843 */                 Items.decay(newItem.getWurmId(), newItem.getDbStrings());
/*  9844 */                 newItem = null;
/*       */               }
/*       */             
/*       */             }
/*       */             else
/*       */             {
/*  9850 */               performer.getInventory().insertItem(newItem, true);
/*       */             
/*       */             }
/*       */           
/*       */           }
/*       */         
/*       */         }
/*  9857 */         else if (realTarget.isFoodMaker()) {
/*       */           
/*  9859 */           for (Item item : realTarget.getItemsAsArray())
/*       */           {
/*  9861 */             Items.destroyItem(item.getWurmId());
/*       */           }
/*  9863 */           newItem.setLastOwnerId(performer.getWurmId());
/*  9864 */           realTarget.insertItem(newItem);
/*       */         }
/*  9866 */         else if (realSource != null && realSource.isCookingTool() && recipe
/*  9867 */           .useResultTemplateWeight() && !template.isLiquid()) {
/*       */ 
/*       */ 
/*       */           
/*  9871 */           realTarget.setWeight(realTarget.getWeightGrams() - recipe.getTargetLossWeight(realTarget), true);
/*  9872 */           performer.getInventory().insertItem(newItem, true);
/*       */         }
/*  9874 */         else if (realSource != null && realSource.isLiquid()) {
/*       */ 
/*       */           
/*  9877 */           realSource.setWeight(realSource.getWeightGrams() - realTarget.getWeightGrams(), true);
/*  9878 */           Items.destroyItem(realTarget.getWurmId());
/*  9879 */           performer.getInventory().insertItem(newItem, true);
/*       */         
/*       */         }
/*       */         else {
/*       */           
/*  9884 */           Item c = realTarget.getParentOrNull();
/*  9885 */           if (c != null) {
/*       */             
/*  9887 */             Items.destroyItem(realTarget.getWurmId());
/*  9888 */             if (!c.insertItem(newItem)) {
/*       */ 
/*       */ 
/*       */               
/*  9892 */               c = c.getParentOrNull();
/*  9893 */               if (c == null || !c.insertItem(newItem)) {
/*       */                 
/*  9895 */                 c = performer.getInventory();
/*  9896 */                 c.insertItem(newItem, true);
/*       */               } 
/*       */             } 
/*       */             
/*  9900 */             if (realSource != null && realSource.getTemplateId() == 688) {
/*  9901 */               Items.destroyItem(realSource.getWurmId());
/*       */             }
/*       */             
/*  9904 */             if (realTarget.getTemplateId() == 1238 && newItem.getTemplateId() == 349) {
/*  9905 */               for (int x = 20; x < 100; x += 30) {
/*  9906 */                 power = (float)primSkill.skillCheck((diff + alc), realSource, bonus, false, skillMultiplier);
/*       */                 
/*  9908 */                 ql = Math.min(99.0F, Math.max(1.0F, realTarget.getCurrentQualityLevel() + power / 2.0F));
/*       */                 
/*  9910 */                 ql = Math.max(1.0D, Math.min(ql, realTarget.getCurrentQualityLevel()));
/*  9911 */                 if (act.getRarity() > 0 || sourceRarity > 0 || realTarget.getRarity() > 0)
/*       */                 {
/*  9913 */                   ql = GeneralUtilities.calcRareQuality(ql, act.getRarity(), sourceRarity, realTarget.getRarity());
/*       */                 }
/*       */                 
/*  9916 */                 if (performer.getInventory().getNumItemsNotCoins() >= 100) {
/*  9917 */                   performer.getCommunicator().sendNormalServerMessage("You do not have space for any more salt.");
/*       */                   
/*  9919 */                   return true;
/*       */                 } 
/*  9921 */                 newItem = ItemFactory.createItem(template.getTemplateId(), (float)ql, material, act
/*  9922 */                     .getRarity(), performer.getName());
/*  9923 */                 createdCount++;
/*  9924 */                 c.insertItem(newItem);
/*       */                 
/*  9926 */                 int skillLevel = (int)primSkill.getKnowledge(0.0D) - x;
/*  9927 */                 if (Server.rand.nextInt(100) > skillLevel) {
/*       */                   break;
/*       */                 }
/*       */               }
/*       */             
/*       */             }
/*       */           }
/*       */           else {
/*       */             
/*  9936 */             performer.getInventory().insertItem(newItem, true);
/*       */           } 
/*       */         } 
/*       */ 
/*       */         
/*  9941 */         if (recipe.hasActiveItem())
/*       */         {
/*  9943 */           if (recipe.getActiveItem().getTemplateId() != 14 && realSource != null && !realSource.isLiquid() && !realSource.isCookingTool() && !realSource.isRecipeItem())
/*       */           {
/*  9945 */             realSource.setWeight(realSource.getWeightGrams() - realSource.getTemplate().getWeightGrams(), true);
/*       */           }
/*       */         }
/*  9948 */         if (createdCount > 1) {
/*  9949 */           performer.getCommunicator().sendNormalServerMessage("You created " + createdCount + " " + newName + ".");
/*       */         } else {
/*  9951 */           performer.getCommunicator().sendNormalServerMessage("You create " + newNameWithGenus + ".");
/*       */         } 
/*  9953 */       } catch (FailedException fe) {
/*       */ 
/*       */         
/*  9956 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*       */       }
/*  9958 */       catch (NoSuchTemplateException nste) {
/*       */ 
/*       */         
/*  9961 */         logger.log(Level.WARNING, nste.getMessage(), (Throwable)nste);
/*       */       } 
/*  9963 */       return true;
/*       */     } 
/*  9965 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   static void playToolSound(Creature performer, Item source) {
/*  9970 */     switch (source.getTemplateId()) {
/*       */       
/*       */       case 93:
/*  9973 */         SoundPlayer.playSound("sound.butcherKnife", performer, 1.0F);
/*       */         break;
/*       */       case 259:
/*  9976 */         SoundPlayer.playSound("sound.forkMix", performer, 1.0F);
/*       */         break;
/*       */       case 1237:
/*  9979 */         SoundPlayer.playSound("sound.grindSpice", performer, 1.0F);
/*       */         break;
/*       */       case 202:
/*  9982 */         SoundPlayer.playSound("sound.grindstone", performer, 1.0F);
/*       */         break;
/*       */       case 258:
/*  9985 */         SoundPlayer.playSound("sound.knifeChop", performer, 1.0F);
/*       */         break;
/*       */       case 413:
/*       */       case 747:
/*  9989 */         SoundPlayer.playSound("sound.press", performer, 1.0F);
/*       */         break;
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
/*       */   public static boolean getSalted(@Nullable Item source, Item target) {
/* 10003 */     if (source != null && (source.isFood() || source.isLiquid()))
/*       */     {
/* 10005 */       if (source.isSalted() || source.getTemplateId() == 349)
/* 10006 */         return true; 
/*       */     }
/* 10008 */     if (target.isFoodMaker()) {
/*       */       
/* 10010 */       for (Item item : target.getItemsAsArray()) {
/*       */         
/* 10012 */         if (item.isSalted() || item.getTemplateId() == 349) {
/* 10013 */           return true;
/*       */         }
/*       */       } 
/* 10016 */     } else if (target.isFood() || target.isLiquid()) {
/*       */       
/* 10018 */       if (target.isSalted() || target.getTemplateId() == 349)
/* 10019 */         return true; 
/*       */     } 
/* 10021 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private void showRecipeInfo(Creature performer, Item source, Item target) {
/* 10026 */     if ((target.getTemplate().isCooker() || target.isFoodMaker()) && target.isEmpty(true)) {
/*       */       
/* 10028 */       performer.getCommunicator().sendNormalServerMessage("This can be used for cooking many wonderful things.");
/*       */     }
/* 10030 */     else if (target.getTemplateId() == 1178 && target.isEmpty(true)) {
/*       */       
/* 10032 */       performer.getCommunicator().sendNormalServerMessage("This can be used for distilling many wonderful things.");
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 10037 */       Item realTarget = target;
/* 10038 */       if (target.getTemplateId() == 1178)
/*       */       {
/*       */         
/* 10041 */         for (Item item : target.getItemsAsArray()) {
/*       */           
/* 10043 */           if (item.getTemplateId() == 1284) {
/*       */             
/* 10045 */             realTarget = item;
/*       */             break;
/*       */           } 
/*       */         } 
/*       */       }
/* 10050 */       boolean foundHeat = showRecipeInfo(performer, source, target, realTarget, (byte)1);
/* 10051 */       boolean foundCreate = showRecipeInfo(performer, source, target, realTarget, (byte)2);
/* 10052 */       boolean foundTime = showRecipeInfo(performer, source, target, realTarget, (byte)0);
/* 10053 */       if (!foundHeat && !foundCreate && !foundTime) {
/*       */ 
/*       */         
/* 10056 */         recipeRandom.setSeed(performer.getWurmId());
/*       */         
/* 10058 */         if (recipeRandom.nextBoolean()) {
/*       */           
/* 10060 */           foundHeat = showPartialRecipeInfo(performer, source, target, realTarget, (byte)1);
/* 10061 */           if (!foundHeat) {
/* 10062 */             foundCreate = showPartialRecipeInfo(performer, source, target, realTarget, (byte)2);
/*       */           }
/*       */         } else {
/*       */           
/* 10066 */           foundCreate = showPartialRecipeInfo(performer, source, target, realTarget, (byte)2);
/* 10067 */           if (!foundCreate)
/* 10068 */             foundHeat = showPartialRecipeInfo(performer, source, target, realTarget, (byte)1); 
/*       */         } 
/* 10070 */         if (!foundHeat && !foundCreate)
/* 10071 */           foundTime = showPartialRecipeInfo(performer, source, target, realTarget, (byte)0); 
/* 10072 */         if (!foundHeat && !foundCreate && !foundTime) {
/* 10073 */           performer.getCommunicator().sendNormalServerMessage("The items inside do not make any known recipe.");
/*       */         }
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean showRecipeInfo(Creature performer, @Nullable Item source, Item target, Item realTarget, byte wantedType) {
/* 10082 */     Recipe eRecipe = Recipes.getRecipeFor(performer.getWurmId(), wantedType, source, realTarget, false, true);
/* 10083 */     if (eRecipe != null) {
/*       */       
/* 10085 */       if (RecipesByPlayer.isKnownRecipe(performer.getWurmId(), eRecipe.getRecipeId())) {
/*       */         
/* 10087 */         if (wantedType == 1) {
/*       */           
/* 10089 */           String needs = " when cooked.";
/*       */           
/* 10091 */           Item cooker = target.getTopParentOrNull();
/* 10092 */           if (cooker != null && cooker.getTemplate().isCooker()) {
/*       */ 
/*       */             
/* 10095 */             if (!eRecipe.hasCooker(cooker.getTemplateId())) {
/* 10096 */               needs = " when cooked in a different cooker.";
/*       */             }
/*       */           } else {
/* 10099 */             needs = " when cooked in a cooker.";
/* 10100 */           }  performer.getCommunicator().sendNormalServerMessage("The ingredients in the " + target.getName() + " would make " + eRecipe
/* 10101 */               .getResultNameWithGenus(realTarget) + needs);
/*       */         }
/* 10103 */         else if (wantedType == 0) {
/* 10104 */           performer.getCommunicator().sendNormalServerMessage("The ingredients in the " + target.getName() + " would make " + eRecipe
/* 10105 */               .getResultNameWithGenus(realTarget) + " given time.");
/*       */         } else {
/* 10107 */           performer.getCommunicator().sendNormalServerMessage("The ingredients in the " + target.getName() + " could make " + eRecipe
/* 10108 */               .getResultNameWithGenus(realTarget) + ((eRecipe
/* 10109 */               .hasActiveItem() && eRecipe.getActiveItem().getTemplateId() != 14) ? (" if you used " + 
/* 10110 */               StringUtilities.addGenus(eRecipe.getActiveItemName()) + ".") : "."));
/*       */         
/*       */         }
/*       */       
/*       */       }
/* 10115 */       else if (wantedType == 1) {
/*       */         
/* 10117 */         String needs = "when cooked.";
/*       */         
/* 10119 */         Item cooker = target.getTopParentOrNull();
/* 10120 */         if (cooker != null && cooker.getTemplate().isCooker()) {
/*       */ 
/*       */           
/* 10123 */           if (!eRecipe.hasCooker(cooker.getTemplateId())) {
/* 10124 */             needs = "when cooked in a different cooker.";
/*       */           }
/*       */         } else {
/* 10127 */           needs = "when cooked in a cooker.";
/* 10128 */         }  performer.getCommunicator().sendNormalServerMessage("You think this may well work " + needs);
/*       */       }
/* 10130 */       else if (wantedType == 0) {
/* 10131 */         performer.getCommunicator().sendNormalServerMessage("You think this may well work given time.");
/*       */       } else {
/* 10133 */         performer.getCommunicator().sendNormalServerMessage("You think this may well work" + ((eRecipe
/* 10134 */             .hasActiveItem() && eRecipe.getActiveItem().getTemplateId() != 14) ? (" if you used " + 
/* 10135 */             StringUtilities.addGenus(eRecipe.getActiveItemName()) + ".") : "."));
/*       */       } 
/*       */       
/* 10138 */       if (performer.getPower() > 1 || performer.hasFlag(51)) {
/* 10139 */         performer.getCommunicator().sendNormalServerMessage("(Recipe Id:" + eRecipe.getRecipeId() + ", Current difficulty:" + eRecipe.getDifficulty(target) + ")");
/*       */       } else {
/* 10141 */         performer.getCommunicator().sendNormalServerMessage("Current difficulty:" + eRecipe.getDifficulty(target) + ".");
/* 10142 */       }  return true;
/*       */     } 
/*       */     
/* 10145 */     Recipe lRecipe = Recipes.getRecipeFor(performer.getWurmId(), wantedType, source, realTarget, false, false);
/* 10146 */     if (lRecipe != null) {
/*       */ 
/*       */       
/* 10149 */       Recipe.LiquidResult liquidResult = lRecipe.getNewWeightGrams(target);
/* 10150 */       if (!liquidResult.isSuccess()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 10156 */         if (RecipesByPlayer.isKnownRecipe(performer.getWurmId(), lRecipe.getRecipeId())) {
/*       */           
/* 10158 */           if (wantedType == 1) {
/* 10159 */             performer.getCommunicator().sendNormalServerMessage("The ingredients in the " + target.getName() + " would make " + lRecipe
/* 10160 */                 .getResultNameWithGenus(realTarget) + " when cooked, but...");
/* 10161 */           } else if (wantedType == 0) {
/* 10162 */             performer.getCommunicator().sendNormalServerMessage("The ingredients in the " + target.getName() + " would make " + lRecipe
/* 10163 */                 .getResultNameWithGenus(realTarget) + " given time, but...");
/*       */           } else {
/* 10165 */             performer.getCommunicator().sendNormalServerMessage("The ingredients in the " + target.getName() + " would make " + lRecipe
/* 10166 */                 .getResultNameWithGenus(realTarget) + ((lRecipe
/* 10167 */                 .hasActiveItem() && lRecipe.getActiveItem().getTemplateId() != 14) ? (" if you used " + 
/* 10168 */                 StringUtilities.addGenus(lRecipe.getActiveItemName())) : "") + ", but...");
/*       */           
/*       */           }
/*       */ 
/*       */         
/*       */         }
/* 10174 */         else if (wantedType == 1) {
/* 10175 */           performer.getCommunicator().sendNormalServerMessage("You think this may well work when cooked, but...");
/* 10176 */         } else if (wantedType == 0) {
/* 10177 */           performer.getCommunicator().sendNormalServerMessage("You think this may well work given time, but...");
/*       */         } else {
/* 10179 */           performer.getCommunicator().sendNormalServerMessage("You think this may well work" + ((lRecipe
/* 10180 */               .hasActiveItem() && lRecipe.getActiveItem().getTemplateId() != 14) ? (" if you used " + 
/* 10181 */               StringUtilities.addGenus(lRecipe.getActiveItemName())) : "") + ", but...");
/*       */         } 
/*       */ 
/*       */         
/* 10185 */         for (String error : liquidResult.getErrors().values())
/*       */         {
/* 10187 */           performer.getCommunicator().sendNormalServerMessage(error);
/*       */         }
/* 10189 */         if (performer.getPower() > 1 || performer.hasFlag(51)) {
/* 10190 */           performer.getCommunicator().sendNormalServerMessage("(Recipe Id:" + lRecipe.getRecipeId() + ", Current difficulty:" + lRecipe.getDifficulty(target) + ")");
/*       */         } else {
/* 10192 */           performer.getCommunicator().sendNormalServerMessage("Current difficulty:" + lRecipe.getDifficulty(target) + ".");
/* 10193 */         }  return true;
/*       */       } 
/*       */     } 
/* 10196 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean showPartialRecipeInfo(Creature performer, @Nullable Item source, Item target, Item realTarget, byte wantedType) {
/* 10204 */     if (target.isFoodMaker()) {
/*       */       
/* 10206 */       Recipe[] recipes = Recipes.getPartialRecipeListFor(performer, wantedType, realTarget);
/* 10207 */       if (recipes.length > 0) {
/*       */ 
/*       */         
/* 10210 */         List<Recipe> recipesUnknown = new ArrayList<>();
/* 10211 */         List<Recipe> recipesUnNamed = new ArrayList<>();
/* 10212 */         List<Recipe> recipesKnown = new ArrayList<>();
/* 10213 */         List<Recipe> recipesNamed = new ArrayList<>();
/* 10214 */         for (Recipe recipe : recipes) {
/*       */           
/* 10216 */           if (RecipesByPlayer.isKnownRecipe(performer.getWurmId(), recipe.getRecipeId())) {
/*       */             
/* 10218 */             if (recipe.isNameable()) {
/* 10219 */               recipesNamed.add(recipe);
/*       */             } else {
/* 10221 */               recipesKnown.add(recipe);
/*       */             }
/*       */           
/*       */           }
/* 10225 */           else if (recipe.isNameable()) {
/* 10226 */             recipesUnNamed.add(recipe);
/* 10227 */           } else if (!recipe.isLootable()) {
/* 10228 */             recipesUnknown.add(recipe);
/*       */           } 
/*       */         } 
/*       */         
/* 10232 */         if (recipesUnknown.size() > 0) {
/*       */ 
/*       */           
/* 10235 */           Recipe recipe = recipesUnknown.get(recipeRandom.nextInt(recipesUnknown.size()));
/* 10236 */           if (pickRandomIngredient(performer, recipe)) {
/* 10237 */             return true;
/*       */           }
/*       */         } 
/* 10240 */         if (recipesUnNamed.size() > 0) {
/*       */ 
/*       */           
/* 10243 */           Recipe recipe = recipesUnNamed.get(recipeRandom.nextInt(recipesUnNamed.size()));
/* 10244 */           if (pickRandomIngredient(performer, recipe)) {
/* 10245 */             return true;
/*       */           }
/*       */         } 
/* 10248 */         if (recipesKnown.size() > 0) {
/*       */ 
/*       */           
/* 10251 */           Recipe recipe = recipesKnown.get(recipeRandom.nextInt(recipesKnown.size()));
/* 10252 */           if (pickRandomIngredient(performer, recipe)) {
/* 10253 */             return true;
/*       */           }
/*       */         } 
/* 10256 */         if (recipesNamed.size() > 0) {
/*       */ 
/*       */           
/* 10259 */           Recipe recipe = recipesNamed.get(recipeRandom.nextInt(recipesNamed.size()));
/* 10260 */           if (pickRandomIngredient(performer, recipe))
/* 10261 */             return true; 
/*       */         } 
/*       */       } 
/*       */     } 
/* 10265 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean pickRandomIngredient(Creature performer, Recipe recipe) {
/* 10270 */     Ingredient[] ingredients = recipe.getWhatsMissing();
/*       */     
/* 10272 */     if (ingredients.length > 0) {
/*       */ 
/*       */       
/* 10275 */       Ingredient ingredient = ingredients[recipeRandom.nextInt(ingredients.length)];
/*       */       
/* 10277 */       String name = Recipes.getIngredientName(ingredient, false);
/* 10278 */       StringBuilder buf = new StringBuilder("Have you tried ");
/* 10279 */       if (!ingredient.isLiquid()) {
/*       */ 
/*       */         
/* 10282 */         if (ingredient.getFound() == 0) {
/* 10283 */           buf.append("adding " + StringUtilities.addGenus(name));
/* 10284 */         } else if (ingredient.getFound() < ingredient.getAmount()) {
/* 10285 */           buf.append("adding more " + name);
/*       */         } else {
/* 10287 */           buf.append("removing " + StringUtilities.addGenus(name));
/*       */         } 
/*       */       } else {
/*       */         
/* 10291 */         buf.append(StringUtilities.addGenus(name));
/* 10292 */         if (ingredient.getRatio() > 0)
/* 10293 */           buf.append(" (" + ingredient.getRatio() + "% of solids)"); 
/*       */       } 
/* 10295 */       performer.getCommunicator().sendNormalServerMessage(buf.toString() + "?" + ((performer
/* 10296 */           .getPower() > 1 || performer.hasFlag(51)) ? (" (Could make recipe:" + recipe.getRecipeId() + ")") : ""));
/* 10297 */       return true;
/*       */     } 
/* 10299 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private void setVolume(Creature performer, Item target, int auxbyte) {
/* 10304 */     if (target.getTemplateId() == 1172)
/*       */     {
/* 10306 */       if (target.getTopParentOrNull() != performer.getInventory()) {
/* 10307 */         performer.getCommunicator().sendNormalServerMessage("You can only change volume when the " + target.getName() + " is in your inventory.");
/* 10308 */       } else if (!target.isEmpty(false)) {
/* 10309 */         performer.getCommunicator().sendNormalServerMessage("You can only change volume when the " + target.getName() + " is empty.");
/*       */       }
/*       */       else {
/*       */         
/* 10313 */         target.setAuxData((byte)auxbyte);
/* 10314 */         int newVolume = target.setInternalVolumeFromAuxByte();
/* 10315 */         String vm = (newVolume >= 1000) ? ((newVolume / 1000) + "kg") : (newVolume + "g");
/* 10316 */         performer.getCommunicator().sendNormalServerMessage("You carefully adjust the " + target
/* 10317 */             .getName() + " volume by utilising a small wheel on the bottom and set it to " + vm + ".");
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   private void taste(Creature performer, Item target) {
/* 10324 */     float qlevel = target.getCurrentQualityLevel();
/* 10325 */     int temp = target.getTemperature();
/* 10326 */     float nut = target.getNutritionLevel();
/*       */     
/* 10328 */     if (target.getDamage() > 90.0F) {
/*       */       
/* 10330 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " does not taste good at all.");
/*       */       return;
/*       */     } 
/* 10333 */     if (temp > 2500) {
/*       */       
/* 10335 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " would burn your mouth.");
/*       */       return;
/*       */     } 
/* 10338 */     if (target.isCheese() && target.isZombiefied())
/*       */     {
/* 10340 */       if (performer.getKingdomTemplateId() == 3) {
/*       */         
/* 10342 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10343 */             .getName() + " tastes weird, but good.");
/*       */       }
/*       */       else {
/*       */         
/* 10347 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 10348 */             .getName() + " is horrible, and you can't eat it.");
/*       */         
/*       */         return;
/*       */       } 
/*       */     }
/* 10353 */     String tasteString = target.getName().endsWith("s") ? " taste" : " tastes";
/* 10354 */     String toSend = "The " + target.getName();
/* 10355 */     if (qlevel > 50.0F) {
/* 10356 */       toSend = toSend + tasteString + " " + target.getTasteString() + " It also seems";
/*       */     } else {
/* 10358 */       toSend = toSend + tasteString;
/*       */     } 
/* 10360 */     if (nut > 0.9D) {
/* 10361 */       toSend = toSend + " extremely nutritional.";
/* 10362 */     } else if (nut > 0.7D) {
/* 10363 */       toSend = toSend + " quite nutritional.";
/* 10364 */     } else if (nut > 0.5D) {
/* 10365 */       toSend = toSend + " nutritional.";
/* 10366 */     } else if (nut > 0.3D) {
/* 10367 */       toSend = toSend + " not very nutritional.";
/*       */     } else {
/* 10369 */       toSend = toSend + " not at all nutritional.";
/*       */     } 
/* 10371 */     if (target.isSalted())
/* 10372 */       toSend = toSend + " You think it might have some salt in it."; 
/* 10373 */     performer.getCommunicator().sendNormalServerMessage(toSend);
/*       */     
/* 10375 */     SkillTemplate skill = AffinitiesTimed.getTimedAffinitySkill(performer, target);
/* 10376 */     if (skill != null)
/*       */     {
/* 10378 */       performer.getCommunicator().sendNormalServerMessage("You think the " + target.getName() + " might give you more of an insight about " + skill
/* 10379 */           .getName().toLowerCase() + "!");
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   private void tasteLiquid(Creature performer, Item target) {
/* 10385 */     if (!target.isSealedByPlayer()) {
/*       */       
/* 10387 */       performer.getCommunicator().sendNormalServerMessage("Its not sealed, just look inside!");
/*       */       
/*       */       return;
/*       */     } 
/* 10391 */     Item liquid = null;
/* 10392 */     Item[] items = target.getItemsAsArray();
/* 10393 */     for (Item item : items) {
/*       */       
/* 10395 */       if (item.isLiquid()) {
/*       */         
/* 10397 */         liquid = item;
/*       */         break;
/*       */       } 
/*       */     } 
/* 10401 */     if (liquid == null) {
/*       */       
/* 10403 */       performer.getCommunicator().sendNormalServerMessage("There is nothing to taste!", (byte)1);
/*       */       
/*       */       return;
/*       */     } 
/* 10407 */     String rb = "";
/* 10408 */     Recipe recipe = liquid.getRecipe();
/*       */     
/* 10410 */     int skillId = (recipe == null) ? SkillSystem.getSkillByName("natural substances") : recipe.getSkillId();
/*       */     
/* 10412 */     Skill skill = performer.getSkills().getSkillOrLearn(skillId);
/* 10413 */     double knowledge = skill.getKnowledge(0.0D);
/*       */     
/* 10415 */     if (knowledge > 75.0D) {
/*       */       
/* 10417 */       rb = "it has a quality of " + String.format("%.2f", new Object[] { Float.valueOf(liquid.getCurrentQualityLevel()) }) + ".";
/*       */     }
/* 10419 */     else if (knowledge > 40.0D) {
/*       */       
/* 10421 */       int range = (int)liquid.getCurrentQualityLevel() / 5 * 5;
/* 10422 */       rb = "it has a quality in the range of " + range + " to " + (range + 4) + ".";
/*       */     }
/* 10424 */     else if (liquid.getTemplateId() == 128 || liquid.getTemplateId() == 634) {
/*       */       
/* 10426 */       if (liquid.getCurrentQualityLevel() > 90.0F) {
/* 10427 */         rb = "it is very clear.";
/* 10428 */       } else if (liquid.getCurrentQualityLevel() > 70.0F) {
/* 10429 */         rb = "it is clear.";
/* 10430 */       } else if (liquid.getCurrentQualityLevel() > 45.0F) {
/* 10431 */         rb = "it is slightly cloudy.";
/* 10432 */       } else if (liquid.getCurrentQualityLevel() > 20.0F) {
/* 10433 */         rb = "it is cloudy.";
/*       */       } else {
/* 10435 */         rb = "it is very cloudy.";
/*       */       } 
/* 10437 */     } else if (liquid.isAlcohol()) {
/*       */       
/* 10439 */       if (liquid.getCurrentQualityLevel() > 95.0F) {
/* 10440 */         rb = "it is mind blowingly strong.";
/* 10441 */       } else if (liquid.getCurrentQualityLevel() > 80.0F) {
/* 10442 */         rb = "it is very strong.";
/* 10443 */       } else if (liquid.getCurrentQualityLevel() > 65.0F) {
/* 10444 */         rb = "it is strong.";
/* 10445 */       } else if (liquid.getCurrentQualityLevel() > 50.0F) {
/* 10446 */         rb = "it is mostly strong.";
/* 10447 */       } else if (liquid.getCurrentQualityLevel() > 35.0F) {
/* 10448 */         rb = "it has a medium strength.";
/* 10449 */       } else if (liquid.getCurrentQualityLevel() > 20.0F) {
/* 10450 */         rb = "it has a weak strength.";
/*       */       } else {
/* 10452 */         rb = "it has a very weak strength.";
/*       */       } 
/* 10454 */     } else if (liquid.isMilk()) {
/*       */       
/* 10456 */       if (liquid.getCurrentQualityLevel() > 90.0F) {
/* 10457 */         rb = "it is creamy.";
/* 10458 */       } else if (liquid.getCurrentQualityLevel() > 75.0F) {
/* 10459 */         rb = "it is smooth.";
/* 10460 */       } else if (liquid.getCurrentQualityLevel() > 60.0F) {
/* 10461 */         rb = "it is more like semi-skimmed milk.";
/* 10462 */       } else if (liquid.getCurrentQualityLevel() > 40.0F) {
/* 10463 */         rb = "it is more like skimmed milk.";
/* 10464 */       } else if (liquid.getCurrentQualityLevel() > 20.0F) {
/* 10465 */         rb = "it tastes like it has been watered down.";
/*       */       } else {
/* 10467 */         rb = "it tastes like it has passed its use by date.";
/*       */       } 
/* 10469 */     } else if (liquid.isDye()) {
/*       */       
/* 10471 */       if (liquid.getCurrentQualityLevel() > 90.0F) {
/* 10472 */         rb = "it colours your tongue, and strangely tastes excellent.";
/* 10473 */       } else if (liquid.getCurrentQualityLevel() > 75.0F) {
/* 10474 */         rb = "it colours your tongue, and strangely tastes very good.";
/* 10475 */       } else if (liquid.getCurrentQualityLevel() > 60.0F) {
/* 10476 */         rb = "it colours your tongue, and strangely tastes good.";
/* 10477 */       } else if (liquid.getCurrentQualityLevel() > 40.0F) {
/* 10478 */         rb = "it colours your tongue, and strangely tastes average.";
/* 10479 */       } else if (liquid.getCurrentQualityLevel() > 20.0F) {
/* 10480 */         rb = "it colours your tongue, and strangely tastes acceptable.";
/*       */       } else {
/* 10482 */         rb = "it colours your tounge, and strangely tastes poor.";
/*       */       } 
/* 10484 */     } else if (liquid.isDrinkable() || liquid.isFood()) {
/*       */       
/* 10486 */       if (liquid.getCurrentQualityLevel() > 90.0F) {
/* 10487 */         rb = "it is excellent quality.";
/* 10488 */       } else if (liquid.getCurrentQualityLevel() > 75.0F) {
/* 10489 */         rb = "it is very good quality.";
/* 10490 */       } else if (liquid.getCurrentQualityLevel() > 60.0F) {
/* 10491 */         rb = "it is good quality.";
/* 10492 */       } else if (liquid.getCurrentQualityLevel() > 40.0F) {
/* 10493 */         rb = "it is average quality.";
/* 10494 */       } else if (liquid.getCurrentQualityLevel() > 20.0F) {
/* 10495 */         rb = "it is acceptable quality.";
/*       */       } else {
/* 10497 */         rb = "it is poor quality.";
/*       */       
/*       */       }
/*       */     
/*       */     }
/* 10502 */     else if (liquid.getCurrentQualityLevel() > 90.0F) {
/* 10503 */       rb = "it tastes odd but strangely seems excellent.";
/* 10504 */     } else if (liquid.getCurrentQualityLevel() > 75.0F) {
/* 10505 */       rb = "it tastes odd but strangely seems very good.";
/* 10506 */     } else if (liquid.getCurrentQualityLevel() > 60.0F) {
/* 10507 */       rb = "it tastes odd but strangely seems good.";
/* 10508 */     } else if (liquid.getCurrentQualityLevel() > 40.0F) {
/* 10509 */       rb = "it tastes odd but strangely seems average.";
/* 10510 */     } else if (liquid.getCurrentQualityLevel() > 20.0F) {
/* 10511 */       rb = "it tastes odd but strangely seems acceptable.";
/*       */     } else {
/* 10513 */       rb = "it tastes odd but strangely seems poor.";
/*       */     } 
/*       */     
/* 10516 */     if (recipe == null || !recipe.hasDescription()) {
/*       */       
/* 10518 */       performer.getCommunicator().sendNormalServerMessage(liquid.examine(performer) + " Also " + rb);
/*       */     }
/*       */     else {
/*       */       
/* 10522 */       performer.getCommunicator().sendNormalServerMessage(recipe.getResultDescription(target) + " Also " + rb);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private void readVillageMessages(Creature performer, Item target) {
/* 10529 */     if (target.mayAccessHold(performer)) {
/*       */       
/* 10531 */       if (performer.getCurrentVillage() != null) {
/*       */         
/* 10533 */         VillageMessageBoard vmb = new VillageMessageBoard(performer, performer.getCurrentVillage(), target);
/* 10534 */         vmb.sendQuestion();
/*       */       }
/*       */       else {
/*       */         
/* 10538 */         performer.getCommunicator().sendNormalServerMessage("Village mesage board is not in a village.");
/*       */       } 
/*       */     } else {
/*       */       
/* 10542 */       performer.getCommunicator().sendNormalServerMessage("You dont have permission to read this Village mesage board.");
/*       */     } 
/*       */   }
/*       */   
/*       */   private void postVillageMessage(Creature performer, Item source, Item target) {
/* 10547 */     Village village = performer.getCurrentVillage();
/* 10548 */     if (village == null) {
/*       */       
/* 10550 */       performer.getCommunicator().sendNormalServerMessage("You are not stood in a village.");
/*       */       return;
/*       */     } 
/* 10553 */     if (!target.mayAddPMs(performer) && !target.mayPostNotices(performer)) {
/*       */       
/* 10555 */       performer.getCommunicator().sendNormalServerMessage("You do not have permissions to put anything on this vilage notice board.");
/*       */       return;
/*       */     } 
/* 10558 */     if (source.canHaveInscription() && source.getAuxData() == 0) {
/*       */       
/* 10560 */       InscriptionData ins = source.getInscription();
/* 10561 */       if (ins != null && ins.hasBeenInscribed()) {
/*       */ 
/*       */ 
/*       */         
/* 10565 */         VillageMessagePopup vmp = new VillageMessagePopup(performer, village, ins, source.getWurmId(), target);
/* 10566 */         vmp.sendQuestion();
/*       */       }
/*       */       else {
/*       */         
/* 10570 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/* 10571 */             .getName() + " needs to be inscribed before adding to the " + target.getName() + ".");
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private static boolean useRuneOnItem(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 10578 */     if (target.isVehicle())
/*       */     {
/* 10580 */       if (!target.mayManage(performer)) {
/*       */         
/* 10582 */         performer.getCommunicator().sendNormalServerMessage("You do not have permission to use the rune on that item.", (byte)3);
/* 10583 */         return true;
/*       */       } 
/*       */     }
/* 10586 */     if (!Methods.isActionAllowed(performer, (short)192, target.getTileX(), target.getTileY())) {
/*       */       
/* 10588 */       performer.getCommunicator().sendNormalServerMessage("You are not allowed to use that here.", (byte)3);
/* 10589 */       return true;
/*       */     } 
/*       */     
/* 10592 */     if ((RuneUtilities.isEnchantRune(source) || RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_COLOR) > 0.0F) && 
/* 10593 */       !RuneUtilities.canApplyRuneTo(source, target)) {
/*       */       
/* 10595 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the rune on that item.", (byte)3);
/* 10596 */       return true;
/*       */     } 
/* 10598 */     if (RuneUtilities.isSingleUseRune(source) && RuneUtilities.getSpellForRune(source) != null) {
/*       */       
/* 10600 */       if (!Methods.isActionAllowed(performer, (short)245, target.getTileX(), target.getTileY())) {
/*       */         
/* 10602 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to use that here.", (byte)3);
/* 10603 */         return true;
/*       */       } 
/*       */     } else {
/* 10606 */       if (!RuneUtilities.isCorrectTarget(source, target)) {
/*       */         
/* 10608 */         performer.getCommunicator().sendNormalServerMessage("That is the wrong type of rune for that item.", (byte)3);
/* 10609 */         return true;
/*       */       } 
/* 10611 */       if (target.getOwnerId() != -10L && target.getOwnerId() != performer.getWurmId()) {
/*       */         
/* 10613 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to use the rune on that item.", (byte)3);
/* 10614 */         return true;
/*       */       } 
/* 10616 */       if (MethodsItems.checkIfStealing(target, performer, null)) {
/*       */         
/* 10618 */         performer.getCommunicator().sendNormalServerMessage("You do not have permission to use the rune on that item.", (byte)3);
/* 10619 */         return true;
/*       */       } 
/*       */     } 
/* 10622 */     int time = act.getTimeLeft();
/* 10623 */     if (counter == 1.0F) {
/*       */       
/* 10625 */       String actionString = "use the rune on the ";
/* 10626 */       if (RuneUtilities.isEnchantRune(source))
/* 10627 */         actionString = "attach the rune to the "; 
/* 10628 */       performer.getCommunicator().sendNormalServerMessage("You start to " + actionString + target.getName() + ".");
/* 10629 */       time = Actions.getSlowActionTime(performer, performer.getSoulDepth(), null, 0.0D);
/* 10630 */       act.setTimeLeft(time);
/* 10631 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 10632 */       performer.getStatus().modifyStamina(-600.0F);
/*       */     } 
/* 10634 */     if (act.currentSecond() % 5 == 0)
/*       */     {
/* 10636 */       performer.getStatus().modifyStamina(-300.0F);
/*       */     }
/* 10638 */     if (counter * 10.0F > time) {
/*       */       
/* 10640 */       Skill soulDepth = performer.getSoulDepth();
/* 10641 */       double diff = (20.0F + source.getDamage()) - (source.getCurrentQualityLevel() + source.getRarity()) - 45.0D;
/* 10642 */       double power = soulDepth.skillCheck(diff, source.getCurrentQualityLevel(), false, counter);
/* 10643 */       if (power > 0.0D) {
/*       */         
/* 10645 */         if (RuneUtilities.isEnchantRune(source)) {
/*       */ 
/*       */           
/* 10648 */           performer.getCommunicator().sendNormalServerMessage("You successfully attach the rune to the " + target.getName() + ".", (byte)2);
/* 10649 */           performer.achievement(491);
/* 10650 */           ItemSpellEffects effs = target.getSpellEffects();
/* 10651 */           if (effs == null) {
/* 10652 */             effs = new ItemSpellEffects(target.getWurmId());
/*       */           }
/* 10654 */           byte runeEffect = effs.getRandomRuneEffect();
/* 10655 */           while (runeEffect != -10L) {
/*       */             
/* 10657 */             if (RuneUtilities.getModifier(runeEffect, RuneUtilities.ModifierEffect.ENCH_GLOW) > 0.0F) {
/*       */               
/* 10659 */               target.setLightOverride(false);
/* 10660 */               target.setIsAlwaysLit((target.getTemplate()).alwaysLit);
/*       */             } 
/* 10662 */             effs.removeSpellEffect(runeEffect);
/*       */             
/* 10664 */             runeEffect = effs.getRandomRuneEffect();
/*       */           } 
/*       */           
/* 10667 */           byte runeEnch = RuneUtilities.getEnchantForRune(source);
/* 10668 */           SpellEffect eff = new SpellEffect(target.getWurmId(), runeEnch, 50.0F, 200000000);
/* 10669 */           effs.addSpellEffect(eff);
/*       */           
/* 10671 */           if (RuneUtilities.getModifier(runeEnch, RuneUtilities.ModifierEffect.ENCH_GLOW) > 0.0F)
/*       */           {
/* 10673 */             target.setLightOverride(true);
/* 10674 */             target.setIsAlwaysLit(true);
/*       */           
/*       */           }
/*       */         
/*       */         }
/* 10679 */         else if (RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_COLOR) > 0.0F) {
/*       */           
/* 10681 */           if (RuneUtilities.canApplyRuneTo(source, target)) {
/*       */             int c;
/* 10683 */             performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is given a random color.");
/* 10684 */             performer.achievement(491);
/*       */ 
/*       */             
/* 10687 */             if (source.getColor() == -1) {
/*       */               
/* 10689 */               c = WurmColor.createColor(1 + Server.rand
/* 10690 */                   .nextInt(255), 1 + Server.rand
/* 10691 */                   .nextInt(255), 1 + Server.rand
/* 10692 */                   .nextInt(255));
/*       */             } else {
/* 10694 */               c = source.getColor();
/*       */             } 
/* 10696 */             if (target.isDragonArmour()) {
/* 10697 */               target.setColor2(c);
/*       */             } else {
/* 10699 */               target.setColor(c);
/*       */             } 
/*       */           } 
/* 10702 */         } else if (RuneUtilities.getSpellForRune(source) != null && RuneUtilities.getSpellForRune(source).isTargetAnyItem()) {
/*       */           
/* 10704 */           if (!RuneUtilities.isCorrectTarget(source, target)) {
/*       */             
/* 10706 */             performer.getCommunicator().sendNormalServerMessage("You can't use the rune on that.", (byte)3);
/* 10707 */             return true;
/*       */           } 
/*       */           
/* 10710 */           RuneUtilities.getSpellForRune(source).castSpell(50.0D, performer, target);
/* 10711 */           performer.achievement(491);
/*       */         }
/*       */         else {
/*       */           
/* 10715 */           performer.getCommunicator().sendNormalServerMessage("You can't use the rune on that.", (byte)3);
/* 10716 */           return true;
/*       */         } 
/*       */         
/* 10719 */         target.sendUpdate();
/*       */ 
/*       */       
/*       */       }
/* 10723 */       else if (RuneUtilities.isEnchantRune(source)) {
/* 10724 */         performer.getCommunicator().sendNormalServerMessage("You try to attach the rune to the " + target.getName() + " but fail.", (byte)3);
/*       */       } else {
/*       */         
/* 10727 */         performer.getCommunicator().sendNormalServerMessage("You try to use the rune on the " + target.getName() + " but fail.", (byte)3);
/*       */       } 
/*       */       
/* 10730 */       if (Servers.isThisATestServer())
/* 10731 */         performer.getCommunicator().sendNormalServerMessage("Diff: " + diff + ", bonus: " + source.getCurrentQualityLevel() + ", sd: " + soulDepth
/* 10732 */             .getKnowledge() + ", power: " + power + ", chance: " + soulDepth.getChance(diff, null, source.getCurrentQualityLevel())); 
/* 10733 */       Items.destroyItem(source.getWurmId());
/* 10734 */       return true;
/*       */     } 
/* 10736 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static boolean combineFragment(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 10742 */     if (source.getTemplateId() != 1307 || target.getTemplateId() != 1307) {
/* 10743 */       return true;
/*       */     }
/* 10745 */     if (source.getWurmId() == target.getWurmId()) {
/* 10746 */       return true;
/*       */     }
/* 10748 */     if (source.getOwnerId() != performer.getWurmId() || target.getOwnerId() != performer.getWurmId()) {
/*       */       
/* 10750 */       performer.getCommunicator().sendNormalServerMessage("The fragments must be in your inventory in order to combine them.", (byte)3);
/* 10751 */       return true;
/*       */     } 
/* 10753 */     if (source.getData1() <= 0 || target.getData1() <= 0) {
/*       */       
/* 10755 */       String fully = (source.getAuxData() >= 64 || target.getAuxData() >= 64) ? "fully " : "";
/* 10756 */       performer.getCommunicator().sendNormalServerMessage("The fragments must be " + fully + "identified before you can combine them.", (byte)3);
/* 10757 */       return true;
/*       */     } 
/* 10759 */     if (source.getRealTemplate() == null || target.getRealTemplate() == null) {
/*       */       
/* 10761 */       performer.getCommunicator().sendNormalServerMessage("You're not quite sure how these fragments can go together.", (byte)3);
/* 10762 */       return true;
/*       */     } 
/* 10764 */     if (source.getRealTemplateId() != target.getRealTemplateId()) {
/*       */       
/* 10766 */       performer.getCommunicator().sendNormalServerMessage("You don't think these two fragments can be pieced together in any way.", (byte)3);
/* 10767 */       return true;
/*       */     } 
/* 10769 */     if (source.getRealTemplate().isMetal() && !(source.getRealTemplate()).isOre && !source.getRealTemplate().isMetalLump())
/*       */     {
/* 10771 */       if (source.getMaterial() == 11) {
/*       */         
/* 10773 */         source.setMaterial((byte)93);
/* 10774 */         source.sendUpdate();
/*       */       }
/* 10776 */       else if (source.getMaterial() == 9) {
/*       */         
/* 10778 */         source.setMaterial((byte)94);
/* 10779 */         source.sendUpdate();
/*       */       } 
/*       */     }
/* 10782 */     if (target.getRealTemplate().isMetal() && !(source.getRealTemplate()).isOre && !source.getRealTemplate().isMetalLump())
/*       */     {
/* 10784 */       if (target.getMaterial() == 11) {
/*       */         
/* 10786 */         target.setMaterial((byte)93);
/* 10787 */         target.sendUpdate();
/*       */       }
/* 10789 */       else if (source.getMaterial() == 9) {
/*       */         
/* 10791 */         target.setMaterial((byte)94);
/* 10792 */         target.sendUpdate();
/*       */       } 
/*       */     }
/* 10795 */     if (source.getMaterial() != target.getMaterial()) {
/*       */       
/* 10797 */       if (source.getMaterial() == 0 && source.getRealTemplate().isMetal() && 
/* 10798 */         !(source.getRealTemplate()).isOre && !source.getRealTemplate().isMetalLump()) {
/*       */         
/* 10800 */         source.setMaterial((byte)93);
/* 10801 */         source.sendUpdate();
/*       */       }
/* 10803 */       else if (target.getMaterial() == 0 && target.getRealTemplate().isMetal() && 
/* 10804 */         !(source.getRealTemplate()).isOre && !source.getRealTemplate().isMetalLump()) {
/*       */         
/* 10806 */         target.setMaterial((byte)93);
/* 10807 */         target.sendUpdate();
/*       */       } 
/*       */       
/* 10810 */       if (source.getMaterial() != target.getMaterial()) {
/*       */         
/* 10812 */         performer.getCommunicator().sendNormalServerMessage("The fragments look like they might fit together if they were made of the same material.", (byte)3);
/* 10813 */         return true;
/*       */       } 
/*       */     } 
/*       */     
/* 10817 */     int time = act.getTimeLeft();
/* 10818 */     if (counter == 1.0F) {
/*       */       
/* 10820 */       performer.getCommunicator().sendNormalServerMessage("You start to carefully piece together the fragments.");
/* 10821 */       Server.getInstance().broadCastAction(performer.getName() + " starts to carefully piece together some fragments.", performer, 5);
/* 10822 */       time = Actions.getStandardActionTime(performer, performer.getSkills().getSkillOrLearn(10095), null, 0.0D);
/* 10823 */       act.setTimeLeft(time);
/* 10824 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 10825 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     } 
/* 10827 */     if (act.currentSecond() % 5 == 0)
/*       */     {
/* 10829 */       performer.getStatus().modifyStamina(-500.0F);
/*       */     }
/* 10831 */     if (counter * 10.0F > time) {
/*       */       
/* 10833 */       if (act.getRarity() != 0) {
/* 10834 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/* 10836 */       Skill restoration = performer.getSkills().getSkillOrLearn(10095);
/* 10837 */       ItemTemplate combinedTemplate = ItemTemplateFactory.getInstance().getTemplateOrNull(source.getRealTemplateId());
/* 10838 */       if (combinedTemplate == null) {
/*       */         
/* 10840 */         performer.getCommunicator().sendNormalServerMessage("Something went wrong when piecing these two fragments together. You're not sure what item they create.");
/*       */         
/* 10842 */         return true;
/*       */       } 
/* 10844 */       double totalNeeded = combinedTemplate.getFragmentAmount();
/* 10845 */       int newTotal = source.getAuxData() + target.getAuxData();
/*       */       
/* 10847 */       boolean annGift = (combinedTemplate.getTemplateId() == 651);
/* 10848 */       if (performer.hasFlag(55) && annGift == true) {
/*       */         
/* 10850 */         performer.getCommunicator().sendNormalServerMessage("You decide against completing this item, as you've already completed your gift for this year.");
/*       */         
/* 10852 */         return true;
/*       */       } 
/* 10854 */       if (!performer.isPaying() && annGift == true) {
/*       */         
/* 10856 */         performer.getCommunicator().sendNormalServerMessage("As a non-premium player you're not really sure what you should do to put this back together properly.");
/*       */         
/* 10858 */         return true;
/*       */       } 
/* 10860 */       if (!WurmCalendar.isAnniversary() && annGift == true) {
/*       */         
/* 10862 */         performer.getCommunicator().sendNormalServerMessage("The anniversary week is now over, you're unable to combine these fragments any longer.");
/* 10863 */         return true;
/*       */       } 
/* 10865 */       double difficulty = Math.min(90.0D, FragmentUtilities.getDifficultyForItem(source.getRealTemplateId(), source.getMaterial()) * (source
/* 10866 */           .getAuxData() + target.getAuxData()) / totalNeeded);
/* 10867 */       double power = restoration.skillCheck(difficulty, 0.0D, false, (float)difficulty);
/* 10868 */       if (power >= 0.0D) {
/*       */         
/* 10870 */         if (newTotal >= totalNeeded)
/*       */         {
/*       */           
/*       */           try {
/* 10874 */             Item createdItem = null;
/* 10875 */             if (annGift == true) {
/*       */               
/* 10877 */               createdItem = ItemFactory.createItem(FragmentUtilities.getRandomAnniversaryGift(), 80.0F, act
/* 10878 */                   .getRarity(), performer.getName());
/* 10879 */               performer.setFlag(55, true);
/*       */               
/* 10881 */               if (source.getRarity() > createdItem.getRarity())
/* 10882 */                 createdItem.setRarity(source.getRarity()); 
/* 10883 */               if (target.getRarity() > createdItem.getRarity()) {
/* 10884 */                 createdItem.setRarity(target.getRarity());
/*       */               }
/*       */             } else {
/*       */               
/* 10888 */               int finalData2 = (source.getData2() * source.getAuxData() + target.getData2() * target.getAuxData()) / newTotal;
/*       */               
/* 10890 */               double newQl = ((source.getCurrentQualityLevel() * source.getAuxData() + target.getCurrentQualityLevel() * target.getAuxData()) / newTotal);
/* 10891 */               newQl += (100.0D - newQl) * (finalData2 / 500.0F);
/* 10892 */               createdItem = ItemFactory.createItem(combinedTemplate.getTemplateId(), (float)Math.max(1.0D, Math.min(100.0D, newQl)), act
/* 10893 */                   .getRarity(), performer.getName());
/*       */               
/* 10895 */               if (source.getRarity() > createdItem.getRarity())
/* 10896 */                 createdItem.setRarity(source.getRarity()); 
/* 10897 */               if (target.getRarity() > createdItem.getRarity()) {
/* 10898 */                 createdItem.setRarity(target.getRarity());
/*       */               }
/* 10900 */               if (source.getMaterial() != createdItem.getMaterial() && source.getMaterial() != 0) {
/* 10901 */                 createdItem.setMaterial(source.getMaterial());
/*       */               }
/*       */ 
/*       */               
/* 10905 */               if (createdItem.getMaterial() == 0 && createdItem.isMetal()) {
/* 10906 */                 createdItem.setMaterial((byte)11);
/*       */               }
/* 10908 */               if (createdItem.isMetal()) {
/*       */                 
/* 10910 */                 if (createdItem.getMaterial() == 94) {
/*       */                   
/* 10912 */                   if (finalData2 >= 85) {
/* 10913 */                     createdItem.setMaterial(FragmentUtilities.getMetalMoonMaterial(finalData2));
/*       */                   } else {
/* 10915 */                     createdItem.setMaterial(FragmentUtilities.getMetalAlloyMaterial(finalData2));
/*       */                   } 
/* 10917 */                 } else if (createdItem.getMaterial() == 93) {
/*       */                   
/* 10919 */                   createdItem.setMaterial(FragmentUtilities.getMetalBaseMaterial(finalData2));
/*       */                 }
/*       */               
/* 10922 */               } else if (createdItem.isWood()) {
/*       */                 
/* 10924 */                 createdItem.setMaterial(FragmentUtilities.getRandomWoodMaterial(finalData2));
/*       */               } 
/*       */               
/* 10927 */               if (finalData2 >= 50) {
/*       */ 
/*       */                 
/* 10930 */                 int numEnchants = FragmentUtilities.getRandomEnchantNumber(finalData2);
/* 10931 */                 for (int i = numEnchants; i > 0; i--) {
/*       */                   
/* 10933 */                   float enchPower = Math.max(1.0F, Math.min(104.0F, (finalData2 - 50) * 2.0F - i * 10.0F));
/* 10934 */                   FragmentUtilities.addRandomEnchantment(createdItem, i, enchPower);
/*       */                 } 
/*       */               } 
/*       */ 
/*       */               
/* 10939 */               if (createdItem.isAbility()) {
/* 10940 */                 createdItem.setAuxData((byte)2);
/*       */               }
/* 10942 */               performer.achievement(484);
/* 10943 */               if (createdItem.getTemplate().isStatue())
/* 10944 */                 performer.achievement(486); 
/* 10945 */               if (createdItem.getTemplate().isMask())
/* 10946 */                 performer.achievement(487); 
/* 10947 */               if (createdItem.getTemplate().isTool()) {
/* 10948 */                 performer.achievement(488);
/*       */               }
/*       */             } 
/* 10951 */             performer.getCommunicator().sendNormalServerMessage("You successfully recreate " + createdItem.getNameWithGenus() + " from the fragments.", (byte)2);
/*       */             
/* 10953 */             Server.getInstance().broadCastAction(performer.getName() + " looks pleased as they hold up a completed " + createdItem
/* 10954 */                 .getName() + ".", performer, 5);
/*       */             
/* 10956 */             performer.getInventory().insertItem(createdItem);
/* 10957 */             Items.destroyItem(source.getWurmId());
/* 10958 */             if (newTotal > totalNeeded) {
/*       */               
/* 10960 */               target.setRarity((byte)0);
/* 10961 */               target.setAuxData((byte)(int)(newTotal - totalNeeded));
/* 10962 */               target.setData2((int)(target.getData2() * 0.75F));
/* 10963 */               target.setWeight(combinedTemplate.getWeightGrams() / combinedTemplate.getFragmentAmount() * target.getAuxData(), true);
/*       */ 
/*       */               
/*       */               try {
/* 10967 */                 Item parentItem = target.getParent();
/* 10968 */                 if (parentItem != null)
/*       */                 {
/* 10970 */                   parentItem.dropItem(target.getWurmId(), false);
/* 10971 */                   parentItem.insertItem(target);
/*       */                 }
/*       */               
/* 10974 */               } catch (NoSuchItemException e) {
/*       */                 
/* 10976 */                 target.updateIfGroundItem();
/*       */               } 
/*       */             } else {
/*       */               
/* 10980 */               Items.destroyItem(target.getWurmId());
/*       */             } 
/* 10982 */           } catch (FailedException|NoSuchTemplateException failedException) {}
/*       */ 
/*       */         
/*       */         }
/*       */         else
/*       */         {
/*       */           
/* 10989 */           performer.getCommunicator().sendNormalServerMessage("You add a little bit more to the " + source.getName() + ".", (byte)2);
/* 10990 */           Server.getInstance().broadCastAction(performer.getName() + " manages to combine the fragments.", performer, 5);
/* 10991 */           source.setQualityLevel((source.getCurrentQualityLevel() * source.getAuxData() + target
/* 10992 */               .getCurrentQualityLevel() * target.getAuxData()) / newTotal);
/* 10993 */           source.setData2((source.getData2() * source.getAuxData() + target.getData2() * target.getAuxData()) / newTotal);
/* 10994 */           source.setAuxData((byte)newTotal);
/* 10995 */           source.setWeight(source.getWeightGrams() + target.getWeightGrams(), true);
/* 10996 */           source.setDamage(0.0F);
/*       */           
/* 10998 */           if (act.getRarity() > source.getRarity() && 
/* 10999 */             Server.rand.nextInt(source.getAuxData()) == 0)
/* 11000 */             source.setRarity(act.getRarity()); 
/* 11001 */           if (target.getRarity() > source.getRarity()) {
/* 11002 */             source.setRarity(target.getRarity());
/*       */           }
/* 11004 */           Items.destroyItem(target.getWurmId());
/*       */         
/*       */         }
/*       */       
/*       */       }
/* 11009 */       else if (power > -30.0D) {
/*       */         
/* 11011 */         performer.getCommunicator().sendNormalServerMessage("You make a slight mistake combining the fragments and they fall apart but are untarnished.", (byte)2);
/* 11012 */         Server.getInstance().broadCastAction(performer.getName() + " makes a bad move and the fragments fall back apart.", performer, 5);
/*       */       }
/*       */       else {
/*       */         
/* 11016 */         performer.getCommunicator().sendNormalServerMessage("You use a bit too much pressure and slightly crack the fragments as they fall apart.", (byte)2);
/* 11017 */         Server.getInstance().broadCastAction(performer.getName() + " grunts as a small crack appears on the fragments.", performer, 5);
/* 11018 */         source.setDamage((float)(source.getDamage() + -power * 0.10000000149011612D));
/* 11019 */         target.setDamage((float)(target.getDamage() + -power * 0.10000000149011612D));
/*       */       } 
/*       */       
/* 11022 */       return true;
/*       */     } 
/* 11024 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static boolean identifyFragment(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 11030 */     if (target.getTemplateId() != 1307) {
/* 11031 */       return true;
/*       */     }
/* 11033 */     if (source.getTemplateId() != 441 && source.getTemplateId() != 97) {
/*       */       
/* 11035 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " might not be the best tool for identifying this fragment.", (byte)3);
/* 11036 */       return true;
/*       */     } 
/* 11038 */     if (target.getData1() > 0) {
/*       */       
/* 11040 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " has already been fully identified.", (byte)3);
/* 11041 */       return true;
/*       */     } 
/*       */     
/* 11044 */     int time = act.getTimeLeft();
/* 11045 */     if (counter == 1.0F) {
/*       */       
/* 11047 */       String usage = (source.getTemplateId() == 441) ? "brush" : "chisel";
/* 11048 */       performer.getCommunicator().sendNormalServerMessage("You carefully start " + usage + "ing away bits of dirt and rock from the " + target.getName() + ".");
/* 11049 */       Server.getInstance().broadCastAction(performer.getName() + " starts to carefully " + usage + " away at a small item fragment.", performer, 5);
/* 11050 */       time = Actions.getStandardActionTime(performer, performer.getSkills().getSkillOrLearn(10095), source, 0.0D);
/* 11051 */       act.setTimeLeft(time);
/* 11052 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 11053 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     } 
/* 11055 */     if (act.currentSecond() % 5 == 0)
/*       */     {
/* 11057 */       performer.getStatus().modifyStamina(-500.0F);
/*       */     }
/* 11059 */     if (counter * 10.0F > time) {
/*       */       
/* 11061 */       if (act.getRarity() != 0) {
/* 11062 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/* 11064 */       boolean correctTool = (target.getAuxData() < 65) ? ((source.getTemplateId() == 97)) : ((source.getTemplateId() == 441));
/* 11065 */       Skill restoration = performer.getSkills().getSkillOrLearn(10095);
/* 11066 */       float difficulty = (FragmentUtilities.getDifficultyForItem(target.getRealTemplateId(), target.getMaterial()) / 2);
/* 11067 */       float power = (float)restoration.skillCheck(difficulty, source, 0.0D, false, correctTool ? counter : (counter / 2.0F));
/*       */ 
/*       */       
/*       */       try {
/* 11071 */         performer.getSkills().getSkillOrLearn(source.getPrimarySkill()).skillCheck(difficulty, 0.0D, false, correctTool ? counter : (counter / 2.0F));
/*       */       }
/* 11073 */       catch (NoSuchSkillException noSuchSkillException) {}
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 11078 */       if (power >= 0.0F) {
/*       */         
/* 11080 */         ItemTemplate combinedTemplate = ItemTemplateFactory.getInstance().getTemplateOrNull(target.getRealTemplateId());
/* 11081 */         if (combinedTemplate == null) {
/*       */           
/* 11083 */           performer.getCommunicator().sendNormalServerMessage("Something is wrong with this fragment. It may be a piece of old garbage.");
/* 11084 */           return true;
/*       */         } 
/*       */         
/* 11087 */         if (act.getRarity() > target.getRarity()) {
/* 11088 */           target.setRarity(act.getRarity());
/*       */         }
/* 11090 */         double bonus = (10.0F * (power / 20.0F + 1.0F) * (correctTool ? 1.0F : 0.5F));
/* 11091 */         source.setDamage(source.getDamage() + (100.0F - power) * 1.0E-4F * source.getDamageModifier());
/* 11092 */         int newTotal = (int)Math.min(127.0D, target.getAuxData() + bonus);
/* 11093 */         int finalFragmentWeight = combinedTemplate.getWeightGrams() / combinedTemplate.getFragmentAmount();
/* 11094 */         if (newTotal == 127) {
/*       */           
/* 11096 */           target.setAuxData((byte)1);
/* 11097 */           target.setData1(1);
/* 11098 */           target.setData2((int)((target.getData2() + power) / 2.0F));
/* 11099 */           target.setWeight(finalFragmentWeight, false);
/* 11100 */           performer.getCommunicator().sendNormalServerMessage("You successfully identify the fragment as " + target.getNameWithGenus() + ".");
/* 11101 */           Server.getInstance().broadCastAction(performer.getName() + " looks pleased as they identify the item fragment.", performer, 5);
/* 11102 */           performer.achievement(482);
/*       */ 
/*       */           
/*       */           try {
/* 11106 */             Item parentItem = target.getParent();
/* 11107 */             if (parentItem != null)
/*       */             {
/* 11109 */               parentItem.dropItem(target.getWurmId(), false);
/* 11110 */               parentItem.insertItem(target);
/*       */             }
/*       */           
/* 11113 */           } catch (NoSuchItemException e) {
/*       */             
/* 11115 */             target.updateIfGroundItem();
/*       */           }
/*       */         
/*       */         } else {
/*       */           
/* 11120 */           target.setAuxData((byte)newTotal);
/* 11121 */           target.setData2((int)((target.getData2() + power) / 2.0F));
/* 11122 */           int newWeight = (int)(target.getTemplate().getWeightGrams() - newTotal / 127.0F * (target.getTemplate().getWeightGrams() - finalFragmentWeight));
/* 11123 */           target.setWeight(newWeight, false);
/* 11124 */           performer.getCommunicator().sendNormalServerMessage("You successfully clear away a bit of dirt and rock from the " + target.getName() + " exposing a little more about its composition.");
/*       */           
/* 11126 */           Server.getInstance().broadCastAction(performer.getName() + " finishes clearing away a bit of dirt and rock from the small item fragment.", performer, 5);
/*       */           
/* 11128 */           if (newTotal >= 65) {
/* 11129 */             performer.getCommunicator().sendNormalServerMessage("You think a metal brush might work best for brushing away the last small bits.");
/*       */           } else {
/* 11131 */             performer.getCommunicator().sendNormalServerMessage("You think a chisel might work best for chipping away the larger bits of rock.");
/*       */           } 
/*       */         } 
/* 11134 */         performer.getCommunicator().sendUpdateInventoryItem(target);
/*       */       }
/*       */       else {
/*       */         
/* 11138 */         performer.getCommunicator().sendNormalServerMessage("You make a bad move and almost ruin the " + target.getName() + ".");
/* 11139 */         Server.getInstance().broadCastAction(performer.getName() + " slips and almost ruins the item fragment.", performer, 5);
/*       */       } 
/* 11141 */       return true;
/*       */     } 
/*       */     
/* 11144 */     return false;
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
/*       */   private static boolean makeBait(Action act, Creature performer, Item tool, Item target, short action, float counter) {
/* 11160 */     if (target.isFish()) {
/*       */       
/* 11162 */       if (target.getWeightGrams() >= 300)
/*       */       {
/* 11164 */         performer.getCommunicator().sendNormalServerMessage("Cannot make bait from that, as its too large.");
/* 11165 */         return true;
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/* 11170 */       performer.getCommunicator().sendNormalServerMessage("Cannot make bait from that, as its not a fish!");
/* 11171 */       return true;
/*       */     } 
/* 11173 */     if (tool.getTemplateId() != 258 && tool.getTemplateId() != 93 && tool
/* 11174 */       .getTemplateId() != 8) {
/*       */       
/* 11176 */       performer.getCommunicator().sendNormalServerMessage("Cannot make fish bait using " + tool.getNameWithGenus() + ".");
/* 11177 */       return true;
/*       */     } 
/* 11179 */     int time = act.getTimeLeft();
/* 11180 */     if (counter == 1.0F) {
/*       */       
/* 11182 */       performer.getCommunicator().sendNormalServerMessage("You start making bait from the " + target.getName() + ".");
/* 11183 */       Server.getInstance().broadCastAction(performer.getName() + " starts making bait from " + target.getName() + ".", performer, 5);
/* 11184 */       time = Actions.getStandardActionTime(performer, performer.getSkills().getSkillOrLearn(10033), tool, 0.0D);
/* 11185 */       act.setTimeLeft(time);
/* 11186 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 11187 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     } 
/* 11189 */     if (act.currentSecond() % 5 == 0)
/*       */     {
/* 11191 */       performer.getStatus().modifyStamina(-500.0F);
/*       */     }
/* 11193 */     if (counter * 10.0F > time) {
/*       */       
/* 11195 */       if (act.getRarity() != 0) {
/* 11196 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/* 11198 */       Skill fishing = performer.getSkills().getSkillOrLearn(10033);
/* 11199 */       float difficulty = getBaitDifficulty(target, tool);
/*       */ 
/*       */       
/*       */       try {
/* 11203 */         performer.getSkills().getSkillOrLearn(tool.getPrimarySkill()).skillCheck(difficulty, 0.0D, false, counter / 2.0F);
/*       */       }
/* 11205 */       catch (NoSuchSkillException noSuchSkillException) {}
/*       */ 
/*       */ 
/*       */       
/* 11209 */       if (act.getRarity() != 0) {
/* 11210 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/*       */       
/*       */       try {
/* 11214 */         float ql = target.getCurrentQualityLevel();
/* 11215 */         int knowledge = (int)fishing.getKnowledge(0.0D);
/* 11216 */         if (knowledge < ql)
/*       */         {
/*       */           
/* 11219 */           ql -= Server.rand.nextFloat() * (ql - knowledge) / 2.0F;
/*       */         }
/*       */         
/* 11222 */         ql = Math.min(100.0F, ql + tool.getRarity());
/*       */         
/* 11224 */         int count = 0;
/* 11225 */         int newWeight = target.getWeightGrams();
/* 11226 */         String baitName = "fish bits";
/* 11227 */         float addDam = 0.0F;
/* 11228 */         int tempId = getBaitTemplateId(target);
/* 11229 */         while (newWeight > 0 && performer.getInventory().mayCreatureInsertItem()) {
/*       */           
/* 11231 */           float power = (float)fishing.skillCheck(difficulty, tool, 0.0D, false, counter / 10.0F);
/* 11232 */           float newql = Math.max(Math.min(ql + power / 50.0F, 100.0F), 1.0F);
/* 11233 */           byte rarity = (byte)Math.max(act.getRarity(), target.getRarity());
/* 11234 */           Item bait = ItemFactory.createItem(tempId, newql, rarity, null);
/* 11235 */           performer.getInventory().insertItem(bait);
/* 11236 */           if (target.getRarity() > 0) {
/*       */             
/* 11238 */             target.setRarity((byte)0);
/* 11239 */             target.sendUpdate();
/*       */           } 
/* 11241 */           count++;
/* 11242 */           newWeight -= bait.getWeightGrams();
/* 11243 */           addDam += (100.0F - power) * 1.0E-5F * tool.getDamageModifier();
/*       */         } 
/* 11245 */         tool.setDamage(tool.getDamage() + addDam);
/* 11246 */         if (!performer.getInventory().mayCreatureInsertItem())
/*       */         {
/* 11248 */           performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put more fish bits, so you destroy the remainder.");
/*       */         }
/*       */         
/* 11251 */         performer.getCommunicator().sendNormalServerMessage("You make " + count + " " + "fish bits" + " from the " + target
/* 11252 */             .getName() + ".");
/* 11253 */         Server.getInstance().broadCastAction(performer
/* 11254 */             .getName() + " makes some " + "fish bits" + " from a " + target.getName() + ".", performer, 5);
/*       */         
/* 11256 */         Items.destroyItem(target.getWurmId());
/*       */       }
/* 11258 */       catch (NoSuchTemplateException nst) {
/*       */         
/* 11260 */         logger.log(Level.WARNING, "No template for 1364", (Throwable)nst);
/* 11261 */         performer.getCommunicator().sendNormalServerMessage("You fail to make any bait. You realize something is wrong with the world.");
/*       */       
/*       */       }
/* 11264 */       catch (FailedException fe) {
/*       */         
/* 11266 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 11267 */         performer.getCommunicator().sendNormalServerMessage("You fail to make any bait. You realize something is wrong with the world.");
/*       */       } 
/*       */       
/* 11270 */       return true;
/*       */     } 
/*       */     
/* 11273 */     return false;
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
/*       */   private static boolean makeBait(Action act, Creature performer, Item target, short action, float counter) {
/* 11288 */     int ttid = target.getTemplateId();
/* 11289 */     if (ttid != 200 && ttid != 1192 && ttid != 69 && ttid != 66 && ttid != 68 && ttid != 29 && ttid != 32) {
/*       */ 
/*       */       
/* 11292 */       performer.getCommunicator().sendNormalServerMessage("You cannot make bait from that.");
/* 11293 */       return true;
/*       */     } 
/* 11295 */     int time = act.getTimeLeft();
/* 11296 */     if (counter == 1.0F) {
/*       */       
/* 11298 */       performer.getCommunicator().sendNormalServerMessage("You start making bait from the " + target.getName() + ".");
/* 11299 */       Server.getInstance().broadCastAction(performer.getName() + " starts making bait from " + target.getName() + ".", performer, 5);
/* 11300 */       time = Actions.getStandardActionTime(performer, performer.getSkills().getSkillOrLearn(10033), null, 0.0D);
/* 11301 */       act.setTimeLeft(time);
/* 11302 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 11303 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     } 
/* 11305 */     if (act.currentSecond() % 5 == 0)
/*       */     {
/* 11307 */       performer.getStatus().modifyStamina(-500.0F);
/*       */     }
/* 11309 */     if (counter * 10.0F > time) {
/*       */       
/* 11311 */       if (act.getRarity() != 0) {
/* 11312 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/* 11314 */       Skill fishing = performer.getSkills().getSkillOrLearn(10033);
/* 11315 */       float difficulty = getBaitDifficulty(target);
/*       */       
/*       */       try {
/* 11318 */         int knowledge = (int)fishing.getKnowledge(0.0D);
/* 11319 */         float ql = target.getCurrentQualityLevel();
/* 11320 */         if (knowledge < ql)
/*       */         {
/*       */           
/* 11323 */           ql -= Server.rand.nextFloat() * (ql - knowledge) / 2.0F;
/*       */         }
/* 11325 */         int count = 0;
/* 11326 */         int newWeight = target.getWeightGrams();
/* 11327 */         String baitName = getBaitName(target);
/* 11328 */         int tempId = getBaitTemplateId(target);
/* 11329 */         float extraLoss = getBaitExtraLossMultiplier(target);
/* 11330 */         while (count < 10 && newWeight > 0 && performer.getInventory().mayCreatureInsertItem()) {
/*       */           
/* 11332 */           count++;
/* 11333 */           float power = (float)fishing.skillCheck(difficulty, null, 0.0D, false, counter / 100.0F);
/* 11334 */           float newql = Math.max(Math.min(ql + power / 50.0F, 100.0F), 1.0F);
/* 11335 */           byte rarity = (byte)Math.max(act.getRarity(), target.getRarity());
/* 11336 */           Item bait = ItemFactory.createItem(tempId, newql, rarity, null);
/* 11337 */           performer.getInventory().insertItem(bait);
/* 11338 */           if (target.getRarity() > 0) {
/*       */             
/* 11340 */             target.setRarity((byte)0);
/* 11341 */             target.sendUpdate();
/*       */           } 
/* 11343 */           newWeight = (int)(newWeight - bait.getWeightGrams() * extraLoss);
/*       */         } 
/*       */         
/* 11346 */         target.setWeight(newWeight, true);
/* 11347 */         performer.getCommunicator().sendNormalServerMessage("You make " + count + " " + baitName + " from a " + target
/* 11348 */             .getName() + ".");
/* 11349 */         Server.getInstance().broadCastAction(performer
/* 11350 */             .getName() + " makes some " + baitName + " from a " + target.getName() + ".", performer, 5);
/*       */       
/*       */       }
/* 11353 */       catch (NoSuchTemplateException nst) {
/*       */         
/* 11355 */         logger.log(Level.WARNING, "No template for 1364", (Throwable)nst);
/* 11356 */         performer.getCommunicator().sendNormalServerMessage("You fail to make any bait. You realize something is wrong with the world.");
/*       */       
/*       */       }
/* 11359 */       catch (FailedException fe) {
/*       */         
/* 11361 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 11362 */         performer.getCommunicator().sendNormalServerMessage("You fail to make any bait. You realize something is wrong with the world.");
/*       */       } 
/*       */       
/* 11365 */       return true;
/*       */     } 
/*       */     
/* 11368 */     return false;
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
/*       */   private static boolean makeFloat(Action act, Creature performer, Item target, short action, float counter) {
/* 11383 */     int ttid = target.getTemplateId();
/* 11384 */     if (ttid != 479) {
/*       */       
/* 11386 */       performer.getCommunicator().sendNormalServerMessage("You cannot make a float from that.");
/* 11387 */       return true;
/*       */     } 
/* 11389 */     int time = act.getTimeLeft();
/* 11390 */     if (counter == 1.0F) {
/*       */       
/* 11392 */       performer.getCommunicator().sendNormalServerMessage("You start making a float from the " + target.getName() + ".");
/* 11393 */       Server.getInstance().broadCastAction(performer.getName() + " starts making a float from " + target.getName() + ".", performer, 5);
/* 11394 */       time = Actions.getStandardActionTime(performer, performer.getSkills().getSkillOrLearn(10033), null, 0.0D);
/* 11395 */       act.setTimeLeft(time);
/* 11396 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 11397 */       performer.getStatus().modifyStamina(-1000.0F);
/*       */     } 
/* 11399 */     if (act.currentSecond() % 5 == 0)
/*       */     {
/* 11401 */       performer.getStatus().modifyStamina(-500.0F);
/*       */     }
/* 11403 */     if (counter * 10.0F > time) {
/*       */       
/* 11405 */       if (act.getRarity() != 0) {
/* 11406 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/* 11408 */       Skill fishing = performer.getSkills().getSkillOrLearn(10033);
/*       */       
/* 11410 */       float difficulty = target.getCurrentQualityLevel() / 2.0F;
/*       */       
/*       */       try {
/* 11413 */         int knowledge = (int)fishing.getKnowledge(0.0D);
/* 11414 */         float ql = target.getCurrentQualityLevel();
/* 11415 */         if (knowledge < ql)
/*       */         {
/*       */           
/* 11418 */           ql -= Server.rand.nextFloat() * (ql - knowledge) / 2.0F;
/*       */         }
/* 11420 */         int count = 0;
/* 11421 */         int newWeight = target.getWeightGrams();
/* 11422 */         String floatName = "small piece of moss";
/* 11423 */         while (count < 10 && newWeight > 0 && performer.getInventory().mayCreatureInsertItem()) {
/*       */           
/* 11425 */           count++;
/* 11426 */           float power = (float)fishing.skillCheck(difficulty, null, 0.0D, false, counter / 100.0F);
/* 11427 */           float newql = Math.max(Math.min(ql + power / 50.0F, 100.0F), 1.0F);
/* 11428 */           byte rarity = (byte)Math.max(act.getRarity(), target.getRarity());
/* 11429 */           Item moss = ItemFactory.createItem(1354, newql, rarity, null);
/* 11430 */           performer.getInventory().insertItem(moss);
/* 11431 */           if (target.getRarity() > 0) {
/*       */             
/* 11433 */             target.setRarity((byte)0);
/* 11434 */             target.sendUpdate();
/*       */           } 
/* 11436 */           newWeight -= moss.getWeightGrams() * 2;
/*       */         } 
/*       */         
/* 11439 */         target.setWeight(newWeight, true);
/* 11440 */         performer.getCommunicator().sendNormalServerMessage("You make " + count + " " + "small piece of moss" + " from the " + target
/* 11441 */             .getName() + ".");
/* 11442 */         Server.getInstance().broadCastAction(performer
/* 11443 */             .getName() + " makes some " + "small piece of moss" + " from a " + target.getName() + ".", performer, 5);
/*       */       
/*       */       }
/* 11446 */       catch (NoSuchTemplateException nst) {
/*       */         
/* 11448 */         logger.log(Level.WARNING, "No template for 1354", (Throwable)nst);
/* 11449 */         performer.getCommunicator().sendNormalServerMessage("You fail to make any bait. You realize something is wrong with the world.");
/*       */       
/*       */       }
/* 11452 */       catch (FailedException fe) {
/*       */         
/* 11454 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 11455 */         performer.getCommunicator().sendNormalServerMessage("You fail to make any bait. You realize something is wrong with the world.");
/*       */       } 
/*       */       
/* 11458 */       return true;
/*       */     } 
/*       */     
/* 11461 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private static float getBaitDifficulty(Item target) {
/* 11466 */     switch (target.getTemplateId()) {
/*       */       
/*       */       case 66:
/* 11469 */         return 7.0F;
/*       */       case 68:
/* 11471 */         return 5.0F;
/*       */       case 69:
/* 11473 */         return 10.0F;
/*       */       case 200:
/* 11475 */         return 3.0F;
/*       */       case 29:
/* 11477 */         return 15.0F;
/*       */       case 32:
/* 11479 */         return 20.0F;
/*       */     } 
/* 11481 */     return 1.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   private static float getBaitDifficulty(Item target, Item knife) {
/* 11486 */     if (target.isFish())
/*       */     {
/* 11488 */       switch (knife.getTemplateId()) {
/*       */         
/*       */         case 258:
/* 11491 */           return 15.0F;
/*       */         case 8:
/* 11493 */           return 10.0F;
/*       */         case 93:
/* 11495 */           return 5.0F;
/*       */       } 
/*       */     }
/* 11498 */     return 1.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   private static int getBaitTemplateId(Item target) {
/* 11503 */     switch (target.getTemplateId()) {
/*       */       
/*       */       case 66:
/*       */       case 68:
/*       */       case 69:
/* 11508 */         return 1360;
/*       */       case 200:
/* 11510 */         return 1361;
/*       */       case 29:
/* 11512 */         return 1365;
/*       */       case 32:
/* 11514 */         return 1366;
/*       */     } 
/* 11516 */     if (target.isFish())
/*       */     {
/* 11518 */       return 1363;
/*       */     }
/* 11520 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   private static String getBaitName(Item target) {
/* 11525 */     switch (target.getTemplateId()) {
/*       */       
/*       */       case 66:
/*       */       case 68:
/*       */       case 69:
/* 11530 */         return "cheese piece";
/*       */       case 200:
/* 11532 */         return "dough ball";
/*       */       case 29:
/* 11534 */         return "grain of wheat";
/*       */       case 32:
/* 11536 */         return "corn kernel";
/*       */     } 
/* 11538 */     if (target.isFish())
/*       */     {
/* 11540 */       return "bit of Fish";
/*       */     }
/* 11542 */     return "";
/*       */   }
/*       */ 
/*       */   
/*       */   private static float getBaitExtraLossMultiplier(Item target) {
/* 11547 */     switch (target.getTemplateId()) {
/*       */       
/*       */       case 66:
/*       */       case 68:
/*       */       case 69:
/* 11552 */         return 1.0F;
/*       */       case 200:
/* 11554 */         return 1.5F;
/*       */       case 29:
/* 11556 */         return 2.0F;
/*       */       case 32:
/* 11558 */         return 2.0F;
/*       */     } 
/* 11560 */     if (target.isFish())
/*       */     {
/* 11562 */       return 2.0F;
/*       */     }
/* 11564 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static boolean convertFishingEquipment(Creature performer, Item target) {
/* 11570 */     switch (target.getTemplateId()) {
/*       */ 
/*       */       
/*       */       case 94:
/* 11574 */         return convertFishingEquipment(performer, target, 1344, new int[] { 1347, 1357 });
/*       */       
/*       */       case 152:
/* 11577 */         return convertFishingEquipment(performer, target, 1344, new int[] { 1347, 1356 });
/*       */       
/*       */       case 780:
/* 11580 */         return convertFishingEquipment(performer, target, 1344, new int[0]);
/*       */       
/*       */       case 95:
/* 11583 */         return convertFishingEquipment(performer, target, 1357, new int[0]);
/*       */       
/*       */       case 150:
/* 11586 */         return convertFishingEquipment(performer, target, 1347, new int[] { 1357 });
/*       */       
/*       */       case 96:
/* 11589 */         return convertFishingEquipment(performer, target, 1356, new int[0]);
/*       */       
/*       */       case 151:
/* 11592 */         return convertFishingEquipment(performer, target, 1347, new int[] { 1356 });
/*       */     } 
/* 11594 */     return true;
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
/*       */   private static boolean convertFishingEquipment(Creature performer, Item target, int newId, int... makeIds) {
/* 11608 */     target.setTemplateId(newId);
/*       */     
/*       */     try {
/* 11611 */       ItemTemplate newTemplate = ItemTemplateFactory.getInstance().getTemplate(newId);
/* 11612 */       target.setWeight(newTemplate.getWeightGrams(), false);
/*       */     }
/* 11614 */     catch (NoSuchTemplateException e) {
/*       */       
/* 11616 */       logger.warning(String.format("Error: Could not find template ID for converted fishing equipment: %s", new Object[] { Integer.valueOf(newId) }));
/*       */     } 
/* 11618 */     ItemFactory.createContainerRestrictions(target);
/*       */     
/* 11620 */     Item parent = target;
/*       */     
/*       */     try {
/* 11623 */       for (int id : makeIds) {
/*       */         
/* 11625 */         Item newItem = ItemFactory.createItem(id, target.getQualityLevel(), (byte)0, (byte)0, target
/* 11626 */             .getCreatorName());
/* 11627 */         parent.insertItem(newItem, true);
/*       */         
/* 11629 */         if (newItem.isHollow()) {
/* 11630 */           parent = newItem;
/*       */         }
/*       */       } 
/* 11633 */     } catch (FailedException e) {
/*       */ 
/*       */       
/* 11636 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */     }
/* 11638 */     catch (NoSuchTemplateException e) {
/*       */ 
/*       */       
/* 11641 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */     } 
/* 11643 */     return true;
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
/*       */   private static boolean openClam(Action act, Creature performer, Item tool, Item target, short action, float counter) {
/* 11659 */     int time = act.getTimeLeft();
/* 11660 */     if (counter == 1.0F) {
/*       */       
/* 11662 */       performer.getCommunicator().sendNormalServerMessage("You start prying open " + target.getNameWithGenus() + ".");
/* 11663 */       Server.getInstance().broadCastAction(performer.getName() + " starts prying open something.", performer, 5);
/* 11664 */       time = Actions.getVariableActionTime(performer, performer.getSkills().getSkillOrLearn(10059), tool, 0.0D, 60, 20, 2500);
/*       */       
/* 11666 */       act.setTimeLeft(time);
/* 11667 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 11668 */       performer.getStatus().modifyStamina(-1000.0F);
/* 11669 */       return false;
/*       */     } 
/* 11671 */     if (counter * 10.0F > time) {
/*       */       
/* 11673 */       if (act.getRarity() != 0) {
/* 11674 */         performer.playPersonalSound("sound.fx.drumroll");
/*       */       }
/* 11676 */       getClamItem(performer, target, act);
/* 11677 */       Items.destroyItem(target.getWurmId());
/* 11678 */       return true;
/*       */     } 
/* 11680 */     return false;
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
/*       */   private static void getClamItem(Creature performer, Item target, Action act) {
/* 11697 */     ClamLootEnum[] loot = ClamLootEnum.getLootTable();
/*       */     
/* 11699 */     Skill butchering = performer.getSkills().getSkillOrLearn(10059);
/* 11700 */     float knowledge = (float)butchering.getKnowledge(0.0D);
/* 11701 */     if (Server.rand.nextFloat() * knowledge * 10.0F < 1.0F) {
/*       */ 
/*       */       
/* 11704 */       performer.getCommunicator().sendNormalServerMessage("You mysteriously cut yourself when trying to open that and throw the clam away in disgust!");
/*       */       
/* 11706 */       CombatEngine.addWound(performer, performer, (byte)1, 13, (2000 + Server.rand
/* 11707 */           .nextInt(2000)), 0.0F, "cut", null, 0.0F, 0.0F, false, false, false, false);
/*       */       return;
/*       */     } 
/* 11710 */     byte lootptr = (byte)Server.rand.nextInt(256);
/* 11711 */     ClamLootEnum cle = loot[lootptr & 0xFF];
/* 11712 */     float ql = target.getCurrentQualityLevel();
/* 11713 */     int lootId = cle.getTemplateId();
/* 11714 */     FragmentUtilities.Fragment frag = null;
/*       */ 
/*       */ 
/*       */     
/* 11718 */     switch (lootId) {
/*       */ 
/*       */ 
/*       */       
/*       */       case 51:
/* 11723 */         if (performer.checkCoinAward(100)) {
/* 11724 */           performer.getCommunicator().sendSafeServerMessage("You find a rare coin!");
/*       */         }
/*       */         return;
/*       */       
/*       */       case 1397:
/* 11729 */         if (Server.rand.nextFloat() * 100.0F < 1.0F) {
/* 11730 */           lootId = 1398;
/*       */         }
/*       */         break;
/*       */       
/*       */       case 1307:
/* 11735 */         frag = FragmentUtilities.getRandomFragmentForSkill((knowledge / 5.0F), true);
/* 11736 */         if (frag == null) {
/*       */           
/* 11738 */           performer.getCommunicator().sendNormalServerMessage("You fail to find anything in the " + target.getName() + ".");
/*       */           return;
/*       */         } 
/*       */         break;
/*       */     } 
/* 11743 */     if (lootId == ClamLootEnum.NONE.getTemplateId()) {
/*       */       
/* 11745 */       performer.getCommunicator().sendNormalServerMessage("You fail to find anything in the " + target.getName() + ".");
/*       */       
/*       */       return;
/*       */     } 
/* 11749 */     if (knowledge < ql)
/*       */     {
/*       */       
/* 11752 */       ql -= Server.rand.nextFloat() * (ql - knowledge);
/*       */     }
/* 11754 */     byte rarity = (byte)Math.max(act.getRarity(), target.getRarity());
/*       */     
/*       */     try {
/* 11757 */       Item foundItem = ItemFactory.createItem(lootId, ql, rarity, null);
/*       */       
/* 11759 */       switch (lootId) {
/*       */ 
/*       */         
/*       */         case 1307:
/* 11763 */           foundItem.setRealTemplate(frag.getItemId());
/* 11764 */           if (foundItem.getRealTemplate().getMaterial() != frag.getMaterial()) {
/* 11765 */             foundItem.setMaterial((byte)frag.getMaterial());
/*       */           }
/*       */           break;
/*       */         
/*       */         case 92:
/* 11770 */           foundItem.setMaterial((byte)85);
/*       */           break;
/*       */       } 
/*       */ 
/*       */       
/* 11775 */       if (cle.canHaveDamage()) {
/*       */ 
/*       */         
/* 11778 */         float rnd = Server.rand.nextFloat() * 100.0F - 75.0F;
/* 11779 */         if (rnd > 0.0F)
/*       */         {
/* 11781 */           foundItem.setDamage(rnd);
/*       */         }
/*       */       } 
/* 11784 */       if (cle.randomMaterial())
/*       */       {
/*       */         
/* 11787 */         if (MaterialUtilities.isMetal(foundItem.getMaterial())) {
/*       */           
/* 11789 */           foundItem.setMaterial(getRandomMetalMaterial());
/*       */         }
/* 11791 */         else if (MaterialUtilities.isWood(foundItem.getMaterial())) {
/*       */           
/* 11793 */           foundItem.setMaterial(getRandomWoodMaterial());
/*       */         } 
/*       */       }
/*       */       
/* 11797 */       performer.getInventory().insertItem(foundItem);
/* 11798 */       if (rarity > 2)
/* 11799 */         performer.achievement(334); 
/* 11800 */       performer.getCommunicator().sendNormalServerMessage("You found " + foundItem
/* 11801 */           .getNameWithGenus() + " in the " + target.getName() + ".");
/* 11802 */       Server.getInstance().broadCastAction(performer
/* 11803 */           .getName() + " found something in a " + target.getName() + ".", performer, 5);
/*       */     }
/* 11805 */     catch (FailedException e) {
/*       */ 
/*       */       
/* 11808 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */     }
/* 11810 */     catch (NoSuchTemplateException e) {
/*       */ 
/*       */       
/* 11813 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static byte getRandomMetalMaterial() {
/* 11820 */     switch (Server.rand.nextInt(20)) {
/*       */       
/*       */       case 0:
/* 11823 */         return 7;
/*       */       case 1:
/* 11825 */         return 8;
/*       */       case 2:
/* 11827 */         return 10;
/*       */       case 3:
/* 11829 */         return 13;
/*       */       case 4:
/* 11831 */         return 34;
/*       */       case 5:
/* 11833 */         return 12;
/*       */     } 
/* 11835 */     return 11;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static byte getRandomWoodMaterial() {
/* 11842 */     if (Server.rand.nextInt(10) > 1) {
/*       */       
/* 11844 */       switch (Server.rand.nextInt(10)) {
/*       */         
/*       */         case 0:
/* 11847 */           return 14;
/*       */         case 1:
/* 11849 */           return 37;
/*       */         case 2:
/* 11851 */           return 40;
/*       */         case 3:
/* 11853 */           return 38;
/*       */         case 4:
/* 11855 */           return 41;
/*       */         case 5:
/* 11857 */           return 63;
/*       */         case 6:
/* 11859 */           return 64;
/*       */         case 7:
/* 11861 */           return 65;
/*       */         case 8:
/* 11863 */           return 66;
/*       */       } 
/* 11865 */       return 39;
/*       */     } 
/*       */     
/* 11868 */     switch (Server.rand.nextInt(5)) {
/*       */       
/*       */       case 0:
/* 11871 */         return 42;
/*       */       case 1:
/* 11873 */         return 45;
/*       */       case 2:
/* 11875 */         return 43;
/*       */       case 3:
/* 11877 */         return 88;
/*       */     } 
/* 11879 */     return 44;
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
/*       */   private static boolean moveBulkItemAsAction(Action act, Creature performer, Item source, Item target, float counter) {
/*       */     Item moveTargetContainer;
/*       */     ItemTemplate sourceTemplate;
/* 11905 */     Item targetParent = target.getParentOrNull();
/* 11906 */     Item sourceParent = source.getParentOrNull();
/* 11907 */     byte sourceAuxByte = source.getAuxData();
/* 11908 */     byte sourceMaterialByte = source.getMaterial();
/* 11909 */     int sourceTemplateID = source.getRealTemplateId();
/*       */     
/* 11911 */     int playerAmountToTransfer = (int)act.getData();
/*       */     
/* 11913 */     boolean done = false;
/* 11914 */     boolean abortingEarly = false;
/*       */ 
/*       */     
/* 11917 */     if (!source.isBulkItem()) {
/*       */       
/* 11919 */       performer.getCommunicator().sendNormalServerMessage("Uhh... this is for bulk items only.", (byte)3);
/* 11920 */       if (Servers.localServer.testServer)
/* 11921 */         performer.getCommunicator().sendNormalServerMessage("Wrong source item! item=" + source); 
/* 11922 */       return true;
/*       */     } 
/*       */ 
/*       */     
/* 11926 */     if (!target.isBulkContainer()) {
/*       */       
/* 11928 */       if (targetParent == null || !targetParent.isBulkContainer()) {
/*       */         
/* 11930 */         performer.getCommunicator().sendNormalServerMessage("Oh, that won't work at all.", (byte)3);
/* 11931 */         if (Servers.localServer.testServer)
/* 11932 */           performer.getCommunicator().sendNormalServerMessage("Wrong target of action! parent=" + targetParent); 
/* 11933 */         return true;
/*       */       } 
/*       */       
/* 11936 */       moveTargetContainer = targetParent;
/*       */     } else {
/*       */       
/* 11939 */       moveTargetContainer = target;
/*       */     } 
/*       */ 
/*       */     
/*       */     try {
/* 11944 */       sourceTemplate = ItemTemplateFactory.getInstance().getTemplate(sourceTemplateID);
/*       */     }
/* 11946 */     catch (NoSuchTemplateException nst) {
/*       */       
/* 11948 */       performer.getCommunicator().sendNormalServerMessage("ERROR: Could not find template for value " + sourceTemplateID + " source item = " + source);
/*       */       
/* 11950 */       return true;
/*       */     } 
/*       */ 
/*       */     
/* 11954 */     Item parent = target.getParentOrNull();
/* 11955 */     while (parent != null) {
/*       */       
/* 11957 */       if (parent != null && parent.getTemplateId() == 1315) {
/*       */         
/* 11959 */         performer.getCommunicator().sendNormalServerMessage("The bulk storage bin is not allowed to contain items if it's in a Rack for Empty BSB");
/* 11960 */         return true;
/*       */       } 
/*       */       
/* 11963 */       parent = parent.getParentOrNull();
/*       */     } 
/*       */     
/* 11966 */     if (sourceTemplateID < 0) {
/*       */       
/* 11968 */       performer.getCommunicator().sendAlertServerMessage("ERROR! Source templace id is " + sourceTemplateID);
/* 11969 */       return true;
/*       */     } 
/*       */     
/* 11972 */     if (target.isLocked()) {
/*       */ 
/*       */       
/* 11975 */       if (performer != null && !target.mayAccessHold(performer))
/*       */       {
/* 11977 */         performer.getCommunicator().sendNormalServerMessage("You're not allowed to put things into this " + target
/* 11978 */             .getName() + ".");
/* 11979 */         return true;
/*       */       
/*       */       }
/*       */ 
/*       */     
/*       */     }
/* 11985 */     else if (performer != null && !Methods.isActionAllowed(performer, (short)7, moveTargetContainer)) {
/*       */       
/* 11987 */       return true;
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/* 11992 */     int mtcTemplateId = moveTargetContainer.getTemplateId();
/* 11993 */     if (MethodsItems.checkIfStealing(source, performer, act) || 
/* 11994 */       MethodsItems.checkIfStealing(moveTargetContainer, performer, act)) {
/*       */       
/* 11996 */       performer.getCommunicator().sendNormalServerMessage("You may not do that here.");
/* 11997 */       abortingEarly = true;
/* 11998 */       done = true;
/*       */     
/*       */     }
/* 12001 */     else if (source.isOnSurface() != moveTargetContainer.isOnSurface() || performer.isOnSurface() != source.isOnSurface()) {
/*       */       
/* 12003 */       performer.getCommunicator().sendNormalServerMessage("Everything needs to be on the same layer.");
/* 12004 */       abortingEarly = true;
/* 12005 */       done = true;
/*       */     
/*       */     }
/* 12008 */     else if (sourceTemplate.isFood() && mtcTemplateId != 851 && mtcTemplateId != 852 && mtcTemplateId != 661) {
/*       */       
/* 12010 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " would be destroyed.");
/* 12011 */       done = true;
/*       */     }
/* 12013 */     else if (!sourceTemplate.isFood() && mtcTemplateId != 852 && mtcTemplateId != 851 && mtcTemplateId != 662 && mtcTemplateId != 1317) {
/*       */ 
/*       */       
/* 12016 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " would be destroyed.");
/* 12017 */       done = true;
/*       */ 
/*       */     
/*       */     }
/* 12021 */     else if (!moveTargetContainer.isCrate() && moveTargetContainer.getFreeVolume() < sourceTemplate.getVolume()) {
/*       */       
/* 12023 */       performer.getCommunicator().sendNormalServerMessage("You can not even fit one of those.", (byte)3);
/* 12024 */       abortingEarly = true;
/* 12025 */       done = true;
/*       */     }
/* 12027 */     else if (moveTargetContainer.isCrate() && moveTargetContainer.getRemainingCrateSpace() <= 0) {
/*       */       
/* 12029 */       performer.getCommunicator().sendNormalServerMessage("You can not even fit one of those.", (byte)3);
/* 12030 */       abortingEarly = true;
/* 12031 */       done = true;
/*       */     } 
/*       */     
/* 12034 */     if (!done) {
/*       */ 
/*       */       
/* 12037 */       int amountToTransfer, amountMayCarry = performer.getCarryingCapacityLeft() / sourceTemplate.getWeightGrams();
/*       */ 
/*       */       
/* 12040 */       int maxMayFit = moveTargetContainer.isCrate() ? moveTargetContainer.getRemainingCrateSpace() : (moveTargetContainer.getFreeVolume() / sourceTemplate.getVolume());
/* 12041 */       int remainingItems = (int)Math.ceil(source.getBulkNumsFloat(false));
/*       */       
/* 12043 */       boolean transferringTheLastPortion = false;
/*       */       
/* 12045 */       if (amountMayCarry <= 0) {
/*       */         
/* 12047 */         performer.getCommunicator().sendNormalServerMessage("You can not even carry one of those.", (byte)3);
/* 12048 */         return true;
/*       */       } 
/* 12050 */       if (remainingItems < amountMayCarry && remainingItems <= playerAmountToTransfer) {
/*       */         
/* 12052 */         if (remainingItems > maxMayFit) {
/* 12053 */           amountToTransfer = maxMayFit;
/*       */         } else {
/*       */           
/* 12056 */           transferringTheLastPortion = true;
/* 12057 */           amountToTransfer = remainingItems;
/*       */         }
/*       */       
/* 12060 */       } else if (remainingItems < amountMayCarry && remainingItems > playerAmountToTransfer) {
/*       */         
/* 12062 */         amountToTransfer = playerAmountToTransfer;
/*       */       }
/* 12064 */       else if (maxMayFit < amountMayCarry && maxMayFit <= playerAmountToTransfer) {
/*       */         
/* 12066 */         amountToTransfer = maxMayFit;
/*       */       }
/* 12068 */       else if (maxMayFit < amountMayCarry && maxMayFit > playerAmountToTransfer) {
/*       */         
/* 12070 */         amountToTransfer = playerAmountToTransfer;
/*       */       }
/* 12072 */       else if (playerAmountToTransfer < amountMayCarry) {
/*       */         
/* 12074 */         amountToTransfer = playerAmountToTransfer;
/*       */       }
/*       */       else {
/*       */         
/* 12078 */         amountToTransfer = amountMayCarry;
/*       */       } 
/*       */ 
/*       */       
/* 12082 */       if ((moveTargetContainer.isCrate() && moveTargetContainer.getRemainingCrateSpace() < amountToTransfer) || (
/* 12083 */         !moveTargetContainer.isCrate() && moveTargetContainer.getFreeVolume() < sourceTemplate.getWeightGrams())) {
/*       */ 
/*       */         
/* 12086 */         if (Servers.localServer.testServer) {
/*       */           
/* 12088 */           String msg = "";
/*       */           
/* 12090 */           if (moveTargetContainer.isCrate()) {
/*       */             
/* 12092 */             msg = moveTargetContainer.getRemainingCrateSpace() + " remaining crate spots, trying to move " + amountToTransfer + " items.";
/*       */           }
/*       */           else {
/*       */             
/* 12096 */             String s1 = String.format("%,d", new Object[] { Integer.valueOf(source.getWeightGrams()) });
/* 12097 */             String m1 = String.format("%,d", new Object[] { Integer.valueOf(moveTargetContainer.getFreeVolume()) });
/*       */             
/* 12099 */             msg = "Source Volume: " + s1 + ", Target free Volume: " + m1;
/*       */           } 
/*       */           
/* 12102 */           performer.getCommunicator().sendNormalServerMessage(msg);
/*       */         } 
/* 12104 */         performer.getCommunicator().sendNormalServerMessage("Target is full.");
/* 12105 */         return true;
/*       */       } 
/*       */ 
/*       */       
/* 12109 */       if (counter == 1.0F) {
/*       */         
/* 12111 */         String actionString = "move " + source.getName() + " from one bulk container to another";
/* 12112 */         performer.getCommunicator().sendNormalServerMessage("You start to " + actionString + ".");
/* 12113 */         int actionTime = source.getBulkNums() / amountMayCarry * 10;
/* 12114 */         act.setTimeLeft(actionTime);
/* 12115 */         performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 12116 */         Server.getInstance().broadCastAction(performer.getName() + " starts to " + actionString, performer, 3);
/*       */ 
/*       */         
/* 12119 */         String countString = (act.getData() == 2147483647L) ? ("a whole plethora (" + source.getBulkNums() + ") of") : ("" + act.getData());
/* 12120 */         performer.getCommunicator().sendNormalServerMessage("Heave-ho, moving " + countString + " " + source.getName() + " at the rate of " + amountToTransfer + " per second!");
/*       */       } 
/*       */ 
/*       */ 
/*       */       
/* 12125 */       if (act.justTickedSecond()) {
/*       */         int totalVolume;
/* 12127 */         if (amountToTransfer < 1) {
/*       */           
/* 12129 */           if (Servers.localServer.testServer)
/* 12130 */             performer.getCommunicator().sendAlertServerMessage("Something went wrong, amount to transfer is less than 1:  " + amountToTransfer); 
/* 12131 */           performer.getCommunicator().sendNormalServerMessage("Not enough room in your inventory!");
/* 12132 */           return true;
/*       */         } 
/*       */ 
/*       */         
/* 12136 */         Item tempItem = null;
/* 12137 */         int countForMe = 0;
/* 12138 */         for (Item i : moveTargetContainer.getItemsAsArray()) {
/*       */ 
/*       */           
/* 12141 */           if (i.getRealTemplateId() == sourceTemplateID && i
/* 12142 */             .getData1() == source.getData1() && i
/* 12143 */             .getData2() == source.getData2() && i
/* 12144 */             .getAuxData() == sourceAuxByte && i
/* 12145 */             .getMaterial() == sourceMaterialByte) {
/*       */             
/* 12147 */             tempItem = i;
/* 12148 */             countForMe++;
/*       */           } 
/*       */         } 
/* 12151 */         if (countForMe > 1) {
/*       */           
/* 12153 */           performer.getCommunicator().sendAlertServerMessage("ERROR: Found more than one item of matching parameters, aborting.");
/* 12154 */           if (Servers.localServer.testServer)
/* 12155 */             performer.getCommunicator().sendNormalServerMessage("Counter = " + countForMe); 
/* 12156 */           return true;
/*       */         } 
/* 12158 */         Item moveTargetItem = tempItem;
/*       */ 
/*       */         
/* 12161 */         performer.getStatus().modifyStamina(-600.0F);
/*       */ 
/*       */ 
/*       */         
/* 12165 */         if (transferringTheLastPortion) {
/*       */           
/* 12167 */           totalVolume = source.getWeightGrams();
/*       */         } else {
/*       */           
/* 12170 */           totalVolume = amountToTransfer * sourceTemplate.getVolume();
/*       */         } 
/* 12172 */         if ((moveTargetContainer.isCrate() && moveTargetContainer.getRemainingCrateSpace() >= amountToTransfer) || (moveTargetContainer
/* 12173 */           .isBulkContainer() && moveTargetContainer.getFreeVolume() >= totalVolume)) {
/*       */ 
/*       */ 
/*       */           
/* 12177 */           if (moveTargetItem != null)
/*       */           {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/* 12184 */             float sourceAmountItems, sourceQL = source.getQualityLevel();
/* 12185 */             float targetQL = moveTargetItem.getQualityLevel();
/* 12186 */             float targetAmountItems = moveTargetItem.getBulkNumsFloat(false);
/*       */ 
/*       */             
/* 12189 */             if (transferringTheLastPortion) {
/* 12190 */               sourceAmountItems = source.getBulkNumsFloat(false);
/*       */             } else {
/* 12192 */               sourceAmountItems = amountToTransfer;
/*       */             } 
/* 12194 */             float top = sourceQL * sourceAmountItems + targetQL * targetAmountItems;
/* 12195 */             float bottom = sourceAmountItems + targetAmountItems;
/*       */             
/* 12197 */             float finalQL = top / bottom;
/*       */ 
/*       */             
/* 12200 */             source.setWeight(source.getWeightGrams() - totalVolume, true);
/* 12201 */             moveTargetItem.setWeight(moveTargetItem.getWeightGrams() + totalVolume, true);
/* 12202 */             moveTargetItem.setQualityLevel(finalQL);
/* 12203 */             moveTargetItem.setLastOwnerId(performer.getWurmId());
/* 12204 */             moveTargetContainer.updateModelNameOnGroundItem();
/* 12205 */             sourceParent.updateModelNameOnGroundItem();
/*       */             
/* 12207 */             if (transferringTheLastPortion) {
/* 12208 */               done = true;
/*       */             }
/*       */           }
/* 12211 */           else if (moveTargetItem == null)
/*       */           {
/*       */             
/*       */             try {
/* 12215 */               if (amountToTransfer == 1) {
/*       */                 int weightToSet;
/*       */                 
/* 12218 */                 if (transferringTheLastPortion) {
/* 12219 */                   weightToSet = source.getWeightGrams();
/*       */                 } else {
/* 12221 */                   weightToSet = sourceTemplate.getVolume();
/*       */                 } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/* 12228 */                 Item tempHolderItem = ItemFactory.createItem(669, source.getQualityLevel(), source.getMaterial(), (byte)0, performer.getName());
/* 12229 */                 source.setWeight(source.getWeightGrams() - weightToSet, true);
/* 12230 */                 tempHolderItem.setRealTemplate(sourceTemplateID);
/* 12231 */                 tempHolderItem.setWeight(weightToSet, true);
/* 12232 */                 tempHolderItem.setData1(source.getData1());
/* 12233 */                 tempHolderItem.setData2(source.getData2());
/* 12234 */                 tempHolderItem.setAuxData(source.getAuxData());
/*       */                 
/* 12236 */                 if (source.usesFoodState()) {
/*       */                   
/* 12238 */                   tempHolderItem.setName(source.getActualName());
/* 12239 */                   ItemMealData imd = ItemMealData.getItemMealData(source.getWurmId());
/* 12240 */                   if (imd != null)
/*       */                   {
/*       */                     
/* 12243 */                     ItemMealData.save(source.getWurmId(), imd.getRecipeId(), imd.getCalories(), imd.getCarbs(), imd.getFats(), imd
/* 12244 */                         .getProteins(), imd.getBonus(), imd.getStages(), imd.getIngredients());
/*       */                   }
/*       */                 } 
/*       */                 
/* 12248 */                 tempHolderItem.setLastOwnerId(performer.getWurmId());
/* 12249 */                 moveTargetContainer.insertItem(tempHolderItem, true);
/* 12250 */                 moveTargetContainer.updateModelNameOnGroundItem();
/* 12251 */                 sourceParent.updateModelNameOnGroundItem();
/* 12252 */                 done = true;
/*       */               } else {
/*       */                 float sourceAmountItems;
/*       */                 
/* 12256 */                 if (source.getWeightGrams() < sourceTemplate.getVolume()) {
/*       */                   
/* 12258 */                   performer.getCommunicator().sendAlertServerMessage("Something went wrong, source weight less than template even though transferring more than one item.......");
/*       */                   
/* 12260 */                   return true;
/*       */                 } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/* 12268 */                 int modifiedTotalVolume = totalVolume - sourceTemplate.getVolume();
/* 12269 */                 Item tempHolderItem = ItemFactory.createItem(669, source.getQualityLevel(), source.getMaterial(), (byte)0, performer.getName());
/* 12270 */                 tempHolderItem.setRealTemplate(sourceTemplateID);
/* 12271 */                 tempHolderItem.setWeight(sourceTemplate.getVolume(), true);
/* 12272 */                 tempHolderItem.setLastOwnerId(performer.getWurmId());
/*       */ 
/*       */                 
/* 12275 */                 float sourceQL = source.getQualityLevel();
/* 12276 */                 float targetQL = tempHolderItem.getQualityLevel();
/* 12277 */                 float targetAmountItems = tempHolderItem.getBulkNumsFloat(false);
/*       */ 
/*       */                 
/* 12280 */                 if (transferringTheLastPortion) {
/* 12281 */                   sourceAmountItems = source.getBulkNumsFloat(false);
/*       */                 } else {
/* 12283 */                   sourceAmountItems = amountToTransfer;
/*       */                 } 
/* 12285 */                 float top = sourceQL * sourceAmountItems + targetQL * targetAmountItems;
/* 12286 */                 float bottom = sourceAmountItems + targetAmountItems;
/*       */                 
/* 12288 */                 float finalQL = top / bottom;
/*       */ 
/*       */                 
/* 12291 */                 source.setWeight(source.getWeightGrams() - totalVolume, true);
/* 12292 */                 tempHolderItem.setWeight(tempHolderItem.getWeightGrams() + modifiedTotalVolume, true);
/* 12293 */                 tempHolderItem.setQualityLevel(finalQL);
/* 12294 */                 tempHolderItem.setData1(source.getData1());
/* 12295 */                 tempHolderItem.setData2(source.getData2());
/* 12296 */                 tempHolderItem.setAuxData(source.getAuxData());
/*       */                 
/* 12298 */                 if (source.usesFoodState()) {
/*       */                   
/* 12300 */                   tempHolderItem.setName(source.getActualName());
/* 12301 */                   ItemMealData imd = ItemMealData.getItemMealData(source.getWurmId());
/* 12302 */                   if (imd != null)
/*       */                   {
/*       */                     
/* 12305 */                     ItemMealData.save(source.getWurmId(), imd.getRecipeId(), imd.getCalories(), imd.getCarbs(), imd.getFats(), imd
/* 12306 */                         .getProteins(), imd.getBonus(), imd.getStages(), imd.getIngredients());
/*       */                   }
/*       */                 } 
/*       */ 
/*       */                 
/* 12311 */                 moveTargetContainer.insertItem(tempHolderItem, true);
/* 12312 */                 moveTargetContainer.updateModelNameOnGroundItem();
/* 12313 */                 sourceParent.updateModelNameOnGroundItem();
/*       */                 
/* 12315 */                 if (transferringTheLastPortion) {
/* 12316 */                   done = true;
/*       */                 }
/*       */               }
/*       */             
/* 12320 */             } catch (FailedException e) {
/*       */               
/* 12322 */               logger.warning("FailEX on creating item for bulk transfer" + e);
/* 12323 */               performer.getCommunicator().sendAlertServerMessage("Uh-oh, something went wrong, if this persists, contact a developer.");
/* 12324 */               return true;
/* 12325 */             } catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */ 
/*       */           
/*       */           }
/*       */           else
/*       */           {
/*       */             
/* 12332 */             if (Servers.localServer.testServer) {
/*       */               
/* 12334 */               String msg = "Option 1: ";
/*       */               
/* 12336 */               if (moveTargetContainer.isCrate()) {
/*       */                 
/* 12338 */                 msg = moveTargetContainer.getRemainingCrateSpace() + " remaining crate spots, trying to move " + amountToTransfer + " items.";
/*       */               }
/*       */               else {
/*       */                 
/* 12342 */                 String s1 = String.format("%,d", new Object[] { Integer.valueOf(source.getWeightGrams()) });
/* 12343 */                 String m1 = String.format("%,d", new Object[] { Integer.valueOf(moveTargetContainer.getFreeVolume()) });
/*       */                 
/* 12345 */                 msg = "Source Volume: " + s1 + ", Target free Volume: " + m1;
/*       */               } 
/*       */               
/* 12348 */               performer.getCommunicator().sendNormalServerMessage(msg);
/*       */             } 
/* 12350 */             performer.getCommunicator().sendNormalServerMessage("Unnngh... not enough space!");
/* 12351 */             return true;
/*       */           }
/*       */         
/*       */         } else {
/*       */           
/* 12356 */           if (Servers.localServer.testServer) {
/*       */             
/* 12358 */             String msg = "Option 2: ";
/*       */             
/* 12360 */             if (moveTargetContainer.isCrate()) {
/*       */               
/* 12362 */               msg = moveTargetContainer.getRemainingCrateSpace() + " remaining crate spots, trying to move " + amountToTransfer + " items.";
/*       */             }
/*       */             else {
/*       */               
/* 12366 */               String s1 = String.format("%,d", new Object[] { Integer.valueOf(source.getWeightGrams()) });
/* 12367 */               String m1 = String.format("%,d", new Object[] { Integer.valueOf(moveTargetContainer.getFreeVolume()) });
/*       */               
/* 12369 */               msg = "Source Volume: " + s1 + ", Target free Volume: " + m1;
/*       */             } 
/*       */             
/* 12372 */             performer.getCommunicator().sendNormalServerMessage(msg);
/*       */           } 
/*       */           
/* 12375 */           performer.getCommunicator().sendNormalServerMessage("Unnngh... not enough space!");
/* 12376 */           return true;
/*       */         } 
/*       */ 
/*       */         
/* 12380 */         act.setData((playerAmountToTransfer - amountToTransfer));
/* 12381 */         if (done)
/*       */         {
/* 12383 */           if (moveTargetItem != null) {
/* 12384 */             moveTargetItem.setBusy(false);
/*       */           }
/*       */         }
/*       */       } 
/*       */     } 
/* 12389 */     if (act.getData() <= 0L || done) {
/*       */       
/* 12391 */       if (source != null)
/* 12392 */         source.setBusy(false); 
/* 12393 */       performer.getCommunicator().sendNormalServerMessage("Done!");
/* 12394 */       return true;
/*       */     } 
/*       */     
/* 12397 */     return false;
/*       */   }
/*       */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ItemBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */