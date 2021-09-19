/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public class Event
/*    */ {
/* 29 */   protected List<InstanceID> instanceIDs = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public Event() {}
/*    */   
/*    */   public Event(List<InstanceID> instanceIDs) {
/* 35 */     this.instanceIDs = instanceIDs;
/*    */   }
/*    */   
/*    */   public List<InstanceID> getInstanceIDs() {
/* 39 */     return this.instanceIDs;
/*    */   }
/*    */   
/*    */   public InstanceID getInstanceID(UnsignedIntegerFourBytes id) {
/* 43 */     for (InstanceID instanceID : this.instanceIDs) {
/* 44 */       if (instanceID.getId().equals(id)) return instanceID; 
/*    */     } 
/* 46 */     return null;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 50 */     this.instanceIDs = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public void setEventedValue(UnsignedIntegerFourBytes id, EventedValue ev) {
/* 54 */     InstanceID instanceID = null;
/* 55 */     for (InstanceID i : getInstanceIDs()) {
/* 56 */       if (i.getId().equals(id)) {
/* 57 */         instanceID = i;
/*    */       }
/*    */     } 
/* 60 */     if (instanceID == null) {
/* 61 */       instanceID = new InstanceID(id);
/* 62 */       getInstanceIDs().add(instanceID);
/*    */     } 
/*    */     
/* 65 */     Iterator<EventedValue> it = instanceID.getValues().iterator();
/* 66 */     while (it.hasNext()) {
/* 67 */       EventedValue existingEv = it.next();
/* 68 */       if (existingEv.getClass().equals(ev.getClass())) {
/* 69 */         it.remove();
/*    */       }
/*    */     } 
/* 72 */     instanceID.getValues().add(ev);
/*    */   }
/*    */   
/*    */   public <EV extends EventedValue> EV getEventedValue(UnsignedIntegerFourBytes id, Class<EV> type) {
/* 76 */     for (InstanceID instanceID : getInstanceIDs()) {
/* 77 */       if (instanceID.getId().equals(id))
/* 78 */         for (EventedValue eventedValue : instanceID.getValues()) {
/* 79 */           if (eventedValue.getClass().equals(type)) {
/* 80 */             return (EV)eventedValue;
/*    */           }
/*    */         }  
/*    */     } 
/* 84 */     return null;
/*    */   }
/*    */   
/*    */   public boolean hasChanges() {
/* 88 */     for (InstanceID instanceID : this.instanceIDs) {
/* 89 */       if (instanceID.getValues().size() > 0) return true; 
/*    */     } 
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\Event.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */