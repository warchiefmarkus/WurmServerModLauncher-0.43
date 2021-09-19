/*    */ package org.fourthline.cling.model.message.control;
/*    */ 
/*    */ import org.fourthline.cling.model.action.ActionException;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.header.SoapActionHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.Action;
/*    */ import org.fourthline.cling.model.meta.LocalService;
/*    */ import org.fourthline.cling.model.types.ErrorCode;
/*    */ import org.fourthline.cling.model.types.SoapActionType;
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
/*    */ public class IncomingActionRequestMessage
/*    */   extends StreamRequestMessage
/*    */   implements ActionRequestMessage
/*    */ {
/*    */   private final Action action;
/*    */   private final String actionNamespace;
/*    */   
/*    */   public IncomingActionRequestMessage(StreamRequestMessage source, LocalService service) throws ActionException {
/* 38 */     super(source);
/*    */     
/* 40 */     SoapActionHeader soapActionHeader = (SoapActionHeader)getHeaders().getFirstHeader(UpnpHeader.Type.SOAPACTION, SoapActionHeader.class);
/* 41 */     if (soapActionHeader == null) {
/* 42 */       throw new ActionException(ErrorCode.INVALID_ACTION, "Missing SOAP action header");
/*    */     }
/*    */     
/* 45 */     SoapActionType actionType = (SoapActionType)soapActionHeader.getValue();
/*    */     
/* 47 */     this.action = service.getAction(actionType.getActionName());
/* 48 */     if (this.action == null) {
/* 49 */       throw new ActionException(ErrorCode.INVALID_ACTION, "Service doesn't implement action: " + actionType.getActionName());
/*    */     }
/*    */     
/* 52 */     if (!"QueryStateVariable".equals(actionType.getActionName()) && 
/* 53 */       !service.getServiceType().implementsVersion(actionType.getServiceType())) {
/* 54 */       throw new ActionException(ErrorCode.INVALID_ACTION, "Service doesn't support the requested service version");
/*    */     }
/*    */ 
/*    */     
/* 58 */     this.actionNamespace = actionType.getTypeString();
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 62 */     return this.action;
/*    */   }
/*    */   
/*    */   public String getActionNamespace() {
/* 66 */     return this.actionNamespace;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\control\IncomingActionRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */