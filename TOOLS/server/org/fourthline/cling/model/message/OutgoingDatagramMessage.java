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
/*    */ public abstract class OutgoingDatagramMessage<O extends UpnpOperation>
/*    */   extends UpnpMessage<O>
/*    */ {
/*    */   private InetAddress destinationAddress;
/*    */   private int destinationPort;
/* 30 */   private UpnpHeaders headers = new UpnpHeaders(false);
/*    */   
/*    */   protected OutgoingDatagramMessage(O operation, InetAddress destinationAddress, int destinationPort) {
/* 33 */     super(operation);
/* 34 */     this.destinationAddress = destinationAddress;
/* 35 */     this.destinationPort = destinationPort;
/*    */   }
/*    */   
/*    */   protected OutgoingDatagramMessage(O operation, UpnpMessage.BodyType bodyType, Object body, InetAddress destinationAddress, int destinationPort) {
/* 39 */     super(operation, bodyType, body);
/* 40 */     this.destinationAddress = destinationAddress;
/* 41 */     this.destinationPort = destinationPort;
/*    */   }
/*    */   
/*    */   public InetAddress getDestinationAddress() {
/* 45 */     return this.destinationAddress;
/*    */   }
/*    */   
/*    */   public int getDestinationPort() {
/* 49 */     return this.destinationPort;
/*    */   }
/*    */ 
/*    */   
/*    */   public UpnpHeaders getHeaders() {
/* 54 */     return this.headers;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\OutgoingDatagramMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */