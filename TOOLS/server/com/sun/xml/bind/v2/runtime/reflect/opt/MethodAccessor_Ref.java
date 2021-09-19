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
/*    */ public class MethodAccessor_Ref
/*    */   extends Accessor
/*    */ {
/*    */   public MethodAccessor_Ref() {
/* 48 */     super(Ref.class);
/*    */   }
/*    */   
/*    */   public Object get(Object bean) {
/* 52 */     return ((Bean)bean).get_ref();
/*    */   }
/*    */   
/*    */   public void set(Object bean, Object value) {
/* 56 */     ((Bean)bean).set_ref((Ref)value);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\MethodAccessor_Ref.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */