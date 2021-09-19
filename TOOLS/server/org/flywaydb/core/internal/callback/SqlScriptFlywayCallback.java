/*     */ package org.flywaydb.core.internal.callback;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.MigrationInfo;
/*     */ import org.flywaydb.core.api.callback.FlywayCallback;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlScript;
/*     */ import org.flywaydb.core.internal.util.Location;
/*     */ import org.flywaydb.core.internal.util.Locations;
/*     */ import org.flywaydb.core.internal.util.PlaceholderReplacer;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*     */ import org.flywaydb.core.internal.util.scanner.Resource;
/*     */ import org.flywaydb.core.internal.util.scanner.Scanner;
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
/*     */ public class SqlScriptFlywayCallback
/*     */   implements FlywayCallback
/*     */ {
/*  42 */   private static final Log LOG = LogFactory.getLog(SqlScriptFlywayCallback.class);
/*     */   
/*     */   private static final String BEFORE_CLEAN = "beforeClean";
/*     */   private static final String AFTER_CLEAN = "afterClean";
/*     */   private static final String BEFORE_MIGRATE = "beforeMigrate";
/*     */   private static final String AFTER_MIGRATE = "afterMigrate";
/*     */   private static final String BEFORE_EACH_MIGRATE = "beforeEachMigrate";
/*     */   private static final String AFTER_EACH_MIGRATE = "afterEachMigrate";
/*     */   private static final String BEFORE_VALIDATE = "beforeValidate";
/*     */   private static final String AFTER_VALIDATE = "afterValidate";
/*     */   private static final String BEFORE_BASELINE = "beforeBaseline";
/*     */   private static final String AFTER_BASELINE = "afterBaseline";
/*     */   private static final String BEFORE_REPAIR = "beforeRepair";
/*     */   private static final String AFTER_REPAIR = "afterRepair";
/*     */   private static final String BEFORE_INFO = "beforeInfo";
/*     */   private static final String AFTER_INFO = "afterInfo";
/*  58 */   public static final List<String> ALL_CALLBACKS = Arrays.asList(new String[] { "beforeClean", "afterClean", "beforeMigrate", "beforeEachMigrate", "afterEachMigrate", "afterMigrate", "beforeValidate", "afterValidate", "beforeBaseline", "afterBaseline", "beforeRepair", "afterRepair", "beforeInfo", "afterInfo" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final Map<String, SqlScript> scripts = new HashMap<String, SqlScript>();
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
/*     */   public SqlScriptFlywayCallback(DbSupport dbSupport, Scanner scanner, Locations locations, PlaceholderReplacer placeholderReplacer, String encoding, String sqlMigrationSuffix) {
/*  80 */     for (String callback : ALL_CALLBACKS) {
/*  81 */       this.scripts.put(callback, null);
/*     */     }
/*     */     
/*  84 */     LOG.debug("Scanning for SQL callbacks ...");
/*  85 */     for (Location location : locations.getLocations()) {
/*     */       Resource[] resources;
/*     */       try {
/*  88 */         resources = scanner.scanForResources(location, "", sqlMigrationSuffix);
/*  89 */       } catch (FlywayException e) {
/*     */         continue;
/*     */       } 
/*     */       
/*  93 */       for (Resource resource : resources) {
/*  94 */         String key = resource.getFilename().replace(sqlMigrationSuffix, "");
/*  95 */         if (this.scripts.keySet().contains(key)) {
/*  96 */           SqlScript existing = this.scripts.get(key);
/*  97 */           if (existing != null) {
/*  98 */             throw new FlywayException("Found more than 1 SQL callback script for " + key + "!\n" + "Offenders:\n" + "-> " + existing
/*     */                 
/* 100 */                 .getResource().getLocationOnDisk() + "\n" + "-> " + resource
/* 101 */                 .getLocationOnDisk());
/*     */           }
/* 103 */           this.scripts.put(key, new SqlScript(dbSupport, resource, placeholderReplacer, encoding));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeClean(Connection connection) {
/* 111 */     execute("beforeClean", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterClean(Connection connection) {
/* 116 */     execute("afterClean", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeMigrate(Connection connection) {
/* 121 */     execute("beforeMigrate", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterMigrate(Connection connection) {
/* 126 */     execute("afterMigrate", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeEachMigrate(Connection connection, MigrationInfo info) {
/* 131 */     execute("beforeEachMigrate", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterEachMigrate(Connection connection, MigrationInfo info) {
/* 136 */     execute("afterEachMigrate", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeValidate(Connection connection) {
/* 141 */     execute("beforeValidate", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterValidate(Connection connection) {
/* 146 */     execute("afterValidate", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeBaseline(Connection connection) {
/* 151 */     execute("beforeBaseline", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterBaseline(Connection connection) {
/* 156 */     execute("afterBaseline", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeRepair(Connection connection) {
/* 161 */     execute("beforeRepair", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterRepair(Connection connection) {
/* 166 */     execute("afterRepair", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeInfo(Connection connection) {
/* 171 */     execute("beforeInfo", connection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterInfo(Connection connection) {
/* 176 */     execute("afterInfo", connection);
/*     */   }
/*     */   
/*     */   private void execute(String key, Connection connection) {
/* 180 */     SqlScript sqlScript = this.scripts.get(key);
/* 181 */     if (sqlScript != null) {
/* 182 */       LOG.info("Executing SQL callback: " + key);
/* 183 */       sqlScript.execute(new JdbcTemplate(connection, 0));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\callback\SqlScriptFlywayCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */