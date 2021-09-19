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
/*    */ public class MethodAccessor_Character
/*    */   extends Accessor
/*    */ {
/*    */   public MethodAccessor_Character() {
/* 51 */     super(Character.class);
/*    */   }
/*    */   
/*    */   public Object get(Object bean) {
/* 55 */     return Character.valueOf(((Bean)bean).get_char());
/*    */   }
/*    */   
/*    */   public void set(Object bean, Object value) {
/* 59 */     ((Bean)bean).set_char((value == null) ? Const.default_value_char : ((Character)value).charValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\MethodAccessor_Character.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */