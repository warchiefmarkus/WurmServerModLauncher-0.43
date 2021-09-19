/*    */ package com.sun.xml.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.xsom.XSType;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SubstGroupBaseTypeRef
/*    */   implements Ref.Type
/*    */ {
/*    */   private final Ref.Element e;
/*    */   
/*    */   public SubstGroupBaseTypeRef(Ref.Element _e) {
/* 35 */     this.e = _e;
/*    */   }
/*    */   
/*    */   public XSType getType() {
/* 39 */     return this.e.get().getType();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\SubstGroupBaseTypeRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */