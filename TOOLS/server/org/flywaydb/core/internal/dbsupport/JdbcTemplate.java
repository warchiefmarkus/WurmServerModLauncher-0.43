/*     */ package org.flywaydb.core.internal.dbsupport;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
/*     */ import org.flywaydb.core.internal.util.jdbc.RowMapper;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
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
/*     */ public class JdbcTemplate
/*     */ {
/*  39 */   private static final Log LOG = LogFactory.getLog(JdbcTemplate.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Connection connection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int nullType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JdbcTemplate(Connection connection, int nullType) {
/*  58 */     this.connection = connection;
/*  59 */     this.nullType = nullType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/*  66 */     return this.connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Map<String, String>> queryForList(String query, String... params) throws SQLException {
/*     */     List<Map<String, String>> result;
/*  78 */     PreparedStatement statement = null;
/*  79 */     ResultSet resultSet = null;
/*     */ 
/*     */     
/*     */     try {
/*  83 */       statement = this.connection.prepareStatement(query);
/*  84 */       for (int i = 0; i < params.length; i++) {
/*  85 */         statement.setString(i + 1, params[i]);
/*     */       }
/*  87 */       resultSet = statement.executeQuery();
/*     */       
/*  89 */       result = new ArrayList<Map<String, String>>();
/*  90 */       while (resultSet.next()) {
/*  91 */         Map<String, String> rowMap = new HashMap<String, String>();
/*  92 */         for (int j = 1; j <= resultSet.getMetaData().getColumnCount(); j++) {
/*  93 */           rowMap.put(resultSet.getMetaData().getColumnLabel(j), resultSet.getString(j));
/*     */         }
/*  95 */         result.add(rowMap);
/*     */       } 
/*     */     } finally {
/*  98 */       JdbcUtils.closeResultSet(resultSet);
/*  99 */       JdbcUtils.closeStatement(statement);
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> queryForStringList(String query, String... params) throws SQLException {
/*     */     List<String> result;
/* 115 */     PreparedStatement statement = null;
/* 116 */     ResultSet resultSet = null;
/*     */ 
/*     */     
/*     */     try {
/* 120 */       statement = this.connection.prepareStatement(query);
/* 121 */       for (int i = 0; i < params.length; i++) {
/* 122 */         statement.setString(i + 1, params[i]);
/*     */       }
/* 124 */       resultSet = statement.executeQuery();
/*     */       
/* 126 */       result = new ArrayList<String>();
/* 127 */       while (resultSet.next()) {
/* 128 */         result.add(resultSet.getString(1));
/*     */       }
/*     */     } finally {
/* 131 */       JdbcUtils.closeResultSet(resultSet);
/* 132 */       JdbcUtils.closeStatement(statement);
/*     */     } 
/*     */     
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int queryForInt(String query, String... params) throws SQLException {
/*     */     int result;
/* 147 */     PreparedStatement statement = null;
/* 148 */     ResultSet resultSet = null;
/*     */ 
/*     */     
/*     */     try {
/* 152 */       statement = this.connection.prepareStatement(query);
/* 153 */       for (int i = 0; i < params.length; i++) {
/* 154 */         statement.setString(i + 1, params[i]);
/*     */       }
/* 156 */       resultSet = statement.executeQuery();
/* 157 */       resultSet.next();
/* 158 */       result = resultSet.getInt(1);
/*     */     } finally {
/* 160 */       JdbcUtils.closeResultSet(resultSet);
/* 161 */       JdbcUtils.closeStatement(statement);
/*     */     } 
/*     */     
/* 164 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String queryForString(String query, String... params) throws SQLException {
/*     */     String result;
/* 176 */     PreparedStatement statement = null;
/* 177 */     ResultSet resultSet = null;
/*     */ 
/*     */     
/*     */     try {
/* 181 */       statement = this.connection.prepareStatement(query);
/* 182 */       for (int i = 0; i < params.length; i++) {
/* 183 */         statement.setString(i + 1, params[i]);
/*     */       }
/* 185 */       resultSet = statement.executeQuery();
/* 186 */       result = null;
/* 187 */       if (resultSet.next()) {
/* 188 */         result = resultSet.getString(1);
/*     */       }
/*     */     } finally {
/* 191 */       JdbcUtils.closeResultSet(resultSet);
/* 192 */       JdbcUtils.closeStatement(statement);
/*     */     } 
/*     */     
/* 195 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseMetaData getMetaData() throws SQLException {
/* 205 */     return this.connection.getMetaData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(String sql, Object... params) throws SQLException {
/* 216 */     PreparedStatement statement = null;
/*     */     try {
/* 218 */       statement = prepareStatement(sql, params);
/* 219 */       statement.execute();
/*     */     } finally {
/* 221 */       JdbcUtils.closeStatement(statement);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void executeStatement(String sql) throws SQLException {
/* 232 */     Statement statement = null;
/*     */     try {
/* 234 */       statement = this.connection.createStatement();
/* 235 */       statement.setEscapeProcessing(false);
/* 236 */       boolean hasResults = false;
/*     */       try {
/* 238 */         hasResults = statement.execute(sql);
/*     */       } finally {
/* 240 */         SQLWarning warning = statement.getWarnings();
/* 241 */         while (warning != null) {
/* 242 */           if ("00000".equals(warning.getSQLState())) {
/* 243 */             LOG.info("DB: " + warning.getMessage());
/*     */           } else {
/* 245 */             LOG.warn("DB: " + warning.getMessage() + " (SQL State: " + warning
/* 246 */                 .getSQLState() + " - Error Code: " + warning.getErrorCode() + ")");
/*     */           } 
/* 248 */           warning = warning.getNextWarning();
/*     */         } 
/*     */         
/* 251 */         int updateCount = -1;
/* 252 */         while (hasResults || (updateCount = statement.getUpdateCount()) != -1) {
/* 253 */           if (updateCount != -1) {
/* 254 */             LOG.debug("Update Count: " + updateCount);
/*     */           }
/* 256 */           hasResults = statement.getMoreResults();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 260 */       JdbcUtils.closeStatement(statement);
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
/*     */   public void update(String sql, Object... params) throws SQLException {
/* 272 */     PreparedStatement statement = null;
/*     */     try {
/* 274 */       statement = prepareStatement(sql, params);
/* 275 */       statement.executeUpdate();
/*     */     } finally {
/* 277 */       JdbcUtils.closeStatement(statement);
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
/*     */   
/*     */   private PreparedStatement prepareStatement(String sql, Object[] params) throws SQLException {
/* 290 */     PreparedStatement statement = this.connection.prepareStatement(sql);
/* 291 */     for (int i = 0; i < params.length; i++) {
/* 292 */       if (params[i] == null) {
/* 293 */         statement.setNull(i + 1, this.nullType);
/* 294 */       } else if (params[i] instanceof Integer) {
/* 295 */         statement.setInt(i + 1, ((Integer)params[i]).intValue());
/* 296 */       } else if (params[i] instanceof Boolean) {
/* 297 */         statement.setBoolean(i + 1, ((Boolean)params[i]).booleanValue());
/*     */       } else {
/* 299 */         statement.setString(i + 1, (String)params[i]);
/*     */       } 
/*     */     } 
/* 302 */     return statement;
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
/*     */   public <T> List<T> query(String query, RowMapper<T> rowMapper) throws SQLException {
/*     */     List<T> results;
/* 315 */     Statement statement = null;
/* 316 */     ResultSet resultSet = null;
/*     */ 
/*     */     
/*     */     try {
/* 320 */       statement = this.connection.createStatement();
/* 321 */       resultSet = statement.executeQuery(query);
/*     */       
/* 323 */       results = new ArrayList<T>();
/* 324 */       while (resultSet.next()) {
/* 325 */         results.add((T)rowMapper.mapRow(resultSet));
/*     */       }
/*     */     } finally {
/* 328 */       JdbcUtils.closeResultSet(resultSet);
/* 329 */       JdbcUtils.closeStatement(statement);
/*     */     } 
/*     */     
/* 332 */     return results;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\JdbcTemplate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */