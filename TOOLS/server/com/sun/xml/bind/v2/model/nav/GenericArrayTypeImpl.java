/*    */ package com.sun.xml.bind.v2.model.nav;
/*    */ 
/*    */ import java.lang.reflect.GenericArrayType;
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
/*    */ final class GenericArrayTypeImpl
/*    */   implements GenericArrayType
/*    */ {
/*    */   private Type genericComponentType;
/*    */   
/*    */   GenericArrayTypeImpl(Type ct) {
/* 49 */     assert ct != null;
/* 50 */     this.genericComponentType = ct;
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
/*    */   public Type getGenericComponentType() {
/* 62 */     return this.genericComponentType;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 66 */     Type componentType = getGenericComponentType();
/* 67 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 69 */     if (componentType instanceof Class) {
/* 70 */       sb.append(((Class)componentType).getName());
/*    */     } else {
/* 72 */       sb.append(componentType.toString());
/* 73 */     }  sb.append("[]");
/* 74 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 79 */     if (o instanceof GenericArrayType) {
/* 80 */       GenericArrayType that = (GenericArrayType)o;
/*    */       
/* 82 */       Type thatComponentType = that.getGenericComponentType();
/* 83 */       return this.genericComponentType.equals(thatComponentType);
/*    */     } 
/* 85 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 90 */     return this.genericComponentType.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\nav\GenericArrayTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */