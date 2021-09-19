/*    */ package org.flywaydb.core.internal.dbsupport.sqlserver;
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
/*    */ public class SQLServerSqlStatementBuilder
/*    */   extends SqlStatementBuilder
/*    */ {
/* 31 */   private static final Pattern KEYWORDS_BEFORE_STRING_LITERAL_REGEX = Pattern.compile("^(LIKE)('.*)");
/*    */ 
/*    */   
/*    */   protected Delimiter getDefaultDelimiter() {
/* 35 */     return new Delimiter("GO", true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String cleanToken(String token) {
/* 40 */     if (token.startsWith("N'")) {
/* 41 */       return token.substring(token.indexOf("'"));
/*    */     }
/*    */     
/* 44 */     Matcher beforeMatcher = KEYWORDS_BEFORE_STRING_LITERAL_REGEX.matcher(token);
/* 45 */     if (beforeMatcher.find()) {
/* 46 */       token = beforeMatcher.group(2);
/*    */     }
/*    */     
/* 49 */     return token;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sqlserver\SQLServerSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */