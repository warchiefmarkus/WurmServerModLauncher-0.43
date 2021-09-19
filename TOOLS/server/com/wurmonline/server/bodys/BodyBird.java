/*    */ package com.wurmonline.server.bodys;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
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
/*    */ final class BodyBird
/*    */   extends BodyTemplate
/*    */ {
/*    */   BodyBird() {
/* 32 */     super((byte)7);
/* 33 */     this.leftArmS = "left wing";
/* 34 */     this.rightArmS = "right wing";
/* 35 */     this.leftHandS = "left wingtip";
/* 36 */     this.rightHandS = "right wingtip";
/* 37 */     this.leftFootS = "left claw";
/* 38 */     this.rightFootS = "right claw";
/* 39 */     this.legsS = "claws";
/* 40 */     this.lowerBackS = "tail";
/* 41 */     this.baseOfNoseS = "beak";
/* 42 */     this.legsS = "claws";
/* 43 */     this.typeString = new String[] { this.bodyS, this.headS, this.bodyS, this.leftArmS, this.rightArmS, this.leftArmS, this.rightArmS, this.leftFootS, this.rightFootS, this.leftArmS, this.rightArmS, this.leftFootS, this.rightFootS, this.leftHandS, this.rightHandS, this.leftFootS, this.rightFootS, this.neckS, this.headS, this.headS, this.headS, this.bodyS, this.topBackS, this.stomachS, this.lowerBackS, this.bodyS, this.leftArmS, this.rightArmS, this.headS, this.headS, this.leftLegS, this.rightLegS, this.bodyS, this.baseOfNoseS, this.legsS };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getRandomWoundPos() throws Exception {
/* 52 */     int rand = Server.rand.nextInt(100);
/* 53 */     if (rand < 10)
/* 54 */       return 1; 
/* 55 */     if (rand < 30)
/* 56 */       return 13; 
/* 57 */     if (rand < 60)
/* 58 */       return 14; 
/* 59 */     if (rand < 85)
/* 60 */       return 0; 
/* 61 */     if (rand < 95)
/* 62 */       return 15; 
/* 63 */     if (rand < 100) {
/* 64 */       return 16;
/*    */     }
/* 66 */     throw new WurmServerException("Bad randomizer");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void buildBody(Item[] spaces, Creature owner) {
/* 72 */     spaces[0].setOwner(owner.getWurmId(), true);
/* 73 */     spaces[0].insertItem(spaces[1]);
/* 74 */     spaces[1].insertItem(spaces[29]);
/* 75 */     spaces[0].insertItem(spaces[2]);
/* 76 */     spaces[2].insertItem(spaces[34]);
/* 77 */     spaces[34].insertItem(spaces[15]);
/* 78 */     spaces[34].insertItem(spaces[16]);
/* 79 */     spaces[34].insertItem(spaces[3]);
/* 80 */     spaces[34].insertItem(spaces[4]);
/* 81 */     spaces[3].insertItem(spaces[13]);
/* 82 */     spaces[4].insertItem(spaces[14]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyBird.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */