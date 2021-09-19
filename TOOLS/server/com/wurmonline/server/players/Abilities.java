/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.BitSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Abilities
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*   61 */   private static final String[] abilityDescs = new String[64];
/*      */   
/*   63 */   private static final Logger logger = Logger.getLogger(Abilities.class.getName());
/*      */ 
/*      */   
/*      */   static {
/*   67 */     initialiseAbilities();
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
/*      */   static void initialiseAbilities() {
/*   80 */     for (int x = 0; x < 64; x++) {
/*      */       
/*   82 */       abilityDescs[x] = "";
/*   83 */       if (x == 1) {
/*   84 */         abilityDescs[x] = "Witch";
/*   85 */       } else if (x == 2) {
/*   86 */         abilityDescs[x] = "Hag";
/*   87 */       } else if (x == 3) {
/*   88 */         abilityDescs[x] = "Crone";
/*   89 */       } else if (x == 4) {
/*   90 */         abilityDescs[x] = "Night Hag";
/*   91 */       } else if (x == 5) {
/*   92 */         abilityDescs[x] = "Enchantress";
/*   93 */       } else if (x == 6) {
/*   94 */         abilityDescs[x] = "Norn";
/*   95 */       } else if (x == 11) {
/*   96 */         abilityDescs[x] = "Siren";
/*   97 */       } else if (x == 8) {
/*   98 */         abilityDescs[x] = "Mesmeriser";
/*   99 */       } else if (x == 9) {
/*  100 */         abilityDescs[x] = "Soothsayer";
/*  101 */       } else if (x == 10) {
/*  102 */         abilityDescs[x] = "Medium";
/*  103 */       } else if (x == 7) {
/*  104 */         abilityDescs[x] = "Fortune Teller";
/*  105 */       } else if (x == 12) {
/*  106 */         abilityDescs[x] = "Diviner";
/*  107 */       } else if (x == 13) {
/*  108 */         abilityDescs[x] = "Inquisitor";
/*  109 */       } else if (x == 14) {
/*  110 */         abilityDescs[x] = "Witch Doctor";
/*  111 */       } else if (x == 15) {
/*  112 */         abilityDescs[x] = "Necromancer";
/*  113 */       } else if (x == 16) {
/*  114 */         abilityDescs[x] = "Occultist";
/*  115 */       } else if (x == 17) {
/*  116 */         abilityDescs[x] = "Death Knight";
/*  117 */       } else if (x == 18) {
/*  118 */         abilityDescs[x] = "Diabolist";
/*  119 */       } else if (x == 19) {
/*  120 */         abilityDescs[x] = "Hypnotist";
/*  121 */       } else if (x == 20) {
/*  122 */         abilityDescs[x] = "Evocator";
/*  123 */       } else if (x == 21) {
/*  124 */         abilityDescs[x] = "Thaumaturg";
/*  125 */       } else if (x == 22) {
/*  126 */         abilityDescs[x] = "Warlock";
/*  127 */       } else if (x == 23) {
/*  128 */         abilityDescs[x] = "Magician";
/*  129 */       } else if (x == 24) {
/*  130 */         abilityDescs[x] = "Conjurer";
/*  131 */       } else if (x == 25) {
/*  132 */         abilityDescs[x] = "Magus";
/*  133 */       } else if (x == 26) {
/*  134 */         abilityDescs[x] = "Arch Mage";
/*  135 */       } else if (x == 27) {
/*  136 */         abilityDescs[x] = "Witch Hunter";
/*  137 */       } else if (x == 28) {
/*  138 */         abilityDescs[x] = "Wizard";
/*  139 */       } else if (x == 29) {
/*  140 */         abilityDescs[x] = "Summoner";
/*  141 */       } else if (x == 30) {
/*  142 */         abilityDescs[x] = "Spellbinder";
/*  143 */       } else if (x == 31) {
/*  144 */         abilityDescs[x] = "Illusionist";
/*  145 */       } else if (x == 32) {
/*  146 */         abilityDescs[x] = "Enchanter";
/*  147 */       } else if (x == 33) {
/*  148 */         abilityDescs[x] = "Druid";
/*  149 */       } else if (x == 34) {
/*  150 */         abilityDescs[x] = "Sorceror";
/*  151 */       } else if (x == 35) {
/*  152 */         abilityDescs[x] = "Sorceress";
/*  153 */       } else if (x == 36) {
/*  154 */         abilityDescs[x] = "Demon Queen";
/*  155 */       } else if (x == 37) {
/*  156 */         abilityDescs[x] = "Mage";
/*  157 */       } else if (x == 38) {
/*  158 */         abilityDescs[x] = "Shadowmage";
/*  159 */       } else if (x == 39) {
/*  160 */         abilityDescs[x] = "Ascended";
/*  161 */       } else if (x == 40) {
/*  162 */         abilityDescs[x] = "Planeswalker";
/*  163 */       } else if (x == 41) {
/*  164 */         abilityDescs[x] = "Worgmaster";
/*  165 */       } else if (x == 42) {
/*  166 */         abilityDescs[x] = "Valkyrie";
/*  167 */       } else if (x == 43) {
/*  168 */         abilityDescs[x] = "Berserker";
/*  169 */       } else if (x == 44) {
/*  170 */         abilityDescs[x] = "Incinerator";
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static BitSet setTraitBits(long bits, BitSet toSet) {
/*  179 */     for (int x = 0; x < 64; x++) {
/*      */       
/*  181 */       if (x == 0) {
/*      */         
/*  183 */         if ((bits & 0x1L) == 1L) {
/*  184 */           toSet.set(x, true);
/*      */         } else {
/*  186 */           toSet.set(x, false);
/*      */         }
/*      */       
/*      */       }
/*  190 */       else if ((bits >> x & 0x1L) == 1L) {
/*  191 */         toSet.set(x, true);
/*      */       } else {
/*  193 */         toSet.set(x, false);
/*      */       } 
/*      */     } 
/*  196 */     return toSet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long getTraitBits(BitSet bitsprovided) {
/*  204 */     long ret = 0L;
/*  205 */     for (int x = 0; x <= 64; x++) {
/*      */       
/*  207 */       if (bitsprovided.get(x))
/*  208 */         ret += (1 << x); 
/*      */     } 
/*  210 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getAbilityString(int ability) {
/*  215 */     if (ability >= 0 && ability < 64)
/*  216 */       return abilityDescs[ability]; 
/*  217 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isWitch(Creature creature) {
/*  222 */     return creature.hasAbility(1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isHag(Creature creature) {
/*  227 */     return creature.hasAbility(2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isCrone(Creature creature) {
/*  232 */     return creature.hasAbility(3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isNightHag(Creature creature) {
/*  237 */     return creature.hasAbility(4);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isEnchantress(Creature creature) {
/*  242 */     return creature.hasAbility(5);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isNorn(Creature creature) {
/*  247 */     return creature.hasAbility(6);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isSiren(Creature creature) {
/*  252 */     return creature.hasAbility(11);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isMesmeriser(Creature creature) {
/*  257 */     return creature.hasAbility(8);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isSoothSayer(Creature creature) {
/*  262 */     return creature.hasAbility(9);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isMedium(Creature creature) {
/*  267 */     return creature.hasAbility(10);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isFortuneTeller(Creature creature) {
/*  272 */     return creature.hasAbility(7);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isDiviner(Creature creature) {
/*  277 */     return creature.hasAbility(12);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isInquisitor(Creature creature) {
/*  282 */     return creature.hasAbility(13);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isIncinerator(Creature creature) {
/*  287 */     return creature.hasAbility(44);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isWitchDoctor(Creature creature) {
/*  292 */     return creature.hasAbility(14);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isNecromancer(Creature creature) {
/*  297 */     return creature.hasAbility(15);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isOccultist(Creature creature) {
/*  302 */     return creature.hasAbility(16);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isDeathKnight(Creature creature) {
/*  307 */     return creature.hasAbility(17);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isDiabolist(Creature creature) {
/*  312 */     return creature.hasAbility(18);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isHypnostist(Creature creature) {
/*  317 */     return creature.hasAbility(19);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isEvocator(Creature creature) {
/*  322 */     return creature.hasAbility(20);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isThaumaturg(Creature creature) {
/*  327 */     return creature.hasAbility(21);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isWarlock(Creature creature) {
/*  332 */     return creature.hasAbility(22);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isMagician(Creature creature) {
/*  337 */     return creature.hasAbility(23);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isConjurer(Creature creature) {
/*  342 */     return creature.hasAbility(24);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isMagus(Creature creature) {
/*  347 */     return creature.hasAbility(25);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isArchmage(Creature creature) {
/*  352 */     return creature.hasAbility(26);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isWitchHunter(Creature creature) {
/*  357 */     return creature.hasAbility(27);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isWizard(Creature creature) {
/*  362 */     return creature.hasAbility(28);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isSummoner(Creature creature) {
/*  367 */     return creature.hasAbility(29);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isSpellbinder(Creature creature) {
/*  372 */     return creature.hasAbility(30);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isIllusionist(Creature creature) {
/*  377 */     return creature.hasAbility(31);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isEnchanter(Creature creature) {
/*  382 */     return creature.hasAbility(32);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isDruid(Creature creature) {
/*  387 */     return creature.hasAbility(33);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isSorceror(Creature creature) {
/*  392 */     return creature.hasAbility(34);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isSorceress(Creature creature) {
/*  397 */     return creature.hasAbility(35);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isDemonQueen(Creature creature) {
/*  402 */     return creature.hasAbility(36);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isMage(Creature creature) {
/*  407 */     return creature.hasAbility(37);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isShadowMage(Creature creature) {
/*  412 */     return creature.hasAbility(38);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isAscended(Creature creature) {
/*  417 */     return creature.hasAbility(39);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isPlanesWalker(Creature creature) {
/*  422 */     return creature.hasAbility(40);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isWorgMaster(Creature creature) {
/*  427 */     return creature.hasAbility(41);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isValkyrie(Creature creature) {
/*  432 */     return creature.hasAbility(42);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isBerserker(Creature creature) {
/*  437 */     return creature.hasAbility(43);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean ascend(Creature responder, byte deityId, int data) {
/*  442 */     LoginServerWebConnection lsw = new LoginServerWebConnection();
/*      */     
/*      */     try {
/*  445 */       Players.getInstance().sendGlobalNonPersistantTimedEffect(0L, (short)19, responder.getTileX(), responder.getTileY(), responder
/*  446 */           .getPositionZ(), System.currentTimeMillis() + 600000L);
/*  447 */       Players.getInstance().sendGlobalNonPersistantTimedEffect(0L, (short)4, responder.getTileX(), responder.getTileY(), responder
/*  448 */           .getPositionZ(), System.currentTimeMillis() + 600000L);
/*  449 */       Players.getInstance().sendGlobalNonPersistantTimedEffect(0L, (short)22, responder.getTileX(), responder.getTileY(), responder
/*  450 */           .getPositionZ(), System.currentTimeMillis() + 600000L);
/*  451 */       responder.getMusicPlayer().checkMUSIC_UNLIMITED_SND();
/*      */     }
/*  453 */     catch (Exception ex) {
/*      */       
/*  455 */       logger.log(Level.WARNING, responder.getName(), ex.getMessage());
/*      */     } 
/*  457 */     responder.setAbility(39, true);
/*  458 */     for (int x = 0; x < 3; x++) {
/*      */       
/*  460 */       int num = Server.rand.nextInt(8);
/*  461 */       if (responder.getSex() == 0) {
/*      */         
/*  463 */         switch (num) {
/*      */           
/*      */           case 0:
/*  466 */             responder.setAbility(15, true);
/*      */             break;
/*      */           case 1:
/*  469 */             responder.setAbility(41, true);
/*      */             break;
/*      */           case 2:
/*  472 */             responder.setAbility(16, true);
/*      */             break;
/*      */           case 3:
/*  475 */             responder.setAbility(29, true);
/*      */             break;
/*      */           case 4:
/*  478 */             responder.setAbility(33, true);
/*      */             break;
/*      */           case 5:
/*  481 */             responder.setAbility(12, true);
/*      */             break;
/*      */           case 6:
/*  484 */             responder.setAbility(30, true);
/*      */             break;
/*      */           case 7:
/*  487 */             responder.setAbility(13, true);
/*      */             break;
/*      */           default:
/*  490 */             assert false : num + " is not possible";
/*      */             break;
/*      */         } 
/*      */       
/*      */       } else {
/*  495 */         switch (num) {
/*      */           
/*      */           case 0:
/*  498 */             responder.setAbility(1, true);
/*      */             break;
/*      */           case 1:
/*  501 */             responder.setAbility(3, true);
/*      */             break;
/*      */           case 2:
/*  504 */             responder.setAbility(42, true);
/*      */             break;
/*      */           case 3:
/*  507 */             responder.setAbility(10, true);
/*      */             break;
/*      */           case 4:
/*  510 */             responder.setAbility(33, true);
/*      */             break;
/*      */           case 5:
/*  513 */             responder.setAbility(35, true);
/*      */             break;
/*      */           case 6:
/*  516 */             responder.setAbility(11, true);
/*      */             break;
/*      */           case 7:
/*  519 */             responder.setAbility(2, true);
/*      */             break;
/*      */           default:
/*  522 */             assert false : num + " is not possible"; break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  526 */     if (data == 577)
/*  527 */       responder.achievement(322); 
/*  528 */     HistoryManager.addHistory(responder.getName(), "has ascended to immortality as a demigod!");
/*  529 */     Server.getInstance().broadCastSafe(responder.getName() + " has ascended to immortality as a demigod!");
/*      */     
/*  531 */     Skills s = responder.getSkills();
/*  532 */     float bodyStr = (float)(s.getSkillOrLearn(102).getKnowledge(0.0D) - 20.0D);
/*  533 */     float bodySta = (float)(s.getSkillOrLearn(103).getKnowledge(0.0D) - 20.0D);
/*  534 */     float bodyCon = (float)(s.getSkillOrLearn(104).getKnowledge(0.0D) - 20.0D);
/*  535 */     float mindLog = (float)(s.getSkillOrLearn(100).getKnowledge(0.0D) - 20.0D);
/*  536 */     float mindSpe = (float)(s.getSkillOrLearn(101).getKnowledge(0.0D) - 20.0D);
/*  537 */     float soulStr = (float)(s.getSkillOrLearn(105).getKnowledge(0.0D) - 20.0D);
/*  538 */     float soulDep = (float)(s.getSkillOrLearn(106).getKnowledge(0.0D) - 20.0D);
/*      */     
/*  540 */     responder.getCommunicator().sendNormalServerMessage(lsw
/*  541 */         .ascend(Deities.getNextDeityNum(), responder.getName(), responder.getWurmId(), deityId, responder.getSex(), (byte)2, bodyStr, bodySta, bodyCon, mindLog, mindSpe, soulStr, soulDep));
/*      */ 
/*      */     
/*  544 */     logger.log(Level.INFO, responder.getName() + " ascends to demigod!");
/*  545 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isBlack(int ability) {
/*  550 */     switch (ability) {
/*      */ 
/*      */       
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 13:
/*      */       case 15:
/*      */       case 16:
/*  559 */         return true;
/*      */     } 
/*  561 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean isRed(int ability) {
/*  567 */     switch (ability) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 20:
/*      */       case 24:
/*      */       case 34:
/*      */       case 35:
/*      */       case 44:
/*  580 */         return true;
/*      */     } 
/*  582 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean isOther(int ability) {
/*  588 */     switch (ability) {
/*      */ 
/*      */       
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 27:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*  604 */         return true;
/*      */     } 
/*  606 */     return false;
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
/*      */   public static final int getNewAbilityTitle(Creature performer) {
/*  619 */     int black = 0;
/*  620 */     int red = 0;
/*  621 */     int rest = 0;
/*  622 */     int nums = 0;
/*  623 */     int lastAbility = 0;
/*  624 */     for (int x = 0; x <= 44; x++) {
/*      */       
/*  626 */       if (performer.hasAbility(x)) {
/*      */         
/*  628 */         nums++;
/*  629 */         if (isBlack(x)) {
/*  630 */           black++;
/*  631 */         } else if (isRed(x)) {
/*  632 */           red++;
/*  633 */         } else if (isOther(x)) {
/*  634 */           rest++;
/*  635 */         }  lastAbility = x;
/*      */       } 
/*      */     } 
/*  638 */     if (nums <= 1)
/*  639 */       return lastAbility; 
/*  640 */     boolean isBlack = false;
/*  641 */     boolean isRed = false;
/*  642 */     boolean isOther = false;
/*  643 */     if (isMayorBlack(black, red, rest)) {
/*  644 */       isBlack = true;
/*  645 */     } else if (isMayorRed(black, red, rest)) {
/*  646 */       isRed = true;
/*  647 */     } else if (isMayorRest(black, red, rest)) {
/*  648 */       isOther = true;
/*      */     } 
/*  650 */     if (nums <= 5 && performer.hasAbility(39))
/*      */     {
/*  652 */       return 39;
/*      */     }
/*      */     
/*  655 */     if (nums > 9 && performer.hasAbility(39)) {
/*      */       
/*  657 */       performer.achievement(328);
/*  658 */       return 40;
/*      */     } 
/*      */     
/*  661 */     if (nums >= 9) {
/*      */       
/*  663 */       performer.achievement(327);
/*  664 */       return 26;
/*      */     } 
/*  666 */     if (nums >= 6) {
/*      */       
/*  668 */       if (isBlack) {
/*      */         
/*  670 */         performer.achievement(329);
/*  671 */         return 38;
/*      */       } 
/*      */ 
/*      */       
/*  675 */       performer.achievement(330);
/*  676 */       return 23;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  682 */     if (isBlack) {
/*      */       
/*  684 */       if (nums == 4) {
/*      */         
/*  686 */         performer.achievement(331);
/*  687 */         return 18;
/*      */       } 
/*  689 */       if (black == 2 || black == 3) {
/*      */         
/*  691 */         if (performer.getSex() == 1) {
/*      */           
/*  693 */           if (black == 2)
/*  694 */             return 4; 
/*  695 */           if (black == 3) {
/*  696 */             return 36;
/*      */           }
/*      */         } else {
/*      */           
/*  700 */           if (black == 2)
/*  701 */             return 14; 
/*  702 */           if (black == 3) {
/*  703 */             return 17;
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*  708 */         performer.achievement(329);
/*  709 */         return 38;
/*      */       } 
/*      */     } else {
/*  712 */       if (isRed) {
/*      */         
/*  714 */         if (red == 2)
/*  715 */           return 21; 
/*  716 */         if (red == 3) {
/*  717 */           return 22;
/*      */         }
/*      */         
/*  720 */         performer.achievement(332);
/*  721 */         return 25;
/*      */       } 
/*      */       
/*  724 */       if (isOther) {
/*      */         
/*  726 */         if (rest == 2) {
/*      */           
/*  728 */           if (performer.getSex() == 1) {
/*  729 */             return 5;
/*      */           }
/*  731 */           return 28;
/*      */         } 
/*  733 */         if (rest == 3) {
/*      */           
/*  735 */           performer.achievement(333);
/*  736 */           return 37;
/*      */         } 
/*      */ 
/*      */         
/*  740 */         performer.achievement(333);
/*  741 */         return 37;
/*      */       } 
/*      */       
/*  744 */       if (nums > 3)
/*  745 */         return 19; 
/*  746 */     }  return performer.getAbilityTitleVal();
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isMayorBlack(int black, int red, int rest) {
/*  751 */     return (black > red && black > rest);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isMayorRed(int black, int red, int rest) {
/*  756 */     return (red > black && red > rest);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isMayorRest(int black, int red, int rest) {
/*  761 */     return (rest > black && rest > red);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean alreadyHasAbilityForItem(Item item, Creature performer) {
/*  766 */     return performer.hasAbility(getAbilityForItem(item.getTemplateId(), performer));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getAbilityForItem(int itemTemplate, Creature performer) {
/*  773 */     switch (itemTemplate)
/*      */     
/*      */     { case 808:
/*  776 */         if (performer.getSex() == 1) {
/*  777 */           ability = 1;
/*      */         } else {
/*  779 */           ability = 15;
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
/*  863 */         return ability;case 796: if (performer.getSex() == 1) { ability = 3; } else { ability = 16; }  return ability;case 806: if (performer.getSex() == 1) { ability = 2; } else { ability = 13; }  return ability;case 809: if (performer.getSex() == 1) { ability = 12; } else { ability = 31; }  return ability;case 797: if (performer.getSex() == 1) { ability = 9; } else { ability = 27; }  return ability;case 799: if (performer.getSex() == 1) { ability = 11; } else { ability = 30; }  return ability;case 810: if (performer.getSex() == 1) { ability = 10; } else { ability = 29; }  return ability;case 801: if (performer.getSex() == 1) { ability = 7; } else { ability = 20; }  return ability;case 798: if (performer.getSex() == 1) { ability = 35; } else { ability = 34; }  return ability;case 800: if (performer.getSex() == 1) { ability = 6; } else { ability = 32; }  return ability;case 795: if (performer.getSex() == 1) { ability = 8; } else { ability = 24; }  return ability;case 803: ability = 33; return ability;case 807: ability = 41; return ability;case 804: ability = 44; return ability;case 802: if (performer.getSex() == 1) { ability = 42; } else { ability = 43; }  return ability;case 794: ability = 39; return ability; }  byte ability = 0; return ability;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean useItem(Creature performer, Item item, Action act, float counter) {
/*  868 */     if (!item.isAbility()) {
/*      */       
/*  870 */       performer.getCommunicator().sendNormalServerMessage("The " + item
/*  871 */           .getName() + " makes no sense to you.");
/*  872 */       return true;
/*      */     } 
/*  874 */     if (item.isStreetLamp()) {
/*      */       
/*  876 */       performer.getCommunicator().sendNormalServerMessage("You need to plant the " + item
/*  877 */           .getName() + " for it to have effect.");
/*  878 */       return true;
/*      */     } 
/*  880 */     if (alreadyHasAbilityForItem(item, performer)) {
/*      */       
/*  882 */       performer.getCommunicator().sendNormalServerMessage("You already know the secrets that the " + item
/*  883 */           .getName() + " contains.");
/*  884 */       return true;
/*      */     } 
/*  886 */     if (item.getAuxData() >= 3 && item.getTemplateId() != 794) {
/*      */       
/*  888 */       performer.getCommunicator().sendNormalServerMessage("The " + item
/*  889 */           .getName() + " is all used up.");
/*  890 */       return true;
/*      */     } 
/*  892 */     if (!isInProperLocation(item, performer))
/*      */     {
/*  894 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  898 */     int time = act.getTimeLeft();
/*  899 */     boolean toReturn = false;
/*      */     
/*  901 */     if (counter == 1.0F) {
/*      */       
/*  903 */       time = 200;
/*  904 */       if (item.getTemplateId() == 794) {
/*      */         
/*  906 */         performer.getCommunicator().sendNormalServerMessage("You try to solve the puzzle of the " + item
/*  907 */             .getName() + ".");
/*  908 */         Server.getInstance().broadCastAction(performer
/*  909 */             .getName() + " starts solving the puzzle of the " + item.getName() + ".", performer, 5);
/*      */       }
/*      */       else {
/*      */         
/*  913 */         performer.getCommunicator().sendNormalServerMessage("You start using the " + item
/*  914 */             .getName() + ".");
/*  915 */         Server.getInstance().broadCastAction(performer
/*  916 */             .getName() + " starts using " + item.getNameWithGenus() + ".", performer, 5);
/*      */       } 
/*  918 */       performer.sendActionControl(Actions.actionEntrys[118].getVerbString(), true, time);
/*  919 */       act.setTimeLeft(time);
/*      */     } 
/*  921 */     if (act.currentSecond() == 10) {
/*      */       
/*  923 */       act.setManualInvulnerable(true);
/*  924 */       sendUseMessage(item, performer);
/*  925 */       act.setManualInvulnerable(false);
/*      */     }
/*  927 */     else if (act.currentSecond() > time / 10) {
/*      */       
/*  929 */       toReturn = true;
/*  930 */       if (item.getTemplateId() != 794) {
/*  931 */         item.setAuxData((byte)(item.getAuxData() + 1));
/*      */       }
/*  933 */       if (item.getTemplateId() == 794) {
/*      */ 
/*      */         
/*      */         try {
/*  937 */           if (performer.getSkills().getSkill(100).skillCheck(25.0D, 0.0D, false, 30.0F) > 0.0D) {
/*      */             
/*  939 */             ascend(performer, item.getAuxData(), item.getData1());
/*  940 */             Items.destroyItem(item.getWurmId());
/*  941 */             performer.setAbilityTitle(getNewAbilityTitle(performer));
/*      */           } else {
/*      */             
/*  944 */             performer.getCommunicator().sendNormalServerMessage("You fail to solve the puzzle this time. You may have to train your logical skills.");
/*      */           } 
/*  946 */         } catch (NoSuchSkillException nsc) {
/*      */           
/*  948 */           performer.getSkills().learn(100, 19.0F);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  953 */         int newAbility = getAbilityForItem(item.getTemplateId(), performer);
/*  954 */         performer.setAbility(newAbility, true);
/*  955 */         performer.setAbilityTitle(getNewAbilityTitle(performer));
/*      */         
/*  957 */         sendEffectsToCreature(performer);
/*      */       } 
/*  959 */       if (performer.isFrozen())
/*  960 */         performer.toggleFrozen(performer); 
/*  961 */       if (item.getAuxData() >= 3 && item.getTemplateId() != 794) {
/*      */         
/*  963 */         performer.getCommunicator().sendNormalServerMessage("The " + item.getName() + " crumbles to dust.");
/*  964 */         Items.destroyItem(item.getWurmId());
/*      */       } 
/*      */     } 
/*  967 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void sendUseMessage(Item item, Creature performer) {
/*  973 */     switch (item.getTemplateId()) {
/*      */       
/*      */       case 808:
/*  976 */         performer.getCommunicator().sendNormalServerMessage("You stare into the darkness of the Abyss. You step into it and fall. You fall..");
/*      */         
/*  978 */         performer.setDisease((byte)50);
/*  979 */         performer.getStatus().modifyStamina(-65535.0F);
/*  980 */         performer.getStatus().modifyThirst(65535.0F);
/*  981 */         performer.getStatus().modifyHunger(65535, 0.0F);
/*      */         break;
/*      */       case 796:
/*  984 */         performer.getCommunicator().sendNormalServerMessage("The pain! The horror! The horror!");
/*  985 */         performer.getStatus().modifyStamina(-65535.0F);
/*  986 */         performer.getStatus().modifyThirst(65535.0F);
/*  987 */         performer.getStatus().modifyHunger(65535, 0.0F);
/*  988 */         performer.addWoundOfType(null, (byte)6, 21, false, 1.0F, true, 10000.0D, 10.0F, 0.0F, false, false);
/*      */         break;
/*      */       
/*      */       case 806:
/*  992 */         performer.getCommunicator().sendNormalServerMessage("You never thought that something could be this dark.. so.. vicious and hopeless..");
/*      */         
/*  994 */         performer.getStatus().modifyStamina(-65535.0F);
/*  995 */         performer.getStatus().modifyThirst(65535.0F);
/*  996 */         performer.getStatus().modifyHunger(65535, 0.0F);
/*  997 */         performer.addWoundOfType(null, (byte)6, 21, false, 1.0F, true, 10000.0D, 10.0F, 0.0F, false, false);
/*      */         break;
/*      */       
/*      */       case 809:
/* 1001 */         performer.getCommunicator().sendNormalServerMessage("The tome sparkles with weird energies!");
/*      */         
/* 1003 */         if (!performer.addWoundOfType(null, (byte)4, 14, false, 1.0F, true, 5000.0D, 0.0F, 0.0F, false, false))
/*      */         {
/* 1005 */           performer.addWoundOfType(null, (byte)4, 13, false, 1.0F, true, 5000.0D, 0.0F, 0.0F, false, false);
/*      */         }
/*      */         break;
/*      */       
/*      */       case 797:
/* 1010 */         if (!performer.addWoundOfType(null, (byte)10, 1, false, 1.0F, false, 10000.0D, 0.0F, 0.0F, false, false))
/*      */         {
/* 1012 */           performer.toggleFrozen(performer); } 
/*      */         break;
/*      */       case 799:
/* 1015 */         performer.getCommunicator().sendNormalServerMessage("The pages all seem to be water... waves.. flowing..");
/*      */         
/* 1017 */         if (performer.addWoundOfType(null, (byte)7, 2, false, 1.0F, false, 20000.0D, 0.0F, 0.0F, false, false)) {
/*      */           return;
/*      */         }
/* 1020 */         performer.toggleFrozen(performer);
/* 1021 */         performer.getStatus().modifyStamina(-65535.0F);
/* 1022 */         performer.getStatus().modifyThirst(65535.0F);
/* 1023 */         performer.getStatus().modifyHunger(65535, 0.0F);
/*      */         break;
/*      */       case 810:
/* 1026 */         performer.getCommunicator().sendNormalServerMessage("The strong light emanating from the pages make you wonder if you really read those symbols!");
/*      */         
/* 1028 */         performer.addWoundOfType(null, (byte)4, 1, false, 1.0F, false, 10000.0D, 0.0F, 0.0F, false, false);
/*      */         break;
/*      */       
/*      */       case 794:
/* 1032 */         performer.getCommunicator().sendNormalServerMessage("You get the feeling that you are dissolving!");
/*      */         
/* 1034 */         performer.toggleFrozen(performer);
/*      */         break;
/*      */       case 801:
/* 1037 */         performer.getCommunicator().sendNormalServerMessage("This cherry is the best thing you've ever tasted!");
/*      */         
/* 1039 */         performer.getStatus().refresh(0.99F, true);
/*      */         break;
/*      */       case 798:
/* 1042 */         performer.getCommunicator().sendNormalServerMessage("Your inner eye sees fires as you enter a feverish trance.");
/*      */         
/* 1044 */         performer.getStatus().refresh(0.99F, true);
/*      */         break;
/*      */       case 800:
/* 1047 */         performer.getCommunicator().sendNormalServerMessage("The cherry is pretty tasteless.");
/*      */         break;
/*      */       case 795:
/* 1050 */         performer.getCommunicator().sendNormalServerMessage("Woah! This.. is.. good!");
/* 1051 */         performer.getStatus().refresh(0.99F, true);
/*      */         break;
/*      */       case 803:
/* 1054 */         performer.getCommunicator().sendNormalServerMessage("The walnut is surprisingly sweet.");
/*      */         
/* 1056 */         performer.getStatus().refresh(0.99F, true);
/*      */         break;
/*      */       case 807:
/* 1059 */         performer.getCommunicator().sendNormalServerMessage("It's confusing. You read something about 'Source alignment, karma pulse'...");
/*      */         
/* 1061 */         if (performer.getKarma() < 100)
/* 1062 */           performer.setKarma(100); 
/*      */         break;
/*      */       case 802:
/* 1065 */         performer.getCommunicator().sendNormalServerMessage("The cherry is very bitter.");
/*      */         
/* 1067 */         CombatEngine.addWound(null, performer, (byte)1, 5, 10000.0D, 1.0F, "", null, 0.0F, 1.0F, false, false, false, false);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean isInProperLocation(Item item, Creature performer) {
/* 1077 */     if (performer.getPower() >= 2)
/* 1078 */       return true; 
/* 1079 */     boolean ok = true;
/* 1080 */     boolean onlySand = true;
/* 1081 */     boolean tree = false;
/* 1082 */     boolean water = false;
/* 1083 */     float height = 0.0F;
/* 1084 */     boolean hasAltar = false;
/* 1085 */     int tilex = performer.getTileX();
/* 1086 */     int tiley = performer.getTileY();
/* 1087 */     int sx = Zones.safeTileX(tilex - 1);
/* 1088 */     int ex = Zones.safeTileX(tilex + 1);
/* 1089 */     int sy = Zones.safeTileY(tiley - 1);
/* 1090 */     int ey = Zones.safeTileY(tiley + 1);
/* 1091 */     for (int x = sx; x <= ex; x++) {
/* 1092 */       for (int y = sy; y <= ey; y++) {
/*      */         
/* 1094 */         int tile = Server.surfaceMesh.getTile(x, y);
/* 1095 */         if (!performer.isOnSurface())
/* 1096 */           tile = Server.caveMesh.getTile(x, y); 
/* 1097 */         if (!Terraforming.isFlat(x, y, performer.isOnSurface(), 1)) {
/*      */ 
/*      */           
/* 1100 */           if (ok) {
/* 1101 */             performer.getCommunicator().sendNormalServerMessage("You need to be standing in a 3x3 flat area in order to use this.");
/*      */           }
/* 1103 */           ok = false;
/*      */         } 
/*      */         
/* 1106 */         height = Tiles.decodeHeightAsFloat(tile);
/* 1107 */         water = (height <= 0.0F && height > -1.0F);
/* 1108 */         Tiles.Tile theTile = Tiles.getTile(Tiles.decodeType(tile));
/* 1109 */         if (theTile.isNormalTree()) {
/*      */           
/* 1111 */           byte data = Tiles.decodeData(tile);
/*      */           
/* 1113 */           if (!theTile.getTreeType(data).isFruitTree()) {
/*      */ 
/*      */             
/* 1116 */             byte age = FoliageAge.getAgeAsByte(data);
/* 1117 */             if (age >= FoliageAge.MATURE_SPROUTING.getAgeId() && age <= FoliageAge.OVERAGED.getAgeId())
/* 1118 */               tree = true; 
/*      */           } 
/*      */         } 
/* 1121 */         if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1122 */           onlySand = false; 
/* 1123 */         VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/* 1124 */         if (t != null) {
/*      */           
/* 1126 */           Item[] items = t.getItems();
/* 1127 */           for (Item i : items) {
/*      */             
/* 1129 */             if (i.isAltar())
/* 1130 */               hasAltar = true; 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1134 */     }  if (onlySand) {
/*      */       
/* 1136 */       int tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex - 20), Zones.safeTileY(tiley - 20));
/* 1137 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1138 */         onlySand = false; 
/* 1139 */       tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex + 20), Zones.safeTileX(tiley + 20));
/* 1140 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1141 */         onlySand = false; 
/* 1142 */       tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex - 20), Zones.safeTileX(tiley + 20));
/* 1143 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1144 */         onlySand = false; 
/* 1145 */       tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex + 20), Zones.safeTileX(tiley - 20));
/* 1146 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1147 */         onlySand = false; 
/* 1148 */       tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex + 20), Zones.safeTileX(tiley));
/* 1149 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1150 */         onlySand = false; 
/* 1151 */       tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex - 20), Zones.safeTileX(tiley));
/* 1152 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1153 */         onlySand = false; 
/* 1154 */       tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex), Zones.safeTileX(tiley - 20));
/* 1155 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1156 */         onlySand = false; 
/* 1157 */       tile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex), Zones.safeTileX(tiley + 20));
/* 1158 */       if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SAND.id)
/* 1159 */         onlySand = false; 
/*      */     } 
/* 1161 */     if (!hasAltar) {
/*      */       
/* 1163 */       performer.getCommunicator().sendNormalServerMessage("You need to be in the vicinity of a holy altar.");
/* 1164 */       ok = false;
/*      */     } 
/* 1166 */     switch (item.getTemplateId()) {
/*      */       
/*      */       case 796:
/*      */       case 806:
/*      */       case 808:
/* 1171 */         if (performer.isOnSurface()) {
/*      */           
/* 1173 */           ok = false;
/* 1174 */           performer.getCommunicator().sendNormalServerMessage("You need to be in the darkness of caves, sheltered from sight.");
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 797:
/*      */       case 799:
/*      */       case 809:
/* 1181 */         if (!water) {
/*      */           
/* 1183 */           ok = false;
/* 1184 */           performer.getCommunicator().sendNormalServerMessage("You need to be standing in the cleansing shallow water.");
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 794:
/*      */       case 801:
/*      */       case 810:
/* 1191 */         if (height < 175.0F) {
/*      */           
/* 1193 */           ok = false;
/* 1194 */           performer.getCommunicator().sendNormalServerMessage("You need to be high up towards the heavens, closer to the gods.");
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 795:
/*      */       case 798:
/*      */       case 800:
/* 1201 */         if (!onlySand) {
/*      */           
/* 1203 */           ok = false;
/* 1204 */           performer.getCommunicator().sendNormalServerMessage("You need to be deep in the barren desert, where nothing ever grows.");
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 802:
/*      */       case 803:
/*      */       case 807:
/* 1211 */         if (!tree) {
/*      */           
/* 1213 */           ok = false;
/* 1214 */           performer.getCommunicator().sendNormalServerMessage("You need to be close to a strong plant, so that you may connect to its life force.");
/*      */         } 
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1221 */     return ok;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void sendEffectsToCreature(Creature c) {
/* 1226 */     if (!c.hasAnyAbility())
/*      */       return; 
/* 1228 */     if (c.getFireResistance() > 0.0F)
/* 1229 */       c.getCommunicator()
/* 1230 */         .sendAddSpellEffect(SpellEffectsEnum.FIRE_RESIST, 100000, c
/* 1231 */           .getFireResistance() * 100.0F); 
/* 1232 */     if (c.getColdResistance() > 0.0F)
/* 1233 */       c.getCommunicator()
/* 1234 */         .sendAddSpellEffect(SpellEffectsEnum.COLD_RESIST, 100000, c
/* 1235 */           .getColdResistance() * 100.0F); 
/* 1236 */     if (c.getDiseaseResistance() > 0.0F)
/* 1237 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.DISEASE_RESIST, 100000, c
/*      */           
/* 1239 */           .getDiseaseResistance() * 100.0F); 
/* 1240 */     if (c.getPhysicalResistance() > 0.0F)
/* 1241 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.PHYSICAL_RESIST, 100000, c
/* 1242 */           .getPhysicalResistance() * 100.0F); 
/* 1243 */     if (c.getPierceResistance() > 0.0F)
/* 1244 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.PIERCE_RESIST, 100000, c
/* 1245 */           .getPierceResistance() * 100.0F); 
/* 1246 */     if (c.getSlashResistance() > 0.0F)
/* 1247 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.SLASH_RESIST, 100000, c
/* 1248 */           .getSlashResistance() * 100.0F); 
/* 1249 */     if (c.getCrushResistance() > 0.0F)
/* 1250 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.CRUSH_RESIST, 100000, c
/* 1251 */           .getCrushResistance() * 100.0F); 
/* 1252 */     if (c.getBiteResistance() > 0.0F)
/* 1253 */       c.getCommunicator()
/* 1254 */         .sendAddSpellEffect(SpellEffectsEnum.BITE_RESIST, 100000, c
/* 1255 */           .getBiteResistance() * 100.0F); 
/* 1256 */     if (c.getPoisonResistance() > 0.0F)
/* 1257 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POISON_RESIST, 100000, c
/* 1258 */           .getPoisonResistance() * 100.0F); 
/* 1259 */     if (c.getWaterResistance() > 0.0F)
/* 1260 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.WATER_RESIST, 100000, c
/* 1261 */           .getWaterResistance() * 100.0F); 
/* 1262 */     if (c.getAcidResistance() > 0.0F)
/* 1263 */       c.getCommunicator()
/* 1264 */         .sendAddSpellEffect(SpellEffectsEnum.ACID_RESIST, 100000, c
/* 1265 */           .getAcidResistance() * 100.0F); 
/* 1266 */     if (c.getInternalResistance() > 0.0F) {
/* 1267 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.INTERNAL_RESIST, 100000, c
/* 1268 */           .getInternalResistance() * 100.0F);
/*      */     }
/* 1270 */     if (c.getFireVulnerability() > 0.0F)
/* 1271 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.FIRE_VULNERABILITY, 100000, -100.0F + c
/* 1272 */           .getFireVulnerability() * 100.0F); 
/* 1273 */     if (c.getColdVulnerability() > 0.0F)
/* 1274 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.COLD_VULNERABILITY, 100000, -100.0F + c
/* 1275 */           .getColdVulnerability() * 100.0F); 
/* 1276 */     if (c.getDiseaseVulnerability() > 0.0F)
/* 1277 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.DISEASE_VULNERABILITY, 100000, -100.0F + c
/* 1278 */           .getDiseaseVulnerability() * 100.0F); 
/* 1279 */     if (c.getPhysicalVulnerability() > 0.0F)
/* 1280 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.PHYSICAL_VULNERABILITY, 100000, -100.0F + c
/*      */           
/* 1282 */           .getPhysicalVulnerability() * 100.0F); 
/* 1283 */     if (c.getPierceVulnerability() > 0.0F)
/* 1284 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.PIERCE_VULNERABILITY, 100000, -100.0F + c
/* 1285 */           .getPierceVulnerability() * 100.0F); 
/* 1286 */     if (c.getSlashVulnerability() > 0.0F)
/* 1287 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.SLASH_VULNERABILITY, 100000, -100.0F + c
/* 1288 */           .getSlashVulnerability() * 100.0F); 
/* 1289 */     if (c.getCrushVulnerability() > 0.0F)
/* 1290 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.CRUSH_VULNERABILITY, 100000, -100.0F + c
/* 1291 */           .getCrushVulnerability() * 100.0F); 
/* 1292 */     if (c.getBiteVulnerability() > 0.0F)
/* 1293 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.BITE_VULNERABILITY, 100000, -100.0F + c
/* 1294 */           .getBiteVulnerability() * 100.0F); 
/* 1295 */     if (c.getPoisonVulnerability() > 0.0F)
/* 1296 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POISON_VULNERABILITY, 100000, -100.0F + c
/* 1297 */           .getPoisonVulnerability() * 100.0F); 
/* 1298 */     if (c.getWaterVulnerability() > 0.0F)
/* 1299 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.WATER_VULNERABILITY, 100000, -100.0F + c
/* 1300 */           .getWaterVulnerability() * 100.0F); 
/* 1301 */     if (c.getAcidVulnerability() > 0.0F)
/* 1302 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.ACID_VULNERABILITY, 100000, -100.0F + c
/* 1303 */           .getAcidVulnerability() * 100.0F); 
/* 1304 */     if (c.getInternalVulnerability() > 0.0F)
/* 1305 */       c.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.INTERNAL_VULNERABILITY, 100000, -100.0F + c
/*      */           
/* 1307 */           .getInternalVulnerability() * 100.0F); 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Abilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */