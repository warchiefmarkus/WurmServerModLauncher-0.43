/*    */ package org.flywaydb.core.internal.dbsupport.sybase.ase;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SybaseASETable
/*    */   extends Table
/*    */ {
/*    */   public SybaseASETable(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 44 */     super(jdbcTemplate, dbSupport, schema, name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doExists() throws SQLException {
/* 49 */     return exists(null, getSchema(), getName(), new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLock() throws SQLException {
/* 54 */     this.jdbcTemplate.execute("LOCK TABLE " + this + " IN EXCLUSIVE MODE WAIT 10", new Object[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 60 */     this.jdbcTemplate.execute("DROP TABLE " + getName(), new Object[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sybase\ase\SybaseASETable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */