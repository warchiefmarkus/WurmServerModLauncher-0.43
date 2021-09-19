/*      */ package com.wurmonline.server.combat;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.ai.NoPathException;
/*      */ import com.wurmonline.server.creatures.ai.Path;
/*      */ import com.wurmonline.server.creatures.ai.PathFinder;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.utils.CreatureLineSegment;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VirtualZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import java.util.ArrayList;
/*      */ import java.util.logging.Level;
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
/*      */ 
/*      */ 
/*      */ public final class Archery
/*      */   implements MiscConstants, ItemMaterials, SoundNames, TimeConstants
/*      */ {
/*      */   public static final int BREAKNUM = 2;
/*   82 */   private static final Logger logger = Logger.getLogger(Archery.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean fnd = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final float swimDepth = 1.4F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean attack(Creature performer, Creature defender, Item bow, float counter, Action act) {
/*   96 */     boolean done = false;
/*   97 */     if (defender.isInvulnerable()) {
/*      */       
/*   99 */       performer.getCommunicator().sendCombatNormalMessage("You can't attack " + defender.getNameWithGenus() + " right now.");
/*  100 */       return true;
/*      */     } 
/*  102 */     if (defender.equals(performer))
/*      */     {
/*  104 */       return true;
/*      */     }
/*  106 */     if (performer.getPositionZ() < -0.5D && performer.getVehicle() == -10L) {
/*      */       
/*  108 */       performer.getCommunicator().sendCombatNormalMessage("You are too deep in the water to fire a bow.");
/*  109 */       return true;
/*      */     } 
/*  111 */     if (performer.getPrimWeapon() != bow) {
/*      */       
/*  113 */       Item[] weps = performer.getSecondaryWeapons();
/*  114 */       fnd = false;
/*  115 */       for (int x = 0; x < weps.length; x++) {
/*      */         
/*  117 */         if (weps[x] == bow)
/*  118 */           fnd = true; 
/*      */       } 
/*  120 */       if (!fnd) {
/*      */         
/*  122 */         performer.getCommunicator().sendCombatNormalMessage("You need to wield the bow in order to use it.");
/*  123 */         return true;
/*      */       } 
/*      */     } 
/*  126 */     if (performer.getShield() != null) {
/*      */       
/*  128 */       performer.getCommunicator().sendCombatNormalMessage("You can not use the bow while wearing a shield.");
/*  129 */       return true;
/*      */     } 
/*  131 */     int minRange = getMinimumRangeForBow(bow);
/*  132 */     double maxRange = 180.0D;
/*  133 */     if (Creature.getRange(performer, defender.getPosX(), defender.getPosY()) < minRange) {
/*      */       
/*  135 */       performer.getCommunicator().sendCombatNormalMessage(defender.getNameWithGenus() + " is too close.");
/*  136 */       return true;
/*      */     } 
/*  138 */     if (Creature.getRange(performer, defender.getPosX(), defender.getPosY()) > 180.0D) {
/*      */       
/*  140 */       performer.getCommunicator().sendCombatNormalMessage(defender.getNameWithGenus() + " is too far away.");
/*  141 */       return true;
/*      */     } 
/*      */     
/*  144 */     if (defender.isDead())
/*  145 */       return true; 
/*  146 */     if (bow.isWeaponBow()) {
/*      */       
/*  148 */       Skill archery = null;
/*  149 */       Skill bowskill = null;
/*      */       
/*      */       try {
/*  152 */         int skillnum = bow.getPrimarySkill();
/*  153 */         if (skillnum != -10L) {
/*      */           try
/*      */           {
/*      */             
/*  157 */             bowskill = performer.getSkills().getSkill(skillnum);
/*      */           }
/*  159 */           catch (NoSuchSkillException nss)
/*      */           {
/*  161 */             bowskill = performer.getSkills().learn(skillnum, 1.0F);
/*      */           }
/*      */         
/*      */         }
/*  165 */       } catch (NoSuchSkillException nss) {
/*      */         
/*  167 */         performer.getCommunicator().sendCombatNormalMessage("This weapon has no skill attached. Please report this bug using the forums or the /dev channel.");
/*      */         
/*  169 */         return true;
/*      */       } 
/*      */       
/*      */       try {
/*  173 */         archery = performer.getSkills().getSkill(1030);
/*      */       }
/*  175 */       catch (NoSuchSkillException nss) {
/*      */         
/*  177 */         archery = performer.getSkills().learn(1030, 1.0F);
/*      */       } 
/*      */       
/*  180 */       if (bowskill == null || archery == null) {
/*      */         
/*  182 */         performer
/*  183 */           .getCommunicator()
/*  184 */           .sendCombatNormalMessage("There was a bug with the skill for this item. Please report this bug using the forums or the /dev channel.");
/*      */         
/*  186 */         return true;
/*      */       } 
/*      */       
/*  189 */       if (!Servers.isThisAPvpServer() && defender.getBrandVillage() != null && 
/*  190 */         !defender.getBrandVillage().mayAttack(performer, defender)) {
/*      */         
/*  192 */         performer.getCommunicator().sendCombatNormalMessage(defender.getNameWithGenus() + " is protected by its branding.");
/*  193 */         return true;
/*      */       } 
/*      */       
/*  196 */       if (performer.currentVillage != null && 
/*  197 */         !performer.currentVillage.mayAttack(performer, defender) && performer
/*  198 */         .isLegal()) {
/*      */         
/*  200 */         performer.getCommunicator().sendCombatNormalMessage("The permissions for the settlement you are on prevents this.");
/*  201 */         return true;
/*      */       } 
/*      */       
/*  204 */       if (defender.currentVillage != null && 
/*  205 */         !defender.currentVillage.mayAttack(performer, defender) && performer
/*  206 */         .isLegal()) {
/*      */         
/*  208 */         performer.getCommunicator().sendCombatNormalMessage("The permissions for the settlement that the target is on prevents this.");
/*  209 */         return true;
/*      */       } 
/*      */       
/*  212 */       int time = 200;
/*  213 */       if (counter == 1.0F) {
/*      */         
/*  215 */         if (performer.isOnSurface() != defender.isOnSurface()) {
/*      */           
/*  217 */           performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot of " + defender
/*  218 */               .getNameWithGenus() + ".");
/*  219 */           return true;
/*      */         } 
/*  221 */         Item arrow = getArrow(performer);
/*  222 */         if (arrow == null) {
/*      */           
/*  224 */           performer.getCommunicator().sendCombatNormalMessage("You have no arrows left to shoot!");
/*  225 */           return true;
/*      */         } 
/*  227 */         if (defender.isGhost())
/*      */         {
/*  229 */           if (arrow.getSpellEffects() == null || (arrow.getSpellEffects().getEffects()).length == 0) {
/*      */             
/*  231 */             performer.getCommunicator().sendCombatNormalMessage("An unenchanted arrow would not harm the " + defender
/*  232 */                 .getNameWithGenus() + ".");
/*  233 */             return true;
/*      */           } 
/*      */         }
/*      */         
/*  237 */         time = Actions.getQuickActionTime(performer, archery, bow, 0.0D);
/*  238 */         time = Math.max(50, time);
/*  239 */         if (act.getNumber() == 125 && 
/*  240 */           time > 50)
/*      */         {
/*  242 */           time = (int)Math.max(20.0F, time * 0.8F);
/*      */         }
/*      */         
/*  245 */         performer.setStealth(false);
/*  246 */         performer.getCommunicator().sendCombatNormalMessage("You start aiming at " + defender.getNameWithGenus() + ".");
/*  247 */         if (WurmCalendar.getHour() > 20 || WurmCalendar.getHour() < 5)
/*  248 */           performer.getCommunicator().sendCombatNormalMessage("The dusk makes it harder to get a clear view of " + defender
/*  249 */               .getNameWithGenus() + "."); 
/*  250 */         performer.sendActionControl(Actions.actionEntrys[act.getNumber()].getVerbString(), true, time);
/*  251 */         Server.getInstance().broadCastAction(performer
/*  252 */             .getName() + " draws an arrow and raises " + performer.getHisHerItsString() + " bow.", performer, 5);
/*      */ 
/*      */         
/*  255 */         SoundPlayer.playSound("sound.arrow.aim", performer, 1.6F);
/*  256 */         act.setTimeLeft(time);
/*      */       } else {
/*      */         
/*  259 */         time = act.getTimeLeft();
/*  260 */       }  if (counter * 10.0F > time || performer.getPower() > 0)
/*      */       {
/*  262 */         done = true;
/*      */         
/*  264 */         performer.getStatus().modifyStamina(-1000.0F);
/*  265 */         if (performer.isOnSurface() != defender.isOnSurface()) {
/*      */           
/*  267 */           performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot of " + defender
/*  268 */               .getNameWithGenus() + ".");
/*  269 */           return true;
/*      */         } 
/*  271 */         boolean limitFail = false;
/*  272 */         if (performer.getArmourLimitingFactor() < 0.0F && Server.rand
/*  273 */           .nextFloat() < Math.abs(performer.getArmourLimitingFactor() * ItemBonus.getArcheryPenaltyReduction(performer)))
/*      */         {
/*  275 */           limitFail = true;
/*      */         }
/*  277 */         Fence fence = null;
/*  278 */         Zones.resetCoverHolder();
/*  279 */         boolean isAttackingPenned = false;
/*  280 */         if (performer.isOnSurface())
/*      */         {
/*  282 */           if (!performer.isWithinDistanceTo(defender, 110.0F))
/*      */           {
/*  284 */             if (defender.getCurrentTile() != null && defender.getCurrentTile().getStructure() != null && defender
/*  285 */               .getCurrentTile().getStructure().isFinalFinished()) {
/*      */               
/*  287 */               performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot of " + defender
/*  288 */                   .getNameWithGenus() + " inside the structure.");
/*  289 */               return true;
/*      */             } 
/*      */           }
/*      */         }
/*  293 */         BlockingResult result = Blocking.getRangedBlockerBetween(performer, defender);
/*      */         
/*  295 */         if (result != null) {
/*      */           
/*  297 */           if (!performer.isOnPvPServer() || !defender.isOnPvPServer()) {
/*      */ 
/*      */             
/*  300 */             performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot of " + defender
/*  301 */                 .getNameWithGenus() + ".");
/*  302 */             return true;
/*      */           } 
/*  304 */           for (Blocker b : result.getBlockerArray()) {
/*      */             
/*  306 */             if (b.getBlockPercent(performer) >= 100.0F) {
/*      */               
/*  308 */               performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot of " + defender
/*  309 */                   .getNameWithGenus() + ".");
/*  310 */               return true;
/*      */             } 
/*      */           } 
/*  313 */           if (!defender.isPlayer() && result.getTotalCover() > 0.0F)
/*  314 */             isAttackingPenned = true; 
/*      */         } 
/*  316 */         if (!defender.isPlayer() && defender.getFloorLevel() != performer.getFloorLevel()) {
/*  317 */           isAttackingPenned = true;
/*      */         }
/*  319 */         if (!VirtualZone.isCreatureTurnedTowardsTarget(defender, performer, 60.0F, false)) {
/*      */           
/*  321 */           performer.getCommunicator().sendCombatNormalMessage("You must turn towards " + defender
/*  322 */               .getNameWithGenus() + " in order to shoot it.");
/*  323 */           return true;
/*      */         } 
/*      */         
/*  326 */         int minRange1 = getMinimumRangeForBow(bow);
/*  327 */         if (Creature.getRange(performer, defender.getPosX(), defender.getPosY()) < minRange1) {
/*      */           
/*  329 */           performer.getCommunicator().sendCombatNormalMessage(defender.getNameWithGenus() + " is too close.");
/*  330 */           return true;
/*      */         } 
/*  332 */         int trees = 0;
/*  333 */         int treetilex = -1;
/*  334 */         int treetiley = -1;
/*  335 */         int tileArrowDownX = -1;
/*  336 */         int tileArrowDownY = -1;
/*  337 */         PathFinder pf = new PathFinder(true);
/*      */         
/*      */         try {
/*  340 */           Path path = pf.rayCast((performer.getCurrentTile()).tilex, (performer.getCurrentTile()).tiley, 
/*  341 */               (defender.getCurrentTile()).tilex, (defender.getCurrentTile()).tiley, performer.isOnSurface(), (
/*  342 */               (int)Creature.getRange(performer, defender.getPosX(), defender.getPosY()) >> 2) + 5);
/*  343 */           float initialHeight = Math.max(-1.4F, performer.getPositionZ() + performer.getAltOffZ() + 1.4F);
/*      */ 
/*      */           
/*  346 */           float targetHeight = Math.max(-1.4F, defender.getPositionZ() + defender.getAltOffZ() + 1.4F);
/*  347 */           double distx = Math.pow(((performer.getCurrentTile()).tilex - (defender.getCurrentTile()).tilex), 2.0D);
/*  348 */           double disty = Math.pow(((performer.getCurrentTile()).tiley - (defender.getCurrentTile()).tiley), 2.0D);
/*  349 */           double dist = Math.sqrt(distx + disty);
/*  350 */           double dx = (targetHeight - initialHeight) / dist;
/*  351 */           while (!path.isEmpty())
/*      */           {
/*  353 */             PathTile p = path.getFirst();
/*  354 */             if (Tiles.getTile(Tiles.decodeType(p.getTile())).isTree()) {
/*      */               
/*  356 */               trees++;
/*  357 */               if (treetilex == -1 && 
/*  358 */                 Server.rand.nextInt(10) < trees) {
/*      */                 
/*  360 */                 treetilex = p.getTileX();
/*  361 */                 treetiley = p.getTileY();
/*      */               } 
/*      */             } 
/*  364 */             distx = Math.pow((p.getTileX() - (defender.getCurrentTile()).tilex), 2.0D);
/*  365 */             disty = Math.pow((p.getTileY() - (defender.getCurrentTile()).tiley), 2.0D);
/*  366 */             double currdist = Math.sqrt(distx + disty);
/*      */ 
/*      */ 
/*      */             
/*  370 */             float currHeight = Math.max(-1.4F, 
/*  371 */                 Zones.getLowestCorner(p.getTileX(), p.getTileY(), performer.getLayer()));
/*      */             
/*  373 */             double distmod = currdist * dx;
/*  374 */             if (dx < 0.0D) {
/*      */ 
/*      */               
/*  377 */               if (currHeight > targetHeight - distmod)
/*      */               {
/*  379 */                 performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot.");
/*      */                 
/*  381 */                 return true;
/*      */               
/*      */               }
/*      */             
/*      */             }
/*  386 */             else if (currHeight > targetHeight - distmod) {
/*      */               
/*  388 */               performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot.");
/*      */               
/*  390 */               return true;
/*      */             } 
/*      */             
/*  393 */             if (tileArrowDownX == -1 && Server.rand.nextInt(15) == 0) {
/*      */               
/*  395 */               tileArrowDownX = p.getTileX();
/*  396 */               tileArrowDownY = p.getTileY();
/*      */             } 
/*  398 */             path.removeFirst();
/*      */           }
/*      */         
/*  401 */         } catch (NoPathException np) {
/*      */           
/*  403 */           performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot.");
/*  404 */           return true;
/*      */         } 
/*      */ 
/*      */         
/*  408 */         if (tileArrowDownX == -1)
/*      */         {
/*  410 */           if (result != null && result.getTotalCover() > 0.0F)
/*      */           {
/*  412 */             for (Blocker b : result.getBlockerArray()) {
/*      */               
/*  414 */               if (Server.rand.nextInt(100) < b.getBlockPercent(performer)) {
/*      */                 
/*  416 */                 tileArrowDownX = b.getTileX();
/*  417 */                 tileArrowDownY = b.getTileY();
/*  418 */                 if (!b.isHorizontal() && (performer.getCurrentTile()).tilex < tileArrowDownX) {
/*  419 */                   tileArrowDownX--;
/*  420 */                 } else if (b.isHorizontal() && (performer.getCurrentTile()).tiley < tileArrowDownY) {
/*  421 */                   tileArrowDownY--;
/*      */                 } 
/*      */               } 
/*      */             }  } 
/*      */         }
/*  426 */         performer.getStatus().modifyStamina(-2000.0F);
/*  427 */         byte stringql = bow.getAuxData();
/*  428 */         if (Server.rand.nextInt(Math.max(2, stringql) * 10) == 0) {
/*      */           
/*  430 */           int realTemplate = 459;
/*  431 */           if (bow.getTemplateId() == 449) {
/*  432 */             realTemplate = 461;
/*  433 */           } else if (bow.getTemplateId() == 448) {
/*  434 */             realTemplate = 460;
/*  435 */           }  bow.setTemplateId(realTemplate);
/*  436 */           bow.setAuxData((byte)0);
/*  437 */           performer.getCommunicator().sendCombatNormalMessage("The string breaks!");
/*  438 */           return true;
/*      */         } 
/*      */         
/*  441 */         Item arrow = getArrow(performer);
/*      */         
/*  443 */         if (arrow == null) {
/*      */           
/*  445 */           performer.getCommunicator().sendCombatNormalMessage("You have no arrows left to shoot!");
/*  446 */           return true;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  452 */           arrow.getParent().dropItem(arrow.getWurmId(), false);
/*      */         }
/*  454 */         catch (NoSuchItemException nsi) {
/*      */           
/*  456 */           Items.destroyItem(arrow.getWurmId());
/*  457 */           performer.getCommunicator().sendCombatNormalMessage("You have no arrows left to shoot!");
/*  458 */           return true;
/*      */         } 
/*      */         
/*  461 */         performer.setSecondsToLogout(300);
/*  462 */         if (!defender.isPlayer()) {
/*  463 */           performer.setSecondsToLogout(180);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  470 */         Arrows.addToHitCreature(arrow, performer, defender, counter, act, trees, bow, bowskill, archery, isAttackingPenned, tileArrowDownX, tileArrowDownY, treetilex, treetiley, fence, limitFail);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  476 */       performer.getCommunicator().sendCombatNormalMessage("You can't shoot with that.");
/*  477 */       done = true;
/*      */     } 
/*  479 */     return done;
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
/*      */   static int getMinimumRangeForBow(int bowTemplateId) {
/*  491 */     int minRange = 4;
/*  492 */     if (bowTemplateId == 449) {
/*  493 */       minRange = 40;
/*  494 */     } else if (bowTemplateId == 448) {
/*  495 */       minRange = 20;
/*  496 */     } else if (bowTemplateId == 447) {
/*  497 */       minRange = 4;
/*  498 */     } else if (bowTemplateId == 450) {
/*  499 */       minRange = 20;
/*  500 */     }  return minRange;
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
/*      */   static int getMinimumRangeForBow(Item bow) {
/*  512 */     assert bow != null : "Bow was null when trying to get minimum range";
/*      */     
/*  514 */     return getMinimumRangeForBow(bow.getTemplateId());
/*      */   }
/*      */ 
/*      */   
/*      */   public static final float getRarityArrowModifier(Item arrow) {
/*  519 */     return 1.0F + arrow.getRarity() * 0.3F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final float getRarityBowModifier(Item bow) {
/*  524 */     return 1.0F + bow.getRarity() * 0.05F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final double getDamage(Creature performer, Creature defender, Item bow, Item arrow, Skill archery) {
/*  532 */     double damage = ((bow.getDamagePercent() * arrow.getCurrentQualityLevel() + bow.getDamagePercent() * bow.getCurrentQualityLevel()) / 2.0F) + archery.getKnowledge(0.0D) * bow.getDamagePercent();
/*  533 */     if (!Servers.isThisAnEpicOrChallengeServer()) {
/*      */       
/*  535 */       damage += (bow.getSpellExtraDamageBonus() / 5.0F);
/*  536 */       damage += arrow.getSpellExtraDamageBonus();
/*      */     } 
/*  538 */     damage += getRarityArrowModifier(arrow);
/*  539 */     damage += getRarityBowModifier(bow);
/*      */     
/*  541 */     damage *= 1.0D + (performer.getStrengthSkill() - 20.0D) / 100.0D;
/*  542 */     if (Servers.isThisAnEpicOrChallengeServer()) {
/*      */       
/*  544 */       damage *= (1.0F + bow.getSpellExtraDamageBonus() / 30000.0F);
/*  545 */       damage *= (1.0F + arrow.getSpellExtraDamageBonus() / 30000.0F);
/*      */     } 
/*      */     
/*  548 */     if (defender != null) {
/*      */ 
/*      */       
/*  551 */       float heightDiff = Math.max(-1.4F, performer.getPositionZ() + performer.getAltOffZ()) - Math.max(-1.4F, defender.getPositionZ() + defender.getAltOffZ());
/*  552 */       float heightDamEffect = Math.max(-0.2F, Math.min(0.2F, heightDiff / 100.0F));
/*  553 */       damage *= (1.0F + heightDamEffect);
/*  554 */       if (Math.max(-1.4F, defender.getPositionZ()) + defender.getCentimetersHigh() / 100.0F < -1.0F) {
/*  555 */         damage *= 0.5D;
/*      */       }
/*      */     } 
/*  558 */     if (performer.getFightlevel() >= 1)
/*  559 */       damage *= 1.2000000476837158D; 
/*  560 */     if (performer.getFightlevel() >= 4)
/*  561 */       damage *= 1.2000000476837158D; 
/*  562 */     if (arrow.getTemplateId() == 456) {
/*  563 */       damage *= 1.2000000476837158D;
/*  564 */     } else if (arrow.getTemplateId() == 454) {
/*  565 */       damage *= 0.20000000298023224D;
/*  566 */     }  return damage;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean throwItem(Creature performer, Creature defender, Item thrown, Action act) {
/*  571 */     if (defender.isInvulnerable()) {
/*      */       
/*  573 */       performer.getCommunicator().sendCombatNormalMessage("You can't attack " + defender.getNameWithGenus() + " right now.");
/*  574 */       return true;
/*      */     } 
/*  576 */     if (performer.getPositionZ() < -1.0F && performer.getVehicle() == -10L) {
/*      */       
/*  578 */       performer.getCommunicator().sendCombatNormalMessage("You are too deep in the water to throw anything effectively.");
/*  579 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  583 */     if (thrown.isBodyPartAttached()) {
/*      */       
/*  585 */       performer.getCommunicator().sendCombatNormalMessage("You need to wield a weapon to throw.");
/*  586 */       return true;
/*      */     } 
/*  588 */     if (thrown.isNoDrop()) {
/*      */       
/*  590 */       performer.getCommunicator().sendCombatNormalMessage("You are not allowed to drop that.");
/*  591 */       return true;
/*      */     } 
/*  593 */     if (thrown.isArtifact()) {
/*      */       
/*  595 */       performer.getCommunicator().sendCombatNormalMessage("You can't bring yourself to let go.");
/*  596 */       return true;
/*      */     } 
/*  598 */     if (defender.isDead()) {
/*  599 */       return true;
/*      */     }
/*  601 */     Skill weaponSkill = null;
/*      */     
/*      */     try {
/*  604 */       int skillnum = thrown.getPrimarySkill();
/*  605 */       if (skillnum != -10L) {
/*      */         try
/*      */         {
/*      */           
/*  609 */           weaponSkill = performer.getSkills().getSkill(skillnum);
/*      */         }
/*  611 */         catch (NoSuchSkillException nss)
/*      */         {
/*  613 */           weaponSkill = performer.getSkills().learn(skillnum, 1.0F);
/*      */         }
/*      */       
/*      */       }
/*  617 */     } catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  622 */     if (performer.isOnSurface() != defender.isOnSurface()) {
/*      */       
/*  624 */       performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear view of " + defender.getNameWithGenus() + ".");
/*  625 */       return true;
/*      */     } 
/*  627 */     if (WurmCalendar.getHour() > 20 || WurmCalendar.getHour() < 5)
/*  628 */       performer.getCommunicator().sendCombatNormalMessage("The dusk makes it harder to get a clear view of " + defender
/*  629 */           .getNameWithGenus() + "."); 
/*  630 */     Zones.resetCoverHolder();
/*  631 */     if (performer.isOnSurface())
/*      */     {
/*  633 */       if (!performer.isWithinDistanceTo(defender, 110.0F))
/*      */       {
/*  635 */         if (defender.getCurrentTile() != null && defender.getCurrentTile().getStructure() != null && defender
/*  636 */           .getCurrentTile().getStructure().isFinalFinished()) {
/*      */           
/*  638 */           performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear view of " + defender
/*  639 */               .getNameWithGenus() + " inside the structure.");
/*  640 */           return true;
/*      */         } 
/*      */       }
/*      */     }
/*      */     
/*  645 */     BlockingResult result = Blocking.getRangedBlockerBetween(performer, defender);
/*      */     
/*  647 */     if (result != null) {
/*      */       
/*  649 */       if (!performer.isOnPvPServer() || !defender.isOnPvPServer()) {
/*      */ 
/*      */         
/*  652 */         performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot of " + defender
/*  653 */             .getNameWithGenus() + ".");
/*  654 */         return true;
/*      */       } 
/*  656 */       for (Blocker b : result.getBlockerArray()) {
/*      */         
/*  658 */         if (b.getBlockPercent(performer) >= 100.0F) {
/*      */           
/*  660 */           performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot of " + defender
/*  661 */               .getNameWithGenus() + ".");
/*  662 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*  666 */     if (!VirtualZone.isCreatureTurnedTowardsTarget(defender, performer, 60.0F, false)) {
/*      */       
/*  668 */       performer.getCommunicator().sendCombatNormalMessage("You must turn towards " + defender
/*  669 */           .getNameWithGenus() + " in order to throw at it.");
/*  670 */       return true;
/*      */     } 
/*      */     
/*  673 */     double diff = Creature.getRange(performer, defender.getPosX(), defender.getPosY());
/*  674 */     if (diff < 2.0D) {
/*      */       
/*  676 */       performer.getCommunicator().sendCombatNormalMessage(defender.getNameWithGenus() + " is too close.");
/*  677 */       return true;
/*      */     } 
/*  679 */     if (diff * Math.max(1, thrown.getWeightGrams() / 5000) > performer.getStrengthSkill()) {
/*      */       
/*  681 */       performer.getCommunicator().sendCombatNormalMessage(defender.getNameWithGenus() + " is too far away.");
/*  682 */       return true;
/*      */     } 
/*  684 */     if ((thrown.getWeightGrams() / 1000.0F) > performer.getStrengthSkill()) {
/*      */       
/*  686 */       performer.getCommunicator().sendCombatNormalMessage(defender.getNameWithGenus() + " is too heavy.");
/*  687 */       return true;
/*      */     } 
/*  689 */     float bon = thrown.getSpellNimbleness();
/*  690 */     if (bon > 0.0F)
/*  691 */       diff -= (bon / 10.0F); 
/*  692 */     int trees = 0;
/*  693 */     int treetilex = -1;
/*  694 */     int treetiley = -1;
/*  695 */     int tileArrowDownX = -1;
/*  696 */     int tileArrowDownY = -1;
/*  697 */     if (performer.getCurrentTile() != defender.getCurrentTile()) {
/*      */       
/*  699 */       PathFinder pf = new PathFinder(true);
/*      */       
/*      */       try {
/*  702 */         Path path = pf.rayCast((performer.getCurrentTile()).tilex, (performer.getCurrentTile()).tiley, 
/*  703 */             (defender.getCurrentTile()).tilex, (defender.getCurrentTile()).tiley, performer.isOnSurface(), (
/*  704 */             (int)Creature.getRange(performer, defender.getPosX(), defender.getPosY()) >> 2) + 5);
/*  705 */         while (!path.isEmpty())
/*      */         {
/*  707 */           PathTile p = path.getFirst();
/*      */           
/*  709 */           if (Tiles.getTile(Tiles.decodeType(p.getTile())).isTree()) {
/*      */             
/*  711 */             trees++;
/*  712 */             if (treetilex == -1 && 
/*  713 */               Server.rand.nextInt(10) > trees) {
/*      */               
/*  715 */               treetilex = p.getTileX();
/*  716 */               treetiley = p.getTileY();
/*      */             } 
/*      */           } 
/*  719 */           if (tileArrowDownX == -1 && Server.rand.nextInt(15) == 0) {
/*      */             
/*  721 */             tileArrowDownX = p.getTileX();
/*  722 */             tileArrowDownY = p.getTileY();
/*      */           } 
/*  724 */           path.removeFirst();
/*      */         }
/*      */       
/*  727 */       } catch (NoPathException np) {
/*      */         
/*  729 */         performer.getCommunicator().sendCombatNormalMessage("You fail to get a clear shot.");
/*  730 */         return true;
/*      */       } 
/*      */       
/*  733 */       if (tileArrowDownX == -1)
/*      */       {
/*  735 */         if (result != null && result.getTotalCover() > 0.0F)
/*      */         {
/*  737 */           for (Blocker b : result.getBlockerArray()) {
/*      */             
/*  739 */             if (Server.rand.nextInt(100) < b.getBlockPercent(performer)) {
/*      */               
/*  741 */               tileArrowDownX = b.getTileX();
/*  742 */               tileArrowDownY = b.getTileY();
/*  743 */               if (!b.isHorizontal() && (performer.getCurrentTile()).tilex < tileArrowDownX) {
/*  744 */                 tileArrowDownX--;
/*  745 */               } else if (b.isHorizontal() && (performer.getCurrentTile()).tiley < tileArrowDownY) {
/*  746 */                 tileArrowDownY--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/*      */     try {
/*  755 */       thrown.getParent().dropItem(thrown.getWurmId(), false);
/*      */     }
/*  757 */     catch (NoSuchItemException nsi) {
/*      */       
/*  759 */       return true;
/*      */     } 
/*  761 */     performer.setStealth(false);
/*  762 */     if (WurmCalendar.getHour() > 19) {
/*  763 */       diff += (WurmCalendar.getHour() - 19);
/*  764 */     } else if (WurmCalendar.getHour() < 6) {
/*  765 */       diff += (6 - WurmCalendar.getHour());
/*  766 */     }  diff += trees;
/*  767 */     diff += Zones.getCoverHolder();
/*  768 */     if (defender.isMoving())
/*  769 */       diff += 10.0D; 
/*  770 */     if (Math.max(0.0F, defender.getPositionZ()) + defender.getCentimetersHigh() / 100.0F < -1.0F)
/*  771 */       diff += 40.0D; 
/*  772 */     performer.setSecondsToLogout(300);
/*      */     
/*  774 */     Server.getInstance().broadCastAction(performer
/*  775 */         .getName() + " throws " + performer.getHisHerItsString() + " " + thrown.getName() + ".", performer, 5);
/*  776 */     performer.getCommunicator().sendCombatNormalMessage("You throw the " + thrown.getName() + ".");
/*  777 */     performer.getStatus().modifyStamina(-5000.0F);
/*      */     
/*      */     try {
/*  780 */       Skill bcontrol = defender.getSkills().getSkill(104);
/*  781 */       diff += bcontrol.getKnowledge(0.0D) / 5.0D;
/*      */     }
/*  783 */     catch (NoSuchSkillException nss) {
/*      */       
/*  785 */       logger.log(Level.WARNING, defender.getWurmId() + ", " + defender.getName() + " no body control.");
/*      */     } 
/*      */     
/*  788 */     diff *= 1.0D - (performer.getMovementScheme()).armourMod.getModifier();
/*      */     
/*  790 */     double power = 0.0D;
/*  791 */     if (weaponSkill != null) {
/*  792 */       power = weaponSkill.skillCheck(diff, thrown, thrown.getCurrentQualityLevel(), true, 1.0F);
/*      */     } else {
/*  794 */       power = performer.getBodyControl() - Server.rand.nextInt(100);
/*  795 */     }  double defCheck = 0.0D;
/*  796 */     boolean parriedShield = false;
/*  797 */     if (power > 0.0D) {
/*      */       
/*  799 */       Item defShield = defender.getShield();
/*  800 */       if (defShield != null)
/*      */       {
/*  802 */         if (defender.getStatus().getStamina() >= 300)
/*      */         {
/*  804 */           if (willParryWithShield(performer, defender)) {
/*      */             
/*  806 */             Skill defShieldSkill = null;
/*  807 */             Skills defenderSkills = defender.getSkills();
/*      */             
/*  809 */             int skillnum = -10;
/*      */ 
/*      */             
/*      */             try {
/*  813 */               skillnum = defShield.getPrimarySkill();
/*  814 */               defShieldSkill = defenderSkills.getSkill(skillnum);
/*      */             }
/*  816 */             catch (NoSuchSkillException nss) {
/*      */               
/*  818 */               if (skillnum != -10)
/*  819 */                 defShieldSkill = defenderSkills.learn(skillnum, 1.0F); 
/*      */             } 
/*  821 */             if (defShieldSkill != null) {
/*      */               
/*  823 */               defShieldSkill.skillCheck(20.0D + (defender.isMoving() ? power : Math.max(1.0D, power - 20.0D)), defShield, 0.0D, false, 1.0F);
/*      */ 
/*      */               
/*  826 */               defCheck = defShieldSkill.getKnowledge(defShield, defender.isMoving() ? -20.0D : 0.0D) - (Server.rand.nextFloat() * 115.0F);
/*      */             } 
/*  828 */             defCheck += ((defShield.getSizeY() + defShield.getSizeZ()) / 3.0F);
/*  829 */             if (defCheck > 0.0D)
/*  830 */               parriedShield = true; 
/*      */           } 
/*      */         }
/*      */       }
/*  834 */       defender.addAttacker(performer);
/*  835 */       if (defender.isFriendlyKingdom(performer.getKingdomId()))
/*      */       {
/*  837 */         if (defender.isDominated()) {
/*      */           
/*  839 */           if (!performer.hasBeenAttackedBy(defender.getWurmId()) && 
/*  840 */             !performer.hasBeenAttackedBy(defender.dominator))
/*      */           {
/*  842 */             performer.setUnmotivatedAttacker();
/*      */           }
/*      */         }
/*  845 */         else if (defender.isRidden()) {
/*      */           
/*  847 */           if (performer.getCitizenVillage() == null || defender.getCurrentVillage() != performer.getCitizenVillage())
/*      */           {
/*  849 */             for (Long riderLong : defender.getRiders())
/*      */             {
/*      */               
/*      */               try {
/*  853 */                 Creature rider = Server.getInstance().getCreature(riderLong.longValue());
/*  854 */                 rider.addAttacker(performer);
/*      */               }
/*  856 */               catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */               
/*      */               }
/*  860 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/*      */         }
/*  867 */         else if (!performer.hasBeenAttackedBy(defender.getWurmId())) {
/*      */           
/*  869 */           if (performer.getCitizenVillage() == null || !performer.getCitizenVillage().isEnemy(defender))
/*  870 */             performer.setUnmotivatedAttacker(); 
/*      */         } 
/*      */       }
/*  873 */       power = Math.max(power, 5.0D);
/*  874 */       byte pos = getWoundPos(defender, act.getNumber());
/*  875 */       pos = (byte)CombatEngine.getRealPosition(pos);
/*  876 */       float armourMod = defender.getArmourMod();
/*  877 */       float evasionChance = 0.0F;
/*  878 */       double damage = (Weapon.getBaseDamageForWeapon(thrown) * thrown.getCurrentQualityLevel() * 10.0F);
/*  879 */       damage *= 1.0D + (performer.getStrengthSkill() - 20.0D) / 100.0D;
/*  880 */       if (performer.getFightlevel() >= 1)
/*  881 */         damage *= 1.5D; 
/*  882 */       if (performer.getFightlevel() >= 4)
/*  883 */         damage *= 1.5D; 
/*  884 */       if (thrown.isLiquid())
/*  885 */         damage = ((Math.max(0, (Math.min(1500, thrown.getTemperature()) - 800) / 200) * thrown.getWeightGrams()) / 30.0F); 
/*  886 */       if (defCheck > 0.0D) {
/*      */         
/*  888 */         performer.getCommunicator().sendCombatNormalMessage("Your " + thrown
/*  889 */             .getName() + " glances off " + defender.getNameWithGenus() + "'s shield.");
/*  890 */         defender.getCommunicator().sendCombatSafeMessage("You instinctively block the " + thrown
/*  891 */             .getName() + " with your shield.");
/*  892 */         if (damage > 500.0D) {
/*  893 */           defender.getStatus().modifyStamina(-300.0F);
/*      */         }
/*  895 */       } else if (armourMod == 1.0F || defender.isVehicle() || defender.isKingdomGuard()) {
/*      */ 
/*      */         
/*      */         try {
/*  899 */           byte bodyPosition = ArmourTemplate.getArmourPosition(pos);
/*  900 */           Item armour = defender.getArmour(bodyPosition);
/*  901 */           if (!defender.isKingdomGuard()) {
/*  902 */             armourMod = ArmourTemplate.calculateDR(armour, (byte)2);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  907 */             armourMod *= ArmourTemplate.calculateDR(armour, (byte)2);
/*      */           } 
/*      */ 
/*      */           
/*  911 */           armour.setDamage(armour.getDamage() + (float)(damage * armourMod / 30000.0D) * armour.getDamageModifier() * 
/*  912 */               ArmourTemplate.getArmourDamageModFor(armour, (byte)2));
/*  913 */           CombatEngine.checkEnchantDestruction(thrown, armour, defender);
/*  914 */           if (defender.getBonusForSpellEffect((byte)22) > 0.0F)
/*      */           {
/*  916 */             if (armourMod >= 1.0F) {
/*  917 */               armourMod = 0.2F + (1.0F - defender.getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F;
/*      */             } else {
/*  919 */               armourMod = Math.min(armourMod, 0.2F + (1.0F - defender
/*  920 */                   .getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F);
/*      */             } 
/*      */           }
/*  923 */         } catch (NoArmourException nsi) {
/*      */ 
/*      */           
/*  926 */           evasionChance = 1.0F - defender.getArmourMod();
/*      */         }
/*  928 */         catch (NoSpaceException nsp) {
/*      */           
/*  930 */           logger.log(Level.WARNING, defender.getName() + " no armour space on loc " + pos);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  937 */       if (!(defender instanceof com.wurmonline.server.players.Player))
/*      */       {
/*  939 */         if (!performer.isInvulnerable())
/*  940 */           defender.setTarget(performer.getWurmId(), false); 
/*      */       }
/*  942 */       if (defender.isUnique()) {
/*      */         
/*  944 */         evasionChance = 0.5F;
/*  945 */         damage *= armourMod;
/*      */       } 
/*  947 */       boolean dropattile = false;
/*  948 */       if (thrown.getTemplateId() == 105 || thrown.getTemplateId() == 116)
/*  949 */         performer.achievement(138); 
/*  950 */       if (defCheck > 0.0D) {
/*      */         
/*  952 */         dropattile = true;
/*      */       }
/*  954 */       else if (Server.rand.nextFloat() < evasionChance) {
/*      */         
/*  956 */         dropattile = true;
/*  957 */         performer.getCommunicator().sendCombatNormalMessage("Your " + thrown
/*  958 */             .getName() + " glances off " + defender.getNameWithGenus() + "'s armour.");
/*  959 */         defender.getCommunicator().sendCombatSafeMessage(
/*  960 */             LoginHandler.raiseFirstLetter(thrown.getNameWithGenus()) + " hits you on the " + defender
/*  961 */             .getBody().getWoundLocationString(pos) + " but glances off your armour.");
/*      */       }
/*  963 */       else if (damage > 500.0D) {
/*      */         
/*  965 */         hit(defender, performer, thrown, null, damage, 1.0F, armourMod, pos, true, false, 0.0D, 0.0D);
/*  966 */         performer.achievement(41);
/*      */       }
/*      */       else {
/*      */         
/*  970 */         dropattile = true;
/*  971 */         performer.getCommunicator().sendCombatNormalMessage("Your " + thrown
/*  972 */             .getName() + " glances off " + defender.getNameWithGenus() + " and does no damage.");
/*  973 */         defender.getCommunicator().sendCombatSafeMessage(
/*  974 */             LoginHandler.raiseFirstLetter(thrown.getNameWithGenus()) + " hits you on the " + defender
/*  975 */             .getBody().getWoundLocationString(pos) + " but does no damage.");
/*      */       } 
/*  977 */       float fullDamage = thrown.getDamage() + 5.0F * Server.rand.nextFloat();
/*  978 */       if (dropattile) {
/*      */         
/*  980 */         if (thrown.isLiquid()) {
/*      */           
/*  982 */           Items.destroyItem(thrown.getWurmId());
/*  983 */           return true;
/*      */         } 
/*  985 */         if (defCheck > 0.0D && parriedShield) {
/*      */           
/*  987 */           tileArrowDownX = (defender.getCurrentTile()).tilex;
/*  988 */           tileArrowDownY = (defender.getCurrentTile()).tiley;
/*      */           
/*  990 */           if (defShield.isWood()) {
/*  991 */             SoundPlayer.playSound("sound.arrow.hit.wood", defender, 1.6F);
/*  992 */           } else if (defShield.isMetal()) {
/*  993 */             SoundPlayer.playSound("sound.arrow.hit.metal", defender, 1.6F);
/*      */           } 
/*  995 */         }  if (!thrown.isIndestructible() && Server.rand
/*  996 */           .nextInt(Math.max(1, (int)thrown.getCurrentQualityLevel())) < 2) {
/*      */           
/*  998 */           for (Item item : thrown.getAllItems(false)) {
/*      */             
/*      */             try {
/* 1001 */               Zone z = Zones.getZone((defender.getCurrentTile()).tilex, (defender.getCurrentTile()).tiley, performer
/* 1002 */                   .isOnSurface());
/* 1003 */               VolaTile t = z.getOrCreateTile((defender.getCurrentTile()).tilex, 
/* 1004 */                   (defender.getCurrentTile()).tiley);
/* 1005 */               t.addItem(item, false, false);
/*      */             }
/* 1007 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 1009 */               performer.getCommunicator().sendCombatNormalMessage("The " + item
/* 1010 */                   .getName() + " disappears from your view.");
/* 1011 */               Items.destroyItem(item.getWurmId());
/*      */             } 
/* 1013 */           }  performer.getCommunicator().sendCombatNormalMessage("The " + thrown.getName() + " breaks.");
/* 1014 */           Items.destroyItem(thrown.getWurmId());
/*      */         }
/* 1016 */         else if (fullDamage < 100.0F || thrown.isIndestructible() || thrown.isLocked()) {
/*      */           
/* 1018 */           tileArrowDownX = (defender.getCurrentTile()).tilex;
/* 1019 */           tileArrowDownY = (defender.getCurrentTile()).tiley;
/*      */           
/*      */           try {
/* 1022 */             Zone z = Zones.getZone(tileArrowDownX, tileArrowDownY, performer.isOnSurface());
/* 1023 */             VolaTile t = z.getOrCreateTile(tileArrowDownX, tileArrowDownY);
/* 1024 */             t.addItem(thrown, false, false);
/*      */           }
/* 1026 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1028 */             performer.getCommunicator().sendCombatNormalMessage("The " + thrown
/* 1029 */                 .getName() + " disappears from your view.");
/* 1030 */             Items.destroyItem(thrown.getWurmId());
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1035 */           Items.destroyItem(thrown.getWurmId());
/* 1036 */           performer.getCommunicator().sendCombatNormalMessage("The " + thrown.getName() + " breaks.");
/* 1037 */           for (Item item : thrown.getAllItems(false)) {
/*      */             
/*      */             try {
/* 1040 */               Zone z = Zones.getZone((defender.getCurrentTile()).tilex, (defender.getCurrentTile()).tiley, performer
/* 1041 */                   .isOnSurface());
/* 1042 */               VolaTile t = z.getOrCreateTile((defender.getCurrentTile()).tilex, 
/* 1043 */                   (defender.getCurrentTile()).tiley);
/* 1044 */               t.addItem(item, false, false);
/*      */             }
/* 1046 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 1048 */               performer.getCommunicator().sendCombatNormalMessage("The " + item
/* 1049 */                   .getName() + " disappears from your view.");
/* 1050 */               Items.destroyItem(item.getWurmId());
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1057 */       if (thrown.isLiquid()) {
/*      */         
/* 1059 */         Items.destroyItem(thrown.getWurmId());
/* 1060 */         return true;
/*      */       } 
/* 1062 */       float fullDamage = thrown.getDamage() + 5.0F * Server.rand.nextFloat();
/* 1063 */       if (!thrown.isIndestructible() && !thrown.isLocked() && Server.rand
/* 1064 */         .nextInt(Math.max(1, (int)thrown.getCurrentQualityLevel())) < 2) {
/*      */         
/* 1066 */         performer.getCommunicator().sendCombatNormalMessage("The " + thrown.getName() + " breaks.");
/* 1067 */         for (Item item : thrown.getAllItems(false)) {
/*      */           
/*      */           try {
/* 1070 */             Zone z = Zones.getZone((defender.getCurrentTile()).tilex, (defender.getCurrentTile()).tiley, performer
/* 1071 */                 .isOnSurface());
/* 1072 */             VolaTile t = z.getOrCreateTile((defender.getCurrentTile()).tilex, 
/* 1073 */                 (defender.getCurrentTile()).tiley);
/* 1074 */             t.addItem(item, false, false);
/*      */           }
/* 1076 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1078 */             performer.getCommunicator().sendCombatNormalMessage("The " + item
/* 1079 */                 .getName() + " disappears from your view.");
/* 1080 */             Items.destroyItem(item.getWurmId());
/*      */           } 
/* 1082 */         }  Items.destroyItem(thrown.getWurmId());
/*      */       }
/* 1084 */       else if (fullDamage >= 100.0F && !thrown.isIndestructible() && !thrown.isLocked()) {
/*      */         
/* 1086 */         for (Item item : thrown.getAllItems(false)) {
/*      */           
/*      */           try {
/* 1089 */             Zone z = Zones.getZone((defender.getCurrentTile()).tilex, (defender.getCurrentTile()).tiley, performer
/* 1090 */                 .isOnSurface());
/* 1091 */             VolaTile t = z.getOrCreateTile((defender.getCurrentTile()).tilex, 
/* 1092 */                 (defender.getCurrentTile()).tiley);
/* 1093 */             t.addItem(item, false, false);
/*      */           }
/* 1095 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 1097 */             performer.getCommunicator().sendCombatNormalMessage("The " + item
/* 1098 */                 .getName() + " disappears from your view.");
/* 1099 */             Items.destroyItem(item.getWurmId());
/*      */           } 
/* 1101 */         }  thrown.setDamage(fullDamage);
/* 1102 */         performer.getCommunicator().sendCombatNormalMessage("The " + thrown.getName() + " breaks.");
/*      */       }
/*      */       else {
/*      */         
/* 1106 */         if (trees > 0)
/*      */         {
/* 1108 */           if (treetilex > 0) {
/*      */             
/* 1110 */             tileArrowDownX = treetilex;
/* 1111 */             tileArrowDownY = treetiley;
/*      */           } 
/*      */         }
/* 1114 */         boolean hitdef = false;
/* 1115 */         if (tileArrowDownX == -1) {
/*      */           
/* 1117 */           if (defender.opponent != null && performer.getKingdomId() == defender.opponent.getKingdomId())
/*      */           {
/* 1119 */             if (power < -20.0D && Server.rand.nextInt(100) < Math.abs(power))
/*      */             {
/* 1121 */               if (defender.opponent.isPlayer() && defender.opponent != performer)
/*      */               {
/* 1123 */                 if (performer.getFightlevel() < 1) {
/*      */                   
/* 1125 */                   byte pos = getWoundPos(defender.opponent, act.getNumber());
/* 1126 */                   pos = (byte)CombatEngine.getRealPosition(pos);
/* 1127 */                   float armourMod = defender.opponent.getArmourMod();
/* 1128 */                   double damage = (Weapon.getBaseDamageForWeapon(thrown) * thrown.getCurrentQualityLevel() * 10.0F);
/*      */                   
/* 1130 */                   damage *= 1.0D + (performer.getStrengthSkill() - 20.0D) / 100.0D;
/* 1131 */                   damage *= (1.0F + thrown.getCurrentQualityLevel() / 100.0F);
/*      */                   
/* 1133 */                   if (armourMod == 1.0F) {
/*      */                     
/*      */                     try {
/*      */                       
/* 1137 */                       byte bodyPosition = ArmourTemplate.getArmourPosition(pos);
/* 1138 */                       Item armour = defender.opponent.getArmour(bodyPosition);
/* 1139 */                       armourMod = ArmourTemplate.calculateDR(armour, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 1144 */                       armour.setDamage(armour.getDamage() + (float)(damage * armourMod / 50000.0D) * armour
/* 1145 */                           .getDamageModifier() * 
/* 1146 */                           ArmourTemplate.getArmourDamageModFor(armour, (byte)2));
/* 1147 */                       CombatEngine.checkEnchantDestruction(thrown, armour, defender.opponent);
/*      */                     }
/* 1149 */                     catch (NoArmourException noArmourException) {
/*      */ 
/*      */                     
/*      */                     }
/* 1153 */                     catch (NoSpaceException nsp) {
/*      */                       
/* 1155 */                       logger.log(Level.WARNING, defender.getName() + " no armour space on loc " + pos);
/*      */                     } 
/*      */                   }
/* 1158 */                   hit(defender.opponent, performer, thrown, null, damage, 1.0F, armourMod, pos, false, false, 0.0D, 0.0D);
/*      */                   
/* 1160 */                   hitdef = true;
/*      */                 } 
/*      */               }
/*      */             }
/*      */           }
/* 1165 */           if (!hitdef) {
/*      */             
/* 1167 */             tileArrowDownX = (defender.getCurrentTile()).tilex;
/* 1168 */             tileArrowDownY = (defender.getCurrentTile()).tiley;
/*      */           } 
/*      */         } 
/* 1171 */         if (!hitdef) {
/*      */           
/*      */           try {
/*      */             
/* 1175 */             Zone z = Zones.getZone(tileArrowDownX, tileArrowDownY, performer.isOnSurface());
/* 1176 */             VolaTile t = z.getOrCreateTile(tileArrowDownX, tileArrowDownY);
/* 1177 */             t.addItem(thrown, false, false);
/* 1178 */             if (treetilex > 0) {
/*      */               
/* 1180 */               SoundPlayer.playSound("sound.arrow.stuck.wood", tileArrowDownX, tileArrowDownY, performer.isOnSurface(), 0.0F);
/*      */             
/*      */             }
/* 1183 */             else if (result != null) {
/*      */               
/* 1185 */               if (result.getFirstBlocker() != null)
/*      */               {
/* 1187 */                 if (result.getFirstBlocker().isFence()) {
/* 1188 */                   SoundPlayer.playSound("sound.work.masonry", tileArrowDownX, tileArrowDownY, performer
/* 1189 */                       .isOnSurface(), 0.0F);
/*      */                 } else {
/* 1191 */                   SoundPlayer.playSound("sound.arrow.stuck.wood", tileArrowDownX, tileArrowDownY, performer
/* 1192 */                       .isOnSurface(), 0.0F);
/*      */                 } 
/*      */               }
/*      */             } else {
/*      */               
/* 1197 */               SoundPlayer.playSound("sound.arrow.stuck.ground", tileArrowDownX, tileArrowDownY, performer.isOnSurface(), 0.0F);
/*      */             }
/*      */           
/* 1200 */           } catch (NoSuchZoneException nsz) {
/*      */             
/* 1202 */             Items.destroyItem(thrown.getWurmId());
/* 1203 */             performer.getCommunicator().sendCombatNormalMessage("The " + thrown
/* 1204 */                 .getName() + " disappears from your view");
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1209 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hit(Creature defender, Creature performer, Item arrow, @Nullable Item bow, double damage, float damMod, float armourMod, byte pos, boolean intentional, boolean dryRun, double diff, double bonus) {
/* 1216 */     if (defender.isDead())
/* 1217 */       return true; 
/* 1218 */     boolean done = false;
/*      */     
/* 1220 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1221 */     segments.add(new CreatureLineSegment(performer));
/* 1222 */     segments.add(new MulticolorLineSegment("'s ", (byte)7));
/* 1223 */     segments.add(new MulticolorLineSegment(" " + arrow.getName() + " hits ", (byte)7));
/* 1224 */     segments.add(new CreatureLineSegment(defender));
/* 1225 */     segments.add(new MulticolorLineSegment(" in the " + defender.getBody().getWoundLocationString(pos) + ".", (byte)7));
/*      */     
/* 1227 */     defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */     
/* 1229 */     ((MulticolorLineSegment)segments.get(1)).setText("r");
/* 1230 */     for (MulticolorLineSegment s : segments) {
/* 1231 */       s.setColor((byte)3);
/*      */     }
/* 1233 */     performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1243 */     Battle battle = performer.getBattle();
/* 1244 */     float spdamb = arrow.getSpellDamageBonus();
/* 1245 */     float rotdam = arrow.getWeaponSpellDamageBonus();
/* 1246 */     if (!arrow.isIndestructible() && (arrow
/* 1247 */       .isLiquid() || Server.rand.nextInt(Math.max(1, (int)arrow.getCurrentQualityLevel())) < 2)) {
/*      */       
/* 1249 */       Items.destroyItem(arrow.getWurmId());
/* 1250 */       defender.getCommunicator().sendNormalServerMessage("The " + arrow.getName() + " breaks!");
/* 1251 */       performer.getCommunicator().sendNormalServerMessage("The " + arrow.getName() + " breaks!");
/*      */     }
/* 1253 */     else if (!arrow.setDamage(arrow.getDamage() + 5.0F * damMod)) {
/*      */       
/* 1255 */       defender.getInventory().insertItem(arrow, true);
/* 1256 */       arrow.setBusy(false);
/*      */     } 
/*      */     
/* 1259 */     damage = Math.min(1.2D * damage, 
/* 1260 */         Math.max(0.800000011920929D * damage, (Server.rand.nextFloat() + Server.rand.nextFloat()) * damage));
/* 1261 */     float ms = arrow.getSpellMindStealModifier();
/* 1262 */     if (ms > 0.0F && !defender.isPlayer() && defender.getKingdomId() != performer.getKingdomId()) {
/*      */       
/* 1264 */       Skills s = defender.getSkills();
/* 1265 */       int r = Server.rand.nextInt((s.getSkills()).length);
/* 1266 */       Skill toSteal = s.getSkills()[r];
/* 1267 */       double mod = 1.0D;
/* 1268 */       if (damage < 5000.0D)
/* 1269 */         mod = damage / 5000.0D; 
/* 1270 */       double skillStolen = (ms / 100.0F * 0.1F) * mod;
/*      */ 
/*      */       
/*      */       try {
/* 1274 */         Skill owned = defender.getSkills().getSkill(toSteal.getNumber());
/* 1275 */         if (owned.getKnowledge() < toSteal.getKnowledge())
/*      */         {
/* 1277 */           toSteal.setKnowledge(toSteal.getKnowledge() - skillStolen / 10.0D, false);
/* 1278 */           owned.setKnowledge(owned.getKnowledge() + skillStolen / 10.0D, false);
/* 1279 */           performer.getCommunicator().sendSafeServerMessage("The " + arrow
/* 1280 */               .getName() + " steals some " + toSteal.getName() + ".");
/*      */         }
/*      */       
/* 1283 */       } catch (NoSuchSkillException noSuchSkillException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1291 */     byte wtype = 2;
/* 1292 */     if (arrow.isLiquid())
/* 1293 */       wtype = 4; 
/* 1294 */     boolean dead = false;
/* 1295 */     float poisdam = arrow.getSpellVenomBonus();
/* 1296 */     if (poisdam > 0.0F) {
/*      */       
/* 1298 */       float half = Math.max(1.0F, poisdam / 2.0F);
/* 1299 */       poisdam = half + Server.rand.nextInt((int)half);
/* 1300 */       wtype = 5;
/* 1301 */       damage *= 0.800000011920929D;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1309 */     if (bow != null && damage * armourMod > 500.0D)
/*      */     {
/* 1311 */       if (bow.isWeaponBow()) {
/*      */         
/* 1313 */         Skill archery = null;
/* 1314 */         Skill bowskill = null;
/*      */         
/*      */         try {
/* 1317 */           int skillnum = bow.getPrimarySkill();
/* 1318 */           if (skillnum != -10L) {
/*      */             try
/*      */             {
/*      */               
/* 1322 */               bowskill = performer.getSkills().getSkill(skillnum);
/*      */             }
/* 1324 */             catch (NoSuchSkillException nss)
/*      */             {
/* 1326 */               bowskill = performer.getSkills().learn(skillnum, 1.0F);
/*      */             }
/*      */           
/*      */           }
/* 1330 */         } catch (NoSuchSkillException nss) {
/*      */           
/* 1332 */           performer.getCommunicator().sendCombatNormalMessage("This weapon has no skill attached. Please report this bug using the forums or the /dev channel.");
/*      */           
/* 1334 */           return true;
/*      */         } 
/*      */         
/*      */         try {
/* 1338 */           archery = performer.getSkills().getSkill(1030);
/*      */         }
/* 1340 */         catch (NoSuchSkillException nss) {
/*      */           
/* 1342 */           archery = performer.getSkills().learn(1030, 1.0F);
/*      */         } 
/*      */         
/* 1345 */         if (bowskill == null || archery == null) {
/*      */           
/* 1347 */           performer
/* 1348 */             .getCommunicator()
/* 1349 */             .sendCombatNormalMessage("There was a bug with the skill for this item. Please report this bug using the forums or the /dev channel.");
/*      */           
/* 1351 */           return true;
/*      */         } 
/* 1353 */         bowskill.skillCheck(diff, bow, arrow.getCurrentQualityLevel(), (dryRun || arrow
/* 1354 */             .getTemplateId() == 454), (float)damage / 500.0F);
/*      */       } 
/*      */     }
/* 1357 */     if (rotdam > 0.0F) {
/*      */       
/* 1359 */       dead = CombatEngine.addWound(performer, defender, wtype, pos, damage + damage * rotdam / 500.0D, armourMod, "hit", battle, Server.rand
/* 1360 */           .nextInt((int)Math.max(1.0F, rotdam)), poisdam, true, false, false, false);
/*      */     }
/*      */     else {
/*      */       
/* 1364 */       dead = CombatEngine.addWound(performer, defender, wtype, pos, damage, armourMod, "hit", battle, 0.0F, poisdam, true, false, false, false);
/*      */       
/* 1366 */       if (arrow.getSpellLifeTransferModifier() > 0.0F)
/*      */       {
/* 1368 */         if (damage * arrow.getSpellLifeTransferModifier() / 100.0D > 500.0D)
/*      */         {
/* 1370 */           if (performer.getBody() != null && performer.getBody().getWounds() != null) {
/*      */             
/* 1372 */             Wound[] w = performer.getBody().getWounds().getWounds();
/* 1373 */             if (w.length > 0) {
/* 1374 */               w[0].modifySeverity(
/* 1375 */                   -((int)(damage * arrow.getSpellLifeTransferModifier() / ((performer.getCultist() != null && performer.getCultist().healsFaster()) ? 250.0F : 500.0F))));
/*      */             }
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/* 1381 */     if (!dead && spdamb > 0.0F)
/*      */     {
/* 1383 */       if ((spdamb / 300.0F) * damage > 500.0D)
/*      */       {
/* 1385 */         dead = defender.addWoundOfType(performer, (byte)4, pos, false, armourMod, false, (spdamb / 300.0F) * damage, 0.0F, 0.0F, false, true);
/*      */       }
/*      */     }
/*      */     
/* 1389 */     if (!dead && arrow.getSpellFrostDamageBonus() > 0.0F)
/*      */     {
/* 1391 */       if ((arrow.getSpellFrostDamageBonus() / 300.0F) * damage > 500.0D)
/*      */       {
/* 1393 */         dead = defender.addWoundOfType(performer, (byte)8, pos, false, armourMod, false, (arrow
/* 1394 */             .getSpellFrostDamageBonus() / 300.0F) * damage, 0.0F, 0.0F, false, true);
/*      */       }
/*      */     }
/*      */     
/* 1398 */     if (!Players.getInstance().isOverKilling(performer.getWurmId(), defender.getWurmId())) {
/*      */       
/* 1400 */       if (arrow.getSpellExtraDamageBonus() > 0.0F)
/*      */       {
/* 1402 */         if (defender.isPlayer() && !defender.isNewbie()) {
/*      */           
/* 1404 */           SpellEffect speff = arrow.getSpellEffect((byte)45);
/* 1405 */           double gainMod = 1.0D;
/* 1406 */           if (damage < 5000.0D)
/* 1407 */             gainMod = damage / 5000.0D; 
/* 1408 */           if (speff != null) {
/* 1409 */             speff.setPower(Math.min(10000.0F, speff.power + (float)(dead ? 50.0D : (10.0D * gainMod))));
/*      */           }
/* 1411 */         } else if (!defender.isPlayer() && !defender.isGuard() && dead) {
/*      */           
/* 1413 */           SpellEffect speff = arrow.getSpellEffect((byte)45);
/* 1414 */           float gainMod = 1.0F;
/* 1415 */           if (speff.getPower() > 5000.0F && !Servers.isThisAnEpicOrChallengeServer())
/* 1416 */             gainMod = Math.max(0.5F, 1.0F - (speff.getPower() - 5000.0F) / 5000.0F); 
/* 1417 */           if (speff != null)
/* 1418 */             speff.setPower(Math.min(10000.0F, speff.power + defender.getBaseCombatRating() * gainMod)); 
/*      */         } 
/*      */       }
/* 1421 */       if (bow != null && bow.getSpellExtraDamageBonus() > 0.0F)
/*      */       {
/* 1423 */         if (defender.isPlayer() && !defender.isNewbie()) {
/*      */           
/* 1425 */           SpellEffect speff = bow.getSpellEffect((byte)45);
/* 1426 */           double gainMod = 1.0D;
/* 1427 */           if (damage * armourMod < 5000.0D)
/* 1428 */             gainMod = damage * armourMod / 5000.0D; 
/* 1429 */           if (speff != null) {
/* 1430 */             speff.setPower(Math.min(10000.0F, speff.power + (float)(dead ? 30.0D : (2.0D * gainMod))));
/*      */           }
/* 1432 */         } else if (!defender.isPlayer() && !defender.isGuard() && dead) {
/*      */           
/* 1434 */           SpellEffect speff = bow.getSpellEffect((byte)45);
/* 1435 */           float gainMod = 1.0F;
/* 1436 */           if (speff.getPower() > 5000.0F && !Servers.isThisAnEpicOrChallengeServer())
/* 1437 */             gainMod = Math.max(0.5F, 1.0F - (speff.getPower() - 5000.0F) / 5000.0F); 
/* 1438 */           if (speff != null)
/* 1439 */             speff.setPower(Math.min(10000.0F, speff.power + defender.getBaseCombatRating() * gainMod)); 
/*      */         } 
/*      */       }
/*      */     } 
/* 1443 */     if (dead) {
/*      */       
/* 1445 */       performer.getCommunicator().sendCombatSafeMessage(defender.getNameWithGenus() + " is dead!");
/* 1446 */       if (battle != null)
/* 1447 */         battle.addCasualty(performer, defender); 
/* 1448 */       performer.getCombatHandler().setKillEffects(performer, defender);
/* 1449 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/* 1453 */       SoundPlayer.playSound(defender.getHitSound(), defender, 1.6F);
/* 1454 */       if (!defender.isPlayer() && defender.isTypeFleeing()) {
/*      */         
/* 1456 */         defender.setFleeCounter(20, true);
/* 1457 */         Server.getInstance().broadCastAction(defender.getNameWithGenus() + " panics.", defender, 10);
/*      */       } 
/*      */     } 
/* 1460 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTargetTurnedTowardsCreature(Creature performer, Item target) {
/* 1465 */     return VirtualZone.isItemTurnedTowardsCreature(performer, target, 60.0F);
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
/*      */   public static boolean attack(Creature performer, Item target, Item bow, float counter, Action act) {
/* 1509 */     boolean done = false;
/* 1510 */     boolean boat = target.isBoat();
/*      */     
/* 1512 */     if (!target.isDecoration() && !boat) {
/*      */       
/* 1514 */       performer.getCommunicator().sendNormalServerMessage("You can't attack " + target.getNameWithGenus() + ".");
/* 1515 */       return true;
/*      */     } 
/* 1517 */     if (performer.getPositionZ() < -0.5D && performer.getVehicle() == -10L) {
/*      */       
/* 1519 */       performer.getCommunicator().sendNormalServerMessage("You are too deep in the water to fire a bow.");
/* 1520 */       return true;
/*      */     } 
/*      */     
/* 1523 */     if (bow.isWeaponBow()) {
/*      */       
/* 1525 */       int minRange = getMinimumRangeForBow(bow);
/* 1526 */       if (Creature.getRange(performer, target.getPosX(), target.getPosY()) < minRange) {
/*      */         
/* 1528 */         performer.getCommunicator().sendNormalServerMessage(
/* 1529 */             LoginHandler.raiseFirstLetter(target.getName()) + " is too close.");
/* 1530 */         return true;
/*      */       } 
/*      */       
/* 1533 */       Skill bowskill = null;
/*      */       
/*      */       try {
/* 1536 */         int skillnum = bow.getPrimarySkill();
/* 1537 */         if (skillnum != -10L) {
/*      */           try
/*      */           {
/*      */             
/* 1541 */             bowskill = performer.getSkills().getSkill(skillnum);
/*      */           }
/* 1543 */           catch (NoSuchSkillException nss)
/*      */           {
/* 1545 */             bowskill = performer.getSkills().learn(skillnum, 1.0F);
/*      */           }
/*      */         
/*      */         }
/* 1549 */       } catch (NoSuchSkillException nss) {
/*      */         
/* 1551 */         performer.getCommunicator().sendNormalServerMessage("This weapon has no skill attached. Please report this bug using the forums or the /dev channel.");
/*      */         
/* 1553 */         return true;
/*      */       } 
/* 1555 */       if (bowskill == null) {
/*      */         
/* 1557 */         performer
/* 1558 */           .getCommunicator()
/* 1559 */           .sendNormalServerMessage("There was a bug with the skill for this item. Please report this bug using the forums or the /dev channel.");
/*      */         
/* 1561 */         return true;
/*      */       } 
/* 1563 */       if (!isTargetTurnedTowardsCreature(performer, target) && !boat) {
/*      */         
/* 1565 */         performer.getCommunicator().sendNormalServerMessage("You must position yourself in front of the " + target
/* 1566 */             .getName() + " in order to shoot at it.");
/* 1567 */         return true;
/*      */       } 
/*      */       
/* 1570 */       if (!VirtualZone.isCreatureTurnedTowardsItem(target, performer, 60.0F)) {
/*      */         
/* 1572 */         performer.getCommunicator().sendCombatNormalMessage("You must turn towards the " + target
/* 1573 */             .getName() + " in order to shoot at it.");
/* 1574 */         return true;
/*      */       } 
/*      */       
/* 1577 */       if (performer.attackingIntoIllegalDuellingRing(target.getTileX(), target.getTileY(), target.isOnSurface())) {
/*      */         
/* 1579 */         performer.getCommunicator().sendNormalServerMessage("The duelling ring is holy ground and you may not attack across the border.");
/*      */         
/* 1581 */         return true;
/*      */       } 
/* 1583 */       if (act.justTickedSecond())
/* 1584 */         performer.decreaseFatigue(); 
/* 1585 */       int time = 200;
/* 1586 */       if (counter == 1.0F) {
/*      */         
/* 1588 */         if (performer.isOnSurface() != target.isOnSurface()) {
/*      */           
/* 1590 */           performer.getCommunicator().sendNormalServerMessage("You fail to get a clear shot of the " + target
/* 1591 */               .getName() + ".");
/* 1592 */           return true;
/*      */         } 
/* 1594 */         Item arrow = getArrow(performer);
/* 1595 */         if (arrow == null) {
/*      */           
/* 1597 */           performer.getCommunicator().sendNormalServerMessage("You have no arrows left to shoot!");
/* 1598 */           return true;
/*      */         } 
/*      */         
/* 1601 */         time = Actions.getQuickActionTime(performer, bowskill, bow, 0.0D);
/* 1602 */         if (act.getNumber() == 125 && 
/* 1603 */           time > 30)
/*      */         {
/* 1605 */           time = (int)Math.max(30.0F, time * 0.8F);
/*      */         }
/*      */         
/* 1608 */         performer.getCommunicator().sendNormalServerMessage("You start aiming at " + target.getNameWithGenus() + ".");
/* 1609 */         if (WurmCalendar.getHour() > 20 || WurmCalendar.getHour() < 5)
/* 1610 */           performer.getCommunicator().sendNormalServerMessage("The dusk makes it harder to get a clear view of the " + target
/* 1611 */               .getName() + "."); 
/* 1612 */         performer.sendActionControl(Actions.actionEntrys[act.getNumber()].getVerbString(), true, time);
/* 1613 */         Server.getInstance().broadCastAction(performer
/* 1614 */             .getName() + " draws an arrow and raises " + performer.getHisHerItsString() + " bow.", performer, 5);
/*      */         
/* 1616 */         act.setTimeLeft(time);
/*      */         
/* 1618 */         SoundPlayer.playSound("sound.arrow.aim", performer, 1.6F);
/*      */       } else {
/*      */         
/* 1621 */         time = act.getTimeLeft();
/* 1622 */       }  if (counter * 10.0F > time)
/*      */       {
/* 1624 */         done = true;
/*      */         
/* 1626 */         performer.getStatus().modifyStamina(-1000.0F);
/* 1627 */         if (performer.isOnSurface() != target.isOnSurface()) {
/*      */           
/* 1629 */           performer.getCommunicator().sendNormalServerMessage("You fail to get a clear shot of the " + target
/* 1630 */               .getName() + ".");
/* 1631 */           return true;
/*      */         } 
/* 1633 */         Zones.resetCoverHolder();
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
/* 1663 */         int minRange1 = getMinimumRangeForBow(bow);
/* 1664 */         if (Creature.getRange(performer, target.getPosX(), target.getPosY()) < minRange1) {
/*      */           
/* 1666 */           performer.getCommunicator().sendNormalServerMessage(target.getName() + " is too close.");
/* 1667 */           return true;
/*      */         } 
/*      */         
/* 1670 */         int trees = 0;
/* 1671 */         int treetilex = -1;
/* 1672 */         int treetiley = -1;
/* 1673 */         int tileArrowDownX = -1;
/* 1674 */         int tileArrowDownY = -1;
/* 1675 */         PathFinder pf = new PathFinder(true);
/*      */         
/*      */         try {
/* 1678 */           Path path = pf.rayCast((performer.getCurrentTile()).tilex, (performer.getCurrentTile()).tiley, target
/* 1679 */               .getTileX(), target.getTileY(), performer.isOnSurface(), (
/* 1680 */               (int)Creature.getRange(performer, target.getPosX(), target.getPosY()) >> 2) + 5);
/* 1681 */           while (!path.isEmpty())
/*      */           {
/* 1683 */             PathTile p = path.getFirst();
/*      */             
/* 1685 */             if (Tiles.getTile(Tiles.decodeType(p.getTile())).isTree()) {
/*      */               
/* 1687 */               trees++;
/* 1688 */               if (treetilex == -1 && Server.rand.nextInt(10) > trees) {
/*      */                 
/* 1690 */                 treetilex = p.getTileX();
/* 1691 */                 treetiley = p.getTileY();
/*      */               } 
/*      */             } 
/* 1694 */             if (tileArrowDownX == -1 && Server.rand.nextInt(15) == 0) {
/*      */               
/* 1696 */               tileArrowDownX = p.getTileX();
/* 1697 */               tileArrowDownY = p.getTileY();
/*      */             } 
/* 1699 */             path.removeFirst();
/*      */           }
/*      */         
/* 1702 */         } catch (NoPathException np) {
/*      */           
/* 1704 */           performer.getCommunicator().sendNormalServerMessage("You fail to get a clear shot.");
/* 1705 */           return true;
/*      */         } 
/* 1707 */         byte stringql = bow.getAuxData();
/* 1708 */         if (Server.rand.nextInt(Math.max(2, stringql) * 10) == 0) {
/*      */           
/* 1710 */           int realTemplate = 459;
/* 1711 */           if (bow.getTemplateId() == 449) {
/* 1712 */             realTemplate = 461;
/* 1713 */           } else if (bow.getTemplateId() == 448) {
/* 1714 */             realTemplate = 460;
/* 1715 */           }  bow.setTemplateId(realTemplate);
/* 1716 */           bow.setAuxData((byte)0);
/* 1717 */           performer.getCommunicator().sendNormalServerMessage("The string breaks!");
/* 1718 */           return true;
/*      */         } 
/*      */         
/* 1721 */         double diff = getBaseDifficulty(act.getNumber());
/* 1722 */         if (WurmCalendar.getHour() > 19) {
/* 1723 */           diff += (WurmCalendar.getHour() - 19);
/* 1724 */         } else if (WurmCalendar.getHour() < 6) {
/* 1725 */           diff += (6 - WurmCalendar.getHour());
/* 1726 */         }  diff += trees;
/* 1727 */         float bon = bow.getSpellNimbleness();
/* 1728 */         if (bon > 0.0F)
/* 1729 */           diff -= (bon / 10.0F); 
/* 1730 */         double deviation = getRangeDifficulty(performer, bow.getTemplateId(), target.getPosX(), target.getPosY());
/* 1731 */         diff += deviation;
/* 1732 */         if (target.isBoat()) {
/* 1733 */           diff += 30.0D;
/*      */         }
/* 1735 */         Item arrow = getArrow(performer);
/*      */         
/* 1737 */         if (arrow == null) {
/*      */           
/* 1739 */           performer.getCommunicator().sendNormalServerMessage("You have no arrows left to shoot!");
/* 1740 */           return true;
/*      */         } 
/*      */         
/*      */         try {
/* 1744 */           diff -= bow.getRarity();
/* 1745 */           diff -= arrow.getRarity();
/* 1746 */           arrow.getParent().dropItem(arrow.getWurmId(), false);
/*      */         }
/* 1748 */         catch (NoSuchItemException nsi) {
/*      */           
/* 1750 */           Items.destroyItem(arrow.getWurmId());
/* 1751 */           performer.getCommunicator().sendNormalServerMessage("You have no arrows left to shoot!");
/* 1752 */           return true;
/*      */         } 
/*      */         
/* 1755 */         Arrows.addToHitItem(arrow, performer, target, counter, bowskill, bow, tileArrowDownX, tileArrowDownY, deviation, diff, trees, treetilex, treetiley);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1761 */       done = true;
/* 1762 */       performer.getCommunicator().sendNormalServerMessage("You can't shoot with that.");
/*      */     } 
/* 1764 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean mayTakeDamage(Item target) {
/* 1769 */     if (target.isStone() || target.isMetal() || target.isBodyPart() || target.isTemporary() || target
/* 1770 */       .getTemplateId() == 272 || (target.isLockable() && target.getLockId() != -10L) || target
/* 1771 */       .isIndestructible() || target.isHugeAltar() || target.isDomainItem() || target.isKingdomMarker() || target
/* 1772 */       .isTraded() || target.isBanked() || target.isArtifact())
/* 1773 */       return false; 
/* 1774 */     return true;
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
/*      */   public static Item getArrow(Creature performer) {
/* 1825 */     Item quiver = performer.getBody().getBodyItem().findFirstContainedItem(462);
/* 1826 */     if (quiver == null || quiver.isEmpty(false))
/* 1827 */       quiver = performer.getInventory().findItem(462, true); 
/* 1828 */     Item arrow = null;
/* 1829 */     if (quiver != null) {
/*      */       
/* 1831 */       arrow = quiver.findFirstContainedItem(456);
/* 1832 */       if (arrow == null)
/* 1833 */         arrow = quiver.findFirstContainedItem(455); 
/* 1834 */       if (arrow == null) {
/* 1835 */         arrow = quiver.findFirstContainedItem(454);
/*      */       }
/*      */     } else {
/*      */       
/* 1839 */       arrow = performer.getBody().getBodyItem().findFirstContainedItem(456);
/* 1840 */       if (arrow == null)
/* 1841 */         arrow = performer.getBody().getBodyItem().findFirstContainedItem(455); 
/* 1842 */       if (arrow == null)
/* 1843 */         arrow = performer.getBody().getBodyItem().findFirstContainedItem(454); 
/* 1844 */       if (arrow == null)
/* 1845 */         arrow = performer.getInventory().findItem(456, true); 
/* 1846 */       if (arrow == null)
/* 1847 */         arrow = performer.getInventory().findItem(455, true); 
/* 1848 */       if (arrow == null)
/* 1849 */         arrow = performer.getInventory().findItem(454, true); 
/*      */     } 
/* 1851 */     return arrow;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isArchery(int action) {
/* 1856 */     return (action == 124 || action == 125 || action == 128 || action == 131 || action == 129 || action == 130 || action == 126 || action == 127);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final double getBaseDifficulty(int action) {
/* 1863 */     if (action == 124)
/* 1864 */       return 20.0D; 
/* 1865 */     if (action == 125)
/* 1866 */       return 30.0D; 
/* 1867 */     if (action == 128)
/* 1868 */       return 35.0D; 
/* 1869 */     if (action == 129)
/* 1870 */       return 40.0D; 
/* 1871 */     if (action == 130)
/* 1872 */       return 40.0D; 
/* 1873 */     if (action == 126)
/* 1874 */       return 70.0D; 
/* 1875 */     if (action == 127)
/* 1876 */       return 70.0D; 
/* 1877 */     if (action == 131)
/* 1878 */       return 70.0D; 
/* 1879 */     if (action == 134)
/* 1880 */       return 1.0D; 
/* 1881 */     return 99.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final byte getWoundPos(Creature defender, int action) {
/* 1886 */     if (action == 124 || action == 125) {
/*      */ 
/*      */       
/*      */       try {
/* 1890 */         return defender.getBody().getRandomWoundPos();
/*      */       }
/* 1892 */       catch (Exception ex) {
/*      */         
/* 1894 */         logger.log(Level.WARNING, defender + ", " + ex.getMessage(), ex);
/*      */       } 
/*      */     } else {
/* 1897 */       if (action == 128)
/* 1898 */         return 2; 
/* 1899 */       if (action == 131)
/* 1900 */         return 34; 
/* 1901 */       if (action == 129)
/* 1902 */         return 3; 
/* 1903 */       if (action == 130)
/* 1904 */         return 4; 
/* 1905 */       if (action == 126)
/* 1906 */         return 1; 
/* 1907 */       if (action == 127)
/* 1908 */         return 29; 
/* 1909 */     }  return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final double getRangeDifficulty(Creature performer, int itemTemplateId, float targetX, float targetY) {
/* 1915 */     double perfectDist = 30.0D;
/* 1916 */     if (itemTemplateId == 447) {
/* 1917 */       perfectDist = 20.0D;
/* 1918 */     } else if (itemTemplateId == 448) {
/* 1919 */       perfectDist = 40.0D;
/* 1920 */     } else if (itemTemplateId == 449) {
/* 1921 */       perfectDist = 80.0D;
/* 1922 */     }  double range = Creature.getRange(performer, targetX, targetY);
/* 1923 */     double deviation = Math.abs(perfectDist - range);
/* 1924 */     deviation += range / 5.0D;
/* 1925 */     if (deviation < 1.0D)
/* 1926 */       deviation = 1.0D; 
/* 1927 */     return deviation;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean willParryWithShield(Creature archer, Creature shieldbearer) {
/* 1932 */     return VirtualZone.isCreatureTurnedTowardsTarget(archer, shieldbearer, 90.0F, true);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\Archery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */