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
/*    */ public class MySQLTransactionRollbackException
/*    */   extends MySQLTransientException
/*    */   implements DeadlockTimeoutRollbackMarker
/*    */ {
/*    */   public MySQLTransactionRollbackException(String reason, String SQLState, int vendorCode) {
/* 30 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException(String reason, String SQLState) {
/* 34 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException(String reason) {
/* 38 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\MySQLTransactionRollbackException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */