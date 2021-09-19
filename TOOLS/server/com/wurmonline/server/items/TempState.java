/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TempState
/*     */   implements ItemTypes
/*     */ {
/*     */   private final int origItemTemplateId;
/*     */   private final int newItemTemplateId;
/*     */   private final short temperatureChangeLevel;
/*     */   private final boolean atIncrease;
/*  54 */   private static final Logger logger = Logger.getLogger(TempState.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean keepWeight;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean keepMaterial;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TempState(int aOrigItemTemplateId, int aNewItemTemplateId, short aTemperatureChangeLevel, boolean aAtIncrease, boolean aKeepWeight, boolean aKeepMaterial) {
/*  76 */     this.origItemTemplateId = aOrigItemTemplateId;
/*  77 */     this.newItemTemplateId = aNewItemTemplateId;
/*  78 */     this.temperatureChangeLevel = aTemperatureChangeLevel;
/*  79 */     this.atIncrease = aAtIncrease;
/*  80 */     this.keepWeight = aKeepWeight;
/*  81 */     this.keepMaterial = aKeepMaterial;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean changeItem(Item parent, Item item, short oldTemp, short newTemp, float qualityRatio) {
/*  87 */     int itemPrimarySkill = -10;
/*     */     
/*  89 */     if (passedLevel(oldTemp, newTemp)) {
/*     */       
/*  91 */       if (newTemp >= this.temperatureChangeLevel) {
/*     */         
/*  93 */         if (this.atIncrease)
/*     */         {
/*     */           
/*     */           try {
/*  97 */             Item newItem = null;
/*  98 */             Creature performer = null;
/*     */             
/* 100 */             if (this.keepMaterial) {
/* 101 */               newItem = ItemFactory.createItem(this.newItemTemplateId, item.getCurrentQualityLevel() * qualityRatio, item
/* 102 */                   .getMaterial(), item.getRarity(), item.creator);
/*     */             } else {
/* 104 */               newItem = ItemFactory.createItem(this.newItemTemplateId, item.getCurrentQualityLevel() * qualityRatio, (byte)0, item
/* 105 */                   .getRarity(), item.creator);
/*     */             } 
/*     */             
/* 108 */             newItem.setDescription(item.getDescription());
/* 109 */             Set<Item> items = item.getItems();
/* 110 */             if (items != null) {
/*     */               
/* 112 */               Item[] itarr = items.<Item>toArray(new Item[items.size()]);
/* 113 */               for (int x = 0; x < itarr.length; x++) {
/*     */ 
/*     */                 
/*     */                 try {
/* 117 */                   item.dropItem(itarr[x].getWurmId(), false);
/* 118 */                   newItem.insertItem(itarr[x], true);
/*     */                 }
/* 120 */                 catch (NoSuchItemException nsi) {
/*     */                   
/* 122 */                   logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*     */                 } 
/*     */               } 
/*     */             } 
/* 126 */             if (item.isPassFullData())
/* 127 */               newItem.setData(item.getData()); 
/* 128 */             newItem.setLastOwnerId(item.getLastOwnerId());
/*     */             
/* 130 */             if (newItem.hasPrimarySkill()) {
/*     */ 
/*     */               
/*     */               try {
/*     */ 
/*     */                 
/* 136 */                 itemPrimarySkill = newItem.getPrimarySkill();
/*     */               }
/* 138 */               catch (NoSuchSkillException noSuchSkillException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 145 */                 performer = Server.getInstance().getCreature(newItem.getLastOwnerId());
/*     */               }
/* 147 */               catch (Exception exception) {}
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 153 */             Items.destroyItem(item.getWurmId());
/* 154 */             if (this.keepWeight) {
/* 155 */               newItem.setWeight(item.getWeightGrams(), false);
/*     */             }
/*     */             else {
/*     */               
/* 159 */               int currweight = item.getWeightGrams();
/* 160 */               float mod = currweight / item.getTemplate().getWeightGrams();
/* 161 */               if (item.getTemplateId() == 684)
/* 162 */                 mod *= 0.8F; 
/* 163 */               int newWeight = (int)(newItem.getTemplate().getWeightGrams() * mod);
/* 164 */               newItem.setWeight(newWeight, false);
/*     */             } 
/* 166 */             if (newItem.getWeightGrams() > 0) {
/*     */               
/* 168 */               newItem.setTemperature(newTemp);
/*     */               
/* 170 */               if (!parent.insertItem(newItem, true)) {
/*     */                 
/* 172 */                 logger.log(Level.WARNING, parent.getName() + " failed to insert item " + newItem.getName());
/* 173 */                 if (newItem.getWeightGrams() > parent.getFreeVolume()) {
/*     */                   
/* 175 */                   logger.log(Level.INFO, "Old weight=" + newItem.getWeightGrams() + ", trying to set weight to " + parent
/* 176 */                       .getFreeVolume());
/* 177 */                   newItem.setWeight(parent.getFreeVolume(), true);
/* 178 */                   if (parent.insertItem(newItem)) {
/* 179 */                     logger.log(Level.INFO, "THAT did the trick:)");
/*     */                   } else {
/* 181 */                     logger.log(Level.INFO, "Didn't help.");
/*     */                   } 
/*     */                 } else {
/*     */                   
/* 185 */                   logger.log(Level.INFO, newItem.getName() + ": old weight=" + newItem.getWeightGrams() + ", larger than " + parent
/* 186 */                       .getFreeVolume() + " have to change sizes from " + newItem
/* 187 */                       .getSizeX() + ", " + newItem.getSizeY() + ", " + newItem.getSizeZ() + ".");
/* 188 */                   for (int x = 0; x < 10; x++) {
/*     */                     
/* 190 */                     if (!newItem.depleteSizeWith(Math.max(1, newItem.getSizeX() / 10), 
/* 191 */                         Math.max(1, newItem.getSizeY() / 10), Math.max(1, newItem.getSizeZ() / 10))) {
/*     */                       
/* 193 */                       if (parent.insertItem(newItem)) {
/*     */                         
/* 195 */                         logger.log(Level.INFO, "Managed to insert item with size " + newItem.getSizeX() + ", " + newItem
/* 196 */                             .getSizeY() + ", " + newItem.getSizeZ() + " after " + x + " iterations.");
/*     */ 
/*     */ 
/*     */                         
/*     */                         break;
/*     */                       } 
/*     */                     } else {
/* 203 */                       logger.log(Level.INFO, "Item destroyed. Breaking out.");
/*     */ 
/*     */                       
/*     */                       break;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 212 */               Items.decay(newItem.getWurmId(), newItem.getDbStrings());
/*     */             } 
/*     */             
/* 215 */             giveSkillGainForTemplatePrimarySkill(performer, newItem, itemPrimarySkill);
/*     */           
/*     */           }
/* 218 */           catch (NoSuchTemplateException nst) {
/*     */             
/* 220 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */           }
/* 222 */           catch (FailedException fe) {
/*     */             
/* 224 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */           } 
/*     */           
/* 227 */           return true;
/*     */         }
/*     */       
/* 230 */       } else if (newTemp <= this.temperatureChangeLevel) {
/*     */         
/* 232 */         if (!this.atIncrease)
/*     */         {
/*     */           
/*     */           try {
/* 236 */             Item newItem = null;
/* 237 */             if (this.keepMaterial) {
/* 238 */               newItem = ItemFactory.createItem(this.newItemTemplateId, item.getCurrentQualityLevel() * qualityRatio, item
/* 239 */                   .getMaterial(), item.getRarity(), item.creator);
/*     */             } else {
/* 241 */               newItem = ItemFactory.createItem(this.newItemTemplateId, item.getCurrentQualityLevel() * qualityRatio, item.creator);
/*     */             } 
/*     */             
/* 244 */             newItem.setLastOwnerId(item.getLastOwnerId());
/* 245 */             Items.destroyItem(item.getWurmId());
/* 246 */             newItem.setTemperature(newTemp);
/* 247 */             if (this.keepWeight)
/* 248 */               newItem.setWeight(item.getWeightGrams(), false); 
/* 249 */             if (newItem.getWeightGrams() > 0) {
/* 250 */               parent.insertItem(newItem, true);
/*     */             } else {
/* 252 */               Items.decay(newItem.getWurmId(), newItem.getDbStrings());
/*     */             } 
/* 254 */           } catch (NoSuchTemplateException nst) {
/*     */             
/* 256 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */           }
/* 258 */           catch (FailedException fe) {
/*     */             
/* 260 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */           } 
/* 262 */           return true;
/*     */         }
/*     */       
/*     */       } 
/* 266 */     } else if (item.isFood()) {
/*     */       
/* 268 */       if (newTemp > 2700)
/*     */       {
/* 270 */         item.setDamage(item.getDamage() + Math.max(0.1F, (newTemp - oldTemp) / 10.0F));
/*     */       }
/*     */     } 
/* 273 */     return false;
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
/*     */   boolean passedLevel(short oldTemp, short newTemp) {
/* 285 */     if (oldTemp > this.temperatureChangeLevel && newTemp <= this.temperatureChangeLevel)
/* 286 */       return true; 
/* 287 */     if (oldTemp < this.temperatureChangeLevel && newTemp >= this.temperatureChangeLevel)
/* 288 */       return true; 
/* 289 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getOrigItemTemplateId() {
/* 300 */     return this.origItemTemplateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getNewItemTemplateId() {
/* 311 */     return this.newItemTemplateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short getTemperatureChangeLevel() {
/* 322 */     return this.temperatureChangeLevel;
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
/*     */   boolean isAtIncrease() {
/* 334 */     return this.atIncrease;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isKeepWeight() {
/* 345 */     return this.keepWeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isKeepMaterial() {
/* 356 */     return this.keepMaterial;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 367 */     int prime = 31;
/* 368 */     int result = 1;
/* 369 */     result = 31 * result + (this.atIncrease ? 1231 : 1237);
/* 370 */     result = 31 * result + (this.keepMaterial ? 1231 : 1237);
/* 371 */     result = 31 * result + (this.keepWeight ? 1231 : 1237);
/* 372 */     result = 31 * result + this.newItemTemplateId;
/* 373 */     result = 31 * result + this.origItemTemplateId;
/* 374 */     result = 31 * result + this.temperatureChangeLevel;
/* 375 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 386 */     if (this == obj)
/*     */     {
/* 388 */       return true;
/*     */     }
/* 390 */     if (obj == null)
/*     */     {
/* 392 */       return false;
/*     */     }
/* 394 */     if (!(obj instanceof TempState))
/*     */     {
/* 396 */       return false;
/*     */     }
/* 398 */     TempState other = (TempState)obj;
/* 399 */     if (this.atIncrease != other.atIncrease || this.keepMaterial != other.keepMaterial || this.keepWeight != other.keepWeight || this.newItemTemplateId != other.newItemTemplateId || this.origItemTemplateId != other.origItemTemplateId || this.temperatureChangeLevel != other.temperatureChangeLevel)
/*     */     {
/*     */ 
/*     */       
/* 403 */       return false;
/*     */     }
/* 405 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 416 */     return "TempState [atIncrease=" + this.atIncrease + ", keepMaterial=" + this.keepMaterial + ", keepWeight=" + this.keepWeight + ", newItemTemplateId=" + this.newItemTemplateId + ", origItemTemplateId=" + this.origItemTemplateId + ", temperatureChangeLevel=" + this.temperatureChangeLevel + "]";
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
/*     */   private void giveSkillGainForTemplatePrimarySkill(Creature performer, Item newItem, int skillId) {
/*     */     Skill skill;
/* 436 */     if (performer == null) {
/*     */       return;
/*     */     }
/* 439 */     if (newItem == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 444 */     Skills skills = performer.getSkills();
/*     */ 
/*     */     
/*     */     try {
/* 448 */       skill = skills.getSkill(skillId);
/*     */     }
/* 450 */     catch (NoSuchSkillException ss) {
/*     */       
/* 452 */       skill = skills.learn(skillId, 1.0F);
/*     */     } 
/*     */     
/* 455 */     float diff = newItem.getTemplate().getDifficulty();
/* 456 */     if (skill != null)
/*     */     {
/* 458 */       skill.skillCheck(diff, newItem, 0.0D, false, 1.0F);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\TempState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */