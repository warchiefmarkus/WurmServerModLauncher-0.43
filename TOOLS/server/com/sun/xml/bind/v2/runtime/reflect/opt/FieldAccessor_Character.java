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
/*    */ public class FieldAccessor_Character
/*    */   extends Accessor
/*    */ {
/*    */   public FieldAccessor_Character() {
/* 51 */     super(Character.class);
/*    */   }
/*    */   
/*    */   public Object get(Object bean) {
/* 55 */     return Character.valueOf(((Bean)bean).f_char);
/*    */   }
/*    */   
/*    */   public void set(Object bean, Object value) {
/* 59 */     ((Bean)bean).f_char = (value == null) ? Const.default_value_char : ((Character)value).charValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\FieldAccessor_Character.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */