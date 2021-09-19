/*     */ package org.flywaydb.core.internal.resolver.sql;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.zip.CRC32;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.MigrationType;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*     */ import org.flywaydb.core.api.resolver.ResolvedMigration;
/*     */ import org.flywaydb.core.internal.callback.SqlScriptFlywayCallback;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.resolver.MigrationInfoHelper;
/*     */ import org.flywaydb.core.internal.resolver.ResolvedMigrationComparator;
/*     */ import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;
/*     */ import org.flywaydb.core.internal.util.Location;
/*     */ import org.flywaydb.core.internal.util.Pair;
/*     */ import org.flywaydb.core.internal.util.PlaceholderReplacer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SqlMigrationResolver
/*     */   implements MigrationResolver
/*     */ {
/*     */   private final DbSupport dbSupport;
/*     */   private final Scanner scanner;
/*     */   private final Location location;
/*     */   private final PlaceholderReplacer placeholderReplacer;
/*     */   private final String encoding;
/*     */   private final String sqlMigrationPrefix;
/*     */   private final String repeatableSqlMigrationPrefix;
/*     */   private final String sqlMigrationSeparator;
/*     */   private final String sqlMigrationSuffix;
/*     */   
/*     */   public SqlMigrationResolver(DbSupport dbSupport, Scanner scanner, Location location, PlaceholderReplacer placeholderReplacer, String encoding, String sqlMigrationPrefix, String repeatableSqlMigrationPrefix, String sqlMigrationSeparator, String sqlMigrationSuffix) {
/* 109 */     this.dbSupport = dbSupport;
/* 110 */     this.scanner = scanner;
/* 111 */     this.location = location;
/* 112 */     this.placeholderReplacer = placeholderReplacer;
/* 113 */     this.encoding = encoding;
/* 114 */     this.sqlMigrationPrefix = sqlMigrationPrefix;
/* 115 */     this.repeatableSqlMigrationPrefix = repeatableSqlMigrationPrefix;
/* 116 */     this.sqlMigrationSeparator = sqlMigrationSeparator;
/* 117 */     this.sqlMigrationSuffix = sqlMigrationSuffix;
/*     */   }
/*     */   
/*     */   public List<ResolvedMigration> resolveMigrations() {
/* 121 */     List<ResolvedMigration> migrations = new ArrayList<ResolvedMigration>();
/*     */     
/* 123 */     scanForMigrations(migrations, this.sqlMigrationPrefix, this.sqlMigrationSeparator, this.sqlMigrationSuffix);
/* 124 */     scanForMigrations(migrations, this.repeatableSqlMigrationPrefix, this.sqlMigrationSeparator, this.sqlMigrationSuffix);
/*     */     
/* 126 */     Collections.sort(migrations, (Comparator<? super ResolvedMigration>)new ResolvedMigrationComparator());
/* 127 */     return migrations;
/*     */   }
/*     */   
/*     */   public void scanForMigrations(List<ResolvedMigration> migrations, String prefix, String separator, String suffix) {
/* 131 */     for (Resource resource : this.scanner.scanForResources(this.location, prefix, suffix)) {
/* 132 */       String filename = resource.getFilename();
/* 133 */       if (!isSqlCallback(filename, suffix)) {
/*     */ 
/*     */ 
/*     */         
/* 137 */         Pair<MigrationVersion, String> info = MigrationInfoHelper.extractVersionAndDescription(filename, prefix, separator, suffix);
/*     */         
/* 139 */         ResolvedMigrationImpl migration = new ResolvedMigrationImpl();
/* 140 */         migration.setVersion((MigrationVersion)info.getLeft());
/* 141 */         migration.setDescription((String)info.getRight());
/* 142 */         migration.setScript(extractScriptName(resource));
/* 143 */         migration.setChecksum(Integer.valueOf(calculateChecksum(resource, resource.loadAsString(this.encoding))));
/* 144 */         migration.setType(MigrationType.SQL);
/* 145 */         migration.setPhysicalLocation(resource.getLocationOnDisk());
/* 146 */         migration.setExecutor(new SqlMigrationExecutor(this.dbSupport, resource, this.placeholderReplacer, this.encoding));
/* 147 */         migrations.add(migration);
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
/*     */   static boolean isSqlCallback(String filename, String suffix) {
/* 160 */     String baseName = filename.substring(0, filename.length() - suffix.length());
/* 161 */     return SqlScriptFlywayCallback.ALL_CALLBACKS.contains(baseName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String extractScriptName(Resource resource) {
/* 171 */     if (this.location.getPath().isEmpty()) {
/* 172 */       return resource.getLocation();
/*     */     }
/*     */     
/* 175 */     return resource.getLocation().substring(this.location.getPath().length() + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int calculateChecksum(Resource resource, String str) {
/* 186 */     CRC32 crc32 = new CRC32();
/*     */     
/* 188 */     BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
/*     */     try {
/*     */       String line;
/* 191 */       while ((line = bufferedReader.readLine()) != null) {
/* 192 */         crc32.update(line.getBytes("UTF-8"));
/*     */       }
/* 194 */     } catch (IOException e) {
/* 195 */       String message = "Unable to calculate checksum";
/* 196 */       if (resource != null) {
/* 197 */         message = message + " for " + resource.getLocation() + " (" + resource.getLocationOnDisk() + ")";
/*     */       }
/* 199 */       throw new FlywayException(message, e);
/*     */     } 
/*     */     
/* 202 */     return (int)crc32.getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\sql\SqlMigrationResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */