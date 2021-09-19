/*    */ package 1.0.com.sun.tools.xjc.addon.elementwrapper;
/*    */ 
/*    */ import com.sun.tools.xjc.BadCommandLineException;
/*    */ import com.sun.tools.xjc.CodeAugmenter;
/*    */ import com.sun.tools.xjc.Options;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.runtime.ElementWrapper;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.ErrorHandler;
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
/*    */ public class ElementWrapperAddOn
/*    */   implements CodeAugmenter
/*    */ {
/*    */   public String getOptionName() {
/* 25 */     return "Xelement-wrapper";
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 29 */     return "  -Xelement-wrapper  :  generates the general purpose element wrapper into impl.runtime";
/*    */   }
/*    */   
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean run(AnnotatedGrammar grammar, GeneratorContext context, Options opt, ErrorHandler errorHandler) {
/* 42 */     context.getRuntime(ElementWrapper.class);
/*    */     
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\addon\elementwrapper\ElementWrapperAddOn.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */