/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.combat.Weapon;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.endgames.EndGameItem;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HugeAltarBehaviour
/*     */   extends DomainItemBehaviour
/*     */ {
/*  61 */   private static final Logger logger = Logger.getLogger(HugeAltarBehaviour.class.getName());
/*     */   
/*     */   private static final float MAX_DAM = 99.9F;
/*     */   
/*     */   public static final int maxCharges = 30;
/*     */   
/*     */   HugeAltarBehaviour() {
/*  68 */     super((short)34);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  79 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  80 */     toReturn.addAll(super.getBehavioursFor(performer, target));
/*  81 */     toReturn.addAll(getCommonBehaviours(performer, target));
/*  82 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<ActionEntry> getCommonBehaviours(Creature performer, Item target) {
/*  87 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  88 */     if (EndGameItems.getEndGameItem(target) != null)
/*     */     {
/*  90 */       if (EndGameItems.getEndGameItem(target).isHoly()) {
/*     */         
/*  92 */         int nums = -3;
/*     */         
/*  94 */         Deity rand = Deities.getRandomNonHateDeity();
/*  95 */         if (rand != null)
/*  96 */           nums--; 
/*  97 */         toReturn.add(new ActionEntry((short)nums, "Inscriptions", "inscriptions"));
/*  98 */         toReturn.add(Actions.actionEntrys[217]);
/*  99 */         toReturn.add(Actions.actionEntrys[218]);
/* 100 */         toReturn.add(Actions.actionEntrys[219]);
/* 101 */         if (rand != null) {
/* 102 */           toReturn.add(Actions.actionEntrys[482]);
/*     */         }
/* 104 */         if (performer.getDeity() != null && !performer.getDeity().isHateGod() && performer.getFaith() == 30.0F && 
/* 105 */           !performer.isPriest())
/*     */         {
/* 107 */           toReturn.add(Actions.actionEntrys[286]);
/*     */         }
/* 109 */         if (!performer.isChampion())
/*     */         {
/* 111 */           if (!Servers.localServer.isChallengeServer())
/*     */           {
/* 113 */             if (performer.getDeity() != null && !performer.getDeity().isHateGod() && performer.getFaith() >= 50.0F)
/*     */             {
/* 115 */               if (performer.getDeity().isWarrior()) {
/*     */ 
/*     */                 
/*     */                 try {
/* 119 */                   if (performer.getSkills().getSkill(105).getKnowledge(0.0D) > 25.0D) {
/* 120 */                     toReturn.add(Actions.actionEntrys[220]);
/*     */                   }
/* 122 */                 } catch (NoSuchSkillException nss) {
/*     */                   
/* 124 */                   logger.log(Level.WARNING, "Weird - " + performer.getName() + " has no soul strength.");
/* 125 */                   performer.getSkills().learn(105, 1.0F);
/*     */                 } 
/*     */               } else {
/*     */ 
/*     */                 
/*     */                 try {
/*     */                   
/* 132 */                   if (performer.getSkills().getSkill(106).getKnowledge(0.0D) > 25.0D) {
/* 133 */                     toReturn.add(Actions.actionEntrys[220]);
/*     */                   }
/* 135 */                 } catch (NoSuchSkillException nss) {
/*     */                   
/* 137 */                   logger.log(Level.WARNING, "Weird - " + performer.getName() + " has no soul depth.");
/* 138 */                   performer.getSkills().learn(106, 1.0F);
/*     */                 }
/*     */               
/*     */               } 
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/*     */         
/* 147 */         int nums = -1;
/* 148 */         Deity rand = Deities.getRandomHateDeity();
/* 149 */         if (rand != null)
/* 150 */           nums--; 
/* 151 */         toReturn.add(new ActionEntry((short)nums, "Inscriptions", "inscriptions"));
/* 152 */         toReturn.add(Actions.actionEntrys[217]);
/* 153 */         if (rand != null) {
/* 154 */           toReturn.add(Actions.actionEntrys[482]);
/*     */         }
/* 156 */         if (performer.getDeity() != null && performer.getDeity().isHateGod() && performer.getFaith() == 30.0F && 
/* 157 */           !performer.isPriest())
/*     */         {
/* 159 */           toReturn.add(Actions.actionEntrys[286]);
/*     */         }
/* 161 */         if (!performer.isChampion())
/*     */         {
/* 163 */           if (!Servers.localServer.isChallengeServer())
/*     */           {
/* 165 */             if (performer.getDeity() != null && performer.getDeity().isHateGod() && performer.getFaith() >= 50.0F) {
/*     */               
/*     */               try {
/*     */                 
/* 169 */                 if (performer.getSkills().getSkill(105).getKnowledge(0.0D) > 25.0D) {
/* 170 */                   toReturn.add(Actions.actionEntrys[220]);
/*     */                 }
/* 172 */               } catch (NoSuchSkillException nss) {
/*     */                 
/* 174 */                 logger.log(Level.WARNING, "Weird - " + performer.getName() + " has no soul strength.");
/* 175 */                 performer.getSkills().learn(105, 1.0F);
/*     */               } 
/*     */             }
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/* 182 */     return toReturn;
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
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/* 194 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/* 195 */     toReturn.addAll(getCommonBehaviours(performer, target));
/*     */ 
/*     */     
/* 198 */     if (EndGameItems.getEndGameItem(target) != null)
/*     */     {
/*     */ 
/*     */       
/* 202 */       toReturn.add(Actions.actionEntrys[221]);
/*     */     }
/*     */ 
/*     */     
/* 206 */     if (source.isArtifact())
/*     */     {
/* 208 */       toReturn.add(Actions.actionEntrys[370]);
/*     */     }
/* 210 */     return toReturn;
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
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 222 */     boolean done = true;
/* 223 */     boolean reachable = true;
/* 224 */     if (target.getOwnerId() == -10L) {
/*     */       
/* 226 */       reachable = false;
/* 227 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F))
/*     */       {
/* 229 */         reachable = true;
/*     */       }
/*     */     } 
/* 232 */     if (reachable) {
/*     */       
/* 234 */       if (EndGameItems.getEndGameItem(target) != null) {
/*     */         
/* 236 */         if (EndGameItems.getEndGameItem(target).isHoly()) {
/*     */           
/* 238 */           if (action == 217) {
/* 239 */             Methods.sendAltarConversion(performer, target, Deities.getDeity(1));
/* 240 */           } else if (action == 218) {
/* 241 */             Methods.sendAltarConversion(performer, target, Deities.getDeity(2));
/* 242 */           } else if (action == 219) {
/* 243 */             Methods.sendAltarConversion(performer, target, Deities.getDeity(3));
/* 244 */           } else if (action == 482) {
/*     */             
/* 246 */             Deity rand = Deities.getRandomNonHateDeity();
/* 247 */             if (rand != null) {
/* 248 */               Methods.sendAltarConversion(performer, target, rand);
/*     */             }
/*     */           } 
/* 251 */         } else if (action == 217 || action == 482) {
/*     */           
/* 253 */           if (action == 217) {
/* 254 */             Methods.sendAltarConversion(performer, target, Deities.getDeity(4));
/*     */           } else {
/*     */             
/* 257 */             Deity rand = Deities.getRandomHateDeity();
/* 258 */             if (rand != null)
/* 259 */               Methods.sendAltarConversion(performer, target, rand); 
/*     */           } 
/*     */         } 
/* 262 */         if (action == 220) {
/*     */           
/* 264 */           if (Servers.localServer.isChallengeServer())
/*     */           {
/* 266 */             return true;
/*     */           }
/* 268 */           if (performer.isChampion()) {
/*     */             
/* 270 */             performer.getCommunicator()
/* 271 */               .sendNormalServerMessage("You are already the Champion of a deity.");
/* 272 */             return true;
/*     */           } 
/* 274 */           if (System.currentTimeMillis() - performer.getChampTimeStamp() < 14515200000L) {
/*     */ 
/*     */             
/* 277 */             String timefor = Server.getTimeFor(14515200000L + performer.getChampTimeStamp() - 
/* 278 */                 System.currentTimeMillis());
/* 279 */             performer.getCommunicator()
/* 280 */               .sendNormalServerMessage("You may become champion again in " + timefor + ".");
/* 281 */             return true;
/*     */           } 
/* 283 */           if (EndGameItems.getEndGameItem(target) != null)
/*     */           {
/* 285 */             if (EndGameItems.getEndGameItem(target).isHoly()) {
/*     */               
/* 287 */               if (performer.getDeity() != null && !performer.getDeity().isHateGod() && performer
/* 288 */                 .getFaith() >= 50.0F)
/*     */               {
/* 290 */                 if (performer.isKing()) {
/*     */                   
/* 292 */                   performer.getCommunicator().sendNormalServerMessage(performer
/* 293 */                       .getDeity().getName() + " will not let a ruler such as yourself risk " + performer
/* 294 */                       .getHisHerItsString() + " life as a champion.");
/* 295 */                   return true;
/*     */                 } 
/* 297 */                 if (performer.getDeity().isWarrior()) {
/*     */ 
/*     */                   
/*     */                   try {
/* 301 */                     if (performer.getSkills().getSkill(105).getKnowledge(0.0D) > 25.0D) {
/* 302 */                       Methods.sendRealDeathQuestion(performer, target, performer.getDeity());
/*     */                     }
/* 304 */                   } catch (NoSuchSkillException nss) {
/*     */                     
/* 306 */                     logger.log(Level.WARNING, "Weird - " + performer.getName() + " has no soul strength.");
/* 307 */                     performer.getSkills().learn(105, 1.0F);
/*     */                   } 
/*     */                 } else {
/*     */ 
/*     */                   
/*     */                   try {
/*     */                     
/* 314 */                     if (performer.getSkills().getSkill(106).getKnowledge(0.0D) > 25.0D) {
/* 315 */                       Methods.sendRealDeathQuestion(performer, target, performer.getDeity());
/*     */                     }
/* 317 */                   } catch (NoSuchSkillException nss) {
/*     */                     
/* 319 */                     logger.log(Level.WARNING, "Weird - " + performer.getName() + " has no soul depth.");
/* 320 */                     performer.getSkills().learn(106, 1.0F);
/*     */                   }
/*     */                 
/*     */                 }
/*     */               
/*     */               }
/*     */             
/* 327 */             } else if (performer.getDeity() != null && performer.getDeity().isHateGod() && performer.getFaith() >= 50.0F) {
/*     */               
/* 329 */               if (performer.isKing()) {
/*     */                 
/* 331 */                 performer.getCommunicator().sendNormalServerMessage(performer
/* 332 */                     .getDeity().getName() + " will not let a ruler such as yourself risk " + performer
/* 333 */                     .getHisHerItsString() + " life as a champion.");
/* 334 */                 return true;
/*     */               } 
/*     */               
/*     */               try {
/* 338 */                 if (performer.getSkills().getSkill(105).getKnowledge(0.0D) > 25.0D) {
/* 339 */                   Methods.sendRealDeathQuestion(performer, target, performer.getDeity());
/*     */                 }
/* 341 */               } catch (NoSuchSkillException nss) {
/*     */                 
/* 343 */                 logger.log(Level.WARNING, "Weird - " + performer.getName() + " has no soul strength.");
/* 344 */                 performer.getSkills().learn(106, 1.0F);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 350 */         } else if (action == 286) {
/*     */           
/* 352 */           if (EndGameItems.getEndGameItem(target) != null)
/*     */           {
/* 354 */             if (EndGameItems.getEndGameItem(target).isHoly())
/*     */             {
/* 356 */               if (performer.getDeity() != null && !performer.getDeity().isHateGod() && performer
/* 357 */                 .getFaith() == 30.0F)
/*     */               {
/* 359 */                 MethodsCreatures.sendAskPriestQuestion(performer, target, null);
/*     */               
/*     */               }
/*     */             
/*     */             }
/* 364 */             else if (performer.getDeity() != null && performer.getDeity().isHateGod() && performer.getFaith() == 30.0F)
/*     */             {
/* 366 */               MethodsCreatures.sendAskPriestQuestion(performer, target, null);
/*     */             }
/*     */           
/*     */           }
/*     */         } else {
/*     */           
/* 372 */           done = super.action(act, performer, target, action, counter);
/*     */         } 
/* 374 */       } else if (action == 7 && Constants.loadEndGameItems && performer.getPower() >= 5) {
/*     */         
/* 376 */         boolean holy = (target.getRealTemplateId() == 327);
/* 377 */         if ((holy && EndGameItems.getGoodAltar() != null) || (!holy && EndGameItems.getEvilAltar() != null)) {
/*     */           
/* 379 */           performer.getCommunicator().sendNormalServerMessage("You must remove the current " + target.getActualName() + " before placing a new one.");
/* 380 */           return done;
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 385 */           target.putItemInfrontof(performer);
/*     */           
/* 387 */           EndGameItem eg = new EndGameItem(target, holy, (short)68, true);
/* 388 */           EndGameItems.altars.put(new Long(eg.getWurmid()), eg);
/*     */           
/* 390 */           target.bless(holy ? 1 : 4);
/* 391 */           target.enchant(holy ? 5 : 8);
/*     */           
/* 393 */           short eff = holy ? 2 : 3;
/* 394 */           for (Player lPlayer : Players.getInstance().getPlayers()) {
/* 395 */             lPlayer.getCommunicator().sendAddEffect(eg.getWurmid(), eff, eg
/* 396 */                 .getItem().getPosX(), eg.getItem().getPosY(), eg.getItem().getPosZ(), (byte)0);
/*     */           }
/* 398 */           logger.log(Level.INFO, "Created " + (holy ? "holy" : "unholy") + " altar at " + target.getPosX() + ", " + target.getPosY() + ".");
/* 399 */           performer.getCommunicator().sendNormalServerMessage("You successfully place " + target.getNameWithGenus() + " into the world.");
/*     */         }
/* 401 */         catch (NoSuchItemException nsi) {
/*     */           
/* 403 */           logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*     */         }
/* 405 */         catch (NoSuchCreatureException nsc) {
/*     */           
/* 407 */           logger.log(Level.WARNING, "Failed to locate creature " + performer.getWurmId(), (Throwable)nsc);
/*     */         }
/* 409 */         catch (NoSuchPlayerException nsp) {
/*     */           
/* 411 */           logger.log(Level.WARNING, "Failed to locate player " + performer.getWurmId(), (Throwable)nsp);
/*     */         }
/* 413 */         catch (NoSuchZoneException nsz) {
/*     */           
/* 415 */           Items.destroyItem(target.getWurmId());
/*     */         } 
/*     */       } else {
/*     */         
/* 419 */         done = super.action(act, performer, target, action, counter);
/*     */       } 
/*     */     } else {
/* 422 */       done = super.action(act, performer, target, action, counter);
/* 423 */     }  return done;
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
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 436 */     boolean done = false;
/* 437 */     boolean reachable = true;
/* 438 */     if (target.getOwnerId() == -10L) {
/*     */       
/* 440 */       reachable = false;
/* 441 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F))
/*     */       {
/* 443 */         reachable = true;
/*     */       }
/*     */     } 
/* 446 */     if (reachable) {
/*     */       
/* 448 */       if (action == 217 || action == 218 || action == 219 || action == 482 || action == 220) {
/*     */ 
/*     */         
/* 451 */         done = action(act, performer, target, action, counter);
/* 452 */       } else if (action == 221) {
/*     */         
/* 454 */         if (source.getOwnerId() != performer.getWurmId()) {
/* 455 */           return true;
/*     */         }
/*     */         
/* 458 */         if (EndGameItems.getEndGameItem(target) != null)
/*     */         {
/*     */ 
/*     */           
/* 462 */           done = destroy(act, performer, source, target, counter);
/*     */         }
/*     */         else
/*     */         {
/* 466 */           done = true;
/*     */         }
/*     */       
/* 469 */       } else if (action == 370) {
/*     */         
/* 471 */         if (source.getOwnerId() != performer.getWurmId())
/* 472 */           return true; 
/* 473 */         if (source.isArtifact()) {
/*     */           
/* 475 */           if (!Deities.mayDestroyAltars()) {
/*     */             
/* 477 */             performer
/* 478 */               .getCommunicator()
/* 479 */               .sendNormalServerMessage("The " + target
/*     */                 
/* 481 */                 .getName() + " is vigilantly protected from Valrei. The position of the moons make it possible to recharge the " + source
/*     */                 
/* 483 */                 .getName() + " on Wrath day and the day of Awakening in the first and third week of a Starfall.");
/*     */ 
/*     */             
/* 486 */             return true;
/*     */           } 
/* 488 */           if (EndGameItems.getEndGameItem(target) != null) {
/*     */             
/* 490 */             if (EndGameItems.getEndGameItem(target).isHoly()) {
/*     */               
/* 492 */               if (EndGameItems.getEndGameItem(source) == null || 
/* 493 */                 !EndGameItems.getEndGameItem(source).isHoly()) {
/*     */ 
/*     */                 
/* 496 */                 performer.getCommunicator().sendNormalServerMessage("Nothing happens. Maybe you need to try at the other altar?");
/*     */                 
/* 498 */                 return true;
/*     */               } 
/* 500 */               if (counter == 1.0F) {
/*     */                 
/* 502 */                 if (!EndGameItems.mayRechargeItem()) {
/*     */                   
/* 504 */                   performer.getCommunicator().sendNormalServerMessage("Nothing happens. You have to wait a minute between recharge attempts.");
/*     */                   
/* 506 */                   return true;
/*     */                 } 
/* 508 */                 Server.getInstance().broadCastNormal("The sky darkens as someone starts to recharge the " + source
/* 509 */                     .getName() + " at the " + target
/* 510 */                     .getName() + ".");
/* 511 */                 Players.getInstance().sendGlobalNonPersistantEffect(-10L, (short)16, target
/*     */                     
/* 513 */                     .getTileX(), target
/* 514 */                     .getTileY(), 
/* 515 */                     Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(target.getTileX(), target
/* 516 */                         .getTileY())));
/* 517 */                 EndGameItems.touchRecharge();
/*     */               } 
/*     */             } 
/*     */           } else {
/*     */             
/* 522 */             return true;
/*     */           } 
/* 524 */           if (EndGameItems.getEndGameItem(target) != null) {
/*     */             
/* 526 */             if (!EndGameItems.getEndGameItem(target).isHoly()) {
/*     */ 
/*     */               
/* 529 */               if (EndGameItems.getEndGameItem(source) == null || 
/* 530 */                 EndGameItems.getEndGameItem(source).isHoly()) {
/*     */                 
/* 532 */                 performer.getCommunicator().sendNormalServerMessage("Nothing happens. Maybe you need to try at the other altar?");
/*     */                 
/* 534 */                 return true;
/*     */               } 
/*     */               
/* 537 */               if (counter == 1.0F) {
/*     */                 
/* 539 */                 if (!EndGameItems.mayRechargeItem()) {
/*     */                   
/* 541 */                   performer.getCommunicator().sendNormalServerMessage("Nothing happens. You have to wait a minute between recharge attempts.");
/*     */                   
/* 543 */                   return true;
/*     */                 } 
/* 545 */                 Players.getInstance().sendGlobalNonPersistantEffect(-10L, (short)17, target
/*     */                     
/* 547 */                     .getTileX(), target
/* 548 */                     .getTileY(), 
/* 549 */                     Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(target.getTileX(), target
/* 550 */                         .getTileY())));
/* 551 */                 Server.getInstance().broadCastNormal("The sky gets brighter as someone starts to recharge the " + source
/* 552 */                     .getName() + " at the " + target
/* 553 */                     .getName() + ".");
/* 554 */                 EndGameItems.touchRecharge();
/*     */               } 
/*     */             } 
/*     */           } else {
/*     */             
/* 559 */             return true;
/* 560 */           }  done = false;
/* 561 */           if (counter == 1.0F) {
/*     */             
/* 563 */             performer.getCommunicator().sendNormalServerMessage("You start to recharge the " + source
/* 564 */                 .getName() + ".");
/* 565 */             Server.getInstance().broadCastAction(performer
/* 566 */                 .getName() + " starts to recharge the " + source.getName() + ".", performer, 5);
/*     */             
/* 568 */             performer.sendActionControl(Actions.actionEntrys[370].getVerbString(), true, 2400);
/*     */             
/* 570 */             performer.getStatus().modifyStamina(-500.0F);
/*     */           }
/* 572 */           else if (act.currentSecond() % 10 == 0) {
/*     */             
/* 574 */             performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " vibrates faintly.");
/* 575 */             source.setAuxData((byte)Math.min(30, source.getAuxData() + 1));
/* 576 */             EndGameItems.touchRecharge();
/*     */           }
/* 578 */           else if (counter >= 300.0F || source.getAuxData() == 30) {
/*     */             
/* 580 */             performer.getCommunicator().sendNormalServerMessage("The " + source
/* 581 */                 .getName() + " is now fully charged.");
/* 582 */             source.setAuxData((byte)30);
/* 583 */             done = true;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 588 */         done = super.action(act, performer, source, target, action, counter);
/*     */       } 
/*     */     } else {
/* 591 */       done = super.action(act, performer, source, target, action, counter);
/* 592 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean destroy(Action act, Creature performer, Item source, Item altar, float counter) {
/* 598 */     boolean done = false;
/*     */     
/* 600 */     if (Deities.mayDestroyAltars()) {
/*     */       
/* 602 */       float dam = Weapon.getBaseDamageForWeapon(source);
/* 603 */       if (dam <= 0.0F) {
/*     */         
/* 605 */         performer.getCommunicator().sendNormalServerMessage("That wouldn't leave a dent in the " + altar
/* 606 */             .getName() + ".");
/* 607 */         return true;
/*     */       } 
/* 609 */       if (counter == 1.0F) {
/*     */         
/* 611 */         performer.getCommunicator().sendNormalServerMessage("You start to destroy the " + altar.getName() + ".");
/* 612 */         Server.getInstance().broadCastAction(performer.getName() + " starts to destroy the " + altar.getName() + ".", performer, 5);
/*     */ 
/*     */         
/* 615 */         performer.sendActionControl(Actions.actionEntrys[221].getVerbString(), true, 1000);
/*     */         
/* 617 */         performer.getStatus().modifyStamina(-500.0F);
/*     */       }
/* 619 */       else if (act.currentSecond() % 10 == 0) {
/*     */         
/* 621 */         Skills skills = performer.getSkills();
/* 622 */         Skill weapon = null;
/* 623 */         Skill soul = null;
/*     */         
/*     */         try {
/* 626 */           weapon = skills.getSkill(source.getPrimarySkill());
/*     */         }
/* 628 */         catch (NoSuchSkillException nss) {
/*     */ 
/*     */           
/*     */           try {
/* 632 */             weapon = skills.learn(source.getPrimarySkill(), 1.0F);
/*     */           }
/* 634 */           catch (NoSuchSkillException ns2) {
/*     */             
/* 636 */             logger.log(Level.WARNING, performer.getName() + " using " + source.getName() + " which has no primary skill!");
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 642 */           soul = skills.getSkill(105);
/*     */         }
/* 644 */         catch (NoSuchSkillException nss) {
/*     */           
/* 646 */           soul = skills.learn(105, 1.0F);
/*     */         } 
/* 648 */         if (weapon != null && 
/* 649 */           weapon.skillCheck(80.0D, soul.getKnowledge(0.0D), true, act.currentSecond()) > 0.0D)
/*     */         {
/* 651 */           if (altar.getDamage() >= 99.9F) {
/*     */             
/* 653 */             performer.getCommunicator().sendSafeServerMessage("You destroy the " + altar.getName() + "!");
/* 654 */             EndGameItems.destroyHugeAltar(altar, performer);
/* 655 */             done = true;
/*     */           }
/*     */           else {
/*     */             
/* 659 */             if (performer.getPower() > 0 && Servers.isThisATestServer()) {
/* 660 */               altar.setDamage(Math.min(99.9F, altar.getDamage() + 5.0F));
/* 661 */             } else if (source.isDestroyHugeAltar()) {
/* 662 */               altar.setDamage(Math.min(99.9F, altar.getDamage() + 0.1F));
/*     */             } else {
/* 664 */               altar.setDamage(Math.min(99.9F, altar
/*     */                     
/* 666 */                     .getDamage() + 
/* 667 */                     (float)Weapon.getModifiedDamageForWeapon(source, performer.getBodyStrength()) / 200000.0F));
/*     */             } 
/* 669 */             performer
/* 670 */               .getCommunicator()
/* 671 */               .sendNormalServerMessage("Using your skills and drawing from the strength of your soul, you manage to dent the huge altar a little!");
/*     */           } 
/*     */         }
/*     */         
/* 675 */         performer.getStatus().modifyStamina(-500.0F);
/*     */       }
/* 677 */       else if (act.currentSecond() > 100) {
/*     */         
/* 679 */         done = true;
/*     */         
/* 681 */         if (altar.getDamage() >= 99.9F)
/*     */         {
/* 683 */           performer.getCommunicator().sendSafeServerMessage("You destroy the " + altar.getName() + "!");
/* 684 */           EndGameItems.destroyHugeAltar(altar, performer);
/*     */         }
/*     */         else
/*     */         {
/* 688 */           if (performer.getPower() > 0 && Servers.isThisATestServer()) {
/* 689 */             altar.setDamage(Math.min(99.9F, altar.getDamage() + 10.0F));
/* 690 */           } else if (source.isDestroyHugeAltar()) {
/* 691 */             altar.setDamage(Math.min(99.9F, altar.getDamage() + 0.3F));
/*     */           } else {
/* 693 */             altar.setDamage(Math.min(99.9F, altar
/*     */                   
/* 695 */                   .getDamage() + 
/* 696 */                   (float)Weapon.getModifiedDamageForWeapon(source, performer.getBodyStrength()) / 100000.0F));
/*     */           } 
/* 698 */           performer.getCommunicator().sendNormalServerMessage("You manage to damage the altar a bit.");
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 704 */       performer.getCommunicator().sendNormalServerMessage("The " + altar
/* 705 */           .getName() + " is vigilantly protected from Valrei. The position of the moons make the " + altar
/* 706 */           .getName() + " vulnerable on Wrath day and the day of Awakening in the first and third week of a Starfall.");
/*     */       
/* 708 */       done = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 716 */     return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\HugeAltarBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */