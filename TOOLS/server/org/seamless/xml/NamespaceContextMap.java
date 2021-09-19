/*    */ package org.seamless.xml;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import javax.xml.namespace.NamespaceContext;
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
/*    */ public abstract class NamespaceContextMap
/*    */   extends HashMap<String, String>
/*    */   implements NamespaceContext
/*    */ {
/*    */   public String getNamespaceURI(String prefix) {
/* 34 */     if (prefix == null)
/* 35 */       throw new IllegalArgumentException("No prefix provided!"); 
/* 36 */     if (prefix.equals(""))
/* 37 */       return getDefaultNamespaceURI(); 
/* 38 */     if (get(prefix) != null) {
/* 39 */       return get(prefix);
/*    */     }
/* 41 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPrefix(String namespaceURI) {
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator getPrefixes(String s) {
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract String getDefaultNamespaceURI();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xml\NamespaceContextMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */