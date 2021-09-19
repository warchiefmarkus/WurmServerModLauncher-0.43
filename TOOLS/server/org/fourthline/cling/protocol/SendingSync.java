/*    */ package org.fourthline.cling.protocol;
/*    */ 
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.transport.RouterException;
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
/*    */ public abstract class SendingSync<IN extends StreamRequestMessage, OUT extends StreamResponseMessage>
/*    */   extends SendingAsync
/*    */ {
/*    */   private final IN inputMessage;
/*    */   protected OUT outputMessage;
/*    */   
/*    */   protected SendingSync(UpnpService upnpService, IN inputMessage) {
/* 41 */     super(upnpService);
/* 42 */     this.inputMessage = inputMessage;
/*    */   }
/*    */   
/*    */   public IN getInputMessage() {
/* 46 */     return this.inputMessage;
/*    */   }
/*    */   
/*    */   public OUT getOutputMessage() {
/* 50 */     return this.outputMessage;
/*    */   }
/*    */   
/*    */   protected final void execute() throws RouterException {
/* 54 */     this.outputMessage = executeSync();
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract OUT executeSync() throws RouterException;
/*    */   
/*    */   public String toString() {
/* 61 */     return "(" + getClass().getSimpleName() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\SendingSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */