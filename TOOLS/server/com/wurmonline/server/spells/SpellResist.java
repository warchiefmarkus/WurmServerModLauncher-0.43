/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpellResist
/*     */   implements TimeConstants
/*     */ {
/*  23 */   protected static Logger logger = Logger.getLogger(SpellResist.class.getName());
/*     */   
/*  25 */   protected static ArrayList<Creature> resistingCreatures = new ArrayList<>();
/*     */   
/*     */   public Creature creature;
/*     */   public long lastUpdated;
/*     */   public double currentResistance;
/*     */   public long fullyExpires;
/*  31 */   public SpellEffectsEnum spellEffect = SpellEffectsEnum.RES_HEAL;
/*  32 */   public double recoveryPerSecond = 0.016666666666666666D; protected static final int GROUP_HEALING = 0; protected static final int GROUP_DRAIN_HEALTH = 1; protected static final int GROUP_FUNGUS_TRAP = 2; protected static final int GROUP_ICE_PILLAR = 3; protected static final int GROUP_FIRE_PILLAR = 4; protected static final int GROUP_TENTACLES = 5; protected static final int GROUP_PAIN_RAIN = 6; protected static final int GROUP_ROTTING_GUT = 7;
/*     */   protected static final int GROUP_TORNADO = 8;
/*     */   
/*     */   public SpellResist(Creature creature) {
/*  36 */     this.creature = creature;
/*  37 */     this.lastUpdated = System.currentTimeMillis();
/*  38 */     this.currentResistance = 1.0D;
/*  39 */     this.fullyExpires = this.lastUpdated;
/*     */   }
/*     */   protected static final int GROUP_HEAVY_NUKE = 9; protected static final int GROUP_FIREHEART = 10; protected static final int GROUP_SHARDOFICE = 11; protected static final int GROUP_SCORN_OF_LIBILA = 12; protected static final int GROUP_HUMID_DRIZZLE = 13; protected static final int GROUP_SMITE = 14; protected static final int GROUP_LOCATE = 15; protected static final int GROUP_WRATH_MAGRANON = 16; protected static final int GROUP_DISPEL = 17; protected static final double HEALING_RECOVERY_SECOND = 8.0E-4D;
/*     */   
/*     */   public double scalePower(double power) {
/*  44 */     return power;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class HealingResist
/*     */     extends SpellResist
/*     */   {
/*     */     public HealingResist(Creature creature) {
/*  56 */       super(creature);
/*  57 */       this.spellEffect = SpellEffectsEnum.RES_HEAL;
/*  58 */       this.recoveryPerSecond = 8.0E-4D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/*  66 */       return power / 131070.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DrainHealthResist
/*     */     extends SpellResist
/*     */   {
/*     */     public DrainHealthResist(Creature creature) {
/*  74 */       super(creature);
/*  75 */       this.spellEffect = SpellEffectsEnum.RES_DRAINHEALTH;
/*  76 */       this.recoveryPerSecond = 0.0033333333333333335D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/*  84 */       double maxDamage = 4000.0D;
/*  85 */       double castPower = power * 100.0D / maxDamage;
/*  86 */       return castPower * this.recoveryPerSecond;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FungusTrapResist
/*     */     extends SpellResist
/*     */   {
/*     */     public FungusTrapResist(Creature creature) {
/*  94 */       super(creature);
/*  95 */       this.spellEffect = SpellEffectsEnum.RES_FUNGUSTRAP;
/*  96 */       this.recoveryPerSecond = 0.005555555555555556D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 104 */       double maxDamage = 550.0D;
/* 105 */       double castPower = power * 100.0D / maxDamage;
/* 106 */       return castPower * this.recoveryPerSecond * 0.05D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IcePillarResist
/*     */     extends SpellResist
/*     */   {
/*     */     public IcePillarResist(Creature creature) {
/* 114 */       super(creature);
/* 115 */       this.spellEffect = SpellEffectsEnum.RES_ICEPILLAR;
/* 116 */       this.recoveryPerSecond = 0.005555555555555556D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 124 */       double maxDamage = 550.0D;
/* 125 */       double castPower = power * 100.0D / maxDamage;
/* 126 */       return castPower * this.recoveryPerSecond * 0.05D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FirePillarResist
/*     */     extends SpellResist
/*     */   {
/*     */     public FirePillarResist(Creature creature) {
/* 134 */       super(creature);
/* 135 */       this.spellEffect = SpellEffectsEnum.RES_FIREPILLAR;
/* 136 */       this.recoveryPerSecond = 0.005555555555555556D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 144 */       double maxDamage = 575.0D;
/* 145 */       double castPower = power * 100.0D / maxDamage;
/* 146 */       return castPower * this.recoveryPerSecond * 0.05D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TentaclesResist
/*     */     extends SpellResist
/*     */   {
/*     */     public TentaclesResist(Creature creature) {
/* 154 */       super(creature);
/* 155 */       this.spellEffect = SpellEffectsEnum.RES_TENTACLES;
/* 156 */       this.recoveryPerSecond = 0.005555555555555556D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 164 */       double maxDamage = 500.0D;
/* 165 */       double castPower = power * 100.0D / maxDamage;
/* 166 */       return castPower * this.recoveryPerSecond * 0.05D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PainRainResist
/*     */     extends SpellResist
/*     */   {
/*     */     public PainRainResist(Creature creature) {
/* 174 */       super(creature);
/* 175 */       this.spellEffect = SpellEffectsEnum.RES_PAINRAIN;
/* 176 */       this.recoveryPerSecond = 0.004166666666666667D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 184 */       double maxDamage = 10000.0D;
/* 185 */       double castPower = power * 100.0D / maxDamage;
/* 186 */       return castPower * this.recoveryPerSecond * 2.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RottingGutResist
/*     */     extends SpellResist
/*     */   {
/*     */     public RottingGutResist(Creature creature) {
/* 194 */       super(creature);
/* 195 */       this.spellEffect = SpellEffectsEnum.RES_ROTTINGGUT;
/* 196 */       this.recoveryPerSecond = 0.006666666666666667D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 204 */       double maxDamage = 17000.0D;
/* 205 */       double castPower = power * 100.0D / maxDamage;
/* 206 */       return castPower * this.recoveryPerSecond * 1.5D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TornadoResist
/*     */     extends SpellResist
/*     */   {
/*     */     public TornadoResist(Creature creature) {
/* 214 */       super(creature);
/* 215 */       this.spellEffect = SpellEffectsEnum.RES_TORNADO;
/* 216 */       this.recoveryPerSecond = 0.005555555555555556D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 224 */       double maxDamage = 12000.0D;
/* 225 */       double castPower = power * 100.0D / maxDamage;
/* 226 */       return castPower * this.recoveryPerSecond;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HeavyNukeResist
/*     */     extends SpellResist
/*     */   {
/*     */     public HeavyNukeResist(Creature creature) {
/* 234 */       super(creature);
/* 235 */       this.spellEffect = SpellEffectsEnum.RES_SHARED;
/* 236 */       this.recoveryPerSecond = 0.0033333333333333335D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 244 */       double maxDamage = 29500.0D;
/* 245 */       double castPower = power * 100.0D / maxDamage;
/* 246 */       return castPower * this.recoveryPerSecond * 3.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FireHeartResist
/*     */     extends SpellResist
/*     */   {
/*     */     public FireHeartResist(Creature creature) {
/* 254 */       super(creature);
/* 255 */       this.spellEffect = SpellEffectsEnum.RES_FIREHEART;
/* 256 */       this.recoveryPerSecond = 0.006666666666666667D;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 262 */       double maxDamage = 17000.0D;
/* 263 */       double castPower = power * 100.0D / maxDamage;
/* 264 */       return castPower * this.recoveryPerSecond * 1.5D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ShardOfIceResist
/*     */     extends SpellResist
/*     */   {
/*     */     public ShardOfIceResist(Creature creature) {
/* 272 */       super(creature);
/* 273 */       this.spellEffect = SpellEffectsEnum.RES_SHARDOFICE;
/* 274 */       this.recoveryPerSecond = 0.006666666666666667D;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 280 */       double maxDamage = 17000.0D;
/* 281 */       double castPower = power * 100.0D / maxDamage;
/* 282 */       return castPower * this.recoveryPerSecond * 1.5D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ScornOfLibilaResist
/*     */     extends SpellResist
/*     */   {
/*     */     public ScornOfLibilaResist(Creature creature) {
/* 290 */       super(creature);
/* 291 */       this.spellEffect = SpellEffectsEnum.RES_SCORNLIBILA;
/* 292 */       this.recoveryPerSecond = 0.0033333333333333335D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 300 */       double maxDamage = 8000.0D;
/* 301 */       double castPower = power * 100.0D / maxDamage;
/* 302 */       return castPower * this.recoveryPerSecond * 1.5D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HumidDrizzleResist
/*     */     extends SpellResist
/*     */   {
/*     */     public HumidDrizzleResist(Creature creature) {
/* 310 */       super(creature);
/* 311 */       this.spellEffect = null;
/* 312 */       this.recoveryPerSecond = 0.0011111111111111111D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 319 */       return 1.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SmiteResist
/*     */     extends SpellResist
/*     */   {
/*     */     public SmiteResist(Creature creature) {
/* 327 */       super(creature);
/* 328 */       this.spellEffect = SpellEffectsEnum.RES_SMITE;
/* 329 */       this.recoveryPerSecond = 0.0033333333333333335D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 337 */       return power / 65535.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LocateResist
/*     */     extends SpellResist
/*     */   {
/*     */     public LocateResist(Creature creature) {
/* 345 */       super(creature);
/* 346 */       this.spellEffect = null;
/* 347 */       this.recoveryPerSecond = 0.0033333333333333335D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 357 */       return power / 100.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WrathMagranonResist
/*     */     extends SpellResist
/*     */   {
/*     */     public WrathMagranonResist(Creature creature) {
/* 365 */       super(creature);
/* 366 */       this.spellEffect = SpellEffectsEnum.RES_WRATH_OF_MAGRANON;
/* 367 */       this.recoveryPerSecond = 0.0033333333333333335D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 374 */       double maxDamage = 9000.0D;
/* 375 */       double castPower = power * 100.0D / maxDamage;
/* 376 */       return castPower * this.recoveryPerSecond;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DispelResist
/*     */     extends SpellResist
/*     */   {
/*     */     public DispelResist(Creature creature) {
/* 384 */       super(creature);
/* 385 */       this.spellEffect = SpellEffectsEnum.RES_DISPEL;
/* 386 */       this.recoveryPerSecond = 0.0033333333333333335D;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double scalePower(double power) {
/* 393 */       return power * this.recoveryPerSecond;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SpellResist getNewResistanceForGroup(Creature creature, int group) {
/* 418 */     switch (group) {
/*     */       
/*     */       case 0:
/* 421 */         return new HealingResist(creature);
/*     */       case 1:
/* 423 */         return new DrainHealthResist(creature);
/*     */       case 2:
/* 425 */         return new FungusTrapResist(creature);
/*     */       case 3:
/* 427 */         return new IcePillarResist(creature);
/*     */       case 4:
/* 429 */         return new FirePillarResist(creature);
/*     */       case 5:
/* 431 */         return new TentaclesResist(creature);
/*     */       case 6:
/* 433 */         return new PainRainResist(creature);
/*     */       case 7:
/* 435 */         return new RottingGutResist(creature);
/*     */       case 8:
/* 437 */         return new TornadoResist(creature);
/*     */       case 9:
/* 439 */         return new HeavyNukeResist(creature);
/*     */       case 10:
/* 441 */         return new FireHeartResist(creature);
/*     */       case 11:
/* 443 */         return new ShardOfIceResist(creature);
/*     */       case 12:
/* 445 */         return new ScornOfLibilaResist(creature);
/*     */       case 13:
/* 447 */         return new HumidDrizzleResist(creature);
/*     */       case 14:
/* 449 */         return new SmiteResist(creature);
/*     */       case 15:
/* 451 */         return new LocateResist(creature);
/*     */       case 16:
/* 453 */         return new WrathMagranonResist(creature);
/*     */       case 17:
/* 455 */         return new DispelResist(creature);
/*     */     } 
/* 457 */     logger.warning(String.format("Could not find a proper SpellResist instance for resist group %s.", new Object[] { Integer.valueOf(group) }));
/* 458 */     return new HealingResist(creature);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getSpellGroup(int spellNumber) {
/* 463 */     switch (spellNumber) {
/*     */       
/*     */       case 246:
/*     */       case 247:
/*     */       case 248:
/*     */       case 249:
/*     */       case 408:
/*     */       case 409:
/*     */       case 438:
/* 472 */         return 0;
/*     */       
/*     */       case 255:
/* 475 */         return 1;
/*     */       
/*     */       case 433:
/* 478 */         return 2;
/*     */       
/*     */       case 414:
/* 481 */         return 3;
/*     */       
/*     */       case 420:
/* 484 */         return 4;
/*     */       
/*     */       case 418:
/* 487 */         return 5;
/*     */       
/*     */       case 432:
/* 490 */         return 6;
/*     */       
/*     */       case 428:
/* 493 */         return 7;
/*     */       
/*     */       case 413:
/* 496 */         return 8;
/*     */       
/*     */       case 430:
/*     */       case 931:
/*     */       case 932:
/* 501 */         return 9;
/*     */       
/*     */       case 424:
/* 504 */         return 10;
/*     */       
/*     */       case 485:
/* 507 */         return 11;
/*     */       
/*     */       case 448:
/* 510 */         return 12;
/*     */       
/*     */       case 407:
/* 513 */         return 13;
/*     */       
/*     */       case 252:
/* 516 */         return 14;
/*     */       
/*     */       case 419:
/*     */       case 451:
/* 520 */         return 15;
/*     */       
/*     */       case 441:
/* 523 */         return 16;
/*     */       
/*     */       case 450:
/* 526 */         return 17;
/*     */     } 
/* 528 */     logger.warning(String.format("Could not find a proper SpellResist group for spell number %s.", new Object[] { Integer.valueOf(spellNumber) }));
/* 529 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static double updateSpellResistance(Creature creature, SpellResist res, double additionalResistance) {
/* 552 */     long timeDelta = System.currentTimeMillis() - res.lastUpdated;
/* 553 */     double secondsPassed = timeDelta / 1000.0D;
/* 554 */     res.currentResistance = Math.min(1.0D, res.currentResistance + secondsPassed * res.recoveryPerSecond);
/* 555 */     res.currentResistance = Math.max(0.0D, res.currentResistance - additionalResistance);
/* 556 */     res.lastUpdated = System.currentTimeMillis();
/* 557 */     double secondsUntilFullyHealed = (1.0D - res.currentResistance) / res.recoveryPerSecond;
/* 558 */     res.fullyExpires = (long)(System.currentTimeMillis() + secondsUntilFullyHealed * 1000.0D);
/* 559 */     if (res.spellEffect != null) {
/*     */       
/* 561 */       creature.getCommunicator().sendAddStatusEffect(res.spellEffect, (int)secondsUntilFullyHealed);
/* 562 */       if (!resistingCreatures.contains(creature))
/*     */       {
/* 564 */         resistingCreatures.add(creature);
/*     */       }
/*     */     } 
/* 567 */     return res.currentResistance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getSpellResistance(Creature creature, int spellNumber) {
/* 579 */     HashMap<Integer, SpellResist> resistances = creature.getSpellResistances();
/* 580 */     int group = getSpellGroup(spellNumber);
/* 581 */     if (resistances.containsKey(Integer.valueOf(group))) {
/*     */       
/* 583 */       SpellResist res = resistances.get(Integer.valueOf(group));
/* 584 */       return updateSpellResistance(creature, res, 0.0D);
/*     */     } 
/* 586 */     return 1.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addSpellResistance(Creature creature, int spellNumber, double power) {
/* 597 */     HashMap<Integer, SpellResist> resistances = creature.getSpellResistances();
/* 598 */     int group = getSpellGroup(spellNumber);
/* 599 */     if (resistances.containsKey(Integer.valueOf(group))) {
/*     */       
/* 601 */       SpellResist res = resistances.get(Integer.valueOf(group));
/* 602 */       double reduction = res.scalePower(power);
/* 603 */       double castPower = 0.0D;
/* 604 */       if (group == 9)
/*     */       {
/* 606 */         switch (spellNumber) {
/*     */           
/*     */           case 430:
/* 609 */             castPower = power * 100.0D / 17500.0D + 12000.0D;
/* 610 */             reduction = castPower * res.recoveryPerSecond * 3.0D;
/*     */             break;
/*     */           case 931:
/* 613 */             castPower = power * 100.0D / 25000.0D + 7500.0D;
/* 614 */             reduction = castPower * res.recoveryPerSecond * 3.0D;
/*     */             break;
/*     */           case 932:
/* 617 */             castPower = power * 100.0D / 10000.0D + 25000.0D;
/* 618 */             reduction = castPower * res.recoveryPerSecond * 3.5D;
/*     */             break;
/*     */         } 
/*     */       }
/* 622 */       updateSpellResistance(creature, res, reduction);
/*     */     }
/*     */     else {
/*     */       
/* 626 */       SpellResist res = getNewResistanceForGroup(creature, group);
/* 627 */       double reduction = res.scalePower(power);
/* 628 */       updateSpellResistance(creature, res, reduction);
/* 629 */       resistances.put(Integer.valueOf(group), res);
/*     */     } 
/*     */   }
/*     */   
/* 633 */   public static long lastPolledSpellResist = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long pollSpellResistTime = 1000L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void onServerPoll() {
/* 645 */     long now = System.currentTimeMillis();
/* 646 */     if (lastPolledSpellResist + 1000L <= now) {
/*     */ 
/*     */       
/* 649 */       ArrayList<Creature> crets = new ArrayList<>(resistingCreatures);
/* 650 */       for (Creature cret : crets) {
/*     */         
/* 652 */         HashMap<Integer, SpellResist> resistances = cret.getSpellResistances();
/* 653 */         if (!resistances.isEmpty()) {
/*     */ 
/*     */ 
/*     */           
/* 657 */           Set<Integer> nums = new HashSet<>(resistances.keySet());
/* 658 */           for (Iterator<Integer> iterator = nums.iterator(); iterator.hasNext(); ) { int num = ((Integer)iterator.next()).intValue();
/*     */             
/* 660 */             SpellResist res = resistances.get(Integer.valueOf(num));
/* 661 */             if (res.fullyExpires <= System.currentTimeMillis()) {
/*     */               
/* 663 */               if (res.creature != null)
/*     */               {
/* 665 */                 res.creature.getCommunicator().sendRemoveSpellEffect(res.spellEffect);
/*     */               }
/* 667 */               resistances.remove(Integer.valueOf(num));
/*     */             }  }
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 674 */         resistingCreatures.remove(cret);
/*     */       } 
/*     */       
/* 677 */       lastPolledSpellResist = System.currentTimeMillis();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SpellResist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */