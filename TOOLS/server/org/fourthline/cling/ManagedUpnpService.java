/*     */ package org.fourthline.cling;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.context.ApplicationScoped;
/*     */ import javax.enterprise.event.Event;
/*     */ import javax.enterprise.event.Observes;
/*     */ import javax.enterprise.inject.Any;
/*     */ import javax.enterprise.inject.Instance;
/*     */ import javax.enterprise.util.AnnotationLiteral;
/*     */ import javax.inject.Inject;
/*     */ import org.fourthline.cling.controlpoint.ControlPoint;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.registry.Registry;
/*     */ import org.fourthline.cling.registry.RegistryListener;
/*     */ import org.fourthline.cling.registry.event.After;
/*     */ import org.fourthline.cling.registry.event.Before;
/*     */ import org.fourthline.cling.registry.event.FailedRemoteDeviceDiscovery;
/*     */ import org.fourthline.cling.registry.event.LocalDeviceDiscovery;
/*     */ import org.fourthline.cling.registry.event.Phase;
/*     */ import org.fourthline.cling.registry.event.RegistryShutdown;
/*     */ import org.fourthline.cling.registry.event.RemoteDeviceDiscovery;
/*     */ import org.fourthline.cling.transport.DisableRouter;
/*     */ import org.fourthline.cling.transport.EnableRouter;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ManagedUpnpService
/*     */   implements UpnpService
/*     */ {
/*  68 */   private static final Logger log = Logger.getLogger(ManagedUpnpService.class.getName());
/*     */   
/*     */   @Inject
/*     */   RegistryListenerAdapter registryListenerAdapter;
/*     */   
/*     */   @Inject
/*     */   Instance<UpnpServiceConfiguration> configuration;
/*     */   
/*     */   @Inject
/*     */   Instance<Registry> registryInstance;
/*     */   
/*     */   @Inject
/*     */   Instance<Router> routerInstance;
/*     */   
/*     */   @Inject
/*     */   Instance<ProtocolFactory> protocolFactoryInstance;
/*     */   
/*     */   @Inject
/*     */   Instance<ControlPoint> controlPointInstance;
/*     */   
/*     */   @Inject
/*     */   Event<EnableRouter> enableRouterEvent;
/*     */   
/*     */   @Inject
/*     */   Event<DisableRouter> disableRouterEvent;
/*     */ 
/*     */   
/*     */   public UpnpServiceConfiguration getConfiguration() {
/*  96 */     return (UpnpServiceConfiguration)this.configuration.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public ControlPoint getControlPoint() {
/* 101 */     return (ControlPoint)this.controlPointInstance.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/* 106 */     return (ProtocolFactory)this.protocolFactoryInstance.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public Registry getRegistry() {
/* 111 */     return (Registry)this.registryInstance.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public Router getRouter() {
/* 116 */     return (Router)this.routerInstance.get();
/*     */   }
/*     */   
/*     */   public void start(@Observes UpnpService.Start start) {
/* 120 */     log.info(">>> Starting managed UPnP service...");
/*     */ 
/*     */ 
/*     */     
/* 124 */     getRegistry().addListener(this.registryListenerAdapter);
/*     */     
/* 126 */     this.enableRouterEvent.fire(new EnableRouter());
/*     */     
/* 128 */     log.info("<<< Managed UPnP service started successfully");
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 133 */     shutdown(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown(@Observes UpnpService.Shutdown shutdown) {
/* 140 */     log.info(">>> Shutting down managed UPnP service...");
/*     */ 
/*     */     
/* 143 */     getRegistry().shutdown();
/*     */     
/* 145 */     this.disableRouterEvent.fire(new DisableRouter());
/*     */     
/* 147 */     getConfiguration().shutdown();
/*     */     
/* 149 */     log.info("<<< Managed UPnP service shutdown completed");
/*     */   }
/*     */ 
/*     */   
/*     */   @ApplicationScoped
/*     */   static class RegistryListenerAdapter
/*     */     implements RegistryListener
/*     */   {
/*     */     @Inject
/*     */     @Any
/*     */     Event<RemoteDeviceDiscovery> remoteDeviceDiscoveryEvent;
/*     */     
/*     */     @Inject
/*     */     @Any
/*     */     Event<FailedRemoteDeviceDiscovery> failedRemoteDeviceDiscoveryEvent;
/*     */     
/*     */     @Inject
/*     */     @Any
/*     */     Event<LocalDeviceDiscovery> localDeviceDiscoveryEvent;
/*     */     @Inject
/*     */     @Any
/*     */     Event<RegistryShutdown> registryShutdownEvent;
/*     */     
/*     */     public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
/* 173 */       this.remoteDeviceDiscoveryEvent.select(new Annotation[] { (Annotation)Phase.ALIVE }).fire(new RemoteDeviceDiscovery(device));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
/* 180 */       this.failedRemoteDeviceDiscoveryEvent.fire(new FailedRemoteDeviceDiscovery(device, ex));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
/* 187 */       this.remoteDeviceDiscoveryEvent.select(new Annotation[] { (Annotation)Phase.COMPLETE }).fire(new RemoteDeviceDiscovery(device));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
/* 194 */       this.remoteDeviceDiscoveryEvent.select(new Annotation[] { (Annotation)Phase.UPDATED }).fire(new RemoteDeviceDiscovery(device));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
/* 201 */       this.remoteDeviceDiscoveryEvent.select(new Annotation[] { (Annotation)Phase.BYEBYE }).fire(new RemoteDeviceDiscovery(device));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void localDeviceAdded(Registry registry, LocalDevice device) {
/* 208 */       this.localDeviceDiscoveryEvent.select(new Annotation[] { (Annotation)Phase.COMPLETE }).fire(new LocalDeviceDiscovery(device));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void localDeviceRemoved(Registry registry, LocalDevice device) {
/* 215 */       this.localDeviceDiscoveryEvent.select(new Annotation[] { (Annotation)Phase.BYEBYE }).fire(new LocalDeviceDiscovery(device));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void beforeShutdown(Registry registry) {
/* 222 */       this.registryShutdownEvent.select(new Annotation[] { (Annotation)new AnnotationLiteral<Before>() {  }
/* 223 */              }).fire(new RegistryShutdown());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void afterShutdown() {
/* 230 */       this.registryShutdownEvent.select(new Annotation[] { (Annotation)new AnnotationLiteral<After>() {  }
/* 231 */              }).fire(new RegistryShutdown());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\ManagedUpnpService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */