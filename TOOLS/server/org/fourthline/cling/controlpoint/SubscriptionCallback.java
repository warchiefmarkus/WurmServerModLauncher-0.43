/*     */ package org.fourthline.cling.controlpoint;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.gena.CancelReason;
/*     */ import org.fourthline.cling.model.gena.GENASubscription;
/*     */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*     */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.protocol.ProtocolCreationException;
/*     */ import org.fourthline.cling.protocol.sync.SendingSubscribe;
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
/*     */ public abstract class SubscriptionCallback
/*     */   implements Runnable
/*     */ {
/*  77 */   protected static Logger log = Logger.getLogger(SubscriptionCallback.class.getName());
/*     */   
/*     */   protected final Service service;
/*     */   
/*     */   protected final Integer requestedDurationSeconds;
/*     */   private ControlPoint controlPoint;
/*     */   private GENASubscription subscription;
/*     */   
/*     */   protected SubscriptionCallback(Service service) {
/*  86 */     this.service = service;
/*  87 */     this.requestedDurationSeconds = Integer.valueOf(1800);
/*     */   }
/*     */   
/*     */   protected SubscriptionCallback(Service service, int requestedDurationSeconds) {
/*  91 */     this.service = service;
/*  92 */     this.requestedDurationSeconds = Integer.valueOf(requestedDurationSeconds);
/*     */   }
/*     */   
/*     */   public Service getService() {
/*  96 */     return this.service;
/*     */   }
/*     */   
/*     */   public synchronized ControlPoint getControlPoint() {
/* 100 */     return this.controlPoint;
/*     */   }
/*     */   
/*     */   public synchronized void setControlPoint(ControlPoint controlPoint) {
/* 104 */     this.controlPoint = controlPoint;
/*     */   }
/*     */   
/*     */   public synchronized GENASubscription getSubscription() {
/* 108 */     return this.subscription;
/*     */   }
/*     */   
/*     */   public synchronized void setSubscription(GENASubscription subscription) {
/* 112 */     this.subscription = subscription;
/*     */   }
/*     */   
/*     */   public synchronized void run() {
/* 116 */     if (getControlPoint() == null) {
/* 117 */       throw new IllegalStateException("Callback must be executed through ControlPoint");
/*     */     }
/*     */     
/* 120 */     if (getService() instanceof LocalService) {
/* 121 */       establishLocalSubscription((LocalService)this.service);
/* 122 */     } else if (getService() instanceof RemoteService) {
/* 123 */       establishRemoteSubscription((RemoteService)this.service);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void establishLocalSubscription(LocalService service) {
/* 129 */     if (getControlPoint().getRegistry().getLocalDevice(service.getDevice().getIdentity().getUdn(), false) == null) {
/* 130 */       log.fine("Local device service is currently not registered, failing subscription immediately");
/* 131 */       failed(null, null, new IllegalStateException("Local device is not registered"));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     LocalGENASubscription localSubscription = null;
/*     */     
/*     */     try {
/* 142 */       localSubscription = new LocalGENASubscription(service, Integer.valueOf(2147483647), Collections.EMPTY_LIST)
/*     */         {
/*     */           public void failed(Exception ex) {
/* 145 */             synchronized (SubscriptionCallback.this) {
/* 146 */               SubscriptionCallback.this.setSubscription(null);
/* 147 */               SubscriptionCallback.this.failed(null, null, ex);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void established() {
/* 152 */             synchronized (SubscriptionCallback.this) {
/* 153 */               SubscriptionCallback.this.setSubscription((GENASubscription)this);
/* 154 */               SubscriptionCallback.this.established((GENASubscription)this);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void ended(CancelReason reason) {
/* 159 */             synchronized (SubscriptionCallback.this) {
/* 160 */               SubscriptionCallback.this.setSubscription(null);
/* 161 */               SubscriptionCallback.this.ended((GENASubscription)this, reason, null);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void eventReceived() {
/* 166 */             synchronized (SubscriptionCallback.this) {
/* 167 */               SubscriptionCallback.log.fine("Local service state updated, notifying callback, sequence is: " + getCurrentSequence());
/* 168 */               SubscriptionCallback.this.eventReceived((GENASubscription)this);
/* 169 */               incrementSequence();
/*     */             } 
/*     */           }
/*     */         };
/*     */       
/* 174 */       log.fine("Local device service is currently registered, also registering subscription");
/* 175 */       getControlPoint().getRegistry().addLocalSubscription(localSubscription);
/*     */       
/* 177 */       log.fine("Notifying subscription callback of local subscription availablity");
/* 178 */       localSubscription.establish();
/*     */       
/* 180 */       log.fine("Simulating first initial event for local subscription callback, sequence: " + localSubscription.getCurrentSequence());
/* 181 */       eventReceived((GENASubscription)localSubscription);
/* 182 */       localSubscription.incrementSequence();
/*     */       
/* 184 */       log.fine("Starting to monitor state changes of local service");
/* 185 */       localSubscription.registerOnService();
/*     */     }
/* 187 */     catch (Exception ex) {
/* 188 */       log.fine("Local callback creation failed: " + ex.toString());
/* 189 */       log.log(Level.FINE, "Exception root cause: ", Exceptions.unwrap(ex));
/* 190 */       if (localSubscription != null)
/* 191 */         getControlPoint().getRegistry().removeLocalSubscription(localSubscription); 
/* 192 */       failed((GENASubscription)localSubscription, null, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void establishRemoteSubscription(RemoteService service) {
/*     */     SendingSubscribe protocol;
/* 198 */     RemoteGENASubscription remoteSubscription = new RemoteGENASubscription(service, this.requestedDurationSeconds.intValue())
/*     */       {
/*     */         public void failed(UpnpResponse responseStatus) {
/* 201 */           synchronized (SubscriptionCallback.this) {
/* 202 */             SubscriptionCallback.this.setSubscription(null);
/* 203 */             SubscriptionCallback.this.failed((GENASubscription)this, responseStatus, null);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void established() {
/* 208 */           synchronized (SubscriptionCallback.this) {
/* 209 */             SubscriptionCallback.this.setSubscription((GENASubscription)this);
/* 210 */             SubscriptionCallback.this.established((GENASubscription)this);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void ended(CancelReason reason, UpnpResponse responseStatus) {
/* 215 */           synchronized (SubscriptionCallback.this) {
/* 216 */             SubscriptionCallback.this.setSubscription(null);
/* 217 */             SubscriptionCallback.this.ended((GENASubscription)this, reason, responseStatus);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void eventReceived() {
/* 222 */           synchronized (SubscriptionCallback.this) {
/* 223 */             SubscriptionCallback.this.eventReceived((GENASubscription)this);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void eventsMissed(int numberOfMissedEvents) {
/* 228 */           synchronized (SubscriptionCallback.this) {
/* 229 */             SubscriptionCallback.this.eventsMissed((GENASubscription)this, numberOfMissedEvents);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void invalidMessage(UnsupportedDataException ex) {
/* 234 */           synchronized (SubscriptionCallback.this) {
/* 235 */             SubscriptionCallback.this.invalidMessage(this, ex);
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */     
/*     */     try {
/* 242 */       protocol = getControlPoint().getProtocolFactory().createSendingSubscribe(remoteSubscription);
/* 243 */     } catch (ProtocolCreationException ex) {
/* 244 */       failed(this.subscription, null, (Exception)ex);
/*     */       return;
/*     */     } 
/* 247 */     protocol.run();
/*     */   }
/*     */   
/*     */   public synchronized void end() {
/* 251 */     if (this.subscription == null)
/* 252 */       return;  if (this.subscription instanceof LocalGENASubscription) {
/* 253 */       endLocalSubscription((LocalGENASubscription)this.subscription);
/* 254 */     } else if (this.subscription instanceof RemoteGENASubscription) {
/* 255 */       endRemoteSubscription((RemoteGENASubscription)this.subscription);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void endLocalSubscription(LocalGENASubscription subscription) {
/* 260 */     log.fine("Removing local subscription and ending it in callback: " + subscription);
/* 261 */     getControlPoint().getRegistry().removeLocalSubscription(subscription);
/* 262 */     subscription.end(null);
/*     */   }
/*     */   
/*     */   private void endRemoteSubscription(RemoteGENASubscription subscription) {
/* 266 */     log.fine("Ending remote subscription: " + subscription);
/* 267 */     getControlPoint().getConfiguration().getSyncProtocolExecutorService().execute((Runnable)
/* 268 */         getControlPoint().getProtocolFactory().createSendingUnsubscribe(subscription));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception) {
/* 273 */     failed(subscription, responseStatus, exception, createDefaultFailureMessage(responseStatus, exception));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void failed(GENASubscription paramGENASubscription, UpnpResponse paramUpnpResponse, Exception paramException, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void established(GENASubscription paramGENASubscription);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void ended(GENASubscription paramGENASubscription, CancelReason paramCancelReason, UpnpResponse paramUpnpResponse);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void eventReceived(GENASubscription paramGENASubscription);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void eventsMissed(GENASubscription paramGENASubscription, int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createDefaultFailureMessage(UpnpResponse responseStatus, Exception exception) {
/* 333 */     String message = "Subscription failed: ";
/* 334 */     if (responseStatus != null) {
/* 335 */       message = message + " HTTP response was: " + responseStatus.getResponseDetails();
/* 336 */     } else if (exception != null) {
/* 337 */       message = message + " Exception occured: " + exception;
/*     */     } else {
/* 339 */       message = message + " No response received.";
/*     */     } 
/* 341 */     return message;
/*     */   }
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
/*     */   protected void invalidMessage(RemoteGENASubscription remoteGENASubscription, UnsupportedDataException ex) {
/* 361 */     log.info("Invalid event message received, causing: " + ex);
/* 362 */     if (log.isLoggable(Level.FINE)) {
/* 363 */       log.fine("------------------------------------------------------------------------------");
/* 364 */       log.fine((ex.getData() != null) ? ex.getData().toString() : "null");
/* 365 */       log.fine("------------------------------------------------------------------------------");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 371 */     return "(SubscriptionCallback) " + getService();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\controlpoint\SubscriptionCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */