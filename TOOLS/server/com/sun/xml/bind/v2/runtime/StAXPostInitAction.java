/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import javax.xml.namespace.NamespaceContext;
/*    */ import javax.xml.stream.XMLEventWriter;
/*    */ import javax.xml.stream.XMLStreamWriter;
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
/*    */ 
/*    */ 
/*    */ final class StAXPostInitAction
/*    */   implements Runnable
/*    */ {
/*    */   private final XMLStreamWriter xsw;
/*    */   private final XMLEventWriter xew;
/*    */   private final NamespaceContext nsc;
/*    */   private final XMLSerializer serializer;
/*    */   
/*    */   StAXPostInitAction(XMLStreamWriter xsw, XMLSerializer serializer) {
/* 61 */     this.xsw = xsw;
/* 62 */     this.xew = null;
/* 63 */     this.nsc = null;
/* 64 */     this.serializer = serializer;
/*    */   }
/*    */   
/*    */   StAXPostInitAction(XMLEventWriter xew, XMLSerializer serializer) {
/* 68 */     this.xsw = null;
/* 69 */     this.xew = xew;
/* 70 */     this.nsc = null;
/* 71 */     this.serializer = serializer;
/*    */   }
/*    */   
/*    */   StAXPostInitAction(NamespaceContext nsc, XMLSerializer serializer) {
/* 75 */     this.xsw = null;
/* 76 */     this.xew = null;
/* 77 */     this.nsc = nsc;
/* 78 */     this.serializer = serializer;
/*    */   }
/*    */   
/*    */   public void run() {
/* 82 */     NamespaceContext ns = this.nsc;
/* 83 */     if (this.xsw != null) ns = this.xsw.getNamespaceContext(); 
/* 84 */     if (this.xew != null) ns = this.xew.getNamespaceContext();
/*    */ 
/*    */ 
/*    */     
/* 88 */     if (ns == null) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 94 */     for (String nsUri : this.serializer.grammar.nameList.namespaceURIs) {
/* 95 */       String p = ns.getPrefix(nsUri);
/* 96 */       if (p != null)
/* 97 */         this.serializer.addInscopeBinding(nsUri, p); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\StAXPostInitAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */