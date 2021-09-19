/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JMods;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.codemodel.JVar;
/*    */ 
/*    */ 
/*    */ public class JCatchBlock
/*    */   implements JGenerable
/*    */ {
/*    */   JClass exception;
/* 16 */   private JVar var = null;
/* 17 */   private JBlock body = new JBlock();
/*    */   
/*    */   JCatchBlock(JClass exception) {
/* 20 */     this.exception = exception;
/*    */   }
/*    */   
/*    */   public JVar param(String name) {
/* 24 */     if (this.var != null) throw new IllegalStateException(); 
/* 25 */     this.var = new JVar(JMods.forVar(0), (JType)this.exception, name, null);
/* 26 */     return this.var;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 30 */     return this.body;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 34 */     if (this.var == null) this.var = new JVar(JMods.forVar(0), (JType)this.exception, "_x", null);
/*    */     
/* 36 */     f.p("catch (").b(this.var).p(')').g((JGenerable)this.body);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JCatchBlock.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */