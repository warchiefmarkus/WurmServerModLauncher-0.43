/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*    */ import com.sun.xml.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.bind.v2.model.nav.Navigator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class AnyTypeImpl<T, C>
/*    */   implements NonElement<T, C>
/*    */ {
/*    */   private final T type;
/*    */   private final Navigator<T, C, ?, ?> nav;
/*    */   
/*    */   public AnyTypeImpl(Navigator<T, C, ?, ?> nav) {
/* 60 */     this.type = (T)nav.ref(Object.class);
/* 61 */     this.nav = nav;
/*    */   }
/*    */   
/*    */   public QName getTypeName() {
/* 65 */     return name;
/*    */   }
/*    */   
/*    */   public T getType() {
/* 69 */     return this.type;
/*    */   }
/*    */   
/*    */   public Locatable getUpstream() {
/* 73 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isSimpleType() {
/* 77 */     return false;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 81 */     return this.nav.getClassLocation(this.nav.asDecl(Object.class));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean canBeReferencedByIDREF() {
/* 91 */     return true;
/*    */   }
/*    */   
/* 94 */   private static final QName name = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\AnyTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */