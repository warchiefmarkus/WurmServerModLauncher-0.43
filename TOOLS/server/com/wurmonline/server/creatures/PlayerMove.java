/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.players.Player;
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
/*     */ public final class PlayerMove
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(PlayerMove.class.getName());
/*     */   
/*     */   public static final int NOHEIGHTCHANGE = -10000;
/*     */   static final float NOSPEEDCHANGE = -100.0F;
/*     */   static final byte NOWINDCHANGE = -100;
/*     */   static final short NOMOVECHANGE = -100;
/*     */   static final byte NOBRIDGECHANGE = 0;
/*     */   private float newPosX;
/*     */   private float newPosY;
/*     */   private float newPosZ;
/*     */   private float newRot;
/*     */   private byte bm;
/*     */   private byte layer;
/*  48 */   private PlayerMove next = null;
/*  49 */   private int sameMoves = 0;
/*     */   private boolean handled = false;
/*     */   private boolean toggleClimb = false;
/*     */   private boolean climbing = false;
/*     */   private boolean weatherChange = false;
/*  54 */   private float newSpeedMod = -100.0F;
/*  55 */   private byte newWindMod = -100;
/*  56 */   private short newMountSpeed = -100;
/*  57 */   private long newBridgeId = 0L;
/*     */   
/*  59 */   private int newHeightOffset = -10000;
/*     */   private boolean changeHeightImmediately = false;
/*     */   private boolean isOnFloor = false;
/*     */   private boolean isFalling = false;
/*  63 */   int number = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean cleared;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerMove() {
/*  73 */     this.cleared = false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear(boolean clearMoveChanges, MovementScheme ticker, Player player, Logger cheatlogger) {
/*  98 */     PlayerMove cnext = this.next;
/*  99 */     while (cnext != null) {
/*     */       
/* 101 */       if (cnext.cleared) {
/*     */         
/* 103 */         logger.log(Level.INFO, "This (" + cnext + ") was already cleared. Returning. Next=" + this.next, new Exception());
/*     */         return;
/*     */       } 
/* 106 */       if (clearMoveChanges)
/* 107 */         CommuincatorMoveChangeChecker.checkMoveChanges(cnext, ticker, player, cheatlogger); 
/* 108 */       cnext.cleared = true;
/* 109 */       if (cnext.next == cnext) {
/*     */         
/* 111 */         logger.log(Level.INFO, "This (" + cnext + ") was same as this. Returning. Next=" + cnext.next, new Exception());
/* 112 */         this.next = null;
/*     */         
/*     */         return;
/*     */       } 
/* 116 */       PlayerMove nnext = cnext.next;
/*     */       
/* 118 */       cnext.next = null;
/*     */       
/* 120 */       cnext = nnext;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getNewPosX() {
/* 131 */     return this.newPosX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNewPosX(float aNewPosX) {
/* 142 */     this.newPosX = aNewPosX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getNewPosY() {
/* 152 */     return this.newPosY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNewPosY(float aNewPosY) {
/* 163 */     this.newPosY = aNewPosY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getNewPosZ() {
/* 173 */     return this.newPosZ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNewPosZ(float aNewPosZ) {
/* 184 */     this.newPosZ = aNewPosZ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getNewRot() {
/* 194 */     return this.newRot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNewRot(float aNewRot) {
/* 205 */     this.newRot = aNewRot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte getBm() {
/* 215 */     return this.bm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setBm(byte aBm) {
/* 226 */     this.bm = aBm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte getLayer() {
/* 236 */     return this.layer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setLayer(byte aLayer) {
/* 247 */     this.layer = aLayer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerMove getNext() {
/* 257 */     return this.next;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumber() {
/* 262 */     return this.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNext(PlayerMove aNext) {
/* 273 */     this.next = aNext;
/* 274 */     if (this.next != null) {
/* 275 */       this.number++;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSameMoves() {
/* 287 */     return this.sameMoves;
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
/*     */   void incrementSameMoves() {
/* 305 */     this.sameMoves++;
/*     */   }
/*     */ 
/*     */   
/*     */   void sameNoMoves() {
/* 310 */     this.sameMoves = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void resetSameMoves() {
/* 318 */     this.sameMoves = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHandled() {
/* 328 */     return this.handled;
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
/* 339 */     this.handled = aHandled;
/* 340 */     if (this.handled) {
/* 341 */       this.number = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isToggleClimb() {
/* 351 */     return this.toggleClimb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setToggleClimb(boolean aToggleClimb) {
/* 362 */     this.toggleClimb = aToggleClimb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isClimbing() {
/* 372 */     return this.climbing;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isFalling() {
/* 377 */     return this.isFalling;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsFalling(boolean falling) {
/* 382 */     this.isFalling = falling;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setClimbing(boolean aClimbing) {
/* 393 */     this.climbing = aClimbing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isWeatherChange() {
/* 403 */     return this.weatherChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setWeatherChange(boolean aWeatherChange) {
/* 414 */     this.weatherChange = aWeatherChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getNewSpeedMod() {
/* 424 */     return this.newSpeedMod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNewSpeedMod(float aNewSpeedMod) {
/* 435 */     this.newSpeedMod = aNewSpeedMod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte getNewWindMod() {
/* 445 */     return this.newWindMod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNewWindMod(byte aNewWindMod) {
/* 456 */     this.newWindMod = aNewWindMod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short getNewMountSpeed() {
/* 466 */     return this.newMountSpeed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setNewMountSpeed(short aNewMountSpeed) {
/* 477 */     this.newMountSpeed = aNewMountSpeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerMove getLast() {
/* 482 */     if (this.next != null)
/* 483 */       return this.next.getLast(); 
/* 484 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNewHeightOffset() {
/* 494 */     return this.newHeightOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNewHeightOffset(int aNewHeightOffset) {
/* 505 */     this.newHeightOffset = aNewHeightOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChangeHeightImmediately() {
/* 515 */     return this.changeHeightImmediately;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChangeHeightImmediately(boolean aChangeHeightImmediately) {
/* 526 */     this.changeHeightImmediately = aChangeHeightImmediately;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNewBridgeId() {
/* 534 */     return this.newBridgeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNewBridgeId(long newBridgeId) {
/* 543 */     this.newBridgeId = newBridgeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOnFloor() {
/* 553 */     return this.isOnFloor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnFloor(boolean aIsOnFloor) {
/* 564 */     this.isOnFloor = aIsOnFloor;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\PlayerMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */