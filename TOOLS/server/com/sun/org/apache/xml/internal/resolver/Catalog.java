/*      */ package com.sun.org.apache.xml.internal.resolver;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*      */ import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
/*      */ import com.sun.org.apache.xml.internal.resolver.readers.CatalogReader;
/*      */ import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
/*      */ import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Catalog
/*      */ {
/*  194 */   public static final int BASE = CatalogEntry.addEntryType("BASE", 1);
/*      */ 
/*      */   
/*  197 */   public static final int CATALOG = CatalogEntry.addEntryType("CATALOG", 1);
/*      */ 
/*      */   
/*  200 */   public static final int DOCUMENT = CatalogEntry.addEntryType("DOCUMENT", 1);
/*      */ 
/*      */   
/*  203 */   public static final int OVERRIDE = CatalogEntry.addEntryType("OVERRIDE", 1);
/*      */ 
/*      */   
/*  206 */   public static final int SGMLDECL = CatalogEntry.addEntryType("SGMLDECL", 1);
/*      */ 
/*      */   
/*  209 */   public static final int DELEGATE_PUBLIC = CatalogEntry.addEntryType("DELEGATE_PUBLIC", 2);
/*      */ 
/*      */   
/*  212 */   public static final int DELEGATE_SYSTEM = CatalogEntry.addEntryType("DELEGATE_SYSTEM", 2);
/*      */ 
/*      */   
/*  215 */   public static final int DELEGATE_URI = CatalogEntry.addEntryType("DELEGATE_URI", 2);
/*      */ 
/*      */   
/*  218 */   public static final int DOCTYPE = CatalogEntry.addEntryType("DOCTYPE", 2);
/*      */ 
/*      */   
/*  221 */   public static final int DTDDECL = CatalogEntry.addEntryType("DTDDECL", 2);
/*      */ 
/*      */   
/*  224 */   public static final int ENTITY = CatalogEntry.addEntryType("ENTITY", 2);
/*      */ 
/*      */   
/*  227 */   public static final int LINKTYPE = CatalogEntry.addEntryType("LINKTYPE", 2);
/*      */ 
/*      */   
/*  230 */   public static final int NOTATION = CatalogEntry.addEntryType("NOTATION", 2);
/*      */ 
/*      */   
/*  233 */   public static final int PUBLIC = CatalogEntry.addEntryType("PUBLIC", 2);
/*      */ 
/*      */   
/*  236 */   public static final int SYSTEM = CatalogEntry.addEntryType("SYSTEM", 2);
/*      */ 
/*      */   
/*  239 */   public static final int URI = CatalogEntry.addEntryType("URI", 2);
/*      */ 
/*      */   
/*  242 */   public static final int REWRITE_SYSTEM = CatalogEntry.addEntryType("REWRITE_SYSTEM", 2);
/*      */ 
/*      */   
/*  245 */   public static final int REWRITE_URI = CatalogEntry.addEntryType("REWRITE_URI", 2);
/*      */   
/*  247 */   public static final int SYSTEM_SUFFIX = CatalogEntry.addEntryType("SYSTEM_SUFFIX", 2);
/*      */   
/*  249 */   public static final int URI_SUFFIX = CatalogEntry.addEntryType("URI_SUFFIX", 2);
/*      */ 
/*      */ 
/*      */   
/*      */   protected URL base;
/*      */ 
/*      */ 
/*      */   
/*      */   protected URL catalogCwd;
/*      */ 
/*      */ 
/*      */   
/*  261 */   protected Vector catalogEntries = new Vector();
/*      */ 
/*      */   
/*      */   protected boolean default_override = true;
/*      */ 
/*      */   
/*  267 */   protected CatalogManager catalogManager = CatalogManager.getStaticManager();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  280 */   protected Vector catalogFiles = new Vector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  299 */   protected Vector localCatalogFiles = new Vector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  318 */   protected Vector catalogs = new Vector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  335 */   protected Vector localDelegate = new Vector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  344 */   protected Hashtable readerMap = new Hashtable<Object, Object>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  354 */   protected Vector readerArr = new Vector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Catalog() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Catalog(CatalogManager manager) {
/*  375 */     this.catalogManager = manager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CatalogManager getCatalogManager() {
/*  383 */     return this.catalogManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCatalogManager(CatalogManager manager) {
/*  391 */     this.catalogManager = manager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setupReaders() {
/*  398 */     SAXParserFactory spf = SAXParserFactory.newInstance();
/*  399 */     spf.setNamespaceAware(true);
/*  400 */     spf.setValidating(false);
/*      */     
/*  402 */     SAXCatalogReader saxReader = new SAXCatalogReader(spf);
/*      */     
/*  404 */     saxReader.setCatalogParser(null, "XMLCatalog", "com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader");
/*      */ 
/*      */     
/*  407 */     saxReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader");
/*      */ 
/*      */ 
/*      */     
/*  411 */     addReader("application/xml", (CatalogReader)saxReader);
/*      */     
/*  413 */     TR9401CatalogReader textReader = new TR9401CatalogReader();
/*  414 */     addReader("text/plain", (CatalogReader)textReader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addReader(String mimeType, CatalogReader reader) {
/*  438 */     if (this.readerMap.containsKey(mimeType)) {
/*  439 */       Integer pos = (Integer)this.readerMap.get(mimeType);
/*  440 */       this.readerArr.set(pos.intValue(), reader);
/*      */     } else {
/*  442 */       this.readerArr.add(reader);
/*  443 */       Integer pos = new Integer(this.readerArr.size() - 1);
/*  444 */       this.readerMap.put(mimeType, pos);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void copyReaders(Catalog newCatalog) {
/*  459 */     Vector<String> mapArr = new Vector(this.readerMap.size());
/*      */ 
/*      */     
/*  462 */     for (int count = 0; count < this.readerMap.size(); count++) {
/*  463 */       mapArr.add(null);
/*      */     }
/*      */     
/*  466 */     Enumeration<String> en = this.readerMap.keys();
/*  467 */     while (en.hasMoreElements()) {
/*  468 */       String mimeType = en.nextElement();
/*  469 */       Integer pos = (Integer)this.readerMap.get(mimeType);
/*  470 */       mapArr.set(pos.intValue(), mimeType);
/*      */     } 
/*      */     
/*  473 */     for (int i = 0; i < mapArr.size(); i++) {
/*  474 */       String mimeType = mapArr.get(i);
/*  475 */       Integer pos = (Integer)this.readerMap.get(mimeType);
/*  476 */       newCatalog.addReader(mimeType, this.readerArr.get(pos.intValue()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Catalog newCatalog() {
/*  495 */     String catalogClass = getClass().getName();
/*      */     
/*      */     try {
/*  498 */       Catalog catalog = (Catalog)Class.forName(catalogClass).newInstance();
/*  499 */       catalog.setCatalogManager(this.catalogManager);
/*  500 */       copyReaders(catalog);
/*  501 */       return catalog;
/*  502 */     } catch (ClassNotFoundException cnfe) {
/*  503 */       this.catalogManager.debug.message(1, "Class Not Found Exception: " + catalogClass);
/*  504 */     } catch (IllegalAccessException iae) {
/*  505 */       this.catalogManager.debug.message(1, "Illegal Access Exception: " + catalogClass);
/*  506 */     } catch (InstantiationException ie) {
/*  507 */       this.catalogManager.debug.message(1, "Instantiation Exception: " + catalogClass);
/*  508 */     } catch (ClassCastException cce) {
/*  509 */       this.catalogManager.debug.message(1, "Class Cast Exception: " + catalogClass);
/*  510 */     } catch (Exception e) {
/*  511 */       this.catalogManager.debug.message(1, "Other Exception: " + catalogClass);
/*      */     } 
/*      */     
/*  514 */     Catalog c = new Catalog();
/*  515 */     c.setCatalogManager(this.catalogManager);
/*  516 */     copyReaders(c);
/*  517 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCurrentBase() {
/*  524 */     return this.base.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDefaultOverride() {
/*  535 */     if (this.default_override) {
/*  536 */       return "yes";
/*      */     }
/*  538 */     return "no";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadSystemCatalogs() throws MalformedURLException, IOException {
/*  556 */     Vector catalogs = this.catalogManager.getCatalogFiles();
/*  557 */     if (catalogs != null) {
/*  558 */       for (int count = 0; count < catalogs.size(); count++) {
/*  559 */         this.catalogFiles.addElement(catalogs.elementAt(count));
/*      */       }
/*      */     }
/*      */     
/*  563 */     if (this.catalogFiles.size() > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  576 */       String catfile = this.catalogFiles.lastElement();
/*  577 */       this.catalogFiles.removeElement(catfile);
/*  578 */       parseCatalog(catfile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void parseCatalog(String fileName) throws MalformedURLException, IOException {
/*  594 */     this.default_override = this.catalogManager.getPreferPublic();
/*  595 */     this.catalogManager.debug.message(4, "Parse catalog: " + fileName);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  600 */     this.catalogFiles.addElement(fileName);
/*      */ 
/*      */     
/*  603 */     parsePendingCatalogs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void parseCatalog(String mimeType, InputStream is) throws IOException, CatalogException {
/*  622 */     this.default_override = this.catalogManager.getPreferPublic();
/*  623 */     this.catalogManager.debug.message(4, "Parse " + mimeType + " catalog on input stream");
/*      */     
/*  625 */     CatalogReader reader = null;
/*      */     
/*  627 */     if (this.readerMap.containsKey(mimeType)) {
/*  628 */       int arrayPos = ((Integer)this.readerMap.get(mimeType)).intValue();
/*  629 */       reader = this.readerArr.get(arrayPos);
/*      */     } 
/*      */     
/*  632 */     if (reader == null) {
/*  633 */       String msg = "No CatalogReader for MIME type: " + mimeType;
/*  634 */       this.catalogManager.debug.message(2, msg);
/*  635 */       throw new CatalogException(6, msg);
/*      */     } 
/*      */     
/*  638 */     reader.readCatalog(this, is);
/*      */ 
/*      */     
/*  641 */     parsePendingCatalogs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void parseCatalog(URL aUrl) throws IOException {
/*  660 */     this.catalogCwd = aUrl;
/*  661 */     this.base = aUrl;
/*      */     
/*  663 */     this.default_override = this.catalogManager.getPreferPublic();
/*  664 */     this.catalogManager.debug.message(4, "Parse catalog: " + aUrl.toString());
/*      */     
/*  666 */     DataInputStream inStream = null;
/*  667 */     boolean parsed = false;
/*      */     
/*  669 */     for (int count = 0; !parsed && count < this.readerArr.size(); count++) {
/*  670 */       CatalogReader reader = this.readerArr.get(count);
/*      */       
/*      */       try {
/*  673 */         inStream = new DataInputStream(aUrl.openStream());
/*  674 */       } catch (FileNotFoundException fnfe) {
/*      */         break;
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  680 */         reader.readCatalog(this, inStream);
/*  681 */         parsed = true;
/*  682 */       } catch (CatalogException ce) {
/*  683 */         if (ce.getExceptionType() == 7) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  692 */         inStream.close();
/*  693 */       } catch (IOException e) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  698 */     if (parsed) parsePendingCatalogs();
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void parsePendingCatalogs() throws MalformedURLException, IOException {
/*  710 */     if (!this.localCatalogFiles.isEmpty()) {
/*      */ 
/*      */       
/*  713 */       Vector<String> newQueue = new Vector();
/*  714 */       Enumeration q = this.localCatalogFiles.elements();
/*  715 */       while (q.hasMoreElements()) {
/*  716 */         newQueue.addElement(q.nextElement());
/*      */       }
/*      */ 
/*      */       
/*  720 */       for (int curCat = 0; curCat < this.catalogFiles.size(); curCat++) {
/*  721 */         String catfile = this.catalogFiles.elementAt(curCat);
/*  722 */         newQueue.addElement(catfile);
/*      */       } 
/*      */       
/*  725 */       this.catalogFiles = newQueue;
/*  726 */       this.localCatalogFiles.clear();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  732 */     if (this.catalogFiles.isEmpty() && !this.localDelegate.isEmpty()) {
/*  733 */       Enumeration e = this.localDelegate.elements();
/*  734 */       while (e.hasMoreElements()) {
/*  735 */         this.catalogEntries.addElement(e.nextElement());
/*      */       }
/*  737 */       this.localDelegate.clear();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  743 */     while (!this.catalogFiles.isEmpty()) {
/*  744 */       String catfile = this.catalogFiles.elementAt(0);
/*      */       try {
/*  746 */         this.catalogFiles.remove(0);
/*  747 */       } catch (ArrayIndexOutOfBoundsException e) {}
/*      */ 
/*      */ 
/*      */       
/*  751 */       if (this.catalogEntries.size() == 0 && this.catalogs.size() == 0) {
/*      */ 
/*      */         
/*      */         try {
/*  755 */           parseCatalogFile(catfile);
/*  756 */         } catch (CatalogException ce) {
/*  757 */           System.out.println("FIXME: " + ce.toString());
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  762 */         this.catalogs.addElement(catfile);
/*      */       } 
/*      */       
/*  765 */       if (!this.localCatalogFiles.isEmpty()) {
/*      */ 
/*      */         
/*  768 */         Vector<String> newQueue = new Vector();
/*  769 */         Enumeration q = this.localCatalogFiles.elements();
/*  770 */         while (q.hasMoreElements()) {
/*  771 */           newQueue.addElement(q.nextElement());
/*      */         }
/*      */ 
/*      */         
/*  775 */         for (int curCat = 0; curCat < this.catalogFiles.size(); curCat++) {
/*  776 */           catfile = this.catalogFiles.elementAt(curCat);
/*  777 */           newQueue.addElement(catfile);
/*      */         } 
/*      */         
/*  780 */         this.catalogFiles = newQueue;
/*  781 */         this.localCatalogFiles.clear();
/*      */       } 
/*      */       
/*  784 */       if (!this.localDelegate.isEmpty()) {
/*  785 */         Enumeration e = this.localDelegate.elements();
/*  786 */         while (e.hasMoreElements()) {
/*  787 */           this.catalogEntries.addElement(e.nextElement());
/*      */         }
/*  789 */         this.localDelegate.clear();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  794 */     this.catalogFiles.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void parseCatalogFile(String fileName) throws MalformedURLException, IOException, CatalogException {
/*      */     try {
/*  816 */       this.catalogCwd = FileURL.makeURL("basename");
/*  817 */     } catch (MalformedURLException e) {
/*  818 */       String userdir = System.getProperty("user.dir");
/*  819 */       userdir.replace('\\', '/');
/*  820 */       this.catalogManager.debug.message(1, "Malformed URL on cwd", userdir);
/*  821 */       this.catalogCwd = null;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  826 */       this.base = new URL(this.catalogCwd, fixSlashes(fileName));
/*  827 */     } catch (MalformedURLException e) {
/*      */       try {
/*  829 */         this.base = new URL("file:" + fixSlashes(fileName));
/*  830 */       } catch (MalformedURLException e2) {
/*  831 */         this.catalogManager.debug.message(1, "Malformed URL on catalog filename", fixSlashes(fileName));
/*      */         
/*  833 */         this.base = null;
/*      */       } 
/*      */     } 
/*      */     
/*  837 */     this.catalogManager.debug.message(2, "Loading catalog", fileName);
/*  838 */     this.catalogManager.debug.message(4, "Default BASE", this.base.toString());
/*      */     
/*  840 */     fileName = this.base.toString();
/*      */     
/*  842 */     DataInputStream inStream = null;
/*  843 */     boolean parsed = false;
/*  844 */     boolean notFound = false;
/*      */     
/*  846 */     for (int count = 0; !parsed && count < this.readerArr.size(); count++) {
/*  847 */       CatalogReader reader = this.readerArr.get(count);
/*      */       
/*      */       try {
/*  850 */         notFound = false;
/*  851 */         inStream = new DataInputStream(this.base.openStream());
/*  852 */       } catch (FileNotFoundException fnfe) {
/*      */         
/*  854 */         notFound = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */       try {
/*  859 */         reader.readCatalog(this, inStream);
/*  860 */         parsed = true;
/*  861 */       } catch (CatalogException ce) {
/*  862 */         if (ce.getExceptionType() == 7) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  871 */         inStream.close();
/*  872 */       } catch (IOException e) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  877 */     if (!parsed) {
/*  878 */       if (notFound) {
/*  879 */         this.catalogManager.debug.message(3, "Catalog does not exist", fileName);
/*      */       } else {
/*  881 */         this.catalogManager.debug.message(1, "Failed to parse catalog", fileName);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addEntry(CatalogEntry entry) {
/*  897 */     int type = entry.getEntryType();
/*      */     
/*  899 */     if (type == BASE) {
/*  900 */       String value = entry.getEntryArg(0);
/*  901 */       URL newbase = null;
/*      */       
/*  903 */       if (this.base == null) {
/*  904 */         this.catalogManager.debug.message(5, "BASE CUR", "null");
/*      */       } else {
/*  906 */         this.catalogManager.debug.message(5, "BASE CUR", this.base.toString());
/*      */       } 
/*  908 */       this.catalogManager.debug.message(4, "BASE STR", value);
/*      */       
/*      */       try {
/*  911 */         value = fixSlashes(value);
/*  912 */         newbase = new URL(this.base, value);
/*  913 */       } catch (MalformedURLException e) {
/*      */         try {
/*  915 */           newbase = new URL("file:" + value);
/*  916 */         } catch (MalformedURLException e2) {
/*  917 */           this.catalogManager.debug.message(1, "Malformed URL on base", value);
/*  918 */           newbase = null;
/*      */         } 
/*      */       } 
/*      */       
/*  922 */       if (newbase != null) {
/*  923 */         this.base = newbase;
/*      */       }
/*      */       
/*  926 */       this.catalogManager.debug.message(5, "BASE NEW", this.base.toString());
/*  927 */     } else if (type == CATALOG) {
/*  928 */       String fsi = makeAbsolute(entry.getEntryArg(0));
/*      */       
/*  930 */       this.catalogManager.debug.message(4, "CATALOG", fsi);
/*      */       
/*  932 */       this.localCatalogFiles.addElement(fsi);
/*  933 */     } else if (type == PUBLIC) {
/*  934 */       String publicid = PublicId.normalize(entry.getEntryArg(0));
/*  935 */       String systemid = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/*  937 */       entry.setEntryArg(0, publicid);
/*  938 */       entry.setEntryArg(1, systemid);
/*      */       
/*  940 */       this.catalogManager.debug.message(4, "PUBLIC", publicid, systemid);
/*      */       
/*  942 */       this.catalogEntries.addElement(entry);
/*  943 */     } else if (type == SYSTEM) {
/*  944 */       String systemid = normalizeURI(entry.getEntryArg(0));
/*  945 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/*  947 */       entry.setEntryArg(1, fsi);
/*      */       
/*  949 */       this.catalogManager.debug.message(4, "SYSTEM", systemid, fsi);
/*      */       
/*  951 */       this.catalogEntries.addElement(entry);
/*  952 */     } else if (type == URI) {
/*  953 */       String uri = normalizeURI(entry.getEntryArg(0));
/*  954 */       String altURI = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/*  956 */       entry.setEntryArg(1, altURI);
/*      */       
/*  958 */       this.catalogManager.debug.message(4, "URI", uri, altURI);
/*      */       
/*  960 */       this.catalogEntries.addElement(entry);
/*  961 */     } else if (type == DOCUMENT) {
/*  962 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(0)));
/*  963 */       entry.setEntryArg(0, fsi);
/*      */       
/*  965 */       this.catalogManager.debug.message(4, "DOCUMENT", fsi);
/*      */       
/*  967 */       this.catalogEntries.addElement(entry);
/*  968 */     } else if (type == OVERRIDE) {
/*  969 */       this.catalogManager.debug.message(4, "OVERRIDE", entry.getEntryArg(0));
/*      */       
/*  971 */       this.catalogEntries.addElement(entry);
/*  972 */     } else if (type == SGMLDECL) {
/*      */       
/*  974 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(0)));
/*  975 */       entry.setEntryArg(0, fsi);
/*      */       
/*  977 */       this.catalogManager.debug.message(4, "SGMLDECL", fsi);
/*      */       
/*  979 */       this.catalogEntries.addElement(entry);
/*  980 */     } else if (type == DELEGATE_PUBLIC) {
/*  981 */       String ppi = PublicId.normalize(entry.getEntryArg(0));
/*  982 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/*  984 */       entry.setEntryArg(0, ppi);
/*  985 */       entry.setEntryArg(1, fsi);
/*      */       
/*  987 */       this.catalogManager.debug.message(4, "DELEGATE_PUBLIC", ppi, fsi);
/*      */       
/*  989 */       addDelegate(entry);
/*  990 */     } else if (type == DELEGATE_SYSTEM) {
/*  991 */       String psi = normalizeURI(entry.getEntryArg(0));
/*  992 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/*  994 */       entry.setEntryArg(0, psi);
/*  995 */       entry.setEntryArg(1, fsi);
/*      */       
/*  997 */       this.catalogManager.debug.message(4, "DELEGATE_SYSTEM", psi, fsi);
/*      */       
/*  999 */       addDelegate(entry);
/* 1000 */     } else if (type == DELEGATE_URI) {
/* 1001 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1002 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/* 1004 */       entry.setEntryArg(0, pui);
/* 1005 */       entry.setEntryArg(1, fsi);
/*      */       
/* 1007 */       this.catalogManager.debug.message(4, "DELEGATE_URI", pui, fsi);
/*      */       
/* 1009 */       addDelegate(entry);
/* 1010 */     } else if (type == REWRITE_SYSTEM) {
/* 1011 */       String psi = normalizeURI(entry.getEntryArg(0));
/* 1012 */       String rpx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/* 1014 */       entry.setEntryArg(0, psi);
/* 1015 */       entry.setEntryArg(1, rpx);
/*      */       
/* 1017 */       this.catalogManager.debug.message(4, "REWRITE_SYSTEM", psi, rpx);
/*      */       
/* 1019 */       this.catalogEntries.addElement(entry);
/* 1020 */     } else if (type == REWRITE_URI) {
/* 1021 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1022 */       String upx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/* 1024 */       entry.setEntryArg(0, pui);
/* 1025 */       entry.setEntryArg(1, upx);
/*      */       
/* 1027 */       this.catalogManager.debug.message(4, "REWRITE_URI", pui, upx);
/*      */       
/* 1029 */       this.catalogEntries.addElement(entry);
/* 1030 */     } else if (type == SYSTEM_SUFFIX) {
/* 1031 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1032 */       String upx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/* 1034 */       entry.setEntryArg(0, pui);
/* 1035 */       entry.setEntryArg(1, upx);
/*      */       
/* 1037 */       this.catalogManager.debug.message(4, "SYSTEM_SUFFIX", pui, upx);
/*      */       
/* 1039 */       this.catalogEntries.addElement(entry);
/* 1040 */     } else if (type == URI_SUFFIX) {
/* 1041 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1042 */       String upx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */       
/* 1044 */       entry.setEntryArg(0, pui);
/* 1045 */       entry.setEntryArg(1, upx);
/*      */       
/* 1047 */       this.catalogManager.debug.message(4, "URI_SUFFIX", pui, upx);
/*      */       
/* 1049 */       this.catalogEntries.addElement(entry);
/* 1050 */     } else if (type == DOCTYPE) {
/* 1051 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1052 */       entry.setEntryArg(1, fsi);
/*      */       
/* 1054 */       this.catalogManager.debug.message(4, "DOCTYPE", entry.getEntryArg(0), fsi);
/*      */       
/* 1056 */       this.catalogEntries.addElement(entry);
/* 1057 */     } else if (type == DTDDECL) {
/*      */       
/* 1059 */       String fpi = PublicId.normalize(entry.getEntryArg(0));
/* 1060 */       entry.setEntryArg(0, fpi);
/* 1061 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1062 */       entry.setEntryArg(1, fsi);
/*      */       
/* 1064 */       this.catalogManager.debug.message(4, "DTDDECL", fpi, fsi);
/*      */       
/* 1066 */       this.catalogEntries.addElement(entry);
/* 1067 */     } else if (type == ENTITY) {
/* 1068 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1069 */       entry.setEntryArg(1, fsi);
/*      */       
/* 1071 */       this.catalogManager.debug.message(4, "ENTITY", entry.getEntryArg(0), fsi);
/*      */       
/* 1073 */       this.catalogEntries.addElement(entry);
/* 1074 */     } else if (type == LINKTYPE) {
/*      */       
/* 1076 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1077 */       entry.setEntryArg(1, fsi);
/*      */       
/* 1079 */       this.catalogManager.debug.message(4, "LINKTYPE", entry.getEntryArg(0), fsi);
/*      */       
/* 1081 */       this.catalogEntries.addElement(entry);
/* 1082 */     } else if (type == NOTATION) {
/* 1083 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1084 */       entry.setEntryArg(1, fsi);
/*      */       
/* 1086 */       this.catalogManager.debug.message(4, "NOTATION", entry.getEntryArg(0), fsi);
/*      */       
/* 1088 */       this.catalogEntries.addElement(entry);
/*      */     } else {
/* 1090 */       this.catalogEntries.addElement(entry);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unknownEntry(Vector<String> strings) {
/* 1101 */     if (strings != null && strings.size() > 0) {
/* 1102 */       String keyword = strings.elementAt(0);
/* 1103 */       this.catalogManager.debug.message(2, "Unrecognized token parsing catalog", keyword);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseAllCatalogs() throws MalformedURLException, IOException {
/* 1140 */     for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
/* 1141 */       Catalog c = null;
/*      */       
/*      */       try {
/* 1144 */         c = this.catalogs.elementAt(catPos);
/* 1145 */       } catch (ClassCastException e) {
/* 1146 */         String catfile = this.catalogs.elementAt(catPos);
/* 1147 */         c = newCatalog();
/*      */         
/* 1149 */         c.parseCatalog(catfile);
/* 1150 */         this.catalogs.setElementAt(c, catPos);
/* 1151 */         c.parseAllCatalogs();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1156 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1157 */     while (en.hasMoreElements()) {
/* 1158 */       CatalogEntry e = en.nextElement();
/* 1159 */       if (e.getEntryType() == DELEGATE_PUBLIC || e.getEntryType() == DELEGATE_SYSTEM || e.getEntryType() == DELEGATE_URI) {
/*      */ 
/*      */         
/* 1162 */         Catalog dcat = newCatalog();
/* 1163 */         dcat.parseCatalog(e.getEntryArg(1));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String resolveDoctype(String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
/* 1189 */     String resolved = null;
/*      */     
/* 1191 */     this.catalogManager.debug.message(3, "resolveDoctype(" + entityName + "," + publicId + "," + systemId + ")");
/*      */ 
/*      */     
/* 1194 */     systemId = normalizeURI(systemId);
/*      */     
/* 1196 */     if (publicId != null && publicId.startsWith("urn:publicid:")) {
/* 1197 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */     
/* 1200 */     if (systemId != null && systemId.startsWith("urn:publicid:")) {
/* 1201 */       systemId = PublicId.decodeURN(systemId);
/* 1202 */       if (publicId != null && !publicId.equals(systemId)) {
/* 1203 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1204 */         systemId = null;
/*      */       } else {
/* 1206 */         publicId = systemId;
/* 1207 */         systemId = null;
/*      */       } 
/*      */     } 
/*      */     
/* 1211 */     if (systemId != null) {
/*      */       
/* 1213 */       resolved = resolveLocalSystem(systemId);
/* 1214 */       if (resolved != null) {
/* 1215 */         return resolved;
/*      */       }
/*      */     } 
/*      */     
/* 1219 */     if (publicId != null) {
/*      */       
/* 1221 */       resolved = resolveLocalPublic(DOCTYPE, entityName, publicId, systemId);
/*      */ 
/*      */ 
/*      */       
/* 1225 */       if (resolved != null) {
/* 1226 */         return resolved;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1231 */     boolean over = this.default_override;
/* 1232 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1233 */     while (en.hasMoreElements()) {
/* 1234 */       CatalogEntry e = en.nextElement();
/* 1235 */       if (e.getEntryType() == OVERRIDE) {
/* 1236 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */         
/*      */         continue;
/*      */       } 
/* 1240 */       if (e.getEntryType() == DOCTYPE && e.getEntryArg(0).equals(entityName))
/*      */       {
/* 1242 */         if (over || systemId == null) {
/* 1243 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1249 */     return resolveSubordinateCatalogs(DOCTYPE, entityName, publicId, systemId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String resolveDocument() throws MalformedURLException, IOException {
/* 1268 */     this.catalogManager.debug.message(3, "resolveDocument");
/*      */     
/* 1270 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1271 */     while (en.hasMoreElements()) {
/* 1272 */       CatalogEntry e = en.nextElement();
/* 1273 */       if (e.getEntryType() == DOCUMENT) {
/* 1274 */         return e.getEntryArg(0);
/*      */       }
/*      */     } 
/*      */     
/* 1278 */     return resolveSubordinateCatalogs(DOCUMENT, null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String resolveEntity(String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
/* 1302 */     String resolved = null;
/*      */     
/* 1304 */     this.catalogManager.debug.message(3, "resolveEntity(" + entityName + "," + publicId + "," + systemId + ")");
/*      */ 
/*      */     
/* 1307 */     systemId = normalizeURI(systemId);
/*      */     
/* 1309 */     if (publicId != null && publicId.startsWith("urn:publicid:")) {
/* 1310 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */     
/* 1313 */     if (systemId != null && systemId.startsWith("urn:publicid:")) {
/* 1314 */       systemId = PublicId.decodeURN(systemId);
/* 1315 */       if (publicId != null && !publicId.equals(systemId)) {
/* 1316 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1317 */         systemId = null;
/*      */       } else {
/* 1319 */         publicId = systemId;
/* 1320 */         systemId = null;
/*      */       } 
/*      */     } 
/*      */     
/* 1324 */     if (systemId != null) {
/*      */       
/* 1326 */       resolved = resolveLocalSystem(systemId);
/* 1327 */       if (resolved != null) {
/* 1328 */         return resolved;
/*      */       }
/*      */     } 
/*      */     
/* 1332 */     if (publicId != null) {
/*      */       
/* 1334 */       resolved = resolveLocalPublic(ENTITY, entityName, publicId, systemId);
/*      */ 
/*      */ 
/*      */       
/* 1338 */       if (resolved != null) {
/* 1339 */         return resolved;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1344 */     boolean over = this.default_override;
/* 1345 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1346 */     while (en.hasMoreElements()) {
/* 1347 */       CatalogEntry e = en.nextElement();
/* 1348 */       if (e.getEntryType() == OVERRIDE) {
/* 1349 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */         
/*      */         continue;
/*      */       } 
/* 1353 */       if (e.getEntryType() == ENTITY && e.getEntryArg(0).equals(entityName))
/*      */       {
/* 1355 */         if (over || systemId == null) {
/* 1356 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1362 */     return resolveSubordinateCatalogs(ENTITY, entityName, publicId, systemId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String resolveNotation(String notationName, String publicId, String systemId) throws MalformedURLException, IOException {
/* 1388 */     String resolved = null;
/*      */     
/* 1390 */     this.catalogManager.debug.message(3, "resolveNotation(" + notationName + "," + publicId + "," + systemId + ")");
/*      */ 
/*      */     
/* 1393 */     systemId = normalizeURI(systemId);
/*      */     
/* 1395 */     if (publicId != null && publicId.startsWith("urn:publicid:")) {
/* 1396 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */     
/* 1399 */     if (systemId != null && systemId.startsWith("urn:publicid:")) {
/* 1400 */       systemId = PublicId.decodeURN(systemId);
/* 1401 */       if (publicId != null && !publicId.equals(systemId)) {
/* 1402 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1403 */         systemId = null;
/*      */       } else {
/* 1405 */         publicId = systemId;
/* 1406 */         systemId = null;
/*      */       } 
/*      */     } 
/*      */     
/* 1410 */     if (systemId != null) {
/*      */       
/* 1412 */       resolved = resolveLocalSystem(systemId);
/* 1413 */       if (resolved != null) {
/* 1414 */         return resolved;
/*      */       }
/*      */     } 
/*      */     
/* 1418 */     if (publicId != null) {
/*      */       
/* 1420 */       resolved = resolveLocalPublic(NOTATION, notationName, publicId, systemId);
/*      */ 
/*      */ 
/*      */       
/* 1424 */       if (resolved != null) {
/* 1425 */         return resolved;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1430 */     boolean over = this.default_override;
/* 1431 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1432 */     while (en.hasMoreElements()) {
/* 1433 */       CatalogEntry e = en.nextElement();
/* 1434 */       if (e.getEntryType() == OVERRIDE) {
/* 1435 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */         
/*      */         continue;
/*      */       } 
/* 1439 */       if (e.getEntryType() == NOTATION && e.getEntryArg(0).equals(notationName))
/*      */       {
/* 1441 */         if (over || systemId == null) {
/* 1442 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1448 */     return resolveSubordinateCatalogs(NOTATION, notationName, publicId, systemId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String resolvePublic(String publicId, String systemId) throws MalformedURLException, IOException {
/* 1480 */     this.catalogManager.debug.message(3, "resolvePublic(" + publicId + "," + systemId + ")");
/*      */     
/* 1482 */     systemId = normalizeURI(systemId);
/*      */     
/* 1484 */     if (publicId != null && publicId.startsWith("urn:publicid:")) {
/* 1485 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */     
/* 1488 */     if (systemId != null && systemId.startsWith("urn:publicid:")) {
/* 1489 */       systemId = PublicId.decodeURN(systemId);
/* 1490 */       if (publicId != null && !publicId.equals(systemId)) {
/* 1491 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1492 */         systemId = null;
/*      */       } else {
/* 1494 */         publicId = systemId;
/* 1495 */         systemId = null;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1500 */     if (systemId != null) {
/* 1501 */       String str = resolveLocalSystem(systemId);
/* 1502 */       if (str != null) {
/* 1503 */         return str;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1508 */     String resolved = resolveLocalPublic(PUBLIC, null, publicId, systemId);
/*      */ 
/*      */ 
/*      */     
/* 1512 */     if (resolved != null) {
/* 1513 */       return resolved;
/*      */     }
/*      */ 
/*      */     
/* 1517 */     return resolveSubordinateCatalogs(PUBLIC, null, publicId, systemId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized String resolveLocalPublic(int entityType, String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
/* 1578 */     publicId = PublicId.normalize(publicId);
/*      */ 
/*      */     
/* 1581 */     if (systemId != null) {
/* 1582 */       String resolved = resolveLocalSystem(systemId);
/* 1583 */       if (resolved != null) {
/* 1584 */         return resolved;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1589 */     boolean over = this.default_override;
/* 1590 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1591 */     while (en.hasMoreElements()) {
/* 1592 */       CatalogEntry e = en.nextElement();
/* 1593 */       if (e.getEntryType() == OVERRIDE) {
/* 1594 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */         
/*      */         continue;
/*      */       } 
/* 1598 */       if (e.getEntryType() == PUBLIC && e.getEntryArg(0).equals(publicId))
/*      */       {
/* 1600 */         if (over || systemId == null) {
/* 1601 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1607 */     over = this.default_override;
/* 1608 */     en = this.catalogEntries.elements();
/* 1609 */     Vector<String> delCats = new Vector();
/* 1610 */     while (en.hasMoreElements()) {
/* 1611 */       CatalogEntry e = en.nextElement();
/* 1612 */       if (e.getEntryType() == OVERRIDE) {
/* 1613 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */         
/*      */         continue;
/*      */       } 
/* 1617 */       if (e.getEntryType() == DELEGATE_PUBLIC && (over || systemId == null)) {
/*      */         
/* 1619 */         String p = e.getEntryArg(0);
/* 1620 */         if (p.length() <= publicId.length() && p.equals(publicId.substring(0, p.length())))
/*      */         {
/*      */ 
/*      */           
/* 1624 */           delCats.addElement(e.getEntryArg(1));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1629 */     if (delCats.size() > 0) {
/* 1630 */       Enumeration<String> enCats = delCats.elements();
/*      */       
/* 1632 */       if (this.catalogManager.debug.getDebug() > 1) {
/* 1633 */         this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
/* 1634 */         while (enCats.hasMoreElements()) {
/* 1635 */           String delegatedCatalog = enCats.nextElement();
/* 1636 */           this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
/*      */         } 
/*      */       } 
/*      */       
/* 1640 */       Catalog dcat = newCatalog();
/*      */       
/* 1642 */       enCats = delCats.elements();
/* 1643 */       while (enCats.hasMoreElements()) {
/* 1644 */         String delegatedCatalog = enCats.nextElement();
/* 1645 */         dcat.parseCatalog(delegatedCatalog);
/*      */       } 
/*      */       
/* 1648 */       return dcat.resolvePublic(publicId, null);
/*      */     } 
/*      */ 
/*      */     
/* 1652 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String resolveSystem(String systemId) throws MalformedURLException, IOException {
/* 1676 */     this.catalogManager.debug.message(3, "resolveSystem(" + systemId + ")");
/*      */     
/* 1678 */     systemId = normalizeURI(systemId);
/*      */     
/* 1680 */     if (systemId != null && systemId.startsWith("urn:publicid:")) {
/* 1681 */       systemId = PublicId.decodeURN(systemId);
/* 1682 */       return resolvePublic(systemId, null);
/*      */     } 
/*      */ 
/*      */     
/* 1686 */     if (systemId != null) {
/* 1687 */       String resolved = resolveLocalSystem(systemId);
/* 1688 */       if (resolved != null) {
/* 1689 */         return resolved;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1694 */     return resolveSubordinateCatalogs(SYSTEM, null, null, systemId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveLocalSystem(String systemId) throws MalformedURLException, IOException {
/* 1714 */     String osname = System.getProperty("os.name");
/* 1715 */     boolean windows = (osname.indexOf("Windows") >= 0);
/* 1716 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1717 */     while (en.hasMoreElements()) {
/* 1718 */       CatalogEntry e = en.nextElement();
/* 1719 */       if (e.getEntryType() == SYSTEM && (e.getEntryArg(0).equals(systemId) || (windows && e.getEntryArg(0).equalsIgnoreCase(systemId))))
/*      */       {
/*      */ 
/*      */         
/* 1723 */         return e.getEntryArg(1);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1728 */     en = this.catalogEntries.elements();
/* 1729 */     String startString = null;
/* 1730 */     String prefix = null;
/* 1731 */     while (en.hasMoreElements()) {
/* 1732 */       CatalogEntry e = en.nextElement();
/*      */       
/* 1734 */       if (e.getEntryType() == REWRITE_SYSTEM) {
/* 1735 */         String p = e.getEntryArg(0);
/* 1736 */         if (p.length() <= systemId.length() && p.equals(systemId.substring(0, p.length())))
/*      */         {
/*      */           
/* 1739 */           if (startString == null || p.length() > startString.length()) {
/*      */             
/* 1741 */             startString = p;
/* 1742 */             prefix = e.getEntryArg(1);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1748 */     if (prefix != null)
/*      */     {
/* 1750 */       return prefix + systemId.substring(startString.length());
/*      */     }
/*      */ 
/*      */     
/* 1754 */     en = this.catalogEntries.elements();
/* 1755 */     String suffixString = null;
/* 1756 */     String suffixURI = null;
/* 1757 */     while (en.hasMoreElements()) {
/* 1758 */       CatalogEntry e = en.nextElement();
/*      */       
/* 1760 */       if (e.getEntryType() == SYSTEM_SUFFIX) {
/* 1761 */         String p = e.getEntryArg(0);
/* 1762 */         if (p.length() <= systemId.length() && systemId.endsWith(p))
/*      */         {
/*      */           
/* 1765 */           if (suffixString == null || p.length() > suffixString.length()) {
/*      */             
/* 1767 */             suffixString = p;
/* 1768 */             suffixURI = e.getEntryArg(1);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1774 */     if (suffixURI != null)
/*      */     {
/* 1776 */       return suffixURI;
/*      */     }
/*      */ 
/*      */     
/* 1780 */     en = this.catalogEntries.elements();
/* 1781 */     Vector<String> delCats = new Vector();
/* 1782 */     while (en.hasMoreElements()) {
/* 1783 */       CatalogEntry e = en.nextElement();
/*      */       
/* 1785 */       if (e.getEntryType() == DELEGATE_SYSTEM) {
/* 1786 */         String p = e.getEntryArg(0);
/* 1787 */         if (p.length() <= systemId.length() && p.equals(systemId.substring(0, p.length())))
/*      */         {
/*      */ 
/*      */           
/* 1791 */           delCats.addElement(e.getEntryArg(1));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1796 */     if (delCats.size() > 0) {
/* 1797 */       Enumeration<String> enCats = delCats.elements();
/*      */       
/* 1799 */       if (this.catalogManager.debug.getDebug() > 1) {
/* 1800 */         this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
/* 1801 */         while (enCats.hasMoreElements()) {
/* 1802 */           String delegatedCatalog = enCats.nextElement();
/* 1803 */           this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
/*      */         } 
/*      */       } 
/*      */       
/* 1807 */       Catalog dcat = newCatalog();
/*      */       
/* 1809 */       enCats = delCats.elements();
/* 1810 */       while (enCats.hasMoreElements()) {
/* 1811 */         String delegatedCatalog = enCats.nextElement();
/* 1812 */         dcat.parseCatalog(delegatedCatalog);
/*      */       } 
/*      */       
/* 1815 */       return dcat.resolveSystem(systemId);
/*      */     } 
/*      */     
/* 1818 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String resolveURI(String uri) throws MalformedURLException, IOException {
/* 1840 */     this.catalogManager.debug.message(3, "resolveURI(" + uri + ")");
/*      */     
/* 1842 */     uri = normalizeURI(uri);
/*      */     
/* 1844 */     if (uri != null && uri.startsWith("urn:publicid:")) {
/* 1845 */       uri = PublicId.decodeURN(uri);
/* 1846 */       return resolvePublic(uri, null);
/*      */     } 
/*      */ 
/*      */     
/* 1850 */     if (uri != null) {
/* 1851 */       String resolved = resolveLocalURI(uri);
/* 1852 */       if (resolved != null) {
/* 1853 */         return resolved;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1858 */     return resolveSubordinateCatalogs(URI, null, null, uri);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveLocalURI(String uri) throws MalformedURLException, IOException {
/* 1876 */     Enumeration<CatalogEntry> en = this.catalogEntries.elements();
/* 1877 */     while (en.hasMoreElements()) {
/* 1878 */       CatalogEntry e = en.nextElement();
/* 1879 */       if (e.getEntryType() == URI && e.getEntryArg(0).equals(uri))
/*      */       {
/* 1881 */         return e.getEntryArg(1);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1886 */     en = this.catalogEntries.elements();
/* 1887 */     String startString = null;
/* 1888 */     String prefix = null;
/* 1889 */     while (en.hasMoreElements()) {
/* 1890 */       CatalogEntry e = en.nextElement();
/*      */       
/* 1892 */       if (e.getEntryType() == REWRITE_URI) {
/* 1893 */         String p = e.getEntryArg(0);
/* 1894 */         if (p.length() <= uri.length() && p.equals(uri.substring(0, p.length())))
/*      */         {
/*      */           
/* 1897 */           if (startString == null || p.length() > startString.length()) {
/*      */             
/* 1899 */             startString = p;
/* 1900 */             prefix = e.getEntryArg(1);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1906 */     if (prefix != null)
/*      */     {
/* 1908 */       return prefix + uri.substring(startString.length());
/*      */     }
/*      */ 
/*      */     
/* 1912 */     en = this.catalogEntries.elements();
/* 1913 */     String suffixString = null;
/* 1914 */     String suffixURI = null;
/* 1915 */     while (en.hasMoreElements()) {
/* 1916 */       CatalogEntry e = en.nextElement();
/*      */       
/* 1918 */       if (e.getEntryType() == URI_SUFFIX) {
/* 1919 */         String p = e.getEntryArg(0);
/* 1920 */         if (p.length() <= uri.length() && uri.endsWith(p))
/*      */         {
/*      */           
/* 1923 */           if (suffixString == null || p.length() > suffixString.length()) {
/*      */             
/* 1925 */             suffixString = p;
/* 1926 */             suffixURI = e.getEntryArg(1);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1932 */     if (suffixURI != null)
/*      */     {
/* 1934 */       return suffixURI;
/*      */     }
/*      */ 
/*      */     
/* 1938 */     en = this.catalogEntries.elements();
/* 1939 */     Vector<String> delCats = new Vector();
/* 1940 */     while (en.hasMoreElements()) {
/* 1941 */       CatalogEntry e = en.nextElement();
/*      */       
/* 1943 */       if (e.getEntryType() == DELEGATE_URI) {
/* 1944 */         String p = e.getEntryArg(0);
/* 1945 */         if (p.length() <= uri.length() && p.equals(uri.substring(0, p.length())))
/*      */         {
/*      */ 
/*      */           
/* 1949 */           delCats.addElement(e.getEntryArg(1));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1954 */     if (delCats.size() > 0) {
/* 1955 */       Enumeration<String> enCats = delCats.elements();
/*      */       
/* 1957 */       if (this.catalogManager.debug.getDebug() > 1) {
/* 1958 */         this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
/* 1959 */         while (enCats.hasMoreElements()) {
/* 1960 */           String delegatedCatalog = enCats.nextElement();
/* 1961 */           this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
/*      */         } 
/*      */       } 
/*      */       
/* 1965 */       Catalog dcat = newCatalog();
/*      */       
/* 1967 */       enCats = delCats.elements();
/* 1968 */       while (enCats.hasMoreElements()) {
/* 1969 */         String delegatedCatalog = enCats.nextElement();
/* 1970 */         dcat.parseCatalog(delegatedCatalog);
/*      */       } 
/*      */       
/* 1973 */       return dcat.resolveURI(uri);
/*      */     } 
/*      */     
/* 1976 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized String resolveSubordinateCatalogs(int entityType, String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
/* 2013 */     for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
/* 2014 */       Catalog c = null;
/*      */       
/*      */       try {
/* 2017 */         c = this.catalogs.elementAt(catPos);
/* 2018 */       } catch (ClassCastException e) {
/* 2019 */         String catfile = this.catalogs.elementAt(catPos);
/* 2020 */         c = newCatalog();
/*      */         
/*      */         try {
/* 2023 */           c.parseCatalog(catfile);
/* 2024 */         } catch (MalformedURLException mue) {
/* 2025 */           this.catalogManager.debug.message(1, "Malformed Catalog URL", catfile);
/* 2026 */         } catch (FileNotFoundException fnfe) {
/* 2027 */           this.catalogManager.debug.message(1, "Failed to load catalog, file not found", catfile);
/*      */         }
/* 2029 */         catch (IOException ioe) {
/* 2030 */           this.catalogManager.debug.message(1, "Failed to load catalog, I/O error", catfile);
/*      */         } 
/*      */         
/* 2033 */         this.catalogs.setElementAt(c, catPos);
/*      */       } 
/*      */       
/* 2036 */       String resolved = null;
/*      */ 
/*      */       
/* 2039 */       if (entityType == DOCTYPE) {
/* 2040 */         resolved = c.resolveDoctype(entityName, publicId, systemId);
/*      */       
/*      */       }
/* 2043 */       else if (entityType == DOCUMENT) {
/* 2044 */         resolved = c.resolveDocument();
/* 2045 */       } else if (entityType == ENTITY) {
/* 2046 */         resolved = c.resolveEntity(entityName, publicId, systemId);
/*      */       
/*      */       }
/* 2049 */       else if (entityType == NOTATION) {
/* 2050 */         resolved = c.resolveNotation(entityName, publicId, systemId);
/*      */       
/*      */       }
/* 2053 */       else if (entityType == PUBLIC) {
/* 2054 */         resolved = c.resolvePublic(publicId, systemId);
/* 2055 */       } else if (entityType == SYSTEM) {
/* 2056 */         resolved = c.resolveSystem(systemId);
/* 2057 */       } else if (entityType == URI) {
/* 2058 */         resolved = c.resolveURI(systemId);
/*      */       } 
/*      */       
/* 2061 */       if (resolved != null) {
/* 2062 */         return resolved;
/*      */       }
/*      */     } 
/*      */     
/* 2066 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String fixSlashes(String sysid) {
/* 2080 */     return sysid.replace('\\', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String makeAbsolute(String sysid) {
/* 2092 */     URL local = null;
/*      */     
/* 2094 */     sysid = fixSlashes(sysid);
/*      */     
/*      */     try {
/* 2097 */       local = new URL(this.base, sysid);
/* 2098 */     } catch (MalformedURLException e) {
/* 2099 */       this.catalogManager.debug.message(1, "Malformed URL on system identifier", sysid);
/*      */     } 
/*      */     
/* 2102 */     if (local != null) {
/* 2103 */       return local.toString();
/*      */     }
/* 2105 */     return sysid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String normalizeURI(String uriref) {
/*      */     byte[] bytes;
/* 2116 */     String newRef = "";
/*      */ 
/*      */     
/* 2119 */     if (uriref == null) {
/* 2120 */       return null;
/*      */     }
/*      */     
/*      */     try {
/* 2124 */       bytes = uriref.getBytes("UTF-8");
/* 2125 */     } catch (UnsupportedEncodingException uee) {
/*      */       
/* 2127 */       this.catalogManager.debug.message(1, "UTF-8 is an unsupported encoding!?");
/* 2128 */       return uriref;
/*      */     } 
/*      */     
/* 2131 */     for (int count = 0; count < bytes.length; count++) {
/* 2132 */       int ch = bytes[count] & 0xFF;
/*      */       
/* 2134 */       if (ch <= 32 || ch > 127 || ch == 34 || ch == 60 || ch == 62 || ch == 92 || ch == 94 || ch == 96 || ch == 123 || ch == 124 || ch == 125 || ch == 127) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2146 */         newRef = newRef + encodedByte(ch);
/*      */       } else {
/* 2148 */         newRef = newRef + (char)bytes[count];
/*      */       } 
/*      */     } 
/*      */     
/* 2152 */     return newRef;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String encodedByte(int b) {
/* 2163 */     String hex = Integer.toHexString(b).toUpperCase();
/* 2164 */     if (hex.length() < 2) {
/* 2165 */       return "%0" + hex;
/*      */     }
/* 2167 */     return "%" + hex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addDelegate(CatalogEntry entry) {
/* 2183 */     int pos = 0;
/* 2184 */     String partial = entry.getEntryArg(0);
/*      */     
/* 2186 */     Enumeration<CatalogEntry> local = this.localDelegate.elements();
/* 2187 */     while (local.hasMoreElements()) {
/* 2188 */       CatalogEntry dpe = local.nextElement();
/* 2189 */       String dp = dpe.getEntryArg(0);
/* 2190 */       if (dp.equals(partial)) {
/*      */         return;
/*      */       }
/*      */       
/* 2194 */       if (dp.length() > partial.length()) {
/* 2195 */         pos++;
/*      */       }
/* 2197 */       if (dp.length() < partial.length()) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2203 */     if (this.localDelegate.size() == 0) {
/* 2204 */       this.localDelegate.addElement(entry);
/*      */     } else {
/* 2206 */       this.localDelegate.insertElementAt(entry, pos);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\Catalog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */