/*     */ package com.wurmonline.server.combat;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Weapon
/*     */   implements MiscConstants
/*     */ {
/*     */   private final int itemid;
/*     */   private final float damage;
/*     */   private final float speed;
/*     */   private final float critchance;
/*     */   private final int reach;
/*     */   private final int weightGroup;
/*     */   private final float parryPercent;
/*     */   private final double skillPenalty;
/*  51 */   private static float randomizer = 0.0F;
/*  52 */   private static final Map<Integer, Weapon> weapons = new HashMap<>();
/*  53 */   private static Weapon toCheck = null;
/*     */   
/*     */   private boolean damagedByMetal = false;
/*     */   private static final float critChanceMod = 5.0F;
/*  57 */   private static final float strengthModifier = Servers.localServer.isChallengeOrEpicServer() ? 1000.0F : 300.0F;
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
/*     */   
/*     */   public Weapon(int _itemid, float _damage, float _speed, float _critchance, int _reach, int _weightGroup, float _parryPercent, double _skillPenalty) {
/*  81 */     this.itemid = _itemid;
/*  82 */     this.damage = _damage;
/*  83 */     this.speed = _speed;
/*  84 */     this.critchance = _critchance / 5.0F;
/*  85 */     this.reach = _reach;
/*  86 */     this.weightGroup = _weightGroup;
/*  87 */     this.parryPercent = _parryPercent;
/*  88 */     this.skillPenalty = _skillPenalty;
/*  89 */     weapons.put(Integer.valueOf(this.itemid), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float getBaseDamageForWeapon(Item weapon) {
/*  94 */     if (weapon == null)
/*  95 */       return 0.0F; 
/*  96 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/*  97 */     if (toCheck != null)
/*  98 */       return toCheck.damage; 
/*  99 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final double getModifiedDamageForWeapon(Item weapon, Skill strength) {
/* 104 */     return getModifiedDamageForWeapon(weapon, strength, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final double getModifiedDamageForWeapon(Item weapon, Skill strength, boolean fullDam) {
/* 109 */     if (fullDam) {
/* 110 */       randomizer = 1.0F;
/*     */     } else {
/* 112 */       randomizer = (50.0F + Server.rand.nextFloat() * 50.0F) / 100.0F;
/* 113 */     }  double damreturn = 1.0D;
/* 114 */     if (weapon.isBodyPartAttached()) {
/* 115 */       damreturn = getBaseDamageForWeapon(weapon);
/*     */     } else {
/* 117 */       damreturn = (getBaseDamageForWeapon(weapon) * weapon.getCurrentQualityLevel() / 100.0F);
/* 118 */     }  damreturn *= 1.0D + strength.getKnowledge(0.0D) / strengthModifier;
/* 119 */     damreturn *= randomizer;
/* 120 */     return damreturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float getBaseSpeedForWeapon(Item weapon) {
/* 125 */     if (weapon == null || weapon.isBodyPartAttached()) {
/* 126 */       return 1.0F;
/*     */     }
/* 128 */     float materialMod = 1.0F;
/* 129 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 131 */       switch (weapon.getMaterial()) {
/*     */         
/*     */         case 57:
/* 134 */           materialMod = 0.9F;
/*     */           break;
/*     */         case 7:
/* 137 */           materialMod = 1.05F;
/*     */           break;
/*     */         case 67:
/* 140 */           materialMod = 0.95F;
/*     */           break;
/*     */         case 34:
/* 143 */           materialMod = 0.96F;
/*     */           break;
/*     */         case 13:
/* 146 */           materialMod = 0.95F;
/*     */           break;
/*     */         case 96:
/* 149 */           materialMod = 1.025F;
/*     */           break;
/*     */       } 
/*     */     
/*     */     }
/* 154 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/* 155 */     if (toCheck != null)
/* 156 */       return toCheck.speed * materialMod; 
/* 157 */     return 20.0F * materialMod;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float getRarityCritMod(byte rarity) {
/* 162 */     switch (rarity) {
/*     */       
/*     */       case 0:
/* 165 */         return 1.0F;
/*     */       case 1:
/* 167 */         return 1.1F;
/*     */       case 2:
/* 169 */         return 1.3F;
/*     */       case 3:
/* 171 */         return 1.5F;
/*     */     } 
/* 173 */     return 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getCritChanceForWeapon(Item weapon) {
/* 179 */     if (weapon == null || weapon.isBodyPartAttached())
/* 180 */       return 0.01F; 
/* 181 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/* 182 */     if (toCheck != null)
/* 183 */       return toCheck.critchance * getRarityCritMod(weapon.getRarity()); 
/* 184 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getReachForWeapon(Item weapon) {
/* 189 */     if (weapon == null || weapon.isBodyPartAttached())
/* 190 */       return 1; 
/* 191 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/* 192 */     if (toCheck != null)
/* 193 */       return toCheck.reach; 
/* 194 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getWeightGroupForWeapon(Item weapon) {
/* 199 */     if (weapon == null || weapon.isBodyPartAttached())
/* 200 */       return 1; 
/* 201 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/* 202 */     if (toCheck != null)
/* 203 */       return toCheck.weightGroup; 
/* 204 */     return 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final double getSkillPenaltyForWeapon(Item weapon) {
/* 209 */     if (weapon == null || weapon.isBodyPartAttached())
/* 210 */       return 0.0D; 
/* 211 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/* 212 */     if (toCheck != null)
/* 213 */       return toCheck.skillPenalty; 
/* 214 */     return 7.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float getWeaponParryPercent(Item weapon) {
/* 219 */     if (weapon == null)
/* 220 */       return 0.0F; 
/* 221 */     if (weapon.isBodyPart())
/* 222 */       return 0.0F; 
/* 223 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/* 224 */     if (toCheck != null)
/* 225 */       return toCheck.parryPercent; 
/* 226 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDamagedByMetal(boolean aDamagedByMetal) {
/* 237 */     this.damagedByMetal = aDamagedByMetal;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isWeaponDamByMetal(Item weapon) {
/* 242 */     if (weapon == null)
/* 243 */       return false; 
/* 244 */     if (weapon.isBodyPart() && weapon.isBodyPartRemoved())
/* 245 */       return true; 
/* 246 */     toCheck = weapons.get(Integer.valueOf(weapon.getTemplateId()));
/* 247 */     if (toCheck != null)
/* 248 */       return toCheck.damagedByMetal; 
/* 249 */     return false;
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
/*     */   public static double getMaterialDamageBonus(byte material) {
/* 261 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*     */       
/* 263 */       switch (material) {
/*     */         
/*     */         case 56:
/* 266 */           return 1.100000023841858D;
/*     */         case 30:
/* 268 */           return 0.9900000095367432D;
/*     */         case 31:
/* 270 */           return 0.9850000143051147D;
/*     */         case 10:
/* 272 */           return 0.6499999761581421D;
/*     */         case 7:
/* 274 */           return 0.9750000238418579D;
/*     */         case 12:
/* 276 */           return 0.5D;
/*     */         case 67:
/* 278 */           return 1.0499999523162842D;
/*     */         case 34:
/* 280 */           return 0.925000011920929D;
/*     */         case 13:
/* 282 */           return 0.8999999761581421D;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/* 287 */     } else if (material == 56) {
/* 288 */       return 1.100000023841858D;
/*     */     } 
/* 290 */     return 1.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getMaterialHunterDamageBonus(byte material) {
/* 301 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 303 */       switch (material) {
/*     */         
/*     */         case 8:
/* 306 */           return 1.100000023841858D;
/*     */         case 96:
/* 308 */           return 1.0499999523162842D;
/*     */       } 
/*     */     }
/* 311 */     return 1.0D;
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
/*     */   public static double getMaterialArmourDamageBonus(byte material) {
/* 323 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 325 */       switch (material) {
/*     */         
/*     */         case 30:
/* 328 */           return 1.0499999523162842D;
/*     */         case 31:
/* 330 */           return 1.0750000476837158D;
/*     */         case 7:
/* 332 */           return 1.0499999523162842D;
/*     */         case 9:
/* 334 */           return 1.024999976158142D;
/*     */       } 
/*     */     }
/* 337 */     return 1.0D;
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
/*     */   public static float getMaterialParryBonus(byte material) {
/* 349 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 351 */       switch (material) {
/*     */         
/*     */         case 8:
/* 354 */           return 1.025F;
/*     */         case 34:
/* 356 */           return 1.05F;
/*     */       } 
/*     */     }
/* 359 */     return 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getMaterialExtraWoundMod(byte material) {
/* 370 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 372 */       switch (material) {
/*     */         
/*     */         case 10:
/* 375 */           return 0.3F;
/*     */         case 12:
/* 377 */           return 0.75F;
/*     */       } 
/*     */     }
/* 380 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte getMaterialExtraWoundType(byte material) {
/* 391 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 393 */       switch (material) {
/*     */         
/*     */         case 10:
/* 396 */           return 5;
/*     */         case 12:
/* 398 */           return 5;
/*     */       } 
/*     */     }
/* 401 */     return 5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getMaterialBashModifier(byte material) {
/* 412 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 414 */       switch (material) {
/*     */         
/*     */         case 56:
/* 417 */           return 1.0750000476837158D;
/*     */         case 30:
/* 419 */           return 1.0499999523162842D;
/*     */         case 31:
/* 421 */           return 1.024999976158142D;
/*     */         case 10:
/* 423 */           return 0.8999999761581421D;
/*     */         case 57:
/* 425 */           return 1.100000023841858D;
/*     */         case 7:
/* 427 */           return 1.100000023841858D;
/*     */         case 12:
/* 429 */           return 1.2000000476837158D;
/*     */         case 67:
/* 431 */           return 1.0750000476837158D;
/*     */         case 8:
/* 433 */           return 1.100000023841858D;
/*     */         case 9:
/* 435 */           return 1.0499999523162842D;
/*     */         case 34:
/* 437 */           return 0.8999999761581421D;
/*     */         case 13:
/* 439 */           return 0.8500000238418579D;
/*     */         case 96:
/* 441 */           return 1.100000023841858D;
/*     */       } 
/*     */     }
/* 444 */     return 1.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\Weapon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */