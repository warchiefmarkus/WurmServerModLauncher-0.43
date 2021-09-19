/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.creatures.Creature;
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
/*     */ public class CreationRequirement
/*     */ {
/*     */   private final int resourceTemplateId;
/*     */   private final int resourceNumber;
/*     */   private float qualityLevelNeeded;
/*  38 */   private float maxDamageAllowed = 50.0F;
/*     */ 
/*     */ 
/*     */   
/*  42 */   private int volumeNeeded = 0;
/*     */   private final boolean consumed;
/*  44 */   private int distance = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int number;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreationRequirement(int aNumber, int aResourceTemplateId, int aResourceNumber, boolean aConsume) {
/*  62 */     this.resourceTemplateId = aResourceTemplateId;
/*  63 */     this.resourceNumber = aResourceNumber;
/*  64 */     this.consumed = aConsume;
/*  65 */     this.number = aNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getNumber() {
/*  76 */     return this.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setQualityLevelNeeded(float needed) {
/*  85 */     this.qualityLevelNeeded = needed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setMaxDamageAllowed(float allowed) {
/*  94 */     this.maxDamageAllowed = allowed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getDistance() {
/* 104 */     return this.distance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDistance(int aDistance) {
/* 115 */     this.distance = aDistance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setVolumeNeeded(int volume) {
/* 124 */     this.volumeNeeded = volume;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean willBeConsumed() {
/* 129 */     return this.consumed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getVolumeNeeded() {
/* 138 */     return this.volumeNeeded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getResourceTemplateId() {
/* 147 */     return this.resourceTemplateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getResourceNumber() {
/* 156 */     return this.resourceNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getQualityLevelNeeded() {
/* 165 */     return this.qualityLevelNeeded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getMaxDamageAllowed() {
/* 174 */     return this.maxDamageAllowed;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean fill(Creature performer, Item creation) {
/* 179 */     if (canBeFilled(performer)) {
/*     */       
/* 181 */       int found = 0;
/* 182 */       Item inventory = performer.getInventory();
/* 183 */       Item[] items = inventory.getAllItems(false);
/* 184 */       for (int i = 0; i < items.length; i++) {
/*     */         
/* 186 */         if (items[i].getTemplateId() == this.resourceTemplateId) {
/*     */           
/* 188 */           found++;
/* 189 */           Items.destroyItem(items[i].getWurmId());
/* 190 */           if (found == this.resourceNumber)
/* 191 */             return true; 
/*     */         } 
/*     */       } 
/* 194 */       Item body = performer.getBody().getBodyItem();
/* 195 */       items = body.getAllItems(false);
/* 196 */       for (int j = 0; j < items.length; j++) {
/*     */         
/* 198 */         if (items[j].getTemplateId() == this.resourceTemplateId) {
/*     */           
/* 200 */           found++;
/* 201 */           Items.destroyItem(items[j].getWurmId());
/* 202 */           if (found == this.resourceNumber)
/* 203 */             return true; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean canBeFilled(Creature performer) {
/* 212 */     int found = 0;
/* 213 */     Item inventory = performer.getInventory();
/* 214 */     Item[] items = inventory.getAllItems(false);
/* 215 */     for (int i = 0; i < items.length; i++) {
/*     */       
/* 217 */       if (items[i].getTemplateId() == this.resourceTemplateId) {
/*     */         
/* 219 */         found++;
/* 220 */         if (found == this.resourceNumber)
/* 221 */           return true; 
/*     */       } 
/*     */     } 
/* 224 */     Item body = performer.getBody().getBodyItem();
/* 225 */     items = body.getAllItems(false);
/* 226 */     for (int j = 0; j < items.length; j++) {
/*     */       
/* 228 */       if (items[j].getTemplateId() == this.resourceTemplateId) {
/*     */         
/* 230 */         found++;
/* 231 */         if (found == this.resourceNumber)
/* 232 */           return true; 
/*     */       } 
/*     */     } 
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean canRunOnce(Creature performer) {
/* 240 */     Item inventory = performer.getInventory();
/* 241 */     Item[] items = inventory.getAllItems(false);
/* 242 */     for (int i = 0; i < items.length; i++) {
/*     */       
/* 244 */       if (items[i].getTemplateId() == this.resourceTemplateId)
/* 245 */         return true; 
/*     */     } 
/* 247 */     Item body = performer.getBody().getBodyItem();
/* 248 */     items = body.getAllItems(false);
/* 249 */     for (int j = 0; j < items.length; j++) {
/*     */       
/* 251 */       if (items[j].getTemplateId() == this.resourceTemplateId)
/* 252 */         return true; 
/*     */     } 
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean runOnce(Creature performer) {
/* 259 */     Item inventory = performer.getInventory();
/* 260 */     Item[] items = inventory.getAllItems(false);
/* 261 */     for (int i = 0; i < items.length; i++) {
/*     */       
/* 263 */       if (items[i].getTemplateId() == this.resourceTemplateId) {
/*     */         
/* 265 */         Items.destroyItem(items[i].getWurmId());
/* 266 */         return true;
/*     */       } 
/*     */     } 
/* 269 */     Item body = performer.getBody().getBodyItem();
/* 270 */     items = body.getAllItems(false);
/* 271 */     for (int j = 0; j < items.length; j++) {
/*     */       
/* 273 */       if (items[j].getTemplateId() == this.resourceTemplateId) {
/*     */         
/* 275 */         Items.destroyItem(items[j].getWurmId());
/* 276 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 280 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\CreationRequirement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */