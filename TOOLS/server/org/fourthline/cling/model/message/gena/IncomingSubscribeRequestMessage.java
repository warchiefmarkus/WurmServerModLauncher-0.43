/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.header.CallbackHeader;
/*    */ import org.fourthline.cling.model.message.header.NTEventHeader;
/*    */ import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
/*    */ import org.fourthline.cling.model.message.header.TimeoutHeader;
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
/*    */ 
/*    */ public class IncomingSubscribeRequestMessage
/*    */   extends StreamRequestMessage
/*    */ {
/*    */   private final LocalService service;
/*    */   
/*    */   public IncomingSubscribeRequestMessage(StreamRequestMessage source, LocalService service) {
/* 37 */     super(source);
/* 38 */     this.service = service;
/*    */   }
/*    */   
/*    */   public LocalService getService() {
/* 42 */     return this.service;
/*    */   }
/*    */   
/*    */   public List<URL> getCallbackURLs() {
/* 46 */     CallbackHeader header = (CallbackHeader)getHeaders().getFirstHeader(UpnpHeader.Type.CALLBACK, CallbackHeader.class);
/* 47 */     return (header != null) ? (List<URL>)header.getValue() : null;
/*    */   }
/*    */   
/*    */   public boolean hasNotificationHeader() {
/* 51 */     return (getHeaders().getFirstHeader(UpnpHeader.Type.NT, NTEventHeader.class) != null);
/*    */   }
/*    */   
/*    */   public Integer getRequestedTimeoutSeconds() {
/* 55 */     TimeoutHeader timeoutHeader = (TimeoutHeader)getHeaders().getFirstHeader(UpnpHeader.Type.TIMEOUT, TimeoutHeader.class);
/* 56 */     return (timeoutHeader != null) ? (Integer)timeoutHeader.getValue() : null;
/*    */   }
/*    */   
/*    */   public String getSubscriptionId() {
/* 60 */     SubscriptionIdHeader header = (SubscriptionIdHeader)getHeaders().getFirstHeader(UpnpHeader.Type.SID, SubscriptionIdHeader.class);
/* 61 */     return (header != null) ? (String)header.getValue() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\IncomingSubscribeRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */