/*    */ package org.flywaydb.core.internal.util;
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
/*    */ public class ExceptionUtils
/*    */ {
/*    */   public static Throwable getRootCause(Throwable throwable) {
/* 37 */     if (throwable == null) {
/* 38 */       return null;
/*    */     }
/*    */     
/* 41 */     Throwable cause = throwable;
/*    */     Throwable rootCause;
/* 43 */     while ((rootCause = cause.getCause()) != null) {
/* 44 */       cause = rootCause;
/*    */     }
/*    */     
/* 47 */     return cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\ExceptionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */