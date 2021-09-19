/*     */ package com.wurmonline.server.economy;
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
/*     */ public final class Change
/*     */   implements MonetaryConstants, MiscConstants
/*     */ {
/*     */   public static final String NOTHING = "0 irons";
/*     */   public long ironCoins;
/*     */   public long goldCoins;
/*     */   public long silverCoins;
/*     */   public long copperCoins;
/*     */   private static final String AND_SEPARATOR = " and ";
/*     */   private static final String COMMA_SEPARATOR = ", ";
/*     */   private static final String IRON_STRING = " iron";
/*     */   private static final String IRON_SHORT_STRING = "i";
/*     */   private static final String COPPER_STRING = " copper";
/*     */   private static final String COPPER_SHORT_STRING = "c";
/*     */   private static final String SILVER_STRING = " silver";
/*     */   private static final String SILVER_SHORT_STRING = "s";
/*     */   private static final String GOLD_STRING = " gold";
/*     */   private static final String GOLD_SHORT_STRING = "g";
/*     */   
/*     */   public Change(long ironValue) {
/* 123 */     this.goldCoins = ironValue / 1000000L;
/* 124 */     long rest = ironValue % 1000000L;
/* 125 */     this.silverCoins = rest / 10000L;
/* 126 */     rest = ironValue % 10000L;
/* 127 */     this.copperCoins = rest / 100L;
/* 128 */     rest = ironValue % 100L;
/* 129 */     this.ironCoins = rest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getGoldCoins() {
/* 140 */     return this.goldCoins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSilverCoins() {
/* 151 */     return this.silverCoins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCopperCoins() {
/* 162 */     return this.copperCoins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIronCoins() {
/* 173 */     return this.ironCoins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getChangeString() {
/* 183 */     String toSend = "";
/* 184 */     if (this.goldCoins > 0L)
/*     */     {
/* 186 */       toSend = toSend + this.goldCoins + " gold";
/*     */     }
/* 188 */     if (this.silverCoins > 0L) {
/*     */       
/* 190 */       if (this.goldCoins > 0L)
/*     */       {
/* 192 */         if (this.copperCoins > 0L || this.ironCoins > 0L) {
/* 193 */           toSend = toSend + ", ";
/*     */         } else {
/* 195 */           toSend = toSend + " and ";
/*     */         }  } 
/* 197 */       toSend = toSend + this.silverCoins + " silver";
/*     */     } 
/* 199 */     if (this.copperCoins > 0L) {
/*     */       
/* 201 */       if (this.silverCoins > 0L || this.goldCoins > 0L)
/*     */       {
/* 203 */         if (this.ironCoins > 0L) {
/* 204 */           toSend = toSend + ", ";
/*     */         } else {
/* 206 */           toSend = toSend + " and ";
/*     */         }  } 
/* 208 */       toSend = toSend + this.copperCoins + " copper";
/*     */     } 
/* 210 */     if (this.ironCoins > 0L) {
/*     */       
/* 212 */       if (this.silverCoins > 0L || this.goldCoins > 0L || this.copperCoins > 0L)
/* 213 */         toSend = toSend + " and "; 
/* 214 */       toSend = toSend + this.ironCoins + " iron";
/*     */     } 
/* 216 */     if (toSend.length() == 0) {
/* 217 */       return "0 irons";
/*     */     }
/* 219 */     return toSend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getChangeShortString() {
/* 229 */     StringBuilder toSend = new StringBuilder();
/* 230 */     if (this.goldCoins > 0L)
/*     */     {
/* 232 */       toSend.append(this.goldCoins).append("g");
/*     */     }
/* 234 */     if (this.silverCoins > 0L) {
/*     */       
/* 236 */       if (this.goldCoins > 0L)
/*     */       {
/* 238 */         toSend.append(", ");
/*     */       }
/* 240 */       toSend.append(this.silverCoins).append("s");
/*     */     } 
/* 242 */     if (this.copperCoins > 0L) {
/*     */       
/* 244 */       if (this.silverCoins > 0L || this.goldCoins > 0L)
/*     */       {
/* 246 */         toSend.append(", ");
/*     */       }
/* 248 */       toSend.append(this.copperCoins).append("c");
/*     */     } 
/* 250 */     if (this.ironCoins > 0L) {
/*     */       
/* 252 */       if (this.silverCoins > 0L || this.goldCoins > 0L || this.copperCoins > 0L)
/*     */       {
/* 254 */         toSend.append(", ");
/*     */       }
/* 256 */       toSend.append(this.ironCoins).append("i");
/*     */     } 
/* 258 */     return toSend.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\Change.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */