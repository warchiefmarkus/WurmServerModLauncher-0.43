/*    */ package org.fourthline.cling.support.avtransport.impl.state;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
/*    */ import org.fourthline.cling.support.lastchange.EventedValue;
/*    */ import org.fourthline.cling.support.model.AVTransport;
/*    */ import org.fourthline.cling.support.model.TransportAction;
/*    */ import org.fourthline.cling.support.model.TransportInfo;
/*    */ import org.fourthline.cling.support.model.TransportState;
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
/*    */ public abstract class NoMediaPresent<T extends AVTransport>
/*    */   extends AbstractState<T>
/*    */ {
/* 32 */   private static final Logger log = Logger.getLogger(Stopped.class.getName());
/*    */   
/*    */   public NoMediaPresent(T transport) {
/* 35 */     super(transport);
/*    */   }
/*    */   
/*    */   public void onEntry() {
/* 39 */     log.fine("Setting transport state to NO_MEDIA_PRESENT");
/* 40 */     getTransport().setTransportInfo(new TransportInfo(TransportState.NO_MEDIA_PRESENT, 
/*    */ 
/*    */           
/* 43 */           getTransport().getTransportInfo().getCurrentTransportStatus(), 
/* 44 */           getTransport().getTransportInfo().getCurrentSpeed()));
/*    */ 
/*    */     
/* 47 */     getTransport().getLastChange().setEventedValue(
/* 48 */         getTransport().getInstanceId(), new EventedValue[] { (EventedValue)new AVTransportVariable.TransportState(TransportState.NO_MEDIA_PRESENT), (EventedValue)new AVTransportVariable.CurrentTransportActions(
/*    */             
/* 50 */             getCurrentTransportActions()) });
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Class<? extends AbstractState> setTransportURI(URI paramURI, String paramString);
/*    */   
/*    */   public TransportAction[] getCurrentTransportActions() {
/* 57 */     return new TransportAction[] { TransportAction.Stop };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\impl\state\NoMediaPresent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */