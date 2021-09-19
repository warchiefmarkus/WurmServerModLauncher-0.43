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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Attribute
/*    */ {
/*    */   final String nsUri;
/*    */   final String localName;
/*    */   Attribute next;
/* 38 */   final StringBuilder value = new StringBuilder();
/*    */   
/*    */   Attribute(String nsUri, String localName) {
/* 41 */     assert nsUri != null && localName != null;
/*    */     
/* 43 */     this.nsUri = nsUri;
/* 44 */     this.localName = localName;
/*    */   }
/*    */   
/*    */   boolean hasName(String nsUri, String localName) {
/* 48 */     return (this.localName.equals(localName) && this.nsUri.equals(nsUri));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\Attribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */