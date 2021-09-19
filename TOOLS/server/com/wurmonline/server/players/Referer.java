/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
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
/*     */ public final class Referer
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final byte R_TYPE_NOTHANDLED = 0;
/*     */   public static final byte R_TYPE_MONEY = 1;
/*     */   static final byte R_TYPE_TIME = 2;
/*     */   private final long wurmid;
/*     */   private final long referer;
/*     */   private boolean money = false;
/*     */   private boolean handled = false;
/*     */   
/*     */   Referer(long aWurmid, long aReferer) {
/*  40 */     this.wurmid = aWurmid;
/*  41 */     this.referer = aReferer;
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
/*     */   Referer(long aWurmid, long aReferer, boolean aMoney, boolean aHandled) {
/*  53 */     this.wurmid = aWurmid;
/*  54 */     this.referer = aReferer;
/*  55 */     this.money = aMoney;
/*  56 */     this.handled = aHandled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getWurmid() {
/*  66 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getReferer() {
/*  76 */     return this.referer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isMoney() {
/*  86 */     return this.money;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setMoney(boolean aMoney) {
/*  97 */     this.money = aMoney;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHandled() {
/* 107 */     return this.handled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setHandled(boolean aHandled) {
/* 118 */     this.handled = aHandled;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Referer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */