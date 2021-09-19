/*     */ package 1.0.com.sun.tools.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.reader.GrammarReader;
/*     */ import com.sun.msv.reader.GrammarReaderController;
/*     */ import com.sun.msv.reader.State;
/*     */ import com.sun.msv.reader.trex.ng.RELAXNGReader;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.reader.GrammarReaderControllerAdaptor;
/*     */ import com.sun.tools.xjc.reader.HierarchicalPackageTracker;
/*     */ import com.sun.tools.xjc.reader.PackageManager;
/*     */ import com.sun.tools.xjc.reader.PackageTracker;
/*     */ import com.sun.tools.xjc.reader.StackPackageManager;
/*     */ import com.sun.tools.xjc.reader.annotator.Annotator;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorControllerImpl;
/*     */ import com.sun.tools.xjc.reader.decorator.Decorator;
/*     */ import com.sun.tools.xjc.reader.decorator.RoleBasedDecorator;
/*     */ import com.sun.tools.xjc.reader.relaxng.DefaultDecorator;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TRELAXNGReader
/*     */   extends RELAXNGReader
/*     */ {
/*     */   protected final HierarchicalPackageTracker packageTracker;
/*     */   protected final StackPackageManager packageManager;
/*     */   private final RoleBasedDecorator decorator;
/*     */   protected final CodeModelClassFactory classFactory;
/*     */   private final AnnotatorController annController;
/*     */   protected final AnnotatedGrammar annGrammar;
/*     */   
/*     */   public TRELAXNGReader(ErrorReceiver errorReceiver, EntityResolver entityResolver, SAXParserFactory parserFactory, String defaultPackage) {
/*  43 */     this(new GrammarReaderControllerAdaptor(errorReceiver, entityResolver), parserFactory, defaultPackage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TRELAXNGReader(GrammarReaderControllerAdaptor _controller, SAXParserFactory parserFactory, String defaultPackage) {
/*  52 */     super((GrammarReaderController)_controller, parserFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.packageTracker = new HierarchicalPackageTracker();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     this.annGrammar = new AnnotatedGrammar(this.pool); if (defaultPackage == null)
/*     */       defaultPackage = "generated";  this.packageManager = new StackPackageManager(this.annGrammar.codeModel._package(defaultPackage)); this.classFactory = new CodeModelClassFactory((ErrorReceiver)_controller); this.annController = (AnnotatorController)new AnnotatorControllerImpl((GrammarReader)this, (ErrorReceiver)_controller, (PackageTracker)this.packageTracker);
/*  91 */     this.decorator = new RoleBasedDecorator((GrammarReader)this, (ErrorReceiver)_controller, this.annGrammar, this.annController.getNameConverter(), (PackageManager)this.packageManager, (Decorator)new DefaultDecorator(this, this.annController.getNameConverter())); } public AnnotatedGrammar getAnnotatedResult() { return this.annGrammar; }
/*     */ 
/*     */   
/*     */   protected Expression interceptExpression(State state, Expression exp) {
/*  95 */     exp = super.interceptExpression(state, exp);
/*     */ 
/*     */     
/*  98 */     if (this.controller.hadError()) {
/*  99 */       return exp;
/*     */     }
/* 101 */     if (exp == null)
/*     */     {
/* 103 */       return exp;
/*     */     }
/* 105 */     exp = this.decorator.decorate(state, exp);
/*     */     
/* 107 */     this.packageTracker.associate(exp, this.packageManager.getCurrentPackage());
/*     */     
/* 109 */     return exp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void wrapUp() {
/* 114 */     super.wrapUp();
/*     */ 
/*     */     
/* 117 */     if (this.controller.hadError()) {
/*     */       return;
/*     */     }
/*     */     
/* 121 */     this.packageTracker.associate((Expression)this.annGrammar, this.packageManager.getCurrentPackage());
/*     */ 
/*     */     
/* 124 */     this.annGrammar.exp = this.grammar.exp;
/* 125 */     Annotator.annotate(this.annGrammar, this.annController);
/* 126 */     this.grammar.exp = this.annGrammar.exp;
/*     */   }
/*     */   
/*     */   public void startElement(String a, String b, String c, Attributes atts) throws SAXException {
/* 130 */     this.packageManager.startElement(atts);
/* 131 */     super.startElement(a, b, c, atts);
/*     */   }
/*     */   public void endElement(String a, String b, String c) throws SAXException {
/* 134 */     super.endElement(a, b, c);
/* 135 */     this.packageManager.endElement();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String localizeMessage(String propertyName, Object[] args) {
/*     */     String format;
/*     */     try {
/* 142 */       format = ResourceBundle.getBundle("com.sun.tools.xjc.reader.relaxng.Messages").getString(propertyName);
/* 143 */     } catch (Exception e) {
/*     */       try {
/* 145 */         format = ResourceBundle.getBundle("com.sun.tools.xjc.reader.Messages").getString(propertyName);
/* 146 */       } catch (Exception ee) {
/* 147 */         return super.localizeMessage(propertyName, args);
/*     */       } 
/*     */     } 
/* 150 */     return MessageFormat.format(format, args);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\relaxng\TRELAXNGReader.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */