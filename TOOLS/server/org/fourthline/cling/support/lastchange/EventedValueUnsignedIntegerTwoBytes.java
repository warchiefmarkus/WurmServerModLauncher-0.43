/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.types.Datatype;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
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
/*    */ public class EventedValueUnsignedIntegerTwoBytes
/*    */   extends EventedValue<UnsignedIntegerTwoBytes>
/*    */ {
/*    */   public EventedValueUnsignedIntegerTwoBytes(UnsignedIntegerTwoBytes value) {
/* 29 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueUnsignedIntegerTwoBytes(Map.Entry<String, String>[] attributes) {
/* 33 */     super(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 38 */     return Datatype.Builtin.UI2.getDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValueUnsignedIntegerTwoBytes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */