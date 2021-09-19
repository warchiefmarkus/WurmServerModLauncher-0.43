/*     */ package org.fourthline.cling.protocol.sync;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.gena.CancelReason;
/*     */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.gena.IncomingSubscribeRequestMessage;
/*     */ import org.fourthline.cling.model.message.gena.OutgoingSubscribeResponseMessage;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.resource.ServiceEventSubscriptionResource;
/*     */ import org.fourthline.cling.protocol.ReceivingSync;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ import org.seamless.util.Exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReceivingSubscribe
/*     */   extends ReceivingSync<StreamRequestMessage, OutgoingSubscribeResponseMessage>
/*     */ {
/*  58 */   private static final Logger log = Logger.getLogger(ReceivingSubscribe.class.getName());
/*     */   
/*     */   protected LocalGENASubscription subscription;
/*     */   
/*     */   public ReceivingSubscribe(UpnpService upnpService, StreamRequestMessage inputMessage) {
/*  63 */     super(upnpService, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutgoingSubscribeResponseMessage executeSync() throws RouterException {
/*  69 */     ServiceEventSubscriptionResource resource = (ServiceEventSubscriptionResource)getUpnpService().getRegistry().getResource(ServiceEventSubscriptionResource.class, ((StreamRequestMessage)
/*     */         
/*  71 */         getInputMessage()).getUri());
/*     */ 
/*     */     
/*  74 */     if (resource == null) {
/*  75 */       log.fine("No local resource found: " + getInputMessage());
/*  76 */       return null;
/*     */     } 
/*     */     
/*  79 */     log.fine("Found local event subscription matching relative request URI: " + ((StreamRequestMessage)getInputMessage()).getUri());
/*     */ 
/*     */     
/*  82 */     IncomingSubscribeRequestMessage requestMessage = new IncomingSubscribeRequestMessage((StreamRequestMessage)getInputMessage(), (LocalService)resource.getModel());
/*     */ 
/*     */     
/*  85 */     if (requestMessage.getSubscriptionId() != null && (requestMessage
/*  86 */       .hasNotificationHeader() || requestMessage.getCallbackURLs() != null)) {
/*  87 */       log.fine("Subscription ID and NT or Callback in subscribe request: " + getInputMessage());
/*  88 */       return new OutgoingSubscribeResponseMessage(UpnpResponse.Status.BAD_REQUEST);
/*     */     } 
/*     */     
/*  91 */     if (requestMessage.getSubscriptionId() != null)
/*  92 */       return processRenewal((LocalService)resource.getModel(), requestMessage); 
/*  93 */     if (requestMessage.hasNotificationHeader() && requestMessage.getCallbackURLs() != null) {
/*  94 */       return processNewSubscription((LocalService)resource.getModel(), requestMessage);
/*     */     }
/*  96 */     log.fine("No subscription ID, no NT or Callback, neither subscription or renewal: " + getInputMessage());
/*  97 */     return new OutgoingSubscribeResponseMessage(UpnpResponse.Status.PRECONDITION_FAILED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutgoingSubscribeResponseMessage processRenewal(LocalService service, IncomingSubscribeRequestMessage requestMessage) {
/* 105 */     this.subscription = getUpnpService().getRegistry().getLocalSubscription(requestMessage.getSubscriptionId());
/*     */ 
/*     */     
/* 108 */     if (this.subscription == null) {
/* 109 */       log.fine("Invalid subscription ID for renewal request: " + getInputMessage());
/* 110 */       return new OutgoingSubscribeResponseMessage(UpnpResponse.Status.PRECONDITION_FAILED);
/*     */     } 
/*     */     
/* 113 */     log.fine("Renewing subscription: " + this.subscription);
/* 114 */     this.subscription.setSubscriptionDuration(requestMessage.getRequestedTimeoutSeconds());
/* 115 */     if (getUpnpService().getRegistry().updateLocalSubscription(this.subscription)) {
/* 116 */       return new OutgoingSubscribeResponseMessage(this.subscription);
/*     */     }
/* 118 */     log.fine("Subscription went away before it could be renewed: " + getInputMessage());
/* 119 */     return new OutgoingSubscribeResponseMessage(UpnpResponse.Status.PRECONDITION_FAILED);
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutgoingSubscribeResponseMessage processNewSubscription(LocalService service, IncomingSubscribeRequestMessage requestMessage) {
/*     */     Integer timeoutSeconds;
/* 125 */     List<URL> callbackURLs = requestMessage.getCallbackURLs();
/*     */ 
/*     */     
/* 128 */     if (callbackURLs == null || callbackURLs.size() == 0) {
/* 129 */       log.fine("Missing or invalid Callback URLs in subscribe request: " + getInputMessage());
/* 130 */       return new OutgoingSubscribeResponseMessage(UpnpResponse.Status.PRECONDITION_FAILED);
/*     */     } 
/*     */     
/* 133 */     if (!requestMessage.hasNotificationHeader()) {
/* 134 */       log.fine("Missing or invalid NT header in subscribe request: " + getInputMessage());
/* 135 */       return new OutgoingSubscribeResponseMessage(UpnpResponse.Status.PRECONDITION_FAILED);
/*     */     } 
/*     */ 
/*     */     
/* 139 */     if (getUpnpService().getConfiguration().isReceivedSubscriptionTimeoutIgnored()) {
/* 140 */       timeoutSeconds = null;
/*     */     } else {
/* 142 */       timeoutSeconds = requestMessage.getRequestedTimeoutSeconds();
/*     */     } 
/*     */     
/*     */     try {
/* 146 */       this.subscription = new LocalGENASubscription(service, timeoutSeconds, callbackURLs)
/*     */         {
/*     */           public void established() {}
/*     */ 
/*     */           
/*     */           public void ended(CancelReason reason) {}
/*     */ 
/*     */           
/*     */           public void eventReceived() {
/* 155 */             ReceivingSubscribe.this.getUpnpService().getConfiguration().getSyncProtocolExecutorService().execute((Runnable)ReceivingSubscribe.this
/* 156 */                 .getUpnpService().getProtocolFactory().createSendingEvent(this));
/*     */           }
/*     */         };
/*     */     }
/* 160 */     catch (Exception ex) {
/* 161 */       log.warning("Couldn't create local subscription to service: " + Exceptions.unwrap(ex));
/* 162 */       return new OutgoingSubscribeResponseMessage(UpnpResponse.Status.INTERNAL_SERVER_ERROR);
/*     */     } 
/*     */     
/* 165 */     log.fine("Adding subscription to registry: " + this.subscription);
/* 166 */     getUpnpService().getRegistry().addLocalSubscription(this.subscription);
/*     */     
/* 168 */     log.fine("Returning subscription response, waiting to send initial event");
/* 169 */     return new OutgoingSubscribeResponseMessage(this.subscription);
/*     */   }
/*     */ 
/*     */   
/*     */   public void responseSent(StreamResponseMessage responseMessage) {
/* 174 */     if (this.subscription == null)
/* 175 */       return;  if (responseMessage != null && 
/* 176 */       !((UpnpResponse)responseMessage.getOperation()).isFailed() && this.subscription
/* 177 */       .getCurrentSequence().getValue().longValue() == 0L) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 184 */       log.fine("Establishing subscription");
/* 185 */       this.subscription.registerOnService();
/* 186 */       this.subscription.establish();
/*     */       
/* 188 */       log.fine("Response to subscription sent successfully, now sending initial event asynchronously");
/* 189 */       getUpnpService().getConfiguration().getAsyncProtocolExecutor().execute((Runnable)
/* 190 */           getUpnpService().getProtocolFactory().createSendingEvent(this.subscription));
/*     */     
/*     */     }
/* 193 */     else if (this.subscription.getCurrentSequence().getValue().longValue() == 0L) {
/* 194 */       log.fine("Subscription request's response aborted, not sending initial event");
/* 195 */       if (responseMessage == null) {
/* 196 */         log.fine("Reason: No response at all from subscriber");
/*     */       } else {
/* 198 */         log.fine("Reason: " + responseMessage.getOperation());
/*     */       } 
/* 200 */       log.fine("Removing subscription from registry: " + this.subscription);
/* 201 */       getUpnpService().getRegistry().removeLocalSubscription(this.subscription);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void responseException(Throwable t) {
/* 207 */     if (this.subscription == null)
/* 208 */       return;  log.fine("Response could not be send to subscriber, removing local GENA subscription: " + this.subscription);
/* 209 */     getUpnpService().getRegistry().removeLocalSubscription(this.subscription);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\ReceivingSubscribe.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */