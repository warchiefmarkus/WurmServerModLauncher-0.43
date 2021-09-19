/*    */ package org.fourthline.cling.model.state;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.seamless.util.Reflections;
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
/*    */ 
/*    */ public class FieldStateVariableAccessor
/*    */   extends StateVariableAccessor
/*    */ {
/*    */   protected Field field;
/*    */   
/*    */   public FieldStateVariableAccessor(Field field) {
/* 32 */     this.field = field;
/*    */   }
/*    */   
/*    */   public Field getField() {
/* 36 */     return this.field;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getReturnType() {
/* 41 */     return getField().getType();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object read(Object serviceImpl) throws Exception {
/* 46 */     return Reflections.get(this.field, serviceImpl);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return super.toString() + " Field: " + getField();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\state\FieldStateVariableAccessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */