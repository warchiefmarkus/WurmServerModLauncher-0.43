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
/*    */ public class FieldAccessor_Long
/*    */   extends Accessor
/*    */ {
/*    */   public FieldAccessor_Long() {
/* 51 */     super(Long.class);
/*    */   }
/*    */   
/*    */   public Object get(Object bean) {
/* 55 */     return Long.valueOf(((Bean)bean).f_long);
/*    */   }
/*    */   
/*    */   public void set(Object bean, Object value) {
/* 59 */     ((Bean)bean).f_long = (value == null) ? Const.default_value_long : ((Long)value).longValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\FieldAccessor_Long.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */