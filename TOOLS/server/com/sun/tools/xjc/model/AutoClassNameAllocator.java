/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.tools.xjc.api.ClassNameAllocator;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ public class AutoClassNameAllocator
/*    */   implements ClassNameAllocator
/*    */ {
/*    */   private final ClassNameAllocator core;
/* 54 */   private final Map<String, Set<String>> names = new HashMap<String, Set<String>>();
/*    */   
/*    */   public AutoClassNameAllocator(ClassNameAllocator core) {
/* 57 */     this.core = core;
/*    */   }
/*    */   
/*    */   public String assignClassName(String packageName, String className) {
/* 61 */     className = determineName(packageName, className);
/* 62 */     if (this.core != null)
/* 63 */       className = this.core.assignClassName(packageName, className); 
/* 64 */     return className;
/*    */   }
/*    */   
/*    */   private String determineName(String packageName, String className) {
/* 68 */     Set<String> s = this.names.get(packageName);
/* 69 */     if (s == null) {
/* 70 */       s = new HashSet<String>();
/* 71 */       this.names.put(packageName, s);
/*    */     } 
/*    */     
/* 74 */     if (s.add(className)) {
/* 75 */       return className;
/*    */     }
/* 77 */     for (int i = 2;; i++) {
/* 78 */       if (s.add(className + i))
/* 79 */         return className + i; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\AutoClassNameAllocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */