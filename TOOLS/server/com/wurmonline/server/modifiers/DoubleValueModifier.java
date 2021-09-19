/*    */ package com.wurmonline.server.modifiers;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ public class DoubleValueModifier
/*    */   extends ValueModifier
/*    */ {
/* 29 */   private double modifier = 0.0D;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DoubleValueModifier(double value) {
/* 38 */     this.modifier = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DoubleValueModifier(int aType, double value) {
/* 49 */     super(aType);
/* 50 */     this.modifier = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getModifier() {
/* 59 */     return this.modifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setModifier(double newValue) {
/* 68 */     this.modifier = newValue;
/* 69 */     if (getListeners() != null)
/*    */     {
/* 71 */       for (Iterator<ValueModifiedListener> it = getListeners().iterator(); it.hasNext(); ) {
/*    */         
/* 73 */         ValueModifiedListener list = it.next();
/* 74 */         list.valueChanged(this.modifier, newValue);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\modifiers\DoubleValueModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */