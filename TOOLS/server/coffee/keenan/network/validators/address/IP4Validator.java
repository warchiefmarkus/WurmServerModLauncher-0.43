/*    */ package coffee.keenan.network.validators.address;
/*    */ 
/*    */ import coffee.keenan.network.config.IConfiguration;
/*    */ import java.net.InetAddress;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IP4Validator
/*    */   implements IAddressValidator
/*    */ {
/*    */   public boolean validate(InetAddress address, IConfiguration configuration) {
/* 13 */     return address instanceof java.net.Inet4Address;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Exception getException() {
/* 19 */     return new Exception("address is not an instance of Inet4Address");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\address\IP4Validator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */