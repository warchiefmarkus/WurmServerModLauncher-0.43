/*    */ package org.fourthline.cling.binding.staging;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.meta.StateVariable;
/*    */ import org.fourthline.cling.model.meta.StateVariableAllowedValueRange;
/*    */ import org.fourthline.cling.model.meta.StateVariableEventDetails;
/*    */ import org.fourthline.cling.model.meta.StateVariableTypeDetails;
/*    */ import org.fourthline.cling.model.types.Datatype;
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
/*    */ public class MutableStateVariable
/*    */ {
/*    */   public String name;
/*    */   public Datatype dataType;
/*    */   public String defaultValue;
/*    */   public List<String> allowedValues;
/*    */   public MutableAllowedValueRange allowedValueRange;
/*    */   public StateVariableEventDetails eventDetails;
/*    */   
/*    */   public StateVariable build() {
/* 39 */     return new StateVariable(this.name, new StateVariableTypeDetails(this.dataType, this.defaultValue, (this.allowedValues == null || this.allowedValues
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 44 */           .size() == 0) ? null : this.allowedValues
/*    */           
/* 46 */           .<String>toArray(new String[this.allowedValues.size()]), (this.allowedValueRange == null) ? null : new StateVariableAllowedValueRange(this.allowedValueRange.minimum
/*    */ 
/*    */ 
/*    */             
/* 50 */             .longValue(), this.allowedValueRange.maximum
/* 51 */             .longValue(), this.allowedValueRange.step
/* 52 */             .longValue())), this.eventDetails);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\staging\MutableStateVariable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */