/*    */ package com.wurmonline.server.bodys;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.shared.exceptions.WurmServerException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class BodyBear
/*    */   extends BodyTemplate
/*    */ {
/*    */   BodyBear() {
/* 33 */     super((byte)2);
/* 34 */     this.leftHandS = "left paw";
/* 35 */     this.rightHandS = "right paw";
/* 36 */     this.typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getRandomWoundPos() throws Exception {
/* 46 */     int rand = Server.rand.nextInt(1000);
/* 47 */     if (rand < 30)
/* 48 */       return 1; 
/* 49 */     if (rand < 80)
/* 50 */       return 5; 
/* 51 */     if (rand < 130)
/* 52 */       return 6; 
/* 53 */     if (rand < 180)
/* 54 */       return 7; 
/* 55 */     if (rand < 230)
/* 56 */       return 8; 
/* 57 */     if (rand < 280)
/* 58 */       return 9; 
/* 59 */     if (rand < 320)
/* 60 */       return 10; 
/* 61 */     if (rand < 370)
/* 62 */       return 11; 
/* 63 */     if (rand < 420)
/* 64 */       return 12; 
/* 65 */     if (rand < 460)
/* 66 */       return 13; 
/* 67 */     if (rand < 500)
/* 68 */       return 14; 
/* 69 */     if (rand < 540)
/* 70 */       return 15; 
/* 71 */     if (rand < 580)
/* 72 */       return 16; 
/* 73 */     if (rand < 600)
/* 74 */       return 17; 
/* 75 */     if (rand < 601)
/* 76 */       return 18; 
/* 77 */     if (rand < 602)
/* 78 */       return 19; 
/* 79 */     if (rand < 730)
/* 80 */       return 21; 
/* 81 */     if (rand < 780)
/* 82 */       return 22; 
/* 83 */     if (rand < 830)
/* 84 */       return 23; 
/* 85 */     if (rand < 890)
/* 86 */       return 24; 
/* 87 */     if (rand < 900)
/* 88 */       return 25; 
/* 89 */     if (rand < 950)
/* 90 */       return 26; 
/* 91 */     if (rand < 1000) {
/* 92 */       return 27;
/*    */     }
/* 94 */     throw new WurmServerException("Bad randomizer");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyBear.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */