/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import java.sql.SQLTimeoutException;
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
/*    */ public class MySQLTimeoutException
/*    */   extends SQLTimeoutException
/*    */ {
/*    */   public MySQLTimeoutException(String reason, String SQLState, int vendorCode) {
/* 31 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTimeoutException(String reason, String SQLState) {
/* 35 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTimeoutException(String reason) {
/* 39 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTimeoutException() {
/* 43 */     super("Statement cancelled due to timeout or client request");
/*    */   }
/*    */ 
/*    */   
/*    */   public int getErrorCode() {
/* 48 */     return super.getErrorCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLTimeoutException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */