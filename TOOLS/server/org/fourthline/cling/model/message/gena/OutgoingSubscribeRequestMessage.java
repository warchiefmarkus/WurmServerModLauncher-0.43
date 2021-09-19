/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.UpnpHeaders;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
/*    */ import org.fourthline.cling.model.message.header.CallbackHeader;
/*    */ import org.fourthline.cling.model.message.header.NTEventHeader;
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
/*    */ 
/*    */ 
/*    */ public class OutgoingSubscribeRequestMessage
/*    */   extends StreamRequestMessage
/*    */ {
/*    */   public OutgoingSubscribeRequestMessage(RemoteGENASubscription subscription, List<URL> callbackURLs, UpnpHeaders extraHeaders) {
/* 39 */     super(UpnpRequest.Method.SUBSCRIBE, subscription.getEventSubscriptionURL());
/*    */     
/* 41 */     getHeaders().add(UpnpHeader.Type.CALLBACK, (UpnpHeader)new CallbackHeader(callbackURLs));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     getHeaders().add(UpnpHeader.Type.NT, (UpnpHeader)new NTEventHeader());
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 51 */     getHeaders().add(UpnpHeader.Type.TIMEOUT, (UpnpHeader)new TimeoutHeader(subscription
/*    */           
/* 53 */           .getRequestedDurationSeconds()));
/*    */ 
/*    */     
/* 56 */     if (extraHeaders != null) {
/* 57 */       getHeaders().putAll((Map)extraHeaders);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean hasCallbackURLs() {
/* 62 */     CallbackHeader callbackHeader = (CallbackHeader)getHeaders().getFirstHeader(UpnpHeader.Type.CALLBACK, CallbackHeader.class);
/* 63 */     return (((List)callbackHeader.getValue()).size() > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\OutgoingSubscribeRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */