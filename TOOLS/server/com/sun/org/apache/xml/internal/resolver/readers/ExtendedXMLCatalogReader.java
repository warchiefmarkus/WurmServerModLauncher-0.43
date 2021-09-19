/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.Resolver;
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedXMLCatalogReader
/*     */   extends OASISXMLCatalogReader
/*     */ {
/*     */   public static final String extendedNamespaceName = "http://nwalsh.com/xcatalog/1.0";
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  67 */     boolean inExtension = inExtensionNamespace();
/*     */     
/*  69 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  71 */     int entryType = -1;
/*  72 */     Vector<String> entryArgs = new Vector();
/*     */     
/*  74 */     if (namespaceURI != null && "http://nwalsh.com/xcatalog/1.0".equals(namespaceURI) && !inExtension) {
/*     */ 
/*     */ 
/*     */       
/*  78 */       if (atts.getValue("xml:base") != null) {
/*  79 */         String baseURI = atts.getValue("xml:base");
/*  80 */         entryType = Catalog.BASE;
/*  81 */         entryArgs.add(baseURI);
/*  82 */         this.baseURIStack.push(baseURI);
/*     */         
/*  84 */         this.debug.message(4, "xml:base", baseURI);
/*     */         
/*     */         try {
/*  87 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/*  88 */           this.catalog.addEntry(ce);
/*  89 */         } catch (CatalogException cex) {
/*  90 */           if (cex.getExceptionType() == 3) {
/*  91 */             this.debug.message(1, "Invalid catalog entry type", localName);
/*  92 */           } else if (cex.getExceptionType() == 2) {
/*  93 */             this.debug.message(1, "Invalid catalog entry (base)", localName);
/*     */           } 
/*     */         } 
/*     */         
/*  97 */         entryType = -1;
/*  98 */         entryArgs = new Vector<String>();
/*     */       } else {
/* 100 */         this.baseURIStack.push(this.baseURIStack.peek());
/*     */       } 
/*     */       
/* 103 */       if (localName.equals("uriSuffix")) {
/* 104 */         if (checkAttributes(atts, "suffix", "uri")) {
/* 105 */           entryType = Resolver.URISUFFIX;
/* 106 */           entryArgs.add(atts.getValue("suffix"));
/* 107 */           entryArgs.add(atts.getValue("uri"));
/*     */           
/* 109 */           this.debug.message(4, "uriSuffix", atts.getValue("suffix"), atts.getValue("uri"));
/*     */         }
/*     */       
/*     */       }
/* 113 */       else if (localName.equals("systemSuffix")) {
/* 114 */         if (checkAttributes(atts, "suffix", "uri")) {
/* 115 */           entryType = Resolver.SYSTEMSUFFIX;
/* 116 */           entryArgs.add(atts.getValue("suffix"));
/* 117 */           entryArgs.add(atts.getValue("uri"));
/*     */           
/* 119 */           this.debug.message(4, "systemSuffix", atts.getValue("suffix"), atts.getValue("uri"));
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 125 */         this.debug.message(1, "Invalid catalog entry type", localName);
/*     */       } 
/*     */       
/* 128 */       if (entryType >= 0) {
/*     */         try {
/* 130 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 131 */           this.catalog.addEntry(ce);
/* 132 */         } catch (CatalogException cex) {
/* 133 */           if (cex.getExceptionType() == 3) {
/* 134 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 135 */           } else if (cex.getExceptionType() == 2) {
/* 136 */             this.debug.message(1, "Invalid catalog entry", localName);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 149 */     super.endElement(namespaceURI, localName, qName);
/*     */ 
/*     */ 
/*     */     
/* 153 */     boolean inExtension = inExtensionNamespace();
/*     */     
/* 155 */     int entryType = -1;
/* 156 */     Vector<String> entryArgs = new Vector();
/*     */     
/* 158 */     if (namespaceURI != null && "http://nwalsh.com/xcatalog/1.0".equals(namespaceURI) && !inExtension) {
/*     */ 
/*     */ 
/*     */       
/* 162 */       String popURI = this.baseURIStack.pop();
/* 163 */       String baseURI = this.baseURIStack.peek();
/*     */       
/* 165 */       if (!baseURI.equals(popURI)) {
/* 166 */         entryType = Catalog.BASE;
/* 167 */         entryArgs.add(baseURI);
/*     */         
/* 169 */         this.debug.message(4, "(reset) xml:base", baseURI);
/*     */         
/*     */         try {
/* 172 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 173 */           this.catalog.addEntry(ce);
/* 174 */         } catch (CatalogException cex) {
/* 175 */           if (cex.getExceptionType() == 3) {
/* 176 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 177 */           } else if (cex.getExceptionType() == 2) {
/* 178 */             this.debug.message(1, "Invalid catalog entry (rbase)", localName);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\ExtendedXMLCatalogReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */