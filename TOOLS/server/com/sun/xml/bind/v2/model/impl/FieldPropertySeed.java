/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*    */ import com.sun.xml.bind.v2.runtime.Location;
/*    */ import java.lang.annotation.Annotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FieldPropertySeed<TypeT, ClassDeclT, FieldT, MethodT>
/*    */   implements PropertySeed<TypeT, ClassDeclT, FieldT, MethodT>
/*    */ {
/*    */   protected final FieldT field;
/*    */   private ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent;
/*    */   
/*    */   FieldPropertySeed(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> classInfo, FieldT field) {
/* 55 */     this.parent = classInfo;
/* 56 */     this.field = field;
/*    */   }
/*    */   
/*    */   public <A extends Annotation> A readAnnotation(Class<A> a) {
/* 60 */     return (A)this.parent.reader().getFieldAnnotation(a, this.field, this);
/*    */   }
/*    */   
/*    */   public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 64 */     return this.parent.reader().hasFieldAnnotation(annotationType, this.field);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 70 */     return this.parent.nav().getFieldName(this.field);
/*    */   }
/*    */   
/*    */   public TypeT getRawType() {
/* 74 */     return (TypeT)this.parent.nav().getFieldType(this.field);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Locatable getUpstream() {
/* 81 */     return this.parent;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 85 */     return this.parent.nav().getFieldLocation(this.field);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\FieldPropertySeed.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */