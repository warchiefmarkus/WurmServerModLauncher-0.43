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
/*    */ public class MethodAccessor_Float
/*    */   extends Accessor
/*    */ {
/*    */   public MethodAccessor_Float() {
/* 51 */     super(Float.class);
/*    */   }
/*    */   
/*    */   public Object get(Object bean) {
/* 55 */     return Float.valueOf(((Bean)bean).get_float());
/*    */   }
/*    */   
/*    */   public void set(Object bean, Object value) {
/* 59 */     ((Bean)bean).set_float((value == null) ? Const.default_value_float : ((Float)value).floatValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\MethodAccessor_Float.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */