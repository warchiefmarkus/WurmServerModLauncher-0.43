/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.spells.Rebirth;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import java.io.IOException;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
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
/*      */ final class CorpseBehaviour
/*      */   extends ItemBehaviour
/*      */   implements CreatureTypes
/*      */ {
/*   67 */   private static final Logger logger = Logger.getLogger(CorpseBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CorpseBehaviour() {
/*   74 */     super((short)28);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*   85 */     List<ActionEntry> toReturn = new LinkedList<>();
/*   86 */     toReturn.addAll(super.getBehavioursFor(performer, target));
/*   87 */     if (performer.getDeity() != null && performer.getDeity().isHateGod()) {
/*      */       
/*      */       try {
/*      */         
/*   91 */         CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target.getData1());
/*   92 */         if (template.isHuman()) {
/*   93 */           toReturn.add(Actions.actionEntrys[141]);
/*      */         }
/*   95 */       } catch (NoSuchCreatureTemplateException nst) {
/*      */         
/*   97 */         logger.log(Level.WARNING, "No creatureTemplate for corpse " + target
/*   98 */             .getName() + " with id " + target.getWurmId());
/*      */       } 
/*      */     }
/*  101 */     return toReturn;
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
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  113 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  114 */     if (source.isWeaponKnife() || source.isWeaponSlash() || source.isWeaponPierce())
/*      */     {
/*  116 */       if (!target.isButchered() && (target.getWasBrandedTo() == -10L || target.mayCommand(performer)))
/*  117 */         toReturn.add(Actions.actionEntrys[120]); 
/*      */     }
/*  119 */     if (source.getTemplateId() == 25 || source.getTemplateId() == 821 || source
/*  120 */       .getTemplateId() == 20) {
/*      */       
/*  122 */       if (target.getTemplateId() == 272 && (target.getWasBrandedTo() == -10L || target.mayCommand(performer)))
/*      */       {
/*  124 */         toReturn.add(Actions.actionEntrys[119]);
/*  125 */         toReturn.add(Actions.actionEntrys[707]);
/*      */       }
/*      */     
/*  128 */     } else if (source.getTemplateId() == 338) {
/*      */       
/*  130 */       toReturn.add(Actions.actionEntrys[118]);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  180 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  191 */     boolean done = true;
/*  192 */     if (action == 1) {
/*      */       
/*  194 */       if (target.isButchered()) {
/*  195 */         performer.getCommunicator().sendNormalServerMessage("You see the butchered " + target.getName() + ".");
/*      */       } else {
/*  197 */         performer.getCommunicator().sendNormalServerMessage("You see the " + target.getName() + ".");
/*  198 */       }  if (target.getWasBrandedTo() != -10L) {
/*      */         try
/*      */         {
/*      */           
/*  202 */           Village wasBrandedTo = Villages.getVillage((int)target.getWasBrandedTo());
/*  203 */           performer.getCommunicator().sendNormalServerMessage("It still shows a brand from village " + wasBrandedTo.getName() + ".");
/*      */         }
/*  205 */         catch (NoSuchVillageException e)
/*      */         {
/*  207 */           target.setWasBrandedTo(-10L);
/*      */         }
/*      */       
/*      */       }
/*  211 */     } else if (action == 141) {
/*      */       
/*  213 */       if (performer.getDeity() != null && performer.getDeity().isHateGod()) {
/*      */         
/*      */         try {
/*      */           
/*  217 */           CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target.getData1());
/*  218 */           if (template.isHuman()) {
/*  219 */             done = MethodsReligion.pray(act, performer, counter);
/*      */           }
/*  221 */         } catch (NoSuchCreatureTemplateException nst) {
/*      */           
/*  223 */           logger.log(Level.WARNING, "No creatureTemplate for corpse " + target
/*  224 */               .getName() + " with id " + target.getWurmId());
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       
/*  229 */       done = super.action(act, performer, target, action, counter);
/*  230 */     }  return done;
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
/*      */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  243 */     boolean done = true;
/*  244 */     if (action == 120) {
/*      */       
/*  246 */       if (target.getWasBrandedTo() != -10L && !target.mayCommand(performer)) {
/*  247 */         performer.getCommunicator().sendNormalServerMessage("You don't have permission to do that!");
/*  248 */       } else if (!source.isWeaponKnife() && !source.isWeaponSlash() && !source.isWeaponPierce()) {
/*  249 */         performer.getCommunicator().sendNormalServerMessage("You can't butcher with that!");
/*      */       } else {
/*  251 */         done = butcher(performer, source, target, counter);
/*      */       } 
/*  253 */     } else if (action == 119 || action == 707) {
/*      */       
/*  255 */       if (MethodsItems.isLootableBy(performer, target)) {
/*      */         
/*  257 */         if (source.getTemplateId() == 25 || source.getTemplateId() == 20) {
/*      */           
/*  259 */           done = bury(act, performer, source, target, counter, action);
/*      */         }
/*  261 */         else if (source.getTemplateId() == 821) {
/*      */           
/*  263 */           done = createGrave(act, performer, source, target, counter, action);
/*      */         } else {
/*      */           
/*  266 */           done = true;
/*      */         } 
/*      */       } else {
/*      */         
/*  270 */         done = true;
/*  271 */         performer.getCommunicator().sendNormalServerMessage("You may not bury that corpse.");
/*      */       }
/*      */     
/*  274 */     } else if (action == 1) {
/*      */       
/*  276 */       if (target.isButchered()) {
/*  277 */         performer.getCommunicator().sendNormalServerMessage("You see the butchered " + target.getName());
/*      */       } else {
/*  279 */         performer.getCommunicator().sendNormalServerMessage("You see the " + target.getName());
/*  280 */       }  if (target.getWasBrandedTo() != -10L) {
/*      */         try
/*      */         {
/*      */           
/*  284 */           Village wasBrandedTo = Villages.getVillage((int)target.getWasBrandedTo());
/*  285 */           performer.getCommunicator().sendNormalServerMessage("It still shows a brand from village " + wasBrandedTo.getName() + ".");
/*      */         }
/*  287 */         catch (NoSuchVillageException e)
/*      */         {
/*  289 */           target.setWasBrandedTo(-10L);
/*      */         }
/*      */       
/*      */       }
/*  293 */     } else if (action == 141) {
/*      */       
/*  295 */       if (performer.getDeity() != null && performer.getDeity().isHateGod()) {
/*      */         
/*      */         try {
/*      */           
/*  299 */           CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target.getData1());
/*  300 */           if (template.isHuman()) {
/*  301 */             done = MethodsReligion.pray(act, performer, counter);
/*      */           } else {
/*  303 */             done = true;
/*      */           } 
/*  305 */         } catch (NoSuchCreatureTemplateException nst) {
/*      */           
/*  307 */           done = true;
/*  308 */           logger.log(Level.WARNING, "No creatureTemplate for corpse " + target
/*  309 */               .getName() + " with id " + target.getWurmId());
/*      */         }
/*      */       
/*      */       }
/*  313 */     } else if (action == 118) {
/*      */       
/*  315 */       if (source.getTemplateId() == 338)
/*      */       {
/*  317 */         if (source.getAuxData() > 0) {
/*      */           
/*  319 */           if (Rebirth.mayRaise(performer, target, true)) {
/*      */             
/*  321 */             Rebirth.raise(50.0D, performer, target, false);
/*  322 */             source.setAuxData((byte)(source.getAuxData() - 1));
/*      */           } 
/*      */         } else {
/*      */           
/*  326 */           performer.getCommunicator().sendNormalServerMessage("The " + source
/*  327 */               .getName() + " emits no sense of power right now.");
/*      */         } 
/*      */       }
/*      */     } else {
/*  331 */       done = super.action(act, performer, source, target, action, counter);
/*  332 */     }  return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean createGrave(Action act, Creature performer, Item gravestone, Item corpse, float counter, short action) {
/*  338 */     int time = 0;
/*  339 */     Skill dig = null;
/*      */     
/*      */     try {
/*  342 */       CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(corpse.getData1());
/*      */       
/*  344 */       if (corpse.getParentId() != -10L && corpse.getNumItemsNotCoins() > 0) {
/*      */         
/*      */         try {
/*      */           
/*  348 */           Item parent = Items.getItem(corpse.getParentId());
/*  349 */           if (parent.getNumItemsNotCoins() >= 100)
/*      */           {
/*  351 */             performer.getCommunicator().sendNormalServerMessage("The " + parent
/*  352 */                 .getName() + " is full so you need to bury the corpse from the ground.");
/*  353 */             return true;
/*      */           }
/*      */         
/*  356 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  362 */       Item shovel = getItemOfType(performer.getInventory(), 25);
/*      */       
/*  364 */       if (shovel == null) {
/*      */         
/*  366 */         performer.getCommunicator().sendNormalServerMessage("You need a shovel in your inventory to do this action.");
/*  367 */         return true;
/*      */       } 
/*      */       
/*  370 */       if (counter == 1.0F) {
/*      */         
/*  372 */         if (!performer.isOnSurface()) {
/*      */           
/*  374 */           performer.getCommunicator().sendNormalServerMessage("The ground is too hard to bury anything in.");
/*  375 */           return true;
/*      */         } 
/*      */         
/*  378 */         VolaTile tile = performer.getCurrentTile();
/*  379 */         int t = Server.surfaceMesh.getTile(tile.tilex, tile.tiley);
/*  380 */         float h = Tiles.decodeHeight(t);
/*  381 */         if (h < 0.0F) {
/*      */           
/*  383 */           performer.getCommunicator().sendNormalServerMessage("The water is too deep.");
/*  384 */           return true;
/*      */         } 
/*      */         
/*  387 */         if (template.isHuman())
/*      */         {
/*  389 */           if (performer.getDeity() != null && performer.getDeity().isAllowsButchering()) {
/*      */             
/*  391 */             if (performer.faithful) {
/*      */               
/*  393 */               performer.getCommunicator().sendNormalServerMessage(
/*  394 */                   (performer.getDeity()).name + " wants corpses to rot in the open.");
/*  395 */               return true;
/*      */             } 
/*      */ 
/*      */             
/*  399 */             performer.maybeModifyAlignment(1.0F);
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  405 */         Skills skills = performer.getSkills();
/*      */         
/*      */         try {
/*  408 */           dig = skills.getSkill(1009);
/*      */         }
/*  410 */         catch (NoSuchSkillException nss) {
/*      */           
/*  412 */           dig = skills.learn(1009, 1.0F);
/*      */         } 
/*  414 */         time = Actions.getStandardActionTime(performer, dig, shovel, 0.0D);
/*  415 */         act.setTimeLeft(time);
/*  416 */         performer.sendActionControl(Actions.actionEntrys[119].getVerbString(), true, time);
/*  417 */         performer.getCommunicator().sendNormalServerMessage("You start to bury the " + corpse.getName() + ".");
/*  418 */         Server.getInstance().broadCastAction(performer.getName() + " starts to bury the " + corpse.getName() + ".", performer, 5);
/*      */         
/*  420 */         return false;
/*      */       } 
/*      */       
/*  423 */       time = act.getTimeLeft();
/*      */       
/*  425 */       if (act.currentSecond() % 5 == 0) {
/*      */         
/*  427 */         shovel.setDamage(shovel.getDamage() + 5.0E-4F * shovel.getDamageModifier());
/*      */         
/*  429 */         VolaTile vtile = performer.getCurrentTile();
/*  430 */         String sstring = "sound.work.digging1";
/*  431 */         int x = Server.rand.nextInt(3);
/*  432 */         if (x == 0) {
/*  433 */           sstring = "sound.work.digging2";
/*  434 */         } else if (x == 1) {
/*  435 */           sstring = "sound.work.digging3";
/*  436 */         }  SoundPlayer.playSound(sstring, vtile.tilex, vtile.tiley, performer.isOnSurface(), 1.0F);
/*      */         
/*  438 */         performer.getStatus().modifyStamina(-500.0F);
/*      */       } 
/*      */ 
/*      */       
/*  442 */       if (counter * 10.0F > time)
/*      */       {
/*  444 */         VolaTile tile = performer.getCurrentTile();
/*  445 */         int t = Server.surfaceMesh.getTile(tile.tilex, tile.tiley);
/*  446 */         short h = Tiles.decodeHeight(t);
/*  447 */         int tg = Server.rockMesh.getTile(tile.tilex, tile.tiley);
/*  448 */         if (Tiles.decodeHeight(tg) > h - 3) {
/*      */           
/*  450 */           performer.getCommunicator().sendNormalServerMessage("The rock is too shallow to bury anything in.");
/*  451 */           return true;
/*      */         } 
/*      */         
/*  454 */         Skills skills = performer.getSkills();
/*      */         
/*      */         try {
/*  457 */           dig = skills.getSkill(1009);
/*      */         }
/*  459 */         catch (NoSuchSkillException nss) {
/*      */           
/*  461 */           dig = skills.learn(1009, 1.0F);
/*      */         } 
/*  463 */         dig.skillCheck(corpse.getCurrentQualityLevel(), 0.0D, false, counter);
/*  464 */         if (template.isHuman()) {
/*      */           
/*  466 */           if (performer.getDeity() != null)
/*      */           {
/*  468 */             if (performer.getDeity().isAllowsButchering())
/*      */             {
/*  470 */               if (Server.rand.nextInt(100) > performer.getFaith() - 10.0F) {
/*      */                 
/*  472 */                 performer.getCommunicator().sendNormalServerMessage(
/*  473 */                     (performer.getDeity()).name + " noticed you and is outraged at your behaviour!");
/*      */                 
/*  475 */                 performer.modifyFaith(-0.25F);
/*      */                 
/*      */                 try {
/*  478 */                   performer.setFavor(performer.getFavor() - 10.0F);
/*      */                 }
/*  480 */                 catch (IOException iox) {
/*      */                   
/*  482 */                   logger.log(Level.WARNING, "Problem setting the Favor of " + performer.getName() + " after burying a human corpse " + iox
/*  483 */                       .getMessage(), iox);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           }
/*  488 */           if (Servers.localServer.PVPSERVER) {
/*  489 */             performer.maybeModifyAlignment(1.0F);
/*      */           } else {
/*  491 */             performer.maybeModifyAlignment(2.0F);
/*      */           } 
/*      */         } 
/*      */         try {
/*  495 */           Item newGravestone = ItemFactory.createItem(822, gravestone.getQualityLevel(), gravestone
/*  496 */               .getRarity(), gravestone.getCreatorName());
/*  497 */           newGravestone.setPos(corpse.getPosX(), corpse.getPosY(), corpse.getPosZ(), corpse.getRotation(), corpse.getBridgeId());
/*      */           
/*  499 */           Zone z = Zones.getZone((int)corpse.getPosX() >> 2, (int)corpse.getPosY() >> 2, corpse.isOnSurface());
/*  500 */           z.addItem(newGravestone);
/*  501 */           String name = corpse.getName().replace("corpse of ", "");
/*  502 */           newGravestone.setDescription(name);
/*  503 */           newGravestone.setLastOwnerId(performer.getWurmId());
/*      */ 
/*      */ 
/*      */           
/*  507 */           if (!template.isHuman() && !template.isUnique() && action == 707)
/*      */           {
/*      */             
/*  510 */             for (Item i : corpse.getAllItems(false))
/*      */             {
/*  512 */               Items.destroyItem(i.getWurmId());
/*      */             }
/*      */           }
/*  515 */           Items.destroyItem(corpse.getWurmId());
/*  516 */           Items.destroyItem(gravestone.getWurmId());
/*  517 */           performer.getCommunicator().sendNormalServerMessage("You bury the " + corpse.getName() + ".");
/*  518 */           Server.getInstance().broadCastAction(performer
/*  519 */               .getName() + " buries the " + corpse.getName() + ".", performer, 5);
/*      */           
/*  521 */           performer.achievement(101);
/*      */         }
/*  523 */         catch (NoSuchZoneException nsz) {
/*      */           
/*  525 */           logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*      */         }
/*  527 */         catch (FailedException fe) {
/*      */           
/*  529 */           logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */         }
/*  531 */         catch (NoSuchTemplateException nst) {
/*      */           
/*  533 */           logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */         } 
/*  535 */         return true;
/*      */       }
/*      */     
/*  538 */     } catch (NoSuchCreatureTemplateException nst) {
/*      */       
/*  540 */       logger.log(Level.WARNING, performer.getName() + " had a problem burying " + corpse + ", source item: " + gravestone + ": " + nst
/*  541 */           .getMessage(), (Throwable)nst);
/*  542 */       return true;
/*      */     } 
/*  544 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean bury(Action act, Creature performer, Item source, Item corpse, float counter, short action) {
/*  550 */     boolean done = false;
/*  551 */     Skill dig = null;
/*  552 */     int buryskill = 1009;
/*  553 */     int time = 1000;
/*      */     
/*      */     try {
/*  556 */       CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(corpse.getData1());
/*      */       
/*  558 */       if (template.isUnique()) {
/*      */         
/*  560 */         performer.getCommunicator().sendNormalServerMessage("The " + corpse
/*  561 */             .getName() + " is too large to be buried.");
/*  562 */         return true;
/*      */       } 
/*  564 */       if (corpse.getParentId() != -10L && corpse.getNumItemsNotCoins() > 0) {
/*      */         
/*      */         try {
/*      */           
/*  568 */           Item parent = Items.getItem(corpse.getParentId());
/*  569 */           if (parent.getNumItemsNotCoins() >= 100)
/*      */           {
/*  571 */             performer.getCommunicator().sendNormalServerMessage("The " + parent
/*  572 */                 .getName() + " is full so you need to bury the corpse from the ground.");
/*  573 */             return true;
/*      */           }
/*      */         
/*  576 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  581 */       if (corpse.getParentOrNull() != null && corpse.getParentOrNull().getTemplate().hasViewableSubItems() && (
/*  582 */         !corpse.getParentOrNull().getTemplate().isContainerWithSubItems() || corpse.isPlacedOnParent())) {
/*      */         
/*  584 */         performer.getCommunicator().sendNormalServerMessage("The " + corpse.getName() + " cannot be buried from there.");
/*  585 */         return true;
/*      */       } 
/*      */       
/*  588 */       if (counter == 1.0F) {
/*      */         
/*  590 */         if (!performer.isOnSurface()) {
/*      */           
/*  592 */           if (source.getTemplateId() != 20) {
/*      */             
/*  594 */             performer.getCommunicator().sendNormalServerMessage("The ground is too hard to bury anything in using a shovel, try using a pickaxe.");
/*  595 */             return true;
/*      */           } 
/*  597 */           buryskill = 1008;
/*      */         }
/*      */         else {
/*      */           
/*  601 */           VolaTile tile = performer.getCurrentTile();
/*  602 */           int t = Server.surfaceMesh.getTile(tile.tilex, tile.tiley);
/*  603 */           byte type = Tiles.decodeType(t);
/*  604 */           if (type == Tiles.Tile.TILE_ROCK.id || type == Tiles.Tile.TILE_CLIFF.id) {
/*      */             
/*  606 */             if (source.getTemplateId() != 20) {
/*      */               
/*  608 */               performer.getCommunicator().sendNormalServerMessage("The ground is too hard to bury anything in using a shovel, try using a pickaxe.");
/*  609 */               return true;
/*      */             } 
/*  611 */             buryskill = 1008;
/*      */ 
/*      */           
/*      */           }
/*  615 */           else if (source.getTemplateId() != 25) {
/*      */             
/*  617 */             performer.getCommunicator().sendNormalServerMessage("Try using a shovel to bury this corpse.");
/*  618 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/*  622 */         if (template.isHuman())
/*      */         {
/*  624 */           if (performer.getDeity() != null && performer.getDeity().isAllowsButchering()) {
/*      */             
/*  626 */             if (performer.faithful) {
/*      */               
/*  628 */               performer.getCommunicator().sendNormalServerMessage(
/*  629 */                   (performer.getDeity()).name + " wants corpses to rot in the open.");
/*  630 */               return true;
/*      */             } 
/*      */ 
/*      */             
/*  634 */             performer.maybeModifyAlignment(1.0F);
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*  639 */         Skills skills = performer.getSkills();
/*  640 */         dig = skills.getSkillOrLearn(buryskill);
/*  641 */         time = Actions.getStandardActionTime(performer, dig, source, 0.0D);
/*  642 */         act.setTimeLeft(time);
/*  643 */         performer.sendActionControl(Actions.actionEntrys[119].getVerbString(), true, time);
/*  644 */         performer.getCommunicator().sendNormalServerMessage("You start to bury the " + corpse.getName() + ".");
/*  645 */         Server.getInstance().broadCastAction(performer.getName() + " starts to bury the " + corpse.getName() + ".", performer, 5);
/*      */       }
/*      */       else {
/*      */         
/*  649 */         time = act.getTimeLeft();
/*  650 */       }  if (act.currentSecond() % 5 == 0) {
/*      */         
/*  652 */         source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */         
/*  654 */         VolaTile vtile = performer.getCurrentTile();
/*  655 */         String sstring = "sound.work.digging1";
/*  656 */         int x = Server.rand.nextInt(3);
/*  657 */         if (x == 0) {
/*  658 */           sstring = "sound.work.digging2";
/*  659 */         } else if (x == 1) {
/*  660 */           sstring = "sound.work.digging3";
/*  661 */         }  SoundPlayer.playSound(sstring, vtile.tilex, vtile.tiley, performer.isOnSurface(), 1.0F);
/*      */         
/*  663 */         performer.getStatus().modifyStamina(-500.0F);
/*      */       } 
/*  665 */       if (counter * 10.0F > time) {
/*      */         byte type; int encodedtype;
/*  667 */         VolaTile tile = performer.getCurrentTile();
/*      */ 
/*      */         
/*  670 */         if (!performer.isOnSurface()) {
/*      */           
/*  672 */           encodedtype = Server.caveMesh.getTile(tile.tilex, tile.tiley);
/*  673 */           type = Tiles.decodeType(encodedtype);
/*  674 */           buryskill = 1008;
/*      */         }
/*      */         else {
/*      */           
/*  678 */           encodedtype = Server.surfaceMesh.getTile(tile.tilex, tile.tiley);
/*  679 */           type = Tiles.decodeType(encodedtype);
/*  680 */           if (type == Tiles.Tile.TILE_ROCK.id || type == Tiles.Tile.TILE_CLIFF.id)
/*  681 */             buryskill = 1008; 
/*      */         } 
/*  683 */         Skills skills = performer.getSkills();
/*  684 */         dig = skills.getSkillOrLearn(buryskill);
/*  685 */         dig.skillCheck(corpse.getCurrentQualityLevel(), 0.0D, false, counter);
/*  686 */         done = true;
/*  687 */         if (template.isHuman()) {
/*      */           
/*  689 */           if (performer.getDeity() != null)
/*      */           {
/*  691 */             if (performer.getDeity().isAllowsButchering())
/*      */             {
/*      */ 
/*      */               
/*  695 */               if (Server.rand.nextInt(100) > performer.getFaith() - 10.0F) {
/*      */                 
/*  697 */                 performer.getCommunicator().sendNormalServerMessage(
/*  698 */                     (performer.getDeity()).name + " noticed you and is outraged at your behaviour!");
/*      */                 
/*  700 */                 performer.modifyFaith(-0.25F);
/*      */                 
/*      */                 try {
/*  703 */                   performer.setFavor(performer.getFavor() - 10.0F);
/*      */                 }
/*  705 */                 catch (IOException iox) {
/*      */                   
/*  707 */                   logger.log(Level.WARNING, "Problem setting the Favor of " + performer.getName() + " after burying a human corpse " + iox
/*  708 */                       .getMessage(), iox);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           }
/*  713 */           if (Servers.localServer.PVPSERVER) {
/*  714 */             performer.maybeModifyAlignment(1.0F);
/*      */           } else {
/*  716 */             performer.maybeModifyAlignment(2.0F);
/*      */           } 
/*      */         } 
/*  719 */         if (!template.isHuman() && !template.isUnique() && action == 707)
/*      */         {
/*      */           
/*  722 */           for (Item i : corpse.getAllItems(false))
/*      */           {
/*  724 */             Items.destroyItem(i.getWurmId());
/*      */           }
/*      */         }
/*  727 */         Items.destroyItem(corpse.getWurmId());
/*      */         
/*  729 */         if (!performer.isOnSurface()) {
/*      */           
/*  731 */           float h = Tiles.decodeHeight(encodedtype);
/*  732 */           if (h < 0.0F) {
/*  733 */             performer.getCommunicator().sendNormalServerMessage("You mine some rocks, attach them to the corpse and watch as the " + corpse
/*  734 */                 .getName() + " sinks into the depths.");
/*      */           } else {
/*  736 */             performer.getCommunicator().sendNormalServerMessage("You mine some rocks and use them to bury the " + corpse.getName() + ".");
/*      */           } 
/*      */         } else {
/*      */           
/*  740 */           short h = Tiles.decodeHeight(encodedtype);
/*  741 */           if (type == Tiles.Tile.TILE_ROCK.id || type == Tiles.Tile.TILE_CLIFF.id) {
/*      */             
/*  743 */             if (h < 0) {
/*  744 */               performer.getCommunicator().sendNormalServerMessage("You mine some rocks, attach them to the corpse and watch as the " + corpse
/*  745 */                   .getName() + " slowly sinks into the depths.");
/*      */             } else {
/*  747 */               performer.getCommunicator().sendNormalServerMessage("You mine some rocks and use them to bury the " + corpse.getName() + ".");
/*      */             }
/*      */           
/*      */           }
/*  751 */           else if (h < 0) {
/*  752 */             performer.getCommunicator().sendNormalServerMessage("You find some rocks, attach them to the corpse and watch as the " + corpse
/*  753 */                 .getName() + " slowly sinks into the depths.");
/*      */           } else {
/*  755 */             performer.getCommunicator().sendNormalServerMessage("You bury the " + corpse.getName() + ".");
/*      */           } 
/*      */         } 
/*  758 */         Server.getInstance().broadCastAction(performer.getName() + " buries the " + corpse.getName() + ".", performer, 5);
/*      */         
/*  760 */         performer.achievement(101);
/*  761 */         performer.checkCoinAward(100);
/*      */       }
/*      */     
/*  764 */     } catch (NoSuchCreatureTemplateException nst) {
/*      */       
/*  766 */       logger.log(Level.WARNING, performer.getName() + " had a problem burying " + corpse + ", source item: " + source + ": " + nst
/*  767 */           .getMessage(), (Throwable)nst);
/*  768 */       done = true;
/*      */     } 
/*  770 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean butcher(Creature performer, Item source, Item corpse, float counter) {
/*  775 */     boolean done = false;
/*  776 */     Skill butcher = null;
/*      */     
/*  778 */     if (corpse.getOwnerId() != -10L && corpse.getOwnerId() != performer.getWurmId()) {
/*      */       
/*  780 */       done = true;
/*  781 */       performer.getCommunicator().sendNormalServerMessage("You can't reach the " + corpse.getName() + ".");
/*      */     } 
/*      */     
/*  784 */     if (corpse.isButchered()) {
/*      */       
/*  786 */       done = true;
/*  787 */       performer.getCommunicator().sendNormalServerMessage("The corpse is already butchered.");
/*      */     } 
/*      */ 
/*      */     
/*  791 */     if (corpse.getTopParentOrNull() != performer.getInventory())
/*      */     {
/*  793 */       if (!Methods.isActionAllowed(performer, (short)120, corpse))
/*  794 */         return true; 
/*      */     }
/*  796 */     if (!done) {
/*      */       
/*      */       try {
/*      */         
/*  800 */         int time = 1000;
/*  801 */         Action act = performer.getCurrentAction();
/*  802 */         double power = 0.0D;
/*  803 */         CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(corpse.getData1());
/*      */         
/*  805 */         if (counter == 1.0F) {
/*      */           
/*  807 */           if (template.isHuman())
/*      */           {
/*  809 */             if (performer.getDeity() != null && !performer.getDeity().isAllowsButchering() && 
/*  810 */               performer.faithful) {
/*      */               
/*  812 */               performer.getCommunicator().sendNormalServerMessage(
/*  813 */                   (performer.getDeity()).name + " does not accept that.");
/*  814 */               return true;
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*  819 */           Skills skills = performer.getSkills();
/*      */           
/*      */           try {
/*  822 */             butcher = skills.getSkill(10059);
/*      */           }
/*  824 */           catch (NoSuchSkillException nss) {
/*      */             
/*  826 */             butcher = skills.learn(10059, 1.0F);
/*      */           } 
/*  828 */           time = Actions.getStandardActionTime(performer, butcher, source, 0.0D);
/*  829 */           act.setTimeLeft(time);
/*  830 */           performer.sendActionControl(Actions.actionEntrys[120].getVerbString(), true, time);
/*      */           
/*  832 */           performer.getCommunicator().sendNormalServerMessage("You start to butcher the " + corpse.getName() + ".");
/*  833 */           Server.getInstance().broadCastAction(performer
/*  834 */               .getNameWithGenus() + " starts to butcher the " + corpse.getName() + ".", performer, 5);
/*  835 */           SoundPlayer.playSound("sound.butcherKnife", performer, 1.0F);
/*      */         } else {
/*      */           
/*  838 */           time = act.getTimeLeft();
/*  839 */         }  if (act.currentSecond() % 5 == 0) {
/*      */           
/*  841 */           source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*  842 */           SoundPlayer.playSound("sound.butcherKnife", performer, 1.0F);
/*      */         } 
/*  844 */         if (counter * 10.0F > time)
/*      */         {
/*  846 */           done = true;
/*  847 */           Skills skills = performer.getSkills();
/*      */           
/*      */           try {
/*  850 */             butcher = skills.getSkill(10059);
/*      */           }
/*  852 */           catch (NoSuchSkillException nss) {
/*      */             
/*  854 */             butcher = skills.learn(10059, 1.0F);
/*      */           } 
/*  856 */           double bonus = 0.0D;
/*  857 */           boolean dryRun = template.isHuman();
/*      */           
/*      */           try {
/*  860 */             Skill primskill = null;
/*  861 */             int primarySkill = source.getPrimarySkill();
/*      */             
/*      */             try {
/*  864 */               primskill = performer.getSkills().getSkill(primarySkill);
/*      */             }
/*  866 */             catch (Exception ex) {
/*      */               
/*  868 */               primskill = performer.getSkills().learn(primarySkill, 1.0F);
/*      */             } 
/*  870 */             bonus = primskill.skillCheck(10.0D, 0.0D, dryRun, counter);
/*      */           }
/*  872 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */           
/*  876 */           if (source.getTemplateId() != 93)
/*      */           {
/*  878 */             bonus = 0.0D;
/*      */           }
/*  880 */           int fat = corpse.getFat();
/*  881 */           if (template.isHuman()) {
/*      */             
/*  883 */             if (performer.getDeity() != null)
/*      */             {
/*  885 */               if (!performer.getDeity().isAllowsButchering())
/*      */               {
/*  887 */                 if (Server.rand.nextInt(100) > performer.getFaith() - 10.0F) {
/*      */                   
/*  889 */                   performer.getCommunicator().sendNormalServerMessage(
/*  890 */                       (performer.getDeity()).name + " noticed you and is outraged at your behaviour!");
/*      */                   
/*  892 */                   performer.modifyFaith(-0.25F);
/*      */                   
/*      */                   try {
/*  895 */                     performer.setFavor(performer.getFavor() - 10.0F);
/*      */                   }
/*  897 */                   catch (IOException iox) {
/*      */                     
/*  899 */                     logger.log(Level.WARNING, "Problem setting the Favor of " + performer.getName() + " after burying a human corpse  " + iox
/*  900 */                         .getMessage(), iox);
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */             }
/*  905 */             performer.maybeModifyAlignment(-1.0F);
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
/*      */ 
/*      */           
/*  919 */           performer.getCommunicator().sendNormalServerMessage("You butcher the " + corpse.getName() + ".");
/*  920 */           Server.getInstance().broadCastAction(performer.getNameWithGenus() + " butchers the " + corpse.getName() + ".", performer, 5);
/*      */           
/*  922 */           createResult(performer, corpse, butcher, source, Math.min(butcher.getKnowledge(0.0D), 0.0D), bonus, template, fat);
/*      */ 
/*      */ 
/*      */           
/*  926 */           corpse.setButchered();
/*      */         }
/*      */       
/*  929 */       } catch (NoSuchActionException nsa) {
/*      */         
/*  931 */         done = true;
/*  932 */         logger.log(Level.WARNING, performer.getName() + " this action doesn't exist?");
/*      */       }
/*  934 */       catch (NoSuchCreatureTemplateException ex) {
/*      */         
/*  936 */         logger.log(Level.WARNING, "Data1 (templateid) was " + corpse
/*  937 */             .getData1() + " for corpse with id " + corpse.getWurmId() + ". This is not a valid template.");
/*      */         
/*  939 */         done = true;
/*      */       } 
/*      */     }
/*  942 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createResult(Creature performer, Item corpse, Skill butcher, Item tool, double power, double bonus, CreatureTemplate creaturetemplate, int fat) {
/*      */     try {
/*  950 */       int[] itemnums = creaturetemplate.getItemsButchered();
/*  951 */       String creatureName = "";
/*  952 */       boolean dryRun = true;
/*  953 */       creatureName = creaturetemplate.getName().toLowerCase();
/*  954 */       dryRun = creaturetemplate.isHuman();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  962 */       ItemTemplate meattemplate = null;
/*  963 */       int meatType = (creaturetemplate.getTemplateId() == 95) ? 900 : 92;
/*      */       
/*      */       try {
/*  966 */         meattemplate = ItemTemplateFactory.getInstance().getTemplate(meatType);
/*      */       }
/*  968 */       catch (NoSuchTemplateException nst) {
/*      */         
/*  970 */         logger.log(Level.WARNING, "No template for meat!");
/*      */       } 
/*  972 */       boolean createMeat = false;
/*  973 */       if ((!dryRun && creaturetemplate.isNeedFood()) || creaturetemplate.isAnimal()) {
/*  974 */         createMeat = true;
/*      */       
/*      */       }
/*  977 */       else if (performer.getKingdomTemplateId() == 3) {
/*      */         
/*  979 */         if (!creaturetemplate.isNoSkillgain()) {
/*  980 */           createMeat = true;
/*      */         }
/*      */       } 
/*  983 */       if (meattemplate != null && corpse.getWeightGrams() < meattemplate.getWeightGrams())
/*  984 */         createMeat = false; 
/*  985 */       if (creaturetemplate.isKingdomGuard())
/*  986 */         createMeat = false; 
/*  987 */       int diffAdded = 0;
/*  988 */       if (tool.getTemplateId() != 93)
/*      */       {
/*  990 */         diffAdded = 1;
/*      */       }
/*  992 */       if (createMeat && meattemplate != null) {
/*      */         
/*  994 */         int max = tool.getRarity() + fat / 10;
/*  995 */         for (int i = 0; i < max; i++) {
/*      */           
/*  997 */           power = butcher.skillCheck(Server.rand.nextInt((i + 1 + diffAdded) * 3), tool, bonus, dryRun, 1.0F);
/*  998 */           if (tool.getSpellEffects() != null) {
/*      */             
/* 1000 */             float imbueEnhancement = 1.0F + tool.getSkillSpellImprovement(10059) / 100.0F;
/* 1001 */             power *= (tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED) * imbueEnhancement);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1006 */           if ((power > 0.0D && tool.getTemplateId() == 93) || i == 0) {
/*      */             
/*      */             try {
/*      */               
/* 1010 */               Item toCreate = ItemFactory.createItem(meatType, 
/*      */                   
/* 1012 */                   Math.min((float)Math.max((1.0F + Server.rand.nextFloat() * (10 + tool.getRarity() * 5)), 
/* 1013 */                       Math.min(100.0D, power)), corpse
/* 1014 */                     .getCurrentQualityLevel()), null);
/* 1015 */               toCreate.setData2(corpse.getData1());
/*      */               
/* 1017 */               toCreate.setMaterial(creaturetemplate.getMeatMaterial());
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1022 */               toCreate.setWeight(
/* 1023 */                   (int)Math.min(corpse.getWeightGrams() * 0.5F, (meattemplate.getWeightGrams() * creaturetemplate
/* 1024 */                     .getSize())), true);
/* 1025 */               if (toCreate.getWeightGrams() != 0)
/*      */               {
/* 1027 */                 corpse.insertItem(toCreate, true);
/* 1028 */                 performer.getCommunicator().sendNormalServerMessage("You produce " + toCreate
/* 1029 */                     .getNameWithGenus() + ".");
/*      */               }
/*      */             
/* 1032 */             } catch (NoSuchTemplateException nst) {
/*      */               
/* 1034 */               logger.log(Level.WARNING, "No template for meat!");
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/* 1039 */       for (int x = 0; x < itemnums.length; x++) {
/*      */         
/* 1041 */         if (!createMeat || (itemnums[x] != 92 && itemnums[x] != 900)) {
/*      */           
/*      */           try {
/*      */             
/* 1045 */             meattemplate = ItemTemplateFactory.getInstance().getTemplate(itemnums[x]);
/* 1046 */             power = butcher.skillCheck(Server.rand.nextInt((x + 1 + diffAdded) * 10), tool, 0.0D, dryRun, 1.0F);
/* 1047 */             if (tool.getSpellEffects() != null) {
/*      */ 
/*      */               
/* 1050 */               float imbueEnhancement = 1.0F + tool.getSkillSpellImprovement(10059) / 100.0F;
/* 1051 */               power *= (tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED) * imbueEnhancement);
/*      */             } 
/*      */             
/* 1054 */             if (power > 0.0D) {
/*      */               
/* 1056 */               Item toCreate = ItemFactory.createItem(itemnums[x], 
/*      */                   
/* 1058 */                   Math.min((float)power + Server.rand.nextFloat() * tool.getRarity() * 5.0F, corpse
/* 1059 */                     .getCurrentQualityLevel()), null);
/*      */ 
/*      */               
/* 1062 */               toCreate.setData2(corpse.getData1());
/* 1063 */               if (toCreate.getTemplateId() != 683) {
/*      */                 
/* 1065 */                 if (!toCreate.getName().contains(creatureName))
/* 1066 */                   toCreate.setName(creatureName.toLowerCase() + " " + meattemplate.getName()); 
/* 1067 */                 int modWeight = meattemplate.getWeightGrams() * creaturetemplate.getSize();
/* 1068 */                 toCreate.setWeight((int)Math.min(corpse.getWeightGrams() * 0.5F, modWeight), true);
/* 1069 */                 if (toCreate.getTemplateId() == 867)
/*      */                 {
/* 1071 */                   if (Server.rand.nextInt(250) == 0) {
/* 1072 */                     toCreate.setRarity((byte)3);
/* 1073 */                   } else if (Server.rand.nextInt(50) == 0) {
/* 1074 */                     toCreate.setRarity((byte)2);
/*      */                   } else {
/* 1076 */                     toCreate.setRarity((byte)1);
/*      */                   }  } 
/*      */               } 
/* 1079 */               if (toCreate.getWeightGrams() != 0) {
/*      */                 
/* 1081 */                 toCreate.setLastOwnerId(performer.getWurmId());
/* 1082 */                 corpse.insertItem(toCreate, true);
/* 1083 */                 performer.getCommunicator().sendNormalServerMessage("You produce " + toCreate
/* 1084 */                     .getNameWithGenus() + ".");
/*      */               } 
/*      */             } else {
/*      */               
/* 1088 */               performer.getCommunicator().sendNormalServerMessage("You fail to produce " + meattemplate
/* 1089 */                   .getNameWithGenus() + ".");
/*      */             } 
/* 1091 */           } catch (NoSuchTemplateException nst) {
/*      */             
/* 1093 */             logger.log(Level.WARNING, "No template for item id " + itemnums[x]);
/*      */           } 
/*      */         }
/*      */       } 
/* 1097 */       if (creaturetemplate.isFromValrei) {
/*      */         
/*      */         try {
/*      */           
/* 1101 */           if (power > 0.0D)
/*      */           {
/* 1103 */             int chanceModifier = 1;
/*      */             
/* 1105 */             if (Server.rand.nextInt(30 * chanceModifier) == 0) {
/*      */               
/* 1107 */               Item seryll = ItemFactory.createItem(837, 
/* 1108 */                   Math.min((float)power + Server.rand.nextFloat() * tool.getRarity() * 5.0F, 70.0F + Server.rand
/* 1109 */                     .nextFloat() * 5.0F), null);
/*      */               
/* 1111 */               seryll.setLastOwnerId(performer.getWurmId());
/* 1112 */               corpse.insertItem(seryll, true);
/* 1113 */               performer.getCommunicator().sendNormalServerMessage("You manage to extract some seryll from the cranium.");
/*      */             } 
/*      */             
/* 1116 */             if (Server.rand.nextInt(60 * chanceModifier) == 0)
/*      */             {
/* 1118 */               int num = 871 + Server.rand.nextInt(14);
/* 1119 */               Item potion = ItemFactory.createItem(num, 
/* 1120 */                   Math.min((float)power + Server.rand.nextFloat() * tool.getRarity() * 5.0F, 70.0F + Server.rand
/* 1121 */                     .nextFloat() * 5.0F), null);
/*      */               
/* 1123 */               potion.setLastOwnerId(performer.getWurmId());
/* 1124 */               corpse.insertItem(potion, true);
/* 1125 */               performer.getCommunicator().sendNormalServerMessage("You manage to extract some weird concoction from the liver.");
/*      */             }
/*      */           
/*      */           }
/*      */         
/* 1130 */         } catch (NoSuchTemplateException nst) {
/*      */           
/* 1132 */           logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */         }
/*      */       
/*      */       }
/* 1136 */     } catch (FailedException fe) {
/*      */       
/* 1138 */       logger.log(Level.WARNING, performer
/* 1139 */           .getName() + " had a problem with corpse: " + corpse + ", butcher skill: " + butcher + ", tool: " + tool + ", template: " + creaturetemplate + ", fatigue: " + fat + " due to " + fe
/* 1140 */           .getMessage(), (Throwable)fe);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Item getItemOfType(Item container, int templateId) {
/* 1146 */     Item[] items = container.getItemsAsArray();
/* 1147 */     Item found = null;
/* 1148 */     for (int i = 0; i < items.length; i++) {
/*      */       
/* 1150 */       if (items[i].getTemplateId() == templateId) {
/*      */         
/* 1152 */         found = items[i];
/*      */         break;
/*      */       } 
/* 1155 */       if (items[i].isHollow()) {
/*      */         
/* 1157 */         found = getItemOfType(items[i], templateId);
/* 1158 */         if (found != null)
/*      */           break; 
/*      */       } 
/*      */     } 
/* 1162 */     return found;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\CorpseBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */