/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public abstract class EventedValueEnum<E extends Enum>
/*    */   extends EventedValue<E>
/*    */ {
/*    */   public EventedValueEnum(E e) {
/* 29 */     super(e);
/*    */   }
/*    */   
/*    */   public EventedValueEnum(Map.Entry<String, String>[] attributes) {
/* 33 */     super(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected E valueOf(String s) throws InvalidValueException {
/* 38 */     return enumValueOf(s);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return ((Enum)getValue()).name();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 50 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract E enumValueOf(String paramString);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValueEnum.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */