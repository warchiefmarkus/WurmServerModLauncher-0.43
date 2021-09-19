/*     */ package org.fourthline.cling.mock;
/*     */ 
/*     */ import javax.enterprise.inject.Alternative;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.UpnpServiceConfiguration;
/*     */ import org.fourthline.cling.controlpoint.ControlPoint;
/*     */ import org.fourthline.cling.controlpoint.ControlPointImpl;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.protocol.ProtocolFactoryImpl;
/*     */ import org.fourthline.cling.protocol.async.SendingNotificationAlive;
/*     */ import org.fourthline.cling.protocol.async.SendingSearch;
/*     */ import org.fourthline.cling.registry.Registry;
/*     */ import org.fourthline.cling.registry.RegistryImpl;
/*     */ import org.fourthline.cling.registry.RegistryMaintainer;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ import org.fourthline.cling.transport.spi.NetworkAddressFactory;
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
/*     */ public class MockUpnpService
/*     */   implements UpnpService
/*     */ {
/*     */   protected final UpnpServiceConfiguration configuration;
/*     */   protected final ControlPoint controlPoint;
/*     */   protected final ProtocolFactory protocolFactory;
/*     */   protected final Registry registry;
/*     */   protected final MockRouter router;
/*     */   protected final NetworkAddressFactory networkAddressFactory;
/*     */   
/*     */   public MockUpnpService() {
/*  59 */     this(false, new MockUpnpServiceConfiguration(false, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MockUpnpService(MockUpnpServiceConfiguration configuration) {
/*  66 */     this(false, configuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MockUpnpService(boolean sendsAlive, boolean maintainsRegistry) {
/*  73 */     this(sendsAlive, new MockUpnpServiceConfiguration(maintainsRegistry, false));
/*     */   }
/*     */   
/*     */   public MockUpnpService(boolean sendsAlive, boolean maintainsRegistry, boolean multiThreaded) {
/*  77 */     this(sendsAlive, new MockUpnpServiceConfiguration(maintainsRegistry, multiThreaded));
/*     */   }
/*     */ 
/*     */   
/*     */   public MockUpnpService(boolean sendsAlive, final MockUpnpServiceConfiguration configuration) {
/*  82 */     this.configuration = (UpnpServiceConfiguration)configuration;
/*     */     
/*  84 */     this.protocolFactory = createProtocolFactory(this, sendsAlive);
/*     */     
/*  86 */     this.registry = (Registry)new RegistryImpl(this)
/*     */       {
/*     */         protected RegistryMaintainer createRegistryMaintainer() {
/*  89 */           return configuration.isMaintainsRegistry() ? super.createRegistryMaintainer() : null;
/*     */         }
/*     */       };
/*     */     
/*  93 */     this.networkAddressFactory = this.configuration.createNetworkAddressFactory();
/*     */     
/*  95 */     this.router = createRouter();
/*     */     
/*  97 */     this.controlPoint = (ControlPoint)new ControlPointImpl((UpnpServiceConfiguration)configuration, this.protocolFactory, this.registry);
/*     */   }
/*     */   
/*     */   protected ProtocolFactory createProtocolFactory(UpnpService service, boolean sendsAlive) {
/* 101 */     return (ProtocolFactory)new MockProtocolFactory(service, sendsAlive);
/*     */   }
/*     */   
/*     */   protected MockRouter createRouter() {
/* 105 */     return new MockRouter(getConfiguration(), getProtocolFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MockProtocolFactory
/*     */     extends ProtocolFactoryImpl
/*     */   {
/*     */     private boolean sendsAlive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MockProtocolFactory(UpnpService upnpService, boolean sendsAlive) {
/* 124 */       super(upnpService);
/* 125 */       this.sendsAlive = sendsAlive;
/*     */     }
/*     */ 
/*     */     
/*     */     public SendingNotificationAlive createSendingNotificationAlive(LocalDevice localDevice) {
/* 130 */       return new SendingNotificationAlive(getUpnpService(), localDevice)
/*     */         {
/*     */           protected void execute() throws RouterException {
/* 133 */             if (MockUpnpService.MockProtocolFactory.this.sendsAlive) super.execute();
/*     */           
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public SendingSearch createSendingSearch(UpnpHeader searchTarget, int mxSeconds) {
/* 140 */       return new SendingSearch(getUpnpService(), searchTarget, mxSeconds)
/*     */         {
/*     */           public int getBulkIntervalMilliseconds() {
/* 143 */             return 0;
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   public UpnpServiceConfiguration getConfiguration() {
/* 150 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public ControlPoint getControlPoint() {
/* 154 */     return this.controlPoint;
/*     */   }
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/* 158 */     return this.protocolFactory;
/*     */   }
/*     */   
/*     */   public Registry getRegistry() {
/* 162 */     return this.registry;
/*     */   }
/*     */   
/*     */   public MockRouter getRouter() {
/* 166 */     return this.router;
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 170 */     getRegistry().shutdown();
/* 171 */     getConfiguration().shutdown();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\mock\MockUpnpService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */