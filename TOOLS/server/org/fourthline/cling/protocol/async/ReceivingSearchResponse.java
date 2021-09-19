/*    */ package org.fourthline.cling.protocol.async;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.ValidationError;
/*    */ import org.fourthline.cling.model.ValidationException;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.UpnpMessage;
/*    */ import org.fourthline.cling.model.message.UpnpResponse;
/*    */ import org.fourthline.cling.model.message.discovery.IncomingSearchResponse;
/*    */ import org.fourthline.cling.model.meta.RemoteDevice;
/*    */ import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
/*    */ import org.fourthline.cling.model.types.UDN;
/*    */ import org.fourthline.cling.protocol.ReceivingAsync;
/*    */ import org.fourthline.cling.protocol.RetrieveRemoteDescriptors;
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
/*    */ public class ReceivingSearchResponse
/*    */   extends ReceivingAsync<IncomingSearchResponse>
/*    */ {
/* 45 */   private static final Logger log = Logger.getLogger(ReceivingSearchResponse.class.getName());
/*    */   
/*    */   public ReceivingSearchResponse(UpnpService upnpService, IncomingDatagramMessage<UpnpResponse> inputMessage) {
/* 48 */     super(upnpService, (UpnpMessage)new IncomingSearchResponse(inputMessage));
/*    */   }
/*    */   
/*    */   protected void execute() throws RouterException {
/*    */     RemoteDevice rd;
/* 53 */     if (!((IncomingSearchResponse)getInputMessage()).isSearchResponseMessage()) {
/* 54 */       log.fine("Ignoring invalid search response message: " + getInputMessage());
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     UDN udn = ((IncomingSearchResponse)getInputMessage()).getRootDeviceUDN();
/* 59 */     if (udn == null) {
/* 60 */       log.fine("Ignoring search response message without UDN: " + getInputMessage());
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     RemoteDeviceIdentity rdIdentity = new RemoteDeviceIdentity((IncomingSearchResponse)getInputMessage());
/* 65 */     log.fine("Received device search response: " + rdIdentity);
/*    */     
/* 67 */     if (getUpnpService().getRegistry().update(rdIdentity)) {
/* 68 */       log.fine("Remote device was already known: " + udn);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/*    */     try {
/* 74 */       rd = new RemoteDevice(rdIdentity);
/* 75 */     } catch (ValidationException ex) {
/* 76 */       log.warning("Validation errors of device during discovery: " + rdIdentity);
/* 77 */       for (ValidationError validationError : ex.getErrors()) {
/* 78 */         log.warning(validationError.toString());
/*    */       }
/*    */       
/*    */       return;
/*    */     } 
/* 83 */     if (rdIdentity.getDescriptorURL() == null) {
/* 84 */       log.finer("Ignoring message without location URL header: " + getInputMessage());
/*    */       
/*    */       return;
/*    */     } 
/* 88 */     if (rdIdentity.getMaxAgeSeconds() == null) {
/* 89 */       log.finer("Ignoring message without max-age header: " + getInputMessage());
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 95 */     getUpnpService().getConfiguration().getAsyncProtocolExecutor().execute((Runnable)new RetrieveRemoteDescriptors(
/* 96 */           getUpnpService(), rd));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\async\ReceivingSearchResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */