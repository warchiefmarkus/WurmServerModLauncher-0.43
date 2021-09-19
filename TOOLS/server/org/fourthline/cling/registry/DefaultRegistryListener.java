/*    */ package org.fourthline.cling.registry;
/*    */ 
/*    */ import org.fourthline.cling.model.meta.Device;
/*    */ import org.fourthline.cling.model.meta.LocalDevice;
/*    */ import org.fourthline.cling.model.meta.RemoteDevice;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultRegistryListener
/*    */   implements RegistryListener
/*    */ {
/*    */   public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {}
/*    */   
/*    */   public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {}
/*    */   
/*    */   public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
/* 49 */     deviceAdded(registry, (Device)device);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
/* 63 */     deviceRemoved(registry, (Device)device);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void localDeviceAdded(Registry registry, LocalDevice device) {
/* 73 */     deviceAdded(registry, (Device)device);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void localDeviceRemoved(Registry registry, LocalDevice device) {
/* 83 */     deviceRemoved(registry, (Device)device);
/*    */   }
/*    */   
/*    */   public void deviceAdded(Registry registry, Device device) {}
/*    */   
/*    */   public void deviceRemoved(Registry registry, Device device) {}
/*    */   
/*    */   public void beforeShutdown(Registry registry) {}
/*    */   
/*    */   public void afterShutdown() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\DefaultRegistryListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */