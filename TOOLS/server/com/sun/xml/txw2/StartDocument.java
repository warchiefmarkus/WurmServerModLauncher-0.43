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
/*    */ final class StartDocument
/*    */   extends Content
/*    */ {
/*    */   boolean concludesPendingStartTag() {
/* 28 */     return true;
/*    */   }
/*    */   
/*    */   void accept(ContentVisitor visitor) {
/* 32 */     visitor.onStartDocument();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\StartDocument.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */