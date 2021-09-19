/*     */ package org.flywaydb.core.internal.util.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import javax.sql.DataSource;
/*     */ import org.flywaydb.core.api.FlywayException;
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
/*     */ public class JdbcUtils
/*     */ {
/*  32 */   private static final Log LOG = LogFactory.getLog(JdbcUtils.class);
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
/*     */   public static Connection openConnection(DataSource dataSource) throws FlywayException {
/*     */     try {
/*  50 */       Connection connection = dataSource.getConnection();
/*  51 */       if (connection == null) {
/*  52 */         throw new FlywayException("Unable to obtain Jdbc connection from DataSource");
/*     */       }
/*  54 */       return connection;
/*  55 */     } catch (SQLException e) {
/*  56 */       throw new FlywayException("Unable to obtain Jdbc connection from DataSource", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeConnection(Connection connection) {
/*  66 */     if (connection == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  71 */       connection.close();
/*  72 */     } catch (SQLException e) {
/*  73 */       LOG.error("Error while closing Jdbc connection", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeStatement(Statement statement) {
/*  83 */     if (statement == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  88 */       statement.close();
/*  89 */     } catch (SQLException e) {
/*  90 */       LOG.error("Error while closing Jdbc statement", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeResultSet(ResultSet resultSet) {
/* 100 */     if (resultSet == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 105 */       resultSet.close();
/* 106 */     } catch (SQLException e) {
/* 107 */       LOG.error("Error while closing Jdbc resultSet", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\jdbc\JdbcUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */