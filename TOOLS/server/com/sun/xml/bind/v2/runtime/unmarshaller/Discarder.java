/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Discarder
/*    */   extends Loader
/*    */ {
/* 53 */   public static final Loader INSTANCE = new Discarder();
/*    */   
/*    */   private Discarder() {
/* 56 */     super(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void childElement(UnmarshallingContext.State state, TagName ea) {
/* 61 */     state.target = null;
/*    */     
/* 63 */     state.loader = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\Discarder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */