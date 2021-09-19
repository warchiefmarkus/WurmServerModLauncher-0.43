/*    */ package com.mysql.jdbc.integration.jboss;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.jboss.resource.adapter.jdbc.vendor.MySQLExceptionSorter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ExtendedMysqlExceptionSorter
/*    */   extends MySQLExceptionSorter
/*    */ {
/*    */   public boolean isExceptionFatal(SQLException ex) {
/* 44 */     String sqlState = ex.getSQLState();
/*    */     
/* 46 */     if (sqlState != null && sqlState.startsWith("08")) {
/* 47 */       return true;
/*    */     }
/*    */     
/* 50 */     return super.isExceptionFatal(ex);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\integration\jboss\ExtendedMysqlExceptionSorter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */