/*     */ package com.sun.org.apache.xml.internal.resolver;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.BootstrapResolver;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CatalogManager
/*     */ {
/* 125 */   private static String pFiles = "xml.catalog.files";
/* 126 */   private static String pVerbosity = "xml.catalog.verbosity";
/* 127 */   private static String pPrefer = "xml.catalog.prefer";
/* 128 */   private static String pStatic = "xml.catalog.staticCatalog";
/* 129 */   private static String pAllowPI = "xml.catalog.allowPI";
/* 130 */   private static String pClassname = "xml.catalog.className";
/* 131 */   private static String pIgnoreMissing = "xml.catalog.ignoreMissing";
/*     */ 
/*     */   
/* 134 */   private static CatalogManager staticManager = new CatalogManager();
/*     */ 
/*     */   
/* 137 */   private BootstrapResolver bResolver = new BootstrapResolver();
/*     */ 
/*     */   
/* 140 */   private boolean ignoreMissingProperties = (System.getProperty(pIgnoreMissing) != null || System.getProperty(pFiles) != null);
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceBundle resources;
/*     */ 
/*     */ 
/*     */   
/* 148 */   private String propertyFile = "CatalogManager.properties";
/*     */ 
/*     */   
/* 151 */   private URL propertyFileURI = null;
/*     */ 
/*     */   
/* 154 */   private String defaultCatalogFiles = "./xcatalog";
/*     */ 
/*     */   
/* 157 */   private String catalogFiles = null;
/*     */ 
/*     */   
/*     */   private boolean fromPropertiesFile = false;
/*     */ 
/*     */   
/* 163 */   private int defaultVerbosity = 1;
/*     */ 
/*     */   
/* 166 */   private Integer verbosity = null;
/*     */ 
/*     */   
/*     */   private boolean defaultPreferPublic = true;
/*     */ 
/*     */   
/* 172 */   private Boolean preferPublic = null;
/*     */ 
/*     */   
/*     */   private boolean defaultUseStaticCatalog = true;
/*     */ 
/*     */   
/* 178 */   private Boolean useStaticCatalog = null;
/*     */ 
/*     */   
/* 181 */   private static Catalog staticCatalog = null;
/*     */ 
/*     */   
/*     */   private boolean defaultOasisXMLCatalogPI = true;
/*     */ 
/*     */   
/* 187 */   private Boolean oasisXMLCatalogPI = null;
/*     */ 
/*     */   
/*     */   private boolean defaultRelativeCatalogs = true;
/*     */ 
/*     */   
/* 193 */   private Boolean relativeCatalogs = null;
/*     */ 
/*     */   
/* 196 */   private String catalogClassName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public Debug debug = null;
/*     */ 
/*     */   
/*     */   public CatalogManager() {
/* 207 */     this.debug = new Debug();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatalogManager(String propertyFile) {
/* 217 */     this.propertyFile = propertyFile;
/*     */     
/* 219 */     this.debug = new Debug();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBootstrapResolver(BootstrapResolver resolver) {
/* 229 */     this.bResolver = resolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public BootstrapResolver getBootstrapResolver() {
/* 234 */     return this.bResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void readProperties() {
/*     */     try {
/* 243 */       this.propertyFileURI = CatalogManager.class.getResource("/" + this.propertyFile);
/* 244 */       InputStream in = CatalogManager.class.getResourceAsStream("/" + this.propertyFile);
/*     */       
/* 246 */       if (in == null) {
/* 247 */         if (!this.ignoreMissingProperties) {
/* 248 */           System.err.println("Cannot find " + this.propertyFile);
/*     */           
/* 250 */           this.ignoreMissingProperties = true;
/*     */         } 
/*     */         return;
/*     */       } 
/* 254 */       this.resources = new PropertyResourceBundle(in);
/* 255 */     } catch (MissingResourceException mre) {
/* 256 */       if (!this.ignoreMissingProperties) {
/* 257 */         System.err.println("Cannot read " + this.propertyFile);
/*     */       }
/* 259 */     } catch (IOException e) {
/* 260 */       if (!this.ignoreMissingProperties) {
/* 261 */         System.err.println("Failure trying to read " + this.propertyFile);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     if (this.verbosity == null) {
/*     */       try {
/* 270 */         String verbStr = this.resources.getString("verbosity");
/* 271 */         int verb = Integer.parseInt(verbStr.trim());
/* 272 */         this.debug.setDebug(verb);
/* 273 */         this.verbosity = new Integer(verb);
/* 274 */       } catch (Exception e) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CatalogManager getStaticManager() {
/* 284 */     return staticManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIgnoreMissingProperties() {
/* 295 */     return this.ignoreMissingProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreMissingProperties(boolean ignore) {
/* 306 */     this.ignoreMissingProperties = ignore;
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
/*     */   public void ignoreMissingProperties(boolean ignore) {
/* 319 */     setIgnoreMissingProperties(ignore);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int queryVerbosity() {
/* 329 */     String defaultVerbStr = Integer.toString(this.defaultVerbosity);
/*     */     
/* 331 */     String verbStr = System.getProperty(pVerbosity);
/*     */     
/* 333 */     if (verbStr == null) {
/* 334 */       if (this.resources == null) readProperties(); 
/* 335 */       if (this.resources != null) {
/*     */         try {
/* 337 */           verbStr = this.resources.getString("verbosity");
/* 338 */         } catch (MissingResourceException e) {
/* 339 */           verbStr = defaultVerbStr;
/*     */         } 
/*     */       } else {
/* 342 */         verbStr = defaultVerbStr;
/*     */       } 
/*     */     } 
/*     */     
/* 346 */     int verb = this.defaultVerbosity;
/*     */     
/*     */     try {
/* 349 */       verb = Integer.parseInt(verbStr.trim());
/* 350 */     } catch (Exception e) {
/* 351 */       System.err.println("Cannot parse verbosity: \"" + verbStr + "\"");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 357 */     if (this.verbosity == null) {
/* 358 */       this.debug.setDebug(verb);
/* 359 */       this.verbosity = new Integer(verb);
/*     */     } 
/*     */     
/* 362 */     return verb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVerbosity() {
/* 369 */     if (this.verbosity == null) {
/* 370 */       this.verbosity = new Integer(queryVerbosity());
/*     */     }
/*     */     
/* 373 */     return this.verbosity.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbosity(int verbosity) {
/* 380 */     this.verbosity = new Integer(verbosity);
/* 381 */     this.debug.setDebug(verbosity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int verbosity() {
/* 390 */     return getVerbosity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean queryRelativeCatalogs() {
/* 400 */     if (this.resources == null) readProperties();
/*     */     
/* 402 */     if (this.resources == null) return this.defaultRelativeCatalogs;
/*     */     
/*     */     try {
/* 405 */       String allow = this.resources.getString("relative-catalogs");
/* 406 */       return (allow.equalsIgnoreCase("true") || allow.equalsIgnoreCase("yes") || allow.equalsIgnoreCase("1"));
/*     */     
/*     */     }
/* 409 */     catch (MissingResourceException e) {
/* 410 */       return this.defaultRelativeCatalogs;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRelativeCatalogs() {
/* 435 */     if (this.relativeCatalogs == null) {
/* 436 */       this.relativeCatalogs = new Boolean(queryRelativeCatalogs());
/*     */     }
/*     */     
/* 439 */     return this.relativeCatalogs.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRelativeCatalogs(boolean relative) {
/* 448 */     this.relativeCatalogs = new Boolean(relative);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean relativeCatalogs() {
/* 457 */     return getRelativeCatalogs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String queryCatalogFiles() {
/* 466 */     String catalogList = System.getProperty(pFiles);
/* 467 */     this.fromPropertiesFile = false;
/*     */     
/* 469 */     if (catalogList == null) {
/* 470 */       if (this.resources == null) readProperties(); 
/* 471 */       if (this.resources != null) {
/*     */         try {
/* 473 */           catalogList = this.resources.getString("catalogs");
/* 474 */           this.fromPropertiesFile = true;
/* 475 */         } catch (MissingResourceException e) {
/* 476 */           System.err.println(this.propertyFile + ": catalogs not found.");
/* 477 */           catalogList = null;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 482 */     if (catalogList == null) {
/* 483 */       catalogList = this.defaultCatalogFiles;
/*     */     }
/*     */     
/* 486 */     return catalogList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getCatalogFiles() {
/* 496 */     if (this.catalogFiles == null) {
/* 497 */       this.catalogFiles = queryCatalogFiles();
/*     */     }
/*     */     
/* 500 */     StringTokenizer files = new StringTokenizer(this.catalogFiles, ";");
/* 501 */     Vector<String> catalogs = new Vector();
/* 502 */     while (files.hasMoreTokens()) {
/* 503 */       String catalogFile = files.nextToken();
/* 504 */       URL absURI = null;
/*     */       
/* 506 */       if (this.fromPropertiesFile && !relativeCatalogs()) {
/*     */         try {
/* 508 */           absURI = new URL(this.propertyFileURI, catalogFile);
/* 509 */           catalogFile = absURI.toString();
/* 510 */         } catch (MalformedURLException mue) {
/* 511 */           absURI = null;
/*     */         } 
/*     */       }
/*     */       
/* 515 */       catalogs.add(catalogFile);
/*     */     } 
/*     */     
/* 518 */     return catalogs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalogFiles(String fileList) {
/* 525 */     this.catalogFiles = fileList;
/* 526 */     this.fromPropertiesFile = false;
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
/*     */   public Vector catalogFiles() {
/* 538 */     return getCatalogFiles();
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
/*     */   private boolean queryPreferPublic() {
/* 551 */     String prefer = System.getProperty(pPrefer);
/*     */     
/* 553 */     if (prefer == null) {
/* 554 */       if (this.resources == null) readProperties(); 
/* 555 */       if (this.resources == null) return this.defaultPreferPublic; 
/*     */       try {
/* 557 */         prefer = this.resources.getString("prefer");
/* 558 */       } catch (MissingResourceException e) {
/* 559 */         return this.defaultPreferPublic;
/*     */       } 
/*     */     } 
/*     */     
/* 563 */     if (prefer == null) {
/* 564 */       return this.defaultPreferPublic;
/*     */     }
/*     */     
/* 567 */     return prefer.equalsIgnoreCase("public");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getPreferPublic() {
/* 576 */     if (this.preferPublic == null) {
/* 577 */       this.preferPublic = new Boolean(queryPreferPublic());
/*     */     }
/* 579 */     return this.preferPublic.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreferPublic(boolean preferPublic) {
/* 586 */     this.preferPublic = new Boolean(preferPublic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean preferPublic() {
/* 597 */     return getPreferPublic();
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
/*     */   private boolean queryUseStaticCatalog() {
/* 610 */     String staticCatalog = System.getProperty(pStatic);
/*     */     
/* 612 */     if (staticCatalog == null) {
/* 613 */       if (this.resources == null) readProperties(); 
/* 614 */       if (this.resources == null) return this.defaultUseStaticCatalog; 
/*     */       try {
/* 616 */         staticCatalog = this.resources.getString("static-catalog");
/* 617 */       } catch (MissingResourceException e) {
/* 618 */         return this.defaultUseStaticCatalog;
/*     */       } 
/*     */     } 
/*     */     
/* 622 */     if (staticCatalog == null) {
/* 623 */       return this.defaultUseStaticCatalog;
/*     */     }
/*     */     
/* 626 */     return (staticCatalog.equalsIgnoreCase("true") || staticCatalog.equalsIgnoreCase("yes") || staticCatalog.equalsIgnoreCase("1"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseStaticCatalog() {
/* 635 */     if (this.useStaticCatalog == null) {
/* 636 */       this.useStaticCatalog = new Boolean(queryUseStaticCatalog());
/*     */     }
/*     */     
/* 639 */     return this.useStaticCatalog.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseStaticCatalog(boolean useStatic) {
/* 646 */     this.useStaticCatalog = new Boolean(useStatic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean staticCatalog() {
/* 655 */     return getUseStaticCatalog();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Catalog getPrivateCatalog() {
/* 664 */     Catalog catalog = staticCatalog;
/*     */     
/* 666 */     if (this.useStaticCatalog == null) {
/* 667 */       this.useStaticCatalog = new Boolean(getUseStaticCatalog());
/*     */     }
/*     */     
/* 670 */     if (catalog == null || !this.useStaticCatalog.booleanValue()) {
/*     */       
/*     */       try {
/* 673 */         String catalogClassName = getCatalogClassName();
/*     */         
/* 675 */         if (catalogClassName == null) {
/* 676 */           catalog = new Catalog();
/*     */         } else {
/*     */           try {
/* 679 */             catalog = (Catalog)Class.forName(catalogClassName).newInstance();
/* 680 */           } catch (ClassNotFoundException cnfe) {
/* 681 */             this.debug.message(1, "Catalog class named '" + catalogClassName + "' could not be found. Using default.");
/*     */ 
/*     */             
/* 684 */             catalog = new Catalog();
/* 685 */           } catch (ClassCastException cnfe) {
/* 686 */             this.debug.message(1, "Class named '" + catalogClassName + "' is not a Catalog. Using default.");
/*     */ 
/*     */             
/* 689 */             catalog = new Catalog();
/*     */           } 
/*     */         } 
/*     */         
/* 693 */         catalog.setCatalogManager(this);
/* 694 */         catalog.setupReaders();
/* 695 */         catalog.loadSystemCatalogs();
/* 696 */       } catch (Exception ex) {
/* 697 */         ex.printStackTrace();
/*     */       } 
/*     */       
/* 700 */       if (this.useStaticCatalog.booleanValue()) {
/* 701 */         staticCatalog = catalog;
/*     */       }
/*     */     } 
/*     */     
/* 705 */     return catalog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Catalog getCatalog() {
/* 715 */     Catalog catalog = staticCatalog;
/*     */     
/* 717 */     if (this.useStaticCatalog == null) {
/* 718 */       this.useStaticCatalog = new Boolean(getUseStaticCatalog());
/*     */     }
/*     */     
/* 721 */     if (catalog == null || !this.useStaticCatalog.booleanValue()) {
/* 722 */       catalog = getPrivateCatalog();
/* 723 */       if (this.useStaticCatalog.booleanValue()) {
/* 724 */         staticCatalog = catalog;
/*     */       }
/*     */     } 
/*     */     
/* 728 */     return catalog;
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
/*     */   public boolean queryAllowOasisXMLCatalogPI() {
/* 741 */     String allow = System.getProperty(pAllowPI);
/*     */     
/* 743 */     if (allow == null) {
/* 744 */       if (this.resources == null) readProperties(); 
/* 745 */       if (this.resources == null) return this.defaultOasisXMLCatalogPI; 
/*     */       try {
/* 747 */         allow = this.resources.getString("allow-oasis-xml-catalog-pi");
/* 748 */       } catch (MissingResourceException e) {
/* 749 */         return this.defaultOasisXMLCatalogPI;
/*     */       } 
/*     */     } 
/*     */     
/* 753 */     if (allow == null) {
/* 754 */       return this.defaultOasisXMLCatalogPI;
/*     */     }
/*     */     
/* 757 */     return (allow.equalsIgnoreCase("true") || allow.equalsIgnoreCase("yes") || allow.equalsIgnoreCase("1"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAllowOasisXMLCatalogPI() {
/* 766 */     if (this.oasisXMLCatalogPI == null) {
/* 767 */       this.oasisXMLCatalogPI = new Boolean(queryAllowOasisXMLCatalogPI());
/*     */     }
/*     */     
/* 770 */     return this.oasisXMLCatalogPI.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowOasisXMLCatalogPI(boolean allowPI) {
/* 777 */     this.oasisXMLCatalogPI = new Boolean(allowPI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean allowOasisXMLCatalogPI() {
/* 786 */     return getAllowOasisXMLCatalogPI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String queryCatalogClassName() {
/* 794 */     String className = System.getProperty(pClassname);
/*     */     
/* 796 */     if (className == null) {
/* 797 */       if (this.resources == null) readProperties(); 
/* 798 */       if (this.resources == null) return null; 
/*     */       try {
/* 800 */         return this.resources.getString("catalog-class-name");
/* 801 */       } catch (MissingResourceException e) {
/* 802 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 806 */     return className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCatalogClassName() {
/* 813 */     if (this.catalogClassName == null) {
/* 814 */       this.catalogClassName = queryCatalogClassName();
/*     */     }
/*     */     
/* 817 */     return this.catalogClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalogClassName(String className) {
/* 824 */     this.catalogClassName = className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String catalogClassName() {
/* 833 */     return getCatalogClassName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\CatalogManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */