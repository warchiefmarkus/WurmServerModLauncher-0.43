/*     */ package org.fourthline.cling;
/*     */ 
/*     */ import org.fourthline.cling.model.message.header.STAllHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.registry.Registry;
/*     */ import org.fourthline.cling.registry.RegistryListener;
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
/*     */ public class Main
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  32 */     RegistryListener listener = new RegistryListener()
/*     */       {
/*     */         public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device)
/*     */         {
/*  36 */           System.out.println("Discovery started: " + device
/*  37 */               .getDisplayString());
/*     */         }
/*     */ 
/*     */         
/*     */         public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
/*  42 */           System.out.println("Discovery failed: " + device
/*  43 */               .getDisplayString() + " => " + ex);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
/*  48 */           System.out.println("Remote device available: " + device
/*  49 */               .getDisplayString());
/*     */         }
/*     */ 
/*     */         
/*     */         public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
/*  54 */           System.out.println("Remote device updated: " + device
/*  55 */               .getDisplayString());
/*     */         }
/*     */ 
/*     */         
/*     */         public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
/*  60 */           System.out.println("Remote device removed: " + device
/*  61 */               .getDisplayString());
/*     */         }
/*     */ 
/*     */         
/*     */         public void localDeviceAdded(Registry registry, LocalDevice device) {
/*  66 */           System.out.println("Local device added: " + device
/*  67 */               .getDisplayString());
/*     */         }
/*     */ 
/*     */         
/*     */         public void localDeviceRemoved(Registry registry, LocalDevice device) {
/*  72 */           System.out.println("Local device removed: " + device
/*  73 */               .getDisplayString());
/*     */         }
/*     */ 
/*     */         
/*     */         public void beforeShutdown(Registry registry) {
/*  78 */           System.out.println("Before shutdown, the registry has devices: " + registry
/*  79 */               .getDevices().size());
/*     */         }
/*     */ 
/*     */         
/*     */         public void afterShutdown() {
/*  84 */           System.out.println("Shutdown of registry complete!");
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*  90 */     System.out.println("Starting Cling...");
/*  91 */     UpnpService upnpService = new UpnpServiceImpl(new RegistryListener[] { listener });
/*     */ 
/*     */     
/*  94 */     System.out.println("Sending SEARCH message to all devices...");
/*  95 */     upnpService.getControlPoint().search((UpnpHeader)new STAllHeader());
/*     */ 
/*     */     
/*  98 */     System.out.println("Waiting 10 seconds before shutting down...");
/*  99 */     Thread.sleep(10000L);
/*     */ 
/*     */     
/* 102 */     System.out.println("Stopping Cling...");
/* 103 */     upnpService.shutdown();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */