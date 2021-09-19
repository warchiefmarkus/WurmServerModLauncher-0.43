/*    */ package com.sun.codemodel;
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
/*    */ public class JStringLiteral
/*    */   extends JExpressionImpl
/*    */ {
/*    */   public final String str;
/*    */   
/*    */   JStringLiteral(String what) {
/* 34 */     this.str = what;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(JFormatter f) {
/* 40 */     f.p(JExpr.quotify('"', this.str));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JStringLiteral.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */