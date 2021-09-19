/*    */ package org.flywaydb.core.internal.dbsupport;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.api.FlywayException;
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
/*    */ public abstract class SchemaObject
/*    */ {
/*    */   protected final JdbcTemplate jdbcTemplate;
/*    */   protected final DbSupport dbSupport;
/*    */   protected final Schema schema;
/*    */   protected final String name;
/*    */   
/*    */   public SchemaObject(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 55 */     this.name = name;
/* 56 */     this.jdbcTemplate = jdbcTemplate;
/* 57 */     this.dbSupport = dbSupport;
/* 58 */     this.schema = schema;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Schema getSchema() {
/* 65 */     return this.schema;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 72 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void drop() {
/*    */     try {
/* 80 */       doDrop();
/* 81 */     } catch (SQLException e) {
/* 82 */       throw new FlywayException("Unable to drop " + this, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void doDrop() throws SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return this.dbSupport.quote(new String[] { this.schema.getName(), this.name });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\SchemaObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */