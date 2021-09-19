/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.xml.bind.unmarshaller.DOMScanner;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DOMForestScanner
/*     */ {
/*     */   private final DOMForest forest;
/*     */   
/*     */   public DOMForestScanner(DOMForest _forest) {
/*  73 */     this.forest = _forest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan(Element e, ContentHandler contentHandler) throws SAXException {
/*  81 */     DOMScanner scanner = new DOMScanner();
/*     */ 
/*     */     
/*  84 */     LocationResolver resolver = new LocationResolver(scanner);
/*  85 */     resolver.setContentHandler(contentHandler);
/*     */ 
/*     */     
/*  88 */     scanner.setContentHandler(resolver);
/*  89 */     scanner.scan(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan(Document d, ContentHandler contentHandler) throws SAXException {
/*  97 */     scan(d.getDocumentElement(), contentHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class LocationResolver
/*     */     extends XMLFilterImpl
/*     */     implements Locator
/*     */   {
/*     */     private final DOMScanner parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean inStart;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     LocationResolver(DOMScanner _parent) {
/* 125 */       this.inStart = false;
/*     */       this.parent = _parent;
/*     */     }
/*     */     
/*     */     public void setDocumentLocator(Locator locator) {
/* 130 */       super.setDocumentLocator(this);
/*     */     }
/*     */     
/*     */     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 134 */       this.inStart = false;
/* 135 */       super.endElement(namespaceURI, localName, qName);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 140 */       this.inStart = true;
/* 141 */       super.startElement(namespaceURI, localName, qName, atts);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Locator findLocator() {
/* 148 */       Node n = this.parent.getCurrentLocation();
/* 149 */       if (n instanceof Element) {
/* 150 */         Element e = (Element)n;
/* 151 */         if (this.inStart) {
/* 152 */           return DOMForestScanner.this.forest.locatorTable.getStartLocation(e);
/*     */         }
/* 154 */         return DOMForestScanner.this.forest.locatorTable.getEndLocation(e);
/*     */       } 
/* 156 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 165 */       Locator l = findLocator();
/* 166 */       if (l != null) return l.getColumnNumber(); 
/* 167 */       return -1;
/*     */     }
/*     */     
/*     */     public int getLineNumber() {
/* 171 */       Locator l = findLocator();
/* 172 */       if (l != null) return l.getLineNumber(); 
/* 173 */       return -1;
/*     */     }
/*     */     
/*     */     public String getPublicId() {
/* 177 */       Locator l = findLocator();
/* 178 */       if (l != null) return l.getPublicId(); 
/* 179 */       return null;
/*     */     }
/*     */     
/*     */     public String getSystemId() {
/* 183 */       Locator l = findLocator();
/* 184 */       if (l != null) return l.getSystemId(); 
/* 185 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\DOMForestScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */