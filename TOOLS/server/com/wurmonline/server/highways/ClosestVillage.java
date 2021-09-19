/*    */ package com.wurmonline.server.highways;
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
/*    */ 
/*    */ public class ClosestVillage
/*    */ {
/*    */   private final String name;
/*    */   private final short distance;
/*    */   
/*    */   ClosestVillage(String name, short distance) {
/* 33 */     this.name = name;
/* 34 */     this.distance = distance;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 39 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public short getDistance() {
/* 44 */     return this.distance;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\ClosestVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */