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
/*    */ public class TypeClosure
/*    */   extends TypeSet
/*    */ {
/*    */   private final TypeSet typeSet;
/*    */   
/*    */   public TypeClosure(TypeSet typeSet) {
/* 38 */     this.typeSet = typeSet;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(XSType type) {
/* 47 */     if (this.typeSet.contains(type)) {
/* 48 */       return true;
/*    */     }
/* 50 */     XSType baseType = type.getBaseType();
/* 51 */     if (baseType == null) {
/* 52 */       return false;
/*    */     }
/*    */     
/* 55 */     return contains(baseType);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\TypeClosure.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */