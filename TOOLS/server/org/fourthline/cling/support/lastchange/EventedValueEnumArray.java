/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.fourthline.cling.model.types.Datatype;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
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
/*    */ public abstract class EventedValueEnumArray<E extends Enum>
/*    */   extends EventedValue<E[]>
/*    */ {
/*    */   public EventedValueEnumArray(E[] e) {
/* 30 */     super(e);
/*    */   }
/*    */   
/*    */   public EventedValueEnumArray(Map.Entry<String, String>[] attributes) {
/* 34 */     super(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected E[] valueOf(String s) throws InvalidValueException {
/* 39 */     return enumValueOf(ModelUtil.fromCommaSeparatedList(s));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return ModelUtil.toCommaSeparatedList((Object[])getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract E[] enumValueOf(String[] paramArrayOfString);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValueEnumArray.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */