/*     */ package com.sun.org.apache.xml.internal.resolver.helpers;
/*     */ 
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Namespaces
/*     */ {
/*     */   public static String getPrefix(Element element) {
/*  44 */     String name = element.getTagName();
/*  45 */     String prefix = "";
/*     */     
/*  47 */     if (name.indexOf(':') > 0) {
/*  48 */       prefix = name.substring(0, name.indexOf(':'));
/*     */     }
/*     */     
/*  51 */     return prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getLocalName(Element element) {
/*  62 */     String name = element.getTagName();
/*     */     
/*  64 */     if (name.indexOf(':') > 0) {
/*  65 */       name = name.substring(name.indexOf(':') + 1);
/*     */     }
/*     */     
/*  68 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNamespaceURI(Node node, String prefix) {
/*  81 */     if (node == null || node.getNodeType() != 1) {
/*  82 */       return null;
/*     */     }
/*     */     
/*  85 */     if (prefix.equals("")) {
/*  86 */       if (((Element)node).hasAttribute("xmlns")) {
/*  87 */         return ((Element)node).getAttribute("xmlns");
/*     */       }
/*     */     } else {
/*  90 */       String nsattr = "xmlns:" + prefix;
/*  91 */       if (((Element)node).hasAttribute(nsattr)) {
/*  92 */         return ((Element)node).getAttribute(nsattr);
/*     */       }
/*     */     } 
/*     */     
/*  96 */     return getNamespaceURI(node.getParentNode(), prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNamespaceURI(Element element) {
/* 108 */     String prefix = getPrefix(element);
/* 109 */     return getNamespaceURI(element, prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\helpers\Namespaces.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */