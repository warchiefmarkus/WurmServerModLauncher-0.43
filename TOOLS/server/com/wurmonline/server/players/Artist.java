/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Artist
/*    */ {
/*    */   private final long wurmid;
/*    */   private final boolean sound;
/*    */   private final boolean graphics;
/*    */   
/*    */   public Artist(long wurmId, boolean isSound, boolean isGraphics) {
/* 30 */     this.wurmid = wurmId;
/* 31 */     this.sound = isSound;
/* 32 */     this.graphics = isGraphics;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getWurmid() {
/* 37 */     return this.wurmid;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSound() {
/* 42 */     return this.sound;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isGraphics() {
/* 47 */     return this.graphics;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Artist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */