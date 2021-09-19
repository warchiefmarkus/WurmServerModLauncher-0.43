/*    */ package org.fourthline.cling.model.meta;
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
/*    */ public class StateVariableEventDetails
/*    */ {
/*    */   private final boolean sendEvents;
/*    */   private final int eventMaximumRateMilliseconds;
/*    */   private final int eventMinimumDelta;
/*    */   
/*    */   public StateVariableEventDetails() {
/* 30 */     this(true, 0, 0);
/*    */   }
/*    */   
/*    */   public StateVariableEventDetails(boolean sendEvents) {
/* 34 */     this(sendEvents, 0, 0);
/*    */   }
/*    */   
/*    */   public StateVariableEventDetails(boolean sendEvents, int eventMaximumRateMilliseconds, int eventMinimumDelta) {
/* 38 */     this.sendEvents = sendEvents;
/* 39 */     this.eventMaximumRateMilliseconds = eventMaximumRateMilliseconds;
/* 40 */     this.eventMinimumDelta = eventMinimumDelta;
/*    */   }
/*    */   
/*    */   public boolean isSendEvents() {
/* 44 */     return this.sendEvents;
/*    */   }
/*    */   
/*    */   public int getEventMaximumRateMilliseconds() {
/* 48 */     return this.eventMaximumRateMilliseconds;
/*    */   }
/*    */   
/*    */   public int getEventMinimumDelta() {
/* 52 */     return this.eventMinimumDelta;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\StateVariableEventDetails.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */