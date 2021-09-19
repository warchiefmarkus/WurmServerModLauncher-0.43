/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*    */ import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
/*    */ import org.fourthline.cling.model.message.header.TimeoutHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
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
/*    */ public class IncomingSubscribeResponseMessage
/*    */   extends StreamResponseMessage
/*    */ {
/*    */   public IncomingSubscribeResponseMessage(StreamResponseMessage source) {
/* 29 */     super(source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidHeaders() {
/* 36 */     return (getHeaders().getFirstHeader(UpnpHeader.Type.SID, SubscriptionIdHeader.class) != null && 
/* 37 */       getHeaders().getFirstHeader(UpnpHeader.Type.TIMEOUT, TimeoutHeader.class) != null);
/*    */   }
/*    */   
/*    */   public String getSubscriptionId() {
/* 41 */     return (String)((SubscriptionIdHeader)getHeaders().getFirstHeader(UpnpHeader.Type.SID, SubscriptionIdHeader.class)).getValue();
/*    */   }
/*    */   
/*    */   public int getSubscriptionDurationSeconds() {
/* 45 */     return ((Integer)((TimeoutHeader)getHeaders().getFirstHeader(UpnpHeader.Type.TIMEOUT, TimeoutHeader.class)).getValue()).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\IncomingSubscribeResponseMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */