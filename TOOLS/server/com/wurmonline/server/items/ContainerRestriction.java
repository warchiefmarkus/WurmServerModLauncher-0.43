/*    */ package com.wurmonline.server.items;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContainerRestriction
/*    */ {
/*    */   private final boolean onlyOneOf;
/*    */   private ArrayList<Integer> itemTemplateIds;
/* 11 */   private String emptySlotName = null;
/*    */ 
/*    */   
/*    */   public ContainerRestriction(boolean onlyOneOf, int... itemTemplateId) {
/* 15 */     this.onlyOneOf = onlyOneOf;
/* 16 */     this.itemTemplateIds = new ArrayList<>();
/*    */     
/* 18 */     for (int i : itemTemplateId) {
/* 19 */       this.itemTemplateIds.add(Integer.valueOf(i));
/*    */     }
/*    */   }
/*    */   
/*    */   public ContainerRestriction(boolean onlyOneOf, String emptySlotName, int... itemTemplateId) {
/* 24 */     this(onlyOneOf, itemTemplateId);
/* 25 */     setEmptySlotName(emptySlotName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canInsertItem(Item[] existing, Item toInsert) {
/* 31 */     if (!this.itemTemplateIds.contains(Integer.valueOf(toInsert.getTemplateId()))) {
/* 32 */       return false;
/*    */     }
/* 34 */     if (this.onlyOneOf)
/*    */     {
/*    */       
/* 37 */       for (Item i : existing) {
/* 38 */         if (this.itemTemplateIds.contains(Integer.valueOf(i.getTemplateId())))
/* 39 */           return false; 
/*    */       }  } 
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEmptySlotName(String name) {
/* 46 */     this.emptySlotName = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getEmptySlotName() {
/* 51 */     if (this.emptySlotName != null) {
/* 52 */       return this.emptySlotName;
/*    */     }
/* 54 */     return "empty " + ItemTemplateFactory.getInstance().getTemplateName(getEmptySlotTemplateId()) + " slot";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEmptySlotTemplateId() {
/* 59 */     return ((Integer)this.itemTemplateIds.get(0)).intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(int id) {
/* 64 */     return this.itemTemplateIds.contains(Integer.valueOf(id));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doesItemOverrideSlot(Item toInsert) {
/* 69 */     if (this.itemTemplateIds.contains(Integer.valueOf(toInsert.getTemplateId()))) {
/* 70 */       return true;
/*    */     }
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ContainerRestriction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */