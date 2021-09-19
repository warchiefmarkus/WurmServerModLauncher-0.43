/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.UpnpHeaders;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
/*    */ import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
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
/*    */ public class OutgoingUnsubscribeRequestMessage
/*    */   extends StreamRequestMessage
/*    */ {
/*    */   public OutgoingUnsubscribeRequestMessage(RemoteGENASubscription subscription, UpnpHeaders extraHeaders) {
/* 33 */     super(UpnpRequest.Method.UNSUBSCRIBE, subscription.getEventSubscriptionURL());
/*    */     
/* 35 */     getHeaders().add(UpnpHeader.Type.SID, (UpnpHeader)new SubscriptionIdHeader(subscription
/*    */           
/* 37 */           .getSubscriptionId()));
/*    */ 
/*    */     
/* 40 */     if (extraHeaders != null)
/* 41 */       getHeaders().putAll((Map)extraHeaders); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\OutgoingUnsubscribeRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */