/*     */ package org.fourthline.cling.protocol.async;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.DiscoveryOptions;
/*     */ import org.fourthline.cling.model.Location;
/*     */ import org.fourthline.cling.model.NetworkAddress;
/*     */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.discovery.IncomingSearchRequest;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingSearchResponse;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingSearchResponseDeviceType;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingSearchResponseRootDevice;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingSearchResponseServiceType;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingSearchResponseUDN;
/*     */ import org.fourthline.cling.model.message.header.MXHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.types.DeviceType;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.fourthline.cling.protocol.ReceivingAsync;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReceivingSearch
/*     */   extends ReceivingAsync<IncomingSearchRequest>
/*     */ {
/*  68 */   private static final Logger log = Logger.getLogger(ReceivingSearch.class.getName());
/*     */   
/*  70 */   private static final boolean LOG_ENABLED = log.isLoggable(Level.FINE);
/*     */   
/*  72 */   protected final Random randomGenerator = new Random();
/*     */   
/*     */   public ReceivingSearch(UpnpService upnpService, IncomingDatagramMessage<UpnpRequest> inputMessage) {
/*  75 */     super(upnpService, (UpnpMessage)new IncomingSearchRequest(inputMessage));
/*     */   }
/*     */   
/*     */   protected void execute() throws RouterException {
/*  79 */     if (getUpnpService().getRouter() == null) {
/*     */       
/*  81 */       log.fine("Router hasn't completed initialization, ignoring received search message");
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     if (!((IncomingSearchRequest)getInputMessage()).isMANSSDPDiscover()) {
/*  86 */       log.fine("Invalid search request, no or invalid MAN ssdp:discover header: " + getInputMessage());
/*     */       
/*     */       return;
/*     */     } 
/*  90 */     UpnpHeader searchTarget = ((IncomingSearchRequest)getInputMessage()).getSearchTarget();
/*     */     
/*  92 */     if (searchTarget == null) {
/*  93 */       log.fine("Invalid search request, did not contain ST header: " + getInputMessage());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  98 */     List<NetworkAddress> activeStreamServers = getUpnpService().getRouter().getActiveStreamServers(((IncomingSearchRequest)getInputMessage()).getLocalAddress());
/*  99 */     if (activeStreamServers.size() == 0) {
/* 100 */       log.fine("Aborting search response, no active stream servers found (network disabled?)");
/*     */       
/*     */       return;
/*     */     } 
/* 104 */     for (NetworkAddress activeStreamServer : activeStreamServers) {
/* 105 */       sendResponses(searchTarget, activeStreamServer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean waitBeforeExecution() throws InterruptedException {
/* 112 */     Integer mx = ((IncomingSearchRequest)getInputMessage()).getMX();
/*     */     
/* 114 */     if (mx == null) {
/* 115 */       log.fine("Invalid search request, did not contain MX header: " + getInputMessage());
/* 116 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     if (mx.intValue() > 120 || mx.intValue() <= 0) mx = MXHeader.DEFAULT_VALUE;
/*     */ 
/*     */     
/* 125 */     if (getUpnpService().getRegistry().getLocalDevices().size() > 0) {
/* 126 */       int sleepTime = this.randomGenerator.nextInt(mx.intValue() * 1000);
/* 127 */       log.fine("Sleeping " + sleepTime + " milliseconds to avoid flooding with search responses");
/* 128 */       Thread.sleep(sleepTime);
/*     */     } 
/*     */     
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   protected void sendResponses(UpnpHeader searchTarget, NetworkAddress activeStreamServer) throws RouterException {
/* 135 */     if (searchTarget instanceof org.fourthline.cling.model.message.header.STAllHeader) {
/*     */       
/* 137 */       sendSearchResponseAll(activeStreamServer);
/*     */     }
/* 139 */     else if (searchTarget instanceof org.fourthline.cling.model.message.header.RootDeviceHeader) {
/*     */       
/* 141 */       sendSearchResponseRootDevices(activeStreamServer);
/*     */     }
/* 143 */     else if (searchTarget instanceof org.fourthline.cling.model.message.header.UDNHeader) {
/*     */       
/* 145 */       sendSearchResponseUDN((UDN)searchTarget.getValue(), activeStreamServer);
/*     */     }
/* 147 */     else if (searchTarget instanceof org.fourthline.cling.model.message.header.DeviceTypeHeader) {
/*     */       
/* 149 */       sendSearchResponseDeviceType((DeviceType)searchTarget.getValue(), activeStreamServer);
/*     */     }
/* 151 */     else if (searchTarget instanceof org.fourthline.cling.model.message.header.ServiceTypeHeader) {
/*     */       
/* 153 */       sendSearchResponseServiceType((ServiceType)searchTarget.getValue(), activeStreamServer);
/*     */     } else {
/*     */       
/* 156 */       log.warning("Non-implemented search request target: " + searchTarget.getClass());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void sendSearchResponseAll(NetworkAddress activeStreamServer) throws RouterException {
/* 161 */     if (LOG_ENABLED) {
/* 162 */       log.fine("Responding to 'all' search with advertisement messages for all local devices");
/*     */     }
/* 164 */     for (LocalDevice localDevice : getUpnpService().getRegistry().getLocalDevices()) {
/*     */       
/* 166 */       if (isAdvertisementDisabled(localDevice)) {
/*     */         continue;
/*     */       }
/*     */       
/* 170 */       if (LOG_ENABLED) {
/* 171 */         log.finer("Sending root device messages: " + localDevice);
/*     */       }
/*     */       
/* 174 */       List<OutgoingSearchResponse> rootDeviceMsgs = createDeviceMessages(localDevice, activeStreamServer);
/* 175 */       for (OutgoingSearchResponse upnpMessage : rootDeviceMsgs) {
/* 176 */         getUpnpService().getRouter().send((OutgoingDatagramMessage)upnpMessage);
/*     */       }
/*     */       
/* 179 */       if (localDevice.hasEmbeddedDevices()) {
/* 180 */         for (LocalDevice embeddedDevice : (LocalDevice[])localDevice.findEmbeddedDevices()) {
/* 181 */           if (LOG_ENABLED) {
/* 182 */             log.finer("Sending embedded device messages: " + embeddedDevice);
/*     */           }
/*     */           
/* 185 */           List<OutgoingSearchResponse> embeddedDeviceMsgs = createDeviceMessages(embeddedDevice, activeStreamServer);
/* 186 */           for (OutgoingSearchResponse upnpMessage : embeddedDeviceMsgs) {
/* 187 */             getUpnpService().getRouter().send((OutgoingDatagramMessage)upnpMessage);
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 193 */       List<OutgoingSearchResponse> serviceTypeMsgs = createServiceTypeMessages(localDevice, activeStreamServer);
/* 194 */       if (serviceTypeMsgs.size() > 0) {
/* 195 */         if (LOG_ENABLED) {
/* 196 */           log.finer("Sending service type messages");
/*     */         }
/* 198 */         for (OutgoingSearchResponse upnpMessage : serviceTypeMsgs) {
/* 199 */           getUpnpService().getRouter().send((OutgoingDatagramMessage)upnpMessage);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<OutgoingSearchResponse> createDeviceMessages(LocalDevice device, NetworkAddress activeStreamServer) {
/* 208 */     List<OutgoingSearchResponse> msgs = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 212 */     if (device.isRoot()) {
/* 213 */       msgs.add(new OutgoingSearchResponseRootDevice((IncomingDatagramMessage)
/*     */             
/* 215 */             getInputMessage(), 
/* 216 */             getDescriptorLocation(activeStreamServer, device), device));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     msgs.add(new OutgoingSearchResponseUDN((IncomingDatagramMessage)
/*     */           
/* 224 */           getInputMessage(), 
/* 225 */           getDescriptorLocation(activeStreamServer, device), device));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     msgs.add(new OutgoingSearchResponseDeviceType((IncomingDatagramMessage)
/*     */           
/* 232 */           getInputMessage(), 
/* 233 */           getDescriptorLocation(activeStreamServer, device), device));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     for (OutgoingSearchResponse msg : msgs) {
/* 239 */       prepareOutgoingSearchResponse(msg);
/*     */     }
/*     */     
/* 242 */     return msgs;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<OutgoingSearchResponse> createServiceTypeMessages(LocalDevice device, NetworkAddress activeStreamServer) {
/* 247 */     List<OutgoingSearchResponse> msgs = new ArrayList<>();
/* 248 */     for (ServiceType serviceType : device.findServiceTypes()) {
/*     */ 
/*     */ 
/*     */       
/* 252 */       OutgoingSearchResponseServiceType outgoingSearchResponseServiceType = new OutgoingSearchResponseServiceType((IncomingDatagramMessage)getInputMessage(), getDescriptorLocation(activeStreamServer, device), device, serviceType);
/*     */ 
/*     */ 
/*     */       
/* 256 */       prepareOutgoingSearchResponse((OutgoingSearchResponse)outgoingSearchResponseServiceType);
/* 257 */       msgs.add(outgoingSearchResponseServiceType);
/*     */     } 
/* 259 */     return msgs;
/*     */   }
/*     */   
/*     */   protected void sendSearchResponseRootDevices(NetworkAddress activeStreamServer) throws RouterException {
/* 263 */     log.fine("Responding to root device search with advertisement messages for all local root devices");
/* 264 */     for (LocalDevice device : getUpnpService().getRegistry().getLocalDevices()) {
/*     */       
/* 266 */       if (isAdvertisementDisabled(device)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 272 */       OutgoingSearchResponseRootDevice outgoingSearchResponseRootDevice = new OutgoingSearchResponseRootDevice((IncomingDatagramMessage)getInputMessage(), getDescriptorLocation(activeStreamServer, device), device);
/*     */ 
/*     */       
/* 275 */       prepareOutgoingSearchResponse((OutgoingSearchResponse)outgoingSearchResponseRootDevice);
/* 276 */       getUpnpService().getRouter().send((OutgoingDatagramMessage)outgoingSearchResponseRootDevice);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void sendSearchResponseUDN(UDN udn, NetworkAddress activeStreamServer) throws RouterException {
/* 281 */     Device device = getUpnpService().getRegistry().getDevice(udn, false);
/* 282 */     if (device != null && device instanceof LocalDevice) {
/*     */       
/* 284 */       if (isAdvertisementDisabled((LocalDevice)device)) {
/*     */         return;
/*     */       }
/* 287 */       log.fine("Responding to UDN device search: " + udn);
/*     */ 
/*     */ 
/*     */       
/* 291 */       OutgoingSearchResponseUDN outgoingSearchResponseUDN = new OutgoingSearchResponseUDN((IncomingDatagramMessage)getInputMessage(), getDescriptorLocation(activeStreamServer, (LocalDevice)device), (LocalDevice)device);
/*     */ 
/*     */       
/* 294 */       prepareOutgoingSearchResponse((OutgoingSearchResponse)outgoingSearchResponseUDN);
/* 295 */       getUpnpService().getRouter().send((OutgoingDatagramMessage)outgoingSearchResponseUDN);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void sendSearchResponseDeviceType(DeviceType deviceType, NetworkAddress activeStreamServer) throws RouterException {
/* 300 */     log.fine("Responding to device type search: " + deviceType);
/* 301 */     Collection<Device> devices = getUpnpService().getRegistry().getDevices(deviceType);
/* 302 */     for (Device device : devices) {
/* 303 */       if (device instanceof LocalDevice) {
/*     */         
/* 305 */         if (isAdvertisementDisabled((LocalDevice)device)) {
/*     */           continue;
/*     */         }
/* 308 */         log.finer("Sending matching device type search result for: " + device);
/*     */ 
/*     */ 
/*     */         
/* 312 */         OutgoingSearchResponseDeviceType outgoingSearchResponseDeviceType = new OutgoingSearchResponseDeviceType((IncomingDatagramMessage)getInputMessage(), getDescriptorLocation(activeStreamServer, (LocalDevice)device), (LocalDevice)device);
/*     */ 
/*     */         
/* 315 */         prepareOutgoingSearchResponse((OutgoingSearchResponse)outgoingSearchResponseDeviceType);
/* 316 */         getUpnpService().getRouter().send((OutgoingDatagramMessage)outgoingSearchResponseDeviceType);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void sendSearchResponseServiceType(ServiceType serviceType, NetworkAddress activeStreamServer) throws RouterException {
/* 322 */     log.fine("Responding to service type search: " + serviceType);
/* 323 */     Collection<Device> devices = getUpnpService().getRegistry().getDevices(serviceType);
/* 324 */     for (Device device : devices) {
/* 325 */       if (device instanceof LocalDevice) {
/*     */         
/* 327 */         if (isAdvertisementDisabled((LocalDevice)device)) {
/*     */           continue;
/*     */         }
/* 330 */         log.finer("Sending matching service type search result: " + device);
/*     */ 
/*     */ 
/*     */         
/* 334 */         OutgoingSearchResponseServiceType outgoingSearchResponseServiceType = new OutgoingSearchResponseServiceType((IncomingDatagramMessage)getInputMessage(), getDescriptorLocation(activeStreamServer, (LocalDevice)device), (LocalDevice)device, serviceType);
/*     */ 
/*     */ 
/*     */         
/* 338 */         prepareOutgoingSearchResponse((OutgoingSearchResponse)outgoingSearchResponseServiceType);
/* 339 */         getUpnpService().getRouter().send((OutgoingDatagramMessage)outgoingSearchResponseServiceType);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Location getDescriptorLocation(NetworkAddress activeStreamServer, LocalDevice device) {
/* 345 */     return new Location(activeStreamServer, 
/*     */         
/* 347 */         getUpnpService().getConfiguration().getNamespace().getDescriptorPathString((Device)device));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAdvertisementDisabled(LocalDevice device) {
/* 353 */     DiscoveryOptions options = getUpnpService().getRegistry().getDiscoveryOptions(device.getIdentity().getUdn());
/* 354 */     return (options != null && !options.isAdvertised());
/*     */   }
/*     */   
/*     */   protected void prepareOutgoingSearchResponse(OutgoingSearchResponse message) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\async\ReceivingSearch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */