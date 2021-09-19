/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.v2.util.FatalAdapter;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ final class ValidatingUnmarshaller
/*     */   implements XmlVisitor, XmlVisitor.TextPredictor
/*     */ {
/*     */   private final XmlVisitor next;
/*     */   private final ValidatorHandler validator;
/*     */   private final XmlVisitor.TextPredictor predictor;
/*  61 */   private char[] buf = new char[256];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidatingUnmarshaller(Schema schema, XmlVisitor next) {
/*  67 */     this.validator = schema.newValidatorHandler();
/*  68 */     this.next = next;
/*  69 */     this.predictor = next.getPredictor();
/*     */ 
/*     */     
/*  72 */     this.validator.setErrorHandler((ErrorHandler)new FatalAdapter(getContext()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException {
/*  78 */     this.validator.setDocumentLocator(locator);
/*  79 */     this.validator.startDocument();
/*  80 */     this.next.startDocument(locator, nsContext);
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/*  84 */     this.validator.endDocument();
/*  85 */     this.next.endDocument();
/*     */   }
/*     */   
/*     */   public void startElement(TagName tagName) throws SAXException {
/*  89 */     this.validator.startElement(tagName.uri, tagName.local, tagName.getQname(), tagName.atts);
/*  90 */     this.next.startElement(tagName);
/*     */   }
/*     */   
/*     */   public void endElement(TagName tagName) throws SAXException {
/*  94 */     this.validator.endElement(tagName.uri, tagName.local, tagName.getQname());
/*  95 */     this.next.endElement(tagName);
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
/*  99 */     this.validator.startPrefixMapping(prefix, nsUri);
/* 100 */     this.next.startPrefixMapping(prefix, nsUri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 104 */     this.validator.endPrefixMapping(prefix);
/* 105 */     this.next.endPrefixMapping(prefix);
/*     */   }
/*     */   
/*     */   public void text(CharSequence pcdata) throws SAXException {
/* 109 */     int len = pcdata.length();
/* 110 */     if (this.buf.length < len) {
/* 111 */       this.buf = new char[len];
/*     */     }
/* 113 */     for (int i = 0; i < len; i++) {
/* 114 */       this.buf[i] = pcdata.charAt(i);
/*     */     }
/* 116 */     this.validator.characters(this.buf, 0, len);
/* 117 */     if (this.predictor.expectText())
/* 118 */       this.next.text(pcdata); 
/*     */   }
/*     */   
/*     */   public UnmarshallingContext getContext() {
/* 122 */     return this.next.getContext();
/*     */   }
/*     */   
/*     */   public XmlVisitor.TextPredictor getPredictor() {
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean expectText() {
/* 135 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\ValidatingUnmarshaller.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */