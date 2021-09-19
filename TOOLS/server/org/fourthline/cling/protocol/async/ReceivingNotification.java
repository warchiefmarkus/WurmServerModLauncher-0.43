/*     */ package org.fourthline.cling.protocol.async;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.discovery.IncomingNotificationRequest;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.fourthline.cling.protocol.ReceivingAsync;
/*     */ import org.fourthline.cling.protocol.RetrieveRemoteDescriptors;
/*     */ import org.fourthline.cling.transport.RouterException;
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
/*     */ public class ReceivingNotification
/*     */   extends ReceivingAsync<IncomingNotificationRequest>
/*     */ {
/*  74 */   private static final Logger log = Logger.getLogger(ReceivingNotification.class.getName());
/*     */   
/*     */   public ReceivingNotification(UpnpService upnpService, IncomingDatagramMessage<UpnpRequest> inputMessage) {
/*  77 */     super(upnpService, (UpnpMessage)new IncomingNotificationRequest(inputMessage));
/*     */   }
/*     */   
/*     */   protected void execute() throws RouterException {
/*     */     RemoteDevice rd;
/*  82 */     UDN udn = ((IncomingNotificationRequest)getInputMessage()).getUDN();
/*  83 */     if (udn == null) {
/*  84 */       log.fine("Ignoring notification message without UDN: " + getInputMessage());
/*     */       
/*     */       return;
/*     */     } 
/*  88 */     RemoteDeviceIdentity rdIdentity = new RemoteDeviceIdentity((IncomingNotificationRequest)getInputMessage());
/*  89 */     log.fine("Received device notification: " + rdIdentity);
/*     */ 
/*     */     
/*     */     try {
/*  93 */       rd = new RemoteDevice(rdIdentity);
/*  94 */     } catch (ValidationException ex) {
/*  95 */       log.warning("Validation errors of device during discovery: " + rdIdentity);
/*  96 */       for (ValidationError validationError : ex.getErrors()) {
/*  97 */         log.warning(validationError.toString());
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 102 */     if (((IncomingNotificationRequest)getInputMessage()).isAliveMessage()) {
/*     */       
/* 104 */       log.fine("Received device ALIVE advertisement, descriptor location is: " + rdIdentity.getDescriptorURL());
/*     */       
/* 106 */       if (rdIdentity.getDescriptorURL() == null) {
/* 107 */         log.finer("Ignoring message without location URL header: " + getInputMessage());
/*     */         
/*     */         return;
/*     */       } 
/* 111 */       if (rdIdentity.getMaxAgeSeconds() == null) {
/* 112 */         log.finer("Ignoring message without max-age header: " + getInputMessage());
/*     */         
/*     */         return;
/*     */       } 
/* 116 */       if (getUpnpService().getRegistry().update(rdIdentity)) {
/* 117 */         log.finer("Remote device was already known: " + udn);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 123 */       getUpnpService().getConfiguration().getAsyncProtocolExecutor().execute((Runnable)new RetrieveRemoteDescriptors(
/* 124 */             getUpnpService(), rd));
/*     */     
/*     */     }
/* 127 */     else if (((IncomingNotificationRequest)getInputMessage()).isByeByeMessage()) {
/*     */       
/* 129 */       log.fine("Received device BYEBYE advertisement");
/* 130 */       boolean removed = getUpnpService().getRegistry().removeDevice(rd);
/* 131 */       if (removed) {
/* 132 */         log.fine("Removed remote device from registry: " + rd);
/*     */       }
/*     */     } else {
/*     */       
/* 136 */       log.finer("Ignoring unknown notification message: " + getInputMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\async\ReceivingNotification.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */