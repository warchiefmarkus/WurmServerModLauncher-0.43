/*    */ package org.fourthline.cling.mock;
/*    */ 
/*    */ import java.net.URL;
/*    */ import javax.enterprise.inject.Alternative;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.gena.LocalGENASubscription;
/*    */ import org.fourthline.cling.model.gena.RemoteGENASubscription;
/*    */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*    */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*    */ import org.fourthline.cling.model.meta.LocalDevice;
/*    */ import org.fourthline.cling.protocol.ProtocolCreationException;
/*    */ import org.fourthline.cling.protocol.ProtocolFactory;
/*    */ import org.fourthline.cling.protocol.ReceivingAsync;
/*    */ import org.fourthline.cling.protocol.ReceivingSync;
/*    */ import org.fourthline.cling.protocol.async.SendingNotificationAlive;
/*    */ import org.fourthline.cling.protocol.async.SendingNotificationByebye;
/*    */ import org.fourthline.cling.protocol.async.SendingSearch;
/*    */ import org.fourthline.cling.protocol.sync.SendingAction;
/*    */ import org.fourthline.cling.protocol.sync.SendingEvent;
/*    */ import org.fourthline.cling.protocol.sync.SendingRenewal;
/*    */ import org.fourthline.cling.protocol.sync.SendingSubscribe;
/*    */ import org.fourthline.cling.protocol.sync.SendingUnsubscribe;
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
/*    */ @Alternative
/*    */ public class MockProtocolFactory
/*    */   implements ProtocolFactory
/*    */ {
/*    */   public UpnpService getUpnpService() {
/* 49 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public ReceivingAsync createReceivingAsync(IncomingDatagramMessage message) throws ProtocolCreationException {
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public ReceivingSync createReceivingSync(StreamRequestMessage requestMessage) throws ProtocolCreationException {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingNotificationAlive createSendingNotificationAlive(LocalDevice localDevice) {
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingNotificationByebye createSendingNotificationByebye(LocalDevice localDevice) {
/* 69 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingSearch createSendingSearch(UpnpHeader searchTarget, int mxSeconds) {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingAction createSendingAction(ActionInvocation actionInvocation, URL controlURL) {
/* 79 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingSubscribe createSendingSubscribe(RemoteGENASubscription subscription) {
/* 84 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingRenewal createSendingRenewal(RemoteGENASubscription subscription) {
/* 89 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingUnsubscribe createSendingUnsubscribe(RemoteGENASubscription subscription) {
/* 94 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SendingEvent createSendingEvent(LocalGENASubscription subscription) {
/* 99 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\mock\MockProtocolFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */