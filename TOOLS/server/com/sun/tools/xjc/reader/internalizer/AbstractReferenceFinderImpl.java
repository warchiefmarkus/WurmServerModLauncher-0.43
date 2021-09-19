/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.SAXParseException2;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public abstract class AbstractReferenceFinderImpl
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   protected final DOMForest parent;
/*     */   private Locator locator;
/*     */   
/*     */   protected AbstractReferenceFinderImpl(DOMForest _parent) {
/*  66 */     this.parent = _parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String findExternalResource(String paramString1, String paramString2, Attributes paramAttributes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  84 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  86 */     String relativeRef = findExternalResource(namespaceURI, localName, atts);
/*  87 */     if (relativeRef == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  91 */       String ref = (new URI(this.locator.getSystemId())).resolve(new URI(relativeRef)).toString();
/*     */ 
/*     */ 
/*     */       
/*  95 */       this.parent.parse(ref, false);
/*  96 */     } catch (URISyntaxException e) {
/*  97 */       String msg = e.getMessage();
/*  98 */       if ((new File(relativeRef)).exists()) {
/*  99 */         msg = Messages.format("ERR_FILENAME_IS_NOT_URI", new Object[0]) + ' ' + msg;
/*     */       }
/*     */       
/* 102 */       SAXParseException2 sAXParseException2 = new SAXParseException2(Messages.format("AbstractReferenceFinderImpl.UnableToParse", new Object[] { relativeRef, msg }), this.locator, e);
/*     */ 
/*     */ 
/*     */       
/* 106 */       fatalError((SAXParseException)sAXParseException2);
/* 107 */       throw sAXParseException2;
/* 108 */     } catch (IOException e) {
/* 109 */       SAXParseException2 sAXParseException2 = new SAXParseException2(Messages.format("AbstractReferenceFinderImpl.UnableToParse", new Object[] { relativeRef, e.getMessage() }), this.locator, e);
/*     */ 
/*     */ 
/*     */       
/* 113 */       fatalError((SAXParseException)sAXParseException2);
/* 114 */       throw sAXParseException2;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 121 */     super.setDocumentLocator(locator);
/* 122 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\AbstractReferenceFinderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */