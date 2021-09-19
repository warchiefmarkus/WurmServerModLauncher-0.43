/*     */ package com.sun.org.apache.xml.internal.resolver;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.readers.CatalogReader;
/*     */ import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
/*     */ import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Resolver
/*     */   extends Catalog
/*     */ {
/*  52 */   public static final int URISUFFIX = CatalogEntry.addEntryType("URISUFFIX", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final int SYSTEMSUFFIX = CatalogEntry.addEntryType("SYSTEMSUFFIX", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final int RESOLVER = CatalogEntry.addEntryType("RESOLVER", 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static final int SYSTEMREVERSE = CatalogEntry.addEntryType("SYSTEMREVERSE", 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setupReaders() {
/*  84 */     SAXParserFactory spf = SAXParserFactory.newInstance();
/*  85 */     spf.setNamespaceAware(true);
/*  86 */     spf.setValidating(false);
/*     */     
/*  88 */     SAXCatalogReader saxReader = new SAXCatalogReader(spf);
/*     */     
/*  90 */     saxReader.setCatalogParser(null, "XMLCatalog", "com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader");
/*     */ 
/*     */     
/*  93 */     saxReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader");
/*     */ 
/*     */ 
/*     */     
/*  97 */     addReader("application/xml", (CatalogReader)saxReader);
/*     */     
/*  99 */     TR9401CatalogReader textReader = new TR9401CatalogReader();
/* 100 */     addReader("text/plain", (CatalogReader)textReader);
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
/*     */   public void addEntry(CatalogEntry entry) {
/* 114 */     int type = entry.getEntryType();
/*     */     
/* 116 */     if (type == URISUFFIX) {
/* 117 */       String suffix = normalizeURI(entry.getEntryArg(0));
/* 118 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*     */       
/* 120 */       entry.setEntryArg(1, fsi);
/*     */       
/* 122 */       this.catalogManager.debug.message(4, "URISUFFIX", suffix, fsi);
/* 123 */     } else if (type == SYSTEMSUFFIX) {
/* 124 */       String suffix = normalizeURI(entry.getEntryArg(0));
/* 125 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*     */       
/* 127 */       entry.setEntryArg(1, fsi);
/*     */       
/* 129 */       this.catalogManager.debug.message(4, "SYSTEMSUFFIX", suffix, fsi);
/*     */     } 
/*     */     
/* 132 */     super.addEntry(entry);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolveURI(String uri) throws MalformedURLException, IOException {
/* 158 */     String resolved = super.resolveURI(uri);
/* 159 */     if (resolved != null) {
/* 160 */       return resolved;
/*     */     }
/*     */     
/* 163 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 164 */     while (en.hasMoreElements()) {
/* 165 */       CatalogEntry e = en.nextElement();
/* 166 */       if (e.getEntryType() == RESOLVER) {
/* 167 */         resolved = resolveExternalSystem(uri, e.getEntryArg(0));
/* 168 */         if (resolved != null)
/* 169 */           return resolved;  continue;
/*     */       } 
/* 171 */       if (e.getEntryType() == URISUFFIX) {
/* 172 */         String suffix = e.getEntryArg(0);
/* 173 */         String result = e.getEntryArg(1);
/*     */         
/* 175 */         if (suffix.length() <= uri.length() && uri.substring(uri.length() - suffix.length()).equals(suffix))
/*     */         {
/* 177 */           return result;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 183 */     return resolveSubordinateCatalogs(Catalog.URI, null, null, uri);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolveSystem(String systemId) throws MalformedURLException, IOException {
/* 215 */     String resolved = super.resolveSystem(systemId);
/* 216 */     if (resolved != null) {
/* 217 */       return resolved;
/*     */     }
/*     */     
/* 220 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 221 */     while (en.hasMoreElements()) {
/* 222 */       CatalogEntry e = en.nextElement();
/* 223 */       if (e.getEntryType() == RESOLVER) {
/* 224 */         resolved = resolveExternalSystem(systemId, e.getEntryArg(0));
/* 225 */         if (resolved != null)
/* 226 */           return resolved;  continue;
/*     */       } 
/* 228 */       if (e.getEntryType() == SYSTEMSUFFIX) {
/* 229 */         String suffix = e.getEntryArg(0);
/* 230 */         String result = e.getEntryArg(1);
/*     */         
/* 232 */         if (suffix.length() <= systemId.length() && systemId.substring(systemId.length() - suffix.length()).equals(suffix))
/*     */         {
/* 234 */           return result;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 239 */     return resolveSubordinateCatalogs(Catalog.SYSTEM, null, null, systemId);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolvePublic(String publicId, String systemId) throws MalformedURLException, IOException {
/* 277 */     String resolved = super.resolvePublic(publicId, systemId);
/* 278 */     if (resolved != null) {
/* 279 */       return resolved;
/*     */     }
/*     */     
/* 282 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 283 */     while (en.hasMoreElements()) {
/* 284 */       CatalogEntry e = en.nextElement();
/* 285 */       if (e.getEntryType() == RESOLVER) {
/* 286 */         if (systemId != null) {
/* 287 */           resolved = resolveExternalSystem(systemId, e.getEntryArg(0));
/*     */           
/* 289 */           if (resolved != null) {
/* 290 */             return resolved;
/*     */           }
/*     */         } 
/* 293 */         resolved = resolveExternalPublic(publicId, e.getEntryArg(0));
/* 294 */         if (resolved != null) {
/* 295 */           return resolved;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     return resolveSubordinateCatalogs(Catalog.PUBLIC, null, publicId, systemId);
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
/*     */   
/*     */   protected String resolveExternalSystem(String systemId, String resolver) throws MalformedURLException, IOException {
/* 316 */     Resolver r = queryResolver(resolver, "i2l", systemId, (String)null);
/* 317 */     if (r != null) {
/* 318 */       return r.resolveSystem(systemId);
/*     */     }
/* 320 */     return null;
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
/*     */   protected String resolveExternalPublic(String publicId, String resolver) throws MalformedURLException, IOException {
/* 334 */     Resolver r = queryResolver(resolver, "fpi2l", publicId, (String)null);
/* 335 */     if (r != null) {
/* 336 */       return r.resolvePublic(publicId, (String)null);
/*     */     }
/* 338 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resolver queryResolver(String resolver, String command, String arg1, String arg2) {
/* 356 */     InputStream iStream = null;
/* 357 */     String RFC2483 = resolver + "?command=" + command + "&format=tr9401&uri=" + arg1 + "&uri2=" + arg2;
/*     */ 
/*     */     
/* 360 */     String line = null;
/*     */     
/*     */     try {
/* 363 */       URL url = new URL(RFC2483);
/*     */       
/* 365 */       URLConnection urlCon = url.openConnection();
/*     */       
/* 367 */       urlCon.setUseCaches(false);
/*     */       
/* 369 */       Resolver r = (Resolver)newCatalog();
/*     */       
/* 371 */       String cType = urlCon.getContentType();
/*     */ 
/*     */       
/* 374 */       if (cType.indexOf(";") > 0) {
/* 375 */         cType = cType.substring(0, cType.indexOf(";"));
/*     */       }
/*     */       
/* 378 */       r.parseCatalog(cType, urlCon.getInputStream());
/*     */       
/* 380 */       return r;
/* 381 */     } catch (CatalogException cex) {
/* 382 */       if (cex.getExceptionType() == 6) {
/* 383 */         this.catalogManager.debug.message(1, "Unparseable catalog: " + RFC2483);
/* 384 */       } else if (cex.getExceptionType() == 5) {
/*     */         
/* 386 */         this.catalogManager.debug.message(1, "Unknown catalog format: " + RFC2483);
/*     */       } 
/* 388 */       return null;
/* 389 */     } catch (MalformedURLException mue) {
/* 390 */       this.catalogManager.debug.message(1, "Malformed resolver URL: " + RFC2483);
/* 391 */       return null;
/* 392 */     } catch (IOException ie) {
/* 393 */       this.catalogManager.debug.message(1, "I/O Exception opening resolver: " + RFC2483);
/* 394 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Vector appendVector(Vector vec, Vector appvec) {
/* 406 */     if (appvec != null) {
/* 407 */       for (int count = 0; count < appvec.size(); count++) {
/* 408 */         vec.addElement(appvec.elementAt(count));
/*     */       }
/*     */     }
/* 411 */     return vec;
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
/*     */   public Vector resolveAllSystemReverse(String systemId) throws MalformedURLException, IOException {
/* 423 */     Vector resolved = new Vector();
/*     */ 
/*     */     
/* 426 */     if (systemId != null) {
/* 427 */       Vector localResolved = resolveLocalSystemReverse(systemId);
/* 428 */       resolved = appendVector(resolved, localResolved);
/*     */     } 
/*     */ 
/*     */     
/* 432 */     Vector subResolved = resolveAllSubordinateCatalogs(SYSTEMREVERSE, (String)null, (String)null, systemId);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 437 */     return appendVector(resolved, subResolved);
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
/*     */   public String resolveSystemReverse(String systemId) throws MalformedURLException, IOException {
/* 449 */     Vector<String> resolved = resolveAllSystemReverse(systemId);
/* 450 */     if (resolved != null && resolved.size() > 0) {
/* 451 */       return resolved.elementAt(0);
/*     */     }
/* 453 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector resolveAllSystem(String systemId) throws MalformedURLException, IOException {
/* 486 */     Vector resolutions = new Vector();
/*     */ 
/*     */     
/* 489 */     if (systemId != null) {
/* 490 */       Vector localResolutions = resolveAllLocalSystem(systemId);
/* 491 */       resolutions = appendVector(resolutions, localResolutions);
/*     */     } 
/*     */ 
/*     */     
/* 495 */     Vector subResolutions = resolveAllSubordinateCatalogs(SYSTEM, (String)null, (String)null, systemId);
/*     */ 
/*     */ 
/*     */     
/* 499 */     resolutions = appendVector(resolutions, subResolutions);
/*     */     
/* 501 */     if (resolutions.size() > 0) {
/* 502 */       return resolutions;
/*     */     }
/* 504 */     return null;
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
/*     */   
/*     */   private Vector resolveAllLocalSystem(String systemId) {
/* 520 */     Vector<String> map = new Vector();
/* 521 */     String osname = System.getProperty("os.name");
/* 522 */     boolean windows = (osname.indexOf("Windows") >= 0);
/* 523 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 524 */     while (en.hasMoreElements()) {
/* 525 */       CatalogEntry e = en.nextElement();
/* 526 */       if (e.getEntryType() == SYSTEM && (e.getEntryArg(0).equals(systemId) || (windows && e.getEntryArg(0).equalsIgnoreCase(systemId))))
/*     */       {
/*     */ 
/*     */         
/* 530 */         map.addElement(e.getEntryArg(1));
/*     */       }
/*     */     } 
/* 533 */     if (map.size() == 0) {
/* 534 */       return null;
/*     */     }
/* 536 */     return map;
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
/*     */   private Vector resolveLocalSystemReverse(String systemId) {
/* 548 */     Vector<String> map = new Vector();
/* 549 */     String osname = System.getProperty("os.name");
/* 550 */     boolean windows = (osname.indexOf("Windows") >= 0);
/* 551 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 552 */     while (en.hasMoreElements()) {
/* 553 */       CatalogEntry e = en.nextElement();
/* 554 */       if (e.getEntryType() == SYSTEM && (e.getEntryArg(1).equals(systemId) || (windows && e.getEntryArg(1).equalsIgnoreCase(systemId))))
/*     */       {
/*     */ 
/*     */         
/* 558 */         map.addElement(e.getEntryArg(0));
/*     */       }
/*     */     } 
/* 561 */     if (map.size() == 0) {
/* 562 */       return null;
/*     */     }
/* 564 */     return map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Vector resolveAllSubordinateCatalogs(int entityType, String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
/* 602 */     Vector<String> resolutions = new Vector();
/*     */     
/* 604 */     for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
/* 605 */       Resolver c = null;
/*     */       
/*     */       try {
/* 608 */         c = this.catalogs.elementAt(catPos);
/* 609 */       } catch (ClassCastException e) {
/* 610 */         String catfile = this.catalogs.elementAt(catPos);
/* 611 */         c = (Resolver)newCatalog();
/*     */         
/*     */         try {
/* 614 */           c.parseCatalog(catfile);
/* 615 */         } catch (MalformedURLException mue) {
/* 616 */           this.catalogManager.debug.message(1, "Malformed Catalog URL", catfile);
/* 617 */         } catch (FileNotFoundException fnfe) {
/* 618 */           this.catalogManager.debug.message(1, "Failed to load catalog, file not found", catfile);
/*     */         }
/* 620 */         catch (IOException ioe) {
/* 621 */           this.catalogManager.debug.message(1, "Failed to load catalog, I/O error", catfile);
/*     */         } 
/*     */         
/* 624 */         this.catalogs.setElementAt(c, catPos);
/*     */       } 
/*     */       
/* 627 */       String resolved = null;
/*     */ 
/*     */       
/* 630 */       if (entityType == DOCTYPE)
/* 631 */       { resolved = c.resolveDoctype(entityName, publicId, systemId);
/*     */ 
/*     */         
/* 634 */         if (resolved != null) {
/*     */           
/* 636 */           resolutions.addElement(resolved);
/* 637 */           return resolutions;
/*     */         }  }
/* 639 */       else if (entityType == DOCUMENT)
/* 640 */       { resolved = c.resolveDocument();
/* 641 */         if (resolved != null) {
/*     */           
/* 643 */           resolutions.addElement(resolved);
/* 644 */           return resolutions;
/*     */         }  }
/* 646 */       else if (entityType == ENTITY)
/* 647 */       { resolved = c.resolveEntity(entityName, publicId, systemId);
/*     */ 
/*     */         
/* 650 */         if (resolved != null) {
/*     */           
/* 652 */           resolutions.addElement(resolved);
/* 653 */           return resolutions;
/*     */         }  }
/* 655 */       else if (entityType == NOTATION)
/* 656 */       { resolved = c.resolveNotation(entityName, publicId, systemId);
/*     */ 
/*     */         
/* 659 */         if (resolved != null) {
/*     */           
/* 661 */           resolutions.addElement(resolved);
/* 662 */           return resolutions;
/*     */         }  }
/* 664 */       else if (entityType == PUBLIC)
/* 665 */       { resolved = c.resolvePublic(publicId, systemId);
/* 666 */         if (resolved != null) {
/*     */           
/* 668 */           resolutions.addElement(resolved);
/* 669 */           return resolutions;
/*     */         }  }
/* 671 */       else { if (entityType == SYSTEM) {
/* 672 */           Vector localResolutions = c.resolveAllSystem(systemId);
/* 673 */           resolutions = appendVector(resolutions, localResolutions); break;
/*     */         } 
/* 675 */         if (entityType == SYSTEMREVERSE) {
/* 676 */           Vector localResolutions = c.resolveAllSystemReverse(systemId);
/* 677 */           resolutions = appendVector(resolutions, localResolutions);
/*     */         }  }
/*     */     
/*     */     } 
/* 681 */     if (resolutions != null) {
/* 682 */       return resolutions;
/*     */     }
/* 684 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\Resolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */