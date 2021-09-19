/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import java.sql.SQLDataException;
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
/*    */ public class MySQLDataException
/*    */   extends SQLDataException
/*    */ {
/*    */   public MySQLDataException() {}
/*    */   
/*    */   public MySQLDataException(String reason, String SQLState, int vendorCode) {
/* 35 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLDataException(String reason, String SQLState) {
/* 39 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLDataException(String reason) {
/* 43 */     super(reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLDataException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */