/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JDocComment;
/*    */ import com.sun.codemodel.JMethod;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.codemodel.JVar;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
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
/*    */ public abstract class MethodWriter
/*    */ {
/*    */   protected final JCodeModel codeModel;
/*    */   
/*    */   protected MethodWriter(ClassContext context) {
/* 31 */     this.codeModel = context.parent.getCodeModel();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JMethod declareMethod(JType paramJType, String paramString);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final JMethod declareMethod(Class returnType, String methodName) {
/* 44 */     return declareMethod((JType)this.codeModel.ref(returnType), methodName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JDocComment javadoc();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JVar addParameter(JType paramJType, String paramString);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final JVar addParameter(Class type, String name) {
/* 65 */     return addParameter((JType)this.codeModel.ref(type), name);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\MethodWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */