/*    */ package org.flywaydb.core.internal.dbsupport.postgresql;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class PostgreSQLSqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/* 31 */   private static final Delimiter COPY_DELIMITER = new Delimiter("\\.", true);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static final String DOLLAR_QUOTE_REGEX = "(\\$[A-Za-z0-9_]*\\$).*";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean firstLine = true;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String copyStatement;
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean pgCopy;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String extractAlternateOpenQuote(String token) {
/* 56 */     Matcher matcher = Pattern.compile("(\\$[A-Za-z0-9_]*\\$).*").matcher(token);
/* 57 */     if (matcher.find()) {
/* 58 */       return matcher.group(1);
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/* 65 */     if (this.pgCopy) {
/* 66 */       return COPY_DELIMITER;
/*    */     }
/*    */     
/* 69 */     if (this.firstLine) {
/* 70 */       this.firstLine = false;
/* 71 */       if (line.matches("COPY|COPY\\s.*")) {
/* 72 */         this.copyStatement = line;
/*    */       }
/* 74 */     } else if (this.copyStatement != null) {
/* 75 */       this.copyStatement += " " + line;
/*    */     } 
/*    */     
/* 78 */     if (this.copyStatement != null && this.copyStatement.contains(" FROM STDIN")) {
/* 79 */       this.pgCopy = true;
/* 80 */       return COPY_DELIMITER;
/*    */     } 
/*    */     
/* 83 */     return delimiter;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPgCopyFromStdIn() {
/* 88 */     return this.pgCopy;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String cleanToken(String token) {
/* 93 */     if (token.startsWith("E'")) {
/* 94 */       return token.substring(token.indexOf("'"));
/*    */     }
/*    */     
/* 97 */     return token;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\postgresql\PostgreSQLSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */