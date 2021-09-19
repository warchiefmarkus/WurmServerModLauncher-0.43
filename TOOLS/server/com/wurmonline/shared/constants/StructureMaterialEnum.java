/*    */ package com.wurmonline.shared.constants;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum StructureMaterialEnum
/*    */ {
/* 14 */   WOOD((byte)0, "wood"),
/* 15 */   STONE((byte)1, "stone"),
/* 16 */   METAL((byte)2, "metal"),
/* 17 */   TIMBER_FRAMED((byte)3, "timber framed"),
/* 18 */   PLAIN_STONE((byte)4, "plain stone"),
/* 19 */   SLATE((byte)5, "slate"),
/* 20 */   ROUNDED_STONE((byte)6, "rounded stone"),
/* 21 */   POTTERY((byte)7, "pottery"),
/* 22 */   SANDSTONE((byte)8, "sandstone"),
/* 23 */   RENDERED((byte)9, "rendered"),
/* 24 */   MARBLE((byte)10, "marble"),
/* 25 */   IRON((byte)11, "iron"),
/* 26 */   LOG((byte)12, "log"),
/* 27 */   CRUDE_WOOD((byte)13, "crude wood"),
/* 28 */   FLOWER1((byte)14, "flower"),
/* 29 */   FLOWER2((byte)15, "flower"),
/* 30 */   FLOWER3((byte)16, "flower"),
/* 31 */   FLOWER4((byte)17, "flower"),
/* 32 */   FLOWER5((byte)18, "flower"),
/* 33 */   FLOWER6((byte)19, "flower"),
/* 34 */   FLOWER7((byte)20, "flower"),
/* 35 */   ICE((byte)21, "ice"),
/* 36 */   FIRE((byte)22, "fire");
/*    */   
/*    */   public final byte material;
/*    */   
/*    */   public final String nameString;
/*    */ 
/*    */   
/*    */   StructureMaterialEnum(byte _material, String _nameString) {
/* 44 */     this.material = _material;
/* 45 */     this.nameString = _nameString;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static StructureMaterialEnum getEnumByMaterial(byte material) {
/* 52 */     if (material >= 0 && material < (values()).length) {
/* 53 */       return values()[material];
/*    */     }
/* 55 */     Logger.getGlobal().warning("Reached default return value for material=" + material);
/*    */     
/* 57 */     return WOOD;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\StructureMaterialEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */