/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DDefine
/*    */ {
/*    */   private final String name;
/*    */   private DPattern pattern;
/*    */   private Boolean nullable;
/*    */   DAnnotation annotation;
/*    */   
/*    */   public DDefine(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public DPattern getPattern() {
/* 18 */     return this.pattern;
/*    */   }
/*    */   
/*    */   public DAnnotation getAnnotation() {
/* 22 */     if (this.annotation == null)
/* 23 */       return DAnnotation.EMPTY; 
/* 24 */     return this.annotation;
/*    */   }
/*    */   
/*    */   public void setPattern(DPattern pattern) {
/* 28 */     this.pattern = pattern;
/* 29 */     this.nullable = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isNullable() {
/* 40 */     if (this.nullable == null)
/* 41 */       this.nullable = this.pattern.isNullable() ? Boolean.TRUE : Boolean.FALSE; 
/* 42 */     return this.nullable.booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DDefine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */