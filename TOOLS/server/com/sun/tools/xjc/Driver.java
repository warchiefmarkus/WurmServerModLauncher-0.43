/*     */ package com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.codemodel.CodeWriter;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.writer.ZipCodeWriter;
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.generator.bean.BeanGenerator;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.reader.gbind.Expression;
/*     */ import com.sun.tools.xjc.reader.gbind.Graph;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ExpressionBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.tools.xjc.util.NullStream;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import com.sun.tools.xjc.writer.SignatureWriter;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import org.xml.sax.SAXException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Driver
/*     */ {
/*     */   public static void main(final String[] args) throws Exception {
/*     */     try {
/*  80 */       System.setProperty("java.net.useSystemProxies", "true");
/*  81 */     } catch (SecurityException e) {}
/*     */ 
/*     */ 
/*     */     
/*  85 */     if (Util.getSystemProperty(Driver.class, "noThreadSwap") != null) {
/*  86 */       _main(args);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  91 */     final Throwable[] ex = new Throwable[1];
/*     */     
/*  93 */     Thread th = new Thread() {
/*     */         public void run() {
/*     */           try {
/*  96 */             Driver._main(args);
/*  97 */           } catch (Throwable e) {
/*  98 */             ex[0] = e;
/*     */           } 
/*     */         }
/*     */       };
/* 102 */     th.start();
/* 103 */     th.join();
/*     */     
/* 105 */     if (ex[0] != null) {
/*     */       
/* 107 */       if (ex[0] instanceof Exception) {
/* 108 */         throw (Exception)ex[0];
/*     */       }
/* 110 */       throw (Error)ex[0];
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void _main(String[] args) throws Exception {
/*     */     try {
/* 116 */       System.exit(run(args, System.err, System.out));
/* 117 */     } catch (BadCommandLineException e) {
/*     */ 
/*     */       
/* 120 */       if (e.getMessage() != null) {
/* 121 */         System.out.println(e.getMessage());
/* 122 */         System.out.println();
/*     */       } 
/*     */       
/* 125 */       usage(e.getOptions(), false);
/* 126 */       System.exit(-1);
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
/*     */   public static int run(String[] args, final PrintStream status, final PrintStream out) throws Exception {
/*     */     class Listener
/*     */       extends XJCListener
/*     */     {
/*     */       ConsoleErrorReporter cer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       Listener() {
/* 164 */         this.cer = new ConsoleErrorReporter((out == null) ? new PrintStream((OutputStream)new NullStream()) : out);
/*     */       }
/*     */       public void generatedFile(String fileName, int count, int total) {
/* 167 */         message(fileName);
/*     */       }
/*     */       public void message(String msg) {
/* 170 */         if (status != null)
/* 171 */           status.println(msg); 
/*     */       }
/*     */       
/*     */       public void error(SAXParseException exception) {
/* 175 */         this.cer.error(exception);
/*     */       }
/*     */       
/*     */       public void fatalError(SAXParseException exception) {
/* 179 */         this.cer.fatalError(exception);
/*     */       }
/*     */       
/*     */       public void warning(SAXParseException exception) {
/* 183 */         this.cer.warning(exception);
/*     */       }
/*     */       
/*     */       public void info(SAXParseException exception) {
/* 187 */         this.cer.info(exception);
/*     */       }
/*     */     };
/*     */     
/* 191 */     return run(args, new Listener());
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
/*     */   public static int run(String[] args, @NotNull final XJCListener listener) throws BadCommandLineException {
/* 217 */     for (String arg : args) {
/* 218 */       if (arg.equals("-version")) {
/* 219 */         listener.message(Messages.format("Driver.Version", new Object[0]));
/* 220 */         return -1;
/*     */       } 
/*     */     } 
/*     */     
/* 224 */     final OptionsEx opt = new OptionsEx();
/* 225 */     opt.setSchemaLanguage(Language.XMLSCHEMA);
/*     */     try {
/* 227 */       opt.parseArguments(args);
/* 228 */     } catch (WeAreDone _) {
/* 229 */       return -1;
/* 230 */     } catch (BadCommandLineException e) {
/* 231 */       e.initOptions(opt);
/* 232 */       throw e;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (opt.defaultPackage != null && opt.defaultPackage.length() == 0) {
/* 238 */       listener.message(Messages.format("Driver.WarningMessage", new Object[] { Messages.format("Driver.DefaultPackageWarning", new Object[0]) }));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/* 245 */     Thread.currentThread().setContextClassLoader(opt.getUserClassLoader(contextClassLoader));
/*     */ 
/*     */     
/*     */     try {
/*     */       Outline outline;
/*     */       
/* 251 */       if (!opt.quiet) {
/* 252 */         listener.message(Messages.format("Driver.ParsingSchema", new Object[0]));
/*     */       }
/*     */       
/* 255 */       final boolean[] hadWarning = new boolean[1];
/*     */       
/* 257 */       ErrorReceiverFilter errorReceiverFilter = new ErrorReceiverFilter(listener) {
/*     */           public void info(SAXParseException exception) {
/* 259 */             if (opt.verbose)
/* 260 */               super.info(exception); 
/*     */           }
/*     */           public void warning(SAXParseException exception) {
/* 263 */             hadWarning[0] = true;
/* 264 */             if (!opt.quiet)
/* 265 */               super.warning(exception); 
/*     */           }
/*     */           
/*     */           public void pollAbort() throws AbortException {
/* 269 */             if (listener.isCanceled()) {
/* 270 */               throw new AbortException();
/*     */             }
/*     */           }
/*     */         };
/* 274 */       if (opt.mode == Mode.FOREST) {
/*     */         
/* 276 */         ModelLoader loader = new ModelLoader(opt, new JCodeModel(), (ErrorReceiver)errorReceiverFilter);
/*     */         try {
/* 278 */           DOMForest forest = loader.buildDOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/* 279 */           forest.dump(System.out);
/* 280 */           return 0;
/* 281 */         } catch (SAXException e) {
/*     */         
/* 283 */         } catch (IOException e) {
/* 284 */           errorReceiverFilter.error(e);
/*     */         } 
/*     */         
/* 287 */         return -1;
/*     */       } 
/*     */       
/* 290 */       if (opt.mode == Mode.GBIND) {
/*     */         try {
/* 292 */           XSSchemaSet xss = (new ModelLoader(opt, new JCodeModel(), (ErrorReceiver)errorReceiverFilter)).loadXMLSchema();
/* 293 */           Iterator<XSComplexType> it = xss.iterateComplexTypes();
/* 294 */           while (it.hasNext()) {
/* 295 */             XSComplexType ct = it.next();
/* 296 */             XSParticle p = ct.getContentType().asParticle();
/* 297 */             if (p == null)
/*     */               continue; 
/* 299 */             Expression tree = ExpressionBuilder.createTree(p);
/* 300 */             System.out.println("Graph for " + ct.getName());
/* 301 */             System.out.println(tree.toString());
/* 302 */             Graph g = new Graph(tree);
/* 303 */             System.out.println(g.toString());
/* 304 */             System.out.println();
/*     */           } 
/* 306 */           return 0;
/* 307 */         } catch (SAXException e) {
/*     */ 
/*     */           
/* 310 */           return -1;
/*     */         } 
/*     */       }
/* 313 */       Model model = ModelLoader.load(opt, new JCodeModel(), (ErrorReceiver)errorReceiverFilter);
/*     */       
/* 315 */       if (model == null) {
/* 316 */         listener.message(Messages.format("Driver.ParseFailed", new Object[0]));
/* 317 */         return -1;
/*     */       } 
/*     */       
/* 320 */       if (!opt.quiet) {
/* 321 */         listener.message(Messages.format("Driver.CompilingSchema", new Object[0]));
/*     */       }
/*     */       
/* 324 */       switch (opt.mode) {
/*     */         case SIGNATURE:
/*     */           try {
/* 327 */             SignatureWriter.write(BeanGenerator.generate(model, (ErrorReceiver)errorReceiverFilter), new OutputStreamWriter(System.out));
/*     */ 
/*     */             
/* 330 */             return 0;
/* 331 */           } catch (IOException e) {
/* 332 */             errorReceiverFilter.error(e);
/* 333 */             return -1;
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/*     */         case CODE:
/*     */         case DRYRUN:
/*     */         case ZIP:
/* 341 */           errorReceiverFilter.debug("generating code");
/*     */           
/* 343 */           outline = model.generateCode(opt, (ErrorReceiver)errorReceiverFilter);
/* 344 */           if (outline == null) {
/* 345 */             listener.message(Messages.format("Driver.FailedToGenerateCode", new Object[0]));
/*     */             
/* 347 */             return -1;
/*     */           } 
/*     */           
/* 350 */           listener.compiled(outline);
/*     */ 
/*     */           
/* 353 */           if (opt.mode == Mode.DRYRUN) {
/*     */             break;
/*     */           }
/*     */           try {
/*     */             CodeWriter cw;
/*     */             ProgressCodeWriter progressCodeWriter;
/* 359 */             if (opt.mode == Mode.ZIP) {
/*     */               OutputStream os;
/* 361 */               if (opt.targetDir.getPath().equals(".")) {
/* 362 */                 os = System.out;
/*     */               } else {
/* 364 */                 os = new FileOutputStream(opt.targetDir);
/*     */               } 
/* 366 */               cw = opt.createCodeWriter((CodeWriter)new ZipCodeWriter(os));
/*     */             } else {
/* 368 */               cw = opt.createCodeWriter();
/*     */             } 
/* 370 */             if (!opt.quiet) {
/* 371 */               progressCodeWriter = new ProgressCodeWriter(cw, listener, model.codeModel.countArtifacts());
/*     */             }
/* 373 */             model.codeModel.build((CodeWriter)progressCodeWriter);
/* 374 */           } catch (IOException e) {
/* 375 */             errorReceiverFilter.error(e);
/* 376 */             return -1;
/*     */           } 
/*     */           break;
/*     */         
/*     */         default:
/*     */           assert false;
/*     */           break;
/*     */       } 
/*     */       
/* 385 */       if (opt.debugMode) {
/*     */         try {
/* 387 */           (new FileOutputStream(new File(opt.targetDir, hadWarning[0] ? "hadWarning" : "noWarning"))).close();
/* 388 */         } catch (IOException e) {
/* 389 */           errorReceiverFilter.error(e);
/* 390 */           return -1;
/*     */         } 
/*     */       }
/*     */       
/* 394 */       return 0;
/* 395 */     } catch (StackOverflowError e) {
/* 396 */       if (opt.verbose)
/*     */       {
/*     */         
/* 399 */         throw e;
/*     */       }
/*     */ 
/*     */       
/* 403 */       listener.message(Messages.format("Driver.StackOverflow", new Object[0]));
/* 404 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getBuildID() {
/* 410 */     return Messages.format("Driver.BuildID", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum Mode
/*     */   {
/* 419 */     CODE,
/*     */ 
/*     */     
/* 422 */     SIGNATURE,
/*     */ 
/*     */     
/* 425 */     FOREST,
/*     */ 
/*     */     
/* 428 */     DRYRUN,
/*     */ 
/*     */     
/* 431 */     ZIP,
/*     */ 
/*     */     
/* 434 */     GBIND;
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
/*     */   static class OptionsEx
/*     */     extends Options
/*     */   {
/* 448 */     protected Driver.Mode mode = Driver.Mode.CODE;
/*     */ 
/*     */     
/*     */     public boolean noNS = false;
/*     */ 
/*     */     
/*     */     public int parseArgument(String[] args, int i) throws BadCommandLineException {
/* 455 */       if (args[i].equals("-noNS")) {
/* 456 */         this.noNS = true;
/* 457 */         return 1;
/*     */       } 
/* 459 */       if (args[i].equals("-mode")) {
/* 460 */         i++;
/* 461 */         if (i == args.length) {
/* 462 */           throw new BadCommandLineException(Messages.format("Driver.MissingModeOperand", new Object[0]));
/*     */         }
/*     */         
/* 465 */         String mstr = args[i].toLowerCase();
/*     */         
/* 467 */         for (Driver.Mode m : Driver.Mode.values()) {
/* 468 */           if (m.name().toLowerCase().startsWith(mstr) && mstr.length() > 2) {
/* 469 */             this.mode = m;
/* 470 */             return 2;
/*     */           } 
/*     */         } 
/*     */         
/* 474 */         throw new BadCommandLineException(Messages.format("Driver.UnrecognizedMode", new Object[] { args[i] }));
/*     */       } 
/*     */       
/* 477 */       if (args[i].equals("-help")) {
/* 478 */         Driver.usage(this, false);
/* 479 */         throw new Driver.WeAreDone();
/*     */       } 
/* 481 */       if (args[i].equals("-private")) {
/* 482 */         Driver.usage(this, true);
/* 483 */         throw new Driver.WeAreDone();
/*     */       } 
/*     */       
/* 486 */       return super.parseArgument(args, i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class WeAreDone
/*     */     extends BadCommandLineException
/*     */   {
/*     */     private WeAreDone() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void usage(@Nullable Options opts, boolean privateUsage) {
/* 504 */     if (privateUsage) {
/* 505 */       System.out.println(Messages.format("Driver.Private.Usage", new Object[0]));
/*     */     } else {
/* 507 */       System.out.println(Messages.format("Driver.Public.Usage", new Object[0]));
/*     */     } 
/*     */     
/* 510 */     if (opts != null && opts.getAllPlugins().size() != 0) {
/* 511 */       System.out.println(Messages.format("Driver.AddonUsage", new Object[0]));
/* 512 */       for (Plugin p : opts.getAllPlugins())
/* 513 */         System.out.println(p.getUsage()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\Driver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */