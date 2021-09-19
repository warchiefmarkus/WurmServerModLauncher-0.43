/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.Namespace;
/*     */ import org.fourthline.cling.model.Validatable;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.profile.RemoteClientInfo;
/*     */ import org.fourthline.cling.model.resource.Resource;
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
/*     */ public abstract class Device<DI extends DeviceIdentity, D extends Device, S extends Service>
/*     */   implements Validatable
/*     */ {
/*  45 */   private static final Logger log = Logger.getLogger(Device.class.getName());
/*     */   
/*     */   private final DI identity;
/*     */   
/*     */   private final UDAVersion version;
/*     */   
/*     */   private final DeviceType type;
/*     */   
/*     */   private final DeviceDetails details;
/*     */   private final Icon[] icons;
/*     */   protected final S[] services;
/*     */   protected final D[] embeddedDevices;
/*     */   private D parentDevice;
/*     */   
/*     */   public Device(DI identity) throws ValidationException {
/*  60 */     this(identity, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Device(DI identity, DeviceType type, DeviceDetails details, Icon[] icons, S[] services) throws ValidationException {
/*  65 */     this(identity, null, type, details, icons, services, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Device(DI identity, DeviceType type, DeviceDetails details, Icon[] icons, S[] services, D[] embeddedDevices) throws ValidationException {
/*  70 */     this(identity, null, type, details, icons, services, embeddedDevices);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Device(DI identity, UDAVersion version, DeviceType type, DeviceDetails details, Icon[] icons, S[] services, D[] embeddedDevices) throws ValidationException {
/*  76 */     this.identity = identity;
/*  77 */     this.version = (version == null) ? new UDAVersion() : version;
/*  78 */     this.type = type;
/*  79 */     this.details = details;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     List<Icon> validIcons = new ArrayList<>();
/*  85 */     if (icons != null) {
/*  86 */       for (Icon icon : icons) {
/*  87 */         if (icon != null) {
/*  88 */           icon.setDevice(this);
/*  89 */           List<ValidationError> iconErrors = icon.validate();
/*  90 */           if (iconErrors.isEmpty()) {
/*  91 */             validIcons.add(icon);
/*     */           } else {
/*  93 */             log.warning("Discarding invalid '" + icon + "': " + iconErrors);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*  98 */     this.icons = validIcons.<Icon>toArray(new Icon[validIcons.size()]);
/*     */     
/* 100 */     boolean allNullServices = true;
/* 101 */     if (services != null) {
/* 102 */       for (S service : services) {
/* 103 */         if (service != null) {
/* 104 */           allNullServices = false;
/* 105 */           service.setDevice(this);
/*     */         } 
/*     */       } 
/*     */     }
/* 109 */     this.services = (services == null || allNullServices) ? null : services;
/*     */     
/* 111 */     boolean allNullEmbedded = true;
/* 112 */     if (embeddedDevices != null) {
/* 113 */       for (D embeddedDevice : embeddedDevices) {
/* 114 */         if (embeddedDevice != null) {
/* 115 */           allNullEmbedded = false;
/* 116 */           embeddedDevice.setParentDevice(this);
/*     */         } 
/*     */       } 
/*     */     }
/* 120 */     this.embeddedDevices = (embeddedDevices == null || allNullEmbedded) ? null : embeddedDevices;
/*     */     
/* 122 */     List<ValidationError> errors = validate();
/* 123 */     if (errors.size() > 0) {
/* 124 */       if (log.isLoggable(Level.FINEST)) {
/* 125 */         for (ValidationError error : errors) {
/* 126 */           log.finest(error.toString());
/*     */         }
/*     */       }
/* 129 */       throw new ValidationException("Validation of device graph failed, call getErrors() on exception", errors);
/*     */     } 
/*     */   }
/*     */   
/*     */   public DI getIdentity() {
/* 134 */     return this.identity;
/*     */   }
/*     */   
/*     */   public UDAVersion getVersion() {
/* 138 */     return this.version;
/*     */   }
/*     */   
/*     */   public DeviceType getType() {
/* 142 */     return this.type;
/*     */   }
/*     */   
/*     */   public DeviceDetails getDetails() {
/* 146 */     return this.details;
/*     */   }
/*     */   
/*     */   public DeviceDetails getDetails(RemoteClientInfo info) {
/* 150 */     return getDetails();
/*     */   }
/*     */   
/*     */   public Icon[] getIcons() {
/* 154 */     return this.icons;
/*     */   }
/*     */   
/*     */   public boolean hasIcons() {
/* 158 */     return (getIcons() != null && (getIcons()).length > 0);
/*     */   }
/*     */   
/*     */   public boolean hasServices() {
/* 162 */     return (getServices() != null && (getServices()).length > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEmbeddedDevices() {
/* 167 */     return (getEmbeddedDevices() != null && (getEmbeddedDevices()).length > 0);
/*     */   }
/*     */   
/*     */   public D getParentDevice() {
/* 171 */     return this.parentDevice;
/*     */   }
/*     */   
/*     */   void setParentDevice(D parentDevice) {
/* 175 */     if (this.parentDevice != null)
/* 176 */       throw new IllegalStateException("Final value has been set already, model is immutable"); 
/* 177 */     this.parentDevice = parentDevice;
/*     */   }
/*     */   
/*     */   public boolean isRoot() {
/* 181 */     return (getParentDevice() == null);
/*     */   }
/*     */   
/*     */   public abstract S[] getServices();
/*     */   
/*     */   public abstract D[] getEmbeddedDevices();
/*     */   
/*     */   public abstract D getRoot();
/*     */   
/*     */   public abstract D findDevice(UDN paramUDN);
/*     */   
/*     */   public D[] findEmbeddedDevices() {
/* 193 */     return toDeviceArray(findEmbeddedDevices((D)this));
/*     */   }
/*     */   
/*     */   public D[] findDevices(DeviceType deviceType) {
/* 197 */     return toDeviceArray(find(deviceType, (D)this));
/*     */   }
/*     */   
/*     */   public D[] findDevices(ServiceType serviceType) {
/* 201 */     return toDeviceArray(find(serviceType, (D)this));
/*     */   }
/*     */   
/*     */   public Icon[] findIcons() {
/* 205 */     List<Icon> icons = new ArrayList<>();
/* 206 */     if (hasIcons()) {
/* 207 */       icons.addAll(Arrays.asList(getIcons()));
/*     */     }
/* 209 */     D[] embeddedDevices = findEmbeddedDevices();
/* 210 */     for (D embeddedDevice : embeddedDevices) {
/* 211 */       if (embeddedDevice.hasIcons()) {
/* 212 */         icons.addAll(Arrays.asList(embeddedDevice.getIcons()));
/*     */       }
/*     */     } 
/* 215 */     return icons.<Icon>toArray(new Icon[icons.size()]);
/*     */   }
/*     */   
/*     */   public S[] findServices() {
/* 219 */     return toServiceArray(findServices(null, null, (D)this));
/*     */   }
/*     */   
/*     */   public S[] findServices(ServiceType serviceType) {
/* 223 */     return toServiceArray(findServices(serviceType, null, (D)this));
/*     */   }
/*     */   
/*     */   protected D find(UDN udn, D current) {
/* 227 */     if (current.getIdentity() != null && current.getIdentity().getUdn() != null && 
/* 228 */       current.getIdentity().getUdn().equals(udn)) return current;
/*     */     
/* 230 */     if (current.hasEmbeddedDevices())
/* 231 */       for (Device device : (Device[])current.getEmbeddedDevices()) {
/*     */         D match;
/* 233 */         if ((match = find(udn, (D)device)) != null) return match;
/*     */       
/*     */       }  
/* 236 */     return null;
/*     */   }
/*     */   
/*     */   protected Collection<D> findEmbeddedDevices(D current) {
/* 240 */     Collection<D> devices = new HashSet<>();
/* 241 */     if (!current.isRoot() && current.getIdentity().getUdn() != null) {
/* 242 */       devices.add(current);
/*     */     }
/* 244 */     if (current.hasEmbeddedDevices()) {
/* 245 */       for (Device device : (Device[])current.getEmbeddedDevices()) {
/* 246 */         devices.addAll(findEmbeddedDevices((D)device));
/*     */       }
/*     */     }
/* 249 */     return devices;
/*     */   }
/*     */   
/*     */   protected Collection<D> find(DeviceType deviceType, D current) {
/* 253 */     Collection<D> devices = new HashSet<>();
/*     */     
/* 255 */     if (current.getType() != null && current.getType().implementsVersion(deviceType)) {
/* 256 */       devices.add(current);
/*     */     }
/* 258 */     if (current.hasEmbeddedDevices()) {
/* 259 */       for (Device device : (Device[])current.getEmbeddedDevices()) {
/* 260 */         devices.addAll(find(deviceType, (D)device));
/*     */       }
/*     */     }
/* 263 */     return devices;
/*     */   }
/*     */   
/*     */   protected Collection<D> find(ServiceType serviceType, D current) {
/* 267 */     Collection<S> services = findServices(serviceType, null, current);
/* 268 */     Collection<D> devices = new HashSet<>();
/* 269 */     for (Service service : services) {
/* 270 */       devices.add((D)service.getDevice());
/*     */     }
/* 272 */     return devices;
/*     */   }
/*     */   
/*     */   protected Collection<S> findServices(ServiceType serviceType, ServiceId serviceId, D current) {
/* 276 */     Collection<Object> services = new HashSet();
/* 277 */     if (current.hasServices())
/* 278 */       for (Object object : current.getServices()) {
/* 279 */         if (isMatch((Service)object, serviceType, serviceId)) {
/* 280 */           services.add(object);
/*     */         }
/*     */       }  
/* 283 */     Collection<D> embeddedDevices = findEmbeddedDevices(current);
/* 284 */     if (embeddedDevices != null)
/* 285 */       for (Device device : embeddedDevices) {
/* 286 */         if (device.hasServices()) {
/* 287 */           for (Object object : device.getServices()) {
/* 288 */             if (isMatch((Service)object, serviceType, serviceId)) {
/* 289 */               services.add(object);
/*     */             }
/*     */           } 
/*     */         }
/*     */       }  
/* 294 */     return (Collection)services;
/*     */   }
/*     */   
/*     */   public S findService(ServiceId serviceId) {
/* 298 */     Collection<S> services = findServices(null, serviceId, (D)this);
/* 299 */     return (services.size() == 1) ? (S)services.iterator().next() : null;
/*     */   }
/*     */   
/*     */   public S findService(ServiceType serviceType) {
/* 303 */     Collection<S> services = findServices(serviceType, null, (D)this);
/* 304 */     return (services.size() > 0) ? (S)services.iterator().next() : null;
/*     */   }
/*     */   
/*     */   public ServiceType[] findServiceTypes() {
/* 308 */     Collection<S> services = findServices(null, null, (D)this);
/* 309 */     Collection<ServiceType> col = new HashSet<>();
/* 310 */     for (Service service : services) {
/* 311 */       col.add(service.getServiceType());
/*     */     }
/* 313 */     return col.<ServiceType>toArray(new ServiceType[col.size()]);
/*     */   }
/*     */   
/*     */   private boolean isMatch(Service s, ServiceType serviceType, ServiceId serviceId) {
/* 317 */     boolean matchesType = (serviceType == null || s.getServiceType().implementsVersion(serviceType));
/* 318 */     boolean matchesId = (serviceId == null || s.getServiceId().equals(serviceId));
/* 319 */     return (matchesType && matchesId);
/*     */   }
/*     */   
/*     */   public boolean isFullyHydrated() {
/* 323 */     S[] services = findServices();
/* 324 */     for (S service : services) {
/* 325 */       if (service.hasStateVariables()) return true; 
/*     */     } 
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayString() {
/* 334 */     String cleanModelName = null;
/* 335 */     String cleanModelNumber = null;
/*     */     
/* 337 */     if (getDetails() != null && getDetails().getModelDetails() != null) {
/*     */ 
/*     */       
/* 340 */       ModelDetails modelDetails = getDetails().getModelDetails();
/* 341 */       if (modelDetails.getModelName() != null)
/*     */       {
/*     */         
/* 344 */         cleanModelName = (modelDetails.getModelNumber() != null && modelDetails.getModelName().endsWith(modelDetails.getModelNumber())) ? modelDetails.getModelName().substring(0, modelDetails.getModelName().length() - modelDetails.getModelNumber().length()) : modelDetails.getModelName();
/*     */       }
/*     */ 
/*     */       
/* 348 */       if (cleanModelName != null) {
/*     */         
/* 350 */         cleanModelNumber = (modelDetails.getModelNumber() != null && !cleanModelName.startsWith(modelDetails.getModelNumber())) ? modelDetails.getModelNumber() : "";
/*     */       } else {
/*     */         
/* 353 */         cleanModelNumber = modelDetails.getModelNumber();
/*     */       } 
/*     */     } 
/*     */     
/* 357 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 359 */     if (getDetails() != null && getDetails().getManufacturerDetails() != null) {
/*     */ 
/*     */       
/* 362 */       if (cleanModelName != null && getDetails().getManufacturerDetails().getManufacturer() != null)
/*     */       {
/*     */         
/* 365 */         cleanModelName = cleanModelName.startsWith(getDetails().getManufacturerDetails().getManufacturer()) ? cleanModelName.substring(getDetails().getManufacturerDetails().getManufacturer().length()).trim() : cleanModelName.trim();
/*     */       }
/*     */       
/* 368 */       if (getDetails().getManufacturerDetails().getManufacturer() != null) {
/* 369 */         sb.append(getDetails().getManufacturerDetails().getManufacturer());
/*     */       }
/*     */     } 
/*     */     
/* 373 */     sb.append((cleanModelName != null && cleanModelName.length() > 0) ? (" " + cleanModelName) : "");
/* 374 */     sb.append((cleanModelNumber != null && cleanModelNumber.length() > 0) ? (" " + cleanModelNumber.trim()) : "");
/* 375 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/* 379 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/* 381 */     if (getType() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 388 */       errors.addAll(getVersion().validate());
/*     */       
/* 390 */       if (getIdentity() != null) {
/* 391 */         errors.addAll(getIdentity().validate());
/*     */       }
/*     */       
/* 394 */       if (getDetails() != null) {
/* 395 */         errors.addAll(getDetails().validate());
/*     */       }
/*     */       
/* 398 */       if (hasServices()) {
/* 399 */         for (S s : getServices()) {
/* 400 */           if (s != null) {
/* 401 */             errors.addAll(s.validate());
/*     */           }
/*     */         } 
/*     */       }
/* 405 */       if (hasEmbeddedDevices()) {
/* 406 */         for (D d : getEmbeddedDevices()) {
/* 407 */           if (d != null) {
/* 408 */             errors.addAll(d.validate());
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 413 */     return errors;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 418 */     if (this == o) return true; 
/* 419 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 421 */     Device device = (Device)o;
/*     */     
/* 423 */     if (!this.identity.equals(device.identity)) return false;
/*     */     
/* 425 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 430 */     return this.identity.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract D newInstance(UDN paramUDN, UDAVersion paramUDAVersion, DeviceType paramDeviceType, DeviceDetails paramDeviceDetails, Icon[] paramArrayOfIcon, S[] paramArrayOfS, List<D> paramList) throws ValidationException;
/*     */ 
/*     */   
/*     */   public abstract S newInstance(ServiceType paramServiceType, ServiceId paramServiceId, URI paramURI1, URI paramURI2, URI paramURI3, Action<S>[] paramArrayOfAction, StateVariable<S>[] paramArrayOfStateVariable) throws ValidationException;
/*     */ 
/*     */   
/*     */   public abstract D[] toDeviceArray(Collection<D> paramCollection);
/*     */ 
/*     */   
/*     */   public abstract S[] newServiceArray(int paramInt);
/*     */   
/*     */   public abstract S[] toServiceArray(Collection<S> paramCollection);
/*     */   
/*     */   public abstract Resource[] discoverResources(Namespace paramNamespace);
/*     */   
/*     */   public String toString() {
/* 450 */     return "(" + getClass().getSimpleName() + ") Identity: " + getIdentity().toString() + ", Root: " + isRoot();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\Device.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */