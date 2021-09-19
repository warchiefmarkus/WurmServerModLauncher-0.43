/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MessageServer;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.ActionEntry;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.combat.Battle;
/*      */ import com.wurmonline.server.combat.CombatConstants;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.combat.CombatMove;
/*      */ import com.wurmonline.server.combat.SpecialMove;
/*      */ import com.wurmonline.server.combat.Weapon;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemSpellEffects;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.modifiers.DoubleValueModifier;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.spells.EnchantUtil;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.spells.SpellResist;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.utils.CreatureLineSegment;
/*      */ import com.wurmonline.server.zones.VirtualZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Random;
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
/*      */ public final class CombatHandler
/*      */   implements MiscConstants, TimeConstants, CombatConstants, SoundNames, CreatureTemplateIds
/*      */ {
/*   88 */   private static final Logger logger = Logger.getLogger(CombatHandler.class.getName());
/*      */   private final Creature creature;
/*      */   private boolean addToSkills = false;
/*   91 */   public static final byte[] NO_COMBAT_OPTIONS = new byte[0];
/*   92 */   private List<ActionEntry> moveStack = null;
/*      */   private boolean turned = false;
/*   94 */   private byte currentStance = 15;
/*   95 */   private static SpecialMove[] specialmoves = null;
/*   96 */   private byte currentStrength = 1;
/*   97 */   private static final List<ActionEntry> standardDefences = new LinkedList<>();
/*      */   private static boolean hit = false;
/*      */   private static boolean miss = true;
/*      */   private static boolean crit = false;
/*  101 */   private int usedShieldThisRound = 0;
/*      */   
/*      */   private boolean receivedShieldSkill = false;
/*      */   private static boolean dead = false;
/*      */   private static boolean aiming = false;
/*  106 */   private static float chanceToHit = 0.0F;
/*  107 */   private static double attCheck = 0.0D;
/*  108 */   private static double attBonus = 0.0D;
/*  109 */   private static double defCheck = 0.0D;
/*  110 */   private static double defBonus = 0.0D;
/*  111 */   private static double damage = 0.0D;
/*  112 */   private static byte pos = 0;
/*  113 */   private static byte type = 0;
/*  114 */   private static Item defShield = null;
/*  115 */   private static Item defParryWeapon = null;
/*  116 */   private static Item defLeftWeapon = null;
/*  117 */   private static Skill defPrimWeaponSkill = null;
/*  118 */   private static Skills defenderSkills = null;
/*  119 */   private static String attString = "";
/*  120 */   private static String othersString = "";
/*  121 */   private static final List<ActionEntry> selectStanceList = new LinkedList<>();
/*      */   
/*      */   private static final String prones = "stancerebound";
/*      */   
/*      */   private static final String opens = "stanceopen";
/*      */   
/*      */   private static final String dodge = "dodge";
/*      */   
/*      */   private static final String fight = "fight";
/*      */   private static final String strike = "_strike";
/*      */   public static final float minShieldDam = 0.01F;
/*      */   private static boolean justOpen = false;
/*  133 */   private static double manouvreMod = 0.0D;
/*  134 */   private byte opportunityAttacks = 0;
/*      */   
/*      */   public static final float enemyTerritoryMod = 0.7F;
/*  137 */   private static float parryBonus = 1.0F;
/*  138 */   private byte battleratingPenalty = 0;
/*      */   
/*      */   private Set<DoubleValueModifier> parryModifiers;
/*      */   
/*      */   private Set<DoubleValueModifier> dodgeModifiers;
/*      */   
/*      */   private boolean sentAttacks = false;
/*      */   
/*      */   private static final float DODGE_MODIFIER = 3.0F;
/*      */   private boolean receivedFStyleSkill = false;
/*      */   private boolean receivedWeaponSkill = false;
/*      */   private boolean receivedSecWeaponSkill = false;
/*  150 */   private Set<Item> secattacks = null;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasSpiritFervor = false;
/*      */ 
/*      */   
/*  157 */   private int lastShieldBashed = 0;
/*      */   
/*      */   private boolean hasRodEffect = false;
/*      */   private static final float poleArmDamageBonus = 1.7F;
/*      */   
/*      */   static {
/*  163 */     standardDefences.add(Actions.actionEntrys[314]);
/*  164 */     standardDefences.add(Actions.actionEntrys[315]);
/*  165 */     standardDefences.add(Actions.actionEntrys[316]);
/*  166 */     standardDefences.add(Actions.actionEntrys[317]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float lastTimeStamp;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float lastAttackPollDelta;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float waitTime;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void resolveRound() {
/*  199 */     Players.getInstance().combatRound();
/*  200 */     Creatures.getInstance().combatRound();
/*      */   }
/*      */ 
/*      */   
/*      */   public void shieldBash() {
/*  205 */     this.lastShieldBashed = 2;
/*  206 */     this.creature.getCommunicator().sendToggleShield(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayShieldBash() {
/*  216 */     return (this.lastShieldBashed <= 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void calcAttacks(boolean newround) {
/*  221 */     if (!this.creature.isDead() && (this.moveStack == null || newround || !this.sentAttacks)) {
/*      */       
/*  223 */       if (this.moveStack == null) {
/*  224 */         this.moveStack = new LinkedList<>();
/*      */       } else {
/*  226 */         this.moveStack.clear();
/*  227 */       }  manouvreMod = (this.creature.getMovementScheme()).armourMod.getModifier();
/*      */       
/*  229 */       if (this.creature.opponent != null && this.creature.getPrimWeapon() != null) {
/*      */         
/*  231 */         float knowl = getCombatKnowledgeSkill();
/*  232 */         if (!this.creature.isPlayer())
/*  233 */           knowl += 20.0F; 
/*  234 */         if (knowl > 50.0F)
/*  235 */           this.moveStack.addAll(standardDefences); 
/*  236 */         float mycr = this.creature.getCombatHandler().getCombatRating(this.creature.opponent, this.creature.getPrimWeapon(), false);
/*      */         
/*  238 */         float oppcr = this.creature.opponent.getCombatHandler().getCombatRating(this.creature, this.creature.opponent
/*  239 */             .getPrimWeapon(), false);
/*  240 */         this.moveStack.addAll(getHighAttacks(this.creature.getPrimWeapon(), this.creature.isAutofight(), this.creature.opponent, mycr, oppcr, knowl));
/*      */         
/*  242 */         this.moveStack.addAll(getMidAttacks(this.creature.getPrimWeapon(), this.creature.isAutofight(), this.creature.opponent, mycr, oppcr, knowl));
/*      */         
/*  244 */         this.moveStack.addAll(getLowAttacks(this.creature.getPrimWeapon(), this.creature.isAutofight(), this.creature.opponent, mycr, oppcr, knowl));
/*      */       } 
/*      */       
/*  247 */       if (!this.sentAttacks || newround) {
/*      */         
/*  249 */         this.sentAttacks = true;
/*  250 */         if (!this.creature.isAutofight()) {
/*  251 */           this.creature.getCommunicator().sendCombatOptions(getOptions(this.moveStack, this.currentStance), (short)0);
/*      */         }
/*  253 */         sendSpecialMoves();
/*      */         
/*  255 */         if (this.creature.getShield() != null) {
/*      */           
/*  257 */           if (mayShieldBash()) {
/*  258 */             this.creature.getCommunicator().sendToggleShield(true);
/*      */           } else {
/*  260 */             this.creature.getCommunicator().sendToggleShield(false);
/*      */           } 
/*      */         } else {
/*  263 */           this.creature.getCommunicator().sendToggleShield(false);
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
/*      */   public float getCombatKnowledgeSkill() {
/*  276 */     float knowl = 0.0F;
/*  277 */     int primarySkill = 10052;
/*      */     
/*      */     try {
/*  280 */       if (!this.creature.getPrimWeapon().isBodyPartAttached())
/*  281 */         primarySkill = this.creature.getPrimWeapon().getPrimarySkill(); 
/*  282 */       Skill fightingSkill = this.creature.getSkills().getSkill(primarySkill);
/*  283 */       knowl = (float)fightingSkill.getKnowledge(this.creature.getPrimWeapon(), 0.0D);
/*      */     }
/*  285 */     catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  290 */     if (knowl == 0.0F && !this.creature.isPlayer()) {
/*      */       
/*  292 */       Skill unarmed = this.creature.getFightingSkill();
/*  293 */       knowl = (float)unarmed.getKnowledge(0.0D);
/*      */     } 
/*  295 */     if (this.creature.getPrimWeapon().isBodyPartAttached())
/*  296 */       knowl += this.creature.getBonusForSpellEffect((byte)24) / 5.0F; 
/*  297 */     Seat s = this.creature.getSeat();
/*  298 */     if (s != null)
/*  299 */       knowl *= s.manouvre; 
/*  300 */     if (this.creature.isOnHostileHomeServer())
/*  301 */       knowl *= 0.525F; 
/*  302 */     return knowl;
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
/*      */   private void sendSpecialMoves() {
/*  335 */     if (this.creature.combatRound > 3 && !this.creature.getPrimWeapon().isBodyPart()) {
/*      */       
/*  337 */       double fightskill = 0.0D;
/*      */       
/*      */       try {
/*  340 */         fightskill = this.creature.getSkills().getSkill(this.creature.getPrimWeapon().getPrimarySkill()).getKnowledge(0.0D);
/*  341 */         if (fightskill > 19.0D) {
/*      */           
/*  343 */           specialmoves = SpecialMove.getMovesForWeaponSkillAndStance(this.creature, this.creature.getPrimWeapon(), (int)fightskill);
/*      */           
/*  345 */           if (specialmoves.length > 0) {
/*      */             
/*  347 */             this.creature.getCommunicator().sendSpecialMove((short)-1, "");
/*  348 */             if (!this.creature.isAutofight())
/*      */             {
/*  350 */               for (int sx = 0; sx < specialmoves.length; sx++)
/*      */               {
/*  352 */                 this.creature.getCommunicator().sendSpecialMove((short)(197 + sx), specialmoves[sx]
/*  353 */                     .getName());
/*      */               }
/*      */             }
/*  356 */             selectSpecialMove();
/*      */           } else {
/*      */             
/*  359 */             this.creature.getCommunicator().sendSpecialMove((short)-1, "N/A");
/*      */           } 
/*      */         } else {
/*  362 */           this.creature.getCommunicator().sendSpecialMove((short)-1, "N/A");
/*      */         } 
/*  364 */       } catch (NoSuchSkillException nss) {
/*      */         
/*  366 */         this.creature.getCommunicator().sendSpecialMove((short)-1, "N/A");
/*      */       } 
/*      */     } else {
/*      */       
/*  370 */       this.creature.getCommunicator().sendSpecialMove((short)-1, "N/A");
/*      */     } 
/*      */   }
/*      */   
/*      */   private void selectSpecialMove() {
/*  375 */     if (this.creature.isAutofight() && Server.rand.nextInt(3) == 0) {
/*      */       
/*  377 */       int sm = Server.rand.nextInt(specialmoves.length);
/*      */       
/*      */       try {
/*  380 */         float chance = getChanceToHit(this.creature.opponent, this.creature.getPrimWeapon());
/*      */         
/*  382 */         if (chance > 50.0F)
/*      */         {
/*  384 */           if (this.creature.getStatus().getStamina() > specialmoves[sm].getStaminaCost())
/*      */           {
/*  386 */             this.creature.setAction(new Action(this.creature, -1L, this.creature.getWurmId(), (short)(197 + sm), this.creature
/*  387 */                   .getPosX(), this.creature.getPosY(), this.creature
/*  388 */                   .getPositionZ() + this.creature.getAltOffZ(), this.creature.getStatus().getRotation()));
/*      */           }
/*      */         }
/*      */       }
/*  392 */       catch (Exception fe) {
/*      */         
/*  394 */         logger.log(Level.WARNING, this.creature.getName() + " failed:" + fe.getMessage(), fe);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBattleRatingPenalty(byte penalty) {
/*  401 */     if (this.battleratingPenalty == 0)
/*  402 */       penalty = (byte)Math.max(penalty, 2); 
/*  403 */     this.battleratingPenalty = (byte)Math.min(5, this.battleratingPenalty + penalty);
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
/*      */   byte getBattleratingPenalty() {
/*  415 */     return this.battleratingPenalty;
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
/*      */   public void setCurrentStance(int actNum, byte aStance) {
/*  441 */     this.currentStance = aStance;
/*      */     
/*  443 */     if (actNum > 0) {
/*      */ 
/*      */ 
/*      */       
/*  447 */       this.creature.sendStance(this.currentStance);
/*      */     }
/*  449 */     else if (aStance == 15) {
/*      */ 
/*      */ 
/*      */       
/*  453 */       this.creature.sendStance(this.currentStance);
/*      */     }
/*  455 */     else if (aStance == 8) {
/*      */ 
/*      */ 
/*      */       
/*  459 */       this.creature.playAnimation("stancerebound", true);
/*      */     }
/*  461 */     else if (aStance == 9) {
/*      */       
/*  463 */       this.creature.getStatus().setStunned(3.0F, false);
/*  464 */       this.creature.playAnimation("stanceopen", false);
/*      */ 
/*      */     
/*      */     }
/*  468 */     else if (aStance == 0) {
/*      */ 
/*      */ 
/*      */       
/*  472 */       this.creature.sendStance(this.currentStance);
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
/*      */   public void setCurrentStance(byte aCurrentStance) {
/*  486 */     this.currentStance = aCurrentStance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getCurrentStance() {
/*  496 */     return this.currentStance;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendStanceAnimation(byte aStance, boolean attack) {
/*  501 */     if (aStance == 8) {
/*      */       
/*  503 */       this.creature.sendToLoggers(this.creature.getName() + ": " + "stancerebound", (byte)2);
/*  504 */       this.creature.playAnimation("stancerebound", false);
/*      */     }
/*  506 */     else if (aStance == 9) {
/*      */       
/*  508 */       this.creature.getStatus().setStunned(3.0F, false);
/*  509 */       this.creature.playAnimation("stanceopen", false);
/*  510 */       this.creature.sendToLoggers(this.creature.getName() + ": " + "stanceopen", (byte)2);
/*      */     }
/*      */     else {
/*      */       
/*  514 */       StringBuilder sb = new StringBuilder();
/*  515 */       sb.append("fight");
/*  516 */       if (attack)
/*      */       {
/*  518 */         if (attString.equals("hit")) {
/*  519 */           sb.append("_strike");
/*      */         } else {
/*  521 */           sb.append("_" + attString);
/*      */         }  } 
/*  523 */       if (!this.creature.isUnique() || this.creature.getHugeMoveCounter() == 2)
/*  524 */         this.creature.playAnimation(sb.toString(), !attack); 
/*  525 */       this.creature.sendToLoggers(this.creature.getName() + ": " + sb.toString(), (byte)2);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getStanceDescription(byte currentStance) {
/*  531 */     StringBuilder sb = new StringBuilder();
/*  532 */     if (isHigh(currentStance)) {
/*  533 */       sb.append("higher ");
/*  534 */     } else if (isLow(currentStance)) {
/*  535 */       sb.append("lower ");
/*      */     } else {
/*  537 */       sb.append("mid ");
/*  538 */     }  if (isLeft(currentStance)) {
/*  539 */       sb.append("left ");
/*  540 */     } else if (isRight(currentStance)) {
/*  541 */       sb.append("right ");
/*      */     } else {
/*  543 */       sb.append("center ");
/*  544 */     }  return sb.toString();
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
/*      */   private void addToList(List<ActionEntry> list, @Nullable Item weapon, short number, Creature opponent, float mycr, float oppcr, float primweaponskill) {
/*      */     float movechance;
/*  557 */     if (this.creature.isPlayer()) {
/*  558 */       movechance = getMoveChance(this.creature, weapon, this.currentStance, Actions.actionEntrys[number], mycr, oppcr, primweaponskill);
/*      */     }
/*      */     else {
/*      */       
/*  562 */       movechance = getMoveChance(this.creature, weapon, this.currentStance, Actions.actionEntrys[number], mycr, oppcr, primweaponskill);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  568 */     if (movechance > 0.0F) {
/*  569 */       list.add(new ActionEntry(number, (int)movechance + "%, " + Actions.actionEntrys[number].getActionString(), "attack"));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getAttackSkillCap(short action) {
/*  575 */     switch (action) {
/*      */       
/*      */       case 303:
/*  578 */         return 0;
/*      */       case 291:
/*  580 */         return 3;
/*      */       case 309:
/*  582 */         return 2;
/*      */       
/*      */       case 300:
/*  585 */         return 15;
/*      */       case 288:
/*  587 */         return 13;
/*      */       case 306:
/*  589 */         return 12;
/*      */       
/*      */       case 297:
/*  592 */         return 9;
/*      */       case 294:
/*  594 */         return 7;
/*      */       case 312:
/*  596 */         return 5;
/*      */     } 
/*  598 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<ActionEntry> getHighAttacks(@Nullable Item weapon, boolean auto, Creature opponent, float mycr, float oppcr, float primweaponskill) {
/*  606 */     LinkedList<ActionEntry> tempList = new LinkedList<>();
/*  607 */     if (primweaponskill > getAttackSkillCap((short)300)) {
/*  608 */       addToList(tempList, weapon, (short)300, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  613 */     if (primweaponskill > getAttackSkillCap((short)288)) {
/*  614 */       addToList(tempList, weapon, (short)288, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  619 */     if (primweaponskill > getAttackSkillCap((short)306)) {
/*  620 */       addToList(tempList, weapon, (short)306, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  625 */     if (!auto && tempList.size() > 0)
/*      */     {
/*  627 */       tempList.addFirst(new ActionEntry((short)-tempList.size(), "High", "high"));
/*      */     }
/*  629 */     return tempList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<ActionEntry> getMidAttacks(@Nullable Item weapon, boolean auto, Creature opponent, float mycr, float oppcr, float primweaponskill) {
/*  636 */     LinkedList<ActionEntry> tempList = new LinkedList<>();
/*  637 */     addToList(tempList, weapon, (short)303, opponent, mycr, oppcr, primweaponskill);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  642 */     if (primweaponskill > getAttackSkillCap((short)291)) {
/*  643 */       addToList(tempList, weapon, (short)291, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  648 */     if (primweaponskill > getAttackSkillCap((short)309)) {
/*  649 */       addToList(tempList, weapon, (short)309, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  655 */     if (!auto && tempList.size() > 0)
/*      */     {
/*  657 */       tempList.addFirst(new ActionEntry((short)-tempList.size(), "Mid", "Mid"));
/*      */     }
/*  659 */     return tempList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<ActionEntry> getLowAttacks(@Nullable Item weapon, boolean auto, Creature opponent, float mycr, float oppcr, float primweaponskill) {
/*  666 */     LinkedList<ActionEntry> tempList = new LinkedList<>();
/*  667 */     if (primweaponskill > getAttackSkillCap((short)297)) {
/*  668 */       addToList(tempList, weapon, (short)297, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  673 */     if (primweaponskill > getAttackSkillCap((short)294)) {
/*  674 */       addToList(tempList, weapon, (short)294, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  679 */     if (primweaponskill > getAttackSkillCap((short)312)) {
/*  680 */       addToList(tempList, weapon, (short)312, opponent, mycr, oppcr, primweaponskill);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  686 */     if (!auto && tempList.size() > 0)
/*      */     {
/*  688 */       tempList.addFirst(new ActionEntry((short)-tempList.size(), "Low", "Low"));
/*      */     }
/*  690 */     return tempList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float getMoveChance(Creature performer, @Nullable Item weapon, int stance, ActionEntry entry, float mycr, float oppcr, float primweaponskill) {
/*  697 */     float basechance = 100.0F - oppcr * 2.0F + mycr + primweaponskill;
/*      */     
/*  699 */     float cost = 0.0F;
/*  700 */     if (isHigh(stance)) {
/*      */       
/*  702 */       if (entry.isAttackHigh()) {
/*  703 */         cost += 5.0F;
/*  704 */       } else if (entry.isAttackLow()) {
/*  705 */         cost += 10.0F;
/*      */       } else {
/*  707 */         cost += 3.0F;
/*      */       } 
/*  709 */     } else if (isLow(stance)) {
/*      */       
/*  711 */       if (entry.isAttackHigh()) {
/*  712 */         cost += 10.0F;
/*  713 */       } else if (entry.isAttackLow()) {
/*  714 */         cost += 5.0F;
/*      */       } else {
/*  716 */         cost += 3.0F;
/*      */       }
/*      */     
/*      */     }
/*  720 */     else if (entry.isAttackHigh()) {
/*  721 */       cost += 5.0F;
/*  722 */     } else if (entry.isAttackLow()) {
/*  723 */       cost += 5.0F;
/*      */     } 
/*  725 */     if (isRight(stance)) {
/*      */       
/*  727 */       if (entry.isAttackRight()) {
/*  728 */         cost += 3.0F;
/*  729 */       } else if (entry.isAttackLeft()) {
/*  730 */         cost += 10.0F;
/*      */       } else {
/*  732 */         cost += 3.0F;
/*      */       } 
/*  734 */     } else if (isLeft(stance)) {
/*      */       
/*  736 */       if (entry.isAttackRight()) {
/*  737 */         cost += 10.0F;
/*  738 */       } else if (entry.isAttackLeft()) {
/*  739 */         cost += 3.0F;
/*      */       } else {
/*  741 */         cost += 3.0F;
/*      */       }
/*      */     
/*      */     }
/*  745 */     else if (entry.isAttackLeft()) {
/*  746 */       cost += 5.0F;
/*  747 */     } else if (entry.isAttackRight()) {
/*  748 */       cost += 5.0F;
/*      */     } else {
/*  750 */       cost += 10.0F;
/*      */     } 
/*      */ 
/*      */     
/*  754 */     if (entry.isAttackHigh() && !entry.isAttackLeft() && !entry.isAttackRight()) {
/*      */       
/*  756 */       cost += 3.0F;
/*      */     }
/*  758 */     else if (entry.isAttackLow() && !entry.isAttackLeft() && !entry.isAttackRight()) {
/*      */       
/*  760 */       cost += 3.0F;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  768 */     cost = (float)(cost * (1.0D - manouvreMod));
/*  769 */     if (weapon != null)
/*  770 */       cost += Weapon.getBaseSpeedForWeapon(weapon); 
/*  771 */     if (performer.fightlevel >= 2) {
/*  772 */       cost -= 10.0F;
/*      */     }
/*      */     
/*  775 */     return Math.min(100.0F, Math.max(0.0F, basechance - cost));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isHigh(int stance) {
/*  780 */     return (stance == 6 || stance == 1 || stance == 7);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isLow(int stance) {
/*  785 */     return (stance == 4 || stance == 3 || stance == 10 || stance == 8);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isLeft(int stance) {
/*  790 */     return (stance == 4 || stance == 5 || stance == 6);
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
/*      */   public static final boolean isRight(int stance) {
/*  802 */     return (stance == 3 || stance == 2 || stance == 1 || stance == 11);
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
/*      */   public static final boolean isCenter(int stance) {
/*  814 */     return (stance == 0 || stance == 9 || stance == 13 || stance == 14 || stance == 12);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isDefend(int stance) {
/*  820 */     return (stance == 13 || stance == 14 || stance == 12 || stance == 11);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean prerequisitesFail(Creature creature, Creature opponent, boolean opportunity, Item weapon) {
/*  827 */     return prerequisitesFail(creature, opponent, opportunity, weapon, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean prerequisitesFail(Creature creature, Creature opponent, boolean opportunity, Item weapon, boolean ignoreWeapon) {
/*  833 */     if (opponent.isDead()) {
/*      */       
/*  835 */       creature.setTarget(-10L, true);
/*  836 */       return true;
/*      */     } 
/*  838 */     if (opponent.equals(creature)) {
/*      */       
/*  840 */       if (!opportunity) {
/*      */         
/*  842 */         creature.getCommunicator().sendCombatAlertMessage("You cannot attack yourself.");
/*  843 */         creature.setOpponent(null);
/*      */       } 
/*  845 */       return true;
/*      */     } 
/*      */     
/*  848 */     if (!creature.isPlayer() && opponent.isPlayer())
/*      */     {
/*  850 */       if (creature.getHitched() != null && (creature.getHitched()).wurmid == opponent.getVehicle()) {
/*      */         
/*  852 */         creature.setOpponent(null);
/*  853 */         creature.setTarget(-10L, true);
/*  854 */         return true;
/*      */       } 
/*      */     }
/*      */     
/*  858 */     if (!opponent.isPlayer() && creature.isPlayer())
/*      */     {
/*  860 */       if (opponent.getHitched() != null && (opponent.getHitched()).wurmid == creature.getVehicle()) {
/*      */         
/*  862 */         opponent.setOpponent(null);
/*  863 */         opponent.setTarget(-10L, true);
/*  864 */         return true;
/*      */       } 
/*      */     }
/*  867 */     if (!ignoreWeapon)
/*      */     {
/*  869 */       if (weapon == null) {
/*      */         
/*  871 */         if (!opportunity) {
/*      */           
/*  873 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  874 */           segments.add(new MulticolorLineSegment("You have no weapon to attack ", (byte)0));
/*  875 */           segments.add(new CreatureLineSegment(opponent));
/*  876 */           segments.add(new MulticolorLineSegment(" with.", (byte)0));
/*      */           
/*  878 */           creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */           
/*  882 */           creature.setOpponent(null);
/*      */         } 
/*  884 */         return true;
/*      */       } 
/*      */     }
/*  887 */     if (opponent.isBridgeBlockingAttack(creature, false)) {
/*  888 */       return true;
/*      */     }
/*  890 */     if (!GeneralUtilities.mayAttackSameLevel(creature, opponent)) {
/*      */ 
/*      */       
/*  893 */       if (creature.isOnSurface()) {
/*      */         
/*  895 */         VolaTile t = Zones.getTileOrNull(creature.getTileX(), creature.getTileY(), creature.isOnSurface());
/*  896 */         if (t != null) {
/*      */           
/*  898 */           creature.sendToLoggers("Fighting " + opponent.getName() + " my z=" + creature.getPositionZ() + " opponent z=" + opponent.getPositionZ() + " structure=" + t.getStructure() + " diff=" + (Math.abs(creature.getStatus().getPositionZ() - opponent.getStatus().getPositionZ()) * 10.0F));
/*  899 */           if (t.getStructure() != null)
/*  900 */             return true; 
/*      */         } 
/*      */       } 
/*  903 */       if (opponent.isOnSurface()) {
/*      */         
/*  905 */         VolaTile t = Zones.getTileOrNull(opponent.getTileX(), opponent.getTileY(), opponent.isOnSurface());
/*  906 */         if (t != null)
/*      */         {
/*  908 */           if (t.getStructure() != null)
/*  909 */             return true; 
/*      */         }
/*      */       } 
/*      */     } 
/*  913 */     BlockingResult result = Blocking.getBlockerBetween(creature, opponent, 4);
/*  914 */     if (result != null) {
/*      */       
/*  916 */       boolean blocked = false;
/*  917 */       for (Blocker b : result.getBlockerArray()) {
/*      */         
/*  919 */         if (!b.isDoor()) {
/*  920 */           blocked = true;
/*      */         }
/*  922 */         if (!b.canBeOpenedBy(creature, false)) {
/*  923 */           blocked = true;
/*      */         }
/*  925 */         if (!b.canBeOpenedBy(opponent, false)) {
/*  926 */           blocked = true;
/*      */         }
/*  928 */         if (blocked) {
/*      */           break;
/*      */         }
/*      */       } 
/*  932 */       if (blocked) {
/*      */         
/*  934 */         creature.breakout();
/*  935 */         if (!opportunity) {
/*      */           
/*  937 */           creature.getCommunicator().sendNormalServerMessage("The " + result
/*  938 */               .getFirstBlocker().getName() + " blocks your attempt.");
/*      */           
/*  940 */           if (result.getFirstBlocker().isTile()) {
/*      */             
/*  942 */             if (opponent.opponent == creature || opponent.getTarget() == creature)
/*      */             {
/*  944 */               opponent.setTarget(-10L, true);
/*      */             }
/*  946 */             if (creature.getTarget() == opponent)
/*  947 */               creature.setTarget(-10L, true); 
/*      */           } 
/*      */         } 
/*  950 */         creature.setOpponent(null);
/*  951 */         creature.sendToLoggers("Blocker result when attacking " + opponent.getName() + " " + result
/*  952 */             .getFirstBlocker().getName(), (byte)2);
/*  953 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  957 */     if (creature.isOnSurface() != opponent.isOnSurface()) {
/*      */ 
/*      */ 
/*      */       
/*  961 */       boolean fail = false;
/*  962 */       boolean transition = false;
/*  963 */       if ((opponent.getCurrentTile()).isTransition) {
/*      */         
/*  965 */         transition = true;
/*  966 */         if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(opponent.getTileX(), opponent.getTileY()))))
/*      */         {
/*  968 */           fail = true;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  974 */       if (!fail && (creature.getCurrentTile()).isTransition) {
/*      */         
/*  976 */         transition = true;
/*  977 */         if (Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(creature.getTileX(), creature.getTileY()))))
/*      */         {
/*  979 */           fail = true;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  984 */       if (!transition)
/*  985 */         fail = true; 
/*  986 */       return fail;
/*      */     } 
/*  988 */     return false;
/*      */   }
/*      */   public CombatHandler(Creature _creature) {
/*  991 */     this.lastTimeStamp = 1.0F;
/*  992 */     this.lastAttackPollDelta = 0.0F;
/*  993 */     this.waitTime = 0.0F;
/*      */     this.creature = _creature;
/*      */   }
/*      */   private final AttackAction getAttackAction(boolean isSecondary) {
/*  997 */     AttackAction attack = null;
/*      */     
/*  999 */     List<AttackAction> list = !isSecondary ? this.creature.getTemplate().getPrimaryAttacks() : this.creature.getTemplate().getSecondaryAttacks();
/* 1000 */     List<AttackAction> valid = new ArrayList<>();
/*      */     
/* 1002 */     for (AttackAction act : list) {
/*      */       
/* 1004 */       UsedAttackData data = this.creature.getUsedAttackData(act);
/* 1005 */       if (data == null) {
/* 1006 */         valid.add(act); continue;
/* 1007 */       }  if (data.getTime() <= 0.0F && data.getRounds() <= 0) {
/* 1008 */         valid.add(act);
/*      */       }
/*      */     } 
/* 1011 */     if (valid.size() > 0) {
/*      */       
/* 1013 */       int index = Server.rand.nextInt(valid.size());
/* 1014 */       attack = valid.get(index);
/*      */     } 
/*      */     
/* 1017 */     return attack;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean attack2(Creature opponent, int combatCounter, boolean opportunity, float actionCounter, Action act) {
/* 1023 */     float delta = Math.max(0.0F, actionCounter - this.lastTimeStamp);
/* 1024 */     float updateTime = Math.abs(delta - this.lastAttackPollDelta);
/*      */     
/* 1026 */     this.lastAttackPollDelta = updateTime;
/* 1027 */     this.creature.updateAttacksUsed(updateTime);
/* 1028 */     if (delta <= this.waitTime)
/*      */     {
/* 1030 */       return false;
/*      */     }
/*      */     
/* 1033 */     if (opportunity) {
/* 1034 */       this.creature.opportunityAttackCounter = 2;
/*      */     }
/* 1036 */     this.lastAttackPollDelta = 0.0F;
/* 1037 */     this.waitTime = 0.5F;
/*      */     
/* 1039 */     AttackAction primaryAttack = getAttackAction(false);
/* 1040 */     AttackAction secondaryAttack = getAttackAction(true);
/* 1041 */     int[] tcmoves = this.creature.getCombatMoves();
/* 1042 */     boolean canDoSpecials = (tcmoves != null && tcmoves.length > 0);
/* 1043 */     this.creature.setSecondsToLogout(300);
/* 1044 */     if (!opponent.isPlayer()) {
/* 1045 */       this.creature.setSecondsToLogout(180);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1052 */     boolean shouldDoSecondary = false;
/*      */     
/* 1054 */     if (primaryAttack != null && secondaryAttack != null && this.creature.combatRound > 1) {
/* 1055 */       shouldDoSecondary = (Server.rand.nextInt(5) == 0);
/* 1056 */     } else if (primaryAttack == null && secondaryAttack != null) {
/* 1057 */       shouldDoSecondary = true;
/*      */     } 
/* 1059 */     boolean doSpecialAttack = false;
/*      */ 
/*      */     
/* 1062 */     if (!this.creature.isPlayer()) {
/*      */       
/* 1064 */       boolean changedStance = false;
/*      */ 
/*      */       
/* 1067 */       if (Server.rand.nextInt(10) == 0) {
/* 1068 */         changedStance = checkStanceChange(this.creature, opponent);
/*      */       }
/*      */       
/* 1071 */       if (canDoSpecials && !changedStance)
/*      */       {
/*      */ 
/*      */         
/* 1075 */         if (primaryAttack == null && secondaryAttack == null) {
/* 1076 */           doSpecialAttack = true;
/*      */         } else {
/* 1078 */           doSpecialAttack = (Server.rand.nextInt(80) < 20);
/*      */         } 
/*      */       }
/*      */     } 
/* 1082 */     Item weapon = null;
/*      */ 
/*      */     
/* 1085 */     if ((shouldDoSecondary && secondaryAttack != null && secondaryAttack.isUsingWeapon()) || (!shouldDoSecondary && primaryAttack != null && primaryAttack
/* 1086 */       .isUsingWeapon()))
/*      */     {
/*      */       
/* 1089 */       weapon = this.creature.getPrimWeapon();
/*      */     }
/*      */     
/* 1092 */     if (opportunity)
/* 1093 */       this.creature.opportunityAttackCounter = 2; 
/* 1094 */     this.lastTimeStamp = actionCounter;
/*      */ 
/*      */     
/* 1097 */     if (prerequisitesFail(this.creature, opponent, opportunity, weapon, (weapon == null))) {
/* 1098 */       return true;
/*      */     }
/* 1100 */     if (act != null && act.justTickedSecond())
/*      */     {
/* 1102 */       this.creature.getCommunicator().sendCombatStatus(getDistdiff(this.creature, opponent, shouldDoSecondary ? secondaryAttack : primaryAttack), 
/*      */           
/* 1104 */           getFootingModifier(weapon, opponent), this.currentStance);
/*      */     }
/*      */ 
/*      */     
/* 1108 */     if (isProne() || isOpen()) {
/* 1109 */       return false;
/*      */     }
/* 1111 */     boolean lDead = false;
/* 1112 */     this.creature.opponentCounter = 30;
/*      */     
/* 1114 */     if (actionCounter == 1.0F && !opportunity && this.creature.isMoving() && !opponent.isMoving() && opponent.target == this.creature
/* 1115 */       .getWurmId()) {
/*      */ 
/*      */ 
/*      */       
/* 1119 */       opponent.attackTarget();
/* 1120 */       if (opponent.opponent == this.creature)
/*      */       {
/* 1122 */         this.creature.sendToLoggers("Opponent strikes first", (byte)2);
/*      */         
/* 1124 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1125 */         segments.add(new CreatureLineSegment(opponent));
/* 1126 */         segments.add(new MulticolorLineSegment(" strike ", (byte)0));
/* 1127 */         segments.add(new CreatureLineSegment(this.creature));
/* 1128 */         segments.add(new MulticolorLineSegment(" as " + this.creature.getHeSheItString() + " approaches!", (byte)0));
/*      */         
/* 1130 */         opponent.getCommunicator().sendColoredMessageCombat(segments);
/* 1131 */         ((MulticolorLineSegment)segments.get(1)).setText(" strikes ");
/* 1132 */         ((MulticolorLineSegment)segments.get(1)).setText(" as you approach. ");
/* 1133 */         this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1143 */         lDead = opponent.getCombatHandler().attack(this.creature, combatCounter, true, 2.0F, (Action)null);
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 1149 */     else if (opportunity && primaryAttack != null) {
/*      */       
/* 1151 */       this.opportunityAttacks = (byte)(this.opportunityAttacks + 1);
/* 1152 */       this.creature.sendToLoggers("YOU OPPORTUNITY", (byte)2);
/* 1153 */       opponent.sendToLoggers(this.creature.getName() + " OPPORTUNITY", (byte)2);
/* 1154 */       if (opponent.spamMode()) {
/*      */         
/* 1156 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1157 */         segments.add(new MulticolorLineSegment("You open yourself to an attack from ", (byte)7));
/* 1158 */         segments.add(new CreatureLineSegment(this.creature));
/* 1159 */         segments.add(new MulticolorLineSegment(".", (byte)7));
/*      */         
/* 1161 */         opponent.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */       
/* 1165 */       if (this.creature.spamMode()) {
/*      */         
/* 1167 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1168 */         segments.add(new CreatureLineSegment(opponent));
/* 1169 */         segments.add(new MulticolorLineSegment(" opens " + opponent.getHimHerItString() + "self up to an easy attack.", (byte)3));
/*      */         
/* 1171 */         opponent.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1176 */       lDead = attack(opponent, primaryAttack);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1181 */     else if (!lDead && primaryAttack != null && !shouldDoSecondary && !doSpecialAttack) {
/*      */       
/* 1183 */       float time = getSpeed(primaryAttack, 
/* 1184 */           primaryAttack.isUsingWeapon() ? this.creature.getPrimWeapon() : null);
/* 1185 */       this.creature.addToAttackUsed(primaryAttack, time, primaryAttack
/* 1186 */           .getAttackValues().getRounds());
/* 1187 */       lDead = attack(opponent, primaryAttack);
/* 1188 */       this.waitTime = primaryAttack.getAttackValues().getWaitTime();
/*      */       
/* 1190 */       if (this.creature.isPlayer() && act != null && act.justTickedSecond())
/*      */       {
/* 1192 */         checkStanceChange(this.creature, opponent);
/*      */       
/*      */       }
/*      */     }
/* 1196 */     else if (!lDead && secondaryAttack != null && shouldDoSecondary && !doSpecialAttack) {
/*      */       
/* 1198 */       float time = getSpeed(secondaryAttack, secondaryAttack.isUsingWeapon() ? this.creature
/* 1199 */           .getPrimWeapon(false) : null);
/* 1200 */       this.creature.addToAttackUsed(secondaryAttack, time, secondaryAttack
/* 1201 */           .getAttackValues().getRounds());
/*      */       
/* 1203 */       lDead = attack(opponent, secondaryAttack);
/*      */       
/* 1205 */       this.waitTime = secondaryAttack.getAttackValues().getWaitTime();
/*      */       
/* 1207 */       if (this.creature.isPlayer() && act != null && act.justTickedSecond())
/*      */       {
/* 1209 */         checkStanceChange(this.creature, opponent);
/*      */       }
/*      */     }
/* 1212 */     else if (!lDead) {
/*      */       
/* 1214 */       if (!this.creature.isPlayer() && this.creature.getTarget() != null)
/*      */       {
/*      */         
/* 1217 */         if (doSpecialAttack) {
/*      */           
/* 1219 */           int[] cmoves = this.creature.getCombatMoves();
/* 1220 */           if (cmoves.length > 0)
/*      */           {
/* 1222 */             for (int lCmove : cmoves) {
/*      */               
/* 1224 */               CombatMove c = CombatMove.getCombatMove(lCmove);
/* 1225 */               if (Server.rand.nextFloat() < c.getRarity())
/*      */               {
/* 1227 */                 if (this.creature.getHugeMoveCounter() == 0) {
/*      */                   
/* 1229 */                   this.creature.sendToLoggers("YOU COMBAT MOVE", (byte)2);
/* 1230 */                   opponent.sendToLoggers(this.creature.getName() + " COMBAT MOVE", (byte)2);
/* 1231 */                   this.creature.setHugeMoveCounter(2 + Server.rand.nextInt(4));
/* 1232 */                   c.perform(this.creature);
/* 1233 */                   this.waitTime = 2.0F;
/*      */                   
/*      */                   break;
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1244 */     return lDead;
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetSecAttacks() {
/* 1249 */     if (this.secattacks != null) {
/* 1250 */       this.secattacks.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attack(Creature opponent, int combatCounter, boolean opportunity, float actionCounter, Action act) {
/* 1256 */     opponent.addAttacker(this.creature);
/*      */     
/* 1258 */     if (actionCounter == 1.0F)
/*      */     {
/* 1260 */       this.lastTimeStamp = actionCounter;
/*      */     }
/* 1262 */     if (Features.Feature.CREATURE_COMBAT_CHANGES.isEnabled())
/*      */     {
/* 1264 */       if (this.creature.getTemplate().isUsingNewAttacks())
/*      */       {
/* 1266 */         return attack2(opponent, combatCounter, opportunity, actionCounter, act);
/*      */       }
/*      */     }
/*      */     
/* 1270 */     float delta = Math.max(0.0F, actionCounter - this.lastTimeStamp);
/* 1271 */     if (delta < 0.1D)
/*      */     {
/* 1273 */       return false;
/*      */     }
/* 1275 */     if (opportunity)
/* 1276 */       this.creature.opportunityAttackCounter = 2; 
/* 1277 */     this.lastTimeStamp = actionCounter;
/*      */     
/* 1279 */     Item weapon = this.creature.getPrimWeapon();
/* 1280 */     this.creature.setSecondsToLogout(300);
/* 1281 */     if (!opponent.isPlayer()) {
/* 1282 */       this.creature.setSecondsToLogout(180);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1289 */     if (prerequisitesFail(this.creature, opponent, opportunity, weapon))
/* 1290 */       return true; 
/* 1291 */     if (act != null && act.justTickedSecond())
/* 1292 */       this.creature.getCommunicator().sendCombatStatus(getDistdiff(weapon, this.creature, opponent), 
/* 1293 */           getFootingModifier(weapon, opponent), this.currentStance); 
/* 1294 */     boolean lDead = false;
/* 1295 */     if (isProne() || isOpen())
/* 1296 */       return false; 
/* 1297 */     this.creature.opponentCounter = 30;
/* 1298 */     if (actionCounter == 1.0F && !opportunity && this.creature.isMoving() && !opponent.isMoving() && opponent.target == this.creature
/* 1299 */       .getWurmId()) {
/*      */ 
/*      */ 
/*      */       
/* 1303 */       opponent.attackTarget();
/* 1304 */       if (opponent.opponent == this.creature)
/*      */       {
/* 1306 */         this.creature.sendToLoggers("Opponent strikes first", (byte)2);
/*      */         
/* 1308 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1309 */         segments.add(new CreatureLineSegment(opponent));
/* 1310 */         segments.add(new MulticolorLineSegment(" strike ", (byte)0));
/* 1311 */         segments.add(new CreatureLineSegment(this.creature));
/* 1312 */         segments.add(new MulticolorLineSegment(" as " + this.creature.getHeSheItString() + " approaches!", (byte)0));
/*      */         
/* 1314 */         opponent.getCommunicator().sendColoredMessageCombat(segments);
/* 1315 */         ((MulticolorLineSegment)segments.get(1)).setText(" strikes ");
/* 1316 */         ((MulticolorLineSegment)segments.get(1)).setText(" as you approach. ");
/* 1317 */         this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1327 */         lDead = opponent.getCombatHandler().attack(this.creature, combatCounter, true, 2.0F, (Action)null);
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1332 */     else if (opportunity) {
/*      */       
/* 1334 */       this.opportunityAttacks = (byte)(this.opportunityAttacks + 1);
/* 1335 */       this.creature.sendToLoggers("YOU OPPORTUNITY", (byte)2);
/* 1336 */       opponent.sendToLoggers(this.creature.getName() + " OPPORTUNITY", (byte)2);
/*      */       
/* 1338 */       if (opponent.spamMode()) {
/*      */         
/* 1340 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1341 */         segments.add(new MulticolorLineSegment("You open yourself to an attack from ", (byte)7));
/* 1342 */         segments.add(new CreatureLineSegment(this.creature));
/* 1343 */         segments.add(new MulticolorLineSegment(".", (byte)7));
/*      */         
/* 1345 */         opponent.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */       
/* 1349 */       if (this.creature.spamMode()) {
/*      */         
/* 1351 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1352 */         segments.add(new CreatureLineSegment(opponent));
/* 1353 */         segments.add(new MulticolorLineSegment(" opens " + opponent.getHimHerItString() + "self up to an easy attack.", (byte)3));
/*      */         
/* 1355 */         this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */       
/* 1359 */       if (Server.rand.nextInt(3) == 0) {
/*      */         
/* 1361 */         Item[] secweapons = this.creature.getSecondaryWeapons();
/* 1362 */         if (secweapons.length > 0)
/* 1363 */           weapon = secweapons[Server.rand.nextInt(secweapons.length)]; 
/*      */       } 
/* 1365 */       lDead = attack(opponent, weapon, false);
/*      */     }
/*      */     else {
/*      */       
/* 1369 */       boolean performedAttack = false;
/* 1370 */       if (!lDead && this.creature.combatRound > 1) {
/*      */         
/* 1372 */         Item[] secweapons = this.creature.getSecondaryWeapons();
/* 1373 */         for (Item lSecweapon : secweapons) {
/*      */           
/* 1375 */           if (this.creature.opponent != null) {
/*      */             
/* 1377 */             if (this.secattacks == null)
/* 1378 */               this.secattacks = new HashSet<>(); 
/* 1379 */             if (!this.secattacks.contains(lSecweapon))
/*      */             {
/* 1381 */               if ((lSecweapon.getTemplateId() != 12 && lSecweapon.getTemplateId() != 17) || (this.creature
/* 1382 */                 .getHugeMoveCounter() == 0 && Server.rand.nextBoolean())) {
/*      */                 
/* 1384 */                 float f1 = getSpeed(lSecweapon);
/* 1385 */                 float f2 = this.creature.addToWeaponUsed(lSecweapon, delta);
/* 1386 */                 boolean bool = (f2 > f1);
/*      */ 
/*      */ 
/*      */                 
/* 1390 */                 if (!lDead && this.creature.combatRound % 2 == 1 && bool) {
/*      */                   
/* 1392 */                   this.creature.deductFromWeaponUsed(lSecweapon, f1);
/* 1393 */                   this.creature.sendToLoggers("YOU SECONDARY " + lSecweapon.getName(), (byte)2);
/* 1394 */                   opponent.sendToLoggers(this.creature.getName() + " SECONDARY " + lSecweapon.getName() + "(" + lSecweapon
/* 1395 */                       .getWurmId() + ")", (byte)2);
/* 1396 */                   this.creature.setHugeMoveCounter(2 + Server.rand.nextInt(4));
/* 1397 */                   lDead = attack(opponent, lSecweapon, true);
/*      */                   
/* 1399 */                   performedAttack = true;
/*      */                   
/* 1401 */                   this.secattacks.add(lSecweapon);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1409 */       float time = getSpeed(weapon);
/* 1410 */       float timer = this.creature.addToWeaponUsed(weapon, delta);
/* 1411 */       boolean shouldAttack = (timer > time);
/*      */       
/* 1413 */       if (!lDead && shouldAttack) {
/*      */         
/* 1415 */         this.creature.deductFromWeaponUsed(weapon, time);
/* 1416 */         this.creature.sendToLoggers("YOU PRIMARY " + weapon.getName(), (byte)2);
/* 1417 */         opponent.sendToLoggers(this.creature.getName() + " PRIMARY " + weapon.getName(), (byte)2);
/* 1418 */         lDead = attack(opponent, weapon, false);
/* 1419 */         performedAttack = true;
/* 1420 */         if (this.creature.isPlayer() && act != null && act.justTickedSecond())
/*      */         {
/* 1422 */           checkStanceChange(this.creature, opponent);
/*      */         }
/*      */       }
/* 1425 */       else if (!performedAttack && !lDead) {
/*      */         
/* 1427 */         if (!this.creature.isPlayer() && this.creature.getTarget() != null)
/*      */         {
/* 1429 */           if (this.creature.getLayer() == opponent.getLayer())
/*      */           {
/* 1431 */             if (!checkStanceChange(this.creature, opponent)) {
/*      */               
/* 1433 */               int[] cmoves = this.creature.getCombatMoves();
/* 1434 */               if (cmoves.length > 0)
/*      */               {
/* 1436 */                 for (int lCmove : cmoves) {
/*      */                   
/* 1438 */                   CombatMove c = CombatMove.getCombatMove(lCmove);
/* 1439 */                   if (Server.rand.nextFloat() < c.getRarity())
/*      */                   {
/* 1441 */                     if (this.creature.getHugeMoveCounter() == 0) {
/*      */                       
/* 1443 */                       this.creature.sendToLoggers("YOU COMBAT MOVE", (byte)2);
/* 1444 */                       opponent.sendToLoggers(this.creature.getName() + " COMBAT MOVE", (byte)2);
/*      */                       
/* 1446 */                       this.creature.setHugeMoveCounter(2 + Server.rand.nextInt(4));
/* 1447 */                       c.perform(this.creature);
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/* 1459 */     return lDead;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearRound() {
/* 1464 */     this.opportunityAttacks = 0;
/* 1465 */     this.receivedWeaponSkill = false;
/* 1466 */     this.receivedSecWeaponSkill = false;
/* 1467 */     this.receivedFStyleSkill = false;
/* 1468 */     this.receivedShieldSkill = false;
/*      */     
/* 1470 */     this.usedShieldThisRound = 0;
/* 1471 */     if (this.lastShieldBashed > 0) {
/* 1472 */       this.lastShieldBashed--;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1478 */     if (this.secattacks != null) {
/* 1479 */       this.secattacks.clear();
/*      */     }
/* 1481 */     this.turned = false;
/* 1482 */     if (this.battleratingPenalty > 0) {
/*      */       
/* 1484 */       this.battleratingPenalty = (byte)Math.max(0, this.battleratingPenalty - 2);
/* 1485 */       if (this.battleratingPenalty == 0 && this.creature.isPlayer())
/*      */       {
/* 1487 */         this.creature.getCommunicator().sendCombatNormalMessage("You concentrate better again.");
/*      */       }
/*      */     } 
/* 1490 */     if (this.creature.isFighting()) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1495 */       this.creature.combatRound++;
/* 1496 */       if (!this.creature.opponent.isDead()) {
/*      */         
/* 1498 */         calcAttacks(true);
/*      */       } else {
/*      */         
/* 1501 */         this.moveStack = null;
/* 1502 */       }  this.creature.setStealth(false);
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
/*      */   public static final boolean isStanceParrying(byte defenderStance, byte attackerStance) {
/* 1517 */     if (attackerStance == 8 || attackerStance == 9)
/* 1518 */       return true; 
/* 1519 */     if (defenderStance == 8 || defenderStance == 9)
/* 1520 */       return false; 
/* 1521 */     if (defenderStance == 11)
/* 1522 */       return (attackerStance == 3 || attackerStance == 4 || attackerStance == 10); 
/* 1523 */     if (defenderStance == 12)
/* 1524 */       return (attackerStance == 1 || attackerStance == 6 || attackerStance == 7); 
/* 1525 */     if (defenderStance == 14) {
/* 1526 */       return (attackerStance == 5 || attackerStance == 6 || attackerStance == 4);
/*      */     }
/* 1528 */     if (defenderStance == 13) {
/* 1529 */       return (attackerStance == 2 || attackerStance == 1 || attackerStance == 3);
/*      */     }
/* 1531 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isStanceOpposing(byte defenderStance, byte attackerStance) {
/* 1536 */     if (attackerStance == 8 || attackerStance == 9)
/* 1537 */       return true; 
/* 1538 */     if (defenderStance == 8 || defenderStance == 9)
/* 1539 */       return false; 
/* 1540 */     if (defenderStance == 1)
/* 1541 */       return (attackerStance == 6); 
/* 1542 */     if (defenderStance == 6)
/* 1543 */       return (attackerStance == 1); 
/* 1544 */     if (defenderStance == 4)
/* 1545 */       return (attackerStance == 3); 
/* 1546 */     if (defenderStance == 3)
/* 1547 */       return (attackerStance == 4); 
/* 1548 */     if (defenderStance == 5)
/* 1549 */       return (attackerStance == 2); 
/* 1550 */     if (defenderStance == 2)
/* 1551 */       return (attackerStance == 5); 
/* 1552 */     if (defenderStance == 7)
/* 1553 */       return (attackerStance == 7); 
/* 1554 */     if (defenderStance == 0)
/* 1555 */       return (attackerStance == 0); 
/* 1556 */     if (defenderStance == 10)
/* 1557 */       return (attackerStance == 10); 
/* 1558 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private byte getWoundPos(byte aStance, Creature aCreature) throws Exception {
/* 1563 */     return aCreature.getBody().getRandomWoundPos(aStance);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void resetFlags(Creature opponent) {
/* 1568 */     hit = false;
/* 1569 */     miss = false;
/* 1570 */     crit = false;
/* 1571 */     aiming = false;
/* 1572 */     dead = false;
/* 1573 */     chanceToHit = 0.0F;
/* 1574 */     pos = 0;
/*      */     
/* 1576 */     attCheck = 0.0D;
/* 1577 */     attBonus = 0.0D;
/* 1578 */     defCheck = 0.0D;
/* 1579 */     defBonus = 0.0D;
/* 1580 */     defShield = null;
/* 1581 */     defenderSkills = opponent.getSkills();
/* 1582 */     defParryWeapon = null;
/* 1583 */     defLeftWeapon = null;
/* 1584 */     defPrimWeaponSkill = null;
/* 1585 */     type = 0;
/* 1586 */     attString = "";
/* 1587 */     damage = 0.0D;
/* 1588 */     justOpen = false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSpeed(AttackAction act, Item weapon) {
/* 1594 */     float timeMod = 0.5F;
/* 1595 */     if (this.currentStrength == 0) {
/* 1596 */       timeMod = 1.5F;
/*      */     }
/* 1598 */     if (act.isUsingWeapon() && weapon != null) {
/*      */       
/* 1600 */       float f = getWeaponSpeed(act, weapon);
/*      */       
/* 1602 */       f += timeMod;
/* 1603 */       if (weapon.getSpellSpeedBonus() != 0.0F) {
/* 1604 */         f = (float)(f - 0.5D * (weapon.getSpellSpeedBonus() / 100.0F));
/* 1605 */       } else if (!weapon.isArtifact()) {
/*      */         
/* 1607 */         if (this.creature.getBonusForSpellEffect((byte)39) > 0.0F)
/* 1608 */           f -= 0.5F; 
/*      */       } 
/* 1610 */       if (weapon.isTwoHanded() && this.currentStrength == 3) {
/* 1611 */         f *= 0.9F;
/*      */       }
/* 1613 */       if (!Features.Feature.METALLIC_ITEMS.isEnabled() && weapon.getMaterial() == 57)
/* 1614 */         f *= 0.9F; 
/* 1615 */       if (this.creature.getStatus().getStamina() < 2000) {
/* 1616 */         f++;
/*      */       }
/* 1618 */       f = (float)(f * this.creature.getMovementScheme().getWebArmourMod() * -4.0D);
/* 1619 */       if (this.creature.hasSpellEffect((byte)66))
/* 1620 */         f *= 2.0F; 
/* 1621 */       return Math.max(3.0F, f);
/*      */     } 
/*      */ 
/*      */     
/* 1625 */     float calcspeed = getWeaponSpeed(act, (Item)null);
/*      */     
/* 1627 */     calcspeed += timeMod;
/*      */     
/* 1629 */     if (this.creature.getStatus().getStamina() < 2000)
/* 1630 */       calcspeed++; 
/* 1631 */     calcspeed = (float)(calcspeed * this.creature.getMovementScheme().getWebArmourMod() * -4.0D);
/* 1632 */     if (this.creature.hasSpellEffect((byte)66))
/* 1633 */       calcspeed *= 2.0F; 
/* 1634 */     return Math.max(3.0F, calcspeed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSpeed(Item weapon) {
/* 1641 */     float timeMod = 0.5F;
/* 1642 */     if (this.currentStrength == 0) {
/* 1643 */       timeMod = 1.5F;
/*      */     }
/* 1645 */     float calcspeed = getWeaponSpeed(weapon);
/*      */     
/* 1647 */     calcspeed += timeMod;
/* 1648 */     if (weapon.getSpellSpeedBonus() != 0.0F) {
/* 1649 */       calcspeed = (float)(calcspeed - 0.5D * (weapon.getSpellSpeedBonus() / 100.0F));
/* 1650 */     } else if (!weapon.isArtifact() && this.creature.getBonusForSpellEffect((byte)39) > 0.0F) {
/*      */       
/* 1652 */       float maxBonus = calcspeed * 0.1F;
/* 1653 */       float percentBonus = this.creature.getBonusForSpellEffect((byte)39) / 100.0F;
/* 1654 */       calcspeed -= maxBonus * percentBonus;
/*      */     } 
/* 1656 */     if (weapon.isTwoHanded() && this.currentStrength == 3) {
/* 1657 */       calcspeed *= 0.9F;
/*      */     }
/* 1659 */     if (!Features.Feature.METALLIC_ITEMS.isEnabled() && weapon.getMaterial() == 57)
/* 1660 */       calcspeed *= 0.9F; 
/* 1661 */     if (this.creature.getStatus().getStamina() < 2000) {
/* 1662 */       calcspeed++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1670 */     float waMult = (float)(this.creature.getMovementScheme().getWebArmourMod() * -2.0D);
/* 1671 */     calcspeed *= 1.0F + waMult;
/* 1672 */     if (this.creature.hasSpellEffect((byte)66))
/* 1673 */       calcspeed *= 2.0F; 
/* 1674 */     return Math.max(3.0F, calcspeed);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOpen() {
/* 1679 */     return (this.currentStance == 9);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isProne() {
/* 1684 */     return (this.currentStance == 8);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean attack(Creature opponent, AttackAction attackAction) {
/* 1689 */     resetFlags(opponent);
/* 1690 */     if (!(opponent instanceof Player) || !opponent.hasLink()) {
/*      */       
/* 1692 */       if (!this.turned) {
/*      */         
/* 1694 */         if (opponent.getTarget() == null || opponent.getTarget() == this.creature)
/* 1695 */           opponent.turnTowardsCreature(this.creature); 
/* 1696 */         this.turned = true;
/*      */       } 
/*      */       
/* 1699 */       boolean switchOpp = false;
/* 1700 */       if (!opponent.isFighting() && (this.creature.isPlayer() || this.creature.isDominated()))
/* 1701 */         switchOpp = true; 
/* 1702 */       opponent.setTarget(this.creature.getWurmId(), switchOpp);
/*      */     } 
/*      */     
/* 1705 */     this.creature.getStatus().modifyStamina((int)(-80.0F * (1.0F + this.currentStrength * 0.5F)));
/* 1706 */     this.addToSkills = true;
/* 1707 */     Item weapon = null;
/* 1708 */     weapon = this.creature.getPrimWeapon(!attackAction.isUsingWeapon());
/* 1709 */     chanceToHit = getChanceToHit(opponent, weapon);
/*      */     
/* 1711 */     type = attackAction.getAttackValues().getDamageType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1719 */     float percent = checkShield(opponent, weapon);
/* 1720 */     if (percent > 50.0F) {
/* 1721 */       chanceToHit = 0.0F;
/* 1722 */     } else if (percent > 0.0F) {
/* 1723 */       chanceToHit *= 1.0F - percent / 100.0F;
/* 1724 */     }  float parrPercent = -1.0F;
/* 1725 */     if (opponent.getFightStyle() != 1 || Server.rand.nextInt(3) == 0)
/*      */     {
/* 1727 */       if (chanceToHit > 0.0F) {
/*      */         
/* 1729 */         parrPercent = checkDefenderParry(opponent, weapon);
/* 1730 */         if (parrPercent > 60.0F) {
/* 1731 */           chanceToHit = 0.0F;
/* 1732 */         } else if (parrPercent > 0.0F) {
/* 1733 */           chanceToHit *= 1.0F - parrPercent / 200.0F;
/*      */         } 
/*      */       }  } 
/* 1736 */     pos = 2;
/*      */     
/*      */     try {
/* 1739 */       pos = getWoundPos(this.currentStance, opponent);
/*      */     }
/* 1741 */     catch (Exception ex) {
/*      */       
/* 1743 */       logger.log(Level.WARNING, this.creature.getName() + " " + ex.getMessage(), ex);
/*      */     } 
/*      */     
/* 1746 */     attCheck = (Server.rand.nextFloat() * 100.0F) * (1.0D + this.creature.getVisionMod());
/*      */     
/* 1748 */     String combatDetails = " CHANCE:" + chanceToHit + ", roll=" + attCheck;
/* 1749 */     if (this.creature.spamMode() && Servers.isThisATestServer())
/*      */     {
/* 1751 */       this.creature.getCommunicator().sendCombatSafeMessage(combatDetails);
/*      */     }
/* 1753 */     this.creature.sendToLoggers("YOU" + combatDetails, (byte)2);
/* 1754 */     opponent.sendToLoggers(this.creature.getName() + combatDetails, (byte)2);
/*      */     
/* 1756 */     if (attCheck < chanceToHit) {
/*      */       
/* 1758 */       if (opponent.isPlayer() && !weapon.isArtifact())
/*      */       {
/* 1760 */         float critChance = attackAction.getAttackValues().getCriticalChance();
/*      */         
/* 1762 */         if (isAtSoftSpot(opponent.getCombatHandler().getCurrentStance(), getCurrentStance())) {
/* 1763 */           critChance += 0.05F;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1768 */         if (Server.rand.nextFloat() < critChance)
/*      */         {
/* 1770 */           crit = true;
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1776 */       miss = true;
/*      */     } 
/* 1778 */     if (!miss)
/*      */     {
/* 1780 */       if (!crit) {
/*      */         
/* 1782 */         boolean keepGoing = true;
/*      */ 
/*      */         
/* 1785 */         defCheck = (Server.rand.nextFloat() * 100.0F) * opponent.getCombatHandler().getDodgeMod();
/* 1786 */         defCheck *= opponent.getStatus().getDodgeTypeModifier();
/* 1787 */         if (opponent.getMovePenalty() != 0) {
/* 1788 */           defCheck *= (1.0F + opponent.getMovePenalty() / 10.0F);
/*      */         }
/*      */ 
/*      */         
/* 1792 */         defCheck *= 1.0D - (opponent.getMovementScheme()).armourMod.getModifier();
/*      */ 
/*      */         
/* 1795 */         if (defCheck < opponent.getBodyControl() * ItemBonus.getDodgeBonus(opponent) / 3.0D) {
/*      */           
/* 1797 */           if ((opponent.getStatus().getDodgeTypeModifier() * 100.0F) < opponent.getBodyControl() / 3.0D)
/* 1798 */             logger.log(Level.WARNING, opponent
/* 1799 */                 .getName() + " is impossible to hit except for crits: " + (opponent
/* 1800 */                 .getCombatHandler().getDodgeMod() * 100.0D) + " is always less than " + opponent
/* 1801 */                 .getBodyControl()); 
/* 1802 */           sendDodgeMessage(opponent);
/* 1803 */           keepGoing = false;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1808 */           String dodgeDetails = "Dodge=" + defCheck + "<" + (opponent.getBodyControl() / 3.0D) + " dodgemod=" + opponent.getCombatHandler().getDodgeMod() + " dodgeType=" + opponent.getStatus().getDodgeTypeModifier() + " dodgeMovePenalty=" + opponent.getMovePenalty() + " armour=" + (opponent.getMovementScheme()).armourMod.getModifier();
/* 1809 */           if (this.creature.spamMode() && Servers.isThisATestServer())
/*      */           {
/* 1811 */             this.creature.getCommunicator().sendCombatSafeMessage(dodgeDetails);
/*      */           }
/* 1813 */           this.creature.sendToLoggers(dodgeDetails, (byte)4);
/*      */           
/* 1815 */           checkIfHitVehicle(this.creature, opponent);
/*      */         } 
/*      */         
/* 1818 */         if (keepGoing)
/* 1819 */           hit = true; 
/*      */       } 
/*      */     }
/* 1822 */     if (hit || crit) {
/*      */       
/* 1824 */       this.creature.sendToLoggers("YOU DAMAGE " + weapon.getName(), (byte)2);
/* 1825 */       opponent.sendToLoggers(this.creature.getName() + " DAMAGE " + weapon.getName(), (byte)2);
/* 1826 */       dead = setDamage(opponent, weapon, damage, pos, type);
/*      */     } 
/*      */     
/* 1829 */     if (dead) {
/* 1830 */       setKillEffects(this.creature, opponent);
/*      */     }
/* 1832 */     if (miss) {
/*      */       
/* 1834 */       if (this.creature.spamMode())
/*      */       {
/* 1836 */         if (chanceToHit > 0.0F || (percent > 0.0F && parrPercent > 0.0F)) {
/*      */           
/* 1838 */           this.creature.getCommunicator().sendCombatNormalMessage("You miss with the " + weapon.getName() + ".");
/* 1839 */           this.creature.sendToLoggers("YOU MISS " + weapon.getName(), (byte)2);
/* 1840 */           opponent.sendToLoggers(this.creature.getName() + " MISS " + weapon.getName(), (byte)2);
/*      */         } 
/*      */       }
/* 1843 */       if (!this.creature.isUnique() && attCheck - chanceToHit > 50.0D && Server.rand.nextInt(10) == 0) {
/*      */         
/* 1845 */         justOpen = true;
/*      */         
/* 1847 */         setCurrentStance(-1, (byte)9);
/*      */         
/* 1849 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1850 */         segments.add(new CreatureLineSegment(this.creature));
/* 1851 */         segments.add(new MulticolorLineSegment(" makes a bad move and is an easy target!.", (byte)0));
/*      */         
/* 1853 */         opponent.getCommunicator().sendColoredMessageCombat(segments);
/* 1854 */         ((MulticolorLineSegment)segments.get(1)).setText(" make a bad move, making you an easy target.");
/* 1855 */         this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1862 */         this.creature.getCurrentTile().checkOpportunityAttacks(this.creature);
/* 1863 */         opponent.getCurrentTile().checkOpportunityAttacks(this.creature);
/*      */ 
/*      */       
/*      */       }
/* 1867 */       else if (Server.rand.nextInt(10) == 0) {
/* 1868 */         checkIfHitVehicle(this.creature, opponent);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1873 */     this.addToSkills = false;
/*      */     
/* 1875 */     getDamage(this.creature, attackAction, opponent);
/* 1876 */     attString = attackAction.getAttackIdentifier().getAnimationString();
/* 1877 */     sendStanceAnimation(this.currentStance, true);
/*      */ 
/*      */     
/* 1880 */     return dead;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean attack(Creature opponent, Item weapon, boolean secondaryWeapon) {
/* 1885 */     if (weapon.isWeaponBow())
/* 1886 */       return false; 
/* 1887 */     resetFlags(opponent);
/* 1888 */     if (!(opponent instanceof Player) || !opponent.hasLink()) {
/*      */       
/* 1890 */       if (!this.turned) {
/*      */         
/* 1892 */         if (opponent.getTarget() == null || opponent.getTarget() == this.creature)
/* 1893 */           opponent.turnTowardsCreature(this.creature); 
/* 1894 */         this.turned = true;
/*      */       } 
/*      */       
/* 1897 */       boolean switchOpp = false;
/* 1898 */       if (!opponent.isFighting() && (this.creature.isPlayer() || this.creature.isDominated()))
/* 1899 */         switchOpp = true; 
/* 1900 */       opponent.setTarget(this.creature.getWurmId(), switchOpp);
/*      */     } 
/* 1902 */     this.creature.getStatus().modifyStamina((int)(-weapon.getWeightGrams() / 10.0F * (1.0F + this.currentStrength * 0.5F)));
/* 1903 */     this.addToSkills = true;
/*      */     
/* 1905 */     chanceToHit = getChanceToHit(opponent, weapon);
/*      */ 
/*      */     
/* 1908 */     getType(weapon, false);
/*      */     
/* 1910 */     getDamage(this.creature, weapon, opponent);
/* 1911 */     setAttString(this.creature, weapon, type);
/* 1912 */     sendStanceAnimation(this.currentStance, true);
/*      */ 
/*      */     
/* 1915 */     float percent = checkShield(opponent, weapon);
/* 1916 */     if (percent > 50.0F) {
/* 1917 */       chanceToHit = 0.0F;
/* 1918 */     } else if (percent > 0.0F) {
/* 1919 */       chanceToHit *= 1.0F - percent / 100.0F;
/* 1920 */     }  float parrPercent = -1.0F;
/* 1921 */     if (opponent.getFightStyle() != 1 || Server.rand.nextInt(3) == 0)
/*      */     {
/* 1923 */       if (chanceToHit > 0.0F) {
/*      */         
/* 1925 */         parrPercent = checkDefenderParry(opponent, weapon);
/* 1926 */         if (parrPercent > 60.0F) {
/* 1927 */           chanceToHit = 0.0F;
/* 1928 */         } else if (parrPercent > 0.0F) {
/* 1929 */           chanceToHit *= 1.0F - parrPercent / 200.0F;
/*      */         } 
/*      */       }  } 
/* 1932 */     pos = 2;
/*      */     
/*      */     try {
/* 1935 */       pos = getWoundPos(this.currentStance, opponent);
/*      */     }
/* 1937 */     catch (Exception ex) {
/*      */       
/* 1939 */       logger.log(Level.WARNING, this.creature.getName() + " " + ex.getMessage(), ex);
/*      */     } 
/*      */     
/* 1942 */     attCheck = (Server.rand.nextFloat() * 100.0F) * (1.0D + this.creature.getVisionMod());
/*      */     
/* 1944 */     String combatDetails = " CHANCE:" + chanceToHit + ", roll=" + attCheck;
/* 1945 */     if (this.creature.spamMode() && Servers.isThisATestServer())
/*      */     {
/* 1947 */       this.creature.getCommunicator().sendCombatSafeMessage(combatDetails);
/*      */     }
/* 1949 */     this.creature.sendToLoggers("YOU" + combatDetails, (byte)2);
/* 1950 */     opponent.sendToLoggers(this.creature.getName() + combatDetails, (byte)2);
/*      */ 
/*      */ 
/*      */     
/* 1954 */     if (attCheck < chanceToHit) {
/*      */       
/* 1956 */       if (opponent.isPlayer())
/*      */       {
/* 1958 */         float critChance = Weapon.getCritChanceForWeapon(weapon);
/*      */         
/* 1960 */         if (isAtSoftSpot(opponent.getCombatHandler().getCurrentStance(), getCurrentStance())) {
/* 1961 */           critChance += 0.05F;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1966 */         if (!weapon.isArtifact() && Server.rand.nextFloat() < critChance)
/*      */         {
/* 1968 */           crit = true;
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/* 1984 */       miss = true;
/*      */     } 
/* 1986 */     if (!miss)
/*      */     {
/* 1988 */       if (!crit) {
/*      */         
/* 1990 */         boolean keepGoing = true;
/*      */ 
/*      */         
/* 1993 */         defCheck = (Server.rand.nextFloat() * 100.0F) * opponent.getCombatHandler().getDodgeMod();
/* 1994 */         defCheck *= opponent.getStatus().getDodgeTypeModifier();
/* 1995 */         if (opponent.getMovePenalty() != 0) {
/* 1996 */           defCheck *= (1.0F + opponent.getMovePenalty() / 10.0F);
/*      */         }
/*      */ 
/*      */         
/* 2000 */         defCheck *= 1.0D - (opponent.getMovementScheme()).armourMod.getModifier();
/*      */ 
/*      */         
/* 2003 */         if (defCheck < opponent.getBodyControl() / 3.0D) {
/*      */           
/* 2005 */           if ((opponent.getStatus().getDodgeTypeModifier() * 100.0F) < opponent.getBodyControl() / 3.0D)
/* 2006 */             logger.log(Level.WARNING, opponent
/* 2007 */                 .getName() + " is impossible to hit except for crits: " + (opponent
/* 2008 */                 .getCombatHandler().getDodgeMod() * 100.0D) + " is always less than " + opponent
/* 2009 */                 .getBodyControl()); 
/* 2010 */           sendDodgeMessage(opponent);
/* 2011 */           keepGoing = false;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2016 */           String dodgeDetails = "Dodge=" + defCheck + "<" + (opponent.getBodyControl() / 3.0D) + " dodgemod=" + opponent.getCombatHandler().getDodgeMod() + " dodgeType=" + opponent.getStatus().getDodgeTypeModifier() + " dodgeMovePenalty=" + opponent.getMovePenalty() + " armour=" + (opponent.getMovementScheme()).armourMod.getModifier();
/* 2017 */           if (this.creature.spamMode() && Servers.isThisATestServer())
/*      */           {
/* 2019 */             this.creature.getCommunicator().sendCombatSafeMessage(dodgeDetails);
/*      */           }
/* 2021 */           this.creature.sendToLoggers(dodgeDetails, (byte)4);
/*      */           
/* 2023 */           checkIfHitVehicle(this.creature, opponent);
/*      */         } 
/*      */         
/* 2026 */         if (keepGoing)
/* 2027 */           hit = true; 
/*      */       } 
/*      */     }
/* 2030 */     if (hit || crit) {
/*      */       
/* 2032 */       this.creature.sendToLoggers("YOU DAMAGE " + weapon.getName(), (byte)2);
/* 2033 */       opponent.sendToLoggers(this.creature.getName() + " DAMAGE " + weapon.getName(), (byte)2);
/* 2034 */       dead = setDamage(opponent, weapon, damage, pos, type);
/*      */     } 
/*      */     
/* 2037 */     if (dead) {
/* 2038 */       setKillEffects(this.creature, opponent);
/*      */     }
/* 2040 */     if (miss) {
/*      */       
/* 2042 */       if (this.creature.spamMode())
/*      */       {
/* 2044 */         if (chanceToHit > 0.0F || (percent > 0.0F && parrPercent > 0.0F)) {
/*      */           
/* 2046 */           this.creature.getCommunicator().sendCombatNormalMessage("You miss with the " + weapon.getName() + ".");
/* 2047 */           this.creature.sendToLoggers("YOU MISS " + weapon.getName(), (byte)2);
/* 2048 */           opponent.sendToLoggers(this.creature.getName() + " MISS " + weapon.getName(), (byte)2);
/*      */         } 
/*      */       }
/* 2051 */       if (!this.creature.isUnique() && attCheck - chanceToHit > 50.0D && Server.rand.nextInt(10) == 0) {
/*      */         
/* 2053 */         justOpen = true;
/*      */         
/* 2055 */         setCurrentStance(-1, (byte)9);
/*      */         
/* 2057 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2058 */         segments.add(new CreatureLineSegment(this.creature));
/* 2059 */         segments.add(new MulticolorLineSegment(" makes a bad move and is an easy target!.", (byte)0));
/*      */         
/* 2061 */         opponent.getCommunicator().sendColoredMessageCombat(segments);
/* 2062 */         ((MulticolorLineSegment)segments.get(1)).setText(" make a bad move, making you an easy target.");
/* 2063 */         this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2071 */         this.creature.getCurrentTile().checkOpportunityAttacks(this.creature);
/* 2072 */         opponent.getCurrentTile().checkOpportunityAttacks(this.creature);
/*      */ 
/*      */       
/*      */       }
/* 2076 */       else if (Server.rand.nextInt(10) == 0) {
/* 2077 */         checkIfHitVehicle(this.creature, opponent);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2082 */     this.addToSkills = false;
/*      */ 
/*      */     
/* 2085 */     return dead;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void checkIfHitVehicle(Creature creature, Creature opponent) {
/* 2090 */     if (creature.isBreakFence() && opponent.getVehicle() > -10L) {
/*      */       
/* 2092 */       Vehicle vehic = Vehicles.getVehicleForId(opponent.getVehicle());
/* 2093 */       if (vehic != null)
/*      */       {
/* 2095 */         if (!vehic.creature) {
/*      */           
/*      */           try {
/*      */             
/* 2099 */             Item i = Items.getItem(opponent.getVehicle());
/*      */             
/* 2101 */             Server.getInstance().broadCastAction(creature
/* 2102 */                 .getNameWithGenus() + " hits the " + i
/* 2103 */                 .getName() + " with huge force!", creature, 10, true);
/* 2104 */             i.setDamage(i.getDamage() + (float)(damage / 300000.0D));
/*      */           }
/* 2106 */           catch (NoSuchItemException noSuchItemException) {}
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setAttString(Creature _creature, Item _weapon, byte _type) {
/* 2117 */     attString = CombatEngine.getAttackString(_creature, _weapon, _type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setAttString(String string) {
/* 2122 */     attString = string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setDamage(Creature defender, Item attWeapon, double ddamage, byte position, byte _type) {
/* 2129 */     float armourMod = defender.getArmourMod();
/*      */ 
/*      */     
/* 2132 */     float poisdam = 0.0F;
/* 2133 */     if (attWeapon.getSpellVenomBonus() > 0.0F)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2140 */       _type = 5;
/*      */     }
/*      */ 
/*      */     
/* 2144 */     if (attWeapon.enchantment == 90) {
/* 2145 */       _type = 10;
/* 2146 */     } else if (attWeapon.enchantment == 92) {
/* 2147 */       _type = 8;
/* 2148 */     } else if (attWeapon.enchantment == 91) {
/* 2149 */       _type = 4;
/*      */     } 
/*      */     
/* 2152 */     float infection = 0.0F;
/* 2153 */     if (attWeapon.getSpellExtraDamageBonus() > 0.0F) {
/*      */       
/* 2155 */       float bloodthirstPower = attWeapon.getSpellExtraDamageBonus();
/*      */ 
/*      */       
/* 2158 */       if (Server.rand.nextFloat() * 100000.0F <= bloodthirstPower) {
/*      */         
/* 2160 */         _type = 6;
/* 2161 */         infection = bloodthirstPower / 1000.0F;
/*      */       } 
/*      */     } 
/*      */     
/* 2165 */     boolean metalArmour = false;
/* 2166 */     Item armour = null;
/* 2167 */     float bounceWoundPower = 0.0F;
/* 2168 */     float evasionChance = ArmourTemplate.calculateGlanceRate(defender.getArmourType(), armour, _type, armourMod);
/*      */     
/* 2170 */     if (armourMod == 1.0F || defender.isVehicle() || defender.isKingdomGuard()) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 2175 */         byte bodyPosition = ArmourTemplate.getArmourPosition(position);
/* 2176 */         armour = defender.getArmour(bodyPosition);
/* 2177 */         if (!defender.isKingdomGuard()) {
/* 2178 */           armourMod = ArmourTemplate.calculateDR(armour, _type);
/*      */         }
/*      */         else {
/*      */           
/* 2182 */           armourMod *= ArmourTemplate.calculateDR(armour, _type);
/* 2183 */         }  defender.sendToLoggers("YOU ARMORMOD " + armourMod, (byte)2);
/* 2184 */         this.creature.sendToLoggers(defender.getName() + " ARMORMOD " + armourMod, (byte)2);
/* 2185 */         if (defender.isPlayer() || defender.isHorse())
/*      */         {
/* 2187 */           armour.setDamage(armour.getDamage() + 
/* 2188 */               Math.max(0.01F, 
/*      */                 
/* 2190 */                 Math.min(1.0F, 
/* 2191 */                   (float)(ddamage * Weapon.getMaterialArmourDamageBonus(attWeapon.getMaterial()) * ArmourTemplate.getArmourDamageModFor(armour, _type) / 1200000.0D) * armour
/* 2192 */                   .getDamageModifier())));
/*      */         }
/* 2194 */         CombatEngine.checkEnchantDestruction(attWeapon, armour, defender);
/* 2195 */         if (armour.isMetal())
/* 2196 */           metalArmour = true; 
/* 2197 */         if (!defender.isPlayer()) {
/* 2198 */           evasionChance = ArmourTemplate.calculateCreatureGlanceRate(_type, armour);
/*      */         } else {
/* 2200 */           evasionChance = ArmourTemplate.calculateGlanceRate(null, armour, _type, armourMod);
/*      */         } 
/* 2202 */         evasionChance *= 1.0F + ItemBonus.getGlanceBonusFor(armour.getArmourType(), _type, attWeapon, defender);
/*      */       }
/* 2204 */       catch (NoArmourException noArmourException) {
/*      */ 
/*      */       
/*      */       }
/* 2208 */       catch (NoSpaceException nsp) {
/*      */         
/* 2210 */         logger.log(Level.WARNING, defender.getName() + " no armour space on loc " + position);
/*      */       } 
/*      */ 
/*      */       
/* 2214 */       if ((armour == null || (armour.getArmourType() != null && armour.getArmourType().getLimitFactor() >= 0.0F)) && defender
/* 2215 */         .getBonusForSpellEffect((byte)22) > 0.0F) {
/*      */         
/* 2217 */         if (!CombatEngine.isEye(position) || defender.isUnique()) {
/*      */           
/* 2219 */           float omod = 100.0F;
/* 2220 */           float minmod = 0.6F;
/* 2221 */           if (!defender.isPlayer()) {
/*      */             
/* 2223 */             omod = 300.0F;
/* 2224 */             minmod = 0.7F;
/*      */           }
/* 2226 */           else if (defender.getBonusForSpellEffect((byte)22) > 70.0F) {
/* 2227 */             bounceWoundPower = defender.getBonusForSpellEffect((byte)22);
/* 2228 */           }  if (armourMod >= 1.0F)
/*      */           {
/*      */             
/* 2231 */             armourMod = 0.3F + (float)(1.0D - Server.getBuffedQualityEffect((defender
/* 2232 */                 .getBonusForSpellEffect((byte)22) / omod))) * minmod;
/*      */             
/* 2234 */             evasionChance = (float)Server.getBuffedQualityEffect((defender
/* 2235 */                 .getBonusForSpellEffect((byte)22) / 100.0F)) / 3.0F;
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 2240 */             armourMod = Math.min(armourMod, 0.3F + 
/* 2241 */                 (float)(1.0D - Server.getBuffedQualityEffect((defender
/* 2242 */                   .getBonusForSpellEffect((byte)22) / omod))) * minmod);
/*      */           }
/*      */         
/*      */         } 
/* 2246 */       } else if (defender.isReborn()) {
/*      */         
/* 2248 */         armourMod = (float)(1.0D - Server.getBuffedQualityEffect(defender.getStrengthSkill() / 100.0D));
/*      */       } 
/*      */     } 
/*      */     
/* 2252 */     if (defender.isUnique())
/*      */     {
/* 2254 */       evasionChance = 0.5F;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2260 */     if (!attWeapon.isBodyPartAttached())
/*      */     {
/* 2262 */       if (this.creature.isPlayer()) {
/*      */         
/* 2264 */         boolean rust = defender.hasSpellEffect((byte)70);
/* 2265 */         if (rust)
/*      */         {
/* 2267 */           this.creature.getCommunicator().sendAlertServerMessage("Your " + attWeapon
/* 2268 */               .getName() + " takes excessive damage from " + defender.getNameWithGenus() + ".");
/*      */         }
/* 2270 */         float mod = rust ? 5.0F : 1.0F;
/* 2271 */         attWeapon.setDamage(attWeapon.getDamage() + Math.min(1.0F, (float)(ddamage * armourMod / 1000000.0D)) * attWeapon
/* 2272 */             .getDamageModifier() * mod);
/*      */       } 
/*      */     }
/*      */     
/* 2276 */     double defdamage = ddamage * ItemBonus.getDamReductionBonusFor((armour != null) ? armour.getArmourType() : defender.getArmourType(), _type, attWeapon, defender);
/*      */     
/* 2278 */     if (attWeapon.getSpellVenomBonus() > 0.0F)
/*      */     {
/*      */ 
/*      */       
/* 2282 */       defdamage *= (0.8F + 0.2F * attWeapon.getSpellVenomBonus() / 100.0F);
/*      */     }
/*      */     
/* 2285 */     if (defender.isPlayer()) {
/*      */       
/* 2287 */       if (((Player)defender).getAlcohol() > 50.0F)
/*      */       {
/* 2289 */         defdamage *= 0.5D;
/*      */       }
/* 2291 */       if (defender.fightlevel >= 5)
/* 2292 */         defdamage *= 0.5D; 
/*      */     } 
/* 2294 */     if (defender.hasTrait(2)) {
/* 2295 */       defdamage *= 0.8999999761581421D;
/*      */     }
/*      */     
/* 2298 */     float demiseBonus = EnchantUtil.getDemiseBonus(attWeapon, defender);
/* 2299 */     if (demiseBonus > 0.0F) {
/* 2300 */       defdamage *= (1.0F + demiseBonus);
/*      */     }
/* 2302 */     if (this.creature.hasSpellEffect((byte)67) && !attWeapon.isArtifact())
/* 2303 */       crit = true; 
/* 2304 */     if (crit && !defender.isUnique())
/*      */     {
/* 2306 */       armourMod *= 1.5F;
/*      */     }
/* 2308 */     if (defender.getTemplate().isTowerBasher())
/*      */     {
/*      */       
/* 2311 */       if (this.creature.isSpiritGuard() || this.creature.isKingdomGuard()) {
/*      */         
/* 2313 */         float mod = 1.0F / defender.getArmourMod();
/* 2314 */         defdamage = Math.max(((500 + Server.rand.nextInt(1000)) * mod), defdamage);
/*      */       } 
/*      */     }
/* 2317 */     if (Server.rand.nextFloat() < evasionChance) {
/*      */       
/* 2319 */       if (this.creature.spamMode()) {
/*      */         
/* 2321 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2322 */         segments.add(new MulticolorLineSegment("Your attack glances off ", (byte)0));
/* 2323 */         segments.add(new CreatureLineSegment(defender));
/* 2324 */         segments.add(new MulticolorLineSegment("'s armour.", (byte)0));
/*      */         
/* 2326 */         this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */       
/* 2330 */       if (defender.spamMode())
/* 2331 */         defender.getCommunicator().sendCombatNormalMessage("The attack to the " + defender
/* 2332 */             .getBody().getWoundLocationString(pos) + " glances off your armour."); 
/* 2333 */       this.creature.sendToLoggers(defender.getName() + " GLANCE", (byte)2);
/* 2334 */       defender.sendToLoggers("YOU GLANCE", (byte)2);
/*      */     }
/* 2336 */     else if (defdamage * armourMod >= 500.0D) {
/*      */       
/* 2338 */       if (this.creature.hasSpellEffect((byte)67))
/*      */       {
/* 2340 */         if (!attWeapon.isArtifact())
/* 2341 */           this.creature.removeTrueStrike(); 
/*      */       }
/* 2343 */       if (attWeapon != null && !attWeapon.isBodyPartRemoved())
/*      */       {
/* 2345 */         if (!attWeapon.isWeaponBow()) {
/*      */           
/*      */           try {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2359 */             int primweaponskill = 10052;
/* 2360 */             if (!attWeapon.isBodyPartAttached()) {
/* 2361 */               primweaponskill = attWeapon.getPrimarySkill();
/*      */             }
/*      */ 
/*      */             
/*      */             try {
/* 2366 */               Skill pwsk = this.creature.getSkills().getSkill(primweaponskill);
/*      */               
/* 2368 */               double d = pwsk.skillCheck(pwsk.getKnowledge(), attWeapon, 0.0D, defender.isNoSkillFor(this.creature), (float)defdamage * armourMod / 1000.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 2377 */             catch (NoSuchSkillException nss1) {
/*      */               
/* 2379 */               this.creature.getSkills().learn(primweaponskill, 1.0F);
/*      */             }
/*      */           
/*      */           }
/* 2383 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2390 */       if (Servers.isThisATestServer()) {
/*      */         
/* 2392 */         String message = String.format("Base Damage: %.1f, Armour DR: %.2f%%, Final Damage: %.1f. Critical: %s", new Object[] {
/* 2393 */               Double.valueOf(defdamage), Float.valueOf((1.0F - armourMod) * 100.0F), Double.valueOf(defdamage * armourMod), Boolean.valueOf(crit) });
/* 2394 */         if (this.creature.spamMode())
/* 2395 */           this.creature.getCommunicator().sendCombatSafeMessage(message); 
/* 2396 */         if (defender.spamMode()) {
/* 2397 */           defender.getCommunicator().sendCombatAlertMessage(message);
/*      */         }
/*      */       } 
/* 2400 */       this.creature.sendToLoggers(defender.getName() + " DAMAGED " + (defdamage * armourMod) + " crit=" + crit, (byte)2);
/* 2401 */       defender.sendToLoggers("YOU DAMAGED " + (defdamage * armourMod) + " crit=" + crit, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2406 */       Battle battle = defender.getBattle();
/*      */       
/* 2408 */       dead = false;
/*      */       
/* 2410 */       float champMod = defender.isChampion() ? 0.4F : 1.0F;
/* 2411 */       if (armour != null)
/*      */       {
/* 2413 */         if (armour.getSpellPainShare() > 0.0F) {
/*      */           
/* 2415 */           bounceWoundPower = armour.getSpellPainShare();
/* 2416 */           int rarityModifier = Math.max(1, armour.getRarity() * 5);
/* 2417 */           SpellEffect speff = armour.getSpellEffect((byte)17);
/* 2418 */           if (speff != null)
/*      */           {
/* 2420 */             if (Server.rand.nextInt(Math.max(2, (int)(rarityModifier * speff.power * 80.0F))) == 0) {
/*      */               
/* 2422 */               speff.setPower(speff.getPower() - 1.0F);
/* 2423 */               if (speff.getPower() <= 0.0F) {
/*      */                 
/* 2425 */                 ItemSpellEffects speffs = armour.getSpellEffects();
/* 2426 */                 if (speffs != null) {
/* 2427 */                   speffs.removeSpellEffect(speff.type);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/* 2434 */       if (defender.isUnique() && this.creature.isUnique())
/*      */       {
/*      */         
/* 2437 */         if ((defender.getStatus()).damage > 10000) {
/*      */           
/* 2439 */           defender.setTarget(-10L, true);
/* 2440 */           this.creature.setTarget(-10L, true);
/* 2441 */           defender.setOpponent(null);
/* 2442 */           this.creature.setOpponent(null);
/*      */           
/*      */           try {
/* 2445 */             defender.checkMove();
/*      */           }
/* 2447 */           catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/* 2453 */             this.creature.checkMove();
/*      */           }
/* 2455 */           catch (Exception exception) {}
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 2461 */       if (defender.isSparring(this.creature)) {
/*      */         
/* 2463 */         if ((defender.getStatus()).damage + defdamage * armourMod * 2.0D > 65535.0D) {
/*      */           
/* 2465 */           defender.setTarget(-10L, true);
/* 2466 */           this.creature.setTarget(-10L, true);
/* 2467 */           defender.setOpponent(null);
/* 2468 */           this.creature.setOpponent(null);
/* 2469 */           this.creature.getCommunicator().sendCombatSafeMessage("You win against " + defender
/* 2470 */               .getName() + "! Congratulations!");
/* 2471 */           defender.getCommunicator().sendCombatNormalMessage("You lose against " + this.creature
/* 2472 */               .getName() + " who stops just before finishing you off!");
/* 2473 */           Server.getInstance().broadCastAction(this.creature
/* 2474 */               .getName() + " defeats " + defender.getName() + " while sparring!", this.creature, defender, 10);
/* 2475 */           this.creature.getCommunicator().sendCombatOptions(NO_COMBAT_OPTIONS, (short)0);
/* 2476 */           this.creature.getCommunicator().sendSpecialMove((short)-1, "N/A");
/* 2477 */           this.creature.achievement(39);
/*      */           
/* 2479 */           if (!Servers.localServer.PVPSERVER)
/* 2480 */             this.creature.achievement(8); 
/* 2481 */           Item weapon = this.creature.getPrimWeapon();
/* 2482 */           if (weapon != null) {
/*      */             
/* 2484 */             if (weapon.isWeaponBow()) {
/* 2485 */               this.creature.achievement(11);
/* 2486 */             } else if (weapon.isWeaponSword()) {
/* 2487 */               this.creature.achievement(14);
/* 2488 */             } else if (weapon.isWeaponCrush()) {
/* 2489 */               this.creature.achievement(17);
/* 2490 */             } else if (weapon.isWeaponAxe()) {
/* 2491 */               this.creature.achievement(20);
/* 2492 */             } else if (weapon.isWeaponKnife()) {
/* 2493 */               this.creature.achievement(25);
/* 2494 */             }  if (weapon.getTemplateId() == 314) {
/* 2495 */               this.creature.achievement(27);
/* 2496 */             } else if (weapon.getTemplateId() == 567) {
/* 2497 */               this.creature.achievement(29);
/* 2498 */             } else if (weapon.getTemplateId() == 20) {
/* 2499 */               this.creature.achievement(30);
/*      */             } 
/*      */           } 
/* 2502 */           return true;
/*      */         } 
/* 2504 */         if (bounceWoundPower > 0.0F)
/*      */         {
/* 2506 */           if (defdamage * bounceWoundPower * champMod / 300.0D > 500.0D)
/*      */           {
/* 2508 */             if ((this.creature.getStatus()).damage + defdamage * bounceWoundPower * champMod / 300.0D > 65535.0D) {
/*      */               
/* 2510 */               defender.setTarget(-10L, true);
/* 2511 */               this.creature.setTarget(-10L, true);
/* 2512 */               defender.setOpponent(null);
/* 2513 */               this.creature.setOpponent(null);
/* 2514 */               defender.getCommunicator().sendCombatSafeMessage("You win against " + this.creature
/* 2515 */                   .getName() + "! Congratulations!");
/* 2516 */               this.creature.getCommunicator().sendCombatNormalMessage("You lose against " + defender
/* 2517 */                   .getName() + " whose armour enchantment almost finished you off!");
/*      */               
/* 2519 */               Server.getInstance().broadCastAction(defender
/* 2520 */                   .getName() + " defeats " + this.creature.getName() + " while sparring!", defender, this.creature, 10);
/*      */               
/* 2522 */               this.creature.getCommunicator().sendCombatOptions(NO_COMBAT_OPTIONS, (short)0);
/* 2523 */               this.creature.getCommunicator().sendSpecialMove((short)-1, "N/A");
/* 2524 */               this.creature.achievement(39);
/*      */               
/* 2526 */               if (!Servers.localServer.PVPSERVER)
/* 2527 */                 this.creature.achievement(8); 
/* 2528 */               Item weapon = this.creature.getPrimWeapon();
/* 2529 */               if (weapon != null) {
/*      */                 
/* 2531 */                 if (weapon.isWeaponBow()) {
/* 2532 */                   this.creature.achievement(11);
/* 2533 */                 } else if (weapon.isWeaponSword()) {
/* 2534 */                   this.creature.achievement(14);
/* 2535 */                 } else if (weapon.isWeaponCrush()) {
/* 2536 */                   this.creature.achievement(17);
/* 2537 */                 } else if (weapon.isWeaponAxe()) {
/* 2538 */                   this.creature.achievement(20);
/* 2539 */                 } else if (weapon.isWeaponKnife()) {
/* 2540 */                   this.creature.achievement(25);
/* 2541 */                 }  if (weapon.getTemplateId() == 314) {
/* 2542 */                   this.creature.achievement(27);
/* 2543 */                 } else if (weapon.getTemplateId() == 567) {
/* 2544 */                   this.creature.achievement(29);
/* 2545 */                 } else if (weapon.getTemplateId() == 20) {
/* 2546 */                   this.creature.achievement(30);
/*      */                 } 
/*      */               } 
/* 2549 */               return true;
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/* 2554 */       if (defender.getStaminaSkill().getKnowledge() < 2.0D) {
/*      */         
/* 2556 */         defender.die(false, "Combat Stam Check Fail");
/* 2557 */         this.creature.achievement(223);
/* 2558 */         dead = true;
/*      */       }
/* 2560 */       else if (attWeapon.getWeaponSpellDamageBonus() > 0.0F) {
/*      */ 
/*      */         
/* 2563 */         defdamage += defdamage * attWeapon.getWeaponSpellDamageBonus() / 500.0D;
/* 2564 */         dead = CombatEngine.addWound(this.creature, defender, _type, position, defdamage, armourMod, attString, battle, Server.rand
/* 2565 */             .nextInt((int)Math.max(1.0F, attWeapon.getWeaponSpellDamageBonus())), poisdam, false, false, false, false);
/* 2566 */         if (attWeapon.isWeaponCrush() && attWeapon.getWeightGrams() > 4000 && 
/* 2567 */           armour != null && armour.getTemplateId() == 286) {
/* 2568 */           defender.achievement(49);
/*      */         }
/*      */       } else {
/*      */         
/* 2572 */         int dmgBefore = (defender.getStatus()).damage;
/*      */ 
/*      */         
/* 2575 */         dead = CombatEngine.addWound(this.creature, defender, _type, position, defdamage, armourMod, attString, battle, infection, poisdam, false, false, false, false);
/*      */ 
/*      */ 
/*      */         
/* 2579 */         float lifeTransferPower = Math.max(attWeapon.getSpellLifeTransferModifier(), attWeapon.getSpellEssenceDrainModifier() / 3.0F);
/* 2580 */         if (lifeTransferPower > 0.0F && dmgBefore != (defender.getStatus()).damage)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2586 */           if (this.creature.getBody() != null && this.creature.getBody().getWounds() != null) {
/*      */ 
/*      */             
/* 2589 */             Wound[] w = this.creature.getBody().getWounds().getWounds();
/*      */             
/* 2591 */             if (w.length > 0) {
/*      */ 
/*      */               
/* 2594 */               float mod = 500.0F;
/* 2595 */               if (this.creature.isChampion()) {
/* 2596 */                 mod = 1000.0F;
/* 2597 */               } else if (this.creature.getCultist() != null && this.creature.getCultist().healsFaster()) {
/* 2598 */                 mod = 250.0F;
/*      */               } 
/*      */               
/* 2601 */               double toHeal = defdamage * lifeTransferPower / mod;
/* 2602 */               double resistance = SpellResist.getSpellResistance(this.creature, 409);
/* 2603 */               toHeal *= resistance;
/*      */               
/* 2605 */               Wound targetWound = w[0];
/* 2606 */               for (Wound wound : w) {
/*      */                 
/* 2608 */                 if (wound.getSeverity() > targetWound.getSeverity()) {
/* 2609 */                   targetWound = wound;
/*      */                 }
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2618 */               SpellResist.addSpellResistance(this.creature, 409, Math.min(targetWound.getSeverity(), toHeal));
/*      */               
/* 2620 */               targetWound.modifySeverity(-((int)toHeal));
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 2627 */       if (this.creature.isPlayer() != defender.isPlayer())
/*      */       {
/* 2629 */         if (defdamage > 10000.0D)
/*      */         {
/* 2631 */           if (defender.fightlevel > 0) {
/*      */             
/* 2633 */             defender.fightlevel = (byte)(defender.fightlevel - 1);
/* 2634 */             defender.getCommunicator().sendCombatNormalMessage("You lose some focus.");
/* 2635 */             if (defender.isPlayer()) {
/* 2636 */               defender.getCommunicator().sendFocusLevel(defender.getWurmId());
/*      */             }
/*      */           } 
/*      */         }
/*      */       }
/*      */       
/* 2642 */       if (!dead && attWeapon.getSpellDamageBonus() > 0.0F)
/*      */       {
/* 2644 */         if ((attWeapon.getSpellDamageBonus() / 300.0F) * defdamage > 500.0D || crit) {
/* 2645 */           dead = defender.addWoundOfType(this.creature, (byte)4, position, false, armourMod, false, (attWeapon
/* 2646 */               .getSpellDamageBonus() / 300.0F) * defdamage, 0.0F, 0.0F, true, true);
/*      */         }
/*      */       }
/*      */       
/* 2650 */       if (!dead && attWeapon.getSpellFrostDamageBonus() > 0.0F)
/*      */       {
/* 2652 */         if ((attWeapon.getSpellFrostDamageBonus() / 300.0F) * defdamage > 500.0D || crit) {
/* 2653 */           dead = defender.addWoundOfType(this.creature, (byte)8, position, false, armourMod, false, (attWeapon
/* 2654 */               .getSpellFrostDamageBonus() / 300.0F) * defdamage, 0.0F, 0.0F, true, true);
/*      */         }
/*      */       }
/*      */       
/* 2658 */       if (!dead && attWeapon.getSpellEssenceDrainModifier() > 0.0F)
/*      */       {
/* 2660 */         if ((attWeapon.getSpellEssenceDrainModifier() / 1000.0F) * defdamage > 500.0D || crit)
/*      */         {
/* 2662 */           dead = defender.addWoundOfType(this.creature, (byte)9, position, false, armourMod, false, (attWeapon
/* 2663 */               .getSpellEssenceDrainModifier() / 1000.0F) * defdamage, 0.0F, 0.0F, true, true);
/*      */         }
/*      */       }
/*      */ 
/*      */       
/* 2668 */       if (!dead && Weapon.getMaterialExtraWoundMod(attWeapon.getMaterial()) > 0.0F) {
/*      */         
/* 2670 */         float extraDmg = Weapon.getMaterialExtraWoundMod(attWeapon.getMaterial());
/* 2671 */         if (extraDmg * defdamage > 500.0D || crit) {
/* 2672 */           dead = defender.addWoundOfType(this.creature, Weapon.getMaterialExtraWoundType(attWeapon.getMaterial()), position, false, armourMod, false, extraDmg * defdamage, 0.0F, 0.0F, false, true);
/*      */         }
/*      */       } 
/*      */       
/* 2676 */       if (armour != null || bounceWoundPower > 0.0F)
/*      */       {
/* 2678 */         if (bounceWoundPower > 0.0F) {
/*      */           
/* 2680 */           if (this.creature.isUnique())
/*      */           {
/* 2682 */             if (armour != null) {
/* 2683 */               defender.getCommunicator().sendCombatNormalMessage(this.creature
/* 2684 */                   .getNameWithGenus() + " ignores the effects of the " + armour.getName() + ".");
/*      */             
/*      */             }
/*      */           }
/* 2688 */           else if (defdamage * bounceWoundPower * champMod / 300.0D > 500.0D)
/*      */           {
/* 2690 */             CombatEngine.addBounceWound(defender, this.creature, _type, position, defdamage * bounceWoundPower * champMod / 300.0D, armourMod, 0.0F, 0.0F, false, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
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
/*      */         }
/* 2710 */         else if (armour != null && armour.getSpellSlowdown() > 0.0F) {
/*      */ 
/*      */           
/* 2713 */           if (this.creature.getMovementScheme().setWebArmourMod(true, armour.getSpellSlowdown())) {
/*      */ 
/*      */ 
/*      */             
/* 2717 */             this.creature.setWebArmourModTime(armour.getSpellSlowdown() / 10.0F);
/* 2718 */             this.creature.getCommunicator().sendCombatAlertMessage("Dark stripes spread along your " + attWeapon
/* 2719 */                 .getName() + " from " + defender
/* 2720 */                 .getNamePossessive() + " armour. You feel drained.");
/*      */           } 
/* 2722 */           int rm = Math.max(1, armour.getRarity() * 5);
/* 2723 */           SpellEffect speff = armour.getSpellEffect((byte)46);
/* 2724 */           if (speff != null)
/*      */           {
/* 2726 */             if (Server.rand.nextInt(Math.max(2, (int)(rm * speff.power * 80.0F))) == 0) {
/*      */               
/* 2728 */               speff.setPower(speff.getPower() - 1.0F);
/* 2729 */               if (speff.getPower() <= 0.0F) {
/*      */                 
/* 2731 */                 ItemSpellEffects speffs = armour.getSpellEffects();
/* 2732 */                 if (speffs != null) {
/* 2733 */                   speffs.removeSpellEffect(speff.type);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/* 2740 */       if (!Players.getInstance().isOverKilling(this.creature.getWurmId(), defender.getWurmId()))
/*      */       {
/* 2742 */         if (attWeapon.getSpellExtraDamageBonus() > 0.0F)
/*      */         {
/* 2744 */           if (defender.isPlayer() && !defender.isNewbie()) {
/*      */             
/* 2746 */             SpellEffect speff = attWeapon.getSpellEffect((byte)45);
/* 2747 */             float mod = 1.0F;
/* 2748 */             if (defdamage * armourMod * champMod < 5000.0D)
/* 2749 */               mod = (float)(defdamage * armourMod * champMod / 5000.0D); 
/* 2750 */             if (speff != null) {
/* 2751 */               speff.setPower(Math.min(10000.0F, speff.power + (dead ? 20.0F : (2.0F * mod))));
/*      */             }
/* 2753 */           } else if (!defender.isPlayer() && !defender.isGuard() && dead) {
/*      */             
/* 2755 */             SpellEffect speff = attWeapon.getSpellEffect((byte)45);
/* 2756 */             float mod = 1.0F;
/* 2757 */             if (speff.getPower() > 5000.0F && !Servers.isThisAnEpicOrChallengeServer())
/* 2758 */               mod = Math.max(0.5F, 1.0F - (speff.getPower() - 5000.0F) / 5000.0F); 
/* 2759 */             if (speff != null)
/* 2760 */               speff.setPower(Math.min(10000.0F, speff.power + defender.getBaseCombatRating() * mod)); 
/*      */           } 
/*      */         }
/*      */       }
/* 2764 */       if (dead) {
/*      */ 
/*      */         
/* 2767 */         if (battle != null)
/* 2768 */           battle.addCasualty(this.creature, defender); 
/* 2769 */         if (defender.isSparring(this.creature))
/*      */         {
/* 2771 */           if ((defender.getStatus()).damage + defdamage * armourMod * 2.0D > 65535.0D) {
/*      */             
/* 2773 */             this.creature.achievement(39);
/*      */             
/* 2775 */             if (!Servers.localServer.PVPSERVER)
/* 2776 */               this.creature.achievement(8); 
/* 2777 */             Item weapon = this.creature.getPrimWeapon();
/* 2778 */             if (weapon != null) {
/*      */               
/* 2780 */               if (weapon.isWeaponBow()) {
/* 2781 */                 this.creature.achievement(11);
/* 2782 */               } else if (weapon.isWeaponSword()) {
/* 2783 */                 this.creature.achievement(14);
/* 2784 */               } else if (weapon.isWeaponCrush()) {
/* 2785 */                 this.creature.achievement(17);
/* 2786 */               } else if (weapon.isWeaponAxe()) {
/* 2787 */                 this.creature.achievement(20);
/* 2788 */               } else if (weapon.isWeaponKnife()) {
/* 2789 */                 this.creature.achievement(25);
/* 2790 */               }  if (weapon.getTemplateId() == 314) {
/* 2791 */                 this.creature.achievement(27);
/* 2792 */               } else if (weapon.getTemplateId() == 567) {
/* 2793 */                 this.creature.achievement(29);
/* 2794 */               } else if (weapon.getTemplateId() == 20) {
/* 2795 */                 this.creature.achievement(30);
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2802 */             this.creature.getCommunicator().sendCombatSafeMessage("You accidentally slay " + defender
/* 2803 */                 .getName() + "! Congratulations!");
/* 2804 */             defender.getCommunicator().sendCombatNormalMessage("You lose against " + this.creature
/* 2805 */                 .getName() + " who unfortunately fails to stop just before finishing you off!");
/*      */             
/* 2807 */             Server.getInstance().broadCastAction(this.creature
/* 2808 */                 .getName() + " defeats and accidentally slays " + defender.getName() + " while sparring!", this.creature, defender, 10);
/*      */           } 
/*      */         }
/*      */         
/* 2812 */         if (this.creature.isDuelling(defender))
/*      */         {
/* 2814 */           this.creature.achievement(37);
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 2819 */       else if (defdamage > 30000.0D) {
/*      */         
/* 2821 */         if (Server.rand.nextInt(100000) < defdamage) {
/*      */           
/* 2823 */           Skill defBodyControl = null;
/*      */           
/*      */           try {
/* 2826 */             defBodyControl = defender.getSkills().getSkill(104);
/*      */           }
/* 2828 */           catch (NoSuchSkillException nss) {
/*      */             
/* 2830 */             defBodyControl = defender.getSkills().learn(104, 1.0F);
/*      */           } 
/*      */           
/* 2833 */           if (defBodyControl.skillCheck(defdamage / 10000.0D, (defender
/* 2834 */               .getCombatHandler().getFootingModifier(attWeapon, this.creature) * 10.0F), false, 10.0F, defender, this.creature) < 0.0D) {
/*      */ 
/*      */             
/* 2837 */             defender.getCombatHandler().setCurrentStance(-1, (byte)8);
/* 2838 */             defender.getStatus().setStunned((byte)(int)Math.max(3.0D, defdamage / 10000.0D), false);
/*      */             
/* 2840 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2841 */             segments.add(new CreatureLineSegment(defender));
/* 2842 */             segments.add(new MulticolorLineSegment(" is knocked senseless from the hit.", (byte)0));
/*      */             
/* 2844 */             this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */             
/* 2847 */             defender.getCommunicator().sendCombatNormalMessage("You are knocked senseless from the hit.");
/*      */             
/* 2849 */             segments.clear();
/* 2850 */             segments.add(new CreatureLineSegment(this.creature));
/* 2851 */             segments.add(new MulticolorLineSegment(" knocks ", (byte)0));
/* 2852 */             segments.add(new CreatureLineSegment(defender));
/* 2853 */             segments.add(new MulticolorLineSegment(" senseless with " + this.creature.getHisHerItsString() + " hit!", (byte)0));
/*      */             
/* 2855 */             MessageServer.broadcastColoredAction(segments, this.creature, defender, 5, true);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2863 */       int numsound = Server.rand.nextInt(3);
/* 2864 */       if (defdamage > 10000.0D) {
/*      */         
/* 2866 */         if (numsound == 0) {
/* 2867 */           SoundPlayer.playSound("sound.combat.fleshbone1", defender, 1.6F);
/* 2868 */         } else if (numsound == 1) {
/* 2869 */           SoundPlayer.playSound("sound.combat.fleshbone2", defender, 1.6F);
/* 2870 */         } else if (numsound == 2) {
/* 2871 */           SoundPlayer.playSound("sound.combat.fleshbone3", defender, 1.6F);
/*      */         } 
/* 2873 */       } else if (metalArmour) {
/*      */         
/* 2875 */         if (numsound == 0) {
/* 2876 */           SoundPlayer.playSound("sound.combat.fleshmetal1", defender, 1.6F);
/* 2877 */         } else if (numsound == 1) {
/* 2878 */           SoundPlayer.playSound("sound.combat.fleshmetal2", defender, 1.6F);
/* 2879 */         } else if (numsound == 2) {
/* 2880 */           SoundPlayer.playSound("sound.combat.fleshmetal3", defender, 1.6F);
/*      */         }
/*      */       
/*      */       }
/* 2884 */       else if (numsound == 0) {
/* 2885 */         SoundPlayer.playSound("sound.combat.fleshhit1", defender, 1.6F);
/* 2886 */       } else if (numsound == 1) {
/* 2887 */         SoundPlayer.playSound("sound.combat.fleshhit2", defender, 1.6F);
/* 2888 */       } else if (numsound == 2) {
/* 2889 */         SoundPlayer.playSound("sound.combat.fleshhit3", defender, 1.6F);
/*      */       } 
/* 2891 */       SoundPlayer.playSound(defender.getHitSound(), defender, 1.6F);
/*      */     }
/*      */     else {
/*      */       
/* 2895 */       if (aiming || this.creature.spamMode()) {
/*      */         
/* 2897 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2898 */         segments.add(new CreatureLineSegment(defender));
/* 2899 */         segments.add(new MulticolorLineSegment(" takes no real damage from the hit to the " + defender
/* 2900 */               .getBody().getWoundLocationString(position) + ".", (byte)0));
/*      */         
/* 2902 */         this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2908 */       if (defender.spamMode()) {
/* 2909 */         defender.getCommunicator().sendCombatNormalMessage("You take no real damage from the blow to the " + defender
/* 2910 */             .getBody().getWoundLocationString(position) + ".");
/*      */       }
/* 2912 */       this.creature.sendToLoggers(defender.getName() + " NO DAMAGE", (byte)2);
/* 2913 */       defender.sendToLoggers("YOU TAKE NO DAMAGE", (byte)2);
/*      */     } 
/* 2915 */     return dead;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setDefenderWeaponSkill(Item defPrimWeapon) {
/* 2920 */     int skillnum = -10;
/* 2921 */     if (defPrimWeapon != null)
/*      */     {
/* 2923 */       if (defPrimWeapon.isBodyPart()) {
/*      */ 
/*      */         
/*      */         try {
/* 2927 */           skillnum = 10052;
/* 2928 */           defPrimWeaponSkill = defenderSkills.getSkill(skillnum);
/*      */         }
/* 2930 */         catch (NoSuchSkillException nss) {
/*      */           
/* 2932 */           if (skillnum != -10) {
/* 2933 */             defPrimWeaponSkill = defenderSkills.learn(skillnum, 1.0F);
/*      */           }
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/* 2940 */           skillnum = defPrimWeapon.getPrimarySkill();
/* 2941 */           defPrimWeaponSkill = defenderSkills.getSkill(skillnum);
/*      */         }
/* 2943 */         catch (NoSuchSkillException nss) {
/*      */           
/* 2945 */           if (skillnum != -10) {
/* 2946 */             defPrimWeaponSkill = defenderSkills.learn(skillnum, 1.0F);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static float getWeaponParryBonus(Item weapon) {
/* 2954 */     if (weapon.isWeaponSword())
/* 2955 */       return 2.0F; 
/* 2956 */     return 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float checkDefenderParry(Creature defender, Item attWeapon) {
/* 2964 */     defCheck = 0.0D;
/* 2965 */     boolean parried = false;
/*      */ 
/*      */ 
/*      */     
/* 2969 */     int parryTime = 200;
/* 2970 */     if (defender.getFightStyle() == 2) {
/* 2971 */       parryTime = 120;
/* 2972 */     } else if (defender.getFightStyle() == 1) {
/* 2973 */       parryTime = 360;
/*      */     } 
/* 2975 */     parryBonus = getParryBonus((defender.getCombatHandler()).currentStance, this.currentStance);
/*      */ 
/*      */     
/* 2978 */     if (defender.fightlevel > 0) {
/* 2979 */       parryBonus -= (defender.fightlevel * 4) / 100.0F;
/*      */     }
/* 2981 */     if (defender.getPrimWeapon() != null) {
/* 2982 */       parryBonus *= Weapon.getMaterialParryBonus(defender.getPrimWeapon().getMaterial());
/*      */     }
/*      */ 
/*      */     
/* 2986 */     parryTime = (int)(parryTime * parryBonus);
/*      */     
/* 2988 */     if (WurmCalendar.currentTime > defender.lastParry + Server.rand.nextInt(parryTime)) {
/*      */       
/* 2990 */       defParryWeapon = defender.getPrimWeapon();
/* 2991 */       if (Weapon.getWeaponParryPercent(defParryWeapon) > 0.0F) {
/*      */         
/* 2993 */         if (defParryWeapon.isTwoHanded() && defShield != null) {
/*      */           
/* 2995 */           defParryWeapon = null;
/* 2996 */           parried = false;
/*      */         } else {
/*      */           
/* 2999 */           parried = true;
/*      */         } 
/*      */       } else {
/* 3002 */         defParryWeapon = null;
/*      */       } 
/* 3004 */       if ((!parried || Server.rand.nextInt(3) == 0) && defShield == null) {
/*      */         
/* 3006 */         defLeftWeapon = defender.getLefthandWeapon();
/* 3007 */         if (defLeftWeapon != defParryWeapon) {
/*      */ 
/*      */           
/* 3010 */           if (defLeftWeapon != null)
/*      */           {
/* 3012 */             if (defLeftWeapon.getSizeZ() > defender.getSize() * 10 || 
/* 3013 */               Weapon.getWeaponParryPercent(defLeftWeapon) <= 0.0F) {
/* 3014 */               defLeftWeapon = null;
/*      */             }
/*      */           }
/* 3017 */           if (defLeftWeapon != null)
/*      */           {
/*      */             
/* 3020 */             if (defParryWeapon != null && parried) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 3025 */               if (defLeftWeapon.getSizeZ() > defParryWeapon.getSizeZ())
/*      */               {
/* 3027 */                 defParryWeapon = defLeftWeapon;
/*      */               }
/*      */             } else {
/*      */               
/* 3031 */               defParryWeapon = defLeftWeapon;
/*      */             }  } 
/*      */         } 
/*      */       } 
/* 3035 */       if (defParryWeapon != null && Weapon.getWeaponParryPercent(defParryWeapon) > Server.rand.nextFloat()) {
/*      */         
/* 3037 */         defCheck = -1.0D;
/* 3038 */         if (defender.getStatus().getStamina() >= 300) {
/*      */           
/* 3040 */           setDefenderWeaponSkill(defParryWeapon);
/* 3041 */           if (defPrimWeaponSkill != null)
/*      */           {
/*      */             
/* 3044 */             if (!defender.isMoving() || defPrimWeaponSkill.getRealKnowledge() > 40.0D) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 3050 */               double pdiff = Math.max(1.0D, (attCheck - defBonus + (defParryWeapon
/* 3051 */                   .getWeightGrams() / 100.0F)) / getWeaponParryBonus(defParryWeapon) * (1.0D - 
/* 3052 */                   getParryMod()));
/* 3053 */               if (!defender.isPlayer())
/* 3054 */                 pdiff *= defender.getStatus().getParryTypeModifier(); 
/* 3055 */               defCheck = defPrimWeaponSkill.skillCheck(pdiff * ItemBonus.getParryBonus(defender, defParryWeapon), defParryWeapon, 0.0D, (this.creature.isNoSkillFor(defender) || defParryWeapon
/* 3056 */                   .isWeaponBow()), 1.0F, defender, this.creature);
/* 3057 */               defender.lastParry = WurmCalendar.currentTime;
/* 3058 */               defender.getStatus().modifyStamina(-300.0F);
/*      */             } 
/*      */           }
/* 3061 */           if (defCheck < 0.0D && Server.rand.nextInt(20) == 0)
/*      */           {
/*      */ 
/*      */             
/* 3065 */             if (defLeftWeapon != null && !defLeftWeapon.equals(defParryWeapon)) {
/*      */               
/* 3067 */               setDefenderWeaponSkill(defLeftWeapon);
/* 3068 */               if (!defender.isMoving() || defPrimWeaponSkill.getRealKnowledge() > 40.0D) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3073 */                 double pdiff = Math.max(1.0D, (attCheck - defBonus + (defLeftWeapon.getWeightGrams() / 100.0F)) / 
/* 3074 */                     getWeaponParryBonus(defLeftWeapon) * getParryMod());
/* 3075 */                 pdiff *= defender.getStatus().getParryTypeModifier();
/* 3076 */                 defCheck = defPrimWeaponSkill.skillCheck(pdiff * ItemBonus.getParryBonus(defender, defParryWeapon), defLeftWeapon, 0.0D, (this.creature
/* 3077 */                     .isNoSkillFor(defender) || defParryWeapon.isWeaponBow()), 1.0F, defender, this.creature);
/* 3078 */                 defender.lastParry = WurmCalendar.currentTime;
/* 3079 */                 defender.getStatus().modifyStamina(-300.0F);
/*      */               } 
/*      */             } 
/*      */           }
/* 3083 */           if (defCheck > 0.0D)
/*      */           {
/* 3085 */             setParryEffects(defender, attWeapon, defCheck);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 3090 */     return (float)defCheck;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setParryEffects(Creature defender, Item attWeapon, double parryEff) {
/* 3095 */     defender.lastParry = WurmCalendar.currentTime;
/*      */     
/* 3097 */     if (aiming || this.creature.spamMode()) {
/*      */       
/* 3099 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 3100 */       segments.add(new CreatureLineSegment(defender));
/* 3101 */       segments.add(new MulticolorLineSegment(" " + CombatEngine.getParryString(parryEff) + " parries with " + defParryWeapon
/* 3102 */             .getNameWithGenus() + ".", (byte)0));
/*      */       
/* 3104 */       this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3110 */     if (defender.spamMode())
/*      */     {
/* 3112 */       defender.getCommunicator().sendCombatNormalMessage("You " + 
/* 3113 */           CombatEngine.getParryString(parryEff) + " parry with your " + defParryWeapon.getName() + ".");
/*      */     }
/* 3115 */     if (!defParryWeapon.isBodyPart() || defParryWeapon.getAuxData() == 100) {
/*      */       
/* 3117 */       float vulnerabilityModifier = 1.0F;
/* 3118 */       if (defender.isPlayer()) {
/*      */         
/* 3120 */         if (attWeapon.isMetal() && Weapon.isWeaponDamByMetal(defParryWeapon))
/* 3121 */           vulnerabilityModifier = 4.0F; 
/* 3122 */         if (defParryWeapon.isWeaponSword()) {
/* 3123 */           defParryWeapon.setDamage(defParryWeapon.getDamage() + 1.0E-7F * (float)damage * defParryWeapon
/* 3124 */               .getDamageModifier() * vulnerabilityModifier);
/*      */         } else {
/* 3126 */           defParryWeapon.setDamage(defParryWeapon.getDamage() + 2.0E-7F * (float)damage * defParryWeapon
/* 3127 */               .getDamageModifier() * vulnerabilityModifier);
/*      */         } 
/* 3129 */       }  if (this.creature.isPlayer()) {
/*      */         
/* 3131 */         vulnerabilityModifier = 1.0F;
/* 3132 */         if (defParryWeapon.isMetal() && Weapon.isWeaponDamByMetal(attWeapon))
/* 3133 */           vulnerabilityModifier = 4.0F; 
/* 3134 */         if (!(!attWeapon.isBodyPartAttached() ? 1 : 0)) {
/* 3135 */           attWeapon.setDamage(attWeapon.getDamage() + 1.0E-7F * (float)damage * attWeapon.getDamageModifier() * vulnerabilityModifier);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3140 */     this.creature.sendToLoggers(defender.getName() + " PARRY " + parryEff, (byte)2);
/* 3141 */     defender.sendToLoggers("YOU PARRY " + parryEff, (byte)2);
/* 3142 */     String lSstring = getParrySound(Server.rand);
/* 3143 */     SoundPlayer.playSound(lSstring, defender, 1.6F);
/* 3144 */     CombatEngine.checkEnchantDestruction(attWeapon, defParryWeapon, defender);
/* 3145 */     defender.playAnimation("parry.weapon", false);
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
/*      */   static String getParrySound(Random aRandom) {
/*      */     String lSstring;
/* 3160 */     int x = aRandom.nextInt(3);
/*      */     
/* 3162 */     if (x == 0) {
/* 3163 */       lSstring = "sound.combat.parry2";
/* 3164 */     } else if (x == 1) {
/* 3165 */       lSstring = "sound.combat.parry3";
/*      */     } else {
/* 3167 */       lSstring = "sound.combat.parry1";
/* 3168 */     }  return lSstring;
/*      */   }
/*      */ 
/*      */   
/*      */   public void increaseUseShieldCounter() {
/* 3173 */     this.usedShieldThisRound++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float checkShield(Creature defender, Item weapon) {
/* 3184 */     if ((defender.getCombatHandler()).usedShieldThisRound > 1) {
/* 3185 */       return 0.0F;
/*      */     }
/* 3187 */     defShield = defender.getShield();
/* 3188 */     defCheck = 0.0D;
/*      */     
/* 3190 */     float blockPercent = 0.0F;
/* 3191 */     if (defShield != null) {
/*      */ 
/*      */ 
/*      */       
/* 3195 */       Item defweapon = defender.getPrimWeapon();
/* 3196 */       if (defweapon != null && defweapon.isTwoHanded()) {
/* 3197 */         return 0.0F;
/*      */       }
/* 3199 */       Item defSecondWeapon = defender.getLefthandWeapon();
/* 3200 */       if (defSecondWeapon != null && defSecondWeapon.isTwoHanded()) {
/* 3201 */         return 0.0F;
/*      */       }
/* 3203 */       if (!defShield.isArtifact())
/* 3204 */         (defender.getCombatHandler()).usedShieldThisRound++; 
/* 3205 */       if (VirtualZone.isCreatureShieldedVersusTarget(this.creature, defender)) {
/*      */         
/* 3207 */         int skillnum = -10;
/* 3208 */         Skill defShieldSkill = null;
/*      */         
/*      */         try {
/* 3211 */           skillnum = defShield.getPrimarySkill();
/* 3212 */           defShieldSkill = defenderSkills.getSkill(skillnum);
/*      */         }
/* 3214 */         catch (NoSuchSkillException nss) {
/*      */           
/* 3216 */           if (skillnum != -10)
/* 3217 */             defShieldSkill = defenderSkills.learn(skillnum, 1.0F); 
/*      */         } 
/* 3219 */         if (defShieldSkill != null) {
/*      */           
/* 3221 */           if (pos == 9) {
/*      */             
/* 3223 */             blockPercent = 100.0F;
/* 3224 */             if (defender.spamMode() && Servers.isThisATestServer()) {
/* 3225 */               defender.getCommunicator().sendCombatNormalMessage("Blocking left underarm.");
/*      */             }
/* 3227 */           } else if ((defender.getStatus().getStamina() >= 300 || Server.rand.nextInt(10) == 0) && (
/* 3228 */             !defender.isMoving() || defShieldSkill.getRealKnowledge() > 40.0D)) {
/*      */ 
/*      */ 
/*      */             
/* 3232 */             double shieldModifier = ((defShield.getSizeY() + defShield.getSizeZ()) / 2.0F * defShield.getCurrentQualityLevel() / 100.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3237 */             double diff = Math.max(1.0D, chanceToHit - shieldModifier) - defBonus;
/*      */             
/* 3239 */             blockPercent = (float)defShieldSkill.skillCheck(diff, defShield, defShield.isArtifact() ? 50.0D : 0.0D, (this.creature
/* 3240 */                 .isNoSkillFor(defender) || (defender.getCombatHandler()).receivedShieldSkill), (float)(damage / 1000.0D), defender, this.creature);
/*      */             
/* 3242 */             (defender.getCombatHandler()).receivedShieldSkill = true;
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3247 */             if (defender.spamMode() && Servers.isThisATestServer())
/* 3248 */               defender.getCommunicator().sendCombatNormalMessage("Shield parrying difficulty=" + diff + " including defensive bonus " + defBonus + " vs " + defShieldSkill
/*      */                   
/* 3250 */                   .getKnowledge(defShield, 0.0D) + " " + defender.zoneBonus + ":" + defender
/* 3251 */                   .getMovePenalty() + " gave " + blockPercent + ">0"); 
/* 3252 */             defender.getStatus().modifyStamina((int)(-300.0F - defShield.getWeightGrams() / 20.0F));
/*      */           } 
/* 3254 */           if (blockPercent > 0.0F) {
/*      */             float damageMod;
/*      */             
/* 3257 */             if (!weapon.isBodyPart() && weapon.isWeaponCrush()) {
/* 3258 */               damageMod = 1.5E-5F;
/* 3259 */             } else if (type == 0) {
/* 3260 */               damageMod = 1.0E-6F;
/*      */             } else {
/* 3262 */               damageMod = 5.0E-6F;
/* 3263 */             }  if (defender.isPlayer())
/* 3264 */               defShield.setDamage(defShield.getDamage() + 
/* 3265 */                   Math.max(0.01F, damageMod * (float)damage * defShield.getDamageModifier())); 
/* 3266 */             sendShieldMessage(defender, weapon, blockPercent);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3272 */     return blockPercent;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendShieldMessage(Creature defender, Item weapon, float blockPercent) {
/* 3277 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 3278 */     segments.add(new CreatureLineSegment(defender));
/* 3279 */     segments.add(new MulticolorLineSegment(" raises " + defender.getHisHerItsString() + " shield and parries your " + attString + ".", (byte)0));
/*      */     
/* 3281 */     if (aiming || this.creature.spamMode())
/* 3282 */       this.creature.getCommunicator().sendColoredMessageCombat(segments); 
/* 3283 */     if (defender.spamMode()) {
/*      */       
/* 3285 */       ((MulticolorLineSegment)segments.get(1)).setText(" raise your shield and parry against ");
/* 3286 */       segments.add(new CreatureLineSegment(this.creature));
/* 3287 */       segments.add(new MulticolorLineSegment("'s " + attString + ".", (byte)0));
/*      */       
/* 3289 */       defender.getCommunicator().sendColoredMessageCombat(segments);
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
/* 3308 */     if (defShield.isWood()) {
/* 3309 */       Methods.sendSound(defender, "sound.combat.shield.wood");
/*      */     } else {
/* 3311 */       Methods.sendSound(defender, "sound.combat.shield.metal");
/* 3312 */     }  CombatEngine.checkEnchantDestruction(weapon, defShield, defender);
/* 3313 */     this.creature.sendToLoggers(defender.getName() + " SHIELD " + blockPercent, (byte)2);
/* 3314 */     defender.sendToLoggers("You SHIELD " + blockPercent, (byte)2);
/* 3315 */     defender.playAnimation("parry.shield", false);
/*      */   }
/*      */   
/*      */   private void sendDodgeMessage(Creature defender) {
/*      */     String sstring;
/* 3320 */     double power = (float)(defender.getBodyControl() / 3.0D - defCheck);
/*      */     
/* 3322 */     if (power > 20.0D) {
/* 3323 */       sstring = "sound.combat.miss.heavy";
/* 3324 */     } else if (power > 10.0D) {
/* 3325 */       sstring = "sound.combat.miss.med";
/*      */     } else {
/* 3327 */       sstring = "sound.combat.miss.light";
/* 3328 */     }  SoundPlayer.playSound(sstring, this.creature, 1.6F);
/*      */     
/* 3330 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 3331 */     segments.add(new CreatureLineSegment(defender));
/* 3332 */     segments.add(new MulticolorLineSegment(" " + CombatEngine.getParryString(power) + " evades the blow to the " + defender
/* 3333 */           .getBody().getWoundLocationString(pos) + ".", (byte)0));
/*      */     
/* 3335 */     if (aiming || this.creature.spamMode())
/*      */     {
/* 3337 */       this.creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3342 */     if (defender.spamMode()) {
/*      */       
/* 3344 */       ((MulticolorLineSegment)segments.get(1)).setText(" " + CombatEngine.getParryString(power) + " evade the blow to the " + defender
/* 3345 */           .getBody().getWoundLocationString(pos) + ".");
/* 3346 */       defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3351 */     this.creature.sendToLoggers(defender.getName() + " EVADE", (byte)2);
/* 3352 */     defender.sendToLoggers("You EVADE", (byte)2);
/* 3353 */     defender.playAnimation("dodge", false);
/*      */   }
/*      */ 
/*      */   
/*      */   private final double getDamage(Creature _creature, AttackAction attk, Creature opponent) {
/* 3358 */     Skill attStrengthSkill = null;
/*      */     
/*      */     try {
/* 3361 */       attStrengthSkill = _creature.getSkills().getSkill(102);
/*      */     }
/* 3363 */     catch (NoSuchSkillException nss) {
/*      */       
/* 3365 */       attStrengthSkill = _creature.getSkills().learn(102, 1.0F);
/* 3366 */       logger.log(Level.WARNING, _creature.getName() + " had no strength. Weird.");
/*      */     } 
/* 3368 */     Item weapon = _creature.getPrimWeapon(!attk.isUsingWeapon());
/* 3369 */     if (!attk.isUsingWeapon()) {
/*      */       
/* 3371 */       damage = (attk.getAttackValues().getBaseDamage() * 1000.0F * _creature.getStatus().getDamageTypeModifier());
/* 3372 */       if (_creature.isPlayer()) {
/*      */         
/* 3374 */         Skill weaponLess = _creature.getWeaponLessFightingSkill();
/*      */         
/* 3376 */         double modifier = 1.0D + 2.0D * weaponLess.getKnowledge() / 100.0D;
/* 3377 */         damage *= modifier;
/*      */       } 
/* 3379 */       if (damage < 10000.0D && _creature.getBonusForSpellEffect((byte)24) > 0.0F)
/*      */       {
/* 3381 */         if (_creature.isPlayer()) {
/* 3382 */           damage += Server.getBuffedQualityEffect((_creature.getBonusForSpellEffect((byte)24) / 100.0F)) * 15000.0D;
/*      */         } else {
/* 3384 */           damage += Server.getBuffedQualityEffect((_creature.getBonusForSpellEffect((byte)24) / 100.0F)) * 5000.0D;
/*      */         }  } 
/* 3386 */       float randomizer = (50.0F + Server.rand.nextFloat() * 50.0F) / 100.0F;
/* 3387 */       damage *= randomizer;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3392 */       damage = Weapon.getModifiedDamageForWeapon(weapon, attStrengthSkill, (opponent.getTemplate().getTemplateId() == 116)) * 1000.0D;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3397 */       damage += Server.getBuffedQualityEffect((weapon.getCurrentQualityLevel() / 100.0F)) * 
/* 3398 */         Weapon.getBaseDamageForWeapon(weapon) * 2400.0D;
/*      */       
/* 3400 */       damage *= Weapon.getMaterialDamageBonus(weapon.getMaterial());
/* 3401 */       if (!opponent.isPlayer() && opponent.isHunter())
/* 3402 */         damage *= Weapon.getMaterialHunterDamageBonus(weapon.getMaterial()); 
/* 3403 */       damage *= ItemBonus.getWeaponDamageIncreaseBonus(_creature, weapon);
/*      */ 
/*      */       
/* 3406 */       damage *= (1.0F + weapon.getCurrentQualityLevel() / 100.0F * weapon.getSpellExtraDamageBonus() / 30000.0F);
/*      */     } 
/* 3408 */     if (_creature.getEnemyPresense() > 1200 && opponent.isPlayer() && !weapon.isArtifact()) {
/* 3409 */       damage *= 1.149999976158142D;
/*      */     }
/* 3411 */     if (!weapon.isArtifact() && this.hasRodEffect && opponent.isPlayer())
/* 3412 */       damage *= 1.2000000476837158D; 
/* 3413 */     Vehicle vehicle = Vehicles.getVehicleForId(opponent.getVehicle());
/* 3414 */     boolean mildStack = false;
/*      */     
/* 3416 */     if (weapon.isWeaponPolearm() && ((vehicle != null && vehicle
/* 3417 */       .isCreature()) || (opponent.isRidden() && weapon.isWeaponPierce()))) {
/*      */       
/* 3419 */       damage *= 1.7000000476837158D;
/*      */     }
/* 3421 */     else if (weapon.isArtifact()) {
/* 3422 */       mildStack = true;
/* 3423 */     } else if (_creature.getCultist() != null && _creature.getCultist().doubleWarDamage()) {
/*      */       
/* 3425 */       damage *= 1.5D;
/* 3426 */       mildStack = true;
/*      */     }
/* 3428 */     else if (_creature.getDeity() != null && _creature.getDeity().isWarrior()) {
/*      */       
/* 3430 */       if (_creature.getFaith() >= 40.0F && _creature.getFavor() >= 20.0F) {
/*      */         
/* 3432 */         damage *= 1.149999976158142D;
/* 3433 */         mildStack = true;
/*      */       } 
/*      */     } 
/*      */     
/* 3437 */     if (_creature.isPlayer()) {
/*      */       
/* 3439 */       if (_creature.getFightStyle() != 2 || attStrengthSkill.getRealKnowledge() < 20.0D)
/*      */       {
/*      */ 
/*      */         
/* 3443 */         if (attStrengthSkill.getRealKnowledge() != 20.0D) {
/* 3444 */           damage *= 1.0D + (attStrengthSkill.getRealKnowledge() - 20.0D) / 200.0D;
/*      */         }
/*      */       }
/* 3447 */       if (this.currentStrength == 0) {
/*      */         
/* 3449 */         Skill fstyle = null;
/*      */         
/*      */         try {
/* 3452 */           fstyle = _creature.getSkills().getSkill(10054);
/*      */         }
/* 3454 */         catch (NoSuchSkillException nss) {
/*      */           
/* 3456 */           fstyle = _creature.getSkills().learn(10054, 1.0F);
/*      */         } 
/* 3458 */         if (fstyle.skillCheck((opponent.getBaseCombatRating() * 3.0F), 0.0D, (this.receivedFStyleSkill || opponent
/* 3459 */             .isNoSkillFor(_creature)), 10.0F, _creature, opponent) > 0.0D) {
/*      */           
/* 3461 */           this.receivedFStyleSkill = true;
/* 3462 */           damage *= 0.800000011920929D;
/*      */         }
/*      */         else {
/*      */           
/* 3466 */           damage *= 0.5D;
/*      */         } 
/*      */       } 
/*      */       
/* 3470 */       if (_creature.getStatus().getStamina() > 2000)
/*      */       {
/* 3472 */         if (this.currentStrength >= 1 && !this.receivedFStyleSkill) {
/*      */           
/* 3474 */           int num = 10053;
/* 3475 */           if (this.currentStrength == 1) {
/* 3476 */             num = 10055;
/*      */           }
/* 3478 */           Skill fstyle = null;
/*      */           
/*      */           try {
/* 3481 */             fstyle = _creature.getSkills().getSkill(num);
/*      */           }
/* 3483 */           catch (NoSuchSkillException nss) {
/*      */             
/* 3485 */             fstyle = _creature.getSkills().learn(num, 1.0F);
/*      */           } 
/* 3487 */           if (fstyle.skillCheck((opponent.getBaseCombatRating() * 3.0F), 0.0D, (this.receivedFStyleSkill || opponent
/* 3488 */               .isNoSkillFor(_creature)), 10.0F, _creature, opponent) > 0.0D) {
/*      */             
/* 3490 */             this.receivedFStyleSkill = true;
/*      */             
/* 3492 */             if (this.currentStrength > 1)
/* 3493 */               damage *= 1.0D + Server.getModifiedFloatEffect(fstyle.getRealKnowledge() / 100.0D) / (mildStack ? 8.0F : 4.0F); 
/*      */           } 
/*      */         } 
/*      */       }
/* 3497 */       float knowl = 1.0F;
/*      */       
/*      */       try {
/* 3500 */         Skill wSkill = _creature.getSkills().getSkill(weapon.getPrimarySkill());
/* 3501 */         knowl = (float)wSkill.getRealKnowledge();
/*      */       }
/* 3503 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */       
/* 3507 */       if (knowl < 50.0F) {
/* 3508 */         damage = 0.800000011920929D * damage + 0.2D * (knowl / 50.0F) * damage;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 3513 */       damage *= (0.85F + this.currentStrength * 0.15F);
/*      */     } 
/* 3515 */     if (_creature.isStealth())
/*      */     {
/* 3517 */       if (_creature.opponent != null)
/*      */       {
/* 3519 */         if (!_creature.isVisibleTo(opponent)) {
/*      */           
/* 3521 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 3522 */           segments.add(new CreatureLineSegment(_creature));
/* 3523 */           segments.add(new MulticolorLineSegment(" backstab ", (byte)0));
/* 3524 */           segments.add(new CreatureLineSegment(opponent));
/*      */           
/* 3526 */           _creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */           
/* 3529 */           damage = Math.min(50000.0D, damage * 4.0D);
/*      */         } 
/*      */       }
/*      */     }
/* 3533 */     if (_creature.getCitizenVillage() != null)
/*      */     {
/* 3535 */       if (_creature.getCitizenVillage().getFaithWarBonus() > 0.0F)
/* 3536 */         damage *= (1.0F + _creature.getCitizenVillage().getFaithWarBonus() / 100.0F); 
/*      */     }
/* 3538 */     if (_creature.fightlevel >= 4) {
/* 3539 */       damage *= 1.100000023841858D;
/*      */     }
/* 3541 */     return damage;
/*      */   }
/*      */ 
/*      */   
/*      */   private final double getDamage(Creature _creature, Item weapon, Creature opponent) {
/* 3546 */     Skill attStrengthSkill = null;
/*      */     
/*      */     try {
/* 3549 */       attStrengthSkill = _creature.getSkills().getSkill(102);
/*      */     }
/* 3551 */     catch (NoSuchSkillException nss) {
/*      */       
/* 3553 */       attStrengthSkill = _creature.getSkills().learn(102, 1.0F);
/* 3554 */       logger.log(Level.WARNING, _creature.getName() + " had no strength. Weird.");
/*      */     } 
/* 3556 */     if (weapon.isBodyPartAttached()) {
/*      */       
/* 3558 */       damage = (_creature.getCombatDamage(weapon) * 1000.0F * _creature.getStatus().getDamageTypeModifier());
/* 3559 */       if (_creature.isPlayer()) {
/*      */         
/* 3561 */         Skill weaponLess = _creature.getWeaponLessFightingSkill();
/*      */         
/* 3563 */         double modifier = 1.0D + 2.0D * weaponLess.getKnowledge() / 100.0D;
/* 3564 */         damage *= modifier;
/*      */       } 
/* 3566 */       if (damage < 10000.0D && _creature.getBonusForSpellEffect((byte)24) > 0.0F)
/*      */       {
/* 3568 */         if (_creature.isPlayer()) {
/* 3569 */           damage += Server.getBuffedQualityEffect((_creature.getBonusForSpellEffect((byte)24) / 100.0F)) * 15000.0D;
/*      */         } else {
/* 3571 */           damage += Server.getBuffedQualityEffect((_creature.getBonusForSpellEffect((byte)24) / 100.0F)) * 5000.0D;
/*      */         }  } 
/* 3573 */       float randomizer = (50.0F + Server.rand.nextFloat() * 50.0F) / 100.0F;
/* 3574 */       damage *= randomizer;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3579 */       damage = Weapon.getModifiedDamageForWeapon(weapon, attStrengthSkill, (opponent.getTemplate().getTemplateId() == 116)) * 1000.0D;
/*      */       
/* 3581 */       if (!Servers.isThisAnEpicOrChallengeServer()) {
/* 3582 */         damage += (weapon.getCurrentQualityLevel() / 100.0F * weapon.getSpellExtraDamageBonus());
/*      */       }
/* 3584 */       damage += Server.getBuffedQualityEffect((weapon.getCurrentQualityLevel() / 100.0F)) * 
/* 3585 */         Weapon.getBaseDamageForWeapon(weapon) * 2400.0D;
/*      */       
/* 3587 */       damage *= Weapon.getMaterialDamageBonus(weapon.getMaterial());
/* 3588 */       if (!opponent.isPlayer() && opponent.isHunter())
/* 3589 */         damage *= Weapon.getMaterialHunterDamageBonus(weapon.getMaterial()); 
/* 3590 */       damage *= ItemBonus.getWeaponDamageIncreaseBonus(_creature, weapon);
/* 3591 */       if (Servers.isThisAnEpicOrChallengeServer())
/* 3592 */         damage *= (1.0F + weapon.getCurrentQualityLevel() / 100.0F * weapon.getSpellExtraDamageBonus() / 30000.0F); 
/*      */     } 
/* 3594 */     if (_creature.getEnemyPresense() > 1200 && opponent.isPlayer() && !weapon.isArtifact())
/* 3595 */       damage *= 1.149999976158142D; 
/* 3596 */     if (!weapon.isArtifact() && this.hasRodEffect && opponent.isPlayer())
/* 3597 */       damage *= 1.2000000476837158D; 
/* 3598 */     Vehicle vehicle = Vehicles.getVehicleForId(opponent.getVehicle());
/* 3599 */     boolean mildStack = false;
/*      */     
/* 3601 */     if (weapon.isWeaponPolearm() && ((vehicle != null && vehicle
/* 3602 */       .isCreature()) || (opponent.isRidden() && weapon.isWeaponPierce()))) {
/*      */       
/* 3604 */       damage *= 1.7000000476837158D;
/*      */     }
/* 3606 */     else if (weapon.isArtifact()) {
/* 3607 */       mildStack = true;
/* 3608 */     } else if (_creature.getCultist() != null && _creature.getCultist().doubleWarDamage()) {
/*      */       
/* 3610 */       damage *= 1.5D;
/* 3611 */       mildStack = true;
/*      */     }
/* 3613 */     else if (_creature.getDeity() != null && _creature.getDeity().isWarrior()) {
/*      */       
/* 3615 */       if (_creature.getFaith() >= 40.0F && _creature.getFavor() >= 20.0F) {
/*      */         
/* 3617 */         damage *= 1.149999976158142D;
/* 3618 */         mildStack = true;
/*      */       } 
/*      */     } 
/*      */     
/* 3622 */     if (_creature.isPlayer()) {
/*      */       
/* 3624 */       if (_creature.getFightStyle() != 2 || attStrengthSkill.getRealKnowledge() < 20.0D)
/*      */       {
/*      */ 
/*      */         
/* 3628 */         if (attStrengthSkill.getRealKnowledge() != 20.0D) {
/* 3629 */           damage *= 1.0D + (attStrengthSkill.getRealKnowledge() - 20.0D) / 200.0D;
/*      */         }
/*      */       }
/* 3632 */       if (this.currentStrength == 0) {
/*      */         
/* 3634 */         Skill fstyle = null;
/*      */         
/*      */         try {
/* 3637 */           fstyle = _creature.getSkills().getSkill(10054);
/*      */         }
/* 3639 */         catch (NoSuchSkillException nss) {
/*      */           
/* 3641 */           fstyle = _creature.getSkills().learn(10054, 1.0F);
/*      */         } 
/* 3643 */         if (fstyle.skillCheck((opponent.getBaseCombatRating() * 3.0F), 0.0D, (this.receivedFStyleSkill || opponent
/* 3644 */             .isNoSkillFor(_creature)), 10.0F, _creature, opponent) > 0.0D) {
/*      */           
/* 3646 */           this.receivedFStyleSkill = true;
/* 3647 */           damage *= 0.800000011920929D;
/*      */         }
/*      */         else {
/*      */           
/* 3651 */           damage *= 0.5D;
/*      */         } 
/*      */       } 
/*      */       
/* 3655 */       if (_creature.getStatus().getStamina() > 2000)
/*      */       {
/* 3657 */         if (this.currentStrength >= 1 && !this.receivedFStyleSkill) {
/*      */           
/* 3659 */           int num = 10053;
/* 3660 */           if (this.currentStrength == 1) {
/* 3661 */             num = 10055;
/*      */           }
/* 3663 */           Skill fstyle = null;
/*      */           
/*      */           try {
/* 3666 */             fstyle = _creature.getSkills().getSkill(num);
/*      */           }
/* 3668 */           catch (NoSuchSkillException nss) {
/*      */             
/* 3670 */             fstyle = _creature.getSkills().learn(num, 1.0F);
/*      */           } 
/* 3672 */           if (fstyle.skillCheck((opponent.getBaseCombatRating() * 3.0F), 0.0D, (this.receivedFStyleSkill || opponent
/* 3673 */               .isNoSkillFor(_creature)), 10.0F, _creature, opponent) > 0.0D) {
/*      */             
/* 3675 */             this.receivedFStyleSkill = true;
/*      */             
/* 3677 */             if (this.currentStrength > 1)
/* 3678 */               damage *= 1.0D + Server.getModifiedFloatEffect(fstyle.getRealKnowledge() / 100.0D) / (mildStack ? 8.0F : 4.0F); 
/*      */           } 
/*      */         } 
/*      */       }
/* 3682 */       float knowl = 1.0F;
/*      */       
/*      */       try {
/* 3685 */         Skill wSkill = _creature.getSkills().getSkill(weapon.getPrimarySkill());
/* 3686 */         knowl = (float)wSkill.getRealKnowledge();
/*      */       }
/* 3688 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */       
/* 3692 */       if (knowl < 50.0F) {
/* 3693 */         damage = 0.800000011920929D * damage + 0.2D * (knowl / 50.0F) * damage;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 3698 */       damage *= (0.85F + this.currentStrength * 0.15F);
/*      */     } 
/* 3700 */     if (_creature.isStealth())
/*      */     {
/* 3702 */       if (_creature.opponent != null)
/*      */       {
/* 3704 */         if (!_creature.isVisibleTo(opponent)) {
/*      */           
/* 3706 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 3707 */           segments.add(new CreatureLineSegment(_creature));
/* 3708 */           segments.add(new MulticolorLineSegment(" backstab ", (byte)0));
/* 3709 */           segments.add(new CreatureLineSegment(opponent));
/*      */           
/* 3711 */           _creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */           
/* 3714 */           damage = Math.min(50000.0D, damage * 4.0D);
/*      */         } 
/*      */       }
/*      */     }
/* 3718 */     if (_creature.getCitizenVillage() != null)
/*      */     {
/* 3720 */       if (_creature.getCitizenVillage().getFaithWarBonus() > 0.0F)
/* 3721 */         damage *= (1.0F + _creature.getCitizenVillage().getFaithWarBonus() / 100.0F); 
/*      */     }
/* 3723 */     if (_creature.fightlevel >= 4) {
/* 3724 */       damage *= 1.100000023841858D;
/*      */     }
/* 3726 */     return damage;
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
/*      */   public byte getType(Item weapon, boolean rawType) {
/* 3738 */     byte woundType = this.creature.getCombatDamageType();
/* 3739 */     if (weapon.isWeaponSword() || weapon.getTemplateId() == 706) {
/*      */       
/* 3741 */       if (rawType || Server.rand.nextInt(2) == 0) {
/* 3742 */         woundType = 1;
/*      */       } else {
/* 3744 */         woundType = 2;
/*      */       } 
/* 3746 */     } else if (weapon.getTemplateId() == 1115) {
/*      */       
/* 3748 */       if (rawType || Server.rand.nextInt(3) == 0) {
/* 3749 */         woundType = 2;
/*      */       } else {
/* 3751 */         woundType = 0;
/*      */       } 
/* 3753 */     } else if (weapon.isWeaponSlash()) {
/* 3754 */       woundType = 1;
/* 3755 */     } else if (weapon.isWeaponPierce()) {
/* 3756 */       woundType = 2;
/* 3757 */     } else if (weapon.isWeaponCrush()) {
/* 3758 */       woundType = 0;
/* 3759 */     } else if (weapon.isBodyPart()) {
/*      */       
/* 3761 */       if (weapon.getTemplateId() == 17) {
/* 3762 */         woundType = 3;
/* 3763 */       } else if (weapon.getTemplateId() == 12) {
/* 3764 */         woundType = 0;
/*      */       } 
/*      */     } 
/* 3767 */     type = woundType;
/* 3768 */     return woundType;
/*      */   }
/*      */ 
/*      */   
/*      */   private float getWeaponSpeed(Item _weapon) {
/* 3773 */     float flspeed = 20.0F;
/* 3774 */     float knowl = 0.0F;
/* 3775 */     int spskillnum = 10052;
/* 3776 */     if (_weapon.isBodyPartAttached()) {
/*      */       
/* 3778 */       flspeed = this.creature.getBodyWeaponSpeed(_weapon);
/*      */     }
/*      */     else {
/*      */       
/* 3782 */       flspeed = Weapon.getBaseSpeedForWeapon(_weapon);
/*      */       
/*      */       try {
/* 3785 */         spskillnum = _weapon.getPrimarySkill();
/*      */       }
/* 3787 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3793 */       Skill wSkill = this.creature.getSkills().getSkill(spskillnum);
/* 3794 */       knowl = (float)wSkill.getRealKnowledge();
/*      */     }
/* 3796 */     catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3801 */     if (!this.creature.isGhost())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3809 */       flspeed -= flspeed * 0.1F * knowl / 100.0F;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3817 */     return flspeed;
/*      */   }
/*      */ 
/*      */   
/*      */   private float getWeaponSpeed(AttackAction act, Item _weapon) {
/* 3822 */     float flspeed = 20.0F;
/* 3823 */     float knowl = 0.0F;
/* 3824 */     int spskillnum = 10052;
/* 3825 */     if (!act.isUsingWeapon()) {
/*      */       
/* 3827 */       flspeed = act.getAttackValues().getBaseSpeed();
/*      */     }
/*      */     else {
/*      */       
/* 3831 */       flspeed = act.getAttackValues().getBaseSpeed();
/*      */       
/*      */       try {
/* 3834 */         spskillnum = _weapon.getPrimarySkill();
/*      */       }
/* 3836 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3842 */       Skill wSkill = this.creature.getSkills().getSkill(spskillnum);
/* 3843 */       knowl = (float)wSkill.getRealKnowledge();
/*      */     }
/* 3845 */     catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3850 */     if (!this.creature.isGhost())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3858 */       flspeed -= flspeed * 0.1F * knowl / 100.0F;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3866 */     return flspeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setHasSpiritFervor(boolean hasFervor) {
/* 3871 */     this.hasSpiritFervor = hasFervor;
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
/*      */   public float getCombatRating(Creature opponent, Item weapon, boolean attacking) {
/* 3888 */     float combatRating = this.creature.getBaseCombatRating();
/* 3889 */     if (this.hasSpiritFervor)
/* 3890 */       combatRating++; 
/* 3891 */     if (this.creature.isKing() && this.creature.isEligibleForKingdomBonus()) {
/* 3892 */       combatRating += 3.0F;
/*      */     }
/*      */     
/* 3895 */     if (this.creature.hasTrait(0)) {
/* 3896 */       combatRating++;
/*      */     }
/*      */     
/* 3899 */     if (attacking) {
/*      */       
/* 3901 */       combatRating += 1.0F + this.creature.getBonusForSpellEffect((byte)30) / 30.0F;
/*      */     }
/*      */     else {
/*      */       
/* 3905 */       combatRating += 1.0F + this.creature.getBonusForSpellEffect((byte)28) / 30.0F;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3911 */     if (this.creature.getDeity() != null && this.creature.getFaith() > 70.0F) {
/*      */       
/* 3913 */       MeshIO mesh = Server.surfaceMesh;
/* 3914 */       if (this.creature.getLayer() < 0)
/* 3915 */         mesh = Server.caveMesh; 
/* 3916 */       int tile = mesh.getTile(this.creature.getCurrentTile().getTileX(), this.creature.getCurrentTile().getTileY());
/* 3917 */       byte type = Tiles.decodeType(tile);
/*      */ 
/*      */       
/* 3920 */       if (this.creature.getDeity().isFo()) {
/*      */ 
/*      */         
/* 3923 */         Tiles.Tile theTile = Tiles.getTile(type);
/* 3924 */         if (theTile.isNormalTree() || theTile.isMyceliumTree() || type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_TUNDRA.id)
/*      */         {
/*      */ 
/*      */           
/* 3928 */           combatRating++;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 3933 */       if (this.creature.getDeity().isMagranon() || this.creature.getDeity().isLibila())
/*      */       {
/*      */         
/* 3936 */         if (attacking)
/*      */         {
/* 3938 */           combatRating += 2.0F;
/*      */         }
/*      */       }
/*      */ 
/*      */       
/* 3943 */       if (this.creature.getDeity().isVynora())
/*      */       {
/*      */         
/* 3946 */         if (!attacking) {
/*      */           
/* 3948 */           short height = Tiles.decodeHeight(tile);
/* 3949 */           if (height < 0) {
/*      */             
/* 3951 */             combatRating += 2.0F;
/*      */           }
/* 3953 */           else if (Terraforming.isRoad(type)) {
/*      */             
/* 3955 */             combatRating += 2.0F;
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3962 */     if (this.creature.getCultist() != null && this.creature.getCultist().hasFearEffect())
/*      */     {
/* 3964 */       combatRating += 2.0F;
/*      */     }
/* 3966 */     if (this.creature.isPlayer()) {
/*      */ 
/*      */ 
/*      */       
/* 3970 */       int antiGankBonus = Math.max(0, this.creature.getLastAttackers() - 1);
/* 3971 */       combatRating += antiGankBonus;
/* 3972 */       this.creature.sendToLoggers("Adding " + antiGankBonus + " to combat rating due to attackers.");
/*      */     } 
/* 3974 */     if (this.creature.isHorse() && this.creature.getLeader() != null && this.creature.getLeader().isPlayer())
/* 3975 */       combatRating -= 5.0F; 
/* 3976 */     if (this.creature.hasSpellEffect((byte)97))
/* 3977 */       combatRating -= 4.0F; 
/* 3978 */     if (this.creature.isSpiritGuard())
/*      */     {
/* 3980 */       if (Servers.localServer.isChallengeServer()) {
/*      */ 
/*      */         
/* 3983 */         if (opponent.isPlayer() && opponent.getKingdomId() != this.creature.getKingdomId())
/*      */         {
/* 3985 */           combatRating = 10.0F;
/*      */         }
/*      */       }
/* 3988 */       else if (this.creature.getCitizenVillage() != null && 
/* 3989 */         (this.creature.getCitizenVillage()).plan.isUnderSiege()) {
/* 3990 */         combatRating += ((this.creature.getCitizenVillage()).plan.getSiegeCount() / 3);
/*      */       }  } 
/* 3992 */     float bon = weapon.getSpellNimbleness();
/* 3993 */     if (bon > 0.0F)
/* 3994 */       combatRating += bon / 30.0F; 
/* 3995 */     if (this.creature.isPlayer() && opponent.isPlayer()) {
/*      */       
/* 3997 */       if (this.creature.isRoyalExecutioner() && this.creature.isEligibleForKingdomBonus()) {
/* 3998 */         combatRating += 2.0F;
/* 3999 */       } else if (this.creature.hasCrownInfluence()) {
/* 4000 */         combatRating++;
/* 4001 */       }  combatRating += Players.getInstance().getCRBonus(this.creature.getKingdomId());
/* 4002 */       if (this.creature.isInOwnDuelRing()) {
/*      */         
/* 4004 */         if (opponent.getKingdomId() != this.creature.getKingdomId())
/*      */         {
/* 4006 */           combatRating += 4.0F;
/*      */         }
/*      */       }
/* 4009 */       else if (opponent.isInOwnDuelRing()) {
/*      */         
/* 4011 */         if (opponent.getKingdomId() != this.creature.getKingdomId())
/*      */         {
/* 4013 */           combatRating -= 4.0F;
/*      */         }
/*      */       } 
/* 4016 */       if (Servers.localServer.PVPSERVER && this.creature.getNumberOfFollowers() > 1) {
/* 4017 */         combatRating -= 10.0F;
/*      */       }
/*      */     } 
/* 4020 */     if (this.creature.isPlayer() && this.creature.hasBattleCampBonus())
/* 4021 */       combatRating += 3.0F; 
/* 4022 */     combatRating += ItemBonus.getCRBonus(this.creature);
/*      */     
/* 4024 */     float crmod = 1.0F;
/* 4025 */     if (attacking) {
/*      */       
/* 4027 */       if (this.creature.isPlayer() && this.currentStrength >= 1)
/*      */       {
/* 4029 */         if (this.creature.getStatus().getStamina() > 2000)
/*      */         {
/* 4031 */           int num = 10053;
/* 4032 */           if (this.currentStrength == 1) {
/* 4033 */             num = 10055;
/*      */           }
/* 4035 */           Skill def = null;
/*      */           
/*      */           try {
/* 4038 */             def = this.creature.getSkills().getSkill(num);
/*      */           }
/* 4040 */           catch (NoSuchSkillException nss) {
/*      */             
/* 4042 */             def = this.creature.getSkills().learn(num, 1.0F);
/*      */           } 
/* 4044 */           if (def.skillCheck((this.creature.getBaseCombatRating() * 2.0F), 0.0D, true, 10.0F, this.creature, opponent) > 0.0D)
/*      */           {
/*      */             
/* 4047 */             combatRating = (float)(combatRating + (this.currentStrength / 2.0F) * Server.getModifiedFloatEffect(def.getRealKnowledge() / 100.0D));
/*      */           
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     }
/* 4054 */     else if (this.creature.isPlayer() && this.currentStrength > 1) {
/*      */       
/* 4056 */       Skill def = null;
/*      */       
/*      */       try {
/* 4059 */         def = this.creature.getSkills().getSkill(10053);
/*      */       }
/* 4061 */       catch (NoSuchSkillException nss) {
/*      */         
/* 4063 */         def = this.creature.getSkills().learn(10053, 1.0F);
/*      */       } 
/* 4065 */       if (def.skillCheck(Server.getModifiedFloatEffect(70.0D), 0.0D, true, 10.0F, this.creature, opponent) < 0.0D)
/*      */       {
/*      */         
/* 4068 */         combatRating = (float)(combatRating - this.currentStrength * Server.getModifiedFloatEffect((100.0D - def.getRealKnowledge()) / 100.0D));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 4073 */     if (this.creature.isPlayer()) {
/*      */       
/* 4075 */       combatRating = (float)(combatRating - Weapon.getSkillPenaltyForWeapon(weapon));
/* 4076 */       combatRating += this.creature.getCRCounterBonus();
/*      */     } 
/*      */     
/* 4079 */     if (this.creature.isPlayer())
/*      */     {
/* 4081 */       if (opponent.isPlayer()) {
/*      */ 
/*      */ 
/*      */         
/* 4085 */         combatRating = (float)(combatRating + this.creature.getFightingSkill().getKnowledge(0.0D) / 5.0D);
/*      */       } else {
/*      */         
/* 4088 */         combatRating = (float)(combatRating + this.creature.getFightingSkill().getRealKnowledge() / 10.0D);
/*      */       } 
/*      */     }
/* 4091 */     if (this.battleratingPenalty > 0) {
/* 4092 */       combatRating -= this.battleratingPenalty;
/*      */     }
/*      */ 
/*      */     
/* 4096 */     crmod *= getFlankingModifier(opponent);
/* 4097 */     crmod *= getHeightModifier(opponent);
/* 4098 */     crmod *= getAlcMod();
/*      */ 
/*      */     
/* 4101 */     if (this.creature.getCitizenVillage() != null)
/*      */     {
/* 4103 */       crmod *= 1.0F + this.creature.getCitizenVillage().getFaithWarBonus() / 100.0F;
/*      */     }
/* 4105 */     combatRating *= crmod;
/* 4106 */     if (this.creature.fightlevel >= 3) {
/* 4107 */       combatRating += (this.creature.fightlevel * 2);
/*      */     }
/* 4109 */     if (this.creature.isPlayer())
/* 4110 */       combatRating *= Servers.localServer.getCombatRatingModifier(); 
/* 4111 */     combatRating *= getFootingModifier(weapon, opponent);
/*      */     
/* 4113 */     if (this.creature.isOnHostileHomeServer()) {
/* 4114 */       combatRating *= 0.7F;
/*      */     }
/*      */     
/* 4117 */     if (isOpen()) {
/* 4118 */       combatRating *= 0.7F;
/* 4119 */     } else if (isProne()) {
/* 4120 */       combatRating *= 0.5F;
/*      */     } else {
/*      */       
/*      */       try {
/* 4124 */         Action act = this.creature.getCurrentAction();
/* 4125 */         if (act.isVulnerable()) {
/* 4126 */           combatRating *= 0.5F;
/* 4127 */         } else if (this.creature.isLinked()) {
/*      */           
/* 4129 */           Creature linkedTo = this.creature.getCreatureLinkedTo();
/* 4130 */           if (linkedTo != null) {
/*      */             
/*      */             try {
/*      */               
/* 4134 */               linkedTo.getCurrentAction().isSpell();
/* 4135 */               combatRating *= 0.7F;
/*      */             }
/* 4137 */             catch (NoSuchActionException noSuchActionException) {}
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 4144 */       catch (NoSuchActionException noSuchActionException) {}
/*      */     } 
/*      */ 
/*      */     
/* 4148 */     if (this.creature.hasAttackedUnmotivated())
/* 4149 */       combatRating = Math.min(4.0F, combatRating); 
/* 4150 */     return normcr(combatRating);
/*      */   }
/*      */ 
/*      */   
/*      */   private float getAlcMod() {
/* 4155 */     if (this.creature.isPlayer()) {
/*      */       
/* 4157 */       float alc = 0.0F;
/* 4158 */       alc = ((Player)this.creature).getAlcohol();
/* 4159 */       if (alc < 20.0F) {
/* 4160 */         return (100.0F + alc) / 100.0F;
/*      */       }
/* 4162 */       return Math.max(40.0F, 100.0F - alc) / 80.0F;
/*      */     } 
/* 4164 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   private float normcr(float combatRating) {
/* 4169 */     return Math.min(100.0F, Math.max(1.0F, combatRating));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static float getParryBonus(byte defenderStance, byte attackerStance) {
/* 4180 */     if (isStanceParrying(defenderStance, attackerStance))
/*      */     {
/* 4182 */       return 0.8F;
/*      */     }
/* 4184 */     if (isStanceOpposing(defenderStance, attackerStance))
/*      */     {
/* 4186 */       return 0.9F;
/*      */     }
/* 4188 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getChanceToHit(Creature opponent, Item weapon) {
/* 4193 */     setBonuses(weapon, opponent);
/* 4194 */     float myCR = getCombatRating(opponent, weapon, true);
/* 4195 */     float oppCR = opponent.getCombatHandler().getCombatRating(this.creature, opponent.getPrimWeapon(), false);
/*      */     
/* 4197 */     if (this.creature.isPlayer()) {
/*      */       
/* 4199 */       float distdiff = Math.abs(getDistdiff(weapon, this.creature, opponent));
/* 4200 */       if (distdiff > 10.0F)
/* 4201 */         myCR--; 
/* 4202 */       if (distdiff > 20.0F)
/* 4203 */         myCR--; 
/*      */     } 
/* 4205 */     parryBonus = getParryBonus((opponent.getCombatHandler()).currentStance, this.currentStance);
/*      */     
/* 4207 */     if (opponent.fightlevel > 0) {
/* 4208 */       parryBonus -= (opponent.fightlevel * 1) / 100.0F;
/*      */     }
/* 4210 */     double m = 1.0D;
/* 4211 */     if (attBonus != 0.0D)
/* 4212 */       m = 1.0D + attBonus / 100.0D; 
/* 4213 */     Seat s = opponent.getSeat();
/* 4214 */     if (s != null) {
/* 4215 */       m *= s.cover;
/*      */     }
/* 4217 */     float chance = (float)((normcr(myCR) / (normcr(oppCR) + normcr(myCR))) * m * parryBonus);
/* 4218 */     float rest = Math.max(0.01F, 1.0F - chance);
/* 4219 */     return 100.0F * Math.max(0.01F, (float)Server.getBuffedQualityEffect((1.0F - rest)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBonuses(Item weapon, Creature defender) {
/* 4230 */     attBonus = this.creature.zoneBonus - this.creature.getMovePenalty() * 0.5D;
/* 4231 */     if (this.currentStrength == 0)
/* 4232 */       attBonus -= 20.0D; 
/* 4233 */     defBonus = (defender.zoneBonus - defender.getMovePenalty());
/*      */ 
/*      */     
/* 4236 */     if (this.addToSkills)
/*      */     {
/* 4238 */       if (defender.isPlayer() && (defender.getCombatHandler()).currentStrength == 0) {
/*      */         
/* 4240 */         Skill def = null;
/*      */         
/*      */         try {
/* 4243 */           def = defender.getSkills().getSkill(10054);
/*      */         }
/* 4245 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4247 */           def = defender.getSkills().learn(10054, 1.0F);
/*      */         } 
/* 4249 */         if (defender.getStatus().getStamina() > 2000)
/*      */         {
/* 4251 */           if (def.skillCheck((this.creature.getBaseCombatRating() * 2.0F), 0.0D, (this.creature.isNoSkillFor(defender) || 
/* 4252 */               (defender.getCombatHandler()).receivedFStyleSkill), 10.0F, defender, this.creature) > 0.0D) {
/*      */             
/* 4254 */             (defender.getCombatHandler()).receivedFStyleSkill = true;
/*      */             
/* 4256 */             defBonus += def.getKnowledge(0.0D) / 4.0D;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/* 4261 */     if ((defender.getCombatHandler()).currentStrength > 0 && defender instanceof Player)
/*      */     {
/* 4263 */       if (defender.isMoving()) {
/* 4264 */         defBonus -= ((defender.getCombatHandler()).currentStrength * 15);
/* 4265 */       } else if ((defender.getCombatHandler()).currentStrength > 1) {
/* 4266 */         defBonus -= ((defender.getCombatHandler()).currentStrength * 7);
/*      */       }  } 
/* 4268 */     if (defender.isOnHostileHomeServer()) {
/*      */       
/* 4270 */       defBonus -= 20.0D;
/*      */     }
/* 4272 */     else if (this.creature.isMoving() && this.creature instanceof Player) {
/* 4273 */       attBonus -= 15.0D;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final float getDistdiff(Creature creature, Creature opponent, AttackAction atk) {
/* 4278 */     if (atk != null && !atk.isUsingWeapon()) {
/*      */       
/* 4280 */       float idealDist = (10 + atk.getAttackValues().getAttackReach() * 3);
/* 4281 */       float dist = Creature.rangeToInDec(creature, opponent);
/*      */       
/* 4283 */       return idealDist - dist;
/*      */     } 
/*      */ 
/*      */     
/* 4287 */     Item wpn = creature.getPrimWeapon();
/* 4288 */     return getDistdiff(wpn, creature, opponent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final float getDistdiff(Item weapon, Creature creature, Creature opponent) {
/* 4294 */     float idealDist = (10 + Weapon.getReachForWeapon(weapon) * 3);
/*      */     
/* 4296 */     float dist = Creature.rangeToInDec(creature, opponent);
/* 4297 */     return idealDist - dist;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float getFootingModifier(Item weapon, Creature opponent) {
/* 4306 */     short[] steepness = Creature.getTileSteepness((this.creature.getCurrentTile()).tilex, (this.creature.getCurrentTile()).tiley, this.creature
/* 4307 */         .isOnSurface());
/*      */ 
/*      */     
/* 4310 */     float footingMod = 0.0F;
/* 4311 */     float heightDiff = 0.0F;
/*      */     
/* 4313 */     heightDiff = Math.max(-1.45F, this.creature.getStatus().getPositionZ() + this.creature.getAltOffZ()) - Math.max(-1.45F, opponent.getStatus().getPositionZ() + opponent.getAltOffZ());
/* 4314 */     if (heightDiff > 0.5D) {
/* 4315 */       footingMod = (float)(footingMod + 0.1D);
/* 4316 */     } else if (heightDiff < -0.5D) {
/* 4317 */       footingMod -= 0.1F;
/* 4318 */     }  if (this.creature.isSubmerged()) {
/* 4319 */       return 1.0F;
/*      */     }
/* 4321 */     if (this.creature.getVehicle() == -10L) {
/*      */       
/* 4323 */       if (weapon != null)
/*      */       {
/* 4325 */         if (opponent.getVehicle() != -10L && weapon.isTwoHanded() && !weapon.isWeaponBow())
/* 4326 */           footingMod += 0.3F; 
/*      */       }
/* 4328 */       if (this.creature.getStatus().getPositionZ() <= -1.45F)
/* 4329 */         return 0.2F + footingMod; 
/* 4330 */       if (this.creature.isPlayer() && (steepness[1] > 20 || steepness[1] < -20))
/*      */       {
/* 4332 */         Skill bcskill = null;
/*      */         
/*      */         try {
/* 4335 */           bcskill = this.creature.getSkills().getSkill(104);
/*      */         }
/* 4337 */         catch (NoSuchSkillException nss) {
/*      */           
/* 4339 */           bcskill = this.creature.getSkills().learn(104, 1.0F);
/*      */         } 
/* 4341 */         if (bcskill != null)
/*      */         {
/* 4343 */           if (bcskill.skillCheck(Math.abs(Math.max(Math.min(steepness[1], 99), -99)), (this.creature.fightlevel * 10), true, 1.0F) > 0.0D)
/*      */           {
/* 4345 */             return 1.0F + footingMod; } 
/*      */         }
/* 4347 */         if (steepness[1] > 40 || steepness[1] < -40) {
/*      */           
/* 4349 */           if (steepness[1] > 60 || steepness[1] < -60) {
/*      */             
/* 4351 */             if (steepness[1] > 80 || steepness[1] < -80) {
/*      */               
/* 4353 */               if (steepness[1] > 100 || steepness[1] < -100)
/*      */               {
/* 4355 */                 return 0.2F + footingMod;
/*      */               }
/* 4357 */               return 0.4F + footingMod;
/*      */             } 
/* 4359 */             return 0.6F + footingMod;
/*      */           } 
/* 4361 */           return 0.8F + footingMod;
/*      */         } 
/* 4363 */         return 0.9F + footingMod;
/*      */       }
/*      */     
/* 4366 */     } else if (opponent.isSubmerged()) {
/*      */       
/* 4368 */       footingMod = 0.0F;
/* 4369 */     }  return 1.0F + footingMod;
/*      */   }
/*      */ 
/*      */   
/*      */   private float getDirectionTo(Creature opponent) {
/* 4374 */     float defAngle = Creature.normalizeAngle(opponent.getStatus().getRotation());
/* 4375 */     double newrot = Math.atan2((this.creature.getStatus().getPositionY() - opponent.getStatus().getPositionY()), (this.creature
/* 4376 */         .getStatus().getPositionX() - opponent.getStatus().getPositionX()));
/* 4377 */     float attAngle = (float)(newrot * 57.29577951308232D) + 90.0F;
/* 4378 */     return Creature.normalizeAngle(attAngle - defAngle);
/*      */   }
/*      */ 
/*      */   
/*      */   private float getFlankingModifier(Creature opponent) {
/* 4383 */     if (opponent == null) {
/* 4384 */       return 1.0F;
/*      */     }
/* 4386 */     float attAngle = getDirectionTo(opponent);
/* 4387 */     if (opponent.getVehicle() > -10L) {
/*      */       
/* 4389 */       Vehicle vehic = Vehicles.getVehicleForId(opponent.getVehicle());
/* 4390 */       if (vehic != null && vehic.isCreature()) {
/*      */         
/*      */         try {
/*      */           
/* 4394 */           Creature ridden = Server.getInstance().getCreature(opponent.getVehicle());
/* 4395 */           attAngle = getDirectionTo(ridden);
/*      */         }
/* 4397 */         catch (Exception ex) {
/*      */ 
/*      */           
/* 4400 */           logger.log(Level.INFO, "No creature for id " + opponent.getVehicle());
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 4405 */     if (attAngle > 140.0F && attAngle < 220.0F) {
/*      */       
/* 4407 */       if (attAngle > 160.0F && attAngle < 200.0F) {
/* 4408 */         return 1.25F;
/*      */       }
/* 4410 */       return 1.1F;
/*      */     } 
/* 4412 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   private float getHeightModifier(Creature opponent) {
/* 4417 */     if (opponent == null)
/* 4418 */       return 1.0F; 
/* 4419 */     float diff = this.creature.getPositionZ() + this.creature.getAltOffZ() - opponent.getPositionZ() + opponent.getAltOffZ();
/* 4420 */     if (diff > 1.0F) {
/*      */       
/* 4422 */       if (diff > 2.0F)
/* 4423 */         return 1.1F; 
/* 4424 */       return 1.05F;
/*      */     } 
/* 4426 */     if (diff < -1.0F) {
/*      */       
/* 4428 */       if (diff < -2.0F)
/* 4429 */         return 0.9F; 
/* 4430 */       return 0.95F;
/*      */     } 
/* 4432 */     return 1.0F;
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
/*      */   public static final byte getStanceForAction(ActionEntry entry) {
/* 4446 */     if (entry.isAttackHigh()) {
/*      */       
/* 4448 */       if (entry.isAttackLeft())
/* 4449 */         return 6; 
/* 4450 */       if (entry.isAttackRight())
/* 4451 */         return 1; 
/* 4452 */       return 7;
/*      */     } 
/* 4454 */     if (entry.isAttackLow()) {
/*      */       
/* 4456 */       if (entry.isAttackLeft())
/* 4457 */         return 4; 
/* 4458 */       if (entry.isAttackRight())
/* 4459 */         return 3; 
/* 4460 */       return 10;
/*      */     } 
/*      */ 
/*      */     
/* 4464 */     if (entry.isAttackLeft())
/* 4465 */       return 5; 
/* 4466 */     if (entry.isAttackRight())
/* 4467 */       return 2; 
/* 4468 */     if (entry.isDefend()) {
/*      */       
/* 4470 */       switch (entry.getNumber()) {
/*      */         
/*      */         case 314:
/* 4473 */           return 12;
/*      */         case 315:
/* 4475 */           return 14;
/*      */         case 316:
/* 4477 */           return 11;
/*      */         case 317:
/* 4479 */           return 13;
/*      */       } 
/* 4481 */       return 0;
/*      */     } 
/*      */     
/* 4484 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKillEffects(Creature performer, Creature defender) {
/* 4490 */     defender.setOpponent(null);
/* 4491 */     defender.setTarget(-10L, true);
/* 4492 */     if (defender.getWurmId() == performer.target)
/* 4493 */       performer.setTarget(-10L, true); 
/* 4494 */     defender.getCombatHandler().setCurrentStance(-1, (byte)15);
/* 4495 */     performer.getCombatHandler().setCurrentStance(-1, (byte)15);
/* 4496 */     if (performer.isUndead()) {
/*      */       
/* 4498 */       performer.healRandomWound(100);
/* 4499 */       float nut = (50 + Server.rand.nextInt(49)) / 100.0F;
/* 4500 */       performer.getStatus().refresh(nut, true);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4510 */     if (performer.getCitizenVillage() != null)
/*      */     {
/*      */       
/* 4513 */       performer.getCitizenVillage().removeTarget(defender);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4523 */     if (defender.isPlayer() && performer.isPlayer()) {
/*      */       
/* 4525 */       if (!defender.isOkToKillBy(performer))
/*      */       {
/* 4527 */         if (performer.hasAttackedUnmotivated() && performer.getReputation() < 0) {
/* 4528 */           performer.setReputation(performer.getReputation() - 10);
/*      */         } else {
/* 4530 */           performer.setReputation(performer.getReputation() - 20);
/*      */         } 
/*      */       }
/* 4533 */       if (!defender.isFriendlyKingdom(performer.getKingdomId()))
/*      */       {
/* 4535 */         if (!Players.getInstance().isOverKilling(performer.getWurmId(), defender.getWurmId())) {
/*      */           
/* 4537 */           if (performer.getKingdomTemplateId() == 3 || (performer.getDeity() != null && performer.getDeity().isHateGod())) {
/* 4538 */             performer.maybeModifyAlignment(-5.0F);
/*      */           } else {
/* 4540 */             performer.maybeModifyAlignment(5.0F);
/* 4541 */           }  if ((performer.getCombatHandler()).currentStrength == 0) {
/* 4542 */             performer.achievement(43);
/*      */           }
/*      */         } 
/*      */       }
/* 4546 */     } else if (!defender.isPlayer() && !performer.isPlayer()) {
/*      */       
/* 4548 */       if (defender.isPrey() && performer.isCarnivore())
/*      */       {
/* 4550 */         performer.getStatus().modifyHunger(-65000, 99.0F);
/*      */       }
/*      */     } 
/* 4553 */     if (!defender.isPlayer() && !defender.isReborn() && performer.isPlayer()) {
/*      */       
/* 4555 */       if (defender.isKingdomGuard() && defender.getKingdomId() == performer.getKingdomId()) {
/* 4556 */         performer.achievement(44);
/*      */       }
/*      */       try {
/* 4559 */         int tid = defender.getTemplate().getTemplateId();
/* 4560 */         if (CreatureTemplate.isDragon(tid)) {
/* 4561 */           ((Player)performer).addTitle(Titles.Title.DragonSlayer);
/* 4562 */         } else if (tid == 11 || tid == 27) {
/* 4563 */           ((Player)performer).addTitle(Titles.Title.TrollSlayer);
/* 4564 */         } else if (tid == 20 || tid == 22) {
/* 4565 */           ((Player)performer).addTitle(Titles.Title.GiantSlayer);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 4571 */       catch (Exception ex) {
/*      */         
/* 4573 */         logger.log(Level.WARNING, defender.getName() + " and " + performer.getName() + ":" + ex.getMessage(), ex);
/*      */       } 
/* 4575 */       if (performer.getDeity() != null && (performer.getDeity()).number == 2)
/*      */       {
/* 4577 */         performer.maybeModifyAlignment(0.5F);
/*      */       }
/* 4579 */       if (performer.getDeity() != null && (performer.getDeity()).number == 4)
/*      */       {
/* 4581 */         performer.maybeModifyAlignment(-0.5F);
/*      */       }
/*      */     } 
/*      */     
/* 4585 */     if (performer.getPrimWeapon() != null) {
/*      */       
/* 4587 */       float ms = performer.getPrimWeapon().getSpellMindStealModifier();
/* 4588 */       if (ms > 0.0F && !defender.isPlayer() && defender.getKingdomId() != performer.getKingdomId()) {
/*      */         
/* 4590 */         Skills s = defender.getSkills();
/* 4591 */         int r = Server.rand.nextInt((s.getSkills()).length);
/* 4592 */         Skill toSteal = s.getSkills()[r];
/* 4593 */         float skillStolen = ms / 100.0F * 0.1F;
/*      */ 
/*      */         
/*      */         try {
/* 4597 */           Skill owned = this.creature.getSkills().getSkill(toSteal.getNumber());
/* 4598 */           if (owned.getKnowledge() < toSteal.getKnowledge())
/*      */           {
/* 4600 */             double smod = (toSteal.getKnowledge() - owned.getKnowledge()) / 100.0D;
/*      */ 
/*      */             
/* 4603 */             owned.setKnowledge(owned.getKnowledge() + skillStolen * smod, false);
/* 4604 */             this.creature.getCommunicator().sendSafeServerMessage("The " + performer
/* 4605 */                 .getPrimWeapon().getName() + " steals some " + toSteal.getName() + ".");
/*      */           }
/*      */         
/* 4608 */         } catch (NoSuchSkillException noSuchSkillException) {}
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkStanceChange(Creature defender, Creature opponent) {
/* 4618 */     if (defender.isFighting())
/*      */     {
/* 4620 */       if (defender.isPlayer()) {
/*      */ 
/*      */         
/* 4623 */         if (defender.isAutofight() && Server.rand.nextInt(10) == 0)
/*      */         {
/* 4625 */           selectStance(defender, opponent);
/* 4626 */           return true;
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 4632 */       else if (Server.rand.nextInt(5) == 0) {
/*      */         
/* 4634 */         selectStance(defender, opponent);
/* 4635 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 4639 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void selectStance(Creature defender, Creature player) {
/* 4644 */     boolean selectNewStance = false;
/*      */     
/*      */     try {
/* 4647 */       if (!defender.getCurrentAction().isDefend() && !defender.getCurrentAction().isStanceChange()) {
/* 4648 */         selectNewStance = true;
/*      */       }
/* 4650 */     } catch (NoSuchActionException nsa) {
/*      */       
/* 4652 */       selectNewStance = true;
/*      */     } 
/* 4654 */     if (!defender.isPlayer() && selectNewStance && Server.rand
/*      */       
/* 4656 */       .nextInt((int)(11.0F - Math.min(10.0F, defender.getAggressivity() * defender
/* 4657 */           .getStatus().getAggTypeModifier() / 10.0F))) != 0) {
/* 4658 */       selectNewStance = false;
/*      */     }
/*      */     
/* 4661 */     if (selectNewStance) {
/*      */       
/* 4663 */       selectStanceList.clear();
/* 4664 */       float mycr = -1.0F;
/* 4665 */       float oppcr = -1.0F;
/* 4666 */       float knowl = -1.0F;
/* 4667 */       if (defender.isFighting()) {
/*      */         
/* 4669 */         if (defender.mayRaiseFightLevel() && defender.getMindLogical().getKnowledge(0.0D) > 7.0D)
/*      */         {
/* 4671 */           if (defender.isPlayer() || Server.rand.nextInt(100) < 30) {
/*      */ 
/*      */             
/* 4674 */             selectNewStance = false;
/* 4675 */             selectStanceList.add(Actions.actionEntrys[340]);
/*      */           } else {
/*      */             
/* 4678 */             selectStanceList.add(Actions.actionEntrys[340]);
/*      */           } 
/*      */         }
/* 4681 */         if (defender.isPlayer())
/*      */         {
/* 4683 */           if (getSpeed(defender.getPrimWeapon()) > Server.rand.nextInt(10))
/* 4684 */             selectNewStance = false; 
/*      */         }
/* 4686 */         if (selectNewStance) {
/*      */           
/* 4688 */           mycr = defender.getCombatHandler().getCombatRating(player, defender.getPrimWeapon(), false);
/* 4689 */           oppcr = player.getCombatHandler().getCombatRating(defender, player.getPrimWeapon(), false);
/*      */           
/* 4691 */           knowl = getCombatKnowledgeSkill();
/* 4692 */           if (knowl > 50.0F) {
/* 4693 */             selectStanceList.addAll(standardDefences);
/*      */           }
/* 4695 */           if (!defender.isPlayer())
/* 4696 */             knowl += 20.0F; 
/* 4697 */           selectStanceList.addAll(defender.getCombatHandler().getHighAttacks((Item)null, true, player, mycr, oppcr, knowl));
/* 4698 */           selectStanceList.addAll(defender.getCombatHandler().getMidAttacks((Item)null, true, player, mycr, oppcr, knowl));
/* 4699 */           selectStanceList.addAll(defender.getCombatHandler().getLowAttacks((Item)null, true, player, mycr, oppcr, knowl));
/*      */         } 
/*      */       } 
/* 4702 */       if (selectStanceList.size() > 0)
/*      */       {
/*      */         
/* 4705 */         selectStanceFromList(defender, player, mycr, oppcr, knowl);
/*      */       }
/* 4707 */       if (!defender.isPlayer() && Server.rand.nextInt(10) == 0) {
/*      */         
/* 4709 */         int randInt = Server.rand.nextInt(100);
/* 4710 */         if (randInt <= Math.max(10.0F, (defender.getAggressivity() - 20) * defender.getStatus().getAggTypeModifier())) {
/*      */           
/* 4712 */           if (defender.getFightStyle() != 1)
/*      */           {
/* 4714 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 4715 */             segments.add(new CreatureLineSegment(defender));
/* 4716 */             segments.add(new MulticolorLineSegment(" suddenly goes into a frenzy.", (byte)0));
/*      */             
/* 4718 */             player.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */             
/* 4721 */             defender.setFightingStyle((byte)1);
/*      */           }
/*      */         
/* 4724 */         } else if (randInt > Math.min(90.0F, (defender.getAggressivity() * defender.getStatus().getAggTypeModifier() + 20.0F) * defender
/* 4725 */             .getStatus().getAggTypeModifier())) {
/*      */           
/* 4727 */           if (defender.getFightStyle() != 2)
/*      */           {
/* 4729 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 4730 */             segments.add(new CreatureLineSegment(defender));
/* 4731 */             segments.add(new MulticolorLineSegment(" cowers.", (byte)0));
/*      */             
/* 4733 */             player.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */             
/* 4736 */             defender.setFightingStyle((byte)2);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 4741 */           if (defender.getFightStyle() == 1) {
/*      */             
/* 4743 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 4744 */             segments.add(new CreatureLineSegment(defender));
/* 4745 */             segments.add(new MulticolorLineSegment(" calms down a bit.", (byte)0));
/*      */             
/* 4747 */             player.getCommunicator().sendColoredMessageCombat(segments);
/*      */           
/*      */           }
/* 4750 */           else if (defender.getFightStyle() == 2) {
/*      */             
/* 4752 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 4753 */             segments.add(new CreatureLineSegment(defender));
/* 4754 */             segments.add(new MulticolorLineSegment(" seems a little more brave now.", (byte)0));
/*      */             
/* 4756 */             player.getCommunicator().sendColoredMessageCombat(segments);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 4761 */           if (defender.getFightStyle() != 0) {
/* 4762 */             defender.setFightingStyle((byte)0);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final ActionEntry getDefensiveActionEntry(byte opponentStance) {
/* 4770 */     for (Iterator<ActionEntry> it = selectStanceList.listIterator(); it.hasNext(); ) {
/*      */       
/* 4772 */       ActionEntry e = it.next();
/* 4773 */       e = Actions.actionEntrys[e.getNumber()];
/* 4774 */       if (isStanceParrying(getStanceForAction(e), opponentStance))
/*      */       {
/*      */         
/* 4777 */         if (!isAtSoftSpot(getStanceForAction(e), opponentStance))
/* 4778 */           return e; 
/*      */       }
/*      */     } 
/* 4781 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final ActionEntry getOpposingActionEntry(byte opponentStance) {
/* 4786 */     for (Iterator<ActionEntry> it = selectStanceList.listIterator(); it.hasNext(); ) {
/*      */       
/* 4788 */       ActionEntry e = it.next();
/* 4789 */       e = Actions.actionEntrys[e.getNumber()];
/* 4790 */       if (isStanceOpposing(getStanceForAction(e), opponentStance))
/*      */       {
/*      */         
/* 4793 */         if (!isAtSoftSpot(getStanceForAction(e), opponentStance))
/* 4794 */           return e; 
/*      */       }
/*      */     } 
/* 4797 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final ActionEntry getNonDefensiveActionEntry(byte opponentStance) {
/* 4802 */     for (int x = 0; x < selectStanceList.size(); x++) {
/*      */       
/* 4804 */       int num = Server.rand.nextInt(selectStanceList.size());
/* 4805 */       ActionEntry e = selectStanceList.get(num);
/* 4806 */       e = Actions.actionEntrys[e.getNumber()];
/*      */       
/* 4808 */       if (!isStanceParrying(getStanceForAction(e), opponentStance) && 
/* 4809 */         !isStanceOpposing(getStanceForAction(e), opponentStance))
/*      */       {
/*      */         
/* 4812 */         if (!isAtSoftSpot(getStanceForAction(e), opponentStance))
/* 4813 */           return e; 
/*      */       }
/*      */     } 
/* 4816 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean isNextGoodStance(byte currentStance, byte nextStance, byte opponentStance) {
/* 4822 */     if (isAtSoftSpot(nextStance, opponentStance)) {
/* 4823 */       return false;
/*      */     }
/* 4825 */     if (isAtSoftSpot(opponentStance, currentStance)) {
/* 4826 */       return false;
/*      */     }
/* 4828 */     if (isAtSoftSpot(opponentStance, nextStance))
/* 4829 */       return true; 
/* 4830 */     if (currentStance == 0)
/* 4831 */       return (nextStance == 5 || nextStance == 2); 
/* 4832 */     if (currentStance == 5)
/* 4833 */       return (nextStance == 6 || nextStance == 4); 
/* 4834 */     if (currentStance == 2)
/* 4835 */       return (nextStance == 1 || nextStance == 3); 
/* 4836 */     if (currentStance == 1 || currentStance == 6)
/* 4837 */       return (nextStance == 7); 
/* 4838 */     if (currentStance == 3 || currentStance == 4)
/* 4839 */       return (nextStance == 10); 
/* 4840 */     return false;
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
/*      */   static final byte[] getSoftSpots(byte currentStance) {
/* 4852 */     if (currentStance == 0)
/* 4853 */       return standardSoftSpots; 
/* 4854 */     if (currentStance == 5)
/* 4855 */       return midLeftSoftSpots; 
/* 4856 */     if (currentStance == 2)
/* 4857 */       return midRightSoftSpots; 
/* 4858 */     if (currentStance == 1)
/* 4859 */       return upperRightSoftSpots; 
/* 4860 */     if (currentStance == 6)
/* 4861 */       return upperLeftSoftSpots; 
/* 4862 */     if (currentStance == 3)
/* 4863 */       return lowerRightSoftSpots; 
/* 4864 */     if (currentStance == 4)
/* 4865 */       return lowerLeftSoftSpots; 
/* 4866 */     return emptyByteArray;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isAtSoftSpot(byte stanceChecked, byte stanceUnderAttack) {
/* 4871 */     byte[] opponentSoftSpots = getSoftSpots(stanceChecked);
/* 4872 */     for (byte spot : opponentSoftSpots) {
/*      */       
/* 4874 */       if (spot == stanceUnderAttack)
/*      */       {
/* 4876 */         return true;
/*      */       }
/*      */     } 
/* 4879 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean existsBetterOffensiveStance(byte _currentStance, byte opponentStance) {
/* 4884 */     if (isAtSoftSpot(opponentStance, _currentStance)) {
/* 4885 */       return false;
/*      */     }
/* 4887 */     boolean isOpponentAtSoftSpot = isAtSoftSpot(_currentStance, opponentStance);
/* 4888 */     if (isOpponentAtSoftSpot || (
/* 4889 */       !isStanceParrying(_currentStance, opponentStance) && !isStanceOpposing(_currentStance, opponentStance))) {
/*      */ 
/*      */       
/* 4892 */       for (int i = 0; i < selectStanceList.size(); i++) {
/*      */         
/* 4894 */         int num = Server.rand.nextInt(selectStanceList.size());
/* 4895 */         ActionEntry e = selectStanceList.get(num);
/* 4896 */         e = Actions.actionEntrys[e.getNumber()];
/* 4897 */         byte nextStance = getStanceForAction(e);
/* 4898 */         if (isNextGoodStance(_currentStance, nextStance, opponentStance))
/*      */         {
/* 4900 */           return true;
/*      */         }
/*      */       } 
/* 4903 */       return false;
/*      */     } 
/* 4905 */     for (int x = 0; x < selectStanceList.size(); x++) {
/*      */       
/* 4907 */       int num = Server.rand.nextInt(selectStanceList.size());
/* 4908 */       ActionEntry e = selectStanceList.get(num);
/* 4909 */       e = Actions.actionEntrys[e.getNumber()];
/* 4910 */       byte nextStance = getStanceForAction(e);
/* 4911 */       if (!isStanceParrying(_currentStance, nextStance) && !isStanceOpposing(_currentStance, nextStance))
/*      */       {
/* 4913 */         return true;
/*      */       }
/*      */     } 
/* 4916 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final ActionEntry changeToBestOffensiveStance(byte _currentStance, byte opponentStance) {
/* 4921 */     for (int x = 0; x < selectStanceList.size(); x++) {
/*      */       
/* 4923 */       int num = Server.rand.nextInt(selectStanceList.size());
/* 4924 */       ActionEntry e = selectStanceList.get(num);
/* 4925 */       e = Actions.actionEntrys[e.getNumber()];
/* 4926 */       byte nextStance = getStanceForAction(e);
/* 4927 */       if (isNextGoodStance(_currentStance, nextStance, opponentStance))
/*      */       {
/* 4929 */         return e;
/*      */       }
/*      */     } 
/* 4932 */     return null;
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
/*      */   private final void selectStanceFromList(Creature defender, Creature opponent, float mycr, float oppcr, float knowl) {
/* 4947 */     ActionEntry e = null;
/* 4948 */     if (defender.isPlayer() || defender.getMindLogical().getKnowledge(0.0D) > 17.0D) {
/*      */       
/* 4950 */       if (oppcr - mycr > 3.0F)
/*      */       {
/* 4952 */         if (Server.rand.nextInt(2) == 0) {
/*      */           
/* 4954 */           if (defender.mayRaiseFightLevel()) {
/* 4955 */             e = Actions.actionEntrys[340];
/*      */           }
/* 4957 */         } else if (defender.opponent == opponent) {
/*      */           
/* 4959 */           e = getDefensiveActionEntry((opponent.getCombatHandler()).currentStance);
/* 4960 */           if (e == null)
/* 4961 */             e = getOpposingActionEntry((opponent.getCombatHandler()).currentStance); 
/*      */         } 
/*      */       }
/* 4964 */       if (e == null)
/*      */       {
/* 4966 */         if (defender.combatRound > 2 && Server.rand.nextInt(2) == 0) {
/*      */           
/* 4968 */           if (defender.mayRaiseFightLevel()) {
/* 4969 */             e = Actions.actionEntrys[340];
/*      */           }
/* 4971 */         } else if (mycr - oppcr > 2.0F || defender.getCombatHandler().getSpeed(defender.getPrimWeapon()) < 3.0F) {
/*      */           
/* 4973 */           if (existsBetterOffensiveStance((defender.getCombatHandler()).currentStance, 
/* 4974 */               (opponent.getCombatHandler()).currentStance)) {
/*      */             
/* 4976 */             e = changeToBestOffensiveStance((defender.getCombatHandler()).currentStance, 
/* 4977 */                 (opponent.getCombatHandler()).currentStance);
/* 4978 */             if (e == null) {
/* 4979 */               e = getNonDefensiveActionEntry((opponent.getCombatHandler()).currentStance);
/*      */             }
/*      */           } 
/* 4982 */         } else if (mycr >= oppcr) {
/*      */           
/* 4984 */           if ((defender.getStatus()).damage < (opponent.getStatus()).damage) {
/*      */             
/* 4986 */             if (existsBetterOffensiveStance((defender.getCombatHandler()).currentStance, 
/* 4987 */                 (opponent.getCombatHandler()).currentStance)) {
/*      */               
/* 4989 */               e = changeToBestOffensiveStance((defender.getCombatHandler()).currentStance, 
/* 4990 */                   (opponent.getCombatHandler()).currentStance);
/* 4991 */               if (e == null) {
/* 4992 */                 e = getNonDefensiveActionEntry((opponent.getCombatHandler()).currentStance);
/*      */               }
/*      */             } 
/*      */           } else {
/*      */             
/* 4997 */             e = getDefensiveActionEntry((opponent.getCombatHandler()).currentStance);
/* 4998 */             if (e == null) {
/* 4999 */               e = getOpposingActionEntry((opponent.getCombatHandler()).currentStance);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/* 5004 */     } else if (e == null) {
/*      */       
/* 5006 */       if (!Server.rand.nextBoolean() || defender.getShield() == null) {
/*      */         
/* 5008 */         int num = Server.rand.nextInt(selectStanceList.size());
/* 5009 */         e = selectStanceList.get(num);
/* 5010 */         e = Actions.actionEntrys[e.getNumber()];
/*      */       } else {
/*      */         
/* 5013 */         e = Actions.actionEntrys[105];
/*      */       } 
/* 5015 */     }  if (e != null && e.getNumber() > 0) {
/*      */       
/*      */       try {
/*      */         
/* 5019 */         if (Creature.rangeTo(defender, opponent) <= e.getRange()) {
/*      */           
/* 5021 */           if (e.getNumber() == 105) {
/*      */             
/* 5023 */             defender.setAction(new Action(defender, -1L, opponent.getWurmId(), e.getNumber(), defender
/* 5024 */                   .getPosX(), defender.getPosY(), defender.getPositionZ() + defender.getAltOffZ(), defender
/* 5025 */                   .getStatus().getRotation()));
/*      */           }
/* 5027 */           else if (e.isStanceChange() && e.getNumber() != 340) {
/*      */             
/* 5029 */             if (getStanceForAction(e) != this.currentStance)
/*      */             {
/*      */               
/* 5032 */               defender.setAction(new Action(defender, -1L, opponent.getWurmId(), e.getNumber(), defender
/* 5033 */                     .getPosX(), defender.getPosY(), defender.getPositionZ() + defender.getAltOffZ(), defender
/* 5034 */                     .getStatus()
/* 5035 */                     .getRotation()));
/*      */             }
/*      */           }
/* 5038 */           else if (defender.mayRaiseFightLevel() && e.getNumber() == 340) {
/*      */             
/* 5040 */             defender.setAction(new Action(defender, -1L, opponent.getWurmId(), e.getNumber(), defender
/* 5041 */                   .getPosX(), defender.getPosY(), defender.getPositionZ() + defender.getAltOffZ(), defender
/* 5042 */                   .getStatus().getRotation()));
/*      */           } else {
/*      */             
/* 5045 */             defender.setAction(new Action(defender, -1L, opponent.getWurmId(), e.getNumber(), defender
/* 5046 */                   .getPosX(), defender.getPosY(), defender.getPositionZ() + defender.getAltOffZ(), defender
/* 5047 */                   .getStatus().getRotation()));
/*      */           } 
/*      */         } else {
/* 5050 */           logger.log(Level.INFO, defender.getName() + " too far away for stance " + e.getActionString() + " attacking " + opponent
/* 5051 */               .getName() + " with range " + Creature.rangeTo(defender, opponent));
/*      */         } 
/* 5053 */       } catch (Exception fe) {
/*      */         
/* 5055 */         logger.log(Level.WARNING, defender.getName() + " failed:" + fe.getMessage(), fe);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final byte[] getOptions(List<ActionEntry> list, byte currentStance) {
/* 5062 */     if (list == null || list.isEmpty())
/* 5063 */       return NO_COMBAT_OPTIONS; 
/* 5064 */     byte[] toReturn = new byte[31];
/* 5065 */     for (ListIterator<ActionEntry> it = list.listIterator(); it.hasNext(); ) {
/*      */       
/* 5067 */       ActionEntry act = it.next();
/* 5068 */       int x = act.getNumber() - 287;
/*      */       
/* 5070 */       if (act.isDefend()) {
/*      */         
/* 5072 */         toReturn[x] = 50; continue;
/*      */       } 
/* 5074 */       if (x >= 0 && x <= 30)
/*      */       {
/* 5076 */         if (getStanceForAction(Actions.actionEntrys[act.getNumber()]) != currentStance)
/*      */         {
/* 5078 */           toReturn[x] = Byte.parseByte(act.getActionString().substring(0, act.getActionString().indexOf("%")));
/*      */         }
/*      */       }
/*      */     } 
/* 5082 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addParryModifier(DoubleValueModifier modifier) {
/* 5087 */     if (this.parryModifiers == null)
/* 5088 */       this.parryModifiers = new HashSet<>(); 
/* 5089 */     this.parryModifiers.add(modifier);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeParryModifier(DoubleValueModifier modifier) {
/* 5094 */     if (this.parryModifiers != null) {
/* 5095 */       this.parryModifiers.remove(modifier);
/*      */     }
/*      */   }
/*      */   
/*      */   private double getParryMod() {
/* 5100 */     if (this.parryModifiers == null) {
/* 5101 */       return 1.0D;
/*      */     }
/*      */     
/* 5104 */     double doubleModifier = 1.0D;
/* 5105 */     for (DoubleValueModifier lDoubleValueModifier : this.parryModifiers)
/* 5106 */       doubleModifier += lDoubleValueModifier.getModifier(); 
/* 5107 */     return doubleModifier;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDodgeModifier(DoubleValueModifier modifier) {
/* 5113 */     if (this.dodgeModifiers == null)
/* 5114 */       this.dodgeModifiers = new HashSet<>(); 
/* 5115 */     this.dodgeModifiers.add(modifier);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeDodgeModifier(DoubleValueModifier modifier) {
/* 5120 */     if (this.dodgeModifiers != null) {
/* 5121 */       this.dodgeModifiers.remove(modifier);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private double getDodgeMod() {
/* 5128 */     float diff = this.creature.getTemplate().getWeight() / this.creature.getWeight();
/* 5129 */     if (this.creature.isPlayer())
/*      */     {
/*      */       
/* 5132 */       diff = this.creature.getTemplate().getWeight() / (this.creature.getWeight() + this.creature.getBody().getBodyItem().getFullWeight() + this.creature.getInventory().getFullWeight());
/*      */     }
/*      */     
/* 5135 */     diff = 0.8F + diff * 0.2F;
/* 5136 */     if (this.dodgeModifiers == null) {
/* 5137 */       return (1.0F * diff);
/*      */     }
/*      */     
/* 5140 */     double doubleModifier = 1.0D;
/* 5141 */     for (DoubleValueModifier lDoubleValueModifier : this.dodgeModifiers)
/* 5142 */       doubleModifier += lDoubleValueModifier.getModifier(); 
/* 5143 */     return doubleModifier * diff;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void setFightingStyle(byte style) {
/* 5149 */     if (style == 2) {
/* 5150 */       this.currentStrength = 0;
/* 5151 */     } else if (style == 1) {
/* 5152 */       this.currentStrength = 3;
/*      */     } else {
/* 5154 */       this.currentStrength = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getMoveStack() {
/* 5164 */     return this.moveStack;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void clearMoveStack() {
/* 5173 */     if (this.moveStack != null) {
/*      */       
/* 5175 */       this.moveStack.clear();
/* 5176 */       this.moveStack = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   byte getOpportunityAttacks() {
/* 5187 */     return this.opportunityAttacks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSentAttacks() {
/* 5197 */     return this.sentAttacks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSentAttacks(boolean aSentAttacks) {
/* 5208 */     this.sentAttacks = aSentAttacks;
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
/*      */   public void setRodEffect(boolean effect) {
/* 5271 */     this.hasRodEffect = effect;
/* 5272 */     sendRodEffect();
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRodEffect() {
/* 5277 */     if (this.hasRodEffect) {
/*      */       
/* 5279 */       this.creature.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.ROD_BEGUILING_EFFECT, 100000, 100.0F);
/*      */     }
/*      */     else {
/*      */       
/* 5283 */       this.creature.getCommunicator().sendRemoveSpellEffect(SpellEffectsEnum.ROD_BEGUILING_EFFECT);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String getOthersString() {
/* 5288 */     return othersString;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setOthersString(String othersString) {
/* 5293 */     CombatHandler.othersString = othersString;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CombatHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */