/*    */ package coffee.keenan.network.validators.port;
/*    */ 
/*    */ import coffee.keenan.network.config.IConfiguration;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.nio.channels.DatagramChannel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UDPValidator
/*    */   implements IPortValidator
/*    */ {
/*    */   private Exception exception;
/*    */   
/*    */   public boolean validate(InetAddress address, IConfiguration configuration, int port) {
/* 16 */     try (DatagramChannel datagram = DatagramChannel.open()) {
/*    */       
/* 18 */       datagram.socket().setSoTimeout(configuration.getTimeout());
/* 19 */       datagram.bind(new InetSocketAddress(address, port));
/*    */     }
/* 21 */     catch (Exception e) {
/*    */       
/* 23 */       this.exception = e;
/* 24 */       return false;
/*    */     } 
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Exception getException() {
/* 32 */     return this.exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\port\UDPValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */