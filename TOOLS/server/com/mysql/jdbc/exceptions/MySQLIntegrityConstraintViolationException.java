/*    */ package com.mysql.jdbc.exceptions;
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
/*    */ public class MySQLIntegrityConstraintViolationException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   public MySQLIntegrityConstraintViolationException() {}
/*    */   
/*    */   public MySQLIntegrityConstraintViolationException(String reason, String SQLState, int vendorCode) {
/* 34 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLIntegrityConstraintViolationException(String reason, String SQLState) {
/* 38 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLIntegrityConstraintViolationException(String reason) {
/* 42 */     super(reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\MySQLIntegrityConstraintViolationException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */