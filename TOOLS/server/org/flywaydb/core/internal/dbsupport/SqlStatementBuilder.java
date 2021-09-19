/*     */ package org.flywaydb.core.internal.dbsupport;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public class SqlStatementBuilder
/*     */ {
/*  30 */   private StringBuilder statement = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int lineNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean empty = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean terminated;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean insideQuoteStringLiteral = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean insideAlternateQuoteStringLiteral = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String alternateQuote;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean lineEndsWithSingleLineComment = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean insideMultiLineComment = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean nonCommentStatementPartSeen = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected Delimiter delimiter = getDefaultDelimiter();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Delimiter getDefaultDelimiter() {
/*  86 */     return new Delimiter(";", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineNumber(int lineNumber) {
/*  93 */     this.lineNumber = lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelimiter(Delimiter delimiter) {
/* 100 */     this.delimiter = delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 109 */     return this.empty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/* 118 */     return this.terminated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStatement getSqlStatement() {
/* 125 */     String sql = this.statement.toString();
/* 126 */     return new SqlStatement(this.lineNumber, sql, isPgCopyFromStdIn());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Delimiter extractNewDelimiterFromLine(String line) {
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPgCopyFromStdIn() {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCommentDirective(String line) {
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSingleLineComment(String line) {
/* 166 */     return line.startsWith("--");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLine(String line) {
/* 175 */     if (isEmpty()) {
/* 176 */       this.empty = false;
/*     */     } else {
/* 178 */       this.statement.append("\n");
/*     */     } 
/*     */     
/* 181 */     if (isCommentDirective(line.trim())) {
/* 182 */       this.nonCommentStatementPartSeen = true;
/*     */     }
/*     */     
/* 185 */     String lineSimplified = simplifyLine(line);
/*     */     
/* 187 */     applyStateChanges(lineSimplified);
/* 188 */     if (endWithOpenMultilineStringLiteral() || this.insideMultiLineComment) {
/* 189 */       this.statement.append(line);
/*     */       
/*     */       return;
/*     */     } 
/* 193 */     this.delimiter = changeDelimiterIfNecessary(lineSimplified, this.delimiter);
/*     */     
/* 195 */     this.statement.append(line);
/*     */     
/* 197 */     if (!this.lineEndsWithSingleLineComment && lineTerminatesStatement(lineSimplified, this.delimiter)) {
/* 198 */       stripDelimiter(this.statement, this.delimiter);
/* 199 */       this.terminated = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean endWithOpenMultilineStringLiteral() {
/* 208 */     return (this.insideQuoteStringLiteral || this.insideAlternateQuoteStringLiteral);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDiscard() {
/* 215 */     return (!this.insideAlternateQuoteStringLiteral && !this.insideQuoteStringLiteral && !this.insideMultiLineComment && !this.nonCommentStatementPartSeen);
/*     */   }
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
/*     */   protected String simplifyLine(String line) {
/* 229 */     return removeEscapedQuotes(line).replace("--", " -- ").replace("/*", " /* ").replace("*/", " */ ").replaceAll("\\s+", " ").trim().toUpperCase();
/*     */   }
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
/*     */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/* 242 */     return delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean lineTerminatesStatement(String line, Delimiter delimiter) {
/* 253 */     if (delimiter == null) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     String upperCaseDelimiter = delimiter.getDelimiter().toUpperCase();
/*     */     
/* 259 */     if (delimiter.isAloneOnLine()) {
/* 260 */       return line.equals(upperCaseDelimiter);
/*     */     }
/*     */     
/* 263 */     return line.endsWith(upperCaseDelimiter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void stripDelimiter(StringBuilder sql, Delimiter delimiter) {
/*     */     int last;
/* 276 */     for (last = sql.length(); last > 0 && 
/* 277 */       Character.isWhitespace(sql.charAt(last - 1)); last--);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     sql.delete(last - delimiter.getDelimiter().length(), sql.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String extractAlternateOpenQuote(String token) {
/* 292 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String computeAlternateCloseQuote(String openQuote) {
/* 302 */     return openQuote;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyStateChanges(String line) {
/* 312 */     String[] tokens = StringUtils.tokenizeToStringArray(line, " @<>;:=|(),+{}");
/*     */     
/* 314 */     List<TokenType> delimitingTokens = extractStringLiteralDelimitingTokens(tokens);
/*     */     
/* 316 */     this.lineEndsWithSingleLineComment = false;
/* 317 */     for (TokenType delimitingToken : delimitingTokens) {
/* 318 */       if (!this.insideQuoteStringLiteral && !this.insideAlternateQuoteStringLiteral && TokenType.MULTI_LINE_COMMENT_OPEN
/* 319 */         .equals(delimitingToken)) {
/* 320 */         this.insideMultiLineComment = true;
/*     */       }
/* 322 */       if (!this.insideQuoteStringLiteral && !this.insideAlternateQuoteStringLiteral && TokenType.MULTI_LINE_COMMENT_CLOSE
/* 323 */         .equals(delimitingToken)) {
/* 324 */         this.insideMultiLineComment = false;
/*     */       }
/*     */       
/* 327 */       if (!this.insideQuoteStringLiteral && !this.insideAlternateQuoteStringLiteral && !this.insideMultiLineComment && TokenType.SINGLE_LINE_COMMENT
/* 328 */         .equals(delimitingToken)) {
/* 329 */         this.lineEndsWithSingleLineComment = true;
/*     */         
/*     */         return;
/*     */       } 
/* 333 */       if (!this.insideMultiLineComment && !this.insideQuoteStringLiteral && TokenType.ALTERNATE_QUOTE
/* 334 */         .equals(delimitingToken)) {
/* 335 */         this.insideAlternateQuoteStringLiteral = !this.insideAlternateQuoteStringLiteral;
/*     */       }
/*     */       
/* 338 */       if (!this.insideMultiLineComment && !this.insideAlternateQuoteStringLiteral && TokenType.QUOTE
/* 339 */         .equals(delimitingToken)) {
/* 340 */         this.insideQuoteStringLiteral = !this.insideQuoteStringLiteral;
/*     */       }
/*     */       
/* 343 */       if (!this.insideMultiLineComment && !this.insideQuoteStringLiteral && !this.insideAlternateQuoteStringLiteral && TokenType.OTHER
/* 344 */         .equals(delimitingToken)) {
/* 345 */         this.nonCommentStatementPartSeen = true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<TokenType> extractStringLiteralDelimitingTokens(String[] tokens) {
/* 358 */     List<TokenType> delimitingTokens = new ArrayList<TokenType>();
/* 359 */     for (String token : tokens) {
/* 360 */       String cleanToken = cleanToken(token);
/* 361 */       boolean handled = false;
/*     */       
/* 363 */       if (this.alternateQuote == null) {
/* 364 */         String alternateQuoteFromToken = extractAlternateOpenQuote(cleanToken);
/* 365 */         if (alternateQuoteFromToken != null) {
/* 366 */           String closeQuote = computeAlternateCloseQuote(alternateQuoteFromToken);
/* 367 */           if (cleanToken.length() < alternateQuoteFromToken.length() + closeQuote.length() || 
/* 368 */             !cleanToken.startsWith(alternateQuoteFromToken) || !cleanToken.endsWith(closeQuote)) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 373 */             this.alternateQuote = closeQuote;
/* 374 */             delimitingTokens.add(TokenType.ALTERNATE_QUOTE);
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/* 379 */       if (this.alternateQuote != null && cleanToken.endsWith(this.alternateQuote)) {
/* 380 */         this.alternateQuote = null;
/* 381 */         delimitingTokens.add(TokenType.ALTERNATE_QUOTE);
/*     */         
/*     */         continue;
/*     */       } 
/* 385 */       if (cleanToken.length() >= 2 && cleanToken.startsWith("'") && cleanToken.endsWith("'")) {
/*     */         continue;
/*     */       }
/*     */       
/* 389 */       if (cleanToken.length() >= 4) {
/* 390 */         int numberOfOpeningMultiLineComments = StringUtils.countOccurrencesOf(cleanToken, "/*");
/* 391 */         int numberOfClosingMultiLineComments = StringUtils.countOccurrencesOf(cleanToken, "*/");
/* 392 */         if (numberOfOpeningMultiLineComments > 0 && numberOfOpeningMultiLineComments == numberOfClosingMultiLineComments) {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 398 */       if (isSingleLineComment(cleanToken)) {
/* 399 */         delimitingTokens.add(TokenType.SINGLE_LINE_COMMENT);
/* 400 */         handled = true;
/*     */       } 
/*     */       
/* 403 */       if (cleanToken.contains("/*")) {
/* 404 */         delimitingTokens.add(TokenType.MULTI_LINE_COMMENT_OPEN);
/* 405 */         handled = true;
/* 406 */       } else if (cleanToken.startsWith("'")) {
/* 407 */         delimitingTokens.add(TokenType.QUOTE);
/* 408 */         handled = true;
/*     */       } 
/*     */       
/* 411 */       if (!cleanToken.contains("/*") && cleanToken.contains("*/")) {
/* 412 */         delimitingTokens.add(TokenType.MULTI_LINE_COMMENT_CLOSE);
/* 413 */         handled = true;
/* 414 */       } else if (!cleanToken.startsWith("'") && cleanToken.endsWith("'")) {
/* 415 */         delimitingTokens.add(TokenType.QUOTE);
/* 416 */         handled = true;
/*     */       } 
/*     */       
/* 419 */       if (!handled) {
/* 420 */         delimitingTokens.add(TokenType.OTHER);
/*     */       }
/*     */       continue;
/*     */     } 
/* 424 */     return delimitingTokens;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String removeEscapedQuotes(String token) {
/* 434 */     return StringUtils.replaceAll(token, "''", "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String cleanToken(String token) {
/* 445 */     return token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum TokenType
/*     */   {
/* 455 */     OTHER,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 460 */     QUOTE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 465 */     ALTERNATE_QUOTE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 470 */     SINGLE_LINE_COMMENT,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 475 */     MULTI_LINE_COMMENT_OPEN,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 480 */     MULTI_LINE_COMMENT_CLOSE;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\SqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */