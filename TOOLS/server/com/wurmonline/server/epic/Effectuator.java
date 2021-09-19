/*      */ package com.wurmonline.server.epic;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.behaviours.TerraformingTask;
/*      */ import com.wurmonline.server.bodys.BodyHuman;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.meshgen.IslandAdder;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Effectuator
/*      */   implements CreatureTemplateIds, MiscConstants
/*      */ {
/*   67 */   private static final Random rand = new Random();
/*      */   
/*      */   public static final int EFFECT_NONE = 0;
/*      */   
/*      */   public static final int EFFECT_SPEED = 1;
/*      */   
/*      */   public static final int EFFECT_COMBATRATING = 2;
/*      */   
/*      */   public static final int EFFECT_STAMINA_REGAIN = 3;
/*      */   
/*      */   public static final int EFFECT_FAVORGAIN = 4;
/*      */   
/*      */   public static final int EFFECT_SPAWN = 5;
/*      */   
/*      */   private static final String LOAD_KINGDOM_EFFECTS = "SELECT * FROM KINGDOMEFFECTS";
/*      */   
/*      */   private static final String INSERT_KINGDOM_EFFECTS = "INSERT INTO KINGDOMEFFECTS (EFFECT,KINGDOM) VALUES(?,?)";
/*      */   
/*      */   private static final String UPDATE_KINGDOM_EFFECTS = "UPDATE KINGDOMEFFECTS SET KINGDOM=? WHERE EFFECT=?";
/*      */   private static final String LOAD_DEITY_EFFECTS = "SELECT * FROM DEITYEFFECTS";
/*      */   private static final String INSERT_DEITY_EFFECTS = "INSERT INTO DEITYEFFECTS (EFFECT,DEITY) VALUES(?,?)";
/*      */   private static final String UPDATE_DEITY_EFFECTS = "UPDATE DEITYEFFECTS SET DEITY=? WHERE EFFECT=?";
/*   89 */   private static int kingdomTemplateWithSpeedBonus = 0;
/*   90 */   private static int kingdomTemplateWithCombatRating = 0;
/*   91 */   private static int kingdomTemplateWithStaminaRegain = 0;
/*   92 */   private static int kingdomTemplateWithFavorGain = 0;
/*      */   
/*   94 */   private static int deityWithSpeedBonus = 0;
/*   95 */   private static int deityWithCombatRating = 0;
/*   96 */   private static int deityWithStaminaRegain = 0;
/*   97 */   private static int deityWithFavorGain = 0;
/*   98 */   private static final LinkedBlockingQueue<SynchedEpicEffect> comingEvents = new LinkedBlockingQueue<>();
/*      */   
/*  100 */   private static final Logger logger = Logger.getLogger(Effectuator.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSpiritType(int effect) {
/*  114 */     switch (effect)
/*      */     
/*      */     { case 1:
/*  117 */         toReturn = "fire";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  132 */         return toReturn;case 2: toReturn = "forest"; return toReturn;case 3: toReturn = "mountain"; return toReturn;case 4: toReturn = "water"; return toReturn; }  String toReturn = "hidden"; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addEpicEffect(SynchedEpicEffect effect) {
/*  137 */     comingEvents.add(effect);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void pollEpicEffects() {
/*  142 */     for (SynchedEpicEffect effect : comingEvents)
/*      */     {
/*  144 */       effect.run();
/*      */     }
/*  146 */     comingEvents.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void doEvent(int eventNum, long deityNumber, int creatureTemplateId, int bonusEffectNum, String eventDesc) {
/*  157 */     if (Servers.localServer.EPIC && !Servers.localServer.LOGINSERVER) {
/*      */       
/*  159 */       setEffectController(4, 0L);
/*  160 */       setEffectController(2, 0L);
/*  161 */       setEffectController(1, 0L);
/*  162 */       setEffectController(3, 0L);
/*  163 */       byte favoredKingdom = Deities.getFavoredKingdom((int)deityNumber);
/*      */ 
/*      */ 
/*      */       
/*  167 */       boolean doNegative = false;
/*  168 */       switch (rand.nextInt(7)) {
/*      */ 
/*      */         
/*      */         case 0:
/*  172 */           spawnDefenders(deityNumber, creatureTemplateId);
/*      */           break;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void doEvent1(long deityNumber) {
/*  214 */     if (Servers.localServer.EPIC) {
/*      */       
/*  216 */       if (deityNumber == 5L)
/*  217 */         wurmPunish(4000, 0.0F, 20.0F, (byte)6); 
/*  218 */       spawnOwnCreatures(deityNumber, 38, true);
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
/*      */   static void doEvent5(long deityNumber) {
/*  230 */     if (Servers.localServer.EPIC)
/*      */     {
/*  232 */       if (deityNumber == 5L) {
/*  233 */         wurmPunish(4000, 20.0F, 0.0F, (byte)5);
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
/*      */   static void doEvent7(long deityNumber) {
/*  245 */     if (Servers.localServer.EPIC)
/*      */     {
/*  247 */       if (deityNumber == 5L) {
/*  248 */         crushStructures();
/*      */       } else {
/*      */         
/*  251 */         IslandAdder isl = new IslandAdder(Server.surfaceMesh, Server.rockMesh);
/*  252 */         isl.addOneIsland(Zones.worldTileSizeX, Zones.worldTileSizeY);
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
/*      */   static void doEvent8(long deityNumber) {
/*  265 */     if (Servers.localServer.EPIC)
/*      */     {
/*  267 */       if (deityNumber == 5L) {
/*  268 */         wurmPunish(8000, 0.0F, 0.0F, (byte)9);
/*      */       } else {
/*  270 */         doEvent15(deityNumber);
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
/*      */   static void terraform(int task, long deityNumber, int nums) {
/*  282 */     byte favoredKingdom = Deities.getFavoredKingdom((int)deityNumber);
/*  283 */     Deity d = Deities.getDeity((int)deityNumber);
/*  284 */     if (d != null)
/*      */     {
/*  286 */       new TerraformingTask(task, favoredKingdom, d.getName(), (int)deityNumber, nums, true);
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
/*      */   static void doEvent12(long deityNumber) {
/*  299 */     if (Servers.localServer.EPIC)
/*      */     {
/*  301 */       disease(deityNumber);
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
/*      */   static void doEvent14(long deityNumber) {
/*  313 */     if (Servers.localServer.EPIC)
/*      */     {
/*  315 */       slay(deityNumber);
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
/*      */   static void doEvent15(long deityNumber) {
/*  327 */     awardSkill(deityNumber, 103, 0.005F, 20.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void doEvent17(long deityNumber) {
/*  338 */     if (Servers.localServer.EPIC)
/*      */     {
/*  340 */       if (deityNumber == 5L) {
/*  341 */         wurmPunish(4000, 0.0F, 0.0F, (byte)9);
/*      */       } else {
/*  343 */         awardSkill(deityNumber, 105, 0.005F, 20.0F);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void appointAlly(long deityNumber) {
/*  354 */     if (Servers.localServer.EPIC)
/*      */     {
/*  356 */       if (deityNumber == 5L) {
/*  357 */         wurmPunish(14000, 20.0F, 20.0F, (byte)9);
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
/*      */   static final void promoteImmortal(long deityNumber) {
/*  372 */     if (Servers.localServer.LOGINSERVER)
/*      */     {
/*  374 */       if (!HexMap.VALREI.elevateDemigod(deityNumber));
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
/*      */   static void doEvent20(long deityNumber) {
/*  392 */     punishSkill(deityNumber, 100, 0.5F);
/*  393 */     punishSkill(deityNumber, 102, 0.5F);
/*  394 */     punishSkill(deityNumber, 106, 0.5F);
/*  395 */     punishSkill(deityNumber, 104, 0.5F);
/*  396 */     punishSkill(deityNumber, 101, 0.5F);
/*  397 */     punishSkill(deityNumber, 105, 0.5F);
/*  398 */     lowerFaith(deityNumber);
/*  399 */     for (int x = 0; x < Math.min(20, Players.getInstance().getNumberOfPlayers()); x++) {
/*  400 */       slay(deityNumber);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void doEvent21(long deityNumber) {
/*  411 */     punishSkill(deityNumber, 100, 0.04F);
/*  412 */     punishSkill(deityNumber, 102, 0.04F);
/*  413 */     punishSkill(deityNumber, 106, 0.04F);
/*  414 */     punishSkill(deityNumber, 104, 0.04F);
/*  415 */     punishSkill(deityNumber, 101, 0.04F);
/*  416 */     punishSkill(deityNumber, 105, 0.04F);
/*      */     
/*  418 */     awardSkill(deityNumber, 100, 0.005F, 20.0F);
/*  419 */     awardSkill(deityNumber, 102, 0.005F, 20.0F);
/*  420 */     awardSkill(deityNumber, 106, 0.005F, 20.0F);
/*  421 */     awardSkill(deityNumber, 104, 0.005F, 20.0F);
/*  422 */     awardSkill(deityNumber, 101, 0.005F, 20.0F);
/*  423 */     awardSkill(deityNumber, 105, 0.005F, 20.0F);
/*  424 */     lowerFaith(deityNumber);
/*  425 */     for (int x = 0; x < Math.min(10, Players.getInstance().getNumberOfPlayers()); x++) {
/*  426 */       slay(deityNumber);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void doEvent22(long deityNumber) {
/*  437 */     punishSkill(deityNumber, 100, 0.05F);
/*  438 */     punishSkill(deityNumber, 102, 0.05F);
/*  439 */     punishSkill(deityNumber, 106, 0.05F);
/*  440 */     punishSkill(deityNumber, 105, 0.05F);
/*      */     
/*  442 */     awardSkill(deityNumber, 100, 0.005F, 20.0F);
/*  443 */     awardSkill(deityNumber, 102, 0.005F, 20.0F);
/*  444 */     awardSkill(deityNumber, 106, 0.005F, 20.0F);
/*  445 */     awardSkill(deityNumber, 104, 0.005F, 20.0F);
/*  446 */     awardSkill(deityNumber, 101, 0.005F, 20.0F);
/*  447 */     awardSkill(deityNumber, 105, 0.005F, 20.0F);
/*  448 */     lowerFaith(deityNumber);
/*  449 */     for (int x = 0; x < Math.min(10, Players.getInstance().getNumberOfPlayers()); x++) {
/*  450 */       slay(deityNumber);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void doEvent23(long deityNumber) {
/*  461 */     punishSkill(deityNumber, 100, 0.05F);
/*  462 */     punishSkill(deityNumber, 102, 0.05F);
/*  463 */     punishSkill(deityNumber, 101, 0.05F);
/*  464 */     punishSkill(deityNumber, 105, 0.05F);
/*  465 */     awardSkill(deityNumber, 100, 0.005F, 20.0F);
/*  466 */     awardSkill(deityNumber, 102, 0.005F, 20.0F);
/*  467 */     awardSkill(deityNumber, 106, 0.005F, 20.0F);
/*  468 */     awardSkill(deityNumber, 104, 0.005F, 20.0F);
/*  469 */     awardSkill(deityNumber, 101, 0.005F, 20.0F);
/*  470 */     awardSkill(deityNumber, 105, 0.005F, 20.0F);
/*  471 */     lowerFaith(deityNumber);
/*  472 */     for (int x = 0; x < Math.min(10, Players.getInstance().getNumberOfPlayers()); x++) {
/*  473 */       slay(deityNumber);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void doEvent24(long deityNumber) {
/*  484 */     punishSkill(deityNumber, 103, 0.05F);
/*  485 */     punishSkill(deityNumber, 102, 0.05F);
/*  486 */     punishSkill(deityNumber, 101, 0.05F);
/*  487 */     punishSkill(deityNumber, 105, 0.05F);
/*      */     
/*  489 */     awardSkill(deityNumber, 100, 0.005F, 20.0F);
/*  490 */     awardSkill(deityNumber, 102, 0.005F, 20.0F);
/*  491 */     awardSkill(deityNumber, 106, 0.005F, 20.0F);
/*  492 */     awardSkill(deityNumber, 104, 0.005F, 20.0F);
/*  493 */     awardSkill(deityNumber, 101, 0.005F, 20.0F);
/*  494 */     awardSkill(deityNumber, 105, 0.005F, 20.0F);
/*  495 */     lowerFaith(deityNumber);
/*  496 */     for (int x = 0; x < Math.min(10, Players.getInstance().getNumberOfPlayers()); x++) {
/*  497 */       slay(deityNumber);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void doEvent25(long deityNumber) {
/*  508 */     punishSkill(deityNumber, 103, 0.05F);
/*  509 */     punishSkill(deityNumber, 102, 0.05F);
/*  510 */     punishSkill(deityNumber, 101, 0.05F);
/*  511 */     punishSkill(deityNumber, 105, 0.05F);
/*      */     
/*  513 */     awardSkill(deityNumber, 100, 0.005F, 20.0F);
/*  514 */     awardSkill(deityNumber, 102, 0.005F, 20.0F);
/*  515 */     awardSkill(deityNumber, 106, 0.005F, 20.0F);
/*  516 */     awardSkill(deityNumber, 104, 0.005F, 20.0F);
/*  517 */     awardSkill(deityNumber, 101, 0.005F, 20.0F);
/*  518 */     awardSkill(deityNumber, 105, 0.005F, 20.0F);
/*  519 */     lowerFaith(deityNumber);
/*  520 */     for (int x = 0; x < Math.min(20, Players.getInstance().getNumberOfPlayers()); x++) {
/*  521 */       slay(deityNumber);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void punishSkill(long deityNum, int skillNum, float toDecrease) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void disease(long deityNumberSaved) {
/*  650 */     byte friendlyKingdom = Deities.getFavoredKingdom((int)deityNumberSaved);
/*  651 */     Player[] players = Players.getInstance().getPlayers();
/*  652 */     for (int x = 0; x < players.length; x++) {
/*      */       
/*  654 */       if (friendlyKingdom == 0 || players[x].getKingdomTemplateId() != friendlyKingdom)
/*      */       {
/*  656 */         if (players[x].getDeity() == null || players[x].getDeity().getNumber() != deityNumberSaved) {
/*      */           
/*  658 */           players[x].getCommunicator().sendAlertServerMessage("An evil aura emanates from valrei. You suddenly feel like vomiting.");
/*      */           
/*  660 */           players[x].setDisease((byte)50);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void awardSkill(long deityNum, int skillNum, float toIncrease, float minNumber) {
/*  668 */     byte friendlyKingdom = Deities.getFavoredKingdom((int)deityNum);
/*  669 */     Player[] players = Players.getInstance().getPlayers();
/*  670 */     for (int x = 0; x < players.length; x++) {
/*      */       
/*  672 */       if (players[x].getKingdomTemplateId() == friendlyKingdom) {
/*      */         
/*      */         try {
/*      */           
/*  676 */           Skill old = players[x].getSkills().getSkill(skillNum);
/*  677 */           old.setKnowledge(old.getKnowledge() + (100.0D - old.getKnowledge()) * toIncrease, false);
/*      */         }
/*  679 */         catch (NoSuchSkillException nss) {
/*      */           
/*  681 */           players[x].getSkills().learn(skillNum, minNumber);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void slay(long deityNum) {
/*  689 */     Player[] players = Players.getInstance().getPlayers();
/*  690 */     if (deityNum == 5L) {
/*      */       
/*  692 */       boolean found = false;
/*  693 */       while (!found) {
/*      */         
/*  695 */         int p = rand.nextInt(players.length);
/*  696 */         if (!players[p].isDead() && players[p].isFullyLoaded() && players[p].getVisionArea() != null) {
/*      */           
/*  698 */           players[p].getCommunicator().sendAlertServerMessage("You feel an abnormal wave of heat coming from Valrei! Wurm has punished you!");
/*      */           
/*  700 */           players[p].die(false, "Valrei Lazer Beams");
/*  701 */           found = true;
/*      */         } 
/*      */         
/*  704 */         if (!found && players.length < 5 && 
/*  705 */           rand.nextBoolean()) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/*  711 */       boolean found = false;
/*  712 */       int seeks = 0;
/*      */       
/*  714 */       byte friendlyKingdom = Deities.getFavoredKingdom((int)deityNum);
/*  715 */       while (!found) {
/*      */         
/*  717 */         seeks++;
/*  718 */         int p = rand.nextInt(players.length);
/*  719 */         if (!players[p].isDead() && players[p].isFullyLoaded() && players[p].getVisionArea() != null)
/*      */         {
/*  721 */           if (players[p].getKingdomTemplateId() != friendlyKingdom)
/*      */           {
/*  723 */             if (players[p].getDeity() != null && players[p].getDeity().getNumber() != deityNum) {
/*      */               
/*  725 */               if ((deityNum != 1L || players[p].getDeity().getNumber() != 3) && (deityNum != 3L || players[p]
/*  726 */                 .getDeity().getNumber() != 1))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  732 */                 players[p].getCommunicator().sendAlertServerMessage("You suddenly feel yourself immolated in an abnormal wave of heat coming from Valrei!");
/*      */                 
/*  734 */                 players[p].die(false, "Valrei Bombardment");
/*  735 */                 found = true;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  740 */               players[p].getCommunicator().sendAlertServerMessage("You suddenly feel yourself immolated in an abnormal wave of heat coming from Valrei!");
/*      */               
/*  742 */               players[p].die(false, "Valrei Nuclear Blast");
/*  743 */               found = true;
/*      */             } 
/*      */           }
/*      */         }
/*      */         
/*  748 */         if (!found && seeks > players.length && 
/*  749 */           rand.nextBoolean()) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void lowerFaith(long deityNum) {
/*  757 */     PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfos();
/*  758 */     for (int x = 0; x < infos.length; x++) {
/*      */       
/*  760 */       byte kingdom = Players.getInstance().getKingdomForPlayer((infos[x]).wurmId);
/*  761 */       Kingdom k = Kingdoms.getKingdom(kingdom);
/*  762 */       byte kingdomTemplateId = k.getTemplate();
/*  763 */       byte favoredKingdom = Deities.getFavoredKingdom((int)deityNum);
/*  764 */       if (kingdomTemplateId != favoredKingdom) {
/*      */         
/*      */         try {
/*  767 */           if (infos[x].getFaith() > 80.0F) {
/*  768 */             infos[x].setFaith(infos[x].getFaith() - 1.0F);
/*  769 */           } else if (infos[x].getFaith() > 50.0F) {
/*  770 */             infos[x].setFaith(infos[x].getFaith() - 3.0F);
/*  771 */           } else if (infos[x].getFaith() > 20.0F) {
/*  772 */             infos[x].setFaith(infos[x].getFaith() * 0.8F);
/*  773 */           }  infos[x].setFavor(0.0F);
/*      */         }
/*  775 */         catch (IOException iOException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void wurmPunish(int damage, float poisondam, float disease, byte woundType) {
/*  784 */     Player[] players = Players.getInstance().getPlayers();
/*  785 */     BodyHuman body = new BodyHuman();
/*  786 */     for (int x = 0; x < players.length; x++) {
/*      */ 
/*      */       
/*      */       try {
/*  790 */         CombatEngine.addWound(null, (Creature)players[x], woundType, body.getRandomWoundPos(), damage, 1.0F, "hurts", null, disease, poisondam, false, false, false, false);
/*      */       
/*      */       }
/*  793 */       catch (Exception exception) {}
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
/*      */   private static void crushStructures() {
/*  809 */     Structure[] structures = Structures.getAllStructures();
/*  810 */     if (structures.length > 0)
/*      */     {
/*  812 */       structures[rand.nextInt(structures.length)].totallyDestroy();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void spawnDefenders(long deityId, int creatureTemplateId) {
/*  818 */     if (!Servers.isThisATestServer() && (Servers.localServer
/*  819 */       .isChallengeOrEpicServer() || Servers.isThisAChaosServer())) {
/*      */       
/*  821 */       byte friendlyKingdom = Deities.getFavoredKingdom((int)deityId);
/*  822 */       Deity deity = Deities.getDeity((int)deityId);
/*      */       
/*      */       try {
/*  825 */         CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(creatureTemplateId);
/*  826 */         if (friendlyKingdom != 0) {
/*      */           
/*  828 */           Kingdom k = Kingdoms.getKingdom(friendlyKingdom);
/*  829 */           if (k != null)
/*      */           {
/*  831 */             if (k.lastConfrontationTileX > 1 && k.lastConfrontationTileY > 1)
/*      */             {
/*  833 */               for (int a = 0; a < rand.nextInt(7) + 1; a++) {
/*      */                 
/*  835 */                 int tx = Zones.safeTileX(k.lastConfrontationTileX - 5 + rand.nextInt(10));
/*  836 */                 int ty = Zones.safeTileY(k.lastConfrontationTileY - 5 + rand.nextInt(10));
/*  837 */                 spawnCreatureAt(tx, ty, ctemplate, friendlyKingdom);
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*  842 */         if (deity != null)
/*      */         {
/*  844 */           if (deity.lastConfrontationTileX > 1 && deity.lastConfrontationTileY > 1)
/*      */           {
/*  846 */             for (int a = 0; a < rand.nextInt(7) + 1; a++)
/*      */             {
/*  848 */               int tx = Zones.safeTileX(deity.lastConfrontationTileX - 5 + rand.nextInt(10));
/*  849 */               int ty = Zones.safeTileY(deity.lastConfrontationTileY - 5 + rand.nextInt(10));
/*  850 */               spawnCreatureAt(tx, ty, ctemplate, friendlyKingdom);
/*      */             }
/*      */           
/*      */           }
/*      */         }
/*  855 */       } catch (NoSuchCreatureTemplateException ex) {
/*      */         
/*  857 */         logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */       } 
/*      */     } else {
/*      */       
/*  861 */       logger.log(Level.INFO, "Spawning defenders");
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void spawnOwnCreatures(long deityId, int creatureTemplateId, boolean onlyAtHome) {
/*  866 */     byte friendlyKingdom = Deities.getFavoredKingdom((int)deityId);
/*      */     
/*      */     try {
/*  869 */       CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(creatureTemplateId);
/*  870 */       int summoned = 0;
/*  871 */       Player[] players = Players.getInstance().getPlayers();
/*  872 */       int maxplayers = players.length / 10;
/*  873 */       int maxSummoned = (int)(200.0F / ctemplate.baseCombatRating);
/*  874 */       if (creatureTemplateId == 75) {
/*      */         
/*  876 */         maxplayers = 2;
/*  877 */         maxSummoned = 5;
/*      */       } 
/*      */ 
/*      */       
/*  881 */       if (!Servers.localServer.isChallengeOrEpicServer() && !Servers.isThisAChaosServer()) {
/*      */         return;
/*      */       }
/*  884 */       if (players.length > 10)
/*      */       {
/*  886 */         for (int x = 0; x < maxplayers; x++) {
/*      */           
/*  888 */           int playint = rand.nextInt(players.length);
/*  889 */           if (players[playint].getPositionZ() > -1.0F || (ctemplate
/*  890 */             .isSwimming() && players[playint].getPositionZ() < -4.0F))
/*      */           {
/*  892 */             if (players[playint].getKingdomTemplateId() != friendlyKingdom)
/*      */             {
/*  894 */               if (!players[playint].isFriendlyKingdom(friendlyKingdom)) {
/*      */                 
/*  896 */                 int centerx = players[playint].getTileX();
/*  897 */                 int centery = players[playint].getTileY();
/*  898 */                 for (int a = 0; a < Math.max(1.0F, 30.0F / ctemplate.baseCombatRating); a++) {
/*      */                   
/*  900 */                   int tx = Zones.safeTileX(centerx - 5 + rand.nextInt(10));
/*  901 */                   int ty = Zones.safeTileY(centery - 5 + rand.nextInt(10));
/*  902 */                   VolaTile t = Zones.getOrCreateTile(tx, ty, true);
/*  903 */                   if (t.getStructure() == null && t.getVillage() == null) {
/*      */                     
/*  905 */                     spawnCreatureAt(tx, ty, ctemplate, friendlyKingdom);
/*  906 */                     summoned++;
/*  907 */                     if (summoned >= maxSummoned)
/*      */                       break; 
/*      */                   } 
/*      */                 } 
/*  911 */                 if (summoned >= maxSummoned)
/*      */                   break; 
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*  918 */       if (!Servers.isThisATestServer()) {
/*      */         
/*  920 */         int tries = 0;
/*  921 */         while (summoned < maxSummoned && tries < 5000) {
/*      */           
/*  923 */           tries++;
/*  924 */           int centerx = rand.nextInt(Zones.worldTileSizeX);
/*  925 */           int centery = rand.nextInt(Zones.worldTileSizeY);
/*  926 */           if ((onlyAtHome && Zones.getKingdom(centerx, centery) == friendlyKingdom) || 
/*  927 */             Zones.getKingdom(centerx, centery) != friendlyKingdom)
/*      */           {
/*  929 */             for (int x = 0; x < 10; x++) {
/*      */               
/*  931 */               int tx = Zones.safeTileX(centerx - 5 + rand.nextInt(10));
/*  932 */               int ty = Zones.safeTileY(centery - 5 + rand.nextInt(10));
/*      */               
/*      */               try {
/*  935 */                 float height = Zones.calculateHeight((tx * 4 + 2), (ty * 4 + 2), true);
/*  936 */                 if (height >= 0.0F || (ctemplate.isSwimming() && height < -2.0F)) {
/*      */                   
/*  938 */                   VolaTile t = Zones.getOrCreateTile(tx, ty, true);
/*  939 */                   if (t.getStructure() == null && t.getVillage() == null) {
/*      */                     
/*  941 */                     spawnCreatureAt(tx, ty, ctemplate, friendlyKingdom);
/*  942 */                     summoned++;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*  947 */               } catch (NoSuchZoneException nsz) {
/*      */                 
/*  949 */                 logger.log(Level.WARNING, nsz.getMessage());
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*  956 */         logger.log(Level.INFO, "Spawning Own creatures");
/*      */       } 
/*  958 */     } catch (NoSuchCreatureTemplateException ex) {
/*      */       
/*  960 */       logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void spawnCreatureAt(int tilex, int tiley, CreatureTemplate ctemplate, byte friendlyKingdom) {
/*  967 */     if (ctemplate != null) {
/*      */       
/*      */       try {
/*      */         
/*  971 */         byte sex = ctemplate.getSex();
/*  972 */         if (sex == 0 && !ctemplate.keepSex)
/*      */         {
/*  974 */           if (Server.rand.nextInt(2) == 0)
/*  975 */             sex = 1; 
/*      */         }
/*  977 */         byte ctype = 0;
/*  978 */         int switchi = Server.rand.nextInt(40);
/*  979 */         if (switchi == 0) {
/*  980 */           ctype = 99;
/*  981 */         } else if (switchi == 1) {
/*  982 */           ctype = 1;
/*  983 */         } else if (switchi == 2) {
/*  984 */           ctype = 4;
/*  985 */         } else if (switchi == 4) {
/*  986 */           ctype = 11;
/*      */         } 
/*  988 */         Zones.flash(tilex, tiley, false);
/*  989 */         Creature.doNew(ctemplate.getTemplateId(), false, (tilex * 4 + 2), (tiley * 4 + 2), rand.nextFloat() * 360.0F, 0, ctemplate
/*  990 */             .getName(), sex, friendlyKingdom, ctype, false);
/*      */       }
/*  992 */       catch (Exception ex) {
/*      */         
/*  994 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadEffects() {
/* 1001 */     Connection dbcon = null;
/* 1002 */     PreparedStatement ps = null;
/* 1003 */     ResultSet rs = null;
/*      */     
/* 1005 */     if (Servers.localServer.PVPSERVER && !Servers.localServer.HOMESERVER) {
/*      */ 
/*      */       
/*      */       try {
/* 1009 */         dbcon = DbConnector.getDeityDbCon();
/* 1010 */         ps = dbcon.prepareStatement("SELECT * FROM KINGDOMEFFECTS");
/* 1011 */         rs = ps.executeQuery();
/* 1012 */         int found = 0;
/*      */         
/* 1014 */         while (rs.next()) {
/*      */           
/* 1016 */           int effect = rs.getInt("EFFECT");
/* 1017 */           byte kingdomId = rs.getByte("KINGDOM");
/* 1018 */           implementEffectControl(effect, kingdomId);
/* 1019 */           found++;
/*      */         } 
/* 1021 */         if (found == 0)
/*      */         {
/* 1023 */           createEffects();
/*      */         }
/*      */       }
/* 1026 */       catch (SQLException sqx) {
/*      */         
/* 1028 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1032 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 1033 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1040 */         dbcon = DbConnector.getDeityDbCon();
/* 1041 */         ps = dbcon.prepareStatement("SELECT * FROM DEITYEFFECTS");
/* 1042 */         rs = ps.executeQuery();
/* 1043 */         int found = 0;
/*      */         
/* 1045 */         while (rs.next()) {
/*      */           
/* 1047 */           int effect = rs.getInt("EFFECT");
/* 1048 */           int deityId = rs.getByte("DEITY");
/* 1049 */           implementDeityEffectControl(effect, deityId);
/* 1050 */           found++;
/*      */         } 
/* 1052 */         if (found == 0)
/*      */         {
/* 1054 */           createEffects();
/*      */         }
/*      */       }
/* 1057 */       catch (SQLException sqx) {
/*      */         
/* 1059 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1063 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 1064 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void createEffects() {
/* 1071 */     initializeEffect(3);
/* 1072 */     initializeEffect(4);
/* 1073 */     initializeEffect(1);
/* 1074 */     initializeEffect(2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getKingdomTemplateWithSpeedBonus() {
/* 1079 */     return kingdomTemplateWithSpeedBonus;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getKingdomTemplateWithCombatRating() {
/* 1084 */     return kingdomTemplateWithCombatRating;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getKingdomTemplateWithStaminaRegain() {
/* 1089 */     return kingdomTemplateWithStaminaRegain;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getKingdomTemplateWithFavorGain() {
/* 1094 */     return kingdomTemplateWithFavorGain;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void removeEffectFromPlayersWithKingdom(int effectId, int deityId) {
/* 1099 */     byte kingdomTemplate = Deities.getFavoredKingdom(deityId);
/* 1100 */     Player[] players = Players.getInstance().getPlayers();
/* 1101 */     for (Player p : players) {
/*      */       
/* 1103 */       if (p.getKingdomTemplateId() == kingdomTemplate)
/*      */       {
/* 1105 */         p.sendRemoveDeityEffectBonus(effectId);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addEffectToPlayersWithKingdom(int effectId, int deityId) {
/* 1112 */     byte kingdomTemplate = Deities.getFavoredKingdom(deityId);
/* 1113 */     Player[] players = Players.getInstance().getPlayers();
/* 1114 */     for (Player p : players) {
/*      */       
/* 1116 */       if (p.getKingdomTemplateId() == kingdomTemplate)
/*      */       {
/* 1118 */         p.sendAddDeityEffectBonus(effectId);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void implementEffectControl(int effectId, int kingdomTemplateId) {
/* 1125 */     switch (effectId) {
/*      */       
/*      */       case 3:
/* 1128 */         if (kingdomTemplateWithStaminaRegain != 0 && kingdomTemplateWithStaminaRegain != kingdomTemplateId)
/* 1129 */           removeEffectFromPlayersWithKingdom(effectId, kingdomTemplateId); 
/* 1130 */         kingdomTemplateWithStaminaRegain = kingdomTemplateId;
/* 1131 */         if (kingdomTemplateWithStaminaRegain != 0)
/* 1132 */           addEffectToPlayersWithKingdom(3, kingdomTemplateWithStaminaRegain); 
/*      */         break;
/*      */       case 4:
/* 1135 */         if (kingdomTemplateWithFavorGain != 0 && kingdomTemplateWithFavorGain != kingdomTemplateId)
/* 1136 */           removeEffectFromPlayersWithKingdom(effectId, kingdomTemplateId); 
/* 1137 */         kingdomTemplateWithFavorGain = kingdomTemplateId;
/* 1138 */         if (kingdomTemplateWithFavorGain != 0)
/* 1139 */           addEffectToPlayersWithKingdom(4, kingdomTemplateWithFavorGain); 
/*      */         break;
/*      */       case 1:
/* 1142 */         if (kingdomTemplateWithSpeedBonus != 0 && kingdomTemplateWithSpeedBonus != kingdomTemplateId)
/* 1143 */           removeEffectFromPlayersWithKingdom(effectId, kingdomTemplateId); 
/* 1144 */         kingdomTemplateWithSpeedBonus = kingdomTemplateId;
/* 1145 */         if (kingdomTemplateWithSpeedBonus != 0)
/* 1146 */           addEffectToPlayersWithKingdom(1, kingdomTemplateWithSpeedBonus); 
/*      */         break;
/*      */       case 2:
/* 1149 */         if (kingdomTemplateWithCombatRating != 0 && kingdomTemplateWithCombatRating != kingdomTemplateId)
/* 1150 */           removeEffectFromPlayersWithKingdom(effectId, kingdomTemplateId); 
/* 1151 */         kingdomTemplateWithCombatRating = kingdomTemplateId;
/* 1152 */         if (kingdomTemplateWithCombatRating != 0) {
/* 1153 */           addEffectToPlayersWithKingdom(2, kingdomTemplateWithCombatRating);
/*      */         }
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void initializeEffect(int effectId) {
/* 1163 */     Connection dbcon = null;
/* 1164 */     PreparedStatement ps = null;
/*      */     
/* 1166 */     if (Servers.localServer.PVPSERVER && !Servers.localServer.HOMESERVER) {
/*      */ 
/*      */       
/*      */       try {
/* 1170 */         dbcon = DbConnector.getDeityDbCon();
/* 1171 */         ps = dbcon.prepareStatement("INSERT INTO KINGDOMEFFECTS (EFFECT,KINGDOM) VALUES(?,?)");
/* 1172 */         ps.setInt(1, effectId);
/* 1173 */         ps.setByte(2, (byte)0);
/* 1174 */         ps.executeUpdate();
/*      */       }
/* 1176 */       catch (SQLException sqx) {
/*      */         
/* 1178 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1182 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1183 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1190 */         dbcon = DbConnector.getDeityDbCon();
/* 1191 */         ps = dbcon.prepareStatement("INSERT INTO DEITYEFFECTS (EFFECT,DEITY) VALUES(?,?)");
/* 1192 */         ps.setInt(1, effectId);
/* 1193 */         ps.setInt(2, 0);
/* 1194 */         ps.executeUpdate();
/*      */       }
/* 1196 */       catch (SQLException sqx) {
/*      */         
/* 1198 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1202 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1203 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getDeityWithSpeedBonus() {
/* 1210 */     return deityWithSpeedBonus;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getDeityWithCombatRating() {
/* 1215 */     return deityWithCombatRating;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getDeityWithStaminaRegain() {
/* 1220 */     return deityWithStaminaRegain;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getDeityWithFavorGain() {
/* 1225 */     return deityWithFavorGain;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void removeEffectFromPlayersWithDeity(int effectId, int deityId) {
/* 1230 */     Player[] players = Players.getInstance().getPlayers();
/* 1231 */     for (Player p : players) {
/*      */       
/* 1233 */       if (p.getDeity() != null && (p.getDeity()).number == deityId)
/*      */       {
/* 1235 */         p.sendRemoveDeityEffectBonus(effectId);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addEffectToPlayersWithDeity(int effectId, int deityId) {
/* 1242 */     Player[] players = Players.getInstance().getPlayers();
/* 1243 */     for (Player p : players) {
/*      */       
/* 1245 */       if (p.getDeity() != null && (p.getDeity()).number == deityId)
/*      */       {
/* 1247 */         p.sendAddDeityEffectBonus(effectId);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void implementDeityEffectControl(int effectId, int deityId) {
/* 1255 */     if (!Servers.localServer.PVPSERVER || Servers.localServer.HOMESERVER)
/*      */     {
/* 1257 */       switch (effectId) {
/*      */         
/*      */         case 3:
/* 1260 */           if (deityWithStaminaRegain != 0 && deityWithStaminaRegain != deityId)
/* 1261 */             removeEffectFromPlayersWithDeity(effectId, deityId); 
/* 1262 */           deityWithStaminaRegain = deityId;
/* 1263 */           if (deityWithStaminaRegain != 0)
/* 1264 */             addEffectToPlayersWithDeity(3, deityWithStaminaRegain); 
/*      */           break;
/*      */         case 4:
/* 1267 */           if (deityWithFavorGain != 0 && deityWithFavorGain != deityId)
/* 1268 */             removeEffectFromPlayersWithDeity(effectId, deityId); 
/* 1269 */           deityWithFavorGain = deityId;
/* 1270 */           if (deityWithFavorGain != 0)
/* 1271 */             addEffectToPlayersWithDeity(4, deityWithFavorGain); 
/*      */           break;
/*      */         case 1:
/* 1274 */           if (deityWithSpeedBonus != 0 && deityWithSpeedBonus != deityId)
/* 1275 */             removeEffectFromPlayersWithDeity(effectId, deityId); 
/* 1276 */           deityWithSpeedBonus = deityId;
/* 1277 */           if (deityWithSpeedBonus != 0)
/* 1278 */             addEffectToPlayersWithDeity(1, deityWithSpeedBonus); 
/*      */           break;
/*      */         case 2:
/* 1281 */           if (deityWithCombatRating != 0 && deityWithCombatRating != deityId)
/* 1282 */             removeEffectFromPlayersWithDeity(effectId, deityId); 
/* 1283 */           deityWithCombatRating = deityId;
/* 1284 */           if (deityWithCombatRating != 0) {
/* 1285 */             addEffectToPlayersWithDeity(2, deityWithCombatRating);
/*      */           }
/*      */           break;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setEffectController(int effectId, long deityId) {
/* 1296 */     Connection dbcon = null;
/* 1297 */     PreparedStatement ps = null;
/*      */     
/* 1299 */     if (Servers.localServer.PVPSERVER && !Servers.localServer.HOMESERVER) {
/*      */       
/* 1301 */       byte kingdomId = Deities.getFavoredKingdom((int)deityId);
/*      */       
/*      */       try {
/* 1304 */         dbcon = DbConnector.getDeityDbCon();
/* 1305 */         ps = dbcon.prepareStatement("UPDATE KINGDOMEFFECTS SET KINGDOM=? WHERE EFFECT=?");
/* 1306 */         ps.setByte(1, kingdomId);
/* 1307 */         ps.setInt(2, effectId);
/* 1308 */         ps.executeUpdate();
/*      */       }
/* 1310 */       catch (SQLException sqx) {
/*      */         
/* 1312 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1316 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1317 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1319 */       implementEffectControl(effectId, kingdomId);
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1325 */         dbcon = DbConnector.getDeityDbCon();
/* 1326 */         ps = dbcon.prepareStatement("UPDATE DEITYEFFECTS SET DEITY=? WHERE EFFECT=?");
/* 1327 */         ps.setLong(1, deityId);
/* 1328 */         ps.setInt(2, effectId);
/* 1329 */         ps.executeUpdate();
/*      */       }
/* 1331 */       catch (SQLException sqx) {
/*      */         
/* 1333 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1337 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1338 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1340 */       implementDeityEffectControl(effectId, (int)deityId);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\Effectuator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */