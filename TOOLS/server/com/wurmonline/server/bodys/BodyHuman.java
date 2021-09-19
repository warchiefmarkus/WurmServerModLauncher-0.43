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
/*    */ public final class BodyHuman
/*    */   extends BodyTemplate
/*    */ {
/*    */   public BodyHuman() {
/* 33 */     super((byte)0);
/*    */   }
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


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyHuman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */