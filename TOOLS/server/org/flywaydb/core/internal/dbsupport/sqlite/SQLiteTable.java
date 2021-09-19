/*    */ package org.flywaydb.core.internal.dbsupport.sqlite;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
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
/*    */ public class SQLiteTable
/*    */   extends Table
/*    */ {
/* 33 */   private static final Log LOG = LogFactory.getLog(SQLiteTable.class);
/*    */ 
/*    */ 
/*    */   
/* 37 */   private static final Collection<String> SYSTEM_TABLES = Collections.singleton("sqlite_sequence");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean undroppable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLiteTable(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 50 */     super(jdbcTemplate, dbSupport, schema, name);
/* 51 */     this.undroppable = SYSTEM_TABLES.contains(name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 56 */     if (this.undroppable) {
/* 57 */       LOG.debug("SQLite system table " + this + " cannot be dropped. Ignoring.");
/*    */     } else {
/* 59 */       this.jdbcTemplate.execute("DROP TABLE " + this.dbSupport.quote(new String[] { this.schema.getName(), this.name }, ), new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doExists() throws SQLException {
/* 65 */     return (this.jdbcTemplate.queryForInt("SELECT count(tbl_name) FROM " + this.dbSupport
/* 66 */         .quote(new String[] { this.schema.getName() }, ) + ".sqlite_master WHERE type='table' AND tbl_name='" + this.name + "'", new String[0]) > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLock() throws SQLException {
/* 71 */     LOG.debug("Unable to lock " + this + " as SQLite does not support locking. No concurrent migration supported.");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sqlite\SQLiteTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */