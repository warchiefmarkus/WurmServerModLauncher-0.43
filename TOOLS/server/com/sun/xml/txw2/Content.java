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
/*    */ abstract class Content
/*    */ {
/*    */   private Content next;
/*    */   
/*    */   final Content getNext() {
/* 33 */     return this.next;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   final void setNext(Document doc, Content next) {
/* 45 */     assert next != null;
/* 46 */     assert this.next == null : "next of " + this + " is already set to " + this.next;
/* 47 */     this.next = next;
/* 48 */     doc.run();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isReadyToCommit() {
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   abstract boolean concludesPendingStartTag();
/*    */   
/*    */   abstract void accept(ContentVisitor paramContentVisitor);
/*    */   
/*    */   public void written() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\Content.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */