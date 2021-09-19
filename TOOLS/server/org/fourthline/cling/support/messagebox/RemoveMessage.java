/*    */ package org.fourthline.cling.support.messagebox;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.support.messagebox.model.Message;
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
/*    */ public abstract class RemoveMessage
/*    */   extends ActionCallback
/*    */ {
/*    */   public RemoveMessage(Service service, Message message) {
/* 31 */     this(service, message.getId());
/*    */   }
/*    */   
/*    */   public RemoveMessage(Service service, int id) {
/* 35 */     super(new ActionInvocation(service.getAction("RemoveMessage")));
/* 36 */     getActionInvocation().setInput("MessageID", Integer.valueOf(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\RemoveMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */