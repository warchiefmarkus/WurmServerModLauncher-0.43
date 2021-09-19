/*    */ package com.mysql.jdbc;
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
/*    */ class OperationNotSupportedException
/*    */   extends SQLException
/*    */ {
/*    */   OperationNotSupportedException() {
/* 28 */     super(Messages.getString("RowDataDynamic.10"), "S1009");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\OperationNotSupportedException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */