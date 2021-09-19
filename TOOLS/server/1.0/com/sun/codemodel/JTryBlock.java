/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JCatchBlock;
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JStatement;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JTryBlock
/*    */   implements JStatement
/*    */ {
/* 19 */   private JBlock body = new JBlock();
/* 20 */   private List catches = new ArrayList();
/* 21 */   private JBlock _finally = null;
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock body() {
/* 26 */     return this.body;
/*    */   }
/*    */   
/*    */   public JCatchBlock _catch(JClass exception) {
/* 30 */     JCatchBlock cb = new JCatchBlock(exception);
/* 31 */     this.catches.add(cb);
/* 32 */     return cb;
/*    */   }
/*    */   
/*    */   public JBlock _finally() {
/* 36 */     if (this._finally == null) this._finally = new JBlock(); 
/* 37 */     return this._finally;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 41 */     f.p("try").g((JGenerable)this.body);
/* 42 */     for (Iterator i = this.catches.iterator(); i.hasNext();)
/* 43 */       f.g((JGenerable)i.next()); 
/* 44 */     if (this._finally != null)
/* 45 */       f.p("finally").g((JGenerable)this._finally); 
/* 46 */     f.nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JTryBlock.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */