/*    */ package coffee.keenan.network.validators.interfaces;
/*    */ 
/*    */ import coffee.keenan.network.config.IConfiguration;
/*    */ import java.net.NetworkInterface;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NotLoopbackValidator
/*    */   implements IInterfaceValidator
/*    */ {
/*    */   private Exception exception;
/*    */   
/*    */   public boolean validate(NetworkInterface networkInterface, IConfiguration configuration) {
/*    */     try {
/* 16 */       return !networkInterface.isLoopback();
/*    */     }
/* 18 */     catch (Exception e) {
/*    */       
/* 20 */       this.exception = e;
/*    */       
/* 22 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Exception getException() {
/* 28 */     return (this.exception == null) ? new Exception("interface is loopback") : this.exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\interfaces\NotLoopbackValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */