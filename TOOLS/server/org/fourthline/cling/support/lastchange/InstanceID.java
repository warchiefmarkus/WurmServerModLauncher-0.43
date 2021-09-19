/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ public class InstanceID
/*    */ {
/*    */   protected UnsignedIntegerFourBytes id;
/* 29 */   protected List<EventedValue> values = new ArrayList<>();
/*    */   
/*    */   public InstanceID(UnsignedIntegerFourBytes id) {
/* 32 */     this(id, new ArrayList<>());
/*    */   }
/*    */   
/*    */   public InstanceID(UnsignedIntegerFourBytes id, List<EventedValue> values) {
/* 36 */     this.id = id;
/* 37 */     this.values = values;
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getId() {
/* 41 */     return this.id;
/*    */   }
/*    */   
/*    */   public List<EventedValue> getValues() {
/* 45 */     return this.values;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\InstanceID.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */