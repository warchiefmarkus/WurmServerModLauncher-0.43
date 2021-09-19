/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.xml.bind.marshaller.SAX2DOMEx;
/*     */ import java.util.Set;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
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
/*     */ class DOMBuilder
/*     */   extends SAX2DOMEx
/*     */ {
/*     */   private final LocatorTable locatorTable;
/*     */   private final Set outerMostBindings;
/*     */   private Locator locator;
/*     */   
/*     */   public DOMBuilder(Document dom, LocatorTable ltable, Set outerMostBindings) {
/*  69 */     super(dom);
/*  70 */     this.locatorTable = ltable;
/*  71 */     this.outerMostBindings = outerMostBindings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/*  82 */     this.locator = locator;
/*  83 */     super.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
/*  88 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  90 */     Element e = getCurrentElement();
/*  91 */     this.locatorTable.storeStartLocation(e, this.locator);
/*     */ 
/*     */     
/*  94 */     if ("http://java.sun.com/xml/ns/jaxb".equals(e.getNamespaceURI()) && "bindings".equals(e.getLocalName())) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 100 */       Node p = e.getParentNode();
/* 101 */       if (p instanceof Document || (p instanceof Element && !e.getNamespaceURI().equals(p.getNamespaceURI())))
/*     */       {
/* 103 */         this.outerMostBindings.add(e);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) {
/* 109 */     this.locatorTable.storeEndLocation(getCurrentElement(), this.locator);
/* 110 */     super.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\DOMBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */