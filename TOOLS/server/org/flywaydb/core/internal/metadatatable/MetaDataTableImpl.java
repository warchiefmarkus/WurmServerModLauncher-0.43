/*     */ package org.flywaydb.core.internal.metadatatable;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.MigrationType;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlScript;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
/*     */ import org.flywaydb.core.internal.util.PlaceholderReplacer;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
/*     */ import org.flywaydb.core.internal.util.jdbc.RowMapper;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*     */ import org.flywaydb.core.internal.util.scanner.classpath.ClassPathResource;
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
/*     */ public class MetaDataTableImpl
/*     */   implements MetaDataTable
/*     */ {
/*  37 */   private static final Log LOG = LogFactory.getLog(MetaDataTableImpl.class);
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
/*     */   private final Table table;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final JdbcTemplate jdbcTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetaDataTableImpl(DbSupport dbSupport, Table table) {
/*  61 */     this.jdbcTemplate = dbSupport.getJdbcTemplate();
/*  62 */     this.dbSupport = dbSupport;
/*  63 */     this.table = table;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean upgradeIfNecessary() {
/*  68 */     if (this.table.exists() && this.table.hasColumn("version_rank")) {
/*  69 */       LOG.info("Upgrading metadata table " + this.table + " to the Flyway 4.0 format ...");
/*  70 */       String resourceName = "org/flywaydb/core/internal/dbsupport/" + this.dbSupport.getDbName() + "/upgradeMetaDataTable.sql";
/*  71 */       String source = (new ClassPathResource(resourceName, getClass().getClassLoader())).loadAsString("UTF-8");
/*     */       
/*  73 */       Map<String, String> placeholders = new HashMap<String, String>();
/*  74 */       placeholders.put("schema", this.table.getSchema().getName());
/*  75 */       placeholders.put("table", this.table.getName());
/*  76 */       String sourceNoPlaceholders = (new PlaceholderReplacer(placeholders, "${", "}")).replacePlaceholders(source);
/*     */       
/*  78 */       SqlScript sqlScript = new SqlScript(sourceNoPlaceholders, this.dbSupport);
/*  79 */       sqlScript.execute(this.jdbcTemplate);
/*  80 */       return true;
/*     */     } 
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createIfNotExists() {
/*  89 */     if (this.table.exists()) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     LOG.info("Creating Metadata table: " + this.table);
/*     */     
/*  95 */     String resourceName = "org/flywaydb/core/internal/dbsupport/" + this.dbSupport.getDbName() + "/createMetaDataTable.sql";
/*  96 */     String source = (new ClassPathResource(resourceName, getClass().getClassLoader())).loadAsString("UTF-8");
/*     */     
/*  98 */     Map<String, String> placeholders = new HashMap<String, String>();
/*  99 */     placeholders.put("schema", this.table.getSchema().getName());
/* 100 */     placeholders.put("table", this.table.getName());
/* 101 */     String sourceNoPlaceholders = (new PlaceholderReplacer(placeholders, "${", "}")).replacePlaceholders(source);
/*     */     
/* 103 */     SqlScript sqlScript = new SqlScript(sourceNoPlaceholders, this.dbSupport);
/* 104 */     sqlScript.execute(this.jdbcTemplate);
/*     */     
/* 106 */     LOG.debug("Metadata table " + this.table + " created.");
/*     */   }
/*     */ 
/*     */   
/*     */   public void lock() {
/* 111 */     createIfNotExists();
/* 112 */     this.table.lock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAppliedMigration(AppliedMigration appliedMigration) {
/* 117 */     createIfNotExists();
/*     */     
/* 119 */     MigrationVersion version = appliedMigration.getVersion();
/*     */     
/*     */     try {
/* 122 */       String versionStr = (version == null) ? null : version.toString();
/*     */ 
/*     */       
/* 125 */       String resourceName = "org/flywaydb/core/internal/dbsupport/" + this.dbSupport.getDbName() + "/updateMetaDataTable.sql";
/* 126 */       ClassPathResource classPathResource = new ClassPathResource(resourceName, getClass().getClassLoader());
/* 127 */       if (classPathResource.exists()) {
/* 128 */         String source = classPathResource.loadAsString("UTF-8");
/* 129 */         Map<String, String> placeholders = new HashMap<String, String>();
/*     */ 
/*     */         
/* 132 */         placeholders.put("schema", this.table.getSchema().getName());
/* 133 */         placeholders.put("table", this.table.getName());
/*     */ 
/*     */         
/* 136 */         placeholders.put("installed_rank_val", String.valueOf(calculateInstalledRank()));
/* 137 */         placeholders.put("version_val", versionStr);
/* 138 */         placeholders.put("description_val", appliedMigration.getDescription());
/* 139 */         placeholders.put("type_val", appliedMigration.getType().name());
/* 140 */         placeholders.put("script_val", appliedMigration.getScript());
/* 141 */         placeholders.put("checksum_val", String.valueOf(appliedMigration.getChecksum()));
/* 142 */         placeholders.put("installed_by_val", this.dbSupport.getCurrentUserFunction());
/* 143 */         placeholders.put("execution_time_val", String.valueOf(appliedMigration.getExecutionTime() * 1000L));
/* 144 */         placeholders.put("success_val", String.valueOf(appliedMigration.isSuccess()));
/*     */         
/* 146 */         String sourceNoPlaceholders = (new PlaceholderReplacer(placeholders, "${", "}")).replacePlaceholders(source);
/*     */         
/* 148 */         SqlScript sqlScript = new SqlScript(sourceNoPlaceholders, this.dbSupport);
/*     */         
/* 150 */         sqlScript.execute(this.jdbcTemplate);
/*     */       } else {
/*     */         
/* 153 */         this.jdbcTemplate.update("INSERT INTO " + this.table + " (" + this.dbSupport
/* 154 */             .quote(new String[] { "installed_rank" }, ) + "," + this.dbSupport
/* 155 */             .quote(new String[] { "version" }, ) + "," + this.dbSupport
/* 156 */             .quote(new String[] { "description" }, ) + "," + this.dbSupport
/* 157 */             .quote(new String[] { "type" }, ) + "," + this.dbSupport
/* 158 */             .quote(new String[] { "script" }, ) + "," + this.dbSupport
/* 159 */             .quote(new String[] { "checksum" }, ) + "," + this.dbSupport
/* 160 */             .quote(new String[] { "installed_by" }, ) + "," + this.dbSupport
/* 161 */             .quote(new String[] { "execution_time" }, ) + "," + this.dbSupport
/* 162 */             .quote(new String[] { "success" }, ) + ")" + " VALUES (?, ?, ?, ?, ?, ?, " + this.dbSupport
/*     */             
/* 164 */             .getCurrentUserFunction() + ", ?, ?)", new Object[] {
/* 165 */               Integer.valueOf(calculateInstalledRank()), versionStr, appliedMigration
/*     */               
/* 167 */               .getDescription(), appliedMigration
/* 168 */               .getType().name(), appliedMigration
/* 169 */               .getScript(), appliedMigration
/* 170 */               .getChecksum(), 
/* 171 */               Integer.valueOf(appliedMigration.getExecutionTime()), 
/* 172 */               Boolean.valueOf(appliedMigration.isSuccess())
/*     */             });
/*     */       } 
/*     */       
/* 176 */       LOG.debug("MetaData table " + this.table + " successfully updated to reflect changes");
/* 177 */     } catch (SQLException e) {
/* 178 */       throw new FlywayException("Unable to insert row for version '" + version + "' in metadata table " + this.table, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateInstalledRank() throws SQLException {
/* 188 */     int currentMax = this.jdbcTemplate.queryForInt("SELECT MAX(" + this.dbSupport.quote(new String[] { "installed_rank" }, ) + ")" + " FROM " + this.table, new String[0]);
/*     */     
/* 190 */     return currentMax + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<AppliedMigration> allAppliedMigrations() {
/* 195 */     return findAppliedMigrations(new MigrationType[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<AppliedMigration> findAppliedMigrations(MigrationType... migrationTypes) {
/* 205 */     if (!this.table.exists()) {
/* 206 */       return new ArrayList<AppliedMigration>();
/*     */     }
/*     */     
/* 209 */     createIfNotExists();
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
/* 220 */     String query = "SELECT " + this.dbSupport.quote(new String[] { "installed_rank" }) + "," + this.dbSupport.quote(new String[] { "version" }) + "," + this.dbSupport.quote(new String[] { "description" }) + "," + this.dbSupport.quote(new String[] { "type" }) + "," + this.dbSupport.quote(new String[] { "script" }) + "," + this.dbSupport.quote(new String[] { "checksum" }) + "," + this.dbSupport.quote(new String[] { "installed_on" }) + "," + this.dbSupport.quote(new String[] { "installed_by" }) + "," + this.dbSupport.quote(new String[] { "execution_time" }) + "," + this.dbSupport.quote(new String[] { "success" }) + " FROM " + this.table;
/*     */ 
/*     */     
/* 223 */     if (migrationTypes.length > 0) {
/* 224 */       query = query + " WHERE " + this.dbSupport.quote(new String[] { "type" }) + " IN (";
/* 225 */       for (int i = 0; i < migrationTypes.length; i++) {
/* 226 */         if (i > 0) {
/* 227 */           query = query + ",";
/*     */         }
/* 229 */         query = query + "'" + migrationTypes[i] + "'";
/*     */       } 
/* 231 */       query = query + ")";
/*     */     } 
/*     */     
/* 234 */     query = query + " ORDER BY " + this.dbSupport.quote(new String[] { "installed_rank" });
/*     */     
/*     */     try {
/* 237 */       return this.jdbcTemplate.query(query, new RowMapper<AppliedMigration>() {
/*     */             public AppliedMigration mapRow(ResultSet rs) throws SQLException {
/* 239 */               Integer checksum = Integer.valueOf(rs.getInt("checksum"));
/* 240 */               if (rs.wasNull()) {
/* 241 */                 checksum = null;
/*     */               }
/*     */               
/* 244 */               return new AppliedMigration(rs
/* 245 */                   .getInt("installed_rank"), 
/* 246 */                   (rs.getString("version") != null) ? MigrationVersion.fromVersion(rs.getString("version")) : null, rs
/* 247 */                   .getString("description"), 
/* 248 */                   MigrationType.valueOf(rs.getString("type")), rs
/* 249 */                   .getString("script"), checksum, rs
/*     */                   
/* 251 */                   .getTimestamp("installed_on"), rs
/* 252 */                   .getString("installed_by"), rs
/* 253 */                   .getInt("execution_time"), rs
/* 254 */                   .getBoolean("success"));
/*     */             }
/*     */           });
/*     */     }
/* 258 */     catch (SQLException e) {
/* 259 */       throw new FlywayException("Error while retrieving the list of applied migrations from metadata table " + this.table, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBaselineMarker(MigrationVersion baselineVersion, String baselineDescription) {
/* 266 */     addAppliedMigration(new AppliedMigration(baselineVersion, baselineDescription, MigrationType.BASELINE, baselineDescription, null, 0, true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeFailedMigrations() {
/* 272 */     if (!this.table.exists()) {
/* 273 */       LOG.info("Repair of failed migration in metadata table " + this.table + " not necessary. No failed migration detected.");
/*     */       
/*     */       return;
/*     */     } 
/* 277 */     createIfNotExists();
/*     */     
/*     */     try {
/* 280 */       int failedCount = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM " + this.table + " WHERE " + this.dbSupport
/* 281 */           .quote(new String[] { "success" }, ) + "=" + this.dbSupport.getBooleanFalse(), new String[0]);
/* 282 */       if (failedCount == 0) {
/* 283 */         LOG.info("Repair of failed migration in metadata table " + this.table + " not necessary. No failed migration detected.");
/*     */         return;
/*     */       } 
/* 286 */     } catch (SQLException e) {
/* 287 */       throw new FlywayException("Unable to check the metadata table " + this.table + " for failed migrations", e);
/*     */     } 
/*     */     
/*     */     try {
/* 291 */       this.jdbcTemplate.execute("DELETE FROM " + this.table + " WHERE " + this.dbSupport
/* 292 */           .quote(new String[] { "success" }, ) + " = " + this.dbSupport.getBooleanFalse(), new Object[0]);
/* 293 */     } catch (SQLException e) {
/* 294 */       throw new FlywayException("Unable to repair metadata table " + this.table, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSchemasMarker(Schema[] schemas) {
/* 300 */     createIfNotExists();
/*     */     
/* 302 */     addAppliedMigration(new AppliedMigration(MigrationVersion.fromVersion("0"), "<< Flyway Schema Creation >>", MigrationType.SCHEMA, 
/* 303 */           StringUtils.arrayToCommaDelimitedString((Object[])schemas), null, 0, true));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSchemasMarker() {
/* 308 */     if (!this.table.exists()) {
/* 309 */       return false;
/*     */     }
/*     */     
/* 312 */     createIfNotExists();
/*     */     
/*     */     try {
/* 315 */       int count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM " + this.table + " WHERE " + this.dbSupport
/* 316 */           .quote(new String[] { "type" }, ) + "='SCHEMA'", new String[0]);
/* 317 */       return (count > 0);
/* 318 */     } catch (SQLException e) {
/* 319 */       throw new FlywayException("Unable to check whether the metadata table " + this.table + " has a schema marker migration", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasBaselineMarker() {
/* 325 */     if (!this.table.exists()) {
/* 326 */       return false;
/*     */     }
/*     */     
/* 329 */     createIfNotExists();
/*     */     
/*     */     try {
/* 332 */       int count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM " + this.table + " WHERE " + this.dbSupport
/* 333 */           .quote(new String[] { "type" }, ) + "='INIT' OR " + this.dbSupport.quote(new String[] { "type" }, ) + "='BASELINE'", new String[0]);
/* 334 */       return (count > 0);
/* 335 */     } catch (SQLException e) {
/* 336 */       throw new FlywayException("Unable to check whether the metadata table " + this.table + " has an baseline marker migration", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AppliedMigration getBaselineMarker() {
/* 342 */     List<AppliedMigration> appliedMigrations = findAppliedMigrations(new MigrationType[] { MigrationType.BASELINE });
/* 343 */     return appliedMigrations.isEmpty() ? null : appliedMigrations.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAppliedMigrations() {
/* 348 */     if (!this.table.exists()) {
/* 349 */       return false;
/*     */     }
/*     */     
/* 352 */     createIfNotExists();
/*     */     
/*     */     try {
/* 355 */       int count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM " + this.table + " WHERE " + this.dbSupport
/* 356 */           .quote(new String[] { "type" }, ) + " NOT IN ('SCHEMA', 'INIT', 'BASELINE')", new String[0]);
/* 357 */       return (count > 0);
/* 358 */     } catch (SQLException e) {
/* 359 */       throw new FlywayException("Unable to check whether the metadata table " + this.table + " has applied migrations", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateChecksum(MigrationVersion version, Integer checksum) {
/* 365 */     LOG.info("Updating checksum of " + version + " to " + checksum + " ...");
/*     */ 
/*     */     
/*     */     try {
/* 369 */       String resourceName = "org/flywaydb/core/internal/dbsupport/" + this.dbSupport.getDbName() + "/updateChecksum.sql";
/* 370 */       String source = (new ClassPathResource(resourceName, getClass().getClassLoader())).loadAsString("UTF-8");
/* 371 */       Map<String, String> placeholders = new HashMap<String, String>();
/*     */ 
/*     */       
/* 374 */       placeholders.put("schema", this.table.getSchema().getName());
/* 375 */       placeholders.put("table", this.table.getName());
/*     */ 
/*     */       
/* 378 */       placeholders.put("version_val", version.toString());
/* 379 */       placeholders.put("checksum_val", String.valueOf(checksum));
/*     */       
/* 381 */       String sourceNoPlaceholders = (new PlaceholderReplacer(placeholders, "${", "}")).replacePlaceholders(source);
/*     */       
/* 383 */       SqlScript sqlScript = new SqlScript(sourceNoPlaceholders, this.dbSupport);
/*     */       
/* 385 */       sqlScript.execute(this.jdbcTemplate);
/*     */     }
/* 387 */     catch (FlywayException fe) {
/*     */       try {
/* 389 */         this.jdbcTemplate.update("UPDATE " + this.table + " SET " + this.dbSupport.quote(new String[] { "checksum" }, ) + "=" + checksum + " WHERE " + this.dbSupport
/* 390 */             .quote(new String[] { "version" }, ) + "='" + version + "'", new Object[0]);
/* 391 */       } catch (SQLException e) {
/* 392 */         throw new FlywayException("Unable to update checksum in metadata table " + this.table + " for version " + version + " to " + checksum, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 400 */     return this.table.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\metadatatable\MetaDataTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */