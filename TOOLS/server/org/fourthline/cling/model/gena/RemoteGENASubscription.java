/*     */ package org.fourthline.cling.model.gena;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.model.Location;
/*     */ import org.fourthline.cling.model.Namespace;
/*     */ import org.fourthline.cling.model.NetworkAddress;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.state.StateVariableValue;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*     */ public abstract class RemoteGENASubscription
/*     */   extends GENASubscription<RemoteService>
/*     */ {
/*  44 */   protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
/*     */ 
/*     */   
/*     */   protected RemoteGENASubscription(RemoteService service, int requestedDurationSeconds) {
/*  48 */     super(service, requestedDurationSeconds);
/*     */   }
/*     */   
/*     */   public synchronized URL getEventSubscriptionURL() {
/*  52 */     return ((RemoteDevice)getService().getDevice()).normalizeURI(
/*  53 */         getService().getEventSubscriptionURI());
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized List<URL> getEventCallbackURLs(List<NetworkAddress> activeStreamServers, Namespace namespace) {
/*  58 */     List<URL> callbackURLs = new ArrayList<>();
/*  59 */     for (NetworkAddress activeStreamServer : activeStreamServers) {
/*  60 */       callbackURLs.add((new Location(activeStreamServer, namespace
/*     */ 
/*     */             
/*  63 */             .getEventCallbackPathString((Service)getService())))
/*  64 */           .getURL());
/*     */     }
/*  66 */     return callbackURLs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void establish() {
/*  75 */     established();
/*     */   }
/*     */   
/*     */   public synchronized void fail(UpnpResponse responseStatus) {
/*  79 */     failed(responseStatus);
/*     */   }
/*     */   
/*     */   public synchronized void end(CancelReason reason, UpnpResponse response) {
/*  83 */     ended(reason, response);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void receive(UnsignedIntegerFourBytes sequence, Collection<StateVariableValue> newValues) {
/*  88 */     if (this.currentSequence != null) {
/*     */ 
/*     */       
/*  91 */       if (this.currentSequence.getValue().equals(Long.valueOf(this.currentSequence.getBits().getMaxValue())) && sequence.getValue().longValue() == 1L) {
/*  92 */         System.err.println("TODO: HANDLE ROLLOVER");
/*     */         
/*     */         return;
/*     */       } 
/*  96 */       if (this.currentSequence.getValue().longValue() >= sequence.getValue().longValue()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 101 */       long expectedValue = this.currentSequence.getValue().longValue() + 1L; int difference;
/* 102 */       if ((difference = (int)(sequence.getValue().longValue() - expectedValue)) != 0) {
/* 103 */         eventsMissed(difference);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 108 */     this.currentSequence = sequence;
/*     */     
/* 110 */     for (StateVariableValue<RemoteService> newValue : newValues) {
/* 111 */       this.currentValues.put(newValue.getStateVariable().getName(), newValue);
/*     */     }
/*     */     
/* 114 */     eventReceived();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void invalidMessage(UnsupportedDataException paramUnsupportedDataException);
/*     */   
/*     */   public abstract void failed(UpnpResponse paramUpnpResponse);
/*     */   
/*     */   public abstract void ended(CancelReason paramCancelReason, UpnpResponse paramUpnpResponse);
/*     */   
/*     */   public abstract void eventsMissed(int paramInt);
/*     */   
/*     */   public String toString() {
/* 127 */     return "(SID: " + getSubscriptionId() + ") " + getService();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\gena\RemoteGENASubscription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */