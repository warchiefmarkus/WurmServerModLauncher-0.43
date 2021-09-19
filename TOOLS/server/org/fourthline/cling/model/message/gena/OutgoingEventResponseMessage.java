/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
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
/*    */ public class OutgoingEventResponseMessage
/*    */   extends StreamResponseMessage
/*    */ {
/*    */   public OutgoingEventResponseMessage() {
/* 29 */     super(new UpnpResponse(UpnpResponse.Status.OK));
/* 30 */     getHeaders().add(UpnpHeader.Type.CONTENT_TYPE, (UpnpHeader)new ContentTypeHeader());
/*    */   }
/*    */   
/*    */   public OutgoingEventResponseMessage(UpnpResponse operation) {
/* 34 */     super(operation);
/* 35 */     getHeaders().add(UpnpHeader.Type.CONTENT_TYPE, (UpnpHeader)new ContentTypeHeader());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\OutgoingEventResponseMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */