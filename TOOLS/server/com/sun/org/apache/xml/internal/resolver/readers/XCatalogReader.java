/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.SAXParserFactory;
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
/*     */ public class XCatalogReader
/*     */   extends SAXCatalogReader
/*     */   implements SAXCatalogParser
/*     */ {
/*  47 */   protected Catalog catalog = null;
/*     */ 
/*     */   
/*     */   public void setCatalog(Catalog catalog) {
/*  51 */     this.catalog = catalog;
/*     */   }
/*     */ 
/*     */   
/*     */   public Catalog getCatalog() {
/*  56 */     return this.catalog;
/*     */   }
/*     */ 
/*     */   
/*     */   public XCatalogReader(SAXParserFactory parserFactory) {
/*  61 */     super(parserFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endDocument() throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 102 */     int entryType = -1;
/* 103 */     Vector<String> entryArgs = new Vector();
/*     */     
/* 105 */     if (localName.equals("Base")) {
/* 106 */       entryType = Catalog.BASE;
/* 107 */       entryArgs.add(atts.getValue("HRef"));
/*     */       
/* 109 */       (this.catalog.getCatalogManager()).debug.message(4, "Base", atts.getValue("HRef"));
/* 110 */     } else if (localName.equals("Delegate")) {
/* 111 */       entryType = Catalog.DELEGATE_PUBLIC;
/* 112 */       entryArgs.add(atts.getValue("PublicId"));
/* 113 */       entryArgs.add(atts.getValue("HRef"));
/*     */       
/* 115 */       (this.catalog.getCatalogManager()).debug.message(4, "Delegate", PublicId.normalize(atts.getValue("PublicId")), atts.getValue("HRef"));
/*     */     
/*     */     }
/* 118 */     else if (localName.equals("Extend")) {
/* 119 */       entryType = Catalog.CATALOG;
/* 120 */       entryArgs.add(atts.getValue("HRef"));
/*     */       
/* 122 */       (this.catalog.getCatalogManager()).debug.message(4, "Extend", atts.getValue("HRef"));
/* 123 */     } else if (localName.equals("Map")) {
/* 124 */       entryType = Catalog.PUBLIC;
/* 125 */       entryArgs.add(atts.getValue("PublicId"));
/* 126 */       entryArgs.add(atts.getValue("HRef"));
/*     */       
/* 128 */       (this.catalog.getCatalogManager()).debug.message(4, "Map", PublicId.normalize(atts.getValue("PublicId")), atts.getValue("HRef"));
/*     */     
/*     */     }
/* 131 */     else if (localName.equals("Remap")) {
/* 132 */       entryType = Catalog.SYSTEM;
/* 133 */       entryArgs.add(atts.getValue("SystemId"));
/* 134 */       entryArgs.add(atts.getValue("HRef"));
/*     */       
/* 136 */       (this.catalog.getCatalogManager()).debug.message(4, "Remap", atts.getValue("SystemId"), atts.getValue("HRef"));
/*     */     
/*     */     }
/* 139 */     else if (!localName.equals("XMLCatalog")) {
/*     */ 
/*     */ 
/*     */       
/* 143 */       (this.catalog.getCatalogManager()).debug.message(1, "Invalid catalog entry type", localName);
/*     */     } 
/*     */     
/* 146 */     if (entryType >= 0)
/*     */       try {
/* 148 */         CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 149 */         this.catalog.addEntry(ce);
/* 150 */       } catch (CatalogException cex) {
/* 151 */         if (cex.getExceptionType() == 3) {
/* 152 */           (this.catalog.getCatalogManager()).debug.message(1, "Invalid catalog entry type", localName);
/* 153 */         } else if (cex.getExceptionType() == 2) {
/* 154 */           (this.catalog.getCatalogManager()).debug.message(1, "Invalid catalog entry", localName);
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {}
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {}
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\XCatalogReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */