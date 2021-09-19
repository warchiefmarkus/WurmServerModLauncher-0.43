/*    */ package org.fourthline.cling.model.meta;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.model.Validatable;
/*    */ import org.fourthline.cling.model.ValidationError;
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
/*    */ public class StateVariableAllowedValueRange
/*    */   implements Validatable
/*    */ {
/* 34 */   private static final Logger log = Logger.getLogger(StateVariableAllowedValueRange.class.getName());
/*    */   
/*    */   private final long minimum;
/*    */   private final long maximum;
/*    */   private final long step;
/*    */   
/*    */   public StateVariableAllowedValueRange(long minimum, long maximum) {
/* 41 */     this(minimum, maximum, 1L);
/*    */   }
/*    */   
/*    */   public StateVariableAllowedValueRange(long minimum, long maximum, long step) {
/* 45 */     if (minimum > maximum) {
/* 46 */       log.warning("UPnP specification violation, allowed value range minimum '" + minimum + "' is greater than maximum '" + maximum + "', switching values.");
/*    */       
/* 48 */       this.minimum = maximum;
/* 49 */       this.maximum = minimum;
/*    */     } else {
/* 51 */       this.minimum = minimum;
/* 52 */       this.maximum = maximum;
/*    */     } 
/* 54 */     this.step = step;
/*    */   }
/*    */   
/*    */   public long getMinimum() {
/* 58 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public long getMaximum() {
/* 62 */     return this.maximum;
/*    */   }
/*    */   
/*    */   public long getStep() {
/* 66 */     return this.step;
/*    */   }
/*    */   
/*    */   public boolean isInRange(long value) {
/* 70 */     return (value >= getMinimum() && value <= getMaximum() && value % this.step == 0L);
/*    */   }
/*    */   
/*    */   public List<ValidationError> validate() {
/* 74 */     return new ArrayList<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return "Range Min: " + getMinimum() + " Max: " + getMaximum() + " Step: " + getStep();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\StateVariableAllowedValueRange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */