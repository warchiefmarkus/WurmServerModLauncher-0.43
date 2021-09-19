/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import java.util.BitSet;
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
/*     */ 
/*     */ public class Permissions
/*     */ {
/*     */   public enum Allow
/*     */     implements IPermission
/*     */   {
/* 503 */     SETTLEMENT_MAY_MANAGE(0, "Allow Settlememnt to Manage", "Allow", "Manage", ""),
/*     */     
/* 505 */     NOT_RUNEABLE(7, "Item Attributes", "Cannot be", "Runed", ""),
/* 506 */     SEALED_BY_PLAYER(8, "Item Attributes", "Cannot", "Take / Put / Eat or Drink", ""),
/* 507 */     NO_EAT_OR_DRINK(9, "Item Attributes", "Cannot", "Eat or Drink", ""),
/* 508 */     OWNER_TURNABLE(10, "Item Attributes", "Turnable", "by Owner", ""),
/* 509 */     OWNER_MOVEABLE(11, "Item Attributes", "Moveable", "by Owner", ""),
/* 510 */     NO_DRAG(12, "Item Attributes", "Cannot be", "Dragged", ""),
/* 511 */     NO_IMPROVE(13, "Item Attributes", "Cannot be", "Improved", ""),
/* 512 */     NO_DROP(14, "Item Attributes", "Cannot be", "Dropped", ""),
/* 513 */     NO_REPAIR(15, "Item Attributes", "Cannot be", "Repaired", ""),
/* 514 */     PLANTED(16, "Item Attributes", "Is", "Planted", ""),
/* 515 */     AUTO_FILL(17, "Item Attributes", "Auto", "Fills", ""),
/* 516 */     AUTO_LIGHT(18, "Item Attributes", "Auto", "Lights", ""),
/* 517 */     ALWAYS_LIT(19, "Item Attributes", "Always", "Lit", ""),
/* 518 */     HAS_COURIER(20, "Item Attributes", "Has", "Courier", ""),
/* 519 */     HAS_DARK_MESSENGER(21, "Item Attributes", "Has", "Dark Messanger", ""),
/* 520 */     DECAY_DISABLED(22, "Item Attributes", "Decay", "Disabled", ""),
/* 521 */     NO_TAKE(23, "Item Attributes", "Cannot be", "Taken", ""),
/* 522 */     NO_SPELLS(24, "Item Restrictions", "Cannot be", "Cast Upon", ""),
/* 523 */     NO_BASH(25, "Item Restrictions", "Cannot be", "Bashed / Destroyed", ""),
/* 524 */     NOT_LOCKABLE(26, "Item Restrictions", "Cannot be", "Locked", ""),
/* 525 */     NOT_LOCKPICKABLE(27, "Item Restrictions", "Cannot be", "Lockpicked", ""),
/* 526 */     NOT_MOVEABLE(28, "Item Restrictions", "Cannot be", "Moved", ""),
/* 527 */     NOT_TURNABLE(29, "Item Restrictions", "Cannot be", "Turned", ""),
/* 528 */     NOT_PAINTABLE(30, "Item Restrictions", "Cannot be", "Painted", ""),
/* 529 */     NO_PUT(31, "Item Attributes", "Cannot", "Put items inside", "");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final byte bit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String description;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String header1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String header2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final String hover;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Allow(int aBit, String aDescription, String aHeader1, String aHeader2, String aHover) {
/*     */       this.bit = (byte)aBit;
/*     */       this.description = aDescription;
/*     */       this.header1 = aHeader1;
/*     */       this.header2 = aHeader2;
/*     */       this.hover = aHover;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getBit() {
/*     */       return this.bit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 616 */     private static final Allow[] types = values();
/*     */     public int getValue() { return 1 << this.bit; }
/*     */     public String getDescription() { return this.description; }
/*     */     public String getHeader1() { return this.header1; }
/*     */     public String getHeader2() { return this.header2; }
/* 621 */     public static Permissions.IPermission[] getPermissions() { return (Permissions.IPermission[])types; } public String getHover() {
/*     */       return this.hover;
/*     */     } static {
/*     */     
/* 625 */     } } private int permissions = 0;
/* 626 */   protected BitSet permissionBits = new BitSet(32);
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
/*     */   public void setPermissionBits(int newPermissions) {
/* 638 */     this.permissions = newPermissions;
/* 639 */     this.permissionBits.clear();
/* 640 */     for (int x = 0; x < 32; x++) {
/*     */       
/* 642 */       if ((newPermissions >>> x & 0x1) == 1)
/*     */       {
/* 644 */         this.permissionBits.set(x);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasPermission(int permissionBit) {
/* 656 */     if (this.permissions != 0)
/*     */     {
/* 658 */       return this.permissionBits.get(permissionBit);
/*     */     }
/* 660 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final int getPermissionsInt() {
/* 665 */     int ret = 0;
/* 666 */     for (int x = 0; x < 32; x++) {
/*     */       
/* 668 */       if (this.permissionBits.get(x))
/*     */       {
/* 670 */         ret = (int)(ret + (1L << x));
/*     */       }
/*     */     } 
/* 673 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPermissionBit(int bit, boolean value) {
/* 683 */     this.permissionBits.set(bit, value);
/* 684 */     this.permissions = getPermissionsInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPermissions() {
/* 692 */     return this.permissions;
/*     */   }
/*     */   
/*     */   public static interface IAllow {
/*     */     boolean canBeAlwaysLit();
/*     */     
/*     */     boolean canBeAutoFilled();
/*     */     
/*     */     boolean canBeAutoLit();
/*     */     
/*     */     boolean canBePeggedByPlayer();
/*     */     
/*     */     boolean canBePlanted();
/*     */     
/*     */     boolean canBeSealedByPlayer();
/*     */     
/*     */     boolean canChangeCreator();
/*     */     
/*     */     boolean canDisableDecay();
/*     */     
/*     */     boolean canDisableDestroy();
/*     */     
/*     */     boolean canDisableDrag();
/*     */     
/*     */     boolean canDisableDrop();
/*     */     
/*     */     boolean canDisableEatAndDrink();
/*     */     
/*     */     boolean canDisableImprove();
/*     */     
/*     */     boolean canDisableLocking();
/*     */     
/*     */     boolean canDisableLockpicking();
/*     */     
/*     */     boolean canDisableMoveable();
/*     */     
/*     */     boolean canDisableOwnerMoveing();
/*     */     
/*     */     boolean canDisableOwnerTurning();
/*     */     
/*     */     boolean canDisablePainting();
/*     */     
/*     */     boolean canDisablePut();
/*     */     
/*     */     boolean canDisableRepair();
/*     */     
/*     */     boolean canDisableRuneing();
/*     */     
/*     */     boolean canDisableSpellTarget();
/*     */     
/*     */     boolean canDisableTake();
/*     */     
/*     */     boolean canDisableTurning();
/*     */     
/*     */     boolean canHaveCourier();
/*     */     
/*     */     boolean canHaveDakrMessenger();
/*     */     
/*     */     String getCreatorName();
/*     */     
/*     */     float getDamage();
/*     */     
/*     */     String getName();
/*     */     
/*     */     float getQualityLevel();
/*     */     
/*     */     boolean hasCourier();
/*     */     
/*     */     boolean hasDarkMessenger();
/*     */     
/*     */     boolean hasNoDecay();
/*     */     
/*     */     boolean isAlwaysLit();
/*     */     
/*     */     boolean isAutoFilled();
/*     */     
/*     */     boolean isAutoLit();
/*     */     
/*     */     boolean isIndestructible();
/*     */     
/*     */     boolean isNoDrag();
/*     */     
/*     */     boolean isNoDrop();
/*     */     
/*     */     boolean isNoEatOrDrink();
/*     */     
/*     */     boolean isNoImprove();
/*     */     
/*     */     boolean isNoMove();
/*     */     
/*     */     boolean isNoPut();
/*     */     
/*     */     boolean isNoRepair();
/*     */     
/*     */     boolean isNoTake();
/*     */     
/*     */     boolean isNotLockable();
/*     */     
/*     */     boolean isNotLockpickable();
/*     */     
/*     */     boolean isNotPaintable();
/*     */     
/*     */     boolean isNotRuneable();
/*     */     
/*     */     boolean isNotSpellTarget();
/*     */     
/*     */     boolean isNotTurnable();
/*     */     
/*     */     boolean isOwnerMoveable();
/*     */     
/*     */     boolean isOwnerTurnable();
/*     */     
/*     */     boolean isPlanted();
/*     */     
/*     */     boolean isSealedByPlayer();
/*     */     
/*     */     void setCreator(String param1String);
/*     */     
/*     */     boolean setDamage(float param1Float);
/*     */     
/*     */     void setHasCourier(boolean param1Boolean);
/*     */     
/*     */     void setHasDarkMessenger(boolean param1Boolean);
/*     */     
/*     */     void setHasNoDecay(boolean param1Boolean);
/*     */     
/*     */     void setIsAlwaysLit(boolean param1Boolean);
/*     */     
/*     */     void setIsAutoFilled(boolean param1Boolean);
/*     */     
/*     */     void setIsAutoLit(boolean param1Boolean);
/*     */     
/*     */     void setIsIndestructible(boolean param1Boolean);
/*     */     
/*     */     void setIsNoDrag(boolean param1Boolean);
/*     */     
/*     */     void setIsNoDrop(boolean param1Boolean);
/*     */     
/*     */     void setIsNoEatOrDrink(boolean param1Boolean);
/*     */     
/*     */     void setIsNoImprove(boolean param1Boolean);
/*     */     
/*     */     void setIsNoMove(boolean param1Boolean);
/*     */     
/*     */     void setIsNoPut(boolean param1Boolean);
/*     */     
/*     */     void setIsNoRepair(boolean param1Boolean);
/*     */     
/*     */     void setIsNoTake(boolean param1Boolean);
/*     */     
/*     */     void setIsNotLockable(boolean param1Boolean);
/*     */     
/*     */     void setIsNotLockpickable(boolean param1Boolean);
/*     */     
/*     */     void setIsNotPaintable(boolean param1Boolean);
/*     */     
/*     */     void setIsNotRuneable(boolean param1Boolean);
/*     */     
/*     */     void setIsNotSpellTarget(boolean param1Boolean);
/*     */     
/*     */     void setIsNotTurnable(boolean param1Boolean);
/*     */     
/*     */     void setIsOwnerMoveable(boolean param1Boolean);
/*     */     
/*     */     void setIsOwnerTurnable(boolean param1Boolean);
/*     */     
/*     */     void setIsPlanted(boolean param1Boolean);
/*     */     
/*     */     void setIsSealedByPlayer(boolean param1Boolean);
/*     */     
/*     */     boolean setQualityLevel(float param1Float);
/*     */     
/*     */     void setOriginalQualityLevel(float param1Float);
/*     */     
/*     */     void savePermissions();
/*     */   }
/*     */   
/*     */   public static interface IPermission {
/*     */     byte getBit();
/*     */     
/*     */     int getValue();
/*     */     
/*     */     String getDescription();
/*     */     
/*     */     String getHeader1();
/*     */     
/*     */     String getHeader2();
/*     */     
/*     */     String getHover();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Permissions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */