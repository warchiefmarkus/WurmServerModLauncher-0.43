/*      */ package com.wurmonline.server.bodys;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.MovementScheme;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.modifiers.DoubleValueModifier;
/*      */ import com.wurmonline.server.modifiers.ModifierTypes;
/*      */ import com.wurmonline.server.modifiers.ValueModifiedListener;
/*      */ import com.wurmonline.server.modifiers.ValueModifier;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.spells.EnchantUtil;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import java.util.HashSet;
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
/*      */ public abstract class Wound
/*      */   implements CounterTypes, TimeConstants, MiscConstants, ModifierTypes
/*      */ {
/*      */   private final byte location;
/*      */   private final long id;
/*      */   private byte type;
/*      */   float severity;
/*      */   private final long owner;
/*   63 */   float poisonSeverity = 0.0F;
/*   64 */   float infectionSeverity = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   69 */   private Set<ValueModifier> modifiers = null;
/*      */   
/*      */   public static final String severe = "severe";
/*      */   
/*      */   public static final String verylight = "very light";
/*      */   
/*      */   public static final String light = "light";
/*      */   
/*      */   public static final String medium = "medium";
/*      */   
/*      */   public static final String bad = "bad";
/*      */   public static final String bandaged = ", bandaged";
/*      */   public static final String applied = ", applied";
/*      */   public static final String treated = ", treated";
/*      */   boolean isBandaged = false;
/*   84 */   long lastPolled = 0L;
/*   85 */   byte healEff = 0;
/*      */   
/*      */   public static final byte TYPE_CRUSH = 0;
/*      */   
/*      */   public static final byte TYPE_SLASH = 1;
/*      */   
/*      */   public static final byte TYPE_PIERCE = 2;
/*      */   
/*      */   public static final byte TYPE_BITE = 3;
/*      */   public static final byte TYPE_BURN = 4;
/*      */   public static final byte TYPE_POISON = 5;
/*      */   public static final byte TYPE_INFECTION = 6;
/*      */   public static final byte TYPE_WATER = 7;
/*      */   public static final byte TYPE_COLD = 8;
/*      */   public static final byte TYPE_INTERNAL = 9;
/*      */   public static final byte TYPE_ACID = 10;
/*      */   public static final String wound = "Wound";
/*      */   public static final String burn = "Burn";
/*      */   public static final String coldburn = "Coldburn";
/*      */   public static final String acidburn = "Acidburn";
/*      */   public static final String bruise = "Bruise";
/*      */   public static final String inter = "Internal";
/*      */   public static final String drown = "Water";
/*      */   public static final String cut = "Cut";
/*      */   public static final String bite = "Bite";
/*      */   public static final String poison = "Poison";
/*      */   public static final String hole = "Hole";
/*      */   public static final String infection = "Infection";
/*      */   public static final String acid = "Acid";
/*      */   private static final float WOUNDMULTIPLIER = 50000.0F;
/*  115 */   private static final Logger logger = Logger.getLogger(Wound.class.getName());
/*      */   
/*      */   public static final float slowWoundMod = 4.0F;
/*      */   
/*      */   public static final float fastWoundMod = 5.0F;
/*      */   
/*      */   public static final float severityVeryLight = 3275.0F;
/*      */   
/*      */   public static final float severityLight = 9825.0F;
/*      */   
/*      */   public static final float severityMedium = 19650.0F;
/*      */   
/*      */   public static final float severityBad = 29475.0F;
/*      */   
/*      */   public static final float severitySevere = 60000.0F;
/*      */   
/*      */   public static final float effSeverityVeryLight = 20.0F;
/*      */   
/*      */   public static final float effSeverityLight = 40.0F;
/*      */   
/*      */   public static final float effSeverityMedium = 60.0F;
/*      */   
/*      */   public static final float effSeverityBad = 80.0F;
/*      */   
/*      */   public static final float effSeveritySevere = 100.0F;
/*      */   
/*      */   public static final String crushVeryLight = "a small bruise";
/*      */   
/*      */   public static final String crushLight = "a bruise";
/*      */   public static final String crushMedium = "an aching bruise";
/*      */   public static final String crushBad = "a severe fracture";
/*      */   public static final String crushSevere = "splinters of crushed bone";
/*      */   public static final String slashVeryLight = "a small bleeding scar";
/*      */   public static final String slashLight = "a trickle of blood";
/*      */   public static final String slashMedium = "a cut";
/*      */   public static final String slashBad = "a severe cut";
/*      */   public static final String slashSevere = "a wide gap of cut tissue";
/*      */   public static final String pierceVeryLight = "a small bleeding pinch";
/*      */   public static final String pierceLight = "a trickle of blood";
/*      */   public static final String pierceMedium = "a small hole";
/*      */   public static final String pierceBad = "a deep hole";
/*      */   public static final String pierceSevere = "a straight-through gaping hole";
/*      */   public static final String biteVeryLight = "a bruise from a bite";
/*      */   public static final String biteLight = "a light bite";
/*      */   public static final String biteMedium = "holes from a bite";
/*      */   public static final String biteBad = "a large bitewound";
/*      */   public static final String biteSevere = "a huge bitewound";
/*      */   public static final String coldLight = "a reddish tone";
/*      */   public static final String coldMedium = "white flecks";
/*      */   public static final String coldSevere = "black skin with possible gangrene";
/*      */   public static final String internalLight = "small tingle";
/*      */   public static final String internalMedium = "throbbing ache";
/*      */   public static final String internalSevere = "excruciating pain";
/*      */   public static final String burnVeryLight = "a few blisters";
/*      */   public static final String burnLight = "a lot of blisters on red skin";
/*      */   public static final String burnMedium = "some black and red burnt skin";
/*      */   public static final String burnBad = "some melted skin";
/*      */   public static final String burnSevere = "black and red melted and loose skin";
/*      */   public static final String acidVeryLight = "a few moist blisters";
/*      */   public static final String acidLight = "a lot of moist blisters on red skin";
/*      */   public static final String acidMedium = "some watery red burnt skin";
/*      */   public static final String acidBad = "some oozing melted skin";
/*      */   public static final String acidSevere = "black and bubbling melted and loose skin";
/*      */   public static final String waterVeryLight = "some coughing and choking";
/*      */   public static final String waterMedium = "water in the lungs";
/*      */   public static final String waterSevere = "waterfilled lungs, causing severe breathing difficulties";
/*      */   public static final String poisonVeryLight = "a faint dark aura";
/*      */   public static final String poisonLight = "a worrying dark aura";
/*      */   public static final String poisonMedium = "an ominous dark aura";
/*      */   public static final String poisonBad = "blue-black aura miscolouring the veins";
/*      */   public static final String poisonSevere = "black veins running from";
/*      */   public static final String infectionVeryLight = "with faintly miscolored edges";
/*      */   public static final String infectionLight = "with worryingly deep red edges";
/*      */   public static final String infectionMedium = "with ominously red edges and some yellow pus";
/*      */   public static final String infectionBad = "covered in yellow pus";
/*      */   public static final String infectionSevere = "rotting from infection";
/*  191 */   Creature creature = null;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float champDamageModifier = 0.4F;
/*      */ 
/*      */ 
/*      */   
/*      */   Wound(byte _type, byte _location, float _severity, long _owner, float _poisonSeverity, float _infectionSeverity, boolean isTemporary, boolean pvp, boolean spell) {
/*  200 */     this.type = _type;
/*      */     
/*  202 */     this.location = _location;
/*  203 */     this.severity = _severity;
/*  204 */     this.severity = (float)(this.severity * getWoundMod());
/*  205 */     this.severity = (int)this.severity;
/*      */     
/*  207 */     this.owner = _owner;
/*  208 */     this.poisonSeverity = _poisonSeverity;
/*  209 */     this.infectionSeverity = _infectionSeverity;
/*  210 */     this.lastPolled = System.currentTimeMillis();
/*  211 */     this.id = isTemporary ? WurmId.getNextTemporaryWoundId() : WurmId.getNextWoundId();
/*  212 */     setCreature();
/*  213 */     if (this.creature == null)
/*      */       return; 
/*  215 */     if ((this.type == 4 || this.type == 7 || this.type == 8) && this.creature.getCultist() != null && this.creature
/*  216 */       .getCultist().hasNoElementalDamage()) {
/*  217 */       this.severity = 0.0F;
/*      */     }
/*  219 */     this.severity *= this.creature.getDamageModifier(pvp, spell);
/*      */     
/*  221 */     if (this.creature.isChampion())
/*  222 */       this.severity = (int)Math.max(1.0F, this.severity * 0.4F); 
/*  223 */     if (this.creature.isPlayer() && (
/*  224 */       this.location == 18 || this.location == 19))
/*      */     {
/*  226 */       if (this.severity > 29475.0F)
/*  227 */         this.creature.achievement(35); 
/*      */     }
/*  229 */     create();
/*      */     
/*  231 */     addModifier(2);
/*  232 */     addModifier(3);
/*  233 */     addModifier(1);
/*  234 */     addModifier(4);
/*  235 */     addModifier(6);
/*  236 */     addModifier(5);
/*  237 */     this.creature.maybeInterruptAction((int)this.severity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Wound(long _id, byte _type, byte _location, float _severity, long _owner, float _poisonSeverity, float _infectionSeverity, long _lastPolled, boolean aBandaged, byte healeff) {
/*  244 */     this.id = _id;
/*  245 */     this.type = _type;
/*      */     
/*  247 */     this.location = _location;
/*  248 */     this.severity = _severity;
/*  249 */     this.owner = _owner;
/*  250 */     this.poisonSeverity = _poisonSeverity;
/*  251 */     this.infectionSeverity = _infectionSeverity;
/*  252 */     this.healEff = healeff;
/*  253 */     this.isBandaged = aBandaged;
/*  254 */     this.lastPolled = System.currentTimeMillis();
/*  255 */     addModifier(2);
/*  256 */     addModifier(3);
/*  257 */     addModifier(1);
/*  258 */     addModifier(4);
/*  259 */     addModifier(6);
/*  260 */     addModifier(5);
/*  261 */     setCreature();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   private static float getVulnerabilityModifier(Creature c, byte woundType) {
/*  267 */     if (c.hasAnyAbility())
/*      */     
/*  269 */     { switch (woundType)
/*      */       
/*      */       { case 4:
/*  272 */           if (c.getFireVulnerability() > 0.0F) {
/*  273 */             return c.getFireVulnerability();
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
/*      */ 
/*      */           
/*  315 */           return 1.0F;case 1: if (c.getSlashVulnerability() > 0.0F) return c.getSlashVulnerability();  return 1.0F;case 3: if (c.getBiteVulnerability() > 0.0F) return c.getBiteVulnerability();  return 1.0F;case 2: if (c.getPierceVulnerability() > 0.0F) return c.getPierceVulnerability();  return 1.0F;case 0: if (c.getCrushVulnerability() > 0.0F) return c.getCrushVulnerability();  return 1.0F;case 9: if (c.getInternalVulnerability() > 0.0F) return c.getInternalVulnerability();  return 1.0F;case 7: if (c.getWaterVulnerability() > 0.0F) return c.getWaterVulnerability();  return 1.0F;case 6: if (c.getDiseaseVulnerability() > 0.0F) return c.getDiseaseVulnerability();  return 1.0F;case 8: if (c.getColdVulnerability() > 0.0F) return c.getColdVulnerability();  return 1.0F;case 5: if (c.getPoisonVulnerability() > 0.0F) return c.getPoisonVulnerability();  return 1.0F; }  return 1.0F; }  return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getResistModifier(@Nullable Creature attacker, Creature c, byte woundType) {
/*  320 */     float mod = 1.0F;
/*  321 */     if (attacker != null) {
/*      */       
/*  323 */       float resMult = EnchantUtil.getJewelryDamageIncrease(attacker, woundType);
/*  324 */       if (Servers.isThisATestServer() && resMult != 1.0F)
/*      */       {
/*  326 */         c.getCommunicator().sendCombatAlertMessage(String.format("Damage reduced to %.1f%% from jewelry enchants.", new Object[] {
/*  327 */                 Float.valueOf(mod * 100.0F)
/*      */               })); } 
/*  329 */       mod *= resMult;
/*      */     } 
/*  331 */     float damMult = EnchantUtil.getJewelryResistModifier(c, woundType);
/*  332 */     mod *= damMult;
/*  333 */     if (Servers.isThisATestServer() && damMult != 1.0F)
/*      */     {
/*  335 */       c.getCommunicator().sendCombatAlertMessage(String.format("Damage increased to %.1f%% from jewelry enchants.", new Object[] {
/*  336 */               Float.valueOf(mod * 100.0F)
/*      */             })); } 
/*  338 */     if (c.hasAnyAbility()) {
/*      */       
/*  340 */       float physMod = 1.0F;
/*  341 */       if (c.getPhysicalResistance() > 0.0F)
/*  342 */         physMod = 1.0F - c.getPhysicalResistance(); 
/*  343 */       switch (woundType) {
/*      */         
/*      */         case 4:
/*  346 */           if (c.getFireResistance() > 0.0F)
/*  347 */             mod *= c.getFireResistance(); 
/*  348 */           if (c.getFireVulnerability() > 0.0F)
/*  349 */             mod *= c.getFireVulnerability(); 
/*      */           break;
/*      */         case 1:
/*  352 */           if (c.getSlashResistance() > 0.0F)
/*  353 */             mod *= c.getSlashResistance(); 
/*  354 */           if (c.getSlashVulnerability() > 0.0F)
/*  355 */             mod *= c.getSlashVulnerability(); 
/*  356 */           mod *= physMod;
/*      */           break;
/*      */         case 3:
/*  359 */           if (c.getBiteResistance() > 0.0F)
/*  360 */             mod *= c.getBiteResistance(); 
/*  361 */           if (c.getBiteVulnerability() > 0.0F)
/*  362 */             mod *= c.getBiteVulnerability(); 
/*  363 */           mod *= physMod;
/*      */           break;
/*      */         case 2:
/*  366 */           if (c.getPierceResistance() > 0.0F)
/*  367 */             mod *= c.getPierceResistance(); 
/*  368 */           if (c.getPierceVulnerability() > 0.0F)
/*  369 */             mod *= c.getPierceVulnerability(); 
/*  370 */           mod *= physMod;
/*      */           break;
/*      */         case 0:
/*  373 */           if (c.getCrushResistance() > 0.0F)
/*  374 */             mod *= c.getCrushResistance() * physMod; 
/*  375 */           if (c.getCrushVulnerability() > 0.0F)
/*  376 */             mod *= c.getCrushVulnerability(); 
/*  377 */           mod *= physMod;
/*      */           break;
/*      */         case 9:
/*  380 */           if (c.getInternalResistance() > 0.0F)
/*  381 */             mod *= c.getInternalResistance(); 
/*  382 */           if (c.getInternalVulnerability() > 0.0F)
/*  383 */             mod *= c.getInternalVulnerability(); 
/*      */           break;
/*      */         case 7:
/*  386 */           if (c.getWaterResistance() > 0.0F)
/*  387 */             mod *= c.getWaterResistance(); 
/*  388 */           if (c.getWaterVulnerability() > 0.0F)
/*  389 */             mod *= c.getWaterVulnerability(); 
/*      */           break;
/*      */         case 6:
/*  392 */           if (c.getDiseaseResistance() > 0.0F)
/*  393 */             mod *= c.getDiseaseResistance(); 
/*  394 */           if (c.getDiseaseVulnerability() > 0.0F)
/*  395 */             mod *= c.getDiseaseVulnerability(); 
/*      */           break;
/*      */         case 8:
/*  398 */           if (c.getColdResistance() > 0.0F)
/*  399 */             mod *= c.getColdResistance(); 
/*  400 */           if (c.getColdVulnerability() > 0.0F)
/*  401 */             mod *= c.getColdVulnerability(); 
/*      */           break;
/*      */         case 5:
/*  404 */           if (c.getPoisonResistance() > 0.0F)
/*  405 */             mod *= c.getPoisonResistance(); 
/*  406 */           if (c.getPoisonVulnerability() > 0.0F)
/*  407 */             mod *= c.getPoisonVulnerability(); 
/*      */           break;
/*      */       } 
/*      */     } 
/*  411 */     return mod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPoison() {
/*  422 */     return (this.poisonSeverity > 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInternal() {
/*  427 */     return (this.type == 9 || this.type == 5);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBruise() {
/*  432 */     return (this.severity < 19650.0F && this.type == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDrownWound() {
/*  437 */     return (this.type == 7);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAcidWound() {
/*  442 */     return (this.type == 10);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setType(byte newType) {
/*  447 */     this.type = newType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addModifier(int _type) {
/*  454 */     if (this.modifiers == null)
/*  455 */       this.modifiers = new HashSet<>(); 
/*  456 */     ValueModifier modifier = null;
/*      */     
/*  458 */     if (_type == 2) {
/*      */       
/*  460 */       int w = Wounds.getModifiedSkill(this.location, this.type);
/*  461 */       if (w != -1) {
/*      */         try
/*      */         {
/*      */           
/*  465 */           Creature _creature = Server.getInstance().getCreature(this.owner);
/*  466 */           float champMod = 1.0F;
/*  467 */           if (_creature.isChampion())
/*  468 */             champMod = 2.5F; 
/*  469 */           DoubleValueModifier doubleValueModifier = new DoubleValueModifier(2, (champMod * -this.severity / 50000.0F));
/*  470 */           _creature.getSkills().getSkill(w).addModifier(doubleValueModifier);
/*      */         }
/*  472 */         catch (NoSuchPlayerException nsp)
/*      */         {
/*  474 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*  475 */           modifier = null;
/*      */         }
/*  477 */         catch (NoSuchCreatureException nsc)
/*      */         {
/*  479 */           logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*  480 */           modifier = null;
/*      */         }
/*  482 */         catch (NoSuchSkillException nss)
/*      */         {
/*  484 */           modifier = null;
/*      */         }
/*      */       
/*      */       }
/*      */     }
/*  489 */     else if (_type == 1) {
/*      */       
/*  491 */       if (this.infectionSeverity > 0.0F) {
/*      */         try
/*      */         {
/*      */           
/*  495 */           Creature _creature = Server.getInstance().getCreature(this.owner);
/*  496 */           float champMod = 1.0F;
/*  497 */           if (_creature.isChampion())
/*  498 */             champMod = 2.5F; 
/*  499 */           DoubleValueModifier doubleValueModifier = new DoubleValueModifier(1, (champMod * -this.infectionSeverity / 500.0F));
/*  500 */           _creature.getStatus().addModifier(doubleValueModifier);
/*      */         }
/*  502 */         catch (NoSuchPlayerException nsp)
/*      */         {
/*  504 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*  505 */           modifier = null;
/*      */         }
/*  507 */         catch (NoSuchCreatureException nsc)
/*      */         {
/*  509 */           logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*  510 */           modifier = null;
/*      */         }
/*      */       
/*      */       }
/*  514 */     } else if (_type == 3) {
/*      */       
/*  516 */       if (impairsMovement()) {
/*      */         try
/*      */         {
/*      */           
/*  520 */           double mod = 0.30000001192092896D;
/*  521 */           if (this.location == 15 || this.location == 16)
/*  522 */             mod = 0.44999998807907104D; 
/*  523 */           Creature _creature = Server.getInstance().getCreature(this.owner);
/*  524 */           float champMod = 1.0F;
/*  525 */           if (_creature.isChampion())
/*  526 */             champMod = 1.1F; 
/*  527 */           DoubleValueModifier doubleValueModifier = new DoubleValueModifier(3, (champMod * -this.severity / 50000.0F) * mod);
/*  528 */           MovementScheme scheme = _creature.getMovementScheme();
/*  529 */           scheme.addModifier(doubleValueModifier);
/*  530 */           doubleValueModifier.addListener((ValueModifiedListener)scheme);
/*      */         }
/*  532 */         catch (NoSuchPlayerException nsp)
/*      */         {
/*  534 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*  535 */           modifier = null;
/*      */         }
/*  537 */         catch (NoSuchCreatureException nsc)
/*      */         {
/*  539 */           logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*  540 */           modifier = null;
/*      */         }
/*      */       
/*      */       }
/*  544 */     } else if (_type == 4) {
/*      */       
/*  546 */       if (this.location == 18 || this.location == 19) {
/*      */         try
/*      */         {
/*      */           
/*  550 */           Creature _creature = Server.getInstance().getCreature(this.owner);
/*  551 */           float champMod = 1.0F;
/*  552 */           if (_creature.isChampion())
/*  553 */             champMod = 2.5F; 
/*  554 */           DoubleValueModifier doubleValueModifier = new DoubleValueModifier(4, (champMod * this.severity / 50000.0F));
/*  555 */           _creature.addVisionModifier(doubleValueModifier);
/*      */         }
/*  557 */         catch (NoSuchPlayerException nsp)
/*      */         {
/*  559 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*  560 */           modifier = null;
/*      */         }
/*  562 */         catch (NoSuchCreatureException nsc)
/*      */         {
/*  564 */           logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*  565 */           modifier = null;
/*      */         }
/*      */       
/*      */       }
/*  569 */     } else if (_type == 5) {
/*      */       
/*  571 */       if (this.location == 4) {
/*      */         try
/*      */         {
/*      */           
/*  575 */           Creature _creature = Server.getInstance().getCreature(this.owner);
/*  576 */           float champMod = 1.0F;
/*  577 */           if (_creature.isChampion())
/*  578 */             champMod = 2.5F; 
/*  579 */           DoubleValueModifier doubleValueModifier = new DoubleValueModifier(5, (champMod * this.severity / 50000.0F));
/*  580 */           _creature.getCombatHandler().addParryModifier(doubleValueModifier);
/*      */         }
/*  582 */         catch (NoSuchPlayerException nsp)
/*      */         {
/*  584 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*  585 */           modifier = null;
/*      */         }
/*  587 */         catch (NoSuchCreatureException nsc)
/*      */         {
/*  589 */           logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*  590 */           modifier = null;
/*      */         }
/*      */       
/*      */       }
/*  594 */     } else if (_type == 6) {
/*      */       
/*  596 */       if (this.location == 23) {
/*      */         
/*      */         try {
/*      */           
/*  600 */           Creature _creature = Server.getInstance().getCreature(this.owner);
/*  601 */           float champMod = 1.0F;
/*  602 */           if (_creature.isChampion())
/*  603 */             champMod = 2.5F; 
/*  604 */           DoubleValueModifier doubleValueModifier = new DoubleValueModifier(6, (champMod * this.severity / 50000.0F));
/*  605 */           _creature.getCombatHandler().addDodgeModifier(doubleValueModifier);
/*      */         }
/*  607 */         catch (NoSuchPlayerException nsp) {
/*      */           
/*  609 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*  610 */           modifier = null;
/*      */         }
/*  612 */         catch (NoSuchCreatureException nsc) {
/*      */           
/*  614 */           logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*  615 */           modifier = null;
/*      */         } 
/*      */       }
/*      */     } 
/*  619 */     if (modifier != null) {
/*  620 */       this.modifiers.add(modifier);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean impairsMovement() {
/*  626 */     return (this.location == 30 || this.location == 31 || this.location == 15 || this.location == 16 || this.location == 11 || this.location == 12);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeModifier(ValueModifier modifier) {
/*  632 */     this.modifiers.remove(modifier);
/*      */     
/*      */     try {
/*  635 */       Creature _creature = Server.getInstance().getCreature(this.owner);
/*  636 */       if (modifier.getType() == 1) {
/*      */         
/*  638 */         _creature.getStatus().removeModifier((DoubleValueModifier)modifier);
/*      */       }
/*  640 */       else if (modifier.getType() == 2) {
/*      */         
/*  642 */         int w = Wounds.getModifiedSkill(this.location, this.type);
/*  643 */         if (w != -1) {
/*  644 */           _creature.getSkills().getSkill(w).removeModifier((DoubleValueModifier)modifier);
/*      */         } else {
/*  646 */           logger.log(Level.WARNING, "This should not happen.");
/*      */         } 
/*  648 */       } else if (modifier.getType() == 3) {
/*      */         
/*  650 */         MovementScheme scheme = _creature.getMovementScheme();
/*  651 */         scheme.removeModifier((DoubleValueModifier)modifier);
/*  652 */         modifier.removeListener((ValueModifiedListener)scheme);
/*      */       }
/*  654 */       else if (modifier.getType() == 5) {
/*      */         
/*  656 */         _creature.getCombatHandler().removeParryModifier((DoubleValueModifier)modifier);
/*      */       }
/*  658 */       else if (modifier.getType() == 6) {
/*      */         
/*  660 */         _creature.getCombatHandler().removeDodgeModifier((DoubleValueModifier)modifier);
/*      */       }
/*  662 */       else if (modifier.getType() == 4) {
/*      */         
/*  664 */         _creature.removeVisionModifier((DoubleValueModifier)modifier);
/*      */       }
/*      */     
/*  667 */     } catch (NoSuchPlayerException nsp) {
/*      */       
/*  669 */       logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */     }
/*  671 */     catch (NoSuchCreatureException nsc) {
/*      */       
/*  673 */       logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */     }
/*  675 */     catch (NoSuchSkillException noSuchSkillException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeModifier(int _type) {
/*  683 */     if (this.modifiers != null) {
/*      */       
/*  685 */       ValueModifier[] mods = getModifiers();
/*  686 */       for (int x = 0; x < mods.length; x++) {
/*      */         
/*  688 */         if (mods[x].getType() == _type) {
/*  689 */           removeModifier(mods[x]);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private ValueModifier getModifier(int _type) {
/*  696 */     if (this.modifiers != null) {
/*      */       
/*  698 */       ValueModifier[] mods = getModifiers();
/*  699 */       for (int x = 0; x < mods.length; x++) {
/*      */         
/*  701 */         if (mods[x].getType() == _type)
/*  702 */           return mods[x]; 
/*      */       } 
/*      */     } 
/*  705 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ValueModifier[] getModifiers() {
/*  714 */     if (this.modifiers != null) {
/*  715 */       return this.modifiers.<ValueModifier>toArray(new ValueModifier[this.modifiers.size()]);
/*      */     }
/*  717 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   final void removeAllModifiers() {
/*  722 */     ValueModifier[] mods = getModifiers();
/*  723 */     if (mods != null)
/*      */     {
/*  725 */       for (int x = 0; x < mods.length; x++) {
/*  726 */         removeModifier(mods[x]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getLocation() {
/*  736 */     return this.location;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPoisonSeverity() {
/*  746 */     return this.poisonSeverity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getSeverity() {
/*  755 */     return this.severity;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ProtoConstants.WoundSeverity getSeverityEnum() {
/*  760 */     ProtoConstants.WoundSeverity toReturn = ProtoConstants.WoundSeverity.severe;
/*  761 */     if (this.severity < 3275.0F) {
/*  762 */       toReturn = ProtoConstants.WoundSeverity.verylight;
/*  763 */     } else if (this.severity < 9825.0F) {
/*  764 */       toReturn = ProtoConstants.WoundSeverity.light;
/*  765 */     } else if (this.severity < 19650.0F) {
/*  766 */       toReturn = ProtoConstants.WoundSeverity.medium;
/*  767 */     } else if (this.severity < 29475.0F) {
/*  768 */       toReturn = ProtoConstants.WoundSeverity.bad;
/*  769 */     }  return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final long getId() {
/*  779 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getType() {
/*  789 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ProtoConstants.WoundType getTypeEnum() {
/*  794 */     return ProtoConstants.WoundType.bite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final long getOwner() {
/*  804 */     return this.owner;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getInfectionSeverity() {
/*  814 */     return this.infectionSeverity;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ProtoConstants.InfectionSeverity getInfectionSeverityEnum() {
/*  819 */     return ProtoConstants.InfectionSeverity.bad;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastPolled() {
/*  829 */     return this.lastPolled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getHealEff() {
/*  839 */     return this.healEff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Creature getCreature() {
/*  849 */     return this.creature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void removeCreature() {
/*  858 */     this.creature = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean bandage() {
/*  868 */     setBandaged(true);
/*  869 */     return this.isBandaged;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean curePoison() {
/*  879 */     if (this.type == 5) {
/*  880 */       heal();
/*      */     } else {
/*      */       
/*  883 */       setPoisonSeverity(0.0F);
/*      */     } 
/*  885 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean cureInfection() {
/*  895 */     if (this.type == 6) {
/*  896 */       heal();
/*      */     } else {
/*      */       
/*  899 */       setInfectionSeverity(0.0F);
/*  900 */       removeModifier(1);
/*      */     } 
/*  902 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void heal() {
/*  907 */     if (this.creature != null) {
/*      */       
/*  909 */       this.creature.getStatus().modifyWounds((int)-this.severity);
/*  910 */       Body body = this.creature.getBody();
/*  911 */       body.removeWound(this);
/*      */       
/*  913 */       if (this.creature != null && isPoison()) {
/*  914 */         this.creature.poisonChanged(true, this);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean modifySeverity(int num) {
/*  920 */     return modifySeverity(num, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean modifySeverity(int num, boolean pvp, boolean spell) {
/*  930 */     boolean dead = false;
/*  931 */     if (this.creature != null) {
/*      */       
/*  933 */       num = (int)(num * getWoundMod());
/*      */ 
/*      */       
/*  936 */       if (num > 0) {
/*      */         
/*  938 */         num = (int)(num * this.creature.getDamageModifier(pvp, spell));
/*  939 */         if (this.creature.isChampion()) {
/*  940 */           num = (int)Math.max(1.0F, num * 0.4F);
/*      */         }
/*      */       } 
/*  943 */       Body body = this.creature.getBody();
/*      */       
/*  945 */       float sev = this.severity + num;
/*      */       
/*  947 */       if (sev <= 0.0F) {
/*      */         
/*  949 */         this.creature.getStatus().modifyWounds((int)-this.severity);
/*  950 */         body.removeWound(this);
/*      */       }
/*      */       else {
/*      */         
/*  954 */         setSeverity(sev);
/*  955 */         if (num > 0 && this.severity > 1000.0F) {
/*  956 */           this.creature.maybeInterruptAction((int)this.severity);
/*      */         }
/*      */ 
/*      */         
/*  960 */         float champMod = 1.0F;
/*  961 */         if (this.creature.isChampion())
/*  962 */           champMod = 2.5F; 
/*  963 */         dead = this.creature.getStatus().modifyWounds(num);
/*  964 */         DoubleValueModifier val = (DoubleValueModifier)getModifier(2);
/*  965 */         if (val != null)
/*  966 */           val.setModifier((champMod * -this.severity / 50000.0F)); 
/*  967 */         val = (DoubleValueModifier)getModifier(6);
/*  968 */         if (val != null)
/*  969 */           val.setModifier((champMod * this.severity / 50000.0F)); 
/*  970 */         val = (DoubleValueModifier)getModifier(5);
/*  971 */         if (val != null)
/*  972 */           val.setModifier((champMod * this.severity / 50000.0F)); 
/*  973 */         val = (DoubleValueModifier)getModifier(4);
/*  974 */         if (val != null)
/*  975 */           val.setModifier((champMod * this.severity / 50000.0F)); 
/*  976 */         val = (DoubleValueModifier)getModifier(3);
/*  977 */         if (val != null) {
/*      */           
/*  979 */           double mod = 0.30000001192092896D;
/*  980 */           if (this.location == 15 || this.location == 16)
/*  981 */             mod = 0.44999998807907104D; 
/*  982 */           if (this.creature.isChampion())
/*  983 */             champMod = 1.1F; 
/*  984 */           val.setModifier((champMod * -this.severity / 50000.0F) * mod);
/*      */         } 
/*  986 */         if (!dead) {
/*      */           
/*      */           try {
/*      */             
/*  990 */             if (this.creature != null)
/*      */             {
/*      */ 
/*      */               
/*  994 */               if (this.creature.getBody() != null)
/*      */               {
/*  996 */                 Item bodypart = this.creature.getBody().getBodyPartForWound(this);
/*      */ 
/*      */ 
/*      */                 
/*      */                 try {
/* 1001 */                   Creature[] watchers = bodypart.getWatchers();
/* 1002 */                   for (int x = 0; x < watchers.length; x++) {
/* 1003 */                     watchers[x].getCommunicator().sendUpdateWound(this, bodypart);
/*      */                   }
/* 1005 */                 } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */               
/*      */               }
/*      */               else
/*      */               {
/*      */                 
/* 1012 */                 logger.log(Level.WARNING, this.creature.getName() + " body is null.", new Exception());
/*      */               }
/*      */             
/*      */             }
/* 1016 */           } catch (NoSpaceException nsp) {
/*      */             
/* 1018 */             logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1023 */     return dead;
/*      */   }
/*      */ 
/*      */   
/*      */   private double getWoundMod() {
/* 1028 */     double toReturn = 1.0D;
/* 1029 */     if (this.location == 18 || this.location == 19 || this.location == 20) {
/* 1030 */       toReturn = 1.35D;
/* 1031 */     } else if (this.location == 29 || this.location == 17 || this.location == 33 || this.location == 1) {
/*      */       
/* 1033 */       toReturn = 1.3D;
/* 1034 */     } else if (this.location == 5 || this.location == 6) {
/* 1035 */       toReturn = 1.25D;
/* 1036 */     } else if (this.location == 13 || this.location == 14 || this.location == 15 || this.location == 16 || this.location == 25) {
/*      */       
/* 1038 */       toReturn = 1.2D;
/* 1039 */     }  return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void poll(boolean hasWoundIncreasePrevention) throws Exception {
/* 1045 */     if (System.currentTimeMillis() - this.lastPolled > 600000L || ((this.creature == null || 
/* 1046 */       !this.creature.isUnique()) && this.type == 6 && System.currentTimeMillis() - this.lastPolled > 60000L)) {
/*      */       
/* 1048 */       float mod = 5.0F;
/*      */       
/* 1050 */       if (this.severity > 3275.0F) {
/*      */         
/* 1052 */         mod = -5.0F;
/* 1053 */         if (this.severity < 9825.0F) {
/* 1054 */           mod = 4.0F;
/* 1055 */         } else if (this.severity < 19650.0F) {
/* 1056 */           mod = 0.0F;
/* 1057 */         } else if (this.severity < 29475.0F) {
/* 1058 */           mod = -4.0F;
/*      */         } 
/* 1060 */       }  if (this.healEff > 0) {
/* 1061 */         mod += this.healEff / 2.0F;
/*      */       }
/* 1063 */       if (this.isBandaged)
/* 1064 */         mod++; 
/* 1065 */       if (this.type == 7) {
/* 1066 */         mod += 3.0F;
/* 1067 */       } else if (this.type == 9) {
/* 1068 */         mod += 10.0F;
/* 1069 */       }  if (this.type == 10) {
/* 1070 */         mod -= 3.0F;
/*      */       }
/* 1072 */       if (this.type == 6 && !isBandaged() && !isTreated()) {
/* 1073 */         mod -= 5.0F;
/*      */       }
/* 1075 */       if (this.creature != null) {
/*      */         
/* 1077 */         if (!this.creature.isUnique()) {
/*      */           
/* 1079 */           if (this.creature.getStatus().getNutritionlevel() > 0.6F) {
/* 1080 */             mod++;
/* 1081 */           } else if (this.creature.getStatus().getNutritionlevel() < 0.4F) {
/* 1082 */             mod--;
/*      */           } 
/* 1084 */           if ((this.creature.getStatus()).fat > 70) {
/* 1085 */             mod++;
/* 1086 */           } else if ((this.creature.getStatus()).fat < 30) {
/* 1087 */             mod--;
/* 1088 */           }  if (this.infectionSeverity > 0.0F) {
/*      */             
/* 1090 */             int rand = Server.rand.nextInt(100 + this.healEff);
/* 1091 */             if (rand < this.infectionSeverity)
/* 1092 */               mod -= Math.max(1.0F, this.infectionSeverity / 10.0F); 
/*      */           } 
/* 1094 */           if (this.creature != null && this.creature.getDeity() != null)
/*      */           {
/* 1096 */             if (this.creature.getDeity().isHealer() && this.creature.getFaith() >= 20.0F && this.creature.getFavor() > 10.0F)
/* 1097 */               mod += 3.0F; 
/*      */           }
/* 1099 */           if (this.creature.getCultist() != null && 
/* 1100 */             this.creature.getCultist().healsFaster())
/* 1101 */             mod += 3.0F; 
/* 1102 */           int tn = this.creature.getCurrentTileNum();
/* 1103 */           if (Tiles.getTile(Tiles.decodeType(tn)).isEnchanted()) {
/* 1104 */             mod += 2.0F;
/*      */           }
/*      */         } else {
/*      */           
/* 1108 */           setInfectionSeverity(0.0F);
/* 1109 */           mod += 3.0F;
/*      */         } 
/* 1111 */         if (this.type == 7)
/*      */         {
/* 1113 */           if (this.creature.getPositionZ() + this.creature.getAltOffZ() > 0.0F)
/* 1114 */             mod = 100.0F; 
/*      */         }
/* 1116 */         if (this.creature.getSpellEffects() != null && 
/* 1117 */           this.creature.getSpellEffects().getSpellEffect((byte)75) != null)
/*      */         {
/* 1119 */           mod += 5.0F;
/*      */         }
/*      */       } 
/* 1122 */       if (this.creature != null) {
/*      */         
/* 1124 */         if (this.creature.getCitizenVillage() != null)
/*      */         {
/* 1126 */           if (this.creature.getCitizenVillage().getFaithHealBonus() > 0.0F && mod > 0.0F) {
/* 1127 */             mod *= 1.0F + this.creature.getCitizenVillage().getFaithHealBonus() / 100.0F;
/*      */           }
/*      */         }
/* 1130 */         if (mod > 0.0F) {
/*      */           
/* 1132 */           mod *= 1.0F + ItemBonus.getHealingBonus(this.creature);
/* 1133 */           if (this.creature.isPlayer() && (this.creature.getStatus()).damage > 63568.953F)
/* 1134 */             this.creature.achievement(36); 
/*      */         } 
/* 1136 */         if (ItemBonus.getHealingBonus(this.creature) > 0.0F)
/* 1137 */           mod++; 
/*      */       } 
/* 1139 */       if (!hasWoundIncreasePrevention || mod > 0.0F)
/* 1140 */         modifySeverity((int)(-655.0F * mod)); 
/* 1141 */       if (this.creature != null && !this.creature.isUnique()) {
/*      */         
/* 1143 */         checkInfection();
/* 1144 */         checkPoison();
/*      */       } 
/* 1146 */       setLastPolled(System.currentTimeMillis());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkPoison() {
/* 1152 */     if (this.poisonSeverity > 0.0F) {
/*      */       
/* 1154 */       setPoisonSeverity(this.poisonSeverity + Server.rand.nextInt(18) - 10.0F);
/*      */       
/* 1156 */       if (this.poisonSeverity >= 100.0F) {
/*      */         
/* 1158 */         if (this.creature != null) {
/*      */           
/* 1160 */           this.creature.getCommunicator().sendAlertServerMessage("The poison reaches your heart!", (byte)2);
/* 1161 */           Server.getInstance().broadCastAction(this.creature.getName() + " falls down dead, poisoned.", this.creature, 5);
/* 1162 */           this.creature.die(false, "Poison");
/*      */         } else {
/*      */           
/* 1165 */           logger.log(Level.WARNING, "Wound with id " + this.id + ", owner " + this.owner + " has no owner!", new Exception());
/*      */         } 
/* 1167 */       } else if (this.poisonSeverity > 50.0F) {
/* 1168 */         this.creature.getCommunicator().sendAlertServerMessage("The poison burning in your veins makes you sweat!", (byte)2);
/*      */       } else {
/* 1170 */         this.creature.getCommunicator().sendAlertServerMessage("Your wound aches and you feel feverish.", (byte)2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setCreature() {
/*      */     try {
/* 1178 */       this.creature = Server.getInstance().getCreature(this.owner);
/*      */       
/* 1180 */       if (this.creature.isPlayer())
/*      */       {
/* 1182 */         if (this.poisonSeverity > 0.0F) {
/* 1183 */           this.creature.poisonChanged(false, this);
/*      */         }
/*      */       }
/* 1186 */     } catch (NoSuchCreatureException nsc) {
/*      */       
/* 1188 */       logger.log(Level.WARNING, "Creature not found for this wound " + nsc.getMessage(), (Throwable)nsc);
/*      */     }
/* 1190 */     catch (NoSuchPlayerException nsp) {
/*      */       
/* 1192 */       logger.log(Level.WARNING, "Player not found for this wound " + nsp.getMessage(), (Throwable)nsp);
/*      */     }
/* 1194 */     catch (Exception nsw) {
/*      */       
/* 1196 */       logger.log(Level.WARNING, "Wound not found " + nsw.getMessage(), nsw);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkInfection() {
/* 1202 */     int r = 0;
/* 1203 */     if (this.type == 1 || this.type == 2) {
/* 1204 */       r = 100;
/* 1205 */     } else if (this.type == 3) {
/* 1206 */       r = 100;
/* 1207 */     }  if (this.type == 6)
/* 1208 */       r = 100; 
/* 1209 */     if (r > 0) {
/*      */       
/* 1211 */       int rand = Server.rand.nextInt(r);
/* 1212 */       if (rand == 0) {
/*      */         
/* 1214 */         if (this.infectionSeverity == 0.0F)
/*      */         {
/* 1216 */           setInfectionSeverity(10.0F);
/* 1217 */           addModifier(1);
/*      */         }
/*      */       
/* 1220 */       } else if (this.infectionSeverity != 0.0F && rand > r * 0.7D) {
/*      */ 
/*      */         
/* 1223 */         setInfectionSeverity(this.infectionSeverity - rand / 10.0F);
/* 1224 */         DoubleValueModifier val = (DoubleValueModifier)getModifier(1);
/* 1225 */         if (val != null)
/*      */         {
/* 1227 */           if (this.infectionSeverity > 0.0F) {
/* 1228 */             val.setModifier(this.infectionSeverity);
/*      */           } else {
/* 1230 */             removeModifier(1);
/*      */           } 
/*      */         }
/* 1233 */       } else if (this.infectionSeverity != 0.0F && this.infectionSeverity > 0.0F) {
/*      */         
/* 1235 */         if (rand % 2 == 0) {
/* 1236 */           setInfectionSeverity(this.infectionSeverity + rand / 10.0F);
/*      */         } else {
/* 1238 */           setInfectionSeverity(this.infectionSeverity - rand / 20.0F);
/*      */         } 
/* 1240 */         DoubleValueModifier val = (DoubleValueModifier)getModifier(1);
/* 1241 */         if (val != null)
/*      */         {
/* 1243 */           if (this.infectionSeverity > 0.0F) {
/* 1244 */             val.setModifier(this.infectionSeverity);
/*      */           } else {
/* 1246 */             removeModifier(1);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final int getWoundIconId() {
/* 1254 */     if (this.poisonSeverity > 0.0F)
/* 1255 */       return 86; 
/* 1256 */     switch (this.type) {
/*      */       
/*      */       case 4:
/* 1259 */         return 82;
/*      */       case 1:
/* 1261 */         return 83;
/*      */       case 3:
/* 1263 */         return 80;
/*      */       case 2:
/* 1265 */         return 84;
/*      */       case 0:
/* 1267 */         return 81;
/*      */       case 9:
/* 1269 */         return 89;
/*      */       case 7:
/* 1271 */         return 88;
/*      */       case 6:
/* 1273 */         return 85;
/*      */       case 8:
/* 1275 */         return 87;
/*      */       case 5:
/* 1277 */         return 86;
/*      */       case 10:
/* 1279 */         return 90;
/*      */     } 
/* 1281 */     return 81;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/* 1287 */     if (this.poisonSeverity > 0.0F) {
/* 1288 */       return "Poison";
/*      */     }
/* 1290 */     return getName(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getName(byte type) {
/* 1295 */     switch (type) {
/*      */       
/*      */       case 4:
/* 1298 */         return "Burn";
/*      */       case 1:
/* 1300 */         return "Cut";
/*      */       case 3:
/* 1302 */         return "Bite";
/*      */       case 2:
/* 1304 */         return "Hole";
/*      */       case 0:
/* 1306 */         return "Bruise";
/*      */       case 9:
/* 1308 */         return "Internal";
/*      */       case 7:
/* 1310 */         return "Water";
/*      */       case 6:
/* 1312 */         return "Infection";
/*      */       case 8:
/* 1314 */         return "Coldburn";
/*      */       case 5:
/* 1316 */         return "Poison";
/*      */       case 10:
/* 1318 */         return "Acid";
/*      */     } 
/* 1320 */     return "Wound";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getNumBandagesNeeded() {
/* 1326 */     if (this.severity < 9825.0F)
/* 1327 */       return 1; 
/* 1328 */     if (this.severity < 19650.0F)
/* 1329 */       return 2; 
/* 1330 */     if (this.severity < 29475.0F)
/* 1331 */       return 4; 
/* 1332 */     return 8;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getDescription() {
/* 1337 */     String toReturn = "severe";
/* 1338 */     if (this.severity < 3275.0F) {
/* 1339 */       toReturn = "very light";
/* 1340 */     } else if (this.severity < 9825.0F) {
/* 1341 */       toReturn = "light";
/* 1342 */     } else if (this.severity < 19650.0F) {
/* 1343 */       toReturn = "medium";
/* 1344 */     } else if (this.severity < 29475.0F) {
/* 1345 */       toReturn = "bad";
/* 1346 */     }  if (!isInternal() && this.isBandaged) {
/* 1347 */       toReturn = toReturn + ", bandaged";
/* 1348 */     } else if (this.isBandaged) {
/* 1349 */       toReturn = toReturn + ", applied";
/* 1350 */     }  if (this.healEff > 0)
/* 1351 */       toReturn = toReturn + ", treated"; 
/* 1352 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getWoundString() {
/* 1357 */     String toReturn = "";
/* 1358 */     if (this.poisonSeverity > 0.0F) {
/*      */       
/* 1360 */       if (this.poisonSeverity < 20.0F) {
/* 1361 */         toReturn = toReturn + "a faint dark aura";
/* 1362 */       } else if (this.poisonSeverity < 40.0F) {
/* 1363 */         toReturn = toReturn + "a worrying dark aura";
/* 1364 */       } else if (this.poisonSeverity < 60.0F) {
/* 1365 */         toReturn = toReturn + "an ominous dark aura";
/* 1366 */       } else if (this.poisonSeverity < 80.0F) {
/* 1367 */         toReturn = toReturn + "blue-black aura miscolouring the veins";
/*      */       } else {
/* 1369 */         toReturn = toReturn + "black veins running from";
/* 1370 */       }  toReturn = toReturn + " around ";
/*      */     } 
/* 1372 */     if (this.type == 1) {
/*      */       
/* 1374 */       if (this.severity < 3275.0F) {
/* 1375 */         toReturn = toReturn + "a small bleeding scar";
/* 1376 */       } else if (this.severity < 9825.0F) {
/* 1377 */         toReturn = toReturn + "a trickle of blood";
/* 1378 */       } else if (this.severity < 19650.0F) {
/* 1379 */         toReturn = toReturn + "a cut";
/* 1380 */       } else if (this.severity < 29475.0F) {
/* 1381 */         toReturn = toReturn + "a severe cut";
/*      */       } else {
/* 1383 */         toReturn = toReturn + "a wide gap of cut tissue";
/*      */       } 
/* 1385 */     } else if (this.type == 2) {
/*      */       
/* 1387 */       if (this.severity < 3275.0F) {
/* 1388 */         toReturn = toReturn + "a small bleeding pinch";
/* 1389 */       } else if (this.severity < 9825.0F) {
/* 1390 */         toReturn = toReturn + "a trickle of blood";
/* 1391 */       } else if (this.severity < 19650.0F) {
/* 1392 */         toReturn = toReturn + "a small hole";
/* 1393 */       } else if (this.severity < 29475.0F) {
/* 1394 */         toReturn = toReturn + "a deep hole";
/*      */       } else {
/* 1396 */         toReturn = toReturn + "a straight-through gaping hole";
/*      */       } 
/* 1398 */     } else if (this.type == 0) {
/*      */       
/* 1400 */       if (this.severity < 3275.0F) {
/* 1401 */         toReturn = toReturn + "a small bruise";
/* 1402 */       } else if (this.severity < 9825.0F) {
/* 1403 */         toReturn = toReturn + "a bruise";
/* 1404 */       } else if (this.severity < 19650.0F) {
/* 1405 */         toReturn = toReturn + "an aching bruise";
/* 1406 */       } else if (this.severity < 29475.0F) {
/* 1407 */         toReturn = toReturn + "a severe fracture";
/*      */       } else {
/* 1409 */         toReturn = toReturn + "splinters of crushed bone";
/*      */       } 
/* 1411 */     } else if (this.type == 3) {
/*      */       
/* 1413 */       if (this.severity < 3275.0F) {
/* 1414 */         toReturn = toReturn + "a bruise from a bite";
/* 1415 */       } else if (this.severity < 9825.0F) {
/* 1416 */         toReturn = toReturn + "a light bite";
/* 1417 */       } else if (this.severity < 19650.0F) {
/* 1418 */         toReturn = toReturn + "holes from a bite";
/* 1419 */       } else if (this.severity < 29475.0F) {
/* 1420 */         toReturn = toReturn + "a large bitewound";
/*      */       } else {
/* 1422 */         toReturn = toReturn + "a huge bitewound";
/*      */       } 
/* 1424 */     } else if (this.type == 4) {
/*      */       
/* 1426 */       if (this.severity < 3275.0F) {
/* 1427 */         toReturn = toReturn + "a few blisters";
/* 1428 */       } else if (this.severity < 9825.0F) {
/* 1429 */         toReturn = toReturn + "a lot of blisters on red skin";
/* 1430 */       } else if (this.severity < 19650.0F) {
/* 1431 */         toReturn = toReturn + "some black and red burnt skin";
/* 1432 */       } else if (this.severity < 29475.0F) {
/* 1433 */         toReturn = toReturn + "some melted skin";
/*      */       } else {
/* 1435 */         toReturn = toReturn + "black and red melted and loose skin";
/*      */       } 
/* 1437 */     } else if (this.type == 10) {
/*      */       
/* 1439 */       if (this.severity < 3275.0F) {
/* 1440 */         toReturn = toReturn + "a few moist blisters";
/* 1441 */       } else if (this.severity < 9825.0F) {
/* 1442 */         toReturn = toReturn + "a lot of moist blisters on red skin";
/* 1443 */       } else if (this.severity < 19650.0F) {
/* 1444 */         toReturn = toReturn + "some watery red burnt skin";
/* 1445 */       } else if (this.severity < 29475.0F) {
/* 1446 */         toReturn = toReturn + "some oozing melted skin";
/*      */       } else {
/* 1448 */         toReturn = toReturn + "black and bubbling melted and loose skin";
/*      */       } 
/* 1450 */     } else if (this.type == 8) {
/*      */       
/* 1452 */       if (this.severity < 9825.0F) {
/* 1453 */         toReturn = toReturn + "a reddish tone";
/* 1454 */       } else if (this.severity < 29475.0F) {
/* 1455 */         toReturn = toReturn + "white flecks";
/*      */       } else {
/* 1457 */         toReturn = toReturn + "black skin with possible gangrene";
/*      */       } 
/* 1459 */     } else if (this.type == 7) {
/*      */       
/* 1461 */       if (this.severity < 9825.0F) {
/* 1462 */         toReturn = toReturn + "some coughing and choking";
/* 1463 */       } else if (this.severity < 29475.0F) {
/* 1464 */         toReturn = toReturn + "water in the lungs";
/*      */       } else {
/* 1466 */         toReturn = toReturn + "waterfilled lungs, causing severe breathing difficulties";
/*      */       } 
/* 1468 */     } else if (this.type == 9) {
/*      */       
/* 1470 */       if (this.severity < 9825.0F) {
/* 1471 */         toReturn = toReturn + "small tingle";
/* 1472 */       } else if (this.severity < 29475.0F) {
/* 1473 */         toReturn = toReturn + "throbbing ache";
/*      */       } else {
/* 1475 */         toReturn = toReturn + "excruciating pain";
/*      */       } 
/*      */     } else {
/* 1478 */       toReturn = toReturn + "a wound";
/* 1479 */     }  if (this.infectionSeverity > 0.0F) {
/*      */       
/* 1481 */       toReturn = toReturn + " ";
/* 1482 */       if (this.infectionSeverity < 20.0F) {
/* 1483 */         toReturn = toReturn + "with faintly miscolored edges";
/* 1484 */       } else if (this.infectionSeverity < 40.0F) {
/* 1485 */         toReturn = toReturn + "with worryingly deep red edges";
/* 1486 */       } else if (this.infectionSeverity < 60.0F) {
/* 1487 */         toReturn = toReturn + "with ominously red edges and some yellow pus";
/* 1488 */       } else if (this.infectionSeverity < 80.0F) {
/* 1489 */         toReturn = toReturn + "covered in yellow pus";
/*      */       } else {
/* 1491 */         toReturn = toReturn + "rotting from infection";
/*      */       } 
/* 1493 */     }  if (this.creature != null && this.creature.getPower() >= 3)
/* 1494 */       toReturn = toReturn + " (" + this.severity + ")"; 
/* 1495 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getWurmId() {
/* 1500 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBandaged() {
/* 1509 */     return this.isBandaged;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTreated() {
/* 1514 */     return (this.healEff > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void create();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setSeverity(float paramFloat);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setPoisonSeverity(float paramFloat);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setInfectionSeverity(float paramFloat);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setBandaged(boolean paramBoolean);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setLastPolled(long paramLong);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setHealeff(byte paramByte);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void delete();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte getFlagByte(boolean isBandaged, boolean isTreated) {
/* 1562 */     byte flags = 0;
/* 1563 */     flags = (byte)(((isBandaged == true) ? 1 : 0) << 1);
/* 1564 */     flags = (byte)(flags & ((isTreated == true) ? 1 : 0) << 0);
/* 1565 */     return (byte)(flags & 0x7);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\Wound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */