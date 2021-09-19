/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.bodys.Wounds;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Traits;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemSpellEffects;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.AskConvertQuestion;
/*      */ import com.wurmonline.server.questions.ConvertQuestion;
/*      */ import com.wurmonline.server.questions.QuestionParser;
/*      */ import com.wurmonline.server.questions.QuestionTypes;
/*      */ import com.wurmonline.server.questions.RechargeQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.spells.RiteEvent;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.tutorial.MissionTrigger;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.zones.FaithZone;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.io.IOException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class MethodsReligion
/*      */   implements MiscConstants, QuestionTypes, ItemTypes, CounterTypes, ItemMaterials, SoundNames, VillageStatus, TimeConstants, MonetaryConstants
/*      */ {
/*      */   public static final String cvsversion = "$Id: MethodsReligion.java,v 1.22 2007-04-19 23:05:18 root Exp $";
/*  100 */   private static final Logger logger = Logger.getLogger(MethodsReligion.class.getName());
/*  101 */   public static final Map<Long, Long> listenedTo = new HashMap<>();
/*  102 */   private static final Map<Long, Long> lastReceivedAlignment = new HashMap<>();
/*  103 */   private static final Map<Long, Long> lastHeldSermon = new HashMap<>();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final float MILLION = 1000000.0F;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final float KTHOUS = 100000.0F;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean mayReceiveAlignment(Creature c) {
/*  116 */     Long lastReceived = lastReceivedAlignment.get(Long.valueOf(c.getWurmId()));
/*  117 */     if (lastReceived != null)
/*  118 */       return (System.currentTimeMillis() - lastReceived.longValue() > 1800000L); 
/*  119 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void setReceivedAlignment(Creature c) {
/*  124 */     lastReceivedAlignment.put(Long.valueOf(c.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean sendRechargeQuestion(Creature responder, Item rechargeable) {
/*  130 */     RechargeQuestion spm = new RechargeQuestion(responder, "Recharging " + rechargeable.getName(), "Do you want to ", rechargeable.getWurmId());
/*  131 */     spm.sendQuestion();
/*  132 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendAskConvertQuestion(Creature asker, Creature responder, Item holyItem) {
/*  137 */     if (((Player)asker).lastSentQuestion > 0) {
/*      */       
/*  139 */       asker.getCommunicator().sendNormalServerMessage("You must wait another " + ((Player)asker).lastSentQuestion + " seconds before asking again.");
/*      */       
/*      */       return;
/*      */     } 
/*  143 */     if (asker.getDeity() == null) {
/*      */       
/*  145 */       asker.getCommunicator().sendNormalServerMessage("You have no deity!");
/*      */       return;
/*      */     } 
/*  148 */     if (!holyItem.isHolyItem(asker.getDeity())) {
/*      */       
/*  150 */       asker.getCommunicator().sendNormalServerMessage("The " + holyItem.getName() + " is not blessed by your deity.");
/*      */       return;
/*      */     } 
/*  153 */     if (!responder.isPlayer()) {
/*      */       
/*  155 */       asker.getCommunicator().sendNormalServerMessage("Only players may be converted for the moment.");
/*      */       return;
/*      */     } 
/*  158 */     if (responder.isNewbie()) {
/*      */       
/*  160 */       asker.getCommunicator().sendNormalServerMessage(responder.getName() + " is too inexperienced to be converted.");
/*      */       return;
/*      */     } 
/*  163 */     if (!responder.mayChangeDeity(asker.getDeity().getNumber())) {
/*      */       
/*  165 */       asker.getCommunicator().sendNormalServerMessage(responder.getName() + " changed faith too recently to consider a new deity.");
/*      */       return;
/*      */     } 
/*  168 */     if (!responder.isWithinTileDistanceTo(asker.getTileX(), asker.getTileY(), (int)(asker.getPositionZ() + asker.getAltOffZ()) >> 2, 4)) {
/*      */       
/*  170 */       asker.getCommunicator().sendNormalServerMessage(responder.getName() + " is too far away now.");
/*      */       return;
/*      */     } 
/*  173 */     if (responder.isChampion()) {
/*      */       
/*  175 */       asker.getCommunicator().sendNormalServerMessage(responder.getName() + " is a champion, and may not convert.");
/*      */       
/*      */       return;
/*      */     } 
/*  179 */     if (responder.getDeity() == null || responder.getDeity() != asker.getDeity()) {
/*      */       
/*  181 */       if (QuestionParser.doesKingdomTemplateAcceptDeity(responder.getKingdomTemplateId(), asker.getDeity())) {
/*      */ 
/*      */         
/*  184 */         AskConvertQuestion spm = new AskConvertQuestion(responder, "The words of " + (asker.getDeity()).name, "Lecture:", asker.getWurmId(), holyItem);
/*  185 */         spm.sendQuestion();
/*  186 */         ((Player)asker).lastSentQuestion = 60;
/*      */       }
/*      */       else {
/*      */         
/*  190 */         asker.getCommunicator().sendNormalServerMessage("Following that deity would expel " + responder.getName() + " from their kingdom.");
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } else {
/*  196 */       asker.getCommunicator().sendNormalServerMessage(responder.getName() + " already follows " + asker.getDeity().getName() + ".");
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void sendConvertQuestion(Creature asker, Creature responder, Item holyItem, float counter) {
/*  204 */     if (holyItem.isHolyItem(asker.getDeity())) {
/*      */       
/*  206 */       Deity deity = asker.getDeity();
/*      */       
/*  208 */       if (deity != null) {
/*      */         
/*  210 */         if (responder instanceof Player) {
/*      */           
/*  212 */           if (responder.getDeity() != deity) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  217 */             if (!responder.isNewbie()) {
/*      */               
/*  219 */               if (responder.mayChangeDeity(deity.number)) {
/*      */                 
/*  221 */                 if (responder.isWithinTileDistanceTo(asker.getTileX(), asker.getTileY(), 
/*  222 */                     (int)(asker.getPositionZ() + asker.getAltOffZ()) >> 2, 4)) {
/*      */ 
/*      */                   
/*  225 */                   ConvertQuestion spm = new ConvertQuestion(responder, "Converting to " + (asker.getDeity()).name, "Convert:", asker.getWurmId(), holyItem);
/*  226 */                   spm.sendQuestion();
/*  227 */                   spm.setSkillcounter(counter);
/*      */                 } else {
/*      */                   
/*  230 */                   asker.getCommunicator().sendNormalServerMessage(responder
/*  231 */                       .getName() + " is too far away now.");
/*      */                 } 
/*      */               } else {
/*  234 */                 asker.getCommunicator().sendNormalServerMessage(responder
/*  235 */                     .getName() + " changed faith too recently to consider a new deity.");
/*      */               } 
/*      */             } else {
/*  238 */               asker.getCommunicator().sendNormalServerMessage(responder
/*  239 */                   .getName() + " is too inexperienced to be converted.");
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  247 */             asker.getCommunicator().sendNormalServerMessage(responder
/*  248 */                 .getName() + " already prays to " + (responder.getDeity()).name + "!");
/*  249 */             responder.getCommunicator().sendNormalServerMessage("You already pray to " + 
/*  250 */                 (responder.getDeity()).name + "!");
/*      */           } 
/*      */         } else {
/*      */           
/*  254 */           asker.getCommunicator().sendNormalServerMessage("Only players may be converted for the moment.");
/*      */         } 
/*      */       } else {
/*  257 */         asker.getCommunicator().sendNormalServerMessage("You have no deity!");
/*      */       } 
/*      */     } else {
/*  260 */       asker.getCommunicator().sendNormalServerMessage("The " + holyItem.getName() + " is not blessed by your deity.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean performRitual(Creature performer, Item source, Item target, float counter, int action, Action act) {
/*  266 */     boolean done = false;
/*  267 */     boolean ok = false;
/*  268 */     if (target.isKingdomMarker() || target.isEpicTargetItem() || target.getTemplateId() == 236)
/*      */     {
/*  270 */       ok = true;
/*      */     }
/*  272 */     if (act != null && ok) {
/*      */       
/*  274 */       String riteName = Actions.actionEntrys[action].getActionString();
/*  275 */       if (counter == 1.0F) {
/*      */         
/*  277 */         performer.getCommunicator().sendNormalServerMessage("You start to perform the " + riteName + ".");
/*  278 */         Server.getInstance().broadCastAction(performer.getName() + " starts to perform the " + riteName + ".", performer, 5);
/*      */         
/*  280 */         performer.sendActionControl(Actions.actionEntrys[216].getVerbString(), true, 360);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  285 */         short emote = 2010;
/*  286 */         if (act.currentSecond() == 3)
/*      */         {
/*  288 */           if (action >= 496)
/*  289 */             emote = 2002; 
/*  290 */           Emotes.emoteAt(emote, performer, target);
/*  291 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         }
/*  293 */         else if (act.currentSecond() == 9)
/*      */         {
/*  295 */           if (action >= 496 && action <= 497) {
/*  296 */             emote = 2014;
/*      */           } else {
/*  298 */             emote = 2016;
/*  299 */           }  Emotes.emoteAt(emote, performer, target);
/*  300 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         }
/*  302 */         else if (act.currentSecond() == 15)
/*      */         {
/*  304 */           if (action >= 496 && action <= 498) {
/*  305 */             emote = 2022;
/*      */           } else {
/*  307 */             emote = 2015;
/*  308 */           }  Emotes.emoteAt(emote, performer, target);
/*  309 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         }
/*  311 */         else if (act.currentSecond() == 21)
/*      */         {
/*  313 */           if (action >= 496 && action <= 499) {
/*  314 */             emote = 2021;
/*      */           } else {
/*  316 */             emote = 2024;
/*  317 */           }  Emotes.emoteAt(emote, performer, target);
/*  318 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         }
/*  320 */         else if (act.currentSecond() == 27)
/*      */         {
/*  322 */           if (action >= 496 && action <= 500) {
/*  323 */             emote = 2023;
/*      */           } else {
/*  325 */             emote = 2028;
/*  326 */           }  Emotes.emoteAt(emote, performer, target);
/*      */         }
/*  328 */         else if (act.currentSecond() == 33)
/*      */         {
/*  330 */           if (action >= 496 && action <= 501) {
/*  331 */             emote = 2013;
/*      */           } else {
/*  333 */             emote = 2005;
/*  334 */           }  Emotes.emoteAt(emote, performer, target);
/*  335 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         }
/*  337 */         else if (act.currentSecond() >= 36)
/*      */         {
/*  339 */           performer.getStatus().modifyStamina(-1000.0F);
/*  340 */           if (action >= 496 && action <= 500) {
/*  341 */             emote = 2006;
/*      */           } else {
/*  343 */             emote = 2029;
/*  344 */           }  Emotes.emoteAt(emote, performer, target);
/*  345 */           performer.getCommunicator().sendNormalServerMessage("Your ritual is complete.");
/*  346 */           Server.getInstance().broadCastAction(performer
/*  347 */               .getName() + " ends " + performer.getHisHerItsString() + " ritual.", performer, 5);
/*  348 */           if (action == 496) {
/*      */             
/*      */             try {
/*  351 */               performer.setFavor(performer.getFavor() + 0.1F);
/*      */             }
/*  353 */             catch (IOException iox) {
/*      */               
/*  355 */               logger.log(Level.WARNING, iox.getMessage());
/*      */             } 
/*  357 */           } else if (action == 501) {
/*      */             
/*  359 */             performer.healRandomWound(1);
/*  360 */             performer.setWounded();
/*      */           }
/*  362 */           else if (action == 498) {
/*      */             
/*  364 */             Server.getWeather().modifyFogTarget(0.01F);
/*      */           }
/*  366 */           else if (action == 497) {
/*      */             
/*  368 */             Server.getWeather().modifyRainTarget(0.01F);
/*      */           }
/*  370 */           else if (action == 499) {
/*      */             
/*  372 */             Server.getWeather().modifyCloudTarget(-0.01F);
/*      */           }
/*  374 */           else if (action == 502) {
/*      */             
/*  376 */             performer.addWoundOfType(performer, (byte)9, 1, false, 1.0F, false, 2000.0D, 0.0F, 0.0F, false, true);
/*      */           
/*      */           }
/*  379 */           else if (action == 500) {
/*      */             
/*  381 */             performer.healRandomWound(100);
/*      */           } 
/*      */           
/*  384 */           boolean trigger = true;
/*      */           
/*  386 */           boolean epicHomeServer = (Servers.localServer.EPIC && Servers.localServer.HOMESERVER);
/*      */ 
/*      */           
/*  389 */           if (target.isKingdomMarker()) {
/*      */             
/*  391 */             trigger = false;
/*  392 */             if (epicHomeServer) {
/*  393 */               trigger = true;
/*  394 */             } else if (!Servers.localServer.HOMESERVER) {
/*  395 */               trigger = true;
/*      */             } 
/*      */           } 
/*  398 */           if (trigger)
/*  399 */             MissionTriggers.activateTriggers(performer, source, action, target
/*  400 */                 .getWurmId(), 1); 
/*  401 */           done = true;
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/*  407 */       done = true;
/*  408 */       performer.getCommunicator().sendNormalServerMessage("You can't perform the ritual now.");
/*      */     } 
/*  410 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean preach(Creature performer, Creature responder, Item holyItem, float counter) {
/*  415 */     boolean done = false;
/*  416 */     Deity deity = performer.getDeity();
/*  417 */     if (deity == null) {
/*      */       
/*  419 */       performer.getCommunicator().sendNormalServerMessage("You have no deity and stop preaching.");
/*  420 */       done = true;
/*      */     }
/*  422 */     else if (!holyItem.isHolyItem(deity)) {
/*      */       
/*  424 */       performer.getCommunicator().sendNormalServerMessage("Your holy item is not blessed by " + deity.name + "!");
/*  425 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/*  429 */       Action act = null;
/*      */       
/*      */       try {
/*  432 */         act = performer.getCurrentAction();
/*      */       }
/*  434 */       catch (NoSuchActionException nsa) {
/*      */         
/*  436 */         done = true;
/*      */       } 
/*  438 */       if (act != null && act.getNumber() == 216) {
/*      */         
/*  440 */         if (counter == 1.0F)
/*      */         {
/*  442 */           performer.getCommunicator().sendNormalServerMessage("You start to preach about " + deity.name + ".");
/*  443 */           Server.getInstance().broadCastAction(performer.getName() + " starts to preach about " + deity.name + ".", performer, 5);
/*      */           
/*  445 */           performer.sendActionControl(Actions.actionEntrys[216].getVerbString(), true, 360);
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  450 */         else if (act.currentSecond() == 3)
/*      */         {
/*  452 */           performer.getCommunicator().sendNormalServerMessage("You clear your throat.");
/*  453 */           Server.getInstance().broadCastAction(performer
/*  454 */               .getName() + " clears " + performer.getHisHerItsString() + " throat.", performer, 5);
/*      */         }
/*  456 */         else if (act.currentSecond() == 6)
/*      */         {
/*  458 */           Methods.sendSound(performer, "sound.religion.preach");
/*  459 */           performer.say(deity.convertText1[0]);
/*      */         }
/*  461 */         else if (act.currentSecond() == 9)
/*      */         {
/*  463 */           performer.say(deity.convertText1[1]);
/*      */         }
/*  465 */         else if (act.currentSecond() == 12)
/*      */         {
/*  467 */           performer.say(deity.convertText1[2]);
/*      */         }
/*  469 */         else if (act.currentSecond() == 15)
/*      */         {
/*  471 */           performer.say(deity.convertText1[3]);
/*      */         }
/*  473 */         else if (act.currentSecond() == 18)
/*      */         {
/*  475 */           Methods.sendSound(performer, "sound.religion.preach");
/*  476 */           performer.say(deity.convertText1[4]);
/*      */         }
/*  478 */         else if (act.currentSecond() == 21)
/*      */         {
/*  480 */           performer.say(deity.convertText1[5]);
/*      */         }
/*  482 */         else if (act.currentSecond() == 24)
/*      */         {
/*  484 */           performer.say(deity.convertText1[6]);
/*      */         }
/*  486 */         else if (act.currentSecond() == 27)
/*      */         {
/*  488 */           Methods.sendSound(performer, "sound.religion.preach");
/*  489 */           performer.say(deity.convertText1[7]);
/*      */         }
/*  491 */         else if (act.currentSecond() == 30)
/*      */         {
/*  493 */           performer.say(deity.convertText1[8]);
/*      */         }
/*  495 */         else if (act.currentSecond() == 33)
/*      */         {
/*  497 */           Methods.sendSound(performer, "sound.religion.preach");
/*  498 */           performer.say(deity.convertText1[9]);
/*      */         }
/*  500 */         else if (act.currentSecond() >= 36)
/*      */         {
/*  502 */           performer.getCommunicator().sendNormalServerMessage("You end your speech.");
/*  503 */           Server.getInstance().broadCastAction(performer
/*  504 */               .getName() + " ends " + performer.getHisHerItsString() + " speech.", performer, 5);
/*  505 */           done = true;
/*  506 */           sendConvertQuestion(performer, responder, holyItem, counter);
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  512 */         done = true;
/*  513 */         performer.getCommunicator().sendNormalServerMessage("You stop preaching.");
/*      */       } 
/*      */     } 
/*  516 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void prayResult(Creature performer, float power, Deity deity, int rarity) {
/*  521 */     int num = Server.rand.nextInt(50 - rarity);
/*  522 */     String message = "Nothing special seems to happen.";
/*      */ 
/*      */     
/*  525 */     boolean effect = false;
/*  526 */     switch (num) {
/*      */ 
/*      */       
/*      */       case 0:
/*  530 */         if (performer.getBody().getWounds() != null) {
/*      */           
/*  532 */           Wound[] wounds = performer.getBody().getWounds().getWounds();
/*  533 */           if (wounds.length > 0) {
/*      */             
/*  535 */             message = deity.name + " heals you.";
/*  536 */             wounds[0].heal();
/*      */             
/*  538 */             effect = true;
/*      */           } 
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/*  545 */         if (performer.isPriest())
/*      */         {
/*  547 */           for (int x = Zones.safeTileX(performer.getTileX() - 1); x <= Zones.safeTileX(performer.getTileX() + 1); x++) {
/*      */             
/*  549 */             for (int y = Zones.safeTileY(performer.getTileY() - 1); y <= Zones.safeTileY(performer.getTileY() + 1); y++) {
/*      */               
/*  551 */               VolaTile t = Zones.getTileOrNull(x, y, performer.isOnSurface());
/*  552 */               if (t != null) {
/*      */                 
/*  554 */                 Creature[] crets = t.getCreatures();
/*  555 */                 if (crets.length > 0)
/*      */                 {
/*  557 */                   for (int c = 0; c < crets.length; c++) {
/*      */                     
/*  559 */                     if (crets[c] != performer)
/*      */                     {
/*  561 */                       if (crets[c].getAttitude(performer) == 1) {
/*      */                         
/*  563 */                         Wounds w = crets[c].getBody().getWounds();
/*  564 */                         if (w != null) {
/*      */ 
/*      */                           
/*  567 */                           Wound[] wounds = w.getWounds();
/*  568 */                           if (wounds.length > 0) {
/*      */                             
/*  570 */                             message = deity.name + " heals " + crets[c].getName() + ".";
/*  571 */                             crets[c].getCommunicator().sendNormalServerMessage("You feel a hot feeling near your wound and it suddenly goes away. Praise be " + deity.name + "!");
/*      */ 
/*      */                             
/*  574 */                             wounds[Server.rand.nextInt(wounds.length)].heal();
/*  575 */                             effect = true;
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                     }
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 2:
/*  590 */         effect = true;
/*  591 */         if (performer.getFaith() > 50.0F) {
/*      */           
/*  593 */           Item gem = TileRockBehaviour.createRandomGem(40.0F);
/*  594 */           if (performer.getInventory().getNumItemsNotCoins() < 100) {
/*  595 */             performer.getInventory().insertItem(gem, true);
/*      */           } else {
/*      */             
/*      */             try {
/*  599 */               gem.putItemInfrontof(performer);
/*      */             }
/*  601 */             catch (Exception nsz) {
/*      */               
/*  603 */               logger.log(Level.INFO, nsz.getMessage(), nsz);
/*      */             } 
/*  605 */           }  performer.achievement(622);
/*      */         }
/*      */         else {
/*      */           
/*  609 */           int templateId = 246 + Server.rand.nextInt(6);
/*      */ 
/*      */           
/*      */           try {
/*  613 */             Item mushroom = ItemFactory.createItem(templateId, power, deity.name);
/*  614 */             if (performer.getInventory().getNumItemsNotCoins() < 100) {
/*  615 */               performer.getInventory().insertItem(mushroom, true);
/*      */             } else {
/*  617 */               mushroom.putItemInfrontof(performer);
/*      */             } 
/*  619 */           } catch (Exception ex) {
/*      */             
/*  621 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */           } 
/*      */         } 
/*  624 */         message = deity.name + " puts something in your pocket.";
/*  625 */         performer.achievement(609);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 3:
/*  630 */         if (performer.isPriest()) {
/*      */ 
/*      */           
/*  633 */           effect = true;
/*      */           
/*      */           try {
/*  636 */             performer.setFavor(performer.getFavor() + 10.0F);
/*      */           }
/*  638 */           catch (IOException iox) {
/*      */             
/*  640 */             logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */           } 
/*  642 */           message = deity.name + " gives you some favor.";
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 4:
/*  648 */         if (performer.isPlayer() && performer.isPriest())
/*      */         {
/*  650 */           if (performer.getVehicle() == -10L) {
/*      */ 
/*      */             
/*  653 */             effect = true;
/*  654 */             ((Player)performer).setFarwalkerSeconds((byte)45);
/*  655 */             performer.getMovementScheme().setFarwalkerMoveMod(true);
/*  656 */             performer.getStatus().sendStateString();
/*  657 */             message = "Your legs tingle and you feel unstoppable.";
/*      */           } 
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 5:
/*  664 */         if (performer.isPlayer()) {
/*      */ 
/*      */           
/*  667 */           effect = true;
/*  668 */           for (int x = 0; x < power / 10.0F; x++) {
/*      */ 
/*      */             
/*      */             try {
/*  672 */               if (performer.getInventory().getItemCount() < 100) {
/*      */                 
/*  674 */                 Item coin = ItemFactory.createItem(51, Server.rand.nextFloat() * power, deity.name);
/*      */                 
/*  676 */                 performer.getInventory().insertItem(coin, true);
/*  677 */                 message = "Your pocket suddenly feels heavier.";
/*      */               }
/*      */               else {
/*      */                 
/*  681 */                 message = "You think you feel a faint tug in your inventory but perhaps because it's full nothing happens.";
/*      */                 
/*      */                 break;
/*      */               } 
/*  685 */             } catch (Exception ex) {
/*      */               
/*  687 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */             } 
/*      */           } 
/*  690 */           performer.achievement(609);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 6:
/*  696 */         if (performer.isPlayer()) {
/*      */ 
/*      */           
/*  699 */           effect = true;
/*  700 */           int skillNum = 100;
/*  701 */           if (deity.number == 1)
/*  702 */             skillNum = 103; 
/*  703 */           if (deity.number == 4 || deity.number == 2)
/*  704 */             skillNum = 102; 
/*  705 */           if (deity.number == 3) {
/*  706 */             skillNum = 100;
/*      */           }
/*      */           try {
/*  709 */             Skill toImprove = performer.getSkills().getSkill(skillNum);
/*  710 */             toImprove.setKnowledge(toImprove.getKnowledge() + 1.0E-4D, false);
/*  711 */             message = "You are blessed with some " + toImprove.getName() + ".";
/*      */           }
/*  713 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 7:
/*  722 */         if (performer.isPlayer() && power > 50.0F)
/*      */         {
/*  724 */           if (((Player)performer).getAlcoholAddiction() > 0L && 
/*  725 */             Server.rand.nextInt(100) == 0) {
/*      */ 
/*      */             
/*  728 */             effect = true;
/*  729 */             ((Player)performer).setAlcohol(0.0F);
/*  730 */             ((Player)performer).getSaveFile().setAlcoholTime(0L);
/*  731 */             message = "You sober up and your addiction is removed.";
/*      */           } 
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 8:
/*  738 */         if (performer.isPlayer())
/*      */         {
/*  740 */           if (performer.getPet() != null) {
/*      */ 
/*      */             
/*  743 */             effect = true;
/*  744 */             if (performer.getPower() > 0)
/*  745 */               performer.getCommunicator().sendNormalServerMessage("Loyalty before: " + performer
/*  746 */                   .getPet().getLoyalty()); 
/*  747 */             performer.getPet().setLoyalty(performer.getPet().getLoyalty() + 2.0F);
/*      */             
/*  749 */             message = performer.getPet().getName() + " suddenly seems more loyal.";
/*  750 */             if (performer.getPower() > 0) {
/*  751 */               performer.getCommunicator().sendNormalServerMessage("Loyalty after: " + performer
/*  752 */                   .getPet().getLoyalty());
/*      */             }
/*      */           } 
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 9:
/*  760 */         effect = true;
/*  761 */         performer.setPrayerSeconds(300);
/*  762 */         message = deity.name + " grants you faster favor gain.";
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/*  768 */         effect = false;
/*  769 */         message = "You send your prayers to " + deity.name + ".";
/*      */         break;
/*      */     } 
/*      */     
/*  773 */     performer.getCommunicator().sendNormalServerMessage(message);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean pray(Action act, Creature performer, float counter) {
/*  778 */     float faith = performer.getFaith();
/*  779 */     if (performer.getDeity() == null) {
/*      */       
/*  781 */       performer.getCommunicator().sendNormalServerMessage("You have no deity, so you cannot pray here.");
/*  782 */       return true;
/*      */     } 
/*  784 */     Deity deity = performer.getDeity();
/*  785 */     boolean done = false;
/*      */     
/*  787 */     int time = act.getTimeLeft();
/*      */     
/*  789 */     if (act.mayPlaySound())
/*      */     {
/*  791 */       Methods.sendSound(performer, "sound.religion.prayer");
/*      */     }
/*  793 */     if (counter == 1.0F) {
/*      */       
/*  795 */       Skill prayer = null;
/*      */       
/*      */       try {
/*  798 */         prayer = performer.getSkills().getSkill(10066);
/*      */       }
/*  800 */       catch (NoSuchSkillException nss) {
/*      */         
/*  802 */         prayer = performer.getSkills().learn(10066, 1.0F);
/*      */       } 
/*  804 */       double mod = prayer.getKnowledge(0.0D);
/*  805 */       time = (int)(300.0D - mod);
/*  806 */       performer.getCommunicator().sendNormalServerMessage("You start to pray.");
/*  807 */       Server.getInstance().broadCastAction(performer.getName() + " starts to pray.", performer, 5);
/*  808 */       performer.sendActionControl(Actions.actionEntrys[141].getVerbString(), true, time);
/*  809 */       act.setTimeLeft(time);
/*  810 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     }
/*  812 */     else if (counter * 10.0F >= time) {
/*      */       
/*  814 */       if (act.getRarity() > 0)
/*      */       {
/*  816 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/*  818 */       performer.getStatus().modifyStamina(-3000.0F);
/*  819 */       Skill prayer = null;
/*      */       
/*      */       try {
/*  822 */         prayer = performer.getSkills().getSkill(10066);
/*      */       }
/*  824 */       catch (NoSuchSkillException nss) {
/*      */         
/*  826 */         prayer = performer.getSkills().learn(10066, 1.0F);
/*      */       } 
/*  828 */       double result = prayer.skillCheck(prayer.getKnowledge(0.0D) - (30.0F + Server.rand.nextFloat() * 60.0F), faith, false, counter / 3.0F);
/*      */       
/*  830 */       if (result > 0.0D)
/*      */       {
/*  832 */         prayResult(performer, (float)result, deity, act.getRarity());
/*      */       }
/*      */ 
/*      */       
/*  836 */       if (performer.isPlayer())
/*      */       {
/*  838 */         RiteEvent.checkRiteRewards((Player)performer);
/*      */       }
/*      */ 
/*      */       
/*  842 */       Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*  843 */       templateDeity.increaseFavor();
/*  844 */       done = true;
/*  845 */       performer.getCommunicator().sendNormalServerMessage("You finish your prayer to " + deity.name + ".");
/*  846 */       Server.getInstance().broadCastAction(performer
/*  847 */           .getName() + " finishes " + performer.getHisHerItsString() + " prayer to " + deity.name + ".", performer, 5);
/*      */     } 
/*      */     
/*  850 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean pray(Action act, Creature performer, Item altar, float counter) {
/*  855 */     float faith = performer.getFaith();
/*      */     
/*  857 */     if (performer.getDeity() == null) {
/*      */       
/*  859 */       performer.getCommunicator().sendNormalServerMessage("You have no deity, so you cannot pray here.");
/*  860 */       return true;
/*      */     } 
/*  862 */     Deity deity = altar.getBless();
/*  863 */     if (deity == null) {
/*      */       
/*  865 */       performer.getCommunicator().sendNormalServerMessage("The " + altar
/*  866 */           .getName() + " has no deity, so you cannot pray here.");
/*  867 */       return true;
/*      */     } 
/*  869 */     if (performer.getDeity() != deity)
/*      */     {
/*  871 */       if ((!performer.getDeity().isHateGod() && altar.getTemplateId() != 327) || (altar.getTemplateId() != 328 && performer.getDeity().isHateGod())) {
/*      */         
/*  873 */         performer.getCommunicator().sendNormalServerMessage("You cannot pray at altars belonging to " + deity.name + ".");
/*      */         
/*  875 */         return true;
/*      */       } 
/*      */     }
/*  878 */     if (altar.getParentId() != -10L) {
/*      */       
/*  880 */       performer.getCommunicator().sendNormalServerMessage("The altar needs to be on the ground to be used.");
/*      */       
/*  882 */       return true;
/*      */     } 
/*  884 */     boolean done = false;
/*      */     
/*  886 */     int time = act.getTimeLeft();
/*      */     
/*  888 */     if (counter == 1.0F) {
/*      */       
/*  890 */       Skill prayer = null;
/*      */       
/*      */       try {
/*  893 */         prayer = performer.getSkills().getSkill(10066);
/*      */       }
/*  895 */       catch (NoSuchSkillException nss) {
/*      */         
/*  897 */         prayer = performer.getSkills().learn(10066, 1.0F);
/*      */       } 
/*  899 */       double mod = prayer.getKnowledge(0.0D);
/*  900 */       time = (int)(300.0D - mod - (altar.getCurrentQualityLevel() / 10.0F));
/*      */       
/*      */       try {
/*  903 */         performer.getSkills().getSkill(10066);
/*      */       }
/*  905 */       catch (NoSuchSkillException nss) {
/*      */         
/*  907 */         performer.getSkills().learn(10066, 1.0F);
/*      */       } 
/*  909 */       performer.getCommunicator().sendNormalServerMessage("You start to pray at the " + altar.getName() + ".");
/*  910 */       Server.getInstance().broadCastAction(performer.getName() + " starts to pray at the " + altar.getName() + ".", performer, 5);
/*      */ 
/*      */       
/*  913 */       act.setTimeLeft(time);
/*  914 */       performer.sendActionControl(Actions.actionEntrys[141].getVerbString(), true, time);
/*      */       
/*  916 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     } 
/*  918 */     if (counter * 10.0F >= time) {
/*      */       
/*  920 */       if (act.getRarity() > 0)
/*      */       {
/*  922 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/*  924 */       performer.getStatus().modifyStamina(-3000.0F);
/*  925 */       Methods.sendSound(performer, "sound.religion.prayer");
/*  926 */       Skill prayer = null;
/*      */       
/*      */       try {
/*  929 */         prayer = performer.getSkills().getSkill(10066);
/*      */       }
/*  931 */       catch (NoSuchSkillException nss) {
/*      */         
/*  933 */         prayer = performer.getSkills().learn(10066, 1.0F);
/*      */       } 
/*  935 */       double result = prayer.skillCheck(prayer
/*  936 */           .getKnowledge(0.0D) - (30.0F + Server.rand.nextFloat() * 60.0F) - (altar.getCurrentQualityLevel() / 10.0F), faith, false, counter / 3.0F);
/*      */       
/*  938 */       if (result > 0.0D)
/*      */       {
/*  940 */         prayResult(performer, (float)result, deity, act.getRarity() + altar.getRarity());
/*      */       }
/*      */ 
/*      */       
/*  944 */       if (performer.isPlayer())
/*      */       {
/*  946 */         RiteEvent.checkRiteRewards((Player)performer);
/*      */       }
/*      */ 
/*      */       
/*  950 */       Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*  951 */       templateDeity.increaseFavor();
/*  952 */       if (altar.isHugeAltar())
/*      */       {
/*  954 */         if (performer.getDeity().isHateGod()) {
/*  955 */           performer.maybeModifyAlignment((-3 - altar.getRarity()));
/*      */         } else {
/*  957 */           performer.maybeModifyAlignment((3 + altar.getRarity()));
/*      */         }  } 
/*  959 */       done = true;
/*  960 */       performer.getCommunicator().sendNormalServerMessage("You finish your prayer to " + (performer.getDeity()).name + ".");
/*  961 */       Server.getInstance().broadCastAction(performer
/*  962 */           .getName() + " finishes " + performer.getHisHerItsString() + " prayer to " + 
/*  963 */           (performer.getDeity()).name + ".", performer, 5);
/*  964 */       if (performer.checkPrayerFaith()) {
/*      */         
/*  966 */         if (performer.getFaith() < 10.0F || Server.rand.nextInt(5) == 0) {
/*  967 */           performer.getCommunicator().sendNormalServerMessage("You feel calm and solemn.");
/*  968 */         } else if (performer.getFaith() < 20.0F || Server.rand.nextInt(5) == 0) {
/*  969 */           performer.getCommunicator().sendNormalServerMessage("You feel sincere devotion to " + 
/*  970 */               (performer.getDeity()).name + ".");
/*  971 */         } else if (performer.getFaith() < 30.0F || Server.rand.nextInt(5) == 0) {
/*  972 */           performer.getCommunicator().sendNormalServerMessage("You are relying on " + 
/*  973 */               (performer.getDeity()).name + " to help you.");
/*  974 */         } else if (performer.getFaith() < 40.0F || Server.rand.nextInt(5) == 0) {
/*  975 */           performer.getCommunicator().sendNormalServerMessage("Was that a sudden gust of wind? Maybe " + 
/*  976 */               (performer.getDeity()).name + " is pleased with your devotion?");
/*      */         }
/*  978 */         else if (performer.getFaith() < 60.0F || Server.rand.nextInt(5) == 0) {
/*  979 */           performer.getCommunicator().sendNormalServerMessage("You can almost feel that an envoy of " + 
/*  980 */               (performer.getDeity()).name + " is watching you.");
/*  981 */         } else if (performer.getFaith() < 80.0F || Server.rand.nextInt(5) == 0) {
/*  982 */           performer.getCommunicator().sendNormalServerMessage("You have a feeling that " + 
/*  983 */               (performer.getDeity()).name + " is here with you now.");
/*      */         } else {
/*  985 */           performer.getCommunicator().sendNormalServerMessage("Deep in your heart, you feel certain that " + 
/*  986 */               (performer.getDeity()).name + " is pleased with you and will protect you.");
/*      */         } 
/*  988 */         if (Server.rand.nextInt(10) == 0)
/*      */         {
/*  990 */           if (performer.getMusicPlayer() != null)
/*      */           {
/*  992 */             performer.getMusicPlayer().playPrayer();
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*  997 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean canBeSacrificed(Item item) {
/* 1002 */     if (item.isArtifact() || item.isUnique())
/* 1003 */       return false; 
/* 1004 */     if ((item.isNoDrop() || item.isNoTrade() || item.isLocked() || item.isIndestructible()) && item
/* 1005 */       .getTemplateId() != 168 && 
/* 1006 */       !item.isCoin() && 
/* 1007 */       !item.isLock())
/* 1008 */       return false; 
/* 1009 */     return true;
/*      */   }
/*      */   
/*      */   public static float getFavorModifier(@Nullable Deity deity, @Nonnull Item item) {
/* 1013 */     float mod = 1.0F;
/* 1014 */     if (deity != null)
/*      */     {
/* 1016 */       if (item.isWood() && deity.isWoodAffinity()) {
/* 1017 */         mod = 2.0F;
/* 1018 */       } else if (item.isMetal() && deity.isMetalAffinity()) {
/* 1019 */         mod = 2.0F;
/* 1020 */       } else if (item.isCloth() && deity.isClothAffinity()) {
/* 1021 */         mod = 2.0F;
/* 1022 */       } else if ((item.getAlchemyType() > 0 || item.getTemplateId() == 272) && deity
/* 1023 */         .isMeatAffinity()) {
/* 1024 */         if (item.getAlchemyType() > 0)
/* 1025 */           if (item.isFood() && item.isChopped()) {
/* 1026 */             mod = 2.5F;
/*      */           } else {
/* 1028 */             mod = 2.0F;
/*      */           }  
/* 1030 */       } else if (item.isFood() && deity.isFoodAffinity()) {
/* 1031 */         if (item.isHighNutrition())
/* 1032 */         { mod = 3.0F; }
/* 1033 */         else if (item.isGoodNutrition())
/* 1034 */         { mod = 2.5F; }
/* 1035 */         else if (item.isMediumNutrition())
/* 1036 */         { mod = 2.0F; }
/* 1037 */         else if (item.isLowNutrition())
/* 1038 */         { mod = 1.0F; } 
/* 1039 */       } else if (item.isPottery() && deity.isClayAffinity()) {
/* 1040 */         mod = 2.0F;
/*      */       } 
/*      */     }
/* 1043 */     if (mod <= 1.0F)
/*      */     {
/* 1045 */       if (item.isFood() && item.isChopped())
/* 1046 */         mod = 2.0F; 
/*      */     }
/* 1048 */     return mod;
/*      */   }
/*      */   
/*      */   public static float getFavorValue(@Nullable Deity deity, @Nonnull Item item) {
/* 1052 */     float newVal = item.getValue();
/* 1053 */     if (deity != null)
/*      */     {
/* 1055 */       if ((item.getAlchemyType() > 0 || item.getTemplateId() == 272) && deity
/* 1056 */         .isMeatAffinity()) {
/* 1057 */         if (item.getTemplateId() == 272) {
/* 1058 */           newVal = (int)(10000.0F * item.getCurrentQualityLevel() / 100.0F);
/* 1059 */         } else if (item.getAlchemyType() > 0) {
/* 1060 */           if (item.isFood() && item.isChopped()) {
/* 1061 */             newVal = (int)(2500.0F * item.getCurrentQualityLevel() / 100.0F);
/*      */           } else {
/* 1063 */             newVal = (int)((1500 * item.getAlchemyType()) * item.getCurrentQualityLevel() / 100.0F);
/*      */           } 
/*      */         } 
/* 1066 */       } else if (item.isFood() && deity.isFoodAffinity() && 
/* 1067 */         item.isDish() && item.getFoodComplexity() > 0.0F) {
/* 1068 */         newVal = (int)(2500.0F * item.getFoodComplexity() * item.getCurrentQualityLevel() / 100.0F);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1073 */     if (item.isFood() && item.isChopped())
/*      */     {
/* 1075 */       newVal = (int)(2500.0F * item.getCurrentQualityLevel() / 100.0F);
/*      */     }
/* 1077 */     return newVal;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean sacrifice(Action action, Creature performer, Item altar) {
/* 1082 */     boolean done = false;
/*      */     
/* 1084 */     if (performer.getDeity() == null) {
/*      */       
/* 1086 */       performer.getCommunicator().sendNormalServerMessage("You have no deity, so you cannot sacrifice here.");
/* 1087 */       return true;
/*      */     } 
/* 1089 */     if (performer.getCurrentVillage() != null)
/*      */     {
/* 1091 */       if (performer.getCurrentVillage().isEnemy(performer)) {
/*      */         
/* 1093 */         performer.getCommunicator().sendNormalServerMessage("You can not sacrifice here.");
/* 1094 */         return true;
/*      */       } 
/*      */     }
/* 1097 */     Deity deity = altar.getBless();
/* 1098 */     if (deity == null) {
/*      */       
/* 1100 */       performer.getCommunicator().sendNormalServerMessage("The " + altar
/* 1101 */           .getName() + " has no deity, so you cannot perform rituals here.");
/* 1102 */       return true;
/*      */     } 
/* 1104 */     if (performer.getDeity() != deity)
/*      */     {
/* 1106 */       if ((performer.getDeity()).number == 4 || altar.getTemplateId() != 327) {
/*      */         
/* 1108 */         performer.getCommunicator().sendNormalServerMessage("You cannot perform rituals at altars belonging to " + deity.name + ".");
/*      */         
/* 1110 */         return true;
/*      */       } 
/*      */     }
/* 1113 */     if (altar.getParentId() != -10L) {
/*      */       
/* 1115 */       performer.getCommunicator().sendNormalServerMessage("The altar needs to be on the ground to be used.");
/*      */       
/* 1117 */       return true;
/*      */     } 
/* 1119 */     if (action.currentSecond() == 1) {
/*      */       
/* 1121 */       performer.getCommunicator().sendNormalServerMessage("You start to sacrifice at the " + altar.getName() + ".");
/* 1122 */       Server.getInstance().broadCastAction(performer
/* 1123 */           .getName() + " starts to sacrifice at the " + altar.getName() + ".", performer, 5);
/*      */       
/* 1125 */       performer.sendActionControl(Actions.actionEntrys[142].getVerbString(), true, 300);
/*      */     
/*      */     }
/* 1128 */     else if (action.currentSecond() == 5) {
/*      */       
/* 1130 */       performer.getCommunicator().sendNormalServerMessage("You call out to " + deity.name + " by all " + deity
/* 1131 */           .getHisHerItsString() + " names.");
/* 1132 */       Server.getInstance().broadCastAction(performer
/* 1133 */           .getName() + " calls out to " + deity.name + " by all " + deity.getHisHerItsString() + " names.", performer, 5);
/*      */     
/*      */     }
/* 1136 */     else if (action.currentSecond() == 10) {
/*      */       
/* 1138 */       performer.getCommunicator().sendNormalServerMessage("You beg " + deity.name + " to accept your offerings.");
/* 1139 */       Server.getInstance().broadCastAction(performer
/* 1140 */           .getName() + " begs " + deity.name + " to accept " + performer.getHisHerItsString() + " offerings.", performer, 5);
/*      */     
/*      */     }
/* 1143 */     else if (action.currentSecond() == 15) {
/*      */       
/* 1145 */       performer.getCommunicator().sendNormalServerMessage("You ask " + deity.name + " for forgiveness if some of the items do not please " + deity
/*      */           
/* 1147 */           .getHimHerItString() + ".");
/* 1148 */       Server.getInstance().broadCastAction(performer
/* 1149 */           .getName() + " asks " + deity.name + " for forgiveness if some of the items do not please " + deity
/* 1150 */           .getHimHerItString() + ".", performer, 5);
/*      */     }
/* 1152 */     else if (action.currentSecond() == 20) {
/*      */       
/* 1154 */       performer.getCommunicator().sendNormalServerMessage("You ask " + deity.name + " to bless you with " + deity
/* 1155 */           .getHisHerItsString() + " favors.");
/* 1156 */       Server.getInstance().broadCastAction(performer
/* 1157 */           .getName() + " asks " + deity.name + " to bless " + performer.getHimHerItString() + " with " + deity
/* 1158 */           .getHisHerItsString() + " favors.", performer, 5);
/*      */     }
/* 1160 */     else if (action.currentSecond() == 25) {
/*      */       
/* 1162 */       performer.getCommunicator().sendNormalServerMessage("You kneel at the altar in silent hope.");
/* 1163 */       Server.getInstance().broadCastAction(performer.getName() + " kneels at the altar in silence.", performer, 5);
/*      */     }
/* 1165 */     else if (action.currentSecond() > 30) {
/*      */       
/* 1167 */       done = true;
/* 1168 */       Item[] items = altar.getAllItems(false);
/* 1169 */       float favor = 0.0F;
/* 1170 */       boolean corpse = false;
/* 1171 */       if (items.length > 0) {
/*      */         
/* 1173 */         int value = 0;
/*      */         
/* 1175 */         Village v = performer.getCitizenVillage();
/* 1176 */         int bestRarity = 0;
/* 1177 */         float bestQL = 0.0F;
/* 1178 */         int startNut = 0;
/* 1179 */         for (int x = 0; x < items.length; x++) {
/*      */           
/* 1181 */           if (!canBeSacrificed(items[x])) {
/*      */             
/* 1183 */             performer.getCommunicator().sendNormalServerMessage(deity.name + " does not accept " + items[x]
/* 1184 */                 .getNameWithGenus() + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1192 */           else if (items[x].isBanked()) {
/*      */             
/* 1194 */             performer.getCommunicator().sendNormalServerMessage(deity.name + " does not accept " + items[x]
/* 1195 */                 .getNameWithGenus() + ".");
/* 1196 */             logger.log(Level.WARNING, performer.getName() + " tried to sac banked item!");
/*      */             
/*      */             try {
/* 1199 */               Item parent = items[x].getParent();
/* 1200 */               parent.dropItem(items[x].getWurmId(), false);
/*      */             }
/* 1202 */             catch (NoSuchItemException nsi) {
/*      */               
/* 1204 */               logger.log(Level.WARNING, "Coin lacks parent when sacrificed.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1220 */           else if (!items[x].isPlacedOnParent() && !items[x].isInsidePlacedContainer()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1276 */             float newVal = getFavorValue(deity, items[x]);
/* 1277 */             float mod = getFavorModifier(deity, items[x]);
/* 1278 */             value = (int)(value + Math.max(10.0F, newVal * mod));
/* 1279 */             if (items[x].getTemplateId() == 272) {
/* 1280 */               corpse = true;
/*      */             }
/* 1282 */             if (performer.getFaith() <= performer.getFavor())
/*      */             {
/* 1284 */               if (v != null) {
/*      */                 
/* 1286 */                 float enchVal = Math.max(10, items[x].getValue()) * mod;
/* 1287 */                 if (items[x].getTemplateId() == 683 || items[x]
/* 1288 */                   .getTemplateId() == 737) {
/* 1289 */                   enchVal = 200000.0F * mod;
/*      */                 }
/* 1291 */                 ItemSpellEffects sp = items[x].getSpellEffects();
/* 1292 */                 if (sp != null) {
/*      */                   
/* 1294 */                   SpellEffect[] spefs = sp.getEffects();
/* 1295 */                   for (int s = 0; s < spefs.length; s++) {
/*      */                     
/* 1297 */                     if (spefs[s].getSpellInfluenceType() != 1)
/*      */                     {
/* 1299 */                       enchVal += (spefs[s]).power * 100.0F;
/*      */                     }
/*      */                   } 
/*      */                 } 
/* 1303 */                 if (items[x].isWeapon() || items[x].isArmour() || items[x].isShield() || items[x]
/* 1304 */                   .isWeaponBow() || items[x].isBowUnstringed()) {
/*      */                   
/* 1306 */                   v.setFaithWar(v.getFaithWarValue() + enchVal / 100000.0F);
/* 1307 */                   if (enchVal > 100000.0F) {
/* 1308 */                     v.addHistory(performer.getName(), " adds " + (enchVal / 100000.0F) + " to war faith bonus");
/*      */                   }
/*      */                 }
/* 1311 */                 else if (items[x].isFood() || items[x].isHealing() || items[x]
/* 1312 */                   .getMaterial() == 22 || items[x]
/* 1313 */                   .getMaterial() == 55) {
/*      */                   
/* 1315 */                   if (items[x].isLowNutrition()) {
/* 1316 */                     enchVal *= 10.0F;
/* 1317 */                   } else if (items[x].isMediumNutrition()) {
/* 1318 */                     enchVal *= 20.0F;
/*      */                   } else {
/* 1320 */                     enchVal *= 30.0F;
/* 1321 */                   }  v.setFaithHeal(v.getFaithHealValue() + enchVal / 100000.0F);
/* 1322 */                   if (enchVal > 100000.0F) {
/* 1323 */                     v.addHistory(performer.getName(), " adds " + (enchVal / 100000.0F) + " to healing faith bonus");
/*      */                   }
/*      */                 }
/*      */                 else {
/*      */                   
/* 1328 */                   if (items[x].isStone() || items[x].isWood() || items[x].isMetal()) {
/* 1329 */                     enchVal = Math.max(100.0F, Math.max(enchVal, (items[x].getWeightGrams() / 20)));
/*      */                   } else {
/* 1331 */                     enchVal = Math.max(enchVal, 200.0F);
/* 1332 */                   }  v.setFaithCreate(v.getFaithCreateValue() + enchVal / 100000.0F);
/* 1333 */                   if (enchVal > 100000.0F) {
/* 1334 */                     v.addHistory(performer.getName(), " adds " + (enchVal / 1000000.0F) + " to enchant faith bonus");
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             }
/*      */             
/* 1340 */             if (items[x].getTemplateId() == 683 || items[x]
/* 1341 */               .getTemplateId() == 737)
/*      */             {
/* 1343 */               if (performer.getFavor() < performer.getFaith())
/*      */                 
/*      */                 try {
/* 1346 */                   performer.setFavor(performer.getFavor() + performer.getFaith() / 10.0F);
/*      */                 }
/* 1348 */                 catch (IOException iox) {
/*      */                   
/* 1350 */                   logger.log(Level.WARNING, performer.getName() + ", " + iox.getMessage());
/*      */                 }  
/*      */             }
/* 1353 */             if (items[x].getRarity() > 0)
/*      */             {
/* 1355 */               if (items[x].getTemplate() != null)
/*      */               {
/* 1357 */                 if (items[x].getWeightGrams() >= items[x].getTemplate().getWeightGrams() || items[x]
/* 1358 */                   .getTemplate().isFish()) {
/*      */                   
/* 1360 */                   if (items[x].getRarity() == 1) {
/*      */                     
/* 1362 */                     performer.healRandomWound(100);
/* 1363 */                     if (startNut < 50) {
/* 1364 */                       startNut = 50;
/*      */                     }
/* 1366 */                   } else if (items[x].getRarity() == 2) {
/*      */                     
/* 1368 */                     Skills skills = performer.getSkills();
/* 1369 */                     Skill[] skillarr = skills.getSkills();
/* 1370 */                     Skill s = skillarr[Server.rand.nextInt(skillarr.length)];
/* 1371 */                     double diffToMax = 100.0D - s.getKnowledge();
/* 1372 */                     float divider = 1000.0F;
/* 1373 */                     if (s.getType() == 1 || s.getType() == 0)
/* 1374 */                       divider *= 20.0F; 
/* 1375 */                     s.setKnowledge(s.getKnowledge() + diffToMax / divider, false);
/* 1376 */                     if (startNut < 70) {
/* 1377 */                       startNut = 70;
/*      */                     }
/* 1379 */                   } else if (items[x].getRarity() == 3) {
/*      */                     
/* 1381 */                     Skills skills = performer.getSkills();
/* 1382 */                     Skill[] skillarr = skills.getSkills();
/* 1383 */                     Skill s = skillarr[Server.rand.nextInt(skillarr.length)];
/* 1384 */                     double diffToMax = 100.0D - s.getKnowledge();
/* 1385 */                     float divider = 300.0F;
/* 1386 */                     if (s.getType() == 1 || s.getType() == 0)
/* 1387 */                       divider *= 20.0F; 
/* 1388 */                     s.setKnowledge(s.getKnowledge() + Math.min(1.0D, diffToMax / divider), false);
/* 1389 */                     performer.getBody().healFully();
/* 1390 */                     if (startNut < 90)
/* 1391 */                       startNut = 90; 
/*      */                   } 
/* 1393 */                   if (items[x].getRarity() > bestRarity)
/* 1394 */                     bestRarity = items[x].getRarity(); 
/* 1395 */                   if (items[x].getCurrentQualityLevel() > bestQL)
/* 1396 */                     bestQL = items[x].getCurrentQualityLevel(); 
/*      */                 } 
/*      */               }
/*      */             }
/* 1400 */             if (items[x].isCoin()) {
/*      */ 
/*      */               
/*      */               try {
/* 1404 */                 Item parent = items[x].getParent();
/* 1405 */                 parent.dropItem(items[x].getWurmId(), false);
/*      */               }
/* 1407 */               catch (NoSuchItemException nsi) {
/*      */                 
/* 1409 */                 logger.log(Level.WARNING, "Coin lacks parent when sacrificed.");
/*      */               } 
/* 1411 */               Economy.getEconomy().returnCoin(items[x], "Sacrificed");
/*      */             }
/*      */             else {
/*      */               
/* 1415 */               if (items[x].getTemplate().isMissionItem() && (items[x].getQualityLevel() >= 30.0F || items[x]
/* 1416 */                 .getTemplateId() == 683 || items[x].getTemplateId() == 737))
/* 1417 */                 MissionTriggers.activateTriggers(performer, items[x], 142, -10L, 1); 
/* 1418 */               Items.destroyItem(items[x].getWurmId());
/*      */             } 
/* 1420 */             performer.achievement(607);
/* 1421 */             if (newVal * mod / 1000.0F >= 30.0F)
/* 1422 */               performer.achievement(620); 
/*      */           } 
/*      */         } 
/* 1425 */         if (bestRarity > 0) {
/*      */ 
/*      */           
/* 1428 */           float nut = (startNut + bestQL * (100 - startNut) / 100.0F) / 100.0F;
/* 1429 */           performer.getStatus().refresh(nut, true);
/*      */         } 
/*      */         
/* 1432 */         favor = value / 1000.0F;
/* 1433 */         if (value < 100 && value > 0) {
/*      */           
/* 1435 */           performer.getCommunicator().sendNormalServerMessage(deity.name + " kindly accepts your offerings.");
/* 1436 */           Server.getInstance().broadCastAction(performer
/* 1437 */               .getName() + " looks pleased as " + deity.name + " accepts " + performer
/* 1438 */               .getHisHerItsString() + " offerings.", performer, 5);
/*      */         }
/* 1440 */         else if (value > 0) {
/*      */           
/* 1442 */           if (value < 50000) {
/*      */             
/* 1444 */             if (value > 10000) {
/*      */               
/* 1446 */               if (Server.rand.nextInt(20) == 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1450 */                   Item i = ItemFactory.createItem(5, Server.rand.nextInt(100), deity.name);
/*      */                   
/* 1452 */                   performer.getInventory().insertItem(i);
/*      */                 }
/* 1454 */                 catch (NoSuchTemplateException mso) {
/*      */                   
/* 1456 */                   logger.log(Level.WARNING, performer.getName() + ": " + mso.getMessage(), (Throwable)mso);
/*      */                 }
/* 1458 */                 catch (FailedException fe) {
/*      */                   
/* 1460 */                   logger.log(Level.WARNING, performer.getName() + ": " + fe.getMessage(), (Throwable)fe);
/*      */                 } 
/*      */               }
/* 1463 */               if (performer.getDeity().isHateGod()) {
/* 1464 */                 performer.maybeModifyAlignment(-1.0F);
/*      */               } else {
/* 1466 */                 performer.maybeModifyAlignment(1.0F);
/*      */               } 
/* 1468 */             }  SoundPlayer.playSound("sound.fx.humm", altar, 1.0F);
/*      */             
/* 1470 */             performer.getCommunicator().sendNormalServerMessage("A strange sound is emitted from the " + altar
/* 1471 */                 .getName() + " as " + deity.name + " gladly accepts your offerings.");
/*      */             
/* 1473 */             Server.getInstance().broadCastAction(performer
/* 1474 */                 .getName() + " looks awed as " + deity.name + " accepts " + performer
/* 1475 */                 .getHisHerItsString() + " offerings.", performer, 5);
/*      */           } else {
/*      */             String sn;
/*      */             
/* 1479 */             performer.getBody().healFully();
/*      */             
/* 1481 */             if (deity.sex == 1) {
/* 1482 */               sn = "sound.fx.ooh.female";
/*      */             } else {
/* 1484 */               sn = "sound.fx.ooh.male";
/* 1485 */             }  SoundPlayer.playSound(sn, altar, 1.0F);
/* 1486 */             if (value < 100000) {
/*      */               
/* 1488 */               performer.getCommunicator().sendNormalServerMessage(deity.name + " is very pleased with your offerings.");
/*      */               
/* 1490 */               Server.getInstance().broadCastAction(performer
/* 1491 */                   .getName() + " looks very happy as " + deity.name + " accepts " + performer
/* 1492 */                   .getHisHerItsString() + " offerings.", performer, 5);
/*      */             }
/*      */             else {
/*      */               
/* 1496 */               EndGameItems.locateRandomEndGameItem(performer);
/* 1497 */               if (value < 1000000) {
/*      */                 
/* 1499 */                 performer.getCommunicator().sendNormalServerMessage(deity.name + " is surprised by your generous offerings.");
/*      */                 
/* 1501 */                 Server.getInstance().broadCastAction(performer
/* 1502 */                     .getName() + " looks very happy as " + deity.name + " accepts " + performer
/* 1503 */                     .getHisHerItsString() + " offerings.", performer, 5);
/*      */               }
/*      */               else {
/*      */                 
/* 1507 */                 performer.getCommunicator().sendNormalServerMessage(deity.name + " is extremely satisfied with your generous offerings.");
/*      */                 
/* 1509 */                 Server.getInstance().broadCastAction(performer
/* 1510 */                     .getName() + " looks very happy as " + deity.name + " accepts " + performer
/* 1511 */                     .getHisHerItsString() + " offerings.", performer, 5);
/*      */                 
/* 1513 */                 Skill channeling = performer.getChannelingSkill();
/* 1514 */                 if (channeling != null) {
/*      */                   
/* 1516 */                   double knowledge = channeling.getKnowledge();
/* 1517 */                   if (knowledge < 100.0D)
/* 1518 */                     channeling.setKnowledge(knowledge + (100.0D - knowledge) / 3000.0D, false); 
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 1524 */         if (corpse && altar.isHugeAltar() && 
/* 1525 */           performer.getDeity().isHateGod()) {
/* 1526 */           performer.maybeModifyAlignment(-1.0F);
/*      */         }
/*      */       } else {
/*      */         
/* 1530 */         performer.getCommunicator().sendNormalServerMessage(deity.name + " seems upset as the altar is empty!");
/*      */       } 
/*      */       
/*      */       try {
/* 1534 */         performer.setFavor(performer.getFavor() + favor);
/*      */       }
/* 1536 */       catch (IOException iox) {
/*      */         
/* 1538 */         logger.log(Level.WARNING, performer.getName() + " " + iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/*      */     
/* 1542 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean sacrifice(Creature performer, Creature target, Item source, Action action, float counter) {
/* 1548 */     boolean done = true;
/* 1549 */     if (target.isDead()) {
/*      */       
/* 1551 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1552 */           .getName() + " is dead! How can you sacrifice a dead parrot?");
/* 1553 */       return true;
/*      */     } 
/* 1555 */     if (performer.getDeity() == null) {
/*      */       
/* 1557 */       performer.getCommunicator().sendNormalServerMessage("You have no deity, so you cannot sacrifice here.");
/* 1558 */       return true;
/*      */     } 
/* 1560 */     double diff = performer.getFightingSkill().getKnowledge(0.0D) / 4.0D - target.getBaseCombatRating();
/* 1561 */     int damageMin = (int)Math.min(32767.0D, 65535.0D - (65535.0F / target.getBaseCombatRating()) * diff);
/* 1562 */     int damageDiff = (target.getStatus()).damage - damageMin + 32767;
/* 1563 */     if ((target.getStatus()).damage < damageMin) {
/*      */       
/* 1565 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is still strong enough to resist your attempts.");
/* 1566 */       return true;
/*      */     } 
/* 1568 */     if (performer.getCurrentVillage() != null)
/*      */     {
/* 1570 */       if (performer.getCurrentVillage().isEnemy(performer)) {
/*      */         
/* 1572 */         performer.getCommunicator().sendNormalServerMessage("You can not sacrifice here.");
/* 1573 */         return true;
/*      */       } 
/*      */     }
/* 1576 */     Deity deity = null;
/*      */     
/*      */     try {
/* 1579 */       FaithZone z = Zones.getFaithZone(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 1580 */       if (z != null)
/*      */       {
/* 1582 */         deity = z.getCurrentRuler();
/*      */       }
/*      */       else
/*      */       {
/* 1586 */         performer.getCommunicator().sendNormalServerMessage("You cannot feel the presence of " + performer.getDeity().getName() + " here. Sacrificing the " + target
/* 1587 */             .getName() + " here would do nothing.");
/* 1588 */         return true;
/*      */       }
/*      */     
/* 1591 */     } catch (NoSuchZoneException e) {
/*      */       
/* 1593 */       performer.getCommunicator().sendNormalServerMessage("This area feels weird. Sacrificing the " + target.getName() + " here would do nothing.");
/* 1594 */       return true;
/*      */     } 
/* 1596 */     if (deity == null) {
/*      */       
/* 1598 */       performer.getCommunicator().sendNormalServerMessage("Something is wrong with this location. You think sacrificng in another spot would be better.");
/* 1599 */       return true;
/*      */     } 
/*      */     
/* 1602 */     done = false;
/* 1603 */     if (action.currentSecond() == 1) {
/*      */       
/* 1605 */       done = true;
/*      */       
/* 1607 */       MissionTrigger[] triggers = MissionTriggers.getMissionTriggersWith(-target.getTemplate().getTemplateId(), 142, -10L);
/* 1608 */       for (MissionTrigger t : triggers) {
/*      */         
/* 1610 */         if (!t.isInactive())
/*      */         {
/*      */           
/* 1613 */           done = false;
/*      */         }
/*      */       } 
/* 1616 */       if (!done || target.getTemplate().isEpicMissionSlayable())
/*      */       {
/* 1618 */         performer.getCommunicator().sendNormalServerMessage("You start to sacrifice the " + target.getName() + ".");
/* 1619 */         Server.getInstance().broadCastAction(performer.getName() + " starts to sacrifice the " + target
/* 1620 */             .getName() + ".", performer, 5);
/*      */         
/* 1622 */         performer.sendActionControl(Actions.actionEntrys[142].getVerbString(), true, 120);
/* 1623 */         done = false;
/*      */       }
/*      */       else
/*      */       {
/* 1627 */         performer.getCommunicator().sendNormalServerMessage("You cannot sacrifice the " + target.getName() + " right now.");
/*      */       }
/*      */     
/* 1630 */     } else if (action.currentSecond() == 2) {
/*      */       
/* 1632 */       performer.getCommunicator().sendNormalServerMessage("You call out to " + deity.name + " by all " + deity
/* 1633 */           .getHisHerItsString() + " names.");
/* 1634 */       Server.getInstance().broadCastAction(performer.getName() + " calls out to " + deity.name + " by all " + deity
/* 1635 */           .getHisHerItsString() + " names.", performer, 5);
/*      */     }
/* 1637 */     else if (action.currentSecond() == 4) {
/*      */       
/* 1639 */       if (Server.rand.nextInt(65535) > damageDiff) {
/*      */         
/* 1641 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " hits you and interrupts your offering to " + deity
/* 1642 */             .getName() + ". This may be easier if " + target
/* 1643 */             .getHeSheItString() + " was closer to death's door.");
/* 1644 */         Server.getInstance().broadCastAction(performer.getName() + " is interrupted by the " + target
/* 1645 */             .getName() + ".", performer, 5);
/*      */         
/* 1647 */         return true;
/*      */       } 
/* 1649 */       performer.getCommunicator().sendNormalServerMessage("You beg " + deity.name + " to accept your offerings.");
/* 1650 */       Server.getInstance().broadCastAction(performer.getName() + " begs " + deity.name + " to accept " + performer
/* 1651 */           .getHisHerItsString() + " offerings.", performer, 5);
/*      */     }
/* 1653 */     else if (action.currentSecond() == 6) {
/*      */       
/* 1655 */       performer.getCommunicator().sendNormalServerMessage("You ask " + deity.name + " for forgiveness if the " + target
/* 1656 */           .getName() + " does not please " + deity.getHimHerItString() + ".");
/* 1657 */       Server.getInstance().broadCastAction(performer.getName() + " asks " + deity.name + " for forgiveness if the " + target
/* 1658 */           .getName() + " does not please " + deity.getHimHerItString() + ".", performer, 5);
/*      */     }
/* 1660 */     else if (action.currentSecond() == 8) {
/*      */       
/* 1662 */       if (Server.rand.nextInt(65535) > damageDiff) {
/*      */         
/* 1664 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " hits you and interrupts your offering to " + deity
/* 1665 */             .getName() + ". This may be easier if " + target
/* 1666 */             .getHeSheItString() + " was closer to death's door.");
/* 1667 */         Server.getInstance().broadCastAction(performer.getName() + " is interrupted by the " + target
/* 1668 */             .getName() + ".", performer, 5);
/*      */         
/* 1670 */         return true;
/*      */       } 
/* 1672 */       performer.getCommunicator().sendNormalServerMessage("You ask " + deity.name + " to bless you with " + deity
/* 1673 */           .getHisHerItsString() + " favors.");
/* 1674 */       Server.getInstance().broadCastAction(performer.getName() + " asks " + deity.name + " to bless " + performer
/* 1675 */           .getHimHerItString() + " with " + deity.getHisHerItsString() + " favors.", performer, 5);
/*      */     }
/* 1677 */     else if (action.currentSecond() == 10) {
/*      */       
/* 1679 */       performer.getCommunicator().sendNormalServerMessage("You kneel in silent hope.");
/* 1680 */       Server.getInstance().broadCastAction(performer.getName() + " kneels in silence.", performer, 5);
/*      */     }
/* 1682 */     else if (action.currentSecond() >= 12) {
/*      */       
/* 1684 */       done = true;
/* 1685 */       performer.achievement(611);
/* 1686 */       if (target.getStatus().isChampion()) {
/* 1687 */         performer.achievement(634);
/*      */       }
/* 1689 */       float favor = 5.0F;
/* 1690 */       if (target.hasTraits())
/*      */       {
/* 1692 */         for (int x = 0; x < 64; x++) {
/*      */           
/* 1694 */           if (target.getStatus().isTraitBitSet(x))
/*      */           {
/* 1696 */             if (!Traits.isTraitNegative(x) && !Traits.isTraitNeutral(x)) {
/* 1697 */               favor++;
/* 1698 */             } else if (Traits.isTraitNegative(x)) {
/* 1699 */               favor--;
/*      */             }  } 
/*      */         } 
/*      */       }
/* 1703 */       favor *= target.getBaseCombatRating();
/* 1704 */       if (deity != performer.getDeity()) {
/* 1705 */         favor = 0.0F;
/*      */       }
/*      */       
/* 1708 */       MissionTriggers.activateTriggers(performer, -target.getTemplate().getTemplateId(), 142, -10L, 1);
/* 1709 */       target.die(false, "Religious Sacrifice Action");
/* 1710 */       if (favor >= 30.0F) {
/*      */         
/* 1712 */         if (performer.getDeity().isHateGod()) {
/* 1713 */           performer.maybeModifyAlignment(-1.0F);
/*      */         } else {
/* 1715 */           performer.maybeModifyAlignment(1.0F);
/* 1716 */         }  SoundPlayer.playSound("sound.fx.humm", target, 1.0F);
/*      */         
/* 1718 */         performer.getCommunicator().sendNormalServerMessage("You hear a strange sound as " + deity.name + " gladly accepts your offerings.");
/*      */         
/* 1720 */         Server.getInstance().broadCastAction(performer.getName() + " looks awed as " + deity.name + " accepts " + performer
/* 1721 */             .getHisHerItsString() + " offerings.", performer, 5);
/*      */       }
/* 1723 */       else if (favor > 0.0F) {
/*      */         
/* 1725 */         performer.getCommunicator().sendNormalServerMessage(deity.name + " kindly accepts your offerings.");
/* 1726 */         Server.getInstance().broadCastAction(performer.getName() + " looks pleased as " + deity.name + " accepts " + performer
/* 1727 */             .getHisHerItsString() + " offerings.", performer, 5);
/*      */       }
/*      */       else {
/*      */         
/* 1731 */         performer.getCommunicator().sendNormalServerMessage(deity.name + " is silent.");
/* 1732 */         Server.getInstance().broadCastAction(performer.getName() + " looks worried as " + deity.name + " accepts " + performer
/* 1733 */             .getHisHerItsString() + " offerings.", performer, 5);
/*      */       } 
/*      */       
/*      */       try {
/* 1737 */         performer.setFavor(performer.getFavor() + favor);
/*      */       }
/* 1739 */       catch (IOException iox) {
/*      */         
/* 1741 */         logger.log(Level.WARNING, performer.getName() + " " + iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/* 1744 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean desecrate(Action act, Creature performer, @Nullable Item item, Item altar) {
/* 1749 */     boolean done = false;
/* 1750 */     Deity deity = performer.getDeity();
/* 1751 */     Deity altarDeity = altar.getBless();
/* 1752 */     if (item != null)
/*      */     {
/* 1754 */       if (item.isBodyPart() || item.getTemplateId() == 0 || item.getTemplateId() == 824) {
/*      */         
/* 1756 */         performer.getCommunicator().sendNormalServerMessage("You cannot use that.");
/* 1757 */         return true;
/*      */       } 
/*      */     }
/* 1760 */     if (altarDeity == null) {
/*      */       
/* 1762 */       performer.getCommunicator().sendNormalServerMessage("The " + altar.getName() + " has no deity!");
/* 1763 */       return true;
/*      */     } 
/* 1765 */     boolean usingHolyItem = false;
/* 1766 */     if (deity != null) {
/*      */       
/* 1768 */       if (altarDeity.equals(deity)) {
/*      */         
/* 1770 */         if (performer.faithful || performer.isChampion())
/*      */         {
/* 1772 */           performer.getCommunicator().sendNormalServerMessage("How can you even think of desecrating " + altar
/* 1773 */               .getNameWithGenus() + "?");
/* 1774 */           return true;
/*      */         }
/*      */       
/* 1777 */       } else if (altarDeity.alignment > 0 && deity.alignment > 0) {
/*      */         
/* 1779 */         if (performer.faithful) {
/*      */           
/* 1781 */           performer.getCommunicator().sendNormalServerMessage(deity.name + " would not approve of you desecrating " + altar
/* 1782 */               .getNameWithGenus() + ".");
/* 1783 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 1787 */       if (!Methods.isActionAllowed(performer, (short)83))
/* 1788 */         return true; 
/* 1789 */       if (item != null && item.isHolyItem() && item.isHolyItem(deity))
/*      */       {
/*      */         
/* 1792 */         usingHolyItem = true;
/*      */       }
/*      */     } 
/* 1795 */     Skill exorcism = null;
/*      */     
/*      */     try {
/* 1798 */       exorcism = performer.getSkills().getSkill(10068);
/*      */     }
/* 1800 */     catch (NoSuchSkillException nss) {
/*      */       
/* 1802 */       if (deity != null)
/* 1803 */         exorcism = performer.getSkills().learn(10068, 1.0F); 
/*      */     } 
/* 1805 */     if (act.currentSecond() == 1) {
/*      */       
/* 1807 */       if (deity != null && item != null && item.isHolyItem(deity))
/*      */       {
/* 1809 */         performer.getCommunicator().sendNormalServerMessage("You will use the power of " + deity.name + " to desecrate the altar!");
/*      */       }
/*      */       
/* 1812 */       performer.getCommunicator().sendNormalServerMessage("You start to desecrate the " + altar.getName() + ".");
/* 1813 */       Server.getInstance().broadCastAction(performer.getName() + " starts to desecrate the " + altar.getName() + ".", performer, 5);
/*      */       
/* 1815 */       performer.sendActionControl(Actions.actionEntrys[143].getVerbString(), true, 900);
/*      */     }
/*      */     else {
/*      */       
/* 1819 */       float bonus = performer.zoneBonus;
/*      */       
/* 1821 */       if (act.currentSecond() % 5 == 0 && act.currentSecond() != 90) {
/*      */         
/* 1823 */         performer.getStatus().modifyStamina(-3000.0F);
/* 1824 */         if (deity != null)
/*      */         {
/* 1826 */           if (altarDeity.equals(deity))
/*      */           {
/* 1828 */             performer
/* 1829 */               .getCommunicator()
/* 1830 */               .sendAlertServerMessage("This is a great risk. " + deity.name + " may notice you and become furious!");
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/* 1835 */       if (act.currentSecond() == 10) {
/*      */         
/* 1837 */         performer.getCommunicator().sendNormalServerMessage("You think of ways to defile the " + altar.getName() + ".");
/* 1838 */         Server.getInstance().broadCastAction(performer.getName() + " gives the " + altar.getName() + " a stern look.", performer, 5);
/*      */       
/*      */       }
/* 1841 */       else if (act.currentSecond() == 20) {
/*      */         
/* 1843 */         performer.getCommunicator().sendNormalServerMessage("You spit on the " + altar.getName() + ".");
/* 1844 */         Server.getInstance().broadCastAction(performer.getName() + " spits on the " + altar.getName() + ".", performer, 5);
/*      */       
/*      */       }
/* 1847 */       else if (act.currentSecond() == 30) {
/*      */         
/* 1849 */         if (deity != null)
/*      */         {
/* 1851 */           performer.getCommunicator().sendNormalServerMessage("You call out to " + deity.name + " for strength.");
/* 1852 */           Server.getInstance().broadCastAction(performer
/* 1853 */               .getName() + " calls out to " + deity.name + " for strength.", performer, 5);
/*      */         }
/*      */         else
/*      */         {
/* 1857 */           performer.getCommunicator().sendNormalServerMessage("You call " + altarDeity.name + " names.");
/* 1858 */           Server.getInstance().broadCastAction(performer.getName() + " calls " + altarDeity.name + " names.", performer, 5);
/*      */         }
/*      */       
/*      */       }
/* 1862 */       else if (act.currentSecond() == 40 || act.currentSecond() == 75) {
/*      */         
/* 1864 */         if (usingHolyItem)
/*      */         {
/* 1866 */           Methods.sendSound(performer, "sound.religion.channel");
/* 1867 */           performer.getCommunicator().sendNormalServerMessage("You channel the power of " + deity.name + " through your " + item
/* 1868 */               .getName() + ".");
/* 1869 */           Server.getInstance().broadCastAction(performer
/* 1870 */               .getName() + " channels the power of " + deity.name + " through " + performer
/* 1871 */               .getHisHerItsString() + " " + item.getName() + ".", performer, 5);
/*      */         }
/* 1873 */         else if (item != null)
/*      */         {
/* 1875 */           performer.getCommunicator().sendNormalServerMessage("You bang the " + item
/* 1876 */               .getName() + " on the " + altar.getName() + ".");
/* 1877 */           Server.getInstance().broadCastAction(performer
/* 1878 */               .getName() + " bangs " + item.getNameWithGenus() + " on the " + altar.getName() + ".", performer, 5);
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 1883 */           performer.getCommunicator().sendNormalServerMessage("You hit the " + altar.getName() + " hard.");
/* 1884 */           Server.getInstance().broadCastAction(performer.getName() + " hits the " + altar.getName() + " hard.", performer, 5);
/*      */         }
/*      */       
/*      */       }
/* 1888 */       else if (act.currentSecond() == 50) {
/*      */         
/* 1890 */         performer.getCommunicator().sendNormalServerMessage("You relieve yourself on the " + altar.getName() + ".");
/* 1891 */         Server.getInstance().broadCastAction(performer
/* 1892 */             .getName() + " relieves " + performer.getHimHerItString() + "self on the " + altar.getName() + "!", performer, 5);
/*      */       
/*      */       }
/* 1895 */       else if (act.currentSecond() == 60) {
/*      */         
/* 1897 */         performer.getCommunicator().sendNormalServerMessage("You try to push the " + altar.getName() + " over.");
/* 1898 */         Server.getInstance().broadCastAction(performer.getName() + " tries to push the " + altar.getName() + " over.", performer, 5);
/*      */       
/*      */       }
/* 1901 */       else if (act.currentSecond() == 70) {
/*      */         
/* 1903 */         if (deity != null) {
/*      */           
/* 1905 */           Methods.sendSound(performer, "sound.religion.desecrate");
/* 1906 */           performer.getCommunicator().sendNormalServerMessage("In the name of " + deity.name + " you demand the presence of " + altarDeity.name + " to leave this place.");
/*      */ 
/*      */           
/* 1909 */           Server.getInstance().broadCastAction(performer
/* 1910 */               .getName() + " demands that the presence of " + altarDeity.name + " leaves this place in the name of " + deity.name + ".", performer, 5);
/*      */         
/*      */         }
/* 1913 */         else if (item != null) {
/*      */           
/* 1915 */           if (item.isFood() || item.isCloth())
/*      */           {
/* 1917 */             performer.getCommunicator().sendNormalServerMessage("You throw the " + item
/* 1918 */                 .getName() + " on the " + altar.getName() + " in disgust.");
/* 1919 */             Server.getInstance().broadCastAction(performer
/* 1920 */                 .getName() + " throws " + item.getNameWithGenus() + " on the " + altar.getName() + " in disgust.", performer, 5);
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 1925 */             performer.getCommunicator().sendNormalServerMessage("You try to scratch the " + altar
/* 1926 */                 .getName() + " with the " + item.getName() + ".");
/* 1927 */             Server.getInstance().broadCastAction(performer
/* 1928 */                 .getName() + " tries to scratch the " + altar.getName() + " with the " + item
/* 1929 */                 .getName() + ".", performer, 5);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1934 */           performer.getCommunicator().sendNormalServerMessage("You kick the " + altar.getName() + " hard.");
/* 1935 */           Server.getInstance().broadCastAction(performer.getName() + " kick the " + altar.getName() + " hard.", performer, 5);
/*      */         }
/*      */       
/*      */       }
/* 1939 */       else if (act.currentSecond() == 80) {
/*      */         
/* 1941 */         performer.getCommunicator().sendNormalServerMessage("You notice a weird silence.");
/* 1942 */         Server.getInstance().broadCastAction(performer.getName() + " stops and listens.", performer, 5);
/*      */       }
/* 1944 */       else if (act.currentSecond() >= 90) {
/*      */         
/* 1946 */         Methods.sendSound(performer, "sound.religion.desecrate");
/* 1947 */         done = true;
/* 1948 */         if (item != null) {
/*      */           
/* 1950 */           bonus = performer.zoneBonus + item.getCurrentQualityLevel() / 5.0F;
/* 1951 */           if (!usingHolyItem)
/* 1952 */             bonus = performer.zoneBonus + item.getCurrentQualityLevel() / 10.0F; 
/* 1953 */           if (item.getTemplateId() == 340) {
/* 1954 */             bonus += 50.0F;
/*      */           }
/*      */         } 
/* 1957 */         double power = 0.0D;
/* 1958 */         if (exorcism != null) {
/* 1959 */           power = exorcism.skillCheck(altar.getCurrentQualityLevel(), bonus, false, act.getCounterAsFloat());
/*      */         } else {
/* 1961 */           power = (bonus - Server.rand.nextInt(200));
/* 1962 */         }  float alignMod = 0.0F;
/* 1963 */         if (altarDeity.alignment > 0) {
/* 1964 */           alignMod = -2.0F;
/* 1965 */         } else if (altarDeity.alignment < 0) {
/* 1966 */           alignMod = 2.0F;
/* 1967 */         }  boolean hugeAltar = altar.isHugeAltar();
/*      */         
/* 1969 */         if (power > 0.0D && !hugeAltar) {
/*      */           
/* 1971 */           performer.getCommunicator().sendNormalServerMessage("The " + altar.getName() + " crumbles to dust!");
/* 1972 */           Server.getInstance().broadCastAction("The " + altar.getName() + " crumbles to dust!", performer, 5);
/* 1973 */           Items.destroyItem(altar.getWurmId());
/* 1974 */           if (deity != null)
/*      */           {
/* 1976 */             if (altarDeity.equals(deity)) {
/*      */               
/* 1978 */               performer.getCommunicator()
/* 1979 */                 .sendNormalServerMessage(deity.name + " has noticed you and is furious!");
/*      */               
/* 1981 */               byte type = 6;
/* 1982 */               int rand = Server.rand.nextInt(3);
/* 1983 */               if (rand != 0)
/*      */               {
/*      */ 
/*      */                 
/* 1987 */                 if (rand == 1) {
/*      */                   
/* 1989 */                   type = 5;
/*      */                 }
/* 1991 */                 else if (rand == 2) {
/*      */                   
/* 1993 */                   type = 2;
/*      */                 } 
/*      */               }
/*      */               try {
/* 1997 */                 performer.addWoundOfType(null, type, 0, true, 1.0F, true, (
/* 1998 */                     (float)Math.abs(power) * 200.0F), 
/* 1999 */                     (float)Math.abs(power), (float)Math.abs(power), false, false);
/*      */               }
/* 2001 */               catch (Exception ex) {
/*      */                 
/* 2003 */                 logger.log(Level.WARNING, performer.getName(), ex);
/*      */               } 
/*      */               
/*      */               try {
/* 2007 */                 performer.setFaith(performer.getFaith() - 5.0F);
/*      */               }
/* 2009 */               catch (IOException iox) {
/*      */                 
/* 2011 */                 logger.log(Level.WARNING, performer.getName(), iox);
/*      */               }
/*      */             
/* 2014 */             } else if (altarDeity.alignment > 0 && deity.alignment > 0) {
/*      */               
/* 2016 */               if (Server.rand.nextInt(120) > performer.getFaith()) {
/*      */                 
/* 2018 */                 performer.getCommunicator().sendNormalServerMessage(deity.name + " has noticed you and is very upset!");
/*      */                 
/* 2020 */                 performer.modifyFaith(-1.0F);
/*      */                 
/*      */                 try {
/* 2023 */                   performer.setFavor(performer.getFavor() - 20.0F);
/*      */                 }
/* 2025 */                 catch (IOException iox) {
/*      */                   
/* 2027 */                   logger.log(Level.WARNING, performer.getName(), iox);
/*      */                 }
/*      */               
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } else {
/*      */           
/* 2035 */           performer.getCommunicator().sendNormalServerMessage("You are hit by a sudden pain!");
/* 2036 */           Server.getInstance().broadCastAction(performer.getName() + " suddenly writhes in pain!", performer, 5);
/* 2037 */           byte type = 6;
/* 2038 */           int rand = Server.rand.nextInt(3);
/* 2039 */           double isev = 0.0D;
/* 2040 */           double psev = 0.0D;
/* 2041 */           double pow = -power;
/* 2042 */           if (rand == 1) {
/*      */             
/* 2044 */             type = 5;
/* 2045 */             psev = pow;
/*      */           }
/* 2047 */           else if (rand == 2) {
/*      */             
/* 2049 */             type = 2;
/*      */           } else {
/*      */             
/* 2052 */             isev = pow;
/* 2053 */           }  EndGameItem three = EndGameItems.getGoodAltar();
/* 2054 */           if (three != null && three.getItem() != null && three.getItem().getWurmId() == altar.getWurmId())
/*      */           {
/* 2056 */             if (performer.getKingdomTemplateId() != 0 && performer
/* 2057 */               .getKingdomTemplateId() != 3) {
/* 2058 */               performer.setReputation(performer.getReputation() - 70);
/*      */             }
/*      */           }
/* 2061 */           if (hugeAltar && power > 0.0D) {
/*      */             
/* 2063 */             pow = power;
/* 2064 */             altar.setDamage(altar.getDamage() + 0.1F);
/*      */           } 
/*      */           
/* 2067 */           performer.addWoundOfType(null, type, 1, true, 1.0F, true, pow * 200.0D, (float)psev, (float)isev, false, false);
/*      */         } 
/*      */         
/* 2070 */         performer.maybeModifyAlignment(alignMod);
/*      */       } 
/*      */     } 
/* 2073 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean listen(Creature performer, Creature target, Action act, float counter) {
/* 2078 */     boolean done = false;
/*      */     
/* 2080 */     Deity deity = performer.getDeity();
/* 2081 */     if (target.isInvulnerable()) {
/*      */       
/* 2083 */       target.getCommunicator().sendNormalServerMessage("You must not be invulnerable in order to confess.");
/* 2084 */       performer.getCommunicator().sendNormalServerMessage(target.getName() + " must not be invulnerable!");
/* 2085 */       return true;
/*      */     } 
/* 2087 */     if (target.isMoving()) {
/*      */       
/* 2089 */       target.getCommunicator().sendNormalServerMessage("You must stand still in order to confess.");
/* 2090 */       performer.getCommunicator().sendNormalServerMessage(target.getName() + " must stand still!");
/* 2091 */       return true;
/*      */     } 
/* 2093 */     Long last = listenedTo.get(Long.valueOf(target.getWurmId()));
/* 2094 */     if (last != null)
/*      */     {
/* 2096 */       if (System.currentTimeMillis() - last.longValue() < 86400000L) {
/*      */         
/* 2098 */         performer.getCommunicator().sendNormalServerMessage(target
/* 2099 */             .getName() + " apparently confessed recently. That will have to do for another " + 
/* 2100 */             Server.getTimeFor(last.longValue() + 86400000L - System.currentTimeMillis()) + ".");
/* 2101 */         target.getCommunicator().sendNormalServerMessage("You confessed recently. That will have to do for another " + 
/*      */             
/* 2103 */             Server.getTimeFor(last.longValue() + 86400000L - System.currentTimeMillis()) + ".");
/* 2104 */         return true;
/*      */       } 
/*      */     }
/* 2107 */     if (counter == 1.0F) {
/*      */       
/* 2109 */       target.getCommunicator().sendNormalServerMessage(performer
/* 2110 */           .getName() + " focuses on you and awaits your confession.");
/* 2111 */       performer.getCommunicator().sendNormalServerMessage("You start to listen to the confession of " + target
/* 2112 */           .getName() + ".");
/* 2113 */       Server.getInstance().broadCastAction(performer
/* 2114 */           .getName() + " starts to hear the confessions of " + target.getName() + ".", performer, target, 5);
/* 2115 */       performer.sendActionControl(Actions.actionEntrys[115].getVerbString(), true, 400);
/*      */     }
/*      */     else {
/*      */       
/* 2119 */       if (act.currentSecond() == 5) {
/*      */         
/* 2121 */         if (deity.isHateGod()) {
/*      */           
/* 2123 */           target.getCommunicator().sendNormalServerMessage("You open up with something fairly harmless.");
/* 2124 */           performer.getCommunicator().sendNormalServerMessage("You nod and think of ways to humiliate " + target
/* 2125 */               .getName() + ".");
/*      */         }
/*      */         else {
/*      */           
/* 2129 */           target.getCommunicator().sendNormalServerMessage("You open up with something fairly harmless.");
/* 2130 */           performer.getCommunicator().sendNormalServerMessage("You nod and hope that " + target
/* 2131 */               .getName() + " hasn't done too many bad things.");
/*      */         } 
/*      */         
/* 2134 */         Server.getInstance().broadCastAction(performer
/* 2135 */             .getName() + " nods and mumbles something as " + target.getName() + " confesses.", performer, target, 5);
/*      */       } 
/*      */       
/* 2138 */       if (act.currentSecond() == 10) {
/*      */         
/* 2140 */         if (deity.isLibila()) {
/*      */           
/* 2142 */           target.getCommunicator().sendNormalServerMessage("You try to distract by sounding very worried that you may not do enough in her service.");
/*      */           
/* 2144 */           performer.getCommunicator().sendNormalServerMessage("Aha! The foolish " + target
/* 2145 */               .getName() + " shows weakness!");
/*      */         }
/*      */         else {
/*      */           
/* 2149 */           target.getCommunicator().sendNormalServerMessage("You show ridiculous amounts of grief over the trifle you just told.");
/*      */           
/* 2151 */           performer.getCommunicator().sendNormalServerMessage(target
/* 2152 */               .getName() + " plays over. Surely " + target.getHeSheItString() + " can't be that remorseful. This is a sin if anything!");
/*      */         } 
/*      */ 
/*      */         
/* 2156 */         Server.getInstance().broadCastAction(performer
/* 2157 */             .getName() + " sits silent and listens sincerely while " + target.getName() + " looks very worried.", performer, target, 5);
/*      */       
/*      */       }
/* 2160 */       else if (act.currentSecond() == 20) {
/*      */         
/* 2162 */         if (deity.isHateGod()) {
/*      */           
/* 2164 */           target.getCommunicator().sendNormalServerMessage("You try to hide your weakness in a subordinate clause but alas! " + performer
/* 2165 */               .getName() + " stabs at it like a snake!");
/*      */           
/* 2167 */           performer.getCommunicator().sendNormalServerMessage("Hold it! " + target
/* 2168 */               .getName() + " tries to hide something!");
/*      */         }
/*      */         else {
/*      */           
/* 2172 */           target.getCommunicator()
/* 2173 */             .sendNormalServerMessage("Elaborately you tell about a recent failure of yours, trying to smear out the importance with many words. It doesn't work at all and " + performer
/*      */               
/* 2175 */               .getName() + " reprimands you.");
/* 2176 */           performer.getCommunicator().sendNormalServerMessage(target
/* 2177 */               .getName() + " tells you a story, the length of which only upsets you more. You have to reprimand " + target
/*      */               
/* 2179 */               .getHimHerItString() + " sternly. This has gone too far!");
/*      */         } 
/*      */         
/* 2182 */         Server.getInstance().broadCastAction(performer
/* 2183 */             .getName() + " suddenly asks a stern question, and " + target.getName() + " goes bleak.", performer, target, 5);
/*      */       
/*      */       }
/* 2186 */       else if (act.currentSecond() == 30) {
/*      */         
/* 2188 */         if (deity.isHateGod()) {
/*      */           
/* 2190 */           target.getCommunicator().sendNormalServerMessage("You apologize sincerely, practically begging for your life!");
/*      */           
/* 2192 */           performer.getCommunicator().sendNormalServerMessage(target
/* 2193 */               .getName() + " whimpers beneath the might of Libila!");
/*      */         }
/*      */         else {
/*      */           
/* 2197 */           target.getCommunicator().sendNormalServerMessage("You regret your ways and ask " + performer
/* 2198 */               .getName() + " what you can do to repent.");
/* 2199 */           performer.getCommunicator().sendNormalServerMessage(target
/* 2200 */               .getName() + " asks what " + target.getHeSheItString() + " should do for absolution.");
/*      */         } 
/*      */         
/* 2203 */         Server.getInstance().broadCastAction("Through tearfilled eyes, " + target
/* 2204 */             .getName() + " asks " + performer.getName() + " a question.", performer, target, 5);
/*      */ 
/*      */       
/*      */       }
/* 2208 */       else if (act.currentSecond() >= 40) {
/*      */         
/* 2210 */         if (deity.isLibila()) {
/*      */           
/* 2212 */           target.getCommunicator().sendNormalServerMessage(performer
/* 2213 */               .getName() + " scorns you and tells you to give " + performer.getHimHerItString() + " 10 copper coins for Libila to forgive you.");
/*      */           
/* 2215 */           performer.getCommunicator().sendNormalServerMessage("You decide that you can probably fool " + target
/* 2216 */               .getName() + " to give you 10 copper pieces and say that it is the penance " + target
/*      */               
/* 2218 */               .getHeSheItString() + " has to pay.");
/*      */         }
/*      */         else {
/*      */           
/* 2222 */           int templateId = 29;
/* 2223 */           if (deity.isMagranon()) {
/* 2224 */             templateId = 204;
/* 2225 */           } else if (deity.isFo()) {
/* 2226 */             templateId = 436;
/* 2227 */           }  String name = "10 copper";
/*      */           
/*      */           try {
/* 2230 */             ItemTemplate it = ItemTemplateFactory.getInstance().getTemplate(templateId);
/* 2231 */             name = it.getNameWithGenus();
/*      */           }
/* 2233 */           catch (NoSuchTemplateException noSuchTemplateException) {}
/*      */ 
/*      */ 
/*      */           
/* 2237 */           target.getCommunicator().sendNormalServerMessage(performer
/* 2238 */               .getName() + " thinks for a while and asks you to sacrifice " + name + ".");
/* 2239 */           performer.getCommunicator().sendNormalServerMessage("You decide that a good penance is for " + target
/* 2240 */               .getName() + " to sacrifice " + name + ". That will surely please " + deity.name + ".");
/*      */         } 
/*      */ 
/*      */         
/* 2244 */         Server.getInstance().broadCastAction(target
/* 2245 */             .getName() + " finishes " + target.getHisHerItsString() + " confession and " + performer.getName() + " seems to tell " + target
/* 2246 */             .getHimHerItString() + " what to do.", performer, target, 5);
/* 2247 */         done = true;
/* 2248 */         listenedTo.put(Long.valueOf(target.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/*      */         
/* 2250 */         performer.achievement(612);
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 2255 */           Skill preaching = performer.getSkills().getSkill(10065);
/* 2256 */           preaching.skillCheck(1.0D, 0.0D, false, 30.0F);
/*      */         }
/* 2258 */         catch (NoSuchSkillException nss) {
/*      */           
/* 2260 */           performer.getSkills().learn(10065, 1.0F);
/*      */         } 
/* 2262 */         if (deity.isHateGod()) {
/*      */           
/* 2264 */           performer.maybeModifyAlignment(-1.0F);
/* 2265 */           if (target.isPriest()) {
/* 2266 */             target.maybeModifyAlignment(-1.0F);
/*      */           } else {
/* 2268 */             target.maybeModifyAlignment(-5.0F);
/*      */           } 
/*      */         } else {
/*      */           
/* 2272 */           performer.maybeModifyAlignment(1.0F);
/* 2273 */           if (target.isPriest()) {
/* 2274 */             target.maybeModifyAlignment(1.0F);
/*      */           } else {
/* 2276 */             target.maybeModifyAlignment(5.0F);
/*      */           } 
/*      */         } 
/*      */       } 
/* 2280 */     }  return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean holdSermon(Creature performer, Item altar, Item holySymbol, Action act, float counter) {
/* 2286 */     boolean done = false;
/*      */     
/* 2288 */     Deity deity = performer.getDeity();
/* 2289 */     Long last = lastHeldSermon.get(Long.valueOf(performer.getWurmId()));
/* 2290 */     if (last != null)
/*      */     {
/* 2292 */       if (System.currentTimeMillis() - last.longValue() < 10800000L) {
/*      */         
/* 2294 */         performer.getCommunicator().sendNormalServerMessage("You held a sermon recently and you are still recovering psychically. You need to wait another " + 
/*      */             
/* 2296 */             Server.getTimeFor(last.longValue() + 10800000L - System.currentTimeMillis()) + ".");
/* 2297 */         return true;
/*      */       } 
/*      */     }
/* 2300 */     if (!holySymbol.isHolyItem() || !holySymbol.isHolyItem(performer.getDeity())) {
/*      */       
/* 2302 */       performer.getCommunicator().sendNormalServerMessage("You need to use your own holy symbol to initate the sermon.");
/* 2303 */       return true;
/*      */     } 
/* 2305 */     if (counter == 1.0F) {
/*      */       
/* 2307 */       int tilex = (performer.getCurrentTile()).tilex;
/* 2308 */       int tiley = (performer.getCurrentTile()).tiley;
/* 2309 */       int numfollowers = 0;
/* 2310 */       for (int x = -5; x < 5; x++) {
/*      */         
/* 2312 */         for (int y = -5; y < 5; y++) {
/*      */           
/* 2314 */           VolaTile t = Zones.getTileOrNull(tilex + x, tiley + y, performer.isOnSurface());
/* 2315 */           if (t != null) {
/*      */             
/* 2317 */             Creature[] crets = t.getCreatures();
/* 2318 */             for (int c = 0; c < crets.length; c++) {
/*      */               
/* 2320 */               if (crets[c].getWurmId() != performer.getWurmId() && 
/* 2321 */                 crets[c].isPlayer() && crets[c].getDeity() != null && crets[c]
/* 2322 */                 .getKingdomId() == performer.getKingdomId())
/* 2323 */                 numfollowers++; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2328 */       if (numfollowers <= 0) {
/*      */         
/* 2330 */         performer.getCommunicator().sendNormalServerMessage("You need at least one follower to attend the sermon.");
/* 2331 */         return true;
/*      */       } 
/* 2333 */       performer.getCommunicator().sendNormalServerMessage("You initiate the sermon by brandishing your " + holySymbol
/* 2334 */           .getName() + ".");
/* 2335 */       Server.getInstance().broadCastAction(performer
/* 2336 */           .getName() + " initiates the sermon by brandishing " + performer.getHisHerItsString() + " " + holySymbol
/* 2337 */           .getName() + ".", performer, 5);
/* 2338 */       performer.sendActionControl(Actions.actionEntrys[216].getVerbString(), true, 600);
/*      */     }
/*      */     else {
/*      */       
/* 2342 */       if (act.currentSecond() == 5) {
/*      */         
/* 2344 */         performer.getCommunicator().sendNormalServerMessage("You clear your throat and evaluate your audience.");
/* 2345 */         Server.getInstance().broadCastAction(performer
/* 2346 */             .getName() + " clears " + performer.getHisHerItsString() + " throat and looks at the audience sternly.", performer, 5);
/*      */       } 
/*      */       
/* 2349 */       if (act.currentSecond() == 10) {
/*      */         
/* 2351 */         if (deity.isHateGod())
/*      */         {
/* 2353 */           performer.getCommunicator().sendNormalServerMessage("The rabble you have to deal with these days! Let's scare them into submission.");
/*      */           
/* 2355 */           Server.getInstance()
/* 2356 */             .broadCastAction(performer
/* 2357 */               .getName() + " screams something about how infidels will be slaughtered and fed to the wolves. You suddenly realize that " + performer
/*      */               
/* 2359 */               .getHeSheItString() + " means you, the audience.", performer, 5);
/*      */         }
/*      */         else
/*      */         {
/* 2363 */           performer.getCommunicator().sendNormalServerMessage("These people need guidance and motivation in their hardship. You praise " + deity.name + " for " + deity
/*      */               
/* 2365 */               .getHisHerItsString() + " glory.");
/* 2366 */           Server.getInstance().broadCastAction(performer
/* 2367 */               .getName() + " speaks about " + deity.name + " and how " + deity.getHeSheItString() + " will help you through any hardships.", performer, 5);
/*      */         }
/*      */       
/*      */       }
/* 2371 */       else if (act.currentSecond() == 20) {
/*      */         
/* 2373 */         if (Server.rand.nextBoolean())
/*      */         {
/* 2375 */           if (deity.isLibila())
/*      */           {
/* 2377 */             performer.getCommunicator().sendNormalServerMessage("Clearly that didn't do the trick. More gore!");
/* 2378 */             Server.getInstance()
/* 2379 */               .broadCastAction(performer
/* 2380 */                 .getName() + " keeps on about how Libila will boil you alive and chew on your loose skin unless you assault the enemy in her name.", performer, 5);
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*      */             
/* 2386 */             performer.getCommunicator().sendNormalServerMessage("You explain how we all have our doubts and feel lost some times but in the end we will be saved by " + deity.name + "!");
/*      */ 
/*      */             
/* 2389 */             Server.getInstance().broadCastAction(performer
/* 2390 */                 .getName() + " comforts you and explains how " + deity.name + " will always come to your aid in the end.", performer, 5);
/*      */           }
/*      */         
/*      */         }
/* 2394 */       } else if (act.currentSecond() == 30) {
/*      */         
/* 2396 */         if (deity.getFavor() > 100000)
/*      */         {
/* 2398 */           performer.getCommunicator().sendNormalServerMessage("You sense that " + deity
/* 2399 */               .getName() + " is brimming with power. Maybe you can channel it somehow?");
/* 2400 */           Server.getInstance().broadCastAction(performer.getName() + " speaks of miracles soon to come!", performer, 5);
/*      */         
/*      */         }
/* 2403 */         else if (deity.getFavor() > 50000)
/*      */         {
/* 2405 */           performer.getCommunicator().sendNormalServerMessage("You explain how " + deity.name + " is strong but still needs you to work faithfully so that " + deity
/*      */               
/* 2407 */               .getHeSheItString() + " may manifest " + deity.getHisHerItsString() + " powers.");
/* 2408 */           Server.getInstance().broadCastAction(performer
/* 2409 */               .getName() + " explains how " + deity.name + " is strong but still needs you to work faithfully so that " + deity
/* 2410 */               .getHeSheItString() + " may manifest " + deity
/* 2411 */               .getHisHerItsString() + " powers.", performer, 5);
/*      */         }
/* 2413 */         else if (deity.getFavor() > 25000)
/*      */         {
/* 2415 */           performer.getCommunicator().sendNormalServerMessage("You explain how " + deity.name + " is still waning and needs you to keep working for " + deity
/*      */               
/* 2417 */               .getHimHerItString() + " so that " + deity.getHeSheItString() + " may manifest " + deity
/* 2418 */               .getHisHerItsString() + " powers.");
/* 2419 */           Server.getInstance().broadCastAction(performer
/* 2420 */               .getName() + " explains how " + deity.name + " is still waning and needs you to keep working for " + deity
/* 2421 */               .getHimHerItString() + " so that " + deity
/* 2422 */               .getHeSheItString() + " may manifest " + deity.getHisHerItsString() + " powers.", performer, 5);
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 2427 */           performer.getCommunicator().sendNormalServerMessage("You explain how " + deity.name + " is in the waning crescent and needs you to do a lot more for " + deity
/*      */               
/* 2429 */               .getHimHerItString() + " so that " + deity.getHeSheItString() + " may manifest " + deity
/* 2430 */               .getHisHerItsString() + " powers.");
/* 2431 */           Server.getInstance().broadCastAction(performer
/* 2432 */               .getName() + " explains how " + deity.name + " is in the waning crescent and needs you to do more for " + deity
/* 2433 */               .getHimHerItString() + " so that " + deity
/* 2434 */               .getHeSheItString() + " may manifest " + deity.getHisHerItsString() + " powers.", performer, 5);
/*      */         }
/*      */       
/*      */       }
/* 2438 */       else if (act.currentSecond() == 40) {
/*      */         
/* 2440 */         String todo = "sacrifice something";
/* 2441 */         if (deity.isHateGod()) {
/*      */           
/* 2443 */           int num = Server.rand.nextInt(5);
/* 2444 */           if (num == 0) {
/* 2445 */             todo = "desecrate an enemy altar";
/* 2446 */           } else if (num == 1) {
/* 2447 */             todo = "confess";
/* 2448 */           } else if (num == 2) {
/* 2449 */             todo = "butcher enemy corpses";
/* 2450 */           } else if (num == 3) {
/* 2451 */             todo = "pray regularly";
/* 2452 */           } else if (num == 4) {
/* 2453 */             todo = "attend sermon";
/*      */           } 
/*      */         } else {
/*      */           
/* 2457 */           int num = Server.rand.nextInt(5);
/* 2458 */           if (num == 0) {
/* 2459 */             todo = "desecrate an enemy altar";
/* 2460 */           } else if (num == 1) {
/* 2461 */             todo = "confess";
/* 2462 */           } else if (num == 2) {
/* 2463 */             todo = "bury a corpse";
/* 2464 */           } else if (num == 3) {
/* 2465 */             todo = "heal your next of kin";
/* 2466 */           } else if (num == 4) {
/* 2467 */             todo = "pray regularly";
/* 2468 */           } else if (num == 5) {
/* 2469 */             todo = "attend sermon";
/*      */           } 
/*      */         } 
/* 2472 */         performer.getCommunicator().sendNormalServerMessage("Today you decide to suggest that they " + todo + ".");
/* 2473 */         Server.getInstance().broadCastAction(performer.getName() + " suggests that you " + todo + ".", performer, 5);
/*      */       }
/* 2475 */       else if (act.currentSecond() == 50) {
/*      */         
/* 2477 */         if (Deities.hasValreiStatuses() && Server.rand.nextBoolean()) {
/*      */           
/* 2479 */           String status = Deities.getRandomStatusFor(deity.getNumber());
/* 2480 */           if (status.length() == 0 || Server.rand.nextBoolean())
/*      */           {
/* 2482 */             status = Deities.getRandomStatus();
/*      */           }
/* 2484 */           if (status.length() > 0)
/*      */           {
/* 2486 */             performer.getCommunicator().sendNormalServerMessage("You have a vision: " + status);
/* 2487 */             Server.getInstance().broadCastAction(performer.getName() + " claims to have a vision: " + status, performer, 5);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 2493 */         else if (EpicServerStatus.getCurrentScenario() != null && EpicServerStatus.getCurrentScenario().isCurrent()) {
/*      */           
/* 2495 */           performer.getCommunicator().sendNormalServerMessage("You have the vision that " + 
/* 2496 */               EpicServerStatus.getCurrentScenario().getScenarioQuest() + '.');
/*      */           
/* 2498 */           Server.getInstance().broadCastAction(performer
/* 2499 */               .getName() + " claims that " + 
/* 2500 */               EpicServerStatus.getCurrentScenario().getScenarioQuest() + '.', performer, 5);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2505 */         if (Servers.localServer.PVPSERVER)
/*      */         {
/* 2507 */           performer
/* 2508 */             .getCommunicator()
/* 2509 */             .sendNormalServerMessage("There are enemies of the faith and these must be stopped. You urge the flock to be vigilant, and wary of strangers and infidels.");
/*      */           
/* 2511 */           Server.getInstance().broadCastAction(performer
/* 2512 */               .getName() + " explains that the enemies of " + deity.name + " must be summarily slaughtered.", performer, 5);
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 2517 */           performer
/* 2518 */             .getCommunicator()
/* 2519 */             .sendNormalServerMessage("In distant lands there are enemies of the faith and people struggle to keep them at bay. You urge the flock to send these people a thought and a prayer.");
/*      */           
/* 2521 */           Server.getInstance().broadCastAction(performer
/* 2522 */               .getName() + " explains that far away other people of the faith fight horrible enemies of " + deity.name + ". You are urged to send a thought and a prayer to these valiant protectors.", performer, 5);
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 2531 */       else if (act.currentSecond() >= 60) {
/*      */         
/* 2533 */         performer.getCommunicator().sendNormalServerMessage("You finish this sermon by yet again praising " + deity.name + " and ask everyone to pray together with you.");
/*      */ 
/*      */         
/* 2536 */         Server.getInstance().broadCastAction(performer
/* 2537 */             .getName() + " finishes the sermon by asking you to join " + performer.getHimHerItString() + " in a prayer to " + deity.name + ".", performer, 5);
/*      */         
/* 2539 */         done = true;
/* 2540 */         lastHeldSermon.put(Long.valueOf(performer.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/*      */         
/* 2542 */         Skill preaching = null;
/*      */         
/*      */         try {
/* 2545 */           preaching = performer.getSkills().getSkill(10065);
/*      */         }
/* 2547 */         catch (NoSuchSkillException nss) {
/*      */           
/* 2549 */           preaching = performer.getSkills().learn(10065, 1.0F);
/*      */         } 
/* 2551 */         Set<Creature> listeners = new HashSet<>();
/* 2552 */         int tilex = (performer.getCurrentTile()).tilex;
/* 2553 */         int tiley = (performer.getCurrentTile()).tiley;
/*      */         
/* 2555 */         int numfollowers = 0;
/* 2556 */         for (int x = -5; x < 5; x++) {
/*      */           
/* 2558 */           for (int y = -5; y < 5; y++) {
/*      */             
/* 2560 */             VolaTile t = Zones.getTileOrNull(tilex + x, tiley + y, performer.isOnSurface());
/* 2561 */             if (t != null) {
/*      */               
/* 2563 */               Creature[] crets = t.getCreatures();
/* 2564 */               for (int c = 0; c < crets.length; c++) {
/*      */                 
/* 2566 */                 if (crets[c].getWurmId() != performer.getWurmId()) {
/*      */                   
/* 2568 */                   listeners.add(crets[c]);
/* 2569 */                   if (crets[c].isPaying() && crets[c].isPlayer() && crets[c].getDeity() != null && crets[c]
/* 2570 */                     .getKingdomId() == performer.getKingdomId())
/*      */                   {
/* 2572 */                     numfollowers++;
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 2579 */         int numsTouched = 0;
/* 2580 */         if (listeners.size() > 0) {
/*      */           
/* 2582 */           for (Creature c : listeners) {
/*      */             
/* 2584 */             if (c.isPlayer() && c.getDeity() != null && c
/* 2585 */               .getKingdomId() == performer.getKingdomId()) {
/*      */               
/* 2587 */               if (deity.isHateGod()) {
/*      */                 
/* 2589 */                 if (c.maybeModifyAlignment(Math.max(-4 - altar.getRarity(), -numfollowers - altar.getRarity())) && 
/* 2590 */                   c.isPaying()) {
/* 2591 */                   numsTouched++;
/*      */                 
/*      */                 }
/*      */               }
/* 2595 */               else if (c.maybeModifyAlignment(Math.min(4 + altar.getRarity(), numfollowers + altar.getRarity())) && 
/* 2596 */                 c.isPaying()) {
/* 2597 */                 numsTouched++;
/*      */               } 
/* 2599 */               c.achievement(610);
/*      */             } 
/*      */           } 
/* 2602 */           if (preaching.skillCheck(Math.max(1, numsTouched * 3), altar, 0.0D, false, 10.0F) > 0.0D)
/*      */           {
/* 2604 */             if (deity.isHateGod()) {
/*      */               
/* 2606 */               performer.maybeModifyAlignment(Math.max(-4, Math.min(-1, -numsTouched)));
/*      */             }
/*      */             else {
/*      */               
/* 2610 */               performer.maybeModifyAlignment(Math.min(4, Math.max(1, numsTouched)));
/*      */             } 
/*      */           }
/* 2613 */           float rarityMod = 1.0F;
/* 2614 */           if (altar.getRarity() > 0) {
/* 2615 */             rarityMod += altar.getRarity() * 0.01F;
/*      */           }
/*      */           
/* 2618 */           Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/* 2619 */           templateDeity.setFavor(templateDeity.getFavor() + numsTouched + altar.getRarity());
/* 2620 */           if (numsTouched > 3) {
/*      */             
/* 2622 */             performer.getCommunicator().sendNormalServerMessage(deity.name + " is mighty pleased with you!");
/*      */             
/*      */             try {
/* 2625 */               performer.setFaith(performer.getFaith() + numsTouched * rarityMod / 50.0F);
/*      */             }
/* 2627 */             catch (IOException iox) {
/*      */               
/* 2629 */               logger.log(Level.WARNING, performer.getName() + " " + iox.getMessage());
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/* 2634 */           if (numsTouched > 5) {
/*      */             
/*      */             try {
/*      */               
/* 2638 */               ((Player)performer).getSaveFile().setNumFaith((byte)0, System.currentTimeMillis());
/*      */             }
/* 2640 */             catch (IOException iOException) {}
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2646 */           performer.achievement(639);
/*      */         } 
/*      */       } 
/*      */     } 
/* 2650 */     return done;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MethodsReligion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */