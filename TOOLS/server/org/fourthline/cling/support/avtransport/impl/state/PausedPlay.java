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
/*    */ 
/*    */ public abstract class PausedPlay<T extends AVTransport>
/*    */   extends AbstractState<T>
/*    */ {
/* 33 */   private static final Logger log = Logger.getLogger(PausedPlay.class.getName());
/*    */   
/*    */   public PausedPlay(T transport) {
/* 36 */     super(transport);
/*    */   }
/*    */   
/*    */   public void onEntry() {
/* 40 */     log.fine("Setting transport state to PAUSED_PLAYBACK");
/* 41 */     getTransport().setTransportInfo(new TransportInfo(TransportState.PAUSED_PLAYBACK, 
/*    */ 
/*    */           
/* 44 */           getTransport().getTransportInfo().getCurrentTransportStatus(), 
/* 45 */           getTransport().getTransportInfo().getCurrentSpeed()));
/*    */ 
/*    */     
/* 48 */     getTransport().getLastChange().setEventedValue(
/* 49 */         getTransport().getInstanceId(), new EventedValue[] { (EventedValue)new AVTransportVariable.TransportState(TransportState.PAUSED_PLAYBACK), (EventedValue)new AVTransportVariable.CurrentTransportActions(
/*    */             
/* 51 */             getCurrentTransportActions()) });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> setTransportURI(URI paramURI, String paramString);
/*    */ 
/*    */   
/*    */   public TransportAction[] getCurrentTransportActions() {
/* 60 */     return new TransportAction[] { TransportAction.Stop, TransportAction.Play };
/*    */   }
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> stop();
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> play(String paramString);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\impl\state\PausedPlay.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */