/*    */ package com.wurmonline.server.modifiers;
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
/*    */ public final class IntValueModifier
/*    */   extends ValueModifier
/*    */ {
/* 27 */   private int modifier = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IntValueModifier(int value) {
/* 35 */     this.modifier = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntValueModifier(int aType, int value) {
/* 40 */     super(aType);
/* 41 */     this.modifier = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getModifier() {
/* 50 */     return this.modifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setModifier(int newValue) {
/* 59 */     this.modifier = newValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\modifiers\IntValueModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */