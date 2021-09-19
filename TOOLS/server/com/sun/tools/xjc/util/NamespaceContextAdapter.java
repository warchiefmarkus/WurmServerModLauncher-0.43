/*    */ package com.sun.tools.xjc.util;
/*    */ 
/*    */ import com.sun.xml.xsom.XmlString;
/*    */ import java.util.Collections;
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
/*    */ public final class NamespaceContextAdapter
/*    */   implements NamespaceContext
/*    */ {
/*    */   private XmlString xstr;
/*    */   
/*    */   public NamespaceContextAdapter(XmlString xstr) {
/* 57 */     this.xstr = xstr;
/*    */   }
/*    */   
/*    */   public String getNamespaceURI(String prefix) {
/* 61 */     return this.xstr.resolvePrefix(prefix);
/*    */   }
/*    */   
/*    */   public String getPrefix(String namespaceURI) {
/* 65 */     return null;
/*    */   }
/*    */   
/*    */   public Iterator getPrefixes(String namespaceURI) {
/* 69 */     return Collections.EMPTY_LIST.iterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\NamespaceContextAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */