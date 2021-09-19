/*     */ package org.fourthline.cling.mock;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.AbstractExecutorService;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.enterprise.inject.Alternative;
/*     */ import org.fourthline.cling.DefaultUpnpServiceConfiguration;
/*     */ import org.fourthline.cling.transport.impl.NetworkAddressFactoryImpl;
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
/*     */ @Alternative
/*     */ public class MockUpnpServiceConfiguration
/*     */   extends DefaultUpnpServiceConfiguration
/*     */ {
/*     */   protected final boolean maintainsRegistry;
/*     */   protected final boolean multiThreaded;
/*     */   
/*     */   public MockUpnpServiceConfiguration() {
/*  45 */     this(false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MockUpnpServiceConfiguration(boolean maintainsRegistry) {
/*  52 */     this(maintainsRegistry, false);
/*     */   }
/*     */   
/*     */   public MockUpnpServiceConfiguration(boolean maintainsRegistry, boolean multiThreaded) {
/*  56 */     super(false);
/*  57 */     this.maintainsRegistry = maintainsRegistry;
/*  58 */     this.multiThreaded = multiThreaded;
/*     */   }
/*     */   
/*     */   public boolean isMaintainsRegistry() {
/*  62 */     return this.maintainsRegistry;
/*     */   }
/*     */   
/*     */   public boolean isMultiThreaded() {
/*  66 */     return this.multiThreaded;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NetworkAddressFactory createNetworkAddressFactory(int streamListenPort) {
/*  72 */     return (NetworkAddressFactory)new NetworkAddressFactoryImpl(streamListenPort)
/*     */       {
/*     */         protected boolean isUsableNetworkInterface(NetworkInterface iface) throws Exception {
/*  75 */           return iface.isLoopback();
/*     */         }
/*     */ 
/*     */         
/*     */         protected boolean isUsableAddress(NetworkInterface networkInterface, InetAddress address) {
/*  80 */           return (address.isLoopbackAddress() && address instanceof java.net.Inet4Address);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor getRegistryMaintainerExecutor() {
/*  88 */     if (isMaintainsRegistry()) {
/*  89 */       return new Executor() {
/*     */           public void execute(Runnable runnable) {
/*  91 */             (new Thread(runnable)).start();
/*     */           }
/*     */         };
/*     */     }
/*  95 */     return getDefaultExecutorService();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ExecutorService getDefaultExecutorService() {
/* 100 */     if (isMultiThreaded()) {
/* 101 */       return super.getDefaultExecutorService();
/*     */     }
/* 103 */     return new AbstractExecutorService()
/*     */       {
/*     */         boolean terminated;
/*     */         
/*     */         public void shutdown() {
/* 108 */           this.terminated = true;
/*     */         }
/*     */         
/*     */         public List<Runnable> shutdownNow() {
/* 112 */           shutdown();
/* 113 */           return null;
/*     */         }
/*     */         
/*     */         public boolean isShutdown() {
/* 117 */           return this.terminated;
/*     */         }
/*     */         
/*     */         public boolean isTerminated() {
/* 121 */           return this.terminated;
/*     */         }
/*     */         
/*     */         public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
/* 125 */           shutdown();
/* 126 */           return this.terminated;
/*     */         }
/*     */         
/*     */         public void execute(Runnable runnable) {
/* 130 */           runnable.run();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\mock\MockUpnpServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */