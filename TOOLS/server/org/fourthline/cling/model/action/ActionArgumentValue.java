/*    */ package org.fourthline.cling.model.action;
/*    */ 
/*    */ import org.fourthline.cling.model.VariableValue;
/*    */ import org.fourthline.cling.model.meta.ActionArgument;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
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
/*    */ public class ActionArgumentValue<S extends Service>
/*    */   extends VariableValue
/*    */ {
/*    */   private final ActionArgument<S> argument;
/*    */   
/*    */   public ActionArgumentValue(ActionArgument<S> argument, Object value) throws InvalidValueException {
/* 33 */     super(argument.getDatatype(), (value != null && value.getClass().isEnum()) ? value.toString() : value);
/* 34 */     this.argument = argument;
/*    */   }
/*    */   
/*    */   public ActionArgument<S> getArgument() {
/* 38 */     return this.argument;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\ActionArgumentValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */