/*    */ package com.mysql.jdbc.exceptions;
/*    */ 
/*    */ import java.sql.SQLException;
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
/*    */ public class MySQLNonTransientException
/*    */   extends SQLException
/*    */ {
/*    */   public MySQLNonTransientException() {}
/*    */   
/*    */   public MySQLNonTransientException(String reason, String SQLState, int vendorCode) {
/* 35 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLNonTransientException(String reason, String SQLState) {
/* 39 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLNonTransientException(String reason) {
/* 43 */     super(reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\MySQLNonTransientException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */