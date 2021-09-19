/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.NoSpaceException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class AutoEquipMethods
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(AutoEquipMethods.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static List<Short> nextAutoEquipAction = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public static List<ActionEntry> getBehaviours(Item item, Creature player) {
/*  63 */     List<ActionEntry> entries = new LinkedList<>();
/*  64 */     if (item.isCreatureWearableOnly())
/*  65 */       return entries; 
/*  66 */     if (player.hasFlag(7))
/*  67 */       return entries; 
/*  68 */     Item parent = item.getParentOrNull();
/*  69 */     if (parent != null && parent.isBodyPart() && parent.getOwnerId() == player.getWurmId()) {
/*     */       
/*  71 */       entries.add(Actions.actionEntrys[585]);
/*  72 */       return entries;
/*     */     } 
/*     */     
/*  75 */     byte[] slots = getValidEquipmentSlots(item);
/*  76 */     if (slots != null && parent != null)
/*     */     {
/*  78 */       if (containsLeftOrRightSlots(slots, item) && slots.length > 1) {
/*     */         
/*  80 */         entries.add(Actions.actionEntrys[582]);
/*  81 */         entries.add(new ActionEntry((short)-2, "Equip specific", "equipping"));
/*  82 */         entries.add(Actions.actionEntrys[584]);
/*  83 */         entries.add(Actions.actionEntrys[583]);
/*     */       }
/*  85 */       else if (isMultiSlot(slots, item) && slots.length > 1) {
/*     */         
/*  87 */         Item part1 = getBodySlot(slots[0], player);
/*  88 */         Item part2 = getBodySlot(slots[1], player);
/*  89 */         entries.add(Actions.actionEntrys[582]);
/*  90 */         short num = 0;
/*  91 */         if (part1 != null)
/*  92 */           num = (short)(num - 1); 
/*  93 */         if (part2 != null)
/*  94 */           num = (short)(num - 1); 
/*  95 */         if (num < 0)
/*  96 */           entries.add(new ActionEntry(num, "Equip specific", "equipping")); 
/*  97 */         if (part1 != null)
/*  98 */           entries.add(new ActionEntry((short)584, "equip " + part1.getName(), "equipping")); 
/*  99 */         if (part2 != null) {
/* 100 */           entries.add(new ActionEntry((short)583, "equip " + part2.getName(), "equipping"));
/*     */         }
/* 102 */       } else if (slots.length > 0) {
/*     */         
/* 104 */         entries.add(Actions.actionEntrys[582]);
/*     */       } 
/*     */     }
/* 107 */     return entries;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean dropFromParent(Item item, Item parent) {
/*     */     try {
/* 114 */       if (parent != null) {
/*     */         
/* 116 */         parent.dropItem(item.getWurmId(), parent.getWurmId(), false);
/* 117 */         if (parent.getTemplateId() == 177)
/*     */         {
/* 119 */           parent.removeFromPile(item);
/*     */         }
/* 121 */         return true;
/*     */       } 
/*     */       
/* 124 */       return false;
/*     */     }
/* 126 */     catch (NoSuchItemException nsi) {
/*     */       
/* 128 */       logger.log(Level.WARNING, "Failed to drop item: " + item.getName() + " id: " + item.getWurmId() + " from parent: " + parent
/* 129 */           .getName() + " id: " + parent.getWurmId(), (Throwable)nsi);
/* 130 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean dropToInventory(Item item, Creature player) {
/* 136 */     Item parent = item.getParentOrNull();
/* 137 */     dropFromParent(item, parent);
/* 138 */     if (player.getInventory().testInsertItem(item)) {
/*     */       
/* 140 */       player.getInventory().insertItem(item);
/* 141 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 145 */     parent.insertItem(item);
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean canCarry(Item item, Creature player) {
/* 152 */     return (item.getTopParent() == player.getInventory().getWurmId() || item.getTopParent() == player.getBody().getId()) ? true : player
/* 153 */       .canCarry(item.getFullWeight());
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean autoEquipWeapon(Item item, Creature player, byte slot, boolean isLeft) {
/* 158 */     Item part = getBodySlot(slot, player);
/* 159 */     if (part == null) {
/*     */       
/* 161 */       logger.log(Level.WARNING, "(autoEquipWeapon) Player: " + player.getName() + " is unable to find body part for slot: " + slot);
/*     */       
/* 163 */       return false;
/*     */     } 
/* 165 */     Item leftWeapon = player.getLefthandItem();
/* 166 */     Item rightWeapon = player.getRighthandItem();
/* 167 */     Item shield = player.getShield();
/* 168 */     Item parent = item.getParentOrNull();
/*     */     
/* 170 */     boolean hasLeft = (leftWeapon != null);
/* 171 */     boolean hasRight = (rightWeapon != null);
/* 172 */     boolean hasShield = (shield != null);
/*     */     
/* 174 */     boolean hasTwoHander = ((hasRight && rightWeapon.isTwoHanded()) || (hasLeft && leftWeapon.isTwoHanded()));
/* 175 */     boolean failedReplace = false;
/* 176 */     if ((hasLeft && isLeft) || (hasLeft && hasTwoHander) || (hasLeft && item.isTwoHanded()))
/* 177 */       failedReplace = !dropToInventory(leftWeapon, player); 
/* 178 */     if (((hasRight && !isLeft) || (hasRight && hasTwoHander) || (hasRight && item.isTwoHanded())) && !failedReplace)
/* 179 */       failedReplace = !dropToInventory(rightWeapon, player); 
/* 180 */     if ((hasShield && isLeft) || (hasShield && item.isTwoHanded() && !failedReplace))
/* 181 */       failedReplace = !dropToInventory(shield, player); 
/* 182 */     if (failedReplace) {
/* 183 */       return false;
/*     */     }
/* 185 */     boolean canCarry = canCarry(item, player);
/* 186 */     if (part.testInsertItem(item) && canCarry) {
/*     */       
/* 188 */       if (!dropFromParent(item, parent))
/* 189 */         return false; 
/* 190 */       part.insertItem(item);
/* 191 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 195 */     if (parent != null)
/* 196 */       parent.insertItem(item); 
/* 197 */     if (!canCarry)
/* 198 */       player.getCommunicator().sendNormalServerMessage("You are carrying too much."); 
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Item getCurrentlyEquippedItem(Item part) {
/* 205 */     if (part != null)
/*     */     {
/* 207 */       for (Item item : part.getItems()) {
/*     */         
/* 209 */         if (!item.isBodyPart())
/* 210 */           return item; 
/*     */       } 
/*     */     }
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addNextAutoEquipAction(short action) {
/* 218 */     nextAutoEquipAction.add(Short.valueOf(action));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean timedAutoEquip(Creature performer, long itemId, short action, Action act, float counter) {
/* 223 */     boolean done = false;
/* 224 */     Item armour = null;
/*     */     
/*     */     try {
/* 227 */       armour = Items.getItem(itemId);
/*     */     }
/* 229 */     catch (NoSuchItemException e) {
/*     */       
/* 231 */       performer.getCommunicator().sendNormalServerMessage("You cannot equip that.");
/* 232 */       return true;
/*     */     } 
/*     */     
/* 235 */     if (counter == 1.0F) {
/*     */       
/* 237 */       act.setTimeLeft((int)Math.max(25.0D, 50.0D - performer.getBodyControlSkill().getKnowledge() * 0.4000000059604645D));
/* 238 */       performer.getCommunicator().sendNormalServerMessage("You try to quickly equip the " + armour.getName() + ".");
/* 239 */       performer.sendActionControl(Actions.actionEntrys[action].getVerbString(), true, act.getTimeLeft());
/*     */     } 
/*     */     
/* 242 */     if (act.currentSecond() % 2 == 0)
/*     */     {
/* 244 */       performer.getStatus().modifyStamina(-1000.0F);
/*     */     }
/*     */     
/* 247 */     if (counter > (act.getTimeLeft() / 10)) {
/*     */       
/* 249 */       short actualAction = action;
/* 250 */       if (!nextAutoEquipAction.isEmpty())
/* 251 */         actualAction = ((Short)nextAutoEquipAction.remove(0)).shortValue(); 
/* 252 */       autoEquip(itemId, performer, actualAction, act);
/* 253 */       done = true;
/*     */     } 
/*     */     
/* 256 */     return done;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean timedDragEquip(Creature performer, long itemId, long targetId, Action act, float counter) {
/* 261 */     boolean done = false;
/* 262 */     Item armour = null;
/*     */     
/*     */     try {
/* 265 */       armour = Items.getItem(itemId);
/*     */     }
/* 267 */     catch (NoSuchItemException e) {
/*     */       
/* 269 */       performer.getCommunicator().sendNormalServerMessage("You cannot equip that.");
/* 270 */       return true;
/*     */     } 
/* 272 */     if (armour == null) {
/* 273 */       return true;
/*     */     }
/* 275 */     if (counter == 1.0F) {
/*     */       
/* 277 */       act.setTimeLeft((int)Math.max(25.0D, 50.0D - performer.getBodyControlSkill().getKnowledge() * 0.4000000059604645D));
/* 278 */       performer.getCommunicator().sendNormalServerMessage("You try to quickly equip the " + armour.getName() + ".");
/* 279 */       performer.sendActionControl(Actions.actionEntrys[723].getVerbString(), true, act.getTimeLeft());
/*     */     } 
/*     */     
/* 282 */     if (act.currentSecond() % 2 == 0)
/*     */     {
/* 284 */       performer.getStatus().modifyStamina(-1000.0F);
/*     */     }
/*     */     
/* 287 */     if (counter > (act.getTimeLeft() / 10)) {
/*     */       
/* 289 */       Item topParent = armour.getTopParentOrNull();
/*     */       
/*     */       try {
/* 292 */         if (armour.moveToItem(performer, targetId, true)) {
/*     */           
/* 294 */           performer.getCommunicator().sendUpdateInventoryItem(armour);
/* 295 */           if (topParent != null && topParent.getTemplateId() != 177) {
/* 296 */             topParent.updateModelNameOnGroundItem();
/*     */           }
/*     */         } 
/* 299 */       } catch (NoSuchItemException|com.wurmonline.server.NoSuchPlayerException|com.wurmonline.server.creatures.NoSuchCreatureException e) {
/*     */         
/* 301 */         performer.getCommunicator().sendNormalServerMessage("You cannot equip that.");
/*     */       } 
/*     */       
/* 304 */       done = true;
/*     */     } 
/* 306 */     return done;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean autoEquip(long itemId, Creature player, short action, Action act) {
/*     */     try {
/* 313 */       Item item = Items.getItem(itemId);
/* 314 */       if (!item.isBulkContainer() && !item.isNoTake())
/* 315 */         return autoEquip(item, player, action, act, true); 
/* 316 */       return false;
/*     */     }
/* 318 */     catch (NoSuchItemException nsi) {
/*     */       
/* 320 */       logger.log(Level.WARNING, "Unable to find item to equip.", (Throwable)nsi);
/* 321 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean autoEquip(Item item, Creature player, short action, Action act, boolean performGuardCheck) {
/* 328 */     if (item.isCreatureWearableOnly())
/* 329 */       return false; 
/* 330 */     if (item.getTopParent() == player.getBody().getId())
/*     */     {
/* 332 */       if (item.getParentOrNull() != null && !item.getParentOrNull().isHollow()) {
/*     */         
/* 334 */         player.getCommunicator().sendNormalServerMessage("You already have this item equipped.");
/* 335 */         return false;
/*     */       } 
/*     */     }
/* 338 */     if (item.getParentOrNull() == null) {
/* 339 */       return false;
/*     */     }
/* 341 */     if (item.isTraded()) {
/*     */       
/* 343 */       player.getCommunicator().sendNormalServerMessage("You are not allowed to do that, the item: " + item
/* 344 */           .getName() + " is being traded.");
/* 345 */       return false;
/*     */     } 
/*     */     
/* 348 */     if (!MethodsItems.isLootableBy(player, item) || item.isNoTake()) {
/*     */       
/* 350 */       player.getCommunicator().sendNormalServerMessage("You are not allowed to loot that.");
/* 351 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 355 */     TakeResultEnum result = MethodsItems.take(act, player, item);
/* 356 */     switch (result) {
/*     */       case SUCCESS:
/*     */       case TARGET_HAS_NO_OWNER:
/*     */       case PERFORMER_IS_OWNER:
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 365 */         result.sendToPerformer(player);
/* 366 */         return false;
/*     */     } 
/*     */     
/* 369 */     if (MethodsItems.checkIfStealing(item, player, null)) {
/*     */       
/* 371 */       int tilex = (int)item.getPosX() >> 2;
/* 372 */       int tiley = (int)item.getPosY() >> 2;
/* 373 */       Village vil = Zones.getVillage(tilex, tiley, player.isOnSurface());
/* 374 */       if (player.isLegal() && vil != null) {
/*     */         
/* 376 */         player.getCommunicator().sendNormalServerMessage("That would be illegal here. You can check the settlement token for the local laws.");
/*     */         
/* 378 */         return false;
/*     */       } 
/* 380 */       if (player.getDeity() != null && !player.getDeity().isLibila())
/*     */       {
/* 382 */         if (player.faithful) {
/*     */           
/* 384 */           player.getCommunicator().sendNormalServerMessage("Your deity would never allow stealing.");
/* 385 */           return false;
/*     */         } 
/*     */       }
/*     */       
/* 389 */       if (!Servers.localServer.PVPSERVER) {
/*     */         
/* 391 */         player.getCommunicator().sendNormalServerMessage("That would be very bad for your karma and is disallowed on this server.");
/*     */         
/* 393 */         return false;
/*     */       } 
/*     */       
/* 396 */       if (!player.maySteal()) {
/*     */         
/* 398 */         player.getCommunicator().sendNormalServerMessage("You need more body control to steal things.");
/* 399 */         return false;
/*     */       } 
/*     */       
/* 402 */       if (performGuardCheck && 
/* 403 */         MethodsItems.setTheftEffects(player, act, item)) {
/* 404 */         return false;
/*     */       }
/*     */     } 
/* 407 */     if (action == 582 || action == 724 || action == 723) {
/*     */       
/* 409 */       byte[] spaces = getValidEquipmentSlots(item);
/* 410 */       if (item.isShield())
/*     */       {
/* 412 */         return equipShield(item, player, spaces[0]);
/*     */       }
/* 414 */       if (containsLeftOrRightSlots(spaces, item)) {
/*     */         
/* 416 */         byte leftSlot = getLeftSlot(spaces);
/* 417 */         byte rightSlot = getRightSlot(spaces);
/* 418 */         return equipLeftRight(player, item, leftSlot, rightSlot, act);
/*     */       } 
/* 420 */       if (isMultiSlot(spaces, item)) {
/*     */         
/* 422 */         byte rightSlot = spaces[0];
/* 423 */         byte leftSlot = spaces[1];
/* 424 */         return equipLeftRight(player, item, leftSlot, rightSlot, act);
/*     */       } 
/*     */ 
/*     */       
/* 428 */       for (int i = 0; i < spaces.length; i++) {
/*     */         
/* 430 */         if (tryEquipInSlot(spaces[i], item, player))
/* 431 */           return true; 
/*     */       } 
/* 433 */       return false;
/*     */     } 
/*     */     
/* 436 */     if (action == 583)
/*     */     {
/* 438 */       return tryEquipLeft(item, player, act);
/*     */     }
/* 440 */     if (action == 584)
/*     */     {
/* 442 */       return tryEquipRight(item, player, act);
/*     */     }
/* 444 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean equipShield(Item item, Creature player, byte slot) {
/* 449 */     Item part = getBodySlot(slot, player);
/* 450 */     if (part == null)
/* 451 */       return false; 
/* 452 */     Item leftWeapon = player.getLefthandItem();
/* 453 */     Item shield = player.getShield();
/* 454 */     Item rightWeapon = player.getRighthandItem();
/* 455 */     Item parent = item.getParentOrNull();
/* 456 */     boolean hasShield = (shield != null);
/* 457 */     boolean hasLeftWeapon = (leftWeapon != null);
/* 458 */     boolean hasRightWeapon = (rightWeapon != null);
/*     */     
/* 460 */     boolean isTwoHanded = ((hasRightWeapon && rightWeapon.isTwoHanded()) || (hasLeftWeapon && leftWeapon.isTwoHanded()));
/* 461 */     boolean failedReplace = false;
/*     */     
/* 463 */     if (hasRightWeapon && isTwoHanded)
/* 464 */       failedReplace = !dropToInventory(rightWeapon, player); 
/* 465 */     if (hasLeftWeapon && !failedReplace)
/* 466 */       failedReplace = !dropToInventory(leftWeapon, player); 
/* 467 */     if (hasShield && !failedReplace)
/* 468 */       failedReplace = !dropToInventory(shield, player); 
/* 469 */     if (failedReplace)
/* 470 */       return false; 
/* 471 */     dropFromParent(item, parent);
/* 472 */     if (part.testInsertItem(item) && player.canCarry(item.getFullWeight())) {
/*     */       
/* 474 */       part.insertItem(item);
/* 475 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 479 */     if (parent != null)
/* 480 */       parent.insertItem(item); 
/* 481 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean equipLeftRight(Creature player, Item item, byte leftSlot, byte rightSlot, Action act) {
/* 488 */     Item leftPart = getBodySlot(leftSlot, player);
/* 489 */     Item rightPart = getBodySlot(rightSlot, player);
/* 490 */     Item equippedRight = getCurrentlyEquippedItem(rightPart);
/*     */     
/* 492 */     if (rightSlot != -1 && (equippedRight == null || item.isWeapon() || item.isWeaponBow() || equippedRight.isTwoHanded()))
/* 493 */       return autoEquip(item, player, (short)584, act, false); 
/* 494 */     if (leftSlot != -1 && isSlotEmpty(leftPart)) {
/* 495 */       return autoEquip(item, player, (short)583, act, false);
/*     */     }
/*     */     
/* 498 */     Item equippedLeft = getCurrentlyEquippedItem(leftPart);
/* 499 */     if (rightSlot != -1 && equippedRight.getTemplateId() != item.getTemplateId())
/* 500 */       return autoEquip(item, player, (short)584, act, false); 
/* 501 */     if (leftSlot != -1 && equippedLeft.getTemplateId() != item.getTemplateId()) {
/* 502 */       return autoEquip(item, player, (short)583, act, false);
/*     */     }
/* 504 */     return autoEquip(item, player, (short)584, act, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Item getBodySlot(byte slot, Creature player) {
/*     */     try {
/* 512 */       return player.getBody().getBodyPart(slot);
/*     */     }
/* 514 */     catch (NoSpaceException ns) {
/*     */       
/* 516 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final byte[] getValidEquipmentSlots(Item item) {
/* 522 */     int templateId = item.getTemplateId();
/* 523 */     if (item.isShield()) {
/* 524 */       return new byte[] { 44 };
/*     */     }
/* 526 */     if (item.isWeapon()) {
/* 527 */       return new byte[] { 37, 38 };
/*     */     }
/* 529 */     if (item.isWeaponBow()) {
/* 530 */       return new byte[] { 38 };
/*     */     }
/* 532 */     if (item.isArmour() && !item.isShield()) {
/*     */       
/* 534 */       byte[] spaces = item.getBodySpaces();
/* 535 */       boolean containsSecondHead = false;
/* 536 */       for (int i = 0; i < spaces.length; i++) {
/* 537 */         if (spaces[i] == 28)
/* 538 */           containsSecondHead = true; 
/* 539 */       }  if (containsSecondHead) {
/* 540 */         return new byte[] { 1 };
/*     */       }
/* 542 */       return spaces;
/*     */     } 
/* 544 */     if (item.isBelt()) {
/* 545 */       return new byte[] { 43 };
/*     */     }
/* 547 */     if (templateId == 297) {
/* 548 */       return new byte[] { 40, 39 };
/*     */     }
/* 550 */     if (templateId == 231) {
/* 551 */       return new byte[] { 3, 4 };
/*     */     }
/* 553 */     if (templateId == 230 || templateId == 740 || templateId == 985) {
/* 554 */       return new byte[] { 36 };
/*     */     }
/* 556 */     if (templateId == 443) {
/* 557 */       return new byte[] { 41 };
/*     */     }
/* 559 */     if (item.isBag()) {
/* 560 */       return new byte[] { 42 };
/*     */     }
/* 562 */     if (item.isQuiver()) {
/* 563 */       return new byte[] { 42, 41 };
/*     */     }
/*     */     
/* 566 */     return item.getBodySpaces();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean unequip(long target, Creature player) {
/*     */     try {
/* 574 */       Item item = Items.getItem(target);
/* 575 */       return unequip(item, player);
/*     */     }
/* 577 */     catch (NoSuchItemException nsi) {
/*     */       
/* 579 */       logger.log(Level.WARNING, "Unable to find item to unequip.", (Throwable)nsi);
/* 580 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean unequip(Item item, Creature player) {
/* 586 */     if (item.getTopParent() != player.getBody().getId()) {
/*     */       
/* 588 */       player.getCommunicator().sendNormalServerMessage("You don't have this item equipped.", (byte)3);
/* 589 */       return false;
/*     */     } 
/* 591 */     Item parent = item.getParentOrNull();
/* 592 */     if (parent == null) {
/*     */       
/* 594 */       player.getCommunicator().sendNormalServerMessage("No parent for item, not equipped?", (byte)3);
/* 595 */       return false;
/*     */     } 
/*     */     
/* 598 */     if (parent.getOwnerId() != player.getWurmId()) {
/*     */       
/* 600 */       player.getCommunicator().sendNormalServerMessage("You don't have this item equipped.", (byte)3);
/* 601 */       return false;
/*     */     } 
/*     */     
/* 604 */     boolean canInsert = player.getInventory().testInsertItem(item);
/* 605 */     boolean mayInsert = player.getInventory().mayCreatureInsertItem();
/* 606 */     if (canInsert && mayInsert) {
/*     */       
/*     */       try {
/*     */         
/* 610 */         parent.dropItem(item.getWurmId(), player.getInventory().getWurmId(), false);
/* 611 */         if (!player.getInventory().insertItem(item)) {
/*     */           
/* 613 */           player.getCommunicator().sendNormalServerMessage("Failed to insert item in inventory.", (byte)3);
/*     */           
/* 615 */           parent.insertItem(item);
/* 616 */           return false;
/*     */         } 
/* 618 */         return true;
/*     */       }
/* 620 */       catch (NoSuchItemException nsi) {
/*     */ 
/*     */         
/* 623 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 629 */     if (!canInsert) {
/* 630 */       player.getCommunicator().sendNormalServerMessage("Unable to add the item to the inventory.", (byte)3);
/*     */     }
/* 632 */     if (!mayInsert) {
/* 633 */       player.getCommunicator().sendNormalServerMessage("The inventory contains too many items.", (byte)3);
/*     */     }
/* 635 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean isSlotEmpty(Item part) {
/* 641 */     for (Item item : part.getItems()) {
/* 642 */       if (!item.isBodyPart())
/* 643 */         return false; 
/* 644 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean containsLeftOrRightSlots(byte[] slots, Item item) {
/* 649 */     if (item.isShield())
/* 650 */       return false; 
/* 651 */     for (int i = 0; i < slots.length; i++) {
/*     */       
/* 653 */       switch (slots[i]) {
/*     */         
/*     */         case 3:
/*     */         case 4:
/*     */         case 13:
/*     */         case 14:
/*     */         case 15:
/*     */         case 16:
/*     */         case 37:
/*     */         case 38:
/*     */         case 39:
/*     */         case 40:
/*     */         case 46:
/*     */         case 47:
/* 667 */           return true;
/*     */       } 
/*     */     
/*     */     } 
/* 671 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final byte getRightSlot(byte[] slots) {
/* 676 */     for (int i = 0; i < slots.length; i++) {
/*     */       
/* 678 */       switch (slots[i]) {
/*     */         
/*     */         case 4:
/*     */         case 14:
/*     */         case 16:
/*     */         case 38:
/*     */         case 40:
/*     */         case 47:
/* 686 */           return slots[i];
/*     */       } 
/*     */     
/*     */     } 
/* 690 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final byte getLeftSlot(byte[] slots) {
/* 695 */     for (int i = 0; i < slots.length; i++) {
/*     */       
/* 697 */       switch (slots[i]) {
/*     */         
/*     */         case 3:
/*     */         case 13:
/*     */         case 15:
/*     */         case 37:
/*     */         case 39:
/*     */         case 46:
/* 705 */           return slots[i];
/*     */       } 
/*     */     
/*     */     } 
/* 709 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean isMultiSlot(byte[] slots, Item item) {
/* 714 */     return (!containsLeftOrRightSlots(slots, item) && slots.length > 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean tryEquipLeft(Item item, Creature player, Action act) {
/* 719 */     byte[] slots = getValidEquipmentSlots(item);
/* 720 */     if (item.isWeapon() || item.isWeaponBow()) {
/*     */       
/* 722 */       byte slot = getLeftSlot(slots);
/* 723 */       return autoEquipWeapon(item, player, slot, true);
/*     */     } 
/* 725 */     if (containsLeftOrRightSlots(slots, item)) {
/*     */       
/* 727 */       byte slot = getLeftSlot(slots);
/* 728 */       return tryEquipInSlot(slot, item, player);
/*     */     } 
/* 730 */     if (isMultiSlot(slots, item)) {
/*     */       
/* 732 */       byte slot = slots[1];
/* 733 */       return tryEquipInSlot(slot, item, player);
/*     */     } 
/*     */     
/* 736 */     return autoEquip(item, player, (short)582, act, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean tryEquipRight(Item item, Creature player, Action act) {
/* 741 */     byte[] slots = getValidEquipmentSlots(item);
/* 742 */     if (item.isWeapon() || item.isWeaponBow()) {
/*     */       
/* 744 */       byte slot = getRightSlot(slots);
/* 745 */       return autoEquipWeapon(item, player, slot, false);
/*     */     } 
/* 747 */     if (containsLeftOrRightSlots(slots, item)) {
/*     */       
/* 749 */       byte slot = getRightSlot(slots);
/* 750 */       return tryEquipInSlot(slot, item, player);
/*     */     } 
/* 752 */     if (isMultiSlot(slots, item)) {
/*     */       
/* 754 */       byte slot = slots[0];
/* 755 */       return tryEquipInSlot(slot, item, player);
/*     */     } 
/*     */     
/* 758 */     return autoEquip(item, player, (short)582, act, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean tryEquipInSlot(byte slot, Item item, Creature player) {
/* 763 */     Item part = getBodySlot(slot, player);
/* 764 */     if (part == null) {
/*     */       
/* 766 */       logger.log(Level.WARNING, "Player: " + player.getName() + " Unable to find body slot for slot id: " + slot);
/* 767 */       return false;
/*     */     } 
/* 769 */     Item oldItem = getCurrentlyEquippedItem(part);
/*     */     
/* 771 */     boolean failedDrop = false;
/* 772 */     Item oldParent = null;
/* 773 */     if (oldItem != null) {
/*     */       
/* 775 */       oldParent = oldItem.getParentOrNull();
/* 776 */       failedDrop = !dropToInventory(oldItem, player);
/*     */     } 
/* 778 */     if (failedDrop)
/* 779 */       return false; 
/* 780 */     boolean canCarry = canCarry(item, player);
/* 781 */     if (part.testInsertItem(item) && canCarry) {
/*     */       
/* 783 */       Item parent = item.getParentOrNull();
/* 784 */       if (parent != null && 
/* 785 */         !dropFromParent(item, parent)) {
/*     */         
/* 787 */         if (oldParent != null && !failedDrop)
/* 788 */           oldParent.insertItem(oldItem); 
/* 789 */         return false;
/*     */       } 
/* 791 */       if (part.insertItem(item)) {
/*     */         
/* 793 */         if (item.isBelt()) {
/*     */           
/* 795 */           player.setBestToolbelt(null);
/* 796 */           if (item.getTemplateId() == 516)
/* 797 */             player.setBestToolbelt(item); 
/* 798 */           player.pollToolbelt();
/*     */         } 
/* 800 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 804 */       if (parent != null)
/* 805 */         parent.insertItem(item); 
/* 806 */       if (oldParent != null && !failedDrop)
/* 807 */         oldParent.insertItem(oldItem); 
/* 808 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 813 */     if (oldItem != null && oldParent != null) {
/*     */       
/* 815 */       dropFromParent(oldItem, oldParent);
/* 816 */       oldParent.insertItem(oldItem);
/*     */     } 
/* 818 */     if (!canCarry) {
/* 819 */       player.getCommunicator().sendNormalServerMessage("You are carrying too much.", (byte)3);
/*     */     }
/* 821 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\AutoEquipMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */