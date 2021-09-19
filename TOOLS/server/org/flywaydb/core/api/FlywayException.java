/*    */ package org.flywaydb.core.api;
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
/*    */ public class FlywayException
/*    */   extends RuntimeException
/*    */ {
/*    */   public FlywayException(String message, Throwable cause) {
/* 29 */     super(message, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FlywayException(Throwable cause) {
/* 38 */     super(cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FlywayException(String message) {
/* 47 */     super(message);
/*    */   }
/*    */   
/*    */   public FlywayException() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\FlywayException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */