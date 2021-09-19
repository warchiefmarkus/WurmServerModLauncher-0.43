/*    */ package org.fourthline.cling.registry.event;
/*    */ 
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
/*    */ public class FailedRemoteDeviceDiscovery
/*    */   extends DeviceDiscovery<RemoteDevice>
/*    */ {
/*    */   protected Exception exception;
/*    */   
/*    */   public FailedRemoteDeviceDiscovery(RemoteDevice device, Exception ex) {
/* 28 */     super(device);
/* 29 */     this.exception = ex;
/*    */   }
/*    */   
/*    */   public Exception getException() {
/* 33 */     return this.exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\event\FailedRemoteDeviceDiscovery.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */