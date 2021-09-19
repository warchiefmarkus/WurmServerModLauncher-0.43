/*    */ package com.sun.xml.bind.v2.model.nav;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.WildcardType;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class WildcardTypeImpl
/*    */   implements WildcardType
/*    */ {
/*    */   private final Type[] ub;
/*    */   private final Type[] lb;
/*    */   
/*    */   public WildcardTypeImpl(Type[] ub, Type[] lb) {
/* 52 */     this.ub = ub;
/* 53 */     this.lb = lb;
/*    */   }
/*    */   
/*    */   public Type[] getUpperBounds() {
/* 57 */     return this.ub;
/*    */   }
/*    */   
/*    */   public Type[] getLowerBounds() {
/* 61 */     return this.lb;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 65 */     return Arrays.hashCode((Object[])this.lb) ^ Arrays.hashCode((Object[])this.ub);
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 69 */     if (obj instanceof WildcardType) {
/* 70 */       WildcardType that = (WildcardType)obj;
/* 71 */       return (Arrays.equals((Object[])that.getLowerBounds(), (Object[])this.lb) && Arrays.equals((Object[])that.getUpperBounds(), (Object[])this.ub));
/*    */     } 
/*    */     
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\nav\WildcardTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */