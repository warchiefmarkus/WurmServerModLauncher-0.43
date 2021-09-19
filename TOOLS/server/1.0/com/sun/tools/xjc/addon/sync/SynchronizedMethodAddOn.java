/*    */ package 1.0.com.sun.tools.xjc.addon.sync;
/*    */ 
/*    */ import com.sun.codemodel.JMethod;
/*    */ import com.sun.tools.xjc.BadCommandLineException;
/*    */ import com.sun.tools.xjc.CodeAugmenter;
/*    */ import com.sun.tools.xjc.Options;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import java.io.IOException;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ public class SynchronizedMethodAddOn
/*    */   implements CodeAugmenter
/*    */ {
/*    */   public String getOptionName() {
/* 30 */     return "Xsync-methods";
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 34 */     return "  -Xsync-methods     :  generate accessor methods with the 'synchronized' keyword";
/*    */   }
/*    */   
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 38 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean run(AnnotatedGrammar grammar, GeneratorContext context, Options opt, ErrorHandler errorHandler) {
/* 47 */     ClassItem[] cis = grammar.getClasses();
/* 48 */     for (int i = 0; i < cis.length; i++) {
/* 49 */       augument(context.getClassContext(cis[i]));
/*    */     }
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void augument(ClassContext cc) {
/* 58 */     for (Iterator itr = cc.implClass.methods(); itr.hasNext(); ) {
/* 59 */       JMethod m = itr.next();
/* 60 */       m.getMods().setSynchronized(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\addon\sync\SynchronizedMethodAddOn.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */