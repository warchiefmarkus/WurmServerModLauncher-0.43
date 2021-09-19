/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*     */ import com.wurmonline.shared.constants.StructureStateEnum;
/*     */ import java.io.IOException;
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
/*     */ public final class TempFence
/*     */   extends Fence
/*     */ {
/*     */   private Item fenceItem;
/*     */   
/*     */   public TempFence(StructureConstantsEnum aType, int aTileX, int aTileY, int aHeightOffset, Item item, Tiles.TileBorderDirection aDir, int aZoneId, int aLayer) {
/*  49 */     super(aType, aTileX, aTileY, aHeightOffset, item.getQualityLevel(), aDir, aZoneId, aLayer);
/*  50 */     this.fenceItem = item;
/*  51 */     this.state = StructureStateEnum.FINISHED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZoneId(int zid) {
/*  62 */     this.zoneId = zid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void load() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getQualityLevel() {
/*  90 */     return this.fenceItem.getQualityLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOriginalQualityLevel() {
/*  96 */     return this.fenceItem.getOriginalQualityLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDamage() {
/* 102 */     return this.fenceItem.getDamage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setDamage(float newDam) {
/* 113 */     return this.fenceItem.setDamage(this.fenceItem.getDamage() + newDam);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTemporary() {
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setQualityLevel(float newQl) {
/* 130 */     return this.fenceItem.setQualityLevel(newQl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void improveOrigQualityLevel(float newQl) {
/* 141 */     this.fenceItem.setOriginalQualityLevel(newQl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 152 */     Items.destroyItem(this.fenceItem.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastUsed(long aLastUsed) {
/* 163 */     this.fenceItem.setLastMaintained(aLastUsed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getTempId() {
/* 169 */     return this.fenceItem.getWurmId();
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
/*     */   public void savePermissions() {}
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
/*     */   boolean changeColor(int aNewcolor) {
/* 193 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\TempFence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */