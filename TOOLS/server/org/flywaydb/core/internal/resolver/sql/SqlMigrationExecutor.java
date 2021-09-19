/*    */ package org.flywaydb.core.internal.resolver.sql;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import org.flywaydb.core.api.resolver.MigrationExecutor;
/*    */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*    */ import org.flywaydb.core.internal.dbsupport.SqlScript;
/*    */ import org.flywaydb.core.internal.util.PlaceholderReplacer;
/*    */ import org.flywaydb.core.internal.util.scanner.Resource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SqlMigrationExecutor
/*    */   implements MigrationExecutor
/*    */ {
/*    */   private final DbSupport dbSupport;
/*    */   private final PlaceholderReplacer placeholderReplacer;
/*    */   private final Resource sqlScriptResource;
/*    */   private final String encoding;
/*    */   
/*    */   public SqlMigrationExecutor(DbSupport dbSupport, Resource sqlScriptResource, PlaceholderReplacer placeholderReplacer, String encoding) {
/* 62 */     this.dbSupport = dbSupport;
/* 63 */     this.sqlScriptResource = sqlScriptResource;
/* 64 */     this.encoding = encoding;
/* 65 */     this.placeholderReplacer = placeholderReplacer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(Connection connection) {
/* 70 */     SqlScript sqlScript = new SqlScript(this.dbSupport, this.sqlScriptResource, this.placeholderReplacer, this.encoding);
/* 71 */     sqlScript.execute(new JdbcTemplate(connection, 0));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean executeInTransaction() {
/* 76 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\sql\SqlMigrationExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */