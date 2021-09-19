/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.shared.constants.Enchants;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnchantUtil
/*     */   implements Enchants
/*     */ {
/*  15 */   protected static final Logger logger = Logger.getLogger(EnchantUtil.class.getName());
/*     */   
/*  17 */   public static ArrayList<ArrayList<Byte>> enchantGroups = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getDemiseBonus(Item item, Creature defender) {
/*  28 */     if (item.enchantment != 0)
/*     */     {
/*  30 */       if (item.enchantment == 11) {
/*     */         
/*  32 */         if (defender.isAnimal() && !defender.isUnique()) {
/*  33 */           return 0.03F;
/*     */         }
/*  35 */       } else if (item.enchantment == 9) {
/*     */         
/*  37 */         if (defender.isPlayer() || defender.isHuman()) {
/*  38 */           return 0.03F;
/*     */         }
/*  40 */       } else if (item.enchantment == 10) {
/*     */         
/*  42 */         if (defender.isMonster() && !defender.isUnique()) {
/*  43 */           return 0.03F;
/*     */         }
/*  45 */       } else if (item.enchantment == 12) {
/*     */         
/*  47 */         if (defender.isUnique())
/*  48 */           return 0.03F; 
/*     */       } 
/*     */     }
/*  51 */     return 0.0F;
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
/*     */   public static float getJewelryDamageIncrease(Creature attacker, byte woundType) {
/*  63 */     byte jewelryEnchant = 0;
/*  64 */     switch (woundType) {
/*     */       
/*     */       case 4:
/*  67 */         jewelryEnchant = 2;
/*     */         break;
/*     */       case 5:
/*  70 */         jewelryEnchant = 1;
/*     */         break;
/*     */       case 8:
/*  73 */         jewelryEnchant = 3;
/*     */         break;
/*     */       case 10:
/*  76 */         jewelryEnchant = 4;
/*     */         break;
/*     */     } 
/*     */     
/*  80 */     if (jewelryEnchant == 0) {
/*  81 */       return 1.0F;
/*     */     }
/*     */     
/*  84 */     Item[] bodyItems = attacker.getBody().getContainersAndWornItems();
/*  85 */     float damageMultiplier = 1.0F;
/*  86 */     float totalPower = 0.0F;
/*  87 */     int activeJewelry = 0;
/*  88 */     for (Item bodyItem : bodyItems) {
/*     */ 
/*     */       
/*  91 */       if (bodyItem.isEnchantableJewelry() && bodyItem.getBonusForSpellEffect(jewelryEnchant) > 0.0F) {
/*     */         
/*  93 */         activeJewelry++;
/*     */         
/*  95 */         totalPower += (bodyItem.getCurrentQualityLevel() + bodyItem.getBonusForSpellEffect(jewelryEnchant)) / 2.0F;
/*     */       } 
/*     */     } 
/*  98 */     if (totalPower > 0.0F) {
/*     */ 
/*     */       
/* 101 */       float increase = 0.025F * activeJewelry + 0.025F * totalPower / 100.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 106 */       increase *= 2.0F / (activeJewelry + 1);
/* 107 */       damageMultiplier *= 1.0F + increase;
/*     */     } 
/*     */     
/* 110 */     return damageMultiplier;
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
/*     */   public static float getJewelryResistModifier(Creature defender, byte woundType) {
/* 122 */     byte jewelryEnchant = 0;
/* 123 */     switch (woundType) {
/*     */       
/*     */       case 4:
/* 126 */         jewelryEnchant = 7;
/*     */         break;
/*     */       case 5:
/* 129 */         jewelryEnchant = 8;
/*     */         break;
/*     */       case 8:
/* 132 */         jewelryEnchant = 6;
/*     */         break;
/*     */       case 10:
/* 135 */         jewelryEnchant = 5;
/*     */         break;
/*     */     } 
/*     */     
/* 139 */     if (jewelryEnchant == 0) {
/* 140 */       return 1.0F;
/*     */     }
/*     */     
/* 143 */     Item[] bodyItems = defender.getBody().getContainersAndWornItems();
/* 144 */     float damageMultiplier = 1.0F;
/* 145 */     float totalPower = 0.0F;
/* 146 */     int activeJewelry = 0;
/* 147 */     for (Item bodyItem : bodyItems) {
/*     */ 
/*     */       
/* 150 */       if (bodyItem.isEnchantableJewelry() && bodyItem.getBonusForSpellEffect(jewelryEnchant) > 0.0F) {
/*     */         
/* 152 */         activeJewelry++;
/*     */         
/* 154 */         totalPower += (bodyItem.getCurrentQualityLevel() + bodyItem.getBonusForSpellEffect(jewelryEnchant)) / 2.0F;
/*     */       } 
/*     */     } 
/* 157 */     if (totalPower > 0.0F) {
/*     */ 
/*     */       
/* 160 */       float reduction = 0.025F * activeJewelry + 0.05F * totalPower / 100.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 165 */       reduction *= 2.0F / (activeJewelry + 1);
/* 166 */       damageMultiplier *= 1.0F - reduction;
/*     */     } 
/*     */     
/* 169 */     return damageMultiplier;
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
/*     */   public static SpellEffect hasNegatingEffect(Item target, byte enchantment) {
/* 182 */     if (target.getSpellEffects() != null)
/*     */     {
/* 184 */       for (ArrayList<Byte> group : enchantGroups) {
/*     */         
/* 186 */         if (group.contains(Byte.valueOf(enchantment)))
/*     */         {
/* 188 */           for (Iterator<Byte> iterator = group.iterator(); iterator.hasNext(); ) { byte ench = ((Byte)iterator.next()).byteValue();
/*     */             
/* 190 */             if (ench != enchantment && 
/* 191 */               target.getBonusForSpellEffect(ench) > 0.0F)
/*     */             {
/* 193 */               return target.getSpellEffect(ench);
/*     */             } }
/*     */         
/*     */         }
/*     */       } 
/*     */     }
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canEnchantDemise(Creature performer, Item target) {
/* 210 */     if (!Spell.mayBeEnchanted(target)) {
/*     */       
/* 212 */       performer.getCommunicator().sendNormalServerMessage("The spell will not work on that.", (byte)3);
/*     */       
/* 214 */       return false;
/*     */     } 
/* 216 */     if (target.enchantment != 0) {
/*     */       
/* 218 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already enchanted.", (byte)3);
/*     */       
/* 220 */       return false;
/*     */     } 
/* 222 */     if (target.getCurrentQualityLevel() < 70.0F) {
/*     */       
/* 224 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 225 */           .getName() + " is of too low quality for this enchantment.", (byte)3);
/* 226 */       return false;
/*     */     } 
/* 228 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendInvalidTargetMessage(Creature performer, Spell spell) {
/* 239 */     StringBuilder str = new StringBuilder();
/* 240 */     str.append("You can only target ");
/*     */ 
/*     */     
/* 243 */     ArrayList<String> targets = new ArrayList<>();
/* 244 */     if (spell.isTargetArmour())
/* 245 */       targets.add("armour"); 
/* 246 */     if (spell.isTargetWeapon())
/* 247 */       targets.add("weapons"); 
/* 248 */     if (spell.isTargetJewelry())
/* 249 */       targets.add("jewelry"); 
/* 250 */     if (spell.isTargetPendulum()) {
/* 251 */       targets.add("pendulums");
/*     */     }
/*     */     
/* 254 */     if (targets.size() == 0) {
/* 255 */       logger.warning("Spell " + spell.getName() + " has no valid targets.");
/* 256 */     } else if (targets.size() == 1) {
/* 257 */       str.append(targets.get(0));
/* 258 */     } else if (targets.size() == 2) {
/* 259 */       str.append(targets.get(0)).append(" or ").append(targets.get(1));
/*     */     } else {
/*     */       
/* 262 */       StringBuilder allTargets = new StringBuilder();
/* 263 */       for (String target : targets) {
/*     */         
/* 265 */         if (allTargets.length() > 0)
/* 266 */           allTargets.append(", "); 
/* 267 */         allTargets.append(target);
/*     */       } 
/* 269 */       str.append(allTargets);
/*     */     } 
/*     */     
/* 272 */     str.append(".");
/* 273 */     performer.getCommunicator().sendNormalServerMessage(str.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendCannotBeEnchantedMessage(Creature performer) {
/* 282 */     performer.getCommunicator().sendNormalServerMessage("The spell will not work on that.", (byte)3);
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
/*     */   public static void sendNegatingEffectMessage(String name, Creature performer, Item target, SpellEffect negatingEffect) {
/* 296 */     performer.getCommunicator().sendNormalServerMessage(String.format("The %s is already enchanted with %s, which would negate the effect of %s.", new Object[] { target
/*     */             
/* 298 */             .getName(), negatingEffect.getName(), name }), (byte)3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initializeEnchantGroups() {
/* 309 */     ArrayList<Byte> speedEffectGroup = new ArrayList<>();
/* 310 */     speedEffectGroup.add(Byte.valueOf((byte)47));
/* 311 */     speedEffectGroup.add(Byte.valueOf((byte)16));
/* 312 */     speedEffectGroup.add(Byte.valueOf((byte)32));
/* 313 */     enchantGroups.add(speedEffectGroup);
/*     */ 
/*     */     
/* 316 */     ArrayList<Byte> skillgainEffectGroup = new ArrayList<>();
/* 317 */     skillgainEffectGroup.add(Byte.valueOf((byte)47));
/* 318 */     skillgainEffectGroup.add(Byte.valueOf((byte)13));
/* 319 */     enchantGroups.add(skillgainEffectGroup);
/*     */ 
/*     */     
/* 322 */     ArrayList<Byte> weaponDamageEffectGroup = new ArrayList<>();
/* 323 */     weaponDamageEffectGroup.add(Byte.valueOf((byte)45));
/* 324 */     weaponDamageEffectGroup.add(Byte.valueOf((byte)63));
/* 325 */     weaponDamageEffectGroup.add(Byte.valueOf((byte)14));
/* 326 */     weaponDamageEffectGroup.add(Byte.valueOf((byte)33));
/* 327 */     weaponDamageEffectGroup.add(Byte.valueOf((byte)26));
/* 328 */     weaponDamageEffectGroup.add(Byte.valueOf((byte)18));
/* 329 */     weaponDamageEffectGroup.add(Byte.valueOf((byte)27));
/* 330 */     enchantGroups.add(weaponDamageEffectGroup);
/*     */ 
/*     */     
/* 333 */     ArrayList<Byte> armourEffectGroup = new ArrayList<>();
/* 334 */     armourEffectGroup.add(Byte.valueOf((byte)17));
/* 335 */     armourEffectGroup.add(Byte.valueOf((byte)46));
/* 336 */     enchantGroups.add(armourEffectGroup);
/*     */ 
/*     */     
/* 339 */     ArrayList<Byte> mailboxEffectGroup = new ArrayList<>();
/* 340 */     mailboxEffectGroup.add(Byte.valueOf((byte)20));
/* 341 */     mailboxEffectGroup.add(Byte.valueOf((byte)44));
/* 342 */     enchantGroups.add(mailboxEffectGroup);
/*     */ 
/*     */     
/* 345 */     ArrayList<Byte> jewelryEffectGroup = new ArrayList<>();
/* 346 */     jewelryEffectGroup.add(Byte.valueOf((byte)29));
/* 347 */     jewelryEffectGroup.add(Byte.valueOf((byte)1));
/* 348 */     jewelryEffectGroup.add(Byte.valueOf((byte)5));
/* 349 */     jewelryEffectGroup.add(Byte.valueOf((byte)4));
/* 350 */     jewelryEffectGroup.add(Byte.valueOf((byte)8));
/* 351 */     jewelryEffectGroup.add(Byte.valueOf((byte)2));
/* 352 */     jewelryEffectGroup.add(Byte.valueOf((byte)6));
/* 353 */     jewelryEffectGroup.add(Byte.valueOf((byte)3));
/* 354 */     jewelryEffectGroup.add(Byte.valueOf((byte)7));
/* 355 */     enchantGroups.add(jewelryEffectGroup);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\EnchantUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */