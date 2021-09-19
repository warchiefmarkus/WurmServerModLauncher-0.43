/*     */ package org.fourthline.cling.registry;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.DiscoveryOptions;
/*     */ import org.fourthline.cling.model.gena.CancelReason;
/*     */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.fourthline.cling.protocol.async.SendingNotificationByebye;
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
/*     */ class LocalItems
/*     */   extends RegistryItems<LocalDevice, LocalGENASubscription>
/*     */ {
/*  43 */   private static Logger log = Logger.getLogger(Registry.class.getName());
/*     */   
/*  45 */   protected Map<UDN, DiscoveryOptions> discoveryOptions = new HashMap<>();
/*  46 */   protected long lastAliveIntervalTimestamp = 0L;
/*     */   protected Random randomGenerator;
/*     */   
/*  49 */   LocalItems(RegistryImpl registry) { super(registry);
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
/* 287 */     this.randomGenerator = new Random(); }
/*     */   protected void setDiscoveryOptions(UDN udn, DiscoveryOptions options) { if (options != null) { this.discoveryOptions.put(udn, options); } else { this.discoveryOptions.remove(udn); }  } protected DiscoveryOptions getDiscoveryOptions(UDN udn) { return this.discoveryOptions.get(udn); } protected boolean isAdvertised(UDN udn) { return (getDiscoveryOptions(udn) == null || getDiscoveryOptions(udn).isAdvertised()); } protected boolean isByeByeBeforeFirstAlive(UDN udn) { return (getDiscoveryOptions(udn) != null && getDiscoveryOptions(udn).isByeByeBeforeFirstAlive()); } void add(LocalDevice localDevice) throws RegistrationException { add(localDevice, (DiscoveryOptions)null); } void add(final LocalDevice localDevice, DiscoveryOptions options) throws RegistrationException { setDiscoveryOptions(localDevice.getIdentity().getUdn(), options); if (this.registry.getDevice(localDevice.getIdentity().getUdn(), false) != null) { log.fine("Ignoring addition, device already registered: " + localDevice); return; }  log.fine("Adding local device to registry: " + localDevice); for (Resource deviceResource : getResources((Device)localDevice)) { if (this.registry.getResource(deviceResource.getPathQuery()) != null) throw new RegistrationException("URI namespace conflict with already registered resource: " + deviceResource);  this.registry.addResource(deviceResource); log.fine("Registered resource: " + deviceResource); }  log.fine("Adding item to registry with expiration in seconds: " + localDevice.getIdentity().getMaxAgeSeconds()); RegistryItem<UDN, LocalDevice> localItem = new RegistryItem<>(localDevice.getIdentity().getUdn(), localDevice, localDevice.getIdentity().getMaxAgeSeconds().intValue()); getDeviceItems().add(localItem); log.fine("Registered local device: " + localItem); if (isByeByeBeforeFirstAlive(localItem.getKey())) advertiseByebye(localDevice, true);  if (isAdvertised(localItem.getKey())) advertiseAlive(localDevice);  for (RegistryListener listener : this.registry.getListeners()) { this.registry.getConfiguration().getRegistryListenerExecutor().execute(new Runnable() {
/*     */             public void run() { listener.localDeviceAdded(LocalItems.this.registry, localDevice); }
/* 290 */           }); }  } Collection<LocalDevice> get() { Set<LocalDevice> c = new HashSet<>(); for (RegistryItem<UDN, LocalDevice> item : getDeviceItems()) c.add(item.getItem());  return Collections.unmodifiableCollection(c); } protected void advertiseAlive(final LocalDevice localDevice) { this.registry.executeAsyncProtocol(new Runnable()
/*     */         {
/*     */           public void run() { try {
/* 293 */               LocalItems.log.finer("Sleeping some milliseconds to avoid flooding the network with ALIVE msgs");
/* 294 */               Thread.sleep(LocalItems.this.randomGenerator.nextInt(100));
/* 295 */             } catch (InterruptedException ex) {
/* 296 */               LocalItems.log.severe("Background execution interrupted: " + ex.getMessage());
/*     */             } 
/* 298 */             LocalItems.this.registry.getProtocolFactory().createSendingNotificationAlive(localDevice).run(); }
/*     */         }); }
/*     */   boolean remove(LocalDevice localDevice) throws RegistrationException { return remove(localDevice, false); } boolean remove(final LocalDevice localDevice, boolean shuttingDown) throws RegistrationException { LocalDevice registeredDevice = get(localDevice.getIdentity().getUdn(), true); if (registeredDevice != null) { log.fine("Removing local device from registry: " + localDevice); setDiscoveryOptions(localDevice.getIdentity().getUdn(), (DiscoveryOptions)null); getDeviceItems().remove(new RegistryItem<>(localDevice.getIdentity().getUdn())); for (Resource deviceResource : getResources((Device)localDevice)) { if (this.registry.removeResource(deviceResource)) log.fine("Unregistered resource: " + deviceResource);  }  Iterator<RegistryItem<String, LocalGENASubscription>> it = getSubscriptionItems().iterator(); while (it.hasNext()) { final RegistryItem<String, LocalGENASubscription> incomingSubscription = it.next(); UDN subscriptionForUDN = ((LocalService)((LocalGENASubscription)incomingSubscription.getItem()).getService()).getDevice().getIdentity().getUdn(); if (subscriptionForUDN.equals(registeredDevice.getIdentity().getUdn())) { log.fine("Removing incoming subscription: " + (String)incomingSubscription.getKey()); it.remove(); if (!shuttingDown) this.registry.getConfiguration().getRegistryListenerExecutor().execute(new Runnable() {
/*     */                   public void run() { ((LocalGENASubscription)incomingSubscription.getItem()).end(CancelReason.DEVICE_WAS_REMOVED); }
/*     */                 });  }  }  if (isAdvertised(localDevice.getIdentity().getUdn())) advertiseByebye(localDevice, !shuttingDown);  if (!shuttingDown) for (RegistryListener listener : this.registry.getListeners()) { this.registry.getConfiguration().getRegistryListenerExecutor().execute(new Runnable() {
/*     */                 public void run() { listener.localDeviceRemoved(LocalItems.this.registry, localDevice); }
/* 304 */               }); }   return true; }  return false; } void removeAll() { removeAll(false); } void removeAll(boolean shuttingDown) { LocalDevice[] allDevices = get().<LocalDevice>toArray(new LocalDevice[get().size()]); for (LocalDevice device : allDevices) remove(device, shuttingDown);  } public void advertiseLocalDevices() { for (RegistryItem<UDN, LocalDevice> localItem : this.deviceItems) { if (isAdvertised(localItem.getKey())) advertiseAlive(localItem.getItem());  }  } void maintain() { if (getDeviceItems().isEmpty()) return;  Set<RegistryItem<UDN, LocalDevice>> expiredLocalItems = new HashSet<>(); int aliveIntervalMillis = this.registry.getConfiguration().getAliveIntervalMillis(); if (aliveIntervalMillis > 0) { long now = System.currentTimeMillis(); if (now - this.lastAliveIntervalTimestamp > aliveIntervalMillis) { this.lastAliveIntervalTimestamp = now; for (RegistryItem<UDN, LocalDevice> localItem : getDeviceItems()) { if (isAdvertised(localItem.getKey())) { log.finer("Flooding advertisement of local item: " + localItem); expiredLocalItems.add(localItem); }  }  }  } else { this.lastAliveIntervalTimestamp = 0L; for (RegistryItem<UDN, LocalDevice> localItem : getDeviceItems()) { if (isAdvertised(localItem.getKey()) && localItem.getExpirationDetails().hasExpired(true)) { log.finer("Local item has expired: " + localItem); expiredLocalItems.add(localItem); }  }  }  for (RegistryItem<UDN, LocalDevice> expiredLocalItem : expiredLocalItems) { log.fine("Refreshing local device advertisement: " + expiredLocalItem.getItem()); advertiseAlive(expiredLocalItem.getItem()); expiredLocalItem.getExpirationDetails().stampLastRefresh(); }  Set<RegistryItem<String, LocalGENASubscription>> expiredIncomingSubscriptions = new HashSet<>(); for (RegistryItem<String, LocalGENASubscription> item : getSubscriptionItems()) { if (item.getExpirationDetails().hasExpired(false)) expiredIncomingSubscriptions.add(item);  }  for (RegistryItem<String, LocalGENASubscription> subscription : expiredIncomingSubscriptions) { log.fine("Removing expired: " + subscription); removeSubscription((LocalGENASubscription)subscription.getItem()); ((LocalGENASubscription)subscription.getItem()).end(CancelReason.EXPIRED); }  } void shutdown() { log.fine("Clearing all registered subscriptions to local devices during shutdown"); getSubscriptionItems().clear(); log.fine("Removing all local devices from registry during shutdown"); removeAll(true); } protected void advertiseByebye(LocalDevice localDevice, boolean asynchronous) { SendingNotificationByebye sendingNotificationByebye = this.registry.getProtocolFactory().createSendingNotificationByebye(localDevice);
/* 305 */     if (asynchronous) {
/* 306 */       this.registry.executeAsyncProtocol((Runnable)sendingNotificationByebye);
/*     */     } else {
/* 308 */       sendingNotificationByebye.run();
/*     */     }  }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\LocalItems.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */