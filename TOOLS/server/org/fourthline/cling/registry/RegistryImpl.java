/*     */ package org.fourthline.cling.registry;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.context.ApplicationScoped;
/*     */ import javax.inject.Inject;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.UpnpServiceConfiguration;
/*     */ import org.fourthline.cling.model.DiscoveryOptions;
/*     */ import org.fourthline.cling.model.ServiceReference;
/*     */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*     */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.fourthline.cling.model.types.DeviceType;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
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
/*     */ @ApplicationScoped
/*     */ public class RegistryImpl
/*     */   implements Registry
/*     */ {
/*  57 */   private static Logger log = Logger.getLogger(Registry.class.getName());
/*     */   
/*     */   protected UpnpService upnpService;
/*     */   protected RegistryMaintainer registryMaintainer;
/*  61 */   protected final Set<RemoteGENASubscription> pendingSubscriptionsLock = new HashSet<>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Set<RegistryListener> registryListeners;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Set<RegistryItem<URI, Resource>> resourceItems;
/*     */ 
/*     */   
/*     */   protected final List<Runnable> pendingExecutions;
/*     */ 
/*     */   
/*     */   protected final RemoteItems remoteItems;
/*     */ 
/*     */   
/*     */   protected final LocalItems localItems;
/*     */ 
/*     */ 
/*     */   
/*     */   public UpnpService getUpnpService() {
/*  83 */     return this.upnpService;
/*     */   }
/*     */   
/*     */   public UpnpServiceConfiguration getConfiguration() {
/*  87 */     return getUpnpService().getConfiguration();
/*     */   }
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/*  91 */     return getUpnpService().getProtocolFactory();
/*     */   }
/*     */   
/*     */   protected RegistryMaintainer createRegistryMaintainer() {
/*  95 */     return new RegistryMaintainer(this, 
/*     */         
/*  97 */         getConfiguration().getRegistryMaintenanceIntervalMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RegistryImpl() {
/* 103 */     this.registryListeners = new HashSet<>();
/* 104 */     this.resourceItems = new HashSet<>();
/* 105 */     this.pendingExecutions = new ArrayList<>();
/*     */     
/* 107 */     this.remoteItems = new RemoteItems(this);
/* 108 */     this.localItems = new LocalItems(this); } @Inject public RegistryImpl(UpnpService upnpService) { this.registryListeners = new HashSet<>(); this.resourceItems = new HashSet<>(); this.pendingExecutions = new ArrayList<>(); this.remoteItems = new RemoteItems(this); this.localItems = new LocalItems(this); log.fine("Creating Registry: " + getClass().getName());
/*     */     this.upnpService = upnpService;
/*     */     log.fine("Starting registry background maintenance...");
/*     */     this.registryMaintainer = createRegistryMaintainer();
/*     */     if (this.registryMaintainer != null)
/* 113 */       getConfiguration().getRegistryMaintainerExecutor().execute(this.registryMaintainer);  } public synchronized void addListener(RegistryListener listener) { this.registryListeners.add(listener); }
/*     */ 
/*     */   
/*     */   public synchronized void removeListener(RegistryListener listener) {
/* 117 */     this.registryListeners.remove(listener);
/*     */   }
/*     */   
/*     */   public synchronized Collection<RegistryListener> getListeners() {
/* 121 */     return Collections.unmodifiableCollection(this.registryListeners);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean notifyDiscoveryStart(final RemoteDevice device) {
/* 126 */     if (getUpnpService().getRegistry().getRemoteDevice(((RemoteDeviceIdentity)device.getIdentity()).getUdn(), true) != null) {
/* 127 */       log.finer("Not notifying listeners, already registered: " + device);
/* 128 */       return false;
/*     */     } 
/* 130 */     for (RegistryListener listener : getListeners()) {
/* 131 */       getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 134 */               listener.remoteDeviceDiscoveryStarted(RegistryImpl.this, device);
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized void notifyDiscoveryFailure(final RemoteDevice device, final Exception ex) {
/* 143 */     for (RegistryListener listener : getListeners()) {
/* 144 */       getConfiguration().getRegistryListenerExecutor().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 147 */               listener.remoteDeviceDiscoveryFailed(RegistryImpl.this, device, ex);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addDevice(LocalDevice localDevice) {
/* 157 */     this.localItems.add(localDevice);
/*     */   }
/*     */   
/*     */   public synchronized void addDevice(LocalDevice localDevice, DiscoveryOptions options) {
/* 161 */     this.localItems.add(localDevice, options);
/*     */   }
/*     */   
/*     */   public synchronized void setDiscoveryOptions(UDN udn, DiscoveryOptions options) {
/* 165 */     this.localItems.setDiscoveryOptions(udn, options);
/*     */   }
/*     */   
/*     */   public synchronized DiscoveryOptions getDiscoveryOptions(UDN udn) {
/* 169 */     return this.localItems.getDiscoveryOptions(udn);
/*     */   }
/*     */   
/*     */   public synchronized void addDevice(RemoteDevice remoteDevice) {
/* 173 */     this.remoteItems.add(remoteDevice);
/*     */   }
/*     */   
/*     */   public synchronized boolean update(RemoteDeviceIdentity rdIdentity) {
/* 177 */     return this.remoteItems.update(rdIdentity);
/*     */   }
/*     */   
/*     */   public synchronized boolean removeDevice(LocalDevice localDevice) {
/* 181 */     return this.localItems.remove(localDevice);
/*     */   }
/*     */   
/*     */   public synchronized boolean removeDevice(RemoteDevice remoteDevice) {
/* 185 */     return this.remoteItems.remove(remoteDevice);
/*     */   }
/*     */   
/*     */   public synchronized void removeAllLocalDevices() {
/* 189 */     this.localItems.removeAll();
/*     */   }
/*     */   
/*     */   public synchronized void removeAllRemoteDevices() {
/* 193 */     this.remoteItems.removeAll();
/*     */   }
/*     */   
/*     */   public synchronized boolean removeDevice(UDN udn) {
/* 197 */     Device device = getDevice(udn, true);
/* 198 */     if (device != null && device instanceof LocalDevice)
/* 199 */       return removeDevice((LocalDevice)device); 
/* 200 */     if (device != null && device instanceof RemoteDevice)
/* 201 */       return removeDevice((RemoteDevice)device); 
/* 202 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized Device getDevice(UDN udn, boolean rootOnly) {
/*     */     LocalDevice localDevice;
/* 207 */     if ((localDevice = this.localItems.get(udn, rootOnly)) != null) return (Device)localDevice;  RemoteDevice remoteDevice;
/* 208 */     if ((remoteDevice = this.remoteItems.get(udn, rootOnly)) != null) return (Device)remoteDevice; 
/* 209 */     return null;
/*     */   }
/*     */   
/*     */   public synchronized LocalDevice getLocalDevice(UDN udn, boolean rootOnly) {
/* 213 */     return this.localItems.get(udn, rootOnly);
/*     */   }
/*     */   
/*     */   public synchronized RemoteDevice getRemoteDevice(UDN udn, boolean rootOnly) {
/* 217 */     return this.remoteItems.get(udn, rootOnly);
/*     */   }
/*     */   
/*     */   public synchronized Collection<LocalDevice> getLocalDevices() {
/* 221 */     return Collections.unmodifiableCollection(this.localItems.get());
/*     */   }
/*     */   
/*     */   public synchronized Collection<RemoteDevice> getRemoteDevices() {
/* 225 */     return Collections.unmodifiableCollection(this.remoteItems.get());
/*     */   }
/*     */   
/*     */   public synchronized Collection<Device> getDevices() {
/* 229 */     Set<LocalDevice> all = new HashSet();
/* 230 */     all.addAll(this.localItems.get());
/* 231 */     all.addAll(this.remoteItems.get());
/* 232 */     return (Collection)Collections.unmodifiableCollection(all);
/*     */   }
/*     */   
/*     */   public synchronized Collection<Device> getDevices(DeviceType deviceType) {
/* 236 */     Collection<Device> devices = new HashSet<>();
/*     */     
/* 238 */     devices.addAll(this.localItems.get(deviceType));
/* 239 */     devices.addAll(this.remoteItems.get(deviceType));
/*     */     
/* 241 */     return Collections.unmodifiableCollection(devices);
/*     */   }
/*     */   
/*     */   public synchronized Collection<Device> getDevices(ServiceType serviceType) {
/* 245 */     Collection<Device> devices = new HashSet<>();
/*     */     
/* 247 */     devices.addAll(this.localItems.get(serviceType));
/* 248 */     devices.addAll(this.remoteItems.get(serviceType));
/*     */     
/* 250 */     return Collections.unmodifiableCollection(devices);
/*     */   }
/*     */   
/*     */   public synchronized Service getService(ServiceReference serviceReference) {
/*     */     Device device;
/* 255 */     if ((device = getDevice(serviceReference.getUdn(), false)) != null) {
/* 256 */       return device.findService(serviceReference.getServiceId());
/*     */     }
/* 258 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Resource getResource(URI pathQuery) throws IllegalArgumentException {
/* 264 */     if (pathQuery.isAbsolute()) {
/* 265 */       throw new IllegalArgumentException("Resource URI can not be absolute, only path and query:" + pathQuery);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 270 */     for (RegistryItem<URI, Resource> resourceItem : this.resourceItems) {
/* 271 */       Resource resource = resourceItem.getItem();
/* 272 */       if (resource.matches(pathQuery)) {
/* 273 */         return resource;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 279 */     if (pathQuery.getPath().endsWith("/")) {
/* 280 */       URI pathQueryWithoutSlash = URI.create(pathQuery.toString().substring(0, pathQuery.toString().length() - 1));
/*     */       
/* 282 */       for (RegistryItem<URI, Resource> resourceItem : this.resourceItems) {
/* 283 */         Resource resource = resourceItem.getItem();
/* 284 */         if (resource.matches(pathQueryWithoutSlash)) {
/* 285 */           return resource;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 290 */     return null;
/*     */   }
/*     */   
/*     */   public synchronized <T extends Resource> T getResource(Class<T> resourceType, URI pathQuery) throws IllegalArgumentException {
/* 294 */     Resource resource = getResource(pathQuery);
/* 295 */     if (resource != null && resourceType.isAssignableFrom(resource.getClass())) {
/* 296 */       return (T)resource;
/*     */     }
/* 298 */     return null;
/*     */   }
/*     */   
/*     */   public synchronized Collection<Resource> getResources() {
/* 302 */     Collection<Resource> s = new HashSet<>();
/* 303 */     for (RegistryItem<URI, Resource> resourceItem : this.resourceItems) {
/* 304 */       s.add(resourceItem.getItem());
/*     */     }
/* 306 */     return s;
/*     */   }
/*     */   
/*     */   public synchronized <T extends Resource> Collection<T> getResources(Class<T> resourceType) {
/* 310 */     Collection<T> s = new HashSet<>();
/* 311 */     for (RegistryItem<URI, Resource> resourceItem : this.resourceItems) {
/* 312 */       if (resourceType.isAssignableFrom(((Resource)resourceItem.getItem()).getClass()))
/* 313 */         s.add((T)resourceItem.getItem()); 
/*     */     } 
/* 315 */     return s;
/*     */   }
/*     */   
/*     */   public synchronized void addResource(Resource resource) {
/* 319 */     addResource(resource, 0);
/*     */   }
/*     */   
/*     */   public synchronized void addResource(Resource resource, int maxAgeSeconds) {
/* 323 */     RegistryItem<URI, Resource> resourceItem = new RegistryItem<>(resource.getPathQuery(), resource, maxAgeSeconds);
/* 324 */     this.resourceItems.remove(resourceItem);
/* 325 */     this.resourceItems.add(resourceItem);
/*     */   }
/*     */   
/*     */   public synchronized boolean removeResource(Resource resource) {
/* 329 */     return this.resourceItems.remove(new RegistryItem<>(resource.getPathQuery()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addLocalSubscription(LocalGENASubscription subscription) {
/* 335 */     this.localItems.addSubscription(subscription);
/*     */   }
/*     */   
/*     */   public synchronized LocalGENASubscription getLocalSubscription(String subscriptionId) {
/* 339 */     return this.localItems.getSubscription(subscriptionId);
/*     */   }
/*     */   
/*     */   public synchronized boolean updateLocalSubscription(LocalGENASubscription subscription) {
/* 343 */     return this.localItems.updateSubscription(subscription);
/*     */   }
/*     */   
/*     */   public synchronized boolean removeLocalSubscription(LocalGENASubscription subscription) {
/* 347 */     return this.localItems.removeSubscription(subscription);
/*     */   }
/*     */   
/*     */   public synchronized void addRemoteSubscription(RemoteGENASubscription subscription) {
/* 351 */     this.remoteItems.addSubscription(subscription);
/*     */   }
/*     */   
/*     */   public synchronized RemoteGENASubscription getRemoteSubscription(String subscriptionId) {
/* 355 */     return this.remoteItems.getSubscription(subscriptionId);
/*     */   }
/*     */   
/*     */   public synchronized void updateRemoteSubscription(RemoteGENASubscription subscription) {
/* 359 */     this.remoteItems.updateSubscription(subscription);
/*     */   }
/*     */   
/*     */   public synchronized void removeRemoteSubscription(RemoteGENASubscription subscription) {
/* 363 */     this.remoteItems.removeSubscription(subscription);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void advertiseLocalDevices() {
/* 369 */     this.localItems.advertiseLocalDevices();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 376 */     log.fine("Shutting down registry...");
/*     */     
/* 378 */     if (this.registryMaintainer != null) {
/* 379 */       this.registryMaintainer.stop();
/*     */     }
/*     */ 
/*     */     
/* 383 */     log.finest("Executing final pending operations on shutdown: " + this.pendingExecutions.size());
/* 384 */     runPendingExecutions(false);
/*     */     
/* 386 */     for (RegistryListener listener : this.registryListeners) {
/* 387 */       listener.beforeShutdown(this);
/*     */     }
/*     */     
/* 390 */     RegistryItem[] arrayOfRegistryItem = this.resourceItems.<RegistryItem>toArray(new RegistryItem[this.resourceItems.size()]);
/* 391 */     for (RegistryItem<URI, Resource> resourceItem : arrayOfRegistryItem) {
/* 392 */       ((Resource)resourceItem.getItem()).shutdown();
/*     */     }
/*     */     
/* 395 */     this.remoteItems.shutdown();
/* 396 */     this.localItems.shutdown();
/*     */     
/* 398 */     for (RegistryListener listener : this.registryListeners) {
/* 399 */       listener.afterShutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void pause() {
/* 404 */     if (this.registryMaintainer != null) {
/* 405 */       log.fine("Pausing registry maintenance");
/* 406 */       runPendingExecutions(true);
/* 407 */       this.registryMaintainer.stop();
/* 408 */       this.registryMaintainer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void resume() {
/* 413 */     if (this.registryMaintainer == null) {
/* 414 */       log.fine("Resuming registry maintenance");
/* 415 */       this.remoteItems.resume();
/* 416 */       this.registryMaintainer = createRegistryMaintainer();
/* 417 */       if (this.registryMaintainer != null) {
/* 418 */         getConfiguration().getRegistryMaintainerExecutor().execute(this.registryMaintainer);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized boolean isPaused() {
/* 424 */     return (this.registryMaintainer == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void maintain() {
/* 431 */     if (log.isLoggable(Level.FINEST)) {
/* 432 */       log.finest("Maintaining registry...");
/*     */     }
/*     */     
/* 435 */     Iterator<RegistryItem<URI, Resource>> it = this.resourceItems.iterator();
/* 436 */     while (it.hasNext()) {
/* 437 */       RegistryItem<URI, Resource> item = it.next();
/* 438 */       if (item.getExpirationDetails().hasExpired()) {
/* 439 */         if (log.isLoggable(Level.FINER))
/* 440 */           log.finer("Removing expired resource: " + item); 
/* 441 */         it.remove();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 446 */     for (RegistryItem<URI, Resource> resourceItem : this.resourceItems) {
/* 447 */       ((Resource)resourceItem.getItem()).maintain(this.pendingExecutions, resourceItem
/*     */           
/* 449 */           .getExpirationDetails());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 454 */     this.remoteItems.maintain();
/* 455 */     this.localItems.maintain();
/*     */ 
/*     */     
/* 458 */     runPendingExecutions(true);
/*     */   }
/*     */   
/*     */   synchronized void executeAsyncProtocol(Runnable runnable) {
/* 462 */     this.pendingExecutions.add(runnable);
/*     */   }
/*     */   
/*     */   synchronized void runPendingExecutions(boolean async) {
/* 466 */     if (log.isLoggable(Level.FINEST))
/* 467 */       log.finest("Executing pending operations: " + this.pendingExecutions.size()); 
/* 468 */     for (Runnable pendingExecution : this.pendingExecutions) {
/* 469 */       if (async) {
/* 470 */         getConfiguration().getAsyncProtocolExecutor().execute(pendingExecution); continue;
/*     */       } 
/* 472 */       pendingExecution.run();
/*     */     } 
/* 474 */     if (this.pendingExecutions.size() > 0) {
/* 475 */       this.pendingExecutions.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDebugLog() {
/* 482 */     if (log.isLoggable(Level.FINE)) {
/* 483 */       log.fine("====================================    REMOTE   ================================================");
/*     */       
/* 485 */       for (RemoteDevice remoteDevice : this.remoteItems.get()) {
/* 486 */         log.fine(remoteDevice.toString());
/*     */       }
/*     */       
/* 489 */       log.fine("====================================    LOCAL    ================================================");
/*     */       
/* 491 */       for (LocalDevice localDevice : this.localItems.get()) {
/* 492 */         log.fine(localDevice.toString());
/*     */       }
/*     */       
/* 495 */       log.fine("====================================  RESOURCES  ================================================");
/*     */       
/* 497 */       for (RegistryItem<URI, Resource> resourceItem : this.resourceItems) {
/* 498 */         log.fine(resourceItem.toString());
/*     */       }
/*     */       
/* 501 */       log.fine("=================================================================================================");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerPendingRemoteSubscription(RemoteGENASubscription subscription) {
/* 509 */     synchronized (this.pendingSubscriptionsLock) {
/* 510 */       this.pendingSubscriptionsLock.add(subscription);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregisterPendingRemoteSubscription(RemoteGENASubscription subscription) {
/* 516 */     synchronized (this.pendingSubscriptionsLock) {
/* 517 */       if (this.pendingSubscriptionsLock.remove(subscription)) {
/* 518 */         this.pendingSubscriptionsLock.notifyAll();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteGENASubscription getWaitRemoteSubscription(String subscriptionId) {
/* 525 */     synchronized (this.pendingSubscriptionsLock) {
/* 526 */       RemoteGENASubscription subscription = getRemoteSubscription(subscriptionId);
/* 527 */       while (subscription == null && !this.pendingSubscriptionsLock.isEmpty()) {
/*     */         try {
/* 529 */           log.finest("Subscription not found, waiting for pending subscription procedure to terminate.");
/* 530 */           this.pendingSubscriptionsLock.wait();
/* 531 */         } catch (InterruptedException interruptedException) {}
/*     */         
/* 533 */         subscription = getRemoteSubscription(subscriptionId);
/*     */       } 
/* 535 */       return subscription;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\RegistryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */