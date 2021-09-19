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
/*    */ public class SqlStatement
/*    */ {
/*    */   private int lineNumber;
/*    */   private String sql;
/*    */   private boolean pgCopy;
/*    */   
/*    */   public SqlStatement(int lineNumber, String sql, boolean pgCopy) {
/* 47 */     this.lineNumber = lineNumber;
/* 48 */     this.sql = sql;
/* 49 */     this.pgCopy = pgCopy;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLineNumber() {
/* 56 */     return this.lineNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSql() {
/* 63 */     return this.sql;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPgCopy() {
/* 70 */     return this.pgCopy;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\SqlStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */