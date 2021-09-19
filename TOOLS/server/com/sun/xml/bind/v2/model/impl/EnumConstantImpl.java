/*    */ package com.sun.xml.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.core.EnumConstant;
/*    */ import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class EnumConstantImpl<T, C, F, M>
/*    */   implements EnumConstant<T, C>
/*    */ {
/*    */   protected final String lexical;
/*    */   protected final EnumLeafInfoImpl<T, C, F, M> owner;
/*    */   protected final String name;
/*    */   protected final EnumConstantImpl<T, C, F, M> next;
/*    */   
/*    */   public EnumConstantImpl(EnumLeafInfoImpl<T, C, F, M> owner, String name, String lexical, EnumConstantImpl<T, C, F, M> next) {
/* 56 */     this.lexical = lexical;
/* 57 */     this.owner = owner;
/* 58 */     this.name = name;
/* 59 */     this.next = next;
/*    */   }
/*    */   
/*    */   public EnumLeafInfo<T, C> getEnclosingClass() {
/* 63 */     return this.owner;
/*    */   }
/*    */   
/*    */   public final String getLexicalValue() {
/* 67 */     return this.lexical;
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 71 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\EnumConstantImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */