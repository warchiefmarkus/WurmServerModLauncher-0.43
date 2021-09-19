/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.AllianceQuestion;
/*      */ import com.wurmonline.server.questions.AltarConversionQuestion;
/*      */ import com.wurmonline.server.questions.AreaHistoryQuestion;
/*      */ import com.wurmonline.server.questions.CreateZoneQuestion;
/*      */ import com.wurmonline.server.questions.CreatureCreationQuestion;
/*      */ import com.wurmonline.server.questions.CreatureDataQuestion;
/*      */ import com.wurmonline.server.questions.DeclareWarQuestion;
/*      */ import com.wurmonline.server.questions.FriendQuestion;
/*      */ import com.wurmonline.server.questions.GMBuildAllWallsQuestion;
/*      */ import com.wurmonline.server.questions.GateManagementQuestion;
/*      */ import com.wurmonline.server.questions.GmSetEnchants;
/*      */ import com.wurmonline.server.questions.GmSetMedPath;
/*      */ import com.wurmonline.server.questions.GmSetTraits;
/*      */ import com.wurmonline.server.questions.GuardManagementQuestion;
/*      */ import com.wurmonline.server.questions.HideQuestion;
/*      */ import com.wurmonline.server.questions.ItemCreationQuestion;
/*      */ import com.wurmonline.server.questions.ItemDataQuestion;
/*      */ import com.wurmonline.server.questions.ItemRestrictionManagement;
/*      */ import com.wurmonline.server.questions.LearnSkillQuestion;
/*      */ import com.wurmonline.server.questions.ManageAllianceQuestion;
/*      */ import com.wurmonline.server.questions.NewKingQuestion;
/*      */ import com.wurmonline.server.questions.PaymentQuestion;
/*      */ import com.wurmonline.server.questions.PeaceQuestion;
/*      */ import com.wurmonline.server.questions.PlanBridgeQuestion;
/*      */ import com.wurmonline.server.questions.PlayerPaymentQuestion;
/*      */ import com.wurmonline.server.questions.PowerManagementQuestion;
/*      */ import com.wurmonline.server.questions.QuestionTypes;
/*      */ import com.wurmonline.server.questions.RealDeathQuestion;
/*      */ import com.wurmonline.server.questions.ReputationQuestion;
/*      */ import com.wurmonline.server.questions.ServerQuestion;
/*      */ import com.wurmonline.server.questions.SetDeityQuestion;
/*      */ import com.wurmonline.server.questions.ShutDownQuestion;
/*      */ import com.wurmonline.server.questions.SimplePopup;
/*      */ import com.wurmonline.server.questions.SinglePriceManageQuestion;
/*      */ import com.wurmonline.server.questions.TeleportQuestion;
/*      */ import com.wurmonline.server.questions.TerrainQuestion;
/*      */ import com.wurmonline.server.questions.TileDataQuestion;
/*      */ import com.wurmonline.server.questions.TwitSetupQuestion;
/*      */ import com.wurmonline.server.questions.VillageCitizenManageQuestion;
/*      */ import com.wurmonline.server.questions.VillageFoundationQuestion;
/*      */ import com.wurmonline.server.questions.VillageHistoryQuestion;
/*      */ import com.wurmonline.server.questions.VillageInfo;
/*      */ import com.wurmonline.server.questions.VillageJoinQuestion;
/*      */ import com.wurmonline.server.questions.VillageRolesManageQuestion;
/*      */ import com.wurmonline.server.questions.VillageSettingsManageQuestion;
/*      */ import com.wurmonline.server.questions.VillageUpkeep;
/*      */ import com.wurmonline.server.questions.VoiceChatQuestion;
/*      */ import com.wurmonline.server.questions.WithdrawMoneyQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.spells.Spell;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.GuardPlan;
/*      */ import com.wurmonline.server.villages.NoSuchRoleException;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageRole;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Methods
/*      */   implements MiscConstants, QuestionTypes, ItemTypes, CounterTypes, ItemMaterials, SoundNames, VillageStatus, TimeConstants, MonetaryConstants
/*      */ {
/*  221 */   private static final Logger logger = Logger.getLogger(Methods.class.getName());
/*      */   
/*  223 */   private static Creature jennElector = null;
/*  224 */   private static Creature hotsElector = null;
/*  225 */   private static Item molrStone = null;
/*  226 */   private static Set<Long> kingAspirants = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendTeleportQuestion(Creature performer, Item source) {
/*  239 */     TeleportQuestion dq = new TeleportQuestion(performer, "Teleportation coordinates", "Set coordinates: x, 0-" + ((1 << Constants.meshSize) - 1) + " y, 0-" + ((1 << Constants.meshSize) - 1) + " or provide a player name.", source.getWurmId());
/*  240 */     dq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendCreateQuestion(Creature performer, Item source) {
/*  246 */     ItemCreationQuestion dq = new ItemCreationQuestion(performer, "Create Item", "Create the item of your liking:", source.getWurmId());
/*  247 */     dq.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static void sendTerraformingQuestion(Creature performer, Item source, int tilex, int tiley) {
/*  252 */     TerrainQuestion dq = new TerrainQuestion(performer, source, tilex, tiley);
/*  253 */     dq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendWithdrawMoneyQuestion(Creature performer, Item token) {
/*  259 */     WithdrawMoneyQuestion dq = new WithdrawMoneyQuestion(performer, "Withdraw money", "Withdraw selected amount:", token.getWurmId());
/*  260 */     dq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendSummonQuestion(Creature performer, Item source, int tilex, int tiley, long structureId) {
/*  267 */     CreatureCreationQuestion cq = new CreatureCreationQuestion(performer, "Summon creature", "Summon the creature of your liking:", source.getWurmId(), tilex, tiley, performer.getLayer(), structureId);
/*  268 */     cq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendAltarConversion(Creature performer, Item altar, Deity deity) {
/*  274 */     AltarConversionQuestion cq = new AltarConversionQuestion(performer, "Inscription", "Ancient inscription:", altar.getWurmId(), deity);
/*  275 */     cq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendRealDeathQuestion(Creature performer, Item altar, Deity deity) {
/*  286 */     if (Players.getChampionsFromKingdom(performer.getKingdomId(), deity.getNumber()) < 1) {
/*      */       
/*  288 */       if (Players.getChampionsFromKingdom(performer.getKingdomId()) < 3) {
/*      */ 
/*      */         
/*  291 */         RealDeathQuestion cq = new RealDeathQuestion(performer, "Real death", "Offer to become a Champion:", altar.getWurmId(), deity);
/*  292 */         cq.sendQuestion();
/*      */       } else {
/*      */         
/*  295 */         performer.getCommunicator().sendNormalServerMessage("Your kingdom does not support more champions right now.");
/*      */       } 
/*      */     } else {
/*  298 */       performer.getCommunicator().sendNormalServerMessage(deity.name + " can not support another champion from your kingdom right now.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendFoundVillageQuestion(Creature performer, Item deed) {
/*  308 */     if (!performer.isOnSurface()) {
/*      */       
/*  310 */       performer.getCommunicator().sendSafeServerMessage("You cannot found a settlement here below the surface.");
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/*  318 */     VolaTile tile = performer.getCurrentTile();
/*  319 */     if (tile != null) {
/*      */       
/*  321 */       int tx = tile.tilex;
/*  322 */       int ty = tile.tiley;
/*  323 */       int tt = Server.surfaceMesh.getTile(tx, ty);
/*  324 */       if (Tiles.decodeType(tt) == Tiles.Tile.TILE_LAVA.id || Tiles.isMineDoor(Tiles.decodeType(tt))) {
/*      */         
/*  326 */         performer.getCommunicator().sendSafeServerMessage("You cannot found a settlement here.");
/*      */         return;
/*      */       } 
/*  329 */       for (int x = -1; x <= 1; x++) {
/*      */         
/*  331 */         for (int y = -1; y <= 1; y++) {
/*      */           
/*  333 */           int t = Server.surfaceMesh.getTile(tx + x, ty + y);
/*  334 */           if (Tiles.decodeHeight(t) < 0) {
/*      */             
/*  336 */             performer.getCommunicator().sendSafeServerMessage("You cannot found a settlement here. Too close to water.");
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } 
/*  343 */     } else if (tile == null) {
/*      */       
/*  345 */       performer.getCommunicator().sendSafeServerMessage("You cannot found a settlement here.");
/*  346 */       logger.log(Level.WARNING, performer.getName() + " no tile when founding deed.");
/*      */       
/*      */       return;
/*      */     } 
/*  350 */     Map<Village, String> decliners = Villages.canFoundVillage(5, 5, 5, 5, tile.tilex, tile.tiley, 0, true, null, performer);
/*      */     
/*  352 */     if (!decliners.isEmpty()) {
/*      */       
/*  354 */       performer.getCommunicator().sendSafeServerMessage("You cannot found the settlement here:");
/*  355 */       for (Iterator<Village> it = decliners.keySet().iterator(); it.hasNext(); ) {
/*      */         
/*  357 */         Village vill = it.next();
/*  358 */         String reason = decliners.get(vill);
/*  359 */         if (reason.startsWith("has perimeter")) {
/*  360 */           performer.getCommunicator().sendSafeServerMessage(vill.getName() + " " + reason); continue;
/*      */         } 
/*  362 */         performer.getCommunicator().sendSafeServerMessage("Some settlement nearby " + reason);
/*      */       } 
/*      */       return;
/*      */     } 
/*  366 */     if (deed.isNewDeed() || Servers.localServer.testServer || deed.getTemplateId() == 862) {
/*      */       
/*  368 */       Village village = performer.getCitizenVillage();
/*      */       
/*      */       try {
/*  371 */         if (village != null && village
/*  372 */           .getCitizen(performer.getWurmId()).getRole() == village.getRoleForStatus((byte)2)) {
/*  373 */           performer.getCommunicator().sendNormalServerMessage("You cannot found another settlement while being mayor in one. Give away one of the deeds.");
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/*  380 */           VillageFoundationQuestion vf = new VillageFoundationQuestion(performer, "Settlement Application Form", "Welcome to the Settlement Application Form", deed.getWurmId());
/*      */           
/*  382 */           if (vf != null)
/*      */           {
/*      */             
/*  385 */             float rot = Creature.normalizeAngle(performer.getStatus().getRotation() + 45.0F);
/*  386 */             vf.dir = (byte)((int)(rot / 90.0F) * 2);
/*  387 */             vf.sendIntro();
/*      */           }
/*      */         
/*      */         } 
/*  391 */       } catch (NoSuchRoleException nsr) {
/*      */         
/*  393 */         logger.log(Level.WARNING, nsr.getMessage(), (Throwable)nsr);
/*  394 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the mayor role for that request. Please contact administration.");
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
/*      */   static void sendManageVillageSettingsQuestion(Creature performer, @Nullable Item deed) {
/*  408 */     long wId = (deed == null) ? -10L : deed.getWurmId();
/*  409 */     VillageSettingsManageQuestion vs = new VillageSettingsManageQuestion(performer, "Manage settings", "Managing settings.", wId);
/*      */     
/*  411 */     if (vs != null) {
/*  412 */       vs.sendQuestion();
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
/*      */   static void sendManageVillageRolesQuestion(Creature performer, @Nullable Item deed) {
/*  424 */     long wId = (deed == null) ? -10L : deed.getWurmId();
/*  425 */     VillageRolesManageQuestion vs = new VillageRolesManageQuestion(performer, "Manage roles", "Managing roles and titles.", wId);
/*      */     
/*  427 */     if (vs != null) {
/*  428 */       vs.sendQuestion();
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
/*      */   static void sendManageVillageGatesQuestion(Creature performer, @Nullable Item deed) {
/*  455 */     long wId = (deed == null) ? -10L : deed.getWurmId();
/*  456 */     GateManagementQuestion vs = new GateManagementQuestion(performer, "Manage gates", "Managing gates.", wId);
/*      */     
/*  458 */     if (vs != null) {
/*  459 */       vs.sendQuestion();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendManageVillageGuardsQuestion(Creature performer, @Nullable Item deed) {
/*  470 */     long wId = (deed == null) ? -10L : deed.getWurmId();
/*  471 */     GuardManagementQuestion gm = new GuardManagementQuestion(performer, "Guard management", "Manage guards", wId);
/*      */     
/*  473 */     gm.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendManageVillageCitizensQuestion(Creature performer, @Nullable Item deed) {
/*  484 */     long wId = (deed == null) ? -10L : deed.getWurmId();
/*  485 */     VillageCitizenManageQuestion vc = new VillageCitizenManageQuestion(performer, "Citizen management", "Set statuses of citizens.", wId);
/*      */     
/*  487 */     vc.setSelecting(true);
/*  488 */     vc.sendQuestion();
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
/*      */   static void sendExpandVillageQuestion(Creature performer, @Nullable Item deed) {
/*      */     try {
/*      */       Village village;
/*  502 */       long dId = -10L;
/*  503 */       if (deed == null) {
/*      */         
/*  505 */         village = performer.getCitizenVillage();
/*  506 */         dId = village.getDeedId();
/*      */       }
/*      */       else {
/*      */         
/*  510 */         dId = deed.getWurmId();
/*  511 */         int oldVill = deed.getData2();
/*  512 */         village = Villages.getVillage(oldVill);
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  517 */         long coolDown = System.currentTimeMillis() - 3600000L;
/*  518 */         if (coolDown < village.getToken().getLastOwnerId()) {
/*      */           
/*  520 */           performer.getCommunicator().sendNormalServerMessage("The settlement has been attacked, or been under siege recently. You need to wait " + 
/*      */               
/*  522 */               Server.getTimeFor(village.getToken().getLastOwnerId() - coolDown) + ".");
/*      */           
/*      */           return;
/*      */         } 
/*  526 */       } catch (NoSuchItemException e) {
/*      */         
/*  528 */         e.printStackTrace();
/*      */       } 
/*  530 */       if (village.isDisbanding()) {
/*      */         
/*  532 */         performer.getCommunicator().sendNormalServerMessage("This settlement is disbanding. You can not change these settings now.");
/*      */         
/*      */         return;
/*      */       } 
/*  536 */       if (village.plan.isUnderSiege()) {
/*      */         
/*  538 */         performer.getCommunicator().sendNormalServerMessage("This settlement is under siege. You can not change these settings now.");
/*      */         
/*      */         return;
/*      */       } 
/*  542 */       if (village.isActionAllowed((short)76, performer)) {
/*      */ 
/*      */ 
/*      */         
/*  546 */         VillageFoundationQuestion vf = new VillageFoundationQuestion(performer, "Settlement Size", "Stage One - The size of your settlement", dId);
/*      */         
/*  548 */         if (vf != null)
/*      */         {
/*  550 */           vf.setSequence(1);
/*  551 */           vf.tokenx = village.getTokenX();
/*  552 */           vf.tokeny = village.getTokenY();
/*  553 */           vf.surfaced = village.isOnSurface();
/*  554 */           vf.initialPerimeter = village.getPerimeterSize();
/*  555 */           vf.democracy = village.isDemocracy();
/*  556 */           vf.spawnKingdom = village.kingdom;
/*  557 */           vf.motto = village.getMotto();
/*  558 */           vf.villageName = village.getName();
/*  559 */           vf.selectedWest = vf.tokenx - village.getStartX();
/*  560 */           vf.selectedEast = village.getEndX() - vf.tokenx;
/*  561 */           vf.selectedNorth = vf.tokeny - village.getStartY();
/*  562 */           vf.selectedSouth = village.getEndY() - vf.tokeny;
/*      */ 
/*      */ 
/*      */           
/*  566 */           float rot = Creature.normalizeAngle(performer.getStatus().getRotation() + 45.0F);
/*  567 */           vf.dir = (byte)((int)(rot / 90.0F) * 2);
/*      */ 
/*      */           
/*  570 */           vf.selectedGuards = village.plan.getNumHiredGuards();
/*  571 */           vf.setSize();
/*  572 */           vf.checkDeedItem();
/*  573 */           vf.expanding = true;
/*  574 */           vf.sendQuestion();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  579 */         performer
/*  580 */           .getCommunicator()
/*  581 */           .sendNormalServerMessage("You are not allowed to use this deed here. Ask the mayor to set the permissions on the management deed to allow you to expand.");
/*      */       }
/*      */     
/*      */     }
/*  585 */     catch (NoSuchVillageException nsv) {
/*      */       
/*  587 */       performer.getCommunicator().sendNormalServerMessage("Failed to localize the settlement for that deed.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void setVillageToken(Creature performer, Item token) {
/*  598 */     int tilex = performer.getTileX();
/*  599 */     int tiley = performer.getTileY();
/*  600 */     Village village = Zones.getVillage(tilex, tiley, performer.isOnSurface());
/*  601 */     if (village == null) {
/*  602 */       performer.getCommunicator().sendNormalServerMessage("No settlement here. You cannot plant the token.");
/*      */     } else {
/*      */       
/*  605 */       VolaTile tile = Zones.getTileOrNull(tilex, tiley, performer.isOnSurface());
/*  606 */       if (tile != null) {
/*      */         
/*  608 */         if (tile.getStructure() != null) {
/*      */           
/*  610 */           performer.getCommunicator().sendNormalServerMessage("You cannot plant the token inside.");
/*      */           return;
/*      */         } 
/*  613 */         Fence[] fences = tile.getFencesForLevel(0);
/*  614 */         if (fences.length > 0) {
/*      */           
/*  616 */           performer.getCommunicator().sendNormalServerMessage("You cannot plant the token on fences and walls.");
/*      */           return;
/*      */         } 
/*      */       } 
/*  620 */       Item oldToken = null;
/*      */       
/*      */       try {
/*  623 */         oldToken = village.getToken();
/*      */       }
/*  625 */       catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  631 */         village.setTokenId(token.getWurmId());
/*  632 */         token.setData2(village.getId());
/*  633 */         Item parent = token.getParent();
/*  634 */         parent.dropItem(token.getWurmId(), false);
/*      */         
/*      */         try {
/*  637 */           token.setPosXY(performer.getStatus().getPositionX(), performer.getStatus().getPositionY());
/*  638 */           Zone zone = Zones.getZone(tilex, tiley, performer.isOnSurface());
/*  639 */           zone.addItem(token);
/*      */         }
/*  641 */         catch (NoSuchZoneException nsz) {
/*      */           
/*  643 */           logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*  644 */           performer.getCommunicator().sendNormalServerMessage("You can't place the token here.");
/*      */           
/*      */           return;
/*      */         } 
/*  648 */       } catch (IOException iox) {
/*      */         
/*  650 */         logger.log(Level.WARNING, "Village: " + village
/*  651 */             .getId() + ", token: " + token.getWurmId() + " - " + iox.getMessage(), iox);
/*  652 */         performer.getCommunicator().sendNormalServerMessage("A server error occured. Please report this.");
/*      */         
/*      */         return;
/*  655 */       } catch (NoSuchItemException nsi) {
/*      */         
/*  657 */         logger.log(Level.WARNING, "Village: " + village
/*  658 */             .getId() + ", token: " + token.getWurmId() + " - " + nsi.getMessage(), (Throwable)nsi);
/*  659 */         performer.getCommunicator().sendNormalServerMessage("A server error occured. Please report this.");
/*      */         return;
/*      */       } 
/*  662 */       if (oldToken != null) {
/*  663 */         Items.destroyItem(oldToken.getWurmId());
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
/*      */   static final boolean drainCoffers(Creature performer, Village village, float counter, Item token, Action act) {
/*  678 */     boolean done = true;
/*  679 */     if (!performer.isFriendlyKingdom(village.kingdom) || village
/*  680 */       .isEnemy(performer.getCitizenVillage())) {
/*      */       
/*  682 */       if (village.guards != null && village.guards.size() > 0) {
/*      */         
/*  684 */         performer.getCommunicator().sendNormalServerMessage("The guards prevent you from draining the coffers.");
/*  685 */         done = true;
/*      */       }
/*      */       else {
/*      */         
/*  689 */         if (token.isOnSurface() != performer.isOnSurface()) {
/*      */           
/*  691 */           performer.getCommunicator().sendNormalServerMessage("You can't reach the " + token.getName() + " now.");
/*  692 */           return true;
/*      */         } 
/*  694 */         if (!performer.isWithinDistanceTo(token.getPosX(), token.getPosY(), token.getPosZ(), 2.0F)) {
/*      */           
/*  696 */           performer.getCommunicator().sendNormalServerMessage("You are too far away from the " + token.getName() + " to do that.");
/*  697 */           return true;
/*      */         } 
/*  699 */         if (token.getFloorLevel() != performer.getFloorLevel()) {
/*      */           
/*  701 */           performer.getCommunicator().sendNormalServerMessage("You must be on the same floor level as the " + token.getName() + ".");
/*  702 */           return true;
/*      */         } 
/*  704 */         GuardPlan plan = village.plan;
/*  705 */         if (plan != null) {
/*      */           
/*  707 */           long nextDrain = plan.getTimeToNextDrain();
/*  708 */           if (nextDrain < 0L) {
/*      */             
/*  710 */             long moneyDrained = plan.getMoneyDrained();
/*      */             
/*  712 */             if (moneyDrained > 1L) {
/*      */               
/*  714 */               done = false;
/*  715 */               boolean insta = (performer.getPower() >= 5);
/*  716 */               int time = 300;
/*  717 */               if (counter == 1.0F) {
/*      */                 
/*  719 */                 act.setTimeLeft(time);
/*  720 */                 performer.getCommunicator().sendNormalServerMessage("You start to search for gold in the coffers of " + village
/*  721 */                     .getName() + ".");
/*  722 */                 Server.getInstance().broadCastAction(performer
/*  723 */                     .getName() + " starts to rummage through the coffers of " + village.getName() + ", looking for coins.", performer, 5);
/*      */                 
/*  725 */                 performer.sendActionControl(Actions.actionEntrys[350]
/*  726 */                     .getVerbString(), true, time);
/*      */               }
/*      */               else {
/*      */                 
/*  730 */                 time = act.getTimeLeft();
/*      */               } 
/*  732 */               if (counter * 10.0F > time || insta) {
/*      */                 
/*  734 */                 done = true;
/*  735 */                 Change change = Economy.getEconomy().getChangeFor(moneyDrained / 2L);
/*      */                 
/*  737 */                 performer.getCommunicator().sendNormalServerMessage("You find " + change
/*  738 */                     .getChangeString() + " in the coffers of " + village.getName() + ".");
/*      */                 
/*  740 */                 Server.getInstance().broadCastAction(performer
/*  741 */                     .getName() + " proudly displays the " + change.getChangeString() + " " + performer
/*  742 */                     .getHeSheItString() + " found in the coffers of " + village
/*  743 */                     .getName() + ".", performer, 5);
/*  744 */                 Item[] coins = Economy.getEconomy().getCoinsFor(moneyDrained / 2L);
/*  745 */                 for (Item lCoin : coins)
/*  746 */                   performer.getInventory().insertItem(lCoin, true); 
/*  747 */                 plan.drainMoney();
/*  748 */                 performer.achievement(45);
/*  749 */                 village.addHistory(performer.getName(), "drained " + change.getChangeString() + " from the coffers.");
/*      */                 
/*  751 */                 if (village.plan != null && village.plan.getNumHiredGuards() >= 5)
/*      */                 {
/*  753 */                   if (performer.isChampion() && village.kingdom != performer.getKingdomId()) {
/*      */                     
/*  755 */                     performer.modifyChampionPoints(3);
/*  756 */                     Servers.localServer.createChampTwit(performer.getName() + " drains " + village
/*  757 */                         .getName() + " and gains 3 champion points");
/*      */                   } 
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/*  763 */                 boolean enemyHomeServer = (Servers.localServer.isChallengeOrEpicServer() && Servers.localServer.HOMESERVER && Servers.localServer.KINGDOM != performer.getKingdomId());
/*  764 */                 if (!Servers.localServer.HOMESERVER || enemyHomeServer)
/*      */                 {
/*  766 */                   MissionTriggers.activateTriggers(performer, -1, 350, token
/*  767 */                       .getWurmId(), 1);
/*      */                 }
/*      */               } 
/*      */             } else {
/*  771 */               performer.getCommunicator().sendNormalServerMessage("There is no money to steal in the coffers of " + village
/*  772 */                   .getName() + ".");
/*      */             } 
/*      */           } else {
/*  775 */             performer.getCommunicator().sendNormalServerMessage("The coffer timelock has been activated. Try again in " + 
/*  776 */                 Server.getTimeFor(nextDrain) + ".");
/*      */           } 
/*      */         } else {
/*  779 */           performer.getCommunicator().sendNormalServerMessage("The coffers of " + village
/*  780 */               .getName() + " echo hollowly.");
/*      */         } 
/*      */       } 
/*      */     } else {
/*  784 */       performer.getCommunicator().sendNormalServerMessage("You are not an enemy of " + village.getName() + ".");
/*  785 */     }  return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendVillageInfo(Creature performer, @Nullable Item villageToken) {
/*  795 */     long wId = (villageToken == null) ? -10L : villageToken.getWurmId();
/*  796 */     VillageInfo info = new VillageInfo(performer, "Settlement billboard", "", wId);
/*      */     
/*  798 */     info.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static void sendManageUpkeep(Creature performer, @Nullable Item villageToken) {
/*  803 */     long wId = (villageToken == null) ? -10L : villageToken.getWurmId();
/*  804 */     VillageUpkeep upkeep = new VillageUpkeep(performer, "Settlement Upkeep", "", wId);
/*      */     
/*  806 */     upkeep.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendVillageHistory(Creature performer, @Nullable Item villageToken) {
/*  816 */     long wId = (villageToken == null) ? -10L : villageToken.getWurmId();
/*  817 */     VillageHistoryQuestion info = new VillageHistoryQuestion(performer, "Settlement history", "", wId);
/*      */ 
/*      */     
/*  820 */     info.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendAreaHistory(Creature performer, @Nullable Item villageToken) {
/*  830 */     long wId = (villageToken == null) ? -10L : villageToken.getWurmId();
/*  831 */     AreaHistoryQuestion info = new AreaHistoryQuestion(performer, "Area history", "", wId);
/*      */     
/*  833 */     info.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sendJoinVillageQuestion(Creature performer, Creature invited) {
/*  843 */     if (performer.getKingdomId() == invited.getKingdomId()) {
/*      */       
/*  845 */       Village vill = invited.getCitizenVillage();
/*  846 */       if (vill != null) {
/*      */         
/*  848 */         Citizen citiz = vill.getCitizen(invited.getWurmId());
/*  849 */         VillageRole role = citiz.getRole();
/*  850 */         if (role.getStatus() == 2) {
/*      */           
/*  852 */           performer.getCommunicator().sendNormalServerMessage(invited
/*  853 */               .getName() + " is the mayor of " + vill.getName() + ". He can't join another settlement.");
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */       try {
/*  859 */         Village village = performer.getCitizenVillage();
/*  860 */         if (village != null && village.acceptsNewCitizens())
/*      */         {
/*  862 */           if (village.kingdom != 3 && invited.getReputation() < 0) {
/*      */             
/*  864 */             performer.getCommunicator().sendNormalServerMessage(invited
/*  865 */                 .getName() + " has negative reputation and may not join a settlement now.");
/*      */             
/*      */             return;
/*      */           } 
/*  869 */           VillageJoinQuestion vj = new VillageJoinQuestion(performer, "Settlement invitation", "Invitation to become citizen of a settlement.", invited.getWurmId());
/*  870 */           vj.sendQuestion();
/*      */           
/*  872 */           performer.getCommunicator().sendNormalServerMessage("You invite " + invited
/*  873 */               .getName() + " to join " + village.getName() + ".");
/*      */         }
/*      */         else
/*      */         {
/*  877 */           SimplePopup pp = new SimplePopup(performer, "Max citizens reached", "The settlement does not accept more citizens right now");
/*      */ 
/*      */           
/*  880 */           pp.setToSend("Every settlement has a maximum amount of citizens depending on their size. You may unlimit the amount of allowed citizens in the citizen management or settlement management forms. As long as " + village
/*  881 */               .getName() + " has more than " + village
/*      */               
/*  883 */               .getMaxCitizens() + " player citizens your upkeep is doubled.");
/*      */           
/*  885 */           pp.sendQuestion();
/*      */         }
/*      */       
/*  888 */       } catch (NoSuchCreatureException nsc) {
/*      */         
/*  890 */         logger.log(Level.WARNING, "Failed to locate creature " + invited.getName(), (Throwable)nsc);
/*  891 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that invitation.");
/*      */       }
/*  893 */       catch (NoSuchPlayerException nsp) {
/*      */         
/*  895 */         logger.log(Level.WARNING, "Failed to locate player " + invited.getName(), (Throwable)nsp);
/*  896 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the player for that invitation.");
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
/*      */   static void sendVillagePeaceQuestion(Creature performer, Creature invited) {
/*  908 */     Village vill = invited.getCitizenVillage();
/*  909 */     if (vill != null)
/*      */     {
/*  911 */       if (vill.mayDoDiplomacy(invited)) {
/*      */         
/*  913 */         Village village = performer.getCitizenVillage();
/*  914 */         if (village != null)
/*      */         {
/*  916 */           if (village.mayDoDiplomacy(performer)) {
/*      */             
/*      */             try
/*      */             {
/*      */               
/*  921 */               PeaceQuestion pq = new PeaceQuestion(performer, "Peace offer", "Will you accept peace?", invited.getWurmId());
/*  922 */               pq.sendQuestion();
/*      */             }
/*  924 */             catch (NoSuchCreatureException nsc)
/*      */             {
/*  926 */               logger.log(Level.WARNING, "Failed to locate creature " + invited.getName(), (Throwable)nsc);
/*  927 */               performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that invitation.");
/*      */             
/*      */             }
/*  930 */             catch (NoSuchPlayerException nsp)
/*      */             {
/*  932 */               logger.log(Level.WARNING, "Failed to locate player " + invited.getName(), (Throwable)nsp);
/*  933 */               performer.getCommunicator().sendNormalServerMessage("Failed to locate the player for that invitation.");
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  939 */             performer.getCommunicator().sendNormalServerMessage("You may not do diplomacy in the name of " + village
/*  940 */                 .getName() + ". You cannot offer peace.");
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/*      */       } else {
/*  947 */         performer.getCommunicator().sendNormalServerMessage(invited
/*  948 */             .getName() + " may not do diplomacy in the name of " + vill.getName() + ". He cannot give you peace.");
/*      */         return;
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
/*      */   static void sendWarDeclarationQuestion(Creature performer, Village targetVillage) {
/*  963 */     if (targetVillage != null) {
/*      */       
/*  965 */       Village village = performer.getCitizenVillage();
/*  966 */       if (village != null)
/*      */       {
/*  968 */         if (village.mayDoDiplomacy(performer)) {
/*      */           
/*      */           try
/*      */           {
/*      */             
/*  973 */             DeclareWarQuestion pq = new DeclareWarQuestion(performer, "War declaration", "Will you declare war?", targetVillage.getId());
/*  974 */             pq.sendQuestion();
/*      */           }
/*  976 */           catch (NoSuchCreatureException nsc)
/*      */           {
/*  978 */             logger.log(Level.WARNING, "Failed to locate creature " + performer.getName(), (Throwable)nsc);
/*  979 */             performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that declaration.");
/*      */           
/*      */           }
/*  982 */           catch (NoSuchPlayerException nsp)
/*      */           {
/*  984 */             logger.log(Level.WARNING, "Failed to locate player " + performer.getName(), (Throwable)nsp);
/*  985 */             performer.getCommunicator()
/*  986 */               .sendNormalServerMessage("Failed to locate the player for that declaration.");
/*      */           }
/*  988 */           catch (NoSuchVillageException nsp)
/*      */           {
/*  990 */             logger.log(Level.WARNING, "Failed to locate village " + targetVillage.getName(), (Throwable)nsp);
/*  991 */             performer.getCommunicator().sendNormalServerMessage("Failed to locate the village for that declaration.");
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  997 */           performer.getCommunicator().sendNormalServerMessage("You may not do diplomacy in the name of " + village
/*  998 */               .getName() + ". You cannot declare war.");
/*      */           return;
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
/*      */   static void sendShutdownQuestion(Creature performer, Item wand) {
/* 1013 */     ShutDownQuestion vs = new ShutDownQuestion(performer, "Shutting down the server", "Select the number of minutes and seconds to shutdown as well as the reason for it.", wand.getWurmId());
/* 1014 */     vs.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendHideQuestion(Creature performer, Item wand, Item target) {
/* 1025 */     HideQuestion hs = new HideQuestion(performer, "Hiding " + target.getName(), "Do you wish to hide the " + target.getName() + "?", target.getWurmId());
/* 1026 */     hs.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendPaymentQuestion(Creature performer, Item wand) {
/* 1037 */     PaymentQuestion vs = new PaymentQuestion(performer, "Setting payment expiretime for a player.", "Select the number of days and months before the subscription expires.", wand.getWurmId());
/* 1038 */     vs.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendPlayerPaymentQuestion(Creature performer) {
/* 1047 */     PlayerPaymentQuestion vs = new PlayerPaymentQuestion(performer);
/* 1048 */     vs.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendPowerManagementQuestion(Creature performer, Item wand) {
/* 1059 */     PowerManagementQuestion vs = new PowerManagementQuestion(performer, "Setting the power status for a player.", "Set the power of the player to the selected level.", wand.getWurmId());
/* 1060 */     vs.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendFaithManagementQuestion(Creature performer, Item wand) {
/* 1071 */     SetDeityQuestion dq = new SetDeityQuestion(performer, "Setting the deity for a player.", "Set the deity of the player.", wand.getWurmId());
/* 1072 */     dq.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static void sendConfigureTwitter(Creature performer, long target, boolean village, String name) {
/* 1077 */     TwitSetupQuestion twq = new TwitSetupQuestion(performer, "Twitter", "Configure Twitter for " + name, target, village);
/*      */     
/* 1079 */     twq.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static void sendCreateZone(Creature performer) {
/* 1084 */     CreateZoneQuestion twq = new CreateZoneQuestion(performer);
/* 1085 */     twq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendServerManagementQuestion(Creature performer, long target) {
/* 1095 */     ServerQuestion dq = new ServerQuestion(performer, "Wurm servers.", "Wurm servers management", target);
/* 1096 */     dq.sendQuestion();
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
/*      */   static void sendTileDataQuestion(Creature performer, Item wand, int tilex, int tiley) {
/* 1109 */     TileDataQuestion dq = new TileDataQuestion(performer, "Setting data for tile at " + tilex + ", " + tiley, "Set the data:", tilex, tiley, wand.getWurmId());
/* 1110 */     dq.sendQuestion();
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
/*      */   static void sendLearnSkillQuestion(Creature performer, Item wand, long target) {
/* 1123 */     Creature creature = null;
/* 1124 */     if (WurmId.getType(target) == 1 || WurmId.getType(target) == 0) {
/*      */       
/*      */       try {
/*      */         
/* 1128 */         creature = Server.getInstance().getCreature(target);
/*      */       }
/* 1130 */       catch (NoSuchCreatureException nsc) {
/*      */         
/* 1132 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the creature for that request!");
/*      */       }
/* 1134 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 1136 */         performer.getCommunicator().sendNormalServerMessage("Failed to locate the player for that request!");
/*      */       } 
/*      */     }
/* 1139 */     LearnSkillQuestion ls = null;
/* 1140 */     if (creature != null) {
/* 1141 */       ls = new LearnSkillQuestion(performer, "Imbue with skill", "Set the skill of " + creature.getName() + " to the value of your choice:", target);
/*      */     } else {
/*      */       
/* 1144 */       ls = new LearnSkillQuestion(performer, "Set your skill", "Set or learn a skill:", target);
/* 1145 */     }  ls.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void sendAllianceQuestion(Creature performer, Creature target) {
/* 1155 */     if (!target.isFighting() && target.hasLink()) {
/*      */ 
/*      */       
/* 1158 */       AllianceQuestion aq = new AllianceQuestion(target, "Alliance invitation", "Request to form a village alliance:", performer.getWurmId());
/* 1159 */       aq.sendQuestion();
/* 1160 */       performer.getCommunicator().sendNormalServerMessage("You send an elaborate invitation to form a high and mighty alliance to " + target
/* 1161 */           .getName() + ".");
/*      */     } else {
/*      */       
/* 1164 */       performer.getCommunicator().sendNormalServerMessage(target.getName() + " does not answer questions right now.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void sendFriendQuestion(Creature performer, Creature target) {
/* 1174 */     if (performer.getKingdomId() == target.getKingdomId())
/*      */     {
/* 1176 */       if (!target.isFighting() && target.hasLink()) {
/*      */ 
/*      */         
/* 1179 */         FriendQuestion fq = new FriendQuestion(target, "Friend list invitation", "Request to add you to the friend list:", performer.getWurmId());
/* 1180 */         fq.sendQuestion();
/* 1181 */         performer.getCommunicator().sendNormalServerMessage("You ask " + target
/* 1182 */             .getName() + " for permission to add " + target.getHimHerItString() + " to your friends list.");
/*      */       }
/*      */       else {
/*      */         
/* 1186 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " does not answer questions right now.");
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
/*      */   static final void sendManageAllianceQuestion(Creature performer, @Nullable Item villageToken) {
/* 1198 */     long wId = (villageToken == null) ? -10L : villageToken.getWurmId();
/* 1199 */     ManageAllianceQuestion aq = new ManageAllianceQuestion(performer, "Manage alliances", "Select an alliance to break:", wId);
/*      */     
/* 1201 */     aq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void sendSinglePriceQuestion(Creature responder, Item target) {
/* 1212 */     SinglePriceManageQuestion spm = new SinglePriceManageQuestion(responder, "Price management", "Set the desired price:", target.getWurmId());
/* 1213 */     spm.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void sendSetDataQuestion(Creature responder, Item target) {
/* 1223 */     ItemDataQuestion spm = new ItemDataQuestion(responder, "Item data", "Set the desired data:", target.getWurmId());
/* 1224 */     spm.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendSetDataQuestion(Creature responder, Creature target) {
/* 1229 */     CreatureDataQuestion spm = new CreatureDataQuestion(responder, target);
/* 1230 */     spm.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendItemRestrictionManagement(Creature responder, Permissions.IAllow target, long wurmId) {
/* 1235 */     ItemRestrictionManagement irm = new ItemRestrictionManagement(responder, target, wurmId);
/* 1236 */     irm.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void sendReputationManageQuestion(Creature responder, @Nullable Item target) {
/* 1247 */     long wId = (target == null) ? -10L : target.getWurmId();
/* 1248 */     ReputationQuestion spm = new ReputationQuestion(responder, "Reputation management", "Set the reputation levels:", wId);
/*      */     
/* 1250 */     spm.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean discardSellItem(Creature performer, Action act, Item discardItem, float counter) {
/* 1255 */     boolean toReturn = false;
/* 1256 */     String message = "That item cannot be sold this way.";
/* 1257 */     if (discardItem.isNoDiscard() || discardItem.isTemporary()) {
/*      */       
/* 1259 */       if (act.getNumber() == 600) {
/* 1260 */         message = "That item cannot be discarded.";
/*      */       }
/* 1262 */       performer.getCommunicator().sendNormalServerMessage(message);
/* 1263 */       return true;
/*      */     } 
/* 1265 */     if (discardItem.getOwnerId() != performer.getWurmId()) {
/*      */       
/* 1267 */       performer.getCommunicator().sendNormalServerMessage("You need to carry the item in order to sell it.");
/* 1268 */       return true;
/*      */     } 
/*      */     
/* 1271 */     if (discardItem.isInstaDiscard()) {
/*      */       
/* 1273 */       if (act.getNumber() == 600) {
/*      */         
/* 1275 */         performer.getCommunicator().sendNormalServerMessage("You break it down in little pieces and throw it away.");
/* 1276 */         Items.destroyItem(discardItem.getWurmId());
/* 1277 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1281 */       performer.getCommunicator().sendNormalServerMessage(message);
/* 1282 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1286 */     if (performer.getMoneyEarnedBySellingLastHour() > 500L) {
/*      */       
/* 1288 */       performer.getCommunicator().sendNormalServerMessage("You have sold your quota for now.");
/* 1289 */       return true;
/*      */     } 
/* 1291 */     if (counter == 1.0F) {
/*      */       
/* 1293 */       Shop kingsShop = Economy.getEconomy().getKingsShop();
/* 1294 */       if (kingsShop.getMoney() > 100000L) {
/*      */         
/* 1296 */         int time = 30;
/* 1297 */         performer.sendActionControl("Selling", true, 30);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1302 */         performer.getCommunicator().sendNormalServerMessage("There are apparently no coins in the coffers at the moment.");
/*      */         
/* 1304 */         return true;
/*      */       } 
/* 1306 */       if (Constants.maintaining) {
/*      */         
/* 1308 */         performer.getCommunicator().sendNormalServerMessage("The server is shutting down so the shop is closed for now.");
/*      */         
/* 1310 */         return true;
/*      */       } 
/*      */     } 
/* 1313 */     if (counter > 3.0F) {
/*      */       
/* 1315 */       toReturn = true;
/* 1316 */       Shop kingsShop = Economy.getEconomy().getKingsShop();
/* 1317 */       if (kingsShop.getMoney() > 100000L) {
/*      */         
/* 1319 */         long percentMod = 0L;
/* 1320 */         if (!Servers.localServer.HOMESERVER) {
/*      */           
/* 1322 */           if (Server.rand.nextFloat() < Zones.getPercentLandForKingdom(performer.getKingdomId()) / 100.0F)
/* 1323 */             percentMod = 1L; 
/* 1324 */           if (Server.rand.nextInt(10) < Items.getBattleCampControl(performer.getKingdomId()))
/* 1325 */             percentMod++; 
/*      */         } 
/* 1327 */         long value = (long)discardItem.getCurrentQualityLevel() / 10L + 1L + percentMod;
/* 1328 */         performer.addMoneyEarnedBySellingLastHour(value);
/* 1329 */         kingsShop.setMoney(kingsShop.getMoney() - value);
/* 1330 */         Items.destroyItem(discardItem.getWurmId());
/* 1331 */         if (performer.checkCoinAward(1000)) {
/* 1332 */           performer.getCommunicator().sendSafeServerMessage("The king awards you with a rare coin!");
/*      */         }
/*      */       } else {
/*      */         
/* 1336 */         performer.getCommunicator().sendNormalServerMessage("There are apparently no coins in the coffers at the moment.");
/*      */         
/* 1338 */         return true;
/*      */       } 
/*      */     } 
/* 1341 */     return toReturn;
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
/*      */   static final boolean disbandVillage(Creature performer, Item villageToken, float counter) {
/* 1353 */     boolean insta = false;
/* 1354 */     boolean toReturn = true;
/* 1355 */     boolean settings = false;
/* 1356 */     if (performer.getPower() > 3) {
/* 1357 */       insta = true;
/*      */     }
/*      */     try {
/* 1360 */       Village vill = Villages.getVillage(villageToken.getData2());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1368 */       if ((vill == performer.getCitizenVillage() && vill
/* 1369 */         .isActionAllowed((short)348, performer)) || insta)
/*      */       {
/*      */         
/* 1372 */         settings = true;
/*      */       }
/* 1374 */       if (!vill.isDisbanding()) {
/*      */         
/* 1376 */         if (settings) {
/*      */           
/* 1378 */           toReturn = false;
/* 1379 */           if (counter == 1.0F)
/*      */           {
/* 1381 */             if (!insta) {
/*      */               
/* 1383 */               int time = 3000;
/* 1384 */               performer.sendActionControl(Actions.actionEntrys[348]
/* 1385 */                   .getVerbString(), true, 3000);
/* 1386 */               performer.getCommunicator().sendNormalServerMessage("You start to disband the village of " + vill
/* 1387 */                   .getName() + ".");
/* 1388 */               Server.getInstance().broadCastAction(performer
/* 1389 */                   .getName() + " starts to disband the village of " + vill.getName() + ".", performer, 5);
/*      */               
/* 1391 */               vill.broadCastMessage(new Message(performer, (byte)3, "Village", "<" + performer.getName() + "> starts to disband the village of " + vill
/* 1392 */                     .getName() + "."));
/* 1393 */               vill.broadCastAlert("Any traders who are citizens of " + vill.getName() + " will disband without refund!");
/*      */             } 
/*      */           }
/*      */           
/* 1397 */           if (counter > 300.0F || insta || vill.getMayor().getId() == performer.getWurmId()) {
/*      */             
/* 1399 */             toReturn = true;
/* 1400 */             if (insta) {
/*      */               
/* 1402 */               vill.disband(performer.getName());
/*      */             }
/*      */             else {
/*      */               
/* 1406 */               vill.startDisbanding(performer, performer.getName(), performer.getWurmId());
/* 1407 */               if (vill.getMayor().getId() == performer.getWurmId() && vill
/* 1408 */                 .getDiameterX() < 30 && vill.getDiameterY() < 30)
/*      */               {
/* 1410 */                 performer.getCommunicator().sendNormalServerMessage("Your settlement is disbanding. It will be disbanded in about an hour.");
/*      */                 
/* 1412 */                 if (!Servers.localServer.isFreeDeeds() || Servers.localServer.isUpkeep())
/*      */                 {
/* 1414 */                   performer
/* 1415 */                     .getCommunicator()
/* 1416 */                     .sendAlertServerMessage("Do not change server during this process. You may not receive the money from the coffers in that case.");
/*      */                 }
/*      */                 
/* 1419 */                 if (Servers.localServer.isFreeDeeds() && Servers.localServer.isUpkeep() && vill
/* 1420 */                   .getCreationDate() < System.currentTimeMillis() + 2419200000L)
/*      */                 {
/* 1422 */                   performer.getCommunicator().sendAlertServerMessage("Free deeding is enabled and your settlement is less than 30 days old. If you disband now, you will not receive a refund.");
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/* 1427 */                 Server.getInstance().broadCastAction(performer
/* 1428 */                     .getName() + " has set " + vill.getName() + " to disband immediately.", performer, 5);
/*      */                 
/* 1430 */                 vill.broadCastMessage(new Message(performer, (byte)3, "Village", "<" + performer
/* 1431 */                       .getName() + "> has set " + vill.getName() + " to disband immediately."));
/* 1432 */                 Village[] allies = vill.getAllies();
/* 1433 */                 for (Village lAllie : allies)
/*      */                 {
/* 1435 */                   lAllie.broadCastMessage(new Message(performer, (byte)3, "Village", "<" + performer
/* 1436 */                         .getName() + "> has set " + vill.getName() + " to disband immediately."));
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 1441 */                 String hours = "24 hours";
/* 1442 */                 performer.getCommunicator().sendNormalServerMessage(vill
/* 1443 */                     .getName() + " will disband in " + "24 hours" + ".");
/* 1444 */                 if (Servers.localServer.isUpkeep() || !Servers.localServer.isFreeDeeds())
/*      */                 {
/* 1446 */                   performer
/* 1447 */                     .getCommunicator()
/* 1448 */                     .sendNormalServerMessage("If the mayor is still on the same server when the deed disbands he or she should receive part of the money that is left in the coffers.");
/*      */                 }
/*      */                 
/* 1451 */                 Server.getInstance().broadCastAction(performer
/* 1452 */                     .getName() + " has set " + vill.getName() + " to disband in " + "24 hours" + ".", performer, 5);
/*      */                 
/* 1454 */                 vill.broadCastMessage(new Message(performer, (byte)3, "Village", "<" + performer
/* 1455 */                       .getName() + "> has set " + vill.getName() + " to disband in " + "24 hours" + "."));
/* 1456 */                 Village[] allies = vill.getAllies();
/* 1457 */                 for (Village lAllie : allies)
/*      */                 {
/* 1459 */                   lAllie.broadCastMessage(new Message(performer, (byte)3, "Village", "<" + performer
/* 1460 */                         .getName() + "> has set " + vill.getName() + " to disband in " + "24 hours" + "."));
/*      */                 }
/*      */               }
/*      */             
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/* 1468 */           performer.getCommunicator().sendNormalServerMessage(vill.getName() + " may not be disbanded right now.");
/*      */         } 
/*      */       } else {
/* 1471 */         performer.getCommunicator().sendNormalServerMessage(vill.getName() + " is already disbanding.");
/*      */       } 
/* 1473 */     } catch (NoSuchVillageException nsv) {
/*      */       
/* 1475 */       performer.getCommunicator().sendAlertServerMessage("No village found for that request.");
/* 1476 */       toReturn = true;
/*      */     } 
/* 1478 */     return toReturn;
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
/*      */   static final boolean preventDisbandVillage(Creature performer, Item villageToken, float counter) {
/* 1490 */     boolean insta = false;
/* 1491 */     boolean toReturn = false;
/* 1492 */     if (performer.getPower() > 3) {
/* 1493 */       insta = true;
/*      */     }
/*      */     try {
/* 1496 */       Village vill = Villages.getVillage(villageToken.getData2());
/* 1497 */       if (vill.isDisbanding()) {
/*      */         
/* 1499 */         if (counter == 1.0F) {
/*      */           
/* 1501 */           Village citizVill = performer.getCitizenVillage();
/* 1502 */           if (citizVill != null && citizVill.equals(vill)) {
/*      */             
/* 1504 */             int time = 300;
/* 1505 */             performer.sendActionControl(Actions.actionEntrys[349]
/* 1506 */                 .getVerbString(), true, 300);
/* 1507 */             performer.getCommunicator().sendNormalServerMessage("You start to salvage the settlement of " + vill
/* 1508 */                 .getName() + ".");
/* 1509 */             Server.getInstance()
/* 1510 */               .broadCastAction(performer
/* 1511 */                 .getName() + " starts to salvage the settlement of " + vill.getName() + ".", performer, 5);
/*      */             
/* 1513 */             vill.broadCastMessage(new Message(performer, (byte)3, "Village", "<" + performer.getName() + "> starts to salvage the settlement of " + vill
/* 1514 */                   .getName() + "."));
/*      */             
/*      */             try {
/* 1517 */               Player player = Players.getInstance().getPlayer(vill.getDisbander());
/* 1518 */               player.getCommunicator().sendAlertServerMessage(performer
/* 1519 */                   .getName() + " is trying to salvage the settlement of " + vill.getName() + "!");
/*      */             }
/* 1521 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 1528 */             if (!insta)
/* 1529 */               performer.getCommunicator().sendNormalServerMessage("You need to be citizen to salvage the settlement of " + vill
/* 1530 */                   .getName() + "."); 
/* 1531 */             toReturn = true;
/*      */           } 
/*      */         } 
/* 1534 */         if (counter > 30.0F || insta) {
/*      */           
/* 1536 */           toReturn = true;
/*      */ 
/*      */           
/*      */           try {
/*      */             try {
/* 1541 */               Player player = Players.getInstance().getPlayer(vill.getDisbander());
/* 1542 */               player.getCommunicator().sendAlertServerMessage(performer
/* 1543 */                   .getName() + " successfully salvaged the settlement of " + vill.getName() + "!");
/*      */             }
/* 1545 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */             
/* 1549 */             vill.setDisbandTime(0L);
/* 1550 */             vill.setDisbander(-10L);
/* 1551 */             performer.getCommunicator().sendNormalServerMessage(vill.getName() + " is salvaged for now.");
/* 1552 */             Server.getInstance().broadCastAction(performer.getName() + " has salvaged " + vill.getName() + ".", performer, 5);
/*      */             
/* 1554 */             vill.broadCastMessage(new Message(performer, (byte)3, "Village", "<" + performer.getName() + "> has salvaged the settlement of " + vill
/* 1555 */                   .getName() + "."));
/*      */           
/*      */           }
/* 1558 */           catch (IOException iox) {
/*      */             
/* 1560 */             logger.log(Level.WARNING, performer.getName() + " " + iox.getMessage(), iox);
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 1566 */         toReturn = true;
/* 1567 */         performer.getCommunicator().sendNormalServerMessage(vill.getName() + " does not need salvaging right now.");
/*      */       }
/*      */     
/* 1570 */     } catch (NoSuchVillageException nsv) {
/*      */       
/* 1572 */       performer.getCommunicator().sendAlertServerMessage("No settlement found for that request.");
/* 1573 */       toReturn = true;
/*      */     } 
/* 1575 */     return toReturn;
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
/*      */   public static final String getTimeString(long timeleft) {
/* 1587 */     String times = "";
/* 1588 */     if (timeleft < 60000L) {
/*      */       
/* 1590 */       long secs = timeleft / 1000L;
/* 1591 */       times = times + secs + " seconds";
/*      */     }
/*      */     else {
/*      */       
/* 1595 */       long daysleft = timeleft / 86400000L;
/* 1596 */       long hoursleft = (timeleft - daysleft * 86400000L) / 3600000L;
/* 1597 */       long minutesleft = (timeleft - daysleft * 86400000L - hoursleft * 3600000L) / 60000L;
/*      */       
/* 1599 */       if (daysleft > 0L)
/* 1600 */         times = times + daysleft + " days"; 
/* 1601 */       if (hoursleft > 0L) {
/*      */         
/* 1603 */         String aft = "";
/* 1604 */         if (daysleft > 0L && minutesleft > 0L) {
/*      */           
/* 1606 */           times = times + ", ";
/* 1607 */           aft = aft + " and ";
/*      */         }
/* 1609 */         else if (daysleft > 0L) {
/*      */           
/* 1611 */           times = times + " and ";
/*      */         }
/* 1613 */         else if (minutesleft > 0L) {
/* 1614 */           aft = aft + " and ";
/* 1615 */         }  times = times + hoursleft + " hours" + aft;
/*      */       } 
/* 1617 */       if (minutesleft > 0L) {
/*      */         
/* 1619 */         String aft = "";
/* 1620 */         if (daysleft > 0L && hoursleft == 0L)
/* 1621 */           aft = " and "; 
/* 1622 */         times = times + aft + minutesleft + " minutes";
/*      */       } 
/*      */     } 
/* 1625 */     if (times.length() == 0)
/* 1626 */       times = "nothing"; 
/* 1627 */     return times;
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
/*      */   static boolean castSpell(Creature performer, Spell spell, Item item, float counter) {
/* 1640 */     return spell.run(performer, item, counter);
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
/*      */   static boolean castSpell(Creature performer, Spell spell, Creature target, float counter) {
/* 1653 */     return spell.run(performer, target, counter);
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
/*      */   static boolean castSpell(Creature performer, Spell spell, int tilex, int tiley, int layer, int heightOffset, Tiles.TileBorderDirection dir, float counter) {
/* 1667 */     return spell.run(performer, tilex, tiley, layer, heightOffset, dir, counter);
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
/*      */   static boolean castSpell(Creature performer, Spell spell, Wound target, float counter) {
/* 1680 */     return spell.run(performer, target, counter);
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
/*      */   static boolean castSpell(Creature performer, Spell spell, int tilex, int tiley, int layer, int heightOffset, float counter) {
/* 1696 */     return spell.run(performer, tilex, tiley, layer, heightOffset, counter);
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
/*      */   public static void sendSound(Creature performer, String soundId) {
/* 1708 */     if (soundId.length() > 0)
/*      */     {
/* 1710 */       SoundPlayer.playSound(soundId, performer, 1.6F);
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
/*      */   static boolean transferPlayer(Creature performer, Creature target, Action act, float counter) {
/* 1725 */     boolean done = false;
/* 1726 */     int action = act.getNumber();
/* 1727 */     if (performer.getPower() < 2) {
/* 1728 */       return true;
/*      */     }
/*      */     
/* 1731 */     ServerEntry targetserver = Servers.localServer;
/*      */     
/* 1733 */     if (action == 241) {
/*      */       
/* 1735 */       if (Servers.localServer.serverEast == null) {
/*      */         
/* 1737 */         performer.getCommunicator().sendNormalServerMessage("No server east of here.");
/* 1738 */         done = true;
/*      */       } else {
/*      */         
/* 1741 */         targetserver = Servers.localServer.serverEast;
/*      */       } 
/* 1743 */     } else if (action == 240) {
/*      */       
/* 1745 */       if (Servers.localServer.serverNorth == null) {
/*      */         
/* 1747 */         performer.getCommunicator().sendNormalServerMessage("No server north of here. Using entryserver if one is available.");
/*      */         
/* 1749 */         if (Servers.loginServer.entryServer) {
/* 1750 */           targetserver = Servers.loginServer;
/*      */         } else {
/* 1752 */           targetserver = Servers.getEntryServer();
/* 1753 */         }  if (targetserver == null) {
/*      */           
/* 1755 */           performer.getCommunicator().sendNormalServerMessage("No entryserver was found. Nothing happens.");
/* 1756 */           done = true;
/*      */         }
/* 1758 */         else if (targetserver.id == Servers.localServer.id) {
/*      */           
/* 1760 */           performer.getCommunicator().sendNormalServerMessage("This option leads back here. Nothing happens.");
/* 1761 */           done = true;
/*      */         } 
/*      */       } else {
/*      */         
/* 1765 */         targetserver = Servers.localServer.serverNorth;
/*      */       } 
/* 1767 */     } else if (action == 242) {
/*      */       
/* 1769 */       if (Servers.localServer.serverSouth == null) {
/*      */         
/* 1771 */         performer.getCommunicator().sendNormalServerMessage("No server south of here.");
/* 1772 */         done = true;
/*      */       } else {
/*      */         
/* 1775 */         targetserver = Servers.localServer.serverSouth;
/*      */       } 
/* 1777 */     } else if (action == 243) {
/*      */       
/* 1779 */       if (Servers.localServer.serverWest == null) {
/*      */         
/* 1781 */         performer.getCommunicator().sendNormalServerMessage("No server west of here.");
/* 1782 */         done = true;
/*      */       } else {
/*      */         
/* 1785 */         targetserver = Servers.localServer.serverWest;
/*      */       } 
/*      */     } 
/* 1788 */     if (!done)
/*      */     {
/* 1790 */       if (counter == 1.0F) {
/*      */         
/* 1792 */         if (!targetserver.isAvailable(5, true)) {
/*      */           
/* 1794 */           target.getCommunicator().sendNormalServerMessage(targetserver.name + " is no longer available.");
/* 1795 */           return true;
/*      */         } 
/*      */ 
/*      */         
/* 1799 */         target.getCommunicator().sendNormalServerMessage("You transfer to " + targetserver.name + ".");
/* 1800 */         Server.getInstance().broadCastAction(target.getName() + " transfers to " + targetserver.name + ".", target, 5);
/*      */         
/* 1802 */         int tilex = targetserver.SPAWNPOINTJENNX;
/* 1803 */         int tiley = targetserver.SPAWNPOINTJENNY;
/* 1804 */         if (target.getKingdomId() == 1) {
/*      */           
/* 1806 */           tilex = targetserver.SPAWNPOINTJENNX;
/* 1807 */           tiley = targetserver.SPAWNPOINTJENNY;
/*      */         }
/* 1809 */         else if (target.getKingdomId() == 3) {
/*      */           
/* 1811 */           tilex = targetserver.SPAWNPOINTLIBX;
/* 1812 */           tiley = targetserver.SPAWNPOINTLIBY;
/*      */         }
/* 1814 */         else if (target.getKingdomId() == 2) {
/*      */           
/* 1816 */           tilex = targetserver.SPAWNPOINTMOLX;
/* 1817 */           tiley = targetserver.SPAWNPOINTMOLY;
/*      */         } 
/* 1819 */         ((Player)target).sendTransfer(Server.getInstance(), targetserver.INTRASERVERADDRESS, 
/* 1820 */             Integer.parseInt(targetserver.INTRASERVERPORT), targetserver.INTRASERVERPASSWORD, targetserver.id, tilex, tiley, true, false, target
/* 1821 */             .getKingdomId());
/* 1822 */         ((Player)target).transferCounter = 30;
/* 1823 */         if (!target.equals(performer))
/*      */         {
/* 1825 */           performer.getLogger().log(Level.INFO, performer
/* 1826 */               .getName() + " transferred " + target.getName() + " to " + targetserver.name + ".");
/*      */         }
/* 1828 */         done = true;
/*      */ 
/*      */       
/*      */       }
/* 1832 */       else if (!target.hasLink()) {
/* 1833 */         done = true;
/*      */       } 
/*      */     }
/*      */     
/* 1837 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void resetAspirants() {
/* 1842 */     kingAspirants.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean hasAspiredKing(Creature performer) {
/* 1847 */     return kingAspirants.contains(Long.valueOf(performer.getWurmId()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void setAspiredKing(Creature performer) {
/* 1852 */     kingAspirants.add(Long.valueOf(performer.getWurmId()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean aspireKing(Creature performer, byte kingdom, @Nullable Item item, Creature elector, Action act, float counter) {
/* 1858 */     boolean done = false;
/* 1859 */     King current = King.getKing(kingdom);
/* 1860 */     if (performer.isChampion()) {
/* 1861 */       performer.getCommunicator().sendAlertServerMessage("Champions are not able to rule kingdoms.");
/* 1862 */       done = true;
/*      */     } 
/* 1864 */     if (performer.getKingdomId() != kingdom) {
/*      */       
/* 1866 */       performer.getCommunicator().sendNormalServerMessage("You may not aspire to become the " + 
/* 1867 */           King.getRulerTitle((performer.getSex() == 0), kingdom) + " of " + 
/* 1868 */           Kingdoms.getNameFor(kingdom) + ".");
/* 1869 */       done = true;
/*      */     }
/* 1871 */     else if (current != null) {
/*      */       
/* 1873 */       performer.getCommunicator().sendNormalServerMessage("There is already a " + current
/* 1874 */           .getRulerTitle() + " of " + Kingdoms.getNameFor(kingdom) + "!");
/* 1875 */       done = true;
/*      */     } 
/* 1877 */     if (hasAspiredKing(performer) || performer.getPower() > 0)
/*      */     {
/* 1879 */       if (performer.getPower() < 5) {
/*      */         
/* 1881 */         performer.getCommunicator().sendNormalServerMessage("You are not eligible to take the test right now.");
/* 1882 */         done = true;
/*      */       } 
/*      */     }
/* 1885 */     if (counter == 1.0F) {
/*      */       
/* 1887 */       if (kingdom == 1)
/*      */       {
/* 1889 */         if (jennElector != null) {
/*      */           
/* 1891 */           performer.getCommunicator().sendNormalServerMessage("The " + elector
/* 1892 */               .getName() + " is busy. You will have to wait for your turn.");
/* 1893 */           done = true;
/*      */         } 
/*      */       }
/* 1896 */       if (kingdom == 3)
/*      */       {
/* 1898 */         if (hotsElector != null) {
/*      */           
/* 1900 */           performer.getCommunicator().sendNormalServerMessage("The " + elector
/* 1901 */               .getName() + " is busy. You will have to wait for your turn.");
/* 1902 */           done = true;
/*      */         } 
/*      */       }
/* 1905 */       if (kingdom == 2)
/*      */       {
/* 1907 */         if (molrStone != null) {
/*      */           
/* 1909 */           performer.getCommunicator().sendNormalServerMessage("The " + item
/* 1910 */               .getName() + " is occupied. You will have to wait for your turn.");
/* 1911 */           done = true;
/*      */         } 
/*      */       }
/*      */     } 
/* 1915 */     if (!done) {
/*      */       
/* 1917 */       int skill1 = 102;
/* 1918 */       int skill2 = 100;
/* 1919 */       if (kingdom == 1)
/* 1920 */         skill1 = 105; 
/* 1921 */       if (kingdom == 3)
/* 1922 */         skill2 = 105; 
/* 1923 */       if (counter == 1.0F) {
/*      */         
/* 1925 */         if (kingdom == 1)
/* 1926 */           jennElector = elector; 
/* 1927 */         if (kingdom == 3)
/* 1928 */           hotsElector = elector; 
/* 1929 */         if (kingdom == 2)
/* 1930 */           molrStone = item; 
/* 1931 */         String tname = "";
/* 1932 */         if (elector != null) {
/*      */           
/* 1934 */           tname = elector.getName();
/*      */         }
/* 1936 */         else if (item != null) {
/* 1937 */           tname = item.getName();
/* 1938 */         }  performer.getCommunicator().sendNormalServerMessage("You hesitantly approach the " + tname + ".");
/* 1939 */         Server.getInstance().broadCastAction(performer.getName() + " hesitantly approaches the " + tname + ".", performer, 5);
/*      */         
/* 1941 */         performer.sendActionControl(Actions.actionEntrys[353].getVerbString(), true, 300);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1947 */         if (act.currentSecond() % 10 == 0 && elector != null)
/* 1948 */           elector.playAnimation("regalia", false); 
/* 1949 */         if (act.currentSecond() == 5) {
/*      */           
/* 1951 */           if (item != null) {
/*      */             
/* 1953 */             performer.getCommunicator().sendNormalServerMessage("You struggle with the " + item.getName() + ".");
/* 1954 */             Server.getInstance().broadCastAction(performer
/* 1955 */                 .getName() + " struggles with the " + item.getName() + ".", performer, 5);
/*      */           } 
/* 1957 */           if (elector != null)
/*      */           {
/* 1959 */             if (kingdom == 1)
/*      */             {
/* 1961 */               elector.almostSurface();
/*      */             }
/* 1963 */             performer.getCommunicator().sendNormalServerMessage(elector.getName() + " watches you intensely.");
/* 1964 */             Server.getInstance().broadCastAction(elector
/* 1965 */                 .getName() + " watches " + performer.getName() + " intensely.", performer, 5);
/* 1966 */             elector.turnTowardsCreature(performer);
/*      */           }
/*      */         
/* 1969 */         } else if (act.currentSecond() == 10) {
/*      */           
/* 1971 */           done = true;
/*      */           
/*      */           try {
/* 1974 */             Skill sk = performer.getSkills().getSkill(skill1);
/* 1975 */             if (sk.getKnowledge(0.0D) > 21.0D)
/*      */             {
/* 1977 */               done = false;
/* 1978 */               if (item != null) {
/*      */                 
/* 1980 */                 performer.getCommunicator().sendNormalServerMessage("You feel the sword budge!");
/* 1981 */                 Server.getInstance().broadCastAction(performer
/* 1982 */                     .getName() + " seems to make progress with the sword!", performer, 5);
/*      */               } 
/* 1984 */               if (elector != null)
/*      */               {
/* 1986 */                 if (kingdom == 1)
/*      */                 {
/*      */                   
/* 1989 */                   performer.getCommunicator().sendNormalServerMessage(elector.getName() + " nods solemnly!");
/* 1990 */                   Server.getInstance().broadCastAction(elector.getName() + " nods faintly in approval!", performer, 5);
/*      */ 
/*      */                 
/*      */                 }
/* 1994 */                 else if (kingdom == 3)
/*      */                 {
/* 1996 */                   performer.getCommunicator().sendNormalServerMessage(elector
/* 1997 */                       .getName() + " hisses and sways " + elector.getHisHerItsString() + " head back and forth in excitement!");
/*      */                   
/* 1999 */                   Server.getInstance().broadCastAction(elector
/* 2000 */                       .getName() + " hisses and sways " + elector.getHisHerItsString() + " head back and forth in excitement!", performer, 5);
/*      */                 }
/*      */               
/*      */               }
/*      */             }
/*      */           
/*      */           }
/* 2007 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */           
/* 2011 */           if (done)
/*      */           {
/* 2013 */             setAspiredKing(performer);
/* 2014 */             if (item != null) {
/*      */               
/* 2016 */               performer.getCommunicator().sendNormalServerMessage("The sword is stuck and won't budge.");
/* 2017 */               Server.getInstance().broadCastAction(performer
/* 2018 */                   .getName() + " shrugs in disappointment as the sword does not budge.", performer, 5);
/*      */             } 
/*      */             
/* 2021 */             if (elector != null)
/*      */             {
/* 2023 */               if (kingdom == 1)
/*      */               {
/* 2025 */                 elector.submerge();
/* 2026 */                 performer.getCommunicator().sendNormalServerMessage(elector
/* 2027 */                     .getName() + " silently disappears into the depths.");
/* 2028 */                 Server.getInstance().broadCastAction(elector
/* 2029 */                     .getName() + " disappears into the depths in silence.", performer, 5);
/*      */               }
/* 2031 */               else if (kingdom == 3)
/*      */               {
/* 2033 */                 performer.getCommunicator().sendNormalServerMessage(elector
/* 2034 */                     .getName() + " ushers you away with some really threatening moves.");
/* 2035 */                 Server.getInstance()
/* 2036 */                   .broadCastAction(elector
/* 2037 */                     .getName() + " thwarts " + performer.getName() + " with some threatening moves.", performer, 5);
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         
/* 2043 */         } else if (act.currentSecond() == 15) {
/*      */           
/* 2045 */           if (item != null) {
/*      */             
/* 2047 */             performer.getCommunicator().sendNormalServerMessage("You continue struggle with the sword.");
/* 2048 */             Server.getInstance().broadCastAction(performer.getName() + " continues to struggle with the sword.", performer, 5);
/*      */           } 
/*      */           
/* 2051 */           if (elector != null)
/*      */           {
/* 2053 */             performer.getCommunicator()
/* 2054 */               .sendNormalServerMessage(elector.getName() + " watches you intensely again.");
/* 2055 */             Server.getInstance().broadCastAction(elector
/* 2056 */                 .getName() + " watches " + performer.getName() + " intensely again.", performer, 5);
/* 2057 */             elector.turnTowardsCreature(performer);
/*      */           }
/*      */         
/* 2060 */         } else if (act.currentSecond() == 20) {
/*      */           
/* 2062 */           done = true;
/*      */           
/*      */           try {
/* 2065 */             Skill sk = performer.getSkills().getSkill(skill2);
/* 2066 */             if (sk.getKnowledge(0.0D) > 21.0D)
/*      */             {
/* 2068 */               done = false;
/* 2069 */               if (item != null) {
/*      */                 
/* 2071 */                 performer.getCommunicator().sendNormalServerMessage("You feel the sword budge even more!");
/* 2072 */                 Server.getInstance()
/* 2073 */                   .broadCastAction(performer
/* 2074 */                     .getName() + " seems to make even more progress with the sword!", performer, 5);
/*      */               } 
/*      */               
/* 2077 */               if (elector != null)
/*      */               {
/* 2079 */                 if (kingdom == 1)
/*      */                 {
/* 2081 */                   performer.getCommunicator().sendNormalServerMessage(elector.getName() + " nods solemnly!");
/* 2082 */                   Server.getInstance().broadCastAction(elector.getName() + " nods faintly in approval!", performer, 5);
/*      */                 
/*      */                 }
/* 2085 */                 else if (kingdom == 3)
/*      */                 {
/* 2087 */                   performer.getCommunicator().sendNormalServerMessage(elector
/* 2088 */                       .getName() + " hisses and sways " + elector.getHisHerItsString() + " head back and forth in excitement!");
/*      */                   
/* 2090 */                   Server.getInstance().broadCastAction(elector
/* 2091 */                       .getName() + " hisses and sways " + elector.getHisHerItsString() + " head back and forth in excitement!", performer, 5);
/*      */                 }
/*      */               
/*      */               }
/*      */             }
/*      */           
/*      */           }
/* 2098 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */           
/* 2102 */           if (done)
/*      */           {
/* 2104 */             setAspiredKing(performer);
/* 2105 */             if (item != null) {
/*      */               
/* 2107 */               performer.getCommunicator().sendNormalServerMessage("The sword is stuck and won't budge.");
/* 2108 */               Server.getInstance().broadCastAction(performer
/* 2109 */                   .getName() + " shrugs in disappointment as the sword does not budge.", performer, 5);
/*      */             } 
/*      */             
/* 2112 */             if (elector != null)
/*      */             {
/* 2114 */               if (kingdom == 1)
/*      */               {
/* 2116 */                 elector.submerge();
/* 2117 */                 performer.getCommunicator().sendNormalServerMessage(elector
/* 2118 */                     .getName() + " silently disappears into the depths.");
/* 2119 */                 Server.getInstance().broadCastAction(elector
/* 2120 */                     .getName() + " disappears into the depths in silence.", performer, 5);
/*      */               }
/* 2122 */               else if (kingdom == 3)
/*      */               {
/* 2124 */                 performer.getCommunicator().sendNormalServerMessage(elector
/* 2125 */                     .getName() + " ushers you away with some really threatening moves.");
/* 2126 */                 Server.getInstance()
/* 2127 */                   .broadCastAction(elector
/* 2128 */                     .getName() + " thwarts " + performer.getName() + " with some threatening moves.", performer, 5);
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         
/* 2134 */         } else if (act.currentSecond() == 25) {
/*      */           
/* 2136 */           if (item != null) {
/*      */             
/* 2138 */             performer.getCommunicator().sendNormalServerMessage("You make one final push with the sword.");
/* 2139 */             Server.getInstance().broadCastAction(performer
/* 2140 */                 .getName() + " exerts all " + performer.getHisHerItsString() + " force on the sword.", performer, 5);
/*      */           } 
/*      */           
/* 2143 */           if (elector != null)
/*      */           {
/* 2145 */             performer.getCommunicator().sendNormalServerMessage(elector
/* 2146 */                 .getName() + " seems to make up " + elector.getHisHerItsString() + " mind about you.");
/* 2147 */             Server.getInstance().broadCastAction(elector
/* 2148 */                 .getName() + " watches " + performer.getName() + " intensely again.", performer, 5);
/* 2149 */             elector.turnTowardsCreature(performer);
/*      */           }
/*      */         
/* 2152 */         } else if (act.currentSecond() >= 30) {
/*      */           
/* 2154 */           done = true;
/* 2155 */           setAspiredKing(performer);
/* 2156 */           int randomint = 1000;
/* 2157 */           if (performer.getKingdomId() == 1) {
/* 2158 */             randomint = (int)(Kingdoms.activePremiumJenn * 1.5F);
/* 2159 */           } else if (performer.getKingdomId() == 3) {
/* 2160 */             randomint = (int)(Kingdoms.activePremiumHots * 1.5F);
/* 2161 */           } else if (performer.getKingdomId() == 2) {
/* 2162 */             randomint = (int)(Kingdoms.activePremiumMolr * 1.5F);
/* 2163 */           }  randomint = Math.max(10, randomint);
/* 2164 */           if (Server.rand.nextInt(randomint) == 0 || performer.getPower() >= 3) {
/*      */             
/* 2166 */             sendSound(performer, "sound.fx.ooh.male");
/* 2167 */             sendSound(performer, "sound.fx.ooh.female");
/* 2168 */             if (item != null) {
/*      */               
/* 2170 */               performer.getCommunicator().sendNormalServerMessage("The sword gets loose and disappears! You are the new " + 
/*      */                   
/* 2172 */                   King.getRulerTitle((performer.getSex() == 0), kingdom) + " of " + 
/* 2173 */                   Kingdoms.getNameFor(kingdom) + "!");
/* 2174 */               Server.getInstance().broadCastAction("The sword gets loose! " + performer
/* 2175 */                   .getName() + " is the new " + 
/* 2176 */                   King.getRulerTitle((performer.getSex() == 0), kingdom) + " of " + 
/* 2177 */                   Kingdoms.getNameFor(kingdom) + "!", performer, 10);
/*      */             } 
/* 2179 */             if (elector != null) {
/*      */               
/* 2181 */               elector.turnTowardsCreature(performer);
/* 2182 */               if (kingdom == 1) {
/*      */                 
/* 2184 */                 elector.submerge();
/* 2185 */                 elector.playAnimation("regalia", false);
/* 2186 */                 performer.getCommunicator().sendNormalServerMessage(elector
/* 2187 */                     .getName() + " hands you the royal regalia! You are the new " + 
/* 2188 */                     King.getRulerTitle((performer.getSex() == 0), kingdom) + " of " + 
/* 2189 */                     Kingdoms.getNameFor(kingdom) + "!");
/* 2190 */                 Server.getInstance().broadCastAction(elector
/* 2191 */                     .getName() + " hands the royal regalia to " + performer.getName() + "! " + performer
/* 2192 */                     .getHeSheItString() + " is the new " + 
/* 2193 */                     King.getRulerTitle((performer.getSex() == 0), kingdom) + " of " + 
/* 2194 */                     Kingdoms.getNameFor(kingdom) + "!", performer, 10);
/*      */               }
/* 2196 */               else if (kingdom == 3) {
/*      */                 
/* 2198 */                 performer
/* 2199 */                   .getCommunicator()
/* 2200 */                   .sendNormalServerMessage(elector
/* 2201 */                     .getName() + " hisses loudly and the royal regalia is handed to you from above! You are the new " + 
/*      */                     
/* 2203 */                     King.getRulerTitle((performer.getSex() == 0), kingdom) + " of " + 
/* 2204 */                     Kingdoms.getNameFor(kingdom) + "!");
/* 2205 */                 Server.getInstance().broadCastAction(elector
/* 2206 */                     .getName() + " hisses loudly in excitement! " + performer.getName() + " is the new " + 
/* 2207 */                     King.getRulerTitle((performer.getSex() == 0), kingdom) + " of " + 
/* 2208 */                     Kingdoms.getNameFor(kingdom) + "!", performer, 10);
/*      */               } 
/*      */             } 
/* 2211 */             King k = King.createKing(kingdom, performer.getName(), performer.getWurmId(), performer.getSex());
/* 2212 */             if (performer.getCitizenVillage() != null)
/* 2213 */               k.setCapital(performer.getCitizenVillage().getName(), false); 
/* 2214 */             rewardRegalia(performer);
/*      */             
/* 2216 */             NewKingQuestion nk = new NewKingQuestion(performer, "New ruler!", "Congratulations!", performer.getWurmId());
/* 2217 */             nk.sendQuestion();
/*      */           }
/*      */           else {
/*      */             
/* 2221 */             if (item != null) {
/*      */               
/* 2223 */               performer.getCommunicator().sendNormalServerMessage("The " + item
/* 2224 */                   .getName() + " is stuck and won't budge.");
/* 2225 */               Server.getInstance().broadCastAction(performer
/* 2226 */                   .getName() + " shrugs in disappointment as the " + item.getName() + " does not budge.", performer, 5);
/*      */             } 
/*      */             
/* 2229 */             if (elector != null)
/*      */             {
/* 2231 */               if (kingdom == 1) {
/*      */                 
/* 2233 */                 elector.submerge();
/* 2234 */                 performer.getCommunicator().sendNormalServerMessage("For no obvious reason you are rejected. " + elector
/* 2235 */                     .getName() + " silently disappears into the depths.");
/*      */                 
/* 2237 */                 Server.getInstance().broadCastAction(elector
/* 2238 */                     .getName() + " disappears into the depths in silence.", performer, 5);
/*      */               }
/* 2240 */               else if (kingdom == 3) {
/*      */                 
/* 2242 */                 performer.getCommunicator().sendNormalServerMessage("For no obvious reason you are rejected. " + elector
/* 2243 */                     .getName() + " ushers you away with some really threatening moves.");
/*      */                 
/* 2245 */                 Server.getInstance()
/* 2246 */                   .broadCastAction(elector
/* 2247 */                     .getName() + " thwarts " + performer.getName() + " with some threatening moves.", performer, 5);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2255 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void rewardRegalia(Creature creature) {
/* 2260 */     Item inventory = creature.getInventory();
/* 2261 */     if (inventory != null) {
/*      */       
/*      */       try {
/*      */         
/* 2265 */         byte template = Kingdoms.getKingdom(creature.getKingdomId()).getTemplate();
/* 2266 */         byte kingdom = creature.getKingdomId();
/* 2267 */         if (template == 1)
/*      */         {
/* 2269 */           Item sceptre = ItemFactory.createItem(529, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2270 */               .getName());
/* 2271 */           sceptre.setAuxData(kingdom);
/* 2272 */           inventory.insertItem(sceptre, true);
/* 2273 */           Item crown = ItemFactory.createItem(530, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2274 */               .getName());
/* 2275 */           crown.setAuxData(kingdom);
/* 2276 */           inventory.insertItem(crown, true);
/* 2277 */           Item robes = ItemFactory.createItem(531, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2278 */               .getName());
/* 2279 */           robes.setAuxData(kingdom);
/* 2280 */           inventory.insertItem(robes, true);
/*      */         }
/* 2282 */         else if (template == 3)
/*      */         {
/* 2284 */           Item sceptre = ItemFactory.createItem(535, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2285 */               .getName());
/* 2286 */           sceptre.setAuxData(kingdom);
/* 2287 */           inventory.insertItem(sceptre, true);
/* 2288 */           Item crown = ItemFactory.createItem(536, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2289 */               .getName());
/* 2290 */           crown.setAuxData(kingdom);
/* 2291 */           inventory.insertItem(crown, true);
/* 2292 */           Item robes = ItemFactory.createItem(537, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2293 */               .getName());
/* 2294 */           robes.setAuxData(kingdom);
/* 2295 */           inventory.insertItem(robes, true);
/*      */         }
/* 2297 */         else if (template == 2)
/*      */         {
/* 2299 */           Item sceptre = ItemFactory.createItem(532, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2300 */               .getName());
/* 2301 */           sceptre.setAuxData(kingdom);
/* 2302 */           inventory.insertItem(sceptre, true);
/* 2303 */           Item crown = ItemFactory.createItem(533, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2304 */               .getName());
/* 2305 */           crown.setAuxData(kingdom);
/* 2306 */           inventory.insertItem(crown, true);
/* 2307 */           Item robes = ItemFactory.createItem(534, Server.rand.nextFloat() * 30.0F + 70.0F, creature
/* 2308 */               .getName());
/* 2309 */           robes.setAuxData(kingdom);
/* 2310 */           inventory.insertItem(robes, true);
/*      */         }
/*      */       
/* 2313 */       } catch (Exception ex) {
/*      */         
/* 2315 */         logger.log(Level.WARNING, creature.getName() + " " + ex.getMessage(), ex);
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
/*      */   public static Creature getJennElector() {
/* 2327 */     return jennElector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void resetJennElector() {
/* 2335 */     jennElector = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Creature getHotsElector() {
/* 2345 */     return hotsElector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void resetHotsElector() {
/* 2353 */     hotsElector = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Item getMolrStone() {
/* 2363 */     return molrStone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void resetMolrStone() {
/* 2371 */     molrStone = null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void sendVoiceChatQuestion(Creature player) {
/* 2376 */     if (player != null)
/*      */     {
/* 2378 */       if (Constants.isEigcEnabled) {
/*      */         
/* 2380 */         VoiceChatQuestion vcq = new VoiceChatQuestion(player);
/* 2381 */         vcq.sendQuestion();
/*      */       } else {
/*      */         
/* 2384 */         player.getCommunicator().sendNormalServerMessage("Voice chat is not enabled on this server.");
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   static void sendGmSetEnchantQuestion(Creature performer, Item target) {
/* 2390 */     GmSetEnchants gmse = new GmSetEnchants(performer, target);
/* 2391 */     gmse.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static void sendGmSetTraitsQuestion(Creature performer, Creature target) {
/* 2396 */     GmSetTraits gmst = new GmSetTraits(performer, target);
/* 2397 */     gmst.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static void sendGmSetMedpathQuestion(Creature performer, Creature target) {
/* 2402 */     GmSetMedPath gmsm = new GmSetMedPath(performer, target);
/* 2403 */     gmsm.sendQuestion();
/*      */   }
/*      */ 
/*      */   
/*      */   static void sendGmBuildAllWallsQuestion(Creature performer, Structure target) {
/* 2408 */     GMBuildAllWallsQuestion gmbawq = new GMBuildAllWallsQuestion(performer, target);
/* 2409 */     gmbawq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendPlanBridgeQuestion(Creature performer, int targetFloorLevel, Point start, Point end, byte dir, int width, int length) {
/* 2415 */     PlanBridgeQuestion pbq = new PlanBridgeQuestion(performer, targetFloorLevel, start, end, dir, width, length);
/* 2416 */     pbq.sendQuestion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Item[] getBestReports(Creature creature, @Nullable Item container) {
/* 2426 */     Map<Byte, Item> reports = new HashMap<>();
/* 2427 */     if (container == null) {
/*      */ 
/*      */       
/* 2430 */       for (Item item : creature.getInventory().getAllItems(true)) {
/*      */         
/* 2432 */         if (item.getTemplateId() == 1127) {
/* 2433 */           addAlmanacReports(reports, item);
/*      */         }
/*      */       } 
/* 2436 */     } else if (container.getTemplateId() == 1127) {
/* 2437 */       addAlmanacReports(reports, container);
/* 2438 */     } else if (container.getTemplateId() == 1128) {
/* 2439 */       addAlmanacFolderReports(reports, container);
/*      */     } 
/* 2441 */     Item[] reportArr = (Item[])reports.values().toArray((Object[])new Item[reports.size()]);
/* 2442 */     Arrays.sort((Object[])reportArr);
/* 2443 */     return reportArr;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addAlmanacReports(Map<Byte, Item> reports, Item almanac) {
/* 2448 */     for (Item item : almanac.getItems()) {
/*      */       
/* 2450 */       if (item.getTemplateId() == 1128) {
/*      */         
/* 2452 */         addAlmanacFolderReports(reports, item);
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 2457 */       addReport(reports, item);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addAlmanacFolderReports(Map<Byte, Item> reports, Item almanacFolder) {
/* 2464 */     for (Item report : almanacFolder.getItems())
/*      */     {
/* 2466 */       addReport(reports, report);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addReport(Map<Byte, Item> reports, Item report) {
/* 2473 */     if (report.isHarvestReport()) {
/*      */ 
/*      */       
/* 2476 */       Item oldReport = reports.get(Byte.valueOf(report.getAuxData()));
/* 2477 */       if (oldReport == null) {
/*      */         
/* 2479 */         reports.put(Byte.valueOf(report.getAuxData()), report);
/*      */ 
/*      */       
/*      */       }
/* 2483 */       else if (oldReport.getCurrentQualityLevel() < report.getCurrentQualityLevel()) {
/*      */         
/* 2485 */         reports.put(Byte.valueOf(report.getAuxData()), report);
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
/*      */   public static void addActionIfAbsent(List<ActionEntry> actionEntries, ActionEntry newActionEntry) {
/* 2499 */     if (!actionEntries.contains(newActionEntry))
/*      */     {
/* 2501 */       actionEntries.add(newActionEntry);
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
/*      */   public static boolean isActionAllowed(Creature performer, short action) {
/* 2520 */     return isActionAllowed(performer, action, performer.getTileX(), performer.getTileY());
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
/*      */   public static boolean isActionAllowed(Creature performer, short action, Item item) {
/* 2532 */     return isActionAllowed(performer, action, false, item.getTileX(), item.getTileY(), item, 0, 0);
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
/*      */   public static boolean isActionAllowed(Creature performer, short action, int x, int y) {
/* 2546 */     return isActionAllowed(performer, action, false, x, y, null, 0, 0);
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
/*      */   public static boolean isActionAllowed(Creature performer, short action, boolean setHunted, int tileX, int tileY, int encodedTile, int dir) {
/* 2564 */     return isActionAllowed(performer, action, false, tileX, tileY, null, encodedTile, dir);
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
/*      */   public static boolean isActionAllowed(Creature performer, short action, boolean setHunted, int tileX, int tileY, @Nullable Item item, int encodedTile, int dir) {
/* 2585 */     VolaTile vt = Zones.getOrCreateTile(tileX, tileY, performer.isOnSurface());
/*      */     
/* 2587 */     Village village = (vt != null) ? vt.getVillage() : null;
/* 2588 */     Structure structure = (vt != null) ? vt.getStructure() : null;
/* 2589 */     boolean canDo = true;
/*      */ 
/*      */     
/* 2592 */     if (Actions.isActionDestroy(action) && village != null && village.isActionAllowed(action, performer)) {
/* 2593 */       canDo = true;
/*      */     }
/* 2595 */     else if (structure != null && structure.isTypeHouse() && !structure.isFinished() && (
/* 2596 */       Actions.isActionBuild(action) || Actions.isActionDestroy(action)) && structure
/* 2597 */       .isActionAllowed(performer, action)) {
/* 2598 */       canDo = true;
/*      */     
/*      */     }
/* 2601 */     else if (structure != null && structure.isTypeHouse() && structure.isFinished()) {
/*      */       
/* 2603 */       if (!Actions.isActionBuildingPermission(action) && village != null && village
/* 2604 */         .isActionAllowed(action, performer)) {
/* 2605 */         canDo = true;
/* 2606 */       } else if (!structure.isActionAllowed(performer, action)) {
/* 2607 */         canDo = isNotAllowedMessage(performer, village, structure, action, false);
/*      */       } else {
/* 2609 */         canDo = true;
/*      */       }
/*      */     
/* 2612 */     } else if (village != null) {
/*      */       
/* 2614 */       if (!village.isActionAllowed(action, performer, false, encodedTile, dir)) {
/* 2615 */         canDo = isNotAllowedMessage(performer, village, structure, action, false);
/*      */       } else {
/* 2617 */         canDo = true;
/*      */       } 
/* 2619 */     }  if (village == null && Actions.actionEntrys[action].isPerimeterAction()) {
/*      */ 
/*      */       
/* 2622 */       Village villagePerim = Villages.getVillageWithPerimeterAt(tileX, tileY, true);
/* 2623 */       if (villagePerim != null && !villagePerim.isCitizen(performer) && !villagePerim.isAlly(performer)) {
/*      */ 
/*      */         
/* 2626 */         boolean skipOthers = false;
/*      */         
/*      */         try {
/* 2629 */           Item token = villagePerim.getToken();
/* 2630 */           if (token != null && token.getWurmId() == 7689502046815490L)
/*      */           {
/* 2632 */             canDo = true;
/* 2633 */             skipOthers = true;
/*      */           }
/*      */         
/* 2636 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2641 */         if (!skipOthers)
/*      */         {
/* 2643 */           if (!villagePerim.isActionAllowed(action, performer, false, 0, 0)) {
/* 2644 */             canDo = isNotAllowedMessage(performer, villagePerim, structure, action, true);
/*      */           } else {
/* 2646 */             canDo = true;
/*      */           } 
/*      */         }
/*      */       } 
/* 2650 */     } else if (village == null && item != null && item.isRoadMarker()) {
/*      */ 
/*      */       
/* 2653 */       Village twoCheck = null;
/* 2654 */       twoCheck = Zones.getVillage(item.getTilePos(), item.isOnSurface());
/* 2655 */       if (twoCheck == null) {
/*      */ 
/*      */         
/* 2658 */         Village vill = Villages.getVillageWithPerimeterAt(item.getTileX(), item.getTileY(), item.isOnSurface());
/* 2659 */         if (vill != null && vill.coversPlus(item.getTileX(), item.getTileY(), 2))
/* 2660 */           twoCheck = vill; 
/*      */       } 
/* 2662 */       if (twoCheck != null)
/*      */       {
/* 2664 */         if (!twoCheck.isActionAllowed(action, performer, false, 0, 0)) {
/* 2665 */           canDo = isNotAllowedMessage(performer, twoCheck, structure, action, true);
/*      */         } else {
/* 2667 */           canDo = true;
/*      */         }  } 
/*      */     } 
/* 2670 */     return canDo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isNotAllowedMessage(Creature performer, Village village, Structure structure, short action, boolean inPerimeter) {
/* 2678 */     if (!performer.isOnPvPServer() || Servers.isThisAChaosServer()) {
/*      */       String msg;
/*      */ 
/*      */       
/* 2682 */       if (inPerimeter) {
/* 2683 */         msg = village.getName() + " does not allow that.";
/* 2684 */       } else if (village != null && (village.getGuards()).length > 0) {
/* 2685 */         msg = "The guards kindly inform you that you are not allowed to do that here.";
/* 2686 */       } else if (village != null) {
/* 2687 */         msg = "That would be very bad for your karma and is disallowed on this server.";
/*      */       } else {
/* 2689 */         msg = "You do not have permission to do that here.";
/* 2690 */       }  performer.getCommunicator().sendNormalServerMessage(msg);
/* 2691 */       return false;
/*      */     } 
/*      */     
/* 2694 */     if (village != null) {
/*      */       
/* 2696 */       if (!village.isEnemy(performer)) {
/*      */         
/* 2698 */         if (performer.isLegal()) {
/*      */           
/* 2700 */           performer.getCommunicator().sendNormalServerMessage("That would be illegal here. You can check the settlement token for the local laws.");
/* 2701 */           return false;
/*      */         } 
/*      */ 
/*      */         
/* 2705 */         if (Actions.actionEntrys[action].isEnemyAllowedWhenNoGuards() && (village.getGuards()).length > 0) {
/*      */           
/* 2707 */           performer.getCommunicator().sendNormalServerMessage("A guard has noted you and stops you with a warning.");
/* 2708 */           return false;
/*      */         } 
/* 2710 */         if (Actions.actionEntrys[action].isEnemyNeverAllowed())
/*      */         {
/* 2712 */           performer.getCommunicator().sendNormalServerMessage("That action makes no sense here.");
/* 2713 */           return false;
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2719 */         if (Actions.actionEntrys[action].isEnemyAllowedWhenNoGuards() && (village.getGuards()).length > 0) {
/*      */           
/* 2721 */           performer.getCommunicator().sendNormalServerMessage("A guard has noted you and stops you with a warning.");
/* 2722 */           return false;
/*      */         } 
/* 2724 */         if (Actions.actionEntrys[action].isEnemyNeverAllowed()) {
/*      */           
/* 2726 */           performer.getCommunicator().sendNormalServerMessage("That action makes no sense here.");
/* 2727 */           return false;
/*      */         } 
/*      */       } 
/* 2730 */       return true;
/*      */     } 
/*      */     
/* 2733 */     if (structure != null && structure.isFinished()) {
/*      */       
/* 2735 */       if (!structure.isEnemy(performer)) {
/*      */         
/* 2737 */         if (performer.isLegal())
/*      */         {
/* 2739 */           performer.getCommunicator().sendNormalServerMessage("That would be illegal. ");
/* 2740 */           return false;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 2745 */       else if (Actions.actionEntrys[action].isEnemyNeverAllowed()) {
/*      */         
/* 2747 */         performer.getCommunicator().sendNormalServerMessage("That action makes no sense here.");
/* 2748 */         return false;
/*      */       } 
/*      */       
/* 2751 */       return true;
/*      */     } 
/* 2753 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Methods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */