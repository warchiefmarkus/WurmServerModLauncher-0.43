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
/*    */ final class EndTag
/*    */   extends Content
/*    */ {
/*    */   boolean concludesPendingStartTag() {
/* 28 */     return true;
/*    */   }
/*    */   
/*    */   void accept(ContentVisitor visitor) {
/* 32 */     visitor.onEndTag();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\EndTag.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */