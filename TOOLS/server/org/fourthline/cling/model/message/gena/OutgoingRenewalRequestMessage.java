/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.UpnpHeaders;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
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
/*    */ public class OutgoingRenewalRequestMessage
/*    */   extends StreamRequestMessage
/*    */ {
/*    */   public OutgoingRenewalRequestMessage(RemoteGENASubscription subscription, UpnpHeaders extraHeaders) {
/* 34 */     super(UpnpRequest.Method.SUBSCRIBE, subscription.getEventSubscriptionURL());
/*    */     
/* 36 */     getHeaders().add(UpnpHeader.Type.SID, (UpnpHeader)new SubscriptionIdHeader(subscription
/*    */           
/* 38 */           .getSubscriptionId()));
/*    */ 
/*    */     
/* 41 */     getHeaders().add(UpnpHeader.Type.TIMEOUT, (UpnpHeader)new TimeoutHeader(subscription
/*    */           
/* 43 */           .getRequestedDurationSeconds()));
/*    */ 
/*    */     
/* 46 */     if (extraHeaders != null)
/* 47 */       getHeaders().putAll((Map)extraHeaders); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\OutgoingRenewalRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */