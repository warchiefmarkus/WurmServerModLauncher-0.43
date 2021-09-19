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
/*    */ public abstract class Alliance
/*    */ {
/*    */   final Village villone;
/*    */   final Village villtwo;
/*    */   
/*    */   Alliance(Village vone, Village vtwo) {
/* 31 */     this.villone = vone;
/* 32 */     this.villtwo = vtwo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   final Village getVillone() {
/* 41 */     return this.villone;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   final Village getVilltwo() {
/* 50 */     return this.villtwo;
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
/* 63 */     return "Alliance [" + this.villone + " and " + this.villtwo + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\Alliance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */