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
/*     */ public final class BodySnake
/*     */   extends BodyTemplate
/*     */ {
/*     */   BodySnake() {
/*  29 */     super((byte)9);
/*  30 */     this.leftOverArmS = "upper body";
/*  31 */     this.rightOverArmS = "upper body";
/*  32 */     this.leftThighS = "center body";
/*  33 */     this.rightThighS = "center body";
/*  34 */     this.leftUnderArmS = "center body";
/*  35 */     this.rightUnderArmS = "center body";
/*  36 */     this.torsoS = "upper body";
/*  37 */     this.chestS = "chest";
/*  38 */     this.topBackS = "upper body";
/*  39 */     this.lowerBackS = "lower body";
/*  40 */     this.legsS = "tail";
/*  41 */     this.leftCalfS = "lower body";
/*  42 */     this.rightCalfS = "lower body";
/*  43 */     this.leftHandS = "center body";
/*  44 */     this.rightHandS = "center body";
/*  45 */     this.leftFootS = "tail";
/*  46 */     this.rightFootS = "tail";
/*  47 */     this.leftArmS = "upper body";
/*  48 */     this.rightArmS = "upper body";
/*  49 */     this.leftLegS = "center body";
/*  50 */     this.rightLegS = "center body";
/*  51 */     this.leftEyeS = "eyes";
/*  52 */     this.rightEyeS = "eyes";
/*  53 */     this.baseOfNoseS = "nostrils";
/*  54 */     this.typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void buildBody(Item[] spaces, Creature owner) {
/*  64 */     spaces[0].setOwner(owner.getWurmId(), true);
/*  65 */     spaces[0].insertItem(spaces[1]);
/*  66 */     spaces[1].insertItem(spaces[29]);
/*  67 */     spaces[0].insertItem(spaces[2]);
/*  68 */     spaces[2].insertItem(spaces[34]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getRandomWoundPos() throws Exception {
/*  74 */     int rand = Server.rand.nextInt(100);
/*  75 */     if (rand < 10)
/*  76 */       return 1; 
/*  77 */     if (rand < 40)
/*  78 */       return 21; 
/*  79 */     if (rand < 50)
/*  80 */       return 22; 
/*  81 */     if (rand < 60)
/*  82 */       return 24; 
/*  83 */     if (rand < 101) {
/*  84 */       return 34;
/*     */     }
/*  86 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getUpperLeftWoundPos() throws Exception {
/*  92 */     int rand = Server.rand.nextInt(100);
/*  93 */     if (rand < 3) {
/*  94 */       return 1;
/*     */     }
/*  96 */     return 21;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getUpperRightWoundPos() throws Exception {
/* 102 */     int rand = Server.rand.nextInt(100);
/* 103 */     if (rand < 3) {
/* 104 */       return 1;
/*     */     }
/* 106 */     return 21;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getHighWoundPos() throws Exception {
/* 112 */     int rand = Server.rand.nextInt(100);
/* 113 */     if (rand < 40)
/* 114 */       return 1; 
/* 115 */     if (rand < 60)
/* 116 */       return 17; 
/* 117 */     if (rand < 61)
/* 118 */       return 18; 
/* 119 */     if (rand < 62)
/* 120 */       return 19; 
/* 121 */     if (rand < 64)
/* 122 */       return 29; 
/* 123 */     if (rand < 81)
/* 124 */       return 22; 
/* 125 */     if (rand < 100) {
/* 126 */       return 21;
/*     */     }
/* 128 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getMidLeftWoundPos() throws Exception {
/* 134 */     int rand = Server.rand.nextInt(100);
/* 135 */     if (rand < 58)
/* 136 */       return 21; 
/* 137 */     if (rand < 76)
/* 138 */       return 22; 
/* 139 */     if (rand < 100) {
/* 140 */       return 24;
/*     */     }
/* 142 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getCenterWoundPos() throws Exception {
/* 148 */     int rand = Server.rand.nextInt(100);
/* 149 */     if (rand < 58)
/* 150 */       return 21; 
/* 151 */     if (rand < 76)
/* 152 */       return 22; 
/* 153 */     if (rand < 100) {
/* 154 */       return 24;
/*     */     }
/* 156 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getMidRightWoundPos() throws Exception {
/* 162 */     int rand = Server.rand.nextInt(100);
/* 163 */     if (rand < 58)
/* 164 */       return 21; 
/* 165 */     if (rand < 76)
/* 166 */       return 22; 
/* 167 */     if (rand < 100) {
/* 168 */       return 24;
/*     */     }
/* 170 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getLowerLeftWoundPos() throws Exception {
/* 176 */     int rand = Server.rand.nextInt(100);
/* 177 */     if (rand < 58)
/* 178 */       return 24; 
/* 179 */     if (rand < 100) {
/* 180 */       return 34;
/*     */     }
/* 182 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getLowWoundPos() throws Exception {
/* 188 */     int rand = Server.rand.nextInt(100);
/* 189 */     if (rand < 58)
/* 190 */       return 24; 
/* 191 */     if (rand < 100) {
/* 192 */       return 34;
/*     */     }
/* 194 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte getLowerRightWoundPos() throws Exception {
/* 200 */     int rand = Server.rand.nextInt(100);
/* 201 */     if (rand < 58)
/* 202 */       return 24; 
/* 203 */     if (rand < 100) {
/* 204 */       return 34;
/*     */     }
/* 206 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodySnake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */