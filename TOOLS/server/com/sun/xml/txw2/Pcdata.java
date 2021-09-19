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
/*    */ final class Pcdata
/*    */   extends Text
/*    */ {
/*    */   Pcdata(Document document, NamespaceResolver nsResolver, Object obj) {
/* 30 */     super(document, nsResolver, obj);
/*    */   }
/*    */   
/*    */   void accept(ContentVisitor visitor) {
/* 34 */     visitor.onPcdata(this.buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\Pcdata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */