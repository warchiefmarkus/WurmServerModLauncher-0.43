/*    */ package org.fourthline.cling.model.message.gena;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.Collection;
/*    */ import org.fourthline.cling.model.gena.GENASubscription;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.UpnpRequest;
/*    */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*    */ import org.fourthline.cling.model.message.header.EventSequenceHeader;
/*    */ import org.fourthline.cling.model.message.header.NTEventHeader;
/*    */ import org.fourthline.cling.model.message.header.NTSHeader;
/*    */ import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OutgoingEventRequestMessage
/*    */   extends StreamRequestMessage
/*    */ {
/*    */   private final Collection<StateVariableValue> stateVariableValues;
/*    */   
/*    */   public OutgoingEventRequestMessage(GENASubscription subscription, URL callbackURL, UnsignedIntegerFourBytes sequence, Collection<StateVariableValue> values) {
/* 46 */     super(new UpnpRequest(UpnpRequest.Method.NOTIFY, callbackURL));
/*    */     
/* 48 */     getHeaders().add(UpnpHeader.Type.CONTENT_TYPE, (UpnpHeader)new ContentTypeHeader());
/* 49 */     getHeaders().add(UpnpHeader.Type.NT, (UpnpHeader)new NTEventHeader());
/* 50 */     getHeaders().add(UpnpHeader.Type.NTS, (UpnpHeader)new NTSHeader(NotificationSubtype.PROPCHANGE));
/* 51 */     getHeaders().add(UpnpHeader.Type.SID, (UpnpHeader)new SubscriptionIdHeader(subscription.getSubscriptionId()));
/*    */ 
/*    */     
/* 54 */     getHeaders().add(UpnpHeader.Type.SEQ, (UpnpHeader)new EventSequenceHeader(sequence.getValue().longValue()));
/*    */     
/* 56 */     this.stateVariableValues = values;
/*    */   }
/*    */   
/*    */   public OutgoingEventRequestMessage(GENASubscription subscription, URL callbackURL) {
/* 60 */     this(subscription, callbackURL, subscription.getCurrentSequence(), subscription.getCurrentValues().values());
/*    */   }
/*    */   
/*    */   public Collection<StateVariableValue> getStateVariableValues() {
/* 64 */     return this.stateVariableValues;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\gena\OutgoingEventRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */