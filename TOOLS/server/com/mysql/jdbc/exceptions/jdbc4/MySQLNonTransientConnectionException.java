/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import java.sql.SQLNonTransientConnectionException;
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
/*    */ public class MySQLNonTransientConnectionException
/*    */   extends SQLNonTransientConnectionException
/*    */ {
/*    */   public MySQLNonTransientConnectionException() {}
/*    */   
/*    */   public MySQLNonTransientConnectionException(String reason, String SQLState, int vendorCode) {
/* 36 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLNonTransientConnectionException(String reason, String SQLState) {
/* 40 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLNonTransientConnectionException(String reason) {
/* 44 */     super(reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLNonTransientConnectionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */