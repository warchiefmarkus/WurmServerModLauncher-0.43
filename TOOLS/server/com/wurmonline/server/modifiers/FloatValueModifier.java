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
/*    */ 
/*    */ 
/*    */ public final class FloatValueModifier
/*    */   extends ValueModifier
/*    */ {
/* 29 */   private float modifier = 0.0F;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FloatValueModifier(float value) {
/* 37 */     this.modifier = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatValueModifier(int aType, float value) {
/* 42 */     super(aType);
/* 43 */     this.modifier = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getModifier() {
/* 52 */     return this.modifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setModifier(float newValue) {
/* 61 */     this.modifier = newValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\modifiers\FloatValueModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */