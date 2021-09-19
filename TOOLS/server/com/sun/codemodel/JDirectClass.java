/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class JDirectClass
/*    */   extends JClass
/*    */ {
/*    */   private final String fullName;
/*    */   
/*    */   public JDirectClass(JCodeModel _owner, String fullName) {
/* 18 */     super(_owner);
/* 19 */     this.fullName = fullName;
/*    */   }
/*    */   
/*    */   public String name() {
/* 23 */     int i = this.fullName.lastIndexOf('.');
/* 24 */     if (i >= 0) return this.fullName.substring(i + 1); 
/* 25 */     return this.fullName;
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 29 */     return this.fullName;
/*    */   }
/*    */   
/*    */   public JPackage _package() {
/* 33 */     int i = this.fullName.lastIndexOf('.');
/* 34 */     if (i >= 0) return owner()._package(this.fullName.substring(0, i)); 
/* 35 */     return owner().rootPackage();
/*    */   }
/*    */   
/*    */   public JClass _extends() {
/* 39 */     return owner().ref(Object.class);
/*    */   }
/*    */   
/*    */   public Iterator<JClass> _implements() {
/* 43 */     return Collections.<JClass>emptyList().iterator();
/*    */   }
/*    */   
/*    */   public boolean isInterface() {
/* 47 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 51 */     return false;
/*    */   }
/*    */   
/*    */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 55 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JDirectClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */