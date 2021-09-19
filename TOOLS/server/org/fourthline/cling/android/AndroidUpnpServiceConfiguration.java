/*     */ package org.fourthline.cling.android;
/*     */ 
/*     */ import android.os.Build;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import org.fourthline.cling.DefaultUpnpServiceConfiguration;
/*     */ import org.fourthline.cling.binding.xml.DeviceDescriptorBinder;
/*     */ import org.fourthline.cling.binding.xml.RecoveringUDA10DeviceDescriptorBinderImpl;
/*     */ import org.fourthline.cling.binding.xml.ServiceDescriptorBinder;
/*     */ import org.fourthline.cling.binding.xml.UDA10ServiceDescriptorBinderSAXImpl;
/*     */ import org.fourthline.cling.model.Namespace;
/*     */ import org.fourthline.cling.model.ServerClientTokens;
/*     */ import org.fourthline.cling.transport.impl.AsyncServletStreamServerConfigurationImpl;
/*     */ import org.fourthline.cling.transport.impl.AsyncServletStreamServerImpl;
/*     */ import org.fourthline.cling.transport.impl.RecoveringGENAEventProcessorImpl;
/*     */ import org.fourthline.cling.transport.impl.RecoveringSOAPActionProcessorImpl;
/*     */ import org.fourthline.cling.transport.impl.jetty.JettyServletContainer;
/*     */ import org.fourthline.cling.transport.impl.jetty.StreamClientConfigurationImpl;
/*     */ import org.fourthline.cling.transport.impl.jetty.StreamClientImpl;
/*     */ import org.fourthline.cling.transport.spi.GENAEventProcessor;
/*     */ import org.fourthline.cling.transport.spi.NetworkAddressFactory;
/*     */ import org.fourthline.cling.transport.spi.SOAPActionProcessor;
/*     */ import org.fourthline.cling.transport.spi.ServletContainerAdapter;
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
/*     */ public class AndroidUpnpServiceConfiguration
/*     */   extends DefaultUpnpServiceConfiguration
/*     */ {
/*     */   public AndroidUpnpServiceConfiguration() {
/*  65 */     this(0);
/*     */   }
/*     */   
/*     */   public AndroidUpnpServiceConfiguration(int streamListenPort) {
/*  69 */     super(streamListenPort, false);
/*     */ 
/*     */     
/*  72 */     System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
/*     */   }
/*     */ 
/*     */   
/*     */   protected NetworkAddressFactory createNetworkAddressFactory(int streamListenPort) {
/*  77 */     return (NetworkAddressFactory)new AndroidNetworkAddressFactory(streamListenPort);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Namespace createNamespace() {
/*  83 */     return new Namespace("/upnp");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamClient createStreamClient() {
/*  89 */     return (StreamClient)new StreamClientImpl(new StreamClientConfigurationImpl(
/*     */           
/*  91 */           getSyncProtocolExecutorService())
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public String getUserAgentValue(int majorVersion, int minorVersion)
/*     */           {
/*  98 */             ServerClientTokens tokens = new ServerClientTokens(majorVersion, minorVersion);
/*  99 */             tokens.setOsName("Android");
/* 100 */             tokens.setOsVersion(Build.VERSION.RELEASE);
/* 101 */             return tokens.toString();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
/* 110 */     return (StreamServer)new AsyncServletStreamServerImpl(new AsyncServletStreamServerConfigurationImpl((ServletContainerAdapter)JettyServletContainer.INSTANCE, networkAddressFactory
/*     */ 
/*     */           
/* 113 */           .getStreamListenPort()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DeviceDescriptorBinder createDeviceDescriptorBinderUDA10() {
/* 120 */     return (DeviceDescriptorBinder)new RecoveringUDA10DeviceDescriptorBinderImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ServiceDescriptorBinder createServiceDescriptorBinderUDA10() {
/* 125 */     return (ServiceDescriptorBinder)new UDA10ServiceDescriptorBinderSAXImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SOAPActionProcessor createSOAPActionProcessor() {
/* 130 */     return (SOAPActionProcessor)new RecoveringSOAPActionProcessorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   protected GENAEventProcessor createGENAEventProcessor() {
/* 135 */     return (GENAEventProcessor)new RecoveringGENAEventProcessorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRegistryMaintenanceIntervalMillis() {
/* 140 */     return 3000;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\android\AndroidUpnpServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */