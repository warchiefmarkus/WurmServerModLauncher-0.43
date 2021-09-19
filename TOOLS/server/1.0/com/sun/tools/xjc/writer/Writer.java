/*     */ package 1.0.com.sun.tools.xjc.writer;
/*     */ 
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.NameClassVisitor;
/*     */ import com.sun.msv.writer.GrammarWriter;
/*     */ import com.sun.msv.writer.SAXRuntimeException;
/*     */ import com.sun.msv.writer.XMLWriter;
/*     */ import com.sun.msv.writer.relaxng.Context;
/*     */ import com.sun.msv.writer.relaxng.PatternWriter;
/*     */ import com.sun.org.apache.xml.internal.serialize.OutputFormat;
/*     */ import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.DocumentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Writer
/*     */   implements GrammarWriter, Context
/*     */ {
/*     */   private final boolean noNS;
/*     */   private final boolean signatureOnly;
/*     */   private final Set candidates;
/*     */   private final XMLWriter writer;
/*     */   private final PatternWriter patternWriter;
/*     */   
/*     */   public Writer(boolean _noNS, boolean _sigOnly) {
/* 100 */     this.candidates = new HashSet();
/*     */     
/* 102 */     this.writer = new XMLWriter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     this.patternWriter = (PatternWriter)new SmartWriter(this, this);
/*     */     this.noNS = _noNS;
/*     */     this.signatureOnly = _sigOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeToConsole(boolean noNS, Grammar grammar) {
/*     */     writeToConsole(noNS, false, grammar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeToConsole(boolean noNS, boolean signatureOnly, Grammar grammar) {
/*     */     try {
/*     */       com.sun.tools.xjc.writer.Writer w = new com.sun.tools.xjc.writer.Writer(noNS, signatureOnly);
/*     */       OutputFormat format = new OutputFormat("xml", null, true);
/*     */       format.setIndent(1);
/*     */       w.setDocumentHandler(new XMLSerializer(System.out, format));
/*     */       w.write(grammar);
/*     */     } catch (SAXException e) {
/*     */       throw new JAXBAssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLWriter getWriter() {
/*     */     return this.writer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTargetNamespace() {
/*     */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocumentHandler(DocumentHandler handler) {
/*     */     this.writer.setDocumentHandler(handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Grammar g) throws SAXException {
/*     */     write((AnnotatedGrammar)g);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(AnnotatedGrammar grammar) throws SAXException {
/*     */     try {
/*     */       DocumentHandler handler = this.writer.getDocumentHandler();
/*     */       handler.setDocumentLocator(new LocatorImpl());
/*     */       handler.startDocument();
/*     */       this.writer.start("bgm");
/*     */       this.writer.start("root");
/*     */       grammar.getTopLevel().visit((ExpressionVisitorVoid)this.patternWriter);
/*     */       this.writer.end("root");
/*     */       ClassItem[] cs = grammar.getClasses();
/*     */       for (int i = 0; i < cs.length; i++) {
/*     */         writeClass(cs[i]);
/*     */       }
/*     */       InterfaceItem[] is = grammar.getInterfaces();
/*     */       for (int j = 0; j < is.length; j++) {
/*     */         writeInterface(is[j]);
/*     */       }
/*     */       this.writer.end("bgm");
/*     */       handler.endDocument();
/*     */     } catch (SAXRuntimeException sre) {
/*     */       throw sre.e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeClass(ClassItem item) {
/*     */     this.writer.start("class", new String[] { "name", item.getType().fullName() });
/*     */     if (item.getSuperClass() != null) {
/*     */       this.writer.element("extends", new String[] { "name", (item.getSuperClass()).name });
/*     */     }
/*     */     this.writer.start("field-summary");
/*     */     FieldUse[] fus = item.getDeclaredFieldUses();
/*     */     for (int i = 0; i < fus.length; i++) {
/*     */       FieldUse fu = fus[i];
/*     */       Vector vec = new Vector();
/*     */       vec.add("name");
/*     */       vec.add(fu.name);
/*     */       vec.add("type");
/*     */       vec.add(fu.type.name());
/*     */       vec.add("occurs");
/*     */       vec.add(fu.multiplicity.toString());
/*     */       if (fu.getRealization() != null) {
/*     */         vec.add("realization");
/*     */         vec.add(fu.getRealization().getClass().getName());
/*     */       } 
/*     */       this.writer.element("field", vec.<String>toArray(new String[0]));
/*     */     } 
/*     */     this.writer.end("field-summary");
/*     */     if (!this.signatureOnly) {
/*     */       this.patternWriter.visitUnary(item.exp);
/*     */     }
/*     */     this.writer.end("class");
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeInterface(InterfaceItem item) {
/*     */     this.writer.start("interface", new String[] { "name", item.name });
/*     */     this.patternWriter.visitUnary(item.exp);
/*     */     this.writer.end("interface");
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeNameClass(NameClass nc) {
/* 303 */     nc.visit((NameClassVisitor)new Object(this));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\writer\Writer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */