/*    */ package com.sun.xml.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UnmarshallerChain
/*    */ {
/* 62 */   private int offset = 0;
/*    */   
/*    */   public final JAXBContextImpl context;
/*    */   
/*    */   public UnmarshallerChain(JAXBContextImpl context) {
/* 67 */     this.context = context;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int allocateOffset() {
/* 74 */     return this.offset++;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getScopeSize() {
/* 81 */     return this.offset;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\UnmarshallerChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */