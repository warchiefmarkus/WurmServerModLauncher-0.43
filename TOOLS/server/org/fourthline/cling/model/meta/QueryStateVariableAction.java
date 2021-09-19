/*    */ package org.fourthline.cling.model.meta;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ public class QueryStateVariableAction<S extends Service>
/*    */   extends Action<S>
/*    */ {
/*    */   public static final String INPUT_ARG_VAR_NAME = "varName";
/*    */   public static final String OUTPUT_ARG_RETURN = "return";
/*    */   public static final String ACTION_NAME = "QueryStateVariable";
/*    */   public static final String VIRTUAL_STATEVARIABLE_INPUT = "VirtualQueryActionInput";
/*    */   public static final String VIRTUAL_STATEVARIABLE_OUTPUT = "VirtualQueryActionOutput";
/*    */   
/*    */   public QueryStateVariableAction() {
/* 41 */     this(null);
/*    */   }
/*    */   
/*    */   public QueryStateVariableAction(S service) {
/* 45 */     super("QueryStateVariable", new ActionArgument[] { new ActionArgument<>("varName", "VirtualQueryActionInput", ActionArgument.Direction.IN), new ActionArgument<>("return", "VirtualQueryActionOutput", ActionArgument.Direction.OUT) });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 51 */     setService(service);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 56 */     return "QueryStateVariable";
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ValidationError> validate() {
/* 61 */     return Collections.EMPTY_LIST;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\QueryStateVariableAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */