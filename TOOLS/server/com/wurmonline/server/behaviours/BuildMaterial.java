/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.server.items.ItemTemplateFactory;
/*    */ import com.wurmonline.server.items.NoSuchTemplateException;
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
/*    */ public class BuildMaterial
/*    */ {
/*    */   private final int templateId;
/*    */   private final int weightGrams;
/*    */   private final int totalQuantityRequired;
/*    */   private int neededQuantity;
/*    */   
/*    */   public BuildMaterial(int tid, int quantity) throws NoSuchTemplateException {
/* 37 */     int qty = (quantity < 0) ? 0 : quantity;
/* 38 */     this.templateId = tid;
/* 39 */     this.weightGrams = ItemTemplateFactory.getInstance().getTemplate(tid).getWeightGrams();
/* 40 */     this.totalQuantityRequired = qty;
/* 41 */     this.neededQuantity = qty;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTemplateId() {
/* 51 */     return this.templateId;
/*    */   }
/*    */ 
/*    */   
/*    */   int getTotalQuantityRequired() {
/* 56 */     return this.totalQuantityRequired;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getWeightGrams() {
/* 66 */     return this.weightGrams;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     String toReturn = "";
/*    */ 
/*    */     
/*    */     try {
/* 76 */       toReturn = "" + (this.weightGrams / ItemTemplateFactory.getInstance().getTemplate(this.templateId).getWeightGrams()) + " " + ItemTemplateFactory.getInstance().getTemplate(this.templateId).getName();
/*    */     }
/* 78 */     catch (NoSuchTemplateException noSuchTemplateException) {}
/*    */ 
/*    */ 
/*    */     
/* 82 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNeededQuantity(int qty) {
/* 90 */     this.neededQuantity = (qty < 0) ? 0 : qty;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNeededQuantity() {
/* 95 */     return this.neededQuantity;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\BuildMaterial.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */