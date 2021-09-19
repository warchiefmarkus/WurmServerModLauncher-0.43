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
/*    */ final class Comment
/*    */   extends Content
/*    */ {
/* 32 */   private final StringBuilder buffer = new StringBuilder();
/*    */   
/*    */   public Comment(Document document, NamespaceResolver nsResolver, Object obj) {
/* 35 */     document.writeValue(obj, nsResolver, this.buffer);
/*    */   }
/*    */   
/*    */   boolean concludesPendingStartTag() {
/* 39 */     return false;
/*    */   }
/*    */   
/*    */   void accept(ContentVisitor visitor) {
/* 43 */     visitor.onComment(this.buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\Comment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */