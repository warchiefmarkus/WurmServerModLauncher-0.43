/*     */ package com.sun.xml.bind.v2.model.nav;
/*     */ 
/*     */ import java.lang.reflect.MalformedParameterizedTypeException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ParameterizedTypeImpl
/*     */   implements ParameterizedType
/*     */ {
/*     */   private Type[] actualTypeArguments;
/*     */   private Class<?> rawType;
/*     */   private Type ownerType;
/*     */   
/*     */   ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
/*  57 */     this.actualTypeArguments = actualTypeArguments;
/*  58 */     this.rawType = rawType;
/*  59 */     if (ownerType != null) {
/*  60 */       this.ownerType = ownerType;
/*     */     } else {
/*  62 */       this.ownerType = rawType.getDeclaringClass();
/*     */     } 
/*  64 */     validateConstructorArguments();
/*     */   }
/*     */   
/*     */   private void validateConstructorArguments() {
/*  68 */     TypeVariable[] formals = (TypeVariable[])this.rawType.getTypeParameters();
/*     */     
/*  70 */     if (formals.length != this.actualTypeArguments.length) {
/*  71 */       throw new MalformedParameterizedTypeException();
/*     */     }
/*  73 */     for (int i = 0; i < this.actualTypeArguments.length; i++);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] getActualTypeArguments() {
/*  80 */     return (Type[])this.actualTypeArguments.clone();
/*     */   }
/*     */   
/*     */   public Class<?> getRawType() {
/*  84 */     return this.rawType;
/*     */   }
/*     */   
/*     */   public Type getOwnerType() {
/*  88 */     return this.ownerType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 100 */     if (o instanceof ParameterizedType) {
/*     */       
/* 102 */       ParameterizedType that = (ParameterizedType)o;
/*     */       
/* 104 */       if (this == that) {
/* 105 */         return true;
/*     */       }
/* 107 */       Type thatOwner = that.getOwnerType();
/* 108 */       Type thatRawType = that.getRawType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 130 */       if ((this.ownerType == null) ? (thatOwner == null) : this.ownerType.equals(thatOwner)) if (((this.rawType == null) ? (thatRawType == null) : this.rawType.equals(thatRawType)) && Arrays.equals((Object[])this.actualTypeArguments, (Object[])that.getActualTypeArguments()));  return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 145 */     return Arrays.hashCode((Object[])this.actualTypeArguments) ^ ((this.ownerType == null) ? 0 : this.ownerType.hashCode()) ^ ((this.rawType == null) ? 0 : this.rawType.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 153 */     if (this.ownerType != null)
/* 154 */     { if (this.ownerType instanceof Class) {
/* 155 */         sb.append(((Class)this.ownerType).getName());
/*     */       } else {
/* 157 */         sb.append(this.ownerType.toString());
/*     */       } 
/* 159 */       sb.append(".");
/*     */       
/* 161 */       if (this.ownerType instanceof ParameterizedTypeImpl) {
/*     */ 
/*     */         
/* 164 */         sb.append(this.rawType.getName().replace(((ParameterizedTypeImpl)this.ownerType).rawType.getName() + "$", ""));
/*     */       } else {
/*     */         
/* 167 */         sb.append(this.rawType.getName());
/*     */       }  }
/* 169 */     else { sb.append(this.rawType.getName()); }
/*     */     
/* 171 */     if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
/*     */       
/* 173 */       sb.append("<");
/* 174 */       boolean first = true;
/* 175 */       for (Type t : this.actualTypeArguments) {
/* 176 */         if (!first)
/* 177 */           sb.append(", "); 
/* 178 */         if (t instanceof Class) {
/* 179 */           sb.append(((Class)t).getName());
/*     */         } else {
/* 181 */           sb.append(t.toString());
/* 182 */         }  first = false;
/*     */       } 
/* 184 */       sb.append(">");
/*     */     } 
/*     */     
/* 187 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\nav\ParameterizedTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */