/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
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
/*     */ abstract class StAXConnector
/*     */ {
/*     */   protected final XmlVisitor visitor;
/*     */   protected final UnmarshallingContext context;
/*     */   protected final XmlVisitor.TextPredictor predictor;
/*     */   
/*     */   private final class TagNameImpl
/*     */     extends TagName
/*     */   {
/*     */     private TagNameImpl() {}
/*     */     
/*     */     public String getQname() {
/*  62 */       return StAXConnector.this.getCurrentQName();
/*     */     }
/*     */   }
/*     */   
/*  66 */   protected final TagName tagName = new TagNameImpl();
/*     */   
/*     */   protected StAXConnector(XmlVisitor visitor) {
/*  69 */     this.visitor = visitor;
/*  70 */     this.context = visitor.getContext();
/*  71 */     this.predictor = visitor.getPredictor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void bridge() throws XMLStreamException;
/*     */ 
/*     */   
/*     */   protected abstract Location getCurrentLocation();
/*     */ 
/*     */   
/*     */   protected abstract String getCurrentQName();
/*     */ 
/*     */   
/*     */   protected final void handleStartDocument(NamespaceContext nsc) throws SAXException {
/*  86 */     this.visitor.startDocument(new LocatorEx() {
/*     */           public ValidationEventLocator getLocation() {
/*  88 */             return (ValidationEventLocator)new ValidationEventLocatorImpl(this);
/*     */           }
/*     */           public int getColumnNumber() {
/*  91 */             return StAXConnector.this.getCurrentLocation().getColumnNumber();
/*     */           }
/*     */           public int getLineNumber() {
/*  94 */             return StAXConnector.this.getCurrentLocation().getLineNumber();
/*     */           }
/*     */           public String getPublicId() {
/*  97 */             return StAXConnector.this.getCurrentLocation().getPublicId();
/*     */           }
/*     */           public String getSystemId() {
/* 100 */             return StAXConnector.this.getCurrentLocation().getSystemId();
/*     */           }
/*     */         },  nsc);
/*     */   }
/*     */   
/*     */   protected final void handleEndDocument() throws SAXException {
/* 106 */     this.visitor.endDocument();
/*     */   }
/*     */   
/*     */   protected static String fixNull(String s) {
/* 110 */     if (s == null) return ""; 
/* 111 */     return s;
/*     */   }
/*     */   
/*     */   protected final String getQName(String prefix, String localName) {
/* 115 */     if (prefix == null || prefix.length() == 0) {
/* 116 */       return localName;
/*     */     }
/* 118 */     return prefix + ':' + localName;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\StAXConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */