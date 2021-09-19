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
/*    */ final class NamespaceDecl
/*    */ {
/*    */   final String uri;
/*    */   boolean requirePrefix;
/*    */   final String dummyPrefix;
/*    */   final char uniqueId;
/*    */   String prefix;
/*    */   boolean declared;
/*    */   NamespaceDecl next;
/*    */   
/*    */   NamespaceDecl(char uniqueId, String uri, String prefix, boolean requirePrefix) {
/* 57 */     this.dummyPrefix = (new StringBuilder(2)).append(false).append(uniqueId).toString();
/* 58 */     this.uri = uri;
/* 59 */     this.prefix = prefix;
/* 60 */     this.requirePrefix = requirePrefix;
/* 61 */     this.uniqueId = uniqueId;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\NamespaceDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */