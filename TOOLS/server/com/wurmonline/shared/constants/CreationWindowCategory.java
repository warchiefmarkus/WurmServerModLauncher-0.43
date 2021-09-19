/*    */ package com.wurmonline.shared.constants;
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
/*    */ public enum CreationWindowCategory
/*    */ {
/* 28 */   NONE(0, ""), EPIC(1, "Epic"), ARMOUR(2, "Armour"), CONTAINERS(3, "Containers"), FOOD(4, "Food"), LOCKS(5, "Locks"), MAGIC(6, "Magic"),
/* 29 */   POTTERY(7, "Pottery"), DECORATIONS(8, "Decorations"), TOOLS(9, "Tools"), SHIELDS(10, "Shields"), WEAPONS(11, "Weapons"),
/* 30 */   MISCELLANEOUS(12, "Miscellaneous");
/*    */   
/*    */   private final byte id;
/*    */   private final String name;
/*    */   private static final CreationWindowCategory[] types;
/*    */   
/*    */   CreationWindowCategory(int id, String name) {
/* 37 */     this.id = (byte)id;
/* 38 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 43 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 55 */     types = values();
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
/*    */   public static CreationWindowCategory creationWindowCategoryFromId(byte aId) {
/* 67 */     for (int i = 0; i < types.length; i++) {
/*    */       
/* 69 */       if (aId == types[i].getId()) {
/* 70 */         return types[i];
/*    */       }
/*    */     } 
/* 73 */     return NONE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte idFromName(String aName) {
/* 79 */     for (int i = 0; i < types.length; i++) {
/*    */       
/* 81 */       if (aName.equals(types[i].getName())) {
/* 82 */         return types[i].getId();
/*    */       }
/*    */     } 
/* 85 */     return NONE.getId();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\CreationWindowCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */