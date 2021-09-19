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
/*    */ public final class JNullType
/*    */   extends JClass
/*    */ {
/*    */   JNullType(JCodeModel _owner) {
/* 38 */     super(_owner);
/*    */   }
/*    */   
/* 41 */   public String name() { return "null"; } public String fullName() {
/* 42 */     return "null";
/*    */   } public JPackage _package() {
/* 44 */     return owner()._package("");
/*    */   } public JClass _extends() {
/* 46 */     return null;
/*    */   }
/*    */   public Iterator<JClass> _implements() {
/* 49 */     return Collections.<JClass>emptyList().iterator();
/*    */   }
/*    */   
/* 52 */   public boolean isInterface() { return false; } public boolean isAbstract() {
/* 53 */     return false;
/*    */   }
/*    */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 56 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JNullType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */