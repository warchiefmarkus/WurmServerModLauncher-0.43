/*    */ package com.wurmonline.server.bodys;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
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
/*    */ final class BodySpider
/*    */   extends BodyTemplate
/*    */ {
/*    */   BodySpider() {
/* 28 */     super((byte)8);
/* 29 */     this.leftOverArmS = "left foreleg";
/* 30 */     this.rightOverArmS = "right foreleg";
/* 31 */     this.leftThighS = "left hindleg";
/* 32 */     this.rightThighS = "right hindleg";
/* 33 */     this.leftUnderArmS = "left foreleg";
/* 34 */     this.rightUnderArmS = "right foreleg ";
/* 35 */     this.leftCalfS = "left hindleg";
/* 36 */     this.rightCalfS = "right hindleg";
/* 37 */     this.leftHandS = "left leg";
/* 38 */     this.rightHandS = "right leg";
/* 39 */     this.leftFootS = "left leg";
/* 40 */     this.rightFootS = "right leg";
/* 41 */     this.leftArmS = "left foreleg";
/* 42 */     this.rightArmS = "right foreleg";
/* 43 */     this.leftLegS = "left hindleg";
/* 44 */     this.rightLegS = "right hindleg";
/* 45 */     this.leftEyeS = "eyes";
/* 46 */     this.rightEyeS = "eyes";
/* 47 */     this.baseOfNoseS = "mandibles";
/* 48 */     this.typeString = new String[] { this.bodyS, this.headS, this.torsoS, this.leftArmS, this.rightArmS, this.leftOverArmS, this.rightOverArmS, this.leftThighS, this.rightThighS, this.leftUnderArmS, this.rightUnderArmS, this.leftCalfS, this.rightCalfS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.leftEyeS, this.rightEyeS, this.centerEyeS, this.chestS, this.topBackS, this.stomachS, this.lowerBackS, this.crotchS, this.leftShoulderS, this.rightShoulderS, this.secondHeadS, this.faceS, this.leftLegS, this.rightLegS, this.hipS, this.baseOfNoseS, this.legsS };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void buildBody(Item[] spaces, Creature owner) {
/* 57 */     spaces[0].setOwner(owner.getWurmId(), true);
/* 58 */     spaces[0].insertItem(spaces[1]);
/* 59 */     spaces[1].insertItem(spaces[29]);
/* 60 */     spaces[0].insertItem(spaces[2]);
/* 61 */     spaces[2].insertItem(spaces[34]);
/* 62 */     spaces[34].insertItem(spaces[15]);
/* 63 */     spaces[34].insertItem(spaces[16]);
/* 64 */     spaces[34].insertItem(spaces[3]);
/* 65 */     spaces[34].insertItem(spaces[4]);
/* 66 */     spaces[34].insertItem(spaces[13]);
/* 67 */     spaces[34].insertItem(spaces[14]);
/* 68 */     spaces[34].insertItem(spaces[30]);
/* 69 */     spaces[34].insertItem(spaces[31]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodySpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */