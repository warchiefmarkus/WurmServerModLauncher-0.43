/*     */ package com.wurmonline.server.bodys;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
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
/*     */ final class BodyDog
/*     */   extends BodyTemplate
/*     */ {
/*     */   BodyDog() {
/*  35 */     super((byte)3);
/*  36 */     this.leftOverArmS = "thigh of the left foreleg";
/*  37 */     this.rightOverArmS = "thigh of the right foreleg";
/*  38 */     this.leftThighS = "thigh of the left hindleg";
/*  39 */     this.rightThighS = "thigh of the right hindleg";
/*  40 */     this.leftUnderArmS = "calf of the left foreleg";
/*  41 */     this.rightUnderArmS = "calf of the right foreleg";
/*  42 */     this.leftCalfS = "calf of the left hindleg";
/*  43 */     this.rightCalfS = "calf of the right hindleg";
/*  44 */     this.leftHandS = "left paw";
/*  45 */     this.rightHandS = "right paw";
/*  46 */     this.leftFootS = "left paw";
/*  47 */     this.rightFootS = "right paw";
/*  48 */     this.leftArmS = "left foreleg";
/*  49 */     this.rightArmS = "right foreleg";
/*  50 */     this.leftLegS = "left hindleg";
/*  51 */     this.rightLegS = "right hindleg";
/*  52 */     this.typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getRandomWoundPos() throws Exception {
/*  62 */     int rand = Server.rand.nextInt(1000);
/*  63 */     if (rand < 3)
/*  64 */       return 1; 
/*  65 */     if (rand < 80)
/*  66 */       return 5; 
/*  67 */     if (rand < 130)
/*  68 */       return 6; 
/*  69 */     if (rand < 180)
/*  70 */       return 7; 
/*  71 */     if (rand < 230)
/*  72 */       return 8; 
/*  73 */     if (rand < 280)
/*  74 */       return 9; 
/*  75 */     if (rand < 320)
/*  76 */       return 10; 
/*  77 */     if (rand < 370)
/*  78 */       return 11; 
/*  79 */     if (rand < 420)
/*  80 */       return 12; 
/*  81 */     if (rand < 460)
/*  82 */       return 13; 
/*  83 */     if (rand < 500)
/*  84 */       return 14; 
/*  85 */     if (rand < 540)
/*  86 */       return 15; 
/*  87 */     if (rand < 580)
/*  88 */       return 16; 
/*  89 */     if (rand < 600)
/*  90 */       return 17; 
/*  91 */     if (rand < 601)
/*  92 */       return 18; 
/*  93 */     if (rand < 602)
/*  94 */       return 19; 
/*  95 */     if (rand < 730)
/*  96 */       return 21; 
/*  97 */     if (rand < 780)
/*  98 */       return 22; 
/*  99 */     if (rand < 830)
/* 100 */       return 23; 
/* 101 */     if (rand < 890)
/* 102 */       return 24; 
/* 103 */     if (rand < 900)
/* 104 */       return 25; 
/* 105 */     if (rand < 950)
/* 106 */       return 26; 
/* 107 */     if (rand < 1000) {
/* 108 */       return 27;
/*     */     }
/* 110 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void buildBody(Item[] spaces, Creature owner) {
/* 116 */     spaces[0].setOwner(owner.getWurmId(), true);
/* 117 */     spaces[0].insertItem(spaces[1]);
/* 118 */     spaces[1].insertItem(spaces[29]);
/* 119 */     spaces[0].insertItem(spaces[2]);
/* 120 */     spaces[2].insertItem(spaces[34]);
/* 121 */     spaces[34].insertItem(spaces[15]);
/* 122 */     spaces[34].insertItem(spaces[16]);
/* 123 */     spaces[34].insertItem(spaces[3]);
/* 124 */     spaces[34].insertItem(spaces[4]);
/* 125 */     spaces[3].insertItem(spaces[13]);
/* 126 */     spaces[4].insertItem(spaces[14]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyDog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */