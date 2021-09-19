/*     */ package org.fourthline.cling.model.action;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.Command;
/*     */ import org.fourthline.cling.model.ServiceManager;
/*     */ import org.fourthline.cling.model.meta.Action;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.state.StateVariableAccessor;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ import org.seamless.util.Exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractActionExecutor
/*     */   implements ActionExecutor
/*     */ {
/*  40 */   private static Logger log = Logger.getLogger(AbstractActionExecutor.class.getName());
/*     */   
/*  42 */   protected Map<ActionArgument<LocalService>, StateVariableAccessor> outputArgumentAccessors = new HashMap<>();
/*     */ 
/*     */   
/*     */   protected AbstractActionExecutor() {}
/*     */ 
/*     */   
/*     */   protected AbstractActionExecutor(Map<ActionArgument<LocalService>, StateVariableAccessor> outputArgumentAccessors) {
/*  49 */     this.outputArgumentAccessors = outputArgumentAccessors;
/*     */   }
/*     */   
/*     */   public Map<ActionArgument<LocalService>, StateVariableAccessor> getOutputArgumentAccessors() {
/*  53 */     return this.outputArgumentAccessors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final ActionInvocation<LocalService> actionInvocation) {
/*  61 */     log.fine("Invoking on local service: " + actionInvocation);
/*     */     
/*  63 */     LocalService service = (LocalService)actionInvocation.getAction().getService();
/*     */ 
/*     */     
/*     */     try {
/*  67 */       if (service.getManager() == null) {
/*  68 */         throw new IllegalStateException("Service has no implementation factory, can't get service instance");
/*     */       }
/*     */       
/*  71 */       service.getManager().execute(new Command() {
/*     */             public void execute(ServiceManager serviceManager) throws Exception {
/*  73 */               AbstractActionExecutor.this.execute(actionInvocation, serviceManager
/*     */                   
/*  75 */                   .getImplementation());
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public String toString() {
/*  81 */               return "Action invocation: " + actionInvocation.getAction();
/*     */             }
/*     */           });
/*     */     }
/*  85 */     catch (ActionException ex) {
/*  86 */       if (log.isLoggable(Level.FINE)) {
/*  87 */         log.fine("ActionException thrown by service, wrapping in invocation and returning: " + ex);
/*  88 */         log.log(Level.FINE, "Exception root cause: ", Exceptions.unwrap(ex));
/*     */       } 
/*  90 */       actionInvocation.setFailure(ex);
/*  91 */     } catch (InterruptedException ex) {
/*  92 */       if (log.isLoggable(Level.FINE)) {
/*  93 */         log.fine("InterruptedException thrown by service, wrapping in invocation and returning: " + ex);
/*  94 */         log.log(Level.FINE, "Exception root cause: ", Exceptions.unwrap(ex));
/*     */       } 
/*  96 */       actionInvocation.setFailure(new ActionCancelledException(ex));
/*  97 */     } catch (Throwable t) {
/*  98 */       Throwable rootCause = Exceptions.unwrap(t);
/*  99 */       if (log.isLoggable(Level.FINE)) {
/* 100 */         log.fine("Execution has thrown, wrapping root cause in ActionException and returning: " + t);
/* 101 */         log.log(Level.FINE, "Exception root cause: ", rootCause);
/*     */       } 
/* 103 */       actionInvocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, 
/*     */ 
/*     */             
/* 106 */             (rootCause.getMessage() != null) ? rootCause.getMessage() : rootCause.toString(), rootCause));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void execute(ActionInvocation<LocalService> paramActionInvocation, Object paramObject) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readOutputArgumentValues(Action<LocalService> action, Object instance) throws Exception {
/* 125 */     Object[] results = new Object[(action.getOutputArguments()).length];
/* 126 */     log.fine("Attempting to retrieve output argument values using accessor: " + results.length);
/*     */     
/* 128 */     int i = 0;
/* 129 */     for (ActionArgument outputArgument : action.getOutputArguments()) {
/* 130 */       log.finer("Calling acccessor method for: " + outputArgument);
/*     */       
/* 132 */       StateVariableAccessor accessor = getOutputArgumentAccessors().get(outputArgument);
/* 133 */       if (accessor != null) {
/* 134 */         log.fine("Calling accessor to read output argument value: " + accessor);
/* 135 */         results[i++] = accessor.read(instance);
/*     */       } else {
/* 137 */         throw new IllegalStateException("No accessor bound for: " + outputArgument);
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     if (results.length == 1) {
/* 142 */       return results[0];
/*     */     }
/* 144 */     return (results.length > 0) ? results : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setOutputArgumentValue(ActionInvocation<LocalService> actionInvocation, ActionArgument<LocalService> argument, Object result) throws ActionException {
/* 153 */     LocalService service = (LocalService)actionInvocation.getAction().getService();
/*     */     
/* 155 */     if (result != null) {
/*     */       try {
/* 157 */         if (service.isStringConvertibleType(result)) {
/* 158 */           log.fine("Result of invocation matches convertible type, setting toString() single output argument value");
/* 159 */           actionInvocation.setOutput(new ActionArgumentValue<>(argument, result.toString()));
/*     */         } else {
/* 161 */           log.fine("Result of invocation is Object, setting single output argument value");
/* 162 */           actionInvocation.setOutput(new ActionArgumentValue<>(argument, result));
/*     */         } 
/* 164 */       } catch (InvalidValueException ex) {
/* 165 */         throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Wrong type or invalid value for '" + argument
/*     */             
/* 167 */             .getName() + "': " + ex.getMessage(), ex);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 173 */       log.fine("Result of invocation is null, not setting any output argument value(s)");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\AbstractActionExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */