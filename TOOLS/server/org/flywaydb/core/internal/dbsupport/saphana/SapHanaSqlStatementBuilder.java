/*    */ package org.flywaydb.core.internal.dbsupport.saphana;
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
/*    */ public class SapHanaSqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/*    */   private boolean insideBeginEndBlock;
/* 34 */   private String statementStart = "";
/*    */ 
/*    */   
/*    */   protected String cleanToken(String token) {
/* 38 */     if (token.startsWith("N'") || token.startsWith("X'") || token
/* 39 */       .startsWith("DATE'") || token.startsWith("TIME'") || token.startsWith("TIMESTAMP'")) {
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
/* 52 */     if (this.statementStart.startsWith("CREATE TRIGGER")) {
/* 53 */       if (line.startsWith("BEGIN")) {
/* 54 */         this.insideBeginEndBlock = true;
/*    */       }
/*    */       
/* 57 */       if (line.endsWith("END;")) {
/* 58 */         this.insideBeginEndBlock = false;
/*    */       }
/*    */     } 
/*    */     
/* 62 */     if (this.insideBeginEndBlock) {
/* 63 */       return null;
/*    */     }
/* 65 */     return getDefaultDelimiter();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\saphana\SapHanaSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */