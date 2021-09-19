/*    */ package org.fourthline.cling.model.message;
/*    */ 
/*    */ import java.net.InetAddress;
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
/*    */ public class IncomingDatagramMessage<O extends UpnpOperation>
/*    */   extends UpnpMessage<O>
/*    */ {
/*    */   private InetAddress sourceAddress;
/*    */   private int sourcePort;
/*    */   private InetAddress localAddress;
/*    */   
/*    */   public IncomingDatagramMessage(O operation, InetAddress sourceAddress, int sourcePort, InetAddress localAddress) {
/* 44 */     super(operation);
/* 45 */     this.sourceAddress = sourceAddress;
/* 46 */     this.sourcePort = sourcePort;
/* 47 */     this.localAddress = localAddress;
/*    */   }
/*    */   
/*    */   protected IncomingDatagramMessage(IncomingDatagramMessage<O> source) {
/* 51 */     super(source);
/* 52 */     this.sourceAddress = source.getSourceAddress();
/* 53 */     this.sourcePort = source.getSourcePort();
/* 54 */     this.localAddress = source.getLocalAddress();
/*    */   }
/*    */   
/*    */   public InetAddress getSourceAddress() {
/* 58 */     return this.sourceAddress;
/*    */   }
/*    */   
/*    */   public int getSourcePort() {
/* 62 */     return this.sourcePort;
/*    */   }
/*    */   
/*    */   public InetAddress getLocalAddress() {
/* 66 */     return this.localAddress;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\IncomingDatagramMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */