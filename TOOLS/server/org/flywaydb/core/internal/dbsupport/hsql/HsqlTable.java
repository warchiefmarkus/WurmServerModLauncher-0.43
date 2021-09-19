/*    */ package org.flywaydb.core.internal.dbsupport.hsql;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.api.FlywayException;
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
/*    */ public class HsqlTable
/*    */   extends Table
/*    */ {
/* 32 */   private static final Log LOG = LogFactory.getLog(HsqlDbSupport.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean version18;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HsqlTable(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 48 */     super(jdbcTemplate, dbSupport, schema, name);
/*    */     
/*    */     try {
/* 51 */       int majorVersion = jdbcTemplate.getMetaData().getDatabaseMajorVersion();
/* 52 */       this.version18 = (majorVersion < 2);
/* 53 */     } catch (SQLException e) {
/* 54 */       throw new FlywayException("Unable to determine the Hsql version", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 60 */     this.jdbcTemplate.execute("DROP TABLE " + this.dbSupport.quote(new String[] { this.schema.getName(), this.name }, ) + " CASCADE", new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doExists() throws SQLException {
/* 65 */     return exists(null, this.schema, this.name, new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLock() throws SQLException {
/* 70 */     if (this.version18) {
/* 71 */       LOG.debug("Unable to lock " + this + " as Hsql 1.8 does not support locking. No concurrent migration supported.");
/*    */     } else {
/* 73 */       this.jdbcTemplate.execute("LOCK TABLE " + this + " WRITE", new Object[0]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\hsql\HsqlTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */