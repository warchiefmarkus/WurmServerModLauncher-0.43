/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NamespaceContextImpl
/*     */   implements NamespaceContext
/*     */ {
/*     */   private final Element e;
/*     */   
/*     */   public NamespaceContextImpl(Element e) {
/*  59 */     this.e = e;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  78 */     Node parent = this.e;
/*  79 */     String namespace = null;
/*  80 */     String prefixColon = prefix + ':';
/*     */     
/*  82 */     if (prefix.equals("xml")) {
/*  83 */       namespace = "http://www.w3.org/XML/1998/namespace";
/*     */     } else {
/*     */       int type;
/*     */ 
/*     */       
/*  88 */       while (null != parent && null == namespace && ((type = parent.getNodeType()) == 1 || type == 5)) {
/*     */         
/*  90 */         if (type == 1) {
/*  91 */           if (parent.getNodeName().startsWith(prefixColon))
/*  92 */             return parent.getNamespaceURI(); 
/*  93 */           NamedNodeMap nnm = parent.getAttributes();
/*     */           
/*  95 */           for (int i = 0; i < nnm.getLength(); i++) {
/*  96 */             Node attr = nnm.item(i);
/*  97 */             String aname = attr.getNodeName();
/*  98 */             boolean isPrefix = aname.startsWith("xmlns:");
/*     */             
/* 100 */             if (isPrefix || aname.equals("xmlns")) {
/* 101 */               int index = aname.indexOf(':');
/* 102 */               String p = isPrefix ? aname.substring(index + 1) : "";
/*     */               
/* 104 */               if (p.equals(prefix)) {
/* 105 */                 namespace = attr.getNodeValue();
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 113 */         parent = parent.getParentNode();
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     if (prefix.equals(""))
/* 118 */       return ""; 
/* 119 */     return namespace;
/*     */   }
/*     */   
/*     */   public String getPrefix(String namespaceURI) {
/* 123 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Iterator getPrefixes(String namespaceURI) {
/* 127 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\NamespaceContextImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */