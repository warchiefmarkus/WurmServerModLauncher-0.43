/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InterningXmlVisitor
/*     */   implements XmlVisitor
/*     */ {
/*     */   private final XmlVisitor next;
/*  52 */   private final AttributesImpl attributes = new AttributesImpl();
/*     */   
/*     */   public InterningXmlVisitor(XmlVisitor next) {
/*  55 */     this.next = next;
/*     */   }
/*     */   
/*     */   public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException {
/*  59 */     this.next.startDocument(locator, nsContext);
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/*  63 */     this.next.endDocument();
/*     */   }
/*     */   
/*     */   public void startElement(TagName tagName) throws SAXException {
/*  67 */     this.attributes.setAttributes(tagName.atts);
/*  68 */     tagName.atts = this.attributes;
/*  69 */     tagName.uri = intern(tagName.uri);
/*  70 */     tagName.local = intern(tagName.local);
/*  71 */     this.next.startElement(tagName);
/*     */   }
/*     */   
/*     */   public void endElement(TagName tagName) throws SAXException {
/*  75 */     tagName.uri = intern(tagName.uri);
/*  76 */     tagName.local = intern(tagName.local);
/*  77 */     this.next.endElement(tagName);
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
/*  81 */     this.next.startPrefixMapping(intern(prefix), intern(nsUri));
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/*  85 */     this.next.endPrefixMapping(intern(prefix));
/*     */   }
/*     */   
/*     */   public void text(CharSequence pcdata) throws SAXException {
/*  89 */     this.next.text(pcdata);
/*     */   }
/*     */   
/*     */   public UnmarshallingContext getContext() {
/*  93 */     return this.next.getContext();
/*     */   }
/*     */   
/*     */   public XmlVisitor.TextPredictor getPredictor() {
/*  97 */     return this.next.getPredictor();
/*     */   }
/*     */   
/*     */   private static class AttributesImpl implements Attributes {
/*     */     private Attributes core;
/*     */     
/*     */     void setAttributes(Attributes att) {
/* 104 */       this.core = att;
/*     */     }
/*     */     private AttributesImpl() {}
/*     */     public int getIndex(String qName) {
/* 108 */       return this.core.getIndex(qName);
/*     */     }
/*     */     
/*     */     public int getIndex(String uri, String localName) {
/* 112 */       return this.core.getIndex(uri, localName);
/*     */     }
/*     */     
/*     */     public int getLength() {
/* 116 */       return this.core.getLength();
/*     */     }
/*     */     
/*     */     public String getLocalName(int index) {
/* 120 */       return InterningXmlVisitor.intern(this.core.getLocalName(index));
/*     */     }
/*     */     
/*     */     public String getQName(int index) {
/* 124 */       return InterningXmlVisitor.intern(this.core.getQName(index));
/*     */     }
/*     */     
/*     */     public String getType(int index) {
/* 128 */       return InterningXmlVisitor.intern(this.core.getType(index));
/*     */     }
/*     */     
/*     */     public String getType(String qName) {
/* 132 */       return InterningXmlVisitor.intern(this.core.getType(qName));
/*     */     }
/*     */     
/*     */     public String getType(String uri, String localName) {
/* 136 */       return InterningXmlVisitor.intern(this.core.getType(uri, localName));
/*     */     }
/*     */     
/*     */     public String getURI(int index) {
/* 140 */       return InterningXmlVisitor.intern(this.core.getURI(index));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue(int index) {
/* 149 */       return this.core.getValue(index);
/*     */     }
/*     */     
/*     */     public String getValue(String qName) {
/* 153 */       return this.core.getValue(qName);
/*     */     }
/*     */     
/*     */     public String getValue(String uri, String localName) {
/* 157 */       return this.core.getValue(uri, localName);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String intern(String s) {
/* 162 */     if (s == null) return null; 
/* 163 */     return s.intern();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\InterningXmlVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */