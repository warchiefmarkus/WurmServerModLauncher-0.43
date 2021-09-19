/*     */ package com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.codemodel.CodeWriter;
/*     */ import com.sun.codemodel.writer.FileCodeWriter;
/*     */ import com.sun.codemodel.writer.PrologCodeWriter;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
/*     */ import com.sun.tools.xjc.api.ClassNameAllocator;
/*     */ import com.sun.tools.xjc.api.SpecVersion;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.reader.Util;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Options
/*     */ {
/*     */   public boolean debugMode;
/*     */   public boolean verbose;
/*     */   public boolean quiet;
/*     */   public boolean readOnly;
/*     */   public boolean noFileHeader;
/*     */   public boolean strictCheck = true;
/*     */   public boolean runtime14 = false;
/*     */   public boolean automaticNameConflictResolution = false;
/*     */   public static final int STRICT = 1;
/*     */   public static final int EXTENSION = 2;
/* 137 */   public int compatibilityMode = 1;
/*     */   
/*     */   public boolean isExtensionMode() {
/* 140 */     return (this.compatibilityMode == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public SpecVersion target = SpecVersion.V2_1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public File targetDir = new File(".");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public EntityResolver entityResolver = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   private Language schemaLanguage = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public String defaultPackage = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   public String defaultPackage2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   private final List<InputSource> grammars = new ArrayList<InputSource>();
/*     */   
/* 188 */   private final List<InputSource> bindFiles = new ArrayList<InputSource>();
/*     */ 
/*     */   
/* 191 */   private String proxyHost = null;
/* 192 */   private String proxyPort = null;
/* 193 */   private String proxyUser = null;
/* 194 */   private String proxyPassword = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public final List<Plugin> activePlugins = new ArrayList<Plugin>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Plugin> allPlugins;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public final Set<String> pluginURIs = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassNameAllocator classNameAllocator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean packageLevelAnnotations = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   private FieldRendererFactory fieldRendererFactory = new FieldRendererFactory();
/*     */ 
/*     */ 
/*     */   
/* 231 */   private Plugin fieldRendererFactoryOwner = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   private NameConverter nameConverter = null;
/*     */ 
/*     */ 
/*     */   
/* 241 */   private Plugin nameConverterOwner = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldRendererFactory getFieldRendererFactory() {
/* 249 */     return this.fieldRendererFactory;
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
/*     */   public void setFieldRendererFactory(FieldRendererFactory frf, Plugin owner) throws BadCommandLineException {
/* 271 */     if (frf == null)
/* 272 */       throw new IllegalArgumentException(); 
/* 273 */     if (this.fieldRendererFactoryOwner != null) {
/* 274 */       throw new BadCommandLineException(Messages.format("FIELD_RENDERER_CONFLICT", new Object[] { this.fieldRendererFactoryOwner.getOptionName(), owner.getOptionName() }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 279 */     this.fieldRendererFactoryOwner = owner;
/* 280 */     this.fieldRendererFactory = frf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameConverter getNameConverter() {
/* 290 */     return this.nameConverter;
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
/*     */   public void setNameConverter(NameConverter nc, Plugin owner) throws BadCommandLineException {
/* 312 */     if (nc == null)
/* 313 */       throw new IllegalArgumentException(); 
/* 314 */     if (this.nameConverter != null) {
/* 315 */       throw new BadCommandLineException(Messages.format("NAME_CONVERTER_CONFLICT", new Object[] { this.nameConverterOwner.getOptionName(), owner.getOptionName() }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 320 */     this.nameConverterOwner = owner;
/* 321 */     this.nameConverter = nc;
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
/*     */   public List<Plugin> getAllPlugins() {
/* 333 */     if (this.allPlugins == null) {
/* 334 */       this.allPlugins = new ArrayList<Plugin>();
/* 335 */       ClassLoader ucl = getUserClassLoader(getClass().getClassLoader());
/* 336 */       for (Plugin aug : (Plugin[])findServices(Plugin.class, ucl)) {
/* 337 */         this.allPlugins.add(aug);
/*     */       }
/*     */     } 
/* 340 */     return this.allPlugins;
/*     */   }
/*     */   
/*     */   public Language getSchemaLanguage() {
/* 344 */     if (this.schemaLanguage == null)
/* 345 */       this.schemaLanguage = guessSchemaLanguage(); 
/* 346 */     return this.schemaLanguage;
/*     */   }
/*     */   public void setSchemaLanguage(Language _schemaLanguage) {
/* 349 */     this.schemaLanguage = _schemaLanguage;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputSource[] getGrammars() {
/* 354 */     return this.grammars.<InputSource>toArray(new InputSource[this.grammars.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGrammar(InputSource is) {
/* 361 */     this.grammars.add(absolutize(is));
/*     */   }
/*     */   
/*     */   private InputSource fileToInputSource(File source) {
/*     */     try {
/* 366 */       String url = source.toURL().toExternalForm();
/* 367 */       return new InputSource(Util.escapeSpace(url));
/* 368 */     } catch (MalformedURLException e) {
/* 369 */       return new InputSource(source.getPath());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addGrammar(File source) {
/* 374 */     addGrammar(fileToInputSource(source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGrammarRecursive(File dir) {
/* 381 */     addRecursive(dir, ".xsd", this.grammars);
/*     */   }
/*     */   
/*     */   private void addRecursive(File dir, String suffix, List<InputSource> result) {
/* 385 */     File[] files = dir.listFiles();
/* 386 */     if (files == null)
/*     */       return; 
/* 388 */     for (File f : files) {
/* 389 */       if (f.isDirectory()) {
/* 390 */         addRecursive(f, suffix, result);
/*     */       }
/* 392 */       else if (f.getPath().endsWith(suffix)) {
/* 393 */         result.add(absolutize(fileToInputSource(f)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource absolutize(InputSource is) {
/*     */     try {
/* 402 */       URL baseURL = (new File(".")).getCanonicalFile().toURL();
/* 403 */       is.setSystemId((new URL(baseURL, is.getSystemId())).toExternalForm());
/* 404 */     } catch (IOException e) {}
/*     */ 
/*     */     
/* 407 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource[] getBindFiles() {
/* 413 */     return this.bindFiles.<InputSource>toArray(new InputSource[this.bindFiles.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBindFile(InputSource is) {
/* 420 */     this.bindFiles.add(absolutize(is));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBindFile(File bindFile) {
/* 427 */     this.bindFiles.add(fileToInputSource(bindFile));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBindFileRecursive(File dir) {
/* 434 */     addRecursive(dir, ".xjb", this.bindFiles);
/*     */   }
/*     */   
/* 437 */   public final List<URL> classpaths = new ArrayList<URL>();
/*     */   
/*     */   private static String pluginLoadFailure;
/*     */ 
/*     */   
/*     */   public URLClassLoader getUserClassLoader(ClassLoader parent) {
/* 443 */     return new URLClassLoader(this.classpaths.<URL>toArray(new URL[this.classpaths.size()]), parent);
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
/*     */   public int parseArgument(String[] args, int i) throws BadCommandLineException {
/* 459 */     if (args[i].equals("-classpath") || args[i].equals("-cp")) {
/* 460 */       File file = new File(requireArgument(args[i], args, ++i));
/*     */       try {
/* 462 */         this.classpaths.add(file.toURL());
/* 463 */       } catch (MalformedURLException e) {
/* 464 */         throw new BadCommandLineException(Messages.format("Driver.NotAValidFileName", new Object[] { file }), e);
/*     */       } 
/*     */       
/* 467 */       return 2;
/*     */     } 
/* 469 */     if (args[i].equals("-d")) {
/* 470 */       this.targetDir = new File(requireArgument("-d", args, ++i));
/* 471 */       if (!this.targetDir.exists()) {
/* 472 */         throw new BadCommandLineException(Messages.format("Driver.NonExistentDir", new Object[] { this.targetDir }));
/*     */       }
/* 474 */       return 2;
/*     */     } 
/* 476 */     if (args[i].equals("-readOnly")) {
/* 477 */       this.readOnly = true;
/* 478 */       return 1;
/*     */     } 
/* 480 */     if (args[i].equals("-p")) {
/* 481 */       this.defaultPackage = requireArgument("-p", args, ++i);
/* 482 */       if (this.defaultPackage.length() == 0)
/*     */       {
/*     */         
/* 485 */         this.packageLevelAnnotations = false;
/*     */       }
/* 487 */       return 2;
/*     */     } 
/* 489 */     if (args[i].equals("-debug")) {
/* 490 */       this.debugMode = true;
/* 491 */       this.verbose = true;
/* 492 */       return 1;
/*     */     } 
/* 494 */     if (args[i].equals("-nv")) {
/* 495 */       this.strictCheck = false;
/* 496 */       return 1;
/*     */     } 
/* 498 */     if (args[i].equals("-npa")) {
/* 499 */       this.packageLevelAnnotations = false;
/* 500 */       return 1;
/*     */     } 
/* 502 */     if (args[i].equals("-no-header")) {
/* 503 */       this.noFileHeader = true;
/* 504 */       return 1;
/*     */     } 
/* 506 */     if (args[i].equals("-verbose")) {
/* 507 */       this.verbose = true;
/* 508 */       return 1;
/*     */     } 
/* 510 */     if (args[i].equals("-quiet")) {
/* 511 */       this.quiet = true;
/* 512 */       return 1;
/*     */     } 
/* 514 */     if (args[i].equals("-XexplicitAnnotation")) {
/* 515 */       this.runtime14 = true;
/* 516 */       return 1;
/*     */     } 
/* 518 */     if (args[i].equals("-XautoNameResolution")) {
/* 519 */       this.automaticNameConflictResolution = true;
/* 520 */       return 1;
/*     */     } 
/* 522 */     if (args[i].equals("-b")) {
/* 523 */       addFile(requireArgument("-b", args, ++i), this.bindFiles, ".xjb");
/* 524 */       return 2;
/*     */     } 
/* 526 */     if (args[i].equals("-dtd")) {
/* 527 */       this.schemaLanguage = Language.DTD;
/* 528 */       return 1;
/*     */     } 
/* 530 */     if (args[i].equals("-relaxng")) {
/* 531 */       this.schemaLanguage = Language.RELAXNG;
/* 532 */       return 1;
/*     */     } 
/* 534 */     if (args[i].equals("-relaxng-compact")) {
/* 535 */       this.schemaLanguage = Language.RELAXNG_COMPACT;
/* 536 */       return 1;
/*     */     } 
/* 538 */     if (args[i].equals("-xmlschema")) {
/* 539 */       this.schemaLanguage = Language.XMLSCHEMA;
/* 540 */       return 1;
/*     */     } 
/* 542 */     if (args[i].equals("-wsdl")) {
/* 543 */       this.schemaLanguage = Language.WSDL;
/* 544 */       return 1;
/*     */     } 
/* 546 */     if (args[i].equals("-extension")) {
/* 547 */       this.compatibilityMode = 2;
/* 548 */       return 1;
/*     */     } 
/* 550 */     if (args[i].equals("-target")) {
/* 551 */       String token = requireArgument("-target", args, ++i);
/* 552 */       this.target = SpecVersion.parse(token);
/* 553 */       if (this.target == null)
/* 554 */         throw new BadCommandLineException(Messages.format("Driver.ILLEGAL_TARGET_VERSION", new Object[] { token })); 
/* 555 */       return 2;
/*     */     } 
/* 557 */     if (args[i].equals("-httpproxyfile")) {
/* 558 */       if (i == args.length - 1 || args[i + 1].startsWith("-")) {
/* 559 */         throw new BadCommandLineException(Messages.format("Driver.MISSING_PROXYFILE", new Object[0]));
/*     */       }
/*     */ 
/*     */       
/* 563 */       File file = new File(args[++i]);
/* 564 */       if (!file.exists()) {
/* 565 */         throw new BadCommandLineException(Messages.format("Driver.NO_SUCH_FILE", new Object[] { file }));
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 570 */         BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/* 571 */         parseProxy(in.readLine());
/* 572 */         in.close();
/* 573 */       } catch (IOException e) {
/* 574 */         throw new BadCommandLineException(Messages.format("Driver.FailedToParse", new Object[] { file, e.getMessage() }), e);
/*     */       } 
/*     */ 
/*     */       
/* 578 */       return 2;
/*     */     } 
/* 580 */     if (args[i].equals("-httpproxy")) {
/* 581 */       if (i == args.length - 1 || args[i + 1].startsWith("-")) {
/* 582 */         throw new BadCommandLineException(Messages.format("Driver.MISSING_PROXY", new Object[0]));
/*     */       }
/*     */ 
/*     */       
/* 586 */       parseProxy(args[++i]);
/* 587 */       return 2;
/*     */     } 
/* 589 */     if (args[i].equals("-host")) {
/* 590 */       this.proxyHost = requireArgument("-host", args, ++i);
/* 591 */       return 2;
/*     */     } 
/* 593 */     if (args[i].equals("-port")) {
/* 594 */       this.proxyPort = requireArgument("-port", args, ++i);
/* 595 */       return 2;
/*     */     } 
/* 597 */     if (args[i].equals("-catalog")) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 602 */       File catalogFile = new File(requireArgument("-catalog", args, ++i));
/*     */       try {
/* 604 */         addCatalog(catalogFile);
/* 605 */       } catch (IOException e) {
/* 606 */         throw new BadCommandLineException(Messages.format("Driver.FailedToParse", new Object[] { catalogFile, e.getMessage() }), e);
/*     */       } 
/*     */       
/* 609 */       return 2;
/*     */     } 
/* 611 */     if (args[i].equals("-source")) {
/* 612 */       String version = requireArgument("-source", args, ++i);
/*     */ 
/*     */ 
/*     */       
/* 616 */       if (!version.equals("2.0") && !version.equals("2.1")) {
/* 617 */         throw new BadCommandLineException(Messages.format("Driver.DefaultVersion", new Object[0]));
/*     */       }
/* 619 */       return 2;
/*     */     } 
/* 621 */     if (args[i].equals("-Xtest-class-name-allocator")) {
/* 622 */       this.classNameAllocator = new ClassNameAllocator() {
/*     */           public String assignClassName(String packageName, String className) {
/* 624 */             System.out.printf("assignClassName(%s,%s)\n", new Object[] { packageName, className });
/* 625 */             return className + "_Type";
/*     */           }
/*     */         };
/* 628 */       return 1;
/*     */     } 
/*     */ 
/*     */     
/* 632 */     for (Plugin plugin : getAllPlugins()) {
/*     */       try {
/* 634 */         if (('-' + plugin.getOptionName()).equals(args[i])) {
/* 635 */           this.activePlugins.add(plugin);
/* 636 */           plugin.onActivated(this);
/* 637 */           this.pluginURIs.addAll(plugin.getCustomizationURIs());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 643 */           int j = plugin.parseArgument(this, args, i);
/* 644 */           if (j != 0) {
/* 645 */             return j;
/*     */           }
/* 647 */           return 1;
/*     */         } 
/*     */         
/* 650 */         int r = plugin.parseArgument(this, args, i);
/* 651 */         if (r != 0) return r; 
/* 652 */       } catch (IOException e) {
/* 653 */         throw new BadCommandLineException(e.getMessage(), e);
/*     */       } 
/*     */     } 
/*     */     
/* 657 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseProxy(String text) throws BadCommandLineException {
/* 662 */     String token = "([^@:]+)";
/* 663 */     Pattern p = Pattern.compile("(?:" + token + "(?:\\:" + token + ")?\\@)?" + token + "(?:\\:" + token + ")");
/*     */     
/* 665 */     Matcher matcher = p.matcher(text);
/* 666 */     if (!matcher.matches()) {
/* 667 */       throw new BadCommandLineException(Messages.format("Driver.ILLEGAL_PROXY", new Object[] { text }));
/*     */     }
/* 669 */     this.proxyUser = matcher.group(1);
/* 670 */     this.proxyPassword = matcher.group(2);
/* 671 */     this.proxyHost = matcher.group(3);
/* 672 */     this.proxyPort = matcher.group(4);
/*     */     try {
/* 674 */       Integer.valueOf(this.proxyPort);
/* 675 */     } catch (NumberFormatException e) {
/* 676 */       throw new BadCommandLineException(Messages.format("Driver.ILLEGAL_PROXY", new Object[] { text }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String requireArgument(String optionName, String[] args, int i) throws BadCommandLineException {
/* 684 */     if (i == args.length || args[i].startsWith("-")) {
/* 685 */       throw new BadCommandLineException(Messages.format("Driver.MissingOperand", new Object[] { optionName }));
/*     */     }
/*     */     
/* 688 */     return args[i];
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
/*     */   private void addFile(String name, List<InputSource> target, String suffix) throws BadCommandLineException {
/*     */     Object src;
/*     */     try {
/* 702 */       src = Util.getFileOrURL(name);
/* 703 */     } catch (IOException e) {
/* 704 */       throw new BadCommandLineException(Messages.format("Driver.NotAFileNorURL", new Object[] { name }));
/*     */     } 
/*     */     
/* 707 */     if (src instanceof URL) {
/* 708 */       target.add(absolutize(new InputSource(Util.escapeSpace(((URL)src).toExternalForm()))));
/*     */     } else {
/* 710 */       File fsrc = (File)src;
/* 711 */       if (fsrc.isDirectory()) {
/* 712 */         addRecursive(fsrc, suffix, target);
/*     */       } else {
/* 714 */         target.add(absolutize(fileToInputSource(fsrc)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCatalog(File catalogFile) throws IOException {
/* 723 */     if (this.entityResolver == null) {
/* 724 */       CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
/* 725 */       this.entityResolver = (EntityResolver)new CatalogResolver(true);
/*     */     } 
/* 727 */     ((CatalogResolver)this.entityResolver).getCatalog().parseCatalog(catalogFile.getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseArguments(String[] args) throws BadCommandLineException {
/* 738 */     for (int i = 0; i < args.length; i++) {
/* 739 */       if (args[i].length() == 0)
/* 740 */         throw new BadCommandLineException(); 
/* 741 */       if (args[i].charAt(0) == '-') {
/* 742 */         int j = parseArgument(args, i);
/* 743 */         if (j == 0) {
/* 744 */           throw new BadCommandLineException(Messages.format("Driver.UnrecognizedParameter", new Object[] { args[i] }));
/*     */         }
/* 746 */         i += j - 1;
/*     */       }
/* 748 */       else if (args[i].endsWith(".jar")) {
/* 749 */         scanEpisodeFile(new File(args[i]));
/*     */       } else {
/* 751 */         addFile(args[i], this.grammars, ".xsd");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 756 */     if (this.proxyHost != null || this.proxyPort != null) {
/* 757 */       if (this.proxyHost != null && this.proxyPort != null)
/* 758 */       { System.setProperty("http.proxyHost", this.proxyHost);
/* 759 */         System.setProperty("http.proxyPort", this.proxyPort);
/* 760 */         System.setProperty("https.proxyHost", this.proxyHost);
/* 761 */         System.setProperty("https.proxyPort", this.proxyPort); }
/* 762 */       else { if (this.proxyHost == null) {
/* 763 */           throw new BadCommandLineException(Messages.format("Driver.MissingProxyHost", new Object[0]));
/*     */         }
/*     */         
/* 766 */         throw new BadCommandLineException(Messages.format("Driver.MissingProxyPort", new Object[0])); }
/*     */ 
/*     */       
/* 769 */       if (this.proxyUser != null)
/* 770 */         System.setProperty("http.proxyUser", this.proxyUser); 
/* 771 */       if (this.proxyPassword != null) {
/* 772 */         System.setProperty("http.proxyPassword", this.proxyPassword);
/*     */       }
/*     */     } 
/*     */     
/* 776 */     if (this.grammars.size() == 0) {
/* 777 */       throw new BadCommandLineException(Messages.format("Driver.MissingGrammar", new Object[0]));
/*     */     }
/*     */     
/* 780 */     if (this.schemaLanguage == null) {
/* 781 */       this.schemaLanguage = guessSchemaLanguage();
/*     */     }
/* 783 */     if (pluginLoadFailure != null) {
/* 784 */       throw new BadCommandLineException(Messages.format("PLUGIN_LOAD_FAILURE", new Object[] { pluginLoadFailure }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void scanEpisodeFile(File jar) throws BadCommandLineException {
/*     */     try {
/* 793 */       URLClassLoader ucl = new URLClassLoader(new URL[] { jar.toURL() });
/* 794 */       Enumeration<URL> resources = ucl.findResources("META-INF/sun-jaxb.episode");
/* 795 */       while (resources.hasMoreElements()) {
/* 796 */         URL url = resources.nextElement();
/* 797 */         addBindFile(new InputSource(url.toExternalForm()));
/*     */       } 
/* 799 */     } catch (IOException e) {
/* 800 */       throw new BadCommandLineException(Messages.format("FAILED_TO_LOAD", new Object[] { jar, e.getMessage() }), e);
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
/*     */   public Language guessSchemaLanguage() {
/* 812 */     String name = ((InputSource)this.grammars.get(0)).getSystemId().toLowerCase();
/*     */     
/* 814 */     if (name.endsWith(".rng"))
/* 815 */       return Language.RELAXNG; 
/* 816 */     if (name.endsWith(".rnc"))
/* 817 */       return Language.RELAXNG_COMPACT; 
/* 818 */     if (name.endsWith(".dtd"))
/* 819 */       return Language.DTD; 
/* 820 */     if (name.endsWith(".wsdl")) {
/* 821 */       return Language.WSDL;
/*     */     }
/*     */     
/* 824 */     return Language.XMLSCHEMA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeWriter createCodeWriter() throws IOException {
/* 831 */     return createCodeWriter((CodeWriter)new FileCodeWriter(this.targetDir, this.readOnly));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeWriter createCodeWriter(CodeWriter core) {
/* 838 */     if (this.noFileHeader) {
/* 839 */       return core;
/*     */     }
/* 841 */     return (CodeWriter)new PrologCodeWriter(core, getPrologComment());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrologComment() {
/* 850 */     String format = Messages.format("Driver.DateFormat", new Object[0]) + " '" + Messages.format("Driver.At", new Object[0]) + "' " + Messages.format("Driver.TimeFormat", new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 856 */     SimpleDateFormat dateFormat = new SimpleDateFormat(format);
/*     */     
/* 858 */     return Messages.format("Driver.FilePrologComment", new Object[] { dateFormat.format(new Date()) });
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
/*     */   private static <T> T[] findServices(Class<T> clazz, ClassLoader classLoader) {
/* 874 */     boolean debug = (Util.getSystemProperty(Options.class, "findServices") != null);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 879 */       Class<?> serviceLoader = Class.forName("java.util.ServiceLoader");
/* 880 */       if (debug)
/* 881 */         System.out.println("Using java.util.ServiceLoader"); 
/* 882 */       Iterable<T> itr = (Iterable<T>)serviceLoader.getMethod("load", new Class[] { Class.class, ClassLoader.class }).invoke(null, new Object[] { clazz, classLoader });
/* 883 */       List<T> r = new ArrayList<T>();
/* 884 */       for (T t : itr)
/* 885 */         r.add(t); 
/* 886 */       return r.toArray((T[])Array.newInstance(clazz, r.size()));
/* 887 */     } catch (ClassNotFoundException e) {
/*     */     
/* 889 */     } catch (IllegalAccessException e) {
/* 890 */       Error x = new IllegalAccessError();
/* 891 */       x.initCause(e);
/* 892 */       throw x;
/* 893 */     } catch (InvocationTargetException e) {
/* 894 */       Throwable x = e.getTargetException();
/* 895 */       if (x instanceof RuntimeException)
/* 896 */         throw (RuntimeException)x; 
/* 897 */       if (x instanceof Error)
/* 898 */         throw (Error)x; 
/* 899 */       throw new Error(x);
/* 900 */     } catch (NoSuchMethodException e) {
/* 901 */       Error x = new NoSuchMethodError();
/* 902 */       x.initCause(e);
/* 903 */       throw x;
/*     */     } 
/*     */     
/* 906 */     String serviceId = "META-INF/services/" + clazz.getName();
/*     */ 
/*     */     
/* 909 */     Set<String> classNames = new HashSet<String>();
/*     */     
/* 911 */     if (debug) {
/* 912 */       System.out.println("Looking for " + serviceId + " for add-ons");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 917 */       Enumeration<URL> e = classLoader.getResources(serviceId);
/* 918 */       if (e == null) return (T[])Array.newInstance(clazz, 0);
/*     */       
/* 920 */       ArrayList<T> a = new ArrayList<T>();
/* 921 */       while (e.hasMoreElements()) {
/* 922 */         URL url = e.nextElement();
/* 923 */         BufferedReader reader = null;
/*     */         
/* 925 */         if (debug) {
/* 926 */           System.out.println("Checking " + url + " for an add-on");
/*     */         }
/*     */         
/*     */         try {
/* 930 */           reader = new BufferedReader(new InputStreamReader(url.openStream()));
/*     */           String impl;
/* 932 */           while ((impl = reader.readLine()) != null) {
/*     */             
/* 934 */             impl = impl.trim();
/* 935 */             if (classNames.add(impl)) {
/* 936 */               Class<?> implClass = classLoader.loadClass(impl);
/* 937 */               if (!clazz.isAssignableFrom(implClass)) {
/* 938 */                 pluginLoadFailure = impl + " is not a subclass of " + clazz + ". Skipping";
/* 939 */                 if (debug)
/* 940 */                   System.out.println(pluginLoadFailure); 
/*     */                 continue;
/*     */               } 
/* 943 */               if (debug) {
/* 944 */                 System.out.println("Attempting to instanciate " + impl);
/*     */               }
/* 946 */               a.add(clazz.cast(implClass.newInstance()));
/*     */             } 
/*     */           } 
/* 949 */           reader.close();
/* 950 */         } catch (Exception ex) {
/*     */           
/* 952 */           StringWriter w = new StringWriter();
/* 953 */           ex.printStackTrace(new PrintWriter(w));
/* 954 */           pluginLoadFailure = w.toString();
/* 955 */           if (debug) {
/* 956 */             System.out.println(pluginLoadFailure);
/*     */           }
/* 958 */           if (reader != null) {
/*     */             try {
/* 960 */               reader.close();
/* 961 */             } catch (IOException ex2) {}
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 968 */       return a.toArray((T[])Array.newInstance(clazz, a.size()));
/* 969 */     } catch (Throwable e) {
/*     */       
/* 971 */       StringWriter w = new StringWriter();
/* 972 */       e.printStackTrace(new PrintWriter(w));
/* 973 */       pluginLoadFailure = w.toString();
/* 974 */       if (debug) {
/* 975 */         System.out.println(pluginLoadFailure);
/*     */       }
/* 977 */       return (T[])Array.newInstance(clazz, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getBuildID() {
/* 983 */     return Messages.format("Driver.BuildID", new Object[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\Options.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */