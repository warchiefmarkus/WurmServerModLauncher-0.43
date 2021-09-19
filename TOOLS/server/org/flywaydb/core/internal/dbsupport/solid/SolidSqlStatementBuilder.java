/*    */ package org.flywaydb.core.internal.dbsupport.solid;
/*    */ 
/*    */ import org.flywaydb.core.internal.dbsupport.Delimiter;
/*    */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
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
/*    */ public class SolidSqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/*    */   public Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/* 33 */     if (line.startsWith("\"")) {
/* 34 */       return new Delimiter("\"", false);
/*    */     }
/* 36 */     if (line.endsWith("\";")) {
/* 37 */       return getDefaultDelimiter();
/*    */     }
/* 39 */     return delimiter;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\solid\SolidSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */