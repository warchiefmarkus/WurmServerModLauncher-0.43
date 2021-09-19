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
/*    */ public enum WildcardMode
/*    */ {
/* 45 */   STRICT(false, true), SKIP(true, false), LAX(true, true);
/*    */   
/*    */   public final boolean allowTypedObject;
/*    */   public final boolean allowDom;
/*    */   
/*    */   WildcardMode(boolean allowDom, boolean allowTypedObject) {
/* 51 */     this.allowDom = allowDom;
/* 52 */     this.allowTypedObject = allowTypedObject;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\WildcardMode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */