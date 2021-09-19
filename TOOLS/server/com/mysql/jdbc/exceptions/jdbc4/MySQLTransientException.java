/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import java.sql.SQLTransientException;
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
/*    */ public class MySQLTransientException
/*    */   extends SQLTransientException
/*    */ {
/*    */   public MySQLTransientException(String reason, String SQLState, int vendorCode) {
/* 32 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransientException(String reason, String SQLState) {
/* 36 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransientException(String reason) {
/* 40 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransientException() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLTransientException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */