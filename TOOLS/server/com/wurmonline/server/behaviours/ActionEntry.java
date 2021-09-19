/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
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
/*      */ public final class ActionEntry
/*      */   implements ActionTypes, MiscConstants, Comparable<ActionEntry>
/*      */ {
/*   38 */   private static final Logger logger = Logger.getLogger(ActionEntry.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private String actionString;
/*      */ 
/*      */   
/*      */   private final String verbString;
/*      */ 
/*      */   
/*      */   private final short number;
/*      */ 
/*      */   
/*      */   private boolean quickSkillLess = false;
/*      */ 
/*      */   
/*      */   private final int maxRange;
/*      */ 
/*      */   
/*      */   private final int prio;
/*      */ 
/*      */   
/*      */   private String animationString;
/*      */ 
/*      */   
/*      */   private boolean needsFood = false;
/*      */ 
/*      */   
/*      */   private boolean isSpell = false;
/*      */ 
/*      */   
/*      */   private boolean isOffensive = false;
/*      */ 
/*      */   
/*      */   private boolean isFatigue = false;
/*      */ 
/*      */   
/*      */   private boolean isPoliced = false;
/*      */ 
/*      */   
/*      */   private boolean isNoMove = false;
/*      */ 
/*      */   
/*      */   private boolean isNonLibila = false;
/*      */ 
/*      */   
/*      */   private boolean isNonWhiteReligion = false;
/*      */ 
/*      */   
/*      */   private boolean isNonReligion = false;
/*      */ 
/*      */   
/*      */   private boolean isAttackHigh = false;
/*      */ 
/*      */   
/*      */   private boolean isAttackLow = false;
/*      */ 
/*      */   
/*      */   private boolean isAttackLeft = false;
/*      */ 
/*      */   
/*      */   private boolean isAttackRight = false;
/*      */ 
/*      */   
/*      */   private boolean isDefend = false;
/*      */ 
/*      */   
/*      */   private boolean isStanceChange = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowFo = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowFoOnSurface = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowMagranon = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowMagranonInCave = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowVynora = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowVynoraOnSurface = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowLibila = false;
/*      */ 
/*      */   
/*      */   private boolean isAllowLibilaInCave = false;
/*      */ 
/*      */   
/*      */   private boolean isOpportunity = true;
/*      */ 
/*      */   
/*      */   private boolean ignoresRange = false;
/*      */ 
/*      */   
/*      */   private boolean vulnerable = false;
/*      */ 
/*      */   
/*      */   private boolean isMission = false;
/*      */ 
/*      */   
/*      */   private boolean notVulnerable = false;
/*      */ 
/*      */   
/*      */   private boolean isBlockedByUseOnGroundOnly = true;
/*      */ 
/*      */   
/*      */   private boolean usesNewSkillSystem = false;
/*      */ 
/*      */   
/*      */   private boolean isVerifiedNewSystem = false;
/*      */ 
/*      */   
/*      */   private boolean showOnSelectBar = false;
/*      */ 
/*      */   
/*  159 */   private int blockType = 4;
/*      */   
/*      */   private boolean sameBridgeOnly = false;
/*      */   
/*      */   private boolean isPerimeterAction = false;
/*      */   
/*      */   private boolean isCornerAction = false;
/*      */   
/*      */   private boolean isEnemyNever = false;
/*      */   
/*      */   private boolean isEnemyAlways = false;
/*      */   
/*      */   private boolean isEnemyNoGuards = false;
/*      */   
/*      */   private boolean useItemOnGround = false;
/*      */   
/*      */   private boolean stackable = true;
/*      */   
/*      */   private boolean stackableFight = true;
/*  178 */   private byte requiresActiveItem = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ActionEntry(short aNumber, int aPriority, String aActionString, String aVerbString, String aAnimationString, @Nullable int[] aTypes, int aRange, boolean blockedByUseOnGroundOnly) {
/*  209 */     this.prio = aPriority;
/*  210 */     this.number = aNumber;
/*  211 */     this.actionString = aActionString;
/*  212 */     this.verbString = aVerbString;
/*  213 */     this.animationString = aAnimationString.toLowerCase().replace(" ", "");
/*      */     
/*  215 */     this.maxRange = aRange;
/*  216 */     this.isBlockedByUseOnGroundOnly = blockedByUseOnGroundOnly;
/*  217 */     assignTypes(aTypes);
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
/*      */   ActionEntry(short aNumber, int aPriority, String aActionString, String aVerbString, @Nullable int[] aTypes, int aRange, boolean blockedByUseOnGroundOnly) {
/*  234 */     this(aNumber, aPriority, aActionString, aVerbString, aActionString, aTypes, aRange, blockedByUseOnGroundOnly);
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
/*      */   ActionEntry(short aNumber, String aActionString, String aVerbString, int[] aTypes, int aRange) {
/*  254 */     this(aNumber, aActionString, aVerbString, aActionString, aTypes, aRange);
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
/*      */   ActionEntry(short aNumber, String aActionString, String aVerbString, String aAnimationString, int[] aTypes, int aRange) {
/*  277 */     this(aNumber, 5, aActionString, aVerbString, aAnimationString, aTypes, aRange, true);
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
/*      */   ActionEntry(short aNumber, String aActionString, String aVerbString, int[] aTypes, int aRange, boolean blockedByUseOnGround) {
/*  298 */     this(aNumber, 5, aActionString, aVerbString, aActionString, aTypes, aRange, blockedByUseOnGround);
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
/*      */   ActionEntry(short aNumber, int aPriority, String aActionString, String aVerbString, int[] aTypes) {
/*  319 */     this(aNumber, aPriority, aActionString, aVerbString, aActionString, aTypes, 4, true);
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
/*      */   ActionEntry(short aNumber, int aPriority, String aActionString, String aVerbString, int[] aTypes, boolean blockedByUseOnGround) {
/*  342 */     this(aNumber, aPriority, aActionString, aVerbString, aActionString, aTypes, 4, blockedByUseOnGround);
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
/*      */   ActionEntry(short aNumber, String aActionString, String aVerbString, @Nullable int[] aTypes) {
/*  361 */     this(aNumber, aActionString, aVerbString, aActionString, aTypes);
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
/*      */   ActionEntry(short aNumber, String aActionString, String aVerbString, String aAnimationString, @Nullable int[] aTypes) {
/*  382 */     this(aNumber, 5, aActionString, aVerbString, aAnimationString, aTypes, 4, true);
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
/*      */   ActionEntry(short aNumber, String aActionString, String aVerbString, @Nullable int[] aTypes, boolean blockedByUseOnGround) {
/*  404 */     this(aNumber, 5, aActionString, aVerbString, aActionString, aTypes, 4, blockedByUseOnGround);
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
/*      */   public ActionEntry(short aNumber, String aActionString, String aVerbString) {
/*  423 */     this(aNumber, aActionString, aVerbString, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static ActionEntry createEntry(short number, String actionString, String verb, int[] types) {
/*  428 */     return new ActionEntry(number, actionString, verb, types, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void assignTypes(@Nullable int[] types) {
/*  439 */     if (types == null) {
/*      */       return;
/*      */     }
/*      */     
/*  443 */     for (int x = 0; x < types.length; x++) {
/*      */       
/*  445 */       switch (types[x]) {
/*      */ 
/*      */         
/*      */         case 0:
/*  449 */           this.quickSkillLess = true;
/*  450 */           this.isOpportunity = false;
/*      */           break;
/*      */         
/*      */         case 1:
/*  454 */           this.needsFood = true;
/*      */           break;
/*      */         case 2:
/*  457 */           this.isSpell = true;
/*      */           break;
/*      */         case 3:
/*  460 */           this.isOffensive = true;
/*      */           break;
/*      */         case 4:
/*  463 */           this.isFatigue = true;
/*      */           break;
/*      */         case 5:
/*  466 */           this.isPoliced = true;
/*      */           break;
/*      */         case 6:
/*  469 */           this.isNoMove = true;
/*      */           break;
/*      */         case 7:
/*  472 */           setIsNonLibila(true);
/*      */           break;
/*      */         case 8:
/*  475 */           setIsNonWhiteReligion(true);
/*      */           break;
/*      */         case 9:
/*  478 */           setIsNonReligion(true);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 12:
/*  487 */           this.isAttackHigh = true;
/*      */           break;
/*      */         case 13:
/*  490 */           this.isAttackLow = true;
/*      */           break;
/*      */         case 14:
/*  493 */           this.isAttackLeft = true;
/*      */           break;
/*      */         case 15:
/*  496 */           this.isAttackRight = true;
/*      */           break;
/*      */         case 16:
/*  499 */           this.isOpportunity = false;
/*  500 */           this.isDefend = true;
/*  501 */           this.ignoresRange = true;
/*      */           break;
/*      */         case 17:
/*  504 */           this.isOpportunity = false;
/*  505 */           this.isStanceChange = true;
/*  506 */           this.ignoresRange = true;
/*      */           break;
/*      */         case 18:
/*  509 */           setAllowMagranon(true);
/*      */           break;
/*      */         case 38:
/*  512 */           setAllowMagranonInCave(true);
/*      */           break;
/*      */         case 19:
/*  515 */           setAllowFo(true);
/*      */           break;
/*      */         case 39:
/*  518 */           setAllowFoOnSurface(true);
/*      */           break;
/*      */         case 20:
/*  521 */           setAllowVynora(true);
/*      */           break;
/*      */         case 52:
/*  524 */           setAllowVynoraOnSurface(true);
/*      */           break;
/*      */         case 21:
/*  527 */           setAllowLibila(true);
/*      */           break;
/*      */         case 40:
/*  530 */           setAllowLibilaInCave(true);
/*      */           break;
/*      */         case 22:
/*  533 */           this.isOpportunity = false;
/*      */           break;
/*      */         case 23:
/*  536 */           this.blockType = 0;
/*  537 */           this.ignoresRange = true;
/*      */           break;
/*      */         case 43:
/*  540 */           this.showOnSelectBar = true;
/*      */           break;
/*      */         case 24:
/*  543 */           this.vulnerable = true;
/*      */           break;
/*      */         case 25:
/*  546 */           this.isMission = true;
/*      */           break;
/*      */         case 26:
/*  549 */           this.notVulnerable = true;
/*      */           break;
/*      */         case 27:
/*  552 */           this.stackable = false;
/*      */           break;
/*      */         case 28:
/*  555 */           this.stackableFight = false;
/*      */           break;
/*      */         case 29:
/*  558 */           this.blockType = 0;
/*      */           break;
/*      */         case 30:
/*  561 */           this.blockType = 1;
/*      */           break;
/*      */         case 31:
/*  564 */           this.blockType = 2;
/*      */           break;
/*      */         case 32:
/*  567 */           this.blockType = 3;
/*      */           break;
/*      */         case 33:
/*  570 */           this.blockType = 5;
/*      */           break;
/*      */         case 34:
/*  573 */           this.blockType = 7;
/*      */           break;
/*      */         case 35:
/*  576 */           this.requiresActiveItem = 0;
/*      */           break;
/*      */         case 36:
/*  579 */           this.requiresActiveItem = 1;
/*      */           break;
/*      */         case 37:
/*  582 */           this.requiresActiveItem = 2;
/*      */           break;
/*      */         case 50:
/*  585 */           this.blockType = 8;
/*      */           break;
/*      */         case 44:
/*  588 */           setSameBridgeOnly(true);
/*      */           break;
/*      */         case 45:
/*  591 */           this.isPerimeterAction = true;
/*      */           break;
/*      */         case 46:
/*  594 */           this.isCornerAction = true;
/*      */           break;
/*      */         case 47:
/*  597 */           this.isEnemyNever = true;
/*      */           break;
/*      */         case 48:
/*  600 */           this.isEnemyAlways = true;
/*      */           break;
/*      */         case 49:
/*  603 */           this.isEnemyNoGuards = true;
/*      */           break;
/*      */         case 51:
/*  606 */           this.useItemOnGround = true;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 41:
/*  612 */           this.usesNewSkillSystem = true;
/*      */           break;
/*      */         case 42:
/*  615 */           this.isVerifiedNewSystem = true;
/*      */           break;
/*      */         default:
/*  618 */           logger.warning("Unexepected ActionType: " + types[x] + " in " + this);
/*      */           break;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getActionString() {
/*  642 */     return this.actionString;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getAnimationString() {
/*  647 */     return this.animationString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getRange() {
/*  657 */     return this.maxRange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getNumber() {
/*  668 */     return this.number;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getVerbString() {
/*  679 */     return this.verbString;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getVerbStartString() {
/*  684 */     if (this.verbString.toLowerCase().startsWith("stop ") || this.verbString.toLowerCase().startsWith("start ")) {
/*  685 */       return this.verbString;
/*      */     }
/*  687 */     return "start " + this.verbString;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getVerbFinishString() {
/*  692 */     if (this.verbString.toLowerCase().startsWith("stop ") || this.verbString.toLowerCase().startsWith("start ")) {
/*  693 */       return this.verbString;
/*      */     }
/*  695 */     return "finish " + this.verbString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isQuickSkillLess() {
/*  706 */     return this.quickSkillLess;
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
/*      */   public final boolean isStackable() {
/*  718 */     return this.stackable;
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
/*      */   public final boolean isStackableFight() {
/*  730 */     return this.stackableFight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isSpell() {
/*  740 */     return this.isSpell;
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
/*      */   final boolean needsFood() {
/*  752 */     return this.needsFood;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isFatigue() {
/*  762 */     return this.isFatigue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isBlockedByUseOnGroundOnly() {
/*  772 */     return this.isBlockedByUseOnGroundOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isPoliced() {
/*  782 */     return this.isPoliced;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isNoMove() {
/*  792 */     return this.isNoMove;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isNonLibila() {
/*  802 */     return this.isNonLibila;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isNonWhiteReligion() {
/*  812 */     return this.isNonWhiteReligion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isNonReligion() {
/*  822 */     return this.isNonReligion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDefend() {
/*  832 */     return this.isDefend;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStanceChange() {
/*  842 */     return this.isStanceChange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isOpportunity() {
/*  852 */     return this.isOpportunity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAttackHigh() {
/*  862 */     return this.isAttackHigh;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAttackLow() {
/*  872 */     return this.isAttackLow;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAttackLeft() {
/*  882 */     return this.isAttackLeft;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAttackRight() {
/*  892 */     return this.isAttackRight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isIgnoresRange() {
/*  902 */     return this.ignoresRange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOffensive() {
/*  912 */     return this.isOffensive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMission() {
/*  922 */     return this.isMission;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isShowOnSelectBar() {
/*  927 */     return this.showOnSelectBar;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isVulnerable() {
/*  933 */     if (this.notVulnerable)
/*  934 */       return false; 
/*  935 */     if (this.isSpell)
/*  936 */       return true; 
/*  937 */     if (this.isOffensive)
/*      */     {
/*  939 */       return this.vulnerable;
/*      */     }
/*  941 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean canUseNewSkillSystem() {
/*  946 */     if (this.usesNewSkillSystem && (this.isVerifiedNewSystem || Features.Feature.NEW_SKILL_SYSTEM.isEnabled()))
/*      */     {
/*  948 */       return true;
/*      */     }
/*  950 */     return false;
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
/*      */   public final int getPriority() {
/*  966 */     return this.prio;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getBlockType() {
/*  971 */     return this.blockType;
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
/*      */   public final byte getUseActiveItem() {
/*  984 */     return this.requiresActiveItem;
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
/*      */   public boolean isAllowed(Creature creature) {
/*  998 */     if (creature.getDeity() != null) {
/*      */       
/* 1000 */       if (creature.getDeity().isWaterGod()) {
/*      */         
/* 1002 */         if (creature.isOnSurface() && isAllowVynoraOnSurface())
/* 1003 */           return true; 
/* 1004 */         if (isAllowVynora())
/* 1005 */           return true; 
/*      */       } 
/* 1007 */       if (creature.getDeity().isMountainGod()) {
/*      */         
/* 1009 */         if (!creature.isOnSurface() && isAllowMagranonInCave())
/* 1010 */           return true; 
/* 1011 */         if (isAllowMagranon())
/* 1012 */           return true; 
/*      */       } 
/* 1014 */       if (creature.getDeity().isForestGod()) {
/*      */         
/* 1016 */         if (creature.isOnSurface() && isAllowFoOnSurface())
/* 1017 */           return true; 
/* 1018 */         if (isAllowFo())
/* 1019 */           return true; 
/*      */       } 
/* 1021 */       if (creature.getDeity().isHateGod()) {
/*      */         
/* 1023 */         if (!creature.isOnSurface() && isAllowLibilaInCave())
/* 1024 */           return true; 
/* 1025 */         if (isAllowLibila())
/* 1026 */           return true; 
/*      */       } 
/*      */     } 
/* 1029 */     return false;
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
/*      */   public int compareTo(ActionEntry aActionEntry) {
/* 1042 */     return getActionString().compareTo(aActionEntry.getActionString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1048 */     StringBuilder lBuilder = new StringBuilder();
/* 1049 */     lBuilder.append("ActionEntry [");
/* 1050 */     lBuilder.append("Number: ").append(this.number);
/* 1051 */     lBuilder.append(", Action String: ").append(this.actionString);
/* 1052 */     lBuilder.append(", Verb String: ").append(this.verbString);
/* 1053 */     lBuilder.append(", Range: ").append(this.maxRange);
/* 1054 */     lBuilder.append(", Priority: ").append(this.prio);
/* 1055 */     lBuilder.append(", UseActiveItem: ").append(this.requiresActiveItem);
/* 1056 */     lBuilder.append(']');
/* 1057 */     return lBuilder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSameBridgeOnly() {
/* 1067 */     return this.sameBridgeOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSameBridgeOnly(boolean aSameBridgeOnly) {
/* 1078 */     this.sameBridgeOnly = aSameBridgeOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPerimeterAction() {
/* 1088 */     return this.isPerimeterAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseItemOnGroundAction() {
/* 1098 */     return this.useItemOnGround;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCornerAction() {
/* 1108 */     return this.isCornerAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnemyNeverAllowed() {
/* 1118 */     return this.isEnemyNever;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnemyAlwaysAllowed() {
/* 1128 */     return this.isEnemyAlways;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnemyAllowedWhenNoGuards() {
/* 1138 */     return this.isEnemyNoGuards;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNonReligion(boolean nonReligion) {
/* 1148 */     this.isNonReligion = nonReligion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNonWhiteReligion(boolean nonWhiteReligion) {
/* 1158 */     this.isNonWhiteReligion = nonWhiteReligion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNonLibila(boolean nonLibila) {
/* 1168 */     this.isNonLibila = nonLibila;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowFo() {
/* 1178 */     return this.isAllowFo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowFo(boolean allowFo) {
/* 1188 */     this.isAllowFo = allowFo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowFoOnSurface() {
/* 1199 */     return this.isAllowFoOnSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowFoOnSurface(boolean allowFoOnSurface) {
/* 1209 */     this.isAllowFoOnSurface = allowFoOnSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowMagranon() {
/* 1219 */     return this.isAllowMagranon;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowMagranon(boolean allowMagranon) {
/* 1229 */     this.isAllowMagranon = allowMagranon;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowMagranonInCave() {
/* 1239 */     return this.isAllowMagranonInCave;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowMagranonInCave(boolean allowMagranonInCave) {
/* 1248 */     this.isAllowMagranonInCave = allowMagranonInCave;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowVynora() {
/* 1258 */     return this.isAllowVynora;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowVynora(boolean allowVynora) {
/* 1268 */     this.isAllowVynora = allowVynora;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowVynoraOnSurface() {
/* 1279 */     return this.isAllowVynoraOnSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowVynoraOnSurface(boolean allowVynoraOnSurface) {
/* 1289 */     this.isAllowVynoraOnSurface = allowVynoraOnSurface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowLibila() {
/* 1299 */     return this.isAllowLibila;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowLibila(boolean allowLibila) {
/* 1309 */     this.isAllowLibila = allowLibila;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowLibilaInCave() {
/* 1319 */     return this.isAllowLibilaInCave;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowLibilaInCave(boolean allowLibilaInCave) {
/* 1329 */     this.isAllowLibilaInCave = allowLibilaInCave;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ActionEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */