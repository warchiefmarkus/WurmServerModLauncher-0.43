/*    */ package org.fourthline.cling.model.action;
/*    */ 
/*    */ import org.fourthline.cling.model.meta.LocalService;
/*    */ import org.fourthline.cling.model.meta.StateVariable;
/*    */ import org.fourthline.cling.model.state.StateVariableAccessor;
/*    */ import org.fourthline.cling.model.types.ErrorCode;
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
/*    */ public class QueryStateVariableExecutor
/*    */   extends AbstractActionExecutor
/*    */ {
/*    */   protected void execute(ActionInvocation<LocalService> actionInvocation, Object serviceImpl) throws Exception {
/* 35 */     if (actionInvocation.getAction() instanceof org.fourthline.cling.model.meta.QueryStateVariableAction) {
/* 36 */       if (!((LocalService)actionInvocation.getAction().getService()).isSupportsQueryStateVariables()) {
/* 37 */         actionInvocation.setFailure(new ActionException(ErrorCode.INVALID_ACTION, "This service does not support querying state variables"));
/*    */       }
/*    */       else {
/*    */         
/* 41 */         executeQueryStateVariable(actionInvocation, serviceImpl);
/*    */       } 
/*    */     } else {
/* 44 */       throw new IllegalStateException("This class can only execute QueryStateVariableAction's, not: " + actionInvocation
/* 45 */           .getAction());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void executeQueryStateVariable(ActionInvocation<LocalService> actionInvocation, Object serviceImpl) throws Exception {
/* 52 */     LocalService service = (LocalService)actionInvocation.getAction().getService();
/*    */     
/* 54 */     String stateVariableName = actionInvocation.getInput("varName").toString();
/* 55 */     StateVariable stateVariable = service.getStateVariable(stateVariableName);
/*    */     
/* 57 */     if (stateVariable == null) {
/* 58 */       throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "No state variable found: " + stateVariableName);
/*    */     }
/*    */ 
/*    */     
/*    */     StateVariableAccessor accessor;
/*    */     
/* 64 */     if ((accessor = service.getAccessor(stateVariable.getName())) == null) {
/* 65 */       throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "No accessor for state variable, can't read state: " + stateVariableName);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 71 */       setOutputArgumentValue(actionInvocation, actionInvocation
/*    */           
/* 73 */           .getAction().getOutputArgument("return"), accessor
/* 74 */           .read(stateVariable, serviceImpl).toString());
/*    */     }
/* 76 */     catch (Exception ex) {
/* 77 */       throw new ActionException(ErrorCode.ACTION_FAILED, ex.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\QueryStateVariableExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */