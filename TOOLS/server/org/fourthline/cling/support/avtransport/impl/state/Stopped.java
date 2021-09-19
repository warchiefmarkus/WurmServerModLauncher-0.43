/*    */ package org.fourthline.cling.support.avtransport.impl.state;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
/*    */ import org.fourthline.cling.support.lastchange.EventedValue;
/*    */ import org.fourthline.cling.support.model.AVTransport;
/*    */ import org.fourthline.cling.support.model.SeekMode;
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
/*    */ public abstract class Stopped<T extends AVTransport>
/*    */   extends AbstractState<T>
/*    */ {
/* 33 */   private static final Logger log = Logger.getLogger(Stopped.class.getName());
/*    */   
/*    */   public Stopped(T transport) {
/* 36 */     super(transport);
/*    */   }
/*    */   
/*    */   public void onEntry() {
/* 40 */     log.fine("Setting transport state to STOPPED");
/* 41 */     getTransport().setTransportInfo(new TransportInfo(TransportState.STOPPED, 
/*    */ 
/*    */           
/* 44 */           getTransport().getTransportInfo().getCurrentTransportStatus(), 
/* 45 */           getTransport().getTransportInfo().getCurrentSpeed()));
/*    */ 
/*    */     
/* 48 */     getTransport().getLastChange().setEventedValue(
/* 49 */         getTransport().getInstanceId(), new EventedValue[] { (EventedValue)new AVTransportVariable.TransportState(TransportState.STOPPED), (EventedValue)new AVTransportVariable.CurrentTransportActions(
/*    */             
/* 51 */             getCurrentTransportActions()) });
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> setTransportURI(URI paramURI, String paramString);
/*    */ 
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> stop();
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> play(String paramString);
/*    */   
/*    */   public TransportAction[] getCurrentTransportActions() {
/* 63 */     return new TransportAction[] { TransportAction.Stop, TransportAction.Play, TransportAction.Next, TransportAction.Previous, TransportAction.Seek };
/*    */   }
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> next();
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> previous();
/*    */   
/*    */   public abstract Class<? extends AbstractState<?>> seek(SeekMode paramSeekMode, String paramString);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\impl\state\Stopped.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */