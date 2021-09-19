/*     */ package org.fourthline.cling.transport.spi;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.protocol.ProtocolCreationException;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.protocol.ReceivingSync;
/*     */ import org.seamless.util.Exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UpnpStream
/*     */   implements Runnable
/*     */ {
/*  45 */   private static Logger log = Logger.getLogger(UpnpStream.class.getName());
/*     */   
/*     */   protected final ProtocolFactory protocolFactory;
/*     */   protected ReceivingSync syncProtocol;
/*     */   
/*     */   protected UpnpStream(ProtocolFactory protocolFactory) {
/*  51 */     this.protocolFactory = protocolFactory;
/*     */   }
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/*  55 */     return this.protocolFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamResponseMessage process(StreamRequestMessage requestMsg) {
/*  70 */     log.fine("Processing stream request message: " + requestMsg);
/*     */ 
/*     */     
/*     */     try {
/*  74 */       this.syncProtocol = getProtocolFactory().createReceivingSync(requestMsg);
/*  75 */     } catch (ProtocolCreationException ex) {
/*  76 */       log.warning("Processing stream request failed - " + Exceptions.unwrap((Throwable)ex).toString());
/*  77 */       return new StreamResponseMessage(UpnpResponse.Status.NOT_IMPLEMENTED);
/*     */     } 
/*     */ 
/*     */     
/*  81 */     log.fine("Running protocol for synchronous message processing: " + this.syncProtocol);
/*  82 */     this.syncProtocol.run();
/*     */ 
/*     */     
/*  85 */     StreamResponseMessage responseMsg = this.syncProtocol.getOutputMessage();
/*     */     
/*  87 */     if (responseMsg == null) {
/*     */       
/*  89 */       log.finer("Protocol did not return any response message");
/*  90 */       return null;
/*     */     } 
/*  92 */     log.finer("Protocol returned response: " + responseMsg);
/*  93 */     return responseMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void responseSent(StreamResponseMessage responseMessage) {
/* 102 */     if (this.syncProtocol != null) {
/* 103 */       this.syncProtocol.responseSent(responseMessage);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void responseException(Throwable t) {
/* 112 */     if (this.syncProtocol != null) {
/* 113 */       this.syncProtocol.responseException(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 118 */     return "(" + getClass().getSimpleName() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\UpnpStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */