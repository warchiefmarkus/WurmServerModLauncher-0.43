/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.types.Datatype;
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
/*    */ public class EventedValueUnsignedIntegerFourBytes
/*    */   extends EventedValue<UnsignedIntegerFourBytes>
/*    */ {
/*    */   public EventedValueUnsignedIntegerFourBytes(UnsignedIntegerFourBytes value) {
/* 29 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueUnsignedIntegerFourBytes(Map.Entry<String, String>[] attributes) {
/* 33 */     super(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 38 */     return Datatype.Builtin.UI4.getDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValueUnsignedIntegerFourBytes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */