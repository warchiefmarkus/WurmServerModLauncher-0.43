/*    */ package com.sun.xml.bind.v2.model.nav;
/*    */ 
/*    */ import java.lang.reflect.GenericArrayType;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
/*    */ import java.lang.reflect.WildcardType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class TypeVisitor<T, P>
/*    */ {
/*    */   public final T visit(Type t, P param) {
/* 50 */     assert t != null;
/*    */     
/* 52 */     if (t instanceof Class)
/* 53 */       return onClass((Class)t, param); 
/* 54 */     if (t instanceof ParameterizedType)
/* 55 */       return onParameterizdType((ParameterizedType)t, param); 
/* 56 */     if (t instanceof GenericArrayType)
/* 57 */       return onGenericArray((GenericArrayType)t, param); 
/* 58 */     if (t instanceof WildcardType)
/* 59 */       return onWildcard((WildcardType)t, param); 
/* 60 */     if (t instanceof TypeVariable) {
/* 61 */       return onVariable((TypeVariable)t, param);
/*    */     }
/*    */     
/*    */     assert false;
/* 65 */     throw new IllegalArgumentException();
/*    */   }
/*    */   
/*    */   protected abstract T onClass(Class paramClass, P paramP);
/*    */   
/*    */   protected abstract T onParameterizdType(ParameterizedType paramParameterizedType, P paramP);
/*    */   
/*    */   protected abstract T onGenericArray(GenericArrayType paramGenericArrayType, P paramP);
/*    */   
/*    */   protected abstract T onVariable(TypeVariable paramTypeVariable, P paramP);
/*    */   
/*    */   protected abstract T onWildcard(WildcardType paramWildcardType, P paramP);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\nav\TypeVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */