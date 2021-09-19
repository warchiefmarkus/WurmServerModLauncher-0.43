/*    */ package org.flywaydb.core.internal.dbsupport.db2zos;
/*    */ 
/*    */ import org.flywaydb.core.internal.dbsupport.Delimiter;
/*    */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
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
/*    */ public class DB2zosSqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/*    */   private boolean insideBeginEndBlock;
/* 34 */   private String statementStart = "";
/*    */ 
/*    */ 
/*    */   
/*    */   protected String cleanToken(String token) {
/* 39 */     if (token.startsWith("X'")) {
/* 40 */       return token.substring(token.indexOf("'"));
/*    */     }
/* 42 */     return super.cleanToken(token);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/* 47 */     if (StringUtils.countOccurrencesOf(this.statementStart, " ") < 4) {
/* 48 */       this.statementStart += line;
/* 49 */       this.statementStart += " ";
/*    */     } 
/*    */     
/* 52 */     if (this.statementStart.startsWith("CREATE FUNCTION") || this.statementStart
/* 53 */       .startsWith("CREATE PROCEDURE") || this.statementStart
/* 54 */       .startsWith("CREATE TRIGGER") || this.statementStart
/* 55 */       .startsWith("CREATE OR REPLACE FUNCTION") || this.statementStart
/* 56 */       .startsWith("CREATE OR REPLACE PROCEDURE") || this.statementStart
/* 57 */       .startsWith("CREATE OR REPLACE TRIGGER")) {
/* 58 */       if (line.startsWith("BEGIN")) {
/* 59 */         this.insideBeginEndBlock = true;
/*    */       }
/*    */       
/* 62 */       if (line.endsWith("END;")) {
/* 63 */         this.insideBeginEndBlock = false;
/*    */       }
/*    */     } 
/*    */     
/* 67 */     if (this.insideBeginEndBlock) {
/* 68 */       return null;
/*    */     }
/* 70 */     return getDefaultDelimiter();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2zos\DB2zosSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */