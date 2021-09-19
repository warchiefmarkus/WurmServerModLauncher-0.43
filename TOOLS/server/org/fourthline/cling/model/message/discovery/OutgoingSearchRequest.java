/*    */ package org.fourthline.cling.model.message.discovery;
/*    */ 
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.UpnpOperation;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
/*    */ import org.fourthline.cling.model.message.header.HostHeader;
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
/*    */ public class OutgoingSearchRequest
/*    */   extends OutgoingDatagramMessage<UpnpRequest>
/*    */ {
/*    */   private UpnpHeader searchTarget;
/*    */   
/*    */   public OutgoingSearchRequest(UpnpHeader searchTarget, int mxSeconds) {
/* 36 */     super((UpnpOperation)new UpnpRequest(UpnpRequest.Method.MSEARCH), 
/*    */         
/* 38 */         ModelUtil.getInetAddressByName("239.255.255.250"), 1900);
/*    */ 
/*    */ 
/*    */     
/* 42 */     this.searchTarget = searchTarget;
/*    */     
/* 44 */     getHeaders().add(UpnpHeader.Type.MAN, (UpnpHeader)new MANHeader(NotificationSubtype.DISCOVER.getHeaderString()));
/* 45 */     getHeaders().add(UpnpHeader.Type.MX, (UpnpHeader)new MXHeader(Integer.valueOf(mxSeconds)));
/* 46 */     getHeaders().add(UpnpHeader.Type.ST, searchTarget);
/* 47 */     getHeaders().add(UpnpHeader.Type.HOST, (UpnpHeader)new HostHeader());
/*    */   }
/*    */   
/*    */   public UpnpHeader getSearchTarget() {
/* 51 */     return this.searchTarget;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\discovery\OutgoingSearchRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */