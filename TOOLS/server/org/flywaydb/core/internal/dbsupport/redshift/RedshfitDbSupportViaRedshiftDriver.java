/*    */ package org.flywaydb.core.internal.dbsupport.redshift;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
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
/*    */ public class RedshfitDbSupportViaRedshiftDriver
/*    */   extends RedshiftDbSupport
/*    */ {
/*    */   public RedshfitDbSupportViaRedshiftDriver(Connection connection) {
/* 31 */     super(new JdbcTemplate(connection, 12));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\redshift\RedshfitDbSupportViaRedshiftDriver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */