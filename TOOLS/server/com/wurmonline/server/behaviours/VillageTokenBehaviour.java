/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.banks.Bank;
/*      */ import com.wurmonline.server.banks.Banks;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.BankManagementQuestion;
/*      */ import com.wurmonline.server.questions.EconomicAdvisorInfo;
/*      */ import com.wurmonline.server.questions.KingdomHistory;
/*      */ import com.wurmonline.server.questions.KingdomStatusQuestion;
/*      */ import com.wurmonline.server.questions.ManageObjectList;
/*      */ import com.wurmonline.server.questions.VillageShowPlan;
/*      */ import com.wurmonline.server.villages.Guard;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.Zones;
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
/*      */ final class VillageTokenBehaviour
/*      */   extends ItemBehaviour
/*      */ {
/*   58 */   private static final Logger logger = Logger.getLogger(VillageTokenBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   VillageTokenBehaviour() {
/*   65 */     super((short)25);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*   71 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*   72 */     toReturn.addAll(getVillageBehaviours(performer, target));
/*      */     
/*   74 */     if (target.getOwnerId() != -10L) {
/*   75 */       toReturn.add(Actions.actionEntrys[176]);
/*      */     }
/*   77 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*   83 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*   84 */     toReturn.addAll(getVillageBehaviours(performer, target));
/*      */     
/*   86 */     if (target.getOwnerId() != -10L)
/*   87 */       toReturn.add(Actions.actionEntrys[176]); 
/*   88 */     if (performer.getPower() > 0 && (source
/*   89 */       .getTemplateId() == 176 || source.getTemplateId() == 315))
/*      */     {
/*   91 */       toReturn.add(Actions.actionEntrys[513]);
/*      */     }
/*   93 */     if (performer.getCurrentVillage() != null && (performer.getCurrentVillage()).kingdom == performer.getKingdomId())
/*      */     {
/*   95 */       if (!performer.getCurrentVillage().isEnemy(performer))
/*      */       {
/*   97 */         if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 0, 2))
/*      */         {
/*   99 */           if (!source.isNoDiscard() && !source.isInstaDiscard() && 
/*  100 */             !source.isTemporary()) {
/*  101 */             toReturn.add(Actions.actionEntrys[31]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  106 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  112 */     boolean done = true;
/*  113 */     if (action == 176) {
/*  114 */       Methods.setVillageToken(performer, target);
/*  115 */     } else if (action == 77) {
/*  116 */       Methods.sendVillageInfo(performer, target);
/*  117 */     } else if (action == 670) {
/*  118 */       Methods.sendManageUpkeep(performer, target);
/*  119 */     } else if (action == 71) {
/*  120 */       Methods.sendVillageHistory(performer, target);
/*  121 */     } else if (action == 72) {
/*  122 */       Methods.sendAreaHistory(performer, target);
/*  123 */     } else if (action == 80) {
/*      */       
/*  125 */       Village village = performer.getCitizenVillage();
/*      */ 
/*      */       
/*  128 */       if (village.mayDoDiplomacy(performer)) {
/*  129 */         Methods.sendManageAllianceQuestion(performer, target);
/*      */       } else {
/*  131 */         performer.getCommunicator().sendNormalServerMessage("You may not perform diplomacy for " + village
/*  132 */             .getName() + ".");
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  137 */     else if (action == 67) {
/*      */       
/*  139 */       int villageId = target.getData2();
/*  140 */       if (villageId <= 0) {
/*      */         
/*  142 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  143 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         
/*  145 */         return true;
/*      */       } 
/*      */       
/*      */       try {
/*  149 */         Village currVill = Villages.getVillage(villageId);
/*  150 */         if (currVill != null)
/*      */         {
/*  152 */           if (currVill.isActionAllowed((short)67, performer)) {
/*  153 */             Methods.sendManageVillageGuardsQuestion(performer, target);
/*      */           }
/*      */         }
/*  156 */       } catch (NoSuchVillageException nsv) {
/*      */         
/*  158 */         return true;
/*      */       }
/*      */     
/*  161 */     } else if (action == 348) {
/*      */       
/*  163 */       if (performer.isGuest()) {
/*  164 */         done = true;
/*      */       } else {
/*  166 */         done = Methods.disbandVillage(performer, target, counter);
/*      */       } 
/*  168 */     } else if (action == 349) {
/*      */       
/*  170 */       if (performer.isGuest()) {
/*  171 */         done = true;
/*      */       } else {
/*  173 */         done = Methods.preventDisbandVillage(performer, target, counter);
/*      */       } 
/*  175 */     } else if (action == 350) {
/*      */       
/*  177 */       if (performer.isPaying() && !performer.hasFlag(63))
/*      */       {
/*  179 */         int villageId = target.getData2();
/*  180 */         if (villageId <= 0) {
/*      */           
/*  182 */           performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  183 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */           
/*  185 */           return true;
/*      */         } 
/*      */         
/*      */         try {
/*  189 */           Village currVill = Villages.getVillage(target.getData2());
/*  190 */           done = Methods.drainCoffers(performer, currVill, counter, target, act);
/*      */         }
/*  192 */         catch (NoSuchVillageException noSuchVillageException) {}
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  197 */     else if (action == 513) {
/*      */       
/*  199 */       int villageId = target.getData2();
/*  200 */       if (villageId <= 0) {
/*      */         
/*  202 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  203 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         
/*  205 */         return true;
/*      */       } 
/*      */       
/*      */       try {
/*  209 */         Village currVill = Villages.getVillage(villageId);
/*  210 */         if ((currVill.getGuards()).length == 1 || (performer
/*  211 */           .getPower() >= 2 && (currVill.getGuards()).length >= 1)) {
/*      */           
/*  213 */           if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*      */             
/*  215 */             currVill.putGuardsAtToken();
/*      */           } else {
/*      */             
/*  218 */             performer.getCommunicator().sendNormalServerMessage("You are now too far away to summon the guard.");
/*      */           } 
/*  220 */         } else if ((currVill.getGuards()).length > 0) {
/*  221 */           performer.getCommunicator().sendNormalServerMessage("There are more than one guard left. You can not summon it now.");
/*      */         } else {
/*      */           
/*  224 */           performer.getCommunicator().sendNormalServerMessage("There are no guards active.");
/*      */         }
/*      */       
/*  227 */       } catch (NoSuchVillageException noSuchVillageException) {}
/*      */ 
/*      */     
/*      */     }
/*  231 */     else if (action == 69) {
/*      */       
/*  233 */       done = true;
/*  234 */       int villageId = target.getData2();
/*  235 */       if (villageId <= 0) {
/*      */         
/*  237 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  238 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  245 */           Village currVill = Villages.getVillage(villageId);
/*  246 */           if (currVill.isActionAllowed(action, performer))
/*      */           {
/*  248 */             Methods.sendReputationManageQuestion(performer, target);
/*      */           }
/*      */           else
/*      */           {
/*  252 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/*  253 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*      */           }
/*      */         
/*      */         }
/*  257 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  259 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  264 */     } else if (action == 68) {
/*      */       
/*  266 */       done = true;
/*  267 */       int villageId = target.getData2();
/*  268 */       if (villageId <= 0) {
/*      */         
/*  270 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  271 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  278 */           Village currVill = Villages.getVillage(villageId);
/*  279 */           if (currVill.isActionAllowed(action, performer))
/*      */           {
/*  281 */             Methods.sendManageVillageSettingsQuestion(performer, target);
/*      */           }
/*      */           else
/*      */           {
/*  285 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/*  286 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*      */           }
/*      */         
/*      */         }
/*  290 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  292 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  297 */     } else if (action == 540) {
/*      */       
/*  299 */       done = true;
/*  300 */       int villageId = target.getData2();
/*  301 */       if (villageId <= 0) {
/*      */         
/*  303 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  304 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  311 */           Village currVill = Villages.getVillage(villageId);
/*  312 */           if (currVill.isActionAllowed(action, performer))
/*      */           {
/*  314 */             Methods.sendManageVillageRolesQuestion(performer, target);
/*      */           }
/*      */           else
/*      */           {
/*  318 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/*  319 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*      */           }
/*      */         
/*      */         }
/*  323 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  325 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  330 */     } else if (action == 66) {
/*      */       
/*  332 */       done = true;
/*  333 */       int villageId = target.getData2();
/*  334 */       if (villageId <= 0) {
/*      */         
/*  336 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  337 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  344 */           Village currVill = Villages.getVillage(villageId);
/*  345 */           if (currVill.isActionAllowed(action, performer))
/*      */           {
/*  347 */             Methods.sendManageVillageCitizensQuestion(performer, target);
/*      */           }
/*      */           else
/*      */           {
/*  351 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/*  352 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*      */           }
/*      */         
/*      */         }
/*  356 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  358 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  363 */     } else if (action == 70) {
/*      */       
/*  365 */       done = true;
/*  366 */       int villageId = target.getData2();
/*  367 */       if (villageId <= 0) {
/*      */         
/*  369 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  370 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  377 */           Village currVill = Villages.getVillage(target.getData2());
/*  378 */           if (currVill.isActionAllowed(action, performer))
/*      */           {
/*  380 */             Methods.sendManageVillageGatesQuestion(performer, target);
/*      */           }
/*      */           else
/*      */           {
/*  384 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/*  385 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*      */           }
/*      */         
/*      */         }
/*  389 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  391 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  396 */     } else if (action == 76) {
/*      */       
/*  398 */       done = true;
/*  399 */       int villageId = target.getData2();
/*  400 */       if (villageId <= 0) {
/*      */         
/*  402 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  403 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  410 */           Village currVill = Villages.getVillage(target.getData2());
/*      */ 
/*      */           
/*  413 */           if (currVill.isActionAllowed(action, performer)) {
/*      */ 
/*      */             
/*      */             try {
/*  417 */               Item deed = Items.getItem(currVill.getDeedId());
/*  418 */               if (deed.isNewDeed()) {
/*  419 */                 Methods.sendExpandVillageQuestion(performer, deed);
/*      */               } else {
/*  421 */                 performer.getCommunicator().sendNormalServerMessage("The mayor needs to replace the current deed first.");
/*      */               }
/*      */             
/*  424 */             } catch (NoSuchItemException nsi) {
/*      */               
/*  426 */               logger.log(Level.WARNING, "No deed for " + currVill.getName());
/*  427 */               performer
/*  428 */                 .getCommunicator()
/*  429 */                 .sendSafeServerMessage("Something went wrong as you tried to resize the settlement. The deed could not be located.");
/*      */               
/*  431 */               return done;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  436 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/*  437 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*      */           }
/*      */         
/*      */         }
/*  441 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  443 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  448 */     } else if (action == 689) {
/*      */       
/*  450 */       done = true;
/*  451 */       int villageId = target.getData2();
/*  452 */       if (villageId <= 0) {
/*      */         
/*  454 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  455 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  462 */           Village currVill = Villages.getVillage(target.getData2());
/*  463 */           if (currVill.isMayor(performer) || performer.getPower() >= 2)
/*      */           {
/*  465 */             VillageShowPlan vsp = new VillageShowPlan(performer, currVill);
/*  466 */             vsp.sendQuestion();
/*      */           }
/*      */           else
/*      */           {
/*  470 */             logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " trying to see plan when not a mayor (" + villageId + ").");
/*      */           }
/*      */         
/*      */         }
/*  474 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  476 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  481 */     } else if (action == 222) {
/*      */       
/*  483 */       int villageId = target.getData2();
/*  484 */       Bank bank = Banks.getBank(performer.getWurmId());
/*  485 */       if (bank != null) {
/*      */ 
/*      */         
/*  488 */         if (!performer.isGuest()) {
/*      */           
/*  490 */           if (villageId == bank.currentVillage) {
/*      */             
/*  492 */             if (!bank.open) {
/*      */               
/*  494 */               ((Player)performer).openBank();
/*      */             } else {
/*      */               
/*  497 */               performer.getCommunicator().sendNormalServerMessage("Your bank account is already open.");
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  502 */             BankManagementQuestion bm = new BankManagementQuestion(performer, "Bank management", "Move bank", target.getWurmId(), bank);
/*  503 */             bm.sendQuestion();
/*      */           } 
/*      */         } else {
/*      */           
/*  507 */           performer.getCommunicator().sendNormalServerMessage("This feature is only available to paying players.");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  512 */       else if (!performer.isGuest()) {
/*      */ 
/*      */         
/*      */         try {
/*  516 */           Village v = Villages.getVillage(villageId);
/*  517 */           if (v.kingdom == performer.getKingdomId()) {
/*      */ 
/*      */             
/*  520 */             BankManagementQuestion bm = new BankManagementQuestion(performer, "Bank management", "Opening bank account", target.getWurmId(), null);
/*  521 */             bm.sendQuestion();
/*      */           } else {
/*      */             
/*  524 */             performer.getCommunicator().sendNormalServerMessage("You can't open a bank here.");
/*      */           } 
/*  526 */         } catch (NoSuchVillageException nsv) {
/*      */           
/*  528 */           logger.log(Level.WARNING, performer.getName() + ":" + nsv.getMessage(), (Throwable)nsv);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  533 */         performer.getCommunicator().sendNormalServerMessage("This feature is only available to paying players.");
/*      */       }
/*      */     
/*  536 */     } else if (action == 226) {
/*      */ 
/*      */ 
/*      */       
/*  540 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 30.0F)) {
/*      */         
/*  542 */         Methods.sendWithdrawMoneyQuestion(performer, target);
/*  543 */         done = true;
/*      */       } else {
/*      */         
/*  546 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/*      */       }
/*      */     
/*  549 */     } else if (action == 194) {
/*      */       
/*  551 */       done = true;
/*  552 */       Methods.sendPlayerPaymentQuestion(performer);
/*      */     }
/*  554 */     else if (action == 1) {
/*      */       
/*  556 */       String descString = target.examine(performer);
/*      */       
/*  558 */       performer.getCommunicator().sendNormalServerMessage(descString);
/*  559 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*  560 */       target.sendExtraStrings(performer.getCommunicator());
/*  561 */       if (target.getData2() > 0) {
/*      */         
/*      */         try {
/*      */           
/*  565 */           Village currVill = Villages.getVillage(target.getData2());
/*  566 */           performer.getCommunicator().sendNormalServerMessage("You spot " + (currVill
/*  567 */               .getGuards()).length + " guards in the vicinity.");
/*      */ 
/*      */           
/*  570 */           if (performer.citizenVillage != null && performer.citizenVillage == currVill)
/*      */           {
/*  572 */             int numNpcTraders = currVill.getTraders().size();
/*  573 */             if (numNpcTraders == 0)
/*      */             {
/*  575 */               performer.getCommunicator().sendNormalServerMessage("There are no traders belonging to this settlement.");
/*      */             
/*      */             }
/*  578 */             else if (numNpcTraders == 1)
/*      */             {
/*  580 */               performer.getCommunicator().sendNormalServerMessage("You spot " + numNpcTraders + " trader belonging to this settlement.");
/*      */             
/*      */             }
/*      */             else
/*      */             {
/*  585 */               performer.getCommunicator().sendNormalServerMessage("You spot " + numNpcTraders + " traders belonging to this settlement.");
/*      */             }
/*      */           
/*      */           }
/*      */         
/*  590 */         } catch (NoSuchVillageException nsv) {
/*      */           
/*  592 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + target
/*  593 */               .getData2());
/*      */         }
/*      */       
/*      */       }
/*  597 */     } else if (action == 334) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  606 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 30.0F)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  614 */         if (Servers.loginServer.isAvailable(5, true)) {
/*      */           
/*  616 */           if (performer.checkLoyaltyProgram()) {
/*      */             
/*  618 */             performer
/*  619 */               .getCommunicator()
/*  620 */               .sendSafeServerMessage("Any Legacy Loyalty Bonus items you are entitled to should arrive soon.");
/*      */           }
/*      */           else {
/*      */             
/*  624 */             performer
/*  625 */               .getCommunicator()
/*  626 */               .sendNormalServerMessage("You have already received any Legacy Loyalty Bonus items that you were entitled to.");
/*      */           } 
/*      */         } else {
/*      */           
/*  630 */           performer
/*  631 */             .getCommunicator()
/*  632 */             .sendNormalServerMessage("The login server is unavailable right now. Please try later.");
/*      */         } 
/*      */       } else {
/*      */         
/*  636 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/*      */       } 
/*  638 */     } else if (action == 355) {
/*      */ 
/*      */       
/*  641 */       KingdomStatusQuestion kq = new KingdomStatusQuestion(performer, "Kingdom status", "Kingdoms", performer.getWurmId());
/*  642 */       kq.sendQuestion();
/*      */     }
/*  644 */     else if (action == 356) {
/*      */ 
/*      */       
/*  647 */       KingdomHistory kq = new KingdomHistory(performer, "Kingdom history", "History of the kingdoms", performer.getWurmId());
/*  648 */       kq.sendQuestion();
/*      */     }
/*  650 */     else if (action == 34) {
/*      */       
/*  652 */       if (performer.getPower() >= 3) {
/*      */         
/*      */         try {
/*      */           
/*  656 */           Village currVill = Villages.getVillage(target.getData2());
/*  657 */           Guard[] guards = currVill.getGuards();
/*  658 */           if (guards.length == 0)
/*      */           {
/*  660 */             performer.getCommunicator().sendNormalServerMessage("There are no guards!");
/*      */           }
/*      */           else
/*      */           {
/*  664 */             for (int x = 0; x < guards.length; x++)
/*      */             {
/*  666 */               performer.getCommunicator().sendNormalServerMessage(guards[x]
/*  667 */                   .getCreature().getName() + " (" + guards[x].getCreature().getWurmId() + ") at " + (
/*  668 */                   (int)guards[x].getCreature().getPosX() >> 2) + ", " + (
/*  669 */                   (int)guards[x].getCreature().getPosY() >> 2) + ", surfaced=" + guards[x]
/*  670 */                   .getCreature().isOnSurface());
/*      */             }
/*      */           }
/*      */         
/*  674 */         } catch (NoSuchVillageException nsv) {
/*      */           
/*  676 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + target
/*  677 */               .getData2());
/*      */         }
/*      */       
/*      */       }
/*  681 */     } else if (action == 371) {
/*      */       
/*  683 */       if (performer.isEconomicAdvisor() || performer.getPower() >= 3)
/*      */       {
/*  685 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 30.0F)) {
/*      */ 
/*      */           
/*  688 */           EconomicAdvisorInfo question = new EconomicAdvisorInfo(performer, "Status sheet", "Economic breakdown", target.getWurmId());
/*  689 */           question.sendQuestion();
/*      */         } else {
/*      */           
/*  692 */           performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/*      */         } 
/*      */       }
/*  695 */     } else if (action == 118 || action == 381 || action == 382) {
/*      */       
/*  697 */       if (performer.getPower() >= 4) {
/*      */         
/*      */         try {
/*      */ 
/*      */           
/*  702 */           Village currVill = Villages.getVillage(target.getData2());
/*  703 */           byte newType = (currVill.kingdom == 3) ? Tiles.Tile.TILE_MYCELIUM_LAWN.id : Tiles.Tile.TILE_LAWN.id;
/*  704 */           for (int x = currVill.getStartX(); x <= currVill.getEndX(); x++) {
/*      */             
/*  706 */             for (int y = currVill.getStartY(); y <= currVill.getEndY(); y++) {
/*      */               
/*  708 */               if (action == 118) {
/*      */ 
/*      */                 
/*  711 */                 int tile = Server.surfaceMesh.getTile(x, y);
/*  712 */                 byte type = Tiles.decodeType(tile);
/*  713 */                 if (!Tiles.isRoadType(type))
/*      */                 {
/*  715 */                   Server.surfaceMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(tile), newType, (byte)0));
/*  716 */                   Server.modifyFlagsByTileType(x, y, newType);
/*  717 */                   Server.setWorldResource(x, y, 0);
/*  718 */                   Players.getInstance().sendChangedTile(x, y, true, true);
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/*  723 */                 Zones.protectedTiles[x][y] = (action == 381);
/*      */               } 
/*      */             } 
/*      */           } 
/*  727 */           if (action == 118)
/*      */           {
/*  729 */             performer.getCommunicator().sendNormalServerMessage("You convert all non pavement tiles on settlement to lawn.");
/*      */           }
/*  731 */           else if (action == 381)
/*      */           {
/*  733 */             performer.getCommunicator().sendNormalServerMessage("You protect the settlement tiles.");
/*      */           }
/*      */           else
/*      */           {
/*  737 */             performer.getCommunicator().sendNormalServerMessage("You unprotect the settlement tiles.");
/*      */           }
/*      */         
/*  740 */         } catch (NoSuchVillageException nsv) {
/*      */           
/*  742 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + target
/*  743 */               .getData2());
/*      */         }
/*      */       
/*      */       }
/*  747 */     } else if (action == 481) {
/*      */       
/*  749 */       done = true;
/*  750 */       int villageId = target.getData2();
/*  751 */       if (villageId <= 0) {
/*      */         
/*  753 */         performer.getCommunicator().sendSafeServerMessage("This settlement is not founded yet!");
/*  754 */         logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */ 
/*      */           
/*  761 */           Village currVill = Villages.getVillage(villageId);
/*  762 */           if (currVill.isActionAllowed((short)68, performer) || performer
/*  763 */             .getPower() >= 2)
/*      */           {
/*  765 */             Methods.sendConfigureTwitter(performer, target.getData2(), true, currVill.getName());
/*      */           }
/*      */           else
/*      */           {
/*  769 */             performer.getCommunicator().sendSafeServerMessage("Illegal option.");
/*  770 */             logger.log(Level.WARNING, performer.getName() + " cheating? Illegal option for " + target.getWurmId() + " at villageid=" + villageId);
/*      */           }
/*      */         
/*      */         }
/*  774 */         catch (NoSuchVillageException nsv)
/*      */         {
/*  776 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  781 */     } else if (action == 209) {
/*      */ 
/*      */       
/*  784 */       done = true;
/*  785 */       int villageId = target.getData2();
/*  786 */       if (performer.getCitizenVillage() != null) {
/*      */ 
/*      */         
/*      */         try {
/*  790 */           Village currVill = Villages.getVillage(villageId);
/*  791 */           if (currVill != null)
/*      */           {
/*  793 */             if (performer.getCitizenVillage().mayDeclareWarOn(currVill)) {
/*      */               
/*  795 */               Methods.sendWarDeclarationQuestion(performer, currVill);
/*      */             }
/*      */             else {
/*      */               
/*  799 */               performer.getCommunicator().sendAlertServerMessage(currVill
/*  800 */                   .getName() + " is already at war with your village.");
/*      */             } 
/*      */           }
/*  803 */         } catch (NoSuchVillageException nsv) {
/*      */           
/*  805 */           logger.log(Level.WARNING, performer.getName() + " managing token with id " + target.getWurmId() + " but villageid=" + villageId);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  810 */         performer.getCommunicator().sendAlertServerMessage("You are no longer a citizen of a village.");
/*      */       } 
/*  812 */     } else if (action == 863) {
/*      */       
/*  814 */       ManageObjectList mol = new ManageObjectList(performer, ManageObjectList.Type.WAGONER);
/*  815 */       mol.sendQuestion();
/*  816 */       done = true;
/*      */     } else {
/*      */       
/*  819 */       done = super.action(act, performer, target, action, counter);
/*  820 */     }  return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  827 */     boolean done = true;
/*  828 */     if (action == 176) {
/*  829 */       Methods.setVillageToken(performer, target);
/*  830 */     } else if (action == 31) {
/*      */       
/*  832 */       if (performer.getCurrentVillage() != null && 
/*  833 */         (performer.getCurrentVillage()).kingdom == performer.getKingdomId())
/*      */       {
/*  835 */         if (!performer.getCurrentVillage().isEnemy(performer))
/*      */         {
/*  837 */           if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 0, 2))
/*      */           {
/*  839 */             done = Methods.discardSellItem(performer, act, source, counter);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  844 */     else if (action == 68 || action == 77 || action == 222 || action == 226 || action == 350 || action == 334 || action == 34 || action == 513 || action == 194 || action == 80 || action == 67 || action == 1 || action == 66 || action == 348 || action == 372 || action == 349 || action == 70 || action == 69 || action == 356 || action == 355 || action == 76 || action == 371 || action == 481 || action == 540 || action == 209) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  856 */       done = action(act, performer, target, action, counter);
/*      */     } else {
/*      */       
/*  859 */       done = super.action(act, performer, source, target, action, counter);
/*  860 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<ActionEntry> getVillageBehaviours(Creature performer, Item target) {
/*  865 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  866 */     int size = -3;
/*  867 */     Village village = performer.getCitizenVillage();
/*      */     
/*      */     try {
/*  870 */       Village currVill = Villages.getVillage(target.getData2());
/*      */       
/*  872 */       toReturn.addAll(getSettlementMenu(performer, performer
/*  873 */             .isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F), village, currVill));
/*      */ 
/*      */       
/*  876 */       size = -3;
/*  877 */       toReturn.add(new ActionEntry((short)size, "Bank", "banking"));
/*  878 */       toReturn.add(Actions.actionEntrys[222]);
/*  879 */       toReturn.add(Actions.actionEntrys[226]);
/*  880 */       toReturn.add(Actions.actionEntrys[194]);
/*      */ 
/*      */ 
/*      */       
/*  884 */       toReturn.add(Actions.actionEntrys[334]);
/*      */       
/*  886 */       if (performer.isEconomicAdvisor() || performer.getPower() >= 3) {
/*  887 */         toReturn.add(Actions.actionEntrys[371]);
/*      */       }
/*  889 */       if (performer.getPower() >= 4 && currVill.isPermanent) {
/*      */         
/*  891 */         size = -2;
/*  892 */         toReturn.add(new ActionEntry((short)size, "Settlement tiles", "special"));
/*  893 */         toReturn.add(new ActionEntry((short)118, "Set to Lawn", "growing"));
/*  894 */         if (Zones.protectedTiles[target.getTileX()][target.getTileY()]) {
/*  895 */           toReturn.add(Actions.actionEntrys[382]);
/*      */         } else {
/*  897 */           toReturn.add(Actions.actionEntrys[381]);
/*      */         } 
/*      */       } 
/*  900 */     } catch (NoSuchVillageException nsv) {
/*      */       
/*  902 */       logger.log(Level.WARNING, "No settlement for token with id " + target.getWurmId() + " at " + target.getPosX() + ", " + target
/*      */           
/*  904 */           .getPosY() + ", surf=" + target.isOnSurface());
/*      */     } 
/*      */     
/*  907 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static List<ActionEntry> getSettlementMenu(Creature performer, boolean closeToToken, Village citizenVillage, Village currentVillage) {
/*  914 */     boolean diplomat = false;
/*  915 */     boolean guards = false;
/*  916 */     boolean destroy = false;
/*  917 */     boolean disbanding = false;
/*  918 */     boolean settings = false;
/*  919 */     boolean roles = false;
/*  920 */     boolean citizens = false;
/*  921 */     boolean reputations = false;
/*  922 */     boolean gates = false;
/*  923 */     boolean resize = false;
/*  924 */     boolean enemy = false;
/*  925 */     boolean findguards = false;
/*  926 */     boolean warmonger = false;
/*  927 */     boolean twitter = false;
/*      */     
/*  929 */     if (citizenVillage != null && citizenVillage.mayDoDiplomacy(performer))
/*  930 */       diplomat = true; 
/*  931 */     if (citizenVillage != null && citizenVillage.mayDoDiplomacy(performer) && currentVillage != null)
/*      */     {
/*  933 */       if (citizenVillage != currentVillage) {
/*      */         
/*  935 */         boolean atPeace = citizenVillage.mayDeclareWarOn(currentVillage);
/*  936 */         if (atPeace)
/*  937 */           warmonger = true; 
/*      */       } 
/*      */     }
/*  940 */     if (citizenVillage != null && citizenVillage.equals(currentVillage) && currentVillage
/*  941 */       .isActionAllowed((short)67, performer))
/*  942 */       guards = true; 
/*  943 */     if (currentVillage.isDisbanding() && currentVillage.equals(citizenVillage) && !performer.isGuest()) {
/*  944 */       disbanding = true;
/*      */     }
/*  946 */     if (citizenVillage != null && citizenVillage.equals(currentVillage)) {
/*      */       
/*  948 */       settings = citizenVillage.isActionAllowed((short)68, performer);
/*  949 */       roles = citizenVillage.isActionAllowed((short)540, performer);
/*  950 */       citizens = citizenVillage.isActionAllowed((short)66, performer);
/*  951 */       reputations = citizenVillage.isActionAllowed((short)69, performer);
/*  952 */       gates = false;
/*  953 */       resize = citizenVillage.isActionAllowed((short)76, performer);
/*  954 */       twitter = citizenVillage.isActionAllowed((short)481, performer);
/*  955 */       if (citizenVillage.isActionAllowed((short)348, performer) && !disbanding && !performer.isGuest())
/*  956 */         destroy = true; 
/*      */     } 
/*  958 */     if (performer.getPower() >= 3) {
/*      */       
/*  960 */       if (!disbanding)
/*  961 */         destroy = true; 
/*  962 */       findguards = true;
/*      */     } 
/*  964 */     if (performer.getPower() >= 4)
/*  965 */       settings = true; 
/*  966 */     if ((performer.isPaying() && currentVillage.kingdom != performer.getKingdomId()) || currentVillage
/*  967 */       .isEnemy(performer.getCitizenVillage())) {
/*  968 */       enemy = true;
/*      */     }
/*  970 */     List<ActionEntry> actionMenu = new LinkedList<>();
/*      */     
/*  972 */     actionMenu.add(Actions.actionEntrys[670]);
/*  973 */     actionMenu.add(Actions.actionEntrys[72]);
/*  974 */     if (destroy && closeToToken)
/*  975 */       actionMenu.add(Actions.actionEntrys[348]); 
/*  976 */     if (enemy)
/*  977 */       actionMenu.add(Actions.actionEntrys[350]); 
/*  978 */     if (findguards)
/*  979 */       actionMenu.add(new ActionEntry((short)34, "Find guards", "finding")); 
/*  980 */     actionMenu.add(Actions.actionEntrys[77]);
/*  981 */     if (King.currentEra > 0)
/*  982 */       actionMenu.add(Actions.actionEntrys[356]); 
/*  983 */     if (King.currentEra > 0)
/*  984 */       actionMenu.add(Actions.actionEntrys[355]); 
/*  985 */     if (citizens)
/*  986 */       actionMenu.add(Actions.actionEntrys[66]); 
/*  987 */     if (gates)
/*  988 */       actionMenu.add(Actions.actionEntrys[70]); 
/*  989 */     if (guards)
/*  990 */       actionMenu.add(Actions.actionEntrys[67]); 
/*  991 */     if (diplomat)
/*  992 */       actionMenu.add(Actions.actionEntrys[80]); 
/*  993 */     if (reputations)
/*  994 */       actionMenu.add(Actions.actionEntrys[69]); 
/*  995 */     if (roles)
/*  996 */       actionMenu.add(Actions.actionEntrys[540]); 
/*  997 */     if (settings) {
/*  998 */       actionMenu.add(Actions.actionEntrys[68]);
/*      */     }
/* 1000 */     if (enemy && currentVillage.kingdom == performer.getKingdomId() && diplomat)
/* 1001 */       actionMenu.add(Actions.actionEntrys[210]); 
/* 1002 */     if (resize)
/* 1003 */       actionMenu.add(Actions.actionEntrys[76]); 
/* 1004 */     if (closeToToken && (currentVillage.isMayor(performer) || performer.getPower() >= 2))
/* 1005 */       actionMenu.add(Actions.actionEntrys[689]); 
/* 1006 */     if (disbanding)
/* 1007 */       actionMenu.add(Actions.actionEntrys[349]); 
/* 1008 */     actionMenu.add(Actions.actionEntrys[71]);
/* 1009 */     if (enemy && (currentVillage.getGuards()).length == 1 && closeToToken)
/* 1010 */       actionMenu.add(Actions.actionEntrys[513]); 
/* 1011 */     if (twitter || performer.getPower() >= 2)
/* 1012 */       actionMenu.add(Actions.actionEntrys[481]); 
/* 1013 */     if (warmonger) {
/* 1014 */       actionMenu.add(Actions.actionEntrys[209]);
/*      */     }
/* 1016 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 1017 */     toReturn.add(new ActionEntry((short)-actionMenu.size(), "Settlement", "Settlement options"));
/* 1018 */     toReturn.addAll(actionMenu);
/*      */     
/* 1020 */     return toReturn;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\VillageTokenBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */