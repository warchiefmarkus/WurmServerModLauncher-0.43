/*    */ package org.fourthline.cling.support.igd.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
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
/*    */ public abstract class GetExternalIP
/*    */   extends ActionCallback
/*    */ {
/*    */   public GetExternalIP(Service service) {
/* 28 */     super(new ActionInvocation(service.getAction("GetExternalIPAddress")));
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 33 */     success((String)invocation.getOutput("NewExternalIPAddress").getValue());
/*    */   }
/*    */   
/*    */   protected abstract void success(String paramString);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\igd\callback\GetExternalIP.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */