/*    */ package org.fourthline.cling.model.message.control;
/*    */ 
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.EXTHeader;
/*    */ import org.fourthline.cling.model.message.header.ServerHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.Action;
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
/*    */ public class OutgoingActionResponseMessage
/*    */   extends StreamResponseMessage
/*    */   implements ActionResponseMessage
/*    */ {
/*    */   private String actionNamespace;
/*    */   
/*    */   public OutgoingActionResponseMessage(Action action) {
/* 36 */     this(UpnpResponse.Status.OK, action);
/*    */   }
/*    */   
/*    */   public OutgoingActionResponseMessage(UpnpResponse.Status status) {
/* 40 */     this(status, null);
/*    */   }
/*    */   
/*    */   public OutgoingActionResponseMessage(UpnpResponse.Status status, Action action) {
/* 44 */     super(new UpnpResponse(status));
/*    */     
/* 46 */     if (action != null) {
/* 47 */       if (action instanceof org.fourthline.cling.model.meta.QueryStateVariableAction) {
/* 48 */         this.actionNamespace = "urn:schemas-upnp-org:control-1-0";
/*    */       } else {
/* 50 */         this.actionNamespace = action.getService().getServiceType().toString();
/*    */       } 
/*    */     }
/*    */     
/* 54 */     addHeaders();
/*    */   }
/*    */   
/*    */   protected void addHeaders() {
/* 58 */     getHeaders().add(UpnpHeader.Type.CONTENT_TYPE, (UpnpHeader)new ContentTypeHeader(ContentTypeHeader.DEFAULT_CONTENT_TYPE_UTF8));
/*    */ 
/*    */ 
/*    */     
/* 62 */     getHeaders().add(UpnpHeader.Type.SERVER, (UpnpHeader)new ServerHeader());
/*    */ 
/*    */ 
/*    */     
/* 66 */     getHeaders().add(UpnpHeader.Type.EXT, (UpnpHeader)new EXTHeader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getActionNamespace() {
/* 73 */     return this.actionNamespace;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\control\OutgoingActionResponseMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */