/*     */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*     */ import com.sun.tools.xjc.grammar.util.FieldMultiplicityCounter;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.reader.annotator.Messages;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RelationNormalizer
/*     */ {
/*     */   private final AnnotatorController controller;
/*     */   private final ExpressionPool pool;
/*     */   private final AnnotatedGrammar grammar;
/*     */   static Class class$java$lang$Object;
/*     */   
/*     */   private RelationNormalizer(AnnotatorController _controller, AnnotatedGrammar _grammar) {
/* 111 */     this.controller = _controller;
/* 112 */     this.pool = _grammar.getPool();
/* 113 */     this.grammar = _grammar;
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
/*     */   public static void normalize(AnnotatedGrammar grammar, AnnotatorController controller) {
/* 131 */     com.sun.tools.xjc.reader.annotator.RelationNormalizer n = new com.sun.tools.xjc.reader.annotator.RelationNormalizer(controller, grammar);
/* 132 */     ClassItem[] classItems = grammar.getClasses();
/* 133 */     InterfaceItem[] interfaceItems = grammar.getInterfaces();
/*     */     
/* 135 */     n.getClass(); Pass1 pass1 = new Pass1(n, null);
/*     */     int i;
/* 137 */     for (i = 0; i < classItems.length; i++)
/* 138 */       pass1.process(classItems[i]); 
/* 139 */     for (i = 0; i < interfaceItems.length; i++) {
/* 140 */       interfaceItems[i].visit((ExpressionVisitorExpression)pass1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     for (i = 0; i < classItems.length; i++) {
/* 147 */       FieldUse[] fieldUses = classItems[i].getDeclaredFieldUses();
/* 148 */       for (int j = 0; j < fieldUses.length; j++) {
/*     */         
/* 150 */         (fieldUses[j]).multiplicity = FieldMultiplicityCounter.count((classItems[i]).exp, fieldUses[j]);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 155 */         Set possibleTypes = new HashSet();
/* 156 */         FieldItem[] fields = (FieldItem[])(fieldUses[j]).items.toArray((Object[])new FieldItem[0]);
/* 157 */         for (int k = 0; k < fields.length; k++) {
/* 158 */           possibleTypes.add(fields[k].getType(grammar.codeModel));
/*     */         }
/*     */         
/* 161 */         (fieldUses[j]).type = TypeUtil.getCommonBaseType(grammar.codeModel, possibleTypes);
/*     */         
/* 163 */         if (fieldUses[j].isDelegated() && !(fieldUses[j]).multiplicity.isAtMostOnce())
/*     */         {
/*     */           
/* 166 */           controller.reportError(new Locator[] { (classItems[i]).locator }, Messages.format("Normalizer.DelegationMultiplicityMustBe1", (fieldUses[j]).name));
/*     */         }
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 336 */     try { return Class.forName(x0); } catch (ClassNotFoundException x1) { throw new NoClassDefFoundError(x1.getMessage()); }
/*     */   
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void _assert(boolean b) {
/* 614 */     if (!b) {
/* 615 */       throw new JAXBAssertionError();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isInterface(JType t) {
/* 623 */     if (t.isPrimitive()) return false; 
/* 624 */     return ((JClass)t).isInterface();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isClass(Object exp) {
/* 631 */     return exp instanceof ClassItem;
/*     */   }
/*     */   private static boolean isSuperClass(Object exp) {
/* 634 */     return exp instanceof com.sun.tools.xjc.grammar.SuperClassItem;
/*     */   }
/*     */   private static boolean isInterface(Object exp) {
/* 637 */     return exp instanceof InterfaceItem;
/*     */   }
/*     */   private static boolean isField(Object exp) {
/* 640 */     return exp instanceof FieldItem;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isPrimitive(Object exp) {
/* 646 */     return exp instanceof com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */   }
/*     */   private static boolean isIgnore(Object exp) {
/* 649 */     return exp instanceof com.sun.tools.xjc.grammar.IgnoreItem;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\RelationNormalizer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */