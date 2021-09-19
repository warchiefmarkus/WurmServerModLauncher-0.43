/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.model.Namespace;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.profile.DeviceDetailsProvider;
/*     */ import org.fourthline.cling.model.profile.RemoteClientInfo;
/*     */ import org.fourthline.cling.model.resource.DeviceDescriptorResource;
/*     */ import org.fourthline.cling.model.resource.IconResource;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.fourthline.cling.model.resource.ServiceControlResource;
/*     */ import org.fourthline.cling.model.resource.ServiceDescriptorResource;
/*     */ import org.fourthline.cling.model.resource.ServiceEventSubscriptionResource;
/*     */ import org.fourthline.cling.model.types.DeviceType;
/*     */ import org.fourthline.cling.model.types.ServiceId;
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
/*     */ public class LocalDevice
/*     */   extends Device<DeviceIdentity, LocalDevice, LocalService>
/*     */ {
/*     */   private final DeviceDetailsProvider deviceDetailsProvider;
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity) throws ValidationException {
/*  50 */     super(identity);
/*  51 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, LocalService service) throws ValidationException {
/*  56 */     super(identity, type, details, null, new LocalService[] { service });
/*  57 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetailsProvider deviceDetailsProvider, LocalService service) throws ValidationException {
/*  62 */     super(identity, type, null, null, new LocalService[] { service });
/*  63 */     this.deviceDetailsProvider = deviceDetailsProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetailsProvider deviceDetailsProvider, LocalService service, LocalDevice embeddedDevice) throws ValidationException {
/*  68 */     super(identity, type, null, null, new LocalService[] { service }, new LocalDevice[] { embeddedDevice });
/*  69 */     this.deviceDetailsProvider = deviceDetailsProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, LocalService service, LocalDevice embeddedDevice) throws ValidationException {
/*  74 */     super(identity, type, details, null, new LocalService[] { service }, new LocalDevice[] { embeddedDevice });
/*  75 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, LocalService[] services) throws ValidationException {
/*  80 */     super(identity, type, details, null, services);
/*  81 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, LocalService[] services, LocalDevice[] embeddedDevices) throws ValidationException {
/*  86 */     super(identity, type, details, null, services, embeddedDevices);
/*  87 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, LocalService service) throws ValidationException {
/*  92 */     super(identity, type, details, new Icon[] { icon }, new LocalService[] { service });
/*  93 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, LocalService service, LocalDevice embeddedDevice) throws ValidationException {
/*  98 */     super(identity, type, details, new Icon[] { icon }, new LocalService[] { service }, new LocalDevice[] { embeddedDevice });
/*  99 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, LocalService[] services) throws ValidationException {
/* 104 */     super(identity, type, details, new Icon[] { icon }, services);
/* 105 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetailsProvider deviceDetailsProvider, Icon icon, LocalService[] services) throws ValidationException {
/* 110 */     super(identity, type, null, new Icon[] { icon }, services);
/* 111 */     this.deviceDetailsProvider = deviceDetailsProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon icon, LocalService[] services, LocalDevice[] embeddedDevices) throws ValidationException {
/* 116 */     super(identity, type, details, new Icon[] { icon }, services, embeddedDevices);
/* 117 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, LocalService service) throws ValidationException {
/* 122 */     super(identity, type, details, icons, new LocalService[] { service });
/* 123 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, LocalService service, LocalDevice embeddedDevice) throws ValidationException {
/* 128 */     super(identity, type, details, icons, new LocalService[] { service }, new LocalDevice[] { embeddedDevice });
/* 129 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetailsProvider deviceDetailsProvider, Icon[] icons, LocalService service, LocalDevice embeddedDevice) throws ValidationException {
/* 134 */     super(identity, type, null, icons, new LocalService[] { service }, new LocalDevice[] { embeddedDevice });
/* 135 */     this.deviceDetailsProvider = deviceDetailsProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, LocalService[] services) throws ValidationException {
/* 140 */     super(identity, type, details, icons, services);
/* 141 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, DeviceType type, DeviceDetails details, Icon[] icons, LocalService[] services, LocalDevice[] embeddedDevices) throws ValidationException {
/* 146 */     super(identity, type, details, icons, services, embeddedDevices);
/* 147 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, UDAVersion version, DeviceType type, DeviceDetails details, Icon[] icons, LocalService[] services, LocalDevice[] embeddedDevices) throws ValidationException {
/* 152 */     super(identity, version, type, details, icons, services, embeddedDevices);
/* 153 */     this.deviceDetailsProvider = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice(DeviceIdentity identity, UDAVersion version, DeviceType type, DeviceDetailsProvider deviceDetailsProvider, Icon[] icons, LocalService[] services, LocalDevice[] embeddedDevices) throws ValidationException {
/* 158 */     super(identity, version, type, null, icons, services, embeddedDevices);
/* 159 */     this.deviceDetailsProvider = deviceDetailsProvider;
/*     */   }
/*     */   
/*     */   public DeviceDetailsProvider getDeviceDetailsProvider() {
/* 163 */     return this.deviceDetailsProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails getDetails(RemoteClientInfo info) {
/* 168 */     if (getDeviceDetailsProvider() != null) {
/* 169 */       return getDeviceDetailsProvider().provide(info);
/*     */     }
/* 171 */     return getDetails();
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalService[] getServices() {
/* 176 */     return (this.services != null) ? this.services : new LocalService[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice[] getEmbeddedDevices() {
/* 181 */     return (this.embeddedDevices != null) ? this.embeddedDevices : new LocalDevice[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalDevice newInstance(UDN udn, UDAVersion version, DeviceType type, DeviceDetails details, Icon[] icons, LocalService[] services, List<LocalDevice> embeddedDevices) throws ValidationException {
/* 188 */     return new LocalDevice(new DeviceIdentity(udn, 
/* 189 */           getIdentity().getMaxAgeSeconds()), version, type, details, icons, services, 
/*     */ 
/*     */         
/* 192 */         (embeddedDevices.size() > 0) ? embeddedDevices.<LocalDevice>toArray(new LocalDevice[embeddedDevices.size()]) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalService newInstance(ServiceType serviceType, ServiceId serviceId, URI descriptorURI, URI controlURI, URI eventSubscriptionURI, Action<LocalService>[] actions, StateVariable<LocalService>[] stateVariables) throws ValidationException {
/* 200 */     return new LocalService(serviceType, serviceId, (Action[])actions, (StateVariable[])stateVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalDevice[] toDeviceArray(Collection<LocalDevice> col) {
/* 208 */     return col.<LocalDevice>toArray(new LocalDevice[col.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalService[] newServiceArray(int size) {
/* 213 */     return new LocalService[size];
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalService[] toServiceArray(Collection<LocalService> col) {
/* 218 */     return col.<LocalService>toArray(new LocalService[col.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ValidationError> validate() {
/* 223 */     List<ValidationError> errors = new ArrayList<>();
/* 224 */     errors.addAll(super.validate());
/*     */ 
/*     */ 
/*     */     
/* 228 */     if (hasIcons()) {
/* 229 */       for (Icon icon : getIcons()) {
/* 230 */         if (icon.getUri().isAbsolute()) {
/* 231 */           errors.add(new ValidationError(
/* 232 */                 getClass(), "icons", "Local icon URI can not be absolute: " + icon
/*     */                 
/* 234 */                 .getUri()));
/*     */         }
/*     */         
/* 237 */         if (icon.getUri().toString().contains("../")) {
/* 238 */           errors.add(new ValidationError(
/* 239 */                 getClass(), "icons", "Local icon URI must not contain '../': " + icon
/*     */                 
/* 241 */                 .getUri()));
/*     */         }
/*     */         
/* 244 */         if (icon.getUri().toString().startsWith("/")) {
/* 245 */           errors.add(new ValidationError(
/* 246 */                 getClass(), "icons", "Local icon URI must not start with '/': " + icon
/*     */                 
/* 248 */                 .getUri()));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 254 */     return errors;
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource[] discoverResources(Namespace namespace) {
/* 259 */     List<Resource> discovered = new ArrayList<>();
/*     */ 
/*     */     
/* 262 */     if (isRoot())
/*     */     {
/*     */       
/* 265 */       discovered.add(new DeviceDescriptorResource(namespace.getDescriptorPath(this), this));
/*     */     }
/*     */ 
/*     */     
/* 269 */     for (LocalService service : getServices()) {
/*     */       
/* 271 */       discovered.add(new ServiceDescriptorResource(namespace
/* 272 */             .getDescriptorPath(service), service));
/*     */ 
/*     */ 
/*     */       
/* 276 */       discovered.add(new ServiceControlResource(namespace
/* 277 */             .getControlPath(service), service));
/*     */ 
/*     */ 
/*     */       
/* 281 */       discovered.add(new ServiceEventSubscriptionResource(namespace
/* 282 */             .getEventSubscriptionPath(service), service));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     for (Icon icon : getIcons()) {
/* 289 */       discovered.add(new IconResource(namespace.prefixIfRelative(this, icon.getUri()), icon));
/*     */     }
/*     */ 
/*     */     
/* 293 */     if (hasEmbeddedDevices()) {
/* 294 */       for (Device embeddedDevice : getEmbeddedDevices()) {
/* 295 */         discovered.addAll(Arrays.asList(embeddedDevice.discoverResources(namespace)));
/*     */       }
/*     */     }
/*     */     
/* 299 */     return discovered.<Resource>toArray(new Resource[discovered.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice getRoot() {
/* 304 */     if (isRoot()) return this; 
/* 305 */     LocalDevice current = this;
/* 306 */     while (current.getParentDevice() != null) {
/* 307 */       current = current.getParentDevice();
/*     */     }
/* 309 */     return current;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalDevice findDevice(UDN udn) {
/* 314 */     return find(udn, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\LocalDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */