/*     */ package 1.0.com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.tools.xjc.BadCommandLineException;
/*     */ import com.sun.tools.xjc.CodeAugmenter;
/*     */ import com.sun.tools.xjc.Driver;
/*     */ import com.sun.tools.xjc.Messages;
/*     */ import com.sun.tools.xjc.reader.Util;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import org.apache.xml.resolver.CatalogManager;
/*     */ import org.apache.xml.resolver.helpers.Debug;
/*     */ import org.apache.xml.resolver.tools.CatalogResolver;
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
/*     */ public class Options
/*     */ {
/*     */   public boolean debugMode;
/*     */   public boolean verbose;
/*     */   public boolean quiet;
/*     */   public boolean traceUnmarshaller;
/*     */   public boolean readOnly;
/*     */   public boolean generateValidationCode = true;
/*     */   public boolean generateMarshallingCode = true;
/*     */   public boolean generateUnmarshallingCode = true;
/*     */   public boolean generateValidatingUnmarshallingCode = true;
/*     */   public boolean strictCheck = true;
/*     */   public static final int STRICT = 1;
/*     */   public static final int EXTENSION = 2;
/*  90 */   public int compatibilityMode = 1;
/*     */ 
/*     */   
/*  93 */   public File targetDir = new File(".");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public EntityResolver entityResolver = null;
/*     */ 
/*     */   
/*     */   public static final int SCHEMA_DTD = 0;
/*     */   
/*     */   public static final int SCHEMA_XMLSCHEMA = 1;
/*     */   
/*     */   public static final int SCHEMA_RELAXNG = 2;
/*     */   
/*     */   public static final int SCHEMA_WSDL = 3;
/*     */   
/*     */   private static final int SCHEMA_AUTODETECT = -1;
/*     */   
/* 113 */   private int schemaLanguage = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public String defaultPackage = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private final List grammars = new ArrayList();
/*     */   
/* 126 */   private final List bindFiles = new ArrayList();
/*     */ 
/*     */   
/* 129 */   String proxyHost = null;
/* 130 */   String proxyPort = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean generateRuntime = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public String runtimePackage = null;
/*     */ 
/*     */ 
/*     */   
/* 148 */   public final List enabledModelAugmentors = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final Object[] codeAugmenters = findServices(CodeAugmenter.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSchemaLanguage() {
/* 161 */     if (this.schemaLanguage == -1)
/* 162 */       this.schemaLanguage = guessSchemaLanguage(); 
/* 163 */     return this.schemaLanguage;
/*     */   }
/*     */   public void setSchemaLanguage(int _schemaLanguage) {
/* 166 */     this.schemaLanguage = _schemaLanguage;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputSource[] getGrammars() {
/* 171 */     return (InputSource[])this.grammars.toArray((Object[])new InputSource[this.grammars.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGrammar(InputSource is) {
/* 178 */     this.grammars.add(absolutize(is));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource absolutize(InputSource is) {
/*     */     try {
/* 186 */       URL baseURL = (new File(".")).getCanonicalFile().toURL();
/* 187 */       is.setSystemId((new URL(baseURL, is.getSystemId())).toExternalForm());
/* 188 */     } catch (IOException e) {}
/*     */ 
/*     */     
/* 191 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputSource[] getBindFiles() {
/* 197 */     return (InputSource[])this.bindFiles.toArray((Object[])new InputSource[this.bindFiles.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBindFile(InputSource is) {
/* 204 */     this.bindFiles.add(absolutize(is));
/*     */   }
/*     */   
/* 207 */   public final List classpaths = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLClassLoader getUserClassLoader(ClassLoader parent) {
/* 213 */     return new URLClassLoader((URL[])this.classpaths.toArray((Object[])new URL[this.classpaths.size()]), parent);
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
/*     */   protected int parseArgument(String[] args, int i) throws BadCommandLineException, IOException {
/* 229 */     if (args[i].equals("-classpath") || args[i].equals("-cp")) {
/* 230 */       if (i == args.length - 1) {
/* 231 */         throw new BadCommandLineException(Messages.format("Driver.MissingClassPath"));
/*     */       }
/* 233 */       this.classpaths.add((new File(args[++i])).toURL());
/* 234 */       return 2;
/*     */     } 
/* 236 */     if (args[i].equals("-d")) {
/* 237 */       if (i == args.length - 1) {
/* 238 */         throw new BadCommandLineException(Messages.format("Driver.MissingDir"));
/*     */       }
/* 240 */       this.targetDir = new File(args[++i]);
/* 241 */       if (!this.targetDir.exists()) {
/* 242 */         throw new BadCommandLineException(Messages.format("Driver.NonExistentDir", this.targetDir));
/*     */       }
/* 244 */       return 2;
/*     */     } 
/* 246 */     if (args[i].equals("-readOnly")) {
/* 247 */       this.readOnly = true;
/* 248 */       return 1;
/*     */     } 
/* 250 */     if (args[i].equals("-p")) {
/* 251 */       if (i == args.length - 1) {
/* 252 */         throw new BadCommandLineException(Messages.format("Driver.MissingPackageName"));
/*     */       }
/* 254 */       this.defaultPackage = args[++i];
/* 255 */       return 2;
/*     */     } 
/* 257 */     if (args[i].equals("-debug")) {
/* 258 */       this.debugMode = true;
/*     */       
/*     */       try {
/* 261 */         Debug.setDebug(10);
/* 262 */       } catch (Throwable _) {}
/*     */ 
/*     */       
/* 265 */       return 1;
/*     */     } 
/* 267 */     if (args[i].equals("-trace-unmarshaller")) {
/* 268 */       this.traceUnmarshaller = true;
/* 269 */       return 1;
/*     */     } 
/* 271 */     if (args[i].equals("-nv")) {
/* 272 */       this.strictCheck = false;
/* 273 */       return 1;
/*     */     } 
/* 275 */     if (args[i].equals("-use-runtime")) {
/* 276 */       if (i == args.length - 1) {
/* 277 */         throw new BadCommandLineException(Messages.format("Driver.MissingRuntimePackageName"));
/*     */       }
/* 279 */       this.generateRuntime = false;
/* 280 */       this.runtimePackage = args[++i];
/* 281 */       return 2;
/*     */     } 
/* 283 */     if (args[i].equals("-verbose")) {
/* 284 */       this.verbose = true;
/* 285 */       return 1;
/*     */     } 
/* 287 */     if (args[i].equals("-quiet")) {
/* 288 */       this.quiet = true;
/* 289 */       return 1;
/*     */     } 
/* 291 */     if (args[i].equals("-b")) {
/* 292 */       if (i == args.length - 1) {
/* 293 */         throw new BadCommandLineException(Messages.format("Driver.MissingFileName"));
/*     */       }
/* 295 */       if (args[i + 1].startsWith("-")) {
/* 296 */         throw new BadCommandLineException(Messages.format("Driver.MissingFileName"));
/*     */       }
/*     */       
/* 299 */       addBindFile(Util.getInputSource(args[++i]));
/* 300 */       return 2;
/*     */     } 
/* 302 */     if (args[i].equals("-dtd")) {
/* 303 */       this.schemaLanguage = 0;
/* 304 */       return 1;
/*     */     } 
/* 306 */     if (args[i].equals("-relaxng")) {
/* 307 */       this.schemaLanguage = 2;
/* 308 */       return 1;
/*     */     } 
/* 310 */     if (args[i].equals("-xmlschema")) {
/* 311 */       this.schemaLanguage = 1;
/* 312 */       return 1;
/*     */     } 
/* 314 */     if (args[i].equals("-wsdl")) {
/* 315 */       this.schemaLanguage = 3;
/* 316 */       return 1;
/*     */     } 
/* 318 */     if (args[i].equals("-extension")) {
/* 319 */       this.compatibilityMode = 2;
/* 320 */       return 1;
/*     */     } 
/* 322 */     if (args[i].equals("-host")) {
/* 323 */       if (i == args.length - 1) {
/* 324 */         throw new BadCommandLineException(Messages.format("Driver.MissingProxyHost"));
/*     */       }
/*     */       
/* 327 */       if (args[i + 1].startsWith("-")) {
/* 328 */         throw new BadCommandLineException(Messages.format("Driver.MissingProxyHost"));
/*     */       }
/*     */       
/* 331 */       this.proxyHost = args[++i];
/* 332 */       return 2;
/*     */     } 
/* 334 */     if (args[i].equals("-port")) {
/* 335 */       if (i == args.length - 1) {
/* 336 */         throw new BadCommandLineException(Messages.format("Driver.MissingProxyPort"));
/*     */       }
/*     */       
/* 339 */       if (args[i + 1].startsWith("-")) {
/* 340 */         throw new BadCommandLineException(Messages.format("Driver.MissingProxyPort"));
/*     */       }
/*     */       
/* 343 */       this.proxyPort = args[++i];
/* 344 */       return 2;
/*     */     } 
/* 346 */     if (args[i].equals("-catalog")) {
/*     */ 
/*     */ 
/*     */       
/* 350 */       if (i == args.length - 1) {
/* 351 */         throw new BadCommandLineException(Messages.format("Driver.MissingCatalog"));
/*     */       }
/*     */       
/* 354 */       addCatalog(new File(args[++i]));
/* 355 */       return 2;
/*     */     } 
/* 357 */     if (args[i].equals("-source")) {
/*     */ 
/*     */       
/* 360 */       if (i == args.length - 1) {
/* 361 */         return 1;
/*     */       }
/* 363 */       return 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     for (int j = 0; j < codeAugmenters.length; j++) {
/* 387 */       CodeAugmenter ma = (CodeAugmenter)codeAugmenters[j];
/* 388 */       if (("-" + ma.getOptionName()).equals(args[i])) {
/* 389 */         this.enabledModelAugmentors.add(ma);
/* 390 */         return 1;
/*     */       } 
/*     */       
/* 393 */       int r = ma.parseArgument(this, args, i);
/* 394 */       if (r != 0) return r;
/*     */     
/*     */     } 
/* 397 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCatalog(File catalogFile) throws IOException {
/* 404 */     if (this.entityResolver == null) {
/* 405 */       CatalogManager.ignoreMissingProperties(true);
/* 406 */       this.entityResolver = (EntityResolver)new CatalogResolver(true);
/*     */     } 
/* 408 */     ((CatalogResolver)this.entityResolver).getCatalog().parseCatalog(catalogFile.getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseArguments(String[] args) throws BadCommandLineException, IOException {
/* 419 */     for (int i = 0; i < args.length; i++) {
/* 420 */       if (args[i].charAt(0) == '-') {
/* 421 */         int j = parseArgument(args, i);
/* 422 */         if (j == 0) {
/* 423 */           throw new BadCommandLineException(Messages.format("Driver.UnrecognizedParameter", args[i]));
/*     */         }
/* 425 */         i += j - 1;
/*     */       } else {
/* 427 */         addGrammar(Util.getInputSource(args[i]));
/*     */       } 
/*     */     } 
/*     */     
/* 431 */     if (this.proxyHost != null || this.proxyPort != null) {
/* 432 */       if (this.proxyHost != null && this.proxyPort != null)
/* 433 */       { System.setProperty("http.proxyHost", this.proxyHost);
/* 434 */         System.setProperty("http.proxyPort", this.proxyPort);
/* 435 */         System.setProperty("https.proxyHost", this.proxyHost);
/* 436 */         System.setProperty("https.proxyPort", this.proxyPort); }
/* 437 */       else { if (this.proxyHost == null) {
/* 438 */           throw new BadCommandLineException(Messages.format("Driver.MissingProxyHost"));
/*     */         }
/*     */         
/* 441 */         throw new BadCommandLineException(Messages.format("Driver.MissingProxyPort")); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/* 446 */     if (this.grammars.size() == 0) {
/* 447 */       throw new BadCommandLineException(Messages.format("Driver.MissingGrammar"));
/*     */     }
/*     */     
/* 450 */     if (this.schemaLanguage == -1) {
/* 451 */       this.schemaLanguage = guessSchemaLanguage();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int guessSchemaLanguage() {
/* 459 */     if (this.grammars.size() > 1) {
/* 460 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 464 */     String name = ((InputSource)this.grammars.get(0)).getSystemId().toLowerCase();
/*     */     
/* 466 */     if (name.endsWith(".rng"))
/* 467 */       return 2; 
/* 468 */     if (name.endsWith(".dtd"))
/* 469 */       return 0; 
/* 470 */     if (name.endsWith(".wsdl")) {
/* 471 */       return 3;
/*     */     }
/*     */     
/* 474 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object[] findServices(String className) {
/* 482 */     return findServices(className, Driver.class.getClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object[] findServices(String className, ClassLoader classLoader) {
/* 492 */     boolean debug = (Util.getSystemProperty(com.sun.tools.xjc.Options.class, "findServices") != null);
/*     */     
/* 494 */     String serviceId = "META-INF/services/" + className;
/*     */     
/* 496 */     if (debug) {
/* 497 */       System.out.println("Looking for " + serviceId + " for add-ons");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 502 */       Enumeration e = classLoader.getResources(serviceId);
/* 503 */       if (e == null) return new Object[0];
/*     */       
/* 505 */       ArrayList a = new ArrayList();
/* 506 */       while (e.hasMoreElements()) {
/* 507 */         URL url = e.nextElement();
/* 508 */         BufferedReader reader = null;
/*     */         
/* 510 */         if (debug) {
/* 511 */           System.out.println("Checking " + url + " for an add-on");
/*     */         }
/*     */         
/*     */         try {
/* 515 */           reader = new BufferedReader(new InputStreamReader(url.openStream()));
/*     */           String impl;
/* 517 */           while ((impl = reader.readLine()) != null) {
/*     */             
/* 519 */             impl = impl.trim();
/* 520 */             if (debug) {
/* 521 */               System.out.println("Attempting to instanciate " + impl);
/*     */             }
/* 523 */             Class implClass = classLoader.loadClass(impl);
/* 524 */             a.add(implClass.newInstance());
/*     */           } 
/* 526 */           reader.close();
/* 527 */         } catch (Exception ex) {
/*     */           
/* 529 */           if (debug) {
/* 530 */             ex.printStackTrace(System.out);
/*     */           }
/* 532 */           if (reader != null) {
/*     */             try {
/* 534 */               reader.close();
/* 535 */             } catch (IOException ex2) {}
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 541 */       return a.toArray();
/* 542 */     } catch (Throwable e) {
/*     */       
/* 544 */       if (debug) {
/* 545 */         e.printStackTrace(System.out);
/*     */       }
/* 547 */       return new Object[0];
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\Options.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */