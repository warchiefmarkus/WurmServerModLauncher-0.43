/*     */ package org.flywaydb.core.internal.dbsupport;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.util.PlaceholderReplacer;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*     */ import org.flywaydb.core.internal.util.scanner.Resource;
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
/*     */ public class SqlScript
/*     */ {
/*  38 */   private static final Log LOG = LogFactory.getLog(SqlScript.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final DbSupport dbSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<SqlStatement> sqlStatements;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Resource resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlScript(String sqlScriptSource, DbSupport dbSupport) {
/*  62 */     this.dbSupport = dbSupport;
/*  63 */     this.sqlStatements = parse(sqlScriptSource);
/*  64 */     this.resource = null;
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
/*     */   public SqlScript(DbSupport dbSupport, Resource sqlScriptResource, PlaceholderReplacer placeholderReplacer, String encoding) {
/*  76 */     this.dbSupport = dbSupport;
/*     */     
/*  78 */     String sqlScriptSource = sqlScriptResource.loadAsString(encoding);
/*  79 */     this.sqlStatements = parse(placeholderReplacer.replacePlaceholders(sqlScriptSource));
/*     */     
/*  81 */     this.resource = sqlScriptResource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SqlStatement> getSqlStatements() {
/*  90 */     return this.sqlStatements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource() {
/*  97 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(JdbcTemplate jdbcTemplate) {
/* 106 */     for (SqlStatement sqlStatement : this.sqlStatements) {
/* 107 */       String sql = sqlStatement.getSql();
/* 108 */       LOG.debug("Executing SQL: " + sql);
/*     */       
/*     */       try {
/* 111 */         if (sqlStatement.isPgCopy()) {
/* 112 */           this.dbSupport.executePgCopy(jdbcTemplate.getConnection(), sql); continue;
/*     */         } 
/* 114 */         jdbcTemplate.executeStatement(sql);
/*     */       }
/* 116 */       catch (SQLException e) {
/* 117 */         throw new FlywaySqlScriptException(this.resource, sqlStatement, e);
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
/*     */   List<SqlStatement> parse(String sqlScriptSource) {
/* 130 */     return linesToStatements(readLines(new StringReader(sqlScriptSource)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<SqlStatement> linesToStatements(List<String> lines) {
/* 141 */     List<SqlStatement> statements = new ArrayList<SqlStatement>();
/*     */     
/* 143 */     Delimiter nonStandardDelimiter = null;
/* 144 */     SqlStatementBuilder sqlStatementBuilder = this.dbSupport.createSqlStatementBuilder();
/*     */     
/* 146 */     int lineNumber = 1; while (true) { String line; if (lineNumber <= lines.size())
/* 147 */       { line = lines.get(lineNumber - 1);
/*     */         
/* 149 */         if (sqlStatementBuilder.isEmpty())
/* 150 */         { if (StringUtils.hasText(line))
/*     */           
/*     */           { 
/*     */ 
/*     */             
/* 155 */             Delimiter newDelimiter = sqlStatementBuilder.extractNewDelimiterFromLine(line);
/* 156 */             if (newDelimiter != null)
/* 157 */             { nonStandardDelimiter = newDelimiter;
/*     */                }
/*     */             
/*     */             else
/*     */             
/* 162 */             { sqlStatementBuilder.setLineNumber(lineNumber);
/*     */ 
/*     */               
/* 165 */               if (nonStandardDelimiter != null) {
/* 166 */                 sqlStatementBuilder.setDelimiter(nonStandardDelimiter);
/*     */               }
/*     */ 
/*     */               
/* 170 */               sqlStatementBuilder.addLine(line); }  }  continue; }  } else { break; }  sqlStatementBuilder.addLine(line);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       lineNumber++; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     if (!sqlStatementBuilder.isEmpty()) {
/* 185 */       statements.add(sqlStatementBuilder.getSqlStatement());
/*     */     }
/*     */     
/* 188 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> readLines(Reader reader) {
/* 199 */     List<String> lines = new ArrayList<String>();
/*     */     
/* 201 */     BufferedReader bufferedReader = new BufferedReader(reader);
/*     */     
/*     */     try {
/*     */       String line;
/* 205 */       while ((line = bufferedReader.readLine()) != null) {
/* 206 */         lines.add(line);
/*     */       }
/* 208 */     } catch (IOException e) {
/*     */ 
/*     */       
/* 211 */       String message = (this.resource == null) ? "Unable to parse lines" : ("Unable to parse " + this.resource.getLocation() + " (" + this.resource.getLocationOnDisk() + ")");
/* 212 */       throw new FlywayException(message, e);
/*     */     } 
/*     */     
/* 215 */     return lines;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\SqlScript.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */