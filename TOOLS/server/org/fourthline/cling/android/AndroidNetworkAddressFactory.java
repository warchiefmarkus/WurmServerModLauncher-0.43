/*     */ package org.fourthline.cling.android;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.transport.impl.NetworkAddressFactoryImpl;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
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
/*     */ public class AndroidNetworkAddressFactory
/*     */   extends NetworkAddressFactoryImpl
/*     */ {
/*  37 */   private static final Logger log = Logger.getLogger(AndroidUpnpServiceConfiguration.class.getName());
/*     */   
/*     */   public AndroidNetworkAddressFactory(int streamListenPort) {
/*  40 */     super(streamListenPort);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean requiresNetworkInterface() {
/*  45 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isUsableAddress(NetworkInterface networkInterface, InetAddress address) {
/*  50 */     boolean result = super.isUsableAddress(networkInterface, address);
/*  51 */     if (result) {
/*     */ 
/*     */       
/*  54 */       String hostName = address.getHostAddress();
/*     */       
/*  56 */       Field field0 = null;
/*  57 */       Object target = null;
/*     */ 
/*     */       
/*     */       try {
/*     */         try {
/*  62 */           field0 = InetAddress.class.getDeclaredField("holder");
/*  63 */           field0.setAccessible(true);
/*  64 */           target = field0.get(address);
/*  65 */           field0 = target.getClass().getDeclaredField("hostName");
/*  66 */         } catch (NoSuchFieldException e) {
/*     */           
/*  68 */           field0 = InetAddress.class.getDeclaredField("hostName");
/*  69 */           target = address;
/*     */         } 
/*     */         
/*  72 */         if (field0 != null && target != null && hostName != null) {
/*  73 */           field0.setAccessible(true);
/*  74 */           field0.set(target, hostName);
/*     */         } else {
/*  76 */           return false;
/*     */         }
/*     */       
/*  79 */       } catch (Exception ex) {
/*  80 */         log.log(Level.SEVERE, "Failed injecting hostName to work around Android InetAddress DNS bug: " + address, ex);
/*     */ 
/*     */ 
/*     */         
/*  84 */         return false;
/*     */       } 
/*     */     } 
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress(NetworkInterface networkInterface, boolean isIPv6, InetAddress remoteAddress) {
/*  93 */     for (InetAddress localAddress : getInetAddresses(networkInterface)) {
/*  94 */       if (isIPv6 && localAddress instanceof java.net.Inet6Address)
/*  95 */         return localAddress; 
/*  96 */       if (!isIPv6 && localAddress instanceof java.net.Inet4Address)
/*  97 */         return localAddress; 
/*     */     } 
/*  99 */     throw new IllegalStateException("Can't find any IPv4 or IPv6 address on interface: " + networkInterface.getDisplayName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void discoverNetworkInterfaces() throws InitializationException {
/*     */     try {
/* 105 */       super.discoverNetworkInterfaces();
/* 106 */     } catch (Exception ex) {
/*     */ 
/*     */       
/* 109 */       log.warning("Exception while enumerating network interfaces, trying once more: " + ex);
/* 110 */       super.discoverNetworkInterfaces();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\android\AndroidNetworkAddressFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */