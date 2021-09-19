/*    */ package com.sun.xml.xsom.util;
/*    */ 
/*    */ import com.sun.xml.xsom.XSType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TypeSet
/*    */ {
/*    */   public abstract boolean contains(XSType paramXSType);
/*    */   
/*    */   public static TypeSet intersection(final TypeSet a, final TypeSet b) {
/* 51 */     return new TypeSet() {
/*    */         public boolean contains(XSType type) {
/* 53 */           return (a.contains(type) && b.contains(type));
/*    */         }
/*    */       };
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
/*    */   public static TypeSet union(final TypeSet a, final TypeSet b) {
/* 67 */     return new TypeSet() {
/*    */         public boolean contains(XSType type) {
/* 69 */           return (a.contains(type) || b.contains(type));
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\TypeSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */