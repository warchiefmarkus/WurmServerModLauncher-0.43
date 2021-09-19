/*    */ package org.seamless.util;
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
/*    */ public class Exceptions
/*    */ {
/*    */   public static Throwable unwrap(Throwable throwable) throws IllegalArgumentException {
/* 23 */     if (throwable == null) {
/* 24 */       throw new IllegalArgumentException("Cannot unwrap null throwable");
/*    */     }
/* 26 */     for (Throwable current = throwable; current != null; current = current.getCause()) {
/* 27 */       throwable = current;
/*    */     }
/* 29 */     return throwable;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\Exceptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */