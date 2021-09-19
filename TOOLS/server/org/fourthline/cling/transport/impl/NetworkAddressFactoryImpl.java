/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InterfaceAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.fourthline.cling.transport.spi.NetworkAddressFactory;
/*     */ import org.fourthline.cling.transport.spi.NoNetworkException;
/*     */ import org.seamless.util.Iterators;
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
/*     */ public class NetworkAddressFactoryImpl
/*     */   implements NetworkAddressFactory
/*     */ {
/*     */   public static final int DEFAULT_TCP_HTTP_LISTEN_PORT = 0;
/*  57 */   private static Logger log = Logger.getLogger(NetworkAddressFactoryImpl.class.getName());
/*     */   
/*  59 */   protected final Set<String> useInterfaces = new HashSet<>();
/*  60 */   protected final Set<String> useAddresses = new HashSet<>();
/*     */   
/*  62 */   protected final List<NetworkInterface> networkInterfaces = new ArrayList<>();
/*  63 */   protected final List<InetAddress> bindAddresses = new ArrayList<>();
/*     */ 
/*     */   
/*     */   protected int streamListenPort;
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkAddressFactoryImpl() throws InitializationException {
/*  71 */     this(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public NetworkAddressFactoryImpl(int streamListenPort) throws InitializationException {
/*  76 */     System.setProperty("java.net.preferIPv4Stack", "true");
/*     */     
/*  78 */     String useInterfacesString = System.getProperty("org.fourthline.cling.network.useInterfaces");
/*  79 */     if (useInterfacesString != null) {
/*  80 */       String[] userInterfacesStrings = useInterfacesString.split(",");
/*  81 */       this.useInterfaces.addAll(Arrays.asList(userInterfacesStrings));
/*     */     } 
/*     */     
/*  84 */     String useAddressesString = System.getProperty("org.fourthline.cling.network.useAddresses");
/*  85 */     if (useAddressesString != null) {
/*  86 */       String[] useAddressesStrings = useAddressesString.split(",");
/*  87 */       this.useAddresses.addAll(Arrays.asList(useAddressesStrings));
/*     */     } 
/*     */     
/*  90 */     discoverNetworkInterfaces();
/*  91 */     discoverBindAddresses();
/*     */     
/*  93 */     if (this.networkInterfaces.size() == 0 || this.bindAddresses.size() == 0) {
/*  94 */       log.warning("No usable network interface or addresses found");
/*  95 */       if (requiresNetworkInterface()) {
/*  96 */         throw new NoNetworkException("Could not discover any usable network interfaces and/or addresses");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 102 */     this.streamListenPort = streamListenPort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean requiresNetworkInterface() {
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   public void logInterfaceInformation() {
/* 113 */     synchronized (this.networkInterfaces) {
/* 114 */       if (this.networkInterfaces.isEmpty()) {
/* 115 */         log.info("No network interface to display!");
/*     */         return;
/*     */       } 
/* 118 */       for (NetworkInterface networkInterface : this.networkInterfaces) {
/*     */         try {
/* 120 */           logInterfaceInformation(networkInterface);
/* 121 */         } catch (SocketException ex) {
/* 122 */           log.log(Level.WARNING, "Exception while logging network interface information", ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public InetAddress getMulticastGroup() {
/*     */     try {
/* 130 */       return InetAddress.getByName("239.255.255.250");
/* 131 */     } catch (UnknownHostException ex) {
/* 132 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMulticastPort() {
/* 137 */     return 1900;
/*     */   }
/*     */   
/*     */   public int getStreamListenPort() {
/* 141 */     return this.streamListenPort;
/*     */   }
/*     */   
/*     */   public Iterator<NetworkInterface> getNetworkInterfaces() {
/* 145 */     return (Iterator<NetworkInterface>)new Iterators.Synchronized<NetworkInterface>(this.networkInterfaces)
/*     */       {
/*     */         protected void synchronizedRemove(int index) {
/* 148 */           synchronized (NetworkAddressFactoryImpl.this.networkInterfaces) {
/* 149 */             NetworkAddressFactoryImpl.this.networkInterfaces.remove(index);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<InetAddress> getBindAddresses() {
/* 156 */     return (Iterator<InetAddress>)new Iterators.Synchronized<InetAddress>(this.bindAddresses)
/*     */       {
/*     */         protected void synchronizedRemove(int index) {
/* 159 */           synchronized (NetworkAddressFactoryImpl.this.bindAddresses) {
/* 160 */             NetworkAddressFactoryImpl.this.bindAddresses.remove(index);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean hasUsableNetwork() {
/* 167 */     return (this.networkInterfaces.size() > 0 && this.bindAddresses.size() > 0);
/*     */   }
/*     */   
/*     */   public byte[] getHardwareAddress(InetAddress inetAddress) {
/*     */     try {
/* 172 */       NetworkInterface iface = NetworkInterface.getByInetAddress(inetAddress);
/* 173 */       return (iface != null) ? iface.getHardwareAddress() : null;
/* 174 */     } catch (Throwable ex) {
/* 175 */       log.log(Level.WARNING, "Cannot get hardware address for: " + inetAddress, ex);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 182 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public InetAddress getBroadcastAddress(InetAddress inetAddress) {
/* 187 */     synchronized (this.networkInterfaces) {
/* 188 */       for (NetworkInterface iface : this.networkInterfaces) {
/* 189 */         for (InterfaceAddress interfaceAddress : getInterfaceAddresses(iface)) {
/* 190 */           if (interfaceAddress != null && interfaceAddress.getAddress().equals(inetAddress)) {
/* 191 */             return interfaceAddress.getBroadcast();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 196 */     return null;
/*     */   }
/*     */   
/*     */   public Short getAddressNetworkPrefixLength(InetAddress inetAddress) {
/* 200 */     synchronized (this.networkInterfaces) {
/* 201 */       for (NetworkInterface iface : this.networkInterfaces) {
/* 202 */         for (InterfaceAddress interfaceAddress : getInterfaceAddresses(iface)) {
/* 203 */           if (interfaceAddress != null && interfaceAddress.getAddress().equals(inetAddress)) {
/* 204 */             short prefix = interfaceAddress.getNetworkPrefixLength();
/* 205 */             if (prefix > 0 && prefix < 32) return Short.valueOf(prefix); 
/* 206 */             return null;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress(NetworkInterface networkInterface, boolean isIPv6, InetAddress remoteAddress) {
/* 217 */     InetAddress localIPInSubnet = getBindAddressInSubnetOf(remoteAddress);
/* 218 */     if (localIPInSubnet != null) return localIPInSubnet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     log.finer("Could not find local bind address in same subnet as: " + remoteAddress.getHostAddress());
/*     */ 
/*     */     
/* 230 */     for (InetAddress interfaceAddress : getInetAddresses(networkInterface)) {
/* 231 */       if (isIPv6 && interfaceAddress instanceof java.net.Inet6Address)
/* 232 */         return interfaceAddress; 
/* 233 */       if (!isIPv6 && interfaceAddress instanceof java.net.Inet4Address)
/* 234 */         return interfaceAddress; 
/*     */     } 
/* 236 */     throw new IllegalStateException("Can't find any IPv4 or IPv6 address on interface: " + networkInterface.getDisplayName());
/*     */   }
/*     */   
/*     */   protected List<InterfaceAddress> getInterfaceAddresses(NetworkInterface networkInterface) {
/* 240 */     return networkInterface.getInterfaceAddresses();
/*     */   }
/*     */   
/*     */   protected List<InetAddress> getInetAddresses(NetworkInterface networkInterface) {
/* 244 */     return Collections.list(networkInterface.getInetAddresses());
/*     */   }
/*     */   
/*     */   protected InetAddress getBindAddressInSubnetOf(InetAddress inetAddress) {
/* 248 */     synchronized (this.networkInterfaces) {
/* 249 */       for (NetworkInterface iface : this.networkInterfaces) {
/* 250 */         for (InterfaceAddress ifaceAddress : getInterfaceAddresses(iface)) {
/*     */           
/* 252 */           synchronized (this.bindAddresses) {
/* 253 */             if (ifaceAddress == null || !this.bindAddresses.contains(ifaceAddress.getAddress())) {
/*     */               continue;
/*     */             }
/*     */           } 
/*     */           
/* 258 */           if (isInSubnet(inetAddress
/* 259 */               .getAddress(), ifaceAddress
/* 260 */               .getAddress().getAddress(), ifaceAddress
/* 261 */               .getNetworkPrefixLength()))
/*     */           {
/* 263 */             return ifaceAddress.getAddress();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 269 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean isInSubnet(byte[] ip, byte[] network, short prefix) {
/* 273 */     if (ip.length != network.length) {
/* 274 */       return false;
/*     */     }
/*     */     
/* 277 */     if (prefix / 8 > ip.length) {
/* 278 */       return false;
/*     */     }
/*     */     
/* 281 */     int i = 0;
/* 282 */     while (prefix >= 8 && i < ip.length) {
/* 283 */       if (ip[i] != network[i]) {
/* 284 */         return false;
/*     */       }
/* 286 */       i++;
/* 287 */       prefix = (short)(prefix - 8);
/*     */     } 
/* 289 */     if (i == ip.length) return true; 
/* 290 */     byte mask = (byte)((1 << 8 - prefix) - 1 ^ 0xFFFFFFFF);
/*     */     
/* 292 */     return ((ip[i] & mask) == (network[i] & mask));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void discoverNetworkInterfaces() throws InitializationException {
/*     */     try {
/* 298 */       Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
/* 299 */       for (NetworkInterface iface : Collections.<NetworkInterface>list(interfaceEnumeration))
/*     */       {
/*     */         
/* 302 */         log.finer("Analyzing network interface: " + iface.getDisplayName());
/* 303 */         if (isUsableNetworkInterface(iface)) {
/* 304 */           log.fine("Discovered usable network interface: " + iface.getDisplayName());
/* 305 */           synchronized (this.networkInterfaces) {
/* 306 */             this.networkInterfaces.add(iface);
/*     */           }  continue;
/*     */         } 
/* 309 */         log.finer("Ignoring non-usable network interface: " + iface.getDisplayName());
/*     */       }
/*     */     
/*     */     }
/* 313 */     catch (Exception ex) {
/* 314 */       throw new InitializationException("Could not not analyze local network interfaces: " + ex, ex);
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
/*     */   protected boolean isUsableNetworkInterface(NetworkInterface iface) throws Exception {
/* 342 */     if (!iface.isUp()) {
/* 343 */       log.finer("Skipping network interface (down): " + iface.getDisplayName());
/* 344 */       return false;
/*     */     } 
/*     */     
/* 347 */     if (getInetAddresses(iface).size() == 0) {
/* 348 */       log.finer("Skipping network interface without bound IP addresses: " + iface.getDisplayName());
/* 349 */       return false;
/*     */     } 
/*     */     
/* 352 */     if (iface.getName().toLowerCase(Locale.ROOT).startsWith("vmnet") || (iface
/* 353 */       .getDisplayName() != null && iface.getDisplayName().toLowerCase(Locale.ROOT).contains("vmnet"))) {
/* 354 */       log.finer("Skipping network interface (VMWare): " + iface.getDisplayName());
/* 355 */       return false;
/*     */     } 
/*     */     
/* 358 */     if (iface.getName().toLowerCase(Locale.ROOT).startsWith("vnic")) {
/* 359 */       log.finer("Skipping network interface (Parallels): " + iface.getDisplayName());
/* 360 */       return false;
/*     */     } 
/*     */     
/* 363 */     if (iface.getName().toLowerCase(Locale.ROOT).startsWith("vboxnet")) {
/* 364 */       log.finer("Skipping network interface (Virtual Box): " + iface.getDisplayName());
/* 365 */       return false;
/*     */     } 
/*     */     
/* 368 */     if (iface.getName().toLowerCase(Locale.ROOT).contains("virtual")) {
/* 369 */       log.finer("Skipping network interface (named '*virtual*'): " + iface.getDisplayName());
/* 370 */       return false;
/*     */     } 
/*     */     
/* 373 */     if (iface.getName().toLowerCase(Locale.ROOT).startsWith("ppp")) {
/* 374 */       log.finer("Skipping network interface (PPP): " + iface.getDisplayName());
/* 375 */       return false;
/*     */     } 
/*     */     
/* 378 */     if (iface.isLoopback()) {
/* 379 */       log.finer("Skipping network interface (ignoring loopback): " + iface.getDisplayName());
/* 380 */       return false;
/*     */     } 
/*     */     
/* 383 */     if (this.useInterfaces.size() > 0 && !this.useInterfaces.contains(iface.getName())) {
/* 384 */       log.finer("Skipping unwanted network interface (-Dorg.fourthline.cling.network.useInterfaces): " + iface.getName());
/* 385 */       return false;
/*     */     } 
/*     */     
/* 388 */     if (!iface.supportsMulticast()) {
/* 389 */       log.warning("Network interface may not be multicast capable: " + iface.getDisplayName());
/*     */     }
/* 391 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void discoverBindAddresses() throws InitializationException {
/*     */     try {
/* 397 */       synchronized (this.networkInterfaces) {
/* 398 */         Iterator<NetworkInterface> it = this.networkInterfaces.iterator();
/* 399 */         while (it.hasNext()) {
/* 400 */           NetworkInterface networkInterface = it.next();
/*     */           
/* 402 */           log.finer("Discovering addresses of interface: " + networkInterface.getDisplayName());
/* 403 */           int usableAddresses = 0;
/* 404 */           for (InetAddress inetAddress : getInetAddresses(networkInterface)) {
/* 405 */             if (inetAddress == null) {
/* 406 */               log.warning("Network has a null address: " + networkInterface.getDisplayName());
/*     */               
/*     */               continue;
/*     */             } 
/* 410 */             if (isUsableAddress(networkInterface, inetAddress)) {
/* 411 */               log.fine("Discovered usable network interface address: " + inetAddress.getHostAddress());
/* 412 */               usableAddresses++;
/* 413 */               synchronized (this.bindAddresses) {
/* 414 */                 this.bindAddresses.add(inetAddress);
/*     */               }  continue;
/*     */             } 
/* 417 */             log.finer("Ignoring non-usable network interface address: " + inetAddress.getHostAddress());
/*     */           } 
/*     */ 
/*     */           
/* 421 */           if (usableAddresses == 0) {
/* 422 */             log.finer("Network interface has no usable addresses, removing: " + networkInterface.getDisplayName());
/* 423 */             it.remove();
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 428 */     } catch (Exception ex) {
/* 429 */       throw new InitializationException("Could not not analyze local network interfaces: " + ex, ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUsableAddress(NetworkInterface networkInterface, InetAddress address) {
/* 451 */     if (!(address instanceof java.net.Inet4Address)) {
/* 452 */       log.finer("Skipping unsupported non-IPv4 address: " + address);
/* 453 */       return false;
/*     */     } 
/*     */     
/* 456 */     if (address.isLoopbackAddress()) {
/* 457 */       log.finer("Skipping loopback address: " + address);
/* 458 */       return false;
/*     */     } 
/*     */     
/* 461 */     if (this.useAddresses.size() > 0 && !this.useAddresses.contains(address.getHostAddress())) {
/* 462 */       log.finer("Skipping unwanted address: " + address);
/* 463 */       return false;
/*     */     } 
/*     */     
/* 466 */     return true;
/*     */   }
/*     */   
/*     */   protected void logInterfaceInformation(NetworkInterface networkInterface) throws SocketException {
/* 470 */     log.info("---------------------------------------------------------------------------------");
/* 471 */     log.info(String.format("Interface display name: %s", new Object[] { networkInterface.getDisplayName() }));
/* 472 */     if (networkInterface.getParent() != null)
/* 473 */       log.info(String.format("Parent Info: %s", new Object[] { networkInterface.getParent() })); 
/* 474 */     log.info(String.format("Name: %s", new Object[] { networkInterface.getName() }));
/*     */     
/* 476 */     Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
/*     */     
/* 478 */     for (InetAddress inetAddress : Collections.<InetAddress>list(inetAddresses)) {
/* 479 */       log.info(String.format("InetAddress: %s", new Object[] { inetAddress }));
/*     */     } 
/*     */     
/* 482 */     List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
/*     */     
/* 484 */     for (InterfaceAddress interfaceAddress : interfaceAddresses) {
/* 485 */       if (interfaceAddress == null) {
/* 486 */         log.warning("Skipping null InterfaceAddress!");
/*     */         continue;
/*     */       } 
/* 489 */       log.info(" Interface Address");
/* 490 */       log.info("  Address: " + interfaceAddress.getAddress());
/* 491 */       log.info("  Broadcast: " + interfaceAddress.getBroadcast());
/* 492 */       log.info("  Prefix length: " + interfaceAddress.getNetworkPrefixLength());
/*     */     } 
/*     */     
/* 495 */     Enumeration<NetworkInterface> subIfs = networkInterface.getSubInterfaces();
/*     */     
/* 497 */     for (NetworkInterface subIf : Collections.<NetworkInterface>list(subIfs)) {
/* 498 */       if (subIf == null) {
/* 499 */         log.warning("Skipping null NetworkInterface sub-interface");
/*     */         continue;
/*     */       } 
/* 502 */       log.info(String.format("\tSub Interface Display name: %s", new Object[] { subIf.getDisplayName() }));
/* 503 */       log.info(String.format("\tSub Interface Name: %s", new Object[] { subIf.getName() }));
/*     */     } 
/* 505 */     log.info(String.format("Up? %s", new Object[] { Boolean.valueOf(networkInterface.isUp()) }));
/* 506 */     log.info(String.format("Loopback? %s", new Object[] { Boolean.valueOf(networkInterface.isLoopback()) }));
/* 507 */     log.info(String.format("PointToPoint? %s", new Object[] { Boolean.valueOf(networkInterface.isPointToPoint()) }));
/* 508 */     log.info(String.format("Supports multicast? %s", new Object[] { Boolean.valueOf(networkInterface.supportsMulticast()) }));
/* 509 */     log.info(String.format("Virtual? %s", new Object[] { Boolean.valueOf(networkInterface.isVirtual()) }));
/* 510 */     log.info(String.format("Hardware address: %s", new Object[] { Arrays.toString(networkInterface.getHardwareAddress()) }));
/* 511 */     log.info(String.format("MTU: %s", new Object[] { Integer.valueOf(networkInterface.getMTU()) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\NetworkAddressFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */