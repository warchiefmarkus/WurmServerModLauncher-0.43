/*     */ package org.fourthline.cling;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.inject.Alternative;
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
/*     */ import org.fourthline.cling.transport.impl.DatagramProcessorImpl;
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
/*     */ public class DefaultUpnpServiceConfiguration
/*     */   implements UpnpServiceConfiguration
/*     */ {
/*  89 */   private static Logger log = Logger.getLogger(DefaultUpnpServiceConfiguration.class.getName());
/*     */   
/*     */   private final int streamListenPort;
/*     */   
/*     */   private final ExecutorService defaultExecutorService;
/*     */   
/*     */   private final DatagramProcessor datagramProcessor;
/*     */   
/*     */   private final SOAPActionProcessor soapActionProcessor;
/*     */   
/*     */   private final GENAEventProcessor genaEventProcessor;
/*     */   
/*     */   private final DeviceDescriptorBinder deviceDescriptorBinderUDA10;
/*     */   
/*     */   private final ServiceDescriptorBinder serviceDescriptorBinderUDA10;
/*     */   
/*     */   private final Namespace namespace;
/*     */   
/*     */   public DefaultUpnpServiceConfiguration() {
/* 108 */     this(0);
/*     */   }
/*     */   
/*     */   public DefaultUpnpServiceConfiguration(int streamListenPort) {
/* 112 */     this(streamListenPort, true);
/*     */   }
/*     */   
/*     */   protected DefaultUpnpServiceConfiguration(boolean checkRuntime) {
/* 116 */     this(0, checkRuntime);
/*     */   }
/*     */   
/*     */   protected DefaultUpnpServiceConfiguration(int streamListenPort, boolean checkRuntime) {
/* 120 */     if (checkRuntime && ModelUtil.ANDROID_RUNTIME) {
/* 121 */       throw new Error("Unsupported runtime environment, use org.fourthline.cling.android.AndroidUpnpServiceConfiguration");
/*     */     }
/*     */     
/* 124 */     this.streamListenPort = streamListenPort;
/*     */     
/* 126 */     this.defaultExecutorService = createDefaultExecutorService();
/*     */     
/* 128 */     this.datagramProcessor = createDatagramProcessor();
/* 129 */     this.soapActionProcessor = createSOAPActionProcessor();
/* 130 */     this.genaEventProcessor = createGENAEventProcessor();
/*     */     
/* 132 */     this.deviceDescriptorBinderUDA10 = createDeviceDescriptorBinderUDA10();
/* 133 */     this.serviceDescriptorBinderUDA10 = createServiceDescriptorBinderUDA10();
/*     */     
/* 135 */     this.namespace = createNamespace();
/*     */   }
/*     */   
/*     */   public DatagramProcessor getDatagramProcessor() {
/* 139 */     return this.datagramProcessor;
/*     */   }
/*     */   
/*     */   public SOAPActionProcessor getSoapActionProcessor() {
/* 143 */     return this.soapActionProcessor;
/*     */   }
/*     */   
/*     */   public GENAEventProcessor getGenaEventProcessor() {
/* 147 */     return this.genaEventProcessor;
/*     */   }
/*     */   
/*     */   public StreamClient createStreamClient() {
/* 151 */     return (StreamClient)new StreamClientImpl(new StreamClientConfigurationImpl(
/*     */           
/* 153 */           getSyncProtocolExecutorService()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MulticastReceiver createMulticastReceiver(NetworkAddressFactory networkAddressFactory) {
/* 159 */     return (MulticastReceiver)new MulticastReceiverImpl(new MulticastReceiverConfigurationImpl(networkAddressFactory
/*     */           
/* 161 */           .getMulticastGroup(), networkAddressFactory
/* 162 */           .getMulticastPort()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramIO createDatagramIO(NetworkAddressFactory networkAddressFactory) {
/* 168 */     return (DatagramIO)new DatagramIOImpl(new DatagramIOConfigurationImpl());
/*     */   }
/*     */   
/*     */   public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
/* 172 */     return (StreamServer)new StreamServerImpl(new StreamServerConfigurationImpl(networkAddressFactory
/*     */           
/* 174 */           .getStreamListenPort()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor getMulticastReceiverExecutor() {
/* 180 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public Executor getDatagramIOExecutor() {
/* 184 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public ExecutorService getStreamServerExecutorService() {
/* 188 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public DeviceDescriptorBinder getDeviceDescriptorBinderUDA10() {
/* 192 */     return this.deviceDescriptorBinderUDA10;
/*     */   }
/*     */   
/*     */   public ServiceDescriptorBinder getServiceDescriptorBinderUDA10() {
/* 196 */     return this.serviceDescriptorBinderUDA10;
/*     */   }
/*     */   
/*     */   public ServiceType[] getExclusiveServiceTypes() {
/* 200 */     return new ServiceType[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReceivedSubscriptionTimeoutIgnored() {
/* 207 */     return false;
/*     */   }
/*     */   
/*     */   public UpnpHeaders getDescriptorRetrievalHeaders(RemoteDeviceIdentity identity) {
/* 211 */     return null;
/*     */   }
/*     */   
/*     */   public UpnpHeaders getEventSubscriptionHeaders(RemoteService service) {
/* 215 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegistryMaintenanceIntervalMillis() {
/* 222 */     return 1000;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAliveIntervalMillis() {
/* 229 */     return 0;
/*     */   }
/*     */   
/*     */   public Integer getRemoteDeviceMaxAgeSeconds() {
/* 233 */     return null;
/*     */   }
/*     */   
/*     */   public Executor getAsyncProtocolExecutor() {
/* 237 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public ExecutorService getSyncProtocolExecutorService() {
/* 241 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public Namespace getNamespace() {
/* 245 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public Executor getRegistryMaintainerExecutor() {
/* 249 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public Executor getRegistryListenerExecutor() {
/* 253 */     return getDefaultExecutorService();
/*     */   }
/*     */   
/*     */   public NetworkAddressFactory createNetworkAddressFactory() {
/* 257 */     return createNetworkAddressFactory(this.streamListenPort);
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 261 */     log.fine("Shutting down default executor service");
/* 262 */     getDefaultExecutorService().shutdownNow();
/*     */   }
/*     */   
/*     */   protected NetworkAddressFactory createNetworkAddressFactory(int streamListenPort) {
/* 266 */     return (NetworkAddressFactory)new NetworkAddressFactoryImpl(streamListenPort);
/*     */   }
/*     */   
/*     */   protected DatagramProcessor createDatagramProcessor() {
/* 270 */     return (DatagramProcessor)new DatagramProcessorImpl();
/*     */   }
/*     */   
/*     */   protected SOAPActionProcessor createSOAPActionProcessor() {
/* 274 */     return (SOAPActionProcessor)new SOAPActionProcessorImpl();
/*     */   }
/*     */   
/*     */   protected GENAEventProcessor createGENAEventProcessor() {
/* 278 */     return (GENAEventProcessor)new GENAEventProcessorImpl();
/*     */   }
/*     */   
/*     */   protected DeviceDescriptorBinder createDeviceDescriptorBinderUDA10() {
/* 282 */     return (DeviceDescriptorBinder)new UDA10DeviceDescriptorBinderImpl();
/*     */   }
/*     */   
/*     */   protected ServiceDescriptorBinder createServiceDescriptorBinderUDA10() {
/* 286 */     return (ServiceDescriptorBinder)new UDA10ServiceDescriptorBinderImpl();
/*     */   }
/*     */   
/*     */   protected Namespace createNamespace() {
/* 290 */     return new Namespace();
/*     */   }
/*     */   
/*     */   protected ExecutorService getDefaultExecutorService() {
/* 294 */     return this.defaultExecutorService;
/*     */   }
/*     */   
/*     */   protected ExecutorService createDefaultExecutorService() {
/* 298 */     return new ClingExecutor();
/*     */   }
/*     */   
/*     */   public static class ClingExecutor
/*     */     extends ThreadPoolExecutor {
/*     */     public ClingExecutor() {
/* 304 */       this(new DefaultUpnpServiceConfiguration.ClingThreadFactory(), new ThreadPoolExecutor.DiscardPolicy()
/*     */           {
/*     */ 
/*     */             
/*     */             public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor)
/*     */             {
/* 310 */               DefaultUpnpServiceConfiguration.log.info("Thread pool rejected execution of " + runnable.getClass());
/* 311 */               super.rejectedExecution(runnable, threadPoolExecutor);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ClingExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedHandler) {
/* 319 */       super(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory, rejectedHandler);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void afterExecute(Runnable runnable, Throwable throwable) {
/* 331 */       super.afterExecute(runnable, throwable);
/* 332 */       if (throwable != null) {
/* 333 */         Throwable cause = Exceptions.unwrap(throwable);
/* 334 */         if (cause instanceof InterruptedException) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 341 */         DefaultUpnpServiceConfiguration.log.warning("Thread terminated " + runnable + " abruptly with exception: " + throwable);
/* 342 */         DefaultUpnpServiceConfiguration.log.warning("Root cause: " + cause);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ClingThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/*     */     protected final ThreadGroup group;
/* 351 */     protected final AtomicInteger threadNumber = new AtomicInteger(1);
/* 352 */     protected final String namePrefix = "cling-";
/*     */     
/*     */     public ClingThreadFactory() {
/* 355 */       SecurityManager s = System.getSecurityManager();
/* 356 */       this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Thread newThread(Runnable r) {
/* 362 */       Thread t = new Thread(this.group, r, "cling-" + this.threadNumber.getAndIncrement(), 0L);
/*     */ 
/*     */       
/* 365 */       if (t.isDaemon())
/* 366 */         t.setDaemon(false); 
/* 367 */       if (t.getPriority() != 5) {
/* 368 */         t.setPriority(5);
/*     */       }
/* 370 */       return t;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\DefaultUpnpServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */