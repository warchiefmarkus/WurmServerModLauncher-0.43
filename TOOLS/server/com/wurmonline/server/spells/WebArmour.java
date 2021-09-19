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
/*    */ public class WebArmour
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public WebArmour() {
/* 31 */     super("Web Armour", 455, 20, 35, 60, 25, 0L);
/* 32 */     this.targetArmour = true;
/* 33 */     this.enchantment = 46;
/* 34 */     this.effectdesc = "may slow down creatures when they hit this armour.";
/* 35 */     this.description = "may slow down creatures when they hit armour enchanted with this";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WebArmour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */