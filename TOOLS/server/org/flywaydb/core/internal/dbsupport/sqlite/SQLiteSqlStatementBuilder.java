/*    */ package org.flywaydb.core.internal.dbsupport.sqlite;
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
/*    */ public class SQLiteSqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/* 29 */   private String statementStart = "";
/*    */ 
/*    */   
/*    */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/* 33 */     if (StringUtils.countOccurrencesOf(this.statementStart, " ") < 8) {
/* 34 */       this.statementStart += line;
/* 35 */       this.statementStart += " ";
/* 36 */       this.statementStart = this.statementStart.replaceAll("\\s+", " ");
/*    */     } 
/* 38 */     boolean createTriggerStatement = this.statementStart.matches("CREATE( TEMP| TEMPORARY)? TRIGGER.*");
/*    */     
/* 40 */     if (createTriggerStatement && !line.endsWith("END;")) {
/* 41 */       return null;
/*    */     }
/* 43 */     return getDefaultDelimiter();
/*    */   }
/*    */ 
/*    */   
/*    */   protected String cleanToken(String token) {
/* 48 */     if (token.startsWith("X'"))
/*    */     {
/* 50 */       return token.substring(token.indexOf("'"));
/*    */     }
/* 52 */     return token;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sqlite\SQLiteSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */