/*    */ package org.flywaydb.core.internal.dbsupport.db2;
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
/*    */ public class DB2SqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/*    */   private boolean insideBeginEndBlock;
/* 34 */   private String statementStart = "";
/*    */ 
/*    */   
/*    */   protected String cleanToken(String token) {
/* 38 */     if (token.startsWith("X'")) {
/* 39 */       return token.substring(token.indexOf("'"));
/*    */     }
/* 41 */     return super.cleanToken(token);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/* 46 */     if (StringUtils.countOccurrencesOf(this.statementStart, " ") < 4) {
/* 47 */       this.statementStart += line;
/* 48 */       this.statementStart += " ";
/*    */     } 
/*    */     
/* 51 */     if (this.statementStart.startsWith("CREATE FUNCTION") || this.statementStart
/* 52 */       .startsWith("CREATE PROCEDURE") || this.statementStart
/* 53 */       .startsWith("CREATE TRIGGER") || this.statementStart
/* 54 */       .startsWith("CREATE OR REPLACE FUNCTION") || this.statementStart
/* 55 */       .startsWith("CREATE OR REPLACE PROCEDURE") || this.statementStart
/* 56 */       .startsWith("CREATE OR REPLACE TRIGGER")) {
/* 57 */       if (line.startsWith("BEGIN")) {
/* 58 */         this.insideBeginEndBlock = true;
/*    */       }
/*    */       
/* 61 */       if (line.endsWith("END;")) {
/* 62 */         this.insideBeginEndBlock = false;
/*    */       }
/*    */     } 
/*    */     
/* 66 */     if (this.insideBeginEndBlock) {
/* 67 */       return null;
/*    */     }
/* 69 */     return getDefaultDelimiter();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2\DB2SqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */