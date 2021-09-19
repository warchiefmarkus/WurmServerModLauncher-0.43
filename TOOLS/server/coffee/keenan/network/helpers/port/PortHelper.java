/*    */ package coffee.keenan.network.helpers.port;
/*    */ 
/*    */ import coffee.keenan.network.config.DefaultConfiguration;
/*    */ import coffee.keenan.network.config.IConfiguration;
/*    */ import coffee.keenan.network.helpers.ErrorTracking;
/*    */ import coffee.keenan.network.validators.port.IPortValidator;
/*    */ import coffee.keenan.network.wrappers.upnp.UPNPService;
/*    */ import java.net.InetAddress;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ public class PortHelper
/*    */   extends ErrorTracking
/*    */ {
/*    */   private final IConfiguration configuration;
/*    */   
/*    */   public PortHelper() {
/* 21 */     this.configuration = (IConfiguration)new DefaultConfiguration();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PortHelper(IConfiguration configuration) {
/* 27 */     this.configuration = configuration;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Port assignPort(@NotNull Port port) {
/* 32 */     return assignPort(port, (IConfiguration)new DefaultConfiguration());
/*    */   }
/*    */ 
/*    */   
/*    */   public Port assignFavoredPort(@NotNull Port port) {
/* 37 */     Integer p = port.getFavoredPort();
/* 38 */     if (p != null && validatePort(port.getAddress(), p.intValue(), port.getValidators()))
/* 39 */       port.setAssignedPort(p.intValue()); 
/* 40 */     return port;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Port assignPort(@NotNull Port port, @NotNull IConfiguration configuration) {
/* 46 */     PortHelper portHelper = new PortHelper(configuration);
/* 47 */     portHelper.assignFavoredPort(port);
/* 48 */     if (port.getAssignedPort() != 0) return port;
/*    */     
/* 50 */     for (Iterator<Integer> iterator = port.getPorts().iterator(); iterator.hasNext(); ) { int p = ((Integer)iterator.next()).intValue();
/*    */       
/* 52 */       if (portHelper.validatePort(port.getAddress(), p, port.getValidators())) {
/*    */         
/* 54 */         port.setAssignedPort(p);
/*    */         break;
/*    */       }  }
/*    */     
/* 58 */     if (port.isToMap() && port.getAssignedPort() != 0) UPNPService.getInstance().openPort(port); 
/* 59 */     return port;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean validatePort(InetAddress address, int port, IPortValidator... validators) {
/* 65 */     return validatePort(address, port, Arrays.asList(validators));
/*    */   }
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
/*    */   public boolean validatePort(InetAddress address, int port, Collection<IPortValidator> validators) {
/* 79 */     for (IPortValidator validator : validators) {
/*    */       
/* 81 */       if (!validator.validate(address, this.configuration, port)) {
/*    */         
/* 83 */         addException("address: " + address.toString() + ", port: " + String.valueOf(port), validator.getException());
/* 84 */         return false;
/*    */       } 
/*    */     } 
/* 87 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\helpers\port\PortHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */