/*    */ package coffee.keenan.network.validators.port;
/*    */ 
/*    */ import coffee.keenan.network.config.IConfiguration;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.nio.channels.SocketChannel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TCPValidator
/*    */   implements IPortValidator
/*    */ {
/*    */   private Exception exception;
/*    */   
/*    */   public boolean validate(InetAddress address, IConfiguration configuration, int port) {
/* 16 */     try (SocketChannel socket = SocketChannel.open()) {
/*    */       
/* 18 */       socket.socket().setSoTimeout(configuration.getTimeout());
/* 19 */       socket.bind(new InetSocketAddress(address, port));
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


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\validators\port\TCPValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */