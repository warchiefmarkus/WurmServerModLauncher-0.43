/*    */ package coffee.keenan.network.wrappers.upnp;
/*    */ 
/*    */ import org.fourthline.cling.model.meta.RemoteDevice;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.ServiceType;
/*    */ import org.fourthline.cling.registry.DefaultRegistryListener;
/*    */ import org.fourthline.cling.registry.Registry;
/*    */ import org.fourthline.cling.support.igd.PortMappingListener;
/*    */ 
/*    */ class FindRouterListener
/*    */   extends DefaultRegistryListener {
/* 12 */   private final ServiceType[] searchServices = new ServiceType[] { PortMappingListener.IP_SERVICE_TYPE, PortMappingListener.PPP_SERVICE_TYPE };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
/* 20 */     super.remoteDeviceAdded(registry, device);
/* 21 */     if (UPNPService.getRouterDevice() != null && UPNPService.getWanService() != null)
/* 22 */       return;  for (ServiceType serviceType : this.searchServices) {
/*    */       
/* 24 */       Service service = device.findService(serviceType);
/* 25 */       if (service != null) {
/*    */         
/* 27 */         UPNPService.getInstance().setRouterAndService(device, service);
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\wrapper\\upnp\FindRouterListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */