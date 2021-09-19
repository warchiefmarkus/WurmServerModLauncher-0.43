/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeValuePropertyInfo;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RuntimeValuePropertyInfoImpl
/*    */   extends ValuePropertyInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeValuePropertyInfo
/*    */ {
/*    */   RuntimeValuePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class<?>, Field, Method> seed) {
/* 54 */     super(classInfo, seed);
/*    */   }
/*    */   
/*    */   public boolean elementOnlyContent() {
/* 58 */     return false;
/*    */   }
/*    */   
/*    */   public RuntimePropertyInfo getSource() {
/* 62 */     return (RuntimePropertyInfo)super.getSource();
/*    */   }
/*    */   
/*    */   public RuntimeNonElement getTarget() {
/* 66 */     return (RuntimeNonElement)super.getTarget();
/*    */   }
/*    */   
/*    */   public List<? extends RuntimeNonElement> ref() {
/* 70 */     return (List)super.ref();
/*    */   }
/*    */   
/*    */   public void link() {
/* 74 */     getTransducer();
/* 75 */     super.link();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeValuePropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */