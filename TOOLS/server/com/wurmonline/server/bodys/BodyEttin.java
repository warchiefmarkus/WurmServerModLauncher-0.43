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
/*     */ final class BodyEttin
/*     */   extends BodyTemplate
/*     */ {
/*     */   BodyEttin() {
/*  35 */     super((byte)4);
/*  36 */     this.headS = "left head";
/*  37 */     this.secondHeadS = "right head";
/*  38 */     this.typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getRandomWoundPos() throws Exception {
/*  48 */     int rand = Server.rand.nextInt(1000);
/*  49 */     if (rand < 30)
/*  50 */       return 1; 
/*  51 */     if (rand < 60)
/*  52 */       return 28; 
/*  53 */     if (rand < 110)
/*  54 */       return 5; 
/*  55 */     if (rand < 160)
/*  56 */       return 6; 
/*  57 */     if (rand < 210)
/*  58 */       return 7; 
/*  59 */     if (rand < 260)
/*  60 */       return 8; 
/*  61 */     if (rand < 310)
/*  62 */       return 9; 
/*  63 */     if (rand < 360)
/*  64 */       return 10; 
/*  65 */     if (rand < 410)
/*  66 */       return 11; 
/*  67 */     if (rand < 460)
/*  68 */       return 12; 
/*  69 */     if (rand < 500)
/*  70 */       return 13; 
/*  71 */     if (rand < 540)
/*  72 */       return 14; 
/*  73 */     if (rand < 580)
/*  74 */       return 15; 
/*  75 */     if (rand < 620)
/*  76 */       return 16; 
/*  77 */     if (rand < 630)
/*  78 */       return 17; 
/*  79 */     if (rand < 631)
/*  80 */       return 18; 
/*  81 */     if (rand < 632)
/*  82 */       return 19; 
/*  83 */     if (rand < 730)
/*  84 */       return 21; 
/*  85 */     if (rand < 780)
/*  86 */       return 22; 
/*  87 */     if (rand < 830)
/*  88 */       return 23; 
/*  89 */     if (rand < 890)
/*  90 */       return 24; 
/*  91 */     if (rand < 900)
/*  92 */       return 25; 
/*  93 */     if (rand < 950)
/*  94 */       return 26; 
/*  95 */     if (rand < 1000) {
/*  96 */       return 27;
/*     */     }
/*  98 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void buildBody(Item[] spaces, Creature owner) {
/* 104 */     spaces[0].setOwner(owner.getWurmId(), true);
/* 105 */     spaces[0].insertItem(spaces[1]);
/* 106 */     spaces[0].insertItem(spaces[28]);
/* 107 */     spaces[1].insertItem(spaces[29]);
/* 108 */     spaces[0].insertItem(spaces[2]);
/* 109 */     spaces[2].insertItem(spaces[3]);
/* 110 */     spaces[2].insertItem(spaces[4]);
/* 111 */     spaces[3].insertItem(spaces[13]);
/* 112 */     spaces[4].insertItem(spaces[14]);
/*     */     
/* 114 */     spaces[2].insertItem(spaces[34]);
/* 115 */     spaces[34].insertItem(spaces[15]);
/* 116 */     spaces[34].insertItem(spaces[16]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyEttin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */