/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.model.Namespace;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.fourthline.cling.model.resource.ServiceEventCallbackResource;
/*     */ import org.fourthline.cling.model.types.DeviceType;
/*     */ import org.fourthline.cling.model.types.ServiceId;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.seamless.util.URIUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteDevice
/*     */   extends Device<RemoteDeviceIdentity, RemoteDevice, RemoteService>
/*     */ {
/*     */   public RemoteDevice(RemoteDeviceIdentity identity) throws ValidationException {
/*  44 */     super(identity);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, RemoteService service) throws ValidationException {
/*  49 */     super(identity, type, details, null, new RemoteService[] { service });
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, RemoteService service, RemoteDevice embeddedDevice) throws ValidationException {
/*  54 */     super(identity, type, details, null, new RemoteService[] { service }, new RemoteDevice[] { embeddedDevice });
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, RemoteService[] services) throws ValidationException {
/*  59 */     super(identity, type, details, null, services);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, RemoteService[] services, RemoteDevice[] embeddedDevices) throws ValidationException {
/*  64 */     super(identity, type, details, null, services, embeddedDevices);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, RemoteService service) throws ValidationException {
/*  69 */     super(identity, type, details, new Icon[] { icon }, new RemoteService[] { service });
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, RemoteService service, RemoteDevice embeddedDevice) throws ValidationException {
/*  74 */     super(identity, type, details, new Icon[] { icon }, new RemoteService[] { service }, new RemoteDevice[] { embeddedDevice });
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, RemoteService[] services) throws ValidationException {
/*  79 */     super(identity, type, details, new Icon[] { icon }, services);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, RemoteService[] services, RemoteDevice[] embeddedDevices) throws ValidationException {
/*  84 */     super(identity, type, details, new Icon[] { icon }, services, embeddedDevices);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, RemoteService service) throws ValidationException {
/*  89 */     super(identity, type, details, icons, new RemoteService[] { service });
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, RemoteService service, RemoteDevice embeddedDevice) throws ValidationException {
/*  94 */     super(identity, type, details, icons, new RemoteService[] { service }, new RemoteDevice[] { embeddedDevice });
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, RemoteService[] services) throws ValidationException {
/*  99 */     super(identity, type, details, icons, services);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, RemoteService[] services, RemoteDevice[] embeddedDevices) throws ValidationException {
/* 104 */     super(identity, type, details, icons, services, embeddedDevices);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice(RemoteDeviceIdentity identity, UDAVersion version, DeviceType type, DeviceDetails details, Icon[] icons, RemoteService[] services, RemoteDevice[] embeddedDevices) throws ValidationException {
/* 109 */     super(identity, version, type, details, icons, services, embeddedDevices);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteService[] getServices() {
/* 114 */     return (this.services != null) ? this.services : new RemoteService[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice[] getEmbeddedDevices() {
/* 119 */     return (this.embeddedDevices != null) ? this.embeddedDevices : new RemoteDevice[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL normalizeURI(URI relativeOrAbsoluteURI) {
/* 128 */     if (getDetails() != null && getDetails().getBaseURL() != null)
/*     */     {
/* 130 */       return URIUtil.createAbsoluteURL(getDetails().getBaseURL(), relativeOrAbsoluteURI);
/*     */     }
/*     */     
/* 133 */     return URIUtil.createAbsoluteURL(getIdentity().getDescriptorURL(), relativeOrAbsoluteURI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteDevice newInstance(UDN udn, UDAVersion version, DeviceType type, DeviceDetails details, Icon[] icons, RemoteService[] services, List<RemoteDevice> embeddedDevices) throws ValidationException {
/* 142 */     return new RemoteDevice(new RemoteDeviceIdentity(udn, 
/* 143 */           getIdentity()), version, type, details, icons, services, 
/*     */ 
/*     */         
/* 146 */         (embeddedDevices.size() > 0) ? embeddedDevices.<RemoteDevice>toArray(new RemoteDevice[embeddedDevices.size()]) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteService newInstance(ServiceType serviceType, ServiceId serviceId, URI descriptorURI, URI controlURI, URI eventSubscriptionURI, Action<RemoteService>[] actions, StateVariable<RemoteService>[] stateVariables) throws ValidationException {
/* 154 */     return new RemoteService(serviceType, serviceId, descriptorURI, controlURI, eventSubscriptionURI, actions, stateVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteDevice[] toDeviceArray(Collection<RemoteDevice> col) {
/* 163 */     return col.<RemoteDevice>toArray(new RemoteDevice[col.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteService[] newServiceArray(int size) {
/* 168 */     return new RemoteService[size];
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteService[] toServiceArray(Collection<RemoteService> col) {
/* 173 */     return col.<RemoteService>toArray(new RemoteService[col.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource[] discoverResources(Namespace namespace) {
/* 178 */     List<Resource> discovered = new ArrayList<>();
/*     */ 
/*     */     
/* 181 */     for (RemoteService service : getServices()) {
/* 182 */       if (service != null) {
/* 183 */         discovered.add(new ServiceEventCallbackResource(namespace.getEventCallbackPath(service), service));
/*     */       }
/*     */     } 
/*     */     
/* 187 */     if (hasEmbeddedDevices()) {
/* 188 */       for (Device embeddedDevice : getEmbeddedDevices()) {
/* 189 */         if (embeddedDevice != null) {
/* 190 */           discovered.addAll(Arrays.asList(embeddedDevice.discoverResources(namespace)));
/*     */         }
/*     */       } 
/*     */     }
/* 194 */     return discovered.<Resource>toArray(new Resource[discovered.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice getRoot() {
/* 199 */     if (isRoot()) return this; 
/* 200 */     RemoteDevice current = this;
/* 201 */     while (current.getParentDevice() != null) {
/* 202 */       current = current.getParentDevice();
/*     */     }
/* 204 */     return current;
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDevice findDevice(UDN udn) {
/* 209 */     return find(udn, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\RemoteDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */