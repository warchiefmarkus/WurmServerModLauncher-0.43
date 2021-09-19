/*    */ package 1.0.com.sun.tools.xjc.addon.locator;
/*    */ 
/*    */ import com.sun.codemodel.JAssignmentTarget;
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFieldRef;
/*    */ import com.sun.codemodel.JFieldVar;
/*    */ import com.sun.codemodel.JMethod;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.BadCommandLineException;
/*    */ import com.sun.tools.xjc.CodeAugmenter;
/*    */ import com.sun.tools.xjc.Options;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.runtime.UnmarshallingContext;
/*    */ import com.sun.xml.bind.Locatable;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SourceLocationAddOn
/*    */   implements CodeAugmenter
/*    */ {
/*    */   private static final String fieldName = "locator";
/*    */   
/*    */   public String getOptionName() {
/* 40 */     return "Xlocator";
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 44 */     return "  -Xlocator          :  enable source location support for generated code";
/*    */   }
/*    */   
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 48 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean run(AnnotatedGrammar grammar, GeneratorContext context, Options opt, ErrorHandler errorHandler) {
/* 59 */     JCodeModel codeModel = grammar.codeModel;
/*    */     
/* 61 */     ClassItem[] cis = grammar.getClasses();
/* 62 */     for (int i = 0; i < cis.length; i++) {
/* 63 */       JDefinedClass impl = (context.getClassContext(cis[i])).implClass;
/* 64 */       if (cis[i].getSuperClass() == null) {
/* 65 */         JFieldVar jFieldVar = impl.field(2, Locator.class, "locator");
/* 66 */         impl._implements(Locatable.class);
/*    */         
/* 68 */         impl.method(1, Locator.class, "sourceLocation").body()._return((JExpression)jFieldVar);
/*    */       } 
/*    */       
/* 71 */       JClass[] inner = impl.listClasses();
/* 72 */       for (int j = 0; j < inner.length; j++) {
/* 73 */         if (inner[j].name().equals("Unmarshaller")) {
/* 74 */           JDefinedClass unm = (JDefinedClass)inner[j];
/*    */           
/* 76 */           JMethod cons = unm.getConstructor(new JType[] { (JType)context.getRuntime(UnmarshallingContext.class) });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 83 */           JFieldRef locatorField = JExpr.ref("locator");
/* 84 */           cons.body()._if(locatorField.eq(JExpr._null()))._then().assign((JAssignmentTarget)locatorField, (JExpression)JExpr._new(codeModel.ref(LocatorImpl.class)).arg((JExpression)cons.listParams()[0].invoke("getLocator")));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 91 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\addon\locator\SourceLocationAddOn.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */