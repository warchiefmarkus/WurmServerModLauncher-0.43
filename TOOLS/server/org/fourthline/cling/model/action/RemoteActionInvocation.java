/*    */ package org.fourthline.cling.model.action;
/*    */ 
/*    */ import org.fourthline.cling.model.meta.Action;
/*    */ import org.fourthline.cling.model.profile.RemoteClientInfo;
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
/*    */ public class RemoteActionInvocation
/*    */   extends ActionInvocation
/*    */ {
/*    */   protected final RemoteClientInfo remoteClientInfo;
/*    */   
/*    */   public RemoteActionInvocation(Action<S> action, ActionArgumentValue[] input, ActionArgumentValue[] output, RemoteClientInfo remoteClientInfo) {
/* 34 */     super(action, (ActionArgumentValue<S>[])input, (ActionArgumentValue<S>[])output, null);
/* 35 */     this.remoteClientInfo = remoteClientInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   public RemoteActionInvocation(Action<S> action, RemoteClientInfo remoteClientInfo) {
/* 40 */     super(action);
/* 41 */     this.remoteClientInfo = remoteClientInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   public RemoteActionInvocation(ActionException failure, RemoteClientInfo remoteClientInfo) {
/* 46 */     super(failure);
/* 47 */     this.remoteClientInfo = remoteClientInfo;
/*    */   }
/*    */   
/*    */   public RemoteClientInfo getRemoteClientInfo() {
/* 51 */     return this.remoteClientInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\RemoteActionInvocation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */