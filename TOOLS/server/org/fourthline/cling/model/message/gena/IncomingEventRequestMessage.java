/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.header.EventSequenceHeader;
/*    */ import org.fourthline.cling.model.message.header.NTEventHeader;
/*    */ import org.fourthline.cling.model.message.header.NTSHeader;
/*    */ import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.RemoteService;
/*    */ import org.fourthline.cling.model.state.StateVariableValue;
/*    */ import org.fourthline.cling.model.types.NotificationSubtype;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*    */ public class IncomingEventRequestMessage
/*    */   extends StreamRequestMessage
/*    */ {
/* 37 */   private final List<StateVariableValue> stateVariableValues = new ArrayList<>();
/*    */   private final RemoteService service;
/*    */   
/*    */   public IncomingEventRequestMessage(StreamRequestMessage source, RemoteService service) {
/* 41 */     super(source);
/* 42 */     this.service = service;
/*    */   }
/*    */   
/*    */   public RemoteService getService() {
/* 46 */     return this.service;
/*    */   }
/*    */   
/*    */   public List<StateVariableValue> getStateVariableValues() {
/* 50 */     return this.stateVariableValues;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSubscrptionId() {
/* 55 */     SubscriptionIdHeader header = (SubscriptionIdHeader)getHeaders().getFirstHeader(UpnpHeader.Type.SID, SubscriptionIdHeader.class);
/* 56 */     return (header != null) ? (String)header.getValue() : null;
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getSequence() {
/* 60 */     EventSequenceHeader header = (EventSequenceHeader)getHeaders().getFirstHeader(UpnpHeader.Type.SEQ, EventSequenceHeader.class);
/* 61 */     return (header != null) ? (UnsignedIntegerFourBytes)header.getValue() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasNotificationHeaders() {
/* 68 */     UpnpHeader ntHeader = getHeaders().getFirstHeader(UpnpHeader.Type.NT);
/* 69 */     UpnpHeader ntsHeader = getHeaders().getFirstHeader(UpnpHeader.Type.NTS);
/* 70 */     return (ntHeader != null && ntHeader.getValue() != null && ntsHeader != null && ntsHeader
/* 71 */       .getValue() != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasValidNotificationHeaders() {
/* 79 */     NTEventHeader ntHeader = (NTEventHeader)getHeaders().getFirstHeader(UpnpHeader.Type.NT, NTEventHeader.class);
/* 80 */     NTSHeader ntsHeader = (NTSHeader)getHeaders().getFirstHeader(UpnpHeader.Type.NTS, NTSHeader.class);
/* 81 */     return (ntHeader != null && ntHeader.getValue() != null && ntsHeader != null && ((NotificationSubtype)ntsHeader
/* 82 */       .getValue()).equals(NotificationSubtype.PROPCHANGE));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return super.toString() + " SEQUENCE: " + getSequence().getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\IncomingEventRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */