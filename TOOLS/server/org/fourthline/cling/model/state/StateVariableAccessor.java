/*    */ package org.fourthline.cling.model.state;
/*    */ 
/*    */ import org.fourthline.cling.model.Command;
/*    */ import org.fourthline.cling.model.ServiceManager;
/*    */ import org.fourthline.cling.model.meta.LocalService;
/*    */ import org.fourthline.cling.model.meta.StateVariable;
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
/*    */ public abstract class StateVariableAccessor
/*    */ {
/*    */   public StateVariableValue read(final StateVariable<LocalService> stateVariable, final Object serviceImpl) throws Exception {
/*    */     class AccessCommand
/*    */       implements Command
/*    */     {
/*    */       Object result;
/*    */       
/*    */       public void execute(ServiceManager serviceManager) throws Exception {
/* 38 */         this.result = StateVariableAccessor.this.read(serviceImpl);
/* 39 */         if (((LocalService)stateVariable.getService()).isStringConvertibleType(this.result)) {
/* 40 */           this.result = this.result.toString();
/*    */         }
/*    */       }
/*    */     };
/*    */     
/* 45 */     AccessCommand cmd = new AccessCommand();
/* 46 */     ((LocalService)stateVariable.getService()).getManager().execute(cmd);
/* 47 */     return new StateVariableValue<>(stateVariable, cmd.result);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Class<?> getReturnType();
/*    */ 
/*    */   
/*    */   public abstract Object read(Object paramObject) throws Exception;
/*    */   
/*    */   public String toString() {
/* 57 */     return "(" + getClass().getSimpleName() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\state\StateVariableAccessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */