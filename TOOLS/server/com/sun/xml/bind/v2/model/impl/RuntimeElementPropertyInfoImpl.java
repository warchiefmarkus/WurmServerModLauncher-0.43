/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*    */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class RuntimeElementPropertyInfoImpl
/*    */   extends ElementPropertyInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeElementPropertyInfo
/*    */ {
/*    */   private final Accessor acc;
/*    */   
/*    */   RuntimeElementPropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class<?>, Field, Method> seed) {
/* 59 */     super(classInfo, seed);
/* 60 */     Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed)seed).getAccessor();
/* 61 */     if (getAdapter() != null && !isCollection())
/*    */     {
/*    */       
/* 64 */       rawAcc = rawAcc.adapt(getAdapter()); } 
/* 65 */     this.acc = rawAcc;
/*    */   }
/*    */   
/*    */   public Accessor getAccessor() {
/* 69 */     return this.acc;
/*    */   }
/*    */   
/*    */   public boolean elementOnlyContent() {
/* 73 */     return true;
/*    */   }
/*    */   
/*    */   public List<? extends RuntimeTypeInfo> ref() {
/* 77 */     return (List)super.ref();
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuntimeTypeRefImpl createTypeRef(QName name, Type type, boolean isNillable, String defaultValue) {
/* 82 */     return new RuntimeTypeRefImpl(this, name, type, isNillable, defaultValue);
/*    */   }
/*    */   
/*    */   public List<RuntimeTypeRefImpl> getTypes() {
/* 86 */     return (List)super.getTypes();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeElementPropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */