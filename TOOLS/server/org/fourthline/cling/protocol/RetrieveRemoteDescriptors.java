/*     */ package org.fourthline.cling.protocol;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.binding.xml.DescriptorBindingException;
/*     */ import org.fourthline.cling.binding.xml.DeviceDescriptorBinder;
/*     */ import org.fourthline.cling.binding.xml.ServiceDescriptorBinder;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.Icon;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.fourthline.cling.registry.RegistrationException;
/*     */ import org.fourthline.cling.transport.RouterException;
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
/*     */ public class RetrieveRemoteDescriptors
/*     */   implements Runnable
/*     */ {
/*  68 */   private static final Logger log = Logger.getLogger(RetrieveRemoteDescriptors.class.getName());
/*     */   
/*     */   private final UpnpService upnpService;
/*     */   
/*     */   private RemoteDevice rd;
/*  73 */   private static final Set<URL> activeRetrievals = new CopyOnWriteArraySet<>();
/*  74 */   protected List<UDN> errorsAlreadyLogged = new ArrayList<>();
/*     */   
/*     */   public RetrieveRemoteDescriptors(UpnpService upnpService, RemoteDevice rd) {
/*  77 */     this.upnpService = upnpService;
/*  78 */     this.rd = rd;
/*     */   }
/*     */   
/*     */   public UpnpService getUpnpService() {
/*  82 */     return this.upnpService;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  87 */     URL deviceURL = ((RemoteDeviceIdentity)this.rd.getIdentity()).getDescriptorURL();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (activeRetrievals.contains(deviceURL)) {
/*  94 */       log.finer("Exiting early, active retrieval for URL already in progress: " + deviceURL);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  99 */     if (getUpnpService().getRegistry().getRemoteDevice(((RemoteDeviceIdentity)this.rd.getIdentity()).getUdn(), true) != null) {
/* 100 */       log.finer("Exiting early, already discovered: " + deviceURL);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 105 */       activeRetrievals.add(deviceURL);
/* 106 */       describe();
/* 107 */     } catch (RouterException ex) {
/* 108 */       log.log(Level.WARNING, "Descriptor retrieval failed: " + deviceURL, (Throwable)ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 113 */       activeRetrievals.remove(deviceURL);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void describe() throws RouterException {
/*     */     StreamResponseMessage deviceDescMsg;
/* 124 */     if (getUpnpService().getRouter() == null) {
/* 125 */       log.warning("Router not yet initialized");
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 135 */       StreamRequestMessage deviceDescRetrievalMsg = new StreamRequestMessage(UpnpRequest.Method.GET, ((RemoteDeviceIdentity)this.rd.getIdentity()).getDescriptorURL());
/*     */ 
/*     */ 
/*     */       
/* 139 */       UpnpHeaders headers = getUpnpService().getConfiguration().getDescriptorRetrievalHeaders((RemoteDeviceIdentity)this.rd.getIdentity());
/* 140 */       if (headers != null) {
/* 141 */         deviceDescRetrievalMsg.getHeaders().putAll((Map)headers);
/*     */       }
/* 143 */       log.fine("Sending device descriptor retrieval message: " + deviceDescRetrievalMsg);
/* 144 */       deviceDescMsg = getUpnpService().getRouter().send(deviceDescRetrievalMsg);
/*     */     }
/* 146 */     catch (IllegalArgumentException ex) {
/*     */ 
/*     */       
/* 149 */       log.warning("Device descriptor retrieval failed: " + ((RemoteDeviceIdentity)this.rd
/*     */           
/* 151 */           .getIdentity()).getDescriptorURL() + ", possibly invalid URL: " + ex);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 156 */     if (deviceDescMsg == null) {
/* 157 */       log.warning("Device descriptor retrieval failed, no response: " + ((RemoteDeviceIdentity)this.rd
/* 158 */           .getIdentity()).getDescriptorURL());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 163 */     if (((UpnpResponse)deviceDescMsg.getOperation()).isFailed()) {
/* 164 */       log.warning("Device descriptor retrieval failed: " + ((RemoteDeviceIdentity)this.rd
/*     */           
/* 166 */           .getIdentity()).getDescriptorURL() + ", " + ((UpnpResponse)deviceDescMsg
/*     */           
/* 168 */           .getOperation()).getResponseDetails());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 173 */     if (!deviceDescMsg.isContentTypeTextUDA()) {
/* 174 */       log.fine("Received device descriptor without or with invalid Content-Type: " + ((RemoteDeviceIdentity)this.rd
/*     */           
/* 176 */           .getIdentity()).getDescriptorURL());
/*     */     }
/*     */ 
/*     */     
/* 180 */     String descriptorContent = deviceDescMsg.getBodyString();
/* 181 */     if (descriptorContent == null || descriptorContent.length() == 0) {
/* 182 */       log.warning("Received empty device descriptor:" + ((RemoteDeviceIdentity)this.rd.getIdentity()).getDescriptorURL());
/*     */       
/*     */       return;
/*     */     } 
/* 186 */     log.fine("Received root device descriptor: " + deviceDescMsg);
/* 187 */     describe(descriptorContent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void describe(String descriptorXML) throws RouterException {
/* 192 */     boolean notifiedStart = false;
/* 193 */     RemoteDevice describedDevice = null;
/*     */ 
/*     */     
/*     */     try {
/* 197 */       DeviceDescriptorBinder deviceDescriptorBinder = getUpnpService().getConfiguration().getDeviceDescriptorBinderUDA10();
/*     */       
/* 199 */       describedDevice = (RemoteDevice)deviceDescriptorBinder.describe((Device)this.rd, descriptorXML);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 204 */       log.fine("Remote device described (without services) notifying listeners: " + describedDevice);
/* 205 */       notifiedStart = getUpnpService().getRegistry().notifyDiscoveryStart(describedDevice);
/*     */       
/* 207 */       log.fine("Hydrating described device's services: " + describedDevice);
/* 208 */       RemoteDevice hydratedDevice = describeServices(describedDevice);
/* 209 */       if (hydratedDevice == null) {
/* 210 */         if (!this.errorsAlreadyLogged.contains(((RemoteDeviceIdentity)this.rd.getIdentity()).getUdn())) {
/* 211 */           this.errorsAlreadyLogged.add(((RemoteDeviceIdentity)this.rd.getIdentity()).getUdn());
/* 212 */           log.warning("Device service description failed: " + this.rd);
/*     */         } 
/* 214 */         if (notifiedStart) {
/* 215 */           getUpnpService().getRegistry().notifyDiscoveryFailure(describedDevice, (Exception)new DescriptorBindingException("Device service description failed: " + this.rd));
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 221 */       log.fine("Adding fully hydrated remote device to registry: " + hydratedDevice);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 226 */       getUpnpService().getRegistry().addDevice(hydratedDevice);
/*     */     
/*     */     }
/* 229 */     catch (ValidationException ex) {
/*     */       
/* 231 */       if (!this.errorsAlreadyLogged.contains(((RemoteDeviceIdentity)this.rd.getIdentity()).getUdn())) {
/* 232 */         this.errorsAlreadyLogged.add(((RemoteDeviceIdentity)this.rd.getIdentity()).getUdn());
/* 233 */         log.warning("Could not validate device model: " + this.rd);
/* 234 */         for (ValidationError validationError : ex.getErrors()) {
/* 235 */           log.warning(validationError.toString());
/*     */         }
/* 237 */         if (describedDevice != null && notifiedStart) {
/* 238 */           getUpnpService().getRegistry().notifyDiscoveryFailure(describedDevice, (Exception)ex);
/*     */         }
/*     */       } 
/* 241 */     } catch (DescriptorBindingException ex) {
/* 242 */       log.warning("Could not hydrate device or its services from descriptor: " + this.rd);
/* 243 */       log.warning("Cause was: " + Exceptions.unwrap((Throwable)ex));
/* 244 */       if (describedDevice != null && notifiedStart) {
/* 245 */         getUpnpService().getRegistry().notifyDiscoveryFailure(describedDevice, (Exception)ex);
/*     */       }
/* 247 */     } catch (RegistrationException ex) {
/* 248 */       log.warning("Adding hydrated device to registry failed: " + this.rd);
/* 249 */       log.warning("Cause was: " + ex.toString());
/* 250 */       if (describedDevice != null && notifiedStart) {
/* 251 */         getUpnpService().getRegistry().notifyDiscoveryFailure(describedDevice, (Exception)ex);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected RemoteDevice describeServices(RemoteDevice currentDevice) throws RouterException, DescriptorBindingException, ValidationException {
/* 258 */     List<RemoteService> describedServices = new ArrayList<>();
/* 259 */     if (currentDevice.hasServices()) {
/* 260 */       List<RemoteService> filteredServices = filterExclusiveServices(currentDevice.getServices());
/* 261 */       for (RemoteService service : filteredServices) {
/* 262 */         RemoteService svc = describeService(service);
/*     */         
/* 264 */         if (svc != null) {
/* 265 */           describedServices.add(svc); continue;
/*     */         } 
/* 267 */         log.warning("Skipping invalid service '" + service + "' of: " + currentDevice);
/*     */       } 
/*     */     } 
/*     */     
/* 271 */     List<RemoteDevice> describedEmbeddedDevices = new ArrayList<>();
/* 272 */     if (currentDevice.hasEmbeddedDevices())
/* 273 */       for (RemoteDevice embeddedDevice : currentDevice.getEmbeddedDevices()) {
/*     */         
/* 275 */         if (embeddedDevice != null) {
/*     */           
/* 277 */           RemoteDevice describedEmbeddedDevice = describeServices(embeddedDevice);
/*     */           
/* 279 */           if (describedEmbeddedDevice != null) {
/* 280 */             describedEmbeddedDevices.add(describedEmbeddedDevice);
/*     */           }
/*     */         } 
/*     */       }  
/* 284 */     Icon[] iconDupes = new Icon[(currentDevice.getIcons()).length];
/* 285 */     for (int i = 0; i < (currentDevice.getIcons()).length; i++) {
/* 286 */       Icon icon = currentDevice.getIcons()[i];
/* 287 */       iconDupes[i] = icon.deepCopy();
/*     */     } 
/*     */ 
/*     */     
/* 291 */     return currentDevice.newInstance(((RemoteDeviceIdentity)currentDevice
/* 292 */         .getIdentity()).getUdn(), currentDevice
/* 293 */         .getVersion(), currentDevice
/* 294 */         .getType(), currentDevice
/* 295 */         .getDetails(), iconDupes, currentDevice
/*     */         
/* 297 */         .toServiceArray(describedServices), describedEmbeddedDevices);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteService describeService(RemoteService service) throws RouterException, DescriptorBindingException, ValidationException {
/*     */     URL descriptorURL;
/*     */     try {
/* 307 */       descriptorURL = ((RemoteDevice)service.getDevice()).normalizeURI(service.getDescriptorURI());
/* 308 */     } catch (IllegalArgumentException e) {
/* 309 */       log.warning("Could not normalize service descriptor URL: " + service.getDescriptorURI());
/* 310 */       return null;
/*     */     } 
/*     */     
/* 313 */     StreamRequestMessage serviceDescRetrievalMsg = new StreamRequestMessage(UpnpRequest.Method.GET, descriptorURL);
/*     */ 
/*     */ 
/*     */     
/* 317 */     UpnpHeaders headers = getUpnpService().getConfiguration().getDescriptorRetrievalHeaders((RemoteDeviceIdentity)((RemoteDevice)service.getDevice()).getIdentity());
/* 318 */     if (headers != null) {
/* 319 */       serviceDescRetrievalMsg.getHeaders().putAll((Map)headers);
/*     */     }
/* 321 */     log.fine("Sending service descriptor retrieval message: " + serviceDescRetrievalMsg);
/* 322 */     StreamResponseMessage serviceDescMsg = getUpnpService().getRouter().send(serviceDescRetrievalMsg);
/*     */     
/* 324 */     if (serviceDescMsg == null) {
/* 325 */       log.warning("Could not retrieve service descriptor, no response: " + service);
/* 326 */       return null;
/*     */     } 
/*     */     
/* 329 */     if (((UpnpResponse)serviceDescMsg.getOperation()).isFailed()) {
/* 330 */       log.warning("Service descriptor retrieval failed: " + descriptorURL + ", " + ((UpnpResponse)serviceDescMsg
/*     */ 
/*     */           
/* 333 */           .getOperation()).getResponseDetails());
/* 334 */       return null;
/*     */     } 
/*     */     
/* 337 */     if (!serviceDescMsg.isContentTypeTextUDA()) {
/* 338 */       log.fine("Received service descriptor without or with invalid Content-Type: " + descriptorURL);
/*     */     }
/*     */ 
/*     */     
/* 342 */     String descriptorContent = serviceDescMsg.getBodyString();
/* 343 */     if (descriptorContent == null || descriptorContent.length() == 0) {
/* 344 */       log.warning("Received empty service descriptor:" + descriptorURL);
/* 345 */       return null;
/*     */     } 
/*     */     
/* 348 */     log.fine("Received service descriptor, hydrating service model: " + serviceDescMsg);
/*     */     
/* 350 */     ServiceDescriptorBinder serviceDescriptorBinder = getUpnpService().getConfiguration().getServiceDescriptorBinderUDA10();
/*     */     
/* 352 */     return (RemoteService)serviceDescriptorBinder.describe((Service)service, descriptorContent);
/*     */   }
/*     */   
/*     */   protected List<RemoteService> filterExclusiveServices(RemoteService[] services) {
/* 356 */     ServiceType[] exclusiveTypes = getUpnpService().getConfiguration().getExclusiveServiceTypes();
/*     */     
/* 358 */     if (exclusiveTypes == null || exclusiveTypes.length == 0) {
/* 359 */       return Arrays.asList(services);
/*     */     }
/* 361 */     List<RemoteService> exclusiveServices = new ArrayList<>();
/* 362 */     for (RemoteService discoveredService : services) {
/* 363 */       for (ServiceType exclusiveType : exclusiveTypes) {
/* 364 */         if (discoveredService.getServiceType().implementsVersion(exclusiveType)) {
/* 365 */           log.fine("Including exclusive service: " + discoveredService);
/* 366 */           exclusiveServices.add(discoveredService);
/*     */         } else {
/* 368 */           log.fine("Excluding unwanted service: " + exclusiveType);
/*     */         } 
/*     */       } 
/*     */     } 
/* 372 */     return exclusiveServices;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\RetrieveRemoteDescriptors.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */