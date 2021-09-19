/*     */ package coffee.keenan.network.wrappers.upnp;
/*     */ 
/*     */ import coffee.keenan.network.helpers.port.Port;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.UpnpServiceImpl;
/*     */ import org.fourthline.cling.controlpoint.ActionCallback;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.registry.RegistryListener;
/*     */ import org.fourthline.cling.support.igd.callback.PortMappingAdd;
/*     */ import org.fourthline.cling.support.model.PortMapping;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public enum UPNPService {
/*  21 */   INSTANCE;
/*     */   static {
/*  23 */     logger = Logger.getLogger(String.valueOf(UPNPService.class));
/*  24 */   } private final Object o = new Object();
/*  25 */   private final ExecutorService executorService = Executors.newSingleThreadExecutor();
/*     */   
/*     */   private boolean initialized = false;
/*     */   
/*     */   private static final Logger logger;
/*     */   
/*     */   private UpnpService upnpService;
/*     */   
/*     */   private RemoteDevice router;
/*     */   private Service wanService;
/*     */   
/*     */   public static void initialize() {
/*  37 */     if ((getInstance()).initialized)
/*  38 */       return;  (getInstance()).upnpService = (UpnpService)new UpnpServiceImpl();
/*  39 */     (getInstance()).upnpService.getRegistry().addListener((RegistryListener)new FindRouterListener());
/*  40 */     (getInstance()).initialized = true;
/*  41 */     getInstance().refresh();
/*  42 */     Runtime.getRuntime().addShutdownHook(new Thread((getInstance()).upnpService::shutdown));
/*  43 */     Runtime.getRuntime().addShutdownHook(new Thread(getInstance()::shutdownExecutor));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void shutdown() {
/*  48 */     if (!(getInstance()).initialized)
/*  49 */       return;  getInstance().shutdownExecutor();
/*  50 */     (getInstance()).upnpService.shutdown();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract(pure = true)
/*     */   public static UPNPService getInstance() {
/*  57 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract(pure = true)
/*     */   static RemoteDevice getRouterDevice() {
/*  63 */     return (getInstance()).router;
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract(pure = true)
/*     */   static Service getWanService() {
/*  69 */     return (getInstance()).wanService;
/*     */   }
/*     */ 
/*     */   
/*     */   private void shutdownExecutor() {
/*  74 */     if (!this.initialized)
/*     */       return; 
/*     */     try {
/*  77 */       this.executorService.shutdown();
/*  78 */       this.executorService.awaitTermination(5L, TimeUnit.SECONDS);
/*     */     }
/*  80 */     catch (InterruptedException e) {
/*     */       
/*  82 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/*     */       
/*  86 */       this.executorService.shutdownNow();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void refresh() {
/*  93 */     if (!this.initialized)
/*  94 */       return;  synchronized (this.o) {
/*     */       
/*  96 */       this.wanService = null;
/*  97 */       this.router = null;
/*  98 */       this.upnpService.getControlPoint().search();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setRouterAndService(RemoteDevice device, Service service) {
/* 104 */     if (!this.initialized)
/* 105 */       return;  synchronized (this.o) {
/*     */       
/* 107 */       this.wanService = service;
/* 108 */       this.router = device;
/* 109 */       this.o.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void openPort(final Port port) {
/* 115 */     if (!this.initialized) {
/*     */       
/* 117 */       logger.info("uPNP support not initialized, skipping mapping for " + port.toString());
/*     */       return;
/*     */     } 
/* 120 */     for (PortMapping portMapping : port.getMappings()) {
/*     */       
/* 122 */       this.executorService.submit(() -> {
/*     */ 
/*     */             
/*     */             if (this.wanService == null) {
/*     */               synchronized (this.o) {
/*     */ 
/*     */                 
/*     */                 try {
/*     */                   
/*     */                   this.o.wait(5000L);
/* 132 */                 } catch (InterruptedException e) {
/*     */                   e.printStackTrace();
/*     */                   
/*     */                   port.addException(port.toString(), e);
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */             this.upnpService.getControlPoint().execute((ActionCallback)new PortMappingAdd(this.wanService, portMapping)
/*     */                 {
/*     */                   public void success(ActionInvocation actionInvocation)
/*     */                   {
/* 145 */                     port.setMapped(true);
/* 146 */                     UPNPService.logger.info("port mapped for " + port.toString());
/*     */                   }
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {
/* 152 */                     UPNPService.logger.warning("unable to map port for " + port.toString());
/* 153 */                     port.addException(port.toString(), new Exception(s));
/*     */                   }
/*     */                 });
/*     */           });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\wrapper\\upnp\UPNPService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */