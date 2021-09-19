/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.RuneUtilities;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.KarmaQuestion;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.server.spells.Spells;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.util.StringUtilities;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class BodyPartBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(BodyPartBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BodyPartBehaviour() {
/*  54 */     super((short)10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
/*  65 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  66 */     if (object.isBodyPartRemoved()) {
/*     */       
/*  68 */       toReturn.addAll(super.getBehavioursFor(performer, object));
/*     */     }
/*  70 */     else if (object.isBodyPartAttached()) {
/*     */       
/*  72 */       if (performer.getCultist() != null && performer.getCultist().mayRefresh())
/*  73 */         toReturn.add(new ActionEntry((short)91, "Refresh", "refreshing", emptyIntArr)); 
/*  74 */       if (performer.getCultist() != null && performer.getCultist().mayStartLoveEffect())
/*  75 */         toReturn.add(Actions.actionEntrys[389]); 
/*  76 */       if (performer.getCultist() != null && performer.getCultist().mayStartDoubleWarDamage())
/*  77 */         toReturn.add(Actions.actionEntrys[390]); 
/*  78 */       if (performer.getCultist() != null && performer.getCultist().mayStartDoubleStructDamage())
/*  79 */         toReturn.add(Actions.actionEntrys[391]); 
/*  80 */       if (performer.getCultist() != null && performer.getCultist().mayStartFearEffect())
/*  81 */         toReturn.add(Actions.actionEntrys[392]); 
/*  82 */       if (performer.getCultist() != null && performer.getCultist().mayStartNoElementalDamage())
/*  83 */         toReturn.add(Actions.actionEntrys[393]); 
/*  84 */       if (performer.getCultist() != null && performer.getCultist().mayStartIgnoreTraps())
/*  85 */         toReturn.add(Actions.actionEntrys[394]); 
/*  86 */       if (performer.getCultist() != null && performer.getCultist().mayFillup())
/*  87 */         toReturn.add(Actions.actionEntrys[396]); 
/*  88 */       if (performer.getCultist() != null && performer.getCultist().mayTeleport()) {
/*     */ 
/*     */ 
/*     */         
/*  92 */         toReturn.add(new ActionEntry((short)-1, "Random Teleport", "Teleporting", new int[0]));
/*  93 */         toReturn.add(new ActionEntry((short)95, "Confirm", "Teleporting", new int[0]));
/*     */       } 
/*  95 */       if (performer.getCultist() != null && performer.getCultist().mayRecall())
/*  96 */         toReturn.add(Actions.actionEntrys[489]); 
/*  97 */       if (Constants.isEigcEnabled)
/*  98 */         toReturn.add(Actions.actionEntrys[494]); 
/*  99 */       if (performer.mayUseLastGasp())
/* 100 */         toReturn.add(Actions.actionEntrys[495]); 
/* 101 */       if (performer.getKarma() > 0)
/* 102 */         toReturn.add(Actions.actionEntrys[511]); 
/* 103 */       toReturn.add(new ActionEntry((short)585, "Unequip all", "unequipping", emptyIntArr));
/*     */       
/* 105 */       toReturn.addAll(ManageMenu.getBehavioursFor(performer));
/*     */       
/* 107 */       if (performer.getPower() <= 0 && Kingdoms.isCustomKingdom(performer.getKingdomId()))
/*     */       {
/* 109 */         toReturn.add(Actions.actionEntrys[89]);
/*     */       }
/* 111 */       addEmotes(toReturn);
/*     */     } 
/* 113 */     return toReturn;
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
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
/* 125 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 126 */     toReturn.addAll(getBehavioursFor(performer, object));
/* 127 */     if (source.isHolyItem() && object.isBodyPartAttached()) {
/*     */       
/* 129 */       if (source.isHolyItem(performer.getDeity()))
/*     */       {
/* 131 */         if (performer.isPriest() || performer.getPower() > 0) {
/*     */           
/* 133 */           float faith = performer.getFaith();
/* 134 */           Spell[] spells = performer.getDeity().getSpellsTargettingCreatures((int)faith);
/*     */           
/* 136 */           if (spells.length > 0) {
/*     */             
/* 138 */             toReturn.add(new ActionEntry((short)-spells.length, "Spells", "spells"));
/* 139 */             for (Spell lSpell : spells) {
/* 140 */               toReturn.add(Actions.actionEntrys[lSpell.number]);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/* 145 */     } else if (object.isBodyPartAttached() && (source.isMagicStaff() || (source
/* 146 */       .getTemplateId() == 176 && performer.getPower() >= 4 && Servers.isThisATestServer()))) {
/*     */       
/* 148 */       List<ActionEntry> slist = new LinkedList<>();
/*     */       
/* 150 */       if (performer.knowsKarmaSpell(547))
/* 151 */         slist.add(Actions.actionEntrys[547]); 
/* 152 */       if (performer.knowsKarmaSpell(554))
/* 153 */         slist.add(Actions.actionEntrys[554]); 
/* 154 */       if (performer.knowsKarmaSpell(555))
/* 155 */         slist.add(Actions.actionEntrys[555]); 
/* 156 */       if (performer.knowsKarmaSpell(560))
/* 157 */         slist.add(Actions.actionEntrys[560]); 
/* 158 */       if (object.getOwnerId() == performer.getWurmId()) {
/*     */         
/* 160 */         if (performer.knowsKarmaSpell(548))
/* 161 */           slist.add(Actions.actionEntrys[548]); 
/* 162 */         if (performer.knowsKarmaSpell(552))
/* 163 */           slist.add(Actions.actionEntrys[552]); 
/* 164 */         if (performer.knowsKarmaSpell(553))
/* 165 */           slist.add(Actions.actionEntrys[553]); 
/* 166 */         if (performer.knowsKarmaSpell(562))
/* 167 */           slist.add(Actions.actionEntrys[562]); 
/*     */       } 
/* 169 */       if (performer.getPower() >= 4)
/* 170 */         toReturn.add(new ActionEntry((short)-slist.size(), "Sorcery", "casting")); 
/* 171 */       toReturn.addAll(slist);
/*     */     } 
/* 173 */     if (object.isBodyPartAttached() && source.getTemplate().isRune()) {
/*     */       
/* 175 */       Skill soulDepth = performer.getSoulDepth();
/* 176 */       double diff = (20.0F + source.getDamage()) - (source.getCurrentQualityLevel() + source.getRarity()) - 45.0D;
/* 177 */       double chance = soulDepth.getChance(diff, null, source.getCurrentQualityLevel());
/* 178 */       if (RuneUtilities.isSingleUseRune(source) && ((RuneUtilities.getSpellForRune(source) != null && 
/* 179 */         RuneUtilities.getSpellForRune(source).isTargetCreature()) || 
/* 180 */         RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_REFRESH) > 0.0F))
/* 181 */         toReturn.add(new ActionEntry((short)945, "Use Rune: " + chance + "%", "using rune", emptyIntArr)); 
/*     */     } 
/* 183 */     if (object.isBodyPartAttached() && source.canHaveInscription())
/*     */     {
/* 185 */       toReturn.addAll(PapyrusBehaviour.getPapyrusBehavioursFor(performer, source));
/*     */     }
/* 187 */     if (performer.getPower() >= 4 && object.getOwnerId() == performer.getWurmId())
/*     */     {
/* 189 */       toReturn.add(Actions.actionEntrys[721]);
/*     */     }
/* 191 */     return toReturn;
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
/* 204 */     if (target.isBodyPartRemoved())
/*     */     {
/* 206 */       return super.action(act, performer, source, target, action, counter);
/*     */     }
/* 208 */     if (action == 95) {
/*     */       
/* 210 */       if (performer.getCultist() != null && performer.getCultist().mayTeleport())
/*     */       {
/* 212 */         return action(act, performer, target, action, counter);
/*     */       }
/*     */     } else {
/* 215 */       if (action == 489)
/*     */       {
/* 217 */         return action(act, performer, target, action, counter);
/*     */       }
/* 219 */       if (action == 721) {
/*     */         
/* 221 */         if (performer.getPower() >= 4 && target.getOwnerId() == performer.getWurmId()) {
/* 222 */           Methods.sendGmSetMedpathQuestion(performer, performer);
/*     */         }
/* 224 */         return true;
/*     */       } 
/* 226 */     }  if (action == 945)
/*     */     {
/* 228 */       if (source.getTemplate().isRune())
/*     */       {
/* 230 */         if (RuneUtilities.isSingleUseRune(source) && ((RuneUtilities.getSpellForRune(source) != null && 
/* 231 */           RuneUtilities.getSpellForRune(source).isTargetCreature()) || 
/* 232 */           RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(source), RuneUtilities.ModifierEffect.SINGLE_REFRESH) > 0.0F))
/*     */         {
/* 234 */           return CreatureBehaviour.useRuneOnCreature(act, performer, source, performer, action, counter);
/*     */         }
/*     */       }
/*     */     }
/* 238 */     if ((action == 6 || action == 7) && target.isBodyPartAttached())
/* 239 */       return true; 
/* 240 */     if (act.isSpell() && target.isBodyPartAttached()) {
/*     */       
/* 242 */       boolean done = true;
/* 243 */       Spell spell = Spells.getSpell(action);
/* 244 */       if (spell != null) {
/*     */         
/*     */         try {
/*     */           
/* 248 */           Creature targ = Server.getInstance().getCreature(target.getOwnerId());
/* 249 */           if (spell.offensive)
/*     */           {
/* 251 */             if (!targ.equals(performer)) {
/*     */               
/* 253 */               if (!targ.isInvulnerable()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 259 */                 if (performer.mayAttack(targ)) {
/*     */                   
/* 261 */                   if (performer.opponent != null || targ.isAggHuman())
/*     */                   {
/* 263 */                     if (performer.opponent == null)
/* 264 */                       performer.setOpponent(targ); 
/*     */                   }
/* 266 */                   if (targ.opponent == null) {
/*     */                     
/* 268 */                     targ.setOpponent(performer);
/* 269 */                     targ.setTarget(performer.getWurmId(), false);
/* 270 */                     targ.getCommunicator().sendNormalServerMessage(performer
/* 271 */                         .getNameWithGenus() + " is attacking you with a spell!");
/*     */                   } 
/*     */                 } else {
/* 274 */                   if (!performer.isStunned() && !performer.isUnconscious()) {
/*     */                     
/* 276 */                     performer.getCommunicator().sendNormalServerMessage("You are too weak to attack.");
/* 277 */                     return true;
/*     */                   } 
/*     */                   
/* 280 */                   return true;
/*     */                 } 
/*     */               } else {
/*     */                 
/* 284 */                 performer.getCommunicator().sendNormalServerMessage(target
/* 285 */                     .getName() + " seems to absorb the spell with no effect.");
/* 286 */                 return true;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 291 */               performer.getCommunicator().sendNormalServerMessage("You can not cast that upon yourself.");
/* 292 */               return true;
/*     */             } 
/*     */           }
/* 295 */           if (spell.religious) {
/*     */             
/* 297 */             if (performer.getDeity() != null) {
/*     */ 
/*     */               
/* 300 */               if (Methods.isActionAllowed(performer, (short)245)) {
/* 301 */                 done = Methods.castSpell(performer, spell, targ, counter);
/*     */               }
/*     */             } else {
/*     */               
/* 305 */               performer.getCommunicator().sendNormalServerMessage("You have no deity and cannot cast the spell.");
/* 306 */               done = true;
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 311 */           else if (source.isMagicStaff() || (source
/* 312 */             .getTemplateId() == 176 && performer.getPower() >= 2 && Servers.isThisATestServer())) {
/*     */ 
/*     */             
/* 315 */             if (Methods.isActionAllowed(performer, (short)547)) {
/* 316 */               done = Methods.castSpell(performer, spell, targ, counter);
/*     */             }
/*     */           } else {
/*     */             
/* 320 */             performer.getCommunicator().sendNormalServerMessage("You need to use a magic staff.");
/* 321 */             done = true;
/*     */           }
/*     */         
/*     */         }
/* 325 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */         
/*     */         }
/* 329 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 334 */       return done;
/*     */     } 
/* 336 */     if (action == 744 && source.canHaveInscription())
/*     */     {
/* 338 */       return PapyrusBehaviour.addToCookbook(act, performer, source, target, action, counter);
/*     */     }
/* 340 */     if (action == 1 || action == 494 || action == 495 || action == 511 || action == 566)
/*     */     {
/* 342 */       return action(act, performer, target, action, counter); } 
/* 343 */     return super.action(act, performer, source, target, action, counter);
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
/* 354 */     boolean toReturn = true;
/* 355 */     if (target.isBodyPartRemoved())
/*     */     {
/* 357 */       return super.action(act, performer, target, action, counter);
/*     */     }
/* 359 */     if (action == 1) {
/*     */       
/* 361 */       performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/* 362 */       if (target.isBodyPartAttached()) {
/*     */         
/* 364 */         Creature owner = performer;
/*     */         
/*     */         try {
/* 367 */           owner = Server.getInstance().getCreature(target.getOwnerId());
/* 368 */           if (owner.equals(performer)) {
/*     */             
/* 370 */             String kingdom = "a Jenn-Kellon.";
/* 371 */             if (owner.getKingdomId() == 0) {
/* 372 */               kingdom = "not allied to anyone.";
/* 373 */             } else if (owner.getKingdomId() == 2) {
/* 374 */               kingdom = "a Mol Rehan.";
/* 375 */             } else if (owner.getKingdomId() == 3) {
/* 376 */               kingdom = "with the Horde of the Summoned.";
/* 377 */             } else if (owner.getKingdomId() == 4) {
/* 378 */               kingdom = "from the Freedom Isles.";
/* 379 */             } else if (owner.getKingdomId() != 0) {
/* 380 */               kingdom = "a " + Kingdoms.getNameFor(owner.getKingdomId());
/* 381 */             }  if (owner.isKing()) {
/*     */               
/* 383 */               King k = King.getKing(owner.getKingdomId());
/* 384 */               if (k != null)
/*     */               {
/* 386 */                 performer.getCommunicator().sendNormalServerMessage("You are " + k.getFullTitle() + ".");
/*     */               }
/*     */             } 
/* 389 */             performer.getCommunicator().sendNormalServerMessage("You are " + kingdom);
/*     */             
/* 391 */             String appointments = owner.getAppointmentTitles();
/* 392 */             if (appointments.length() > 0)
/* 393 */               performer.getCommunicator().sendNormalServerMessage(appointments); 
/* 394 */             if (performer.getPower() > 0) {
/*     */               
/* 396 */               performer.getCommunicator().sendNormalServerMessage("Reputation: " + owner
/* 397 */                   .getReputation() + " Alignment: " + owner.getAlignment());
/* 398 */               if (performer.getPower() >= 5)
/* 399 */                 performer.getCommunicator().sendNormalServerMessage("Mount speed mod=" + performer
/* 400 */                     .getMovementScheme().getMountSpeed()); 
/*     */             } 
/* 402 */             performer.getCommunicator().sendNormalServerMessage(
/* 403 */                 StringUtilities.raiseFirstLetter(owner.getStatus().getBodyType()));
/* 404 */             if (owner.isPlayer())
/* 405 */               performer.getCommunicator().sendNormalServerMessage("Rank=" + ((Player)owner)
/* 406 */                   .getRank() + ", Max Rank=" + ((Player)owner).getMaxRank()); 
/* 407 */             if (Servers.isThisATestServer() && performer.isPlayer()) {
/*     */               
/* 409 */               Player player = (Player)performer;
/* 410 */               performer.getCommunicator().sendNormalServerMessage(String.format("Your current alcohol level is %.1f.", new Object[] {
/* 411 */                       Float.valueOf(player.getAlcohol())
/*     */                     }));
/*     */             } 
/*     */           } 
/* 415 */         } catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */         
/*     */         }
/* 419 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 424 */         if (performer.getPower() >= 3) {
/*     */           
/* 426 */           performer.getCommunicator().sendNormalServerMessage("Position x=" + owner
/* 427 */               .getPosX() + "(" + owner.getTileX() + "), y=" + owner.getPosY() + "(" + owner
/* 428 */               .getTileY() + "), z=" + owner.getPositionZ() + "(+" + owner.getAltOffZ() + "), floorlevel=" + owner
/* 429 */               .getFloorLevel());
/* 430 */           if (performer.getCurrentTile() != null) {
/* 431 */             performer.getCommunicator().sendNormalServerMessage("Tile position x=" + 
/* 432 */                 (owner.getCurrentTile()).tilex + ", " + (owner.getCurrentTile()).tiley);
/*     */           } else {
/* 434 */             performer.getCommunicator().sendNormalServerMessage(owner.getName() + " has no current tile.");
/* 435 */           }  performer.getCommunicator().sendNormalServerMessage("Layer=" + owner
/* 436 */               .getLayer() + ", Teleporting=" + owner.isTeleporting() + ", vehicle=" + owner
/* 437 */               .getVehicle() + " Origin server " + WurmId.getOrigin(performer.getWurmId()));
/* 438 */           if (owner.getVisionArea() != null) {
/* 439 */             performer.getCommunicator().sendNormalServerMessage("Visionarea initialized=" + owner
/* 440 */                 .getVisionArea().isInitialized() + ". Surface startx=" + owner
/* 441 */                 .getVisionArea().getSurface().getStartX() + ", startY=" + owner
/* 442 */                 .getVisionArea().getSurface().getStartY() + ", endX=" + owner
/* 443 */                 .getVisionArea().getSurface().getEndX() + ", endY=" + owner
/* 444 */                 .getVisionArea().getSurface().getEndY());
/*     */           } else {
/* 446 */             performer.getCommunicator().sendNormalServerMessage("Visionarea not initialized");
/*     */           } 
/*     */         } 
/*     */       } 
/* 450 */     } else if (action == 5) {
/*     */       
/* 452 */       Server.getInstance().broadCastNormal(performer.getName() + " waves his " + target.getName() + " frantically.");
/*     */     }
/* 454 */     else if (action == 389) {
/*     */       
/* 456 */       if (performer.getCultist() != null && performer.getCultist().mayStartLoveEffect())
/*     */       {
/* 458 */         performer.getCommunicator().sendNormalServerMessage("You feel the love stream towards you.");
/* 459 */         performer.getCultist().touchCooldown3();
/* 460 */         if (performer.isFighting())
/*     */         {
/* 462 */           if (performer.opponent != null && performer.opponent.opponent == performer)
/*     */           {
/* 464 */             performer.opponent.setTarget(-10L, true);
/*     */           }
/*     */         }
/* 467 */         performer.setTarget(-10L, true);
/*     */         
/* 469 */         performer.refreshAttitudes();
/* 470 */         performer.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.LOVE_EFFECT, performer
/*     */             
/* 472 */             .getCultist().getLoveEffectTimeLeftSeconds(), 100.0F);
/*     */       }
/*     */     
/* 475 */     } else if (action == 91) {
/*     */       
/* 477 */       if (performer.getCultist() != null && performer.getCultist().mayRefresh()) {
/*     */         
/* 479 */         float nut = Math.min(50 + (performer.getCultist().getLevel() - 4) * 5, 99) / 100.0F;
/* 480 */         performer.getStatus().refresh(nut, true);
/* 481 */         performer.getCommunicator().sendNormalServerMessage("You send yourself a warm thought.");
/* 482 */         if (performer.getCultist() != null) {
/* 483 */           performer.getCultist().touchCooldown1();
/*     */         }
/*     */       } 
/* 486 */     } else if (action == 390) {
/*     */       
/* 488 */       if (performer.getCultist() != null && performer.getCultist().mayStartDoubleWarDamage())
/*     */       {
/* 490 */         performer.getCommunicator().sendNormalServerMessage("You feel the rage pulsate within.");
/* 491 */         performer.getCultist().touchCooldown1();
/* 492 */         performer.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.HATE_DOUBLE_WAR, performer
/*     */             
/* 494 */             .getCultist().getDoubleWarDamageTimeLeftSeconds(), 100.0F);
/*     */       }
/*     */     
/* 497 */     } else if (action == 391) {
/*     */       
/* 499 */       if (performer.getCultist() != null && performer.getCultist().mayStartDoubleStructDamage())
/*     */       {
/* 501 */         performer.getCommunicator().sendNormalServerMessage("You feel the rage pulsate within.");
/* 502 */         performer.getCultist().touchCooldown2();
/* 503 */         performer.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.HATE_DOUBLE_STRUCT, performer
/*     */             
/* 505 */             .getCultist().getDoubleStructDamageTimeLeftSeconds(), 100.0F);
/*     */       }
/*     */     
/* 508 */     } else if (action == 392) {
/*     */       
/* 510 */       if (performer.getCultist() != null && performer.getCultist().mayStartFearEffect())
/*     */       {
/* 512 */         performer.getCommunicator().sendNormalServerMessage("Everything will fear you now, and rightly so.");
/* 513 */         performer.getCultist().touchCooldown3();
/* 514 */         performer.refreshAttitudes();
/* 515 */         performer.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.HATE_FEAR_EFFECT, performer
/*     */             
/* 517 */             .getCultist().getFearEffectTimeLeftSeconds(), 100.0F);
/*     */       }
/*     */     
/* 520 */     } else if (action == 393) {
/*     */       
/* 522 */       if (performer.getCultist() != null && performer.getCultist().mayStartNoElementalDamage())
/*     */       {
/* 524 */         performer.getCommunicator().sendNormalServerMessage("You align yourself with the elements.");
/* 525 */         performer.getCultist().touchCooldown1();
/* 526 */         performer.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POWER_NO_ELEMENTAL, performer
/*     */             
/* 528 */             .getCultist().getElementalImmunityTimeLeftSeconds(), 100.0F);
/*     */       }
/*     */     
/* 531 */     } else if (action == 394) {
/*     */       
/* 533 */       if (performer.getCultist() != null && performer.getCultist().mayStartIgnoreTraps())
/*     */       {
/* 535 */         performer.getCommunicator().sendNormalServerMessage("You alert yourself to traps.");
/* 536 */         performer.getCultist().touchCooldown3();
/* 537 */         performer.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POWER_IGNORE_TRAPS, performer
/*     */             
/* 539 */             .getCultist().getIgnoreTrapsTimeLeftSeconds(), 100.0F);
/*     */       }
/*     */     
/* 542 */     } else if (action == 396) {
/*     */       
/* 544 */       if (performer.getCultist() != null && performer.getCultist().mayFillup()) {
/*     */         
/* 546 */         performer.getCommunicator().sendNormalServerMessage("You relax and forget about your hunger.");
/* 547 */         performer.getCultist().touchCooldown2();
/*     */         
/* 549 */         performer.getStatus().modifyHunger(-60000, 0.98F);
/*     */       } 
/*     */     } else {
/* 552 */       if (action == 95) {
/*     */         
/* 554 */         if (performer.getCultist() != null && performer.getCultist().mayTeleport()) {
/*     */           
/* 556 */           if (performer.getEnemyPresense() > 0) {
/*     */             
/* 558 */             performer.getCommunicator().sendNormalServerMessage("There is a blocking enemy presence nearby. Nothing happens.");
/*     */             
/* 560 */             return true;
/*     */           } 
/* 562 */           int tilex = Zones.safeTileX(Server.rand.nextInt(Zones.worldTileSizeX));
/* 563 */           int tiley = Zones.safeTileY(Server.rand.nextInt(Zones.worldTileSizeY));
/*     */           
/* 565 */           if (!MethodsCreatures.mayTelePortToTile(performer, tilex, tiley, true)) {
/*     */             
/* 567 */             performer.getCommunicator().sendNormalServerMessage("Something blocked your attempt. Nothing happens.");
/* 568 */             return true;
/*     */           } 
/* 570 */           if (performer.getPower() <= 0) {
/*     */             
/* 572 */             Item[] inventoryItems = performer.getInventory().getAllItems(true);
/* 573 */             for (Item lInventoryItem : inventoryItems) {
/*     */               
/* 575 */               if (lInventoryItem.isArtifact()) {
/*     */                 
/* 577 */                 performer.getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/* 578 */                     .getName() + " softly hums. Nothing happens.");
/* 579 */                 return true;
/*     */               } 
/*     */             } 
/* 582 */             Item[] bodyItems = performer.getBody().getBodyItem().getAllItems(true);
/* 583 */             for (Item lInventoryItem : bodyItems) {
/*     */               
/* 585 */               if (lInventoryItem.isArtifact()) {
/*     */                 
/* 587 */                 performer.getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/* 588 */                     .getName() + " softly hums. Nothing happens.");
/* 589 */                 return true;
/*     */               } 
/*     */             } 
/* 592 */             if (performer.isInPvPZone()) {
/*     */               
/* 594 */               performer.getCommunicator().sendNormalServerMessage("The magic of the pvp zone interferes. You can not teleport here.");
/*     */               
/* 596 */               return true;
/*     */             } 
/*     */           } 
/* 599 */           performer.setTeleportPoints((short)tilex, (short)tiley, 0, 0);
/* 600 */           Server.getInstance()
/* 601 */             .broadCastAction(performer
/* 602 */               .getName() + " shuts " + performer.getHisHerItsString() + " eyes, dreaming away.", performer, 5);
/*     */           
/* 604 */           if (performer.startTeleporting()) {
/*     */             
/* 606 */             performer.getCommunicator().sendNormalServerMessage("You close your eyes and let your spirit fly.");
/*     */             
/* 608 */             performer.getCommunicator().sendTeleport(false);
/* 609 */             performer.getCultist().touchCooldown3();
/*     */           } 
/*     */         } 
/* 612 */         return true;
/*     */       } 
/* 614 */       if (action == 489) {
/*     */         
/* 616 */         if (performer.getCultist() != null && performer.getCultist().mayRecall()) {
/*     */           
/* 618 */           Item[] inventoryItems = performer.getInventory().getAllItems(true);
/* 619 */           for (Item lInventoryItem : inventoryItems) {
/*     */             
/* 621 */             if (lInventoryItem.isArtifact()) {
/*     */               
/* 623 */               performer.getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/* 624 */                   .getName() + " hums and disturbs the weave. You can not teleport right now.");
/*     */               
/* 626 */               return true;
/*     */             } 
/*     */           } 
/* 629 */           Item[] bodyItems = performer.getBody().getBodyItem().getAllItems(true);
/* 630 */           for (Item lInventoryItem : bodyItems) {
/*     */             
/* 632 */             if (lInventoryItem.isArtifact()) {
/*     */               
/* 634 */               performer.getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/* 635 */                   .getName() + " hums and disturbs the weave. You can not teleport right now.");
/*     */               
/* 637 */               return true;
/*     */             } 
/*     */           } 
/* 640 */           if (performer.getCitizenVillage() != null) {
/*     */             
/* 642 */             if (performer.mayChangeVillageInMillis() > 0L) {
/*     */               
/* 644 */               performer.getCommunicator().sendNormalServerMessage("You are still too new to this village. Nothing happens.");
/*     */               
/* 646 */               return true;
/*     */             } 
/* 648 */             if (performer.getEnemyPresense() > 0) {
/*     */               
/* 650 */               performer.getCommunicator().sendNormalServerMessage("There is a blocking enemy presence nearby. Nothing happens.");
/*     */               
/* 652 */               return true;
/*     */             } 
/* 654 */             if (performer.isInPvPZone()) {
/*     */               
/* 656 */               performer.getCommunicator().sendNormalServerMessage("The magic of the pvp zone interferes. You can not teleport here.");
/*     */               
/* 658 */               return true;
/*     */             } 
/* 660 */             performer.setTeleportPoints((short)performer.getCitizenVillage().getTokenX(), 
/* 661 */                 (short)performer.getCitizenVillage().getTokenY(), 0, 0);
/* 662 */             if (performer.startTeleporting()) {
/*     */               
/* 664 */               performer.getCommunicator()
/* 665 */                 .sendNormalServerMessage("You close your eyes and let your spirit fly home.");
/* 666 */               Server.getInstance().broadCastAction(performer
/* 667 */                   .getName() + " shuts " + performer.getHisHerItsString() + " eyes and suddenly disappears.", performer, 5);
/*     */               
/* 669 */               performer.getCommunicator().sendTeleport(false);
/* 670 */               performer.getCultist().touchCooldown4();
/*     */             } 
/*     */           } else {
/*     */             
/* 674 */             performer.getCommunicator().sendNormalServerMessage("You need to be part of a settlement you can call home.");
/*     */           } 
/*     */         } 
/* 677 */         return true;
/*     */       } 
/* 679 */       if (action == 494)
/*     */       
/* 681 */       { Methods.sendVoiceChatQuestion(performer); }
/*     */       
/* 683 */       else if (action == 495)
/*     */       
/* 685 */       { if (performer.mayUseLastGasp()) {
/* 686 */           performer.useLastGasp();
/*     */         } }
/* 688 */       else if (action == 511)
/*     */       
/* 690 */       { if (performer.getKarma() > 0) {
/*     */           
/* 692 */           KarmaQuestion kq = new KarmaQuestion(performer);
/* 693 */           kq.sendQuestion();
/*     */         } else {
/*     */           
/* 696 */           performer.getCommunicator().sendNormalServerMessage("You have no karma to use.");
/*     */         }  }
/* 698 */       else { if (ManageMenu.isManageAction(performer, action))
/*     */         {
/* 700 */           return ManageMenu.action(act, performer, action, counter);
/*     */         }
/* 702 */         if (action == 89)
/*     */         {
/* 704 */           if (performer.getPower() <= 0)
/* 705 */             return super.action(act, performer, target, action, counter);  }  }
/*     */     
/* 707 */     }  return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\BodyPartBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */