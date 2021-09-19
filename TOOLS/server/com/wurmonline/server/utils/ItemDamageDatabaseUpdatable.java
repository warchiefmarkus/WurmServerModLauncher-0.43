/*     */ package com.wurmonline.server.utils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ItemDamageDatabaseUpdatable
/*     */   implements WurmDbUpdatable
/*     */ {
/*     */   private final long id;
/*     */   private final float damage;
/*     */   private final long lastMaintained;
/*     */   private final String updateStatement;
/*     */   
/*     */   public ItemDamageDatabaseUpdatable(long aId, float aDamage, long aLastMaintained, String aUpdateStatement) {
/*  52 */     this.id = aId;
/*  53 */     this.damage = aDamage;
/*  54 */     this.lastMaintained = aLastMaintained;
/*  55 */     this.updateStatement = aUpdateStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatabaseUpdateStatement() {
/*  66 */     return this.updateStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getId() {
/*  76 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDamage() {
/*  86 */     return this.damage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastMaintained() {
/*  96 */     return this.lastMaintained;
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
/* 107 */     return "ItemDamageDatabaseUpdatable [id=" + this.id + ", damage=" + this.damage + ", lastMaintained=" + this.lastMaintained + ", updateStatement=" + this.updateStatement + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\ItemDamageDatabaseUpdatable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */