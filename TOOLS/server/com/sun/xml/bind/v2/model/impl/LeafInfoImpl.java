/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*    */ import com.sun.xml.bind.v2.model.core.LeafInfo;
/*    */ import com.sun.xml.bind.v2.runtime.Location;
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
/*    */ 
/*    */ 
/*    */ abstract class LeafInfoImpl<TypeT, ClassDeclT>
/*    */   implements LeafInfo<TypeT, ClassDeclT>, Location
/*    */ {
/*    */   private final TypeT type;
/*    */   private final QName typeName;
/*    */   
/*    */   protected LeafInfoImpl(TypeT type, QName typeName) {
/* 56 */     assert type != null;
/*    */     
/* 58 */     this.type = type;
/* 59 */     this.typeName = typeName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeT getType() {
/* 66 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean canBeReferencedByIDREF() {
/* 76 */     return false;
/*    */   }
/*    */   
/*    */   public QName getTypeName() {
/* 80 */     return this.typeName;
/*    */   }
/*    */   
/*    */   public Locatable getUpstream() {
/* 84 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Location getLocation() {
/* 91 */     return this;
/*    */   }
/*    */   
/*    */   public boolean isSimpleType() {
/* 95 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 99 */     return this.type.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\LeafInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */