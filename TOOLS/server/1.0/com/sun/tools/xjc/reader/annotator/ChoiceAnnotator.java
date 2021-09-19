/*    */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*    */ 
/*    */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*    */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*    */ import java.io.PrintStream;
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
/*    */ class ChoiceAnnotator
/*    */ {
/* 43 */   private static PrintStream debug = null;
/*    */   private final AnnotatedGrammar grammar;
/*    */   
/*    */   public static void annotate(AnnotatedGrammar g, AnnotatorController _controller) {
/* 47 */     com.sun.tools.xjc.reader.annotator.ChoiceAnnotator ann = new com.sun.tools.xjc.reader.annotator.ChoiceAnnotator(g, _controller);
/*    */ 
/*    */     
/* 50 */     ann.getClass(); g.visit((ExpressionVisitorVoid)new Finder(ann, null));
/*    */   }
/*    */   private final CodeModelClassFactory classFactory;
/*    */   private ChoiceAnnotator(AnnotatedGrammar g, AnnotatorController _controller) {
/* 54 */     this.grammar = g;
/* 55 */     this.classFactory = new CodeModelClassFactory(_controller.getErrorReceiver());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\ChoiceAnnotator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */