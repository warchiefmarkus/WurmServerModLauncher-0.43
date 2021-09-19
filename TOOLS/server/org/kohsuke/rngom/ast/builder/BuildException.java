/*    */ package org.kohsuke.rngom.ast.builder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BuildException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final Throwable cause;
/*    */   
/*    */   public BuildException(Throwable cause) {
/* 13 */     if (cause == null)
/* 14 */       throw new NullPointerException("null cause"); 
/* 15 */     this.cause = cause;
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 19 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\BuildException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */