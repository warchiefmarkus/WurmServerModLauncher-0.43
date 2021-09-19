/*    */ package org.fourthline.cling.support.messagebox;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.support.messagebox.model.Message;
/*    */ import org.seamless.util.MimeType;
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
/*    */ public abstract class AddMessage
/*    */   extends ActionCallback
/*    */ {
/* 29 */   protected final MimeType mimeType = MimeType.valueOf("text/xml;charset=\"utf-8\"");
/*    */   
/*    */   public AddMessage(Service service, Message message) {
/* 32 */     super(new ActionInvocation(service.getAction("AddMessage")));
/*    */     
/* 34 */     getActionInvocation().setInput("MessageID", Integer.toString(message.getId()));
/* 35 */     getActionInvocation().setInput("MessageType", this.mimeType.toString());
/* 36 */     getActionInvocation().setInput("Message", message.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\AddMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */