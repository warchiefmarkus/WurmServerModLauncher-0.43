/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import com.mysql.jdbc.exceptions.DeadlockTimeoutRollbackMarker;
/*    */ import java.sql.SQLTransactionRollbackException;
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
/*    */ public class MySQLTransactionRollbackException
/*    */   extends SQLTransactionRollbackException
/*    */   implements DeadlockTimeoutRollbackMarker
/*    */ {
/*    */   public MySQLTransactionRollbackException(String reason, String SQLState, int vendorCode) {
/* 34 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException(String reason, String SQLState) {
/* 38 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException(String reason) {
/* 42 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLTransactionRollbackException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */