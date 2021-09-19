/*     */ package org.fourthline.cling.support.lastchange;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LastChange
/*     */ {
/*     */   private final Event event;
/*     */   private final LastChangeParser parser;
/*     */   private String previousValue;
/*     */   
/*     */   public LastChange(String s) {
/*  51 */     throw new UnsupportedOperationException("This constructor is only for service binding detection");
/*     */   }
/*     */   
/*     */   public LastChange(LastChangeParser parser, Event event) {
/*  55 */     this.parser = parser;
/*  56 */     this.event = event;
/*     */   }
/*     */   
/*     */   public LastChange(LastChangeParser parser) {
/*  60 */     this(parser, new Event());
/*     */   }
/*     */   
/*     */   public LastChange(LastChangeParser parser, String xml) throws Exception {
/*  64 */     if (xml != null && xml.length() > 0) {
/*  65 */       this.event = parser.parse(xml);
/*     */     } else {
/*  67 */       this.event = new Event();
/*     */     } 
/*  69 */     this.parser = parser;
/*     */   }
/*     */   
/*     */   public synchronized void reset() {
/*  73 */     this.previousValue = toString();
/*  74 */     this.event.clear();
/*     */   }
/*     */   
/*     */   public synchronized void setEventedValue(int instanceID, EventedValue... ev) {
/*  78 */     setEventedValue(new UnsignedIntegerFourBytes(instanceID), ev);
/*     */   }
/*     */   
/*     */   public synchronized void setEventedValue(UnsignedIntegerFourBytes instanceID, EventedValue... ev) {
/*  82 */     for (EventedValue eventedValue : ev) {
/*  83 */       if (eventedValue != null) {
/*  84 */         this.event.setEventedValue(instanceID, eventedValue);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized UnsignedIntegerFourBytes[] getInstanceIDs() {
/*  90 */     List<UnsignedIntegerFourBytes> list = new ArrayList<>();
/*  91 */     for (InstanceID instanceID : this.event.getInstanceIDs()) {
/*  92 */       list.add(instanceID.getId());
/*     */     }
/*  94 */     return list.<UnsignedIntegerFourBytes>toArray(new UnsignedIntegerFourBytes[list.size()]);
/*     */   }
/*     */   
/*     */   synchronized EventedValue[] getEventedValues(UnsignedIntegerFourBytes instanceID) {
/*  98 */     InstanceID inst = this.event.getInstanceID(instanceID);
/*  99 */     return (inst != null) ? inst.getValues().<EventedValue>toArray(new EventedValue[inst.getValues().size()]) : null;
/*     */   }
/*     */   
/*     */   public synchronized <EV extends EventedValue> EV getEventedValue(int instanceID, Class<EV> type) {
/* 103 */     return getEventedValue(new UnsignedIntegerFourBytes(instanceID), type);
/*     */   }
/*     */   
/*     */   public synchronized <EV extends EventedValue> EV getEventedValue(UnsignedIntegerFourBytes id, Class<EV> type) {
/* 107 */     return this.event.getEventedValue(id, type);
/*     */   }
/*     */   
/*     */   public synchronized void fire(PropertyChangeSupport propertyChangeSupport) {
/* 111 */     String lastChanges = toString();
/* 112 */     if (lastChanges != null && lastChanges.length() > 0) {
/* 113 */       propertyChangeSupport.firePropertyChange("LastChange", this.previousValue, lastChanges);
/* 114 */       reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 120 */     if (!this.event.hasChanges()) return ""; 
/*     */     try {
/* 122 */       return this.parser.generate(this.event);
/* 123 */     } catch (Exception ex) {
/* 124 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\LastChange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */