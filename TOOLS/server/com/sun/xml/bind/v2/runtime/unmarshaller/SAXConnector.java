/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SAXConnector
/*     */   implements UnmarshallerHandler
/*     */ {
/*     */   private LocatorEx loc;
/*  61 */   private final StringBuilder buffer = new StringBuilder();
/*     */   private final XmlVisitor next;
/*     */   private final UnmarshallingContext context;
/*     */   private final XmlVisitor.TextPredictor predictor;
/*     */   
/*     */   private static final class TagNameImpl
/*     */     extends TagName {
/*     */     String qname;
/*     */     
/*     */     public String getQname() {
/*  71 */       return this.qname;
/*     */     }
/*     */     private TagNameImpl() {} }
/*  74 */   private final TagNameImpl tagName = new TagNameImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SAXConnector(XmlVisitor next, LocatorEx externalLocator) {
/*  85 */     this.next = next;
/*  86 */     this.context = next.getContext();
/*  87 */     this.predictor = next.getPredictor();
/*  88 */     this.loc = externalLocator;
/*     */   }
/*     */   
/*     */   public Object getResult() throws JAXBException, IllegalStateException {
/*  92 */     return this.context.getResult();
/*     */   }
/*     */   
/*     */   public UnmarshallingContext getContext() {
/*  96 */     return this.context;
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 100 */     if (this.loc != null) {
/*     */       return;
/*     */     }
/* 103 */     this.loc = new LocatorExWrapper(locator);
/*     */   }
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 107 */     this.next.startDocument(this.loc, null);
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/* 111 */     this.next.endDocument();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 115 */     this.next.startPrefixMapping(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 119 */     this.next.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String uri, String local, String qname, Attributes atts) throws SAXException {
/* 124 */     if (uri == null || uri.length() == 0)
/* 125 */       uri = ""; 
/* 126 */     if (local == null || local.length() == 0)
/* 127 */       local = qname; 
/* 128 */     if (qname == null || qname.length() == 0) {
/* 129 */       qname = local;
/*     */     }
/* 131 */     processText(true);
/*     */     
/* 133 */     this.tagName.uri = uri;
/* 134 */     this.tagName.local = local;
/* 135 */     this.tagName.qname = qname;
/* 136 */     this.tagName.atts = atts;
/* 137 */     this.next.startElement(this.tagName);
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 141 */     processText(false);
/* 142 */     this.tagName.uri = uri;
/* 143 */     this.tagName.local = localName;
/* 144 */     this.tagName.qname = qName;
/* 145 */     this.next.endElement(this.tagName);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void characters(char[] buf, int start, int len) {
/* 150 */     if (this.predictor.expectText())
/* 151 */       this.buffer.append(buf, start, len); 
/*     */   }
/*     */   
/*     */   public final void ignorableWhitespace(char[] buf, int start, int len) {
/* 155 */     characters(buf, start, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String data) {}
/*     */ 
/*     */   
/*     */   public void skippedEntity(String name) {}
/*     */ 
/*     */   
/*     */   private void processText(boolean ignorable) throws SAXException {
/* 167 */     if (this.predictor.expectText() && (!ignorable || !WhiteSpaceProcessor.isWhiteSpace(this.buffer)))
/* 168 */       this.next.text(this.buffer); 
/* 169 */     this.buffer.setLength(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\SAXConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */