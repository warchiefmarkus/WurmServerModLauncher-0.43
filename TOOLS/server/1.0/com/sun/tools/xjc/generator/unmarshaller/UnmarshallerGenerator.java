/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.NameClassVisitor;
/*     */ import com.sun.msv.util.StringPair;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.PackageContext;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.AutomatonBuilder;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.PerClassGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Automaton;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnmarshallerGenerator
/*     */ {
/*     */   final Options options;
/*     */   final AnnotatedGrammar grammar;
/*     */   final JCodeModel codeModel;
/*     */   final GeneratorContext context;
/*     */   final boolean trace;
/*     */   static Class class$javax$xml$namespace$QName;
/*     */   
/*     */   public static Automaton[] generate(AnnotatedGrammar grammar, GeneratorContext context, Options opt) {
/*  55 */     return (new com.sun.tools.xjc.generator.unmarshaller.UnmarshallerGenerator(grammar, context, opt))._generate();
/*     */   }
/*     */ 
/*     */   
/*     */   private Automaton[] _generate() {
/*  60 */     ClassItem[] cis = this.grammar.getClasses();
/*  61 */     Automaton[] automata = new Automaton[cis.length];
/*     */     
/*  63 */     Map automataDic = new HashMap();
/*     */     
/*     */     int i;
/*     */     
/*  67 */     for (i = 0; i < cis.length; i++) {
/*  68 */       automata[i] = new Automaton(this.context.getClassContext(cis[i]));
/*  69 */       automataDic.put(cis[i], automata[i]);
/*     */     } 
/*     */ 
/*     */     
/*  73 */     for (i = 0; i < automata.length; i++) {
/*  74 */       AutomatonBuilder.build(automata[i], this.context, automataDic);
/*     */     }
/*     */     
/*  77 */     if (this.options.debugMode && this.options.verbose) {
/*  78 */       for (i = 0; i < cis.length; i++) {
/*     */ 
/*     */         
/*  81 */         System.out.println(cis[i].getType().fullName());
/*  82 */         System.out.println("nullable: " + automata[i].isNullable());
/*  83 */         System.out.println();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     for (i = 0; i < automata.length; i++) {
/*  91 */       (new PerClassGenerator(this, automata[i])).generate();
/*     */     }
/*     */     
/*  94 */     generateGrammarInfoImpl();
/*     */     
/*  96 */     return automata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateGrammarInfoImpl() {
/* 104 */     PackageContext[] pcs = this.context.getAllPackageContexts();
/* 105 */     for (int i = 0; i < pcs.length; i++) {
/*     */       
/* 107 */       RootMapBuilder rmb = new RootMapBuilder((pcs[i]).rootTagMap, (pcs[i]).objectFactory);
/*     */ 
/*     */       
/* 110 */       Map roots = getRootMap((pcs[i])._package);
/*     */       
/* 112 */       ClassItem[] classes = (ClassItem[])roots.keySet().toArray((Object[])new ClassItem[roots.size()]);
/* 113 */       NameClass[] nameClasses = (NameClass[])roots.values().toArray((Object[])new NameClass[roots.size()]);
/*     */ 
/*     */       
/* 116 */       ProbePointBuilder ppb = new ProbePointBuilder(null);
/* 117 */       for (int j = 0; j < nameClasses.length; j++) {
/* 118 */         nameClasses[j].visit((NameClassVisitor)ppb);
/*     */       }
/*     */       
/* 121 */       StringPair[] probePoints = ppb.getResult();
/* 122 */       for (int k = 0; k < probePoints.length; k++) {
/*     */         int m;
/* 124 */         for (m = 0; m < nameClasses.length; m++) {
/* 125 */           if (nameClasses[m].accepts(probePoints[k])) {
/* 126 */             rmb.add(probePoints[k], classes[m]); break;
/*     */           } 
/*     */         } 
/* 129 */         if (m == nameClasses.length) {
/* 130 */           rmb.add(probePoints[k], null);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map getRootMap(JPackage currentPackage) {
/* 140 */     Map roots = new HashMap();
/*     */ 
/*     */     
/* 143 */     this.grammar.getTopLevel().visit((ExpressionVisitorVoid)new Object(this, currentPackage, roots));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     return roots;
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
/*     */   UnmarshallerGenerator(AnnotatedGrammar _grammar, GeneratorContext _context, Options _opt) {
/* 211 */     this.options = _opt;
/* 212 */     this.trace = this.options.traceUnmarshaller;
/* 213 */     this.grammar = _grammar;
/* 214 */     this.codeModel = this.grammar.codeModel;
/* 215 */     this.context = _context;
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
/*     */   protected JExpression generateNameClassTest(NameClass nc, JVar $uri, JVar $local) {
/* 232 */     return (JExpression)nc.visit((NameClassVisitor)new Object(this, $local, $uri));
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
/*     */   static Class class$(String x0) {
/*     */     
/* 282 */     try { return Class.forName(x0); } catch (ClassNotFoundException x1) { throw new NoClassDefFoundError(x1.getMessage()); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\UnmarshallerGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */