/*     */ package 1.0.com.sun.tools.xjc;
/*     */ import com.sun.codemodel.CodeWriter;
/*     */ import com.sun.codemodel.writer.FileCodeWriter;
/*     */ import com.sun.codemodel.writer.ProgressCodeWriter;
/*     */ import com.sun.codemodel.writer.PrologCodeWriter;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.tools.xjc.BadCommandLineException;
/*     */ import com.sun.tools.xjc.CodeAugmenter;
/*     */ import com.sun.tools.xjc.ConsoleErrorReporter;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.GrammarLoader;
/*     */ import com.sun.tools.xjc.Messages;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.SkeletonGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.UnmarshallerGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Automaton;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.util.AnnotationRemover;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.relaxng.RELAXNGInternalizationLogic;
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import com.sun.tools.xjc.writer.Writer;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Driver {
/*     */   private static final int MODE_BGM = 0;
/*     */   private static final int MODE_SIGNATURE = 1;
/*     */   private static final int MODE_SERIALIZE = 2;
/*     */   private static final int MODE_CODE = 3;
/*     */   private static final int MODE_AUTOMATA = 4;
/*     */   private static final int MODE_FOREST = 5;
/*     */   private static final int MODE_DRYRUN = 6;
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  49 */     if (Util.getSystemProperty(com.sun.tools.xjc.Driver.class, "noThreadSwap") != null) {
/*  50 */       _main(args);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  55 */     Throwable[] ex = new Throwable[1];
/*     */     
/*  57 */     Object object = new Object(args, ex);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     object.start();
/*  67 */     object.join();
/*     */     
/*  69 */     if (ex[0] != null) {
/*     */       
/*  71 */       if (ex[0] instanceof Exception) {
/*  72 */         throw (Exception)ex[0];
/*     */       }
/*  74 */       throw (Error)ex[0];
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void _main(String[] args) throws Exception {
/*     */     try {
/*  80 */       System.exit(run(args, System.err, System.out));
/*  81 */     } catch (BadCommandLineException e) {
/*     */ 
/*     */       
/*  84 */       System.out.println(e.getMessage());
/*  85 */       System.out.println();
/*     */       
/*  87 */       usage(false);
/*  88 */       System.exit(-1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int run(String[] args, PrintStream status, PrintStream out) throws Exception {
/* 127 */     if (status == null) {
/* 128 */       status = new PrintStream((OutputStream)new NullStream());
/*     */     }
/*     */     
/* 131 */     for (int i = 0; i < args.length; i++) {
/* 132 */       if (args[i].equals("-help")) {
/* 133 */         usage(false);
/* 134 */         return -1;
/*     */       } 
/* 136 */       if (args[i].equals("-version")) {
/* 137 */         status.println(Messages.format("Driver.Version"));
/* 138 */         return -1;
/*     */       } 
/* 140 */       if (args[i].equals("-private")) {
/* 141 */         usage(true);
/* 142 */         return -1;
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     OptionsEx opt = new OptionsEx();
/* 147 */     opt.setSchemaLanguage(1);
/* 148 */     opt.parseArguments(args);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/* 154 */     Thread.currentThread().setContextClassLoader(opt.getUserClassLoader(contextClassLoader)); try {
/*     */       AnnotatedGrammar grammar;
/*     */       ObjectOutputStream stream;
/*     */       GeneratorContext context;
/*     */       Automaton[] automata;
/*     */       int j;
/* 160 */       if (!opt.quiet) {
/* 161 */         status.println(Messages.format("Driver.ParsingSchema"));
/*     */       }
/*     */       
/* 164 */       ConsoleErrorReporter consoleErrorReporter = new ConsoleErrorReporter(out, !opt.debugMode, opt.quiet);
/*     */       
/* 166 */       if (opt.mode == 5) {
/*     */         
/* 168 */         GrammarLoader loader = new GrammarLoader((Options)opt, (ErrorReceiver)consoleErrorReporter);
/* 169 */         DOMForest forest = loader.buildDOMForest((opt.getSchemaLanguage() == 2) ? (InternalizationLogic)new RELAXNGInternalizationLogic() : (InternalizationLogic)new XMLSchemaInternalizationLogic());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 174 */         forest.dump(System.out);
/* 175 */         return 0;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 181 */         grammar = GrammarLoader.load((Options)opt, (ErrorReceiver)consoleErrorReporter);
/*     */         
/* 183 */         if (grammar == null) {
/* 184 */           out.println(Messages.format("Driver.ParseFailed"));
/* 185 */           return -1;
/*     */         } 
/* 187 */       } catch (SAXException e) {
/*     */         
/* 189 */         if (e.getException() != null)
/* 190 */           e.getException().printStackTrace(out); 
/* 191 */         throw e;
/*     */       } 
/*     */       
/* 194 */       if (!opt.quiet) {
/* 195 */         status.println(Messages.format("Driver.CompilingSchema"));
/*     */       }
/*     */       
/* 198 */       switch (opt.mode)
/*     */       
/*     */       { 
/*     */         case 0:
/* 202 */           Writer.writeToConsole(opt.noNS, false, (Grammar)grammar);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 265 */           return 0;case 1: SignatureWriter.write(grammar, new OutputStreamWriter(out)); return 0;case 2: stream = new ObjectOutputStream(out); stream.writeObject(AnnotationRemover.remove((Grammar)grammar)); stream.close(); return 0;case 3: case 6: context = generateCode(grammar, (Options)opt, (ErrorReceiver)consoleErrorReporter); if (context == null) { out.println(Messages.format("Driver.FailedToGenerateCode")); return -1; }  if (opt.mode != 6) { ProgressCodeWriter progressCodeWriter; CodeWriter cw = createCodeWriter(opt.targetDir, opt.readOnly); if (!opt.quiet) progressCodeWriter = new ProgressCodeWriter(cw, status);  grammar.codeModel.build((CodeWriter)progressCodeWriter); }  return 0;case 4: context = SkeletonGenerator.generate(grammar, (Options)opt, (ErrorReceiver)consoleErrorReporter); if (context == null) return -1;  automata = UnmarshallerGenerator.generate(grammar, context, (Options)opt); for (j = 0; j < automata.length; j++) AutomatonToGraphViz.convert(automata[j], new File(opt.targetDir, (automata[j].getOwner()).ref.name() + ".gif"));  return 0; }  throw new JAXBAssertionError();
/* 266 */     } catch (StackOverflowError e) {
/* 267 */       if (opt.debugMode)
/*     */       {
/*     */         
/* 270 */         throw e;
/*     */       }
/*     */ 
/*     */       
/* 274 */       out.println(Messages.format("Driver.StackOverflow"));
/* 275 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getBuildID() {
/* 281 */     return Messages.format("Driver.BuildID");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GeneratorContext generateCode(AnnotatedGrammar grammar, Options opt, ErrorReceiver errorReceiver) {
/* 375 */     errorReceiver.debug("generating code");
/*     */     
/* 377 */     ErrorReceiverFilter ehFilter = new ErrorReceiverFilter(errorReceiver);
/*     */     
/* 379 */     GeneratorContext context = SkeletonGenerator.generate(grammar, opt, (ErrorReceiver)ehFilter);
/*     */     
/* 381 */     if (context == null) return null;
/*     */     
/* 383 */     if (opt.generateUnmarshallingCode)
/* 384 */       UnmarshallerGenerator.generate(grammar, context, opt); 
/* 385 */     if (opt.generateValidationCode || opt.generateMarshallingCode)
/*     */     {
/*     */       
/* 388 */       MarshallerGenerator.generate(grammar, context, opt); } 
/* 389 */     if (opt.generateValidationCode) {
/* 390 */       ValidatorGenerator.generate(grammar, context, opt);
/*     */     }
/* 392 */     if (ehFilter.hadError()) {
/* 393 */       return null;
/*     */     }
/*     */     
/* 396 */     Iterator itr = opt.enabledModelAugmentors.iterator();
/* 397 */     while (itr.hasNext()) {
/* 398 */       CodeAugmenter ma = itr.next();
/* 399 */       ma.run(grammar, context, opt, (ErrorHandler)errorReceiver);
/*     */     } 
/*     */     
/* 402 */     return context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void usage(boolean privateUsage) {
/* 410 */     if (privateUsage) {
/* 411 */       System.out.println(Messages.format("Driver.Private.Usage"));
/*     */     } else {
/* 413 */       System.out.println(Messages.format("Driver.Public.Usage"));
/*     */     } 
/*     */     
/* 416 */     if (Options.codeAugmenters.length != 0) {
/* 417 */       System.out.println(Messages.format("Driver.AddonUsage"));
/* 418 */       for (int i = 0; i < Options.codeAugmenters.length; i++) {
/* 419 */         System.out.println(((CodeAugmenter)Options.codeAugmenters[i]).getUsage());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CodeWriter createCodeWriter(File targetDir, boolean readonly) throws IOException {
/* 429 */     return createCodeWriter((CodeWriter)new FileCodeWriter(targetDir, readonly));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CodeWriter createCodeWriter(CodeWriter core) throws IOException {
/* 438 */     String format = Messages.format("Driver.DateFormat") + " '" + Messages.format("Driver.At") + "' " + Messages.format("Driver.TimeFormat");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 444 */     SimpleDateFormat dateFormat = new SimpleDateFormat(format);
/*     */     
/* 446 */     return (CodeWriter)new PrologCodeWriter(core, Messages.format("Driver.FilePrologComment", dateFormat.format(new Date())));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\Driver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */