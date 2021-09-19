/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.combat.ArmourTemplate;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffects;
/*     */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.spells.SpellEffect;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemBonus
/*     */   implements TimeConstants
/*     */ {
/*  25 */   private static final Logger logger = Logger.getLogger(ItemBonus.class.getName());
/*  26 */   private static final ConcurrentHashMap<Long, Map<Integer, ItemBonus>> playerBonuses = new ConcurrentHashMap<>();
/*     */   
/*     */   private final Item itemOne;
/*     */   
/*     */   private Item itemTwo;
/*     */   
/*     */   private final long playerId;
/*     */   
/*     */   private final int bonusTypeId;
/*     */   
/*     */   private float bonusValue;
/*     */   
/*     */   private float bonus2Value;
/*     */   
/*     */   private boolean stacks = false;
/*     */   
/*     */   private static final long decayTime = 28800L;
/*     */   
/*     */   private ItemBonus(Item item, long playerid, int bonusType, float value) {
/*  45 */     this(item, playerid, bonusType, value, false);
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
/*     */   private ItemBonus(Item item, long playerid, int bonusType, float value, boolean isStacking) {
/*  59 */     this.itemOne = item;
/*  60 */     this.playerId = playerid;
/*  61 */     this.bonusTypeId = bonusType;
/*  62 */     this.bonusValue = value;
/*  63 */     setStacking(isStacking);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void calcAndAddBonus(Item item, Creature creature) {
/*  74 */     SpellEffectsEnum bonusType = SpellEffectsEnum.getEnumForItemTemplateId(item.getTemplateId(), item.getData1());
/*  75 */     if (bonusType != null) {
/*     */       
/*  77 */       float value = getBonusValueForItem(item);
/*  78 */       if (value > 0.0F)
/*     */       {
/*  80 */         addBonus(item, creature, bonusType.getTypeId(), value, getStacking(item));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void checkDepleteAndRename(Item usedItem, Creature owner) {
/*  87 */     if (usedItem.isRiftLoot() && isTimed(usedItem))
/*     */     {
/*  89 */       if (WurmCalendar.currentTime - usedItem.getLastMaintained() > 28800L) {
/*     */         
/*  91 */         SpellEffectsEnum bonusType = SpellEffectsEnum.getEnumForItemTemplateId(usedItem.getTemplateId(), usedItem.getData1());
/*  92 */         if (bonusType != null) {
/*     */           
/*  94 */           ItemBonus cbonus = getItemBonusObject(owner.getWurmId(), bonusType.getTypeId());
/*  95 */           if (cbonus != null) {
/*     */             
/*  97 */             if (usedItem.getAuxData() <= 0) {
/*     */               
/*  99 */               removeBonus(usedItem, owner);
/*     */               return;
/*     */             } 
/* 102 */             usedItem.setAuxData((byte)(usedItem.getAuxData() - 1));
/* 103 */             usedItem.setLastMaintained(WurmCalendar.currentTime);
/* 104 */             rename(usedItem);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void rename(Item usedItem) {
/* 113 */     if (usedItem.getAuxData() > 0 && !usedItem.getActualName().toLowerCase().contains("used")) {
/*     */       
/* 115 */       usedItem.setName("used " + usedItem.getActualName());
/*     */     }
/* 117 */     else if (usedItem.getAuxData() <= 0) {
/*     */       
/* 119 */       if (usedItem.getActualName().toLowerCase().contains("used")) {
/*     */         
/* 121 */         usedItem.setName(usedItem.getActualName().replace("used", "depleted"));
/*     */       }
/* 123 */       else if (!usedItem.getActualName().toLowerCase().contains("depleted")) {
/* 124 */         usedItem.setName("depleted " + usedItem.getActualName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final void addBonus(Item item, Creature creature, int bonusType, float value, boolean isStacking) {
/* 130 */     if (isTimed(item) && item.getAuxData() <= 0)
/*     */       return; 
/* 132 */     Map<Integer, ItemBonus> curr = playerBonuses.get(Long.valueOf(creature.getWurmId()));
/* 133 */     if (curr == null) {
/*     */       
/* 135 */       curr = new ConcurrentHashMap<>();
/* 136 */       playerBonuses.put(Long.valueOf(creature.getWurmId()), curr);
/*     */     } 
/* 138 */     ItemBonus cbonus = curr.get(Integer.valueOf(bonusType));
/* 139 */     if (cbonus == null) {
/*     */       
/* 141 */       cbonus = new ItemBonus(item, creature.getWurmId(), bonusType, value, isStacking);
/* 142 */       curr.put(Integer.valueOf(bonusType), cbonus);
/*     */     }
/*     */     else {
/*     */       
/* 146 */       cbonus.setItemTwo(item);
/* 147 */       cbonus.setBonus2Value(value);
/*     */     } 
/* 149 */     cbonus.sendNewBonusToClient(item, creature);
/* 150 */     checkDepleteAndRename(item, creature);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendNewBonusToClient(Item item, Creature creature) {
/* 155 */     if (item != null) {
/*     */       
/* 157 */       if (!isTimed(item) || getSecondsLeft() > 0) {
/*     */         
/* 159 */         SpellEffectsEnum senum = SpellEffectsEnum.getEnumForItemTemplateId(item.getTemplateId(), item.getData1());
/* 160 */         creature.getCommunicator().sendAddSpellEffect(senum, getSecondsLeft(), getBonusVal(0.0F));
/*     */       } 
/*     */     } else {
/*     */       
/* 164 */       logger.log(Level.INFO, "Item was null for " + creature.getName(), new Exception());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void sendAllItemBonusToPlayer(Player player) {
/* 174 */     Map<Integer, ItemBonus> curr = playerBonuses.get(Long.valueOf(player.getWurmId()));
/* 175 */     if (curr != null)
/*     */     {
/* 177 */       for (ItemBonus bonus : curr.values()) {
/* 178 */         bonus.sendNewBonusToClient(bonus.getItemOne(), player);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final void sendRemoveBonusToClient(Item item, Creature creature) {
/* 184 */     SpellEffectsEnum senum = SpellEffectsEnum.getEnumForItemTemplateId(item.getTemplateId(), item.getData1());
/* 185 */     creature.getCommunicator().sendRemoveSpellEffect(senum.getId(), senum);
/* 186 */     byte debuff = SpellEffectsEnum.getDebuffForEnum(senum);
/* 187 */     if (debuff != 0) {
/*     */       
/* 189 */       SpellEffects effs = creature.getSpellEffects();
/*     */       
/* 191 */       SpellEffect edebuff = effs.getSpellEffect(debuff);
/* 192 */       if (edebuff == null) {
/*     */         
/* 194 */         edebuff = new SpellEffect(creature.getWurmId(), debuff, 100.0F, 300, (byte)10, (byte)1, true);
/*     */         
/* 196 */         effs.addSpellEffect(edebuff);
/*     */       } else {
/*     */         
/* 199 */         edebuff.setTimeleft(300);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void removeBonus(Item item, Creature creature) {
/* 206 */     Map<Integer, ItemBonus> curr = playerBonuses.get(Long.valueOf(creature.getWurmId()));
/* 207 */     if (curr == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 212 */     SpellEffectsEnum senum = SpellEffectsEnum.getEnumForItemTemplateId(item.getTemplateId(), item.getData1());
/* 213 */     ItemBonus cbonus = curr.get(Integer.valueOf(senum.getTypeId()));
/* 214 */     if (cbonus != null) {
/*     */       
/* 216 */       if (cbonus.getItemTwo() == item) {
/*     */ 
/*     */         
/* 219 */         cbonus.setItemTwo(null);
/* 220 */         cbonus.setBonus2Value(0.0F);
/* 221 */         cbonus.sendNewBonusToClient(item, creature);
/*     */         return;
/*     */       } 
/* 224 */       if (cbonus.getItemOne() == item)
/*     */       {
/* 226 */         if (cbonus.getItemTwo() != null) {
/*     */ 
/*     */           
/* 229 */           ItemBonus newBonus = new ItemBonus(cbonus.getItemTwo(), creature.getWurmId(), cbonus.getBonusType(), cbonus.getItemTwoBonusValue(0.0F), cbonus.isStacking());
/* 230 */           curr.put(Integer.valueOf(cbonus.getBonusType()), newBonus);
/* 231 */           newBonus.sendNewBonusToClient(item, creature);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 236 */           curr.remove(Integer.valueOf(cbonus.getBonusType()));
/* 237 */           cbonus.sendRemoveBonusToClient(item, creature);
/*     */         } 
/*     */       }
/*     */     } else {
/*     */       
/* 242 */       logger.log(Level.INFO, "Failed to remove bonus for " + item.getName() + " for " + creature.getName() + " although it should be registered.");
/* 243 */     }  if (curr.isEmpty()) {
/* 244 */       playerBonuses.remove(Long.valueOf(creature.getWurmId()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void clearBonuses(long playerid) {
/* 255 */     playerBonuses.remove(Long.valueOf(playerid));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final float getBonus(long playerid, int bonusType) {
/* 260 */     return getBonus(playerid, bonusType, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final float getBonus(long playerid, int bonusType, float damageDealt) {
/* 265 */     ItemBonus bonus = getItemBonusObject(playerid, bonusType);
/* 266 */     if (bonus == null)
/* 267 */       return 0.0F; 
/* 268 */     return bonus.getBonusVal(damageDealt);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final ItemBonus getItemBonusObject(long playerId, int bonusType) {
/* 273 */     Map<Integer, ItemBonus> curr = playerBonuses.get(Long.valueOf(playerId));
/* 274 */     if (curr == null)
/*     */     {
/* 276 */       return null;
/*     */     }
/* 278 */     return curr.get(Integer.valueOf(bonusType));
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getBonusVal(float damageDealt) {
/* 283 */     if (isStacking())
/*     */     {
/* 285 */       return getItemOneBonusValue(damageDealt) + getItemTwoBonusValue(damageDealt);
/*     */     }
/* 287 */     if (getItemOneBonusValue(0.0F) > getItemTwoBonusValue(0.0F))
/* 288 */       return getItemOneBonusValue(damageDealt); 
/* 289 */     return getItemTwoBonusValue(damageDealt);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getSecondsLeft() {
/* 294 */     if (isStacking()) {
/* 295 */       return Math.min(getSeconds1Left(), getSeconds2Left());
/*     */     }
/*     */     
/* 298 */     if (getItemOneBonusValue(0.0F) > getItemTwoBonusValue(0.0F))
/* 299 */       return getSeconds1Left(); 
/* 300 */     return getSeconds2Left();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItemOne() {
/* 306 */     return this.itemOne;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/* 311 */     return this.playerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBonusType() {
/* 316 */     return this.bonusTypeId;
/*     */   }
/*     */ 
/*     */   
/*     */   private float getItemOneBonusValue(float damageDealt) {
/* 321 */     if (damageDealt > 0.0F)
/* 322 */       this.itemOne.setDamage(this.itemOne.getDamage() + damageDealt); 
/* 323 */     return this.bonusValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private float getItemTwoBonusValue(float damageDealt) {
/* 328 */     if (this.itemTwo != null && damageDealt > 0.0F)
/* 329 */       this.itemTwo.setDamage(this.itemTwo.getDamage() + damageDealt); 
/* 330 */     return this.bonus2Value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBonus2Value(float bonus2Value) {
/* 335 */     this.bonus2Value = bonus2Value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemTwo() {
/* 340 */     return this.itemTwo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemTwo(Item item2) {
/* 345 */     this.itemTwo = item2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStacking() {
/* 350 */     return this.stacks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStacking(boolean stacks) {
/* 355 */     this.stacks = stacks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getBonusValueForItem(Item item) {
/* 364 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isTimed(Item item) {
/* 375 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean getStacking(Item item) {
/* 385 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getSeconds1Left() {
/* 390 */     if (this.itemOne != null && isTimed(this.itemOne))
/* 391 */       return this.itemOne.getAuxData() * 3600; 
/* 392 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getSeconds2Left() {
/* 398 */     if (this.itemTwo != null && isTimed(this.itemTwo))
/* 399 */       return this.itemTwo.getAuxData() * 3600; 
/* 400 */     return -1;
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
/*     */   public static final float getGlanceBonusFor(ArmourTemplate.ArmourType armourType, byte woundType, Item weapon, Creature creature) {
/* 413 */     float bonus = 0.0F;
/* 414 */     if (armourType == ArmourTemplate.ARMOUR_TYPE_CLOTH) {
/* 415 */       if (woundType == 0)
/* 416 */         bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_COTTON_CRUSHING.getTypeId()); 
/* 417 */       if (woundType == 1)
/* 418 */         bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_COTTON_SLASHING.getTypeId()); 
/* 419 */     } else if (armourType == ArmourTemplate.ARMOUR_TYPE_LEATHER) {
/* 420 */       if (weapon.isTwoHanded())
/* 421 */         bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_LEATHER_TWOHANDED.getTypeId()); 
/* 422 */     } else if (armourType == ArmourTemplate.ARMOUR_TYPE_STUDDED && 
/* 423 */       weapon.isTwoHanded()) {
/* 424 */       bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_STUDDED_TWOHANDED.getTypeId());
/*     */     } 
/*     */     
/* 427 */     return bonus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getFaceDamReductionBonus(Creature creature) {
/* 438 */     return 1.0F - getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_FACEDAM.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getAreaSpellReductionBonus(Creature creature) {
/* 449 */     return 1.0F - getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_AREASPELL_DAMREDUCT.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getAreaSpellDamageIncreaseBonus(Creature creature) {
/* 460 */     return 1.0F + getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_AREA_SPELL.getTypeId());
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
/*     */   public static final float getWeaponDamageIncreaseBonus(Creature creature, Item weapon) {
/* 472 */     float bonus = 0.0F;
/* 473 */     return 1.0F + bonus;
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
/*     */   public static final float getArcheryPenaltyReduction(Creature creature) {
/* 487 */     if (creature.getArmourLimitingFactor() <= -0.15F)
/* 488 */       return 1.0F + getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_HEAVY_ARCHERY.getTypeId()); 
/* 489 */     return 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getStaminaReductionBonus(Creature creature) {
/* 500 */     return 1.0F - getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_STAMINA.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getDodgeBonus(Creature creature) {
/* 511 */     return 1.0F + getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_DODGE.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getCRBonus(Creature creature) {
/* 522 */     return getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_CR.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getSpellResistBonus(Creature creature) {
/* 533 */     return getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_SPELLRESIST.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getHealingBonus(Creature creature) {
/* 544 */     return getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_HEALING.getTypeId());
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
/*     */   public static final float getSkillGainBonus(Creature creature, int skillId) {
/* 556 */     return 0.0F;
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
/*     */   public static final float getKillEfficiencyBonus(Creature creature) {
/* 569 */     return 1.0F + getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_NECKLACE_SKILLEFF.getTypeId(), 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getImproveSkillMaxBonus(Creature creature) {
/* 580 */     return 1.0F + getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_NECKLACE_SKILLMAX.getTypeId());
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
/*     */   public static final float getDrownDamReduction(Creature creature) {
/* 592 */     return 1.0F - getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_SWIMMING.getTypeId(), 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getStealthBonus(Creature creature) {
/* 603 */     return 1.0F + getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_STEALTH.getTypeId(), 0.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getDetectionBonus(Creature creature) {
/* 614 */     return 50.0F * getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_RING_DETECTION.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getParryBonus(Creature creature, Item weapon) {
/* 625 */     float bonus = 0.0F;
/* 626 */     return 1.0F - bonus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getWeaponSpellDamageIncreaseBonus(long ownerid) {
/* 637 */     if (ownerid > 0L)
/* 638 */       return 1.0F + getBonus(ownerid, SpellEffectsEnum.ITEM_BRACELET_ENCHANTDAM.getTypeId()); 
/* 639 */     return 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getHurtingReductionBonus(Creature creature) {
/* 650 */     return 1.0F - getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_NECKLACE_HURTING.getTypeId(), 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getFocusBonus(Creature creature) {
/* 661 */     return 1.0F - getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_NECKLACE_FOCUS.getTypeId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getReplenishBonus(Creature creature) {
/* 672 */     return 1.0F - getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_NECKLACE_REPLENISH.getTypeId());
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
/*     */   public static final float getDamReductionBonusFor(ArmourTemplate.ArmourType armourType, byte woundType, Item weapon, Creature creature) {
/* 686 */     float bonus = 0.0F;
/* 687 */     if (armourType == ArmourTemplate.ARMOUR_TYPE_CLOTH) {
/* 688 */       if (woundType == 1)
/* 689 */         bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_COTTON_SLASHDAM.getTypeId(), 0.1F); 
/* 690 */     } else if (armourType == ArmourTemplate.ARMOUR_TYPE_LEATHER) {
/* 691 */       if (woundType == 0)
/* 692 */         bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_LEATHER_CRUSHDAM.getTypeId(), 0.1F); 
/* 693 */     } else if (armourType == ArmourTemplate.ARMOUR_TYPE_CHAIN) {
/* 694 */       if (woundType == 1)
/* 695 */         bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_CHAIN_SLASHDAM.getTypeId(), 0.1F); 
/* 696 */       if (woundType == 2)
/* 697 */         bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_CHAIN_PIERCEDAM.getTypeId(), 0.1F); 
/*     */     } 
/* 699 */     if (weapon.getEnchantmentDamageType() > 0)
/* 700 */       bonus += getBonus(creature.getWurmId(), SpellEffectsEnum.ITEM_ENCHANT_DAMREDUCT.getTypeId(), 0.1F); 
/* 701 */     return 1.0F - bonus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float getBashDodgeBonusFor(Creature creature) {
/* 712 */     return 1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\ItemBonus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */