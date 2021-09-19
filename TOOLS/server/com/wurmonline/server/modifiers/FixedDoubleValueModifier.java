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
/*    */ public final class FixedDoubleValueModifier
/*    */   extends DoubleValueModifier
/*    */ {
/*    */   public FixedDoubleValueModifier(double aValue) {
/* 40 */     super(aValue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedDoubleValueModifier(int aType, double aValue) {
/* 51 */     super(aType, aValue);
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
/*    */   
/*    */   public void setModifier(double aNewValue) {
/* 64 */     assert false : "Do not call FixedDoubleValueModifier.setModifier()";
/*    */     
/* 66 */     throw new IllegalArgumentException("Do not call FixedDoubleValueModifier.setModifier(). The modifier cannot be changed.");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\modifiers\FixedDoubleValueModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */