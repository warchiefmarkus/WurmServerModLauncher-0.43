/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import java.lang.reflect.ParameterizedType;
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
/*    */ public abstract class AbstractDatatype<V>
/*    */   implements Datatype<V>
/*    */ {
/*    */   private Datatype.Builtin builtin;
/*    */   
/*    */   protected Class<V> getValueType() {
/* 28 */     return (Class<V>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isHandlingJavaType(Class<?> type) {
/* 33 */     return getValueType().isAssignableFrom(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public V valueOf(String s) throws InvalidValueException {
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public Datatype.Builtin getBuiltin() {
/* 42 */     return this.builtin;
/*    */   }
/*    */   
/*    */   public void setBuiltin(Datatype.Builtin builtin) {
/* 46 */     this.builtin = builtin;
/*    */   }
/*    */   
/*    */   public String getString(V value) throws InvalidValueException {
/* 50 */     if (value == null) return ""; 
/* 51 */     if (!isValid(value)) {
/* 52 */       throw new InvalidValueException("Value is not valid: " + value);
/*    */     }
/* 54 */     return value.toString();
/*    */   }
/*    */   
/*    */   public boolean isValid(V value) {
/* 58 */     return (value == null || getValueType().isAssignableFrom(value.getClass()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "(" + getClass().getSimpleName() + ")";
/*    */   }
/*    */   
/*    */   public String getDisplayString() {
/* 67 */     if (this instanceof CustomDatatype)
/* 68 */       return ((CustomDatatype)this).getName(); 
/* 69 */     if (getBuiltin() != null) {
/* 70 */       return getBuiltin().getDescriptorName();
/*    */     }
/* 72 */     return getValueType().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\AbstractDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */