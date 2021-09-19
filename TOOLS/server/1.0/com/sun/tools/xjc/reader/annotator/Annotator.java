/*     */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*     */ 
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.util.NotAllowedRemover;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.reader.annotator.ChoiceAnnotator;
/*     */ import com.sun.tools.xjc.reader.annotator.DatatypeSimplifier;
/*     */ import com.sun.tools.xjc.reader.annotator.EmptyJavaItemRemover;
/*     */ import com.sun.tools.xjc.reader.annotator.FieldItemAnnotation;
/*     */ import com.sun.tools.xjc.reader.annotator.HierarchyAnnotator;
/*     */ import com.sun.tools.xjc.reader.annotator.MixedRemover;
/*     */ import com.sun.tools.xjc.reader.annotator.PrimitiveTypeAnnotator;
/*     */ import com.sun.tools.xjc.reader.annotator.RelationNormalizer;
/*     */ import com.sun.tools.xjc.reader.annotator.SymbolSpaceTypeAssigner;
/*     */ import com.sun.tools.xjc.reader.annotator.TemporaryClassItemRemover;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import com.sun.tools.xjc.writer.Writer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ public class Annotator
/*     */ {
/*  27 */   private static PrintStream debug = (Util.getSystemProperty(com.sun.tools.xjc.reader.annotator.Annotator.class, "debug") != null) ? System.out : null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void annotate(AnnotatedGrammar grammar, AnnotatorController controller) {
/*  36 */     if (debug != null) {
/*  37 */       debug.println("---------------------------------------------");
/*  38 */       debug.println("initial grammar");
/*  39 */       Writer.writeToConsole(true, (Grammar)grammar);
/*  40 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     if (debug != null) debug.println("removing notAllowed");
/*     */     
/*  50 */     NotAllowedRemover notAllowedRemover = new NotAllowedRemover(grammar.getPool());
/*  51 */     grammar.visit((ExpressionVisitorExpression)notAllowedRemover);
/*  52 */     if (grammar.exp == Expression.nullSet) {
/*     */       return;
/*     */     }
/*  55 */     ClassItem[] classes = grammar.getClasses(); int i;
/*  56 */     for (i = 0; i < classes.length; i++) {
/*  57 */       (classes[i]).exp = (classes[i]).exp.visit((ExpressionVisitorExpression)notAllowedRemover);
/*     */     }
/*     */     
/*  60 */     if (debug != null) {
/*  61 */       Writer.writeToConsole(true, (Grammar)grammar);
/*  62 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (debug != null) debug.println("removing empty JavaItems");
/*     */     
/*  72 */     EmptyJavaItemRemover emptyJavaItemRemover = new EmptyJavaItemRemover(grammar.getPool());
/*  73 */     grammar.visit((ExpressionVisitorExpression)emptyJavaItemRemover);
/*  74 */     if (grammar.exp == Expression.nullSet) {
/*     */       return;
/*     */     }
/*  77 */     classes = grammar.getClasses();
/*  78 */     for (i = 0; i < classes.length; i++) {
/*  79 */       (classes[i]).exp = (classes[i]).exp.visit((ExpressionVisitorExpression)emptyJavaItemRemover);
/*     */     }
/*     */     
/*  82 */     if (debug != null) {
/*  83 */       Writer.writeToConsole(true, (Grammar)grammar);
/*  84 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     if (debug != null) debug.println("removing mixed");
/*     */     
/*  93 */     MixedRemover mixedRemover = new MixedRemover(grammar);
/*  94 */     grammar.visit((ExpressionVisitorExpression)mixedRemover);
/*  95 */     if (grammar.exp == Expression.nullSet) {
/*     */       return;
/*     */     }
/*  98 */     classes = grammar.getClasses();
/*  99 */     for (i = 0; i < classes.length; i++) {
/* 100 */       (classes[i]).exp = (classes[i]).exp.visit((ExpressionVisitorExpression)mixedRemover);
/*     */     }
/*     */     
/* 103 */     if (debug != null) {
/* 104 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 105 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (debug != null) debug.println("simplifying datatypes"); 
/* 113 */     grammar.visit((ExpressionVisitorExpression)new DatatypeSimplifier(grammar.getPool()));
/*     */ 
/*     */ 
/*     */     
/* 117 */     if (debug != null) {
/* 118 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 119 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     if (debug != null) debug.println("examining primitive types");
/*     */     
/* 128 */     PrimitiveTypeAnnotator visitor = new PrimitiveTypeAnnotator(grammar, controller);
/* 129 */     grammar.visit((ExpressionVisitorExpression)visitor);
/* 130 */     if (grammar.exp == Expression.nullSet)
/* 131 */       return;  classes = grammar.getClasses();
/* 132 */     for (i = 0; i < classes.length; i++) {
/* 133 */       (classes[i]).exp = (classes[i]).exp.visit((ExpressionVisitorExpression)visitor);
/*     */     }
/*     */     
/* 136 */     if (debug != null) {
/* 137 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 138 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     if (debug != null) debug.println("annotating complex choices"); 
/* 145 */     ChoiceAnnotator.annotate(grammar, controller);
/*     */     
/* 147 */     if (debug != null) {
/* 148 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 149 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     if (debug != null) debug.println("removing temporary class items"); 
/* 158 */     TemporaryClassItemRemover.remove(grammar);
/*     */     
/* 160 */     if (debug != null) {
/* 161 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 162 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     if (debug != null) debug.println("adding field items"); 
/* 170 */     FieldItemAnnotation.annotate(grammar, controller);
/*     */     
/* 172 */     if (debug != null) {
/* 173 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 174 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     if (debug != null) debug.println("computing type hierarchy"); 
/* 182 */     HierarchyAnnotator.annotate(grammar, controller);
/*     */     
/* 184 */     if (debug != null) {
/* 185 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 186 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (debug != null) debug.println("determining types for symbol spaces"); 
/* 195 */     SymbolSpaceTypeAssigner.assign(grammar, controller);
/*     */     
/* 197 */     if (debug != null) {
/* 198 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 199 */       debug.println("---------------------------------------------");
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
/* 213 */     if (debug != null) debug.println("normalizing relations"); 
/* 214 */     RelationNormalizer.normalize(grammar, controller);
/*     */     
/* 216 */     if (debug != null) {
/* 217 */       Writer.writeToConsole(true, (Grammar)grammar);
/* 218 */       debug.println("---------------------------------------------");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\Annotator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */