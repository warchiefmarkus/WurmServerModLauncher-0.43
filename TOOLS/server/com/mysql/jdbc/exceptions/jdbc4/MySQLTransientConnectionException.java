/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import java.sql.SQLTransientConnectionException;
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
/*    */ public class MySQLTransientConnectionException
/*    */   extends SQLTransientConnectionException
/*    */ {
/*    */   public MySQLTransientConnectionException(String reason, String SQLState, int vendorCode) {
/* 32 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransientConnectionException(String reason, String SQLState) {
/* 36 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransientConnectionException(String reason) {
/* 40 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransientConnectionException() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLTransientConnectionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */