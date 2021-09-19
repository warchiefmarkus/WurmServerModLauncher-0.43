/*     */ package org.flywaydb.core.internal.dbsupport.mysql;
/*     */ 
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
/*     */ public class MySQLSqlStatementBuilder
/*     */   extends SqlStatementBuilder
/*     */ {
/*     */   private static final String DELIMITER_KEYWORD = "DELIMITER";
/*  32 */   private final String[] charSets = new String[] { "ARMSCII8", "ASCII", "BIG5", "BINARY", "CP1250", "CP1251", "CP1256", "CP1257", "CP850", "CP852", "CP866", "CP932", "DEC8", "EUCJPMS", "EUCKR", "GB2312", "GBK", "GEOSTD8", "GREEK", "HEBREW", "HP8", "KEYBCS2", "KOI8R", "KOI8U", "LATIN1", "LATIN2", "LATIN5", "LATIN7", "MACCE", "MACROMAN", "SJIS", "SWE7", "TIS620", "UCS2", "UJIS", "UTF8" };
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isInMultiLineCommentDirective = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Delimiter extractNewDelimiterFromLine(String line) {
/*  42 */     if (line.toUpperCase().startsWith("DELIMITER")) {
/*  43 */       return new Delimiter(line.substring("DELIMITER".length()).trim(), false);
/*     */     }
/*     */     
/*  46 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
/*  51 */     if (line.toUpperCase().startsWith("DELIMITER")) {
/*  52 */       return new Delimiter(line.substring("DELIMITER".length()).trim(), false);
/*     */     }
/*     */     
/*  55 */     return delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCommentDirective(String line) {
/*  61 */     if (line.matches("^" + Pattern.quote("/*!") + "\\d{5} .*" + Pattern.quote("*/") + "\\s*;?")) {
/*  62 */       return true;
/*     */     }
/*     */     
/*  65 */     if (line.matches("^" + Pattern.quote("/*!") + "\\d{5} .*")) {
/*  66 */       this.isInMultiLineCommentDirective = true;
/*  67 */       return true;
/*     */     } 
/*     */     
/*  70 */     if (this.isInMultiLineCommentDirective && line.matches(".*" + Pattern.quote("*/") + "\\s*;?")) {
/*  71 */       this.isInMultiLineCommentDirective = false;
/*  72 */       return true;
/*     */     } 
/*  74 */     return this.isInMultiLineCommentDirective;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSingleLineComment(String token) {
/*  79 */     return (token.startsWith("--") || (token.startsWith("#") && (!"#".equals(this.delimiter.getDelimiter()) || !"#".equals(token))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String removeEscapedQuotes(String token) {
/*  84 */     String noEscapedBackslashes = StringUtils.replaceAll(token, "\\\\", "");
/*  85 */     String noBackslashEscapes = StringUtils.replaceAll(StringUtils.replaceAll(noEscapedBackslashes, "\\'", ""), "\\\"", "");
/*  86 */     return StringUtils.replaceAll(noBackslashEscapes, "''", "").replace("'", " ' ");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String cleanToken(String token) {
/*  91 */     if (token.startsWith("B'") || token.startsWith("X'")) {
/*  92 */       return token.substring(token.indexOf("'"));
/*     */     }
/*     */     
/*  95 */     if (token.startsWith("_")) {
/*  96 */       for (String charSet : this.charSets) {
/*  97 */         String cast = "_" + charSet;
/*  98 */         if (token.startsWith(cast)) {
/*  99 */           return token.substring(cast.length());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 105 */     return token;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String extractAlternateOpenQuote(String token) {
/* 110 */     if (token.startsWith("\"")) {
/* 111 */       return "\"";
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\mysql\MySQLSqlStatementBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */