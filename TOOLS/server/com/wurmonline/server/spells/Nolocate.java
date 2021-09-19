/*    */ package com.wurmonline.server.spells;
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
/*    */ public class Nolocate
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public Nolocate() {
/* 29 */     super("Nolocate", 451, 15, 60, 10, 22, 0L);
/* 30 */     this.targetJewelry = true;
/* 31 */     this.enchantment = 29;
/* 32 */     this.effectdesc = "protects from being located.";
/* 33 */     this.description = "hides you from locate spells";
/* 34 */     this.type = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Nolocate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */