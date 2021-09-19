/*    */ package org.fourthline.cling.protocol.sync;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.gena.IncomingUnsubscribeRequestMessage;
/*    */ import org.fourthline.cling.model.meta.LocalService;
/*    */ import org.fourthline.cling.model.resource.ServiceEventSubscriptionResource;
/*    */ import org.fourthline.cling.protocol.ReceivingSync;
/*    */ import org.fourthline.cling.transport.RouterException;
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
/*    */ public class ReceivingUnsubscribe
/*    */   extends ReceivingSync<StreamRequestMessage, StreamResponseMessage>
/*    */ {
/* 37 */   private static final Logger log = Logger.getLogger(ReceivingUnsubscribe.class.getName());
/*    */   
/*    */   public ReceivingUnsubscribe(UpnpService upnpService, StreamRequestMessage inputMessage) {
/* 40 */     super(upnpService, inputMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected StreamResponseMessage executeSync() throws RouterException {
/* 46 */     ServiceEventSubscriptionResource resource = (ServiceEventSubscriptionResource)getUpnpService().getRegistry().getResource(ServiceEventSubscriptionResource.class, ((StreamRequestMessage)
/*    */         
/* 48 */         getInputMessage()).getUri());
/*    */ 
/*    */     
/* 51 */     if (resource == null) {
/* 52 */       log.fine("No local resource found: " + getInputMessage());
/* 53 */       return null;
/*    */     } 
/*    */     
/* 56 */     log.fine("Found local event subscription matching relative request URI: " + ((StreamRequestMessage)getInputMessage()).getUri());
/*    */ 
/*    */     
/* 59 */     IncomingUnsubscribeRequestMessage requestMessage = new IncomingUnsubscribeRequestMessage((StreamRequestMessage)getInputMessage(), (LocalService)resource.getModel());
/*    */ 
/*    */     
/* 62 */     if (requestMessage.getSubscriptionId() != null && (requestMessage
/* 63 */       .hasNotificationHeader() || requestMessage.hasCallbackHeader())) {
/* 64 */       log.fine("Subscription ID and NT or Callback in unsubcribe request: " + getInputMessage());
/* 65 */       return new StreamResponseMessage(UpnpResponse.Status.BAD_REQUEST);
/*    */     } 
/*    */ 
/*    */     
/* 69 */     LocalGENASubscription subscription = getUpnpService().getRegistry().getLocalSubscription(requestMessage.getSubscriptionId());
/*    */     
/* 71 */     if (subscription == null) {
/* 72 */       log.fine("Invalid subscription ID for unsubscribe request: " + getInputMessage());
/* 73 */       return new StreamResponseMessage(UpnpResponse.Status.PRECONDITION_FAILED);
/*    */     } 
/*    */     
/* 76 */     log.fine("Unregistering subscription: " + subscription);
/* 77 */     if (getUpnpService().getRegistry().removeLocalSubscription(subscription)) {
/* 78 */       subscription.end(null);
/*    */     } else {
/* 80 */       log.fine("Subscription was already removed from registry");
/*    */     } 
/*    */     
/* 83 */     return new StreamResponseMessage(UpnpResponse.Status.OK);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\ReceivingUnsubscribe.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */