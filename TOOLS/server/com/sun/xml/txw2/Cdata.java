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
/*    */ final class Cdata
/*    */   extends Text
/*    */ {
/*    */   Cdata(Document document, NamespaceResolver nsResolver, Object obj) {
/* 30 */     super(document, nsResolver, obj);
/*    */   }
/*    */   
/*    */   void accept(ContentVisitor visitor) {
/* 34 */     visitor.onCdata(this.buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\Cdata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */