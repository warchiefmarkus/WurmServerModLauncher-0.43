/*     */ package org.fourthline.cling.support.igd;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.controlpoint.ControlPoint;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.types.DeviceType;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDADeviceType;
/*     */ import org.fourthline.cling.model.types.UDAServiceType;
/*     */ import org.fourthline.cling.registry.DefaultRegistryListener;
/*     */ import org.fourthline.cling.registry.Registry;
/*     */ import org.fourthline.cling.support.igd.callback.PortMappingAdd;
/*     */ import org.fourthline.cling.support.igd.callback.PortMappingDelete;
/*     */ import org.fourthline.cling.support.model.PortMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PortMappingListener
/*     */   extends DefaultRegistryListener
/*     */ {
/*  72 */   private static final Logger log = Logger.getLogger(PortMappingListener.class.getName());
/*     */   
/*  74 */   public static final DeviceType IGD_DEVICE_TYPE = (DeviceType)new UDADeviceType("InternetGatewayDevice", 1);
/*  75 */   public static final DeviceType CONNECTION_DEVICE_TYPE = (DeviceType)new UDADeviceType("WANConnectionDevice", 1);
/*     */   
/*  77 */   public static final ServiceType IP_SERVICE_TYPE = (ServiceType)new UDAServiceType("WANIPConnection", 1);
/*  78 */   public static final ServiceType PPP_SERVICE_TYPE = (ServiceType)new UDAServiceType("WANPPPConnection", 1);
/*     */ 
/*     */   
/*     */   protected PortMapping[] portMappings;
/*     */   
/*  83 */   protected Map<Service, List<PortMapping>> activePortMappings = new HashMap<>();
/*     */   
/*     */   public PortMappingListener(PortMapping portMapping) {
/*  86 */     this(new PortMapping[] { portMapping });
/*     */   }
/*     */   
/*     */   public PortMappingListener(PortMapping[] portMappings) {
/*  90 */     this.portMappings = portMappings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void deviceAdded(Registry registry, Device device) {
/*     */     Service connectionService;
/*  97 */     if ((connectionService = discoverConnectionService(device)) == null)
/*     */       return; 
/*  99 */     log.fine("Activating port mappings on: " + connectionService);
/*     */     
/* 101 */     final List<PortMapping> activeForService = new ArrayList<>();
/* 102 */     for (PortMapping pm : this.portMappings) {
/* 103 */       (new PortMappingAdd(connectionService, registry.getUpnpService().getControlPoint(), pm)
/*     */         {
/*     */           public void success(ActionInvocation invocation)
/*     */           {
/* 107 */             PortMappingListener.log.fine("Port mapping added: " + pm);
/* 108 */             activeForService.add(pm);
/*     */           }
/*     */ 
/*     */           
/*     */           public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
/* 113 */             PortMappingListener.this.handleFailureMessage("Failed to add port mapping: " + pm);
/* 114 */             PortMappingListener.this.handleFailureMessage("Reason: " + defaultMsg);
/*     */           }
/* 116 */         }).run();
/*     */     } 
/*     */     
/* 119 */     this.activePortMappings.put(connectionService, activeForService);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void deviceRemoved(Registry registry, Device device) {
/* 124 */     for (Service service : device.findServices()) {
/* 125 */       Iterator<Map.Entry<Service, List<PortMapping>>> it = this.activePortMappings.entrySet().iterator();
/* 126 */       while (it.hasNext()) {
/* 127 */         Map.Entry<Service, List<PortMapping>> activeEntry = it.next();
/* 128 */         if (!((Service)activeEntry.getKey()).equals(service))
/*     */           continue; 
/* 130 */         if (((List)activeEntry.getValue()).size() > 0) {
/* 131 */           handleFailureMessage("Device disappeared, couldn't delete port mappings: " + ((List)activeEntry.getValue()).size());
/*     */         }
/* 133 */         it.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void beforeShutdown(Registry registry) {
/* 140 */     for (Map.Entry<Service, List<PortMapping>> activeEntry : this.activePortMappings.entrySet()) {
/*     */       
/* 142 */       final Iterator<PortMapping> it = ((List<PortMapping>)activeEntry.getValue()).iterator();
/* 143 */       while (it.hasNext()) {
/* 144 */         final PortMapping pm = it.next();
/* 145 */         log.fine("Trying to delete port mapping on IGD: " + pm);
/* 146 */         (new PortMappingDelete(activeEntry.getKey(), registry.getUpnpService().getControlPoint(), pm)
/*     */           {
/*     */             public void success(ActionInvocation invocation)
/*     */             {
/* 150 */               PortMappingListener.log.fine("Port mapping deleted: " + pm);
/* 151 */               it.remove();
/*     */             }
/*     */ 
/*     */             
/*     */             public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
/* 156 */               PortMappingListener.this.handleFailureMessage("Failed to delete port mapping: " + pm);
/* 157 */               PortMappingListener.this.handleFailureMessage("Reason: " + defaultMsg);
/*     */             }
/* 160 */           }).run();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Service discoverConnectionService(Device device) {
/* 166 */     if (!device.getType().equals(IGD_DEVICE_TYPE)) {
/* 167 */       return null;
/*     */     }
/*     */     
/* 170 */     Device[] connectionDevices = device.findDevices(CONNECTION_DEVICE_TYPE);
/* 171 */     if (connectionDevices.length == 0) {
/* 172 */       log.fine("IGD doesn't support '" + CONNECTION_DEVICE_TYPE + "': " + device);
/* 173 */       return null;
/*     */     } 
/*     */     
/* 176 */     Device connectionDevice = connectionDevices[0];
/* 177 */     log.fine("Using first discovered WAN connection device: " + connectionDevice);
/*     */     
/* 179 */     Service ipConnectionService = connectionDevice.findService(IP_SERVICE_TYPE);
/* 180 */     Service pppConnectionService = connectionDevice.findService(PPP_SERVICE_TYPE);
/*     */     
/* 182 */     if (ipConnectionService == null && pppConnectionService == null) {
/* 183 */       log.fine("IGD doesn't support IP or PPP WAN connection service: " + device);
/*     */     }
/*     */     
/* 186 */     return (ipConnectionService != null) ? ipConnectionService : pppConnectionService;
/*     */   }
/*     */   
/*     */   protected void handleFailureMessage(String s) {
/* 190 */     log.warning(s);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\igd\PortMappingListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */