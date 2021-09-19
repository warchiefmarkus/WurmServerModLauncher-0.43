/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForkingFilter
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   private ContentHandler side;
/*     */   private int depth;
/*  75 */   private final ArrayList<String> namespaces = new ArrayList<String>();
/*     */   
/*     */   private Locator loc;
/*     */ 
/*     */   
/*     */   public ForkingFilter() {}
/*     */   
/*     */   public ForkingFilter(ContentHandler next) {
/*  83 */     setContentHandler(next);
/*     */   }
/*     */   
/*     */   public ContentHandler getSideHandler() {
/*  87 */     return this.side;
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/*  91 */     super.setDocumentLocator(locator);
/*  92 */     this.loc = locator;
/*     */   }
/*     */   
/*     */   public Locator getDocumentLocator() {
/*  96 */     return this.loc;
/*     */   }
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 100 */     reset();
/* 101 */     super.startDocument();
/*     */   }
/*     */   
/*     */   private void reset() {
/* 105 */     this.namespaces.clear();
/* 106 */     this.side = null;
/* 107 */     this.depth = 0;
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/* 111 */     this.loc = null;
/* 112 */     reset();
/* 113 */     super.endDocument();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 117 */     if (this.side != null)
/* 118 */       this.side.startPrefixMapping(prefix, uri); 
/* 119 */     this.namespaces.add(prefix);
/* 120 */     this.namespaces.add(uri);
/* 121 */     super.startPrefixMapping(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 125 */     if (this.side != null)
/* 126 */       this.side.endPrefixMapping(prefix); 
/* 127 */     super.endPrefixMapping(prefix);
/* 128 */     this.namespaces.remove(this.namespaces.size() - 1);
/* 129 */     this.namespaces.remove(this.namespaces.size() - 1);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 133 */     if (this.side != null) {
/* 134 */       this.side.startElement(uri, localName, qName, atts);
/* 135 */       this.depth++;
/*     */     } 
/* 137 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startForking(String uri, String localName, String qName, Attributes atts, ContentHandler side) throws SAXException {
/* 144 */     if (this.side != null) throw new IllegalStateException();
/*     */     
/* 146 */     this.side = side;
/* 147 */     this.depth = 1;
/* 148 */     side.setDocumentLocator(this.loc);
/* 149 */     side.startDocument();
/* 150 */     for (int i = 0; i < this.namespaces.size(); i += 2)
/* 151 */       side.startPrefixMapping(this.namespaces.get(i), this.namespaces.get(i + 1)); 
/* 152 */     side.startElement(uri, localName, qName, atts);
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 156 */     if (this.side != null) {
/* 157 */       this.side.endElement(uri, localName, qName);
/* 158 */       this.depth--;
/* 159 */       if (this.depth == 0) {
/* 160 */         for (int i = this.namespaces.size() - 2; i >= 0; i -= 2)
/* 161 */           this.side.endPrefixMapping(this.namespaces.get(i)); 
/* 162 */         this.side.endDocument();
/* 163 */         this.side = null;
/*     */       } 
/*     */     } 
/* 166 */     super.endElement(uri, localName, qName);
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 170 */     if (this.side != null)
/* 171 */       this.side.characters(ch, start, length); 
/* 172 */     super.characters(ch, start, length);
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 176 */     if (this.side != null)
/* 177 */       this.side.ignorableWhitespace(ch, start, length); 
/* 178 */     super.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\ForkingFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */