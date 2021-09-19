/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeElement;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
/*    */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class RuntimeReferencePropertyInfoImpl
/*    */   extends ReferencePropertyInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeReferencePropertyInfo
/*    */ {
/*    */   private final Accessor acc;
/*    */   
/*    */   public RuntimeReferencePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class<?>, Field, Method> seed) {
/* 57 */     super(classInfo, seed);
/* 58 */     Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed)seed).getAccessor();
/* 59 */     if (getAdapter() != null && !isCollection())
/*    */     {
/*    */       
/* 62 */       rawAcc = rawAcc.adapt(getAdapter()); } 
/* 63 */     this.acc = rawAcc;
/*    */   }
/*    */   
/*    */   public Set<? extends RuntimeElement> getElements() {
/* 67 */     return (Set)super.getElements();
/*    */   }
/*    */   
/*    */   public Set<? extends RuntimeElement> ref() {
/* 71 */     return (Set)super.ref();
/*    */   }
/*    */   
/*    */   public Accessor getAccessor() {
/* 75 */     return this.acc;
/*    */   }
/*    */   
/*    */   public boolean elementOnlyContent() {
/* 79 */     return !isMixed();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeReferencePropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */