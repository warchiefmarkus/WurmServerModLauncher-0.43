/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
/*    */ import org.fourthline.cling.model.message.header.MANHeader;
/*    */ import org.fourthline.cling.model.message.header.MXHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.types.NotificationSubtype;
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
/*    */ public class IncomingSearchRequest
/*    */   extends IncomingDatagramMessage<UpnpRequest>
/*    */ {
/*    */   public IncomingSearchRequest(IncomingDatagramMessage<UpnpRequest> source) {
/* 31 */     super(source);
/*    */   }
/*    */   
/*    */   public UpnpHeader getSearchTarget() {
/* 35 */     return getHeaders().getFirstHeader(UpnpHeader.Type.ST);
/*    */   }
/*    */   
/*    */   public Integer getMX() {
/* 39 */     MXHeader header = (MXHeader)getHeaders().getFirstHeader(UpnpHeader.Type.MX, MXHeader.class);
/* 40 */     if (header != null) {
/* 41 */       return (Integer)header.getValue();
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMANSSDPDiscover() {
/* 51 */     MANHeader header = (MANHeader)getHeaders().getFirstHeader(UpnpHeader.Type.MAN, MANHeader.class);
/* 52 */     return (header != null && ((String)header.getValue()).equals(NotificationSubtype.DISCOVER.getHeaderString()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\IncomingSearchRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */