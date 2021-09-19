/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class DOMUtil
/*     */ {
/*     */   static final String getAttribute(Element e, String attName) {
/*  51 */     if (e.getAttributeNode(attName) == null) return null; 
/*  52 */     return e.getAttribute(attName);
/*     */   }
/*     */   
/*     */   public static String getAttribute(Element e, String nsUri, String local) {
/*  56 */     if (e.getAttributeNodeNS(nsUri, local) == null) return null; 
/*  57 */     return e.getAttributeNS(nsUri, local);
/*     */   }
/*     */   
/*     */   public static Element getElement(Element e, String nsUri, String localName) {
/*  61 */     NodeList l = e.getChildNodes();
/*  62 */     for (int i = 0; i < l.getLength(); i++) {
/*  63 */       Node n = l.item(i);
/*  64 */       if (n.getNodeType() == 1) {
/*  65 */         Element r = (Element)n;
/*  66 */         if (equals(r.getLocalName(), localName) && equals(fixNull(r.getNamespaceURI()), nsUri))
/*  67 */           return r; 
/*     */       } 
/*     */     } 
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean equals(String a, String b) {
/*  78 */     if (a == b) return true; 
/*  79 */     if (a == null || b == null) return false; 
/*  80 */     return a.equals(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String fixNull(String s) {
/*  87 */     if (s == null) return ""; 
/*  88 */     return s;
/*     */   }
/*     */   
/*     */   public static Element getElement(Element e, String localName) {
/*  92 */     return getElement(e, "", localName);
/*     */   }
/*     */   
/*     */   public static List<Element> getChildElements(Element e) {
/*  96 */     List<Element> r = new ArrayList<Element>();
/*  97 */     NodeList l = e.getChildNodes();
/*  98 */     for (int i = 0; i < l.getLength(); i++) {
/*  99 */       Node n = l.item(i);
/* 100 */       if (n.getNodeType() == 1)
/* 101 */         r.add((Element)n); 
/*     */     } 
/* 103 */     return r;
/*     */   }
/*     */   
/*     */   public static List<Element> getChildElements(Element e, String localName) {
/* 107 */     List<Element> r = new ArrayList<Element>();
/* 108 */     NodeList l = e.getChildNodes();
/* 109 */     for (int i = 0; i < l.getLength(); i++) {
/* 110 */       Node n = l.item(i);
/* 111 */       if (n.getNodeType() == 1) {
/* 112 */         Element c = (Element)n;
/* 113 */         if (c.getLocalName().equals(localName))
/* 114 */           r.add(c); 
/*     */       } 
/*     */     } 
/* 117 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\DOMUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */