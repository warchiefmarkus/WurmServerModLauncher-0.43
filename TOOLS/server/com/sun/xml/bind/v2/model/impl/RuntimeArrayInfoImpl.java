/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*    */ import com.sun.xml.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeArrayInfo;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.bind.v2.runtime.Transducer;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
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
/*    */ final class RuntimeArrayInfoImpl
/*    */   extends ArrayInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeArrayInfo
/*    */ {
/*    */   RuntimeArrayInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class arrayType) {
/* 53 */     super(builder, upstream, arrayType);
/*    */   }
/*    */   
/*    */   public Class getType() {
/* 57 */     return (Class)super.getType();
/*    */   }
/*    */   
/*    */   public RuntimeNonElement getItemType() {
/* 61 */     return (RuntimeNonElement)super.getItemType();
/*    */   }
/*    */   
/*    */   public <V> Transducer<V> getTransducer() {
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeArrayInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */