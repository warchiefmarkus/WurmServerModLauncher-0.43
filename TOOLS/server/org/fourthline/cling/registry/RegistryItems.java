/*     */ package org.fourthline.cling.registry;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.gena.GENASubscription;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.fourthline.cling.model.types.DeviceType;
/*     */ import org.fourthline.cling.model.types.ServiceType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class RegistryItems<D extends Device, S extends GENASubscription>
/*     */ {
/*     */   protected final RegistryImpl registry;
/*  40 */   protected final Set<RegistryItem<UDN, D>> deviceItems = new HashSet<>();
/*  41 */   protected final Set<RegistryItem<String, S>> subscriptionItems = new HashSet<>();
/*     */   
/*     */   RegistryItems(RegistryImpl registry) {
/*  44 */     this.registry = registry;
/*     */   }
/*     */   
/*     */   Set<RegistryItem<UDN, D>> getDeviceItems() {
/*  48 */     return this.deviceItems;
/*     */   }
/*     */   
/*     */   Set<RegistryItem<String, S>> getSubscriptionItems() {
/*  52 */     return this.subscriptionItems;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void add(D paramD);
/*     */ 
/*     */   
/*     */   abstract boolean remove(D paramD);
/*     */ 
/*     */   
/*     */   abstract void removeAll();
/*     */ 
/*     */   
/*     */   abstract void maintain();
/*     */ 
/*     */   
/*     */   abstract void shutdown();
/*     */   
/*     */   D get(UDN udn, boolean rootOnly) {
/*  71 */     for (RegistryItem<UDN, D> item : this.deviceItems) {
/*  72 */       Device device = (Device)item.getItem();
/*  73 */       if (device.getIdentity().getUdn().equals(udn)) {
/*  74 */         return (D)device;
/*     */       }
/*  76 */       if (!rootOnly) {
/*  77 */         Device device1 = ((Device)item.getItem()).findDevice(udn);
/*  78 */         if (device1 != null) return (D)device1; 
/*     */       } 
/*     */     } 
/*  81 */     return null;
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
/*     */   Collection<D> get(DeviceType deviceType) {
/*  94 */     Collection<D> devices = new HashSet<>();
/*  95 */     for (RegistryItem<UDN, D> item : this.deviceItems) {
/*  96 */       Device[] arrayOfDevice = ((Device)item.getItem()).findDevices(deviceType);
/*  97 */       if (arrayOfDevice != null) {
/*  98 */         devices.addAll(Arrays.asList((D[])arrayOfDevice));
/*     */       }
/*     */     } 
/* 101 */     return devices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Collection<D> get(ServiceType serviceType) {
/* 111 */     Collection<D> devices = new HashSet<>();
/* 112 */     for (RegistryItem<UDN, D> item : this.deviceItems) {
/*     */       
/* 114 */       Device[] arrayOfDevice = ((Device)item.getItem()).findDevices(serviceType);
/* 115 */       if (arrayOfDevice != null) {
/* 116 */         devices.addAll(Arrays.asList((D[])arrayOfDevice));
/*     */       }
/*     */     } 
/* 119 */     return devices;
/*     */   }
/*     */   
/*     */   Collection<D> get() {
/* 123 */     Collection<D> devices = new HashSet<>();
/* 124 */     for (RegistryItem<UDN, D> item : this.deviceItems) {
/* 125 */       devices.add(item.getItem());
/*     */     }
/* 127 */     return devices;
/*     */   }
/*     */   
/*     */   boolean contains(D device) {
/* 131 */     return contains(device.getIdentity().getUdn());
/*     */   }
/*     */   
/*     */   boolean contains(UDN udn) {
/* 135 */     return this.deviceItems.contains(new RegistryItem<>(udn));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addSubscription(S subscription) {
/* 144 */     RegistryItem<String, S> subscriptionItem = new RegistryItem<>(subscription.getSubscriptionId(), subscription, subscription.getActualDurationSeconds());
/*     */ 
/*     */     
/* 147 */     this.subscriptionItems.add(subscriptionItem);
/*     */   }
/*     */   
/*     */   boolean updateSubscription(S subscription) {
/* 151 */     if (removeSubscription(subscription)) {
/* 152 */       addSubscription(subscription);
/* 153 */       return true;
/*     */     } 
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   boolean removeSubscription(S subscription) {
/* 159 */     return this.subscriptionItems.remove(new RegistryItem<>(subscription.getSubscriptionId()));
/*     */   }
/*     */   
/*     */   S getSubscription(String subscriptionId) {
/* 163 */     for (RegistryItem<String, S> registryItem : this.subscriptionItems) {
/* 164 */       if (((String)registryItem.getKey()).equals(subscriptionId)) {
/* 165 */         return registryItem.getItem();
/*     */       }
/*     */     } 
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   Resource[] getResources(Device device) throws RegistrationException {
/*     */     try {
/* 173 */       return this.registry.getConfiguration().getNamespace().getResources(device);
/* 174 */     } catch (ValidationException ex) {
/* 175 */       throw new RegistrationException("Resource discover error: " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\RegistryItems.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */