/*    */ package com.wurmonline.server.villages;
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
/*    */ public abstract class VillageWar
/*    */ {
/*    */   final Village villone;
/*    */   public final Village villtwo;
/*    */   
/*    */   VillageWar(Village vone, Village vtwo) {
/* 32 */     this.villone = vone;
/* 33 */     this.villtwo = vtwo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Village getVillone() {
/* 42 */     return this.villone;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Village getVilltwo() {
/* 51 */     return this.villtwo;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   abstract void save();
/*    */ 
/*    */ 
/*    */   
/*    */   abstract void delete();
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 64 */     return "VillageWar [" + this.villone + " and " + this.villtwo + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\VillageWar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */