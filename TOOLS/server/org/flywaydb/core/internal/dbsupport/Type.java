/*    */ package org.flywaydb.core.internal.dbsupport;
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
/*    */ public abstract class Type
/*    */   extends SchemaObject
/*    */ {
/*    */   public Type(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/* 31 */     super(jdbcTemplate, dbSupport, schema, name);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\Type.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */