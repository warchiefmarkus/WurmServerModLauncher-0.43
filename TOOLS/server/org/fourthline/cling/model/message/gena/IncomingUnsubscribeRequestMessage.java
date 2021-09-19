/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.header.CallbackHeader;
/*    */ import org.fourthline.cling.model.message.header.NTEventHeader;
/*    */ import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.LocalService;
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
/*    */ public class IncomingUnsubscribeRequestMessage
/*    */   extends StreamRequestMessage
/*    */ {
/*    */   private final LocalService service;
/*    */   
/*    */   public IncomingUnsubscribeRequestMessage(StreamRequestMessage source, LocalService service) {
/* 33 */     super(source);
/* 34 */     this.service = service;
/*    */   }
/*    */   
/*    */   public LocalService getService() {
/* 38 */     return this.service;
/*    */   }
/*    */   
/*    */   public boolean hasCallbackHeader() {
/* 42 */     return (getHeaders().getFirstHeader(UpnpHeader.Type.CALLBACK, CallbackHeader.class) != null);
/*    */   }
/*    */   
/*    */   public boolean hasNotificationHeader() {
/* 46 */     return (getHeaders().getFirstHeader(UpnpHeader.Type.NT, NTEventHeader.class) != null);
/*    */   }
/*    */   
/*    */   public String getSubscriptionId() {
/* 50 */     SubscriptionIdHeader header = (SubscriptionIdHeader)getHeaders().getFirstHeader(UpnpHeader.Type.SID, SubscriptionIdHeader.class);
/* 51 */     return (header != null) ? (String)header.getValue() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\IncomingUnsubscribeRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */