/*     */ package org.flywaydb.core.internal.dbsupport;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
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
/*     */ public abstract class Table
/*     */   extends SchemaObject
/*     */ {
/*  30 */   private static final Log LOG = LogFactory.getLog(Table.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Table(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
/*  41 */     super(jdbcTemplate, dbSupport, schema, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*     */     try {
/*  51 */       return doExists();
/*  52 */     } catch (SQLException e) {
/*  53 */       throw new FlywayException("Unable to check whether table " + this + " exists", e);
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
/*     */   protected abstract boolean doExists() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean exists(Schema catalog, Schema schema, String table, String... tableTypes) throws SQLException {
/*     */     boolean found;
/*  76 */     String[] types = tableTypes;
/*  77 */     if (types.length == 0) {
/*  78 */       types = null;
/*     */     }
/*     */     
/*  81 */     ResultSet resultSet = null;
/*     */     
/*     */     try {
/*  84 */       resultSet = this.jdbcTemplate.getMetaData().getTables((catalog == null) ? null : catalog
/*  85 */           .getName(), (schema == null) ? null : schema
/*  86 */           .getName(), table, types);
/*     */ 
/*     */       
/*  89 */       found = resultSet.next();
/*     */     } finally {
/*  91 */       JdbcUtils.closeResultSet(resultSet);
/*     */     } 
/*     */     
/*  94 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrimaryKey() {
/*     */     boolean found;
/* 103 */     ResultSet resultSet = null;
/*     */     
/*     */     try {
/* 106 */       if (this.dbSupport.catalogIsSchema()) {
/* 107 */         resultSet = this.jdbcTemplate.getMetaData().getPrimaryKeys(this.schema.getName(), null, this.name);
/*     */       } else {
/* 109 */         resultSet = this.jdbcTemplate.getMetaData().getPrimaryKeys(null, this.schema.getName(), this.name);
/*     */       } 
/* 111 */       found = resultSet.next();
/* 112 */     } catch (SQLException e) {
/* 113 */       throw new FlywayException("Unable to check whether table " + this + " has a primary key", e);
/*     */     } finally {
/* 115 */       JdbcUtils.closeResultSet(resultSet);
/*     */     } 
/*     */     
/* 118 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasColumn(String column) {
/*     */     boolean found;
/* 128 */     ResultSet resultSet = null;
/*     */     
/*     */     try {
/* 131 */       if (this.dbSupport.catalogIsSchema()) {
/* 132 */         resultSet = this.jdbcTemplate.getMetaData().getColumns(this.schema.getName(), null, this.name, column);
/*     */       } else {
/* 134 */         resultSet = this.jdbcTemplate.getMetaData().getColumns(null, this.schema.getName(), this.name, column);
/*     */       } 
/* 136 */       found = resultSet.next();
/* 137 */     } catch (SQLException e) {
/* 138 */       throw new FlywayException("Unable to check whether table " + this + " has a column named " + column, e);
/*     */     } finally {
/* 140 */       JdbcUtils.closeResultSet(resultSet);
/*     */     } 
/*     */     
/* 143 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnSize(String column) {
/*     */     int columnSize;
/* 153 */     ResultSet resultSet = null;
/*     */     
/*     */     try {
/* 156 */       if (this.dbSupport.catalogIsSchema()) {
/* 157 */         resultSet = this.jdbcTemplate.getMetaData().getColumns(this.schema.getName(), null, this.name, column);
/*     */       } else {
/* 159 */         resultSet = this.jdbcTemplate.getMetaData().getColumns(null, this.schema.getName(), this.name, column);
/*     */       } 
/* 161 */       resultSet.next();
/* 162 */       columnSize = resultSet.getInt("COLUMN_SIZE");
/* 163 */     } catch (SQLException e) {
/* 164 */       throw new FlywayException("Unable to check the size of column " + column + " in table " + this, e);
/*     */     } finally {
/* 166 */       JdbcUtils.closeResultSet(resultSet);
/*     */     } 
/*     */     
/* 169 */     return columnSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lock() {
/*     */     try {
/* 177 */       LOG.debug("Locking table " + this + "...");
/* 178 */       doLock();
/* 179 */       LOG.debug("Lock acquired for table " + this);
/* 180 */     } catch (SQLException e) {
/* 181 */       throw new FlywayException("Unable to lock table " + this, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void doLock() throws SQLException;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\Table.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */