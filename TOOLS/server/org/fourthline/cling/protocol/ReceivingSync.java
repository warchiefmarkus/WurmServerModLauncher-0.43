/*    */ package org.fourthline.cling.protocol;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.profile.RemoteClientInfo;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ReceivingSync<IN extends StreamRequestMessage, OUT extends StreamResponseMessage>
/*    */   extends ReceivingAsync<IN>
/*    */ {
/* 48 */   private static final Logger log = Logger.getLogger(UpnpService.class.getName());
/*    */   
/*    */   protected final RemoteClientInfo remoteClientInfo;
/*    */   protected OUT outputMessage;
/*    */   
/*    */   protected ReceivingSync(UpnpService upnpService, IN inputMessage) {
/* 54 */     super(upnpService, inputMessage);
/* 55 */     this.remoteClientInfo = new RemoteClientInfo((StreamRequestMessage)inputMessage);
/*    */   }
/*    */   
/*    */   public OUT getOutputMessage() {
/* 59 */     return this.outputMessage;
/*    */   }
/*    */   
/*    */   protected final void execute() throws RouterException {
/* 63 */     this.outputMessage = executeSync();
/*    */     
/* 65 */     if (this.outputMessage != null && getRemoteClientInfo().getExtraResponseHeaders().size() > 0) {
/* 66 */       log.fine("Setting extra headers on response message: " + getRemoteClientInfo().getExtraResponseHeaders().size());
/* 67 */       this.outputMessage.getHeaders().putAll((Map)getRemoteClientInfo().getExtraResponseHeaders());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract OUT executeSync() throws RouterException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void responseSent(StreamResponseMessage responseMessage) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void responseException(Throwable t) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RemoteClientInfo getRemoteClientInfo() {
/* 94 */     return this.remoteClientInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 99 */     return "(" + getClass().getSimpleName() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\ReceivingSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */