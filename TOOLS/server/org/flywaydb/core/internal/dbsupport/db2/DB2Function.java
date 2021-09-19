/*    */ package org.flywaydb.core.internal.dbsupport.db2;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
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
/*    */ public class DB2Function
/*    */   extends Function
/*    */ {
/* 36 */   private static final Collection<String> typesWithLength = Arrays.asList(new String[] { "character", "char", "varchar", "graphic", "vargraphic", "decimal", "float", "varbinary" });
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
/*    */   public DB2Function(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name, String... args) {
/* 56 */     super(jdbcTemplate, dbSupport, schema, name, args);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/*    */     try {
/* 62 */       this.jdbcTemplate.execute("DROP FUNCTION " + this.dbSupport
/* 63 */           .quote(new String[] { this.schema.getName(), this.name }, ) + "(" + 
/* 64 */           argsToCommaSeparatedString(this.args) + ")", new Object[0]);
/* 65 */     } catch (SQLException e) {
/*    */ 
/*    */       
/* 68 */       this.jdbcTemplate.execute("DROP FUNCTION " + this.dbSupport.quote(new String[] { this.schema.getName(), this.name }, ), new Object[0]);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String argsToCommaSeparatedString(String[] args) {
/* 80 */     StringBuilder buf = new StringBuilder();
/* 81 */     for (String arg : args) {
/* 82 */       if (buf.length() > 0) {
/* 83 */         buf.append(",");
/*    */       }
/* 85 */       buf.append(arg);
/* 86 */       if (typesWithLength.contains(arg.toLowerCase())) {
/* 87 */         buf.append("()");
/*    */       }
/*    */     } 
/* 90 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return super.toString() + "(" + StringUtils.arrayToCommaDelimitedString((Object[])this.args) + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2\DB2Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */