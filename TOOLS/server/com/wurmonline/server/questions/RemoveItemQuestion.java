/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.behaviours.Action;
/*     */ import com.wurmonline.server.behaviours.BehaviourDispatcher;
/*     */ import com.wurmonline.server.behaviours.NoSuchBehaviourException;
/*     */ import com.wurmonline.server.behaviours.Vehicle;
/*     */ import com.wurmonline.server.behaviours.Vehicles;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemMealData;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.structures.NoSuchWallException;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import java.util.Properties;
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
/*     */ public class RemoveItemQuestion
/*     */   extends Question
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(RemoveItemQuestion.class.getName());
/*     */   private static final int MAXNUMS = 2147483647;
/*  50 */   private long moveTarget = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoveItemQuestion(Creature aResponder, long aTarget) {
/*  61 */     super(aResponder, "Removing items", "How many items do you wish to remove?", 84, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoveItemQuestion(Creature aResponder, long aTarget, long aMoveTarget) {
/*  67 */     super(aResponder, "Removing items", "How many items do you wish to remove?", 84, aTarget);
/*  68 */     this.moveTarget = aMoveTarget;
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
/*     */   public void answer(Properties aAnswers) {
/*  80 */     String numstext = aAnswers.getProperty("numstext");
/*  81 */     String nums = aAnswers.getProperty("items");
/*  82 */     if (numstext != null && numstext.length() > 0)
/*  83 */       nums = numstext; 
/*  84 */     if (nums != null && nums.length() > 0) {
/*     */       
/*  86 */       if (nums.equals(String.valueOf(2147483647))) {
/*  87 */         getResponder().getCommunicator().sendNormalServerMessage("You selected max.");
/*     */       } else {
/*  89 */         getResponder().getCommunicator().sendNormalServerMessage("You selected " + nums + ".");
/*     */       } 
/*     */       try {
/*  92 */         int i = Integer.parseInt(nums);
/*  93 */         if (i > 0) {
/*     */           
/*     */           try {
/*  96 */             Item bulkitem = Items.getItem(this.target);
/*     */ 
/*     */             
/*  99 */             long topParentId = bulkitem.getTopParent();
/* 100 */             Item topParent = Items.getItem(topParentId);
/* 101 */             float maxDist = 4.0F;
/* 102 */             if (topParent.isVehicle()) {
/*     */               
/* 104 */               Vehicle vehicle = Vehicles.getVehicle(topParent);
/* 105 */               if (vehicle != null)
/* 106 */                 maxDist = Math.max(maxDist, vehicle.getMaxAllowedLoadDistance()); 
/*     */             } 
/* 108 */             if (!getResponder().isWithinDistanceTo(topParent.getPosX(), topParent.getPosY(), topParent.getPosZ(), maxDist) && bulkitem
/* 109 */               .getTopParent() != getResponder().getVehicle()) {
/*     */               
/* 111 */               getResponder().getCommunicator().sendNormalServerMessage("You are too far away from the " + bulkitem
/* 112 */                   .getName() + " now.");
/*     */               return;
/*     */             } 
/* 115 */             boolean full = false;
/* 116 */             Item parent = null;
/*     */             
/*     */             try {
/* 119 */               parent = bulkitem.getParent();
/* 120 */               full = parent.isFull();
/*     */             }
/* 122 */             catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */ 
/*     */             
/* 126 */             boolean max = (i == Integer.MAX_VALUE);
/*     */             
/* 128 */             int bnums = 0;
/*     */ 
/*     */ 
/*     */             
/* 132 */             if (bulkitem.getRealTemplate() != null && bulkitem.getRealTemplate().isCombine()) {
/* 133 */               bnums = (int)Math.ceil(bulkitem.getBulkNumsFloat(false));
/*     */             } else {
/* 135 */               bnums = bulkitem.getBulkNums();
/* 136 */             }  Item toInsert = null;
/* 137 */             int current = getResponder().getInventory().getNumItemsNotCoins();
/*     */             
/* 139 */             int maxCapac = Math.max(0, 100 - current);
/* 140 */             if (i > maxCapac)
/*     */             {
/* 142 */               i = Math.min(bnums, maxCapac);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 149 */             if (bnums >= i) {
/*     */               
/* 151 */               int weightReduced = 0;
/* 152 */               ItemTemplate template = bulkitem.getRealTemplate();
/* 153 */               if (template != null) {
/*     */                 
/* 155 */                 int volume = template.getVolume();
/* 156 */                 int tweight = template.getWeightGrams();
/* 157 */                 Item targetInventory = null;
/*     */                 
/*     */                 try {
/* 160 */                   if (this.moveTarget != 0L) {
/* 161 */                     targetInventory = Items.getItem(this.moveTarget);
/*     */                   }
/* 163 */                 } catch (NoSuchItemException nsi) {
/*     */                   
/* 165 */                   String message = StringUtil.format("Unable to find item: %d.", new Object[] {
/*     */                         
/* 167 */                         Long.valueOf(this.moveTarget) });
/* 168 */                   logger.log(Level.WARNING, message, (Throwable)nsi);
/*     */                   
/*     */                   return;
/*     */                 } 
/* 172 */                 if (template.isFish() && template.getTemplateId() != 369) {
/*     */                   
/* 174 */                   double ql = (bulkitem.getCurrentQualityLevel() / 100.0F);
/* 175 */                   tweight = (int)(tweight * ql);
/*     */                 } 
/* 177 */                 if (max) {
/*     */                   
/* 179 */                   i = Math.min(maxCapac, getResponder().getCarryCapacityFor(tweight));
/* 180 */                   if (i <= 0) {
/*     */                     
/* 182 */                     getResponder().getCommunicator().sendNormalServerMessage("You can not even carry one of those.");
/*     */                     
/*     */                     return;
/*     */                   } 
/* 186 */                   i = Math.min(i, bnums);
/*     */                 }
/* 188 */                 else if (!getResponder().canCarry(tweight * i)) {
/*     */                   
/* 190 */                   if (targetInventory == null || !targetInventory.isBulkContainer())
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/* 198 */                     if (i > 1 || bulkitem.getWeightGrams() >= volume) {
/*     */                       
/* 200 */                       getResponder().getCommunicator().sendNormalServerMessage("You may not carry that weight.");
/*     */                       
/*     */                       return;
/*     */                     } 
/*     */                   }
/*     */                 } 
/* 206 */                 if (targetInventory.isContainerLiquid() && (targetInventory.getSizeX() < template.getSizeX() || targetInventory
/* 207 */                   .getSizeY() < template.getSizeY() || targetInventory
/* 208 */                   .getSizeZ() < template.getSizeZ())) {
/*     */                   
/* 210 */                   getResponder().getCommunicator().sendNormalServerMessage("The " + template
/* 211 */                       .getName() + " will not fit inside the " + targetInventory.getName() + ".");
/*     */                   
/*     */                   return;
/*     */                 } 
/* 215 */                 byte auxdata = bulkitem.getAuxData();
/* 216 */                 int toMake = bulkitem.getRealTemplateId();
/* 217 */                 String aName = bulkitem.getActualName();
/*     */                 
/* 219 */                 if (toMake == 129 && auxdata == 0) {
/*     */                   
/* 221 */                   toMake = 92;
/*     */ 
/*     */                   
/* 224 */                   aName = "meat";
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 231 */                 if (Features.Feature.MOVE_BULK_TO_BULK.isEnabled() && targetInventory.isBulkContainer() && this.moveTarget > 0L) {
/*     */ 
/*     */                   
/*     */                   try {
/* 235 */                     BehaviourDispatcher.action(getResponder(), getResponder().getCommunicator(), this.target, this.moveTarget, (short)914);
/*     */ 
/*     */ 
/*     */                     
/* 239 */                     Action act = ((Player)getResponder()).getActions().getLastSlowAction();
/* 240 */                     if (act != null) {
/* 241 */                       act.setData(Integer.parseInt(nums));
/*     */                     } else {
/* 243 */                       getResponder().getCommunicator().sendAlertServerMessage("ERROR: Action was null, could not set amount!");
/*     */                     } 
/* 245 */                   } catch (NoSuchPlayerException e) {
/*     */                     
/* 247 */                     logger.fine("No such player Ex");
/* 248 */                   } catch (NoSuchCreatureException e) {
/*     */                     
/* 250 */                     logger.fine("No such creature Ex");
/* 251 */                   } catch (NoSuchBehaviourException e) {
/*     */                     
/* 253 */                     logger.fine("No such behaviour Ex :(");
/* 254 */                   } catch (NoSuchWallException e) {
/*     */                     
/* 256 */                     logger.fine("no such wall ex");
/* 257 */                   } catch (FailedException e) {
/*     */                     
/* 259 */                     logger.fine("Failed EX?");
/*     */                   } 
/*     */ 
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */ 
/*     */                 
/* 267 */                 for (int created = 0; created < i; created++) {
/*     */ 
/*     */                   
/*     */                   try {
/* 271 */                     int weight = bulkitem.getWeightGrams() - weightReduced;
/* 272 */                     float percent = 1.0F;
/* 273 */                     if (weight < volume) {
/* 274 */                       percent = weight / volume;
/*     */                     } else {
/* 276 */                       weight = Math.min(bulkitem.getWeightGrams(), volume);
/*     */                     } 
/* 278 */                     if (weight > 0)
/*     */                     {
/*     */                       
/* 281 */                       toInsert = ItemFactory.createItem(toMake, bulkitem
/* 282 */                           .getCurrentQualityLevel(), bulkitem.getMaterial(), (byte)0, null);
/*     */ 
/*     */ 
/*     */                       
/* 286 */                       if (!toInsert.isFish())
/* 287 */                         toInsert.setCreator(getResponder().getName()); 
/* 288 */                       toInsert.setLastOwnerId(getResponder().getWurmId());
/* 289 */                       if (toInsert.isRepairable())
/*     */                       {
/*     */ 
/*     */                         
/* 293 */                         toInsert.setCreationState((byte)0);
/*     */                       }
/* 295 */                       if (toInsert.usesFoodState()) {
/*     */                         
/* 297 */                         toInsert.setAuxData(auxdata);
/* 298 */                         ItemMealData imd = ItemMealData.getItemMealData(bulkitem.getWurmId());
/* 299 */                         if (imd != null)
/*     */                         {
/* 301 */                           ItemMealData.save(toInsert.getWurmId(), imd.getRecipeId(), imd.getCalories(), imd
/* 302 */                               .getCarbs(), imd.getFats(), imd.getProteins(), imd.getBonus(), imd
/* 303 */                               .getStages(), imd.getIngredients());
/*     */                         }
/*     */                       } 
/* 306 */                       if (template.isFish() && template.getTemplateId() != 369) {
/*     */ 
/*     */ 
/*     */                         
/* 310 */                         toInsert.setSizes(tweight);
/* 311 */                         toInsert.setWeight(tweight, true);
/*     */                       } else {
/*     */                         
/* 314 */                         toInsert.setWeight((int)(percent * template.getWeightGrams()), true);
/*     */                       } 
/* 316 */                       if (bulkitem.getData1() != -1)
/*     */                       {
/* 318 */                         toInsert.setRealTemplate(bulkitem.getData1());
/*     */                       }
/*     */                       
/* 321 */                       if (!bulkitem.getActualName().equalsIgnoreCase("bulk item"))
/* 322 */                         toInsert.setName(aName); 
/* 323 */                       if (this.moveTarget == 0L) {
/* 324 */                         getResponder().getInventory().insertItem(toInsert);
/*     */                       
/*     */                       }
/* 327 */                       else if (targetInventory.isBulkContainer()) {
/*     */ 
/*     */                         
/*     */                         try {
/* 331 */                           if ((!targetInventory.isCrate() && targetInventory
/* 332 */                             .hasSpaceFor(toInsert.getVolume())) || (targetInventory
/* 333 */                             .isCrate() && targetInventory
/* 334 */                             .canAddToCrate(toInsert))) {
/*     */                             
/* 336 */                             if (!toInsert.moveToItem(getResponder(), targetInventory
/* 337 */                                 .getWurmId(), false)) {
/*     */                               
/* 339 */                               Items.destroyItem(toInsert.getWurmId());
/*     */ 
/*     */                               
/*     */                               break;
/*     */                             } 
/*     */                           } else {
/* 345 */                             String message = "The %s will not fit in the %s.";
/* 346 */                             getResponder().getCommunicator().sendNormalServerMessage(
/* 347 */                                 StringUtil.format("The %s will not fit in the %s.", new Object[] {
/* 348 */                                     toInsert.getName(), targetInventory
/* 349 */                                     .getName() }));
/* 350 */                             Items.destroyItem(toInsert.getWurmId());
/*     */ 
/*     */                             
/*     */                             break;
/*     */                           } 
/* 355 */                         } catch (NoSuchPlayerException noSuchPlayerException) {
/*     */ 
/*     */                         
/*     */                         }
/* 359 */                         catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/*     */                       }
/* 366 */                       else if (targetInventory.testInsertItem(toInsert) && targetInventory
/* 367 */                         .mayCreatureInsertItem()) {
/*     */                         
/* 369 */                         targetInventory.insertItem(toInsert);
/*     */                       }
/*     */                       else {
/*     */                         
/* 373 */                         String message = "There is not enough space for any more items.";
/* 374 */                         getResponder().getCommunicator().sendNormalServerMessage("There is not enough space for any more items.");
/* 375 */                         Items.destroyItem(toInsert.getWurmId());
/*     */                         
/*     */                         break;
/*     */                       } 
/*     */                       
/* 380 */                       weightReduced += weight;
/*     */                     }
/*     */                   
/* 383 */                   } catch (NoSuchTemplateException nst) {
/*     */                     
/* 385 */                     logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */                   }
/* 387 */                   catch (FailedException fe) {
/*     */                     
/* 389 */                     logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */                   } 
/*     */                 } 
/*     */                 
/* 393 */                 getResponder().achievement(167, -i);
/* 394 */                 if (!bulkitem.setWeight(bulkitem.getWeightGrams() - weightReduced, true));
/*     */               } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 403 */               if (parent != null)
/*     */               {
/* 405 */                 if (full != parent.isFull() || parent.isCrate()) {
/* 406 */                   parent.updateModelNameOnGroundItem();
/*     */                 }
/*     */               }
/*     */             } else {
/*     */               
/* 411 */               getResponder().getCommunicator().sendNormalServerMessage("The " + bulkitem
/* 412 */                   .getName() + " does not contain " + i + " items.");
/*     */               
/*     */               return;
/*     */             } 
/* 416 */           } catch (NoSuchItemException nsc) {
/*     */             
/* 418 */             getResponder().getCommunicator().sendNormalServerMessage("No such item.");
/*     */             return;
/*     */           } 
/*     */         }
/* 422 */       } catch (NumberFormatException ne) {
/*     */         
/* 424 */         getResponder().getCommunicator().sendNormalServerMessage("Not a number.");
/*     */       } 
/*     */     } 
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
/*     */   public void sendQuestion() {
/*     */     Item temp;
/*     */     try {
/* 443 */       if (this.moveTarget > 0L)
/* 444 */       { temp = Items.getItem(this.moveTarget); }
/*     */       else
/* 446 */       { temp = null; } 
/* 447 */     } catch (NoSuchItemException e) {
/*     */       
/* 449 */       temp = null;
/*     */     } 
/*     */     
/* 452 */     Item moveTargetItem = temp;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 457 */       Item bulkitem = Items.getItem(this.target);
/*     */       
/* 459 */       String carryString = (moveTargetItem != null && (moveTargetItem.isBulk() || moveTargetItem.isBulkContainer())) ? "All items" : "As many as I can carry";
/*     */ 
/*     */       
/* 462 */       StringBuilder buf = new StringBuilder();
/* 463 */       int nums = bulkitem.getBulkNums();
/* 464 */       buf.append(getBmlHeader());
/* 465 */       if (nums > 0) {
/*     */         
/* 467 */         buf.append("text{text=\"How many items do you wish to remove?\"};");
/* 468 */         buf.append("text{text=''}");
/* 469 */         buf.append("input{text='';id='numstext';maxlength='2'};");
/* 470 */         buf.append("text{text=''}");
/* 471 */         buf.append("radio{ group='items'; id='2147483647';selected='true';text='" + carryString + "'}");
/* 472 */         buf.append("radio{ group='items'; id='0';text='None'}");
/* 473 */         if (nums < 100 && nums != 1)
/* 474 */           buf.append("radio{ group='items'; id='" + nums + "';text='" + nums + "'}"); 
/* 475 */         buf.append("radio{ group='items'; id='1';text='1'}");
/* 476 */         if (nums > 2 && nums != 2)
/* 477 */           buf.append("radio{ group='items'; id='2';text='2'}"); 
/* 478 */         if (nums > 5 && nums != 5)
/* 479 */           buf.append("radio{ group='items'; id='5';text='5'}"); 
/* 480 */         if (nums > 10 && nums != 10)
/* 481 */           buf.append("radio{ group='items'; id='10';text='10'}"); 
/* 482 */         if (nums > 20 && nums != 20)
/* 483 */           buf.append("radio{ group='items'; id='20';text='20'}"); 
/* 484 */         if (nums > 50 && nums != 50) {
/* 485 */           buf.append("radio{ group='items'; id='50';text='50'}");
/*     */         }
/*     */       } else {
/* 488 */         buf.append("text{text=\"The " + bulkitem.getName() + " is empty.\"}");
/* 489 */       }  buf.append(createAnswerButton2());
/* 490 */       getResponder().getCommunicator().sendBml(300, 340, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 492 */     catch (NoSuchItemException noSuchItemException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\RemoveItemQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */