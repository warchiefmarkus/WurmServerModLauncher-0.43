/*     */ package org.fourthline.cling.protocol;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.context.ApplicationScoped;
/*     */ import javax.inject.Inject;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.NetworkAddress;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*     */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*     */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ import org.fourthline.cling.model.types.NamedServiceType;
/*     */ import org.fourthline.cling.model.types.NotificationSubtype;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.protocol.async.ReceivingNotification;
/*     */ import org.fourthline.cling.protocol.async.ReceivingSearch;
/*     */ import org.fourthline.cling.protocol.async.ReceivingSearchResponse;
/*     */ import org.fourthline.cling.protocol.async.SendingNotificationAlive;
/*     */ import org.fourthline.cling.protocol.async.SendingNotificationByebye;
/*     */ import org.fourthline.cling.protocol.async.SendingSearch;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingAction;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingEvent;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingRetrieval;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingSubscribe;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingUnsubscribe;
/*     */ import org.fourthline.cling.protocol.sync.SendingAction;
/*     */ import org.fourthline.cling.protocol.sync.SendingEvent;
/*     */ import org.fourthline.cling.protocol.sync.SendingRenewal;
/*     */ import org.fourthline.cling.protocol.sync.SendingSubscribe;
/*     */ import org.fourthline.cling.protocol.sync.SendingUnsubscribe;
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
/*     */ @ApplicationScoped
/*     */ public class ProtocolFactoryImpl
/*     */   implements ProtocolFactory
/*     */ {
/*  68 */   private static final Logger log = Logger.getLogger(ProtocolFactory.class.getName());
/*     */   
/*     */   protected final UpnpService upnpService;
/*     */   
/*     */   protected ProtocolFactoryImpl() {
/*  73 */     this.upnpService = null;
/*     */   }
/*     */   
/*     */   @Inject
/*     */   public ProtocolFactoryImpl(UpnpService upnpService) {
/*  78 */     log.fine("Creating ProtocolFactory: " + getClass().getName());
/*  79 */     this.upnpService = upnpService;
/*     */   }
/*     */   
/*     */   public UpnpService getUpnpService() {
/*  83 */     return this.upnpService;
/*     */   }
/*     */   
/*     */   public ReceivingAsync createReceivingAsync(IncomingDatagramMessage<UpnpRequest> message) throws ProtocolCreationException {
/*  87 */     if (log.isLoggable(Level.FINE)) {
/*  88 */       log.fine("Creating protocol for incoming asynchronous: " + message);
/*     */     }
/*     */     
/*  91 */     if (message.getOperation() instanceof UpnpRequest) {
/*  92 */       IncomingDatagramMessage<UpnpRequest> incomingRequest = message;
/*     */       
/*  94 */       switch (((UpnpRequest)incomingRequest.getOperation()).getMethod()) {
/*     */         case NOTIFY:
/*  96 */           return (isByeBye(incomingRequest) || isSupportedServiceAdvertisement(incomingRequest)) ? 
/*  97 */             createReceivingNotification(incomingRequest) : null;
/*     */         case MSEARCH:
/*  99 */           return createReceivingSearch(incomingRequest);
/*     */       } 
/*     */     
/* 102 */     } else if (message.getOperation() instanceof UpnpResponse) {
/* 103 */       IncomingDatagramMessage<UpnpRequest> incomingDatagramMessage = message;
/*     */       
/* 105 */       return isSupportedServiceAdvertisement(incomingDatagramMessage) ? 
/* 106 */         createReceivingSearchResponse((IncomingDatagramMessage)incomingDatagramMessage) : null;
/*     */     } 
/*     */     
/* 109 */     throw new ProtocolCreationException("Protocol for incoming datagram message not found: " + message);
/*     */   }
/*     */   
/*     */   protected ReceivingAsync createReceivingNotification(IncomingDatagramMessage<UpnpRequest> incomingRequest) {
/* 113 */     return (ReceivingAsync)new ReceivingNotification(getUpnpService(), incomingRequest);
/*     */   }
/*     */   
/*     */   protected ReceivingAsync createReceivingSearch(IncomingDatagramMessage<UpnpRequest> incomingRequest) {
/* 117 */     return (ReceivingAsync)new ReceivingSearch(getUpnpService(), incomingRequest);
/*     */   }
/*     */   
/*     */   protected ReceivingAsync createReceivingSearchResponse(IncomingDatagramMessage<UpnpResponse> incomingResponse) {
/* 121 */     return (ReceivingAsync)new ReceivingSearchResponse(getUpnpService(), incomingResponse);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isByeBye(IncomingDatagramMessage message) {
/* 127 */     String ntsHeader = message.getHeaders().getFirstHeader(UpnpHeader.Type.NTS.getHttpName());
/* 128 */     return (ntsHeader != null && ntsHeader.equals(NotificationSubtype.BYEBYE.getHeaderString()));
/*     */   }
/*     */   
/*     */   protected boolean isSupportedServiceAdvertisement(IncomingDatagramMessage message) {
/* 132 */     ServiceType[] exclusiveServiceTypes = getUpnpService().getConfiguration().getExclusiveServiceTypes();
/* 133 */     if (exclusiveServiceTypes == null) return false; 
/* 134 */     if (exclusiveServiceTypes.length == 0) return true;
/*     */     
/* 136 */     String usnHeader = message.getHeaders().getFirstHeader(UpnpHeader.Type.USN.getHttpName());
/* 137 */     if (usnHeader == null) return false;
/*     */     
/*     */     try {
/* 140 */       NamedServiceType nst = NamedServiceType.valueOf(usnHeader);
/* 141 */       for (ServiceType exclusiveServiceType : exclusiveServiceTypes) {
/* 142 */         if (nst.getServiceType().implementsVersion(exclusiveServiceType))
/* 143 */           return true; 
/*     */       } 
/* 145 */     } catch (InvalidValueException ex) {
/* 146 */       log.finest("Not a named service type header value: " + usnHeader);
/*     */     } 
/* 148 */     log.fine("Service advertisement not supported, dropping it: " + usnHeader);
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   public ReceivingSync createReceivingSync(StreamRequestMessage message) throws ProtocolCreationException {
/* 153 */     log.fine("Creating protocol for incoming synchronous: " + message);
/*     */     
/* 155 */     if (((UpnpRequest)message.getOperation()).getMethod().equals(UpnpRequest.Method.GET))
/*     */     {
/* 157 */       return (ReceivingSync)createReceivingRetrieval(message);
/*     */     }
/* 159 */     if (getUpnpService().getConfiguration().getNamespace().isControlPath(message.getUri())) {
/*     */       
/* 161 */       if (((UpnpRequest)message.getOperation()).getMethod().equals(UpnpRequest.Method.POST)) {
/* 162 */         return (ReceivingSync)createReceivingAction(message);
/*     */       }
/* 164 */     } else if (getUpnpService().getConfiguration().getNamespace().isEventSubscriptionPath(message.getUri())) {
/*     */       
/* 166 */       if (((UpnpRequest)message.getOperation()).getMethod().equals(UpnpRequest.Method.SUBSCRIBE))
/* 167 */         return (ReceivingSync)createReceivingSubscribe(message); 
/* 168 */       if (((UpnpRequest)message.getOperation()).getMethod().equals(UpnpRequest.Method.UNSUBSCRIBE)) {
/* 169 */         return (ReceivingSync)createReceivingUnsubscribe(message);
/*     */       }
/*     */     }
/* 172 */     else if (getUpnpService().getConfiguration().getNamespace().isEventCallbackPath(message.getUri())) {
/*     */       
/* 174 */       if (((UpnpRequest)message.getOperation()).getMethod().equals(UpnpRequest.Method.NOTIFY)) {
/* 175 */         return (ReceivingSync)createReceivingEvent(message);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 183 */     else if (message.getUri().getPath().contains("/event/cb")) {
/* 184 */       log.warning("Fixing trailing garbage in event message path: " + message.getUri().getPath());
/* 185 */       String invalid = message.getUri().toString();
/* 186 */       message.setUri(
/* 187 */           URI.create(invalid.substring(0, invalid
/* 188 */               .indexOf("/cb") + "/cb".length())));
/*     */ 
/*     */       
/* 191 */       if (getUpnpService().getConfiguration().getNamespace().isEventCallbackPath(message.getUri()) && ((UpnpRequest)message
/* 192 */         .getOperation()).getMethod().equals(UpnpRequest.Method.NOTIFY)) {
/* 193 */         return (ReceivingSync)createReceivingEvent(message);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 198 */     throw new ProtocolCreationException("Protocol for message type not found: " + message);
/*     */   }
/*     */   
/*     */   public SendingNotificationAlive createSendingNotificationAlive(LocalDevice localDevice) {
/* 202 */     return new SendingNotificationAlive(getUpnpService(), localDevice);
/*     */   }
/*     */   
/*     */   public SendingNotificationByebye createSendingNotificationByebye(LocalDevice localDevice) {
/* 206 */     return new SendingNotificationByebye(getUpnpService(), localDevice);
/*     */   }
/*     */   
/*     */   public SendingSearch createSendingSearch(UpnpHeader searchTarget, int mxSeconds) {
/* 210 */     return new SendingSearch(getUpnpService(), searchTarget, mxSeconds);
/*     */   }
/*     */   
/*     */   public SendingAction createSendingAction(ActionInvocation actionInvocation, URL controlURL) {
/* 214 */     return new SendingAction(getUpnpService(), actionInvocation, controlURL);
/*     */   }
/*     */ 
/*     */   
/*     */   public SendingSubscribe createSendingSubscribe(RemoteGENASubscription subscription) throws ProtocolCreationException {
/*     */     try {
/* 220 */       List<NetworkAddress> activeStreamServers = getUpnpService().getRouter().getActiveStreamServers(((RemoteDeviceIdentity)((RemoteDevice)((RemoteService)subscription
/* 221 */           .getService()).getDevice()).getIdentity()).getDiscoveredOnLocalAddress());
/*     */       
/* 223 */       return new SendingSubscribe(getUpnpService(), subscription, activeStreamServers);
/* 224 */     } catch (RouterException ex) {
/* 225 */       throw new ProtocolCreationException("Failed to obtain local stream servers (for event callback URL creation) from router", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SendingRenewal createSendingRenewal(RemoteGENASubscription subscription) {
/* 233 */     return new SendingRenewal(getUpnpService(), subscription);
/*     */   }
/*     */   
/*     */   public SendingUnsubscribe createSendingUnsubscribe(RemoteGENASubscription subscription) {
/* 237 */     return new SendingUnsubscribe(getUpnpService(), subscription);
/*     */   }
/*     */   
/*     */   public SendingEvent createSendingEvent(LocalGENASubscription subscription) {
/* 241 */     return new SendingEvent(getUpnpService(), subscription);
/*     */   }
/*     */   
/*     */   protected ReceivingRetrieval createReceivingRetrieval(StreamRequestMessage message) {
/* 245 */     return new ReceivingRetrieval(getUpnpService(), message);
/*     */   }
/*     */   
/*     */   protected ReceivingAction createReceivingAction(StreamRequestMessage message) {
/* 249 */     return new ReceivingAction(getUpnpService(), message);
/*     */   }
/*     */   
/*     */   protected ReceivingSubscribe createReceivingSubscribe(StreamRequestMessage message) {
/* 253 */     return new ReceivingSubscribe(getUpnpService(), message);
/*     */   }
/*     */   
/*     */   protected ReceivingUnsubscribe createReceivingUnsubscribe(StreamRequestMessage message) {
/* 257 */     return new ReceivingUnsubscribe(getUpnpService(), message);
/*     */   }
/*     */   
/*     */   protected ReceivingEvent createReceivingEvent(StreamRequestMessage message) {
/* 261 */     return new ReceivingEvent(getUpnpService(), message);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\ProtocolFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */