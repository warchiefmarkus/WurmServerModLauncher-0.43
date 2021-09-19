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
/*    */ public class MySQLInvalidAuthorizationSpecException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   public MySQLInvalidAuthorizationSpecException() {}
/*    */   
/*    */   public MySQLInvalidAuthorizationSpecException(String reason, String SQLState, int vendorCode) {
/* 34 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLInvalidAuthorizationSpecException(String reason, String SQLState) {
/* 38 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLInvalidAuthorizationSpecException(String reason) {
/* 42 */     super(reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\exceptions\MySQLInvalidAuthorizationSpecException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */