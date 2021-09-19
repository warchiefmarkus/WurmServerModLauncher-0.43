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
/*    */ public class MySQLStatementCancelledException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   public MySQLStatementCancelledException(String reason, String SQLState, int vendorCode) {
/* 28 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLStatementCancelledException(String reason, String SQLState) {
/* 32 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLStatementCancelledException(String reason) {
/* 36 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLStatementCancelledException() {
/* 40 */     super("Statement cancelled due to client request");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\MySQLStatementCancelledException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */