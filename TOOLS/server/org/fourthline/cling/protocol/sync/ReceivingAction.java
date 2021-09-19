/*     */ package org.fourthline.cling.protocol.sync;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.action.RemoteActionInvocation;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.control.ActionRequestMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionResponseMessage;
/*     */ import org.fourthline.cling.model.message.control.IncomingActionRequestMessage;
/*     */ import org.fourthline.cling.model.message.control.OutgoingActionResponseMessage;
/*     */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.resource.ServiceControlResource;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.protocol.ReceivingSync;
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
/*     */ public class ReceivingAction
/*     */   extends ReceivingSync<StreamRequestMessage, StreamResponseMessage>
/*     */ {
/*  51 */   private static final Logger log = Logger.getLogger(ReceivingAction.class.getName());
/*     */   
/*     */   public ReceivingAction(UpnpService upnpService, StreamRequestMessage inputMessage) {
/*  54 */     super(upnpService, inputMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected StreamResponseMessage executeSync() throws RouterException {
/*     */     RemoteActionInvocation remoteActionInvocation;
/*  60 */     ContentTypeHeader contentTypeHeader = (ContentTypeHeader)((StreamRequestMessage)getInputMessage()).getHeaders().getFirstHeader(UpnpHeader.Type.CONTENT_TYPE, ContentTypeHeader.class);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     if (contentTypeHeader != null && !contentTypeHeader.isUDACompliantXML()) {
/*  66 */       log.warning("Received invalid Content-Type '" + contentTypeHeader + "': " + getInputMessage());
/*  67 */       return new StreamResponseMessage(new UpnpResponse(UpnpResponse.Status.UNSUPPORTED_MEDIA_TYPE));
/*     */     } 
/*     */     
/*  70 */     if (contentTypeHeader == null) {
/*  71 */       log.warning("Received without Content-Type: " + getInputMessage());
/*     */     }
/*     */ 
/*     */     
/*  75 */     ServiceControlResource resource = (ServiceControlResource)getUpnpService().getRegistry().getResource(ServiceControlResource.class, ((StreamRequestMessage)
/*     */         
/*  77 */         getInputMessage()).getUri());
/*     */ 
/*     */     
/*  80 */     if (resource == null) {
/*  81 */       log.fine("No local resource found: " + getInputMessage());
/*  82 */       return null;
/*     */     } 
/*     */     
/*  85 */     log.fine("Found local action resource matching relative request URI: " + ((StreamRequestMessage)getInputMessage()).getUri());
/*     */ 
/*     */     
/*  88 */     OutgoingActionResponseMessage responseMessage = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  94 */       IncomingActionRequestMessage requestMessage = new IncomingActionRequestMessage((StreamRequestMessage)getInputMessage(), (LocalService)resource.getModel());
/*     */       
/*  96 */       log.finer("Created incoming action request message: " + requestMessage);
/*  97 */       remoteActionInvocation = new RemoteActionInvocation(requestMessage.getAction(), getRemoteClientInfo());
/*     */ 
/*     */       
/* 100 */       log.fine("Reading body of request message");
/* 101 */       getUpnpService().getConfiguration().getSoapActionProcessor().readBody((ActionRequestMessage)requestMessage, (ActionInvocation)remoteActionInvocation);
/*     */       
/* 103 */       log.fine("Executing on local service: " + remoteActionInvocation);
/* 104 */       ((LocalService)resource.getModel()).getExecutor(remoteActionInvocation.getAction()).execute((ActionInvocation)remoteActionInvocation);
/*     */       
/* 106 */       if (remoteActionInvocation.getFailure() == null) {
/*     */         
/* 108 */         responseMessage = new OutgoingActionResponseMessage(remoteActionInvocation.getAction());
/*     */       } else {
/*     */         
/* 111 */         if (remoteActionInvocation.getFailure() instanceof org.fourthline.cling.model.action.ActionCancelledException) {
/* 112 */           log.fine("Action execution was cancelled, returning 404 to client");
/*     */ 
/*     */ 
/*     */           
/* 116 */           return null;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 121 */         responseMessage = new OutgoingActionResponseMessage(UpnpResponse.Status.INTERNAL_SERVER_ERROR, remoteActionInvocation.getAction());
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 126 */     catch (ActionException ex) {
/* 127 */       log.finer("Error executing local action: " + ex);
/*     */       
/* 129 */       remoteActionInvocation = new RemoteActionInvocation(ex, getRemoteClientInfo());
/* 130 */       responseMessage = new OutgoingActionResponseMessage(UpnpResponse.Status.INTERNAL_SERVER_ERROR);
/*     */     }
/* 132 */     catch (UnsupportedDataException ex) {
/* 133 */       log.log(Level.WARNING, "Error reading action request XML body: " + ex.toString(), Exceptions.unwrap((Throwable)ex));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 140 */       remoteActionInvocation = new RemoteActionInvocation((Exceptions.unwrap((Throwable)ex) instanceof ActionException) ? (ActionException)Exceptions.unwrap((Throwable)ex) : new ActionException(ErrorCode.ACTION_FAILED, ex.getMessage()), getRemoteClientInfo());
/*     */       
/* 142 */       responseMessage = new OutgoingActionResponseMessage(UpnpResponse.Status.INTERNAL_SERVER_ERROR);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 148 */       log.fine("Writing body of response message");
/* 149 */       getUpnpService().getConfiguration().getSoapActionProcessor().writeBody((ActionResponseMessage)responseMessage, (ActionInvocation)remoteActionInvocation);
/*     */       
/* 151 */       log.fine("Returning finished response message: " + responseMessage);
/* 152 */       return (StreamResponseMessage)responseMessage;
/*     */     }
/* 154 */     catch (UnsupportedDataException ex) {
/* 155 */       log.warning("Failure writing body of response message, sending '500 Internal Server Error' without body");
/* 156 */       log.log(Level.WARNING, "Exception root cause: ", Exceptions.unwrap((Throwable)ex));
/* 157 */       return new StreamResponseMessage(UpnpResponse.Status.INTERNAL_SERVER_ERROR);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\ReceivingAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */