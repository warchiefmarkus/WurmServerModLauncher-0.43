/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JDocComment;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JMods;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.codemodel.JVar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JFieldVar
/*    */   extends JVar
/*    */ {
/* 18 */   private JDocComment jdoc = null;
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
/*    */   JFieldVar(JMods mods, JType type, String name, JExpression init) {
/* 33 */     super(mods, type, name, init);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JDocComment javadoc() {
/* 42 */     if (this.jdoc == null)
/* 43 */       this.jdoc = new JDocComment(); 
/* 44 */     return this.jdoc;
/*    */   }
/*    */   
/*    */   public void declare(JFormatter f) {
/* 48 */     if (this.jdoc != null)
/* 49 */       f.g((JGenerable)this.jdoc); 
/* 50 */     super.declare(f);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JFieldVar.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */