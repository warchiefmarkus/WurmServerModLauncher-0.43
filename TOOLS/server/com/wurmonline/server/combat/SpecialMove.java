/*      */ package com.wurmonline.server.combat;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.creatures.CombatHandler;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.utils.CreatureLineSegment;
/*      */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ public class SpecialMove
/*      */   implements CombatConstants, MiscConstants
/*      */ {
/*   50 */   private static final Logger logger = Logger.getLogger(SpecialMove.class.getName());
/*      */   private final int speed;
/*      */   private final String name;
/*   53 */   private String actorMessage = "decapitate";
/*   54 */   private String othersMessage = "decapitates";
/*      */   
/*   56 */   private byte[] triggeredStances = new byte[] { 7 };
/*      */   
/*      */   private final int fightingSkillLevelNeeded;
/*      */   private final double difficulty;
/*   60 */   private byte weaponType = 1;
/*   61 */   private int staminaCost = 5000;
/*   62 */   private byte kingdom = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   67 */   private int battleRatingPenalty = 0;
/*   68 */   private int stunseconds = 0;
/*   69 */   private int staminaStolen = 0;
/*      */ 
/*      */   
/*      */   private boolean pukeMove = false;
/*      */   
/*   74 */   private byte[] woundLocationDmg = new byte[] { 1 };
/*   75 */   private int[] woundLocation = new int[] { 2 };
/*      */ 
/*      */   
/*   78 */   private static final Map<Integer, SpecialMove> specialMoves = new HashMap<>();
/*      */   
/*   80 */   private static final Map<Byte, Map<Integer, Set<SpecialMove>>> movesByWeapon = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   private static final SpecialMove[] emptyMoves = new SpecialMove[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*   97 */     createMoves();
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
/*      */   public SpecialMove(int id, String _name, int _speed, int fightskillNeeded, double _difficulty) {
/*  112 */     this.name = _name;
/*  113 */     this.speed = _speed;
/*  114 */     this.fightingSkillLevelNeeded = fightskillNeeded;
/*  115 */     this.difficulty = _difficulty;
/*  116 */     specialMoves.put(Integer.valueOf(id), this);
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
/*      */   public boolean mayPerform(Creature creature, Item weapon, int skill) {
/*  131 */     return ((this.weaponType == -1 || creature.getCombatHandler().getType(weapon, true) == this.weaponType) && skill >= this.fightingSkillLevelNeeded);
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
/*      */   public void doEffect(Creature creature, Item weapon, Creature defender, double eff) {
/*  150 */     doWoundEffect(creature, weapon, defender, eff);
/*  151 */     doBREffect(creature, weapon, defender, eff);
/*      */     
/*  153 */     doStaminaEffect(creature, weapon, defender, eff);
/*  154 */     doPukeEffect(creature, weapon, defender, eff);
/*  155 */     doStunEffect(creature, weapon, defender, eff);
/*  156 */     creature.getStatus().modifyStamina(-this.staminaCost);
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
/*      */   private void doBREffect(Creature creature, Item weapon, Creature defender, double eff) {
/*  174 */     if (this.battleRatingPenalty != 0) {
/*      */       
/*  176 */       defender.getCombatHandler().addBattleRatingPenalty((byte)this.battleRatingPenalty);
/*      */       
/*  178 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  179 */       segments.add(new CreatureLineSegment(defender));
/*  180 */       segments.add(new MulticolorLineSegment(" loses concentration.", (byte)0));
/*      */       
/*  182 */       creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */       
/*  184 */       segments.set(0, new CreatureLineSegment(creature));
/*  185 */       ((MulticolorLineSegment)segments.get(1)).setText(" hits you with a " + this.name + " that lowers your concentration.");
/*  186 */       defender.getCommunicator().sendColoredMessageCombat(segments);
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
/*      */   private void doWoundEffect(Creature creature, Item weapon, Creature defender, double eff) {
/*  208 */     if (this.woundLocationDmg != null)
/*      */     {
/*  210 */       if (this.woundLocation != null) {
/*      */         
/*  212 */         if (this.woundLocationDmg.length == this.woundLocation.length) {
/*      */           
/*  214 */           for (int w = 0; w < this.woundLocationDmg.length; w++)
/*      */           {
/*  216 */             if (this.woundLocationDmg[w] > 0 && !defender.isDead())
/*      */             {
/*  218 */               byte t = creature.getCombatHandler().getType(weapon, true);
/*      */ 
/*      */               
/*  221 */               CombatHandler.setAttString(getActorMessage());
/*  222 */               CombatHandler.setOthersString(getOthersMessage());
/*  223 */               creature.getCombatHandler().setDamage(defender, weapon, eff / 100.0D * this.woundLocationDmg[w] * 2500.0D, (byte)this.woundLocation[w], t);
/*      */               
/*  225 */               CombatHandler.setOthersString("");
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  232 */           logger.log(Level.WARNING, "Combat move wrong damages:" + this.name);
/*      */         } 
/*      */       } else {
/*  235 */         logger.log(Level.WARNING, "Combat move lacking damage:" + this.name);
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
/*      */   private void doStaminaEffect(Creature creature, Item weapon, Creature defender, double eff) {
/*  252 */     if (this.staminaStolen > 0) {
/*      */ 
/*      */       
/*  255 */       defender.getStatus().modifyStamina(-((int)(this.staminaStolen * Math.max(50.0D, eff) / 100.0D)));
/*      */       
/*  257 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  258 */       segments.add(new CreatureLineSegment(defender));
/*  259 */       segments.add(new MulticolorLineSegment(" is drained of stamina and gasps for air.", (byte)0));
/*      */       
/*  261 */       creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */       
/*  263 */       segments.set(0, new CreatureLineSegment(creature));
/*  264 */       ((MulticolorLineSegment)segments.get(1)).setText(" hits you with a " + this.name + " in a sensitive spot. You lose stamina!");
/*  265 */       defender.getCommunicator().sendColoredMessageCombat(segments);
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
/*      */   private void doPukeEffect(Creature creature, Item weapon, Creature defender, double eff) {
/*  287 */     if (this.pukeMove) {
/*      */ 
/*      */       
/*  290 */       defender.getStatus().modifyHunger((int)(65535.0D * eff / 100.0D), 0.01F);
/*  291 */       defender.getStatus().modifyThirst((int)(65535.0D * eff / 100.0D));
/*      */       
/*  293 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  294 */       segments.add(new CreatureLineSegment(defender));
/*  295 */       segments.add(new MulticolorLineSegment(" throws up from the impact.", (byte)0));
/*      */       
/*  297 */       creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */       
/*  299 */       segments.set(0, new CreatureLineSegment(creature));
/*  300 */       ((MulticolorLineSegment)segments.get(1)).setText(" hits you with a " + this.name + " and the impact makes you throw up.");
/*  301 */       defender.getCommunicator().sendColoredMessageCombat(segments);
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
/*      */   private void doStunEffect(Creature creature, Item weapon, Creature defender, double eff) {
/*  322 */     if (this.stunseconds > 0) {
/*      */       
/*  324 */       defender.getStatus().setStunned((byte)(int)(this.stunseconds * eff / 100.0D));
/*      */       
/*  326 */       ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  327 */       segments.add(new CreatureLineSegment(defender));
/*  328 */       segments.add(new MulticolorLineSegment(" is stunned.", (byte)0));
/*      */       
/*  330 */       creature.getCommunicator().sendColoredMessageCombat(segments);
/*      */       
/*  332 */       segments.set(0, new CreatureLineSegment(creature));
/*  333 */       ((MulticolorLineSegment)segments.get(1)).setText(" hits you with a " + this.name + " and stuns you.");
/*  334 */       defender.getCommunicator().sendColoredMessageCombat(segments);
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
/*      */   public static final void createMoves() {
/*  347 */     SpecialMove sludge = new SpecialMove(1, "Sludge", 5, 19, 25.0D);
/*  348 */     sludge.triggeredStances = new byte[] { 10 };
/*      */     
/*  350 */     sludge.woundLocation = new int[] { 25 };
/*      */     
/*  352 */     sludge.woundLocationDmg = new byte[] { 2 };
/*      */     
/*  354 */     sludge.staminaCost = 11000;
/*  355 */     sludge.stunseconds = 2;
/*  356 */     sludge.staminaStolen = 2000;
/*  357 */     sludge.weaponType = -1;
/*  358 */     sludge.kingdom = 1;
/*  359 */     sludge.setActorMessage("duck low and hit");
/*  360 */     sludge.setOthersMessage("ducks low and hits");
/*  361 */     addMovesByWeapon(sludge.weaponType, sludge);
/*      */     
/*  363 */     SpecialMove falcon = new SpecialMove(2, "Falcon", 7, 25, 30.0D);
/*  364 */     falcon.triggeredStances = new byte[] { 6, 1, 7 };
/*      */     
/*  366 */     falcon.woundLocationDmg = new byte[] { 3 };
/*      */     
/*  368 */     falcon.staminaCost = 13000;
/*  369 */     falcon.staminaStolen = 8000;
/*  370 */     falcon.weaponType = -1;
/*  371 */     falcon.kingdom = 1;
/*  372 */     falcon.setActorMessage("knock");
/*  373 */     falcon.setOthersMessage("knocks");
/*  374 */     addMovesByWeapon(falcon.weaponType, falcon);
/*      */ 
/*      */     
/*  377 */     SpecialMove narr = new SpecialMove(3, "Narrower", 8, 30, 35.0D);
/*  378 */     narr.triggeredStances = new byte[] { 5 };
/*      */     
/*  380 */     narr.woundLocation = new int[] { 23 };
/*      */     
/*  382 */     narr.woundLocationDmg = new byte[] { 6 };
/*      */     
/*  384 */     narr.staminaCost = 12000;
/*  385 */     narr.stunseconds = 3;
/*  386 */     narr.weaponType = 2;
/*  387 */     narr.kingdom = 1;
/*  388 */     narr.setActorMessage("strongly pierce");
/*  389 */     narr.setOthersMessage("strongly pierces");
/*  390 */     addMovesByWeapon(narr.weaponType, narr);
/*      */     
/*  392 */     SpecialMove cray = new SpecialMove(4, "Crayfish", 7, 40, 45.0D);
/*  393 */     cray.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/*  395 */     cray.woundLocation = new int[] { 34 };
/*      */     
/*  397 */     cray.woundLocationDmg = new byte[] { 10 };
/*      */     
/*  399 */     cray.staminaCost = 14000;
/*  400 */     cray.stunseconds = 2;
/*  401 */     cray.weaponType = 2;
/*  402 */     cray.kingdom = 1;
/*  403 */     cray.setActorMessage("stab");
/*  404 */     cray.setOthersMessage("stabs");
/*  405 */     addMovesByWeapon(cray.weaponType, cray);
/*      */     
/*  407 */     SpecialMove pearl = new SpecialMove(5, "Mommys pearl", 4, 50, 55.0D);
/*  408 */     pearl.triggeredStances = new byte[] { 7 };
/*      */     
/*  410 */     pearl.woundLocation = new int[] { 1 };
/*      */     
/*  412 */     pearl.woundLocationDmg = new byte[] { 11 };
/*      */     
/*  414 */     pearl.staminaCost = 17000;
/*  415 */     pearl.weaponType = 2;
/*  416 */     pearl.stunseconds = 3;
/*  417 */     pearl.kingdom = 1;
/*  418 */     pearl.setActorMessage("stab");
/*  419 */     pearl.setOthersMessage("stabs");
/*  420 */     addMovesByWeapon(pearl.weaponType, pearl);
/*      */     
/*  422 */     SpecialMove motley = new SpecialMove(6, "Motley visions", 8, 60, 50.0D);
/*  423 */     motley.triggeredStances = new byte[] { 10 };
/*      */     
/*  425 */     motley.woundLocation = new int[] { 25 };
/*      */     
/*  427 */     motley.woundLocationDmg = new byte[] { 10 };
/*      */     
/*  429 */     motley.staminaCost = 16000;
/*  430 */     motley.weaponType = 2;
/*  431 */     motley.stunseconds = 3;
/*  432 */     motley.kingdom = 1;
/*  433 */     motley.setActorMessage("punch holes in");
/*  434 */     motley.setOthersMessage("punches holes in");
/*  435 */     addMovesByWeapon(motley.weaponType, motley);
/*      */ 
/*      */     
/*  438 */     SpecialMove nick = new SpecialMove(7, "Carver", 5, 30, 35.0D);
/*  439 */     nick.triggeredStances = new byte[] { 5 };
/*      */     
/*  441 */     nick.woundLocation = new int[] { 23, 24 };
/*      */     
/*  443 */     nick.woundLocationDmg = new byte[] { 3, 3 };
/*      */     
/*  445 */     nick.battleRatingPenalty = 1;
/*      */     
/*  447 */     nick.staminaCost = 15000;
/*  448 */     nick.weaponType = 1;
/*  449 */     nick.kingdom = 1;
/*  450 */     nick.setActorMessage("engrave");
/*  451 */     nick.setOthersMessage("engraves");
/*  452 */     addMovesByWeapon(nick.weaponType, nick);
/*      */     
/*  454 */     SpecialMove props = new SpecialMove(8, "False props", 7, 40, 35.0D);
/*  455 */     props.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/*  457 */     props.woundLocation = new int[] { 34, 15, 16 };
/*      */     
/*  459 */     props.woundLocationDmg = new byte[] { 4, 2, 2 };
/*      */     
/*  461 */     props.staminaCost = 15000;
/*  462 */     props.battleRatingPenalty = 1;
/*  463 */     props.weaponType = 1;
/*  464 */     props.kingdom = 1;
/*  465 */     props.setActorMessage("fool and cut");
/*  466 */     props.setOthersMessage("fools and cuts");
/*  467 */     addMovesByWeapon(props.weaponType, props);
/*      */     
/*  469 */     SpecialMove flurry = new SpecialMove(9, "Flurry of pain", 4, 50, 55.0D);
/*  470 */     flurry.triggeredStances = new byte[] { 2, 5 };
/*      */     
/*  472 */     flurry.woundLocation = new int[] { 3, 21, 4 };
/*      */     
/*  474 */     flurry.woundLocationDmg = new byte[] { 3, 4, 3 };
/*      */     
/*  476 */     flurry.staminaCost = 17000;
/*  477 */     flurry.battleRatingPenalty = 2;
/*  478 */     flurry.weaponType = 1;
/*  479 */     flurry.kingdom = 1;
/*  480 */     flurry.setActorMessage("assault");
/*  481 */     flurry.setOthersMessage("assaults");
/*  482 */     addMovesByWeapon(flurry.weaponType, flurry);
/*      */     
/*  484 */     SpecialMove twilfit = new SpecialMove(10, "Twilfit twin", 8, 60, 50.0D);
/*  485 */     twilfit.triggeredStances = new byte[] { 6, 1, 7 };
/*      */     
/*  487 */     twilfit.woundLocation = new int[] { 1, 22 };
/*      */     
/*  489 */     twilfit.woundLocationDmg = new byte[] { 4, 5 };
/*      */     
/*  491 */     twilfit.staminaCost = 15000;
/*  492 */     twilfit.weaponType = 1;
/*  493 */     twilfit.battleRatingPenalty = 3;
/*  494 */     twilfit.kingdom = 1;
/*  495 */     twilfit.setActorMessage("doubly slash");
/*  496 */     twilfit.setOthersMessage("doubly slashes");
/*  497 */     addMovesByWeapon(twilfit.weaponType, twilfit);
/*      */ 
/*      */     
/*  500 */     SpecialMove toe = new SpecialMove(11, "Union of the snake", 5, 30, 35.0D);
/*  501 */     toe.triggeredStances = new byte[] { 4, 3, 10 };
/*      */     
/*  503 */     toe.woundLocation = new int[] { 16 };
/*      */     
/*  505 */     toe.woundLocationDmg = new byte[] { 5 };
/*      */     
/*  507 */     toe.staminaCost = 11000;
/*  508 */     toe.staminaStolen = 5000;
/*  509 */     toe.weaponType = 0;
/*  510 */     toe.kingdom = 1;
/*  511 */     toe.setActorMessage("pound");
/*  512 */     toe.setOthersMessage("pounds");
/*  513 */     addMovesByWeapon(toe.weaponType, toe);
/*      */     
/*  515 */     SpecialMove marmalade = new SpecialMove(12, "Sour marmalade", 6, 40, 30.0D);
/*  516 */     marmalade.triggeredStances = new byte[] { 2 };
/*      */     
/*  518 */     marmalade.woundLocationDmg = new byte[] { 2 };
/*      */     
/*  520 */     marmalade.staminaCost = 14000;
/*  521 */     marmalade.staminaStolen = 8000;
/*  522 */     marmalade.weaponType = 0;
/*  523 */     marmalade.kingdom = 1;
/*  524 */     marmalade.pukeMove = true;
/*  525 */     marmalade.setActorMessage("squish");
/*  526 */     marmalade.setOthersMessage("squishes");
/*  527 */     addMovesByWeapon(marmalade.weaponType, marmalade);
/*      */     
/*  529 */     SpecialMove minikill = new SpecialMove(13, "Minikill", 7, 50, 55.0D);
/*  530 */     minikill.triggeredStances = new byte[] { 6, 1 };
/*      */     
/*  532 */     minikill.woundLocation = new int[] { 23 };
/*      */     
/*  534 */     minikill.woundLocationDmg = new byte[] { 6 };
/*      */     
/*  536 */     minikill.staminaCost = 16000;
/*  537 */     minikill.weaponType = 0;
/*  538 */     minikill.staminaStolen = 15000;
/*  539 */     minikill.kingdom = 1;
/*  540 */     minikill.setActorMessage("bonk");
/*  541 */     minikill.setOthersMessage("bonks");
/*  542 */     addMovesByWeapon(minikill.weaponType, minikill);
/*      */     
/*  544 */     SpecialMove golem = new SpecialMove(14, "Golem roar", 9, 60, 65.0D);
/*  545 */     golem.triggeredStances = new byte[] { 0 };
/*      */     
/*  547 */     golem.woundLocationDmg = new byte[] { 4 };
/*      */     
/*  549 */     golem.staminaCost = 17000;
/*  550 */     golem.weaponType = 0;
/*  551 */     golem.pukeMove = true;
/*  552 */     golem.staminaStolen = 25000;
/*  553 */     golem.kingdom = 1;
/*  554 */     golem.setActorMessage("devastate");
/*  555 */     golem.setOthersMessage("devastates");
/*  556 */     addMovesByWeapon(golem.weaponType, golem);
/*      */ 
/*      */     
/*  559 */     SpecialMove tail = new SpecialMove(15, "Dragontail", 7, 19, 25.0D);
/*  560 */     tail.triggeredStances = new byte[] { 10 };
/*      */     
/*  562 */     tail.woundLocationDmg = new byte[] { 2 };
/*      */     
/*  564 */     tail.staminaCost = 12000;
/*  565 */     tail.stunseconds = 2;
/*  566 */     tail.staminaStolen = 3000;
/*  567 */     tail.weaponType = -1;
/*  568 */     tail.kingdom = 3;
/*  569 */     tail.setActorMessage("whip");
/*  570 */     tail.setOthersMessage("whips");
/*  571 */     addMovesByWeapon(tail.weaponType, tail);
/*      */     
/*  573 */     SpecialMove wanderer = new SpecialMove(16, "Wanderer", 8, 25, 30.0D);
/*  574 */     wanderer.triggeredStances = new byte[] { 3, 4 };
/*      */     
/*  576 */     wanderer.woundLocationDmg = new byte[] { 2 };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  581 */     wanderer.staminaCost = 14000;
/*  582 */     wanderer.staminaStolen = 10000;
/*  583 */     wanderer.weaponType = -1;
/*  584 */     wanderer.kingdom = 3;
/*  585 */     wanderer.setActorMessage("stomp");
/*  586 */     wanderer.setOthersMessage("stomps");
/*  587 */     addMovesByWeapon(wanderer.weaponType, wanderer);
/*      */ 
/*      */     
/*  590 */     SpecialMove horses = new SpecialMove(17, "Horses ass", 7, 30, 35.0D);
/*  591 */     horses.triggeredStances = new byte[] { 5, 2 };
/*      */     
/*  593 */     horses.woundLocation = new int[] { 24 };
/*      */     
/*  595 */     horses.woundLocationDmg = new byte[] { 6 };
/*      */     
/*  597 */     horses.stunseconds = 3;
/*  598 */     horses.staminaCost = 11000;
/*  599 */     horses.weaponType = 2;
/*  600 */     horses.kingdom = 3;
/*  601 */     horses.setActorMessage("restructure");
/*  602 */     horses.setOthersMessage("restructures");
/*  603 */     addMovesByWeapon(horses.weaponType, horses);
/*      */     
/*  605 */     SpecialMove scorpio = new SpecialMove(18, "Slow scorpio", 9, 40, 45.0D);
/*  606 */     scorpio.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/*  608 */     scorpio.woundLocation = new int[] { 34 };
/*      */     
/*  610 */     scorpio.woundLocationDmg = new byte[] { 10 };
/*      */     
/*  612 */     scorpio.stunseconds = 3;
/*  613 */     scorpio.staminaCost = 12000;
/*  614 */     scorpio.weaponType = 2;
/*  615 */     scorpio.kingdom = 3;
/*  616 */     scorpio.setActorMessage("sting");
/*  617 */     scorpio.setOthersMessage("stings");
/*  618 */     addMovesByWeapon(scorpio.weaponType, scorpio);
/*      */     
/*  620 */     SpecialMove moist = new SpecialMove(19, "Moist hind", 6, 50, 55.0D);
/*  621 */     moist.triggeredStances = new byte[] { 7 };
/*      */     
/*  623 */     moist.woundLocation = new int[] { 1 };
/*      */     
/*  625 */     moist.woundLocationDmg = new byte[] { 9 };
/*      */     
/*  627 */     moist.staminaCost = 15000;
/*  628 */     moist.weaponType = 2;
/*  629 */     moist.stunseconds = 4;
/*  630 */     moist.kingdom = 3;
/*  631 */     moist.setActorMessage("stab");
/*  632 */     moist.setOthersMessage("stabs");
/*  633 */     addMovesByWeapon(moist.weaponType, moist);
/*      */     
/*  635 */     SpecialMove mask = new SpecialMove(20, "Mask of defiance", 8, 60, 65.0D);
/*  636 */     mask.triggeredStances = new byte[] { 7 };
/*      */     
/*  638 */     mask.woundLocation = new int[] { 1 };
/*      */     
/*  640 */     mask.woundLocationDmg = new byte[] { 14 };
/*      */     
/*  642 */     mask.staminaCost = 18000;
/*  643 */     mask.weaponType = 2;
/*  644 */     mask.stunseconds = 3;
/*  645 */     mask.kingdom = 3;
/*  646 */     mask.setActorMessage("punch holes in");
/*  647 */     mask.setOthersMessage("punches holes in");
/*  648 */     addMovesByWeapon(mask.weaponType, mask);
/*      */ 
/*      */     
/*  651 */     SpecialMove red = new SpecialMove(21, "Red wind", 7, 30, 35.0D);
/*  652 */     red.triggeredStances = new byte[] { 2 };
/*      */     
/*  654 */     red.woundLocation = new int[] { 23 };
/*      */     
/*  656 */     red.woundLocationDmg = new byte[] { 6 };
/*      */     
/*  658 */     red.staminaCost = 15000;
/*  659 */     red.battleRatingPenalty = 2;
/*  660 */     red.weaponType = 1;
/*  661 */     red.kingdom = 3;
/*  662 */     red.setActorMessage("engrave");
/*  663 */     red.setOthersMessage("engraves");
/*  664 */     addMovesByWeapon(red.weaponType, red);
/*      */     
/*  666 */     SpecialMove slow = new SpecialMove(22, "Dark grace", 9, 40, 45.0D);
/*  667 */     slow.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/*  669 */     slow.woundLocation = new int[] { 34, 15, 16 };
/*      */     
/*  671 */     slow.woundLocationDmg = new byte[] { 5, 3, 3 };
/*      */     
/*  673 */     slow.staminaCost = 14000;
/*  674 */     slow.battleRatingPenalty = 1;
/*  675 */     slow.weaponType = 1;
/*  676 */     slow.kingdom = 3;
/*  677 */     slow.setActorMessage("grace");
/*  678 */     slow.setOthersMessage("graces");
/*  679 */     addMovesByWeapon(slow.weaponType, slow);
/*      */     
/*  681 */     SpecialMove hurting = new SpecialMove(23, "Hurting scion", 4, 50, 55.0D);
/*  682 */     hurting.triggeredStances = new byte[] { 5, 2 };
/*      */     
/*  684 */     hurting.woundLocation = new int[] { 21, 4 };
/*      */     
/*  686 */     hurting.woundLocationDmg = new byte[] { 6, 4 };
/*      */     
/*  688 */     hurting.staminaCost = 17000;
/*  689 */     hurting.weaponType = 1;
/*  690 */     hurting.battleRatingPenalty = 2;
/*  691 */     hurting.kingdom = 3;
/*  692 */     hurting.setActorMessage("cut open");
/*  693 */     hurting.setOthersMessage("cuts open");
/*  694 */     addMovesByWeapon(hurting.weaponType, hurting);
/*      */     
/*  696 */     SpecialMove trueb = new SpecialMove(24, "True blood", 8, 55, 60.0D);
/*  697 */     trueb.triggeredStances = new byte[] { 1, 6 };
/*      */     
/*  699 */     trueb.woundLocation = new int[] { 1, 21 };
/*      */     
/*  701 */     trueb.woundLocationDmg = new byte[] { 4, 8 };
/*      */     
/*  703 */     trueb.staminaCost = 15000;
/*  704 */     trueb.weaponType = 1;
/*  705 */     trueb.battleRatingPenalty = 2;
/*  706 */     trueb.kingdom = 3;
/*  707 */     trueb.setActorMessage("whip");
/*  708 */     trueb.setOthersMessage("whips");
/*  709 */     addMovesByWeapon(trueb.weaponType, trueb);
/*      */ 
/*      */     
/*  712 */     SpecialMove kissg = new SpecialMove(25, "Kiss goodnight", 4, 25, 30.0D);
/*  713 */     kissg.triggeredStances = new byte[] { 6, 1, 7 };
/*      */     
/*  715 */     kissg.woundLocationDmg = new byte[] { 3 };
/*      */     
/*  717 */     kissg.pukeMove = true;
/*  718 */     kissg.staminaCost = 15000;
/*  719 */     kissg.staminaStolen = 5000;
/*  720 */     kissg.weaponType = 0;
/*  721 */     kissg.kingdom = 3;
/*  722 */     kissg.setActorMessage("pound");
/*  723 */     kissg.setOthersMessage("pounds");
/*  724 */     addMovesByWeapon(kissg.weaponType, kissg);
/*      */     
/*  726 */     SpecialMove squarep = new SpecialMove(26, "Squarepusher", 7, 40, 45.0D);
/*  727 */     squarep.triggeredStances = new byte[] { 2 };
/*      */     
/*  729 */     squarep.woundLocation = new int[] { 14 };
/*      */     
/*  731 */     squarep.woundLocationDmg = new byte[] { 6 };
/*      */     
/*  733 */     squarep.staminaStolen = 10000;
/*  734 */     squarep.staminaCost = 12000;
/*  735 */     squarep.weaponType = 0;
/*  736 */     squarep.kingdom = 3;
/*  737 */     squarep.setActorMessage("maul");
/*  738 */     squarep.setOthersMessage("mauls");
/*  739 */     addMovesByWeapon(squarep.weaponType, squarep);
/*      */     
/*  741 */     SpecialMove doubleimp = new SpecialMove(27, "Double impact", 4, 50, 55.0D);
/*  742 */     doubleimp.triggeredStances = new byte[] { 6, 1 };
/*      */     
/*  744 */     doubleimp.woundLocation = new int[] { 1 };
/*      */     
/*  746 */     doubleimp.woundLocationDmg = new byte[] { 5 };
/*      */     
/*  748 */     doubleimp.staminaCost = 16000;
/*  749 */     doubleimp.staminaStolen = 14000;
/*  750 */     doubleimp.weaponType = 0;
/*  751 */     doubleimp.pukeMove = true;
/*  752 */     doubleimp.kingdom = 3;
/*  753 */     doubleimp.setActorMessage("gong-gong");
/*  754 */     doubleimp.setOthersMessage("gong-gongs");
/*  755 */     addMovesByWeapon(doubleimp.weaponType, doubleimp);
/*      */     
/*  757 */     SpecialMove dissolver = new SpecialMove(28, "Dissolver", 9, 60, 60.0D);
/*  758 */     dissolver.triggeredStances = new byte[] { 0 };
/*      */     
/*  760 */     dissolver.woundLocationDmg = new byte[] { 3 };
/*      */     
/*  762 */     dissolver.staminaCost = 18000;
/*  763 */     dissolver.weaponType = 0;
/*      */     
/*  765 */     dissolver.staminaStolen = 25000;
/*  766 */     dissolver.kingdom = 3;
/*  767 */     dissolver.setActorMessage("dissolve");
/*  768 */     dissolver.setOthersMessage("dissolves");
/*  769 */     addMovesByWeapon(dissolver.weaponType, dissolver);
/*      */ 
/*      */     
/*  772 */     SpecialMove cricket = new SpecialMove(29, "Cricket", 5, 19, 25.0D);
/*  773 */     cricket.triggeredStances = new byte[] { 6, 1, 7 };
/*      */     
/*  775 */     cricket.woundLocationDmg = new byte[] { 2 };
/*      */     
/*  777 */     cricket.staminaCost = 9000;
/*  778 */     cricket.stunseconds = 2;
/*  779 */     cricket.staminaStolen = 2000;
/*  780 */     cricket.weaponType = -1;
/*  781 */     cricket.kingdom = 2;
/*  782 */     cricket.setActorMessage("string");
/*  783 */     cricket.setOthersMessage("strings");
/*  784 */     addMovesByWeapon(cricket.weaponType, cricket);
/*      */     
/*  786 */     SpecialMove faithpush = new SpecialMove(30, "Faithpush", 5, 25, 30.0D);
/*  787 */     faithpush.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/*  789 */     faithpush.woundLocationDmg = new byte[] { 3 };
/*      */     
/*  791 */     faithpush.staminaCost = 12000;
/*  792 */     faithpush.staminaStolen = 6000;
/*  793 */     faithpush.weaponType = -1;
/*  794 */     faithpush.kingdom = 2;
/*  795 */     faithpush.setActorMessage("stomp");
/*  796 */     faithpush.setOthersMessage("stomps");
/*  797 */     addMovesByWeapon(faithpush.weaponType, faithpush);
/*      */ 
/*      */     
/*  800 */     SpecialMove delusion = new SpecialMove(31, "Rampant delusion", 4, 30, 35.0D);
/*  801 */     delusion.triggeredStances = new byte[] { 5, 2 };
/*      */     
/*  803 */     delusion.woundLocation = new int[] { 21 };
/*      */     
/*  805 */     delusion.woundLocationDmg = new byte[] { 4 };
/*      */     
/*  807 */     delusion.staminaCost = 10000;
/*  808 */     delusion.stunseconds = 3;
/*  809 */     delusion.weaponType = 2;
/*  810 */     delusion.kingdom = 2;
/*  811 */     delusion.setActorMessage("puncture");
/*  812 */     delusion.setOthersMessage("punctures");
/*  813 */     addMovesByWeapon(delusion.weaponType, delusion);
/*      */     
/*  815 */     SpecialMove swamp = new SpecialMove(32, "Burning swamp", 7, 40, 45.0D);
/*  816 */     swamp.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/*  818 */     swamp.woundLocation = new int[] { 34 };
/*      */     
/*  820 */     swamp.woundLocationDmg = new byte[] { 10 };
/*      */     
/*  822 */     swamp.staminaCost = 12000;
/*  823 */     swamp.stunseconds = 2;
/*  824 */     swamp.weaponType = 2;
/*  825 */     swamp.kingdom = 2;
/*  826 */     swamp.setActorMessage("stab");
/*  827 */     swamp.setOthersMessage("stabs");
/*  828 */     addMovesByWeapon(swamp.weaponType, swamp);
/*      */     
/*  830 */     SpecialMove sensgard = new SpecialMove(33, "Sensitive warden", 4, 45, 50.0D);
/*  831 */     sensgard.triggeredStances = new byte[] { 7 };
/*      */     
/*  833 */     sensgard.woundLocation = new int[] { 1 };
/*      */     
/*  835 */     sensgard.woundLocationDmg = new byte[] { 10 };
/*      */     
/*  837 */     sensgard.staminaCost = 15000;
/*  838 */     sensgard.weaponType = 2;
/*  839 */     sensgard.stunseconds = 2;
/*  840 */     sensgard.kingdom = 2;
/*  841 */     sensgard.setActorMessage("penetrate");
/*  842 */     sensgard.setOthersMessage("penetrates");
/*  843 */     addMovesByWeapon(sensgard.weaponType, sensgard);
/*      */     
/*  845 */     SpecialMove prod = new SpecialMove(34, "Prod", 3, 60, 55.0D);
/*  846 */     prod.triggeredStances = new byte[] { 10 };
/*      */     
/*  848 */     prod.woundLocation = new int[] { 25 };
/*      */     
/*  850 */     prod.woundLocationDmg = new byte[] { 10 };
/*      */     
/*  852 */     prod.staminaCost = 15000;
/*  853 */     prod.weaponType = 2;
/*  854 */     prod.stunseconds = 3;
/*  855 */     prod.kingdom = 2;
/*  856 */     prod.setActorMessage("punch holes in");
/*  857 */     prod.setOthersMessage("punches holes in");
/*  858 */     addMovesByWeapon(prod.weaponType, prod);
/*      */ 
/*      */     
/*  861 */     SpecialMove boneb = new SpecialMove(35, "Bonebringer", 5, 30, 35.0D);
/*  862 */     boneb.triggeredStances = new byte[] { 5, 6 };
/*      */     
/*  864 */     boneb.woundLocation = new int[] { 23 };
/*      */     
/*  866 */     boneb.woundLocationDmg = new byte[] { 6 };
/*      */     
/*  868 */     boneb.staminaCost = 12000;
/*  869 */     boneb.battleRatingPenalty = 1;
/*  870 */     boneb.weaponType = 1;
/*  871 */     boneb.kingdom = 2;
/*  872 */     boneb.setActorMessage("dissect");
/*  873 */     boneb.setOthersMessage("dissects");
/*  874 */     addMovesByWeapon(boneb.weaponType, boneb);
/*      */     
/*  876 */     SpecialMove winged = new SpecialMove(36, "Winged fang", 6, 35, 40.0D);
/*  877 */     winged.triggeredStances = new byte[] { 7, 6, 1 };
/*      */     
/*  879 */     winged.woundLocation = new int[] { 27, 26, 21 };
/*      */     
/*  881 */     winged.woundLocationDmg = new byte[] { 2, 2, 4 };
/*      */     
/*  883 */     winged.staminaCost = 15000;
/*  884 */     winged.battleRatingPenalty = 1;
/*  885 */     winged.weaponType = 1;
/*  886 */     winged.kingdom = 2;
/*  887 */     winged.setActorMessage("paint");
/*  888 */     winged.setOthersMessage("paints");
/*  889 */     addMovesByWeapon(winged.weaponType, winged);
/*      */     
/*  891 */     SpecialMove fast = new SpecialMove(37, "Firefangs", 4, 45, 55.0D);
/*  892 */     fast.triggeredStances = new byte[] { 6, 7 };
/*      */     
/*  894 */     fast.woundLocation = new int[] { 21, 5 };
/*      */     
/*  896 */     fast.woundLocationDmg = new byte[] { 6, 6 };
/*      */     
/*  898 */     fast.staminaCost = 16000;
/*  899 */     fast.weaponType = 1;
/*  900 */     fast.battleRatingPenalty = 1;
/*  901 */     fast.kingdom = 2;
/*  902 */     fast.setActorMessage("cut");
/*  903 */     fast.setOthersMessage("cuts");
/*  904 */     addMovesByWeapon(fast.weaponType, fast);
/*      */     
/*  906 */     SpecialMove wildgard = new SpecialMove(38, "Wild garden", 5, 50, 55.0D);
/*  907 */     wildgard.triggeredStances = new byte[] { 2, 5 };
/*      */     
/*  909 */     wildgard.woundLocation = new int[] { 17, 29 };
/*      */     
/*  911 */     wildgard.woundLocationDmg = new byte[] { 7, 6 };
/*      */     
/*  913 */     wildgard.staminaCost = 14000;
/*  914 */     wildgard.weaponType = 1;
/*  915 */     wildgard.battleRatingPenalty = 1;
/*  916 */     wildgard.kingdom = 2;
/*  917 */     wildgard.setActorMessage("redecorate");
/*  918 */     wildgard.setOthersMessage("redecorates");
/*  919 */     addMovesByWeapon(wildgard.weaponType, wildgard);
/*      */ 
/*      */     
/*  922 */     SpecialMove bonker = new SpecialMove(39, "Bonker", 5, 25, 30.0D);
/*  923 */     bonker.triggeredStances = new byte[] { 4, 3, 10 };
/*      */     
/*  925 */     bonker.woundLocationDmg = new byte[] { 3 };
/*      */     
/*  927 */     bonker.staminaCost = 15000;
/*  928 */     bonker.staminaStolen = 5000;
/*  929 */     bonker.pukeMove = true;
/*  930 */     bonker.weaponType = 0;
/*  931 */     bonker.kingdom = 2;
/*  932 */     bonker.setActorMessage("pound");
/*  933 */     bonker.setOthersMessage("pounds");
/*  934 */     addMovesByWeapon(bonker.weaponType, bonker);
/*      */     
/*  936 */     SpecialMove rotten = new SpecialMove(40, "Rotten stomach", 4, 30, 35.0D);
/*  937 */     rotten.triggeredStances = new byte[] { 1, 2 };
/*      */     
/*  939 */     rotten.woundLocation = new int[] { 23 };
/*      */     
/*  941 */     rotten.woundLocationDmg = new byte[] { 4 };
/*      */     
/*  943 */     rotten.staminaCost = 9000;
/*  944 */     rotten.staminaStolen = 5000;
/*  945 */     rotten.weaponType = 0;
/*  946 */     rotten.kingdom = 2;
/*  947 */     rotten.setActorMessage("jam");
/*  948 */     rotten.setOthersMessage("jams");
/*  949 */     addMovesByWeapon(rotten.weaponType, rotten);
/*      */     
/*  951 */     SpecialMove thing = new SpecialMove(41, "Pain thing", 9, 40, 45.0D);
/*  952 */     thing.triggeredStances = new byte[] { 5, 4 };
/*      */     
/*  954 */     thing.woundLocationDmg = new byte[] { 5 };
/*      */     
/*  956 */     thing.staminaCost = 16000;
/*  957 */     thing.weaponType = 0;
/*  958 */     thing.pukeMove = true;
/*  959 */     thing.staminaStolen = 15000;
/*  960 */     thing.kingdom = 2;
/*  961 */     thing.setActorMessage("hurt");
/*  962 */     thing.setOthersMessage("hurts");
/*  963 */     addMovesByWeapon(thing.weaponType, thing);
/*      */     
/*  965 */     SpecialMove tripleimp = new SpecialMove(42, "Triple impact", 6, 50, 55.0D);
/*  966 */     tripleimp.triggeredStances = new byte[] { 6, 1 };
/*      */     
/*  968 */     tripleimp.woundLocation = new int[] { 1 };
/*      */     
/*  970 */     tripleimp.woundLocationDmg = new byte[] { 2 };
/*      */     
/*  972 */     tripleimp.staminaCost = 15000;
/*  973 */     tripleimp.staminaStolen = 20000;
/*  974 */     tripleimp.weaponType = 0;
/*  975 */     tripleimp.kingdom = 2;
/*  976 */     tripleimp.setActorMessage("bowl");
/*  977 */     tripleimp.setOthersMessage("bowls");
/*  978 */     addMovesByWeapon(tripleimp.weaponType, tripleimp);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1003 */     SpecialMove lrider = new SpecialMove(1, "Low rider", 5, 19, 25.0D);
/* 1004 */     lrider.triggeredStances = new byte[] { 10 };
/*      */     
/* 1006 */     lrider.woundLocation = new int[] { 25 };
/*      */     
/* 1008 */     lrider.woundLocationDmg = new byte[] { 2 };
/*      */     
/* 1010 */     lrider.staminaCost = 11000;
/* 1011 */     lrider.stunseconds = 2;
/* 1012 */     lrider.staminaStolen = 2000;
/* 1013 */     lrider.weaponType = -1;
/* 1014 */     lrider.kingdom = 4;
/* 1015 */     lrider.setActorMessage("duck low and hit");
/* 1016 */     lrider.setOthersMessage("ducks low and hits");
/* 1017 */     addMovesByWeapon(lrider.weaponType, lrider);
/*      */     
/* 1019 */     SpecialMove clouds = new SpecialMove(2, "Cold clouds", 7, 25, 30.0D);
/* 1020 */     clouds.triggeredStances = new byte[] { 6, 1, 7 };
/*      */     
/* 1022 */     clouds.woundLocationDmg = new byte[] { 3 };
/*      */     
/* 1024 */     clouds.staminaCost = 13000;
/* 1025 */     clouds.staminaStolen = 8000;
/* 1026 */     clouds.weaponType = -1;
/* 1027 */     clouds.kingdom = 4;
/* 1028 */     clouds.setActorMessage("knock");
/* 1029 */     clouds.setOthersMessage("knocks");
/* 1030 */     addMovesByWeapon(clouds.weaponType, clouds);
/*      */ 
/*      */     
/* 1033 */     SpecialMove backbr = new SpecialMove(7, "Back breaker", 5, 30, 35.0D);
/* 1034 */     backbr.triggeredStances = new byte[] { 5 };
/*      */     
/* 1036 */     backbr.woundLocation = new int[] { 23, 24 };
/*      */     
/* 1038 */     backbr.woundLocationDmg = new byte[] { 3, 3 };
/*      */     
/* 1040 */     backbr.battleRatingPenalty = 1;
/* 1041 */     backbr.staminaCost = 15000;
/* 1042 */     backbr.weaponType = 1;
/* 1043 */     backbr.kingdom = 4;
/* 1044 */     backbr.setActorMessage("engrave");
/* 1045 */     backbr.setOthersMessage("engraves");
/* 1046 */     addMovesByWeapon(backbr.weaponType, backbr);
/*      */     
/* 1048 */     SpecialMove bloodsc = new SpecialMove(8, "Bloodscion", 7, 40, 35.0D);
/* 1049 */     bloodsc.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/* 1051 */     bloodsc.woundLocation = new int[] { 34, 15, 16 };
/*      */     
/* 1053 */     bloodsc.woundLocationDmg = new byte[] { 4, 2, 2 };
/*      */     
/* 1055 */     bloodsc.staminaCost = 15000;
/* 1056 */     bloodsc.battleRatingPenalty = 1;
/* 1057 */     bloodsc.weaponType = 1;
/* 1058 */     bloodsc.kingdom = 4;
/* 1059 */     bloodsc.setActorMessage("lunge and cut");
/* 1060 */     bloodsc.setOthersMessage("lunges and cuts");
/* 1061 */     addMovesByWeapon(bloodsc.weaponType, bloodsc);
/*      */     
/* 1063 */     SpecialMove raktak = new SpecialMove(9, "Raktaktak", 4, 50, 55.0D);
/* 1064 */     raktak.triggeredStances = new byte[] { 2, 5 };
/*      */     
/* 1066 */     raktak.woundLocation = new int[] { 3, 21, 4 };
/*      */     
/* 1068 */     raktak.woundLocationDmg = new byte[] { 3, 4, 3 };
/*      */     
/* 1070 */     raktak.staminaCost = 17000;
/* 1071 */     raktak.battleRatingPenalty = 2;
/* 1072 */     raktak.weaponType = 1;
/* 1073 */     raktak.kingdom = 4;
/* 1074 */     raktak.setActorMessage("assault");
/* 1075 */     raktak.setOthersMessage("assaults");
/* 1076 */     addMovesByWeapon(raktak.weaponType, raktak);
/*      */     
/* 1078 */     SpecialMove tattoo = new SpecialMove(10, "Tattoo twice", 8, 60, 50.0D);
/* 1079 */     tattoo.triggeredStances = new byte[] { 6, 1, 7 };
/*      */     
/* 1081 */     tattoo.woundLocation = new int[] { 1, 22 };
/*      */     
/* 1083 */     tattoo.woundLocationDmg = new byte[] { 4, 5 };
/*      */     
/* 1085 */     tattoo.staminaCost = 15000;
/* 1086 */     tattoo.weaponType = 1;
/* 1087 */     tattoo.battleRatingPenalty = 3;
/* 1088 */     tattoo.kingdom = 4;
/* 1089 */     tattoo.setActorMessage("doubly grind");
/* 1090 */     tattoo.setOthersMessage("doubly grinds");
/* 1091 */     addMovesByWeapon(tattoo.weaponType, tattoo);
/*      */ 
/*      */     
/* 1094 */     SpecialMove sleepwalk = new SpecialMove(25, "Sleepwalker", 4, 25, 30.0D);
/* 1095 */     sleepwalk.triggeredStances = new byte[] { 6, 1, 7 };
/*      */     
/* 1097 */     sleepwalk.woundLocationDmg = new byte[] { 3 };
/*      */     
/* 1099 */     sleepwalk.pukeMove = true;
/* 1100 */     sleepwalk.staminaCost = 15000;
/* 1101 */     sleepwalk.staminaStolen = 5000;
/* 1102 */     sleepwalk.weaponType = 0;
/* 1103 */     sleepwalk.kingdom = 4;
/* 1104 */     sleepwalk.setActorMessage("pound");
/* 1105 */     sleepwalk.setOthersMessage("pounds");
/* 1106 */     addMovesByWeapon(sleepwalk.weaponType, sleepwalk);
/*      */     
/* 1108 */     SpecialMove hammerha = new SpecialMove(26, "Hammerhand", 7, 40, 45.0D);
/* 1109 */     hammerha.triggeredStances = new byte[] { 2 };
/*      */     
/* 1111 */     hammerha.woundLocation = new int[] { 14 };
/*      */     
/* 1113 */     hammerha.woundLocationDmg = new byte[] { 6 };
/*      */     
/* 1115 */     hammerha.staminaStolen = 10000;
/* 1116 */     hammerha.staminaCost = 12000;
/* 1117 */     hammerha.weaponType = 0;
/* 1118 */     hammerha.kingdom = 4;
/* 1119 */     hammerha.setActorMessage("spruce");
/* 1120 */     hammerha.setOthersMessage("spruces");
/* 1121 */     addMovesByWeapon(hammerha.weaponType, hammerha);
/*      */     
/* 1123 */     SpecialMove echop = new SpecialMove(27, "Echo pain", 4, 50, 55.0D);
/* 1124 */     echop.triggeredStances = new byte[] { 6, 1 };
/*      */     
/* 1126 */     echop.woundLocation = new int[] { 1 };
/*      */     
/* 1128 */     echop.woundLocationDmg = new byte[] { 5 };
/*      */     
/* 1130 */     echop.staminaCost = 16000;
/* 1131 */     echop.staminaStolen = 14000;
/* 1132 */     echop.weaponType = 0;
/* 1133 */     echop.pukeMove = true;
/* 1134 */     echop.kingdom = 4;
/* 1135 */     echop.setActorMessage("impact");
/* 1136 */     echop.setOthersMessage("impacts");
/* 1137 */     addMovesByWeapon(echop.weaponType, echop);
/*      */     
/* 1139 */     SpecialMove highnum = new SpecialMove(28, "High number", 9, 60, 60.0D);
/* 1140 */     highnum.triggeredStances = new byte[] { 0 };
/*      */     
/* 1142 */     highnum.woundLocationDmg = new byte[] { 3 };
/*      */     
/* 1144 */     highnum.staminaCost = 18000;
/* 1145 */     highnum.weaponType = 0;
/* 1146 */     highnum.staminaStolen = 25000;
/* 1147 */     highnum.kingdom = 4;
/* 1148 */     highnum.setActorMessage("numb");
/* 1149 */     highnum.setOthersMessage("numbs");
/* 1150 */     addMovesByWeapon(highnum.weaponType, highnum);
/*      */ 
/*      */     
/* 1153 */     SpecialMove sharptw = new SpecialMove(31, "Sharp twig", 4, 30, 35.0D);
/* 1154 */     sharptw.triggeredStances = new byte[] { 5, 2 };
/*      */     
/* 1156 */     sharptw.woundLocation = new int[] { 21 };
/*      */     
/* 1158 */     sharptw.woundLocationDmg = new byte[] { 4 };
/*      */     
/* 1160 */     sharptw.staminaCost = 10000;
/* 1161 */     sharptw.stunseconds = 3;
/* 1162 */     sharptw.weaponType = 2;
/* 1163 */     sharptw.kingdom = 4;
/* 1164 */     sharptw.setActorMessage("puncture");
/* 1165 */     sharptw.setOthersMessage("punctures");
/* 1166 */     addMovesByWeapon(sharptw.weaponType, sharptw);
/*      */     
/* 1168 */     SpecialMove snakeb = new SpecialMove(32, "Snakebite", 7, 40, 45.0D);
/* 1169 */     snakeb.triggeredStances = new byte[] { 10, 4, 3 };
/*      */     
/* 1171 */     snakeb.woundLocation = new int[] { 34 };
/*      */     
/* 1173 */     snakeb.woundLocationDmg = new byte[] { 10 };
/*      */     
/* 1175 */     snakeb.staminaCost = 12000;
/* 1176 */     snakeb.stunseconds = 2;
/* 1177 */     snakeb.weaponType = 2;
/* 1178 */     snakeb.kingdom = 4;
/* 1179 */     snakeb.setActorMessage("stab");
/* 1180 */     snakeb.setOthersMessage("stabs");
/* 1181 */     addMovesByWeapon(snakeb.weaponType, snakeb);
/*      */     
/* 1183 */     SpecialMove eyesock = new SpecialMove(33, "Eyesocket", 4, 45, 50.0D);
/* 1184 */     eyesock.triggeredStances = new byte[] { 7 };
/*      */     
/* 1186 */     eyesock.woundLocation = new int[] { 1 };
/*      */     
/* 1188 */     eyesock.woundLocationDmg = new byte[] { 10 };
/*      */     
/* 1190 */     eyesock.staminaCost = 15000;
/* 1191 */     eyesock.weaponType = 2;
/* 1192 */     eyesock.stunseconds = 2;
/* 1193 */     eyesock.kingdom = 4;
/* 1194 */     eyesock.setActorMessage("penetrate");
/* 1195 */     eyesock.setOthersMessage("penetrates");
/* 1196 */     addMovesByWeapon(eyesock.weaponType, eyesock);
/*      */     
/* 1198 */     SpecialMove rflood = new SpecialMove(34, "Red flood", 3, 60, 55.0D);
/* 1199 */     rflood.triggeredStances = new byte[] { 10 };
/*      */     
/* 1201 */     rflood.woundLocation = new int[] { 25 };
/*      */     
/* 1203 */     rflood.woundLocationDmg = new byte[] { 10 };
/*      */     
/* 1205 */     rflood.staminaCost = 15000;
/* 1206 */     rflood.weaponType = 2;
/* 1207 */     rflood.stunseconds = 3;
/* 1208 */     rflood.kingdom = 4;
/* 1209 */     rflood.setActorMessage("open up");
/* 1210 */     rflood.setOthersMessage("opens up");
/* 1211 */     addMovesByWeapon(rflood.weaponType, rflood);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static SpecialMove getById(int id) {
/* 1217 */     return specialMoves.get(Integer.valueOf(id));
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
/*      */   public static void addMovesByWeapon(byte weaponType, SpecialMove move) {
/* 1230 */     Map<Integer, Set<SpecialMove>> movesForWeaponType = movesByWeapon.get(Byte.valueOf(weaponType));
/* 1231 */     if (movesForWeaponType == null)
/*      */     {
/* 1233 */       movesForWeaponType = new HashMap<>();
/*      */     }
/* 1235 */     Set<SpecialMove> tempset = movesForWeaponType.get(Integer.valueOf(move.fightingSkillLevelNeeded));
/* 1236 */     if (tempset == null)
/*      */     {
/* 1238 */       tempset = new HashSet<>();
/*      */     }
/* 1240 */     tempset.add(move);
/* 1241 */     movesForWeaponType.put(Integer.valueOf(move.fightingSkillLevelNeeded), tempset);
/* 1242 */     movesByWeapon.put(Byte.valueOf(weaponType), movesForWeaponType);
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
/*      */   public static final SpecialMove[] getMovesForWeaponSkillAndStance(Creature cret, Item weapon, int skill) {
/* 1260 */     Set<SpecialMove> tset = null;
/* 1261 */     byte creatureKingdom = cret.getKingdomTemplateId();
/* 1262 */     Map<Integer, Set<SpecialMove>> tempmap = movesByWeapon.get(Byte.valueOf(cret.getCombatHandler().getType(weapon, true)));
/* 1263 */     byte creatureStance = cret.getCombatHandler().getCurrentStance();
/* 1264 */     tset = fillTSet(skill, tempmap, creatureKingdom, creatureStance, tset);
/* 1265 */     tempmap = movesByWeapon.get(Byte.valueOf((byte)-1));
/*      */     
/* 1267 */     tset = fillTSet(skill, tempmap, creatureKingdom, creatureStance, tset);
/* 1268 */     if (tset != null)
/* 1269 */       return tset.<SpecialMove>toArray(new SpecialMove[tset.size()]); 
/* 1270 */     return emptyMoves;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Set<SpecialMove> fillTSet(int skill, Map<Integer, Set<SpecialMove>> aMovesForWeaponType, byte aCreatureKingdom, byte aCreatureStance, @Nullable Set<SpecialMove> tset) {
/* 1281 */     Set<SpecialMove> movesToReturn = tset;
/* 1282 */     if (aMovesForWeaponType != null) {
/*      */       
/* 1284 */       Set<SpecialMove> tempset = null;
/* 1285 */       for (Map.Entry<Integer, Set<SpecialMove>> entry : aMovesForWeaponType.entrySet()) {
/*      */         
/* 1287 */         if (((Integer)entry.getKey()).intValue() <= skill) {
/*      */           
/* 1289 */           tempset = entry.getValue();
/* 1290 */           if (tempset != null)
/*      */           {
/* 1292 */             for (SpecialMove tmove : tempset) {
/*      */               
/* 1294 */               if (tmove.kingdom == aCreatureKingdom)
/*      */               {
/* 1296 */                 for (byte lTriggeredStance : tmove.triggeredStances) {
/*      */                   
/* 1298 */                   if (aCreatureStance == lTriggeredStance) {
/*      */                     
/* 1300 */                     if (movesToReturn == null)
/* 1301 */                       movesToReturn = new HashSet<>(); 
/* 1302 */                     if (logger.isLoggable(Level.FINEST))
/*      */                     {
/* 1304 */                       logger.finest("Adding " + tmove.name + ", for type " + tmove.weaponType);
/*      */                     }
/* 1306 */                     movesToReturn.add(tmove);
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1315 */     return movesToReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDifficulty() {
/* 1325 */     return this.difficulty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1335 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStaminaCost() {
/* 1345 */     return this.staminaCost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSpeed() {
/* 1355 */     return this.speed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getWeaponType() {
/* 1365 */     return this.weaponType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getActorMessage() {
/* 1375 */     return this.actorMessage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setActorMessage(String aActorMessage) {
/* 1386 */     this.actorMessage = aActorMessage;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getOthersMessage() {
/* 1391 */     return this.othersMessage;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOthersMessage(String othersMessage) {
/* 1396 */     this.othersMessage = othersMessage;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\SpecialMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */