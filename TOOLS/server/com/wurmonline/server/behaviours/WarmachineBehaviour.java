/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.math.Vector3f;
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MeshTile;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.combat.ServerProjectile;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WarmachineBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  65 */   private static final Logger logger = Logger.getLogger(WarmachineBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   WarmachineBehaviour() {
/*  72 */     super((short)40);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  78 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  79 */     addToList(performer, toReturn, target, null);
/*  80 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  86 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  87 */     toReturn.addAll(super.getBehavioursFor(performer, source, target));
/*     */     
/*  89 */     addToList(performer, toReturn, target, source);
/*     */     
/*  91 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToList(Creature performer, List<ActionEntry> toReturn, Item target, @Nullable Item source) {
/*  98 */     boolean reachable = false;
/*  99 */     if (target.getOwnerId() == -10L) {
/*     */       
/* 101 */       int distance = (target.getTemplateId() == 1125) ? 2 : 4;
/* 102 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), distance))
/*     */       {
/* 104 */         BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/* 105 */         if (result == null)
/*     */         {
/* 107 */           reachable = true;
/*     */         }
/*     */       }
/*     */     
/* 111 */     } else if (target.getOwnerId() == performer.getWurmId()) {
/*     */       
/* 113 */       reachable = true;
/*     */     } 
/*     */     
/* 116 */     if (target.getTopParent() != target.getWurmId()) {
/*     */       return;
/*     */     }
/* 119 */     if (reachable) {
/*     */       
/* 121 */       if (target.getTemplateId() == 1125) {
/*     */         
/* 123 */         toReturn.add(Actions.actionEntrys[851]);
/*     */         
/*     */         return;
/*     */       } 
/* 127 */       boolean loaded = false;
/* 128 */       boolean mayload = false;
/* 129 */       boolean maywinch = false;
/* 130 */       boolean mayfire = false;
/* 131 */       short nums = 0;
/* 132 */       if (target.getData() <= 0L) {
/*     */         
/* 134 */         loaded = false;
/* 135 */         if (source != null && source.canBeDropped(true))
/*     */         {
/* 137 */           if (!source.isArtifact())
/*     */           {
/* 139 */             mayload = true;
/* 140 */             nums = (short)(nums - 1);
/*     */           }
/*     */         
/*     */         }
/*     */       } else {
/*     */         
/* 146 */         nums = (short)(nums - 1);
/* 147 */         loaded = true;
/* 148 */         if (target.getWinches() < 50 && target.getTemplateId() != 937) {
/*     */           
/* 150 */           maywinch = true;
/* 151 */           nums = (short)(nums - 3);
/*     */         } 
/* 153 */         if (target.getWinches() > 0 || (target.getTemplateId() == 937 && !target.isEmpty(false)))
/*     */         {
/* 155 */           if (target.getTemplateId() != 937 || target.mayFireTrebuchet()) {
/*     */             
/* 157 */             mayfire = true;
/* 158 */             nums = (short)(nums - ((target.getTemplateId() == 937) ? 1 : 2));
/*     */           } 
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 164 */       if (maywinch || mayfire)
/* 165 */         nums = (short)(nums - 2); 
/* 166 */       if (nums < 0 && target.getPosZ() > 0.0F) {
/*     */         
/* 168 */         toReturn.add(new ActionEntry(nums, "Machine", "Machine options"));
/* 169 */         if (!loaded && mayload) {
/* 170 */           toReturn.add(Actions.actionEntrys[233]);
/* 171 */         } else if (loaded) {
/* 172 */           toReturn.add(Actions.actionEntrys[234]);
/* 173 */         }  if (maywinch || mayfire) {
/*     */           
/* 175 */           toReturn.add(Actions.actionEntrys[849]);
/* 176 */           toReturn.add(Actions.actionEntrys[850]);
/*     */         } 
/* 178 */         if (maywinch) {
/*     */           
/* 180 */           toReturn.add(Actions.actionEntrys[237]);
/* 181 */           toReturn.add(Actions.actionEntrys[238]);
/* 182 */           toReturn.add(Actions.actionEntrys[239]);
/*     */         } 
/* 184 */         if (mayfire) {
/*     */           
/* 186 */           toReturn.add(Actions.actionEntrys[236]);
/* 187 */           if (target.getTemplateId() != 937) {
/* 188 */             toReturn.add(Actions.actionEntrys[235]);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 197 */     boolean done = false;
/* 198 */     if (action == 237 || action == 239 || action == 238 || action == 234 || action == 236 || action == 235 || action == 849 || action == 850 || action == 851) {
/*     */ 
/*     */ 
/*     */       
/* 202 */       done = action(act, performer, null, target, action, counter);
/*     */     } else {
/*     */       
/* 205 */       done = super.action(act, performer, target, action, counter);
/* 206 */     }  return done;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean mayManouvre(Creature performer, Item warMachine) {
/* 211 */     int tx = warMachine.getTileX();
/* 212 */     int ty = warMachine.getTileY();
/*     */     
/* 214 */     VolaTile wmT = Zones.getOrCreateTile(tx, ty, warMachine.isOnSurface());
/* 215 */     for (int x = tx - 2; x <= tx + 2; x++) {
/*     */       
/* 217 */       for (int y = ty - 2; y <= ty + 2; y++) {
/*     */         
/* 219 */         VolaTile t = Zones.getTileOrNull(Zones.safeTileX(x), Zones.safeTileY(y), warMachine.isOnSurface());
/* 220 */         if (t != null) {
/*     */           
/* 222 */           Item[] items = t.getItems();
/* 223 */           for (Item item : items) {
/*     */             
/* 225 */             if (item.isUseOnGroundOnly())
/*     */             {
/* 227 */               if (item.getWurmId() != warMachine.getWurmId() && item
/* 228 */                 .getPos3f().distance(warMachine.getPos3f()) <= 8.0F)
/*     */               {
/* 230 */                 if (t.getStructure() == wmT.getStructure()) {
/*     */                   
/* 232 */                   performer.getCommunicator().sendAlertServerMessage("You can't work with the " + warMachine
/* 233 */                       .getName() + ". This area is too crowded.");
/* 234 */                   return false;
/*     */                 } 
/*     */               }
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter) {
/* 249 */     boolean done = false;
/* 250 */     boolean reachable = false;
/* 251 */     if (target.getOwnerId() == -10L) {
/*     */       
/* 253 */       int distance = (target.getTemplateId() == 1125) ? 2 : 4;
/* 254 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), distance))
/*     */       {
/* 256 */         reachable = true;
/*     */       }
/*     */     }
/* 259 */     else if (target.getOwnerId() == performer.getWurmId()) {
/*     */       
/* 261 */       reachable = true;
/*     */     } 
/* 263 */     if (performer.isOnSurface() != target.isOnSurface())
/*     */     {
/* 265 */       reachable = false;
/*     */     }
/*     */     
/* 268 */     if (!reachable) {
/*     */       
/* 270 */       performer.getCommunicator().sendNormalServerMessage("You cannot reach the " + target.getName() + " from there.");
/* 271 */       return true;
/*     */     } 
/*     */     
/* 274 */     if (action == 237 || action == 239 || action == 238 || action == 233 || action == 234 || action == 236 || action == 235 || action == 849 || action == 850 || action == 851) {
/*     */ 
/*     */ 
/*     */       
/* 278 */       if (source != null && source.isBanked())
/*     */       {
/* 280 */         return true;
/*     */       }
/* 282 */       if (!mayManouvre(performer, target))
/* 283 */         return true; 
/* 284 */       if (target.getPosZ() < 0.0F)
/* 285 */         return true; 
/* 286 */       if (reachable) {
/*     */         
/* 288 */         Skills skills = performer.getSkills();
/*     */         
/*     */         try {
/* 291 */           Skill str = skills.getSkill(102);
/* 292 */           if (str.getKnowledge(0.0D) <= 21.0D)
/*     */           {
/* 294 */             String message = StringUtil.format("You are too weak to handle the heavy %s. You need 21 %s.", new Object[] { target
/*     */                   
/* 296 */                   .getName(), str.getName() });
/* 297 */             performer.getCommunicator().sendNormalServerMessage(message);
/* 298 */             return true;
/*     */           }
/*     */         
/* 301 */         } catch (NoSuchSkillException nss) {
/*     */           
/* 303 */           logger.log(Level.WARNING, "Weird, " + performer.getName() + " has no strength!");
/*     */         } 
/*     */         
/* 306 */         if (action == 233)
/*     */         {
/* 308 */           return loadCatapult(performer, act, source, target, action, counter);
/*     */         }
/* 310 */         if (action == 234) {
/*     */           
/* 312 */           unloadCatapult(performer, target);
/* 313 */           return true;
/*     */         } 
/* 315 */         if (action == 237 || action == 239 || action == 238)
/*     */         {
/* 317 */           return winchCatapult(performer, act, target, action, counter);
/*     */         }
/* 319 */         if (action == 235) {
/*     */           
/* 321 */           unwindCatapult(performer, target);
/* 322 */           return true;
/*     */         } 
/* 324 */         if (action == 236) {
/*     */           
/* 326 */           fireCatapult(act, performer, target);
/* 327 */           return true;
/*     */         } 
/* 329 */         if (action == 849 || action == 850)
/*     */         {
/* 331 */           return changeFiringAngle(performer, act, target, action, counter);
/*     */         }
/* 333 */         if (action == 851)
/*     */         {
/* 335 */           return useBatteringRam(performer, act, target, action, counter);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 340 */       done = super.action(act, performer, source, target, action, counter);
/* 341 */     }  return done;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean useBatteringRam(Creature performer, Action act, Item target, short action, float counter) {
/* 346 */     if (performer.getStrengthSkill() < 21.0D) {
/*     */       
/* 348 */       performer.getCommunicator().sendNormalServerMessage("You are too weak to use the " + target.getName() + " effectively.");
/* 349 */       return true;
/*     */     } 
/* 351 */     if (Items.isItemDragged(target)) {
/*     */       
/* 353 */       performer.getCommunicator().sendNormalServerMessage("You cannot use the " + target.getName() + " while it is being dragged.");
/* 354 */       return true;
/*     */     } 
/* 356 */     MeshTile m = new MeshTile(target.isOnSurface() ? Server.surfaceMesh : Server.caveMesh, target.getTileX(), target.getTileY());
/* 357 */     if (m.checkSlopes(30, 45)) {
/*     */       
/* 359 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " cannot be used on such a slope.");
/* 360 */       return true;
/*     */     } 
/*     */     
/* 363 */     target.lastRammed = System.currentTimeMillis();
/* 364 */     target.lastRamUser = performer.getWurmId();
/*     */     
/* 366 */     float swingDistance = 1.0F;
/*     */     
/* 368 */     Vector3f targetPos = new Vector3f((float)(swingDistance * Math.cos((target.getRotation() - 90.0F) * Math.PI / 180.0D)), (float)(swingDistance * Math.sin((target.getRotation() - 90.0F) * Math.PI / 180.0D)), 0.0F);
/* 369 */     Vector3f fromPos = target.getPos3f().add(targetPos.mult(2.0F).add(0.0F, 0.0F, target.getTemplate().getSizeY() / 300.0F));
/* 370 */     targetPos = fromPos.add(targetPos.add(0.0F, 0.0F, 0.33F));
/*     */     
/* 372 */     Skill warmachines = performer.getSkills().getSkillOrLearn(1029);
/* 373 */     double skillModifier = 1.0D - warmachines.getKnowledge(0.0D) / 300.0D;
/* 374 */     boolean done = (counter >= 2.0F && counter >= 30.0D * skillModifier);
/* 375 */     if (counter == 1.0F) {
/*     */       
/* 377 */       performer.getCommunicator().sendNormalServerMessage("You start pulling back the beam on the " + target.getName() + ".");
/* 378 */       performer.sendActionControl(act.getActionString(), true, (int)Math.round(300.0D * skillModifier));
/*     */     } 
/* 380 */     if (done) {
/*     */       
/* 382 */       performer.getCommunicator().sendNormalServerMessage("You swing the beam on the " + target.getName() + " forward.");
/* 383 */       Server.getInstance().broadCastAction(performer.getName() + " swings the beam on the " + target.getName() + " forward.", performer, 5);
/*     */       
/* 385 */       VolaTile t = Zones.getTileOrNull(target.getTilePos(), target.isOnSurface());
/* 386 */       if (t != null) {
/*     */ 
/*     */ 
/*     */         
/* 390 */         BlockingResult result = Blocking.getBlockerBetween(null, fromPos.getX(), fromPos.getY(), targetPos.getX(), targetPos.getY(), fromPos
/* 391 */             .getZ(), targetPos.getZ(), target.isOnSurface(), target.isOnSurface(), true, 4, -10L, target
/* 392 */             .getBridgeId(), -10L, false);
/*     */         
/* 394 */         if (result != null) {
/*     */           
/* 396 */           VolaTile targetTile = Zones.getOrCreateTile(result.getFirstBlocker().getTileX(), result
/* 397 */               .getFirstBlocker().getTileY(), target.isOnSurface());
/* 398 */           Village v = targetTile.getVillage();
/* 399 */           if (!ServerProjectile.isOkToAttack(targetTile, performer, 10.0F)) {
/*     */             
/* 401 */             boolean ok = false;
/* 402 */             if (v != null)
/*     */             {
/* 404 */               if (v.isActionAllowed((result.getFirstBlocker() instanceof Fence) ? 172 : 174, performer, false, 0, 0) || v
/* 405 */                 .isEnemy(performer))
/*     */               {
/* 407 */                 ok = true;
/*     */               }
/*     */             }
/*     */             
/* 411 */             if (!ok) {
/*     */               
/* 413 */               performer.getCommunicator().sendNormalServerMessage("You stop the " + target.getName() + " at the last second as you realise you are not allowed to do that here.");
/*     */               
/* 415 */               return true;
/*     */             } 
/*     */           } 
/*     */           
/* 419 */           double power = -1.0D;
/* 420 */           if (result.getFirstBlocker() instanceof Wall) {
/*     */             
/* 422 */             Wall wall = (Wall)result.getFirstBlocker();
/* 423 */             power = warmachines.skillCheck((wall.getCurrentQualityLevel() / 3.0F), target, 0.0D, false, 
/* 424 */                 Math.max(1.0F, wall.getCurrentQualityLevel() / 20.0F));
/*     */           }
/* 426 */           else if (result.getFirstBlocker() instanceof Fence) {
/*     */             
/* 428 */             Fence fence = (Fence)result.getFirstBlocker();
/* 429 */             power = warmachines.skillCheck((fence.getCurrentQualityLevel() / 4.0F), target, 0.0D, false, 
/* 430 */                 Math.max(1.0F, fence.getCurrentQualityLevel() / 20.0F));
/*     */           }
/*     */           else {
/*     */             
/* 434 */             power = warmachines.skillCheck((40.0F - result.getFirstBlocker().getDamage()), target, 0.0D, false, 
/* 435 */                 Math.max(1.0F, 5.0F - result.getFirstBlocker().getDamage() / 20.0F));
/*     */           } 
/*     */           
/* 438 */           float damage = 12.5F;
/* 439 */           damage = (float)(damage * Math.max(0.75D, power / 75.0D));
/* 440 */           damage *= 0.8F + target.getCurrentQualityLevel() / 500.0F;
/* 441 */           if (target.isOnSurface())
/* 442 */             damage *= 0.5F; 
/* 443 */           damage = Math.min(20.0F, damage);
/*     */           
/* 445 */           if (power < 0.0D) {
/* 446 */             damage *= 0.2F;
/*     */           }
/* 448 */           if (v != null)
/*     */           {
/* 450 */             if (MethodsStructure.isCitizenAndMayPerformAction((short)174, performer, v)) {
/*     */               
/* 452 */               damage *= 5.0F;
/*     */             }
/* 454 */             else if (MethodsStructure.isAllyAndMayPerformAction((short)174, performer, v)) {
/*     */               
/* 456 */               damage *= 2.5F;
/*     */             } 
/*     */           }
/*     */           
/* 460 */           performer.getCommunicator().sendNormalServerMessage("You swing the beam on the " + target.getName() + " forward and it hits a " + result
/* 461 */               .getFirstBlocker().getName() + ((power < 0.0D) ? " but deals less damage than you expected." : "."));
/* 462 */           Server.getInstance().broadCastAction(performer.getName() + " swings the beam on the " + target.getName() + " forward and hits a " + result
/* 463 */               .getFirstBlocker().getName() + ".", performer, 5);
/*     */           
/* 465 */           if (Servers.localServer.testServer) {
/* 466 */             performer.getCommunicator().sendNormalServerMessage("[TEST] Skillcheck: " + power + ", Total Damage: " + (damage * result
/* 467 */                 .getFirstBlocker().getDamageModifier()));
/*     */           }
/* 469 */           result.getFirstBlocker().setDamage(result.getFirstBlocker().getDamage() + damage * result
/* 470 */               .getFirstBlocker().getDamageModifier());
/* 471 */           target.setDamage(target.getDamage() + damage / 100.0F * target.getDamageModifier());
/*     */         }
/*     */         else {
/*     */           
/* 475 */           performer.getCommunicator().sendNormalServerMessage("The beam of the " + target.getName() + " hits nothing.");
/*     */         } 
/*     */       } 
/* 478 */       return true;
/*     */     } 
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean changeFiringAngle(Creature performer, Action act, Item target, short action, float counter) {
/* 485 */     byte currentAngle = target.getAuxData();
/* 486 */     if (action == 849 && currentAngle >= 8) {
/*     */       
/* 488 */       performer.getCommunicator().sendNormalServerMessage("You cannot increase the firing angle any further.");
/* 489 */       return true;
/*     */     } 
/* 491 */     if (action == 850 && currentAngle <= -8) {
/*     */       
/* 493 */       performer.getCommunicator().sendNormalServerMessage("You cannot reduce the firing angle any further.");
/* 494 */       return true;
/*     */     } 
/*     */     
/* 497 */     int skillNum = 10077;
/* 498 */     if (target.getTemplateId() == 936) {
/* 499 */       skillNum = 10093;
/* 500 */     } else if (target.getTemplateId() == 937) {
/* 501 */       skillNum = 10094;
/* 502 */     }  double speedModifier = 1.0D - performer.getSkills().getSkillOrLearn(skillNum).getKnowledge(0.0D) / 300.0D;
/*     */     
/* 504 */     boolean done = (counter >= 2.0F && counter >= (int)Math.round(5.0D * speedModifier));
/* 505 */     if (counter == 1.0F) {
/*     */       
/* 507 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 508 */           .getName() + " creaks as you change the firing angle.");
/* 509 */       performer.sendActionControl(act.getActionString(), true, (int)Math.round(50.0D * speedModifier));
/*     */     } 
/*     */     
/* 512 */     if (done) {
/*     */       
/* 514 */       target.setAuxData((byte)Math.min(8, Math.max(-8, currentAngle + ((action == 849) ? 1 : -1))));
/* 515 */       Server.getInstance().broadCastAction("The " + target.getName() + " creaks as " + performer
/* 516 */           .getName() + " slightly changes the firing angle.", performer, 5);
/*     */       
/* 518 */       float angle = 45.0F + (target.getAuxData() * 5);
/* 519 */       float zHeight = target.getTemplate().getSizeY() * 0.75F / 100.0F;
/* 520 */       float approxDistance = ServerProjectile.getProjectileDistance(target.getPos3f(), zHeight, target
/* 521 */           .getWinches(), (float)((target.getRotation() - 90.0F) * Math.PI / 180.0D), (float)(angle * Math.PI / 180.0D));
/*     */       
/* 523 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 524 */           .getName() + " will now " + (
/* 525 */           (target.getTemplateId() == 936) ? "shoot" : "throw") + " approximately " + 
/* 526 */           Math.round(approxDistance / 4.0F) + " tiles with " + (
/* 527 */           (target.getTemplateId() == 937) ? ("a loaded weight of " + target.getWinches()) : (target.getWinches() + " winches")) + " and an angle of around " + angle + " degrees.");
/*     */ 
/*     */       
/* 530 */       return true;
/*     */     } 
/*     */     
/* 533 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireCatapult(Action act, Creature performer, Item target) {
/* 544 */     if (target.getData() <= 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 548 */     if (target.getWinches() < 10 && target.getTemplateId() != 937) {
/*     */       
/* 550 */       performer.getCommunicator().sendNormalServerMessage("You need to winch the " + target
/* 551 */           .getName() + " more before it can fire properly.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 556 */     if (!target.isOnSurface()) {
/*     */       
/* 558 */       performer.getCommunicator().sendNormalServerMessage("You cannot fire the " + target
/* 559 */           .getName() + " below ground.");
/*     */       
/*     */       return;
/*     */     } 
/* 563 */     int sx = target.getTileX();
/* 564 */     int sy = target.getTileY();
/* 565 */     VolaTile t = Zones.getTileOrNull(sx, sy, target.isOnSurface());
/* 566 */     if (t != null && t.getStructure() != null && target.onBridge() == -10L) {
/*     */       
/* 568 */       performer.getCommunicator().sendNormalServerMessage("You cannot fire the " + target
/* 569 */           .getName() + " inside a structure.");
/*     */       
/*     */       return;
/*     */     } 
/* 573 */     if (target.getTemplateId() == 937) {
/*     */       
/* 575 */       if (!target.mayFireTrebuchet()) {
/*     */         
/* 577 */         performer.getCommunicator().sendNormalServerMessage("The trebuchet still hasn't stabilized itself enough to fire again.");
/*     */         
/*     */         return;
/*     */       } 
/* 581 */       if (target.isEmpty(false)) {
/*     */         
/* 583 */         performer.getCommunicator().sendNormalServerMessage("There is no counter weight in the trebuchet, so it will not be able to fire.");
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 588 */       if (!Terraforming.isFlat(target.getTileX(), target.getTileY(), target.isOnSurface(), 5)) {
/*     */         
/* 590 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 591 */             .getName() + " must stand on more level ground in order to be fired.");
/*     */         
/*     */         return;
/*     */       } 
/* 595 */       int weight = 0;
/* 596 */       for (Item i : target.getAllItems(true))
/* 597 */         weight += i.getWeightGrams(); 
/* 598 */       target.setWinches((short)(byte)Math.min(50, weight / 20000));
/* 599 */       if (target.getWinches() < 25) {
/*     */         
/* 601 */         performer.getCommunicator().sendNormalServerMessage(" You cannot fire now. You need more counter weight.");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 607 */     Item projectile = null;
/*     */     
/*     */     try {
/* 610 */       projectile = Items.getItem(target.getData());
/* 611 */       if (projectile.isBanked() || projectile.getOwnerId() != -10L) {
/*     */         
/* 613 */         performer.getCommunicator().sendNormalServerMessage("The " + projectile
/* 614 */             .getName() + " crumbles to dust in mid-air.");
/* 615 */         if (performer.getVisionArea() != null)
/*     */         {
/* 617 */           performer.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*     */         }
/*     */         return;
/*     */       } 
/* 621 */       if (projectile.isUnfinished()) {
/*     */         
/* 623 */         performer.getCommunicator().sendNormalServerMessage("The " + projectile
/* 624 */             .getName() + " can't be fired in this state.");
/*     */         
/*     */         return;
/*     */       } 
/* 628 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 630 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 631 */           .getName() + " was empty.");
/* 632 */       target.setData(0L);
/* 633 */       target.setWinches((short)0);
/* 634 */       if (performer.getVisionArea() != null)
/*     */       {
/* 636 */         performer.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*     */       }
/*     */       return;
/*     */     } 
/* 640 */     if (target.getWinches() < 10) {
/*     */       
/* 642 */       performer.getCommunicator().sendNormalServerMessage(" You cannot fire now. You need to tighten the rope more.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 647 */     float dist = (target.getWinches() + 1);
/*     */     
/* 649 */     float damdealt = 0.0F;
/*     */     
/* 651 */     if ((projectile != null && projectile.isMetal()) || projectile.isStone() || projectile.isCorpse() || projectile
/* 652 */       .getTemplateId() == 932)
/*     */     {
/* 654 */       if (projectile.getTemplateId() != 298 && projectile
/* 655 */         .getTemplateId() != 26 && !projectile.isEgg()) {
/*     */         
/* 657 */         damdealt = (10.0F + dist / 10.0F) * projectile.getCurrentQualityLevel() / 100.0F;
/* 658 */         if (target.getTemplateId() != 936)
/*     */         {
/* 660 */           damdealt = damdealt * projectile.getWeightGrams() / 10000.0F;
/*     */         }
/* 662 */         if (performer.getCitizenVillage() != null)
/* 663 */           damdealt *= 1.0F + performer.getCitizenVillage().getFaithWarBonus() / 100.0F; 
/* 664 */         damdealt *= (1 + act.getRarity());
/* 665 */         damdealt *= (1 + projectile.getRarity());
/* 666 */         damdealt *= 1.0F + target.getRarity() * 0.1F;
/* 667 */         if (projectile.isCorpse()) {
/* 668 */           damdealt *= 0.1F;
/* 669 */         } else if (target.getTemplateId() == 937) {
/* 670 */           damdealt *= 2.0F;
/*     */         } 
/*     */       }  } 
/* 673 */     float rot = target.getRotation();
/* 674 */     rot = Creature.normalizeAngle(rot - 90.0F);
/* 675 */     int newx = (int)(Math.cos(rot * Math.PI / 180.0D) * dist) + sx;
/* 676 */     int newy = (int)(Math.sin(rot * Math.PI / 180.0D) * dist) + sy;
/*     */     
/* 678 */     if (performer.getPower() >= 5 && !Features.Feature.NEW_PROJECTILES.isEnabled()) {
/* 679 */       performer.getCommunicator().sendNormalServerMessage("You are firing from " + sx + "," + sy + " dist:" + dist + " with rotation " + rot + " to " + newx + "," + newy + ".");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 685 */       ServerProjectile proj = new ServerProjectile(target, projectile, (newx * 4 + 2), (newy * 4 + 2), performer, act.getRarity(), damdealt);
/*     */ 
/*     */       
/* 688 */       if (!proj.fire(target.isOnSurface()))
/*     */       {
/* 690 */         ServerProjectile.removeProjectile(proj);
/*     */       }
/* 692 */       if (performer.getVisionArea() != null)
/*     */       {
/* 694 */         performer.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*     */       }
/*     */     }
/* 697 */     catch (NoSuchZoneException nsz) {
/*     */       
/* 699 */       performer.getCommunicator().sendNormalServerMessage("The catapult malfunctions, and you suspect it will need to be turned or moved.");
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
/*     */   private void unwindCatapult(Creature performer, Item target) {
/* 711 */     if (target.getData() <= 0L) {
/*     */       return;
/*     */     }
/* 714 */     if (target.getWinches() <= 0) {
/*     */       return;
/*     */     }
/* 717 */     performer.getCommunicator().sendNormalServerMessage("The " + target
/* 718 */         .getName() + " makes a whirring sound as you release the tension on the winch.");
/* 719 */     Server.getInstance().broadCastAction("The " + target
/* 720 */         .getName() + " makes a whirring sound as " + performer.getName() + " releases the tension on the winch.", performer, 5);
/*     */     
/* 722 */     target.setWinches((short)0);
/* 723 */     if (performer.getVisionArea() != null)
/*     */     {
/* 725 */       performer.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
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
/*     */   private final boolean winchCatapult(Creature performer, Action act, Item target, short action, float counter) {
/* 740 */     if (target.getData() <= 0L)
/* 741 */       return true; 
/* 742 */     if (!act.justTickedSecond())
/* 743 */       return false; 
/* 744 */     if (target.getTemplateId() == 937) {
/*     */       
/* 746 */       performer.getCommunicator().sendNormalServerMessage("The trebuchet may not be winched.", (byte)3);
/* 747 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 751 */     int max = 50;
/* 752 */     if (Features.Feature.NEW_PROJECTILES.isEnabled())
/*     */     {
/* 754 */       if (target.getTemplateId() == 936) {
/* 755 */         max = 40;
/* 756 */       } else if (target.getTemplateId() == 445) {
/* 757 */         max = 30;
/*     */       }  } 
/* 759 */     if (target.getWinches() >= max) {
/*     */       
/* 761 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 762 */           .getName() + " is already winched to max.", (byte)3);
/* 763 */       return true;
/*     */     } 
/*     */     
/* 766 */     int skillNum = 10077;
/* 767 */     if (target.getTemplateId() == 936) {
/* 768 */       skillNum = 10093;
/* 769 */     } else if (target.getTemplateId() == 937) {
/* 770 */       skillNum = 10094;
/* 771 */     }  double speedModifier = 1.0D - performer.getSkills().getSkillOrLearn(skillNum).getKnowledge(0.0D) / 300.0D;
/*     */ 
/*     */     
/* 774 */     if (!mayManouvre(performer, target)) {
/* 775 */       return true;
/*     */     }
/* 777 */     int nums = 1;
/* 778 */     if (action == 238) {
/*     */       
/* 780 */       nums = 5;
/*     */     }
/* 782 */     else if (action == 239) {
/*     */       
/* 784 */       nums = 10;
/*     */     } 
/* 786 */     boolean done = (counter >= 2.0F && counter >= (int)Math.round(nums * speedModifier));
/* 787 */     if (counter == 1.0F) {
/*     */       
/* 789 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 790 */           .getName() + " creaks as you put strain on the ropes.");
/* 791 */       if (!done)
/* 792 */         performer.sendActionControl("Winding", true, (int)Math.round((nums * 10) * speedModifier)); 
/*     */     } 
/* 794 */     if (done) {
/*     */       
/* 796 */       target.setWinches((short)Math.min(max, target.getWinches() + nums));
/*     */       
/* 798 */       float angle = 45.0F + (target.getAuxData() * 5);
/* 799 */       float zHeight = target.getTemplate().getSizeY() * 0.75F / 100.0F;
/* 800 */       float approxDistance = ServerProjectile.getProjectileDistance(target.getPos3f(), zHeight, target
/* 801 */           .getWinches(), (float)((target.getRotation() - 90.0F) * Math.PI / 180.0D), (float)(angle * Math.PI / 180.0D));
/*     */       
/* 803 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 804 */           .getName() + " will now " + (
/* 805 */           (target.getTemplateId() == 936) ? "shoot" : "throw") + " approximately " + 
/* 806 */           Math.round(approxDistance / 4.0F) + " tiles with " + (
/* 807 */           (target.getTemplateId() == 937) ? ("a loaded weight of " + target.getWinches()) : (target.getWinches() + " winches")) + " and an angle of around " + angle + " degrees.");
/*     */ 
/*     */       
/* 810 */       Server.getInstance().broadCastAction(performer.getName() + " winches the ropes on the " + target
/* 811 */           .getName() + ".", performer, 5);
/*     */       
/* 813 */       if (performer.getVisionArea() != null)
/*     */       {
/* 815 */         performer.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*     */       }
/*     */     } 
/* 818 */     performer.getStatus().modifyStamina(-200.0F);
/* 819 */     return done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void unloadCatapult(Creature performer, Item target) {
/* 828 */     boolean isLoaded = (target.getData() > 0L);
/*     */     
/* 830 */     if (!isLoaded) {
/*     */       return;
/*     */     }
/* 833 */     if (performer.getInventory().mayCreatureInsertItem()) {
/*     */ 
/*     */       
/*     */       try {
/* 837 */         Item it = Items.getItem(target.getData());
/*     */         
/* 839 */         if (!performer.canCarry(it.getWeightGrams())) {
/*     */           
/* 841 */           performer.getCommunicator().sendNormalServerMessage("You are carrying too much to pick up the item that you're trying to unload.");
/*     */           
/*     */           return;
/*     */         } 
/* 845 */         if (!it.isBanked() && it.getOwnerId() == -10L) {
/* 846 */           performer.getInventory().insertItem(it, true);
/*     */         } else {
/*     */           
/* 849 */           performer.getCommunicator().sendNormalServerMessage("The " + it
/* 850 */               .getName() + " crumbles to dust.");
/*     */         }
/*     */       
/* 853 */       } catch (NoSuchItemException nsi) {
/*     */         
/* 855 */         performer.getCommunicator().sendNormalServerMessage("Only scrap was found.", (byte)3);
/*     */       } 
/* 857 */       target.setData(0L);
/* 858 */       if (performer.getVisionArea() != null)
/*     */       {
/* 860 */         performer.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*     */       }
/*     */       
/* 863 */       performer.getCommunicator().sendNormalServerMessage("You unload the " + target.getName() + ".");
/*     */       
/* 865 */       Server.getInstance().broadCastAction(performer.getName() + " unloads the " + target.getName() + ".", performer, 5);
/*     */     } else {
/*     */       
/* 868 */       performer.getCommunicator().sendNormalServerMessage("Your inventory is full.", (byte)3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean loadCatapult(Creature performer, Action act, Item source, Item target, short action, float counter) {
/* 879 */     boolean isLoaded = (target.getData() > 0L);
/* 880 */     if (isLoaded) {
/*     */       
/* 882 */       performer.getCommunicator().sendNormalServerMessage(
/* 883 */           StringUtil.format("Something is already loaded into the %s.", new Object[] { target.getName() }), (byte)3);
/* 884 */       return true;
/*     */     } 
/*     */     
/* 887 */     if (target.getTemplateId() == 936)
/*     */     {
/* 889 */       if (source.getTemplateId() != 932) {
/*     */         
/* 891 */         performer.getCommunicator().sendNormalServerMessage("You need to use ballista darts.", (byte)3);
/* 892 */         return true;
/*     */       } 
/*     */     }
/*     */     
/* 896 */     List<Item> items = new ArrayList<>(Arrays.asList(source.getAllItems(true)));
/* 897 */     items.add(source);
/*     */     
/* 899 */     for (Item item : items) {
/*     */       
/* 901 */       boolean mayLoad = false;
/*     */       
/* 903 */       if (isItemLoadable(item))
/*     */       {
/* 905 */         if (item.getOwnerId() == performer.getWurmId()) {
/* 906 */           mayLoad = true;
/*     */         }
/*     */       }
/* 909 */       if (!mayLoad) {
/*     */         
/* 911 */         performer.getCommunicator().sendNormalServerMessage(StringUtil.format("The %s may not be loaded into the %s.", new Object[] { item.getName(), target.getName() }), (byte)3);
/* 912 */         return true;
/*     */       } 
/*     */       
/* 915 */       if (item.isTraded() || item.getOwnerId() != performer.getWurmId()) {
/*     */         
/* 917 */         performer.getCommunicator().sendNormalServerMessage("You cannot load that item now.", (byte)3);
/* 918 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 922 */     if ((source.getSizeX() < 50 && source.getSizeY() < 50 && source.getSizeZ() < 50) || (source
/* 923 */       .isCorpse() && source.getWeightGrams() < 100000)) {
/*     */       
/* 925 */       double skillModifier = 1.0D - performer.getSkills().getSkillOrLearn(10094).getKnowledge(0.0D) / 300.0D;
/* 926 */       boolean done = ((counter >= 2.0F && counter >= 8.0D * skillModifier) || target.getTemplateId() != 937);
/* 927 */       if (counter == 1.0F && !done) {
/*     */         
/* 929 */         performer.getCommunicator().sendNormalServerMessage("You start to load the " + target.getName() + " with " + source.getNameWithGenus() + ".");
/* 930 */         performer.sendActionControl(Actions.actionEntrys[233].getActionString(), true, (int)Math.round(80.0D * skillModifier));
/*     */       } 
/*     */       
/* 933 */       if (!done) {
/* 934 */         return false;
/*     */       }
/* 936 */       target.setData(source.getWurmId());
/* 937 */       performer.getCommunicator().sendNormalServerMessage("You load the " + target
/* 938 */           .getName() + " with " + source.getNameWithGenus() + ".");
/*     */       
/* 940 */       Server.getInstance().broadCastAction(performer
/* 941 */           .getName() + " loads the " + target.getName() + " with " + source
/* 942 */           .getNameWithGenus() + ".", performer, 5);
/* 943 */       source.putInVoid();
/*     */       
/* 945 */       if (target.getTemplateId() == 937) {
/*     */         
/* 947 */         int weight = 0;
/* 948 */         for (Item i : target.getAllItems(true))
/* 949 */           weight += i.getWeightGrams(); 
/* 950 */         target.setWinches((short)(byte)Math.min(50, weight / 20000));
/*     */       } 
/*     */       
/* 953 */       if (performer.getVisionArea() != null)
/*     */       {
/* 955 */         performer.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*     */       }
/*     */     } else {
/*     */       
/* 959 */       performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " will not fit.", (byte)3);
/*     */     } 
/* 961 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isItemLoadable(Item item) {
/* 971 */     return (item != null && 
/* 972 */       !item.isLiquid() && item
/* 973 */       .canBeDropped(true) && (
/* 974 */       !item.isBodyPart() || item.getAuxData() == 100) && 
/* 975 */       !item.isArtifact() && 
/* 976 */       !item.isComponentItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isLoaded(Item warmachine) {
/* 981 */     return (warmachine.isWarmachine() && warmachine.getData() > 0L);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WarmachineBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */