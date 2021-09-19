/*     */ package org.fourthline.cling.model;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
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
/*     */ public class XMLUtil
/*     */ {
/*     */   public static String documentToString(Document document) throws Exception {
/*  55 */     return documentToString(document, true);
/*     */   }
/*     */   
/*     */   public static String documentToString(Document document, boolean standalone) throws Exception {
/*  59 */     String prol = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"" + (standalone ? "yes" : "no") + "\"?>";
/*  60 */     return prol + nodeToString(document.getDocumentElement(), new HashSet<>(), document.getDocumentElement().getNamespaceURI());
/*     */   }
/*     */   
/*     */   public static String documentToFragmentString(Document document) throws Exception {
/*  64 */     return nodeToString(document.getDocumentElement(), new HashSet<>(), document.getDocumentElement().getNamespaceURI());
/*     */   }
/*     */   
/*     */   protected static String nodeToString(Node node, Set<String> parentPrefixes, String namespaceURI) throws Exception {
/*  68 */     StringBuilder b = new StringBuilder();
/*     */     
/*  70 */     if (node == null) {
/*  71 */       return "";
/*     */     }
/*     */     
/*  74 */     if (node instanceof Element) {
/*  75 */       Element element = (Element)node;
/*  76 */       b.append("<");
/*  77 */       b.append(element.getNodeName());
/*     */       
/*  79 */       Map<String, String> thisLevelPrefixes = new HashMap<>();
/*  80 */       if (element.getPrefix() != null && !parentPrefixes.contains(element.getPrefix())) {
/*  81 */         thisLevelPrefixes.put(element.getPrefix(), element.getNamespaceURI());
/*     */       }
/*     */       
/*  84 */       if (element.hasAttributes()) {
/*  85 */         NamedNodeMap map = element.getAttributes();
/*  86 */         for (int j = 0; j < map.getLength(); j++) {
/*  87 */           Node attr = map.item(j);
/*  88 */           if (!attr.getNodeName().startsWith("xmlns")) {
/*  89 */             if (attr.getPrefix() != null && !parentPrefixes.contains(attr.getPrefix())) {
/*  90 */               thisLevelPrefixes.put(attr.getPrefix(), element.getNamespaceURI());
/*     */             }
/*  92 */             b.append(" ");
/*  93 */             b.append(attr.getNodeName());
/*  94 */             b.append("=\"");
/*  95 */             b.append(attr.getNodeValue());
/*  96 */             b.append("\"");
/*     */           } 
/*     */         } 
/*     */       } 
/* 100 */       if (namespaceURI != null && !thisLevelPrefixes.containsValue(namespaceURI) && 
/* 101 */         !namespaceURI.equals(element.getParentNode().getNamespaceURI())) {
/* 102 */         b.append(" xmlns=\"").append(namespaceURI).append("\"");
/*     */       }
/*     */       
/* 105 */       for (Map.Entry<String, String> entry : thisLevelPrefixes.entrySet()) {
/* 106 */         b.append(" xmlns:").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
/* 107 */         parentPrefixes.add(entry.getKey());
/*     */       } 
/*     */       
/* 110 */       NodeList children = element.getChildNodes();
/* 111 */       boolean hasOnlyAttributes = true; int i;
/* 112 */       for (i = 0; i < children.getLength(); i++) {
/* 113 */         Node child = children.item(i);
/* 114 */         if (child.getNodeType() != 2) {
/* 115 */           hasOnlyAttributes = false;
/*     */           break;
/*     */         } 
/*     */       } 
/* 119 */       if (!hasOnlyAttributes) {
/* 120 */         b.append(">");
/* 121 */         for (i = 0; i < children.getLength(); i++) {
/* 122 */           b.append(nodeToString(children.item(i), parentPrefixes, children.item(i).getNamespaceURI()));
/*     */         }
/* 124 */         b.append("</");
/* 125 */         b.append(element.getNodeName());
/* 126 */         b.append(">");
/*     */       } else {
/* 128 */         b.append("/>");
/*     */       } 
/*     */       
/* 131 */       for (String thisLevelPrefix : thisLevelPrefixes.keySet()) {
/* 132 */         parentPrefixes.remove(thisLevelPrefix);
/*     */       }
/*     */     }
/* 135 */     else if (node.getNodeValue() != null) {
/* 136 */       b.append(encodeText(node.getNodeValue(), node instanceof org.w3c.dom.Attr));
/*     */     } 
/*     */     
/* 139 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static String encodeText(String s) {
/* 143 */     return encodeText(s, true);
/*     */   }
/*     */   
/*     */   public static String encodeText(String s, boolean encodeQuotes) {
/* 147 */     s = s.replaceAll("&", "&amp;");
/* 148 */     s = s.replaceAll("<", "&lt;");
/* 149 */     s = s.replaceAll(">", "&gt;");
/* 150 */     if (encodeQuotes) {
/* 151 */       s = s.replaceAll("'", "&apos;");
/* 152 */       s = s.replaceAll("\"", "&quot;");
/*     */     } 
/* 154 */     return s;
/*     */   }
/*     */   
/*     */   public static Element appendNewElement(Document document, Element parent, Enum el) {
/* 158 */     return appendNewElement(document, parent, el.toString());
/*     */   }
/*     */   
/*     */   public static Element appendNewElement(Document document, Element parent, String element) {
/* 162 */     Element child = document.createElement(element);
/* 163 */     parent.appendChild(child);
/* 164 */     return child;
/*     */   }
/*     */   
/*     */   public static Element appendNewElementIfNotNull(Document document, Element parent, Enum el, Object content) {
/* 168 */     return appendNewElementIfNotNull(document, parent, el, content, (String)null);
/*     */   }
/*     */   
/*     */   public static Element appendNewElementIfNotNull(Document document, Element parent, Enum el, Object content, String namespace) {
/* 172 */     return appendNewElementIfNotNull(document, parent, el.toString(), content, namespace);
/*     */   }
/*     */   
/*     */   public static Element appendNewElementIfNotNull(Document document, Element parent, String element, Object content) {
/* 176 */     return appendNewElementIfNotNull(document, parent, element, content, (String)null);
/*     */   }
/*     */   
/*     */   public static Element appendNewElementIfNotNull(Document document, Element parent, String element, Object content, String namespace) {
/* 180 */     if (content == null) return parent; 
/* 181 */     return appendNewElement(document, parent, element, content, namespace);
/*     */   }
/*     */   
/*     */   public static Element appendNewElement(Document document, Element parent, String element, Object content) {
/* 185 */     return appendNewElement(document, parent, element, content, null);
/*     */   }
/*     */   
/*     */   public static Element appendNewElement(Document document, Element parent, String element, Object content, String namespace) {
/*     */     Element childElement;
/* 190 */     if (namespace != null) {
/* 191 */       childElement = document.createElementNS(namespace, element);
/*     */     } else {
/* 193 */       childElement = document.createElement(element);
/*     */     } 
/*     */     
/* 196 */     if (content != null)
/*     */     {
/*     */ 
/*     */       
/* 200 */       childElement.appendChild(document.createTextNode(content.toString()));
/*     */     }
/*     */     
/* 203 */     parent.appendChild(childElement);
/* 204 */     return childElement;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getTextContent(Node node) {
/* 209 */     StringBuffer buffer = new StringBuffer();
/* 210 */     NodeList childList = node.getChildNodes();
/* 211 */     for (int i = 0; i < childList.getLength(); i++) {
/* 212 */       Node child = childList.item(i);
/* 213 */       if (child.getNodeType() == 3)
/*     */       {
/* 215 */         buffer.append(child.getNodeValue()); } 
/*     */     } 
/* 217 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\XMLUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */