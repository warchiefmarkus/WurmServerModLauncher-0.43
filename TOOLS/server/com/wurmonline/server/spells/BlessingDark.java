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
/*    */ 
/*    */ public class BlessingDark
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public BlessingDark() {
/* 37 */     super("Blessings of the Dark", 456, 20, 70, 60, 51, 0L);
/* 38 */     this.targetItem = true;
/* 39 */     this.enchantment = 47;
/* 40 */     this.effectdesc = "will increase skill gained and speed with it when used.";
/* 41 */     this.description = "increases skill gain and usage speed";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\BlessingDark.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */