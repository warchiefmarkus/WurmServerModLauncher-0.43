/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public abstract class CreationEntry
/*     */   implements ItemTypes, ItemMaterials, MiscConstants
/*     */ {
/*     */   final int primarySkill;
/*     */   final int objectSource;
/*  47 */   protected byte objectSourceMaterial = 0;
/*     */ 
/*     */   
/*     */   final int objectTarget;
/*     */   
/*  52 */   protected byte objectTargetMaterial = 0;
/*     */   
/*     */   final int objectCreated;
/*     */   
/*     */   final boolean destroyTarget;
/*     */   
/*     */   final boolean useCapacity;
/*     */   
/*     */   final float percentageLost;
/*     */   final int minTimeSeconds;
/*     */   final boolean destroyBoth;
/*  63 */   private static final Logger logger = Logger.getLogger(CreationEntry.class.getName());
/*     */   
/*     */   final boolean createOnGround;
/*     */   
/*     */   public boolean depleteEqually;
/*     */   
/*     */   public boolean depleteSource = false;
/*     */   public boolean depleteTarget = false;
/*     */   public static final float TUTORIALCHANCE = 90.0F;
/*     */   public boolean isOnlyCreateEpicTargetMission = false;
/*     */   public boolean isCreateEpicTargetMission = true;
/*     */   private boolean hasCustomChanceCutoff = false;
/*  75 */   private int customCreationChanceCutOff = 0;
/*     */   private boolean hasMinimumSkillRequirement = false;
/*  77 */   private double minimumSkill = 0.0D;
/*     */   
/*  79 */   private int depleteFromSource = 0;
/*  80 */   private int depleteFromTarget = 0;
/*     */   
/*     */   private CreationCategories category;
/*  83 */   private int deity = 0;
/*     */   
/*     */   private boolean useTemplateWeight = false;
/*     */   private boolean colouringCreation = false;
/*     */   private boolean useFinalMaterial = false;
/*  88 */   private byte finalMaterial = 0;
/*  89 */   protected static final CreationRequirement[] emptyReqs = new CreationRequirement[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDestroyTarget, boolean aUseCapacity, float aPercentageLost, int aMinTimeSeconds, boolean aDestroyBoth, boolean aCreateOnGround, CreationCategories aCategory) {
/*  97 */     this.primarySkill = aPrimarySkill;
/*  98 */     this.objectSource = aObjectSource;
/*  99 */     this.objectTarget = aObjectTarget;
/* 100 */     this.objectCreated = aObjectCreated;
/* 101 */     this.destroyTarget = aDestroyTarget;
/* 102 */     this.depleteTarget = this.destroyTarget;
/* 103 */     this.useCapacity = aUseCapacity;
/* 104 */     this.depleteSource = this.useCapacity;
/* 105 */     this.percentageLost = aPercentageLost;
/* 106 */     this.minTimeSeconds = aMinTimeSeconds;
/* 107 */     this.destroyBoth = aDestroyBoth;
/* 108 */     this.depleteEqually = this.destroyBoth;
/* 109 */     this.createOnGround = aCreateOnGround;
/* 110 */     this.category = aCategory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAdvanced() {
/* 115 */     return false;
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
/*     */   CreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, boolean aDepleteEqually, float aPercentageLost, boolean aCreateOnGround, CreationCategories aCategory) {
/* 127 */     this.primarySkill = aPrimarySkill;
/* 128 */     this.objectSource = aObjectSource;
/* 129 */     this.objectTarget = aObjectTarget;
/* 130 */     this.objectCreated = aObjectCreated;
/* 131 */     this.depleteTarget = aDepleteTarget;
/* 132 */     this.destroyTarget = this.depleteTarget;
/* 133 */     this.depleteSource = aDepleteSource;
/* 134 */     this.percentageLost = aPercentageLost;
/* 135 */     this.depleteEqually = false;
/* 136 */     if (aDepleteEqually) {
/*     */       
/* 138 */       this.depleteTarget = true;
/* 139 */       this.depleteSource = true;
/*     */     } 
/* 141 */     this.destroyBoth = aDepleteEqually;
/* 142 */     this.minTimeSeconds = 5;
/* 143 */     this.useCapacity = this.depleteSource;
/* 144 */     this.createOnGround = aCreateOnGround;
/* 145 */     this.category = aCategory;
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
/*     */   CreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, boolean aDepleteEqually, float aPercentageLost, boolean aCreateOnGround, int aCustomCutOffChance, double aMinimumSkill, CreationCategories aCategory) {
/* 159 */     this(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDepleteSource, aDepleteTarget, aDepleteEqually, aPercentageLost, aCreateOnGround, aCategory);
/*     */ 
/*     */ 
/*     */     
/* 163 */     if (aCustomCutOffChance != 0) {
/*     */       
/* 165 */       this.customCreationChanceCutOff = aCustomCutOffChance;
/* 166 */       this.hasCustomChanceCutoff = true;
/*     */     } 
/* 168 */     if (aMinimumSkill != 0.0D) {
/*     */       
/* 170 */       this.minimumSkill = aMinimumSkill;
/* 171 */       this.hasMinimumSkillRequirement = true;
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
/*     */   CreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, float aPercentageLost, boolean aDepleteEqually, boolean aCreateOnGround, CreationCategories aCategory) {
/* 183 */     this.primarySkill = aPrimarySkill;
/* 184 */     this.objectSource = aObjectSource;
/* 185 */     this.objectTarget = aObjectTarget;
/* 186 */     this.objectCreated = aObjectCreated;
/* 187 */     this.depleteTarget = aDepleteTarget;
/* 188 */     this.destroyTarget = this.depleteTarget;
/* 189 */     this.depleteSource = aDepleteSource;
/* 190 */     this.percentageLost = aPercentageLost;
/* 191 */     this.depleteEqually = aDepleteEqually;
/* 192 */     this.destroyBoth = this.depleteEqually;
/* 193 */     this.minTimeSeconds = 5;
/* 194 */     this.useCapacity = this.depleteSource;
/* 195 */     this.createOnGround = aCreateOnGround;
/* 196 */     this.category = aCategory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, float aPercentageLost, boolean aDepleteEqually, boolean aCreateOnGround, int aCustomCutOffChance, double aMinimumSkill, CreationCategories aCategory) {
/* 204 */     this(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDepleteSource, aDepleteTarget, aPercentageLost, aDepleteEqually, aCreateOnGround, aCategory);
/*     */     
/* 206 */     if (aCustomCutOffChance != 0) {
/*     */       
/* 208 */       this.customCreationChanceCutOff = aCustomCutOffChance;
/* 209 */       this.hasCustomChanceCutoff = true;
/*     */     } 
/*     */     
/* 212 */     if (aMinimumSkill != 0.0D) {
/*     */       
/* 214 */       this.minimumSkill = aMinimumSkill;
/* 215 */       this.hasMinimumSkillRequirement = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getObjectCreated() {
/* 225 */     return this.objectCreated;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getCustomCutOffChance() {
/* 230 */     return this.customCreationChanceCutOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public final CreationCategories getCategory() {
/* 235 */     return this.category;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalNumberOfItems() {
/* 240 */     if (this.depleteSource && this.depleteTarget)
/* 241 */       return 2; 
/* 242 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public CreationRequirement[] getRequirements() {
/* 247 */     return emptyReqs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getObjectSource() {
/* 255 */     return this.objectSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getObjectSourceMaterial() {
/* 260 */     return this.objectSourceMaterial;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObjectSourceMaterial(byte sourceMaterial) {
/* 265 */     this.objectSourceMaterial = sourceMaterial;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getObjectTarget() {
/* 275 */     return this.objectTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getObjectTargetMaterial() {
/* 280 */     return this.objectTargetMaterial;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObjectTargetMaterial(byte targetMaterial) {
/* 285 */     this.objectTargetMaterial = targetMaterial;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrimarySkill() {
/* 294 */     return this.primarySkill;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isDepleteSourceAndTarget() {
/* 304 */     return this.depleteEqually;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDepleteSourceAndTarget(boolean aDepleteSourceAndTarget) {
/* 315 */     this.depleteEqually = aDepleteSourceAndTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isDestroyTarget() {
/* 325 */     return this.destroyTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasCustomCreationChanceCutOff() {
/* 330 */     return this.hasCustomChanceCutoff;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasCustomDepleteFromSource() {
/* 335 */     return (this.depleteFromSource != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasCustomDepleteFromTarget() {
/* 340 */     return (this.depleteFromTarget != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDepleteFromSource(int toDeplete) {
/* 345 */     this.depleteFromSource = toDeplete;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDepleteFromTarget(int toDeplete) {
/* 350 */     this.depleteFromTarget = toDeplete;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getDepleteFromTarget() {
/* 355 */     return this.depleteFromTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getDepleteFromSource() {
/* 360 */     return this.depleteFromSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRestrictedToDeityFollower() {
/* 365 */     return (this.deity != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeityRestriction(int deity) {
/* 371 */     this.deity = deity;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getDeityRestriction() {
/* 376 */     return this.deity;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean usesFinalMaterial() {
/* 381 */     return this.useFinalMaterial;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFinalMaterial(byte material) {
/* 386 */     this.finalMaterial = material;
/* 387 */     if (material == 0) {
/*     */       
/* 389 */       this.useFinalMaterial = false;
/*     */       return;
/*     */     } 
/* 392 */     this.useFinalMaterial = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte getFinalMaterial() {
/* 397 */     return this.finalMaterial;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isUseCapacity() {
/* 407 */     return this.useCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getPercentageLost() {
/* 417 */     return this.percentageLost;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUseTemplateWeight(boolean templateWeight) {
/* 422 */     this.useTemplateWeight = templateWeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean getUseTempalateWeight() {
/* 427 */     return this.useTemplateWeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isColouringCreation() {
/* 432 */     return this.colouringCreation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColouringCreation(boolean addsColour) {
/* 437 */     this.colouringCreation = addsColour;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsEpicBuildMissionTarget(boolean target_ok) {
/* 442 */     this.isCreateEpicTargetMission = target_ok;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getMinTimeSeconds() {
/* 452 */     return this.minTimeSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public final double getMinimumSkillRequirement() {
/* 457 */     return this.minimumSkill;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasMinimumSkillRequirement() {
/* 462 */     return this.hasMinimumSkillRequirement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isDestroyBoth() {
/* 472 */     return this.destroyBoth;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDifficultyFor(Item source, Item target, Creature performer) throws NoSuchTemplateException {
/* 478 */     Item realSource = source;
/*     */     
/* 480 */     if (source.getTemplateId() == this.objectTarget && target.getTemplateId() == this.objectSource)
/*     */     {
/*     */       
/* 483 */       if (source.getTemplateId() != target.getTemplateId())
/* 484 */         realSource = target; 
/*     */     }
/* 486 */     Skills skills = performer.getSkills();
/* 487 */     Skill primSkill = null;
/* 488 */     Skill secondarySkill = null;
/* 489 */     double bonus = 0.0D;
/*     */     
/*     */     try {
/* 492 */       primSkill = skills.getSkill(this.primarySkill);
/* 493 */       if (hasMinimumSkillRequirement() && getMinimumSkillRequirement() > primSkill.getKnowledge(0.0D)) {
/* 494 */         return 0.0F;
/*     */       }
/* 496 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 501 */       secondarySkill = skills.getSkill(realSource.getPrimarySkill());
/*     */     }
/* 503 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 509 */     ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(this.objectCreated);
/* 510 */     if (secondarySkill != null)
/* 511 */       bonus = Math.max(1.0D, secondarySkill.getKnowledge(realSource, 0.0D) / 10.0D); 
/* 512 */     float chance = 0.0F;
/*     */ 
/*     */     
/* 515 */     if (template.isRune()) {
/*     */       
/* 517 */       boolean godBonus = false;
/* 518 */       if (performer.getDeity() != null) {
/*     */         
/* 520 */         if ((performer.getDeity().isMountainGod() && this.objectCreated == 1289) || (performer
/* 521 */           .getDeity().isForestGod() && this.objectCreated == 1290) || (performer
/* 522 */           .getDeity().isWaterGod() && this.objectCreated == 1291) || (performer
/* 523 */           .getDeity().isHateGod() && this.objectCreated == 1292)) {
/*     */           
/* 525 */           if (performer.getFaith() >= 20.0F) {
/* 526 */             godBonus = true;
/*     */           }
/* 528 */         } else if (performer.getFaith() < 20.0F && this.objectCreated == 1293) {
/*     */           
/* 530 */           godBonus = true;
/*     */         }
/*     */       
/* 533 */       } else if (this.objectCreated == 1293) {
/*     */         
/* 535 */         godBonus = true;
/*     */       } 
/*     */       
/* 538 */       if (godBonus) {
/* 539 */         bonus += 100.0D;
/*     */       }
/*     */     } 
/* 542 */     if (template.isTutorialItem())
/*     */     {
/* 544 */       return 90.0F;
/*     */     }
/* 546 */     if (primSkill != null) {
/*     */       
/* 548 */       chance = (float)primSkill.getChance(template.getDifficulty(), realSource, bonus);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 553 */       chance = 1.0F / (1.0F + template.getDifficulty()) * 100.0F;
/*     */     } 
/*     */     
/* 556 */     return chance;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCreateOnGround() {
/* 561 */     return this.createOnGround;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getScrapMaterial(byte material) {
/* 566 */     if (Materials.isWood(material))
/* 567 */       return 169; 
/* 568 */     if (material == 11)
/* 569 */       return 46; 
/* 570 */     if (material == 17)
/* 571 */       return 171; 
/* 572 */     if (material == 2 || material == 22)
/* 573 */       return 173; 
/* 574 */     if (material == 16)
/* 575 */       return 172; 
/* 576 */     if (material == 10)
/* 577 */       return 47; 
/* 578 */     if (material == 7)
/* 579 */       return 44; 
/* 580 */     if (material == 8)
/* 581 */       return 45; 
/* 582 */     if (material == 13)
/* 583 */       return 48; 
/* 584 */     if (material == 12)
/* 585 */       return 49; 
/* 586 */     if (material == 30)
/* 587 */       return 221; 
/* 588 */     if (material == 31)
/* 589 */       return 223; 
/* 590 */     if (material == 34)
/* 591 */       return 220; 
/* 592 */     if (material == 9)
/* 593 */       return 205; 
/* 594 */     if (material == 56)
/* 595 */       return 694; 
/* 596 */     if (material == 57)
/* 597 */       return 698; 
/* 598 */     if (material == 26)
/* 599 */       return 634; 
/* 600 */     if (material == 67)
/* 601 */       return 837; 
/* 602 */     if (material == 96)
/* 603 */       return 1411; 
/* 604 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void checkSaneAmounts(Item realSource, int sourceWeightToRemove, Item realTarget, int targetWeightToRemove, ItemTemplate template, Creature performer, boolean advancedItem) throws NoSuchItemException {
/* 611 */     if ((this.depleteSource && sourceWeightToRemove <= 0) || (this.depleteTarget && targetWeightToRemove <= 0)) {
/*     */       
/* 613 */       performer.getCommunicator().sendNormalServerMessage("The " + realSource
/* 614 */           .getName() + " or the " + realTarget.getName() + " contains too little material to create " + template
/* 615 */           .getNameWithGenus() + ". You need to find more.");
/*     */       
/* 617 */       throw new NoSuchItemException("Too little material.");
/*     */     } 
/* 619 */     if (!advancedItem && ((this.depleteSource && this.depleteTarget) || this.depleteEqually) && realSource
/* 620 */       .getWeightGrams(false) + realTarget.getWeightGrams(false) < template.getWeightGrams()) {
/*     */       
/* 622 */       if (realSource.isCombine() || realSource.isLiquid() || realTarget.isLiquid() || realTarget.isCombine()) {
/* 623 */         performer.getCommunicator().sendNormalServerMessage("The " + realSource
/* 624 */             .getName() + " and the " + realTarget.getName() + " contains too little material to create " + template
/* 625 */             .getNameWithGenus() + ".  Try to combine any of them with a similar object to get larger pieces.");
/*     */       } else {
/*     */         
/* 628 */         performer.getCommunicator().sendNormalServerMessage("The " + realSource
/* 629 */             .getName() + " and the " + realTarget.getName() + " contains too little material to create " + template
/* 630 */             .getNameWithGenus() + ". You need to find larger parts.");
/*     */       } 
/* 632 */       throw new NoSuchItemException("Too little material.");
/*     */     } 
/* 634 */     if (!advancedItem && ((this.depleteSource && this.depleteTarget) || this.depleteEqually) && (realTarget
/* 635 */       .isCombine() || realTarget.isLiquid()) && (realSource.isCombine() || realSource.isLiquid())) {
/*     */ 
/*     */ 
/*     */       
/* 639 */       int sourceMax = (sourceWeightToRemove <= realSource.getWeightGrams()) ? 1 : 0;
/*     */ 
/*     */       
/* 642 */       int targetMax = (targetWeightToRemove <= realTarget.getWeightGrams()) ? 1 : 0;
/* 643 */       if (template.isCombine() && this.objectCreated != 73) {
/*     */ 
/*     */         
/* 646 */         sourceMax = (int)(realSource.getWeightGrams() / template.getWeightGrams() / 2.0F);
/* 647 */         targetMax = (int)(realTarget.getWeightGrams() / template.getWeightGrams() / 2.0F);
/*     */       } 
/* 649 */       if (sourceMax == 0 || targetMax == 0) {
/*     */         
/* 651 */         performer.getCommunicator().sendNormalServerMessage("The amount of materials is too low to produce anything.");
/* 652 */         throw new NoSuchItemException("Bad amounts of combined items.");
/*     */       } 
/*     */     } 
/* 655 */     if (realSource.getWeightGrams(false) < sourceWeightToRemove) {
/*     */       
/* 657 */       if (realSource.isCombine()) {
/* 658 */         performer.getCommunicator().sendNormalServerMessage("The " + realSource
/* 659 */             .getName() + " contains too little material to create " + template
/* 660 */             .getNameWithGenus() + ".  Try to combine it with a similar object to get a larger amount.");
/*     */       } else {
/*     */         
/* 663 */         performer.getCommunicator().sendNormalServerMessage("The " + realSource
/* 664 */             .getName() + " contains too little material to create " + template
/* 665 */             .getNameWithGenus() + ".");
/* 666 */       }  throw new NoSuchItemException("Too little material.");
/*     */     } 
/* 668 */     if (realTarget.getWeightGrams(false) < targetWeightToRemove) {
/*     */       
/* 670 */       if (realTarget.isCombine()) {
/* 671 */         performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/* 672 */             .getName() + " contains too little material to create " + template
/* 673 */             .getNameWithGenus() + ".  Try to combine it with a similar object to get a larger amount.");
/*     */       } else {
/*     */         
/* 676 */         performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/* 677 */             .getName() + " contains too little material to create " + template
/* 678 */             .getNameWithGenus() + ".");
/* 679 */       }  throw new NoSuchItemException("Too little material.");
/*     */     } 
/* 681 */     if (realSource.isLiquid() && realTarget.isLiquid()) {
/*     */ 
/*     */ 
/*     */       
/* 685 */       int sourceMax = realSource.getWeightGrams() / realSource.getTemplate().getWeightGrams();
/* 686 */       int targetMax = realTarget.getWeightGrams() / realTarget.getTemplate().getWeightGrams();
/*     */ 
/*     */ 
/*     */       
/* 690 */       if (sourceMax < 1 || targetMax < 1 || sourceMax / targetMax > 2 || targetMax / sourceMax > 2) {
/*     */         
/* 692 */         if (sourceMax < 1)
/* 693 */           performer.getCommunicator().sendNormalServerMessage("You need more " + realSource.getName() + "."); 
/* 694 */         if (targetMax < 1) {
/* 695 */           performer.getCommunicator().sendNormalServerMessage("You need more " + realTarget.getName() + ".");
/* 696 */         } else if (sourceMax != targetMax) {
/*     */           
/* 698 */           if (sourceMax < targetMax) {
/* 699 */             performer.getCommunicator().sendNormalServerMessage("You need to add more " + realSource
/* 700 */                 .getName() + " or remove some " + realTarget.getName() + ".");
/*     */           } else {
/*     */             
/* 703 */             performer.getCommunicator().sendNormalServerMessage("You need to add more " + realTarget
/* 704 */                 .getName() + " or remove some " + realSource.getName() + ".");
/*     */           } 
/*     */         } 
/* 707 */         throw new NoSuchItemException("Not balanced.");
/*     */       } 
/* 709 */       Item parent = null;
/* 710 */       if (template.isLiquid()) {
/*     */ 
/*     */         
/*     */         try {
/* 714 */           parent = realSource.getParent();
/*     */         }
/* 716 */         catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 721 */           Item parent2 = Items.getItem(realTarget.getParentId());
/* 722 */           if (parent == null || !parent.isContainerLiquid())
/*     */           {
/* 724 */             if (parent2.isEmpty(false)) {
/* 725 */               parent = parent2;
/*     */             }
/*     */           }
/* 728 */         } catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */         
/* 731 */         if (parent != null)
/*     */         {
/* 733 */           if (parent.getVolume() < realSource.getWeightGrams() + realTarget.getWeightGrams())
/*     */           {
/* 735 */             performer.getCommunicator().sendNormalServerMessage("Not all the liquid will fit in the " + parent
/* 736 */                 .getName() + " so some will be lost.");
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSourceWeightToRemove(Item realSource, Item realTarget, ItemTemplate template, boolean advancedEntry) {
/* 746 */     int weightToRemove = 0;
/* 747 */     if (hasCustomDepleteFromSource()) {
/*     */       
/* 749 */       weightToRemove = getDepleteFromSource();
/*     */     }
/* 751 */     else if (this.depleteEqually) {
/*     */       
/* 753 */       if (advancedEntry) {
/* 754 */         weightToRemove = realSource.getTemplate().getWeightGrams();
/*     */       } else {
/* 756 */         weightToRemove = template.getWeightGrams() / 2;
/*     */       } 
/* 758 */     } else if (template.isLiquid()) {
/*     */       
/* 760 */       int nums = 1;
/* 761 */       if (this.depleteTarget && this.depleteSource) {
/*     */         
/* 763 */         nums = getTemplateNumbers(realSource, realTarget, template);
/* 764 */         weightToRemove = nums * realSource.getTemplate().getWeightGrams();
/*     */       }
/* 766 */       else if (this.depleteSource) {
/*     */         
/* 768 */         nums = getTemplateNumbersForSource(realSource, template);
/*     */         
/* 770 */         weightToRemove = nums * realSource.getTemplate().getWeightGrams();
/*     */       } else {
/*     */         
/* 773 */         nums = 0;
/*     */       }
/*     */     
/*     */     }
/* 777 */     else if (this.depleteSource && this.depleteTarget && (realTarget.isCombine() || realTarget.isLiquid()) && (realSource
/* 778 */       .isCombine() || realSource.isLiquid())) {
/*     */       
/* 780 */       int nums = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 785 */       if (advancedEntry)
/* 786 */         nums = 1; 
/* 787 */       weightToRemove = nums * realSource.getTemplate().getWeightGrams();
/* 788 */       if (realSource.getTemplateId() == 73) {
/* 789 */         weightToRemove /= 10;
/*     */       }
/* 791 */     } else if (this.depleteSource) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 798 */       weightToRemove = realSource.getTemplate().getWeightGrams();
/* 799 */       if (this.depleteTarget) {
/*     */         
/* 801 */         if (!advancedEntry && !realTarget.isCombine() && realSource.isCombine())
/*     */         {
/* 803 */           weightToRemove = Math.max(realSource.getTemplate().getWeightGrams(), template
/* 804 */               .getWeightGrams() - realTarget.getWeightGrams());
/*     */         }
/* 806 */         if (realSource.getTemplateId() == 9 && 
/* 807 */           realSource.getWeightGrams() > realSource.getTemplate().getWeightGrams() * 0.7F) {
/* 808 */           weightToRemove = realSource.getWeightGrams();
/*     */         }
/* 810 */         if (realSource.getTemplateId() == 73) {
/* 811 */           weightToRemove /= 10;
/*     */         }
/*     */       } 
/*     */     } 
/* 815 */     return weightToRemove;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTargetWeightToRemove(Item realSource, Item realTarget, ItemTemplate template, boolean advancedEntry) {
/* 821 */     int weightToRemove = 0;
/* 822 */     if (hasCustomDepleteFromTarget()) {
/*     */       
/* 824 */       weightToRemove = getDepleteFromTarget();
/*     */     }
/* 826 */     else if (this.depleteEqually) {
/*     */       
/* 828 */       if (advancedEntry) {
/* 829 */         weightToRemove = realTarget.getTemplate().getWeightGrams();
/*     */       } else {
/* 831 */         weightToRemove = template.getWeightGrams() / 2;
/*     */       } 
/* 833 */     } else if (template.isLiquid()) {
/*     */       
/* 835 */       int nums = 1;
/* 836 */       if (this.depleteTarget && this.depleteSource) {
/* 837 */         nums = getTemplateNumbers(realSource, realTarget, template);
/* 838 */       } else if (this.depleteTarget) {
/* 839 */         nums = getTemplateNumbersForTarget(realTarget, template);
/*     */       } else {
/* 841 */         nums = 0;
/* 842 */       }  weightToRemove = nums * realTarget.getTemplate().getWeightGrams();
/*     */     
/*     */     }
/* 845 */     else if (this.depleteSource && this.depleteTarget && (realTarget.isCombine() || realTarget.isLiquid()) && (realSource
/* 846 */       .isCombine() || realSource.isLiquid())) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 856 */       int nums = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 861 */       if (advancedEntry)
/* 862 */         nums = 1; 
/* 863 */       weightToRemove = nums * (realTarget.getTemplate().getWeightGrams() + getExtraWeight(template));
/* 864 */       int weightCap = (int)((template.getWeightGrams() + getExtraWeight(template)) * 1.5F);
/* 865 */       if (weightToRemove > weightCap) {
/* 866 */         weightToRemove = weightCap;
/*     */       }
/* 868 */     } else if (this.depleteTarget) {
/*     */ 
/*     */ 
/*     */       
/* 872 */       if (!realTarget.isCombine()) {
/*     */         
/* 874 */         if (!this.depleteSource) {
/*     */           
/* 876 */           if (advancedEntry) {
/* 877 */             weightToRemove = realTarget.getTemplate().getWeightGrams() + getExtraWeight(template);
/*     */           } else {
/* 879 */             weightToRemove = template.getWeightGrams() + getExtraWeight(template);
/*     */           } 
/*     */         } else {
/* 882 */           weightToRemove = realTarget.getTemplate().getWeightGrams();
/*     */         } 
/* 884 */       } else if (this.depleteSource) {
/*     */         
/* 886 */         if (advancedEntry) {
/* 887 */           weightToRemove = realTarget.getTemplate().getWeightGrams() + getExtraWeight(template);
/*     */         } else {
/*     */           
/* 890 */           weightToRemove = template.getWeightGrams() - realSource.getTemplate().getWeightGrams() + getExtraWeight(template);
/*     */         } 
/*     */       } else {
/*     */         
/* 894 */         weightToRemove = template.getWeightGrams() + getExtraWeight(template);
/*     */       } 
/*     */     } 
/* 897 */     if (weightToRemove < 0) {
/*     */       
/* 899 */       logger.log(Level.WARNING, template.getName() + " when created depletes less than 0.");
/* 900 */       weightToRemove = realTarget.getTemplate().getWeightGrams() + getExtraWeight(template);
/*     */     } 
/* 902 */     return weightToRemove;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int getTemplateNumbersForTarget(Item realTarget, ItemTemplate template) {
/* 908 */     return 
/* 909 */       Math.max(1, realTarget.getWeightGrams() / (realTarget.getTemplate().getWeightGrams() + getExtraWeight(template)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int getTemplateNumbersForSource(Item realSource, ItemTemplate template) {
/* 915 */     return 
/* 916 */       Math.max(1, realSource.getWeightGrams() / (realSource.getTemplate().getWeightGrams() + getExtraWeight(template)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int getTemplateNumbers(Item realSource, Item realTarget, ItemTemplate template) {
/* 922 */     return Math.max(1, 
/*     */         
/* 924 */         Math.min(realSource.getWeightGrams() / realSource.getTemplate().getWeightGrams(), realTarget.getWeightGrams() / (realTarget
/* 925 */           .getTemplate().getWeightGrams() + getExtraWeight(template))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected final int getExtraWeight(ItemTemplate template) {
/* 930 */     if (template.isRune())
/*     */     {
/* 932 */       return 0;
/*     */     }
/*     */     
/* 935 */     if (this.percentageLost > 0.0F)
/*     */     {
/* 937 */       return (int)(this.percentageLost / 100.0F * template.getWeightGrams());
/*     */     }
/* 939 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean meetsCreatureRestriction(Item source, Item target) {
/* 944 */     if (this.objectCreated == 848) {
/*     */       
/* 946 */       int data = 14;
/* 947 */       return (source.getData2() == 14 || target.getData2() == 14);
/*     */     } 
/* 949 */     if (this.objectCreated == 847) {
/*     */       
/* 951 */       int data = 12;
/* 952 */       return (source.getData2() == 12 || target.getData2() == 12);
/*     */     } 
/* 954 */     if (this.objectCreated == 846) {
/*     */       
/* 956 */       int data = 42;
/* 957 */       return (source.getData2() == 42 || target.getData2() == 42);
/*     */     } 
/* 959 */     if (this.objectCreated == 849) {
/*     */       
/* 961 */       int data = 10;
/* 962 */       return (source.getData2() == 10 || target.getData2() == 10);
/*     */     } 
/*     */     
/* 965 */     return true;
/*     */   }
/*     */   
/*     */   public abstract Item run(Creature paramCreature, Item paramItem, long paramLong, float paramFloat) throws FailedException, NoSuchSkillException, NoSuchItemException;
/*     */   
/*     */   abstract CreationEntry cloneAndRevert();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\CreationEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */