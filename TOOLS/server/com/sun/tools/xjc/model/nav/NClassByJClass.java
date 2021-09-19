/*    */ package com.sun.tools.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.outline.Aspect;
/*    */ import com.sun.tools.xjc.outline.Outline;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NClassByJClass
/*    */   implements NClass
/*    */ {
/*    */   final JClass clazz;
/*    */   
/*    */   NClassByJClass(JClass clazz) {
/* 50 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public JClass toType(Outline o, Aspect aspect) {
/* 54 */     return this.clazz;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 58 */     return this.clazz.isAbstract();
/*    */   }
/*    */   
/*    */   public boolean isBoxedType() {
/* 62 */     return (this.clazz.getPrimitiveType() != null);
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 66 */     return this.clazz.fullName();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\nav\NClassByJClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */