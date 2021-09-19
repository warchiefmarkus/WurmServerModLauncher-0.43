/*    */ package coffee.keenan.network.validators.address;
/*    */ 
/*    */ import coffee.keenan.network.config.IConfiguration;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.nio.channels.SocketChannel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InternetValidator
/*    */   implements IAddressValidator
/*    */ {
/*    */   private Exception exception;
/*    */   
/*    */   public boolean validate(InetAddress address, IConfiguration configuration) {
/* 16 */     try (SocketChannel socket = SocketChannel.open()) {
/*    */       
/* 18 */       socket.socket().setSoTimeout(configuration.getTimeout());
/* 19 */       socket.bind(new InetSocketAddress(address, 0));
/* 20 */       socket.connect(new InetSocketAddress(configuration.getTestUrl(), configuration.getTestPort()));
/*    */     }
/* 22 */     catch (Exception e) {
/*    */       
/* 24 */       this.exception = e;
/* 25 */       return false;
/*    */     } 
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Exception getException() {
/* 33 */     return this.exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\address\InternetValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */