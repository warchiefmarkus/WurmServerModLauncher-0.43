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
/*    */ final class EndDocument
/*    */   extends Content
/*    */ {
/*    */   boolean concludesPendingStartTag() {
/* 28 */     return true;
/*    */   }
/*    */   
/*    */   void accept(ContentVisitor visitor) {
/* 32 */     visitor.onEndDocument();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\EndDocument.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */