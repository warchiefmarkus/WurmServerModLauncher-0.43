/*     */ package org.fourthline.cling.protocol.async;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.Location;
/*     */ import org.fourthline.cling.model.NetworkAddress;
/*     */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingNotificationRequest;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingNotificationRequestDeviceType;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingNotificationRequestRootDevice;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingNotificationRequestServiceType;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingNotificationRequestUDN;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.types.NotificationSubtype;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.protocol.SendingAsync;
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
/*     */ public abstract class SendingNotification
/*     */   extends SendingAsync
/*     */ {
/*  47 */   private static final Logger log = Logger.getLogger(SendingNotification.class.getName());
/*     */   
/*     */   private LocalDevice device;
/*     */   
/*     */   public SendingNotification(UpnpService upnpService, LocalDevice device) {
/*  52 */     super(upnpService);
/*  53 */     this.device = device;
/*     */   }
/*     */   
/*     */   public LocalDevice getDevice() {
/*  57 */     return this.device;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void execute() throws RouterException {
/*  63 */     List<NetworkAddress> activeStreamServers = getUpnpService().getRouter().getActiveStreamServers(null);
/*  64 */     if (activeStreamServers.size() == 0) {
/*  65 */       log.fine("Aborting notifications, no active stream servers found (network disabled?)");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  70 */     List<Location> descriptorLocations = new ArrayList<>();
/*  71 */     for (NetworkAddress activeStreamServer : activeStreamServers) {
/*  72 */       descriptorLocations.add(new Location(activeStreamServer, 
/*     */ 
/*     */             
/*  75 */             getUpnpService().getConfiguration().getNamespace().getDescriptorPathString((Device)getDevice())));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  80 */     for (int i = 0; i < getBulkRepeat(); i++) {
/*     */       
/*     */       try {
/*  83 */         for (Location descriptorLocation : descriptorLocations) {
/*  84 */           sendMessages(descriptorLocation);
/*     */         }
/*     */ 
/*     */         
/*  88 */         log.finer("Sleeping " + getBulkIntervalMilliseconds() + " milliseconds");
/*  89 */         Thread.sleep(getBulkIntervalMilliseconds());
/*     */       }
/*  91 */       catch (InterruptedException ex) {
/*  92 */         log.warning("Advertisement thread was interrupted: " + ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getBulkRepeat() {
/*  98 */     return 3;
/*     */   }
/*     */   
/*     */   protected int getBulkIntervalMilliseconds() {
/* 102 */     return 150;
/*     */   }
/*     */   
/*     */   public void sendMessages(Location descriptorLocation) throws RouterException {
/* 106 */     log.finer("Sending root device messages: " + getDevice());
/*     */     
/* 108 */     List<OutgoingNotificationRequest> rootDeviceMsgs = createDeviceMessages(getDevice(), descriptorLocation);
/* 109 */     for (OutgoingNotificationRequest upnpMessage : rootDeviceMsgs) {
/* 110 */       getUpnpService().getRouter().send((OutgoingDatagramMessage)upnpMessage);
/*     */     }
/*     */     
/* 113 */     if (getDevice().hasEmbeddedDevices()) {
/* 114 */       for (LocalDevice embeddedDevice : (LocalDevice[])getDevice().findEmbeddedDevices()) {
/* 115 */         log.finer("Sending embedded device messages: " + embeddedDevice);
/*     */         
/* 117 */         List<OutgoingNotificationRequest> embeddedDeviceMsgs = createDeviceMessages(embeddedDevice, descriptorLocation);
/* 118 */         for (OutgoingNotificationRequest upnpMessage : embeddedDeviceMsgs) {
/* 119 */           getUpnpService().getRouter().send((OutgoingDatagramMessage)upnpMessage);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 125 */     List<OutgoingNotificationRequest> serviceTypeMsgs = createServiceTypeMessages(getDevice(), descriptorLocation);
/* 126 */     if (serviceTypeMsgs.size() > 0) {
/* 127 */       log.finer("Sending service type messages");
/* 128 */       for (OutgoingNotificationRequest upnpMessage : serviceTypeMsgs) {
/* 129 */         getUpnpService().getRouter().send((OutgoingDatagramMessage)upnpMessage);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<OutgoingNotificationRequest> createDeviceMessages(LocalDevice device, Location descriptorLocation) {
/* 136 */     List<OutgoingNotificationRequest> msgs = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (device.isRoot()) {
/* 141 */       msgs.add(new OutgoingNotificationRequestRootDevice(descriptorLocation, device, 
/*     */ 
/*     */ 
/*     */             
/* 145 */             getNotificationSubtype()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 150 */     msgs.add(new OutgoingNotificationRequestUDN(descriptorLocation, device, 
/*     */           
/* 152 */           getNotificationSubtype()));
/*     */ 
/*     */     
/* 155 */     msgs.add(new OutgoingNotificationRequestDeviceType(descriptorLocation, device, 
/*     */           
/* 157 */           getNotificationSubtype()));
/*     */ 
/*     */ 
/*     */     
/* 161 */     return msgs;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<OutgoingNotificationRequest> createServiceTypeMessages(LocalDevice device, Location descriptorLocation) {
/* 166 */     List<OutgoingNotificationRequest> msgs = new ArrayList<>();
/*     */     
/* 168 */     for (ServiceType serviceType : device.findServiceTypes()) {
/* 169 */       msgs.add(new OutgoingNotificationRequestServiceType(descriptorLocation, device, 
/*     */ 
/*     */             
/* 172 */             getNotificationSubtype(), serviceType));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 177 */     return msgs;
/*     */   }
/*     */   
/*     */   protected abstract NotificationSubtype getNotificationSubtype();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\async\SendingNotification.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */