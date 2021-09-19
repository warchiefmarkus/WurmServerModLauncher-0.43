/*     */ package org.fourthline.cling.model.message.control;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.action.RemoteActionInvocation;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*     */ import org.fourthline.cling.model.message.header.SoapActionHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.message.header.UserAgentHeader;
/*     */ import org.fourthline.cling.model.meta.Action;
/*     */ import org.fourthline.cling.model.types.SoapActionType;
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
/*     */ public class OutgoingActionRequestMessage
/*     */   extends StreamRequestMessage
/*     */   implements ActionRequestMessage
/*     */ {
/*  38 */   private static Logger log = Logger.getLogger(OutgoingActionRequestMessage.class.getName());
/*     */   
/*     */   private final String actionNamespace;
/*     */   
/*     */   public OutgoingActionRequestMessage(ActionInvocation actionInvocation, URL controlURL) {
/*  43 */     this(actionInvocation.getAction(), new UpnpRequest(UpnpRequest.Method.POST, controlURL));
/*     */ 
/*     */     
/*  46 */     if (actionInvocation instanceof RemoteActionInvocation) {
/*  47 */       RemoteActionInvocation remoteActionInvocation = (RemoteActionInvocation)actionInvocation;
/*  48 */       if (remoteActionInvocation.getRemoteClientInfo() != null && remoteActionInvocation
/*  49 */         .getRemoteClientInfo().getRequestUserAgent() != null) {
/*  50 */         getHeaders().add(UpnpHeader.Type.USER_AGENT, (UpnpHeader)new UserAgentHeader(remoteActionInvocation
/*     */               
/*  52 */               .getRemoteClientInfo().getRequestUserAgent()));
/*     */       }
/*     */     }
/*  55 */     else if (actionInvocation.getClientInfo() != null) {
/*  56 */       getHeaders().putAll((Map)actionInvocation.getClientInfo().getRequestHeaders());
/*     */     } 
/*     */   }
/*     */   
/*     */   public OutgoingActionRequestMessage(Action action, UpnpRequest operation) {
/*  61 */     super(operation);
/*     */     SoapActionHeader soapActionHeader;
/*  63 */     getHeaders().add(UpnpHeader.Type.CONTENT_TYPE, (UpnpHeader)new ContentTypeHeader(ContentTypeHeader.DEFAULT_CONTENT_TYPE_UTF8));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     if (action instanceof org.fourthline.cling.model.meta.QueryStateVariableAction) {
/*  70 */       log.fine("Adding magic control SOAP action header for state variable query action");
/*     */ 
/*     */       
/*  73 */       soapActionHeader = new SoapActionHeader(new SoapActionType("schemas-upnp-org", "control-1-0", null, action.getName()));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  80 */       soapActionHeader = new SoapActionHeader(new SoapActionType(action.getService().getServiceType(), action.getName()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     this.actionNamespace = ((SoapActionType)soapActionHeader.getValue()).getTypeString();
/*     */     
/*  88 */     if (((UpnpRequest)getOperation()).getMethod().equals(UpnpRequest.Method.POST)) {
/*     */       
/*  90 */       getHeaders().add(UpnpHeader.Type.SOAPACTION, (UpnpHeader)soapActionHeader);
/*  91 */       log.fine("Added SOAP action header: " + soapActionHeader);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       throw new IllegalArgumentException("Can't send action with request method: " + ((UpnpRequest)getOperation()).getMethod());
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getActionNamespace() {
/* 109 */     return this.actionNamespace;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\control\OutgoingActionRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */