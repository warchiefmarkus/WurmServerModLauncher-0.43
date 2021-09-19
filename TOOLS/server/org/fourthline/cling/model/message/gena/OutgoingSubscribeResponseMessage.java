/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.header.ServerHeader;
/*    */ import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
/*    */ import org.fourthline.cling.model.message.header.TimeoutHeader;
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
/*    */ 
/*    */ public class OutgoingSubscribeResponseMessage
/*    */   extends StreamResponseMessage
/*    */ {
/*    */   public OutgoingSubscribeResponseMessage(UpnpResponse.Status status) {
/* 33 */     super(status);
/*    */   }
/*    */   
/*    */   public OutgoingSubscribeResponseMessage(LocalGENASubscription subscription) {
/* 37 */     super(new UpnpResponse(UpnpResponse.Status.OK));
/*    */     
/* 39 */     getHeaders().add(UpnpHeader.Type.SERVER, (UpnpHeader)new ServerHeader());
/* 40 */     getHeaders().add(UpnpHeader.Type.SID, (UpnpHeader)new SubscriptionIdHeader(subscription.getSubscriptionId()));
/* 41 */     getHeaders().add(UpnpHeader.Type.TIMEOUT, (UpnpHeader)new TimeoutHeader(subscription.getActualDurationSeconds()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\OutgoingSubscribeResponseMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */