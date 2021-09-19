/*    */ package org.fourthline.cling.protocol.sync;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.gena.CancelReason;
/*    */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.gena.OutgoingUnsubscribeRequestMessage;
/*    */ import org.fourthline.cling.model.meta.RemoteService;
/*    */ import org.fourthline.cling.protocol.SendingSync;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SendingUnsubscribe
/*    */   extends SendingSync<OutgoingUnsubscribeRequestMessage, StreamResponseMessage>
/*    */ {
/* 41 */   private static final Logger log = Logger.getLogger(SendingUnsubscribe.class.getName());
/*    */   
/*    */   protected final RemoteGENASubscription subscription;
/*    */   
/*    */   public SendingUnsubscribe(UpnpService upnpService, RemoteGENASubscription subscription) {
/* 46 */     super(upnpService, (StreamRequestMessage)new OutgoingUnsubscribeRequestMessage(subscription, upnpService
/*    */ 
/*    */ 
/*    */           
/* 50 */           .getConfiguration().getEventSubscriptionHeaders((RemoteService)subscription.getService())));
/*    */ 
/*    */     
/* 53 */     this.subscription = subscription;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StreamResponseMessage executeSync() throws RouterException {
/* 58 */     log.fine("Sending unsubscribe request: " + getInputMessage());
/*    */     
/* 60 */     StreamResponseMessage response = null;
/*    */     try {
/* 62 */       response = getUpnpService().getRouter().send(getInputMessage());
/* 63 */       return response;
/*    */     } finally {
/* 65 */       onUnsubscribe(response);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onUnsubscribe(final StreamResponseMessage response) {
/* 71 */     getUpnpService().getRegistry().removeRemoteSubscription(this.subscription);
/*    */     
/* 73 */     getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*    */         {
/*    */           public void run() {
/* 76 */             if (response == null) {
/* 77 */               SendingUnsubscribe.log.fine("Unsubscribe failed, no response received");
/* 78 */               SendingUnsubscribe.this.subscription.end(CancelReason.UNSUBSCRIBE_FAILED, null);
/* 79 */             } else if (((UpnpResponse)response.getOperation()).isFailed()) {
/* 80 */               SendingUnsubscribe.log.fine("Unsubscribe failed, response was: " + response);
/* 81 */               SendingUnsubscribe.this.subscription.end(CancelReason.UNSUBSCRIBE_FAILED, (UpnpResponse)response.getOperation());
/*    */             } else {
/* 83 */               SendingUnsubscribe.log.fine("Unsubscribe successful, response was: " + response);
/* 84 */               SendingUnsubscribe.this.subscription.end(null, (UpnpResponse)response.getOperation());
/*    */             } 
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\SendingUnsubscribe.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */