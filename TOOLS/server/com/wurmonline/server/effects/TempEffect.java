/*    */ package com.wurmonline.server.effects;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class TempEffect
/*    */   extends Effect
/*    */ {
/*    */   public TempEffect(long aOwner, short aType, float aPosX, float aPosY, float aPosZ, boolean aSurfaced) {
/* 10 */     super(aOwner, aType, aPosX, aPosY, aPosZ, aSurfaced);
/*    */   }
/*    */ 
/*    */   
/*    */   public TempEffect(int num, long ownerid, short typ, float posx, float posy, float posz, long stime) {
/* 15 */     super(num, ownerid, typ, posx, posy, posz, stime);
/*    */   }
/*    */ 
/*    */   
/*    */   public TempEffect(long aOwner, int aNumber) throws IOException {
/* 20 */     super(aOwner, aNumber);
/*    */   }
/*    */   
/*    */   public void save() throws IOException {}
/*    */   
/*    */   void load() throws IOException {}
/*    */   
/*    */   void delete() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\effects\TempEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */