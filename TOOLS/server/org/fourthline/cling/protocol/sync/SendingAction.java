/*     */ package org.fourthline.cling.protocol.sync;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.action.ActionCancelledException;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.control.ActionRequestMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionResponseMessage;
/*     */ import org.fourthline.cling.model.message.control.IncomingActionResponseMessage;
/*     */ import org.fourthline.cling.model.message.control.OutgoingActionRequestMessage;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.protocol.SendingSync;
/*     */ import org.fourthline.cling.transport.RouterException;
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
/*     */ public class SendingAction
/*     */   extends SendingSync<OutgoingActionRequestMessage, IncomingActionResponseMessage>
/*     */ {
/*  53 */   private static final Logger log = Logger.getLogger(SendingAction.class.getName());
/*     */   
/*     */   protected final ActionInvocation actionInvocation;
/*     */   
/*     */   public SendingAction(UpnpService upnpService, ActionInvocation actionInvocation, URL controlURL) {
/*  58 */     super(upnpService, (StreamRequestMessage)new OutgoingActionRequestMessage(actionInvocation, controlURL));
/*  59 */     this.actionInvocation = actionInvocation;
/*     */   }
/*     */   
/*     */   protected IncomingActionResponseMessage executeSync() throws RouterException {
/*  63 */     return invokeRemote((OutgoingActionRequestMessage)getInputMessage());
/*     */   }
/*     */   
/*     */   protected IncomingActionResponseMessage invokeRemote(OutgoingActionRequestMessage requestMessage) throws RouterException {
/*  67 */     Device device = this.actionInvocation.getAction().getService().getDevice();
/*     */     
/*  69 */     log.fine("Sending outgoing action call '" + this.actionInvocation.getAction().getName() + "' to remote service of: " + device);
/*  70 */     IncomingActionResponseMessage responseMessage = null;
/*     */     
/*     */     try {
/*  73 */       StreamResponseMessage streamResponse = sendRemoteRequest(requestMessage);
/*     */       
/*  75 */       if (streamResponse == null) {
/*  76 */         log.fine("No connection or no no response received, returning null");
/*  77 */         this.actionInvocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Connection error or no response received"));
/*  78 */         return null;
/*     */       } 
/*     */       
/*  81 */       responseMessage = new IncomingActionResponseMessage(streamResponse);
/*     */       
/*  83 */       if (responseMessage.isFailedNonRecoverable()) {
/*  84 */         log.fine("Response was a non-recoverable failure: " + responseMessage);
/*  85 */         throw new ActionException(ErrorCode.ACTION_FAILED, "Non-recoverable remote execution failure: " + ((UpnpResponse)responseMessage
/*  86 */             .getOperation()).getResponseDetails());
/*     */       } 
/*  88 */       if (responseMessage.isFailedRecoverable()) {
/*  89 */         handleResponseFailure(responseMessage);
/*     */       } else {
/*  91 */         handleResponse(responseMessage);
/*     */       } 
/*     */       
/*  94 */       return responseMessage;
/*     */     
/*     */     }
/*  97 */     catch (ActionException ex) {
/*  98 */       log.fine("Remote action invocation failed, returning Internal Server Error message: " + ex.getMessage());
/*  99 */       this.actionInvocation.setFailure(ex);
/* 100 */       if (responseMessage == null || !((UpnpResponse)responseMessage.getOperation()).isFailed()) {
/* 101 */         return new IncomingActionResponseMessage(new UpnpResponse(UpnpResponse.Status.INTERNAL_SERVER_ERROR));
/*     */       }
/* 103 */       return responseMessage;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StreamResponseMessage sendRemoteRequest(OutgoingActionRequestMessage requestMessage) throws ActionException, RouterException {
/*     */     try {
/* 112 */       log.fine("Writing SOAP request body of: " + requestMessage);
/* 113 */       getUpnpService().getConfiguration().getSoapActionProcessor().writeBody((ActionRequestMessage)requestMessage, this.actionInvocation);
/*     */       
/* 115 */       log.fine("Sending SOAP body of message as stream to remote device");
/* 116 */       return getUpnpService().getRouter().send((StreamRequestMessage)requestMessage);
/* 117 */     } catch (RouterException ex) {
/* 118 */       Throwable cause = Exceptions.unwrap((Throwable)ex);
/* 119 */       if (cause instanceof InterruptedException) {
/* 120 */         if (log.isLoggable(Level.FINE)) {
/* 121 */           log.fine("Sending action request message was interrupted: " + cause);
/*     */         }
/* 123 */         throw new ActionCancelledException((InterruptedException)cause);
/*     */       } 
/* 125 */       throw ex;
/* 126 */     } catch (UnsupportedDataException ex) {
/* 127 */       if (log.isLoggable(Level.FINE)) {
/* 128 */         log.fine("Error writing SOAP body: " + ex);
/* 129 */         log.log(Level.FINE, "Exception root cause: ", Exceptions.unwrap((Throwable)ex));
/*     */       } 
/* 131 */       throw new ActionException(ErrorCode.ACTION_FAILED, "Error writing request message. " + ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleResponse(IncomingActionResponseMessage responseMsg) throws ActionException {
/*     */     try {
/* 138 */       log.fine("Received response for outgoing call, reading SOAP response body: " + responseMsg);
/* 139 */       getUpnpService().getConfiguration().getSoapActionProcessor().readBody((ActionResponseMessage)responseMsg, this.actionInvocation);
/* 140 */     } catch (UnsupportedDataException ex) {
/* 141 */       log.fine("Error reading SOAP body: " + ex);
/* 142 */       log.log(Level.FINE, "Exception root cause: ", Exceptions.unwrap((Throwable)ex));
/* 143 */       throw new ActionException(ErrorCode.ACTION_FAILED, "Error reading SOAP response message. " + ex
/*     */           
/* 145 */           .getMessage(), false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleResponseFailure(IncomingActionResponseMessage responseMsg) throws ActionException {
/*     */     try {
/* 154 */       log.fine("Received response with Internal Server Error, reading SOAP failure message");
/* 155 */       getUpnpService().getConfiguration().getSoapActionProcessor().readBody((ActionResponseMessage)responseMsg, this.actionInvocation);
/* 156 */     } catch (UnsupportedDataException ex) {
/* 157 */       log.fine("Error reading SOAP body: " + ex);
/* 158 */       log.log(Level.FINE, "Exception root cause: ", Exceptions.unwrap((Throwable)ex));
/* 159 */       throw new ActionException(ErrorCode.ACTION_FAILED, "Error reading SOAP response failure message. " + ex
/*     */           
/* 161 */           .getMessage(), false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\SendingAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */