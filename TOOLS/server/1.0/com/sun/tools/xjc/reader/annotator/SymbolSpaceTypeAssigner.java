/*    */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*    */ 
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
/*    */ import com.sun.tools.xjc.reader.TypeUtil;
/*    */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SymbolSpaceTypeAssigner
/*    */ {
/*    */   public static void assign(AnnotatedGrammar grammar, AnnotatorController controller) {
/* 37 */     Map applicableTypes = new HashMap();
/*    */ 
/*    */     
/* 40 */     ClassItem[] classes = grammar.getClasses();
/* 41 */     for (int i = 0; i < classes.length; i++) {
/* 42 */       ClassItem ci = classes[i];
/* 43 */       ci.exp.visit((ExpressionVisitorVoid)new Object(applicableTypes, ci));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 94 */     Iterator itr = applicableTypes.entrySet().iterator();
/* 95 */     while (itr.hasNext()) {
/* 96 */       Map.Entry e = itr.next();
/*    */       
/* 98 */       ((SymbolSpace)e.getKey()).setType(TypeUtil.getCommonBaseType(grammar.codeModel, (JType[])((Set)e.getValue()).toArray((Object[])new JType[0])));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\SymbolSpaceTypeAssigner.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */