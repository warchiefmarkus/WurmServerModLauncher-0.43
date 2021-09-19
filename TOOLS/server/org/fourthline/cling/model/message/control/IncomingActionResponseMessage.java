/*    */ package org.fourthline.cling.model.message.control;
/*    */ 
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
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
/*    */ public class IncomingActionResponseMessage
/*    */   extends StreamResponseMessage
/*    */   implements ActionResponseMessage
/*    */ {
/*    */   public IncomingActionResponseMessage(StreamResponseMessage source) {
/* 28 */     super(source);
/*    */   }
/*    */   
/*    */   public IncomingActionResponseMessage(UpnpResponse operation) {
/* 32 */     super(operation);
/*    */   }
/*    */   
/*    */   public String getActionNamespace() {
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isFailedNonRecoverable() {
/* 40 */     int statusCode = ((UpnpResponse)getOperation()).getStatusCode();
/* 41 */     return (((UpnpResponse)getOperation()).isFailed() && statusCode != UpnpResponse.Status.METHOD_NOT_SUPPORTED
/* 42 */       .getStatusCode() && (statusCode != UpnpResponse.Status.INTERNAL_SERVER_ERROR
/* 43 */       .getStatusCode() || !hasBody()));
/*    */   }
/*    */   
/*    */   public boolean isFailedRecoverable() {
/* 47 */     return (hasBody() && ((UpnpResponse)getOperation()).getStatusCode() == UpnpResponse.Status.INTERNAL_SERVER_ERROR.getStatusCode());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\control\IncomingActionResponseMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */