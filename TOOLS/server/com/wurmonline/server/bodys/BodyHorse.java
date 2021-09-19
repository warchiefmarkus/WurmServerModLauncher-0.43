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
/*     */ final class BodyHorse
/*     */   extends BodyTemplate
/*     */ {
/*     */   BodyHorse() {
/*  35 */     super((byte)1);
/*  36 */     this.leftOverArmS = "thigh of the left foreleg";
/*  37 */     this.rightOverArmS = "thigh of the right foreleg";
/*  38 */     this.leftThighS = "thigh of the left hindleg";
/*  39 */     this.rightThighS = "thigh of the right hindleg";
/*  40 */     this.leftUnderArmS = "calf of the left foreleg";
/*  41 */     this.rightUnderArmS = "calf of the right foreleg";
/*  42 */     this.leftCalfS = "calf of the left hindleg";
/*  43 */     this.rightCalfS = "calf of the right hindleg";
/*  44 */     this.leftHandS = "left hoof";
/*  45 */     this.rightHandS = "right hoof";
/*  46 */     this.leftFootS = "left hoof";
/*  47 */     this.rightFootS = "right hoof";
/*  48 */     this.leftArmS = "left foreleg";
/*  49 */     this.rightArmS = "right foreleg";
/*  50 */     this.leftLegS = "left hindleg";
/*  51 */     this.rightLegS = "right hindleg";
/*     */     
/*  53 */     this.type = 1;
/*  54 */     this.typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getRandomWoundPos() throws Exception {
/*  64 */     int rand = Server.rand.nextInt(1000);
/*  65 */     if (rand < 30)
/*  66 */       return 1; 
/*  67 */     if (rand < 80)
/*  68 */       return 5; 
/*  69 */     if (rand < 130)
/*  70 */       return 6; 
/*  71 */     if (rand < 180)
/*  72 */       return 7; 
/*  73 */     if (rand < 230)
/*  74 */       return 8; 
/*  75 */     if (rand < 280)
/*  76 */       return 9; 
/*  77 */     if (rand < 320)
/*  78 */       return 10; 
/*  79 */     if (rand < 370)
/*  80 */       return 11; 
/*  81 */     if (rand < 420)
/*  82 */       return 12; 
/*  83 */     if (rand < 460)
/*  84 */       return 13; 
/*  85 */     if (rand < 500)
/*  86 */       return 14; 
/*  87 */     if (rand < 540)
/*  88 */       return 15; 
/*  89 */     if (rand < 580)
/*  90 */       return 16; 
/*  91 */     if (rand < 600)
/*  92 */       return 17; 
/*  93 */     if (rand < 601)
/*  94 */       return 18; 
/*  95 */     if (rand < 602)
/*  96 */       return 19; 
/*  97 */     if (rand < 730)
/*  98 */       return 21; 
/*  99 */     if (rand < 780)
/* 100 */       return 22; 
/* 101 */     if (rand < 830)
/* 102 */       return 23; 
/* 103 */     if (rand < 890)
/* 104 */       return 24; 
/* 105 */     if (rand < 900)
/* 106 */       return 25; 
/* 107 */     if (rand < 950)
/* 108 */       return 26; 
/* 109 */     if (rand < 1000) {
/* 110 */       return 27;
/*     */     }
/* 112 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void buildBody(Item[] spaces, Creature owner) {
/* 118 */     spaces[0].setOwner(owner.getWurmId(), true);
/* 119 */     spaces[0].insertItem(spaces[1]);
/* 120 */     spaces[1].insertItem(spaces[29]);
/* 121 */     spaces[0].insertItem(spaces[2]);
/* 122 */     spaces[2].insertItem(spaces[34]);
/* 123 */     spaces[34].insertItem(spaces[15]);
/* 124 */     spaces[34].insertItem(spaces[16]);
/* 125 */     spaces[34].insertItem(spaces[3]);
/* 126 */     spaces[34].insertItem(spaces[4]);
/* 127 */     spaces[3].insertItem(spaces[13]);
/* 128 */     spaces[4].insertItem(spaces[14]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */