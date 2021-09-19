/*     */ package org.fourthline.cling.protocol.sync;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.NetworkAddress;
/*     */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.gena.IncomingSubscribeResponseMessage;
/*     */ import org.fourthline.cling.model.message.gena.OutgoingSubscribeRequestMessage;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.protocol.SendingSync;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SendingSubscribe
/*     */   extends SendingSync<OutgoingSubscribeRequestMessage, IncomingSubscribeResponseMessage>
/*     */ {
/*  49 */   private static final Logger log = Logger.getLogger(SendingSubscribe.class.getName());
/*     */ 
/*     */   
/*     */   protected final RemoteGENASubscription subscription;
/*     */ 
/*     */   
/*     */   public SendingSubscribe(UpnpService upnpService, RemoteGENASubscription subscription, List<NetworkAddress> activeStreamServers) {
/*  56 */     super(upnpService, (StreamRequestMessage)new OutgoingSubscribeRequestMessage(subscription, subscription
/*     */ 
/*     */ 
/*     */           
/*  60 */           .getEventCallbackURLs(activeStreamServers, upnpService
/*     */             
/*  62 */             .getConfiguration().getNamespace()), upnpService
/*     */           
/*  64 */           .getConfiguration().getEventSubscriptionHeaders((RemoteService)subscription.getService())));
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.subscription = subscription;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IncomingSubscribeResponseMessage executeSync() throws RouterException {
/*  73 */     if (!((OutgoingSubscribeRequestMessage)getInputMessage()).hasCallbackURLs()) {
/*  74 */       log.fine("Subscription failed, no active local callback URLs available (network disabled?)");
/*  75 */       getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/*  78 */               SendingSubscribe.this.subscription.fail(null);
/*     */             }
/*     */           });
/*     */       
/*  82 */       return null;
/*     */     } 
/*     */     
/*  85 */     log.fine("Sending subscription request: " + getInputMessage());
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  90 */       getUpnpService().getRegistry().registerPendingRemoteSubscription(this.subscription);
/*     */       
/*  92 */       StreamResponseMessage response = null;
/*     */       try {
/*  94 */         response = getUpnpService().getRouter().send(getInputMessage());
/*  95 */       } catch (RouterException ex) {
/*  96 */         onSubscriptionFailure();
/*  97 */         return null;
/*     */       } 
/*     */       
/* 100 */       if (response == null) {
/* 101 */         onSubscriptionFailure();
/* 102 */         return null;
/*     */       } 
/*     */       
/* 105 */       final IncomingSubscribeResponseMessage responseMessage = new IncomingSubscribeResponseMessage(response);
/*     */       
/* 107 */       if (((UpnpResponse)response.getOperation()).isFailed()) {
/* 108 */         log.fine("Subscription failed, response was: " + responseMessage);
/* 109 */         getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 112 */                 SendingSubscribe.this.subscription.fail((UpnpResponse)responseMessage.getOperation());
/*     */               }
/*     */             });
/*     */       }
/* 116 */       else if (!responseMessage.isValidHeaders()) {
/* 117 */         log.severe("Subscription failed, invalid or missing (SID, Timeout) response headers");
/* 118 */         getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 121 */                 SendingSubscribe.this.subscription.fail((UpnpResponse)responseMessage.getOperation());
/*     */               }
/*     */             });
/*     */       }
/*     */       else {
/*     */         
/* 127 */         log.fine("Subscription established, adding to registry, response was: " + response);
/* 128 */         this.subscription.setSubscriptionId(responseMessage.getSubscriptionId());
/* 129 */         this.subscription.setActualSubscriptionDurationSeconds(responseMessage.getSubscriptionDurationSeconds());
/*     */         
/* 131 */         getUpnpService().getRegistry().addRemoteSubscription(this.subscription);
/*     */         
/* 133 */         getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 136 */                 SendingSubscribe.this.subscription.establish();
/*     */               }
/*     */             });
/*     */       } 
/*     */ 
/*     */       
/* 142 */       return responseMessage;
/*     */     } finally {
/* 144 */       getUpnpService().getRegistry().unregisterPendingRemoteSubscription(this.subscription);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onSubscriptionFailure() {
/* 149 */     log.fine("Subscription failed");
/* 150 */     getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 153 */             SendingSubscribe.this.subscription.fail(null);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\SendingSubscribe.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */