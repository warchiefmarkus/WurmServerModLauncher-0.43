/*    */ package org.flywaydb.core.internal.dbsupport.h2;
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
/*    */ public class H2Table
/*    */   extends Table
/*    */ {
/*    */   public H2Table(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 38 */     super(jdbcTemplate, dbSupport, schema, name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 43 */     this.jdbcTemplate.execute("DROP TABLE " + this.dbSupport.quote(new String[] { this.schema.getName(), this.name }, ) + " CASCADE", new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doExists() throws SQLException {
/* 48 */     return exists(null, this.schema, this.name, new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLock() throws SQLException {
/* 53 */     this.jdbcTemplate.execute("select * from " + this + " for update", new Object[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\h2\H2Table.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */