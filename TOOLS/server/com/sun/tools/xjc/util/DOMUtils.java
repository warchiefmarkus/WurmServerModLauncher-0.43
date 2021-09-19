/*     */ package com.sun.tools.xjc.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DOMUtils
/*     */ {
/*     */   public static Element getFirstChildElement(Element parent, String nsUri, String localPart) {
/*  65 */     NodeList children = parent.getChildNodes();
/*  66 */     for (int i = 0; i < children.getLength(); i++) {
/*  67 */       Node item = children.item(i);
/*  68 */       if (item instanceof Element)
/*     */       {
/*  70 */         if (nsUri.equals(item.getNamespaceURI()) && localPart.equals(item.getLocalName()))
/*     */         {
/*  72 */           return (Element)item; }  } 
/*     */     } 
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element[] getChildElements(Element parent, String nsUri, String localPart) {
/*  79 */     ArrayList<Node> a = new ArrayList();
/*  80 */     NodeList children = parent.getChildNodes();
/*  81 */     for (int i = 0; i < children.getLength(); i++) {
/*  82 */       Node item = children.item(i);
/*  83 */       if (item instanceof Element)
/*     */       {
/*  85 */         if (nsUri.equals(item.getNamespaceURI()) && localPart.equals(item.getLocalName()))
/*     */         {
/*  87 */           a.add(item); }  } 
/*     */     } 
/*  89 */     return a.<Element>toArray(new Element[a.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element[] getChildElements(Element parent) {
/*  94 */     ArrayList<Node> a = new ArrayList();
/*  95 */     NodeList children = parent.getChildNodes();
/*  96 */     for (int i = 0; i < children.getLength(); i++) {
/*  97 */       Node item = children.item(i);
/*  98 */       if (item instanceof Element)
/*     */       {
/* 100 */         a.add(item); } 
/*     */     } 
/* 102 */     return a.<Element>toArray(new Element[a.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getElementText(Element element) throws DOMException {
/* 107 */     for (Node child = element.getFirstChild(); child != null; 
/* 108 */       child = child.getNextSibling()) {
/* 109 */       if (child.getNodeType() == 3)
/* 110 */         return child.getNodeValue(); 
/*     */     } 
/* 112 */     return element.getNodeValue();
/*     */   }
/*     */   
/*     */   public static Element getElement(Document parent, String name) {
/* 116 */     NodeList children = parent.getElementsByTagName(name);
/* 117 */     if (children.getLength() >= 1)
/* 118 */       return (Element)children.item(0); 
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public static Element getElement(Document parent, QName qname) {
/* 123 */     NodeList children = parent.getElementsByTagNameNS(qname.getNamespaceURI(), qname.getLocalPart());
/* 124 */     if (children.getLength() >= 1)
/* 125 */       return (Element)children.item(0); 
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element getElement(Document parent, String namespaceURI, String localName) {
/* 131 */     NodeList children = parent.getElementsByTagNameNS(namespaceURI, localName);
/* 132 */     if (children.getLength() >= 1)
/* 133 */       return (Element)children.item(0); 
/* 134 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Element[] getElements(NodeList children) {
/* 157 */     Element[] elements = null;
/* 158 */     int len = 0;
/* 159 */     for (int i = 0; i < children.getLength(); i++) {
/* 160 */       if (elements == null)
/* 161 */         elements = new Element[1]; 
/* 162 */       if (elements.length == len) {
/* 163 */         Element[] buf = new Element[elements.length + 1];
/* 164 */         System.arraycopy(elements, 0, buf, 0, elements.length);
/* 165 */         elements = buf;
/*     */       } 
/* 167 */       elements[len++] = (Element)children.item(i);
/*     */     } 
/* 169 */     return elements;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\DOMUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */