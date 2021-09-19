/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.w3c.dom.Attr;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
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
/*    */ 
/*    */ final class DomPostInitAction
/*    */   implements Runnable
/*    */ {
/*    */   private final Node node;
/*    */   private final XMLSerializer serializer;
/*    */   
/*    */   DomPostInitAction(Node node, XMLSerializer serializer) {
/* 62 */     this.node = node;
/* 63 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 68 */     Set<String> declaredPrefixes = new HashSet<String>();
/* 69 */     for (Node n = this.node; n != null && n.getNodeType() == 1; n = n.getParentNode()) {
/* 70 */       NamedNodeMap atts = n.getAttributes();
/* 71 */       if (atts != null)
/* 72 */         for (int i = 0; i < atts.getLength(); i++) {
/* 73 */           Attr a = (Attr)atts.item(i);
/* 74 */           String nsUri = a.getNamespaceURI();
/* 75 */           if (nsUri != null && nsUri.equals("http://www.w3.org/2000/xmlns/")) {
/*    */             
/* 77 */             String prefix = a.getLocalName();
/* 78 */             if (prefix != null) {
/*    */               
/* 80 */               if (prefix.equals("xmlns")) {
/* 81 */                 prefix = "";
/*    */               }
/* 83 */               String value = a.getValue();
/* 84 */               if (value != null)
/*    */               {
/* 86 */                 if (declaredPrefixes.add(prefix))
/* 87 */                   this.serializer.addInscopeBinding(value, prefix); 
/*    */               }
/*    */             } 
/*    */           } 
/*    */         }  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\DomPostInitAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */