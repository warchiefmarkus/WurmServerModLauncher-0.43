/*     */ package org.fourthline.cling;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.inject.Alternative;
/*     */ import org.fourthline.cling.controlpoint.ControlPoint;
/*     */ import org.fourthline.cling.controlpoint.ControlPointImpl;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.protocol.ProtocolFactoryImpl;
/*     */ import org.fourthline.cling.registry.Registry;
/*     */ import org.fourthline.cling.registry.RegistryImpl;
/*     */ import org.fourthline.cling.registry.RegistryListener;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ import org.fourthline.cling.transport.RouterImpl;
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
/*     */ @Alternative
/*     */ public class UpnpServiceImpl
/*     */   implements UpnpService
/*     */ {
/*  52 */   private static Logger log = Logger.getLogger(UpnpServiceImpl.class.getName());
/*     */   
/*     */   protected final UpnpServiceConfiguration configuration;
/*     */   protected final ControlPoint controlPoint;
/*     */   protected final ProtocolFactory protocolFactory;
/*     */   protected final Registry registry;
/*     */   protected final Router router;
/*     */   
/*     */   public UpnpServiceImpl() {
/*  61 */     this(new DefaultUpnpServiceConfiguration(), new RegistryListener[0]);
/*     */   }
/*     */   
/*     */   public UpnpServiceImpl(RegistryListener... registryListeners) {
/*  65 */     this(new DefaultUpnpServiceConfiguration(), registryListeners);
/*     */   }
/*     */   
/*     */   public UpnpServiceImpl(UpnpServiceConfiguration configuration, RegistryListener... registryListeners) {
/*  69 */     this.configuration = configuration;
/*     */     
/*  71 */     log.info(">>> Starting UPnP service...");
/*     */     
/*  73 */     log.info("Using configuration: " + getConfiguration().getClass().getName());
/*     */ 
/*     */ 
/*     */     
/*  77 */     this.protocolFactory = createProtocolFactory();
/*     */     
/*  79 */     this.registry = createRegistry(this.protocolFactory);
/*  80 */     for (RegistryListener registryListener : registryListeners) {
/*  81 */       this.registry.addListener(registryListener);
/*     */     }
/*     */     
/*  84 */     this.router = createRouter(this.protocolFactory, this.registry);
/*     */     
/*     */     try {
/*  87 */       this.router.enable();
/*  88 */     } catch (RouterException ex) {
/*  89 */       throw new RuntimeException("Enabling network router failed: " + ex, ex);
/*     */     } 
/*     */     
/*  92 */     this.controlPoint = createControlPoint(this.protocolFactory, this.registry);
/*     */     
/*  94 */     log.info("<<< UPnP service started successfully");
/*     */   }
/*     */   
/*     */   protected ProtocolFactory createProtocolFactory() {
/*  98 */     return (ProtocolFactory)new ProtocolFactoryImpl(this);
/*     */   }
/*     */   
/*     */   protected Registry createRegistry(ProtocolFactory protocolFactory) {
/* 102 */     return (Registry)new RegistryImpl(this);
/*     */   }
/*     */   
/*     */   protected Router createRouter(ProtocolFactory protocolFactory, Registry registry) {
/* 106 */     return (Router)new RouterImpl(getConfiguration(), protocolFactory);
/*     */   }
/*     */   
/*     */   protected ControlPoint createControlPoint(ProtocolFactory protocolFactory, Registry registry) {
/* 110 */     return (ControlPoint)new ControlPointImpl(getConfiguration(), protocolFactory, registry);
/*     */   }
/*     */   
/*     */   public UpnpServiceConfiguration getConfiguration() {
/* 114 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public ControlPoint getControlPoint() {
/* 118 */     return this.controlPoint;
/*     */   }
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/* 122 */     return this.protocolFactory;
/*     */   }
/*     */   
/*     */   public Registry getRegistry() {
/* 126 */     return this.registry;
/*     */   }
/*     */   
/*     */   public Router getRouter() {
/* 130 */     return this.router;
/*     */   }
/*     */   
/*     */   public synchronized void shutdown() {
/* 134 */     shutdown(false);
/*     */   }
/*     */   
/*     */   protected void shutdown(boolean separateThread) {
/* 138 */     Runnable shutdown = new Runnable()
/*     */       {
/*     */         public void run() {
/* 141 */           UpnpServiceImpl.log.info(">>> Shutting down UPnP service...");
/* 142 */           UpnpServiceImpl.this.shutdownRegistry();
/* 143 */           UpnpServiceImpl.this.shutdownRouter();
/* 144 */           UpnpServiceImpl.this.shutdownConfiguration();
/* 145 */           UpnpServiceImpl.log.info("<<< UPnP service shutdown completed");
/*     */         }
/*     */       };
/* 148 */     if (separateThread) {
/*     */       
/* 150 */       (new Thread(shutdown)).start();
/*     */     } else {
/* 152 */       shutdown.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void shutdownRegistry() {
/* 157 */     getRegistry().shutdown();
/*     */   }
/*     */   
/*     */   protected void shutdownRouter() {
/*     */     try {
/* 162 */       getRouter().shutdown();
/* 163 */     } catch (RouterException ex) {
/* 164 */       Throwable cause = Exceptions.unwrap((Throwable)ex);
/* 165 */       if (cause instanceof InterruptedException) {
/* 166 */         log.log(Level.INFO, "Router shutdown was interrupted: " + ex, cause);
/*     */       } else {
/* 168 */         log.log(Level.SEVERE, "Router error on shutdown: " + ex, cause);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void shutdownConfiguration() {
/* 174 */     getConfiguration().shutdown();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\UpnpServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */