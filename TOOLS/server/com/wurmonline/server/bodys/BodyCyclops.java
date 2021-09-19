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
/*    */ final class BodyCyclops
/*    */   extends BodyTemplate
/*    */ {
/*    */   BodyCyclops() {
/* 33 */     super((byte)5);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getRandomWoundPos() throws Exception {
/* 39 */     int rand = Server.rand.nextInt(1000);
/* 40 */     if (rand < 30)
/* 41 */       return 1; 
/* 42 */     if (rand < 40)
/* 43 */       return 20; 
/* 44 */     if (rand < 80)
/* 45 */       return 5; 
/* 46 */     if (rand < 130)
/* 47 */       return 6; 
/* 48 */     if (rand < 180)
/* 49 */       return 7; 
/* 50 */     if (rand < 230)
/* 51 */       return 8; 
/* 52 */     if (rand < 280)
/* 53 */       return 9; 
/* 54 */     if (rand < 320)
/* 55 */       return 10; 
/* 56 */     if (rand < 370)
/* 57 */       return 11; 
/* 58 */     if (rand < 420)
/* 59 */       return 12; 
/* 60 */     if (rand < 460)
/* 61 */       return 13; 
/* 62 */     if (rand < 500)
/* 63 */       return 14; 
/* 64 */     if (rand < 540)
/* 65 */       return 15; 
/* 66 */     if (rand < 580)
/* 67 */       return 16; 
/* 68 */     if (rand < 600)
/* 69 */       return 17; 
/* 70 */     if (rand < 601)
/* 71 */       return 18; 
/* 72 */     if (rand < 602)
/* 73 */       return 19; 
/* 74 */     if (rand < 730)
/* 75 */       return 21; 
/* 76 */     if (rand < 780)
/* 77 */       return 22; 
/* 78 */     if (rand < 830)
/* 79 */       return 23; 
/* 80 */     if (rand < 890)
/* 81 */       return 24; 
/* 82 */     if (rand < 900)
/* 83 */       return 25; 
/* 84 */     if (rand < 950)
/* 85 */       return 26; 
/* 86 */     if (rand < 1000) {
/* 87 */       return 27;
/*    */     }
/* 89 */     throw new WurmServerException("Bad randomizer");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyCyclops.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */