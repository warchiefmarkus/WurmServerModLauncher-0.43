/*      */ package com.wurmonline.server.combat;
/*      */ 
/*      */ import com.wurmonline.math.Vector2f;
/*      */ import com.wurmonline.math.Vector3f;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.behaviours.MethodsItems;
/*      */ import com.wurmonline.server.behaviours.MethodsStructure;
/*      */ import com.wurmonline.server.bodys.TempWound;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.effects.EffectFactory;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VirtualZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ 
/*      */ 
/*      */ public class ServerProjectile
/*      */   implements MiscConstants
/*      */ {
/*   80 */   private static final Logger logger = Logger.getLogger(ServerProjectile.class.getName());
/*      */   public static final float meterPerSecond = 12.0F;
/*      */   private static final float gravity = 0.04F;
/*      */   private static final float newGravity = -9.8F;
/*      */   public static final int TICKS_PER_SECOND = 24;
/*      */   private final float posDownX;
/*      */   private final float posDownY;
/*      */   private final Item projectile;
/*   88 */   private float currentSecondsInAir = 0.0F;
/*   89 */   private long timeAtLanding = 0L;
/*      */   private final Creature shooter;
/*      */   private final Item weapon;
/*      */   private final byte rarity;
/*   93 */   private float damageDealth = 0.0F;
/*   94 */   private BlockingResult result = null;
/*   95 */   private static final CopyOnWriteArraySet<ServerProjectile> projectiles = new CopyOnWriteArraySet<>();
/*      */   
/*   97 */   private ProjectileInfo projectileInfo = null;
/*      */   
/*      */   boolean sentEffect = false;
/*      */ 
/*      */   
/*      */   public ServerProjectile(Item aWeapon, Item aProjectile, float aPosDownX, float aPosDownY, Creature aShooter, byte actionRarity, float damDealt) throws NoSuchZoneException {
/*  103 */     this.weapon = aWeapon;
/*  104 */     this.projectile = aProjectile;
/*  105 */     this.posDownX = aPosDownX;
/*  106 */     this.posDownY = aPosDownY;
/*  107 */     this.shooter = aShooter;
/*  108 */     setDamageDealt(damDealt);
/*  109 */     this.rarity = actionRarity;
/*  110 */     projectiles.add(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean fire(boolean isOnSurface) throws NoSuchZoneException {
/*  115 */     if (Features.Feature.NEW_PROJECTILES.isEnabled() && this.weapon.getTemplateId() != 936) {
/*      */       
/*  117 */       float firingAngle = 45.0F + (this.weapon.getAuxData() * 5);
/*  118 */       if (this.weapon.getTemplateId() == 936) {
/*  119 */         firingAngle = 7.5F;
/*      */       }
/*  121 */       ProjectileInfo projectileInfo = getProjectileInfo(this.weapon, this.shooter, firingAngle, 15.0F);
/*  122 */       this.projectileInfo = projectileInfo;
/*      */       
/*  124 */       VolaTile t = Zones.getOrCreateTile((int)(projectileInfo.endPosition.x / 4.0F), (int)(projectileInfo.endPosition.y / 4.0F), this.weapon
/*  125 */           .isOnSurface());
/*  126 */       Village v = Villages.getVillage((int)(projectileInfo.endPosition.x / 4.0F), (int)(projectileInfo.endPosition.y / 4.0F), this.weapon
/*  127 */           .isOnSurface());
/*  128 */       if (!isOkToAttack(t, getShooter(), getDamageDealt())) {
/*      */         
/*  130 */         boolean ok = false;
/*  131 */         if (v != null)
/*      */         {
/*  133 */           if (v.isActionAllowed((short)174, getShooter(), false, 0, 0) || v
/*  134 */             .isEnemy(getShooter()))
/*      */           {
/*  136 */             ok = true;
/*      */           }
/*      */         }
/*      */         
/*  140 */         if (!ok) {
/*      */           
/*  142 */           this.shooter.getCommunicator().sendNormalServerMessage("You cannot fire the " + getProjectile().getName() + " to there, you are not allowed.");
/*  143 */           return false;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  148 */       Skill firingSkill = null;
/*  149 */       int skillType = 10077;
/*  150 */       if (this.weapon.getTemplateId() == 936) {
/*  151 */         skillType = 10093;
/*  152 */       } else if (this.weapon.getTemplateId() == 937) {
/*  153 */         skillType = 10094;
/*  154 */       }  firingSkill = this.shooter.getSkills().getSkillOrLearn(skillType);
/*  155 */       firingSkill.skillCheck(this.weapon.getWinches(), 0.0D, false, this.weapon.getWinches() / 5.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  160 */       this.weapon.setData(0L);
/*  161 */       this.weapon.setWinches((short)0);
/*  162 */       if (this.weapon.getTemplateId() == 937) {
/*      */         
/*  164 */         int weight = 0;
/*  165 */         for (Item i : this.weapon.getAllItems(true))
/*  166 */           weight += i.getWeightGrams(); 
/*  167 */         this.weapon.setWinches((short)(byte)Math.min(50, weight / 20000));
/*      */       } 
/*      */       
/*  170 */       this.timeAtLanding = System.currentTimeMillis() + projectileInfo.timeToImpact;
/*  171 */       VolaTile startTile = Zones.getOrCreateTile((int)(projectileInfo.startPosition.x / 4.0F), (int)(projectileInfo.startPosition.y / 4.0F), isOnSurface);
/*  172 */       startTile.sendNewProjectile(getProjectile().getWurmId(), (byte)2, getProjectile().getModelName(), 
/*  173 */           getProjectile().getName(), getProjectile().getMaterial(), projectileInfo.startPosition, projectileInfo.startVelocity, projectileInfo.endPosition, this.weapon
/*  174 */           .getRotation(), this.weapon.isOnSurface());
/*  175 */       VolaTile endTile = Zones.getOrCreateTile((int)(projectileInfo.endPosition.x / 4.0F), (int)(projectileInfo.endPosition.y / 4.0F), isOnSurface);
/*  176 */       endTile.sendNewProjectile(getProjectile().getWurmId(), (byte)2, getProjectile().getModelName(), 
/*  177 */           getProjectile().getName(), getProjectile().getMaterial(), projectileInfo.startPosition, projectileInfo.startVelocity, projectileInfo.endPosition, this.weapon
/*  178 */           .getRotation(), this.weapon.isOnSurface());
/*      */       
/*  180 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  184 */     float targetZ = Zones.calculateHeight(this.posDownX, this.posDownY, isOnSurface);
/*  185 */     this.result = calculateBlocker(this.weapon, this.posDownX, this.posDownY, targetZ);
/*  186 */     if (this.result == null) {
/*      */       
/*  188 */       logger.log(Level.INFO, "Blocker is null");
/*  189 */       return false;
/*      */     } 
/*  191 */     if (this.result.getFirstBlocker() != null) {
/*      */       
/*  193 */       float newx = (this.result.getFirstBlocker().getTileX() * 4 + 2);
/*  194 */       float newy = (this.result.getFirstBlocker().getTileY() * 4 + 2);
/*  195 */       Vector2f targPos = new Vector2f((this.weapon.getTileX() * 4 + 2), (this.weapon.getTileY() * 4 + 2));
/*  196 */       Vector2f projPos = new Vector2f(newx, newy);
/*  197 */       float dist = projPos.subtract(targPos).length() / 4.0F;
/*  198 */       if (dist < 8.0F) {
/*      */         
/*  200 */         if (this.shooter.getPower() > 0 && Servers.isThisATestServer())
/*      */         {
/*  202 */           this.shooter.getCommunicator().sendNormalServerMessage("Calculated block from " + this.weapon
/*  203 */               .getPosX() + "," + this.weapon.getPosY() + " dist:" + dist + " at " + newx + "," + newy + ".");
/*      */         }
/*      */         
/*  206 */         this.shooter.getCommunicator().sendNormalServerMessage(" You cannot fire at such a short range.");
/*      */         
/*  208 */         return false;
/*      */       } 
/*  210 */       this.weapon.setData(0L);
/*  211 */       this.weapon.setWinches((short)0);
/*  212 */       setTimeAtLanding(System.currentTimeMillis() + (long)(this.result.getActualBlockingTime() * 1000.0F));
/*  213 */       VolaTile tile = Zones.getOrCreateTile(this.weapon.getTileX(), this.weapon.getTileY(), isOnSurface);
/*  214 */       tile.sendProjectile(getProjectile().getWurmId(), 
/*  215 */           (this.weapon.getTemplateId() == 936) ? 9 : 2, 
/*  216 */           getProjectile()
/*  217 */           .getModelName(), getProjectile().getName(), getProjectile().getMaterial(), this.weapon.getPosX(), this.weapon
/*  218 */           .getPosY(), this.weapon.getPosZ(), this.weapon.getRotation(), (byte)0, projPos.x, projPos.y, targetZ, this.weapon
/*  219 */           .getWurmId(), -10L, this.result.getEstimatedBlockingTime(), this.result.getActualBlockingTime());
/*  220 */       tile = Zones.getOrCreateTile((int)(projPos.x / 4.0F), (int)(projPos.y / 4.0F), true);
/*  221 */       tile.sendProjectile(getProjectile().getWurmId(), 
/*  222 */           (this.weapon.getTemplateId() == 936) ? 9 : 2, 
/*  223 */           getProjectile().getModelName(), getProjectile().getName(), 
/*  224 */           getProjectile().getMaterial(), this.weapon.getPosX(), this.weapon.getPosY(), this.weapon.getPosZ(), this.weapon
/*  225 */           .getRotation(), (byte)0, projPos.x, projPos.y, targetZ, this.weapon
/*  226 */           .getWurmId(), -10L, this.result.getEstimatedBlockingTime(), this.result.getActualBlockingTime());
/*  227 */       if (this.shooter.getPower() >= 5) {
/*  228 */         this.shooter.getCommunicator().sendNormalServerMessage("You hit tile (" + this.result
/*  229 */             .getFirstBlocker().getTileX() + "," + this.result.getFirstBlocker().getTileY() + "), distance: " + dist + ".");
/*      */       }
/*      */       
/*  232 */       if (this.weapon.getTemplateId() == 937)
/*  233 */         this.weapon.setLastMaintained(WurmCalendar.currentTime); 
/*  234 */       return true;
/*      */     } 
/*      */     
/*  237 */     logger.log(Level.INFO, "No blocker");
/*  238 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTimeAtLanding() {
/*  249 */     return this.timeAtLanding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimeAtLanding(long aTimeAtLanding) {
/*  260 */     this.timeAtLanding = aTimeAtLanding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void clear() {
/*  268 */     for (ServerProjectile projectile : projectiles)
/*      */     {
/*  270 */       projectile.poll(Long.MAX_VALUE);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isOkToAttack(VolaTile t, Creature performer, float damdealt) {
/*  277 */     boolean ok = true;
/*  278 */     Village v = t.getVillage();
/*  279 */     if (v != null)
/*      */     {
/*  281 */       if (performer.isFriendlyKingdom(v.kingdom))
/*      */       {
/*      */         
/*  284 */         if (v.isActionAllowed((short)174, performer, false, 0, 0)) {
/*      */           
/*  286 */           ok = true;
/*      */         }
/*  288 */         else if (!v.isEnemy(performer)) {
/*      */           
/*  290 */           performer.setUnmotivatedAttacker();
/*  291 */           ok = false;
/*      */ 
/*      */ 
/*      */           
/*  295 */           if (t.isInPvPZone()) {
/*      */             
/*  297 */             v.modifyReputation(performer.getWurmId(), -20, false);
/*  298 */             if (performer.getKingdomTemplateId() != 3) {
/*      */               
/*  300 */               performer.setReputation(performer.getReputation() - 30);
/*  301 */               performer.getCommunicator().sendAlertServerMessage("This is bad for your reputation.");
/*      */               
/*  303 */               if (performer.getDeity() != null && 
/*  304 */                 !performer.getDeity().isLibila())
/*      */               {
/*  306 */                 if (Server.rand.nextInt(Math.max(1, (int)performer.getFaith())) < 5) {
/*      */                   
/*  308 */                   performer
/*  309 */                     .getCommunicator()
/*  310 */                     .sendNormalServerMessage(
/*  311 */                       (performer.getDeity()).name + " has noticed you and is upset at your behaviour!");
/*      */                   
/*  313 */                   performer.modifyFaith(-0.25F);
/*  314 */                   performer.maybeModifyAlignment(-1.0F);
/*      */                 } 
/*      */               }
/*      */             } else {
/*      */               
/*  319 */               ok = true;
/*      */             } 
/*      */           } 
/*      */         }  } 
/*      */     }
/*  324 */     Structure structure = t.getStructure();
/*  325 */     if (structure != null && structure.isTypeBridge())
/*  326 */       ok = true; 
/*  327 */     if (structure != null && structure.isTypeHouse())
/*      */     {
/*  329 */       if (damdealt > 0.0F) {
/*      */         
/*  331 */         if (v != null && v.isEnemy(performer))
/*  332 */           ok = true; 
/*  333 */         if (performer.getKingdomTemplateId() != 3) {
/*      */           
/*  335 */           byte ownerkingdom = Players.getInstance().getKingdomForPlayer(structure
/*  336 */               .getOwnerId());
/*  337 */           if (performer.isFriendlyKingdom(ownerkingdom)) {
/*      */             
/*  339 */             ok = false;
/*      */ 
/*      */ 
/*      */             
/*  343 */             boolean found = false;
/*  344 */             if (!t.isInPvPZone() || v != null)
/*      */             {
/*  346 */               if (structure.isFinished() && structure.isLocked())
/*      */               {
/*  348 */                 found = (structure.mayModify(performer) || t.isInPvPZone());
/*      */               }
/*      */             }
/*  351 */             if (found)
/*  352 */               ok = true; 
/*  353 */             if (!found) {
/*      */               
/*  355 */               performer.setUnmotivatedAttacker();
/*  356 */               if (t.isInPvPZone()) {
/*  357 */                 ok = true;
/*      */               }
/*      */             } 
/*      */           } else {
/*  361 */             ok = true;
/*      */           } 
/*  363 */           if (structure.mayModify(performer)) {
/*  364 */             ok = true;
/*      */           }
/*      */         } else {
/*  367 */           ok = true;
/*      */         } 
/*      */       } 
/*      */     }
/*  371 */     return ok;
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
/*      */   public static final boolean setEffects(Item weapon, Item projectile, int newx, int newy, float dist, int floorLevelDown, Creature performer, byte rarity, float damdealt) {
/*      */     try {
/*  391 */       Zones.getZone(newx, newy, weapon.isOnSurface());
/*  392 */       VolaTile t = Zones.getOrCreateTile(newx, newy, weapon.isOnSurface());
/*  393 */       String whatishit = "the ground";
/*  394 */       boolean hit = false;
/*  395 */       boolean ok = isOkToAttack(t, performer, damdealt);
/*  396 */       double pwr = 0.0D;
/*  397 */       int floorLevel = 0;
/*  398 */       Structure structure = t.getStructure();
/*  399 */       Skill cataskill = null;
/*  400 */       boolean doneSkillRoll = false;
/*  401 */       boolean arrowStuck = false;
/*  402 */       int skilltype = 10077;
/*  403 */       if (weapon.getTemplateId() == 936)
/*  404 */         skilltype = 10093; 
/*  405 */       if (weapon.getTemplateId() == 937) {
/*  406 */         skilltype = 10094;
/*      */       }
/*      */       try {
/*  409 */         cataskill = performer.getSkills().getSkill(skilltype);
/*      */       }
/*  411 */       catch (NoSuchSkillException nss) {
/*      */         
/*  413 */         cataskill = performer.getSkills().learn(skilltype, 1.0F);
/*      */       } 
/*  415 */       if (structure != null && structure.isTypeHouse()) {
/*      */         
/*  417 */         hit = true;
/*  418 */         whatishit = structure.getName();
/*  419 */         if (!t.isInPvPZone() && !ok)
/*      */         {
/*  421 */           if (performer.getKingdomTemplateId() != 3) {
/*      */             
/*  423 */             byte ownerkingdom = Players.getInstance().getKingdomForPlayer(structure
/*  424 */                 .getOwnerId());
/*  425 */             if (performer.isFriendlyKingdom(ownerkingdom)) {
/*      */               
/*  427 */               damdealt = 0.0F;
/*  428 */               hit = false;
/*      */             } 
/*      */           } 
/*      */         }
/*  432 */         if (hit && !doneSkillRoll) {
/*      */           
/*  434 */           pwr = cataskill.skillCheck(dist - 9.0D, weapon, 0.0D, false, 10.0F);
/*  435 */           doneSkillRoll = true;
/*      */         } 
/*  437 */         if (pwr > 0.0D) {
/*      */           
/*  439 */           if (damdealt > 0.0F) {
/*      */             
/*  441 */             int destroyed = 0;
/*  442 */             Floor f = t.getTopFloor();
/*  443 */             if (f != null)
/*      */             {
/*  445 */               floorLevel = f.getFloorLevel();
/*      */             }
/*  447 */             Wall w = t.getTopWall();
/*  448 */             if (w != null)
/*      */             {
/*  450 */               if (w.getFloorLevel() > floorLevel)
/*  451 */                 floorLevel = w.getFloorLevel(); 
/*      */             }
/*  453 */             Fence fence = t.getTopFence();
/*  454 */             if (fence != null)
/*      */             {
/*  456 */               if (fence.getFloorLevel() > floorLevel) {
/*  457 */                 floorLevel = fence.getFloorLevel();
/*      */               }
/*      */             }
/*  460 */             boolean logged = false;
/*  461 */             float mod = 2.0F;
/*  462 */             if (floorLevel > 0) {
/*      */               
/*  464 */               Floor[] arrayOfFloor = t.getFloors(floorLevel * 30, floorLevel * 30);
/*      */               
/*  466 */               if (arrayOfFloor.length > 0)
/*      */               {
/*  468 */                 for (int j = 0; j < arrayOfFloor.length; j++) {
/*      */ 
/*      */                   
/*  471 */                   float newdam = arrayOfFloor[j].getDamage() + Math.min(20.0F, arrayOfFloor[j].getDamageModifier() * damdealt / 2.0F);
/*      */                   
/*  473 */                   if (newdam >= 100.0F) {
/*      */                     
/*  475 */                     if (!logged) {
/*      */                       
/*  477 */                       logged = true;
/*  478 */                       TileEvent.log(arrayOfFloor[j].getTileX(), arrayOfFloor[j].getTileY(), 0, performer
/*      */                           
/*  480 */                           .getWurmId(), 236);
/*      */                     } 
/*  482 */                     destroyed++;
/*      */                   } 
/*  484 */                   if (arrayOfFloor[j].setDamage(newdam))
/*  485 */                     arrayOfFloor[j].getTile().removeFloor(arrayOfFloor[j]); 
/*  486 */                   arrowStuck = true;
/*      */                 } 
/*      */               }
/*      */             } 
/*  490 */             Wall[] warr = t.getWalls();
/*  491 */             for (int x = 0; x < warr.length; x++) {
/*      */               
/*  493 */               if (warr[x].getFloorLevel() == floorLevel) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  499 */                 float newdam = warr[x].getDamage() + Math.min(warr[x].isFinished() ? 20.0F : 100.0F, warr[x].getDamageModifier() * damdealt / 2.0F);
/*  500 */                 if (newdam >= 100.0F) {
/*      */                   
/*  502 */                   if (!logged) {
/*      */                     
/*  504 */                     logged = true;
/*  505 */                     TileEvent.log(warr[x].getTileX(), warr[x].getTileY(), 0, performer
/*  506 */                         .getWurmId(), 236);
/*      */                   } 
/*  508 */                   destroyed++;
/*      */                 } 
/*  510 */                 warr[x].setDamage(newdam);
/*  511 */                 arrowStuck = true;
/*      */               } 
/*      */             } 
/*  514 */             Floor[] floors = t.getFloors();
/*  515 */             for (int i = 0; i < floors.length; i++) {
/*      */               
/*  517 */               if (floors[i].getFloorLevel() == floorLevel) {
/*      */ 
/*      */                 
/*  520 */                 float newdam = floors[i].getDamage() + Math.min(20.0F, floors[i].getDamageModifier() * damdealt / 2.0F);
/*  521 */                 if (newdam >= 100.0F) {
/*      */                   
/*  523 */                   if (!logged) {
/*      */                     
/*  525 */                     logged = true;
/*  526 */                     TileEvent.log(floors[i].getTileX(), floors[i].getTileY(), 0, performer
/*  527 */                         .getWurmId(), 236);
/*      */                   } 
/*  529 */                   destroyed++;
/*      */                 } 
/*  531 */                 floors[i].setDamage(newdam);
/*  532 */                 arrowStuck = true;
/*      */               } 
/*      */             } 
/*  535 */             if (destroyed > 0)
/*      */             {
/*  537 */               if (!ok) {
/*      */                 
/*  539 */                 performer.getCommunicator().sendNormalServerMessage("You feel very bad about this.");
/*      */ 
/*      */                 
/*  542 */                 performer.maybeModifyAlignment(-5.0F);
/*  543 */                 performer.punishSkills(0.1D * Math.min(3, destroyed), false);
/*      */               } 
/*      */             }
/*  546 */             alertGuards(performer, newx, newy, ok, t, destroyed);
/*      */           } 
/*  548 */           if (damdealt > 0.0F) {
/*  549 */             performer.getCommunicator().sendNormalServerMessage("You seem to have hit " + t
/*  550 */                 .getStructure().getName() + "!" + (
/*  551 */                 Servers.isThisATestServer() ? (" Dealt:" + damdealt) : ""));
/*      */           } else {
/*  553 */             performer.getCommunicator().sendNormalServerMessage("You seem to have hit " + t
/*  554 */                 .getStructure().getName() + " but luckily it took no damage!");
/*      */           } 
/*      */         } else {
/*      */           
/*  558 */           performer.getCommunicator().sendNormalServerMessage("You just missed " + t
/*  559 */               .getStructure().getName() + ".");
/*  560 */         }  if (t.getStructure() == null)
/*      */         {
/*      */ 
/*      */           
/*  564 */           performer.achievement(51);
/*      */         }
/*      */       } 
/*  567 */       if (structure != null && structure.isTypeBridge()) {
/*      */         
/*  569 */         hit = true;
/*  570 */         whatishit = structure.getName();
/*  571 */         if (hit && !doneSkillRoll) {
/*      */           
/*  573 */           pwr = cataskill.skillCheck(dist - 9.0D, weapon, 0.0D, false, 10.0F);
/*  574 */           doneSkillRoll = true;
/*      */         } 
/*  576 */         if (pwr > 0.0D) {
/*      */           
/*  578 */           if (damdealt > 0.0F)
/*      */           {
/*      */             
/*  581 */             for (BridgePart bp : t.getBridgeParts()) {
/*      */               
/*  583 */               float mod = bp.getModByMaterial();
/*      */               
/*  585 */               float newdam = bp.getDamage() + Math.min(20.0F, bp.getDamageModifier() * damdealt / mod);
/*  586 */               TileEvent.log(bp.getTileX(), bp.getTileY(), 0, performer
/*  587 */                   .getWurmId(), 236);
/*  588 */               bp.setDamage(newdam);
/*      */             } 
/*      */           }
/*  591 */           if (damdealt > 0.0F) {
/*  592 */             performer.getCommunicator().sendNormalServerMessage("You seem to have hit " + t
/*  593 */                 .getStructure().getName() + "!" + (
/*  594 */                 Servers.isThisATestServer() ? (" Dealt:" + damdealt) : ""));
/*      */           } else {
/*  596 */             performer.getCommunicator().sendNormalServerMessage("You seem to have hit " + t
/*  597 */                 .getStructure().getName() + " but luckily it took no damage!");
/*      */           } 
/*      */         } else {
/*      */           
/*  601 */           performer.getCommunicator().sendNormalServerMessage("You just missed " + t
/*  602 */               .getStructure().getName() + ".");
/*      */         } 
/*  604 */       }  for (Fence fence : t.getFencesForLevel(floorLevel)) {
/*      */ 
/*      */         
/*  607 */         hit = true;
/*  608 */         whatishit = "the " + fence.getName();
/*  609 */         Village vill = MethodsStructure.getVillageForFence(fence);
/*      */         
/*  611 */         if (!ok && vill != null)
/*      */         {
/*      */ 
/*      */           
/*  615 */           if (vill.isActionAllowed((short)174, performer, false, 0, 0)) {
/*      */             
/*  617 */             ok = true;
/*      */           }
/*  619 */           else if (!vill.isEnemy(performer)) {
/*      */             
/*  621 */             hit = false;
/*  622 */             damdealt = 0.0F;
/*      */           } 
/*      */         }
/*      */         
/*  626 */         if (hit && !doneSkillRoll) {
/*      */           
/*  628 */           pwr = cataskill.skillCheck(dist - 9.0D, weapon, 0.0D, false, 10.0F);
/*  629 */           doneSkillRoll = true;
/*      */         } 
/*  631 */         if (pwr > 0.0D) {
/*      */           
/*  633 */           if (damdealt > 0.0F) {
/*      */             
/*  635 */             float mod = 2.0F;
/*      */ 
/*      */             
/*  638 */             TileEvent.log(fence.getTileX(), fence.getTileY(), 0, performer.getWurmId(), 236);
/*      */             
/*  640 */             fence.setDamage(fence.getDamage() + 
/*  641 */                 Math.min(fence.isFinished() ? 20.0F : 100.0F, fence.getDamageModifier() * damdealt / 2.0F));
/*  642 */             performer.getCommunicator().sendNormalServerMessage("You seem to have hit " + whatishit + "!");
/*      */             
/*  644 */             arrowStuck = true;
/*      */           } 
/*      */         } else {
/*      */           
/*  648 */           performer.getCommunicator().sendNormalServerMessage("You just missed some fences with the " + projectile
/*  649 */               .getName() + ".");
/*      */         } 
/*  651 */       }  VolaTile southTile = Zones.getTileOrNull(newx, newy + 1, true);
/*  652 */       if (southTile != null)
/*  653 */         for (Fence fence : southTile.getFencesForLevel(floorLevel)) {
/*      */           
/*  655 */           if (fence.isHorizontal()) {
/*      */             
/*  657 */             hit = true;
/*  658 */             whatishit = "the " + fence.getName();
/*  659 */             Village vill = MethodsStructure.getVillageForFence(fence);
/*      */             
/*  661 */             if (!ok && vill != null)
/*      */             {
/*      */ 
/*      */ 
/*      */               
/*  666 */               if (vill.isActionAllowed((short)174, performer, false, 0, 0)) {
/*      */                 
/*  668 */                 ok = true;
/*      */               }
/*  670 */               else if (!vill.isEnemy(performer)) {
/*      */                 
/*  672 */                 hit = false;
/*  673 */                 damdealt = 0.0F;
/*      */               } 
/*      */             }
/*  676 */             if (hit && !doneSkillRoll) {
/*      */               
/*  678 */               pwr = cataskill.skillCheck(dist - 9.0D, weapon, 0.0D, false, 10.0F);
/*  679 */               doneSkillRoll = true;
/*      */             } 
/*  681 */             if (pwr > 0.0D) {
/*      */               
/*  683 */               if (damdealt > 0.0F) {
/*      */                 
/*  685 */                 float mod = 2.0F;
/*      */ 
/*      */ 
/*      */                 
/*  689 */                 TileEvent.log(fence.getTileX(), fence.getTileY(), 0, performer
/*  690 */                     .getWurmId(), 236);
/*  691 */                 fence.setDamage(fence.getDamage() + 
/*  692 */                     Math.min(20.0F, fence.getDamageModifier() * damdealt / 2.0F));
/*  693 */                 performer.getCommunicator().sendNormalServerMessage("You seem to have hit " + whatishit + "!");
/*      */                 
/*  695 */                 arrowStuck = true;
/*      */               } 
/*      */             } else {
/*      */               
/*  699 */               performer.getCommunicator().sendNormalServerMessage("You just missed some fences with the " + projectile
/*  700 */                   .getName() + ".");
/*      */             } 
/*      */           } 
/*  703 */         }   VolaTile eastTile = Zones.getTileOrNull(newx + 1, newy, true);
/*  704 */       if (eastTile != null)
/*  705 */         for (Fence fence : eastTile.getFencesForLevel(floorLevel)) {
/*      */           
/*  707 */           if (!fence.isHorizontal()) {
/*      */             
/*  709 */             hit = true;
/*  710 */             whatishit = "the " + fence.getName();
/*  711 */             Village vill = MethodsStructure.getVillageForFence(fence);
/*      */             
/*  713 */             if (!ok && vill != null)
/*      */             {
/*      */ 
/*      */ 
/*      */               
/*  718 */               if (vill.isActionAllowed((short)174, performer, false, 0, 0)) {
/*      */                 
/*  720 */                 ok = true;
/*      */               }
/*  722 */               else if (!vill.isEnemy(performer)) {
/*      */                 
/*  724 */                 hit = false;
/*  725 */                 damdealt = 0.0F;
/*      */               } 
/*      */             }
/*      */             
/*  729 */             if (hit && !doneSkillRoll) {
/*      */               
/*  731 */               pwr = cataskill.skillCheck(dist - 9.0D, weapon, 0.0D, false, 10.0F);
/*  732 */               doneSkillRoll = true;
/*      */             } 
/*  734 */             if (pwr > 0.0D) {
/*      */               
/*  736 */               if (damdealt > 0.0F) {
/*      */                 
/*  738 */                 float mod = 2.0F;
/*      */ 
/*      */ 
/*      */                 
/*  742 */                 TileEvent.log(fence.getTileX(), fence.getTileY(), 0, performer
/*  743 */                     .getWurmId(), 236);
/*  744 */                 fence.setDamage(fence.getDamage() + 
/*  745 */                     Math.min(20.0F, fence.getDamageModifier() * damdealt / 2.0F));
/*  746 */                 performer.getCommunicator().sendNormalServerMessage("You seem to have hit " + whatishit + "!");
/*      */                 
/*  748 */                 arrowStuck = true;
/*      */               } 
/*      */             } else {
/*      */               
/*  752 */               performer.getCommunicator().sendNormalServerMessage("You just missed some fences with the " + projectile
/*  753 */                   .getName() + ".");
/*      */             } 
/*      */           } 
/*  756 */         }   if (testHitCreaturesOnTile(t, performer, projectile, damdealt, dist, floorLevel)) {
/*      */         
/*  758 */         if (weapon.getTemplateId() != 936 || !arrowStuck) {
/*      */ 
/*      */           
/*  761 */           if (weapon.getTemplateId() == 936)
/*  762 */             damdealt *= 3.0F; 
/*  763 */           if (!doneSkillRoll) {
/*      */             
/*  765 */             pwr = cataskill.skillCheck(dist - 9.0D, weapon, 0.0D, false, 10.0F);
/*  766 */             doneSkillRoll = true;
/*      */           } 
/*  768 */           boolean hit2 = hitCreaturesOnTile(t, pwr, performer, projectile, damdealt, dist, floorLevel);
/*      */           
/*  770 */           if (!hit && hit2)
/*  771 */             hit = true; 
/*      */         } 
/*  773 */         if (!hit) {
/*  774 */           performer.getCommunicator().sendNormalServerMessage("You hit nothing with the " + projectile
/*  775 */               .getName() + ".");
/*      */         }
/*      */       } 
/*      */       
/*  779 */       t = Zones.getOrCreateTile(newx, newy, weapon.isOnSurface());
/*  780 */       if (projectile.isEgg())
/*      */       {
/*  782 */         t.broadCast("A " + projectile.getName() + " comes flying through the air, hits " + whatishit + ", and shatters.");
/*      */         
/*  784 */         performer.getCommunicator().sendNormalServerMessage("The " + projectile.getName() + " shatters.");
/*  785 */         Items.destroyItem(projectile.getWurmId());
/*      */       }
/*  787 */       else if (projectile.setDamage(projectile.getDamage() + projectile.getDamageModifier() * (20 + Server.rand
/*  788 */           .nextInt(Math.max(1, (int)dist)))))
/*      */       {
/*  790 */         t.broadCast("A " + projectile.getName() + " comes flying through the air, hits " + whatishit + ", and shatters.");
/*      */         
/*  792 */         performer.getCommunicator().sendNormalServerMessage("The " + projectile.getName() + " shatters.");
/*      */       }
/*      */       else
/*      */       {
/*  796 */         t.broadCast("A " + projectile.getName() + " comes flying through the air and hits " + whatishit + ".");
/*      */       }
/*      */     
/*      */     }
/*  800 */     catch (NoSuchZoneException nsz) {
/*      */       
/*  802 */       performer.getCommunicator().sendNormalServerMessage("You hit nothing with the " + projectile
/*  803 */           .getName() + ".");
/*  804 */       Items.destroyItem(weapon.getData());
/*  805 */       return true;
/*      */     } 
/*  807 */     return projectile.deleted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void alertGuards(Creature performer, int newx, int newy, boolean ok, VolaTile t, int destroyed) {
/*      */     try {
/*  815 */       if (!MethodsItems.mayTakeThingsFromStructure(performer, null, newx, newy))
/*      */       {
/*  817 */         if (!ok) {
/*      */           
/*  819 */           Structure struct = t.getStructure();
/*  820 */           if (struct != null && struct.isFinished())
/*      */           {
/*  822 */             for (VirtualZone vz : t.getWatchers()) {
/*      */ 
/*      */               
/*      */               try {
/*  826 */                 if (vz.getWatcher() != null && vz
/*  827 */                   .getWatcher().getCurrentTile() != null)
/*      */                 {
/*  829 */                   if (performer.isFriendlyKingdom(vz
/*  830 */                       .getWatcher().getKingdomId())) {
/*      */                     
/*  832 */                     boolean cares = false;
/*  833 */                     if (vz.getWatcher().isKingdomGuard())
/*  834 */                       cares = true; 
/*  835 */                     if (!cares)
/*      */                     {
/*  837 */                       cares = struct.isGuest(vz.getWatcher());
/*      */                     }
/*  839 */                     if (cares && (
/*  840 */                       Math.abs(
/*  841 */                         (vz.getWatcher().getCurrentTile()).tilex - newx) <= 20 || 
/*  842 */                       Math.abs(
/*  843 */                         (vz.getWatcher().getCurrentTile()).tiley - newy) <= 20))
/*      */                     {
/*      */                       
/*  846 */                       if (cares) if (performer
/*      */                           
/*  848 */                           .getStealSkill()
/*  849 */                           .skillCheck((95 - 
/*      */                             
/*  851 */                             Math.min(
/*  852 */                               Math.abs(
/*      */                                 
/*  854 */                                 (vz.getWatcher().getCurrentTile()).tilex - newx), 
/*      */                               
/*  856 */                               Math.abs(
/*      */                                 
/*  858 */                                 (vz.getWatcher().getCurrentTile()).tiley - newy)) * 5), 0.0D, true, 10.0F) < 0.0D)
/*      */                         {
/*      */ 
/*      */                           
/*  862 */                           if (!Servers.localServer.PVPSERVER || destroyed > 0) {
/*      */ 
/*      */                             
/*  865 */                             performer.setReputation(performer
/*  866 */                                 .getReputation() - 10);
/*      */                             
/*  868 */                             performer
/*  869 */                               .getCommunicator()
/*  870 */                               .sendNormalServerMessage("People notice you. This is bad for your reputation!", (byte)2);
/*      */ 
/*      */                             
/*      */                             break;
/*      */                           } 
/*      */                         }
/*      */                     
/*      */                     }
/*      */                   } 
/*      */                 }
/*  880 */               } catch (Exception e) {
/*      */                 
/*  882 */                 logger.log(Level.WARNING, e.getMessage(), e);
/*      */               }
/*      */             
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*  889 */     } catch (NoSuchStructureException noSuchStructureException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean testHitCreaturesOnTile(VolaTile t, Creature performer, Item projectile, float damdealt, float dist, int floorlevel) {
/*  897 */     boolean hit = false;
/*  898 */     Creature[] initialCreatures = t.getCreatures();
/*  899 */     Set<Creature> creatureSet = new HashSet<>();
/*      */     
/*  901 */     for (Creature c : initialCreatures) {
/*      */       
/*  903 */       if ((t.getBridgeParts()).length > 0) {
/*      */         
/*  905 */         if (c.getBridgeId() == t.getBridgeParts()[0].getStructureId()) {
/*  906 */           creatureSet.add(c);
/*      */         }
/*  908 */       } else if (c.getFloorLevel() == floorlevel) {
/*      */         
/*  910 */         creatureSet.add(c);
/*      */       } 
/*      */     } 
/*      */     
/*  914 */     Creature[] creatures = creatureSet.<Creature>toArray(new Creature[creatureSet.size()]);
/*  915 */     if (creatures.length > 0) {
/*      */       
/*  917 */       boolean nonpvp = false;
/*  918 */       int x = Server.rand.nextInt(creatures.length);
/*  919 */       if (!creatures[x].isUnique() && !creatures[x].isInvulnerable() && creatures[x]
/*  920 */         .getPower() == 0) {
/*      */         
/*  922 */         if (performer.isFriendlyKingdom(creatures[x].getKingdomId()))
/*      */         {
/*  924 */           if (!creatures[x].isOnPvPServer() || 
/*  925 */             !performer.isOnPvPServer())
/*      */           {
/*  927 */             if (performer.getCitizenVillage() == null || 
/*  928 */               !performer.getCitizenVillage().isEnemy(creatures[x]
/*  929 */                 .getCitizenVillage()))
/*  930 */               nonpvp = true; 
/*      */           }
/*      */         }
/*  933 */         if (!nonpvp) {
/*      */           
/*      */           try {
/*      */             
/*  937 */             hit = true;
/*      */           }
/*  939 */           catch (Exception ex) {
/*      */             
/*  941 */             logger.log(Level.WARNING, creatures[x].getName() + ex.getMessage(), ex);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  947 */     return hit;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean hitCreaturesOnTile(VolaTile t, double power, Creature performer, Item projectile, float damdealt, float dist, int floorlevel) {
/*  953 */     boolean hit = false;
/*  954 */     Creature[] initialCreatures = t.getCreatures();
/*  955 */     Set<Creature> creatureSet = new HashSet<>();
/*      */     
/*  957 */     for (Creature c : initialCreatures) {
/*      */       
/*  959 */       if ((t.getBridgeParts()).length > 0) {
/*      */         
/*  961 */         if (c.getBridgeId() == t.getBridgeParts()[0].getStructureId()) {
/*  962 */           creatureSet.add(c);
/*      */         }
/*  964 */       } else if (c.getFloorLevel() == floorlevel) {
/*      */         
/*  966 */         creatureSet.add(c);
/*      */       } 
/*      */     } 
/*      */     
/*  970 */     Creature[] creatures = creatureSet.<Creature>toArray(new Creature[creatureSet.size()]);
/*  971 */     if (power > 0.0D && creatures.length > 0) {
/*      */       
/*  973 */       boolean nonpvp = false;
/*  974 */       int x = Server.rand.nextInt(creatures.length);
/*  975 */       if (!creatures[x].isUnique() && !creatures[x].isInvulnerable() && creatures[x]
/*  976 */         .getPower() == 0) {
/*      */         
/*  978 */         if (performer.isFriendlyKingdom(creatures[x].getKingdomId())) {
/*      */ 
/*      */           
/*  981 */           if ((performer.getCitizenVillage() == null || 
/*  982 */             !performer.getCitizenVillage().isEnemy(creatures[x].getCitizenVillage())) && 
/*  983 */             !performer.hasBeenAttackedBy(creatures[x].getWurmId()) && 
/*  984 */             creatures[x].getCurrentKingdom() == creatures[x].getKingdomId())
/*  985 */             performer.setUnmotivatedAttacker(); 
/*  986 */           if (!creatures[x].isOnPvPServer() || 
/*  987 */             !performer.isOnPvPServer()) {
/*      */             
/*  989 */             if (performer.getCitizenVillage() == null || 
/*  990 */               !performer.getCitizenVillage().isEnemy(creatures[x]
/*  991 */                 .getCitizenVillage())) {
/*  992 */               nonpvp = true;
/*      */             }
/*  994 */           } else if (Servers.localServer.HOMESERVER && 
/*  995 */             !performer.isOnHostileHomeServer()) {
/*      */             
/*  997 */             if (creatures[x].getReputation() >= 0 && ((creatures[x]).citizenVillage == null || 
/*      */               
/*  999 */               !(creatures[x]).citizenVillage.isEnemy(performer))) {
/*      */               
/* 1001 */               performer.setReputation(performer.getReputation() - 30);
/* 1002 */               performer.getCommunicator().sendAlertServerMessage("This is bad for your reputation.");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 1007 */         if (!nonpvp) {
/*      */           try
/*      */           {
/*      */             
/* 1011 */             creatures[x].getCommunicator().sendAlertServerMessage("You are hit by some " + projectile
/* 1012 */                 .getName() + " coming through the air!");
/*      */             
/* 1014 */             if (!creatures[x].isPlayer()) {
/*      */               
/* 1016 */               creatures[x].setTarget(performer.getWurmId(), false);
/* 1017 */               creatures[x].setFleeCounter(20);
/*      */             } 
/* 1019 */             if (damdealt > 0.0F)
/*      */             {
/* 1021 */               if (creatures[x].isPlayer()) {
/*      */                 
/* 1023 */                 boolean dead = creatures[x].addWoundOfType(performer, (byte)0, 1, true, 1.0F, false, 
/* 1024 */                     Math.min(25000.0F, damdealt * 1000.0F), 0.0F, 0.0F, false, false);
/* 1025 */                 performer.achievement(47);
/* 1026 */                 if (dead)
/*      */                 {
/* 1028 */                   creatures[x].achievement(48);
/*      */                 }
/*      */               } else {
/*      */                 
/* 1032 */                 creatures[x].getBody().addWound((Wound)new TempWound((byte)0, creatures[x]
/*      */                       
/* 1034 */                       .getBody().getRandomWoundPos(), Math.min(25000.0F, damdealt * 1000.0F), creatures[x]
/*      */                       
/* 1036 */                       .getWurmId(), 0.0F, 0.0F, false));
/*      */               }  } 
/* 1038 */             hit = true;
/* 1039 */             performer.getCommunicator().sendNormalServerMessage("You hit " + creatures[x]
/* 1040 */                 .getNameWithGenus() + "!");
/*      */           
/*      */           }
/* 1043 */           catch (Exception ex)
/*      */           {
/* 1045 */             logger.log(Level.WARNING, creatures[x].getName() + ex.getMessage(), ex);
/*      */           }
/*      */         
/*      */         }
/*      */       }
/* 1050 */       else if (creatures[x].isVisible()) {
/* 1051 */         performer.getCommunicator().sendNormalServerMessage(creatures[x]
/* 1052 */             .getNameWithGenus() + " dodges your " + projectile.getName() + " with no problem.");
/*      */       } 
/*      */     } 
/* 1055 */     return hit;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final BlockingResult calculateBlocker(Item weapon, float estimatedEndPosX, float estimatedEndPosY, float estimatedPosZ) throws NoSuchZoneException {
/* 1061 */     Vector3f startPos = new Vector3f((weapon.getTileX() * 4 + 2), (weapon.getTileY() * 4 + 2), weapon.getPosZ());
/* 1062 */     Vector3f currentPos = startPos.clone();
/* 1063 */     Vector3f targetPos = new Vector3f(estimatedEndPosX, estimatedEndPosY, estimatedPosZ);
/* 1064 */     Vector3f vector = targetPos.subtract(startPos);
/*      */     
/* 1066 */     float length = vector.length();
/* 1067 */     Vector3f dir = vector.normalize();
/* 1068 */     float totalTimeInAir = length / 12.0F;
/*      */     
/* 1070 */     float speed = 0.5F;
/* 1071 */     float hVelocity = totalTimeInAir * 24.0F * 0.02F;
/* 1072 */     hVelocity += dir.z * 0.5F;
/* 1073 */     boolean hitSomething = false;
/* 1074 */     float stepLength = 2.0F;
/* 1075 */     float secondsPerStep = 0.16666667F;
/* 1076 */     float gravityPerStep = 0.16F;
/*      */     
/* 1078 */     float lastGroundHeight = Zones.calculateHeight(currentPos.getX(), currentPos.getY(), true);
/* 1079 */     BlockingResult toReturn = null;
/* 1080 */     float timeMoved = 0.0F;
/* 1081 */     while (!hitSomething) {
/*      */       
/* 1083 */       timeMoved += 0.16666667F;
/* 1084 */       Vector3f lastPos = currentPos.clone();
/* 1085 */       hVelocity -= 0.16F;
/* 1086 */       currentPos.z += hVelocity;
/* 1087 */       currentPos.x += dir.x * 2.0F;
/* 1088 */       currentPos.y += dir.y * 2.0F;
/*      */ 
/*      */       
/* 1091 */       float groundHeight = Zones.calculateHeight(currentPos.getX(), currentPos.getY(), true);
/* 1092 */       toReturn = Blocking.getBlockerBetween(null, lastPos.getX(), lastPos.getY(), currentPos.getX(), currentPos.getY(), lastPos
/* 1093 */           .getZ(), currentPos.z, true, true, true, 4, -10L, -10L, -10L, false);
/*      */       
/* 1095 */       if (currentPos.getZ() < groundHeight - 1.0F) {
/*      */         
/* 1097 */         toReturn = new BlockingResult();
/* 1098 */         toReturn.addBlocker((Blocker)new PathTile((int)currentPos.getX() / 4, (int)currentPos.getY() / 4, Server.surfaceMesh
/* 1099 */               .getTile((int)currentPos.getX() / 4, (int)currentPos.getY() / 4), true, 0), currentPos, 100.0F);
/*      */ 
/*      */         
/* 1102 */         logger.log(Level.INFO, "Hit ground at " + 
/* 1103 */             (int)(currentPos.getX() / 4.0F) + "," + (int)(currentPos.getY() / 4.0F) + " height was " + groundHeight + ", compared to " + currentPos
/* 1104 */             .getZ());
/* 1105 */         toReturn.setEstimatedBlockingTime(totalTimeInAir);
/* 1106 */         toReturn.setActualBlockingTime(timeMoved);
/* 1107 */         return toReturn;
/*      */       } 
/*      */       
/* 1110 */       if (toReturn != null) {
/*      */         
/* 1112 */         toReturn.setEstimatedBlockingTime(totalTimeInAir);
/* 1113 */         toReturn.setActualBlockingTime(timeMoved);
/* 1114 */         hitSomething = true;
/*      */       } 
/* 1116 */       lastGroundHeight = groundHeight;
/*      */     } 
/* 1118 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float getProjectileDistance(Vector3f startingPosition, float heightOffset, float power, float rotation, float firingAngle) {
/* 1125 */     Vector3f startingVelocity = new Vector3f((float)(power * Math.cos(rotation) * Math.cos(firingAngle)), (float)(power * Math.sin(rotation) * Math.cos(firingAngle)), (float)(power * Math.sin(firingAngle)));
/* 1126 */     float offsetModifier = heightOffset / startingVelocity.z / -9.8F * startingVelocity.z;
/* 1127 */     float flightTime = startingVelocity.z * (2.0F - offsetModifier) / -9.8F;
/* 1128 */     startingVelocity.z = 0.0F;
/* 1129 */     Vector3f endingPosition = startingPosition.add(startingVelocity.mult(flightTime));
/*      */ 
/*      */ 
/*      */     
/* 1133 */     return endingPosition.distance(startingPosition.add(0.0F, 0.0F, heightOffset));
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
/*      */   public static final ProjectileInfo getProjectileInfo(Item weapon, Creature cret, float averageFiringAngle, float firingAngleVariationMax) throws NoSuchZoneException {
/* 1150 */     Vector3f landingPosition = new Vector3f();
/* 1151 */     int power = weapon.getWinches();
/* 1152 */     float angleVar = 1.0F - Math.abs(averageFiringAngle - 45.0F) / 45.0F;
/* 1153 */     float tiltAngle = averageFiringAngle - firingAngleVariationMax * angleVar;
/* 1154 */     float rotation = (float)((weapon.getRotation() - 90.0F) * Math.PI / 180.0D);
/*      */     
/* 1156 */     Skill firingSkill = null;
/* 1157 */     int skillType = 10077;
/* 1158 */     if (weapon.getTemplateId() == 936) {
/*      */       
/* 1160 */       skillType = 10093;
/* 1161 */       power = Math.min(40, power);
/* 1162 */       power = (int)(power * 1.5F);
/*      */     }
/* 1164 */     else if (weapon.getTemplateId() == 937) {
/*      */       
/* 1166 */       skillType = 10094;
/* 1167 */       power = Math.min(50, power);
/*      */     }
/*      */     else {
/*      */       
/* 1171 */       power = Math.min(30, power);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1176 */       firingSkill = cret.getSkills().getSkill(skillType);
/*      */     }
/* 1178 */     catch (NoSuchSkillException nss) {
/*      */       
/* 1180 */       firingSkill = cret.getSkills().learn(skillType, 1.0F);
/*      */     } 
/*      */     
/* 1183 */     double skillModifier = firingSkill.skillCheck(power, 0.0D, true, 1.0F) / 100.0D;
/* 1184 */     float qlModifier = (100.0F - weapon.getCurrentQualityLevel()) / 100.0F;
/* 1185 */     tiltAngle = (float)(tiltAngle + (firingAngleVariationMax * angleVar * qlModifier) * skillModifier);
/*      */     
/* 1187 */     if (skillModifier < 0.0D) {
/*      */       
/* 1189 */       tiltAngle /= 3.0F;
/* 1190 */       power /= 2;
/* 1191 */       cret.getCommunicator().sendNormalServerMessage("Something goes wrong when you fire the " + weapon.getName() + " and it doesn't fire as far as you expected.");
/*      */     } 
/* 1193 */     tiltAngle = (float)(tiltAngle * 0.017453292519943295D);
/*      */ 
/*      */ 
/*      */     
/* 1197 */     Vector3f currentVelocity = new Vector3f((float)(power * Math.cos(rotation) * Math.cos(tiltAngle)), (float)(power * Math.sin(rotation) * Math.cos(tiltAngle)), (float)(power * Math.sin(tiltAngle)));
/* 1198 */     Vector3f currentPos = weapon.getPos3f();
/* 1199 */     currentPos.z += weapon.getTemplate().getSizeY() * 0.75F / 100.0F;
/*      */     
/* 1201 */     Vector3f startingPosition = currentPos.clone();
/* 1202 */     Vector3f startingVelocity = currentVelocity.clone();
/*      */     
/* 1204 */     Vector3f nextPos = null;
/* 1205 */     BlockingResult blocker = null;
/* 1206 */     float stepAmount = 2.0F / currentVelocity.length();
/* 1207 */     float gravityPerStep = -9.8F * stepAmount;
/* 1208 */     long flightTime = 0L;
/*      */     
/* 1210 */     boolean landed = false;
/* 1211 */     while (!landed) {
/*      */       
/* 1213 */       flightTime = (long)((float)flightTime + stepAmount * 1000.0F);
/* 1214 */       nextPos = currentPos.add(currentVelocity.mult(stepAmount));
/*      */       
/* 1216 */       blocker = Blocking.getBlockerBetween(null, currentPos.getX(), currentPos.getY(), nextPos.getX(), nextPos.getY(), currentPos
/* 1217 */           .getZ() - 0.5F, nextPos.getZ() - 0.5F, weapon.isOnSurface(), weapon.isOnSurface(), true, 4, -10L, weapon
/* 1218 */           .getBridgeId(), -10L, false);
/* 1219 */       if (blocker != null) {
/*      */         
/* 1221 */         landingPosition.set(blocker.getFirstIntersection());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1226 */         landed = true;
/*      */       }
/*      */       else {
/*      */         
/* 1230 */         float groundHeight = Zones.calculateHeight(nextPos.getX(), nextPos.getY(), weapon.isOnSurface());
/* 1231 */         if (nextPos.getZ() <= groundHeight) {
/*      */ 
/*      */           
/* 1234 */           landingPosition.set(nextPos.getX(), nextPos.getY(), groundHeight);
/* 1235 */           landed = true;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1240 */       currentVelocity.z += gravityPerStep;
/* 1241 */       currentPos = nextPos;
/*      */     } 
/*      */     
/* 1244 */     ProjectileInfo toReturn = new ProjectileInfo(startingPosition, startingVelocity, landingPosition, currentVelocity, flightTime);
/* 1245 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean poll(long now) {
/* 1250 */     if (Features.Feature.NEW_PROJECTILES.isEnabled() && this.weapon.getTemplateId() != 936) {
/*      */       
/* 1252 */       if (now > this.timeAtLanding) {
/*      */         
/* 1254 */         float majorRadius = (getProjectile().getSizeX(true) + getProjectile().getSizeY(true)) / 2.0F / 10.0F;
/*      */ 
/*      */         
/* 1257 */         float damageMultiplier = this.projectileInfo.endVelocity.length() / 30.0F * (this.weapon.getCurrentQualityLevel() / 300.0F + 0.33F) * getProjectile().getWeightGrams() / 20000.0F;
/* 1258 */         float damage = 1.0F * damageMultiplier;
/*      */         
/* 1260 */         if (getProjectile().isStone() || getProjectile().isMetal()) {
/* 1261 */           damage *= 10.0F;
/* 1262 */         } else if (getProjectile().isCorpse()) {
/* 1263 */           damage *= 2.5F;
/*      */         } 
/* 1265 */         if (getProjectile().getTemplateId() == 298 || 
/* 1266 */           getProjectile().getTemplateId() == 26 || 
/* 1267 */           getProjectile().isEgg()) {
/* 1268 */           damage /= 15.0F;
/*      */         }
/* 1270 */         float extraDamage = (damage - 20.0F) / 4.0F;
/* 1271 */         float minorRadius = 1.0F + extraDamage / 10.0F;
/* 1272 */         damage = Math.min(20.0F, damage);
/*      */         
/* 1274 */         float radius = majorRadius * minorRadius;
/* 1275 */         int hitCounter = 0, wallCount = 0, fenceCount = 0, floorCount = 0;
/* 1276 */         int roofCount = 0, bridgeCount = 0, itemCount = 0;
/* 1277 */         ArrayList<Item> itemHitList = null;
/* 1278 */         ArrayList<Structure> structureHitList = null;
/* 1279 */         for (int i = (int)((this.projectileInfo.endPosition.x - radius) / 4.0F); i <= (int)((this.projectileInfo.endPosition.x + radius) / 4.0F); i++) {
/*      */           
/* 1281 */           for (int j = (int)((this.projectileInfo.endPosition.y - radius) / 4.0F); j <= (int)((this.projectileInfo.endPosition.y + radius) / 4.0F); j++) {
/*      */             
/* 1283 */             VolaTile tileInRadius = Zones.getOrCreateTile(i, j, this.weapon.isOnSurface());
/* 1284 */             if (tileInRadius == null)
/*      */               continue; 
/* 1286 */             for (Creature c : tileInRadius.getCreatures()) {
/*      */               
/* 1288 */               if (c.isUnique() || c.isInvulnerable() || c.getPower() > 0) {
/*      */                 continue;
/*      */               }
/* 1291 */               if (!c.isOnPvPServer() && (c.isHitched() || c.isCaredFor() || (c
/* 1292 */                 .isBranded() && !c.mayManage(this.shooter)))) {
/*      */                 continue;
/*      */               }
/* 1295 */               float distance = c.getPos3f().distance(this.projectileInfo.endPosition);
/* 1296 */               if (distance <= radius) {
/*      */                 
/* 1298 */                 if (c.isPlayer() && this.shooter.isFriendlyKingdom(c.getKingdomId()))
/*      */                 {
/*      */                   
/* 1301 */                   if (c != this.shooter) {
/*      */                     
/* 1303 */                     if (this.shooter.getCitizenVillage() == null || 
/* 1304 */                       !this.shooter.getCitizenVillage().isEnemy(c.getCitizenVillage())) {
/*      */                       
/* 1306 */                       if (!this.shooter.hasBeenAttackedBy(c.getWurmId()) && c
/* 1307 */                         .getCurrentKingdom() == c.getKingdomId()) {
/* 1308 */                         this.shooter.setUnmotivatedAttacker();
/*      */                       }
/* 1310 */                       if (!this.shooter.isOnPvPServer() || !c.isOnPvPServer()) {
/*      */                         continue;
/*      */                       }
/*      */                     } 
/*      */ 
/*      */ 
/*      */                     
/* 1317 */                     if (Servers.localServer.HOMESERVER && !this.shooter.isOnHostileHomeServer())
/*      */                     {
/* 1319 */                       if (c.getReputation() >= 0 && (c.citizenVillage == null || 
/* 1320 */                         !c.citizenVillage.isEnemy(this.shooter))) {
/*      */                         
/* 1322 */                         this.shooter.setReputation(this.shooter.getReputation() - 30);
/* 1323 */                         this.shooter.getCommunicator().sendAlertServerMessage("This is bad for your reputation.");
/*      */                       } 
/*      */                     }
/*      */                   } 
/*      */                 }
/*      */ 
/*      */                 
/* 1330 */                 if (damage > 0.0F) {
/*      */                   
/* 1332 */                   c.getCommunicator().sendAlertServerMessage("You are hit by " + this.projectile.getName() + " coming through the air!");
/* 1333 */                   if (!c.isPlayer()) {
/*      */                     
/* 1335 */                     c.setTarget(this.shooter.getWurmId(), false);
/* 1336 */                     c.setFleeCounter(20);
/*      */                   } 
/*      */                   
/* 1339 */                   this.shooter.getCommunicator().sendNormalServerMessage("You hit " + c.getNameWithGenus() + "!");
/* 1340 */                   float actualDam = (distance > majorRadius) ? Math.min(10.0F, extraDamage) : damage;
/* 1341 */                   if (c.isPlayer()) {
/*      */                     
/* 1343 */                     boolean dead = c.addWoundOfType(this.shooter, (byte)0, 1, true, 1.0F, true, 
/* 1344 */                         Math.min(25000.0F, actualDam * 1000.0F), 0.0F, 0.0F, false, false);
/* 1345 */                     this.shooter.achievement(47);
/* 1346 */                     if (dead) {
/*      */                       
/* 1348 */                       c.achievement(48);
/* 1349 */                       if (this.weapon.getTemplateId() == 445) {
/* 1350 */                         this.shooter.achievement(573);
/*      */                       }
/*      */                     } 
/*      */                   } else {
/*      */                     
/* 1355 */                     boolean dead = c.addWoundOfType(this.shooter, (byte)0, 1, true, 1.0F, true, 
/* 1356 */                         Math.min(25000.0F, actualDam * 1000.0F), 0.0F, 0.0F, false, false);
/* 1357 */                     if (dead && this.weapon.getTemplateId() == 445) {
/* 1358 */                       this.shooter.achievement(573);
/*      */                     }
/*      */                   } 
/*      */                 } 
/* 1362 */                 hitCounter++;
/*      */               } 
/*      */               
/*      */               continue;
/*      */             } 
/*      */             
/* 1368 */             if (!Servers.localServer.PVPSERVER && tileInRadius.getStructure() != null)
/*      */             {
/* 1370 */               if (!tileInRadius.getStructure().isActionAllowed(this.shooter, (short)174)) {
/*      */                 continue;
/*      */               }
/*      */             }
/* 1374 */             for (Wall w : tileInRadius.getWalls()) {
/*      */               
/* 1376 */               if (!w.isWallPlan())
/*      */               {
/*      */                 
/* 1379 */                 if (Math.abs((w.getCenterPoint()).z - this.projectileInfo.endPosition.z) <= 1.5F) {
/*      */ 
/*      */                   
/* 1382 */                   float distance = w.isHorizontal() ? Math.abs((w.getCenterPoint()).y - this.projectileInfo.endPosition.y) : Math.abs((w.getCenterPoint()).x - this.projectileInfo.endPosition.x);
/* 1383 */                   float actualDam = (distance > majorRadius) ? Math.min(10.0F, extraDamage) : damage;
/* 1384 */                   if (distance <= radius) {
/*      */                     
/* 1386 */                     wallCount++;
/* 1387 */                     hitCounter++;
/*      */                     
/* 1389 */                     if (Servers.localServer.testServer) {
/* 1390 */                       this.shooter.getCommunicator().sendSafeServerMessage(w.getName() + " hit for " + (w.getDamageModifier() * actualDam));
/*      */                     }
/* 1392 */                     w.setDamage(w.getDamage() + w.getDamageModifier() * actualDam);
/*      */                     
/*      */                     try {
/* 1395 */                       if (structureHitList == null) {
/* 1396 */                         structureHitList = new ArrayList<>();
/*      */                       }
/* 1398 */                       if (!structureHitList.contains(Structures.getStructure(w.getStructureId())))
/* 1399 */                         structureHitList.add(Structures.getStructure(w.getStructureId())); 
/* 1400 */                     } catch (NoSuchStructureException noSuchStructureException) {}
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */             } 
/*      */ 
/*      */             
/* 1407 */             for (Fence f : tileInRadius.getFences()) {
/*      */               
/* 1409 */               if (Math.abs((f.getCenterPoint()).z - this.projectileInfo.endPosition.z) <= 1.5F) {
/*      */ 
/*      */                 
/* 1412 */                 float distance = f.isHorizontal() ? Math.abs((f.getCenterPoint()).y - this.projectileInfo.endPosition.y) : Math.abs((f.getCenterPoint()).x - this.projectileInfo.endPosition.x);
/* 1413 */                 float actualDam = (distance > majorRadius) ? Math.min(10.0F, extraDamage) : damage;
/* 1414 */                 if (distance <= radius) {
/*      */                   
/* 1416 */                   fenceCount++;
/* 1417 */                   hitCounter++;
/*      */                   
/* 1419 */                   if (Servers.localServer.testServer) {
/* 1420 */                     this.shooter.getCommunicator().sendSafeServerMessage(f.getName() + " hit for " + (f.getDamageModifier() * actualDam));
/*      */                   }
/* 1422 */                   f.setDamage(f.getDamage() + f.getDamageModifier() * actualDam);
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/* 1427 */             for (Floor f : tileInRadius.getFloors()) {
/*      */               
/* 1429 */               if (!f.isAPlan()) {
/*      */ 
/*      */                 
/* 1432 */                 float distance = Math.abs((f.getCenterPoint()).z - this.projectileInfo.endPosition.z);
/* 1433 */                 float actualDam = (distance > majorRadius) ? Math.min(10.0F, extraDamage) : damage;
/* 1434 */                 if (distance <= radius) {
/*      */                   
/* 1436 */                   if (f.isRoof()) {
/* 1437 */                     roofCount++;
/*      */                   } else {
/* 1439 */                     floorCount++;
/* 1440 */                   }  hitCounter++;
/*      */                   
/* 1442 */                   if (Servers.localServer.testServer) {
/* 1443 */                     this.shooter.getCommunicator().sendSafeServerMessage(f.getName() + " hit for " + (f.getDamageModifier() * actualDam));
/*      */                   }
/* 1445 */                   f.setDamage(f.getDamage() + f.getDamageModifier() * actualDam);
/*      */                   
/*      */                   try {
/* 1448 */                     if (structureHitList == null) {
/* 1449 */                       structureHitList = new ArrayList<>();
/*      */                     }
/* 1451 */                     if (!structureHitList.contains(Structures.getStructure(f.getStructureId())))
/* 1452 */                       structureHitList.add(Structures.getStructure(f.getStructureId())); 
/* 1453 */                   } catch (NoSuchStructureException noSuchStructureException) {}
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */ 
/*      */             
/* 1459 */             for (BridgePart bp : tileInRadius.getBridgeParts()) {
/*      */               
/* 1461 */               if (!bp.isAPlan())
/*      */               {
/*      */                 
/* 1464 */                 if (bp.getCenterPoint().distance(this.projectileInfo.endPosition) <= 4.0F) {
/*      */                   
/* 1466 */                   bridgeCount++;
/* 1467 */                   hitCounter++;
/*      */                   
/* 1469 */                   bp.setDamage(bp.getDamage() + bp.getDamageModifier() * damage);
/*      */                 } 
/*      */               }
/*      */             } 
/* 1473 */             for (Item it : tileInRadius.getItems()) {
/*      */               
/* 1475 */               if (it.isIndestructible() || it.isRoadMarker()) {
/*      */                 continue;
/*      */               }
/* 1478 */               if (it.isLocked() || it.isVehicle()) {
/*      */                 continue;
/*      */               }
/* 1481 */               if (it.isOwnerDestroyable())
/*      */               {
/* 1483 */                 if (it.lastOwner != this.shooter.getWurmId() && !this.shooter.isOnPvPServer()) {
/*      */                   
/* 1485 */                   Village village = tileInRadius.getVillage();
/* 1486 */                   if (village == null || !village.isActionAllowed((short)83, this.shooter)) {
/*      */                     continue;
/*      */                   }
/*      */                 } 
/*      */               }
/* 1491 */               if (it.getZoneId() != -10L)
/*      */               {
/* 1493 */                 if (it.isKingdomMarker() && it.getKingdom() == this.shooter.getKingdomId())
/*      */                 {
/* 1495 */                   if (this.shooter.getWurmId() != it.lastOwner)
/*      */                   {
/* 1497 */                     if (tileInRadius.getVillage() == null || tileInRadius.getVillage() != this.shooter.getCitizenVillage()) {
/*      */                       continue;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/* 1503 */               if (it.getPos3f().distance(this.projectileInfo.endPosition) <= radius) {
/*      */                 
/* 1505 */                 itemCount++;
/*      */                 
/* 1507 */                 if (itemHitList == null)
/* 1508 */                   itemHitList = new ArrayList<>(); 
/* 1509 */                 itemHitList.add(it);
/*      */               }  continue;
/*      */             } 
/*      */             continue;
/*      */           } 
/*      */         } 
/* 1515 */         if (itemHitList != null) {
/*      */           
/* 1517 */           hitCounter++;
/* 1518 */           Item t = itemHitList.get(Server.rand.nextInt(itemHitList.size()));
/*      */           
/* 1520 */           float actualDam = (t.getPos3f().distance(this.projectileInfo.endPosition) > majorRadius) ? Math.min(10.0F, extraDamage) : damage;
/* 1521 */           t.setDamage(t.getDamage() + t.getDamageModifier() * actualDam / 25.0F);
/*      */         } 
/*      */         
/* 1524 */         if (hitCounter == 0) {
/*      */           
/* 1526 */           this.shooter.getCommunicator().sendNormalServerMessage("It doesn't sound like the " + getProjectile().getName() + " hit anything.");
/*      */         }
/*      */         else {
/*      */           
/* 1530 */           StringBuilder targetList = new StringBuilder();
/* 1531 */           targetList.append("It sounds as though the " + getProjectile().getName() + " hit ");
/* 1532 */           if (wallCount > 0)
/* 1533 */             targetList.append(((wallCount > 1) ? (String)Integer.valueOf(wallCount) : "a") + " wall" + ((wallCount > 1) ? "s" : "") + ", "); 
/* 1534 */           if (fenceCount > 0)
/* 1535 */             targetList.append(((fenceCount > 1) ? (String)Integer.valueOf(fenceCount) : "a") + " fence" + ((fenceCount > 1) ? "s" : "") + ", "); 
/* 1536 */           if (roofCount > 0)
/* 1537 */             targetList.append(((roofCount > 1) ? (String)Integer.valueOf(roofCount) : "a") + " roof" + ((roofCount > 1) ? "s" : "") + ", "); 
/* 1538 */           if (floorCount > 0)
/* 1539 */             targetList.append(((floorCount > 1) ? (String)Integer.valueOf(floorCount) : "a") + " floor" + ((floorCount > 1) ? "s" : "") + ", "); 
/* 1540 */           if (bridgeCount > 0)
/* 1541 */             targetList.append(((bridgeCount > 1) ? (String)Integer.valueOf(bridgeCount) : "a") + " bridge part" + ((bridgeCount > 1) ? "s" : "") + ", "); 
/* 1542 */           if (itemCount > 0)
/* 1543 */             targetList.append("an item, "); 
/* 1544 */           targetList.append("and nothing else.");
/*      */           
/* 1546 */           this.shooter.getCommunicator().sendNormalServerMessage(targetList.toString());
/*      */           
/* 1548 */           if (structureHitList != null && !structureHitList.isEmpty()) {
/*      */             
/* 1550 */             for (Structure struct : structureHitList) {
/*      */               
/* 1552 */               if (struct.isDestroyed()) {
/*      */                 
/* 1554 */                 struct.totallyDestroy();
/* 1555 */                 if (this.weapon.getTemplateId() == 445)
/* 1556 */                   this.shooter.achievement(51); 
/*      */               } 
/*      */             } 
/* 1559 */             targetList.delete(0, targetList.length());
/* 1560 */             targetList.append("You managed to hit ");
/* 1561 */             int id = 0;
/* 1562 */             while (id < structureHitList.size()) {
/*      */               
/* 1564 */               boolean hasMore = (id < structureHitList.size() - 1);
/* 1565 */               targetList.append(((hasMore && id > 0) ? "" : ((id == 0) ? "" : "and ")) + ((Structure)structureHitList.get(id)).getName());
/* 1566 */               if (hasMore && id + 1 < structureHitList.size() - 1) {
/* 1567 */                 targetList.append(", ");
/* 1568 */               } else if (hasMore) {
/* 1569 */                 targetList.append(" ");
/*      */               } else {
/* 1571 */                 targetList.append(".");
/*      */               } 
/* 1573 */               id++;
/*      */             } 
/*      */             
/* 1576 */             this.shooter.getCommunicator().sendNormalServerMessage(targetList.toString());
/*      */           } 
/*      */         } 
/*      */         
/* 1580 */         boolean projDestroyed = getProjectile().setDamage(getProjectile().getDamage() + damage);
/* 1581 */         if (!projDestroyed) {
/*      */           
/*      */           try
/*      */           {
/* 1585 */             getProjectile().setPosXYZ(this.projectileInfo.endPosition.x, this.projectileInfo.endPosition.y, this.projectileInfo.endPosition.z);
/*      */             
/* 1587 */             Zone z = Zones.getZone(getProjectile().getTileX(), getProjectile().getTileY(), this.weapon.isOnSurface());
/* 1588 */             z.addItem(getProjectile());
/*      */           }
/* 1590 */           catch (NoSuchZoneException e)
/*      */           {
/* 1592 */             e.printStackTrace();
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1597 */           this.shooter.getCommunicator().sendNormalServerMessage("The " + getProjectile().getName() + " crumbles to dust as it lands.");
/*      */         } 
/*      */         
/* 1600 */         if (Servers.localServer.testServer) {
/* 1601 */           this.shooter.getCommunicator().sendNormalServerMessage("[TEST] Projectile " + getProjectile().getName() + " landed, damage multiplier: " + damageMultiplier + ", damage: " + damage + " total things hit: " + hitCounter + ". Total distance: " + this.projectileInfo.startPosition
/*      */               
/* 1603 */               .distance(this.projectileInfo.endPosition) + "m or " + (this.projectileInfo.startPosition
/* 1604 */               .distance(this.projectileInfo.endPosition) / 4.0F) + " tiles.");
/*      */         }
/* 1606 */         return true;
/*      */       } 
/* 1608 */       if (this.timeAtLanding - now <= 500L)
/*      */       {
/* 1610 */         if (!this.sentEffect) {
/*      */ 
/*      */ 
/*      */           
/* 1614 */           EffectFactory.getInstance().createGenericTempEffect("dust03", this.projectileInfo.endPosition.x, this.projectileInfo.endPosition.y, this.projectileInfo.endPosition.z, this.weapon
/* 1615 */               .isOnSurface(), -1.0F, 0.0F);
/* 1616 */           this.sentEffect = true;
/*      */         } 
/*      */       }
/* 1619 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1623 */     if (now > this.timeAtLanding) {
/*      */       
/* 1625 */       float newx = getPosDownX();
/* 1626 */       float newy = getPosDownY();
/* 1627 */       if (this.result != null)
/*      */       {
/* 1629 */         if (this.result.getFirstBlocker() != null) {
/*      */           
/* 1631 */           newx = (this.result.getFirstBlocker().getTileX() * 4 + 2);
/* 1632 */           newy = (this.result.getFirstBlocker().getTileY() * 4 + 2);
/*      */         } 
/*      */       }
/* 1635 */       Skill cataskill = null;
/* 1636 */       int skilltype = 10077;
/* 1637 */       if (this.weapon.getTemplateId() == 936)
/* 1638 */         skilltype = 10093; 
/* 1639 */       if (this.weapon.getTemplateId() == 937) {
/* 1640 */         skilltype = 10094;
/*      */       }
/*      */       try {
/* 1643 */         cataskill = getShooter().getSkills().getSkill(skilltype);
/*      */       }
/* 1645 */       catch (NoSuchSkillException nss) {
/*      */         
/* 1647 */         cataskill = getShooter().getSkills().learn(skilltype, 1.0F);
/*      */       } 
/* 1649 */       Vector2f targPos = new Vector2f(this.weapon.getPosX(), this.weapon.getPosY());
/* 1650 */       Vector2f projPos = new Vector2f(newx, newy);
/*      */       
/* 1652 */       float dist = Math.abs(projPos.subtract(targPos).length() / 4.0F);
/*      */       
/* 1654 */       double power = 0.0D;
/*      */       
/* 1656 */       VolaTile droptile = Zones.getOrCreateTile((int)(newx / 4.0F), (int)(newy / 4.0F), true);
/* 1657 */       int dropFloorLevel = 0;
/* 1658 */       boolean hit = false;
/* 1659 */       boolean itemDestroyed = false;
/*      */       
/*      */       try {
/* 1662 */         Item i = getProjectile();
/* 1663 */         if (this.result != null)
/*      */         {
/* 1665 */           if (this.result.getFirstBlocker() != null) {
/*      */             
/* 1667 */             dropFloorLevel = droptile.getDropFloorLevel(this.result.getFirstBlocker()
/* 1668 */                 .getFloorLevel());
/* 1669 */             if (this.result.getFirstBlocker().isTile()) {
/*      */               
/* 1671 */               itemDestroyed = setEffects(getWeapon(), i, (int)(newx / 4.0F), (int)(newy / 4.0F), dist, this.result
/* 1672 */                   .getFirstBlocker().getFloorLevel(), getShooter(), 
/* 1673 */                   getRarity(), getDamageDealt());
/*      */             }
/*      */             else {
/*      */               
/* 1677 */               boolean hadSkillGainChance = false;
/* 1678 */               boolean messageSent = false;
/* 1679 */               String whatishit = "the " + this.result.getFirstBlocker().getName();
/* 1680 */               Village vill = MethodsStructure.getVillageForBlocker(this.result.getFirstBlocker());
/* 1681 */               VolaTile t = Zones.getOrCreateTile(this.result.getFirstBlocker().getTileX(), this.result
/* 1682 */                   .getFirstBlocker().getTileY(), true);
/* 1683 */               boolean ok = isOkToAttack(t, getShooter(), getDamageDealt());
/* 1684 */               if (!ok && vill != null)
/*      */               {
/*      */ 
/*      */                 
/* 1688 */                 if (vill.isActionAllowed((short)174, getShooter(), false, 0, 0)) {
/*      */                   
/* 1690 */                   ok = true;
/*      */                 }
/* 1692 */                 else if (!vill.isEnemy(getShooter())) {
/*      */                   
/* 1694 */                   ok = false;
/*      */                 } 
/*      */               }
/* 1697 */               if (ok) {
/*      */                 
/* 1699 */                 power = cataskill.skillCheck(dist - 9.0D, this.weapon, 0.0D, false, 10.0F);
/* 1700 */                 hadSkillGainChance = true;
/* 1701 */                 hit = (power > 0.0D);
/* 1702 */                 if (hit)
/*      */                 {
/* 1704 */                   if (getDamageDealt() > 0.0F) {
/*      */                     
/* 1706 */                     int fl = this.result.getFirstBlocker().getFloorLevel();
/* 1707 */                     float mod = 1.0F;
/*      */                     
/* 1709 */                     float newDam = this.result.getFirstBlocker().getDamage() + Math.min(20.0F, this.result.getFirstBlocker().getDamageModifier() * 
/* 1710 */                         getDamageDealt() / 1.0F);
/* 1711 */                     whatishit = this.result.getFirstBlocker().getName();
/* 1712 */                     getShooter().getCommunicator().sendNormalServerMessage("You seem to have hit " + whatishit + "!" + (
/*      */                         
/* 1714 */                         Servers.isThisATestServer() ? (" Dam:" + getDamageDealt() + " NewDam:" + newDam) : ""));
/*      */ 
/*      */                     
/* 1717 */                     if (!this.result.getFirstBlocker().isFloor()) {
/*      */                       
/* 1719 */                       t.damageFloors(fl, fl + 1, 
/* 1720 */                           Math.min(20.0F, this.result.getFirstBlocker().getDamageModifier() * 
/* 1721 */                             getDamageDealt()));
/*      */                       
/* 1723 */                       if (this.result.getFirstBlocker().isHorizontal()) {
/*      */                         
/* 1725 */                         VolaTile t2 = Zones.getTileOrNull(this.result.getFirstBlocker()
/* 1726 */                             .getTileX() - 1, this.result
/* 1727 */                             .getFirstBlocker().getTileY(), true);
/* 1728 */                         if (t2 != null)
/*      */                         {
/* 1730 */                           t2.damageFloors(fl, fl + 1, 
/* 1731 */                               Math.min(20.0F, this.result.getFirstBlocker().getDamageModifier() * 
/* 1732 */                                 getDamageDealt()));
/*      */                         }
/*      */                       } 
/*      */                     } 
/* 1736 */                     this.result.getFirstBlocker().setDamage(newDam);
/*      */                     
/* 1738 */                     if (newDam >= 100.0F) {
/*      */                       
/* 1740 */                       TileEvent.log(this.result.getFirstBlocker().getTileX(), this.result.getFirstBlocker()
/* 1741 */                           .getTileY(), 0, 
/*      */                           
/* 1743 */                           getShooter().getWurmId(), 236);
/*      */                       
/* 1745 */                       if (this.result.getFirstBlocker().isFloor())
/*      */                       {
/* 1747 */                         t.removeFloor(this.result.getFirstBlocker());
/*      */                       }
/*      */                     } 
/*      */                   } 
/*      */                 }
/* 1752 */                 itemDestroyed = this.projectile.setDamage(this.projectile.getDamage() + this.projectile
/* 1753 */                     .getDamageModifier() * (20 + Server.rand
/* 1754 */                     .nextInt(Math.max(1, (int)dist))));
/* 1755 */                 if (itemDestroyed)
/*      */                 {
/* 1757 */                   t.broadCast("A " + this.projectile.getName() + " comes flying through the air, hits " + whatishit + ", and shatters.");
/*      */                 
/*      */                 }
/*      */                 else
/*      */                 {
/* 1762 */                   t.broadCast("A " + this.projectile.getName() + " comes flying through the air and hits " + whatishit + ".");
/*      */                 }
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/* 1768 */                 getShooter().getCommunicator().sendNormalServerMessage("You seem to miss with the " + this.projectile
/* 1769 */                     .getName() + ".");
/* 1770 */                 messageSent = true;
/*      */               } 
/*      */               
/* 1773 */               if (testHitCreaturesOnTile(droptile, getShooter(), i, getDamageDealt(), dist, dropFloorLevel)) {
/*      */ 
/*      */                 
/* 1776 */                 if (!hadSkillGainChance)
/* 1777 */                   power = cataskill.skillCheck(dist - 9.0D, this.weapon, 0.0D, false, 10.0F); 
/* 1778 */                 hit = hitCreaturesOnTile(droptile, power, getShooter(), i, getDamageDealt(), dist, dropFloorLevel);
/*      */               } 
/*      */ 
/*      */               
/* 1782 */               if (!hit && !messageSent)
/*      */               {
/* 1784 */                 getShooter().getCommunicator().sendNormalServerMessage("You just missed with the " + this.projectile
/* 1785 */                     .getName() + ".");
/*      */               }
/*      */             } 
/*      */           } 
/*      */           
/* 1790 */           if (!itemDestroyed)
/*      */           {
/* 1792 */             i.setPosXYZ(newx, newy, 
/*      */ 
/*      */                 
/* 1795 */                 Zones.calculateHeight(newx, newy, 
/* 1796 */                   (this.result.getFirstBlocker().getFloorLevel() >= 0)) + (
/* 1797 */                 Math.max(0, dropFloorLevel) * 3));
/*      */ 
/*      */ 
/*      */             
/* 1801 */             VolaTile vt = Zones.getOrCreateTile((int)(newx / 4.0F), (int)(newy / 4.0F), this.weapon.isOnSurface());
/* 1802 */             if ((vt.getBridgeParts()).length > 0)
/*      */             {
/* 1804 */               i.setOnBridge(vt.getBridgeParts()[0].getStructureId());
/*      */             }
/*      */             
/* 1807 */             Zone z = Zones.getZone(i.getTileX(), i.getTileY(), 
/* 1808 */                 (this.result.getFirstBlocker().getFloorLevel() >= 0));
/* 1809 */             z.addItem(i);
/* 1810 */             logger.log(Level.INFO, "Adding " + i.getName() + " at " + (int)(newx / 4.0F) + "," + (int)(newy / 4.0F));
/*      */           }
/*      */         
/*      */         }
/* 1814 */         else if (!setEffects(getWeapon(), i, (int)(getPosDownX() / 4.0F), (int)(getPosDownY() / 4.0F), dist, 0, 
/*      */             
/* 1816 */             getShooter(), getRarity(), getDamageDealt()))
/*      */         {
/*      */           
/* 1819 */           VolaTile vt = Zones.getOrCreateTile((int)(newx / 4.0F), (int)(newy / 4.0F), this.weapon.isOnSurface());
/* 1820 */           float newz = 0.0F;
/* 1821 */           if ((vt.getBridgeParts()).length > 0) {
/*      */ 
/*      */             
/* 1824 */             i.setOnBridge(-10L);
/*      */           } else {
/*      */             
/* 1827 */             newz = Zones.calculateHeight(getPosDownX(), getPosDownY(), false);
/*      */           } 
/* 1829 */           i.setPosXYZ(getPosDownX(), getPosDownY(), newz);
/* 1830 */           Zone z = Zones.getZone(i.getTileX(), i.getTileY(), true);
/* 1831 */           z.addItem(i);
/* 1832 */           logger.log(Level.INFO, "Adding " + i.getName() + " at " + (int)(getPosDownX() / 4.0F) + "," + 
/* 1833 */               (int)(getPosDownY() / 4.0F));
/*      */         }
/*      */       
/* 1836 */       } catch (NoSuchZoneException nsz) {
/*      */         
/* 1838 */         logger.log(Level.INFO, getProjectile().getModelName() + " projectile with id " + getProjectile().getWurmId() + " shot outside the map");
/*      */       } 
/*      */       
/* 1841 */       return true;
/*      */     } 
/* 1843 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pollAll() {
/* 1849 */     long now = System.currentTimeMillis();
/* 1850 */     for (ServerProjectile projectile : projectiles) {
/*      */       
/* 1852 */       if (projectile.poll(now))
/*      */       {
/* 1854 */         projectiles.remove(projectile);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void removeProjectile(ServerProjectile projectile) {
/* 1861 */     projectiles.remove(projectile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPosDownX() {
/* 1871 */     return this.posDownX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPosDownY() {
/* 1881 */     return this.posDownY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCurrentSecondsInAir() {
/* 1891 */     return this.currentSecondsInAir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCurrentSecondsInAir(float aCurrentSecondsInAir) {
/* 1902 */     this.currentSecondsInAir = aCurrentSecondsInAir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item getWeapon() {
/* 1912 */     return this.weapon;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getRarity() {
/* 1922 */     return this.rarity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockingResult getResult() {
/* 1932 */     return this.result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResult(BlockingResult aResult) {
/* 1943 */     this.result = aResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item getProjectile() {
/* 1953 */     return this.projectile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getDamageDealt() {
/* 1963 */     return this.damageDealth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDamageDealt(float aDamageDealth) {
/* 1974 */     this.damageDealth = aDamageDealth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Creature getShooter() {
/* 1984 */     return this.shooter;
/*      */   }
/*      */ 
/*      */   
/*      */   static class ProjectileInfo
/*      */   {
/*      */     public final Vector3f startPosition;
/*      */     
/*      */     public final Vector3f startVelocity;
/*      */     
/*      */     public final Vector3f endPosition;
/*      */     public final Vector3f endVelocity;
/*      */     public final long timeToImpact;
/*      */     
/*      */     ProjectileInfo(Vector3f startPosition, Vector3f startVelocity, Vector3f endPosition, Vector3f endVelocity, long timeToImpact) {
/* 1999 */       this.startPosition = startPosition.clone();
/* 2000 */       this.startVelocity = startVelocity.clone();
/* 2001 */       this.endPosition = endPosition.clone();
/* 2002 */       this.endVelocity = endVelocity.clone();
/* 2003 */       this.timeToImpact = timeToImpact;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\ServerProjectile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */