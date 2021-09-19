/*      */ package com.wurmonline.shared.util;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class MovementChecker
/*      */ {
/*   23 */   protected static final Logger logger = Logger.getLogger(MovementChecker.class.getName());
/*      */   
/*   25 */   int blocks = 0;
/*      */   
/*      */   public static final float DEGS_TO_RADS = 0.017453292F;
/*      */   
/*      */   public static final int BIT_FORWARD = 1;
/*      */   
/*      */   public static final int BIT_BACK = 2;
/*      */   
/*      */   public static final int BIT_LEFT = 4;
/*      */   
/*      */   public static final int BIT_RIGHT = 8;
/*      */   
/*      */   private static final float WALK_SPEED = 0.08F;
/*      */   
/*      */   public static final float FLOATING_HEIGHT = -1.45F;
/*      */   
/*      */   public boolean serverWestAvailable = true;
/*      */   
/*      */   public boolean serverNorthAvailable = true;
/*      */   
/*      */   public boolean serverEastAvailable = true;
/*      */   
/*      */   public boolean serverSouthAvailable = true;
/*      */   
/*      */   private static final float CLIMB_SPEED_MODIFIER = 0.25F;
/*   50 */   private float speedMod = 1.0F;
/*      */   
/*      */   private boolean climbing = false;
/*      */   
/*   54 */   private long bridgeId = -10L;
/*      */   
/*   56 */   private int bridgeCounter = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private float x;
/*      */ 
/*      */ 
/*      */   
/*      */   private float y;
/*      */ 
/*      */ 
/*      */   
/*      */   private float z;
/*      */ 
/*      */ 
/*      */   
/*      */   private float xRot;
/*      */ 
/*      */ 
/*      */   
/*      */   public float xOld;
/*      */ 
/*      */ 
/*      */   
/*      */   public float yOld;
/*      */ 
/*      */ 
/*      */   
/*      */   private float zOld;
/*      */ 
/*      */ 
/*      */   
/*      */   private float xa;
/*      */ 
/*      */ 
/*      */   
/*      */   private float ya;
/*      */ 
/*      */ 
/*      */   
/*      */   private float za;
/*      */ 
/*      */ 
/*      */   
/*      */   private float groundOffset;
/*      */ 
/*      */ 
/*      */   
/*      */   private int targetGroundOffset;
/*      */ 
/*      */ 
/*      */   
/*      */   private byte bitmask;
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean onGround = false;
/*      */ 
/*      */   
/*      */   public boolean inWater = false;
/*      */ 
/*      */   
/*  118 */   private int layer = 0;
/*      */   
/*      */   private boolean abort = false;
/*      */   
/*      */   public boolean ignoreErrors = false;
/*      */   
/*      */   public boolean started = false;
/*      */   
/*  126 */   public float diffWindX = 0.0F;
/*      */   
/*  128 */   public float diffWindY = 0.0F;
/*      */   
/*  130 */   private float windRotation = 0.0F;
/*      */   
/*  132 */   private float windStrength = 0.0F;
/*      */   
/*  134 */   private float windImpact = 0.0F;
/*      */   
/*  136 */   private float mountSpeed = 0.1F;
/*      */   
/*      */   public boolean commandingBoat = false;
/*      */   
/*  140 */   private float vehicleRotation = 0.0F;
/*      */ 
/*      */   
/*      */   private float currx;
/*      */   
/*      */   private float curry;
/*      */   
/*      */   private boolean first = true;
/*      */   
/*      */   public static final float MINHEIGHTC = -3000.0F;
/*      */   
/*      */   public static final float MAXHEIGHTC = 3000.0F;
/*      */   
/*      */   private static final float fallMod = 0.04F;
/*      */   
/*      */   private static final float deltaH = 1.0F;
/*      */   
/*      */   private static final float moveMod = 0.4F;
/*      */   
/*      */   private boolean movingVehicle = false;
/*      */   
/*  161 */   private float leftTurnMod = 1.0F;
/*      */   
/*  163 */   private float rightTurnMod = 1.0F;
/*      */ 
/*      */   
/*  166 */   public float offZ = 0.0F;
/*      */   
/*      */   private boolean flying = false;
/*      */   
/*      */   protected boolean wasOnStair = false;
/*      */   
/*  172 */   private int counter = 0;
/*      */ 
/*      */   
/*      */   private boolean acceptedError = false;
/*      */ 
/*      */   
/*      */   private boolean isFalling = false;
/*      */ 
/*      */   
/*      */   private boolean onFloorOverridden = false;
/*      */ 
/*      */   
/*      */   protected strictfp boolean isPressed(int key) {
/*  185 */     return ((this.bitmask & key) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp void setFlying(boolean fly) {
/*  190 */     this.flying = fly;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp void setIsFalling(boolean falling) {
/*  195 */     this.isFalling = falling;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp boolean isFalling() {
/*  200 */     return this.isFalling;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp boolean isFlying() {
/*  205 */     return this.flying;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp boolean isAborted() {
/*  210 */     return this.abort;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setAbort(boolean abort) {
/*  219 */     this.abort = abort;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp boolean isKeyPressed() {
/*  224 */     return (isPressed(1) || isPressed(2) || isPressed(4) || isPressed(8));
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void resetBm() {
/*  229 */     this.bitmask = 0;
/*      */   }
/*      */   
/*  232 */   float mhdlog = 0.0F;
/*      */ 
/*      */   
/*      */   public final strictfp void movestep(float maxHeightDiff, float xTarget, float zTarget, float yTarget, float maxDepth, float maxHeight, float rotation, byte _bitmask, int estimatedLayer) {
/*  236 */     this.abort = false;
/*  237 */     this.currx = xTarget;
/*  238 */     this.curry = yTarget;
/*  239 */     this.movingVehicle = false;
/*      */     
/*  241 */     this.mhdlog = maxHeightDiff;
/*  242 */     if (estimatedLayer != this.layer) {
/*  243 */       handleWrongLayer(estimatedLayer, this.layer);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  252 */     boolean isCommanding = isCommanding(maxDepth, maxHeight);
/*  253 */     if (isCommanding) {
/*  254 */       maybePrintDebugInfo(1);
/*      */     }
/*      */ 
/*      */     
/*  258 */     if (xTarget != this.x || yTarget != this.y) {
/*      */       
/*  260 */       float expxDist = this.x - this.xOld;
/*  261 */       float expyDist = this.y - this.yOld;
/*  262 */       float expectedDistance = (float)StrictMath.sqrt((expxDist * expxDist + expyDist * expyDist));
/*      */       
/*  264 */       float realxDist = xTarget - this.xOld;
/*  265 */       float realyDist = yTarget - this.yOld;
/*  266 */       float realDistance = (float)StrictMath.sqrt((realxDist * realxDist + realyDist * realyDist));
/*      */       
/*  268 */       if (this.bridgeCounter <= 0 && !movedOnStair())
/*      */       {
/*      */         
/*  271 */         if (!this.isFalling && realDistance > expectedDistance) {
/*      */           
/*  273 */           if (this.acceptedError)
/*      */           {
/*  275 */             handleMoveTooFar(realDistance, expectedDistance);
/*      */           }
/*      */           else
/*      */           {
/*  279 */             this.acceptedError = true;
/*      */           
/*      */           }
/*      */         
/*      */         }
/*  284 */         else if (this.acceptedError) {
/*      */           
/*  286 */           handleMoveTooShort(realDistance, expectedDistance);
/*      */         } else {
/*      */           
/*  289 */           this.acceptedError = true;
/*      */         }
/*      */       
/*      */       }
/*  293 */     } else if (zTarget != this.z && Math.abs(zTarget - this.z) > 0.25F) {
/*      */ 
/*      */       
/*  296 */       if (!this.isFalling && 
/*  297 */         this.bridgeCounter <= 0 && this.bridgeId == -10L && !movedOnStair())
/*      */       {
/*  299 */         if (this.acceptedError)
/*      */         {
/*  301 */           handleZError(zTarget, this.z);
/*      */         }
/*      */         else
/*      */         {
/*  305 */           this.acceptedError = true;
/*      */         }
/*      */       
/*      */       }
/*      */     } else {
/*      */       
/*  311 */       this.acceptedError = false;
/*      */     } 
/*      */     
/*  314 */     this.bridgeCounter = Math.max(0, this.bridgeCounter - 1);
/*  315 */     if (this.abort) {
/*      */       return;
/*      */     }
/*  318 */     int currentTileX = (int)(xTarget / 4.0F);
/*  319 */     int currentTileY = (int)(yTarget / 4.0F);
/*      */     
/*  321 */     this.x = xTarget;
/*  322 */     this.y = yTarget;
/*  323 */     this.z = zTarget;
/*  324 */     this.layer = estimatedLayer;
/*  325 */     this.xRot = rotation;
/*  326 */     this.bitmask = _bitmask;
/*      */     
/*  328 */     float speedModifier = this.speedMod;
/*  329 */     float heightTarget = getHeight(this.x, this.y, -3000.0F);
/*      */     
/*  331 */     this.inWater = (this.z + this.za <= -1.0F);
/*  332 */     if (isCommanding)
/*  333 */       this.inWater = false; 
/*  334 */     int dirs = 0;
/*  335 */     float xPosMod = 0.0F;
/*  336 */     float yPosMod = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  341 */     if (!this.onGround && !this.inWater && !this.commandingBoat && !isOnFloor())
/*      */     {
/*  343 */       speedModifier *= 0.1F;
/*      */     }
/*      */     
/*  346 */     speedModifier *= 1.5F;
/*      */     
/*  348 */     if (this.climbing) {
/*  349 */       speedModifier *= 0.25F;
/*      */     }
/*      */ 
/*      */     
/*  353 */     if (isPressed(1)) {
/*      */       
/*  355 */       dirs++;
/*  356 */       if (isCommanding) {
/*      */         
/*  358 */         if (speedModifier > 0.0F)
/*      */         {
/*  360 */           xPosMod += (float)StrictMath.sin((this.vehicleRotation * 0.017453292F)) * this.mountSpeed;
/*      */           
/*  362 */           if (!this.serverWestAvailable && xPosMod < 0.0F) {
/*  363 */             xPosMod = 0.0F;
/*  364 */           } else if (!this.serverEastAvailable && xPosMod > 0.0F) {
/*  365 */             xPosMod = 0.0F;
/*  366 */           }  yPosMod -= (float)StrictMath.cos((this.vehicleRotation * 0.017453292F)) * this.mountSpeed;
/*      */           
/*  368 */           if (!this.serverNorthAvailable && yPosMod < 0.0F) {
/*  369 */             yPosMod = 0.0F;
/*  370 */           } else if (!this.serverSouthAvailable && yPosMod > 0.0F) {
/*  371 */             yPosMod = 0.0F;
/*      */           } 
/*  373 */           this.movingVehicle = true;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  378 */         xPosMod += (float)StrictMath.sin((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*  379 */         yPosMod -= (float)StrictMath.cos((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  386 */     if (isPressed(2)) {
/*      */       
/*  388 */       dirs++;
/*  389 */       if (isCommanding) {
/*      */ 
/*      */         
/*  392 */         if (speedModifier > 0.0F)
/*      */         {
/*  394 */           xPosMod -= (float)StrictMath.sin((this.vehicleRotation * 0.017453292F)) * this.mountSpeed * 0.3F;
/*  395 */           if (!this.serverWestAvailable && xPosMod < 0.0F) {
/*  396 */             xPosMod = 0.0F;
/*  397 */           } else if (!this.serverEastAvailable && xPosMod > 0.0F) {
/*  398 */             xPosMod = 0.0F;
/*  399 */           }  yPosMod += (float)StrictMath.cos((this.vehicleRotation * 0.017453292F)) * this.mountSpeed * 0.3F;
/*      */           
/*  401 */           if (!this.serverNorthAvailable && yPosMod < 0.0F) {
/*  402 */             yPosMod = 0.0F;
/*  403 */           } else if (!this.serverSouthAvailable && yPosMod > 0.0F) {
/*  404 */             yPosMod = 0.0F;
/*  405 */           }  this.movingVehicle = true;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  410 */         xPosMod -= (float)StrictMath.sin((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*  411 */         yPosMod += (float)StrictMath.cos((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  418 */     if (isPressed(4)) {
/*      */       
/*  420 */       dirs++;
/*      */       
/*  422 */       if (isCommanding) {
/*      */         
/*  424 */         if (!this.commandingBoat || this.windImpact != 0.0F)
/*      */         {
/*  426 */           if (!this.commandingBoat) {
/*      */             
/*  428 */             if (this.movingVehicle)
/*      */             {
/*  430 */               this.leftTurnMod++;
/*  431 */               int mod = 3;
/*  432 */               if (this.leftTurnMod > 20.0F)
/*  433 */                 mod = 2; 
/*  434 */               if (this.leftTurnMod > 40.0F)
/*  435 */                 mod = 1; 
/*  436 */               this.vehicleRotation = normalizeAngle(this.vehicleRotation - mod);
/*      */ 
/*      */             
/*      */             }
/*  440 */             else if (speedModifier > 0.0F)
/*      */             {
/*  442 */               this.leftTurnMod++;
/*  443 */               int mod = 3;
/*  444 */               if (this.leftTurnMod > 20.0F)
/*  445 */                 mod = 2; 
/*  446 */               if (this.leftTurnMod > 40.0F)
/*  447 */                 mod = 1; 
/*  448 */               this.vehicleRotation = normalizeAngle(this.vehicleRotation - mod);
/*  449 */               xPosMod += (float)StrictMath.sin((this.vehicleRotation * 0.017453292F)) * this.mountSpeed * 0.3F;
/*      */               
/*  451 */               if (!this.serverWestAvailable && xPosMod < 0.0F) {
/*  452 */                 xPosMod = 0.0F;
/*  453 */               } else if (!this.serverEastAvailable && xPosMod > 0.0F) {
/*  454 */                 xPosMod = 0.0F;
/*  455 */               }  yPosMod -= (float)StrictMath.cos((this.vehicleRotation * 0.017453292F)) * this.mountSpeed * 0.3F;
/*      */               
/*  457 */               if (!this.serverNorthAvailable && yPosMod < 0.0F) {
/*  458 */                 yPosMod = 0.0F;
/*  459 */               } else if (!this.serverSouthAvailable && yPosMod > 0.0F) {
/*  460 */                 yPosMod = 0.0F;
/*      */               } 
/*  462 */               this.movingVehicle = true;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  467 */             this.vehicleRotation = normalizeAngle(this.vehicleRotation - 1.0F);
/*      */           } 
/*      */         }
/*      */       } else {
/*      */         
/*  472 */         xPosMod -= (float)StrictMath.cos((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*  473 */         yPosMod -= (float)StrictMath.sin((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*      */       } 
/*      */     } else {
/*      */       
/*  477 */       this.leftTurnMod = 0.0F;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  482 */     if (isPressed(8)) {
/*      */       
/*  484 */       dirs++;
/*  485 */       if (isCommanding) {
/*      */         
/*  487 */         if (!this.commandingBoat || this.windImpact != 0.0F)
/*      */         {
/*  489 */           if (!this.commandingBoat) {
/*      */             
/*  491 */             if (this.movingVehicle) {
/*      */               
/*  493 */               this.rightTurnMod++;
/*  494 */               int mod = 3;
/*  495 */               if (this.rightTurnMod > 20.0F)
/*  496 */                 mod = 2; 
/*  497 */               if (this.rightTurnMod > 40.0F)
/*  498 */                 mod = 1; 
/*  499 */               this.vehicleRotation = normalizeAngle(this.vehicleRotation + mod);
/*      */             }
/*  501 */             else if (speedModifier > 0.0F) {
/*      */               
/*  503 */               this.rightTurnMod++;
/*  504 */               int mod = 3;
/*  505 */               if (this.rightTurnMod > 20.0F)
/*  506 */                 mod = 2; 
/*  507 */               if (this.rightTurnMod > 40.0F)
/*  508 */                 mod = 1; 
/*  509 */               this.vehicleRotation = normalizeAngle(this.vehicleRotation + mod);
/*  510 */               xPosMod += (float)StrictMath.sin((this.vehicleRotation * 0.017453292F)) * this.mountSpeed * 0.3F;
/*      */               
/*  512 */               if (!this.serverWestAvailable && xPosMod < 0.0F) {
/*  513 */                 xPosMod = 0.0F;
/*  514 */               } else if (!this.serverEastAvailable && xPosMod > 0.0F) {
/*  515 */                 xPosMod = 0.0F;
/*  516 */               }  yPosMod -= (float)StrictMath.cos((this.vehicleRotation * 0.017453292F)) * this.mountSpeed * 0.3F;
/*      */               
/*  518 */               if (!this.serverNorthAvailable && yPosMod < 0.0F) {
/*  519 */                 yPosMod = 0.0F;
/*  520 */               } else if (!this.serverSouthAvailable && yPosMod > 0.0F) {
/*  521 */                 yPosMod = 0.0F;
/*      */               } 
/*  523 */               this.movingVehicle = true;
/*      */             } 
/*      */           } else {
/*      */             
/*  527 */             this.vehicleRotation = normalizeAngle(this.vehicleRotation + 1.0F);
/*      */           } 
/*      */         }
/*      */       } else {
/*      */         
/*  532 */         xPosMod += (float)StrictMath.cos((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*  533 */         yPosMod += (float)StrictMath.sin((rotation * 0.017453292F)) * 0.08F * speedModifier;
/*      */       } 
/*      */     } else {
/*      */       
/*  537 */       this.rightTurnMod = 0.0F;
/*      */     } 
/*  539 */     if (dirs > 0) {
/*      */       
/*  541 */       this.xa = (float)(this.xa + xPosMod / StrictMath.sqrt(dirs));
/*  542 */       this.ya = (float)(this.ya + yPosMod / StrictMath.sqrt(dirs));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  547 */     if (this.windImpact != 0.0F && speedModifier > 0.0F) {
/*      */       
/*  549 */       float strength = getWindPower(this.windRotation - 180.0F, this.vehicleRotation);
/*  550 */       float driftx = this.diffWindX * this.windImpact * 0.05F;
/*  551 */       float drifty = this.diffWindY * this.windImpact * 0.05F;
/*      */ 
/*      */       
/*  554 */       if (!this.serverWestAvailable && driftx < 0.0F)
/*  555 */         driftx = 0.0F; 
/*  556 */       if (!this.serverEastAvailable && driftx > 0.0F)
/*  557 */         driftx = 0.0F; 
/*  558 */       if (!this.serverSouthAvailable && drifty > 0.0F)
/*  559 */         drifty = 0.0F; 
/*  560 */       if (!this.serverNorthAvailable && drifty < 0.0F) {
/*  561 */         drifty = 0.0F;
/*      */       }
/*  563 */       this.xa += driftx;
/*  564 */       this.ya += drifty;
/*      */ 
/*      */       
/*  567 */       float windx = (float)StrictMath.sin((this.vehicleRotation * 0.017453292F)) * Math.abs(this.windStrength) * this.windImpact * strength;
/*      */       
/*  569 */       float windy = (float)StrictMath.cos((this.vehicleRotation * 0.017453292F)) * Math.abs(this.windStrength) * this.windImpact * strength;
/*      */       
/*  571 */       if (!this.serverWestAvailable && windx < 0.0F)
/*  572 */         windx = 0.0F; 
/*  573 */       if (!this.serverEastAvailable && windx > 0.0F)
/*  574 */         windx = 0.0F; 
/*  575 */       if (!this.serverSouthAvailable && windy > 0.0F)
/*  576 */         windy = 0.0F; 
/*  577 */       if (!this.serverNorthAvailable && windy < 0.0F)
/*  578 */         windy = 0.0F; 
/*  579 */       this.xa += windx;
/*  580 */       this.ya -= windy;
/*      */     } 
/*      */     
/*  583 */     float waterHeight = -1.45F;
/*      */     
/*  585 */     if (this.commandingBoat) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  590 */       float dHeight = getHeight(this.x + this.xa, this.y + this.ya, getHeight(this.x, this.y, -3000.0F));
/*  591 */       if (dHeight < maxDepth || dHeight > maxHeight) {
/*      */         
/*  593 */         this.xa = 0.0F;
/*  594 */         this.ya = 0.0F;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  600 */       if (this.layer == 0 && getTextureForTile(currentTileX, currentTileY, this.layer, this.bridgeId) == Tiles.Tile.TILE_HOLE.id)
/*      */       {
/*      */         
/*  603 */         this.layer = -1;
/*  604 */         setLayer(this.layer);
/*      */       }
/*      */     
/*  607 */     } else if (heightTarget < waterHeight) {
/*      */ 
/*      */       
/*  610 */       heightTarget = waterHeight;
/*  611 */       this.xa *= 0.6F;
/*  612 */       this.ya *= 0.6F;
/*  613 */       float dHeight = getHeight(this.x + this.xa, this.y + this.ya, getHeight(this.x, this.y, -3000.0F));
/*  614 */       if (dHeight < maxDepth || dHeight > maxHeight) {
/*      */         
/*  616 */         this.xa = 0.0F;
/*  617 */         this.ya = 0.0F;
/*      */       } 
/*      */       
/*  620 */       if (this.onGround)
/*      */       {
/*  622 */         if (this.layer == 0 && getTextureForTile(currentTileX, currentTileY, this.layer, this.bridgeId) == Tiles.Tile.TILE_HOLE.id)
/*      */         {
/*      */           
/*  625 */           this.layer = -1;
/*  626 */           setLayer(this.layer);
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  636 */     else if (this.onGround) {
/*      */ 
/*      */       
/*  639 */       if (this.layer == 0 && getTextureForTile(currentTileX, currentTileY, this.layer, this.bridgeId) == Tiles.Tile.TILE_HOLE.id) {
/*      */ 
/*      */         
/*  642 */         this.layer = -1;
/*  643 */         setLayer(this.layer);
/*      */       } 
/*      */       
/*  646 */       float tileSpeedMod = isOnFloor() ? 1.0F : getSpeedForTile(currentTileX, currentTileY, this.layer);
/*  647 */       this.xa *= tileSpeedMod;
/*  648 */       this.ya *= tileSpeedMod;
/*  649 */       float dHeight = getHeight(this.x + this.xa, this.y + this.ya, getHeight(this.x, this.y, -3000.0F));
/*  650 */       if (dHeight < maxDepth || dHeight > maxHeight)
/*      */       {
/*  652 */         this.xa = 0.0F;
/*  653 */         this.ya = 0.0F;
/*      */       }
/*      */       else
/*      */       {
/*  657 */         float hDiff = getHeight(this.x + this.xa, this.y + this.ya, heightTarget) - heightTarget;
/*  658 */         if (hDiff > 0.0F) {
/*      */           
/*  660 */           float f = (float)StrictMath.sqrt((this.xa * this.xa + this.ya * this.ya));
/*  661 */           this.xa /= hDiff * hDiff / f * 10.0F + 1.0F;
/*  662 */           this.ya /= hDiff * hDiff / f * 10.0F + 1.0F;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  668 */         int ntx = (int)StrictMath.floor(((this.x + this.xa) / 4.0F));
/*  669 */         int nty = (int)StrictMath.floor(((this.y + this.ya) / 4.0F));
/*  670 */         if (currentTileX != ntx || currentTileY != nty) {
/*      */           
/*  672 */           byte text = getTextureForTile(ntx, nty, this.layer, this.bridgeId);
/*  673 */           if (!Tiles.isSolidCave(text) && text != Tiles.Tile.TILE_HOLE.id)
/*      */           {
/*  675 */             if (getTileSteepness(ntx, nty, this.layer) > maxHeightDiff * 100.0F)
/*      */             {
/*  677 */               if (getHeightOfBridge(ntx, nty, this.layer) <= -1000.0F || (this.bridgeId <= 0L && hDiff > 0.0F)) {
/*      */                 
/*  679 */                 this.xa = 0.0F;
/*  680 */                 this.ya = 0.0F;
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  689 */         if (this.started && !this.climbing && !isCommanding)
/*      */         {
/*  691 */           float suggestedHeight = getHeight(this.x, this.y, -3000.0F);
/*  692 */           float xSlip = (getHeight(this.x - 0.25F, this.y, suggestedHeight) - getHeight(this.x + 0.25F, this.y, suggestedHeight)) / 0.5F;
/*      */           
/*  694 */           float ySlip = (getHeight(this.x, this.y - 0.25F, suggestedHeight) - getHeight(this.x, this.y + 0.25F, suggestedHeight)) / 0.5F;
/*      */ 
/*      */           
/*  697 */           float slipTreshold = 0.6F;
/*  698 */           float slipDampen = 0.3F;
/*      */           
/*  700 */           if (xSlip > 0.6F) {
/*  701 */             xSlip -= 0.3F;
/*  702 */           } else if (xSlip < -0.6F) {
/*  703 */             xSlip += 0.3F;
/*      */           } else {
/*  705 */             xSlip = 0.0F;
/*      */           } 
/*  707 */           if (ySlip > 0.6F) {
/*  708 */             ySlip -= 0.3F;
/*  709 */           } else if (ySlip < -0.6F) {
/*  710 */             ySlip += 0.3F;
/*      */           } else {
/*  712 */             ySlip = 0.0F;
/*      */           } 
/*  714 */           if (xSlip != 0.0F || ySlip != 0.0F)
/*      */           {
/*  716 */             float slipDist = xSlip * xSlip + ySlip * ySlip;
/*  717 */             float f1 = slipDist * 0.25F;
/*  718 */             if (f1 > 0.2F) {
/*  719 */               f1 = 0.2F;
/*      */             }
/*  721 */             slipDist = (float)Math.sqrt(slipDist);
/*      */             
/*  723 */             xSlip = xSlip * f1 / slipDist;
/*  724 */             ySlip = ySlip * f1 / slipDist;
/*      */             
/*  726 */             this.xa += xSlip;
/*  727 */             this.ya += ySlip;
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  735 */     else if (this.layer == 0 && getTextureForTile(currentTileX, currentTileY, this.layer, this.bridgeId) == Tiles.Tile.TILE_HOLE.id) {
/*      */ 
/*      */       
/*  738 */       this.layer = -1;
/*  739 */       setLayer(this.layer);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  744 */       if (isOnFloor() && this.bridgeId <= 0L) {
/*      */ 
/*      */         
/*  747 */         float tileSpeedMod = 1.0F;
/*  748 */         this.xa *= 1.0F;
/*  749 */         this.ya *= 1.0F;
/*      */       }
/*  751 */       else if (this.bridgeId > 0L) {
/*      */         
/*  753 */         float tileSpeedMod = getSpeedForTile(currentTileX, currentTileY, this.layer);
/*  754 */         this.xa *= tileSpeedMod;
/*  755 */         this.ya *= tileSpeedMod;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  762 */       boolean onBridge = false;
/*      */       
/*  764 */       if (isCommanding && !this.commandingBoat && this.bridgeId == -10L) {
/*      */         
/*  766 */         float hDiff = getHeight(this.x + this.xa, this.y + this.ya, heightTarget) - heightTarget;
/*  767 */         if (hDiff > 0.0F) {
/*      */           
/*  769 */           float f = (float)StrictMath.sqrt((this.xa * this.xa + this.ya * this.ya));
/*  770 */           this.xa /= hDiff * hDiff / f * 20.0F + 1.0F;
/*  771 */           this.ya /= hDiff * hDiff / f * 20.0F + 1.0F;
/*      */         } 
/*  773 */         int ntx = (int)StrictMath.floor(((this.x + this.xa) / 4.0F));
/*  774 */         int nty = (int)StrictMath.floor(((this.y + this.ya) / 4.0F));
/*  775 */         if (currentTileX != ntx || currentTileY != nty) {
/*      */           
/*  777 */           byte text = getTextureForTile(ntx, nty, this.layer, this.bridgeId);
/*  778 */           if (!Tiles.isSolidCave(text) && text != Tiles.Tile.TILE_HOLE.id)
/*      */           {
/*  780 */             if (getTileSteepness(ntx, nty, this.layer) > maxHeightDiff * 100.0F)
/*      */             {
/*  782 */               if (getHeightOfBridge(ntx, nty, this.layer) <= -1000.0F || (this.bridgeId <= 0L && hDiff > 0.0F))
/*      */               {
/*  784 */                 this.xa = 0.0F;
/*  785 */                 this.ya = 0.0F;
/*      */               }
/*      */               else
/*      */               {
/*  789 */                 onBridge = true;
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         } 
/*  795 */       } else if (this.bridgeId != -10L) {
/*      */         
/*  797 */         float hDiff = getHeight(this.x + this.xa, this.y + this.ya, heightTarget) - heightTarget;
/*  798 */         if (hDiff > 0.0F && hDiff < 1.0F) {
/*      */           
/*  800 */           float f = (float)StrictMath.sqrt((this.xa * this.xa + this.ya * this.ya));
/*  801 */           this.xa /= hDiff * hDiff / f * 10.0F + 1.0F;
/*  802 */           this.ya /= hDiff * hDiff / f * 10.0F + 1.0F;
/*      */         } 
/*  804 */         maybePrintDebugInfo(75);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  841 */       float dHeight = getHeight(this.x + this.xa, this.y + this.ya, getHeight(this.x, this.y, -3000.0F));
/*  842 */       if (!onBridge && (dHeight < maxDepth || dHeight > maxHeight)) {
/*      */         
/*  844 */         this.xa = 0.0F;
/*  845 */         this.ya = 0.0F;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  854 */     if (Math.abs(getTargetGroundOffset() - getGroundOffset()) > 3.0F) {
/*      */       
/*  856 */       this.xa = 0.0F;
/*  857 */       this.ya = 0.0F;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  863 */     float dist = this.xa * this.xa + this.ya * this.ya;
/*  864 */     float maxSpeed = 0.65000004F;
/*  865 */     if (dist > 0.42250004F) {
/*      */       
/*  867 */       dist = (float)Math.sqrt(dist);
/*  868 */       this.xa = this.xa / dist * 0.65000004F;
/*  869 */       this.ya = this.ya / dist * 0.65000004F;
/*  870 */       this.za = this.za / dist * 0.65000004F;
/*      */     } 
/*      */     
/*  873 */     this.xOld = this.x;
/*  874 */     this.yOld = this.y;
/*  875 */     this.zOld = this.z;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  881 */     int nextTileX = (int)((this.x + this.xa) / 4.0F);
/*  882 */     int nextTileY = (int)((this.y + this.ya) / 4.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  888 */     if (this.layer == -1 && Tiles.isSolidCave(getTextureForTile(currentTileX, currentTileY, this.layer, this.bridgeId))) {
/*      */       
/*  890 */       handlePlayerInRock();
/*      */     }
/*  892 */     else if (this.layer == -1 && getTextureForTile(currentTileX, currentTileY, this.layer, this.bridgeId) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */       
/*  894 */       if (Tiles.isSolidCave(getTextureForTile(nextTileX, nextTileY, this.layer, this.bridgeId))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  900 */         this.layer = 0;
/*  901 */         float dHeight = getHeight(this.x + this.xa, this.y + this.ya, getHeight(this.x, this.y, -3000.0F));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  907 */         if (dHeight > maxHeight)
/*      */         {
/*  909 */           this.layer = -1;
/*  910 */           this.xa = 0.0F;
/*  911 */           this.ya = 0.0F;
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*  916 */           setLayer(this.layer);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  921 */         int diffx = nextTileX - currentTileX;
/*  922 */         int diffy = nextTileY - currentTileY;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  928 */         if (diffx != 0 && diffy != 0) {
/*      */           
/*  930 */           if (diffx < 0 && diffy < 0) {
/*      */             
/*  932 */             byte text = getTextureForTile(currentTileX - 1, currentTileY, -1, this.bridgeId);
/*  933 */             byte text2 = getTextureForTile(currentTileX, currentTileY - 1, -1, this.bridgeId);
/*  934 */             if (Tiles.isSolidCave(text) && Tiles.isSolidCave(text2)) {
/*      */               
/*  936 */               this.xa = 0.0F;
/*  937 */               this.ya = 0.0F;
/*      */             } 
/*      */           } 
/*  940 */           if (diffx > 0 && diffy < 0) {
/*      */             
/*  942 */             byte text = getTextureForTile(currentTileX + 1, currentTileY, -1, this.bridgeId);
/*  943 */             byte text2 = getTextureForTile(currentTileX, currentTileY - 1, -1, this.bridgeId);
/*  944 */             if (Tiles.isSolidCave(text) && Tiles.isSolidCave(text2)) {
/*      */               
/*  946 */               this.xa = 0.0F;
/*  947 */               this.ya = 0.0F;
/*      */             } 
/*      */           } 
/*  950 */           if (diffx > 0 && diffy > 0) {
/*      */             
/*  952 */             byte text = getTextureForTile(currentTileX + 1, currentTileY, -1, this.bridgeId);
/*  953 */             byte text2 = getTextureForTile(currentTileX, currentTileY + 1, -1, this.bridgeId);
/*  954 */             if (Tiles.isSolidCave(text) && Tiles.isSolidCave(text2)) {
/*      */               
/*  956 */               this.xa = 0.0F;
/*  957 */               this.ya = 0.0F;
/*      */             } 
/*      */           } 
/*  960 */           if (diffx < 0 && diffy > 0) {
/*      */             
/*  962 */             byte text = getTextureForTile(currentTileX - 1, currentTileY, -1, this.bridgeId);
/*  963 */             byte text2 = getTextureForTile(currentTileX, currentTileY + 1, -1, this.bridgeId);
/*  964 */             if (Tiles.isSolidCave(text) && Tiles.isSolidCave(text2)) {
/*      */               
/*  966 */               this.xa = 0.0F;
/*  967 */               this.ya = 0.0F;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  977 */     this.x += this.xa;
/*  978 */     this.y += this.ya;
/*  979 */     this.z += this.za;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  984 */     updateGroundOffset();
/*      */ 
/*      */     
/*  987 */     float nextHeightTarget = Math.max(getHeight(this.x, this.y, -3000.0F), waterHeight);
/*      */     
/*  989 */     this.onGround = (this.z <= nextHeightTarget && !isOnFloor());
/*      */     
/*  991 */     if ((isCommanding || this.offZ != 0.0F) && (!isOnFloor() || !this.commandingBoat)) {
/*      */ 
/*      */       
/*  994 */       this.onGround = false;
/*  995 */       this.inWater = false;
/*      */ 
/*      */       
/*  998 */       if (!this.commandingBoat)
/*      */       {
/* 1000 */         if (isOnFloor() && (this.z - nextHeightTarget) > 2.9D + (this.groundOffset / 10.0F))
/*      */         {
/* 1002 */           this.za = 0.0F;
/*      */ 
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*      */           
/* 1009 */           this.z = nextHeightTarget + (isOnFloor() ? 0.25F : 0.0F);
/* 1010 */           this.za = 0.0F;
/*      */         }
/*      */       
/*      */       }
/* 1014 */     } else if (this.onGround) {
/*      */       
/* 1016 */       boolean landed = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1023 */       if (this.za < -0.25D && !this.inWater && this.bridgeId <= 0L) {
/*      */         
/* 1025 */         hitGround(-this.za);
/* 1026 */         landed = true;
/*      */       } 
/*      */ 
/*      */       
/* 1030 */       if (landed && nextHeightTarget > heightTarget)
/*      */       {
/*      */         
/* 1033 */         float dzPlayer = this.z - this.zOld;
/* 1034 */         float dzTerrain = nextHeightTarget - heightTarget;
/* 1035 */         float intersection = (this.zOld - heightTarget) / (dzTerrain - dzPlayer);
/*      */ 
/*      */         
/* 1038 */         this.xa = 0.0F;
/* 1039 */         this.ya = 0.0F;
/* 1040 */         this.za = 0.0F;
/*      */ 
/*      */         
/* 1043 */         this.x = this.xOld + intersection * (this.x - this.xOld);
/* 1044 */         this.y = this.yOld + intersection * (this.y - this.yOld);
/* 1045 */         this.z = this.zOld + intersection * dzPlayer;
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 1050 */         this.z = nextHeightTarget;
/* 1051 */         this.za = 0.0F;
/*      */       }
/*      */     
/* 1054 */     } else if (isOnFloor()) {
/*      */ 
/*      */ 
/*      */       
/* 1058 */       if (this.bridgeId <= 0L || this.z < nextHeightTarget)
/*      */       {
/*      */ 
/*      */         
/* 1062 */         if (isAdjustingGroundOffset() || (this.xa == 0.0F && this.ya == 0.0F)) {
/* 1063 */           this.z = nextHeightTarget;
/*      */         } else {
/* 1065 */           this.z = this.zOld;
/*      */         }  } 
/* 1067 */       if (this.za < -0.25D && !this.inWater && isOnFloor() && !isCommanding)
/* 1068 */         hitGround(-this.za); 
/* 1069 */       this.za = 0.0F;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1080 */     if (this.onGround || this.inWater || isCommanding || isOnFloor()) {
/*      */       
/* 1082 */       this.xa *= getMoveMod();
/* 1083 */       this.ya *= getMoveMod();
/*      */     } 
/* 1085 */     if ((isCommanding || this.offZ != 0.0F || this.flying) && !isFalling()) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1090 */       this.za = 0.0F;
/*      */     }
/* 1092 */     else if (this.started) {
/*      */       
/* 1094 */       this.za -= getFallMod();
/*      */     } 
/* 1096 */     if (this.wasOnStair)
/* 1097 */       this.wasOnStair = false; 
/* 1098 */     if (isCommanding) {
/* 1099 */       maybePrintDebugInfo(100);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getWaterLevel(float x, float y) {
/* 1109 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   private final strictfp void maybePrintDebugInfo(int step) {
/* 1114 */     maybePrintDebugInfo(step, 0.0F, 0.0F, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final strictfp void maybePrintDebugInfo(int step, float val1, float val2, float val3) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp void setOnFloorOverride(boolean onFloor) {
/* 1144 */     if (onFloor != this.onFloorOverridden)
/*      */     {
/* 1146 */       this.counter = 0;
/*      */     }
/* 1148 */     if (onFloor) {
/*      */       
/* 1150 */       this.onFloorOverridden = true;
/*      */     }
/*      */     else {
/*      */       
/* 1154 */       this.onFloorOverridden = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp boolean getOnFloorOverride() {
/* 1160 */     return this.onFloorOverridden;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp boolean isOnFloor() {
/* 1171 */     return ((getGroundOffset() > 0.0F && !isAdjustingGroundOffset()) || this.bridgeId > 0L || this.onFloorOverridden == true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp float getFallMod() {
/* 1177 */     return 0.04F;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp float getMoveMod() {
/* 1182 */     return 0.4F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp boolean movedOnStair() {
/* 1192 */     return this.wasOnStair;
/*      */   }
/*      */ 
/*      */   
/*      */   private final strictfp boolean isCommanding(float maxDepth, float maxHeight) {
/* 1197 */     return (maxDepth > -2500.0F || maxHeight < 2500.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract void hitGround(float paramFloat);
/*      */   
/*      */   public abstract float getTileSteepness(int paramInt1, int paramInt2, int paramInt3);
/*      */   
/*      */   private strictfp float getHeight(float xp, float yp, float suggestedHeight) {
/* 1206 */     int xx = (int)StrictMath.floor((xp / 4.0F));
/* 1207 */     int yy = (int)StrictMath.floor((yp / 4.0F));
/*      */     
/* 1209 */     if (this.layer == 0 && getTextureForTile(xx, yy, this.layer, this.bridgeId) == Tiles.Tile.TILE_HOLE.id)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1215 */       return getHeight(xp, yp, suggestedHeight, -1);
/*      */     }
/* 1217 */     if (this.layer == -1 && Tiles.isSolidCave(getTextureForTile(xx, yy, this.layer, this.bridgeId)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1223 */       return suggestedHeight;
/*      */     }
/* 1225 */     return getHeight(xp, yp, suggestedHeight, this.layer);
/*      */   }
/*      */ 
/*      */   
/*      */   private final strictfp float getHeight(float xp, float yp, float suggestedHeight, int layer) {
/* 1230 */     int xx = (int)StrictMath.floor((xp / 4.0F));
/* 1231 */     int yy = (int)StrictMath.floor((yp / 4.0F));
/*      */     
/* 1233 */     float xa = xp / 4.0F - xx;
/* 1234 */     float ya = yp / 4.0F - yy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1246 */     if (layer == -1 && suggestedHeight > -2999.0F) {
/*      */       
/* 1248 */       byte id = getTextureForTile(xx, yy, layer, this.bridgeId);
/* 1249 */       if (id == Tiles.Tile.TILE_CAVE_WALL.id || id == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id)
/*      */       {
/* 1251 */         return suggestedHeight;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1256 */     float[] hts = getNodeHeights(xx, yy, layer, this.bridgeId);
/*      */     
/* 1258 */     float height = hts[0] * (1.0F - xa) * (1.0F - ya) + hts[1] * xa * (1.0F - ya) + hts[2] * (1.0F - xa) * ya + hts[3] * xa * ya;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1269 */     return height + getCurrentGroundOffset() / 10.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp void setSpeedModifier(float speedModifier) {
/* 1279 */     this.speedMod = speedModifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp void setPosition(float x, float y, float z, float xRot, int _layer) {
/* 1286 */     this.abort = true;
/* 1287 */     this.onGround = false;
/* 1288 */     this.inWater = false;
/* 1289 */     this.x = x;
/* 1290 */     this.y = y;
/* 1291 */     this.z = z;
/* 1292 */     this.xRot = xRot;
/* 1293 */     this.layer = _layer;
/* 1294 */     this.xa = 0.0F;
/* 1295 */     this.ya = 0.0F;
/* 1296 */     this.za = 0.0F;
/* 1297 */     if (this.layer != _layer) {
/* 1298 */       setLayer(_layer);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp void changeLayer(int _layer) {
/* 1305 */     this.layer = _layer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private strictfp float getSpeedForTile(int xTile, int yTile, int layer) {
/*      */     try {
/* 1312 */       return Tiles.getTile(getTextureForTile(xTile, yTile, layer, this.bridgeId)).getSpeed();
/*      */     }
/* 1314 */     catch (NullPointerException e) {
/*      */       
/* 1316 */       System.out.println("Can't get speed for tile " + xTile + ", " + yTile + ", layer " + layer + ", since it's of id " + 
/* 1317 */           getTextureForTile(xTile, yTile, layer, this.bridgeId));
/* 1318 */       return 0.1F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final strictfp byte buildBitmap(boolean f, boolean b, boolean l, boolean r) {
/* 1324 */     byte result = 0;
/*      */     
/* 1326 */     if (f)
/* 1327 */       result = (byte)(result | 0x1); 
/* 1328 */     if (b)
/* 1329 */       result = (byte)(result | 0x2); 
/* 1330 */     if (l)
/* 1331 */       result = (byte)(result | 0x4); 
/* 1332 */     if (r) {
/* 1333 */       result = (byte)(result | 0x8);
/*      */     }
/* 1335 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp float getX() {
/* 1341 */     return this.x;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp float getY() {
/* 1347 */     return this.y;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp float getZ() {
/* 1353 */     return this.z;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp float getRot() {
/* 1359 */     return this.xRot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isServerWestAvailable() {
/* 1369 */     return this.serverWestAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setServerWestAvailable(boolean serverWestAvailable) {
/* 1378 */     this.serverWestAvailable = serverWestAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isServerNorthAvailable() {
/* 1388 */     return this.serverNorthAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setServerNorthAvailable(boolean serverNorthAvailable) {
/* 1397 */     this.serverNorthAvailable = serverNorthAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isServerEastAvailable() {
/* 1407 */     return this.serverEastAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setServerEastAvailable(boolean serverEastAvailable) {
/* 1416 */     this.serverEastAvailable = serverEastAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isServerSouthAvailable() {
/* 1426 */     return this.serverSouthAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setServerSouthAvailable(boolean serverSouthAvailable) {
/* 1435 */     this.serverSouthAvailable = serverSouthAvailable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getXa() {
/* 1445 */     return this.xa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setXa(float xa) {
/* 1454 */     this.xa = xa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getYa() {
/* 1464 */     return this.ya;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setYa(float ya) {
/* 1473 */     this.ya = ya;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getZa() {
/* 1483 */     return this.za;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setZa(float za) {
/* 1492 */     this.za = za;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isOnGround() {
/* 1502 */     return this.onGround;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setOnGround(boolean onGround) {
/* 1511 */     this.onGround = onGround;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp boolean isInWater() {
/* 1521 */     return this.inWater;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setInWater(boolean inWater) {
/* 1530 */     this.inWater = inWater;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isIgnoreErrors() {
/* 1540 */     return this.ignoreErrors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setIgnoreErrors(boolean ignoreErrors) {
/* 1549 */     this.ignoreErrors = ignoreErrors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isStarted() {
/* 1559 */     return this.started;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setStarted(boolean started) {
/* 1568 */     this.started = started;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getDiffWindX() {
/* 1578 */     return this.diffWindX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setDiffWindX(float diffWindX) {
/* 1587 */     this.diffWindX = diffWindX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getDiffWindY() {
/* 1597 */     return this.diffWindY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setDiffWindY(float diffWindY) {
/* 1606 */     this.diffWindY = diffWindY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isCommandingBoat() {
/* 1616 */     return this.commandingBoat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setCommandingBoat(boolean commandingBoat) {
/* 1625 */     this.commandingBoat = commandingBoat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getCurrx() {
/* 1635 */     return this.currx;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getCurry() {
/* 1645 */     return this.curry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isFirst() {
/* 1655 */     return this.first;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setFirst(boolean first) {
/* 1664 */     this.first = first;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isMovingVehicle() {
/* 1674 */     return this.movingVehicle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp float getOffZ() {
/* 1684 */     return this.offZ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setOffZ(float offZ) {
/* 1693 */     this.offZ = offZ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp boolean isClimbing() {
/* 1703 */     return this.climbing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setX(float x) {
/* 1712 */     this.x = x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setY(float y) {
/* 1721 */     this.y = y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setZ(float z) {
/* 1730 */     this.z = z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract float getHeightOfBridge(int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract byte getTextureForTile(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract float getCeilingForNode(int paramInt1, int paramInt2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract float getHeightForNode(int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract float[] getNodeHeights(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract boolean handleWrongLayer(int paramInt1, int paramInt2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract boolean handleMoveTooFar(float paramFloat1, float paramFloat2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract boolean handleMoveTooShort(float paramFloat1, float paramFloat2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract boolean handleZError(float paramFloat1, float paramFloat2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void handlePlayerInRock();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setLayer(int layer) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp void fly(float xTarget, float yTarget, float zTarget, float xRot, float yRot, byte bitmask, int layerTarget) {
/* 1837 */     this.x = xTarget;
/* 1838 */     this.y = yTarget;
/* 1839 */     this.z = zTarget;
/* 1840 */     this.layer = layerTarget;
/* 1841 */     this.onGround = false;
/* 1842 */     this.xRot = xRot;
/* 1843 */     this.bitmask = bitmask;
/*      */     
/* 1845 */     float speedModifier = 1.0F;
/*      */     
/* 1847 */     int dirs = 0;
/* 1848 */     float xPosMod = 0.0F;
/* 1849 */     float yPosMod = 0.0F;
/* 1850 */     float zPosMod = 0.0F;
/*      */     
/* 1852 */     if (isPressed(1)) {
/*      */       
/* 1854 */       dirs++;
/* 1855 */       xPosMod += (float)StrictMath.sin((xRot * 0.017453292F)) * 0.08F * 1.0F * 
/* 1856 */         (float)StrictMath.cos((yRot * 0.017453292F));
/* 1857 */       yPosMod -= (float)StrictMath.cos((xRot * 0.017453292F)) * 0.08F * 1.0F * 
/* 1858 */         (float)StrictMath.cos((yRot * 0.017453292F));
/* 1859 */       zPosMod -= (float)StrictMath.sin((yRot * 0.017453292F)) * 0.08F * 1.0F;
/*      */     } 
/* 1861 */     if (isPressed(2)) {
/*      */       
/* 1863 */       dirs++;
/* 1864 */       xPosMod -= (float)StrictMath.sin((xRot * 0.017453292F)) * 0.08F * 1.0F * 
/* 1865 */         (float)StrictMath.cos((yRot * 0.017453292F));
/* 1866 */       yPosMod += (float)StrictMath.cos((xRot * 0.017453292F)) * 0.08F * 1.0F * 
/* 1867 */         (float)StrictMath.cos((yRot * 0.017453292F));
/*      */ 
/*      */ 
/*      */       
/* 1871 */       zPosMod += (float)StrictMath.sin((yRot * 0.017453292F)) * 0.08F * 1.0F;
/*      */     } 
/* 1873 */     if (isPressed(4)) {
/*      */       
/* 1875 */       dirs++;
/* 1876 */       xPosMod -= (float)StrictMath.cos((xRot * 0.017453292F)) * 0.08F * 1.0F;
/* 1877 */       yPosMod -= (float)StrictMath.sin((xRot * 0.017453292F)) * 0.08F * 1.0F;
/*      */     } 
/* 1879 */     if (isPressed(8)) {
/*      */       
/* 1881 */       dirs++;
/* 1882 */       xPosMod += (float)StrictMath.cos((xRot * 0.017453292F)) * 0.08F * 1.0F;
/* 1883 */       yPosMod += (float)StrictMath.sin((xRot * 0.017453292F)) * 0.08F * 1.0F;
/*      */     } 
/*      */     
/* 1886 */     if (dirs > 0) {
/*      */       
/* 1888 */       this.xa = (float)(this.xa + xPosMod / StrictMath.sqrt(dirs));
/* 1889 */       this.ya = (float)(this.ya + yPosMod / StrictMath.sqrt(dirs));
/* 1890 */       this.za = (float)(this.za + zPosMod / StrictMath.sqrt(dirs));
/*      */     } 
/*      */     
/* 1893 */     float height = getHeight(this.x, this.y, -3000.0F);
/*      */     
/* 1895 */     if (height < -1.45D)
/*      */     {
/* 1897 */       height = -1.45F;
/*      */     }
/*      */     
/* 1900 */     float dist = this.xa * this.xa + this.ya * this.ya;
/* 1901 */     float maxSpeed = 0.65000004F;
/* 1902 */     if (dist > 0.42250004F) {
/*      */       
/* 1904 */       dist = (float)Math.sqrt(dist);
/* 1905 */       this.xa = this.xa / dist * 0.65000004F;
/* 1906 */       this.ya = this.ya / dist * 0.65000004F;
/* 1907 */       this.za = this.za / dist * 0.65000004F;
/*      */     } 
/*      */     
/* 1910 */     this.xOld = this.x;
/* 1911 */     this.yOld = this.y;
/* 1912 */     this.zOld = this.z;
/*      */     
/* 1914 */     int xx = (int)(this.x / 4.0F);
/* 1915 */     int yy = (int)(this.y / 4.0F);
/*      */     
/* 1917 */     this.x += this.xa;
/* 1918 */     this.y += this.ya;
/* 1919 */     this.z += this.za;
/*      */     
/* 1921 */     int newxx = (int)(this.x / 4.0F);
/* 1922 */     int newyy = (int)(this.y / 4.0F);
/*      */     
/* 1924 */     if (this.layer == -1 && 
/* 1925 */       getTextureForTile(xx, yy, this.layer, this.bridgeId) != Tiles.Tile.TILE_CAVE.id && 
/* 1926 */       !Tiles.isReinforcedFloor(getTextureForTile(xx, yy, this.layer, this.bridgeId)) && 
/* 1927 */       getTextureForTile(xx, yy, this.layer, this.bridgeId) != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */       
/* 1929 */       handlePlayerInRock();
/*      */ 
/*      */     
/*      */     }
/* 1933 */     else if (this.layer == -1 && getTextureForTile(xx, yy, this.layer, this.bridgeId) == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */       
/* 1935 */       if (getTextureForTile(newxx, newyy, this.layer, this.bridgeId) == Tiles.Tile.TILE_CAVE_WALL.id || 
/* 1936 */         getTextureForTile(newxx, newyy, this.layer, this.bridgeId) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) {
/*      */         
/* 1938 */         this.layer = 0;
/* 1939 */         setLayer(this.layer);
/*      */       }
/*      */       else {
/*      */         
/* 1943 */         if (newyy == yy) {
/*      */           
/* 1945 */           int xa = (newxx < xx) ? 0 : 1;
/* 1946 */           if (getCeilingForNode(xx + xa, yy) < 0.5F && getCeilingForNode(xx + xa, yy + 1) < 0.5F) {
/*      */             
/* 1948 */             this.layer = 0;
/* 1949 */             setLayer(this.layer);
/*      */           } 
/*      */         } 
/* 1952 */         if (newxx == xx) {
/*      */           
/* 1954 */           int ya = (newyy < yy) ? 0 : 1;
/* 1955 */           if (getCeilingForNode(xx, yy + ya) < 0.5F && getCeilingForNode(xx + 1, yy + ya) < 0.5F) {
/*      */             
/* 1957 */             this.layer = 0;
/* 1958 */             setLayer(this.layer);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1964 */     if (this.z < height) {
/*      */       
/* 1966 */       this.z = height;
/* 1967 */       this.za = 0.0F;
/*      */     } 
/*      */     
/* 1970 */     this.xa *= 0.9F;
/* 1971 */     this.ya *= 0.9F;
/* 1972 */     this.za *= 0.9F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final strictfp void setClimbing(boolean climbing) {
/* 1978 */     this.climbing = climbing;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp int getLayer() {
/* 1983 */     return this.layer;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void setMountSpeed(float newMountSpeed) {
/* 1988 */     this.mountSpeed = newMountSpeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp float getWindImpact() {
/* 1993 */     return this.windImpact;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void setWindImpact(float wrot) {
/* 1998 */     this.windImpact = wrot;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final strictfp float normalizeAngle(float angle) {
/* 2003 */     angle -= ((int)(angle / 360.0F) * 360);
/* 2004 */     if (angle < 0.0F)
/* 2005 */       angle += 360.0F; 
/* 2006 */     return angle;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void reset() {
/* 2011 */     setMountSpeed(0.0F);
/* 2012 */     setWindImpact(0.0F);
/* 2013 */     setWindRotation(0.0F);
/* 2014 */     setWindStrength(0.0F);
/* 2015 */     this.diffWindX = 0.0F;
/* 2016 */     this.diffWindY = 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final strictfp float getWindPower(float aWindRotation, float aVehicleRotation) {
/* 2021 */     float lVehicleRotation, lWindRotation = normalizeAngle(aWindRotation);
/*      */     
/* 2023 */     if (lWindRotation > aVehicleRotation) {
/* 2024 */       lVehicleRotation = normalizeAngle(lWindRotation - aVehicleRotation);
/*      */     } else {
/* 2026 */       lVehicleRotation = normalizeAngle(aVehicleRotation - lWindRotation);
/*      */     } 
/* 2028 */     if (lVehicleRotation > 150.0F && lVehicleRotation < 210.0F)
/*      */     {
/* 2030 */       return 0.0F;
/*      */     }
/* 2032 */     if (lVehicleRotation > 120.0F && lVehicleRotation < 240.0F)
/*      */     {
/* 2034 */       return 0.5F;
/*      */     }
/* 2036 */     if (lVehicleRotation > 90.0F && lVehicleRotation < 270.0F)
/*      */     {
/* 2038 */       return 0.65F;
/*      */     }
/* 2040 */     if (lVehicleRotation > 60.0F && lVehicleRotation < 300.0F)
/*      */     {
/* 2042 */       return 0.8F;
/*      */     }
/* 2044 */     if (lVehicleRotation > 30.0F && lVehicleRotation < 330.0F)
/*      */     {
/* 2046 */       return 1.0F;
/*      */     }
/*      */     
/* 2049 */     return 0.9F;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp float getSpeedMod() {
/* 2054 */     return this.speedMod;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp float getMountSpeed() {
/* 2059 */     return this.mountSpeed;
/*      */   }
/*      */ 
/*      */   
/*      */   protected strictfp float getXold() {
/* 2064 */     return this.xOld;
/*      */   }
/*      */ 
/*      */   
/*      */   protected strictfp float getYold() {
/* 2069 */     return this.yOld;
/*      */   }
/*      */ 
/*      */   
/*      */   protected strictfp float getZold() {
/* 2074 */     return this.zOld;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected strictfp void setLog(boolean log) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp byte getBitMask() {
/* 2087 */     return this.bitmask;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp float getVehicleRotation() {
/* 2092 */     return this.vehicleRotation;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void setVehicleRotation(float rotation) {
/* 2097 */     this.vehicleRotation = rotation;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp float getWindStrength() {
/* 2102 */     return this.windStrength;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void setWindStrength(float wstr) {
/* 2107 */     this.windStrength = wstr;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp float getWindRotation() {
/* 2112 */     return this.windRotation;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void setWindRotation(float wrot) {
/* 2117 */     this.windRotation = wrot;
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp void setGroundOffset(int newOffset, boolean immediately) {
/* 2122 */     setTargetGroundOffset(Math.min(getMaxTargetGroundOffset(newOffset), newOffset));
/*      */     
/* 2124 */     if (immediately) {
/* 2125 */       setGroundOffset(getTargetGroundOffset());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp int getMaxTargetGroundOffset(int suggestedOffset) {
/* 2135 */     return suggestedOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp float getTargetGroundOffset() {
/* 2140 */     return this.targetGroundOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp void setTargetGroundOffset(int newOffset) {
/* 2145 */     this.targetGroundOffset = newOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp float getGroundOffset() {
/* 2150 */     return this.groundOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp void setGroundOffset(float newOffset) {
/* 2155 */     this.groundOffset = newOffset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final strictfp float getCurrentGroundOffset() {
/* 2167 */     return getGroundOffset();
/*      */   }
/*      */ 
/*      */   
/*      */   private final strictfp void updateGroundOffset() {
/* 2172 */     if (getTargetGroundOffset() > getGroundOffset() + 1.0F) {
/*      */       
/* 2174 */       setGroundOffset(getGroundOffset() + 1.0F);
/*      */     }
/* 2176 */     else if (getTargetGroundOffset() < getGroundOffset() - 1.0F) {
/*      */       
/* 2178 */       setGroundOffset(getGroundOffset() - 1.0F);
/*      */     }
/*      */     else {
/*      */       
/* 2182 */       setGroundOffset(getTargetGroundOffset());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final strictfp boolean isAdjustingGroundOffset() {
/* 2188 */     return (getGroundOffset() != getTargetGroundOffset());
/*      */   }
/*      */ 
/*      */   
/*      */   public strictfp String getInfo() {
/* 2193 */     return "commanding boat: " + this.commandingBoat + "in water=" + this.inWater + " onground=" + this.onGround + " speedmod=" + this.speedMod + ",mountspeed=" + this.mountSpeed + " vehic rot " + this.vehicleRotation + " windrot=" + this.windRotation + " wind str=" + this.windStrength + " windImpact=" + this.windImpact;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp long getBridgeId() {
/* 2203 */     return this.bridgeId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp void setBridgeCounter(int nums) {
/* 2212 */     this.bridgeCounter = nums;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public strictfp void setBridgeId(long bridgeId) {
/* 2221 */     this.bridgeId = bridgeId;
/* 2222 */     this.bridgeCounter = 10;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\MovementChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */