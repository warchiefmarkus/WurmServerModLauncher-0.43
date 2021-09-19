/*    */ package com.sun.xml.txw2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class Text
/*    */   extends Content
/*    */ {
/* 32 */   protected final StringBuilder buffer = new StringBuilder();
/*    */   
/*    */   protected Text(Document document, NamespaceResolver nsResolver, Object obj) {
/* 35 */     document.writeValue(obj, nsResolver, this.buffer);
/*    */   }
/*    */   
/*    */   boolean concludesPendingStartTag() {
/* 39 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\Text.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */