/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.WurmHarvestables;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.InscriptionData;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.Recipe;
/*     */ import com.wurmonline.server.items.RecipesByPlayer;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.questions.CookBookQuestion;
/*     */ import com.wurmonline.server.questions.GMSelectHarvestable;
/*     */ import com.wurmonline.server.questions.ShowHarvestableInfo;
/*     */ import com.wurmonline.server.questions.SimplePopup;
/*     */ import com.wurmonline.server.questions.TextInputQuestion;
/*     */ import com.wurmonline.server.questions.WriteRecipeQuestion;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class PapyrusBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  63 */   private static final Logger logger = Logger.getLogger(PapyrusBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   PapyrusBehaviour() {
/*  67 */     super((short)44);
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
/*     */   List<ActionEntry> getPapyrusBehaviours(Creature performer, @Nullable Item source, Item target) {
/*  81 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */ 
/*     */     
/*  84 */     if (source != null)
/*     */     {
/*  86 */       if (source.isContainerLiquid() || source.getTemplateId() == 14 || source
/*  87 */         .getTemplateId() == 272) {
/*     */         
/*  89 */         if (target.getInscription() == null)
/*     */         {
/*  91 */           toReturn.add(Actions.actionEntrys[505]);
/*     */         }
/*  93 */         if (target.getInscription() == null) {
/*     */           
/*  95 */           Player player = (Player)performer;
/*  96 */           Recipe recipe = player.getViewingRecipe();
/*  97 */           if (recipe != null)
/*     */           {
/*  99 */             toReturn.add(new ActionEntry((short)742, "Write recipe \"" + recipe
/* 100 */                   .getName() + "\"", "writing recipe"));
/*     */           }
/* 102 */           if (player.getStudied() > 0)
/*     */           {
/* 104 */             WurmHarvestables.Harvestable harvestable = WurmHarvestables.getHarvestable(player.getStudied());
/* 105 */             if (harvestable != null)
/*     */             {
/* 107 */               toReturn.add(new ActionEntry((short)853, "Record " + harvestable
/* 108 */                     .getName() + " info", "Recording info"));
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 113 */       } else if (source.getTemplateId() == 176 && performer.getPower() > 2) {
/*     */         
/* 115 */         if (target.getInscription() == null) {
/*     */           
/* 117 */           toReturn.add(new ActionEntry((short)742, "Write recipe", "writing recipe"));
/* 118 */           toReturn.add(new ActionEntry((short)853, "Record harvestable info", "Recording info"));
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 124 */     if (target.canHaveInscription()) {
/*     */       
/* 126 */       InscriptionData ins = target.getInscription();
/* 127 */       if (ins != null && ins.hasBeenInscribed()) {
/*     */         
/* 129 */         if (target.getAuxData() == 0) {
/* 130 */           toReturn.add(Actions.actionEntrys[506]);
/* 131 */         } else if (target.getAuxData() == 1) {
/*     */           
/* 133 */           toReturn.add(Actions.actionEntrys[743]);
/*     */         
/*     */         }
/* 136 */         else if (target.getAuxData() > 8) {
/*     */ 
/*     */           
/* 139 */           WurmHarvestables.Harvestable harvestable = WurmHarvestables.getHarvestable(target.getAuxData() - 8);
/* 140 */           if (harvestable != null) {
/* 141 */             toReturn.add(new ActionEntry((short)17, "Read " + harvestable.getName() + " info", "reading"));
/*     */           }
/*     */         } 
/* 144 */       } else if (source != null && source.getTemplateId() == 1254 && target.getAuxData() == 0) {
/*     */         
/* 146 */         toReturn.add(new ActionEntry((short)118, "Wax", "waxing"));
/*     */       } 
/*     */     } 
/* 149 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/* 155 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/* 156 */     toReturn.addAll(getPapyrusBehaviours(performer, null, target));
/* 157 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/* 163 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/* 164 */     toReturn.addAll(getPapyrusBehaviours(performer, source, target));
/* 165 */     return toReturn;
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
/* 176 */     return performPapyrusAction(act, performer, null, target, action, counter);
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
/* 189 */     return performPapyrusAction(act, performer, source, target, action, counter);
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
/*     */ 
/*     */ 
/*     */   
/*     */   boolean performPapyrusAction(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter) {
/* 208 */     InscriptionData inscriptionData = target.getInscription();
/* 209 */     if (action == 505) {
/*     */       
/* 211 */       if (source == null) {
/*     */ 
/*     */         
/* 214 */         performer.getCommunicator().sendNormalServerMessage("You fumble with the " + target
/* 215 */             .getName() + " but you cannot figure out how it works.");
/* 216 */         return true;
/*     */       } 
/*     */       
/* 219 */       if (!target.canHaveInscription()) {
/*     */         
/* 221 */         performer.getCommunicator().sendNormalServerMessage("You cannot inscribe on that!");
/* 222 */         return true;
/*     */       } 
/*     */       
/* 225 */       if (inscriptionData != null && (performer.getPower() < 2 || target.getAuxData() != 0))
/*     */       {
/* 227 */         if (inscriptionData.hasBeenInscribed()) {
/*     */           
/* 229 */           performer.getCommunicator().sendNormalServerMessage("This " + target
/* 230 */               .getName() + " has already been used by " + inscriptionData.getInscriber() + ".");
/* 231 */           return true;
/*     */         } 
/*     */       }
/* 234 */       if (target.getAuxData() == 2) {
/*     */         
/* 236 */         performer.getCommunicator().sendNormalServerMessage("This " + target
/* 237 */             .getName() + " has a waxy surface and therefore cannot be used to write on.");
/* 238 */         return true;
/*     */       } 
/*     */       
/* 241 */       if (source.getTemplateId() == 14) {
/*     */         
/* 243 */         performer.getCommunicator().sendNormalServerMessage("You push your hand against the " + target
/* 244 */             .getName() + " in hopes of producing an imprint.");
/* 245 */         Server.getInstance().broadCastAction(performer
/* 246 */             .getName() + " presses a hand against the " + target.getName() + " in hopes of producing an imprint.", performer, 5);
/* 247 */         target.setInscription("The dirty hand of " + performer
/* 248 */             .getName() + " vaguely appears on the " + target.getName() + ".", performer
/* 249 */             .getName());
/* 250 */         return true;
/*     */       } 
/* 252 */       if (source.getTemplateId() == 272) {
/*     */         
/* 254 */         performer.getCommunicator().sendNormalServerMessage("You push the bloody face of " + source
/* 255 */             .getName() + " against the " + target.getName() + " in hopes of producing an imprint.");
/*     */         
/* 257 */         Server.getInstance().broadCastAction(performer
/* 258 */             .getName() + " pushes the bloody face of " + source.getName() + " against the " + target.getName() + " in hopes of producing an imprint.", performer, 5);
/* 259 */         target.setInscription("The smeared bloody faceprint of " + source.getName() + " vaguely appears on the " + target
/* 260 */             .getName() + ".", performer.getName());
/* 261 */         return true;
/*     */       } 
/* 263 */       if (!source.isContainerLiquid()) {
/*     */         
/* 265 */         performer.getCommunicator().sendNormalServerMessage("You have no container with liquid active.");
/* 266 */         return true;
/*     */       } 
/*     */       
/* 269 */       Item liquid = null;
/* 270 */       Item contained = null;
/* 271 */       for (Iterator<Item> it = source.getItems().iterator(); it.hasNext(); ) {
/*     */         
/* 273 */         contained = it.next();
/* 274 */         if (!isValidColorant(contained.getTemplateId())) {
/*     */           
/* 276 */           performer.getCommunicator().sendNormalServerMessage("You cannot inscribe with " + contained
/* 277 */               .getName() + ". You need to use something special for writing on " + target.getName() + ".");
/* 278 */           return true;
/*     */         } 
/* 280 */         liquid = contained;
/*     */       } 
/* 282 */       if (liquid == null) {
/*     */         
/* 284 */         performer.getCommunicator().sendNormalServerMessage("You need a colorant to be able to inscribe with the " + source
/* 285 */             .getName() + " on the " + target
/* 286 */             .getName() + ".");
/* 287 */         return true;
/*     */       } 
/* 289 */       short numChars = 0;
/* 290 */       float inkQlBonus = liquid.getQualityLevel();
/* 291 */       float targetQlBonus = target.getQualityLevel();
/*     */       
/* 293 */       switch (source.getTemplateId()) {
/*     */         
/*     */         case 749:
/* 296 */           numChars = (short)(numChars + getNumCharBonusForInk(liquid.getTemplateId()));
/* 297 */           numChars = (short)(numChars + (short)(int)(2.0F * source.getQualityLevel() + 2.0F * inkQlBonus));
/* 298 */           numChars = (short)(numChars + source.getRarity() * 40);
/* 299 */           numChars = (short)(int)(numChars + targetQlBonus * 2.0F);
/*     */           break;
/*     */         case 5:
/* 302 */           numChars = (short)(int)(4.0F + source.getQualityLevel() + inkQlBonus);
/* 303 */           numChars = (short)(numChars + target.getRarity() * 10);
/*     */           break;
/*     */         case 78:
/* 306 */           numChars = (short)(int)(4.0D + 0.5D * source.getQualityLevel() + 0.5D * inkQlBonus + 0.5D * targetQlBonus);
/* 307 */           numChars = (short)(numChars + target.getRarity() * 10);
/*     */           break;
/*     */         case 653:
/* 310 */           numChars = (short)(int)(4.0D + 0.5D * source.getQualityLevel() + 0.5D * inkQlBonus + 0.5D * targetQlBonus);
/* 311 */           numChars = (short)(numChars + target.getRarity() * 10);
/*     */           break;
/*     */         case 76:
/* 314 */           numChars = (short)(int)(2.0D + 0.5D * source.getQualityLevel() + 0.5D * inkQlBonus + 0.5D * targetQlBonus);
/* 315 */           numChars = (short)(numChars + target.getRarity() * 10);
/*     */           break;
/*     */         
/*     */         default:
/* 319 */           numChars = 0; break;
/*     */       } 
/* 321 */       if (numChars <= 0) {
/*     */         
/* 323 */         performer.getCommunicator().sendNormalServerMessage("You would only make a mess with the " + source
/* 324 */             .getName() + " on the " + target.getName() + ". Try to use a more delicate tool when inscribing.");
/*     */         
/* 326 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 330 */       numChars = (short)Math.min(500, numChars / 2);
/*     */ 
/*     */       
/* 333 */       TextInputQuestion tiq = new TextInputQuestion(performer, "Inscribing a message on " + target.getName() + ".", "Inscribing is an irreversible process. Enter your important message here:", 2, target.getWurmId(), numChars, false);
/* 334 */       Server.getInstance().broadCastAction(performer
/* 335 */           .getName() + " starts to inscribe with " + source.getName() + " on " + target.getNameWithGenus() + ".", performer, 5);
/* 336 */       tiq.setLiquid(liquid);
/*     */       
/* 338 */       tiq.sendQuestion();
/* 339 */       return true;
/*     */     } 
/* 341 */     if (action == 506) {
/*     */       
/* 343 */       if (inscriptionData != null) {
/*     */         
/* 345 */         SimplePopup pp = new SimplePopup(performer, target.getName(), inscriptionData);
/*     */         
/* 347 */         performer.getCommunicator().sendNormalServerMessage("You read the " + target.getName() + ".");
/* 348 */         pp.sendQuestion("Close");
/*     */       }
/*     */       else {
/*     */         
/* 352 */         performer.getCommunicator().sendNormalServerMessage("There was no inscription to read.");
/*     */       } 
/* 354 */       return true;
/*     */     } 
/* 356 */     if (action == 83 || action == 180) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 365 */       if (performer.mayDestroy(target) || performer.getPower() > 0) {
/* 366 */         return MethodsItems.destroyItem(action, performer, source, target, false, counter);
/*     */       }
/* 368 */       return true;
/*     */     } 
/* 370 */     if (action == 185) {
/*     */       
/* 372 */       if (performer.getPower() >= 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 381 */         performer.getCommunicator().sendNormalServerMessage("It is made from " + 
/* 382 */             MaterialUtilities.getMaterialString(target.getMaterial()) + " (" + target
/* 383 */             .getMaterial() + ") " + ".");
/* 384 */         performer.getCommunicator().sendNormalServerMessage("WurmId:" + target
/* 385 */             .getWurmId() + ", posx=" + target.getPosX() + "(" + ((int)target.getPosX() >> 2) + "), posy=" + target
/* 386 */             .getPosY() + "(" + ((int)target.getPosY() >> 2) + "), posz=" + target
/* 387 */             .getPosZ() + ", rot" + target.getRotation() + " layer=" + (
/* 388 */             target.isOnSurface() ? 0 : -1));
/* 389 */         performer.getCommunicator().sendNormalServerMessage("Ql:" + target
/* 390 */             .getQualityLevel() + ", damage=" + target.getDamage() + ", weight=" + target
/* 391 */             .getWeightGrams() + ", temp=" + target.getTemperature());
/* 392 */         performer.getCommunicator().sendNormalServerMessage("parentid=" + target
/* 393 */             .getParentId() + " ownerid=" + target.getOwnerId() + " zoneid=" + target
/* 394 */             .getZoneId() + " sizex=" + target.getSizeX() + ", sizey=" + target.getSizeY() + " sizez=" + target
/* 395 */             .getSizeZ() + ".");
/* 396 */         if (inscriptionData != null) {
/*     */           
/* 398 */           performer.getCommunicator().sendNormalServerMessage("Inscription: '" + inscriptionData.getInscription() + "'");
/* 399 */           performer.getCommunicator().sendNormalServerMessage("Inscriber: '" + inscriptionData.getInscriber() + "'");
/*     */         }
/*     */         else {
/*     */           
/* 403 */           performer.getCommunicator().sendNormalServerMessage("Inscription: NO INSCRIPTION!");
/* 404 */           if (target.getAuxData() == 2) {
/*     */             
/* 406 */             performer.getCommunicator().sendNormalServerMessage("This " + target
/* 407 */                 .getName() + " has a waxy surface.");
/* 408 */             return true;
/*     */           } 
/*     */         } 
/* 411 */         long timeSince = WurmCalendar.currentTime - target.getLastMaintained();
/*     */         
/* 413 */         String timeString = Server.getTimeFor(timeSince * 1000L);
/* 414 */         performer.getCommunicator().sendNormalServerMessage("Last maintained " + timeString + " ago.");
/* 415 */         String lastOwnerS = String.valueOf(target.lastOwner);
/* 416 */         PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(target.getLastOwnerId());
/* 417 */         if (p != null) {
/* 418 */           lastOwnerS = p.getName();
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */             
/* 424 */             Creature c = Creatures.getInstance().getCreature(target.lastOwner);
/* 425 */             lastOwnerS = c.getName();
/*     */           }
/* 427 */           catch (NoSuchCreatureException nsc) {
/*     */             
/* 429 */             lastOwnerS = "dead " + lastOwnerS;
/*     */           } 
/*     */         } 
/* 432 */         performer.getCommunicator().sendNormalServerMessage("lastownerid=" + lastOwnerS + ", Model=" + target
/* 433 */             .getModelName());
/* 434 */         if (performer.getPower() >= 5)
/*     */         {
/* 436 */           performer.getCommunicator().sendNormalServerMessage("Zoneid=" + target
/* 437 */               .getZoneId() + " real zid=" + target.zoneId + " Counter=" + 
/* 438 */               WurmId.getNumber(target.getWurmId()) + " origin=" + 
/* 439 */               WurmId.getOrigin(target.getWurmId()));
/*     */         }
/* 441 */         if (target.hasData())
/* 442 */           performer.getCommunicator().sendNormalServerMessage("data=" + target
/* 443 */               .getData() + ", data1=" + target.getData1() + " data2=" + target.getData2()); 
/* 444 */         String creator = ", creator=" + target.creator;
/* 445 */         if (target.creator == null || target.creator.length() == 0)
/* 446 */           creator = ""; 
/* 447 */         performer.getCommunicator().sendNormalServerMessage("auxdata=" + target.getAuxData() + creator);
/* 448 */         if (target.isKey())
/* 449 */           performer.getCommunicator().sendNormalServerMessage("lock id=" + target.getLockId()); 
/* 450 */         if (target.isLock()) {
/*     */           
/* 452 */           long[] keys = target.getKeyIds();
/* 453 */           performer.getCommunicator().sendNormalServerMessage("Keys:");
/* 454 */           for (long lKey : keys)
/* 455 */             performer.getCommunicator().sendNormalServerMessage(String.valueOf(lKey)); 
/*     */         } 
/*     */       } 
/* 458 */       return true;
/*     */     } 
/* 460 */     if (action == 1) {
/*     */       
/* 462 */       StringBuilder sendString = new StringBuilder(target.examine(performer));
/* 463 */       if (inscriptionData != null && inscriptionData.hasBeenInscribed() == true) {
/*     */         
/* 465 */         if (target.getAuxData() > 8) {
/* 466 */           sendString.append(" It appears to be a " + target.getName() + ", which should contain its harvest times.");
/*     */         } else {
/* 468 */           sendString.append(" It appears the " + target.getName() + " has something written on it.");
/* 469 */         }  if (target.getQualityLevel() > 20.0F)
/*     */         {
/* 471 */           if (inscriptionData.getInscriber() != null) {
/* 472 */             sendString.append(" The author appears to be someone named '" + inscriptionData.getInscriber() + "'.");
/*     */           }
/*     */         }
/*     */       } else {
/*     */         
/* 477 */         sendString.append(" It is possible to inscribe on the " + target.getName() + " if you know how.");
/*     */       } 
/* 479 */       String s = target.getSignature();
/* 480 */       if (s != null && s.length() > 0) {
/* 481 */         sendString.append("You can barely make out the signature of its maker,  '" + s + "'.");
/*     */       }
/* 483 */       performer.getCommunicator().sendNormalServerMessage(sendString.toString());
/* 484 */       return true;
/*     */     } 
/* 486 */     if (action == 742)
/*     */     {
/* 488 */       return writeRecipe(act, performer, source, target, action, counter, inscriptionData);
/*     */     }
/* 490 */     if (action == 853)
/*     */     {
/* 492 */       return recordInfo(act, performer, source, target, action, counter, inscriptionData);
/*     */     }
/* 494 */     if (action == 743) {
/*     */       
/* 496 */       if (inscriptionData != null && target.getAuxData() == 1) {
/*     */ 
/*     */         
/* 499 */         Recipe recipe = inscriptionData.getRecipe();
/* 500 */         if (recipe != null) {
/*     */           
/* 502 */           performer.getCommunicator().sendNormalServerMessage("You read the " + target.getName() + ".");
/*     */           
/* 504 */           CookBookQuestion pp = new CookBookQuestion(performer, (byte)13, recipe, true, target.getWurmId(), inscriptionData.getInscriber());
/* 505 */           pp.sendQuestion();
/*     */         } else {
/*     */           
/* 508 */           performer.getCommunicator().sendNormalServerMessage("You could not decipher the recipe.");
/*     */         } 
/*     */       } else {
/*     */         
/* 512 */         performer.getCommunicator().sendNormalServerMessage("There was no recipe to read.");
/*     */       } 
/* 514 */       return true;
/*     */     } 
/* 516 */     if (action == 17) {
/*     */       
/* 518 */       if (inscriptionData != null && target.getAuxData() > 8) {
/*     */ 
/*     */         
/* 521 */         WurmHarvestables.Harvestable harvestable = WurmHarvestables.getHarvestable(target.getAuxData() - 8);
/* 522 */         if (harvestable != null) {
/*     */           
/* 524 */           performer.getCommunicator().sendNormalServerMessage("You read the " + target.getName() + ".");
/* 525 */           ShowHarvestableInfo pp = new ShowHarvestableInfo(performer, target, harvestable);
/* 526 */           pp.sendQuestion();
/*     */         } else {
/*     */           
/* 529 */           performer.getCommunicator().sendNormalServerMessage("You could not decipher the report.");
/*     */         } 
/*     */       } else {
/*     */         
/* 533 */         performer.getCommunicator().sendNormalServerMessage("There was no report to read.");
/*     */       } 
/* 535 */       return true;
/*     */     } 
/* 537 */     if (action == 118 && source.getTemplateId() == 1254 && target.canHaveInscription()) {
/*     */       
/* 539 */       if (inscriptionData != null)
/*     */       {
/* 541 */         if (inscriptionData.hasBeenInscribed()) {
/*     */           
/* 543 */           performer.getCommunicator().sendNormalServerMessage("This " + target
/* 544 */               .getName() + " has already been used by " + inscriptionData.getInscriber() + ".");
/* 545 */           return true;
/*     */         } 
/*     */       }
/* 548 */       if (target.getAuxData() == 2) {
/*     */         
/* 550 */         performer.getCommunicator().sendNormalServerMessage("This " + target
/* 551 */             .getName() + " has a waxy surface and does not need more wax.");
/* 552 */         return true;
/*     */       } 
/* 554 */       if (inscriptionData == null && target.getAuxData() == 0) {
/*     */ 
/*     */         
/* 557 */         if (counter == 1.0F) {
/*     */           
/* 559 */           int time = 50;
/* 560 */           performer.getCommunicator().sendNormalServerMessage("You start to wax the " + target
/* 561 */               .getActualName() + ".");
/* 562 */           Server.getInstance().broadCastAction(performer
/* 563 */               .getName() + " starts to wax something.", performer, 5);
/* 564 */           performer.sendActionControl("Waxing " + target.getActualName(), true, 50);
/* 565 */           performer.getStatus().modifyStamina(-400.0F);
/* 566 */           act.setTimeLeft(50);
/*     */         } 
/* 568 */         if (counter * 10.0F > act.getTimeLeft()) {
/*     */           
/* 570 */           performer.getCommunicator().sendNormalServerMessage("You finish waxing the " + target
/* 571 */               .getActualName() + ".");
/* 572 */           Server.getInstance().broadCastAction(performer
/* 573 */               .getName() + " stops waxing.", performer, 5);
/* 574 */           target.setAuxData((byte)2);
/* 575 */           target.updateName();
/* 576 */           source.setWeight(source.getWeightGrams() - 10, true);
/* 577 */           return true;
/*     */         } 
/* 579 */         return false;
/*     */       } 
/* 581 */       return true;
/*     */     } 
/*     */     
/* 584 */     if (source == null) {
/* 585 */       return super.action(act, performer, target, action, counter);
/*     */     }
/* 587 */     return super.action(act, performer, source, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean writeRecipe(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter, InscriptionData inscriptionData) {
/* 593 */     if (source == null) {
/*     */ 
/*     */       
/* 596 */       performer.getCommunicator().sendNormalServerMessage("You fumble with the " + target
/* 597 */           .getName() + " but you cannot figure out how it works.");
/* 598 */       return true;
/*     */     } 
/*     */     
/* 601 */     if (!target.canHaveInscription()) {
/*     */       
/* 603 */       performer.getCommunicator().sendNormalServerMessage("You cannot write a recipe on that!");
/* 604 */       return true;
/*     */     } 
/* 606 */     if (inscriptionData != null)
/*     */     {
/* 608 */       if (inscriptionData.hasBeenInscribed()) {
/*     */         
/* 610 */         performer.getCommunicator().sendNormalServerMessage("This " + target
/* 611 */             .getName() + " has already been used by " + inscriptionData.getInscriber() + ".");
/* 612 */         return true;
/*     */       } 
/*     */     }
/* 615 */     if (target.getAuxData() == 2) {
/*     */       
/* 617 */       performer.getCommunicator().sendNormalServerMessage("This " + target
/* 618 */           .getName() + " has a waxy surface and therefore cannot be used to write on.");
/* 619 */       return true;
/*     */     } 
/* 621 */     if (target.getAuxData() > 8) {
/*     */       
/* 623 */       performer.getCommunicator().sendNormalServerMessage("This " + target
/* 624 */           .getName() + " has some study results on and therefore cannot be used again.");
/* 625 */       return true;
/*     */     } 
/* 627 */     if (source.getTemplateId() == 176) {
/*     */ 
/*     */       
/* 630 */       WriteRecipeQuestion pp = new WriteRecipeQuestion(performer, target);
/* 631 */       pp.sendQuestion();
/* 632 */       return true;
/*     */     } 
/* 634 */     if (!source.isContainerLiquid()) {
/*     */       
/* 636 */       performer.getCommunicator().sendNormalServerMessage("You have no container with liquid active.");
/* 637 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 642 */     Item liquid = null;
/* 643 */     Item contained = null;
/* 644 */     for (Iterator<Item> it = source.getItems().iterator(); it.hasNext(); ) {
/*     */       
/* 646 */       contained = it.next();
/* 647 */       if (!isValidColorant(contained.getTemplateId())) {
/*     */         
/* 649 */         performer.getCommunicator().sendNormalServerMessage("You cannot write a recipe with " + contained
/* 650 */             .getName() + ". You need to use something special for writing on " + target.getName() + ".");
/* 651 */         return true;
/*     */       } 
/* 653 */       liquid = contained;
/*     */     } 
/* 655 */     if (liquid == null) {
/*     */       
/* 657 */       performer.getCommunicator().sendNormalServerMessage("You need a colorant to be able to write a recipe with the " + source
/* 658 */           .getName() + " on the " + target
/* 659 */           .getName() + ".");
/* 660 */       return true;
/*     */     } 
/* 662 */     Recipe recipe = ((Player)performer).getViewingRecipe();
/* 663 */     if (recipe == null) {
/*     */       
/* 665 */       performer.getCommunicator().sendNormalServerMessage("You need to be viewing a recipe to be able to write it.");
/*     */       
/* 667 */       return true;
/*     */     } 
/* 669 */     if (recipe.isKnown()) {
/*     */       
/* 671 */       performer.getCommunicator().sendNormalServerMessage("Everyone knows that recipe, seems a waste of time to write it down.");
/*     */       
/* 673 */       return true;
/*     */     } 
/* 675 */     if (recipe.isLootable()) {
/*     */       
/* 677 */       performer.getCommunicator().sendNormalServerMessage("You cannot bring yourself to write that recipe down.");
/*     */       
/* 679 */       return true;
/*     */     } 
/*     */     
/* 682 */     if (counter == 1.0F) {
/*     */       
/* 684 */       ((Player)performer).setIsWritingRecipe(true);
/* 685 */       int time = 50;
/* 686 */       performer.getCommunicator().sendNormalServerMessage("You start to write the recipe for \"" + recipe
/* 687 */           .getName() + "\" on the " + target.getActualName() + ".");
/* 688 */       Server.getInstance().broadCastAction(performer
/* 689 */           .getName() + " starts to write a recipe down.", performer, 5);
/* 690 */       performer.sendActionControl("Writing " + recipe.getName(), true, 50);
/* 691 */       performer.getStatus().modifyStamina(-400.0F);
/* 692 */       act.setTimeLeft(50);
/*     */     } 
/* 694 */     if (counter * 10.0F > act.getTimeLeft()) {
/*     */       
/* 696 */       target.setInscription(recipe, performer.getName(), liquid.getColor());
/* 697 */       performer.getCommunicator().sendNormalServerMessage("You carefully finish writing the recipe \"" + recipe
/* 698 */           .getName() + "\" and sign it.");
/* 699 */       Server.getInstance().broadCastAction(performer
/* 700 */           .getName() + " stops writing.", performer, 5);
/* 701 */       if (liquid != null)
/* 702 */         liquid.setWeight(liquid.getWeightGrams() - 10, true); 
/* 703 */       ((Player)performer).setIsWritingRecipe(false);
/* 704 */       return true;
/*     */     } 
/* 706 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean recordInfo(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter, InscriptionData inscriptionData) {
/* 713 */     Player player = (Player)performer;
/* 714 */     if (source == null) {
/*     */ 
/*     */       
/* 717 */       performer.getCommunicator().sendNormalServerMessage("You fumble with the " + target
/* 718 */           .getName() + " but you cannot figure out how it works.");
/* 719 */       return true;
/*     */     } 
/*     */     
/* 722 */     if (!target.canHaveInscription()) {
/*     */       
/* 724 */       performer.getCommunicator().sendNormalServerMessage("You cannot write a recipe on that!");
/* 725 */       return true;
/*     */     } 
/* 727 */     if (inscriptionData != null)
/*     */     {
/* 729 */       if (inscriptionData.hasBeenInscribed()) {
/*     */         
/* 731 */         performer.getCommunicator().sendNormalServerMessage("This " + target
/* 732 */             .getName() + " has already been used by " + inscriptionData.getInscriber() + ".");
/* 733 */         return true;
/*     */       } 
/*     */     }
/* 736 */     if (target.getAuxData() == 2) {
/*     */       
/* 738 */       performer.getCommunicator().sendNormalServerMessage("This " + target
/* 739 */           .getName() + " has a waxy surface and therefore cannot be used to write on.");
/* 740 */       return true;
/*     */     } 
/* 742 */     if (target.getAuxData() > 8) {
/*     */       
/* 744 */       performer.getCommunicator().sendNormalServerMessage("This " + target
/* 745 */           .getName() + " already has some study results on and therefore cannot be used again.");
/* 746 */       return true;
/*     */     } 
/* 748 */     int studied = player.getStudied();
/* 749 */     if (studied == 0) {
/*     */       
/* 751 */       if (source.getTemplateId() == 176) {
/*     */ 
/*     */         
/* 754 */         GMSelectHarvestable pp = new GMSelectHarvestable(performer, target);
/* 755 */         pp.sendQuestion();
/* 756 */         return true;
/*     */       } 
/* 758 */       performer.getCommunicator().sendNormalServerMessage("You need to have just studied something to be able to record the results.");
/*     */       
/* 760 */       return true;
/*     */     } 
/* 762 */     if (!source.isContainerLiquid()) {
/*     */       
/* 764 */       performer.getCommunicator().sendNormalServerMessage("You have no container with liquid active.");
/* 765 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 770 */     Item liquid = null;
/* 771 */     Item contained = null;
/* 772 */     for (Iterator<Item> it = source.getItems().iterator(); it.hasNext(); ) {
/*     */       
/* 774 */       contained = it.next();
/* 775 */       if (!isValidColorant(contained.getTemplateId())) {
/*     */         
/* 777 */         performer.getCommunicator().sendNormalServerMessage("You cannot write a study report with " + contained
/* 778 */             .getName() + ". You need to use something special for writing on " + target.getName() + ".");
/* 779 */         return true;
/*     */       } 
/* 781 */       liquid = contained;
/*     */     } 
/* 783 */     if (liquid == null) {
/*     */       
/* 785 */       performer.getCommunicator().sendNormalServerMessage("You need a colorant to be able to write a recipe with the " + source
/* 786 */           .getName() + " on the " + target
/* 787 */           .getName() + ".");
/* 788 */       return true;
/*     */     } 
/* 790 */     WurmHarvestables.Harvestable harvestable = WurmHarvestables.getHarvestable(player.getStudied());
/* 791 */     if (harvestable == null) {
/*     */       
/* 793 */       performer.getCommunicator().sendNormalServerMessage("You need to have studied something to be able to write a report about it.");
/*     */       
/* 795 */       return true;
/*     */     } 
/* 797 */     if (counter == 1.0F) {
/*     */       
/* 799 */       int time = 50;
/* 800 */       performer.getCommunicator().sendNormalServerMessage("You start to write a report for \"" + harvestable
/* 801 */           .getName() + "\" on the " + target.getActualName() + ".");
/* 802 */       Server.getInstance().broadCastAction(performer
/* 803 */           .getName() + " starts to write a report.", performer, 5);
/* 804 */       performer.sendActionControl("Writing " + harvestable.getName(), true, 50);
/* 805 */       performer.getStatus().modifyStamina(-400.0F);
/* 806 */       act.setTimeLeft(50);
/*     */     } 
/* 808 */     if (counter * 10.0F > act.getTimeLeft()) {
/*     */       
/* 810 */       Skill forestry = performer.getSkills().getSkillOrLearn(10048);
/* 811 */       float skillMultiplier = Math.min(Math.max(1.0F, counter / 3.0F), 20.0F);
/* 812 */       float alc = 0.0F;
/* 813 */       if (performer.isPlayer())
/* 814 */         alc = ((Player)performer).getAlcohol(); 
/* 815 */       int diff = harvestable.getReportDifficulty();
/* 816 */       float power = (float)forestry.skillCheck((diff + alc), target, 0.0D, false, skillMultiplier);
/* 817 */       float knowledge = (float)forestry.getKnowledge(0.0D);
/* 818 */       float modify = (100.0F - knowledge) * power / 1000.0F - alc / 10.0F;
/* 819 */       float newQL = Math.min(100.0F, Math.max(1.0F, knowledge + modify));
/*     */       
/* 821 */       performer.getCommunicator().sendNormalServerMessage("You carefully finish writing the report about \"" + harvestable
/* 822 */           .getName() + "\" and sign it.");
/* 823 */       Server.getInstance().broadCastAction(performer
/* 824 */           .getName() + " stops writing.", performer, 5);
/* 825 */       player.setStudied(0);
/* 826 */       target.setAuxData((byte)(studied + 8));
/*     */       
/* 828 */       target.setOriginalQualityLevel(newQL);
/* 829 */       target.setQualityLevel(newQL);
/* 830 */       target.setDamage(0.0F);
/*     */       
/* 832 */       target.setInscription(harvestable.getName() + " report", player.getName(), liquid.getColor());
/* 833 */       target.setName(harvestable.getName() + " report", true);
/* 834 */       if (liquid != null)
/* 835 */         liquid.setWeight(liquid.getWeightGrams() - 10, true); 
/* 836 */       return true;
/*     */     } 
/* 838 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidColorant(int templateId) {
/* 848 */     switch (templateId) {
/*     */       
/*     */       case 431:
/*     */       case 432:
/*     */       case 433:
/*     */       case 434:
/*     */       case 435:
/*     */       case 438:
/*     */       case 753:
/* 857 */         return true;
/*     */     } 
/* 859 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static short getNumCharBonusForInk(int templateId) {
/* 870 */     switch (templateId) {
/*     */       
/*     */       case 753:
/* 873 */         return 200;
/*     */       case 431:
/* 875 */         return 200;
/*     */       case 432:
/*     */       case 433:
/*     */       case 434:
/*     */       case 435:
/* 880 */         return 20;
/*     */       case 438:
/* 882 */         return 20;
/*     */     } 
/* 884 */     return 0;
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
/*     */   static List<ActionEntry> getPapyrusBehavioursFor(Creature performer, Item source) {
/* 896 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 897 */     if (source.canHaveInscription() && source.getParentId() != -10L) {
/*     */ 
/*     */       
/* 900 */       InscriptionData inscriptionData = source.getInscription();
/* 901 */       if (inscriptionData != null && source.getAuxData() == 1) {
/*     */ 
/*     */         
/* 904 */         Recipe recipe = inscriptionData.getRecipe();
/* 905 */         if (recipe != null)
/*     */         {
/* 907 */           toReturn.add(new ActionEntry((short)744, "Add \"" + recipe
/* 908 */                 .getName() + "\" to cookbook", "adding recipe"));
/*     */         }
/*     */       } 
/*     */     } 
/* 912 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean addToCookbook(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 919 */     InscriptionData inscriptionData = source.getInscription();
/* 920 */     if (inscriptionData != null && source.getAuxData() == 1) {
/*     */ 
/*     */       
/* 923 */       Recipe recipe = inscriptionData.getRecipe();
/* 924 */       if (recipe != null) {
/*     */         
/* 926 */         if (RecipesByPlayer.isKnownRecipe(performer.getWurmId(), recipe.getRecipeId())) {
/*     */           
/* 928 */           performer.getCommunicator().sendNormalServerMessage("As you look for a place to add the recipe in your cookbook, you notice that recipe already exists in your cookbook, therefore decide to stop!");
/*     */           
/* 930 */           return true;
/*     */         } 
/* 932 */         if (counter == 1.0F) {
/*     */           
/* 934 */           int time = 50;
/* 935 */           performer.getCommunicator().sendNormalServerMessage("You start to add the recipe " + source
/* 936 */               .getName() + " into your cookbook.");
/* 937 */           Server.getInstance().broadCastAction(performer
/* 938 */               .getName() + " starts to write a recipe down.", performer, 5);
/* 939 */           performer.sendActionControl("Writing " + recipe.getName(), true, 50);
/* 940 */           performer.getStatus().modifyStamina(-400.0F);
/* 941 */           act.setTimeLeft(50);
/*     */         } 
/* 943 */         if (counter * 10.0F > act.getTimeLeft()) {
/*     */           
/* 945 */           if (RecipesByPlayer.addRecipe(performer, recipe)) {
/*     */             
/* 947 */             performer.getCommunicator().sendNormalServerMessage("You finish adding the " + source
/* 948 */                 .getName() + " into your cookbook, just in time, as the recipe has decayed away.");
/* 949 */             Server.getInstance().broadCastAction(performer
/* 950 */                 .getName() + " stops writing.", performer, 5);
/*     */             
/* 952 */             Items.destroyItem(source.getWurmId());
/*     */           } 
/* 954 */           return true;
/*     */         } 
/* 956 */         return false;
/*     */       } 
/*     */     } 
/* 959 */     performer.getCommunicator().sendNormalServerMessage("That " + source.getName() + " does not have a recipe on it");
/* 960 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\PapyrusBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */