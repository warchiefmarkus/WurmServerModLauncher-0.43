/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
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
/*     */ final class VegetableBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(VegetableBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   VegetableBehaviour() {
/*  47 */     super((short)16);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
/*  53 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  54 */     toReturn.addAll(super.getBehavioursFor(performer, object));
/*  55 */     if (object.getOwnerId() == performer.getWurmId()) {
/*     */       
/*  57 */       if (object.isCrushable())
/*  58 */         toReturn.add(Actions.actionEntrys[54]); 
/*  59 */       if (object.hasSeeds())
/*  60 */         toReturn.add(Actions.actionEntrys[55]); 
/*     */     } 
/*  62 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
/*  68 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  69 */     toReturn.addAll(super.getBehavioursFor(performer, source, object));
/*  70 */     if (object.getOwnerId() == performer.getWurmId()) {
/*     */       
/*  72 */       if (object.isCrushable())
/*  73 */         toReturn.add(Actions.actionEntrys[54]); 
/*  74 */       if (object.hasSeeds())
/*  75 */         toReturn.add(Actions.actionEntrys[55]); 
/*     */     } 
/*  77 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  84 */     boolean done = true;
/*  85 */     if (action == 54 || action == 55) {
/*  86 */       done = action(act, performer, target, action, counter);
/*     */     } else {
/*  88 */       done = super.action(act, performer, source, target, action, counter);
/*  89 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  95 */     boolean done = true;
/*  96 */     int nums = -1;
/*  97 */     if (action == 54) {
/*     */       
/*  99 */       if (target.getOwnerId() != performer.getWurmId()) {
/*     */         
/* 101 */         performer.getCommunicator().sendNormalServerMessage("You can't crush that now.");
/* 102 */         return true;
/*     */       } 
/* 104 */       if (target.isProcessedFood()) {
/*     */         
/* 106 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 107 */             .getName() + " is processed and cannot be crushed.");
/* 108 */         return true;
/*     */       } 
/* 110 */       int makes = target.getTemplate().getCrushsTo();
/*     */       
/* 112 */       if (makes > 0) {
/* 113 */         nums = crush(action, performer, target, makes);
/*     */       }
/* 115 */       if (nums > 0)
/*     */       {
/* 117 */         performer.getCommunicator().sendNormalServerMessage("You crush the " + target.getName() + ".");
/* 118 */         Server.getInstance().broadCastAction(performer
/* 119 */             .getName() + " crushes " + target.getNameWithGenus() + ".", performer, 
/* 120 */             Math.max(3, target.getSizeZ() / 10));
/*     */       }
/* 122 */       else if (nums == 0)
/*     */       {
/* 124 */         performer.getCommunicator().sendNormalServerMessage("You fail to crush the " + target.getName() + ".");
/* 125 */         Server.getInstance().broadCastAction(performer
/* 126 */             .getName() + " tries to crush " + target.getNameWithGenus() + " with " + performer
/* 127 */             .getHisHerItsString() + " bare hands.", performer, 
/* 128 */             Math.max(3, target.getSizeZ() / 10));
/*     */       }
/*     */     
/* 131 */     } else if (action == 55) {
/*     */       
/* 133 */       if (target.getOwnerId() != performer.getWurmId()) {
/*     */         
/* 135 */         performer.getCommunicator().sendNormalServerMessage("You can't pick that now.");
/* 136 */         return true;
/*     */       } 
/* 138 */       if (target.isProcessedFood()) {
/*     */         
/* 140 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 141 */             .getName() + " is processed and there are no seeds to be picked.");
/* 142 */         return true;
/*     */       } 
/* 144 */       int makes = target.getTemplate().getPickSeeds();
/*     */       
/* 146 */       if (makes > 0) {
/* 147 */         nums = crush(action, performer, target, makes);
/*     */       }
/* 149 */       if (nums > 0) {
/*     */         
/* 151 */         performer.getCommunicator().sendNormalServerMessage("You pick the " + target
/* 152 */             .getName() + " for seeds, ruining it.");
/*     */       }
/* 154 */       else if (nums == 0) {
/* 155 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 156 */             .getName() + " contains almost no seeds.");
/*     */       } 
/*     */     } else {
/* 159 */       done = super.action(act, performer, target, action, counter);
/* 160 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int crush(short action, Creature performer, Item target, int templateId) {
/* 166 */     int templateWeight = target.getTemplate().getWeightGrams();
/* 167 */     int nums = target.getWeightGrams() / templateWeight;
/* 168 */     Item inventory = performer.getInventory();
/*     */     
/* 170 */     for (int x = 0; x < nums; x++) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 178 */         if (x != nums - 1 || !target.getParent().isInventory())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 184 */           if (!inventory.mayCreatureInsertItem()) {
/*     */             
/* 186 */             performer.getCommunicator().sendNormalServerMessage("You need more space in your inventory.");
/* 187 */             return x;
/*     */           } 
/*     */         }
/* 190 */         Item toCreate = ItemFactory.createItem(templateId, target.getCurrentQualityLevel(), null);
/* 191 */         if (templateId == 745)
/* 192 */           toCreate.setWeight(100, true); 
/* 193 */         inventory.insertItem(toCreate);
/* 194 */         target.setWeight(target.getWeightGrams() - templateWeight, true);
/*     */       }
/* 196 */       catch (FailedException e) {
/*     */ 
/*     */         
/* 199 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */       }
/* 201 */       catch (NoSuchTemplateException e) {
/*     */ 
/*     */         
/* 204 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */       }
/* 206 */       catch (NoSuchItemException e) {
/*     */ 
/*     */         
/* 209 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     return nums;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\VegetableBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */