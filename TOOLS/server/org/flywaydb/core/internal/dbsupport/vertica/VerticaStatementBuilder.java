/*    */ package org.flywaydb.core.internal.dbsupport.vertica;
/*    */ 
/*    */ import org.flywaydb.core.internal.dbsupport.Delimiter;
/*    */ import org.flywaydb.core.internal.dbsupport.postgresql.PostgreSQLSqlStatementBuilder;
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
/*    */ public class VerticaStatementBuilder
/*    */   extends PostgreSQLSqlStatementBuilder
/*    */ {
/*    */   private boolean insideBeginEndBlock;
/* 35 */   private String statementStart = "";
/*    */ 
/*    */   
/*    */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/* 39 */     if (StringUtils.countOccurrencesOf(this.statementStart, " ") < 4) {
/* 40 */       this.statementStart += line;
/* 41 */       this.statementStart += " ";
/*    */     } 
/*    */     
/* 44 */     if (this.statementStart.startsWith("CREATE FUNCTION") || this.statementStart
/* 45 */       .startsWith("CREATE OR REPLACE FUNCTION")) {
/* 46 */       if (line.startsWith("BEGIN") || line.endsWith("BEGIN")) {
/* 47 */         this.insideBeginEndBlock = true;
/*    */       }
/*    */       
/* 50 */       if (line.endsWith("END;")) {
/* 51 */         this.insideBeginEndBlock = false;
/*    */       }
/*    */     } 
/*    */     
/* 55 */     if (this.insideBeginEndBlock) {
/* 56 */       return null;
/*    */     }
/* 58 */     return getDefaultDelimiter();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\vertica\VerticaStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */