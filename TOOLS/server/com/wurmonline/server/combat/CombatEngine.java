/*      */ package com.wurmonline.server.combat;
/*      */ 
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.MessageServer;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*      */ import com.wurmonline.server.behaviours.PracticeDollBehaviour;
/*      */ import com.wurmonline.server.bodys.DbWound;
/*      */ import com.wurmonline.server.bodys.TempWound;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.CombatHandler;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.utils.CreatureLineSegment;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import com.wurmonline.shared.constants.Enchants;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import java.util.ArrayList;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
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
/*      */ public final class CombatEngine
/*      */   implements CombatConstants, CounterTypes, MiscConstants, SoundNames, TimeConstants, Enchants, CreatureTypes
/*      */ {
/*   68 */   private static final Logger logger = Logger.getLogger(CombatEngine.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float ARMOURMOD = 1.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean attack2(Creature performer, Creature defender, int counter, Action act) {
/*   82 */     return attack(performer, defender, counter, -1, act);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean attack(Creature performer, Creature defender, int counter, int pos, Action act) {
/*   89 */     if (!(performer.getStatus()).visible) {
/*      */       
/*   91 */       performer.getCommunicator().sendAlertServerMessage("You are now visible again.");
/*   92 */       performer.setVisible(true);
/*      */     } 
/*      */     
/*   95 */     boolean done = false;
/*   96 */     boolean dead = false;
/*   97 */     boolean aiming = false;
/*   98 */     if (performer.equals(defender)) {
/*      */       
/*  100 */       performer.getCommunicator().sendAlertServerMessage("You cannot attack yourself.");
/*  101 */       performer.setOpponent(null);
/*  102 */       return true;
/*      */     } 
/*      */     
/*  105 */     Item primWeapon = performer.getPrimWeapon();
/*  106 */     performer.setSecondsToLogout(300);
/*  107 */     if (!defender.isPlayer()) {
/*  108 */       performer.setSecondsToLogout(180);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  116 */     if (defender.getAttackers() > 9) {
/*      */       
/*  118 */       performer.getCommunicator().sendNormalServerMessage(defender
/*  119 */           .getNameWithGenus() + " is too crowded with attackers. You find no space.");
/*  120 */       performer.setOpponent(null);
/*  121 */       return true;
/*      */     } 
/*  123 */     if (primWeapon == null) {
/*      */       
/*  125 */       performer.getCommunicator()
/*  126 */         .sendNormalServerMessage("You have no weapon to attack " + defender.getNameWithGenus() + " with.");
/*  127 */       performer.setOpponent(null);
/*  128 */       return true;
/*      */     } 
/*      */     
/*  131 */     BlockingResult result = Blocking.getBlockerBetween(performer, defender, 4);
/*  132 */     if (result != null) {
/*      */       
/*  134 */       performer.getCommunicator().sendNormalServerMessage("The wall blocks your attempt.");
/*  135 */       performer.setOpponent(null);
/*  136 */       return true;
/*      */     } 
/*  138 */     if (Creature.rangeTo(performer, defender) > Actions.actionEntrys[114].getRange()) {
/*      */ 
/*      */ 
/*      */       
/*  142 */       performer.getCommunicator().sendNormalServerMessage("You are now too far away to " + Actions.actionEntrys[114]
/*  143 */           .getActionString().toLowerCase() + " " + defender
/*  144 */           .getNameWithGenus() + ".");
/*  145 */       performer.setOpponent(null);
/*  146 */       return true;
/*      */     } 
/*  148 */     if (performer.getLeader() != null && performer.getLeader() == defender)
/*  149 */       performer.setLeader(null); 
/*  150 */     if (performer.getPet() != null)
/*      */     {
/*  152 */       if ((performer.getPet()).target == -10L)
/*      */       {
/*  154 */         performer.getPet().setTarget(defender.getWurmId(), false);
/*      */       }
/*      */     }
/*      */     
/*  158 */     performer.staminaPollCounter = 2;
/*  159 */     int speed = 10;
/*  160 */     int timeMod = 2;
/*  161 */     if (performer.getFightStyle() == 2) {
/*  162 */       timeMod = 4;
/*  163 */     } else if (performer.getFightStyle() == 1) {
/*  164 */       timeMod = 0;
/*  165 */     }  if (primWeapon.isBodyPart() && primWeapon.getAuxData() != 100) {
/*  166 */       speed = (int)performer.getBodyWeaponSpeed(primWeapon);
/*  167 */     } else if (primWeapon.isWeaponPierce() || primWeapon.isWeaponKnife()) {
/*  168 */       speed = primWeapon.getWeightGrams() / 1000 + 1 + timeMod;
/*  169 */     } else if (primWeapon.isWeaponSlash() || primWeapon.isWeaponSword() || primWeapon.isWeaponAxe()) {
/*  170 */       speed = primWeapon.getWeightGrams() / 1000 + 3 + timeMod;
/*  171 */     } else if (primWeapon.isWeaponCrush()) {
/*  172 */       speed = primWeapon.getWeightGrams() / 1000 + 6 + timeMod;
/*      */     } else {
/*  174 */       speed = primWeapon.getWeightGrams() / 1000 + 6 + timeMod;
/*  175 */     }  if (pos != -1) {
/*      */       
/*  177 */       aiming = true;
/*  178 */       speed++;
/*      */     } 
/*  180 */     defender.addAttacker(performer);
/*  181 */     if (!done) {
/*      */       
/*  183 */       int posBonus = 0;
/*  184 */       float defAngle = Creature.normalizeAngle(defender.getStatus().getRotation());
/*  185 */       double newrot = Math.atan2((performer.getStatus().getPositionY() - defender.getStatus().getPositionY()), (performer
/*  186 */           .getStatus().getPositionX() - defender.getStatus().getPositionX()));
/*  187 */       float attAngle = (float)(newrot * 57.29577951308232D) + 90.0F;
/*  188 */       attAngle = Creature.normalizeAngle(attAngle - defAngle);
/*      */       
/*  190 */       if (attAngle > 90.0F && attAngle < 270.0F)
/*      */       {
/*  192 */         if (attAngle > 135.0F && attAngle < 225.0F) {
/*  193 */           posBonus = 10;
/*      */         } else {
/*  195 */           posBonus = 5;
/*      */         } 
/*      */       }
/*      */       
/*  199 */       float diff = (performer.getPositionZ() + performer.getAltOffZ() - defender.getPositionZ() + defender.getAltOffZ()) / 10.0F;
/*  200 */       posBonus += (int)Math.min(5.0F, diff);
/*      */       
/*  202 */       if (counter == 1) {
/*      */         
/*  204 */         if (!(defender instanceof Player)) {
/*      */           
/*  206 */           defender.turnTowardsCreature(performer);
/*  207 */           if (defender.isHunter()) {
/*  208 */             defender.setTarget(performer.getWurmId(), false);
/*      */           }
/*      */         } 
/*  211 */         if (performer instanceof Player && defender instanceof Player) {
/*      */           
/*  213 */           Battle battle = Battles.getBattleFor(performer, defender);
/*  214 */           battle.addEvent(new BattleEvent((short)-1, performer.getName(), defender.getName()));
/*      */         } 
/*      */         
/*  217 */         if (aiming) {
/*      */           
/*  219 */           String bodypartname = defender.getBody().getWoundLocationString(pos);
/*  220 */           performer.getCommunicator().sendSafeServerMessage("You try to " + 
/*  221 */               getAttackString(performer, primWeapon) + " " + defender.getNameWithGenus() + " in the " + bodypartname + ".");
/*      */           
/*  223 */           defender.getCommunicator().sendAlertServerMessage(performer
/*  224 */               .getNameWithGenus() + " tries to " + getAttackString(performer, primWeapon) + " you!");
/*      */         }
/*      */         else {
/*      */           
/*  228 */           performer.getCommunicator().sendSafeServerMessage("You try to " + 
/*  229 */               getAttackString(performer, primWeapon) + " " + defender.getNameWithGenus() + ".");
/*  230 */           if (performer.isDominated())
/*      */           {
/*  232 */             if (performer.getDominator() != null)
/*  233 */               performer
/*  234 */                 .getDominator()
/*  235 */                 .getCommunicator()
/*  236 */                 .sendSafeServerMessage(performer
/*  237 */                   .getNameWithGenus() + " tries to " + getAttackString(performer, primWeapon) + " " + defender
/*  238 */                   .getNameWithGenus() + "."); 
/*      */           }
/*  240 */           defender.getCommunicator().sendAlertServerMessage(performer
/*  241 */               .getNameWithGenus() + " tries to " + getAttackString(performer, primWeapon) + " you!");
/*  242 */           if (defender.isDominated())
/*      */           {
/*  244 */             if (defender.getDominator() != null) {
/*  245 */               defender.getDominator()
/*  246 */                 .getCommunicator()
/*  247 */                 .sendAlertServerMessage(performer
/*  248 */                   .getNameWithGenus() + " tries to " + getAttackString(performer, primWeapon) + " " + defender
/*  249 */                   .getNameWithGenus() + "!");
/*      */             }
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*  255 */         Battle battle = performer.getBattle();
/*  256 */         if (battle != null) {
/*  257 */           battle.touch();
/*      */         }
/*      */       } 
/*  260 */       if (act.currentSecond() % speed == 0 || (counter == 1 && !(performer instanceof Player))) {
/*      */         
/*  262 */         if (!(defender instanceof Player))
/*      */         {
/*  264 */           defender.turnTowardsCreature(performer);
/*      */         }
/*      */         
/*  267 */         Item defPrimWeapon = null;
/*  268 */         Skill attackerFightSkill = null;
/*  269 */         Skill defenderFightSkill = null;
/*  270 */         Skills performerSkills = performer.getSkills();
/*  271 */         Skills defenderSkills = defender.getSkills();
/*      */ 
/*      */         
/*  274 */         double attBonus = performer.zoneBonus - performer.getMovePenalty() * 0.5D;
/*  275 */         double defBonus = (defender.zoneBonus - defender.getMovePenalty());
/*  276 */         if (defender.isMoving() && defender instanceof Player)
/*  277 */           defBonus -= 5.0D; 
/*  278 */         if (performer.isMoving() && performer instanceof Player) {
/*  279 */           attBonus -= 5.0D;
/*      */         }
/*      */         
/*  282 */         attBonus += posBonus;
/*  283 */         defPrimWeapon = defender.getPrimWeapon();
/*      */ 
/*      */         
/*  286 */         int attSknum = 1023;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  301 */           attackerFightSkill = performerSkills.getSkill(1023);
/*      */         }
/*  303 */         catch (NoSuchSkillException nss) {
/*      */           
/*  305 */           attackerFightSkill = performerSkills.learn(1023, 1.0F);
/*      */         } 
/*  307 */         int defSknum = 1023;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  322 */           defenderFightSkill = defenderSkills.getSkill(1023);
/*      */         }
/*  324 */         catch (NoSuchSkillException nss) {
/*      */           
/*  326 */           defenderFightSkill = defenderSkills.learn(1023, 1.0F);
/*      */         } 
/*  328 */         dead = performAttack(pos, aiming, performer, performerSkills, defender, defenderSkills, primWeapon, defPrimWeapon, attBonus, defBonus, attackerFightSkill, defenderFightSkill, speed);
/*      */         
/*  330 */         if (aiming)
/*  331 */           done = true; 
/*  332 */         if (dead)
/*  333 */           done = true; 
/*      */       } 
/*  335 */       if (!done && !aiming) {
/*      */         
/*  337 */         Item[] secondaryWeapons = performer.getSecondaryWeapons();
/*  338 */         for (int x = 0; x < secondaryWeapons.length; x++) {
/*      */           
/*  340 */           if (!done) {
/*      */             
/*  342 */             speed = 10;
/*  343 */             if (secondaryWeapons[x].isBodyPart() && secondaryWeapons[x].getAuxData() != 100) {
/*  344 */               speed = Server.rand.nextInt((int)(performer.getBodyWeaponSpeed(secondaryWeapons[x]) + 5.0F)) + 1 + timeMod;
/*      */             }
/*  346 */             else if (secondaryWeapons[x].isWeaponPierce() || secondaryWeapons[x].isWeaponKnife()) {
/*  347 */               speed = 5 + Server.rand.nextInt(secondaryWeapons[x].getWeightGrams() / 1000 + 3) + 1 + timeMod;
/*  348 */             } else if (secondaryWeapons[x].isWeaponSlash() || secondaryWeapons[x].isWeaponSword() || secondaryWeapons[x]
/*  349 */               .isWeaponAxe()) {
/*  350 */               speed = 5 + Server.rand.nextInt(secondaryWeapons[x].getWeightGrams() / 1000 + 5) + 1 + timeMod;
/*  351 */             } else if (secondaryWeapons[x].isWeaponCrush()) {
/*  352 */               speed = 5 + Server.rand.nextInt(secondaryWeapons[x].getWeightGrams() / 1000 + 8) + 1 + timeMod;
/*      */             } else {
/*  354 */               speed = 5 + Server.rand.nextInt(secondaryWeapons[x].getWeightGrams() / 1000 + 10) + 1 + timeMod;
/*  355 */             }  if (act.currentSecond() % speed == 0) {
/*      */               
/*  357 */               Item defPrimWeapon = null;
/*  358 */               Skill attackerFightSkill = null;
/*  359 */               Skill defenderFightSkill = null;
/*  360 */               Skills performerSkills = performer.getSkills();
/*  361 */               Skills defenderSkills = defender.getSkills();
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  366 */               double attBonus = performer.zoneBonus - performer.getMovePenalty() * 0.5D;
/*  367 */               double defBonus = (defender.zoneBonus - defender.getMovePenalty());
/*      */ 
/*      */               
/*  370 */               if (defender.isMoving() && defender instanceof Player)
/*  371 */                 defBonus -= 5.0D; 
/*  372 */               if (performer.isMoving() && performer instanceof Player)
/*  373 */                 attBonus -= 5.0D; 
/*  374 */               attBonus += posBonus;
/*      */ 
/*      */               
/*  377 */               defPrimWeapon = defender.getPrimWeapon();
/*      */ 
/*      */ 
/*      */               
/*  381 */               int attSknum = 1023;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/*  396 */                 attackerFightSkill = performerSkills.getSkill(1023);
/*      */               }
/*  398 */               catch (NoSuchSkillException nss) {
/*      */                 
/*  400 */                 attackerFightSkill = performerSkills.learn(1023, 1.0F);
/*      */               } 
/*  402 */               int defSknum = 1023;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/*  417 */                 defenderFightSkill = defenderSkills.getSkill(1023);
/*      */               }
/*  419 */               catch (NoSuchSkillException nss) {
/*      */                 
/*  421 */                 defenderFightSkill = defenderSkills.learn(1023, 1.0F);
/*      */               } 
/*  423 */               dead = performAttack(pos, false, performer, performerSkills, defender, defenderSkills, secondaryWeapons[x], defPrimWeapon, attBonus, defBonus, attackerFightSkill, defenderFightSkill, speed);
/*      */ 
/*      */               
/*  426 */               if (dead)
/*  427 */                 done = true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  433 */     performer.getStatus().modifyStamina(-50.0F);
/*  434 */     if (done) {
/*      */       
/*  436 */       if (aiming) {
/*      */         
/*  438 */         if (dead) {
/*      */           
/*  440 */           defender.setOpponent(null);
/*  441 */           defender.setTarget(-10L, true);
/*  442 */           performer.setTarget(-10L, true);
/*  443 */           performer.setOpponent(null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  463 */           if (performer.getCitizenVillage() != null)
/*  464 */             performer.getCitizenVillage().removeTarget(defender); 
/*  465 */           if (defender instanceof Player && performer instanceof Player) {
/*      */ 
/*      */             
/*      */             try {
/*  469 */               Players.getInstance().addKill(performer.getWurmId(), defender.getWurmId(), defender.getName());
/*      */             }
/*  471 */             catch (Exception ex) {
/*      */               
/*  473 */               logger.log(Level.INFO, "Failed to add kill for " + performer.getName() + ":" + defender.getName() + " - " + ex
/*  474 */                   .getMessage(), ex);
/*      */             } 
/*      */             
/*  477 */             if (!performer.isOnPvPServer() || !defender.isOnPvPServer()) {
/*      */               
/*  479 */               boolean okToKill = false;
/*  480 */               if (performer.getCitizenVillage() != null && 
/*  481 */                 performer.getCitizenVillage().isEnemy(defender.getCitizenVillage()))
/*  482 */                 okToKill = true; 
/*  483 */               if (defender.getKingdomId() == performer.getKingdomId() && defender
/*  484 */                 .getKingdomTemplateId() != 3 && 
/*  485 */                 defender.getReputation() >= 0 && !okToKill) {
/*  486 */                 performer.setReputation(performer.getReputation() - 20);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/*  493 */         performer.setOpponent(null);
/*  494 */         if (dead) {
/*      */           
/*  496 */           defender.setOpponent(null);
/*  497 */           defender.setTarget(-10L, true);
/*  498 */           performer.setTarget(-10L, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  518 */           if (performer.getCitizenVillage() != null)
/*      */           {
/*      */             
/*  521 */             performer.getCitizenVillage().removeTarget(defender);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  533 */           if (defender instanceof Player && performer instanceof Player) {
/*      */ 
/*      */             
/*      */             try {
/*  537 */               Players.getInstance().addKill(performer.getWurmId(), defender.getWurmId(), defender.getName());
/*      */             }
/*  539 */             catch (Exception ex) {
/*      */               
/*  541 */               logger.log(Level.INFO, "Failed to add kill for " + performer.getName() + ":" + defender.getName() + " - " + ex
/*  542 */                   .getMessage(), ex);
/*      */             } 
/*  544 */             if (!performer.isOnPvPServer() || !defender.isOnPvPServer()) {
/*      */               
/*  546 */               boolean okToKill = false;
/*  547 */               if (performer.getCitizenVillage() != null && 
/*  548 */                 performer.getCitizenVillage().isEnemy(defender.getCitizenVillage()))
/*  549 */                 okToKill = true; 
/*  550 */               if (defender.getKingdomId() == performer.getKingdomId() && defender
/*  551 */                 .getKingdomId() != 3 && 
/*  552 */                 defender.getReputation() >= 0 && !okToKill)
/*  553 */                 performer.setReputation(performer.getReputation() - 20); 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  558 */       if (dead)
/*      */       {
/*  560 */         if (!(defender instanceof Player) && performer instanceof Player) {
/*      */           
/*      */           try {
/*      */             
/*  564 */             int tid = defender.getTemplate().getTemplateId();
/*  565 */             if (CreatureTemplate.isDragon(tid)) {
/*  566 */               ((Player)performer).addTitle(Titles.Title.DragonSlayer);
/*  567 */             } else if (tid == 11 || tid == 27) {
/*  568 */               ((Player)performer).addTitle(Titles.Title.TrollSlayer);
/*  569 */             } else if (tid == 20 || tid == 22) {
/*  570 */               ((Player)performer).addTitle(Titles.Title.GiantSlayer);
/*  571 */             }  if (defender.isUnique()) {
/*  572 */               HistoryManager.addHistory(performer.getNameWithGenus(), "slayed " + defender.getNameWithGenus());
/*      */             }
/*  574 */           } catch (Exception ex) {
/*      */             
/*  576 */             logger.log(Level.WARNING, "Defender: " + defender.getName() + " and attacker: " + performer.getName() + ":" + ex
/*  577 */                 .getMessage(), ex);
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*  582 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getEnchantBonus(Item item, Creature defender) {
/*  587 */     if (item.enchantment != 0)
/*      */     {
/*  589 */       if (item.enchantment == 11) {
/*      */         
/*  591 */         if (defender.isAnimal()) {
/*  592 */           return 10.0F;
/*      */         }
/*  594 */       } else if (item.enchantment == 9) {
/*      */         
/*  596 */         if (defender.isHuman()) {
/*  597 */           return 10.0F;
/*      */         }
/*  599 */       } else if (item.enchantment == 10) {
/*      */         
/*  601 */         if (defender.isRegenerating()) {
/*  602 */           return 10.0F;
/*      */         }
/*  604 */       } else if (item.enchantment == 12) {
/*      */         
/*  606 */         if (defender.isDragon()) {
/*  607 */           return 10.0F;
/*      */         }
/*  609 */       } else if (defender.getDeity() != null) {
/*      */         
/*  611 */         Deity d = defender.getDeity();
/*  612 */         if (d.number == 1) {
/*      */           
/*  614 */           if (item.enchantment == 1) {
/*  615 */             return 10.0F;
/*      */           }
/*  617 */         } else if (d.number == 4) {
/*      */           
/*  619 */           if (item.enchantment == 4) {
/*  620 */             return 10.0F;
/*      */           }
/*  622 */         } else if (d.number == 2) {
/*      */           
/*  624 */           if (item.enchantment == 2) {
/*  625 */             return 10.0F;
/*      */           }
/*  627 */         } else if (d.number == 3) {
/*      */           
/*  629 */           if (item.enchantment == 3)
/*  630 */             return 10.0F; 
/*      */         } 
/*      */       } 
/*      */     }
/*  634 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean checkEnchantDestruction(Item item, Item defw, Creature defender) {
/*  639 */     if (item.enchantment != 0 && defw.enchantment != 0) {
/*      */       
/*  641 */       boolean destroyed = false;
/*  642 */       if ((defw.enchantment == 1 && item.enchantment == 5) || (item.enchantment == 1 && defw.enchantment == 5)) {
/*      */ 
/*      */         
/*  645 */         if (!item.isArtifact())
/*  646 */           item.enchant((byte)0); 
/*  647 */         if (!defw.isArtifact())
/*  648 */           defw.enchant((byte)0); 
/*  649 */         destroyed = true;
/*      */       }
/*  651 */       else if ((defw.enchantment == 4 && item.enchantment == 8) || (item.enchantment == 4 && defw.enchantment == 8)) {
/*      */ 
/*      */         
/*  654 */         if (!item.isArtifact())
/*  655 */           item.enchant((byte)0); 
/*  656 */         if (!defw.isArtifact())
/*  657 */           defw.enchant((byte)0); 
/*  658 */         destroyed = true;
/*      */       }
/*  660 */       else if ((defw.enchantment == 2 && item.enchantment == 6) || (item.enchantment == 2 && defw.enchantment == 6)) {
/*      */ 
/*      */         
/*  663 */         if (!item.isArtifact())
/*  664 */           item.enchant((byte)0); 
/*  665 */         if (!defw.isArtifact())
/*  666 */           defw.enchant((byte)0); 
/*  667 */         destroyed = true;
/*      */       }
/*  669 */       else if ((defw.enchantment == 3 && item.enchantment == 7) || (item.enchantment == 3 && defw.enchantment == 7)) {
/*      */ 
/*      */         
/*  672 */         if (!item.isArtifact())
/*  673 */           item.enchant((byte)0); 
/*  674 */         if (!defw.isArtifact())
/*  675 */           defw.enchant((byte)0); 
/*  676 */         destroyed = true;
/*      */       } 
/*  678 */       if (destroyed) {
/*      */         
/*  680 */         int tilex = defender.getTileX();
/*  681 */         int tiley = defender.getTileY();
/*  682 */         VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, defender.isOnSurface());
/*  683 */         vtile.broadCast("A bright light emanates from where the " + item.getName() + " and the " + defw.getName() + " meet!");
/*      */       } 
/*      */     } 
/*      */     
/*  687 */     return false;
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
/*      */   public static final float getMod(Creature performer, Creature defender, Skill skill) {
/*  713 */     float mod = 1.0F;
/*  714 */     if (defender instanceof Player && performer instanceof Player) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  722 */       mod = 3.0F;
/*      */       
/*      */       try {
/*  725 */         if (Players.getInstance().isOverKilling(performer.getWurmId(), defender.getWurmId())) {
/*  726 */           mod = 0.1F;
/*      */         }
/*  728 */       } catch (Exception ex) {
/*      */         
/*  730 */         logger.log(Level.WARNING, performer.getName() + " failed to retrieve pk", ex);
/*      */       }
/*      */     
/*      */     }
/*  734 */     else if (performer instanceof Player) {
/*      */       
/*  736 */       if (skill.getRealKnowledge() >= 50.0D)
/*  737 */         mod = 0.5F; 
/*      */     } 
/*  739 */     return mod;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isEye(int pos) {
/*  744 */     return (pos == 18 || pos == 19 || pos == 20);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getRealPosition(int pos) {
/*  749 */     int rand = 10;
/*  750 */     if (pos == 1) {
/*      */       
/*  752 */       rand = Server.rand.nextInt(100);
/*  753 */       if (rand < 50) {
/*  754 */         pos = 17;
/*      */       }
/*  756 */     } else if (pos == 29) {
/*      */       
/*  758 */       rand = Server.rand.nextInt(100);
/*  759 */       if (rand < 97) {
/*  760 */         pos = 29;
/*  761 */       } else if (rand < 98) {
/*  762 */         pos = 18;
/*  763 */       } else if (rand < 99) {
/*  764 */         pos = 19;
/*      */       } 
/*  766 */     } else if (pos == 2) {
/*      */       
/*  768 */       rand = Server.rand.nextInt(20);
/*  769 */       if (rand < 5) {
/*  770 */         pos = 21;
/*  771 */       } else if (rand < 7) {
/*  772 */         pos = 27;
/*  773 */       } else if (rand < 9) {
/*  774 */         pos = 26;
/*  775 */       } else if (rand < 12) {
/*  776 */         pos = 32;
/*  777 */       } else if (rand < 14) {
/*  778 */         pos = 23;
/*  779 */       } else if (rand < 18) {
/*  780 */         pos = 24;
/*  781 */       } else if (rand < 20) {
/*  782 */         pos = 25;
/*      */       } 
/*  784 */     } else if (pos == 3) {
/*      */       
/*  786 */       rand = Server.rand.nextInt(10);
/*  787 */       if (rand < 5) {
/*  788 */         pos = 5;
/*  789 */       } else if (rand < 9) {
/*  790 */         pos = 9;
/*      */       } else {
/*  792 */         pos = 13;
/*      */       } 
/*  794 */     } else if (pos == 4) {
/*      */       
/*  796 */       rand = Server.rand.nextInt(10);
/*  797 */       if (rand < 5) {
/*  798 */         pos = 6;
/*  799 */       } else if (rand < 9) {
/*  800 */         pos = 10;
/*      */       } else {
/*  802 */         pos = 14;
/*      */       } 
/*  804 */     } else if (pos == 34) {
/*      */       
/*  806 */       rand = Server.rand.nextInt(20);
/*  807 */       if (rand < 5) {
/*  808 */         pos = 7;
/*  809 */       } else if (rand < 9) {
/*  810 */         pos = 11;
/*  811 */       } else if (rand < 10) {
/*  812 */         pos = 15;
/*  813 */       }  if (rand < 15) {
/*  814 */         pos = 8;
/*  815 */       } else if (rand < 19) {
/*  816 */         pos = 12;
/*      */       } else {
/*  818 */         pos = 16;
/*      */       } 
/*  820 */     }  return pos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected static boolean performAttack(int pos, boolean aiming, Creature performer, Skills performerSkills, Creature defender, Skills defenderSkills, Item attWeapon, Item defPrimWeapon, double attBonus, double defBonus, Skill attackerFightSkill, Skill defenderFightSkill, int counter) {
/*  829 */     boolean shieldBlocked = false;
/*  830 */     boolean done = false;
/*  831 */     Skill primWeaponSkill = null;
/*  832 */     Skill defPrimWeaponSkill = null;
/*  833 */     Item defShield = null;
/*  834 */     Skill defShieldSkill = null;
/*  835 */     int skillnum = -10;
/*  836 */     boolean dryrun = false;
/*      */     
/*  838 */     if (performer.isPlayer()) {
/*      */       
/*  840 */       if (defender.isPlayer() || defender.isReborn()) {
/*      */ 
/*      */         
/*  843 */         dryrun = true;
/*      */       }
/*  845 */       else if (defender.isKingdomGuard() || (defender
/*  846 */         .isSpiritGuard() && defender.getKingdomId() == performer.getKingdomId())) {
/*  847 */         dryrun = true;
/*      */       } 
/*  849 */     } else if (performer.isKingdomGuard() || (performer
/*  850 */       .isSpiritGuard() && defender.getKingdomId() == performer.getKingdomId())) {
/*  851 */       dryrun = true;
/*  852 */     }  if ((defender.isPlayer() && !defender.hasLink()) || (performer.isPlayer() && !performer.hasLink()))
/*  853 */       dryrun = true; 
/*  854 */     if (defender.getStatus().getStunned() > 0.0F) {
/*      */       
/*  856 */       defBonus -= 20.0D;
/*  857 */       attBonus += 20.0D;
/*      */     } 
/*  859 */     if (attWeapon != null)
/*      */     {
/*  861 */       if (attWeapon.isBodyPart()) {
/*      */ 
/*      */         
/*      */         try {
/*  865 */           skillnum = 10052;
/*  866 */           primWeaponSkill = performerSkills.getSkill(skillnum);
/*      */         }
/*  868 */         catch (NoSuchSkillException nss) {
/*      */           
/*  870 */           if (skillnum != -10)
/*  871 */             primWeaponSkill = performerSkills.learn(skillnum, 1.0F); 
/*      */         } 
/*  873 */         if (performer.isPlayer() && defender.isPlayer())
/*      */         {
/*  875 */           if (primWeaponSkill.getKnowledge(0.0D) >= 20.0D) {
/*  876 */             dryrun = true;
/*      */           }
/*      */         }
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*  883 */           skillnum = attWeapon.getPrimarySkill();
/*  884 */           primWeaponSkill = performerSkills.getSkill(skillnum);
/*      */         }
/*  886 */         catch (NoSuchSkillException nss) {
/*      */           
/*  888 */           if (skillnum != -10) {
/*  889 */             primWeaponSkill = performerSkills.learn(skillnum, 1.0F);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  896 */     skillnum = -10;
/*  897 */     if (defPrimWeapon != null)
/*      */     {
/*  899 */       if (defPrimWeapon.isBodyPart()) {
/*      */ 
/*      */         
/*      */         try {
/*  903 */           skillnum = 10052;
/*  904 */           defPrimWeaponSkill = defenderSkills.getSkill(skillnum);
/*      */         }
/*  906 */         catch (NoSuchSkillException nss) {
/*      */           
/*  908 */           if (skillnum != -10)
/*  909 */             defPrimWeaponSkill = defenderSkills.learn(skillnum, 1.0F); 
/*      */         } 
/*  911 */         if (performer.isPlayer() && defender.isPlayer())
/*      */         {
/*  913 */           if (defPrimWeaponSkill.getKnowledge(0.0D) >= 20.0D) {
/*  914 */             dryrun = true;
/*      */           }
/*      */         }
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*  921 */           skillnum = defPrimWeapon.getPrimarySkill();
/*  922 */           defPrimWeaponSkill = defenderSkills.getSkill(skillnum);
/*      */         }
/*  924 */         catch (NoSuchSkillException nss) {
/*      */           
/*  926 */           if (skillnum != -10) {
/*  927 */             defPrimWeaponSkill = defenderSkills.learn(skillnum, 1.0F);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  939 */     Skill attStrengthSkill = null;
/*      */     
/*      */     try {
/*  942 */       attStrengthSkill = performerSkills.getSkill(102);
/*      */     }
/*  944 */     catch (NoSuchSkillException nss) {
/*      */       
/*  946 */       attStrengthSkill = performerSkills.learn(102, 1.0F);
/*  947 */       logger.log(Level.WARNING, performer.getName() + " had no strength. Weird.");
/*      */     } 
/*      */     
/*  950 */     double bonus = 0.0D;
/*  951 */     if (primWeaponSkill != null) {
/*      */       
/*  953 */       float mod = getMod(performer, defender, primWeaponSkill);
/*  954 */       bonus = Math.max(-20.0D, primWeaponSkill.skillCheck(
/*  955 */             Math.abs(primWeaponSkill.getKnowledge(0.0D) - attWeapon.getCurrentQualityLevel()), attBonus, (mod == 0.0F || dryrun), 
/*  956 */             (float)(long)Math.max(1.0F, counter * mod)));
/*      */     } 
/*      */ 
/*      */     
/*  960 */     skillnum = -10;
/*  961 */     defShield = defender.getShield();
/*  962 */     if (defShield != null) {
/*      */       
/*      */       try {
/*      */         
/*  966 */         skillnum = defShield.getPrimarySkill();
/*  967 */         defShieldSkill = defenderSkills.getSkill(skillnum);
/*      */       }
/*  969 */       catch (NoSuchSkillException nss) {
/*      */         
/*  971 */         if (skillnum != -10) {
/*  972 */           defShieldSkill = defenderSkills.learn(skillnum, 1.0F);
/*      */         }
/*      */       } 
/*      */     }
/*  976 */     if (aiming) {
/*      */       
/*  978 */       if (pos == 1) {
/*      */         
/*  980 */         bonus = -60.0D;
/*      */       }
/*  982 */       else if (pos == 29) {
/*      */         
/*  984 */         bonus = -80.0D;
/*      */       }
/*  986 */       else if (pos == 2) {
/*      */         
/*  988 */         bonus = -40.0D;
/*      */       }
/*  990 */       else if (pos == 3) {
/*      */         
/*  992 */         bonus = -30.0D;
/*      */       }
/*  994 */       else if (pos == 4) {
/*      */         
/*  996 */         bonus = -30.0D;
/*      */       }
/*  998 */       else if (pos == 34) {
/*      */         
/* 1000 */         bonus = -30.0D;
/*      */       } 
/* 1002 */       pos = getRealPosition(pos);
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1008 */         pos = defender.getBody().getRandomWoundPos();
/*      */       }
/* 1010 */       catch (Exception ex) {
/*      */         
/* 1012 */         logger.log(Level.WARNING, "Problem getting a Random Wound Position for " + defender.getName() + ": due to " + ex
/* 1013 */             .getMessage(), ex);
/*      */       } 
/*      */     } 
/* 1016 */     if (performer.getEnemyPresense() > 1200 && defender.isPlayer())
/* 1017 */       bonus += 20.0D; 
/* 1018 */     double attCheck = 0.0D;
/* 1019 */     boolean defFumbleShield = false;
/* 1020 */     boolean defFumbleParry = false;
/*      */     
/* 1022 */     boolean crit = false;
/*      */     
/* 1024 */     if (defender.isPlayer()) {
/*      */       
/* 1026 */       int critChance = attWeapon.getDamagePercent();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1032 */       if (Server.rand.nextInt(100 - Math.min(3, critChance)) == 0)
/*      */       {
/* 1034 */         crit = true;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1039 */     if (attWeapon.isBodyPartAttached()) {
/*      */       
/* 1041 */       float mod = getMod(performer, defender, attackerFightSkill);
/*      */       
/* 1043 */       attCheck = attackerFightSkill.skillCheck(defenderFightSkill
/* 1044 */           .getKnowledge(defBonus) / Math.min(5, defender.getAttackers()), bonus, (mod == 0.0F || dryrun), 
/* 1045 */           (float)(long)Math.max(1.0F, counter * mod));
/*      */     }
/*      */     else {
/*      */       
/* 1049 */       float mod = getMod(performer, defender, attackerFightSkill);
/* 1050 */       attCheck = attackerFightSkill.skillCheck(defenderFightSkill
/* 1051 */           .getKnowledge(defBonus) / Math.min(5, defender.getAttackers()), attWeapon, bonus, (mod == 0.0F || dryrun), 
/* 1052 */           (float)(long)Math.max(1.0F, counter * mod));
/*      */     } 
/* 1054 */     byte type = (performer.getTemplate()).combatDamageType;
/* 1055 */     if (attWeapon.isWeaponSword()) {
/*      */       
/* 1057 */       if (Server.rand.nextInt(2) == 0) {
/* 1058 */         type = 1;
/*      */       } else {
/* 1060 */         type = 2;
/*      */       } 
/* 1062 */     } else if (attWeapon.isWeaponSlash()) {
/* 1063 */       type = 1;
/* 1064 */     } else if (attWeapon.isWeaponPierce()) {
/* 1065 */       type = 2;
/* 1066 */     } else if (attWeapon.isBodyPart()) {
/*      */       
/* 1068 */       if (attWeapon.getTemplateId() == 17) {
/* 1069 */         type = 3;
/* 1070 */       } else if (attWeapon.getTemplateId() == 12) {
/* 1071 */         type = 0;
/*      */       } 
/*      */     } 
/* 1074 */     String attString = getAttackString(performer, attWeapon, type);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1079 */     double defCheck = 0.0D;
/* 1080 */     double damage = getWeaponDamage(attWeapon, attStrengthSkill);
/*      */     
/* 1082 */     if (performer.getDeity() != null && performer.getDeity().isWarrior() && 
/* 1083 */       performer.getFaith() >= 40.0F && performer.getFavor() >= 20.0F)
/* 1084 */       damage = Math.min(4000.0D, damage * 1.25D); 
/* 1085 */     if (performer.getEnemyPresense() > 1200 && defender.isPlayer()) {
/* 1086 */       damage *= Math.min(4000.0F, 1.15F);
/*      */     }
/*      */     
/* 1089 */     if (defShield != null || crit) {
/*      */       
/* 1091 */       if (!crit)
/*      */       {
/* 1093 */         if (pos == 9) {
/*      */           
/* 1095 */           shieldBlocked = true;
/*      */         }
/* 1097 */         else if (defender.getStatus().getStamina() >= 300 && !defender.isMoving()) {
/*      */           
/* 1099 */           defCheck = 0.0D;
/* 1100 */           if (defShieldSkill != null) {
/*      */             
/* 1102 */             float mod = getMod(performer, defender, defShieldSkill);
/* 1103 */             defCheck = defShieldSkill.skillCheck(attCheck, defShield, defBonus, (mod == 0.0F || dryrun), (float)(long)mod);
/*      */           } 
/*      */ 
/*      */           
/* 1107 */           defCheck += (defShield.getSizeY() + defShield.getSizeZ()) / 10.0D;
/*      */ 
/*      */           
/* 1110 */           defender.getStatus().modifyStamina(-300.0F);
/*      */         } 
/*      */       }
/* 1113 */       if (defCheck > 0.0D || shieldBlocked) {
/*      */         
/* 1115 */         shieldBlocked = true;
/* 1116 */         if (defender.isPlayer()) {
/* 1117 */           defShield.setDamage(defShield.getDamage() + 0.001F * (float)damage * defShield.getDamageModifier());
/*      */         }
/*      */       }
/* 1120 */       else if (defCheck < -90.0D) {
/*      */         
/* 1122 */         defFumbleShield = true;
/*      */       } 
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
/* 1134 */     if (!shieldBlocked || crit) {
/*      */       
/* 1136 */       boolean parryPrimWeapon = true;
/* 1137 */       defCheck = 0.0D;
/* 1138 */       if (!crit && !defender.isMoving()) {
/*      */         
/* 1140 */         int parryTime = 100;
/* 1141 */         if (defender.getFightStyle() == 2) {
/* 1142 */           parryTime = 40;
/* 1143 */         } else if (defender.getFightStyle() == 1) {
/* 1144 */           parryTime = 160;
/*      */         } 
/* 1146 */         if (WurmCalendar.currentTime > defender.lastParry + Server.rand.nextInt(parryTime) && 
/* 1147 */           defPrimWeapon != null && !defPrimWeapon.isWeaponAxe())
/*      */         {
/* 1149 */           if (!defPrimWeapon.isBodyPart() || defPrimWeapon.getAuxData() == 100) {
/*      */             
/* 1151 */             if (defender.getStatus().getStamina() >= 300) {
/*      */               
/* 1153 */               if (defPrimWeaponSkill != null) {
/*      */                 
/* 1155 */                 float mod = getMod(performer, defender, defPrimWeaponSkill);
/* 1156 */                 defCheck = defPrimWeaponSkill.skillCheck((attCheck * defender
/* 1157 */                     .getAttackers() + defPrimWeapon.getWeightGrams() / 200.0D) / 
/* 1158 */                     getWeaponParryBonus(defPrimWeapon), defPrimWeapon, defBonus, (mod == 0.0F || dryrun), (float)(long)mod);
/*      */                 
/* 1160 */                 defender.lastParry = WurmCalendar.currentTime;
/* 1161 */                 defender.getStatus().modifyStamina(-300.0F);
/*      */               } 
/* 1163 */               if (defCheck < -90.0D)
/*      */               {
/* 1165 */                 defFumbleParry = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               }
/* 1182 */               else if (defCheck < 0.0D && Server.rand.nextInt(50) == 0)
/*      */               {
/* 1184 */                 defCheck = secondaryParry(performer, attCheck, defender, defenderSkills, defCheck, defBonus, dryrun);
/*      */                 
/* 1186 */                 if (defCheck < -90.0D) {
/*      */                   
/* 1188 */                   defFumbleParry = true;
/*      */                 }
/* 1190 */                 else if (defCheck > 0.0D) {
/* 1191 */                   parryPrimWeapon = false;
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             } 
/* 1197 */           } else if (defender.getStatus().getStamina() >= 300) {
/*      */             
/* 1199 */             if (defPrimWeaponSkill != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1204 */               float mod = getMod(performer, defender, defPrimWeaponSkill);
/* 1205 */               defCheck = defPrimWeaponSkill.skillCheck(Math.min(100, 80 * defender.getAttackers()), defBonus, (mod == 0.0F || dryrun), (float)(long)mod);
/*      */               
/* 1207 */               defender.lastParry = WurmCalendar.currentTime;
/* 1208 */               defender.getStatus().modifyStamina(-300.0F);
/*      */             } 
/* 1210 */             if (defCheck < 0.0D && Server.rand.nextInt(50) == 0) {
/*      */               
/* 1212 */               defCheck = secondaryParry(performer, attCheck, defender, defenderSkills, defCheck, defBonus, dryrun);
/*      */               
/* 1214 */               if (defCheck < -90.0D) {
/*      */                 
/* 1216 */                 defFumbleParry = true;
/*      */               }
/* 1218 */               else if (defCheck > 0.0D) {
/* 1219 */                 parryPrimWeapon = false;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/* 1225 */       if (defCheck <= 0.0D || defFumbleShield || crit) {
/*      */ 
/*      */ 
/*      */         
/* 1229 */         if (!defFumbleShield && !defFumbleParry && !crit)
/*      */         {
/* 1231 */           if (defender.getStatus().getStamina() >= 300) {
/*      */             
/* 1233 */             defender.getStatus().modifyStamina(-300.0F);
/* 1234 */             Skill defenderBodyControl = null;
/*      */             
/*      */             try {
/* 1237 */               defenderBodyControl = defenderSkills.getSkill(104);
/*      */             }
/* 1239 */             catch (NoSuchSkillException nss) {
/*      */               
/* 1241 */               defenderBodyControl = defenderSkills.learn(104, 1.0F);
/* 1242 */               logger.log(Level.WARNING, defender.getName() + " no body control?");
/*      */             } 
/* 1244 */             if (defenderBodyControl != null) {
/*      */               
/* 1246 */               float mod = getMod(performer, defender, defenderBodyControl);
/* 1247 */               defCheck = defenderBodyControl.skillCheck(attCheck, 0.0D, (mod == 0.0F || dryrun), (float)(long)mod);
/*      */             } else {
/*      */               
/* 1250 */               logger.log(Level.WARNING, defender.getName() + " has no body control!");
/*      */             } 
/*      */           }  } 
/* 1253 */         if (defCheck <= 0.0D || crit) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1261 */           Item armour = null;
/*      */ 
/*      */           
/* 1264 */           float armourMod = defender.getArmourMod();
/* 1265 */           float evasionChance = ArmourTemplate.calculateGlanceRate(defender.getArmourType(), armour, type, armourMod);
/* 1266 */           if (!performer.isPlayer() && !defender.isPlayer() && !defender.isUnique())
/* 1267 */             armourMod = 1.0F; 
/* 1268 */           if (armourMod == 1.0F) {
/*      */             
/*      */             try {
/*      */               
/* 1272 */               byte bodyPosition = ArmourTemplate.getArmourPosition((byte)pos);
/* 1273 */               armour = defender.getArmour(bodyPosition);
/* 1274 */               armourMod = ArmourTemplate.calculateDR(armour, type);
/* 1275 */               if (defender.isPlayer())
/* 1276 */                 armour.setDamage(armour.getDamage() + 
/* 1277 */                     Math.min(1.0F, (float)(damage * armourMod / 80.0D) * armour
/*      */                       
/* 1279 */                       .getDamageModifier() * 
/* 1280 */                       ArmourTemplate.getArmourDamageModFor(armour, type))); 
/* 1281 */               checkEnchantDestruction(attWeapon, armour, defender);
/* 1282 */               evasionChance = ArmourTemplate.calculateGlanceRate(null, armour, type, armourMod);
/*      */             }
/* 1284 */             catch (NoArmourException nsi) {
/*      */ 
/*      */               
/* 1287 */               evasionChance = 1.0F - defender.getArmourMod();
/*      */             }
/* 1289 */             catch (NoSpaceException nsp) {
/*      */               
/* 1291 */               logger.log(Level.WARNING, defender.getName() + " no armour space on loc " + pos);
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1299 */           if (!attWeapon.isBodyPart() || attWeapon.getAuxData() == 100)
/*      */           {
/* 1301 */             if (performer.isPlayer())
/* 1302 */               attWeapon.setDamage(attWeapon.getDamage() + (float)(damage * (2.1D - armourMod) / 1000.0D) * attWeapon
/* 1303 */                   .getDamageModifier()); 
/*      */           }
/* 1305 */           if (defender.isUnique()) {
/*      */             
/* 1307 */             evasionChance = 0.5F;
/* 1308 */             damage *= armourMod;
/*      */           } 
/* 1310 */           if (Server.rand.nextFloat() < evasionChance) {
/*      */             
/* 1312 */             if (aiming || performer.spamMode())
/* 1313 */               performer.getCommunicator().sendNormalServerMessage("Your attack glances off " + defender
/* 1314 */                   .getNameWithGenus() + "'s armour."); 
/* 1315 */             if (defender.spamMode()) {
/* 1316 */               defender.getCommunicator().sendNormalServerMessage("The attack to the " + defender
/* 1317 */                   .getBody().getWoundLocationString(pos) + " glances off your armour.");
/*      */             }
/*      */           }
/* 1320 */           else if (damage > (5.0F + Server.rand.nextFloat() * 5.0F) || crit) {
/*      */             
/* 1322 */             if (crit)
/*      */             {
/* 1324 */               armourMod = 1.0F;
/*      */             }
/*      */ 
/*      */             
/* 1328 */             Battle battle = performer.getBattle();
/* 1329 */             boolean dead = false;
/* 1330 */             if (defender.getStaminaSkill().getKnowledge(0.0D) < 2.0D) {
/*      */               
/* 1332 */               defender.die(false, "Combat Stam Check Fail");
/* 1333 */               dead = true;
/*      */             } else {
/*      */               
/* 1336 */               dead = addWound(performer, defender, type, pos, damage, armourMod, attString, battle, 0.0F, 0.0F, false, false, false, false);
/*      */             } 
/* 1338 */             if (!dead && attWeapon.getSpellDamageBonus() > 0.0F)
/*      */             {
/* 1340 */               if (damage * attWeapon.getSpellDamageBonus() / 300.0D > (Server.rand.nextFloat() * 5.0F) || crit) {
/* 1341 */                 dead = defender.addWoundOfType(performer, (byte)4, (byte)pos, false, armourMod, false, damage * attWeapon
/* 1342 */                     .getSpellDamageBonus() / 300.0D, 0.0F, 0.0F, false, false);
/*      */               }
/*      */             }
/* 1345 */             if (!dead && attWeapon.getWeaponSpellDamageBonus() > 0.0F)
/*      */             {
/* 1347 */               if (damage * attWeapon.getWeaponSpellDamageBonus() / 300.0D > (Server.rand.nextFloat() * 5.0F))
/*      */               {
/* 1349 */                 dead = defender.addWoundOfType(performer, (byte)6, 1, true, armourMod, false, damage * attWeapon
/* 1350 */                     .getWeaponSpellDamageBonus() / 300.0D, Server.rand
/* 1351 */                     .nextInt((int)attWeapon.getWeaponSpellDamageBonus()), 0.0F, false, false);
/*      */               }
/*      */             }
/* 1354 */             if (armour != null)
/*      */             {
/* 1356 */               if (armour.getSpellPainShare() > 0.0F)
/*      */               {
/* 1358 */                 if (performer.isUnique()) {
/* 1359 */                   defender.getCommunicator().sendNormalServerMessage(performer
/* 1360 */                       .getNameWithGenus() + " ignores the effects of the " + armour.getName() + ".");
/*      */ 
/*      */                 
/*      */                 }
/* 1364 */                 else if (damage * armour.getSpellPainShare() / 300.0D > 5.0D) {
/*      */                   
/* 1366 */                   addBounceWound(defender, performer, type, pos, damage * armour
/* 1367 */                       .getSpellPainShare() / 300.0D, armourMod, 0.0F, 0.0F, false, true);
/*      */                 } 
/*      */               }
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 1374 */             if (dead) {
/*      */               
/* 1376 */               performer.getCommunicator().sendSafeServerMessage(defender.getNameWithGenus() + " is dead!");
/* 1377 */               if (battle != null)
/* 1378 */                 battle.addCasualty(performer, defender); 
/* 1379 */               done = true;
/*      */ 
/*      */             
/*      */             }
/* 1383 */             else if (!defender.hasNoServerSound()) {
/* 1384 */               SoundPlayer.playSound(defender.getHitSound(), defender, 1.6F);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1389 */             if (aiming || performer.spamMode())
/*      */             {
/* 1391 */               performer.getCommunicator().sendNormalServerMessage(defender
/* 1392 */                   .getNameWithGenus() + " takes no real damage from the hit to the " + defender
/* 1393 */                   .getBody().getWoundLocationString(pos) + ".");
/*      */             }
/* 1395 */             if (defender.spamMode()) {
/* 1396 */               defender.getCommunicator().sendNormalServerMessage("You take no real damage from the blow to the " + defender
/*      */                   
/* 1398 */                   .getBody().getWoundLocationString(pos) + ".");
/*      */             }
/*      */           } 
/*      */         } else {
/*      */           
/* 1403 */           String sstring = "sound.combat.miss.light";
/* 1404 */           if (attCheck < -80.0D) {
/* 1405 */             sstring = "sound.combat.miss.heavy";
/* 1406 */           } else if (attCheck < -40.0D) {
/* 1407 */             sstring = "sound.combat.miss.med";
/* 1408 */           }  SoundPlayer.playSound(sstring, defender, 1.6F);
/* 1409 */           if (aiming || performer.spamMode())
/*      */           {
/* 1411 */             performer.getCommunicator().sendNormalServerMessage(defender
/* 1412 */                 .getNameWithGenus() + " " + getParryString(defCheck) + " evades the blow to the " + defender
/* 1413 */                 .getBody().getWoundLocationString(pos) + ".");
/*      */           }
/* 1415 */           if (defender.spamMode()) {
/* 1416 */             defender.getCommunicator().sendNormalServerMessage("You " + 
/* 1417 */                 getParryString(defCheck) + " evade the blow to the " + defender
/* 1418 */                 .getBody().getWoundLocationString(pos) + ".");
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/* 1423 */         defender.lastParry = WurmCalendar.currentTime;
/* 1424 */         Item weapon = defPrimWeapon;
/* 1425 */         if (!parryPrimWeapon)
/* 1426 */           weapon = defender.getLefthandWeapon(); 
/* 1427 */         if (aiming || performer.spamMode())
/*      */         {
/* 1429 */           performer.getCommunicator().sendNormalServerMessage(defender
/* 1430 */               .getNameWithGenus() + " " + getParryString(defCheck) + " parries with " + weapon.getNameWithGenus() + ".");
/*      */         }
/*      */ 
/*      */         
/* 1434 */         if (defender.spamMode())
/*      */         {
/* 1436 */           defender.getCommunicator().sendNormalServerMessage("You " + 
/* 1437 */               getParryString(defCheck) + " parry with your " + weapon.getName() + ".");
/*      */         }
/*      */         
/* 1440 */         if (!weapon.isBodyPart() || weapon.getAuxData() == 100) {
/*      */           
/* 1442 */           if (defender.isPlayer())
/*      */           {
/* 1444 */             if (weapon.isWeaponSword()) {
/* 1445 */               weapon.setDamage(weapon.getDamage() + 0.001F * (float)damage * weapon.getDamageModifier());
/*      */             } else {
/* 1447 */               weapon.setDamage(weapon.getDamage() + 0.005F * (float)damage * weapon.getDamageModifier());
/*      */             }  } 
/* 1449 */           if (performer.isPlayer())
/*      */           {
/* 1451 */             if (!attWeapon.isBodyPart() || attWeapon.getAuxData() == 100) {
/* 1452 */               attWeapon
/* 1453 */                 .setDamage(attWeapon.getDamage() + 0.001F * (float)damage * attWeapon.getDamageModifier());
/*      */             }
/*      */           }
/*      */         } 
/* 1457 */         String sstring = "sound.combat.parry1";
/* 1458 */         int x = Server.rand.nextInt(3);
/* 1459 */         if (x == 0) {
/* 1460 */           sstring = "sound.combat.parry2";
/* 1461 */         } else if (x == 1) {
/* 1462 */           sstring = "sound.combat.parry3";
/* 1463 */         }  SoundPlayer.playSound(sstring, defender, 1.6F);
/* 1464 */         checkEnchantDestruction(attWeapon, weapon, defender);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1469 */       if (performer.spamMode()) {
/*      */         
/* 1471 */         if (aiming) {
/* 1472 */           performer.getCommunicator().sendNormalServerMessage(defender
/* 1473 */               .getNameWithGenus() + " raises " + defender.getHisHerItsString() + " shield and parries.");
/*      */         } else {
/* 1475 */           performer.getCommunicator().sendNormalServerMessage("You try to " + attString + " " + defender
/* 1476 */               .getNameWithGenus() + " but " + defender.getHeSheItString() + " raises " + defender
/* 1477 */               .getHisHerItsString() + " shield and parries.");
/*      */         } 
/* 1479 */       } else if (aiming) {
/* 1480 */         performer.getCommunicator().sendNormalServerMessage(defender
/* 1481 */             .getNameWithGenus() + " raises " + defender.getHisHerItsString() + " shield and parries.");
/* 1482 */       }  if (defender.spamMode())
/* 1483 */         defender.getCommunicator().sendNormalServerMessage(performer
/* 1484 */             .getNameWithGenus() + " tries to " + attString + " you but you raise your shield and parry."); 
/* 1485 */       if (defShield.isWood()) {
/* 1486 */         Methods.sendSound(defender, "sound.combat.shield.wood");
/*      */       } else {
/* 1488 */         Methods.sendSound(defender, "sound.combat.shield.metal");
/* 1489 */       }  checkEnchantDestruction(attWeapon, defShield, defender);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1510 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean addWound(@Nullable Creature performer, Creature defender, byte type, int pos, double damage, float armourMod, String attString, @Nullable Battle battle, float infection, float poison, boolean archery, boolean alreadyCalculatedResist, boolean noMinimumDamage, boolean spell) {
/* 1518 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1520 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, type, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1523 */     if (defender != null && defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1525 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, type, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1528 */     if (!alreadyCalculatedResist) {
/*      */       
/* 1530 */       if (type == 8 || type == 4 || type == 10)
/*      */       {
/* 1532 */         if (defender.getCultist() != null && defender.getCultist().hasNoElementalDamage()) {
/* 1533 */           return false;
/*      */         }
/*      */       }
/* 1536 */       if (defender.hasSpellEffect((byte)69))
/*      */       {
/* 1538 */         damage *= 0.800000011920929D;
/*      */       }
/* 1540 */       damage *= Wound.getResistModifier(performer, defender, type);
/*      */     } 
/* 1542 */     boolean dead = false;
/* 1543 */     if (damage * armourMod > 500.0D || noMinimumDamage) {
/*      */       DbWound dbWound;
/* 1545 */       if (defender.hasSpellEffect((byte)68)) {
/*      */         
/* 1547 */         defender.reduceStoneSkin();
/* 1548 */         return false;
/*      */       } 
/* 1550 */       Wound wound = null;
/* 1551 */       boolean foundWound = false;
/* 1552 */       String broadCastString = "";
/*      */       
/* 1554 */       String otherString = (CombatHandler.getOthersString() == "") ? (attString + "s") : CombatHandler.getOthersString();
/*      */ 
/*      */       
/* 1557 */       CombatHandler.setOthersString("");
/*      */ 
/*      */       
/* 1560 */       if (Server.rand.nextInt(10) <= 6 && defender.getBody().getWounds() != null) {
/* 1561 */         wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte)pos, type);
/*      */       }
/* 1563 */       if (wound != null) {
/*      */         
/* 1565 */         defender.setWounded();
/* 1566 */         if (infection > 0.0F)
/* 1567 */           wound.setInfectionSeverity(Math.min(99.0F, wound
/* 1568 */                 .getInfectionSeverity() + Server.rand.nextInt((int)infection))); 
/* 1569 */         if (poison > 0.0F)
/* 1570 */           wound.setPoisonSeverity(Math.min(99.0F, wound.getPoisonSeverity() + poison)); 
/* 1571 */         wound.setBandaged(false);
/* 1572 */         if (wound.getHealEff() > 0 && Server.rand.nextInt(2) == 0)
/* 1573 */           wound.setHealeff((byte)0); 
/* 1574 */         dead = wound.modifySeverity((int)(damage * armourMod), (performer != null) ? performer.isPlayer() : false, spell);
/* 1575 */         foundWound = true;
/*      */       }
/*      */       else {
/*      */         
/* 1579 */         if (!defender.isPlayer()) {
/* 1580 */           TempWound tempWound = new TempWound(type, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */         } else {
/*      */           
/* 1583 */           dbWound = new DbWound(type, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, (performer != null) ? performer.isPlayer() : false, spell);
/* 1584 */         }  defender.setWounded();
/*      */       } 
/*      */       
/* 1587 */       if (performer != null && !attString.isEmpty()) {
/*      */         
/* 1589 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1590 */         segments.add(new CreatureLineSegment(performer));
/* 1591 */         segments.add(new MulticolorLineSegment(" " + otherString + " ", (byte)0));
/* 1592 */         if (performer == defender) {
/* 1593 */           segments.add(new MulticolorLineSegment(performer.getHimHerItString() + "self", (byte)0));
/*      */         } else {
/* 1595 */           segments.add(new CreatureLineSegment(defender));
/* 1596 */         }  segments.add(new MulticolorLineSegment(" " + getStrengthString(damage / 1000.0D) + " in the " + defender
/* 1597 */               .getBody().getWoundLocationString(dbWound.getLocation()) + " and " + 
/* 1598 */               getRealDamageString(damage * armourMod), (byte)0));
/* 1599 */         segments.add(new MulticolorLineSegment("s it.", (byte)0));
/*      */         
/* 1601 */         MessageServer.broadcastColoredAction(segments, performer, defender, 5, true);
/*      */         
/* 1603 */         for (MulticolorLineSegment s : segments) {
/* 1604 */           broadCastString = broadCastString + s.getText();
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1612 */         if (performer != defender) {
/*      */           
/* 1614 */           for (MulticolorLineSegment s : segments) {
/* 1615 */             s.setColor((byte)7);
/*      */           }
/* 1617 */           defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1625 */         ((MulticolorLineSegment)segments.get(1)).setText(" " + attString + " ");
/* 1626 */         ((MulticolorLineSegment)segments.get(4)).setText(" it.");
/* 1627 */         for (MulticolorLineSegment s : segments) {
/* 1628 */           s.setColor((byte)3);
/*      */         }
/* 1630 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1640 */       if (defender.isDominated())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1647 */         if (!archery) {
/*      */           
/* 1649 */           if (!defender.isReborn() || defender.getMother() != -10L) {
/* 1650 */             defender.modifyLoyalty(-((int)(damage * armourMod) * defender.getBaseCombatRating() / 200000.0F));
/*      */           }
/* 1652 */         } else if (defender.getDominator() == performer) {
/* 1653 */           defender.modifyLoyalty(-((int)(damage * armourMod) * defender.getBaseCombatRating() / 200000.0F));
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1660 */       if (infection > 0.0F && !attString.isEmpty())
/*      */       {
/* 1662 */         if (performer != null) {
/*      */           
/* 1664 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1665 */           segments.add(new MulticolorLineSegment("Your weapon", (byte)3));
/* 1666 */           segments.add(new MulticolorLineSegment(" infects ", (byte)3));
/* 1667 */           segments.add(new CreatureLineSegment(defender));
/* 1668 */           segments.add(new MulticolorLineSegment(" with a disease.", (byte)3));
/*      */           
/* 1670 */           performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */           
/* 1672 */           segments.set(0, new CreatureLineSegment(performer));
/* 1673 */           for (MulticolorLineSegment s : segments) {
/* 1674 */             s.setColor((byte)7);
/*      */           }
/* 1676 */           defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1684 */       if (poison > 0.0F && !attString.isEmpty())
/*      */       {
/* 1686 */         if (performer != null) {
/*      */           
/* 1688 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1689 */           segments.add(new MulticolorLineSegment("Your weapon", (byte)3));
/* 1690 */           segments.add(new MulticolorLineSegment(" poisons ", (byte)3));
/* 1691 */           segments.add(new CreatureLineSegment(defender));
/* 1692 */           segments.add(new MulticolorLineSegment(".", (byte)3));
/*      */           
/* 1694 */           performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */           
/* 1696 */           segments.set(0, new CreatureLineSegment(performer));
/* 1697 */           for (MulticolorLineSegment s : segments) {
/* 1698 */             s.setColor((byte)7);
/*      */           }
/* 1700 */           defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1709 */       if (battle != null && performer != null) {
/* 1710 */         battle.addEvent(new BattleEvent((short)114, performer.getName(), defender.getName(), broadCastString));
/*      */       }
/* 1712 */       if (!foundWound)
/* 1713 */         dead = defender.getBody().addWound((Wound)dbWound); 
/*      */     } 
/* 1715 */     return dead;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean addRotWound(@Nullable Creature performer, Creature defender, int pos, double damage, float armourMod, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/* 1721 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1723 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, (byte)6, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1726 */     if (defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1728 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, (byte)6, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1731 */     boolean dead = false;
/* 1732 */     if (damage * armourMod > 500.0D || noMinimumDamage) {
/*      */       DbWound dbWound;
/* 1734 */       if (defender.hasSpellEffect((byte)68)) {
/*      */         
/* 1736 */         defender.reduceStoneSkin();
/* 1737 */         return false;
/*      */       } 
/* 1739 */       Wound wound = null;
/* 1740 */       boolean foundWound = false;
/* 1741 */       if (performer != null) {
/*      */         
/* 1743 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1744 */         segments.add(new MulticolorLineSegment("Your weapon", (byte)3));
/* 1745 */         segments.add(new MulticolorLineSegment(" infects ", (byte)3));
/* 1746 */         segments.add(new CreatureLineSegment(defender));
/* 1747 */         segments.add(new MulticolorLineSegment(" with a disease.", (byte)3));
/*      */         
/* 1749 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 1751 */         segments.set(0, new CreatureLineSegment(performer));
/* 1752 */         for (MulticolorLineSegment s : segments) {
/* 1753 */           s.setColor((byte)7);
/*      */         }
/* 1755 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1761 */       if (defender.getBody().getWounds() != null) {
/*      */         
/* 1763 */         wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte)pos, (byte)6);
/* 1764 */         if (wound != null)
/*      */         {
/* 1766 */           if (wound.getType() == 6) {
/*      */             
/* 1768 */             defender.setWounded();
/* 1769 */             wound.setBandaged(false);
/* 1770 */             dead = wound.modifySeverity((int)(damage * armourMod), (performer != null && performer.isPlayer()), spell);
/* 1771 */             wound.setInfectionSeverity(Math.min(99.0F, wound.getInfectionSeverity() + infection));
/* 1772 */             foundWound = true;
/*      */           } else {
/*      */             
/* 1775 */             wound = null;
/*      */           } 
/*      */         }
/*      */       } 
/* 1779 */       if (wound == null)
/*      */       {
/* 1781 */         if (WurmId.getType(defender.getWurmId()) == 1) {
/*      */           
/* 1783 */           TempWound tempWound = new TempWound((byte)6, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */         } else {
/*      */           
/* 1786 */           dbWound = new DbWound((byte)6, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, (performer != null && performer.isPlayer()), spell);
/*      */         } 
/*      */       }
/* 1789 */       if (!foundWound)
/* 1790 */         dead = defender.getBody().addWound((Wound)dbWound); 
/*      */     } 
/* 1792 */     return dead;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean addFireWound(@Nullable Creature performer, @Nonnull Creature defender, int pos, double damage, float armourMod, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/* 1798 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1800 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, (byte)4, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1803 */     if (defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1805 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, (byte)4, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1808 */     if (defender.getCultist() != null && defender.getCultist().hasNoElementalDamage())
/* 1809 */       return false; 
/* 1810 */     boolean dead = false;
/* 1811 */     if (damage * armourMod > 500.0D || noMinimumDamage) {
/*      */       DbWound dbWound;
/* 1813 */       if (defender.hasSpellEffect((byte)68)) {
/*      */         
/* 1815 */         defender.reduceStoneSkin();
/* 1816 */         return false;
/*      */       } 
/* 1818 */       Wound wound = null;
/* 1819 */       boolean foundWound = false;
/* 1820 */       if (performer != null) {
/*      */         
/* 1822 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1823 */         segments.add(new MulticolorLineSegment("Your weapon", (byte)3));
/* 1824 */         segments.add(new MulticolorLineSegment(" burns ", (byte)3));
/* 1825 */         segments.add(new CreatureLineSegment(defender));
/* 1826 */         segments.add(new MulticolorLineSegment(".", (byte)3));
/*      */         
/* 1828 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 1830 */         segments.set(0, new CreatureLineSegment(performer));
/* 1831 */         for (MulticolorLineSegment s : segments) {
/* 1832 */           s.setColor((byte)7);
/*      */         }
/* 1834 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1840 */       if (defender.getBody().getWounds() != null) {
/*      */         
/* 1842 */         wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte)pos, (byte)4);
/* 1843 */         if (wound != null)
/*      */         {
/* 1845 */           if (wound.getType() == 4) {
/*      */             
/* 1847 */             defender.setWounded();
/* 1848 */             wound.setBandaged(false);
/* 1849 */             dead = wound.modifySeverity((int)(damage * armourMod), (performer != null && performer.isPlayer()), spell);
/* 1850 */             foundWound = true;
/*      */           } else {
/*      */             
/* 1853 */             wound = null;
/*      */           }  } 
/*      */       } 
/* 1856 */       if (wound == null)
/*      */       {
/* 1858 */         if (WurmId.getType(defender.getWurmId()) == 1) {
/* 1859 */           TempWound tempWound = new TempWound((byte)4, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */         } else {
/*      */           
/* 1862 */           dbWound = new DbWound((byte)4, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, (performer != null && performer.isPlayer()), spell);
/*      */         } 
/*      */       }
/* 1865 */       if (!foundWound)
/* 1866 */         dead = defender.getBody().addWound((Wound)dbWound); 
/*      */     } 
/* 1868 */     return dead;
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
/*      */   public static boolean addAcidWound(@Nullable Creature performer, @Nonnull Creature defender, int pos, double damage, float armourMod, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/* 1885 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1887 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, (byte)10, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1890 */     if (defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1892 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, (byte)10, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1895 */     if (defender.getCultist() != null && defender.getCultist().hasNoElementalDamage())
/* 1896 */       return false; 
/* 1897 */     boolean dead = false;
/* 1898 */     if (damage * armourMod > 500.0D || noMinimumDamage) {
/*      */       DbWound dbWound;
/* 1900 */       if (defender.hasSpellEffect((byte)68)) {
/*      */         
/* 1902 */         defender.reduceStoneSkin();
/* 1903 */         return false;
/*      */       } 
/* 1905 */       Wound wound = null;
/* 1906 */       boolean foundWound = false;
/* 1907 */       if (performer != null) {
/*      */         
/* 1909 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1910 */         segments.add(new MulticolorLineSegment("Acid from ", (byte)3));
/* 1911 */         segments.add(new CreatureLineSegment(performer));
/* 1912 */         segments.add(new MulticolorLineSegment(" dissolves ", (byte)3));
/* 1913 */         segments.add(new CreatureLineSegment(defender));
/* 1914 */         segments.add(new MulticolorLineSegment(".", (byte)3));
/*      */         
/* 1916 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 1918 */         for (MulticolorLineSegment s : segments) {
/* 1919 */           s.setColor((byte)7);
/*      */         }
/* 1921 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1927 */       if (defender.getBody().getWounds() != null) {
/*      */         
/* 1929 */         wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte)pos, (byte)10);
/* 1930 */         if (wound != null)
/*      */         {
/* 1932 */           if (wound.getType() == 10) {
/*      */             
/* 1934 */             defender.setWounded();
/* 1935 */             wound.setBandaged(false);
/* 1936 */             dead = wound.modifySeverity((int)(damage / 2.0D * armourMod), (performer != null && performer.isPlayer()), spell);
/* 1937 */             foundWound = true;
/*      */           } else {
/*      */             
/* 1940 */             wound = null;
/*      */           }  } 
/*      */       } 
/* 1943 */       if (wound == null)
/*      */       {
/* 1945 */         if (WurmId.getType(defender.getWurmId()) == 1) {
/* 1946 */           TempWound tempWound = new TempWound((byte)10, (byte)pos, (float)damage / 2.0F * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */         } else {
/*      */           
/* 1949 */           dbWound = new DbWound((byte)10, (byte)pos, (float)damage / 2.0F * armourMod, defender.getWurmId(), poison, infection, (performer != null && performer.isPlayer()), spell);
/*      */         }  } 
/* 1951 */       if (!foundWound)
/* 1952 */         dead = defender.getBody().addWound((Wound)dbWound); 
/*      */     } 
/* 1954 */     return dead;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean addInternalWound(@Nullable Creature performer, @Nonnull Creature defender, int pos, double damage, float armourMod, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/* 1960 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1962 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, (byte)9, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1965 */     if (defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 1967 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, (byte)9, pos, armourMod, damage);
/*      */     }
/*      */     
/* 1970 */     if (defender.isGhost() || defender.isUnique())
/* 1971 */       return false; 
/* 1972 */     boolean dead = false;
/*      */     
/* 1974 */     if (damage * armourMod > 500.0D || noMinimumDamage) {
/*      */       DbWound dbWound;
/* 1976 */       Wound wound = null;
/* 1977 */       boolean foundWound = false;
/* 1978 */       if (performer != null) {
/*      */         
/* 1980 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 1981 */         segments.add(new MulticolorLineSegment("Your weapon", (byte)3));
/* 1982 */         segments.add(new MulticolorLineSegment(" causes pain deep inside ", (byte)3));
/* 1983 */         segments.add(new CreatureLineSegment(defender));
/* 1984 */         segments.add(new MulticolorLineSegment(".", (byte)3));
/*      */         
/* 1986 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 1988 */         segments.set(0, new CreatureLineSegment(performer));
/* 1989 */         for (MulticolorLineSegment s : segments) {
/* 1990 */           s.setColor((byte)7);
/*      */         }
/* 1992 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1998 */       if (defender.getBody().getWounds() != null) {
/*      */         
/* 2000 */         wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte)pos, (byte)9);
/* 2001 */         if (wound != null)
/*      */         {
/* 2003 */           if (wound.getType() == 9) {
/*      */             
/* 2005 */             defender.setWounded();
/* 2006 */             wound.setBandaged(false);
/* 2007 */             dead = wound.modifySeverity((int)(damage * armourMod), (performer != null && performer.isPlayer()), spell);
/* 2008 */             foundWound = true;
/*      */           } else {
/*      */             
/* 2011 */             wound = null;
/*      */           }  } 
/*      */       } 
/* 2014 */       if (wound == null)
/*      */       {
/* 2016 */         if (WurmId.getType(defender.getWurmId()) == 1) {
/* 2017 */           TempWound tempWound = new TempWound((byte)9, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */         } else {
/*      */           
/* 2020 */           dbWound = new DbWound((byte)9, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, (performer != null && performer.isPlayer()), spell);
/*      */         } 
/*      */       }
/* 2023 */       if (!foundWound)
/* 2024 */         dead = defender.getBody().addWound((Wound)dbWound); 
/*      */     } 
/* 2026 */     return dead;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean addColdWound(@Nullable Creature performer, @Nonnull Creature defender, int pos, double damage, float armourMod, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/* 2032 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 2034 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, (byte)8, pos, armourMod, damage);
/*      */     }
/*      */     
/* 2037 */     if (defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 2039 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, (byte)8, pos, armourMod, damage);
/*      */     }
/*      */     
/* 2042 */     if (defender.getCultist() != null && defender.getCultist().hasNoElementalDamage())
/* 2043 */       return false; 
/* 2044 */     boolean dead = false;
/* 2045 */     if (damage * armourMod > 500.0D || noMinimumDamage) {
/*      */       DbWound dbWound;
/* 2047 */       if (defender.hasSpellEffect((byte)68)) {
/*      */         
/* 2049 */         defender.reduceStoneSkin();
/* 2050 */         return false;
/*      */       } 
/* 2052 */       Wound wound = null;
/* 2053 */       boolean foundWound = false;
/* 2054 */       if (performer != null) {
/*      */         
/* 2056 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2057 */         segments.add(new MulticolorLineSegment("Your weapon", (byte)3));
/* 2058 */         segments.add(new MulticolorLineSegment(" freezes ", (byte)3));
/* 2059 */         segments.add(new CreatureLineSegment(defender));
/* 2060 */         segments.add(new MulticolorLineSegment(".", (byte)3));
/*      */         
/* 2062 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 2064 */         segments.set(0, new CreatureLineSegment(performer));
/* 2065 */         for (MulticolorLineSegment s : segments) {
/* 2066 */           s.setColor((byte)7);
/*      */         }
/* 2068 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2074 */       if (defender.getBody().getWounds() != null) {
/*      */         
/* 2076 */         wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte)pos, (byte)8);
/* 2077 */         if (wound != null)
/*      */         {
/* 2079 */           if (wound.getType() == 8) {
/*      */             
/* 2081 */             defender.setWounded();
/* 2082 */             wound.setBandaged(false);
/* 2083 */             dead = wound.modifySeverity((int)(damage * armourMod), (performer != null && performer.isPlayer()), spell);
/* 2084 */             foundWound = true;
/*      */           } else {
/*      */             
/* 2087 */             wound = null;
/*      */           }  } 
/*      */       } 
/* 2090 */       if (wound == null)
/*      */       {
/* 2092 */         if (WurmId.getType(defender.getWurmId()) == 1) {
/* 2093 */           TempWound tempWound = new TempWound((byte)8, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */         } else {
/*      */           
/* 2096 */           dbWound = new DbWound((byte)8, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, (performer != null && performer.isPlayer()), spell);
/*      */         } 
/*      */       }
/* 2099 */       if (!foundWound)
/* 2100 */         dead = defender.getBody().addWound((Wound)dbWound); 
/*      */     } 
/* 2102 */     return dead;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean addDrownWound(@Nullable Creature performer, @Nonnull Creature defender, int pos, double damage, float armourMod, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/* 2108 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 2110 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, (byte)7, pos, armourMod, damage);
/*      */     }
/*      */     
/* 2113 */     if (defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 2115 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, (byte)7, pos, armourMod, damage);
/*      */     }
/*      */ 
/*      */     
/* 2119 */     if (defender.getCultist() != null && defender.getCultist().hasNoElementalDamage())
/* 2120 */       return false; 
/* 2121 */     if (defender.isSubmerged())
/*      */     {
/* 2123 */       return false;
/*      */     }
/* 2125 */     boolean dead = false;
/* 2126 */     if (damage * armourMod > 500.0D || noMinimumDamage) {
/*      */       DbWound dbWound;
/* 2128 */       Wound wound = null;
/* 2129 */       boolean foundWound = false;
/* 2130 */       if (performer != null) {
/*      */         
/* 2132 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2133 */         segments.add(new MulticolorLineSegment("Your weapon", (byte)3));
/* 2134 */         segments.add(new MulticolorLineSegment(" drowns ", (byte)3));
/* 2135 */         segments.add(new CreatureLineSegment(defender));
/* 2136 */         segments.add(new MulticolorLineSegment(".", (byte)3));
/*      */         
/* 2138 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 2140 */         segments.set(0, new CreatureLineSegment(performer));
/* 2141 */         for (MulticolorLineSegment s : segments) {
/* 2142 */           s.setColor((byte)7);
/*      */         }
/* 2144 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2150 */       if (defender.getBody().getWounds() != null) {
/*      */         
/* 2152 */         wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte)pos, (byte)7);
/* 2153 */         if (wound != null)
/*      */         {
/* 2155 */           if (wound.getType() == 7) {
/*      */             
/* 2157 */             defender.setWounded();
/* 2158 */             wound.setBandaged(false);
/* 2159 */             dead = wound.modifySeverity((int)(damage * armourMod), (performer != null && performer.isPlayer()), spell);
/* 2160 */             foundWound = true;
/*      */           } else {
/*      */             
/* 2163 */             wound = null;
/*      */           }  } 
/*      */       } 
/* 2166 */       if (wound == null)
/*      */       {
/* 2168 */         if (WurmId.getType(defender.getWurmId()) == 1) {
/* 2169 */           TempWound tempWound = new TempWound((byte)7, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */         } else {
/*      */           
/* 2172 */           dbWound = new DbWound((byte)7, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, (performer != null && performer.isPlayer()), spell);
/*      */         } 
/*      */       }
/* 2175 */       if (!foundWound)
/* 2176 */         dead = defender.getBody().addWound((Wound)dbWound); 
/*      */     } 
/* 2178 */     if (dead && defender.isPlayer())
/* 2179 */       defender.achievement(98); 
/* 2180 */     return dead;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean addBounceWound(@Nullable Creature performer, @Nonnull Creature defender, byte type, int pos, double damage, float armourMod, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/*      */     DbWound dbWound;
/* 2186 */     if (performer != null && performer.getTemplate().getCreatureAI() != null)
/*      */     {
/* 2188 */       damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, type, pos, armourMod, damage);
/*      */     }
/*      */     
/* 2191 */     if (defender.getTemplate().getCreatureAI() != null)
/*      */     {
/* 2193 */       damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, type, pos, armourMod, damage);
/*      */     }
/*      */     
/* 2196 */     boolean dead = false;
/* 2197 */     Wound wound = null;
/* 2198 */     boolean foundWound = false;
/* 2199 */     if (performer != null) {
/*      */       
/* 2201 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2202 */       segments.add(new MulticolorLineSegment("A sudden pain hits ", (byte)3));
/* 2203 */       segments.add(new CreatureLineSegment(defender));
/* 2204 */       segments.add(new MulticolorLineSegment(" in the same location that " + defender.getHeSheItString() + " hit ", (byte)3));
/* 2205 */       segments.add(new CreatureLineSegment(performer));
/* 2206 */       segments.add(new MulticolorLineSegment(".", (byte)3));
/*      */       
/* 2208 */       performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */       
/* 2210 */       for (MulticolorLineSegment s : segments) {
/* 2211 */         s.setColor((byte)7);
/*      */       }
/* 2213 */       defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2220 */     if (defender.getBody().getWounds() != null) {
/*      */       
/* 2222 */       wound = defender.getBody().getWounds().getWoundAtLocation((byte)pos);
/* 2223 */       if (wound != null) {
/*      */         
/* 2225 */         defender.setWounded();
/* 2226 */         wound.setBandaged(false);
/* 2227 */         dead = wound.modifySeverity((int)(damage * armourMod), (performer != null && performer.isPlayer()), spell);
/* 2228 */         foundWound = true;
/*      */       } 
/*      */     } 
/* 2231 */     if (wound == null) {
/*      */       
/* 2233 */       if (WurmId.getType(defender.getWurmId()) == 1) {
/* 2234 */         TempWound tempWound = new TempWound(type, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, spell);
/*      */       } else {
/*      */         
/* 2237 */         dbWound = new DbWound(type, (byte)pos, (float)damage * armourMod, defender.getWurmId(), poison, infection, (performer != null && performer.isPlayer()), spell);
/* 2238 */       }  defender.setWounded();
/*      */     } 
/*      */     
/* 2241 */     if (!foundWound)
/* 2242 */       dead = defender.getBody().addWound((Wound)dbWound); 
/* 2243 */     return dead;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static double secondaryParry(Creature performer, double attCheck, Creature defender, Skills defenderSkills, double defCheck, double defBonus, boolean dryrun) {
/* 2250 */     Item leftWeapon = defender.getLefthandWeapon();
/* 2251 */     if (leftWeapon != null) {
/*      */       
/* 2253 */       int secSkillNum = -10;
/* 2254 */       Skill secWeaponSkill = null;
/* 2255 */       if (leftWeapon.isBodyPart() && leftWeapon.getAuxData() != 100) {
/*      */ 
/*      */         
/*      */         try {
/* 2259 */           secSkillNum = 10052;
/* 2260 */           secWeaponSkill = defenderSkills.getSkill(secSkillNum);
/*      */         }
/* 2262 */         catch (NoSuchSkillException nss) {
/*      */           
/* 2264 */           if (secSkillNum != -10)
/* 2265 */             secWeaponSkill = defenderSkills.learn(secSkillNum, 1.0F); 
/*      */         } 
/* 2267 */         if (performer.isPlayer() && defender.isPlayer())
/*      */         {
/* 2269 */           secWeaponSkill = null;
/*      */         }
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 2276 */           secSkillNum = leftWeapon.getPrimarySkill();
/* 2277 */           secWeaponSkill = defenderSkills.getSkill(secSkillNum);
/*      */         }
/* 2279 */         catch (NoSuchSkillException nss) {
/*      */           
/* 2281 */           if (secSkillNum != -10)
/* 2282 */             secWeaponSkill = defenderSkills.learn(secSkillNum, 1.0F); 
/*      */         } 
/*      */       } 
/* 2285 */       if (secWeaponSkill != null) {
/*      */         
/* 2287 */         float mod = getMod(performer, defender, secWeaponSkill);
/* 2288 */         defCheck = secWeaponSkill.skillCheck((attCheck * defender.getAttackers() + leftWeapon.getWeightGrams() / 200.0D) / 
/* 2289 */             getWeaponParryBonus(leftWeapon), leftWeapon, defBonus, (mod == 0.0F || dryrun), (float)(long)mod);
/*      */       } 
/*      */     } 
/* 2292 */     return defCheck;
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getWeaponParryBonus(Item weapon) {
/* 2297 */     if (weapon.isWeaponSword())
/* 2298 */       return 4.0F; 
/* 2299 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getMissString(boolean perf, double miss) {
/* 2304 */     if (!perf) {
/*      */       
/* 2306 */       if (miss < 10.0D)
/* 2307 */         return "barely misses."; 
/* 2308 */       if (miss < 30.0D)
/* 2309 */         return "misses by a few inches."; 
/* 2310 */       if (miss < 60.0D)
/* 2311 */         return "misses by a decimeter."; 
/* 2312 */       if (miss < 90.0D)
/* 2313 */         return "isn't even close."; 
/* 2314 */       if (miss < 100.0D) {
/* 2315 */         return "swings a huge hole in the air instead.";
/*      */       }
/* 2317 */       return "misses.";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2322 */     if (miss < 10.0D)
/* 2323 */       return "barely miss."; 
/* 2324 */     if (miss < 30.0D)
/* 2325 */       return "miss by a few inches."; 
/* 2326 */     if (miss < 60.0D)
/* 2327 */       return "miss by a decimeter."; 
/* 2328 */     if (miss < 90.0D)
/* 2329 */       return "aren't even close."; 
/* 2330 */     if (miss < 100.0D) {
/* 2331 */       return "swing a huge hole in the air instead.";
/*      */     }
/* 2333 */     return "miss.";
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
/*      */   public static double getWeaponDamage(Item weapon, Skill attStrength) {
/* 2348 */     if (weapon.isBodyPart() && weapon.getAuxData() != 100) {
/*      */       
/*      */       try {
/*      */         
/* 2352 */         float f = Server.getInstance().getCreature(weapon.getOwnerId()).getCombatDamage(weapon);
/*      */         
/* 2354 */         return (f + Server.rand.nextFloat() * f * 2.0F);
/*      */       }
/* 2356 */       catch (NoSuchCreatureException nsc) {
/*      */         
/* 2358 */         logger.log(Level.WARNING, "Could not find Creature owner of weapon: " + weapon + " due to " + nsc.getMessage(), (Throwable)nsc);
/*      */       
/*      */       }
/* 2361 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 2363 */         logger.log(Level.WARNING, "Could not find Player owner of weapon: " + weapon + " due to " + nsp.getMessage(), (Throwable)nsp);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 2368 */     float base = 6.0F;
/* 2369 */     if (weapon.isWeaponSword()) {
/*      */       
/* 2371 */       base = 24.0F;
/*      */     }
/* 2373 */     else if (weapon.isWeaponAxe()) {
/* 2374 */       base = 30.0F;
/* 2375 */     } else if (weapon.isWeaponPierce()) {
/* 2376 */       base = 12.0F;
/* 2377 */     } else if (weapon.isWeaponSlash()) {
/* 2378 */       base = 18.0F;
/* 2379 */     } else if (weapon.isWeaponCrush()) {
/* 2380 */       base = 36.0F;
/* 2381 */     } else if (weapon.isBodyPart() && weapon.getAuxData() == 100) {
/*      */       
/* 2383 */       base = 6.0F;
/*      */     } 
/* 2385 */     if (weapon.isWood()) {
/* 2386 */       base *= 0.1F;
/* 2387 */     } else if (weapon.isTool()) {
/* 2388 */       base *= 0.3F;
/*      */     } 
/* 2390 */     base = (float)(base * (1.0D + attStrength.getKnowledge(0.0D) / 100.0D));
/*      */     
/* 2392 */     float randomizer = (50.0F + Server.rand.nextFloat() * 50.0F) / 100.0F;
/*      */     
/* 2394 */     return base + (randomizer * base * 4.0F * weapon.getQualityLevel() * weapon.getDamagePercent()) / 10000.0D;
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
/*      */   public static String getParryString(double result) {
/* 2412 */     String toReturn = "easily";
/* 2413 */     if (result < 10.0D) {
/* 2414 */       toReturn = "barely";
/* 2415 */     } else if (result < 30.0D) {
/* 2416 */       toReturn = "skillfully";
/* 2417 */     } else if (result < 60.0D) {
/* 2418 */       toReturn = "safely";
/* 2419 */     }  return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getStrengthString(double damage) {
/* 2424 */     if (damage <= 0.0D)
/* 2425 */       return "unnoticeably"; 
/* 2426 */     if (damage <= 1.0D)
/* 2427 */       return "very lightly"; 
/* 2428 */     if (damage <= 2.0D)
/* 2429 */       return "lightly"; 
/* 2430 */     if (damage <= 3.0D)
/* 2431 */       return "pretty hard"; 
/* 2432 */     if (damage <= 6.0D)
/* 2433 */       return "hard"; 
/* 2434 */     if (damage <= 10.0D)
/* 2435 */       return "very hard"; 
/* 2436 */     if (damage <= 20.0D) {
/* 2437 */       return "extremely hard";
/*      */     }
/* 2439 */     return "deadly hard";
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getConjunctionString(double armour, double origDam, double realDam) {
/* 2444 */     if (armour > 0.4D)
/*      */     {
/* 2446 */       if (origDam - realDam > 5000.0D)
/* 2447 */         return "but only"; 
/*      */     }
/* 2449 */     return "and";
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getRealDamageString(double damage) {
/* 2454 */     if (damage < 500.0D)
/* 2455 */       return "tickle"; 
/* 2456 */     if (damage < 1000.0D)
/* 2457 */       return "slap"; 
/* 2458 */     if (damage < 2500.0D)
/* 2459 */       return "irritate"; 
/* 2460 */     if (damage < 5000.0D)
/* 2461 */       return "hurt"; 
/* 2462 */     if (damage < 10000.0D) {
/* 2463 */       return "harm";
/*      */     }
/* 2465 */     return "damage";
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getAttackString(Creature attacker, Item weapon, byte woundType) {
/* 2470 */     if (weapon.isWeaponSword()) {
/*      */       
/* 2472 */       if (woundType == 2) {
/* 2473 */         return "pierce";
/*      */       }
/* 2475 */       return "cut";
/*      */     } 
/* 2477 */     if (weapon.isWeaponPierce())
/* 2478 */       return "pierce"; 
/* 2479 */     if (weapon.isWeaponSlash())
/* 2480 */       return "cut"; 
/* 2481 */     if (weapon.isWeaponCrush())
/* 2482 */       return "maul"; 
/* 2483 */     if (weapon.isBodyPart() && weapon.getAuxData() != 100)
/*      */     {
/* 2485 */       return attacker.getAttackStringForBodyPart(weapon);
/*      */     }
/* 2487 */     return "hit";
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getAttackString(Creature attacker, Item weapon) {
/* 2492 */     if (weapon.isWeaponPierce())
/* 2493 */       return "pierce"; 
/* 2494 */     if (weapon.isWeaponSlash())
/* 2495 */       return "cut"; 
/* 2496 */     if (weapon.isWeaponCrush())
/* 2497 */       return "maul"; 
/* 2498 */     if (weapon.isBodyPart() && weapon.getAuxData() != 100)
/*      */     {
/* 2500 */       return attacker.getAttackStringForBodyPart(weapon);
/*      */     }
/* 2502 */     return "hit";
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean taunt(Creature performer, Creature defender, float counter, Action act) {
/* 2508 */     boolean done = false;
/* 2509 */     if (defender.isDead()) {
/*      */       
/* 2511 */       logger.log(Level.INFO, defender.getName() + " is dead when taunted by " + performer.getName());
/* 2512 */       return true;
/*      */     } 
/* 2514 */     Skill taunt = null;
/* 2515 */     int time = 70;
/* 2516 */     Skills skills = performer.getSkills();
/*      */     
/* 2518 */     Skill defPsyche = null;
/* 2519 */     Skills defSkills = defender.getSkills();
/*      */     
/*      */     try {
/* 2522 */       defPsyche = defSkills.getSkill(105);
/*      */     }
/* 2524 */     catch (NoSuchSkillException nss) {
/*      */       
/* 2526 */       defPsyche = defSkills.learn(105, 1.0F);
/*      */     } 
/* 2528 */     Skill attPsyche = null;
/*      */     
/*      */     try {
/* 2531 */       attPsyche = skills.getSkill(105);
/*      */     }
/* 2533 */     catch (NoSuchSkillException nss) {
/*      */       
/* 2535 */       attPsyche = skills.learn(105, 1.0F);
/*      */     } 
/* 2537 */     double power = 0.0D;
/*      */     
/*      */     try {
/* 2540 */       taunt = skills.getSkill(10057);
/*      */     }
/* 2542 */     catch (NoSuchSkillException nss) {
/*      */       
/* 2544 */       taunt = skills.learn(10057, 1.0F);
/*      */     } 
/* 2546 */     if (counter == 1.0F) {
/*      */       
/* 2548 */       act.setTimeLeft(time);
/* 2549 */       performer.sendActionControl(Actions.actionEntrys[103].getVerbString(), true, time);
/*      */       
/* 2551 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2552 */       segments.add(new CreatureLineSegment(performer));
/* 2553 */       segments.add(new MulticolorLineSegment(" starts to annoy ", (byte)7));
/* 2554 */       segments.add(new CreatureLineSegment(defender));
/* 2555 */       segments.add(new MulticolorLineSegment(" in all imaginable ways.", (byte)7));
/*      */       
/* 2557 */       defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */       
/* 2559 */       for (MulticolorLineSegment s : segments) {
/* 2560 */         s.setColor((byte)0);
/*      */       }
/* 2562 */       MessageServer.broadcastColoredAction(segments, performer, defender, 5, true);
/*      */       
/* 2564 */       ((MulticolorLineSegment)segments.get(1)).setText(" start to annoy ");
/* 2565 */       performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2578 */       time = act.getTimeLeft();
/*      */     } 
/* 2580 */     if (counter * 10.0F > time) {
/*      */       
/* 2582 */       boolean dryrun = defender.isNoSkillFor(performer);
/* 2583 */       defender.addAttacker(performer);
/* 2584 */       float mod = getMod(performer, defender, taunt);
/* 2585 */       power = taunt.skillCheck(
/* 2586 */           Math.max(1.0D, Math.max(taunt.getRealKnowledge() - 10.0D, defPsyche.getKnowledge(0.0D) - attPsyche.getKnowledge(0.0D))), 0.0D, (mod == 0.0F || dryrun), 
/*      */           
/* 2588 */           (float)(long)Math.max(1.0F, 4.0F * mod), performer, defender);
/* 2589 */       if (power > 0.0D) {
/*      */ 
/*      */         
/* 2592 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2593 */         segments.add(new CreatureLineSegment(defender));
/* 2594 */         segments.add(new MulticolorLineSegment(" sees red and turns to attack ", (byte)0));
/* 2595 */         segments.add(new CreatureLineSegment(performer));
/* 2596 */         segments.add(new MulticolorLineSegment(" instead.", (byte)0));
/*      */         
/* 2598 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/* 2599 */         MessageServer.broadcastColoredAction(segments, performer, defender, 5, true);
/*      */         
/* 2601 */         for (MulticolorLineSegment s : segments) {
/* 2602 */           s.setColor((byte)7);
/*      */         }
/* 2604 */         ((MulticolorLineSegment)segments.get(1)).setText(" see red and turn to attack ");
/* 2605 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2616 */         defender.removeTarget(defender.target);
/* 2617 */         defender.setTarget(performer.getWurmId(), true);
/* 2618 */         defender.setOpponent(performer);
/*      */ 
/*      */         
/* 2621 */         performer.achievement(563);
/*      */       }
/*      */       else {
/*      */         
/* 2625 */         ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2626 */         segments.add(new CreatureLineSegment(defender));
/* 2627 */         segments.add(new MulticolorLineSegment(" ignores your antics.", (byte)0));
/*      */         
/* 2629 */         performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 2631 */         segments.clear();
/* 2632 */         segments.add(new CreatureLineSegment(performer));
/* 2633 */         segments.add(new MulticolorLineSegment(" tires and ceases taunting you.", (byte)0));
/*      */         
/* 2635 */         defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */         
/* 2637 */         segments.clear();
/* 2638 */         segments.add(new CreatureLineSegment(defender));
/* 2639 */         segments.add(new MulticolorLineSegment(" ignores ", (byte)0));
/* 2640 */         segments.add(new CreatureLineSegment(performer));
/* 2641 */         segments.add(new MulticolorLineSegment("'s antics.", (byte)0));
/*      */         
/* 2643 */         MessageServer.broadcastColoredAction(segments, performer, defender, 5, true);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2653 */       done = true;
/*      */     } 
/* 2655 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean shieldBash(Creature performer, Creature defender, float counter) {
/* 2661 */     boolean done = false;
/* 2662 */     Skill bash = null;
/* 2663 */     if (defender.isDead()) {
/* 2664 */       return true;
/*      */     }
/* 2666 */     Item shield = performer.getShield();
/* 2667 */     if (defender.equals(performer))
/*      */     {
/* 2669 */       return true;
/*      */     }
/* 2671 */     if (defender.isStunned()) {
/*      */       
/* 2673 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2674 */       segments.add(new MulticolorLineSegment("You can not bash ", (byte)0));
/* 2675 */       segments.add(new CreatureLineSegment(defender));
/* 2676 */       segments.add(new MulticolorLineSegment(" because " + defender.getHeSheItString() + " is already stunned.", (byte)0));
/*      */       
/* 2678 */       performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */       
/* 2682 */       return true;
/*      */     } 
/* 2684 */     if (!performer.getCombatHandler().mayShieldBash()) {
/*      */       
/* 2686 */       performer.getCommunicator().sendCombatNormalMessage("You are still gaining strength from your last shield bash.");
/* 2687 */       done = true;
/*      */     }
/* 2689 */     else if (shield == null) {
/*      */       
/* 2691 */       performer.getCommunicator().sendCombatNormalMessage("You need to wear the shield to bash someone with it.");
/* 2692 */       done = true;
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 2699 */         boolean dryrun = (defender.isNoSkillFor(performer) || defender.isNoSkillgain() || (defender.isPlayer() && (!defender.isPaying() || defender.isNewbie())));
/*      */         
/* 2701 */         int time = 50;
/* 2702 */         Action act = performer.getCurrentAction();
/* 2703 */         if (!defender.isFighting())
/* 2704 */           defender.setOpponent(performer); 
/* 2705 */         if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2716 */           act.setTimeLeft(time);
/* 2717 */           performer.sendActionControl(Actions.actionEntrys[105].getVerbString(), true, time);
/*      */ 
/*      */           
/* 2720 */           ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2721 */           segments.add(new MulticolorLineSegment("You aim to push ", (byte)0));
/* 2722 */           segments.add(new CreatureLineSegment(defender));
/* 2723 */           segments.add(new MulticolorLineSegment(" over with your shield.", (byte)0));
/*      */           
/* 2725 */           performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 2732 */           time = act.getTimeLeft();
/*      */         } 
/*      */         
/* 2735 */         if (counter * 10.0F > time)
/*      */         {
/* 2737 */           Skills skills = performer.getSkills();
/*      */           
/* 2739 */           Skill defBodyControl = null;
/* 2740 */           Skills defSkills = defender.getSkills();
/* 2741 */           Skill attStrength = null;
/*      */           
/*      */           try {
/* 2744 */             defBodyControl = defSkills.getSkill(104);
/*      */           }
/* 2746 */           catch (NoSuchSkillException nss) {
/*      */             
/* 2748 */             defBodyControl = defSkills.learn(104, 1.0F);
/*      */           } 
/*      */           
/*      */           try {
/* 2752 */             attStrength = skills.getSkill(102);
/*      */           }
/* 2754 */           catch (NoSuchSkillException nss) {
/*      */             
/* 2756 */             attStrength = skills.learn(102, 1.0F);
/*      */           } 
/* 2758 */           double power = 0.0D;
/* 2759 */           double bonus = 0.0D;
/* 2760 */           int skillnum = -10;
/* 2761 */           Skill shieldSkill = null;
/* 2762 */           if (shield != null)
/*      */             
/*      */             try {
/*      */               
/* 2766 */               skillnum = shield.getPrimarySkill();
/* 2767 */               shieldSkill = skills.getSkill(skillnum);
/*      */             }
/* 2769 */             catch (NoSuchSkillException nss) {
/*      */               
/* 2771 */               if (skillnum != -10) {
/* 2772 */                 shieldSkill = skills.learn(skillnum, 1.0F);
/*      */               }
/*      */             }  
/* 2775 */           float mod = 1.0F;
/* 2776 */           if (shieldSkill != null) {
/*      */             
/* 2778 */             mod = getMod(performer, defender, shieldSkill);
/* 2779 */             bonus = shieldSkill.skillCheck(defBodyControl.getKnowledge(0.0D), 0.0D, dryrun, (float)(long)Math.min(1.0F, mod), defender, performer);
/*      */           } 
/*      */           
/* 2782 */           if (attStrength != null) {
/* 2783 */             bonus += attStrength.getKnowledge(0.0D);
/*      */           }
/*      */           try {
/* 2786 */             bash = skills.getSkill(10058);
/*      */           }
/* 2788 */           catch (NoSuchSkillException nss) {
/*      */             
/* 2790 */             bash = skills.learn(10058, 1.0F);
/*      */           } 
/* 2792 */           mod = getMod(performer, defender, bash);
/* 2793 */           defender.addAttacker(performer);
/* 2794 */           Methods.sendSound(defender, "sound.combat.shield.bash");
/* 2795 */           float materialModifier = 1.0F;
/* 2796 */           if (shield.isMetal()) {
/* 2797 */             materialModifier = 2.0F;
/*      */           }
/*      */           
/* 2800 */           int weightModifier = 40000;
/*      */           
/* 2802 */           double diff = (defender.getWeight() / defender.getTemplate().getWeight());
/*      */           
/* 2804 */           if (defender.isPlayer())
/*      */           {
/*      */             
/* 2807 */             diff = ((defender.getWeight() + Math.max(0, defender.getBody().getBodyItem().getFullWeight() + defender.getInventory().getFullWeight() - 40000)) / defender.getTemplate().getWeight());
/*      */           }
/* 2809 */           boolean dodge = true;
/* 2810 */           boolean topple = false;
/* 2811 */           if (diff > 1.0D) {
/*      */ 
/*      */             
/* 2814 */             dodge = false;
/* 2815 */             if (defender.getMovePenalty() > 0) {
/* 2816 */               topple = true;
/*      */             }
/*      */           } 
/* 2819 */           diff = defBodyControl.getKnowledge(0.0D) * diff / Math.max(1, defender.getMovePenalty() / 3);
/* 2820 */           power = bash.skillCheck(diff * ItemBonus.getBashDodgeBonusFor(defender), bonus / 10.0D + (shield.getWeightGrams() * materialModifier) / 1000.0D, (mod == 0.0F || dryrun), (float)((long)mod * 2L), performer, defender);
/*      */ 
/*      */           
/* 2823 */           defender.getCombatHandler().increaseUseShieldCounter();
/* 2824 */           performer.getCombatHandler().shieldBash();
/* 2825 */           if (power > 0.0D) {
/*      */ 
/*      */             
/*      */             try {
/* 2829 */               int pos = defender.getBody().getRandomWoundPos();
/* 2830 */               float armourMod = defender.getArmourMod();
/* 2831 */               double damage = Math.max(500.0D, Server.rand.nextDouble() * bash.getKnowledge(0.0D) * 50.0D);
/* 2832 */               if (!performer.isPlayer() && !defender.isPlayer())
/* 2833 */                 armourMod = 1.0F; 
/* 2834 */               if (armourMod == 1.0F || defender.isVehicle()) {
/*      */                 
/*      */                 try {
/*      */                   
/* 2838 */                   byte bodyPosition = ArmourTemplate.getArmourPosition((byte)pos);
/* 2839 */                   Item armour = defender.getArmour(bodyPosition);
/* 2840 */                   armourMod = ArmourTemplate.calculateDR(armour, (byte)0);
/* 2841 */                   if (defender.isPlayer())
/* 2842 */                     armour.setDamage(armour.getDamage() + 
/* 2843 */                         Math.min(1.0F, (float)(damage * armourMod / 80000.0D) * armour
/* 2844 */                           .getDamageModifier() * 
/* 2845 */                           ArmourTemplate.getArmourDamageModFor(armour, (byte)0))); 
/* 2846 */                   checkEnchantDestruction(shield, armour, defender);
/*      */                 }
/* 2848 */                 catch (NoArmourException noArmourException) {
/*      */ 
/*      */                 
/*      */                 }
/* 2852 */                 catch (NoSpaceException nsp) {
/*      */                   
/* 2854 */                   logger.log(Level.WARNING, defender.getName() + " no armour space on loc " + pos);
/*      */                 } 
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2861 */               if (defender.getBonusForSpellEffect((byte)22) > 0.0F)
/*      */               {
/* 2863 */                 if (armourMod >= 1.0F) {
/* 2864 */                   armourMod = 0.2F + (1.0F - defender.getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F;
/*      */                 } else {
/*      */                   
/* 2867 */                   armourMod = Math.min(armourMod, 0.2F + (1.0F - defender
/* 2868 */                       .getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F);
/*      */                 }  } 
/* 2870 */               if (damage * armourMod > 500.0D) {
/*      */                 
/* 2872 */                 if (shieldSkill != null) {
/*      */                   
/* 2874 */                   mod = getMod(performer, defender, shieldSkill);
/* 2875 */                   bonus = shieldSkill.skillCheck(defBodyControl.getKnowledge(0.0D), 0.0D, dryrun, (float)(long)(mod * 3.0F), defender, performer);
/*      */                 } 
/*      */                 
/* 2878 */                 mod = getMod(performer, defender, bash);
/* 2879 */                 power = bash.skillCheck(defBodyControl.getKnowledge(0.0D), bonus / 10.0D + (shield.getWeightGrams() * materialModifier) / 1000.0D, (mod == 0.0F || dryrun), (float)(long)(mod * 4.0F), performer, defender);
/*      */               } 
/*      */               
/* 2882 */               addWound(performer, defender, (byte)0, pos, damage, armourMod, "hurt", performer
/* 2883 */                   .getBattle(), 0.0F, 0.0F, false, false, false, false);
/*      */             }
/* 2885 */             catch (Exception ex) {
/*      */               
/* 2887 */               logger.log(Level.WARNING, defender.getName() + ":" + ex.getMessage(), ex);
/*      */             } 
/* 2889 */             defender.maybeInterruptAction(200000);
/* 2890 */             String pushtopple = topple ? " topples " : " pushes ";
/*      */             
/* 2892 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2893 */             segments.add(new CreatureLineSegment(performer));
/* 2894 */             segments.add(new MulticolorLineSegment(pushtopple, (byte)7));
/* 2895 */             segments.add(new CreatureLineSegment(defender));
/* 2896 */             segments.add(new MulticolorLineSegment(" over with " + performer.getHisHerItsString() + " shield.", (byte)7));
/*      */             
/* 2898 */             defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */             
/* 2900 */             for (MulticolorLineSegment s : segments)
/* 2901 */               s.setColor((byte)0); 
/* 2902 */             MessageServer.broadcastColoredAction(segments, performer, defender, 5, true);
/*      */             
/* 2904 */             ArrayList<MulticolorLineSegment> segmentsPerformer = new ArrayList<>();
/* 2905 */             segmentsPerformer.add(new CreatureLineSegment(defender));
/* 2906 */             segmentsPerformer.add(new MulticolorLineSegment(" is sprawling on the ground.", (byte)0));
/*      */             
/* 2908 */             performer.getCommunicator().sendColoredMessageCombat(segmentsPerformer);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2923 */             defender.playAnimation("sprawl", false);
/* 2924 */             defender.getStatus().setStunned((byte)(int)Math.max(2.0D, power / 100.0D * 10.0D));
/* 2925 */             performer.getStatus().modifyStamina(-2000.0F);
/*      */ 
/*      */           
/*      */           }
/* 2929 */           else if (dodge) {
/*      */             
/* 2931 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2932 */             segments.add(new CreatureLineSegment(defender));
/* 2933 */             segments.add(new MulticolorLineSegment(" swiftly dodges your bash.", (byte)0));
/*      */             
/* 2935 */             performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2941 */             segments.clear();
/* 2942 */             segments.add(new MulticolorLineSegment("You swiftly dodge the shield bash from ", (byte)0));
/* 2943 */             segments.add(new CreatureLineSegment(defender));
/* 2944 */             segments.add(new MulticolorLineSegment(".", (byte)0));
/*      */             
/* 2946 */             defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */             
/* 2949 */             performer.getStatus().modifyStamina(-500.0F);
/*      */           }
/*      */           else {
/*      */             
/* 2953 */             ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 2954 */             segments.add(new CreatureLineSegment(defender));
/* 2955 */             segments.add(new MulticolorLineSegment(" keeps " + defender.getHisHerItsString() + " balance.", (byte)0));
/*      */             
/* 2957 */             performer.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2963 */             segments.clear();
/* 2964 */             segments.add(new MulticolorLineSegment("You keep your balance after the shield bash from ", (byte)0));
/* 2965 */             segments.add(new CreatureLineSegment(defender));
/* 2966 */             segments.add(new MulticolorLineSegment(".", (byte)0));
/*      */             
/* 2968 */             defender.getCommunicator().sendColoredMessageCombat(segments);
/*      */ 
/*      */             
/* 2971 */             performer.getStatus().modifyStamina(-500.0F);
/*      */           } 
/*      */           
/* 2974 */           done = true;
/*      */         }
/*      */       
/* 2977 */       } catch (NoSuchActionException nsa) {
/*      */         
/* 2979 */         done = true;
/* 2980 */         logger.log(Level.WARNING, "Performer: " + performer.getName() + ", Defender: " + defender.getName() + " this action doesn't exist?");
/*      */       } 
/*      */     } 
/*      */     
/* 2984 */     return done;
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
/*      */   @Deprecated
/*      */   public static boolean attack(Creature performer, Item target, float counter, Action act) {
/* 3020 */     return attack(performer, target, counter, -1, act);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean attack(Creature performer, Item target, float counter, int pos, Action act) {
/* 3027 */     boolean done = false;
/* 3028 */     boolean dead = false;
/* 3029 */     boolean aiming = false;
/* 3030 */     Item primWeapon = performer.getPrimWeapon();
/*      */     
/* 3032 */     if (primWeapon == null || primWeapon.isBodyPart()) {
/*      */ 
/*      */       
/* 3035 */       performer.getCommunicator().sendNormalServerMessage("You have no weapon to attack " + target.getNameWithGenus() + " with.");
/* 3036 */       return true;
/*      */     } 
/* 3038 */     if (primWeapon.isShield()) {
/*      */ 
/*      */       
/* 3041 */       performer.getCommunicator().sendNormalServerMessage("You cannot practice attacks with shields on " + target
/* 3042 */           .getNameWithGenus() + ".");
/* 3043 */       return true;
/*      */     } 
/* 3045 */     if (primWeapon.isWeaponBow() || primWeapon.isBowUnstringed()) {
/*      */ 
/*      */       
/* 3048 */       performer.getCommunicator().sendNormalServerMessage("You cannot practice attacks with bows on " + target
/* 3049 */           .getNameWithGenus() + ". You need to use an archery target instead.");
/*      */       
/* 3051 */       return true;
/*      */     } 
/*      */     
/* 3054 */     BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/* 3055 */     if (result != null) {
/*      */       
/* 3057 */       performer.getCommunicator()
/* 3058 */         .sendCombatNormalMessage("You fail to reach the " + target
/* 3059 */           .getNameWithGenus() + " because of the " + result
/* 3060 */           .getFirstBlocker().getName() + ".");
/* 3061 */       return true;
/*      */     } 
/*      */     
/* 3064 */     if (Creature.rangeTo(performer, target) > Actions.actionEntrys[114].getRange()) {
/*      */       
/* 3066 */       performer.getCommunicator().sendNormalServerMessage("You are now too far away to " + Actions.actionEntrys[114]
/* 3067 */           .getActionString().toLowerCase() + " " + target
/* 3068 */           .getNameWithGenus() + ".");
/* 3069 */       return true;
/*      */     } 
/* 3071 */     int speed = 10;
/* 3072 */     speed = primWeapon.getWeightGrams() / 1000 + 3;
/* 3073 */     if (pos != -1) {
/*      */       
/* 3075 */       aiming = true;
/* 3076 */       speed++;
/*      */     } 
/* 3078 */     if (!done) {
/*      */ 
/*      */       
/* 3081 */       if (act.justTickedSecond())
/* 3082 */         performer.decreaseFatigue(); 
/* 3083 */       if (counter == 1.0F)
/*      */       {
/* 3085 */         if (aiming) {
/*      */           
/* 3087 */           String bodypartname = PracticeDollBehaviour.getWoundLocationString(pos);
/* 3088 */           performer.getCommunicator().sendSafeServerMessage("You try to " + 
/* 3089 */               getAttackString(performer, primWeapon) + " " + target.getNameWithGenus() + " in the " + bodypartname + ".");
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 3094 */           performer.getCommunicator().sendSafeServerMessage("You try to " + 
/* 3095 */               getAttackString(performer, primWeapon) + " " + target.getNameWithGenus() + ".");
/*      */         } 
/*      */       }
/*      */       
/* 3099 */       if (act.currentSecond() % speed == 0) {
/*      */         
/* 3101 */         Skill attackerFightSkill = null;
/* 3102 */         Skills performerSkills = performer.getSkills();
/*      */         
/* 3104 */         double attBonus = 0.0D;
/*      */ 
/*      */         
/* 3107 */         int attSknum = 1023;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 3122 */           attackerFightSkill = performerSkills.getSkill(1023);
/*      */         }
/* 3124 */         catch (NoSuchSkillException nss) {
/*      */           
/* 3126 */           attackerFightSkill = performerSkills.learn(1023, 1.0F);
/*      */         } 
/* 3128 */         dead = performAttack(pos, false, performer, performerSkills, primWeapon, target, 0.0D, attackerFightSkill, speed);
/*      */ 
/*      */         
/* 3131 */         if (aiming)
/* 3132 */           done = true; 
/* 3133 */         if (dead)
/* 3134 */           done = true; 
/*      */       } 
/* 3136 */       if (!done && !aiming) {
/*      */         
/* 3138 */         Item[] secondaryWeapons = performer.getSecondaryWeapons();
/* 3139 */         for (int x = 0; x < secondaryWeapons.length; x++) {
/*      */           
/* 3141 */           if (!secondaryWeapons[x].isBodyPart()) {
/*      */             
/* 3143 */             speed = Server.rand.nextInt(secondaryWeapons[x].getWeightGrams() / 1000 + 7) + 2;
/* 3144 */             if (act.currentSecond() % speed == 0) {
/*      */               
/* 3146 */               Skill attackerFightSkill = null;
/* 3147 */               Skills performerSkills = performer.getSkills();
/*      */               
/* 3149 */               double attBonus = 0.0D;
/*      */ 
/*      */ 
/*      */               
/* 3153 */               int attSknum = 1023;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/* 3168 */                 attackerFightSkill = performerSkills.getSkill(1023);
/*      */               }
/* 3170 */               catch (NoSuchSkillException nss) {
/*      */                 
/* 3172 */                 attackerFightSkill = performerSkills.learn(1023, 1.0F);
/*      */               } 
/* 3174 */               done = performAttack(pos, false, performer, performerSkills, secondaryWeapons[x], target, 0.0D, attackerFightSkill, speed);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3181 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected static boolean performAttack(int pos, boolean aiming, Creature performer, Skills performerSkills, Item attWeapon, Item target, double attBonus, Skill attackerFightSkill, int counter) {
/* 3189 */     if (!performer.hasLink())
/* 3190 */       return true; 
/* 3191 */     boolean done = false;
/* 3192 */     Skill primWeaponSkill = null;
/* 3193 */     int skillnum = -10;
/* 3194 */     performer.getStatus().modifyStamina(-1000.0F);
/* 3195 */     if (attWeapon != null) {
/*      */       
/*      */       try {
/*      */         
/* 3199 */         skillnum = attWeapon.getPrimarySkill();
/* 3200 */         primWeaponSkill = performerSkills.getSkill(skillnum);
/*      */       }
/* 3202 */       catch (NoSuchSkillException nss) {
/*      */         
/* 3204 */         if (skillnum != -10) {
/* 3205 */           primWeaponSkill = performerSkills.learn(skillnum, 1.0F);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3215 */       Skill attStrengthSkill = performerSkills.getSkill(102);
/*      */     }
/* 3217 */     catch (NoSuchSkillException nss) {
/*      */       
/* 3219 */       Skill attStrengthSkill = performerSkills.learn(102, 1.0F);
/* 3220 */       logger.log(Level.WARNING, performer.getName() + " had no strength. Weird.");
/*      */     } 
/*      */     
/* 3223 */     double bonus = 0.0D;
/* 3224 */     if (primWeaponSkill != null) {
/*      */       
/* 3226 */       boolean dryrun = (primWeaponSkill.getKnowledge(0.0D) >= 20.0D);
/* 3227 */       bonus = Math.max(0.0D, primWeaponSkill
/*      */           
/* 3229 */           .skillCheck((attWeapon.getCurrentQualityLevel() + 10.0F), attBonus, dryrun, 
/* 3230 */             Math.max(1, counter / 2)));
/*      */     } 
/*      */     
/* 3233 */     if (aiming) {
/*      */       
/* 3235 */       int rand = 10;
/* 3236 */       if (pos == 1) {
/*      */         
/* 3238 */         rand = Server.rand.nextInt(100);
/* 3239 */         bonus = -60.0D;
/* 3240 */         if (rand < 50) {
/* 3241 */           pos = 17;
/*      */         }
/* 3243 */       } else if (pos == 29) {
/*      */         
/* 3245 */         rand = Server.rand.nextInt(100);
/* 3246 */         bonus = -80.0D;
/* 3247 */         if (rand < 98) {
/* 3248 */           pos = 29;
/* 3249 */         } else if (rand < 99) {
/* 3250 */           pos = 18;
/* 3251 */         } else if (rand < 100) {
/* 3252 */           pos = 19;
/*      */         } 
/* 3254 */       } else if (pos == 2) {
/*      */         
/* 3256 */         rand = Server.rand.nextInt(20);
/* 3257 */         bonus = -40.0D;
/* 3258 */         if (rand < 5) {
/* 3259 */           pos = 21;
/* 3260 */         } else if (rand < 7) {
/* 3261 */           pos = 27;
/* 3262 */         } else if (rand < 9) {
/* 3263 */           pos = 26;
/* 3264 */         } else if (rand < 12) {
/* 3265 */           pos = 32;
/* 3266 */         } else if (rand < 14) {
/* 3267 */           pos = 23;
/* 3268 */         } else if (rand < 18) {
/* 3269 */           pos = 24;
/* 3270 */         } else if (rand < 20) {
/* 3271 */           pos = 25;
/*      */         } 
/* 3273 */       } else if (pos == 3) {
/*      */         
/* 3275 */         rand = Server.rand.nextInt(10);
/* 3276 */         bonus = -30.0D;
/* 3277 */         if (rand < 5) {
/* 3278 */           pos = 5;
/* 3279 */         } else if (rand < 9) {
/* 3280 */           pos = 9;
/*      */         } else {
/* 3282 */           pos = 13;
/*      */         } 
/* 3284 */       } else if (pos == 4) {
/*      */         
/* 3286 */         rand = Server.rand.nextInt(10);
/* 3287 */         bonus = -30.0D;
/* 3288 */         if (rand < 5) {
/* 3289 */           pos = 6;
/* 3290 */         } else if (rand < 9) {
/* 3291 */           pos = 10;
/*      */         } else {
/* 3293 */           pos = 14;
/*      */         } 
/* 3295 */       } else if (pos == 34) {
/*      */         
/* 3297 */         rand = Server.rand.nextInt(20);
/* 3298 */         bonus = -30.0D;
/* 3299 */         if (rand < 5) {
/* 3300 */           pos = 7;
/* 3301 */         } else if (rand < 9) {
/* 3302 */           pos = 11;
/* 3303 */         } else if (rand < 10) {
/* 3304 */           pos = 15;
/* 3305 */         }  if (rand < 15) {
/* 3306 */           pos = 8;
/* 3307 */         } else if (rand < 19) {
/* 3308 */           pos = 12;
/*      */         } else {
/* 3310 */           pos = 16;
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/* 3317 */         pos = PracticeDollBehaviour.getRandomWoundPos();
/*      */       }
/* 3319 */       catch (Exception ex) {
/*      */         
/* 3321 */         logger.log(Level.WARNING, "Could not get random wound position on " + target
/* 3322 */             .getName() + " due to " + ex.getMessage(), ex);
/*      */       } 
/*      */     } 
/* 3325 */     double attCheck = 0.0D;
/*      */ 
/*      */     
/* 3328 */     if (primWeaponSkill != null) {
/*      */       
/* 3330 */       boolean dryrun = (attackerFightSkill.getKnowledge(0.0D) >= 20.0D);
/* 3331 */       attCheck = attackerFightSkill.skillCheck(10.0D, attWeapon, bonus, dryrun, Math.max(1, counter / 2));
/*      */     } else {
/*      */       
/* 3334 */       attCheck = attackerFightSkill.skillCheck(10.0D, attWeapon, bonus, true, Math.max(1, counter));
/* 3335 */     }  String attString = getAttackString(performer, attWeapon);
/* 3336 */     if (attCheck > 0.0D) {
/*      */ 
/*      */ 
/*      */       
/* 3340 */       double damage = Math.max(0.1F, Server.rand.nextFloat() * Weapon.getBaseDamageForWeapon(attWeapon) / 10.0F);
/*      */ 
/*      */ 
/*      */       
/* 3344 */       if (primWeaponSkill != null) {
/* 3345 */         attWeapon.setDamage(attWeapon.getDamage() + 0.05F * Weapon.getBaseDamageForWeapon(attWeapon));
/*      */       }
/*      */ 
/*      */       
/* 3349 */       String broadCastString = performer.getNameWithGenus() + " " + attString + "s " + target.getNameWithGenus() + " " + getStrengthString(damage) + " in the " + PracticeDollBehaviour.getWoundLocationString(pos) + ".";
/*      */       
/* 3351 */       performer.getCommunicator().sendSafeServerMessage("You " + attString + " " + target
/* 3352 */           .getNameWithGenus() + " " + getStrengthString(damage) + " in the " + 
/* 3353 */           PracticeDollBehaviour.getWoundLocationString(pos) + ".");
/* 3354 */       Server.getInstance().broadCastAction(broadCastString, performer, 3);
/*      */       
/* 3356 */       done = target.setDamage(target.getDamage() + (float)damage * target.getDamageModifier());
/*      */       
/* 3358 */       int tilex = (int)target.getPosX() >> 2;
/* 3359 */       int tiley = (int)target.getPosY() >> 2;
/*      */       
/* 3361 */       String sstring = "sound.combat.parry1";
/* 3362 */       int x = Server.rand.nextInt(3);
/* 3363 */       if (x == 0) {
/* 3364 */         sstring = "sound.combat.parry2";
/* 3365 */       } else if (x == 1) {
/* 3366 */         sstring = "sound.combat.parry3";
/* 3367 */       }  SoundPlayer.playSound(sstring, target, 1.6F);
/* 3368 */       performer.playAnimation("practice_cut", false, target.getWurmId());
/* 3369 */       if (done)
/*      */       {
/* 3371 */         broadCastString = target.getNameWithGenus() + " is no more.";
/* 3372 */         Server.getInstance().broadCastMessage(broadCastString, tilex, tiley, performer.isOnSurface(), 3);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 3377 */       String sstring = "sound.combat.miss.light";
/* 3378 */       if (attCheck < -80.0D) {
/* 3379 */         sstring = "sound.combat.miss.heavy";
/* 3380 */       } else if (attCheck < -40.0D) {
/* 3381 */         sstring = "sound.combat.miss.med";
/* 3382 */       }  SoundPlayer.playSound(sstring, target, 1.6F);
/* 3383 */       if (performer.spamMode()) {
/*      */         
/* 3385 */         if (aiming) {
/* 3386 */           performer.getCommunicator().sendNormalServerMessage("You miss.");
/*      */         } else {
/* 3388 */           performer.getCommunicator().sendNormalServerMessage("You try to " + attString + " " + target
/* 3389 */               .getNameWithGenus() + " in the " + 
/* 3390 */               PracticeDollBehaviour.getWoundLocationString(pos) + " but " + 
/* 3391 */               getMissString(true, attCheck));
/*      */         } 
/* 3393 */       } else if (aiming) {
/* 3394 */         performer.getCommunicator().sendNormalServerMessage("You miss.");
/*      */       } 
/*      */     } 
/* 3397 */     return done;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\CombatEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */