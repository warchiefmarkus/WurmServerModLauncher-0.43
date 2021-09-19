/*    */ package org.fourthline.cling.protocol.sync;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.gena.GENASubscription;
/*    */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.gena.OutgoingEventRequestMessage;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*    */ 
/*    */ 
/*    */ public class SendingEvent
/*    */   extends SendingSync<OutgoingEventRequestMessage, StreamResponseMessage>
/*    */ {
/* 43 */   private static final Logger log = Logger.getLogger(SendingEvent.class.getName());
/*    */   
/*    */   protected final String subscriptionId;
/*    */   protected final OutgoingEventRequestMessage[] requestMessages;
/*    */   protected final UnsignedIntegerFourBytes currentSequence;
/*    */   
/*    */   public SendingEvent(UpnpService upnpService, LocalGENASubscription subscription) {
/* 50 */     super(upnpService, null);
/*    */ 
/*    */ 
/*    */     
/* 54 */     this.subscriptionId = subscription.getSubscriptionId();
/*    */     
/* 56 */     this.requestMessages = new OutgoingEventRequestMessage[subscription.getCallbackURLs().size()];
/* 57 */     int i = 0;
/* 58 */     for (URL url : subscription.getCallbackURLs()) {
/* 59 */       this.requestMessages[i] = new OutgoingEventRequestMessage((GENASubscription)subscription, url);
/* 60 */       getUpnpService().getConfiguration().getGenaEventProcessor().writeBody(this.requestMessages[i]);
/* 61 */       i++;
/*    */     } 
/*    */     
/* 64 */     this.currentSequence = subscription.getCurrentSequence();
/*    */ 
/*    */ 
/*    */     
/* 68 */     subscription.incrementSequence();
/*    */   }
/*    */ 
/*    */   
/*    */   protected StreamResponseMessage executeSync() throws RouterException {
/* 73 */     log.fine("Sending event for subscription: " + this.subscriptionId);
/*    */     
/* 75 */     StreamResponseMessage lastResponse = null;
/*    */     
/* 77 */     for (OutgoingEventRequestMessage requestMessage : this.requestMessages) {
/*    */       
/* 79 */       if (this.currentSequence.getValue().longValue() == 0L) {
/* 80 */         log.fine("Sending initial event message to callback URL: " + requestMessage.getUri());
/*    */       } else {
/* 82 */         log.fine("Sending event message '" + this.currentSequence + "' to callback URL: " + requestMessage.getUri());
/*    */       } 
/*    */ 
/*    */ 
/*    */       
/* 87 */       lastResponse = getUpnpService().getRouter().send((StreamRequestMessage)requestMessage);
/* 88 */       log.fine("Received event callback response: " + lastResponse);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 94 */     return lastResponse;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\SendingEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */