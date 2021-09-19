/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
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
/*     */ public class OASISXMLCatalogReader
/*     */   extends SAXCatalogReader
/*     */   implements SAXCatalogParser
/*     */ {
/*  48 */   protected Catalog catalog = null;
/*     */ 
/*     */   
/*     */   public static final String namespaceName = "urn:oasis:names:tc:entity:xmlns:xml:catalog";
/*     */ 
/*     */   
/*     */   public static final String tr9401NamespaceName = "urn:oasis:names:tc:entity:xmlns:tr9401:catalog";
/*     */   
/*  56 */   protected Stack baseURIStack = new Stack();
/*  57 */   protected Stack overrideStack = new Stack();
/*  58 */   protected Stack namespaceStack = new Stack();
/*     */ 
/*     */   
/*     */   public void setCatalog(Catalog catalog) {
/*  62 */     this.catalog = catalog;
/*  63 */     this.debug = (catalog.getCatalogManager()).debug;
/*     */   }
/*     */ 
/*     */   
/*     */   public Catalog getCatalog() {
/*  68 */     return this.catalog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean inExtensionNamespace() {
/*  78 */     boolean inExtension = false;
/*     */     
/*  80 */     Enumeration<String> elements = this.namespaceStack.elements();
/*  81 */     while (!inExtension && elements.hasMoreElements()) {
/*  82 */       String ns = elements.nextElement();
/*  83 */       if (ns == null) {
/*  84 */         inExtension = true; continue;
/*     */       } 
/*  86 */       inExtension = (!ns.equals("urn:oasis:names:tc:entity:xmlns:tr9401:catalog") && !ns.equals("urn:oasis:names:tc:entity:xmlns:xml:catalog"));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  91 */     return inExtension;
/*     */   }
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
/*     */   public void startDocument() throws SAXException {
/* 105 */     this.baseURIStack.push(this.catalog.getCurrentBase());
/* 106 */     this.overrideStack.push(this.catalog.getDefaultOverride());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 134 */     int entryType = -1;
/* 135 */     Vector<String> entryArgs = new Vector();
/*     */     
/* 137 */     this.namespaceStack.push(namespaceURI);
/*     */     
/* 139 */     boolean inExtension = inExtensionNamespace();
/*     */     
/* 141 */     if (namespaceURI != null && "urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(namespaceURI) && !inExtension) {
/*     */ 
/*     */ 
/*     */       
/* 145 */       if (atts.getValue("xml:base") != null) {
/* 146 */         String baseURI = atts.getValue("xml:base");
/* 147 */         entryType = Catalog.BASE;
/* 148 */         entryArgs.add(baseURI);
/* 149 */         this.baseURIStack.push(baseURI);
/*     */         
/* 151 */         this.debug.message(4, "xml:base", baseURI);
/*     */         
/*     */         try {
/* 154 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 155 */           this.catalog.addEntry(ce);
/* 156 */         } catch (CatalogException cex) {
/* 157 */           if (cex.getExceptionType() == 3) {
/* 158 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 159 */           } else if (cex.getExceptionType() == 2) {
/* 160 */             this.debug.message(1, "Invalid catalog entry (base)", localName);
/*     */           } 
/*     */         } 
/*     */         
/* 164 */         entryType = -1;
/* 165 */         entryArgs = new Vector<String>();
/*     */       } else {
/*     */         
/* 168 */         this.baseURIStack.push(this.baseURIStack.peek());
/*     */       } 
/*     */       
/* 171 */       if ((localName.equals("catalog") || localName.equals("group")) && atts.getValue("prefer") != null) {
/*     */         
/* 173 */         String override = atts.getValue("prefer");
/*     */         
/* 175 */         if (override.equals("public")) {
/* 176 */           override = "yes";
/* 177 */         } else if (override.equals("system")) {
/* 178 */           override = "no";
/*     */         } else {
/* 180 */           this.debug.message(1, "Invalid prefer: must be 'system' or 'public'", localName);
/*     */ 
/*     */           
/* 183 */           override = this.catalog.getDefaultOverride();
/*     */         } 
/*     */         
/* 186 */         entryType = Catalog.OVERRIDE;
/* 187 */         entryArgs.add(override);
/* 188 */         this.overrideStack.push(override);
/*     */         
/* 190 */         this.debug.message(4, "override", override);
/*     */         
/*     */         try {
/* 193 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 194 */           this.catalog.addEntry(ce);
/* 195 */         } catch (CatalogException cex) {
/* 196 */           if (cex.getExceptionType() == 3) {
/* 197 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 198 */           } else if (cex.getExceptionType() == 2) {
/* 199 */             this.debug.message(1, "Invalid catalog entry (override)", localName);
/*     */           } 
/*     */         } 
/*     */         
/* 203 */         entryType = -1;
/* 204 */         entryArgs = new Vector<String>();
/*     */       } else {
/*     */         
/* 207 */         this.overrideStack.push(this.overrideStack.peek());
/*     */       } 
/*     */       
/* 210 */       if (localName.equals("delegatePublic")) {
/* 211 */         if (checkAttributes(atts, "publicIdStartString", "catalog")) {
/* 212 */           entryType = Catalog.DELEGATE_PUBLIC;
/* 213 */           entryArgs.add(atts.getValue("publicIdStartString"));
/* 214 */           entryArgs.add(atts.getValue("catalog"));
/*     */           
/* 216 */           this.debug.message(4, "delegatePublic", PublicId.normalize(atts.getValue("publicIdStartString")), atts.getValue("catalog"));
/*     */         }
/*     */       
/*     */       }
/* 220 */       else if (localName.equals("delegateSystem")) {
/* 221 */         if (checkAttributes(atts, "systemIdStartString", "catalog")) {
/* 222 */           entryType = Catalog.DELEGATE_SYSTEM;
/* 223 */           entryArgs.add(atts.getValue("systemIdStartString"));
/* 224 */           entryArgs.add(atts.getValue("catalog"));
/*     */           
/* 226 */           this.debug.message(4, "delegateSystem", atts.getValue("systemIdStartString"), atts.getValue("catalog"));
/*     */         }
/*     */       
/*     */       }
/* 230 */       else if (localName.equals("delegateURI")) {
/* 231 */         if (checkAttributes(atts, "uriStartString", "catalog")) {
/* 232 */           entryType = Catalog.DELEGATE_URI;
/* 233 */           entryArgs.add(atts.getValue("uriStartString"));
/* 234 */           entryArgs.add(atts.getValue("catalog"));
/*     */           
/* 236 */           this.debug.message(4, "delegateURI", atts.getValue("uriStartString"), atts.getValue("catalog"));
/*     */         }
/*     */       
/*     */       }
/* 240 */       else if (localName.equals("rewriteSystem")) {
/* 241 */         if (checkAttributes(atts, "systemIdStartString", "rewritePrefix")) {
/* 242 */           entryType = Catalog.REWRITE_SYSTEM;
/* 243 */           entryArgs.add(atts.getValue("systemIdStartString"));
/* 244 */           entryArgs.add(atts.getValue("rewritePrefix"));
/*     */           
/* 246 */           this.debug.message(4, "rewriteSystem", atts.getValue("systemIdStartString"), atts.getValue("rewritePrefix"));
/*     */         }
/*     */       
/*     */       }
/* 250 */       else if (localName.equals("systemSuffix")) {
/* 251 */         if (checkAttributes(atts, "systemIdSuffix", "uri")) {
/* 252 */           entryType = Catalog.SYSTEM_SUFFIX;
/* 253 */           entryArgs.add(atts.getValue("systemIdSuffix"));
/* 254 */           entryArgs.add(atts.getValue("uri"));
/*     */           
/* 256 */           this.debug.message(4, "systemSuffix", atts.getValue("systemIdSuffix"), atts.getValue("uri"));
/*     */         }
/*     */       
/*     */       }
/* 260 */       else if (localName.equals("rewriteURI")) {
/* 261 */         if (checkAttributes(atts, "uriStartString", "rewritePrefix")) {
/* 262 */           entryType = Catalog.REWRITE_URI;
/* 263 */           entryArgs.add(atts.getValue("uriStartString"));
/* 264 */           entryArgs.add(atts.getValue("rewritePrefix"));
/*     */           
/* 266 */           this.debug.message(4, "rewriteURI", atts.getValue("uriStartString"), atts.getValue("rewritePrefix"));
/*     */         }
/*     */       
/*     */       }
/* 270 */       else if (localName.equals("uriSuffix")) {
/* 271 */         if (checkAttributes(atts, "uriSuffix", "uri")) {
/* 272 */           entryType = Catalog.URI_SUFFIX;
/* 273 */           entryArgs.add(atts.getValue("uriSuffix"));
/* 274 */           entryArgs.add(atts.getValue("uri"));
/*     */           
/* 276 */           this.debug.message(4, "uriSuffix", atts.getValue("uriSuffix"), atts.getValue("uri"));
/*     */         }
/*     */       
/*     */       }
/* 280 */       else if (localName.equals("nextCatalog")) {
/* 281 */         if (checkAttributes(atts, "catalog")) {
/* 282 */           entryType = Catalog.CATALOG;
/* 283 */           entryArgs.add(atts.getValue("catalog"));
/*     */           
/* 285 */           this.debug.message(4, "nextCatalog", atts.getValue("catalog"));
/*     */         } 
/* 287 */       } else if (localName.equals("public")) {
/* 288 */         if (checkAttributes(atts, "publicId", "uri")) {
/* 289 */           entryType = Catalog.PUBLIC;
/* 290 */           entryArgs.add(atts.getValue("publicId"));
/* 291 */           entryArgs.add(atts.getValue("uri"));
/*     */           
/* 293 */           this.debug.message(4, "public", PublicId.normalize(atts.getValue("publicId")), atts.getValue("uri"));
/*     */         }
/*     */       
/*     */       }
/* 297 */       else if (localName.equals("system")) {
/* 298 */         if (checkAttributes(atts, "systemId", "uri")) {
/* 299 */           entryType = Catalog.SYSTEM;
/* 300 */           entryArgs.add(atts.getValue("systemId"));
/* 301 */           entryArgs.add(atts.getValue("uri"));
/*     */           
/* 303 */           this.debug.message(4, "system", atts.getValue("systemId"), atts.getValue("uri"));
/*     */         }
/*     */       
/*     */       }
/* 307 */       else if (localName.equals("uri")) {
/* 308 */         if (checkAttributes(atts, "name", "uri")) {
/* 309 */           entryType = Catalog.URI;
/* 310 */           entryArgs.add(atts.getValue("name"));
/* 311 */           entryArgs.add(atts.getValue("uri"));
/*     */           
/* 313 */           this.debug.message(4, "uri", atts.getValue("name"), atts.getValue("uri"));
/*     */         }
/*     */       
/*     */       }
/* 317 */       else if (!localName.equals("catalog")) {
/*     */         
/* 319 */         if (!localName.equals("group"))
/*     */         {
/*     */ 
/*     */           
/* 323 */           this.debug.message(1, "Invalid catalog entry type", localName);
/*     */         }
/*     */       } 
/* 326 */       if (entryType >= 0) {
/*     */         try {
/* 328 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 329 */           this.catalog.addEntry(ce);
/* 330 */         } catch (CatalogException cex) {
/* 331 */           if (cex.getExceptionType() == 3) {
/* 332 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 333 */           } else if (cex.getExceptionType() == 2) {
/* 334 */             this.debug.message(1, "Invalid catalog entry", localName);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 340 */     if (namespaceURI != null && "urn:oasis:names:tc:entity:xmlns:tr9401:catalog".equals(namespaceURI) && !inExtension) {
/*     */ 
/*     */ 
/*     */       
/* 344 */       if (atts.getValue("xml:base") != null) {
/* 345 */         String baseURI = atts.getValue("xml:base");
/* 346 */         entryType = Catalog.BASE;
/* 347 */         entryArgs.add(baseURI);
/* 348 */         this.baseURIStack.push(baseURI);
/*     */         
/* 350 */         this.debug.message(4, "xml:base", baseURI);
/*     */         
/*     */         try {
/* 353 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 354 */           this.catalog.addEntry(ce);
/* 355 */         } catch (CatalogException cex) {
/* 356 */           if (cex.getExceptionType() == 3) {
/* 357 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 358 */           } else if (cex.getExceptionType() == 2) {
/* 359 */             this.debug.message(1, "Invalid catalog entry (base)", localName);
/*     */           } 
/*     */         } 
/*     */         
/* 363 */         entryType = -1;
/* 364 */         entryArgs = new Vector<String>();
/*     */       } else {
/*     */         
/* 367 */         this.baseURIStack.push(this.baseURIStack.peek());
/*     */       } 
/*     */       
/* 370 */       if (localName.equals("doctype")) {
/* 371 */         entryType = Catalog.DOCTYPE;
/* 372 */         entryArgs.add(atts.getValue("name"));
/* 373 */         entryArgs.add(atts.getValue("uri"));
/* 374 */       } else if (localName.equals("document")) {
/* 375 */         entryType = Catalog.DOCUMENT;
/* 376 */         entryArgs.add(atts.getValue("uri"));
/* 377 */       } else if (localName.equals("dtddecl")) {
/* 378 */         entryType = Catalog.DTDDECL;
/* 379 */         entryArgs.add(atts.getValue("publicId"));
/* 380 */         entryArgs.add(atts.getValue("uri"));
/* 381 */       } else if (localName.equals("entity")) {
/* 382 */         entryType = Catalog.ENTITY;
/* 383 */         entryArgs.add(atts.getValue("name"));
/* 384 */         entryArgs.add(atts.getValue("uri"));
/* 385 */       } else if (localName.equals("linktype")) {
/* 386 */         entryType = Catalog.LINKTYPE;
/* 387 */         entryArgs.add(atts.getValue("name"));
/* 388 */         entryArgs.add(atts.getValue("uri"));
/* 389 */       } else if (localName.equals("notation")) {
/* 390 */         entryType = Catalog.NOTATION;
/* 391 */         entryArgs.add(atts.getValue("name"));
/* 392 */         entryArgs.add(atts.getValue("uri"));
/* 393 */       } else if (localName.equals("sgmldecl")) {
/* 394 */         entryType = Catalog.SGMLDECL;
/* 395 */         entryArgs.add(atts.getValue("uri"));
/*     */       } else {
/*     */         
/* 398 */         this.debug.message(1, "Invalid catalog entry type", localName);
/*     */       } 
/*     */       
/* 401 */       if (entryType >= 0) {
/*     */         try {
/* 403 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 404 */           this.catalog.addEntry(ce);
/* 405 */         } catch (CatalogException cex) {
/* 406 */           if (cex.getExceptionType() == 3) {
/* 407 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 408 */           } else if (cex.getExceptionType() == 2) {
/* 409 */             this.debug.message(1, "Invalid catalog entry", localName);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean checkAttributes(Attributes atts, String attName) {
/* 417 */     if (atts.getValue(attName) == null) {
/* 418 */       this.debug.message(1, "Error: required attribute " + attName + " missing.");
/* 419 */       return false;
/*     */     } 
/* 421 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkAttributes(Attributes atts, String attName1, String attName2) {
/* 428 */     return (checkAttributes(atts, attName1) && checkAttributes(atts, attName2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 438 */     int entryType = -1;
/* 439 */     Vector<String> entryArgs = new Vector();
/*     */     
/* 441 */     boolean inExtension = inExtensionNamespace();
/*     */     
/* 443 */     if (namespaceURI != null && !inExtension && ("urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(namespaceURI) || "urn:oasis:names:tc:entity:xmlns:tr9401:catalog".equals(namespaceURI))) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 448 */       String popURI = this.baseURIStack.pop();
/* 449 */       String baseURI = this.baseURIStack.peek();
/*     */       
/* 451 */       if (!baseURI.equals(popURI)) {
/* 452 */         entryType = Catalog.BASE;
/* 453 */         entryArgs.add(baseURI);
/*     */         
/* 455 */         this.debug.message(4, "(reset) xml:base", baseURI);
/*     */         
/*     */         try {
/* 458 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 459 */           this.catalog.addEntry(ce);
/* 460 */         } catch (CatalogException cex) {
/* 461 */           if (cex.getExceptionType() == 3) {
/* 462 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 463 */           } else if (cex.getExceptionType() == 2) {
/* 464 */             this.debug.message(1, "Invalid catalog entry (rbase)", localName);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 470 */     if (namespaceURI != null && "urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(namespaceURI) && !inExtension)
/*     */     {
/* 472 */       if (localName.equals("catalog") || localName.equals("group")) {
/* 473 */         String popOverride = this.overrideStack.pop();
/* 474 */         String override = this.overrideStack.peek();
/*     */         
/* 476 */         if (!override.equals(popOverride)) {
/* 477 */           entryType = Catalog.OVERRIDE;
/* 478 */           entryArgs.add(override);
/* 479 */           this.overrideStack.push(override);
/*     */           
/* 481 */           this.debug.message(4, "(reset) override", override);
/*     */           
/*     */           try {
/* 484 */             CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 485 */             this.catalog.addEntry(ce);
/* 486 */           } catch (CatalogException cex) {
/* 487 */             if (cex.getExceptionType() == 3) {
/* 488 */               this.debug.message(1, "Invalid catalog entry type", localName);
/* 489 */             } else if (cex.getExceptionType() == 2) {
/* 490 */               this.debug.message(1, "Invalid catalog entry (roverride)", localName);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 497 */     this.namespaceStack.pop();
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {}
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {}
/*     */   
/*     */   public void skippedEntity(String name) throws SAXException {}
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {}
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\OASISXMLCatalogReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */