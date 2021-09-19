/*     */ package 1.0.com.sun.tools.xjc.generator.validator;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.msv.grammar.trex.TREXGrammar;
/*     */ import com.sun.msv.grammar.util.ExpressionPrinter;
/*     */ import com.sun.msv.verifier.DocumentDeclaration;
/*     */ import com.sun.msv.verifier.regexp.REDocumentDeclaration;
/*     */ import com.sun.msv.writer.relaxng.RELAXNGWriter;
/*     */ import com.sun.org.apache.xml.internal.serialize.OutputFormat;
/*     */ import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.validator.SchemaFragmentBuilder;
/*     */ import com.sun.tools.xjc.generator.validator.StringOutputStream;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.runtime.ValidatableObject;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.bind.validator.SchemaDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringWriter;
/*     */ import java.util.zip.GZIPOutputStream;
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
/*     */ 
/*     */ 
/*     */ public class ValidatorGenerator
/*     */ {
/*  52 */   private static final PrintStream debug = (Util.getSystemProperty(com.sun.tools.xjc.generator.validator.ValidatorGenerator.class, "debug") != null) ? System.out : null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(AnnotatedGrammar grammar, GeneratorContext context, Options opt) {
/*  59 */     JCodeModel codeModel = grammar.codeModel;
/*     */ 
/*     */     
/*  62 */     ClassItem[] cis = grammar.getClasses();
/*  63 */     for (int i = 0; i < cis.length; i++) {
/*  64 */       JExpression encodedFragment; ClassItem ci = cis[i];
/*  65 */       JDefinedClass cls = (context.getClassContext(ci)).implClass;
/*     */ 
/*     */       
/*  68 */       cls._implements(context.getRuntime(ValidatableObject.class));
/*     */ 
/*     */       
/*  71 */       JMethod method = cls.method(1, Class.class, "getPrimaryInterface");
/*     */ 
/*     */       
/*  74 */       method.body()._return(((JClass)ci.getType()).dotclass());
/*     */ 
/*     */ 
/*     */       
/*  78 */       ExpressionPool pool = new ExpressionPool();
/*  79 */       Expression fragment = createSchemaFragment(ci, pool);
/*  80 */       if (opt.debugMode && opt.verbose) {
/*  81 */         System.out.println(ci.getType().fullName());
/*  82 */         System.out.println(ExpressionPrinter.printFragment(fragment));
/*  83 */         System.out.println();
/*     */       } 
/*     */       
/*  86 */       if (debug != null) {
/*  87 */         debug.println("---- schema fragment for " + ci.name + " ----");
/*     */         try {
/*  89 */           TREXGrammar g = new TREXGrammar(pool);
/*  90 */           g.exp = fragment;
/*  91 */           RELAXNGWriter w = new RELAXNGWriter();
/*  92 */           OutputFormat format = new OutputFormat("xml", null, true);
/*     */           
/*  94 */           format.setIndent(1);
/*  95 */           w.setDocumentHandler(new XMLSerializer(debug, format));
/*  96 */           w.write((Grammar)g);
/*  97 */         } catch (SAXException e) {
/*  98 */           e.printStackTrace();
/*  99 */           throw new JAXBAssertionError();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 104 */       StringWriter sw = new StringWriter();
/* 105 */       saveFragmentTo(fragment, pool, (OutputStream)new StringOutputStream(sw));
/*     */       
/* 107 */       String deserializeMethodName = "deserialize";
/*     */       
/* 109 */       if (sw.getBuffer().length() > 32768) {
/*     */         
/* 111 */         sw = new StringWriter();
/*     */         try {
/* 113 */           saveFragmentTo(fragment, pool, new GZIPOutputStream((OutputStream)new StringOutputStream(sw)));
/*     */           
/* 115 */           deserializeMethodName = "deserializeCompressed";
/* 116 */         } catch (IOException e) {
/*     */           
/* 118 */           throw new InternalError(e.getMessage());
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 127 */       JFieldVar $schemaFragment = cls.field(20, Grammar.class, "schemaFragment");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 134 */       if (Util.getSystemProperty(com.sun.tools.xjc.generator.validator.ValidatorGenerator.class, "noSplit") != null) {
/* 135 */         encodedFragment = JExpr.lit(sw.toString());
/*     */       } else {
/* 137 */         int len = sw.getBuffer().length();
/* 138 */         StringBuffer buf = new StringBuffer(len);
/* 139 */         for (int j = 0; j < len; j += 60) {
/* 140 */           buf.append('\n');
/* 141 */           if (j != 0) { buf.append('+'); }
/* 142 */           else { buf.append(' '); }
/* 143 */            buf.append(JExpr.quotify('"', sw.getBuffer().substring(j, Math.min(j + 60, len))));
/*     */         } 
/* 145 */         encodedFragment = JExpr.direct(buf.toString());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 156 */       JMethod m = cls.method(1, DocumentDeclaration.class, "createRawValidator");
/*     */ 
/*     */       
/* 159 */       m.body()._if($schemaFragment.eq(JExpr._null()))._then().assign((JAssignmentTarget)$schemaFragment, (JExpression)codeModel.ref(SchemaDeserializer.class).staticInvoke(deserializeMethodName).arg(encodedFragment));
/*     */ 
/*     */ 
/*     */       
/* 163 */       m.body()._return((JExpression)JExpr._new(codeModel.ref(REDocumentDeclaration.class)).arg((JExpression)$schemaFragment));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void saveFragmentTo(Expression fragment, ExpressionPool pool, OutputStream os) {
/*     */     try {
/* 172 */       ObjectOutputStream oos = new ObjectOutputStream(os);
/* 173 */       oos.writeObject(fragment);
/* 174 */       oos.writeObject(pool);
/* 175 */       oos.close();
/* 176 */     } catch (IOException e) {
/*     */ 
/*     */       
/* 179 */       throw new JAXBAssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Expression createSchemaFragment(ClassItem ci, ExpressionPool pool) {
/* 186 */     if (ci.agm.exp == null) { exp = ci.exp; }
/* 187 */     else { exp = ci.agm.exp; }
/* 188 */      Expression exp = exp.visit((ExpressionVisitorExpression)new SchemaFragmentBuilder(new ExpressionPool()));
/*     */ 
/*     */     
/* 191 */     return exp.visit((ExpressionVisitorExpression)new Object(pool));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\validator\ValidatorGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */