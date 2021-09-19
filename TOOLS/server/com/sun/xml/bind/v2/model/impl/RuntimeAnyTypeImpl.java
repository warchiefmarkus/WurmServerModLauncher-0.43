/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.bind.v2.runtime.Transducer;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RuntimeAnyTypeImpl
/*    */   extends AnyTypeImpl<Type, Class>
/*    */   implements RuntimeNonElement
/*    */ {
/*    */   private RuntimeAnyTypeImpl() {
/* 50 */     super((Navigator<Type, Class, ?, ?>)Navigator.REFLECTION);
/*    */   }
/*    */   
/*    */   public <V> Transducer<V> getTransducer() {
/* 54 */     return null;
/*    */   }
/*    */   
/* 57 */   static final RuntimeNonElement theInstance = new RuntimeAnyTypeImpl();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeAnyTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */