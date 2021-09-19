/*     */ package org.fourthline.cling.controlpoint;
/*     */ 
/*     */ import java.net.URL;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.control.IncomingActionResponseMessage;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.RemoteDevice;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.protocol.sync.SendingAction;
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
/*     */ public abstract class ActionCallback
/*     */   implements Runnable
/*     */ {
/*     */   protected final ActionInvocation actionInvocation;
/*     */   protected ControlPoint controlPoint;
/*     */   
/*     */   public static final class Default
/*     */     extends ActionCallback
/*     */   {
/*     */     public Default(ActionInvocation actionInvocation, ControlPoint controlPoint) {
/*  76 */       super(actionInvocation, controlPoint);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void success(ActionInvocation invocation) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActionCallback(ActionInvocation actionInvocation, ControlPoint controlPoint) {
/*  94 */     this.actionInvocation = actionInvocation;
/*  95 */     this.controlPoint = controlPoint;
/*     */   }
/*     */   
/*     */   protected ActionCallback(ActionInvocation actionInvocation) {
/*  99 */     this.actionInvocation = actionInvocation;
/*     */   }
/*     */   
/*     */   public ActionInvocation getActionInvocation() {
/* 103 */     return this.actionInvocation;
/*     */   }
/*     */   
/*     */   public synchronized ControlPoint getControlPoint() {
/* 107 */     return this.controlPoint;
/*     */   }
/*     */   
/*     */   public synchronized ActionCallback setControlPoint(ControlPoint controlPoint) {
/* 111 */     this.controlPoint = controlPoint;
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public void run() {
/* 116 */     Service service = this.actionInvocation.getAction().getService();
/*     */ 
/*     */     
/* 119 */     if (service instanceof LocalService) {
/* 120 */       LocalService localService = (LocalService)service;
/*     */ 
/*     */       
/* 123 */       localService.getExecutor(this.actionInvocation.getAction()).execute(this.actionInvocation);
/*     */       
/* 125 */       if (this.actionInvocation.getFailure() != null) {
/* 126 */         failure(this.actionInvocation, null);
/*     */       } else {
/* 128 */         success(this.actionInvocation);
/*     */       }
/*     */     
/*     */     }
/* 132 */     else if (service instanceof RemoteService) {
/*     */       URL controLURL;
/* 134 */       if (getControlPoint() == null) {
/* 135 */         throw new IllegalStateException("Callback must be executed through ControlPoint");
/*     */       }
/*     */       
/* 138 */       RemoteService remoteService = (RemoteService)service;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 143 */         controLURL = ((RemoteDevice)remoteService.getDevice()).normalizeURI(remoteService.getControlURI());
/* 144 */       } catch (IllegalArgumentException e) {
/* 145 */         failure(this.actionInvocation, null, "bad control URL: " + remoteService.getControlURI());
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 150 */       SendingAction prot = getControlPoint().getProtocolFactory().createSendingAction(this.actionInvocation, controLURL);
/* 151 */       prot.run();
/*     */       
/* 153 */       IncomingActionResponseMessage response = (IncomingActionResponseMessage)prot.getOutputMessage();
/*     */       
/* 155 */       if (response == null) {
/* 156 */         failure(this.actionInvocation, null);
/* 157 */       } else if (((UpnpResponse)response.getOperation()).isFailed()) {
/* 158 */         failure(this.actionInvocation, (UpnpResponse)response.getOperation());
/*     */       } else {
/* 160 */         success(this.actionInvocation);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String createDefaultFailureMessage(ActionInvocation invocation, UpnpResponse operation) {
/* 166 */     String message = "Error: ";
/* 167 */     ActionException exception = invocation.getFailure();
/* 168 */     if (exception != null) {
/* 169 */       message = message + exception.getMessage();
/*     */     }
/* 171 */     if (operation != null) {
/* 172 */       message = message + " (HTTP response was: " + operation.getResponseDetails() + ")";
/*     */     }
/* 174 */     return message;
/*     */   }
/*     */   
/*     */   protected void failure(ActionInvocation invocation, UpnpResponse operation) {
/* 178 */     failure(invocation, operation, createDefaultFailureMessage(invocation, operation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void success(ActionInvocation paramActionInvocation);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 200 */     return "(ActionCallback) " + this.actionInvocation;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\controlpoint\ActionCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */