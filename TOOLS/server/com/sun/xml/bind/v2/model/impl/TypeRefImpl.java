/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*    */ import com.sun.xml.bind.v2.model.core.TypeRef;
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
/*    */ class TypeRefImpl<TypeT, ClassDeclT>
/*    */   implements TypeRef<TypeT, ClassDeclT>
/*    */ {
/*    */   private final QName elementName;
/*    */   private final TypeT type;
/*    */   protected final ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner;
/*    */   private NonElement<TypeT, ClassDeclT> ref;
/*    */   private final boolean isNillable;
/*    */   private String defaultValue;
/*    */   
/*    */   public TypeRefImpl(ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner, QName elementName, TypeT type, boolean isNillable, String defaultValue) {
/* 57 */     this.owner = owner;
/* 58 */     this.elementName = elementName;
/* 59 */     this.type = type;
/* 60 */     this.isNillable = isNillable;
/* 61 */     this.defaultValue = defaultValue;
/* 62 */     assert owner != null;
/* 63 */     assert elementName != null;
/* 64 */     assert type != null;
/*    */   }
/*    */   
/*    */   public NonElement<TypeT, ClassDeclT> getTarget() {
/* 68 */     if (this.ref == null)
/* 69 */       calcRef(); 
/* 70 */     return this.ref;
/*    */   }
/*    */   
/*    */   public QName getTagName() {
/* 74 */     return this.elementName;
/*    */   }
/*    */   
/*    */   public boolean isNillable() {
/* 78 */     return this.isNillable;
/*    */   }
/*    */   
/*    */   public String getDefaultValue() {
/* 82 */     return this.defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void link() {
/* 87 */     calcRef();
/*    */   }
/*    */ 
/*    */   
/*    */   private void calcRef() {
/* 92 */     this.ref = this.owner.parent.builder.getTypeInfo(this.type, this.owner);
/* 93 */     assert this.ref != null;
/*    */   }
/*    */   
/*    */   public PropertyInfo<TypeT, ClassDeclT> getSource() {
/* 97 */     return this.owner;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\TypeRefImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */