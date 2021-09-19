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
/*    */ public class EventedValueString
/*    */   extends EventedValue<String>
/*    */ {
/*    */   public EventedValueString(String value) {
/* 28 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueString(Map.Entry<String, String>[] attributes) {
/* 32 */     super(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 37 */     return Datatype.Builtin.STRING.getDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValueString.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */