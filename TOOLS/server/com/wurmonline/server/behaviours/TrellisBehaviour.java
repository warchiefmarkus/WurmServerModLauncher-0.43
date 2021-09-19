/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.FoliageAge;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.WurmHarvestables;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.items.RuneUtilities;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
/*     */ import com.wurmonline.server.utils.logging.TileEvent;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ public class TrellisBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  60 */   private static final Logger logger = Logger.getLogger(TrellisBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   public TrellisBehaviour() {
/*  64 */     super((short)58);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  70 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  71 */     toReturn.addAll(getBehavioursForTrellis(performer, null, target));
/*  72 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  78 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  79 */     toReturn.addAll(getBehavioursForTrellis(performer, source, target));
/*  80 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  86 */     boolean[] ans = trellisAction(act, performer, null, target, action, counter);
/*  87 */     if (ans[0])
/*  88 */       return ans[1]; 
/*  89 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  95 */     boolean[] ans = trellisAction(act, performer, source, target, action, counter);
/*  96 */     if (ans[0])
/*  97 */       return ans[1]; 
/*  98 */     return super.action(act, performer, source, target, action, counter);
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
/*     */   private List<ActionEntry> getBehavioursForTrellis(Creature performer, @Nullable Item source, Item trellis) {
/* 111 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 113 */     if (trellis.isHarvestable() && source != null && source.getTemplateId() == 267)
/*     */     {
/* 115 */       if (Methods.isActionAllowed(performer, (short)152, trellis))
/* 116 */         toReturn.add(Actions.actionEntrys[152]); 
/*     */     }
/* 118 */     List<ActionEntry> nature = new LinkedList<>();
/* 119 */     FoliageAge age = FoliageAge.getFoliageAge(trellis.getAuxData());
/*     */     
/* 121 */     if (performer.getPower() >= 2 && source != null && source.getTemplateId() == 176)
/*     */     {
/* 123 */       nature.add(Actions.actionEntrys[188]);
/*     */     }
/*     */     
/* 126 */     if (age.isPrunable())
/*     */     {
/* 128 */       nature.add(Actions.actionEntrys[373]);
/*     */     }
/* 130 */     if (TileTreeBehaviour.isSproutingAge(age.getAgeId()))
/*     */     {
/* 132 */       nature.add(Actions.actionEntrys[187]);
/*     */     }
/*     */     
/* 135 */     if (hasFruit(performer, trellis)) {
/* 136 */       nature.add(new ActionEntry((short)852, "Study", "making notes"));
/*     */     }
/* 138 */     if (nature.size() > 0) {
/*     */       
/* 140 */       Collections.sort(nature);
/* 141 */       toReturn.add(new ActionEntry((short)-nature.size(), "Nature", "nature", emptyIntArr));
/* 142 */       toReturn.addAll(nature);
/*     */     } 
/*     */     
/* 145 */     return toReturn;
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
/*     */   public boolean[] trellisAction(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter) {
/* 161 */     if (action == 152 && source.getTemplateId() == 267 && target.isPlanted() && (target
/* 162 */       .isHarvestable() || counter > 1.0F))
/*     */     {
/* 164 */       return new boolean[] { true, harvest(act, performer, source, target, counter) };
/*     */     }
/* 166 */     if (action == 188 && performer.getPower() >= 2 && source != null && source
/* 167 */       .getTemplateId() == 176) {
/*     */       
/* 169 */       target.setLeftAuxData(target.getLeftAuxData() + 1);
/*     */       
/* 171 */       target.updateName();
/* 172 */       return new boolean[] { true, true };
/*     */     } 
/* 174 */     if (action == 373 && source != null)
/*     */     {
/* 176 */       return new boolean[] { true, prune(act, performer, source, target, counter) };
/*     */     }
/* 178 */     if (action == 187 && source != null && source.getTemplateId() == 267)
/*     */     {
/* 180 */       return new boolean[] { true, pickSprout(performer, source, target, counter, act) };
/*     */     }
/* 182 */     if (action == 852 && hasFruit(performer, target))
/*     */     {
/* 184 */       return new boolean[] { true, study(act, performer, target, action, counter) };
/*     */     }
/* 186 */     if (action == 1) {
/*     */       
/* 188 */       super.action(act, performer, target, action, counter);
/* 189 */       examine(performer, target);
/* 190 */       return new boolean[] { true, true };
/*     */     } 
/* 192 */     return new boolean[] { false, false };
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
/*     */   private boolean harvest(Action act, Creature performer, Item tool, Item target, float counter) {
/* 207 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*     */       
/* 209 */       performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put what you harvest.");
/*     */       
/* 211 */       return true;
/*     */     } 
/* 213 */     if (!Methods.isActionAllowed(performer, act.getNumber(), target))
/*     */     {
/* 215 */       return true;
/*     */     }
/* 217 */     int templateId = target.getTemplate().getHarvestsTo();
/*     */     
/* 219 */     if (templateId == 411)
/*     */     {
/* 221 */       if (target.getTileY() > Zones.worldTileSizeY / 2) {
/* 222 */         templateId = 411;
/*     */       } else {
/* 224 */         templateId = 414;
/*     */       }  } 
/* 226 */     boolean toReturn = false;
/* 227 */     int time = 150;
/* 228 */     Skill skill = performer.getSkills().getSkillOrLearn(10045);
/* 229 */     time = Actions.getStandardActionTime(performer, skill, tool, 0.0D);
/*     */     
/* 231 */     if (counter == 1.0F) {
/*     */       
/* 233 */       int maxSearches = calcTrellisMaxHarvest(target, skill.getKnowledge(0.0D), tool);
/* 234 */       time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 235 */       act.setNextTick(time);
/* 236 */       act.setTickCount(1);
/* 237 */       act.setData(0L);
/* 238 */       float totalTime = (time * maxSearches);
/* 239 */       act.setTimeLeft((int)totalTime);
/*     */       
/* 241 */       performer.getCommunicator().sendNormalServerMessage("You start to harvest the " + target.getName() + ".");
/* 242 */       Server.getInstance().broadCastAction(performer.getName() + " starts to harvest a trellis.", performer, 5);
/* 243 */       performer.sendActionControl(Actions.actionEntrys[152].getVerbString(), true, (int)totalTime);
/* 244 */       performer.getStatus().modifyStamina(-500.0F);
/*     */     } else {
/*     */       
/* 247 */       time = act.getTimeLeft();
/*     */     } 
/* 249 */     if (tool != null && act.justTickedSecond()) {
/* 250 */       tool.setDamage(tool.getDamage() + 3.0E-4F * tool.getDamageModifier());
/*     */     }
/* 252 */     if (counter * 10.0F >= act.getNextTick()) {
/*     */       
/* 254 */       if (act.getRarity() != 0) {
/* 255 */         performer.playPersonalSound("sound.fx.drumroll");
/*     */       }
/* 257 */       int searchCount = act.getTickCount();
/* 258 */       int maxSearches = calcTrellisMaxHarvest(target, skill.getKnowledge(0.0D), tool);
/* 259 */       act.incTickCount();
/* 260 */       act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 261 */       int knowledge = (int)skill.getKnowledge(0.0D);
/*     */       
/* 263 */       performer.getStatus().modifyStamina((-1500 * searchCount));
/*     */ 
/*     */       
/* 266 */       if (searchCount >= maxSearches) {
/* 267 */         toReturn = true;
/*     */       }
/* 269 */       act.setData(act.getData() + 1L);
/*     */       
/* 271 */       double power = skill.skillCheck(skill.getKnowledge(0.0D) - 5.0D, tool, 0.0D, false, counter / searchCount);
/*     */ 
/*     */       
/*     */       try {
/* 275 */         float ql = knowledge + (100 - knowledge) * (float)power / 500.0F;
/* 276 */         float modifier = 1.0F;
/* 277 */         if (tool.getSpellEffects() != null)
/*     */         {
/* 279 */           modifier = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*     */         }
/* 281 */         target.setLastMaintained(WurmCalendar.currentTime);
/* 282 */         ql = Math.min(100.0F, (ql + tool.getRarity()) * modifier);
/* 283 */         Item harvested = ItemFactory.createItem(templateId, Math.max(1.0F, ql), act.getRarity(), null);
/* 284 */         if (ql < 0.0F) {
/* 285 */           harvested.setDamage(-ql / 2.0F);
/*     */         }
/* 287 */         performer.getInventory().insertItem(harvested);
/* 288 */         SoundPlayer.playSound("sound.forest.branchsnap", target.getTileX(), target.getTileY(), true, 3.0F);
/*     */         
/* 290 */         if (searchCount == 1) {
/* 291 */           target.setHarvestable(false);
/*     */         }
/* 293 */         performer.getCommunicator().sendNormalServerMessage("You harvest " + harvested
/* 294 */             .getNameWithGenus() + " from the " + target.getName() + ".");
/* 295 */         Server.getInstance().broadCastAction(performer
/* 296 */             .getName() + " harvests " + harvested.getNameWithGenus() + " from a trellis.", performer, 5);
/*     */         
/* 298 */         if (searchCount < maxSearches && !performer.getInventory().mayCreatureInsertItem())
/*     */         {
/* 300 */           performer.getCommunicator().sendNormalServerMessage("Your inventory is now full. You would have no space to put whatever you find.");
/*     */           
/* 302 */           toReturn = true;
/*     */         }
/*     */       
/* 305 */       } catch (NoSuchTemplateException nst) {
/*     */         
/* 307 */         logger.log(Level.WARNING, "No template for " + templateId, (Throwable)nst);
/* 308 */         performer.getCommunicator().sendNormalServerMessage("You fail to harvest. You realize something is wrong with the world.");
/*     */       
/*     */       }
/* 311 */       catch (FailedException fe) {
/*     */         
/* 313 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 314 */         performer.getCommunicator().sendNormalServerMessage("You fail to harvest. You realize something is wrong with the world.");
/*     */       } 
/*     */ 
/*     */       
/* 318 */       if (searchCount < maxSearches)
/*     */       {
/* 320 */         act.setRarity(performer.getRarity());
/*     */       }
/*     */     } 
/* 323 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void examine(Creature performer, Item target) {
/*     */     String grapetype;
/* 335 */     switch (target.getTemplateId()) {
/*     */ 
/*     */       
/*     */       case 920:
/* 339 */         grapetype = (target.getTileY() > Zones.worldTileSizeY / 2) ? "blue grapes." : "green grapes.";
/* 340 */         if (target.isHarvestable()) {
/*     */           
/* 342 */           performer.getCommunicator().sendNormalServerMessage("The trellis has some juicy " + grapetype);
/*     */           break;
/*     */         } 
/* 345 */         if (isAlmostRipe(920)) {
/*     */           
/* 347 */           performer.getCommunicator().sendNormalServerMessage("The trellis has a couple of immature " + grapetype);
/*     */           break;
/*     */         } 
/* 350 */         if (hasBeenPicked(target, 920))
/*     */         {
/* 352 */           performer.getCommunicator().sendNormalServerMessage("The trellis has no grapes left; all have been picked.");
/*     */         }
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 1018:
/* 359 */         if (target.isHarvestable()) {
/*     */           
/* 361 */           performer.getCommunicator().sendNormalServerMessage("The trellis has some beautiful flowers.");
/*     */           break;
/*     */         } 
/* 364 */         if (isAlmostRipe(1018)) {
/*     */           
/* 366 */           performer.getCommunicator().sendNormalServerMessage("The trellis has a couple of promising buds.");
/*     */           break;
/*     */         } 
/* 369 */         if (hasBeenPicked(target, 1018))
/*     */         {
/* 371 */           performer.getCommunicator().sendNormalServerMessage("The trellis has no flowers left; all have been picked.");
/*     */         }
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 1274:
/* 378 */         if (target.isHarvestable()) {
/*     */           
/* 380 */           performer.getCommunicator().sendNormalServerMessage("The trellis has some ripe hops, ready to be harvested.");
/*     */           break;
/*     */         } 
/* 383 */         if (isAlmostRipe(1274)) {
/*     */           
/* 385 */           performer.getCommunicator().sendNormalServerMessage("The trellis has a couple of immature hops.");
/*     */           break;
/*     */         } 
/* 388 */         if (hasBeenPicked(target, 1274))
/*     */         {
/* 390 */           performer.getCommunicator().sendNormalServerMessage("The trellis has no hops left; all have been picked.");
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAlmostRipe(int type) {
/* 401 */     switch (type) {
/*     */       
/*     */       case 920:
/* 404 */         return WurmHarvestables.Harvestable.GRAPE.isAlmostRipe();
/*     */       case 1018:
/* 406 */         return WurmHarvestables.Harvestable.ROSE.isAlmostRipe();
/*     */       case 1274:
/* 408 */         return WurmHarvestables.Harvestable.HOPS.isAlmostRipe();
/*     */     } 
/* 410 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasBeenPicked(Item target, int type) {
/* 416 */     switch (type) {
/*     */       
/*     */       case 920:
/* 419 */         if (!WurmHarvestables.Harvestable.GRAPE.isHarvestable())
/* 420 */           return false; 
/*     */         break;
/*     */       case 1018:
/* 423 */         if (!WurmHarvestables.Harvestable.ROSE.isHarvestable())
/* 424 */           return false; 
/*     */         break;
/*     */       case 1274:
/* 427 */         if (!WurmHarvestables.Harvestable.HOPS.isHarvestable()) {
/* 428 */           return false;
/*     */         }
/*     */         break;
/*     */     } 
/* 432 */     return !target.isHarvestable();
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean prune(Action action, Creature performer, Item sickle, Item trellis, float counter) {
/* 437 */     boolean toReturn = true;
/*     */     
/* 439 */     if (sickle.getTemplateId() == 267) {
/*     */       
/* 441 */       FoliageAge age = FoliageAge.getFoliageAge(trellis.getAuxData());
/* 442 */       String trellisName = trellis.getName().toLowerCase();
/*     */       
/* 444 */       if (!age.isPrunable()) {
/*     */         
/* 446 */         performer.getCommunicator().sendNormalServerMessage("It does not make sense to prune now.");
/* 447 */         return true;
/*     */       } 
/* 449 */       toReturn = false;
/* 450 */       int time = 150;
/* 451 */       Skill forestry = performer.getSkills().getSkillOrLearn(10048);
/* 452 */       Skill sickskill = performer.getSkills().getSkillOrLearn(10046);
/* 453 */       if (sickle.getTemplateId() == 267)
/*     */       {
/* 455 */         time = Actions.getStandardActionTime(performer, forestry, sickle, sickskill.getKnowledge(0.0D));
/*     */       }
/* 457 */       if (counter == 1.0F) {
/*     */         
/* 459 */         performer.getCommunicator().sendNormalServerMessage("You start to prune the " + trellisName + ".");
/* 460 */         Server.getInstance().broadCastAction(performer.getName() + " starts to prune the " + trellisName + ".", performer, 5);
/*     */         
/* 462 */         performer.sendActionControl(Actions.actionEntrys[373].getVerbString(), true, time);
/*     */       } 
/*     */       
/* 465 */       if (action.justTickedSecond())
/* 466 */         sickle.setDamage(sickle.getDamage() + 3.0E-4F * sickle.getDamageModifier()); 
/* 467 */       if (counter * 10.0F >= time)
/*     */       {
/* 469 */         double bonus = 0.0D;
/* 470 */         double power = 0.0D;
/*     */         
/* 472 */         bonus = Math.max(1.0D, sickskill.skillCheck(1.0D, sickle, 0.0D, false, counter));
/* 473 */         power = forestry.skillCheck(forestry.getKnowledge(0.0D) - 10.0D, sickle, bonus, false, counter);
/*     */         
/* 475 */         toReturn = true;
/*     */         
/* 477 */         SoundPlayer.playSound("sound.forest.branchsnap", trellis.getTileX(), trellis.getTileY(), true, 3.0F);
/* 478 */         if (power < 0.0D) {
/*     */           
/* 480 */           performer.getCommunicator().sendNormalServerMessage("You make a lot of errors and need to take a break.");
/*     */           
/* 482 */           return toReturn;
/*     */         } 
/*     */         
/* 485 */         FoliageAge newage = age.getPrunedAge();
/* 486 */         trellis.setLeftAuxData(newage.getAgeId());
/*     */         
/* 488 */         TileEvent.log(trellis.getTileX(), trellis.getTileY(), 0, performer.getWurmId(), 373);
/* 489 */         trellis.updateName();
/* 490 */         performer.getCommunicator().sendNormalServerMessage("You prune the " + trellisName + ".");
/* 491 */         Server.getInstance().broadCastAction(performer.getName() + " prunes the " + trellisName + ".", performer, 5);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 496 */       performer.getCommunicator().sendNormalServerMessage("You cannot prune with that.");
/* 497 */       logger.log(Level.WARNING, performer.getName() + " tried to prune with a " + sickle.getName());
/*     */     } 
/* 499 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean pickSprout(Creature performer, Item sickle, Item trellis, float counter, Action act) {
/* 504 */     boolean toReturn = true;
/*     */     
/* 506 */     if (sickle.getTemplateId() == 267) {
/*     */       
/* 508 */       if (!performer.getInventory().mayCreatureInsertItem()) {
/*     */         
/* 510 */         performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put the sprout.");
/*     */         
/* 512 */         return true;
/*     */       } 
/* 514 */       int age = trellis.getLeftAuxData();
/* 515 */       if (age == 7 || age == 9 || age == 11 || age == 13) {
/*     */         
/* 517 */         Skill forestry = performer.getSkills().getSkillOrLearn(10048);
/* 518 */         toReturn = false;
/* 519 */         int time = Actions.getStandardActionTime(performer, forestry, sickle, 0.0D);
/*     */         
/* 521 */         if (counter == 1.0F) {
/*     */ 
/*     */           
/*     */           try {
/* 525 */             int weight = ItemTemplateFactory.getInstance().getTemplate(266).getWeightGrams();
/* 526 */             if (!performer.canCarry(weight))
/*     */             {
/* 528 */               performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the sprout. You need to drop some things first.");
/*     */               
/* 530 */               return true;
/*     */             }
/*     */           
/* 533 */           } catch (NoSuchTemplateException nst) {
/*     */             
/* 535 */             logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 536 */             return true;
/*     */           } 
/* 538 */           performer.getCommunicator().sendNormalServerMessage("You start cutting a sprout from the trellis.");
/* 539 */           Server.getInstance().broadCastAction(performer.getName() + " starts to cut a sprout off a trellis.", performer, 5);
/*     */           
/* 541 */           performer.sendActionControl(Actions.actionEntrys[187].getVerbString(), true, time);
/*     */         } 
/*     */ 
/*     */         
/* 545 */         if (counter * 10.0F >= time) {
/*     */           
/* 547 */           if (act.getRarity() != 0)
/*     */           {
/* 549 */             performer.playPersonalSound("sound.fx.drumroll");
/*     */           }
/*     */           
/*     */           try {
/* 553 */             int weight = ItemTemplateFactory.getInstance().getTemplate(266).getWeightGrams();
/* 554 */             if (!performer.canCarry(weight))
/*     */             {
/* 556 */               performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the sprout. You need to drop some things first.");
/*     */               
/* 558 */               return true;
/*     */             }
/*     */           
/* 561 */           } catch (NoSuchTemplateException nst) {
/*     */             
/* 563 */             logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 564 */             return true;
/*     */           } 
/* 566 */           sickle.setDamage(sickle.getDamage() + 0.003F * sickle.getDamageModifier());
/* 567 */           double bonus = 0.0D;
/* 568 */           double power = 0.0D;
/* 569 */           Skill sickskill = performer.getSkills().getSkillOrLearn(10046);
/* 570 */           bonus = Math.max(1.0D, sickskill.skillCheck(1.0D, sickle, 0.0D, false, counter));
/* 571 */           power = forestry.skillCheck(1.0D, sickle, bonus, false, counter);
/*     */           
/* 573 */           toReturn = true;
/* 574 */           String templateType = "sprout";
/*     */           
/*     */           try {
/* 577 */             int template = 266;
/* 578 */             byte material = 0;
/*     */             
/* 580 */             if (trellis.getTemplateId() == 920) {
/*     */               
/* 582 */               template = 266;
/* 583 */               material = 49;
/* 584 */               templateType = "sprout";
/*     */             }
/* 586 */             else if (trellis.getTemplateId() == 1018) {
/*     */               
/* 588 */               template = 266;
/* 589 */               material = 47;
/* 590 */               templateType = "sprout";
/*     */             }
/* 592 */             else if (trellis.getTemplateId() == 919) {
/*     */               
/* 594 */               template = 917;
/* 595 */               material = 68;
/* 596 */               templateType = "seedling";
/*     */             }
/* 598 */             else if (trellis.getTemplateId() == 1274) {
/*     */               
/* 600 */               template = 1275;
/* 601 */               material = 68;
/* 602 */               templateType = "seedling";
/*     */             } 
/*     */             
/* 605 */             float modifier = 1.0F;
/* 606 */             if (sickle.getSpellEffects() != null)
/*     */             {
/* 608 */               modifier = sickle.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*     */             }
/*     */             
/* 611 */             Item sprout = ItemFactory.createItem(template, Math.max(1.0F, Math.min(100.0F, (float)power * modifier + sickle.getRarity())), material, act
/* 612 */                 .getRarity(), null);
/*     */             
/* 614 */             if (power < 0.0D) {
/* 615 */               sprout.setDamage((float)-power / 2.0F);
/*     */             }
/* 617 */             SoundPlayer.playSound("sound.forest.branchsnap", trellis.getTileX(), trellis.getTileY(), true, 2.0F);
/*     */             
/* 619 */             age--;
/*     */             
/* 621 */             performer.getInventory().insertItem(sprout);
/* 622 */             trellis.setLeftAuxData(age);
/* 623 */             trellis.updateName();
/* 624 */             performer.getCommunicator().sendNormalServerMessage("You cut a " + templateType + " from the trellis.");
/* 625 */             Server.getInstance().broadCastAction(performer.getName() + " cuts a " + templateType + " off a trellis.", performer, 5);
/*     */           }
/* 627 */           catch (NoSuchTemplateException nst) {
/*     */             
/* 629 */             logger.log(Level.WARNING, "No template for " + templateType + "!", (Throwable)nst);
/* 630 */             performer.getCommunicator().sendNormalServerMessage("You fail to pick the " + templateType + ". You realize something is wrong with the world.");
/*     */           
/*     */           }
/* 633 */           catch (FailedException fe) {
/*     */             
/* 635 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 636 */             performer.getCommunicator().sendNormalServerMessage("You fail to pick the " + templateType + ". You realize something is wrong with the world.");
/*     */           }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         
/* 642 */         performer.getCommunicator().sendNormalServerMessage("The trellis has nothing to pick.");
/*     */       } 
/*     */     } else {
/*     */       
/* 646 */       performer.getCommunicator().sendNormalServerMessage("You cannot pick sprouts with that.");
/* 647 */       logger.log(Level.WARNING, performer.getName() + " tried to pick sprout with a " + sickle.getName());
/*     */     } 
/* 649 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean study(Action act, Creature performer, Item trellis, short action, float counter) {
/* 654 */     int harvestableId = WurmHarvestables.getHarvestableIdFromTrellis(trellis.getTemplateId());
/* 655 */     WurmHarvestables.Harvestable harvestable = WurmHarvestables.getHarvestable(harvestableId);
/*     */     
/* 657 */     if (harvestable == null) {
/*     */       
/* 659 */       performer.getCommunicator().sendNormalServerMessage("You decide not to study " + trellis
/* 660 */           .getName() + " as it seems never to be harvestable.");
/* 661 */       return true;
/*     */     } 
/* 663 */     int time = 0;
/*     */     
/* 665 */     if (counter == 1.0F) {
/*     */       
/* 667 */       time = 500;
/* 668 */       performer.getCommunicator().sendNormalServerMessage("You start to study the " + trellis
/* 669 */           .getName() + ".");
/*     */       
/* 671 */       Server.getInstance().broadCastAction(performer.getName() + " starts to study the " + trellis.getName() + ".", performer, 5);
/* 672 */       performer.sendActionControl("studying " + trellis.getName(), true, time);
/* 673 */       act.setTimeLeft(time);
/* 674 */       return false;
/*     */     } 
/*     */     
/* 677 */     time = act.getTimeLeft();
/*     */     
/* 679 */     if (act.mayPlaySound()) {
/* 680 */       Methods.sendSound(performer, "sound.work.foragebotanize");
/*     */     }
/* 682 */     TileTreeBehaviour.sendStudyMessages(performer, harvestable, act.currentSecond());
/*     */     
/* 684 */     if (counter * 10.0F >= time) {
/*     */       
/* 686 */       if (performer.getPower() < 2) {
/* 687 */         trellis.setHarvestable(false);
/*     */       }
/* 689 */       ((Player)performer).setStudied(WurmHarvestables.getHarvestableIdFromTrellis(trellis.getTemplateId()));
/* 690 */       performer.getCommunicator().sendNormalServerMessage("You finish studying the " + trellis.getName() + ". You now need to record the study results.");
/*     */       
/* 692 */       Server.getInstance().broadCastAction(performer
/* 693 */           .getName() + " looks pleased with " + performer.getHisHerItsString() + " study results.", performer, 5);
/*     */       
/* 695 */       return true;
/*     */     } 
/* 697 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int calcTrellisMaxHarvest(Item trellis, double currentSkill, Item tool) {
/* 703 */     int bonus = 0;
/* 704 */     if (tool.getSpellEffects() != null) {
/*     */       
/* 706 */       float extraChance = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_FARMYIELD) - 1.0F;
/* 707 */       if (extraChance > 0.0F && Server.rand.nextFloat() < extraChance)
/*     */       {
/* 709 */         bonus++;
/*     */       }
/*     */     } 
/*     */     
/* 713 */     return Math.min((int)(trellis.getCurrentQualityLevel() + 1.0F), (int)(currentSkill + 28.0D) / 27 + bonus);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasFruit(Creature performer, Item trellis) {
/* 718 */     int age = trellis.getLeftAuxData();
/* 719 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId()) {
/*     */       
/* 721 */       if (Servers.isThisATestServer() && performer.getPower() > 1)
/* 722 */         return true; 
/* 723 */       return trellis.isHarvestable();
/*     */     } 
/* 725 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TrellisBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */