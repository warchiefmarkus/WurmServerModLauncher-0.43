/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.tools.xjc.api.ClassNameAllocator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ClassNameAllocatorWrapper
/*    */   implements ClassNameAllocator
/*    */ {
/*    */   private final ClassNameAllocator core;
/*    */   
/*    */   ClassNameAllocatorWrapper(ClassNameAllocator core) {
/* 51 */     if (core == null)
/* 52 */       core = new ClassNameAllocator() {
/*    */           public String assignClassName(String packageName, String className) {
/* 54 */             return className;
/*    */           }
/*    */         }; 
/* 57 */     this.core = core;
/*    */   }
/*    */   
/*    */   public String assignClassName(String packageName, String className) {
/* 61 */     return this.core.assignClassName(packageName, className);
/*    */   }
/*    */   
/*    */   public String assignClassName(JPackage pkg, String className) {
/* 65 */     return this.core.assignClassName(pkg.name(), className);
/*    */   }
/*    */   
/*    */   public String assignClassName(CClassInfoParent parent, String className) {
/* 69 */     if (parent instanceof CClassInfoParent.Package) {
/* 70 */       CClassInfoParent.Package p = (CClassInfoParent.Package)parent;
/* 71 */       return assignClassName(p.pkg, className);
/*    */     } 
/*    */     
/* 74 */     return className;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\ClassNameAllocatorWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */