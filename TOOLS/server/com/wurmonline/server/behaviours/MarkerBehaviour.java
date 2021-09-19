/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Delivery;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.highways.HighwayPos;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.highways.Routes;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.questions.FindRouteQuestion;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.HighwayConstants;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MarkerBehaviour
/*     */   extends ItemBehaviour
/*     */   implements HighwayConstants
/*     */ {
/*  68 */   private static final Logger logger = Logger.getLogger(MarkerBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   public MarkerBehaviour() {
/*  72 */     super((short)56);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  78 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  79 */     toReturn.addAll(getBehavioursForMarker(performer, null, target));
/*  80 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  86 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  87 */     toReturn.addAll(getBehavioursForMarker(performer, source, target));
/*  88 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  94 */     boolean[] ans = markerAction(act, performer, null, target, action, counter);
/*  95 */     if (ans[0])
/*  96 */       return ans[1]; 
/*  97 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 103 */     boolean[] ans = markerAction(act, performer, source, target, action, counter);
/* 104 */     if (ans[0])
/* 105 */       return ans[1]; 
/* 106 */     return super.action(act, performer, source, target, action, counter);
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
/*     */   private List<ActionEntry> getBehavioursForMarker(Creature performer, @Nullable Item source, Item target) {
/* 119 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 120 */     if (target.isRoadMarker() && target.isPlanted() && Features.Feature.HIGHWAYS.isEnabled()) {
/*     */ 
/*     */       
/* 123 */       toReturn.add(Actions.actionEntrys[759]);
/* 124 */       int linkCount = MethodsHighways.numberOfSetBits(target.getAuxData());
/* 125 */       if (linkCount > 0) {
/* 126 */         toReturn.add(Actions.actionEntrys[748]);
/*     */       }
/*     */       
/* 129 */       if (target.getTemplateId() == 1112 || linkCount < 2) {
/*     */         
/* 131 */         List<ActionEntry> dirs = new LinkedList<>();
/* 132 */         byte possibles = MethodsHighways.getPossibleLinksFrom(target);
/*     */         
/* 134 */         if (MethodsHighways.hasLink(possibles, (byte)1))
/* 135 */           dirs.add(Actions.actionEntrys[749]); 
/* 136 */         if (MethodsHighways.hasLink(possibles, (byte)2))
/* 137 */           dirs.add(Actions.actionEntrys[750]); 
/* 138 */         if (MethodsHighways.hasLink(possibles, (byte)4))
/* 139 */           dirs.add(Actions.actionEntrys[751]); 
/* 140 */         if (MethodsHighways.hasLink(possibles, (byte)8))
/* 141 */           dirs.add(Actions.actionEntrys[752]); 
/* 142 */         if (MethodsHighways.hasLink(possibles, (byte)16))
/* 143 */           dirs.add(Actions.actionEntrys[753]); 
/* 144 */         if (MethodsHighways.hasLink(possibles, (byte)32))
/* 145 */           dirs.add(Actions.actionEntrys[754]); 
/* 146 */         if (MethodsHighways.hasLink(possibles, (byte)64))
/* 147 */           dirs.add(Actions.actionEntrys[755]); 
/* 148 */         if (MethodsHighways.hasLink(possibles, -128)) {
/* 149 */           dirs.add(Actions.actionEntrys[756]);
/*     */         }
/* 151 */         if (dirs.size() > 0) {
/*     */           
/* 153 */           toReturn.add(new ActionEntry((short)-dirs.size(), "Add Link to", "linking"));
/* 154 */           toReturn.addAll(dirs);
/*     */         } 
/*     */       } 
/* 157 */       if (target.getTemplateId() == 1112) {
/*     */         
/* 159 */         toReturn.add(Actions.actionEntrys[758]);
/* 160 */         if (Features.Feature.WAGONER.isEnabled()) {
/*     */           
/* 162 */           Village waystoneVillage = Villages.getVillage(target.getTilePos(), target.isOnSurface());
/* 163 */           if (waystoneVillage != null && (waystoneVillage.isActionAllowed((short)85, performer) || (performer
/* 164 */             .getPower() >= 4 && waystoneVillage.isPermanent)))
/*     */           {
/* 166 */             if (source != null && source.getTemplateId() == 1129 && source.getData() == -1L && 
/* 167 */               !target.isWagonerCamp() && MethodsHighways.numberOfSetBits(target.getAuxData()) == 1)
/*     */             {
/* 169 */               toReturn.add(new ActionEntry((short)863, "Set up wagoner camp", "setting up wagoner camp"));
/*     */             }
/*     */           }
/* 172 */           if (waystoneVillage != null && (waystoneVillage.isActionAllowed((short)176, performer) || (performer
/* 173 */             .getPower() >= 4 && waystoneVillage.isPermanent))) {
/*     */             
/* 175 */             if (source != null && source.getTemplateId() == 1309 && !target.isWagonerCamp())
/*     */             {
/* 177 */               toReturn.add(new ActionEntry((short)176, "Plant", "planting"));
/*     */             }
/*     */           }
/* 180 */           else if (waystoneVillage == null && source != null && source.getTemplateId() == 1309 && 
/* 181 */             !target.isWagonerCamp()) {
/*     */ 
/*     */ 
/*     */             
/* 185 */             Village per = Villages.getVillageWithPerimeterAt(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 186 */             if (per == null || per.isActionAllowed((short)176, performer))
/*     */             {
/* 188 */               toReturn.add(new ActionEntry((short)176, "Plant", "planting"));
/*     */             }
/*     */           } 
/* 191 */           if ((Delivery.getWaitingDeliveries(performer.getWurmId())).length > 0)
/*     */           {
/* 193 */             toReturn.add(new ActionEntry((short)915, "Accept delivery", "accepting"));
/*     */           }
/* 195 */           if (target.isWagonerCamp()) {
/*     */ 
/*     */             
/* 198 */             Wagoner wagoner = Wagoner.getWagoner(target.getData());
/* 199 */             if (wagoner != null && wagoner.getVillageId() != -1)
/*     */             {
/* 201 */               if (wagoner.getOwnerId() == performer.getWurmId() || performer.getPower() > 1) {
/*     */                 
/* 203 */                 List<ActionEntry> waglist = new LinkedList<>();
/* 204 */                 waglist.add(new ActionEntry((short)-2, "Permissions", "viewing"));
/* 205 */                 waglist.add(Actions.actionEntrys[863]);
/* 206 */                 waglist.add(new ActionEntry((short)691, "History Of Wagoner", "viewing"));
/* 207 */                 waglist.add(Actions.actionEntrys[919]);
/* 208 */                 waglist.add(new ActionEntry((short)566, "Manage chat options", "managing"));
/*     */                 
/* 210 */                 toReturn.add(new ActionEntry((short)-(waglist.size() - 2), wagoner.getName(), "wagoner"));
/* 211 */                 toReturn.addAll(waglist);
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 216 */           if (source != null && source.getTemplateId() == 1129 && source.getData() != -1L && 
/* 217 */             Servers.isThisATestServer()) {
/*     */             
/* 219 */             Wagoner wagoner = Wagoner.getWagoner(source.getData());
/* 220 */             if (wagoner != null && wagoner.getVillageId() != -1) {
/*     */               
/* 222 */               List<ActionEntry> testlist = new LinkedList<>();
/* 223 */               if (wagoner.getState() == 0)
/*     */               {
/* 225 */                 if (target.getData() == source.getData()) {
/*     */ 
/*     */                   
/* 228 */                   testlist.add(new ActionEntry((short)140, "Send to bed", "testing"));
/* 229 */                   testlist.add(new ActionEntry((short)111, "Test delivery", "testing"));
/*     */                 } 
/*     */               }
/* 232 */               if (wagoner.getState() == 2)
/*     */               {
/* 234 */                 if (target.getData() == source.getData())
/* 235 */                   testlist.add(new ActionEntry((short)30, "Wake up", "testing")); 
/*     */               }
/* 237 */               if (wagoner.getState() == 14) {
/*     */                 
/* 239 */                 testlist.add(new ActionEntry((short)682, "Drive to here", "commanding"));
/* 240 */                 if (target.getData() == source.getData())
/*     */                 {
/*     */                   
/* 243 */                   testlist.add(new ActionEntry((short)644, "Force Park", "parking"));
/*     */                 }
/* 245 */                 testlist.add(new ActionEntry((short)636, "Go Home", "parking"));
/*     */               }
/* 247 */               else if (wagoner.getState() == 15) {
/*     */                 
/* 249 */                 testlist.add(new ActionEntry((short)917, "Cancel driving", "cancelling"));
/*     */               } 
/* 251 */               testlist.add(new ActionEntry((short)185, "Show state", "checking"));
/* 252 */               if (!testlist.isEmpty()) {
/*     */                 
/* 254 */                 toReturn.add(new ActionEntry((short)-testlist.size(), "Test only", "testing"));
/* 255 */                 toReturn.addAll(testlist);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 261 */       if (source != null)
/*     */       {
/* 263 */         if (source.isRoadMarker()) {
/*     */ 
/*     */           
/* 266 */           if (target.getTemplateId() == 1114 && source.getTemplateId() == 1112) {
/* 267 */             toReturn.add(Actions.actionEntrys[78]);
/*     */           }
/* 269 */           else if (target.getTemplateId() == 1112 && MethodsHighways.numberOfSetBits(target.getAuxData()) <= 2 && source
/* 270 */             .getTemplateId() == 1114 && target.getLastOwnerId() == performer.getWurmId() && 
/* 271 */             !target.isWagonerCamp()) {
/*     */             
/* 273 */             if (!Items.isWaystoneInUse(target.getWurmId())) {
/* 274 */               toReturn.add(Actions.actionEntrys[78]);
/*     */             }
/*     */           } 
/* 277 */         } else if (!target.isWagonerCamp() && performer.mayDestroy(target) && !target.isIndestructible() && 
/* 278 */           !Items.isWaystoneInUse(target.getWurmId()) && 
/* 279 */           !MethodsHighways.isNextToACamp(MethodsHighways.getHighwayPos(target))) {
/*     */           
/* 281 */           toReturn.add(new ActionEntry((short)-1, "Bash", "Bash"));
/* 282 */           if (source.getTemplateId() == 1115) {
/* 283 */             toReturn.add(Actions.actionEntrys[757]);
/*     */           } else {
/* 285 */             toReturn.add(new ActionEntry((short)83, "Destroy", "Destroying", new int[] { 5, 4, 43 }));
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 292 */     return toReturn;
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
/*     */   public boolean[] markerAction(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter) {
/* 308 */     if (target.isRoadMarker() && target.isPlanted() && Features.Feature.HIGHWAYS.isEnabled()) {
/*     */       FindRouteQuestion frq;
/* 310 */       switch (action) {
/*     */         
/*     */         case 759:
/* 313 */           return new boolean[] { true, showProtection(performer, target, act, counter, null) };
/*     */         case 748:
/* 315 */           return new boolean[] { true, showLinks(performer, target, act, counter, null) };
/*     */         
/*     */         case 749:
/* 318 */           return new boolean[] { true, setLink(performer, target, (byte)1, (byte)16) };
/*     */         case 750:
/* 320 */           return new boolean[] { true, setLink(performer, target, (byte)2, (byte)32) };
/*     */         case 751:
/* 322 */           return new boolean[] { true, setLink(performer, target, (byte)4, (byte)64) };
/*     */         case 752:
/* 324 */           return new boolean[] { true, setLink(performer, target, (byte)8, -128) };
/*     */         case 753:
/* 326 */           return new boolean[] { true, setLink(performer, target, (byte)16, (byte)1) };
/*     */         case 754:
/* 328 */           return new boolean[] { true, setLink(performer, target, (byte)32, (byte)2) };
/*     */         case 755:
/* 330 */           return new boolean[] { true, setLink(performer, target, (byte)64, (byte)4) };
/*     */         case 756:
/* 332 */           return new boolean[] { true, setLink(performer, target, -128, (byte)8) };
/*     */         
/*     */         case 758:
/* 335 */           frq = new FindRouteQuestion(performer, target);
/* 336 */           frq.sendQuestion();
/* 337 */           return new boolean[] { true, true };
/*     */ 
/*     */         
/*     */         case 78:
/* 341 */           if (source != null && source.isRoadMarker()) {
/*     */ 
/*     */             
/* 344 */             if (target.getTemplateId() == 1114 && source.getTemplateId() == 1112)
/*     */             {
/* 346 */               return new boolean[] { true, replace(act, performer, source, target, action, counter) };
/*     */             }
/*     */             
/* 349 */             if (target.getTemplateId() == 1112 && MethodsHighways.numberOfSetBits(target.getAuxData()) <= 2 && source
/* 350 */               .getTemplateId() == 1114 && target.getLastOwnerId() == performer.getWurmId())
/*     */             {
/* 352 */               return new boolean[] { true, replace(act, performer, source, target, action, counter) };
/*     */             }
/*     */           } 
/* 355 */           return new boolean[] { true, true };
/*     */ 
/*     */         
/*     */         case 757:
/* 359 */           if (source != null && source.getTemplateId() == 1115) {
/*     */             
/* 361 */             if (performer.mayDestroy(target) && !target.isIndestructible()) {
/* 362 */               return new boolean[] { true, MethodsItems.destroyItem(action, performer, source, target, false, counter) };
/*     */             }
/* 364 */             return new boolean[] { true, true };
/*     */           } 
/* 366 */           return new boolean[] { true, true };
/*     */       } 
/*     */     
/*     */     } 
/* 370 */     return new boolean[] { false, false };
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
/*     */   private static boolean replace(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 388 */     if (MethodsItems.cannotPlant(performer, source))
/* 389 */       return true; 
/* 390 */     int time = 200;
/* 391 */     Skills skills = performer.getSkills();
/* 392 */     Skill primSkill = skills.getSkillOrLearn(10031);
/*     */     
/* 394 */     if (counter == 1.0F) {
/*     */       
/* 396 */       if (primSkill.getRealKnowledge() < 21.0D) {
/*     */         
/* 398 */         performer.getCommunicator().sendNormalServerMessage("Not enough skill to replace the " + target.getName() + ".", (byte)3);
/* 399 */         return true;
/*     */       } 
/*     */       
/* 402 */       if (!Methods.isActionAllowed(performer, (short)176, target.getTileX(), target.getTileY()))
/* 403 */         return true; 
/* 404 */       if (!performer.canCarry(target.getFullWeight())) {
/*     */         
/* 406 */         performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the " + target.getName() + " as well.", (byte)3);
/* 407 */         return true;
/*     */       } 
/* 409 */       time = Actions.getStandardActionTime(performer, primSkill, source, 0.0D);
/* 410 */       act.setTimeLeft(time);
/* 411 */       performer.getCommunicator().sendNormalServerMessage("You start to replace the " + target.getName() + ".");
/* 412 */       Server.getInstance().broadCastAction(performer.getName() + " starts to replace the " + target.getName() + ".", performer, 5);
/*     */       
/* 414 */       performer.sendActionControl("Replacing " + target.getName(), true, time);
/* 415 */       performer.getStatus().modifyStamina(-1000.0F);
/*     */     }
/*     */     else {
/*     */       
/* 419 */       time = act.getTimeLeft();
/*     */     } 
/* 421 */     if (act.mayPlaySound())
/*     */     {
/* 423 */       SoundPlayer.playSound("sound.work.stonecutting", performer, 1.0F);
/*     */     }
/* 425 */     if (act.currentSecond() == 5)
/*     */     {
/* 427 */       performer.getStatus().modifyStamina(-1000.0F);
/*     */     }
/* 429 */     if (counter * 10.0F > time) {
/*     */       
/* 431 */       byte oldlinks = target.getAuxData();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 439 */         target.setReplacing(true);
/* 440 */         target.setWhatHappened("replaced");
/* 441 */         target.setIsPlanted(false);
/*     */         
/* 443 */         source.setIsPlanted(true);
/*     */         
/* 445 */         source.putItemInCorner(performer, target.getTileX(), target.getTileY(), target.isOnSurface(), target.getBridgeId(), false);
/*     */         
/* 447 */         MethodsHighways.autoLink(source, oldlinks);
/*     */         
/* 449 */         Zone zone = Zones.getZone(target.getTileX(), target.getTileY(), target.isOnSurface());
/* 450 */         zone.removeItem(target);
/* 451 */         performer.getInventory().insertItem(target, false);
/*     */       }
/* 453 */       catch (NoSuchItemException nsie) {
/*     */         
/* 455 */         source.setIsPlanted(false);
/* 456 */         performer.getCommunicator().sendNormalServerMessage("You fail to replace the " + target
/* 457 */             .getName() + ". Something is weird.");
/* 458 */         logger.log(Level.WARNING, performer.getName() + ": " + nsie.getMessage(), (Throwable)nsie);
/*     */       }
/* 460 */       catch (NoSuchZoneException nsze) {
/*     */         
/* 462 */         source.setIsPlanted(false);
/* 463 */         performer.getCommunicator().sendNormalServerMessage("You fail to replace the " + target
/* 464 */             .getName() + ". Something is weird.");
/* 465 */         logger.log(Level.WARNING, performer.getName() + ": " + nsze.getMessage(), (Throwable)nsze);
/*     */       } 
/*     */       
/* 468 */       performer.getCommunicator().sendNormalServerMessage("You replaced the " + target.getName() + " with a " + source.getName() + ".");
/* 469 */       Server.getInstance().broadCastAction(performer.getName() + " replaced the " + target.getName() + " with a " + source.getName() + ".", performer, 5);
/*     */       
/* 471 */       return true;
/*     */     } 
/* 473 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean showLinks(Creature performer, Item marker, Action act, float counter, @Nullable HighwayPos highwayPos) {
/* 479 */     boolean done = false;
/* 480 */     if (act.currentSecond() == 1) {
/*     */       
/* 482 */       String linktypeString = (highwayPos == null) ? "link" : "possible links";
/* 483 */       performer.getCommunicator().sendNormalServerMessage("You start viewing the " + linktypeString + ".");
/* 484 */       performer.sendActionControl(Actions.actionEntrys[748].getVerbString(), true, 300);
/* 485 */       if (highwayPos == null) {
/* 486 */         done = !MethodsHighways.viewLinks(performer, marker);
/*     */       } else {
/* 488 */         done = !MethodsHighways.viewLinks(performer, highwayPos, marker);
/* 489 */       }  if (done) {
/* 490 */         performer.getCommunicator().sendNormalServerMessage("Problem viewing the " + linktypeString + ".");
/*     */       }
/* 492 */     } else if (act.currentSecond() >= 30) {
/*     */       
/* 494 */       done = true;
/* 495 */       String linktypeString = (highwayPos == null) ? "link" : "possible links";
/* 496 */       performer.getCommunicator().sendNormalServerMessage("You stop viewing the " + linktypeString + ".");
/*     */     } 
/* 498 */     return done;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean showProtection(Creature performer, Item marker, Action act, float counter, @Nullable HighwayPos highwayPos) {
/* 504 */     boolean done = false;
/* 505 */     if (act.currentSecond() == 1) {
/*     */       
/* 507 */       String linktypeString = (highwayPos == null) ? "protection" : "possible protection";
/* 508 */       performer.getCommunicator().sendNormalServerMessage("You start viewing the " + linktypeString + ".");
/* 509 */       performer.sendActionControl(Actions.actionEntrys[759].getVerbString(), true, 300);
/* 510 */       if (highwayPos == null) {
/* 511 */         done = !MethodsHighways.viewProtection(performer, marker);
/*     */       } else {
/* 513 */         done = !MethodsHighways.viewProtection(performer, highwayPos, marker);
/* 514 */       }  if (done) {
/* 515 */         performer.getCommunicator().sendNormalServerMessage("Problem viewing the " + linktypeString + ".");
/*     */       }
/* 517 */     } else if (act.currentSecond() >= 30) {
/*     */       
/* 519 */       done = true;
/* 520 */       String linktypeString = (highwayPos == null) ? "protection" : "possible protection";
/* 521 */       performer.getCommunicator().sendNormalServerMessage("You stop viewing the " + linktypeString + ".");
/*     */     } 
/* 523 */     return done;
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
/*     */   private boolean setLink(Creature performer, Item target, byte linkDir, byte oppositeDir) {
/* 536 */     Item marker = MethodsHighways.getMarker(target, linkDir);
/* 537 */     if (marker != null) {
/*     */       
/* 539 */       target.setAuxData((byte)(target.getAuxData() | linkDir));
/* 540 */       marker.setAuxData((byte)(marker.getAuxData() | oppositeDir));
/*     */       
/* 542 */       Routes.checkForNewRoutes(marker);
/*     */       
/* 544 */       target.updateModelNameOnGroundItem();
/* 545 */       marker.updateModelNameOnGroundItem();
/*     */     }
/*     */     else {
/*     */       
/* 549 */       performer.getCommunicator().sendNormalServerMessage("No marker found in " + 
/* 550 */           MethodsHighways.getLinkDirString(linkDir) + " direction.");
/*     */     } 
/* 552 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MarkerBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */