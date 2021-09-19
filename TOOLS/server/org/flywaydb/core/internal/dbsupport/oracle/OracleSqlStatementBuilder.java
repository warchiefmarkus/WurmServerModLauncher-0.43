/*     */ package org.flywaydb.core.internal.dbsupport.oracle;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.flywaydb.core.internal.dbsupport.Delimiter;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OracleSqlStatementBuilder
/*     */   extends SqlStatementBuilder
/*     */ {
/*  32 */   private static final Pattern KEYWORDS_BEFORE_STRING_LITERAL_REGEX = Pattern.compile("^(N|IF|ELSIF|SELECT|IMMEDIATE|RETURN|IS)('.*)");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   private static final Pattern KEYWORDS_AFTER_STRING_LITERAL_REGEX = Pattern.compile("(.*')(USING|THEN|FROM|AND|OR)(?!.)");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final Delimiter PLSQL_DELIMITER = new Delimiter("/", true);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   private String statementStart = "";
/*     */ 
/*     */   
/*     */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/*  51 */     if (line.matches("DECLARE|DECLARE\\s.*") || line.matches("BEGIN|BEGIN\\s.*")) {
/*  52 */       return PLSQL_DELIMITER;
/*     */     }
/*     */     
/*  55 */     if (StringUtils.countOccurrencesOf(this.statementStart, " ") < 8) {
/*  56 */       this.statementStart += line;
/*  57 */       this.statementStart += " ";
/*  58 */       this.statementStart = this.statementStart.replaceAll("\\s+", " ");
/*     */     } 
/*     */     
/*  61 */     if (this.statementStart.matches("CREATE( OR REPLACE)? (FUNCTION|PROCEDURE|PACKAGE|TYPE|TRIGGER).*") || this.statementStart
/*  62 */       .matches("CREATE( OR REPLACE)?( AND (RESOLVE|COMPILE))?( NOFORCE)? JAVA (SOURCE|RESOURCE|CLASS).*")) {
/*  63 */       return PLSQL_DELIMITER;
/*     */     }
/*     */     
/*  66 */     return delimiter;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String cleanToken(String token) {
/*  71 */     if (token.startsWith("'") && token.endsWith("'")) {
/*  72 */       return token;
/*     */     }
/*     */     
/*  75 */     Matcher beforeMatcher = KEYWORDS_BEFORE_STRING_LITERAL_REGEX.matcher(token);
/*  76 */     if (beforeMatcher.find()) {
/*  77 */       token = beforeMatcher.group(2);
/*     */     }
/*     */     
/*  80 */     Matcher afterMatcher = KEYWORDS_AFTER_STRING_LITERAL_REGEX.matcher(token);
/*  81 */     if (afterMatcher.find()) {
/*  82 */       token = afterMatcher.group(1);
/*     */     }
/*     */     
/*  85 */     return token;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String simplifyLine(String line) {
/*  90 */     String simplifiedQQuotes = StringUtils.replaceAll(StringUtils.replaceAll(line, "q'(", "q'["), ")'", "]'");
/*  91 */     return super.simplifyLine(simplifiedQQuotes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String extractAlternateOpenQuote(String token) {
/*  96 */     if (token.startsWith("Q'") && token.length() >= 3) {
/*  97 */       return token.substring(0, 3);
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String computeAlternateCloseQuote(String openQuote) {
/* 104 */     char specialChar = openQuote.charAt(2);
/* 105 */     switch (specialChar) {
/*     */       case '[':
/* 107 */         return "]'";
/*     */       case '(':
/* 109 */         return ")'";
/*     */       case '{':
/* 111 */         return "}'";
/*     */       case '<':
/* 113 */         return ">'";
/*     */     } 
/* 115 */     return specialChar + "'";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDiscard() {
/* 121 */     return (super.canDiscard() || this.statementStart.startsWith("SET DEFINE OFF"));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\oracle\OracleSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */