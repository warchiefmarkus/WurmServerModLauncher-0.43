/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.combat.Archery;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.combat.Arrows;
/*      */ import com.wurmonline.server.combat.Battle;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.combat.SpecialMove;
/*      */ import com.wurmonline.server.creatures.Brand;
/*      */ import com.wurmonline.server.creatures.CombatHandler;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Offspring;
/*      */ import com.wurmonline.server.creatures.Traits;
/*      */ import com.wurmonline.server.creatures.VisionArea;
/*      */ import com.wurmonline.server.creatures.Wagoner;
/*      */ import com.wurmonline.server.creatures.ai.Path;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.effects.EffectFactory;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.items.Trade;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Cultist;
/*      */ import com.wurmonline.server.players.Friend;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.PermissionsPlayerList;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.questions.AppointmentsQuestion;
/*      */ import com.wurmonline.server.questions.CreatureChangeAgeQuestion;
/*      */ import com.wurmonline.server.questions.CultQuestion;
/*      */ import com.wurmonline.server.questions.DuelQuestion;
/*      */ import com.wurmonline.server.questions.ManageObjectList;
/*      */ import com.wurmonline.server.questions.ManagePermissions;
/*      */ import com.wurmonline.server.questions.MissionManager;
/*      */ import com.wurmonline.server.questions.MissionQuestion;
/*      */ import com.wurmonline.server.questions.PermissionsHistory;
/*      */ import com.wurmonline.server.questions.QuestionParser;
/*      */ import com.wurmonline.server.questions.RealDeathQuestion;
/*      */ import com.wurmonline.server.questions.SimplePopup;
/*      */ import com.wurmonline.server.questions.TeamManagementQuestion;
/*      */ import com.wurmonline.server.questions.TraderManagementQuestion;
/*      */ import com.wurmonline.server.questions.WagonerHistory;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.spells.Dominate;
/*      */ import com.wurmonline.server.spells.Spell;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.spells.Spells;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.tutorial.MissionTrigger;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.tutorial.OldMission;
/*      */ import com.wurmonline.server.utils.CoordUtils;
/*      */ import com.wurmonline.server.utils.CreatureLineSegment;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.PvPAlliance;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageRole;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VirtualZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.AttitudeConstants;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.io.IOException;
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
/*      */ public final class CreatureBehaviour
/*      */   extends Behaviour
/*      */   implements CreatureTypes, VillageStatus, MiscConstants, TimeConstants, AttitudeConstants, CreatureTemplateIds
/*      */ {
/*  121 */   private static final Logger logger = Logger.getLogger(CreatureBehaviour.class.getName());
/*      */ 
/*      */   
/*      */   CreatureBehaviour() {
/*  125 */     super((short)4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Creature target) {
/*  137 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  138 */     toReturn.addAll(Actions.getDefaultCreatureActions());
/*  139 */     addEmotes(toReturn);
/*  140 */     if (target instanceof Player) {
/*      */       
/*  142 */       if (performer instanceof Player)
/*      */       {
/*  144 */         if (target.getKingdomId() == performer.getKingdomId()) {
/*      */           
/*  146 */           toReturn.add(new ActionEntry((short)-1, "Friends", "", emptyIntArr));
/*  147 */           Friend[] friends = ((Player)performer).getFriends();
/*  148 */           boolean friend = false;
/*  149 */           long targid = target.getWurmId();
/*  150 */           for (Friend lFriend : friends) {
/*      */             
/*  152 */             if (lFriend.getFriendId() == targid) {
/*      */               
/*  154 */               friend = true;
/*      */               break;
/*      */             } 
/*      */           } 
/*  158 */           if (friend) {
/*  159 */             toReturn.add(Actions.actionEntrys[61]);
/*      */           } else {
/*  161 */             toReturn.add(Actions.actionEntrys[60]);
/*  162 */           }  if (performer.mayInviteTeam()) {
/*      */             
/*  164 */             if (target.getTeam() == null || performer.getTeam() != target.getTeam())
/*  165 */               toReturn.add(Actions.actionEntrys[469]); 
/*  166 */             if (performer.getTeam() == target.getTeam() && (performer
/*  167 */               .isTeamLeader() || (performer.mayInviteTeam() && !target.mayInviteTeam())))
/*  168 */               toReturn.add(Actions.actionEntrys[470]); 
/*  169 */             if (performer.isTeamLeader())
/*  170 */               toReturn.add(Actions.actionEntrys[471]); 
/*      */           } 
/*  172 */           Village perfVillage = performer.getCitizenVillage();
/*  173 */           if (perfVillage != null) {
/*      */             
/*  175 */             Village targVillage = target.getCitizenVillage();
/*  176 */             if (!perfVillage.equals(targVillage)) {
/*      */               
/*  178 */               boolean diplomat = false;
/*  179 */               boolean mayinvite = false;
/*      */               
/*  181 */               boolean peacemaker = false;
/*  182 */               int size = 0;
/*  183 */               if (perfVillage.mayDoDiplomacy(performer) && targVillage != null) {
/*      */                 
/*  185 */                 boolean atPeace = perfVillage.isAtPeaceWith(targVillage);
/*  186 */                 if (atPeace && targVillage.mayDoDiplomacy(target) && !perfVillage.isAlly(targVillage)) {
/*      */                   
/*  188 */                   diplomat = true;
/*  189 */                   size--;
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
/*  201 */                 if (targVillage.kingdom == performer.getKingdomId() && !atPeace) {
/*      */ 
/*      */                   
/*  204 */                   if (perfVillage.mayDeclareWarOn(targVillage))
/*      */                   {
/*      */                     
/*  207 */                     size--;
/*      */                   }
/*      */                 }
/*  210 */                 else if (targVillage.kingdom == performer.getKingdomId() && !atPeace) {
/*      */                   
/*  212 */                   peacemaker = true;
/*  213 */                   size--;
/*      */                 } 
/*      */               } 
/*  216 */               if (perfVillage.isActionAllowed((short)73, performer))
/*      */               {
/*  218 */                 if (perfVillage.acceptsNewCitizens()) {
/*      */                   
/*  220 */                   Village targvil = target.getCitizenVillage();
/*  221 */                   if (!perfVillage.equals(targvil)) {
/*      */                     
/*  223 */                     mayinvite = true;
/*  224 */                     size--;
/*      */                   } 
/*      */                 } 
/*      */               }
/*  228 */               if (size > 0)
/*  229 */                 toReturn.add(new ActionEntry((short)size, "Village", "Village options", emptyIntArr)); 
/*  230 */               if (mayinvite)
/*  231 */                 toReturn.add(Actions.actionEntrys[73]); 
/*  232 */               if (diplomat) {
/*  233 */                 toReturn.add(Actions.actionEntrys[81]);
/*      */               }
/*      */               
/*  236 */               if (peacemaker) {
/*  237 */                 toReturn.add(Actions.actionEntrys[210]);
/*      */               }
/*      */             } 
/*      */           } 
/*  241 */         } else if (performer.getPower() >= 2 || (target
/*  242 */           .acceptsInvitations() && target.getKingdomId() != performer.getKingdomId())) {
/*      */           
/*  244 */           toReturn.add(Actions.actionEntrys[89]);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  255 */       if (performer.getPower() >= 2)
/*      */       {
/*  257 */         toReturn.add(Actions.actionEntrys[89]);
/*      */       }
/*  259 */       if (target.isNpcTrader())
/*      */       {
/*  261 */         if (mayDismissMerchant(performer, target))
/*  262 */           toReturn.add(Actions.actionEntrys[62]); 
/*      */       }
/*  264 */       if (target.isWagoner() && target.getWagoner() != null && target.getWagoner().getVillageId() != -1)
/*      */       {
/*      */         
/*  267 */         if (target.getWagoner().getOwnerId() == performer.getWurmId() || performer.getPower() > 1) {
/*      */           
/*  269 */           List<ActionEntry> waglist = new LinkedList<>();
/*  270 */           waglist.add(new ActionEntry((short)-2, "Permissions", "viewing"));
/*  271 */           waglist.add(Actions.actionEntrys[863]);
/*  272 */           waglist.add(new ActionEntry((short)691, "History Of Wagoner", "viewing"));
/*  273 */           waglist.add(Actions.actionEntrys[919]);
/*  274 */           waglist.add(new ActionEntry((short)566, "Manage chat options", "managing"));
/*  275 */           toReturn.add(new ActionEntry((short)-(waglist.size() - 2), target.getWagoner().getName(), "wagoner"));
/*  276 */           toReturn.addAll(waglist);
/*      */         } 
/*      */       }
/*  279 */       if (target.isHorse() || target.isUnicorn())
/*      */       {
/*  281 */         for (Item i : target.getBody().getAllItems()) {
/*      */           
/*  283 */           if (i.isSaddleBags()) {
/*  284 */             toReturn.add(new ActionEntry((short)3, "Open " + i.getName(), "opening"));
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*  289 */     if (target.getTemplate().isRoyalAspiration() && !Servers.localServer.isChallengeServer())
/*      */     {
/*  291 */       toReturn.add(Actions.actionEntrys[353]);
/*      */     }
/*      */     
/*  294 */     if (performer.getPower() >= 2 || (performer
/*  295 */       .getCultist() != null && performer.getCultist().mayCreatureInfo()))
/*      */     {
/*  297 */       toReturn.add(Actions.actionEntrys[185]);
/*      */     }
/*      */     
/*  300 */     if (performer.isRoyalAnnouncer())
/*      */     {
/*  302 */       if (!performer.isFighting() && !target.isFighting())
/*  303 */         toReturn.add(Actions.actionEntrys[357]); 
/*      */     }
/*  305 */     if (target.isGuide()) {
/*      */       
/*  307 */       toReturn.add(Actions.actionEntrys[351]);
/*  308 */       if (target.getKingdomId() != performer.getKingdomId())
/*      */       {
/*  310 */         toReturn.add(Actions.actionEntrys[213]);
/*      */       }
/*      */     } 
/*  313 */     if (target.isTrader() && !target.isFighting())
/*      */     {
/*  315 */       if (target.getFloorLevel() == performer.getFloorLevel() && (
/*  316 */         !target.isNpcTrader() || performer.getVehicle() == -10L || performer.isVehicleCommander())) {
/*      */         
/*  318 */         if (performer.isFriendlyKingdom(target.getKingdomId()) || performer.getPower() > 0)
/*      */         {
/*  320 */           toReturn.add(Actions.actionEntrys[63]);
/*      */         }
/*      */ 
/*      */         
/*  324 */         if (Servers.localServer.PVPSERVER && Servers.localServer.isChallengeOrEpicServer() && !Servers.localServer.HOMESERVER)
/*      */         {
/*      */           
/*  327 */           if (target.isNpcTrader())
/*      */           {
/*  329 */             if (!target.isFriendlyKingdom(performer.getKingdomId())) {
/*      */               
/*  331 */               Shop shop = Economy.getEconomy().getShop(target);
/*  332 */               if (shop != null && shop.isPersonal()) {
/*      */                 
/*  334 */                 toReturn.add(Actions.actionEntrys[63]);
/*  335 */                 toReturn.add(Actions.actionEntrys[537]);
/*      */               } 
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*  342 */     if (!target.isInvulnerable() && performer.getAttitude(target) == 2) {
/*      */       
/*  344 */       if (performer.getCultist() != null && performer.getCultist().mayDealFinalBreath())
/*      */       {
/*  346 */         if (performer.isWithinDistanceTo(target, 8.0F)) {
/*  347 */           toReturn.add(Actions.actionEntrys[490]);
/*      */         }
/*      */       }
/*  350 */     } else if (!target.isInvulnerable()) {
/*      */ 
/*      */       
/*  353 */       if (!target.isPlayer())
/*      */       {
/*  355 */         if (!target.isHuman() && !target.isGhost() && !target.isReborn())
/*      */         {
/*      */ 
/*      */           
/*  359 */           if (!target.isCaredFor() || target.getCareTakerId() == performer.getWurmId()) {
/*      */             
/*  361 */             BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  362 */             if (result == null)
/*      */             {
/*  364 */               toReturn.add(Actions.actionEntrys[493]);
/*      */             }
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*  370 */     if (target.isBartender() && !target.isFighting()) {
/*      */       
/*  372 */       if (performer.isFriendlyKingdom(target.getKingdomId()) || performer.getPower() > 0)
/*      */       {
/*  374 */         toReturn.add(Actions.actionEntrys[91]);
/*      */       }
/*      */     }
/*  377 */     else if (target.isPlayer() && (performer
/*  378 */       .getPower() > 0 || (performer.getCultist() != null && performer.getCultist().mayRefresh()))) {
/*      */       
/*  380 */       if ((performer.isPaying() && performer.isFriendlyKingdom(target.getKingdomId())) || performer
/*  381 */         .getPower() > 0)
/*  382 */         toReturn.add(new ActionEntry((short)91, "Refresh", "refreshing", emptyIntArr)); 
/*      */     } 
/*  384 */     if (!target.isInvulnerable() || performer.getPower() >= 5) {
/*      */       
/*  386 */       toReturn.add(new ActionEntry((short)-1, "Attacks", "attacks"));
/*  387 */       toReturn.add(Actions.actionEntrys[326]);
/*  388 */       if (!performer.isFighting()) {
/*      */         
/*  390 */         if (target.isPlayer() && target.getKingdomId() == performer.getKingdomId())
/*      */         {
/*  392 */           toReturn.add(new ActionEntry((short)-2, "Sparring", "Sparring"));
/*  393 */           toReturn.add(Actions.actionEntrys[344]);
/*  394 */           toReturn.add(Actions.actionEntrys[343]);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  399 */       else if (target.isFighting() && target.opponent != performer) {
/*      */         
/*  401 */         if (!target.isPlayer() || !performer.isPlayer() || (target
/*  402 */           .isOnPvPServer() && performer.isOnPvPServer())) {
/*  403 */           toReturn.add(Actions.actionEntrys[103]);
/*      */         }
/*      */       } 
/*      */     } 
/*  407 */     if (target.getTemplate().getTemplateId() == 46 || target
/*  408 */       .getTemplate().getTemplateId() == 47)
/*      */     {
/*  410 */       toReturn.add(Actions.actionEntrys[214]);
/*      */     }
/*  412 */     if (target.getLeader() == performer) {
/*      */       
/*  414 */       toReturn.add(Actions.actionEntrys[107]);
/*      */ 
/*      */     
/*      */     }
/*  418 */     else if ((performer.getFollowers()).length == 1) {
/*      */       
/*  420 */       if (performer.getFollowers()[0].mayMate(target)) {
/*  421 */         toReturn.add(Actions.actionEntrys[379]);
/*      */       }
/*      */     } 
/*  424 */     if (!target.isWagoner())
/*  425 */       addVehicleOptions(performer, target, toReturn); 
/*  426 */     if (performer.getPet() != null) {
/*      */       
/*  428 */       boolean attack = false;
/*  429 */       boolean give = false;
/*  430 */       short nums = -1;
/*  431 */       if (target.getAttitude(performer) == 2 || performer.getPower() > 0) {
/*      */         
/*  433 */         if (!target.isInvulnerable())
/*      */         {
/*  435 */           attack = true;
/*  436 */           nums = (short)(nums - 1);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  441 */       else if (target instanceof Player && target.getPet() == null) {
/*      */         
/*  443 */         give = true;
/*  444 */         nums = (short)(nums - 1);
/*      */       } 
/*      */       
/*  447 */       if (performer.getPet().isAnimal() && !performer.getPet().isReborn())
/*  448 */         nums = (short)(nums - 1); 
/*  449 */       toReturn.add(new ActionEntry(nums, "Pet", "Pet"));
/*  450 */       toReturn.add(Actions.actionEntrys[41]);
/*  451 */       if (attack) {
/*  452 */         toReturn.add(Actions.actionEntrys[42]);
/*  453 */       } else if (give) {
/*  454 */         toReturn.add(Actions.actionEntrys[43]);
/*  455 */       }  if (performer.getPet().isAnimal() && !performer.getPet().isReborn())
/*      */       {
/*  457 */         if (performer.getPet().isStayonline()) {
/*  458 */           toReturn.add(Actions.actionEntrys[45]);
/*      */         } else {
/*  460 */           toReturn.add(Actions.actionEntrys[44]);
/*      */         }  } 
/*      */     } 
/*  463 */     if (performer.getVehicle() != -10L)
/*      */     {
/*  465 */       if (performer.isVehicleCommander())
/*      */       {
/*  467 */         if (target.getHitched() != null && target.getHitched().getWurmid() == performer.getVehicle())
/*      */         {
/*  469 */           toReturn.add(Actions.actionEntrys[378]);
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
/*  486 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addVehicleOptions(Creature performer, Creature target, List<ActionEntry> toReturn) {
/*  491 */     if (Constants.enabledMounts) {
/*      */       
/*  493 */       if (performer.getVehicle() == -10L) {
/*      */         
/*  495 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPositionZ(), 8.0F)) {
/*      */           
/*  497 */           BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  498 */           if (result == null)
/*      */           {
/*  500 */             if (!performer.isClimbing()) {
/*      */               
/*  502 */               boolean addedPassenger = false;
/*  503 */               boolean addedDriver = false;
/*  504 */               Vehicle vehicle = Vehicles.getVehicle(target);
/*  505 */               if (vehicle != null)
/*      */               {
/*  507 */                 for (Seat lSeat : vehicle.seats) {
/*      */                   
/*  509 */                   if (!addedDriver && !lSeat.isOccupied() && lSeat.type == 0) {
/*      */ 
/*      */ 
/*      */                     
/*  513 */                     if (!addedDriver && !Servers.isThisAPvpServer() && target.isBranded())
/*      */                     {
/*      */                       
/*  516 */                       if (target.getLeader() == performer && target.mayCommand(performer)) {
/*      */                         
/*  518 */                         toReturn.add(Actions.actionEntrys[11]);
/*  519 */                         addedDriver = true;
/*      */                       } 
/*      */                     }
/*  522 */                     if (!addedDriver && (target.dominator == performer
/*  523 */                       .getWurmId() || target.getLeader() == performer))
/*      */                     {
/*  525 */                       toReturn.add(Actions.actionEntrys[11]);
/*  526 */                       addedDriver = true;
/*      */                     }
/*      */                   
/*  529 */                   } else if (!addedPassenger && !lSeat.isOccupied() && lSeat.type == 1) {
/*      */ 
/*      */                     
/*  532 */                     if (!Servers.isThisAPvpServer() && target.isBranded()) {
/*      */ 
/*      */                       
/*  535 */                       if (target.mayPassenger(performer))
/*      */                       {
/*  537 */                         toReturn.add(Actions.actionEntrys[332]);
/*  538 */                         addedPassenger = true;
/*      */                       
/*      */                       }
/*      */                     
/*      */                     }
/*  543 */                     else if (target.getDominator() != null && performer.getKingdomId() == target.getDominator().getKingdomId()) {
/*      */                       
/*  545 */                       toReturn.add(Actions.actionEntrys[332]);
/*  546 */                       addedPassenger = true;
/*      */                     } 
/*      */                     
/*  549 */                     if (addedPassenger && addedDriver) {
/*      */                       break;
/*      */                     }
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*  560 */         Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle());
/*  561 */         if (vehicle.isChair()) {
/*  562 */           toReturn.add(Actions.actionEntrys[708]);
/*      */         } else {
/*  564 */           toReturn.add(Actions.actionEntrys[333]);
/*      */         } 
/*  566 */       }  if (!target.isWagoner()) {
/*      */         
/*  568 */         List<ActionEntry> permissions = new LinkedList<>();
/*  569 */         if (target.mayManage(performer))
/*  570 */           permissions.add(Actions.actionEntrys[663]); 
/*  571 */         if (target.maySeeHistory(performer)) {
/*  572 */           permissions.add(new ActionEntry((short)691, "History of Animal", "viewing"));
/*      */         }
/*  574 */         if (!permissions.isEmpty()) {
/*      */           
/*  576 */           if (permissions.size() > 1) {
/*      */             
/*  578 */             Collections.sort(permissions);
/*  579 */             toReturn.add(new ActionEntry((short)-permissions.size(), "Permissions", "viewing"));
/*      */           } 
/*  581 */           toReturn.addAll(permissions);
/*      */         } 
/*      */       } 
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
/*      */   @Nonnull
/*      */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Creature target) {
/*  598 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  599 */     toReturn.addAll(getBehavioursFor(performer, target));
/*  600 */     int stid = subject.getTemplateId();
/*      */     
/*  602 */     if (stid == 330 || stid == 331 || stid == 334) {
/*      */       
/*  604 */       if (subject.getOwnerId() == performer.getWurmId()) {
/*  605 */         toReturn.add(Actions.actionEntrys[118]);
/*      */       }
/*  607 */     } else if (stid == 489 || ((stid == 176 || stid == 315) && performer
/*  608 */       .getPower() >= 2)) {
/*      */       
/*  610 */       if (subject.getOwnerId() == performer.getWurmId()) {
/*  611 */         toReturn.add(Actions.actionEntrys[329]);
/*      */       }
/*  613 */     } else if (subject.isWeaponBow() && !target.isInvulnerable()) {
/*      */       
/*  615 */       int numacts = -1;
/*      */       
/*      */       try {
/*  618 */         int skillnum = subject.getPrimarySkill();
/*  619 */         if (skillnum != -10L) {
/*      */           
/*  621 */           Skill skill = performer.getSkills().getSkill(skillnum);
/*  622 */           double knowl = skill.getRealKnowledge();
/*  623 */           if (knowl > 30.0D)
/*  624 */             numacts--; 
/*  625 */           if (knowl > 35.0D)
/*  626 */             numacts--; 
/*  627 */           if (knowl > 40.0D) {
/*      */             
/*  629 */             numacts--;
/*  630 */             numacts--;
/*      */           } 
/*  632 */           if (knowl > 50.0D)
/*  633 */             numacts--; 
/*  634 */           if (knowl > 60.0D)
/*  635 */             numacts--; 
/*  636 */           if (knowl > 70.0D) {
/*  637 */             numacts--;
/*      */           }
/*      */         } 
/*  640 */       } catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */       
/*  644 */       toReturn.add(new ActionEntry((short)numacts, "Bow", "shooting", emptyIntArr));
/*  645 */       toReturn.add(Actions.actionEntrys[124]);
/*  646 */       if (numacts < -1)
/*  647 */         toReturn.add(Actions.actionEntrys[125]); 
/*  648 */       if (numacts < -2)
/*  649 */         toReturn.add(Actions.actionEntrys[128]); 
/*  650 */       if (numacts < -3) {
/*      */         
/*  652 */         toReturn.add(Actions.actionEntrys[129]);
/*  653 */         toReturn.add(Actions.actionEntrys[130]);
/*      */       } 
/*      */       
/*  656 */       if (numacts < -5)
/*  657 */         toReturn.add(Actions.actionEntrys[126]); 
/*  658 */       if (numacts < -6)
/*  659 */         toReturn.add(Actions.actionEntrys[127]); 
/*  660 */       if (numacts < -7) {
/*  661 */         toReturn.add(Actions.actionEntrys[131]);
/*      */       }
/*  663 */     } else if (stid == 676) {
/*      */       
/*  665 */       if (subject.getOwnerId() == performer.getWurmId()) {
/*  666 */         toReturn.add(Actions.actionEntrys[472]);
/*      */       }
/*  668 */     } else if (performer.isFighting() && !target.isInvulnerable()) {
/*      */       
/*  670 */       toReturn.add(new ActionEntry((short)-1, "Throw", "throwing", new int[0]));
/*  671 */       toReturn.add(new ActionEntry((short)342, 8, "At target", "throwing", new int[] { 22, 3, 23 }, 200, false));
/*      */ 
/*      */     
/*      */     }
/*  675 */     else if (subject.getTemplateId() == 1276 || subject
/*  676 */       .getTemplateId() == 833 || (subject
/*  677 */       .getTemplateId() == 1258 && subject.getRealTemplateId() == 1195) || (subject
/*  678 */       .getTemplateId() == 1177 && subject.getRealTemplateId() == 1195)) {
/*      */       
/*  680 */       toReturn.add(Actions.actionEntrys[745]);
/*      */     } 
/*      */     
/*  683 */     if (subject.getTemplate().isRune()) {
/*      */       
/*  685 */       Skill soulDepth = performer.getSoulDepth();
/*  686 */       double diff = (20.0F + subject.getDamage()) - (subject.getCurrentQualityLevel() + subject.getRarity()) - 45.0D;
/*  687 */       double chance = soulDepth.getChance(diff, null, subject.getCurrentQualityLevel());
/*  688 */       if (RuneUtilities.isSingleUseRune(subject) && ((RuneUtilities.getSpellForRune(subject) != null && 
/*  689 */         RuneUtilities.getSpellForRune(subject).isTargetCreature()) || 
/*  690 */         RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(subject), RuneUtilities.ModifierEffect.SINGLE_REFRESH) > 0.0F || 
/*  691 */         RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(subject), RuneUtilities.ModifierEffect.SINGLE_CHANGE_AGE) > 0.0F)) {
/*  692 */         toReturn.add(new ActionEntry((short)945, "Use Rune: " + chance + "%", "using rune", emptyIntArr));
/*      */       }
/*      */     } 
/*  695 */     if (performer.getPower() >= 2) {
/*      */       
/*  697 */       toReturn.add(Actions.actionEntrys[179]);
/*  698 */       toReturn.add(Actions.actionEntrys[47]);
/*      */     } 
/*  700 */     if (subject.getTemplateId() == 76)
/*      */     {
/*  702 */       if (subject.isFlyTrap())
/*      */       {
/*  704 */         toReturn.add(Actions.actionEntrys[938]);
/*      */       }
/*      */     }
/*  707 */     if (target instanceof Player) {
/*      */       
/*  709 */       if (subject.isRoyal())
/*      */       {
/*  711 */         if (stid == 535 || stid == 529 || stid == 532)
/*      */         {
/*      */           
/*  714 */           toReturn.add(Actions.actionEntrys[354]);
/*      */         }
/*      */       }
/*      */       
/*  718 */       if (performer.getDeity() != null && subject.isHolyItem())
/*      */       {
/*  720 */         if (subject.isHolyItem(performer.getDeity())) {
/*      */           
/*  722 */           if (target.getDeity() != performer.getDeity()) {
/*      */             
/*      */             try
/*      */             {
/*  726 */               target.getCurrentAction();
/*      */             }
/*  728 */             catch (NoSuchActionException nsa)
/*      */             {
/*  730 */               toReturn.add(new ActionEntry((short)-1, "Religion", "religion", emptyIntArr));
/*  731 */               toReturn.add(Actions.actionEntrys[213]);
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  736 */           else if (!target.isPriest()) {
/*      */             
/*  738 */             if (performer.getFaith() > 40.0F && target.getFaith() == 30.0F)
/*      */             {
/*  740 */               if (!Servers.localServer.PVPSERVER || Servers.localServer.testServer)
/*  741 */                 toReturn.add(Actions.actionEntrys[286]); 
/*      */             }
/*  743 */             toReturn.add(Actions.actionEntrys[115]);
/*      */           } 
/*      */           
/*  746 */           if (target.getDeity() != null && performer.getDeity().getTemplateDeity() == target.getDeity().getTemplateDeity())
/*      */           {
/*  748 */             toReturn.add(Actions.actionEntrys[399]);
/*      */           }
/*      */         } 
/*      */       }
/*  752 */       if (subject.isMeditation()) {
/*      */         
/*  754 */         Cultist perfCultist = Cultist.getCultist(performer.getWurmId());
/*  755 */         if (perfCultist != null && perfCultist.getLevel() > 4) {
/*      */           
/*  757 */           Cultist respCultist = Cultist.getCultist(target.getWurmId());
/*      */           
/*  759 */           if (respCultist != null && respCultist.getPath() == perfCultist.getPath())
/*      */           {
/*  761 */             if (respCultist != null && respCultist.getLevel() > 0)
/*      */             {
/*  763 */               if (perfCultist.getLevel() - respCultist.getLevel() == 3)
/*  764 */                 toReturn.add(Actions.actionEntrys[387]); 
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*  769 */       if (subject.isPuppet())
/*      */       {
/*  771 */         toReturn.add(Actions.actionEntrys[397]);
/*      */       }
/*  773 */       if (performer instanceof Player && stid == 903)
/*      */       {
/*  775 */         toReturn.add(Actions.actionEntrys[637]);
/*  776 */         toReturn.add(Actions.actionEntrys[640]);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  782 */       if (performer.getPower() >= 2) {
/*      */         
/*  784 */         toReturn.add(Actions.actionEntrys[89]);
/*  785 */         if (performer.getPower() >= 4 && target.isLeadable(performer) && stid == 176) {
/*      */           
/*  787 */           Brand brand = Creatures.getInstance().getBrand(target.getWurmId());
/*  788 */           if (brand != null)
/*  789 */             toReturn.add(Actions.actionEntrys[643]); 
/*      */         } 
/*  791 */         if (target.isFish() && stid == 176)
/*  792 */           toReturn.add(Actions.actionEntrys[88]); 
/*      */       } 
/*  794 */       if (Features.Feature.WAGONER.isEnabled() && target.isWagoner() && performer.getPower() >= 4 && (stid == 176 || (stid == 1129 && target
/*      */         
/*  796 */         .getWurmId() == subject.getData()))) {
/*      */         
/*  798 */         Wagoner wagoner = Wagoner.getWagoner(target.getWurmId());
/*  799 */         if (wagoner != null) {
/*      */           
/*  801 */           List<ActionEntry> testlist = new LinkedList<>();
/*  802 */           if (wagoner.getState() == 0) {
/*      */             
/*  804 */             testlist.add(new ActionEntry((short)140, "Send to bed", "testing"));
/*  805 */             testlist.add(new ActionEntry((short)111, "Test delivery", "testing"));
/*      */           } 
/*  807 */           if (wagoner.getState() == 2)
/*  808 */             testlist.add(new ActionEntry((short)30, "Wake up", "testing")); 
/*  809 */           if (!testlist.isEmpty()) {
/*      */             
/*  811 */             toReturn.add(new ActionEntry((short)-testlist.size(), "Test only", "testing"));
/*  812 */             toReturn.addAll(testlist);
/*      */           } 
/*      */         } 
/*      */       } 
/*  816 */       if (target.isMilkable())
/*      */       {
/*  818 */         if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 
/*  819 */             (int)(target.getStatus().getPositionZ() + target.getAltOffZ()) >> 2, 1))
/*      */         {
/*  821 */           if (!target.isMilked() && subject.isContainerLiquid() && !subject.isSealedByPlayer()) {
/*  822 */             toReturn.add(Actions.actionEntrys[345]);
/*      */           }
/*      */         }
/*      */       }
/*  826 */       if (target.isWoolProducer() && subject.getTemplateId() == 394)
/*      */       {
/*  828 */         if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 
/*  829 */             (int)(target.getStatus().getPositionZ() + target.getAltOffZ()) >> 2, 1))
/*      */         {
/*  831 */           if (!target.isSheared()) {
/*  832 */             toReturn.add(Actions.actionEntrys[646]);
/*      */           }
/*      */         }
/*      */       }
/*  836 */       if (target.isNeedFood())
/*      */       {
/*  838 */         if (!subject.isBodyPartAttached())
/*  839 */           toReturn.add(Actions.actionEntrys[230]); 
/*      */       }
/*  841 */       if (target.isDominatable(performer) && target.isAnimal())
/*      */       {
/*  843 */         toReturn.add(Actions.actionEntrys[46]);
/*      */       }
/*  845 */       if (target.isDomestic())
/*      */       {
/*  847 */         if (stid == 647)
/*      */         {
/*  849 */           if (subject.getOwnerId() == performer.getWurmId())
/*      */           {
/*  851 */             if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 
/*  852 */                 (int)(target.getStatus().getPositionZ() + target.getAltOffZ()) >> 2, 1))
/*      */             {
/*  854 */               toReturn.add(Actions.actionEntrys[398]);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  859 */       if (stid == 792)
/*      */       {
/*  861 */         if (!target.isUnique() && !target.isUndead() && !target.isReborn() && target
/*  862 */           .getHitched() == null && !target.isRidden() && !target.isNpc() && (
/*  863 */           Servers.isThisAPvpServer() || (!target.isDominated() && !target.isBranded())))
/*  864 */           toReturn.add(Actions.actionEntrys[142]); 
/*      */       }
/*  866 */       if (target.isLeadable(performer))
/*      */       {
/*  868 */         if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 
/*  869 */             (int)(target.getStatus().getPositionZ() + target.getAltOffZ()) >> 2, 1)) {
/*      */           
/*  871 */           boolean canItemLead = (!performer.isItemLeading(subject) || subject.isLeadMultipleCreatures());
/*      */           
/*  873 */           if (target.getLeader() == null && ((subject
/*  874 */             .isLeadCreature() && canItemLead) || target.hasBridle())) {
/*      */ 
/*      */             
/*  877 */             boolean lastLed = target.isBranded() ? false : Creatures.getInstance().wasLastLed(performer.getWurmId(), target.getWurmId());
/*  878 */             if ((performer.getVehicle() <= -10L || performer.isVehicleCommander()) && (target
/*  879 */               .mayLead(performer) || lastLed))
/*  880 */               toReturn.add(Actions.actionEntrys[106]); 
/*      */           } 
/*  882 */           if (performer.getCitizenVillage() != null)
/*      */           {
/*  884 */             if (target.getCurrentVillage() != null && target
/*  885 */               .getCurrentVillage() == performer.getCitizenVillage())
/*      */             {
/*  887 */               if (stid == 701) {
/*      */                 
/*  889 */                 Brand brand = Creatures.getInstance().getBrand(target.getWurmId());
/*  890 */                 if (brand == null) {
/*  891 */                   toReturn.add(Actions.actionEntrys[484]);
/*      */                 } else {
/*      */ 
/*      */                   
/*      */                   try {
/*  896 */                     Village villageBrand = Villages.getVillage((int)brand.getBrandId());
/*      */ 
/*      */                     
/*  899 */                     if (performer.getCitizenVillage() == villageBrand)
/*      */                     {
/*  901 */                       toReturn.add(Actions.actionEntrys[643]);
/*      */                     }
/*      */                   }
/*  904 */                   catch (NoSuchVillageException nsv) {
/*      */                     
/*  906 */                     logger.log(Level.INFO, "Deleting brand for " + target.getName() + " since the settlement is gone.");
/*      */                     
/*  908 */                     brand.deleteBrand();
/*  909 */                     toReturn.add(Actions.actionEntrys[484]);
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*  917 */       MissionTrigger[] m = MissionTriggers.getMissionTriggersWith(stid, 47, target.getWurmId());
/*  918 */       if (m.length > 0)
/*  919 */         toReturn.add(Actions.actionEntrys[47]); 
/*  920 */       MissionTrigger[] m2 = MissionTriggers.getMissionTriggersWith(stid, 473, target.getWurmId());
/*  921 */       if (m2.length > 0)
/*  922 */         toReturn.add(Actions.actionEntrys[473]); 
/*  923 */       MissionTrigger[] m3 = MissionTriggers.getMissionTriggersWith(stid, 474, target.getWurmId());
/*  924 */       if (m3.length > 0)
/*  925 */         toReturn.add(Actions.actionEntrys[474]); 
/*      */     } 
/*  927 */     if (subject.isHolyItem()) {
/*      */       
/*  929 */       if (subject.isHolyItem(performer.getDeity()))
/*      */       {
/*  931 */         if (performer.isPriest() || performer.getPower() > 0) {
/*      */           
/*  933 */           float faith = performer.getFaith();
/*  934 */           Spell[] spells = performer.getDeity().getSpellsTargettingCreatures((int)faith);
/*      */           
/*  936 */           if (spells.length > 0) {
/*      */             
/*  938 */             toReturn.add(new ActionEntry((short)-spells.length, "Spells", "spells"));
/*  939 */             for (Spell lSpell : spells) {
/*  940 */               toReturn.add(Actions.actionEntrys[lSpell.number]);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*  945 */     } else if (subject.isMagicStaff() || (subject
/*  946 */       .getTemplateId() == 176 && performer.getPower() >= 4 && Servers.isThisATestServer())) {
/*      */       
/*  948 */       List<ActionEntry> slist = new LinkedList<>();
/*      */       
/*  950 */       if (performer.knowsKarmaSpell(549))
/*  951 */         slist.add(Actions.actionEntrys[549]); 
/*  952 */       if (performer.knowsKarmaSpell(547))
/*  953 */         slist.add(Actions.actionEntrys[547]); 
/*  954 */       if (performer.knowsKarmaSpell(550))
/*  955 */         slist.add(Actions.actionEntrys[550]); 
/*  956 */       if (performer.knowsKarmaSpell(551))
/*  957 */         slist.add(Actions.actionEntrys[551]); 
/*  958 */       if (performer.knowsKarmaSpell(554))
/*  959 */         slist.add(Actions.actionEntrys[554]); 
/*  960 */       if (performer.knowsKarmaSpell(555))
/*  961 */         slist.add(Actions.actionEntrys[555]); 
/*  962 */       if (performer.knowsKarmaSpell(560))
/*  963 */         slist.add(Actions.actionEntrys[560]); 
/*  964 */       if (performer.knowsKarmaSpell(686)) {
/*  965 */         slist.add(Actions.actionEntrys[686]);
/*      */       }
/*  967 */       if (performer.getPower() >= 4)
/*  968 */         toReturn.add(new ActionEntry((short)-slist.size(), "Sorcery", "casting")); 
/*  969 */       toReturn.addAll(slist);
/*      */     } 
/*  971 */     if (stid == 300)
/*      */     {
/*  973 */       if (performer.getPower() > 0)
/*      */       {
/*  975 */         if (subject.getData() < 0L)
/*      */         {
/*  977 */           toReturn.add(Actions.actionEntrys[85]);
/*      */         }
/*      */       }
/*      */     }
/*  981 */     if (target.isTrader() && !target.isPlayer())
/*      */     {
/*  983 */       if (target.getFloorLevel() == performer.getFloorLevel() && (
/*  984 */         !target.isNpcTrader() || performer.getVehicle() == -10L))
/*      */       {
/*  986 */         if (performer.isFriendlyKingdom(target.getKingdomId()) || performer.getPower() > 0)
/*      */         {
/*  988 */           if (target.getCurrentVillage() == null || !target.getCurrentVillage().isEnemy(performer))
/*      */           {
/*  990 */             if (!subject.isNoDiscard() && !subject.isInstaDiscard() && 
/*  991 */               !subject.isTemporary())
/*  992 */               toReturn.add(Actions.actionEntrys[31]); 
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  997 */     if (stid == 1027 && performer.getPower() >= 1 && target instanceof Player) {
/*      */       
/*  999 */       toReturn.add(new ActionEntry((short)-3, "LCM", "checking", emptyIntArr));
/* 1000 */       toReturn.add(Actions.actionEntrys[698]);
/* 1001 */       toReturn.add(Actions.actionEntrys[699]);
/* 1002 */       toReturn.add(Actions.actionEntrys[700]);
/*      */     } 
/* 1004 */     if ((stid == 176 || stid == 315) && performer.getPower() >= 1) {
/*      */ 
/*      */       
/*      */       try {
/* 1008 */         if (target.getBody().getWounds() != null && (target.getBody().getWounds().getWounds()).length > 0)
/* 1009 */           toReturn.add(Actions.actionEntrys[346]); 
/* 1010 */         if (performer.getPower() > 1)
/*      */         {
/* 1012 */           if (target.getPower() <= performer.getPower()) {
/*      */             
/* 1014 */             int sub = -5;
/* 1015 */             if (stid == 176 && performer.getPower() >= 4 && !(target instanceof Player))
/*      */             {
/* 1017 */               sub--; } 
/* 1018 */             if (stid == 176 && performer.getPower() >= 4 && target instanceof Player)
/*      */             {
/* 1020 */               sub--;
/*      */             }
/* 1022 */             toReturn.add(new ActionEntry((short)sub, "Powers", "godlypower", emptyIntArr));
/* 1023 */             toReturn.add(Actions.actionEntrys[33]);
/* 1024 */             toReturn.add(new ActionEntry((short)392, "Test Effect", "testing effect", emptyIntArr));
/* 1025 */             toReturn.add(Actions.actionEntrys[327]);
/* 1026 */             toReturn.add(Actions.actionEntrys[352]);
/* 1027 */             toReturn.add(Actions.actionEntrys[244]);
/* 1028 */             toReturn.add(Actions.actionEntrys[928]);
/*      */             
/* 1030 */             if (stid == 176 && performer.getPower() >= 4 && target instanceof Player)
/*      */             {
/* 1032 */               toReturn.add(Actions.actionEntrys[721]);
/*      */             }
/* 1034 */             if (stid == 176 && performer.getPower() >= 4 && !(target instanceof Player))
/*      */             {
/* 1036 */               toReturn.add(Actions.actionEntrys[538]); } 
/*      */           } 
/* 1038 */           if (performer.getPower() >= 2 && target.isPlayer()) {
/*      */             
/* 1040 */             int nums = 0;
/* 1041 */             if (Servers.localServer.serverNorth != null)
/* 1042 */               nums--; 
/* 1043 */             if (Servers.localServer.serverEast != null)
/* 1044 */               nums--; 
/* 1045 */             if (Servers.localServer.serverSouth != null)
/* 1046 */               nums--; 
/* 1047 */             if (Servers.localServer.serverWest != null)
/* 1048 */               nums--; 
/* 1049 */             if (nums < 0) {
/*      */               
/* 1051 */               toReturn.add(new ActionEntry((short)nums, "Specials", "specials"));
/* 1052 */               if (Servers.localServer.serverNorth != null)
/* 1053 */                 toReturn.add(Actions.actionEntrys[240]); 
/* 1054 */               if (Servers.localServer.serverEast != null)
/* 1055 */                 toReturn.add(Actions.actionEntrys[241]); 
/* 1056 */               if (Servers.localServer.serverSouth != null)
/* 1057 */                 toReturn.add(Actions.actionEntrys[242]); 
/* 1058 */               if (Servers.localServer.serverWest != null) {
/* 1059 */                 toReturn.add(Actions.actionEntrys[243]);
/*      */               }
/*      */             } 
/* 1062 */             if (performer.getPower() >= 4 || (Servers.localServer.testServer && performer
/* 1063 */               .getPower() >= 3)) {
/*      */               
/* 1065 */               toReturn.add(new ActionEntry((short)-1, "Skills", "Skills stuff", emptyIntArr));
/* 1066 */               toReturn.add(Actions.actionEntrys[92]);
/* 1067 */               if (Servers.localServer.testServer)
/* 1068 */                 toReturn.add(new ActionEntry((short)486, "Toggle champ", "testing")); 
/*      */             } 
/*      */           } 
/* 1071 */           if (!target.isPlayer() && performer.getPower() >= 4)
/*      */           {
/* 1073 */             toReturn.add(new ActionEntry((short)-2, "Npcs", "Npc stuff", emptyIntArr));
/* 1074 */             toReturn.add(Actions.actionEntrys[92]);
/* 1075 */             toReturn.add(Actions.actionEntrys[73]);
/*      */           }
/*      */         
/*      */         }
/*      */       
/* 1080 */       } catch (Exception ex) {
/*      */         
/* 1082 */         logger.log(Level.WARNING, target.getName() + ": " + ex.getMessage(), ex);
/*      */       } 
/* 1084 */       toReturn.add(Actions.actionEntrys[180]);
/*      */     } 
/* 1086 */     return toReturn;
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
/*      */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, Creature target, short action, float counter) {
/*      */     Creature pet;
/* 1099 */     boolean done = true;
/* 1100 */     if (target == null) {
/*      */       
/* 1102 */       logger.severe("Target is null for player " + performer);
/* 1103 */       return true;
/*      */     } 
/*      */     
/* 1106 */     switch (action)
/*      */     
/*      */     { 
/*      */       case 1:
/* 1110 */         handle_EXAMINE(performer, target);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2455 */         return done;case 185: done = true; handle_GETINFO(performer, target); return done;case 351: handle_ASK_TUTORIAL(performer, target); return done;case 34: done = MethodsCreatures.findCaveExit(target, performer); return done;case 2: target.sendEquipment(performer); return done;case 326: case 716: if (handle_TARGET_and_TARGET_HOSTILE(performer, target, action)) return true;  return done;case 344: if (target.isPlayer()) if (performer.isSparring(target)) { ((Player)performer).removeSparrer(target); ((Player)target).removeSparrer(performer); } else { DuelQuestion dq = new DuelQuestion(target, "Sparring", "Do you wish to spar with " + performer.getName() + "?", 60, performer.getWurmId()); dq.sendQuestion(); performer.getCommunicator().sendNormalServerMessage("You ask " + target.getName() + " if " + target.getHeSheItString() + " wants to spar with you."); }   return done;case 343: if (target.isPlayer() && target.getKingdomId() == performer.getKingdomId()) if (!performer.isFighting()) if (performer.isDuelling(target)) { ((Player)performer).removeDuellist(target); ((Player)target).removeDuellist(performer); } else { DuelQuestion dq = new DuelQuestion(target, "Duel to the death", "Do you wish to duel with " + performer.getName() + " to the death?", 59, performer.getWurmId()); dq.sendQuestion(); performer.getCommunicator().sendNormalServerMessage("You ask " + target.getName() + " if " + target.getHeSheItString() + " wants to duel to the death with you."); }    return done;case 342: performer.getCommunicator().sendNormalServerMessage("You need to select an item in order to throw it."); return true;case 114: if (target == performer.getTarget() || !performer.hasLink()) if (performer.isGuest()) { performer.getCommunicator().sendNormalServerMessage("Guests may not attack."); } else if (target.isInvulnerable() && performer.getPower() < 5) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " is protected by the gods. You may not attack " + target.getHimHerItString() + "."); performer.setTarget(-10L, true); } else if (performer.mayAttack(target)) { target.setStealth(false); done = performer.getCombatHandler().attack(target, Server.getCombatCounter(), false, counter, act); setOpponent(performer, target, done, act); } else if (!performer.isStunned() && !performer.isUnconscious()) { performer.getCommunicator().sendNormalServerMessage("You are too weak or not allowed to attack " + target.getName() + "."); }   return done;case 197: case 198: case 199: case 200: case 201: case 202: case 203: case 204: case 205: case 206: case 207: case 208: done = handle_SPECMOVE(performer, target, action, counter); return done;case 136: done = MethodsCreatures.stealth(performer, counter); return done;case 103: if (target.isInvulnerable() && performer.getPower() < 5) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " is protected by the gods. You may not attack " + target.getHimHerItString() + "."); } else if (performer.isGuest()) { performer.getCommunicator().sendNormalServerMessage("Guests may not attack."); } else if (performer.mayAttack(target)) { if (performer.isFighting() && target.opponent != performer) done = CombatEngine.taunt(performer, target, counter, act);  } else if (!performer.isStunned() && !performer.isUnconscious()) { performer.getCommunicator().sendNormalServerMessage("You are too inexperienced to start attacking anyone."); }  return done;case 105: if (performer.isGuest()) { performer.getCommunicator().sendNormalServerMessage("Guests may not attack."); } else if (target.isInvulnerable() && performer.getPower() < 5) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " is protected by the gods. You may not attack " + target.getHimHerItString() + "."); } else if (!performer.mayAttack(target)) { if (!performer.isStunned() && !performer.isUnconscious()) performer.getCommunicator().sendNormalServerMessage("You are too inexperienced to start attacking anyone.");  } else if (performer.isFighting()) { if (target.isDead()) return true;  done = CombatEngine.shieldBash(performer, target, counter); }  return done;case 333: case 708: if (performer.getVehicle() != -10L) { if (performer.getVisionArea() != null) performer.getVisionArea().broadCastUpdateSelectBar(performer.getWurmId(), true);  performer.disembark(true); }  return done;case 663: if (target.mayManage(performer)) { ManageObjectList.Type animalType = ManageObjectList.Type.ANIMAL0; if (target.isWagoner()) { animalType = ManageObjectList.Type.WAGONER; } else { Vehicle vehicle = Vehicles.getVehicle(target); if (vehicle == null || vehicle.isUnmountable()) { animalType = ManageObjectList.Type.ANIMAL0; } else if (vehicle.getMaxPassengers() == 0) { animalType = ManageObjectList.Type.ANIMAL1; } else { animalType = ManageObjectList.Type.ANIMAL2; }  }  ManagePermissions mp = new ManagePermissions(performer, animalType, (PermissionsPlayerList.ISettings)target, false, -10L, false, null, ""); mp.sendQuestion(); }  return done;case 691: if (target.isWagoner()) { Wagoner wagoner = Wagoner.getWagoner(target.getWurmId()); if (wagoner == null) { performer.getCommunicator().sendNormalServerMessage("Cannot find the wagoner associated with this creature, most odd."); } else { if (wagoner.getVillageId() == -1) { performer.getCommunicator().sendNormalServerMessage("Wagoner is in progress of being dismissed.."); return done; }  if (target.maySeeHistory(performer)) { PermissionsHistory ph = new PermissionsHistory(performer, target.getWurmId()); ph.sendQuestion(); }  }  return done; }  if (target.maySeeHistory(performer)) { PermissionsHistory permissionsHistory = new PermissionsHistory(performer, target.getWurmId()); permissionsHistory.sendQuestion(); } case 919: if (target.isWagoner()) { Wagoner wagoner = Wagoner.getWagoner(target.getWurmId()); if (wagoner == null) { performer.getCommunicator().sendNormalServerMessage("Cannot find the wagoner associated with this creature, most odd."); } else if (wagoner.getVillageId() == -1) { performer.getCommunicator().sendNormalServerMessage("Wagoner is in progress of being dismissed.."); } else { WagonerHistory wh = new WagonerHistory(performer, target.getWagoner()); wh.sendQuestion(); }  } else { performer.getCommunicator().sendNormalServerMessage("Creature is not a wagoner."); }  return done;case 469: if (performer != target && performer.mayInviteTeam()) try { if (performer.getTeam() == null) { TeamManagementQuestion tme = new TeamManagementQuestion(performer, "Found a team", "Creating a team", false, target.getWurmId(), false, false); tme.sendQuestion(); } else { TeamManagementQuestion tme = new TeamManagementQuestion(performer, "Expanding the team", "Inviting " + target.getName(), false, target.getWurmId(), false, false); tme.sendQuestion(); }  } catch (Exception exception) {}  return done;case 470: if (performer.getTeam() == target.getTeam() && (performer.isTeamLeader() || (performer.mayInviteTeam() && !target.mayInviteTeam()))) { performer.getCommunicator().sendNormalServerMessage("You remove " + target.getName() + " from the team."); target.setTeam(null, true); }  return done;case 471: if (performer.isTeamLeader()) try { TeamManagementQuestion tme = new TeamManagementQuestion(performer, "Managing the team", "Managing " + performer.getTeam().getName(), false, performer.getWurmId(), true, false); tme.sendQuestion(); } catch (Exception exception) {}  return done;case 61: if (performer instanceof Player) ((Player)performer).removeFriend(target.getWurmId());  return done;case 60: if (performer instanceof Player) if (performer.getKingdomId() == target.getKingdomId()) { if (((Player)performer).isFriend(target.getWurmId())) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " already is your friend."); } else { Methods.sendFriendQuestion(performer, target); }  } else { logger.log(Level.WARNING, performer.getName() + " tried to invite ENEMY " + target.getName() + " as friend!"); }   return done;case 63: if (!target.isTrader()) return true;  if (target.isNpcTrader() && performer.getVehicle() != -10L && !performer.isVehicleCommander()) return true;  if (target.isFighting()) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " is too busy fighting!"); } else if (performer.isTrading()) { performer.getCommunicator().sendNormalServerMessage("You are already trading with someone."); } else if (target.isTrading() && !target.shouldStopTrading(true)) { Trade trade = target.getTrade(); if (trade != null) { Creature oppos = trade.creatureOne; if (target.equals(oppos)) oppos = trade.creatureTwo;  String name = "someone"; if (oppos != null) name = oppos.getName();  performer.getCommunicator().sendNormalServerMessage(target.getName() + " is already trading with " + name + "."); }  } else { if (!(target instanceof Player) && performer.isGuest()) { performer.getCommunicator().sendNormalServerMessage("Guests may not trade with creatures to prevent abuse."); return true; }  if (target.getFloorLevel() != performer.getFloorLevel() && performer.getPower() <= 0) { performer.getCommunicator().sendNormalServerMessage("You can't reach " + target.getName() + " there."); return true; }  if (!performer.isFriendlyKingdom(target.getKingdomId()) && performer.getPower() <= 0) { boolean ok = false; if (Servers.localServer.PVPSERVER && Servers.localServer.isChallengeOrEpicServer() && !Servers.localServer.HOMESERVER) { Village v = target.getCurrentVillage(); if (v != null) if ((v.getGuards()).length > 0) { performer.getCommunicator().sendNormalServerMessage("There are guards in the vicinity. You can't start trading with " + target.getName() + " now."); return true; }   if (target.isNpcTrader()) { Shop shop = Economy.getEconomy().getShop(target); if (shop != null && shop.isPersonal()) ok = true;  }  }  if (!ok) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " snorts and refuses to trade with you."); return true; }  }  if (!(target instanceof Player) && performer.getPower() < 2) { target.turnTowardsCreature(performer); try { target.getStatus().savePosition(target.getWurmId(), false, target.getStatus().getZoneId(), true); } catch (IOException iOException) {} }  MethodsCreatures.initiateTrade(performer, target); }  return done;case 537: done = true; if (Servers.localServer.PVPSERVER && Servers.localServer.EPIC && !Servers.localServer.HOMESERVER) if (target.getFloorLevel() != performer.getFloorLevel() || performer.getMountVehicle() != null) { performer.getCommunicator().sendNormalServerMessage("You can't reach " + target.getName() + " there."); done = true; } else if (target.isFriendlyKingdom(performer.getKingdomId())) { performer.getCommunicator().sendNormalServerMessage("You can't rob " + target.getName() + "!"); } else if (!target.isNpcTrader()) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " snorts at you and refuses to yield."); } else { Shop shop = Economy.getEconomy().getShop(target); if (shop != null && shop.isPersonal()) { int time = act.getTimeLeft(); done = false; Skill taunting = null; try { taunting = performer.getSkills().getSkill(10057); } catch (NoSuchSkillException nsc) { taunting = performer.getSkills().learn(10057, 1.0F); }  if (counter == 1.0F) { Village v = target.getCurrentVillage(); if (v != null) if ((v.getGuards()).length > 0) { performer.getCommunicator().sendNormalServerMessage("There are guards in the vicinity. You can't start robbing " + target.getName() + " now."); return true; }   performer.getCommunicator().sendNormalServerMessage("You start to rob " + target.getNameWithGenus() + "."); time = Actions.getSlowActionTime(performer, taunting, null, 0.0D) * 10; Server.getInstance().broadCastAction(performer.getNameWithGenus() + " starts robbing " + target.getNameWithGenus(), performer, 10); performer.sendActionControl("threatening", true, time); act.setTimeLeft(time); performer.getStatus().modifyStamina(-500.0F); }  if (counter * 10.0F > time) { done = true; if (taunting.skillCheck(target.getSoulStrengthVal(), 0.0D, false, 20.0F) <= 0.0D) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " snorts at you and refuses to yield."); Server.getInstance().broadCastAction(performer.getNameWithGenus() + " fails to scare " + target.getNameWithGenus() + ".", performer, 10); } else { performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " looks really scared and fetches " + target.getHisHerItsString() + " hidden stash."); Server.getInstance().broadCastAction(performer.getNameWithGenus() + " scares " + target.getNameWithGenus() + " into fetching " + target.getHisHerItsString() + " hidden stash.", performer, 10); Item[] invitems = target.getInventory().getAllItems(false); for (Item i : invitems) { if (!i.isCoin()) try { i.putItemInfrontof(target); } catch (NoSuchZoneException|NoSuchPlayerException|NoSuchCreatureException|com.wurmonline.server.NoSuchItemException e) { logger.log(Level.INFO, target.getName() + " : " + e.getMessage()); }   }  }  }  }  }   return done;case 90: Server.getInstance().pollShopDemands(); performer.getCommunicator().sendAlertServerMessage("You just lowered the demand of all traders by 0.9!"); return done;case 73: if (target.isPlayer()) if (performer.getKingdomId() != target.getKingdomId()) { logger.log(Level.WARNING, performer.getName() + " tried to invite ENEMY " + target.getName() + " as villager!"); } else if (target.isGuest()) { performer.getCommunicator().sendAlertServerMessage("You just tried to invite a guest. This should not be possible and has been logged."); logger.log(Level.WARNING, performer.getName() + " has managed to invite a guest. This should not be possible, so cheating is involved."); } else if (((Player)performer).getSaveFile().getPaymentExpire() <= 0L && !Servers.localServer.isChallengeServer()) { performer.getCommunicator().sendNormalServerMessage("You may not invite other players to your settlement unless you are premium. The settlement will disband if your character is deleted and the settlement needs a mayor."); } else if (target.isPlayer() && target.mayChangeVillageInMillis() > 0L) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " may not change village until " + Server.getTimeFor(target.mayChangeVillageInMillis()) + " has elapsed."); } else { Methods.sendJoinVillageQuestion(performer, target); }   return done;case 180: if (target instanceof Player) { performer.getCommunicator().sendNormalServerMessage("You can't go around destroying players!"); logger.log(Level.WARNING, performer.getName() + " tried to destroy a creature: " + target); } else if (performer.getPower() >= 2) { MethodsCreatures.destroyCreature(target); }  return done;case 91: handle_ASK_REFRESH(performer, target); return done;case 42: pet = performer.getPet(); if (pet != null) if (!pet.isWithinDistanceTo(performer.getPosX(), performer.getPosY(), performer.getPositionZ(), 200.0F, 0.0F)) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " is too far away."); } else if (!pet.mayReceiveOrder()) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " ignores your order."); } else if (target.getWurmId() == pet.getWurmId()) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " seems to ignore your order."); } else if (target.getAttitude(performer) != 2 && performer.getPower() == 0) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " ignores your order."); } else { Village v = target.getCurrentVillage(); if (v != null) if (v.isEnemy(performer)) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " hesitates and does not enter " + v.getName() + "."); return true; }   if (target.getTileX() < 10 || target.getTileY() < 10 || target.getTileX() > Zones.worldTileSizeX - 10 || target.getTileY() > Zones.worldTileSizeY - 10) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " hesitates and does not go there."); return true; }  if (target.isInvulnerable()) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " ignores your order."); } else { if (target.isUnique()) { if (pet.isUnique()) { performer.getCommunicator().sendAlertServerMessage("The " + pet.getName() + " becomes outraged instead of attacking " + target.getName() + "."); pet.setDominator(-10L); return true; }  if (Server.rand.nextInt((int)performer.getSoulStrength().getKnowledge(0.0D) / 2) == 0) { performer.getCommunicator().sendNormalServerMessage("Your " + pet.getName() + " seems hesitant about attacking " + target.getName() + "."); pet.setLoyalty(pet.getLoyalty() - 20.0F); }  }  pet.setTarget(target.getWurmId(), true); pet.attackTarget(); performer.getCommunicator().sendNormalServerMessage("You issue an order to the " + pet.getName() + "."); }  }   return done;case 43: done = true; MethodsCreatures.petGiveAway(performer, target); return done;case 378: if (performer.getVehicle() != -10L) { Vehicle hitched = target.getHitched(); if (hitched != null && hitched.getWurmid() == performer.getVehicle()) if (performer.isVehicleCommander()) { if (!hitched.positionDragger(target, performer)) { performer.getCommunicator().sendNormalServerMessage("You can't unhitch the " + target.getName() + " here. Please move the vehicle."); return true; }  try { Zone z = Zones.getZone(target.getTilePos(), target.isOnSurface()); target.getStatus().savePosition(target.getWurmId(), true, z.getId(), true); } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }  hitched.removeDragger(target); Creatures.getInstance().setLastLed(target.getWurmId(), performer.getWurmId()); VolaTile t = target.getCurrentTile(); if (t == null) { logger.log(Level.WARNING, target.getName() + " has no tile?"); } else { t.sendAttachCreature(target.getWurmId(), -10L, 0.0F, 0.0F, 0.0F, 0); }  }   }  return done;case 45: done = true; if (performer.getPet() != null) if (!performer.getPet().isAnimal() || performer.getPet().isReborn()) { performer.getCommunicator().sendNormalServerMessage("The " + performer.getPet().getName() + " may not go offline."); } else { performer.getPet().setStayOnline(false); performer.getCommunicator().sendNormalServerMessage("The " + performer.getPet().getName() + " will now leave the world when you do."); }   return done;case 44: done = true; if (performer.getPet() != null) if (!performer.getPet().isAnimal() || performer.getPet().isReborn()) { performer.getCommunicator().sendNormalServerMessage("The " + performer.getPet().getName() + " may not go offline."); } else { performer.getPet().setStayOnline(true); performer.getCommunicator().sendNormalServerMessage("The " + performer.getPet().getName() + " will now stay in the world when you log off."); }   return done;case 41: pet = performer.getPet(); if (pet != null) if (!pet.isWithinDistanceTo(performer, 200.0F)) { performer.getCommunicator().sendNormalServerMessage("The " + pet.getName() + " is too far away."); } else { pet.setTarget(-10L, true); pet.clearOrders(); performer.getCommunicator().sendNormalServerMessage("You order the " + pet.getName() + " to forget all you told " + pet.getHimHerItString() + "."); }   return done;case 214: handle_ASK_GIFT(performer, target); return done;case 81: if (performer.getKingdomId() != target.getKingdomId()) { logger.log(Level.WARNING, performer.getName() + " tried to invite ENEMY " + target.getName() + " as ally!"); } else { Methods.sendAllianceQuestion(performer, target); }  return done;case 398: performer.getCommunicator().sendNormalServerMessage("You need to use a tool to do that."); done = true; return done;case 209: if (target.isGuest()) { performer.getCommunicator().sendAlertServerMessage("You just tried to declare war to a guest. This should not be possible and has been logged."); logger.log(Level.WARNING, performer.getName() + " has managed to declare war to a guest. This should not be possible, so cheating is involved."); } else if (performer.getCitizenVillage() == null) { performer.getCommunicator().sendAlertServerMessage("You are no longer a citizen of a village."); } else if (target.getCitizenVillage() == null) { performer.getCommunicator().sendAlertServerMessage(target.getName() + " is no longer a citizen of a village."); } else if (!performer.getCitizenVillage().mayDeclareWarOn(target.getCitizenVillage())) { performer.getCommunicator().sendAlertServerMessage(target.getName() + " is already at war with your village."); } else { Methods.sendWarDeclarationQuestion(performer, target.getCitizenVillage()); }  return done;case 210: if (target.isGuest()) { performer.getCommunicator().sendAlertServerMessage("You just tried to offer peace to a guest. This should not be possible and has been logged."); logger.log(Level.WARNING, performer.getName() + " has managed to offer peace to a guest. This should not be possible, so cheating is involved."); } else if (performer.getCitizenVillage() == null) { performer.getCommunicator().sendAlertServerMessage("You are no longer a citizen of a village."); } else if (target.getCitizenVillage() == null) { performer.getCommunicator().sendAlertServerMessage(target.getName() + " is no longer a citizen of a village."); } else if (performer.getCitizenVillage().mayDeclareWarOn(target.getCitizenVillage())) { performer.getCommunicator().sendAlertServerMessage(target.getName() + " is no longer at war with your village."); } else { Methods.sendVillagePeaceQuestion(performer, target); }  return done;case 33: handle_WIZKILL(performer, target); return done;case 392: if (performer.getPower() >= 5) if (target.getEffects() == null) { target.addEffect(EffectFactory.getInstance().createGenericEffect(target.getWurmId(), "traitor", target.getPosX(), target.getPosY(), target.getPositionZ(), target.isOnSurface(), -1.0F, target.getStatus().getRotation())); } else { target.removeEffect(EffectFactory.getInstance().getEffectForOwner(target.getWurmId())); EffectFactory.getInstance().deleteEffByOwner(target.getWurmId()); }   return done;case 352: if (performer.getPower() >= 2) if (target.getPower() > performer.getPower()) { performer.getCommunicator().sendSafeServerMessage("You may not log " + target.getName()); target.getCommunicator().sendSafeServerMessage(performer.getName() + " tried to log you."); } else if (target.loggerCreature1 == -10L) { target.loggerCreature1 = performer.getWurmId(); performer.getCommunicator().sendSafeServerMessage("You now log " + target.getName()); performer.getLogger().log(Level.INFO, "Started logging " + target.getName()); } else if (target.loggerCreature1 == performer.getWurmId()) { target.loggerCreature1 = -10L; performer.getCommunicator().sendSafeServerMessage("You no longer log " + target.getName()); performer.getLogger().log(Level.INFO, "Stopped logging " + target.getName()); }   return done;case 327: if (performer.getPower() >= 2) if (target.getPower() <= performer.getPower()) if (!target.isPlayer()) { performer.getCommunicator().sendNormalServerMessage("Your wand has no effect."); } else { String logActionMsg, message = target.getName(); if (target.isFrozen()) { logActionMsg = "thaws"; message = message + " emits a deep sigh of relief and stretches " + target.getHisHerItsString() + " legs."; } else { logActionMsg = "freezes"; message = message + " gnaws " + target.getHisHerItsString() + " teeth as " + target.getHisHerItsString() + " legs refuse to move any longer."; }  logger.log(Level.INFO, performer.getName() + " " + logActionMsg + " " + target.getName()); performer.getCommunicator().sendNormalServerMessage(message); target.toggleFrozen(performer); Server.getInstance().broadCastAction(message, performer, target, 5); }    return done;case 928: if (performer.getPower() >= 2) if (!target.isPlayer()) { CreatureChangeAgeQuestion q = new CreatureChangeAgeQuestion(performer, "Change Age", "", target.getWurmId()); q.sendQuestion(); }   return done;case 107: if (target.getLeader() == performer) { target.setLeader(null); if (target.getVisionArea() != null) target.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());  }  return done;case 379: if (performer.isPlayer()) { done = MethodsCreatures.breed(performer, target, action, act, counter); } else { done = MethodsCreatures.naturalBreed(performer, target, act, counter); }  return done;case 331: done = MethodsCreatures.ride(performer, target, action); return done;case 332: done = MethodsCreatures.ride(performer, target, action); return done;case 140: if (Features.Feature.WAGONER.isEnabled() && target.isWagoner()) { Wagoner wagoner = target.getWagoner(); if (wagoner != null && wagoner.getState() == 0) wagoner.forceStateChange((byte)1);  }  return done;case 111: if (Features.Feature.WAGONER.isEnabled() && target.isWagoner()) { Wagoner wagoner = target.getWagoner(); if (wagoner != null && wagoner.getState() == 0) wagoner.forceStateChange((byte)4);  }  return done;case 30: if (Features.Feature.WAGONER.isEnabled() && target.isWagoner()) { Wagoner wagoner = target.getWagoner(); if (wagoner != null && wagoner.getState() == 2) wagoner.forceStateChange((byte)3);  }  return done;case 89: done = true; handle_SETKINGDOM(performer, target); return done;case 240: case 241: case 242: case 243: if (performer.getPower() >= 2) { performer.getCommunicator().sendNormalServerMessage("You try to transfer " + target.getName() + "."); target.getCommunicator().sendNormalServerMessage(performer.getName() + " tries to transfer you."); done = Methods.transferPlayer(performer, target, act, counter); }  return done;case 213: if (target.isGuide()) if (performer.getKingdomId() != target.getKingdomId()) if (performer.isChampion()) { performer.getCommunicator().sendNormalServerMessage("You are a champion and may not change kingdom."); } else { MethodsCreatures.sendAskKingdomQuestion(target, performer); }    return done;case 353: if (Servers.localServer.isChallengeServer()) { done = true; } else if (target.getTemplate().isRoyalAspiration()) { if (King.getKing(target.getKingdomId()) != null) { done = true; performer.getCommunicator().sendNormalServerMessage("The " + King.getKing(target.getKingdomId()).getRulerTitle() + " is appointed already. The " + target.getName() + " is still and silent."); if (target.getKingdomId() == 1) Methods.resetJennElector();  if (target.getKingdomId() == 3) Methods.resetHotsElector();  } else { done = Methods.aspireKing(performer, target.getKingdomId(), null, target, act, counter); if (done) { if (target.getKingdomId() == 1) Methods.resetJennElector();  if (target.getKingdomId() == 3) Methods.resetHotsElector();  if (target.getKingdomId() == 2) Methods.resetMolrStone();  }  }  }  return done;case 357: if (performer.isRoyalAnnouncer()) { performer.getCommunicator().sendNormalServerMessage("You announce " + target.getAnnounceString()); Server.getInstance().broadCastAction(performer.getName() + " announces, '" + target.getAnnounceString() + "'", performer, 5); }  return done;case 62: if (target.isNpcTrader()) if (mayDismissMerchant(performer, target)) { TraderManagementQuestion tmq = new TraderManagementQuestion(performer, "Dismiss trader", "Do you want to dismiss this merchant?", target); tmq.sendQuestion(); }   return done;case 493: done = true; handle_SET_PROTECTED(performer, target); return done;case 490: done = true; handle_FINAL_BREATH(performer, target); return done;case 3: if (target.isHorse() || target.isUnicorn()) for (Item i : target.getBody().getAllItems()) { if (i.isSaddleBags()) { performer.getCommunicator().sendOpenInventoryWindow(i.getWurmId(), target.getNamePossessive() + " " + i.getName()); performer.addItemWatched(i); i.addWatcher(i.getWurmId(), performer); i.sendContainedItems(i.getWurmId(), performer); }  }   break; }  if (act.isStanceChange()) { done = handleStanceChange(act, performer, target, action, counter); } else if (act.isDefend()) { done = handleDefend(act, performer, action, counter); } else if (act.isSpell()) { done = handleSpell(act, performer, target, action, counter); } else { done = super.action(act, performer, target, action, counter); }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean handle_SPECMOVE(@Nonnull Creature performer, @Nonnull Creature target, short action, float counter) {
/*      */     double fightskill;
/* 2461 */     Communicator comm = performer.getCommunicator();
/*      */ 
/*      */     
/* 2464 */     if (target == performer) {
/*      */       
/* 2466 */       comm.sendCombatNormalMessage("You need to fight a real enemy to perform special moves.");
/* 2467 */       logger.fine(performer.getName() + " tried to attack themself and was told to attack a real enemy for SpecialMove: " + action);
/*      */ 
/*      */       
/* 2470 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2474 */     if (target.isInvulnerable() && performer.getPower() < 5) {
/*      */       
/* 2476 */       comm.sendNormalServerMessage(target
/* 2477 */           .getNameWithGenus() + " is protected by the gods. You may not attack " + target
/* 2478 */           .getHimHerItString() + ".");
/* 2479 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2483 */     if (!performer.isFighting() || target != performer.opponent)
/*      */     {
/* 2485 */       return true;
/*      */     }
/*      */     
/* 2488 */     if (target.isDead())
/*      */     {
/* 2490 */       return true;
/*      */     }
/*      */     
/* 2493 */     if (target.opponent == null)
/*      */     {
/* 2495 */       target.setOpponent(performer);
/*      */     }
/*      */     
/* 2498 */     Item primweapon = performer.getPrimWeapon();
/* 2499 */     if (primweapon == null) {
/*      */       
/* 2501 */       comm.sendCombatNormalMessage("You need to wield a weapon in order to perform a special move.");
/* 2502 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2508 */       fightskill = performer.getSkills().getSkill(primweapon.getPrimarySkill()).getKnowledge(0.0D);
/*      */     }
/* 2510 */     catch (NoSuchSkillException nss) {
/*      */       
/* 2512 */       comm.sendCombatNormalMessage("You are not proficient enough with the " + primweapon
/* 2513 */           .getName() + " to perform such a feat.");
/*      */       
/* 2515 */       return true;
/*      */     } 
/*      */     
/* 2518 */     if (fightskill <= 19.0D)
/*      */     {
/* 2520 */       return true;
/*      */     }
/*      */     
/* 2523 */     CombatHandler tgtCmbtHndl = target.getCombatHandler();
/* 2524 */     CombatHandler srcCmbtHndl = performer.getCombatHandler();
/* 2525 */     byte tgtStance = tgtCmbtHndl.getCurrentStance();
/* 2526 */     byte srcStance = srcCmbtHndl.getCurrentStance();
/* 2527 */     if (CombatHandler.isStanceOpposing(tgtStance, srcStance) || 
/* 2528 */       CombatHandler.isStanceParrying(tgtStance, srcStance)) {
/*      */       
/* 2530 */       comm.sendCombatNormalMessage(target.getNameWithGenus() + " is protecting that area.");
/* 2531 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2535 */     SpecialMove[] specialmoves = SpecialMove.getMovesForWeaponSkillAndStance(performer, primweapon, (int)fightskill);
/*      */     
/* 2537 */     if (specialmoves.length <= 0)
/*      */     {
/* 2539 */       return true;
/*      */     }
/*      */     
/* 2542 */     if (target.isDead())
/*      */     {
/* 2544 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 2548 */     boolean done = false;
/* 2549 */     if (counter == 1.0F) {
/*      */ 
/*      */       
/* 2552 */       if (performer.combatRound < 3) {
/*      */         
/* 2554 */         comm.sendCombatNormalMessage("You have not moved into position yet.");
/* 2555 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 2560 */         SpecialMove specialMove = specialmoves[action - 197];
/* 2561 */         if (specialMove != null)
/*      */         {
/* 2563 */           performer.specialMove = specialMove;
/* 2564 */           performer.sendActionControl(specialMove.getName(), true, specialMove
/* 2565 */               .getSpeed() * 10);
/*      */ 
/*      */           
/* 2568 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2569 */           segments.add(new CreatureLineSegment(performer));
/* 2570 */           segments.add(new MulticolorLineSegment(" prepare to perform a " + specialMove.getName() + " on ", (byte)7));
/* 2571 */           segments.add(new CreatureLineSegment(target));
/* 2572 */           segments.add(new MulticolorLineSegment("!", (byte)7));
/* 2573 */           performer.getCommunicator().sendColoredMessageCombat(segments, (byte)2);
/* 2574 */           ((MulticolorLineSegment)segments.get(1)).setText(" prepares to perform a " + specialMove.getName() + " on ");
/* 2575 */           target.getCommunicator().sendColoredMessageCombat(segments, (byte)4);
/*      */         }
/*      */         else
/*      */         {
/* 2579 */           performer.specialMove = null;
/* 2580 */           comm.sendCombatNormalMessage("No such move available right now.");
/* 2581 */           done = true;
/*      */         }
/*      */       
/* 2584 */       } catch (Exception e) {
/*      */         
/* 2586 */         comm.sendCombatNormalMessage("No such move available right now.");
/* 2587 */         done = true;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2593 */       return done;
/*      */     } 
/*      */ 
/*      */     
/* 2597 */     SpecialMove tempmove = performer.specialMove;
/* 2598 */     if (tempmove == null)
/*      */     {
/* 2600 */       return true;
/*      */     }
/*      */     
/* 2603 */     if (tempmove.getWeaponType() != -1 && srcCmbtHndl
/* 2604 */       .getType(primweapon, true) != tempmove.getWeaponType()) {
/*      */       
/* 2606 */       comm.sendCombatNormalMessage("You can't perform a " + tempmove.getName() + " with the " + performer
/* 2607 */           .getPrimWeapon().getName() + ".");
/* 2608 */       return true;
/*      */     } 
/*      */     
/* 2611 */     if (counter < tempmove.getSpeed())
/*      */     {
/* 2613 */       return false;
/*      */     }
/*      */     
/* 2616 */     if (performer.getStatus().getStamina() < tempmove.getStaminaCost()) {
/*      */       
/* 2618 */       comm.sendCombatNormalMessage("You have no stamina left to perform a " + tempmove.getName() + ".");
/* 2619 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2630 */       double eff = performer.getSkills().getSkill(primweapon.getPrimarySkill()).skillCheck(tempmove
/* 2631 */           .getDifficulty(), 0.0D, primweapon
/* 2632 */           .isWeaponBow(), 5.0F, performer, target);
/*      */ 
/*      */ 
/*      */       
/* 2636 */       if (eff > 0.0D)
/*      */       {
/* 2638 */         comm.sendCombatNormalMessage("You try a " + tempmove
/* 2639 */             .getName() + ".");
/* 2640 */         tempmove.doEffect(performer, performer
/* 2641 */             .getPrimWeapon(), performer.opponent, 
/*      */             
/* 2643 */             Math.max(20.0D, eff));
/*      */       }
/*      */       else
/*      */       {
/* 2647 */         performer.getStatus()
/* 2648 */           .modifyStamina((-tempmove.getStaminaCost() / 3));
/* 2649 */         comm.sendCombatNormalMessage("You try a " + tempmove
/* 2650 */             .getName() + " but miss.");
/* 2651 */         Server.getInstance().broadCastAction(performer
/* 2652 */             .getNameWithGenus() + " tries a " + tempmove.getName() + " but misses.", performer, 2, true);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2663 */     catch (NoSuchSkillException nss) {
/*      */       
/* 2665 */       comm.sendCombatNormalMessage("You fail to perform the attack.");
/*      */       
/* 2667 */       logger.log(Level.WARNING, performer
/* 2668 */           .getName() + " trying spec move with " + performer
/* 2669 */           .getPrimWeapon().getName());
/* 2670 */       return true;
/*      */     } 
/*      */     
/* 2673 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean handleStanceChange(@Nonnull Action act, @Nonnull Creature performer, Creature target, short action, float counter) {
/*      */     String dir;
/* 2679 */     if (target != performer.getTarget())
/*      */     {
/*      */       
/* 2682 */       return true;
/*      */     }
/*      */     
/* 2685 */     Communicator comm = performer.getCommunicator();
/* 2686 */     if (target == performer) {
/*      */       
/* 2688 */       if (counter == 1.0F) {
/*      */         
/* 2690 */         comm.sendCombatNormalMessage("You show off some moves.");
/* 2691 */         if (act.getNumber() > 0) {
/*      */           
/* 2693 */           byte newStance = CombatHandler.getStanceForAction(act.getActionEntry());
/* 2694 */           String animationName = getAnimationNameForStanceChange(newStance);
/*      */           
/* 2696 */           performer.playAnimation(animationName, false);
/*      */         } 
/* 2698 */         return false;
/*      */       } 
/*      */       
/* 2701 */       if (counter > 3.0F) {
/*      */         
/* 2703 */         performer.playAnimation("idle", true);
/* 2704 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 2708 */     if (target.isInvulnerable()) {
/*      */       
/* 2710 */       comm.sendNormalServerMessage(target
/* 2711 */           .getNameWithGenus() + " is protected by the gods. You may not attack " + target
/* 2712 */           .getHimHerItString() + ".");
/*      */       
/* 2714 */       return true;
/*      */     } 
/*      */     
/* 2717 */     if (!performer.mayAttack(target)) {
/*      */       
/* 2719 */       if (performer.isGuest()) {
/*      */         
/* 2721 */         comm.sendNormalServerMessage("Guests may not attack.");
/* 2722 */         return true;
/*      */       } 
/* 2724 */       if (!performer.isStunned() && !performer.isUnconscious()) {
/*      */         
/* 2726 */         comm.sendNormalServerMessage("You are too weak to attack anyone.");
/* 2727 */         return true;
/*      */       } 
/* 2729 */       return true;
/*      */     } 
/*      */     
/* 2732 */     if (performer.opponent == null) {
/*      */       
/* 2734 */       comm.sendCombatNormalMessage("You are not attacking anyone.");
/* 2735 */       return true;
/*      */     } 
/*      */     
/* 2738 */     if (target.isDead())
/*      */     {
/* 2740 */       return true;
/*      */     }
/*      */     
/* 2743 */     CombatHandler perfCombatHandler = performer.getCombatHandler();
/* 2744 */     if (perfCombatHandler.isOpen()) {
/*      */       
/* 2746 */       comm.sendCombatNormalMessage("You are imbalanced and may not change stance right now.");
/* 2747 */       return true;
/*      */     } 
/*      */     
/* 2750 */     if (perfCombatHandler.isProne()) {
/*      */       
/* 2752 */       comm.sendCombatNormalMessage("You are thrown to the ground, trying to get up.");
/* 2753 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2759 */     if (act.getTimeLeft() < 20)
/*      */     {
/* 2761 */       act.setTimeLeft(50);
/*      */     }
/*      */     
/* 2764 */     Communicator targetComm = target.getCommunicator();
/* 2765 */     if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2778 */       if (action == 340) {
/*      */         
/* 2780 */         int actionTime = performer.isPlayer() ? 50 : 20;
/* 2781 */         if (performer.opponent == null || performer.opponent == performer || 
/*      */           
/* 2783 */           Creature.rangeTo(performer, performer.opponent) > 6) {
/*      */           
/* 2785 */           comm.sendCombatNormalMessage("You are too far away from that right now.");
/*      */           
/* 2787 */           return true;
/*      */         } 
/* 2789 */         if (performer.combatRound < 3 && performer.getFightlevel() <= 0) {
/*      */           
/* 2791 */           comm.sendCombatNormalMessage("You need to get into the fight more first.");
/*      */           
/* 2793 */           return true;
/*      */         } 
/* 2795 */         if (performer.getFightlevel() == 5) {
/*      */           
/* 2797 */           comm.sendCombatNormalMessage("You are already focused to the maximum.");
/*      */           
/* 2799 */           return true;
/*      */         } 
/*      */ 
/*      */         
/* 2803 */         comm.sendCombatNormalMessage("You try to focus.");
/*      */         
/* 2805 */         ArrayList<MulticolorLineSegment> arrayList = new ArrayList<>();
/* 2806 */         arrayList.add(new CreatureLineSegment(performer));
/* 2807 */         arrayList.add(new MulticolorLineSegment(" seems to focus.", (byte)0));
/*      */         
/* 2809 */         target.getCommunicator().sendColoredMessageCombat(arrayList);
/*      */ 
/*      */ 
/*      */         
/* 2813 */         performer.sendActionControl("focusing", true, actionTime);
/*      */         
/* 2815 */         act.setTimeLeft(actionTime);
/*      */       } else {
/*      */         String str2;
/*      */         
/* 2819 */         float f1 = 0.0F;
/* 2820 */         if (performer.opponent != null)
/*      */         {
/* 2822 */           f1 = performer.opponent.getCombatHandler().getCombatRating(performer, performer.opponent
/*      */               
/* 2824 */               .getPrimWeapon(), false);
/*      */         }
/*      */ 
/*      */         
/* 2828 */         float f2 = perfCombatHandler.getCombatRating(performer.opponent, performer
/*      */             
/* 2830 */             .getPrimWeapon(), false);
/*      */         
/* 2832 */         ActionEntry actionEntry = act.getActionEntry();
/* 2833 */         float f3 = perfCombatHandler.getCombatKnowledgeSkill();
/*      */         
/* 2835 */         double d = CombatHandler.getMoveChance(performer, performer
/* 2836 */             .getPrimWeapon(), perfCombatHandler
/* 2837 */             .getCurrentStance(), actionEntry, f2, f1, f3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2843 */         if (d < 1.0D || f3 <= 
/* 2844 */           CombatHandler.getAttackSkillCap(actionEntry.getNumber())) {
/*      */           
/* 2846 */           comm.sendCombatNormalMessage("That move is too advanced for you.");
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2851 */           return true;
/*      */         } 
/* 2853 */         String str1 = "";
/*      */         
/* 2855 */         if (actionEntry.isAttackHigh()) {
/*      */           
/* 2857 */           str1 = "upper";
/*      */         }
/* 2859 */         else if (actionEntry.isAttackLow()) {
/*      */           
/* 2861 */           str1 = "lower";
/*      */         } 
/*      */         
/* 2864 */         if (actionEntry.isAttackLeft()) {
/*      */           
/* 2866 */           str2 = str1.equals("") ? "left" : " left";
/*      */         }
/* 2868 */         else if (actionEntry.isAttackRight()) {
/*      */           
/* 2870 */           str2 = str1.equals("") ? "right" : " right";
/*      */         }
/*      */         else {
/*      */           
/* 2874 */           str2 = str1.equals("") ? "center" : " center";
/*      */         } 
/*      */         
/* 2877 */         ArrayList<MulticolorLineSegment> arrayList = new ArrayList<>();
/* 2878 */         arrayList.add(new CreatureLineSegment(performer));
/* 2879 */         arrayList.add(new MulticolorLineSegment(" try to move into position to target the " + str1 + str2 + " parts of ", (byte)0));
/* 2880 */         arrayList.add(new CreatureLineSegment(target));
/* 2881 */         arrayList.add(new MulticolorLineSegment(".", (byte)0));
/*      */         
/* 2883 */         comm.sendColoredMessageCombat(arrayList);
/*      */         
/* 2885 */         ((MulticolorLineSegment)arrayList.get(1)).setText(" tries to move into position to target your " + str1 + str2 + " parts.");
/* 2886 */         arrayList.remove(3);
/* 2887 */         arrayList.remove(2);
/* 2888 */         targetComm.sendColoredMessageCombat(arrayList);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2898 */         short speed = (short)(int)Math.max(2.0F, perfCombatHandler.getSpeed(performer.getPrimWeapon()) / 2.0F);
/* 2899 */         act.setTimeLeft(speed * 10);
/* 2900 */         performer.sendActionControl(str1 + str2, true, act.getTimeLeft());
/* 2901 */         if (act.getNumber() > 0) {
/*      */ 
/*      */           
/* 2904 */           byte newStance = CombatHandler.getStanceForAction(act.getActionEntry());
/* 2905 */           String animationName = getAnimationNameForStanceChange(newStance);
/*      */           
/* 2907 */           performer.playAnimation(animationName, false);
/*      */         } 
/*      */       } 
/* 2910 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 2914 */     if (counter * 10.0F <= act.getTimeLeft())
/*      */     {
/* 2916 */       return false;
/*      */     }
/*      */     
/* 2919 */     if (action == 340) {
/*      */ 
/*      */       
/* 2922 */       if (performer.opponent == null)
/*      */       {
/* 2924 */         return true;
/*      */       }
/*      */       
/* 2927 */       if (CombatHandler.prerequisitesFail(performer, performer.opponent, false, performer
/* 2928 */           .getPrimWeapon()))
/*      */       {
/* 2930 */         return true;
/*      */       }
/* 2932 */       if (performer.getFightlevel() == 5) {
/*      */         
/* 2934 */         comm.sendCombatNormalMessage("You are already focused to the maximum.");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2939 */         double num = performer.getFightingSkill().skillCheck((((performer
/* 2940 */             .getFightlevel() + 1) * 19) * ItemBonus.getFocusBonus(performer)), 0.0D, true, 1.0F);
/* 2941 */         if (num > 0.0D) {
/*      */ 
/*      */           
/* 2944 */           if (Servers.localServer.testServer && performer.spamMode())
/*      */           {
/* 2946 */             comm.sendCombatNormalMessage("Your result for focusing is " + num + " when difficulty is " + ((performer
/*      */ 
/*      */                 
/* 2949 */                 .getFightlevel() + 1) * 10) + " and skill " + performer
/*      */                 
/* 2951 */                 .getFightingSkill().getKnowledge(0.0D));
/*      */           }
/* 2953 */           performer.increaseFightlevel(1);
/* 2954 */           if (performer.getFightlevel() == 3) {
/* 2955 */             performer.achievement(549);
/* 2956 */           } else if (performer.getFightlevel() == 5) {
/* 2957 */             performer.achievement(562);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2966 */           performer.getStatus().modifyStamina(-4000.0F);
/*      */           
/* 2968 */           ArrayList<MulticolorLineSegment> arrayList = new ArrayList<>();
/* 2969 */           arrayList.add(new CreatureLineSegment(performer));
/* 2970 */           arrayList.add(new MulticolorLineSegment(" now seems more focused.", (byte)0));
/*      */           
/* 2972 */           target.getCommunicator().sendColoredMessageCombat(arrayList);
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 2979 */           comm.sendCombatNormalMessage("You fail to reach a higher degree of focus.");
/*      */ 
/*      */           
/* 2982 */           ArrayList<MulticolorLineSegment> arrayList = new ArrayList<>();
/* 2983 */           arrayList.add(new CreatureLineSegment(performer));
/* 2984 */           arrayList.add(new MulticolorLineSegment(" looks disturbed.", (byte)0));
/*      */           
/* 2986 */           target.getCommunicator().sendColoredMessageCombat(arrayList);
/*      */ 
/*      */ 
/*      */           
/* 2990 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         } 
/* 2992 */         comm.sendFocusLevel(performer.getWurmId());
/*      */       } 
/*      */       
/* 2995 */       return true;
/*      */     } 
/*      */     
/* 2998 */     ActionEntry entry = act.getActionEntry();
/*      */     
/* 3000 */     if (perfCombatHandler.getCurrentStance() == CombatHandler.getStanceForAction(entry)) {
/*      */       
/* 3002 */       comm.sendCombatNormalMessage("Changing to existing stance.");
/* 3003 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3008 */     performer.getStatus().modifyStamina(-1000.0F);
/*      */     
/* 3010 */     String move = "";
/*      */     
/* 3012 */     if (entry.isAttackHigh()) {
/*      */       
/* 3014 */       move = "upper";
/*      */     }
/* 3016 */     else if (entry.isAttackLow()) {
/*      */       
/* 3018 */       move = "lower";
/*      */     } 
/*      */     
/* 3021 */     if (entry.isAttackLeft()) {
/*      */       
/* 3023 */       dir = move.equals("") ? "left" : " left";
/*      */     }
/* 3025 */     else if (entry.isAttackRight()) {
/*      */       
/* 3027 */       dir = move.equals("") ? "right" : " right";
/*      */     }
/*      */     else {
/*      */       
/* 3031 */       dir = move.equals("") ? "center" : " center";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3036 */     float oppcr = 0.0F;
/* 3037 */     if (performer.opponent != null)
/*      */     {
/*      */       
/* 3040 */       oppcr = performer.opponent.getCombatHandler().getCombatRating(performer, performer.opponent.getPrimWeapon(), false);
/*      */     }
/* 3042 */     float mycr = perfCombatHandler.getCombatRating(performer.opponent, performer.getPrimWeapon(), false);
/* 3043 */     float knowl = perfCombatHandler.getCombatKnowledgeSkill();
/*      */     
/* 3045 */     double chance = CombatHandler.getMoveChance(performer, performer
/* 3046 */         .getPrimWeapon(), perfCombatHandler
/* 3047 */         .getCurrentStance(), entry, mycr, oppcr, knowl);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3053 */     if (chance <= 0.0D) {
/*      */       
/* 3055 */       comm.sendCombatNormalMessage("That move is too advanced for you.");
/*      */       
/* 3057 */       ArrayList<MulticolorLineSegment> arrayList = new ArrayList<>();
/* 3058 */       arrayList.add(new CreatureLineSegment(performer));
/* 3059 */       arrayList.add(new MulticolorLineSegment(" decides that the move is too advanced.", (byte)0));
/*      */       
/* 3061 */       target.getCommunicator().sendColoredMessageCombat(arrayList);
/*      */ 
/*      */       
/* 3064 */       return true;
/*      */     } 
/*      */     
/* 3067 */     if ((Server.rand.nextFloat() * 100.0F) >= chance) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3072 */       comm.sendCombatNormalMessage("You fail to move into position.");
/*      */       
/* 3074 */       ArrayList<MulticolorLineSegment> arrayList = new ArrayList<>();
/* 3075 */       arrayList.add(new CreatureLineSegment(performer));
/* 3076 */       arrayList.add(new MulticolorLineSegment(" fails to move into position.", (byte)0));
/*      */       
/* 3078 */       target.getCommunicator().sendColoredMessageCombat(arrayList);
/*      */ 
/*      */       
/* 3081 */       return true;
/*      */     } 
/*      */     
/* 3084 */     perfCombatHandler.setCurrentStance(action, CombatHandler.getStanceForAction(entry));
/*      */     
/* 3086 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 3087 */     segments.add(new CreatureLineSegment(performer));
/* 3088 */     segments.add(new MulticolorLineSegment(" move into position for the " + move + dir + " parts of ", (byte)0));
/* 3089 */     segments.add(new CreatureLineSegment(target));
/* 3090 */     segments.add(new MulticolorLineSegment(".", (byte)0));
/*      */     
/* 3092 */     performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */     
/* 3094 */     ((MulticolorLineSegment)segments.get(1)).setText(" targets your " + move + dir + " parts.");
/* 3095 */     segments.remove(3);
/* 3096 */     segments.remove(2);
/* 3097 */     target.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3105 */     perfCombatHandler.setSentAttacks(false);
/* 3106 */     perfCombatHandler.calcAttacks(false);
/*      */     
/* 3108 */     if (!performer.isAutofight())
/*      */     {
/* 3110 */       comm.sendCombatOptions(
/* 3111 */           CombatHandler.getOptions(perfCombatHandler.getMoveStack(), perfCombatHandler.getCurrentStance()), (short)0);
/*      */     }
/*      */ 
/*      */     
/* 3115 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean handleDefend(@Nonnull Action act, @Nonnull Creature performer, short action, float counter) {
/* 3121 */     Communicator comm = performer.getCommunicator();
/*      */     
/* 3123 */     if (performer.getCombatHandler().isOpen()) {
/*      */       
/* 3125 */       comm.sendCombatNormalMessage("You are imbalanced and may not defend right now.");
/* 3126 */       return true;
/*      */     } 
/* 3128 */     if (performer.getCombatHandler().isProne()) {
/*      */       
/* 3130 */       comm.sendCombatNormalMessage("You are thrown to the ground, trying to get up.");
/* 3131 */       return true;
/*      */     } 
/*      */     
/* 3134 */     if (counter == 1.0F) {
/*      */       
/* 3136 */       String str = "center";
/*      */       
/* 3138 */       if (action == 314) {
/*      */         
/* 3140 */         str = "higher";
/*      */       }
/* 3142 */       else if (action == 316) {
/*      */         
/* 3144 */         str = "lower";
/*      */       } 
/* 3146 */       if (action == 315) {
/*      */         
/* 3148 */         str = "left";
/*      */       }
/* 3150 */       else if (action == 317) {
/*      */         
/* 3152 */         str = "right";
/*      */       } 
/* 3154 */       comm.sendCombatNormalMessage("You prepare to shelter the " + str + " parts of your body.");
/*      */       
/* 3156 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 3160 */     if (counter < 3.0F)
/*      */     {
/* 3162 */       return false;
/*      */     }
/*      */     
/* 3165 */     ActionEntry entry = act.getActionEntry();
/* 3166 */     if (performer.getCombatHandler().getCurrentStance() == CombatHandler.getStanceForAction(entry))
/*      */     {
/* 3168 */       return true;
/*      */     }
/*      */     
/* 3171 */     String dir = "center";
/* 3172 */     if (action == 314) {
/*      */       
/* 3174 */       dir = "higher";
/*      */     }
/* 3176 */     else if (action == 316) {
/*      */       
/* 3178 */       dir = "lower";
/*      */     } 
/* 3180 */     if (action == 315) {
/*      */       
/* 3182 */       dir = "left";
/*      */     }
/* 3184 */     else if (action == 317) {
/*      */       
/* 3186 */       dir = "right";
/*      */     } 
/*      */     
/* 3189 */     if (performer.getFightingSkill().skillCheck(10.0D, 0.0D, true, 1.0F) <= 0.0D) {
/*      */       
/* 3191 */       comm.sendCombatNormalMessage("You still feel open at the " + dir + " parts of your body.");
/* 3192 */       return true;
/*      */     } 
/*      */     
/* 3195 */     comm.sendCombatNormalMessage("You shelter the " + dir + " parts of your body.");
/* 3196 */     Server.getInstance().broadCastAction(performer
/* 3197 */         .getNameWithGenus() + " seems to shelter " + performer.getHisHerItsString() + " " + dir + " parts.", performer, 2, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3202 */     performer.getCombatHandler().setCurrentStance(CombatHandler.getStanceForAction(entry));
/*      */     
/* 3204 */     return true;
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
/*      */   private static void handle_FINAL_BREATH(@Nonnull Creature performer, @Nonnull Creature target) {
/* 3216 */     if (!Methods.isActionAllowed(performer, (short)384) || performer
/* 3217 */       .getCultist() == null || !performer.getCultist().mayDealFinalBreath()) {
/*      */       
/* 3219 */       performer.getCommunicator()
/* 3220 */         .sendCombatNormalMessage("You are not at high enough level of insight for this.");
/*      */       
/*      */       return;
/*      */     } 
/* 3224 */     if (target.isInvulnerable() || performer.getAttitude(target) != 2) {
/*      */       
/* 3226 */       performer.getCommunicator().sendCombatNormalMessage("You are not mentally triggered enough to attack " + target
/* 3227 */           .getName() + " with your thought pulse now.");
/*      */       
/*      */       return;
/*      */     } 
/* 3231 */     if (target.getCurrentTile() == null || performer.getCurrentTile() == null) {
/*      */       
/* 3233 */       performer.getCommunicator().sendCombatNormalMessage("You are not in the same place.");
/*      */       
/*      */       return;
/*      */     } 
/* 3237 */     Structure one = target.getCurrentTile().getStructure();
/* 3238 */     Structure two = performer.getCurrentTile().getStructure();
/*      */     
/* 3240 */     if (one != two) {
/*      */       
/* 3242 */       performer.getCommunicator().sendCombatNormalMessage("The structures block the force.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3247 */     performer.getCultist().touchCooldown5();
/* 3248 */     byte woundType = 0;
/* 3249 */     float infectionSeverity = 0.0F;
/* 3250 */     float poisonSeverity = 0.0F;
/* 3251 */     byte location = 1;
/* 3252 */     if (performer.getCultist().getPath() == 2) {
/*      */       
/* 3254 */       woundType = 9;
/* 3255 */       location = 23;
/*      */     } 
/* 3257 */     if (performer.getCultist().getPath() == 3)
/*      */     {
/* 3259 */       woundType = 2;
/*      */     }
/* 3261 */     if (performer.getCultist().getPath() == 1) {
/*      */       
/* 3263 */       woundType = 9;
/* 3264 */       location = 21;
/* 3265 */       poisonSeverity = 50.0F;
/*      */     } 
/* 3267 */     if (performer.getCultist().getPath() == 5)
/*      */     {
/* 3269 */       location = 34;
/*      */     }
/* 3271 */     if (performer.getCultist().getPath() == 4) {
/*      */       
/* 3273 */       woundType = 6;
/* 3274 */       location = 25;
/* 3275 */       infectionSeverity = 50.0F;
/*      */     } 
/*      */ 
/*      */     
/* 3279 */     int damage = target.isUnique() ? (1000 + Server.rand.nextInt(2000)) : (15000 + Server.rand.nextInt(5000));
/* 3280 */     Battle batle = target.getBattle();
/* 3281 */     if (batle == null)
/*      */     {
/* 3283 */       batle = performer.getBattle();
/*      */     }
/*      */     
/* 3286 */     performer.getCommunicator().sendCombatNormalMessage("You sharpen your thoughts into a shining arrow of energy with which you assault " + target
/*      */         
/* 3288 */         .getName() + ".");
/* 3289 */     logger.log(Level.INFO, performer
/* 3290 */         .getName() + " hurting " + target.getName() + " dam=" + damage + " in " + location + ", infection=" + infectionSeverity + ", poison=" + poisonSeverity);
/*      */ 
/*      */ 
/*      */     
/* 3294 */     float armourMod = target.getArmourMod();
/* 3295 */     if (armourMod == 1.0F || target.isVehicle()) {
/*      */       
/*      */       try {
/*      */         
/* 3299 */         armourMod = ArmourTemplate.getArmourModForLocation(target, location, woundType);
/* 3300 */       } catch (NoArmourException noArmourException) {}
/*      */     }
/*      */     
/* 3303 */     target.addAttacker(performer);
/*      */     
/* 3305 */     CombatEngine.addWound(performer, target, woundType, location, damage, armourMod, "thought pulse", batle, infectionSeverity, poisonSeverity, false, false, false, false);
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
/*      */   private static void handle_SET_PROTECTED(@Nonnull Creature performer, @Nonnull Creature target) {
/* 3318 */     if (target.isPlayer()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 3323 */     Communicator comm = performer.getCommunicator();
/* 3324 */     if (target.isHuman() || target
/* 3325 */       .isGhost() || target
/* 3326 */       .isReborn() || target
/* 3327 */       .isUnique() || target
/* 3328 */       .onlyAttacksPlayers()) {
/*      */       
/* 3330 */       comm.sendNormalServerMessage("It makes no sense caring for " + target.getNameWithGenus() + ".");
/*      */       
/*      */       return;
/*      */     } 
/* 3334 */     if (target.getAttitude(performer) == 2) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3341 */     Village v = Villages.getVillage(target.getTileX(), target.getTileY(), performer.isOnSurface());
/* 3342 */     if (v != null)
/*      */     {
/* 3344 */       if (v != performer.getCitizenVillage()) {
/*      */         
/* 3346 */         comm.sendNormalServerMessage("You need to be citizen of " + v
/* 3347 */             .getName() + " in order to care for the " + target
/* 3348 */             .getName() + ".");
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*      */     
/* 3354 */     BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*      */     
/* 3356 */     if (result != null) {
/*      */       
/* 3358 */       Blocker firstBlocker = result.getFirstBlocker();
/* 3359 */       assert firstBlocker != null;
/* 3360 */       comm.sendNormalServerMessage("The " + firstBlocker.getName() + " is in the way.");
/*      */       
/*      */       return;
/*      */     } 
/* 3364 */     if (target.getDominator() != null && target.getDominator() != performer) {
/*      */       
/* 3366 */       comm.sendNormalServerMessage("The " + target
/* 3367 */           .getName() + " is tamed, but not by you, so you cannot care for it.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3372 */     Creatures creInst = Creatures.getInstance();
/* 3373 */     int tc = creInst.getNumberOfCreaturesProtectedBy(performer.getWurmId());
/* 3374 */     int max = performer.getNumberOfPossibleCreatureTakenCareOf();
/* 3375 */     if (!target.isCaredFor()) {
/*      */       
/* 3377 */       if (tc < max)
/*      */       {
/*      */         
/* 3380 */         creInst.setCreatureProtected(target, performer.getWurmId(), true);
/*      */         
/* 3382 */         comm.sendNormalServerMessage("You now care specially for " + target
/* 3383 */             .getName() + ", to ensure longevity. You may care for " + (max - tc - 1) + " more creatures.");
/*      */       
/*      */       }
/*      */       else
/*      */       {
/*      */         
/* 3389 */         comm.sendNormalServerMessage("You may not care for any more creatures right now. You are already caring for " + tc + " creatures.");
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 3395 */     else if (target.getCareTakerId() == performer.getWurmId()) {
/*      */ 
/*      */       
/* 3398 */       creInst.setCreatureProtected(target, -10L, false);
/*      */       
/* 3400 */       comm.sendNormalServerMessage("You let " + target
/* 3401 */           .getName() + " go in order to care for other creatures. You may care for " + (max - tc + 1) + " more creatures.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void handle_CAGE_SET_PROTECTED(@Nonnull Creature performer, @Nonnull Creature target) {
/* 3408 */     if (!target.isPlayer() && !target.isHuman() && !target.isGhost() && !target.isReborn() && (
/* 3409 */       !target.isCaredFor() || target.getCareTakerId() == performer.getWurmId())) {
/* 3410 */       handle_SET_PROTECTED(performer, target);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void handle_SETKINGDOM(@Nonnull Creature performer, @Nonnull Creature target) {
/* 3415 */     Communicator comm = performer.getCommunicator();
/* 3416 */     if (target instanceof Player && performer.getPower() < 2) {
/*      */       
/* 3418 */       if (!target.acceptsInvitations()) {
/*      */         
/* 3420 */         comm.sendNormalServerMessage(target
/* 3421 */             .getName() + " does not accept invitations now. " + target.getHeSheItString() + " needs to type /invitations in a chat window.");
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 3426 */       if (target.isChampion()) {
/*      */         
/* 3428 */         comm.sendNormalServerMessage(target
/* 3429 */             .getName() + " is a champion and may not change kingdom.");
/*      */         
/*      */         return;
/*      */       } 
/* 3433 */       if (target.mayChangeKingdom(performer))
/*      */       {
/*      */         
/* 3436 */         MethodsCreatures.sendAskKingdomQuestion(performer, target);
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3442 */     if (performer.getPower() < 2) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3449 */     if (target.isChampion()) {
/*      */       
/* 3451 */       comm.sendNormalServerMessage(target
/* 3452 */           .getName() + " is a champion and may not change kingdom.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*      */     try {
/* 3458 */       target.setKingdomId(performer.getKingdomId());
/*      */       
/* 3460 */       comm.sendNormalServerMessage(target
/* 3461 */           .getName() + " now is part of " + 
/* 3462 */           Kingdoms.getNameFor(target.getKingdomId()) + ".");
/*      */       
/* 3464 */       if (performer.getLogger() != null)
/*      */       {
/* 3466 */         performer.getLogger().info(performer
/* 3467 */             .getName() + " sets kingdom of " + target.getName() + ", Id: " + target
/* 3468 */             .getWurmId() + " to " + Kingdoms.getNameFor(target.getKingdomId()) + ".");
/*      */       }
/*      */ 
/*      */       
/* 3472 */       target.getCommunicator().sendUpdateKingdomId();
/*      */     }
/* 3474 */     catch (IOException iox) {
/*      */       
/* 3476 */       logger.log(Level.WARNING, performer.getName() + ": " + iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_WIZKILL(@Nonnull Creature performer, @Nonnull Creature target) {
/* 3482 */     if (performer.getPower() <= 1) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 3487 */     if (target.getPower() > performer.getPower()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 3492 */     logger.log(Level.INFO, performer
/* 3493 */         .getName() + " WIZKILL " + target.getName() + ", id: " + target
/* 3494 */         .getWurmId());
/*      */     
/* 3496 */     Communicator perfComm = performer.getCommunicator();
/* 3497 */     Communicator tgtComm = target.getCommunicator();
/*      */     
/* 3499 */     perfComm.sendNormalServerMessage(target
/* 3500 */         .getNameWithGenus() + " looks surprised as you quickly rip the heart out from " + target
/* 3501 */         .getHisHerItsString() + " body.");
/*      */     
/* 3503 */     tgtComm.sendAlertServerMessage("You look surprised as " + performer
/* 3504 */         .getName() + " suddenly rips the heart out of your body.");
/*      */     
/* 3506 */     tgtComm.sendAlertServerMessage("You see it throb one last time.");
/*      */     
/* 3508 */     Server.getInstance().broadCastAction(target
/* 3509 */         .getNameWithGenus() + " looks very surprised as " + performer.getName() + " suddenly rips the heart out of " + target
/* 3510 */         .getHisHerItsString() + " body.", performer, target, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3515 */     if (performer.getLogger() != null)
/*      */     {
/* 3517 */       performer.getLogger().log(Level.INFO, performer
/* 3518 */           .getName() + " wizkills " + target.getName() + ", id: " + target
/* 3519 */           .getWurmId());
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 3524 */       Item heart = ItemFactory.createItem(636, 99.0F, performer.getName());
/* 3525 */       heart.setData1(target.getTemplate().getTemplateId());
/* 3526 */       heart.setName(target.getName().toLowerCase() + " heart");
/* 3527 */       heart.setWeight(heart.getWeightGrams() * Math.max(20, target.getSize()), true);
/* 3528 */       heart.setButchered();
/* 3529 */       performer.getInventory().insertItem(heart, true);
/*      */     
/*      */     }
/* 3532 */     catch (NoSuchTemplateException|com.wurmonline.server.FailedException ex) {
/*      */       
/* 3534 */       logger.log(Level.WARNING, ex.getMessage());
/*      */     } 
/*      */     
/* 3537 */     target.die(true, "Wizkill");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_ASK_GIFT(@Nonnull Creature performer, @Nonnull Creature target) {
/* 3542 */     int targetTemplateId = target.getTemplate().getTemplateId();
/*      */     
/* 3544 */     if (targetTemplateId != 46 && targetTemplateId != 47) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 3549 */     Communicator comm = performer.getCommunicator();
/* 3550 */     if (!performer.isPaying() && performer.getPower() <= 1) {
/*      */       
/* 3552 */       comm.sendNormalServerMessage("Sorry, you have played too little to receive this year's gift.");
/*      */       
/*      */       return;
/*      */     } 
/* 3556 */     String tgtName = target.getName();
/* 3557 */     String perfName = performer.getName();
/* 3558 */     if (performer.hasFlag(62)) {
/*      */       
/* 3560 */       comm.sendNormalServerMessage(tgtName + " says, 'You have already received my gift this year, " + perfName + "'.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3565 */     if (performer.hasFlag(63)) {
/*      */       
/* 3567 */       comm.sendNormalServerMessage(tgtName + " says, 'Sorry, but you must have paid for premium time to receive my gift this year, " + perfName + "'.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3572 */     byte kingdomId = performer.getKingdomId();
/* 3573 */     boolean isEvil = (kingdomId == 3);
/* 3574 */     int fittingSanta = isEvil ? 47 : 46;
/*      */     
/* 3576 */     String fittingBehaviour = isEvil ? "bad" : "good";
/* 3577 */     String unbefitBehaviour = isEvil ? "good" : "bad";
/* 3578 */     String fittingStr = tgtName + " says, 'You have been a " + fittingBehaviour + " person this year, " + perfName + ".";
/*      */ 
/*      */ 
/*      */     
/* 3582 */     if (targetTemplateId != fittingSanta) {
/*      */ 
/*      */       
/* 3585 */       comm.sendNormalServerMessage(fittingStr + " No gift for you!'.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3590 */     if (isEvil || performer.getReputation() >= 0) {
/*      */ 
/*      */       
/* 3593 */       comm.sendNormalServerMessage(fittingStr + " Here is your gift.'.");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3598 */       comm.sendNormalServerMessage(tgtName + " says, 'You have been a " + unbefitBehaviour + " person this year, " + perfName + ". But here is your gift anyways.'.");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3604 */     ItemBehaviour.awardChristmasPresent(performer);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_ASK_REFRESH(@Nonnull Creature performer, Creature target) {
/* 3609 */     Communicator comm = performer.getCommunicator();
/* 3610 */     if (target.isBartender()) {
/*      */       
/* 3612 */       if (performer.getTutorialLevel() == 7 && !performer.skippedTutorial())
/*      */       {
/* 3614 */         performer.missionFinished(true, true);
/*      */       }
/* 3616 */       if (Servers.localServer.testServer) {
/*      */         
/* 3618 */         performer.getStatus().refresh(0.5F, false);
/* 3619 */         comm.sendNormalServerMessage(target.getName() + " feeds you some delicious steak and cool water.");
/* 3620 */         performer.getBody().healFully();
/* 3621 */         target.playAnimation("give", false);
/*      */         
/*      */         try {
/* 3624 */           performer.setFavor(performer.getFaith());
/*      */         }
/* 3626 */         catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */         
/* 3630 */         comm.sendNormalServerMessage(target.getName() + " heals you.");
/*      */ 
/*      */       
/*      */       }
/* 3634 */       else if (performer.getPlayingTime() < 86400000L || performer.getPower() > 0 || (Servers.localServer.entryServer && performer
/* 3635 */         .isPlayerAssistant())) {
/*      */         
/* 3637 */         float nut = (50 + Server.rand.nextInt(49)) / 100.0F;
/* 3638 */         performer.getStatus().refresh(nut, false);
/* 3639 */         comm.sendNormalServerMessage(target
/* 3640 */             .getName() + " feeds you some delicious steak and cool water.");
/* 3641 */         target.playAnimation("give", false);
/*      */       }
/*      */       else {
/*      */         
/* 3645 */         comm.sendNormalServerMessage(target
/* 3646 */             .getName() + " shakes " + target.getHisHerItsString() + " head at you and tells you that you are too experienced to ask for " + target
/*      */             
/* 3648 */             .getHisHerItsString() + " help.");
/* 3649 */         target.playAnimation("deny", false);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 3654 */     boolean isAboveHero = (performer.getPower() > 1);
/* 3655 */     boolean canMeditate = Methods.isActionAllowed(performer, (short)384);
/* 3656 */     Cultist cultist = performer.getCultist();
/* 3657 */     boolean mayCultistRefresh = (cultist != null && cultist.mayRefresh());
/* 3658 */     if (target.isPlayer() && (isAboveHero || (canMeditate && mayCultistRefresh)))
/*      */     {
/* 3660 */       if (isAboveHero || (performer
/* 3661 */         .isPaying() && performer.isFriendlyKingdom(target.getKingdomId()))) {
/*      */         
/* 3663 */         float nut = (50 + Server.rand.nextInt(49)) / 100.0F;
/* 3664 */         target.getStatus().refresh(nut, true);
/* 3665 */         target.getCommunicator().sendNormalServerMessage(performer
/* 3666 */             .getName() + " emits a positive wave of energy in your direction!");
/* 3667 */         comm.sendNormalServerMessage("You send " + target.getName() + " a warm thought.");
/* 3668 */         if (cultist != null)
/*      */         {
/* 3670 */           cultist.touchCooldown1();
/*      */         }
/* 3672 */         if (performer.getLogger() != null)
/*      */         {
/* 3674 */           performer.getLogger().info(performer
/* 3675 */               .getName() + " refreshed " + target.getName() + ", Id: " + target
/* 3676 */               .getWurmId());
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean handle_TARGET_and_TARGET_HOSTILE(@Nonnull Creature performer, Creature target, short action) {
/* 3684 */     Communicator comm = performer.getCommunicator();
/*      */ 
/*      */     
/* 3687 */     if (action == 716 && target.getAttitude(performer) != 2) {
/*      */       
/* 3689 */       comm.sendNormalServerMessage(target.getNameWithGenus() + " is not hostile towards you.");
/*      */       
/* 3691 */       return true;
/*      */     } 
/*      */     
/* 3694 */     if (target.isInvulnerable())
/*      */     {
/* 3696 */       return false;
/*      */     }
/*      */     
/* 3699 */     if (!Servers.isThisAPvpServer()) {
/*      */       
/* 3701 */       Village bVill = target.getBrandVillage();
/*      */       
/* 3703 */       if (bVill != null && !bVill.mayAttack(performer, target)) {
/*      */         
/* 3705 */         comm.sendNormalServerMessage("You cannot attack this branded animal.");
/* 3706 */         performer.setTarget(-10L, true);
/* 3707 */         return true;
/*      */       } 
/*      */       
/* 3710 */       if (target.isRiddenBy(performer.getWurmId())) {
/*      */         
/* 3712 */         comm.sendNormalServerMessage("You cannot attack your own mount.");
/* 3713 */         performer.setTarget(-10L, true);
/* 3714 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 3718 */     Village village = target.getCurrentVillage();
/* 3719 */     if (village != null) {
/*      */       
/* 3721 */       boolean pvpServer = (target.isOnPvPServer() && performer.isOnPvPServer());
/*      */ 
/*      */       
/* 3724 */       if ((performer.isFriendlyKingdom(target.getKingdomId()) || !pvpServer) && 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3730 */         !village.mayAttack(performer, target))
/*      */       {
/*      */ 
/*      */         
/* 3734 */         if (performer.isLegal() || !pvpServer)
/*      */         {
/* 3736 */           comm.sendNormalServerMessage("That would be illegal here. You can check the settlement token for the local laws.");
/* 3737 */           performer.setTarget(-10L, true);
/* 3738 */           return true;
/*      */         }
/*      */       
/*      */       }
/* 3742 */     } else if (target.isPlayer()) {
/*      */       
/* 3744 */       if (!target.isOnPvPServer() || !performer.isOnPvPServer())
/*      */       {
/* 3746 */         if (!target.isDuelOrSpar(performer) && !performer.isDuelOrSpar(target)) {
/*      */           
/* 3748 */           comm.sendNormalServerMessage("That would be very bad for your karma and is disallowed on this server.");
/* 3749 */           performer.setTarget(-10L, true);
/* 3750 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 3755 */     if (!target.isPlayer() && target.getCitizenVillage() != null && target
/* 3756 */       .getCitizenVillage() == performer.getCitizenVillage()) {
/*      */       
/* 3758 */       comm.sendSafeServerMessage("You will not target " + target.getNameWithGenus() + ".");
/* 3759 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3764 */     performer.setTarget(target.getWurmId(), true);
/* 3765 */     comm.sendSafeServerMessage("You target " + target.getNameWithGenus() + ".");
/*      */     
/* 3767 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_ASK_TUTORIAL(@Nonnull Creature performer, Creature target) {
/* 3772 */     target.turnTowardsCreature(performer);
/* 3773 */     if (!performer.skippedTutorial()) {
/*      */       
/* 3775 */       OldMission m = OldMission.getMission(performer.getTutorialLevel(), performer.getKingdomId());
/* 3776 */       if (m != null)
/*      */       {
/*      */ 
/*      */         
/* 3780 */         MissionQuestion ms = new MissionQuestion(m.number, performer, m.title, m.missionDescription, target.getWurmId());
/* 3781 */         ms.sendQuestion();
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 3786 */         SimplePopup popup = new SimplePopup(performer, "Already finished", "The " + target.getName() + " has no more instructions for you.");
/*      */         
/* 3788 */         popup.sendQuestion();
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 3793 */       OldMission m = OldMission.getMission(9999, performer.getKingdomId());
/* 3794 */       if (m != null) {
/*      */ 
/*      */ 
/*      */         
/* 3798 */         MissionQuestion ms = new MissionQuestion(m.number, performer, m.title, m.missionDescription, target.getWurmId());
/* 3799 */         ms.sendQuestion();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_GETINFO(@Nonnull Creature performer, Creature target) {
/* 3806 */     if (performer.getPower() < 2 && (
/* 3807 */       !Methods.isActionAllowed(performer, (short)384) || performer
/* 3808 */       .getCultist() == null || !performer.getCultist().mayCreatureInfo())) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 3813 */     Communicator comm = performer.getCommunicator();
/* 3814 */     if (performer.getCultist() != null && performer.getCultist().mayCreatureInfo()) {
/*      */ 
/*      */       
/* 3817 */       performer.getCultist().touchCooldown1();
/* 3818 */       comm.sendNormalServerMessage("You decide to classify " + target
/* 3819 */           .getNameWithGenus() + " using numbers.");
/* 3820 */       comm.sendNormalServerMessage("Stamina level " + target
/* 3821 */           .getStatus().getStamina() + ", damage level " + 
/* 3822 */           (target.getStatus()).damage + ".");
/*      */       
/* 3824 */       String domname = "none";
/*      */       
/*      */       try {
/* 3827 */         domname = Players.getInstance().getNameFor(target.dominator);
/*      */       }
/* 3829 */       catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */       
/* 3833 */       comm.sendNormalServerMessage("Hunger value " + target
/* 3834 */           .getStatus().getHunger() + ", fat level " + 
/* 3835 */           (target.getStatus()).fat + ", nutrition level " + (target
/* 3836 */           .getStatus().getNutritionlevel() * 100.0F) + ", thirst level " + target
/*      */           
/* 3838 */           .getStatus().getThirst() + ", dominated by " + domname + ", loyalty level " + 
/* 3839 */           (target.getStatus()).loyalty + ".");
/* 3840 */       comm.sendNormalServerMessage("Normal stamina regen " + target
/* 3841 */           .getStatus().hasNormalRegen() + ".");
/* 3842 */       String str1 = "none";
/* 3843 */       if (target.getLeader() != null)
/* 3844 */         str1 = target.getLeader().getName(); 
/* 3845 */       String str2 = "none";
/* 3846 */       if (target.getHitched() != null)
/* 3847 */         str2 = target.getHitched().getName(); 
/* 3848 */       comm.sendNormalServerMessage("Kingdom is " + 
/* 3849 */           Kingdoms.getNameFor(target.getKingdomId()) + ", leader is " + str1 + ", hitched to " + str2);
/*      */ 
/*      */       
/* 3852 */       if (target.isPlayer()) {
/* 3853 */         comm.sendNormalServerMessage("Rank=" + ((Player)target)
/* 3854 */             .getRank() + ", Max Rank=" + ((Player)target)
/* 3855 */             .getMaxRank());
/*      */       }
/*      */       try {
/* 3858 */         if (target.getCurrentAction() != null)
/*      */         {
/* 3860 */           comm.sendNormalServerMessage("Busy " + target
/* 3861 */               .getCurrentAction().getActionString());
/*      */         }
/*      */       }
/* 3864 */       catch (NoSuchActionException ex) {
/*      */         
/* 3866 */         comm.sendNormalServerMessage("Not busy doing anything.");
/*      */       } 
/* 3868 */       String str3 = "none";
/* 3869 */       if (target.opponent != null)
/* 3870 */         str3 = target.opponent.getNameWithGenus(); 
/* 3871 */       String str4 = "none";
/* 3872 */       if (target.target > -10L) {
/*      */         
/*      */         try {
/*      */           
/* 3876 */           Creature c = Server.getInstance().getCreature(target.target);
/* 3877 */           str4 = c.getNameWithGenus();
/*      */         }
/* 3879 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 3881 */           str4 = " unknown creature. That is bad so setting to none.";
/* 3882 */           target.setTarget(-10L, true);
/*      */         }
/* 3884 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 3886 */           str4 = " unknown player. That is bad so setting to none.";
/* 3887 */           target.setTarget(-10L, true);
/*      */         } 
/*      */       }
/* 3890 */       comm.sendNormalServerMessage(target
/* 3891 */           .getNameWithGenus() + " targets " + str4 + ", and fights " + str3);
/*      */       
/* 3893 */       target.getCommunicator().sendAlertServerMessage(performer
/* 3894 */           .getNameWithGenus() + " evaluates you.");
/* 3895 */       if (performer.getCultist().getLevel() > 8) {
/*      */         
/* 3897 */         Skills skills = target.getSkills();
/* 3898 */         for (Skill skill : skills.getSkills()) {
/*      */           
/* 3900 */           if (skill.affinity > 0)
/* 3901 */             comm
/* 3902 */               .sendNormalServerMessage("Affinity in skill " + skill.getName()); 
/*      */         } 
/* 3904 */         comm.sendNormalServerMessage(target
/* 3905 */             .getHeSheItString() + " is carrying " + (target
/* 3906 */             .getInventory().getFullWeight() / 1000) + " kgs in inventory and " + (target
/* 3907 */             .getBody().getBodyItem().getFullWeight() / 1000) + " kgs equipped.");
/*      */         
/* 3909 */         if (target.isPlayer())
/* 3910 */           comm
/* 3911 */             .sendNormalServerMessage("Battle rank: " + ((Player)target).getRank()); 
/*      */       } 
/* 3913 */       if (target.isNpcTrader()) {
/*      */         
/* 3915 */         Shop shop = Economy.getEconomy().getShop(target);
/* 3916 */         if (shop != null) {
/*      */           
/* 3918 */           comm.sendNormalServerMessage("Economic breakdown this period: Earned=" + shop
/* 3919 */               .getMoneyEarnedMonth() + ", spent=" + shop
/*      */               
/* 3921 */               .getMoneySpentMonth() + " Ratio=" + shop.getSellRatio());
/* 3922 */           comm.sendNormalServerMessage("Taxes paid=" + shop
/* 3923 */               .getTaxPaid() + ", rate=" + shop.getTax());
/*      */         } 
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 3930 */     int zid = -1;
/*      */     
/*      */     try {
/* 3933 */       Zone z = Zones.getZone(target.getTileX(), target.getTileY(), true);
/* 3934 */       zid = z.getId();
/*      */     }
/* 3936 */     catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3941 */     comm.sendNormalServerMessage(target
/* 3942 */         .getName() + " posx=" + target.getPos3f() + "(+" + target.getAltOffZ() + "), rot=" + target
/* 3943 */         .getStatus().getRotation() + " floor level " + target
/* 3944 */         .getFloorLevel());
/* 3945 */     comm.sendNormalServerMessage(target.getTilePos() + " surf=" + target
/* 3946 */         .isOnSurface() + " fg=" + target
/* 3947 */         .followsGround() + " og=" + 
/* 3948 */         (target.getMovementScheme()).onGround + ".");
/* 3949 */     comm.sendNormalServerMessage("Stamina=" + target
/* 3950 */         .getStatus().getStamina() + ", damage=" + 
/* 3951 */         (target.getStatus()).damage + ".");
/*      */     
/* 3953 */     comm.sendNormalServerMessage("Normal stamina regen " + target
/* 3954 */         .getStatus().hasNormalRegen() + ". Zonebonus: " + target.zoneBonus);
/*      */     
/* 3956 */     comm.sendNormalServerMessage("Hunger=" + target
/* 3957 */         .getStatus().getHunger() + ", fat=" + 
/* 3958 */         (target.getStatus()).fat + ", nutrition=" + target
/* 3959 */         .getStatus().getNutritionlevel() + ", thirst=" + target
/* 3960 */         .getStatus().getThirst() + ", dominator=" + target.dominator + " loyalty=" + 
/*      */         
/* 3962 */         (target.getStatus()).loyalty + ".");
/*      */     
/* 3964 */     String leader = "none";
/* 3965 */     if (target.getLeader() != null)
/*      */     {
/* 3967 */       leader = target.getLeader().getName();
/*      */     }
/* 3969 */     String hitched = "none";
/* 3970 */     if (target.getHitched() != null)
/*      */     {
/* 3972 */       hitched = target.getHitched().getName();
/*      */     }
/* 3974 */     comm.sendNormalServerMessage("Model=" + target
/* 3975 */         .getModelName() + ", Kingdom=" + 
/* 3976 */         Kingdoms.getNameFor(target.getKingdomId()) + " leader=" + leader + ", hitched=" + hitched);
/*      */ 
/*      */     
/* 3979 */     VisionArea visionArea = target.getVisionArea();
/* 3980 */     VirtualZone surface = (visionArea == null) ? null : visionArea.getSurface();
/* 3981 */     if (surface != null)
/*      */     {
/* 3983 */       comm.sendNormalServerMessage("Watching (" + surface
/* 3984 */           .getStartX() + "," + surface.getStartY() + ")-(" + surface
/* 3985 */           .getEndX() + "," + surface.getEndY() + ")");
/*      */     }
/*      */     
/* 3988 */     if (target.getStatus().getPath() != null) {
/*      */       
/* 3990 */       Path p = target.getStatus().getPath();
/* 3991 */       for (PathTile pt : p.getPathTiles())
/*      */       {
/* 3993 */         comm.sendNormalServerMessage("Pathing to " + pt.getTileX() + ", " + pt.getTileY());
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 3999 */       if (target.getCurrentAction() != null)
/*      */       {
/* 4001 */         comm.sendNormalServerMessage("Busy " + target
/* 4002 */             .getCurrentAction().getActionString());
/*      */       }
/*      */     }
/* 4005 */     catch (NoSuchActionException ex) {
/*      */       
/* 4007 */       comm.sendNormalServerMessage("Not busy doing anything.");
/*      */     } 
/*      */     
/* 4010 */     String opostring = "none";
/* 4011 */     if (target.opponent != null)
/*      */     {
/* 4013 */       opostring = target.opponent.getName();
/*      */     }
/*      */     
/* 4016 */     String targstring = "none";
/* 4017 */     if (target.target > -10L) {
/*      */       
/*      */       try {
/*      */         
/* 4021 */         Creature c = Server.getInstance().getCreature(target.target);
/* 4022 */         targstring = c.getName();
/*      */       }
/* 4024 */       catch (NoSuchCreatureException nsc) {
/*      */         
/* 4026 */         targstring = " unknown creature. That is bad so setting to none.";
/* 4027 */         target.setTarget(-10L, true);
/*      */       }
/* 4029 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 4031 */         targstring = " unknown player. That is bad so setting to none.";
/* 4032 */         target.setTarget(-10L, true);
/*      */       } 
/*      */     }
/* 4035 */     comm.sendNormalServerMessage("Target=" + targstring + ", opponent=" + opostring);
/*      */     
/* 4037 */     if (target.isNpcTrader()) {
/*      */       
/* 4039 */       Shop shop = Economy.getEconomy().getShop(target);
/* 4040 */       if (shop != null) {
/*      */         
/* 4042 */         comm.sendNormalServerMessage("Month spent=" + shop
/* 4043 */             .getMoneySpentMonth() + ", earned=" + shop
/* 4044 */             .getMoneyEarnedMonth() + " Life spent=" + shop
/* 4045 */             .getMoneySpentLife() + " earned=" + shop
/* 4046 */             .getMoneyEarnedLife() + " tax=" + shop.getTax() + " earned=" + shop
/* 4047 */             .getTaxPaid());
/*      */         
/* 4049 */         if (shop.isPersonal()) {
/*      */           
/* 4051 */           Item[] items = target.getInventory().getAllItems(false);
/* 4052 */           for (int x = 0; x < items.length; x++) {
/*      */             
/* 4054 */             String lname = String.valueOf((items[x]).lastOwner);
/*      */             
/* 4056 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId((items[x]).lastOwner);
/* 4057 */             if (pinf != null)
/* 4058 */               lname = pinf.getName(); 
/* 4059 */             comm.sendNormalServerMessage(items[x]
/* 4060 */                 .getName() + " [" + items[x]
/* 4061 */                 .getDescription() + "] " + items[x]
/* 4062 */                 .getQualityLevel() + ": " + 
/*      */                 
/* 4064 */                 Economy.getEconomy().getChangeFor(items[x].getPrice()).getChangeShortString() + " last:" + lname);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 4071 */     comm.sendNormalServerMessage("Target=" + targstring + ", opponent=" + opostring);
/*      */     
/* 4073 */     if (performer.getPower() >= 5)
/*      */     {
/* 4075 */       comm.sendNormalServerMessage("Wurmid=" + target.getWurmId() + ", zoneid=" + zid);
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
/*      */   private static void handle_EXAMINE(@Nonnull Creature performer, Creature target) {
/* 4087 */     if (performer.getPower() >= 3) {
/*      */       
/* 4089 */       Skills skills = target.getSkills();
/* 4090 */       for (Skill skill : skills.getSkills())
/*      */       {
/* 4092 */         performer.getCommunicator().sendNormalServerMessage(skill
/* 4093 */             .getName() + ": " + skill.getKnowledge() + " *:" + skill.affinity);
/*      */       }
/*      */     } 
/* 4096 */     if (target.isPlayer()) {
/*      */       
/* 4098 */       String kingdom = "a Jenn-Kellon.";
/* 4099 */       if (target.getKingdomId() == 0) {
/* 4100 */         kingdom = "not allied to anyone.";
/* 4101 */       } else if (target.getKingdomId() == 2) {
/* 4102 */         kingdom = "a Mol Rehan.";
/* 4103 */       } else if (target.getKingdomId() == 3) {
/* 4104 */         kingdom = "with the Horde of the Summoned.";
/* 4105 */       } else if (target.getKingdomId() == 4) {
/* 4106 */         kingdom = "from the Freedom Isles.";
/* 4107 */       } else if (target.getKingdomId() != 0) {
/* 4108 */         kingdom = "a " + Kingdoms.getNameFor(target.getKingdomId());
/*      */       } 
/* 4110 */       if (performer.isVisible())
/* 4111 */         target.getCommunicator()
/* 4112 */           .sendNormalServerMessage(performer
/* 4113 */             .getNameWithGenus() + " takes a long good look at you."); 
/* 4114 */       boolean send = true;
/* 4115 */       if (target.isKing()) {
/*      */         
/* 4117 */         King k = King.getKing(target.getKingdomId());
/* 4118 */         if (k != null) {
/*      */           
/* 4120 */           performer.getCommunicator()
/* 4121 */             .sendNormalServerMessage("You are looking at " + k.getFullTitle() + ".");
/* 4122 */           send = false;
/*      */         } else {
/*      */           
/* 4125 */           logger.log(Level.WARNING, target
/* 4126 */               .getName() + " is king but there is none for " + target.getKingdomId() + "?");
/*      */         } 
/*      */       } 
/*      */       
/* 4130 */       if (target.getPower() <= 0) {
/*      */         
/* 4132 */         if (send) {
/* 4133 */           performer.getCommunicator().sendNormalServerMessage(target
/* 4134 */               .getNameWithGenus() + " is " + kingdom);
/*      */         }
/* 4136 */       } else if (target.getPower() == 1) {
/* 4137 */         performer.getCommunicator().sendNormalServerMessage("This person is a hero among the living.");
/* 4138 */       } else if (target.getPower() == 2) {
/* 4139 */         performer.getCommunicator().sendNormalServerMessage(target
/* 4140 */             .getName() + " strikes you with " + target.getHisHerItsString() + " splendor! A demigod!");
/*      */       }
/* 4142 */       else if (target.getPower() == 3) {
/* 4143 */         performer.getCommunicator().sendNormalServerMessage("Your eyes hurt as you look at " + target
/* 4144 */             .getName() + "! The presence of a high god awes you.");
/*      */       }
/* 4146 */       else if (target.getPower() == 4) {
/* 4147 */         performer.getCommunicator().sendNormalServerMessage("Looking at " + target
/* 4148 */             .getName() + " is almost unbearable, and you hear beautiful songs in your mind! An arch angel!");
/*      */       }
/* 4150 */       else if (target.getPower() == 5) {
/* 4151 */         performer.getCommunicator().sendNormalServerMessage("You have met your maker. " + target
/* 4152 */             .getName() + " is an implementor, with powers beyond reason.");
/*      */       } 
/* 4154 */       String blood = "no discernable bloodline.";
/* 4155 */       switch (target.getBlood()) {
/*      */         
/*      */         case 2:
/* 4158 */           blood = "blood from the Freedom Isles.";
/*      */           break;
/*      */         case 1:
/* 4161 */           blood = "blood from the Horde.";
/*      */           break;
/*      */         case 4:
/* 4164 */           blood = "blood from Jenn-Kellon.";
/*      */           break;
/*      */         case 8:
/* 4167 */           blood = "blood from Mol Rehan.";
/*      */           break;
/*      */         default:
/* 4170 */           blood = "no discernable bloodline.";
/*      */           break;
/*      */       } 
/* 4173 */       performer.getCommunicator().sendNormalServerMessage(
/* 4174 */           StringUtilities.raiseFirstLetter(target.getHeSheItString()) + " seems to have " + blood);
/* 4175 */       String appointments = target.getAppointmentTitles();
/* 4176 */       if (appointments.length() > 0)
/* 4177 */         performer.getCommunicator().sendNormalServerMessage(appointments); 
/* 4178 */       if (target.getAlignment() < -20.0F && performer.getAlignment() > 20.0F) {
/* 4179 */         performer.getCommunicator().sendNormalServerMessage(
/*      */             
/* 4181 */             StringUtilities.raiseFirstLetter(target.getHeSheItString() + " radiates an unsettling aura."));
/* 4182 */       } else if (performer.getAlignment() < -20.0F && target.getAlignment() > 20.0F) {
/* 4183 */         performer.getCommunicator().sendNormalServerMessage(
/*      */             
/* 4185 */             StringUtilities.raiseFirstLetter(target.getHeSheItString() + " radiates an unsettling aura."));
/* 4186 */       }  if (performer.getPower() > 0)
/*      */       {
/* 4188 */         performer.getCommunicator().sendNormalServerMessage("Reputation: " + target
/* 4189 */             .getReputation() + " Alignment: " + target.getAlignment());
/*      */       }
/* 4191 */       performer.getCommunicator().sendNormalServerMessage(
/* 4192 */           StringUtilities.raiseFirstLetter(target.getStatus().getBodyType()));
/*      */     }
/*      */     else {
/*      */       
/* 4196 */       String exa = target.examine();
/*      */       
/* 4198 */       if (target.isNpcTrader()) {
/*      */         
/* 4200 */         Shop shop = Economy.getEconomy().getShop(target);
/* 4201 */         long owner = shop.getOwnerId();
/* 4202 */         if (owner > 0L) {
/*      */ 
/*      */           
/*      */           try {
/* 4206 */             String name = Players.getInstance().getNameFor(owner);
/* 4207 */             exa = target.getName() + " is here selling items on behalf of " + name + ".";
/*      */           }
/* 4209 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 4211 */             exa = target.getName() + " is here selling items.";
/*      */           }
/* 4213 */           catch (IOException oix) {
/*      */             
/* 4215 */             logger.log(Level.WARNING, oix.getMessage(), oix);
/* 4216 */             exa = target.getName() + " is here selling items.";
/*      */           } 
/*      */         } else {
/*      */           
/* 4220 */           exa = target.getName() + " is here selling items.";
/*      */         } 
/*      */       } 
/* 4223 */       performer.getCommunicator().sendNormalServerMessage(exa);
/* 4224 */       Brand brand = Creatures.getInstance().getBrand(target.getWurmId());
/* 4225 */       if (brand != null) {
/*      */         
/*      */         try {
/*      */           
/* 4229 */           Village v = Villages.getVillage((int)brand.getBrandId());
/* 4230 */           performer.getCommunicator().sendNormalServerMessage("It has been branded by and belongs to the settlement of " + v
/* 4231 */               .getName() + ".");
/*      */         }
/* 4233 */         catch (NoSuchVillageException nsv) {
/*      */           
/* 4235 */           brand.deleteBrand();
/*      */         } 
/*      */       }
/* 4238 */       if (target.isCaredFor()) {
/*      */         
/* 4240 */         long careTaker = target.getCareTakerId();
/* 4241 */         PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(careTaker);
/* 4242 */         if (info != null) {
/*      */           
/* 4244 */           performer.getCommunicator().sendNormalServerMessage("It is being taken care of by " + info
/* 4245 */               .getName() + ".");
/*      */ 
/*      */         
/*      */         }
/* 4249 */         else if (System.currentTimeMillis() - Players.getInstance().getLastLogoutForPlayer(careTaker) > 1209600000L) {
/*      */ 
/*      */           
/* 4252 */           Creatures.getInstance().setCreatureProtected(target, -10L, false);
/*      */         } 
/*      */       } 
/*      */       
/* 4256 */       performer.getCommunicator().sendNormalServerMessage(
/* 4257 */           StringUtilities.raiseFirstLetter(target.getStatus().getBodyType()));
/* 4258 */       if (target.isDominated() && target.isAnimal()) {
/*      */         
/* 4260 */         float loy = target.getLoyalty();
/* 4261 */         if (loy < 10.0F) {
/* 4262 */           exa = target.getNameWithGenus() + " looks upset.";
/* 4263 */         } else if (loy < 20.0F) {
/* 4264 */           exa = target.getNameWithGenus() + " acts nervously.";
/* 4265 */         } else if (loy < 30.0F) {
/* 4266 */           exa = target.getNameWithGenus() + " looks submissive.";
/* 4267 */         } else if (loy < 40.0F) {
/* 4268 */           exa = target.getNameWithGenus() + " looks calm.";
/* 4269 */         } else if (loy < 50.0F) {
/* 4270 */           exa = target.getNameWithGenus() + " looks tame.";
/* 4271 */         } else if (loy < 60.0F) {
/* 4272 */           exa = target.getNameWithGenus() + " acts loyal.";
/* 4273 */         } else if (loy < 70.0F) {
/* 4274 */           exa = target.getNameWithGenus() + " looks trusting.";
/* 4275 */         } else if (loy < 100.0F) {
/* 4276 */           exa = target.getNameWithGenus() + " looks extremely loyal.";
/* 4277 */         }  performer.getCommunicator().sendNormalServerMessage(exa);
/*      */       } 
/* 4279 */       if (target.isDomestic())
/*      */       {
/* 4281 */         if (System.currentTimeMillis() - target.getLastGroomed() > 172800000L)
/* 4282 */           performer.getCommunicator()
/* 4283 */             .sendNormalServerMessage("This creature could use some grooming."); 
/*      */       }
/* 4285 */       if (target.hasTraits()) {
/*      */         
/*      */         try {
/*      */           
/* 4289 */           Skill breeding = performer.getSkills().getSkill(10085);
/* 4290 */           double knowl = breeding.getKnowledge(0.0D);
/* 4291 */           if (knowl > 20.0D) {
/*      */             
/* 4293 */             StringBuilder buf = new StringBuilder();
/* 4294 */             for (int x = 0; x < 64; x++) {
/*      */               
/* 4296 */               if (target.hasTrait(x) && knowl - 20.0D > x) {
/*      */                 
/* 4298 */                 String l = Traits.getTraitString(x);
/* 4299 */                 if (l.length() > 0) {
/*      */                   
/* 4301 */                   buf.append(l);
/* 4302 */                   buf.append(' ');
/*      */                 } 
/*      */               } 
/*      */             } 
/* 4306 */             if (buf.toString().length() > 0) {
/* 4307 */               performer.getCommunicator().sendNormalServerMessage(buf.toString());
/*      */             }
/*      */           } 
/* 4310 */         } catch (NoSuchSkillException noSuchSkillException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4317 */       if (target.isHorse())
/* 4318 */         performer.getCommunicator().sendNormalServerMessage("Its colour is " + target.getColourName() + "."); 
/* 4319 */       if (target.isPregnant()) {
/*      */         
/* 4321 */         Offspring o = target.getOffspring();
/* 4322 */         Random rand = new Random(target.getWurmId());
/* 4323 */         int left = o.getDaysLeft() + rand.nextInt(3);
/* 4324 */         performer.getCommunicator().sendNormalServerMessage(
/* 4325 */             LoginHandler.raiseFirstLetter(target.getHeSheItString()) + " will deliver in about " + left + ((left != 1) ? " days." : " day."));
/*      */       } 
/*      */     } 
/*      */     
/* 4329 */     String motherfather = "";
/* 4330 */     if (target.getMother() != -10L) {
/*      */       
/*      */       try {
/*      */         
/* 4334 */         Creature mother = Server.getInstance().getCreature(target.getMother());
/* 4335 */         motherfather = motherfather + StringUtilities.raiseFirstLetter(target.getHisHerItsString()) + " mother is " + mother.getNameWithGenus() + ". ";
/*      */       }
/* 4337 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */       
/*      */       }
/* 4341 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4346 */     if (target.getFather() != -10L) {
/*      */       
/*      */       try {
/*      */         
/* 4350 */         Creature father = Server.getInstance().getCreature(target.getFather());
/* 4351 */         motherfather = motherfather + StringUtilities.raiseFirstLetter(target.getHisHerItsString()) + " father is " + father.getNameWithGenus() + ". ";
/*      */       }
/* 4353 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */       
/*      */       }
/* 4357 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4362 */     if (motherfather.length() > 0)
/* 4363 */       performer.getCommunicator().sendNormalServerMessage(motherfather); 
/* 4364 */     Village vill = target.getCitizenVillage();
/* 4365 */     if (vill != null) {
/*      */       
/* 4367 */       Citizen citiz = vill.getCitizen(target.getWurmId());
/* 4368 */       if (citiz != null) {
/*      */         
/* 4370 */         VillageRole role = citiz.getRole();
/*      */ 
/*      */         
/* 4373 */         String cit = StringUtilities.raiseFirstLetter(target.getHeSheItString()) + " bears the mark of a " + role.getName() + " of " + vill.getName() + ".";
/* 4374 */         performer.getCommunicator().sendNormalServerMessage(cit);
/*      */         
/* 4376 */         PvPAlliance alliance = PvPAlliance.getPvPAlliance(vill.getAllianceNumber());
/* 4377 */         if (alliance != null) {
/* 4378 */           performer.getCommunicator().sendNormalServerMessage(vill
/* 4379 */               .getName() + " is in the alliance named " + alliance.getName() + ".");
/*      */         }
/*      */       } else {
/*      */         
/* 4383 */         logger.log(Level.WARNING, target.getName() + " with id " + target.getWurmId() + " no citizen role for village " + vill
/* 4384 */             .getName());
/*      */       } 
/*      */     } 
/* 4387 */     if (target.isChampion())
/* 4388 */       performer.getCommunicator().sendNormalServerMessage(
/* 4389 */           StringUtilities.raiseFirstLetter(target.getHeSheItString()) + " is a Champion of " + 
/* 4390 */           (target.getDeity()).name + "."); 
/* 4391 */     if (performer.opponent == target)
/*      */     {
/* 4393 */       if (CombatHandler.isDefend(target.getCombatHandler().getCurrentStance())) {
/* 4394 */         performer.getCommunicator()
/* 4395 */           .sendNormalServerMessage(target.getHeSheItString() + " is in defensive stance.");
/*      */       }
/*      */       else {
/*      */         
/* 4399 */         String desc = CombatHandler.getStanceDescription(target.getCombatHandler().getCurrentStance());
/* 4400 */         performer.getCommunicator()
/* 4401 */           .sendNormalServerMessage(
/* 4402 */             LoginHandler.raiseFirstLetter(target.getHeSheItString()) + " is targetting your " + desc + "parts.");
/*      */ 
/*      */         
/* 4405 */         performer.getCommunicator()
/* 4406 */           .sendCombatNormalMessage(
/* 4407 */             LoginHandler.raiseFirstLetter(target.getHeSheItString()) + " is targetting your " + desc + "parts.");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4413 */     if (performer.getKingdomId() == target.getKingdomId() || target
/* 4414 */       .getAttitude(performer) == 1 || target
/* 4415 */       .getAttitude(performer) == 0)
/*      */     {
/*      */       
/* 4418 */       if (target.getSpellEffects() != null) {
/*      */         
/* 4420 */         SpellEffect[] effs = target.getSpellEffects().getEffects();
/* 4421 */         for (SpellEffect eff : effs) {
/*      */ 
/*      */           
/* 4424 */           performer.getCommunicator().sendNormalServerMessage(
/* 4425 */               String.format("%s has been cast on it, so it has %s [%d]", new Object[] {
/* 4426 */                   eff.getName(), eff.getLongDesc(), Integer.valueOf((int)eff.getPower())
/*      */                 }));
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean handleSpell(@Nonnull Action act, @Nonnull Creature performer, Creature target, short action, float counter) {
/* 4435 */     boolean done = true;
/* 4436 */     Spell spell = Spells.getSpell(action);
/* 4437 */     if (spell != null) {
/*      */       
/* 4439 */       if (spell.offensive) {
/*      */ 
/*      */         
/* 4442 */         if (!Servers.isThisAPvpServer()) {
/*      */           
/* 4444 */           Village bVill = target.getBrandVillage();
/* 4445 */           if (bVill != null)
/*      */           {
/* 4447 */             if (spell.number == 275 || spell.number == 274) {
/*      */               
/* 4449 */               if (!bVill.isActionAllowed((short)46, performer))
/*      */               {
/* 4451 */                 performer.getCommunicator().sendNormalServerMessage(target
/* 4452 */                     .getNameWithGenus() + " rolls its eyes and looks too nervous to focus.");
/*      */                 
/* 4454 */                 return true;
/*      */               }
/*      */             
/* 4457 */             } else if (!bVill.mayAttack(performer, target)) {
/*      */               
/* 4459 */               performer.getCommunicator().sendNormalServerMessage(target
/* 4460 */                   .getNameWithGenus() + " seems to be protected by an aura from its branding.");
/*      */               
/* 4462 */               return true;
/*      */             } 
/*      */           }
/*      */         } 
/* 4466 */         if (!target.isInvulnerable()) {
/*      */           
/* 4468 */           if (!performer.mayAttack(target)) {
/*      */ 
/*      */             
/* 4471 */             if (!performer.isStunned() && !performer.isUnconscious()) {
/*      */               
/* 4473 */               performer.getCommunicator().sendNormalServerMessage("You are too weak to attack.");
/* 4474 */               return true;
/*      */             } 
/*      */             
/* 4477 */             return true;
/*      */           } 
/*      */         } else {
/*      */           
/* 4481 */           performer.getCommunicator().sendNormalServerMessage(target
/* 4482 */               .getNameWithGenus() + " seems to absorb the spell with no effect.");
/*      */           
/* 4484 */           return true;
/*      */         } 
/*      */       } 
/* 4487 */       if (spell.religious) {
/*      */         
/* 4489 */         if (performer.getDeity() != null) {
/*      */           
/* 4491 */           if (spell.number == 275 || spell.number == 274)
/*      */           {
/* 4493 */             if (!Methods.isActionAllowed(performer, action)) {
/* 4494 */               return true;
/*      */             }
/*      */           }
/* 4497 */           if (Methods.isActionAllowed(performer, (short)245)) {
/* 4498 */             done = Methods.castSpell(performer, spell, target, counter);
/*      */           }
/*      */         } else {
/* 4501 */           performer.getCommunicator().sendNormalServerMessage("You have no deity and cannot cast the spell.");
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 4506 */       else if (Methods.isActionAllowed(performer, (short)547)) {
/* 4507 */         done = Methods.castSpell(performer, spell, target, counter);
/*      */       } 
/*      */     } 
/* 4510 */     return done;
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
/*      */   static String getAnimationNameForStanceChange(byte newStance) {
/* 4522 */     StringBuilder sb = new StringBuilder();
/*      */     
/* 4524 */     if (CombatHandler.isDefend(newStance)) {
/* 4525 */       sb.append("defend");
/* 4526 */     } else if (newStance == 0) {
/* 4527 */       sb.append("ready");
/*      */     } 
/* 4529 */     if (CombatHandler.isHigh(newStance)) {
/* 4530 */       sb.append("high");
/* 4531 */     } else if (CombatHandler.isLow(newStance)) {
/* 4532 */       sb.append("low");
/*      */     } 
/* 4534 */     if (CombatHandler.isLeft(newStance)) {
/* 4535 */       sb.append("left");
/* 4536 */     } else if (CombatHandler.isRight(newStance)) {
/* 4537 */       sb.append("right");
/*      */     } 
/* 4539 */     return sb.toString();
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
/*      */   public boolean action(Action act, Creature performer, Item source, Creature target, short action, float counter) {
/* 4551 */     boolean done = false;
/* 4552 */     int stid = source.getTemplateId();
/*      */     
/* 4554 */     switch (action)
/*      */     
/*      */     { case 342:
/* 4557 */         done = true;
/* 4558 */         if (source.isComponentItem() || source.getTemplateId() == 1392) {
/*      */           
/* 4560 */           performer.getCommunicator()
/* 4561 */             .sendNormalServerMessage("You cannot throw that.");
/*      */         
/*      */         }
/* 4564 */         else if (!target.isInvulnerable() || performer.getPower() >= 5) {
/*      */           
/* 4566 */           if (performer.mayAttack(target)) {
/*      */             
/* 4568 */             done = Archery.throwItem(performer, target, source, act);
/*      */           }
/* 4570 */           else if (performer.isGuest()) {
/* 4571 */             performer.getCommunicator().sendNormalServerMessage("Guests may not attack.");
/* 4572 */           } else if (!performer.isStunned() && !performer.isUnconscious()) {
/* 4573 */             performer.getCommunicator()
/* 4574 */               .sendNormalServerMessage("You are too inexperienced to start attacking anyone.");
/*      */           } 
/*      */         } else {
/* 4577 */           performer.getCommunicator().sendNormalServerMessage(target
/* 4578 */               .getNameWithGenus() + " is protected by the gods. You may not attack " + target
/*      */               
/* 4580 */               .getHimHerItString() + ".");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5546 */         return done;case 745: done = true; if (source.getTemplateId() == 1276 || source.getTemplateId() == 833 || (source.getTemplateId() == 1258 && source.getRealTemplateId() == 1195) || (source.getTemplateId() == 1177 && source.getRealTemplateId() == 1195)) { boolean caught = ((Server.rand.nextFloat() * 150.0F) < target.getBodyControl()); Arrows a = new Arrows(source, performer, target, -1, -1, caught ? Arrows.ArrowHitting.NOT : Arrows.ArrowHitting.HIT, caught ? Arrows.ArrowDestroy.DO_NOTHING : Arrows.ArrowDestroy.NORMAL, null, 0.0D, 1.0F, 0.0F, (byte)0, true, 0.0D, 0.0D); float dam = source.getDamage() + 10.0F; if (caught && dam < 100.0F && target.getInventory().mayCreatureInsertItem()) { target.getCommunicator().sendNormalServerMessage(performer.getName() + " throws a " + source.getName() + " at you, but you deftly catch it."); performer.getCommunicator().sendNormalServerMessage(target.getName() + " deftly catches the " + source.getName() + "."); source.setDamage(dam); target.getInventory().insertItem(source, false); source.setBusy(false); } else { String[] reactions = getPlayfulReactionString(); target.getCommunicator().sendNormalServerMessage("You " + reactions[1] + " as " + performer.getName() + " throws a " + source.getName() + " at you!"); performer.getCommunicator().sendNormalServerMessage(target.getName() + " " + reactions[0] + " as you throw a " + source.getName() + " at " + target.getHimHerItString() + "!"); if (reactions[0].equals("falls over")) target.playAnimation("sprawl", false);  Items.destroyItem(source.getWurmId()); }  return done; }  return done;case 81: done = true; Methods.sendAllianceQuestion(performer, target); return done;case 345: if (target.isMilkable()) { done = MethodsCreatures.milk(act, source, performer, target, counter); } else { done = true; performer.getCommunicator().sendNormalServerMessage("You can't milk that!"); logger.log(Level.WARNING, performer.getName() + " tried to milk " + target.getName()); }  return done;case 646: if (target.isWoolProducer()) { done = MethodsCreatures.shear(act, source, performer, target, counter); } else { done = true; }  return done;case 185: if (source.getTemplateId() == 176 && performer.getPower() > 0 && source.getAuxData() == 1) { String positionText = StringUtil.format("TileX: %d TileY: %d, id: %d", new Object[] { Integer.valueOf(target.getTileX()), Integer.valueOf(target.getTileY()), Long.valueOf(target.getWurmId()) }); performer.getCommunicator().sendNormalServerMessage(positionText + " pathing=" + target.isPathing() + " has path=" + ((target.getStatus().getPath() != null) ? 1 : 0) + " should stand still=" + target.shouldStandStill + " leader=" + target.leader); done = true; } else { done = action(act, performer, target, action, counter); }  return done;case 230: done = true; if (!target.isNeedFood()) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " refuses to be fed."); } else if (!target.canEat()) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " does not seem interested in food right now."); } else if (!source.isEdibleBy(target)) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " seems uninterested in the " + source.getName() + "."); } else { target.eat(source); performer.achievement(166); if (!target.canEat()) { performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " does not seem hungry any longer."); if (performer.getPet() != null && performer.getPet() == target) { target.modifyLoyalty(1.0F); performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " seems pleased with you."); }  }  }  return done;case 46: if (source.isEdibleBy(target)) { if (stid == 272) { done = true; performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " seems uninterested in the " + source.getName() + "."); } else if (target.isDominatable(performer) && target.isAnimal()) { done = MethodsCreatures.tame(act, performer, target, source, counter); } else { done = true; }  } else { done = true; performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " seems uninterested in the " + source.getName() + "."); }  return done;case 398: done = true; if (target.isDomestic()) if (stid == 647) if (source.getOwnerId() == performer.getWurmId()) done = MethodsCreatures.groom(performer, target, source, action, act, counter);    return done;case 31: done = true; if (target.isTrader() && !target.isPlayer()) if (!Servers.localServer.LOGINSERVER || Servers.localServer.testServer) if (target.getFloorLevel() == performer.getFloorLevel()) if (performer.getKingdomId() == target.getKingdomId()) if (target.getCurrentVillage() == null || !target.getCurrentVillage().isEnemy(performer)) if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 0, 2)) done = Methods.discardSellItem(performer, act, source, counter);       return done;case 106: done = true; if (target.getLeader() == null && target.isLeadable(performer)) if (source.isLeadCreature() || target.hasBridle()) { float tstZ = target.getStatus().getPositionZ() + target.getAltOffZ(); if (!performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), CoordUtils.WorldToTile(tstZ), 1)) { performer.getCommunicator().sendNormalServerMessage("You are too far away to lead " + target.getNameWithGenus()); } else if (!performer.mayLeadMoreCreatures()) { performer.getCommunicator().sendNormalServerMessage("You would get nowhere if you tried to lead more creatures."); } else { if (performer.getBridgeId() == -10L && target.getBridgeId() == -10L) if (performer.getFloorLevel(true) != target.getFloorLevel()) { performer.getCommunicator().sendNormalServerMessage("You must be on the same floor level to lead."); return true; }   boolean lastLed = target.isBranded() ? false : Creatures.getInstance().wasLastLed(performer.getWurmId(), target.getWurmId()); if (performer.getVehicle() <= -10L || performer.isVehicleCommander()) { if (target.mayLead(performer) || lastLed || Servers.isThisAPvpServer()) { boolean canItemLead = (!performer.isItemLeading(source) || source.isLeadMultipleCreatures()); if (canItemLead || target.hasBridle()) { target.setLeader(performer); if (source != null && source.isRope() && !target.hasBridle()) { performer.addFollower(target, source); } else { performer.addFollower(target, null); }  if (target.getVisionArea() != null) target.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());  if ((performer.getFollowers()).length > 2) { performer.achievement(136); if (performer.getVehicle() != -10L) { Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle()); if (vehicle != null && vehicle.creature) { boolean award = true; for (Creature c : performer.getFollowers()) { int cid = c.getTemplate().getTemplateId(); if (cid != 82 && cid != 3 && cid != 49 && cid != 50) award = false;  }  if (award) performer.achievement(137);  }  }  }  } else { Creature follower = performer.getFollowedCreature(source); if (follower != null) { performer.getCommunicator().sendNormalServerMessage("You are already using that item to lead " + follower.getNameWithGenus()); } else { performer.getCommunicator().sendNormalServerMessage("That item is already used to lead a creature."); }  }  } else { performer.getCommunicator().sendNormalServerMessage("You don't have permission to lead."); }  } else if (Vehicles.getVehicleForId(performer.getVehicle()).isChair()) { performer.getCommunicator().sendNormalServerMessage("You can't lead while sitting."); } else { performer.getCommunicator().sendNormalServerMessage("You can't lead as a passenger."); }  }  }   return done;case 484: if (stid == 701) { done = MethodsCreatures.brand(performer, target, source, act, counter); } else if (performer.getPower() >= 4 && stid == 176) { Brand brand = Creatures.getInstance().getBrand(target.getWurmId()); if (brand != null) { logger.log(Level.INFO, "Deleting brand for " + target.getName() + " by Arch Command."); brand.deleteBrand(); performer.getCommunicator().sendNormalServerMessage("You filled in the old brand!"); }  done = true; } else { done = action(act, performer, target, action, counter); }  return done;case 643: if (stid == 701) { done = MethodsCreatures.unbrand(performer, target, source, act, counter); } else { done = action(act, performer, target, action, counter); }  return done;case 142: done = true; if (stid != 792) { performer.getCommunicator().sendNormalServerMessage("You need to use a special ceremonial knife to do this."); } else if (!target.isUnique() && !target.isUndead() && !target.isReborn() && target.getHitched() == null && !target.isNpc() && !target.isRidden() && (Servers.isThisAPvpServer() || (!target.isDominated() && !target.isBranded()))) { if (!Methods.isActionAllowed(performer, action, target.getTileX(), target.getTileY())) { performer.getCommunicator().sendNormalServerMessage("You need permission to do this."); } else { done = MethodsReligion.sacrifice(performer, target, source, act, counter); }  }  return done;case 397: done = true; if (source.isPuppet()) { boolean found = false; try { Item heldRight = target.getEquippedItem((byte)38); if (heldRight != null) { found = true; done = MethodsItems.puppetSpeak(performer, source, heldRight, act, counter); }  } catch (NoSpaceException noSpaceException) {} if (!found) try { Item heldLeft = target.getEquippedItem((byte)37); if (heldLeft != null) { done = MethodsItems.puppetSpeak(performer, source, heldLeft, act, counter); found = true; }  } catch (NoSpaceException noSpaceException) {}  if (!found) performer.getCommunicator().sendNormalServerMessage(target.getName() + " is not holding a puppet.");  }  return done;case 346: done = true; if ((stid == 176 || stid == 315) && performer.getPower() >= 2) try { Wound[] wounds = target.getBody().getWounds().getWounds(); for (Wound lWound : wounds) { performer.getCommunicator().sendNormalServerMessage("Healing " + lWound.getDescription()); lWound.heal(); }  if (performer.getLogger() != null) performer.getLogger().log(Level.INFO, performer.getName() + " healing " + target.getName());  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   return done;case 213: done = true; if (target.isGuide()) { if (performer.getKingdomId() != target.getKingdomId()) if (performer.isChampion()) { performer.getCommunicator().sendNormalServerMessage("You are a champion and may not change kingdom."); } else { MethodsCreatures.sendAskKingdomQuestion(target, performer); }   } else if (!target.acceptsInvitations()) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " must issue the command '/invitations' to start accepting converting."); } else { if (target.isInvulnerable()) { performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " must not be invulnerable!"); return true; }  try { target.getCurrentAction(); performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " is too busy right now."); } catch (NoSuchActionException nsa) { MethodsReligion.sendAskConvertQuestion(performer, target, source); }  }  return done;case 216: done = MethodsReligion.preach(performer, target, source, counter); return done;case 938: done = MethodsCreatures.catchFlies(performer, target, source, action, act, counter); return done;case 118: done = true; if (source.getOwnerId() == performer.getWurmId()) if (source.isArtifact() && !ArtifactBehaviour.mayUseItem(source, performer)) { performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " emits no sense of power right now."); } else if (stid == 330) { done = false; if (counter == 1.0F) { performer.sendActionControl("dominating", true, 100); act.setTimeLeft(100); } else if (counter >= 10.0F) { done = true; if (!performer.isWithinDistanceTo(target, 8.0F)) { performer.getCommunicator().sendNormalServerMessage("You need to be closer to the " + target.getName() + " in order to control it."); } else if (!Dominate.mayDominate(performer, target)) { performer.getCommunicator().sendNormalServerMessage("Nothing happens."); } else { float f = 1.0F; try { f = (float)target.getSkills().getSkill(105).getKnowledge(0.0D); } catch (NoSuchSkillException nss) { target.getSkills().learn(105, 1.0F); }  if (Server.rand.nextInt(100) > f || performer.getPower() >= 5) { Server.getInstance().broadCastAction(performer.getName() + " uses " + performer.getHisHerItsString() + " " + source.getName() + "!", performer, 5); Dominate.dominate(50.0D, performer, target); if (performer.getPower() < 5) source.setData(WurmCalendar.currentTime);  } else { source.setData(WurmCalendar.currentTime - 691200L + 1800L); performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " resists your attempt to dominate " + target.getHimHerItString() + "."); }  source.setAuxData((byte)(source.getAuxData() - 1)); }  }  } else if (stid == 331) { if (!target.isDominatable(performer) || !target.isAnimal()) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " does not seem subjective to the effect."); } else if (!performer.isWithinDistanceTo(target, 8.0F)) { performer.getCommunicator().sendNormalServerMessage("You need to be closer to the " + target.getName() + " in order to control it."); done = true; } else { Server.getInstance().broadCastAction(performer.getName() + " uses " + performer.getHisHerItsString() + " " + source.getName() + "!", performer, 5); MethodsCreatures.tameEffect(performer, target, 50.0D, false, 50.0D); source.setData(WurmCalendar.currentTime); }  } else if (stid == 334) { target.getStatus().modifyStamina2(100.0F); target.getStatus().refresh(0.99F, true); Server.getInstance().broadCastAction(performer.getNameWithGenus() + " uses " + performer.getHisHerItsString() + " " + source.getName() + "!", performer, 5); performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " is now refreshed."); target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " refreshes you."); source.setAuxData((byte)(source.getAuxData() - 1)); source.setData(WurmCalendar.currentTime); }   return done;case 945: if (source.getTemplate().isRune()) if (RuneUtilities.isSingleUseRune(source) && ((RuneUtilities.getSpellForRune(source) != null && RuneUtilities.getSpellForRune(source).isTargetCreature()) || RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_REFRESH) > 0.0F || RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_CHANGE_AGE) > 0.0F)) done = useRuneOnCreature(act, performer, source, target, action, counter);   return done;case 47: done = MissionTriggers.activateTriggers(performer, source, action, target.getWurmId(), act.currentSecond()); if (!source.deleted) if (performer.getPower() >= 2) if (!source.isNoDrop()) { source.putInVoid(); target.getInventory().insertItem(source, true); if (target.isHuman() || target.getModelName().contains("humanoid")) target.wearItems();  }    return done;case 34: done = MethodsCreatures.findCaveExit(target, performer); return done;case 115: if (performer.isPriest() && performer.getDeity() == target.getDeity()) done = MethodsReligion.listen(performer, target, act, counter);  return done;case 286: done = true; handle_BECOME_PRIEST(performer, target); return done;case 399: done = true; handle_MAGICLINK(performer, target); return done;case 387: done = true; handle_LIGHT_PATH(performer, source, target); return done;case 329: if (stid == 489 || ((stid == 176 || stid == 315) && performer.getPower() >= 2)) { done = MethodsItems.watchSpyglass(performer, source, act, counter); } else { done = action(act, performer, target, action, counter); }  return done;case 538: done = true; if (performer.getPower() >= 4 && !(target instanceof Player)) Methods.sendGmSetTraitsQuestion(performer, target);  return done;case 721: done = true; if (performer.getPower() >= 4 && target instanceof Player) Methods.sendGmSetMedpathQuestion(performer, target);  return done;case 472: done = true; if (stid == 676 && source.getOwnerId() == performer.getWurmId()) { MissionManager m = new MissionManager(performer, "Manage missions", "Select action", target.getWurmId(), target.getName(), source.getWurmId()); m.sendQuestion(); }  return done;case 354: done = true; handle_APPOINT(performer, source, stid); return done;case 179: done = true; handle_SUMMON(performer, target, stid); return done;case 92: done = true; if (performer.getPower() >= 4 || (Servers.localServer.testServer && performer.getPower() >= 3)) if (stid == 176 || stid == 315) Methods.sendLearnSkillQuestion(performer, source, target.getWurmId());   return done;case 352: if (performer.getPower() < 2) { done = action(act, performer, target, action, counter); } else { done = true; if (target.loggerCreature1 == -10L) { target.loggerCreature1 = performer.getWurmId(); performer.getCommunicator().sendSafeServerMessage("You now log " + target.getName()); performer.getLogger().log(Level.INFO, "Started logging " + target.getName()); } else if (target.loggerCreature1 == performer.getWurmId()) { target.loggerCreature1 = -10L; performer.getCommunicator().sendSafeServerMessage("You no longer log " + target.getName()); performer.getLogger().log(Level.INFO, "Stopped logging " + target.getName()); }  }  return done;case 486: done = true; if (Servers.localServer.testServer && performer.getPower() > 1) if (target.isChampion()) { target.revertChamp(); } else { long wid = -10L; EndGameItem altar = null; EndGameItem goodAltar = EndGameItems.getGoodAltar(); EndGameItem evilAltar = EndGameItems.getEvilAltar(); if (goodAltar != null) altar = goodAltar;  if ((performer.getDeity()).number == 4 && evilAltar != null) altar = evilAltar;  if (altar != null) wid = altar.getWurmid();  RealDeathQuestion cq = new RealDeathQuestion(performer, "Real death", "Offer to become a Champion:", wid, performer.getDeity()); cq.sendQuestion(); target.becomeChamp(); }   return done;case 85: done = true; if (performer.getPower() >= 2) if (stid == 300) { Shop shop = Economy.getEconomy().getShop(target); if (shop == null || !shop.isPersonal()) { performer.getCommunicator().sendSafeServerMessage("The merchant has no shop. Weird."); } else if (performer.getPower() > 0) { if (source.getData() >= 0L) { performer.getCommunicator().sendSafeServerMessage("The contract already manages a merchant."); } else { source.setData(target.getWurmId()); performer.getCommunicator().sendSafeServerMessage("The contract will now manage " + target.getName() + "."); }  }  }   return done;case 244: done = true; if (performer.getPower() <= 1) { performer.getCommunicator().sendNormalServerMessage("This option is currently unavailable to your level."); } else if (stid == 176 || stid == 315) { Methods.sendServerManagementQuestion(performer, target.getWurmId()); }  return done;case 698: done = true; Players.appointCA(performer, target.getName()); return done;case 699: done = true; Players.appointCM(performer, target.getName()); return done;case 700: done = true; Players.displayLCMInfo(performer, target.getName()); return done;case 637: if (stid == 903) { done = MethodsSurveying.planBridge(act, performer, source, target, null, action, counter); } else { done = action(act, performer, target, action, counter); }  return done;case 640: if (stid == 903) { done = MethodsSurveying.survey(act, performer, source, target, null, action, counter); } else { done = action(act, performer, target, action, counter); }  return done;case 88: if (target.isFish() && stid == 176) Methods.sendSetDataQuestion(performer, target);  done = true; return done; }  if (Archery.isArchery(action)) { done = Archery.attack(performer, target, source, counter, act); } else if (act.isSpell()) { done = true; Spell spell = Spells.getSpell(action); if (spell != null) { if (spell.offensive) { if (!Servers.isThisAPvpServer()) { Village bVill = target.getBrandVillage(); if (bVill != null) if (spell.number == 275 || spell.number == 274) { if (!bVill.isActionAllowed((short)46, performer)) { performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " rolls its eyes and looks too nervous to focus."); return true; }  } else if (!bVill.mayAttack(performer, target)) { performer.getCommunicator().sendNormalServerMessage(target.getName() + " seems to be protected by an aura from its branding."); return true; }   }  if (!target.isInvulnerable()) { if (!performer.mayAttack(target)) { if (!performer.isStunned() && !performer.isUnconscious()) { performer.getCommunicator().sendNormalServerMessage("You are too weak to attack."); return true; }  return true; }  } else { performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " is protected by the gods."); return true; }  }  if (spell.religious) { if (performer.getDeity() != null) { if (performer.isSpellCaster() || source.isHolyItem(performer.getDeity())) { if (Methods.isActionAllowed(performer, (short)245)) { if (spell.number == 275 || spell.number == 274) if (!Methods.isActionAllowed(performer, action, target.getTileX(), target.getTileY())) return true;   done = Methods.castSpell(performer, spell, target, counter); }  } else { performer.getCommunicator().sendNormalServerMessage((performer.getDeity()).name + " will not let you use that item."); }  } else { performer.getCommunicator().sendNormalServerMessage("You have no deity and cannot cast the spell."); }  } else if (performer.isSpellCaster() || source.isMagicStaff() || (source.getTemplateId() == 176 && performer.getPower() >= 2 && Servers.isThisATestServer())) { if (Methods.isActionAllowed(performer, (short)547, target.getTileX(), target.getTileY())) done = Methods.castSpell(performer, spell, target, counter);  } else { performer.getCommunicator().sendNormalServerMessage("You need to use a magic staff."); done = true; }  } else { logger.log(Level.INFO, performer.getName() + " tries to cast unknown spell:" + Actions.actionEntrys[action].getActionString()); performer.getCommunicator().sendNormalServerMessage("That spell is unknown."); }  } else { done = action(act, performer, target, action, counter); }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_BECOME_PRIEST(Creature performer, Creature target) {
/* 5551 */     if (!performer.isPriest()) {
/*      */       
/* 5553 */       performer.getCommunicator().sendNormalServerMessage("You must be a priest to make a priest!");
/*      */       return;
/*      */     } 
/* 5556 */     if (!target.acceptsInvitations()) {
/*      */       
/* 5558 */       performer.getCommunicator().sendNormalServerMessage(target
/* 5559 */           .getName() + " must issue the command '/invitations' to start accepting converting.");
/*      */       return;
/*      */     } 
/* 5562 */     if (target.getDeity() != performer.getDeity()) {
/*      */       
/* 5564 */       performer.getCommunicator().sendNormalServerMessage("You must be of the same religion as " + target
/* 5565 */           .getName() + ".");
/*      */       return;
/*      */     } 
/* 5568 */     if (Servers.localServer.PVPSERVER && !Servers.localServer.testServer) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 5575 */       target.getCurrentAction();
/* 5576 */       performer.getCommunicator()
/* 5577 */         .sendNormalServerMessage(target.getNameWithGenus() + " is too busy right now.");
/*      */     }
/* 5579 */     catch (NoSuchActionException nsa) {
/*      */       
/* 5581 */       performer.getCommunicator().sendNormalServerMessage("You ask " + target
/* 5582 */           .getName() + " if " + target.getHeSheItString() + " wants to become a priest.");
/*      */       
/* 5584 */       target.getCommunicator().sendNormalServerMessage(performer
/* 5585 */           .getName() + " asks you if you want to become a priest.");
/* 5586 */       Server.getInstance().broadCastAction(performer
/* 5587 */           .getName() + " asks " + target.getName() + " if " + target
/* 5588 */           .getHeSheItString() + " wants to become a priest.", performer, target, 5);
/*      */ 
/*      */       
/* 5591 */       MethodsCreatures.sendAskPriestQuestion(target, null, performer);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void handle_MAGICLINK(Creature performer, Creature target) {
/* 5599 */     if (target.getDeity().getTemplateDeity() != performer.getDeity().getTemplateDeity()) {
/*      */       
/* 5601 */       performer.getCommunicator().sendNormalServerMessage("You must be of the same religion as " + target
/* 5602 */           .getName() + ".");
/*      */       return;
/*      */     } 
/* 5605 */     if (!performer.isFriendlyKingdom(target.getKingdomId())) {
/*      */       return;
/*      */     }
/*      */     
/* 5609 */     if (!performer.isPriest() || !target.isPriest()) {
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
/*      */     try {
/* 5621 */       target.getCurrentAction();
/* 5622 */       performer.getCommunicator()
/* 5623 */         .sendNormalServerMessage(target.getName() + " is too busy right now.");
/*      */     }
/* 5625 */     catch (NoSuchActionException nsa) {
/*      */       
/* 5627 */       if (target.isLinked()) {
/*      */         
/* 5629 */         performer.getCommunicator().sendNormalServerMessage(target
/* 5630 */             .getName() + " is linked and can not use links himself.");
/*      */       }
/* 5632 */       else if (target.getChannelingSkill().getKnowledge(0.0D) / 10.0D > target.getNumLinks()) {
/*      */         
/* 5634 */         Server.getInstance().broadCastAction(performer
/* 5635 */             .getName() + " links with " + target.getName() + ".", performer, target, 5);
/*      */ 
/*      */ 
/*      */         
/* 5639 */         performer.setLinkedTo(target.getWurmId(), true);
/* 5640 */         performer.achievement(615);
/*      */       } else {
/*      */         
/* 5643 */         performer.getCommunicator().sendNormalServerMessage(target
/* 5644 */             .getName() + " may not accept more links right now.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_LIGHT_PATH(Creature performer, Item source, Creature target) {
/* 5651 */     if (!source.isMeditation()) {
/*      */       return;
/*      */     }
/*      */     
/* 5655 */     Cultist perfCultist = Cultist.getCultist(performer.getWurmId());
/* 5656 */     Communicator comm = performer.getCommunicator();
/* 5657 */     if (!Methods.isActionAllowed(performer, (short)384) || perfCultist == null || perfCultist
/* 5658 */       .getLevel() <= 4) {
/*      */       
/* 5660 */       comm.sendNormalServerMessage("You are not able to give enlightenment yet.");
/*      */       return;
/*      */     } 
/* 5663 */     Cultist respCultist = Cultist.getCultist(target.getWurmId());
/* 5664 */     String targetName = target.getName();
/* 5665 */     if (respCultist.getPath() != perfCultist.getPath()) {
/*      */       
/* 5667 */       comm.sendNormalServerMessage(targetName + " is not susceptible enlightenment from your path.");
/*      */       
/*      */       return;
/*      */     } 
/* 5671 */     if (respCultist == null || respCultist.getLevel() <= 0) {
/*      */       
/* 5673 */       comm.sendNormalServerMessage(targetName + " is not susceptible to enlightenment on any path.");
/*      */       
/*      */       return;
/*      */     } 
/* 5677 */     if (perfCultist.getLevel() - respCultist.getLevel() != 3) {
/*      */       
/* 5679 */       comm.sendNormalServerMessage(targetName + " is not susceptible to that enlightenment right now.");
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 5685 */     long timeSinceLastEnlightAttempt = System.currentTimeMillis() - respCultist.getLastEnlightened();
/* 5686 */     if (timeSinceLastEnlightAttempt <= 60000L) {
/*      */       
/* 5688 */       comm.sendNormalServerMessage(targetName + " has to ponder the path another " + 
/*      */           
/* 5690 */           Server.getTimeFor(respCultist.getLastEnlightened() + 60000L - 
/* 5691 */             System.currentTimeMillis()) + ".");
/*      */       
/*      */       return;
/*      */     } 
/* 5695 */     long lastAppointedLevel = perfCultist.getLastAppointedLevel();
/* 5696 */     long timeSinceLast = System.currentTimeMillis() - lastAppointedLevel;
/* 5697 */     if (timeSinceLast <= 604800000L && performer.getPower() < 5) {
/*      */       
/* 5699 */       comm.sendNormalServerMessage("You may not light the path again until another " + 
/*      */ 
/*      */           
/* 5702 */           Server.getTimeFor(lastAppointedLevel + 604800000L - 
/* 5703 */             System.currentTimeMillis()) + " has passed.");
/*      */       return;
/*      */     } 
/* 5706 */     if (timeSinceLast < 604800000L)
/*      */     {
/* 5708 */       comm.sendNormalServerMessage("You actually should not be able light the path again until another " + 
/*      */           
/* 5710 */           Server.getTimeFor(lastAppointedLevel + 604800000L - 
/*      */             
/* 5712 */             System.currentTimeMillis()) + " has passed.");
/*      */     }
/*      */     
/* 5715 */     if (!target.acceptsInvitations()) {
/*      */       
/* 5717 */       comm.sendNormalServerMessage(targetName + " needs to type in the command /invitations.");
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 5723 */     boolean mayIncreaseLevel = false;
/*      */     
/* 5725 */     long timeToNextLevel = respCultist.getTimeLeftToIncreasePath(System.currentTimeMillis(), target
/* 5726 */         .getSkills().getSkillOrLearn(10086).getKnowledge(0.0D));
/* 5727 */     if (timeToNextLevel <= 0L) {
/*      */       
/* 5729 */       mayIncreaseLevel = true;
/*      */     }
/* 5731 */     else if (System.currentTimeMillis() - respCultist
/* 5732 */       .getLastReceivedLevel() > 604800000L) {
/* 5733 */       mayIncreaseLevel = true;
/* 5734 */     } else if (performer.getPower() >= 5) {
/*      */       
/* 5736 */       comm.sendNormalServerMessage(targetName + " next level=" + 
/*      */ 
/*      */           
/* 5739 */           Server.getTimeFor(timeToNextLevel) + ", last received=" + 
/*      */           
/* 5741 */           Server.getTimeFor(System.currentTimeMillis() - respCultist.getLastReceivedLevel()) + ".");
/* 5742 */       mayIncreaseLevel = true;
/*      */     } else {
/*      */       
/* 5745 */       comm.sendNormalServerMessage(targetName + " must wait " + 
/*      */ 
/*      */           
/* 5748 */           Server.getTimeFor(respCultist.getTimeLeftToIncreasePath(System.currentTimeMillis(), target
/* 5749 */               .getSkills().getSkillOrLearn(10086).getKnowledge(0.0D))) + " until next enlightenment.");
/*      */     } 
/* 5751 */     if (!mayIncreaseLevel) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 5756 */     comm.sendNormalServerMessage("You attempt to light the path for " + targetName + ".");
/*      */     
/* 5758 */     Skill meditation = null;
/*      */     
/*      */     try {
/* 5761 */       meditation = target.getSkills().getSkill(10086);
/*      */     }
/* 5763 */     catch (NoSuchSkillException nss) {
/*      */       
/* 5765 */       comm.sendNormalServerMessage(targetName + " needs to at least learn how to meditate!");
/*      */       
/*      */       return;
/*      */     } 
/* 5769 */     if (meditation == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 5774 */     respCultist.setLastEnlightened(System.currentTimeMillis());
/*      */ 
/*      */     
/* 5777 */     float diff = respCultist.getLevel() * 20.0F;
/* 5778 */     if (meditation.skillCheck(diff, 0.0D, true, 1.0F) <= 0.0D) {
/*      */       
/* 5780 */       comm.sendNormalServerMessage(targetName + " does not grasp the question. " + 
/*      */ 
/*      */           
/* 5783 */           LoginHandler.raiseFirstLetter(target
/* 5784 */             .getHeSheItString()) + " needs to meditate more.");
/*      */       
/* 5786 */       target.getCommunicator()
/* 5787 */         .sendNormalServerMessage(performer
/* 5788 */           .getName() + " asks you a very confusing question. You do not understand it and need to contemplate more.");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5799 */     comm.sendNormalServerMessage("You ask " + targetName + " a question. " + 
/*      */ 
/*      */ 
/*      */         
/* 5803 */         LoginHandler.raiseFirstLetter(target.getHeSheItString()) + " seems to think hard.");
/*      */     
/* 5805 */     target.getCommunicator().sendNormalServerMessage(performer.getName() + " asks you a question.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5814 */     CultQuestion cq = new CultQuestion(target, "Question asked by " + performer.getName(), "As follows:", target.getWurmId(), respCultist, respCultist.getPath(), false, false);
/*      */ 
/*      */     
/* 5817 */     cq.sendQuestion();
/* 5818 */     perfCultist.setLastAppointedLevel(System.currentTimeMillis());
/*      */ 
/*      */     
/*      */     try {
/* 5822 */       perfCultist.saveCultist(false);
/*      */     }
/* 5824 */     catch (IOException iox) {
/*      */       
/* 5826 */       logger.log(Level.INFO, performer.getName() + " " + iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void handle_APPOINT(Creature performer, Item source, int stid) {
/* 5833 */     if (!source.isRoyal()) {
/*      */       return;
/*      */     }
/*      */     
/* 5837 */     if (stid != 535 && stid != 529 && stid != 532) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5844 */     King k = King.getKing(performer.getKingdomId());
/* 5845 */     assert k != null;
/* 5846 */     if (k.kingid != performer.getWurmId()) {
/*      */       
/* 5848 */       performer.getCommunicator().sendNormalServerMessage("You laugh at yourself - you who probably couldn't even appoint a cat to catch mice, now wielding a mighty sceptre! How preposterous!");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5858 */     AppointmentsQuestion question = new AppointmentsQuestion(performer, "Appointments", "Which appointments do you wish to do today?", source.getWurmId());
/*      */     
/* 5860 */     question.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handle_SUMMON(Creature performer, Creature target, int stid) {
/* 5865 */     if (performer.getPower() < 2) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 5871 */     if (stid != 176 && stid != 315) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 5877 */     TilePos perfTile = performer.getTilePos();
/* 5878 */     int tilex = perfTile.x;
/* 5879 */     int tiley = perfTile.y;
/* 5880 */     int layer = performer.getLayer();
/* 5881 */     if (target instanceof Player) {
/*      */       
/* 5883 */       String pName = target.getName();
/* 5884 */       QuestionParser.summon(pName, performer, tilex, tiley, (byte)layer);
/*      */       
/*      */       return;
/*      */     } 
/* 5888 */     Server.getInstance().broadCastAction(target.getNameWithGenus() + " suddenly disappears.", target, 5);
/* 5889 */     blinkTo(target, performer.getPosX(), performer.getPosY(), performer.getLayer(), performer.getPositionZ(), performer.getBridgeId(), performer.getFloorLevel());
/* 5890 */     logger.log(Level.INFO, performer
/*      */         
/* 5892 */         .getName() + " summoned creature " + target.getName() + ", with ID: " + target
/* 5893 */         .getWurmId() + " at coords " + tilex + ',' + tiley);
/*      */     
/* 5895 */     Server.getInstance().broadCastAction(target.getNameWithGenus() + " suddenly appears.", target, 5);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void blinkTo(Creature target, float posx, float posy, int layer, float posz, long bridgeId, int floorLevel) {
/* 5900 */     target.getCurrentTile().deleteCreature(target);
/* 5901 */     target.setPositionX(posx);
/* 5902 */     target.setPositionY(posy);
/* 5903 */     target.setLayer(layer, true);
/* 5904 */     target.setPositionZ(posz);
/* 5905 */     target.setBridgeId(bridgeId);
/* 5906 */     target.getMovementScheme().setPosition(posx, posy, posz, target
/*      */ 
/*      */         
/* 5909 */         .getStatus().getRotation(), layer);
/*      */     
/* 5911 */     target.getMovementScheme().setBridgeId(bridgeId);
/* 5912 */     if (bridgeId == -10L) {
/* 5913 */       target.pushToFloorLevel(floorLevel);
/*      */     }
/*      */     
/*      */     try {
/* 5917 */       target.createVisionArea();
/* 5918 */       Zones.getZone(target.getTileX(), target.getTileY(), target.isOnSurface()).addCreature(target
/* 5919 */           .getWurmId());
/*      */     }
/* 5921 */     catch (NoSuchZoneException ez) {
/*      */ 
/*      */       
/* 5924 */       logger.log(Level.WARNING, ez.getMessage(), (Throwable)ez);
/*      */     }
/* 5926 */     catch (NoSuchCreatureException e) {
/*      */ 
/*      */       
/* 5929 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     }
/* 5931 */     catch (NoSuchPlayerException e) {
/*      */ 
/*      */       
/* 5934 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     }
/* 5936 */     catch (Exception ex) {
/*      */       
/* 5938 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOpponent(Creature performer, Creature target, boolean done, Action act) {
/* 5945 */     if (done)
/* 5946 */       performer.setOpponent(null); 
/* 5947 */     if (!done && target.opponent == null) {
/*      */ 
/*      */       
/*      */       try {
/* 5951 */         if (target.getCurrentAction() != null)
/*      */         {
/* 5953 */           if (target.getCurrentAction().getPriority() < act.getPriority())
/*      */           {
/* 5955 */             target.getCurrentAction().stop(false);
/*      */           }
/*      */         }
/*      */       }
/* 5959 */       catch (NoSuchActionException noSuchActionException) {}
/*      */ 
/*      */ 
/*      */       
/* 5963 */       target.setOpponent(performer);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean moveCreature(Creature performer, Creature target, int counter, Action act) {
/* 5969 */     String actString = "push";
/* 5970 */     if (act.getNumber() == 181)
/* 5971 */       actString = "pull"; 
/* 5972 */     boolean toReturn = false;
/* 5973 */     int time = 50;
/* 5974 */     if (performer.getPower() > 0)
/* 5975 */       time = 10; 
/* 5976 */     if (performer.isGuest()) {
/*      */       
/* 5978 */       performer.getCommunicator().sendNormalServerMessage("Sorry, but we cannot allow our guests to push people around.");
/* 5979 */       return true;
/*      */     } 
/* 5981 */     if (performer.getStrengthSkill() < 21.0D) {
/*      */       
/* 5983 */       performer.getCommunicator().sendNormalServerMessage("You are too weak to " + actString + " " + target
/* 5984 */           .getNameWithGenus() + " around.");
/* 5985 */       return true;
/*      */     } 
/* 5987 */     if (counter == 1) {
/*      */       
/* 5989 */       act.setTimeLeft(time);
/* 5990 */       performer.getCommunicator().sendNormalServerMessage("You start to " + actString + " " + target.getNameWithGenus() + ".");
/* 5991 */       Server.getInstance().broadCastAction(performer
/* 5992 */           .getNameWithGenus() + " starts to " + actString + " " + target.getNameWithGenus() + ".", performer, 5);
/* 5993 */       performer.sendActionControl(actString + "ing " + target.getNameWithGenus(), true, time);
/* 5994 */       if (performer.getPower() == 0) {
/* 5995 */         performer.getStatus().modifyStamina(-1000.0F);
/*      */       }
/*      */     } else {
/*      */       
/* 5999 */       time = act.getTimeLeft();
/*      */     } 
/* 6001 */     if (counter * 10 > time) {
/*      */       
/* 6003 */       toReturn = true;
/* 6004 */       performer.getStatus().modifyStamina(-250.0F);
/*      */       
/*      */       try {
/* 6007 */         float dir = target.getStatus().getRotation();
/*      */         
/* 6009 */         float iposx = target.getStatus().getPositionX();
/* 6010 */         float iposy = target.getStatus().getPositionY();
/*      */         
/* 6012 */         BlockingResult firstCheck = Blocking.getBlockerBetween(performer, target, 4);
/* 6013 */         if ((target.getPushCounter() == 0 || performer.getPower() >= 2) && 
/* 6014 */           !target.isTeleporting() && !target.getMovementScheme().isIntraTeleporting() && target
/* 6015 */           .getVehicle() == -10L && !target.isCantMove() && !target.isDead() && firstCheck == null && target
/* 6016 */           .isVisible() && !target.isInvulnerable() && target
/* 6017 */           .getPower() <= performer.getPower())
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 6022 */           float rot = performer.getStatus().getRotation();
/* 6023 */           float xPosMod = (float)Math.sin((rot * 0.017453292F)) * 1.0F;
/* 6024 */           float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * 1.0F;
/*      */           
/* 6026 */           if (act.getNumber() == 181) {
/*      */             
/* 6028 */             double rotRads = Math.atan2((performer.getStatus().getPositionY() - iposy), (performer.getStatus()
/* 6029 */                 .getPositionX() - iposx));
/* 6030 */             rot = (float)(rotRads * 57.29577951308232D) + 90.0F;
/* 6031 */             xPosMod = (float)Math.sin((rot * 0.017453292F)) * 0.2F;
/* 6032 */             yPosMod = -((float)Math.cos((rot * 0.017453292F))) * 0.2F;
/*      */           } 
/* 6034 */           float newPosX = iposx + xPosMod;
/* 6035 */           float newPosY = iposy + yPosMod;
/* 6036 */           float oldheight = Zones.calculateHeight(iposx, iposy, target.isOnSurface());
/* 6037 */           float height = Zones.calculateHeight(newPosX, newPosY, target.isOnSurface());
/* 6038 */           if (Math.abs(height - oldheight) > 1.0F) {
/*      */             
/* 6040 */             performer.getCommunicator().sendNormalServerMessage("You cannot " + actString + " " + target
/* 6041 */                 .getNameWithGenus() + " that high.");
/* 6042 */             return true;
/*      */           } 
/* 6044 */           BlockingResult result = Blocking.getBlockerBetween(target, iposx, iposy, newPosX, newPosY, target
/* 6045 */               .getPositionZ(), target.getPositionZ(), target.isOnSurface(), target.isOnSurface(), false, 4, -1L, target
/* 6046 */               .getBridgeId(), target.getBridgeId(), false);
/* 6047 */           if (result != null && result.getFirstBlocker() != null) {
/*      */             
/* 6049 */             performer.getCommunicator().sendNormalServerMessage("You cannot " + actString + " " + target
/* 6050 */                 .getNameWithGenus() + " into the " + result
/* 6051 */                 .getFirstBlocker().getName() + ".");
/* 6052 */             return true;
/*      */           } 
/*      */ 
/*      */           
/* 6056 */           performer.getCommunicator().sendNormalServerMessage("You " + actString + " " + target.getNameWithGenus() + ".");
/* 6057 */           Server.getInstance().broadCastAction(performer.getNameWithGenus() + " moves " + target
/* 6058 */               .getNameWithGenus() + ".", performer, 5);
/*      */           
/* 6060 */           target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " moves you around.");
/* 6061 */           target.setPushCounter(200);
/* 6062 */           if ((int)iposx >> 2 == (int)newPosX >> 2 && (int)iposy >> 2 == (int)newPosY >> 2)
/*      */           {
/* 6064 */             ((Player)target).intraTeleport(newPosX, newPosY, height, dir, target.getLayer(), actString + "ed by " + performer
/* 6065 */                 .getName());
/*      */           }
/*      */           else
/*      */           {
/* 6069 */             target.setTeleportPoints((short)((int)newPosX >> 2), (short)((int)newPosY >> 2), target
/* 6070 */                 .getLayer(), 0);
/* 6071 */             if (target.startTeleporting())
/*      */             {
/* 6073 */               target.getCommunicator().sendTeleport(false);
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 6080 */           performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " won't budge.");
/* 6081 */           return true;
/*      */         }
/*      */       
/* 6084 */       } catch (NoSuchZoneException nsz) {
/*      */         
/* 6086 */         performer.getCommunicator().sendNormalServerMessage("You fail to " + actString + " " + target
/* 6087 */             .getNameWithGenus() + ". " + target.getHeSheItString() + " must be stuck.");
/*      */         
/* 6089 */         logger.log(Level.WARNING, performer.getName() + ": " + nsz.getMessage(), (Throwable)nsz);
/*      */       } 
/* 6091 */       toReturn = true;
/*      */     } 
/* 6093 */     return toReturn;
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
/*      */   private boolean mayDismissMerchant(Creature performer, Creature target) {
/* 6109 */     Shop shop = Economy.getEconomy().getShop(target);
/* 6110 */     if (shop.isPersonal()) {
/*      */       
/* 6112 */       if (performer.getPower() >= 4 && Servers.isThisATestServer())
/* 6113 */         return true; 
/* 6114 */       if (performer.getCitizenVillage() != null)
/*      */       {
/*      */         
/* 6117 */         if (performer.getCitizenVillage() == target.getCurrentVillage())
/*      */         {
/* 6119 */           if (performer.getCitizenVillage().getMayor().getId() == performer.getWurmId()) {
/*      */             
/* 6121 */             if (shop.howLongEmpty() > 2419200000L) {
/* 6122 */               return true;
/*      */             }
/* 6124 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(shop.getOwnerId());
/* 6125 */             if (pinf != null) {
/*      */ 
/*      */               
/*      */               try {
/* 6129 */                 pinf.load();
/* 6130 */                 if (System.currentTimeMillis() - pinf.lastLogout > 2419200000L)
/*      */                 {
/* 6132 */                   return true;
/*      */                 }
/*      */               }
/* 6135 */               catch (IOException e) {
/*      */ 
/*      */                 
/* 6138 */                 logger.log(Level.WARNING, e.getMessage(), e);
/*      */               } 
/*      */             } else {
/*      */               
/* 6142 */               return true;
/*      */             } 
/*      */           }  } 
/*      */       }
/*      */     } 
/* 6147 */     return false;
/*      */   }
/*      */   
/*      */   static boolean useRuneOnCreature(Action act, Creature performer, Item source, Creature target, short action, float counter) {
/*      */     String targetName;
/* 6152 */     if (RuneUtilities.isEnchantRune(source)) {
/*      */       
/* 6154 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the rune on that.", (byte)3);
/* 6155 */       return true;
/*      */     } 
/* 6157 */     if (RuneUtilities.getSpellForRune(source) == null) {
/*      */ 
/*      */       
/* 6160 */       if (!Methods.isActionAllowed(performer, (short)384, target.getTileX(), target.getTileY()))
/*      */       {
/* 6162 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to use that here.", (byte)3);
/* 6163 */         return true;
/*      */       }
/*      */     
/* 6166 */     } else if (RuneUtilities.isSingleUseRune(source) && RuneUtilities.getSpellForRune(source) != null) {
/*      */       
/* 6168 */       if (!Methods.isActionAllowed(performer, (short)245, target.getTileX(), target.getTileY()))
/*      */       {
/* 6170 */         performer.getCommunicator().sendNormalServerMessage("You are not allowed to use that here.", (byte)3);
/* 6171 */         return true;
/*      */       }
/*      */     
/* 6174 */     } else if (target.isPlayer() && (RuneUtilities.getSpellForRune(source)).number == 275) {
/*      */       
/* 6176 */       performer.getCommunicator().sendNormalServerMessage(target.getName() + " is not very impressed by the rune.", (byte)3);
/* 6177 */       return true;
/*      */     } 
/* 6179 */     if (RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_CHANGE_AGE) > 0.0F) {
/*      */       
/* 6181 */       if (target.isPlayer() || target.isNpc() || target.isNpcTrader()) {
/*      */         
/* 6183 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " is not very impressed by the rune.", (byte)3);
/* 6184 */         return true;
/*      */       } 
/* 6186 */       if (target.isUnique()) {
/*      */         
/* 6188 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " crushes the rune before you have time to pull it away.");
/* 6189 */         Items.destroyItem(source.getWurmId());
/* 6190 */         return true;
/*      */       } 
/* 6192 */       if (target.getAttitude(performer) == 2) {
/*      */         
/* 6194 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " ignores the rune as " + target.getHeSheItString() + " is more interested in attacking you.", (byte)3);
/*      */         
/* 6196 */         return true;
/*      */       } 
/* 6198 */       if ((target.getStatus()).age <= 12 || target.getTemplate().isBabyCreature()) {
/*      */         
/* 6200 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " cannot become any younger.");
/* 6201 */         return true;
/*      */       } 
/* 6203 */       if ((target.isBranded() && !target.mayManage(performer)) || (target
/* 6204 */         .isDominated() && target.getDominator() != performer) || (target
/* 6205 */         .getLeader() != null && target.getLeader() != performer)) {
/*      */         
/* 6207 */         performer.getCommunicator().sendNormalServerMessage("You do not have permission to use the rune on " + target.getName() + ".");
/* 6208 */         return true;
/*      */       } 
/* 6210 */       if (target.isRidden()) {
/*      */         
/* 6212 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " is rather preoccupied right now. Maybe this will work better when " + target
/* 6213 */             .getHeSheItString() + " is not being ridden.");
/* 6214 */         return true;
/*      */       } 
/* 6216 */       if (target.isHitched() && target.getHitched().isDragger(target)) {
/*      */         
/* 6218 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " is rather preoccupied right now. Maybe this will work better when " + target
/* 6219 */             .getHeSheItString() + " is not hitched.");
/* 6220 */         return true;
/*      */       } 
/* 6222 */       if (target.isPregnant()) {
/*      */         
/* 6224 */         performer.getCommunicator().sendNormalServerMessage("You decide against using the rune on " + target.getName() + " as you're unsure what it will do to the unborn baby.");
/*      */         
/* 6226 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 6230 */     int time = act.getTimeLeft();
/*      */     
/* 6232 */     if (performer == target) {
/*      */       
/* 6234 */       if (target.getSex() == 0) {
/* 6235 */         targetName = "himself";
/*      */       } else {
/* 6237 */         targetName = "herself";
/*      */       } 
/*      */     } else {
/* 6240 */       targetName = "the " + target.getName();
/* 6241 */     }  if (counter == 1.0F) {
/*      */       
/* 6243 */       String actionString = "use the rune on ";
/* 6244 */       performer.getCommunicator().sendNormalServerMessage("You start to use the rune on " + target.getName() + ".");
/* 6245 */       Server.getInstance().broadCastAction(performer
/* 6246 */           .getNameWithGenus() + " starts using " + source.getNameWithGenus() + " on " + targetName + ".", performer, 10);
/*      */       
/* 6248 */       time = Actions.getSlowActionTime(performer, performer.getSoulDepth(), null, 0.0D);
/* 6249 */       act.setTimeLeft(time);
/* 6250 */       performer.sendActionControl(act.getActionString(), true, act.getTimeLeft());
/* 6251 */       performer.getStatus().modifyStamina(-600.0F);
/*      */     } 
/* 6253 */     if (act.currentSecond() % 5 == 0)
/*      */     {
/* 6255 */       performer.getStatus().modifyStamina(-300.0F);
/*      */     }
/* 6257 */     if (counter * 10.0F > time) {
/*      */       
/* 6259 */       Skill soulDepth = performer.getSoulDepth();
/* 6260 */       double diff = (20.0F + source.getDamage()) - (source.getCurrentQualityLevel() + source.getRarity()) - 45.0D;
/* 6261 */       double power = soulDepth.skillCheck(diff, source.getCurrentQualityLevel(), false, counter);
/* 6262 */       if (power > 0.0D) {
/*      */         
/* 6264 */         if (RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_REFRESH) > 0.0F) {
/*      */           
/* 6266 */           target.getStatus().refresh(99.0F, true);
/* 6267 */           if (target != performer) {
/*      */             
/* 6269 */             performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " is now refreshed.");
/* 6270 */             target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " refreshes you.");
/*      */           } else {
/*      */             
/* 6273 */             performer.getCommunicator().sendNormalServerMessage("You feel refreshed.");
/* 6274 */           }  performer.achievement(491);
/*      */         }
/* 6276 */         else if (RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_CHANGE_AGE) > 0.0F) {
/*      */           
/* 6278 */           int redux = Math.max(4, (target.getStatus()).age / 10);
/* 6279 */           int ageSum = Math.max(12, (target.getStatus()).age - redux);
/*      */           
/*      */           try {
/* 6282 */             ((DbCreatureStatus)target.getStatus()).updateAge(ageSum);
/* 6283 */             target.refreshVisible();
/*      */             
/* 6285 */             performer.getCommunicator().sendNormalServerMessage(target.getName() + " looks slightly more youthful as the clock is rolled back " + redux + " months for " + target
/* 6286 */                 .getHimHerItString() + ".");
/* 6287 */             performer.achievement(491);
/*      */           }
/* 6289 */           catch (IOException e) {
/*      */             
/* 6291 */             performer.getCommunicator().sendNormalServerMessage("Something went wrong when using the rune. Try again a bit later.");
/* 6292 */             return true;
/*      */           }
/*      */         
/* 6295 */         } else if (RuneUtilities.getSpellForRune(source) != null && RuneUtilities.getSpellForRune(source).isTargetCreature()) {
/*      */           
/* 6297 */           RuneUtilities.getSpellForRune(source).castSpell(50.0D, performer, target);
/* 6298 */           performer.achievement(491);
/*      */         }
/*      */         else {
/*      */           
/* 6302 */           performer.getCommunicator().sendNormalServerMessage("You can't use the rune on that.", (byte)3);
/* 6303 */           return true;
/*      */         } 
/* 6305 */         Server.getInstance().broadCastAction(performer
/* 6306 */             .getNameWithGenus() + " successfully uses " + source.getNameWithGenus() + " on " + targetName + ".", performer, 10);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 6311 */         performer.getCommunicator().sendNormalServerMessage("You try to use the rune on " + target.getName() + " but fail.", (byte)3);
/*      */         
/* 6313 */         Server.getInstance().broadCastAction(performer
/* 6314 */             .getNameWithGenus() + " attempts to use " + source.getNameWithGenus() + " on " + targetName + " but fails.", performer, 10);
/*      */       } 
/*      */       
/* 6317 */       if (Servers.isThisATestServer())
/* 6318 */         performer.getCommunicator().sendNormalServerMessage("Diff: " + diff + ", bonus: " + source.getCurrentQualityLevel() + ", sd: " + soulDepth
/* 6319 */             .getKnowledge() + ", power: " + power + ", chance: " + soulDepth.getChance(diff, null, source.getCurrentQualityLevel())); 
/* 6320 */       Items.destroyItem(source.getWurmId());
/* 6321 */       return true;
/*      */     } 
/* 6323 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static String[] getPlayfulReactionString() {
/* 6329 */     String[] preactions = { "squeals", "cries", "screams", "laughs", "squirms", "giggles", "coughs", "curses", "falls over", "glares" };
/* 6330 */     String[] treactions = { "squeal", "cry", "scream", "laugh", "squirm", "giggle", "cough", "curse", "fall over", "glare" };
/* 6331 */     int selection = Server.rand.nextInt(preactions.length);
/*      */     
/* 6333 */     String[] to_ret = { preactions[selection], treactions[selection] };
/* 6334 */     return to_ret;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\CreatureBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */