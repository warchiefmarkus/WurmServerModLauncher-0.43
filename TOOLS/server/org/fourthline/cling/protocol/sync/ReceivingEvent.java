/*     */ package org.fourthline.cling.protocol.sync;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.gena.IncomingEventRequestMessage;
/*     */ import org.fourthline.cling.model.message.gena.OutgoingEventResponseMessage;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.resource.ServiceEventCallbackResource;
/*     */ import org.fourthline.cling.protocol.ReceivingSync;
/*     */ import org.fourthline.cling.transport.RouterException;
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
/*     */ public class ReceivingEvent
/*     */   extends ReceivingSync<StreamRequestMessage, OutgoingEventResponseMessage>
/*     */ {
/*  45 */   private static final Logger log = Logger.getLogger(ReceivingEvent.class.getName());
/*     */   
/*     */   public ReceivingEvent(UpnpService upnpService, StreamRequestMessage inputMessage) {
/*  48 */     super(upnpService, inputMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutgoingEventResponseMessage executeSync() throws RouterException {
/*  53 */     if (!((StreamRequestMessage)getInputMessage()).isContentTypeTextUDA()) {
/*  54 */       log.warning("Received without or with invalid Content-Type: " + getInputMessage());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     ServiceEventCallbackResource resource = (ServiceEventCallbackResource)getUpnpService().getRegistry().getResource(ServiceEventCallbackResource.class, ((StreamRequestMessage)
/*     */         
/*  62 */         getInputMessage()).getUri());
/*     */ 
/*     */     
/*  65 */     if (resource == null) {
/*  66 */       log.fine("No local resource found: " + getInputMessage());
/*  67 */       return new OutgoingEventResponseMessage(new UpnpResponse(UpnpResponse.Status.NOT_FOUND));
/*     */     } 
/*     */ 
/*     */     
/*  71 */     final IncomingEventRequestMessage requestMessage = new IncomingEventRequestMessage((StreamRequestMessage)getInputMessage(), (RemoteService)resource.getModel());
/*     */ 
/*     */     
/*  74 */     if (requestMessage.getSubscrptionId() == null) {
/*  75 */       log.fine("Subscription ID missing in event request: " + getInputMessage());
/*  76 */       return new OutgoingEventResponseMessage(new UpnpResponse(UpnpResponse.Status.PRECONDITION_FAILED));
/*     */     } 
/*     */     
/*  79 */     if (!requestMessage.hasValidNotificationHeaders()) {
/*  80 */       log.fine("Missing NT and/or NTS headers in event request: " + getInputMessage());
/*  81 */       return new OutgoingEventResponseMessage(new UpnpResponse(UpnpResponse.Status.BAD_REQUEST));
/*     */     } 
/*     */     
/*  84 */     if (!requestMessage.hasValidNotificationHeaders()) {
/*  85 */       log.fine("Invalid NT and/or NTS headers in event request: " + getInputMessage());
/*  86 */       return new OutgoingEventResponseMessage(new UpnpResponse(UpnpResponse.Status.PRECONDITION_FAILED));
/*     */     } 
/*     */     
/*  89 */     if (requestMessage.getSequence() == null) {
/*  90 */       log.fine("Sequence missing in event request: " + getInputMessage());
/*  91 */       return new OutgoingEventResponseMessage(new UpnpResponse(UpnpResponse.Status.PRECONDITION_FAILED));
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  96 */       getUpnpService().getConfiguration().getGenaEventProcessor().readBody(requestMessage);
/*     */     }
/*  98 */     catch (UnsupportedDataException ex) {
/*  99 */       log.fine("Can't read event message request body, " + ex);
/*     */ 
/*     */ 
/*     */       
/* 103 */       final RemoteGENASubscription subscription = getUpnpService().getRegistry().getRemoteSubscription(requestMessage.getSubscrptionId());
/* 104 */       if (remoteGENASubscription != null) {
/* 105 */         getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 108 */                 subscription.invalidMessage(ex);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/* 114 */       return new OutgoingEventResponseMessage(new UpnpResponse(UpnpResponse.Status.INTERNAL_SERVER_ERROR));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     final RemoteGENASubscription subscription = getUpnpService().getRegistry().getWaitRemoteSubscription(requestMessage.getSubscrptionId());
/*     */     
/* 122 */     if (subscription == null) {
/* 123 */       log.severe("Invalid subscription ID, no active subscription: " + requestMessage);
/* 124 */       return new OutgoingEventResponseMessage(new UpnpResponse(UpnpResponse.Status.PRECONDITION_FAILED));
/*     */     } 
/*     */     
/* 127 */     getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 130 */             ReceivingEvent.log.fine("Calling active subscription with event state variable values");
/* 131 */             subscription.receive(requestMessage
/* 132 */                 .getSequence(), requestMessage
/* 133 */                 .getStateVariableValues());
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 139 */     return new OutgoingEventResponseMessage();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\ReceivingEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */