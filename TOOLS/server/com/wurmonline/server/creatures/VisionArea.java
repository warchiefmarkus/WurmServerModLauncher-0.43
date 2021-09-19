/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.zones.VirtualZone;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class VisionArea
/*     */ {
/*  39 */   private static Logger logger = Logger.getLogger(VisionArea.class.getName());
/*     */   
/*     */   private Creature owner;
/*     */   
/*     */   private final int startPosX;
/*     */   private final int startPosY;
/*  45 */   private int xPos = 0;
/*  46 */   private int yPos = 0;
/*     */   
/*     */   private static final int playerViewSize = 150;
/*     */   
/*     */   private boolean initialized = false;
/*     */   
/*     */   private boolean sentCloseStrips = false;
/*     */   private boolean sentFarStrips = false;
/*     */   private boolean resumed = false;
/*  55 */   private int currentStrip = 0;
/*     */   
/*     */   private static final long divModifier = 16L;
/*     */   private VirtualZone surfaceViewField;
/*     */   private VirtualZone underGroundViewField;
/*     */   private boolean seesCaves = false;
/*  61 */   private int caveMoveCounter = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   VisionArea(Creature aOwner, int size) {
/*  70 */     this.owner = aOwner;
/*  71 */     this.xPos = aOwner.getTileX();
/*  72 */     this.yPos = aOwner.getTileY();
/*  73 */     this.startPosX = this.xPos;
/*  74 */     this.startPosY = this.yPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     this.surfaceViewField = Zones.createZone(aOwner, this.xPos - size, this.yPos - size, this.xPos, this.yPos, size, true);
/*  81 */     if (aOwner.isPlayer()) {
/*  82 */       this.underGroundViewField = Zones.createZone(aOwner, this.xPos - 24, this.yPos - 24, this.xPos, this.yPos, 24, false);
/*     */     } else {
/*  84 */       this.underGroundViewField = Zones.createZone(aOwner, this.xPos - size, this.yPos - size, this.xPos, this.yPos, size, false);
/*  85 */     }  if (this.owner.isPlayer()) {
/*     */       
/*  87 */       this.seesCaves = !this.owner.isOnSurface();
/*  88 */       if (!this.seesCaves) {
/*  89 */         this.seesCaves = this.underGroundViewField.shouldSeeCaves();
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  94 */       this.surfaceViewField.initialize();
/*  95 */       this.underGroundViewField.initialize();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isNearCave() {
/* 102 */     if (!this.owner.isOnSurface())
/* 103 */       return true; 
/* 104 */     return this.seesCaves;
/*     */   }
/*     */ 
/*     */   
/*     */   public VirtualZone getSurface() {
/* 109 */     return this.surfaceViewField;
/*     */   }
/*     */ 
/*     */   
/*     */   public VirtualZone getUnderGround() {
/* 114 */     return this.underGroundViewField;
/*     */   }
/*     */ 
/*     */   
/*     */   public void broadCastUpdateSelectBar(long toUpdate) {
/* 119 */     broadCastUpdateSelectBar(toUpdate, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void broadCastUpdateSelectBar(long toUpdate, boolean keepSelection) {
/* 124 */     Zone[] zones = Zones.getZonesCoveredBy(getSurface());
/* 125 */     List<Creature> broadcastTargets = new ArrayList<>();
/* 126 */     if (zones != null) {
/*     */       
/* 128 */       for (int j = 0; j < zones.length; j++) {
/*     */         
/* 130 */         List<Creature> pWatchers = zones[j].getPlayerWatchers();
/* 131 */         for (int k = 0; k < pWatchers.size(); k++) {
/*     */           
/* 133 */           if (!broadcastTargets.contains(pWatchers.get(k)))
/* 134 */             broadcastTargets.add(pWatchers.get(k)); 
/*     */         } 
/*     */       } 
/* 137 */       zones = null;
/*     */     } 
/*     */     
/* 140 */     zones = Zones.getZonesCoveredBy(getUnderGround());
/* 141 */     if (zones != null) {
/*     */       
/* 143 */       for (int j = 0; j < zones.length; j++) {
/*     */         
/* 145 */         List<Creature> pWatchers = zones[j].getPlayerWatchers();
/* 146 */         for (int k = 0; k < pWatchers.size(); k++) {
/*     */           
/* 148 */           if (!broadcastTargets.contains(pWatchers.get(k)))
/* 149 */             broadcastTargets.add(pWatchers.get(k)); 
/*     */         } 
/*     */       } 
/* 152 */       zones = null;
/*     */     } 
/*     */     
/* 155 */     for (int i = 0; i < broadcastTargets.size(); i++) {
/*     */       
/* 157 */       Creature target = broadcastTargets.get(i);
/* 158 */       target.getCommunicator().sendUpdateSelectBar(toUpdate, keepSelection);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDistance(int xFrom, int yFrom, int xTo, int yTo) {
/* 173 */     return Math.max(Math.abs(xTo - xFrom), Math.abs(yTo - yFrom));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(int tilex, int tiley) {
/* 178 */     return (tilex > this.xPos - 152 && tilex < this.xPos + 153 && tiley > this.yPos - 152 && tiley < this.yPos + 153);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsCave(int tilex, int tiley) {
/* 184 */     return (tilex >= this.xPos - 24 && tilex < this.xPos + 23 && tiley >= this.yPos - 24 && tiley < this.yPos + 23);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void linkZones(int xChange, int yChange) {
/* 191 */     while (xChange != 0) {
/*     */       
/* 193 */       this.surfaceViewField.linkVisionArea();
/* 194 */       this.underGroundViewField.linkVisionArea();
/* 195 */       if (xChange > 0) {
/*     */         
/* 197 */         xChange--;
/*     */         
/*     */         continue;
/*     */       } 
/* 201 */       xChange++;
/*     */     } 
/*     */     
/* 204 */     while (yChange != 0) {
/*     */       
/* 206 */       this.surfaceViewField.linkVisionArea();
/* 207 */       this.underGroundViewField.linkVisionArea();
/* 208 */       if (yChange > 0) {
/*     */         
/* 210 */         yChange--;
/*     */         
/*     */         continue;
/*     */       } 
/* 214 */       yChange++;
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
/*     */   
/*     */   private void increaseCaveMoveCounter() {
/* 227 */     this.caveMoveCounter++;
/* 228 */     if (this.caveMoveCounter > 5) {
/*     */       
/* 230 */       checkCaves(false);
/* 231 */       this.caveMoveCounter = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkCaves(boolean initialize) {
/* 237 */     if (this.owner.isPlayer())
/*     */     {
/* 239 */       if (this.seesCaves) {
/*     */         
/* 241 */         if (this.owner.isOnSurface())
/*     */         {
/* 243 */           this.seesCaves = this.underGroundViewField.shouldSeeCaves();
/*     */         }
/* 245 */         if (initialize) {
/* 246 */           initializeCaves();
/*     */         
/*     */         }
/*     */       }
/* 250 */       else if (this.underGroundViewField.shouldSeeCaves() || initialize) {
/*     */         
/* 252 */         this.seesCaves = true;
/* 253 */         initializeCaves();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(int xChange, int yChange) throws IOException {
/* 264 */     while (xChange != 0) {
/*     */       boolean positive;
/* 266 */       if (xChange > 0) {
/*     */         
/* 268 */         this.xPos++;
/* 269 */         this.surfaceViewField.move(1, 0);
/* 270 */         this.underGroundViewField.move(1, 0);
/* 271 */         xChange--;
/* 272 */         positive = true;
/*     */       }
/*     */       else {
/*     */         
/* 276 */         this.xPos--;
/* 277 */         this.surfaceViewField.move(-1, 0);
/* 278 */         this.underGroundViewField.move(-1, 0);
/* 279 */         xChange++;
/* 280 */         positive = false;
/*     */       } 
/* 282 */       if (this.owner.isPlayer()) {
/*     */         
/* 284 */         increaseCaveMoveCounter();
/* 285 */         sendVerticalStrip(positive);
/* 286 */         sendVerticalCaveStrip(positive);
/* 287 */         if ((this.xPos - this.startPosX) % 16L == 0L) {
/* 288 */           sendVerticalStripFar(positive);
/*     */         }
/*     */       } 
/*     */     } 
/* 292 */     while (yChange != 0) {
/*     */       boolean positive;
/* 294 */       if (yChange > 0) {
/*     */         
/* 296 */         this.yPos++;
/* 297 */         this.surfaceViewField.move(0, 1);
/* 298 */         this.underGroundViewField.move(0, 1);
/* 299 */         yChange--;
/* 300 */         positive = true;
/*     */       }
/*     */       else {
/*     */         
/* 304 */         this.yPos--;
/* 305 */         this.surfaceViewField.move(0, -1);
/* 306 */         this.underGroundViewField.move(0, -1);
/* 307 */         yChange++;
/* 308 */         positive = false;
/*     */       } 
/* 310 */       if (this.owner.isPlayer()) {
/*     */         
/* 312 */         increaseCaveMoveCounter();
/* 313 */         sendHorisontalStrip(positive);
/* 314 */         sendHorisontalCaveStrip(positive);
/* 315 */         if ((this.yPos - this.startPosY) % 16L == 0L) {
/* 316 */           sendHorisontalStripFar(positive);
/*     */         }
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 334 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   
/*     */   void destroy() {
/* 339 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 341 */       logger.finest("Destroying varea " + ((this.surfaceViewField != null) ? 1 : 0) + "=sview, uview=" + ((this.underGroundViewField != null) ? 1 : 0));
/*     */     }
/* 343 */     if (this.surfaceViewField != null) {
/*     */       
/* 345 */       this.surfaceViewField.stopWatching();
/* 346 */       Zones.removeZone(this.surfaceViewField.getId());
/*     */     } 
/* 348 */     if (this.underGroundViewField != null) {
/*     */       
/* 350 */       this.underGroundViewField.stopWatching();
/* 351 */       Zones.removeZone(this.underGroundViewField.getId());
/*     */     } 
/* 353 */     this.underGroundViewField = null;
/* 354 */     this.surfaceViewField = null;
/* 355 */     this.owner = null;
/*     */   }
/*     */ 
/*     */   
/*     */   void refreshAttitudes() {
/* 360 */     if (this.surfaceViewField != null)
/*     */     {
/* 362 */       this.surfaceViewField.refreshAttitudes();
/*     */     }
/* 364 */     if (this.underGroundViewField != null)
/*     */     {
/* 366 */       this.underGroundViewField.refreshAttitudes();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendNextStrip() throws Exception {
/* 372 */     if (this.owner != null) {
/*     */       
/* 374 */       Communicator comm = this.owner.getCommunicator();
/* 375 */       while (!this.sentCloseStrips && comm.getConnection().getUnflushed() < 4096) {
/*     */         
/* 377 */         int y = this.startPosY + this.currentStrip;
/* 378 */         comm.sendTileStrip((short)(this.startPosX - 151), (short)y, 302, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 388 */         if (this.seesCaves && this.currentStrip < 24)
/* 389 */           comm.sendCaveStrip((short)(this.startPosX - 24), (short)(this.startPosY + this.currentStrip), 48, 1); 
/* 390 */         this.currentStrip++;
/* 391 */         int y2 = this.startPosY - this.currentStrip;
/* 392 */         comm.sendTileStrip((short)(this.startPosX - 151), (short)y2, 302, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 401 */         if (this.seesCaves && this.currentStrip < 25)
/* 402 */           comm.sendCaveStrip((short)(this.startPosX - 24), (short)(this.startPosY - this.currentStrip), 48, 1); 
/* 403 */         if (this.currentStrip > 3)
/* 404 */           this.owner.checkOpenMineDoor(); 
/* 405 */         if (this.currentStrip > 152) {
/*     */           
/* 407 */           this.sentCloseStrips = true;
/* 408 */           this.currentStrip = 0;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 413 */       if (this.sentCloseStrips)
/* 414 */         sendFarStrips(); 
/* 415 */       if (this.currentStrip > 10)
/*     */       {
/* 417 */         if (!this.resumed) {
/*     */           
/* 419 */           this.surfaceViewField.initialize();
/* 420 */           this.underGroundViewField.initialize();
/* 421 */           this.owner.spawnFreeItems();
/* 422 */           if (this.owner.getVehicle() > 0L)
/* 423 */             this.owner.getMovementScheme().resendMountSpeed(); 
/* 424 */           this.owner.getMovementScheme().resumeSpeedModifier();
/* 425 */           this.owner.getCommunicator().sendStartMoving();
/* 426 */           this.resumed = true;
/*     */         } 
/*     */       }
/* 429 */       if (this.initialized && 
/* 430 */         !this.resumed) {
/* 431 */         logger.log(Level.WARNING, this.owner.getName() + ": VisionArea was never resumed.");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendFarStrips() {
/* 437 */     if (this.owner != null) {
/*     */       
/* 439 */       Communicator comm = this.owner.getCommunicator();
/* 440 */       while (!this.sentFarStrips && comm.getConnection().getUnflushed() < 4096) {
/*     */         
/* 442 */         comm.sendTileStripFar((short)(int)(this.startPosX / 16L - 151L), (short)(int)(this.startPosY / 16L + this.currentStrip), 302, 1);
/*     */         
/* 444 */         this.currentStrip++;
/* 445 */         comm.sendTileStripFar((short)(int)(this.startPosX / 16L - 151L), (short)(int)(this.startPosY / 16L - this.currentStrip), 302, 1);
/*     */         
/* 447 */         if (this.currentStrip > 152) {
/*     */           
/* 449 */           this.sentFarStrips = true;
/* 450 */           this.initialized = true;
/*     */ 
/*     */           
/* 453 */           this.currentStrip = 0;
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendHorisontalStrip(boolean positive) throws IOException {
/* 483 */     Communicator comm = this.owner.getCommunicator();
/* 484 */     if (positive) {
/*     */       
/* 486 */       int y = this.yPos + 150;
/* 487 */       comm.sendTileStrip((short)(this.xPos - 151), (short)y, 302, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 499 */       int y = this.yPos - 151;
/* 500 */       comm.sendTileStrip((short)(this.xPos - 151), (short)y, 302, 1);
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
/*     */ 
/*     */   
/*     */   private void sendVerticalStrip(boolean positive) throws IOException {
/* 514 */     Communicator comm = this.owner.getCommunicator();
/* 515 */     if (positive) {
/*     */       
/* 517 */       int x = this.xPos + 150;
/* 518 */       comm.sendTileStrip((short)x, (short)(this.yPos - 151), 1, 302);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 530 */       int x = this.xPos - 151;
/* 531 */       comm.sendTileStrip((short)x, (short)(this.yPos - 151), 1, 302);
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
/*     */ 
/*     */   
/*     */   private void sendHorisontalCaveStrip(boolean positive) {
/* 545 */     if (isNearCave()) {
/*     */       
/* 547 */       Communicator comm = this.owner.getCommunicator();
/* 548 */       if (positive) {
/* 549 */         comm.sendCaveStrip((short)(this.xPos - 24), (short)(this.yPos + 23), 48, 1);
/*     */       } else {
/* 551 */         comm.sendCaveStrip((short)(this.xPos - 24), (short)(this.yPos - 24), 48, 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendVerticalCaveStrip(boolean positive) {
/* 557 */     if (isNearCave()) {
/*     */       
/* 559 */       Communicator comm = this.owner.getCommunicator();
/* 560 */       if (positive) {
/* 561 */         comm.sendCaveStrip((short)(this.xPos + 23), (short)(this.yPos - 24), 1, 48);
/*     */       } else {
/* 563 */         comm.sendCaveStrip((short)(this.xPos - 24), (short)(this.yPos - 24), 1, 48);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void initializeCaves() {
/* 573 */     Communicator comm = this.owner.getCommunicator();
/* 574 */     comm.sendCaveStrip((short)(this.xPos - 24), (short)(this.yPos - 24), 48, 48);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendHorisontalStripFar(boolean positive) {
/* 580 */     Communicator comm = this.owner.getCommunicator();
/* 581 */     if (positive) {
/* 582 */       comm.sendTileStripFar((short)(int)(this.xPos / 16L - 151L), (short)(int)(this.yPos / 16L + 150L), 302, 1);
/*     */     } else {
/*     */       
/* 585 */       comm.sendTileStripFar((short)(int)(this.xPos / 16L - 151L), (short)(int)(this.yPos / 16L - 151L), 302, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendVerticalStripFar(boolean positive) {
/* 591 */     Communicator comm = this.owner.getCommunicator();
/* 592 */     if (positive) {
/* 593 */       comm.sendTileStripFar((short)(int)(this.xPos / 16L + 150L), (short)(int)(this.yPos / 16L - 151L), 1, 302);
/*     */     } else {
/*     */       
/* 596 */       comm.sendTileStripFar((short)(int)(this.xPos / 16L - 151L), (short)(int)(this.yPos / 16L - 151L), 1, 302);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 603 */     return "VisionArea [initialised: " + isInitialized() + ", resumed: " + this.resumed + ", sentCloseStrips: " + this.sentCloseStrips + ", sentFarStrips: " + this.sentFarStrips + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\VisionArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */