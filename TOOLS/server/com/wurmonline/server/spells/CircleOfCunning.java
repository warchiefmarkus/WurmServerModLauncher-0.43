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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CircleOfCunning
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   CircleOfCunning() {
/* 36 */     super("Circle of Cunning", 276, 20, 50, 60, 51, 0L);
/* 37 */     this.targetItem = true;
/* 38 */     this.enchantment = 13;
/* 39 */     this.effectdesc = "will increase skill gained with it when used.";
/* 40 */     this.description = "increases skill gain";
/* 41 */     this.type = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\CircleOfCunning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */