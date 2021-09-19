/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.LocatorImpl;
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
/*     */ public class VersionChecker
/*     */   extends XMLFilterImpl
/*     */ {
/*  70 */   private String version = null;
/*     */ 
/*     */   
/*     */   private boolean seenRoot = false;
/*     */ 
/*     */   
/*     */   private boolean seenBindings = false;
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */   
/*     */   private Locator rootTagStart;
/*     */ 
/*     */   
/*     */   public VersionChecker(XMLReader parent) {
/*  86 */     setParent(parent);
/*     */   }
/*     */   
/*     */   public VersionChecker(ContentHandler handler, ErrorHandler eh, EntityResolver er) {
/*  90 */     setContentHandler(handler);
/*  91 */     if (eh != null) setErrorHandler(eh); 
/*  92 */     if (er != null) setEntityResolver(er);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  98 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/* 100 */     if (!this.seenRoot) {
/*     */       
/* 102 */       this.seenRoot = true;
/* 103 */       this.rootTagStart = new LocatorImpl(this.locator);
/*     */       
/* 105 */       this.version = atts.getValue("http://java.sun.com/xml/ns/jaxb", "version");
/* 106 */       if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxb")) {
/* 107 */         String version2 = atts.getValue("", "version");
/* 108 */         if (this.version != null && version2 != null) {
/*     */           
/* 110 */           SAXParseException e = new SAXParseException(Messages.format("Internalizer.TwoVersionAttributes", new Object[0]), this.locator);
/*     */           
/* 112 */           getErrorHandler().error(e);
/*     */         } 
/* 114 */         if (this.version == null) {
/* 115 */           this.version = version2;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     if ("http://java.sun.com/xml/ns/jaxb".equals(namespaceURI))
/* 121 */       this.seenBindings = true; 
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/* 125 */     super.endDocument();
/*     */     
/* 127 */     if (this.seenBindings && this.version == null) {
/*     */       
/* 129 */       SAXParseException e = new SAXParseException(Messages.format("Internalizer.VersionNotPresent", new Object[0]), this.rootTagStart);
/*     */       
/* 131 */       getErrorHandler().error(e);
/*     */     } 
/*     */ 
/*     */     
/* 135 */     if (this.version != null && !VERSIONS.contains(this.version)) {
/* 136 */       SAXParseException e = new SAXParseException(Messages.format("Internalizer.IncorrectVersion", new Object[0]), this.rootTagStart);
/*     */       
/* 138 */       getErrorHandler().error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 143 */     super.setDocumentLocator(locator);
/* 144 */     this.locator = locator;
/*     */   }
/*     */   
/* 147 */   private static final Set<String> VERSIONS = new HashSet<String>(Arrays.asList(new String[] { "1.0", "2.0", "2.1" }));
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\VersionChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */