/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.types.Datatype;
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
/*    */ public class EventedValueShort
/*    */   extends EventedValue<Short>
/*    */ {
/*    */   public EventedValueShort(Short value) {
/* 28 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueShort(Map.Entry<String, String>[] attributes) {
/* 32 */     super(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 37 */     return Datatype.Builtin.I2_SHORT.getDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValueShort.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */