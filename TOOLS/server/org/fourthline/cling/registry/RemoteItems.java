/*     */ package org.fourthline.cling.registry;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.gena.CancelReason;
/*     */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.fourthline.cling.model.types.UDN;
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
/*     */ class RemoteItems
/*     */   extends RegistryItems<RemoteDevice, RemoteGENASubscription>
/*     */ {
/*  43 */   private static Logger log = Logger.getLogger(Registry.class.getName());
/*     */   
/*     */   RemoteItems(RegistryImpl registry) {
/*  46 */     super(registry);
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
/*     */   void add(final RemoteDevice device) {
/*  63 */     if (update((RemoteDeviceIdentity)device.getIdentity())) {
/*  64 */       log.fine("Ignoring addition, device already registered: " + device);
/*     */       
/*     */       return;
/*     */     } 
/*  68 */     Resource[] resources = getResources((Device)device);
/*     */     
/*  70 */     for (Resource deviceResource : resources) {
/*  71 */       log.fine("Validating remote device resource; " + deviceResource);
/*  72 */       if (this.registry.getResource(deviceResource.getPathQuery()) != null) {
/*  73 */         throw new RegistrationException("URI namespace conflict with already registered resource: " + deviceResource);
/*     */       }
/*     */     } 
/*     */     
/*  77 */     for (Resource validatedResource : resources) {
/*  78 */       this.registry.addResource(validatedResource);
/*  79 */       log.fine("Added remote device resource: " + validatedResource);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     RegistryItem<UDN, RemoteDevice> item = new RegistryItem<>(((RemoteDeviceIdentity)device.getIdentity()).getUdn(), device, ((this.registry.getConfiguration().getRemoteDeviceMaxAgeSeconds() != null) ? this.registry
/*  87 */         .getConfiguration().getRemoteDeviceMaxAgeSeconds() : ((RemoteDeviceIdentity)device
/*  88 */         .getIdentity()).getMaxAgeSeconds()).intValue());
/*     */     
/*  90 */     log.fine("Adding hydrated remote device to registry with " + item
/*  91 */         .getExpirationDetails().getMaxAgeSeconds() + " seconds expiration: " + device);
/*  92 */     getDeviceItems().add(item);
/*     */     
/*  94 */     if (log.isLoggable(Level.FINEST)) {
/*  95 */       StringBuilder sb = new StringBuilder();
/*  96 */       sb.append("\n");
/*  97 */       sb.append("-------------------------- START Registry Namespace -----------------------------------\n");
/*  98 */       for (Resource resource : this.registry.getResources()) {
/*  99 */         sb.append(resource).append("\n");
/*     */       }
/* 101 */       sb.append("-------------------------- END Registry Namespace -----------------------------------");
/* 102 */       log.finest(sb.toString());
/*     */     } 
/*     */ 
/*     */     
/* 106 */     log.fine("Completely hydrated remote device graph available, calling listeners: " + device);
/* 107 */     for (RegistryListener listener : this.registry.getListeners()) {
/* 108 */       this.registry.getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 111 */               listener.remoteDeviceAdded(RemoteItems.this.registry, device);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean update(RemoteDeviceIdentity rdIdentity) {
/* 121 */     for (LocalDevice localDevice : this.registry.getLocalDevices()) {
/* 122 */       if (localDevice.findDevice(rdIdentity.getUdn()) != null) {
/* 123 */         log.fine("Ignoring update, a local device graph contains UDN");
/* 124 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     RemoteDevice registeredRemoteDevice = get(rdIdentity.getUdn(), false);
/* 129 */     if (registeredRemoteDevice != null) {
/*     */       
/* 131 */       if (!registeredRemoteDevice.isRoot()) {
/* 132 */         log.fine("Updating root device of embedded: " + registeredRemoteDevice);
/* 133 */         registeredRemoteDevice = registeredRemoteDevice.getRoot();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 140 */       final RegistryItem<UDN, RemoteDevice> item = new RegistryItem<>(((RemoteDeviceIdentity)registeredRemoteDevice.getIdentity()).getUdn(), registeredRemoteDevice, ((this.registry.getConfiguration().getRemoteDeviceMaxAgeSeconds() != null) ? this.registry
/* 141 */           .getConfiguration().getRemoteDeviceMaxAgeSeconds() : rdIdentity
/* 142 */           .getMaxAgeSeconds()).intValue());
/*     */ 
/*     */       
/* 145 */       log.fine("Updating expiration of: " + registeredRemoteDevice);
/* 146 */       getDeviceItems().remove(item);
/* 147 */       getDeviceItems().add(item);
/*     */       
/* 149 */       log.fine("Remote device updated, calling listeners: " + registeredRemoteDevice);
/* 150 */       for (RegistryListener listener : this.registry.getListeners()) {
/* 151 */         this.registry.getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 154 */                 listener.remoteDeviceUpdated(RemoteItems.this.registry, (RemoteDevice)item.getItem());
/*     */               }
/*     */             });
/*     */       } 
/*     */ 
/*     */       
/* 160 */       return true;
/*     */     } 
/*     */     
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean remove(RemoteDevice remoteDevice) {
/* 173 */     return remove(remoteDevice, false);
/*     */   }
/*     */   
/*     */   boolean remove(RemoteDevice remoteDevice, boolean shuttingDown) throws RegistrationException {
/* 177 */     final RemoteDevice registeredDevice = get(((RemoteDeviceIdentity)remoteDevice.getIdentity()).getUdn(), true);
/* 178 */     if (registeredDevice != null) {
/*     */       
/* 180 */       log.fine("Removing remote device from registry: " + remoteDevice);
/*     */ 
/*     */       
/* 183 */       for (Resource deviceResource : getResources((Device)registeredDevice)) {
/* 184 */         if (this.registry.removeResource(deviceResource)) {
/* 185 */           log.fine("Unregistered resource: " + deviceResource);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 190 */       Iterator<RegistryItem<String, RemoteGENASubscription>> it = getSubscriptionItems().iterator();
/* 191 */       while (it.hasNext()) {
/* 192 */         final RegistryItem<String, RemoteGENASubscription> outgoingSubscription = it.next();
/*     */ 
/*     */         
/* 195 */         UDN subscriptionForUDN = ((RemoteDeviceIdentity)((RemoteDevice)((RemoteService)((RemoteGENASubscription)outgoingSubscription.getItem()).getService()).getDevice()).getIdentity()).getUdn();
/*     */         
/* 197 */         if (subscriptionForUDN.equals(((RemoteDeviceIdentity)registeredDevice.getIdentity()).getUdn())) {
/* 198 */           log.fine("Removing outgoing subscription: " + (String)outgoingSubscription.getKey());
/* 199 */           it.remove();
/* 200 */           if (!shuttingDown) {
/* 201 */             this.registry.getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */                 {
/*     */                   public void run() {
/* 204 */                     ((RemoteGENASubscription)outgoingSubscription.getItem()).end(CancelReason.DEVICE_WAS_REMOVED, null);
/*     */                   }
/*     */                 });
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 213 */       if (!shuttingDown) {
/* 214 */         for (RegistryListener listener : this.registry.getListeners()) {
/* 215 */           this.registry.getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */               {
/*     */                 public void run() {
/* 218 */                   listener.remoteDeviceRemoved(RemoteItems.this.registry, registeredDevice);
/*     */                 }
/*     */               });
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 226 */       getDeviceItems().remove(new RegistryItem<>(((RemoteDeviceIdentity)registeredDevice.getIdentity()).getUdn()));
/*     */       
/* 228 */       return true;
/*     */     } 
/*     */     
/* 231 */     return false;
/*     */   }
/*     */   
/*     */   void removeAll() {
/* 235 */     removeAll(false);
/*     */   }
/*     */   
/*     */   void removeAll(boolean shuttingDown) {
/* 239 */     RemoteDevice[] allDevices = get().<RemoteDevice>toArray(new RemoteDevice[get().size()]);
/* 240 */     for (RemoteDevice device : allDevices) {
/* 241 */       remove(device, shuttingDown);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void start() {}
/*     */ 
/*     */ 
/*     */   
/*     */   void maintain() {
/* 253 */     if (getDeviceItems().isEmpty()) {
/*     */       return;
/*     */     }
/* 256 */     Map<UDN, RemoteDevice> expiredRemoteDevices = new HashMap<>();
/* 257 */     for (RegistryItem<UDN, RemoteDevice> remoteItem : getDeviceItems()) {
/* 258 */       if (log.isLoggable(Level.FINEST))
/* 259 */         log.finest("Device '" + remoteItem.getItem() + "' expires in seconds: " + remoteItem
/* 260 */             .getExpirationDetails().getSecondsUntilExpiration()); 
/* 261 */       if (remoteItem.getExpirationDetails().hasExpired(false)) {
/* 262 */         expiredRemoteDevices.put(remoteItem.getKey(), remoteItem.getItem());
/*     */       }
/*     */     } 
/* 265 */     for (RemoteDevice remoteDevice : expiredRemoteDevices.values()) {
/* 266 */       if (log.isLoggable(Level.FINE))
/* 267 */         log.fine("Removing expired: " + remoteDevice); 
/* 268 */       remove(remoteDevice);
/*     */     } 
/*     */ 
/*     */     
/* 272 */     Set<RemoteGENASubscription> expiredOutgoingSubscriptions = new HashSet<>();
/* 273 */     for (RegistryItem<String, RemoteGENASubscription> item : getSubscriptionItems()) {
/* 274 */       if (item.getExpirationDetails().hasExpired(true)) {
/* 275 */         expiredOutgoingSubscriptions.add(item.getItem());
/*     */       }
/*     */     } 
/* 278 */     for (RemoteGENASubscription subscription : expiredOutgoingSubscriptions) {
/* 279 */       if (log.isLoggable(Level.FINEST))
/* 280 */         log.fine("Renewing outgoing subscription: " + subscription); 
/* 281 */       renewOutgoingSubscription(subscription);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resume() {
/* 286 */     log.fine("Updating remote device expiration timestamps on resume");
/* 287 */     List<RemoteDeviceIdentity> toUpdate = new ArrayList<>();
/* 288 */     for (RegistryItem<UDN, RemoteDevice> remoteItem : getDeviceItems()) {
/* 289 */       toUpdate.add(((RemoteDevice)remoteItem.getItem()).getIdentity());
/*     */     }
/* 291 */     for (RemoteDeviceIdentity identity : toUpdate) {
/* 292 */       update(identity);
/*     */     }
/*     */   }
/*     */   
/*     */   void shutdown() {
/* 297 */     log.fine("Cancelling all outgoing subscriptions to remote devices during shutdown");
/* 298 */     List<RemoteGENASubscription> remoteSubscriptions = new ArrayList<>();
/* 299 */     for (RegistryItem<String, RemoteGENASubscription> item : getSubscriptionItems()) {
/* 300 */       remoteSubscriptions.add(item.getItem());
/*     */     }
/* 302 */     for (RemoteGENASubscription remoteSubscription : remoteSubscriptions)
/*     */     {
/* 304 */       this.registry.getProtocolFactory()
/* 305 */         .createSendingUnsubscribe(remoteSubscription)
/* 306 */         .run();
/*     */     }
/*     */     
/* 309 */     log.fine("Removing all remote devices from registry during shutdown");
/* 310 */     removeAll(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renewOutgoingSubscription(RemoteGENASubscription subscription) {
/* 316 */     this.registry.executeAsyncProtocol((Runnable)this.registry
/* 317 */         .getProtocolFactory().createSendingRenewal(subscription));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\RemoteItems.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */