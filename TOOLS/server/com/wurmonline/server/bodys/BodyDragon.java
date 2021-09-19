/*     */ package com.wurmonline.server.bodys;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class BodyDragon
/*     */   extends BodyTemplate
/*     */ {
/*     */   BodyDragon() {
/*  36 */     super((byte)6);
/*  37 */     this.leftHandS = "left foreclaw";
/*  38 */     this.rightHandS = "right foreclaw";
/*  39 */     this.leftFootS = "left hindclaw";
/*  40 */     this.rightFootS = "right hindclaw";
/*  41 */     this.lowerBackS = "tail";
/*  42 */     this.leftOverArmS = "left wing";
/*  43 */     this.rightOverArmS = "right wing";
/*  44 */     this.typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getRandomWoundPos() throws Exception {
/*  54 */     int rand = Server.rand.nextInt(1000);
/*  55 */     if (rand < 30)
/*  56 */       return 1; 
/*  57 */     if (rand < 80)
/*  58 */       return 5; 
/*  59 */     if (rand < 130)
/*  60 */       return 6; 
/*  61 */     if (rand < 180)
/*  62 */       return 7; 
/*  63 */     if (rand < 230)
/*  64 */       return 8; 
/*  65 */     if (rand < 280)
/*  66 */       return 9; 
/*  67 */     if (rand < 320)
/*  68 */       return 10; 
/*  69 */     if (rand < 370)
/*  70 */       return 11; 
/*  71 */     if (rand < 420)
/*  72 */       return 12; 
/*  73 */     if (rand < 460)
/*  74 */       return 13; 
/*  75 */     if (rand < 500)
/*  76 */       return 14; 
/*  77 */     if (rand < 540)
/*  78 */       return 15; 
/*  79 */     if (rand < 580)
/*  80 */       return 16; 
/*  81 */     if (rand < 600)
/*  82 */       return 17; 
/*  83 */     if (rand < 601)
/*  84 */       return 18; 
/*  85 */     if (rand < 602)
/*  86 */       return 19; 
/*  87 */     if (rand < 730)
/*  88 */       return 21; 
/*  89 */     if (rand < 780)
/*  90 */       return 22; 
/*  91 */     if (rand < 830)
/*  92 */       return 23; 
/*  93 */     if (rand < 890)
/*  94 */       return 24; 
/*  95 */     if (rand < 900)
/*  96 */       return 25; 
/*  97 */     if (rand < 950)
/*  98 */       return 26; 
/*  99 */     if (rand < 1000) {
/* 100 */       return 27;
/*     */     }
/* 102 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */