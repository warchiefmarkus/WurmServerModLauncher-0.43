/*     */ package org.fourthline.cling;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.enterprise.context.ApplicationScoped;
/*     */ import javax.inject.Inject;
/*     */ import org.fourthline.cling.binding.xml.DeviceDescriptorBinder;
/*     */ import org.fourthline.cling.binding.xml.ServiceDescriptorBinder;
/*     */ import org.fourthline.cling.binding.xml.UDA10DeviceDescriptorBinderImpl;
/*     */ import org.fourthline.cling.binding.xml.UDA10ServiceDescriptorBinderImpl;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.Namespace;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.transport.impl.DatagramIOConfigurationImpl;
/*     */ import org.fourthline.cling.transport.impl.DatagramIOImpl;
/*     */ import org.fourthline.cling.transport.impl.GENAEventProcessorImpl;
/*     */ import org.fourthline.cling.transport.impl.MulticastReceiverConfigurationImpl;
/*     */ import org.fourthline.cling.transport.impl.MulticastReceiverImpl;
/*     */ import org.fourthline.cling.transport.impl.NetworkAddressFactoryImpl;
/*     */ import org.fourthline.cling.transport.impl.SOAPActionProcessorImpl;
/*     */ import org.fourthline.cling.transport.impl.StreamClientConfigurationImpl;
/*     */ import org.fourthline.cling.transport.impl.StreamClientImpl;
/*     */ import org.fourthline.cling.transport.impl.StreamServerConfigurationImpl;
/*     */ import org.fourthline.cling.transport.impl.StreamServerImpl;
/*     */ import org.fourthline.cling.transport.spi.DatagramIO;
/*     */ import org.fourthline.cling.transport.spi.DatagramProcessor;
/*     */ import org.fourthline.cling.transport.spi.GENAEventProcessor;
/*     */ import org.fourthline.cling.transport.spi.MulticastReceiver;
/*     */ import org.fourthline.cling.transport.spi.NetworkAddressFactory;
/*     */ import org.fourthline.cling.transport.spi.SOAPActionProcessor;
/*     */ import org.fourthline.cling.transport.spi.StreamClient;
/*     */ import org.fourthline.cling.transport.spi.StreamServer;
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
/*     */ public class ManagedUpnpServiceConfiguration
/*     */   implements UpnpServiceConfiguration
/*     */ {
/*  63 */   private static Logger log = Logger.getLogger(DefaultUpnpServiceConfiguration.class.getName());
/*     */   
/*     */   private int streamListenPort;
/*     */   
/*     */   private ExecutorService defaultExecutorService;
/*     */   
/*     */   @Inject
/*     */   protected DatagramProcessor datagramProcessor;
/*     */   
/*     */   private SOAPActionProcessor soapActionProcessor;
/*     */   
/*     */   private GENAEventProcessor genaEventProcessor;
/*     */   
/*     */   private DeviceDescriptorBinder deviceDescriptorBinderUDA10;
/*     */   
/*     */   private ServiceDescriptorBinder serviceDescriptorBinderUDA10;
/*     */   
/*     */   private Namespace namespace;
/*     */ 
/*     */   
/*     */   @PostConstruct
/*     */   public void init() {
/*  85 */     if (ModelUtil.ANDROID_RUNTIME) {
/*  86 */       throw new Error("Unsupported runtime environment, use org.fourthline.cling.android.AndroidUpnpServiceConfiguration");
/*     */     }
/*     */     
/*  89 */     this.streamListenPort = 0;
/*     */     
/*  91 */     this.defaultExecutorService = createDefaultExecutorService();
/*     */     
/*  93 */     this.soapActionProcessor = createSOAPActionProcessor();
/*  94 */     this.genaEventProcessor = createGENAEventProcessor();
/*     */     
/*  96 */     this.deviceDescriptorBinderUDA10 = createDeviceDescriptorBinderUDA10();
/*  97 */     this.serviceDescriptorBinderUDA10 = createServiceDescriptorBinderUDA10();
/*     */     
/*  99 */     this.namespace = createNamespace();
/*     */   }
/*     */   
/*     */   public DatagramProcessor getDatagramProcessor() {
/* 103 */     return this.datagramProcessor;
/*     */   }
/*     */   
/*     */   public SOAPActionProcessor getSoapActionProcessor() {
/* 107 */     return this.soapActionProcessor;
/*     */   }
/*     */   
/*     */   public GENAEventProcessor getGenaEventProcessor() {
/* 111 */     return this.genaEventProcessor;
/*     */   }
/*     */   
/*     */   public StreamClient createStreamClient() {
/* 115 */     return (StreamClient)new StreamClientImpl(new StreamClientConfigurationImpl(
/*     */           
/* 117 */           getSyncProtocolExecutorService()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MulticastReceiver createMulticastReceiver(NetworkAddressFactory networkAddressFactory) {
/* 123 */     return (MulticastReceiver)new MulticastReceiverImpl(new MulticastReceiverConfigurationImpl(networkAddressFactory
/*     */           
/* 125 */           .getMulticastGroup(), networkAddressFactory
/* 126 */           .getMulticastPort()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramIO createDatagramIO(NetworkAddressFactory networkAddressFactory) {
/* 132 */     return (DatagramIO)new DatagramIOImpl(new DatagramIOConfigurationImpl());
/*     */   }
/*     */   
/*     */   public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
/* 136 */     return (StreamServer)new StreamServerImpl(new StreamServerConfigurationImpl(networkAddressFactory
/*     */           
/* 138 */           .getStreamListenPort()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor getMulticastReceiverExecutor() {
/* 144 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public Executor getDatagramIOExecutor() {
/* 148 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public ExecutorService getStreamServerExecutorService() {
/* 152 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public DeviceDescriptorBinder getDeviceDescriptorBinderUDA10() {
/* 156 */     return this.deviceDescriptorBinderUDA10;
/*     */   }
/*     */   
/*     */   public ServiceDescriptorBinder getServiceDescriptorBinderUDA10() {
/* 160 */     return this.serviceDescriptorBinderUDA10;
/*     */   }
/*     */   
/*     */   public ServiceType[] getExclusiveServiceTypes() {
/* 164 */     return new ServiceType[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReceivedSubscriptionTimeoutIgnored() {
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   public UpnpHeaders getDescriptorRetrievalHeaders(RemoteDeviceIdentity identity) {
/* 175 */     return null;
/*     */   }
/*     */   
/*     */   public UpnpHeaders getEventSubscriptionHeaders(RemoteService service) {
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegistryMaintenanceIntervalMillis() {
/* 186 */     return 1000;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAliveIntervalMillis() {
/* 193 */     return 0;
/*     */   }
/*     */   
/*     */   public Integer getRemoteDeviceMaxAgeSeconds() {
/* 197 */     return null;
/*     */   }
/*     */   
/*     */   public Executor getAsyncProtocolExecutor() {
/* 201 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public ExecutorService getSyncProtocolExecutorService() {
/* 205 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public Namespace getNamespace() {
/* 209 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public Executor getRegistryMaintainerExecutor() {
/* 213 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public Executor getRegistryListenerExecutor() {
/* 217 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public NetworkAddressFactory createNetworkAddressFactory() {
/* 221 */     return createNetworkAddressFactory(this.streamListenPort);
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 225 */     log.fine("Shutting down default executor service");
/* 226 */     getDefaultExecutorService().shutdownNow();
/*     */   }
/*     */   
/*     */   protected NetworkAddressFactory createNetworkAddressFactory(int streamListenPort) {
/* 230 */     return (NetworkAddressFactory)new NetworkAddressFactoryImpl(streamListenPort);
/*     */   }
/*     */   
/*     */   protected SOAPActionProcessor createSOAPActionProcessor() {
/* 234 */     return (SOAPActionProcessor)new SOAPActionProcessorImpl();
/*     */   }
/*     */   
/*     */   protected GENAEventProcessor createGENAEventProcessor() {
/* 238 */     return (GENAEventProcessor)new GENAEventProcessorImpl();
/*     */   }
/*     */   
/*     */   protected DeviceDescriptorBinder createDeviceDescriptorBinderUDA10() {
/* 242 */     return (DeviceDescriptorBinder)new UDA10DeviceDescriptorBinderImpl();
/*     */   }
/*     */   
/*     */   protected ServiceDescriptorBinder createServiceDescriptorBinderUDA10() {
/* 246 */     return (ServiceDescriptorBinder)new UDA10ServiceDescriptorBinderImpl();
/*     */   }
/*     */   
/*     */   protected Namespace createNamespace() {
/* 250 */     return new Namespace();
/*     */   }
/*     */   
/*     */   protected ExecutorService getDefaultExecutorService() {
/* 254 */     return this.defaultExecutorService;
/*     */   }
/*     */   
/*     */   protected ExecutorService createDefaultExecutorService() {
/* 258 */     return new DefaultUpnpServiceConfiguration.ClingExecutor();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\ManagedUpnpServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */