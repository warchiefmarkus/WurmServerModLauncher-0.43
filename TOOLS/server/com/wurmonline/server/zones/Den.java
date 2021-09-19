/*     */ package com.wurmonline.server.zones;
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
/*     */ public final class Den
/*     */ {
/*     */   private int templateId;
/*     */   private final int tilex;
/*     */   private final int tiley;
/*     */   private final boolean surfaced;
/*     */   
/*     */   Den(int creatureTemplateId, int tileX, int tileY, boolean _surfaced) {
/*  35 */     this.templateId = creatureTemplateId;
/*  36 */     this.tilex = tileX;
/*  37 */     this.tiley = tileY;
/*  38 */     this.surfaced = _surfaced;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTemplateId() {
/*  48 */     return this.templateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setTemplateId(int aTemplateId) {
/*  59 */     this.templateId = aTemplateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTilex() {
/*  69 */     return this.tilex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTiley() {
/*  79 */     return this.tiley;
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
/*     */   public boolean isSurfaced() {
/*  91 */     return this.surfaced;
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
/* 102 */     return "Den [CreatureTemplate: " + this.templateId + ", Tile: " + this.tilex + ", " + this.tiley + ", surfaced: " + this.surfaced + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Den.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */