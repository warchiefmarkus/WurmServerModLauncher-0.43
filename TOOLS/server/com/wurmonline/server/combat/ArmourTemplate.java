/*      */ package com.wurmonline.server.combat;
/*      */ 
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ArmourTemplate
/*      */   implements MiscConstants
/*      */ {
/*   24 */   private static final Logger logger = Logger.getLogger(ArmourTemplate.class.getName());
/*      */   
/*   26 */   public static HashMap<Integer, ArmourTemplate> armourTemplates = new HashMap<>();
/*   27 */   public static ArrayList<ProtectionSlot> protectionSlots = new ArrayList<>();
/*      */   protected int templateId;
/*      */   
/*      */   public static ArmourTemplate getArmourTemplate(int templateId) {
/*   31 */     if (armourTemplates.containsKey(Integer.valueOf(templateId)))
/*      */     {
/*   33 */       return armourTemplates.get(Integer.valueOf(templateId));
/*      */     }
/*   35 */     logger.warning(String.format("Item template id %s has no ArmourTemplate, but one was requested.", new Object[] { Integer.valueOf(templateId) }));
/*   36 */     return null;
/*      */   }
/*      */   protected ArmourType armourType; protected float moveModifier;
/*      */   
/*      */   public static ArmourTemplate getArmourTemplate(Item item) {
/*   41 */     return getArmourTemplate(item.getTemplateId());
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setArmourMoveModifier(int templateId, float value) {
/*   46 */     ArmourTemplate template = getArmourTemplate(templateId);
/*   47 */     if (template != null) {
/*      */       
/*   49 */       template.setMoveModifier(value);
/*      */     }
/*      */     else {
/*      */       
/*   53 */       logger.warning(String.format("Item template id %s has no ArmourTemplate, but one was requested.", new Object[] { Integer.valueOf(templateId) }));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ProtectionSlot
/*      */   {
/*      */     byte armourPosition;
/*      */ 
/*      */     
/*   65 */     ArrayList<Byte> bodySlots = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ProtectionSlot(byte armourPosition, byte[] bodyPositions) {
/*   76 */       this.armourPosition = armourPosition;
/*   77 */       for (byte bodyPos : bodyPositions)
/*      */       {
/*   79 */         this.bodySlots.add(Byte.valueOf(bodyPos));
/*      */       }
/*   81 */       ArmourTemplate.protectionSlots.add(this);
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
/*      */   public static void initializeProtectionSlots() {
/*   93 */     new ProtectionSlot((byte)1, new byte[] { 17 });
/*   94 */     new ProtectionSlot((byte)29, new byte[] { 18, 19, 20 });
/*   95 */     new ProtectionSlot((byte)2, new byte[] { 21, 27, 26, 32, 23, 24, 25, 22 });
/*      */ 
/*      */     
/*   98 */     new ProtectionSlot((byte)3, new byte[] { 5, 9 });
/*   99 */     new ProtectionSlot((byte)4, new byte[] { 6, 10 });
/*  100 */     new ProtectionSlot((byte)34, new byte[] { 7, 11, 8, 12 });
/*      */     
/*  102 */     new ProtectionSlot((byte)15, new byte[0]);
/*  103 */     new ProtectionSlot((byte)16, new byte[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ArmourType
/*      */   {
/*  114 */     protected HashMap<Byte, Float> armourEffectiveness = new HashMap<>();
/*  115 */     protected HashMap<Byte, Float> glanceRates = new HashMap<>();
/*      */     
/*      */     protected String name;
/*      */     
/*      */     protected float baseDamageReduction;
/*      */     
/*      */     protected float limitFactor;
/*      */     
/*      */     protected float arrowGlance;
/*  124 */     protected float creatureGlanceRate = 0.0F;
/*  125 */     protected byte creatureGlanceBonusWoundType = -1;
/*  126 */     protected float creatureBonusWoundIncrease = 0.0F;
/*      */ 
/*      */     
/*      */     public ArmourType(String name, float baseDamageReduction, float limitFactor, float arrowGlance) {
/*  130 */       this.name = name;
/*  131 */       this.baseDamageReduction = baseDamageReduction;
/*  132 */       this.limitFactor = limitFactor;
/*  133 */       this.arrowGlance = arrowGlance;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() {
/*  138 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float getBaseDR() {
/*  147 */       return this.baseDamageReduction;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setBaseDR(float newBaseDR) {
/*  152 */       this.baseDamageReduction = newBaseDR;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getLimitFactor() {
/*  157 */       return this.limitFactor;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getArrowGlance() {
/*  162 */       return this.arrowGlance;
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
/*      */     public float getEffectiveness(byte woundType) {
/*  176 */       if (this.armourEffectiveness.containsKey(Byte.valueOf(woundType)))
/*      */       {
/*  178 */         return ((Float)this.armourEffectiveness.get(Byte.valueOf(woundType))).floatValue();
/*      */       }
/*  180 */       ArmourTemplate.logger.warning(String.format("No armour effectiveness set for wound type %s against %s.", new Object[] { Byte.valueOf(woundType), getName() }));
/*      */       
/*  182 */       return 1.0F;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setEffectiveness(byte woundType, float effectiveness) {
/*  187 */       this.armourEffectiveness.put(Byte.valueOf(woundType), Float.valueOf(effectiveness));
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
/*      */     public float getGlanceRate(byte woundType, byte armourMaterial) {
/*  201 */       float materialMod = ArmourTemplate.getArmourMatGlanceBonus(armourMaterial);
/*  202 */       if (this.glanceRates.containsKey(Byte.valueOf(woundType)))
/*      */       {
/*  204 */         return ((Float)this.glanceRates.get(Byte.valueOf(woundType))).floatValue() * materialMod;
/*      */       }
/*  206 */       ArmourTemplate.logger.warning(String.format("No glance rate set for wound type %s against %s.", new Object[] { Byte.valueOf(woundType), getName() }));
/*      */       
/*  208 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setGlanceRate(byte woundType, float glanceRate) {
/*  213 */       this.glanceRates.put(Byte.valueOf(woundType), Float.valueOf(glanceRate));
/*      */     }
/*      */ 
/*      */     
/*      */     public float getCreatureGlance(byte woundType, @Nonnull Item armour) {
/*  218 */       float toReturn = this.creatureGlanceRate;
/*  219 */       if (woundType == this.creatureGlanceBonusWoundType)
/*  220 */         toReturn += this.creatureBonusWoundIncrease; 
/*  221 */       toReturn = 0.05F + toReturn * (float)Server.getBuffedQualityEffect((armour.getCurrentQualityLevel() / 100.0F));
/*  222 */       return toReturn;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setCreatureGlance(float baseGlance, byte bonusWoundType, float woundBonus) {
/*  227 */       this.creatureGlanceRate = baseGlance;
/*  228 */       this.creatureGlanceBonusWoundType = bonusWoundType;
/*  229 */       this.creatureBonusWoundIncrease = woundBonus;
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
/*      */   public ArmourTemplate(int templateId, ArmourType armourType, float moveModifier) {
/*  248 */     this.templateId = templateId;
/*  249 */     this.armourType = armourType;
/*  250 */     this.moveModifier = moveModifier;
/*  251 */     armourTemplates.put(Integer.valueOf(templateId), this);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getBaseDR() {
/*  256 */     return this.armourType.getBaseDR();
/*      */   }
/*      */   
/*      */   public float getEffectiveness(byte woundType) {
/*  260 */     return this.armourType.getEffectiveness(woundType);
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
/*      */   public float getMoveModifier() {
/*  272 */     return this.moveModifier;
/*      */   }
/*      */   
/*      */   public ArmourType getArmourType() {
/*  276 */     return this.armourType;
/*      */   }
/*      */   
/*      */   public float getLimitFactor() {
/*  280 */     return this.armourType.getLimitFactor();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMoveModifier(float newMoveModifier) {
/*  285 */     this.moveModifier = newMoveModifier;
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
/*      */   public static float calculateDR(@Nonnull Item armour, byte woundType) {
/*  298 */     ArmourTemplate armourTemplate = getArmourTemplate(armour);
/*  299 */     if (armourTemplate != null) {
/*      */       
/*  301 */       float toReturn = armourTemplate.getBaseDR();
/*  302 */       toReturn += getArmourMatBonus(armour.getMaterial());
/*  303 */       toReturn *= armourTemplate.getEffectiveness(woundType);
/*  304 */       toReturn *= 1.0F + getRarityArmourBonus(armour.getRarity());
/*      */       
/*  306 */       toReturn = 0.05F + (float)(toReturn * Server.getBuffedQualityEffect((armour.getCurrentQualityLevel() / 100.0F)));
/*  307 */       return 1.0F - toReturn;
/*      */     } 
/*      */     
/*  310 */     return 1.0F;
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
/*      */   public static float calculateGlanceRate(@Nullable ArmourType armourType, @Nullable Item armour, byte woundType, float armourRating) {
/*  324 */     float toReturn = 0.0F;
/*      */     
/*  326 */     float baseArmour = 0.0F;
/*  327 */     if (armour != null) {
/*  328 */       armourType = armour.getArmourType();
/*  329 */     } else if (armourType != null) {
/*      */ 
/*      */       
/*  332 */       if (woundType == 5) {
/*  333 */         return 0.0F;
/*      */       }
/*      */       
/*  336 */       return (1.0F - armourRating) / 2.0F;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  341 */     if (armourType != null) {
/*      */       
/*  343 */       baseArmour = 0.05F;
/*  344 */       toReturn = armourType.getGlanceRate(woundType, armour.getMaterial());
/*      */     } 
/*  346 */     if (armour != null) {
/*      */       
/*  348 */       toReturn += getRarityArmourBonus(armour.getRarity());
/*  349 */       toReturn = baseArmour + toReturn * (float)Server.getBuffedQualityEffect((armour.getCurrentQualityLevel() / 100.0F));
/*      */     } else {
/*      */       
/*  352 */       toReturn *= 0.5F;
/*      */     } 
/*  354 */     return toReturn;
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
/*      */   public static float calculateCreatureGlanceRate(byte woundType, @Nonnull Item armour) {
/*  368 */     ArmourType armourType = armour.getArmourType();
/*  369 */     if (armourType != null)
/*      */     {
/*  371 */       return armourType.getCreatureGlance(woundType, armour);
/*      */     }
/*      */     
/*  374 */     return 0.0F;
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
/*      */   public static float calculateArrowGlance(@Nonnull Item armour, Item arrow) {
/*  386 */     ArmourType armourType = armour.getArmourType();
/*  387 */     if (armourType != null) {
/*      */       
/*  389 */       float toReturn = armourType.getArrowGlance();
/*  390 */       if (arrow.getTemplateId() == 454) {
/*  391 */         toReturn += 0.1F;
/*  392 */       } else if (arrow.getTemplateId() == 456) {
/*  393 */         toReturn -= 0.05F;
/*  394 */       }  toReturn *= 1.0F + getRarityArmourBonus(armour.getRarity());
/*  395 */       toReturn = (float)(toReturn + toReturn * Server.getBuffedQualityEffect((armour.getCurrentQualityLevel() / 100.0F)));
/*  396 */       return toReturn;
/*      */     } 
/*      */     
/*  399 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float getRarityArmourBonus(byte rarity) {
/*  409 */     return rarity * 0.03F;
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
/*      */   public static float getArmourMatBonus(byte armourMaterial) {
/*  421 */     if (Servers.localServer.isChallengeOrEpicServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/*  423 */       if (armourMaterial == 9)
/*  424 */         return 0.025F; 
/*  425 */       if (armourMaterial == 57 || armourMaterial == 56 || armourMaterial == 67)
/*      */       {
/*      */         
/*  428 */         return 0.05F;
/*      */       }
/*      */     } else {
/*      */       
/*  432 */       if (armourMaterial == 9)
/*  433 */         return 0.02F; 
/*  434 */       if (armourMaterial == 57 || armourMaterial == 67)
/*      */       {
/*  436 */         return 0.1F; } 
/*  437 */       if (armourMaterial == 56)
/*  438 */         return 0.05F; 
/*      */     } 
/*  440 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*      */     {
/*  442 */       switch (armourMaterial) {
/*      */         
/*      */         case 56:
/*  445 */           return 0.05F;
/*      */         case 30:
/*  447 */           return 0.01F;
/*      */         case 31:
/*  449 */           return 0.01F;
/*      */         case 10:
/*  451 */           return -0.01F;
/*      */         case 57:
/*  453 */           return 0.05F;
/*      */         case 7:
/*  455 */           return -0.01F;
/*      */         case 12:
/*  457 */           return -0.025F;
/*      */         case 67:
/*  459 */           return 0.05F;
/*      */         case 8:
/*  461 */           return -0.0075F;
/*      */         case 9:
/*  463 */           return 0.025F;
/*      */         case 34:
/*  465 */           return -0.0175F;
/*      */         case 13:
/*  467 */           return -0.02F;
/*      */         case 96:
/*  469 */           return 0.005F;
/*      */       } 
/*      */     
/*      */     }
/*  473 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float getArmourMatGlanceBonus(byte armourMaterial) {
/*  484 */     float materialMod = 1.0F;
/*  485 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*      */     {
/*  487 */       switch (armourMaterial) {
/*      */         
/*      */         case 10:
/*  490 */           materialMod = 0.98F;
/*      */           break;
/*      */         case 7:
/*  493 */           materialMod = 1.025F;
/*      */           break;
/*      */         case 8:
/*  496 */           materialMod = 1.01F;
/*      */           break;
/*      */         case 96:
/*  499 */           materialMod = 1.02F;
/*      */           break;
/*      */       } 
/*      */     
/*      */     }
/*  504 */     return materialMod;
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
/*      */   public static float getArmourDamageModFor(Item armour, byte woundType) {
/*  517 */     float toReturn = 1.0F;
/*  518 */     ArmourType armourType = armour.getArmourType();
/*  519 */     if (woundType == 0) {
/*      */       
/*  521 */       if (armourType == ARMOUR_TYPE_CLOTH || armourType == ARMOUR_TYPE_PLATE || armourType == ARMOUR_TYPE_SCALE_DRAGON) {
/*  522 */         toReturn = 4.0F;
/*      */       }
/*  524 */     } else if (woundType == 2) {
/*      */       
/*  526 */       if (armourType == ARMOUR_TYPE_PLATE) {
/*  527 */         toReturn = 4.0F;
/*  528 */       } else if (armourType == ARMOUR_TYPE_CLOTH || armourType == ARMOUR_TYPE_CHAIN) {
/*  529 */         toReturn = 2.0F;
/*      */       } 
/*  531 */     } else if (woundType == 1) {
/*      */       
/*  533 */       if (armourType == ARMOUR_TYPE_CLOTH || armourType == ARMOUR_TYPE_LEATHER || armourType == ARMOUR_TYPE_LEATHER_DRAGON || armourType == ARMOUR_TYPE_PLATE) {
/*      */         
/*  535 */         toReturn = 4.0F;
/*  536 */       } else if (armourType == ARMOUR_TYPE_STUDDED || armourType == ARMOUR_TYPE_CHAIN) {
/*  537 */         toReturn = 2.0F;
/*      */       } 
/*  539 */     } else if (woundType == 3) {
/*      */       
/*  541 */       toReturn = 4.0F;
/*      */     }
/*  543 */     else if (woundType == 4) {
/*      */       
/*  545 */       if (armourType == ARMOUR_TYPE_CLOTH) {
/*  546 */         toReturn = 4.0F;
/*  547 */       } else if (armourType == ARMOUR_TYPE_LEATHER || armourType == ARMOUR_TYPE_LEATHER_DRAGON) {
/*  548 */         toReturn = 0.5F;
/*      */       } 
/*      */     } 
/*  551 */     return toReturn;
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
/*      */   public static float getMaterialMovementModifier(byte armourMaterial) {
/*  563 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*      */     {
/*  565 */       switch (armourMaterial) {
/*      */         
/*      */         case 56:
/*  568 */           return 0.95F;
/*      */         case 30:
/*  570 */           return 1.0F;
/*      */         case 31:
/*  572 */           return 1.0F;
/*      */         case 10:
/*  574 */           return 0.99F;
/*      */         case 57:
/*  576 */           return 0.9F;
/*      */         case 7:
/*  578 */           return 1.05F;
/*      */         case 11:
/*  580 */           return 1.0F;
/*      */         case 12:
/*  582 */           return 1.025F;
/*      */         case 67:
/*  584 */           return 0.9F;
/*      */         case 8:
/*  586 */           return 1.02F;
/*      */         case 9:
/*  588 */           return 1.0F;
/*      */         case 34:
/*  590 */           return 0.98F;
/*      */         case 13:
/*  592 */           return 0.975F;
/*      */         case 96:
/*  594 */           return 1.01F;
/*      */       } 
/*      */     
/*      */     }
/*  598 */     return 1.0F;
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
/*      */   public static byte getArmourPosition(byte bodyPosition) {
/*  611 */     for (ProtectionSlot slot : protectionSlots) {
/*      */       
/*  613 */       if (slot.bodySlots.contains(Byte.valueOf(bodyPosition)))
/*      */       {
/*  615 */         return slot.armourPosition;
/*      */       }
/*      */     } 
/*      */     
/*  619 */     return bodyPosition;
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
/*      */   public static float getArmourModForLocation(Creature creature, byte location, byte woundType) throws NoArmourException {
/*  633 */     byte bodyPosition = getArmourPosition(location);
/*      */     
/*      */     try {
/*  636 */       Item armour = creature.getArmour(bodyPosition);
/*  637 */       return calculateDR(armour, woundType);
/*      */     }
/*  639 */     catch (NoSpaceException e) {
/*      */       
/*  641 */       logger.warning(creature.getName() + " no armour space on loc " + location);
/*      */     }
/*  643 */     catch (NoArmourException e) {
/*      */       
/*  645 */       throw new NoArmourException(String.format("Armour not found for %s at location %s.", new Object[] { creature.getName(), Byte.valueOf(location) }));
/*      */     } 
/*      */     
/*  648 */     return 1.0F;
/*      */   }
/*      */   
/*  651 */   public static ArmourType ARMOUR_TYPE_NONE = new ArmourType("none", 0.0F, 0.3F, 0.0F);
/*  652 */   public static ArmourType ARMOUR_TYPE_LEATHER = new ArmourType("leather", 0.45F, 0.3F, 0.2F);
/*  653 */   public static ArmourType ARMOUR_TYPE_STUDDED = new ArmourType("studded", 0.5F, 0.0F, 0.2F);
/*  654 */   public static ArmourType ARMOUR_TYPE_CHAIN = new ArmourType("chain", 0.55F, -0.15F, 0.25F);
/*  655 */   public static ArmourType ARMOUR_TYPE_PLATE = new ArmourType("plate", 0.63F, -0.3F, 0.25F);
/*  656 */   public static ArmourType ARMOUR_TYPE_RING = new ArmourType("ring", 0.5F, 0.0F, 0.35F);
/*  657 */   public static ArmourType ARMOUR_TYPE_CLOTH = new ArmourType("cloth", 0.35F, 0.3F, 0.2F);
/*  658 */   public static ArmourType ARMOUR_TYPE_SCALE = new ArmourType("scale", 0.45F, 0.0F, 0.3F);
/*  659 */   public static ArmourType ARMOUR_TYPE_SPLINT = new ArmourType("splint", 0.55F, 0.0F, 0.3F);
/*  660 */   public static ArmourType ARMOUR_TYPE_LEATHER_DRAGON = new ArmourType("drake", 0.65F, -0.3F, 0.25F);
/*  661 */   public static ArmourType ARMOUR_TYPE_SCALE_DRAGON = new ArmourType("dragonscale", 0.7F, -0.3F, 0.35F);
/*      */ 
/*      */ 
/*      */   
/*      */   public static void initializeArmourTemplates() {
/*  666 */     new ArmourTemplate(116, ARMOUR_TYPE_STUDDED, 0.01F);
/*  667 */     new ArmourTemplate(117, ARMOUR_TYPE_STUDDED, 0.005F);
/*  668 */     new ArmourTemplate(119, ARMOUR_TYPE_STUDDED, 0.005F);
/*  669 */     new ArmourTemplate(118, ARMOUR_TYPE_STUDDED, 0.05F);
/*  670 */     new ArmourTemplate(120, ARMOUR_TYPE_STUDDED, 0.05F);
/*  671 */     new ArmourTemplate(115, ARMOUR_TYPE_STUDDED, 0.005F);
/*      */ 
/*      */     
/*  674 */     new ArmourTemplate(959, ARMOUR_TYPE_STUDDED, 0.005F);
/*  675 */     new ArmourTemplate(1014, ARMOUR_TYPE_STUDDED, 0.005F);
/*  676 */     new ArmourTemplate(1015, ARMOUR_TYPE_STUDDED, 0.005F);
/*      */ 
/*      */     
/*  679 */     new ArmourTemplate(105, ARMOUR_TYPE_LEATHER, 0.007F);
/*  680 */     new ArmourTemplate(107, ARMOUR_TYPE_LEATHER, 0.003F);
/*  681 */     new ArmourTemplate(103, ARMOUR_TYPE_LEATHER, 0.003F);
/*  682 */     new ArmourTemplate(108, ARMOUR_TYPE_LEATHER, 0.04F);
/*  683 */     new ArmourTemplate(104, ARMOUR_TYPE_LEATHER, 0.04F);
/*  684 */     new ArmourTemplate(106, ARMOUR_TYPE_LEATHER, 0.003F);
/*      */     
/*  686 */     new ArmourTemplate(702, ARMOUR_TYPE_LEATHER, 0.13F);
/*      */ 
/*      */     
/*  689 */     new ArmourTemplate(274, ARMOUR_TYPE_CHAIN, 0.015F);
/*  690 */     new ArmourTemplate(279, ARMOUR_TYPE_CHAIN, 0.01F);
/*  691 */     new ArmourTemplate(275, ARMOUR_TYPE_CHAIN, 0.07F);
/*  692 */     new ArmourTemplate(276, ARMOUR_TYPE_CHAIN, 0.07F);
/*  693 */     new ArmourTemplate(277, ARMOUR_TYPE_CHAIN, 0.008F);
/*  694 */     new ArmourTemplate(278, ARMOUR_TYPE_CHAIN, 0.01F);
/*      */     
/*  696 */     new ArmourTemplate(703, ARMOUR_TYPE_CHAIN, 0.17F);
/*      */ 
/*      */     
/*  699 */     new ArmourTemplate(474, ARMOUR_TYPE_SCALE_DRAGON, 0.001F);
/*  700 */     new ArmourTemplate(475, ARMOUR_TYPE_SCALE_DRAGON, 0.02F);
/*  701 */     new ArmourTemplate(476, ARMOUR_TYPE_SCALE_DRAGON, 0.02F);
/*  702 */     new ArmourTemplate(477, ARMOUR_TYPE_SCALE_DRAGON, 0.005F);
/*  703 */     new ArmourTemplate(478, ARMOUR_TYPE_SCALE_DRAGON, 0.001F);
/*      */ 
/*      */     
/*  706 */     new ArmourTemplate(280, ARMOUR_TYPE_PLATE, 0.02F);
/*  707 */     new ArmourTemplate(284, ARMOUR_TYPE_PLATE, 0.02F);
/*  708 */     new ArmourTemplate(281, ARMOUR_TYPE_PLATE, 0.09F);
/*  709 */     new ArmourTemplate(282, ARMOUR_TYPE_PLATE, 0.09F);
/*  710 */     new ArmourTemplate(283, ARMOUR_TYPE_PLATE, 0.01F);
/*  711 */     new ArmourTemplate(273, ARMOUR_TYPE_PLATE, 0.02F);
/*      */     
/*  713 */     new ArmourTemplate(285, ARMOUR_TYPE_PLATE, 0.025F);
/*  714 */     new ArmourTemplate(286, ARMOUR_TYPE_PLATE, 0.025F);
/*  715 */     new ArmourTemplate(287, ARMOUR_TYPE_PLATE, 0.025F);
/*      */     
/*  717 */     if (Servers.localServer.isChallengeOrEpicServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */ 
/*      */       
/*  720 */       setArmourMoveModifier(280, 0.0175F);
/*  721 */       setArmourMoveModifier(284, 0.015F);
/*  722 */       setArmourMoveModifier(281, 0.08F);
/*  723 */       setArmourMoveModifier(282, 0.08F);
/*  724 */       setArmourMoveModifier(273, 0.015F);
/*      */     } 
/*      */ 
/*      */     
/*  728 */     new ArmourTemplate(109, ARMOUR_TYPE_CLOTH, 0.0F);
/*  729 */     new ArmourTemplate(113, ARMOUR_TYPE_CLOTH, 0.0F);
/*  730 */     new ArmourTemplate(1107, ARMOUR_TYPE_CLOTH, 0.0F);
/*  731 */     new ArmourTemplate(1070, ARMOUR_TYPE_CLOTH, 0.0F);
/*  732 */     new ArmourTemplate(1071, ARMOUR_TYPE_CLOTH, 0.0F);
/*  733 */     new ArmourTemplate(1072, ARMOUR_TYPE_CLOTH, 0.0F);
/*  734 */     new ArmourTemplate(1073, ARMOUR_TYPE_CLOTH, 0.0F);
/*  735 */     new ArmourTemplate(112, ARMOUR_TYPE_CLOTH, 0.0F);
/*  736 */     new ArmourTemplate(110, ARMOUR_TYPE_CLOTH, 0.0F);
/*  737 */     new ArmourTemplate(1067, ARMOUR_TYPE_CLOTH, 0.0F);
/*  738 */     new ArmourTemplate(1068, ARMOUR_TYPE_CLOTH, 0.0F);
/*  739 */     new ArmourTemplate(1069, ARMOUR_TYPE_CLOTH, 0.0F);
/*  740 */     new ArmourTemplate(114, ARMOUR_TYPE_CLOTH, 0.0F);
/*  741 */     new ArmourTemplate(111, ARMOUR_TYPE_CLOTH, 0.0F);
/*  742 */     new ArmourTemplate(1075, ARMOUR_TYPE_CLOTH, 0.0F);
/*  743 */     new ArmourTemplate(1105, ARMOUR_TYPE_CLOTH, 0.0F);
/*  744 */     new ArmourTemplate(1074, ARMOUR_TYPE_CLOTH, 0.0F);
/*  745 */     new ArmourTemplate(1106, ARMOUR_TYPE_CLOTH, 0.0F);
/*  746 */     new ArmourTemplate(779, ARMOUR_TYPE_CLOTH, 0.0F);
/*  747 */     new ArmourTemplate(1425, ARMOUR_TYPE_CLOTH, 0.0F);
/*  748 */     new ArmourTemplate(1427, ARMOUR_TYPE_CLOTH, 0.0F);
/*  749 */     new ArmourTemplate(1426, ARMOUR_TYPE_CLOTH, 0.0F);
/*      */     
/*  751 */     new ArmourTemplate(704, ARMOUR_TYPE_CLOTH, 0.1F);
/*  752 */     new ArmourTemplate(791, ARMOUR_TYPE_CLOTH, 0.0F);
/*      */ 
/*      */     
/*  755 */     new ArmourTemplate(943, ARMOUR_TYPE_CLOTH, 0.0F);
/*  756 */     new ArmourTemplate(944, ARMOUR_TYPE_CLOTH, 0.0F);
/*  757 */     new ArmourTemplate(947, ARMOUR_TYPE_CLOTH, 0.0F);
/*  758 */     new ArmourTemplate(945, ARMOUR_TYPE_CLOTH, 0.0F);
/*  759 */     new ArmourTemplate(946, ARMOUR_TYPE_CLOTH, 0.0F);
/*      */     
/*  761 */     new ArmourTemplate(948, ARMOUR_TYPE_CLOTH, 0.0F);
/*  762 */     new ArmourTemplate(949, ARMOUR_TYPE_CLOTH, 0.0F);
/*  763 */     new ArmourTemplate(950, ARMOUR_TYPE_CLOTH, 0.0F);
/*  764 */     new ArmourTemplate(951, ARMOUR_TYPE_CLOTH, 0.0F);
/*  765 */     new ArmourTemplate(953, ARMOUR_TYPE_CLOTH, 0.0F);
/*  766 */     new ArmourTemplate(952, ARMOUR_TYPE_CLOTH, 0.0F);
/*      */     
/*  768 */     new ArmourTemplate(954, ARMOUR_TYPE_CLOTH, 0.0F);
/*  769 */     new ArmourTemplate(957, ARMOUR_TYPE_CLOTH, 0.0F);
/*  770 */     new ArmourTemplate(956, ARMOUR_TYPE_CLOTH, 0.0F);
/*  771 */     new ArmourTemplate(955, ARMOUR_TYPE_CLOTH, 0.0F);
/*  772 */     new ArmourTemplate(958, ARMOUR_TYPE_CLOTH, 0.0F);
/*      */     
/*  774 */     new ArmourTemplate(961, ARMOUR_TYPE_CLOTH, 0.0F);
/*  775 */     new ArmourTemplate(964, ARMOUR_TYPE_CLOTH, 0.0F);
/*  776 */     new ArmourTemplate(963, ARMOUR_TYPE_CLOTH, 0.0F);
/*  777 */     new ArmourTemplate(962, ARMOUR_TYPE_CLOTH, 0.0F);
/*  778 */     new ArmourTemplate(965, ARMOUR_TYPE_CLOTH, 0.0F);
/*  779 */     new ArmourTemplate(966, ARMOUR_TYPE_CLOTH, 0.0F);
/*      */     
/*  781 */     new ArmourTemplate(960, ARMOUR_TYPE_CLOTH, 0.0F);
/*      */ 
/*      */     
/*  784 */     new ArmourTemplate(979, ARMOUR_TYPE_PLATE, 0.025F);
/*  785 */     new ArmourTemplate(980, ARMOUR_TYPE_PLATE, 0.025F);
/*  786 */     new ArmourTemplate(998, ARMOUR_TYPE_PLATE, 0.025F);
/*      */ 
/*      */     
/*  789 */     new ArmourTemplate(472, ARMOUR_TYPE_LEATHER_DRAGON, 0.001F);
/*  790 */     new ArmourTemplate(471, ARMOUR_TYPE_LEATHER_DRAGON, 0.04F);
/*  791 */     new ArmourTemplate(473, ARMOUR_TYPE_LEATHER_DRAGON, 0.04F);
/*  792 */     new ArmourTemplate(470, ARMOUR_TYPE_LEATHER_DRAGON, 0.004F);
/*  793 */     new ArmourTemplate(469, ARMOUR_TYPE_LEATHER_DRAGON, 0.004F);
/*  794 */     new ArmourTemplate(468, ARMOUR_TYPE_LEATHER_DRAGON, 0.001F);
/*      */ 
/*      */     
/*  797 */     new ArmourTemplate(537, ARMOUR_TYPE_SCALE_DRAGON, 0.04F);
/*  798 */     new ArmourTemplate(531, ARMOUR_TYPE_SCALE_DRAGON, 0.04F);
/*  799 */     new ArmourTemplate(534, ARMOUR_TYPE_SCALE_DRAGON, 0.04F);
/*  800 */     new ArmourTemplate(536, ARMOUR_TYPE_SCALE_DRAGON, 0.001F);
/*  801 */     new ArmourTemplate(530, ARMOUR_TYPE_SCALE_DRAGON, 0.001F);
/*  802 */     new ArmourTemplate(533, ARMOUR_TYPE_SCALE_DRAGON, 0.001F);
/*  803 */     new ArmourTemplate(515, ARMOUR_TYPE_SCALE_DRAGON, 0.001F);
/*  804 */     new ArmourTemplate(330, ARMOUR_TYPE_SCALE_DRAGON, 0.009F);
/*      */     
/*  806 */     new ArmourTemplate(600, ARMOUR_TYPE_LEATHER_DRAGON, 0.001F);
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
/*      */   public static void initializeCreatureArmour() {
/*  822 */     ARMOUR_TYPE_NONE.setCreatureGlance(0.0F, (byte)0, 0.0F);
/*  823 */     ARMOUR_TYPE_LEATHER.setCreatureGlance(0.25F, (byte)0, 0.05F);
/*  824 */     ARMOUR_TYPE_STUDDED.setCreatureGlance(0.25F, (byte)1, 0.05F);
/*  825 */     ARMOUR_TYPE_CHAIN.setCreatureGlance(0.3F, (byte)2, 0.1F);
/*  826 */     ARMOUR_TYPE_PLATE.setCreatureGlance(0.3F, (byte)1, 0.05F);
/*  827 */     ARMOUR_TYPE_RING.setCreatureGlance(0.3F, (byte)2, 0.05F);
/*  828 */     ARMOUR_TYPE_CLOTH.setCreatureGlance(0.25F, (byte)0, 0.05F);
/*  829 */     ARMOUR_TYPE_SCALE.setCreatureGlance(0.3F, (byte)1, 0.1F);
/*  830 */     ARMOUR_TYPE_SPLINT.setCreatureGlance(0.3F, (byte)0, 0.1F);
/*  831 */     ARMOUR_TYPE_LEATHER_DRAGON.setCreatureGlance(0.35F, (byte)0, 0.05F);
/*  832 */     ARMOUR_TYPE_SCALE_DRAGON.setCreatureGlance(0.35F, (byte)1, 0.05F);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void initializeArmourEffectiveness() {
/*  838 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)3, 0.95F);
/*  839 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)3, 1.05F);
/*  840 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)3, 1.05F);
/*  841 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)3, 1.07F);
/*  842 */     ARMOUR_TYPE_RING.setEffectiveness((byte)3, 1.0F);
/*  843 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)3, 0.9F);
/*  844 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)3, 1.0F);
/*  845 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)3, 1.0F);
/*  846 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)3, 1.0F);
/*  847 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)3, 1.0F);
/*      */     
/*  849 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/*  851 */       ARMOUR_TYPE_LEATHER.setEffectiveness((byte)3, 0.9F);
/*  852 */       ARMOUR_TYPE_CHAIN.setEffectiveness((byte)3, 1.075F);
/*  853 */       ARMOUR_TYPE_PLATE.setEffectiveness((byte)3, 1.05F);
/*  854 */       ARMOUR_TYPE_CLOTH.setEffectiveness((byte)3, 0.8F);
/*      */     } 
/*      */ 
/*      */     
/*  858 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)0, 1.0F);
/*  859 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)0, 1.0F);
/*  860 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)0, 1.1F);
/*  861 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)0, 0.9F);
/*  862 */     ARMOUR_TYPE_RING.setEffectiveness((byte)0, 1.0F);
/*  863 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)0, 1.15F);
/*  864 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)0, 1.0F);
/*  865 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)0, 1.0F);
/*  866 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)0, 1.1F);
/*  867 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)0, 0.95F);
/*      */     
/*  869 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/*  871 */       ARMOUR_TYPE_CHAIN.setEffectiveness((byte)0, 1.075F);
/*  872 */       ARMOUR_TYPE_PLATE.setEffectiveness((byte)0, 0.95F);
/*  873 */       ARMOUR_TYPE_CLOTH.setEffectiveness((byte)0, 1.2F);
/*  874 */       ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)0, 1.05F);
/*      */     } 
/*      */ 
/*      */     
/*  878 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)2, 0.9F);
/*  879 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)2, 1.1F);
/*  880 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)2, 0.9F);
/*  881 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)2, 1.0F);
/*  882 */     ARMOUR_TYPE_RING.setEffectiveness((byte)2, 1.0F);
/*  883 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)2, 1.0F);
/*  884 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)2, 1.0F);
/*  885 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)2, 1.0F);
/*  886 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)2, 1.0F);
/*  887 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)2, 1.1F);
/*      */     
/*  889 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/*  891 */       ARMOUR_TYPE_CHAIN.setEffectiveness((byte)2, 0.925F);
/*  892 */       ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)2, 1.05F);
/*      */     } 
/*      */ 
/*      */     
/*  896 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)1, 1.1F);
/*  897 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)1, 0.9F);
/*  898 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)1, 1.0F);
/*  899 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)1, 1.05F);
/*  900 */     ARMOUR_TYPE_RING.setEffectiveness((byte)1, 1.0F);
/*  901 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)1, 0.8F);
/*  902 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)1, 1.0F);
/*  903 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)1, 1.0F);
/*  904 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)1, 0.9F);
/*  905 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)1, 1.0F);
/*      */     
/*  907 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled())
/*      */     {
/*  909 */       ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)1, 0.95F);
/*      */     }
/*      */ 
/*      */     
/*  913 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)4, 1.15F);
/*  914 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)4, 1.0F);
/*  915 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)4, 1.05F);
/*  916 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)4, 0.95F);
/*  917 */     ARMOUR_TYPE_RING.setEffectiveness((byte)4, 1.0F);
/*  918 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)4, 0.9F);
/*  919 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)4, 1.0F);
/*  920 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)4, 1.0F);
/*  921 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)4, 1.0F);
/*  922 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)4, 1.1F);
/*      */     
/*  924 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/*  926 */       ARMOUR_TYPE_LEATHER.setEffectiveness((byte)4, 1.1F);
/*  927 */       ARMOUR_TYPE_CHAIN.setEffectiveness((byte)4, 1.075F);
/*  928 */       ARMOUR_TYPE_CLOTH.setEffectiveness((byte)4, 0.8F);
/*  929 */       ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)4, 1.05F);
/*      */     } 
/*      */ 
/*      */     
/*  933 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)8, 1.0F);
/*  934 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)8, 0.9F);
/*  935 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)8, 0.9F);
/*  936 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)8, 1.0F);
/*  937 */     ARMOUR_TYPE_RING.setEffectiveness((byte)8, 1.0F);
/*  938 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)8, 1.25F);
/*  939 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)8, 1.0F);
/*  940 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)8, 1.0F);
/*  941 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)8, 1.05F);
/*  942 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)8, 0.95F);
/*      */     
/*  944 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/*  946 */       ARMOUR_TYPE_STUDDED.setEffectiveness((byte)8, 1.1F);
/*  947 */       ARMOUR_TYPE_CHAIN.setEffectiveness((byte)8, 0.925F);
/*  948 */       ARMOUR_TYPE_CLOTH.setEffectiveness((byte)8, 1.2F);
/*      */     } 
/*      */ 
/*      */     
/*  952 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)10, 0.9F);
/*  953 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)10, 1.05F);
/*  954 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)10, 1.0F);
/*  955 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)10, 1.07F);
/*  956 */     ARMOUR_TYPE_RING.setEffectiveness((byte)10, 1.0F);
/*  957 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)10, 1.0F);
/*  958 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)10, 1.0F);
/*  959 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)10, 1.0F);
/*  960 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)10, 0.95F);
/*  961 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)10, 1.0F);
/*      */     
/*  963 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/*  965 */       ARMOUR_TYPE_STUDDED.setEffectiveness((byte)10, 0.9F);
/*  966 */       ARMOUR_TYPE_PLATE.setEffectiveness((byte)10, 1.05F);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  971 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)6, 1.0F);
/*  972 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)6, 1.0F);
/*  973 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)6, 1.0F);
/*  974 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)6, 1.0F);
/*  975 */     ARMOUR_TYPE_RING.setEffectiveness((byte)6, 1.0F);
/*  976 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)6, 1.0F);
/*  977 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)6, 1.0F);
/*  978 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)6, 1.0F);
/*  979 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)6, 1.0F);
/*  980 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)6, 1.0F);
/*      */ 
/*      */ 
/*      */     
/*  984 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)9, 1.0F);
/*  985 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)9, 1.0F);
/*  986 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)9, 1.0F);
/*  987 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)9, 1.0F);
/*  988 */     ARMOUR_TYPE_RING.setEffectiveness((byte)9, 1.0F);
/*  989 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)9, 1.0F);
/*  990 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)9, 1.0F);
/*  991 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)9, 1.0F);
/*  992 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)9, 1.0F);
/*  993 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)9, 1.0F);
/*      */ 
/*      */ 
/*      */     
/*  997 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)5, 1.0F);
/*  998 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)5, 1.0F);
/*  999 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)5, 1.0F);
/* 1000 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)5, 1.0F);
/* 1001 */     ARMOUR_TYPE_RING.setEffectiveness((byte)5, 1.0F);
/* 1002 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)5, 1.0F);
/* 1003 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)5, 1.0F);
/* 1004 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)5, 1.0F);
/* 1005 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)5, 1.0F);
/* 1006 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)5, 1.0F);
/*      */ 
/*      */ 
/*      */     
/* 1010 */     ARMOUR_TYPE_LEATHER.setEffectiveness((byte)7, 1.0F);
/* 1011 */     ARMOUR_TYPE_STUDDED.setEffectiveness((byte)7, 1.0F);
/* 1012 */     ARMOUR_TYPE_CHAIN.setEffectiveness((byte)7, 1.0F);
/* 1013 */     ARMOUR_TYPE_PLATE.setEffectiveness((byte)7, 1.0F);
/* 1014 */     ARMOUR_TYPE_RING.setEffectiveness((byte)7, 1.0F);
/* 1015 */     ARMOUR_TYPE_CLOTH.setEffectiveness((byte)7, 1.0F);
/* 1016 */     ARMOUR_TYPE_SCALE.setEffectiveness((byte)7, 1.0F);
/* 1017 */     ARMOUR_TYPE_SPLINT.setEffectiveness((byte)7, 1.0F);
/* 1018 */     ARMOUR_TYPE_LEATHER_DRAGON.setEffectiveness((byte)7, 1.0F);
/* 1019 */     ARMOUR_TYPE_SCALE_DRAGON.setEffectiveness((byte)7, 1.0F);
/*      */ 
/*      */     
/* 1022 */     byte woundType = 0;
/* 1023 */     while (woundType <= 10) {
/*      */       
/* 1025 */       ARMOUR_TYPE_NONE.setEffectiveness(woundType, 1.0F);
/* 1026 */       woundType = (byte)(woundType + 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void initializeArmourGlanceRates() {
/* 1035 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)3, 0.3F);
/* 1036 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)3, 0.45F);
/* 1037 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)3, 0.6F);
/* 1038 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)3, 0.45F);
/* 1039 */     ARMOUR_TYPE_RING.setGlanceRate((byte)3, 0.5F);
/* 1040 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)3, 0.3F);
/* 1041 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)3, 0.6F);
/* 1042 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)3, 0.3F);
/* 1043 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)3, 0.5F);
/* 1044 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)3, 0.4F);
/*      */ 
/*      */     
/* 1047 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)0, 0.5F);
/* 1048 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)0, 0.6F);
/* 1049 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)0, 0.25F);
/* 1050 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)0, 0.25F);
/* 1051 */     ARMOUR_TYPE_RING.setGlanceRate((byte)0, 0.3F);
/* 1052 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)0, 0.5F);
/* 1053 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)0, 0.3F);
/* 1054 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)0, 0.4F);
/* 1055 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)0, 0.5F);
/* 1056 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)0, 0.5F);
/*      */ 
/*      */     
/* 1059 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)2, 0.3F);
/* 1060 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)2, 0.25F);
/* 1061 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)2, 0.25F);
/* 1062 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)2, 0.6F);
/* 1063 */     ARMOUR_TYPE_RING.setGlanceRate((byte)2, 0.3F);
/* 1064 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)2, 0.35F);
/* 1065 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)2, 0.5F);
/* 1066 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)2, 0.4F);
/* 1067 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)2, 0.2F);
/* 1068 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)2, 0.6F);
/*      */ 
/*      */     
/* 1071 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)1, 0.3F);
/* 1072 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)1, 0.25F);
/* 1073 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)1, 0.6F);
/* 1074 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)1, 0.25F);
/* 1075 */     ARMOUR_TYPE_RING.setGlanceRate((byte)1, 0.5F);
/* 1076 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)1, 0.35F);
/* 1077 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)1, 0.3F);
/* 1078 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)1, 0.4F);
/* 1079 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)1, 0.5F);
/* 1080 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)1, 0.2F);
/*      */ 
/*      */     
/* 1083 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)4, 0.1F);
/* 1084 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)4, 0.1F);
/* 1085 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)4, 0.6F);
/* 1086 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)4, 0.3F);
/* 1087 */     ARMOUR_TYPE_RING.setGlanceRate((byte)4, 0.2F);
/* 1088 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)4, 0.1F);
/* 1089 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)4, 0.3F);
/* 1090 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)4, 0.2F);
/* 1091 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)4, 0.3F);
/* 1092 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)4, 0.5F);
/*      */ 
/*      */     
/* 1095 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)8, 0.6F);
/* 1096 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)8, 0.6F);
/* 1097 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)8, 0.1F);
/* 1098 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)8, 0.3F);
/* 1099 */     ARMOUR_TYPE_RING.setGlanceRate((byte)8, 0.2F);
/* 1100 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)8, 0.6F);
/* 1101 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)8, 0.3F);
/* 1102 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)8, 0.2F);
/* 1103 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)8, 0.5F);
/* 1104 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)8, 0.2F);
/*      */ 
/*      */     
/* 1107 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)10, 0.2F);
/* 1108 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)10, 0.2F);
/* 1109 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)10, 0.2F);
/* 1110 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)10, 0.3F);
/* 1111 */     ARMOUR_TYPE_RING.setGlanceRate((byte)10, 0.5F);
/* 1112 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)10, 0.6F);
/* 1113 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)10, 0.3F);
/* 1114 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)10, 0.6F);
/* 1115 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)10, 0.3F);
/* 1116 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)10, 0.2F);
/*      */ 
/*      */ 
/*      */     
/* 1120 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)6, 0.0F);
/* 1121 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)6, 0.0F);
/* 1122 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)6, 0.0F);
/* 1123 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)6, 0.0F);
/* 1124 */     ARMOUR_TYPE_RING.setGlanceRate((byte)6, 0.0F);
/* 1125 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)6, 0.0F);
/* 1126 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)6, 0.0F);
/* 1127 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)6, 0.0F);
/* 1128 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)6, 0.0F);
/* 1129 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)6, 0.0F);
/*      */ 
/*      */ 
/*      */     
/* 1133 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)9, 0.0F);
/* 1134 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)9, 0.0F);
/* 1135 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)9, 0.0F);
/* 1136 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)9, 0.0F);
/* 1137 */     ARMOUR_TYPE_RING.setGlanceRate((byte)9, 0.0F);
/* 1138 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)9, 0.0F);
/* 1139 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)9, 0.0F);
/* 1140 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)9, 0.0F);
/* 1141 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)9, 0.0F);
/* 1142 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)9, 0.0F);
/*      */ 
/*      */ 
/*      */     
/* 1146 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)5, 0.0F);
/* 1147 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)5, 0.0F);
/* 1148 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)5, 0.0F);
/* 1149 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)5, 0.0F);
/* 1150 */     ARMOUR_TYPE_RING.setGlanceRate((byte)5, 0.0F);
/* 1151 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)5, 0.0F);
/* 1152 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)5, 0.0F);
/* 1153 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)5, 0.0F);
/* 1154 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)5, 0.0F);
/* 1155 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)5, 0.0F);
/*      */ 
/*      */ 
/*      */     
/* 1159 */     ARMOUR_TYPE_LEATHER.setGlanceRate((byte)7, 0.0F);
/* 1160 */     ARMOUR_TYPE_STUDDED.setGlanceRate((byte)7, 0.0F);
/* 1161 */     ARMOUR_TYPE_CHAIN.setGlanceRate((byte)7, 0.0F);
/* 1162 */     ARMOUR_TYPE_PLATE.setGlanceRate((byte)7, 0.0F);
/* 1163 */     ARMOUR_TYPE_RING.setGlanceRate((byte)7, 0.0F);
/* 1164 */     ARMOUR_TYPE_CLOTH.setGlanceRate((byte)7, 0.0F);
/* 1165 */     ARMOUR_TYPE_SCALE.setGlanceRate((byte)7, 0.0F);
/* 1166 */     ARMOUR_TYPE_SPLINT.setGlanceRate((byte)7, 0.0F);
/* 1167 */     ARMOUR_TYPE_LEATHER_DRAGON.setGlanceRate((byte)7, 0.0F);
/* 1168 */     ARMOUR_TYPE_SCALE_DRAGON.setGlanceRate((byte)7, 0.0F);
/*      */ 
/*      */     
/* 1171 */     byte woundType = 0;
/* 1172 */     while (woundType <= 10) {
/*      */       
/* 1174 */       ARMOUR_TYPE_NONE.setGlanceRate(woundType, 0.0F);
/* 1175 */       woundType = (byte)(woundType + 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void initialize() {
/* 1182 */     if (Servers.isThisAnEpicOrChallengeServer() || Features.Feature.NEW_ARMOUR_VALUES.isEnabled()) {
/*      */       
/* 1184 */       ARMOUR_TYPE_LEATHER.setBaseDR(0.6F);
/* 1185 */       ARMOUR_TYPE_STUDDED.setBaseDR(0.625F);
/* 1186 */       ARMOUR_TYPE_CHAIN.setBaseDR(0.625F);
/* 1187 */       ARMOUR_TYPE_PLATE.setBaseDR(0.65F);
/* 1188 */       ARMOUR_TYPE_CLOTH.setBaseDR(0.4F);
/*      */     } 
/*      */     
/* 1191 */     initializeProtectionSlots();
/* 1192 */     initializeCreatureArmour();
/* 1193 */     initializeArmourEffectiveness();
/* 1194 */     initializeArmourGlanceRates();
/*      */     
/* 1196 */     initializeArmourTemplates();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\ArmourTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */