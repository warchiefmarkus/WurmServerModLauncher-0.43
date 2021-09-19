/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ final class JTypeWildcard
/*    */   extends JClass
/*    */ {
/*    */   private final JClass bound;
/*    */   
/*    */   JTypeWildcard(JClass bound) {
/* 26 */     super(bound.owner());
/* 27 */     this.bound = bound;
/*    */   }
/*    */   
/*    */   public String name() {
/* 31 */     return "? extends " + this.bound.name();
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 35 */     return "? extends " + this.bound.fullName();
/*    */   }
/*    */   
/*    */   public JPackage _package() {
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JClass _extends() {
/* 49 */     if (this.bound != null) {
/* 50 */       return this.bound;
/*    */     }
/* 52 */     return owner().ref(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<JClass> _implements() {
/* 59 */     return this.bound._implements();
/*    */   }
/*    */   
/*    */   public boolean isInterface() {
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 71 */     JClass nb = this.bound.substituteParams(variables, bindings);
/* 72 */     if (nb == this.bound) {
/* 73 */       return this;
/*    */     }
/* 75 */     return new JTypeWildcard(nb);
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 79 */     if (this.bound._extends() == null) {
/* 80 */       f.p("?");
/*    */     } else {
/* 82 */       f.p("? extends").g(this.bound);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JTypeWildcard.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */