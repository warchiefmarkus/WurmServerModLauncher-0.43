/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ public final class JArray
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private final JType type;
/*    */   private final JExpression size;
/* 34 */   private List<JExpression> exprs = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JArray add(JExpression e) {
/* 40 */     if (this.exprs == null)
/* 41 */       this.exprs = new ArrayList<JExpression>(); 
/* 42 */     this.exprs.add(e);
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   JArray(JType type, JExpression size) {
/* 47 */     this.type = type;
/* 48 */     this.size = size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(JFormatter f) {
/* 55 */     int arrayCount = 0;
/* 56 */     JType t = this.type;
/*    */     
/* 58 */     while (t.isArray()) {
/* 59 */       t = t.elementType();
/* 60 */       arrayCount++;
/*    */     } 
/*    */     
/* 63 */     f.p("new").g(t).p('[');
/* 64 */     if (this.size != null)
/* 65 */       f.g(this.size); 
/* 66 */     f.p(']');
/*    */     
/* 68 */     for (int i = 0; i < arrayCount; i++) {
/* 69 */       f.p("[]");
/*    */     }
/* 71 */     if (this.size == null || this.exprs != null)
/* 72 */       f.p('{'); 
/* 73 */     if (this.exprs != null) {
/* 74 */       f.g((Collection)this.exprs);
/*    */     } else {
/* 76 */       f.p(' ');
/*    */     } 
/* 78 */     if (this.size == null || this.exprs != null)
/* 79 */       f.p('}'); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */