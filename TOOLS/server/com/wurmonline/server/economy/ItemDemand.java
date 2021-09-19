/*    */ package com.wurmonline.server.economy;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ItemDemand
/*    */ {
/*    */   private final int templateId;
/*    */   private float demand;
/*    */   
/*    */   ItemDemand(int itemTemplateId, float dem) {
/* 41 */     this.templateId = itemTemplateId;
/* 42 */     this.demand = dem;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getTemplateId() {
/* 52 */     return this.templateId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   float getDemand() {
/* 62 */     return this.demand;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setDemand(float aDemand) {
/* 73 */     this.demand = aDemand;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\ItemDemand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */