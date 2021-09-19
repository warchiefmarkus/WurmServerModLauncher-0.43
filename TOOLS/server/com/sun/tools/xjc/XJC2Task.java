/*     */ package com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.codemodel.CodeWriter;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.writer.FilterCodeWriter;
/*     */ import com.sun.tools.xjc.api.SpecVersion;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.reader.Util;
/*     */ import com.sun.tools.xjc.util.ForkEntityResolver;
/*     */ import com.sun.xml.bind.v2.util.EditDistance;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.XMLCatalog;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XJC2Task
/*     */   extends Task
/*     */ {
/*     */   public final Options options;
/*     */   private long stackSize;
/*     */   private boolean failonerror;
/*     */   private boolean removeOldOutput;
/*     */   private final ArrayList<File> dependsSet;
/*     */   private final ArrayList<File> producesSet;
/*     */   private boolean producesSpecified;
/*     */   private final Path classpath;
/*     */   private final Commandline cmdLine;
/*     */   private XMLCatalog xmlCatalog;
/*     */   
/*     */   public XJC2Task() {
/*  87 */     this.options = new Options();
/*     */ 
/*     */     
/*  90 */     this.stackSize = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     this.failonerror = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     this.removeOldOutput = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     this.dependsSet = new ArrayList<File>();
/* 108 */     this.producesSet = new ArrayList<File>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     this.producesSpecified = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     this.cmdLine = new Commandline();
/*     */ 
/*     */     
/* 125 */     this.xmlCatalog = null;
/*     */     this.classpath = new Path(null);
/*     */     this.options.setSchemaLanguage(Language.XMLSCHEMA);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(String schema) {
/*     */     try {
/* 138 */       this.options.addGrammar(getInputSource(new URL(schema)));
/* 139 */     } catch (MalformedURLException e) {
/* 140 */       File f = getProject().resolveFile(schema);
/* 141 */       this.options.addGrammar(f);
/* 142 */       this.dependsSet.add(f);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredSchema(FileSet fs) {
/* 148 */     for (InputSource value : toInputSources(fs)) {
/* 149 */       this.options.addGrammar(value);
/*     */     }
/* 151 */     addIndividualFilesTo(fs, this.dependsSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClasspath(Path cp) {
/* 156 */     this.classpath.createPath().append(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 161 */     return this.classpath.createPath();
/*     */   }
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 165 */     this.classpath.createPath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/* 172 */     Language l = Language.valueOf(language.toUpperCase());
/* 173 */     if (l == null) {
/* 174 */       Language[] languages = Language.values();
/* 175 */       String[] candidates = new String[languages.length];
/* 176 */       for (int i = 0; i < candidates.length; i++) {
/* 177 */         candidates[i] = languages[i].name();
/*     */       }
/* 179 */       throw new BuildException("Unrecognized language: " + language + ". Did you mean " + EditDistance.findNearest(language.toUpperCase(), candidates) + " ?");
/*     */     } 
/*     */     
/* 182 */     this.options.setSchemaLanguage(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinding(String binding) {
/*     */     try {
/* 190 */       this.options.addBindFile(getInputSource(new URL(binding)));
/* 191 */     } catch (MalformedURLException e) {
/* 192 */       File f = getProject().resolveFile(binding);
/* 193 */       this.options.addBindFile(f);
/* 194 */       this.dependsSet.add(f);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredBinding(FileSet fs) {
/* 200 */     for (InputSource is : toInputSources(fs)) {
/* 201 */       this.options.addBindFile(is);
/*     */     }
/* 203 */     addIndividualFilesTo(fs, this.dependsSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackage(String pkg) {
/* 211 */     this.options.defaultPackage = pkg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalog(File catalog) {
/*     */     try {
/* 219 */       this.options.addCatalog(catalog);
/* 220 */     } catch (IOException e) {
/* 221 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailonerror(boolean value) {
/* 229 */     this.failonerror = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStackSize(String ss) {
/*     */     try {
/* 240 */       this.stackSize = Long.parseLong(ss);
/*     */       return;
/* 242 */     } catch (NumberFormatException e) {
/*     */ 
/*     */ 
/*     */       
/* 246 */       if (ss.length() > 2) {
/* 247 */         String head = ss.substring(0, ss.length() - 2);
/* 248 */         String tail = ss.substring(ss.length() - 2);
/*     */         
/* 250 */         if (tail.equalsIgnoreCase("kb")) {
/*     */           try {
/* 252 */             this.stackSize = Long.parseLong(head) * 1024L;
/*     */             return;
/* 254 */           } catch (NumberFormatException ee) {}
/*     */         }
/*     */ 
/*     */         
/* 258 */         if (tail.equalsIgnoreCase("mb")) {
/*     */           try {
/* 260 */             this.stackSize = Long.parseLong(head) * 1024L * 1024L;
/*     */             return;
/* 262 */           } catch (NumberFormatException ee) {}
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 268 */       throw new BuildException("Unrecognizable stack size: " + ss);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredXMLCatalog(XMLCatalog xmlCatalog) {
/* 277 */     if (this.xmlCatalog == null) {
/* 278 */       this.xmlCatalog = new XMLCatalog();
/* 279 */       this.xmlCatalog.setProject(getProject());
/*     */     } 
/* 281 */     this.xmlCatalog.addConfiguredXMLCatalog(xmlCatalog);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadonly(boolean flg) {
/* 288 */     this.options.readOnly = flg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeader(boolean flg) {
/* 295 */     this.options.noFileHeader = !flg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXexplicitAnnotation(boolean flg) {
/* 302 */     this.options.runtime14 = flg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtension(boolean flg) {
/* 310 */     if (flg) {
/* 311 */       this.options.compatibilityMode = 2;
/*     */     } else {
/* 313 */       this.options.compatibilityMode = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String version) {
/* 320 */     this.options.target = SpecVersion.parse(version);
/* 321 */     if (this.options.target == null) {
/* 322 */       throw new BuildException(version + " is not a valid version number. Perhaps you meant @destdir?");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestdir(File dir) {
/* 329 */     this.options.targetDir = dir;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredDepends(FileSet fs) {
/* 334 */     addIndividualFilesTo(fs, this.dependsSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredProduces(FileSet fs) {
/* 339 */     this.producesSpecified = true;
/* 340 */     if (!fs.getDir(getProject()).exists()) {
/* 341 */       log(fs.getDir(getProject()).getAbsolutePath() + " is not found and thus excluded from the dependency check", 2);
/*     */     }
/*     */     else {
/*     */       
/* 345 */       addIndividualFilesTo(fs, this.producesSet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRemoveOldOutput(boolean roo) {
/* 350 */     this.removeOldOutput = roo;
/*     */   }
/*     */   
/*     */   public Commandline.Argument createArg() {
/* 354 */     return this.cmdLine.createArgument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 363 */     log("build id of XJC is " + Driver.getBuildID(), 3);
/*     */     
/* 365 */     this.classpath.setProject(getProject());
/*     */     
/*     */     try {
/* 368 */       if (this.stackSize == -1L) {
/* 369 */         doXJC();
/*     */       } else {
/*     */         try {
/*     */           Thread t;
/* 373 */           final Throwable[] e = new Throwable[1];
/*     */ 
/*     */           
/* 376 */           Runnable job = new Runnable() {
/*     */               public void run() {
/*     */                 try {
/* 379 */                   XJC2Task.this.doXJC();
/* 380 */                 } catch (Throwable be) {
/* 381 */                   e[0] = be;
/*     */                 } 
/*     */               }
/*     */             };
/*     */ 
/*     */           
/*     */           try {
/* 388 */             Constructor<Thread> c = Thread.class.getConstructor(new Class[] { ThreadGroup.class, Runnable.class, String.class, long.class });
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 393 */             t = c.newInstance(new Object[] { Thread.currentThread().getThreadGroup(), job, Thread.currentThread().getName() + ":XJC", Long.valueOf(this.stackSize) });
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 398 */           catch (Throwable err) {
/*     */             
/* 400 */             log("Unable to set the stack size. Use JDK1.4 or above", 1);
/* 401 */             doXJC();
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 406 */           t.start();
/* 407 */           t.join();
/* 408 */           if (e[0] instanceof Error) throw (Error)e[0]; 
/* 409 */           if (e[0] instanceof RuntimeException) throw (RuntimeException)e[0]; 
/* 410 */           if (e[0] instanceof BuildException) throw (BuildException)e[0]; 
/* 411 */           if (e[0] != null) throw new BuildException(e[0]); 
/* 412 */         } catch (InterruptedException e) {
/* 413 */           throw new BuildException(e);
/*     */         } 
/*     */       } 
/* 416 */     } catch (BuildException e) {
/* 417 */       log("failure in the XJC task. Use the Ant -verbose switch for more details");
/* 418 */       if (this.failonerror) {
/* 419 */         throw e;
/*     */       }
/* 421 */       StringWriter sw = new StringWriter();
/* 422 */       e.printStackTrace(new PrintWriter(sw));
/* 423 */       getProject().log(sw.toString(), 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doXJC() throws BuildException {
/* 430 */     ClassLoader old = Thread.currentThread().getContextClassLoader();
/*     */     
/*     */     try {
/* 433 */       Thread.currentThread().setContextClassLoader((ClassLoader)new AntClassLoader(getProject(), this.classpath));
/* 434 */       _doXJC();
/*     */     } finally {
/*     */       
/* 437 */       Thread.currentThread().setContextClassLoader(old);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void _doXJC() throws BuildException {
/*     */     try {
/* 444 */       this.options.parseArguments(this.cmdLine.getArguments());
/* 445 */     } catch (BadCommandLineException e) {
/* 446 */       throw new BuildException(e.getMessage(), e);
/*     */     } 
/*     */     
/* 449 */     if (this.xmlCatalog != null) {
/* 450 */       if (this.options.entityResolver == null) {
/* 451 */         this.options.entityResolver = (EntityResolver)this.xmlCatalog;
/*     */       } else {
/* 453 */         this.options.entityResolver = (EntityResolver)new ForkEntityResolver(this.options.entityResolver, (EntityResolver)this.xmlCatalog);
/*     */       } 
/*     */     }
/*     */     
/* 457 */     if (!this.producesSpecified) {
/* 458 */       log("Consider using <depends>/<produces> so that XJC won't do unnecessary compilation", 2);
/*     */     }
/*     */ 
/*     */     
/* 462 */     long srcTime = computeTimestampFor(this.dependsSet, true);
/* 463 */     long dstTime = computeTimestampFor(this.producesSet, false);
/* 464 */     log("the last modified time of the inputs is  " + srcTime, 3);
/* 465 */     log("the last modified time of the outputs is " + dstTime, 3);
/*     */     
/* 467 */     if (srcTime < dstTime) {
/* 468 */       log("files are up to date");
/*     */       
/*     */       return;
/*     */     } 
/* 472 */     InputSource[] grammars = this.options.getGrammars();
/*     */     
/* 474 */     String msg = "Compiling " + grammars[0].getSystemId();
/* 475 */     if (grammars.length > 1) msg = msg + " and others"; 
/* 476 */     log(msg, 2);
/*     */     
/* 478 */     if (this.removeOldOutput) {
/* 479 */       log("removing old output files", 2);
/* 480 */       for (File f : this.producesSet) {
/* 481 */         f.delete();
/*     */       }
/*     */     } 
/*     */     
/* 485 */     ErrorReceiver errorReceiver = new ErrorReceiverImpl();
/*     */     
/* 487 */     Model model = ModelLoader.load(this.options, new JCodeModel(), errorReceiver);
/*     */     
/* 489 */     if (model == null) {
/* 490 */       throw new BuildException("unable to parse the schema. Error messages should have been provided");
/*     */     }
/*     */     
/*     */     try {
/* 494 */       if (model.generateCode(this.options, errorReceiver) == null) {
/* 495 */         throw new BuildException("failed to compile a schema");
/*     */       }
/* 497 */       log("Writing output to " + this.options.targetDir, 2);
/*     */       
/* 499 */       model.codeModel.build((CodeWriter)new AntProgressCodeWriter(this.options.createCodeWriter()));
/* 500 */     } catch (IOException e) {
/* 501 */       throw new BuildException("unable to write files: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long computeTimestampFor(List<File> files, boolean findNewest) {
/* 510 */     long lastModified = findNewest ? Long.MIN_VALUE : Long.MAX_VALUE;
/*     */     
/* 512 */     for (File file : files) {
/* 513 */       log("Checking timestamp of " + file.toString(), 3);
/*     */       
/* 515 */       if (findNewest) {
/* 516 */         lastModified = Math.max(lastModified, file.lastModified()); continue;
/*     */       } 
/* 518 */       lastModified = Math.min(lastModified, file.lastModified());
/*     */     } 
/*     */     
/* 521 */     if (lastModified == Long.MIN_VALUE) {
/* 522 */       return Long.MAX_VALUE;
/*     */     }
/* 524 */     if (lastModified == Long.MAX_VALUE) {
/* 525 */       return Long.MIN_VALUE;
/*     */     }
/* 527 */     return lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addIndividualFilesTo(FileSet fs, List<File> lst) {
/* 535 */     DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/* 536 */     String[] includedFiles = ds.getIncludedFiles();
/* 537 */     File baseDir = ds.getBasedir();
/*     */     
/* 539 */     for (String value : includedFiles) {
/* 540 */       lst.add(new File(baseDir, value));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource[] toInputSources(FileSet fs) {
/* 548 */     DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/* 549 */     String[] includedFiles = ds.getIncludedFiles();
/* 550 */     File baseDir = ds.getBasedir();
/*     */     
/* 552 */     ArrayList<InputSource> lst = new ArrayList<InputSource>();
/*     */     
/* 554 */     for (String value : includedFiles) {
/* 555 */       lst.add(getInputSource(new File(baseDir, value)));
/*     */     }
/*     */     
/* 558 */     return lst.<InputSource>toArray(new InputSource[lst.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource getInputSource(File f) {
/*     */     try {
/* 566 */       return new InputSource(f.toURL().toExternalForm());
/* 567 */     } catch (MalformedURLException e) {
/* 568 */       return new InputSource(f.getPath());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource getInputSource(URL url) {
/* 576 */     return Util.getInputSource(url.toExternalForm());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AntProgressCodeWriter
/*     */     extends FilterCodeWriter
/*     */   {
/*     */     public AntProgressCodeWriter(CodeWriter output) {
/* 585 */       super(output);
/*     */     }
/*     */     
/*     */     public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 589 */       if (pkg.isUnnamed()) {
/* 590 */         XJC2Task.this.log("generating " + fileName, 3);
/*     */       } else {
/* 592 */         XJC2Task.this.log("generating " + pkg.name().replace('.', File.separatorChar) + File.separatorChar + fileName, 3);
/*     */       } 
/*     */ 
/*     */       
/* 596 */       return super.openBinary(pkg, fileName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class ErrorReceiverImpl
/*     */     extends ErrorReceiver
/*     */   {
/*     */     private ErrorReceiverImpl() {}
/*     */     
/*     */     public void warning(SAXParseException e) {
/* 607 */       print(1, "Driver.WarningMessage", e);
/*     */     }
/*     */     
/*     */     public void error(SAXParseException e) {
/* 611 */       print(0, "Driver.ErrorMessage", e);
/*     */     }
/*     */     
/*     */     public void fatalError(SAXParseException e) {
/* 615 */       print(0, "Driver.ErrorMessage", e);
/*     */     }
/*     */     
/*     */     public void info(SAXParseException e) {
/* 619 */       print(3, "Driver.InfoMessage", e);
/*     */     }
/*     */     
/*     */     private void print(int logLevel, String header, SAXParseException e) {
/* 623 */       XJC2Task.this.log(Messages.format(header, new Object[] { e.getMessage() }), logLevel);
/* 624 */       XJC2Task.this.log(getLocationString(e), logLevel);
/* 625 */       XJC2Task.this.log("", logLevel);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\XJC2Task.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */