/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.action.ActionArgumentValue;
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
/*    */ public class TransportInfo
/*    */ {
/* 27 */   private TransportState currentTransportState = TransportState.NO_MEDIA_PRESENT;
/* 28 */   private TransportStatus currentTransportStatus = TransportStatus.OK;
/* 29 */   private String currentSpeed = "1";
/*    */ 
/*    */   
/*    */   public TransportInfo() {}
/*    */   
/*    */   public TransportInfo(Map<String, ActionArgumentValue> args) {
/* 35 */     this(
/* 36 */         TransportState.valueOrCustomOf((String)((ActionArgumentValue)args.get("CurrentTransportState")).getValue()), 
/* 37 */         TransportStatus.valueOrCustomOf((String)((ActionArgumentValue)args.get("CurrentTransportStatus")).getValue()), (String)((ActionArgumentValue)args
/* 38 */         .get("CurrentSpeed")).getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public TransportInfo(TransportState currentTransportState) {
/* 43 */     this.currentTransportState = currentTransportState;
/*    */   }
/*    */   
/*    */   public TransportInfo(TransportState currentTransportState, String currentSpeed) {
/* 47 */     this.currentTransportState = currentTransportState;
/* 48 */     this.currentSpeed = currentSpeed;
/*    */   }
/*    */   
/*    */   public TransportInfo(TransportState currentTransportState, TransportStatus currentTransportStatus) {
/* 52 */     this.currentTransportState = currentTransportState;
/* 53 */     this.currentTransportStatus = currentTransportStatus;
/*    */   }
/*    */   
/*    */   public TransportInfo(TransportState currentTransportState, TransportStatus currentTransportStatus, String currentSpeed) {
/* 57 */     this.currentTransportState = currentTransportState;
/* 58 */     this.currentTransportStatus = currentTransportStatus;
/* 59 */     this.currentSpeed = currentSpeed;
/*    */   }
/*    */   
/*    */   public TransportState getCurrentTransportState() {
/* 63 */     return this.currentTransportState;
/*    */   }
/*    */   
/*    */   public TransportStatus getCurrentTransportStatus() {
/* 67 */     return this.currentTransportStatus;
/*    */   }
/*    */   
/*    */   public String getCurrentSpeed() {
/* 71 */     return this.currentSpeed;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\TransportInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */