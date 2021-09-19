/*     */ package 1.0.com.sun.tools.xjc.util;
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
/*     */ public class DOMUtils
/*     */ {
/*     */   public static Element getFirstChildElement(Element parent, String nsUri, String localPart) {
/*  34 */     NodeList children = parent.getChildNodes();
/*  35 */     for (int i = 0; i < children.getLength(); i++) {
/*  36 */       Node item = children.item(i);
/*  37 */       if (item instanceof Element)
/*     */       {
/*  39 */         if (nsUri.equals(item.getNamespaceURI()) && localPart.equals(item.getLocalName()))
/*     */         {
/*  41 */           return (Element)item; }  } 
/*     */     } 
/*  43 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element[] getChildElements(Element parent, String nsUri, String localPart) {
/*  48 */     ArrayList a = new ArrayList();
/*  49 */     NodeList children = parent.getChildNodes();
/*  50 */     for (int i = 0; i < children.getLength(); i++) {
/*  51 */       Node item = children.item(i);
/*  52 */       if (item instanceof Element)
/*     */       {
/*  54 */         if (nsUri.equals(item.getNamespaceURI()) && localPart.equals(item.getLocalName()))
/*     */         {
/*  56 */           a.add(item); }  } 
/*     */     } 
/*  58 */     return a.<Element>toArray(new Element[a.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element[] getChildElements(Element parent) {
/*  63 */     ArrayList a = new ArrayList();
/*  64 */     NodeList children = parent.getChildNodes();
/*  65 */     for (int i = 0; i < children.getLength(); i++) {
/*  66 */       Node item = children.item(i);
/*  67 */       if (item instanceof Element)
/*     */       {
/*  69 */         a.add(item); } 
/*     */     } 
/*  71 */     return a.<Element>toArray(new Element[a.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getElementText(Element element) throws DOMException {
/*  76 */     for (Node child = element.getFirstChild(); child != null; 
/*  77 */       child = child.getNextSibling()) {
/*  78 */       if (child.getNodeType() == 3)
/*  79 */         return child.getNodeValue(); 
/*     */     } 
/*  81 */     return element.getNodeValue();
/*     */   }
/*     */   
/*     */   public static Element getElement(Document parent, String name) {
/*  85 */     NodeList children = parent.getElementsByTagName(name);
/*  86 */     if (children.getLength() >= 1)
/*  87 */       return (Element)children.item(0); 
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   public static Element getElement(Document parent, QName qname) {
/*  92 */     NodeList children = parent.getElementsByTagNameNS(qname.getNamespaceURI(), qname.getLocalPart());
/*  93 */     if (children.getLength() >= 1)
/*  94 */       return (Element)children.item(0); 
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element getElement(Document parent, String namespaceURI, String localName) {
/* 100 */     NodeList children = parent.getElementsByTagNameNS(namespaceURI, localName);
/* 101 */     if (children.getLength() >= 1)
/* 102 */       return (Element)children.item(0); 
/* 103 */     return null;
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
/* 126 */     Element[] elements = null;
/* 127 */     int len = 0;
/* 128 */     for (int i = 0; i < children.getLength(); i++) {
/* 129 */       if (elements == null)
/* 130 */         elements = new Element[1]; 
/* 131 */       if (elements.length == len) {
/* 132 */         Element[] buf = new Element[elements.length + 1];
/* 133 */         System.arraycopy(elements, 0, buf, 0, elements.length);
/* 134 */         elements = buf;
/*     */       } 
/* 136 */       elements[len++] = (Element)children.item(i);
/*     */     } 
/* 138 */     return elements;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\DOMUtils.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */