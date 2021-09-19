/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.economy.Shop;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.LinkedList;
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
/*     */ public abstract class GuardPlan
/*     */   implements CreatureTemplateIds, TimeConstants, MiscConstants, MonetaryConstants
/*     */ {
/*     */   public static final int GUARD_PLAN_NONE = 0;
/*     */   public static final int GUARD_PLAN_LIGHT = 1;
/*     */   public static final int GUARD_PLAN_MEDIUM = 2;
/*     */   public static final int GUARD_PLAN_HEAVY = 3;
/*  51 */   final LinkedList<Creature> freeGuards = new LinkedList<>();
/*     */   
/*  53 */   private static final Logger logger = Logger.getLogger(GuardPlan.class.getName());
/*     */ 
/*     */   
/*  56 */   public int type = 0;
/*     */   final int villageId;
/*     */   long lastChangedPlan;
/*     */   public long moneyLeft;
/*  60 */   private int siegeCount = 0;
/*  61 */   private int waveCounter = 0;
/*     */ 
/*     */   
/*  64 */   private long lastSentWarning = 0L;
/*     */   private static final long polltime = 500000L;
/*  66 */   long lastDrained = 0L;
/*  67 */   float drainModifier = 0.0F;
/*     */   private static final float maxDrainModifier = 5.0F;
/*     */   private static final float drainCumulateFigure = 0.5F;
/*  70 */   private int upkeepCounter = 0;
/*  71 */   int hiredGuardNumber = 0;
/*  72 */   private static final int maxGuards = Servers.localServer.isChallengeOrEpicServer() ? 20 : 50;
/*     */ 
/*     */   
/*     */   private static final long minMoneyDrained = 7500L;
/*     */ 
/*     */ 
/*     */   
/*     */   GuardPlan(int aType, int aVillageId) {
/*  80 */     this.type = aType;
/*  81 */     this.villageId = aVillageId;
/*     */     
/*  83 */     create();
/*     */   }
/*     */ 
/*     */   
/*     */   GuardPlan(int aVillageId) {
/*  88 */     this.villageId = aVillageId;
/*  89 */     load();
/*     */   }
/*     */ 
/*     */   
/*     */   final Village getVillage() throws NoSuchVillageException {
/*  94 */     return Villages.getVillage(this.villageId);
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
/*     */   public final String getName() {
/* 111 */     if (this.type == 3)
/* 112 */       return "Heavy"; 
/* 113 */     if (this.type == 1)
/* 114 */       return "Light"; 
/* 115 */     if (this.type == 2) {
/* 116 */       return "Medium";
/*     */     }
/* 118 */     return "None";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getTimeLeft() {
/*     */     try {
/* 125 */       if ((getVillage()).isPermanent || !Servers.localServer.isUpkeep()) {
/* 126 */         return 29030400000L;
/*     */       }
/* 128 */     } catch (NoSuchVillageException nsv) {
/*     */       
/* 130 */       logger.log(Level.WARNING, this.villageId + ", " + nsv.getMessage(), (Throwable)nsv);
/*     */     } 
/* 132 */     return (long)(this.moneyLeft / Math.max(1.0D, calculateUpkeep(false)) * 500000.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public double calculateUpkeep(boolean calculateFraction) {
/* 137 */     long monthlyCost = getMonthlyCost();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     double upkeep = monthlyCost * 2.0667989417989417E-4D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     return upkeep;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getMoneyLeft() {
/* 163 */     return this.moneyLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long getCostForGuards(int numGuards) {
/* 170 */     if (Servers.localServer.isChallengeOrEpicServer()) {
/* 171 */       return (numGuards * 10000 + (numGuards - 1) * numGuards / 2 * 100 * 50);
/*     */     }
/* 173 */     return numGuards * Villages.GUARD_UPKEEP;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getMonthlyCost() {
/* 178 */     if (!Servers.localServer.isUpkeep()) {
/* 179 */       return 0L;
/*     */     }
/*     */     try {
/* 182 */       Village vill = getVillage();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 195 */       long cost = vill.getNumTiles() * Villages.TILE_UPKEEP;
/* 196 */       cost += vill.getPerimeterNonFreeTiles() * Villages.PERIMETER_UPKEEP;
/* 197 */       cost += getCostForGuards(this.hiredGuardNumber);
/*     */ 
/*     */       
/* 200 */       if (vill.isCapital())
/* 201 */         cost = (long)((float)cost * 0.5F); 
/* 202 */       if (vill.hasToomanyCitizens())
/* 203 */         cost *= 2L; 
/* 204 */       return Math.max(Villages.MINIMUM_UPKEEP, cost);
/*     */     }
/* 206 */     catch (NoSuchVillageException sv) {
/*     */       
/* 208 */       logger.log(Level.WARNING, "Guardplan for village " + this.villageId + ": Village not found. Deleting.", (Throwable)sv);
/* 209 */       delete();
/*     */       
/* 211 */       return 10000L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public final boolean mayRaiseUpkeep() {
/* 216 */     return (System.currentTimeMillis() - this.lastChangedPlan > 604800000L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean mayLowerUpkeep() {
/* 221 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long calculateUpkeepTimeforType(int upkeeptype) {
/* 227 */     int origType = this.type;
/* 228 */     this.type = upkeeptype;
/* 229 */     long timeleft = getTimeLeft();
/* 230 */     this.type = origType;
/* 231 */     return timeleft;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long calculateMonthlyUpkeepTimeforType(int upkeeptype) {
/* 236 */     int origType = this.type;
/* 237 */     this.type = upkeeptype;
/* 238 */     long cost = getMonthlyCost();
/* 239 */     this.type = origType;
/* 240 */     return cost;
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getDisbandMoneyLeft() {
/* 245 */     return this.moneyLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pollGuards() {
/* 254 */     if (this.type != 0) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 260 */         Village village = getVillage();
/* 261 */         int _maxGuards = getConvertedGuardNumber(village);
/* 262 */         Guard[] guards = village.getGuards();
/*     */ 
/*     */         
/* 265 */         if (guards.length < _maxGuards) {
/*     */ 
/*     */           
/*     */           try {
/* 269 */             Item villToken = village.getToken();
/* 270 */             byte sex = 0;
/* 271 */             if (Server.rand.nextInt(2) == 0)
/* 272 */               sex = 1; 
/* 273 */             int templateId = 32;
/* 274 */             if (Kingdoms.getKingdomTemplateFor(village.kingdom) == 3) {
/* 275 */               templateId = 33;
/*     */             }
/*     */             
/* 278 */             for (int x = 0; x < Math.min(this.siegeCount + 1, _maxGuards - guards.length); x++) {
/*     */ 
/*     */               
/*     */               try {
/*     */                 
/* 283 */                 if (this.freeGuards.isEmpty()) {
/*     */                   
/* 285 */                   Creature newc = Creature.doNew(templateId, villToken.getPosX(), villToken.getPosY(), Server.rand
/* 286 */                       .nextInt(360), village.isOnSurface() ? 0 : -1, "", sex, village.kingdom);
/*     */                   
/* 288 */                   village.createGuard(newc, System.currentTimeMillis());
/*     */                 
/*     */                 }
/*     */                 else {
/*     */ 
/*     */                   
/* 294 */                   Creature toReturn = this.freeGuards.removeFirst();
/* 295 */                   if (toReturn.getTemplate().getTemplateId() != templateId)
/*     */                   {
/* 297 */                     removeReturnedGuard(toReturn.getWurmId());
/* 298 */                     toReturn.destroy();
/* 299 */                     Creature newc = Creature.doNew(templateId, villToken.getPosX(), villToken
/* 300 */                         .getPosY(), Server.rand.nextInt(360), village.isOnSurface() ? 0 : -1, "", sex, village.kingdom);
/*     */                     
/* 302 */                     village.createGuard(newc, System.currentTimeMillis());
/*     */                   }
/*     */                   else
/*     */                   {
/* 306 */                     village.createGuard(toReturn, System.currentTimeMillis());
/* 307 */                     removeReturnedGuard(toReturn.getWurmId());
/* 308 */                     putGuardInWorld(toReturn);
/*     */                   }
/*     */                 
/*     */                 } 
/* 312 */               } catch (Exception ex) {
/*     */                 
/* 314 */                 logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */               }
/*     */             
/*     */             } 
/* 318 */           } catch (NoSuchItemException nsi) {
/*     */             
/* 320 */             logger.log(Level.WARNING, "Village " + village.getName() + " has no token.");
/*     */           } 
/* 322 */           if (this.siegeCount > 0) {
/* 323 */             this.siegeCount += 3;
/*     */           }
/*     */         } 
/* 326 */         village.checkForEnemies();
/*     */       }
/* 328 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 330 */         logger.log(Level.WARNING, "No village for guardplan with villageid " + this.villageId, (Throwable)nsv);
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 338 */         Village village = getVillage();
/*     */         
/* 340 */         Guard[] guards = village.getGuards();
/*     */ 
/*     */         
/* 343 */         if (guards.length < this.hiredGuardNumber)
/*     */         {
/* 345 */           if (this.hiredGuardNumber <= 10 || guards.length <= 10 || this.siegeCount == 0) {
/*     */ 
/*     */             
/*     */             try {
/* 349 */               Item villToken = village.getToken();
/*     */ 
/*     */               
/* 352 */               if (Features.Feature.TOWER_CHAINING.isEnabled())
/*     */               {
/*     */                 
/* 355 */                 if (!villToken.isChained()) {
/*     */                   
/* 357 */                   this.waveCounter++;
/*     */ 
/*     */                   
/* 360 */                   if (this.waveCounter % 3 != 0)
/*     */                     return; 
/*     */                 } 
/*     */               }
/* 364 */               byte sex = 0;
/* 365 */               if (Server.rand.nextInt(2) == 0)
/* 366 */                 sex = 1; 
/* 367 */               int templateId = 32;
/* 368 */               if (village.kingdom == 3)
/* 369 */                 templateId = 33; 
/* 370 */               int minguards = Math.max(1, this.hiredGuardNumber / 10);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 375 */               for (int x = 0; x < Math.min(this.siegeCount + minguards, this.hiredGuardNumber - guards.length); x++) {
/*     */ 
/*     */                 
/*     */                 try {
/*     */                   
/* 380 */                   if (this.freeGuards.isEmpty()) {
/*     */                     
/* 382 */                     Creature newc = Creature.doNew(templateId, villToken.getPosX(), villToken
/* 383 */                         .getPosY(), Server.rand.nextInt(360), village.isOnSurface() ? 0 : -1, "", sex, village.kingdom);
/*     */                     
/* 385 */                     village.createGuard(newc, System.currentTimeMillis());
/*     */                   
/*     */                   }
/*     */                   else {
/*     */ 
/*     */                     
/* 391 */                     Creature toReturn = this.freeGuards.removeFirst();
/* 392 */                     if (toReturn.getTemplate().getTemplateId() != templateId)
/*     */                     {
/* 394 */                       removeReturnedGuard(toReturn.getWurmId());
/* 395 */                       toReturn.destroy();
/* 396 */                       Creature newc = Creature.doNew(templateId, villToken.getPosX(), villToken
/* 397 */                           .getPosY(), Server.rand.nextInt(360), village.isOnSurface() ? 0 : -1, "", sex, village.kingdom);
/*     */                       
/* 399 */                       village.createGuard(newc, System.currentTimeMillis());
/*     */                     }
/*     */                     else
/*     */                     {
/* 403 */                       village.createGuard(toReturn, System.currentTimeMillis());
/* 404 */                       removeReturnedGuard(toReturn.getWurmId());
/* 405 */                       putGuardInWorld(toReturn);
/*     */                     }
/*     */                   
/*     */                   } 
/* 409 */                 } catch (Exception ex) {
/*     */                   
/* 411 */                   logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */                 }
/*     */               
/*     */               } 
/* 415 */             } catch (NoSuchItemException nsi) {
/*     */               
/* 417 */               logger.log(Level.WARNING, "Village " + village.getName() + " has no token.");
/*     */             } 
/* 419 */             if (this.siegeCount > 0) {
/* 420 */               this.siegeCount += 3;
/*     */             }
/*     */           } 
/*     */         }
/* 424 */         village.checkForEnemies();
/*     */       }
/* 426 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 428 */         logger.log(Level.WARNING, "No village for guardplan with villageid " + this.villageId, (Throwable)nsv);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startSiege() {
/* 435 */     this.siegeCount = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnderSiege() {
/* 440 */     return (this.siegeCount > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSiegeCount() {
/* 445 */     return this.siegeCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void putGuardInWorld(Creature guard) {
/*     */     try {
/* 452 */       Item token = getVillage().getToken();
/* 453 */       guard.setPositionX(token.getPosX());
/* 454 */       guard.setPositionY(token.getPosY());
/*     */       
/*     */       try {
/* 457 */         guard.setLayer(token.isOnSurface() ? 0 : -1, false);
/* 458 */         guard.setPositionZ(Zones.calculateHeight(guard.getPosX(), guard.getPosY(), token.isOnSurface()));
/*     */         
/* 460 */         guard.respawn();
/* 461 */         Zone zone = Zones.getZone(guard.getTileX(), guard.getTileY(), guard.isOnSurface());
/* 462 */         zone.addCreature(guard.getWurmId());
/* 463 */         guard.savePosition(zone.getId());
/*     */       }
/* 465 */       catch (NoSuchZoneException nsz) {
/*     */         
/* 467 */         logger.log(Level.WARNING, "Guard: " + guard.getWurmId() + ": " + nsz.getMessage(), (Throwable)nsz);
/*     */       }
/* 469 */       catch (NoSuchCreatureException nsc) {
/*     */         
/* 471 */         logger.log(Level.WARNING, "Guard: " + guard.getWurmId() + ": " + nsc.getMessage(), (Throwable)nsc);
/* 472 */         getVillage().deleteGuard(guard, false);
/*     */       }
/* 474 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 476 */         logger.log(Level.WARNING, "Guard: " + guard.getWurmId() + ": " + nsp.getMessage(), (Throwable)nsp);
/*     */       }
/* 478 */       catch (Exception ex) {
/*     */         
/* 480 */         logger.log(Level.WARNING, "Failed to return village guard: " + ex.getMessage(), ex);
/*     */       }
/*     */     
/* 483 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 485 */       logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*     */     }
/* 487 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 489 */       logger.log(Level.WARNING, nsv.getMessage(), (Throwable)nsv);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void returnGuard(Creature guard) {
/* 495 */     if (!this.freeGuards.contains(guard)) {
/*     */       
/* 497 */       this.freeGuards.add(guard);
/* 498 */       addReturnedGuard(guard.getWurmId());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean pollUpkeep() {
/*     */     try {
/* 506 */       if ((getVillage()).isPermanent) {
/* 507 */         return false;
/*     */       }
/* 509 */     } catch (NoSuchVillageException noSuchVillageException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 514 */     if (!Servers.localServer.isUpkeep())
/*     */     {
/* 516 */       return false;
/*     */     }
/*     */     
/* 519 */     long upkeep = (long)calculateUpkeep(true);
/* 520 */     if (this.moneyLeft - upkeep <= 0L) {
/*     */ 
/*     */       
/*     */       try {
/* 524 */         logger.log(Level.INFO, getVillage().getName() + " disbanding. Money left=" + this.moneyLeft + ", upkeep=" + upkeep);
/*     */       }
/* 526 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 528 */         logger.log(Level.INFO, nsv.getMessage(), (Throwable)nsv);
/*     */       } 
/* 530 */       return true;
/*     */     } 
/* 532 */     if (upkeep >= 100L)
/*     */       
/*     */       try {
/* 535 */         logger.log(Level.INFO, getVillage().getName() + " upkeep=" + upkeep);
/*     */       }
/* 537 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 539 */         logger.log(Level.INFO, nsv.getMessage(), (Throwable)nsv);
/*     */       }  
/* 541 */     updateGuardPlan(this.type, this.moneyLeft - Math.max(1L, upkeep), this.hiredGuardNumber);
/* 542 */     this.upkeepCounter++;
/*     */     
/* 544 */     if (this.upkeepCounter == 2) {
/*     */       
/* 546 */       this.upkeepCounter = 0;
/* 547 */       Shop shop = Economy.getEconomy().getKingsShop();
/* 548 */       if (shop != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 560 */         if (upkeep <= 1L) {
/*     */           
/* 562 */           shop.setMoney(shop.getMoney() + Math.max(1L, upkeep));
/*     */         } else {
/*     */           
/* 565 */           shop.setMoney(shop.getMoney() + upkeep);
/*     */         } 
/*     */       } else {
/* 568 */         logger.log(Level.WARNING, "No shop when " + this.villageId + " paying upkeep.");
/*     */       } 
/* 570 */     }  long tl = getTimeLeft();
/* 571 */     if (tl < 3600000L) {
/*     */       
/*     */       try
/*     */       {
/* 575 */         getVillage()
/* 576 */           .broadCastAlert("The village is disbanding within the hour. You may add upkeep money to the village coffers at the token immediately.", (byte)2);
/*     */ 
/*     */         
/* 579 */         getVillage().broadCastAlert("Any traders who are citizens of " + getVillage().getName() + " will disband without refund.");
/*     */       
/*     */       }
/* 582 */       catch (NoSuchVillageException nsv)
/*     */       {
/* 584 */         logger.log(Level.WARNING, "No Village? " + this.villageId, (Throwable)nsv);
/*     */       }
/*     */     
/* 587 */     } else if (tl < 86400000L) {
/*     */       
/* 589 */       if (System.currentTimeMillis() - this.lastSentWarning > 3600000L) {
/*     */         
/* 591 */         this.lastSentWarning = System.currentTimeMillis();
/*     */         
/*     */         try {
/* 594 */           getVillage()
/* 595 */             .broadCastAlert("The village is disbanding within 24 hours. You may add upkeep money to the village coffers at the token.", (byte)2);
/*     */ 
/*     */           
/* 598 */           getVillage().broadCastAlert("Any traders who are citizens of " + getVillage().getName() + " will disband without refund.");
/*     */         
/*     */         }
/* 601 */         catch (NoSuchVillageException nsv) {
/*     */           
/* 603 */           logger.log(Level.WARNING, "No Village? " + this.villageId, (Throwable)nsv);
/*     */         }
/*     */       
/*     */       } 
/* 607 */     } else if (tl < 604800000L) {
/*     */       
/* 609 */       if (System.currentTimeMillis() - this.lastSentWarning > 3600000L) {
/*     */         
/* 611 */         this.lastSentWarning = System.currentTimeMillis();
/*     */         
/*     */         try {
/* 614 */           getVillage()
/* 615 */             .broadCastAlert("The village is disbanding within one week. Due to the low morale this gives, the guards have ceased their general maintenance of structures.", (byte)4);
/*     */ 
/*     */           
/* 618 */           getVillage().broadCastAlert("Any traders who are citizens of " + getVillage().getName() + " will disband without refund.");
/*     */         
/*     */         }
/* 621 */         catch (NoSuchVillageException nsv) {
/*     */           
/* 623 */           logger.log(Level.WARNING, "No Village? " + this.villageId, (Throwable)nsv);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 629 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void destroyGuard(Creature guard) {
/* 634 */     this.freeGuards.remove(guard);
/* 635 */     removeReturnedGuard(guard.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean poll() {
/* 643 */     pollGuards();
/*     */     
/* 645 */     if (this.siegeCount > 0) {
/*     */       
/* 647 */       this.siegeCount--;
/*     */       
/* 649 */       this.siegeCount = Math.min(this.siegeCount, 9);
/*     */       
/*     */       try {
/* 652 */         if (!getVillage().isAlerted()) {
/* 653 */           this.siegeCount = Math.max(0, this.siegeCount - 1);
/*     */         }
/* 655 */       } catch (NoSuchVillageException nsv) {
/*     */         
/* 657 */         logger.log(Level.WARNING, nsv.getMessage());
/*     */       } 
/*     */     } 
/* 660 */     if (this.drainModifier > 0.0F && System.currentTimeMillis() - this.lastDrained > 172800000L) {
/*     */       
/* 662 */       this.drainModifier = 0.0F;
/* 663 */       saveDrainMod();
/*     */     } 
/* 665 */     return pollUpkeep();
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getLastDrained() {
/* 670 */     return this.lastDrained;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getMaxGuards(Village village) {
/* 675 */     return getMaxGuards(village.getDiameterX(), village.getDiameterY());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int getMaxGuards(int diameterX, int diameterY) {
/* 681 */     return Math.min(maxGuards, Math.max(3, diameterX * diameterY / 49));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getNumHiredGuards() {
/* 686 */     return this.hiredGuardNumber;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getConvertedGuardNumber(Village village) {
/* 691 */     int max = getMaxGuards(village);
/* 692 */     if (this.type == 1)
/* 693 */       max = Math.max(1, max / 4); 
/* 694 */     if (this.type == 2) {
/* 695 */       Math.max(1, max /= 2);
/*     */     }
/* 697 */     return max;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void changePlan(int newPlan, int newNumberOfGuards) {
/* 702 */     this.lastChangedPlan = System.currentTimeMillis();
/* 703 */     int changeInGuards = newNumberOfGuards - getNumHiredGuards();
/* 704 */     updateGuardPlan(newPlan, this.moneyLeft, newNumberOfGuards);
/* 705 */     if (changeInGuards < 0) {
/*     */       
/*     */       try {
/*     */         
/* 709 */         Village village = getVillage();
/* 710 */         int deleted = 0;
/*     */ 
/*     */         
/* 713 */         changeInGuards = Math.abs(changeInGuards);
/* 714 */         if (this.freeGuards.size() > 0) {
/*     */           
/* 716 */           Creature[] crets = this.freeGuards.<Creature>toArray(new Creature[this.freeGuards.size()]);
/* 717 */           for (int x = 0; x < Math.min(crets.length, changeInGuards); x++) {
/*     */             
/* 719 */             deleted++;
/* 720 */             removeReturnedGuard(crets[x].getWurmId());
/* 721 */             crets[x].destroy();
/*     */           } 
/*     */         } 
/* 724 */         if (deleted < changeInGuards) {
/*     */           
/* 726 */           Guard[] guards = village.getGuards();
/* 727 */           for (int x = 0; x < Math.min(guards.length, changeInGuards - deleted); x++) {
/*     */             
/* 729 */             if ((guards[x]).creature.isSpiritGuard()) {
/* 730 */               village.deleteGuard((guards[x]).creature, true);
/*     */             }
/*     */           } 
/*     */         } 
/* 734 */       } catch (NoSuchVillageException nsv) {
/*     */         
/* 736 */         logger.log(Level.WARNING, "Village lacking for plan " + this.villageId, (Throwable)nsv);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addMoney(long moneyAdded) {
/* 743 */     if (moneyAdded > 0L)
/*     */     {
/* 745 */       updateGuardPlan(this.type, this.moneyLeft + moneyAdded, this.hiredGuardNumber);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getTimeToNextDrain() {
/*     */     try {
/* 753 */       if ((getVillage()).isPermanent) {
/* 754 */         return 86400000L;
/*     */       }
/* 756 */     } catch (NoSuchVillageException nsv) {
/*     */       
/* 758 */       logger.log(Level.WARNING, this.villageId + ", " + nsv.getMessage(), (Throwable)nsv);
/* 759 */       return 86400000L;
/*     */     } 
/* 761 */     return this.lastDrained + 86400000L - System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getMoneyDrained() {
/*     */     try {
/* 768 */       if ((getVillage()).isPermanent) {
/* 769 */         return 0L;
/*     */       }
/* 771 */     } catch (NoSuchVillageException nsv) {
/*     */       
/* 773 */       logger.log(Level.WARNING, this.villageId + ", " + nsv.getMessage(), (Throwable)nsv);
/* 774 */       return 0L;
/*     */     } 
/*     */     
/* 777 */     return (long)Math.min((float)this.moneyLeft, (1.0F + this.drainModifier) * Math.max(7500.0F, (float)getMonthlyCost() * 0.15F));
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
/*     */   public long drainMoney() {
/* 791 */     long moneyToDrain = getMoneyDrained();
/* 792 */     drainGuardPlan(this.moneyLeft - moneyToDrain);
/* 793 */     this.drainModifier = 0.5F + this.drainModifier;
/* 794 */     saveDrainMod();
/* 795 */     return moneyToDrain;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fixGuards() {
/*     */     try {
/* 802 */       Guard[] gs = getVillage().getGuards();
/* 803 */       for (int x = 0; x < gs.length; x++) {
/*     */         
/* 805 */         if ((gs[x]).creature.isDead())
/*     */         {
/* 807 */           getVillage().deleteGuard((gs[x]).creature, false);
/*     */           
/* 809 */           returnGuard((gs[x]).creature);
/* 810 */           logger.log(Level.INFO, "Destroyed dead guard for " + getVillage().getName());
/*     */         }
/*     */       
/*     */       } 
/* 814 */     } catch (NoSuchVillageException nsv) {
/*     */       
/* 816 */       logger.log(Level.WARNING, "Village lacking for plan " + this.villageId, (Throwable)nsv);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getProsperityModifier() {
/* 822 */     if (getMoneyLeft() > 1000000L)
/*     */     {
/* 824 */       return 1.05F;
/*     */     }
/* 826 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateGuardPlan(long aMoneyLeft) {
/* 831 */     updateGuardPlan(this.type, aMoneyLeft, this.hiredGuardNumber);
/*     */   }
/*     */   
/*     */   abstract void create();
/*     */   
/*     */   abstract void load();
/*     */   
/*     */   public abstract void updateGuardPlan(int paramInt1, long paramLong, int paramInt2);
/*     */   
/*     */   abstract void delete();
/*     */   
/*     */   abstract void addReturnedGuard(long paramLong);
/*     */   
/*     */   abstract void removeReturnedGuard(long paramLong);
/*     */   
/*     */   abstract void saveDrainMod();
/*     */   
/*     */   abstract void deleteReturnedGuards();
/*     */   
/*     */   public abstract void addPayment(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   abstract void drainGuardPlan(long paramLong);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\GuardPlan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */