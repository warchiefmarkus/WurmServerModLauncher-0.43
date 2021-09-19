/*     */ package 1.0.com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.codemodel.CodeWriter;
/*     */ import com.sun.tools.xjc.BadCommandLineException;
/*     */ import com.sun.tools.xjc.Driver;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.GrammarLoader;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class XJCTask
/*     */   extends Task
/*     */ {
/*     */   private final Options options;
/*     */   private long stackSize;
/*     */   private boolean removeOldOutput;
/*     */   private final ArrayList dependsSet;
/*     */   private final ArrayList producesSet;
/*     */   private boolean producesSpecified;
/*     */   private final Path classpath;
/*     */   private final Commandline cmdLine;
/*     */   
/*     */   public XJCTask() {
/*  49 */     this.options = new Options();
/*     */ 
/*     */     
/*  52 */     this.stackSize = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     this.removeOldOutput = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     this.dependsSet = new ArrayList();
/*  64 */     this.producesSet = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.producesSpecified = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     this.cmdLine = new Commandline();
/*     */     this.classpath = new Path(this.project);
/*     */     this.options.setSchemaLanguage(1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(File schema) {
/*  86 */     this.options.addGrammar(getInputSource(schema));
/*  87 */     this.dependsSet.add(schema);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredSchema(FileSet fs) {
/*  92 */     InputSource[] iss = toInputSources(fs);
/*  93 */     for (int i = 0; i < iss.length; i++) {
/*  94 */       this.options.addGrammar(iss[i]);
/*     */     }
/*  96 */     addIndividualFilesTo(fs, this.dependsSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClasspath(Path cp) {
/* 101 */     this.classpath.createPath().append(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 106 */     return this.classpath.createPath();
/*     */   }
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 110 */     this.classpath.createPath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinding(File binding) {
/* 117 */     this.options.addBindFile(getInputSource(binding));
/* 118 */     this.dependsSet.add(binding);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredBinding(FileSet fs) {
/* 123 */     InputSource[] iss = toInputSources(fs);
/* 124 */     for (int i = 0; i < iss.length; i++) {
/* 125 */       this.options.addBindFile(iss[i]);
/*     */     }
/* 127 */     addIndividualFilesTo(fs, this.dependsSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackage(String pkg) {
/* 135 */     this.options.defaultPackage = pkg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalog(File catalog) {
/*     */     try {
/* 143 */       this.options.addCatalog(catalog);
/* 144 */     } catch (IOException e) {
/* 145 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStackSize(String ss) {
/*     */     try {
/* 154 */       this.stackSize = Long.parseLong(ss);
/*     */       return;
/* 156 */     } catch (NumberFormatException e) {
/*     */ 
/*     */ 
/*     */       
/* 160 */       if (ss.length() > 2) {
/* 161 */         String head = ss.substring(0, ss.length() - 2);
/* 162 */         String tail = ss.substring(ss.length() - 2);
/*     */         
/* 164 */         if (tail.equalsIgnoreCase("kb")) {
/*     */           try {
/* 166 */             this.stackSize = Long.parseLong(head) * 1024L;
/*     */             return;
/* 168 */           } catch (NumberFormatException ee) {}
/*     */         }
/*     */ 
/*     */         
/* 172 */         if (tail.equalsIgnoreCase("mb")) {
/*     */           try {
/* 174 */             this.stackSize = Long.parseLong(head) * 1024L * 1024L;
/*     */             return;
/* 176 */           } catch (NumberFormatException ee) {}
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 182 */       throw new BuildException("Unrecognizable stack size: " + ss);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadonly(boolean flg) {
/* 189 */     this.options.readOnly = flg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtension(boolean flg) {
/* 197 */     if (flg) {
/* 198 */       this.options.compatibilityMode = 2;
/*     */     } else {
/* 200 */       this.options.compatibilityMode = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(File dir) {
/* 207 */     this.options.targetDir = dir;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredDepends(FileSet fs) {
/* 212 */     addIndividualFilesTo(fs, this.dependsSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConfiguredProduces(FileSet fs) {
/* 217 */     this.producesSpecified = true;
/* 218 */     if (!fs.getDir(this.project).exists()) {
/* 219 */       log(fs.getDir(this.project).getAbsolutePath() + " is not found and thus excluded from the dependency check", 2);
/*     */     }
/*     */     else {
/*     */       
/* 223 */       addIndividualFilesTo(fs, this.producesSet);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRemoveOldOutput(boolean roo) {
/* 228 */     this.removeOldOutput = roo;
/*     */   }
/*     */   
/*     */   public Commandline.Argument createArg() {
/* 232 */     return this.cmdLine.createArgument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 241 */     log("build id of XJC is " + Driver.getBuildID(), 3);
/*     */     
/*     */     try {
/* 244 */       if (this.stackSize == -1L) {
/* 245 */         doXJC();
/*     */       } else {
/*     */         try {
/*     */           Thread t;
/* 249 */           Throwable[] e = new Throwable[1];
/*     */ 
/*     */           
/* 252 */           Object object = new Object(this, e);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 264 */             Constructor c = Thread.class.getConstructor(new Class[] { ThreadGroup.class, Runnable.class, String.class, long.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 270 */             t = c.newInstance(new Object[] { Thread.currentThread().getThreadGroup(), object, Thread.currentThread().getName() + ":XJC", new Long(this.stackSize) });
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 276 */           catch (Throwable err) {
/*     */             
/* 278 */             log("Unable to set the stack size. Use JDK1.4 or above", 1);
/* 279 */             doXJC();
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 284 */           t.start();
/* 285 */           t.join();
/* 286 */           if (e[0] instanceof Error) throw (Error)e[0]; 
/* 287 */           if (e[0] instanceof RuntimeException) throw (RuntimeException)e[0]; 
/* 288 */           if (e[0] instanceof BuildException) throw (BuildException)e[0]; 
/* 289 */           if (e[0] != null) throw new BuildException(e[0]); 
/* 290 */         } catch (InterruptedException e) {
/* 291 */           throw new BuildException(e);
/*     */         } 
/*     */       } 
/* 294 */     } catch (BuildException e) {
/* 295 */       log("failure in the XJC task. Use the Ant -verbose switch for more details");
/* 296 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doXJC() throws BuildException {
/* 301 */     ClassLoader old = Thread.currentThread().getContextClassLoader();
/*     */     
/*     */     try {
/* 304 */       Thread.currentThread().setContextClassLoader((ClassLoader)new AntClassLoader(this.project, this.classpath));
/* 305 */       _doXJC();
/*     */     } finally {
/*     */       
/* 308 */       Thread.currentThread().setContextClassLoader(old);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void _doXJC() throws BuildException {
/*     */     try {
/* 315 */       this.options.parseArguments(this.cmdLine.getArguments());
/* 316 */     } catch (BadCommandLineException e) {
/* 317 */       throw new BuildException(e.getMessage(), e);
/* 318 */     } catch (IOException e) {
/* 319 */       throw new BuildException(e);
/*     */     } 
/*     */     
/* 322 */     if (!this.producesSpecified) {
/* 323 */       log("Consider using <depends>/<produces> so that XJC won't do unnecessary compilation", 2);
/*     */     }
/*     */ 
/*     */     
/* 327 */     long srcTime = computeTimestampFor(this.dependsSet, true);
/* 328 */     long dstTime = computeTimestampFor(this.producesSet, false);
/* 329 */     log("the last modified time of ths inputs is  " + srcTime, 3);
/* 330 */     log("the last modified time of the outputs is " + dstTime, 3);
/*     */     
/* 332 */     if (srcTime < dstTime) {
/* 333 */       log("files are up to date");
/*     */       
/*     */       return;
/*     */     } 
/* 337 */     InputSource[] grammars = this.options.getGrammars();
/*     */     
/* 339 */     String msg = "Compiling " + grammars[0].getSystemId();
/* 340 */     if (grammars.length > 1) msg = msg + " and others"; 
/* 341 */     log(msg, 2);
/*     */     
/* 343 */     if (this.removeOldOutput) {
/* 344 */       log("removing old output files", 2);
/* 345 */       for (Iterator itr = this.producesSet.iterator(); itr.hasNext(); ) {
/* 346 */         File f = itr.next();
/* 347 */         f.delete();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 352 */     ErrorReceiverImpl errorReceiverImpl = new ErrorReceiverImpl(this, null);
/*     */     
/* 354 */     AnnotatedGrammar grammar = null;
/*     */     try {
/* 356 */       grammar = GrammarLoader.load(this.options, (ErrorReceiver)errorReceiverImpl);
/*     */       
/* 358 */       if (grammar == null)
/* 359 */         throw new BuildException("unable to parse the schema. Error messages should have been provided"); 
/* 360 */     } catch (IOException e) {
/* 361 */       throw new BuildException("Unable to read files: " + e.getMessage(), e);
/* 362 */     } catch (SAXException e) {
/* 363 */       throw new BuildException("failed to compile a schema", e);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 368 */       if (Driver.generateCode(grammar, this.options, (ErrorReceiver)errorReceiverImpl) == null) {
/* 369 */         throw new BuildException("failed to compile a schema");
/*     */       }
/* 371 */       log("Writing output to " + this.options.targetDir, 2);
/*     */       
/* 373 */       grammar.codeModel.build((CodeWriter)new AngProgressCodeWriter(this, Driver.createCodeWriter(this.options.targetDir, this.options.readOnly)));
/*     */     }
/* 375 */     catch (IOException e) {
/* 376 */       throw new BuildException("unable to write files: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long computeTimestampFor(List files, boolean findNewest) {
/* 385 */     long lastModified = findNewest ? Long.MIN_VALUE : Long.MAX_VALUE;
/*     */     
/* 387 */     for (Iterator itr = files.iterator(); itr.hasNext(); ) {
/* 388 */       File file = itr.next();
/*     */       
/* 390 */       log("Checking timestamp of " + file.toString(), 3);
/*     */       
/* 392 */       if (findNewest) {
/* 393 */         lastModified = Math.max(lastModified, file.lastModified()); continue;
/*     */       } 
/* 395 */       lastModified = Math.min(lastModified, file.lastModified());
/*     */     } 
/*     */     
/* 398 */     if (lastModified == Long.MIN_VALUE) {
/* 399 */       return Long.MAX_VALUE;
/*     */     }
/* 401 */     if (lastModified == Long.MAX_VALUE) {
/* 402 */       return Long.MIN_VALUE;
/*     */     }
/* 404 */     return lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addIndividualFilesTo(FileSet fs, List lst) {
/* 412 */     DirectoryScanner ds = fs.getDirectoryScanner(this.project);
/* 413 */     String[] includedFiles = ds.getIncludedFiles();
/* 414 */     File baseDir = ds.getBasedir();
/*     */     
/* 416 */     for (int j = 0; j < includedFiles.length; j++) {
/* 417 */       lst.add(new File(baseDir, includedFiles[j]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource[] toInputSources(FileSet fs) {
/* 425 */     DirectoryScanner ds = fs.getDirectoryScanner(this.project);
/* 426 */     String[] includedFiles = ds.getIncludedFiles();
/* 427 */     File baseDir = ds.getBasedir();
/*     */     
/* 429 */     ArrayList lst = new ArrayList();
/*     */     
/* 431 */     for (int j = 0; j < includedFiles.length; j++) {
/* 432 */       lst.add(getInputSource(new File(baseDir, includedFiles[j])));
/*     */     }
/*     */     
/* 435 */     return lst.<InputSource>toArray(new InputSource[lst.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputSource getInputSource(File f) {
/*     */     try {
/* 443 */       return new InputSource(f.toURL().toExternalForm());
/* 444 */     } catch (MalformedURLException e) {
/* 445 */       return new InputSource(f.getPath());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\XJCTask.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */