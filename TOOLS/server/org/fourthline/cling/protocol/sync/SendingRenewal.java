/*     */ package org.fourthline.cling.protocol.sync;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.gena.CancelReason;
/*     */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.gena.IncomingSubscribeResponseMessage;
/*     */ import org.fourthline.cling.model.message.gena.OutgoingRenewalRequestMessage;
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
/*     */ public class SendingRenewal
/*     */   extends SendingSync<OutgoingRenewalRequestMessage, IncomingSubscribeResponseMessage>
/*     */ {
/*  43 */   private static final Logger log = Logger.getLogger(SendingRenewal.class.getName());
/*     */   
/*     */   protected final RemoteGENASubscription subscription;
/*     */   
/*     */   public SendingRenewal(UpnpService upnpService, RemoteGENASubscription subscription) {
/*  48 */     super(upnpService, (StreamRequestMessage)new OutgoingRenewalRequestMessage(subscription, upnpService
/*     */ 
/*     */ 
/*     */           
/*  52 */           .getConfiguration().getEventSubscriptionHeaders((RemoteService)subscription.getService())));
/*     */ 
/*     */     
/*  55 */     this.subscription = subscription;
/*     */   }
/*     */   protected IncomingSubscribeResponseMessage executeSync() throws RouterException {
/*     */     StreamResponseMessage response;
/*  59 */     log.fine("Sending subscription renewal request: " + getInputMessage());
/*     */ 
/*     */     
/*     */     try {
/*  63 */       response = getUpnpService().getRouter().send(getInputMessage());
/*  64 */     } catch (RouterException ex) {
/*  65 */       onRenewalFailure();
/*  66 */       throw ex;
/*     */     } 
/*     */     
/*  69 */     if (response == null) {
/*  70 */       onRenewalFailure();
/*  71 */       return null;
/*     */     } 
/*     */     
/*  74 */     final IncomingSubscribeResponseMessage responseMessage = new IncomingSubscribeResponseMessage(response);
/*     */     
/*  76 */     if (((UpnpResponse)response.getOperation()).isFailed()) {
/*  77 */       log.fine("Subscription renewal failed, response was: " + response);
/*  78 */       getUpnpService().getRegistry().removeRemoteSubscription(this.subscription);
/*  79 */       getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/*  82 */               SendingRenewal.this.subscription.end(CancelReason.RENEWAL_FAILED, (UpnpResponse)responseMessage.getOperation());
/*     */             }
/*     */           });
/*     */     }
/*  86 */     else if (!responseMessage.isValidHeaders()) {
/*  87 */       log.severe("Subscription renewal failed, invalid or missing (SID, Timeout) response headers");
/*  88 */       getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/*  91 */               SendingRenewal.this.subscription.end(CancelReason.RENEWAL_FAILED, (UpnpResponse)responseMessage.getOperation());
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/*  96 */       log.fine("Subscription renewed, updating in registry, response was: " + response);
/*  97 */       this.subscription.setActualSubscriptionDurationSeconds(responseMessage.getSubscriptionDurationSeconds());
/*  98 */       getUpnpService().getRegistry().updateRemoteSubscription(this.subscription);
/*     */     } 
/*     */     
/* 101 */     return responseMessage;
/*     */   }
/*     */   
/*     */   protected void onRenewalFailure() {
/* 105 */     log.fine("Subscription renewal failed, removing subscription from registry");
/* 106 */     getUpnpService().getRegistry().removeRemoteSubscription(this.subscription);
/* 107 */     getUpnpService().getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 110 */             SendingRenewal.this.subscription.end(CancelReason.RENEWAL_FAILED, null);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\SendingRenewal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */