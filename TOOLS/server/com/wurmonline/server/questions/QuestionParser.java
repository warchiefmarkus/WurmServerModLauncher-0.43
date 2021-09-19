/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.mesh.BushData;
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.mesh.TreeData;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.BehaviourDispatcher;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.behaviours.MethodsItems;
/*      */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*      */ import com.wurmonline.server.behaviours.NoSuchBehaviourException;
/*      */ import com.wurmonline.server.behaviours.WurmPermissions;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreaturePos;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.items.AdvancedCreationEntry;
/*      */ import com.wurmonline.server.items.InscriptionData;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.NotOwnedException;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.kingdom.Appointment;
/*      */ import com.wurmonline.server.kingdom.Appointments;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Cultist;
/*      */ import com.wurmonline.server.players.Cults;
/*      */ import com.wurmonline.server.players.Friend;
/*      */ import com.wurmonline.server.players.JournalTier;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.PlayerJournal;
/*      */ import com.wurmonline.server.players.Spawnpoint;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.skills.Affinities;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.skills.SkillTemplate;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.utils.NameCountList;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.GuardPlan;
/*      */ import com.wurmonline.server.villages.KosWarning;
/*      */ import com.wurmonline.server.villages.NoSuchRoleException;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.PvPAlliance;
/*      */ import com.wurmonline.server.villages.RecruitmentAd;
/*      */ import com.wurmonline.server.villages.RecruitmentAds;
/*      */ import com.wurmonline.server.villages.Reputation;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageRole;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.villages.WarDeclaration;
/*      */ import com.wurmonline.server.webinterface.WcKingdomChat;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
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
/*      */ public final class QuestionParser
/*      */   implements QuestionTypes, CounterTypes, ItemTypes, VillageStatus, TimeConstants, MonetaryConstants, MiscConstants, CreatureTemplateIds
/*      */ {
/*  147 */   private static Logger logger = Logger.getLogger(QuestionParser.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String legalChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,+/!() ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String numbers = "1234567890";
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String villageLegalChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- ";
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean containsIllegalCharacters(String name) {
/*  164 */     char[] chars = name.toCharArray();
/*      */     
/*  166 */     for (int x = 0; x < chars.length; x++) {
/*      */       
/*  168 */       if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,+/!() ".indexOf(chars[x]) < 0)
/*  169 */         return true; 
/*      */     } 
/*  171 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean containsIllegalVillageCharacters(String name) {
/*  176 */     char[] chars = name.toCharArray();
/*      */     
/*  178 */     for (int x = 0; x < chars.length; x++) {
/*      */       
/*  180 */       if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- ".indexOf(chars[x]) < 0)
/*  181 */         return true; 
/*      */     } 
/*  183 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseShutdownQuestion(ShutDownQuestion question) {
/*  193 */     int type = question.getType();
/*  194 */     Creature responder = question.getResponder();
/*  195 */     long target = question.getTarget();
/*  196 */     if (type == 13)
/*      */     {
/*  198 */       if (WurmId.getType(target) == 2 || WurmId.getType(target) == 19 || 
/*  199 */         WurmId.getType(target) == 20) {
/*      */         
/*      */         try {
/*      */           
/*  203 */           Item item = Items.getItem(target);
/*  204 */           if (item.getTemplateId() == 176 && responder.getPower() >= 3)
/*      */           {
/*  206 */             String minutest = question.getAnswer().getProperty("minutes");
/*  207 */             String secondst = question.getAnswer().getProperty("seconds");
/*  208 */             String reason = question.getAnswer().getProperty("reason");
/*      */             
/*      */             try {
/*  211 */               int minutes = Integer.parseInt(minutest);
/*  212 */               int seconds = Integer.parseInt(secondst);
/*  213 */               String globalStr = question.getAnswer().getProperty("global");
/*  214 */               boolean global = false;
/*  215 */               if (globalStr != null && globalStr.equals("true")) {
/*  216 */                 global = true;
/*      */               }
/*  218 */               if (global)
/*  219 */                 Servers.startShutdown(responder.getName(), minutes * 60 + seconds, reason); 
/*  220 */               if (Servers.isThisLoginServer() || !global)
/*      */               {
/*  222 */                 Server.getInstance().startShutdown(minutes * 60 + seconds, reason);
/*      */                 
/*  224 */                 logger.log(Level.INFO, responder.getName() + " shutting down server in " + minutes + " minutes and " + seconds + " seconds, reason: " + reason);
/*      */ 
/*      */                 
/*  227 */                 if (responder.getLogger() != null)
/*      */                 {
/*  229 */                   responder.getLogger().log(Level.INFO, responder
/*      */                       
/*  231 */                       .getName() + " shutting down server in " + minutes + " minutes and " + seconds + " seconds, reason: " + reason);
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             }
/*  238 */             catch (NumberFormatException nfe) {
/*      */               
/*  240 */               responder.getCommunicator().sendNormalServerMessage("Failed to parse " + minutest + " or " + secondst + " to a number. Please try again.");
/*      */             }
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*  246 */             logger.log(Level.WARNING, responder.getName() + " managed to try to shutdown with item " + item + ".");
/*  247 */             responder
/*  248 */               .getCommunicator()
/*  249 */               .sendNormalServerMessage("You can't shutdown with that. In fact you should not even manage to try. This has been logged.");
/*      */           }
/*      */         
/*      */         }
/*  253 */         catch (NoSuchItemException nsi) {
/*      */           
/*  255 */           logger.log(Level.WARNING, "Failed to locate item with id=" + target + " for which shutdown was intended.");
/*  256 */           responder.getCommunicator().sendNormalServerMessage("Failed to locate that item.");
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean containsNonNumber(String name) {
/*  264 */     char[] chars = name.toCharArray();
/*      */     
/*  266 */     for (int x = 0; x < chars.length; x++) {
/*      */       
/*  268 */       if ("1234567890".indexOf(chars[x]) < 0)
/*  269 */         return true; 
/*      */     } 
/*  271 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isMultiplierName(String answer) {
/*  276 */     if (answer.toLowerCase().startsWith("x") || answer.toLowerCase().endsWith("x"))
/*      */     {
/*  278 */       if (answer.length() > 1) {
/*      */         
/*  280 */         String rest = answer;
/*  281 */         if (answer.toLowerCase().startsWith("x")) {
/*  282 */           rest = answer.substring(1, answer.length());
/*      */         } else {
/*  284 */           rest = answer.substring(0, answer.length() - 1);
/*  285 */         }  if (containsNonNumber(rest)) {
/*  286 */           return false;
/*      */         }
/*  288 */         return true;
/*      */       } 
/*      */     }
/*  291 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static void parseTextInputQuestion(TextInputQuestion question, Item liquid) {
/*  296 */     int type = question.getType();
/*  297 */     Creature responder = question.getResponder();
/*  298 */     long target = question.getTarget();
/*  299 */     if (type == 0) {
/*      */       
/*  301 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*  304 */     if (type == 2) {
/*      */ 
/*      */       
/*  307 */       if (liquid != null && !Items.exists(liquid)) {
/*      */         
/*  309 */         responder.getCommunicator().sendNormalServerMessage("Your " + liquid
/*  310 */             .getName() + " you started inscribing with has vanished.");
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*      */       try {
/*  317 */         Item item = Items.getItem(target);
/*  318 */         if (item.getInscription() != null && responder.getPower() < 2) {
/*      */           
/*  320 */           responder.getCommunicator().sendNormalServerMessage("The " + item
/*  321 */               .getName() + " already have an inscription.");
/*      */           
/*      */           return;
/*      */         } 
/*  325 */         String answer = question.getAnswer().getProperty("answer").trim();
/*      */         
/*  327 */         if (InscriptionData.containsIllegalCharacters(answer) == true) {
/*      */           
/*  329 */           responder.getCommunicator().sendNormalServerMessage("The inscription contains some characters that are too complex for you to inscribe.");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*  334 */         if (answer.length() == 0) {
/*      */           
/*  336 */           responder.getCommunicator().sendNormalServerMessage("You decide not to inscribe the " + item
/*  337 */               .getName() + " at the moment.");
/*      */           
/*      */           return;
/*      */         } 
/*  341 */         int colour = 0;
/*  342 */         if (liquid != null && liquid.getTemplateId() != 753)
/*  343 */           colour = liquid.color; 
/*  344 */         item.setInscription(answer, responder.getName(), colour);
/*  345 */         responder.getCommunicator().sendNormalServerMessage("You carefully inscribe the " + item
/*  346 */             .getName() + " with " + answer.length() + " printed letters.");
/*  347 */         if (liquid != null) {
/*  348 */           liquid.setWeight(liquid.getWeightGrams() - 10, true);
/*      */         }
/*  350 */       } catch (NoSuchItemException nsi) {
/*      */         
/*  352 */         logger.log(Level.WARNING, "Failed to locate item with id=" + target + " for which an inscription was intended.");
/*  353 */         responder.getCommunicator().sendNormalServerMessage("Failed to locate that item.");
/*      */       }
/*      */     
/*      */     }
/*  357 */     else if (type == 1 && target != -10L) {
/*      */       
/*  359 */       if (WurmId.getType(target) == 2 || WurmId.getType(target) == 19 || 
/*  360 */         WurmId.getType(target) == 20) {
/*      */ 
/*      */         
/*      */         try {
/*  364 */           Item item = Items.getItem(target);
/*  365 */           if (!item.isNoRename()) {
/*      */             
/*  367 */             int templateId = item.getTemplateId();
/*  368 */             String answer = question.getAnswer().getProperty("answer");
/*  369 */             if (containsIllegalCharacters(answer)) {
/*      */               
/*  371 */               responder.getCommunicator().sendNormalServerMessage("The name contains illegal characters.");
/*      */               return;
/*      */             } 
/*  374 */             boolean updated = false;
/*  375 */             if (item.isSign()) {
/*      */               
/*  377 */               int mod = 1;
/*  378 */               if (templateId == 209)
/*  379 */                 mod = 2; 
/*  380 */               int maxSize = Math.max(5, (int)((item.getRarity() * 3) + item.getCurrentQualityLevel() * mod));
/*  381 */               if (answer.length() > maxSize) {
/*  382 */                 responder.getCommunicator().sendSafeServerMessage("The text is too long. Only " + maxSize + " letters can be imprinted on this sign.");
/*      */               }
/*      */               
/*  385 */               answer = answer.substring(0, Math.min(answer.length(), maxSize));
/*  386 */               String stype = question.getAnswer().getProperty("data1");
/*  387 */               if (stype != null && stype.length() > 0) {
/*      */                 
/*  389 */                 byte bt = Byte.parseByte(stype);
/*  390 */                 if (bt > 0 && bt <= 22) {
/*      */                   
/*  392 */                   item.setAuxData(bt);
/*      */                   
/*  394 */                   if (!item.setDescription(answer))
/*      */                   {
/*  396 */                     if (item.getZoneId() > 0 && item.getParentId() == -10L) {
/*      */                       
/*  398 */                       VolaTile t = Zones.getTileOrNull(item.getTileX(), item.getTileY(), item
/*  399 */                           .isOnSurface());
/*  400 */                       if (t != null)
/*      */                       {
/*  402 */                         t.renameItem(item);
/*      */                       }
/*      */                     } 
/*      */                   }
/*  406 */                   updated = true;
/*      */                 } 
/*      */               } 
/*      */             } 
/*  410 */             if (templateId == 521 && responder.getPower() > 0) {
/*  411 */               item.setName(answer);
/*  412 */             } else if (templateId == 651) {
/*      */               
/*  414 */               if (question.getOldtext().length() > 0)
/*      */               {
/*  416 */                 if (!item.getCreatorName().toLowerCase().equals(responder.getName().toLowerCase())) {
/*      */                   
/*  418 */                   responder.getCommunicator().sendNormalServerMessage("You can't change the recipient of the gift.");
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               }
/*  423 */               item.setDescription(answer);
/*  424 */               item.setName("From " + item.getSignature() + " to " + answer);
/*      */             }
/*  426 */             else if (templateId == 824) {
/*      */               
/*  428 */               if (answer.length() == 0)
/*  429 */                 responder.getCommunicator().sendNormalServerMessage("Groups must have a name."); 
/*  430 */               item.setName(answer);
/*      */             }
/*  432 */             else if (templateId == 1128) {
/*      */               
/*  434 */               if (answer.length() == 0)
/*  435 */                 responder.getCommunicator().sendNormalServerMessage("Almanac folders must have a name."); 
/*  436 */               item.setName(answer);
/*      */             } else {
/*  438 */               if (isMultiplierName(answer)) {
/*      */                 
/*  440 */                 responder.getCommunicator().sendNormalServerMessage("Starting or ending the description with x indicates a multiplier, which is not allowed.");
/*      */                 
/*      */                 return;
/*      */               } 
/*  444 */               if (!updated) {
/*  445 */                 item.setDescription(answer);
/*      */               }
/*      */             } 
/*      */           } else {
/*  449 */             logger.log(Level.WARNING, responder.getName() + " managed to try to rename item " + item.getName() + " which is non-renamable.");
/*      */             
/*  451 */             responder.getCommunicator().sendNormalServerMessage("You can't rename that.");
/*      */           }
/*      */         
/*  454 */         } catch (NoSuchItemException nsi) {
/*      */           
/*  456 */           logger.log(Level.WARNING, "Failed to locate item with id=" + target + " for which name change was intended.");
/*      */           
/*  458 */           responder.getCommunicator().sendNormalServerMessage("Failed to locate that item.");
/*      */         }
/*      */       
/*  461 */       } else if (WurmId.getType(target) == 4) {
/*      */ 
/*      */         
/*      */         try {
/*  465 */           Structure structure = Structures.getStructure(target);
/*  466 */           if (structure.isTypeHouse()) {
/*      */ 
/*      */             
/*  469 */             Item writ = Items.getItem(structure.getWritId());
/*  470 */             if (responder.getPower() < 2 && writ.getOwnerId() != responder.getWurmId()) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */           
/*  475 */           logger.log(Level.INFO, "Setting structure " + structure.getName() + " to " + question
/*  476 */               .getAnswer().getProperty("answer"));
/*  477 */           String answer = question.getAnswer().getProperty("answer");
/*  478 */           if (containsIllegalCharacters(answer)) {
/*      */             
/*  480 */             responder.getCommunicator().sendNormalServerMessage("The name contains illegal characters.");
/*      */             return;
/*      */           } 
/*  483 */           if (!structure.getName().equals(answer)) {
/*  484 */             structure.setName(answer, true);
/*      */           }
/*  486 */         } catch (NoSuchStructureException nss) {
/*      */           
/*  488 */           logger.log(Level.WARNING, "Failed to locate structure with id=" + target + " for which name change was intended.");
/*      */         
/*      */         }
/*  491 */         catch (NoSuchItemException nsi) {
/*      */           
/*  493 */           logger.log(Level.WARNING, "Failed to locate writ for structure with id=" + target + ".");
/*      */         }
/*      */       
/*      */       } 
/*  497 */     } else if (type == 1 && target == -10L) {
/*      */ 
/*      */       
/*  500 */       String answer = question.getAnswer().getProperty("answer").trim();
/*  501 */       if (containsIllegalCharacters(answer)) {
/*      */         
/*  503 */         responder.getCommunicator().sendNormalServerMessage("The name contains illegal characters.");
/*      */         return;
/*      */       } 
/*  506 */       if (isMultiplierName(answer)) {
/*      */         
/*  508 */         responder.getCommunicator().sendNormalServerMessage("Starting or ending the description with x indicates a multiplier, which is not allowed.");
/*      */         
/*      */         return;
/*      */       } 
/*  512 */       for (Item item : question.getItems()) {
/*      */         
/*  514 */         if (!item.isNoRename()) {
/*      */           
/*  516 */           int templateId = item.getTemplateId();
/*  517 */           if (templateId != 521 && templateId != 651 && templateId != 824 && 
/*      */ 
/*      */             
/*  520 */             !item.isCoin()) {
/*      */             
/*  522 */             item.setDescription(answer);
/*      */           } else {
/*      */             
/*  525 */             responder.getCommunicator().sendNormalServerMessage("Cannot rename " + item.getName() + ".");
/*      */           } 
/*      */         } else {
/*  528 */           responder.getCommunicator().sendNormalServerMessage("Cannot rename " + item.getName() + ".");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   static void parseStructureManagement(StructureManagement question) {
/*  535 */     int type = question.getType();
/*  536 */     Creature responder = question.getResponder();
/*  537 */     long target = question.getTarget();
/*  538 */     if (type == 0) {
/*      */       
/*  540 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*  543 */     if (WurmId.getType(target) == 4) {
/*      */       
/*      */       try {
/*      */         
/*  547 */         Structure structure = Structures.getStructure(target);
/*  548 */         Item writ = Items.getItem(structure.getWritId());
/*  549 */         if (writ.getOwnerId() != responder.getWurmId()) {
/*      */           return;
/*      */         }
/*      */         
/*  553 */         Properties props = question.getAnswer();
/*      */         
/*  555 */         for (Iterator<?> it = props.keySet().iterator(); it.hasNext(); ) {
/*      */           
/*  557 */           String key = (String)it.next();
/*  558 */           if (key.equals("demolish") && props.get(key).equals("true")) {
/*      */             
/*  560 */             structure.totallyDestroy();
/*      */             
/*      */             return;
/*      */           } 
/*  564 */           if (key.equals("allowAllies")) {
/*      */             
/*  566 */             if (props.get(key) != null && props.get(key).equals("true")) {
/*  567 */               structure.setAllowAllies(true); continue;
/*      */             } 
/*  569 */             structure.setAllowAllies(false); continue;
/*      */           } 
/*  571 */           if (key.equals("allowVillagers")) {
/*      */             
/*  573 */             if (props.get(key) != null && props.get(key).equals("true")) {
/*  574 */               structure.setAllowVillagers(true); continue;
/*      */             } 
/*  576 */             structure.setAllowVillagers(false); continue;
/*      */           } 
/*  578 */           if (key.equals("allowKingdom")) {
/*      */             
/*  580 */             if (props.get(key) != null && props.get(key).equals("true")) {
/*  581 */               structure.setAllowKingdom(true); continue;
/*      */             } 
/*  583 */             structure.setAllowKingdom(false); continue;
/*      */           } 
/*  585 */           if (key.charAt(0) == 'f') {
/*      */             
/*  587 */             boolean set = false;
/*      */             
/*      */             try {
/*  590 */               String val = props.getProperty(key);
/*  591 */               set = val.equals("true");
/*      */             }
/*  593 */             catch (Exception ex) {
/*      */               
/*  595 */               logger.log(Level.WARNING, "Failed to set " + props.getProperty(key) + " to a boolean.");
/*      */             } 
/*  597 */             if (set) {
/*      */               
/*  599 */               String fis = key.substring(1, key.length());
/*      */               
/*      */               try {
/*  602 */                 long fid = Long.parseLong(fis);
/*  603 */                 structure.addGuest(fid, 42);
/*      */               }
/*  605 */               catch (Exception ex) {
/*      */                 
/*  607 */                 logger.log(Level.WARNING, "Faiiled to add guest " + fis, ex);
/*      */               } 
/*      */             }  continue;
/*      */           } 
/*  611 */           if (key.charAt(0) == 'g') {
/*      */             
/*  613 */             boolean set = false;
/*      */             
/*      */             try {
/*  616 */               String val = props.getProperty(key);
/*  617 */               set = val.equals("true");
/*      */             }
/*  619 */             catch (Exception ex) {
/*      */               
/*  621 */               logger.log(Level.WARNING, "Failed to set " + props.getProperty(key) + " to a boolean.");
/*      */             } 
/*  623 */             if (set) {
/*      */               
/*  625 */               String gis = key.substring(1, key.length());
/*      */               
/*      */               try {
/*  628 */                 long gid = Long.parseLong(gis);
/*  629 */                 structure.removeGuest(gid);
/*      */               }
/*  631 */               catch (Exception ex) {
/*      */                 
/*  633 */                 logger.log(Level.WARNING, "Faiiled to remove guest " + gis, ex);
/*      */               } 
/*      */             }  continue;
/*      */           } 
/*  637 */           if (key.equals("lock")) {
/*      */             
/*  639 */             boolean set = false;
/*      */             
/*      */             try {
/*  642 */               String val = props.getProperty(key);
/*  643 */               set = val.equals("true");
/*      */             }
/*  645 */             catch (Exception ex) {
/*      */               
/*  647 */               logger.log(Level.WARNING, "Failed to set " + props.getProperty(key) + " to a boolean.");
/*      */             } 
/*  649 */             if (set)
/*  650 */               structure.lockAllDoors();  continue;
/*      */           } 
/*  652 */           if (key.equals("unlock")) {
/*      */             
/*  654 */             boolean set = false;
/*      */             
/*      */             try {
/*  657 */               String val = props.getProperty(key);
/*  658 */               set = val.equals("true");
/*      */             }
/*  660 */             catch (Exception ex) {
/*      */               
/*  662 */               logger.log(Level.WARNING, "Failed to set " + props.getProperty(key) + " to a boolean.");
/*      */             } 
/*  664 */             if (set)
/*  665 */               structure.unlockAllDoors();  continue;
/*      */           } 
/*  667 */           if (key.equals("sname")) {
/*      */             
/*  669 */             String name = props.getProperty("sname");
/*  670 */             if (name != null && 
/*  671 */               !name.equals(structure.getName())) {
/*      */               
/*  673 */               if (name.length() >= 41) {
/*      */                 
/*  675 */                 name = name.substring(0, 39);
/*  676 */                 responder.getCommunicator().sendNormalServerMessage("The name has been truncated to " + name + ".");
/*      */                 continue;
/*      */               } 
/*  679 */               if (name.length() < 3) {
/*      */                 
/*  681 */                 responder.getCommunicator().sendSafeServerMessage("Please select a longer name."); continue;
/*      */               } 
/*  683 */               if (containsIllegalCharacters(name)) {
/*      */                 
/*  685 */                 responder.getCommunicator().sendSafeServerMessage("The name " + name + " contain illegal characters. Please select another name.");
/*      */                 
/*      */                 continue;
/*      */               } 
/*      */               
/*  690 */               structure.setName(name, false);
/*      */               
/*      */               try {
/*  693 */                 writ = Items.getItem(structure.getWritId());
/*  694 */                 writ.setDescription(name);
/*      */               }
/*  696 */               catch (NoSuchItemException nsi) {
/*      */                 
/*  698 */                 logger.log(Level.WARNING, "Structure " + target + " has no writ with id " + structure
/*  699 */                     .getWritId() + "?", (Throwable)nsi);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  708 */           structure.save();
/*      */         }
/*  710 */         catch (IOException iox) {
/*      */           
/*  712 */           logger.log(Level.WARNING, "Failed to save structure " + target, iox);
/*      */         }
/*      */       
/*  715 */       } catch (NoSuchStructureException nss) {
/*      */         
/*  717 */         logger.log(Level.WARNING, "Failed to locate structure with id=" + target + " for which name change was intended.", (Throwable)nss);
/*      */       
/*      */       }
/*  720 */       catch (NoSuchItemException nsi) {
/*      */         
/*  722 */         logger.log(Level.WARNING, "Failed to locate writ for structure with id=" + target + ".", (Throwable)nsi);
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
/*      */   static void parseItemDataQuestion(ItemDataQuestion question) {
/*  734 */     int type = question.getType();
/*  735 */     Creature responder = question.getResponder();
/*  736 */     long target = question.getTarget();
/*  737 */     if (type == 0) {
/*      */       
/*  739 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*  742 */     if (type == 4)
/*      */     {
/*  744 */       if (WurmId.getType(target) == 2 || WurmId.getType(target) == 19 || 
/*  745 */         WurmId.getType(target) == 20) {
/*      */         
/*  747 */         String name = question.getAnswer().getProperty("itemName");
/*  748 */         String d1 = question.getAnswer().getProperty("data1");
/*  749 */         String d2 = question.getAnswer().getProperty("data2");
/*  750 */         String e1 = question.getAnswer().getProperty("extra1");
/*  751 */         String e2 = question.getAnswer().getProperty("extra2");
/*  752 */         String aux = "?";
/*      */         
/*      */         try {
/*  755 */           Item item = Items.getItem(target);
/*  756 */           String extra = "";
/*  757 */           if (name != null)
/*  758 */             item.setName(name, true); 
/*  759 */           int data1 = (d1 == null) ? -1 : Integer.parseInt(d1);
/*  760 */           int data2 = (d2 == null) ? -1 : Integer.parseInt(d2);
/*  761 */           int extra1 = (e1 == null) ? -1 : Integer.parseInt(e1);
/*  762 */           int extra2 = (e2 == null) ? -1 : Integer.parseInt(e2);
/*  763 */           if (item.hasData())
/*  764 */             item.setAllData(data1, data2, extra1, extra2); 
/*  765 */           byte auxd = 0;
/*  766 */           if (item.usesFoodState()) {
/*      */             
/*  768 */             String raux = question.getAnswer().getProperty("raux");
/*  769 */             auxd = Byte.parseByte(raux);
/*  770 */             if (Boolean.parseBoolean(question.getAnswer().getProperty("chopped")))
/*  771 */               auxd = (byte)(auxd + 16); 
/*  772 */             if (Boolean.parseBoolean(question.getAnswer().getProperty("mashed")))
/*  773 */               auxd = (byte)(auxd + 32); 
/*  774 */             if (Boolean.parseBoolean(question.getAnswer().getProperty("wrap")))
/*  775 */               auxd = (byte)(auxd + 64); 
/*  776 */             if (Boolean.parseBoolean(question.getAnswer().getProperty("fresh"))) {
/*  777 */               auxd = (byte)(auxd + 128);
/*      */             }
/*      */           } else {
/*      */             
/*  781 */             aux = question.getAnswer().getProperty("aux");
/*  782 */             auxd = (byte)Integer.parseInt(aux);
/*      */           } 
/*  784 */           item.setAuxData(auxd);
/*      */           
/*  786 */           String val = question.getAnswer().getProperty("dam");
/*  787 */           if (val != null) {
/*      */             
/*      */             try {
/*      */               
/*  791 */               item.setDamage(Float.parseFloat(val));
/*  792 */               extra = extra + ", dam=" + val;
/*      */             }
/*  794 */             catch (Exception exception) {}
/*      */           }
/*      */ 
/*      */           
/*  798 */           val = question.getAnswer().getProperty("temp");
/*  799 */           if (val != null) {
/*      */             
/*      */             try {
/*      */               
/*  803 */               item.setTemperature(Short.parseShort(val));
/*  804 */               extra = extra + ", temp=" + val;
/*      */             }
/*  806 */             catch (Exception exception) {}
/*      */           }
/*      */ 
/*      */           
/*  810 */           val = question.getAnswer().getProperty("weight");
/*  811 */           if (val != null) {
/*      */             
/*      */             try {
/*      */               
/*  815 */               if (Integer.parseInt(val) <= 0)
/*      */               {
/*  817 */                 responder.getCommunicator().sendNormalServerMessage("Weight cannot be below 1.");
/*      */               }
/*      */               else
/*      */               {
/*  821 */                 item.setWeight(Integer.parseInt(val), false);
/*  822 */                 extra = extra + ", weight=" + val;
/*      */               }
/*      */             
/*  825 */             } catch (Exception exception) {}
/*      */           }
/*      */ 
/*      */           
/*  829 */           val = question.getAnswer().getProperty("rarity");
/*  830 */           if (val != null) {
/*      */             
/*      */             try {
/*      */               
/*  834 */               byte vals = Byte.parseByte(val);
/*  835 */               if (vals < 0)
/*  836 */                 vals = 0; 
/*  837 */               if (vals > 3)
/*  838 */                 vals = 3; 
/*  839 */               extra = extra + ", rarity=" + vals;
/*  840 */               item.setRarity(vals);
/*      */             }
/*  842 */             catch (Exception exception) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  847 */           String fruit = question.getAnswer().getProperty("fruit");
/*  848 */           if (fruit != null) {
/*      */             
/*  850 */             ItemTemplate template = question.getTemplate(Integer.parseInt(fruit));
/*  851 */             if (template != null && template.getTemplateId() != item.getRealTemplateId()) {
/*      */               
/*  853 */               item.setRealTemplate(template.getTemplateId());
/*  854 */               extra = " and " + template.getName();
/*  855 */               responder.getCommunicator().sendUpdateInventoryItem(item);
/*      */             } 
/*  857 */             if (template == null && item.getRealTemplateId() != -10 && item
/*  858 */               .getTemplateId() != 1307) {
/*      */               
/*  860 */               item.setRealTemplate(-10);
/*  861 */               item.setName("fruit juice");
/*  862 */               responder.getCommunicator().sendUpdateInventoryItem(item);
/*      */             } 
/*      */           } 
/*  865 */           String red = question.getAnswer().getProperty("c_red");
/*  866 */           String green = question.getAnswer().getProperty("c_green");
/*  867 */           String blue = question.getAnswer().getProperty("c_blue");
/*  868 */           String tickp = question.getAnswer().getProperty("primary");
/*      */           
/*      */           try {
/*  871 */             int r = Integer.parseInt(red);
/*  872 */             int g = Integer.parseInt(green);
/*  873 */             int b = Integer.parseInt(blue);
/*  874 */             boolean tick = Boolean.parseBoolean(tickp);
/*  875 */             if (tick)
/*      */             {
/*  877 */               item.setColor(WurmColor.createColor((r < 0) ? 0 : r, (g < 0) ? 0 : g, (b < 0) ? 0 : b));
/*  878 */               extra = extra + ", color=[R:" + r + " G:" + g + " B:" + b + "]";
/*      */             }
/*      */             else
/*      */             {
/*  882 */               item.setColor(-1);
/*  883 */               extra = extra + ", color=none";
/*      */             }
/*      */           
/*  886 */           } catch (NumberFormatException|NullPointerException e) {
/*      */             
/*  888 */             item.setColor(-1);
/*  889 */             extra = extra + ", color=none";
/*      */           } 
/*  891 */           String tick2 = question.getAnswer().getProperty("secondary");
/*  892 */           if (tick2 != null) {
/*      */             
/*  894 */             String red2 = question.getAnswer().getProperty("c2_red");
/*  895 */             String green2 = question.getAnswer().getProperty("c2_green");
/*  896 */             String blue2 = question.getAnswer().getProperty("c2_blue");
/*      */             
/*      */             try {
/*  899 */               int r = Integer.parseInt(red2);
/*  900 */               int g = Integer.parseInt(green2);
/*  901 */               int b = Integer.parseInt(blue2);
/*  902 */               boolean tick = Boolean.parseBoolean(tick2);
/*  903 */               if (tick)
/*      */               {
/*  905 */                 item.setColor2(WurmColor.createColor((r < 0) ? 0 : r, (g < 0) ? 0 : g, (b < 0) ? 0 : b));
/*  906 */                 extra = extra + ", color2=[R:" + r + " G:" + g + " B:" + b + "]";
/*      */               }
/*      */               else
/*      */               {
/*  910 */                 item.setColor2(-1);
/*  911 */                 extra = extra + ", color2=none";
/*      */               }
/*      */             
/*  914 */             } catch (NumberFormatException|NullPointerException e) {
/*      */               
/*  916 */               item.setColor2(-1);
/*  917 */               extra = extra + ", color2=none";
/*      */             } 
/*      */           } 
/*  920 */           if (responder.getPower() >= 5) {
/*      */             
/*  922 */             long lastMaintained = Long.parseLong(question.getAnswer().getProperty("lastMaintained"));
/*  923 */             extra = extra + ", lastMaintained=" + lastMaintained;
/*  924 */             item.setLastMaintained(lastMaintained);
/*      */           } 
/*  926 */           String lastOwner = question.getAnswer().getProperty("lastowner");
/*      */           
/*      */           try {
/*  929 */             long lastOwnerId = Long.parseLong(lastOwner);
/*  930 */             if (lastOwnerId != item.getLastOwnerId())
/*      */             {
/*  932 */               extra = extra + ", lastowner=" + lastOwnerId;
/*  933 */               item.setLastOwnerId(lastOwnerId);
/*      */             }
/*      */           
/*  936 */           } catch (NumberFormatException|NullPointerException numberFormatException) {}
/*      */ 
/*      */ 
/*      */           
/*  940 */           responder.getCommunicator().sendNormalServerMessage("You quietly mumble: " + data1 + ", " + data2 + " as well as " + auxd + extra);
/*      */           
/*  942 */           if (responder.getLogger() != null) {
/*  943 */             responder.getLogger().info("Sets item data of " + target + " (" + item
/*  944 */                 .getName() + ") to : " + data1 + ", " + data2 + ", and aux: " + auxd + extra);
/*      */           }
/*      */         }
/*  947 */         catch (NoSuchItemException nsi) {
/*      */           
/*  949 */           logger.log(Level.WARNING, "Failed to locate item with id=" + target + " for which data change was intended.", (Throwable)nsi);
/*      */           
/*  951 */           responder.getCommunicator().sendNormalServerMessage("Failed to locate that item.");
/*      */         }
/*  953 */         catch (NumberFormatException nfe) {
/*      */           
/*  955 */           responder.getCommunicator().sendNormalServerMessage("You realize that something doesn't match your requirements. Did you mistype a number?");
/*  956 */           question.getAnswer().forEach((k, v) -> responder.getCommunicator().sendNormalServerMessage(k + " = " + v));
/*  957 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/*  959 */             logger.log(Level.FINE, responder.getName() + " realises that data1: " + d1 + ", data2: " + d2 + " or aux: " + aux + " doesn't match their requirements. " + nfe, nfe);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseTileDataQuestion(TileDataQuestion question) {
/*  969 */     int type = question.getType();
/*  970 */     Creature responder = question.getResponder();
/*  971 */     long target = question.getTarget();
/*  972 */     int tilex = question.getTilex();
/*  973 */     int tiley = question.getTiley();
/*  974 */     if (type == 0) {
/*      */       
/*  976 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*  979 */     if (type == 35)
/*      */     {
/*      */       
/*  982 */       if (WurmId.getType(target) == 2 || WurmId.getType(target) == 19 || 
/*  983 */         WurmId.getType(target) == 20) {
/*      */         
/*  985 */         String d1 = question.getAnswer().getProperty("surf");
/*  986 */         String d2 = question.getAnswer().getProperty("cave");
/*  987 */         boolean bot = Boolean.parseBoolean(question.getAnswer().getProperty("bot"));
/*  988 */         boolean forage = Boolean.parseBoolean(question.getAnswer().getProperty("forage"));
/*  989 */         boolean collect = Boolean.parseBoolean(question.getAnswer().getProperty("collect"));
/*  990 */         boolean transforming = Boolean.parseBoolean(question.getAnswer().getProperty("transforming"));
/*  991 */         boolean transformed = Boolean.parseBoolean(question.getAnswer().getProperty("transformed"));
/*  992 */         boolean hive = Boolean.parseBoolean(question.getAnswer().getProperty("hive"));
/*  993 */         boolean hasGrubs = Boolean.parseBoolean(question.getAnswer().getProperty("hasGrubs"));
/*      */         
/*  995 */         byte serverCaveFlags = Byte.parseByte(question.getAnswer().getProperty("caveserverflag"));
/*  996 */         byte clientCaveFlags = Byte.parseByte(question.getAnswer().getProperty("caveclientflag"));
/*      */ 
/*      */         
/*  999 */         short sHeight = Short.parseShort(question.getAnswer().getProperty("surfaceheight"));
/* 1000 */         short rHeight = Short.parseShort(question.getAnswer().getProperty("rockheight"));
/* 1001 */         short cHeight = Short.parseShort(question.getAnswer().getProperty("caveheight"));
/* 1002 */         byte cceil = Byte.parseByte(question.getAnswer().getProperty("caveceiling"));
/*      */         
/* 1004 */         int surfMesh = Server.surfaceMesh.getTile(tilex, tiley);
/* 1005 */         byte surfType = Tiles.decodeType(surfMesh);
/* 1006 */         short surfHeight = Tiles.decodeHeight(surfMesh);
/* 1007 */         short rockHeight = Tiles.decodeHeight(Server.rockMesh.getTile(tilex, tiley));
/* 1008 */         int caveMesh = Server.caveMesh.getTile(tilex, tiley);
/* 1009 */         short caveHeight = Tiles.decodeHeight(caveMesh);
/* 1010 */         byte caveCeiling = Tiles.decodeData(caveMesh);
/* 1011 */         boolean updateCave = false;
/*      */         
/* 1013 */         Tiles.Tile tile = Tiles.getTile(surfType);
/*      */ 
/*      */         
/*      */         try {
/* 1017 */           int data1 = Integer.parseInt(d1);
/* 1018 */           int data2 = Integer.parseInt(d2);
/*      */           
/* 1020 */           if (data1 >= 0 && (((surfType == 7 || surfType == 43) && data1 <= 2047) || data1 <= 65535)) {
/*      */ 
/*      */             
/* 1023 */             if (surfType == 7 || surfType == 43) {
/*      */               
/* 1025 */               int count = Integer.parseInt(question.getAnswer().getProperty("count"));
/* 1026 */               Server.setWorldResource(tilex, tiley, (count & 0x1F) << 11 | data1);
/*      */             }
/* 1028 */             else if (surfType == 1 || surfType == 2 || surfType == 10 || surfType == 22 || surfType == 6 || surfType == 18 || surfType == 24) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1033 */               int count = Integer.parseInt(question.getAnswer().getProperty("qlcnt"));
/* 1034 */               Server.setWorldResource(tilex, tiley, ((count & 0xFF) << 8) + (data1 & 0xFF));
/*      */             } else {
/*      */               
/* 1037 */               Server.setWorldResource(tilex, tiley, data1);
/*      */             } 
/*      */           } else {
/* 1040 */             responder.getCommunicator().sendNormalServerMessage("Surface resource must be 0-32767");
/*      */           } 
/* 1042 */           Server.setBotanizable(tilex, tiley, bot);
/* 1043 */           Server.setForagable(tilex, tiley, forage);
/* 1044 */           Server.setGatherable(tilex, tiley, collect);
/* 1045 */           Server.setBeingTransformed(tilex, tiley, transforming);
/* 1046 */           Server.setTransformed(tilex, tiley, transformed);
/* 1047 */           Server.setCheckHive(tilex, tiley, hive);
/* 1048 */           Server.setGrubs(tilex, tiley, hasGrubs);
/*      */           
/* 1050 */           Server.setClientCaveFlags(tilex, tiley, clientCaveFlags);
/* 1051 */           Server.setServerCaveFlags(tilex, tiley, serverCaveFlags);
/*      */           
/* 1053 */           if (data2 <= 65535 && data2 >= 0) {
/* 1054 */             Server.setCaveResource(tilex, tiley, data2);
/*      */           } else {
/* 1056 */             responder.getCommunicator().sendNormalServerMessage("Cave resource must be 0-65535");
/*      */           } 
/* 1058 */           if (Math.abs(sHeight - surfHeight) <= 300) {
/* 1059 */             surfHeight = sHeight;
/*      */           } else {
/* 1061 */             responder.getCommunicator().sendNormalServerMessage("Unable to change the surface layer height by more than 300 dirt.");
/*      */           } 
/* 1063 */           if (Math.abs(rHeight - rockHeight) <= 300) {
/* 1064 */             rockHeight = rHeight;
/*      */           } else {
/* 1066 */             responder.getCommunicator().sendNormalServerMessage("Unable to change the rock layer height by more than 300 dirt.");
/*      */           } 
/* 1068 */           if (rockHeight > surfHeight) {
/* 1069 */             surfHeight = rockHeight;
/*      */           }
/*      */           
/* 1072 */           if (caveHeight != cHeight || caveCeiling != cceil) {
/*      */             
/* 1074 */             caveHeight = cHeight;
/* 1075 */             caveCeiling = cceil;
/* 1076 */             updateCave = true;
/*      */           } 
/*      */           
/* 1079 */           byte surfaceTileData = 0;
/* 1080 */           if (tile.isGrass() || surfType == 10) {
/*      */ 
/*      */             
/* 1083 */             int ggrowth = Integer.parseInt(question.getAnswer().getProperty("growth"));
/* 1084 */             int gflower = Integer.parseInt(question.getAnswer().getProperty("flower"));
/* 1085 */             surfaceTileData = (byte)((ggrowth & 0x3) << 6 | gflower & 0xF & 0xFF);
/*      */           }
/* 1087 */           else if (surfType == 31 || surfType == 34 || surfType == 3 || surfType == 14 || surfType == 35 || surfType == 11) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1092 */             int tage = Integer.parseInt(question.getAnswer().getProperty("age"));
/* 1093 */             int ttype = Integer.parseInt(question.getAnswer().getProperty("type"));
/* 1094 */             if (surfType == 31 || surfType == 34 || surfType == 35) {
/*      */ 
/*      */               
/* 1097 */               surfaceTileData = (byte)(BushData.BushType.encodeTileData(tage, ttype) & 0xFF);
/*      */             } else {
/*      */               
/* 1100 */               surfaceTileData = (byte)(TreeData.TreeType.encodeTileData(tage, ttype) & 0xFF);
/*      */             }
/*      */           
/* 1103 */           } else if (tile.usesNewData()) {
/*      */ 
/*      */             
/* 1106 */             byte tage = Byte.parseByte(question.getAnswer().getProperty("age"));
/* 1107 */             int harvestable = Integer.parseInt(question.getAnswer().getProperty("harvestable"));
/* 1108 */             int incentre = Integer.parseInt(question.getAnswer().getProperty("incentre"));
/* 1109 */             int growth = Integer.parseInt(question.getAnswer().getProperty("growth"));
/* 1110 */             FoliageAge age = FoliageAge.fromByte(tage);
/* 1111 */             GrassData.GrowthTreeStage grass = GrassData.GrowthTreeStage.fromInt(growth);
/* 1112 */             surfaceTileData = Tiles.encodeTreeData(age, (harvestable != 0), (incentre != 0), grass);
/*      */           }
/* 1114 */           else if (surfType == 7 || surfType == 43) {
/*      */ 
/*      */             
/* 1117 */             boolean ftended = Boolean.parseBoolean(question.getAnswer().getProperty("tended"));
/* 1118 */             int fage = Integer.parseInt(question.getAnswer().getProperty("age"));
/* 1119 */             int fcrop = Integer.parseInt(question.getAnswer().getProperty("crop"));
/* 1120 */             surfaceTileData = (byte)((ftended ? 128 : 0) | (fage & 0x7) << 4 | fcrop & 0xF & 0xFF);
/*      */           }
/* 1122 */           else if (Tiles.isRoadType(surfType)) {
/*      */             
/* 1124 */             int funused = Integer.parseInt(question.getAnswer().getProperty("unused"));
/* 1125 */             int fdir = Integer.parseInt(question.getAnswer().getProperty("dir"));
/* 1126 */             surfaceTileData = (byte)((funused & 0x1F) << 3 | fdir & 0x7 & 0xFF);
/*      */           }
/*      */           else {
/*      */             
/* 1130 */             String d3 = question.getAnswer().getProperty("surftiledata");
/* 1131 */             surfaceTileData = Byte.parseByte(d3);
/*      */           } 
/* 1133 */           Server.setSurfaceTile(tilex, tiley, surfHeight, surfType, surfaceTileData);
/* 1134 */           Server.rockMesh.setTile(tilex, tiley, Tiles.encode(rockHeight, (short)0));
/* 1135 */           if (updateCave) {
/*      */             
/* 1137 */             byte ctype = Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley));
/* 1138 */             Server.caveMesh.setTile(tilex, tiley, Tiles.encode(caveHeight, ctype, caveCeiling));
/*      */           } 
/*      */           
/* 1141 */           int surf = Server.getWorldResource(tilex, tiley);
/* 1142 */           int flag = Server.getServerSurfaceFlags(tilex, tiley);
/* 1143 */           responder.getCommunicator()
/* 1144 */             .sendNormalServerMessage("You quietly mumble: " + tilex + "," + tiley + ":flags:" + flag + ":d1:" + surf + ":d2:" + data2 + "," + (surfaceTileData & 0xFF));
/*      */ 
/*      */ 
/*      */           
/* 1148 */           if (responder.getLogger() != null) {
/* 1149 */             responder.getLogger().info("Sets tile data of " + tilex + "," + tiley + " to flags:" + flag + ":d1:" + surf + ":d2:" + data2 + "," + (surfaceTileData & 0xFF));
/*      */           }
/*      */ 
/*      */           
/* 1153 */           Players.getInstance().sendChangedTile(tilex, tiley, true, true);
/*      */         }
/* 1155 */         catch (NumberFormatException nfe) {
/*      */           
/* 1157 */           responder.getCommunicator().sendNormalServerMessage("You realize that " + d1 + " or " + d2 + " doesn't match your requirements.");
/*      */           
/* 1159 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/* 1161 */             logger.log(Level.FINE, responder.getName() + " realises that surface resource: " + d1 + " or cave resource: " + d2 + " doesn't match their requirements. " + nfe, nfe);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseTeleportQuestion(TeleportQuestion question) {
/* 1171 */     int type = question.getType();
/* 1172 */     Creature responder = question.getResponder();
/* 1173 */     long target = question.getTarget();
/* 1174 */     if (type == 0) {
/*      */       
/* 1176 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 1179 */     if (type == 17)
/*      */     {
/* 1181 */       if (WurmId.getType(target) == 2 || WurmId.getType(target) == 19 || 
/* 1182 */         WurmId.getType(target) == 20) {
/*      */         
/* 1184 */         String d1 = question.getAnswer().getProperty("data1");
/* 1185 */         String d2 = question.getAnswer().getProperty("data2");
/* 1186 */         String wid = question.getAnswer().getProperty("wurmid");
/* 1187 */         String vid = question.getAnswer().getProperty("villid");
/* 1188 */         if (d1.equals("-1") || d2.equals("-1")) {
/*      */ 
/*      */           
/*      */           try {
/* 1192 */             int vidd = Integer.parseInt(vid);
/* 1193 */             if (vidd == 0)
/*      */             {
/* 1195 */               int listid = Integer.parseInt(wid);
/*      */               
/* 1197 */               Player p = question.getPlayer(listid);
/* 1198 */               if (p == null) {
/*      */                 
/* 1200 */                 responder.getCommunicator().sendNormalServerMessage("No player found.");
/*      */                 return;
/*      */               } 
/* 1203 */               int tx = p.getTileX();
/* 1204 */               int ty = p.getTileY();
/* 1205 */               responder.setTeleportLayer(p.isOnSurface() ? 0 : -1);
/* 1206 */               responder.setTeleportFloorLevel(p.getFloorLevel());
/* 1207 */               Item item = Items.getItem(target);
/* 1208 */               if (!p.isOnSurface())
/*      */               {
/* 1210 */                 if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tx, ty)))) {
/*      */                   
/* 1212 */                   responder.getCommunicator().sendNormalServerMessage("The tile " + tx + ", " + ty + " is solid cave.");
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               }
/*      */               
/* 1218 */               item.setData(tx, ty);
/* 1219 */               responder.getLogger().log(Level.INFO, "Located " + p.getName() + " at " + tx + ", " + ty);
/* 1220 */               responder.getCommunicator().sendNormalServerMessage("You quietly mumble: " + p
/* 1221 */                   .getName() + "... " + tx + ", " + ty);
/* 1222 */               if (responder.getPower() >= 5) {
/*      */                 try
/*      */                 {
/*      */                   
/* 1226 */                   Zone z = Zones.getZone(tx, ty, (responder.getTeleportLayer() >= 0));
/* 1227 */                   responder.getCommunicator().sendNormalServerMessage("That zone is number " + z
/* 1228 */                       .getId() + " x=" + z.getStartX() + " to " + z.getEndX() + " and " + z
/* 1229 */                       .getStartY() + " to " + z.getEndY() + ". Size=" + z.getSize());
/*      */                 }
/* 1231 */                 catch (Exception e)
/*      */                 {
/* 1233 */                   responder.getCommunicator().sendNormalServerMessage("Exception: " + e.getMessage());
/* 1234 */                   logger.warning(responder.getName() + " had problems getting zone information while teleporting, data1: " + tx + ", data2: " + ty + ", layer: " + responder
/*      */                       
/* 1236 */                       .getTeleportLayer() + ", wurmid: " + wid + ", villageid: " + vid);
/*      */                 }
/*      */               
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 1243 */               vidd--;
/* 1244 */               Village village = question.getVillage(vidd);
/* 1245 */               if (village == null) {
/*      */                 
/* 1247 */                 responder.getCommunicator().sendNormalServerMessage("No village found.");
/*      */                 return;
/*      */               } 
/* 1250 */               int tx = village.getTokenX();
/* 1251 */               int ty = village.getTokenY();
/* 1252 */               Item item = Items.getItem(target);
/* 1253 */               responder.setTeleportLayer(village.isOnSurface() ? 0 : -1);
/* 1254 */               if (!village.isOnSurface())
/*      */               {
/* 1256 */                 if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tx, ty)))) {
/*      */                   
/* 1258 */                   responder.getCommunicator().sendNormalServerMessage("The tile " + tx + ", " + ty + " is solid cave.");
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               }
/* 1263 */               item.setData(tx, ty);
/*      */               
/* 1265 */               responder.getCommunicator().sendNormalServerMessage("You quietly mumble: " + village
/* 1266 */                   .getName() + "... " + tx + ", " + ty);
/*      */             }
/*      */           
/* 1269 */           } catch (NoSuchItemException nsi) {
/*      */             
/* 1271 */             logger.log(Level.WARNING, "Failed to locate item with id=" + target + " for which name change was intended.", (Throwable)nsi);
/*      */             
/* 1273 */             responder.getCommunicator().sendNormalServerMessage("Failed to locate that item.");
/*      */           }
/* 1275 */           catch (NumberFormatException nfe) {
/*      */             
/* 1277 */             responder.getCommunicator().sendNormalServerMessage("You realize that the player id " + wid + " doesn't match your requirements.");
/*      */             
/* 1279 */             if (logger.isLoggable(Level.FINE))
/*      */             {
/* 1281 */               logger.log(Level.FINE, responder.getName() + " realises that player id " + wid + " doesn't match their requirements. " + nfe, nfe);
/*      */             }
/*      */           } 
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 1290 */             Item item = Items.getItem(target);
/* 1291 */             int data1 = Integer.parseInt(d1);
/* 1292 */             int data2 = Integer.parseInt(d2);
/* 1293 */             String surfaced = question.getAnswer().getProperty("layer");
/* 1294 */             byte layer = 0;
/* 1295 */             if (surfaced != null && surfaced.equals("1"))
/* 1296 */               layer = -1; 
/* 1297 */             responder.setTeleportLayer(layer);
/* 1298 */             if (layer < 0)
/*      */             {
/* 1300 */               if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(data1, data2)))) {
/*      */                 
/* 1302 */                 responder.getCommunicator().sendNormalServerMessage("The tile " + data1 + ", " + data2 + " is solid cave.");
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             }
/* 1307 */             item.setData(data1, data2);
/*      */             
/* 1309 */             responder.getCommunicator().sendNormalServerMessage("You quietly mumble: " + data1 + ", " + data2 + " surfaced=" + ((layer == 0) ? 1 : 0));
/*      */             
/* 1311 */             if (responder.getPower() >= 5) {
/*      */               try
/*      */               {
/*      */                 
/* 1315 */                 Zone z = Zones.getZone(data1, data2, (layer == 0));
/* 1316 */                 responder.getCommunicator().sendNormalServerMessage("That zone is number " + z
/* 1317 */                     .getId() + " x=" + z.getStartX() + " to " + z.getEndX() + " and " + z
/* 1318 */                     .getStartY() + " to " + z.getEndY() + ". Size=" + z.getSize());
/*      */               }
/* 1320 */               catch (Exception e)
/*      */               {
/* 1322 */                 responder.getCommunicator().sendNormalServerMessage("Exception: " + e.getMessage());
/* 1323 */                 logger.warning(responder.getName() + " had problems getting zone information while teleporting, data1: " + data1 + ", data2: " + data2 + ", layer: " + layer + ", wurmid: " + wid + ", villageid: " + vid);
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/*      */           }
/* 1330 */           catch (NoSuchItemException nsi) {
/*      */             
/* 1332 */             logger.log(Level.WARNING, "Failed to locate item with id=" + target + " for which name change was intended.", (Throwable)nsi);
/*      */             
/* 1334 */             responder.getCommunicator().sendNormalServerMessage("Failed to locate that item.");
/*      */           }
/* 1336 */           catch (NumberFormatException nfe) {
/*      */             
/* 1338 */             responder.getCommunicator().sendNormalServerMessage("You realize that " + d1 + " or " + d2 + " doesn't match your requirements.");
/*      */             
/* 1340 */             if (logger.isLoggable(Level.FINE))
/*      */             {
/* 1342 */               logger.log(Level.FINE, responder.getName() + " realises that " + d1 + " or " + d2 + " doesn't match their requirements. " + nfe, nfe);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseItemCreationQuestion(ItemCreationQuestion question) {
/* 1353 */     int type = question.getType();
/* 1354 */     Creature responder = question.getResponder();
/* 1355 */     long target = question.getTarget();
/* 1356 */     if (type == 0) {
/*      */       
/* 1358 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 1361 */     if (type == 5)
/*      */     {
/* 1363 */       if (WurmId.getType(target) == 2 || WurmId.getType(target) == 19 || 
/* 1364 */         WurmId.getType(target) == 20) {
/*      */         
/* 1366 */         String d1 = question.getAnswer().getProperty("data1");
/* 1367 */         String d2 = question.getAnswer().getProperty("data2");
/*      */         
/* 1369 */         String sm = question.getAnswer().getProperty("sizemod");
/*      */         
/* 1371 */         String number = question.getAnswer().getProperty("number");
/* 1372 */         String materialString = question.getAnswer().getProperty("material");
/* 1373 */         String alltypes = question.getAnswer().getProperty("alltypes");
/* 1374 */         boolean allMaterialTypes = (alltypes != null && alltypes.equals("true"));
/* 1375 */         int maxWoodTypes = 16;
/* 1376 */         byte[] woodTypes = MethodsItems.getAllNormalWoodTypes();
/* 1377 */         byte[] metalTypes = MethodsItems.getAllMetalTypes();
/* 1378 */         String rareString = question.getAnswer().getProperty("rare");
/* 1379 */         byte material = 0;
/*      */         
/*      */         try {
/* 1382 */           Item item = Items.getItem(target);
/*      */           
/* 1384 */           long now = System.currentTimeMillis();
/* 1385 */           if (Servers.localServer.testServer || ((item
/* 1386 */             .getTemplateId() == 176 || item.getTemplateId() == 301) && (responder
/* 1387 */             .getPower() >= 2 || Players.isArtist(question.getResponder().getWurmId(), false, false)))) {
/*      */ 
/*      */             
/* 1390 */             int data1 = Integer.parseInt(d1);
/* 1391 */             int data2 = Integer.parseInt(d2);
/* 1392 */             int num = Integer.parseInt(number);
/* 1393 */             byte rare = Byte.parseByte(rareString);
/* 1394 */             String name = question.getAnswer().getProperty("itemName");
/* 1395 */             String red = question.getAnswer().getProperty("c_red");
/* 1396 */             String green = question.getAnswer().getProperty("c_green");
/* 1397 */             String blue = question.getAnswer().getProperty("c_blue");
/* 1398 */             int colour = -1;
/* 1399 */             if (red != null && green != null && blue != null && red.length() > 0 && green.length() > 0 && blue.length() > 0) {
/*      */               
/*      */               try {
/*      */                 
/* 1403 */                 int r = Integer.parseInt(red);
/* 1404 */                 int g = Integer.parseInt(green);
/* 1405 */                 int b = Integer.parseInt(blue);
/* 1406 */                 colour = WurmColor.createColor((r < 0) ? 0 : r, (g < 0) ? 0 : g, (b < 0) ? 0 : b);
/*      */               }
/* 1408 */               catch (NumberFormatException|NullPointerException e) {
/*      */                 
/* 1410 */                 logger.log(Level.WARNING, "Bad colours:" + red + "," + green + "," + blue);
/*      */               } 
/*      */             }
/*      */             
/*      */             try {
/* 1415 */               material = Byte.parseByte(materialString);
/*      */             }
/* 1417 */             catch (NumberFormatException nfe) {
/*      */               
/* 1419 */               logger.log(Level.WARNING, "Material was " + materialString);
/*      */             } 
/* 1421 */             ItemTemplate template = question.getTemplate(data1);
/* 1422 */             if (template == null) {
/*      */               
/* 1424 */               responder.getCommunicator().sendNormalServerMessage("You decide not to create anything.");
/*      */               
/*      */               return;
/*      */             } 
/* 1428 */             float sizemod = 1.0F;
/* 1429 */             if (sm != null && sm.length() > 0) {
/*      */               
/*      */               try {
/*      */                 
/* 1433 */                 sizemod = Math.abs(Float.parseFloat(sm));
/* 1434 */                 if (template.getTemplateId() != 995) {
/* 1435 */                   sizemod = Math.min(5.0F, sizemod);
/*      */                 }
/* 1437 */               } catch (NumberFormatException nfen1) {
/*      */                 
/* 1439 */                 responder.getCommunicator().sendAlertServerMessage("The size mod " + sm + " is not a float value.");
/*      */               } 
/*      */             }
/*      */             
/* 1443 */             if (template.getTemplateId() != 179 && template
/* 1444 */               .getTemplateId() != 386) {
/*      */               
/* 1446 */               if (num != 1 || material != 0)
/* 1447 */                 allMaterialTypes = false; 
/* 1448 */               if (!template.isWood() && !template.isMetal())
/* 1449 */                 allMaterialTypes = false; 
/* 1450 */               if (num == 1 && allMaterialTypes)
/*      */               {
/* 1452 */                 if (template.isWood()) {
/*      */                   
/* 1454 */                   if (template.getTemplateId() == 65 || template.getTemplateId() == 413) {
/* 1455 */                     num = woodTypes.length;
/*      */                   } else {
/* 1457 */                     num = maxWoodTypes;
/*      */                   } 
/*      */                 } else {
/* 1460 */                   num = metalTypes.length;
/*      */                 }  } 
/* 1462 */               for (int x = 0; x < num; x++) {
/*      */                 
/* 1464 */                 data2 = Math.min(100, data2);
/* 1465 */                 data2 = Math.max(1, data2);
/* 1466 */                 Item newItem = null;
/* 1467 */                 if (!Servers.localServer.testServer && responder.getPower() <= 3) {
/*      */                   
/* 1469 */                   if (material == 0)
/* 1470 */                     material = template.getMaterial(); 
/* 1471 */                   newItem = ItemFactory.createItem(387, data2, material, rare, responder
/* 1472 */                       .getName());
/* 1473 */                   newItem.setRealTemplate(template.getTemplateId());
/* 1474 */                   if (template.getTemplateId() == 729) {
/* 1475 */                     newItem.setName("This cake is a lie!");
/*      */                   } else {
/* 1477 */                     newItem.setName("weird " + ItemFactory.generateName(template, material));
/*      */                   } 
/*      */                 } else {
/*      */                   
/* 1481 */                   int t = template.getTemplateId();
/* 1482 */                   int color = colour;
/* 1483 */                   if (template.isColor) {
/*      */                     
/* 1485 */                     t = 438;
/* 1486 */                     if (colour == -1)
/* 1487 */                       color = WurmColor.getInitialColor(template.getTemplateId(), Math.max(1, data2)); 
/*      */                   } 
/* 1489 */                   if (allMaterialTypes && template.isWood()) {
/* 1490 */                     newItem = ItemFactory.createItem(t, data2, woodTypes[x], rare, responder.getName());
/* 1491 */                   } else if (allMaterialTypes && template.isMetal()) {
/* 1492 */                     newItem = ItemFactory.createItem(t, data2, metalTypes[x], rare, responder.getName());
/* 1493 */                   } else if (material != 0) {
/* 1494 */                     newItem = ItemFactory.createItem(t, data2, material, rare, responder.getName());
/*      */                   } else {
/* 1496 */                     newItem = ItemFactory.createItem(t, data2, rare, responder.getName());
/* 1497 */                   }  if (t != -1 && color != -1)
/* 1498 */                     newItem.setColor(color); 
/* 1499 */                   if (name != null && name.length() > 0)
/* 1500 */                     newItem.setName(name, true); 
/* 1501 */                   if (template.getTemplateId() == 175)
/* 1502 */                     newItem.setAuxData((byte)2); 
/*      */                 } 
/* 1504 */                 if (newItem.getTemplateId() == 995)
/*      */                 {
/* 1506 */                   if (sizemod > 0.0F) {
/*      */                     
/* 1508 */                     newItem.setAuxData((byte)(int)sizemod);
/* 1509 */                     if (newItem.getAuxData() > 4)
/* 1510 */                       newItem.setRarity((byte)2); 
/* 1511 */                     newItem.fillTreasureChest();
/*      */                   } 
/*      */                 }
/* 1514 */                 if (newItem.isCoin()) {
/*      */                   
/* 1516 */                   long val = Economy.getValueFor(template.getTemplateId());
/* 1517 */                   if (val * num > 500000000L) {
/*      */                     
/* 1519 */                     responder.getCommunicator().sendNormalServerMessage("You aren't allowed to create that amount of money.");
/*      */                     
/* 1521 */                     responder.getLogger().log(Level.WARNING, responder
/*      */                         
/* 1523 */                         .getName() + " tried to create " + num + " " + newItem.getName() + " but wasn't allowed to.");
/*      */                     
/*      */                     return;
/*      */                   } 
/* 1527 */                   Change change = new Change(val);
/*      */                   
/* 1529 */                   long newGold = change.getGoldCoins();
/* 1530 */                   if (newGold > 0L) {
/*      */                     
/* 1532 */                     long oldGold = Economy.getEconomy().getGold();
/* 1533 */                     Economy.getEconomy().updateCreatedGold(oldGold + newGold);
/*      */                   } 
/* 1535 */                   long newCopper = change.getCopperCoins();
/* 1536 */                   if (newCopper > 0L) {
/*      */                     
/* 1538 */                     long oldCopper = Economy.getEconomy().getCopper();
/* 1539 */                     Economy.getEconomy().updateCreatedCopper(oldCopper + newCopper);
/*      */                   } 
/* 1541 */                   long newSilver = change.getSilverCoins();
/* 1542 */                   if (newSilver > 0L) {
/*      */                     
/* 1544 */                     long oldSilver = Economy.getEconomy().getSilver();
/* 1545 */                     Economy.getEconomy().updateCreatedSilver(oldSilver + newSilver);
/*      */                   } 
/* 1547 */                   long newIron = change.getIronCoins();
/* 1548 */                   if (newIron > 0L) {
/*      */                     
/* 1550 */                     long oldIron = Economy.getEconomy().getIron();
/* 1551 */                     Economy.getEconomy().updateCreatedIron(oldIron + newIron);
/*      */                   } 
/*      */                 } 
/* 1554 */                 if (responder.getLogger() != null) {
/* 1555 */                   responder.getLogger().log(Level.INFO, responder
/*      */                       
/* 1557 */                       .getName() + " created item " + newItem.getName() + ", item template: " + newItem.getTemplate().getName() + ", WurmID: " + newItem
/* 1558 */                       .getWurmId() + ", QL: " + newItem.getQualityLevel() + ", Rarity: " + newItem.getRarity());
/* 1559 */                 } else if (responder.getPower() != 0) {
/* 1560 */                   logger.log(Level.INFO, responder.getName() + " created item " + newItem.getName() + ", WurmID: " + newItem
/* 1561 */                       .getWurmId() + ", QL: " + newItem.getQualityLevel());
/* 1562 */                 }  if (sizemod != 1.0F)
/*      */                 {
/* 1564 */                   if (template.getTemplateId() != 995) {
/*      */                     
/* 1566 */                     newItem.setWeight((int)Math.max(1.0F, sizemod * template.getWeightGrams()), false);
/* 1567 */                     newItem.setSizes(newItem.getWeightGrams());
/*      */                   } 
/*      */                 }
/* 1570 */                 Item inventory = responder.getInventory();
/* 1571 */                 if (newItem.isKingdomMarker() || (newItem
/* 1572 */                   .isWind() && template.getTemplateId() == 579) || template
/* 1573 */                   .getTemplateId() == 578 || template.getTemplateId() == 999)
/*      */                 {
/* 1575 */                   newItem.setAuxData(responder.getKingdomId());
/*      */                 }
/* 1577 */                 if (newItem.isLock() && newItem.getTemplateId() != 167) {
/*      */                   
/*      */                   try {
/*      */                     
/* 1581 */                     Item key = ItemFactory.createItem(168, newItem.getQualityLevel(), responder
/* 1582 */                         .getName());
/* 1583 */                     key.setMaterial(newItem.getMaterial());
/* 1584 */                     inventory.insertItem(key);
/* 1585 */                     newItem.addKey(key.getWurmId());
/* 1586 */                     key.setLockId(newItem.getWurmId());
/*      */                   }
/* 1588 */                   catch (NoSuchTemplateException nst) {
/*      */                     
/* 1590 */                     logger.log(Level.WARNING, responder
/* 1591 */                         .getName() + " failed to create key: " + nst.getMessage(), (Throwable)nst);
/*      */                   }
/* 1593 */                   catch (FailedException fe) {
/*      */                     
/* 1595 */                     logger.log(Level.WARNING, responder
/* 1596 */                         .getName() + " failed to create key: " + fe.getMessage(), (Throwable)fe);
/*      */                   } 
/*      */                 }
/* 1599 */                 Item container = null;
/* 1600 */                 if (newItem.isLiquid()) {
/*      */                   
/* 1602 */                   if (template.getTemplateId() == 654) {
/*      */                     
/* 1604 */                     container = ItemFactory.createItem(653, 99.0F, responder.getName());
/* 1605 */                     responder.getInventory().insertItem(container, true);
/* 1606 */                     container.insertItem(newItem);
/*      */                   }
/*      */                   else {
/*      */                     
/* 1610 */                     Item[] allItems = inventory.getAllItems(false);
/* 1611 */                     for (int a = 0; a < allItems.length; a++) {
/*      */                       
/* 1613 */                       if (allItems[a].isContainerLiquid() && allItems[a].isEmpty(false))
/*      */                       {
/* 1615 */                         if (allItems[a].insertItem(newItem)) {
/*      */                           
/* 1617 */                           container = allItems[a];
/* 1618 */                           newItem.setWeight(container.getFreeVolume(), false);
/*      */                           break;
/*      */                         } 
/*      */                       }
/*      */                     } 
/*      */                   } 
/* 1624 */                   if (container != null) {
/*      */                     
/* 1626 */                     if (item.getTemplateId() == 301)
/*      */                     {
/* 1628 */                       responder.getCommunicator().sendNormalServerMessage("You pour some " + newItem
/* 1629 */                           .getNameWithGenus() + " from the horn into the " + container
/* 1630 */                           .getName() + ".");
/* 1631 */                       Server.getInstance()
/* 1632 */                         .broadCastAction(responder
/* 1633 */                           .getName() + " pours something out of a huge goat horn full of fruit and flowers.", responder, 5);
/*      */                     
/*      */                     }
/*      */                     else
/*      */                     {
/*      */                       
/* 1639 */                       responder.getCommunicator().sendNormalServerMessage("You wave your wand and create " + newItem
/* 1640 */                           .getName() + " in " + container
/* 1641 */                           .getNameWithGenus() + " [" + container.getDescription() + "].");
/*      */                       
/* 1643 */                       Server.getInstance().broadCastAction(responder
/* 1644 */                           .getName() + " waves a black wand vividly.", responder, 5);
/*      */                     }
/*      */                   
/*      */                   } else {
/*      */                     
/* 1649 */                     responder.getCommunicator()
/* 1650 */                       .sendNormalServerMessage("You need an empty container to put the " + newItem
/* 1651 */                         .getNameWithGenus() + " in!");
/*      */                     
/* 1653 */                     Items.decay(newItem.getWurmId(), newItem.getDbStrings());
/*      */                   }
/*      */                 
/* 1656 */                 } else if (inventory.insertItem(newItem)) {
/*      */                   
/* 1658 */                   if (item.getTemplateId() == 301) {
/*      */                     
/* 1660 */                     responder.getCommunicator().sendNormalServerMessage("You pull " + newItem
/* 1661 */                         .getNameWithGenus() + " out from the horn.");
/* 1662 */                     Server.getInstance()
/* 1663 */                       .broadCastAction(responder
/* 1664 */                         .getName() + " pulls something out of a huge goat horn full of fruit and flowers.", responder, 5);
/*      */                   
/*      */                   }
/*      */                   else {
/*      */ 
/*      */                     
/* 1670 */                     responder.getCommunicator().sendNormalServerMessage("You wave your wand and create " + newItem
/* 1671 */                         .getNameWithGenus() + ".");
/* 1672 */                     Server.getInstance().broadCastAction(responder
/* 1673 */                         .getName() + " waves a black wand vividly.", responder, 5);
/*      */                   } 
/*      */                   
/* 1676 */                   if (newItem.isEpicTargetItem() && Servers.localServer.testServer) {
/*      */                     
/* 1678 */                     newItem.setAuxData(responder.getKingdomTemplateId());
/* 1679 */                     AdvancedCreationEntry.onEpicItemCreated(responder, newItem, newItem.getTemplateId(), true);
/*      */                   } 
/*      */                 } else {
/*      */ 
/*      */                   
/*      */                   try {
/*      */ 
/*      */                     
/* 1687 */                     newItem.putItemInfrontof(responder);
/* 1688 */                     if (item.getTemplateId() == 301)
/*      */                     {
/* 1690 */                       responder.getCommunicator().sendNormalServerMessage("You pull " + newItem
/* 1691 */                           .getNameWithGenus() + " out from the horn and puts it on the ground.");
/*      */                       
/* 1693 */                       Server.getInstance()
/* 1694 */                         .broadCastAction(responder
/* 1695 */                           .getName() + " pulls something out of a huge goat horn full of fruit and flowers.", responder, 5);
/*      */                     
/*      */                     }
/*      */                     else
/*      */                     {
/*      */                       
/* 1701 */                       responder.getCommunicator().sendNormalServerMessage("You wave your wand and create " + newItem
/* 1702 */                           .getNameWithGenus() + " in front of you.");
/*      */                       
/* 1704 */                       Server.getInstance().broadCastAction(responder
/* 1705 */                           .getName() + " waves a black wand vividly.", responder, 5);
/*      */                     }
/*      */                   
/* 1708 */                   } catch (NoSuchPlayerException nsp) {
/*      */                     
/* 1710 */                     responder.getCommunicator().sendAlertServerMessage("Could not locate your identity! Check the logs!");
/*      */                     
/* 1712 */                     logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */                   }
/* 1714 */                   catch (NoSuchCreatureException nsc) {
/*      */                     
/* 1716 */                     responder.getCommunicator().sendAlertServerMessage("Could not locate your identity! Check the logs!");
/*      */                     
/* 1718 */                     logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */                   }
/* 1720 */                   catch (NoSuchZoneException nsz) {
/*      */                     
/* 1722 */                     responder.getCommunicator().sendAlertServerMessage("You need to be in valid zones, since you cannot carry the item.");
/*      */                   }
/*      */                 
/*      */                 } 
/*      */               } 
/*      */             } else {
/*      */               
/* 1729 */               responder.getCommunicator().sendAlertServerMessage("Don't create these. They will lack important data.");
/*      */             } 
/*      */           } else {
/*      */             
/* 1733 */             logger.log(Level.WARNING, responder.getName() + " tries to create items by hacking the protocol. data1: " + d1 + ", data2: " + d2 + ", number: " + number);
/*      */           } 
/*      */           
/* 1736 */           if (responder.loggerCreature1 > 0L) {
/* 1737 */             responder.getCommunicator().sendNormalServerMessage("That took " + (System.currentTimeMillis() - now) + " ms.");
/*      */           }
/* 1739 */         } catch (NoSuchItemException nsi) {
/*      */           
/* 1741 */           logger.log(Level.WARNING, "Failed to locate item with id=" + target + " for which name change was intended.");
/*      */           
/* 1743 */           responder.getCommunicator().sendNormalServerMessage("Failed to locate that item.");
/*      */         }
/* 1745 */         catch (NumberFormatException nfe) {
/*      */           
/* 1747 */           responder.getCommunicator().sendNormalServerMessage("You realize that " + d1 + ", " + d2 + " or " + number + " doesn't match your requirements.");
/*      */           
/* 1749 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/* 1751 */             logger.log(Level.FINE, responder.getName() + " realises that " + d1 + ", " + d2 + " or " + number + " doesn't match their requirements. " + nfe, nfe);
/*      */           
/*      */           }
/*      */         }
/* 1755 */         catch (FailedException fe) {
/*      */           
/* 1757 */           responder.getCommunicator().sendNormalServerMessage("Failed!: " + fe.getMessage());
/* 1758 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/* 1760 */             logger.log(Level.FINE, "Failed to create an Item " + fe, (Throwable)fe);
/*      */           }
/*      */         }
/* 1763 */         catch (NoSuchTemplateException nst) {
/*      */           
/* 1765 */           responder.getCommunicator().sendNormalServerMessage("Failed!: " + nst.getMessage());
/* 1766 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/* 1768 */             logger.log(Level.FINE, "Failed to create an Item " + nst, (Throwable)nst);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void parseLearnSkillQuestion(LearnSkillQuestion question) {
/* 1777 */     int type = question.getType();
/* 1778 */     Creature responder = question.getResponder();
/* 1779 */     long target = question.getTarget();
/* 1780 */     if (type == 0) {
/*      */       
/* 1782 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 1785 */     if (type == 16) {
/*      */       
/* 1787 */       String d1 = question.getAnswer().getProperty("data1");
/* 1788 */       String value = question.getAnswer().getProperty("val");
/* 1789 */       String dec = question.getAnswer().getProperty("dec");
/* 1790 */       String aff = question.getAnswer().getProperty("aff");
/* 1791 */       String align = question.getAnswer().getProperty("align");
/* 1792 */       String karma = question.getAnswer().getProperty("karma");
/* 1793 */       String strPath = question.getAnswer().getProperty("path");
/* 1794 */       String strLevel = question.getAnswer().getProperty("level");
/*      */       
/* 1796 */       if (WurmPermissions.mayUseDeityWand(responder)) {
/*      */ 
/*      */         
/*      */         try {
/* 1800 */           int data1 = Integer.parseInt(d1);
/* 1801 */           Collection<SkillTemplate> temps = SkillSystem.templates.values();
/* 1802 */           SkillTemplate[] templates = temps.<SkillTemplate>toArray(new SkillTemplate[temps.size()]);
/* 1803 */           Arrays.sort(templates, new Comparator<SkillTemplate>()
/*      */               {
/*      */                 
/*      */                 public int compare(SkillTemplate o1, SkillTemplate o2)
/*      */                 {
/* 1808 */                   return o1.getName().compareTo(o2.getName());
/*      */                 }
/*      */               });
/* 1811 */           int sk = templates[data1].getNumber();
/* 1812 */           double skillval = Double.parseDouble(value + "." + dec);
/* 1813 */           boolean changeSkill = (skillval != 0.0D);
/* 1814 */           boolean changeAff = !aff.equals("-1");
/* 1815 */           boolean changeAlign = (align.length() > 0);
/* 1816 */           boolean changeKarma = (karma.length() > 0);
/* 1817 */           boolean hasCult = (strPath != null);
/* 1818 */           int newAff = Integer.parseInt(aff);
/* 1819 */           int newAlign = changeAlign ? Integer.parseInt(align) : 0;
/* 1820 */           int newKarma = changeKarma ? Integer.parseInt(karma) : 0;
/* 1821 */           skillval = Math.min(100.0D, skillval);
/* 1822 */           skillval = Math.max(1.0D, skillval);
/* 1823 */           Skills skills = null;
/* 1824 */           if (WurmId.getType(target) == 1 || WurmId.getType(target) == 0) {
/*      */             
/* 1826 */             if (sk != 10086) {
/*      */               
/*      */               try {
/* 1829 */                 Creature receiver = Server.getInstance().getCreature(target);
/* 1830 */                 skills = receiver.getSkills();
/*      */                 
/*      */                 try {
/* 1833 */                   Skill skill = skills.getSkill(sk);
/* 1834 */                   if (changeSkill) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1845 */                     skill.setKnowledge(skillval, false);
/*      */                     
/* 1847 */                     responder.getCommunicator().sendNormalServerMessage("You set the " + skill
/* 1848 */                         .getName() + " skill of " + receiver.getName() + " to " + skillval + ".");
/*      */                     
/* 1850 */                     receiver.getCommunicator().sendNormalServerMessage(responder
/* 1851 */                         .getName() + " sets your " + skill.getName() + " skill to " + skillval + ".");
/*      */                     
/* 1853 */                     logger.log(Level.INFO, responder.getName() + " set " + skill.getName() + " skill of " + receiver
/* 1854 */                         .getName() + " to " + skillval + ".");
/*      */                   } 
/* 1856 */                   if (changeAff) {
/*      */                     
/* 1858 */                     int oldAff = skill.affinity;
/* 1859 */                     if (oldAff != newAff) {
/*      */                       
/* 1861 */                       Affinities.setAffinity(receiver.getWurmId(), sk, newAff, false);
/* 1862 */                       if (oldAff < newAff) {
/* 1863 */                         responder.getCommunicator().sendNormalServerMessage("You increased affinities from " + oldAff + " to " + newAff + ".");
/*      */                       } else {
/*      */                         
/* 1866 */                         responder.getCommunicator().sendNormalServerMessage("You decrease affinities from " + oldAff + " to " + newAff + ".");
/*      */                       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 1876 */                       logger.log(Level.INFO, responder
/* 1877 */                           .getName() + " set affinities for " + skill.getName() + " skill of " + receiver
/*      */                           
/* 1879 */                           .getName() + " to " + newAff + ".");
/*      */                     } 
/*      */                     
/* 1882 */                     if (receiver.isNpc() && skill.getNumber() == 1023) {
/*      */                       
/* 1884 */                       receiver.setSkill(10056, (newAff * 10) + Server.rand.nextFloat() * newAff * 10.0F);
/* 1885 */                       receiver.setSkill(10058, (newAff * 10) + Server.rand.nextFloat() * newAff * 10.0F);
/* 1886 */                       receiver.setSkill(10081, (newAff * 10) + Server.rand.nextFloat() * newAff * 10.0F);
/* 1887 */                       receiver.setSkill(10080, (newAff * 10) + Server.rand.nextFloat() * newAff * 10.0F);
/* 1888 */                       receiver.setSkill(10079, (newAff * 10) + Server.rand.nextFloat() * newAff * 10.0F);
/* 1889 */                       receiver.setSkill(1030, (newAff * 10) + Server.rand.nextFloat() * newAff * 10.0F);
/* 1890 */                       receiver.setSkill(1002, (newAff * 10) + Server.rand.nextFloat() * newAff * 10.0F);
/* 1891 */                       receiver.setSkill(103, (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/* 1892 */                       receiver.setSkill(102, (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/* 1893 */                       receiver.setSkill(10054, (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/* 1894 */                       receiver.setSkill(10053, (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/* 1895 */                       receiver.setSkill(10055, (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/* 1896 */                       receiver.setSkill(10052, (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/*      */                       
/* 1898 */                       Item prim = receiver.getPrimWeapon();
/* 1899 */                       if (prim != null)
/*      */                       {
/* 1901 */                         receiver.setSkill(prim.getPrimarySkill(), (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/*      */                       }
/* 1903 */                       Item shield = receiver.getShield();
/* 1904 */                       if (shield != null)
/*      */                       {
/* 1906 */                         receiver.setSkill(shield.getPrimarySkill(), (Math.max(1, newAff) * 10) + Server.rand.nextFloat() * Math.max(10, newAff * 10));
/*      */                       }
/*      */                     } 
/*      */                   } 
/* 1910 */                   if (changeAlign) {
/*      */                     
/* 1912 */                     float oldAlign = receiver.getAlignment();
/* 1913 */                     if (oldAlign != newAlign) {
/*      */                       
/* 1915 */                       receiver.setAlignment(newAlign);
/* 1916 */                       if (oldAlign < newAlign) {
/* 1917 */                         responder.getCommunicator().sendNormalServerMessage("You increased alignment from " + oldAlign + " to " + newAlign + ".");
/*      */                       } else {
/*      */                         
/* 1920 */                         responder.getCommunicator()
/* 1921 */                           .sendNormalServerMessage("You decrease alignment from " + oldAlign + " to " + newAlign + ".");
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                   
/* 1926 */                   if (changeKarma) {
/*      */                     
/* 1928 */                     float oldKarma = receiver.getKarma();
/* 1929 */                     if (oldKarma != newKarma) {
/*      */                       
/* 1931 */                       receiver.setKarma(newKarma);
/* 1932 */                       if (oldKarma < newKarma) {
/* 1933 */                         responder.getCommunicator().sendNormalServerMessage("You increased karma from " + oldKarma + " to " + newKarma + ".");
/*      */                       } else {
/*      */                         
/* 1936 */                         responder.getCommunicator()
/* 1937 */                           .sendNormalServerMessage("You decrease karma from " + oldKarma + " to " + newKarma + ".");
/*      */                       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 1948 */                       logger.log(Level.INFO, responder.getName() + " set karma of " + receiver
/* 1949 */                           .getName() + " from " + oldKarma + " to " + newKarma + ".");
/*      */                     } 
/*      */                   } 
/* 1952 */                   if (hasCult) {
/*      */ 
/*      */                     
/* 1955 */                     Cultist cultist = Cultist.getCultist(target);
/* 1956 */                     byte path = 0;
/* 1957 */                     byte level = 0;
/* 1958 */                     if (cultist != null) {
/*      */                       
/* 1960 */                       path = cultist.getPath();
/* 1961 */                       level = cultist.getLevel();
/*      */                     } 
/* 1963 */                     byte newPath = Byte.parseByte(strPath);
/* 1964 */                     if (path != newPath) {
/*      */                       
/* 1966 */                       if (path != 0) {
/*      */                         
/*      */                         try {
/*      */ 
/*      */                           
/* 1971 */                           cultist.deleteCultist();
/* 1972 */                           responder.getCommunicator().sendNormalServerMessage("You have removed " + receiver
/* 1973 */                               .getName() + " from " + 
/* 1974 */                               Cults.getPathNameFor(path) + ".");
/* 1975 */                           receiver.getCommunicator().sendNormalServerMessage(responder
/* 1976 */                               .getName() + " removed you from " + 
/* 1977 */                               Cults.getPathNameFor(path) + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                           
/* 1987 */                           logger.log(Level.INFO, responder
/* 1988 */                               .getName() + " removed " + receiver.getName() + " from " + 
/* 1989 */                               Cults.getPathNameFor(path) + ".");
/*      */                         }
/* 1991 */                         catch (IOException e) {
/*      */                           
/* 1993 */                           responder.getCommunicator().sendNormalServerMessage("Problem leaving cultist path for " + receiver
/* 1994 */                               .getName() + ".");
/* 1995 */                           logger.log(Level.INFO, responder.getName() + " had problem resetting cultist path for " + receiver
/*      */                               
/* 1997 */                               .getName() + ".");
/*      */                           return;
/*      */                         } 
/*      */                       }
/* 2001 */                       if (newPath != 0) {
/*      */                         
/* 2003 */                         cultist = new Cultist(target, newPath);
/* 2004 */                         responder.getCommunicator().sendNormalServerMessage("You have added " + receiver
/* 2005 */                             .getName() + " to " + 
/* 2006 */                             Cults.getPathNameFor(newPath) + ".");
/* 2007 */                         receiver.getCommunicator().sendNormalServerMessage(responder
/* 2008 */                             .getName() + " added you to " + Cults.getPathNameFor(newPath) + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/* 2019 */                         logger.log(Level.INFO, responder.getName() + " added " + receiver.getName() + " to " + 
/* 2020 */                             Cults.getPathNameFor(newPath) + ".");
/*      */                       } 
/*      */                     } 
/* 2023 */                     if (newPath != 0)
/*      */                     {
/*      */                       
/* 2026 */                       if (strLevel.length() > 0)
/*      */                       {
/* 2028 */                         level = (byte)Math.min(Byte.parseByte(strLevel), 15);
/* 2029 */                         cultist.setLevel(level);
/* 2030 */                         responder.getCommunicator().sendNormalServerMessage("You have changes cult level for " + receiver
/* 2031 */                             .getName() + " to " + 
/* 2032 */                             Cults.getNameForLevel(newPath, level) + ".");
/* 2033 */                         receiver.getCommunicator().sendNormalServerMessage(responder
/* 2034 */                             .getName() + " changed your cult level to " + 
/* 2035 */                             Cults.getNameForLevel(newPath, level) + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/* 2045 */                         logger.log(Level.INFO, responder.getName() + " changed cult level of " + receiver
/* 2046 */                             .getName() + " to " + 
/* 2047 */                             Cults.getNameForLevel(newPath, level) + ".");
/*      */                       }
/*      */                     
/*      */                     }
/*      */                   } 
/* 2052 */                 } catch (NoSuchSkillException nss) {
/*      */                   
/* 2054 */                   skills.learn(sk, (float)skillval);
/* 2055 */                   responder.getCommunicator().sendNormalServerMessage("You teach " + receiver
/* 2056 */                       .getName() + " " + SkillSystem.getNameFor(sk) + " to " + skillval + ".");
/*      */                   
/* 2058 */                   receiver.getCommunicator().sendNormalServerMessage(responder
/* 2059 */                       .getName() + " teaches you the " + SkillSystem.getNameFor(sk) + " skill to " + skillval + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 2069 */                   logger.log(Level.INFO, responder.getName() + " set " + SkillSystem.getNameFor(sk) + " skill of " + receiver
/* 2070 */                       .getName() + " to " + skillval + ".");
/*      */                 }
/* 2072 */                 catch (IOException e) {
/*      */ 
/*      */                   
/* 2075 */                   responder.getCommunicator().sendNormalServerMessage("Problem changing alignment for " + receiver
/* 2076 */                       .getName() + ".");
/*      */                 }
/*      */               
/* 2079 */               } catch (NoSuchCreatureException nsc) {
/*      */                 
/* 2081 */                 responder.getCommunicator().sendNormalServerMessage("Failed to locate creature with id " + target + ".");
/*      */               
/*      */               }
/* 2084 */               catch (NoSuchPlayerException nsp) {
/*      */                 
/* 2086 */                 responder.getCommunicator().sendNormalServerMessage("Failed to locate player with id " + target + ".");
/*      */               } 
/*      */             } else {
/*      */               
/* 2090 */               responder.getCommunicator().sendNormalServerMessage("This skill is impossible to learn like that.");
/*      */             } 
/*      */           } else {
/*      */             
/* 2094 */             skills = responder.getSkills();
/*      */             
/*      */             try {
/* 2097 */               Skill skill = skills.getSkill(sk);
/* 2098 */               if (changeSkill) {
/*      */                 
/* 2100 */                 skill.setKnowledge(skillval, false);
/*      */                 
/* 2102 */                 responder.getCommunicator().sendNormalServerMessage("You set " + 
/* 2103 */                     SkillSystem.getNameFor(sk) + " to " + skillval + ".");
/*      */               } 
/*      */               
/* 2106 */               if (changeAff) {
/*      */                 
/* 2108 */                 int oldAff = skill.affinity;
/* 2109 */                 if (oldAff != newAff) {
/*      */                   
/* 2111 */                   skill.setAffinity(newAff);
/* 2112 */                   if (oldAff < newAff) {
/* 2113 */                     responder.getCommunicator().sendNormalServerMessage("You increased affinities from " + oldAff + " to " + newAff + ".");
/*      */                   } else {
/*      */                     
/* 2116 */                     responder.getCommunicator().sendNormalServerMessage("You descrmented affinities from " + oldAff + " to " + newAff + ".");
/*      */                   }
/*      */                 
/*      */                 } 
/*      */               } 
/* 2121 */             } catch (NoSuchSkillException nss) {
/*      */               
/* 2123 */               skills.learn(sk, (float)skillval);
/* 2124 */               responder.getCommunicator().sendNormalServerMessage("You learn " + 
/* 2125 */                   SkillSystem.getNameFor(sk) + " to " + skillval + ".");
/*      */             } 
/* 2127 */             logger.log(Level.INFO, responder.getName() + " learnt " + SkillSystem.getNameFor(sk) + " to " + skillval + ".");
/*      */             
/* 2129 */             if (changeKarma)
/*      */             {
/* 2131 */               float oldKarma = responder.getKarma();
/* 2132 */               if (oldKarma != newKarma)
/*      */               {
/* 2134 */                 responder.setKarma(newKarma);
/* 2135 */                 if (oldKarma < newKarma) {
/* 2136 */                   responder.getCommunicator().sendNormalServerMessage("You increased your karma from " + oldKarma + " to " + newKarma + ".");
/*      */                 } else {
/*      */                   
/* 2139 */                   responder.getCommunicator()
/* 2140 */                     .sendNormalServerMessage("You decrease your karma from " + oldKarma + " to " + newKarma + ".");
/*      */                 } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2151 */                 logger.log(Level.INFO, responder.getName() + " set karma of " + responder
/* 2152 */                     .getName() + " from " + oldKarma + " to " + newKarma + ".");
/*      */               }
/*      */             
/*      */             }
/*      */           
/*      */           } 
/* 2158 */         } catch (NumberFormatException nfe) {
/*      */           
/* 2160 */           responder.getCommunicator().sendNormalServerMessage("Failed to interpret " + d1 + " or " + value + " as a number.");
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2166 */         logger.log(Level.WARNING, responder
/* 2167 */             .getName() + " tries to learn skills but their power is only " + responder.getPower() + ". data1: " + d1 + ", value: " + value);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void summon(String pname, Creature responder, int tilex, int tiley, byte layer) {
/*      */     try {
/* 2177 */       Player player = Players.getInstance().getPlayer(StringUtilities.raiseFirstLetter(pname));
/*      */       
/* 2179 */       responder.getCommunicator().sendNormalServerMessage("You summon " + player.getName() + ".");
/*      */       
/* 2181 */       Server.getInstance().broadCastAction(responder.getName() + " makes a commanding gesture!", responder, 5);
/* 2182 */       player.getCommunicator().sendNormalServerMessage("You are summoned by a great force.");
/* 2183 */       Server.getInstance().broadCastAction(player.getName() + " suddenly disappears.", (Creature)player, 5);
/* 2184 */       if (responder.getLogger() != null) {
/* 2185 */         responder.getLogger().log(Level.INFO, responder.getName() + " summons " + pname);
/*      */       }
/* 2187 */       player.setTeleportPoints((short)tilex, (short)tiley, layer, responder.getFloorLevel());
/* 2188 */       player.startTeleporting();
/*      */       
/* 2190 */       Server.getInstance().broadCastAction(player.getName() + " suddenly appears.", (Creature)player, 5);
/* 2191 */       player.getCommunicator().sendTeleport(false);
/* 2192 */       player.setBridgeId(responder.getBridgeId());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2205 */       logger.log(Level.INFO, responder
/* 2206 */           .getName() + " summoned player " + player.getName() + ", with ID: " + player.getWurmId() + " at coords " + tilex + ',' + tiley + " teleportlayer " + player
/* 2207 */           .getTeleportLayer());
/*      */     }
/* 2209 */     catch (NoSuchPlayerException nsp) {
/*      */       
/* 2211 */       PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(pname);
/*      */       
/*      */       try {
/* 2214 */         pinf.load();
/*      */       }
/* 2216 */       catch (IOException iox) {
/*      */         
/* 2218 */         responder.getCommunicator().sendNormalServerMessage("Failed to load data for the player with name " + pname + ".");
/*      */         
/*      */         return;
/*      */       } 
/* 2222 */       if (pinf != null && pinf.wurmId > 0L) {
/*      */         
/* 2224 */         CreaturePos cs = CreaturePos.getPosition(pinf.wurmId);
/* 2225 */         cs.setPosX(((tilex << 2) + 2));
/* 2226 */         cs.setPosY(((tiley << 2) + 2));
/* 2227 */         float z = 0.0F;
/*      */         
/*      */         try {
/* 2230 */           z = Zones.calculateHeight(cs.getPosX(), cs.getPosY(), (layer == 0));
/*      */         }
/* 2232 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 2234 */           responder.getCommunicator().sendNormalServerMessage("No such zone: " + tilex + "," + tiley + ", surf=" + ((layer == 0) ? 1 : 0));
/*      */           
/*      */           return;
/*      */         } 
/* 2238 */         cs.setLayer(layer);
/* 2239 */         cs.setPosZ(z, true);
/*      */         
/* 2241 */         cs.setRotation(responder.getStatus().getRotation() - 180.0F);
/*      */         
/*      */         try {
/* 2244 */           int zoneid = Zones.getZoneIdFor(tilex, tiley, (layer == 0));
/* 2245 */           cs.setZoneId(zoneid);
/* 2246 */           cs.setBridgeId(responder.getBridgeId());
/* 2247 */           cs.save(false);
/* 2248 */           responder.getCommunicator().sendNormalServerMessage("Okay, " + pname + " set to " + tilex + "," + tiley + " surfaced=" + ((layer == 0) ? 1 : 0));
/*      */           
/* 2250 */           logger.log(Level.INFO, responder.getName() + " set " + pname + " to " + tilex + "," + tiley + " surfaced=" + ((layer == 0) ? 1 : 0));
/*      */           
/* 2252 */           if (responder.getLogger() != null) {
/* 2253 */             responder.getLogger().log(Level.INFO, "Set " + pname + " to " + tilex + "," + tiley + " surfaced=" + ((layer == 0) ? 1 : 0));
/*      */           }
/*      */         }
/* 2256 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 2258 */           responder.getCommunicator().sendNormalServerMessage("No such zone: " + tilex + "," + tiley + ", surf=" + ((layer == 0) ? 1 : 0));
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } else {
/* 2264 */         responder.getCommunicator().sendNormalServerMessage("No player with the name " + pname + " found.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   static void parseCreatureCreationQuestion(CreatureCreationQuestion question) {
/* 2270 */     int type = question.getType();
/* 2271 */     Creature responder = question.getResponder();
/* 2272 */     long target = question.getTarget();
/*      */     
/* 2274 */     if (type == 0) {
/*      */       
/* 2276 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 2279 */     if (type == 6)
/*      */     {
/* 2281 */       if (WurmId.getType(target) == 2 || WurmId.getType(target) == 19 || 
/* 2282 */         WurmId.getType(target) == 20) {
/*      */         
/* 2284 */         String pname = question.getAnswer().getProperty("pname");
/* 2285 */         if (pname != null && pname.length() > 0) {
/*      */           
/* 2287 */           if (pname.equalsIgnoreCase(responder.getName())) {
/* 2288 */             responder.getCommunicator().sendNormalServerMessage("You cannot summon yourself.");
/*      */           } else {
/*      */             
/* 2291 */             summon(pname, responder, responder.getTileX(), responder.getTileY(), (byte)responder.getLayer());
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 2296 */           String d1 = question.getAnswer().getProperty("data1");
/* 2297 */           String number = question.getAnswer().getProperty("number");
/* 2298 */           String cname = question.getAnswer().getProperty("cname");
/* 2299 */           String gender = question.getAnswer().getProperty("gender");
/* 2300 */           String sage = question.getAnswer().getProperty("age");
/*      */ 
/*      */           
/*      */           try {
/* 2304 */             Item item = Items.getItem(target);
/* 2305 */             if ((item.getTemplateId() == 176 && responder.getPower() >= 3) || (Servers.localServer.testServer && responder
/* 2306 */               .getPower() >= 1))
/*      */             {
/* 2308 */               float posx = ((question.getTileX() << 2) + 2);
/* 2309 */               float posy = ((question.getTileY() << 2) + 2);
/* 2310 */               int layer = question.getLayer();
/* 2311 */               int floorLevel = responder.getFloorLevel();
/* 2312 */               float rot = responder.getStatus().getRotation();
/* 2313 */               rot -= 180.0F;
/* 2314 */               if (rot < 0.0F) {
/* 2315 */                 rot += 360.0F;
/* 2316 */               } else if (rot > 360.0F) {
/* 2317 */                 rot -= 360.0F;
/*      */               } 
/*      */               
/* 2320 */               int tId = Integer.parseInt(d1);
/* 2321 */               int num = Integer.parseInt(number);
/* 2322 */               int dage = Integer.parseInt(sage);
/*      */               
/* 2324 */               CreatureTemplate template = question.getTemplate(tId);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2331 */               if (template.isUnique() && responder.getPower() < 5 && !Servers.localServer.testServer) {
/*      */ 
/*      */                 
/* 2334 */                 responder.getCommunicator().sendNormalServerMessage("You may not summon that creature.");
/*      */                 return;
/*      */               } 
/* 2337 */               if (template.getTemplateId() == 53)
/*      */               {
/* 2339 */                 if (!WurmCalendar.isEaster() && !Servers.localServer.testServer) {
/*      */                   
/* 2341 */                   responder.getCommunicator().sendNormalServerMessage("You may not summon that creature now.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2357 */               long start = System.nanoTime();
/* 2358 */               for (int x = 0; x < num; x++) {
/*      */                 
/* 2360 */                 if (x > 0) {
/*      */                   
/* 2362 */                   posx += -2.0F + Server.rand.nextFloat() * 4.0F;
/* 2363 */                   posy += -2.0F + Server.rand.nextFloat() * 4.0F;
/*      */                 } 
/*      */                 
/*      */                 try {
/*      */                   Creature newCreature;
/*      */                   
/* 2369 */                   byte sex = 0;
/* 2370 */                   if (gender.equals("female") || template.getSex() == 1)
/* 2371 */                     sex = 1; 
/* 2372 */                   byte kingd = 0;
/* 2373 */                   if (template.isHuman()) {
/* 2374 */                     kingd = responder.getKingdomId();
/*      */                   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 2382 */                   long sid = question.getStructureId();
/* 2383 */                   long bridgeId = -10L;
/* 2384 */                   if (sid > 0L) {
/*      */                     
/*      */                     try {
/*      */                       
/* 2388 */                       Structure struct = Structures.getStructure(sid);
/* 2389 */                       if (struct.isTypeBridge()) {
/* 2390 */                         bridgeId = sid;
/*      */                       }
/* 2392 */                     } catch (NoSuchStructureException noSuchStructureException) {}
/*      */                   }
/*      */ 
/*      */ 
/*      */                   
/* 2397 */                   byte ttype = 0;
/* 2398 */                   if (template.hasDen() || template.isRiftCreature() || Servers.localServer.testServer) {
/* 2399 */                     ttype = Byte.parseByte(question.getAnswer().getProperty("tid"));
/*      */                   }
/*      */                   
/* 2402 */                   if (template.getTemplateId() != 69) {
/*      */                     
/* 2404 */                     int age = dage - 1;
/* 2405 */                     if (dage < 2)
/* 2406 */                       age = (int)(Server.rand.nextFloat() * 5.0F); 
/* 2407 */                     if (template.getTemplateId() == 65 || template
/* 2408 */                       .getTemplateId() == 48 || template
/* 2409 */                       .getTemplateId() == 98 || template
/* 2410 */                       .getTemplateId() == 101 || template
/* 2411 */                       .getTemplateId() == 50 || template
/* 2412 */                       .getTemplateId() == 117 || template
/* 2413 */                       .getTemplateId() == 118) {
/* 2414 */                       newCreature = Creature.doNew(template.getTemplateId(), true, posx, posy, rot, layer, cname, sex, kingd, ttype, false, (byte)age, floorLevel);
/*      */                     }
/*      */                     else {
/*      */                       
/* 2418 */                       if (dage < 2)
/* 2419 */                         age = (int)(Server.rand.nextFloat() * Math.min(48, template.getMaxAge())); 
/* 2420 */                       newCreature = Creature.doNew(template.getTemplateId(), true, posx, posy, rot, layer, cname, sex, kingd, ttype, false, (byte)age, floorLevel);
/*      */                     }
/*      */                   
/*      */                   } else {
/*      */                     
/* 2425 */                     newCreature = Creature.doNew(template.getTemplateId(), false, posx, posy, rot, layer, cname, sex, kingd, ttype, true, (byte)0, floorLevel);
/*      */                   } 
/* 2427 */                   if (sid > 0L)
/*      */                   {
/* 2429 */                     if (bridgeId > 0L)
/*      */                     {
/* 2431 */                       newCreature.setBridgeId(bridgeId);
/*      */                     }
/*      */                   }
/* 2434 */                   logger.log(Level.INFO, responder
/*      */                       
/* 2436 */                       .getName() + " created " + gender + " " + template.getName() + ", with ID: " + newCreature
/* 2437 */                       .getWurmId() + ", age: " + 
/* 2438 */                       (newCreature.getStatus()).age + " at coords " + posx + ',' + posy);
/* 2439 */                   responder.getCommunicator().sendNormalServerMessage("You wave your wand, demanding " + newCreature
/* 2440 */                       .getNameWithGenus() + " to appear from the mists of the void.");
/*      */                   
/* 2442 */                   Server.getInstance().broadCastAction(responder
/* 2443 */                       .getName() + " waves a black wand vividly and " + newCreature
/* 2444 */                       .getNameWithGenus() + " quickly appears from nowhere.", responder, 5);
/*      */                   
/* 2446 */                   if (newCreature.isHorse()) {
/*      */                     
/* 2448 */                     newCreature.setVisible(false);
/* 2449 */                     Creature.setRandomColor(newCreature);
/* 2450 */                     newCreature.setVisible(true);
/*      */                   }
/* 2452 */                   else if ((newCreature.getTemplate()).isColoured) {
/*      */                     
/* 2454 */                     newCreature.setVisible(false);
/* 2455 */                     int randCol = Server.rand.nextInt((newCreature.getTemplate()).maxColourCount);
/*      */ 
/*      */                     
/* 2458 */                     if (randCol == 1) {
/* 2459 */                       newCreature.getStatus().setTraitBit(15, true);
/* 2460 */                     } else if (randCol == 2) {
/* 2461 */                       newCreature.getStatus().setTraitBit(16, true);
/* 2462 */                     } else if (randCol == 3) {
/* 2463 */                       newCreature.getStatus().setTraitBit(17, true);
/* 2464 */                     } else if (randCol == 4) {
/* 2465 */                       newCreature.getStatus().setTraitBit(18, true);
/* 2466 */                     } else if (randCol == 5) {
/* 2467 */                       newCreature.getStatus().setTraitBit(24, true);
/* 2468 */                     } else if (randCol == 6) {
/* 2469 */                       newCreature.getStatus().setTraitBit(25, true);
/* 2470 */                     } else if (randCol == 7) {
/* 2471 */                       newCreature.getStatus().setTraitBit(23, true);
/* 2472 */                     } else if (randCol == 8) {
/* 2473 */                       newCreature.getStatus().setTraitBit(30, true);
/* 2474 */                     } else if (randCol == 9) {
/* 2475 */                       newCreature.getStatus().setTraitBit(31, true);
/* 2476 */                     } else if (randCol == 10) {
/* 2477 */                       newCreature.getStatus().setTraitBit(32, true);
/* 2478 */                     } else if (randCol == 11) {
/* 2479 */                       newCreature.getStatus().setTraitBit(33, true);
/* 2480 */                     } else if (randCol == 12) {
/* 2481 */                       newCreature.getStatus().setTraitBit(34, true);
/* 2482 */                     } else if (randCol == 13) {
/* 2483 */                       newCreature.getStatus().setTraitBit(35, true);
/* 2484 */                     } else if (randCol == 14) {
/* 2485 */                       newCreature.getStatus().setTraitBit(36, true);
/* 2486 */                     } else if (randCol == 15) {
/* 2487 */                       newCreature.getStatus().setTraitBit(37, true);
/* 2488 */                     } else if (randCol == 16) {
/* 2489 */                       newCreature.getStatus().setTraitBit(38, true);
/*      */                     } 
/* 2491 */                     newCreature.setVisible(true);
/*      */                   } 
/*      */                   
/* 2494 */                   SoundPlayer.playSound("sound.work.carpentry.carvingknife", newCreature, 1.0F);
/*      */                 
/*      */                 }
/* 2497 */                 catch (Exception ex) {
/*      */                   
/* 2499 */                   logger.log(Level.WARNING, responder.getName() + " tried to create creature but failed: " + ex
/* 2500 */                       .getMessage(), ex);
/*      */                 } 
/*      */               } 
/* 2503 */               float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 2504 */               logger.info(responder.getName() + " created " + num + ' ' + template.getName() + ", which took " + lElapsedTime + " millis.");
/*      */             
/*      */             }
/* 2507 */             else if (item.getTemplateId() == 315 && responder.getPower() >= 1)
/*      */             {
/* 2509 */               responder.getCommunicator().sendNormalServerMessage("Sorry, but you cannot summon creatures at this moment.");
/*      */             
/*      */             }
/*      */             else
/*      */             {
/* 2514 */               responder.getCommunicator().sendNormalServerMessage("Sorry, but you cannot summon creatures at this moment.");
/*      */               
/* 2516 */               logger.log(Level.WARNING, responder.getName() + ", power=" + responder.getPower() + " tried to summon a " + cname + " creature using a " + item);
/*      */             }
/*      */           
/*      */           }
/* 2520 */           catch (NoSuchItemException nsi) {
/*      */             
/* 2522 */             logger.log(Level.INFO, responder.getName() + " tried to use a deitywand itemid to create a creature, but the item did not exist.", (Throwable)nsi);
/*      */           
/*      */           }
/* 2525 */           catch (NumberFormatException nfe) {
/*      */             
/* 2527 */             responder.getCommunicator().sendNormalServerMessage("You realize that " + d1 + ", or " + number + " doesn't match your requirements.");
/*      */             
/* 2529 */             if (logger.isLoggable(Level.FINE))
/*      */             {
/* 2531 */               logger.log(Level.FINE, responder.getName() + " realises that " + d1 + " or " + number + " doesn't match their requirements. " + nfe, nfe);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseVillageExpansionQuestion(VillageExpansionQuestion question) {
/* 2542 */     Creature responder = question.getResponder();
/* 2543 */     long target = question.getTarget();
/*      */ 
/*      */     
/*      */     try {
/* 2547 */       Item deed = Items.getItem(target);
/* 2548 */       Item token = question.getToken();
/* 2549 */       int villid = token.getData2();
/* 2550 */       Village village = Villages.getVillage(villid);
/* 2551 */       int tilex = village.getTokenX();
/* 2552 */       int tiley = village.getTokenY();
/*      */       
/* 2554 */       int oldSize = (village.getEndX() - village.getStartX()) / 2;
/* 2555 */       int size = Villages.getSizeForDeed(deed.getTemplateId());
/* 2556 */       if (oldSize == size) {
/*      */         
/* 2558 */         responder.getCommunicator().sendSafeServerMessage("There is no difference in the sizes of the deeds.");
/*      */         return;
/*      */       } 
/* 2561 */       Structure[] newstructures = Zones.getStructuresInArea(tilex - size, tiley - size, tilex + size, tiley + size, responder
/* 2562 */           .isOnSurface());
/* 2563 */       Set<Structure> notOverlaps = new HashSet<>();
/* 2564 */       Structure[] oldstructures = Zones.getStructuresInArea(village.getStartX(), village.getStartY(), village
/* 2565 */           .getEndX(), village.getEndY(), responder.isOnSurface());
/* 2566 */       for (int o = 0; o < oldstructures.length; o++) {
/*      */         
/* 2568 */         boolean found = false;
/* 2569 */         for (int n = 0; n < newstructures.length; n++) {
/*      */           
/* 2571 */           if (newstructures[n] == oldstructures[o]) {
/*      */             
/* 2573 */             found = true;
/*      */             break;
/*      */           } 
/*      */         } 
/* 2577 */         if (!found) {
/* 2578 */           notOverlaps.add(oldstructures[o]);
/*      */         }
/*      */       } 
/* 2581 */       if (notOverlaps.size() > 0) {
/*      */         
/* 2583 */         Structure[] structures = notOverlaps.<Structure>toArray(new Structure[notOverlaps.size()]);
/* 2584 */         Item[] writs = responder.getKeys();
/* 2585 */         boolean error = false;
/* 2586 */         for (int x = 0; x < structures.length; x++) {
/*      */           
/* 2588 */           long writid = structures[x].getWritId();
/* 2589 */           boolean found = false;
/* 2590 */           for (int k = 0; k < writs.length; k++) {
/*      */             
/* 2592 */             if (writs[k].getWurmId() == writid)
/* 2593 */               found = true; 
/*      */           } 
/* 2595 */           if (!found) {
/*      */             
/* 2597 */             error = true;
/* 2598 */             if (responder.getPower() > 0) {
/*      */               
/* 2600 */               responder.getLogger().log(Level.INFO, responder
/*      */                   
/* 2602 */                   .getName() + " founding village over existing structure " + structures[x]
/* 2603 */                   .getName());
/*      */               
/* 2605 */               responder.getCommunicator().sendSafeServerMessage("Skipping the writ requirement for the structure " + structures[x]
/* 2606 */                   .getName() + " to expand the village. Are you sure you know what you are doing?");
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 2611 */               responder.getCommunicator().sendSafeServerMessage("You need the writ for the structure " + structures[x]
/* 2612 */                   .getName() + " to expand the village.");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 2617 */         if (error) {
/*      */           
/* 2619 */           if (responder.getPower() <= 0) {
/*      */             return;
/*      */           }
/*      */           
/* 2623 */           logger.log(Level.WARNING, responder.getName() + " founding village over existing structures.");
/*      */         } 
/*      */       } 
/*      */       
/* 2627 */       Item altar = Villages.isAltarOnDeed(size, size, size, size, tilex, tiley, responder.isOnSurface());
/* 2628 */       if (altar != null)
/*      */       {
/* 2630 */         if (altar.isEpicTargetItem() && Servers.localServer.PVPSERVER) {
/*      */           
/* 2632 */           if (EpicServerStatus.getRitualMissionForTarget(altar.getWurmId()) != null) {
/*      */             
/* 2634 */             responder.getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since the " + altar
/* 2635 */                 .getName() + " is currently required for an active mission.");
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } else {
/* 2641 */           responder.getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since the " + altar
/* 2642 */               .getName() + " makes it holy ground.");
/*      */           
/*      */           return;
/*      */         } 
/*      */       }
/* 2647 */       Citizen mayor = village.getMayor();
/* 2648 */       if (mayor != null) {
/*      */         
/* 2650 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(mayor.wurmId);
/* 2651 */         long moneyToReimburse = 0L;
/* 2652 */         if (pinf != null) {
/*      */           
/*      */           try {
/*      */             
/* 2656 */             Item olddeed = Items.getItem(village.deedid);
/* 2657 */             moneyToReimburse = (olddeed.getValue() / 2);
/*      */           }
/* 2659 */           catch (NoSuchItemException nsi) {
/*      */             
/* 2661 */             logger.log(Level.WARNING, village.getName() + " No deed id with id=" + village.deedid, (Throwable)nsi);
/*      */           } 
/*      */         }
/* 2664 */         if (moneyToReimburse > 0L) {
/*      */           
/* 2666 */           LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 2667 */           if (!lsw.addMoney(mayor.wurmId, pinf.getName(), moneyToReimburse, "Expand " + village.getName())) {
/*      */             
/* 2669 */             logger.log(Level.INFO, "Expanding did not yield money for " + village
/* 2670 */                 .getName() + " to " + pinf.getName() + ": " + moneyToReimburse + "?");
/*      */             
/* 2672 */             responder.getCommunicator().sendSafeServerMessage("You expand the village here, but the mayor could not be reimbursed.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2677 */       village.setNewBounds(tilex - size, tiley - size, tilex + size, tiley + size);
/*      */       
/* 2679 */       village.plan.updateGuardPlan(village.plan.type, village.plan.moneyLeft + village.plan
/* 2680 */           .calculateMonthlyUpkeepTimeforType(1), village.plan
/* 2681 */           .getNumHiredGuards());
/* 2682 */       String villageName = village.getName();
/*      */       
/* 2684 */       if (responder.getPower() < 5) {
/*      */         
/* 2686 */         Shop shop = Economy.getEconomy().getKingsShop();
/* 2687 */         shop.setMoney(shop.getMoney() - (int)(deed.getValue() * 0.4F));
/*      */       } 
/*      */       
/* 2690 */       int g = 0;
/* 2691 */       if (village.getGuards() != null)
/* 2692 */         g = (village.getGuards()).length; 
/* 2693 */       int maxCitizens = (int)((size * size) / 2.5F);
/* 2694 */       int numOldCitizens = (village.getCitizens()).length - g;
/* 2695 */       int citizToRemove = numOldCitizens - maxCitizens;
/* 2696 */       if (citizToRemove > 0) {
/*      */         
/* 2698 */         Citizen[] citz = village.getCitizens();
/* 2699 */         for (int x = 0; x < citizToRemove; x++) {
/*      */           
/* 2701 */           if ((citz[x].getRole()).id != 2 && (citz[x].getRole()).id != 4)
/* 2702 */             village.removeCitizen(citz[x]); 
/*      */         } 
/*      */       } 
/* 2705 */       responder.getCommunicator().sendNormalServerMessage(villageName + " has been expanded to size " + size + ".");
/* 2706 */       Server.getInstance().broadCastSafe(WurmCalendar.getTime());
/*      */       
/* 2708 */       Server.getInstance().broadCastSafe("The homestead of " + villageName + " has just been expanded by " + responder
/* 2709 */           .getName() + " to the size of " + size + ".");
/*      */       
/* 2711 */       village.addHistory(responder.getName(), "expanded to the size of " + size);
/* 2712 */       HistoryManager.addHistory(responder.getName(), "expanded " + village.getName() + " to the size of " + size);
/* 2713 */       logger.info("The deed of " + villageName + ", id: " + village.deedid + ", has just been expanded by " + responder
/* 2714 */           .getName() + " to the size of " + size + ".");
/*      */     }
/* 2716 */     catch (NoSuchItemException nsi) {
/*      */       
/* 2718 */       logger.log(Level.WARNING, "Failed to locate village deed with id " + target, (Throwable)nsi);
/* 2719 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*      */     
/*      */     }
/* 2722 */     catch (NoSuchVillageException nsv) {
/*      */       
/* 2724 */       logger.log(Level.WARNING, "Failed to locate village with id " + target, (Throwable)nsv);
/* 2725 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
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
/*      */   static void parseGateManageQuestion(GateManagementQuestion question) {
/* 2737 */     Creature responder = question.getResponder();
/* 2738 */     long target = question.getTarget();
/*      */     
/*      */     try {
/* 2741 */       Item deed = Items.getItem(target);
/* 2742 */       Village village = Villages.getVillage(deed.getData2());
/* 2743 */       Set<FenceGate> gates = village.getGates();
/* 2744 */       String key = "";
/* 2745 */       String val = "";
/* 2746 */       if (gates != null)
/*      */       {
/* 2748 */         for (Iterator<FenceGate> it = gates.iterator(); it.hasNext(); ) {
/*      */           
/* 2750 */           FenceGate gate = it.next();
/* 2751 */           long id = gate.getWurmId();
/* 2752 */           key = "gate" + id;
/* 2753 */           val = question.getAnswer().getProperty(key);
/* 2754 */           if (val != null && val.length() > 0) {
/*      */             
/* 2756 */             val = val.replaceAll("\"", "");
/* 2757 */             gate.setName(val);
/*      */           } 
/* 2759 */           key = "open" + id;
/* 2760 */           val = question.getAnswer().getProperty(key);
/* 2761 */           if (val != null && val.length() > 0) {
/*      */             
/*      */             try {
/*      */               
/* 2765 */               int time = Integer.parseInt(val);
/* 2766 */               if (time > 23 || time < 0) {
/* 2767 */                 responder.getCommunicator().sendNormalServerMessage("When setting open time for gate " + gate
/* 2768 */                     .getName() + " the time was " + val + " which is out of the range 0-23.");
/*      */               } else {
/*      */                 
/* 2771 */                 gate.setOpenTime(time);
/*      */               } 
/* 2773 */             } catch (NumberFormatException nfe) {
/*      */               
/* 2775 */               responder.getCommunicator().sendNormalServerMessage("When setting open time for gate " + gate
/* 2776 */                   .getName() + " the time was " + val + " which did not work.");
/*      */             } 
/*      */           }
/*      */           
/* 2780 */           key = "close" + id;
/* 2781 */           val = question.getAnswer().getProperty(key);
/* 2782 */           if (val != null && val.length() > 0) {
/*      */             
/*      */             try {
/*      */               
/* 2786 */               int time = Integer.parseInt(val);
/* 2787 */               if (time > 23 || time < 0) {
/* 2788 */                 responder.getCommunicator().sendNormalServerMessage("When setting close time for gate " + gate
/* 2789 */                     .getName() + " the time was " + val + " which is out of the range 0-23.");
/*      */                 continue;
/*      */               } 
/* 2792 */               gate.setCloseTime(time);
/*      */             }
/* 2794 */             catch (NumberFormatException nfe) {
/*      */               
/* 2796 */               responder.getCommunicator().sendNormalServerMessage("When setting close time for gate " + gate
/* 2797 */                   .getName() + " the time was " + val + " which did not work.");
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/* 2802 */         responder.getCommunicator().sendSafeServerMessage("Settings updated.");
/*      */       }
/*      */     
/* 2805 */     } catch (NoSuchItemException nsi) {
/*      */       
/* 2807 */       logger.log(Level.WARNING, "Failed to locate village deed with id " + target, (Throwable)nsi);
/* 2808 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*      */     
/*      */     }
/* 2811 */     catch (NoSuchVillageException nsv) {
/*      */       
/* 2813 */       logger.log(Level.WARNING, "Failed to locate village for deed with id " + target, (Throwable)nsv);
/* 2814 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseVillageSettingsManageQuestion(VillageSettingsManageQuestion question) {
/* 2825 */     Creature responder = question.getResponder();
/* 2826 */     long target = question.getTarget();
/* 2827 */     Properties props = question.getAnswer();
/*      */     
/*      */     try {
/*      */       Village village;
/*      */       
/* 2832 */       if (target == -10L) {
/*      */         
/* 2834 */         village = responder.getCitizenVillage();
/* 2835 */         if (village == null) {
/* 2836 */           throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*      */         }
/*      */       } else {
/*      */         
/* 2840 */         Item deed = Items.getItem(target);
/* 2841 */         int villageId = deed.getData2();
/* 2842 */         village = Villages.getVillage(villageId);
/*      */       } 
/* 2844 */       boolean highways = false;
/* 2845 */       boolean kos = false;
/* 2846 */       boolean routing = false;
/* 2847 */       if (Features.Feature.HIGHWAYS.isEnabled()) {
/*      */         
/* 2849 */         highways = Boolean.parseBoolean(props.getProperty("highways"));
/* 2850 */         kos = Boolean.parseBoolean(props.getProperty("kos"));
/* 2851 */         routing = Boolean.parseBoolean(props.getProperty("routing"));
/* 2852 */         boolean hasHighway = village.hasHighway();
/* 2853 */         if (!highways && hasHighway) {
/*      */           
/* 2855 */           responder.getCommunicator().sendNormalServerMessage("Cannot disallow highways as you have one on (or next to) your settlement.");
/*      */           
/*      */           return;
/*      */         } 
/* 2859 */         if ((village.getReputations()).length > 0 && highways) {
/*      */           
/* 2861 */           responder.getCommunicator().sendNormalServerMessage("Cannot allow highways if you have an active kos.");
/*      */           
/*      */           return;
/*      */         } 
/* 2865 */         if ((village.getReputations()).length > 0 && !kos) {
/*      */           
/* 2867 */           responder.getCommunicator().sendNormalServerMessage("Cannot disallow kos if you have an active kos. Clear the kos list first.");
/*      */           
/*      */           return;
/*      */         } 
/* 2871 */         if (highways && kos) {
/*      */           
/* 2873 */           responder.getCommunicator().sendNormalServerMessage("Cannot allow kos and allow highways at same time.");
/*      */           
/*      */           return;
/*      */         } 
/* 2877 */         if (routing && !highways) {
/*      */           
/* 2879 */           responder.getCommunicator().sendNormalServerMessage("Cannot opt-in for you village to be found when routing, if you dont allow highways.");
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 2884 */       String villageName = village.getName();
/* 2885 */       boolean changingName = false;
/* 2886 */       if (village.mayChangeName()) {
/*      */         
/* 2888 */         villageName = props.getProperty("vname");
/* 2889 */         villageName = villageName.replaceAll("\"", "");
/* 2890 */         villageName = villageName.trim();
/*      */ 
/*      */         
/* 2893 */         if (villageName.length() > 3) {
/*      */ 
/*      */           
/* 2896 */           villageName = StringUtilities.raiseFirstLetter(villageName);
/* 2897 */           StringTokenizer tokens = new StringTokenizer(villageName);
/* 2898 */           String newName = tokens.nextToken();
/* 2899 */           while (tokens.hasMoreTokens())
/* 2900 */             newName = newName + " " + StringUtilities.raiseFirstLetter(tokens.nextToken()); 
/* 2901 */           villageName = newName;
/*      */         } 
/* 2903 */         if (villageName.length() >= 41) {
/*      */           
/* 2905 */           villageName = villageName.substring(0, 39);
/* 2906 */           responder.getCommunicator().sendNormalServerMessage("The name of the settlement would be ''" + villageName + "''. Please select a shorter name.");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2911 */         if (villageName.length() < 3) {
/*      */           
/* 2913 */           responder.getCommunicator().sendNormalServerMessage("The name of the settlement would be ''" + villageName + "''. Please select a name with at least 3 letters.");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2918 */         if (containsIllegalVillageCharacters(villageName)) {
/*      */           
/* 2920 */           responder.getCommunicator().sendNormalServerMessage("The name ''" + villageName + "'' contains illegal characters. Please select another name.");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2925 */         if (villageName.equals("Wurm")) {
/*      */           
/* 2927 */           responder.getCommunicator().sendNormalServerMessage("The name ''" + villageName + "'' is illegal. Please select another name.");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2932 */         if (!Villages.isNameOk(villageName, village.id)) {
/*      */           
/* 2934 */           responder.getCommunicator().sendNormalServerMessage("The name ''" + villageName + "'' is already taken. Please select another name.");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2939 */         if (!villageName.equals(village.getName())) {
/*      */ 
/*      */           
/* 2942 */           if (!village.mayChangeName()) {
/*      */             
/* 2944 */             responder.getCommunicator().sendNormalServerMessage("You try to change the settlement name as its just been changed. The action was aborted.");
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/* 2950 */           long moneyNeeded = 50000L;
/* 2951 */           long rest = VillageFoundationQuestion.getExpandMoneyNeededFromBank(moneyNeeded, village);
/* 2952 */           if (rest > 0L) {
/*      */ 
/*      */             
/* 2955 */             if (Servers.localServer.testServer) {
/* 2956 */               responder.getCommunicator().sendNormalServerMessage("We need " + moneyNeeded + ". " + rest + " must be taken from the bank.");
/*      */             }
/* 2958 */             if (!((Player)responder).chargeMoney(rest)) {
/*      */               
/* 2960 */               responder.getCommunicator().sendNormalServerMessage("You try to change the settlement size, but your bank account could not be charged. The action was aborted.");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/*      */ 
/*      */             
/* 2967 */             if (Servers.localServer.testServer)
/* 2968 */               responder.getCommunicator().sendNormalServerMessage("We also take " + village
/* 2969 */                   .getAvailablePlanMoney() + " from upkeep."); 
/* 2970 */             village.plan.updateGuardPlan(village.plan.moneyLeft - village.getAvailablePlanMoney());
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2975 */             if (Servers.localServer.testServer) {
/* 2976 */               responder.getCommunicator().sendNormalServerMessage("We charge " + moneyNeeded + " from the plan which has " + village.plan.moneyLeft);
/*      */             }
/* 2978 */             village.plan.updateGuardPlan(village.plan.moneyLeft - moneyNeeded);
/*      */           } 
/* 2980 */           Item deed = null;
/*      */ 
/*      */           
/*      */           try {
/* 2984 */             deed = Items.getItem(village.getDeedId());
/*      */           }
/* 2986 */           catch (NoSuchItemException nsi) {
/*      */             
/* 2988 */             logger.log(Level.WARNING, "Failed to locate settlement deed with id " + village.getDeedId(), (Throwable)nsi);
/* 2989 */             responder.getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*      */           } 
/*      */           
/* 2992 */           village.setName(villageName);
/* 2993 */           village.setFaithCreate(0.0F);
/* 2994 */           village.setFaithHeal(0.0F);
/* 2995 */           village.setFaithWar(0.0F);
/* 2996 */           village.setLastChangedName(System.currentTimeMillis());
/* 2997 */           deed.setDescription(villageName);
/* 2998 */           responder.getCommunicator().sendNormalServerMessage("Changed settlement name to \"" + villageName + "\".");
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 3003 */       String key = "motto";
/* 3004 */       String val = props.getProperty(key);
/* 3005 */       if (val != null)
/*      */       {
/* 3007 */         if (!containsIllegalCharacters(val)) {
/*      */ 
/*      */           
/*      */           try {
/* 3011 */             village.setMotto(val.replaceAll("\"", ""));
/*      */           }
/* 3013 */           catch (IOException iox) {
/*      */             
/* 3015 */             responder.getCommunicator().sendNormalServerMessage("Failed to update the motto.");
/* 3016 */             logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */           } 
/*      */         } else {
/*      */           
/* 3020 */           responder.getCommunicator().sendNormalServerMessage("The motto contains some illegal characters.");
/*      */         }  } 
/* 3022 */       key = "motd";
/* 3023 */       val = props.getProperty(key);
/* 3024 */       if (val != null) {
/*      */         
/*      */         try {
/*      */           
/* 3028 */           String oldMotd = village.getMotd();
/* 3029 */           village.setMotd(val.replaceAll("\"", ""));
/*      */           
/* 3031 */           if (oldMotd != village.getMotd()) {
/* 3032 */             village.addHistory(responder.getName(), "changed Motd");
/*      */           }
/* 3034 */         } catch (IOException iox) {
/*      */           
/* 3036 */           responder.getCommunicator().sendNormalServerMessage("Failed to update the motd.");
/* 3037 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         } 
/*      */       }
/* 3040 */       boolean setdemocracy = false;
/* 3041 */       key = "democracy";
/* 3042 */       val = props.getProperty(key);
/* 3043 */       if (val != null)
/* 3044 */         setdemocracy = val.equals("true"); 
/* 3045 */       if (setdemocracy) {
/*      */         
/*      */         try {
/*      */           
/* 3049 */           village.setDemocracy(true);
/*      */         }
/* 3051 */         catch (IOException iox) {
/*      */           
/* 3053 */           logger.log(Level.WARNING, "Failed to set " + village.getName() + " to democracy. " + iox.getMessage(), iox);
/*      */         } 
/*      */       }
/*      */       
/* 3057 */       key = "nondemocracy";
/* 3058 */       val = props.getProperty(key);
/* 3059 */       if (val != null) {
/* 3060 */         setdemocracy = val.equals("true");
/*      */       } else {
/* 3062 */         setdemocracy = false;
/* 3063 */       }  if (setdemocracy) {
/*      */         
/*      */         try {
/*      */           
/* 3067 */           village.setDemocracy(false);
/*      */         }
/* 3069 */         catch (IOException iox) {
/*      */           
/* 3071 */           logger.log(Level.WARNING, "Failed to set " + village.getName() + " to dictatorship. " + iox.getMessage(), iox);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3094 */       boolean unlimitedCitizens = false;
/* 3095 */       key = "unlimitC";
/* 3096 */       val = props.getProperty(key);
/* 3097 */       if (val != null) {
/* 3098 */         unlimitedCitizens = val.equals("true");
/*      */       } else {
/* 3100 */         unlimitedCitizens = false;
/*      */       } 
/*      */       
/*      */       try {
/* 3104 */         village.setUnlimitedCitizens(unlimitedCitizens);
/*      */       }
/* 3106 */       catch (IOException iox) {
/*      */         
/* 3108 */         logger.log(Level.WARNING, "Failed to set " + village.getName() + " to unlimitedC - " + unlimitedCitizens + ":" + iox
/* 3109 */             .getMessage(), iox);
/*      */       } 
/* 3111 */       int oldSettings = village.getSettings();
/* 3112 */       if (routing != village.isHighwayFound())
/* 3113 */         responder.getCommunicator().sendNormalServerMessage((routing ? "Enabled" : "Disabled") + " finding village through routing."); 
/* 3114 */       if (highways != village.isHighwayAllowed())
/* 3115 */         responder.getCommunicator().sendNormalServerMessage((highways ? "Enabled" : "Disabled") + " highways through village."); 
/* 3116 */       if (kos != village.isKosAllowed()) {
/* 3117 */         responder.getCommunicator().sendNormalServerMessage((kos ? "Enabled" : "Disabled") + " KOS.");
/*      */       }
/*      */       try {
/* 3120 */         village.setIsHighwayFound(routing);
/* 3121 */         village.setIsKosAllowed(kos);
/* 3122 */         village.setIsHighwayAllowed(highways);
/* 3123 */         if (village.getSettings() != oldSettings) {
/* 3124 */           village.saveSettings();
/*      */         }
/* 3126 */       } catch (IOException iox) {
/*      */         
/* 3128 */         logger.log(Level.WARNING, "Failed to save " + village.getName() + " settings:" + iox
/* 3129 */             .getMessage(), iox);
/*      */       } 
/*      */       
/* 3132 */       boolean setKingdomSpawn = false;
/* 3133 */       key = "spawns";
/* 3134 */       val = props.getProperty(key);
/* 3135 */       if (val != null) {
/* 3136 */         setKingdomSpawn = val.equals("true");
/*      */       } else {
/* 3138 */         setKingdomSpawn = false;
/*      */       } 
/* 3140 */       if (setKingdomSpawn) {
/* 3141 */         village.setSpawnSituation((byte)1);
/*      */       } else {
/* 3143 */         village.setSpawnSituation((byte)0);
/*      */       } 
/* 3145 */       boolean setAllowAggros = false;
/* 3146 */       key = "aggros";
/* 3147 */       val = props.getProperty(key);
/* 3148 */       if (val != null) {
/* 3149 */         setAllowAggros = val.equals("true");
/*      */       } else {
/* 3151 */         setAllowAggros = false;
/*      */       } 
/*      */       
/*      */       try {
/* 3155 */         village.setAllowsAggroCreatures(setAllowAggros);
/*      */       }
/* 3157 */       catch (IOException iox) {
/*      */         
/* 3159 */         logger.log(Level.WARNING, "Failed to set " + village.getName() + " to setAllowsAggros - " + setAllowAggros + ":" + iox
/* 3160 */             .getMessage(), iox);
/*      */       } 
/*      */       
/* 3163 */       responder.getCommunicator().sendSafeServerMessage("Settings updated.");
/*      */     }
/* 3165 */     catch (NoSuchItemException nsi) {
/*      */       
/* 3167 */       logger.log(Level.WARNING, "Failed to locate settlement deed with id " + target, (Throwable)nsi);
/* 3168 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*      */     
/*      */     }
/* 3171 */     catch (NoSuchVillageException nsv) {
/*      */       
/* 3173 */       logger.log(Level.WARNING, "Failed to locate settlement for deed with id " + target, (Throwable)nsv);
/* 3174 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the settlement for that request. Please contact administration.");
/*      */     
/*      */     }
/* 3177 */     catch (IOException ioe) {
/*      */ 
/*      */       
/* 3180 */       responder.getCommunicator().sendNormalServerMessage("Failed to change name of settlement. Please contact administration.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseVillageWarQuestion(DeclareWarQuestion question) {
/* 3191 */     Creature responder = question.getResponder();
/*      */     
/* 3193 */     boolean declare = question.getAnswer().getProperty("declare").equals("true");
/* 3194 */     if (responder.getCitizenVillage() != null) {
/*      */       
/* 3196 */       if (!declare) {
/*      */         
/* 3198 */         responder.getCommunicator().sendNormalServerMessage("You decide not to declare war.");
/*      */       }
/*      */       else {
/*      */         
/* 3202 */         Villages.declareWar(responder.getCitizenVillage(), question.getTargetVillage());
/*      */       } 
/*      */     } else {
/*      */       
/* 3206 */       responder.getCommunicator().sendNormalServerMessage("You are not citizen of a village.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseVillagePeaceQuestion(PeaceQuestion question) {
/* 3216 */     Creature asker = question.getResponder();
/* 3217 */     Creature responder = question.getInvited();
/*      */     
/* 3219 */     boolean peace = question.getAnswer().getProperty("peace").equals("true");
/* 3220 */     Village village = asker.getCitizenVillage();
/* 3221 */     if (!peace) {
/*      */       
/* 3223 */       responder.getCommunicator().sendNormalServerMessage("You decline the peace offer.");
/* 3224 */       asker.getCommunicator().sendNormalServerMessage(responder.getName() + " declines your generous peace offer!");
/*      */     }
/*      */     else {
/*      */       
/* 3228 */       Villages.declarePeace(asker, responder, village, responder.getCitizenVillage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseVillageJoinQuestion(VillageJoinQuestion question) {
/* 3239 */     Creature asker = question.getResponder();
/* 3240 */     Creature responder = question.getInvited();
/* 3241 */     boolean join = question.getAnswer().getProperty("join").equals("true");
/* 3242 */     Village village = asker.getCitizenVillage();
/* 3243 */     if (!join) {
/*      */       
/* 3245 */       responder.getCommunicator().sendNormalServerMessage("You decline to join the settlement.");
/* 3246 */       asker.getCommunicator().sendNormalServerMessage(responder.getName() + " declines to join the settlement.");
/*      */ 
/*      */     
/*      */     }
/* 3250 */     else if (responder.isPlayer() && responder.mayChangeVillageInMillis() > 0L) {
/*      */       
/* 3252 */       responder.getCommunicator().sendNormalServerMessage("You may not change settlement in " + 
/* 3253 */           Server.getTimeFor(responder.mayChangeVillageInMillis()) + ".");
/* 3254 */       asker.getCommunicator().sendNormalServerMessage(responder
/* 3255 */           .getName() + " may join your settlement in " + 
/* 3256 */           Server.getTimeFor(responder.mayChangeVillageInMillis()) + ".");
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 3262 */         village.addCitizen(responder, village.getRoleForStatus((byte)3));
/* 3263 */         if (((Player)responder).canUseFreeVillageTeleport())
/*      */         {
/* 3265 */           VillageTeleportQuestion vtq = new VillageTeleportQuestion(responder);
/* 3266 */           vtq.sendQuestion();
/*      */         }
/*      */       
/* 3269 */       } catch (IOException iox) {
/*      */         
/* 3271 */         logger.log(Level.INFO, "Failed to add " + responder.getName() + " to settlement " + village.getName() + "." + iox
/* 3272 */             .getMessage(), iox);
/* 3273 */         responder.getCommunicator().sendNormalServerMessage("Failed to add you to the settlement. Please contact administration.");
/*      */         
/* 3275 */         asker.getCommunicator().sendNormalServerMessage("Failed to add " + responder
/* 3276 */             .getName() + " to the settlement. Please contact administration.");
/*      */       }
/* 3278 */       catch (NoSuchRoleException nsr) {
/*      */         
/* 3280 */         logger.log(Level.INFO, "Failed to add " + responder.getName() + " to settlement " + village.getName() + "." + nsr
/* 3281 */             .getMessage(), (Throwable)nsr);
/* 3282 */         responder.getCommunicator().sendNormalServerMessage("Failed to add you to the settlement. Please contact administration.");
/*      */         
/* 3284 */         asker.getCommunicator().sendNormalServerMessage("Failed to add " + responder
/* 3285 */             .getName() + " to the settlement. Please contact administration.");
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
/*      */   static void parseVillageCitizenManageQuestion(VillageCitizenManageQuestion question) {
/* 3297 */     Creature responder = question.getResponder();
/* 3298 */     long target = question.getTarget();
/* 3299 */     Properties props = question.getAnswer();
/*      */     
/*      */     try {
/*      */       Village village;
/*      */       
/* 3304 */       if (target == -10L) {
/*      */         
/* 3306 */         village = responder.getCitizenVillage();
/* 3307 */         if (village == null) {
/* 3308 */           throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*      */         }
/*      */       } else {
/*      */         
/* 3312 */         Item deed = Items.getItem(target);
/* 3313 */         int villageId = deed.getData2();
/* 3314 */         village = Villages.getVillage(villageId);
/*      */       } 
/* 3316 */       boolean unlimitedCitizens = false;
/* 3317 */       String key = "unlimitC";
/* 3318 */       String valu = question.getAnswer().getProperty("unlimitC");
/* 3319 */       if (valu != null) {
/* 3320 */         unlimitedCitizens = valu.equals("true");
/*      */       } else {
/* 3322 */         unlimitedCitizens = false;
/*      */       } 
/*      */       
/*      */       try {
/* 3326 */         village.setUnlimitedCitizens(unlimitedCitizens);
/*      */       }
/* 3328 */       catch (IOException iox) {
/*      */         
/* 3330 */         logger.log(Level.WARNING, "Failed to set " + village.getName() + " to unlimitedC - " + unlimitedCitizens + ":" + iox
/* 3331 */             .getMessage(), iox);
/*      */       } 
/*      */       
/* 3334 */       if (question.isSelecting()) {
/*      */         
/* 3336 */         String startLetter = props.getProperty("selectRange");
/* 3337 */         VillageCitizenManageQuestion vc = new VillageCitizenManageQuestion(responder, "Citizen management", "Set statuses of citizens.", target);
/*      */         
/* 3339 */         vc.setSelecting(false);
/* 3340 */         if (startLetter != null)
/*      */         {
/* 3342 */           if (startLetter.equals("0")) {
/* 3343 */             vc.setAllowedLetters("abcdef");
/* 3344 */           } else if (startLetter.equals("1")) {
/* 3345 */             vc.setAllowedLetters("ghijkl");
/* 3346 */           } else if (startLetter.equals("2")) {
/* 3347 */             vc.setAllowedLetters("mnopqr");
/* 3348 */           } else if (startLetter.equals("3")) {
/* 3349 */             vc.setAllowedLetters("stuvwxyz");
/*      */           }  } 
/* 3351 */         vc.sendQuestion();
/*      */         return;
/*      */       } 
/* 3354 */       VillageRole[] roles = village.getRoles();
/*      */       
/* 3356 */       for (Iterator<Integer> it = question.getIdMap().keySet().iterator(); it.hasNext(); )
/*      */       {
/* 3358 */         Integer i = it.next();
/* 3359 */         int x = i.intValue();
/* 3360 */         long citid = ((Long)question.getIdMap().get(i)).longValue();
/* 3361 */         Citizen citiz = village.getCitizen(citid);
/* 3362 */         if (citiz != null) {
/*      */           try
/*      */           {
/*      */             
/* 3366 */             String revString = (String)props.get(x + "revoke");
/* 3367 */             boolean revoked = false;
/* 3368 */             if (revString != null)
/* 3369 */               revoked = revString.equals("true"); 
/* 3370 */             if (!revoked) {
/*      */               
/* 3372 */               String val = (String)props.get(String.valueOf(x));
/* 3373 */               if (val != null) {
/*      */                 
/* 3375 */                 int roleid = Integer.parseInt(val);
/* 3376 */                 VillageRole newRole = null;
/* 3377 */                 int count = 0;
/* 3378 */                 for (int r = 0; r < roles.length; r++) {
/*      */                   
/* 3380 */                   if (roles[r].getStatus() != 4 && roles[r].getStatus() != 5 && roles[r]
/* 3381 */                     .getStatus() != 1 && roles[r]
/* 3382 */                     .getStatus() != 6) {
/*      */                     
/* 3384 */                     if (roleid == count) {
/*      */                       
/* 3386 */                       newRole = roles[r];
/*      */                       break;
/*      */                     } 
/* 3389 */                     count++;
/*      */                   } 
/*      */                 } 
/* 3392 */                 if (newRole == null) {
/*      */                   
/* 3394 */                   responder.getCommunicator().sendNormalServerMessage("Failed to locate role for " + citiz
/* 3395 */                       .getName() + ".");
/*      */                   
/*      */                   continue;
/*      */                 } 
/* 3399 */                 VillageRole role = citiz.getRole();
/*      */                 
/* 3401 */                 if (role.getStatus() == 2 && newRole.getStatus() != 2) {
/* 3402 */                   responder
/* 3403 */                     .getCommunicator()
/* 3404 */                     .sendNormalServerMessage(citiz
/* 3405 */                       .getName() + " is the mayor as long as he/she possesses the village deed. You cannot change that manually."); continue;
/*      */                 } 
/* 3407 */                 if (newRole.getStatus() != 2) {
/*      */                   
/* 3409 */                   if (!role.equals(newRole)) {
/*      */ 
/*      */                     
/* 3412 */                     village.updateGatesForRole(newRole);
/* 3413 */                     citiz.setRole(newRole);
/* 3414 */                     village.updateGatesForRole(newRole);
/* 3415 */                     responder.getCommunicator().sendNormalServerMessage(citiz
/* 3416 */                         .getName() + " role changed to \"" + newRole.getName() + "\".");
/*      */                   }  continue;
/*      */                 } 
/* 3419 */                 if (!role.equals(newRole)) {
/* 3420 */                   responder.getCommunicator().sendNormalServerMessage("Did not set " + citiz
/* 3421 */                       .getName() + " to Mayor. The possessor of the village deed is Mayor.");
/*      */                 }
/*      */               } 
/*      */               
/*      */               continue;
/*      */             } 
/*      */             
/* 3428 */             if (!village.isDemocracy())
/*      */             {
/* 3430 */               if (citiz.getRole().getStatus() == 2) {
/* 3431 */                 responder
/* 3432 */                   .getCommunicator()
/* 3433 */                   .sendNormalServerMessage("You cannot revoke the citizenship of the mayor this way. He/she has to give away the village deed."); continue;
/*      */               } 
/* 3435 */               if (citiz.getId() == responder.getWurmId()) {
/* 3436 */                 responder.getCommunicator().sendNormalServerMessage("You must revoke your own citizenship by typing '/revoke " + village
/* 3437 */                     .getName() + "' on the command line.");
/*      */                 continue;
/*      */               } 
/* 3440 */               village.removeCitizen(citiz);
/*      */             }
/*      */           
/*      */           }
/* 3444 */           catch (NumberFormatException nfe)
/*      */           {
/* 3446 */             logger.log(Level.WARNING, "This is bad: " + nfe.getMessage(), nfe);
/*      */           }
/* 3448 */           catch (IOException iox)
/*      */           {
/* 3450 */             logger.log(Level.WARNING, "This is bad: " + iox.getMessage(), iox);
/* 3451 */             responder.getCommunicator().sendNormalServerMessage("Failed to set role for one or more citizens. Please contact administration.");
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 3463 */     catch (NoSuchItemException nsi) {
/*      */       
/* 3465 */       logger.log(Level.WARNING, "Failed to locate village deed with id " + target, (Throwable)nsi);
/* 3466 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*      */     
/*      */     }
/* 3469 */     catch (NoSuchVillageException nsv) {
/*      */       
/* 3471 */       logger.log(Level.WARNING, "Failed to locate village for deed with id " + target, (Throwable)nsv);
/* 3472 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
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
/*      */   static final boolean charge(Creature responder, long coinsNeeded, String reason, float taxrate) throws FailedException {
/* 3486 */     Item[] items = responder.getInventory().getAllItems(false);
/* 3487 */     LinkedList<Item> coins = new LinkedList<>();
/* 3488 */     long value = 0L;
/* 3489 */     for (int x = 0; x < items.length; x++) {
/*      */       
/* 3491 */       if (items[x].isCoin()) {
/*      */         
/* 3493 */         coins.add(items[x]);
/* 3494 */         value += Economy.getValueFor(items[x].getTemplateId());
/*      */       } 
/*      */     } 
/* 3497 */     if (value < coinsNeeded) {
/*      */       
/* 3499 */       Change change = new Change(coinsNeeded);
/* 3500 */       throw new FailedException("You need " + change.getChangeString() + " coins.");
/*      */     } 
/*      */ 
/*      */     
/* 3504 */     long curv = 0L;
/* 3505 */     for (ListIterator<Item> it = coins.listIterator(); it.hasNext(); ) {
/*      */       
/* 3507 */       Item coin = it.next();
/* 3508 */       curv += Economy.getValueFor(coin.getTemplateId());
/*      */       
/*      */       try {
/* 3511 */         Item parent = coin.getParent();
/* 3512 */         parent.dropItem(coin.getWurmId(), false);
/* 3513 */         Economy.getEconomy().returnCoin(coin, reason);
/*      */       }
/* 3515 */       catch (NoSuchItemException nsi) {
/*      */         
/* 3517 */         logger.log(Level.WARNING, responder
/* 3518 */             .getName() + ":  Failed to locate the container for coin " + coin.getName() + ". Value returned is " + (new Change(curv))
/* 3519 */             .getChangeString() + " coins.", (Throwable)nsi);
/* 3520 */         Item[] newCoins = Economy.getEconomy().getCoinsFor(Economy.getValueFor(coin.getTemplateId()));
/* 3521 */         Item inventory = responder.getInventory();
/* 3522 */         for (int i = 0; i < newCoins.length; i++)
/* 3523 */           inventory.insertItem(newCoins[i]); 
/* 3524 */         throw new FailedException("Failed to locate the container for coin " + coin.getName() + ". This is serious and should be reported. Returned " + (new Change(curv))
/* 3525 */             .getChangeString() + " coins.", nsi);
/*      */       } 
/*      */       
/* 3528 */       if (curv >= coinsNeeded)
/*      */         break; 
/*      */     } 
/* 3531 */     if (curv > coinsNeeded) {
/*      */       
/* 3533 */       Item[] newCoins = Economy.getEconomy().getCoinsFor(curv - coinsNeeded);
/* 3534 */       Item inventory = responder.getInventory();
/* 3535 */       for (int i = 0; i < newCoins.length; i++)
/* 3536 */         inventory.insertItem(newCoins[i]); 
/*      */     } 
/* 3538 */     Shop kingsMoney = Economy.getEconomy().getKingsShop();
/* 3539 */     if (taxrate > 1.0F) {
/*      */       
/* 3541 */       logger.log(Level.WARNING, responder.getName() + ":  Taxrate should be max 1 but is " + taxrate, new Exception());
/* 3542 */       taxrate = 1.0F;
/*      */     } 
/* 3544 */     kingsMoney.setMoney(kingsMoney.getMoney() + (long)((float)coinsNeeded * (1.0F - taxrate)));
/* 3545 */     logger.log(Level.INFO, "King now has " + kingsMoney.getMoney());
/*      */     
/* 3547 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseGuardRentalQuestion(GuardManagementQuestion question) {
/* 3556 */     Properties props = question.getAnswer();
/* 3557 */     Creature responder = question.getResponder();
/*      */     
/* 3559 */     String key = "12345678910";
/* 3560 */     String val = null;
/* 3561 */     Village village = responder.citizenVillage;
/* 3562 */     long money = responder.getMoney();
/*      */     
/* 3564 */     if (money > 0L) {
/*      */       
/* 3566 */       long valueWithdrawn = getValueWithdrawn(question);
/* 3567 */       if (valueWithdrawn > 0L) {
/*      */ 
/*      */         
/*      */         try {
/* 3571 */           if (village.plan != null) {
/*      */             
/* 3573 */             if (responder.chargeMoney(valueWithdrawn)) {
/*      */               
/* 3575 */               village.plan.addMoney(valueWithdrawn);
/* 3576 */               village.plan.addPayment(responder.getName(), responder.getWurmId(), valueWithdrawn);
/* 3577 */               Change newch = Economy.getEconomy().getChangeFor(valueWithdrawn);
/* 3578 */               responder.getCommunicator().sendNormalServerMessage("You pay " + newch
/* 3579 */                   .getChangeString() + " to the upkeep fund of " + village.getName() + ".");
/* 3580 */               logger.log(Level.INFO, responder.getName() + " added " + valueWithdrawn + " irons to " + village.getName() + " upkeep.");
/*      */             }
/*      */             else {
/*      */               
/* 3584 */               responder.getCommunicator().sendNormalServerMessage("You don't have that much money.");
/*      */             } 
/*      */           } else {
/*      */             
/* 3588 */             responder.getCommunicator().sendNormalServerMessage("This village does not have an upkeep plan.");
/*      */           } 
/* 3590 */         } catch (IOException iox) {
/*      */           
/* 3592 */           logger.log(Level.WARNING, "Failed to withdraw money from " + responder.getName() + ":" + iox.getMessage(), iox);
/*      */           
/* 3594 */           responder.getCommunicator().sendNormalServerMessage("The transaction failed. Please contact the game masters using the <i>/dev</i> command.");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 3599 */         responder.getCommunicator().sendNormalServerMessage("No money withdrawn.");
/*      */       } 
/* 3601 */     }  if (responder.mayManageGuards()) {
/*      */       
/* 3603 */       GuardPlan plan = (responder.getCitizenVillage()).plan;
/* 3604 */       if (plan != null) {
/*      */         
/* 3606 */         boolean changed = false;
/* 3607 */         key = "hired";
/* 3608 */         val = (String)props.get(key);
/* 3609 */         int nums = plan.getNumHiredGuards();
/* 3610 */         int oldnums = nums;
/* 3611 */         if (val != null) {
/*      */ 
/*      */           
/*      */           try {
/* 3615 */             nums = Integer.parseInt(val);
/*      */           }
/* 3617 */           catch (NumberFormatException nfe) {
/*      */             
/* 3619 */             responder.getCommunicator().sendNormalServerMessage("Failed to parse the value " + val + ". Please enter a number if you wish to change the number of guards.");
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/* 3624 */           if (nums != plan.getNumHiredGuards()) {
/*      */             
/* 3626 */             boolean aboveMax = (nums > GuardPlan.getMaxGuards(responder.getCitizenVillage()));
/* 3627 */             nums = Math.min(nums, GuardPlan.getMaxGuards(responder.getCitizenVillage()));
/* 3628 */             int diff = nums - plan.getNumHiredGuards();
/* 3629 */             if (diff > 0) {
/*      */ 
/*      */               
/* 3632 */               long moneyOver = plan.moneyLeft - plan.calculateMonthlyUpkeepTimeforType(0);
/* 3633 */               if (moneyOver > (10000 * diff)) {
/*      */                 
/* 3635 */                 changed = true;
/* 3636 */                 plan.changePlan(0, nums);
/* 3637 */                 plan.updateGuardPlan(0, plan.moneyLeft - (10000 * diff), nums);
/*      */               } else {
/*      */                 
/* 3640 */                 responder
/* 3641 */                   .getCommunicator()
/* 3642 */                   .sendNormalServerMessage("There was not enough upkeep to increase the number of guards. Please make sure that there is at least one month of upkeep left after you hire the guards.");
/*      */               }
/*      */             
/* 3645 */             } else if (diff < 0) {
/*      */               
/* 3647 */               changed = true;
/* 3648 */               plan.changePlan(0, nums);
/*      */             } 
/* 3650 */             if (aboveMax) {
/* 3651 */               responder.getCommunicator().sendNormalServerMessage("You tried to increase the amount of guards above the max of " + 
/*      */                   
/* 3653 */                   GuardPlan.getMaxGuards(responder.getCitizenVillage()) + " which was denied.");
/*      */             }
/*      */           } 
/*      */         } else {
/*      */           
/* 3658 */           responder.getCommunicator().sendNormalServerMessage("Failed to parse the value " + val + ". Please enter a number if you wish to change the number of guards.");
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 3664 */         if (changed && oldnums < nums) {
/* 3665 */           responder.getCommunicator().sendNormalServerMessage("You change the upkeep plan. New guards will arrive soon.");
/*      */         }
/* 3667 */         else if (changed) {
/* 3668 */           responder.getCommunicator().sendNormalServerMessage("You change the upkeep plan.");
/*      */         } else {
/* 3670 */           responder.getCommunicator().sendNormalServerMessage("No change was made.");
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 3675 */       logger.log(Level.WARNING, responder.getName() + " tried to manage guards without the right.");
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final long getValueWithdrawn(Question question) {
/* 3680 */     String golds = question.getAnswer().getProperty("gold");
/* 3681 */     String silvers = question.getAnswer().getProperty("silver");
/* 3682 */     String coppers = question.getAnswer().getProperty("copper");
/* 3683 */     String irons = question.getAnswer().getProperty("iron");
/*      */ 
/*      */     
/*      */     try {
/* 3687 */       long wantedGold = 0L;
/* 3688 */       if (golds != null && golds.length() > 0)
/* 3689 */         wantedGold = Long.parseLong(golds); 
/* 3690 */       long wantedSilver = 0L;
/* 3691 */       if (silvers != null && silvers.length() > 0)
/* 3692 */         wantedSilver = Long.parseLong(silvers); 
/* 3693 */       long wantedCopper = 0L;
/* 3694 */       if (coppers != null && coppers.length() > 0)
/* 3695 */         wantedCopper = Long.parseLong(coppers); 
/* 3696 */       long wantedIron = 0L;
/* 3697 */       if (irons != null && irons.length() > 0)
/* 3698 */         wantedIron = Long.parseLong(irons); 
/* 3699 */       if (wantedGold < 0L) {
/*      */         
/* 3701 */         question.getResponder().getCommunicator()
/* 3702 */           .sendNormalServerMessage("You may not withdraw a negative amount of gold coins!");
/* 3703 */         return 0L;
/*      */       } 
/* 3705 */       if (wantedSilver < 0L) {
/*      */         
/* 3707 */         question.getResponder().getCommunicator()
/* 3708 */           .sendNormalServerMessage("You may not withdraw a negative amount of silver coins!");
/* 3709 */         return 0L;
/*      */       } 
/* 3711 */       if (wantedCopper < 0L) {
/*      */         
/* 3713 */         question.getResponder().getCommunicator()
/* 3714 */           .sendNormalServerMessage("You may not withdraw a negative amount of copper coins!");
/* 3715 */         return 0L;
/*      */       } 
/* 3717 */       if (wantedIron < 0L) {
/*      */         
/* 3719 */         question.getResponder().getCommunicator()
/* 3720 */           .sendNormalServerMessage("You may not withdraw a negative amount of iron coins!");
/* 3721 */         return 0L;
/*      */       } 
/* 3723 */       long valueWithdrawn = 1000000L * wantedGold;
/* 3724 */       valueWithdrawn += 10000L * wantedSilver;
/* 3725 */       valueWithdrawn += 100L * wantedCopper;
/* 3726 */       valueWithdrawn += 1L * wantedIron;
/* 3727 */       return valueWithdrawn;
/*      */     }
/* 3729 */     catch (NumberFormatException nfe) {
/*      */       
/* 3731 */       question.getResponder().getCommunicator().sendNormalServerMessage("The values were incorrect.");
/*      */       
/* 3733 */       return 0L;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void parsePlayerPaymentQuestion(PlayerPaymentQuestion question) {
/* 3742 */     Properties props = question.getAnswer();
/* 3743 */     String purch = props.getProperty("purchase");
/* 3744 */     Creature responder = question.getResponder();
/* 3745 */     boolean purchase = Boolean.parseBoolean(purch);
/* 3746 */     long money = responder.getMoney();
/* 3747 */     if (money < 100000L) {
/*      */       
/* 3749 */       responder.getCommunicator().sendAlertServerMessage("You do not have enough money to purchase game time for. You need at least 10 silver in your bank account.");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 3755 */     else if (purchase) {
/*      */ 
/*      */       
/*      */       try {
/* 3759 */         if (responder.chargeMoney(100000L))
/*      */         {
/* 3761 */           LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 3762 */           lsw.addPlayingTime(responder, responder.getName(), 1, 0, System.currentTimeMillis() + Servers.localServer.name);
/*      */ 
/*      */           
/* 3765 */           responder
/* 3766 */             .getCommunicator()
/* 3767 */             .sendSafeServerMessage("Your request for playing time is being processed. It may take up to half an hour until the system is fully updated.");
/*      */           
/* 3769 */           long diff = 30000L;
/*      */           
/* 3771 */           Economy.getEconomy().getKingsShop().setMoney(Economy.getEconomy().getKingsShop().getMoney() + 30000L);
/* 3772 */           logger.log(Level.INFO, responder.getName() + " purchased 1 month premium time for " + 10L + " silver coins. " + 30000L + " iron added to king.");
/*      */         }
/*      */         else
/*      */         {
/* 3776 */           responder.getCommunicator().sendAlertServerMessage("Failed to charge you 10 silvers. Please try later.");
/*      */         }
/*      */       
/* 3779 */       } catch (IOException ex) {
/*      */         
/* 3781 */         responder.getCommunicator().sendSafeServerMessage("Your request for playing time could not be processed.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String generateGuardMaleName() {
/* 3792 */     int rand = Server.rand.nextInt(50);
/* 3793 */     String[] firstPart = { "Carl", "John", "Bil", "Strong", "Dare", "Grave", "Hard", "Marde", "Verde", "Vold", "Tolk", "Roe", "Bee", "Har", "Rol", "Ma", "Lo", "Claw", "Drag", "Hug", "Te", "Two", "Fu", "Ji", "La", "Ze", "Jal", "Milk", "War", "Wild", "Hang", "Just", "Fan", "Cloclo", "Buy", "Bought", "Sard", "Smart", "Slo", "Shield", "Dark", "Hung", "Sed", "Sold", "Swing", "Gar", "Dig", "Bur", "Angel", "Sorrow" };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3798 */     int rand2 = Server.rand.nextInt(50);
/* 3799 */     String[] secondPart = { "ho", "john", "fish", "tree", "ooy", "olli", "tack", "rank", "sy", "moy", "dangly", "tok", "rich", "do", "mark", "stuf", "sin", "nyt", "wer", "mor", "emort", "vaar", "salm", "holm", "wyr", "zah", "ty", "fast", "der", "mar", "star", "bark", "oo", "flifil", "innow", "shoo", "husk", "eric", "ic", "o", "moon", "little", "ien", "strong", "arm", "hope", "slem", "tro", "rot", "heart" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3805 */     return firstPart[rand] + secondPart[rand2];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String generateGuardFemaleName() {
/* 3813 */     int rand = Server.rand.nextInt(50);
/* 3814 */     String[] firstPart = { "Too", "Sand", "Tree", "Whisper", "Lore", "Yan", "Van", "Vard", "Nard", "Oli", "Ala", "Krady", "Whe", "Har", "Zizi", "Zaza", "Lyn", "Claw", "Mali", "High", "Bright", "Star", "Nord", "Jala", "Yna", "Ze", "Jal", "Milk", "War", "Wild", "Fine", "Sweet", "Witty", "Cloclo", "Lory", "Tran", "Vide", "Lax", "Quick", "Shield", "Dark", "Light", "Cry", "Sold", "Juna", "Tear", "Cheek", "Ani", "Angel", "Sorro" };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3819 */     int rand2 = Server.rand.nextInt(50);
/* 3820 */     String[] secondPart = { "peno", "hag", "maiden", "woman", "loy", "oa", "dei", "sai", "nai", "nae", "ane", "aei", "peno", "doa", "ela", "hofaire", "sina", "nyta", "wera", "more", "emorta", "vaara", "salma", "holmi", "wyre", "zahe", "tya", "faste", "dere", "mara", "stare", "barkia", "ooa", "fila", "innowyn", "shoein", "huskyn", "erica", "ica", "oa", "moonie", "littly", "ieny", "strongie", "ermy", "hope", "steam", "high", "wind", "heart" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3826 */     return firstPart[rand] + secondPart[rand2];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void parseFriendQuestion(FriendQuestion question) {
/* 3835 */     Properties props = question.getAnswer();
/*      */     
/* 3837 */     Player responder = (Player)question.getResponder();
/*      */     
/*      */     try {
/* 3840 */       Player sender = Players.getInstance().getPlayer(question.getTarget());
/* 3841 */       String accept = props.getProperty("join");
/* 3842 */       if (accept != null)
/*      */       {
/* 3844 */         if (accept.equals("accept"))
/*      */         {
/* 3846 */           sender.addFriend(responder.getWurmId(), Friend.Category.Other.getCatId(), "");
/* 3847 */           responder.addFriend(sender.getWurmId(), Friend.Category.Other.getCatId(), "");
/* 3848 */           sender.getCommunicator().sendNormalServerMessage("You are now friends with " + responder.getName() + ".");
/* 3849 */           responder.getCommunicator().sendNormalServerMessage("You are now friends with " + sender.getName() + ".");
/*      */         }
/*      */         else
/*      */         {
/* 3853 */           responder.getCommunicator().sendNormalServerMessage("You decline the friendlist offer.");
/* 3854 */           sender.getCommunicator().sendNormalServerMessage(responder
/* 3855 */               .getName() + " declines your friends list invitation.");
/*      */         }
/*      */       
/*      */       }
/* 3859 */     } catch (NoSuchPlayerException nsp) {
/*      */       
/* 3861 */       responder.getCommunicator().sendNormalServerMessage("The player who wanted to add you to the friends list has logged off. You will not be added.");
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
/*      */   static final void parsePvPAllianceQuestion(AllianceQuestion question) {
/*      */     try {
/* 3874 */       Properties props = question.getAnswer();
/* 3875 */       Creature responder = question.getResponder();
/*      */       
/* 3877 */       Creature sender = Server.getInstance().getCreature(question.getTarget());
/* 3878 */       Village senderVillage = sender.getCitizenVillage();
/* 3879 */       Village responderVillage = responder.getCitizenVillage();
/*      */       
/* 3881 */       PvPAlliance respAlliance = PvPAlliance.getPvPAlliance(responderVillage.getAllianceNumber());
/* 3882 */       if (respAlliance != null) {
/*      */         
/* 3884 */         sender.getCommunicator().sendAlertServerMessage(responder
/* 3885 */             .getName() + " is already in the " + respAlliance.getName() + " alliance.");
/* 3886 */         responder.getCommunicator().sendAlertServerMessage("You are already in the " + respAlliance
/* 3887 */             .getName() + " alliance.");
/*      */         return;
/*      */       } 
/* 3890 */       String accept = props.getProperty("join");
/* 3891 */       if (accept != null)
/*      */       {
/* 3893 */         if (accept.equals("accept")) {
/*      */           
/* 3895 */           PvPAlliance oldAlliance = PvPAlliance.getPvPAlliance(sender.getCitizenVillage().getAllianceNumber());
/* 3896 */           if (oldAlliance != null)
/*      */           {
/* 3898 */             responder.getCitizenVillage().setAllianceNumber(oldAlliance.getId());
/* 3899 */             senderVillage.broadCastNormal("Under the rule of " + senderVillage.getMayor().getName() + " of " + senderVillage
/* 3900 */                 .getName() + ", " + responderVillage.getName() + " has been convinced to join the " + oldAlliance
/* 3901 */                 .getName() + ". Citizens rejoice!");
/* 3902 */             responderVillage.broadCastNormal("Under the rule of " + responderVillage.getMayor().getName() + ", " + responderVillage
/* 3903 */                 .getName() + " has joined the " + oldAlliance.getName() + ". Citizens rejoice!");
/*      */             
/* 3905 */             oldAlliance
/* 3906 */               .setWins(Math.max(oldAlliance.getNumberOfWins(), responder.getCitizenVillage().getHotaWins()));
/*      */             
/* 3908 */             responder.getCitizenVillage().sendMapAnnotationsToVillagers(oldAlliance
/* 3909 */                 .getAllianceMapAnnotationsArray());
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 3914 */             PvPAlliance newAlliance = new PvPAlliance(sender.getCitizenVillage().getId(), question.getAllianceName());
/* 3915 */             responder.getCitizenVillage().setAllianceNumber(newAlliance.getId());
/* 3916 */             sender.getCitizenVillage().setAllianceNumber(newAlliance.getId());
/* 3917 */             senderVillage.broadCastNormal("Under the rule of " + senderVillage.getMayor().getName() + " of " + senderVillage
/* 3918 */                 .getName() + ", " + responderVillage.getName() + " has been convinced to form the " + newAlliance
/* 3919 */                 .getName() + " alliance. Citizens rejoice!");
/* 3920 */             responderVillage.broadCastNormal("Under the rule of " + responderVillage.getMayor().getName() + ", " + responderVillage
/* 3921 */                 .getName() + " has formed the " + newAlliance.getName() + " alliance. Citizens rejoice!");
/*      */             
/* 3923 */             newAlliance.setWins(Math.max(responder.getCitizenVillage().getHotaWins(), sender.getCitizenVillage()
/* 3924 */                   .getHotaWins()));
/* 3925 */             responder.getCitizenVillage().sendMapAnnotationsToVillagers(newAlliance
/* 3926 */                 .getAllianceMapAnnotationsArray());
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 3931 */           responder.getCommunicator().sendNormalServerMessage("You decline the alliance offer.");
/* 3932 */           sender.getCommunicator().sendNormalServerMessage(responder
/* 3933 */               .getName() + " declines your generous offer to form an alliance.");
/*      */         }
/*      */       
/*      */       }
/* 3937 */     } catch (NoSuchCreatureException nsc) {
/*      */       
/* 3939 */       logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */     }
/* 3941 */     catch (NoSuchPlayerException nsp) {
/*      */       
/* 3943 */       logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void parseManageAllianceQuestion(ManageAllianceQuestion question) {
/* 3953 */     Properties props = question.getAnswer();
/* 3954 */     Creature responder = question.getResponder();
/*      */     
/* 3956 */     Village responderVillage = responder.getCitizenVillage();
/*      */ 
/*      */ 
/*      */     
/* 3960 */     PvPAlliance pvpAll = PvPAlliance.getPvPAlliance(responderVillage.getAllianceNumber());
/*      */     
/* 3962 */     if (pvpAll != null)
/*      */     {
/* 3964 */       if (responderVillage.mayDoDiplomacy(responder)) {
/*      */         
/* 3966 */         String motd = question.getAnswer().getProperty("motd");
/* 3967 */         if (motd != null)
/*      */         {
/* 3969 */           pvpAll.setMotd(motd.replaceAll("\"", ""));
/*      */         }
/*      */         
/* 3972 */         Village[] alliances = question.getAllies();
/* 3973 */         for (int x = 0; x < alliances.length; x++) {
/*      */           
/* 3975 */           int id = alliances[x].getId();
/* 3976 */           String str = props.getProperty("break" + String.valueOf(id));
/* 3977 */           if (str != null)
/*      */           {
/* 3979 */             if (Boolean.parseBoolean(str))
/*      */             {
/*      */ 
/*      */               
/* 3983 */               if (alliances[x].getId() == pvpAll.getId()) {
/*      */                 
/* 3985 */                 responder.getCitizenVillage().setAllianceNumber(0);
/* 3986 */                 responder.getCitizenVillage().sendClearMapAnnotationsOfType((byte)2);
/* 3987 */                 responder.getCitizenVillage().broadCastAlert(responder
/* 3988 */                     .getName() + " made " + responder.getCitizenVillage().getName() + " leave the " + pvpAll
/* 3989 */                     .getName() + ".");
/* 3990 */                 alliances[x].broadCastAlert(responder.getCitizenVillage().getName() + " has left the " + pvpAll
/* 3991 */                     .getName());
/* 3992 */                 alliances[x].addHistory(responder.getCitizenVillage().getName(), "left the " + pvpAll.getName() + ".");
/*      */                 
/* 3994 */                 if (!pvpAll.exists())
/*      */                 {
/* 3996 */                   alliances[x].broadCastAlert(pvpAll.getName() + " alliance has been disbanded.");
/* 3997 */                   pvpAll.delete();
/* 3998 */                   pvpAll.sendClearAllianceAnnotations();
/* 3999 */                   pvpAll.deleteAllianceMapAnnotations();
/* 4000 */                   alliances[x].setAllianceNumber(0);
/*      */                 }
/*      */               
/* 4003 */               } else if (responder.getCitizenVillage().getId() == pvpAll.getId()) {
/*      */                 
/* 4005 */                 alliances[x].setAllianceNumber(0);
/* 4006 */                 alliances[x].sendClearMapAnnotationsOfType((byte)2);
/* 4007 */                 responder.getCitizenVillage().broadCastAlert(responder
/* 4008 */                     .getName() + " ousted " + alliances[x].getName() + " from the " + pvpAll
/* 4009 */                     .getName() + ".");
/* 4010 */                 alliances[x].broadCastAlert("You have been ousted from the " + pvpAll.getName() + ".");
/* 4011 */                 alliances[x].addHistory(responder.getName(), "ousted you from the " + pvpAll.getName() + ".");
/* 4012 */                 if (!pvpAll.exists()) {
/*      */                   
/* 4014 */                   responder.getCitizenVillage().broadCastAlert(pvpAll
/* 4015 */                       .getName() + " alliance has been disbanded.");
/* 4016 */                   pvpAll.delete();
/* 4017 */                   pvpAll.sendClearAllianceAnnotations();
/* 4018 */                   pvpAll.deleteAllianceMapAnnotations();
/* 4019 */                   responder.getCitizenVillage().setAllianceNumber(0);
/*      */                 } 
/*      */               } else {
/*      */ 
/*      */                 
/*      */                 try {
/*      */                   
/* 4026 */                   Villages.getVillage(pvpAll.getId());
/*      */                 }
/* 4028 */                 catch (NoSuchVillageException nsv) {
/*      */                   
/* 4030 */                   responder.getCitizenVillage().setAllianceNumber(0);
/* 4031 */                   responder.getCitizenVillage().sendClearMapAnnotationsOfType((byte)2);
/* 4032 */                   logger.log(Level.INFO, responder.getName() + " made " + responder
/* 4033 */                       .getCitizenVillage().getName() + " leave the " + pvpAll.getName() + ".");
/* 4034 */                   responder.getCitizenVillage().broadCastAlert(responder
/* 4035 */                       .getName() + " made " + responder.getCitizenVillage().getName() + " leave the " + pvpAll
/* 4036 */                       .getName() + ".");
/* 4037 */                   alliances[x].broadCastAlert(responder.getCitizenVillage().getName() + " has left the " + pvpAll
/* 4038 */                       .getName());
/* 4039 */                   alliances[x].addHistory(responder.getCitizenVillage().getName(), "left the " + pvpAll
/* 4040 */                       .getName() + ".");
/*      */                 } 
/* 4042 */                 if (!pvpAll.exists()) {
/*      */                   
/* 4044 */                   alliances[x].broadCastAlert(pvpAll.getName() + " alliance has been disbanded.");
/* 4045 */                   pvpAll.delete();
/* 4046 */                   pvpAll.sendClearAllianceAnnotations();
/* 4047 */                   pvpAll.deleteAllianceMapAnnotations();
/* 4048 */                   alliances[x].setAllianceNumber(0);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */         
/* 4055 */         String key = "declareWar";
/* 4056 */         String val = props.getProperty(key);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4182 */         if (responderVillage.getMayor().getId() == responder.getWurmId()) {
/*      */           
/* 4184 */           key = "masterVill";
/* 4185 */           val = props.getProperty(key);
/* 4186 */           if (val != null && val.length() > 0) {
/*      */             
/*      */             try {
/*      */               
/* 4190 */               int index = Integer.parseInt(val);
/* 4191 */               if (index < alliances.length)
/*      */               {
/*      */                 
/* 4194 */                 if (alliances[index].getAllianceNumber() == responderVillage.getAllianceNumber()) {
/* 4195 */                   pvpAll.transferControl(responder, alliances[index].getId());
/*      */                 } else {
/* 4197 */                   responder.getCommunicator().sendNormalServerMessage("Unable to set " + alliances[index]
/* 4198 */                       .getName() + " as alliance capital, as they are no longer in the alliance");
/*      */                 }
/*      */               
/*      */               }
/* 4202 */             } catch (NumberFormatException nfe) {
/*      */               
/* 4204 */               responder.getCommunicator().sendAlertServerMessage("Failed to parse value for new capital.");
/*      */             } 
/*      */           }
/* 4207 */           key = "disbandAll";
/* 4208 */           val = props.getProperty(key);
/* 4209 */           if (val != null && val.equals("true"))
/*      */           {
/* 4211 */             pvpAll.disband(responder);
/*      */           }
/* 4213 */           key = "allName";
/* 4214 */           val = props.getProperty(key);
/* 4215 */           if (val != null && val.length() > 0)
/*      */           {
/* 4217 */             pvpAll.setName(responder, val);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/* 4222 */     if (responderVillage.warDeclarations != null) {
/*      */ 
/*      */       
/* 4225 */       WarDeclaration[] declArr = (WarDeclaration[])responderVillage.warDeclarations.values().toArray(
/* 4226 */           (Object[])new WarDeclaration[responderVillage.warDeclarations.size()]);
/* 4227 */       for (int y = 0; y < declArr.length; y++) {
/*      */         
/* 4229 */         if ((declArr[y]).declarer == responderVillage) {
/*      */           
/* 4231 */           int id = (declArr[y]).receiver.getId();
/* 4232 */           String val = props.getProperty("decl" + id);
/* 4233 */           if (Boolean.parseBoolean(val))
/*      */           {
/* 4235 */             declArr[y].dissolve(false);
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 4240 */           int id = (declArr[y]).declarer.getId();
/* 4241 */           String val = props.getProperty("recv" + id);
/* 4242 */           if (Boolean.parseBoolean(val))
/*      */           {
/* 4244 */             declArr[y].accept();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void parsePaymentQuestion(PaymentQuestion question) {
/* 4257 */     Properties props = question.getAnswer();
/* 4258 */     String ds = props.getProperty("days");
/* 4259 */     String ms = props.getProperty("months");
/* 4260 */     String wid = props.getProperty("wurmid");
/*      */     
/* 4262 */     Creature responder = question.getResponder();
/* 4263 */     int days = 0;
/* 4264 */     int months = 0;
/* 4265 */     int playerid = 0;
/* 4266 */     long wurmid = -10L;
/*      */     
/* 4268 */     if (ds != null && ds.length() > 0) {
/*      */       
/*      */       try {
/*      */         
/* 4272 */         days = Integer.parseInt(ds);
/*      */       }
/* 4274 */       catch (NumberFormatException nfe) {
/*      */         
/* 4276 */         responder.getCommunicator().sendAlertServerMessage("Failed to parse the string " + ds + " to a valid number.");
/*      */         return;
/*      */       } 
/*      */     }
/* 4280 */     if (ms != null && ms.length() > 0) {
/*      */       
/*      */       try {
/*      */         
/* 4284 */         months = Integer.parseInt(ms);
/*      */       }
/* 4286 */       catch (NumberFormatException nfe) {
/*      */         
/* 4288 */         responder.getCommunicator().sendAlertServerMessage("Failed to parse the string " + ms + " to a valid number.");
/*      */         return;
/*      */       } 
/*      */     }
/* 4292 */     if (wid != null) {
/*      */       
/*      */       try {
/*      */         
/* 4296 */         playerid = Integer.parseInt(wid);
/* 4297 */         Long pid = question.getPlayerId(playerid);
/* 4298 */         wurmid = pid.longValue();
/*      */       }
/* 4300 */       catch (NumberFormatException nfe) {
/*      */         
/* 4302 */         responder.getCommunicator().sendAlertServerMessage("Failed to parse the string " + wid + " to a valid number.");
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*      */     
/* 4308 */     if (wurmid != -10L) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4321 */       LoginServerWebConnection wit = new LoginServerWebConnection();
/* 4322 */       PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/* 4323 */       if (pinf != null) {
/*      */         
/* 4325 */         if (wit != null)
/*      */         {
/* 4327 */           if (wit.addPlayingTime(responder, pinf.getName(), months, days, responder
/* 4328 */               .getName() + Server.rand.nextInt(10000) + "add" + Servers.localServer.id)) {
/* 4329 */             responder.getCommunicator().sendNormalServerMessage("Ok, added " + months + " months and " + days + " days to " + pinf
/* 4330 */                 .getName() + ".");
/*      */           }
/*      */         }
/*      */       } else {
/*      */         
/* 4335 */         responder.getCommunicator().sendAlertServerMessage("Failed to find a player with that wurmid. Try on the login server.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4356 */       responder.getCommunicator().sendNormalServerMessage("The payment request is being processed.");
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
/*      */   public static final void parsePowerManagementQuestion(PowerManagementQuestion question) {
/* 4372 */     Properties props = question.getAnswer();
/* 4373 */     String pow = props.getProperty("power");
/* 4374 */     String wid = props.getProperty("wurmid");
/* 4375 */     Creature responder = question.getResponder();
/* 4376 */     if (responder.getPower() >= 3) {
/*      */       
/* 4378 */       long wurmid = -10L;
/* 4379 */       byte power = 0;
/* 4380 */       if (pow != null) {
/*      */         
/*      */         try {
/*      */           
/* 4384 */           power = Byte.parseByte(pow);
/*      */         }
/* 4386 */         catch (NumberFormatException nfe) {
/*      */           
/* 4388 */           responder.getCommunicator().sendNormalServerMessage("Failed to parse the string " + pow + " to a valid number between -127 and 127.");
/*      */           
/*      */           return;
/*      */         } 
/*      */       }
/*      */       
/* 4394 */       if (wid != null) {
/*      */         
/*      */         try {
/*      */           
/* 4398 */           int pos = Integer.parseInt(wid);
/* 4399 */           Long widdy = question.getPlayerId(pos);
/* 4400 */           wurmid = widdy.longValue();
/*      */         }
/* 4402 */         catch (NumberFormatException nfe) {
/*      */           
/* 4404 */           responder.getCommunicator().sendNormalServerMessage("Failed to parse the string " + wid + " to a valid number.");
/*      */           
/*      */           return;
/*      */         } 
/*      */       }
/* 4409 */       if (wurmid != -10L) {
/*      */         
/*      */         try {
/*      */           
/* 4413 */           Player player = Players.getInstance().getPlayer(wurmid);
/* 4414 */           if (responder.getPower() >= power) {
/*      */ 
/*      */             
/*      */             try {
/* 4418 */               player.setPower(power);
/* 4419 */               String powString = "normal adventurer";
/* 4420 */               if (power == 1) {
/* 4421 */                 powString = "hero";
/* 4422 */               } else if (power == 2) {
/* 4423 */                 powString = "demigod";
/* 4424 */               } else if (power == 3) {
/* 4425 */                 powString = "high god";
/* 4426 */               } else if (power == 4) {
/* 4427 */                 powString = "arch angel";
/* 4428 */               } else if (power == 5) {
/* 4429 */                 powString = "implementor";
/* 4430 */               }  player.getCommunicator().sendSafeServerMessage("Your status has been set by " + responder
/* 4431 */                   .getName() + " to " + powString + "!");
/* 4432 */               responder.getCommunicator().sendSafeServerMessage("You set the power of " + player
/* 4433 */                   .getName() + " to the status of " + powString);
/* 4434 */               responder.getLogger().log(Level.INFO, responder
/* 4435 */                   .getName() + " set the power of " + player.getName() + " to " + powString + ".");
/*      */             }
/* 4437 */             catch (IOException iox) {
/*      */               
/* 4439 */               logger.log(Level.WARNING, "Failed to change the power of " + player.getName() + " to " + power + ": " + iox
/* 4440 */                   .getMessage(), iox);
/* 4441 */               responder.getCommunicator().sendSafeServerMessage("Failed to set the power of " + player
/* 4442 */                   .getName() + " to the new status: " + iox
/* 4443 */                   .getMessage());
/*      */             } 
/*      */           } else {
/*      */             
/* 4447 */             responder.getCommunicator().sendSafeServerMessage("You can not set powers above your level.");
/*      */           } 
/* 4449 */         } catch (NoSuchPlayerException nsp) {
/*      */           
/* 4451 */           responder.getCommunicator().sendNormalServerMessage("Failed to locate player with wurmid " + wurmid + ".");
/*      */         }
/*      */       
/*      */       }
/*      */     } else {
/*      */       
/* 4457 */       logger.warning(responder.getName() + " tried to set the power of a Player but did not have enough power.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseGmVillageAdQuestion(GmVillageAdInterface question) {
/* 4464 */     Properties props = question.getAnswer();
/* 4465 */     RecruitmentAd[] ads = RecruitmentAds.getAllRecruitmentAds();
/*      */     
/* 4467 */     for (int i = 0; i < ads.length; i++) {
/*      */       
/* 4469 */       String key = ads[i].getVillageId() + "remove";
/* 4470 */       String val = props.getProperty(key);
/* 4471 */       if (val != null && Boolean.parseBoolean(val))
/*      */       {
/* 4473 */         RecruitmentAds.remove(ads[i]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseTraderManagementQuestion(TraderManagementQuestion question) {
/* 4484 */     Creature responder = question.getResponder();
/* 4485 */     Shop shop = null;
/* 4486 */     Item contract = null;
/* 4487 */     Properties props = question.getAnswer();
/* 4488 */     Creature trader = null;
/* 4489 */     long traderId = -1L;
/*      */     
/*      */     try {
/* 4492 */       contract = Items.getItem(question.getTarget());
/* 4493 */       if (contract.getOwner() == responder.getWurmId()) {
/*      */         
/* 4495 */         traderId = contract.getData();
/* 4496 */         if (traderId != -1L)
/*      */         {
/* 4498 */           trader = Server.getInstance().getCreature(traderId);
/* 4499 */           shop = Economy.getEconomy().getShop(trader);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4504 */         responder.getCommunicator().sendNormalServerMessage("You are no longer in possesion of the " + contract
/* 4505 */             .getName() + "!");
/*      */         
/*      */         return;
/*      */       } 
/* 4509 */     } catch (NoSuchItemException nsi) {
/*      */       
/* 4511 */       logger.log(Level.WARNING, responder.getName() + " contract is missing! Contract ID: " + question.getTarget());
/* 4512 */       responder.getCommunicator().sendNormalServerMessage("You are no longer in possesion of the contract!");
/*      */       
/*      */       return;
/* 4515 */     } catch (NoSuchPlayerException nsp) {
/*      */       
/* 4517 */       logger.log(Level.WARNING, "Trader for " + responder.getName() + " is a player? Well it can't be found. Contract ID: " + question
/* 4518 */           .getTarget());
/*      */       
/* 4520 */       responder.getCommunicator().sendNormalServerMessage("The contract has been damaged by water. You can't read the letters!");
/*      */       
/* 4522 */       if (contract != null) {
/* 4523 */         contract.setData(-1, -1);
/*      */       }
/*      */       return;
/* 4526 */     } catch (NoSuchCreatureException nsc) {
/*      */       
/* 4528 */       logger.log(Level.WARNING, "Trader for " + responder
/* 4529 */           .getName() + " can't be found. Contract ID: " + question.getTarget());
/* 4530 */       responder.getCommunicator().sendNormalServerMessage("The contract has been damaged by water. You can't read the letters!");
/*      */       
/* 4532 */       if (contract != null) {
/* 4533 */         contract.setData(-1, -1);
/*      */       }
/*      */       return;
/* 4536 */     } catch (NotOwnedException no) {
/*      */       
/* 4538 */       responder.getCommunicator().sendNormalServerMessage("You are no longer in possesion of the " + contract
/* 4539 */           .getName() + "!");
/*      */       return;
/*      */     } 
/* 4542 */     if (shop != null) {
/*      */       
/* 4544 */       String key = traderId + "dismiss";
/* 4545 */       String val = props.getProperty(key);
/* 4546 */       if (Boolean.parseBoolean(val)) {
/*      */         
/* 4548 */         if (trader != null) {
/*      */           
/* 4550 */           if (!trader.isTrading()) {
/*      */             
/* 4552 */             Server.getInstance().broadCastAction(trader
/* 4553 */                 .getName() + " grunts, packs " + trader.getHisHerItsString() + " things and is off.", trader, 5);
/*      */             
/* 4555 */             responder.getCommunicator().sendNormalServerMessage("You dismiss " + trader
/* 4556 */                 .getName() + " from " + trader.getHisHerItsString() + " post.");
/* 4557 */             logger.log(Level.INFO, responder.getName() + " dismisses trader " + trader.getName() + " with Contract ID: " + question
/* 4558 */                 .getTarget());
/* 4559 */             trader.destroy();
/* 4560 */             contract.setData(-1, -1);
/*      */           } else {
/*      */             
/* 4563 */             responder.getCommunicator().sendNormalServerMessage(trader.getName() + " is trading. Try later.");
/*      */           } 
/*      */         } else {
/* 4566 */           responder.getCommunicator().sendNormalServerMessage("An error occured on the server while dismissing the trader.");
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 4574 */         key = traderId + "local";
/* 4575 */         val = props.getProperty(key);
/* 4576 */         boolean useLocal = Boolean.parseBoolean(val);
/* 4577 */         key = traderId + "pricemod";
/* 4578 */         val = props.getProperty(key);
/* 4579 */         float priceMod = shop.getPriceModifier();
/* 4580 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 4584 */             priceMod = Float.parseFloat(val);
/* 4585 */             shop.setPriceModifier(priceMod);
/*      */           }
/* 4587 */           catch (NumberFormatException f) {
/*      */             
/* 4589 */             responder.getCommunicator().sendSafeServerMessage("Failed to set price modifier to " + val + ". Make sure it is a decimal figure using '.'-notation.");
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 4594 */         key = traderId + "manage";
/* 4595 */         val = props.getProperty(key);
/* 4596 */         boolean manageItems = Boolean.parseBoolean(val);
/*      */         
/* 4598 */         shop.setUseLocalPrice(useLocal);
/* 4599 */         if (manageItems)
/*      */         {
/* 4601 */           MultiPriceManageQuestion mpm = new MultiPriceManageQuestion(responder, "Price management.", "Set prices for items", traderId);
/*      */           
/* 4603 */           mpm.sendQuestion();
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 4609 */       String tname = props.getProperty("ptradername");
/* 4610 */       String gender = props.getProperty("gender");
/* 4611 */       byte sex = 0;
/* 4612 */       if (gender.equals("female"))
/* 4613 */         sex = 1; 
/* 4614 */       if (tname != null && tname.length() > 0) {
/*      */         
/* 4616 */         if (tname.length() < 3 || tname.length() > 20 || containsIllegalCharacters(tname))
/*      */         {
/* 4618 */           if (sex == 0) {
/*      */             
/* 4620 */             tname = generateGuardMaleName();
/* 4621 */             responder.getCommunicator().sendSafeServerMessage("The name didn't fit the trader, so he chose another one.");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 4626 */             responder.getCommunicator().sendSafeServerMessage("The name didn't fit the trader, so she chose another one.");
/*      */             
/* 4628 */             tname = generateGuardFemaleName();
/*      */           } 
/*      */         }
/* 4631 */         tname = StringUtilities.raiseFirstLetter(tname);
/* 4632 */         tname = "Merchant_" + tname;
/* 4633 */         VolaTile tile = responder.getCurrentTile();
/* 4634 */         if (tile != null) {
/*      */           
/* 4636 */           boolean stall = false;
/* 4637 */           Item[] items = tile.getItems();
/*      */           
/* 4639 */           for (int xx = 0; xx < items.length; xx++) {
/*      */             
/* 4641 */             if (items[xx].isMarketStall()) {
/*      */               
/* 4643 */               stall = true;
/*      */               break;
/*      */             } 
/*      */           } 
/* 4647 */           if (!Methods.isActionAllowed(responder, (short)85)) {
/*      */             return;
/*      */           }
/* 4650 */           Structure struct = tile.getStructure();
/* 4651 */           if (stall || (struct != null && struct.isFinished()) || responder.getPower() > 1) {
/*      */             
/* 4653 */             Creature[] crets = tile.getCreatures();
/* 4654 */             boolean notok = false;
/* 4655 */             for (int x = 0; x < crets.length; x++) {
/*      */               
/* 4657 */               if (!crets[x].isPlayer()) {
/*      */                 
/* 4659 */                 notok = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/* 4663 */             if (!notok) {
/*      */               
/* 4665 */               if (struct != null && !struct.isTypeBridge() && !struct.mayPlaceMerchants(responder)) {
/*      */                 
/* 4667 */                 responder.getCommunicator().sendNormalServerMessage("You do not have permission to place a trader in this building.");
/*      */               } else {
/*      */ 
/*      */                 
/*      */                 try {
/*      */ 
/*      */                   
/* 4674 */                   trader = Creature.doNew(9, (tile.getTileX() << 2) + 2.0F, (tile.getTileY() << 2) + 2.0F, 180.0F, responder
/* 4675 */                       .getLayer(), tname, sex, responder.getKingdomId());
/* 4676 */                   if (responder.getFloorLevel(true) != 0) {
/* 4677 */                     trader.pushToFloorLevel(responder.getFloorLevel());
/*      */                   }
/* 4679 */                   shop = Economy.getEconomy().createShop(trader.getWurmId(), responder.getWurmId());
/* 4680 */                   contract.setData(trader.getWurmId());
/* 4681 */                   logger.info(responder.getName() + " created a trader: " + trader);
/*      */                 }
/* 4683 */                 catch (Exception ex) {
/*      */                   
/* 4685 */                   responder.getCommunicator().sendAlertServerMessage("An error occured in the rifts of the void. The trader was not created.");
/*      */                   
/* 4687 */                   logger.log(Level.WARNING, responder.getName() + " failed to create trader.", ex);
/*      */                 } 
/*      */               } 
/*      */             } else {
/*      */               
/* 4692 */               responder.getCommunicator().sendNormalServerMessage("The trader will only set up shop where no other creatures except you are standing.");
/*      */             } 
/*      */           } else {
/*      */             
/* 4696 */             responder.getCommunicator().sendNormalServerMessage("The trader will only set up shop inside a finished building or by a market stall.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseMultiPriceQuestion(MultiPriceManageQuestion question) {
/* 4709 */     Creature responder = question.getResponder();
/* 4710 */     long target = question.getTarget();
/*      */     
/*      */     try {
/* 4713 */       Creature trader = Server.getInstance().getCreature(target);
/* 4714 */       if (trader.isNpcTrader()) {
/*      */         
/* 4716 */         Shop shop = Economy.getEconomy().getShop(trader);
/* 4717 */         Properties props = question.getAnswer();
/* 4718 */         if (shop == null) {
/*      */           
/* 4720 */           responder.getCommunicator().sendNormalServerMessage("No shop registered for that creature.");
/*      */ 
/*      */         
/*      */         }
/* 4724 */         else if (shop.getOwnerId() == responder.getWurmId()) {
/*      */           
/* 4726 */           Item[] items = trader.getInventory().getAllItems(false);
/* 4727 */           Arrays.sort((Object[])items);
/* 4728 */           String key = "";
/* 4729 */           String val = "";
/* 4730 */           int price = 0;
/* 4731 */           Map<Long, Integer> itemMap = question.getItemMap();
/* 4732 */           for (int x = 0; x < items.length; x++) {
/*      */             
/* 4734 */             if (!items[x].isFullprice()) {
/*      */               
/* 4736 */               price = 0;
/* 4737 */               long id = items[x].getWurmId();
/* 4738 */               Integer bbid = itemMap.get(new Long(id));
/* 4739 */               int bid = bbid.intValue();
/*      */               
/* 4741 */               key = bid + "g";
/* 4742 */               val = props.getProperty(key);
/* 4743 */               if (val != null && val.length() > 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 4747 */                   price = Integer.parseInt(val) * 1000000;
/*      */                 }
/* 4749 */                 catch (NumberFormatException nfe) {
/*      */                   
/* 4751 */                   responder.getCommunicator().sendNormalServerMessage("Failed to set the gold price for " + items[x]
/* 4752 */                       .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */                 } 
/*      */               }
/*      */               
/* 4756 */               key = bid + "s";
/* 4757 */               val = props.getProperty(key);
/* 4758 */               if (val != null && val.length() > 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 4762 */                   price += Integer.parseInt(val) * 10000;
/*      */                 }
/* 4764 */                 catch (NumberFormatException nfe) {
/*      */                   
/* 4766 */                   responder.getCommunicator().sendNormalServerMessage("Failed to set a silver price for " + items[x]
/* 4767 */                       .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */                 } 
/*      */               }
/*      */               
/* 4771 */               key = bid + "c";
/* 4772 */               val = props.getProperty(key);
/* 4773 */               if (val != null && val.length() > 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 4777 */                   price += Integer.parseInt(val) * 100;
/*      */                 }
/* 4779 */                 catch (NumberFormatException nfe) {
/*      */                   
/* 4781 */                   responder.getCommunicator().sendNormalServerMessage("Failed to set a copper price for " + items[x]
/* 4782 */                       .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */                 } 
/*      */               }
/*      */               
/* 4786 */               key = bid + "i";
/* 4787 */               val = props.getProperty(key);
/* 4788 */               if (val != null && val.length() > 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 4792 */                   price += Integer.parseInt(val);
/*      */                 }
/* 4794 */                 catch (NumberFormatException nfe) {
/*      */                   
/* 4796 */                   responder.getCommunicator().sendNormalServerMessage("Failed to set an iron price for " + items[x]
/* 4797 */                       .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */                 } 
/*      */               }
/*      */ 
/*      */               
/* 4802 */               items[x].setPrice(price);
/*      */             } 
/*      */           } 
/* 4805 */           responder.getCommunicator().sendNormalServerMessage("The prices are updated.");
/*      */         } else {
/*      */           
/* 4808 */           responder.getCommunicator().sendNormalServerMessage("You don't own that shop.");
/*      */         }
/*      */       
/*      */       } 
/* 4812 */     } catch (NoSuchCreatureException nsc) {
/*      */       
/* 4814 */       responder.getCommunicator().sendNormalServerMessage("No such creature.");
/* 4815 */       logger.log(Level.WARNING, responder.getName(), (Throwable)nsc);
/*      */     }
/* 4817 */     catch (NoSuchPlayerException nsp) {
/*      */       
/* 4819 */       responder.getCommunicator().sendNormalServerMessage("No such creature.");
/* 4820 */       logger.log(Level.WARNING, responder.getName(), (Throwable)nsp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseSinglePriceQuestion(SinglePriceManageQuestion question) {
/* 4830 */     Creature responder = question.getResponder();
/* 4831 */     Properties props = question.getAnswer();
/* 4832 */     long target = question.getTarget();
/* 4833 */     if (target == -10L) {
/*      */       
/* 4835 */       NameCountList itemNames = new NameCountList();
/*      */       
/* 4837 */       int price = 0;
/* 4838 */       String val = props.getProperty("gold");
/* 4839 */       if (val != null && val.length() > 0) {
/*      */         
/*      */         try {
/*      */           
/* 4843 */           price = Integer.parseInt(val) * 1000000;
/*      */         }
/* 4845 */         catch (NumberFormatException nfe) {
/*      */           
/* 4847 */           responder.getCommunicator().sendNormalServerMessage("Failed to set the gold price. Note that a coin value is in whole numbers, no decimals.");
/*      */         } 
/*      */       }
/*      */       
/* 4851 */       val = props.getProperty("silver");
/* 4852 */       if (val != null && val.length() > 0) {
/*      */         
/*      */         try {
/*      */           
/* 4856 */           price += Integer.parseInt(val) * 10000;
/*      */         }
/* 4858 */         catch (NumberFormatException nfe) {
/*      */           
/* 4860 */           responder.getCommunicator().sendNormalServerMessage("Failed to set a silver price. Note that a coin value is in whole numbers, no decimals.");
/*      */         } 
/*      */       }
/*      */       
/* 4864 */       val = props.getProperty("copper");
/* 4865 */       if (val != null && val.length() > 0) {
/*      */         
/*      */         try {
/*      */           
/* 4869 */           price += Integer.parseInt(val) * 100;
/*      */         }
/* 4871 */         catch (NumberFormatException nfe) {
/*      */           
/* 4873 */           responder.getCommunicator().sendNormalServerMessage("Failed to set a copper price. Note that a coin value is in whole numbers, no decimals.");
/*      */         } 
/*      */       }
/*      */       
/* 4877 */       val = props.getProperty("iron");
/* 4878 */       if (val != null && val.length() > 0) {
/*      */         
/*      */         try {
/*      */           
/* 4882 */           price += Integer.parseInt(val);
/*      */         }
/* 4884 */         catch (NumberFormatException nfe) {
/*      */           
/* 4886 */           responder.getCommunicator().sendNormalServerMessage("Failed to set an iron price. Note that a coin value is in whole numbers, no decimals.");
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 4891 */       for (Item item : question.getItems()) {
/*      */         
/* 4893 */         if (item.isFullprice()) {
/* 4894 */           responder.getCommunicator().sendNormalServerMessage("You cannot set the price of " + item
/* 4895 */               .getName() + ".");
/* 4896 */         } else if (item.getOwnerId() != responder.getWurmId()) {
/* 4897 */           responder.getCommunicator().sendNormalServerMessage("You don't own " + item
/* 4898 */               .getName() + ".");
/*      */         } else {
/*      */           
/* 4901 */           item.setPrice(price);
/* 4902 */           itemNames.add(item.getName());
/*      */         } 
/*      */       } 
/* 4905 */       if (!itemNames.isEmpty())
/*      */       {
/* 4907 */         responder.getCommunicator().sendNormalServerMessage("You set the price to " + 
/* 4908 */             Economy.getEconomy().getChangeFor(price).getChangeString() + " for " + itemNames
/* 4909 */             .toString() + ".");
/*      */       }
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 4916 */         Item item = Items.getItem(target);
/* 4917 */         if (item.getOwnerId() == responder.getWurmId()) {
/*      */           
/* 4919 */           int price = 0;
/* 4920 */           long id = item.getWurmId();
/* 4921 */           String key = id + "gold";
/* 4922 */           String val = props.getProperty(key);
/* 4923 */           if (val != null && val.length() > 0) {
/*      */             
/*      */             try {
/*      */               
/* 4927 */               price = Integer.parseInt(val) * 1000000;
/*      */             }
/* 4929 */             catch (NumberFormatException nfe) {
/*      */               
/* 4931 */               responder.getCommunicator().sendNormalServerMessage("Failed to set the gold price for " + item
/* 4932 */                   .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */             } 
/*      */           }
/*      */           
/* 4936 */           key = id + "silver";
/* 4937 */           val = props.getProperty(key);
/* 4938 */           if (val != null && val.length() > 0) {
/*      */             
/*      */             try {
/*      */               
/* 4942 */               price += Integer.parseInt(val) * 10000;
/*      */             }
/* 4944 */             catch (NumberFormatException nfe) {
/*      */               
/* 4946 */               responder.getCommunicator().sendNormalServerMessage("Failed to set a silver price for " + item
/* 4947 */                   .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */             } 
/*      */           }
/*      */           
/* 4951 */           key = id + "copper";
/* 4952 */           val = props.getProperty(key);
/* 4953 */           if (val != null && val.length() > 0) {
/*      */             
/*      */             try {
/*      */               
/* 4957 */               price += Integer.parseInt(val) * 100;
/*      */             }
/* 4959 */             catch (NumberFormatException nfe) {
/*      */               
/* 4961 */               responder.getCommunicator().sendNormalServerMessage("Failed to set a copper price for " + item
/* 4962 */                   .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */             } 
/*      */           }
/*      */           
/* 4966 */           key = id + "iron";
/* 4967 */           val = props.getProperty(key);
/* 4968 */           if (val != null && val.length() > 0) {
/*      */             
/*      */             try {
/*      */               
/* 4972 */               price += Integer.parseInt(val);
/*      */             }
/* 4974 */             catch (NumberFormatException nfe) {
/*      */               
/* 4976 */               responder.getCommunicator().sendNormalServerMessage("Failed to set an iron price for " + item
/* 4977 */                   .getName() + ". Note that a coin value is in whole numbers, no decimals.");
/*      */             } 
/*      */           }
/*      */           
/* 4981 */           item.setPrice(price);
/* 4982 */           responder.getCommunicator().sendNormalServerMessage("Set price to " + 
/* 4983 */               Economy.getEconomy().getChangeFor(price).getChangeString() + ".");
/*      */         } else {
/*      */           
/* 4986 */           responder.getCommunicator().sendNormalServerMessage("You don't own that item.");
/*      */         } 
/* 4988 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 4990 */         responder.getCommunicator().sendNormalServerMessage("No such item.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parseTraderRentalQuestion(TraderRentalQuestion question) {
/* 5001 */     Creature responder = question.getResponder();
/* 5002 */     Properties props = question.getAnswer();
/*      */     
/* 5004 */     if (!responder.isOnSurface()) {
/*      */       
/* 5006 */       responder.getCommunicator().sendNormalServerMessage("The trader refuses to work in this cave.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*      */     try {
/* 5012 */       Item contract = Items.getItem(question.getTarget());
/* 5013 */       if (contract.getOwner() != responder.getWurmId()) {
/*      */         
/* 5015 */         responder.getCommunicator().sendNormalServerMessage("You are no longer in possesion of the " + contract
/* 5016 */             .getName() + "!");
/*      */         
/*      */         return;
/*      */       } 
/* 5020 */     } catch (NoSuchItemException nsi) {
/*      */       
/* 5022 */       responder.getCommunicator().sendNormalServerMessage("The contract no longer exists!");
/*      */       
/*      */       return;
/* 5025 */     } catch (NotOwnedException no) {
/*      */       
/* 5027 */       responder.getCommunicator().sendNormalServerMessage("You are no longer in possesion of the contract!");
/*      */       return;
/*      */     } 
/* 5030 */     String nname = props.getProperty("ntradername");
/*      */     
/*      */     try {
/* 5033 */       Items.getItem(question.getTarget());
/* 5034 */       String gender = props.getProperty("gender");
/*      */       
/* 5036 */       byte sex = 0;
/* 5037 */       if (gender.equals("female"))
/* 5038 */         sex = 1; 
/* 5039 */       if (nname != null && nname.length() > 0) {
/*      */         
/* 5041 */         if (nname.length() < 3 || nname.length() > 20 || containsIllegalCharacters(nname))
/*      */         {
/* 5043 */           if (sex == 0) {
/*      */             
/* 5045 */             nname = generateGuardMaleName();
/* 5046 */             responder.getCommunicator().sendSafeServerMessage("The name didn't fit the trader, so he chose another one.");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 5051 */             responder.getCommunicator().sendSafeServerMessage("The name didn't fit the trader, so she chose another one.");
/*      */             
/* 5053 */             nname = generateGuardFemaleName();
/*      */           } 
/*      */         }
/* 5056 */         nname = StringUtilities.raiseFirstLetter(nname);
/* 5057 */         nname = "Trader_" + nname;
/* 5058 */         VolaTile tile = responder.getCurrentTile();
/* 5059 */         if (tile != null) {
/*      */           
/* 5061 */           Creature t = Economy.getEconomy().getTraderForZone(tile.getTileX(), tile.getTileY(), tile
/* 5062 */               .isOnSurface());
/* 5063 */           if (t == null) {
/*      */             
/* 5065 */             Structure struct = tile.getStructure();
/* 5066 */             if ((struct != null && struct.isFinished()) || responder.getPower() > 0) {
/*      */               
/* 5068 */               Creature[] crets = tile.getCreatures();
/* 5069 */               if (crets != null && crets.length == 1) {
/*      */                 
/* 5071 */                 int tax = 0;
/* 5072 */                 Village v = tile.getVillage();
/* 5073 */                 if (v != null) {
/*      */                   
/* 5075 */                   String taxs = props.getProperty("tax");
/* 5076 */                   if (taxs == null || taxs.length() == 0) {
/*      */                     
/* 5078 */                     responder
/* 5079 */                       .getCommunicator()
/* 5080 */                       .sendAlertServerMessage("The tax you filled in is not appropriate. Make sure it is a number between 0 and 40.");
/*      */ 
/*      */                     
/*      */                     return;
/*      */                   } 
/*      */ 
/*      */                   
/*      */                   try {
/* 5088 */                     tax = Integer.parseInt(taxs);
/* 5089 */                     if (tax < 0 || tax > 40) {
/*      */                       
/* 5091 */                       responder
/* 5092 */                         .getCommunicator()
/* 5093 */                         .sendAlertServerMessage("The tax you filled in is not appropriate. Make sure it is a number between 0 and 40.");
/*      */ 
/*      */                       
/*      */                       return;
/*      */                     } 
/* 5098 */                   } catch (NumberFormatException nfw) {
/*      */                     
/* 5100 */                     responder
/* 5101 */                       .getCommunicator()
/* 5102 */                       .sendAlertServerMessage("The tax you filled in is not appropriate. Make sure it is a whole number between 0 and 40.");
/*      */ 
/*      */                     
/*      */                     return;
/*      */                   } 
/*      */                 } 
/*      */                 
/*      */                 try {
/* 5110 */                   Creature trader = Creature.doNew(9, (tile.getTileX() << 2) + 2.0F, (tile
/* 5111 */                       .getTileY() << 2) + 2.0F, 180.0F, responder.getLayer(), nname, sex, responder
/* 5112 */                       .getKingdomId());
/* 5113 */                   Shop shop = Economy.getEconomy().createShop(trader.getWurmId());
/*      */                   
/* 5115 */                   if (tax > 0)
/* 5116 */                     shop.setTax(tax / 100.0F); 
/* 5117 */                   Items.destroyItem(question.getTarget());
/* 5118 */                   if (v != null)
/*      */                   {
/* 5120 */                     v.addCitizen(trader, v.getRoleForStatus((byte)3));
/*      */                   }
/*      */                 }
/* 5123 */                 catch (Exception ex) {
/*      */                   
/* 5125 */                   responder.getCommunicator().sendAlertServerMessage("An error occured in the rifts of the void. The trader was not created.");
/*      */                   
/* 5127 */                   logger.log(Level.WARNING, responder.getName() + " failed to create trader.", ex);
/*      */                 } 
/*      */               } else {
/*      */                 
/* 5131 */                 responder.getCommunicator().sendNormalServerMessage("The trader will only set up shop where no other creatures except you are standing.");
/*      */               } 
/*      */             } else {
/*      */               
/* 5135 */               responder.getCommunicator().sendNormalServerMessage("The trader will only set up shop inside a finished building.");
/*      */             } 
/*      */           } else {
/*      */             
/* 5139 */             responder.getCommunicator().sendNormalServerMessage("The new trader would be too close to the shop of " + t
/* 5140 */                 .getName() + ". He refuses to set up shop here.");
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/* 5145 */     } catch (NoSuchItemException nsi) {
/*      */       
/* 5147 */       responder.getCommunicator().sendNormalServerMessage("Failed to locate the contract for that request.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void setReputation(Creature responder, int value, String name, long wurmId, boolean perma, Village village) {
/* 5154 */     int oldvalue = village.getReputation(wurmId);
/* 5155 */     boolean warning = false;
/*      */     
/* 5157 */     if (!Servers.localServer.PVPSERVER && Servers.localServer.id != 3)
/*      */     {
/* 5159 */       if (value > -30)
/*      */       {
/* 5161 */         if (Players.getInstance().removeKosFor(wurmId))
/* 5162 */           village.addHistory(responder.getName(), "pardons " + name + "."); 
/*      */       }
/*      */     }
/* 5165 */     if (oldvalue != value) {
/*      */       
/* 5167 */       if (oldvalue <= -30) {
/*      */         
/* 5169 */         if (value > -30)
/*      */         {
/* 5171 */           village.addHistory(responder.getName(), "pardons " + name + ".");
/*      */         }
/*      */       }
/* 5174 */       else if (value <= -30) {
/*      */         
/* 5176 */         if (oldvalue > -30)
/*      */         {
/* 5178 */           if (!Servers.localServer.PVPSERVER && Servers.localServer.id != 3 && value <= -30) {
/*      */ 
/*      */             
/* 5181 */             warning = true;
/* 5182 */             if (Players.getInstance().addKosWarning(new KosWarning(wurmId, value, village, perma))) {
/*      */               
/* 5184 */               responder
/* 5185 */                 .getCommunicator()
/* 5186 */                 .sendNormalServerMessage(name + " will receive a warning to leave the settlement. 3 minutes later the reputation will take the effect and he will be attacked by the guards.");
/*      */ 
/*      */               
/* 5189 */               village.addHistory(responder.getName(), "adds " + name + " to the KOS warning list.");
/*      */             } else {
/*      */               
/* 5192 */               responder.getCommunicator().sendNormalServerMessage(name + " is already put up on the kos list, pending activation.");
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 5197 */             village.addHistory(responder.getName(), "declares " + name + " to be a criminal.");
/*      */           }  } 
/*      */       } 
/* 5200 */       if (!warning) {
/*      */         
/* 5202 */         Reputation r = village.setReputation(wurmId, value, false, true);
/* 5203 */         if (r != null) {
/*      */           
/* 5205 */           r.setPermanent(perma);
/*      */         } else {
/*      */           
/* 5208 */           responder.getCommunicator().sendNormalServerMessage("The reputation for " + name + " was deleted.");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parseReputationQuestion(ReputationQuestion question) {
/* 5219 */     Creature responder = question.getResponder();
/* 5220 */     long target = question.getTarget();
/*      */     
/*      */     try {
/*      */       Village village;
/*      */       
/* 5225 */       if (target == -10L) {
/* 5226 */         village = responder.getCitizenVillage();
/*      */       } else {
/*      */         
/* 5229 */         Item deed = Items.getItem(target);
/* 5230 */         int villageId = deed.getData2();
/* 5231 */         village = Villages.getVillage(villageId);
/*      */       } 
/* 5233 */       long touched = -10L;
/* 5234 */       if (village == null) {
/*      */         
/* 5236 */         responder.getCommunicator().sendNormalServerMessage("No village found.");
/*      */       }
/*      */       else {
/*      */         
/* 5240 */         Properties props = question.getAnswer();
/* 5241 */         Reputation[] reputations = village.getReputations();
/* 5242 */         String key = "";
/* 5243 */         String val = "";
/* 5244 */         int value = 0;
/* 5245 */         Map<Long, Integer> itemMap = question.getItemMap();
/* 5246 */         key = "nn";
/* 5247 */         val = props.getProperty(key);
/* 5248 */         if (val != null && val.length() > 0) {
/*      */           
/*      */           try {
/*      */             
/* 5252 */             Player player = Players.getInstance().getPlayer(StringUtilities.raiseFirstLetter(val));
/*      */             
/* 5254 */             key = "nr";
/* 5255 */             val = props.getProperty(key);
/* 5256 */             if (val != null && val.length() > 0) {
/*      */               
/*      */               try {
/*      */                 
/* 5260 */                 value = Integer.parseInt(val);
/* 5261 */                 if ((value < 0 && player.getPower() == 0) || value >= 0) {
/*      */                   
/* 5263 */                   boolean perma = false;
/* 5264 */                   key = "np";
/* 5265 */                   val = props.getProperty(key);
/* 5266 */                   if (Boolean.parseBoolean(val))
/*      */                   {
/* 5268 */                     perma = true;
/*      */                   }
/* 5270 */                   touched = player.getWurmId();
/* 5271 */                   setReputation(responder, value, player.getName(), player.getWurmId(), perma, village);
/*      */                 } else {
/*      */                   
/* 5274 */                   responder.getCommunicator().sendNormalServerMessage("You cannot modify the reputation for " + player
/* 5275 */                       .getName() + " below 0, since " + player
/* 5276 */                       .getHeSheItString() + " is a GM.");
/*      */                 } 
/* 5278 */               } catch (NumberFormatException nfe) {
/*      */                 
/* 5280 */                 responder.getCommunicator().sendNormalServerMessage("Failed to set the reputation for " + player
/* 5281 */                     .getName() + ". Bad value.");
/*      */               }
/*      */             
/*      */             }
/* 5285 */           } catch (NoSuchPlayerException nsp) {
/*      */             
/* 5287 */             String name = val;
/* 5288 */             PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/* 5289 */             if (pinf != null && pinf.wurmId > 0L) {
/*      */               
/* 5291 */               key = "nr";
/* 5292 */               val = props.getProperty(key);
/* 5293 */               if (val != null && val.length() > 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 5297 */                   value = Integer.parseInt(val);
/* 5298 */                   if ((value < 0 && pinf.getPower() == 0) || value > 0)
/*      */                   {
/* 5300 */                     boolean perma = false;
/* 5301 */                     key = "np";
/* 5302 */                     val = props.getProperty(key);
/* 5303 */                     if (Boolean.parseBoolean(val))
/*      */                     {
/* 5305 */                       perma = true;
/*      */                     }
/* 5307 */                     touched = pinf.wurmId;
/* 5308 */                     setReputation(responder, value, pinf.getName(), pinf.wurmId, perma, village);
/*      */                   }
/*      */                   else
/*      */                   {
/* 5312 */                     responder.getCommunicator().sendNormalServerMessage("Make sure " + name + " is a regular player.");
/*      */                   }
/*      */                 
/* 5315 */                 } catch (NumberFormatException nfe) {
/*      */                   
/* 5317 */                   responder.getCommunicator().sendNormalServerMessage("Failed to set the reputation for " + name + ". Bad value.");
/*      */                 }
/*      */               
/*      */               }
/*      */             } else {
/*      */               
/* 5323 */               responder.getCommunicator().sendNormalServerMessage("Failed to locate player with name " + name + ". Make sure he/she has an account.");
/*      */             } 
/*      */           } 
/*      */         }
/*      */         
/* 5328 */         for (int x = 0; x < reputations.length; x++) {
/*      */           
/* 5330 */           value = 0;
/*      */           
/* 5332 */           long id = reputations[x].getWurmId();
/* 5333 */           Integer bbid = itemMap.get(new Long(id));
/*      */           
/* 5335 */           if (bbid != null) {
/*      */             
/*      */             try {
/*      */               
/* 5339 */               Player player = Players.getInstance().getPlayer(id);
/* 5340 */               key = bbid + "r";
/* 5341 */               val = props.getProperty(key);
/* 5342 */               if (val != null && val.length() > 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 5346 */                   value = Integer.parseInt(val);
/* 5347 */                   if (player.getPower() == 0 || (value >= 0 && player.getPower() > 0)) {
/*      */                     
/* 5349 */                     boolean perma = false;
/* 5350 */                     key = bbid + "p";
/* 5351 */                     val = props.getProperty(key);
/* 5352 */                     if (Boolean.parseBoolean(val))
/*      */                     {
/* 5354 */                       perma = true;
/*      */                     }
/* 5356 */                     if (player.getWurmId() != touched) {
/* 5357 */                       setReputation(responder, value, player.getName(), player.getWurmId(), perma, village);
/*      */                     }
/*      */                   } else {
/*      */                     
/* 5361 */                     responder.getCommunicator().sendNormalServerMessage("You cannot modify the reputation for " + player
/* 5362 */                         .getName() + " below 0, since " + player
/* 5363 */                         .getHeSheItString() + " is a GM.");
/*      */                   } 
/* 5365 */                 } catch (NumberFormatException nfe) {
/*      */                   
/* 5367 */                   responder.getCommunicator().sendNormalServerMessage("Failed to set the reputation for " + player
/* 5368 */                       .getName() + ". Bad value.");
/*      */                 }
/*      */               
/*      */               }
/* 5372 */             } catch (NoSuchPlayerException nsp) {
/*      */               
/* 5374 */               key = bbid + "r";
/* 5375 */               val = props.getProperty(key);
/* 5376 */               if (val != null && val.length() > 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 5380 */                   String bname = Players.getInstance().getNameFor(id);
/* 5381 */                   PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(bname);
/* 5382 */                   pinf.load();
/*      */                   
/* 5384 */                   value = Integer.parseInt(val);
/* 5385 */                   if (pinf.getPower() == 0 || (value > 0 && pinf.getPower() > 0)) {
/*      */                     
/* 5387 */                     boolean perma = false;
/* 5388 */                     key = bbid + "p";
/* 5389 */                     val = props.getProperty(key);
/* 5390 */                     if (Boolean.parseBoolean(val))
/*      */                     {
/* 5392 */                       perma = true;
/*      */                     }
/* 5394 */                     if (pinf.wurmId != touched) {
/* 5395 */                       setReputation(responder, value, pinf.getName(), pinf.wurmId, perma, village);
/*      */                     }
/*      */                   } 
/* 5398 */                 } catch (NumberFormatException nfe) {
/*      */                   
/* 5400 */                   responder.getCommunicator().sendNormalServerMessage("Failed to set the reputation for a player. Bad value.");
/*      */                 
/*      */                 }
/* 5403 */                 catch (IOException iox) {
/*      */                   
/* 5405 */                   logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */                 
/*      */                 }
/* 5408 */                 catch (NoSuchPlayerException nsp2) {
/*      */                   
/* 5410 */                   logger.log(Level.WARNING, nsp2.getMessage(), (Throwable)nsp2);
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/* 5417 */       responder.getCommunicator().sendNormalServerMessage("The reputations are updated.");
/*      */     }
/* 5419 */     catch (NoSuchItemException nsi) {
/*      */       
/* 5421 */       responder.getCommunicator().sendNormalServerMessage("No such item.");
/* 5422 */       logger.log(Level.WARNING, responder.getName(), (Throwable)nsi);
/*      */     }
/* 5424 */     catch (NoSuchVillageException nsp) {
/*      */       
/* 5426 */       responder.getCommunicator().sendNormalServerMessage("No such village.");
/* 5427 */       logger.log(Level.WARNING, responder.getName(), (Throwable)nsp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseSetDeityQuestion(SetDeityQuestion question) {
/* 5437 */     Properties props = question.getAnswer();
/* 5438 */     int type = question.getType();
/* 5439 */     Creature responder = question.getResponder();
/* 5440 */     long target = question.getTarget();
/* 5441 */     if (type == 0) {
/*      */       
/* 5443 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 5446 */     if (type == 26) {
/*      */       
/* 5448 */       String wid = props.getProperty("wurmid");
/* 5449 */       String did = props.getProperty("deityid");
/* 5450 */       String fai = props.getProperty("faith");
/* 5451 */       String dec = props.getProperty("faithdec");
/* 5452 */       String fav = props.getProperty("favor");
/*      */       
/*      */       try {
/* 5455 */         Item targ = Items.getItem(target);
/* 5456 */         if (targ.getTemplateId() == 176 && WurmPermissions.maySetFaith(responder)) {
/*      */           
/* 5458 */           int listid = Integer.parseInt(wid);
/* 5459 */           int deitynum = Integer.parseInt(did);
/* 5460 */           float faith = (float)Double.parseDouble(fai + "." + dec);
/* 5461 */           int favor = Integer.parseInt(fav);
/* 5462 */           Player player = question.getPlayer(listid);
/* 5463 */           if (player == null) {
/*      */             
/* 5465 */             responder.getCommunicator().sendNormalServerMessage("No such player!");
/*      */             return;
/*      */           } 
/* 5468 */           int deityid = question.getDeityNumberFromArrayPos(deitynum);
/* 5469 */           Deity deity = Deities.getDeity(deityid);
/* 5470 */           if (deity == null) {
/*      */             
/* 5472 */             responder.getCommunicator().sendNormalServerMessage("No such deity!");
/*      */             
/*      */             try {
/* 5475 */               player.setDeity(deity);
/* 5476 */               player.setPriest(false);
/*      */             }
/* 5478 */             catch (IOException iox) {
/*      */               
/* 5480 */               responder.getCommunicator().sendNormalServerMessage("Failed to clear deity! " + iox.getMessage());
/* 5481 */               logger.log(Level.WARNING, responder.getName() + " failed to clear deity " + iox.getMessage(), iox);
/*      */             } 
/*      */           } else {
/*      */ 
/*      */             
/*      */             try {
/*      */               
/* 5488 */               player.setDeity(deity);
/* 5489 */               player.setPriest((faith > 30.0F));
/* 5490 */               if (faith > 30.0F)
/* 5491 */                 PlayerJournal.sendTierUnlock(player, (JournalTier)PlayerJournal.getAllTiers().get(Byte.valueOf((byte)10))); 
/* 5492 */               player.setFaith(faith);
/* 5493 */               player.setFavor(favor);
/* 5494 */               responder.getCommunicator().sendNormalServerMessage(player
/* 5495 */                   .getName() + " now has deity " + deity.name + ", faith " + faith + ", and favour: " + favor + ".");
/*      */               
/* 5497 */               player.getCommunicator().sendNormalServerMessage("You are now a follower of " + deity.name + ".", (byte)2);
/* 5498 */               responder.getLogger().info(player
/* 5499 */                   .getName() + " now has deity " + deity.name + ", faith " + faith + ", and favour: " + favor + ".");
/*      */               
/* 5501 */               if (deity.number == 4) {
/*      */                 
/* 5503 */                 if (Servers.isThisAPvpServer()) {
/*      */                   
/* 5505 */                   player.setKingdomId((byte)3);
/* 5506 */                   responder.getCommunicator().sendNormalServerMessage(player
/* 5507 */                       .getName() + " now is with the " + "Horde of the Summoned" + ".");
/* 5508 */                   player.getCommunicator().sendNormalServerMessage("You are now with the Horde of the Summoned.");
/*      */                 } 
/*      */                 
/* 5511 */                 player.setAlignment(Math.min(-50.0F, player.getAlignment()));
/*      */               }
/* 5513 */               else if (player.getAlignment() < 0.0F) {
/*      */                 
/* 5515 */                 if (player.getKingdomId() == 3)
/*      */                 {
/* 5517 */                   if (player.getCurrentTile().getKingdom() != 0) {
/* 5518 */                     player.setKingdomId(player.getCurrentTile().getKingdom());
/* 5519 */                   } else if (responder.getKingdomId() != 3) {
/* 5520 */                     player.setKingdomId(responder.getKingdomId());
/*      */                   } else {
/* 5522 */                     player.setKingdomId((byte)4);
/*      */                   }  } 
/* 5524 */                 player.setAlignment(50.0F);
/*      */               } 
/* 5526 */               if (player.isChampion())
/*      */               {
/* 5528 */                 Server.getInstance().broadCastAlert(player
/* 5529 */                     .getName() + " now is a Champion of " + deity.name + ".", true, (byte)2);
/* 5530 */                 responder.getLogger().log(Level.WARNING, responder
/*      */                     
/* 5532 */                     .getName() + " set the deity of real death player " + player.getName() + " to " + deity.name + ".");
/*      */               }
/*      */             
/*      */             }
/* 5536 */             catch (IOException iox) {
/*      */               
/* 5538 */               responder.getCommunicator().sendNormalServerMessage("Failed to set deity! " + iox.getMessage());
/* 5539 */               logger.log(Level.WARNING, responder.getName() + " failed to set deity " + iox.getMessage(), iox);
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/* 5544 */           logger.log(Level.WARNING, responder.getName() + " item used to answer is not a wand! " + wid + ", " + did + "," + fai);
/*      */         }
/*      */       
/* 5547 */       } catch (NumberFormatException nf) {
/*      */         
/* 5549 */         logger.log(Level.WARNING, responder.getName() + ":" + nf.getMessage() + ": " + wid + ", " + did + "," + fai + "," + fav);
/*      */         
/* 5551 */         responder.getCommunicator().sendNormalServerMessage("The values " + wid + ", " + did + "," + fai + "," + fav + " are improper.");
/*      */       
/*      */       }
/* 5554 */       catch (NoSuchItemException nsi) {
/*      */         
/* 5556 */         logger.log(Level.WARNING, responder.getName() + " tried to use wand but it didn't exist! " + wid + ", " + did + "," + fai);
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
/*      */   static void parseSetKingdomQuestion(SetKingdomQuestion question) {
/* 5569 */     Properties props = question.getAnswer();
/* 5570 */     int type = question.getType();
/* 5571 */     Creature responder = question.getResponder();
/* 5572 */     long target = question.getTarget();
/* 5573 */     if (type == 0) {
/*      */       
/* 5575 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 5578 */     if (type == 37)
/*      */     {
/* 5580 */       if (question.getResponder().getPower() <= 0) {
/*      */         
/* 5582 */         String key = "kingd";
/* 5583 */         String val = question.getAnswer().getProperty("kingd");
/* 5584 */         if (Boolean.parseBoolean(val)) {
/*      */           
/*      */           try
/*      */           {
/* 5588 */             byte previousKingdom = responder.getKingdomId();
/* 5589 */             byte targetKingdom = responder.getKingdomTemplateId();
/* 5590 */             if (Servers.isThisAChaosServer() && targetKingdom != 3)
/*      */             {
/* 5592 */               targetKingdom = 4;
/*      */             }
/* 5594 */             if (responder.setKingdomId(targetKingdom, false, false))
/*      */             {
/* 5596 */               logger.info(responder.getName() + " has just decided to leave " + 
/* 5597 */                   Kingdoms.getNameFor(previousKingdom) + " and joins " + 
/* 5598 */                   Kingdoms.getNameFor(targetKingdom));
/* 5599 */               responder.getCommunicator().sendNormalServerMessage("You decide to leave " + 
/* 5600 */                   Kingdoms.getNameFor(previousKingdom) + " and join " + 
/* 5601 */                   Kingdoms.getNameFor(targetKingdom) + ". Congratulations!");
/*      */               
/* 5603 */               Server.getInstance().broadCastAction(responder
/* 5604 */                   .getName() + " leaves " + 
/* 5605 */                   Kingdoms.getNameFor(previousKingdom) + "!", responder, 5);
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 5610 */           catch (IOException iox)
/*      */           {
/* 5612 */             logger.log(Level.WARNING, responder.getName() + ":" + iox.getMessage() + ": " + question
/* 5613 */                 .getResponder().getName(), iox);
/* 5614 */             responder
/* 5615 */               .getCommunicator()
/* 5616 */               .sendNormalServerMessage("The moons are not properly aligned right now. You will have to wait (there is a server error).");
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 5621 */           responder.getCommunicator().sendNormalServerMessage("You decide not to leave " + 
/* 5622 */               Kingdoms.getNameFor(question.getResponder().getKingdomId()) + " for now.");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 5627 */         String wid = props.getProperty("wurmid");
/* 5628 */         String did = props.getProperty("kingdomid");
/*      */ 
/*      */         
/*      */         try {
/* 5632 */           int listid = Integer.parseInt(wid);
/* 5633 */           int index = Integer.parseInt(did);
/* 5634 */           Item targ = Items.getItem(target);
/* 5635 */           if ((targ.getTemplateId() == 176 || targ.getTemplateId() == 315) && responder
/* 5636 */             .getPower() >= 2) {
/*      */             
/* 5638 */             Kingdom k = question.getAvailKingdoms().get(index - 1);
/* 5639 */             byte kingdomid = (k == null) ? 0 : k.getId();
/* 5640 */             Player player = question.getPlayer(listid);
/* 5641 */             if (player == null) {
/*      */               
/* 5643 */               responder.getCommunicator().sendNormalServerMessage("No such player!");
/*      */               return;
/*      */             } 
/* 5646 */             String kname = Kingdoms.getNameFor(kingdomid);
/* 5647 */             if (kname.equals("no known kingdom")) {
/* 5648 */               responder.getCommunicator().sendNormalServerMessage("Not setting to no kingdom at the moment!");
/*      */             } else {
/*      */               
/* 5651 */               if (player.isChampion()) {
/*      */                 
/* 5653 */                 responder.getCommunicator().sendNormalServerMessage(player
/* 5654 */                     .getName() + " has real death and may not change kingdom.");
/*      */                 
/*      */                 return;
/*      */               } 
/*      */               try {
/* 5659 */                 if (player.setKingdomId(kingdomid)) {
/*      */                   
/* 5661 */                   responder.getCommunicator().sendNormalServerMessage(player
/* 5662 */                       .getName() + " now is part of " + kname + ".");
/* 5663 */                   player.getCommunicator().sendNormalServerMessage("You are now a part of " + kname + ".");
/* 5664 */                   responder.getLogger().log(Level.INFO, "Set kingdom of " + player
/* 5665 */                       .getName() + " to " + kname + ".");
/* 5666 */                   player.getCommunicator().sendUpdateKingdomId();
/*      */                 } else {
/*      */                   
/* 5669 */                   responder.getLogger().log(Level.INFO, "Tried to set kingdom of " + player
/*      */                       
/* 5671 */                       .getName() + " to " + kname + " but it was not allowed.");
/*      */                 }
/*      */               
/* 5674 */               } catch (IOException iox) {
/*      */                 
/* 5676 */                 responder.getCommunicator().sendNormalServerMessage("Failed to set kingdom! " + iox
/* 5677 */                     .getMessage());
/* 5678 */                 logger.log(Level.WARNING, responder.getName() + "failed to set kingdom " + iox.getMessage(), iox);
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 5684 */             logger.log(Level.WARNING, responder.getName() + " item used to answer is not a wand! " + wid + ", " + did);
/*      */           }
/*      */         
/* 5687 */         } catch (NumberFormatException nf) {
/*      */           
/* 5689 */           logger.log(Level.WARNING, responder.getName() + ":" + nf.getMessage() + ": " + wid + ", " + did);
/* 5690 */           responder.getCommunicator().sendNormalServerMessage("The values " + wid + ", " + did + " are improper.");
/*      */         }
/* 5692 */         catch (NoSuchItemException nsi) {
/*      */           
/* 5694 */           logger.log(Level.WARNING, responder.getName() + " tried to use wand but it didn't exist! " + wid + ", " + did);
/*      */         } 
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
/*      */   static void parseAskKingdomQuestion(AskKingdomQuestion question) {
/* 5707 */     Creature responder = question.getResponder();
/* 5708 */     long target = question.target;
/*      */ 
/*      */     
/*      */     try {
/* 5712 */       Creature asker = Server.getInstance().getCreature(target);
/*      */       
/* 5714 */       if (asker.getKingdomId() != responder.getKingdomId()) {
/*      */         
/* 5716 */         if (responder instanceof Player) {
/*      */           
/* 5718 */           if (responder
/* 5719 */             .isWithinTileDistanceTo(asker.getTileX(), asker.getTileY(), 
/* 5720 */               (int)(asker.getPositionZ() + asker.getAltOffZ()) >> 2, 4)) {
/*      */             
/* 5722 */             String key = "conv";
/* 5723 */             String val = question.getAnswer().getProperty("conv");
/* 5724 */             if (Boolean.parseBoolean(val)) {
/*      */               
/* 5726 */               boolean forceToCustom = false;
/* 5727 */               if (!Servers.localServer.HOMESERVER && 
/* 5728 */                 Kingdoms.getKingdom(asker.getKingdomId()).isCustomKingdom() && responder
/* 5729 */                 .getCitizenVillage() != null)
/*      */               {
/* 5731 */                 if ((responder.getCitizenVillage().getMayor()).wurmId == responder.getWurmId())
/* 5732 */                   forceToCustom = true; 
/*      */               }
/* 5734 */               if (responder.mayChangeKingdom(null) || forceToCustom) {
/*      */ 
/*      */                 
/*      */                 try {
/* 5738 */                   if (responder.setKingdomId(asker.getKingdomId(), forceToCustom)) {
/*      */                     
/* 5740 */                     responder.getCommunicator().sendNormalServerMessage("You have now joined " + 
/* 5741 */                         Kingdoms.getNameFor(responder.getKingdomId()) + "!");
/* 5742 */                     asker.getCommunicator().sendNormalServerMessage(responder
/* 5743 */                         .getName() + " has now joined " + 
/* 5744 */                         Kingdoms.getNameFor(responder.getKingdomId()) + "!");
/* 5745 */                     if (Kingdoms.getKingdom(asker.getKingdomId()).isCustomKingdom()) {
/*      */ 
/*      */ 
/*      */                       
/* 5749 */                       String toSend = "<" + asker.getName() + "> convinced " + responder.getName() + " to join " + Kingdoms.getNameFor(responder.getKingdomId()) + ".";
/*      */                       
/* 5751 */                       Message mess = new Message(asker, (byte)10, "GL-" + Kingdoms.getChatNameFor(asker.getKingdomId()), toSend);
/* 5752 */                       Server.getInstance().addMessage(mess);
/*      */                       
/* 5754 */                       WcKingdomChat wc = new WcKingdomChat(WurmId.getNextWCCommandId(), asker.getWurmId(), asker.getName(), toSend, false, asker.getKingdomId(), -1, -1, -1);
/*      */                       
/* 5756 */                       if (Servers.localServer.LOGINSERVER) {
/* 5757 */                         wc.sendFromLoginServer();
/*      */                       } else {
/* 5759 */                         wc.sendToLoginServer();
/*      */                       } 
/*      */                     } 
/*      */                   } 
/* 5763 */                 } catch (IOException iox) {
/*      */                   
/* 5765 */                   logger.log(Level.WARNING, responder.getName() + ": " + iox.getMessage(), iox);
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 5770 */                 responder
/* 5771 */                   .getCommunicator()
/* 5772 */                   .sendNormalServerMessage("You may not change kingdom too frequently. Also, mayors of a settlement may not change kingdom. You may not join a custom kingdom on a home server.");
/*      */                 
/* 5774 */                 asker.getCommunicator().sendNormalServerMessage(responder
/* 5775 */                     .getName() + " may not change kingdom right now.");
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 5780 */               responder.getCommunicator().sendNormalServerMessage("You decide not to join " + 
/* 5781 */                   Kingdoms.getNameFor(asker.getKingdomId()) + ".");
/* 5782 */               asker.getCommunicator().sendNormalServerMessage(responder
/* 5783 */                   .getName() + " decides not to join " + Kingdoms.getNameFor(asker.getKingdomId()) + ".");
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 5788 */             asker.getCommunicator().sendNormalServerMessage(responder.getName() + " is too far away now.");
/*      */           } 
/*      */         } else {
/* 5791 */           asker.getCommunicator().sendNormalServerMessage("Only players may change kingdom.");
/*      */         } 
/*      */       } else {
/* 5794 */         asker.getCommunicator().sendNormalServerMessage(responder
/* 5795 */             .getName() + " is already in " + Kingdoms.getNameFor(responder.getKingdomId()) + ".");
/*      */       } 
/* 5797 */     } catch (NoSuchCreatureException nsc) {
/*      */       
/* 5799 */       responder.getCommunicator().sendNormalServerMessage("The asker is not around any longer.");
/*      */     }
/* 5801 */     catch (NoSuchPlayerException nsp) {
/*      */       
/* 5803 */       responder.getCommunicator().sendNormalServerMessage("The asker is not around any longer.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void parseAskConvertQuestion(AskConvertQuestion question) {
/* 5813 */     Creature responder = question.getResponder();
/* 5814 */     Item holyItem = question.getHolyItem();
/* 5815 */     long target = question.getTarget();
/*      */ 
/*      */     
/*      */     try {
/* 5819 */       Creature asker = Server.getInstance().getCreature(target);
/* 5820 */       Deity deity = asker.getDeity();
/* 5821 */       if (deity == null) {
/*      */         
/* 5823 */         asker.getCommunicator().sendNormalServerMessage("You have no deity.");
/*      */         return;
/*      */       } 
/* 5826 */       if (!responder.isPlayer()) {
/*      */         
/* 5828 */         asker.getCommunicator().sendNormalServerMessage("You may only convert other players.");
/*      */         return;
/*      */       } 
/* 5831 */       if (!responder.isWithinTileDistanceTo(asker.getTileX(), asker.getTileY(), (int)(asker.getPositionZ() + asker.getAltOffZ()) >> 2, 4)) {
/*      */         
/* 5833 */         asker.getCommunicator().sendNormalServerMessage(responder.getName() + " is too far away now.");
/*      */         
/*      */         return;
/*      */       } 
/* 5837 */       String key = "conv";
/* 5838 */       String val = question.getAnswer().getProperty("conv");
/* 5839 */       if (!Boolean.parseBoolean(val)) {
/*      */         
/* 5841 */         responder.getCommunicator().sendNormalServerMessage("You decide not to listen.");
/* 5842 */         asker.getCommunicator().sendNormalServerMessage(responder.getName() + " decides not to listen.");
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*      */       try {
/* 5848 */         asker.getCurrentAction();
/* 5849 */         responder.getCommunicator().sendNormalServerMessage(asker.getName() + " is too busy to preach right now.");
/* 5850 */         asker.getCommunicator().sendNormalServerMessage(responder.getName() + " wants to listen to your preachings but you are too busy.");
/*      */         
/*      */         return;
/* 5853 */       } catch (NoSuchActionException nsa) {
/*      */         
/*      */         try
/*      */         {
/* 5857 */           BehaviourDispatcher.action(asker, asker.getCommunicator(), holyItem.getWurmId(), responder
/* 5858 */               .getWurmId(), (short)216);
/*      */         }
/* 5860 */         catch (FailedException failedException)
/*      */         {
/*      */         
/*      */         }
/* 5864 */         catch (NoSuchBehaviourException|NoSuchItemException|NoSuchPlayerException|com.wurmonline.server.structures.NoSuchWallException|NoSuchCreatureException nsb)
/*      */         {
/*      */           
/* 5867 */           logger.log(Level.WARNING, nsb.getMessage(), (Throwable)nsb);
/*      */         }
/*      */       
/*      */       } 
/* 5871 */     } catch (NoSuchCreatureException|NoSuchPlayerException nsc) {
/*      */       
/* 5873 */       responder.getCommunicator().sendNormalServerMessage("The preacher is not around any longer.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parseConvertQuestion(ConvertQuestion question) {
/* 5883 */     Creature responder = question.getResponder();
/* 5884 */     Item holyItem = question.getHolyItem();
/* 5885 */     long target = question.getTarget();
/*      */     
/*      */     try {
/* 5888 */       Creature asker = Server.getInstance().getCreature(target);
/* 5889 */       Deity deity = asker.getDeity();
/*      */       
/* 5891 */       if (deity == null) {
/*      */         
/* 5893 */         asker.getCommunicator().sendNormalServerMessage("You have no deity.");
/*      */         return;
/*      */       } 
/* 5896 */       if (!responder.isWithinTileDistanceTo(asker.getTileX(), asker.getTileY(), (int)(asker.getPositionZ() + asker.getAltOffZ()) >> 2, 4)) {
/*      */         
/* 5898 */         asker.getCommunicator().sendNormalServerMessage(responder.getName() + " is too far away now.");
/*      */         
/*      */         return;
/*      */       } 
/* 5902 */       String key = "conv";
/* 5903 */       String val = question.getAnswer().getProperty("conv");
/* 5904 */       if (!Boolean.parseBoolean(val)) {
/*      */         
/* 5906 */         responder.getCommunicator().sendNormalServerMessage("You decide not to convert.");
/* 5907 */         asker.getCommunicator().sendNormalServerMessage(responder.getName() + " decides not to convert.");
/*      */         
/*      */         return;
/*      */       } 
/* 5911 */       if (canConvertToDeity(responder, deity)) {
/*      */         
/* 5913 */         if (!doesKingdomTemplateAcceptDeity(responder.getKingdomTemplateId(), deity)) {
/*      */           
/* 5915 */           responder.getCommunicator().sendNormalServerMessage("Following that deity would expel you from " + responder.getKingdomName() + ".");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*      */         try {
/* 5921 */           Skill preaching = asker.getSkills().getSkillOrLearn(10065);
/* 5922 */           preaching.skillCheck(preaching.getKnowledge(0.0D) - 10.0D, holyItem, asker.zoneBonus, false, question.getSkillcounter());
/*      */           
/* 5924 */           responder.setChangedDeity();
/* 5925 */           responder.setDeity(deity);
/* 5926 */           responder.setFaith((float)preaching.getKnowledge(0.0D) / 5.0F);
/*      */ 
/*      */           
/* 5929 */           Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/* 5930 */           templateDeity.increaseFavor();
/* 5931 */           if (deity.isHateGod()) {
/*      */             
/* 5933 */             asker.maybeModifyAlignment(-1.0F);
/* 5934 */             responder.setAlignment(Math.min(-1.0F, responder.getAlignment()));
/*      */           }
/*      */           else {
/*      */             
/* 5938 */             asker.maybeModifyAlignment(1.0F);
/* 5939 */             responder.setAlignment(Math.max(1.0F, responder.getAlignment()));
/*      */           } 
/* 5941 */           asker.setFavor(Math.max(asker.getFavor(), asker.getFaith() / 2.0F));
/* 5942 */           responder.setFavor(1.0F);
/*      */           
/* 5944 */           responder.getCommunicator().sendNormalServerMessage("You have now converted to " + deity.name + "!");
/* 5945 */           asker.getCommunicator().sendNormalServerMessage(responder.getName() + " has now converted to " + deity.name + "!");
/*      */           
/* 5947 */           asker.achievement(621);
/*      */         }
/* 5949 */         catch (IOException iox) {
/*      */           
/* 5951 */           responder.getCommunicator().sendNormalServerMessage("You failed to convert to " + deity.name + " due to a server error! Please report this to the GM's.");
/*      */           
/* 5953 */           asker.getCommunicator().sendNormalServerMessage(responder.getName() + " failed to convert to " + deity.name + " due to a server error! Please report this to the GM's.");
/*      */           
/* 5955 */           logger.log(Level.WARNING, responder.getName() + ":" + iox.getMessage(), iox);
/*      */         }
/*      */       
/*      */       } 
/* 5959 */     } catch (NoSuchCreatureException|NoSuchPlayerException nsc) {
/*      */       
/* 5961 */       responder.getCommunicator().sendNormalServerMessage("The preacher is not around any longer.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean canConvertToDeity(Creature responder, Deity deity) {
/* 5967 */     if (responder == null || !responder.isPlayer()) {
/* 5968 */       return false;
/*      */     }
/* 5970 */     if (deity == null) {
/*      */       
/* 5972 */       responder.getCommunicator().sendNormalServerMessage("That deity is not available to convert to.");
/* 5973 */       return false;
/*      */     } 
/*      */     
/* 5976 */     if (!responder.mayChangeDeity(deity.getNumber())) {
/*      */       
/* 5978 */       responder.getCommunicator().sendNormalServerMessage("Your faith cannot change so frequently. You will have to wait.");
/* 5979 */       return false;
/*      */     } 
/*      */     
/* 5982 */     if (responder.getDeity() == deity) {
/*      */       
/* 5984 */       responder.getCommunicator().sendNormalServerMessage("You already follow the teachings of " + deity.getName() + ".");
/* 5985 */       return false;
/*      */     } 
/*      */     
/* 5988 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean doesKingdomTemplateAcceptDeity(byte kingdomTemplate, Deity deity) {
/* 5994 */     if (kingdomTemplate == 3) {
/*      */       
/* 5996 */       if (deity.isFo() || deity.isMagranon() || deity.isVynora()) {
/* 5997 */         return false;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 6002 */     else if (deity.isLibila()) {
/* 6003 */       return (kingdomTemplate == 4 && !Servers.localServer.PVPSERVER);
/*      */     } 
/*      */     
/* 6006 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void parseAltarConvertQuestion(AltarConversionQuestion question) {
/* 6015 */     Creature responder = question.getResponder();
/* 6016 */     Deity deity = question.getDeity();
/* 6017 */     long target = question.getTarget();
/*      */ 
/*      */     
/*      */     try {
/* 6021 */       Item altar = Items.getItem(target);
/*      */       
/* 6023 */       if (deity == null) {
/*      */         
/* 6025 */         responder.getCommunicator().sendNormalServerMessage("The altar has no deity anymore.");
/*      */         return;
/*      */       } 
/* 6028 */       if (!responder.isWithinTileDistanceTo(altar.getTileX(), altar.getTileY(), (int)altar.getPosZ() >> 2, 4)) {
/*      */         
/* 6030 */         responder.getCommunicator().sendNormalServerMessage("The " + altar.getName() + " is too far away now.");
/*      */         
/*      */         return;
/*      */       } 
/* 6034 */       String key = "conv";
/* 6035 */       String val = question.getAnswer().getProperty(key);
/* 6036 */       if (!Boolean.parseBoolean(val)) {
/*      */         
/* 6038 */         responder.getCommunicator().sendNormalServerMessage("You decide not to convert.");
/*      */         
/*      */         return;
/*      */       } 
/* 6042 */       if (canConvertToDeity(responder, deity)) {
/*      */         try
/*      */         {
/*      */           
/* 6046 */           if (!doesKingdomTemplateAcceptDeity(responder.getKingdomTemplateId(), deity)) {
/*      */             
/* 6048 */             responder.getCommunicator().sendNormalServerMessage("Following that deity would expel you from " + responder.getKingdomName() + ".");
/*      */             
/*      */             return;
/*      */           } 
/* 6052 */           responder.setChangedDeity();
/* 6053 */           responder.setDeity(deity);
/* 6054 */           responder.setFaith(1.0F);
/* 6055 */           responder.setFavor(1.0F);
/* 6056 */           if (deity.isHateGod()) {
/* 6057 */             responder.setAlignment(Math.min(-1.0F, responder.getAlignment()));
/*      */           } else {
/* 6059 */             responder.setAlignment(Math.max(1.0F, responder.getAlignment()));
/* 6060 */           }  responder.getCommunicator().sendNormalServerMessage("You have now converted to " + deity.name + "!");
/*      */         }
/* 6062 */         catch (IOException iox)
/*      */         {
/* 6064 */           responder.getCommunicator().sendNormalServerMessage("You failed to convert to " + deity.name + " due to a server error! Please report this to the GM's.");
/*      */           
/* 6066 */           logger.log(Level.WARNING, responder.getName() + ":" + iox.getMessage(), iox);
/*      */         }
/*      */       
/*      */       }
/* 6070 */     } catch (NoSuchItemException nsc) {
/*      */       
/* 6072 */       responder.getCommunicator().sendNormalServerMessage("The altar is not around any longer.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void parseAscensionQuestion(AscensionQuestion question) {
/* 6078 */     Creature responder = question.getResponder();
/* 6079 */     if (responder.isPlayer()) {
/*      */       
/* 6081 */       String key = "demig";
/* 6082 */       String val = question.getAnswer().getProperty("demig");
/* 6083 */       if (!Boolean.parseBoolean(val)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 6113 */         responder.getCommunicator().sendNormalServerMessage("You decide to remain mortal for now. Maybe the chance returns, who knows?");
/*      */         
/* 6115 */         logger.log(Level.INFO, responder.getName() + " declined ascension to demigod!");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parsePriestQuestion(PriestQuestion question) {
/* 6126 */     Creature responder = question.getResponder();
/* 6127 */     long target = question.getTarget();
/* 6128 */     Deity deity = responder.getDeity();
/* 6129 */     if (WurmId.getType(target) == 2) {
/*      */       
/*      */       try {
/* 6132 */         Item altar = Items.getItem(target);
/* 6133 */         if (altar.isHugeAltar()) {
/*      */           
/* 6135 */           if (!responder.isWithinTileDistanceTo((int)altar.getPosX() >> 2, (int)altar.getPosY() >> 2, 
/* 6136 */               (int)altar.getPosZ() >> 2, 4)) {
/*      */             
/* 6138 */             responder.getCommunicator().sendNormalServerMessage("You must be close to the huge altar in order to become a priest.");
/*      */ 
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } else {
/* 6145 */           responder.getCommunicator().sendNormalServerMessage("You must be close to the huge altar in order to become a priest.");
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/* 6150 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 6152 */         responder.getCommunicator().sendNormalServerMessage("You can not become a priest right now.");
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } else {
/* 6158 */       if (deity == null) {
/*      */         
/* 6160 */         responder.getCommunicator().sendNormalServerMessage("You are not even a follower of a faith!");
/*      */         
/*      */         return;
/*      */       } 
/*      */       try {
/* 6165 */         Creature creature = Server.getInstance().getCreature(question.target);
/* 6166 */         if (!responder.isWithinTileDistanceTo((int)creature.getPosX() >> 2, (int)creature.getPosY() >> 2, 
/* 6167 */             (int)(creature.getPositionZ() + creature.getAltOffZ()) >> 2, 4)) {
/*      */           
/* 6169 */           responder.getCommunicator().sendNormalServerMessage("You must be closer to the person who asked you.");
/*      */           return;
/*      */         } 
/* 6172 */         if (deity != creature.getDeity()) {
/*      */           
/* 6174 */           responder.getCommunicator().sendNormalServerMessage("You must be of the same faith as " + creature
/* 6175 */               .getName() + ".");
/*      */           
/*      */           return;
/*      */         } 
/* 6179 */       } catch (NoSuchCreatureException nsc) {
/*      */         
/* 6181 */         responder.getCommunicator().sendNormalServerMessage("You must be close to the person who asked you.");
/*      */         
/*      */         return;
/* 6184 */       } catch (NoSuchPlayerException nsp) {
/*      */         
/* 6186 */         responder.getCommunicator().sendNormalServerMessage("You must be close to the person who asked you.");
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 6191 */     if (deity != null) {
/*      */       
/* 6193 */       if (!responder.isPriest()) {
/*      */         
/* 6195 */         if (responder.isPlayer()) {
/*      */           
/* 6197 */           String key = "priest";
/* 6198 */           String val = question.getAnswer().getProperty("priest");
/* 6199 */           if (Boolean.parseBoolean(val)) {
/*      */             
/* 6201 */             logger.info(responder.getName() + " has just become a priest of " + deity.name);
/* 6202 */             responder.getCommunicator().sendNormalServerMessage("You have become a priest of " + deity.name + ". Congratulations!");
/*      */             
/* 6204 */             Server.getInstance().broadCastAction(responder.getName() + " is now a priest of " + deity.name + "!", responder, 5);
/*      */ 
/*      */             
/* 6207 */             responder.setPriest(true);
/* 6208 */             PlayerJournal.sendTierUnlock((Player)responder, (JournalTier)PlayerJournal.getAllTiers().get(Byte.valueOf((byte)10)));
/*      */             
/*      */             try {
/* 6211 */               responder.setFavor(responder.getFaith());
/*      */             }
/* 6213 */             catch (IOException iox) {
/*      */               
/* 6215 */               logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */             } 
/*      */           } else {
/*      */             
/* 6219 */             responder.getCommunicator().sendNormalServerMessage("You decide not to become a priest for now.");
/*      */           } 
/*      */         } 
/*      */       } else {
/* 6223 */         responder.getCommunicator().sendNormalServerMessage("You are already a priest.");
/*      */       } 
/*      */     } else {
/* 6226 */       responder.getCommunicator().sendNormalServerMessage("You have no deity!");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void parseRealDeathQuestion(RealDeathQuestion question) {
/* 6235 */     Creature responder = question.getResponder();
/* 6236 */     Deity deity = responder.getDeity();
/* 6237 */     long target = question.getTarget();
/* 6238 */     if (!responder.isChampion()) {
/*      */ 
/*      */       
/*      */       try {
/* 6242 */         Item altar = Items.getItem(target);
/* 6243 */         if (altar.isHugeAltar()) {
/*      */           
/* 6245 */           if (deity != null) {
/*      */             
/* 6247 */             if (deity.accepts(responder.getAlignment())) {
/*      */               
/* 6249 */               if (responder instanceof Player) {
/*      */                 
/* 6251 */                 if (Players.getChampionsFromKingdom(responder.getKingdomId(), deity.getNumber()) < 1) {
/*      */                   
/* 6253 */                   if (Players.getChampionsFromKingdom(responder.getKingdomId()) < 3) {
/*      */                     
/* 6255 */                     if (responder.isWithinTileDistanceTo((int)altar.getPosX() >> 2, 
/* 6256 */                         (int)altar.getPosY() >> 2, (int)altar.getPosZ() >> 2, 4)) {
/*      */                       
/* 6258 */                       String key = "rd";
/* 6259 */                       String val = question.getAnswer().getProperty("rd");
/* 6260 */                       if (Boolean.parseBoolean(val))
/*      */                       {
/* 6262 */                         responder.becomeChamp();
/*      */                       }
/*      */                       else
/*      */                       {
/* 6266 */                         responder.getCommunicator().sendNormalServerMessage("You decide not to become a champion of " + deity.name + ".");
/*      */                       }
/*      */                     
/*      */                     } else {
/*      */                       
/* 6271 */                       responder.getCommunicator().sendNormalServerMessage(altar
/* 6272 */                           .getName() + " is too far away now.");
/*      */                     } 
/*      */                   } else {
/* 6275 */                     responder.getCommunicator().sendNormalServerMessage("Your kingdom does not support more champions right now.");
/*      */                   } 
/*      */                 } else {
/*      */                   
/* 6279 */                   responder.getCommunicator().sendNormalServerMessage(deity.name + " can not support another champion from your kingdom right now.");
/*      */                 } 
/*      */               } else {
/*      */                 
/* 6283 */                 responder.getCommunicator().sendNormalServerMessage("Only players may become champions.");
/*      */               } 
/*      */             } else {
/*      */               
/* 6287 */               responder.getCommunicator().sendNormalServerMessage(deity.name + " would not accept you as " + deity
/* 6288 */                   .getHisHerItsString() + " champion right now since you have strayn from the path.");
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 6294 */             responder.getCommunicator().sendNormalServerMessage("You no longer follow a deity.");
/*      */           } 
/*      */         } else {
/*      */           
/* 6298 */           responder.getCommunicator().sendNormalServerMessage("The altar is not of the right type.");
/*      */         } 
/* 6300 */       } catch (NoSuchItemException nsc) {
/*      */         
/* 6302 */         responder.getCommunicator().sendNormalServerMessage("The altar is not around any longer.");
/*      */       } 
/*      */     } else {
/*      */       
/* 6306 */       responder.getCommunicator().sendNormalServerMessage("You are already a champion of " + deity.name + ".");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void parseSpawnQuestion(SpawnQuestion question) {
/* 6316 */     boolean eserv = false;
/* 6317 */     if (Servers.localServer.KINGDOM != question.getResponder().getKingdomId()) {
/*      */       
/* 6319 */       String sps = question.getAnswer().getProperty("eserver");
/*      */       
/* 6321 */       if (sps != null) {
/*      */         
/* 6323 */         eserv = true;
/* 6324 */         Map<Integer, Integer> servers = question.getServerEntries();
/*      */         
/* 6326 */         int i = Integer.parseInt(sps);
/* 6327 */         if (i > 0) {
/*      */           
/* 6329 */           Integer serverNumber = servers.get(Integer.valueOf(i));
/* 6330 */           ServerEntry toGoTo = Servers.getServerWithId(serverNumber.intValue());
/*      */           
/* 6332 */           if (toGoTo.getKingdom() == question.getResponder().getKingdomId() || toGoTo
/* 6333 */             .getKingdom() == 0) {
/*      */             
/*      */             try {
/*      */               
/* 6337 */               Player player = Players.getInstance().getPlayer(question.getResponder().getWurmId());
/* 6338 */               player.sendTransfer(Server.getInstance(), toGoTo.EXTERNALIP, 
/* 6339 */                   Integer.parseInt(toGoTo.EXTERNALPORT), toGoTo.INTRASERVERPASSWORD, toGoTo
/* 6340 */                   .getId(), -1, -1, true, false, player.getKingdomId());
/*      */               
/*      */               return;
/* 6343 */             } catch (NoSuchPlayerException nsp) {
/*      */               
/* 6345 */               logger.log(Level.INFO, "Player " + question.getResponder().getWurmId() + " is no longer available.");
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 6353 */     String spq = question.getAnswer().getProperty("spawnpoint");
/* 6354 */     if (spq != null) {
/*      */       
/* 6356 */       int i = Integer.parseInt(spq);
/* 6357 */       Spawnpoint sp = question.getSpawnpoint(i);
/* 6358 */       if (sp == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 6364 */         (question.getResponder()).spawnArmour = question.getAnswer().getProperty("armour");
/* 6365 */         (question.getResponder()).spawnWeapon = question.getAnswer().getProperty("weapon");
/* 6366 */         Player p = Players.getInstance().getPlayer(question.getTarget());
/* 6367 */         p.spawn(sp.number);
/*      */       }
/* 6369 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 6371 */         logger.log(Level.WARNING, "Unknown player trying to spawn?", (Throwable)nsp);
/*      */       }
/*      */     
/* 6374 */     } else if (!eserv) {
/*      */       
/* 6376 */       question.getResponder().getCommunicator()
/* 6377 */         .sendNormalServerMessage("You can bring the spawn question back by typing /respawn in a chat window.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void parseWithdrawMoneyQuestion(WithdrawMoneyQuestion question) {
/* 6388 */     Creature responder = question.getResponder();
/* 6389 */     if (responder.isDead()) {
/*      */       
/* 6391 */       responder.getCommunicator().sendNormalServerMessage("You are dead, and may not withdraw any money.");
/*      */       
/*      */       return;
/*      */     } 
/* 6395 */     int type = question.getType();
/* 6396 */     if (type == 0) {
/*      */       
/* 6398 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 6401 */     if (type == 36) {
/*      */ 
/*      */       
/*      */       try {
/* 6405 */         Item token = Items.getItem(question.getTarget());
/* 6406 */         if (token.getTemplateId() == 236) {
/*      */           
/* 6408 */           if (!responder.isWithinDistanceTo(token.getPosX(), token.getPosY(), token.getPosZ(), 30.0F)) {
/*      */             
/* 6410 */             responder.getCommunicator().sendNormalServerMessage("You are too far away from the bank.");
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } else {
/* 6416 */           responder.getCommunicator().sendNormalServerMessage("The " + token
/* 6417 */               .getName() + " does not function as a bank.");
/*      */           
/*      */           return;
/*      */         } 
/* 6421 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 6423 */         responder.getCommunicator().sendNormalServerMessage("The bank no longer is available as the token is gone.");
/*      */         return;
/*      */       } 
/* 6426 */       long money = responder.getMoney();
/* 6427 */       if (money > 0L) {
/*      */         
/* 6429 */         long valueWithdrawn = getValueWithdrawn(question);
/* 6430 */         if (valueWithdrawn > 0L) {
/*      */           
/*      */           try
/*      */           {
/* 6434 */             if (responder.chargeMoney(valueWithdrawn))
/*      */             {
/* 6436 */               Item[] coins = Economy.getEconomy().getCoinsFor(valueWithdrawn);
/* 6437 */               Item inventory = responder.getInventory();
/* 6438 */               for (int x = 0; x < coins.length; x++)
/*      */               {
/* 6440 */                 inventory.insertItem(coins[x]);
/*      */               }
/* 6442 */               Change withd = Economy.getEconomy().getChangeFor(valueWithdrawn);
/* 6443 */               responder.getCommunicator().sendNormalServerMessage("You withdraw " + withd
/* 6444 */                   .getChangeString() + " from the bank.");
/* 6445 */               Change c = new Change(money - valueWithdrawn);
/* 6446 */               responder.getCommunicator().sendNormalServerMessage("New balance: " + c.getChangeString() + ".");
/* 6447 */               logger.info(responder.getName() + " withdraw " + withd.getChangeString() + " from the bank and should have " + c
/* 6448 */                   .getChangeString() + " now.");
/*      */             }
/*      */             else
/*      */             {
/* 6452 */               responder.getCommunicator().sendNormalServerMessage("You can not withdraw that amount of money at the moment.");
/*      */             }
/*      */           
/*      */           }
/* 6456 */           catch (IOException iox)
/*      */           {
/* 6458 */             logger.log(Level.WARNING, "Failed to withdraw money from " + responder
/* 6459 */                 .getName() + ":" + iox.getMessage(), iox);
/* 6460 */             responder.getCommunicator().sendNormalServerMessage("The transaction failed. Please contact the game masters using the <i>/dev</i> command.");
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 6465 */           responder.getCommunicator().sendNormalServerMessage("No money withdrawn.");
/*      */         } 
/*      */       } else {
/* 6468 */         responder.getCommunicator().sendNormalServerMessage("You have no money in the bank.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void parseVillageInfoQuestion(VillageInfo question) {
/* 6478 */     Creature responder = question.getResponder();
/* 6479 */     int type = question.getType();
/*      */     
/* 6481 */     if (type == 0) {
/*      */       
/* 6483 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 6486 */     if (type == 14) {
/*      */       
/*      */       try {
/*      */         
/* 6490 */         Item token = Items.getItem(question.target);
/* 6491 */         int vill = token.getData2();
/* 6492 */         Village village = Villages.getVillage(vill);
/* 6493 */         long money = responder.getMoney();
/* 6494 */         if (money > 0L) {
/*      */           
/* 6496 */           long valueWithdrawn = getValueWithdrawn(question);
/* 6497 */           if (valueWithdrawn > 0L) {
/*      */ 
/*      */             
/*      */             try {
/* 6501 */               if (village.plan != null) {
/*      */                 
/* 6503 */                 if (responder.chargeMoney(valueWithdrawn)) {
/*      */                   
/* 6505 */                   village.plan.addMoney(valueWithdrawn);
/* 6506 */                   village.plan.addPayment(responder.getName(), responder.getWurmId(), valueWithdrawn);
/* 6507 */                   Change newch = Economy.getEconomy().getChangeFor(valueWithdrawn);
/* 6508 */                   responder.getCommunicator().sendNormalServerMessage("You pay " + newch
/* 6509 */                       .getChangeString() + " to the upkeep fund of " + village
/* 6510 */                       .getName() + ".");
/* 6511 */                   logger.log(Level.INFO, responder.getName() + " added " + valueWithdrawn + " irons to " + village.getName() + " upkeep.");
/*      */                 }
/*      */                 else {
/*      */                   
/* 6515 */                   responder.getCommunicator().sendNormalServerMessage("You don't have that much money.");
/*      */                 } 
/*      */               } else {
/*      */                 
/* 6519 */                 responder.getCommunicator().sendNormalServerMessage("This village does not have an upkeep plan.");
/*      */               }
/*      */             
/* 6522 */             } catch (IOException iox) {
/*      */               
/* 6524 */               logger.log(Level.WARNING, "Failed to withdraw money from " + responder
/* 6525 */                   .getName() + ":" + iox.getMessage(), iox);
/* 6526 */               responder.getCommunicator().sendNormalServerMessage("The transaction failed. Please contact the game masters using the <i>/dev</i> command.");
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 6531 */             responder.getCommunicator().sendNormalServerMessage("No money withdrawn.");
/*      */           } 
/*      */         } else {
/* 6534 */           responder.getCommunicator().sendNormalServerMessage("You have no money in the bank.");
/*      */         } 
/* 6536 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 6538 */         logger.log(Level.WARNING, responder.getName() + " tried to get info for null token with id " + question.target, (Throwable)nsi);
/*      */         
/* 6540 */         responder.getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
/*      */       
/*      */       }
/* 6543 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 6545 */         logger.log(Level.WARNING, responder.getName() + " tried to get info for null village for token with id " + question.target);
/*      */         
/* 6547 */         responder.getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
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
/*      */   static final void parseVillageUpkeepQuestion(VillageUpkeep question) {
/* 6559 */     Creature responder = question.getResponder();
/* 6560 */     int type = question.getType();
/*      */     
/* 6562 */     if (type == 0) {
/*      */       
/* 6564 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/* 6567 */     if (type == 120) {
/*      */       try {
/*      */         Village village;
/*      */ 
/*      */ 
/*      */         
/* 6573 */         if (question.target == -10L) {
/* 6574 */           village = question.getResponder().getCitizenVillage();
/*      */         } else {
/*      */           
/* 6577 */           Item token = Items.getItem(question.target);
/* 6578 */           int vill = token.getData2();
/* 6579 */           village = Villages.getVillage(vill);
/*      */         } 
/* 6581 */         long money = responder.getMoney();
/* 6582 */         if (money > 0L) {
/*      */           
/* 6584 */           long valueWithdrawn = getValueWithdrawn(question);
/* 6585 */           if (valueWithdrawn > 0L) {
/*      */ 
/*      */             
/*      */             try {
/* 6589 */               if (village.plan != null) {
/*      */                 
/* 6591 */                 if (responder.chargeMoney(valueWithdrawn)) {
/*      */                   
/* 6593 */                   village.plan.addMoney(valueWithdrawn);
/* 6594 */                   village.plan.addPayment(responder.getName(), responder.getWurmId(), valueWithdrawn);
/* 6595 */                   Change newch = Economy.getEconomy().getChangeFor(valueWithdrawn);
/* 6596 */                   responder.getCommunicator().sendNormalServerMessage("You pay " + newch
/* 6597 */                       .getChangeString() + " to the upkeep fund of " + village
/* 6598 */                       .getName() + ".");
/* 6599 */                   logger.log(Level.INFO, responder.getName() + " added " + valueWithdrawn + " irons to " + village.getName() + " upkeep.");
/*      */                 }
/*      */                 else {
/*      */                   
/* 6603 */                   responder.getCommunicator().sendNormalServerMessage("You don't have that much money.");
/*      */                 } 
/*      */               } else {
/*      */                 
/* 6607 */                 responder.getCommunicator().sendNormalServerMessage("This village does not have an upkeep plan.");
/*      */               }
/*      */             
/* 6610 */             } catch (IOException iox) {
/*      */               
/* 6612 */               logger.log(Level.WARNING, "Failed to withdraw money from " + responder
/* 6613 */                   .getName() + ":" + iox.getMessage(), iox);
/* 6614 */               responder.getCommunicator().sendNormalServerMessage("The transaction failed. Please contact the game masters using the <i>/dev</i> command.");
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 6619 */             responder.getCommunicator().sendNormalServerMessage("No money withdrawn.");
/*      */           } 
/*      */         } else {
/* 6622 */           responder.getCommunicator().sendNormalServerMessage("You have no money in the bank.");
/*      */         } 
/* 6624 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 6626 */         logger.log(Level.WARNING, responder.getName() + " tried to get info for null token with id " + question.target, (Throwable)nsi);
/*      */         
/* 6628 */         responder.getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
/*      */       
/*      */       }
/* 6631 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 6633 */         logger.log(Level.WARNING, responder.getName() + " tried to get info for null village for token with id " + question.target);
/*      */         
/* 6635 */         responder.getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
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
/*      */   static final void parseTitleCompoundQuestion(TitleCompoundQuestion question) {
/* 6647 */     Player responder = (Player)question.getResponder();
/* 6648 */     Titles.Title[] titles = responder.getTitles();
/* 6649 */     if (titles.length == 0 && responder.getAppointments() == 0L && !responder.isAppointed()) {
/*      */       
/* 6651 */       logger.info(String.format("No titles found for %s.", new Object[] { responder.getName() }));
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 6658 */     if (Servers.isThisAPvpServer()) {
/*      */       
/* 6660 */       King king = King.getKing(question.getResponder().getKingdomId());
/* 6661 */       if (king != null && (question.getResponder().getAppointments() != 0L || question.getResponder().isAppointed())) {
/*      */         
/* 6663 */         Appointments a = Appointments.getAppointments(king.era);
/* 6664 */         for (int x = 0; x < a.officials.length; x++) {
/*      */           
/* 6666 */           int oId = x + 1500;
/* 6667 */           String office = question.getAnswer().getProperty("office" + oId);
/* 6668 */           if (office != null && Boolean.parseBoolean(office))
/*      */           {
/* 6670 */             if (a.officials[x] == question.getResponder().getWurmId()) {
/*      */ 
/*      */               
/* 6673 */               Appointment o = a.getAppointment(oId);
/* 6674 */               question.getResponder().getCommunicator().sendNormalServerMessage("You vacate the office of " + o
/* 6675 */                   .getNameForGender((byte)0) + ".", (byte)2);
/* 6676 */               a.setOfficial(x + 1500, 0L);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 6683 */     String occultistAns = question.getAnswer().getProperty("hideoccultist");
/* 6684 */     if (occultistAns != null) {
/*      */       
/* 6686 */       boolean bool = Boolean.parseBoolean(occultistAns);
/* 6687 */       responder.setFlag(24, bool);
/*      */     } 
/* 6689 */     String meditationAns = question.getAnswer().getProperty("hidemeditation");
/* 6690 */     if (meditationAns != null) {
/*      */       
/* 6692 */       boolean bool = Boolean.parseBoolean(meditationAns);
/* 6693 */       responder.setFlag(25, bool);
/*      */     } 
/* 6695 */     String t1 = question.getAnswer().getProperty("First");
/* 6696 */     String t2 = question.getAnswer().getProperty("Second");
/* 6697 */     if (t1 != null) {
/*      */       
/*      */       try {
/*      */         
/* 6701 */         int id = Integer.parseInt(t1);
/* 6702 */         if (id == 0) {
/* 6703 */           responder.setTitle(null);
/*      */         } else {
/*      */           
/* 6706 */           Titles.Title title = question.getFirstTitle(id - 1);
/* 6707 */           if (title == null) {
/*      */             return;
/*      */           }
/* 6710 */           responder.setTitle(title);
/*      */         }
/*      */       
/* 6713 */       } catch (NumberFormatException nfe) {
/*      */         
/* 6715 */         logger.log(Level.WARNING, responder.getName() + " tried to parse " + t1 + " as int.");
/*      */       } 
/*      */     }
/* 6718 */     if (t2 != null) {
/*      */       
/*      */       try {
/*      */         
/* 6722 */         int id = Integer.parseInt(t2);
/* 6723 */         if (id == 0) {
/* 6724 */           responder.setSecondTitle(null);
/*      */         } else {
/*      */           
/* 6727 */           Titles.Title title = question.getFirstTitle(id - 1);
/* 6728 */           if (title == null)
/*      */             return; 
/* 6730 */           if (title == responder.getTitle()) {
/*      */             
/* 6732 */             responder.getCommunicator().sendSafeServerMessage("You cannot use two of the same title.");
/* 6733 */             responder.setSecondTitle(null);
/*      */             
/*      */             return;
/*      */           } 
/* 6737 */           responder.setSecondTitle(title);
/*      */         }
/*      */       
/* 6740 */       } catch (NumberFormatException nfe) {
/*      */         
/* 6742 */         logger.log(Level.WARNING, responder.getName() + " tried to parse " + t2 + " as int.");
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
/*      */   static final void parseTitleQuestion(TitleQuestion question) {
/* 6754 */     Player responder = (Player)question.getResponder();
/* 6755 */     Titles.Title[] titles = responder.getTitles();
/* 6756 */     if (titles.length == 0 && responder.getAppointments() == 0L && !responder.isAppointed()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6763 */     if (Servers.isThisAPvpServer()) {
/*      */       
/* 6765 */       King king = King.getKing(question.getResponder().getKingdomId());
/* 6766 */       if (king != null && (question.getResponder().getAppointments() != 0L || question.getResponder().isAppointed())) {
/*      */         
/* 6768 */         Appointments a = Appointments.getAppointments(king.era);
/* 6769 */         for (int x = 0; x < a.officials.length; x++) {
/*      */           
/* 6771 */           int oId = x + 1500;
/* 6772 */           String office = question.getAnswer().getProperty("office" + oId);
/* 6773 */           if (office != null && Boolean.parseBoolean(office))
/*      */           {
/* 6775 */             if (a.officials[x] == question.getResponder().getWurmId()) {
/*      */ 
/*      */               
/* 6778 */               Appointment o = a.getAppointment(oId);
/* 6779 */               question.getResponder().getCommunicator().sendNormalServerMessage("You vacate the office of " + o
/* 6780 */                   .getNameForGender((byte)0) + ".", (byte)2);
/* 6781 */               a.setOfficial(x + 1500, 0L);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 6788 */     String occultistAns = question.getAnswer().getProperty("hideoccultist");
/* 6789 */     if (occultistAns != null) {
/*      */       
/* 6791 */       boolean bool = Boolean.parseBoolean(occultistAns);
/* 6792 */       responder.setFlag(24, bool);
/*      */     } 
/* 6794 */     String meditationAns = question.getAnswer().getProperty("hidemeditation");
/* 6795 */     if (meditationAns != null) {
/*      */       
/* 6797 */       boolean bool = Boolean.parseBoolean(meditationAns);
/* 6798 */       responder.setFlag(25, bool);
/*      */     } 
/* 6800 */     String accept = question.getAnswer().getProperty("TITLE");
/* 6801 */     if (accept != null) {
/*      */       
/*      */       try {
/*      */         
/* 6805 */         int id = Integer.parseInt(accept);
/* 6806 */         if (id == 0) {
/* 6807 */           responder.setTitle(null);
/*      */         } else {
/*      */           
/* 6810 */           Titles.Title title = question.getTitle(id - 1);
/* 6811 */           if (title == null) {
/*      */             return;
/*      */           }
/* 6814 */           responder.setTitle(title);
/*      */         }
/*      */       
/* 6817 */       } catch (NumberFormatException nfe) {
/*      */         
/* 6819 */         logger.log(Level.WARNING, responder.getName() + " tried to parse " + accept + " as int.");
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
/*      */   static final void parseServerQuestion(ServerQuestion question) {
/* 6831 */     Creature responder = question.getResponder();
/* 6832 */     String key = "transferTo";
/* 6833 */     String val = question.getAnswer().getProperty(key);
/* 6834 */     int transid = 0;
/* 6835 */     if (val != null) {
/*      */ 
/*      */       
/*      */       try {
/* 6839 */         transid = Integer.parseInt(val);
/*      */       }
/* 6841 */       catch (NumberFormatException nfe) {
/*      */         
/* 6843 */         logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 6844 */         responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */         return;
/*      */       } 
/* 6847 */       if (transid > 0) {
/*      */         
/* 6849 */         ServerEntry targetserver = question.getTransferEntry(transid - 1);
/* 6850 */         if (targetserver != null) {
/*      */           
/* 6852 */           if (targetserver.isAvailable(responder.getPower(), responder.isReallyPaying())) {
/*      */             
/* 6854 */             Player playerToTransfer = (Player)responder;
/* 6855 */             if (WurmId.getType(question.getTarget()) == 0) {
/*      */               
/*      */               try {
/*      */                 
/* 6859 */                 playerToTransfer = Players.getInstance().getPlayer(question.getTarget());
/*      */               }
/* 6861 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 6866 */             if (!responder.equals(playerToTransfer)) {
/*      */               
/* 6868 */               if (playerToTransfer.getPower() > responder.getPower()) {
/*      */                 
/* 6870 */                 responder.getCommunicator().sendNormalServerMessage("You are too weak to transfer " + playerToTransfer
/* 6871 */                     .getName() + " to " + targetserver.name + ".");
/*      */                 
/*      */                 return;
/*      */               } 
/* 6875 */               responder.getCommunicator().sendNormalServerMessage("Transferring " + playerToTransfer
/* 6876 */                   .getName() + " to " + targetserver.name + ".");
/* 6877 */               playerToTransfer.getCommunicator().sendNormalServerMessage(responder
/* 6878 */                   .getName() + " transfers you to " + targetserver.name + ".");
/*      */               
/* 6880 */               logger.info(responder.getName() + " transfers " + playerToTransfer.getName() + " to " + targetserver.name + ".");
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 6885 */               playerToTransfer.getCommunicator().sendNormalServerMessage("Transferring to " + targetserver.name + ".");
/*      */               
/* 6887 */               logger.info(playerToTransfer.getName() + " transferring to " + targetserver.name + ".");
/*      */             } 
/* 6889 */             Server.getInstance().broadCastAction(playerToTransfer
/* 6890 */                 .getName() + " transfers to " + targetserver.name + ".", (Creature)playerToTransfer, 5);
/* 6891 */             int tilex = targetserver.SPAWNPOINTJENNX;
/* 6892 */             int tiley = targetserver.SPAWNPOINTJENNY;
/* 6893 */             if (playerToTransfer.getPower() <= 0) {
/*      */               
/* 6895 */               byte targetKingdom = playerToTransfer.getKingdomId();
/* 6896 */               if (targetserver.getKingdom() == 4)
/* 6897 */                 targetKingdom = 4; 
/* 6898 */               playerToTransfer.lastKingdom = playerToTransfer.getKingdomId();
/*      */               
/* 6900 */               if (targetKingdom != playerToTransfer.getKingdomId()) {
/*      */                 
/*      */                 try {
/* 6903 */                   playerToTransfer.setKingdomId(targetKingdom);
/*      */                 }
/* 6905 */                 catch (IOException iOException) {}
/*      */               }
/*      */             } 
/*      */             
/* 6909 */             if (playerToTransfer.getKingdomId() == 1) {
/*      */               
/* 6911 */               tilex = targetserver.SPAWNPOINTJENNX;
/* 6912 */               tiley = targetserver.SPAWNPOINTJENNY;
/*      */             }
/* 6914 */             else if (playerToTransfer.getKingdomId() == 3) {
/*      */               
/* 6916 */               tilex = targetserver.SPAWNPOINTLIBX;
/* 6917 */               tiley = targetserver.SPAWNPOINTLIBY;
/*      */             }
/* 6919 */             else if (playerToTransfer.getKingdomId() == 2) {
/*      */               
/* 6921 */               tilex = targetserver.SPAWNPOINTMOLX;
/* 6922 */               tiley = targetserver.SPAWNPOINTMOLY;
/*      */             } 
/* 6924 */             playerToTransfer.sendTransfer(Server.getInstance(), targetserver.INTRASERVERADDRESS, 
/* 6925 */                 Integer.parseInt(targetserver.INTRASERVERPORT), targetserver.INTRASERVERPASSWORD, targetserver.id, tilex, tiley, true, false, playerToTransfer
/* 6926 */                 .getKingdomId());
/* 6927 */             playerToTransfer.transferCounter = 30;
/*      */             
/*      */             return;
/*      */           } 
/* 6931 */           responder.getCommunicator().sendNormalServerMessage(targetserver.name + " is not available now.");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 6936 */     if (responder.getPower() > 2) {
/*      */       
/* 6938 */       int addid = -1;
/* 6939 */       key = "neighbourServer";
/* 6940 */       val = question.getAnswer().getProperty(key);
/*      */       
/* 6942 */       if (val != null) {
/*      */         
/*      */         try {
/*      */           
/* 6946 */           addid = Integer.parseInt(val);
/* 6947 */           if (addid > 0) {
/*      */             
/* 6949 */             key = "direction";
/* 6950 */             val = question.getAnswer().getProperty(key);
/* 6951 */             if (val != null) {
/*      */ 
/*      */ 
/*      */               
/* 6955 */               ServerEntry entry = question.getServerEntry(addid - 1);
/* 6956 */               if (entry != null) {
/*      */                 
/* 6958 */                 if (val.equals("0"))
/* 6959 */                   val = "NORTH"; 
/* 6960 */                 if (val.equals("1"))
/* 6961 */                   val = "EAST"; 
/* 6962 */                 if (val.equals("2"))
/* 6963 */                   val = "SOUTH"; 
/* 6964 */                 if (val.equals("3"))
/* 6965 */                   val = "WEST"; 
/* 6966 */                 Servers.addServerNeighbour(entry.id, val);
/* 6967 */                 responder.getCommunicator().sendNormalServerMessage("Added server with id " + entry.id + " " + val + " of this server.");
/*      */                 
/* 6969 */                 logger.info(responder.getName() + " added server with name " + entry.name + " and id " + entry.id + " " + val + " of this server.");
/*      */               }
/*      */               else {
/*      */                 
/* 6973 */                 responder.getCommunicator().sendNormalServerMessage("Failed to locate the server to add.");
/*      */               } 
/*      */             } 
/*      */           } 
/* 6977 */         } catch (NumberFormatException nfe) {
/*      */           
/* 6979 */           logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 6980 */           responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */           
/*      */           return;
/*      */         } 
/*      */       }
/* 6985 */       int delid = -1;
/* 6986 */       key = "deleteServer";
/* 6987 */       val = question.getAnswer().getProperty(key);
/*      */       
/* 6989 */       if (val != null) {
/*      */         
/*      */         try {
/*      */           
/* 6993 */           delid = Integer.parseInt(val);
/* 6994 */           if (delid > 0) {
/*      */             
/* 6996 */             ServerEntry entry = question.getServerEntry(delid - 1);
/* 6997 */             if (entry != null) {
/*      */               
/* 6999 */               Servers.deleteServerEntry(entry.id);
/* 7000 */               responder.getCommunicator().sendNormalServerMessage("Deleted server with id " + entry.id + ".");
/* 7001 */               logger.info(responder.getName() + " Deleted server with name " + entry.name + " and id " + entry.id + '.');
/*      */             }
/*      */             else {
/*      */               
/* 7005 */               responder.getCommunicator().sendNormalServerMessage("Failed to locate the server to delete.");
/*      */             } 
/*      */           } 
/* 7008 */         } catch (NumberFormatException nfe) {
/*      */           
/* 7010 */           logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7011 */           responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */           
/*      */           return;
/*      */         } 
/*      */       }
/* 7016 */       int id = -1;
/* 7017 */       key = "addid";
/* 7018 */       val = question.getAnswer().getProperty(key);
/*      */       
/* 7020 */       if (val != null && val.length() > 0) {
/*      */         
/*      */         try {
/*      */           
/* 7024 */           id = Integer.parseInt(val);
/* 7025 */           if (id < 0) {
/*      */             
/* 7027 */             responder.getCommunicator().sendAlertServerMessage("The id of the server can not be " + id + ".");
/*      */             return;
/*      */           } 
/* 7030 */           ServerEntry entry = Servers.getServerWithId(id);
/* 7031 */           if (entry != null) {
/*      */             
/* 7033 */             responder.getCommunicator().sendAlertServerMessage("The id of the server already exists: " + id + ".");
/*      */             
/*      */             return;
/*      */           } 
/* 7037 */         } catch (NumberFormatException nfe) {
/*      */           
/* 7039 */           logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7040 */           responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */           return;
/*      */         } 
/*      */       }
/* 7044 */       if (id >= 0) {
/*      */         
/* 7046 */         key = "addname";
/* 7047 */         val = question.getAnswer().getProperty(key);
/* 7048 */         String name = val;
/* 7049 */         if (val != null)
/*      */         {
/* 7051 */           if (val.length() < 4) {
/*      */             
/* 7053 */             responder.getCommunicator().sendAlertServerMessage("The name of the server can not be " + val + ". It is too short.");
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7058 */         key = "addhome";
/* 7059 */         val = question.getAnswer().getProperty(key);
/* 7060 */         boolean homeServer = false;
/* 7061 */         if (val != null)
/*      */         {
/* 7063 */           if (val.equals("true")) {
/* 7064 */             homeServer = true;
/*      */           }
/*      */         }
/* 7067 */         key = "addpayment";
/* 7068 */         val = question.getAnswer().getProperty(key);
/* 7069 */         boolean isPayment = false;
/* 7070 */         if (val != null)
/*      */         {
/* 7072 */           if (val.equals("true")) {
/* 7073 */             isPayment = true;
/*      */           }
/*      */         }
/* 7076 */         key = "addlogin";
/* 7077 */         val = question.getAnswer().getProperty(key);
/* 7078 */         boolean isLogin = false;
/* 7079 */         if (val != null)
/*      */         {
/* 7081 */           if (val.equals("true")) {
/* 7082 */             isLogin = true;
/*      */           }
/*      */         }
/* 7085 */         int jennx = -1;
/* 7086 */         key = "addsjx";
/* 7087 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7089 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7093 */             jennx = Integer.parseInt(val);
/* 7094 */             if (jennx < 0) {
/*      */               
/* 7096 */               responder.getCommunicator().sendAlertServerMessage("Illegal value for start jenn x " + jennx + ".");
/*      */               
/*      */               return;
/*      */             } 
/* 7100 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7102 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7103 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             return;
/*      */           } 
/*      */         }
/* 7107 */         int jenny = -1;
/* 7108 */         key = "addsjy";
/* 7109 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7111 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7115 */             jenny = Integer.parseInt(val);
/* 7116 */             if (jenny < 0) {
/*      */               
/* 7118 */               responder.getCommunicator().sendAlertServerMessage("Illegal value for start jenn y " + jenny + ".");
/*      */               
/*      */               return;
/*      */             } 
/* 7122 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7124 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7125 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7130 */         int liby = -1;
/* 7131 */         key = "addsly";
/* 7132 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7134 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7138 */             liby = Integer.parseInt(val);
/* 7139 */             if (liby < 0) {
/*      */               
/* 7141 */               responder.getCommunicator()
/* 7142 */                 .sendAlertServerMessage("Illegal value for start Libila y " + liby + ".");
/*      */               
/*      */               return;
/*      */             } 
/* 7146 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7148 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7149 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7154 */         int libx = -1;
/* 7155 */         key = "addslx";
/* 7156 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7158 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7162 */             libx = Integer.parseInt(val);
/* 7163 */             if (libx < 0) {
/*      */               
/* 7165 */               responder.getCommunicator()
/* 7166 */                 .sendAlertServerMessage("Illegal value for start Libila x " + libx + ".");
/*      */               
/*      */               return;
/*      */             } 
/* 7170 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7172 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7173 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7178 */         int molx = -1;
/* 7179 */         key = "addsmx";
/* 7180 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7182 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7186 */             molx = Integer.parseInt(val);
/* 7187 */             if (molx < 0) {
/*      */               
/* 7189 */               responder.getCommunicator().sendAlertServerMessage("Illegal value for start Mol Rehan x " + molx + ".");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/* 7194 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7196 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7197 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7202 */         int moly = -1;
/* 7203 */         key = "addsmy";
/* 7204 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7206 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7210 */             moly = Integer.parseInt(val);
/* 7211 */             if (moly < 0) {
/*      */               
/* 7213 */               responder.getCommunicator().sendAlertServerMessage("Illegal value for start Mol Rehan y " + moly + ".");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/* 7218 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7220 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7221 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7226 */         int intraport = -1;
/* 7227 */         key = "addintport";
/* 7228 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7230 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7234 */             intraport = Integer.parseInt(val);
/* 7235 */             if (intraport < 0) {
/*      */               
/* 7237 */               responder.getCommunicator().sendAlertServerMessage("Illegal value for intra server port " + intraport + ".");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/* 7242 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7244 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7245 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7250 */         int externalport = -1;
/* 7251 */         key = "addextport";
/* 7252 */         val = question.getAnswer().getProperty(key);
/*      */         
/* 7254 */         if (val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7258 */             externalport = Integer.parseInt(val);
/* 7259 */             if (externalport < 0) {
/*      */               
/* 7261 */               responder.getCommunicator().sendAlertServerMessage("Illegal value for external server port " + externalport + ".");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/* 7266 */           } catch (NumberFormatException nfe) {
/*      */             
/* 7268 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7269 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7274 */         key = "addintip";
/* 7275 */         val = question.getAnswer().getProperty(key);
/* 7276 */         String intraip = val;
/* 7277 */         if (val != null)
/*      */         {
/* 7279 */           if (val.length() < 8) {
/*      */             
/* 7281 */             responder.getCommunicator().sendAlertServerMessage("The internal ip address of the server can not be " + val + ". It is too short.");
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/*      */         
/* 7287 */         key = "addextip";
/* 7288 */         val = question.getAnswer().getProperty(key);
/* 7289 */         String externalip = val;
/* 7290 */         if (val != null)
/*      */         {
/* 7292 */           if (val.length() < 8) {
/*      */             
/* 7294 */             responder.getCommunicator().sendAlertServerMessage("The external ip address of the server can not be " + val + ". It is too short.");
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/*      */         
/* 7300 */         key = "addintpass";
/* 7301 */         val = question.getAnswer().getProperty(key);
/* 7302 */         String password = val;
/* 7303 */         if (val != null)
/*      */         {
/* 7305 */           if (val.length() < 4) {
/*      */             
/* 7307 */             responder.getCommunicator().sendAlertServerMessage("The password of the server can not be " + val + ". It is too short.");
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/*      */         
/* 7313 */         key = "addkingdom";
/* 7314 */         val = question.getAnswer().getProperty(key);
/* 7315 */         byte kingdom = 0;
/* 7316 */         if (homeServer && val != null) {
/*      */           
/*      */           try {
/*      */             
/* 7320 */             kingdom = Byte.parseByte(val);
/*      */           }
/* 7322 */           catch (NumberFormatException nfe) {
/*      */             
/* 7324 */             logger.log(Level.WARNING, responder.getName() + " tried to parse " + val + " as int.");
/* 7325 */             responder.getCommunicator().sendNormalServerMessage("Illegal value for key " + key);
/*      */             
/*      */             return;
/*      */           } 
/*      */         }
/* 7330 */         String _consumerKeyToUse = "";
/* 7331 */         String _consumerSecretToUse = "";
/* 7332 */         String _applicationToken = "";
/* 7333 */         String _applicationSecret = "";
/* 7334 */         key = "consumerKeyToUse";
/* 7335 */         val = question.getAnswer().getProperty(key);
/* 7336 */         if (val == null)
/*      */         {
/* 7338 */           _consumerKeyToUse = val;
/*      */         }
/* 7340 */         key = "consumerSecretToUse";
/* 7341 */         val = question.getAnswer().getProperty(key);
/* 7342 */         if (val == null)
/*      */         {
/* 7344 */           _consumerSecretToUse = val;
/*      */         }
/* 7346 */         key = "applicationToken";
/* 7347 */         val = question.getAnswer().getProperty(key);
/* 7348 */         if (val == null)
/*      */         {
/* 7350 */           _applicationToken = val;
/*      */         }
/* 7352 */         key = "applicationSecret";
/* 7353 */         val = question.getAnswer().getProperty(key);
/* 7354 */         if (val == null)
/*      */         {
/* 7356 */           _applicationSecret = val;
/*      */         }
/* 7358 */         Servers.registerServer(id, name, homeServer, jennx, jenny, libx, liby, molx, moly, intraip, 
/* 7359 */             String.valueOf(intraport), password, externalip, String.valueOf(externalport), isLogin, kingdom, isPayment, _consumerKeyToUse, _consumerSecretToUse, _applicationToken, _applicationSecret, false, false, false);
/*      */ 
/*      */         
/* 7362 */         responder.getCommunicator().sendAlertServerMessage("You have successfully registered the server " + name + " and may now add it as a neighbour.");
/*      */         
/* 7364 */         logger.info(responder.getName() + " successfully registered the server " + name + " with ID " + id + " and may now add it as a neighbour.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final void parseLCMManagementQuestion(LCMManagementQuestion question) {
/* 7372 */     String playerName = question.getAnswer().getProperty("name");
/* 7373 */     Creature performer = question.getResponder();
/*      */     
/* 7375 */     if (playerName.isEmpty()) {
/*      */       
/* 7377 */       if (performer != null)
/*      */       {
/* 7379 */         performer.getCommunicator().sendNormalServerMessage("You didn't fill in a name.");
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/* 7384 */     if (question.getActionType() == 698) {
/* 7385 */       Players.appointCA(performer, playerName);
/* 7386 */     } else if (question.getActionType() == 699) {
/* 7387 */       Players.appointCM(performer, playerName);
/* 7388 */     } else if (question.getActionType() == 700) {
/* 7389 */       Players.displayLCMInfo(performer, playerName);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\QuestionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */