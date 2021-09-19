/*    */ package com.sun.xml.bind.v2.model.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum PropertyKind
/*    */ {
/* 55 */   VALUE(true, false, 2147483647),
/* 56 */   ATTRIBUTE(false, false, 2147483647),
/* 57 */   ELEMENT(true, true, 0),
/* 58 */   REFERENCE(false, true, 1),
/* 59 */   MAP(false, true, 2);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean canHaveXmlMimeType;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isOrdered;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final int propertyIndex;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   PropertyKind(boolean canHaveExpectedContentType, boolean isOrdered, int propertyIndex) {
/* 80 */     this.canHaveXmlMimeType = canHaveExpectedContentType;
/* 81 */     this.isOrdered = isOrdered;
/* 82 */     this.propertyIndex = propertyIndex;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\PropertyKind.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */