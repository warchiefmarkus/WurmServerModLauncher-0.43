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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClassType
/*    */ {
/*    */   final String declarationToken;
/*    */   
/*    */   private ClassType(String token) {
/* 36 */     this.declarationToken = token;
/*    */   }
/*    */   
/* 39 */   public static final ClassType CLASS = new ClassType("class");
/* 40 */   public static final ClassType INTERFACE = new ClassType("interface");
/* 41 */   public static final ClassType ANNOTATION_TYPE_DECL = new ClassType("@interface");
/* 42 */   public static final ClassType ENUM = new ClassType("enum");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\ClassType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */