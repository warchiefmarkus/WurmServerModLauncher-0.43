/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.highways.Routes;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Permissions;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.server.spells.SpellEffect;
/*     */ import com.wurmonline.server.spells.Spells;
/*     */ import java.util.Properties;
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
/*     */ public class ItemRestrictionManagement
/*     */   extends Question
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(ItemRestrictionManagement.class.getName());
/*     */   
/*     */   private Permissions.IAllow obj;
/*     */   
/*     */   public ItemRestrictionManagement(Creature aResponder, Permissions.IAllow aObj, long aTargetId) {
/*  49 */     super(aResponder, "Manage Item Restrictions", makeQuestion(aResponder, aObj, aTargetId), 122, aTargetId);
/*     */     
/*  51 */     this.obj = aObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String makeQuestion(Creature aResponder, Permissions.IAllow aObj, long aTargetId) {
/*  56 */     String named = "";
/*  57 */     switch (WurmId.getType(aTargetId))
/*     */     
/*     */     { case 2:
/*  60 */         named = "Item: " + aObj.getName().replace("\"", "'");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  77 */         return "Manage " + named + " Restrictions (id= " + aTargetId + ")";case 28: named = "Bridge Part: " + aObj.getName().replace("\"", "'"); return "Manage " + named + " Restrictions (id= " + aTargetId + ")";case 7: named = "Fence"; return "Manage " + named + " Restrictions (id= " + aTargetId + ")";case 23: named = "Floor"; return "Manage " + named + " Restrictions (id= " + aTargetId + ")";case 5: named = "Wall"; return "Manage " + named + " Restrictions (id= " + aTargetId + ")"; }  named = "Unknown"; return "Manage " + named + " Restrictions (id= " + aTargetId + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  88 */     setAnswer(aAnswer);
/*  89 */     if (this.type == 0) {
/*     */       
/*  91 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  94 */     if (this.type == 122)
/*     */     {
/*  96 */       if (getResponder().getPower() >= 2) {
/*     */         
/*  98 */         String itemName = "The " + this.obj.getName() + " ";
/*  99 */         boolean change = false;
/* 100 */         boolean spellremoved = false;
/* 101 */         boolean spelladded = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 108 */         if (this.obj.canHaveCourier()) {
/*     */           
/* 110 */           boolean courier = Boolean.parseBoolean(aAnswer.getProperty("courier"));
/* 111 */           boolean darkmessenger = Boolean.parseBoolean(aAnswer.getProperty("darkmessenger"));
/* 112 */           String spow = aAnswer.getProperty("pow");
/* 113 */           if (courier && darkmessenger) {
/*     */             
/* 115 */             getResponder().getCommunicator().sendNormalServerMessage(itemName + "cannot support both courier and darkmessenger at the same time. So they have been ignored.");
/*     */ 
/*     */           
/*     */           }
/* 119 */           else if ((courier || darkmessenger) && (spow == null || spow.length() == 0)) {
/*     */             
/* 121 */             getResponder().getCommunicator().sendNormalServerMessage("Need a power for the courier / darkmessenger. So as not given they have been ignored.");
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 127 */             int pow = Integer.parseInt(spow);
/* 128 */             if (pow < 0) {
/*     */               
/* 130 */               getResponder().getCommunicator().sendNormalServerMessage("Need a power for the courier / darkmessenger. Power must be positive, defaulting it to 50.");
/*     */ 
/*     */               
/* 133 */               pow = 50;
/*     */             } 
/* 135 */             Spell spell = null;
/* 136 */             if (this.obj.hasCourier()) {
/* 137 */               spell = Spells.getSpell(338);
/* 138 */             } else if (this.obj.hasDarkMessenger()) {
/* 139 */               spell = Spells.getSpell(339);
/*     */             } 
/* 141 */             if (courier != this.obj.hasCourier()) {
/*     */               
/* 143 */               this.obj.setHasCourier(courier);
/* 144 */               change = true;
/* 145 */               if (courier) {
/* 146 */                 getResponder().getCommunicator().sendSafeServerMessage(itemName + "now has Courier.");
/*     */               } else {
/* 148 */                 getResponder().getCommunicator().sendSafeServerMessage(itemName + "no longer has Courier.");
/*     */               } 
/* 150 */             }  if (darkmessenger != this.obj.hasDarkMessenger()) {
/*     */               
/* 152 */               this.obj.setHasDarkMessenger(darkmessenger);
/* 153 */               change = true;
/* 154 */               if (darkmessenger) {
/* 155 */                 getResponder().getCommunicator().sendSafeServerMessage(itemName + "now has Dark Messenger.");
/*     */               } else {
/* 157 */                 getResponder().getCommunicator().sendSafeServerMessage(itemName + "no longer has Dark Messenger.");
/*     */               } 
/*     */             } 
/* 160 */             Item item = (Item)this.obj;
/*     */             
/* 162 */             if (spell != null) {
/*     */               
/* 164 */               byte ench = spell.getEnchantment();
/* 165 */               SpellEffect eff = item.getSpellEffect(ench);
/* 166 */               if (eff != null) {
/*     */                 
/* 168 */                 item.getSpellEffects().removeSpellEffect(eff.type);
/* 169 */                 spellremoved = true;
/*     */               } 
/*     */             } 
/*     */             
/* 173 */             spell = null;
/* 174 */             if (courier) {
/* 175 */               spell = Spells.getSpell(338);
/* 176 */             } else if (darkmessenger) {
/* 177 */               spell = Spells.getSpell(339);
/* 178 */             }  if (spell != null) {
/*     */               
/* 180 */               spell.castSpell(pow, getResponder(), item);
/* 181 */               spelladded = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 193 */         if (this.obj.canBeSealedByPlayer() || this.obj.canBePeggedByPlayer() || canGMSeal()) {
/*     */           
/* 195 */           boolean sealed = Boolean.parseBoolean(aAnswer.getProperty("sealed"));
/* 196 */           if (sealed != this.obj.isSealedByPlayer()) {
/*     */             
/* 198 */             this.obj.setIsSealedByPlayer(sealed);
/* 199 */             change = true;
/* 200 */             if (sealed) {
/* 201 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "is now sealed.");
/*     */             } else {
/* 203 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "is no longer sealed.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 212 */         if (this.obj.canBeAutoFilled()) {
/*     */           
/* 214 */           boolean autoFill = Boolean.parseBoolean(aAnswer.getProperty("autofill"));
/* 215 */           if (autoFill != this.obj.isAutoFilled()) {
/*     */             
/* 217 */             this.obj.setIsAutoFilled(autoFill);
/* 218 */             change = true;
/* 219 */             if (autoFill) {
/* 220 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will Auto-Fill.");
/*     */             } else {
/* 222 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will no longer Auto-Fill.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 231 */         if (this.obj.canBeAutoLit()) {
/*     */           
/* 233 */           boolean autoLight = Boolean.parseBoolean(aAnswer.getProperty("autolight"));
/* 234 */           if (autoLight != this.obj.isAutoLit()) {
/*     */             
/* 236 */             this.obj.setIsAutoLit(autoLight);
/* 237 */             change = true;
/* 238 */             if (autoLight) {
/* 239 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will Auto-Light.");
/*     */             } else {
/* 241 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will no longer Auto-Light.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 250 */         if (this.obj.canBeAlwaysLit()) {
/*     */           
/* 252 */           boolean alwaysLit = Boolean.parseBoolean(aAnswer.getProperty("alwaysLit"));
/* 253 */           if (alwaysLit != this.obj.isAlwaysLit()) {
/*     */             
/* 255 */             this.obj.setIsAlwaysLit(alwaysLit);
/* 256 */             change = true;
/* 257 */             if (alwaysLit) {
/* 258 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will be Always Lit.");
/*     */             } else {
/* 260 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will no longer be Always Lit.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 269 */         if (this.obj.canDisableDecay()) {
/*     */           
/* 271 */           boolean noDecay = Boolean.parseBoolean(aAnswer.getProperty("noDecay"));
/* 272 */           if (noDecay != this.obj.hasNoDecay()) {
/*     */             
/* 274 */             this.obj.setHasNoDecay(noDecay);
/* 275 */             change = true;
/* 276 */             if (noDecay) {
/* 277 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will not decay further.");
/*     */             } else {
/* 279 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "will decay at default rate.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 288 */         if (this.obj.canDisableEatAndDrink()) {
/*     */           
/* 290 */           boolean noEatOrDrink = Boolean.parseBoolean(aAnswer.getProperty("noEatOrDrink"));
/* 291 */           if (noEatOrDrink != this.obj.isNoEatOrDrink()) {
/*     */             
/* 293 */             this.obj.setIsNoEatOrDrink(noEatOrDrink);
/* 294 */             change = true;
/* 295 */             if (noEatOrDrink) {
/* 296 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be eaten or drunk.");
/*     */             } else {
/* 298 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can be eaten or drunk.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 307 */         if (this.obj.canDisableTake()) {
/*     */           
/* 309 */           boolean noTake = Boolean.parseBoolean(aAnswer.getProperty("noTake"));
/* 310 */           if (noTake != this.obj.isNoTake()) {
/*     */             
/* 312 */             this.obj.setIsNoTake(noTake);
/* 313 */             change = true;
/* 314 */             if (noTake) {
/* 315 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be taken.");
/*     */             } else {
/* 317 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can be taken.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 326 */         if (this.obj.canDisableSpellTarget()) {
/*     */           
/* 328 */           boolean noSpells = Boolean.parseBoolean(aAnswer.getProperty("noSpells"));
/* 329 */           if (noSpells != this.obj.isNotSpellTarget()) {
/*     */             
/* 331 */             this.obj.setIsNotSpellTarget(noSpells);
/* 332 */             change = true;
/* 333 */             if (noSpells) {
/* 334 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be target for spells.");
/*     */             } else {
/* 336 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can be target for spells.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 345 */         if (this.obj.canDisableDestroy()) {
/*     */           
/* 347 */           boolean noDestroy = Boolean.parseBoolean(aAnswer.getProperty("noDestroy"));
/* 348 */           if (noDestroy != this.obj.isIndestructible()) {
/*     */             
/* 350 */             this.obj.setIsIndestructible(noDestroy);
/* 351 */             change = true;
/* 352 */             if (noDestroy) {
/* 353 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "is now indestructible.");
/*     */             } else {
/* 355 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be bashed/destroyed.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 364 */         if (this.obj.canDisableLocking()) {
/*     */           
/* 366 */           boolean noLock = Boolean.parseBoolean(aAnswer.getProperty("noLock"));
/* 367 */           if (noLock != this.obj.isNotLockable()) {
/*     */             
/* 369 */             this.obj.setIsNotLockable(noLock);
/* 370 */             change = true;
/* 371 */             if (noLock) {
/* 372 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "is now not lockable.");
/*     */             } else {
/* 374 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "is now lockable.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 383 */         if (this.obj.canDisableLockpicking()) {
/*     */           
/* 385 */           boolean noLockpick = Boolean.parseBoolean(aAnswer.getProperty("noLockpick"));
/* 386 */           if (noLockpick != this.obj.isNotLockpickable()) {
/*     */             
/* 388 */             this.obj.setIsNotLockpickable(noLockpick);
/* 389 */             change = true;
/* 390 */             if (noLockpick) {
/* 391 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be lockpicked.");
/*     */             } else {
/* 393 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be lockpicked.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 402 */         if (this.obj.canDisableMoveable()) {
/*     */           
/* 404 */           boolean noMove = Boolean.parseBoolean(aAnswer.getProperty("noMove"));
/* 405 */           if (noMove != this.obj.isNoMove()) {
/*     */             
/* 407 */             this.obj.setIsNoMove(noMove);
/* 408 */             change = true;
/* 409 */             if (noMove) {
/* 410 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be moved.");
/*     */             } else {
/* 412 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be moved.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 421 */         if (this.obj.canDisableTurning()) {
/*     */           
/* 423 */           boolean noTurn = Boolean.parseBoolean(aAnswer.getProperty("noTurn"));
/* 424 */           if (noTurn != this.obj.isNotTurnable()) {
/*     */             
/* 426 */             this.obj.setIsNotTurnable(noTurn);
/* 427 */             change = true;
/* 428 */             if (noTurn) {
/* 429 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be turned.");
/*     */             } else {
/* 431 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be turned.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 440 */         if (this.obj.canDisableOwnerMoveing()) {
/*     */           
/* 442 */           boolean ownerMove = Boolean.parseBoolean(aAnswer.getProperty("ownerMove"));
/* 443 */           if (ownerMove != this.obj.isOwnerMoveable()) {
/*     */             
/* 445 */             this.obj.setIsOwnerMoveable(ownerMove);
/* 446 */             change = true;
/* 447 */             if (ownerMove) {
/* 448 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be moved by owner.");
/*     */             } else {
/* 450 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be moved by owner.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 459 */         if (this.obj.canDisableOwnerTurning()) {
/*     */           
/* 461 */           boolean ownerTurn = Boolean.parseBoolean(aAnswer.getProperty("ownerTurn"));
/* 462 */           if (ownerTurn != this.obj.isOwnerTurnable()) {
/*     */             
/* 464 */             this.obj.setIsOwnerTurnable(ownerTurn);
/* 465 */             change = true;
/* 466 */             if (ownerTurn) {
/* 467 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be turned by owner.");
/*     */             } else {
/* 469 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be turned by owner.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 478 */         if (this.obj.canDisablePainting()) {
/*     */           
/* 480 */           boolean noPaint = Boolean.parseBoolean(aAnswer.getProperty("noPaint"));
/* 481 */           if (noPaint != this.obj.isNotPaintable()) {
/*     */             
/* 483 */             this.obj.setIsNotPaintable(noPaint);
/* 484 */             change = true;
/* 485 */             if (noPaint) {
/* 486 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be painted.");
/*     */             } else {
/* 488 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be painted.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 497 */         if (this.obj.canDisableRuneing()) {
/*     */           
/* 499 */           boolean noRunes = Boolean.parseBoolean(aAnswer.getProperty("noRunes"));
/* 500 */           if (noRunes != this.obj.isNotRuneable()) {
/*     */             
/* 502 */             this.obj.setIsNotRuneable(noRunes);
/* 503 */             change = true;
/* 504 */             if (noRunes) {
/* 505 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be runed.");
/*     */             } else {
/* 507 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be runed.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 514 */         if (this.obj.canDisableImprove()) {
/*     */           
/* 516 */           boolean noImprove = Boolean.parseBoolean(aAnswer.getProperty("noImprove"));
/* 517 */           if (noImprove != this.obj.isNoImprove()) {
/*     */             
/* 519 */             this.obj.setIsNoImprove(noImprove);
/* 520 */             change = true;
/* 521 */             if (noImprove) {
/* 522 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be improved.");
/*     */             } else {
/* 524 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be improved.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 533 */         if (this.obj.canDisableRepair()) {
/*     */           
/* 535 */           boolean noRepair = Boolean.parseBoolean(aAnswer.getProperty("noRepair"));
/* 536 */           if (noRepair != this.obj.isNoRepair()) {
/*     */             
/* 538 */             this.obj.setIsNoRepair(noRepair);
/* 539 */             change = true;
/* 540 */             if (noRepair) {
/* 541 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be repaired.");
/*     */             } else {
/* 543 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be repaired.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 552 */         if (this.obj.canDisableDrag()) {
/*     */           
/* 554 */           boolean noDrag = Boolean.parseBoolean(aAnswer.getProperty("noDrag"));
/* 555 */           if (noDrag != this.obj.isNoDrag()) {
/*     */             
/* 557 */             this.obj.setIsNoDrag(noDrag);
/* 558 */             change = true;
/* 559 */             if (noDrag) {
/* 560 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be dragged.");
/*     */             } else {
/* 562 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be dragged.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 571 */         if (this.obj.canDisableDrop()) {
/*     */           
/* 573 */           boolean noDrop = Boolean.parseBoolean(aAnswer.getProperty("noDrop"));
/* 574 */           if (noDrop != this.obj.isNoDrop()) {
/*     */             
/* 576 */             this.obj.setIsNoDrop(noDrop);
/* 577 */             change = true;
/* 578 */             if (noDrop) {
/* 579 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot be dropped.");
/*     */             } else {
/* 581 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now be dropped.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 590 */         if (this.obj.canDisableRepair()) {
/*     */           
/* 592 */           boolean noPut = Boolean.parseBoolean(aAnswer.getProperty("noPut"));
/* 593 */           if (noPut != this.obj.isNoPut()) {
/*     */             
/* 595 */             this.obj.setIsNoPut(noPut);
/* 596 */             change = true;
/* 597 */             if (noPut) {
/* 598 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "cannot have items put in it.");
/*     */             } else {
/* 600 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "can now have items put in it.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 609 */         if (this.obj.canBePlanted()) {
/*     */           
/* 611 */           boolean plant = Boolean.parseBoolean(aAnswer.getProperty("planted"));
/* 612 */           if (plant != this.obj.isPlanted()) {
/*     */             
/* 614 */             if (this.obj instanceof Item && !plant && ((Item)this.obj).isRoadMarker()) {
/*     */               
/* 616 */               Item theTarget = (Item)this.obj;
/*     */ 
/*     */ 
/*     */               
/* 620 */               if (Routes.isMarkerUsed(theTarget))
/* 621 */                 theTarget.setWhatHappened("unplanted by " + getResponder().getName()); 
/*     */             } 
/* 623 */             this.obj.setIsPlanted(plant);
/* 624 */             change = true;
/* 625 */             if (plant) {
/* 626 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "is now planted.");
/*     */             } else {
/* 628 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "is no longer planted.");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 637 */         if (change) {
/* 638 */           this.obj.savePermissions();
/*     */         }
/* 640 */         String val = aAnswer.getProperty("ql");
/* 641 */         if (val != null && val.length() > 0) {
/*     */           
/*     */           try {
/*     */             
/* 645 */             double newQL = Double.parseDouble(val);
/* 646 */             if (newQL < 1.0D || newQL > 100.0D)
/*     */             {
/* 648 */               getResponder().getCommunicator().sendNormalServerMessage("QL " + newQL + " Out of range (1-100).");
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/*     */ 
/*     */               
/* 659 */               this.obj.setQualityLevel((float)newQL);
/* 660 */               this.obj.setOriginalQualityLevel((float)newQL);
/* 661 */               change = true;
/* 662 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "now has QL of " + newQL + ".");
/*     */             }
/*     */           
/* 665 */           } catch (NumberFormatException nfe) {
/*     */             
/* 667 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to interpret " + val + " as a number for QL.");
/*     */           } 
/*     */         }
/*     */         
/* 671 */         val = aAnswer.getProperty("dmg");
/* 672 */         if (val != null && val.length() > 0) {
/*     */           
/*     */           try {
/*     */             
/* 676 */             double newDMG = Double.parseDouble(val);
/* 677 */             if (newDMG < 0.0D || newDMG >= 100.0D)
/*     */             {
/* 679 */               getResponder().getCommunicator().sendNormalServerMessage("DMG " + newDMG + " Out of range (0 upto 100).");
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/*     */ 
/*     */               
/* 690 */               this.obj.setDamage((float)newDMG);
/* 691 */               change = true;
/* 692 */               getResponder().getCommunicator().sendSafeServerMessage(itemName + "now has Damage of " + newDMG + ".");
/*     */             }
/*     */           
/* 695 */           } catch (NumberFormatException nfe) {
/*     */             
/* 697 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to interpret " + val + " as a number for damage.");
/*     */           } 
/*     */         }
/*     */         
/* 701 */         if (this.obj.canChangeCreator()) {
/*     */           
/* 703 */           val = aAnswer.getProperty("creator");
/* 704 */           if (val != null && val.length() > 0)
/*     */           {
/*     */             
/* 707 */             if (LoginHandler.containsIllegalCharacters(val)) {
/*     */               
/* 709 */               getResponder().getCommunicator().sendNormalServerMessage("New creator (" + val + ") contains illegial characters.");
/*     */             
/*     */             }
/*     */             else {
/*     */               
/* 714 */               val = LoginHandler.raiseFirstLetter(val);
/* 715 */               if (val.length() < 3) {
/*     */                 
/* 717 */                 getResponder().getCommunicator().sendNormalServerMessage("New creator (" + val + ") is too short (min 3 chars).");
/*     */               
/*     */               }
/* 720 */               else if (!val.equalsIgnoreCase(this.obj.getCreatorName())) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 732 */                 this.obj.setCreator(val);
/* 733 */                 change = true;
/* 734 */                 getResponder().getCommunicator().sendSafeServerMessage(itemName + "now has creator of " + val + ".");
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/* 739 */         if (!change)
/*     */         {
/* 741 */           if (spellremoved && !spelladded) {
/* 742 */             getResponder().getCommunicator().sendNormalServerMessage(itemName + "has been disenchanted");
/* 743 */           } else if (!spellremoved) {
/* 744 */             getResponder().getCommunicator().sendNormalServerMessage("nothing changed");
/*     */           } 
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 765 */     StringBuilder buf = new StringBuilder();
/* 766 */     buf.append(getBmlHeader());
/*     */     
/* 768 */     int pow = 50;
/* 769 */     Spell spell = null;
/* 770 */     if (this.obj.canHaveCourier()) {
/*     */       
/* 772 */       if (this.obj.hasCourier()) {
/* 773 */         spell = Spells.getSpell(338);
/* 774 */       } else if (this.obj.hasDarkMessenger()) {
/* 775 */         spell = Spells.getSpell(339);
/* 776 */       }  if (spell != null) {
/*     */         
/* 778 */         byte ench = spell.getEnchantment();
/* 779 */         SpellEffect eff = ((Item)this.obj).getSpellEffect(ench);
/* 780 */         if (eff != null) {
/* 781 */           pow = (int)eff.getPower();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 786 */     String blank = "image{src=\"img.gui.bridge.blank\";size=\"160,2\";text=\"\"}";
/*     */     
/* 788 */     buf.append("text{text=\"\"}");
/* 789 */     buf.append("label{type=\"bold\";text=\"Attributes / Restrictions\"}");
/* 790 */     buf.append("table{rows=\"5\";cols=\"4\";");
/* 791 */     buf.append(permission("alwaysLit", this.obj.isAlwaysLit(), "Always Lit", this.obj.canBeAlwaysLit(), "Ticked if item will always be lit.", "Item is always lit.", "Item cannot be always lit."));
/*     */ 
/*     */     
/* 794 */     buf.append(permission("autofill", this.obj.isAutoFilled(), "Auto-Fill", this.obj.canBeAutoFilled(), "Ticked if item will auto-fill. Wells or Fountains (only).", "Item is always Auto-Filled.", "Item cannot be Auto-Filled."));
/*     */ 
/*     */     
/* 797 */     buf.append(permission("autolight", this.obj.isAutoLit(), "Auto-Lights", this.obj.canBeAutoLit(), "Ticked if item will auto-light.", "Item is always Auto-Lit.", "Item cannot be Auto-Lit."));
/*     */ 
/*     */     
/* 800 */     buf.append(permission("noDecay", this.obj.hasNoDecay(), "Decay Disabled", this.obj.canDisableDecay(), "Tick to disable decay on this item.", "Item always has decay disabled.", "Item cannot have decay disabled."));
/*     */ 
/*     */ 
/*     */     
/* 804 */     buf.append(permission("noDestroy", this.obj.isIndestructible(), "No Destroy / Bash", this.obj.canDisableDestroy(), "Tick to disallow bashing/destroying of this item.", "Item is always Indestructable.", "Item cannot be made Industrictable."));
/*     */ 
/*     */     
/* 807 */     buf.append(permission("noDrag", this.obj.isNoDrag(), "No Drag", this.obj.canDisableDrag(), "Tick to disallow dragging of this item.", "Item is always not draggable.", "Item cannot be made not draggable."));
/*     */ 
/*     */     
/* 810 */     buf.append(permission("noDrop", this.obj.isNoDrop(), "No Drop", this.obj.canDisableDrop(), "Tick to disallow dropping of this item.", "Item is always not droppable.", "Item cannot be made not droppable."));
/*     */ 
/*     */     
/* 813 */     buf.append(permission("noEatOrDrink", this.obj.isNoEatOrDrink(), "No Eat Or Drink", this.obj.canDisableEatAndDrink(), "Tick to disallow eating and drinking of this item.", "Item is always not eatable or drinkable.", "Item cannot be made not eatable or drinkable."));
/*     */ 
/*     */ 
/*     */     
/* 817 */     buf.append(permission("noPut", this.obj.isNoPut(), "No Put", this.obj.canDisablePut(), "Tick to disallow putting items in this item.", "Item is always noPut.", "Item cannot be made noPut."));
/*     */ 
/*     */     
/* 820 */     buf.append(permission("noSpells", this.obj.isNotSpellTarget(), "No Spells", this.obj.canDisableSpellTarget(), "Tick to disallow spells to be cast on this item.", "Item cannot accept spells anyway.", "Item cannot be made to not be a spell target."));
/*     */ 
/*     */     
/* 823 */     buf.append(permission("noTake", this.obj.isNoTake(), "No Take", this.obj.canDisableTake(), "Tick to override the No Take.", "Item is always No Take.", "Item cannot be made No Take."));
/*     */ 
/*     */     
/* 826 */     buf.append(permission("noImprove", this.obj.isNoImprove(), "Not Improvable", this.obj.canDisableImprove(), "Tick to disallow improving of this item.", "Item is always not improvable.", "Item cannot be made not improvable."));
/*     */ 
/*     */ 
/*     */     
/* 830 */     buf.append(permission("noLock", this.obj.isNotLockable(), "Not Lockable", this.obj.canDisableLocking(), "Tick to disallow locking of this item.", "Item is always not lockable.", "Item cannot be made not lockable."));
/*     */ 
/*     */     
/* 833 */     buf.append(permission("noLockpick", this.obj.isNotLockpickable(), "Not Lockpickable", this.obj.canDisableLockpicking(), "Tick to disallow lockpicking of this item.", "Item is always not lockpickable.", "Item cannot be made not-lockpickable (cannot have a lock?)"));
/*     */ 
/*     */     
/* 836 */     buf.append(permission("noMove", this.obj.isNoMove(), "Not Moveable", this.obj.canDisableMoveable(), "Tick to disallow movement of this item.", "Item is always not moveable.", "Item cannot be made not moveable."));
/*     */ 
/*     */     
/* 839 */     buf.append(permission("noPaint", this.obj.isNotPaintable(), "Not Paintable", this.obj.canDisablePainting(), "Tick to disallow painting of this item.", "Item is always not paintable.", "Item cannot be made not paintable."));
/*     */ 
/*     */ 
/*     */     
/* 843 */     buf.append(permission("noRepair", this.obj.isNoRepair(), "Not Repairable", this.obj.canDisableRepair(), "Tick to disallow repairing of this item.", "Item is always not repairable.", "Item cannot be made not repairable."));
/*     */ 
/*     */     
/* 846 */     buf.append(permission("noRunes", this.obj.isNotRuneable(), "Not Runeable", this.obj.canDisableRuneing(), "Tick to disallow runeing of this item.", "Item is always not runeable.", "Item cannot be made not runeable."));
/*     */ 
/*     */     
/* 849 */     buf.append(permission("noTurn", this.obj.isNotTurnable(), "Not Turnable", this.obj.canDisableTurning(), "Tick to disallow turning of this item.", "Item is always not turnable.", "Item cannot be made not turnable."));
/*     */ 
/*     */     
/* 852 */     buf.append(permission("ownerTurn", this.obj.isOwnerTurnable(), "Owner Turnable", this.obj.canDisableOwnerTurning(), "Tick to disallow owner turning of this item.", "Item is always not turnable by owner.", "Item cannot be made not turnable by owner."));
/*     */ 
/*     */ 
/*     */     
/* 856 */     buf.append(permission("ownerMove", this.obj.isOwnerMoveable(), "Owner Moveable", this.obj.canDisableOwnerMoveing(), "Tick to disallow owner moveing of this item.", "Item is always not moveable by owner.", "Item cannot be made not moveable by owner."));
/*     */ 
/*     */     
/* 859 */     buf.append(permission("planted", this.obj.isPlanted(), "Planted", this.obj.canBePlanted(), "Ticked if item is planted.", "Item is always planted.", "Item cannot be planted."));
/*     */ 
/*     */     
/* 862 */     buf.append(permission("sealed", this.obj.isSealedByPlayer(), "Sealed", (this.obj.canBeSealedByPlayer() || this.obj.canBePeggedByPlayer() || canGMSeal()), "Ticked if item is sealed by player.", "Item is always sealed.", "Item cannot be sealed by player."));
/*     */ 
/*     */     
/* 865 */     buf.append("label{text=\"\"}");
/*     */     
/* 867 */     buf.append(permission("courier", this.obj.hasCourier(), "Has Courier", this.obj.canHaveCourier(), "Ticked it Courier has been cast on it.", "Item always has courier.", "Item cannot have courier."));
/*     */ 
/*     */     
/* 870 */     buf.append(permission("darkmessenger", this.obj.hasDarkMessenger(), "Has Dark Messenger", this.obj.canHaveDakrMessenger(), "Ticked if Dark Messenger has been cast on it.", "Item always has dark messeneger.", "Item cannot have dark messenger."));
/*     */ 
/*     */     
/* 873 */     buf.append("harray{label{text=\"Power=\"};");
/* 874 */     if (this.obj.canHaveCourier()) {
/* 875 */       buf.append("input{id=\"pow\";maxchars=\"3\";text=\"" + pow + "\"}");
/*     */     } else {
/* 877 */       buf.append("label{text=\"na\"}");
/* 878 */     }  buf.append("}");
/* 879 */     buf.append("label{text=\"\"}");
/* 880 */     buf.append("image{src=\"img.gui.bridge.blank\";size=\"160,2\";text=\"\"}image{src=\"img.gui.bridge.blank\";size=\"160,2\";text=\"\"}image{src=\"img.gui.bridge.blank\";size=\"160,2\";text=\"\"}image{src=\"img.gui.bridge.blank\";size=\"160,2\";text=\"\"}");
/* 881 */     buf.append("}");
/*     */     
/* 883 */     buf.append("label{text=\"Update QL (range 1-100) and/or damage (range 0-99).\"};");
/* 884 */     buf.append("label{type=\"talics\";text=\"leave blank for no change.\"};");
/* 885 */     buf.append("table{rows=\"2\";cols=\"4\";");
/* 886 */     buf.append("label{text=\"QL:\"};label{text=\"" + this.obj
/* 887 */         .getQualityLevel() + "\"};input{id=\"ql\";maxchars=\"10\"};label{text=\"(1-100)\"}");
/*     */ 
/*     */     
/* 890 */     buf.append("label{text=\"DMG:\"};label{text=\"" + this.obj
/* 891 */         .getDamage() + "\"};input{id=\"dmg\";maxchars=\"10\"};label{text=\"(0 upto 100)\"}");
/*     */ 
/*     */     
/* 894 */     if (this.obj.canChangeCreator()) {
/* 895 */       buf.append("label{text=\"Creator:\"};label{text=\"" + this.obj
/* 896 */           .getCreatorName() + "\"};input{id=\"creator\";maxchars=\"40\"};label{text=\"(3-40 chars)\"}");
/*     */     }
/*     */     
/* 899 */     buf.append("}");
/* 900 */     buf.append("label{text=\"\"};");
/*     */     
/* 902 */     buf.append("harray{button{text=\"Update Restrictions\";id=\"update\"}};");
/* 903 */     buf.append("label{text=\"\"};");
/*     */     
/* 905 */     buf.append("}};null;null;}");
/* 906 */     getResponder().getCommunicator().sendBml(500, 380, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String permission(String sid, boolean selected, String text, boolean enabled, String hover, String always, String never) {
/* 912 */     if (enabled) {
/* 913 */       return "checkbox{id=\"" + sid + "\";selected=\"" + selected + "\";text=\"" + text + "\";hover=\"" + hover + "\"};";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 918 */     return "harray{image{src=\"img.gui." + (selected ? "vsmall" : "xsmall") + "\";size=\"16,16\";text=\"" + hover + "\"}label{text=\"" + text + "\";hover=\"" + (selected ? always : never) + "\"};};";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canGMSeal() {
/* 927 */     if (WurmId.getType(this.target) == 2) {
/*     */       
/* 929 */       Item item = (Item)this.obj;
/* 930 */       if (item.getTemplateId() == 768) {
/*     */ 
/*     */         
/* 933 */         Item[] contains = item.getItemsAsArray();
/* 934 */         if (contains.length == 2)
/*     */         {
/* 936 */           return ((contains[0].isLiquid() && contains[0].isFermenting()) || (contains[1]
/* 937 */             .isLiquid() && contains[1].isFermenting()));
/*     */         }
/*     */       } 
/* 940 */       if (item.getTemplateId() == 1309)
/*     */       {
/* 942 */         return true;
/*     */       }
/*     */     } 
/* 945 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ItemRestrictionManagement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */