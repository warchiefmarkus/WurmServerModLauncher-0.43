/*    */ package org.fourthline.cling.model.gena;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.state.StateVariableValue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GENASubscription<S extends Service>
/*    */ {
/*    */   protected S service;
/*    */   protected String subscriptionId;
/* 40 */   protected int requestedDurationSeconds = 1800;
/*    */   protected int actualDurationSeconds;
/*    */   protected UnsignedIntegerFourBytes currentSequence;
/* 43 */   protected Map<String, StateVariableValue<S>> currentValues = new LinkedHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected GENASubscription(S service) {
/* 49 */     this.service = service;
/*    */   }
/*    */   
/*    */   public GENASubscription(S service, int requestedDurationSeconds) {
/* 53 */     this(service);
/* 54 */     this.requestedDurationSeconds = requestedDurationSeconds;
/*    */   }
/*    */   
/*    */   public synchronized S getService() {
/* 58 */     return this.service;
/*    */   }
/*    */   
/*    */   public synchronized String getSubscriptionId() {
/* 62 */     return this.subscriptionId;
/*    */   }
/*    */   
/*    */   public synchronized void setSubscriptionId(String subscriptionId) {
/* 66 */     this.subscriptionId = subscriptionId;
/*    */   }
/*    */   
/*    */   public synchronized int getRequestedDurationSeconds() {
/* 70 */     return this.requestedDurationSeconds;
/*    */   }
/*    */   
/*    */   public synchronized int getActualDurationSeconds() {
/* 74 */     return this.actualDurationSeconds;
/*    */   }
/*    */   
/*    */   public synchronized void setActualSubscriptionDurationSeconds(int seconds) {
/* 78 */     this.actualDurationSeconds = seconds;
/*    */   }
/*    */   
/*    */   public synchronized UnsignedIntegerFourBytes getCurrentSequence() {
/* 82 */     return this.currentSequence;
/*    */   }
/*    */   
/*    */   public synchronized Map<String, StateVariableValue<S>> getCurrentValues() {
/* 86 */     return this.currentValues;
/*    */   }
/*    */   
/*    */   public abstract void established();
/*    */   
/*    */   public abstract void eventReceived();
/*    */   
/*    */   public String toString() {
/* 94 */     return "(GENASubscription, SID: " + getSubscriptionId() + ", SEQUENCE: " + getCurrentSequence() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\gena\GENASubscription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */