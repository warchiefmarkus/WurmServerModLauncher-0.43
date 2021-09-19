/*    */ package org.flywaydb.core.internal.dbsupport.phoenix;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*    */ import org.flywaydb.core.internal.dbsupport.Schema;
/*    */ import org.flywaydb.core.internal.dbsupport.Table;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogFactory;
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
/*    */ public class PhoenixTable
/*    */   extends Table
/*    */ {
/* 32 */   private static final Log LOG = LogFactory.getLog(PhoenixTable.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PhoenixTable(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 42 */     super(jdbcTemplate, dbSupport, schema, name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 47 */     this.jdbcTemplate.execute("DROP TABLE " + this.dbSupport.quote(new String[] { this.schema.getName(), this.name }, ), new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doExists() throws SQLException {
/* 52 */     ResultSet rs = this.jdbcTemplate.getMetaData().getTables(null, this.schema.getName(), this.name, new String[] { "TABLE" });
/* 53 */     if (rs.next()) {
/* 54 */       String tableName = rs.getString("TABLE_NAME");
/* 55 */       if (tableName != null) {
/* 56 */         return tableName.equals(this.name);
/*    */       }
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLock() throws SQLException {
/* 64 */     LOG.debug("Unable to lock " + this + " as Phoenix does not support locking. No concurrent migration supported.");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\phoenix\PhoenixTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */