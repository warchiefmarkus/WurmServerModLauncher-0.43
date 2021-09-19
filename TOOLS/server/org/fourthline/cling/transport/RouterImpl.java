/*     */ package org.fourthline.cling.transport;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.context.ApplicationScoped;
/*     */ import javax.enterprise.event.Observes;
/*     */ import javax.enterprise.inject.Default;
/*     */ import javax.inject.Inject;
/*     */ import org.fourthline.cling.UpnpServiceConfiguration;
/*     */ import org.fourthline.cling.model.NetworkAddress;
/*     */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.protocol.ProtocolCreationException;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.protocol.ReceivingAsync;
/*     */ import org.fourthline.cling.transport.spi.DatagramIO;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.fourthline.cling.transport.spi.MulticastReceiver;
/*     */ import org.fourthline.cling.transport.spi.NetworkAddressFactory;
/*     */ import org.fourthline.cling.transport.spi.NoNetworkException;
/*     */ import org.fourthline.cling.transport.spi.StreamClient;
/*     */ import org.fourthline.cling.transport.spi.StreamServer;
/*     */ import org.fourthline.cling.transport.spi.UpnpStream;
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
/*     */ @ApplicationScoped
/*     */ public class RouterImpl
/*     */   implements Router
/*     */ {
/*  68 */   private static Logger log = Logger.getLogger(Router.class.getName());
/*     */   
/*     */   protected UpnpServiceConfiguration configuration;
/*     */   
/*     */   protected ProtocolFactory protocolFactory;
/*     */   protected volatile boolean enabled;
/*  74 */   protected ReentrantReadWriteLock routerLock = new ReentrantReadWriteLock(true);
/*  75 */   protected Lock readLock = this.routerLock.readLock();
/*  76 */   protected Lock writeLock = this.routerLock.writeLock();
/*     */   
/*     */   protected NetworkAddressFactory networkAddressFactory;
/*     */   
/*     */   protected StreamClient streamClient;
/*  81 */   protected final Map<NetworkInterface, MulticastReceiver> multicastReceivers = new HashMap<>();
/*  82 */   protected final Map<InetAddress, DatagramIO> datagramIOs = new HashMap<>();
/*  83 */   protected final Map<InetAddress, StreamServer> streamServers = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RouterImpl() {}
/*     */ 
/*     */ 
/*     */   
/*     */   @Inject
/*     */   public RouterImpl(UpnpServiceConfiguration configuration, ProtocolFactory protocolFactory) {
/*  94 */     log.info("Creating Router: " + getClass().getName());
/*  95 */     this.configuration = configuration;
/*  96 */     this.protocolFactory = protocolFactory;
/*     */   }
/*     */   
/*     */   public boolean enable(@Observes @Default EnableRouter event) throws RouterException {
/* 100 */     return enable();
/*     */   }
/*     */   
/*     */   public boolean disable(@Observes @Default DisableRouter event) throws RouterException {
/* 104 */     return disable();
/*     */   }
/*     */   
/*     */   public UpnpServiceConfiguration getConfiguration() {
/* 108 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/* 112 */     return this.protocolFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean enable() throws RouterException {
/* 124 */     lock(this.writeLock);
/*     */     try {
/* 126 */       if (!this.enabled) {
/*     */         try {
/* 128 */           log.fine("Starting networking services...");
/* 129 */           this.networkAddressFactory = getConfiguration().createNetworkAddressFactory();
/*     */           
/* 131 */           startInterfaceBasedTransports(this.networkAddressFactory.getNetworkInterfaces());
/* 132 */           startAddressBasedTransports(this.networkAddressFactory.getBindAddresses());
/*     */ 
/*     */           
/* 135 */           if (!this.networkAddressFactory.hasUsableNetwork()) {
/* 136 */             throw new NoNetworkException("No usable network interface and/or addresses available, check the log for errors.");
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 142 */           this.streamClient = getConfiguration().createStreamClient();
/*     */           
/* 144 */           this.enabled = true;
/* 145 */           return true;
/* 146 */         } catch (InitializationException ex) {
/* 147 */           handleStartFailure(ex);
/*     */         } 
/*     */       }
/* 150 */       return false;
/*     */     } finally {
/* 152 */       unlock(this.writeLock);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disable() throws RouterException {
/* 158 */     lock(this.writeLock);
/*     */     try {
/* 160 */       if (this.enabled) {
/* 161 */         log.fine("Disabling network services...");
/*     */         
/* 163 */         if (this.streamClient != null) {
/* 164 */           log.fine("Stopping stream client connection management/pool");
/* 165 */           this.streamClient.stop();
/* 166 */           this.streamClient = null;
/*     */         } 
/*     */         
/* 169 */         for (Map.Entry<InetAddress, StreamServer> entry : this.streamServers.entrySet()) {
/* 170 */           log.fine("Stopping stream server on address: " + entry.getKey());
/* 171 */           ((StreamServer)entry.getValue()).stop();
/*     */         } 
/* 173 */         this.streamServers.clear();
/*     */         
/* 175 */         for (Map.Entry<NetworkInterface, MulticastReceiver> entry : this.multicastReceivers.entrySet()) {
/* 176 */           log.fine("Stopping multicast receiver on interface: " + ((NetworkInterface)entry.getKey()).getDisplayName());
/* 177 */           ((MulticastReceiver)entry.getValue()).stop();
/*     */         } 
/* 179 */         this.multicastReceivers.clear();
/*     */         
/* 181 */         for (Map.Entry<InetAddress, DatagramIO> entry : this.datagramIOs.entrySet()) {
/* 182 */           log.fine("Stopping datagram I/O on address: " + entry.getKey());
/* 183 */           ((DatagramIO)entry.getValue()).stop();
/*     */         } 
/* 185 */         this.datagramIOs.clear();
/*     */         
/* 187 */         this.networkAddressFactory = null;
/* 188 */         this.enabled = false;
/* 189 */         return true;
/*     */       } 
/* 191 */       return false;
/*     */     } finally {
/* 193 */       unlock(this.writeLock);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws RouterException {
/* 199 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/* 204 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStartFailure(InitializationException ex) throws InitializationException {
/* 209 */     if (ex instanceof NoNetworkException) {
/* 210 */       log.info("Unable to initialize network router, no network found.");
/*     */     } else {
/* 212 */       log.severe("Unable to initialize network router: " + ex);
/* 213 */       log.severe("Cause: " + Exceptions.unwrap((Throwable)ex));
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<NetworkAddress> getActiveStreamServers(InetAddress preferredAddress) throws RouterException {
/* 218 */     lock(this.readLock);
/*     */     try {
/* 220 */       if (this.enabled && this.streamServers.size() > 0) {
/* 221 */         List<NetworkAddress> streamServerAddresses = new ArrayList<>();
/*     */         
/*     */         StreamServer preferredServer;
/* 224 */         if (preferredAddress != null && (
/* 225 */           preferredServer = this.streamServers.get(preferredAddress)) != null) {
/* 226 */           streamServerAddresses.add(new NetworkAddress(preferredAddress, preferredServer
/*     */ 
/*     */                 
/* 229 */                 .getPort(), this.networkAddressFactory
/* 230 */                 .getHardwareAddress(preferredAddress)));
/*     */ 
/*     */ 
/*     */           
/* 234 */           return streamServerAddresses;
/*     */         } 
/*     */         
/* 237 */         for (Map.Entry<InetAddress, StreamServer> entry : this.streamServers.entrySet()) {
/* 238 */           byte[] hardwareAddress = this.networkAddressFactory.getHardwareAddress(entry.getKey());
/* 239 */           streamServerAddresses.add(new NetworkAddress(entry
/* 240 */                 .getKey(), ((StreamServer)entry.getValue()).getPort(), hardwareAddress));
/*     */         } 
/*     */         
/* 243 */         return streamServerAddresses;
/*     */       } 
/* 245 */       return Collections.EMPTY_LIST;
/*     */     } finally {
/*     */       
/* 248 */       unlock(this.readLock);
/*     */     } 
/*     */   }
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
/*     */   public void received(IncomingDatagramMessage msg) {
/* 265 */     if (!this.enabled) {
/* 266 */       log.fine("Router disabled, ignoring incoming message: " + msg);
/*     */       return;
/*     */     } 
/*     */     try {
/* 270 */       ReceivingAsync protocol = getProtocolFactory().createReceivingAsync(msg);
/* 271 */       if (protocol == null) {
/* 272 */         if (log.isLoggable(Level.FINEST))
/* 273 */           log.finest("No protocol, ignoring received message: " + msg); 
/*     */         return;
/*     */       } 
/* 276 */       if (log.isLoggable(Level.FINE))
/* 277 */         log.fine("Received asynchronous message: " + msg); 
/* 278 */       getConfiguration().getAsyncProtocolExecutor().execute((Runnable)protocol);
/* 279 */     } catch (ProtocolCreationException ex) {
/* 280 */       log.warning("Handling received datagram failed - " + Exceptions.unwrap((Throwable)ex).toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void received(UpnpStream stream) {
/* 291 */     if (!this.enabled) {
/* 292 */       log.fine("Router disabled, ignoring incoming: " + stream);
/*     */       return;
/*     */     } 
/* 295 */     log.fine("Received synchronous stream: " + stream);
/* 296 */     getConfiguration().getSyncProtocolExecutorService().execute((Runnable)stream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(OutgoingDatagramMessage msg) throws RouterException {
/* 305 */     lock(this.readLock);
/*     */     try {
/* 307 */       if (this.enabled) {
/* 308 */         for (DatagramIO datagramIO : this.datagramIOs.values()) {
/* 309 */           datagramIO.send(msg);
/*     */         }
/*     */       } else {
/* 312 */         log.fine("Router disabled, not sending datagram: " + msg);
/*     */       } 
/*     */     } finally {
/* 315 */       unlock(this.readLock);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamResponseMessage send(StreamRequestMessage msg) throws RouterException {
/* 327 */     lock(this.readLock);
/*     */     try {
/* 329 */       if (this.enabled) {
/* 330 */         if (this.streamClient == null) {
/* 331 */           log.fine("No StreamClient available, not sending: " + msg);
/* 332 */           return null;
/*     */         } 
/* 334 */         log.fine("Sending via TCP unicast stream: " + msg);
/*     */         try {
/* 336 */           return this.streamClient.sendRequest(msg);
/* 337 */         } catch (InterruptedException ex) {
/* 338 */           throw new RouterException("Sending stream request was interrupted", ex);
/*     */         } 
/*     */       } 
/* 341 */       log.fine("Router disabled, not sending stream request: " + msg);
/* 342 */       return null;
/*     */     } finally {
/*     */       
/* 345 */       unlock(this.readLock);
/*     */     } 
/*     */   }
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
/*     */   public void broadcast(byte[] bytes) throws RouterException {
/* 359 */     lock(this.readLock);
/*     */     try {
/* 361 */       if (this.enabled) {
/* 362 */         for (Map.Entry<InetAddress, DatagramIO> entry : this.datagramIOs.entrySet()) {
/* 363 */           InetAddress broadcast = this.networkAddressFactory.getBroadcastAddress(entry.getKey());
/* 364 */           if (broadcast != null) {
/* 365 */             log.fine("Sending UDP datagram to broadcast address: " + broadcast.getHostAddress());
/* 366 */             DatagramPacket packet = new DatagramPacket(bytes, bytes.length, broadcast, 9);
/* 367 */             ((DatagramIO)entry.getValue()).send(packet);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 371 */         log.fine("Router disabled, not broadcasting bytes: " + bytes.length);
/*     */       } 
/*     */     } finally {
/* 374 */       unlock(this.readLock);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void startInterfaceBasedTransports(Iterator<NetworkInterface> interfaces) throws InitializationException {
/* 379 */     while (interfaces.hasNext()) {
/* 380 */       NetworkInterface networkInterface = interfaces.next();
/*     */ 
/*     */       
/* 383 */       MulticastReceiver multicastReceiver = getConfiguration().createMulticastReceiver(this.networkAddressFactory);
/* 384 */       if (multicastReceiver == null) {
/* 385 */         log.info("Configuration did not create a MulticastReceiver for: " + networkInterface); continue;
/*     */       } 
/*     */       try {
/* 388 */         if (log.isLoggable(Level.FINE))
/* 389 */           log.fine("Init multicast receiver on interface: " + networkInterface.getDisplayName()); 
/* 390 */         multicastReceiver.init(networkInterface, this, this.networkAddressFactory, 
/*     */ 
/*     */ 
/*     */             
/* 394 */             getConfiguration().getDatagramProcessor());
/*     */ 
/*     */         
/* 397 */         this.multicastReceivers.put(networkInterface, multicastReceiver);
/* 398 */       } catch (InitializationException ex) {
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
/* 410 */         throw ex;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 415 */     for (Map.Entry<NetworkInterface, MulticastReceiver> entry : this.multicastReceivers.entrySet()) {
/* 416 */       if (log.isLoggable(Level.FINE))
/* 417 */         log.fine("Starting multicast receiver on interface: " + ((NetworkInterface)entry.getKey()).getDisplayName()); 
/* 418 */       getConfiguration().getMulticastReceiverExecutor().execute((Runnable)entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void startAddressBasedTransports(Iterator<InetAddress> addresses) throws InitializationException {
/* 423 */     while (addresses.hasNext()) {
/* 424 */       InetAddress address = addresses.next();
/*     */ 
/*     */       
/* 427 */       StreamServer streamServer = getConfiguration().createStreamServer(this.networkAddressFactory);
/* 428 */       if (streamServer == null) {
/* 429 */         log.info("Configuration did not create a StreamServer for: " + address);
/*     */       } else {
/*     */         try {
/* 432 */           if (log.isLoggable(Level.FINE))
/* 433 */             log.fine("Init stream server on address: " + address); 
/* 434 */           streamServer.init(address, this);
/* 435 */           this.streamServers.put(address, streamServer);
/* 436 */         } catch (InitializationException ex) {
/*     */           
/* 438 */           Throwable cause = Exceptions.unwrap((Throwable)ex);
/* 439 */           if (cause instanceof java.net.BindException) {
/* 440 */             log.warning("Failed to init StreamServer: " + cause);
/* 441 */             if (log.isLoggable(Level.FINE))
/* 442 */               log.log(Level.FINE, "Initialization exception root cause", cause); 
/* 443 */             log.warning("Removing unusable address: " + address);
/* 444 */             addresses.remove();
/*     */             continue;
/*     */           } 
/* 447 */           throw ex;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 452 */       DatagramIO datagramIO = getConfiguration().createDatagramIO(this.networkAddressFactory);
/* 453 */       if (datagramIO == null) {
/* 454 */         log.info("Configuration did not create a StreamServer for: " + address); continue;
/*     */       } 
/*     */       try {
/* 457 */         if (log.isLoggable(Level.FINE))
/* 458 */           log.fine("Init datagram I/O on address: " + address); 
/* 459 */         datagramIO.init(address, this, getConfiguration().getDatagramProcessor());
/* 460 */         this.datagramIOs.put(address, datagramIO);
/* 461 */       } catch (InitializationException ex) {
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
/* 473 */         throw ex;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 478 */     for (Map.Entry<InetAddress, StreamServer> entry : this.streamServers.entrySet()) {
/* 479 */       if (log.isLoggable(Level.FINE))
/* 480 */         log.fine("Starting stream server on address: " + entry.getKey()); 
/* 481 */       getConfiguration().getStreamServerExecutorService().execute((Runnable)entry.getValue());
/*     */     } 
/*     */     
/* 484 */     for (Map.Entry<InetAddress, DatagramIO> entry : this.datagramIOs.entrySet()) {
/* 485 */       if (log.isLoggable(Level.FINE))
/* 486 */         log.fine("Starting datagram I/O on address: " + entry.getKey()); 
/* 487 */       getConfiguration().getDatagramIOExecutor().execute((Runnable)entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void lock(Lock lock, int timeoutMilliseconds) throws RouterException {
/*     */     try {
/* 493 */       log.finest("Trying to obtain lock with timeout milliseconds '" + timeoutMilliseconds + "': " + lock.getClass().getSimpleName());
/* 494 */       if (lock.tryLock(timeoutMilliseconds, TimeUnit.MILLISECONDS)) {
/* 495 */         log.finest("Acquired router lock: " + lock.getClass().getSimpleName());
/*     */       } else {
/* 497 */         throw new RouterException("Router wasn't available exclusively after waiting " + timeoutMilliseconds + "ms, lock failed: " + lock
/*     */             
/* 499 */             .getClass().getSimpleName());
/*     */       }
/*     */     
/* 502 */     } catch (InterruptedException ex) {
/* 503 */       throw new RouterException("Interruption while waiting for exclusive access: " + lock
/* 504 */           .getClass().getSimpleName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void lock(Lock lock) throws RouterException {
/* 510 */     lock(lock, getLockTimeoutMillis());
/*     */   }
/*     */   
/*     */   protected void unlock(Lock lock) {
/* 514 */     log.finest("Releasing router lock: " + lock.getClass().getSimpleName());
/* 515 */     lock.unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLockTimeoutMillis() {
/* 522 */     return 6000;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\RouterImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */