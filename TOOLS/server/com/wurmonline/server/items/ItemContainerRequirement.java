/*    */ package com.wurmonline.server.items;
/*    */ 
/*    */ import com.wurmonline.server.Items;
/*    */ import com.wurmonline.server.creatures.Creature;
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
/*    */ final class ItemContainerRequirement
/*    */   extends CreationRequirement
/*    */ {
/*    */   ItemContainerRequirement(int aNumber, int aResourceTemplateId, int aResourceNumber, int aVolumeNeeded, boolean aConsume) {
/* 35 */     super(aNumber, aResourceTemplateId, aResourceNumber, aConsume);
/* 36 */     setVolumeNeeded(aVolumeNeeded);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean fill(Creature performer, Item container) {
/* 42 */     if (canBeFilled(container))
/*    */     {
/* 44 */       if (willBeConsumed()) {
/*    */         
/* 46 */         int found = 0;
/* 47 */         Item[] items = container.getAllItems(false);
/* 48 */         for (int i = 0; i < items.length; i++) {
/*    */           
/* 50 */           if (items[i].getTemplateId() == getResourceTemplateId()) {
/*    */             
/* 52 */             found++;
/* 53 */             Items.destroyItem(items[i].getWurmId());
/* 54 */             if (found == getResourceNumber())
/* 55 */               return true; 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     }
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean canBeFilled(Item container) {
/* 65 */     int found = 0;
/* 66 */     Item[] items = container.getAllItems(false);
/* 67 */     for (int i = 0; i < items.length; i++) {
/*    */       
/* 69 */       if (items[i].getTemplateId() == getResourceTemplateId()) {
/*    */         
/* 71 */         found++;
/* 72 */         if (found == getResourceNumber())
/* 73 */           return true; 
/*    */       } 
/*    */     } 
/* 76 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemContainerRequirement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */