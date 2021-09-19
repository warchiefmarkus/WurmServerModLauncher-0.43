/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*    */ import com.sun.xml.bind.v2.model.core.ValuePropertyInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ValuePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*    */   extends SingleTypePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*    */   implements ValuePropertyInfo<TypeT, ClassDeclT>
/*    */ {
/*    */   ValuePropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> seed) {
/* 53 */     super(parent, seed);
/*    */   }
/*    */   
/*    */   public PropertyKind kind() {
/* 57 */     return PropertyKind.VALUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ValuePropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */