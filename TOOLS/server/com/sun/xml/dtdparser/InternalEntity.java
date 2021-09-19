/*    */ package com.sun.xml.dtdparser;
/*    */ 
/*    */ 
/*    */ final class InternalEntity
/*    */   extends EntityDecl
/*    */ {
/*    */   char[] buf;
/*    */   
/*    */   InternalEntity(String name, char[] value) {
/* 10 */     this.name = name;
/* 11 */     this.buf = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\dtdparser\InternalEntity.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */