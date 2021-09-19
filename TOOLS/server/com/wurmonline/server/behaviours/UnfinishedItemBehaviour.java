/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchEntryException;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.PlonkData;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.effects.Effect;
/*     */ import com.wurmonline.server.effects.EffectFactory;
/*     */ import com.wurmonline.server.items.AdvancedCreationEntry;
/*     */ import com.wurmonline.server.items.CreationMatrix;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.tutorial.MissionTriggers;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
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
/*     */ final class UnfinishedItemBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  58 */   private static final Logger logger = Logger.getLogger(UnfinishedItemBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnfinishedItemBehaviour() {
/*  65 */     super((short)23);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  71 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  72 */     if (!target.isTraded())
/*     */     {
/*  74 */       if (target.getTemplateId() == 386) {
/*     */         
/*  76 */         int tid = MethodsItems.getItemForImprovement(target.getMaterial(), target.creationState);
/*     */ 
/*     */         
/*  79 */         if (tid == source.getTemplateId())
/*     */         {
/*  81 */           String actString = MethodsItems.getImproveAction(target.getMaterial(), target.creationState);
/*  82 */           toReturn.add(new ActionEntry((short)228, actString, actString));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  87 */         int objectCreated = -1;
/*     */         
/*     */         try {
/*  90 */           objectCreated = AdvancedCreationEntry.getTemplateId(target);
/*  91 */           if (objectCreated == 430 || objectCreated == 528 || objectCreated == 638)
/*     */           {
/*     */             
/*  94 */             objectCreated = 384;
/*     */           }
/*  96 */           AdvancedCreationEntry entry = CreationMatrix.getInstance().getAdvancedCreationEntry(objectCreated);
/*  97 */           if (entry.isItemNeeded(source, target)) {
/*  98 */             toReturn.add(Actions.actionEntrys[169]);
/*     */           }
/* 100 */         } catch (NoSuchEntryException nse) {
/*     */           
/* 102 */           logger.log(Level.WARNING, "No creation entry for " + objectCreated, (Throwable)nse);
/*     */         } 
/*     */       } 
/*     */     }
/* 106 */     toReturn.addAll(super.getBehavioursFor(performer, source, target));
/* 107 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 113 */     boolean toReturn = true;
/* 114 */     if (action == 1) {
/*     */       
/* 116 */       if (target.getTemplateId() == 386) {
/*     */ 
/*     */         
/* 119 */         performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/* 120 */         target.sendEnchantmentStrings(performer.getCommunicator());
/*     */       }
/*     */       else {
/*     */         
/* 124 */         int objectCreated = -1;
/*     */         
/*     */         try {
/* 127 */           objectCreated = AdvancedCreationEntry.getTemplateId(target);
/* 128 */           ItemTemplate template = null;
/*     */           
/*     */           try {
/* 131 */             template = ItemTemplateFactory.getInstance().getTemplate(objectCreated);
/*     */ 
/*     */             
/* 134 */             String start = "You see " + template.getNameWithGenus() + " under construction. Ql: " + target.getQualityLevel() + ", Dam: " + target.getDamage() + ".";
/* 135 */             if (objectCreated == 430 || objectCreated == 528 || objectCreated == 638)
/*     */             {
/* 137 */               objectCreated = 384; } 
/* 138 */             AdvancedCreationEntry creation = CreationMatrix.getInstance().getAdvancedCreationEntry(objectCreated);
/*     */ 
/*     */             
/* 141 */             String rarity = (target.getRarity() == 0) ? "" : MethodsItems.getRarityDesc(target.getRarity());
/* 142 */             String finish = creation.getItemsLeft(target);
/* 143 */             performer.getCommunicator().sendNormalServerMessage(start + rarity + ' ' + finish);
/*     */           }
/* 145 */           catch (NoSuchTemplateException nst) {
/*     */             
/* 147 */             logger.log(Level.WARNING, "No template with id " + objectCreated);
/*     */           }
/*     */         
/* 150 */         } catch (NoSuchEntryException nse) {
/*     */           
/* 152 */           logger.log(Level.WARNING, "No creation entry for " + objectCreated, (Throwable)nse);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 157 */       toReturn = super.action(act, performer, target, action, counter);
/* 158 */     }  return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 165 */     boolean done = true;
/*     */     
/* 167 */     if (action == 169) {
/*     */       
/* 169 */       if (!target.isTraded()) {
/*     */         
/* 171 */         boolean actionResult = false;
/*     */         
/*     */         try {
/* 174 */           if (target.isNoTake() || target.isUseOnGroundOnly() || target.getTopParent() == target.getWurmId())
/*     */           {
/* 176 */             if (!performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*     */               
/* 178 */               performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/* 179 */               return done;
/*     */             } 
/*     */           }
/* 182 */           if (source.getOwnerId() != performer.getWurmId()) {
/*     */             
/* 184 */             performer.getCommunicator().sendSafeServerMessage("You must carry the " + source
/* 185 */                 .getName() + " to use it.");
/* 186 */             return done;
/*     */           } 
/* 188 */           if (target.getOwnerId() == -10L)
/*     */           {
/* 190 */             if (!Methods.isActionAllowed(performer, action))
/* 191 */               return done; 
/*     */           }
/* 193 */           int objectCreated = AdvancedCreationEntry.getTemplateId(target);
/* 194 */           if (objectCreated == 430 || objectCreated == 528 || objectCreated == 638)
/*     */           {
/* 196 */             objectCreated = 384; } 
/* 197 */           AdvancedCreationEntry creation = CreationMatrix.getInstance().getAdvancedCreationEntry(objectCreated);
/* 198 */           done = false;
/* 199 */           Item created = creation.cont(performer, source, target.getWurmId(), counter);
/* 200 */           actionResult = true;
/* 201 */           if (!creation.isCreateOnGround() && !performer.canCarry(created.getWeightGrams()))
/* 202 */             performer.getCommunicator().sendNormalServerMessage("You can't carry the " + created.getName() + "."); 
/* 203 */           if (!creation.isCreateOnGround() && performer.getInventory().insertItem(created) && performer.canCarry(created.getWeightGrams())) {
/*     */             
/* 205 */             created.setLastOwnerId(performer.getWurmId());
/* 206 */             performer.getCommunicator().sendNormalServerMessage("You create " + created.getNameWithGenus() + ".");
/* 207 */             Server.getInstance().broadCastAction(performer
/* 208 */                 .getName() + " creates " + created.getNameWithGenus() + ".", performer, 
/* 209 */                 Math.max(3, created.getSizeZ() / 10));
/* 210 */             if (created.isEpicTargetItem()) {
/*     */ 
/*     */               
/* 213 */               AdvancedCreationEntry.onEpicItemCreated(performer, created, created.getTemplateId(), true);
/*     */             } else {
/*     */               
/* 216 */               MissionTriggers.activateTriggers(performer, created, 148, 0L, 1);
/*     */             } 
/*     */           } else {
/*     */             try {
/*     */               int placedTile;
/*     */               
/* 222 */               float posX = performer.getStatus().getPositionX();
/* 223 */               float posY = performer.getStatus().getPositionY();
/* 224 */               float rot = performer.getStatus().getRotation();
/* 225 */               float xPosMod = (float)Math.sin((rot * 0.017453292F)) * 2.0F;
/* 226 */               float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * 2.0F;
/* 227 */               posX += xPosMod;
/* 228 */               posY += yPosMod;
/* 229 */               int placedX = (int)posX >> 2;
/* 230 */               int placedY = (int)posY >> 2;
/*     */               
/* 232 */               if (performer.isOnSurface()) {
/* 233 */                 placedTile = Server.surfaceMesh.getTile(placedX, placedY);
/*     */               } else {
/* 235 */                 placedTile = Server.caveMesh.getTile(placedX, placedY);
/* 236 */               }  if (Tiles.decodeHeight(placedTile) < 0) {
/*     */                 
/* 238 */                 if (created.getTemplateId() == 37)
/*     */                 {
/* 240 */                   performer.getCommunicator().sendNormalServerMessage("The fire fizzles in the water and goes out.");
/*     */                   
/* 242 */                   Items.decay(created.getWurmId(), created.getDbStrings());
/*     */                 }
/*     */                 else
/*     */                 {
/* 246 */                   created.setLastOwnerId(performer.getWurmId());
/* 247 */                   created.putItemInfrontof(performer);
/* 248 */                   performer.getCommunicator().sendNormalServerMessage("You create " + created
/* 249 */                       .getNameWithGenus() + " in front of you on the ground.");
/* 250 */                   Server.getInstance().broadCastAction(performer
/* 251 */                       .getName() + " creates " + created.getNameWithGenus() + ".", performer, 
/* 252 */                       Math.max(3, created.getSizeZ() / 10));
/*     */                 }
/*     */               
/*     */               } else {
/*     */                 
/* 257 */                 created.setLastOwnerId(performer.getWurmId());
/* 258 */                 created.putItemInfrontof(performer);
/* 259 */                 performer.getCommunicator().sendNormalServerMessage("You create " + created
/* 260 */                     .getNameWithGenus() + " in front of you on the ground.");
/* 261 */                 Server.getInstance().broadCastAction(performer
/* 262 */                     .getName() + " creates " + created.getNameWithGenus() + ".", performer, 
/* 263 */                     Math.max(3, created.getSizeZ() / 10));
/* 264 */                 if (created.getTemplateId() == 37) {
/*     */                   
/* 266 */                   Effect effect = EffectFactory.getInstance().createFire(created.getWurmId(), created
/* 267 */                       .getPosX(), created.getPosY(), created.getPosZ(), performer.isOnSurface());
/* 268 */                   created.addEffect(effect);
/*     */                 } 
/*     */               } 
/* 271 */               if (created.getTemplateId() == 1311)
/*     */               {
/* 273 */                 created.setName(created.getTemplate().getName() + " [Empty]");
/*     */               }
/* 275 */               if (created.isEpicTargetItem()) {
/*     */ 
/*     */                 
/* 278 */                 AdvancedCreationEntry.onEpicItemCreated(performer, created, created.getTemplateId(), true);
/*     */               } else {
/*     */                 
/* 281 */                 MissionTriggers.activateTriggers(performer, created, 148, 0L, 1);
/* 282 */               }  if (created.isSpringFilled()) {
/*     */                 
/* 284 */                 if (Zone.hasSpring(created.getTileX(), created.getTileY()))
/*     */                 {
/* 286 */                   performer.achievement(207);
/*     */                 }
/*     */               }
/* 289 */               else if (created.isKingdomMarker()) {
/*     */                 
/* 291 */                 performer.achievement(208);
/*     */               }
/* 293 */               else if (created.getTemplate().isStorageRack()) {
/*     */                 
/* 295 */                 performer.achievement(508);
/*     */               }
/* 297 */               else if (created.isBoat()) {
/*     */                 
/* 299 */                 PlonkData.BOAT_SECURITY.trigger(performer);
/* 300 */                 switch (created.getTemplateId()) {
/*     */                   
/*     */                   case 540:
/* 303 */                     performer.achievement(209);
/*     */                     break;
/*     */                   case 542:
/* 306 */                     performer.achievement(211);
/*     */                     break;
/*     */                   case 541:
/* 309 */                     performer.achievement(210);
/*     */                     break;
/*     */                   case 490:
/* 312 */                     performer.achievement(213);
/*     */                     break;
/*     */                   case 491:
/* 315 */                     performer.achievement(214);
/*     */                     break;
/*     */                   case 543:
/* 318 */                     performer.achievement(212);
/*     */                     break;
/*     */                   
/*     */                   default:
/* 322 */                     logger.fine("Was a boat but not of a known type so no achievement: templateId: " + created
/* 323 */                         .getTemplateId());
/*     */                     break;
/*     */                 } 
/*     */               } 
/* 327 */             } catch (NoSuchZoneException nsz) {
/*     */               
/* 329 */               logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*     */             }
/* 331 */             catch (NoSuchPlayerException nsp) {
/*     */               
/* 333 */               logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*     */             }
/* 335 */             catch (NoSuchCreatureException nsc) {
/*     */               
/* 337 */               logger.log(Level.INFO, nsc.getMessage(), (Throwable)nsc);
/*     */             } 
/*     */           } 
/* 340 */           done = true;
/*     */         }
/* 342 */         catch (NoSuchEntryException nse) {
/*     */           
/* 344 */           logger.log(Level.WARNING, nse.getMessage(), (Throwable)nse);
/*     */           
/* 346 */           done = true;
/*     */         }
/* 348 */         catch (NoSuchSkillException nss) {
/*     */           
/* 350 */           logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/* 351 */           done = true;
/*     */         }
/* 353 */         catch (NoSuchItemException nsi) {
/*     */ 
/*     */           
/* 356 */           if (nsi.getMessage().equalsIgnoreCase("Not done yet."))
/* 357 */             actionResult = true; 
/* 358 */           done = true;
/*     */         }
/* 360 */         catch (FailedException failedException) {
/*     */ 
/*     */         
/*     */         }
/* 364 */         catch (Exception ex) {
/*     */           
/* 366 */           logger.log(Level.WARNING, performer.getName() + " weird:" + ex.getMessage(), ex);
/* 367 */           done = true;
/*     */         } 
/* 369 */         if (done) {
/* 370 */           performer.getCommunicator().sendActionResult(actionResult);
/*     */         }
/*     */       } 
/* 373 */     } else if (action == 228) {
/*     */       
/* 375 */       done = MethodsItems.polishItem(act, performer, source, target, counter);
/*     */     }
/* 377 */     else if (action == 180) {
/*     */       
/* 379 */       done = true;
/* 380 */       target.deleteAllEffects();
/* 381 */       Items.destroyItem(target.getWurmId());
/*     */     } else {
/*     */       
/* 384 */       done = super.action(act, performer, source, target, action, counter);
/* 385 */     }  return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\UnfinishedItemBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */