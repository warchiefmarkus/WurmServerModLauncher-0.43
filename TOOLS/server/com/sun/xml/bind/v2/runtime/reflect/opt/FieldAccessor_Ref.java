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
/*    */ public class FieldAccessor_Ref
/*    */   extends Accessor
/*    */ {
/*    */   public FieldAccessor_Ref() {
/* 48 */     super(Ref.class);
/*    */   }
/*    */   
/*    */   public Object get(Object bean) {
/* 52 */     return ((Bean)bean).f_ref;
/*    */   }
/*    */   
/*    */   public void set(Object bean, Object value) {
/* 56 */     ((Bean)bean).f_ref = (Ref)value;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\FieldAccessor_Ref.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */