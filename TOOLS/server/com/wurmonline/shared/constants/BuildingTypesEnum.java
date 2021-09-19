/*    */ package com.wurmonline.shared.constants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum BuildingTypesEnum
/*    */ {
/*  8 */   HOUSE("structure.wall.house"),
/*  9 */   ALLFENCES("structure.wall.fence"),
/* 10 */   FLOOR("structure.floor"),
/* 11 */   ROOF("structure.roof"),
/* 12 */   STAIRCASE("structure.staircase");
/*    */   
/*    */   public final String modelString;
/*    */   
/*    */   BuildingTypesEnum(String _modelString) {
/* 17 */     this.modelString = _modelString;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getModelString() {
/* 22 */     return "model." + this.modelString;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getTextureString() {
/* 27 */     return "img.texture." + this.modelString;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\BuildingTypesEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */