/*    */ package org.flywaydb.core.internal.dbsupport.solid;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*    */ import org.flywaydb.core.internal.dbsupport.Schema;
/*    */ import org.flywaydb.core.internal.dbsupport.Table;
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
/*    */ public class SolidTable
/*    */   extends Table
/*    */ {
/*    */   public SolidTable(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 36 */     super(jdbcTemplate, dbSupport, schema, name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doExists() throws SQLException {
/* 41 */     return exists(null, this.schema, this.name, new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLock() throws SQLException {
/* 46 */     this.jdbcTemplate.execute("LOCK TABLE " + this + " IN EXCLUSIVE MODE", new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 51 */     this.jdbcTemplate.execute("DROP TABLE " + this, new Object[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\solid\SolidTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */