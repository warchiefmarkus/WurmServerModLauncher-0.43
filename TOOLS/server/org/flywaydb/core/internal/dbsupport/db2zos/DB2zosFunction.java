/*    */ package org.flywaydb.core.internal.dbsupport.db2zos;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*    */ import org.flywaydb.core.internal.dbsupport.Function;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*    */ import org.flywaydb.core.internal.dbsupport.Schema;
/*    */ import org.flywaydb.core.internal.util.StringUtils;
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
/*    */ public class DB2zosFunction
/*    */   extends Function
/*    */ {
/*    */   public DB2zosFunction(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name, String... args) {
/* 41 */     super(jdbcTemplate, dbSupport, schema, name, args);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 46 */     this.jdbcTemplate.execute("DROP FUNCTION " + this.dbSupport
/* 47 */         .quote(new String[] { this.schema.getName(), this.name }, ) + "(" + 
/* 48 */         StringUtils.arrayToCommaDelimitedString((Object[])this.args) + ")", new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return super.toString() + "(" + StringUtils.arrayToCommaDelimitedString((Object[])this.args) + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2zos\DB2zosFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */