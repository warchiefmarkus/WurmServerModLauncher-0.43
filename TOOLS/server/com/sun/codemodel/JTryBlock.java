/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class JTryBlock
/*    */   implements JStatement
/*    */ {
/* 33 */   private JBlock body = new JBlock();
/* 34 */   private List<JCatchBlock> catches = new ArrayList<JCatchBlock>();
/* 35 */   private JBlock _finally = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock body() {
/* 41 */     return this.body;
/*    */   }
/*    */   
/*    */   public JCatchBlock _catch(JClass exception) {
/* 45 */     JCatchBlock cb = new JCatchBlock(exception);
/* 46 */     this.catches.add(cb);
/* 47 */     return cb;
/*    */   }
/*    */   
/*    */   public JBlock _finally() {
/* 51 */     if (this._finally == null) this._finally = new JBlock(); 
/* 52 */     return this._finally;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 56 */     f.p("try").g(this.body);
/* 57 */     for (JCatchBlock cb : this.catches)
/* 58 */       f.g(cb); 
/* 59 */     if (this._finally != null)
/* 60 */       f.p("finally").g(this._finally); 
/* 61 */     f.nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JTryBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */