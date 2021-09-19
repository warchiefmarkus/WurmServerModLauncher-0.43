/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.bodys.TempWound;
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.LocatePlayerQuestion;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.spells.Excel;
/*     */ import com.wurmonline.server.spells.ForestGiant;
/*     */ import com.wurmonline.server.spells.FranticCharge;
/*     */ import com.wurmonline.server.spells.Hellstrength;
/*     */ import com.wurmonline.server.spells.OakShell;
/*     */ import com.wurmonline.server.spells.Phantasms;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ArtifactBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  78 */   private static final Logger logger = Logger.getLogger(ArtifactBehaviour.class.getName());
/*  79 */   private static long orbActivation = 0L;
/*     */ 
/*     */   
/*     */   ArtifactBehaviour() {
/*  83 */     super((short)35);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getOrbActivation() {
/*  93 */     return orbActivation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetOrbActivation() {
/* 101 */     orbActivation = 0L;
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
/* 112 */     List<ActionEntry> toReturn = new ArrayList<>();
/* 113 */     toReturn.addAll(super.getBehavioursFor(performer, target));
/* 114 */     if (performer.getWurmId() == target.getOwnerId())
/* 115 */       toReturn.add(Actions.actionEntrys[118]); 
/* 116 */     return toReturn;
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
/* 128 */     List<ActionEntry> toReturn = new ArrayList<>();
/* 129 */     toReturn.addAll(super.getBehavioursFor(performer, source, target));
/* 130 */     if (performer.getWurmId() == target.getOwnerId())
/* 131 */       toReturn.add(Actions.actionEntrys[118]); 
/* 132 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 143 */     boolean done = true;
/* 144 */     if (performer.getWurmId() == target.getOwnerId() && action == 118) {
/*     */       
/* 146 */       if (mayUseItem(target, performer))
/*     */       {
/* 148 */         return useItem(null, target, performer, counter);
/*     */       }
/*     */       
/* 151 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 152 */           .getName() + " emits no sense of power right now.");
/*     */     } else {
/*     */       
/* 155 */       done = super.action(act, performer, target, action, counter);
/* 156 */     }  return done;
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
/* 169 */     boolean done = false;
/* 170 */     if (performer.getWurmId() == target.getOwnerId() && action == 118) {
/*     */       
/* 172 */       done = true;
/* 173 */       if (mayUseItem(target, performer))
/*     */       {
/* 175 */         return useItem(source, target, performer, counter);
/*     */       }
/*     */       
/* 178 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 179 */           .getName() + " emits no sense of power right now.");
/*     */     } else {
/*     */       
/* 182 */       done = super.action(act, performer, source, target, action, counter);
/* 183 */     }  return done;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean mayUseItem(Item target, @Nullable Creature performer) {
/* 188 */     if (target.getAuxData() <= 0)
/* 189 */       return false; 
/* 190 */     if (target.getTemplateId() == 330) {
/* 191 */       return (WurmCalendar.currentTime - target.getData() > 86400L);
/*     */     }
/* 193 */     if (target.getTemplateId() == 334)
/* 194 */       return (WurmCalendar.currentTime - target.getData() > 345600L); 
/* 195 */     if (target.getTemplateId() == 339) {
/*     */ 
/*     */       
/* 198 */       if (WurmCalendar.currentTime - target.getData() < 28800L) {
/*     */         
/* 200 */         if (performer != null)
/* 201 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 202 */               .getName() + " is still fading from the last activation."); 
/* 203 */         return false;
/*     */       } 
/* 205 */       return true;
/*     */     } 
/* 207 */     return (WurmCalendar.currentTime - target.getData() > 28800L);
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
/*     */   public static final void spawnCreature(int deity, float posx, float posy, boolean onSurface, byte kingdom) {
/* 220 */     int creatureTemplate = 37;
/*     */     
/* 222 */     if (deity == 4) {
/*     */       
/* 224 */       kingdom = 3;
/* 225 */       creatureTemplate = 40;
/*     */ 
/*     */     
/*     */     }
/* 229 */     else if (kingdom == 3) {
/*     */       
/* 231 */       if (Server.rand.nextInt(2) == 0) {
/* 232 */         kingdom = 1;
/*     */       } else {
/* 234 */         kingdom = 2;
/*     */       } 
/*     */     } 
/* 237 */     if (deity == 2) {
/*     */       
/* 239 */       creatureTemplate = 39;
/*     */     }
/* 241 */     else if (deity == 3) {
/*     */       
/* 243 */       creatureTemplate = 38;
/*     */     } 
/*     */     
/*     */     try {
/* 247 */       CreatureTemplate ctemplate = CreatureTemplateFactory.getInstance().getTemplate(creatureTemplate);
/* 248 */       Creature cret = Creature.doNew(creatureTemplate, posx, posy, Server.rand.nextInt(360), onSurface ? 0 : -1, "", ctemplate
/* 249 */           .getSex(), kingdom);
/* 250 */       cret.setDeity(Deities.getDeity(deity));
/*     */     }
/* 252 */     catch (Exception ex) {
/*     */       
/* 254 */       logger.log(Level.WARNING, "Problem spawning new Creature with Template ID: " + creatureTemplate + ", at position: " + posx + ", " + posy + ", kingdom: " + 
/*     */ 
/*     */           
/* 257 */           Kingdoms.getNameFor(kingdom) + ", deity: " + deity + " due to " + ex
/* 258 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean useItem(@Nullable Item source, Item target, Creature performer, float counter) {
/* 265 */     boolean done = true;
/* 266 */     Server.getInstance().broadCastAction(performer
/* 267 */         .getName() + " uses " + performer.getHisHerItsString() + " " + target.getName() + "!", performer, 5);
/* 268 */     if (target.getTemplateId() == 332) {
/*     */       
/* 270 */       performer.getCommunicator().sendNormalServerMessage(EndGameItems.locateRandomEndGameItem(performer));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 275 */       LocatePlayerQuestion lpq = new LocatePlayerQuestion(performer, "Locate a soul", "Which soul do you wish to locate?", target.getWurmId(), true, 100.0D);
/* 276 */       lpq.sendQuestion();
/* 277 */       target.setAuxData((byte)(target.getAuxData() - 1));
/*     */     }
/* 279 */     else if (target.getTemplateId() == 330) {
/*     */ 
/*     */       
/* 282 */       if (Server.rand.nextInt(8) == 0)
/* 283 */         spawnCreature(2, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 8.0F, performer
/* 284 */             .getPosY() - 4.0F + Server.rand.nextFloat() * 8.0F, performer.isOnSurface(), performer
/* 285 */             .getKingdomId()); 
/* 286 */       performer.getCommunicator().sendNormalServerMessage("You notice how a previously invisible etched dragon glows for a second.");
/*     */       
/* 288 */       if (performer.getDeity() != null && (performer.getDeity()).number == 2) {
/* 289 */         FranticCharge.doImmediateEffect(423, 200, (40 + Server.rand.nextInt(40)), performer);
/*     */       }
/* 291 */       target.setAuxData((byte)(target.getAuxData() - 1));
/*     */     }
/* 293 */     else if (target.getTemplateId() == 331) {
/*     */ 
/*     */       
/* 296 */       if (Server.rand.nextInt(8) == 0)
/* 297 */         spawnCreature(1, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 8.0F, performer.getPosY() - 4.0F + Server.rand
/* 298 */             .nextFloat() * 8.0F, performer.isOnSurface(), performer.getKingdomId()); 
/* 299 */       performer.getCommunicator().sendNormalServerMessage("You notice how a previously invisible etched boar glows for a second.");
/*     */       
/* 301 */       if (performer.getDeity() != null && (performer.getDeity()).number == 1) {
/* 302 */         OakShell.doImmediateEffect(404, 200, (20 + Server.rand.nextInt(40)), performer);
/*     */       }
/* 304 */       target.setAuxData((byte)(target.getAuxData() - 1));
/*     */     }
/* 306 */     else if (target.getTemplateId() == 333) {
/*     */       
/* 308 */       Village v = Villages.getVillageWithPerimeterAt(performer.getTileX(), performer.getTileY(), true);
/* 309 */       if (v == null) {
/*     */         
/* 311 */         if (Server.rand.nextInt(2) == 0) {
/*     */           
/* 313 */           Item item = TileRockBehaviour.createRandomGem();
/* 314 */           if (item != null) {
/*     */             
/* 316 */             item.setQualityLevel(70.0F + Server.rand.nextFloat() * 30.0F);
/* 317 */             performer.getCommunicator().sendNormalServerMessage("You shake the ear, and out pops " + item
/* 318 */                 .getNameWithGenus() + "!");
/* 319 */             performer.getInventory().insertItem(item, true);
/*     */             
/* 321 */             target.setAuxData((byte)(target.getAuxData() - 1));
/*     */           } else {
/*     */             
/* 324 */             performer.getCommunicator().sendNormalServerMessage("You shake the ear, but nothing happens.");
/*     */           } 
/* 326 */         } else if (Server.rand.nextInt(8) == 1) {
/*     */           
/* 328 */           spawnCreature(3, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 8.0F, performer
/* 329 */               .getPosY() - 4.0F + Server.rand.nextFloat() * 8.0F, performer.isOnSurface(), performer
/* 330 */               .getKingdomId());
/*     */           
/* 332 */           target.setAuxData((byte)(target.getAuxData() - 1));
/*     */         } else {
/*     */           
/* 335 */           performer.getCommunicator().sendNormalServerMessage("You shake the ear, but nothing happens.");
/*     */         } 
/*     */       } else {
/* 338 */         performer.getCommunicator().sendNormalServerMessage("You shake the ear, but nothing happens here at " + v
/* 339 */             .getName() + ".");
/*     */       } 
/* 341 */     } else if (target.getTemplateId() == 334) {
/*     */       
/* 343 */       performer.getStatus().modifyStamina2(100.0F);
/* 344 */       performer.getStatus().refresh(0.99F, true);
/* 345 */       ((Player)performer).getSaveFile().addToSleep(3600);
/* 346 */       performer.getCommunicator().sendNormalServerMessage("You feel refreshed.");
/* 347 */       if (performer.getDeity() != null && (performer.getDeity()).number == 3)
/* 348 */         Excel.doImmediateEffect(442, 200, (40 + Server.rand.nextInt(40)), performer); 
/* 349 */       target.setAuxData((byte)(target.getAuxData() - 1));
/*     */     }
/* 351 */     else if (target.getTemplateId() == 335) {
/*     */       
/* 353 */       performer.getBody().healFully();
/* 354 */       performer.getStatus().removeWounds();
/* 355 */       performer.getCommunicator().sendNormalServerMessage("A strong warm feeling permeates you and heals your wounds!");
/* 356 */       if (performer.getDeity() != null && (performer.getDeity()).number == 1) {
/* 357 */         ForestGiant.doImmediateEffect(410, 200, (20 + Server.rand.nextInt(40)), performer);
/*     */       }
/* 359 */       target.setAuxData((byte)(target.getAuxData() - 1));
/*     */     }
/* 361 */     else if (target.getTemplateId() == 336) {
/*     */ 
/*     */       
/*     */       try {
/* 365 */         if (performer.getDeity() != null) {
/*     */           
/* 367 */           performer.setFavor(performer.getFavor() + 10.0F);
/* 368 */           performer.getCommunicator().sendNormalServerMessage("You feel the strength of " + 
/* 369 */               (Deities.getDeity(2)).name + " within you.");
/*     */         }
/*     */         else {
/*     */           
/* 373 */           performer.getCommunicator().sendAlertServerMessage("A sudden pain enters your body.");
/* 374 */           performer.die(false, "Sword of Magranon");
/*     */         } 
/*     */         
/* 377 */         target.setAuxData((byte)(target.getAuxData() - 1));
/*     */       }
/* 379 */       catch (IOException iOException) {}
/*     */ 
/*     */     
/*     */     }
/* 383 */     else if (target.getTemplateId() == 337) {
/*     */ 
/*     */       
/*     */       try {
/* 387 */         if (performer.getDeity() != null) {
/*     */           
/* 389 */           performer.setFavor(performer.getFavor() + 10.0F);
/* 390 */           performer.getCommunicator().sendNormalServerMessage("You feel the strength of " + 
/* 391 */               (Deities.getDeity(2)).name + " within you.");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 396 */           performer.getCommunicator().sendAlertServerMessage("A sudden pain enters your body.");
/* 397 */           performer.die(false, "Hammer of Magranon");
/*     */         } 
/*     */         
/* 400 */         target.setAuxData((byte)(target.getAuxData() - 1));
/*     */       }
/* 402 */       catch (IOException iOException) {}
/*     */ 
/*     */     
/*     */     }
/* 406 */     else if (target.getTemplateId() == 338) {
/*     */ 
/*     */       
/* 409 */       if (Server.rand.nextInt(8) == 0) {
/*     */         
/* 411 */         spawnCreature(4, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 8.0F, performer.getPosY() - 4.0F + Server.rand
/* 412 */             .nextFloat() * 8.0F, performer.isOnSurface(), performer.getKingdomId());
/* 413 */         target.setAuxData((byte)(target.getAuxData() - 1));
/*     */       } 
/*     */       
/* 416 */       if (performer.getDeity() != null && (performer.getDeity()).number != 4) {
/*     */         
/* 418 */         target.setAuxData((byte)(target.getAuxData() - 1));
/* 419 */         Phantasms.doImmediateEffect(100.0D, performer);
/*     */       } 
/* 421 */       performer.getCommunicator().sendNormalServerMessage("You notice how a previously invisible etched skull glows for a second.");
/*     */ 
/*     */     
/*     */     }
/* 425 */     else if (target.getTemplateId() == 339) {
/*     */       
/* 427 */       boolean slow = true;
/* 428 */       if (Server.rand.nextInt(20) == 0) {
/* 429 */         slow = false;
/*     */       }
/* 431 */       if (!slow) {
/*     */         
/* 433 */         if (performer.getCurrentTile() != null)
/*     */         {
/* 435 */           target.setAuxData((byte)Math.max(0, target.getAuxData() - 10));
/* 436 */           int tilex = (performer.getCurrentTile()).tilex;
/* 437 */           int tiley = (performer.getCurrentTile()).tiley;
/* 438 */           for (int x = 2; x >= -2; x--)
/*     */           {
/* 440 */             for (int y = 2; y >= -2; y--)
/*     */             {
/*     */               
/*     */               try {
/* 444 */                 Zone zone = Zones.getZone(tilex + x, tiley + y, performer.isOnSurface());
/* 445 */                 VolaTile vtile = zone.getTileOrNull(tilex + x, tiley + y);
/* 446 */                 if (vtile != null) {
/*     */                   
/* 448 */                   Creature[] crets = vtile.getCreatures();
/* 449 */                   for (int c = 0; c < crets.length; c++) {
/*     */                     
/* 451 */                     if (!crets[c].isUnique() && !crets[c].isInvulnerable()) {
/*     */                       
/* 453 */                       Skill soul = null;
/*     */                       
/*     */                       try {
/* 456 */                         soul = crets[c].getSkills().getSkill(105);
/*     */                       }
/* 458 */                       catch (NoSuchSkillException nss) {
/*     */                         
/* 460 */                         soul = crets[c].getSkills().learn(105, 1.0F);
/*     */                       } 
/* 462 */                       if (soul.skillCheck(crets[c].isChampion() ? 40.0D : 30.0D, 0.0D, false, 1.0F) < 0.0D) {
/*     */                         
/* 464 */                         crets[c].getCommunicator()
/* 465 */                           .sendAlertServerMessage("A sudden pain enters your body.");
/* 466 */                         if (crets[c] == performer) {
/* 467 */                           crets[c].die(false, "Orb of Doom");
/*     */                         } else {
/*     */                           
/* 470 */                           int damage = (crets[c].getStatus()).damage;
/* 471 */                           int minhealth = 65435;
/* 472 */                           if (crets[c].isUnique())
/*     */                           {
/* 474 */                             minhealth = 55535;
/*     */                           }
/* 476 */                           float maxdam = Math.max(0, minhealth - damage);
/*     */                           
/* 478 */                           if (maxdam > 500.0F) {
/*     */                             
/* 480 */                             Wound wound = null;
/* 481 */                             if (crets[c] instanceof Player)
/*     */                             {
/* 483 */                               crets[c].addWoundOfType(performer, (byte)6, 0, false, 1.0F, false, maxdam, 0.0F, 50.0F, false, false);
/*     */ 
/*     */                             
/*     */                             }
/*     */                             else
/*     */                             {
/*     */                               
/* 490 */                               TempWound tempWound = new TempWound((byte)4, (byte)0, maxdam, crets[c].getWurmId(), 0.0F, 0.0F, false);
/* 491 */                               crets[c].getBody().addWound((Wound)tempWound);
/*     */                             }
/*     */                           
/*     */                           } else {
/*     */                             
/* 496 */                             crets[c].getCommunicator()
/* 497 */                               .sendSafeServerMessage("You grit your teeth and escape to the darkest, innermost corner of your soul. There you barely escape death.");
/*     */                             
/* 499 */                             Server.getInstance().broadCastAction(crets[c].getNameWithGenus() + " seems unaffected.", crets[c], 5);
/*     */                           }
/*     */                         
/*     */                         }
/*     */                       
/*     */                       } else {
/*     */                         
/* 506 */                         Server.getInstance().broadCastAction(crets[c].getNameWithGenus() + " seems unaffected.", crets[c], 5);
/*     */                         
/* 508 */                         crets[c].getCommunicator()
/* 509 */                           .sendSafeServerMessage("A sudden pain enters your body! You grit your teeth and escape to the darkest, innermost corner of your soul. There you barely escape death.");
/*     */                       }
/*     */                     
/*     */                     } else {
/*     */                       
/* 514 */                       Server.getInstance()
/* 515 */                         .broadCastAction(crets[c].getNameWithGenus() + " seems unaffected.", crets[c], 5);
/*     */                     } 
/*     */                   } 
/*     */                 } 
/* 519 */               } catch (NoSuchZoneException noSuchZoneException) {}
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 528 */         Skill soul = null;
/*     */         
/*     */         try {
/* 531 */           soul = performer.getSkills().getSkill(105);
/*     */         }
/* 533 */         catch (NoSuchSkillException nss) {
/*     */           
/* 535 */           soul = performer.getSkills().learn(105, 1.0F);
/*     */         } 
/* 537 */         performer.getCommunicator().sendNormalServerMessage("You use the " + target.getName() + ".");
/* 538 */         if (soul.skillCheck(performer.isChampion() ? 30.0D : 25.0D, 0.0D, false, 1.0F) < 0.0D) {
/*     */           
/* 540 */           performer.getCommunicator()
/* 541 */             .sendAlertServerMessage("A sudden pain enters your body.");
/* 542 */           performer.die(false, target.getName());
/* 543 */           return true;
/*     */         } 
/* 545 */         Server.getInstance().broadCastAction("A pulse surges through the air. " + performer
/* 546 */             .getName() + " activates the " + target.getName() + "!", performer, 15);
/*     */ 
/*     */         
/* 549 */         performer.sendActionControl(Actions.actionEntrys[118].getVerbString(), true, 200);
/* 550 */         markOrbRecipients(performer, true, 0.0F, 0.0F, 0.0F);
/* 551 */         target.setData(WurmCalendar.currentTime);
/* 552 */         orbActivation = System.currentTimeMillis();
/*     */       }
/*     */     
/* 555 */     } else if (target.getTemplateId() == 340) {
/*     */ 
/*     */       
/*     */       try {
/* 559 */         target.setAuxData((byte)(target.getAuxData() - 1));
/* 560 */         if (performer.getDeity() != null) {
/*     */           
/* 562 */           performer.setFavor(performer.getFavor() + 5.0F);
/* 563 */           performer.getCommunicator().sendNormalServerMessage("You feel the power of " + 
/* 564 */               (Deities.getDeity(4)).name + " descend on you.");
/* 565 */           if ((performer.getDeity()).number != 4) {
/* 566 */             Phantasms.doImmediateEffect(100.0D, performer);
/*     */           } else {
/* 568 */             Hellstrength.doImmediateEffect(427, 200, (20 + Server.rand.nextInt(40)), performer);
/*     */           } 
/*     */         } else {
/*     */           
/* 572 */           performer.getCommunicator().sendAlertServerMessage("A sudden pain enters your body.");
/* 573 */           performer.die(false, "Scepter of Ascension");
/*     */         }
/*     */       
/* 576 */       } catch (IOException iOException) {}
/*     */ 
/*     */     
/*     */     }
/* 580 */     else if (target.getTemplateId() == 329) {
/*     */ 
/*     */       
/*     */       try {
/* 584 */         target.setAuxData((byte)(target.getAuxData() - 1));
/* 585 */         if (performer.getDeity() != null) {
/*     */           
/* 587 */           if (performer.getFavor() < performer.getFaith() * 0.9F)
/* 588 */             performer.setFavor(performer.getFaith() * 0.9F); 
/* 589 */           performer.getCommunicator().sendNormalServerMessage("You feel the light of " + 
/* 590 */               (Deities.getDeity(1)).name + " shine on you.");
/*     */           
/* 592 */           if ((performer.getDeity()).number != 1) {
/* 593 */             Phantasms.doImmediateEffect(100.0D, performer);
/*     */           } else {
/* 595 */             ForestGiant.doImmediateEffect(410, 200, (20 + Server.rand.nextInt(40)), performer);
/*     */           } 
/*     */         } else {
/*     */           
/* 599 */           performer.getCommunicator().sendAlertServerMessage("A sudden pain enters your body.");
/* 600 */           performer.die(false, "Rod of Beguiling");
/*     */         }
/*     */       
/* 603 */       } catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 608 */     target.setData(WurmCalendar.currentTime);
/* 609 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void markOrbRecipients(@Nullable Creature performer, boolean mark, float posx, float posy, float posz) {
/* 615 */     Player[] players = Players.getInstance().getPlayers();
/* 616 */     for (Player p : players) {
/*     */       
/* 618 */       if (mark && p.isWithinDistanceTo(performer, 24.0F)) {
/*     */         
/* 620 */         if (!p.isFriendlyKingdom(performer.getKingdomId()))
/*     */         {
/* 622 */           if (p.getPower() == 0) {
/* 623 */             p.setMarkedByOrb(true);
/*     */           }
/*     */         }
/* 626 */       } else if (!mark) {
/*     */         
/* 628 */         if (p.isMarkedByOrb()) {
/*     */           
/* 630 */           boolean deal = false;
/* 631 */           if (performer != null) {
/*     */             
/* 633 */             if (p.isWithinDistanceTo(performer, 12.0F)) {
/* 634 */               deal = true;
/*     */             
/*     */             }
/*     */           }
/* 638 */           else if (p.isWithinDistanceTo(posx, posy, posz, 12.0F)) {
/* 639 */             deal = true;
/*     */           } 
/* 641 */           if (deal) {
/*     */             
/* 643 */             p.addAttacker(performer);
/* 644 */             p.getCommunicator().sendAlertServerMessage("You are marked by the Orb of Doom!");
/* 645 */             p.addWoundOfType(performer, (byte)6, 21, false, 1.0F, false, 30000.0D, 0.0F, 50.0F, false, false);
/*     */           } 
/*     */           
/* 648 */           p.setMarkedByOrb(false);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ArtifactBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */