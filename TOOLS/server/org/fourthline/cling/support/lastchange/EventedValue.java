/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.types.Datatype;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.fourthline.cling.support.shared.AbstractMap;
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
/*    */ public abstract class EventedValue<V>
/*    */ {
/*    */   protected final V value;
/*    */   
/*    */   public EventedValue(V value) {
/* 29 */     this.value = value;
/*    */   }
/*    */   
/*    */   public EventedValue(Map.Entry<String, String>[] attributes) {
/*    */     try {
/* 34 */       this.value = valueOf(attributes);
/* 35 */     } catch (InvalidValueException ex) {
/* 36 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getName() {
/* 41 */     return getClass().getSimpleName();
/*    */   }
/*    */   
/*    */   public V getValue() {
/* 45 */     return this.value;
/*    */   }
/*    */   
/*    */   public Map.Entry<String, String>[] getAttributes() {
/* 49 */     return (Map.Entry<String, String>[])new Map.Entry[] { (Map.Entry)new AbstractMap.SimpleEntry("val", 
/* 50 */           toString()) };
/*    */   }
/*    */ 
/*    */   
/*    */   protected V valueOf(Map.Entry<String, String>[] attributes) throws InvalidValueException {
/* 55 */     V v = null;
/* 56 */     for (Map.Entry<String, String> attribute : attributes) {
/* 57 */       if (((String)attribute.getKey()).equals("val")) v = valueOf(attribute.getValue()); 
/*    */     } 
/* 59 */     return v;
/*    */   }
/*    */   
/*    */   protected V valueOf(String s) throws InvalidValueException {
/* 63 */     return (V)getDatatype().valueOf(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return getDatatype().getString(getValue());
/*    */   }
/*    */   
/*    */   protected abstract Datatype getDatatype();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */