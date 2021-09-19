/*     */ package org.fourthline.cling.model.gena;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.state.StateVariableValue;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.seamless.util.Exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LocalGENASubscription
/*     */   extends GENASubscription<LocalService>
/*     */   implements PropertyChangeListener
/*     */ {
/*  58 */   private static Logger log = Logger.getLogger(LocalGENASubscription.class.getName());
/*     */ 
/*     */   
/*     */   final List<URL> callbackURLs;
/*     */   
/*  63 */   final Map<String, Long> lastSentTimestamp = new HashMap<>();
/*  64 */   final Map<String, Long> lastSentNumericValue = new HashMap<>();
/*     */   
/*     */   protected LocalGENASubscription(LocalService service, List<URL> callbackURLs) throws Exception {
/*  67 */     super(service);
/*  68 */     this.callbackURLs = callbackURLs;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalGENASubscription(LocalService service, Integer requestedDurationSeconds, List<URL> callbackURLs) throws Exception {
/*  73 */     super(service);
/*     */     
/*  75 */     setSubscriptionDuration(requestedDurationSeconds);
/*     */     
/*  77 */     log.fine("Reading initial state of local service at subscription time");
/*  78 */     long currentTime = (new Date()).getTime();
/*  79 */     this.currentValues.clear();
/*     */     
/*  81 */     Collection<StateVariableValue> values = getService().getManager().getCurrentState();
/*     */     
/*  83 */     log.finer("Got evented state variable values: " + values.size());
/*     */     
/*  85 */     for (StateVariableValue<LocalService> value : values) {
/*  86 */       this.currentValues.put(value.getStateVariable().getName(), value);
/*     */       
/*  88 */       if (log.isLoggable(Level.FINEST)) {
/*  89 */         log.finer("Read state variable value '" + value.getStateVariable().getName() + "': " + value.toString());
/*     */       }
/*     */ 
/*     */       
/*  93 */       this.lastSentTimestamp.put(value.getStateVariable().getName(), Long.valueOf(currentTime));
/*  94 */       if (value.getStateVariable().isModeratedNumericType()) {
/*  95 */         this.lastSentNumericValue.put(value.getStateVariable().getName(), Long.valueOf(value.toString()));
/*     */       }
/*     */     } 
/*     */     
/*  99 */     this.subscriptionId = "uuid:" + UUID.randomUUID();
/* 100 */     this.currentSequence = new UnsignedIntegerFourBytes(0L);
/* 101 */     this.callbackURLs = callbackURLs;
/*     */   }
/*     */   
/*     */   public synchronized List<URL> getCallbackURLs() {
/* 105 */     return this.callbackURLs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void registerOnService() {
/* 112 */     getService().getManager().getPropertyChangeSupport().addPropertyChangeListener(this);
/*     */   }
/*     */   
/*     */   public synchronized void establish() {
/* 116 */     established();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void end(CancelReason reason) {
/*     */     try {
/* 124 */       getService().getManager().getPropertyChangeSupport().removePropertyChangeListener(this);
/* 125 */     } catch (Exception ex) {
/* 126 */       log.warning("Removal of local service property change listener failed: " + Exceptions.unwrap(ex));
/*     */     } 
/* 128 */     ended(reason);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void propertyChange(PropertyChangeEvent e) {
/* 136 */     if (!e.getPropertyName().equals("_EventedStateVariables"))
/*     */       return; 
/* 138 */     log.fine("Eventing triggered, getting state for subscription: " + getSubscriptionId());
/*     */     
/* 140 */     long currentTime = (new Date()).getTime();
/*     */     
/* 142 */     Collection<StateVariableValue> newValues = (Collection<StateVariableValue>)e.getNewValue();
/* 143 */     Set<String> excludedVariables = moderateStateVariables(currentTime, newValues);
/*     */     
/* 145 */     this.currentValues.clear();
/* 146 */     for (StateVariableValue<LocalService> newValue : newValues) {
/* 147 */       String name = newValue.getStateVariable().getName();
/* 148 */       if (!excludedVariables.contains(name)) {
/* 149 */         log.fine("Adding state variable value to current values of event: " + newValue.getStateVariable() + " = " + newValue);
/* 150 */         this.currentValues.put(newValue.getStateVariable().getName(), newValue);
/*     */ 
/*     */         
/* 153 */         this.lastSentTimestamp.put(name, Long.valueOf(currentTime));
/* 154 */         if (newValue.getStateVariable().isModeratedNumericType()) {
/* 155 */           this.lastSentNumericValue.put(name, Long.valueOf(newValue.toString()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 160 */     if (this.currentValues.size() > 0) {
/* 161 */       log.fine("Propagating new state variable values to subscription: " + this);
/*     */ 
/*     */ 
/*     */       
/* 165 */       eventReceived();
/*     */     } else {
/* 167 */       log.fine("No state variable values for event (all moderated out?), not triggering event");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized Set<String> moderateStateVariables(long currentTime, Collection<StateVariableValue> values) {
/* 180 */     Set<String> excludedVariables = new HashSet<>();
/*     */ 
/*     */     
/* 183 */     for (StateVariableValue stateVariableValue : values) {
/*     */       
/* 185 */       StateVariable stateVariable = stateVariableValue.getStateVariable();
/* 186 */       String stateVariableName = stateVariableValue.getStateVariable().getName();
/*     */       
/* 188 */       if (stateVariable.getEventDetails().getEventMaximumRateMilliseconds() == 0 && stateVariable
/* 189 */         .getEventDetails().getEventMinimumDelta() == 0) {
/* 190 */         log.finer("Variable is not moderated: " + stateVariable);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 195 */       if (!this.lastSentTimestamp.containsKey(stateVariableName)) {
/* 196 */         log.finer("Variable is moderated but was never sent before: " + stateVariable);
/*     */         
/*     */         continue;
/*     */       } 
/* 200 */       if (stateVariable.getEventDetails().getEventMaximumRateMilliseconds() > 0) {
/* 201 */         long timestampLastSent = ((Long)this.lastSentTimestamp.get(stateVariableName)).longValue();
/* 202 */         long timestampNextSend = timestampLastSent + stateVariable.getEventDetails().getEventMaximumRateMilliseconds();
/* 203 */         if (currentTime <= timestampNextSend) {
/* 204 */           log.finer("Excluding state variable with maximum rate: " + stateVariable);
/* 205 */           excludedVariables.add(stateVariableName);
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/* 210 */       if (stateVariable.isModeratedNumericType() && this.lastSentNumericValue.get(stateVariableName) != null) {
/*     */         
/* 212 */         long oldValue = Long.valueOf(((Long)this.lastSentNumericValue.get(stateVariableName)).longValue()).longValue();
/* 213 */         long newValue = Long.valueOf(stateVariableValue.toString()).longValue();
/* 214 */         long minDelta = stateVariable.getEventDetails().getEventMinimumDelta();
/*     */         
/* 216 */         if (newValue > oldValue && newValue - oldValue < minDelta) {
/* 217 */           log.finer("Excluding state variable with minimum delta: " + stateVariable);
/* 218 */           excludedVariables.add(stateVariableName);
/*     */           
/*     */           continue;
/*     */         } 
/* 222 */         if (newValue < oldValue && oldValue - newValue < minDelta) {
/* 223 */           log.finer("Excluding state variable with minimum delta: " + stateVariable);
/* 224 */           excludedVariables.add(stateVariableName);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 229 */     return excludedVariables;
/*     */   }
/*     */   
/*     */   public synchronized void incrementSequence() {
/* 233 */     this.currentSequence.increment(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setSubscriptionDuration(Integer requestedDurationSeconds) {
/* 241 */     this
/*     */ 
/*     */       
/* 244 */       .requestedDurationSeconds = (requestedDurationSeconds == null) ? 1800 : requestedDurationSeconds.intValue();
/*     */     
/* 246 */     setActualSubscriptionDurationSeconds(this.requestedDurationSeconds);
/*     */   }
/*     */   
/*     */   public abstract void ended(CancelReason paramCancelReason);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\gena\LocalGENASubscription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */