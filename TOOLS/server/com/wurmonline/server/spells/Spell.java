/*      */ package com.wurmonline.server.spells;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MessageServer;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.combat.Battle;
/*      */ import com.wurmonline.server.combat.BattleEvent;
/*      */ import com.wurmonline.server.combat.Battles;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.SpellEffects;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemSpellEffects;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.utils.CreatureLineSegment;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.Enchants;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Random;
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
/*      */ public abstract class Spell
/*      */   implements SpellTypes, MiscConstants, SoundNames, Enchants, TimeConstants, Comparable<Spell>
/*      */ {
/*   74 */   protected static final Logger logger = Logger.getLogger(Spell.class.getName());
/*      */   
/*      */   public static final byte TYPE_CHAPLAIN = 1;
/*      */   
/*      */   public static final byte TYPE_MISSIONARY = 2;
/*      */   
/*      */   public static final byte TYPE_BOTH = 0;
/*      */   
/*      */   boolean[][] area;
/*      */   
/*      */   int[][] offsets;
/*      */   
/*      */   public final int number;
/*      */   
/*      */   public final String name;
/*      */   
/*      */   private final int castingTime;
/*      */   
/*      */   public boolean religious = false;
/*      */   
/*      */   public boolean offensive = false;
/*      */   
/*      */   public boolean healing = false;
/*      */   
/*      */   public boolean singleItemEnchant = false;
/*      */   
/*      */   static final int enchantDifficulty = 60;
/*      */   
/*      */   final int cost;
/*      */   
/*      */   protected boolean hasDynamicCost = false;
/*      */   
/*      */   protected final int difficulty;
/*      */   
/*      */   public final int level;
/*      */   
/*      */   protected boolean targetCreature = false;
/*      */   
/*      */   protected boolean targetItem = false;
/*      */   
/*      */   protected boolean targetWeapon = false;
/*      */   
/*      */   protected boolean targetArmour = false;
/*      */   
/*      */   protected boolean targetJewelry = false;
/*      */   
/*      */   protected boolean targetPendulum = false;
/*      */   
/*      */   protected boolean targetWound = false;
/*      */   
/*      */   protected boolean targetTile = false;
/*      */   
/*      */   protected boolean targetTileBorder = false;
/*      */   
/*      */   protected boolean karmaSpell = false;
/*      */   
/*  130 */   private long cooldown = 60000L;
/*      */   
/*      */   boolean dominate = false;
/*      */   
/*      */   public boolean isRitual = false;
/*      */   
/*  136 */   byte enchantment = 0;
/*      */   
/*  138 */   public String effectdesc = "";
/*      */   
/*  140 */   String description = "N/A";
/*      */   
/*  142 */   protected byte type = 0;
/*      */   
/*      */   public static final int TIME_ENCHANT_CAST = 30;
/*      */   
/*      */   public static final long TIME_ENCHANT = 300000L;
/*      */   
/*      */   public static final long TIME_CONTINUUM = 240000L;
/*      */   
/*      */   public static final long TIME_CREATUREBUFF = 180000L;
/*      */   
/*      */   public static final long TIME_AOE = 120000L;
/*      */   
/*      */   public static final long TIME_UTILITY = 1800000L;
/*      */   
/*      */   public static final long TIME_UTILITY_HALF = 900000L;
/*      */   
/*      */   public static final long TIME_UTILITY_DOUBLE = 3600000L;
/*      */   
/*      */   public static final long TIME_COMBAT = 0L;
/*      */   
/*      */   public static final long TIME_COMBAT_SMALLDELAY = 10000L;
/*      */   
/*      */   public static final long TIME_COMBAT_NORMALDELAY = 30000L;
/*      */   
/*      */   public static final long TIME_COMBAT_LONGDELAY = 60000L;
/*      */   
/*      */   public static final int Spirit_Fire = 1;
/*      */   
/*      */   public static final int Spirit_Water = 2;
/*      */   
/*      */   public static final int Spirit_Earth = 3;
/*      */   
/*      */   public static final int Spirit_Air = 4;
/*      */   public static final double minOffensivePower = 50.0D;
/*      */   
/*      */   Spell(String _name, int num, int _castingTime, int _cost, int _difficulty, int _level, long _cooldown) {
/*  178 */     this.name = _name;
/*  179 */     this.number = num;
/*  180 */     this.castingTime = _castingTime;
/*  181 */     this.cost = _cost;
/*  182 */     this.difficulty = _difficulty;
/*  183 */     this.level = _level;
/*  184 */     this.cooldown = _cooldown;
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
/*      */   Spell(String _name, int num, int _castingTime, int _cost, int _difficulty, int _level, String aEffectDescription, byte aEnchantment, boolean aDominate, boolean aReligious, boolean aOffensive, boolean aTargetCreature, boolean aTargetItem, boolean aTargetWound, boolean aTargetTile) {
/*  196 */     this.name = _name;
/*  197 */     this.number = num;
/*  198 */     this.castingTime = _castingTime;
/*  199 */     this.cost = _cost;
/*  200 */     this.difficulty = _difficulty;
/*  201 */     this.level = _level;
/*  202 */     this.effectdesc = aEffectDescription;
/*  203 */     this.enchantment = aEnchantment;
/*  204 */     this.dominate = aDominate;
/*  205 */     this.religious = aReligious;
/*  206 */     this.offensive = aOffensive;
/*  207 */     this.targetCreature = aTargetCreature;
/*  208 */     this.targetItem = aTargetItem;
/*  209 */     this.targetTile = aTargetTile;
/*  210 */     this.targetWound = aTargetWound;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Spell(String aName, int aNum, int aCastingTime, int aCost, int aDifficulty, int aLevel, long aCooldown, boolean aReligious) {
/*  220 */     this.name = aName;
/*  221 */     this.number = aNum;
/*  222 */     this.castingTime = aCastingTime;
/*  223 */     this.cost = aCost;
/*  224 */     this.difficulty = aDifficulty;
/*  225 */     this.level = aLevel;
/*  226 */     this.cooldown = aCooldown;
/*  227 */     this.religious = aReligious;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(Spell otherSpell) {
/*  233 */     return getName().compareTo(otherSpell.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean mayBeEnchanted(Item target) {
/*  238 */     if (target.isOverrideNonEnchantable()) {
/*  239 */       return true;
/*      */     }
/*  241 */     if (target.isBodyPart() || target
/*  242 */       .isNewbieItem() || target
/*  243 */       .isNoTake() || target
/*      */       
/*  245 */       .getTemplateId() == 179 || target
/*  246 */       .getTemplateId() == 386 || target
/*  247 */       .isTemporary() || target
/*  248 */       .getTemplateId() == 272 || (target
/*  249 */       .isLockable() && target.getLockId() != -10L) || target
/*  250 */       .isIndestructible() || target
/*  251 */       .isHugeAltar() || target
/*  252 */       .isDomainItem() || target
/*  253 */       .isKingdomMarker() || target
/*  254 */       .isTraded() || target
/*  255 */       .isBanked() || target
/*  256 */       .isArtifact() || target
/*  257 */       .isEgg() || target
/*  258 */       .isChallengeNewbieItem() || target
/*  259 */       .isRiftLoot() || target
/*  260 */       .isRiftAltar() || target
/*  261 */       .getTemplateId() == 1307)
/*  262 */       return false; 
/*  263 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getCooldown() {
/*  268 */     return this.cooldown;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setType(byte newType) {
/*  273 */     this.type = newType;
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
/*      */   protected static double trimPower(Creature performer, double power) {
/*  288 */     if (Servers.localServer.HOMESERVER && performer.isChampion())
/*  289 */       power = Math.min(power, 50.0D); 
/*  290 */     if (performer.hasFlag(82))
/*  291 */       power += 5.0D; 
/*  292 */     return power;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean stillCooldown(Creature performer) {
/*  297 */     if (this.cooldown > 0L) {
/*      */       
/*  299 */       Cooldowns cd = Cooldowns.getCooldownsFor(performer.getWurmId(), false);
/*  300 */       if (cd != null) {
/*      */         
/*  302 */         long avail = cd.isAvaibleAt(this.number);
/*  303 */         if (avail > System.currentTimeMillis() && performer.getPower() < 3) {
/*      */           
/*  305 */           performer.getCommunicator().sendNormalServerMessage("You need to wait " + 
/*  306 */               Server.getTimeFor(avail - System.currentTimeMillis()) + " until you can cast " + this.name + " again.");
/*      */           
/*  308 */           return true;
/*      */         } 
/*      */       } 
/*  311 */       for (Creature c : performer.getLinks()) {
/*      */         
/*  313 */         if (stillCooldown(c)) {
/*      */           
/*  315 */           Cooldowns cd2 = Cooldowns.getCooldownsFor(performer.getWurmId(), false);
/*  316 */           if (cd2 != null) {
/*      */             
/*  318 */             long avail = cd2.isAvaibleAt(this.number);
/*  319 */             if (avail > System.currentTimeMillis() && c.getPower() < 3)
/*      */             {
/*  321 */               performer.getCommunicator().sendNormalServerMessage(c
/*  322 */                   .getName() + " needs to wait " + Server.getTimeFor(avail - System.currentTimeMillis()) + " until " + c
/*  323 */                   .getHeSheItString() + " can cast " + this.name + " again.");
/*      */             }
/*      */           } 
/*  326 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*  330 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown(Creature performer) {
/*  335 */     if (this.cooldown > 0L) {
/*      */       
/*  337 */       Cooldowns cd = Cooldowns.getCooldownsFor(performer.getWurmId(), true);
/*  338 */       cd.addCooldown(this.number, System.currentTimeMillis() + this.cooldown, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static double modifyDamage(Creature target, double damage) {
/*  344 */     if (!target.isPlayer()) {
/*      */ 
/*      */       
/*  347 */       double armourMult = (target.getArmourMod() * 2.0F);
/*      */       
/*  349 */       double bodyStrengthWeight = target.getStrengthSkill() * 0.25D;
/*  350 */       double soulStrengthWeight = target.getSoulStrengthVal() * 0.75D;
/*  351 */       double strengthMult = (100.0D - bodyStrengthWeight + soulStrengthWeight) * 0.02D;
/*      */       
/*  353 */       double damageMult = armourMult * strengthMult;
/*      */       
/*  355 */       double clampedMult = damageMult / (1.0D + damageMult / 3.0D);
/*      */       
/*  357 */       damage *= clampedMult;
/*      */     } 
/*  359 */     return damage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean checkFavorRequirements(Creature performer, float baseCost) {
/*  370 */     if (isReligious()) {
/*      */       
/*  372 */       if (baseCost < 15.0F)
/*      */       {
/*  374 */         if (performer.getFavor() < baseCost)
/*      */         {
/*  376 */           return true;
/*      */         }
/*      */       }
/*  379 */       if (baseCost < 200.0F && performer.getFavor() < baseCost * 0.33F)
/*      */       {
/*  381 */         if (performer.getFavor() < 15.0F)
/*      */         {
/*  383 */           return true;
/*      */         }
/*      */       }
/*      */     } 
/*  387 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Skill getCastingSkill(Creature performer) {
/*  398 */     if (this.religious) {
/*  399 */       return performer.getChannelingSkill();
/*      */     }
/*  401 */     return performer.getMindLogical();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean canCastSpell(Creature performer) {
/*  411 */     if (isReligious())
/*      */     {
/*      */       
/*  414 */       return (performer.isPriest() || performer.getPower() > 0);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  419 */     return performer.knowsKarmaSpell(getNumber());
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
/*      */   private boolean isCastValid(Creature performer, boolean validTarget, String targetName, int tileX, int tileY, boolean onSurface) {
/*  434 */     if (!canCastSpell(performer)) {
/*      */       
/*  436 */       performer.getCommunicator().sendNormalServerMessage("You cannot cast that spell.");
/*  437 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  441 */     if (isReligiousSpell())
/*      */     {
/*  443 */       if (!validTarget) {
/*      */         
/*  445 */         performer.getCommunicator().sendNormalServerMessage("You cannot cast " + getName() + " on " + targetName + ".");
/*  446 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  451 */     if (stillCooldown(performer))
/*      */     {
/*      */       
/*  454 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  458 */     if (performer.attackingIntoIllegalDuellingRing(tileX, tileY, onSurface)) {
/*      */       
/*  460 */       performer.getCommunicator().sendNormalServerMessage("The duelling ring is holy ground and casting is restricted across the border.");
/*      */       
/*  462 */       return true;
/*      */     } 
/*  464 */     return true;
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
/*      */   public final boolean isValidItemType(Creature performer, Item target) {
/*  477 */     if (isTargetItem()) {
/*  478 */       return true;
/*      */     }
/*      */     
/*  481 */     if (isTargetArmour() && target.isArmour()) {
/*  482 */       return true;
/*      */     }
/*      */     
/*  485 */     if (isTargetWeapon() && (target.isWeapon() || target.isWeaponBow() || target.isBowUnstringed() || target.isArrow())) {
/*  486 */       return true;
/*      */     }
/*      */     
/*  489 */     if (isTargetJewelry() && target.isEnchantableJewelry()) {
/*  490 */       return true;
/*      */     }
/*      */     
/*  493 */     if (isTargetPendulum() && target.getTemplateId() == 233) {
/*  494 */       return true;
/*      */     }
/*      */     
/*  497 */     EnchantUtil.sendInvalidTargetMessage(performer, this);
/*  498 */     return false;
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
/*      */   public final boolean run(Creature performer, Item target, float counter) {
/*  511 */     boolean done = false;
/*      */ 
/*      */     
/*  514 */     if (!isCastValid(performer, target.isBodyPart() ? isTargetCreature() : isTargetAnyItem(), target.getNameWithGenus(), target
/*  515 */         .getTileX(), target.getTileY(), target.isOnSurface()))
/*      */     {
/*      */       
/*  518 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  522 */     if (!isValidItemType(performer, target)) {
/*  523 */       return true;
/*      */     }
/*  525 */     if (target.getTemplateId() == 669) {
/*      */       
/*  527 */       performer.getCommunicator().sendNormalServerMessage("You cannot cast " + getName() + " on the bulk item.");
/*  528 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  532 */     if (target.isMagicContainer()) {
/*      */       
/*  534 */       performer.getCommunicator().sendNormalServerMessage("You cannot cast " + getName() + " on the " + target.getName() + ".");
/*  535 */       return true;
/*      */     } 
/*      */     
/*  538 */     Skill castSkill = getCastingSkill(performer);
/*  539 */     if (!precondition(castSkill, performer, target))
/*  540 */       return true; 
/*  541 */     float baseCost = getCost(target);
/*      */     
/*  543 */     if (performer.getPower() >= 5 && Servers.isThisATestServer())
/*  544 */       baseCost = 1.0F; 
/*  545 */     float needed = baseCost;
/*  546 */     if (this.religious) {
/*      */       
/*  548 */       if (performer.isRoyalPriest())
/*  549 */         needed *= 0.5F; 
/*  550 */       if (performer.getFavorLinked() < needed)
/*      */       {
/*  552 */         performer.getCommunicator().sendNormalServerMessage("You need more favor with your god to cast that spell.");
/*  553 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  558 */     else if (performer.getKarma() < needed) {
/*      */       
/*  560 */       performer.getCommunicator().sendNormalServerMessage("You need more karma to use that ability.");
/*  561 */       return true;
/*      */     } 
/*      */     
/*  564 */     if (counter == 1.0F)
/*      */     {
/*  566 */       if (checkFavorRequirements(performer, baseCost)) {
/*      */         
/*  568 */         performer.getCommunicator().sendNormalServerMessage("You need more favor from your god to cast that spell.");
/*      */         
/*  570 */         return true;
/*      */       } 
/*      */     }
/*  573 */     double power = 0.0D;
/*  574 */     if (counter == 1.0F && getCastingTime(performer) > 1) {
/*      */       
/*  576 */       if (isItemEnchantment())
/*      */       {
/*  578 */         if (performer.isChampion()) {
/*      */           
/*  580 */           if (((Player)performer).getChampionPoints() <= 0) {
/*      */             
/*  582 */             performer.getCommunicator().sendNormalServerMessage("You will need to spend one Champion point in order to enchant items.");
/*      */             
/*  584 */             return true;
/*      */           } 
/*      */           
/*  587 */           performer.getCommunicator().sendAlertServerMessage("You will spend one champion point if you successfully enchant the item!");
/*      */         } 
/*      */       }
/*      */       
/*  591 */       performer.setStealth(false);
/*  592 */       performer.getCommunicator().sendNormalServerMessage("You start to cast '" + this.name + "' on " + target
/*  593 */           .getNameWithGenus() + ".");
/*  594 */       Server.getInstance().broadCastAction(performer
/*  595 */           .getNameWithGenus() + " starts to cast '" + this.name + "' on " + target.getNameWithGenus() + ".", performer, 5, shouldMessageCombat());
/*  596 */       performer.sendActionControl(Actions.actionEntrys[122].getVerbString(), true, 
/*  597 */           getCastingTime(performer) * 10);
/*      */     } 
/*  599 */     int speedMod = 0;
/*  600 */     if (!this.religious) {
/*      */       
/*  602 */       Skill sp = performer.getMindSpeed();
/*  603 */       if (sp != null) {
/*  604 */         speedMod = (int)(sp.getKnowledge(0.0D) / 25.0D);
/*      */       }
/*      */     } 
/*  607 */     if (counter >= (getCastingTime(performer) - speedMod) || (counter > 2.0F && performer.getPower() == 5)) {
/*      */       
/*  609 */       done = true;
/*  610 */       boolean limitFail = false;
/*  611 */       if (isOffensive() && performer.getArmourLimitingFactor() < 0.0F && Server.rand
/*  612 */         .nextFloat() < Math.abs(performer.getArmourLimitingFactor()))
/*      */       {
/*  614 */         limitFail = true;
/*      */       }
/*  616 */       float bonus = 0.0F;
/*  617 */       if (!this.religious) {
/*      */         
/*  619 */         Skill sp = performer.getMindSpeed();
/*  620 */         if (sp != null) {
/*  621 */           sp.skillCheck(getDifficulty(true), performer.zoneBonus, false, counter);
/*      */         }
/*      */       } else {
/*  624 */         bonus = Math.abs(performer.getAlignment()) - 49.0F;
/*  625 */       }  int rdDiff = 0;
/*  626 */       if (performer.mustChangeTerritory()) {
/*      */         
/*  628 */         bonus -= 50.0F;
/*  629 */         rdDiff = 20;
/*      */       }
/*  631 */       else if (target.isCrystal() && !target.isGem()) {
/*  632 */         bonus += 100.0F;
/*      */       } 
/*      */       
/*  635 */       if (performer.getCitizenVillage() != null)
/*      */       {
/*  637 */         bonus += performer.getCitizenVillage().getFaithCreateBonus();
/*      */       }
/*  639 */       boolean dryRun = false;
/*      */       
/*  641 */       float modifier = 1.0F;
/*  642 */       if (target.getSpellEffects() != null)
/*      */       {
/*  644 */         modifier = target.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_ENCHANTABILITY);
/*      */       }
/*      */       
/*  647 */       if (bonus > 0.0F)
/*  648 */         bonus *= 1.0F + performer.getArmourLimitingFactor(); 
/*  649 */       power = trimPower(performer, castSkill
/*      */           
/*  651 */           .skillCheck((getDifficulty(true) + rdDiff + performer.getNumLinks() * 3), ((performer.zoneBonus + bonus) * modifier), false, counter));
/*      */       
/*  653 */       if (limitFail)
/*  654 */         power = (-30.0F + Server.rand.nextFloat() * 29.0F); 
/*  655 */       if (power >= 0.0D) {
/*      */         
/*  657 */         touchCooldown(performer);
/*      */         
/*  659 */         if (power >= 95.0D)
/*  660 */           performer.achievement(629); 
/*  661 */         performer.getCommunicator().sendNormalServerMessage("You cast '" + this.name + "' on " + target
/*  662 */             .getNameWithGenus() + ".");
/*  663 */         Server.getInstance().broadCastAction(performer
/*  664 */             .getNameWithGenus() + " casts '" + this.name + "' on " + target.getNameWithGenus() + ".", performer, 5, shouldMessageCombat());
/*  665 */         if (this.religious) {
/*      */           
/*  667 */           if (!postcondition(castSkill, performer, target, power)) {
/*      */ 
/*      */             
/*      */             try {
/*  671 */               performer.depleteFavor(baseCost / 20.0F, isOffensive());
/*      */             }
/*  673 */             catch (IOException iox) {
/*      */               
/*  675 */               logger.log(Level.WARNING, performer.getName(), iox);
/*  676 */               performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/*  677 */               return true;
/*      */             } 
/*      */           } else {
/*      */             
/*      */             try
/*      */             {
/*      */               
/*  684 */               performer.depleteFavor(needed, isOffensive());
/*      */             }
/*  686 */             catch (IOException iox)
/*      */             {
/*  688 */               logger.log(Level.WARNING, performer.getName(), iox);
/*  689 */               performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/*  690 */               return true;
/*      */             }
/*      */           
/*      */           } 
/*      */         } else {
/*      */           
/*  696 */           performer.modifyKarma((int)-needed);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  703 */         if (Servers.isThisATestServer()) {
/*  704 */           performer.getCommunicator().sendNormalServerMessage("Success Cost:" + needed + ", Power:" + power + ", SpeedMod:" + speedMod + ", Bonus:" + bonus);
/*      */         }
/*  706 */         doEffect(castSkill, power, performer, target);
/*  707 */         if (isItemEnchantment())
/*      */         {
/*  709 */           performer.achievement(606);
/*  710 */           if (performer.isChampion())
/*      */           {
/*  712 */             performer.modifyChampionPoints(-1);
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  718 */         if (this.religious) {
/*      */           
/*  720 */           if (performer.mustChangeTerritory() && performer.isPlayer())
/*      */           {
/*  722 */             if (Server.rand.nextInt(3) == 0) {
/*  723 */               performer.getCommunicator().sendAlertServerMessage("You sense a lack of energy. Rumours have it that " + performer
/*  724 */                   .getDeity().getName() + " wants " + performer
/*  725 */                   .getDeity().getHisHerItsString() + " champions to move between kingdoms and seek out the enemy.");
/*      */             }
/*      */           }
/*  728 */           performer.getCommunicator().sendNormalServerMessage("You fail to channel the '" + this.name + "'.");
/*  729 */           Server.getInstance().broadCastAction(performer.getNameWithGenus() + " fails to channel the '" + this.name + "'.", performer, 5, 
/*      */               
/*  731 */               shouldMessageCombat());
/*      */           
/*      */           try {
/*  734 */             performer.depleteFavor(baseCost / 5.0F, isOffensive());
/*      */           }
/*  736 */           catch (IOException iox) {
/*      */             
/*  738 */             logger.log(Level.WARNING, performer.getName(), iox);
/*  739 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/*  740 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  745 */           performer.getCommunicator().sendNormalServerMessage("The '" + this.name + "' fizzles!");
/*  746 */           Server.getInstance().broadCastAction(performer
/*  747 */               .getNameWithGenus() + " fizzles " + performer
/*  748 */               .getHisHerItsString() + " '" + this.name + "'!", performer, 5, 
/*  749 */               shouldMessageCombat());
/*      */         } 
/*      */         
/*  752 */         if (Servers.isThisATestServer()) {
/*  753 */           performer.getCommunicator().sendNormalServerMessage("Fail Cost:" + needed + ", Power:" + power);
/*      */         }
/*  755 */         doNegativeEffect(castSkill, power, performer, target);
/*      */       } 
/*      */     } 
/*  758 */     return done;
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
/*      */   public final boolean run(Creature performer, Creature target, float counter) {
/*  771 */     boolean done = false;
/*  772 */     if (target.isDead())
/*      */     {
/*  774 */       return true;
/*      */     }
/*      */     
/*  777 */     if (!isCastValid(performer, this.targetCreature, target.getNameWithGenus(), target
/*  778 */         .getTileX(), target.getTileY(), target.isOnSurface()))
/*      */     {
/*      */       
/*  781 */       return true;
/*      */     }
/*      */     
/*  784 */     Skill castSkill = getCastingSkill(performer);
/*  785 */     if (!precondition(castSkill, performer, target)) {
/*  786 */       return true;
/*      */     }
/*  788 */     float baseCost = getCost(target);
/*      */     
/*  790 */     if (performer.getPower() >= 5 && Servers.isThisATestServer())
/*  791 */       baseCost = 1.0F; 
/*  792 */     float needed = baseCost;
/*  793 */     if (this.religious) {
/*      */       
/*  795 */       if (performer.isRoyalPriest())
/*  796 */         needed *= 0.5F; 
/*  797 */       if (performer.getFavorLinked() < needed)
/*      */       {
/*  799 */         performer.getCommunicator().sendNormalServerMessage("You need more favor with your god to cast that spell.");
/*  800 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  805 */     else if (performer.getKarma() < needed) {
/*      */       
/*  807 */       performer.getCommunicator().sendNormalServerMessage("You need more karma to use that ability.");
/*  808 */       return true;
/*      */     } 
/*      */     
/*  811 */     if (counter == 1.0F) {
/*      */       
/*  813 */       if (checkFavorRequirements(performer, baseCost)) {
/*      */         
/*  815 */         performer.getCommunicator().sendNormalServerMessage("You need more favor from your god to cast that spell.");
/*      */         
/*  817 */         return true;
/*      */       } 
/*      */       
/*  820 */       if (this.offensive) {
/*      */         
/*  822 */         if (performer.opponent != null || target.isAggHuman())
/*      */         {
/*  824 */           if (performer.opponent == null)
/*  825 */             performer.setOpponent(target); 
/*      */         }
/*  827 */         if (target.opponent == null) {
/*      */           
/*  829 */           target.setOpponent(performer);
/*  830 */           target.setTarget(performer.getWurmId(), false);
/*  831 */           target.getCommunicator().sendNormalServerMessage(performer
/*  832 */               .getNameWithGenus() + " is attacking you with a spell!");
/*      */         } 
/*  834 */         target.addAttacker(performer);
/*      */       } 
/*      */     } 
/*  837 */     double power = 0.0D;
/*  838 */     int speedMod = 0;
/*      */     
/*  840 */     if (counter == 1.0F && getCastingTime(performer) > 1) {
/*      */       
/*  842 */       performer.setStealth(false);
/*  843 */       if (performer == target) {
/*      */         
/*  845 */         performer.getCommunicator().sendNormalServerMessage("You start to cast '" + this.name + "' on yourself.");
/*  846 */         Server.getInstance().broadCastAction(performer
/*  847 */             .getNameWithGenus() + " starts to cast '" + this.name + "' on " + target.getHimHerItString() + "self.", performer, 5, 
/*  848 */             shouldMessageCombat());
/*      */       }
/*      */       else {
/*      */         
/*  852 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  853 */         segments.add(new CreatureLineSegment(performer));
/*  854 */         segments.add(new MulticolorLineSegment(" starts to cast " + this.name + " on ", (byte)0));
/*  855 */         segments.add(new CreatureLineSegment(target));
/*  856 */         segments.add(new MulticolorLineSegment(".", (byte)0));
/*      */         
/*  858 */         MessageServer.broadcastColoredAction(segments, performer, 5, shouldMessageCombat());
/*  859 */         if (this.offensive || this.number == 450) {
/*  860 */           target.getCommunicator().sendColoredMessageCombat(segments, (byte)2);
/*      */         }
/*  862 */         ((MulticolorLineSegment)segments.get(1)).setText(" start to cast " + this.name + " on ");
/*  863 */         if (shouldMessageCombat()) {
/*  864 */           performer.getCommunicator().sendColoredMessageCombat(segments, (byte)2);
/*      */         } else {
/*  866 */           performer.getCommunicator().sendColoredMessageEvent(segments);
/*      */         } 
/*  868 */       }  performer.sendActionControl(Actions.actionEntrys[122].getVerbString(), true, 
/*  869 */           getCastingTime(performer) * 10);
/*      */     } 
/*      */     
/*  872 */     if (!isReligious()) {
/*      */       
/*  874 */       Skill sp = performer.getMindSpeed();
/*  875 */       if (sp != null)
/*  876 */         speedMod = (int)(sp.getKnowledge(0.0D) / 25.0D); 
/*      */     } 
/*  878 */     if (counter >= (getCastingTime(performer) - speedMod) || (counter > 2.0F && performer.getPower() == 5)) {
/*      */       
/*  880 */       done = true;
/*  881 */       double resist = 0.0D;
/*  882 */       double attbonus = 0.0D;
/*  883 */       if (!Zones.interruptedRange(performer, target)) {
/*      */         
/*  885 */         boolean limitFail = false;
/*  886 */         if (isOffensive() && performer.getArmourLimitingFactor() < 0.0F && Server.rand
/*  887 */           .nextFloat() < Math.abs(performer.getArmourLimitingFactor()))
/*      */         {
/*  889 */           limitFail = true;
/*      */         }
/*  891 */         target.setStealth(false);
/*  892 */         if (!isReligious()) {
/*      */           
/*  894 */           Skill sp = performer.getMindSpeed();
/*  895 */           if (sp != null)
/*  896 */             sp.skillCheck(this.difficulty, performer.zoneBonus, false, counter); 
/*      */         } 
/*  898 */         if (isOffensive()) {
/*      */           
/*  900 */           target.addAttacker(performer);
/*  901 */           if (performer.isPlayer() && target.isPlayer()) {
/*      */             
/*  903 */             Battle battle = Battles.getBattleFor(performer, target);
/*  904 */             if (battle != null)
/*  905 */               battle.addEvent(new BattleEvent((short)114, performer.getName(), target.getName(), performer
/*  906 */                     .getName() + " casts " + getName() + " at " + target.getName() + ".")); 
/*      */           } 
/*  908 */           int defSkill = 105;
/*  909 */           int attSkill = 105;
/*  910 */           if (this.dominate) {
/*      */             
/*      */             try
/*      */             {
/*  914 */               float extraDiff = 0.0F;
/*  915 */               if (target.isUnique())
/*  916 */                 extraDiff = target.getBaseCombatRating(); 
/*  917 */               attbonus = performer.getSkills().getSkill(attSkill).skillCheck(this.difficulty, performer.zoneBonus, false, counter);
/*  918 */               if (attbonus > 0.0D)
/*  919 */                 attbonus *= (1.0F + performer.getArmourLimitingFactor()); 
/*  920 */               power = trimPower(performer, castSkill
/*      */                   
/*  922 */                   .skillCheck((1.0F + ItemBonus.getSpellResistBonus(target)) * (target.getSkills().getSkill(defSkill).getKnowledge(0.0D) + target
/*  923 */                     .getStatus().getBattleRatingTypeModifier() + (performer
/*  924 */                     .getNumLinks() * 3) + extraDiff), performer.zoneBonus + attbonus, false, counter));
/*      */ 
/*      */             
/*      */             }
/*  928 */             catch (NoSuchSkillException nss)
/*      */             {
/*  930 */               performer.getCommunicator().sendNormalServerMessage(target
/*  931 */                   .getNameWithGenus() + " seems impossible to dominate.");
/*  932 */               logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  937 */             float abon = 0.0F;
/*  938 */             float defbon = 0.0F;
/*  939 */             if (performer.getEnemyPresense() > 1200 && target.isPlayer())
/*  940 */               abon = 20.0F; 
/*  941 */             if (target.getEnemyPresense() > 1200 && performer.isPlayer())
/*  942 */               defbon = 20.0F; 
/*  943 */             if (!this.religious) {
/*      */               
/*  945 */               attSkill = 101;
/*  946 */               defSkill = 101;
/*      */             } 
/*      */             
/*      */             try {
/*  950 */               resist = target.getSkills().getSkill(defSkill).skillCheck(this.difficulty, defbon, false, counter);
/*      */             }
/*  952 */             catch (NoSuchSkillException nss) {
/*      */               
/*  954 */               logger.log(Level.WARNING, target.getName() + " learning defskill " + defSkill, (Throwable)nss);
/*  955 */               if (target.isPlayer()) {
/*  956 */                 target.getSkills().learn(defSkill, 20.0F);
/*      */               } else {
/*  958 */                 target.getSkills().learn(defSkill, 99.99F);
/*      */               } 
/*      */             } 
/*      */             try {
/*  962 */               if (resist > 0.0D) {
/*  963 */                 attbonus = performer.getSkills().getSkill(attSkill).skillCheck(resist, abon, false, counter);
/*      */               } else {
/*  965 */                 attbonus = (10.0F + abon);
/*      */               } 
/*  967 */             } catch (NoSuchSkillException nss) {
/*      */               
/*  969 */               logger.log(Level.WARNING, performer.getName() + " learning attskill " + attSkill, (Throwable)nss);
/*  970 */               performer.getSkills().learn(attSkill, 1.0F);
/*      */             } 
/*      */           } 
/*  973 */           if (!target.isPlayer()) {
/*      */             
/*  975 */             if (!performer.isInvulnerable())
/*  976 */               target.setTarget(performer.getWurmId(), false); 
/*  977 */             target.setFleeCounter(20);
/*      */           } 
/*      */         } 
/*      */         
/*  981 */         float bonus = 0.0F;
/*  982 */         if (this.religious)
/*  983 */           bonus = Math.abs(performer.getAlignment()) - 49.0F; 
/*  984 */         if (bonus > 0.0F)
/*  985 */           bonus *= 1.0F + performer.getArmourLimitingFactor(); 
/*  986 */         double distDiff = 0.0D;
/*      */ 
/*      */         
/*  989 */         if (isOffensive() || getNumber() == 450) {
/*      */           
/*  991 */           double dist = Creature.getRange(performer, target.getPosX(), target.getPosY());
/*      */           
/*      */           try {
/*  994 */             distDiff = dist - (Actions.actionEntrys[this.number].getRange() / 2.0F);
/*      */             
/*  996 */             if (distDiff > 0.0D) {
/*  997 */               distDiff *= 2.0D;
/*      */             }
/*  999 */           } catch (Exception ex) {
/*      */             
/* 1001 */             logger.log(Level.WARNING, getName() + " error: " + ex.getMessage());
/*      */           } 
/*      */         } 
/* 1004 */         if (!this.dominate) {
/* 1005 */           power = trimPower(performer, Math.max((Server.rand
/* 1006 */                 .nextFloat() * 10.0F), castSkill
/* 1007 */                 .skillCheck((1.0F + ItemBonus.getSpellResistBonus(target)) * (distDiff + this.difficulty + (performer.getNumLinks() * 3)), performer.zoneBonus + attbonus + bonus, false, counter)));
/*      */         }
/* 1009 */         if (limitFail)
/* 1010 */           power = (-30.0F + Server.rand.nextFloat() * 29.0F); 
/*      */       } 
/* 1012 */       if (power > 0.0D) {
/*      */         
/* 1014 */         touchCooldown(performer);
/* 1015 */         Methods.sendSound(performer, "sound.religion.channel");
/* 1016 */         if (power >= 95.0D)
/* 1017 */           performer.achievement(629); 
/* 1018 */         if (performer == target) {
/*      */           
/* 1020 */           performer.getCommunicator().sendNormalServerMessage("You cast '" + this.name + "' on yourself.");
/* 1021 */           Server.getInstance().broadCastAction(performer
/* 1022 */               .getNameWithGenus() + " casts '" + this.name + "' on " + target
/* 1023 */               .getHimHerItString() + "self.", performer, 5, 
/* 1024 */               shouldMessageCombat());
/*      */         }
/*      */         else {
/*      */           
/* 1028 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1029 */           segments.add(new CreatureLineSegment(performer));
/* 1030 */           segments.add(new MulticolorLineSegment(" casts " + this.name + " on ", (byte)0));
/* 1031 */           segments.add(new CreatureLineSegment(target));
/* 1032 */           segments.add(new MulticolorLineSegment(".", (byte)0));
/*      */           
/* 1034 */           MessageServer.broadcastColoredAction(segments, performer, 5, shouldMessageCombat());
/* 1035 */           if (this.offensive || this.number == 450) {
/* 1036 */             target.getCommunicator().sendColoredMessageCombat(segments, (byte)2);
/*      */           }
/* 1038 */           ((MulticolorLineSegment)segments.get(1)).setText(" cast " + this.name + " on ");
/* 1039 */           if (shouldMessageCombat()) {
/* 1040 */             performer.getCommunicator().sendColoredMessageCombat(segments, (byte)2);
/*      */           } else {
/* 1042 */             performer.getCommunicator().sendColoredMessageEvent(segments);
/*      */           } 
/* 1044 */         }  if (Constants.devmode)
/* 1045 */           performer.getCommunicator().sendNormalServerMessage("Power=" + power); 
/* 1046 */         if (this.religious) {
/*      */           
/*      */           try
/*      */           {
/*      */             
/* 1051 */             performer.depleteFavor(needed, isOffensive());
/*      */           }
/* 1053 */           catch (IOException iox)
/*      */           {
/* 1055 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1056 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1057 */             return true;
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 1063 */           performer.modifyKarma((int)-needed);
/* 1064 */           if (!performer.isPlayer()) {
/*      */             
/*      */             try {
/*      */ 
/*      */               
/* 1069 */               performer.depleteFavor(100.0F, isOffensive());
/*      */             }
/* 1071 */             catch (IOException iox) {
/*      */               
/* 1073 */               logger.log(Level.WARNING, performer.getName(), iox);
/*      */             } 
/*      */           }
/*      */         } 
/* 1077 */         boolean eff = true;
/* 1078 */         if (isOffensive())
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 1083 */           if (target.getCultist() != null && target.getCultist().ignoresSpells()) {
/*      */             
/* 1085 */             eff = false;
/*      */             
/* 1087 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1088 */             segments.add(new CreatureLineSegment(target));
/* 1089 */             segments.add(new MulticolorLineSegment(" ignores the effects!", (byte)0));
/*      */             
/* 1091 */             MessageServer.broadcastColoredAction(segments, performer, target, 5, true);
/* 1092 */             if (shouldMessageCombat()) {
/* 1093 */               performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */             } else {
/* 1095 */               performer.getCommunicator().sendColoredMessageEvent(segments);
/*      */             } 
/* 1097 */             ((MulticolorLineSegment)segments.get(1)).setText(" ignore the effects!");
/* 1098 */             if (shouldMessageCombat()) {
/* 1099 */               target.getCommunicator().sendColoredMessageCombat(segments);
/*      */             } else {
/* 1101 */               target.getCommunicator().sendColoredMessageEvent(segments);
/*      */             } 
/*      */           } 
/*      */         }
/* 1105 */         if (eff)
/*      */         {
/*      */           
/* 1108 */           if (Servers.isThisATestServer()) {
/* 1109 */             performer.getCommunicator().sendNormalServerMessage("Success Cost:" + needed + ", Power:" + power + ", SpeedMod:" + speedMod + ", Bonus:" + attbonus);
/*      */           }
/* 1111 */           doEffect(castSkill, power, performer, target);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1116 */         if (isReligious()) {
/*      */           
/* 1118 */           performer.getCommunicator().sendNormalServerMessage("You fail to channel the '" + this.name + "'.");
/* 1119 */           Server.getInstance().broadCastAction(performer.getNameWithGenus() + " fails to channel the '" + this.name + "'.", performer, 5, 
/*      */               
/* 1121 */               shouldMessageCombat());
/*      */           
/*      */           try {
/* 1124 */             performer.depleteFavor(baseCost / 20.0F, isOffensive());
/*      */           }
/* 1126 */           catch (IOException iox) {
/*      */             
/* 1128 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1129 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1130 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1135 */           performer.getCommunicator().sendNormalServerMessage("The '" + this.name + "' fails!");
/* 1136 */           Server.getInstance().broadCastAction(performer
/* 1137 */               .getNameWithGenus() + " fails " + performer
/* 1138 */               .getHisHerItsString() + " '" + this.name + "'!", performer, 5, 
/* 1139 */               shouldMessageCombat());
/*      */         } 
/*      */         
/* 1142 */         if (Servers.isThisATestServer()) {
/* 1143 */           performer.getCommunicator().sendNormalServerMessage("Fail Cost:" + needed + ", Power:" + power);
/*      */         }
/* 1145 */         doNegativeEffect(castSkill, power, performer, target);
/*      */       } 
/*      */     } 
/* 1148 */     return done;
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
/*      */   public final boolean run(Creature performer, Wound target, float counter) {
/* 1161 */     boolean done = false;
/*      */     
/* 1163 */     if (!isCastValid(performer, this.targetWound, target.getName(), target
/* 1164 */         .getCreature().getTileX(), target.getCreature().getTileY(), target.getCreature().isOnSurface()))
/*      */     {
/*      */       
/* 1167 */       return true;
/*      */     }
/*      */     
/* 1170 */     Skill castSkill = getCastingSkill(performer);
/* 1171 */     if (!precondition(castSkill, performer, target)) {
/* 1172 */       return true;
/*      */     }
/* 1174 */     float baseCost = getCost(target);
/*      */     
/* 1176 */     if (performer.getPower() >= 5 && Servers.isThisATestServer())
/* 1177 */       baseCost = 1.0F; 
/* 1178 */     float needed = baseCost;
/* 1179 */     if (this.religious) {
/*      */       
/* 1181 */       if (performer.isRoyalPriest())
/* 1182 */         needed *= 0.5F; 
/* 1183 */       if (performer.getFavorLinked() < needed)
/*      */       {
/* 1185 */         performer.getCommunicator().sendNormalServerMessage("You need more favor with your god to cast that spell.");
/* 1186 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1191 */     else if (performer.getKarma() < needed) {
/*      */       
/* 1193 */       performer.getCommunicator().sendNormalServerMessage("You need more karma to use that ability.");
/* 1194 */       return true;
/*      */     } 
/*      */     
/* 1197 */     if (target.getCreature() == null) {
/*      */       
/* 1199 */       performer.getCommunicator().sendNormalServerMessage("You fail to get a clear line of sight.");
/* 1200 */       return true;
/*      */     } 
/* 1202 */     if (counter == 1.0F)
/*      */     {
/* 1204 */       if (checkFavorRequirements(performer, baseCost)) {
/*      */         
/* 1206 */         performer.getCommunicator().sendNormalServerMessage("You need more favor from your god to cast that spell.");
/*      */         
/* 1208 */         return true;
/*      */       } 
/*      */     }
/* 1211 */     double power = 0.0D;
/* 1212 */     if (counter == 1.0F && getCastingTime(performer) > 1) {
/*      */       
/* 1214 */       performer.setStealth(false);
/* 1215 */       performer.getCommunicator().sendNormalServerMessage("You start to cast '" + this.name + "' on the wound.");
/* 1216 */       if (target.getCreature() != null)
/* 1217 */         Server.getInstance().broadCastAction(performer
/* 1218 */             .getNameWithGenus() + " starts to cast '" + this.name + "' on " + target.getCreature().getName() + ".", performer, 5, 
/* 1219 */             shouldMessageCombat()); 
/* 1220 */       performer.sendActionControl(Actions.actionEntrys[122].getVerbString(), true, 
/* 1221 */           getCastingTime(performer) * 10);
/*      */     } 
/* 1223 */     int speedMod = 0;
/* 1224 */     if (!isReligious()) {
/*      */       
/* 1226 */       Skill sp = performer.getMindSpeed();
/* 1227 */       if (sp != null)
/* 1228 */         speedMod = (int)(sp.getKnowledge(0.0D) / 25.0D); 
/*      */     } 
/* 1230 */     if (counter >= (getCastingTime(performer) - speedMod) || (counter > 2.0F && performer.getPower() == 5)) {
/*      */       
/* 1232 */       done = true;
/* 1233 */       boolean limitFail = false;
/* 1234 */       if (isOffensive() && performer.getArmourLimitingFactor() < 0.0F && Server.rand
/* 1235 */         .nextFloat() < Math.abs(performer.getArmourLimitingFactor()))
/*      */       {
/* 1237 */         limitFail = true;
/*      */       }
/* 1239 */       float bonus = 0.0F;
/* 1240 */       if (!this.religious) {
/*      */         
/* 1242 */         Skill sp = performer.getMindSpeed();
/* 1243 */         if (sp != null) {
/* 1244 */           sp.skillCheck(this.difficulty, performer.zoneBonus, false, counter);
/*      */         }
/*      */       } else {
/* 1247 */         bonus = Math.abs(performer.getAlignment()) - 49.0F;
/* 1248 */       }  if (bonus > 0.0F)
/* 1249 */         bonus *= 1.0F + performer.getArmourLimitingFactor(); 
/* 1250 */       power = trimPower(performer, 
/* 1251 */           Math.max((Server.rand.nextFloat() * 10.0F), castSkill.skillCheck((this.difficulty + performer.getNumLinks() * 3), (performer.zoneBonus + bonus), false, counter)));
/*      */       
/* 1253 */       if (limitFail)
/* 1254 */         power = (-30.0F + Server.rand.nextFloat() * 29.0F); 
/* 1255 */       if (power >= 0.0D) {
/*      */         
/* 1257 */         touchCooldown(performer);
/* 1258 */         if (power >= 95.0D) {
/* 1259 */           performer.achievement(629);
/*      */         }
/* 1261 */         if (target.getCreature() != null)
/* 1262 */           Server.getInstance().broadCastAction(performer
/* 1263 */               .getNameWithGenus() + " casts '" + this.name + "' on " + target.getCreature().getName() + ".", performer, 5, 
/* 1264 */               shouldMessageCombat()); 
/* 1265 */         Battle battle = performer.getBattle();
/* 1266 */         if (battle != null)
/* 1267 */           battle.addEvent(new BattleEvent((short)114, performer.getName(), target.getName(), performer.getName() + " casts '" + this.name + "' on " + target
/* 1268 */                 .getCreature().getName() + ".")); 
/* 1269 */         if (this.religious) {
/*      */           
/*      */           try
/*      */           {
/*      */             
/* 1274 */             performer.depleteFavor(needed, isOffensive());
/*      */           }
/* 1276 */           catch (IOException iox)
/*      */           {
/* 1278 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1279 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1280 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1285 */           performer.modifyKarma((int)-needed);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1292 */         if (Servers.isThisATestServer()) {
/* 1293 */           performer.getCommunicator().sendNormalServerMessage("Success Cost:" + needed + ", Power:" + power + ", SpeedMod:" + speedMod + ", Bonus:" + bonus);
/*      */         }
/* 1295 */         doEffect(castSkill, power, performer, target);
/*      */       }
/*      */       else {
/*      */         
/* 1299 */         if (isReligious()) {
/*      */           
/* 1301 */           performer.getCommunicator().sendNormalServerMessage("You fail to channel the '" + this.name + "'.");
/* 1302 */           Server.getInstance().broadCastAction(performer.getNameWithGenus() + " fails to channel the '" + this.name + "'.", performer, 5, 
/* 1303 */               shouldMessageCombat());
/*      */           
/*      */           try {
/* 1306 */             performer.depleteFavor(baseCost / 20.0F, isOffensive());
/*      */           }
/* 1308 */           catch (IOException iox) {
/*      */             
/* 1310 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1311 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1312 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1317 */           performer.getCommunicator().sendNormalServerMessage("The '" + this.name + "' fails!");
/* 1318 */           Server.getInstance().broadCastAction(performer
/* 1319 */               .getNameWithGenus() + " fails " + performer.getHisHerItsString() + " '" + this.name + "'!", performer, 5, 
/* 1320 */               shouldMessageCombat());
/*      */         } 
/*      */ 
/*      */         
/* 1324 */         if (Servers.isThisATestServer()) {
/* 1325 */           performer.getCommunicator().sendNormalServerMessage("Fail Cost:" + needed + ", Power:" + power);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1330 */     return done;
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
/*      */   public final boolean run(Creature performer, int tilexborder, int tileyborder, int layer, int heightOffset, Tiles.TileBorderDirection dir, float counter) {
/* 1348 */     boolean done = false;
/*      */     
/* 1350 */     if (!isCastValid(performer, this.targetTileBorder, "that border", tilexborder, tileyborder, (layer >= 0)))
/*      */     {
/*      */ 
/*      */       
/* 1354 */       return true;
/*      */     }
/*      */     
/* 1357 */     Skill castSkill = getCastingSkill(performer);
/* 1358 */     if (!precondition(castSkill, performer, tilexborder, tileyborder, layer, heightOffset, dir)) {
/* 1359 */       return true;
/*      */     }
/* 1361 */     float baseCost = getCost(tilexborder, tileyborder, layer, heightOffset, dir);
/*      */     
/* 1363 */     if (performer.getPower() >= 5 && Servers.isThisATestServer())
/* 1364 */       baseCost = 1.0F; 
/* 1365 */     float needed = baseCost;
/* 1366 */     if (isReligious()) {
/*      */       
/* 1368 */       if (performer.isRoyalPriest())
/* 1369 */         needed *= 0.5F; 
/* 1370 */       if (performer.getFavorLinked() < needed)
/*      */       {
/* 1372 */         performer.getCommunicator().sendNormalServerMessage("You need more favor with your god to cast that spell.");
/* 1373 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1378 */     else if (performer.getPower() <= 1 && performer.getKarma() < needed) {
/*      */       
/* 1380 */       performer.getCommunicator().sendNormalServerMessage("You need more karma to use that ability.");
/* 1381 */       return true;
/*      */     } 
/*      */     
/* 1384 */     if (counter == 1.0F)
/*      */     {
/* 1386 */       if (checkFavorRequirements(performer, baseCost)) {
/*      */         
/* 1388 */         performer.getCommunicator().sendNormalServerMessage("You need more favor from your god to cast that spell.");
/*      */         
/* 1390 */         return true;
/*      */       } 
/*      */     }
/* 1393 */     double power = 0.0D;
/* 1394 */     if (counter == 1.0F && getCastingTime(performer) > 1) {
/*      */       
/* 1396 */       performer.setStealth(false);
/* 1397 */       performer.getCommunicator().sendNormalServerMessage("You start to cast '" + this.name + "'.");
/*      */       
/* 1399 */       Server.getInstance().broadCastAction(performer.getNameWithGenus() + " starts to cast '" + this.name + "'.", performer, 5, shouldMessageCombat());
/*      */       
/* 1401 */       performer.sendActionControl(Actions.actionEntrys[122].getVerbString(), true, 
/* 1402 */           getCastingTime(performer) * 10);
/*      */     } 
/* 1404 */     int speedMod = 0;
/* 1405 */     if (!isReligious()) {
/*      */       
/* 1407 */       Skill sp = performer.getMindSpeed();
/* 1408 */       if (sp != null)
/* 1409 */         speedMod = (int)(sp.getKnowledge(0.0D) / 25.0D); 
/*      */     } 
/* 1411 */     if (counter >= (getCastingTime(performer) - speedMod) || (counter > 2.0F && performer.getPower() == 5)) {
/*      */       
/* 1413 */       done = true;
/* 1414 */       boolean limitFail = false;
/* 1415 */       if (isOffensive() && performer.getArmourLimitingFactor() < 0.0F && Server.rand
/* 1416 */         .nextFloat() < Math.abs(performer.getArmourLimitingFactor()))
/*      */       {
/* 1418 */         limitFail = true;
/*      */       }
/* 1420 */       float bonus = 0.0F;
/* 1421 */       if (!isReligious()) {
/*      */         
/* 1423 */         Skill sp = performer.getMindSpeed();
/* 1424 */         if (sp != null) {
/* 1425 */           sp.skillCheck(this.difficulty, performer.zoneBonus, false, counter);
/*      */         }
/*      */       } else {
/* 1428 */         bonus = Math.abs(performer.getAlignment()) - 49.0F;
/* 1429 */       }  if (bonus > 0.0F)
/* 1430 */         bonus *= 1.0F + performer.getArmourLimitingFactor(); 
/* 1431 */       double distDiff = 0.0D;
/*      */ 
/*      */       
/* 1434 */       if (isOffensive() || getNumber() == 450) {
/*      */ 
/*      */         
/* 1437 */         double dist = 4.0D * Creature.getTileRange(performer, tilexborder, tileyborder);
/*      */         
/*      */         try {
/* 1440 */           distDiff = dist - (Actions.actionEntrys[this.number].getRange() / 2.0F);
/*      */ 
/*      */           
/* 1443 */           if (distDiff > 0.0D) {
/* 1444 */             distDiff *= 2.0D;
/*      */           }
/* 1446 */         } catch (Exception ex) {
/*      */           
/* 1448 */           logger.log(Level.WARNING, getName() + " error: " + ex.getMessage());
/*      */         } 
/*      */       } 
/* 1451 */       power = trimPower(performer, 
/*      */           
/* 1453 */           Math.max((Server.rand
/* 1454 */             .nextFloat() * 10.0F), castSkill
/* 1455 */             .skillCheck(distDiff + this.difficulty + (performer.getNumLinks() * 3), (performer.zoneBonus + bonus), false, counter)));
/*      */       
/* 1457 */       if (limitFail)
/* 1458 */         power = (-30.0F + Server.rand.nextFloat() * 29.0F); 
/* 1459 */       if (power >= 0.0D) {
/*      */         
/* 1461 */         if (performer.getPower() <= 1) {
/* 1462 */           touchCooldown(performer);
/*      */         }
/* 1464 */         if (power >= 95.0D)
/* 1465 */           performer.achievement(629); 
/* 1466 */         Server.getInstance().broadCastAction(performer.getNameWithGenus() + " casts '" + this.name + "'.", performer, 5, shouldMessageCombat());
/*      */         
/* 1468 */         performer.getCommunicator().sendNormalServerMessage("You succeed.");
/* 1469 */         if (isReligious()) {
/*      */           
/*      */           try
/*      */           {
/*      */             
/* 1474 */             performer.depleteFavor(needed, isOffensive());
/*      */           }
/* 1476 */           catch (IOException iox)
/*      */           {
/* 1478 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1479 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1480 */             return true;
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 1485 */         else if (performer.getPower() <= 1) {
/* 1486 */           performer.modifyKarma((int)-needed);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1493 */         if (Servers.isThisATestServer()) {
/* 1494 */           performer.getCommunicator().sendNormalServerMessage("Success Cost:" + needed + ", Power:" + power + ", SpeedMod:" + speedMod + ", Bonus:" + bonus);
/*      */         }
/* 1496 */         doEffect(castSkill, power, performer, tilexborder, tileyborder, layer, heightOffset, dir);
/*      */       }
/*      */       else {
/*      */         
/* 1500 */         if (this.religious) {
/*      */           
/* 1502 */           performer.getCommunicator().sendNormalServerMessage("You fail to channel the '" + this.name + "'.");
/* 1503 */           Server.getInstance().broadCastAction(performer.getNameWithGenus() + " fails to channel the '" + this.name + "'.", performer, 5, 
/* 1504 */               shouldMessageCombat());
/*      */           
/*      */           try {
/* 1507 */             performer.depleteFavor(baseCost / 20.0F, isOffensive());
/*      */           }
/* 1509 */           catch (IOException iox) {
/*      */             
/* 1511 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1512 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1513 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1518 */           performer.getCommunicator().sendNormalServerMessage("The '" + this.name + "' fails!");
/* 1519 */           Server.getInstance().broadCastAction(performer
/* 1520 */               .getNameWithGenus() + " fails " + performer.getHisHerItsString() + " '" + this.name + "'!", performer, 5, 
/* 1521 */               shouldMessageCombat());
/*      */         } 
/*      */         
/* 1524 */         if (Servers.isThisATestServer())
/* 1525 */           performer.getCommunicator().sendNormalServerMessage("Fail Cost:" + needed + ", Power:" + power); 
/*      */       } 
/*      */     } 
/* 1528 */     return done;
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
/*      */   public final boolean run(Creature performer, int tilex, int tiley, int layer, int heightOffset, float counter) {
/* 1545 */     boolean done = false;
/*      */     
/* 1547 */     if (!isCastValid(performer, this.targetTile, "that tile", tilex, tiley, (layer >= 0)))
/*      */     {
/*      */ 
/*      */       
/* 1551 */       return true;
/*      */     }
/*      */     
/* 1554 */     Skill castSkill = getCastingSkill(performer);
/* 1555 */     if (!precondition(castSkill, performer, tilex, tiley, layer)) {
/* 1556 */       return true;
/*      */     }
/* 1558 */     float baseCost = getCost(tilex, tiley, layer, heightOffset);
/*      */     
/* 1560 */     if (performer.getPower() >= 5 && Servers.isThisATestServer())
/* 1561 */       baseCost = 1.0F; 
/* 1562 */     float needed = baseCost;
/* 1563 */     if (isReligious()) {
/*      */       
/* 1565 */       if (performer.isRoyalPriest())
/* 1566 */         needed *= 0.5F; 
/* 1567 */       if (performer.getFavorLinked() < needed)
/*      */       {
/* 1569 */         performer.getCommunicator().sendNormalServerMessage("You need more favor with your god to cast that spell.");
/* 1570 */         return true;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1575 */     else if (performer.getPower() <= 1 && performer.getKarma() < needed) {
/*      */       
/* 1577 */       performer.getCommunicator().sendNormalServerMessage("You need more karma to use that ability.");
/* 1578 */       return true;
/*      */     } 
/*      */     
/* 1581 */     if (counter == 1.0F)
/*      */     {
/* 1583 */       if (checkFavorRequirements(performer, baseCost)) {
/*      */         
/* 1585 */         performer.getCommunicator().sendNormalServerMessage("You need more favor from your god to cast that spell.");
/*      */         
/* 1587 */         return true;
/*      */       } 
/*      */     }
/* 1590 */     double power = 0.0D;
/* 1591 */     if (counter == 1.0F && getCastingTime(performer) > 1) {
/*      */       
/* 1593 */       performer.setStealth(false);
/*      */ 
/*      */       
/* 1596 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1597 */       segments.add(new CreatureLineSegment(performer));
/* 1598 */       segments.add(new MulticolorLineSegment(" starts to cast " + getName() + ".", (byte)0));
/*      */       
/* 1600 */       MessageServer.broadcastColoredAction(segments, performer, null, 5, shouldMessageCombat(), (byte)2);
/*      */       
/* 1602 */       ((MulticolorLineSegment)segments.get(1)).setText(" start to cast " + getName() + ".");
/* 1603 */       if (shouldMessageCombat()) {
/* 1604 */         performer.getCommunicator().sendColoredMessageCombat(segments, (byte)2);
/*      */       } else {
/* 1606 */         performer.getCommunicator().sendColoredMessageEvent(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1613 */       performer.sendActionControl(Actions.actionEntrys[122].getVerbString(), true, 
/* 1614 */           getCastingTime(performer) * 10);
/*      */     } 
/* 1616 */     int speedMod = 0;
/* 1617 */     if (!isReligious()) {
/*      */       
/* 1619 */       Skill sp = performer.getMindSpeed();
/* 1620 */       if (sp != null)
/* 1621 */         speedMod = (int)(sp.getKnowledge(0.0D) / 25.0D); 
/*      */     } 
/* 1623 */     if (counter >= (getCastingTime(performer) - speedMod) || (counter > 2.0F && performer.getPower() == 5)) {
/*      */       
/* 1625 */       done = true;
/* 1626 */       boolean limitFail = false;
/* 1627 */       if (isOffensive() && performer.getArmourLimitingFactor() < 0.0F && Server.rand
/* 1628 */         .nextFloat() < Math.abs(performer.getArmourLimitingFactor()))
/*      */       {
/* 1630 */         limitFail = true;
/*      */       }
/* 1632 */       float bonus = 0.0F;
/* 1633 */       if (!isReligious()) {
/*      */         
/* 1635 */         Skill sp = performer.getMindSpeed();
/* 1636 */         if (sp != null) {
/* 1637 */           sp.skillCheck(this.difficulty, performer.zoneBonus, false, counter);
/*      */         }
/*      */       } else {
/* 1640 */         bonus = Math.abs(performer.getAlignment()) - 49.0F;
/*      */       } 
/* 1642 */       if (bonus > 0.0F)
/* 1643 */         bonus *= 1.0F + performer.getArmourLimitingFactor(); 
/* 1644 */       double distDiff = 0.0D;
/*      */       
/* 1646 */       if (isOffensive() || getNumber() == 450) {
/*      */ 
/*      */         
/* 1649 */         double dist = 4.0D * Creature.getTileRange(performer, tilex, tiley);
/*      */         
/*      */         try {
/* 1652 */           distDiff = dist - (Actions.actionEntrys[this.number].getRange() / 2.0F);
/*      */ 
/*      */           
/* 1655 */           if (distDiff > 0.0D) {
/* 1656 */             distDiff *= 2.0D;
/*      */           }
/* 1658 */         } catch (Exception ex) {
/*      */           
/* 1660 */           logger.log(Level.WARNING, getName() + " error: " + ex.getMessage());
/*      */         } 
/*      */       } 
/* 1663 */       power = trimPower(performer, 
/*      */           
/* 1665 */           Math.max((Server.rand
/* 1666 */             .nextFloat() * 10.0F), castSkill
/* 1667 */             .skillCheck(distDiff + this.difficulty + (performer.getNumLinks() * 3), (performer.zoneBonus + bonus), false, counter)));
/*      */       
/* 1669 */       if (limitFail)
/* 1670 */         power = (-30.0F + Server.rand.nextFloat() * 29.0F); 
/* 1671 */       if (power >= 0.0D) {
/*      */         
/* 1673 */         if (performer.getPower() <= 1) {
/* 1674 */           touchCooldown(performer);
/*      */         }
/* 1676 */         if (power >= 95.0D) {
/* 1677 */           performer.achievement(629);
/*      */         }
/* 1679 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1680 */         segments.add(new CreatureLineSegment(performer));
/* 1681 */         segments.add(new MulticolorLineSegment(" casts " + getName() + ".", (byte)0));
/*      */         
/* 1683 */         MessageServer.broadcastColoredAction(segments, performer, null, 5, shouldMessageCombat(), (byte)2);
/*      */         
/* 1685 */         ((MulticolorLineSegment)segments.get(1)).setText(" cast " + getName() + ".");
/* 1686 */         if (shouldMessageCombat()) {
/* 1687 */           performer.getCommunicator().sendColoredMessageCombat(segments, (byte)2);
/*      */         } else {
/* 1689 */           performer.getCommunicator().sendColoredMessageEvent(segments);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1694 */         if (isReligious()) {
/*      */           
/*      */           try
/*      */           {
/*      */             
/* 1699 */             performer.depleteFavor(needed, isOffensive());
/*      */           }
/* 1701 */           catch (IOException iox)
/*      */           {
/* 1703 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1704 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1705 */             return true;
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 1710 */         else if (performer.getPower() <= 1) {
/* 1711 */           performer.modifyKarma((int)-needed);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1718 */         if (Servers.isThisATestServer()) {
/* 1719 */           performer.getCommunicator().sendNormalServerMessage("Success Cost:" + needed + ", Power:" + power + ", SpeedMod:" + speedMod + ", Bonus:" + bonus);
/*      */         }
/* 1721 */         doEffect(castSkill, power, performer, tilex, tiley, layer, heightOffset);
/*      */       }
/*      */       else {
/*      */         
/* 1725 */         if (isReligious()) {
/*      */           
/* 1727 */           performer.getCommunicator().sendNormalServerMessage("You fail to channel the '" + this.name + "'.");
/* 1728 */           Server.getInstance().broadCastAction(performer.getNameWithGenus() + " fails to channel the '" + this.name + "'.", performer, 5, 
/* 1729 */               shouldMessageCombat());
/*      */           
/*      */           try {
/* 1732 */             performer.depleteFavor(baseCost / 20.0F, isOffensive());
/*      */           }
/* 1734 */           catch (IOException iox) {
/*      */             
/* 1736 */             logger.log(Level.WARNING, performer.getName(), iox);
/* 1737 */             performer.getCommunicator().sendNormalServerMessage("The spell fizzles!");
/* 1738 */             return true;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1743 */           performer.getCommunicator().sendNormalServerMessage("The '" + this.name + "' fails!");
/* 1744 */           Server.getInstance().broadCastAction(performer
/* 1745 */               .getNameWithGenus() + " fails " + performer.getHisHerItsString() + " '" + this.name + "'!", performer, 5, 
/* 1746 */               shouldMessageCombat());
/*      */         } 
/*      */         
/* 1749 */         if (Servers.isThisATestServer())
/* 1750 */           performer.getCommunicator().sendNormalServerMessage("Fail Cost:" + needed + ", Power:" + power); 
/*      */       } 
/*      */     } 
/* 1753 */     return done;
/*      */   }
/*      */   
/*      */   private boolean shouldMessageCombat() {
/* 1757 */     return (this.offensive || this.karmaSpell || this.healing);
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
/*      */   public void enchantItem(Creature performer, Item target, byte enchantment, float power) {
/* 1771 */     ItemSpellEffects effs = target.getSpellEffects();
/* 1772 */     if (effs == null) {
/* 1773 */       effs = new ItemSpellEffects(target.getWurmId());
/*      */     }
/*      */     
/* 1776 */     SpellEffect eff = effs.getSpellEffect(enchantment);
/* 1777 */     if (eff == null) {
/*      */ 
/*      */       
/* 1780 */       eff = new SpellEffect(target.getWurmId(), enchantment, power, 20000000);
/* 1781 */       effs.addSpellEffect(eff);
/*      */       
/* 1783 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1784 */           .getName() + " " + getEffectdesc(), (byte)2);
/*      */       
/* 1786 */       Server.getInstance().broadCastAction(performer.getNameWithGenus() + " looks pleased.", performer, 5);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1791 */     else if (eff.getPower() > power) {
/*      */ 
/*      */       
/* 1794 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/*      */       
/* 1796 */       Server.getInstance().broadCastAction(performer.getNameWithGenus() + " frowns.", performer, 5);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1801 */       eff.improvePower(performer, power);
/*      */       
/* 1803 */       performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + getName() + ".", (byte)2);
/*      */       
/* 1805 */       Server.getInstance().broadCastAction(performer.getNameWithGenus() + " looks pleased.", performer, 5);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean mayArmourBeEnchanted(Item target, @Nullable Creature performer, byte enchantment) {
/* 1812 */     if (!mayBeEnchanted(target)) {
/*      */       
/* 1814 */       if (performer != null)
/* 1815 */         performer.getCommunicator().sendNormalServerMessage("The spell will not work on that."); 
/* 1816 */       return false;
/*      */     } 
/* 1818 */     if (enchantment != 17 && target.getSpellPainShare() > 0.0F) {
/*      */       
/* 1820 */       if (performer != null)
/* 1821 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1822 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1823 */       return false;
/*      */     } 
/* 1825 */     if (enchantment != 46 && target.getSpellSlowdown() > 0.0F) {
/*      */       
/* 1827 */       if (performer != null)
/* 1828 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1829 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1830 */       return false;
/*      */     } 
/* 1832 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean mayReceiveSkillgainBuff(Item target, @Nullable Creature performer, byte enchantment) {
/* 1837 */     if (!mayBeEnchanted(target)) {
/*      */       
/* 1839 */       if (performer != null)
/* 1840 */         performer.getCommunicator().sendNormalServerMessage("The spell will not work on that."); 
/* 1841 */       return false;
/*      */     } 
/* 1843 */     if (enchantment != 47) {
/*      */       
/* 1845 */       if (target.getBonusForSpellEffect((byte)47) > 0.0F)
/*      */       {
/* 1847 */         if (performer != null)
/* 1848 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1849 */               .getName() + " is already enchanted with something that would negate the effect."); 
/* 1850 */         return false;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1855 */     else if (target.getBonusForSpellEffect((byte)13) > 0.0F || target
/* 1856 */       .getBonusForSpellEffect((byte)16) > 0.0F) {
/*      */       
/* 1858 */       if (performer != null)
/* 1859 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1860 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1861 */       return false;
/*      */     } 
/*      */     
/* 1864 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean mayWeaponBeEnchanted(Item target, @Nullable Creature performer, byte enchantment) {
/* 1869 */     if (!mayBeEnchanted(target)) {
/*      */       
/* 1871 */       if (performer != null)
/* 1872 */         performer.getCommunicator().sendNormalServerMessage("The spell will not work on that."); 
/* 1873 */       return false;
/*      */     } 
/* 1875 */     if (enchantment != 18 && target.getSpellRotModifier() > 0.0F) {
/*      */       
/* 1877 */       if (performer != null)
/* 1878 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1879 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1880 */       return false;
/*      */     } 
/* 1882 */     if (enchantment != 26 && target.getSpellLifeTransferModifier() > 0.0F) {
/*      */       
/* 1884 */       if (performer != null)
/* 1885 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1886 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1887 */       return false;
/*      */     } 
/* 1889 */     if (enchantment != 27 && target.getSpellVenomBonus() > 0.0F) {
/*      */       
/* 1891 */       if (performer != null)
/* 1892 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1893 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1894 */       return false;
/*      */     } 
/* 1896 */     if (enchantment != 33 && target.getSpellFrostDamageBonus() > 0.0F) {
/*      */       
/* 1898 */       if (performer != null)
/* 1899 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1900 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1901 */       return false;
/*      */     } 
/* 1903 */     if (enchantment != 45 && target.getSpellExtraDamageBonus() > 0.0F) {
/*      */       
/* 1905 */       if (performer != null)
/* 1906 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1907 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1908 */       return false;
/*      */     } 
/* 1910 */     if (enchantment != 14 && target.getSpellDamageBonus() > 0.0F) {
/*      */       
/* 1912 */       if (performer != null)
/* 1913 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1914 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1915 */       return false;
/*      */     } 
/* 1917 */     if (enchantment != 63 && target.getSpellEssenceDrainModifier() > 0.0F) {
/*      */       
/* 1919 */       if (performer != null)
/* 1920 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1921 */             .getName() + " is already enchanted with something that would negate the effect."); 
/* 1922 */       return false;
/*      */     } 
/* 1924 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {}
/*      */ 
/*      */ 
/*      */   
/*      */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Creature target) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void castSpell(double power, Creature performer, Item target) {
/* 1938 */     if (precondition(performer.getMindLogical(), performer, target)) {
/* 1939 */       doEffect(performer.getMindLogical(), power, performer, target);
/*      */     }
/*      */   }
/*      */   
/*      */   public void castSpell(double power, Creature performer, Creature target) {
/* 1944 */     if (precondition(performer.getMindLogical(), performer, target)) {
/* 1945 */       doEffect(performer.getMindLogical(), power, performer, target);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void castSpell(double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 1951 */     if (precondition(performer.getMindLogical(), performer, tilex, tiley, layer)) {
/* 1952 */       doEffect(performer.getMindLogical(), power, performer, tilex, tiley, layer, heightOffset);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {}
/*      */ 
/*      */ 
/*      */   
/*      */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Item target) {}
/*      */ 
/*      */ 
/*      */   
/*      */   void doEffect(Skill castSkill, double power, Creature performer, Wound target) {}
/*      */ 
/*      */ 
/*      */   
/*      */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset, Tiles.TileBorderDirection dir) {}
/*      */ 
/*      */ 
/*      */   
/*      */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {}
/*      */ 
/*      */ 
/*      */   
/*      */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 1979 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 1984 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean postcondition(Skill castSkill, Creature performer, Item target, double effect) {
/* 1989 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean precondition(Skill castSkill, Creature performer, Wound target) {
/* 1994 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/* 1999 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer, int heightOffset, Tiles.TileBorderDirection dir) {
/* 2005 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int getNumber() {
/* 2015 */     return this.number;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/* 2025 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int getCastingTime(Creature performer) {
/* 2035 */     SpellEffects effs = performer.getSpellEffects();
/* 2036 */     if (effs != null) {
/*      */       
/* 2038 */       SpellEffect eff = effs.getSpellEffect((byte)93);
/* 2039 */       if (eff != null)
/* 2040 */         return (int)(this.castingTime * (1.0F + Math.max(30.0F, eff.getPower()) / 100.0F)); 
/*      */     } 
/* 2042 */     return this.castingTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isReligious() {
/* 2052 */     return this.religious;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isKarmaSpell() {
/* 2062 */     return this.karmaSpell;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isOffensive() {
/* 2072 */     return this.offensive;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCreatureItemEnchantment() {
/* 2077 */     return (isTargetCreature() && isTargetAnyItem() && getEnchantment() != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isItemEnchantment() {
/* 2082 */     return (isTargetAnyItem() && getEnchantment() != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getCost() {
/* 2091 */     return this.cost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCost(Creature creature) {
/* 2101 */     return this.cost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCost(Item item) {
/* 2111 */     return this.cost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCost(Wound wound) {
/* 2121 */     return getCost();
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
/*      */   public int getCost(int tilexborder, int tileyborder, int layer, int heightOffset, Tiles.TileBorderDirection dir) {
/* 2135 */     return getCost();
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
/*      */   public int getCost(int tilex, int tiley, int layer, int heightOffset) {
/* 2148 */     return getCost();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDynamicCost() {
/* 2157 */     return this.hasDynamicCost;
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
/*      */   public final int getDifficulty(boolean forItem) {
/* 2184 */     if (forItem && isCreatureItemEnchantment())
/* 2185 */       return this.difficulty * 2; 
/* 2186 */     return this.difficulty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDescription() {
/* 2196 */     return this.description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int getLevel() {
/* 2206 */     return this.level;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final Logger getLogger() {
/* 2216 */     return logger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetCreature() {
/* 2226 */     return this.targetCreature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetItem() {
/* 2236 */     return this.targetItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetAnyItem() {
/* 2246 */     return (this.targetItem || this.targetWeapon || this.targetArmour || this.targetJewelry || this.targetPendulum);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetWound() {
/* 2256 */     return this.targetWound;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetTile() {
/* 2266 */     return this.targetTile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetTileBorder() {
/* 2276 */     return this.targetTileBorder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetWeapon() {
/* 2286 */     return this.targetWeapon;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetArmour() {
/* 2296 */     return this.targetArmour;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetJewelry() {
/* 2306 */     return this.targetJewelry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTargetPendulum() {
/* 2316 */     return this.targetPendulum;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isDominate() {
/* 2326 */     return this.dominate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getEnchantment() {
/* 2336 */     return this.enchantment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final String getEffectdesc() {
/* 2346 */     return this.effectdesc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isChaplain() {
/* 2356 */     return (this.type == 1 || this.type == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isReligiousSpell() {
/* 2365 */     return this.religious;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSorcerySpell() {
/* 2374 */     return this.karmaSpell;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float getMaterialShatterMod(byte material) {
/* 2385 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/* 2387 */       switch (material) {
/*      */         
/*      */         case 56:
/* 2390 */           return 0.15F;
/*      */         case 57:
/* 2392 */           return 0.25F;
/*      */         case 7:
/* 2394 */           return 0.2F;
/*      */         case 67:
/* 2396 */           return 1.0F;
/*      */         case 8:
/* 2398 */           return 0.1F;
/*      */         case 96:
/* 2400 */           return 0.15F;
/*      */       } 
/*      */ 
/*      */ 
/*      */     
/* 2405 */     } else if (material == 67) {
/* 2406 */       return 1.0F;
/*      */     } 
/* 2408 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   void checkDestroyItem(double power, Creature performer, Item target) {
/* 2413 */     if (Server.rand.nextFloat() < getMaterialShatterMod(target.getMaterial())) {
/*      */       return;
/*      */     }
/* 2416 */     ItemSpellEffects spellEffects = target.getSpellEffects();
/* 2417 */     float chanceModifier = 1.0F;
/* 2418 */     if (spellEffects != null)
/*      */     {
/* 2420 */       chanceModifier = spellEffects.getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SHATTERRES);
/*      */     }
/* 2422 */     if (power < -(target.getQualityLevel() * chanceModifier) || (power < 0.0D && Server.rand.nextFloat() <= 0.01D / chanceModifier)) {
/*      */       
/* 2424 */       if (spellEffects != null) {
/*      */         
/* 2426 */         SpellEffect eff = spellEffects.getSpellEffect((byte)98);
/* 2427 */         if (eff != null) {
/*      */           
/* 2429 */           spellEffects.removeSpellEffect((byte)98);
/* 2430 */           performer.getCommunicator().sendAlertServerMessage("The " + target
/* 2431 */               .getName() + " emits a strong deep sound of resonance and starts to shatter, but the Metallic Liquid protects the " + target
/* 2432 */               .getName() + "! The Metallic Liquid has dissipated.", (byte)3);
/*      */           
/* 2434 */           Server.getInstance().broadCastAction("The " + target.getName() + " starts to shatter, but gets protected by a mystic substance!", performer, 5);
/*      */           return;
/*      */         } 
/*      */       } 
/* 2438 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2439 */           .getName() + " emits a strong deep sound of resonance, then shatters!", (byte)3);
/*      */       
/* 2441 */       Server.getInstance().broadCastAction("The " + target.getName() + " shatters!", performer, 5);
/* 2442 */       Items.destroyItem(target.getWurmId());
/*      */       
/* 2444 */       performer.achievement(627);
/*      */ 
/*      */     
/*      */     }
/* 2448 */     else if (power < (-(target.getQualityLevel() * chanceModifier) / 3.0F)) {
/*      */       
/* 2450 */       if (spellEffects != null) {
/*      */         
/* 2452 */         SpellEffect eff = spellEffects.getSpellEffect((byte)98);
/* 2453 */         if (eff != null) {
/*      */           
/* 2455 */           eff.setPower(eff.getPower() - 20.0F);
/* 2456 */           performer
/* 2457 */             .getCommunicator()
/* 2458 */             .sendNormalServerMessage("The " + target
/* 2459 */               .getName() + " emits a deep worrying sound of resonance, and a small crack wants to start forming, but the Metallic Liquid steps in and takes the damage instead!");
/*      */ 
/*      */           
/* 2462 */           if (eff.getPower() <= 0.0F) {
/*      */             
/* 2464 */             performer
/* 2465 */               .getCommunicator()
/* 2466 */               .sendAlertServerMessage("The Metallic Liquid's strength has been depleted, and its protection has been removed from the " + target
/* 2467 */                 .getName());
/* 2468 */             spellEffects.removeSpellEffect((byte)98);
/*      */           } 
/* 2470 */           Server.getInstance().broadCastAction("The " + target.getName() + " starts to form cracks, but a mystic liquid protects it!", performer, 5);
/*      */           return;
/*      */         } 
/*      */       } 
/* 2474 */       target.setDamage(target.getDamage() + (float)Math.abs(power / 20.0D));
/* 2475 */       performer
/* 2476 */         .getCommunicator()
/* 2477 */         .sendNormalServerMessage("The " + target
/*      */           
/* 2479 */           .getName() + " emits a deep worrying sound of resonance, and a small crack starts to form on the surface.");
/*      */     }
/*      */     else {
/*      */       
/* 2483 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2484 */           .getName() + " emits a deep worrying sound of resonance, but stays intact.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final List<Long> findBridgesInTheArea(int sx, int sy, int ex, int ey, int layer, int heightOffset, int groundHeight) {
/* 2491 */     int actualHeight = groundHeight + heightOffset;
/* 2492 */     List<Long> arr = new ArrayList<>();
/* 2493 */     for (int x = sx; x <= ex; x++) {
/*      */       
/* 2495 */       for (int y = sy; y <= ey; y++) {
/*      */         
/* 2497 */         if (x == 266 && y == 303) {
/*      */           
/* 2499 */           boolean f = false;
/* 2500 */           boolean bool1 = f;
/*      */         } 
/* 2502 */         VolaTile tile = Zones.getOrCreateTile(x, y, (layer >= 0));
/* 2503 */         if (tile != null) {
/*      */ 
/*      */           
/* 2506 */           Structure structure = tile.getStructure();
/* 2507 */           if (structure != null && structure.isTypeBridge()) {
/*      */ 
/*      */             
/* 2510 */             float[] hts = Zones.getNodeHeights(x, y, layer, structure.getWurmId());
/*      */             
/* 2512 */             float h = hts[0] * 0.5F * 0.5F + hts[1] * 0.5F * 0.5F + hts[2] * 0.5F * 0.5F + hts[3] * 0.5F * 0.5F;
/*      */             
/* 2514 */             int closestHeight = -1000;
/* 2515 */             int smallestDiff = 110;
/* 2516 */             for (int i = 0; i < hts.length; i++) {
/*      */               
/* 2518 */               int dec = (int)(hts[i] * 10.0F);
/* 2519 */               int diff = Math.abs(actualHeight - dec);
/* 2520 */               if (diff < smallestDiff) {
/*      */                 
/* 2522 */                 smallestDiff = diff;
/* 2523 */                 closestHeight = dec;
/*      */               } 
/*      */             } 
/* 2526 */             if (closestHeight > -1000 && smallestDiff <= 5) {
/*      */               
/* 2528 */               Long id = Long.valueOf(structure.getWurmId());
/* 2529 */               if (!arr.contains(id)) {
/* 2530 */                 arr.add(id);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 2537 */     return arr;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void calculateAOE(int sx, int sy, int ex, int ey, int tilex, int tiley, int layer, Structure playerStructure, Structure targetStructure, int heightOffset) {
/* 2543 */     this.area = new boolean[1 + ex - sx][1 + ey - sy];
/* 2544 */     this.offsets = new int[1 + ex - sx][1 + ey - sy];
/* 2545 */     int groundHeight = 0;
/* 2546 */     if (targetStructure == null || targetStructure.isTypeHouse()) {
/*      */       
/* 2548 */       float[] hts = Zones.getNodeHeights(tilex, tiley, layer, -10L);
/*      */       
/* 2550 */       float h = hts[0] * 0.5F * 0.5F + hts[1] * 0.5F * 0.5F + hts[2] * 0.5F * 0.5F + hts[3] * 0.5F * 0.5F;
/*      */       
/* 2552 */       groundHeight = (int)(h * 10.0F);
/*      */     } 
/* 2554 */     List<Long> bridges = findBridgesInTheArea(sx, sy, ex, ey, layer, heightOffset, groundHeight);
/*      */     
/* 2556 */     for (int x = sx; x <= ex; x++) {
/*      */       
/* 2558 */       for (int y = sy; y <= ey; y++) {
/*      */ 
/*      */         
/* 2561 */         Item ring = Zones.isWithinDuelRing(x, y, (layer >= 0));
/* 2562 */         if (ring == null) {
/*      */ 
/*      */           
/* 2565 */           VolaTile tile = Zones.getOrCreateTile(x, y, (layer >= 0));
/* 2566 */           if (tile != null) {
/*      */ 
/*      */ 
/*      */             
/* 2570 */             Structure tileStructure = tile.getStructure();
/* 2571 */             int currAreaX = x - sx;
/* 2572 */             int currAreaY = y - sy;
/*      */             
/* 2574 */             if (layer < 0) {
/*      */               
/* 2576 */               int ttile = Server.caveMesh.getTile(x, y);
/* 2577 */               byte ttype = Tiles.decodeType(ttile);
/*      */               
/* 2579 */               if (Tiles.decodeHeight(ttile) < 0)
/*      */               {
/* 2581 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/* 2583 */               else if (Tiles.isSolidCave(ttype))
/*      */               {
/* 2585 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/*      */               else
/*      */               {
/* 2589 */                 if (x > tilex + 1) {
/*      */                   
/* 2591 */                   if (this.area[currAreaX - 1][currAreaY]) {
/* 2592 */                     this.area[currAreaX][currAreaY] = true;
/*      */                   }
/* 2594 */                 } else if (x < tilex - 1) {
/*      */                   
/* 2596 */                   if (this.area[currAreaX + 1][currAreaY])
/* 2597 */                     this.area[currAreaX][currAreaY] = true; 
/*      */                 } 
/* 2599 */                 if (y < tiley - 1) {
/*      */                   
/* 2601 */                   if (this.area[currAreaX][currAreaY + 1]) {
/* 2602 */                     this.area[currAreaX][currAreaY] = true;
/*      */                   }
/* 2604 */                 } else if (y > tiley + 1) {
/*      */                   
/* 2606 */                   if (this.area[currAreaX][currAreaY - 1]) {
/* 2607 */                     this.area[currAreaX][currAreaY] = true;
/*      */                   }
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 2614 */             else if (targetStructure != null && targetStructure.isTypeHouse()) {
/*      */ 
/*      */               
/* 2617 */               if (tileStructure != null && tileStructure.getWurmId() == targetStructure.getWurmId()) {
/*      */ 
/*      */                 
/* 2620 */                 boolean foundFloor = false;
/* 2621 */                 for (Floor floor : tile.getFloors()) {
/*      */                   
/* 2623 */                   if (floor.getHeightOffset() == heightOffset) {
/*      */                     
/* 2625 */                     foundFloor = true;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/* 2630 */                 if (!foundFloor)
/*      */                 {
/* 2632 */                   this.area[currAreaX][currAreaY] = true;
/*      */                 }
/*      */                 else
/*      */                 {
/* 2636 */                   this.offsets[currAreaX][currAreaY] = heightOffset + groundHeight;
/*      */                 }
/*      */               
/*      */               }
/* 2640 */               else if (tileStructure != null && tileStructure.isTypeBridge()) {
/*      */                 
/* 2642 */                 Long bridgeId = Long.valueOf(tileStructure.getWurmId());
/* 2643 */                 if (!bridges.contains(bridgeId)) {
/*      */                   
/* 2645 */                   this.area[currAreaX][currAreaY] = true;
/*      */                 }
/*      */                 else {
/*      */                   
/* 2649 */                   for (BridgePart bp : tileStructure.getBridgeParts()) {
/*      */                     
/* 2651 */                     if (bp.getTileX() == x && bp.getTileY() == y) {
/*      */                       
/* 2653 */                       this.offsets[currAreaX][currAreaY] = bp.getHeightOffset();
/*      */ 
/*      */ 
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } else {
/* 2662 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/*      */             
/*      */             }
/* 2666 */             else if (targetStructure != null && targetStructure.isTypeBridge()) {
/*      */               
/* 2668 */               if (tileStructure != null && tileStructure.isTypeHouse())
/*      */               {
/* 2670 */                 boolean foundConnection = false;
/* 2671 */                 for (int xx = x - 1; xx <= x + 1; xx++) {
/*      */                   
/* 2673 */                   for (int yy = y - 1; yy <= y + 1; yy++) {
/*      */                     
/* 2675 */                     if (yy != y || xx != x) {
/*      */ 
/*      */ 
/*      */                       
/* 2679 */                       VolaTile t = Zones.getOrCreateTile(xx, yy, (layer >= 0));
/* 2680 */                       if (t != null) {
/*      */ 
/*      */ 
/*      */                         
/* 2684 */                         Structure s = t.getStructure();
/* 2685 */                         if (s != null && s.getWurmId() == targetStructure.getWurmId()) {
/*      */                           
/* 2687 */                           foundConnection = true;
/* 2688 */                           int bridgeH = 0;
/* 2689 */                           for (BridgePart part : targetStructure.getBridgeParts()) {
/*      */                             
/* 2691 */                             if (part.getTileX() == xx && part.getTileY() == yy) {
/*      */                               
/* 2693 */                               bridgeH = part.getHeightOffset();
/*      */                               
/*      */                               break;
/*      */                             } 
/*      */                           } 
/* 2698 */                           float[] hts = Zones.getNodeHeights(x, y, layer, -10L);
/*      */                           
/* 2700 */                           float h = hts[0] * 0.5F * 0.5F + hts[1] * 0.5F * 0.5F + hts[2] * 0.5F * 0.5F + hts[3] * 0.5F * 0.5F;
/*      */ 
/*      */                           
/* 2703 */                           int gh = (int)(h * 10.0F);
/* 2704 */                           int closestHeight = -1000;
/* 2705 */                           int smallestDiff = 110;
/*      */                           
/* 2707 */                           for (Floor floor : tile.getFloors()) {
/*      */                             
/* 2709 */                             int fh = gh + floor.getFloorLevel() * 30;
/* 2710 */                             if (Math.abs(fh - bridgeH) < smallestDiff) {
/*      */                               
/* 2712 */                               smallestDiff = Math.abs(fh - bridgeH);
/* 2713 */                               closestHeight = fh;
/*      */                             } 
/*      */                           } 
/*      */                           
/* 2717 */                           this.offsets[currAreaX][currAreaY] = closestHeight; break;
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*      */                   } 
/* 2722 */                   if (foundConnection) {
/*      */                     break;
/*      */                   }
/*      */                 } 
/* 2726 */                 if (!foundConnection)
/*      */                 {
/* 2728 */                   this.area[currAreaX][currAreaY] = true;
/*      */                 }
/*      */               }
/* 2731 */               else if (tileStructure != null && tileStructure.isTypeBridge())
/*      */               {
/*      */                 
/* 2734 */                 if (tileStructure.getWurmId() != targetStructure.getWurmId()) {
/*      */                   
/* 2736 */                   Long id = Long.valueOf(tileStructure.getWurmId());
/* 2737 */                   if (!bridges.contains(id))
/*      */                   {
/* 2739 */                     BridgePart part = null;
/* 2740 */                     for (BridgePart bp : tileStructure.getBridgeParts()) {
/*      */                       
/* 2742 */                       if (bp.getTileX() == x && bp.getTileY() == y) {
/*      */                         
/* 2744 */                         part = bp;
/*      */                         break;
/*      */                       } 
/*      */                     } 
/* 2748 */                     this.area[currAreaX][currAreaY] = true;
/*      */                     
/* 2750 */                     float[] hts = Zones.getNodeHeights(x, y, layer, -10L);
/*      */                     
/* 2752 */                     float h = hts[0] * 0.5F * 0.5F + hts[1] * 0.5F * 0.5F + hts[2] * 0.5F * 0.5F + hts[3] * 0.5F * 0.5F;
/*      */ 
/*      */                     
/* 2755 */                     groundHeight = (int)(h * 10.0F);
/*      */                     
/* 2757 */                     if (Math.abs(groundHeight - heightOffset) < 25)
/*      */                     {
/* 2759 */                       this.offsets[currAreaX][currAreaY] = heightOffset;
/* 2760 */                       this.area[currAreaX][currAreaY] = false;
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 
/*      */                 } else {
/*      */                   
/* 2767 */                   BridgePart part = null;
/* 2768 */                   for (BridgePart bp : tileStructure.getBridgeParts()) {
/*      */                     
/* 2770 */                     if (bp.getTileX() == x && bp.getTileY() == y) {
/*      */                       
/* 2772 */                       part = bp;
/*      */                       break;
/*      */                     } 
/*      */                   } 
/* 2776 */                   if (part != null) {
/* 2777 */                     this.offsets[currAreaX][currAreaY] = part.getHeightOffset();
/*      */                   }
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 2784 */             else if (tileStructure != null) {
/*      */               
/* 2786 */               if (tileStructure.isTypeBridge()) {
/*      */                 
/* 2788 */                 BridgePart part = null;
/* 2789 */                 for (BridgePart p : tileStructure.getBridgeParts()) {
/*      */                   
/* 2791 */                   if (p.getTileX() == x && p.getTileY() == y) {
/*      */                     
/* 2793 */                     part = p;
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/* 2798 */                 if (part != null)
/*      */                 {
/* 2800 */                   if (Math.abs(part.getHeightOffset() - groundHeight) > 25)
/*      */                   {
/* 2802 */                     this.area[currAreaX][currAreaY] = true;
/*      */                   }
/*      */                 }
/*      */               }
/*      */               else {
/*      */                 
/* 2808 */                 this.area[currAreaX][currAreaY] = true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void calculateArea(int sx, int sy, int ex, int ey, int tilex, int tiley, int layer, Structure currstr) {
/* 2822 */     this.area = new boolean[1 + ex - sx][1 + ey - sy];
/*      */     
/*      */     int x;
/*      */     
/* 2826 */     for (x = sx; x <= ex; x++) {
/*      */       
/* 2828 */       for (int y = sy; y <= ey; y++) {
/*      */         
/* 2830 */         Item ring = Zones.isWithinDuelRing(x, y, (layer > 0));
/* 2831 */         if (ring == null) {
/*      */           Structure toCheck;
/* 2833 */           VolaTile t = Zones.getTileOrNull(x, y, (layer >= 0));
/* 2834 */           if (t != null) {
/*      */             
/* 2836 */             toCheck = t.getStructure();
/* 2837 */             if (toCheck != null && (!toCheck.isFinalFinished() || !toCheck.isFinished())) {
/* 2838 */               toCheck = null;
/*      */             }
/*      */           } else {
/* 2841 */             toCheck = null;
/*      */           } 
/* 2843 */           int currAreaX = x - sx;
/* 2844 */           int currAreaY = y - sy;
/*      */ 
/*      */           
/* 2847 */           if (currstr == toCheck) {
/*      */             
/* 2849 */             if (layer < 0) {
/*      */               
/* 2851 */               int ttile = Server.caveMesh.getTile(x, y);
/* 2852 */               byte ttype = Tiles.decodeType(ttile);
/*      */               
/* 2854 */               if (Tiles.decodeHeight(ttile) < 0) {
/*      */                 
/* 2856 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/* 2858 */               else if (Tiles.isSolidCave(ttype)) {
/*      */                 
/* 2860 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/*      */               else {
/*      */                 
/* 2864 */                 if (x > tilex + 1) {
/*      */                   
/* 2866 */                   if (this.area[currAreaX - 1][currAreaY]) {
/* 2867 */                     this.area[currAreaX][currAreaY] = true;
/*      */                   }
/* 2869 */                 } else if (x < tilex - 1) {
/*      */                   
/* 2871 */                   if (this.area[currAreaX + 1][currAreaY])
/* 2872 */                     this.area[currAreaX][currAreaY] = true; 
/*      */                 } 
/* 2874 */                 if (y < tiley - 1) {
/*      */                   
/* 2876 */                   if (this.area[currAreaX][currAreaY + 1]) {
/* 2877 */                     this.area[currAreaX][currAreaY] = true;
/*      */                   }
/* 2879 */                 } else if (y > tiley + 1) {
/*      */                   
/* 2881 */                   if (this.area[currAreaX][currAreaY - 1]) {
/* 2882 */                     this.area[currAreaX][currAreaY] = true;
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } else {
/* 2888 */             this.area[currAreaX][currAreaY] = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2897 */     if (layer < 0)
/*      */     {
/* 2899 */       for (x = sx; x <= ex; x++) {
/*      */         
/* 2901 */         for (int y = sy; y <= ey; y++) {
/*      */           
/* 2903 */           int currAreaX = x - sx;
/* 2904 */           int currAreaY = y - sy;
/* 2905 */           int ttile = Server.caveMesh.getTile(x, y);
/* 2906 */           byte ttype = Tiles.decodeType(ttile);
/*      */           
/* 2908 */           if (Tiles.decodeHeight(ttile) < 0) {
/*      */             
/* 2910 */             this.area[currAreaX][currAreaY] = true;
/*      */           }
/* 2912 */           else if (Tiles.isSolidCave(ttype)) {
/*      */             
/* 2914 */             this.area[currAreaX][currAreaY] = true;
/*      */           }
/*      */           else {
/*      */             
/* 2918 */             if (x > tilex + 1) {
/*      */               
/* 2920 */               if (this.area[currAreaX - 1][currAreaY]) {
/* 2921 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/* 2923 */             } else if (x < tilex - 1) {
/*      */               
/* 2925 */               if (this.area[currAreaX + 1][currAreaY])
/* 2926 */                 this.area[currAreaX][currAreaY] = true; 
/*      */             } 
/* 2928 */             if (y < tiley - 1) {
/*      */               
/* 2930 */               if (this.area[currAreaX][currAreaY + 1]) {
/* 2931 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/* 2933 */             } else if (y > tiley + 1) {
/*      */               
/* 2935 */               if (this.area[currAreaX][currAreaY - 1]) {
/* 2936 */                 this.area[currAreaX][currAreaY] = true;
/*      */               }
/*      */             } 
/*      */           } 
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
/*      */   final boolean isSpellBlocked(int deityId, int blockingSpellNum) {
/* 2952 */     Random rand2 = new Random((deityId + this.number * 1071));
/* 2953 */     if (rand2.nextInt(3) == 0) {
/*      */       
/* 2955 */       Random rand = new Random((deityId + blockingSpellNum * 1071));
/* 2956 */       if (rand.nextInt(3) == 0)
/* 2957 */         return true; 
/*      */     } 
/* 2959 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean deityCanHaveSpell(int deityId) {
/* 2967 */     Random rand = new Random((deityId + this.number * 1071));
/* 2968 */     return (rand.nextInt(3) == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean hateEnchantPrecondition(Item target, Creature performer) {
/* 2973 */     if (!mayBeEnchanted(target)) {
/*      */       
/* 2975 */       performer.getCommunicator().sendNormalServerMessage("The spell will not work on that.", (byte)3);
/*      */       
/* 2977 */       return false;
/*      */     } 
/* 2979 */     if (target.enchantment != 0) {
/*      */       
/* 2981 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already enchanted.", (byte)3);
/*      */       
/* 2983 */       return false;
/*      */     } 
/* 2985 */     if (target.getCurrentQualityLevel() < 70.0F) {
/*      */       
/* 2987 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2988 */           .getName() + " is of too low quality for this enchantment.", (byte)3);
/* 2989 */       return false;
/*      */     } 
/* 2991 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Spell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */