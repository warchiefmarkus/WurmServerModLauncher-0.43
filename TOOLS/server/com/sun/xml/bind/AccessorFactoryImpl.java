/*    */ package com.sun.xml.bind;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccessorFactoryImpl
/*    */   implements AccessorFactory
/*    */ {
/* 48 */   private static AccessorFactoryImpl instance = new AccessorFactoryImpl();
/*    */ 
/*    */   
/*    */   public static AccessorFactoryImpl getInstance() {
/* 52 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Accessor createFieldAccessor(Class bean, Field field, boolean readOnly) {
/* 66 */     return readOnly ? (Accessor)new Accessor.ReadOnlyFieldReflection(field) : (Accessor)new Accessor.FieldReflection(field);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Accessor createPropertyAccessor(Class bean, Method getter, Method setter) {
/* 82 */     if (getter == null) {
/* 83 */       return (Accessor)new Accessor.SetterOnlyReflection(setter);
/*    */     }
/* 85 */     if (setter == null) {
/* 86 */       return (Accessor)new Accessor.GetterOnlyReflection(getter);
/*    */     }
/* 88 */     return (Accessor)new Accessor.GetterSetterReflection(getter, setter);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\AccessorFactoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */