/*    */ package com.sun.xml.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
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
/*    */ 
/*    */ public class FieldAccessor_Boolean
/*    */   extends Accessor
/*    */ {
/*    */   public FieldAccessor_Boolean() {
/* 51 */     super(Boolean.class);
/*    */   }
/*    */   
/*    */   public Object get(Object bean) {
/* 55 */     return Boolean.valueOf(((Bean)bean).f_boolean);
/*    */   }
/*    */   
/*    */   public void set(Object bean, Object value) {
/* 59 */     ((Bean)bean).f_boolean = (value == null) ? Const.default_value_boolean : ((Boolean)value).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\FieldAccessor_Boolean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */