/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.MethodsItems;
/*      */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*      */ import com.wurmonline.server.combat.Weapon;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.shared.constants.SoundNames;
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
/*      */ public final class SimpleCreationEntry
/*      */   extends CreationEntry
/*      */   implements SoundNames, TimeConstants
/*      */ {
/*   63 */   private static final Logger logger = Logger.getLogger(SimpleCreationEntry.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SimpleCreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDestroyTarget, boolean aUseCapacity, float aPercentageLost, int aMinTimeSeconds, boolean aDestroyBoth, boolean aCreateOnGround, CreationCategories aCategory) {
/*   87 */     super(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDestroyTarget, aUseCapacity, aPercentageLost, aMinTimeSeconds, aDestroyBoth, aCreateOnGround, aCategory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SimpleCreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, float aPercentageLost, boolean aDepleteEqually, boolean aCreateOnGround, CreationCategories category) {
/*   95 */     super(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDepleteSource, aDepleteTarget, aPercentageLost, aDepleteEqually, aCreateOnGround, category);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SimpleCreationEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean aDepleteSource, boolean aDepleteTarget, float aPercentageLost, boolean aDepleteEqually, boolean aCreateOnGround, int aCustomChanceCutOff, double aMinimumSkill, CreationCategories category) {
/*  104 */     super(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, aDepleteSource, aDepleteTarget, aPercentageLost, aDepleteEqually, aCreateOnGround, aCustomChanceCutOff, aMinimumSkill, category);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CreationEntry cloneAndRevert() {
/*  115 */     CreationEntry toReturn = new SimpleCreationEntry(this.primarySkill, this.objectTarget, this.objectSource, this.objectCreated, this.destroyTarget, this.useCapacity, this.percentageLost, this.minTimeSeconds, this.destroyBoth, this.createOnGround, getCategory());
/*  116 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item run(Creature performer, Item source, long targetId, float counter) throws FailedException, NoSuchSkillException, NoSuchItemException, IllegalArgumentException {
/*  123 */     boolean sendFailedMessage = true;
/*  124 */     Item target = Items.getItem(targetId);
/*  125 */     Item realSource = source;
/*  126 */     Item realTarget = target;
/*  127 */     boolean create = false;
/*      */     
/*  129 */     if (performer.getVehicle() != -10L) {
/*      */       
/*  131 */       performer.getCommunicator().sendNormalServerMessage("You need to be on solid ground to do that.");
/*  132 */       throw new NoSuchItemException("Need to be on solid ground.");
/*      */     } 
/*  134 */     if (source.getTemplateId() == this.objectSource && target.getTemplateId() == this.objectTarget) {
/*  135 */       create = true;
/*  136 */     } else if (source.getTemplateId() == this.objectTarget && target.getTemplateId() == this.objectSource) {
/*      */       
/*  138 */       create = true;
/*  139 */       realTarget = source;
/*  140 */       realSource = target;
/*      */     } 
/*  142 */     if (source.deleted || target.deleted) {
/*  143 */       create = false;
/*      */     }
/*      */     
/*      */     try {
/*  147 */       int chance = (int)getDifficultyFor(realSource, realTarget, performer);
/*  148 */       if (chance == 0 || chance <= 5)
/*      */       {
/*  150 */         performer.getCommunicator().sendNormalServerMessage("This is impossible, perhaps you are not skilled enough.");
/*      */         
/*  152 */         throw new NoSuchItemException("Not enough skill.");
/*      */       }
/*      */     
/*  155 */     } catch (NoSuchTemplateException nst) {
/*      */       
/*  157 */       throw new NoSuchItemException(nst.getMessage(), nst);
/*      */     } 
/*  159 */     if (this.objectSourceMaterial != 0 && realSource.getMaterial() != this.objectSourceMaterial) {
/*      */       
/*  161 */       performer.getCommunicator().sendNormalServerMessage("Incorrect source material!");
/*      */       
/*  163 */       throw new NoSuchItemException("Incorrect source material!");
/*      */     } 
/*  165 */     if (this.objectTargetMaterial != 0 && realTarget.getMaterial() != this.objectTargetMaterial) {
/*      */       
/*  167 */       performer.getCommunicator().sendNormalServerMessage("Incorrect target material!");
/*      */       
/*  169 */       throw new NoSuchItemException("Incorrect target material!");
/*      */     } 
/*  171 */     if (this.objectCreated == 652) {
/*      */       
/*  173 */       if (realTarget.getMaterial() != 37)
/*      */       {
/*  175 */         performer.getCommunicator().sendNormalServerMessage("You need to use a pine tree.");
/*  176 */         throw new NoSuchItemException("Wrong log type.");
/*      */       }
/*      */     
/*  179 */     } else if (this.objectCreated == 847) {
/*      */       
/*  181 */       int creatureId = realSource.getData2();
/*  182 */       if (realSource.getTemplateId() == 302)
/*      */       {
/*  184 */         if (creatureId != 12)
/*      */         {
/*  186 */           performer.getCommunicator().sendNormalServerMessage("You can't create this with this type of fur.");
/*  187 */           throw new NoSuchItemException("Wrong fur type.");
/*      */         }
/*      */       
/*      */       }
/*  191 */     } else if (this.objectCreated == 849) {
/*      */       
/*  193 */       int creatureId = realSource.getData2();
/*  194 */       if (realSource.getTemplateId() == 302)
/*      */       {
/*  196 */         if (creatureId != 10)
/*      */         {
/*  198 */           performer.getCommunicator().sendNormalServerMessage("You can't create this with this type of fur.");
/*  199 */           throw new NoSuchItemException("Wrong fur type.");
/*      */         }
/*      */       
/*      */       }
/*  203 */     } else if (this.objectCreated == 846) {
/*      */       
/*  205 */       int creatureId = realSource.getData2();
/*  206 */       if (realSource.getTemplateId() == 302)
/*      */       {
/*  208 */         if (creatureId != 42)
/*      */         {
/*  210 */           performer.getCommunicator().sendNormalServerMessage("You can't create this with this type of fur.");
/*  211 */           throw new NoSuchItemException("Wrong fur type.");
/*      */         }
/*      */       
/*      */       }
/*  215 */     } else if (this.objectCreated == 848) {
/*      */       
/*  217 */       int creatureId = realSource.getData2();
/*  218 */       if (realSource.getTemplateId() == 313)
/*      */       {
/*  220 */         if (creatureId != 14)
/*      */         {
/*  222 */           performer.getCommunicator().sendNormalServerMessage("You can't create this with this type of pelt.");
/*  223 */           throw new NoSuchItemException("Wrong pelt type.");
/*      */         }
/*      */       
/*      */       }
/*  227 */     } else if (this.objectCreated == 1269) {
/*      */       InscriptionData ins;
/*  229 */       if (performer.getInventory().getNumItemsNotCoins() > 95) {
/*      */         
/*  231 */         performer.getCommunicator().sendNormalServerMessage("You don't have space for the labels.");
/*  232 */         throw new NoSuchItemException("No space.");
/*      */       } 
/*  234 */       switch (realTarget.getAuxData()) {
/*      */ 
/*      */         
/*      */         case 0:
/*  238 */           ins = target.getInscription();
/*  239 */           if (ins != null && ins.hasBeenInscribed()) {
/*      */             
/*  241 */             performer.getCommunicator().sendNormalServerMessage("You can't create labels from " + realTarget.getNameWithGenus() + ".");
/*  242 */             throw new NoSuchItemException("Inscribed paper.");
/*      */           } 
/*      */           break;
/*      */ 
/*      */         
/*      */         case 1:
/*  248 */           performer.getCommunicator().sendNormalServerMessage("You can't create labels from " + realTarget.getNameWithGenus() + ".");
/*  249 */           throw new NoSuchItemException("Recipe paper.");
/*      */ 
/*      */         
/*      */         case 2:
/*  253 */           performer.getCommunicator().sendNormalServerMessage("You can't create labels from " + realTarget.getNameWithGenus() + ".");
/*  254 */           throw new NoSuchItemException("Waxed paper.");
/*      */ 
/*      */         
/*      */         default:
/*  258 */           performer.getCommunicator().sendNormalServerMessage("You can't create labels from " + realTarget.getNameWithGenus() + ".");
/*  259 */           throw new NoSuchItemException("Harvestable report.");
/*      */       } 
/*      */     
/*      */     } 
/*  263 */     if (create) {
/*      */       
/*  265 */       ItemTemplate template = null;
/*      */       
/*      */       try {
/*  268 */         template = ItemTemplateFactory.getInstance().getTemplate(this.objectCreated);
/*      */       }
/*  270 */       catch (NoSuchTemplateException nst) {
/*      */         
/*  272 */         logger.log(Level.WARNING, "no template for creating " + this.objectCreated, (Throwable)nst);
/*  273 */         performer.getCommunicator().sendSafeServerMessage("You cannot create that item right now. Please contact administrators.");
/*      */         
/*  275 */         throw new NoSuchItemException("Failed to locate template");
/*      */       } 
/*  277 */       if (!this.createOnGround && template.isHollow()) {
/*      */         
/*  279 */         Item parent = realTarget.getParent();
/*  280 */         if (template.getSizeZ() >= parent.getContainerSizeZ() || template
/*  281 */           .getSizeY() >= parent.getContainerSizeY() || template
/*  282 */           .getSizeX() >= parent.getContainerSizeX())
/*      */         {
/*  284 */           performer.getCommunicator().sendSafeServerMessage("The " + realTarget
/*  285 */               .getName() + " will not fit in the " + parent.getName() + ".");
/*  286 */           throw new NoSuchItemException("The " + realTarget.getName() + " will not fit in the " + parent
/*  287 */               .getName() + ".");
/*      */         }
/*      */       
/*  290 */       } else if (this.createOnGround) {
/*      */         
/*  292 */         if (!MethodsItems.mayDropOnTile(performer)) {
/*      */           
/*  294 */           performer.getCommunicator().sendSafeServerMessage("There is no room in front of you to create the " + realTarget.getName() + ".");
/*  295 */           throw new NoSuchItemException("There is no room in front of you to create the " + realTarget.getName() + ".");
/*      */         } 
/*      */       } 
/*  298 */       Skills skills = performer.getSkills();
/*  299 */       Skill primSkill = null;
/*  300 */       Skill secondarySkill = null;
/*  301 */       Action act = null;
/*      */       
/*      */       try {
/*  304 */         act = performer.getCurrentAction();
/*      */       }
/*  306 */       catch (NoSuchActionException nsa) {
/*      */         
/*  308 */         logger.log(Level.WARNING, "This action doesn't exist? " + performer.getName(), (Throwable)nsa);
/*  309 */         throw new FailedException("An error occured on the server. This action was not found. Please report.");
/*      */       } 
/*      */       
/*      */       try {
/*  313 */         primSkill = skills.getSkill(this.primarySkill);
/*      */       }
/*  315 */       catch (Exception ex) {
/*      */         
/*  317 */         primSkill = skills.learn(this.primarySkill, 1.0F);
/*      */       } 
/*  319 */       if (!realSource.isWeapon() || Weapon.getSkillPenaltyForWeapon(realSource) > 0.0D) {
/*      */         
/*      */         try {
/*  322 */           secondarySkill = skills.getSkill(realSource.getPrimarySkill());
/*      */         }
/*  324 */         catch (Exception ex) {
/*      */ 
/*      */           
/*      */           try {
/*  328 */             secondarySkill = skills.learn(realSource.getPrimarySkill(), 1.0F);
/*      */           }
/*  330 */           catch (Exception exception) {}
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  335 */       int time = 10;
/*  336 */       if (counter == 1.0F) {
/*      */         
/*  338 */         int start = 150;
/*  339 */         if (realTarget.isMetal()) {
/*      */           
/*  341 */           if (this.primarySkill == 10041)
/*      */           {
/*  343 */             if (realSource.getTemperature() < 3500) {
/*      */               
/*  345 */               performer.getCommunicator().sendNormalServerMessage("The " + realSource
/*  346 */                   .getName() + " must be glowing hot to do this.");
/*  347 */               throw new NoSuchItemException("Too low temperature.");
/*      */             } 
/*      */           }
/*  350 */           if (realSource.isMetal())
/*      */           {
/*  352 */             if (realTarget.getTemperature() < 3500) {
/*      */               
/*  354 */               performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/*  355 */                   .getName() + " must be glowing hot to do this.");
/*  356 */               throw new NoSuchItemException("Too low temperature.");
/*      */             } 
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  369 */         if (realSource.isForm())
/*      */         {
/*  371 */           if (realTarget.getTemperature() < 3500) {
/*      */             
/*  373 */             performer.getCommunicator().sendNormalServerMessage("The " + realTarget
/*  374 */                 .getName() + " must be glowing hot to do this.");
/*  375 */             throw new NoSuchItemException("Too low temperature.");
/*      */           } 
/*      */         }
/*  378 */         if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */           
/*  380 */           performer.getCommunicator().sendNormalServerMessage("Your inventory is full.");
/*  381 */           throw new NoSuchItemException("Full inventory.");
/*      */         } 
/*      */         
/*  384 */         int sourceWeightToRemove = getSourceWeightToRemove(realSource, realTarget, template, false);
/*  385 */         int targetWeightToRemove = getTargetWeightToRemove(realSource, realTarget, template, false);
/*  386 */         if (this.objectCreated == 652) {
/*  387 */           targetWeightToRemove = realTarget.getWeightGrams();
/*  388 */         } else if (this.objectCreated == 1272 || this.objectCreated == 1347) {
/*  389 */           targetWeightToRemove = realTarget.getTemplate().getWeightGrams();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  395 */         checkSaneAmounts(realSource, sourceWeightToRemove, realTarget, targetWeightToRemove, template, performer, false);
/*      */         
/*  397 */         if (this.objectCreated == 37) {
/*      */           
/*  399 */           VolaTile tile = performer.getCurrentTile();
/*  400 */           if (tile != null)
/*      */           {
/*      */             
/*  403 */             for (Item i : tile.getItems()) {
/*      */               
/*  405 */               if (i.getTemplateId() == 37 && i.getTemperature() > 200) {
/*      */                 
/*  407 */                 performer.getCommunicator().sendNormalServerMessage("There is already a lit campfire here, and no room for another.");
/*  408 */                 throw new NoSuchItemException("No room for more campfires.");
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*  413 */         if (template.isOutsideOnly()) {
/*      */           
/*  415 */           VolaTile tile = performer.getCurrentTile();
/*  416 */           if (tile != null && 
/*  417 */             tile.getStructure() != null)
/*      */           {
/*  419 */             if (tile.getStructure().isTypeBridge()) {
/*      */               
/*  421 */               if (performer.getBridgeId() != -10L)
/*      */               {
/*  423 */                 performer.getCommunicator().sendNormalServerMessage("You cannot create that on a bridge.");
/*  424 */                 throw new NoSuchItemException("Can't create on a bridge.");
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  429 */               performer.getCommunicator().sendNormalServerMessage("You cannot create that inside a building.");
/*  430 */               throw new NoSuchItemException("Can't create inside.");
/*      */             } 
/*      */           }
/*      */         } 
/*  434 */         realTarget.setBusy(true);
/*      */         
/*      */         try {
/*  437 */           time = Actions.getItemCreationTime(150, performer, primSkill, this, realSource, realTarget, template
/*  438 */               .isMassProduction());
/*      */         }
/*  440 */         catch (NoSuchTemplateException nst) {
/*      */           
/*  442 */           logger.log(Level.WARNING, "No template when creating with " + realSource
/*  443 */               .getName() + " and " + realTarget.getName() + "." + nst
/*  444 */               .getMessage(), (Throwable)nst);
/*  445 */           performer.getCommunicator().sendSafeServerMessage("You cannot create that item right now. Please contact administrators.");
/*      */           
/*  447 */           throw new NoSuchItemException("No template.", nst);
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/*  452 */           performer.getCurrentAction().setTimeLeft(time);
/*      */         }
/*  454 */         catch (NoSuchActionException nsa) {
/*      */           
/*  456 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*  458 */         performer.sendActionControl(Actions.actionEntrys[148].getVerbString() + " " + template.getName(), true, time);
/*      */         
/*  460 */         if (realSource.isNoTake()) {
/*      */           
/*  462 */           performer.getCommunicator().sendNormalServerMessage("You start to work with the " + realTarget
/*  463 */               .getName() + " on the " + realSource.getName() + ".");
/*  464 */           Server.getInstance().broadCastAction(performer
/*  465 */               .getName() + " starts working with the " + realTarget.getName() + " on the " + realSource
/*  466 */               .getName() + ".", performer, 5);
/*      */         }
/*      */         else {
/*      */           
/*  470 */           performer.getCommunicator().sendNormalServerMessage("You start to work with the " + realSource
/*  471 */               .getName() + " on the " + realTarget.getName() + ".");
/*  472 */           Server.getInstance().broadCastAction(performer
/*  473 */               .getName() + " starts working with the " + realSource.getName() + " on the " + realTarget
/*  474 */               .getName() + ".", performer, 5);
/*      */         } 
/*  476 */         if (!this.depleteSource && realSource.isRepairable())
/*  477 */           realSource.setDamage(realSource.getDamage() + 0.0025F * realSource.getDamageModifier()); 
/*  478 */         if (!this.depleteTarget && realTarget.isRepairable()) {
/*  479 */           realTarget.setDamage(realTarget.getDamage() + 0.0025F * realSource.getDamageModifier());
/*      */         }
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*  485 */           time = performer.getCurrentAction().getTimeLeft();
/*  486 */           if (act.currentSecond() % 5 == 0) {
/*      */             
/*  488 */             if (!this.depleteEqually && !this.depleteSource && realSource.isRepairable())
/*  489 */               realSource.setDamage(realSource.getDamage() + 0.0025F * realSource.getDamageModifier()); 
/*  490 */             if (!this.depleteEqually && !this.depleteTarget && realTarget.isRepairable())
/*  491 */               realTarget.setDamage(realTarget.getDamage() + 0.0025F * realSource.getDamageModifier()); 
/*      */           } 
/*  493 */           if (act.mayPlaySound()) {
/*  494 */             sendSound(performer, this.objectSource, realTarget, this.objectCreated);
/*      */           }
/*  496 */         } catch (NoSuchActionException nsa) {
/*      */           
/*  498 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*      */       } 
/*  501 */       if (counter * 10.0F > time)
/*      */       {
/*  503 */         if (act.getRarity() != 0)
/*      */         {
/*  505 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/*  507 */         double bonus = performer.getVillageSkillModifier();
/*  508 */         float skillMultiplier = Math.min(Math.max(1.0F, counter / 3.0F), 20.0F);
/*  509 */         if (Servers.localServer.isChallengeOrEpicServer())
/*      */         {
/*      */           
/*  512 */           if (primSkill.hasLowCreationGain()) {
/*  513 */             skillMultiplier /= 3.0F;
/*      */           }
/*      */         }
/*  516 */         if (secondarySkill != null)
/*      */         {
/*  518 */           bonus += Math.max(-10.0D, secondarySkill.skillCheck(template.getDifficulty(), 0.0D, false, counter / 10.0F));
/*      */         }
/*  520 */         performer.sendToLoggers("Skill multiplier=" + skillMultiplier, (byte)3);
/*  521 */         float power = 1.0F;
/*  522 */         float alc = 0.0F;
/*  523 */         if (performer.isPlayer()) {
/*  524 */           alc = ((Player)performer).getAlcohol();
/*      */         }
/*  526 */         if ((Servers.localServer.entryServer && template.newbieItem) || template
/*  527 */           .getTemplateId() == 156) {
/*  528 */           bonus += 100.0D;
/*      */         }
/*  530 */         if (template.isRune()) {
/*      */           
/*  532 */           boolean godBonus = false;
/*  533 */           if (performer.getDeity() != null) {
/*      */             
/*  535 */             if ((performer.getDeity().isMountainGod() && this.objectCreated == 1289) || (performer
/*  536 */               .getDeity().isForestGod() && this.objectCreated == 1290) || (performer
/*  537 */               .getDeity().isWaterGod() && this.objectCreated == 1291) || (performer
/*  538 */               .getDeity().isHateGod() && this.objectCreated == 1292)) {
/*      */               
/*  540 */               if (performer.getFaith() >= 20.0F) {
/*  541 */                 godBonus = true;
/*      */               }
/*  543 */             } else if (performer.getFaith() < 20.0F && this.objectCreated == 1293) {
/*      */               
/*  545 */               godBonus = true;
/*      */             }
/*      */           
/*      */           }
/*  549 */           else if (this.objectCreated == 1293) {
/*      */             
/*  551 */             godBonus = true;
/*      */           } 
/*      */           
/*  554 */           if (godBonus)
/*  555 */             bonus += 100.0D; 
/*      */         } 
/*  557 */         if (realSource.isBodyPart()) {
/*  558 */           power = (float)primSkill.skillCheck((template.getDifficulty() + alc), null, bonus, false, skillMultiplier);
/*      */         } else {
/*  560 */           power = (float)primSkill.skillCheck((template.getDifficulty() + alc), realSource, bonus, false, skillMultiplier);
/*      */         } 
/*  562 */         if (performer.isRoyalSmith())
/*      */         {
/*  564 */           if (template.isMetal() && 
/*  565 */             power < 0.0F && power > -20.0F) {
/*  566 */             power = (10 + Server.rand.nextInt(10));
/*      */           }
/*      */         }
/*  569 */         byte material = realTarget.getMaterial();
/*  570 */         boolean mixedLiquids = false;
/*      */         
/*  572 */         int sourceWeightToRemove = getSourceWeightToRemove(realSource, realTarget, template, false);
/*  573 */         int targetWeightToRemove = getTargetWeightToRemove(realSource, realTarget, template, false);
/*  574 */         if (this.objectCreated == 652) {
/*  575 */           targetWeightToRemove = realTarget.getWeightGrams();
/*  576 */         } else if (this.objectCreated == 1272 || this.objectCreated == 1347) {
/*  577 */           targetWeightToRemove = realTarget.getTemplate().getWeightGrams();
/*  578 */         }  checkSaneAmounts(realSource, sourceWeightToRemove, realTarget, targetWeightToRemove, template, performer, false);
/*  579 */         if (realSource.isLiquid() && realTarget.isLiquid()) {
/*      */           
/*  581 */           material = template.getMaterial();
/*  582 */           mixedLiquids = true;
/*      */         }
/*  584 */         else if (realSource.isLiquid() && template.isLiquid()) {
/*  585 */           material = template.getMaterial();
/*  586 */         } else if (template.getTemplateId() == 1270) {
/*  587 */           material = realTarget.getMaterial();
/*  588 */         } else if ((this.depleteEqually || (this.depleteSource && this.depleteTarget)) && (realTarget
/*  589 */           .isCombine() || realTarget.isLiquid()) && (realSource
/*  590 */           .isCombine() || realSource.isLiquid())) {
/*      */           
/*  592 */           material = template.getMaterial();
/*      */         } 
/*      */         
/*  595 */         if (template.isRune())
/*      */         {
/*  597 */           material = realTarget.getMaterial();
/*      */         }
/*      */         
/*  600 */         if (usesFinalMaterial())
/*      */         {
/*  602 */           material = getFinalMaterial();
/*      */         }
/*  604 */         Item newItem = null;
/*      */         
/*      */         try {
/*  607 */           double imbueEnhancement = 1.0D + 0.23047D * realSource.getSkillSpellImprovement(primSkill.getNumber()) / 100.0D;
/*  608 */           float qlevel = (float)(power * imbueEnhancement);
/*      */           
/*  610 */           float itq = qlevel;
/*  611 */           if (realTarget.getCurrentQualityLevel() < qlevel)
/*  612 */             itq = Math.max(1.0F, realTarget.getCurrentQualityLevel()); 
/*  613 */           if (realSource.isCrude() || realTarget.isCrude())
/*  614 */             itq = 1.0F + Server.rand.nextFloat() * 10.0F; 
/*  615 */           if (this.objectCreated == 37 && (
/*  616 */             this.objectSource == 169 || this.objectTarget == 169)) {
/*  617 */             itq = 1.0F;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  626 */           long targetParentId = -10L;
/*  627 */           if (!realTarget.isNoTake())
/*      */           {
/*  629 */             targetParentId = realTarget.getParentId();
/*      */           }
/*  631 */           if (template.isTutorialItem())
/*  632 */             itq = Math.max(qlevel, (Server.rand.nextInt(20) + 1)); 
/*  633 */           if (qlevel > 0.0F || (template.isTutorialItem() && Server.rand.nextInt(100) < 90.0F)) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  638 */             if (template.isRepairable() && !template.isImproveItem)
/*      */             {
/*      */ 
/*      */               
/*  642 */               itq = Math.max(1.0F, Math.min(itq, (float)(primSkill.getKnowledge(0.0D) / 5.0D)));
/*      */             }
/*  644 */             if (realSource.getMaterial() == 56 || realSource
/*  645 */               .getMaterial() == 57) {
/*      */               
/*  647 */               float leftToMax = 100.0F - itq;
/*  648 */               itq += leftToMax / 10.0F;
/*      */             } 
/*  650 */             if (Item.getMaterialCreationBonus(material) > 0.0F) {
/*      */               
/*  652 */               float leftToMax = 100.0F - itq;
/*  653 */               itq += leftToMax * Item.getMaterialCreationBonus(material);
/*      */             } 
/*      */             
/*  656 */             if (act.getRarity() > 0 || source.getRarity() > 0 || target.getRarity() > 0)
/*      */             {
/*      */               
/*  659 */               itq = GeneralUtilities.calcRareQuality(itq, act.getRarity(), source.getRarity(), target.getRarity());
/*      */             }
/*  661 */             int newWeight = template.getWeightGrams();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  699 */             int t = this.objectCreated;
/*  700 */             int color = -1;
/*      */             
/*  702 */             if (template.isColor) {
/*      */               
/*  704 */               t = 438;
/*      */               
/*  706 */               color = WurmColor.getInitialColor(this.objectCreated, Math.max(1.0F, itq));
/*  707 */               newWeight = Math.min(newWeight, sourceWeightToRemove + targetWeightToRemove / 10);
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  716 */             if (realSource.getTemplateId() == 1254)
/*  717 */               itq = Math.min(itq * 1.25F, 99.0F); 
/*  718 */             if (this.objectCreated == 1270) {
/*  719 */               itq = Math.min((itq + realSource.getCurrentQualityLevel()) / 2.0F, 99.0F);
/*      */             }
/*  721 */             if (this.objectCreated == 1269) {
/*      */               
/*  723 */               performer.getCommunicator().sendNormalServerMessage("You cut the " + realTarget
/*  724 */                   .getName() + " into five labels.");
/*  725 */               Server.getInstance().broadCastAction(performer
/*  726 */                   .getName() + " cuts the " + realTarget.getName() + " into five labels.", performer, 5);
/*      */ 
/*      */               
/*  729 */               for (int x = 0; x < 5; x++) {
/*      */ 
/*      */                 
/*      */                 try {
/*  733 */                   newItem = ItemFactory.createItem(t, Math.max(1.0F, itq), material, act.getRarity(), performer.getName());
/*  734 */                   performer.getInventory().insertItem(newItem, true);
/*      */                 }
/*  736 */                 catch (NoSuchTemplateException nst) {
/*      */                   
/*  738 */                   logger.log(Level.WARNING, nst.getMessage());
/*      */                 }
/*  740 */                 catch (FailedException fe) {
/*      */                   
/*  742 */                   logger.log(Level.WARNING, fe.getMessage());
/*      */                 } 
/*      */               } 
/*  745 */               Items.destroyItem(realTarget.getWurmId());
/*      */             }
/*      */             else {
/*      */               
/*  749 */               newItem = ItemFactory.createItem(t, Math.max(1.0F, itq), material, act.getRarity(), performer.getName());
/*  750 */               if (template.isDragonArmour) {
/*      */                 
/*  752 */                 color = realTarget.getDragonColor();
/*  753 */                 newItem.setName(realTarget.getDragonColorName() + " " + newItem.getName());
/*      */               } 
/*  755 */               if (template.isRune())
/*      */               {
/*  757 */                 newItem.setRealTemplate(realSource.getTemplateId());
/*      */               }
/*      */ 
/*      */               
/*  761 */               if (newItem.getTemplateId() == 488) {
/*      */                 
/*  763 */                 newItem.setRealTemplate(488);
/*  764 */                 newItem.setName("endurance sandwich");
/*  765 */                 performer.achievement(196);
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  775 */               if (color != -1) {
/*  776 */                 newItem.setColor(color);
/*      */               }
/*  778 */               if (realTarget.isMetal() && realSource.isMetal()) {
/*  779 */                 newItem.setTemperature((short)((realTarget.getTemperature() + realSource.getTemperature()) / 2));
/*  780 */               } else if (realTarget.isMetal() && realTarget.isCombine()) {
/*  781 */                 newItem.setTemperature(realTarget.getTemperature());
/*  782 */               } else if (realSource.isMetal() && realSource.isCombine()) {
/*  783 */                 newItem.setTemperature(realSource.getTemperature());
/*      */               } 
/*  785 */               if (this.objectCreated == 652)
/*      */               {
/*  787 */                 newWeight = realTarget.getWeightGrams();
/*      */               }
/*  789 */               newItem.setWeight(newWeight, false);
/*  790 */               if (template.isLiquid())
/*      */               {
/*  792 */                 if (this.depleteSource && this.depleteTarget) {
/*      */                   
/*  794 */                   if (mixedLiquids) {
/*      */                     
/*  796 */                     newWeight = sourceWeightToRemove + targetWeightToRemove;
/*  797 */                     newItem.setWeight(newWeight, false);
/*  798 */                     newItem.setSizes(Math.min(template.getSizeX(), realSource.getSizeX()), 
/*  799 */                         Math.min(template.getSizeY(), realSource.getSizeY()), 
/*  800 */                         Math.min(template.getSizeZ(), realSource.getSizeZ()));
/*      */                   }
/*      */                   else {
/*      */                     
/*  804 */                     int nums = getTemplateNumbers(realSource, realTarget, template);
/*  805 */                     newWeight = Math.min(sourceWeightToRemove + targetWeightToRemove, nums * template
/*  806 */                         .getWeightGrams());
/*  807 */                     newItem.setWeight(newWeight, false);
/*  808 */                     if (template.isLiquid()) {
/*  809 */                       newItem.setSizes(Math.min(template.getSizeX(), realSource.getSizeX()), 
/*  810 */                           Math.min(template.getSizeY(), realSource.getSizeY()), 
/*  811 */                           Math.min(template.getSizeZ(), realSource.getSizeZ()));
/*      */                     } else {
/*  813 */                       newItem.setSizes(
/*  814 */                           (int)Math.min(template.getSizeX() * ((nums < 3) ? nums : (2.0F + nums / 3.0F)), realSource
/*  815 */                             .getSizeX()), 
/*  816 */                           (int)Math.min(template.getSizeY() * Math.max(1.0F, nums / 3.0F), realSource
/*  817 */                             .getSizeY()), 
/*  818 */                           (int)Math.min(template.getSizeZ() * Math.max(1.0F, nums / 3.0F), realSource
/*  819 */                             .getSizeZ()));
/*      */                     } 
/*      */                   } 
/*  822 */                 } else if (this.depleteSource) {
/*      */                   
/*  824 */                   int nums = getTemplateNumbersForSource(realSource, template);
/*  825 */                   newWeight = Math.min(sourceWeightToRemove, nums * template.getWeightGrams());
/*  826 */                   newItem.setWeight(newWeight, false);
/*  827 */                   if (template.isLiquid()) {
/*  828 */                     newItem.setSizes(Math.min(template.getSizeX(), realSource.getSizeX()), 
/*  829 */                         Math.min(template.getSizeY(), realSource.getSizeY()), 
/*  830 */                         Math.min(template.getSizeZ(), realSource.getSizeZ()));
/*      */                   } else {
/*      */                     
/*  833 */                     newItem.setSizes(
/*  834 */                         (int)Math.min(template.getSizeX() * ((nums < 3) ? nums : (2.0F + nums / 3.0F)), realSource
/*  835 */                           .getSizeX()), 
/*  836 */                         (int)Math.min(template.getSizeY() * Math.max(1.0F, nums / 3.0F), realSource.getSizeY()), 
/*  837 */                         (int)Math.min(template.getSizeZ() * Math.max(1.0F, nums / 3.0F), realSource.getSizeZ()));
/*      */                   } 
/*  839 */                 } else if (this.depleteTarget) {
/*      */                   
/*  841 */                   int nums = getTemplateNumbersForTarget(realTarget, template);
/*  842 */                   newWeight = Math.min(targetWeightToRemove, nums * template.getWeightGrams());
/*  843 */                   newItem.setWeight(newWeight, false);
/*  844 */                   if (template.isLiquid()) {
/*  845 */                     newItem.setSizes(Math.min(template.getSizeX(), realTarget.getSizeX()), 
/*  846 */                         Math.min(template.getSizeY(), realTarget.getSizeY()), 
/*  847 */                         Math.min(template.getSizeZ(), realTarget.getSizeZ()));
/*      */                   } else {
/*  849 */                     newItem.setSizes(
/*  850 */                         (int)Math.min(template.getSizeX() * ((nums < 3) ? nums : (2.0F + nums / 3.0F)), realTarget
/*  851 */                           .getSizeX()), 
/*  852 */                         (int)Math.min(template.getSizeY() * Math.max(1.0F, nums / 3.0F), realTarget.getSizeY()), 
/*  853 */                         (int)Math.min(template.getSizeZ() * Math.max(1.0F, nums / 3.0F), realTarget.getSizeZ()));
/*      */                   } 
/*      */                 }  } 
/*  856 */               if (!performer.skippedTutorial() && performer.getTutorialLevel() != 9999) {
/*      */                 
/*  858 */                 if (performer.getTutorialLevel() == 4 && newItem.getTemplateId() == 36)
/*      */                 {
/*  860 */                   performer.missionFinished(true, true);
/*      */                 }
/*  862 */                 if (performer.getTutorialLevel() == 5 && newItem.getTemplateId() == 37)
/*      */                 {
/*  864 */                   performer.missionFinished(true, true);
/*      */                 }
/*  866 */                 if (performer.getTutorialLevel() == 5 && newItem.getTemplateId() == 37)
/*      */                 {
/*  868 */                   performer.missionFinished(true, true);
/*      */                 }
/*  870 */                 if (performer.getTutorialLevel() == 9 && newItem.getTemplateId() == 22)
/*      */                 {
/*  872 */                   performer.missionFinished(true, true);
/*      */                 }
/*      */               } 
/*      */               
/*  876 */               if (template.isKingdomFlag)
/*      */               {
/*  878 */                 newItem.setAuxData(performer.getKingdomId());
/*      */               }
/*  880 */               if (newItem.getRarity() > 2) {
/*  881 */                 performer.achievement(300);
/*  882 */               } else if (newItem.getRarity() > 1) {
/*  883 */                 performer.achievement(302);
/*  884 */               } else if (newItem.getRarity() > 0) {
/*  885 */                 performer.achievement(301);
/*  886 */               }  if (newItem != null) {
/*      */ 
/*      */                 
/*  889 */                 if (t == 133 && realSource.getTemplateId() == 1254)
/*      */                 {
/*  891 */                   newItem.setRealTemplate(1254);
/*      */                 }
/*  893 */                 if (realSource.isKey()) {
/*      */                   
/*  895 */                   long lockId = realSource.getLockId();
/*  896 */                   newItem.setData(lockId);
/*      */                 }
/*  898 */                 else if (template.isKey()) {
/*      */                   
/*      */                   try
/*      */                   {
/*  902 */                     newItem.setLockId(realSource.getData());
/*  903 */                     Item lock = Items.getItem(realSource.getData());
/*  904 */                     lock.addKey(newItem.getWurmId());
/*      */                   }
/*  906 */                   catch (NoSuchItemException nsi)
/*      */                   {
/*  908 */                     logger.log(Level.WARNING, performer.getName() + " No lock for key copy ", (Throwable)nsi);
/*      */                   }
/*      */                 
/*  911 */                 } else if (realSource.isPassFullData()) {
/*  912 */                   newItem.setData(realSource.getData());
/*  913 */                 } else if (realTarget.isPassFullData()) {
/*  914 */                   newItem.setData(realTarget.getData());
/*  915 */                 } else if (this.objectCreated == 846 || this.objectCreated == 847 || this.objectCreated == 848 || this.objectCreated == 849) {
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*  920 */                   newItem.setData2(realTarget.getData2());
/*  921 */                   if (realTarget.getData2() == 14)
/*  922 */                     newItem.setWeight(400, true); 
/*  923 */                   newItem.setMaterial((byte)16);
/*      */                 } 
/*      */                 
/*  926 */                 if (newItem.getWeightGrams() <= 0 || newItem.deleted) {
/*      */                   
/*  928 */                   Items.decay(newItem.getWurmId(), newItem.getDbStrings());
/*  929 */                   newItem = null;
/*  930 */                   if (sendFailedMessage) {
/*  931 */                     performer.getCommunicator().sendNormalServerMessage("You failed to create " + template
/*  932 */                         .getNameWithGenus() + ".");
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  943 */               if (newItem != null) {
/*      */                 
/*  945 */                 int extraWeight = getExtraWeight(template);
/*      */                 
/*  947 */                 if (extraWeight > 10 && !realTarget.isLiquid()) {
/*      */                   
/*  949 */                   int itc = getScrapMaterial(material);
/*  950 */                   if (itc != -1) {
/*      */                     
/*      */                     try {
/*      */ 
/*      */                       
/*  955 */                       Item scrap = ItemFactory.createItem(itc, realTarget.getCurrentQualityLevel() / 10.0F, material, (byte)0, performer
/*  956 */                           .getName());
/*  957 */                       scrap.setWeight(extraWeight, false);
/*  958 */                       scrap.setTemperature(realTarget.getTemperature());
/*  959 */                       performer.getInventory().insertItem(scrap, true);
/*      */                     }
/*  961 */                     catch (NoSuchTemplateException nst) {
/*      */                       
/*  963 */                       logger.log(Level.WARNING, performer
/*  964 */                           .getName() + " tid= " + itc + ", " + nst.getMessage(), (Throwable)nst);
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */ 
/*      */                 
/*  970 */                 if (newItem.isWeaponSword()) {
/*  971 */                   performer.achievement(198);
/*  972 */                 } else if (newItem.isWeaponCrush()) {
/*  973 */                   performer.achievement(199);
/*  974 */                 } else if (newItem.isWeaponAxe()) {
/*  975 */                   performer.achievement(200);
/*  976 */                 } else if (newItem.isShield()) {
/*  977 */                   performer.achievement(201);
/*      */                 } 
/*      */                 
/*  980 */                 if (newItem.getTemplateId() == 63) {
/*  981 */                   performer.achievement(515);
/*  982 */                 } else if (newItem.getTemplateId() == 218 || newItem.getTemplateId() == 217) {
/*  983 */                   performer.achievement(517);
/*  984 */                 } else if (newItem.getTemplateId() == 223) {
/*  985 */                   performer.achievement(530);
/*  986 */                 } else if (newItem.isHolyItem(performer.getDeity()) && newItem.getMaterial() == 96) {
/*  987 */                   performer.achievement(616);
/*  988 */                 } else if (newItem.getTemplateId() == 324) {
/*  989 */                   performer.achievement(628);
/*      */                 } 
/*  991 */                 if (newItem.getTemplate().isRune())
/*  992 */                   performer.achievement(489); 
/*  993 */                 if (!newItem.getTemplate().isMetalLump())
/*      */                 {
/*  995 */                   switch (newItem.getMaterial()) {
/*      */                     
/*      */                     case 11:
/*  998 */                       performer.achievement(495);
/*      */                       break;
/*      */                     case 56:
/* 1001 */                       performer.achievement(496);
/*      */                       break;
/*      */                     case 30:
/* 1004 */                       performer.achievement(497);
/*      */                       break;
/*      */                     case 31:
/* 1007 */                       performer.achievement(498);
/*      */                       break;
/*      */                     case 10:
/* 1010 */                       performer.achievement(499);
/*      */                       break;
/*      */                     case 57:
/* 1013 */                       performer.achievement(500);
/*      */                       break;
/*      */                     case 7:
/* 1016 */                       performer.achievement(501);
/*      */                       break;
/*      */                     case 12:
/* 1019 */                       performer.achievement(502);
/*      */                       break;
/*      */                     case 67:
/* 1022 */                       performer.achievement(503);
/*      */                       break;
/*      */                     case 9:
/* 1025 */                       performer.achievement(505);
/*      */                       break;
/*      */                     case 34:
/* 1028 */                       performer.achievement(506);
/*      */                       break;
/*      */                     case 13:
/* 1031 */                       performer.achievement(507);
/*      */                       break;
/*      */                     case 8:
/* 1034 */                       performer.achievement(504);
/*      */                       break;
/*      */                     case 96:
/* 1037 */                       performer.achievement(512);
/*      */                       break;
/*      */                   } 
/*      */                 }
/*      */               } 
/* 1042 */               if (newItem.getTemplateId() == 36) {
/* 1043 */                 PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.CREATE_KINDLING);
/* 1044 */               } else if (newItem.getTemplateId() == 37) {
/* 1045 */                 PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.CREATE_CAMPFIRE);
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/* 1050 */             String mess = "You realize this was a meaningless effort. The " + template.getName() + " is useless.";
/* 1051 */             if (qlevel < 20.0F) {
/* 1052 */               mess = "You almost made it, but the " + template.getName() + " is useless.";
/* 1053 */             } else if (qlevel < 40.0F) {
/* 1054 */               mess = "This could very well work next time, but the " + template.getName() + " is useless.";
/* 1055 */             } else if (qlevel < 60.0F) {
/* 1056 */               mess = "Too many problems solved in the wrong way makes the " + template.getName() + " useless.";
/* 1057 */             } else if (qlevel < 80.0F) {
/* 1058 */               mess = "You fail miserably with the " + template.getName() + ".";
/* 1059 */             }  performer.getCommunicator().sendNormalServerMessage(mess);
/* 1060 */             Server.getInstance().broadCastAction(performer
/* 1061 */                 .getName() + " fails with the " + template.getName() + ".", performer, 5);
/* 1062 */             if (!realTarget.isKey() && !realTarget.isBodyPart())
/*      */             {
/* 1064 */               if (realTarget.isRepairable() || realTarget.getTemplateId() == 288) {
/* 1065 */                 realTarget.setDamage(realTarget.getDamage() + Math.abs(qlevel / 5000.0F));
/* 1066 */               } else if (!realTarget.isCombine() && 
/* 1067 */                 !realTarget.isLiquid() && realTarget
/* 1068 */                 .getQualityLevel() >= 2.0F && realTarget.getQualityLevel() > primSkill.getKnowledge(0.0D)) {
/* 1069 */                 realTarget.setQualityLevel(realTarget.getQualityLevel() - 0.5F);
/* 1070 */               } else if (this.objectCreated == 1114) {
/* 1071 */                 realTarget.setDamage(realTarget.getDamage() + Math.abs(qlevel / 10.0F));
/* 1072 */               } else if (realTarget.isCombine() || realTarget.isLiquid()) {
/* 1073 */                 realTarget.setWeight(realTarget.getWeightGrams() - (int)(template.getWeightGrams() / 10.0F), true);
/*      */               } 
/*      */             }
/* 1076 */             if (!realSource.isKey() && !realSource.isBodyPart())
/*      */             {
/* 1078 */               if (realSource.isRepairable() || realSource.getTemplateId() == 288) {
/* 1079 */                 realSource.setDamage(realSource.getDamage() + Math.abs(qlevel / 5000.0F));
/* 1080 */               } else if (!realSource.isCombine() && 
/* 1081 */                 !realSource.isLiquid() && realSource
/* 1082 */                 .getQualityLevel() >= 2.0F && realSource.getQualityLevel() > primSkill.getKnowledge(0.0D)) {
/* 1083 */                 realSource.setQualityLevel(realSource.getQualityLevel() - 0.5F);
/* 1084 */               } else if (this.objectCreated == 1114) {
/* 1085 */                 realSource.setDamage(realSource.getDamage() + Math.abs(qlevel / 10.0F));
/* 1086 */               } else if (realSource.isCombine() || realSource.isLiquid()) {
/*      */                 
/* 1088 */                 if (realSource.getTemplateId() == 73) {
/*      */                   
/* 1090 */                   realSource.setWeight(realSource.getWeightGrams() - 50, true);
/*      */                 } else {
/*      */                   
/* 1093 */                   realSource.setWeight(realSource.getWeightGrams() - (int)(template.getWeightGrams() / 10.0F), true);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1102 */           Item parent = realSource;
/* 1103 */           if (newItem != null && newItem.isLiquid()) {
/*      */ 
/*      */             
/* 1106 */             if (!realSource.isContainerLiquid()) {
/*      */               
/*      */               try {
/* 1109 */                 parent = realSource.getParent();
/*      */               }
/* 1111 */               catch (NoSuchItemException noSuchItemException) {}
/*      */             }
/*      */             
/* 1114 */             if (parent == null || !parent.isContainerLiquid()) {
/*      */               
/*      */               try {
/*      */                 
/* 1118 */                 Item parent2 = Items.getItem(targetParentId);
/* 1119 */                 if (parent2.isEmpty(false)) {
/* 1120 */                   parent = parent2;
/*      */                 }
/* 1122 */               } catch (NoSuchItemException noSuchItemException) {}
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1127 */           if (newItem != null) {
/*      */             
/* 1129 */             boolean canTransferSource = false;
/* 1130 */             boolean canTransferTarget = false;
/* 1131 */             CreationEntry ce = CreationMatrix.getInstance().getCreationEntry(realSource.getTemplateId());
/* 1132 */             if (ce != null)
/*      */             {
/* 1134 */               if (ce.getCategory() == CreationCategories.WEAPON_HEADS) {
/* 1135 */                 canTransferSource = true;
/* 1136 */               } else if (ce.getCategory() == CreationCategories.TOOL_PARTS) {
/* 1137 */                 canTransferSource = true;
/* 1138 */               } else if (ce.getCategory() == CreationCategories.BLADES) {
/* 1139 */                 canTransferSource = true;
/*      */               }  } 
/* 1141 */             ce = CreationMatrix.getInstance().getCreationEntry(realTarget.getTemplateId());
/* 1142 */             if (ce != null)
/*      */             {
/* 1144 */               if (ce.getCategory() == CreationCategories.WEAPON_HEADS) {
/* 1145 */                 canTransferTarget = true;
/* 1146 */               } else if (ce.getCategory() == CreationCategories.TOOL_PARTS) {
/* 1147 */                 canTransferTarget = true;
/* 1148 */               } else if (ce.getCategory() == CreationCategories.BLADES) {
/* 1149 */                 canTransferTarget = true;
/*      */               } 
/*      */             }
/* 1152 */             if (!realSource.isLiquid() && sourceWeightToRemove >= realSource.getWeightGrams() && canTransferSource) {
/*      */               
/* 1154 */               if (realSource.getRarity() > newItem.getRarity())
/* 1155 */                 newItem.setRarity(realSource.getRarity()); 
/* 1156 */               if (realSource.getSpellEffects() != null) {
/*      */                 
/* 1158 */                 ItemSpellEffects deletedEffects = realSource.getSpellEffects();
/* 1159 */                 ItemSpellEffects newEffects = newItem.getSpellEffects();
/* 1160 */                 if (newEffects == null)
/* 1161 */                   newEffects = new ItemSpellEffects(newItem.getWurmId()); 
/* 1162 */                 for (SpellEffect e : deletedEffects.getEffects()) {
/*      */                   
/* 1164 */                   if (newEffects.getSpellEffect(e.type) != null) {
/* 1165 */                     newEffects.getSpellEffect(e.type).setPower(Math.max(e.getPower(), newEffects.getSpellEffect(e.type).getPower()));
/*      */                   } else {
/* 1167 */                     newEffects.addSpellEffect(new SpellEffect(newItem.getWurmId(), e.type, e.getPower(), 20000000));
/*      */                   } 
/*      */                 } 
/*      */               } 
/* 1171 */             }  if (!realTarget.isLiquid() && targetWeightToRemove >= realTarget.getWeightGrams() && canTransferTarget) {
/*      */               
/* 1173 */               if (realTarget.getRarity() > newItem.getRarity())
/* 1174 */                 newItem.setRarity(realTarget.getRarity()); 
/* 1175 */               if (realTarget.getSpellEffects() != null) {
/*      */                 
/* 1177 */                 ItemSpellEffects deletedEffects = realTarget.getSpellEffects();
/* 1178 */                 ItemSpellEffects newEffects = newItem.getSpellEffects();
/* 1179 */                 if (newEffects == null)
/* 1180 */                   newEffects = new ItemSpellEffects(newItem.getWurmId()); 
/* 1181 */                 for (SpellEffect e : deletedEffects.getEffects()) {
/*      */                   
/* 1183 */                   if (newEffects.getSpellEffect(e.type) != null) {
/* 1184 */                     newEffects.getSpellEffect(e.type).setPower(Math.max(e.getPower(), newEffects.getSpellEffect(e.type).getPower()));
/*      */                   } else {
/* 1186 */                     newEffects.addSpellEffect(new SpellEffect(newItem.getWurmId(), e.type, e.getPower(), 20000000));
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/* 1191 */           }  destroyItems(newItem, realSource, realTarget, mixedLiquids, targetWeightToRemove, sourceWeightToRemove);
/* 1192 */           if (realTarget.getTemplateId() == 385 && 
/* 1193 */             realTarget.getWeightGrams() <= 24000)
/* 1194 */             realTarget.setTemplateId(9); 
/* 1195 */           if (newItem != null && newItem.isLiquid())
/*      */           {
/* 1197 */             if (parent != null)
/*      */             {
/* 1199 */               MethodsItems.fillContainer(act, parent, newItem, performer, false);
/* 1200 */               if (!newItem.deleted && newItem.getParentId() == -10L)
/*      */               {
/* 1202 */                 performer.getCommunicator().sendNormalServerMessage("Not all the " + newItem
/* 1203 */                     .getName() + " would fit in the " + parent.getName() + ".");
/* 1204 */                 Items.decay(newItem.getWurmId(), newItem.getDbStrings());
/* 1205 */                 newItem = null;
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/*      */         }
/* 1215 */         catch (Exception ex) {
/*      */           
/* 1217 */           logger.log(Level.WARNING, "Failed to create item.", ex);
/*      */         } 
/* 1219 */         performer.getStatus().modifyStamina(-counter * 1000.0F);
/* 1220 */         if (newItem != null) {
/*      */           
/* 1222 */           MissionTriggers.activateTriggers(performer, newItem, 148, 0L, 1);
/* 1223 */           return newItem;
/*      */         } 
/*      */ 
/*      */         
/* 1227 */         throw new NoSuchItemException("The item was not created.");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1232 */       throw new IllegalArgumentException("Illegal parameters for this entry: source=" + realSource.getTemplateId() + ", target=" + realTarget
/* 1233 */           .getTemplateId() + " when creating " + this.objectCreated);
/* 1234 */     }  throw new FailedException("Failed skillcheck.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void destroyItems(Item newItem, Item realSource, Item realTarget, boolean mixedLiquids, int targetWeightToRemove, int sourceWeightToRemove) {
/* 1240 */     if (newItem != null)
/*      */     {
/* 1242 */       if (mixedLiquids) {
/*      */         
/* 1244 */         Items.destroyItem(realTarget.getWurmId());
/* 1245 */         Items.destroyItem(realSource.getWurmId());
/*      */       }
/*      */       else {
/*      */         
/* 1249 */         if (sourceWeightToRemove > 0) {
/*      */           
/* 1251 */           if (newItem.isLiquid() && realSource.isLiquid()) {
/* 1252 */             Items.destroyItem(realSource.getWurmId());
/* 1253 */           } else if (sourceWeightToRemove < realSource.getWeightGrams()) {
/* 1254 */             realSource.setWeight(realSource.getWeightGrams() - sourceWeightToRemove, true);
/*      */           } else {
/* 1256 */             Items.destroyItem(realSource.getWurmId());
/*      */           } 
/* 1258 */         } else if (realSource.isRepairable()) {
/* 1259 */           realSource.setDamage(realSource.getDamage() + 0.004F * realSource.getDamageModifier());
/* 1260 */         }  if (targetWeightToRemove > 0) {
/*      */           
/* 1262 */           if (targetWeightToRemove < realTarget.getWeightGrams()) {
/* 1263 */             realTarget.setWeight(realTarget.getWeightGrams() - targetWeightToRemove, true);
/*      */           } else {
/* 1265 */             Items.destroyItem(realTarget.getWurmId());
/*      */           } 
/* 1267 */         } else if (realTarget.isRepairable()) {
/* 1268 */           realTarget.setDamage(realTarget.getDamage() + 0.004F * realTarget.getDamageModifier());
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void sendSound(Creature aPerformer, int aObjectSource, Item aTarget, int aObjectCreated) {
/* 1275 */     String soundId = "";
/* 1276 */     if (aObjectSource == 24) {
/* 1277 */       soundId = "sound.work.carpentry.saw";
/* 1278 */     } else if (aObjectSource == 8) {
/* 1279 */       soundId = "sound.work.carpentry.carvingknife";
/* 1280 */     } else if (aObjectSource == 62) {
/* 1281 */       soundId = "sound.work.smithing.hammer";
/* 1282 */     } else if (aObjectSource == 64 || aObjectSource == 185) {
/* 1283 */       soundId = "sound.work.smithing.hammer";
/* 1284 */     } else if (aObjectSource == 226) {
/* 1285 */       soundId = "sound.work.tailoring.loom";
/* 1286 */     } else if (aObjectSource == 139) {
/* 1287 */       soundId = "sound.work.tailoring.spindle";
/* 1288 */     } else if (aObjectSource == 97) {
/* 1289 */       soundId = "sound.work.stonecutting";
/* 1290 */     } else if (aObjectCreated == 36) {
/* 1291 */       soundId = "sound.work.woodcutting.kindling";
/* 1292 */     }  if (soundId.length() > 0)
/*      */     {
/* 1294 */       SoundPlayer.playSound(soundId, aPerformer, 1.0F);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\SimpleCreationEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */