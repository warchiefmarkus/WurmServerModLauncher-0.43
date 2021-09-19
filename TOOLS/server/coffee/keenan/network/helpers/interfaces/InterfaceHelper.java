/*    */ package coffee.keenan.network.helpers.interfaces;
/*    */ 
/*    */ import coffee.keenan.network.config.DefaultConfiguration;
/*    */ import coffee.keenan.network.config.IConfiguration;
/*    */ import coffee.keenan.network.helpers.ErrorTracking;
/*    */ import coffee.keenan.network.validators.interfaces.IInterfaceValidator;
/*    */ import coffee.keenan.network.validators.interfaces.NotLoopbackValidator;
/*    */ import coffee.keenan.network.validators.interfaces.UpValidator;
/*    */ import java.net.NetworkInterface;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ public class InterfaceHelper
/*    */   extends ErrorTracking
/*    */ {
/*    */   private final IConfiguration configuration;
/* 17 */   private IInterfaceValidator[] interfaceValidators = new IInterfaceValidator[] { (IInterfaceValidator)new NotLoopbackValidator(), (IInterfaceValidator)new UpValidator() };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InterfaceHelper() {
/* 24 */     this.configuration = (IConfiguration)new DefaultConfiguration();
/*    */   }
/*    */ 
/*    */   
/*    */   public InterfaceHelper(IConfiguration configuration) {
/* 29 */     this.configuration = configuration;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IInterfaceValidator[] getInterfaceValidators() {
/* 35 */     return this.interfaceValidators;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setInterfaceValidators(@NotNull IInterfaceValidator... validators) {
/* 41 */     this.interfaceValidators = validators;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean validateInterface(NetworkInterface networkInterface) {
/* 52 */     if (networkInterface == null) {
/*    */       
/* 54 */       addException("null interface", new Exception("given interface was null"));
/* 55 */       return false;
/*    */     } 
/*    */     
/* 58 */     for (IInterfaceValidator validator : getInterfaceValidators()) {
/*    */       
/* 60 */       if (!validator.validate(networkInterface, this.configuration)) {
/*    */         
/* 62 */         addException("interface: " + networkInterface.getDisplayName() + "(" + networkInterface.getName() + ")", validator.getException());
/* 63 */         return false;
/*    */       } 
/*    */     } 
/* 66 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\helpers\interfaces\InterfaceHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */