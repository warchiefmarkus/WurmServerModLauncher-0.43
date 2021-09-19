/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
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
/*     */ public final class Order
/*     */   implements MiscConstants, CounterTypes
/*     */ {
/*     */   private final int tilex;
/*     */   private final int tiley;
/*     */   private final int layer;
/*     */   private final long wurmid;
/*     */   
/*     */   public Order(int tx, int ty, int lay) {
/*  50 */     this.wurmid = -10L;
/*  51 */     this.tilex = tx;
/*  52 */     this.tiley = ty;
/*  53 */     this.layer = lay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Order(long wid) {
/*  64 */     this.wurmid = wid;
/*  65 */     this.tilex = -1;
/*  66 */     this.tiley = -1;
/*  67 */     this.layer = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTile() {
/*  72 */     return (this.tilex != -1);
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
/*     */   private boolean isItem() {
/*  86 */     if (this.wurmid != -10L)
/*     */     {
/*  88 */       return (WurmId.getType(this.wurmid) == 2 || WurmId.getType(this.wurmid) == 19 || WurmId.getType(this.wurmid) == 20);
/*     */     }
/*     */ 
/*     */     
/*  92 */     return false;
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
/*     */   public boolean isCreature() {
/* 106 */     if (this.wurmid != -10L)
/*     */     {
/* 108 */       return (WurmId.getType(this.wurmid) == 1 || WurmId.getType(this.wurmid) == 0);
/*     */     }
/*     */ 
/*     */     
/* 112 */     return false;
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
/*     */   public boolean isResolved(int tx, int ty, int lay) {
/* 129 */     return (tx == this.tilex && ty == this.tiley && this.layer == lay);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResolved(long wid) {
/* 140 */     return (wid == this.wurmid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Creature getCreature() {
/* 150 */     if (isCreature()) {
/*     */       
/*     */       try {
/*     */         
/* 154 */         return Server.getInstance().getCreature(this.wurmid);
/*     */       }
/* 156 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItem() {
/* 170 */     if (isItem()) {
/*     */       
/*     */       try {
/*     */         
/* 174 */         return Items.getItem(this.wurmid);
/*     */       }
/* 176 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 180 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTileX() {
/* 190 */     return this.tilex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTileY() {
/* 200 */     return this.tiley;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\Order.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */