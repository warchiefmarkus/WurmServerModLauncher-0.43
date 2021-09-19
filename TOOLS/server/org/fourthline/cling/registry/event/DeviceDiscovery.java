/*    */ package org.fourthline.cling.registry.event;
/*    */ 
/*    */ import org.fourthline.cling.model.meta.Device;
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
/*    */ public class DeviceDiscovery<D extends Device>
/*    */ {
/*    */   protected D device;
/*    */   
/*    */   public DeviceDiscovery(D device) {
/* 30 */     this.device = device;
/*    */   }
/*    */   
/*    */   public D getDevice() {
/* 34 */     return this.device;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\event\DeviceDiscovery.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */